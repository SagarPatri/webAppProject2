/**
 * @ (#) ClaimBatchDAOImpl.java July 7, 2015
 * Project 	     : ProjectX
 * File          : ClaimBatchDAOImpl.java
 * Author        : Nagababu K
 * Company       : RCS Technologies
 * Date Created  : July 7, 2015
 *
 * @author       :  Nagababu K
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.dao.impl.claims;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.claims.BatchVO;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;
import com.ttk.dto.preauth.PreAuthAssignmentVO;

public class ClaimBatchDAOImpl implements BaseDAO, Serializable{
	
	private static final long serialVersionUID = -1143723325198093673L;

	private static Logger log = Logger.getLogger(ClaimBatchDAOImpl.class );
	
	private static final String strSaveClaimBatchDetails="{CALL CLAIM_PKG.SAVE_CLM_BATCH_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetClaimBatchDetails="{CALL CLAIM_PKG.SELECT_CLM_BATCH_DETAILS(?,?,?)}";
	private static final String strAddRequestedAmount="{CALL CLAIM_PKG.ADD_BATCH_CLMS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetClaimAmountDetails="{CALL CLAIM_PKG.SELECT_CLAIM(?,?)}";
	private static final String strGetClaimBatchList="{CALL CLAIM_PKG.SELECT_CLM_BATCH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetClaimUploadLogList = "{CALL CLAIM_BULK_UPLOAD.GET_ERROR_LOG_LIST(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strDeleteInvoiceNO="{CALL CLAIM_PKG.DELETE_BATCH_CLAIMS(?,?,?)}";
	private static final String strgetPartnersList ="SELECT P.PTNR_SEQ_ID,P.PARTNER_NAME FROM APP.TPA_PARTNER_INFO P JOIN APP.TPA_PARTNER_EMPANEL_STATUS E ON (P.PTNR_SEQ_ID=E.PTNR_SEQ_ID) WHERE E.EMPANEL_STATUS_TYPE_ID = 'EMP'";
	private static final String strBatchAssignHistory="{CALL CLAIM_PKG.ASSIGN_BATCH_HISTORY(?,?)}";
	private static final String strGetBatchAssignHistoryList="{CALL CLAIM_PKG.SELECT_ASSIGN_BATCH_HISTORY(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetClaimAutoRectionList="{CALL claim_bulk_upload.SELECT_AUTO_REJ_CLM_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	private static final String strGetProviderId ="select HOSP_LICENC_NUMB from TPA_HOSP_INFO where HOSP_SEQ_ID=?";
	private static final String strClaimBatchReportDtls="{CALL claim_bulk_upload.batch_report_autoreject(?,?,?,?)}";
   
    public long saveClaimBatchDetails(BatchVO batchVO) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	long batchSeqID;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveClaimBatchDetails);
    		cStmtObject.setLong(1,batchVO.getBatchSeqID()==null?0:batchVO.getBatchSeqID());
    		cStmtObject.setString(2,batchVO.getBatchNO());
    		if("CNH".equals(batchVO.getClaimType())){
    			cStmtObject.setString(3,batchVO.getProviderID());
    			cStmtObject.setString(17,null);
    		}else{
    			cStmtObject.setString(3,null);
    			cStmtObject.setString(17,null);
    		}
    		cStmtObject.setString(4,null);
    		cStmtObject.setTimestamp(5,new java.sql.Timestamp(TTKCommon.getOracleDateWithTime(batchVO.getBatchReceiveDate(),batchVO.getReceivedTime(),batchVO.getReceiveDay()).getTime()));
    		cStmtObject.setInt(6,batchVO.getNoOfClaimsReceived()==null?0:batchVO.getNoOfClaimsReceived());
    		cStmtObject.setBigDecimal(7,batchVO.getTotalAmount());
    		cStmtObject.setString(8,batchVO.getVidalBranchName());
    		cStmtObject.setString(9,batchVO.getBatchStatus());
    		cStmtObject.setString(10,null);//cStmtObject.setString(10,batchVO.getBenefitType());
    		cStmtObject.setString(11,batchVO.getClaimType());
    		cStmtObject.setString(12,batchVO.getSubmissionType());
    		cStmtObject.setString(13,batchVO.getTotalAmountCurrency());
    		cStmtObject.setString(14,batchVO.getModeOfClaim());
    		cStmtObject.setString(15,batchVO.getOverrideYN());
    		cStmtObject.setString(16,batchVO.getNetWorkType());
    		cStmtObject.setString(18,batchVO.getClaimFrom());
    		cStmtObject.setLong(19,batchVO.getAddedBy());
    		cStmtObject.setString(20,batchVO.getProcessType());
    		cStmtObject.setString(21,batchVO.getPaymentTo());
    		cStmtObject.setString(22,batchVO.getPartnerName());
    		cStmtObject.registerOutParameter(1,OracleTypes.NUMBER);
    		cStmtObject.execute();
    		batchSeqID = cStmtObject.getLong(1);
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "batch");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "batch");
    	}//end of catch (Exception exp)
    	finally
    	{
    		/* Nested Try Catch to ensure resource closure */
    		try // First try closing the Statement
    		{
    			try
    			{
    				if (cStmtObject != null) cStmtObject.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Connection in ClaimBatchDAOImpl saveClaimBatchDetails()",sqlExp);
    				throw new TTKException(sqlExp, "batch");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    			{
    				try
    				{
    					if(conn != null) conn.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Connection in ClaimBatchDAOImpl saveClaimBatchDetails()",sqlExp);
    					throw new TTKException(sqlExp, "batch");
    				}//end of catch (SQLException sqlExp)
    			}//end of finally Connection Close
    		}//end of try
    		catch (TTKException exp)
    		{
    			throw new TTKException(exp, "batch");
    		}//end of catch (TTKException exp)
    		finally // Control will reach here in anycase set null to the objects
    		{
    			cStmtObject = null;
    			conn = null;
    		}//end of finally
    	}//end of finally
    	return batchSeqID;
    }//end of saveClaimBatchDetails(BatchVO)
    
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public long deleteInvoiceNO(String batchSeqID,String claimSeqID) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	long rowPro;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteInvoiceNO);
    		cStmtObject.setString(1,claimSeqID);
    		cStmtObject.setString(2,batchSeqID);
    		cStmtObject.registerOutParameter(3,OracleTypes.NUMBER);
    		cStmtObject.execute();
    		rowPro = cStmtObject.getLong(3);
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "batch");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "batch");
    	}//end of catch (Exception exp)
    	finally
    	{
    		/* Nested Try Catch to ensure resource closure */
    		try // First try closing the Statement
    		{
    			try
    			{
    				if (cStmtObject != null) cStmtObject.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Connection in ClaimBatchDAOImpl deleteInvoiceNO()",sqlExp);
    				throw new TTKException(sqlExp, "batch");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    			{
    				try
    				{
    					if(conn != null) conn.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Connection in ClaimBatchDAOImpl deleteInvoiceNO()",sqlExp);
    					throw new TTKException(sqlExp, "batch");
    				}//end of catch (SQLException sqlExp)
    			}//end of finally Connection Close
    		}//end of try
    		catch (TTKException exp)
    		{
    			throw new TTKException(exp, "batch");
    		}//end of catch (TTKException exp)
    		finally // Control will reach here in anycase set null to the objects
    		{
    			cStmtObject = null;
    			conn = null;
    		}//end of finally
    	}//end of finally
    	return rowPro;
    }//end of deleteInvoiceNO(String batchSeqID,String claimSeqID)
    
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public long addClaimDetails(BatchVO batchVO,ArrayList alFileAUploadList,InputStream inputStream,int formFileSize) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	long batchSeqID;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddRequestedAmount);
    		cStmtObject.setLong(1,batchVO.getClaimSeqID()==null?0:batchVO.getClaimSeqID());
    		cStmtObject.setLong(2,batchVO.getBatchSeqID()==null?0:batchVO.getBatchSeqID());
    		cStmtObject.setString(3,batchVO.getPreviousClaimNO());
    		cStmtObject.setTimestamp(4,new java.sql.Timestamp(TTKCommon.getOracleDateWithTime(batchVO.getBatchReceiveDate(),batchVO.getReceivedTime(),batchVO.getReceiveDay()).getTime()));
    		cStmtObject.setString(5,batchVO.getClaimType());
    		cStmtObject.setString(6,null);//cStmtObject.setString(6,batchVO.getBenefitType());
    		cStmtObject.setString(7,batchVO.getProviderInvoiceNO());
    		cStmtObject.setBigDecimal(8,batchVO.getRequestedAmount());
    		cStmtObject.setBigDecimal(9,batchVO.getAddedTotalAmount());
    		cStmtObject.setString(10,batchVO.getProviderID());
    		if(batchVO.getEnrollmentSeqID()==null)cStmtObject.setString(11,null);
    		else cStmtObject.setLong(11,batchVO.getEnrollmentSeqID());
    		cStmtObject.setString(12,batchVO.getEnrollmentID());
    		cStmtObject.setString(13,batchVO.getResubmissionRemarks());
    		cStmtObject.setLong(14,batchVO.getAddedBy());
    		cStmtObject.setString(15,batchVO.getPaymentTo());
    		cStmtObject.setString(16,batchVO.getPartnerName());
            cStmtObject.setString(17,(String) alFileAUploadList.get(0));//File Name
            cStmtObject.setString(18,(String) alFileAUploadList.get(1));// fileNameWithPath
            cStmtObject.setString(19,(String) alFileAUploadList.get(2));//DropDown Value
            cStmtObject.setBinaryStream(20,inputStream,formFileSize);//FILE INPUT STREAM   (image_file)
    		cStmtObject.registerOutParameter(2,OracleTypes.NUMBER);
    		cStmtObject.registerOutParameter(21,OracleTypes.NUMBER);
    		cStmtObject.execute();
    		batchSeqID = cStmtObject.getLong(2);
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "batch");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "batch");
    	}//end of catch (Exception exp)
    	finally
    	{
    		/* Nested Try Catch to ensure resource closure */
    		try // First try closing the Statement
    		{
    			try
    			{
    				if (cStmtObject != null) cStmtObject.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Connection in ClaimBatchDAOImpl addClaimDetails()",sqlExp);
    				throw new TTKException(sqlExp, "batch");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    			{
    				try
    				{
    					if(conn != null) conn.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Connection in ClaimBatchDAOImpl addClaimDetails()",sqlExp);
    					throw new TTKException(sqlExp, "batch");
    				}//end of catch (SQLException sqlExp)
    			}//end of finally Connection Close
    		}//end of try
    		catch (TTKException exp)
    		{
    			throw new TTKException(exp, "batch");
    		}//end of catch (TTKException exp)
    		finally // Control will reach here in anycase set null to the objects
    		{
    			cStmtObject = null;
    			conn = null;
    		}//end of finally
    	}//end of finally
    	return batchSeqID;
    }//end of addClaimDetails(BatchVO)
    
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public BatchVO getClaimAmountDetails(String claimSeqID) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet 	rs=null;
    	BatchVO batchVO=new BatchVO();
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimAmountDetails);
    		cStmtObject.setString(1,claimSeqID);
    		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
    		cStmtObject.execute();
    	rs =(ResultSet)cStmtObject.getObject(2);
    	if(rs!=null){
    		if(rs.next()){
    			batchVO.setRequestedAmount(rs.getBigDecimal("REQUESTED_AMOUNT"));
    			batchVO.setProviderInvoiceNO(rs.getString("INVOICE_NUMBER"));
    		}
    	}
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "batch");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "batch");
    	}//end of catch (Exception exp)
    	finally
    	{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimAmountDetails()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ClaimBatchDAOImpl getClaimAmountDetails()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ClaimBatchDAOImpl getClaimAmountDetails()",sqlExp);
							throw new TTKException(sqlExp, "batch");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "batch");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    	return batchVO;
    }//end of getClaimAmountDetails(String claimSeqID)
    /**
     * This method returns the Arraylist of ClaimBenefitVO's  which contains Claim Benefit Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimBenefitVO object which contains Claim Benefit details
     * @exception throws TTKException
     */
    public Object[] getClaimBatchDetails(long batchSeqID) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet batchVORs = null;
    	ResultSet batchDeatilsRs = null;
    	Object[] batchDeatils=new Object[2];
    	BatchVO batchVO = new BatchVO();
    	ArrayList<BatchVO> listOfAmounts=new ArrayList<BatchVO>();
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimBatchDetails);			
			cStmtObject.setLong(1,batchSeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			batchVORs = (java.sql.ResultSet)cStmtObject.getObject(2);
			batchDeatilsRs = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(batchVORs != null){
				if(batchVORs.next()){
					batchVO.setOfficeSeqID(batchVORs.getLong("TPA_OFFICE_SEQ_ID"));
					System.out.println("TPA_OFFICE_SEQ_ID::::::::::::::::"+batchVORs.getLong("TPA_OFFICE_SEQ_ID"));
					batchVO.setBatchSeqID(batchVORs.getLong("CLM_BATCH_SEQ_ID"));
					batchVO.setBatchNO(batchVORs.getString("BATCH_NO"));
					batchVO.setProviderName(batchVORs.getString("PROVIDER_NAME"));
					batchVO.setProviderID(batchVORs.getString("SENDER_ID"));
					batchVO.setProviderLisenceNO(batchVORs.getString("SENDER_ID"));
					batchVO.setNetWorkType(batchVORs.getString("NETWORK_YN"));
					batchVO.setNoOfClaimsReceived(batchVORs.getInt("RECORD_COUNT"));
					batchVO.setTotalAmount(batchVORs.getBigDecimal("BATCH_TOT_AMOUNT"));
					batchVO.setTotalAmountCurrency(batchVORs.getString("CURRENCY_TYPE"));
					batchVO.setVidalBranchName(batchVORs.getString("TPA_OFFICE_AEQ_ID"));
					batchVO.setBatchStatus(batchVORs.getString("BATCH_STATUS_TYPE"));
					batchVO.setBenefitType(batchVORs.getString("BENEFIT_TYPE"));
					batchVO.setClaimType(batchVORs.getString("CLM_TYPE_GEN_TYPE_ID"));
					batchVO.setSubmissionType(batchVORs.getString("SUBMISSION_TYPE_ID"));
					if(batchVORs.getString("RECEIVED_DATE") != null){
						batchVO.setBatchReceiveDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(batchVORs.getTimestamp("RECEIVED_DATE").getTime())));
						batchVO.setReceivedTime(TTKCommon.getFormattedDateHour(new Date(batchVORs.getTimestamp("RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(batchVORs.getTimestamp("RECEIVED_DATE").getTime())).split(" ")[1]:"");
						batchVO.setReceiveDay(TTKCommon.getFormattedDateHour(new Date(batchVORs.getTimestamp("RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(batchVORs.getTimestamp("RECEIVED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(batchVORs.getString("PAT_RECEIVED_DATE") != null)
					batchVO.setModeOfClaim(batchVORs.getString("SOURCE_TYPE_ID"));
					batchVO.setCompletedYN(batchVORs.getString("COMPLETED_YN"));
					batchVO.setClaimFrom(batchVORs.getString("CLAIM_FROM"));
					batchVO.setProcessType(batchVORs.getString("PROCESS_TYPE"));
					batchVO.setPartnerName(batchVORs.getString("PARTNER_NAME"));
					batchVO.setPaymentTo(batchVORs.getString("PYMNT_TO_TYPE_ID"));
					batchVO.setFastTrackFlag(batchVORs.getString("FAST_TRACK"));
					batchVO.setFastTrackMsg(batchVORs.getString("FAST_TRACK_MESSEGE"));
					batchVO.setBatchOverrideAllowYN(batchVORs.getString("ALLOW_YN"));
				}//end of if(batchVORs != null)
			}//end of if(batchVORs != null)
			if(batchDeatilsRs != null){
				while(batchDeatilsRs.next()){
					
					listOfAmounts.add(new BatchVO(batchDeatilsRs.getLong("CLAIM_SEQ_ID"),batchDeatilsRs.getString("INVOICE_NUMBER"),batchDeatilsRs.getString("CLAIM_NUMBER"),batchDeatilsRs.getBigDecimal("REQUESTED_AMOUNT"),batchDeatilsRs.getString("PARENT_CLAIM_NUMBER"),batchDeatilsRs.getLong("PARENT_CLAIM_SEQ_ID"),batchDeatilsRs.getString("TPA_ENROLLMENT_ID"),batchDeatilsRs.getLong("MEMBER_SEQ_ID"),batchDeatilsRs.getString("REMARKS")));
				}//end of if(batchDeatilsRs != null)
			}//end of if(batchDeatilsRs != null)
			batchDeatils[0]=batchVO;
			batchDeatils[1]=listOfAmounts;
    		return batchDeatils;
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "batch");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (batchVORs != null) batchVORs.close();
					if (batchDeatilsRs != null) batchDeatilsRs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchDetails()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ClaimBatchDAOImpl getClaimBatchDetails()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ClaimBatchDAOImpl getClaimBatchDetails()",sqlExp);
							throw new TTKException(sqlExp, "batch");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "batch");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				batchVORs = null;
				batchDeatilsRs=null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getClaimBatchDetails(long batchSeqID)
    /**
     * This method returns the Arraylist of ClaimBenefitVO's  which contains Claim Benefit Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimBenefitVO object which contains Claim Benefit details
     * @exception throws TTKException
     */
    public ArrayList<Object> getClaimBatchList(ArrayList<Object> alSearchCriteria) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	Collection<Object> alResultList = new ArrayList<Object>();
    	BatchVO batchVO = null;
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimBatchList);			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));			
			cStmtObject.setString(9,(String)alSearchCriteria.get(10));
			cStmtObject.setString(10,(String)alSearchCriteria.get(11));
			cStmtObject.setString(11,(String)alSearchCriteria.get(12));
			cStmtObject.setString(12,(String)alSearchCriteria.get(13));
			cStmtObject.setLong(13,(Long)alSearchCriteria.get(8));
			cStmtObject.setString(14,(String)alSearchCriteria.get(9));
			cStmtObject.registerOutParameter(15,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(15);
			
			if(rs != null){
				while(rs.next()){
					batchVO = new  BatchVO();
					if(rs.getString("CLM_BATCH_SEQ_ID") != null){
						batchVO.setBatchSeqID(new Long(rs.getLong("CLM_BATCH_SEQ_ID")));
					}//end of if(rs.getString("CLM_BATCH_SEQ_ID") != null)
					batchVO.setBatchNO(TTKCommon.checkNull(rs.getString("BATCH_NO")));
					batchVO.setProviderName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
					batchVO.setClaimType(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
					batchVO.setBatchReceiveDate(TTKCommon.getFormattedDate(rs.getDate("RECEIVED_DATE")));

				
					
					batchVO.setSubmissionType(rs.getString("SUBMISSION_TYPE"));
					batchVO.setProcessType(rs.getString("PROCESS_TYPE"));
					batchVO.setFastTrackFlag(rs.getString("FASTTRACK_YN"));
					batchVO.setFastTrackMsg(rs.getString("FASTTRACK_MESG"));			
					 if("Y".equals(batchVO.getFastTrackFlag()))
	        			{
						 batchVO.setFastTrackFlagImageName("fastTrackImg");
						 batchVO.setFastTrackFlagImageTitle(""+rs.getString("FASTTRACK_MESG"));
	                    }
						if(rs.getString("TPA_OFFICE_SEQ_ID") != null){
							batchVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
						}//end of if(rs.getString("TPA_OFFICE_SEQ_ID") != null)
						if(rs.getString("BATCH_ASSIGN_SEQ") != null){
							batchVO.setAssignUserSeqID(new Long(rs.getLong("BATCH_ASSIGN_SEQ")));
						}//end of if(rs.getString("BATCH_ASSIGN_SEQ") != null)
					alResultList.add(batchVO);
				}//end of if(rs != null)
			}//end of if(rs != null)
    		return (ArrayList<Object>)alResultList;
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "batch");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimBenefitDAOImpl getClaimBatchList()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{	
						log.error("Error while closing the Statement in ClaimBenefitDAOImpl getClaimBatchList()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ClaimBenefitDAOImpl getClaimBatchList()",sqlExp);
							throw new TTKException(sqlExp, "batch");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "batch");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getClaimBatchList(ArrayList alSearchCriteria)
    
    //getClaimUploadErrorLogList
    /**
     * This method returns the Arraylist of ClaimUploadErrorLogVO's  which contains Claim Benefit Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimUploadErrorLogVO object which contains Claim Benefit details
     * @exception throws TTKException
     */
    public ArrayList<Object> getClaimUploadErrorLogList(ArrayList<Object> alSearchCriteria) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	Collection<Object> alResultList = new ArrayList<Object>();
    	ClaimUploadErrorLogVO claimUploadErrorLogVO = null;
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimUploadLogList);			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));		
			cStmtObject.setString(10,(String)alSearchCriteria.get(9));		
			cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(11);
			
			if(rs != null){
				while(rs.next()){
					claimUploadErrorLogVO= new  ClaimUploadErrorLogVO();
					if(rs.getString("XML_SEQ_ID") != null){
						claimUploadErrorLogVO.setReferenceNo(""+new Long(rs.getLong("XML_SEQ_ID")));
					}
					if(rs.getString("SOURCE_TYPE") != null){
					     claimUploadErrorLogVO.setSourceTypeId(TTKCommon.checkNull(rs.getString("SOURCE_TYPE")));
					}
					claimUploadErrorLogVO.setFileName(TTKCommon.checkNull(rs.getString("FILE_NAME")));
					claimUploadErrorLogVO.setStrAddedDate(TTKCommon.checkNull(rs.getString("ADDED_DATE")));
					alResultList.add(claimUploadErrorLogVO);
				}//end of if(rs != null)
			}//end of if(rs != null)
    		return (ArrayList<Object>)alResultList;
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "batch");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchList()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{	
						log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchList()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchList()",sqlExp);
							throw new TTKException(sqlExp, "batch");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "batch");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getClaimBatchList(ArrayList alSearchCriteria)
    
    /**
     * This method returns the HashMap of String,String  which contains Network Types details
     * @return HashMap of String object which contains Network Types details
     * @exception throws TTKException
     */
public HashMap<String,String> getPartnersList() throws TTKException {
		
		Connection conn = null;
		ResultSet rs = null;
		HashMap<String,String> partners=new HashMap<>();
		PreparedStatement pStmt 		= 	null;
        try {
            conn = ResourceManager.getConnection();
            pStmt=conn.prepareStatement(strgetPartnersList);
            rs = pStmt.executeQuery();
			if(rs != null){
				while(rs.next()){
					partners.put(rs.getString("PTNR_SEQ_ID"),rs.getString("PARTNER_NAME"));
				}//end of while(rs.next())
			}//end of if(rs != null)
			return partners;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "batch");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getNetworkTypeList()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PreAuthDAOImpl getNetworkTypeList()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getNetworkTypeList()",sqlExp);
							throw new TTKException(sqlExp, "batch");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "batch");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getNetworkTypeList()


public ArrayList getBatchAssignHistory(long batchSeqID)throws TTKException{		
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	PreAuthAssignmentVO preAuthAssignmentVO	=	null;
	try
	{
		ArrayList<Object> alBatchAssignHistory	=	new ArrayList<Object>();
        conn = ResourceManager.getConnection();
        cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strBatchAssignHistory);
        cStmtObject.setLong(1,batchSeqID);
        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        cStmtObject.execute();
		rs = (java.sql.ResultSet)cStmtObject.getObject(2);
		if(rs != null){
			while(rs.next()){
				preAuthAssignmentVO	=	new PreAuthAssignmentVO();
				preAuthAssignmentVO.setAssignTo(rs.getString("ASSIGNED_TO"));
				preAuthAssignmentVO.setAssignDate(rs.getString("ASSIGNED_DATE"));
				preAuthAssignmentVO.setAssignBy(rs.getString("ASSINGED_BY"));				
				preAuthAssignmentVO.setAssignRemarks(rs.getString("ASSIGN_REMARKS"));
				preAuthAssignmentVO.setOtherRemarks(rs.getString("OTHER_REMARKS"));
				alBatchAssignHistory.add(preAuthAssignmentVO);
			}
		}
		return alBatchAssignHistory;
	}//end of try
	
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "hospital");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "hospital");
	}//end of catch (Exception exp)
	finally
	{
		/* Nested Try Catch to ensure resource closure */ 
		try // First try closing the result set
		{
			try
			{
				if (rs != null) rs.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in ClaimBatchDAOImpl getBatchAssignHistory()",sqlExp);
				throw new TTKException(sqlExp, "hospital");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimBatchDAOImpl getBatchAssignHistory()",sqlExp);
					throw new TTKException(sqlExp, "hospital");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in ClaimBatchDAOImpl getBatchAssignHistory()",sqlExp);
						throw new TTKException(sqlExp, "hospital");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "hospital");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
}


public ArrayList<Object> getClaimBatchHistoryList(ArrayList<Object> alSearchCriteria) throws TTKException{
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	Collection<Object> alResultList = new ArrayList<Object>();
	PreAuthAssignmentVO preAuthAssignmentVO = null;
	int i=0;
/*	for(i=0; i < alSearchCriteria.size();i++){
			System.out.println(i+" value    "+alSearchCriteria.get(i));
	}*/
	try{
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetBatchAssignHistoryList);			
		cStmtObject.setString(1,(String)alSearchCriteria.get(0));
		cStmtObject.setString(2,(String)alSearchCriteria.get(1));
		cStmtObject.setString(3,(String)alSearchCriteria.get(2));
		cStmtObject.setString(4,(String)alSearchCriteria.get(3));
		cStmtObject.setString(5,(String)alSearchCriteria.get(4));
		cStmtObject.setString(6,(String)alSearchCriteria.get(5));
		cStmtObject.setString(7,(String)alSearchCriteria.get(6));
		cStmtObject.setString(8,(String)alSearchCriteria.get(7));			
		cStmtObject.setString(9,(String)alSearchCriteria.get(8));
		cStmtObject.setString(10,(String)alSearchCriteria.get(9));
		cStmtObject.setString(11,(String)alSearchCriteria.get(10));
		cStmtObject.setString(12,(String)alSearchCriteria.get(11));
		cStmtObject.setString(13,(String)alSearchCriteria.get(12));
		cStmtObject.registerOutParameter(14,OracleTypes.CURSOR);
		cStmtObject.execute();
		rs = (java.sql.ResultSet)cStmtObject.getObject(14);
		
		if(rs != null){
			while(rs.next()){
				preAuthAssignmentVO = new  PreAuthAssignmentVO();
				preAuthAssignmentVO.setBatchNO(TTKCommon.checkNull(rs.getString("BATCH_NO")));
				preAuthAssignmentVO.setProviderName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
				preAuthAssignmentVO.setClaimType(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
				preAuthAssignmentVO.setSubmissionType(rs.getString("SUBMISSION_TYPE"));	
				preAuthAssignmentVO.setAssignTo(rs.getString("ASSIGNED_TO"));
				preAuthAssignmentVO.setAssignDate(rs.getString("ASSIGNED_DATE"));	
				preAuthAssignmentVO.setAssignBy(rs.getString("ASSIGNED_BY"));
				alResultList.add(preAuthAssignmentVO);
			}//end of if(rs != null)
		}//end of if(rs != null)
		return (ArrayList<Object>)alResultList;
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "batch");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "batch");
	}//end of catch (Exception exp)
	finally
	{
		/* Nested Try Catch to ensure resource closure */
		try // First try closing the result set
		{
			try
			{
				if (rs != null) rs.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimBatchHistoryList()",sqlExp);
				throw new TTKException(sqlExp, "batch");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{	
					log.error("Error while closing the Statement in ClaimBatchDAOImpl getClaimBatchHistoryList()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in ClaimBatchDAOImpl getClaimBatchHistoryList()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
}//end of getClaimBatchList(ArrayList alSearchCriteria)

public ArrayList getClaimAutoRejectionList(ArrayList<Object> alSearchCriteria) throws TTKException{

	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	Collection<Object> alResultList = new ArrayList<Object>();
	ClaimUploadErrorLogVO claimUploadErrorLogVO = null;
	System.out.println("alSearchCriteria===>"+alSearchCriteria);
	try{
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimAutoRectionList);			
		cStmtObject.setString(1,(String)alSearchCriteria.get(0));
		cStmtObject.setString(2,(String)alSearchCriteria.get(1));
		cStmtObject.setString(3,(String)alSearchCriteria.get(2));
		cStmtObject.setString(4,(String)alSearchCriteria.get(3));
		cStmtObject.setString(5,(String)alSearchCriteria.get(4));
		cStmtObject.setString(6,(String)alSearchCriteria.get(5));
		cStmtObject.setString(7,(String)alSearchCriteria.get(6));
		cStmtObject.setString(8,(String)alSearchCriteria.get(7));
		cStmtObject.setString(9,(String)alSearchCriteria.get(8));	
		cStmtObject.setString(10,(String)alSearchCriteria.get(9));//member id
		cStmtObject.setString(11,(String)alSearchCriteria.get(10));
		cStmtObject.setString(12,(String)alSearchCriteria.get(11));
		cStmtObject.setString(13,(String)alSearchCriteria.get(12));
		cStmtObject.registerOutParameter(14,OracleTypes.CURSOR);
		cStmtObject.execute();
		rs = (java.sql.ResultSet)cStmtObject.getObject(14);
		
		if(rs != null){
			while(rs.next()){
				claimUploadErrorLogVO= new  ClaimUploadErrorLogVO();
				
				
				//claimUploadErrorLogVO.setInvoiceNo(rs.getString("BATCH_NO"));
				claimUploadErrorLogVO.setBatchNO(rs.getString("BATCH_NO"));
				claimUploadErrorLogVO.setParentClaimNo(rs.getString("Parent_Claim_No"));
				//claimUploadErrorLogVO.setAlkootId(rs.getString("BATCH_NO"));
				claimUploadErrorLogVO.setClaimAge(rs.getString("claim_age"));
				claimUploadErrorLogVO.setProviderName(rs.getString("provider_name"));
				//claimUploadErrorLogVO.setMemberName(rs.getString("BATCH_NO"));
				//claimUploadErrorLogVO.setClaimType(rs.getString("BATCH_NO"));
				claimUploadErrorLogVO.setiBatchRefNO(rs.getString("batch_ref_no"));
				
				claimUploadErrorLogVO.setStatus(rs.getString("status"));
				claimUploadErrorLogVO.setXmlSeqId(rs.getString("XML_SEQ_ID"));
				claimUploadErrorLogVO.setHospSeqId(rs.getString("hosp_seq_id"));
				claimUploadErrorLogVO.setStrAddedDate(rs.getString("added_date"));
				claimUploadErrorLogVO.setClaimType(rs.getString("claim_type"));
				String recDateTime=TTKCommon.checkNull(rs.getString("received_date")) ;
				String[] recDate=recDateTime.split(" ");
				claimUploadErrorLogVO.setRecDate(recDate[0]);
				claimUploadErrorLogVO.setRejectionReason(rs.getString("rej_remarks"));
				alResultList.add(claimUploadErrorLogVO);
			}
		}
		
		return (ArrayList<Object>)alResultList;
	}//end of try
	/*catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "batch");
	}//end of catch (SQLException sqlExp)
	*/
	catch (Exception exp)
	{
		throw new TTKException(exp, "batch");
	}//end of catch (Exception exp)
	finally
	{
		/* Nested Try Catch to ensure resource closure */
		try // First try closing the result set
		{
			try
			{
				if (rs != null) rs.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimAutoRejectionList()",sqlExp);
				throw new TTKException(sqlExp, "batch");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{	
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimAutoRejectionList()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimBatchDAOImpl getClaimAutoRejectionList()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
}

public String getProviderId(String providerseqid) throws TTKException{
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	String providerId=null;
	PreparedStatement statement=null;
	//RahulSingh
	//PlanVO planVO = new PlanVO();
	try{
		conn = ResourceManager.getConnection();
		/*cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetProviderId);
		cStmtObject.setString(1, providerseqid);
		cStmtObject.registerOutParameter(2, Types.VARCHAR);
		cStmtObject.execute();
		providerId = cStmtObject.getString(3);*/
		
		
		conn=ResourceManager.getConnection();
		statement=conn.prepareStatement("select HOSP_LICENC_NUMB from TPA_HOSP_INFO where HOSP_SEQ_ID="+providerseqid);		
	      rs=statement.executeQuery();
	      if(rs.next()){
	    	  providerId=rs.getString(1);
	      }
		
		return providerId;
	}//end of try
	catch (Exception exp)
	{
		throw new TTKException(exp, "batch");
	}//end of catch (Exception exp)
	finally
	{
		/* Nested Try Catch to ensure resource closure */
		try // First try closing the result set
		{
			try
			{
				if (rs != null) rs.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in ClaimBatchDAOImpl getProviderId()",sqlExp);
				throw new TTKException(sqlExp, "batch");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{	
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getProviderId()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimBatchDAOImpl getProviderId()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally			
}

public TTKReportDataSource getAutoRejectClaimBatchReport(String parameter) throws TTKException {
	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	ResultSet rsDetail = null;
	OracleCachedRowSet crs = null;
    TTKReportDataSource reportDataSource =null;
    String[] inputParams=parameter.split("\\|");
    
    try {
    	conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimBatchReportDtls);
		cStmtObject.setString(1,inputParams[0]);//batch no
		cStmtObject.setString(2,inputParams[1]);// xmlSeqId
        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
    	cStmtObject.execute();
       rs = (java.sql.ResultSet)cStmtObject.getObject(3);
       rsDetail = (java.sql.ResultSet)cStmtObject.getObject(4);
       crs = new OracleCachedRowSet();
    	
       if(rs !=null)
		{
			reportDataSource = new TTKReportDataSource();
			crs.populate(rs);
			reportDataSource.setData(crs);
		}//end of if(rs !=null)
    	
    	
	} catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "batch");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "batch");
	}//end of catch (Exception exp)
	finally
	{
		/* Nested Try Catch to ensure resource closure */
		try // First try closing the result set
		{
			try
			{
				if (rs != null) rs.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
				throw new TTKException(sqlExp, "batch");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return reportDataSource;
}

public TTKReportDataSource getAutoRejectClaimBatchDetailReport(String parameter) throws TTKException {
	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	ResultSet rsDetail = null;
	OracleCachedRowSet crs = null;
    TTKReportDataSource reportDataSource =null;
    String[] inputParams=parameter.split("\\|");
    
    try {
    	conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimBatchReportDtls);
		cStmtObject.setString(1,inputParams[0]);//batch no
		cStmtObject.setString(2,inputParams[1]);// xmlSeqId
        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
    	cStmtObject.execute();
       rs = (java.sql.ResultSet)cStmtObject.getObject(3);
       rsDetail = (java.sql.ResultSet)cStmtObject.getObject(4);
       crs = new OracleCachedRowSet();
    	
       if(rs !=null)
		{
			reportDataSource = new TTKReportDataSource();
			crs.populate(rs);
			reportDataSource.setData(crs);
		}//end of if(rs !=null)
    	
    	
	} catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "batch");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "batch");
	}//end of catch (Exception exp)
	finally
	{
		/* Nested Try Catch to ensure resource closure */
		try // First try closing the result set
		{
			try
			{
				if (rs != null) rs.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in ClaimBatchDAOImpl getAutoRejectClaimBatchDetailReport()",sqlExp);
				throw new TTKException(sqlExp, "batch");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimBatchDAOImpl getAutoRejectClaimBatchDetailReport()",sqlExp);
					throw new TTKException(sqlExp, "batch");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in ClaimBatchDAOImpl getAutoRejectClaimBatchDetailReport()",sqlExp);
						throw new TTKException(sqlExp, "batch");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "batch");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return reportDataSource;
}


}//end of ClaimBatchDAOImpl
