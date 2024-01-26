package com.ttk.dao.impl.onlineforms.providerLogin; 
/**
 * @ (#) OnlineProviderDAOImpl.java Jul 19, 2007
 * Project 	     : TTK HealthCare Services
 * File          : OnlineProviderDAOImpl.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Jul 19, 2007
 *
 * @author       :  RamaKrishna K M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.ttk.action.table.onlineforms.providerLogin.OverDueReportTable;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.onlineforms.PartnerClaimSubmissionVo;
import com.ttk.dto.onlineforms.providerLogin.BatchListVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.usermanagement.UserListVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;
//import com.ttk.dto.preauth.PreAuthVO;
//import com.ttk.dto.security.GroupVO;
public class OnlineProviderDAOImpl implements BaseDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID 				= 1L;
	private static Logger log = Logger.getLogger(OnlineProviderDAOImpl.class);
	
	private static String strPreAuthLogList	=	"{CALL HOSPITAL_PKG.SELECT_PRE_AUTH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static String strViewPreAuthDetails	=	"{CALL HOSPITAL_PKG.SELECT_PREAUTH_DETAILS(?,?,?)}";
	private static String strViewShortFallList	=	"{CALL HOSPITAL_PKG.SELECT_SHORTFALL_DETAILS (?,?)}";
	private static String strClaimLogList	=	"{CALL HOSPITAL_PKG.SELECT_CLAIMS_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static String strChequeDetails	=	"{CALL HOSPITAL_PKG.SELECT_CHKWISE_REPORT(?,?)}";
	private static final String strLogDetail	= "SELECT R.* FROM( SELECT UC.CONTACT_NAME,HI.USER_ID,HI.LOGIN_DATE "
			+ "FROM APP.TPA_USER_LOGIN_HISTORY HI JOIN APP.TPA_LOGIN_INFO LI ON (HI.USER_ID = LI.USER_ID) JOIN "
			+ "APP.TPA_USER_CONTACTS UC ON (LI.CONTACT_SEQ_ID = UC.CONTACT_SEQ_ID) WHERE HI.USER_ID = ? AND UC.USER_GENERAL_TYPE_ID = 'HOS' "
			+ "GROUP BY UC.CONTACT_NAME,HI.USER_ID,HI.LOGIN_DATE ORDER BY HI.LOGIN_DATE DESC) R WHERE ROWNUM < 6";
	private static String strInvoiceDetails	=	"{CALL HOSPITAL_PKG.SELECT_INVOICEWISE_REPORT(?,?,?)}";
	private static String strBatchReconciliationList	=	"{CALL HOSPITAL_PKG.SELECT_BATCH_RECON_SUMMARY (?,?,?,?,?,?,?,?,?)}";
	private static String strBatchInvoiceDetails	=	"{CALL HOSPITAL_PKG.SELECT_BATCH_RECON_SMRY_RPT (?,?,?,?)}";
	private static String strBatchInvoiceList	=	"{CALL HOSPITAL_PKG.SELECT_BATCH_INVOICE_LIST  (?,?,?,?,?,?,?,?,?)}";
	private static String strInvoiceReportDetails	=	"{CALL HOSPITAL_PKG.SELECT_BATCH_INVOICE_RPT   (?,?,?)}";
	private static String strOverdueReportList	=	"{CALL HOSPITAL_PKG.SELECT_OVERDUE_RPT (?,?,?,?,?,?,?,?,?)}";
	private static String strFinanceDashBoard	=	"{CALL HOSPITAL_PKG.DASHBOARD_ACCOUNT  (?,?,?,?)}";
	private static String strRecentReportsList	=	"{CALL HOSPITAL_PKG.SELECT_PROV_DWNL_RPT_DETAILS   (?,?,?)}";
	private static String strShortFallDocsSave	=	"{CALL HOSP_DIAGNOSYS_PKG.SAVE_SHORTFALL_DETAILS   (?,?,?,?)}";
	private static String strShortFallDetails	=	"{CALL HOSP_DIAGNOSYS_PKG.GET_SHORTFALL_DETAILS   (?,?)}";
	private static String strCLMShortFallDetails	=	"{CALL CLAIM_PKG.GET_SHORTFALL_DETAILS   (?,?)}";
	
	private static String strPartnerPreAuthLogList	=	"{CALL partner_pkg.select_pre_auth_list(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSavePartnerPartialPreAuth 	= "{CALL ONLINE_PARTNER_LOGIN. PRE_AUTH_SUBMISSION_SAVE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetAppealCommentsForProvider 	= "{CALL HOSP_DIAGNOSYS_PKG.select_pat_auth_details(?)}";
	private static String strPartnerClaimSubmit	=	"{CALL online_partner_login.clm_submission_save(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	
	public ArrayList getPreAuthSearchList(ArrayList alSearchCriteria) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strPreAuthLogList);
          //  oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strPreAuthLogList);
            
            callableStatement.setString(1,(String) alSearchCriteria.get(9));//1 Empanel No
           // callableStatement.setString(1,"UAE-SJH-CPC-0023");//1 Empanel No
            callableStatement.setString(2,(String) alSearchCriteria.get(0));//2
            callableStatement.setString(3,(String) alSearchCriteria.get(1));//3
            callableStatement.setString(4,(String) alSearchCriteria.get(2));//4
            callableStatement.setString(5,(String) alSearchCriteria.get(3));//5
            callableStatement.setString(6,(String) alSearchCriteria.get(4));//6
            callableStatement.setString(7,(String) alSearchCriteria.get(5));//7
            callableStatement.setString(8,(String) alSearchCriteria.get(6));//8
            callableStatement.setString(9,(String) alSearchCriteria.get(7));//9
            callableStatement.setString(10,(String) alSearchCriteria.get(8));//10
            callableStatement.setString(11,(String) alSearchCriteria.get(10));//11
            callableStatement.setString(12,(String) alSearchCriteria.get(11));//12
            callableStatement.setString(18,(String) alSearchCriteria.get(12));//17
            callableStatement.setString(13,(String) alSearchCriteria.get(13));//13
                  
            String status = (String) alSearchCriteria.get(8); 	//10
            if("INP".equals(status))
              	callableStatement.setString(19,(String) alSearchCriteria.get(14)); //14 In-Progress Status
            else
            	callableStatement.setString(19,null); //14 In-Progress Status
            
            callableStatement.setString(14,(String) alSearchCriteria.get(15));//15
            callableStatement.setString(15,(String) alSearchCriteria.get(16));//16
            callableStatement.setString(16,(String) alSearchCriteria.get(17));//17
            callableStatement.setString(17,(String) alSearchCriteria.get(18));//18
            callableStatement.registerOutParameter(20,OracleTypes.CURSOR);//

            callableStatement.execute(); 
          
           rs = (java.sql.ResultSet)callableStatement.getObject(20);
            ArrayList<PreAuthSearchVO> alPreAuthSearchVOs	=	new ArrayList<PreAuthSearchVO>();
            if(rs != null){
                while (rs.next()) {

                	preAuthSearchVO	=	new PreAuthSearchVO();
                	
                	preAuthSearchVO.setPreAuthNo(TTKCommon.checkNull(rs.getString("PRE_AUTH_NUMBER")));
                	preAuthSearchVO.setReceivedDate(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("PAT_RECEIVED_DATE"))));
                	preAuthSearchVO.setPatientName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
                	preAuthSearchVO.setBenefitType(TTKCommon.checkNull(rs.getString("BENEFIT_TYPE")));
                	preAuthSearchVO.setTreatingDoctor(TTKCommon.checkNull(rs.getString("TREATING_DOCTOR")));
                	preAuthSearchVO.setApprovalNo(TTKCommon.checkNull(rs.getString("AUTH_NUMBER")));
                	preAuthSearchVO.setStatus(TTKCommon.checkNull(rs.getString("PAT_STATUS_TYPE_ID")));	// status code
                	preAuthSearchVO.setPatientCardId(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
                	preAuthSearchVO.setStatusID(TTKCommon.checkNull(rs.getString("PREAUTH_STATUS")));	// status desc
                	preAuthSearchVO.setPatAuthSeqId(TTKCommon.getLong(rs.getString("PAT_AUTH_SEQ_ID")));
                	preAuthSearchVO.setPreAuthRefNo(TTKCommon.checkNull(rs.getString("PAT_AUTH_REF"))); 
                	preAuthSearchVO.setEmirateID(TTKCommon.checkNull(rs.getString("EMIRATE_ID")));
                	preAuthSearchVO.setEventReferenceNo(TTKCommon.checkNull(rs.getString("event_no")));
                	preAuthSearchVO.setEnhanceYN(TTKCommon.checkNull(rs.getString("ENHANCED")));
                	preAuthSearchVO.setStrShortfallYN(TTKCommon.checkNull(rs.getString("SHRTFALL_YN")));
					if(rs.getString("SHRTFALL_YN").equals("Y")){
						preAuthSearchVO.setStrShortfallImageName("shortfall");
						preAuthSearchVO.setStrShortfallImageTitle("Shortfall Received "+TTKCommon.checkNull(rs.getString("srtfll_updated_date")));
					}//end of if(rs.getString("SHRTFALL_YN").equals("Y"))
					
						preAuthSearchVO.setSubmitYN(TTKCommon.checkNull(rs.getString("SUBMITYN")));		
                //	if("Y".equals(preAuthSearchVO.getEnhanceYN()))
						if("Y".equals(preAuthSearchVO.getSubmitYN()))
                	{
                		preAuthSearchVO.setEnhanceImageName("EnhancementNew");
                		preAuthSearchVO.setEnhanceImageTitle("Enhance PreApproval");
                	}
                	//preAuthSearchVO.setApprovalNo();
                	preAuthSearchVO.setEnhance("");
                	
                	if("Cancelled".equals(preAuthSearchVO.getStatus()))
                		preAuthSearchVO.setApprovalNo("");
                	if(rs.getString("DECISION_DATE")!=null)
                		preAuthSearchVO.setDecisionDt(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("DECISION_DATE"))));
                	else
                		preAuthSearchVO.setDecisionDt("");
                	
                 	preAuthSearchVO.setEnhancedPreauthYN(TTKCommon.checkNull(rs.getString("ENHANCED_PREAUTHYN")));
                 	
                	if("Y".equals(preAuthSearchVO.getEnhancedPreauthYN()))
                	{
                		preAuthSearchVO.setPreauthEnhanceImageName("EnhancedPreauth");
                		preAuthSearchVO.setPreauthEnhanceImageTitle("Enhanced PreApproval");
                	}
                	
                	if("INP-ENH".equals(rs.getString("IN_PROGESS_STATUS")))
                	{	
                		preAuthSearchVO.setStatus("In-Progress - Enhancement");
                		preAuthSearchVO.setInProImageName("InprogressEnhancement");
                	    preAuthSearchVO.setInProImageTitle("InProgress Enhancement");
                	}
                	else if("INP-APL".equals(rs.getString("IN_PROGESS_STATUS")))
                	{
                		preAuthSearchVO.setStatus("In-Progress - Appeal");
                		preAuthSearchVO.setInProImageName("InprogressAppeal");
                		preAuthSearchVO.setInProImageTitle("InProgress Appeal");
                	}
                	else if("INP-RES".equals(rs.getString("IN_PROGESS_STATUS")))
                	{
                		preAuthSearchVO.setStatus("In-Progress-Shortfall Responded");
                		preAuthSearchVO.setInProImageName("AddIcon");
                		preAuthSearchVO.setInProImageTitle("InProgress Shortfall Responded");
                	}
                	else
                	{
                		preAuthSearchVO.setStatus(TTKCommon.checkNull(rs.getString("PAT_STATUS_TYPE_ID")));
                		preAuthSearchVO.setInProImageName("Blank");
                		preAuthSearchVO.setInProImageTitle("");
                	}
                	
                	String PREAUTH_STATUS = TTKCommon.checkNull(rs.getString("PREAUTH_STATUS"));
                	if("APR".equals(PREAUTH_STATUS) || "REJ".equals(PREAUTH_STATUS))
                	{	
                		if(rs.getInt("claim_seq_id") == 0)
                		{
	                		if(rs.getInt("appeal_count")<2)                	
	                		{
	                			preAuthSearchVO.setAppealImageName("AppealButton");
	                			preAuthSearchVO.setApealImageTitle("PreApproval Appeal");
	                		}
	                		else
	                    	{
	                    		preAuthSearchVO.setAppealImageName("Blank");
	                        	preAuthSearchVO.setApealImageTitle("");
	                    	}
                		}
                		else
                    	{
                    		preAuthSearchVO.setAppealImageName("Blank");
                        	preAuthSearchVO.setApealImageTitle("");
                    	}
                	}	
                	else
                	{
                		preAuthSearchVO.setAppealImageName("Blank");
                    	preAuthSearchVO.setApealImageTitle("");
                	}
                	                	
                	alPreAuthSearchVOs.add(preAuthSearchVO);                	
                	
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alPreAuthSearchVOs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPreAuthSearchList(String enrollmentId)
	
	public ArrayList getPartnerPreAuthSearchList(ArrayList alSearchCriteria) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strPartnerPreAuthLogList);
          //  oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strPreAuthLogList);
            
            callableStatement.setString(1,(String) alSearchCriteria.get(9));//1 Empanel No
           // callableStatement.setString(1,"UAE-SJH-CPC-0023");//1 Empanel No
            callableStatement.setString(2,(String) alSearchCriteria.get(0));//2
            callableStatement.setString(3,(String) alSearchCriteria.get(1));//3
            callableStatement.setString(4,(String) alSearchCriteria.get(2));//4
            callableStatement.setString(5,(String) alSearchCriteria.get(3));//5
            callableStatement.setString(6,(String) alSearchCriteria.get(4));//6
            callableStatement.setString(7,(String) alSearchCriteria.get(5));//7
            callableStatement.setString(8,(String) alSearchCriteria.get(6));//8
            callableStatement.setString(9,(String) alSearchCriteria.get(7));//9
            callableStatement.setString(10,(String) alSearchCriteria.get(8));//10
            callableStatement.setString(11,(String) alSearchCriteria.get(10));//11
      
            
            callableStatement.setString(11,(String) alSearchCriteria.get(10));//11
            callableStatement.setString(12,(String) alSearchCriteria.get(11));//12
            callableStatement.setString(13,(String) alSearchCriteria.get(12));//13
            callableStatement.setString(14,(String) alSearchCriteria.get(13));//14
            callableStatement.setString(15,(String) alSearchCriteria.get(14));//15
            callableStatement.setString(16,(String) alSearchCriteria.get(15));//16
            callableStatement.setString(17,(String) alSearchCriteria.get(16));//16
            callableStatement.registerOutParameter(18,OracleTypes.CURSOR);//17

            callableStatement.execute();
           
            //pREaUTH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(18);
            ArrayList<PreAuthSearchVO> alPreAuthSearchVOs	=	new ArrayList<PreAuthSearchVO>();
            if(rs != null){
                while (rs.next()) {

                	preAuthSearchVO	=	new PreAuthSearchVO();
                	
                	preAuthSearchVO.setPreAuthNo(TTKCommon.checkNull(rs.getString("PRE_AUTH_NUMBER")));
                	preAuthSearchVO.setReceivedDate(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("PAT_RECEIVED_DATE"))));
                	preAuthSearchVO.setPatientName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
                	preAuthSearchVO.setBenefitType(TTKCommon.checkNull(rs.getString("BENEFIT_TYPE")));
                	preAuthSearchVO.setTreatingDoctor(TTKCommon.checkNull(rs.getString("TREATING_DOCTOR")));
                	preAuthSearchVO.setApprovalNo(TTKCommon.checkNull(rs.getString("AUTH_NUMBER")));
                	preAuthSearchVO.setStatus(TTKCommon.checkNull(rs.getString("PAT_STATUS_TYPE_ID")));
                	preAuthSearchVO.setPatientCardId(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
                	preAuthSearchVO.setStatusID(TTKCommon.checkNull(rs.getString("PREAUTH_STATUS")));                	
                	preAuthSearchVO.setPatAuthSeqId(TTKCommon.getLong(rs.getString("PAT_AUTH_SEQ_ID")));
                	preAuthSearchVO.setPreAuthRefNo(TTKCommon.checkNull(rs.getString("PAT_AUTH_REF"))); 
                	preAuthSearchVO.setEmirateID(TTKCommon.checkNull(rs.getString("EMIRATE_ID")));
                	preAuthSearchVO.setProviderName(TTKCommon.getString(rs.getString("hosp_name")));
                	preAuthSearchVO.setCountryName(TTKCommon.getString(rs.getString("country")));
                	preAuthSearchVO.setEnhanceYN(TTKCommon.checkNull(rs.getString("ENHANCED")));
                	preAuthSearchVO.setDecisionDt(TTKCommon.checkNull(rs.getString("decision_date")));    
                	if(rs.getString("PARTNER_PAT_AUTH_SEQ_ID")!=null)
                	preAuthSearchVO.setPartnerPatSeqId(TTKCommon.getLong(rs.getString("PARTNER_PAT_AUTH_SEQ_ID")));
                	if("Y".equals(preAuthSearchVO.getEnhanceYN()))
                	{
                		preAuthSearchVO.setEnhanceImageName("EnhancementNew");
                		preAuthSearchVO.setEnhanceImageTitle("Enhance PreApproval");
                	}
                	//preAuthSearchVO.setApprovalNo();
                	preAuthSearchVO.setEnhance("");
                	
                	if("Cancelled".equals(preAuthSearchVO.getStatus()))
                		preAuthSearchVO.setApprovalNo("");
                	
                	alPreAuthSearchVOs.add(preAuthSearchVO);
                	 
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alPreAuthSearchVOs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPreAuthSearchList(String enrollmentId)
	
	
	
	public PreAuthSearchVO getAuthDetails(String empanelNo,String status,Long patAuthSeqId) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strViewPreAuthDetails);
            //callableStatement.setString(1,"UAE-SJH-CPC-0023");//1 Empanel No
            callableStatement.setString(1,empanelNo);//1 Empanel No
            callableStatement.setLong(2,patAuthSeqId);//3
            callableStatement.registerOutParameter(3,OracleTypes.CURSOR);//15

            callableStatement.execute();
           
            rs = (java.sql.ResultSet)callableStatement.getObject(3);
            if(rs != null){
                if (rs.next()) {

                	preAuthSearchVO	=	new PreAuthSearchVO();
                	
                	preAuthSearchVO.setPreAuthType(TTKCommon.getString(rs.getString("PREAUTH_TYPE")));
                	preAuthSearchVO.setReceivedDate(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("PAT_RECEIVED_DATE"))));
                	preAuthSearchVO.setAdmissionDate(TTKCommon.getString(rs.getString("HOSPITALIZATION_DATE")));
                	preAuthSearchVO.setBenefitType(TTKCommon.getString(rs.getString("BENEFIT_TYPE")));
                	preAuthSearchVO.setPreviousApprAmt(TTKCommon.getString(rs.getString("PREVIOUS_APPROVED_AMT")));
                	preAuthSearchVO.setRequestedAmt(TTKCommon.getString(rs.getString("REQUESTED_AMT")));
                	preAuthSearchVO.setTreatingDoctor(TTKCommon.getString(rs.getString("TREATING_DOCTOR")));
                	preAuthSearchVO.setVidalBranch(TTKCommon.getString(rs.getString("VIDAL_BRANCH")));
                	preAuthSearchVO.setProbableDischargeDt(TTKCommon.getString(rs.getString("DISCHARGE_DATE")));
                	preAuthSearchVO.setBeneficiaryId(TTKCommon.getString(rs.getString("TPA_ENROLLMENT_ID")));
                	preAuthSearchVO.setPatientName(TTKCommon.getString(rs.getString("BENEFICIARY_NAME")));
                	preAuthSearchVO.setProviderName(TTKCommon.getString(rs.getString("HOSP_NAME")));
                	preAuthSearchVO.setEmpanelId(TTKCommon.getString(rs.getString("EMPANEL_NUMBER")));
                	preAuthSearchVO.setEmirate(TTKCommon.getString(rs.getString("STATE_TYPE_ID")));
                	preAuthSearchVO.setCity(TTKCommon.getString(rs.getString("CITY_TYPE_ID")));
                	preAuthSearchVO.setApprovalNo(TTKCommon.getString(rs.getString("AUTH_NUMBER")));
                	preAuthSearchVO.setStatusID(TTKCommon.getString(rs.getString("PAT_STATUS_TYPE_ID")));
                	preAuthSearchVO.setDecisionDt(TTKCommon.getString(rs.getString("COMPLETED_DATE")));
                	preAuthSearchVO.setEmirateID(TTKCommon.getString(rs.getString("EMIRATE_ID")));
                	 
                }//end of if(rs.next())
            }//end of if(rs != null)
            return preAuthSearchVO;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of PreAuthSearchVO getAuthDetails(String empanelNo,String status)
	
    
	

	
	public ArrayList getShortfallList(Long patAuthSeqId) throws TTKException {
        Connection conn = null;
        CallableStatement callableStatement=null;
        ArrayList<String[]> alShortFallList	=	null;
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strViewShortFallList);
            callableStatement.setLong(1,patAuthSeqId);//1 patAuthSeqId
            callableStatement.registerOutParameter(2,OracleTypes.CURSOR);//15

            callableStatement.execute();
            rs = (java.sql.ResultSet)callableStatement.getObject(2);
            if(rs != null){
            	alShortFallList	=	new ArrayList<String[]>();
                while (rs.next()) {

                	String[] strShortfallArr	=	new String[7];
                	strShortfallArr[0]	=	TTKCommon.getString(rs.getString("SRTFLL_SENT_DATE"));
                	strShortfallArr[1]	=	TTKCommon.getString(rs.getString("SHORTFALL_ID"));
                	strShortfallArr[2]	=	TTKCommon.getString(rs.getString("SRTFLL_GENERAL_TYPE_ID"));
                	strShortfallArr[3]	=	TTKCommon.getString(rs.getString("SRTFLL_STATUS_GENERAL_TYPE_ID"));
                	strShortfallArr[4]	=	TTKCommon.getString(rs.getString("SRTFLL_RECEIVED_DATE"));
                	strShortfallArr[5]	=	TTKCommon.getString(rs.getString("DOCS_STATUS"));//"ShortFall";
                	strShortfallArr[6]	=	TTKCommon.getString(rs.getString("SHORTFALL_SEQ_ID"));
                	alShortFallList.add(strShortfallArr);
                	
                	 
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alShortFallList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getShortfallList(String enrollmentId)
	
	
	
	
	
	public ArrayList getClaimSearchList(ArrayList alSearchCriteria) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strClaimLogList);
          //  oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strPreAuthLogList);
            
            callableStatement.setString(1,(String) alSearchCriteria.get(0));//1 Empanel No
            //callableStatement.setString(1,"UAE-SJH-CPC-0023");//1 Empanel No
            callableStatement.setString(2,(String) alSearchCriteria.get(1));//2
            callableStatement.setString(3,(String) alSearchCriteria.get(2));//3
            callableStatement.setString(4,(String) alSearchCriteria.get(3));//4
            callableStatement.setString(5,(String) alSearchCriteria.get(4));//5
            callableStatement.setString(6,(String) alSearchCriteria.get(5));//6
            callableStatement.setString(7,(String) alSearchCriteria.get(6));//7
            callableStatement.setString(8,(String) alSearchCriteria.get(7));//8
            callableStatement.setString(9,(String) alSearchCriteria.get(8));//9
            callableStatement.setString(10,(String) alSearchCriteria.get(9));//10
            callableStatement.setString(11,(String) alSearchCriteria.get(10));//11
            callableStatement.setString(12,(String) alSearchCriteria.get(11));//12
            callableStatement.setString(13,(String) alSearchCriteria.get(12));//12
            
            callableStatement.setString(14,"CLAIM_NUMBER");//13 SORT VARIABLE HARDCODED SINCE USING SAME TABLE FOR BOTH PREAUTH AND CLAIMS 
            callableStatement.setString(15,(String) alSearchCriteria.get(14));//14
            callableStatement.setString(16,(String) alSearchCriteria.get(15));//15
            callableStatement.setString(17,(String) alSearchCriteria.get(16));//16

            callableStatement.registerOutParameter(18,OracleTypes.CURSOR);//17

            callableStatement.execute();
           
            //pREaUTH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(18);
            ArrayList<PreAuthSearchVO> alPreAuthSearchVOs	=	new ArrayList<PreAuthSearchVO>();
            if(rs != null){
                while (rs.next()) {

                	preAuthSearchVO	=	new PreAuthSearchVO();
                	preAuthSearchVO.setClmSeqId(TTKCommon.getLong(rs.getString("CLAIM_SEQ_ID")));
                	preAuthSearchVO.setClmBatchSeqId(TTKCommon.getLong(rs.getString("CLM_BATCH_SEQ_ID")));
                	preAuthSearchVO.setReceivedDate(TTKCommon.checkNull(rs.getString("RECEIVED_DATE")));
                	preAuthSearchVO.setPatientName(TTKCommon.getString(rs.getString("MEM_NAME")));
                	preAuthSearchVO.setPatientCardId(TTKCommon.getString(rs.getString("TPA_ENROLLMENT_ID")));
                	preAuthSearchVO.setPreAuthNo(TTKCommon.getString(rs.getString("CLAIM_NUMBER")));
                	preAuthSearchVO.setClaimType(TTKCommon.getString(rs.getString("CLAIM_TYPE")));
                	preAuthSearchVO.setBatchNo(TTKCommon.getString(rs.getString("BATCH_NO")));
                	preAuthSearchVO.setInvoiceNo(TTKCommon.getString(rs.getString("INVOICE_NUMBER")));
                	preAuthSearchVO.setBenefitType(TTKCommon.getString(rs.getString("BENEFIT_TYPE")));
                	preAuthSearchVO.setStatus(TTKCommon.getString(rs.getString("CLAIM_STATUS")));
                	preAuthSearchVO.setApprovalNo(TTKCommon.getString(rs.getString("SETTLEMENT_NUMBER")));
                	preAuthSearchVO.setChequeNo(TTKCommon.getString(rs.getString("CHEQUE_NO")));
                	preAuthSearchVO.setEmirateID(TTKCommon.checkNull(rs.getString("EMIRATE_ID")));
                	alPreAuthSearchVOs.add(preAuthSearchVO);
                	 
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alPreAuthSearchVOs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getClaimSearchList(String enrollmentId)
	
	
	
	public String[] getChequeDetails(String chequeNumber) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strChequeDetails);
          //  oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strPreAuthLogList);
            
            callableStatement.setString(1,(String) chequeNumber);//1 Empanel No

            callableStatement.registerOutParameter(2,OracleTypes.CURSOR);//2

            callableStatement.execute();
           
            //pREaUTH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(2);
            String[] strChequeDetails	=	new String[20];
            if(rs != null){
                if (rs.next()) {
                	strChequeDetails[0]	=	TTKCommon.checkNull(rs.getString("CHECK_NUM"));
                	strChequeDetails[1]	=	TTKCommon.checkNull(rs.getString("CHECK_DATE"));
                	strChequeDetails[2]	=	TTKCommon.checkNull(rs.getString("CHECK_AMOUNT"));
                	strChequeDetails[3]	=	TTKCommon.checkNull(rs.getString("PAYEE_NAME"));
                	strChequeDetails[4]	=	TTKCommon.checkNull(rs.getString("DATE_OF_HOSPITALIZATION"));
                	strChequeDetails[5]	=	TTKCommon.checkNull(rs.getString("BENEFIT_TYPE"));
                	strChequeDetails[6]	=	TTKCommon.checkNull(rs.getString("BATCH_NO"));
                	strChequeDetails[7]	=	TTKCommon.checkNull(rs.getString("INVOICE_NO"));
                	strChequeDetails[8]	=	TTKCommon.checkNull(rs.getString("CLM_RECEIVED_DATE"));
                	strChequeDetails[9]	=	TTKCommon.checkNull(rs.getString("MEM_NAME"));
                	strChequeDetails[10]	=	TTKCommon.checkNull(rs.getString("REQUESTED_AMT"));
                	strChequeDetails[11]	=	TTKCommon.checkNull(rs.getString("TOT_DISCOUNT_AMOUNT"));
                	strChequeDetails[12]	=	TTKCommon.checkNull(rs.getString("DEDUCTABLE_AMT"));
                	strChequeDetails[13]	=	TTKCommon.checkNull(rs.getString("CO_PAY"));
                	strChequeDetails[14]	=	TTKCommon.checkNull(rs.getString("TOT_NET_AMOUNT"));
                	strChequeDetails[15]	=	TTKCommon.checkNull(rs.getString("TOT_ALLOWED_AMOUNT"));
                	strChequeDetails[16]	=	TTKCommon.checkNull(rs.getString("RSON_DIFF"));
                	strChequeDetails[17]	=	TTKCommon.checkNull(rs.getString("REMARKS"));
                }//end of while(rs.next())
            }//end of if(rs != null)
            return strChequeDetails;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getChequeDetails(String chequeNumber)
	
	
	
	
	
	public String[] getInvoiceDetails(String chequeNumber,String empanelNumber) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strInvoiceDetails);
            callableStatement.setString(1,(String) chequeNumber);//1 Invoice No
            callableStatement.setString(2,(String) empanelNumber);//2 empanelNumber

            callableStatement.registerOutParameter(3,OracleTypes.CURSOR);//3

            callableStatement.execute();
           
            //pREaUTH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(3);
            String[] strChequeDetails	=	new String[20];
            if(rs != null){
                if (rs.next()) {

                	strChequeDetails[0]	=	TTKCommon.checkNull(rs.getString("INVOICE_NUMBER"));
                	strChequeDetails[1]	=	TTKCommon.checkNull(rs.getString("REQUESTED_AMT"));
                	strChequeDetails[2]	=	(rs.getString("CLM_RECEIVED_DATE"));
                	strChequeDetails[3]	=	TTKCommon.checkNull(rs.getString("BATCH_NAME"));
                	strChequeDetails[4]	=	TTKCommon.checkNull(rs.getString("BATCH_NO"));
                	strChequeDetails[5]	=	TTKCommon.checkNull(rs.getString("PATIENT_NAME"));
                	strChequeDetails[6]	=	TTKCommon.checkNull(rs.getString("DATE_OF_HOSPITALIZATION"));
                	strChequeDetails[7]	=	TTKCommon.checkNull(rs.getString("CLM_RECEIVED_DATE"));
                	strChequeDetails[8]	=	TTKCommon.checkNull(rs.getString("REQUESTED_AMT"));
                	strChequeDetails[9]	=	TTKCommon.checkNull(rs.getString("TOT_DISCOUNT_AMOUNT"));
                	strChequeDetails[10]	=	TTKCommon.checkNull(rs.getString("DEDUCTABLE_AMT"));
                	strChequeDetails[11]	=	TTKCommon.checkNull(rs.getString("CO_PAY"));
                	strChequeDetails[12]	=	TTKCommon.checkNull(rs.getString("TOT_NET_AMOUNT"));
                	strChequeDetails[13]	=	TTKCommon.checkNull(rs.getString("TOT_ALLOWED_AMOUNT"));
                	strChequeDetails[14]	=	TTKCommon.checkNull(rs.getString("RSON_DIFF"));
                	strChequeDetails[15]	=	TTKCommon.checkNull(rs.getString("CLM_STATUS_TYPE_ID"));
                	strChequeDetails[16]	=	TTKCommon.checkNull(rs.getString("CHECK_NUM"));
                	strChequeDetails[17]	=	TTKCommon.checkNull(rs.getString("CHECK_DATE"));
                }//end of while(rs.next())
            }//end of if(rs != null)
            return strChequeDetails;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getInvoiceDetails(String chequeNumber)
	
	
	
	/*
     * Get Log Details
     */
    public ArrayList getLogData(String strUserId) throws TTKException {
    	ArrayList alResultList = new ArrayList();
        Connection conn 		= null;
        ResultSet rs 			= null;
        PreparedStatement pStmt = null;
        UserListVO userListVO	=	null;
    try{
    	
    		conn	=	null;
    		rs		=	null;
    		conn 	= ResourceManager.getConnection();
    		 pStmt = conn.prepareStatement(strLogDetail);
             pStmt.setString(1,strUserId);
             rs = pStmt.executeQuery();
             if(rs != null){
                 while (rs.next()) {
                	
                	 userListVO = new UserListVO();
                	 
                	 userListVO.setUserID(TTKCommon.checkNull(rs.getString("USER_ID")));
                	 userListVO.setUserName(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
                	 if(rs.getTimestamp("LOGIN_DATE") != null)
                		 userListVO.setLoginDate(new Date(rs.getTimestamp("LOGIN_DATE").getTime()));
                	 alResultList.add(userListVO);
                }
            }
        return (ArrayList<String>)alResultList;
        
    }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in InsCorpDAOImpl getRetailList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in InsCorpDAOImpl getRetailList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in InsCorpDAOImpl getRetailList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getLogData(String strUserId)
    
    
    
    /*
     * Batch Reconciliation Report
     */

	public ArrayList getBatchRenconcilList(ArrayList alSearchCriteria) throws TTKException {
        Connection conn = null;
        BatchListVO batchListVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strBatchReconciliationList);
            
            callableStatement.setString(1,(String) alSearchCriteria.get(0));//1 Empanel No
            callableStatement.setString(2,(String) alSearchCriteria.get(1));//2 From Date
            callableStatement.setString(3,(String) alSearchCriteria.get(2));//3 To Date
            callableStatement.setString(4,(String) alSearchCriteria.get(3));//4 Batch No
            
            callableStatement.setString(5,(String) alSearchCriteria.get(4));//5  
            callableStatement.setString(6,(String) alSearchCriteria.get(5));//6
            callableStatement.setString(7,(String) alSearchCriteria.get(6));//7
            callableStatement.setString(8,(String) alSearchCriteria.get(7));//8
            
            callableStatement.registerOutParameter(9,OracleTypes.CURSOR);//9 Cursor
            callableStatement.execute();
           
            //BATCH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(9);
            ArrayList<BatchListVO> alBatchListVOs	=	new ArrayList<BatchListVO>();
            if(rs != null){
                while (rs.next()) {

                	batchListVO	=	new BatchListVO();
                	
                	batchListVO.setBatchDate(rs.getDate("BATCH_RECEIVED_DATE"));
                	batchListVO.setBatchNumber(rs.getString("BATCH_NO"));
                	batchListVO.setBatchSeqId(rs.getLong("CLM_BATCH_SEQ_ID"));
                	batchListVO.setClaimedAmt(TTKCommon.getDouble(rs.getString("CLAIMED_AMT")));
                	batchListVO.setApproved(TTKCommon.getInt(rs.getString("APPROVED")));
                	batchListVO.setRejected(TTKCommon.getInt(rs.getString("REJECTED")));
                	batchListVO.setShortFall(TTKCommon.getInt(rs.getString("SHORTFALL")));
                	batchListVO.setUnderProcess(TTKCommon.getInt(rs.getString("INPROGRESS")));
                	batchListVO.setClosed(TTKCommon.getInt(rs.getString("CLOSED")));
                	batchListVO.setNoOfInvs(TTKCommon.getInt(rs.getString("No_OF_INVOICES")));
                	
                	alBatchListVOs.add(batchListVO);
                	 
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alBatchListVOs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getBatchRenconcilList(String enrollmentId)
	
	
	
	
	/*
     * Get Batch Reconciliation Invoice details 
     */
	public ArrayList<BatchListVO> getBatchInvDetails(Long BatchSeqId, String strFlag,String EmpnlNo) throws TTKException {
        Connection conn = null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
        	/*  
        	  
        	  */
        	
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strBatchInvoiceDetails);
            callableStatement.setLong(1,BatchSeqId);//1 BatchSeqId
            callableStatement.setString(2,strFlag);//2 Flag
            callableStatement.setString(3,EmpnlNo);//2 EmpnlNo
            
            callableStatement.registerOutParameter(4,OracleTypes.CURSOR);//2

            callableStatement.execute();
            BatchListVO batchListVO	=	null;
            rs = (java.sql.ResultSet)callableStatement.getObject(4);
            ArrayList<BatchListVO> alBatchInvDetails	=	new ArrayList<>();
            if(rs != null){
                while (rs.next()) {

                	batchListVO	=	new BatchListVO();
                	batchListVO.setReceivedDate(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("CLM_RECEIVED_DATE"))));
                	batchListVO.setInvNo(TTKCommon.checkNull(rs.getString("INVOICE_NUMBER")));
                	batchListVO.setBatchNumber(TTKCommon.checkNull(rs.getString("BATCH_NO")));
                	batchListVO.setTreatmentDate(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("DATE_OF_HOSPITALIZATION"))));
                	batchListVO.setPatientName(TTKCommon.checkNull(rs.getString("PATIENT_NAME")));
                	batchListVO.setTtlNetClmd(TTKCommon.checkNull(rs.getString("CLAIMED_AMT")));
                	batchListVO.setTtlAmtApprd(TTKCommon.checkNull(rs.getString("APPROVED_AMT")));
                	
                	alBatchInvDetails.add(batchListVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alBatchInvDetails;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getBatchInvDetails(Long BatchSeqId, String strVar)
	
	
	
	
	/*
     * Batch Reconciliation Report
     */

	public ArrayList getBatchInvoiceList(ArrayList alSearchCriteria) throws TTKException {
        Connection conn = null;
        BatchListVO batchListVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strBatchInvoiceList);
            
            callableStatement.setString(1,(String) alSearchCriteria.get(0));//1 Empanel No
            callableStatement.setString(2,(String) alSearchCriteria.get(1));//2 From Date
            callableStatement.setString(3,(String) alSearchCriteria.get(2));//3 To Date
            callableStatement.setString(4,(String) alSearchCriteria.get(3));//4 Batch No
            
            callableStatement.setString(5,(String) "INVOICE_NUMBER");//5  
            callableStatement.setString(6,(String) alSearchCriteria.get(5));//6
            callableStatement.setString(7,(String) alSearchCriteria.get(6));//7
            callableStatement.setString(8,(String) alSearchCriteria.get(7));//8
            
            callableStatement.registerOutParameter(9,OracleTypes.CURSOR);//9 Cursor
            callableStatement.execute();
           
            //BATCH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(9);
            ArrayList<BatchListVO> alBatchInvListVOs	=	new ArrayList<BatchListVO>();
            if(rs != null){
                while (rs.next()) {

                	batchListVO	=	new BatchListVO();
                	batchListVO.setSlno(rs.getInt("SL_NO"));
                	batchListVO.setInsCompName(rs.getString("INS_COMP_NAME"));
                	batchListVO.setInvNo(rs.getString("INVOICE_NUMBER"));
                	batchListVO.setDtOfHospitalization(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("DATE_OF_HOSPITALIZATION"))));
                	batchListVO.setReceivedDate(rs.getString("CLM_RECEIVED_DATE"));
                	batchListVO.setPatientName(TTKCommon.checkNull(rs.getString("PATIENT_NAME")));
                	batchListVO.setClaimedAmt(TTKCommon.getDouble(rs.getString("CLAIMED_AMT")));
                	batchListVO.setTtlNetClmd(TTKCommon.checkNull(rs.getString("TOT_NET_AMOUNT")));
                	batchListVO.setTtlAmtApprd(TTKCommon.checkNull(rs.getString("TOT_APPROVED_AMOUNT")));
                	batchListVO.setClaimStatus(TTKCommon.checkNull(rs.getString("CLM_STATUS_TYPE_ID")));
                	batchListVO.setBatchNo(TTKCommon.checkNull(rs.getString("BATCH_NO")));
                	
                	alBatchInvListVOs.add(batchListVO);
                	 
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alBatchInvListVOs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getBatchInvoiceList(ArrayList alSearchCriteria)
	
	
	
	/*
	 * Batch Invoice Report Details
	 */
	public String[] getBatchInvReportDetails(String invNo,String EmpnlNo) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strInvoiceReportDetails);
            callableStatement.setString(1,(String) EmpnlNo);//1 EmpnlNo No
            callableStatement.setString(2,(String) invNo);//1 invNo
            callableStatement.registerOutParameter(3,OracleTypes.CURSOR);//2

            callableStatement.execute();
           
            //pREaUTH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(3);
            String[] strInvReportDetails	=	new String[25];
            if(rs != null){
                if (rs.next()) {

                	strInvReportDetails[0]	=	TTKCommon.checkNull(rs.getString("INS_COMP_NAME"));
                	strInvReportDetails[1]	=	TTKCommon.checkNull(rs.getString("INVOICE_NUMBER"));
                	strInvReportDetails[2]	=	TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("DATE_OF_HOSPITALIZATION")));
                	strInvReportDetails[3]	=	TTKCommon.checkNull(rs.getString("BENEFIT_TYPE"));
                	strInvReportDetails[4]	=	TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("CLM_RECEIVED_DATE")));
                	strInvReportDetails[5]	=	TTKCommon.checkNull(rs.getString("SERVICE_CODE"));
                	strInvReportDetails[6]	=	TTKCommon.checkNull(rs.getString("SERVICE_NAME"));
                	strInvReportDetails[7]	=	TTKCommon.checkNull(rs.getString("PATIENT_NAME"));
                	strInvReportDetails[8]	=	TTKCommon.checkNull(rs.getString("CLAIMED_AMT"));
                	strInvReportDetails[9]	=	TTKCommon.checkNull(rs.getString("DISC_AMT"));
                	strInvReportDetails[10]	=	TTKCommon.checkNull(rs.getString("COPAY_AMT"));
                	strInvReportDetails[11]	=	TTKCommon.checkNull(rs.getString("TOT_NET_AMOUNT"));
                	strInvReportDetails[12]	=	TTKCommon.checkNull(rs.getString("TOT_APPROVED_AMOUNT"));
                	strInvReportDetails[13]	=	TTKCommon.checkNull(rs.getString("DIFF_AMT"));
                	strInvReportDetails[14]	=	TTKCommon.checkNull(rs.getString("DIAGNOSYS_CODE"));
                	strInvReportDetails[15]	=	TTKCommon.checkNull(rs.getString("DIAGNOSYS"));
                	strInvReportDetails[16]	=	TTKCommon.checkNull(rs.getString("CLM_STATUS_TYPE_ID"));
                	strInvReportDetails[17]	=	TTKCommon.checkNull(rs.getString("CHECK_NUM"));
                	strInvReportDetails[18]	=	TTKCommon.checkNull(rs.getString("CHECK_DATE"));
                	strInvReportDetails[19]	=	TTKCommon.checkNull(rs.getString("CHECK_AMOUNT"));
                	strInvReportDetails[20]	=	TTKCommon.checkNull(rs.getString("REMARKS"));
                }//end of while(rs.next())
            }//end of if(rs != null)
            return strInvReportDetails;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getBatchInvReportDetails(String invNo,String EmpnlNo)
	
	
	
	
	
	/*
     * Batch Reconciliation Report
     */

	public ArrayList<BatchListVO> getOverDueList(ArrayList<Object> alSearchCriteria) throws TTKException {
        Connection conn = null;
        BatchListVO batchListVO	=	null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strOverdueReportList);
            
            callableStatement.setString(1,(String) alSearchCriteria.get(0));//1 Empanel No
            callableStatement.setString(2,(String) alSearchCriteria.get(1));//2 From Date
            callableStatement.setString(3,(String) alSearchCriteria.get(2));//3 To Date
            callableStatement.setString(4,(String) alSearchCriteria.get(3));//4 Invoice No
            
            //SORT, PAGE INDEX VARIABLES COMING FROM TABLE CLASS
            callableStatement.setString(6,(String) "INVOICE_NUMBER");//5  
            callableStatement.setString(7,(String) alSearchCriteria.get(5));//6
            callableStatement.setString(8,(String) alSearchCriteria.get(6));//7
            callableStatement.setString(9,(String) alSearchCriteria.get(7));//8
            
            callableStatement.registerOutParameter(5,OracleTypes.CURSOR);//9 Cursor
            callableStatement.execute();
           
            //BATCH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(5);
            ArrayList<BatchListVO> alOverDueVOs	=	new ArrayList<BatchListVO>();
            if(rs != null){
                while (rs.next()) {

                	batchListVO	=	new BatchListVO();
                	
                	batchListVO.setReceivedDate(rs.getString("CLM_RECEIVED_DATE"));
                	batchListVO.setInvNo(rs.getString("INVOICE_NUMBER"));
                	batchListVO.setTreatmentDate(TTKCommon.getFormattedDate(TTKCommon.getString(rs.getString("DATE_OF_HOSPITALIZATION"))));
                	batchListVO.setPatientName(TTKCommon.checkNull(rs.getString("PATIENT_NAME")));
                	batchListVO.setInsCompName(rs.getString("INS_COMP_NAME"));
                	batchListVO.setClaimedAmt(TTKCommon.getDouble(rs.getString("CLAIMED_AMT")));
                	batchListVO.setDueDate(TTKCommon.getString(rs.getString("DUE_DATE")));
                	batchListVO.setOveDueBy(TTKCommon.getString(rs.getString("OVER_DUE_BY")));
                	
                	alOverDueVOs.add(batchListVO);
                	 
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alOverDueVOs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getOverDueList(ArrayList<String> arrayList)
	
	
	
	public String[] getFinanceDahBoard(String empanelNo,String fromDate,String toDate) throws TTKException {
        Connection conn = null;
        PreAuthSearchVO preAuthSearchVO	=	null;
        CallableStatement callableStatement=null;
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strFinanceDashBoard);
          //  oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strPreAuthLogList);
            
            callableStatement.setString(1,(String) empanelNo);//1 Empanel No
            callableStatement.setString(2,fromDate);//2 FromDate
            callableStatement.setString(3,toDate);//3 ToDate

            callableStatement.registerOutParameter(4,OracleTypes.CURSOR);//2

            callableStatement.execute();
           
            //pREaUTH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(4);
            String[] strDashBoardDetails	=	new String[9];
            if(rs != null){
                if (rs.next()) {
                	strDashBoardDetails[0]	=	TTKCommon.checkNull(rs.getString("OPENING_AMT"));
                	strDashBoardDetails[1]	=	TTKCommon.checkNull(rs.getString("INPROGRESS_AMT"));
                	strDashBoardDetails[2]	=	TTKCommon.checkNull(rs.getString("APPROVED_AMT"));
                	strDashBoardDetails[3]	=	TTKCommon.checkNull(rs.getString("REJECTED_AMT"));
                	strDashBoardDetails[4]	=	TTKCommon.checkNull(rs.getString("SHORTFALL_AMT"));
                	strDashBoardDetails[5]	=	TTKCommon.checkNull(rs.getString("SETTLED_AMT"));
                	strDashBoardDetails[6]	=	TTKCommon.checkNull(rs.getString("CLOSED_AMT"));
                	strDashBoardDetails[7]	=	TTKCommon.checkNull(rs.getString("FROM_DATE"));
                	strDashBoardDetails[8]	=	TTKCommon.checkNull(rs.getString("TO_DATE"));
                }//end of while(rs.next())
            }//end of if(rs != null)
            return strDashBoardDetails;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of String[] getFinanceDahBoard(String empanelNo)
	
	
	
	/*
     * Recent Reports Downloaded
     */

	public ArrayList<String[]> getRecentReports(String empanelNo,Long userId) throws TTKException {
        Connection conn = null;
        CallableStatement callableStatement=null;
        
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            callableStatement	=	conn.prepareCall(strRecentReportsList);
            callableStatement.setString(1,empanelNo);//1 Empanel No
            callableStatement.setLong(2,userId);//2 From Date
            callableStatement.registerOutParameter(3,OracleTypes.CURSOR);//3   Cursor
            callableStatement.execute();
           
            //BATCH sEARCH lIST
            rs = (java.sql.ResultSet)callableStatement.getObject(3);
            String[] alRecentReps	=	null;
            ArrayList<String[]> alRecentReps1	=	new ArrayList<String[]>();
            if(rs != null){
                while (rs.next()) {
                	alRecentReps	=	new String[5];	
                	alRecentReps[0]	=	TTKCommon.checkNull(rs.getString("REPORT_DATE"));
                	alRecentReps[1]	=	TTKCommon.checkNull(rs.getString("REPORT_TIME"));
                	alRecentReps[2]	=	TTKCommon.checkNull(rs.getString("PROV_RPT_DOWNL_NAME"));
                	alRecentReps[2]	=	alRecentReps[2].substring(alRecentReps[2].lastIndexOf('/')+1, alRecentReps[2].length());
                	alRecentReps[3]	=	TTKCommon.checkNull(rs.getString("PROV_RPT_DOWNL_STATUS"));
                	alRecentReps1.add(alRecentReps);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alRecentReps1;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getOverDueList(ArrayList<String> arrayList)



	public String saveShorfallDocs(Long shortfallSeqID,FormFile formFile) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		String successYN=null; 
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strShortFallDocsSave);
			byte[] iStream	=	formFile.getFileData();
			cStmtObject.setLong(1, shortfallSeqID);
			cStmtObject.setBytes(2, iStream);
			cStmtObject.registerOutParameter(3, Types.VARCHAR);
			cStmtObject.setString(4, null);
			cStmtObject.execute();
			successYN = cStmtObject.getString(3);
			return successYN;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "onlineforms");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl saveShorfallDocs()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl saveShorfallDocs()",sqlExp);
						
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl saveShorfallDocs()",sqlExp);
							
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally			
	}//end of saveShorfallDocs(shortfallVO.getShortfallSeqID())



	public String[] getMemProviderForShortfall(Long shortfallSeqID,String patOrClm) throws TTKException {
        Connection conn = null;
        CallableStatement callableStatement=null;
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            if("CLM".equals(patOrClm))
            	callableStatement	=	conn.prepareCall(strCLMShortFallDetails);
            else
            	callableStatement	=	conn.prepareCall(strShortFallDetails);
            
            callableStatement.setLong(1,shortfallSeqID);//1 
            callableStatement.registerOutParameter(2,OracleTypes.CURSOR);//2
            callableStatement.execute();
           
            rs = (java.sql.ResultSet)callableStatement.getObject(2);
            String[] strShortFallDetails	=	new String[17];
            if(rs != null){
                if (rs.next()) {
                	strShortFallDetails[0]	=	TTKCommon.checkNull(rs.getString("SHORTFALL_ID"));
                	strShortFallDetails[1]	=	TTKCommon.checkNull(rs.getString("STATUS"));
                	strShortFallDetails[2]	=	TTKCommon.checkNull(rs.getString("GENERATED_DATE"));
                	strShortFallDetails[3]	=	TTKCommon.checkNull(rs.getString("PROV_NAME"));
                	strShortFallDetails[4]	=	TTKCommon.checkNull(rs.getString("CONTACT_NO"));
                	strShortFallDetails[5]	=	TTKCommon.checkNull(rs.getString("LICENCE_NO"));
                	strShortFallDetails[6]	=	TTKCommon.checkNull(rs.getString("PROV_EMAIL"));
                	strShortFallDetails[7]	=	TTKCommon.checkNull(rs.getString("PATIENT_NAME"));
                	strShortFallDetails[8]	=	TTKCommon.checkNull(rs.getString("ALKOOT_ID"));
                	strShortFallDetails[9]	=	TTKCommon.checkNull(rs.getString("DOB"));
                	strShortFallDetails[10]	=	TTKCommon.checkNull(rs.getString("GENDER"));
                	strShortFallDetails[11]	=	TTKCommon.checkNull(rs.getString("TREATMENT_DATE"));
                	strShortFallDetails[12]	=	TTKCommon.checkNull(rs.getString("POLICY_NUMBER"));
                	strShortFallDetails[13]	=	TTKCommon.checkNull(rs.getString("PROCESSED_BY"));
                	strShortFallDetails[14]	=	TTKCommon.checkNull(rs.getString("BENEFIT_TYPE"));
                	 if("CLM".equals(patOrClm))
                     	strShortFallDetails[15]	=	TTKCommon.checkNull(rs.getString("CLAIM_NUMBER"));
                	 else
                		 strShortFallDetails[15]	=	TTKCommon.checkNull(rs.getString("PRE_AUTH_NUMBER"));
                	strShortFallDetails[16]	=	TTKCommon.checkNull(rs.getString("SHORTFALL_STATUS"));
                }//end of while(rs.next())
            }//end of if(rs != null)
            return strShortFallDetails;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "onlineforms");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "onlineforms");
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
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
				try
					{
						if (callableStatement != null) callableStatement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
						throw new TTKException(sqlExp, "onlineforms");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
		}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "onlineforms");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				callableStatement = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of String[] getMemProviderForShortfall(Long shortfallSeqID)

	public PreAuthSearchVO getPreAuthMetaData(
			CashlessDetailVO cashlessDetailVO,
			UserSecurityProfile userSecurityProfile, String string,
			String string2, Object object, String string3, String string4) throws TTKException {
		Connection conn = null;
        CallableStatement cStmtObject=null;
        Long lPreAuthSeqId= null;
        int rowsProcessed	=	0;
        ResultSet resultSet=null;
        PreAuthSearchVO preAuthSearchVO=null;
        try {
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePartnerPartialPreAuth);
            cStmtObject.setString(1, userSecurityProfile.getEmpanelNumber());
            if(cashlessDetailVO.getPreAuthSeqID()!=null)
            cStmtObject.setLong(2, cashlessDetailVO.getPreAuthSeqID()); //member Seq Id
            else
            cStmtObject.setLong(2, 0);	
            cStmtObject.setString(3, cashlessDetailVO.getEnrollId());
            cStmtObject.setString(4, cashlessDetailVO.getBenifitTypeCode());
            cStmtObject.setString(5,TTKCommon.getConvertDateToString(cashlessDetailVO.getTreatmentDate())+" "+cashlessDetailVO.getReceiveTime()+" "+cashlessDetailVO.getReceiveDay());
            cStmtObject.setString(6,TTKCommon.getConvertDateToString(cashlessDetailVO.getDischargeDate())+" "+cashlessDetailVO.getDischargeTime()+" "+cashlessDetailVO.getDischargeDay());
            cStmtObject.setDouble(7, cashlessDetailVO.getEstimatedCost());
            cStmtObject.setString(8,cashlessDetailVO.getProviderName());
            cStmtObject.setLong(9,Long.parseLong(cashlessDetailVO.getCountry()));
            cStmtObject.setString(10,cashlessDetailVO.getCurrency());
            cStmtObject.setString(11,cashlessDetailVO.getLineOfTreatment());
        	cStmtObject.setBytes(12, null);

            if(cashlessDetailVO.getGenratedReferenceId()!=null)
            cStmtObject.setString(13, cashlessDetailVO.getGenratedReferenceId());
            else
            	cStmtObject.setString(13, "");
            cStmtObject.setLong(14,userSecurityProfile.getPtrContactSeqID());
            cStmtObject.setString(15,cashlessDetailVO.getPtrEncounterType());
            cStmtObject.setString(16,string3);
            cStmtObject.setString(17,string4);
            cStmtObject.registerOutParameter(18, Types.INTEGER);
            cStmtObject.registerOutParameter(2, Types.INTEGER);
            cStmtObject.registerOutParameter(13, Types.VARCHAR);
            cStmtObject.registerOutParameter(19,OracleTypes.CURSOR);
            cStmtObject.execute();
            rowsProcessed = cStmtObject.getInt(18);
            resultSet = (java.sql.ResultSet)cStmtObject.getObject(19);
            while(resultSet.next()){
            	preAuthSearchVO=new PreAuthSearchVO();
            	preAuthSearchVO.setPreAuthNo(TTKCommon.checkNull(resultSet.getString("PRE_AUTH_NUMBER")));
            	preAuthSearchVO.setPolicyNo(TTKCommon.checkNull(resultSet.getString("policy_number")));
            	preAuthSearchVO.setPreAuthRefNo(TTKCommon.checkNull(resultSet.getString("ONL_PRE_AUTH_REFNO")));
            	preAuthSearchVO.setPatientName(TTKCommon.checkNull(resultSet.getString("MEM_NAME")));
            	preAuthSearchVO.setApprovalNo(TTKCommon.checkNull(resultSet.getString("AUTH_NUMBER")));
            	preAuthSearchVO.setProviderName(TTKCommon.checkNull(resultSet.getString("HOSP_NAME")));
            	preAuthSearchVO.setStatus(TTKCommon.checkNull(resultSet.getString("onl_pat_status")));
            	preAuthSearchVO.setPatientCardId(resultSet.getString("tpa_enrollment_id"));
            	preAuthSearchVO.setPatAuthSeqId(TTKCommon.getLong(resultSet.getString("onl_pat_auth_seq_id")));
            	preAuthSearchVO.setEventReferenceNo(TTKCommon.checkNull(resultSet.getString("onl_pre_auth_refno")));
            	preAuthSearchVO.setTotalGrossAmt(Long.parseLong(TTKCommon.checkNull(resultSet.getString("estimated_amount"))));            	
            	preAuthSearchVO.setCountryName(TTKCommon.checkNull(resultSet.getString("country_name")));
            }
            //if(rowsProcessed>0)
//            	lPreAuthSeqId	=	cStmtObject.getLong(2);
            	
        }//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "onlineformsemp");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "onlineformsemp");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
				{
					if (resultSet != null) resultSet.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in OnlineProviderDAOImpl getPreAuthSearchList()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
				}//end of catch (SQLException sqlExp)
        		finally{
        			try
            		{
            			if (cStmtObject != null) cStmtObject.close();
            		}//end of try
            		catch (SQLException sqlExp)
            		{
            			log.error("Error while closing the Statement in OnlinePreAuthDAOImpl savePreAuthDetails()",sqlExp);
            			throw new TTKException(sqlExp, "onlineformsemp");
            		}//end of catch (SQLException sqlExp)
            		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
            		{
            			try
            			{
            				if(conn != null) conn.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Connection in OnlinePreAuthDAOImpl savePreAuthDetails()",sqlExp);
            				throw new TTKException(sqlExp, "onlineformsemp");
            			}//end of catch (SQLException sqlExp)
            		}//end of finally Connection Close
        		}
        		
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "onlineformsemp");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		resultSet=null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return preAuthSearchVO;
	}

	public String saveProviderShorfallDocs(ArrayList inputData) throws TTKException {
		String successYN="";
		try(Connection connection = ResourceManager.getConnection();
		CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(strShortFallDocsSave)){
			FormFile formFile=null;
			if(inputData.get(1)!=null){
				formFile= (FormFile) inputData.get(1);
			}	
			byte[] iStream	=	null;
			if(formFile!=null)
				iStream=formFile.getFileData();
			cStmtObject.setLong(1, (Long) inputData.get(0));
			cStmtObject.setBytes(2, iStream);
			cStmtObject.registerOutParameter(3, Types.VARCHAR);
			cStmtObject.setLong(4, (Long) inputData.get(2));
			cStmtObject.execute();
			successYN = cStmtObject.getString(3);
		}catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "onlineformsemp");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "onlineformsemp");
		}
		
		return successYN;
	}
	
  public ArrayList<PartnerClaimSubmissionVo> savePartnerClaims(PartnerClaimSubmissionVo partnerClaimSubmissionVo) throws TTKException {
	  
	  ArrayList<PartnerClaimSubmissionVo> list = new ArrayList<>();
	  try(Connection connection = ResourceManager.getConnection();
		CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(strPartnerClaimSubmit)){
		  ResultSet rs = null;
		 
		  if("".equals(partnerClaimSubmissionVo.getClaimSeqID()) || partnerClaimSubmissionVo.getClaimSeqID() == null)
			  cStmtObject.setString(1,null);
		  else
			  cStmtObject.setString(1,partnerClaimSubmissionVo.getClaimSeqID());
		  
		  
		  if("".equals(partnerClaimSubmissionVo.getBatchSeqId()) || partnerClaimSubmissionVo.getBatchSeqId() ==null)
			  cStmtObject.setString(2,null);
		  else
			  cStmtObject.setString(2,partnerClaimSubmissionVo.getBatchSeqId());
		  
		  cStmtObject.setTimestamp(3, new Timestamp(partnerClaimSubmissionVo.getTreatmentDate().getTime()));
		  cStmtObject.setString(4, partnerClaimSubmissionVo.getBenifitTypeID());
		  cStmtObject.setString(5, partnerClaimSubmissionVo.getInvoiceNo());
		  cStmtObject.setBigDecimal(6, partnerClaimSubmissionVo.getRequestedAmt());
		  cStmtObject.setString(7, partnerClaimSubmissionVo.getCurrency());
		  cStmtObject.setString(8, partnerClaimSubmissionVo.getProviderName());
		 
		  if(partnerClaimSubmissionVo.getPartnerSeqId() == null)
		  
			  cStmtObject.setLong(9,0);
		  else
		  cStmtObject.setLong(9, partnerClaimSubmissionVo.getPartnerSeqId());
		 
		  
		  cStmtObject.setLong(10, Long.valueOf(partnerClaimSubmissionVo.getCountry()));
		  cStmtObject.setString(11, partnerClaimSubmissionVo.getEnrollId());
		  cStmtObject.setString(12, partnerClaimSubmissionVo.getIcdCode());
		  if("".equals(partnerClaimSubmissionVo.getIcdCodeSeqId()))
			  cStmtObject.setString(13, null); 
		  else
		  cStmtObject.setLong(13, Long.valueOf(partnerClaimSubmissionVo.getIcdCodeSeqId()));
	      
		  cStmtObject.setString(14, partnerClaimSubmissionVo.getPresentingComplaints());
		  
		 FormFile file1 =  partnerClaimSubmissionVo.getSourceAttchments1();
		 FormFile file2 =  partnerClaimSubmissionVo.getSourceAttchments2();
		 FormFile file3 =  partnerClaimSubmissionVo.getSourceAttchments3();
		 FormFile file4 =  partnerClaimSubmissionVo.getSourceAttchments4();
		 FormFile file5 =  partnerClaimSubmissionVo.getSourceAttchments5();
		 
		 
		 if(file1 != null){
			 byte[] inputstream = file1.getFileData();
			 cStmtObject.setString(15, partnerClaimSubmissionVo.getAttachmentname1());
			 cStmtObject.setBytes(16, inputstream);
		 }else{
			 cStmtObject.setString(15, null);
			 cStmtObject.setBytes(16, null); 
		 }
		 
		 if(file2 != null){
			 byte[] inputstream = file2.getFileData();
			 cStmtObject.setString(17, partnerClaimSubmissionVo.getAttachmentname2());
			 cStmtObject.setBytes(18, inputstream);
		 }else{
			 cStmtObject.setString(17, null);
			 cStmtObject.setBytes(18, null); 
		 }
		 
		 if(file3 != null){
			 byte[] inputstream = file3.getFileData();
			 cStmtObject.setString(19, partnerClaimSubmissionVo.getAttachmentname3());
			 cStmtObject.setBytes(20, inputstream);
		 }else{
			 cStmtObject.setString(19, null);
			 cStmtObject.setBytes(20, null); 
		 }
		 
		 if(file4 != null){
			 byte[] inputstream = file4.getFileData();
			 cStmtObject.setString(21, partnerClaimSubmissionVo.getAttachmentname4());
			 cStmtObject.setBytes(22, inputstream);
		 }else{
			 cStmtObject.setString(21, null);
			 cStmtObject.setBytes(22, null); 
		 }
		 
		 if(file5 != null){
			 byte[] inputstream = file5.getFileData();
			 cStmtObject.setString(23, partnerClaimSubmissionVo.getAttachmentname5());
			 cStmtObject.setBytes(24, inputstream);
		 }else{
			 cStmtObject.setString(23, null);
			 cStmtObject.setBytes(24, null); 
		 }
		 if(partnerClaimSubmissionVo.getUserSeqId() == null)
		 
			 cStmtObject.setString(25,null);
		 else
			 cStmtObject.setLong(25, partnerClaimSubmissionVo.getUserSeqId());
		 cStmtObject.registerOutParameter(26, OracleTypes.CURSOR);
		 
		 cStmtObject.execute();
		 rs = 	 (ResultSet) cStmtObject.getObject(26);
		 
		 while(rs.next()){
			 partnerClaimSubmissionVo.setMemberValidorInvalid(rs.getString("mem_valid_yn"));
			 partnerClaimSubmissionVo.setClaimBatchNo(rs.getString("clm_batch_number"));
			 partnerClaimSubmissionVo.setClaimNumber(rs.getString("claim_number"));
			 partnerClaimSubmissionVo.setInvoiceNo(rs.getString("invoice_number"));
		 }
	  }catch(SQLException e) {
		e.printStackTrace();
	}catch(Exception e1){
		e1.printStackTrace();
	}
	 
	list.add(partnerClaimSubmissionVo);
	  return list;
  }
}//end of OnlineProviderDAOImpl
