package com.ttk.dao.impl.preauth;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;

import com.asprise.util.pdf.ca;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.preauth.DashBoardVO;
import com.ttk.dto.preauth.DoctorDashboardUserVO;
import com.ttk.dto.preauth.ManagentDashboardDeatailsVO;
import com.ttk.dto.preauth.PreAuthAssignmentVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.PreDashboardVO;

public class PreAuthDashboardDAOImpl implements BaseDAO, Serializable{
	
	private static Logger log = Logger.getLogger(PreAuthDashboardDAOImpl.class );
	
	private static final String strManagentDetails = "{CALL preauth_autoassign_pkg.pat_dashboard_cnt(?)}";
	private static final String strUserDeatils ="{CALL preauth_autoassign_pkg.processor_info(?,?)}";
	private static final String strTatDeatils="{CALL preauth_autoassign_pkg.processor_task(?,?)}";
	private static final String strPreauthSearch ="{CALL preauth_autoassign_pkg.search_preauth(?,?,?,?,?,?)}";
	private static final String strAssignedUserList = "{CALL preauth_autoassign_pkg.assign_user_list(?,?)}";
	private static final String strSaveUserAssign="{CALL preauth_autoassign_pkg.save_assign(?,?,?,?,?,?,?)}";
	private static final String strDoctorDashboardUserDetails ="{CALL preauth_autoassign_pkg.processors_deatil(?)}";
	private static final String strDoctorDashboardPreauthDetails ="{CALL preauth_autoassign_pkg.preauth_information_list(?)}";
	
	
	
	
	public PreDashboardVO getManagementDetails(Long userSeqId) throws TTKException{
			
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	PreDashboardVO preDashboardVO =null;
    	try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strManagentDetails);
            cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(1);
            if(rs != null){
                while(rs.next()){
                	preDashboardVO = new PreDashboardVO();
                
                	preDashboardVO.setCoding_cases(TTKCommon.checkNull(rs.getString("total_inp_cases")));
                	preDashboardVO.setHighValuePA(TTKCommon.checkNull(rs.getString("high_inp_cases")));
                	preDashboardVO.setShortfall_cases(TTKCommon.checkNull(rs.getString("open_shortfall")));
                	preDashboardVO.setShortfall_respond_ases(TTKCommon.checkNull(rs.getString("respond_shortfall")));
                	preDashboardVO.setEnhanced_shortfall_cases(TTKCommon.checkNull(rs.getString("enhance_opn_shortfall")));
                	preDashboardVO.setEnhanced_shrtfal_res_cases(TTKCommon.checkNull(rs.getString("enhance_rsp_shortfall")));
                	preDashboardVO.setApproved_cases(TTKCommon.checkNull(rs.getString("approved_cases")));
                	preDashboardVO.setRejected_cases(TTKCommon.checkNull(rs.getString("rejected_cases")));
                	preDashboardVO.setForResolution(TTKCommon.checkNull(rs.getString("for_resolution")));
                	preDashboardVO.setFresh_case(TTKCommon.checkNull(rs.getString("unassign_cases")));
                	preDashboardVO.setEnhance_case(TTKCommon.checkNull(rs.getString("enhance_inp_cases")));
                	preDashboardVO.setAppeal_case(TTKCommon.checkNull(rs.getString("appeal_inp_cases")));
                	               	
    	
                }
            }
    	
    	}//end of try
    	 catch (SQLException sqlExp)
         {
             throw new TTKException(sqlExp, "dashboard");
         }//end of catch (SQLException sqlExp)
         catch (Exception exp)
         {
             throw new TTKException(exp, "dashboard");
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
 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getManagementDetails()",sqlExp);
 					throw new TTKException(sqlExp, "dashboard");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 				{

 					try
 					{
 						if (cStmtObject != null) cStmtObject.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getManagementDetails()",sqlExp);
 						throw new TTKException(sqlExp, "dashboard");
 					}//end of catch (SQLException sqlExp)
 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 					{
 						try
 						{
 							if(conn != null) conn.close();
 						}//end of try
 						catch (SQLException sqlExp)
 						{
 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getManagementDetails()",sqlExp);
 							throw new TTKException(sqlExp, "support");
 						}//end of catch (SQLException sqlExp)
 					}//end of finally Connection Close
 				}//end of finally Statement Close
 			}//end of try
 			catch (TTKException exp)
 			{
 				throw new TTKException(exp, "dashborad");
 			}//end of catch (TTKException exp)
 			finally // Control will reach here in anycase set null to the objects 
 			{
 				rs = null;
 				cStmtObject = null;
 				conn = null;
 			}//end of finally
 		}//end of finally

		return preDashboardVO;
	}
	
	public ArrayList getDataEntryUserInfo(ArrayList alSearchCreteria) throws TTKException{
		Collection<Object> alResultList = new ArrayList<Object>();
		
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	
    	try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strUserDeatils);
          
            cStmtObject.setLong(1,(Long)alSearchCreteria.get(0));
            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);     
            
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(2);
            if(rs != null){
                while(rs.next()){
                	
                	ManagentDashboardDeatailsVO managentDashboardDeatailsVO =new ManagentDashboardDeatailsVO();
                	managentDashboardDeatailsVO.setUserName(TTKCommon.checkNull(rs.getString("user_name")));
                	managentDashboardDeatailsVO.setUserCurStatus(TTKCommon.checkNull(rs.getString("user_status")));
                	managentDashboardDeatailsVO.setNoOfAssignedCases(TTKCommon.checkNull(rs.getString("assign_case")));
                	managentDashboardDeatailsVO.setNoOfCompletedCases(TTKCommon.checkNull(rs.getString("complete_case")));
                	managentDashboardDeatailsVO.setOfficeCode(TTKCommon.checkNull(rs.getString("office_name")));
                	alResultList.add(managentDashboardDeatailsVO);
    	
                }
            }
    	
    	}//end of try
    	 catch (SQLException sqlExp)
         {
             throw new TTKException(sqlExp, "dashboard");
         }//end of catch (SQLException sqlExp)
         catch (Exception exp)
         {
             throw new TTKException(exp, "dashboard");
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
 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getDataEntryUserInfo()",sqlExp);
 					throw new TTKException(sqlExp, "support");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 				{

 					try
 					{
 						if (cStmtObject != null) cStmtObject.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getDataEntryUserInfo()",sqlExp);
 						throw new TTKException(sqlExp, "dashboard");
 					}//end of catch (SQLException sqlExp)
 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 					{
 						try
 						{
 							if(conn != null) conn.close();
 						}//end of try
 						catch (SQLException sqlExp)
 						{
 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getDataEntryUserInfo()",sqlExp);
 							throw new TTKException(sqlExp, "dashboard");
 						}//end of catch (SQLException sqlExp)
 					}//end of finally Connection Close
 				}//end of finally Statement Close
 			}//end of try
 			catch (TTKException exp)
 			{
 				throw new TTKException(exp, "dashborad");
 			}//end of catch (TTKException exp)
 			finally // Control will reach here in anycase set null to the objects 
 			{
 				rs = null;
 				cStmtObject = null;
 				conn = null;
 			}//end of finally
 		}//end of finally
    	return (ArrayList)alResultList;
	}
	

	public ArrayList getDataEntrySelfAssinmentCase(ArrayList alSearchCreteria) throws TTKException{
		Collection<Object> alResultList = new ArrayList<Object>();
		
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	PreDashboardVO preDashboardVO =null;
    	try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTatDeatils);
          
            cStmtObject.setLong(1,(Long)alSearchCreteria.get(0));
            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
            
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(2);
            if(rs != null){
                while(rs.next()){
                	
                	PreAuthVO authorizationDashboardVO =new PreAuthVO();
                
                	authorizationDashboardVO.setPreAuthNo(TTKCommon.checkNull(rs.getString("pre_auth_number")));
                	authorizationDashboardVO.setPreAuthSeqID(rs.getLong("pat_auth_seq_id"));
                    authorizationDashboardVO.setStatus(TTKCommon.checkNull(rs.getString("status")));
                   
                    authorizationDashboardVO.setReceiveDate(TTKCommon.checkNull(rs.getString("receive_date")));
                	/*managentDashboardDeatailsVO.setEnrolmentId(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));*/
                	authorizationDashboardVO.setTat(TTKCommon.checkNull(rs.getString("tat")));
                	authorizationDashboardVO.setCorporateName(TTKCommon.checkNull(rs.getString("group_name")));
                	authorizationDashboardVO.setClaimantName(TTKCommon.checkNull(rs.getString("claimant_name")));
                	authorizationDashboardVO.setPatRequestedAmount(TTKCommon.checkNull(rs.getString("request_amount")));
                	authorizationDashboardVO.setAssigneeName(TTKCommon.checkNull(rs.getString("assignee_name")));
                	authorizationDashboardVO.setLocation(TTKCommon.checkNull(rs.getString("location")));
                	authorizationDashboardVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
                	
                			
                	
                	alResultList.add(authorizationDashboardVO);
    	
                }
            }
    	
    	}//end of try
    	 catch (SQLException sqlExp)
         {
             throw new TTKException(sqlExp, "dashboard");
         }//end of catch (SQLException sqlExp)
         catch (Exception exp)
         {
             throw new TTKException(exp, "dashboard");
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
 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getDataEntrySelfAssinmentCase()",sqlExp);
 					throw new TTKException(sqlExp, "support");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 				{

 					try
 					{
 						if (cStmtObject != null) cStmtObject.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getDataEntrySelfAssinmentCase()",sqlExp);
 						throw new TTKException(sqlExp, "dashboard");
 					}//end of catch (SQLException sqlExp)
 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 					{
 						try
 						{
 							if(conn != null) conn.close();
 						}//end of try
 						catch (SQLException sqlExp)
 						{
 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getDataEntrySelfAssinmentCase()",sqlExp);
 							throw new TTKException(sqlExp, "dashboard");
 						}//end of catch (SQLException sqlExp)
 					}//end of finally Connection Close
 				}//end of finally Statement Close
 			}//end of try
 			catch (TTKException exp)
 			{
 				throw new TTKException(exp, "dashborad");
 			}//end of catch (TTKException exp)
 			finally // Control will reach here in anycase set null to the objects 
 			{
 				rs = null;
 				cStmtObject = null;
 				conn = null;
 			}//end of finally
 		}//end of finally
    	return (ArrayList)alResultList;
	}
	
	
	public ArrayList getDataEntryActiveUsers(ArrayList al) throws TTKException{
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	
    	try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreauthSearch);
            
          
            
            cStmtObject.setString(1,(String)al.get(0));
            cStmtObject.setString(2,(String)al.get(1));
            cStmtObject.setString(3,(String)al.get(2));
            cStmtObject.setString(4,(String)al.get(5));
            cStmtObject.setString(5,(String)al.get(6));
            
            cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
          
            
            cStmtObject.execute();
            
            rs = (java.sql.ResultSet)cStmtObject.getObject(6);
            if(rs != null){
                while(rs.next()){
                
                	PreAuthVO authorizationDashboardVO =new PreAuthVO();
                
                
                	if(rs.getString("pat_auth_seq_id") != null){
                		authorizationDashboardVO.setPreAuthSeqID((new Long(rs.getString("pat_auth_seq_id"))));
					}
                	authorizationDashboardVO.setPreAuthNo(TTKCommon.checkNull(rs.getString("pre_auth_number")));
                    authorizationDashboardVO.setStatus(TTKCommon.checkNull(rs.getString("status")));                  
                	authorizationDashboardVO.setReceiveDate(TTKCommon.checkNull(rs.getString("receive_date"))); 
                	authorizationDashboardVO.setUpdateTime(TTKCommon.checkNull(rs.getString("updated_date")));
                	authorizationDashboardVO.setElapsedTime(TTKCommon.checkNull(rs.getString("elapsed_time")));
                	
                	authorizationDashboardVO.setTat(TTKCommon.checkNull(rs.getString("tat")));
                	authorizationDashboardVO.setCorporateName(TTKCommon.checkNull(rs.getString("Corporate_Name")));
                	authorizationDashboardVO.setClaimantName(TTKCommon.checkNull(rs.getString("claimant_name")));
                	authorizationDashboardVO.setPatRequestedAmount(TTKCommon.checkNull(rs.getString("request_amount")));
                	
                	
                	authorizationDashboardVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("Alkoot_id")));
                	
                	
                	authorizationDashboardVO.setLocation(TTKCommon.checkNull(rs.getString("location")));
                	if(TTKCommon.checkNull(rs.getString("completed_yn")).equalsIgnoreCase("N")){
                		authorizationDashboardVO.setImgIconTwo("UserAssignment");
                	}
					                 	
                	alResultList.add(authorizationDashboardVO);
    	
                }
            }
    	
    	}//end of try
    	 catch (SQLException sqlExp)
         {
             throw new TTKException(sqlExp, "dashboard");
         }//end of catch (SQLException sqlExp)
         catch (Exception exp)
         {
             throw new TTKException(exp, "dashboard");
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
 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getDataEntryActiveUsers()",sqlExp);
 					throw new TTKException(sqlExp, "support");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 				{

 					try
 					{
 						if (cStmtObject != null) cStmtObject.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getDataEntryActiveUsers()",sqlExp);
 						throw new TTKException(sqlExp, "dashboard");
 					}//end of catch (SQLException sqlExp)
 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 					{
 						try
 						{
 							if(conn != null) conn.close();
 						}//end of try
 						catch (SQLException sqlExp)
 						{
 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getDataEntryActiveUsers()",sqlExp);
 							throw new TTKException(sqlExp, "dashboard");
 						}//end of catch (SQLException sqlExp)
 					}//end of finally Connection Close
 				}//end of finally Statement Close
 			}//end of try
 			catch (TTKException exp)
 			{
 				throw new TTKException(exp, "dashborad");
 			}//end of catch (TTKException exp)
 			finally // Control will reach here in anycase set null to the objects 
 			{
 				rs = null;
 				cStmtObject = null;
 				conn = null;
 			}//end of finally
 		}//end of finally
		
		return (ArrayList)alResultList;
	}
	
	
	

	public  ArrayList<CacheObject> getAssignedUserList(Long contactSeqid) throws TTKException{
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	
    	try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAssignedUserList);
            cStmtObject.setLong(1,contactSeqid);		
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();                     
            
            rs = (java.sql.ResultSet)cStmtObject.getObject(2);
            if(rs != null){
                while(rs.next()){
                	CacheObject cacheObject = new CacheObject();
                	
                	cacheObject.setCacheId(rs.getString("CONTACT_SEQ_ID"));
                	cacheObject.setCacheDesc(rs.getString("ASSIGNED_TO"));                
                	alResultList.add(cacheObject);   
                }
            }
    	
    	}//end of try
    	 catch (SQLException sqlExp)
         {
             throw new TTKException(sqlExp, "dashboard");
         }//end of catch (SQLException sqlExp)
         catch (Exception exp)
         {
             throw new TTKException(exp, "dashboard");
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
 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getAssignedUserList()",sqlExp);
 					throw new TTKException(sqlExp, "support");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 				{

 					try
 					{
 						if (cStmtObject != null) cStmtObject.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getAssignedUserList()",sqlExp);
 						throw new TTKException(sqlExp, "dashboard");
 					}//end of catch (SQLException sqlExp)
 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 					{
 						try
 						{
 							if(conn != null) conn.close();
 						}//end of try
 						catch (SQLException sqlExp)
 						{
 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getAssignedUserList()",sqlExp);
 							throw new TTKException(sqlExp, "dashboard");
 						}//end of catch (SQLException sqlExp)
 					}//end of finally Connection Close
 				}//end of finally Statement Close
 			}//end of try
 			catch (TTKException exp)
 			{
 				throw new TTKException(exp, "dashborad");
 			}//end of catch (TTKException exp)
 			finally // Control will reach here in anycase set null to the objects 
 			{
 				rs = null;
 				cStmtObject = null;
 				conn = null;
 			}//end of finally
 		}//end of finally
		
		return (ArrayList)alResultList;
	}
	
	
	
	public Long saveAssignedUser(PreAuthAssignmentVO authorizationDashboardVO) throws TTKException{
		
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	
    	try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveUserAssign);
            
            cStmtObject.setLong(1,0l);
            if(authorizationDashboardVO.getPreAuthSeqID() != null){
                cStmtObject.setLong(2,authorizationDashboardVO.getPreAuthSeqID());
            }//end of if(bufferDetailVO.getBufferDtlSeqID() != null)
            else{
                cStmtObject.setString(2,null);
            }//end of else
            
            if(authorizationDashboardVO.getDoctor() != null){
                cStmtObject.setLong(3,authorizationDashboardVO.getDoctor());
            }//end of if(bufferDetailVO.getBufferDtlSeqID() != null)
           
            cStmtObject.setString(4,authorizationDashboardVO.getRemarks());
            cStmtObject.setString(5,"Y");
            cStmtObject.setLong(6,authorizationDashboardVO.getUpdatedBy());
            cStmtObject.registerOutParameter(7,Types.BIGINT);
           
            cStmtObject.registerOutParameter(1,Types.BIGINT);
            cStmtObject.execute();
            Long  lngUserAssingRes = cStmtObject.getLong(7);
                     
    	
    	}//end of try
    	 catch (SQLException sqlExp)
         {
             throw new TTKException(sqlExp, "dashboard");
         }//end of catch (SQLException sqlExp)
         catch (Exception exp)
         {
             throw new TTKException(exp, "dashboard");
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
 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl saveAssignedUser()",sqlExp);
 					throw new TTKException(sqlExp, "support");
 				}//end of catch (SQLException sqlExp)
 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
 				{

 					try
 					{
 						if (cStmtObject != null) cStmtObject.close();
 					}//end of try
 					catch (SQLException sqlExp)
 					{
 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl saveAssignedUser()",sqlExp);
 						throw new TTKException(sqlExp, "dashboard");
 					}//end of catch (SQLException sqlExp)
 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
 					{
 						try
 						{
 							if(conn != null) conn.close();
 						}//end of try
 						catch (SQLException sqlExp)
 						{
 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl saveAssignedUser()",sqlExp);
 							throw new TTKException(sqlExp, "dashboard");
 						}//end of catch (SQLException sqlExp)
 					}//end of finally Connection Close
 				}//end of finally Statement Close
 			}//end of try
 			catch (TTKException exp)
 			{
 				throw new TTKException(exp, "dashborad");
 			}//end of catch (TTKException exp)
 			finally // Control will reach here in anycase set null to the objects 
 			{
 				rs = null;
 				cStmtObject = null;
 				conn = null;
 			}//end of finally
 		}//end of finally
		
		return null;
	}
	
	
	      //User Information
			public ArrayList getPreauthUserDashBoadinfo(ArrayList alSearchCreteria) throws TTKException{
				
				
				Collection<Object> alResultList = new ArrayList<Object>();
				Collection<Object> alResultList2 = new ArrayList<Object>();
				Collection<Object> alResultList3 = new ArrayList<Object>();
				Collection<Object> alResultList4 = new ArrayList<Object>();
				Collection<Object> alFinalResultList = new ArrayList<Object>();
				
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		    	ResultSet rs = null;
		    	int count=0;
		    	try{
		            conn = ResourceManager.getConnection();
		            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDoctorDashboardUserDetails);
		          
		           
		            cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
		          
		            cStmtObject.execute();
		            rs = (java.sql.ResultSet)cStmtObject.getObject(1);
		            if(rs != null){
		                while(rs.next()){
		                	DoctorDashboardUserVO userListVO = new DoctorDashboardUserVO();
		                	
		                
		                	if(count < 11)
							{
		                		
		                	userListVO.setUserName(TTKCommon.checkNull(rs.getString("doctor_name")));//user_name
							userListVO.setAvgProcessTime(TTKCommon.checkNull(rs.getString("average_time")));//AVG_Processing_Time
							userListVO.setTotalLoggedHour(TTKCommon.checkNull(rs.getString("total_hours")));//Total_Logged_Hrs
						  
							
							
							if ("READY".equals(rs.getString("USER_STATUS").trim().toUpperCase())) {
								userListVO.setsStatusImageName("bullet_green");
								userListVO.setStatusImageTitle("Ready");
							}
							else if ("LOG OUT".equals(rs.getString("USER_STATUS").trim().toUpperCase())) {
								userListVO.setsStatusImageName("bullet_red");
								userListVO.setStatusImageTitle("Log Out");
							}
							/*else{
								userListVO.setsStatusImageName("bullet_blue");
								userListVO.setStatusImageTitle("Stand By");
							}*/
							userListVO.setOfficeCode(TTKCommon.checkNull(rs.getString("total_case")));
							userListVO.setTag(TTKCommon.checkNull(rs.getString("user_tag")));
							
		                	
		                	alResultList.add(userListVO);
		                }else if(count < 22)
						{
		                	userListVO.setUserName(TTKCommon.checkNull(rs.getString("doctor_name")));//user_name
							userListVO.setAvgProcessTime(TTKCommon.checkNull(rs.getString("average_time")));//AVG_Processing_Time
							userListVO.setTotalLoggedHour(TTKCommon.checkNull(rs.getString("total_case")));//Total_Logged_Hrs
							
							if ("Ready".equals(rs.getString("USER_STATUS"))) {
								userListVO.setsStatusImageName("bullet_green");
								userListVO.setStatusImageTitle("Ready");
							}
							else if ("Log Out".equals(rs.getString("USER_STATUS"))) {
								userListVO.setsStatusImageName("bullet_red");
								userListVO.setStatusImageTitle("Log Out");
							}
							else{
								userListVO.setsStatusImageName("bullet_blue");
								userListVO.setStatusImageTitle("Stand By");
							}
							userListVO.setOfficeCode(TTKCommon.checkNull(rs.getString("total_hours")));
							userListVO.setTag(TTKCommon.checkNull(rs.getString("user_tag")));
		                	
							alResultList2.add(userListVO);
		                	
						}
		                else if(count < 33)
						{
		                  	userListVO.setUserName(TTKCommon.checkNull(rs.getString("doctor_name")));//user_name
							userListVO.setAvgProcessTime(TTKCommon.checkNull(rs.getString("average_time")));//AVG_Processing_Time
							userListVO.setTotalLoggedHour(TTKCommon.checkNull(rs.getString("total_case")));//Total_Logged_Hrs
							
							if ("Ready".equals(rs.getString("USER_STATUS"))) {
								userListVO.setsStatusImageName("bullet_green");
								userListVO.setStatusImageTitle("Ready");
							}
							else if ("Log Out".equals(rs.getString("USER_STATUS"))) {
								userListVO.setsStatusImageName("bullet_red");
								userListVO.setStatusImageTitle("Log Out");
							}
							else{
								userListVO.setsStatusImageName("bullet_blue");
								userListVO.setStatusImageTitle("Stand By");
							}
							userListVO.setOfficeCode(TTKCommon.checkNull(rs.getString("total_hours")));
							userListVO.setTag(TTKCommon.checkNull(rs.getString("user_tag")));
							alResultList3.add(userListVO);
						}else{
							userListVO.setUserName(TTKCommon.checkNull(rs.getString("doctor_name")));//user_name
							userListVO.setAvgProcessTime(TTKCommon.checkNull(rs.getString("average_time")));//AVG_Processing_Time
							userListVO.setTotalLoggedHour(TTKCommon.checkNull(rs.getString("total_case")));//Total_Logged_Hrs
							
							if ("Ready".equals(rs.getString("USER_STATUS"))) {
								userListVO.setsStatusImageName("bullet_green");
								userListVO.setStatusImageTitle("Ready");
							}
							else if ("Log Out".equals(rs.getString("USER_STATUS"))) {
								userListVO.setsStatusImageName("bullet_red");
								userListVO.setStatusImageTitle("Log Out");
							}
							else{
								userListVO.setsStatusImageName("bullet_blue");
								userListVO.setStatusImageTitle("Stand By");
							}
							userListVO.setOfficeCode(TTKCommon.checkNull(rs.getString("total_hours")));
							userListVO.setTag(TTKCommon.checkNull(rs.getString("user_tag")));
							alResultList4.add(userListVO);
						}
		                	
		                	count++;	
		                }
		            }
		            alFinalResultList.add(alResultList);
					alFinalResultList.add(alResultList2);
					alFinalResultList.add(alResultList3);
					alFinalResultList.add(alResultList4);
		    	}//end of try
		    	 catch (SQLException sqlExp)
		         {
		             throw new TTKException(sqlExp, "dashboard");
		         }//end of catch (SQLException sqlExp)
		         catch (Exception exp)
		         {
		             throw new TTKException(exp, "dashboard");
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
		 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getPreauthUserDashBoadinfo()",sqlExp);
		 					throw new TTKException(sqlExp, "support");
		 				}//end of catch (SQLException sqlExp)
		 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		 				{

		 					try
		 					{
		 						if (cStmtObject != null) cStmtObject.close();
		 					}//end of try
		 					catch (SQLException sqlExp)
		 					{
		 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getPreauthUserDashBoadinfo()",sqlExp);
		 						throw new TTKException(sqlExp, "dashboard");
		 					}//end of catch (SQLException sqlExp)
		 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		 					{
		 						try
		 						{
		 							if(conn != null) conn.close();
		 						}//end of try
		 						catch (SQLException sqlExp)
		 						{
		 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getPreauthUserDashBoadinfo()",sqlExp);
		 							throw new TTKException(sqlExp, "dashboard");
		 						}//end of catch (SQLException sqlExp)
		 					}//end of finally Connection Close
		 				}//end of finally Statement Close
		 			}//end of try
		 			catch (TTKException exp)
		 			{
		 				throw new TTKException(exp, "dashborad");
		 			}//end of catch (TTKException exp)
		 			finally // Control will reach here in anycase set null to the objects 
		 			{
		 				rs = null;
		 				cStmtObject = null;
		 				conn = null;
		 			}//end of finally
		 		}//end of finally
		    	return (ArrayList)alFinalResultList;
			
				
			}
			

			public PreDashboardVO getDoctorDashboardDetails(Long userSeqId) throws TTKException{
				
				
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		    	ResultSet rs = null;
		    	PreDashboardVO preDashboardVO =null;
		    	try{
		            conn = ResourceManager.getConnection();
		            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strManagentDetails);
		         
		            cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
		          
		            cStmtObject.execute();
		            rs = (java.sql.ResultSet)cStmtObject.getObject(1);
		            if(rs != null){
		                while(rs.next()){
		                	preDashboardVO = new PreDashboardVO();
		                	
		                	
		                	
		                	preDashboardVO.setCoding_cases(TTKCommon.checkNull(rs.getString("total_inp_cases")));
		                	preDashboardVO.setHighValuePA(TTKCommon.checkNull(rs.getString("high_inp_cases")));
		                	preDashboardVO.setShortfall_cases(TTKCommon.checkNull(rs.getString("open_shortfall")));
		                	preDashboardVO.setShortfall_respond_ases(TTKCommon.checkNull(rs.getString("respond_shortfall")));
		                	preDashboardVO.setEnhanced_shortfall_cases(TTKCommon.checkNull(rs.getString("enhance_opn_shortfall")));
		                	preDashboardVO.setEnhanced_shrtfal_res_cases(TTKCommon.checkNull(rs.getString("enhance_rsp_shortfall")));
		                	preDashboardVO.setApproved_cases(TTKCommon.checkNull(rs.getString("approved_cases")));
		                	preDashboardVO.setRejected_cases(TTKCommon.checkNull(rs.getString("rejected_cases")));
		                	preDashboardVO.setForResolution(TTKCommon.checkNull(rs.getString("for_resolution")));
		                	preDashboardVO.setDoctorLogedin(TTKCommon.checkNull(rs.getString("doctors_loged_in")));
		                	preDashboardVO.setFresh_case(TTKCommon.checkNull(rs.getString("unassign_cases")));
		                	preDashboardVO.setEnhance_case((TTKCommon.checkNull(rs.getString("enhance_inp_cases"))));
		                	preDashboardVO.setAppeal_case(TTKCommon.checkNull(rs.getString("appeal_inp_cases")));
		                	
		                	
		                
		                	
		                	
		    	
		                }
		            }
		    	
		    	}//end of try
		    	 catch (SQLException sqlExp)
		         {
		             throw new TTKException(sqlExp, "dashboard");
		         }//end of catch (SQLException sqlExp)
		         catch (Exception exp)
		         {
		             throw new TTKException(exp, "dashboard");
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
		 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getManagementDetails()",sqlExp);
		 					throw new TTKException(sqlExp, "dashboard");
		 				}//end of catch (SQLException sqlExp)
		 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		 				{

		 					try
		 					{
		 						if (cStmtObject != null) cStmtObject.close();
		 					}//end of try
		 					catch (SQLException sqlExp)
		 					{
		 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getManagementDetails()",sqlExp);
		 						throw new TTKException(sqlExp, "dashboard");
		 					}//end of catch (SQLException sqlExp)
		 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		 					{
		 						try
		 						{
		 							if(conn != null) conn.close();
		 						}//end of try
		 						catch (SQLException sqlExp)
		 						{
		 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getManagementDetails()",sqlExp);
		 							throw new TTKException(sqlExp, "support");
		 						}//end of catch (SQLException sqlExp)
		 					}//end of finally Connection Close
		 				}//end of finally Statement Close
		 			}//end of try
		 			catch (TTKException exp)
		 			{
		 				throw new TTKException(exp, "dashborad");
		 			}//end of catch (TTKException exp)
		 			finally // Control will reach here in anycase set null to the objects 
		 			{
		 				rs = null;
		 				cStmtObject = null;
		 				conn = null;
		 			}//end of finally
		 		}//end of finally

				return preDashboardVO;
			}
			public ArrayList getPreauthBoardinfo(ArrayList alSearchCreteria) throws TTKException{
				Collection<Object> alResultList = new ArrayList<Object>();
				
				Connection conn = null;
		    	CallableStatement cStmtObject=null;
		    	ResultSet rs = null;
		    	
		    	try{
		            conn = ResourceManager.getConnection();
		            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDoctorDashboardPreauthDetails);		          		           
		            cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);		          		            
		            cStmtObject.execute();
		            rs = (java.sql.ResultSet)cStmtObject.getObject(1);
		            if(rs != null){
		                while(rs.next()){
		                	
		                	DashBoardVO dashBoardVO = new DashBoardVO();
		                	dashBoardVO.setStatus(TTKCommon.checkNull(rs.getString("status")));
							dashBoardVO.setSource(TTKCommon.checkNull(rs.getString("source_type")));
							dashBoardVO.setPreauthNo(TTKCommon.checkNull(rs.getString("pre_auth_number")));
							dashBoardVO.setRecivedDate(TTKCommon.checkNull(rs.getString("receive_date")));
							/*dashBoardVO.setIdnf(TTKCommon.checkNull(rs.getString("idnfYN"))); */
							dashBoardVO.setElapsedTime(TTKCommon.checkNull(rs.getString("elapsed_time")));	
							dashBoardVO.setTat(TTKCommon.checkNull(rs.getString("tat")));
							dashBoardVO.setAssignTo(TTKCommon.checkNull(rs.getString("assignee_name")));
							dashBoardVO.setCorporate(TTKCommon.checkNull(rs.getString("group_name")));
							dashBoardVO.setLocation(TTKCommon.checkNull(rs.getString("location")));
							dashBoardVO.setRecivedTime(TTKCommon.checkNull(rs.getString("updated_date")));
							
							
							if("Yes".equals(rs.getString("VIP_YN")))
							{
								
								dashBoardVO.setvVipImageName("vvip");
								dashBoardVO.setvVipImageTitle("Vvip");
							}
							if("Yes".equals(rs.getString("METERNITY_YN")))
							{
								//System.out.println("UXUtility.checkNull(rs.getString(\"preauth_number\")" + UXUtility.checkNull(rs.getString("preauth_number")));

								//System.out.println("maternityYN");
								dashBoardVO.setCatogoryImageName("Pregnent_women");
								dashBoardVO.setCatogoryImageTitle("Maternity");
							}
							if("Yes".equals(rs.getString("Emergency_yn")))
							{
								//System.out.println("UXUtility.checkNull(rs.getString(\"preauth_number\")" + UXUtility.checkNull(rs.getString("preauth_number")));

								//System.out.println("emergencyYN");
								dashBoardVO.setEmergencyImageName("Emergency_icon");
								dashBoardVO.setEmergencyImageTitle("Emergency");
							}
							alResultList.add(dashBoardVO);
		                }
		            }
		    	
		    	}//end of try
		    	 catch (SQLException sqlExp)
		         {
		             throw new TTKException(sqlExp, "dashboard");
		         }//end of catch (SQLException sqlExp)
		         catch (Exception exp)
		         {
		             throw new TTKException(exp, "dashboard");
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
		 					log.error("Error while closing the Resultset in PreAuthDashboardDAOImpl getPreauthBoardinfo()",sqlExp);
		 					throw new TTKException(sqlExp, "support");
		 				}//end of catch (SQLException sqlExp)
		 				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		 				{

		 					try
		 					{
		 						if (cStmtObject != null) cStmtObject.close();
		 					}//end of try
		 					catch (SQLException sqlExp)
		 					{
		 						log.error("Error while closing the Statement in PreAuthDashboardDAOImpl getPreauthBoardinfo()",sqlExp);
		 						throw new TTKException(sqlExp, "dashboard");
		 					}//end of catch (SQLException sqlExp)
		 					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		 					{
		 						try
		 						{
		 							if(conn != null) conn.close();
		 						}//end of try
		 						catch (SQLException sqlExp)
		 						{
		 							log.error("Error while closing the Connection in PreAuthDashboardDAOImpl getPreauthBoardinfo()",sqlExp);
		 							throw new TTKException(sqlExp, "dashboard");
		 						}//end of catch (SQLException sqlExp)
		 					}//end of finally Connection Close
		 				}//end of finally Statement Close
		 			}//end of try
		 			catch (TTKException exp)
		 			{
		 				throw new TTKException(exp, "dashborad");
		 			}//end of catch (TTKException exp)
		 			finally // Control will reach here in anycase set null to the objects 
		 			{
		 				rs = null;
		 				cStmtObject = null;
		 				conn = null;
		 			}//end of finally
		 		}//end of finally
		    	return (ArrayList)alResultList;
				
			}
			
	
}// Class
