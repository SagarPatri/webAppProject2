/**
 *   @ (#) ChequeDAOImpl.java June 07, 2006
 *   Project      : TTK HealthCare Services
 *   File         : ChequeDAOImpl.java
 *   Author       : Balakrishna E
 *   Company      : Span Systems Corporation
 *   Date Created : June 07, 2006
 *
 *   @author       :  Balakrishna E
 *   Modified by   :
 *   Modified date :
 *   Reason        :
 */

package com.ttk.dao.impl.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.rowset.OracleCachedRowSet;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.BankAddressVO;
import com.ttk.dto.finance.ChequeDetailVO;
import com.ttk.dto.finance.ChequeVO;
import com.ttk.dto.preauth.PreAuthDetailVO;

public class ChequeDAOImpl implements BaseDAO, Serializable {

	private static Logger log = Logger.getLogger(ChequeDAOImpl.class);
	
	private static final String strChequeList = "{CALL CHEQUE_INFO_PKG.SELECT_CHQ_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strChequeDetail = "{CALL CHEQUE_INFO_PKG.SELECT_CHQ_DETAIL(?,?,?,?,?)}";
	private static final String strSaveCheque = "{CALL CHEQUE_INFO_PKG.CHQ_DETAILS_SAVE(?,?,?,?,?,?)}";
	private static final String strUpdateChequeInfo = "{CALL FIN_MAINTENENACE_PKG.CHEQUE_NUM_DATE_CHANGE(?,?,?,?,?,?,?,?)}";

	 private static final String strGetBankUploadFils = "select pc.docs_seq_id,pc.pat_clm_seq_id,g.description as file_desc,pc.file_name,to_char(pc.added_date, 'DD/MM/RRRR HH24:MI:SS') added_date,initcap(uc.contact_name) as contact_name,pc.file_path from pat_clm_docs_tab pc join clm_authorization_details c on (c.claim_seq_id = pc.pat_clm_seq_id) join tpa_user_contacts uc on (uc.contact_seq_id = pc.added_by) join tpa_general_code g on (g.general_type_id = pc.file_desc) join tpa_claims_payment t on (t.claim_seq_id = c.claim_seq_id) where g.general_type_id = 'BDD' and t.claim_settlement_no = ? order by pc.added_date desc ";
/*	private static final String strDeleteDocsUpload          =    "{CALL AUTHORIZATION_PKG. delete_pat_clm_upld_details (?,?)}";
    private static final String strDeleteTdsCertificates		=	"{CALL AUTHORIZATION_PKG. delete_pat_clm_upld_details (?,?)}";*/
	
	
	 
	 
	 private static final String strPaymentSummaryRpt = "{CALL BATCH_REPORT_PKG.clm_pending_summary_report(?,?,?,?)}";
	 private static final String strgetPartnersList ="SELECT P.PTNR_SEQ_ID,P.PARTNER_NAME FROM APP.TPA_PARTNER_INFO P JOIN APP.TPA_PARTNER_EMPANEL_STATUS E ON (P.PTNR_SEQ_ID=E.PTNR_SEQ_ID) WHERE E.EMPANEL_STATUS_TYPE_ID = 'EMP'";
	 
	/**
	 * This method returns the ArrayList, which contains the ChequeVO's which are populated from the database
	 * @param alSearchCriteria ArrayList which contains Search Criteria
	 * @return ArrayList of ChequeVO'S object's which contains the details of the cheque
	 * @exception throws TTKException
	 */
	public ArrayList getChequeList(ArrayList alSearchCriteria) throws TTKException
	{
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ChequeVO chequeVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strChequeList);
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
			cStmtObject.setString(14,(String)alSearchCriteria.get(15));
			cStmtObject.setString(15,(String)alSearchCriteria.get(16));
			cStmtObject.setString(16,(String)alSearchCriteria.get(17));
			cStmtObject.setString(17,(String)alSearchCriteria.get(18));
			cStmtObject.setLong(18,(Long)alSearchCriteria.get(13));
			cStmtObject.setString(19,(String)alSearchCriteria.get(14));
			cStmtObject.registerOutParameter(20,OracleTypes.CURSOR);

			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(20);

			if(rs != null){
				while(rs.next()){
					chequeVO = new ChequeVO();

					if(rs.getString("CLAIMS_CHK_SEQ_ID") != null){
						chequeVO.setSeqID(new Long(rs.getString("CLAIMS_CHK_SEQ_ID")));
					}//end of if(rs.getString("CLAIMS_CHK_SEQ_ID") != null)

					chequeVO.setChequeNo(TTKCommon.checkNull(rs.getString("CHECK_NUM")));
					chequeVO.setFloatAcctNo(TTKCommon.checkNull(rs.getString("FLOAT_ACCOUNT_NUMBER")));
					chequeVO.setStatusDesc(TTKCommon.checkNull(rs.getString("CHECK_STATUS")));
					if(rs.getTimestamp("CHECK_DATE") != null){
						chequeVO.setChequeDate(new Date(rs.getTimestamp("CHECK_DATE").getTime()));
					}//end of if(rs.getTimestamp("CHECK_DATE") != null)

					chequeVO.setClaimSettNo(TTKCommon.checkNull(rs.getString("CLAIM_SETTLEMENT_NO")));
					chequeVO.setClaimTypeDesc(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
					chequeVO.setInsCompName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					chequeVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
					chequeVO.setPaymentSeqId(new Long(rs.getString("PAYMENT_SEQ_ID")));
					
					//chequeVO.setChequeBatchDates(TTKCommon.checkNull(rs.getString("BATCH_DATE")));
					
					if(rs.getTimestamp("BATCH_DATE") != null){
						chequeVO.setChequeBatchDates(new Date(rs.getTimestamp("BATCH_DATE").getTime()));
					}//end of if(rs.getTimestamp("BATCH_DATE") != null)				
					chequeVO.setIbanNo(TTKCommon.checkNull(rs.getString("IBAN_NO")));
					chequeVO.setPaidCurrency(TTKCommon.checkNull(rs.getString("TRANSFER_CURRENCY")));
					chequeVO.setAmountPaid(rs.getBigDecimal("APPROVED_AMOUNT"));
					alResultList.add(chequeVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}// end of try
		catch (SQLException sqlExp)
		 {
			throw new TTKException(sqlExp, "cheque");
		 }//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
					log.error("Error while closing the Resultset in ChequeDAOImpl getChequeList()",sqlExp);
					throw new TTKException(sqlExp, "cheque");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ChequeDAOImpl getChequeList()",sqlExp);
						throw new TTKException(sqlExp, "cheque");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ChequeDAOImpl getChequeList()",sqlExp);
							throw new TTKException(sqlExp, "cheque");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "cheque");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getChequeList(ArrayList alSearchCriteria)

	/**
	 * This method returns the ChequeDetailVO which contains the Cheque Detail information
	 * @param lngSeqID long value which contains cheque seq id to get the Cheque Detail information
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return ChequeDetailVO this contains the Cheque Detail information
	 * @exception throws TTKException
	 */
	public ChequeDetailVO getChequeDetail(Long lngSeqID, Long lngPaymentSeqId, Long lngFloatSeqID, Long lngUserSeqID) throws TTKException
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ChequeDetailVO chequeDetailVO = null;
        BankAddressVO bankAddressVO = null;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strChequeDetail);
			cStmtObject.setLong(1, lngSeqID);
			if(lngPaymentSeqId!=0)
			{
				cStmtObject.setLong(2, lngPaymentSeqId);
			}
			else
			{
				cStmtObject.setString(2, null);
			}
            if(lngFloatSeqID!=null)
            {
                cStmtObject.setLong(3, lngFloatSeqID);
            }//end of if(lngFloatSeqID!=null)
            else
            {
                cStmtObject.setString(3, null);
            }//end of else
			cStmtObject.setLong(4, lngUserSeqID);
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(5);
			if(rs != null){
				while(rs.next()){
					chequeDetailVO = new ChequeDetailVO();
					bankAddressVO = new BankAddressVO();
                    if(lngFloatSeqID==null)
                    {
    					chequeDetailVO.setChequeNo(TTKCommon.checkNull(rs.getString("CHECK_NUM")));
    					if(rs.getString("CLAIMS_CHK_SEQ_ID") !=null) {
    						chequeDetailVO.setSeqID(new Long(rs.getString("CLAIMS_CHK_SEQ_ID")));
    					}//end of if(rs.getString("CLAIMS_CHK_SEQ_ID") !=null)

                        if(rs.getString("CHECK_AMOUNT") != null) {
                            chequeDetailVO.setChequeAmt(new BigDecimal(rs.getString("CHECK_AMOUNT")));
                        }//end of if(rs.getString("CHECK_AMOUNT") != null)
                        chequeDetailVO.setFloatAcctNo(TTKCommon.checkNull(rs.getString("FLOAT_ACCOUNT_NUMBER")));
                        chequeDetailVO.setStatusTypeID(TTKCommon.checkNull(rs.getString("CHECK_STATUS")));

                        if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSI"))
                        {
                        	 if(rs.getTimestamp("CHECK_DATE") !=null)
                        	 {
                                chequeDetailVO.setClearedDate(rs.getTimestamp("CHECK_DATE"));
                             }//end of if(rs.getTimestamp("CLEARED_DATE") !=null)
                        }//end of if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSI"))

                        if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSV")||TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSR"))
                        {
                        	if(rs.getTimestamp("VOID_DATE") !=null)
                       	 	{
                               chequeDetailVO.setClearedDate(rs.getTimestamp("VOID_DATE"));
                            }//end of if(rs.getTimestamp("CLEARED_DATE") !=null)
                        }//end of if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSV")||TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSR"))
                        if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSC"))
                        {
                        	 if(rs.getTimestamp("CLEARED_DATE") !=null)
                        	 {
                                chequeDetailVO.setClearedDate(rs.getTimestamp("CLEARED_DATE"));
                             }//end of if(rs.getTimestamp("CLEARED_DATE") !=null)
                        }//end of if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSC"))
 			//Changes as per STALE...................
                        if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSS"))
                        {
                        	 if(rs.getTimestamp("STALE_DATE") !=null)
                        	 {
                                chequeDetailVO.setClearedDate(rs.getTimestamp("STALE_DATE"));
                                
                             }//end of if(rs.getTimestamp("CLEARED_DATE") !=null)
                        }//end of if(TTKCommon.checkNull(rs.getString("CHECK_STATUS")).equalsIgnoreCase("CSS"))
                    }//end of if(lngFloatSeqID!=null)
                    else
                    {
                    	chequeDetailVO.setSeqID(lngSeqID);
                    }//end of else

                    chequeDetailVO.setClaimTypeDesc(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
                    chequeDetailVO.setPolicyType(TTKCommon.checkNull(rs.getString("ENROL_DESCRIPTION")));
                    if(rs.getString("APPROVED_AMOUNT") != null) {
                        chequeDetailVO.setClaimAmt(new BigDecimal(rs.getString("APPROVED_AMOUNT")));
                    }//end of if(rs.getString("APPROVED_AMOUNT") != null)

					if(rs.getTimestamp("CHECK_DATE") != null){
						chequeDetailVO.setChequeDate(new Date(rs.getTimestamp("CHECK_DATE").getTime()));
					}//end of if(rs.getTimestamp("CHECK_DATE") != null)
		
					/* ADDED AS PER STALE CHANGE REQUEST
					 * 
					 *TTKCommon.getDate()if(rs.getTimestamp("CURRENT_DATE") != null) {
					chequeDetailVO.setCurentDate(new Date(rs.getTimestamp("CURRENT_DATE").getTime()));
				}//end of if(rs.getTimestamp("CURRENT_DATE") != null)
					*/ 
					
						chequeDetailVO.setCurrentDate(TTKCommon.getDate());
				
					chequeDetailVO.setInFavorOf(TTKCommon.checkNull(rs.getString("IN_FAVOUR_OF")));
					chequeDetailVO.setClaimSettNo(TTKCommon.checkNull(rs.getString("CLAIM_SETTLEMENT_NO")));

				    //added as per KOC 1175 Eft Change Request
					chequeDetailVO.setPaymentType(TTKCommon.checkNull(rs.getString("PAYMENT_METHOD")));
			        //added as per KOC 1175 Eft Change Request
					if(rs.getTimestamp("CLAIM_APRV_DATE") != null) {
						chequeDetailVO.setApprDate(new Date(rs.getTimestamp("CLAIM_APRV_DATE").getTime()));
					}//end of if(rs.getTimestamp("CLAIM_APRV_DATE") != null)

					chequeDetailVO.setEnrollID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					chequeDetailVO.setClaimantName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
					chequeDetailVO.setPolicyNo(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
					chequeDetailVO.setInsCompName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					chequeDetailVO.setInsCompCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
					chequeDetailVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));
					chequeDetailVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					chequeDetailVO.setRemarks(TTKCommon.checkNull(rs.getString("COMMENTS")));
                    bankAddressVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS1")));
                    bankAddressVO.setAddress2(TTKCommon.checkNull(rs.getString("ADDRESS2")));
                    bankAddressVO.setAddress3(TTKCommon.checkNull(rs.getString("ADDRESS3")));
                    bankAddressVO.setCityDesc(TTKCommon.checkNull(rs.getString("CITY")));
                    bankAddressVO.setStateName(TTKCommon.checkNull(rs.getString("STATE")));
                    bankAddressVO.setCountryName(TTKCommon.checkNull(rs.getString("COUNTRY")));
                    bankAddressVO.setPinCode(TTKCommon.checkNull(rs.getString("PINCODE")));
                    bankAddressVO.setEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
                    bankAddressVO.setOffPhone1(TTKCommon.checkNull(rs.getString("PHONE1")));
                    bankAddressVO.setOffPhone2(TTKCommon.checkNull(rs.getString("PHONE2")));
                    bankAddressVO.setHomePhone(TTKCommon.checkNull(rs.getString("HOME_PHONE")));
                    bankAddressVO.setMobile(TTKCommon.checkNull(rs.getString("MOB_NUM")));
                    bankAddressVO.setFax(TTKCommon.checkNull(rs.getString("FAX_NO")));
                    chequeDetailVO.setBankAddressVO(bankAddressVO);
                    chequeDetailVO.setTransferCurrency(TTKCommon.checkNull(rs.getString("TRANSFER_CURRENCY")));
                    if(rs.getString("TRANSFERED_AMT") != null) {
                        chequeDetailVO.setTransferedAmt(new BigDecimal(rs.getString("TRANSFERED_AMT")));
                    }//end of if(rs.getString("TRANSFERED_AMT") != null)
                    chequeDetailVO.setClaimSeqId(rs.getLong("claim_seq_id"));
				}//end of while(rs.next())
			}//end of if(rs != null)
			return chequeDetailVO;
		}// end of try
		catch (SQLException sqlExp)
		 {
			throw new TTKException(sqlExp, "cheque");
		 }//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
					log.error("Error while closing the Resultset in ChequeDAOImpl getChequeDetail()",sqlExp);
					throw new TTKException(sqlExp, "cheque");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ChequeDAOImpl getChequeDetail()",sqlExp);
						throw new TTKException(sqlExp, "cheque");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ChequeDAOImpl getChequeDetail()",sqlExp);
							throw new TTKException(sqlExp, "cheque");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "cheque");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getChequeDetail(long lngSeqID, long lngUserSeqID)

	/**
	 * This method saves the Cheque Detail information
	 * @param chequeDetailVO the object which contains the details of the Cheque
	 * @return long value which contains Cheque Seq ID
	 * @exception throws TTKException
	 */
	public long saveCheque(ChequeDetailVO chequeDetailVO) throws TTKException
	{
		long lngChequeSeqID = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCheque);
			if(chequeDetailVO.getSeqID() != null){
				cStmtObject.setLong(1,(Long)chequeDetailVO.getSeqID());
			}//end of if(chequeDetailVO.getSeqID() != null)
			else {
				cStmtObject.setString(1,null);
			}//end of else

			cStmtObject.setString(2,(String)chequeDetailVO.getStatusTypeID());
			cStmtObject.setTimestamp(3,(Date)chequeDetailVO.getClearedDate()!=null ? new Timestamp(chequeDetailVO.getClearedDate().getTime()):null);
			cStmtObject.setString(4,(String)chequeDetailVO.getRemarks());
			cStmtObject.setLong(5,(Long)chequeDetailVO.getUpdatedBy());
			cStmtObject.registerOutParameter(6,Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.registerOutParameter(1,Types.BIGINT);
			cStmtObject.execute();
			lngChequeSeqID = cStmtObject.getLong(1);
		}// end of try
		catch (SQLException sqlExp)
		 {
			throw new TTKException(sqlExp, "cheque");
		 }//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
        			log.error("Error while closing the Statement in ChequeDAOImpl saveCheque()",sqlExp);
        			throw new TTKException(sqlExp, "cheque");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ChequeDAOImpl saveCheque()",sqlExp);
        				throw new TTKException(sqlExp, "cheque");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "cheque");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return lngChequeSeqID;
	}//end of saveCheque(ChequeDetailVO chequeDetailVO)
	
	/**
	 * This method update the Cheque Detail information from maintenance-finance.
	 * @param chequeVO the object which contains the details of the Cheque
	 * @return int value which contains result
	 * @exception throws TTKException
	 */
	public int updateChequeInfo(ChequeVO chequeVO) throws TTKException
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		int iResult;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strUpdateChequeInfo);
			cStmtObject.setString(1,(String)chequeVO.getFloatAcctNo());
			cStmtObject.setString(2,(String)chequeVO.getClaimSettNo());
			cStmtObject.setString(3,(String)chequeVO.getRemarks());
			cStmtObject.setString(4,(String)chequeVO.getChequeNo());
			cStmtObject.setString(5,(String)chequeVO.getNewChequeNo());
			cStmtObject.setTimestamp(6,(Date)chequeVO.getChequeDate()!=null ? new Timestamp(chequeVO.getChequeDate().getTime()):null);
			cStmtObject.setLong(7,(Long)chequeVO.getUpdatedBy());
			cStmtObject.registerOutParameter(8,Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.execute();
			iResult = cStmtObject.getInt(8);
		}// end of try
		catch (SQLException sqlExp)
		 {
			throw new TTKException(sqlExp, "cheque");
		 }//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
        			log.error("Error while closing the Statement in ChequeDAOImpl updateChequeInfo()",sqlExp);
        			throw new TTKException(sqlExp, "cheque");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ChequeDAOImpl updateChequeInfo()",sqlExp);
        				throw new TTKException(sqlExp, "cheque");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "cheque");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of updateChequeInfo(ChequeVO chequeVO)
	
	
	public ArrayList<Object> getBankDocsUploadsList(String claimSettNo) throws TTKException {
	    	int iResult = 0;
	    	Connection conn = null;
	    	PreparedStatement pStmt1 = null;
	    	ArrayList alMouList	=	new ArrayList();
	    	ResultSet rs1 = null;
	    	PreAuthDetailVO preAuthDetailVO=null;
	    	try{

		    	conn = ResourceManager.getConnection();
		    	pStmt1=conn.prepareStatement(strGetBankUploadFils);
		    	pStmt1.setString(1, claimSettNo);
				rs1= pStmt1.executeQuery();
				
				if(rs1 != null){
					while(rs1.next()){
						
						preAuthDetailVO = new PreAuthDetailVO();
						preAuthDetailVO.setDescription(TTKCommon.checkNull(rs1.getString("file_desc")));
						preAuthDetailVO.setMouDocPath(TTKCommon.checkNull(rs1.getString("file_path")));
						preAuthDetailVO.setMouDocSeqID(((long) rs1.getLong("docs_seq_id")));
						preAuthDetailVO.setFileName(TTKCommon.checkNull(rs1.getString("file_name")));
		                if(rs1.getString("added_date") != null){
		                	preAuthDetailVO.setDateTime(rs1.getString("added_date"));
						}//end of if(rs.getString("ADDED_DATE") != null)
		                preAuthDetailVO.setUserId(TTKCommon.checkNull(rs1.getString("contact_name")));
		               //   
		                alMouList.add(preAuthDetailVO);
					}//end of while(rs.next())
				}//end of if(rs != null)
		    	//return (ArrayList<Object>)alMouList;
		    
	    		
	           
	        }//end of try
	    	catch (SQLException sqlExp)
	        {
	            throw new TTKException(sqlExp, "cheque");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "cheque");
	        }//end of catch (Exception exp)
	        finally
			{
	        	// Nested Try Catch to ensure resource closure
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if(rs1 != null) rs1.close();
	        			if (pStmt1 != null) pStmt1.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in ChequeDAOImpl getBankDocsUploadsList()",sqlExp);
	        			throw new TTKException(sqlExp, "cheque");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in ChequeDAOImpl getBankDocsUploadsList()",sqlExp);
	        				throw new TTKException(sqlExp, "cheque");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "cheque");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		rs1 = null;
	        		pStmt1 = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	 return alMouList;
	    }// 
	
	/* *//**
     * This method deletes the particulr TdsCertificate Info from the database,based on the certificate seq id's present in the arraylist
     * @param ArrayList alCertificateDelete
     * @return int It signifies the number of rows deleted in the database. 
     * @exception throws TTKException
     *//*
	public int deleteDocsUpload(ArrayList<Object> alCertificateDelete,String gateway)throws TTKException
	{
		  
		Connection conn=null;
		int iResult=0;
		CallableStatement cStmtObject=null;
		PreAuthDetailVO preAuthDetailVO = new PreAuthDetailVO();
		try{
			conn=ResourceManager.getConnection();
			if(gateway.equals("tableDataDocsUpload"))
			{				
			cStmtObject=(java.sql.CallableStatement)conn.prepareCall(strDeleteDocsUpload);			
			}
			else
			{
			cStmtObject=(java.sql.CallableStatement)conn.prepareCall(strDeleteTdsCertificates);
			}
			cStmtObject.setString(1,(String)alCertificateDelete.get(0));
			cStmtObject.registerOutParameter(2,OracleTypes.INTEGER);
			cStmtObject.execute();
			iResult=cStmtObject.getInt(2);
		}//end of try
		catch(SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "cheque");
		}//end of catch(SQLException sqlExp)
		catch(Exception exp)
		{
			throw new TTKException(exp, "cheque");
		}//end of catch(Exception exp)
		finally
		{
        	 //Nested Try Catch to ensure resource closure  
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in ChequeDAOImpl deleteDocsUpload()",sqlExp);
        			throw new TTKException(sqlExp, "cheque");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ChequeDAOImpl deleteDocsUpload()",sqlExp);
        				throw new TTKException(sqlExp, "cheque");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "cheque");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		  
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteAssocCertificatesInfo(ArrayList<Object> alCertificateDelete)

	*/
	
	
	
	public TTKReportDataSource doGeneratePaymenySummaryRpt(String parameterValue, String reportID)  throws TTKException {
		
		int iResult =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ResultSet rsSum = null;
		OracleCachedRowSet crs = null;
        TTKReportDataSource reportDataSource =null;
        
        try {
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPaymentSummaryRpt);
			cStmtObject.setString(1,parameterValue);
	        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
        	cStmtObject.execute();
	       rs = (java.sql.ResultSet)cStmtObject.getObject(2);
	       crs = new OracleCachedRowSet();
        	
	       if(rs !=null)
			{
				reportDataSource = new TTKReportDataSource();
				crs.populate(rs);
				reportDataSource.setData(crs);
			}//end of if(rs !=null)
        	
        	
		} catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "cheque");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
					log.error("Error while closing the Resultset in ChequeDAOImpl doGeneratePaymenySummaryRpt()",sqlExp);
					throw new TTKException(sqlExp, "cheque");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ChequeDAOImpl doGeneratePaymenySummaryRpt()",sqlExp);
						throw new TTKException(sqlExp, "cheque");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ChequeDAOImpl doGeneratePaymenySummaryRpt()",sqlExp);
							throw new TTKException(sqlExp, "cheque");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "cheque");
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

	public Object getPartnersList() throws TTKException{
		
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
			throw new TTKException(sqlExp, "cheque");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
					log.error("Error while closing the Resultset in ChequeDAOImpl getPartnersList()",sqlExp);
					throw new TTKException(sqlExp, "cheque");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ChequeDAOImpl getPartnersList()",sqlExp);
						throw new TTKException(sqlExp, "cheque");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ChequeDAOImpl getPartnersList()",sqlExp);
							throw new TTKException(sqlExp, "cheque");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "cheque");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}

	public TTKReportDataSource pendingMemberClaimsRpt(String parameterValue, String reportID)  throws TTKException{
		
		int iResult =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ResultSet rsSum = null;
		OracleCachedRowSet crs = null;
        TTKReportDataSource reportDataSource =null;
        
        try {
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPaymentSummaryRpt);
			cStmtObject.setString(1,parameterValue);
	        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);//network
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);//member
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);//partner
        	cStmtObject.execute();
	       rs = (java.sql.ResultSet)cStmtObject.getObject(3);
	       crs = new OracleCachedRowSet();
        	
	       if(rs !=null)
			{
				reportDataSource = new TTKReportDataSource();
				crs.populate(rs);
				reportDataSource.setData(crs);
			}//end of if(rs !=null)
        	
        	
		} catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "cheque");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
					log.error("Error while closing the Resultset in ChequeDAOImpl pendingMemberClaimsRpt()",sqlExp);
					throw new TTKException(sqlExp, "cheque");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ChequeDAOImpl pendingMemberClaimsRpt()",sqlExp);
						throw new TTKException(sqlExp, "cheque");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ChequeDAOImpl pendingMemberClaimsRpt()",sqlExp);
							throw new TTKException(sqlExp, "cheque");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "cheque");
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

	public TTKReportDataSource pendingNetWorkClaimsRpt(String parameterValue,String reportID) throws TTKException{
		
		int iResult =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ResultSet rsSum = null;
		OracleCachedRowSet crs = null;
        TTKReportDataSource reportDataSource =null;
        
        try {
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPaymentSummaryRpt);
			cStmtObject.setString(1,parameterValue);
	        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);//network
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);//member
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);//partner
        	cStmtObject.execute();
	       rs = (java.sql.ResultSet)cStmtObject.getObject(2);
	       crs = new OracleCachedRowSet();
        	
	       if(rs !=null)
			{
				reportDataSource = new TTKReportDataSource();
				crs.populate(rs);
				reportDataSource.setData(crs);
			}//end of if(rs !=null)
        	
        	
		} catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "cheque");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
					log.error("Error while closing the Resultset in ChequeDAOImpl pendingNetWorkClaimsRpt()",sqlExp);
					throw new TTKException(sqlExp, "cheque");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ChequeDAOImpl pendingNetWorkClaimsRpt()",sqlExp);
						throw new TTKException(sqlExp, "cheque");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ChequeDAOImpl pendingNetWorkClaimsRpt()",sqlExp);
							throw new TTKException(sqlExp, "cheque");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "cheque");
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

	public TTKReportDataSource pendingPartnerClaimsRpt(String parameterValue,String reportID)  throws TTKException{
		
		int iResult =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ResultSet rsSum = null;
		OracleCachedRowSet crs = null;
        TTKReportDataSource reportDataSource =null;
        
        try {
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPaymentSummaryRpt);
			cStmtObject.setString(1,parameterValue);
	        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);//network
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);//member
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);//partner
        	cStmtObject.execute();
	       rs = (java.sql.ResultSet)cStmtObject.getObject(4);
	       crs = new OracleCachedRowSet();
        	
	       if(rs !=null)
			{
				reportDataSource = new TTKReportDataSource();
				crs.populate(rs);
				reportDataSource.setData(crs);
			}//end of if(rs !=null)
        	
        	
		} catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "cheque");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "cheque");
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
					log.error("Error while closing the Resultset in ChequeDAOImpl pendingPartnerClaimsRpt()",sqlExp);
					throw new TTKException(sqlExp, "cheque");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ChequeDAOImpl pendingPartnerClaimsRpt()",sqlExp);
						throw new TTKException(sqlExp, "cheque");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ChequeDAOImpl pendingPartnerClaimsRpt()",sqlExp);
							throw new TTKException(sqlExp, "cheque");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "cheque");
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
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of ChequeDAOImpl