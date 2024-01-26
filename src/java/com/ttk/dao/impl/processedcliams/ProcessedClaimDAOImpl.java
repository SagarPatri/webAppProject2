package com.ttk.dao.impl.processedcliams;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.claims.ClaimDAOImpl;
import com.ttk.dto.preauth.PreAuthVO;

public class ProcessedClaimDAOImpl implements BaseDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ProcessedClaimDAOImpl.class );

	
	private static final String strGetClaimList = "{CALL CLAIM_PKG.SELECT_CLAIMS_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public ArrayList getClaimList(ArrayList<Object> alSearchCriteria) throws TTKException {

		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Collection<Object> alResultList = new ArrayList<Object>();
		PreAuthVO preauthVO = null;
		System.out.println("alSearchCriteria========>"+alSearchCriteria);
		try{
			conn = ResourceManager.getConnection();
			/*cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimList);
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
			cStmtObject.setString(14,(String)alSearchCriteria.get(13));		
            cStmtObject.setString(15,(String)alSearchCriteria.get(14));
			cStmtObject.setString(16,(String)alSearchCriteria.get(15));
			cStmtObject.setString(17,(String)alSearchCriteria.get(16));
			cStmtObject.setString(18,(String)alSearchCriteria.get(17));
			cStmtObject.setLong(19,(Long)alSearchCriteria.get(18));
			
			cStmtObject.setString(20,(String)alSearchCriteria.get(19));//v_sort_var
			cStmtObject.setString(21,(String)alSearchCriteria.get(20));//v_sort_order
			cStmtObject.setString(22,(String)alSearchCriteria.get(21));//v_start_num
			cStmtObject.setString(23,(String)alSearchCriteria.get(22));//v_end_num
			cStmtObject.registerOutParameter(24,OracleTypes.CURSOR);
			
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(24);
			
			if(rs != null){
				while(rs.next()){
					
					preauthVO = new PreAuthVO();
					
					 preauthVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
					 preauthVO.setInvoiceNo(TTKCommon.checkNull(rs.getString("INVOICE_NUMBER")));
					 preauthVO.setBatchNo(TTKCommon.checkNull(rs.getString("BATCH_NO")));
					 preauthVO.setClaimNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					 preauthVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					 preauthVO.setHospitalName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
					 preauthVO.setClaimantName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
					 preauthVO.setClaimType(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
					 preauthVO.setAssignedTo(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
					 
					 if(rs.getString("RECEIVED_DATE") != null){
							preauthVO.setReceivedDateAsString(new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("RECEIVED_DATE").getTime())));
						}
					 preauthVO.setApproveAmount(TTKCommon.checkNull(rs.getString("approved_amount")));
					 preauthVO.setStatus(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					 
						alResultList.add(preauthVO); 
				}
				}*/
			
			preauthVO = new PreAuthVO();
			
			 preauthVO.setClaimSeqID(100000L);
			 preauthVO.setInvoiceNo("777777");
			 preauthVO.setBatchNo("999999");
			 preauthVO.setClaimNo("CLM12345");
			 preauthVO.setEnrollmentID("10062020");
			 preauthVO.setHospitalName("ALHALLI HOSPITAL");
			 preauthVO.setClaimantName("DEEPTHI");
			 preauthVO.setClaimType("In-patient");
			 preauthVO.setAssignedTo("Bharath");
			
			
			
			return (ArrayList)alResultList;
		}
	/*	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "processedClaim");
		}//end of catch (SQLException sqlExp)
*/		catch (Exception exp)
		{
			throw new TTKException(exp, "processedClaim");
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
					log.error("Error while closing the Resultset in ProcessedClaimDAOImpl getClaimList()",sqlExp);
					throw new TTKException(sqlExp, "processedClaim");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProcessedClaimDAOImpl getClaimList()",sqlExp);
						throw new TTKException(sqlExp, "processedClaim");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProcessedClaimDAOImpl getClaimList()",sqlExp);
							throw new TTKException(sqlExp, "processedClaim");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "processedClaim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}
		
	}
	

}
