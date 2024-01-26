/**
 * @ (#) MemberDAOImpl.java Jun 14, 2006
 * Project       : TTK HealthCare Services
 * File          : MemberDAOImpl.java
 * Author        : Krishna K H
 * Company       : Span Systems Corporation
 * Date Created  : Jun 14, 2006
 *
 * @author       :  Krishna K H
 * Modified by   :	Kishor kumar S H
 * Modified date :18022015
 * Reason        :
 */

package com.ttk.dao.impl.webservice;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.driver.OracleConnection;
import oracle.sql.BLOB;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.enrollment.PolicyVO;

import oracle.jdbc.driver.OracleTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.sql.Blob;
import java.text.SimpleDateFormat;

import org.dom4j.io.SAXReader;

import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.CommunicationOptionVO;
import com.ttk.dto.webservice.AuthenticationVO;
import com.ttk.dto.webservice.OnlineIntimationVO;
import com.ttk.dto.webservice.VidalClaimWireRestVO;
import com.ttk.dto.webservice.VidalWireRestVO;

import oracle.jdbc.driver.OracleTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class WebServiceDAOImpl implements BaseDAO, Serializable {

	private static Logger log = Logger.getLogger(WebServiceDAOImpl.class );

    private static final String strSavePolicy = "{CALL POLICY_DATA_FEED_PKG.LOAD_POLICY(?,?,?,?,?)}";
    private static final String strSaveBank = "{CALL FLOAT_ACCOUNTS_PKG.UPLOAD_PAYMENT_ADVICE(?)}";
	// Call to procedure is added for bulk upload CR KOC1169
    private static final String strSaveCustBankDtls = "{CALL IFSC_BANK_DETAILS_UPDATE_ENR.UPLOAD_CUST_BNK_DTLS(?)}";
    private static final String strConsolidatedPolicyList = "{CALL POLICY_DATA_FEED_PKG.GET_POLICY_NUMBERS(?,?,?,?,?,?,?)}";
    private static final String strSaveUploadStatus = "{CALL POLICY_DATA_FEED_PKG.COMPLETE_BATCH(?,?,?)}";
    private static final String strGetTableData = "{CALL TTK_UTIL_PKG.GET_TABLE_DATA(?,?,?)}";
    private static final String strGetRuleErrors = "{CALL POLICY_DATA_FEED_PKG.GET_RULE_ERRORS(?,?)}";

    /** Following Services Written for Mobile Application */
	private static final String strIndividualLoginService = "{CALL TTK_API_PKG.INDIVIDUAL_LOGIN(?,?,?,?)}";
	
	private static final String strPolicySearchService= "{CALL TTK_API_PKG.GET_POLICY_HISTORY(?,?,?)}";//done
	private static final String strClaimHistoryService = "{CALL TTK_API_PKG.CLM_HISTORY_INFO(?,?,?,?,?)}";
	private static final String strClaimStatusService= "{CALL TTK_API_PKG.CLM_STATUS_INFO(?,?,?,?,?)}";
	private static final String strFeedBackServiceWithNewChanges = "{CALL TTK_API_PKG.MEM_HOSP_FEEDBACK_SAVE(?,?,?,?,?)}";
	private static final String strHospitalInfo = "{CALL TTK_API_PKG.TTK_HOSP_INFO(?,?,?,?,?,?,?,?)}";//added param 5th param Double in kilometers
	private static final String strHospitalDetails= "{CALL TTK_API_PKG.TTK_HOSP_LIST(?,?,?,?,?,?,?)}";//added param No of rows to be fetched
	private static final String strDependentInfo= "{CALL TTK_API_PKG.TPA_DEPENDENT_INFO(?,?,?,?)}";
	
	private static final String strTemplateInfo= "{CALL TTK_API_PKG.GET_TEMPLATE_INFO(?,?,?,?,?,?)}";
	private static final String strDailyTipInfo= "{CALL TTK_API_PKG.GET_DAILY_TIP_INFO(?,?,?)}";
	private static final String strDailyTipImage= "{CALL TTK_API_PKG.GET_DAILY_TIP_IMAGE(?,?,?)}";
	private static final String strClaimFeedBack = "{CALL TTK_API_PKG.CLM_FEEDBACK_SAVE(?,?,?,?,?)}";
	private static final String strDocAroundClockStatus = "{CALL TTK_API_PKG.DOC_AROUND_CLOCK_STATUS(?,?)}";
	private static final String strAskExpertOpnionService = "{CALL TTK_API_PKG.ASK_EXPERT_REPLY_SAVE(?,?,?,?,?,?)}";
	private static final String strClaimIntimationService = "{CALL TTK_API_PKG.CLM_INTIMATION_SAVE(?,?,?,?,?,?,?,?,?,?,?)}";//donechanges
	private static final String strPreAuthClmCount = "{CALL TTK_API_PKG.GET_PRE_CLM_COUNT(?,?,?,?)}";


	   //vidalwire request services
		private static final String strloginValidation = "{CALL VIRE_MOB_APP_PCK.LOGIN_APP(?,?,?,?,?,?,?)}";
		private static final String strloginRegister = "{CALL VIRE_MOB_APP_PCK.GENERATE_OTP(?,?,?,?,?,?)}";
		private static final String strloginRegisterOTP = "{CALL VIRE_MOB_APP_PCK.CREATE_NEW_USER(?,?,?,?,?,?)}";
		private static final String strForgotLoginService = "{CALL VIRE_MOB_APP_PCK.FORGOT_PASSWORD(?,?,?)}";
		private static final String strMobileAppDepDetails = "{CALL VIRE_MOB_APP_PCK.DEPENDENT_DETAILS(?,?)}";
		private static final String strMobileAppPreauthList = "{CALL VIRE_MOB_APP_PCK.PRE_AUTH_LIST(?,?)}";
		private static final String strMobileAppPreauthDetails = "{CALL VIRE_MOB_APP_PCK.PRE_AUTH_DETAILS(?,?,?,?)}";
		private static final String strGetPolicyList = "{CALL VIRE_MOB_APP_PCK.MY_POLICY_DETAILS(?,?,?,?)}";
		private static final String strMobileAppClaimList = "{CALL VIRE_MOB_APP_PCK.CLAIMS_LIST(?,?)}";
		private static final String strMobileAppClaimDetails = "{CALL VIRE_MOB_APP_PCK.CLAIM_DETAILS(?,?,?,?,?)}";
		private static final String strGetMyProfile = "{CALL VIRE_MOB_APP_PCK.MY_PROFILE(?,?)}";
		private static final String save_my_profile = "{CALL VIRE_MOB_APP_PCK.SAVE_MY_PROFILE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		private static final String NWtype_Member = "{CALL VIRE_MOB_APP_PCK.GET_NETWORK_TYPE(?,?)}";

		
		
		private static final String strDailyTipImageMob = "{CALL VIRE_MOB_APP_PCK.GET_DAILY_TIP_IMAGE(?,?)}";
		private static final String strquery_ans_details= "{CALL VIRE_MOB_APP_PCK.QUERY_ANS_DETAILS(?)}";
		private static final String strSaveMedReminder = "{CALL VIRE_MOB_APP_PCK.SAVE_MED_REMINDER(?,?,?,?,?,?,?,?,?,?)}";
		private static final String strSelectMedReminders = "{CALL VIRE_MOB_APP_PCK.SELECT_MED_REMINDER(?,?)}";
		private static final String strSelectMedRemindersHistory = "{CALL VIRE_MOB_APP_PCK.SELECT_MED_REM_HISTORY(?,?)}";
	
		private static final String strSaveClaimSubmisson = "{CALL VIRE_MOB_APP_PCK.SUBMIT_CLAIMS(?,?,?,?,?,?,?,?,?,?,?)}";
		private static final String strgetClaimSubmisson = "{CALL VIRE_MOB_APP_PCK.SELECT_CLOB(?,?)}";
		private static final String strClaimSubmission = "{CALL VIRE_MOB_APP_PCK.EMAIL_ID_CHECK(?,?,?,?,?)}";
		private static final String Countrydetails =  "SELECT C.COUNTRY_ID,C.COUNTRY_NAME FROM APP.TPA_COUNTRY_CODE C WHERE NVL(C.DISPLY_MOB_APP_YN,'N') = 'Y' ORDER BY C.COUNTRY_NAME";

		private static final String Statedetails = "SELECT S.STATE_TYPE_ID,S.STATE_NAME FROM APP.TPA_STATE_CODE S WHERE S.COUNTRY_ID = ? ORDER BY S.SORT_NO";
		private static final String Areadetails = "SELECT C.CITY_TYPE_ID,C.CITY_DESCRIPTION FROM APP.TPA_STATE_CODE S JOIN APP.TPA_CITY_CODE C ON (S.STATE_TYPE_ID=C.STATE_TYPE_ID) WHERE S.STATE_TYPE_ID = ? ORDER BY C.CITY_DESCRIPTION";
		private static final String proTypeId = "SELECT PROVIDER_TYPE_ID,PROVIDER_NAME FROM DHA_PROVIDER_TYPE ORDER BY SORT_NO";
		private static final String proSpeciality = "SELECT H.HOSP_TYPE_ID,H.DESCRIPTION FROM TPA_HOSP_CODE H ORDER BY H.SORT_NO";
		private static final String strMobProviderSearchList	=	"{CALL VIRE_MOB_APP_PCK.PROVIDER_LIST(?,?,?,?,?,?,?,?,?,?)}";
		private static final String strGender	=	"SELECT GENERAL_TYPE_ID,DESCRIPTION FROM TPA_GENERAL_CODE WHERE HEADER_TYPE = 'GENDER_DETAILS' ORDER BY SORT_NO";
		private static final String strNetworkType	=	"SELECT GENERAL_TYPE_ID,DESCRIPTION FROM APP.TPA_GENERAL_CODE WHERE HEADER_TYPE='PROVIDER_NETWORK' ORDER BY SORT_NO";
				
		
		private static final String strClaimSearch = "{CALL HYBRID_MOBILE_APP.CLM_INTIMATION_LIST(?,?,?,?,?,?)}";
		private static final String strEcardSearch = "{CALL HYBRID_MOBILE_APP.SELECT_DEPEDENT_LIST(?,?)}";
		private static final String strCoverageDetails = "{CALL HYBRID_MOBILE_APP.SELECT_COVERAGE_DTL(?,?)}";
		private static final String strCoverageDetailsPlan = "{CALL HYBRID_MOBILE_APP.SELECT_POLICY_COVRAGE(?,?)}";
		private static final String strSaveAppClaim = "{CALL HYBRID_MOBILE_APP.CLM_INTIMATION_SAVE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		private static final String strSaveFeedback = "{CALL HYBRID_MOBILE_APP.SAVE_POTAL_FEEDBACK_DTL(?,?,?,?,?,?,?)}";
		private static final String strMobileSendMail = "{CALL GENERATE_MAIL_PKG.proc_form_message (?,?,?,?,?,?,?)}";
		private static final String strDeleteAppClaim = "{CALL HYBRID_MOBILE_APP.INTIMATION_DELETION(?,?,?,?)}";
		private static final String strEcardPrint = "{CALL HYBRID_MOBILE_APP.SELECT_DATA_FOR_CARD_PRINT(?,?,?)}";
		private static final String strSelectAppClaim = "{CALL HYBRID_MOBILE_APP.SELECT_CLM_SUB_LIST(?,?,?,?,?,?)}";
		private static final String strClaimAppHistoryList = "{CALL HYBRID_MOBILE_APP.CREATE_CLAIM_XML(?,?,?)}";
		private static final String strSelectAppSummaryClaim = "SELECT A.MEMBER_SEQ_ID,A.TPA_ENROLLMENT_ID || ' / ' ||A.MEM_NAME AS MEMBER_NAME FROM TPA_ENR_POLICY_MEMBER A WHERE A.POLICY_GROUP_SEQ_ID=? AND A.DELETED_YN='N'";
		private static final String strChangePwdLoginService = "{CALL hybrid_mobile_app.change_password(?,?,?,?,?,?)}";
		private static final String medTimeTypeId="SELECT G.GENERAL_TYPE_ID,G.DESCRIPTION FROM APP.TPA_GENERAL_CODE G WHERE G.HEADER_TYPE = 'MED_TIME_TYPE_ID'";
		private static final String intervaldetails="SELECT G.GENERAL_TYPE_ID,G.DESCRIPTION FROM APP.TPA_GENERAL_CODE G WHERE G.HEADER_TYPE = 'INTERVAL_TYPE_ID'";
		private static final String strgetHospSeqId="SELECT HOSP_SEQ_ID FROM APP.TPA_HOSP_INFO WHERE EMPANEL_NUMBER = ?";				
		private static final String strgetUserSeqId="SELECT CONTACT_SEQ_ID FROM APP.TPA_LOGIN_INFO WHERE USER_ID = ?";
	

	//ProjectX
	//private static final String strFetchXml = "{CALL app.GEN_MEM_XML(?,?,?)}";
	private static final String strFetchXml = "{CALL mohan_babu.sample_xml(?)}";

	/*private static final String strStateListService = "{CALL TTK_API_PKG.STATE_XML(?)}";
	private static final String strCityListService = "{CALL TTK_API_PKG.CITY_XML(?,?)}";
	private static final String strCity_StateListService = "{CALL TTK_API_PKG.STATE_CITY_XML(?)}";

*/
	//SelfFund - added by Kishor
	private static final String strValidatePhoneNum	=	"{CALL health_4_sure_login.cust_login(?,?)}";
	private static final String strProviderList		=	"{CALL health_4_sure_login.select_provider_list(?,?,?,?,?,?)}";
	private static final String strGetDiagList		=	"{CALL health_4_sure_login. select_diag_tests(?,?)}";
	
	private static final String strsendSMSMailImmediate = "{CALL GENERATE_MAIL_PKG.proc_send_scheduled_otp(?,?)}";
	
	private static final String strDeleteMedReminders = "{CALL VIRE_MOB_APP_PCK.DELETE_MED_REMINDER(?,?)}";
	private static final String strpolicyTOBdownload = "{CALL VIRE_MOB_APP_PCK.SELECT_POLICY_TOB_DOC(?,?)}";
	private static final String strAccountType = "SELECT GENERAL_TYPE_ID,DESCRIPTION FROM APP.TPA_GENERAL_CODE WHERE HEADER_TYPE='ACCOUNT_TYPE'";
	private static final String strBank_destination = "SELECT DISTINCT A.BANK_NAME FROM APP.TPA_IFSC_CODE_DETAILS A ORDER BY A.BANK_NAME";
	private static final String strBank_state = "SELECT DISTINCT A.STATE_TYPE_ID,A.STATE_NAME FROM APP.TPA_STATE_CODE A JOIN APP.TPA_ENR_BANK_NAME B ON(A.STATE_TYPE_ID=B.STATE_TYPE_ID)WHERE B.BANK_NAME=? ORDER BY A.STATE_NAME";
	private static final String strBank_city = "SELECT  DISTINCT B.CITY_TYPE_ID,B.CITY_DESCRIPTION  FROM  APP.TPA_STATE_CODE A JOIN APP.TPA_CITY_CODE B ON(A.STATE_TYPE_ID=B.STATE_TYPE_ID) JOIN APP.TPA_IFSC_CODE_DETAILS C ON(B.CITY_TYPE_ID=C.CITY_TYPE_ID)WHERE C.STATE_TYPE_ID=? AND C.BANK_NAME=? ORDER BY B.CITY_DESCRIPTION"; 
	private static final String strBank_branch = "SELECT DISTINCT A.BRANCH_SEQ_ID,ttk_util_pkg.fn_decrypt(A.BRANCH_NAME) as BRANCH_NAME ,A.CITY_TYPE_ID,A.STATE_TYPE_ID FROM APP.TPA_BRANCH_CODE A join APP.TPA_IFSC_CODE_DETAILS B on (b.BRANCH_SEQ_ID=a.branch_seq_id) WHERE b.STATE_TYPE_ID=? AND b.CITY_TYPE_ID=? AND B.BANK_NAME =? ORDER BY BRANCH_NAME ";

	private static final String BenifitTypeList =  "SELECT G.GENERAL_TYPE_ID,G.DESCRIPTION FROM APP.TPA_GENERAL_CODE G WHERE G.HEADER_TYPE = 'FORM_TYPE'";
	private static final String strWindowPeriod =  "select VIRE_MOB_APP_PCK.chk_exp_member(?) from dual";

	
	private static final String strSaveShortfallDetails = "{CALL VIRE_MOB_APP_PCK.save_shortfall_details(?,?,?,?)}";
	
	private static final String strSaveUploadFils = "{CALL 	ALAHLI_PAT_DATA_UPLD_PKG.SAVE_PREAUTH_DOCUMENT(?,?,?,?)}";
	
	private static final String strMemberBenefitLimit = "{CALL VIRE_MOB_APP_PCK.GET_BENEFIT_LIMIT (?,?,?)}";
	private static final String strBenifitTypeListNew = "SELECT CASE GC.GENERAL_TYPE_ID WHEN 'IMTI' THEN 'MTI' WHEN 'PAHC' THEN 'OPHR' ELSE GC.GENERAL_TYPE_ID END AS GENERAL_TYPE_ID ,CASE GC.DESCRIPTION WHEN 'IN-PATIENT MATERNITY' THEN 'Maternity' WHEN 'PALLIATIVE AND HOSPICE CARE BENEFIT' THEN 'OP Pharmacy' ELSE INITCAP(GC.DESCRIPTION) END AS DESCRIPTION FROM APP.TPA_GENERAL_CODE GC WHERE GC.HEADER_TYPE='BENIFIT_TYPE' and GC.GENERAL_TYPE_ID in ('OPTS','DNTL','OPTC','IMTI','PAHC')";
	
	private static final String update_dependent_details = "{CALL VIRE_MOB_APP_PCK.UPDATE_MEMBER_CONTACT_DET(?,?,?,?)}";
	private static final String send_certificate_mail = "{CALL VIRE_MOB_APP_PCK.POLICY_CERTIFICATE_REQUEST(?,?)}";
	private static final String strGetAppVersionDetails = "{CALL VIRE_MOB_APP_PCK.update_mobile_app(?,?,?)}";
	
	/**
     * This method saves the Policy information
     * @param String object which contains the policy and member details
     * @return String object which contains
     * @exception throws TTKException
     */
	
	
	  public VidalWireRestVO getLoginService(VidalWireRestVO vidalwirerestvo) throws TTKException {
	    	String alertMsg = null;
	    	String status = null;
			String strPolicyGroupSeqID = null;
			String strGroupId = null;
			String strPolicyNo = null;
			Connection conn = null;	
			CallableStatement cStmtObject=null;
			

			try {
				
				conn = ResourceManager.getConnection();
				cStmtObject =(OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strloginValidation);
			
			
				cStmtObject.setString(1,vidalwirerestvo.getUsername());
				cStmtObject.setString(2,vidalwirerestvo.getPassword());			
			  	
			  	cStmtObject.registerOutParameter(3,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(4,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(5,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(6,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(7,Types.VARCHAR);
				cStmtObject.execute();
				
				 
				 status=TTKCommon.checkNull(cStmtObject.getString(3));	
				 vidalwirerestvo.setLoginStatus(status);
				 
				 alertMsg = TTKCommon.checkNull(cStmtObject.getString(4));
				 vidalwirerestvo.setPwdAlertMsg(alertMsg);
				
				 
				 strPolicyGroupSeqID=TTKCommon.checkNull(cStmtObject.getString(5));	
				 vidalwirerestvo.setPolicyGroupSeqID(strPolicyGroupSeqID);
				 
				 strPolicyNo=TTKCommon.checkNull(cStmtObject.getString(6));	
				 vidalwirerestvo.setPolicyNo(strPolicyNo);
				 
				 strGroupId=TTKCommon.checkNull(cStmtObject.getString(7));
				 vidalwirerestvo.setGroupID(strGroupId);
				 

			}//end of try
	       catch (SQLException sqlExp)
		   {
			throw new TTKException(sqlExp, "webservice");
		    }//end of catch (SQLException sqlExp)
		 catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			// Nested Try Catch to ensure resource closure 
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return vidalwirerestvo;
	}//end o
	    
	    
	    public VidalWireRestVO getLoginRegister(VidalWireRestVO vidalwirerestvo) throws TTKException {
	    	String alertMsg = null;
	    	String status = null;
			String strPolicyGroupSeqID = null;
		    String strOTP= null;
			Connection conn = null;	
			CallableStatement cStmtObject=null;
			

			try {
				
				conn = ResourceManager.getConnection();
				cStmtObject =(OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strloginRegister);
			
				
				cStmtObject.setString(1,vidalwirerestvo.getUsername());
				cStmtObject.setString(2,vidalwirerestvo.getPassword());			
			  	
			  	cStmtObject.registerOutParameter(3,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(4,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(5,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(6,Types.VARCHAR);
	
				cStmtObject.execute();
				
				strOTP = TTKCommon.checkNull(cStmtObject.getString(3));
				 vidalwirerestvo.setOtpNumber(strOTP);
				 
				 strPolicyGroupSeqID=TTKCommon.checkNull(cStmtObject.getString(4));	
				 vidalwirerestvo.setPolicyGroupSeqID(strPolicyGroupSeqID);
				 
				 status=TTKCommon.checkNull(cStmtObject.getString(5));	
				 vidalwirerestvo.setLoginStatus(status);
				 
				 alertMsg = TTKCommon.checkNull(cStmtObject.getString(6));
				 vidalwirerestvo.setPwdAlertMsg(alertMsg);
				 
						 

			}//end of try
	       catch (SQLException sqlExp)
		   {
			throw new TTKException(sqlExp, "webservice");
		    }//end of catch (SQLException sqlExp)
		 catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			// Nested Try Catch to ensure resource closure 
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return vidalwirerestvo;
	}//end o
	    
	    
	    public VidalWireRestVO getLoginRegisterOTP(VidalWireRestVO vidalwirerestvo) throws TTKException {
	    	String alertMsg = null;
	    	String status = null;
			Connection conn = null;	
			CallableStatement cStmtObject=null;
			

			try {
				
				conn = ResourceManager.getConnection();
				cStmtObject =(OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strloginRegisterOTP);
			
				
				cStmtObject.setString(1,vidalwirerestvo.getUsername());
				cStmtObject.setString(2,vidalwirerestvo.getPassword());		
				cStmtObject.setString(3,vidalwirerestvo.getOtpNumber());	
				cStmtObject.setString(4,vidalwirerestvo.getPolicyGroupSeqID());			

			  	cStmtObject.registerOutParameter(5,Types.VARCHAR);
			  	cStmtObject.registerOutParameter(6,Types.VARCHAR);
	
				cStmtObject.execute();
				
							 
				 status=TTKCommon.checkNull(cStmtObject.getString(5));	
				 vidalwirerestvo.setLoginStatus(status);
				 
				 alertMsg = TTKCommon.checkNull(cStmtObject.getString(6));
				 vidalwirerestvo.setPwdAlertMsg(alertMsg);
				 
						 

			}//end of try
	       catch (SQLException sqlExp)
		   {
			throw new TTKException(sqlExp, "webservice");
		    }//end of catch (SQLException sqlExp)
		 catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			// Nested Try Catch to ensure resource closure 
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return vidalwirerestvo;
	}//end o
	    
	    
	    public VidalWireRestVO getLoginForgetPwdService(VidalWireRestVO vidalwirerestvo)throws TTKException {
				Connection conn = null;
				CallableStatement cStmtObject=null;
				String status ="";
				String alertMsg = "";
				try {
					log.info("INSIDE LOGIN PAGE DAO START");
					conn = ResourceManager.getConnection();
					cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strForgotLoginService);
					cStmtObject.setString(1,vidalwirerestvo.getUsername());
				
					cStmtObject.registerOutParameter(2,Types.VARCHAR);
					cStmtObject.registerOutParameter(3,Types.VARCHAR);
					cStmtObject.execute();
					 status=TTKCommon.checkNull(cStmtObject.getString(2));	
					 vidalwirerestvo.setLoginStatus(status);
					 
					 alertMsg = TTKCommon.checkNull(cStmtObject.getString(3));
					 vidalwirerestvo.setPwdAlertMsg(alertMsg);
				
				}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "webservice");
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
							log.error("Error while closing the Statement in WebServiceDAOImpl getLoginForgetPwdService",sqlExp);
							throw new TTKException(sqlExp, "webservice");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in WebServiceDAOImpl getLoginForgetPwdService",sqlExp);
								throw new TTKException(sqlExp, "webservice");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "webservice");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects
					{
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
				return vidalwirerestvo;
		    }//end of getLoginForgetPwdService(String strUsername)
	    
	    
	    @SuppressWarnings({ "unused", "rawtypes", "null" })
	    public ArrayList getDependentList(OnlineIntimationVO onlineIntimationVO)  throws TTKException {
	       
	       	ArrayList<Object> alSearchList = new ArrayList<Object>();
	       	ArrayList<Object> alFinalList = new ArrayList<Object>();
	   		Connection conn = null;	
	   		CallableStatement cStmtObject=null;
	   		ResultSet rs =null;
	        JSONArray jsonArray = new JSONArray();

	   		//Calendar calendar = Calendar.getInstance();
	   		//StringBuffer sb=null;

	   		try {
	   			conn = ResourceManager.getConnection();
	   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMobileAppDepDetails);
	   			cStmtObject.setLong(1,onlineIntimationVO.getPolicyGrpSeqID());
	   	
				
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
				cStmtObject.execute();					
				rs = (ResultSet)cStmtObject.getObject(2);
				
				if(rs != null)
				{
					 while(rs.next())
					 {
						 
						 JSONObject jsonObject = new JSONObject();
						// profileDetailMobileVO = new ProfileDetailMobileVO();
					
						jsonObject.put("MEMBER_SEQ_ID",rs.getString("member_seq_id")!=null?(String)rs.getString("member_seq_id"):"");
						jsonObject.put("MEMBER_NAME",rs.getString("mem_name")!=null?(String)rs.getString("mem_name"):"");
						jsonObject.put("MEMBER_DOB",rs.getString("date_of_birth")!=null?(String)rs.getString("date_of_birth"):"");
						jsonObject.put("GENDER",rs.getString("gender")!=null?(String)rs.getString("gender"):"");
						jsonObject.put("RELATIONSHIP",rs.getString("relship_description")!=null?(String)rs.getString("relship_description"):"");
						jsonObject.put("ENROLLMENT_ID",rs.getString("tpa_enrollment_id")!=null?(String)rs.getString("tpa_enrollment_id"):"");
						jsonObject.put("ECARD_TEMPLATE",rs.getString("TEMPLATE_NAME")!=null?(String)rs.getString("TEMPLATE_NAME"):"");
						jsonObject.put("PRINCIPAL_EMAIL",rs.getString("principal_Email")!=null?(String)rs.getString("principal_Email"):"");
						 alSearchList.add(jsonObject);
					 }
					 
					 String Status="";
					 String EnrollFlag="";

					 if(alSearchList != null && alSearchList.size() >0)
					 {
						 Status ="Search Successfully";
						 EnrollFlag = "Y";
											 
					 }
					 else{
						 Status ="No data Found";
						 EnrollFlag = "N";
					 }
					 
					 alFinalList.add(alSearchList);
					 alFinalList.add(Status);
					 alFinalList.add(EnrollFlag);
					 
					
				}
	   		}//end of try
	          catch (SQLException sqlExp)
	   	   {
	   		throw new TTKException(sqlExp, "webservice");
	   	    }//end of catch (SQLException sqlExp)
	   	 catch (Exception exp)
	   	{
	   		throw new TTKException(exp, "webservice");
	   	}//end of catch (Exception exp)

	   	finally
	   	{
	   		// Nested Try Catch to ensure resource closure 
	   		try // First try closing the Statement
	   		{
	   			try
	   			{
	   				if(rs != null) rs.close();
	   				if (cStmtObject != null) cStmtObject.close();
	   			}//end of try
	   			catch (SQLException sqlExp)
	   			{
	   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	   				throw new TTKException(sqlExp, "webservice");
	   			}//end of catch (SQLException sqlExp)
	   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	   			{
	   				try
	   				{
	   					if(conn != null) conn.close();
	   					
	   				}//end of try
	   				catch (SQLException sqlExp)
	   				{
	   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	   					throw new TTKException(sqlExp, "webservice");
	   				}//end of catch (SQLException sqlExp)
	   			}//end of finally Connection Close
	   		}//end of try
	   		catch (TTKException exp)
	   		{
	   			throw new TTKException(exp, "webservice");
	   		}//end of catch (TTKException exp)
	   		finally // Control will reach here in anycase set null to the objects
	   		{
	   			rs = null;
	   			cStmtObject = null;
	   			conn = null;
	   			
	   		}//end of finally
	   	}//end of finally
	   		return alFinalList;
	   }//end o
	    
	    @SuppressWarnings({ "unused", "rawtypes", "null" })
	    public ArrayList getPreAuthList(OnlineIntimationVO onlineIntimationVO)  throws TTKException {
	       
	       	ArrayList<Object> alSearchList = new ArrayList<Object>();
	       	ArrayList<Object> alPreapprovalList = new ArrayList<Object>();
	   		Connection conn = null;	
	   		CallableStatement cStmtObject=null;
	   		ResultSet rs =null;
	   		VidalClaimWireRestVO vidalClaimWireRestVO=null;

	   		try {
	   			conn = ResourceManager.getConnection();
	   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMobileAppPreauthList);
	   			cStmtObject.setLong(1,onlineIntimationVO.getMemberSeqID());
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
				

				cStmtObject.execute();					
				rs = (ResultSet)cStmtObject.getObject(2);
				
				if(rs != null)
				{
					 while(rs.next())
					 {
						 JSONObject jsonObject = new JSONObject();

						 
						 vidalClaimWireRestVO = new VidalClaimWireRestVO();
						 jsonObject.put("PREAUTH_SEQ_ID",rs.getString("PAT_AUTH_SEQ_ID")!=null?(String)rs.getString("PAT_AUTH_SEQ_ID"):"");
						 jsonObject.put("PREAUTH_NUMBER",rs.getString("PRE_AUTH_NUMBER")!=null?(String)rs.getString("PRE_AUTH_NUMBER"):"");
						 jsonObject.put("DECISION_DATE",rs.getString("DECISION_DATE")!=null?(String)rs.getString("DECISION_DATE"):"");
						 jsonObject.put("PAT_STATUS",rs.getString("PAT_STATUS")!=null?(String)rs.getString("PAT_STATUS"):"");
						 jsonObject.put("HOSPITAL_NAME",rs.getString("HOSP_NAME")!=null?(String)rs.getString("HOSP_NAME"):"");

						
						 alPreapprovalList.add(jsonObject);
					 }
					 alSearchList.add(alPreapprovalList);

				}
	   		}//end of try
	          catch (SQLException sqlExp)
	   	   {
	   		throw new TTKException(sqlExp, "webservice");
	   	    }//end of catch (SQLException sqlExp)
	   	 catch (Exception exp)
	   	{
	   		throw new TTKException(exp, "webservice");
	   	}//end of catch (Exception exp)

	   	finally
	   	{
	   		// Nested Try Catch to ensure resource closure 
	   		try // First try closing the Statement
	   		{
	   			try
	   			{
	   				if(rs != null) rs.close();
	   				if (cStmtObject != null) cStmtObject.close();
	   			}//end of try
	   			catch (SQLException sqlExp)
	   			{
	   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	   				throw new TTKException(sqlExp, "webservice");
	   			}//end of catch (SQLException sqlExp)
	   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	   			{
	   				try
	   				{
	   					if(conn != null) conn.close();
	   					
	   				}//end of try
	   				catch (SQLException sqlExp)
	   				{
	   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	   					throw new TTKException(sqlExp, "webservice");
	   				}//end of catch (SQLException sqlExp)
	   			}//end of finally Connection Close
	   		}//end of try
	   		catch (TTKException exp)
	   		{
	   			throw new TTKException(exp, "webservice");
	   		}//end of catch (TTKException exp)
	   		finally // Control will reach here in anycase set null to the objects
	   		{
	   			rs = null;
	   			cStmtObject = null;
	   			conn = null;
	   			
	   		}//end of finally
	   	}//end of finally
	   		return alSearchList;
	   }//end o
	
	    
	    @SuppressWarnings({ "unused", "rawtypes", "null" })
	    public ArrayList getPreAuthDetails(VidalClaimWireRestVO vidalClaimWireRestVO)  throws TTKException {
	    	
	    	
	       	ArrayList<Object> alSearchList = new ArrayList<Object>();
	       	ArrayList<Object> alFinalList = new ArrayList<Object>();
	       	ArrayList<Object> alactivityList = new ArrayList<Object>();
	       	ArrayList<Object> aldenailList = new ArrayList<Object>();
	       	ArrayList<Object> alresult = new ArrayList<Object>();
	       	

	   		Connection conn = null;	
	   		CallableStatement cStmtObject=null;
	   		ResultSet rs =null;
	       	ResultSet rs1 =null;
	       	ResultSet rs2 =null;
	
			 JSONObject jsonObject = new JSONObject();
			 JSONObject jsonObjectact = new JSONObject();
			 JSONObject jsonObjgeneral = new JSONObject();
			 JSONObject objresult = new JSONObject();

			 JSONArray jsonArrGeneral = new JSONArray();
			 JSONArray jsonArrActivity = new JSONArray();
			 JSONArray jsonArrDenail = new JSONArray();



	   		try {
	   			conn = ResourceManager.getConnection();
	   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMobileAppPreauthDetails);
	   			cStmtObject.setLong(1,vidalClaimWireRestVO.getPreAuthSeqID());
				
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);   
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);   

				cStmtObject.execute();					
				rs = (ResultSet)cStmtObject.getObject(2);
				rs1 = (ResultSet)cStmtObject.getObject(3);
				rs2 = (ResultSet)cStmtObject.getObject(4);

				
				if(rs != null)
				{
					 while(rs.next())
					 {

						 
								
						 jsonObjgeneral.put("PREAUTH_SEQ_ID",rs.getString("PAT_AUTH_SEQ_ID")!=null?(String)rs.getString("PAT_AUTH_SEQ_ID"):"");
						 jsonObjgeneral.put("PREAUTH_NUMBER",rs.getString("PRE_AUTH_NUMBER")!=null?(String)rs.getString("PRE_AUTH_NUMBER"):"");
						 jsonObjgeneral.put("DECISION_DATE",rs.getString("DECISION_DATE")!=null?(String)rs.getString("DECISION_DATE"):"");
						 jsonObjgeneral.put("PAT_STATUS",rs.getString("PAT_STATUS")!=null?(String)rs.getString("PAT_STATUS"):"");
						 jsonObjgeneral.put("SHORTFALL_NO",rs.getString("SHORTFALL_NO")!=null?(String)rs.getString("SHORTFALL_NO"):"");
						 
						 jsonObjgeneral.put("PROVIDER_NAME",rs.getString("PROVIDER_NAME")!=null?(String)rs.getString("PROVIDER_NAME"):"");
						 jsonObjgeneral.put("PROV_CONTACT_NO",rs.getString("PROV_CONTACT_NO")!=null?(String)rs.getString("PROV_CONTACT_NO"):"");
						 jsonObjgeneral.put("PROVIDER_LICENSE_NO",rs.getString("PROVIDER_LICENSE_NO")!=null?(String)rs.getString("PROVIDER_LICENSE_NO"):"");
						 jsonObjgeneral.put("HOSP_MAIL_ID",rs.getString("HOSP_MAIL_ID")!=null?(String)rs.getString("HOSP_MAIL_ID"):"");
						 jsonObjgeneral.put("PATIENT_NAME",rs.getString("PATIENT_NAME")!=null?(String)rs.getString("PATIENT_NAME"):"");

						 jsonObjgeneral.put("AL_KOOT_ID_NO",rs.getString("AL_KOOT_ID_NO")!=null?(String)rs.getString("AL_KOOT_ID_NO"):"");
						 jsonObjgeneral.put("DATE_OF_BIRTH",rs.getString("DATE_OF_BIRTH")!=null?(String)rs.getString("DATE_OF_BIRTH"):"");
						 jsonObjgeneral.put("POLICY_NUMBER",rs.getString("POLICY_NUMBER")!=null?(String)rs.getString("POLICY_NUMBER"):"");
						 jsonObjgeneral.put("GENDER",rs.getString("GENDER")!=null?(String)rs.getString("GENDER"):"");
						 jsonObjgeneral.put("CLINICIAN_NAME",rs.getString("CLINICIAN_NAME")!=null?(String)rs.getString("CLINICIAN_NAME"):"");
						 
						 jsonObjgeneral.put("CLINICIAN_LICENSE_NO",rs.getString("CLINICIAN_LICENSE_NO")!=null?(String)rs.getString("CLINICIAN_LICENSE_NO"):"");
						 jsonObjgeneral.put("BENEFIT_TYPE",rs.getString("BENEFIT_TYPE")!=null?(String)rs.getString("BENEFIT_TYPE"):"");
						 jsonObjgeneral.put("DATE_OF_TREATMENT",rs.getString("DATE_OF_TREATMENT")!=null?(String)rs.getString("DATE_OF_TREATMENT"):"");
						 jsonObjgeneral.put("DIAGNOSIS_TYPE",rs.getString("DIAGNOSIS_TYPE")!=null?(String)rs.getString("DIAGNOSIS_TYPE"):"");
						 jsonObjgeneral.put("DIAGNOSYS_CODE",rs.getString("DIAGNOSYS_CODE")!=null?(String)rs.getString("DIAGNOSYS_CODE"):"");
						 
						 jsonObjgeneral.put("DESCRIPTION",rs.getString("DESCRIPTION")!=null?(String)rs.getString("DESCRIPTION"):"");
						 jsonObjgeneral.put("TOT_DISC_GROSS_AMOUNT",rs.getString("TOT_DISC_GROSS_AMOUNT")!=null?(String)rs.getString("TOT_DISC_GROSS_AMOUNT"):"");
						 jsonObjgeneral.put("COPAY_AMOUNT",rs.getString("COPAY_AMOUNT")!=null?(String)rs.getString("COPAY_AMOUNT"):"");
						 jsonObjgeneral.put("DEDUCT_AMOUNT",rs.getString("DEDUCT_AMOUNT")!=null?(String)rs.getString("DEDUCT_AMOUNT"):"");
						 jsonObjgeneral.put("FINAL_APP_AMOUNT",rs.getString("FINAL_APP_AMOUNT")!=null?(String)rs.getString("FINAL_APP_AMOUNT"):"");
						 jsonObjgeneral.put("MEDICAL_OPINION_REMARKS",rs.getString("MEDICAL_OPINION_REMARKS")!=null?(String)rs.getString("MEDICAL_OPINION_REMARKS"):"");
						 jsonObjgeneral.put("TOTAL_REQUESTED_AMOUNT",rs.getString("REQ_AMT")!=null?(String)rs.getString("REQ_AMT"):"");
					/*	 jsonObjgeneral.put("SHORTFALL_QUESTIONS",rs.getString("SHORTFALL_QUESTIONS")!=null?(String)rs.getString("SHORTFALL_QUESTIONS"):"");
						 System.out.println("SHORTFALL_QUESTIONS END:::::::"+rs.getString("SHORTFALL_QUESTIONS"));
						 jsonObjgeneral.put("shortfall_flag",rs.getString("shortfall_flag")!=null?(String)rs.getString("shortfall_flag"):"");
						 System.out.println("PreAuthDetails shortfall_flag :::::::::::"+rs.getString("shortfall_flag"));*/
						// jsonArrGeneral.put(jsonObjgeneral);
						 alSearchList.add(jsonObjgeneral);

						 
						 
					 }
					 
					 if(rs1 != null)
						{
							 while(rs1.next())
							 {
								 JSONObject jsonDenail = new JSONObject();
								jsonDenail.put("DENIAL_CODE",rs1.getString("DENIAL_CODE")!=null?(String)rs1.getString("DENIAL_CODE"):"");
								jsonDenail.put("DENIAL_DESCRIPTION",rs1.getString("DENIAL_DESCRIPTION")!=null?(String)rs1.getString("DENIAL_DESCRIPTION"):"");
								aldenailList.add(jsonDenail);
								
							 }
							/*for(int k=0;k<alactivityList.size();k++)
								System.out.println("akl data::"+alactivityList.get(k));*/
							// jsonArrDenail.put(aldenailList);
						}
						
					 if(rs2 != null)
						{
							 while(rs2.next())
							 {
								 JSONObject obj = new JSONObject();
								obj.put("START_DATE",rs2.getString("START_DATE")!=null?(String)rs2.getString("START_DATE"):"");
								obj.put("ACTIVITY_CODE",rs2.getString("ACTIVITY_CODE")!=null?(String)rs2.getString("ACTIVITY_CODE"):"");
								obj.put("ACTIVITY_DESCRIPTION",rs2.getString("ACTIVITY_DESCRIPTION")!=null?(String)rs2.getString("ACTIVITY_DESCRIPTION"):"");
								obj.put("AMOUNT_CLAIMED",rs2.getString("AMOUNT_CLAIMED")!=null?(String)rs2.getString("AMOUNT_CLAIMED"):"");
								obj.put("DISALLOWED_AMOUNT",rs2.getString("DISALLOWED_AMOUNT")!=null?(String)rs2.getString("DISALLOWED_AMOUNT"):"");
								obj.put("AMOUNT_APPROVED",rs2.getString("AMOUNT_APPROVED")!=null?(String)rs2.getString("AMOUNT_APPROVED"):"");
								obj.put("REMARKS",rs2.getString("REMARKS")!=null?(String)rs2.getString("REMARKS"):"");

								alactivityList.add(obj);
								
							 }
							
						}
					 String Status = "";
					 String PreauthFlag = "";
					 if(alSearchList != null && alSearchList.size() >0)
					 {
						 Status ="Search Successfully";
						 PreauthFlag = "Y";
											 
					 }
					 else{
						 Status ="No data Found";
						 PreauthFlag = "N";
					 }
					 
					
					// jsonObject.put("GENERAL_DETAILS",jsonArrGeneral);
					//	 jsonObject.put("ACTIVITY_DETAILS", jsonArrActivity);
					//	 jsonObject.put("DENIAL_DETAILS", jsonArrDenail);
					 
					 alFinalList.add(alSearchList);
					 alFinalList.add(aldenailList);
					 alFinalList.add(alactivityList);
					 alFinalList.add(Status) ;
					 alFinalList.add(PreauthFlag) ;
					 
					//	JSONArray  arra1 = new JSONArray(alSearchList);
			         //   alFinalList.add(arra1);

				
				}
				
				
			
	   		}//end of try
	          catch (SQLException sqlExp)
	   	   {
	   		throw new TTKException(sqlExp, "webservice");
	   	    }//end of catch (SQLException sqlExp)
	   	 catch (Exception exp)
	   	{
	   		throw new TTKException(exp, "webservice");
	   	}//end of catch (Exception exp)

	   	finally
	   	{
	   		// Nested Try Catch to ensure resource closure 
	   		try // First try closing the Statement
	   		{
	   			try
				{
	   				if (rs2 != null) rs2.close();
   					if (rs1 != null) rs1.close();
					if (rs != null) rs.close();
					
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthList()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
	   			finally{
	   			try
	   			{
	   				if (cStmtObject != null) cStmtObject.close();
	   				
	   			
	   			}//end of try
	   			catch (SQLException sqlExp)
	   			{
	   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	   				throw new TTKException(sqlExp, "webservice");
	   			}//end of catch (SQLException sqlExp)
	   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	   			{
	   				try
	   				{
	   					
	   					if(conn != null) conn.close();
						
	   				}//end of try
	   				catch (SQLException sqlExp)
	   				{
	   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	   					throw new TTKException(sqlExp, "webservice");
	   				}//end of catch (SQLException sqlExp)
	   			}//end of finally Connection Close
	   		}//finally closed
	   		}//end of try
	   		catch (TTKException exp)
	   		{
	   			throw new TTKException(exp, "webservice");
	   		}//end of catch (TTKException exp)
	   		finally // Control will reach here in anycase set null to the objects
	   		{
	   			rs2 = null;
	   			rs1 = null;
	   			rs = null;
	   			cStmtObject = null;
	   			conn = null;
	   			
	   		}//end of finally
	   	}//end of finally
	   		return alFinalList;
	   }//end o
	    
	    
	    /*
		 * This method method returns the Arraylist 
		 * @param Arraylist object which contains the value  for which data in is required login Criteria.
		 * @param jsondata String object which contains the input JSON for which data in is required.
		 * @param respJson String object which contains the output JSON for which data in is required.
		 * @param exceptionerr String object which contains the exception message for which data in is required.
		 * @param ttkExp String object which contains the exception message for which data in is required.
		 * @return String object which contains Success message.
		 * @throws TTKException.
		     */
		@SuppressWarnings("unused")
		public ArrayList getPolicyDetails(Long ocoPolicyGrpseqid) throws TTKException {
			
	      	Connection conn = null;	
	   		CallableStatement cStmtObject=null;
	   		ResultSet rs =null;
	       	ResultSet rs1 =null;
	       	ResultSet rs2 =null;
	   		ArrayList<Object> alPolicyList = new ArrayList<Object>();
	       	ArrayList<Object> aldependentList = new ArrayList<Object>();
	       	ArrayList<Object> alabenefitList = new ArrayList<Object>();
	       	ArrayList<Object> alafinalList = new ArrayList<Object>();

	
			 JSONObject jsonObjPolicy = new JSONObject();
			 JSONObject jsonObjBenefit = new JSONObject();
			 JSONArray jsonArrPolicy = new JSONArray();
			 JSONArray jsonArrDependent = new JSONArray();
			 JSONArray jsonArrBenefit = new JSONArray();
		    
			try{
				conn = ResourceManager.getConnection();
	   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetPolicyList);
				cStmtObject.setLong(1,ocoPolicyGrpseqid);
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);   
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);   
		  
				cStmtObject.execute();
				rs = (ResultSet)cStmtObject.getObject(2);
				rs1 = (ResultSet)cStmtObject.getObject(3);
				rs2 = (ResultSet)cStmtObject.getObject(4);
				
				
				if(rs != null)
				{
					 while(rs.next())
					 {

						 
						 jsonObjPolicy.put("INSURED_NAME",rs.getString("INSURED_NAME")!=null?(String)rs.getString("INSURED_NAME"):"");
						 jsonObjPolicy.put("POLICY_NUMBER",rs.getString("POLICY_NUMBER")!=null?(String)rs.getString("POLICY_NUMBER"):"");
						 jsonObjPolicy.put("TPA_ENROLLMENT_ID",rs.getString("TPA_ENROLLMENT_ID")!=null?(String)rs.getString("TPA_ENROLLMENT_ID"):"");
						 jsonObjPolicy.put("GENDER",rs.getString("GENDER")!=null?(String)rs.getString("GENDER"):"");
						 jsonObjPolicy.put("EFFECTIVE_FROM_DATE",rs.getString("EFFECTIVE_FROM_DATE")!=null?(String)rs.getString("EFFECTIVE_FROM_DATE"):"");
						 
						 jsonObjPolicy.put("EFFECTIVE_TO_DATE",rs.getString("EFFECTIVE_TO_DATE")!=null?(String)rs.getString("EFFECTIVE_TO_DATE"):"");
						 jsonObjPolicy.put("MEM_ADDED_DATE",rs.getString("DATE_OF_INCEPTION")!=null?(String)rs.getString("DATE_OF_INCEPTION"):"");
						 jsonObjPolicy.put("MEM_EXPIRY_DATE",rs.getString("DATE_OF_EXIT")!=null?(String)rs.getString("DATE_OF_EXIT"):"");
						 jsonObjPolicy.put("DATE_OF_BIRTH",rs.getString("DATE_OF_BIRTH")!=null?(String)rs.getString("DATE_OF_BIRTH"):"");
						 jsonObjPolicy.put("NETWORK_TYPE",rs.getString("NETWORK_TYPE")!=null?(String)rs.getString("NETWORK_TYPE"):"");
						 alPolicyList.add(jsonObjPolicy);					 
					 }				 
				}
					 if(rs1 != null)
						{
							 while(rs1.next())
							 {
								 
								 JSONObject jsonObjDependent = new JSONObject();
								jsonObjDependent.put("MEMBER_SEQ_ID",rs1.getString("MEMBER_SEQ_ID")!=null?(String)rs1.getString("MEMBER_SEQ_ID"):"");
								jsonObjDependent.put("MEM_NAME",rs1.getString("MEM_NAME")!=null?(String)rs1.getString("MEM_NAME"):"");

								jsonObjDependent.put("TPA_ENROLLMENT_ID",rs1.getString("TPA_ENROLLMENT_ID")!=null?(String)rs1.getString("TPA_ENROLLMENT_ID"):"");
								jsonObjDependent.put("DATE_OF_BIRTH",rs1.getString("DATE_OF_BIRTH")!=null?(String)rs1.getString("DATE_OF_BIRTH"):"");
								jsonObjDependent.put("GENDER",rs1.getString("GENDER")!=null?(String)rs1.getString("GENDER"):"");
								jsonObjDependent.put("RELSHIP_DESCRIPTION",rs1.getString("RELSHIP_DESCRIPTION")!=null?(String)rs1.getString("RELSHIP_DESCRIPTION"):"");

								jsonObjDependent.put("MEM_ADDED_DATE",rs1.getString("DATE_OF_INCEPTION")!=null?(String)rs1.getString("DATE_OF_INCEPTION"):"");
								jsonObjDependent.put("MEM_EXPIRY_DATE",rs1.getString("DATE_OF_EXIT")!=null?(String)rs1.getString("DATE_OF_EXIT"):"");
							
								jsonObjDependent.put("EMAIL_ID",rs1.getString("Email_ID")!=null?(String)rs1.getString("Email_ID"):"");
								jsonObjDependent.put("MOBILE_NO",rs1.getString("Mobile_No")!=null?(String)rs1.getString("Mobile_No"):"");
								jsonObjDependent.put("STATE_NAME",rs1.getString("state_name")!=null?(String)rs1.getString("state_name"):"");
								jsonObjDependent.put("COUNTRY_NAME",rs1.getString("country_name")!=null?(String)rs1.getString("country_name"):"");
								
								aldependentList.add(jsonObjDependent);
								
							 }
						
							// aldependentLoopList.add(aldependentList);
						}
						
					 if(rs2 != null)
						{
							 while(rs2.next())
							 {
								//jsonObjBenefit.put("NETWORK_TYPE",rs2.getString("NETWORK_TYPE")!=null?(String)rs2.getString("NETWORK_TYPE"):"");
								
								jsonObjBenefit.put("OUTPATIENT_YN",rs2.getString("OP_YN")!=null?(String)rs2.getString("OP_YN"):"");
								jsonObjBenefit.put("INPATIENT_YN",rs2.getString("IP_YN")!=null?(String)rs2.getString("IP_YN"):"");
								jsonObjBenefit.put("MATERNITY_YN",rs2.getString("MAT_YN")!=null?(String)rs2.getString("MAT_YN"):"");
								jsonObjBenefit.put("OPTICAL_YN",rs2.getString("OPT_YN")!=null?(String)rs2.getString("OPT_YN"):"");
								jsonObjBenefit.put("DENTAL_YN",rs2.getString("DNT_YN")!=null?(String)rs2.getString("DNT_YN"):"");
							
								alabenefitList.add(jsonObjBenefit);
								
							 }
							
						}
					 String Status = "";
					 String PolicyFlag = "";
					 if(alPolicyList != null && alPolicyList.size() >0)
					 {
						 Status ="Search Successfully";
						 PolicyFlag = "Y";
											 
					 }
					 else{
						 Status ="No data Found";
						 PolicyFlag = "N";
					 }
					 alafinalList.add(alPolicyList);
					 alafinalList.add(aldependentList);
					 alafinalList.add(alabenefitList);
					 alafinalList.add(Status);
					 alafinalList.add(PolicyFlag);
					

			
			}//end else
			//end of try
	          catch (SQLException sqlExp)
	   	   {
	   		throw new TTKException(sqlExp, "webservice");
	   	    }//end of catch (SQLException sqlExp)
	   	 catch (Exception exp)
	   	{
	   		throw new TTKException(exp, "webservice");
	   	}//end of catch (Exception exp)

	   	finally
	   	{
	   		// Nested Try Catch to ensure resource closure 
	   		try // First try closing the Statement
	   		{
	   			try
				{
	   				if (rs2 != null) rs2.close();
					if (rs1 != null) rs1.close();
					if (rs != null) rs.close();
	   				
					
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthList()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
	   			finally{
	   			try
	   			{
	   				if (cStmtObject != null) cStmtObject.close();
	   				
	   			
	   			}//end of try
	   			catch (SQLException sqlExp)
	   			{
	   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	   				throw new TTKException(sqlExp, "webservice");
	   			}//end of catch (SQLException sqlExp)
	   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	   			{
	   				try
	   				{
	   					if(conn != null) conn.close();
						
	   				}//end of try
	   				catch (SQLException sqlExp)
	   				{
	   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	   					throw new TTKException(sqlExp, "webservice");
	   				}//end of catch (SQLException sqlExp)
	   			}//end of finally Connection Close
	   		}//finally closed
	   		}//end of try
	   		catch (TTKException exp)
	   		{
	   			throw new TTKException(exp, "webservice");
	   		}//end of catch (TTKException exp)
	   		finally // Control will reach here in anycase set null to the objects
	   		{
	   			rs2 = null;
	   			rs1 = null;
	   			rs = null;
	   			cStmtObject = null;
	   			conn = null;
	   				   			
	   		}//end of finally
	   	}//end of finally
	   		return alafinalList;
	   }//end o
	 
		
		
		  @SuppressWarnings({ "unused", "rawtypes", "null" })
		    public ArrayList getClaimList(OnlineIntimationVO onlineIntimationVO)  throws TTKException {
		      
		       	ArrayList<Object> alSearchList = new ArrayList<Object>();
		       	ArrayList<Object> alClaimList = new ArrayList<Object>();

		   		Connection conn = null;	
		   		CallableStatement cStmtObject=null;
		   	 	ResultSet rs =null;
		   		VidalClaimWireRestVO vidalClaimWireRestVO=null;

		   		try {
		   			conn = ResourceManager.getConnection();
		   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMobileAppClaimList);
		   			cStmtObject.setLong(1,onlineIntimationVO.getMemberSeqID());
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
					

					cStmtObject.execute();					
					rs = (ResultSet)cStmtObject.getObject(2);
					
					if(rs != null)
					{
						 while(rs.next())
						 {
							 JSONObject jsonObject = new JSONObject();

							 
							 vidalClaimWireRestVO = new VidalClaimWireRestVO();
							 jsonObject.put("CLAIM_SEQ_ID",rs.getString("CLAIM_SEQ_ID")!=null?(String)rs.getString("CLAIM_SEQ_ID"):"");
							 jsonObject.put("CLAIM_NUMBER",rs.getString("CLAIM_NUMBER")!=null?(String)rs.getString("CLAIM_NUMBER"):"");
							 jsonObject.put("DECISION_DATE",rs.getString("DECISION_DATE")!=null?(String)rs.getString("DECISION_DATE"):"");
							 jsonObject.put("PAT_STATUS",rs.getString("PAT_STATUS")!=null?(String)rs.getString("PAT_STATUS"):"");
							 jsonObject.put("HOSPITAL_NAME",rs.getString("HOSP_NAME")!=null?(String)rs.getString("HOSP_NAME"):"");
							
							 alClaimList.add(jsonObject);
						 }
						 alSearchList.add(alClaimList);
						 
					}
		   		}//end of try
		          catch (SQLException sqlExp)
		   	   {
		   		throw new TTKException(sqlExp, "webservice");
		   	    }//end of catch (SQLException sqlExp)
		   	 catch (Exception exp)
		   	{
		   		throw new TTKException(exp, "webservice");
		   	}//end of catch (Exception exp)

		   	finally
		   	{
		   		// Nested Try Catch to ensure resource closure 
		   		try // First try closing the Statement
		   		{
		   			try
		   			{
		   				if(rs != null) rs.close();
		   				if (cStmtObject != null) cStmtObject.close();
		   			}//end of try
		   			catch (SQLException sqlExp)
		   			{
		   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
		   				throw new TTKException(sqlExp, "webservice");
		   			}//end of catch (SQLException sqlExp)
		   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		   			{
		   				try
		   				{
		   					if(conn != null) conn.close();
		   					
		   				}//end of try
		   				catch (SQLException sqlExp)
		   				{
		   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
		   					throw new TTKException(sqlExp, "webservice");
		   				}//end of catch (SQLException sqlExp)
		   			}//end of finally Connection Close
		   		}//end of try
		   		catch (TTKException exp)
		   		{
		   			throw new TTKException(exp, "webservice");
		   		}//end of catch (TTKException exp)
		   		finally // Control will reach here in anycase set null to the objects
		   		{
		   			rs = null;
		   			cStmtObject = null;
		   			conn = null;
		   			
		   		}//end of finally
		   	}//end of finally
		   
		   		return alSearchList;
		   }//end o
		
		
		    
		    @SuppressWarnings({ "unused", "rawtypes", "null" })
		    public ArrayList getClaimDetails(VidalClaimWireRestVO vidalClaimWireRestVO)  throws TTKException {
		    	
		    	
		       	

		       	ArrayList<Object> alSearchList = new ArrayList<Object>();
		       	ArrayList<Object> alFinalList = new ArrayList<Object>();
		       	ArrayList<Object> alactivityList = new ArrayList<Object>();
		       	ArrayList<Object> aldenailList = new ArrayList<Object>();
		       	ArrayList<Object> alresult = new ArrayList<Object>();
		    	ArrayList<Object> alshortfallQues = new ArrayList<Object>();

		       	

		   		Connection conn = null;	
		   		CallableStatement cStmtObject=null;
		   		ResultSet rs =null;
		       	ResultSet rs1 =null;
		       	ResultSet rs2 =null;
		      	ResultSet rs3 =null;
		 
				 JSONObject jsonObject = new JSONObject();
				 JSONObject jsonObjectact = new JSONObject();
				 JSONObject jsonObjgeneral = new JSONObject();
				 JSONObject objresult = new JSONObject();

				 JSONArray jsonArrGeneral = new JSONArray();
				 JSONArray jsonArrActivity = new JSONArray();
				 JSONArray jsonArrDenail = new JSONArray();






		   		try {
		   			conn = ResourceManager.getConnection();
		   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMobileAppClaimDetails);
		   			cStmtObject.setLong(1,vidalClaimWireRestVO.getPreAuthSeqID());
					
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);   
					cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);   
					cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);   
					cStmtObject.execute();					
					rs = (ResultSet)cStmtObject.getObject(2);
					rs1 = (ResultSet)cStmtObject.getObject(3);
					rs2 = (ResultSet)cStmtObject.getObject(4);
					rs3 = (ResultSet)cStmtObject.getObject(5);
					
					if(rs != null)
					{
						 while(rs.next())
						 {						 				
							 jsonObjgeneral.put("CLAIM_SEQ_ID",rs.getString("CLAIM_SEQ_ID")!=null?(String)rs.getString("CLAIM_SEQ_ID"):"");
							 jsonObjgeneral.put("CLAIM_NUMBER",rs.getString("CLAIM_NUMBER")!=null?(String)rs.getString("CLAIM_NUMBER"):"");
							 jsonObjgeneral.put("DECISION_DATE",rs.getString("DECISION_DATE")!=null?(String)rs.getString("DECISION_DATE"):"");
							 jsonObjgeneral.put("PAT_STATUS",rs.getString("PAT_STATUS")!=null?(String)rs.getString("PAT_STATUS"):"");
							 jsonObjgeneral.put("SHORTFALL_NO",rs.getString("SHORTFALL_NO")!=null?(String)rs.getString("SHORTFALL_NO"):"");
							 
							 jsonObjgeneral.put("PROVIDER_NAME",rs.getString("PROVIDER_NAME")!=null?(String)rs.getString("PROVIDER_NAME"):"");
							 jsonObjgeneral.put("PROV_CONTACT_NO",rs.getString("PROV_CONTACT_NO")!=null?(String)rs.getString("PROV_CONTACT_NO"):"");
							 jsonObjgeneral.put("PROVIDER_LICENSE_NO",rs.getString("PROVIDER_LICENSE_NO")!=null?(String)rs.getString("PROVIDER_LICENSE_NO"):"");
							 jsonObjgeneral.put("HOSP_MAIL_ID",rs.getString("HOSP_MAIL_ID")!=null?(String)rs.getString("HOSP_MAIL_ID"):"");
							 jsonObjgeneral.put("PATIENT_NAME",rs.getString("PATIENT_NAME")!=null?(String)rs.getString("PATIENT_NAME"):"");

							 jsonObjgeneral.put("AL_KOOT_ID_NO",rs.getString("AL_KOOT_ID_NO")!=null?(String)rs.getString("AL_KOOT_ID_NO"):"");
							 jsonObjgeneral.put("DATE_OF_BIRTH",rs.getString("DATE_OF_BIRTH")!=null?(String)rs.getString("DATE_OF_BIRTH"):"");
							 jsonObjgeneral.put("POLICY_NUMBER",rs.getString("POLICY_NUMBER")!=null?(String)rs.getString("POLICY_NUMBER"):"");
							 jsonObjgeneral.put("GENDER",rs.getString("GENDER")!=null?(String)rs.getString("GENDER"):"");
							 jsonObjgeneral.put("CLINICIAN_NAME",rs.getString("CLINICIAN_NAME")!=null?(String)rs.getString("CLINICIAN_NAME"):"");
							 
							 jsonObjgeneral.put("CLINICIAN_LICENSE_NO",rs.getString("CLINICIAN_LICENSE_NO")!=null?(String)rs.getString("CLINICIAN_LICENSE_NO"):"");
							 jsonObjgeneral.put("BENEFIT_TYPE",rs.getString("BENEFIT_TYPE")!=null?(String)rs.getString("BENEFIT_TYPE"):"");
							 jsonObjgeneral.put("DATE_OF_TREATMENT",rs.getString("DATE_OF_TREATMENT")!=null?(String)rs.getString("DATE_OF_TREATMENT"):"");
							 jsonObjgeneral.put("DIAGNOSIS_TYPE",rs.getString("DIAGNOSIS_TYPE")!=null?(String)rs.getString("DIAGNOSIS_TYPE"):"");
							 jsonObjgeneral.put("DIAGNOSYS_CODE",rs.getString("DIAGNOSYS_CODE")!=null?(String)rs.getString("DIAGNOSYS_CODE"):"");
							 
							 jsonObjgeneral.put("DESCRIPTION",rs.getString("DESCRIPTION")!=null?(String)rs.getString("DESCRIPTION"):"");
							 jsonObjgeneral.put("TOT_DISC_GROSS_AMOUNT",rs.getString("TOT_DISC_GROSS_AMOUNT")!=null?(String)rs.getString("TOT_DISC_GROSS_AMOUNT"):"");
							 jsonObjgeneral.put("COPAY_AMOUNT",rs.getString("COPAY_AMOUNT")!=null?(String)rs.getString("COPAY_AMOUNT"):"");
							 jsonObjgeneral.put("DEDUCT_AMOUNT",rs.getString("DEDUCT_AMOUNT")!=null?(String)rs.getString("DEDUCT_AMOUNT"):"");
							 jsonObjgeneral.put("FINAL_APP_AMOUNT",rs.getString("FINAL_APP_AMOUNT")!=null?(String)rs.getString("FINAL_APP_AMOUNT"):"");
							 jsonObjgeneral.put("MEDICAL_OPINION_REMARKS",rs.getString("MEDICAL_OPINION_REMARKS")!=null?(String)rs.getString("MEDICAL_OPINION_REMARKS"):"");
							 jsonObjgeneral.put("TOTAL_CLAIMED_AMOUNT",rs.getString("REQ_AMT")!=null?(String)rs.getString("REQ_AMT"):"");
							 /*jsonObjgeneral.put("SHORTFALL_QUESTIONS",rs.getString("SHORTFALL_QUESTIONS")!=null?(String)rs.getString("SHORTFALL_QUESTIONS"):"");
							 System.out.println("SHORTFALL_QUESTIONS END:::::::"+rs.getString("SHORTFALL_QUESTIONS"));*/
							 jsonObjgeneral.put("SHORTFALL_FLAG",rs.getString("shortfall_flag")!=null?(String)rs.getString("shortfall_flag"):"");
							
							 jsonObjgeneral.put("SHORTFALL_SEQ_ID",rs.getString("SHORTFALL_SEQ_ID")!=null?(String)rs.getString("SHORTFALL_SEQ_ID"):"");
						
							// jsonArrGeneral.put(jsonObjgeneral);
							 alSearchList.add(jsonObjgeneral);

							 
							 
						 }
						 
						 if(rs1 != null)
							{
								 while(rs1.next())
								 {
									 JSONObject jsonDenail = new JSONObject();
								
									jsonDenail.put("DENIAL_CODE",rs1.getString("DENIAL_CODE")!=null?(String)rs1.getString("DENIAL_CODE"):"");
									jsonDenail.put("DENIAL_DESCRIPTION",rs1.getString("DENIAL_DESCRIPTION")!=null?(String)rs1.getString("DENIAL_DESCRIPTION"):"");
									aldenailList.add(jsonDenail);
									
								 }
								/*for(int k=0;k<alactivityList.size();k++)
									System.out.println("akl data::"+alactivityList.get(k));*/
								// jsonArrDenail.put(aldenailList);
							}
							
						 if(rs2 != null)
							{
								 while(rs2.next())
								 {
									 JSONObject obj = new JSONObject();
									obj.put("START_DATE",rs2.getString("START_DATE")!=null?(String)rs2.getString("START_DATE"):"");
									obj.put("ACTIVITY_CODE",rs2.getString("ACTIVITY_CODE")!=null?(String)rs2.getString("ACTIVITY_CODE"):"");
									obj.put("ACTIVITY_DESCRIPTION",rs2.getString("ACTIVITY_DESCRIPTION")!=null?(String)rs2.getString("ACTIVITY_DESCRIPTION"):"");
									obj.put("AMOUNT_CLAIMED",rs2.getString("AMOUNT_CLAIMED")!=null?(String)rs2.getString("AMOUNT_CLAIMED"):"");
									obj.put("DISALLOWED_AMOUNT",rs2.getString("DISALLOWED_AMOUNT")!=null?(String)rs2.getString("DISALLOWED_AMOUNT"):"");
									obj.put("AMOUNT_APPROVED",rs2.getString("AMOUNT_APPROVED")!=null?(String)rs2.getString("AMOUNT_APPROVED"):"");
									obj.put("REMARKS",rs2.getString("REMARKS")!=null?(String)rs2.getString("REMARKS"):"");

									alactivityList.add(obj);
									
								 }
								
							}
						 
						 if(rs3 != null)
							{
							 while(rs3.next())
								 {
									 JSONObject jsonShortfall = new JSONObject();
									 
									 jsonShortfall.put("SHORTFALL_MEDICAL_QUERIES",rs3.getString("SHORTFALL_MEDICAL_QUERIES")!=null?(String)rs3.getString("SHORTFALL_MEDICAL_QUERIES"):"");
									 jsonShortfall.put("SHORTFALL_OTHER_QUERIES",rs3.getString("SHORTFALL_OTHER_QUERIES")!=null?(String)rs3.getString("SHORTFALL_OTHER_QUERIES"):"");
									 
									 alshortfallQues.add(jsonShortfall);
								
								 }
							 
								
							}
						 
						 
						 
						 String Status = "";
						 String PreauthFlag = "";
						 if(alSearchList != null && alSearchList.size() >0)
						 {
							 Status ="Search Successfully";
							 PreauthFlag = "Y";
												 
						 }
						 else{
							 Status ="No data Found";
							 PreauthFlag = "N";
						 }
						 
						
						// jsonObject.put("GENERAL_DETAILS",jsonArrGeneral);
						//	 jsonObject.put("ACTIVITY_DETAILS", jsonArrActivity);
						//	 jsonObject.put("DENIAL_DETAILS", jsonArrDenail);
						 
						 alFinalList.add(alSearchList);
						 alFinalList.add(aldenailList);
						 alFinalList.add(alactivityList);
						 alFinalList.add(alshortfallQues);
						 alFinalList.add(Status) ;
						 alFinalList.add(PreauthFlag) ;
						 
						//	JSONArray  arra1 = new JSONArray(alSearchList);
				         //   alFinalList.add(arra1);

					
					}
					
					
				
		   		}//end of try
		          catch (SQLException sqlExp)
		   	   {
		   		throw new TTKException(sqlExp, "webservice");
		   	    }//end of catch (SQLException sqlExp)
		   	 catch (Exception exp)
		   	{
		   		throw new TTKException(exp, "webservice");
		   	}//end of catch (Exception exp)

		   	finally
		   	{
		   		// Nested Try Catch to ensure resource closure 
		   		try // First try closing the Statement
		   		{
		   			try
					{
		   				if (rs3 != null) rs3.close();
	   					if (rs2 != null) rs2.close();
	   					if (rs1 != null) rs1.close();
	   					if (rs != null) rs.close();
						
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
		   			finally{
		   			try
		   			{
		   				if (cStmtObject != null) cStmtObject.close();
		   				
		   				
		   			//	if (cStmtObject1 != null) cStmtObject1.close();
		   			//	if (cStmtObject2 != null) cStmtObject2.close();
		   			}//end of try
		   			catch (SQLException sqlExp)
		   			{
		   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
		   				throw new TTKException(sqlExp, "webservice");
		   			}//end of catch (SQLException sqlExp)
		   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		   			{
		   				try
		   				{
		   					if(conn != null) conn.close();
		   					
							
		   				}//end of try
		   				catch (SQLException sqlExp)
		   				{
		   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
		   					throw new TTKException(sqlExp, "webservice");
		   				}//end of catch (SQLException sqlExp)
		   			}//end of finally Connection Close
		   		}//finally closed
		   		}//end of try
		   		catch (TTKException exp)
		   		{
		   			throw new TTKException(exp, "webservice");
		   		}//end of catch (TTKException exp)
		   		finally // Control will reach here in anycase set null to the objects
		   		{
		   			rs3 = null;
		   			rs2 = null;
		   			rs1 = null;
		   			rs = null;
		   			cStmtObject = null;
		   			conn = null;
		   			
		   				   			
		   		}//end of finally
		   	}//end of finally
		   		return alFinalList;
		   }//end o
		    
	
		    
		    @SuppressWarnings({ "unused", "rawtypes", "null" })
		    public ArrayList getMyProfile(OnlineIntimationVO onlineIntimationVO)  throws TTKException {
		      
		       	ArrayList<Object> alSearchList = new ArrayList<Object>();
		   		Connection conn = null;	
		   		CallableStatement cStmtObject=null;
		   	 	ResultSet rs =null;
		        JSONArray jsonArray = new JSONArray();

		   		//Calendar calendar = Calendar.getInstance();
		   		//StringBuffer sb=null;

		   		try {
		   			conn = ResourceManager.getConnection();
		   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetMyProfile);
		   			cStmtObject.setLong(1,onlineIntimationVO.getPolicyGrpSeqID());
					
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
					cStmtObject.execute();					
					rs = (ResultSet)cStmtObject.getObject(2);
					
					if(rs != null)
					{
						 while(rs.next())
						 {
							 
							 JSONObject jsonObject = new JSONObject();
							// profileDetailMobileVO = new ProfileDetailMobileVO();
							jsonObject.put("USER_ID",rs.getString("USER_ID")!=null?(String)rs.getString("USER_ID"):"");
							jsonObject.put("PASSWORD",rs.getString("PASSWORD")!=null?(String)rs.getString("PASSWORD"):"");
							jsonObject.put("INSURED_NAME",rs.getString("INSURED_NAME")!=null?(String)rs.getString("INSURED_NAME"):"");
							jsonObject.put("POLICY_NUMBER",rs.getString("POLICY_NUMBER")!=null?(String)rs.getString("POLICY_NUMBER"):"");
							jsonObject.put("TPA_ENROLLMENT_ID",rs.getString("TPA_ENROLLMENT_ID")!=null?(String)rs.getString("TPA_ENROLLMENT_ID"):"");
							jsonObject.put("GENDER",rs.getString("GENDER")!=null?(String)rs.getString("GENDER"):"");
							jsonObject.put("EFFECTIVE_FROM_DATE",rs.getString("EFFECTIVE_FROM_DATE")!=null?(String)rs.getString("EFFECTIVE_FROM_DATE"):"");
							jsonObject.put("EFFECTIVE_TO_DATE",rs.getString("EFFECTIVE_TO_DATE")!=null?(String)rs.getString("EFFECTIVE_TO_DATE"):"");
							jsonObject.put("DATE_OF_INCEPTION",rs.getString("DATE_OF_INCEPTION")!=null?(String)rs.getString("DATE_OF_INCEPTION"):"");
							jsonObject.put("DATE_OF_EXIT",rs.getString("DATE_OF_EXIT")!=null?(String)rs.getString("DATE_OF_EXIT"):"");
							jsonObject.put("DATE_OF_BIRTH",rs.getString("DATE_OF_BIRTH")!=null?(String)rs.getString("DATE_OF_BIRTH"):"");
							jsonObject.put("MOBILE_NO",rs.getString("MOBILE_NO")!=null?(String)rs.getString("MOBILE_NO"):"");
							jsonObject.put("EMAIL_ID",rs.getString("EMAIL_ID")!=null?(String)rs.getString("EMAIL_ID"):"");
							
							jsonObject.put("ADDRESS_1",rs.getString("ADDRESS_1")!=null?(String)rs.getString("ADDRESS_1"):"");
							jsonObject.put("ADDRESS_2",rs.getString("ADDRESS_2")!=null?(String)rs.getString("ADDRESS_2"):"");
							jsonObject.put("ADDRESS_3",rs.getString("ADDRESS_3")!=null?(String)rs.getString("ADDRESS_3"):"");
							jsonObject.put("CITY_TYPE_ID",rs.getString("CITY_TYPE_ID")!=null?(String)rs.getString("CITY_TYPE_ID"):"");
							jsonObject.put("STATE_TYPE_ID",rs.getString("STATE_TYPE_ID")!=null?(String)rs.getString("STATE_TYPE_ID"):"");
							jsonObject.put("COUNTRY_ID",rs.getString("COUNTRY_ID")!=null?(String)rs.getString("COUNTRY_ID"):"");
							
							jsonObject.put("CITY_DESCRIPTION",rs.getString("CITY_DESCRIPTION")!=null?(String)rs.getString("CITY_DESCRIPTION"):"");
							jsonObject.put("STATE_NAME",rs.getString("STATE_NAME")!=null?(String)rs.getString("STATE_NAME"):"");
							jsonObject.put("COUNTRY_NAME",rs.getString("COUNTRY_NAME")!=null?(String)rs.getString("COUNTRY_NAME"):"");
							jsonObject.put("ADDRESS_SEQID",rs.getString("ENR_ADDRESS_SEQ_ID")!=null?(String)rs.getString("ENR_ADDRESS_SEQ_ID"):"");
							jsonObject.put("MEMBER_SEQ_ID",rs.getString("MEMBER_SEQ_ID")!=null?(String)rs.getString("MEMBER_SEQ_ID"):"");
							
							jsonObject.put("GENDER_GENERAL_TYPE_ID",rs.getString("GENDER_GENERAL_TYPE_ID")!=null?(String)rs.getString("GENDER_GENERAL_TYPE_ID"):"");
							jsonObject.put("POLICY_GROUP_SEQ_ID",rs.getString("POLICY_GROUP_SEQ_ID")!=null?(String)rs.getString("POLICY_GROUP_SEQ_ID"):"");
							
							
							jsonObject.put("BANKACC_NUMBER",rs.getString("BANKACC_NUMBER")!=null?(String)rs.getString("BANKACC_NUMBER"):"");
							jsonObject.put("BANKACC_TYPE",rs.getString("BANKACC_TYPE")!=null?(String)rs.getString("BANKACC_TYPE"):"");
							jsonObject.put("BANK_DESTINATION",rs.getString("BANK_DESTINATION")!=null?(String)rs.getString("BANK_DESTINATION"):"");
							jsonObject.put("BANK_CITY",rs.getString("BANK_CITY")!=null?(String)rs.getString("BANK_CITY"):"");
							jsonObject.put("BANK_AREA",rs.getString("BANK_AREA")!=null?(String)rs.getString("BANK_AREA"):"");
							jsonObject.put("BANK_BRANCH",rs.getString("BANK_BRANCH")!=null?(String)rs.getString("BANK_BRANCH"):"");
							jsonObject.put("IBAN_NUMBER",rs.getString("IBAN_NUMBER")!=null?(String)rs.getString("IBAN_NUMBER"):"");
							jsonObject.put("BANK_DESTINATION_DESC",rs.getString("BANK_DESTINATION_DESC")!=null?(String)rs.getString("BANK_DESTINATION_DESC"):"");
							jsonObject.put("BANK_CITY_DESC",rs.getString("BANK_CITY_DESC")!=null?(String)rs.getString("BANK_CITY_DESC"):"");
							jsonObject.put("BANK_AREA_DESC",rs.getString("BANK_AREA_DESC")!=null?(String)rs.getString("BANK_AREA_DESC"):"");
							jsonObject.put("BANK_BRANCH_DESC",rs.getString("BANK_BRANCH_DESC")!=null?(String)rs.getString("BANK_BRANCH_DESC"):"");
							
							jsonObject.put("BANK_SWIFT_CODE",rs.getString("BANK_SWIFT_CODE")!=null?(String)rs.getString("BANK_SWIFT_CODE"):"");
							jsonObject.put("ACCOUNT_IN_QATAR_YN",rs.getString("ACCOUNT_IN_QATAR_YN")!=null?(String)rs.getString("ACCOUNT_IN_QATAR_YN"):"");
							
							
							jsonObject.put("BANK_COUNTRY_ID",rs.getString("BANK_COUNTRY_ID")!=null?(String)rs.getString("BANK_COUNTRY_ID"):"");
							jsonObject.put("BANK_COUNTRY_DECS",rs.getString("BANK_COUNTRY_DECS")!=null?(String)rs.getString("BANK_COUNTRY_DECS"):"");
							
							
							
							alSearchList.add(jsonObject);
						 }
					}
		   		}//end of try
		          catch (SQLException sqlExp)
		   	   {
		   		throw new TTKException(sqlExp, "webservice");
		   	    }//end of catch (SQLException sqlExp)
		   	 catch (Exception exp)
		   	{
		   		throw new TTKException(exp, "webservice");
		   	}//end of catch (Exception exp)

		   	finally
		   	{
		   		// Nested Try Catch to ensure resource closure 
		   		try // First try closing the Statement
		   		{
		   			try
		   			{
		   				if(rs != null) rs.close();
		   				if (cStmtObject != null) cStmtObject.close();
		   			}//end of try
		   			catch (SQLException sqlExp)
		   			{
		   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
		   				throw new TTKException(sqlExp, "webservice");
		   			}//end of catch (SQLException sqlExp)
		   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		   			{
		   				try
		   				{
		   					if(conn != null) conn.close();
		   					
		   				}//end of try
		   				catch (SQLException sqlExp)
		   				{
		   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
		   					throw new TTKException(sqlExp, "webservice");
		   				}//end of catch (SQLException sqlExp)
		   			}//end of finally Connection Close
		   		}//end of try
		   		catch (TTKException exp)
		   		{
		   			throw new TTKException(exp, "webservice");
		   		}//end of catch (TTKException exp)
		   		finally // Control will reach here in anycase set null to the objects
		   		{
		   			rs = null;
		   			cStmtObject = null;
		   			conn = null;
		   			
		   		}//end of finally
		   	}//end of finally
		   		return alSearchList;
		   }//end o
		    
		    
		    
		    public ArrayList updateMyprofile(OnlineIntimationVO onlineIntimationVO) throws TTKException {
		      	int intResult = 0;
		  		ArrayList<Object> alResult = new ArrayList<Object>();
		  		Connection conn = null;	
		  		CallableStatement cStmtObject=null;
		  		//Calendar calendar = Calendar.getInstance();
		  		//StringBuffer sb=null;

		  		try {
		  			conn = ResourceManager.getConnection();
		  			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(save_my_profile);
		  			cStmtObject.setLong(1,onlineIntimationVO.getAddressSeqID()); 
		  			
		  			cStmtObject.setLong(2,onlineIntimationVO.getPolicyGrpSeqID());		  			
		  			cStmtObject.setLong(3,onlineIntimationVO.getMemberSeqID());  			
		  			cStmtObject.setString(4,onlineIntimationVO.getPassword()); 
		  			
		  	      SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");  
		  	    	Date date1=formatter1.parse(onlineIntimationVO.getDateOfBirth());  

		  	    	if(date1 != null){
		             	cStmtObject.setTimestamp(5,new Timestamp(date1.getTime()));
		             }//end of if(bufferDetailVO.getBufferDate() != null)
		  	    	
		  	    	
		  			cStmtObject.setString(6,onlineIntimationVO.getGenderGeneralId());
		  			
		  			cStmtObject.setString(7,onlineIntimationVO.getMobileNbr()); 
		  			
		  			cStmtObject.setString(8,onlineIntimationVO.getEmailID()); 
		  			
		  			cStmtObject.setString(9,onlineIntimationVO.getAddress1()); 

		  			//cStmtObject.setString(10,onlineIntimationVO.getAddress2()); 
		  		//	cStmtObject.setString(11,onlineIntimationVO.getAddress3()); ;
		  			cStmtObject.setString(10,onlineIntimationVO.getCityID()); 
		  			
		  			cStmtObject.setString(11,onlineIntimationVO.getStateId()); 

		  			cStmtObject.setString(12,onlineIntimationVO.getCountryId());

		  			cStmtObject.setString(13,onlineIntimationVO.getPincode());

		  		    cStmtObject.registerOutParameter(14,Types.BIGINT);
		  			
		  			cStmtObject.setString(15,onlineIntimationVO.getBankaccno());

		  			cStmtObject.setString(16,onlineIntimationVO.getBankaccdestination());

		  			cStmtObject.setString(17,onlineIntimationVO.getBankacctype());

		  			cStmtObject.setString(18,onlineIntimationVO.getBankstate());

		  			cStmtObject.setString(19,onlineIntimationVO.getBankacity());

		  			cStmtObject.setString(20,onlineIntimationVO.getBankbranch());
		  			//cStmtObject.setString(21,onlineIntimationVO.getMicr());	
		  			cStmtObject.setString(21,onlineIntimationVO.getBankaccno());
		  			cStmtObject.setString(22,onlineIntimationVO.getSwiftCode());	
		  			cStmtObject.setString(23,onlineIntimationVO.getBankAccountQatarYN());	
		  			cStmtObject.setString(24,onlineIntimationVO.getBankBranchText());	
		  			cStmtObject.setString(25,onlineIntimationVO.getBankCountry());	
		  		   cStmtObject.registerOutParameter(26,Types.VARCHAR);
		  		  
		  			cStmtObject.execute();
		  			//intResult = cStmtObject.getInt(15);
		  			alResult.add(cStmtObject.getInt(14));
		  			alResult.add(cStmtObject.getString(26));//if any exception from backend
		  		}//end of try
		         catch (SQLException sqlExp)
		  	   {
		  		throw new TTKException(sqlExp, "webservice");
		  	    }//end of catch (SQLException sqlExp)
		  	 catch (Exception exp)
		  	{
		  		throw new TTKException(exp, "webservice");
		  	}//end of catch (Exception exp)

		  	finally
		  	{
		  		// Nested Try Catch to ensure resource closure 
		  		try // First try closing the Statement
		  		{
		  			try
		  			{
		  				if (cStmtObject != null) cStmtObject.close();
		  			}//end of try
		  			catch (SQLException sqlExp)
		  			{
		  				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
		  				throw new TTKException(sqlExp, "webservice");
		  			}//end of catch (SQLException sqlExp)
		  			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		  			{
		  				try
		  				{
		  					if(conn != null) conn.close();
		  				}//end of try
		  				catch (SQLException sqlExp)
		  				{
		  					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
		  					throw new TTKException(sqlExp, "webservice");
		  				}//end of catch (SQLException sqlExp)
		  			}//end of finally Connection Close
		  		}//end of try
		  		catch (TTKException exp)
		  		{
		  			throw new TTKException(exp, "webservice");
		  		}//end of catch (TTKException exp)
		  		finally // Control will reach here in anycase set null to the objects
		  		{
		  			cStmtObject = null;
		  			conn = null;
		  		}//end of finally
		  	}//end of finally
		  	return alResult;
		  }//end o
		      
		    
		    
		    /**
		     * This method method returns the byte[] 
		     * @param String object which contains the searchValue for which data in is required search Criteria.
		     * @param String object which contains the strIMEINumber for which data in is required search Criteria.
			 * @return String object which contains Rule Errors.
		     * @exception throws TTKException.
		     */
		
				public byte[] getHealthTips(OnlineIntimationVO onlineIntimationVO) throws TTKException  {
					BLOB photo = null;
					byte[] data=null;
					Connection conn = null;
					StringBuffer strTemplateData=new StringBuffer();
					CallableStatement cStmtObject=null;
					
					try {
						log.info("INSIDE DAILY TIPS IMAGE DAO START");
						conn = ResourceManager.getConnection();

						cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDailyTipImageMob);
						cStmtObject.setString(1,onlineIntimationVO.getDtAdmissionDate());
						
						cStmtObject.registerOutParameter(2,Types.BLOB);
						cStmtObject.execute();
						
						 photo = (BLOB) cStmtObject.getBlob(2);
					//    
		           
					if(photo!=null)			{
						
						InputStream in = photo.getBinaryStream();
						int length = (int) photo.length();
						data=photo.getBytes(1,length);
					}  
					else{	data=null;}	
					log.info("INSIDE DAILY TIPS IMAGE DAO END");
					}//end of try
					catch (SQLException sqlExp)
					{
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
						throw new TTKException(exp, "webservice");
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
								log.error("Error while closing the Statement in WebServiceDAOImpl dailyTipService()",sqlExp);
								throw new TTKException(sqlExp, "webservice");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in WebServiceDAOImpl dailyTipService()",sqlExp);
									throw new TTKException(sqlExp, "webservice");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of try
						catch (TTKException exp)
						{
							throw new TTKException(exp, "webservice");
						}//end of catch (TTKException exp)
						finally // Control will reach here in anycase set null to the objects
						{
							cStmtObject = null;
							conn = null;
						}//end of finally
					}//end of finally
					return data;
				}//end of dailyTipImageService(String strDate,String strIMEINumber) 
		    
				
				
				
				 @SuppressWarnings({ "unused", "rawtypes", "null" })
				    public ArrayList getFAQ()  throws TTKException {
				       
				       	ArrayList<Object> alSearchList = new ArrayList<Object>();
				      	ArrayList<Object> alFinalList = new ArrayList<Object>();
				   		Connection conn = null;	
				   		CallableStatement cStmtObject=null;
				   		ResultSet rs =null;
				        JSONArray jsonArray = new JSONArray();

				   		try {
				   			conn = ResourceManager.getConnection();
				   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strquery_ans_details);
							
							cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);   
							cStmtObject.execute();					
							rs = (ResultSet)cStmtObject.getObject(1);
							
							if(rs != null)
							{
								 while(rs.next())
								 {
									 
									 JSONObject jsonObject = new JSONObject();
									jsonObject.put("QUERY_DETAILS",rs.getString("QUERY_DETAILS")!=null?(String)rs.getString("QUERY_DETAILS"):"");
									jsonObject.put("ANS_DETAILS",rs.getString("ANS_DETAILS")!=null?(String)rs.getString("ANS_DETAILS"):"");
									jsonObject.put("QUERY_SEQ_ID",rs.getString("QUERY_SEQ_ID")!=null?(String)rs.getString("QUERY_SEQ_ID"):"");
									

									 alSearchList.add(jsonObject);
								 }
								 alFinalList.add(alSearchList);
							}
				   		}//end of try
				          catch (SQLException sqlExp)
				   	   {
				   		throw new TTKException(sqlExp, "webservice");
				   	    }//end of catch (SQLException sqlExp)
				   	 catch (Exception exp)
				   	{
				   		throw new TTKException(exp, "webservice");
				   	}//end of catch (Exception exp)

				   	finally
				   	{
				   		// Nested Try Catch to ensure resource closure 
				   		try // First try closing the Statement
				   		{
				   			try
				   			{
				   				if (rs != null) rs.close();
				   				if (cStmtObject != null) cStmtObject.close();
				   			}//end of try
				   			catch (SQLException sqlExp)
				   			{
				   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
				   				throw new TTKException(sqlExp, "webservice");
				   			}//end of catch (SQLException sqlExp)
				   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				   			{
				   				try
				   				{
				   					if(conn != null) conn.close();
				   				}//end of try
				   				catch (SQLException sqlExp)
				   				{
				   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
				   					throw new TTKException(sqlExp, "webservice");
				   				}//end of catch (SQLException sqlExp)
				   			}//end of finally Connection Close
				   		}//end of try
				   		catch (TTKException exp)
				   		{
				   			throw new TTKException(exp, "webservice");
				   		}//end of catch (TTKException exp)
				   		finally // Control will reach here in anycase set null to the objects
				   		{
				   			rs = null;
				   			cStmtObject = null;
				   			conn = null;
				   		}//end of finally
				   	}//end of finally
				   		return alFinalList;
				   }//end o
				    

				 public ArrayList getIntervalTypeList() throws TTKException {
			
				 		 Connection conn = null;
				 		 PreparedStatement pStmtObject = null;
				 		 ResultSet rs = null;
				 	  	ArrayList<Object> alSearchList = new ArrayList<Object>();
				      	ArrayList<Object> alFinalList = new ArrayList<Object>();
				 		 try
				 		 {
				 			conn = ResourceManager.getConnection();
				 			pStmtObject = (PreparedStatement)conn.prepareStatement(intervaldetails);
				 				rs = pStmtObject.executeQuery();
				 		
				 			if(rs != null)
				 			{
				 				 while(rs.next())
				 				 {
				 					 JSONObject jsonObject = new JSONObject();
				 					jsonObject.put("INTERVAL_TYPE_ID",rs.getString(1));
									jsonObject.put("INTERVAL_TYPE_DESC",rs.getString(2));
				 					 
									 alSearchList.add(jsonObject);
								 }
								 alFinalList.add(alSearchList);

				 			 }	
				 		 }
				 			catch (SQLException | JSONException e) 
				 			{
				 				e.printStackTrace();
				 			}
				 			finally
				 			{
				 				try
				 				{
				 					if(rs != null)rs.close();
				 				} 
				 				catch (SQLException e1) 
				 				{
				 					e1.printStackTrace();
				 				}
				 				try
				 				{
				 					if(pStmtObject != null) pStmtObject.close();
				 				}catch(SQLException e)
				 				{
				 					e.printStackTrace();
				 				}try
				 				{
				 					if(conn != null) conn.close();
				 				}catch(SQLException e)
				 				{
				 					e.printStackTrace();
				 				}finally{
				 					rs = null;
				 					pStmtObject = null;
				 					conn = null;
				 				}	
				 			}
				 		 return alFinalList; 
				 }
				 
				 public ArrayList getMedTimeList() throws TTKException {
					
					
			 		 Connection conn = null;
			 		 PreparedStatement pStmtObject = null;
			 		 ResultSet rs = null;
			 		 
			 	  	ArrayList<Object> alSearchList = new ArrayList<Object>();
			      	ArrayList<Object> alFinalList = new ArrayList<Object>();
			 		 try
			 		 {
			 			conn = ResourceManager.getConnection();
			 			pStmtObject = (PreparedStatement)conn.prepareStatement(medTimeTypeId);
			 				rs = pStmtObject.executeQuery();
			 		
			 			if(rs != null)
			 			{
			 				 while(rs.next())
			 				 {
			 					 JSONObject jsonObject = new JSONObject();
			 					jsonObject.put("MED_TIME_TYPE_ID",rs.getString(1));
								jsonObject.put("MED_TIME_TYPE_DESC",rs.getString(2));
			 					 
								 alSearchList.add(jsonObject);
							 }
							 alFinalList.add(alSearchList);

			 			 }	
			 		 }
			 			catch (SQLException | JSONException e) 
			 			{
			 				e.printStackTrace();
			 			}
			 			finally
			 			{
			 				try
			 				{
			 					if(rs != null)rs.close();
			 				} 
			 				catch (SQLException e1) 
			 				{
			 					e1.printStackTrace();
			 				}
			 				try
			 				{
			 					if(pStmtObject != null) pStmtObject.close();
			 				}catch(SQLException e)
			 				{
			 					e.printStackTrace();
			 				}try
			 				{
			 					if(conn != null) conn.close();
			 					
			 				}catch(SQLException e)
			 				{
			 					e.printStackTrace();
			 				}finally{
			 					rs = null;
			 					pStmtObject = null;
			 					conn = null;
			 				
			 				}	
			 			}
			 		 return alFinalList; 
			 }
				 
				 
				 
				 @SuppressWarnings({ "unused", "rawtypes", "null" })
				    public ArrayList getMedReminder(OnlineIntimationVO onlineIntimationVO)  throws TTKException {
				      
				       	ArrayList<Object> alSearchList = new ArrayList<Object>();
				       	ArrayList<Object> alFinalList = new ArrayList<Object>();
				   		Connection conn = null;	
				   		CallableStatement cStmtObject=null;
				   	 	ResultSet rs =null;
				        JSONArray jsonArray = new JSONArray();

				   		//Calendar calendar = Calendar.getInstance();
				   		//StringBuffer sb=null;

				   		try {
				   			conn = ResourceManager.getConnection();
				   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectMedReminders);
				   			cStmtObject.setLong(1,onlineIntimationVO.getRem_seq_id());
							
							cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
							cStmtObject.execute();					
							rs = (ResultSet)cStmtObject.getObject(2);
						
							if(rs != null)
							{
								 while(rs.next())
								 {
									 

									 JSONObject jsonObject = new JSONObject();
									jsonObject.put("REM_SEQ_ID",rs.getString("REM_SEQ_ID")!=null?(String)rs.getString("REM_SEQ_ID"):"");
									jsonObject.put("MEMBER_SEQ_ID",rs.getString("MEMBER_SEQ_ID")!=null?(String)rs.getString("MEMBER_SEQ_ID"):"");
									jsonObject.put("TPA_ENROLLMENT_ID",rs.getString("TPA_ENROLLMENT_ID")!=null?(String)rs.getString("TPA_ENROLLMENT_ID"):"");
									jsonObject.put("DRUG_NAME",rs.getString("DRUG_NAME")!=null?(String)rs.getString("DRUG_NAME"):"");
									jsonObject.put("QUANTITY",rs.getString("QUANTITY")!=null?(String)rs.getString("QUANTITY"):"");
									jsonObject.put("TIMES_PER_DAY",rs.getString("TIMES_PER_DAY")!=null?(String)rs.getString("TIMES_PER_DAY"):"");
									jsonObject.put("NO_OF_DAYS",rs.getString("NO_OF_DAYS")!=null?(String)rs.getString("NO_OF_DAYS"):"");
									jsonObject.put("INTERVAL_TYPE_ID",rs.getString("INTERVAL_TYPE_ID")!=null?(String)rs.getString("INTERVAL_TYPE_ID"):"");
									jsonObject.put("MED_TIME_TYPE_ID",rs.getString("MED_TIME_TYPE_ID")!=null?(String)rs.getString("MED_TIME_TYPE_ID"):"");
									jsonObject.put("START_DATE",rs.getString("START_DATE")!=null?(String)rs.getString("START_DATE"):"");

									 alSearchList.add(jsonObject);
								 }
								 
								 
								
								 String Status = "";
								 String MedFlag = "";
								 if(alSearchList != null && alSearchList.size() >0)
								 {
									 Status ="Search Successfully";
									 MedFlag = "Y";
														 
								 }
								 else{
									 Status ="No data Found";
									 MedFlag = "N";
								 }
								 
								 alFinalList.add(alSearchList);
								 alFinalList.add(Status);
								 alFinalList.add(MedFlag);
							}
				   		}//end of try
				          catch (SQLException sqlExp)
				   	   {
				   		throw new TTKException(sqlExp, "webservice");
				   	    }//end of catch (SQLException sqlExp)
				   	 catch (Exception exp)
				   	{
				   		throw new TTKException(exp, "webservice");
				   	}//end of catch (Exception exp)

				   	finally
				   	{
				   		// Nested Try Catch to ensure resource closure 
				   		try // First try closing the Statement
				   		{
				   			try
				   			{
				   				if(rs != null) rs.close();
				   				if (cStmtObject != null) cStmtObject.close();
				   			}//end of try
				   			catch (SQLException sqlExp)
				   			{
				   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
				   				throw new TTKException(sqlExp, "webservice");
				   			}//end of catch (SQLException sqlExp)
				   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				   			{
				   				try
				   				{
				   					if(conn != null) conn.close();
				   					
				   				}//end of try
				   				catch (SQLException sqlExp)
				   				{
				   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
				   					throw new TTKException(sqlExp, "webservice");
				   				}//end of catch (SQLException sqlExp)
				   			}//end of finally Connection Close
				   		}//end of try
				   		catch (TTKException exp)
				   		{
				   			throw new TTKException(exp, "webservice");
				   		}//end of catch (TTKException exp)
				   		finally // Control will reach here in anycase set null to the objects
				   		{
				   			rs = null;
				   			cStmtObject = null;
				   			conn = null;
				   		}//end of finally
				   	}//end of finally
				   		return alFinalList;
				   }//end o
				 
				 
				 @SuppressWarnings({ "unused", "rawtypes", "null" })
				 public int deleteMedReminder(OnlineIntimationVO onlineIntimationVO)  throws TTKException {
					   
					    Connection conn = null;	
				   		CallableStatement cStmtObject=null;
				        JSONArray jsonArray = new JSONArray();
				        int iResult =0;
				   		try {
				   			conn = ResourceManager.getConnection();
				   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteMedReminders);
				   			cStmtObject.setLong(1,onlineIntimationVO.getRem_seq_id());
				   			cStmtObject.registerOutParameter(2,Types.INTEGER);
							cStmtObject.execute();					
							iResult = cStmtObject.getInt(2);
							
				   		}//end of try
				          catch (SQLException sqlExp)
				   	   {
				   		throw new TTKException(sqlExp, "webservice");
				   	    }//end of catch (SQLException sqlExp)
				   	 catch (Exception exp)
				   	{
				   		throw new TTKException(exp, "webservice");
				   	}//end of catch (Exception exp)

				   	finally
				   	{
				   		// Nested Try Catch to ensure resource closure 
				   		try // First try closing the Statement
				   		{
				   			try
				   			{
				   				if (cStmtObject != null) cStmtObject.close();
				   			}//end of try
				   			catch (SQLException sqlExp)
				   			{
				   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
				   				throw new TTKException(sqlExp, "webservice");
				   			}//end of catch (SQLException sqlExp)
				   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				   			{
				   				try
				   				{
				   					if(conn != null) conn.close();
				   				}//end of try
				   				catch (SQLException sqlExp)
				   				{
				   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
				   					throw new TTKException(sqlExp, "webservice");
				   				}//end of catch (SQLException sqlExp)
				   			}//end of finally Connection Close
				   		}//end of try
				   		catch (TTKException exp)
				   		{
				   			throw new TTKException(exp, "webservice");
				   		}//end of catch (TTKException exp)
				   		finally // Control will reach here in anycase set null to the objects
				   		{
				   			cStmtObject = null;
				   			conn = null;
				   		}//end of finally
				   	}//end of finally
				   		return iResult;
				   }//end 
				 
				 
				 
				 @SuppressWarnings({ "unused", "rawtypes", "null" })
				    public ArrayList getMedReminderHistory(OnlineIntimationVO onlineIntimationVO)  throws TTKException {
				       
				       	ArrayList<Object> alSearchList = new ArrayList<Object>();
				       	ArrayList<Object> alFinalList = new ArrayList<Object>();
				   		Connection conn = null;	
				   		CallableStatement cStmtObject=null;
				   		ResultSet rs =null;
				        JSONArray jsonArray = new JSONArray();

				   		//Calendar calendar = Calendar.getInstance();
				   		//StringBuffer sb=null;

				   		try {
				   			conn = ResourceManager.getConnection();
				   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectMedRemindersHistory);
				   			cStmtObject.setLong(1,onlineIntimationVO.getMemberSeqID());
							
							cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
							cStmtObject.execute();					
							rs = (ResultSet)cStmtObject.getObject(2);
							
							if(rs != null)
							{
								 while(rs.next())
								 {
									 
									 JSONObject jsonObject = new JSONObject();
									 jsonObject.put("REM_SEQ_ID",rs.getString("REM_SEQ_ID")!=null?(String)rs.getString("REM_SEQ_ID"):"");
										jsonObject.put("MEMBER_SEQ_ID",rs.getString("MEMBER_SEQ_ID")!=null?(String)rs.getString("MEMBER_SEQ_ID"):"");
										jsonObject.put("TPA_ENROLLMENT_ID",rs.getString("TPA_ENROLLMENT_ID")!=null?(String)rs.getString("TPA_ENROLLMENT_ID"):"");
										jsonObject.put("DRUG_NAME",rs.getString("DRUG_NAME")!=null?(String)rs.getString("DRUG_NAME"):"");
										jsonObject.put("QUANTITY",rs.getString("QUANTITY")!=null?(String)rs.getString("QUANTITY"):"");
										jsonObject.put("TIMES_PER_DAY",rs.getString("TIMES_PER_DAY")!=null?(String)rs.getString("TIMES_PER_DAY"):"");
										jsonObject.put("NO_OF_DAYS",rs.getString("NO_OF_DAYS")!=null?(String)rs.getString("NO_OF_DAYS"):"");
										jsonObject.put("INTERVAL_TYPE_ID",rs.getString("INTERVAL_TYPE_ID")!=null?(String)rs.getString("INTERVAL_TYPE_ID"):"");
										jsonObject.put("MED_TIME_TYPE_ID",rs.getString("MED_TIME_TYPE_ID")!=null?(String)rs.getString("MED_TIME_TYPE_ID"):"");
										jsonObject.put("START_DATE",rs.getString("START_DATE")!=null?(String)rs.getString("START_DATE"):"");
									
									 alSearchList.add(jsonObject);
								 }
								 String Status="";
								 String MedFlag="";

								 if(alSearchList != null && alSearchList.size() >0)
								 {
									 Status ="Search Successfully";
									 MedFlag = "Y";
														 
								 }
								 else{
									 Status ="No data Found";
									 MedFlag = "N";
								 }
								 
								 alFinalList.add(alSearchList);
								 alFinalList.add(Status);
								 alFinalList.add(MedFlag);
							}
				   		}//end of try
				          catch (SQLException sqlExp)
				   	   {
				   		throw new TTKException(sqlExp, "webservice");
				   	    }//end of catch (SQLException sqlExp)
				   	 catch (Exception exp)
				   	{
				   		throw new TTKException(exp, "webservice");
				   	}//end of catch (Exception exp)

				   	finally
				   	{
				   		// Nested Try Catch to ensure resource closure 
				   		try // First try closing the Statement
				   		{
				   			try
				   			{
				   				if(rs != null) rs.close();
				   				if (cStmtObject != null) cStmtObject.close();
				   				
				   			}//end of try
				   			catch (SQLException sqlExp)
				   			{
				   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
				   				throw new TTKException(sqlExp, "webservice");
				   			}//end of catch (SQLException sqlExp)
				   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				   			{
				   				try
				   				{
				   					if(conn != null) conn.close();
				   					
				   				}//end of try
				   				catch (SQLException sqlExp)
				   				{
				   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
				   					throw new TTKException(sqlExp, "webservice");
				   				}//end of catch (SQLException sqlExp)
				   			}//end of finally Connection Close
				   		}//end of try
				   		catch (TTKException exp)
				   		{
				   			throw new TTKException(exp, "webservice");
				   		}//end of catch (TTKException exp)
				   		finally // Control will reach here in anycase set null to the objects
				   		{
				   			rs = null;
				   			cStmtObject = null;
				   			conn = null;
				   			
				   		}//end of finally
				   	}//end of finally
				   		return alFinalList;
				   }//end o
				 
		
				 public ArrayList saveOnlineClaimSubmission(OnlineIntimationVO onlineIntimationVO)throws TTKException 
				    {
					    ArrayList alResult =new ArrayList();
					    String strResult = "",strBatch_No = "";
					    Connection conn = null;
				        CallableStatement cStmtObject=null;
				        try {
				            
				            conn = ResourceManager.getConnection();
				            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveClaimSubmisson);

				            cStmtObject.setLong(1,0);
				            cStmtObject.setLong(2,0);
				            cStmtObject.registerOutParameter(3,OracleTypes.VARCHAR);
				            cStmtObject.setLong(4,onlineIntimationVO.getMemberSeqID());
				            cStmtObject.setString(5,onlineIntimationVO.getInvoice_Number());
				            cStmtObject.setBigDecimal(6,onlineIntimationVO.getRequestedAmt());
				            cStmtObject.setString(7,onlineIntimationVO.getReq_currency_type());
				            cStmtObject.setString(8,onlineIntimationVO.getEmailID());
				            cStmtObject.setBytes(9,onlineIntimationVO.getClaimSubmission());
				            cStmtObject.setString(10,onlineIntimationVO.getFileType());

				            cStmtObject.registerOutParameter(11,OracleTypes.VARCHAR);
				            
				            cStmtObject.execute();
				           strResult = String.valueOf(cStmtObject.getString(11));
				           strBatch_No = String.valueOf(cStmtObject.getString(3));
				            alResult.add(strResult);
				            alResult.add(strBatch_No);
			      
				        }//end of try
				        catch (SQLException sqlExp)
				        {
				            throw new TTKException(sqlExp, "webservice");
				        }//end of catch (SQLException sqlExp)
				        catch (Exception exp)
				        {
				            throw new TTKException(exp, "webservice");
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
				        			log.error("Error while closing the Statement in WebServiceDAOImpl savePolicy()",sqlExp);
				        			throw new TTKException(sqlExp, "webservice");
				        		}//end of catch (SQLException sqlExp)
				        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				        		{
				        			try
				        			{
				        				if(conn != null) conn.close();
				        				
				        			}//end of try
				        			catch (SQLException sqlExp)
				        			{
				        				log.error("Error while closing the Connection in WebServiceDAOImpl savePolicy()",sqlExp);
				        				throw new TTKException(sqlExp, "webservice");
				        			}//end of catch (SQLException sqlExp)
				        		}//end of finally Connection Close
				        	}//end of try
				        	catch (TTKException exp)
				        	{
				        		throw new TTKException(exp, "webservice");
				        	}//end of catch (TTKException exp)
				        	finally // Control will reach here in anycase set null to the objects
				        	{
				        		cStmtObject = null;
				        		conn = null;
				        		
				        	}//end of finally
						}//end of finally
				        return alResult;
				    }//end of savePolicy(String document)


				public ArrayList getOnlineClaimSubmission(OnlineIntimationVO onlineIntimationVO) throws TTKException  {
					Collection<Object> alResultList = new ArrayList<Object>();
					
					Connection conn = null;
			        CallableStatement cStmtObject=null;
			        ResultSet rs = null;
					java.sql.Blob bl = null;
					
					   byte[] bytes = new byte[1024];  
					   InputStream iStream  = null;
					   Blob blob = null;
				
							try{
									conn = ResourceManager.getConnection();
						            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strgetClaimSubmisson);
						            cStmtObject.setLong(1,onlineIntimationVO.getPolicyGrpSeqID());
						            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
						            cStmtObject.execute();
						            
						        	rs = (ResultSet)cStmtObject.getObject(2);
									
									if(rs != null)
									{
										 while(rs.next())
										 {
											
											 blob =  rs.getBlob(("image"));
												
										 }
									}
								
									int blobLength = (int) blob.length();  
									bytes= blob.getBytes(1, blobLength);
									iStream = blob.getBinaryStream();
									//	System.out.println("DAO get bytes::::"+bytes.toString());
								//	onlineIntimationVO.setClaimSubmission(bytes);
								//	alResultList.add(onlineIntimationVO);
									//alResultList.add(bytes);
									alResultList.add(iStream);
									alResultList.add(onlineIntimationVO.getPolicyGrpSeqID());
							
					
					}//end of try
					catch (SQLException sqlExp)
					{
					try {
						throw new TTKException(sqlExp, "onlineforms");
					} catch (TTKException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}//end of catch (SQLException sqlExp)
					catch (Exception exp)
					{
							throw new TTKException(exp, "onlineforms");
							}//end of catch (Exception exp)
							finally
							{
							// Nested Try Catch to ensure resource closure
							try // First try closing the result set
							{
							try
							{
							if (rs != null) rs.close();
							}//end of try
							catch (SQLException sqlExp)
							{
							log.error("Error while closing the Resultset in OnlineAccessDAOImpl getBlobDetails()",sqlExp);
							throw new TTKException(sqlExp, "onlineforms");
							}
							finally // Even if result set is not closed, control reaches here. Try closing the statement now.
							{
							try
							{
								if(rs != null) rs.close();
								if (cStmtObject != null) cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
							log.error("Error while closing the Statement in OnlineAccessDAOImpl getBlobDetails()",sqlExp);
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
							log.error("Error while closing the Connection in OnlineAccessDAOImpl getBlobDetails()",sqlExp);
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
							return (ArrayList)alResultList;
					}//end of getOnlinePolicyList(ArrayList alSearchCriteria)

				public ArrayList getOnlineClaimSubmission2(OnlineIntimationVO onlineIntimationVO) throws TTKException  {
					Collection<Object> alResultList = new ArrayList<Object>();
					Connection conn = null;
					OracleCallableStatement oCstmt = null;
				//	OnlineIntimationVO onlineIntimationVO = null;
					java.sql.Blob bl = null;
					ResultSet rs = null;
					try{
					conn = ResourceManager.getConnection();
					oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall("{CALL VIRE_MOB_APP_PCK.SELECT_CLOB(?,?)}");
					oCstmt.setLong(1,onlineIntimationVO.getPolicyGrpSeqID());
					oCstmt.registerOutParameter(2,OracleTypes.BLOB);
					oCstmt.execute();
					Object value = oCstmt.getObject(2);
						
					InputStream is=new FileInputStream("sample.pdf");
					if (value instanceof Blob) {
					Blob b = (Blob) value;
					if (b.length() > 0)
						onlineIntimationVO.setFileDataOInputStream(b.getBinaryStream()); //b.getBinaryStream();
					}
						
					if(bl.getBinaryStream() != null)
					{
						onlineIntimationVO.setFileDataOInputStream(bl.getBinaryStream());
					}
					else
					{
						onlineIntimationVO.setFileDataOInputStream(is);
					}
					alResultList.add(onlineIntimationVO);
					}
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
					// Nested Try Catch to ensure resource closure
					try // First try closing the result set
					{
					try
					{
					if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
					log.error("Error while closing the Resultset in OnlineAccessDAOImpl getBlobDetails()",sqlExp);
					throw new TTKException(sqlExp, "onlineforms");
					}
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
					try
					{
					if (oCstmt != null) oCstmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
					log.error("Error while closing the Statement in OnlineAccessDAOImpl getBlobDetails()",sqlExp);
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
					log.error("Error while closing the Connection in OnlineAccessDAOImpl getBlobDetails()",sqlExp);
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
					oCstmt = null;
					conn = null;
					}//end of finally
					}//end of finally
					return (ArrayList)alResultList;
					}//end of getOnlinePolicyList(ArrayList alSearchCriteria)


				
				
				 
				 @SuppressWarnings({ "unused", "rawtypes", "null" })
				 public ArrayList claimSubmissionTrigger(OnlineIntimationVO onlineIntimationVO) throws TTKException {
				       
				       	ArrayList<Object> alSearchList = new ArrayList<Object>();
				      	ArrayList<Object> alFinalList = new ArrayList<Object>();
				   		Connection conn = null;	
				   		CallableStatement cStmtObject=null;
				   		ResultSet rs =null;
				   		
				        JSONArray jsonArray = new JSONArray();

				   		try {
				   			conn = ResourceManager.getConnection();
				   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimSubmission);
				   			cStmtObject.setLong(1,onlineIntimationVO.getPolicyGrpSeqID());
							cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
						 	cStmtObject.registerOutParameter(3,Types.VARCHAR);
						  	cStmtObject.registerOutParameter(4,Types.VARCHAR);
						  	cStmtObject.registerOutParameter(5,Types.VARCHAR);
				
							cStmtObject.execute();					
							rs = (ResultSet)cStmtObject.getObject(2);
							
							if(rs != null)
							{
								 while(rs.next())
								 {
									 
									
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("CURRENCY_ID",rs.getString("CURRENCY_ID")!=null?(String)rs.getString("CURRENCY_ID"):"");
									jsonObject.put("CURRENCY_NAME",rs.getString("CURRENCY_NAME")!=null?(String)rs.getString("CURRENCY_NAME"):"");
								
									
									alSearchList.add(jsonObject);
								 }
								 alFinalList.add(alSearchList);
							}
							 JSONObject jsonObject2 = new JSONObject();
							 jsonObject2.put("MEM_EMAIL_ID",TTKCommon.checkNull(cStmtObject.getString(3))!=null?TTKCommon.checkNull(cStmtObject.getString(3)):"");
							 jsonObject2.put("EMAIL_PRESENT_YN",TTKCommon.checkNull(cStmtObject.getString(4))!=null?TTKCommon.checkNull(cStmtObject.getString(4)):"");
							 jsonObject2.put("ALERT_MSG",TTKCommon.checkNull(cStmtObject.getString(5))!=null?TTKCommon.checkNull(cStmtObject.getString(5)):"");
							
							 
							 alFinalList.add(jsonObject2);
					
				   		}//end of try
				          catch (SQLException sqlExp)
				   	   {
				   		throw new TTKException(sqlExp, "webservice");
				   	    }//end of catch (SQLException sqlExp)
				   	 catch (Exception exp)
				   	{
				   		throw new TTKException(exp, "webservice");
				   	}//end of catch (Exception exp)

				   	finally
				   	{
				   		// Nested Try Catch to ensure resource closure 
				   		try // First try closing the Statement
				   		{
				   			try
				   			{
				   				if (rs != null) rs.close();
				   				
				   			}//end of try
				   			catch (SQLException sqlExp)
				   			{
				   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
				   				throw new TTKException(sqlExp, "webservice");
				   			}//end of catch (SQLException sqlExp)
				   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				   			{
				   				try
				   				{
				   					if (cStmtObject != null) cStmtObject.close();
				   					
				   				}//end of try
				   				catch (SQLException sqlExp)
				   				{
				   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
				   					throw new TTKException(sqlExp, "webservice");
				   				}//end of catch (SQLException sqlExp)
				   				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
                                {
                                            try
                                            {
                                               if(conn != null) conn.close();
                                            }//end of try
                                            catch (SQLException sqlExp)
                                            {
            				   					       log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
                    				   					throw new TTKException(sqlExp, "webservice");

                                            }//end of catch (SQLException sqlExp)
                                }//end of finally Connection Close
				   			}//end of finally Connection Close
				   		}//end of try
				   		catch (TTKException exp)
				   		{
				   			throw new TTKException(exp, "webservice");
				   		}//end of catch (TTKException exp)
				   		finally // Control will reach here in anycase set null to the objects
				   		{
				   			rs = null;
				   			cStmtObject = null;
				   			conn = null;
				   			
				   		}//end of finally
				   	}//end of finally
				   		return alFinalList;
				   }//end o
				 
				 
					/* This method method stores the state response coming from Vidal wire rest as a String  */
					public ArrayList getCountryList() throws TTKException
					{
								 Connection conn = null;
								 PreparedStatement pStmtObject = null;
								 ResultSet rs = null;
							    ArrayList<Object> alSearchList = new ArrayList<Object>();
							    	String alertMsg ="",providerFlag="";

								 ArrayList<Object> alCountryList = new ArrayList<Object>();
								 try
								 {
									 conn = ResourceManager.getConnection();
									pStmtObject = (PreparedStatement)conn.prepareStatement(Countrydetails);
									rs = pStmtObject.executeQuery();
								
									if(rs != null)
									{
										 while(rs.next())
										 {
											    JSONObject jsonObject = new JSONObject();
												jsonObject.put("COUNTRY_ID",rs.getString("COUNTRY_ID")!=null?(String)rs.getString("COUNTRY_ID"):"");
												jsonObject.put("COUNTRY_NAME",rs.getString("COUNTRY_NAME")!=null?(String)rs.getString("COUNTRY_NAME"):"");
												alCountryList.add(jsonObject);
										 }
										 if(alCountryList != null && alCountryList.size() >0)
										 {
											 alertMsg ="Search Successfully";
											 providerFlag = "Y";
																 
										 }
										 else{
											 alertMsg ="No data Found";
											 providerFlag = "N";
										 }
										 
										 alSearchList.add(alCountryList);
										 alSearchList.add(providerFlag);
										 alSearchList.add(alertMsg);
										 
									 }	
								 }
								 catch (SQLException | JSONException e) 
						 			{
						 				e.printStackTrace();
						 			}
									finally
									{
										try
										{
											if(rs != null)rs.close();
										} 
										catch (SQLException e1) 
										{
											e1.printStackTrace();
										}
										try
										{
											if(pStmtObject != null) pStmtObject.close();
										}catch(SQLException e)
										{
											e.printStackTrace();
										}try
										{
											if(conn != null) conn.close();
										}catch(SQLException e)
										{
											e.printStackTrace();
										}finally{
											rs = null;
											pStmtObject = null;
											conn = null;
										}	
									}
								 return alSearchList; 	
					}
					/* This method method stores the city response coming from Vidal wire rest as a String  */
					public ArrayList getStateList(String alkCountryId)
					{		
							 Connection conn = null;
							 PreparedStatement pStmtObject = null;
							 ResultSet rs = null;
							 ArrayList<Object> alSearchList = new ArrayList<Object>();
							 	ArrayList<Object> alStatelist = new ArrayList<Object>();
							 	String alertMsg ="",providerFlag="";
							 try
							 {
								 
																
								conn = ResourceManager.getConnection();
								pStmtObject = (PreparedStatement)conn.prepareStatement(Statedetails);
								pStmtObject.setString(1, alkCountryId);
								rs = pStmtObject.executeQuery();
							
								if(rs != null)
								{
									 while(rs.next())
									 {
						 					JSONObject jsonObject = new JSONObject();
											jsonObject.put("STATE_TYPE_ID",rs.getString("STATE_TYPE_ID")!=null?(String)rs.getString("STATE_TYPE_ID"):"");
											jsonObject.put("STATE_NAME",rs.getString("STATE_NAME")!=null?(String)rs.getString("STATE_NAME"):"");
											alStatelist.add(jsonObject);
									 }
									 
										if(alStatelist != null && alStatelist.size() >0)
										 {
											 alertMsg ="Search Successfully";
											 providerFlag = "Y";
																 
										 }
										 else{
											 alertMsg ="No data Found";
											 providerFlag = "N";
										 }
										 
										 alSearchList.add(alStatelist);
										 alSearchList.add(providerFlag);
										 alSearchList.add(alertMsg);
									 
									 
	
								 }	

							 }
							 catch (SQLException | JSONException | TTKException e) 
					 			{
					 				e.printStackTrace();
					 			}
								finally
								{
									try
									{
										if(rs != null)rs.close();
									} 
									catch (SQLException e1) 
									{
										e1.printStackTrace();
									}
									try
									{
										if(pStmtObject != null) pStmtObject.close();
									}catch(SQLException e)
									{
										e.printStackTrace();
									}try
									{
										if(conn != null) conn.close();
									}catch(SQLException e)
									{
										e.printStackTrace();
									}finally{
										rs = null;
										pStmtObject = null;
										conn = null;
									}	
								}
							 return alSearchList; 
					}
					
					/* This method method stores the city response coming from Vidal wire rest as a String  */
					public ArrayList getAreaList(String alkStateId)
					{		
							 Connection conn = null;
							 PreparedStatement pStmtObject = null;
							 ResultSet rs = null;
							 ArrayList<Object> alSearchList = new ArrayList<Object>();
							 	ArrayList<Object> alStatelist = new ArrayList<Object>();
							 	String alertMsg ="",providerFlag="";
							 try
							 {
								 
																
								conn = ResourceManager.getConnection();
								pStmtObject = (PreparedStatement)conn.prepareStatement(Areadetails);
								pStmtObject.setString(1, alkStateId);
								rs = pStmtObject.executeQuery();
							
								if(rs != null)
								{
									 while(rs.next())
									 {
						 					JSONObject jsonObject = new JSONObject();
											jsonObject.put("CITY_TYPE_ID",rs.getString("CITY_TYPE_ID")!=null?(String)rs.getString("CITY_TYPE_ID"):"");
											jsonObject.put("CITY_DESCRIPTION",rs.getString("CITY_DESCRIPTION")!=null?(String)rs.getString("CITY_DESCRIPTION"):"");
											alStatelist.add(jsonObject);
									 }
									 
									 
									 if(alStatelist != null && alStatelist.size() >0)
									 {
										 alertMsg ="Search Successfully";
										 providerFlag = "Y";
															 
									 }
									 else{
										 alertMsg ="No data Found";
										 providerFlag = "N";
									 }
									 
									 alSearchList.add(alStatelist);
									 alSearchList.add(providerFlag);
									 alSearchList.add(alertMsg);
									 
									 
	
								 }	

							 }
							 catch (SQLException | JSONException | TTKException e) 
					 			{
					 				e.printStackTrace();
					 			}
								finally
								{
									try
									{
										if(rs != null)rs.close();
									} 
									catch (SQLException e1) 
									{
										e1.printStackTrace();
									}
									try
									{
										if(pStmtObject != null) pStmtObject.close();
									}catch(SQLException e)
									{
										e.printStackTrace();
									}try
									{
										if(conn != null) conn.close();
									}catch(SQLException e)
									{
										e.printStackTrace();
									}finally{
										rs = null;
										pStmtObject = null;
										conn = null;
									}	
								}
							 return alSearchList; 
					}
					
					
					 public ArrayList getProTypeSpeciality(String module) throws TTKException {
							
				 		 Connection conn = null;
				 		 PreparedStatement pStmtObject = null;
				 		 ResultSet rs = null;
				 	  	ArrayList<Object> alSearchList = new ArrayList<Object>();
				      	ArrayList<Object> alFinalList = new ArrayList<Object>();
				      	String alertMsg ="",providerFlag="";
				      //	module values : STATE ,COUNTRY ,PROTYPE ,PROSPECIALITY
				 		 try
				 		 {
				 			conn = ResourceManager.getConnection();
				 			if(module.equalsIgnoreCase("PROTYPE")){
				 			pStmtObject = (PreparedStatement)conn.prepareStatement(proTypeId);
				 			}else if(module.equalsIgnoreCase("PROSPECIALITY")){
					 		pStmtObject = (PreparedStatement)conn.prepareStatement(proSpeciality);
				 			}else if(module.equalsIgnoreCase("GENDER")){
						 		pStmtObject = (PreparedStatement)conn.prepareStatement(strGender);
				 			}else if(module.equalsIgnoreCase("NWTYPE")){
						 		pStmtObject = (PreparedStatement)conn.prepareStatement(strNetworkType);
				 			}
				 			
				 			
				 				rs = pStmtObject.executeQuery();
				 		
				 			if(rs != null)
				 			{
				 				 while(rs.next())
				 				 {
				 					 JSONObject jsonObject = new JSONObject();
				 					jsonObject.put("ID",rs.getString(1));
									jsonObject.put("DESCRIPTION",rs.getString(2));
				 					 
									 alSearchList.add(jsonObject);
								 }
				 				 
				 				if(alSearchList != null && alSearchList.size() >0)
				 				 {
				 					 alertMsg ="Search Successfully";
				 					 providerFlag = "Y";
				 										 
				 				 }
				 				 else{
				 					 alertMsg ="No data Found";
				 					 providerFlag = "N";
				 				 }
				 				 
				 				alFinalList.add(alSearchList);
				 				alFinalList.add(providerFlag);
				 				alFinalList.add(alertMsg);
				 			

				 			 }	
				 		 }
				 			catch (SQLException | JSONException e) 
				 			{
				 				e.printStackTrace();
				 			}
				 			finally
				 			{
				 				try
				 				{
				 					if(rs != null)rs.close();
				 				} 
				 				catch (SQLException e1) 
				 				{
				 					e1.printStackTrace();
				 				}
				 				try
				 				{
				 					if(pStmtObject != null) pStmtObject.close();
				 				}catch(SQLException e)
				 				{
				 					e.printStackTrace();
				 				}try
				 				{
				 					if(conn != null) conn.close();
				 				}catch(SQLException e)
				 				{
				 					e.printStackTrace();
				 				}finally{
				 					rs = null;
				 					pStmtObject = null;
				 					conn = null;
				 				}	
				 			}
				 		 return alFinalList; 
				 }
					 
					 public ArrayList searchProviderList( String stralkCountry,String stralkState,String stralkProviderType,String alkSpeciality,String alkNetworkType) throws TTKException{
							ArrayList<Object> alSearchList = new ArrayList<Object>();
							ArrayList<Object> alFinalList = new ArrayList<Object>();

							Connection conn = null;
							CallableStatement cStmtObject=null;
							ResultSet rs = null;
							 
					    	try{
								conn = ResourceManager.getConnection();
								cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMobProviderSearchList);
							
										cStmtObject.setString(1,(String)stralkCountry);  
						                cStmtObject.setString(2,(String)stralkState);  
						                cStmtObject.setString(3,(String)stralkProviderType); 
						                cStmtObject.setString(4,(String)alkSpeciality);
						                cStmtObject.setString(5,"HOSP_NAME");
						                cStmtObject.setString(6,"ASC");
						                cStmtObject.setString(7,"1");
						                cStmtObject.setString(8,"1000");
						                cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);
						                cStmtObject.setString(10,(String)alkNetworkType);
						                cStmtObject.execute();
						                rs = (java.sql.ResultSet)cStmtObject.getObject(9);
						            	if(rs != null)
										{
											 while(rs.next())
											 {

												 JSONObject jsonObjgeneral = new JSONObject();
												 jsonObjgeneral.put("HOSP_NAME",rs.getString("HOSP_NAME")!=null?(String)rs.getString("HOSP_NAME"):"");
												 jsonObjgeneral.put("PROVIDER_TYPE_ID",rs.getString("PROVIDER_TYPE_ID")!=null?(String)rs.getString("PROVIDER_TYPE_ID"):"");
												 jsonObjgeneral.put("EMPANEL_STATUS_YN",rs.getString("EMPANEL_STATUS_YN")!=null?(String)rs.getString("EMPANEL_STATUS_YN"):"");
												 jsonObjgeneral.put("COUNTRY_NAME",rs.getString("COUNTRY_NAME")!=null?(String)rs.getString("COUNTRY_NAME"):"");
												 jsonObjgeneral.put("STATE_NAME",rs.getString("STATE_NAME")!=null?(String)rs.getString("STATE_NAME"):"");
												 
												 jsonObjgeneral.put("CITY_DESCRIPTION",rs.getString("CITY_DESCRIPTION")!=null?(String)rs.getString("CITY_DESCRIPTION"):"");
												 jsonObjgeneral.put("DESCRIPTION",rs.getString("DESCRIPTION")!=null?(String)rs.getString("DESCRIPTION"):"");
												 jsonObjgeneral.put("ADDRESS",rs.getString("ADDRESS")!=null?(String)rs.getString("ADDRESS"):"");
												 jsonObjgeneral.put("MOBILE_NO",rs.getString("MOBILE_NO")!=null?(String)rs.getString("MOBILE_NO"):"");
												 jsonObjgeneral.put("OFF_PHONE_NO_2",rs.getString("OFF_PHONE_NO_2")!=null?(String)rs.getString("OFF_PHONE_NO_2"):"");
												 jsonObjgeneral.put("HOSP_EMAIL_ID",rs.getString("HOSP_EMAIL_ID")!=null?(String)rs.getString("HOSP_EMAIL_ID"):"");
												 jsonObjgeneral.put("LATITUDE",rs.getString("LATITUDE")!=null?(String)rs.getString("LATITUDE"):"");
												 jsonObjgeneral.put("LONGITUDE",rs.getString("LONGITUDE")!=null?(String)rs.getString("LONGITUDE"):"");
												 jsonObjgeneral.put("NETWORK_TYPE",rs.getString("NETWORK_TYPE")!=null?(String)rs.getString("NETWORK_TYPE"):"");

												 alSearchList.add(jsonObjgeneral);

											 }
											 alFinalList.add(alSearchList);
										}
						            	
								
								
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
										log.error("Error while closing the Resultset in OnlineAccessDAOImpl getOnlineVBSearch()",sqlExp);
										throw new TTKException(sqlExp, "onlineforms");
									}//end of catch (SQLException sqlExp)
									finally // Even if result set is not closed, control reaches here. Try closing the statement now.
									{

										try
										{
											if (cStmtObject != null) cStmtObject.close();
										}//end of try
										catch (SQLException sqlExp)
										{
											log.error("Error while closing the Statement in OnlineAccessDAOImpl getOnlineVBSearch()",sqlExp);
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
												log.error("Error while closing the Connection in PreAuthDAOImpl getOnlineVBSearch()",sqlExp);
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
					   		return alFinalList;
						}//end of getPreAuthList(ArrayList alSearchCriteria)
					 
					 public int saveContactUs(String alAlkootId,String alMobileNo,String alEmailId, String alSubModule, String alIssueDesc)  throws TTKException{

					        int intResult =0;
					        Connection conn = null;
					        CallableStatement cStmtObject=null;
					        try {
					        	conn = ResourceManager.getConnection();
					        	cStmtObject = (java.sql.CallableStatement)conn.prepareCall("{CALL VIRE_MOB_APP_PCK.SUBMIT_QUERY(?,?,?,?,?,?)}");
					        	cStmtObject.setString(1,alAlkootId);
					        	cStmtObject.setString(2,alMobileNo);
					        	cStmtObject.setString(3,alEmailId);
					            cStmtObject.setString(4,alSubModule);
					            cStmtObject.setString(5,alIssueDesc);
					               
					              cStmtObject.registerOutParameter(6,Types.INTEGER);
					            
					              cStmtObject.execute();
					              intResult = cStmtObject.getInt(6);
					           }//end of try
					        catch (SQLException sqlExp)
					        {
					            throw new TTKException(sqlExp, "webservice");
					        }//end of catch (SQLException sqlExp)
					        catch (Exception exp)
					        {
					            throw new TTKException(exp, "webservice");
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
					        			log.error("Error while closing the Statement in WebServiceDAOImpl getTableData()",sqlExp);
					        			throw new TTKException(sqlExp, "webservice");
					        		}//end of catch (SQLException sqlExp)
					        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					        		{
					        			try
					        			{
					        				if(conn != null) conn.close();
					        			}//end of try
					        			catch (SQLException sqlExp)
					        			{
					        				log.error("Error while closing the Connection in WebServiceDAOImpl getTableData()",sqlExp);
					        				throw new TTKException(sqlExp, "webservice");
					        			}//end of catch (SQLException sqlExp)
					        		}//end of finally Connection Close
					        	}//end of try
					        	catch (TTKException exp)
					        	{
					        		throw new TTKException(exp, "webservice");
					        	}//end of catch (TTKException exp)
					        	finally // Control will reach here in anycase set null to the objects
					        	{
					        		cStmtObject = null;
					        		conn = null;
					        	}//end of finally
							}//end of finally
					        return intResult;
					       //return (doc!=null?doc.asXML():null);
					    }//end of getTableData( )
				 
/*	------------------------------------------------end of mobile app------------------------------------------------------*/	
	    
    public String savePolicy(String document) throws TTKException
    {
        String strResult="";
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try {
            conn = ResourceManager.getConnection();
            //conn = ResourceManager.getWebserviceConnection();
            //PolicyVO policyVO = new PolicyVO();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePolicy);
            XMLType policyXML = null;
            if(document != null)
            {
                policyXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), document);
            }//end of if(document != null)
            cStmtObject.setObject(1,policyXML);
            cStmtObject.setLong(2,0); // Place holder for Batch seq id

           // Long lngPolicySeqID = null;
          //  Long lngPolicyGroupSeqID = null;
           // String strEnrTypeID ="";

            cStmtObject.registerOutParameter(2,Types.BIGINT); // batch seq id
            cStmtObject.registerOutParameter(3,Types.BIGINT); //Policy SeqID
            cStmtObject.registerOutParameter(4,Types.BIGINT); //Policy Group SeqID
            cStmtObject.registerOutParameter(5,Types.VARCHAR); //Enrollment Type ID

            cStmtObject.execute();
            strResult = String.valueOf(cStmtObject.getLong(2));

           /*lngPolicySeqID = cStmtObject.getLong(3);
            lngPolicyGroupSeqID = cStmtObject.getLong(4);
            strEnrTypeID = cStmtObject.getString(5);

            policyVO.setPolicySeqID(lngPolicySeqID);
            policyVO.setPolicyGroupSeqID(lngPolicyGroupSeqID);
            policyVO.setEnrollmentType(strEnrTypeID);
            if(lngPolicyGroupSeqID!=null && lngPolicyGroupSeqID>0)
            {
                //add the policy detail to MDB for rule execution and return the batch seq id to webservice
                this.addToPolicyQueue(policyVO);
            }//end of if(lngPolicyGroupSeqID!=null && lngPolicyGroupSeqID>0)
            */
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "webservice");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "webservice");
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
        			log.error("Error while closing the Statement in WebServiceDAOImpl savePolicy()",sqlExp);
        			throw new TTKException(sqlExp, "webservice");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in WebServiceDAOImpl savePolicy()",sqlExp);
        				throw new TTKException(sqlExp, "webservice");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "webservice");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return strResult;
    }//end of savePolicy(String document)

    /**
     * This method saves the Policy information
     * @param String object which contains the policy and member details
     * @return String object which contains
     * @exception throws TTKException
     */
    public String savePolicy(String document,String strCompAbbr) throws TTKException
    {
        String strResult="";
        Connection conn = null;
        CallableStatement cStmtObject=null;
        log.debug(" strCompAbbr "+strCompAbbr);
        try {
            if(strCompAbbr.equalsIgnoreCase("BNK")||strCompAbbr.equalsIgnoreCase("EFT"))
            {
            	conn = ResourceManager.getConnection();
//            	conn = ResourceManager.getWebserviceConnection();
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveBank);
            	XMLType policyXML = null;
                if(document != null)
                {
                    policyXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), document);
                }//end of if(document != null)
                cStmtObject.setObject(1,policyXML);
                log.debug(" Before Call Procedure  ");
                cStmtObject.execute();
            }//end of if(strCompAbbr.equalsIgnoreCase("BNK")||strCompAbbr.equalsIgnoreCase("EFT"))
			//	Changes Added for Bulk Upload CR KOC1169
            else if(strCompAbbr.equalsIgnoreCase("HOS")||strCompAbbr.equalsIgnoreCase("MEM"))
            {
            	conn = ResourceManager.getConnection();
//            	conn = ResourceManager.getWebserviceConnection();
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCustBankDtls);
            	XMLType policyXML = null;
                if(document != null)
                {
                    policyXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), document);
                //    Document podoc = (Document)policyXML.getDOM(); 
                //    
                }//end of if(document != null)
                cStmtObject.setObject(1,policyXML);
                log.debug(" Before Call Procedure  ");
                cStmtObject.execute();
            }//end of if(strCompAbbr.equalsIgnoreCase("HOS")||strCompAbbr.equalsIgnoreCase("MEM"))
            //	Changes Added for Bulk Upload CR KOC1169
            else
            {
            	return savePolicy(document);
            }//end of else
        }//end of try
        catch (SQLException sqlExp)
        {
			throw new TTKException(sqlExp, "webservice");
        }//end of catch (SQLException sqlExp)
        catch (TTKException exp)
		{
			throw exp;
        }//end of catch (Exception exp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "webservice");
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
        			log.error("Error while closing the Statement in WebServiceDAOImpl savePolicy()",sqlExp);
        			throw new TTKException(sqlExp, "webservice");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in WebServiceDAOImpl savePolicy()",sqlExp);
        				throw new TTKException(sqlExp, "webservice");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "webservice");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return strResult;
    }//end of savePolicy(String document)

    /**
     * This method returns the consolidated list of Policy Number.
     * @param String object which contains the parameter in XML format.
     * @return String object which contains Policy Numbers.
     * @exception throws TTKException.
     */
    public String getConsolidatedPolicyList(String strFromDate,String strToDate,long lngInsSeqID,long lngProductSeqID,long lngOfficeSeqID,String strEnrTypeID) throws TTKException {

        String strResult="";
        Connection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = ResourceManager.getConnection();
//          conn = ResourceManager.getWebserviceConnection();
            stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strConsolidatedPolicyList);

            XMLType policyXML = null;
            stmt.setString(1,strFromDate);
            stmt.setString(2,strToDate);
            stmt.setLong(3,lngInsSeqID);
            stmt.setLong(4,lngProductSeqID);
            stmt.setLong(5,lngOfficeSeqID);
            stmt.setString(6,strEnrTypeID);
            stmt.registerOutParameter(7,OracleTypes.OPAQUE,"SYS.XMLTYPE");
            stmt.execute();
            policyXML = XMLType.createXML(stmt.getOPAQUE(7));
            strResult = policyXML.getStringVal();
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "webservice");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "webservice");
        }//end of catch (Exception exp)

        finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (stmt != null) stmt.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in WebServiceDAOImpl getConsolidatedPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "webservice");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in WebServiceDAOImpl getConsolidatedPolicyList()",sqlExp);
        				throw new TTKException(sqlExp, "webservice");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "webservice");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		stmt = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return strResult;
    }//end of getConsolidatedPolicyList(String strValue)

    /**
     * This method method saves the Number of policy uploaded and number of policy rejected in softcopy upload.
     * @param String object which contains the softcopy upolad status in XML format.
     * @return String object which contains .
     * @exception throws TTKException.
     */
    public String saveUploadStatus(String strBatchNumber,long lngNum_of_policies_rcvd ) throws TTKException {

        String strResult="";
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try {
            conn = ResourceManager.getConnection();
//          conn = ResourceManager.getWebserviceConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveUploadStatus);
            cStmtObject.setString(1,strBatchNumber);
            cStmtObject.setLong(2,lngNum_of_policies_rcvd);
            cStmtObject.registerOutParameter(3,Types.INTEGER);
            cStmtObject.execute();
            strResult = String.valueOf(cStmtObject.getInt(3));
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "webservice");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "webservice");
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
        			log.error("Error while closing the Statement in WebServiceDAOImpl saveUploadStatus()",sqlExp);
        			throw new TTKException(sqlExp, "webservice");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in WebServiceDAOImpl saveUploadStatus()",sqlExp);
        				throw new TTKException(sqlExp, "webservice");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "webservice");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return strResult;
    }//end of saveUploadStatus(String strValue)

    /**
     * This method method returns the data from TTKOffice,Product or Branch.
     * @param String object which contains the table name for which data in is required.
     * @return String object which contains .
     * @exception throws TTKException.
     */
    public String getTableData(String strIdentifier,String strID) throws TTKException{

        String strResult="";
        Connection conn = null;
        OracleCallableStatement stmt = null;
        XMLType xml = null;
        Document doc = null;
        try {
            conn = ResourceManager.getConnection();
//          conn = ResourceManager.getWebserviceConnection();
            stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strGetTableData);
            //stmt = (OracleCallableStatement)conn.prepareCall(strGetTableData);
            stmt.setString(1,strIdentifier);
            stmt.setString(2,strID);
            stmt.registerOutParameter(3,OracleTypes.OPAQUE,"SYS.XMLTYPE");
            stmt.execute();
            xml = XMLType.createXML(stmt.getOPAQUE(3));
            DOMReader domReader = new DOMReader();
            if(xml!=null)
            {
                doc= domReader.read(xml.getDOM());
                strResult= doc.asXML();
                log.debug("strResult value is :"+strResult);
            }//end of if(xml!=null)
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "webservice");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "webservice");
        }//end of catch (Exception exp)

        finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (stmt != null) stmt.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in WebServiceDAOImpl getTableData()",sqlExp);
        			throw new TTKException(sqlExp, "webservice");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in WebServiceDAOImpl getTableData()",sqlExp);
        				throw new TTKException(sqlExp, "webservice");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "webservice");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		stmt = null;
        		conn = null;
        	}//end of finally
		}//end of finally

        return (doc!=null?doc.asXML():null);
    }//end of getTableData(String strIdentifier)
    
    /**
     * This method method returns the Rule Errors.
     * @param String object which contains the Batch Number for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String getRuleErrors(String strBatchNbr) throws TTKException {
    	String strResult="";
        Connection conn = null;
        OracleCallableStatement stmt = null;
        XMLType xml = null;
        Document doc = null;
        try {
            conn = ResourceManager.getConnection();
//          conn = ResourceManager.getWebserviceConnection();
            stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strGetRuleErrors);
            //stmt = (OracleCallableStatement)conn.prepareCall(strGetTableData);
            stmt.setString(1,strBatchNbr);
            stmt.registerOutParameter(2,OracleTypes.OPAQUE,"SYS.XMLTYPE");
            stmt.execute();
            xml = XMLType.createXML(stmt.getOPAQUE(2));
            DOMReader domReader = new DOMReader();
            if(xml!=null)
            {
                doc= domReader.read(xml.getDOM());
                strResult= doc.asXML();
                log.debug("strResult value is :"+strResult);
            }//end of if(xml!=null)
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "webservice");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "webservice");
        }//end of catch (Exception exp)

        finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (stmt != null) stmt.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in WebServiceDAOImpl getRuleErrors()",sqlExp);
        			throw new TTKException(sqlExp, "webservice");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in WebServiceDAOImpl getRuleErrors()",sqlExp);
        				throw new TTKException(sqlExp, "webservice");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "webservice");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		stmt = null;
        		conn = null;
        	}//end of finally
		}//end of finally

        return (doc!=null?doc.asXML():null);
    }//end of getRuleErrors(String strBatchNbr)

    /**
     * This mehod add the Policy detail to MDB queue for Rule Execution
     *
     * @param strPoicy String object containing policy detail in XML format
     */
    private void addToPolicyQueue(PolicyVO policyVO) throws Exception
    {
       QueueConnection cnn = null;
       QueueSender sender = null;
       QueueSession session = null;
       String strEMailQueueName = TTKPropertiesReader.getPropertyValue("JMSRULEENGINEQUEUENAME");
	   String strConnectionFactoryName = TTKPropertiesReader.getPropertyValue("JMSCONNECTIONFACTORYNAME");
	   InitialContext ctx = null;
	   Queue queue = null;
	   QueueConnectionFactory factory = null;
	   ObjectMessage om = null;
	   try{
		   ctx = new InitialContext();
		   queue = (Queue) ctx.lookup(strEMailQueueName);
		   factory = (QueueConnectionFactory) ctx.lookup(strConnectionFactoryName);
		   cnn = factory.createQueueConnection();
		   session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		   om = session.createObjectMessage(policyVO);
		   sender = session.createSender(queue);
		   sender.send(om);
	   }//end of try
	   catch(Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch(Exception exp)
		finally
		{
			try
			{
				try
				{
					sender.close();
				}catch(JMSException jmsExp)
				{
					throw new TTKException(jmsExp, "webservice");
				}//end of catch(JMSException exp)
				finally
				{
					try
					{
						cnn.stop();
					}catch(JMSException jmsExp)
					{
						throw new TTKException(jmsExp, "webservice");
					}//end of catch(JMSException exp)
					finally
					{
						try
						{
							session.close();
						}catch(JMSException jmsExp)
						{
							throw new TTKException(jmsExp, "webservice");
						}//end of catch(JMSException exp)
						finally
						{
							try
							{
								cnn.close();
							}catch(JMSException jmsExp)
							{
								throw new TTKException(jmsExp, "webservice");
							}//end of catch(JMSException exp)
						}//end of finally
					}//end of finally
				}//end of finally
			}//end of try
			finally
			{
				sender=null;
				cnn=null;
				session=null;
			}//end of finally
		}//end of finally
    }//end of addToPolicyQueue(String strPolicy)
       
    /**
     * This method method returns the String 
     * @param String object which contains the strVidalID for which data in is required.
     * @param String object which contains thestrIMEINumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */

	public String individualLoginService(String strVidalID,String strIMEINumber)throws TTKException {
		String strResult="";
		String strMessage="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE LOGIN PAGE DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strIndividualLoginService);
			cStmtObject.setString(1,strVidalID);
			cStmtObject.registerOutParameter(2,Types.VARCHAR);
			cStmtObject.registerOutParameter(3,Types.VARCHAR);
			cStmtObject.setString(4,strIMEINumber);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(2)!=null)?(String)cStmtObject.getString(2):"No Data Found";
			strMessage =cStmtObject.getString(3);
			log.info("INSIDE LOGIN PAGE DAO END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl individualLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl individualLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
    }//end of individualLoginService(String strVidalID,StrIMEINumber)

    
       
    /**
     * This method method returns the String or saves a xml in the desired given location
     * @param String object which contains the strVidalID for which data in is required.
     * @param String object which contains thestrIMEINumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */

	public String getXmlandSaveXml(String strInsID)throws TTKException {
		
		String strResult		=	"";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		 OracleCallableStatement stmt = null;
		try {
			 conn = ResourceManager.getConnection();
//	          conn = ResourceManager.getWebserviceConnection();
	            stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strFetchXml);

	            XMLType policyXML = null;
	            stmt.registerOutParameter(1,OracleTypes.OPAQUE,"SYS.XMLTYPE");
	            stmt.execute();
	            policyXML = XMLType.createXML(stmt.getOPAQUE(1));
	            strResult = policyXML.getStringVal();
	            
	            /*stmt.setString(1, "603900");  
	            stmt.registerOutParameter(2,OracleTypes.OPAQUE,"SYS.XMLTYPE");  
	            stmt.registerOutParameter(3,Types.INTEGER); 
	            stmt.execute();
	            policyXML = XMLType.createXML(stmt.getOPAQUE(2));*/
	            
	            strResult = policyXML.getStringVal();
	            
	        	
	        	
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the Statement
			{
				try
				{
					if (stmt != null) stmt.close();
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getXmlandSaveXml()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getXmlandSaveXml()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				stmt=null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
    }//end of getXmlandSaveXml(String strVidalID,StrIMEINumber)
	/**
     * This method method returns the String Buffer details of Ecard
     * @param String object which contains the Vidal Id for which data in is required.
     * @param String object which contains the strIMEINumber for which data in is required.
	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public StringBuffer getEcardTemplate(String strVidalId, String strPolicyNumber,String strIMEINumber)throws TTKException {
			String strTemplate="";
			String strPolicyGrpSeqId="";
			String strMemSeqId="";
			Connection conn = null;
			StringBuffer strTemplateData=new StringBuffer();
			CallableStatement cStmtObject=null;
			try {
				log.info("INSIDE ECARD SERVICES DAO START");
				conn = ResourceManager.getConnection();

				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTemplateInfo);
				cStmtObject.setString(1,strPolicyNumber);
				cStmtObject.setString(2,strVidalId);
				cStmtObject.registerOutParameter(3,Types.VARCHAR);
				cStmtObject.registerOutParameter(4,Types.VARCHAR);
				cStmtObject.registerOutParameter(5,Types.VARCHAR);
				cStmtObject.setString(6,strIMEINumber);
				cStmtObject.execute();
		 		strPolicyGrpSeqId = cStmtObject.getString(3);
				strMemSeqId = cStmtObject.getString(4);
				strTemplate = cStmtObject.getString(5);
				strTemplateData=(strTemplate !=null)?strTemplateData.append(strPolicyGrpSeqId).append(",").append(strTemplate).append(",").append(strMemSeqId):new StringBuffer("NA");
				log.info("INSIDE ECARD SERVICES DAO END");
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "webservice");
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
						log.error("Error while closing the Statement in WebServiceDAOImpl feedBackService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in WebServiceDAOImpl feedBackService()",sqlExp);
							throw new TTKException(sqlExp, "webservice");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "webservice");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
			return strTemplateData;
	}//end of getEcardTemplate(String strVidalId, String strPolicyNumber,String strIMEINumber)

		
	/**
	 * This method method returns theSuccess Message
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the Policy Number for which data in is required.
	 * @param String object which contains the Diagnostic Details for which data in is required.
	 * @param String object which contains the ClaimAmount for which data in is required.
	 * @param String object which contains the strIMEINumber for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */

	public String  claimIntimationService(String strVidalId,String strPolicyNumber,String strDiagnosticDetails,String strClaimAmount,String strHospitalName,String strHospitalAddress,String strDtAdmission,String strDtDischarge,String strIMEINumber)throws TTKException {
		String strResult="";
		String strIntimationId="";//new change 27 jan2014
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE CLAIM INTIMATION  DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimIntimationService);
			cStmtObject.setString(1,strPolicyNumber);
			cStmtObject.setString(2,strVidalId);
			cStmtObject.setString(3,strDiagnosticDetails);
			cStmtObject.setString(4,strClaimAmount);
			cStmtObject.setString(5,strHospitalName);
			cStmtObject.setString(6,strHospitalAddress);
			cStmtObject.setString(7,strDtAdmission);
			cStmtObject.setString(8,strDtDischarge);
			cStmtObject.registerOutParameter(9,Types.INTEGER);
			cStmtObject.setString(10,strIMEINumber);
			cStmtObject.registerOutParameter(11,Types.VARCHAR);
			//v_clm_intimation_id   out tpa_call_claim_intimation.clm_intimation_id%type)
			cStmtObject.execute();
			   if(cStmtObject.getString(11)!=null || (!cStmtObject.getString(11).equalsIgnoreCase("")))
			{
			strIntimationId=cStmtObject.getString(11);
			}
		//	strIntimationId=(!cStmtObject.getString(11).equalsIgnoreCase(""))? cStmtObject.getString(11) : "";
			//v_clm_intimation_id   out tpa_call_claim_intimation.clm_intimation_id%type)

			strResult =(cStmtObject.getInt(9)>0)?"Intimation Updated Successfully,Please find your Intimation&#"+strIntimationId+"&#":"Intimation Not Updated";
	

			strResult =(cStmtObject.getInt(9)>0)?"Intimation Updated Successfully,Please find your Intimation&#"+strIntimationId+"&#":"Intimation Not Updated";
			log.info("INSIDE CLAIM INTIMATION  DAO END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl claimsService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl claimsService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
    }//end of   claimIntimationService(String strVidalId,String strPolicyNumber,String strDiagnosticDetails,String strClaimAmount,String strHospitalName,String strHospitalAddress,String strDtAdmission,String strDtDischarge,String strIMEINumber)

	/**
	 * This method method returns the List of Claims and Policies as a form of String
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the strIMEINumber for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String policySearchService(String strVidalId,String strIMEINumber)throws TTKException {
		String strResult="";
		
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE POLICY SEARCH DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPolicySearchService);
			cStmtObject.setString(1,strVidalId);
			cStmtObject.registerOutParameter(2,Types.VARCHAR);
			cStmtObject.setString(3,strIMEINumber);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(2)!=null)?(String)cStmtObject.getString(2):"No Data Found";
			log.info("INSIDE POLICY SEARCH DAO END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl policySearchService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl policySearchService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of policySearchService(String strVidalId,String strIMEINumber)

	/**
	 * This method method returns the Claim Details
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the Policy Number for which data in is required.
	 * @param String object which contains the strMode for which data in is required.
	 * @param String object which contains the strIMEINumberfor which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String claimHistoryService(String strVidalId, String strPolicyNumber,String strMode,String strIMEINumber)throws TTKException {
		String strResult="";
		String mode="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE CLAIM HISTORY DAO START");
			if(strMode.equalsIgnoreCase("PREAUTH"))
			{
			mode="PAT";
			}
			else if(strMode.equalsIgnoreCase("CLAIM"))
			{
			mode="CLM";
			}
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimHistoryService);
			cStmtObject.setString(1,strPolicyNumber);
			cStmtObject.setString(2,strVidalId);
			cStmtObject.setString(3,mode);

			cStmtObject.registerOutParameter(4,Types.VARCHAR);
			cStmtObject.setString(5,strIMEINumber);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(4)!=null)?(String)cStmtObject.getString(4):"No Data Found";
			log.info("INSIDE CLAIM HISTORY DAO END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl claimHistoryService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl claimHistoryService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of String claimHistoryService(String strVidalId, String strPolicyNumber,String strMode,String strIMEINumber)

	/**
	 * This method method returns the Claim status as a String
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the Policy Number for which data in is required.
	 * @param String object which contains the Claim Number for which data in is required.
	 * @param String object which contains the Imei Number for which data in is required.
	 
     * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String claimStatusService(String strVidalId, String strPolicyNumber,String strClaimNumber,String strIMEINumber)throws TTKException {
		String strResult="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE CLAIM STATUS DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimStatusService);
			cStmtObject.setString(1,strPolicyNumber);
			cStmtObject.setString(2,strVidalId);
			cStmtObject.setString(3,strClaimNumber);
			cStmtObject.registerOutParameter(4,Types.VARCHAR);
			
			cStmtObject.setString(5,strIMEINumber);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(4)!=null)?(String)cStmtObject.getString(4):"No Data Found";
			log.info("INSIDE CLAIM STATUS DAO END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl claimStatusService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl claimStatusService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of claimStatusService(String strVidalId, String strPolicyNumber,String strClaimNumber,String strIMEINumber)

	
  /**
   * This method method returns the Text 
   * @param String object which contains the Vidal Id for which data in is required.
   *  @param String object which contains the IMEI Nyumber for which data in is required.
   * @param String object which contains the HospID for which data in is required.
   *  @param int  which contains the int rating for which data in is required.
   *  @return String object which contains Rule Errors.
   * @exception throws TTKException.
   */
	public String feedBackService(String strVidalId,String strIMEINumber,String strHospId,int intRating)throws TTKException {
		String strResult="";
		int iResult;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE FEEDBACK SERVICES DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strFeedBackServiceWithNewChanges);
			cStmtObject.setString(1,strHospId.trim());//SODPID
			cStmtObject.setString(2,strVidalId.trim());//VIDAL ID
			cStmtObject.setInt(3,intRating);//Feed Back Rating
			cStmtObject.registerOutParameter(4,Types.INTEGER);
			cStmtObject.setString(5,strIMEINumber);//IMEI NUMBER
			cStmtObject.execute();
			strResult =(cStmtObject.getInt(4)>0)?"FeedBack Updated Successfully":"FeedBack Not Updated";
			log.info("INSIDE FEEDBACK SERVICES DAO END");
			//int ir  = cStmtObject.getInt(4);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl feedBackService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl feedBackService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end offeedBackService(String strVidalId,String strIMEINumber,String strHospId,int intRating)
	

	/**
     * This method method returns the Text 
     * @param String object which contains the Vidal Id for which data in is required.
     *  @param String object which contains the strPolicyNumber for which data in is required.
     *  @param String object which contains the strIMEINumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String getDependentDetailsService(String strVidalId, String strPolicyNumber,String strIMEINumber)throws TTKException {
		String strResult="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE DEPENDENT DETAILS DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDependentInfo);
			cStmtObject.setString(1,strPolicyNumber);
			cStmtObject.setString(2,strVidalId);
			cStmtObject.registerOutParameter(3,Types.VARCHAR);
			cStmtObject.setString(4,strIMEINumber);
			cStmtObject.execute();

			
			strResult =(cStmtObject.getString(3)!=null)?(String)cStmtObject.getString(3):"No Data Found";
			log.info("INSIDE DEPENDENT DETAILS DAO END");
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl feedBackService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl feedBackService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of getDependentDetailsService(String strVidalId, String strPolicyNumber,String strIMEINumber)



	 /**
     * This method returns the list of hospitals matches with longitude and latitude 
     * @param int object which contains the Start Number for which data in is required.
     * @param int object which contains the End Number for which data in is required.
     * @param String object which contains the intLatitude for which data in is required.
     * @param String object which contains the intLongitude for which data in is required.
     * @param String  object which contains the Operator for which data in is required.
     * @param double object which contains the kilometers for which data in is required.
     * @param String  object which contains the IMEi Number for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	 
	public String getHospInfoService(int startNum,int endNum,String intLatitude ,String intLongitude,String strOperator,double dbKilometers,String strIMEINumber)throws TTKException {
		String strResult="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE HOSPITAL INFORMATION DAO START");
			conn = ResourceManager.getConnection();

			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strHospitalInfo);
			cStmtObject.setInt(1,startNum);
			cStmtObject.setInt(2,endNum);
			cStmtObject.setString(3,intLatitude.trim());
			cStmtObject.setString(4,intLongitude.trim());
			cStmtObject.setString(5,strOperator);//lessthan,greaterThan,less than or equal to
			cStmtObject.setDouble(6,dbKilometers);
			cStmtObject.registerOutParameter(7,Types.VARCHAR);
			cStmtObject.setString(8,strIMEINumber);
			cStmtObject.execute();
			
			strResult =(cStmtObject.getString(7)!=null)?(String)cStmtObject.getString(7):"No Data Found";
			log.info("INSIDE HOSPITAL INFORMATION DAO END");
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl getHospInfoService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getHospInfoService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of getHospDetailsService(String searchValue,int startNum,int intNoOfRows,String strIMEINumber,String strStateID,String strCityID)
	
		
	/**
	 * This method method returns the Text 
	 * @param String object which contains the searchValue for which data in is required.
	 * @param int object which contains the startNum Id for which data in is required.
	 * @param int object which contains the intNoOfRows for which data in is required.
	 * @param String object which contains the strIMEINumber Id for which data in is required.
	 *  @param String object which contains the strStateID for which data in is required.
	 * @param String object which contains the strCityID Id for which data in is required.
	* @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String getHospDetailsService(String searchValue,int startNum,int intNoOfRows,String strIMEINumber,String strStateID,String strCityID)throws TTKException {
		String strResult="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE HOSPITAL DETAILS DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strHospitalDetails);
			cStmtObject.setString(1,searchValue);
			cStmtObject.setInt(2,startNum);
			cStmtObject.setInt(3,intNoOfRows);
			cStmtObject.registerOutParameter(4,Types.VARCHAR);
			cStmtObject.setString(5,strIMEINumber);
			cStmtObject.setString(6,strStateID);
			cStmtObject.setString(7,strCityID);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(4)!=null)?(String)cStmtObject.getString(4):"No Data Found";
			log.info("INSIDE HOSPITAL DETAILS DAO START");

		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl feedBackService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl feedBackService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of getHospDetailsService(int intLatitude ,int intLongitude)

	 /**
     * This method method returns the Text (tip_title,tip_text,tip_color)
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @param String object which contains the strIMEINumber for which data in is required search Criteria.
	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */	
	public String dailyTipService(String strDate,String strIMEINumber) throws TTKException  {
			String dailyTipData="";
			Connection conn = null;
			CallableStatement cStmtObject=null;
			
			try {
				log.info("INSIDE DAILY TIPS DAO START");
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDailyTipInfo);
				cStmtObject.setString(1,strDate);
				cStmtObject.registerOutParameter(2,Types.VARCHAR);
				cStmtObject.setString(3,strIMEINumber);
				cStmtObject.execute();
				dailyTipData =(cStmtObject.getString(2)!=null)?(String)cStmtObject.getString(2):"No Tip Found";
				log.info("INSIDE DAILY TIPS DAO END");

			}
					
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "webservice");
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
						log.error("Error while closing the Statement in WebServiceDAOImpl dailyTipService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in WebServiceDAOImpl dailyTipService()",sqlExp);
							throw new TTKException(sqlExp, "webservice");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "webservice");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
			return dailyTipData;
	}//end of dailyTipService(String strDate,String strIMEINumber)
		
		
    /**
     * This method method returns the byte[] 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @param String object which contains the strIMEINumber for which data in is required search Criteria.
	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
		public byte[] dailyTipImageService(String strDate,String strIMEINumber) throws TTKException  {
			BLOB photo = null;
			byte[] data=null;
			Connection conn = null;
			StringBuffer strTemplateData=new StringBuffer();
			CallableStatement cStmtObject=null;
			
			try {
				log.info("INSIDE DAILY TIPS IMAGE DAO START");
				conn = ResourceManager.getConnection();

				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDailyTipImage);
				cStmtObject.setString(1,strDate);
				
				cStmtObject.registerOutParameter(2,Types.BLOB);
				cStmtObject.setString(3,strIMEINumber);
				cStmtObject.execute();
				
				 photo = (BLOB) cStmtObject.getBlob(2);
			//    
           
			if(photo!=null)			{
				
				InputStream in = photo.getBinaryStream();
				int length = (int) photo.length();
				data=photo.getBytes(1,length);
			}  
			else{	data=null;}	
			log.info("INSIDE DAILY TIPS IMAGE DAO END");
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "webservice");
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
						log.error("Error while closing the Statement in WebServiceDAOImpl dailyTipService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in WebServiceDAOImpl dailyTipService()",sqlExp);
							throw new TTKException(sqlExp, "webservice");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "webservice");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
			return data;
		}//end of dailyTipImageService(String strDate,String strIMEINumber) 
		

    /**
     * This method method returns the Text 
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String docAroundClockStatusService()throws TTKException{
		String strResult="";
		String strIMEINumber="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDocAroundClockStatus);
			cStmtObject.setString(1,"");
			cStmtObject.registerOutParameter(2,Types.VARCHAR);
			cStmtObject.setString(3,strIMEINumber);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(2)!=null)?(String)cStmtObject.getString(2):"No Data Found";
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl docAroundClockStatusService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl docAroundClockStatusService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of docAroundClockStatusService()

	/**
     * This method method returns the String (Claim FeedBack Updated Successfully or not)
     * @param String object which contains the strVidalId for which data in is required search Criteria.
     * @param String object which contains the strIMEINumber for which data in is required search Criteria.
  	 * @param String object which contains the strClaimNumber for which data in is required search Criteria.
     * @param String object which contains the strFeedback for which data in is required search Criteria.
 	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String claimFeedBackService(String strVidalId,String strIMEINumber, String strClaimNumber, String strFeedback)throws TTKException {
		String strResult="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE CLAIM FEEDBACK SERVICES DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimFeedBack);
			cStmtObject.setString(1,strClaimNumber);
			cStmtObject.setString(2,strVidalId);
			cStmtObject.setString(3,strFeedback);
			cStmtObject.registerOutParameter(4,Types.VARCHAR);
			cStmtObject.setString(5,strIMEINumber);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(4)!=null)?"Claim FeedBack Updated Successfully":"Claim Feedback not Updated";
			log.info("INSIDE CLAIM FEEDBACK SERVICES DAO END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl claimfeedBackService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl claimfeedBackService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of claimFeedBackService(String strVidalId,String strIMEINumber, String strClaimNumber, String strFeedback)



    /**
     * This method method returns the Text 
     * @param String object which contains the strExpertType for which data in is required search Criteria.
     * @param String object which contains the strUserEmail for which data in is required search Criteria.
	 * @param String object which contains the strUserQuery for which data in is required search Criteria.
     * @param String object which contains the strVidalId for which data in is required search Criteria.
	  * @param String object which contains the strIMEINumber for which data in is required search Criteria.
	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	 
	public String askExpertOpnionService(String strExpertType,String strUserEmail, String strUserQuery, String strVidalId,String strIMEINumber) throws TTKException
	{
	String strResult="";

		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE EXPERT OPNION DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAskExpertOpnionService);
			cStmtObject.setString(1,strExpertType);
			cStmtObject.setString(2,strUserEmail);
			cStmtObject.setString(3,strUserQuery);
			cStmtObject.setString(4,strVidalId);
			cStmtObject.registerOutParameter(5,Types.VARCHAR);
			cStmtObject.setString(6,strIMEINumber);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(5)!=null)?(String)cStmtObject.getString(5):"No Data Found";
			log.info("INSIDE EXPERT OPNION DAO END");

		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl askExpertOpnionService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl askExpertOpnionService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of askExpertOpnionService(String strExpertType,String strUserEmail, String strUserQuery, String strVidalId,String strIMEINumber) throws TTKException

	
	/**
     * This method method returns the Text 
     * @param String object which contains the strExpertType for which data in is required search Criteria.
     * @param String object which contains the strUserEmail for which data in is required search Criteria.
	 * @param String object which contains the strUserQuery for which data in is required search Criteria.
     * @param String object which contains the strVidalId for which data in is required search Criteria.
	  * @param String object which contains the strIMEINumber for which data in is required search Criteria.
	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	 
	public String getValidatePhoneNumber(String strValue) throws TTKException
	{
	String strResult="";

		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE VALIDATE PHONE NUMBER START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strValidatePhoneNum);
			cStmtObject.setString(1,strValue);
			cStmtObject.registerOutParameter(2,Types.VARCHAR);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(2)!=null)?(String)cStmtObject.getString(2):"No customer is enrolled with given mobile no. Please contact Customer Care";
			log.info("INSIDE VALIDATE PHONE NUMBER END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl getValidatePhoneNumber()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getValidatePhoneNumber()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of getValidatePhoneNumber(String strValue) throws TTKException


	
	/**
     * This method method returns the Text 
     * @param String object which contains the strExpertType for which data in is required search Criteria.
     * @param String object which contains the strUserEmail for which data in is required search Criteria.
	 * @param String object which contains the strUserQuery for which data in is required search Criteria.
     * @param String object which contains the strVidalId for which data in is required search Criteria.
	  * @param String object which contains the strIMEINumber for which data in is required search Criteria.
	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	 
	public String getProviderList(String genTypeId, String hospName, String StateTypeID, String cityDesc, String location) throws TTKException
	{
	String strResult="";

		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE GET PROVIDER LIST START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strProviderList);
			
			if(genTypeId!=null)
				cStmtObject.setString(1,genTypeId);
			else
				cStmtObject.setString(1,null);
			
			if(hospName!=null)
				cStmtObject.setString(2,hospName);
			else
				cStmtObject.setString(2,null);
			
			if(StateTypeID!=null)
				cStmtObject.setString(3,StateTypeID);
			else
				cStmtObject.setString(3,null);
			
			if(cityDesc!=null)
				cStmtObject.setString(4,cityDesc);
			else
				cStmtObject.setString(4,null);
			
			if(location!=null)
				cStmtObject.setString(5,location);
			else
				cStmtObject.setString(5,null);
			
			cStmtObject.registerOutParameter(6,Types.CLOB);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(6)!=null)?(String)cStmtObject.getString(6):"Customer is In-Active.Please contact Customer Care.";
			log.info("INSIDE GET PROVIDER LIST END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl getProviderList()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getProviderList()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of getProviderList(Long genTypeId, String hospName, String StateTypeID, String cityDesc, String location) throws TTKException



	

	
	/**
     * 
     * @param String object which contains the strIMEINumber for which data in is required search Criteria.
	 * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	 
	public String getDiagTests(Long hospSeqId) throws TTKException
	{
	String strResult="";

		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE GET DIAG LIST START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDiagList);
			
			if(hospSeqId!=null)
				cStmtObject.setLong(1,hospSeqId);
			else
				cStmtObject.setString(1,null);
			
			cStmtObject.registerOutParameter(2,Types.CLOB);
			cStmtObject.execute();
			strResult =(cStmtObject.getString(2)!=null)?(String)cStmtObject.getString(2):"No Tariff.";
			log.info("INSIDE GET DIAG LIST END");
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl getProviderList()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getProviderList()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of getProviderList(Long genTypeId, String hospName, String StateTypeID, String cityDesc, String location) throws TTKException
	
	
	
	
	/**
     * This method method returns the Text 
     * @param String object which contains the Vidal Id for which data in is required.
     *  @param String object which contains the strPolicyNumber for which data in is required.
     *  @param String object which contains the strIMEINumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String getPreAuthClaimsCount(String strVidalId, String strPolicyNumber,String strIMEINumber)throws TTKException {
		String strResult="";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			log.info("INSIDE PREAUTN/CLAIMS COUNT DAO START");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthClmCount);
			cStmtObject.setString(1,strPolicyNumber);
			cStmtObject.setString(2,strVidalId);
			cStmtObject.registerOutParameter(3,Types.VARCHAR);
			cStmtObject.setString(4,strIMEINumber);
			cStmtObject.execute();

			
			strResult =(cStmtObject.getString(3)!=null)?(String)cStmtObject.getString(3):"No Data Found";
			log.info("INSIDE PREAUTN/CLAIMS COUNT DAO END");
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "webservice");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
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
					log.error("Error while closing the Statement in WebServiceDAOImpl feedBackService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl feedBackService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strResult;
	}//end of getPreAuthClaimsCount(String strVidalId, String strPolicyNumber,String strIMEINumber)


	  
	    
	  //--------------------------------------------------------Mobile App Procedure For Cliaim Search--------------------------------------------------------  
	    
	      @SuppressWarnings("unused")
		public ArrayList getSearchClaim(String strPolicyGroupSeqId) throws TTKException {
	    	ResultSet rs =null;
	    	ArrayList<Object> alSearchList = new ArrayList<Object>();
			Connection conn = null;	
			CallableStatement cStmtObject=null;
			//Calendar calendar = Calendar.getInstance();
			//StringBuffer sb=null;

			try {
				conn = ResourceManager.getConnection();
				//cStmtObject =(OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strClaimSearch);
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimSearch);
				cStmtObject.setString(1,strPolicyGroupSeqId);
				cStmtObject.setString(2,"1");			
				cStmtObject.setString(3,"100");
				cStmtObject.setString(5,"mem_name");
				cStmtObject.setString(6,"ASC");
			  	cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(4);
				if(rs != null){
	        	while(rs.next()){
				VidalClaimWireRestVO vidalClaimWireRestVO=new VidalClaimWireRestVO();
			    vidalClaimWireRestVO.setPreauth_int_seq_Id(TTKCommon.checkNull(rs.getString("pat_intimation_seq_id")));
				vidalClaimWireRestVO.setPreauth_int_Id(TTKCommon.checkNull(rs.getString("pat_intimation_id")));
				vidalClaimWireRestVO.setTpaEnrol_Id(TTKCommon.checkNull(rs.getString("tpa_enrollment_id")));
				vidalClaimWireRestVO.setMem_name(TTKCommon.checkNull(rs.getString("mem_name")));
				vidalClaimWireRestVO.setGender(TTKCommon.checkNull(rs.getString("gender")));
				vidalClaimWireRestVO.setIntimationGenDate(TTKCommon.checkNull(rs.getString("intimation_generated_date")));
				vidalClaimWireRestVO.setPolicyGroupSeqID(TTKCommon.checkNull(rs.getString("policy_group_seq_id")));
				vidalClaimWireRestVO.setMemberSeqId(TTKCommon.checkNull(rs.getString("member_seq_id")));
				vidalClaimWireRestVO.setStatusGenralId(TTKCommon.checkNull(rs.getString("status_general_type_id")));
				vidalClaimWireRestVO.setStatusDesc(TTKCommon.checkNull(rs.getString("status_desc")));
				vidalClaimWireRestVO.setHosName(TTKCommon.checkNull(rs.getString("hospital_name")));
				vidalClaimWireRestVO.setLikelyDoH(TTKCommon.checkNull(rs.getString("likely_date_of_hospitalisation")));
				vidalClaimWireRestVO.setPhyName(TTKCommon.checkNull(rs.getString("phy_name")));
				vidalClaimWireRestVO.setPhyPhNo(TTKCommon.checkNull(rs.getString("phy_ph_no")));
				vidalClaimWireRestVO.setStatusTypeId(TTKCommon.checkNull(rs.getString("state_type_id")));
				vidalClaimWireRestVO.setCityTypeId(TTKCommon.checkNull(rs.getString("city_type_id")));
				vidalClaimWireRestVO.setAilDesc(TTKCommon.checkNull(rs.getString("ailment_description")));
				alSearchList.add(vidalClaimWireRestVO);
			    }
	        	}
				return alSearchList;
			}//end of try
	       catch (SQLException sqlExp)
		   {
			throw new TTKException(sqlExp, "webservice");
		    }//end of catch (SQLException sqlExp)
		 catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			// Nested Try Catch to ensure resource closure 
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end o
	  
	//--------------------------------------------------------Mobile App Procedure For Support Module--------------------------------------------------------  
	   
	      
	    //--------------------------------------------------------Mobile App Procedure For Cliaim Search--------------------------------------------------------  
	      
	      @SuppressWarnings("unused")
		public ArrayList getEcardSearch(String strPolicyGroupSeqId) throws TTKException {
	    	ResultSet rs =null;
	    	ArrayList<Object> alSearchList = new ArrayList<Object>();
			Connection conn = null;	
			CallableStatement cStmtObject=null;
			//Calendar calendar = Calendar.getInstance();
			//StringBuffer sb=null;

			try {
				conn = ResourceManager.getConnection();
				//cStmtObject =(OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strClaimSearch);
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strEcardSearch);
				//System.out.println("1:::::"+strPolicyGroupSeqId);
				cStmtObject.setString(1,strPolicyGroupSeqId);
			  	cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
				if(rs != null){
	        	while(rs.next()){
				VidalClaimWireRestVO vidalClaimWireRestVO=new VidalClaimWireRestVO();
			    vidalClaimWireRestVO.setMemberSeqId(TTKCommon.checkNull(rs.getString("MEMBER_SEQ_ID")));
				vidalClaimWireRestVO.setMem_name(TTKCommon.checkNull(rs.getString("MEM_NAME")));
				alSearchList.add(vidalClaimWireRestVO);
			    }
	        	}
				return alSearchList;
			}//end of try
	       catch (SQLException sqlExp)
		   {
			throw new TTKException(sqlExp, "webservice");
		    }//end of catch (SQLException sqlExp)
		 catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			// Nested Try Catch to ensure resource closure 
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end o
	      
	      
	  //--------------------------------------------------Mobile App for Ecard_pritimation----------------------------------------------    
	      
	      public ArrayList getEcardPrint(String strPolicyGroupSeqId,String strMemberSeqId) throws TTKException {
	      	int intResult = 0;
	  		ArrayList<Object> alSearchList = new ArrayList<Object>();
	  		Connection conn = null;	
	  		CallableStatement cStmtObject=null;
	  		ResultSet rs=null;
	  		//Calendar calendar = Calendar.getInstance();
	  		//StringBuffer sb=null;

	  		try {
	  			conn = ResourceManager.getConnection();
	  			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strEcardPrint);
	  		//cStmtObject = conn.prepareCall(strDeleteIntimation);
	  			cStmtObject.setString(1,strPolicyGroupSeqId);
	  			cStmtObject.setString(2,"|"+strMemberSeqId+"|");
	  			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);//ROWS_PROCESSED
	  			cStmtObject.execute();
	  			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
				if(rs != null){
	        	while(rs.next()){
				VidalClaimWireRestVO vidalClaimWireRestVO=new VidalClaimWireRestVO();
			    vidalClaimWireRestVO.setMemAge(TTKCommon.checkNull(rs.getString("AGE")));
				vidalClaimWireRestVO.setGender(TTKCommon.checkNull(rs.getString("GENDER")));
				vidalClaimWireRestVO.setTollFreeNo(TTKCommon.checkNull(rs.getString("TOLL_FREE_NUM")));
				vidalClaimWireRestVO.setInsurance_Id(TTKCommon.checkNull(rs.getString("INSURANCE_ID")));
				vidalClaimWireRestVO.setPolicyNo(TTKCommon.checkNull(rs.getString("POLICY_NUM")));
				vidalClaimWireRestVO.setCerficateNo(TTKCommon.checkNull(rs.getString("CERTIFICATE_NUM")));
				vidalClaimWireRestVO.setValidFrom(TTKCommon.checkNull(rs.getString("VALID_FROM")));
				vidalClaimWireRestVO.setValidUpto(TTKCommon.checkNull(rs.getString("VALID_UPTO")));
				vidalClaimWireRestVO.setCompanyCode(TTKCommon.checkNull(rs.getString("COMPANY_CODE")));
				vidalClaimWireRestVO.setDOB(TTKCommon.checkNull(rs.getString("DOB")));
				vidalClaimWireRestVO.setDOJ(TTKCommon.checkNull(rs.getString("DOJ")));
				vidalClaimWireRestVO.setImage(TTKCommon.checkNull(rs.getString("IMAGE")));
				vidalClaimWireRestVO.setCompanyName(TTKCommon.checkNull(rs.getString("COMPANY_NAME")));
				vidalClaimWireRestVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("ENROLLMENT_ID")));
				vidalClaimWireRestVO.setPolicyHolderName(TTKCommon.checkNull(rs.getString("POLICY_HOLDER_NAME")));
				vidalClaimWireRestVO.setRelationShip(TTKCommon.checkNull(rs.getString("RELATIONSHIP")));
				vidalClaimWireRestVO.setBankAccNo(TTKCommon.checkNull(rs.getString("BANK_ACCT_NUM")));
				vidalClaimWireRestVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS1")));
				vidalClaimWireRestVO.setProductName(TTKCommon.checkNull(rs.getString("PRODUCT_NAME")));
				vidalClaimWireRestVO.setRemarkd(TTKCommon.checkNull(rs.getString("REMARKS")));
				vidalClaimWireRestVO.setMem_name(TTKCommon.checkNull(rs.getString("MEM_NAME")));
				vidalClaimWireRestVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
				vidalClaimWireRestVO.setMem_name(TTKCommon.checkNull(rs.getString("VFRM")));
				vidalClaimWireRestVO.setMem_name(TTKCommon.checkNull(rs.getString("VUPT")));
				alSearchList.add(vidalClaimWireRestVO);
			    }
	        	}
				return alSearchList;
	  		}//end of try
	         catch (SQLException sqlExp)
	  	   {
	  		throw new TTKException(sqlExp, "webservice");
	  	    }//end of catch (SQLException sqlExp)
	  	 catch (Exception exp)
	  	{
	  		throw new TTKException(exp, "webservice");
	  	}//end of catch (Exception exp)

	  	finally
	  	{
	  		// Nested Try Catch to ensure resource closure 
	  		try // First try closing the Statement
	  		{
	  			try
	  			{
	  				if (rs != null) rs.close();
	  				if (cStmtObject != null) cStmtObject.close();
	  			}//end of try
	  			catch (SQLException sqlExp)
	  			{
	  				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	  				throw new TTKException(sqlExp, "webservice");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  			{
	  				try
	  				{
	  					if(conn != null) conn.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	  					throw new TTKException(sqlExp, "webservice");
	  				}//end of catch (SQLException sqlExp)
	  			}//end of finally Connection Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "webservice");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			cStmtObject = null;
	  			conn = null;
	  		}//end of finally
	  	}//end of finally
	  }//end o
	      
	      
	         
	      //--------------------------------------------------------Mobile App Procedure For Cliaim Search--------------------------------------------------------  
	      
	      @SuppressWarnings("unused")
		public ArrayList getCoveragePlan(String strPolicySeqId) throws TTKException {
	    	ResultSet rs =null;
	    	ArrayList<Object> alSearchList = new ArrayList<Object>();
			Connection conn = null;	
			CallableStatement cStmtObject=null;
			//Calendar calendar = Calendar.getInstance();
			//StringBuffer sb=null;

			try {
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCoverageDetails);
				cStmtObject.setString(1,strPolicySeqId);
			  	cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
				if(rs != null){
	        	while(rs.next()){
				VidalClaimWireRestVO vidalClaimWireRestVO=new VidalClaimWireRestVO();
			    vidalClaimWireRestVO.setPolicySeqID(TTKCommon.checkNull(rs.getString("POLICY_SEQ_ID")));
				vidalClaimWireRestVO.setBenefit(TTKCommon.checkNull(rs.getString("BENEFIT")));
				vidalClaimWireRestVO.setProxy_Exclusion(TTKCommon.checkNull(rs.getString("PROXY_EXCLUSION")));
				vidalClaimWireRestVO.setNon_Medical(TTKCommon.checkNull(rs.getString("NON_MEDICAL")));
				vidalClaimWireRestVO.setAdded_By(TTKCommon.checkNull(rs.getString("ADDED_BY")));
				vidalClaimWireRestVO.setAdded_Date(TTKCommon.checkNull(rs.getString("ADDED_DATE")));
				vidalClaimWireRestVO.setUpdatedBy(TTKCommon.checkNull(rs.getString("UPDATED_BY")));
				vidalClaimWireRestVO.setUpdatedDate(TTKCommon.checkNull(rs.getString("UPDATED_DATE")));
				alSearchList.add(vidalClaimWireRestVO);
			    }
	        	}
				return alSearchList;
			}//end of try
	       catch (SQLException sqlExp)
		   {
			throw new TTKException(sqlExp, "webservice");
		    }//end of catch (SQLException sqlExp)
		 catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			// Nested Try Catch to ensure resource closure 
			try // First try closing the Statement
			{
				try
				{
					if (rs != null) rs.close();
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end o
	      
	      
	      
	      
	   //--------------------------------------------------------Mobile App Procedure For Cliaim Search--------------------------------------------------------  
	      
	      @SuppressWarnings("unused")
		public ArrayList getCoveragePlanDetails(String strUserId) throws TTKException {
	    	ResultSet rs =null;
	    	ArrayList<Object> alSearchList = new ArrayList<Object>();
			Connection conn = null;	
			CallableStatement cStmtObject=null;
			//Calendar calendar = Calendar.getInstance();
			//StringBuffer sb=null;

			try {
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCoverageDetailsPlan);
				cStmtObject.setString(1,strUserId);
			  	cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
				if(rs != null){
	        	while(rs.next()){
				VidalClaimWireRestVO vidalClaimWireRestVO=new VidalClaimWireRestVO();
			    vidalClaimWireRestVO.setPolicySeqID(TTKCommon.checkNull(rs.getString("POLICY_SEQ_ID")));
				vidalClaimWireRestVO.setPolicyNo(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
				vidalClaimWireRestVO.setPolicyGroupSeqID(TTKCommon.checkNull(rs.getString("POLICY_GROUP_SEQ_ID")));
				vidalClaimWireRestVO.setLevel(TTKCommon.checkNull(rs.getString("LEVEL")));
				alSearchList.add(vidalClaimWireRestVO);
			    }
	        	}
				return alSearchList;
			}//end of try
	       catch (SQLException sqlExp)
		   {
			throw new TTKException(sqlExp, "webservice");
		    }//end of catch (SQLException sqlExp)
		 catch (Exception exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (Exception exp)

		finally
		{
			// Nested Try Catch to ensure resource closure 
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end o
	    
	      
	    
	//------------------------------------------Mobile App for SaveIntimation----------------------------------------------    
	    
	      public int saveMobileClaimIntimation(VidalClaimWireRestVO vidalClaimWireRestVO) throws TTKException {
	      	int intResult = 0;
	  		//ArrayList<Object> alResult = new ArrayList<Object>();
	  		Connection conn = null;	
	  		CallableStatement cStmtObject=null;
	  		//Calendar calendar = Calendar.getInstance();
	  		//StringBuffer sb=null;

	  		try {
	  			conn = ResourceManager.getConnection();
	  			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveAppClaim);
	  			if(vidalClaimWireRestVO.getCallLogSeqId() != null){
	  				cStmtObject.setLong(1,vidalClaimWireRestVO.getCallLogSeqId());
	  			}
	  			else {
	  				cStmtObject.setLong(1,0);
	  			}
	  			
	  			if(vidalClaimWireRestVO.getMemberSeqId() != null){
	  			cStmtObject.setString(2,vidalClaimWireRestVO.getMemberSeqId());	
	  			}
	  			else{
	  				cStmtObject.setLong(2,0);
	  			}
	  			if(vidalClaimWireRestVO.getGroupId() != null){
	  			cStmtObject.setString(3,vidalClaimWireRestVO.getGroupId());
	  			}
	  			else{
	  				cStmtObject.setString(3,null);
	  			}
	  			cStmtObject.setString(4,null);	
	  			cStmtObject.setString(5,null);	
	  			cStmtObject.setString(6,null);	
	  			cStmtObject.setString(7,null);
	  			if(vidalClaimWireRestVO.getDtDoa() != null){
	  				cStmtObject.setTimestamp(8,vidalClaimWireRestVO.getDtDoa()!=null ? new Timestamp(TTKCommon.getConvertStringToDate(vidalClaimWireRestVO.getDtDoa()).getTime()):null);
	  			}
	  			else{
	  				cStmtObject.setTimestamp(8, null);
	  			}
	  			cStmtObject.setTimestamp(9, null);
	  			if(vidalClaimWireRestVO.getAilDesc() != null){
	  			cStmtObject.setString(10,vidalClaimWireRestVO.getAilDesc());	
	  			}
	  			else{
	  				cStmtObject.setString(10,null);
	  			}
	  			if(vidalClaimWireRestVO.getHosName() != null){
	  			cStmtObject.setString(11,vidalClaimWireRestVO.getHosName());
	  			}
	  			else{
	  				cStmtObject.setString(11,null);
	  			}
	  			cStmtObject.setString(12,null);
	  			cStmtObject.setString(13,"Y");
	  			cStmtObject.setString(14,null);
	  			cStmtObject.setLong(15,1);
	  			cStmtObject.setLong(16,0);
	  			if(vidalClaimWireRestVO.getPatientName() != null){
	  			cStmtObject.setString(17,vidalClaimWireRestVO.getPatientName());
	  			}
	  			else{
	  				cStmtObject.setString(17,null);
	  			}
	  			cStmtObject.setString(18,"CSO");
	  			if(vidalClaimWireRestVO.getPhyName() != null){
	  			cStmtObject.setString(19,vidalClaimWireRestVO.getPhyName());
	  			}
	  			else{
	  				cStmtObject.setString(19,null);
	  			}
	  			if(vidalClaimWireRestVO.getPhyPhNo() != null){
	  			cStmtObject.setString(20,vidalClaimWireRestVO.getPhyPhNo());	
	  			}
	  			else{
	  				cStmtObject.setString(20,null);
	  			}
	  			if(vidalClaimWireRestVO.getStateTypeId() != null){
	  			cStmtObject.setString(21,vidalClaimWireRestVO.getStateTypeId());
	  			}
	  			else{
	  				cStmtObject.setString(21,null);
	  			}
	  			if(vidalClaimWireRestVO.getCityTypeId() != null){
	  			cStmtObject.setString(22,vidalClaimWireRestVO.getCityTypeId());	
	  			}
	  			else{
	  				cStmtObject.setString(22,null);
	  			}
	  		    cStmtObject.registerOutParameter(1,Types.BIGINT);
	  		  	cStmtObject.registerOutParameter(16,Types.INTEGER);
	  			cStmtObject.execute();
	  			intResult = cStmtObject.getInt(16);
	  		}//end of try
	         catch (SQLException sqlExp)
	  	   {
	  		throw new TTKException(sqlExp, "webservice");
	  	    }//end of catch (SQLException sqlExp)
	  	 catch (Exception exp)
	  	{
	  		throw new TTKException(exp, "webservice");
	  	}//end of catch (Exception exp)

	  	finally
	  	{
	  		// Nested Try Catch to ensure resource closure 
	  		try // First try closing the Statement
	  		{
	  			try
	  			{
	  				if (cStmtObject != null) cStmtObject.close();
	  			}//end of try
	  			catch (SQLException sqlExp)
	  			{
	  				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	  				throw new TTKException(sqlExp, "webservice");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  			{
	  				try
	  				{
	  					if(conn != null) conn.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	  					throw new TTKException(sqlExp, "webservice");
	  				}//end of catch (SQLException sqlExp)
	  			}//end of finally Connection Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "webservice");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			cStmtObject = null;
	  			conn = null;
	  		}//end of finally
	  	}//end of finally
	  	return intResult;
	  }//end o
	      
	    //------------------------------------------Mobile App for SaveIntimation----------------------------------------------    
	      
	      public int saveMobileFeedback(VidalClaimWireRestVO vidalClaimWireRestVO,String strAnswerList) throws TTKException {
	      	int intResult = 0;
	  		//ArrayList<Object> alResult = new ArrayList<Object>();
	  		Connection conn = null;	
	  		CallableStatement cStmtObject=null;
	  		//Calendar calendar = Calendar.getInstance();
	  		//StringBuffer sb=null;

	  		try {
	  			conn = ResourceManager.getConnection();
	  			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveFeedback);
	  			cStmtObject.setString(1,"0"); 			
	  			if(vidalClaimWireRestVO.getPolicyGroupSeqID() != null){
	  			cStmtObject.setString(2,vidalClaimWireRestVO.getPolicyGroupSeqID());	
	  			}
	  			else{
	  				cStmtObject.setLong(2,0);
	  			}
	  			if(vidalClaimWireRestVO.getSelfFeedback() != null){
	  			cStmtObject.setString(3,vidalClaimWireRestVO.getSelfFeedback());
	  			}
	  			else{
	  				cStmtObject.setString(3,null);
	  			}
	  			cStmtObject.setString(4,strAnswerList);
	  			
	  			if(vidalClaimWireRestVO.getNotification() != null){
	  			cStmtObject.setString(5,vidalClaimWireRestVO.getNotification());	
	  			}
	  			else{
	  				cStmtObject.setString(5,null);
	  			}
	  			cStmtObject.setString(6,null);	
	  		    cStmtObject.registerOutParameter(7,Types.BIGINT);
	  			cStmtObject.execute();
	  			intResult = cStmtObject.getInt(7);
	  		}//end of try
	         catch (SQLException sqlExp)
	  	   {
	  		throw new TTKException(sqlExp, "webservice");
	  	    }//end of catch (SQLException sqlExp)
	  	 catch (Exception exp)
	  	{
	  		throw new TTKException(exp, "webservice");
	  	}//end of catch (Exception exp)

	  	finally
	  	{
	  		// Nested Try Catch to ensure resource closure 
	  		try // First try closing the Statement
	  		{
	  			try
	  			{
	  				if (cStmtObject != null) cStmtObject.close();
	  			}//end of try
	  			catch (SQLException sqlExp)
	  			{
	  				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	  				throw new TTKException(sqlExp, "webservice");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  			{
	  				try
	  				{
	  					if(conn != null) conn.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	  					throw new TTKException(sqlExp, "webservice");
	  				}//end of catch (SQLException sqlExp)
	  			}//end of finally Connection Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "webservice");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			cStmtObject = null;
	  			conn = null;
	  		}//end of finally
	  	}//end of finally
	  	return intResult;
	  }//end o
	      
	//------------------------------------------Mobile App for SaveIntimation----------------------------------------------    
	      
	      public int MobileMailSend(long HospitalSeqId,String strUserMailId) throws TTKException {
	      	int intResult = 0;
	  		//ArrayList<Object> alResult = new ArrayList<Object>();
	  		Connection conn = null;	
	  		CallableStatement cStmtObject=null;
	  		//Calendar calendar = Calendar.getInstance();
	  		//StringBuffer sb=null;

	  		try {
	  			conn = ResourceManager.getConnection();
	  			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMobileSendMail);
	  			cStmtObject.setString(1,"HOSPITAL_SEARCH_PORTAL");//MailTemplate
				cStmtObject.setLong(2,HospitalSeqId);//hosp seqId
				cStmtObject.setLong(3,1);//added by
				cStmtObject.registerOutParameter(4, OracleTypes.INTEGER);//out parameter
				cStmtObject.setString(5,strUserMailId);//location
				cStmtObject.setString(6,null);
				cStmtObject.setString(7,null);
				cStmtObject.execute();
	  			intResult = cStmtObject.getInt(4);
	  		}//end of try
	         catch (SQLException sqlExp)
	  	   {
	  		throw new TTKException(sqlExp, "webservice");
	  	    }//end of catch (SQLException sqlExp)
	  	 catch (Exception exp)
	  	{
	  		throw new TTKException(exp, "webservice");
	  	}//end of catch (Exception exp)

	  	finally
	  	{
	  		// Nested Try Catch to ensure resource closure 
	  		try // First try closing the Statement
	  		{
	  			try
	  			{
	  				if (cStmtObject != null) cStmtObject.close();
	  			}//end of try
	  			catch (SQLException sqlExp)
	  			{
	  				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	  				throw new TTKException(sqlExp, "webservice");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  			{
	  				try
	  				{
	  					if(conn != null) conn.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	  					throw new TTKException(sqlExp, "webservice");
	  				}//end of catch (SQLException sqlExp)
	  			}//end of finally Connection Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "webservice");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			cStmtObject = null;
	  			conn = null;
	  		}//end of finally
	  	}//end of finally
	  	return intResult;
	  }//end o
	     
	         
	    //--------------------------------------------------Mobile App for DeleteIntimation----------------------------------------------    
	      
	      public int DeleteAppIntimation(String strType,String strInimationSeqId,long strInimationHosSeqId) throws TTKException {
	      	int intResult = 0;
	  		//ArrayList<Object> alResult = new ArrayList<Object>();
	  		Connection conn = null;	
	  		CallableStatement cStmtObject=null;
	  		
	  		//Calendar calendar = Calendar.getInstance();
	  		//StringBuffer sb=null;

	  		try {
	  			conn = ResourceManager.getConnection();
	  			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteAppClaim);
	  		//cStmtObject = conn.prepareCall(strDeleteIntimation);
	  			cStmtObject.setString(1, "CLM");//FLAG - PAT-Preauth/CLM-Claims/HOS - Cancel Hospital
	  			cStmtObject.setString(2,"|"+strInimationSeqId+"|");
	  			cStmtObject.setLong(3,strInimationHosSeqId);//hosp_int_seq_id  (Long)alDeleteList.get(2)
	  			cStmtObject.registerOutParameter(4, Types.INTEGER);//ROWS_PROCESSED
	  			cStmtObject.execute();
	  			intResult = cStmtObject.getInt(4);
	  		}//end of try
	         catch (SQLException sqlExp)
	  	   {
	  		throw new TTKException(sqlExp, "webservice");
	  	    }//end of catch (SQLException sqlExp)
	  	 catch (Exception exp)
	  	{
	  		throw new TTKException(exp, "webservice");
	  	}//end of catch (Exception exp)

	  	finally
	  	{
	  		// Nested Try Catch to ensure resource closure 
	  		try // First try closing the Statement
	  		{
	  			try
	  			{
	  				if (cStmtObject != null) cStmtObject.close();
	  			}//end of try
	  			catch (SQLException sqlExp)
	  			{
	  				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	  				throw new TTKException(sqlExp, "webservice");
	  			}//end of catch (SQLException sqlExp)
	  			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	  			{
	  				try
	  				{
	  					if(conn != null) conn.close();
	  				}//end of try
	  				catch (SQLException sqlExp)
	  				{
	  					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	  					throw new TTKException(sqlExp, "webservice");
	  				}//end of catch (SQLException sqlExp)
	  			}//end of finally Connection Close
	  		}//end of try
	  		catch (TTKException exp)
	  		{
	  			throw new TTKException(exp, "webservice");
	  		}//end of catch (TTKException exp)
	  		finally // Control will reach here in anycase set null to the objects
	  		{
	  			cStmtObject = null;
	  			conn = null;
	  		}//end of finally
	  	}//end of finally
	  	return intResult;
	  }//end o
	         
	    
	  //-------------------------------------------Mobile App for Select ClaimList-----------------------------------------------------------    
	      
	      public ArrayList SelectAppClaim(long PolicyGroupSeqId) throws TTKException {
	    		ArrayList<Object> alResultList = new ArrayList<Object>();
	    		Connection conn = null;	
	    		CallableStatement cStmtObject=null;
	    		ResultSet rs =null;
	    		VidalClaimWireRestVO vidalClaimWireRestVO=null;
	    		//Calendar calendar = Calendar.getInstance();
	    		//StringBuffer sb=null;

	    		try {
	    			conn = ResourceManager.getConnection();
	    			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectAppClaim);
	    		//cStmtObject = conn.prepareCall(strDeleteIntimation);
	    			cStmtObject.setLong(1,PolicyGroupSeqId);//FLAG - PAT-Preauth/CLM-Claims/HOS - Cancel Hospital
	    			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	    			cStmtObject.setString(3,"DATE_OF_ADMISSION");//hosp_int_seq_id  (Long)alDeleteList.get(2)
	    			cStmtObject.setString(4,"DESC");
	    			cStmtObject.setString(5,"1");			
	    			cStmtObject.setString(6,"11");
	    			cStmtObject.execute();
	    			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
	    			if(rs != null){
	    				while(rs.next()){
	    					vidalClaimWireRestVO=new VidalClaimWireRestVO();

	    					if(rs.getString("CLAIMS_INWARD_SEQ_ID") != null){
	    						vidalClaimWireRestVO.setInwardSeqID(new Long(rs.getLong("CLAIMS_INWARD_SEQ_ID")));
	    					}//end of if(rs.getString("CLAIMS_INWARD_SEQ_ID") != null)

	    					vidalClaimWireRestVO.setInwardNbr(TTKCommon.checkNull(rs.getString("CLAIMS_INWARD_NO")));

	    					if(rs.getString("RCVD_DATE") != null){
	    						//claimInwardVO.setReceivedDate(new Date(rs.getTimestamp("RCVD_DATE").getTime()));
	    					}//end of if(rs.getString("RCVD_DATE") != null)
	    					if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null){
	    						vidalClaimWireRestVO.setClmEnrollDtlSeqID(new Long(rs.getLong("CLM_ENROLL_DETAIL_SEQ_ID")));
	    					}//end of if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null)
	    					vidalClaimWireRestVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
	    					vidalClaimWireRestVO.setClaimantName(TTKCommon.checkNull(rs.getString("CLAIMANT_NAME")));
	    					vidalClaimWireRestVO.setGroupName(TTKCommon.checkNull(rs.getString("claim_number")));
	    					vidalClaimWireRestVO.setClaimTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
	    					vidalClaimWireRestVO.setClaimTypeID(TTKCommon.checkNull(rs.getString("claim_general_type_id")));
	    					vidalClaimWireRestVO.setDocumentTypeID(TTKCommon.checkNull(rs.getString("claim_general_type_id")));
	    					vidalClaimWireRestVO.setClaimStatus(TTKCommon.checkNull(rs.getString("clm_status")));
	    					vidalClaimWireRestVO.setOfflineFlagYN(TTKCommon.checkNull(rs.getString("offline_flag")));
	    					vidalClaimWireRestVO.setMemberSeqId(rs.getString("member_seq_id"));
	    					vidalClaimWireRestVO.setClaimSeqID(rs.getString("claim_seq_id"));
	    					vidalClaimWireRestVO.setHosName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
	    					vidalClaimWireRestVO.setTotalApprovedAmount(TTKCommon.checkNull(rs.getString("TOTAL_APP_AMOUNT")));
	    					vidalClaimWireRestVO.setShortfallPl(TTKCommon.checkNull(rs.getString("SHORTFALL")));
	    					vidalClaimWireRestVO.setAmmdtPl(TTKCommon.checkNull(rs.getString("AMMDT")));
	    					vidalClaimWireRestVO.setStatusPl(TTKCommon.checkNull(rs.getString("status")));
	    					vidalClaimWireRestVO.setSettlementNo(TTKCommon.checkNull(rs.getString("CLAIM_SETTLEMENT_NUMBER")));
	    					//if(rs.getTimestamp("DATE_OF_ADMISSION") != null)vidalClaimWireRestVO.get(TTKCommon.getConvertDateToString(new Date(rs.getTimestamp("DATE_OF_ADMISSION").getTime())));
	    					//if(rs.getTimestamp("DATE_OF_DISCHARGE") != null)vidalClaimWireRestVOvidalClaimWireRestVO.setDtReceivedDate(TTKCommon.getConvertDateToString(new Date(rs.getTimestamp("DATE_OF_DISCHARGE").getTime())));
	    					//claimInwardVO.setDtReceivedDate("10-10-2015");

	    					alResultList.add(vidalClaimWireRestVO);
	    				}
	    			}//end else
	    			return alResultList;
	    		}//end of try
	           catch (SQLException sqlExp)
	    	   {
	    		throw new TTKException(sqlExp, "webservice");
	    	    }//end of catch (SQLException sqlExp)
	    	 catch (Exception exp)
	    	{
	    		throw new TTKException(exp, "webservice");
	    	}//end of catch (Exception exp)

	    	finally
	    	{
	    		// Nested Try Catch to ensure resource closure 
	    		try // First try closing the Statement
	    		{
	    			try
	    			{
	    				if (rs != null) rs.close();
	    				if (cStmtObject != null) cStmtObject.close();
	    			}//end of try
	    			catch (SQLException sqlExp)
	    			{
	    				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	    				throw new TTKException(sqlExp, "webservice");
	    			}//end of catch (SQLException sqlExp)
	    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	    			{
	    				try
	    				{
	    					if(conn != null) conn.close();
	    				}//end of try
	    				catch (SQLException sqlExp)
	    				{
	    					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	    					throw new TTKException(sqlExp, "webservice");
	    				}//end of catch (SQLException sqlExp)
	    			}//end of finally Connection Close
	    		}//end of try
	    		catch (TTKException exp)
	    		{
	    			throw new TTKException(exp, "webservice");
	    		}//end of catch (TTKException exp)
	    		finally // Control will reach here in anycase set null to the objects
	    		{
	    			rs = null;
	    			cStmtObject = null;
	    			conn = null;
	    		}//end of finally
	    	}//end of finally
	    	
	    }//end o
	      
	      
	  //-------------------------------------------Mobile App for Select ClaimSummaryList-----------------------------------------------------------    
	      
	      
	     
	      public ArrayList SelectAppClaimSummary(long PolicyGroupSeqId) throws TTKException {
	    	  ArrayList<CacheObject> alSummary = new ArrayList<CacheObject>();
	    		Connection conn = null;	
	    		PreparedStatement cStmtObject=null;
	    		ResultSet rs =null;
	    		VidalClaimWireRestVO vidalClaimWireRestVO=null;
	    		//Calendar calendar = Calendar.getInstance();
	    		//StringBuffer sb=null;
	    		CacheObject cacheObject = null;
	    		try {
	    			conn = ResourceManager.getConnection();
	    			cStmtObject = (PreparedStatement)conn.prepareStatement(strSelectAppSummaryClaim);
	    		//cStmtObject = conn.prepareCall(strDeleteIntimation);
	    			cStmtObject.setLong(1,PolicyGroupSeqId);//FLAG - PAT-Preauth/CLM-Claims/HOS - Cancel Hospital
	    			cStmtObject.execute();
	    			rs = cStmtObject.executeQuery();
	              	if(rs != null){
	              		while(rs.next()){
	              			cacheObject = new CacheObject();
	              			cacheObject.setCacheId(TTKCommon.checkNull(rs.getString("MEMBER_SEQ_ID")));
	                          cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("MEMBER_NAME")));
	                          alSummary.add(cacheObject);
	              		}
	              		
	          	
	    			}//end else
	            	return alSummary;
	    		}//end of try
	           catch (SQLException sqlExp)
	    	   {
	    		throw new TTKException(sqlExp, "webservice");
	    	    }//end of catch (SQLException sqlExp)
	    	 catch (Exception exp)
	    	{
	    		throw new TTKException(exp, "webservice");
	    	}//end of catch (Exception exp)

	    	finally
	    	{
	    		// Nested Try Catch to ensure resource closure 
	    		try // First try closing the Statement
	    		{
	    			try
	    			{
	    				if (rs != null) rs.close();
	    				if (cStmtObject != null) cStmtObject.close();
	    			}//end of try
	    			catch (SQLException sqlExp)
	    			{
	    				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	    				throw new TTKException(sqlExp, "webservice");
	    			}//end of catch (SQLException sqlExp)
	    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	    			{
	    				try
	    				{
	    					if(conn != null) conn.close();
	    				}//end of try
	    				catch (SQLException sqlExp)
	    				{
	    					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	    					throw new TTKException(sqlExp, "webservice");
	    				}//end of catch (SQLException sqlExp)
	    			}//end of finally Connection Close
	    		}//end of try
	    		catch (TTKException exp)
	    		{
	    			throw new TTKException(exp, "webservice");
	    		}//end of catch (TTKException exp)
	    		finally // Control will reach here in anycase set null to the objects
	    		{
	    			rs=null;
	    			cStmtObject = null;
	    			conn = null;
	    		}//end of finally
	    	}//end of finally
	    	
	    }//end o
	      
	      
	      //-------------------------------------------Mobile App for Select ClaimSummaryDetailList-----------------------------------------------------------    
	      public ArrayList getAppClaimHistoryList(Long MemberSeqId)throws TTKException
	      {
	          Connection conn = null;
	          OracleCallableStatement cStmtObject = null;
	          Document doc = null;
	          XMLType xml = null;
	          ArrayList<Object> alResultList = new ArrayList<Object>() ;
	          try
	          {
	                  conn = ResourceManager.getConnection();
	              	  cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strClaimAppHistoryList);
	                  cStmtObject.setLong(1,MemberSeqId);//MEM_SEQ_ID
	                  cStmtObject.setString(2,null);// TPA_ENROLLMENT_ID
	                  cStmtObject.registerOutParameter(3,OracleTypes.OPAQUE,"SYS.XMLTYPE");
	                  cStmtObject.execute();
	                  xml = XMLType.createXML(cStmtObject.getOPAQUE(3));
	            //end of else if(strHistoryType.equals("HCL"))

	              DOMReader domReader = new DOMReader();
	              doc = xml != null ? domReader.read(xml.getDOM()):null;
	              alResultList.add( XML.toJSONObject(doc.asXML()).toString());
	              return alResultList;
	          }//end of try
	          catch (SQLException sqlExp)
	          {
	              throw new TTKException(sqlExp, "webservice");
	          }//end of catch (SQLException sqlExp)
	          catch (Exception exp)
	          {
	              throw new TTKException(exp, "webservice");
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
	          			log.error("Error while closing the Statement in OnlineAccessDAOImpl getHistory()",sqlExp);
	          			throw new TTKException(sqlExp, "webservice");
	          		}//end of catch (SQLException sqlExp)
	          		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	          		{
	          			try
	          			{
	          				if(conn != null) conn.close();
	          			}//end of try
	          			catch (SQLException sqlExp)
	          			{
	          				log.error("Error while closing the Connection in OnlineAccessDAOImpl getHistory()",sqlExp);
	          				throw new TTKException(sqlExp, "webservice");
	          			}//end of catch (SQLException sqlExp)
	          		}//end of finally Connection Close
	          	}//end of try
	          	catch (TTKException exp)
	          	{
	          		throw new TTKException(exp, "webservice");
	          	}//end of catch (TTKException exp)
	          	finally // Control will reach here in anycase set null to the objects
	          	{
	          		cStmtObject = null;
	          		conn = null;
	          	}//end of finally
	  		}//end of finally
	      }//end of getAppClaimHistoryList(String strHistoryType, long lngSeqId, long lngEnrollDtlSeqId)
	      
	   
	      
	      
	
		
		  
	    public int getChangePwdService(String strUserName,String strOldPassword,String strNewPassword,String strEmail,String strMobileNo)throws TTKException {
			int strResult=0;
			//String strMessage="";
			Connection conn = null;
			CallableStatement cStmtObject=null;
			try {
				log.info("INSIDE LOGIN PAGE DAO START");
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strChangePwdLoginService);
				cStmtObject.setString(1,strUserName);
				cStmtObject.setString(2,strOldPassword);
				cStmtObject.setString(3,strNewPassword);
			
				cStmtObject.registerOutParameter(4,Types.INTEGER);
				cStmtObject.setString(5,strEmail);
				cStmtObject.setString(6,strMobileNo);
				cStmtObject.execute();
	            strResult=cStmtObject.getInt(4);	
				//System.out.println("strResult::"+strResult);
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "webservice");
			}//end of catch (Exception exp)

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
						log.error("Error while closing the Statement in WebServiceDAOImpl getChangePwdService",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in WebServiceDAOImpl getChangePwdService",sqlExp);
							throw new TTKException(sqlExp, "webservice");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "webservice");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
			return strResult;
	    }//end of getLoginForgetPwdService(String strUsername)


	
		
		public int saveMedReminder(OnlineIntimationVO onlineIntimationVO) throws TTKException
		{
		    int iResult = 0;
		    Connection conn = null;
		    CallableStatement cStmtObject=null;
		    try {
		    	conn = ResourceManager.getConnection();
		    	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveMedReminder);
		    		    	
		    	
				cStmtObject.setString(1,null);
		    	cStmtObject.setLong(2, onlineIntimationVO.getMemberSeqID());
		    	cStmtObject.setString(3, onlineIntimationVO.getDrugName());
		    	cStmtObject.setString(4, onlineIntimationVO.getQuantity());
		    	cStmtObject.setString(5, onlineIntimationVO.getTimesPerDay());
		    	cStmtObject.setString(6, onlineIntimationVO.getNoOfDays());
		    	cStmtObject.setString(7, onlineIntimationVO.getIntervalTypeId());
		    	cStmtObject.setString(8, onlineIntimationVO.getMedTimeTypeId());
		    	  SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");  
			    	Date date1=formatter1.parse(onlineIntimationVO.getMedStartDate());  

		  	    	if(date1 != null){
		             	cStmtObject.setTimestamp(9,new Timestamp(date1.getTime()));
		             }

		    	cStmtObject.registerOutParameter(10,Types.INTEGER);
		    	cStmtObject.execute();
		    	iResult = cStmtObject.getInt(10);
		    }//end of try
		    catch(SQLException sqlExp){
				throw new TTKException(sqlExp,null);
			}
			catch (Exception exp) {
				throw new TTKException(exp,null);
				}
				finally{  
					try{
						if(cStmtObject != null) cStmtObject.close();
						if(conn != null) conn.close();
					}catch(SQLException sqle){
						sqle.printStackTrace();
					}finally{
						cStmtObject=null;
						conn=null;
						
					}
				}
		        return iResult;
		 }

		/*
		 * A Common METHOD TI GET SEQ ID OF ANY ID
		 */
		 public Long getSeqIds(String empanelId,String getType) throws TTKException {
				
	 		 Connection conn = null;
	 		 PreparedStatement pStmtObject = null;
	 		 ResultSet rs = null;
	 		 Long hospSqId	=	null;
	 		 try
	 		 {
	 			conn = ResourceManager.getConnection();
	 			if("HOS".equals(getType)){
	 			pStmtObject = (PreparedStatement)conn.prepareStatement(strgetHospSeqId);
	 			}else if("USER".equals(getType)){
		 			pStmtObject = (PreparedStatement)conn.prepareStatement(strgetUserSeqId);
		 			}
				pStmtObject.setString(1, empanelId);
	 				rs = pStmtObject.executeQuery();
	 		
	 			if(rs != null)
	 			{
	 				 if(rs.next())
	 				 {
	 					hospSqId	=	rs.getLong(1);
					 }
	 			 }	
	 		 }
	 			catch (SQLException e) 
	 			{
	 				e.printStackTrace();
	 			}
	 			finally
	 			{
	 				try
	 				{
	 					if(rs != null)rs.close();
	 				} 
	 				catch (SQLException e1) 
	 				{
	 					e1.printStackTrace();
	 				}
	 				try
	 				{
	 					if(pStmtObject != null) pStmtObject.close();
	 				}catch(SQLException e)
	 				{
	 					e.printStackTrace();
	 				}try
	 				{
	 					if(conn != null) conn.close();
	 				}catch(SQLException e)
	 				{
	 					e.printStackTrace();
	 				}finally{
	 					rs = null;
	 					pStmtObject = null;
	 					conn = null;
	 				}	
	 			}
	 		 return hospSqId; 
	 }

	
		 /**
			 * This method returns the Arraylist of CommunicationOptionVO's  which contains list of notification records which will take care by Schedular
			 * @return ArrayList of CommunicationOptionVO object which contains notification details
			 * @exception throws TTKException
			 */
			public ArrayList getSendMailList(String strMsgType) throws TTKException {
				Connection conn = null;
				CommunicationOptionVO cOptionVO = null;
				CallableStatement cStmtObject = null;
				ResultSet rs = null;String FINAL_MESSAGE = "";
				ArrayList<Object> alMailList = new ArrayList<Object>();
				try{
					conn = ResourceManager.getConnection();
					cStmtObject=conn.prepareCall(strsendSMSMailImmediate);
					cStmtObject.setString(1,strMsgType);
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.execute();
					rs = (java.sql.ResultSet)cStmtObject.getObject(2);
					if(rs != null){
						while(rs.next()){
							cOptionVO = new CommunicationOptionVO();
							cOptionVO.setRcptSeqID(TTKCommon.checkNull(rs.getString("DEST_MSG_RCPT_SEQ_ID")));
							cOptionVO.setSentFrom(TTKCommon.checkNull(rs.getString("SENDER_MAIL_ID")));
							cOptionVO.setMsgTitle(TTKCommon.checkNull(rs.getString("MESSAGE_TITLE")));
							//cOptionVO.setMessage(TTKCommon.checkNull(rs.getString("MESSAGE")));
							cOptionVO.setEnrollNum(TTKCommon.checkNull(rs.getString("ENROLLMENT_NUMBER")));
							cOptionVO.setMsgType(TTKCommon.checkNull(rs.getString("MSG_TYPE")));
							cOptionVO.setPrsntYN(TTKCommon.checkNull(rs.getString("PRESENT_YN")));
							cOptionVO.setPrmRcptEmailList(TTKCommon.checkNull(rs.getString("PRM_RCPT_EMAIL_LIST")));
							cOptionVO.setRcptFaxNos(TTKCommon.checkNull(rs.getString("PRM_RCPT_FAX_NOS")));
							cOptionVO.setRcptSMS(TTKCommon.checkNull(rs.getString("PRM_RCPT_SMS")));
							cOptionVO.setSenRcptEmailList(TTKCommon.checkNull(rs.getString("SEC_RCPT_EMAIL_LIST")));
							cOptionVO.setFilePathName(TTKCommon.checkNull(rs.getString("FILE_PATH_NAME")));
							//added for Mail-SMS Template for Cigna

							String MESSAGE1 = TTKCommon.checkNull(rs.getString("MESSAGE"));
							//log.info("MESSAGE1--:"+MESSAGE1);
							String MESSAGE2  = TTKCommon.checkNull(rs.getString("CIGNA_MSG"));					
							//log.info("MESSAGE2--:"+MESSAGE2);
							if(rs.getString("MSG_TYPE")=="SMS")
							{
								FINAL_MESSAGE = MESSAGE1;
							}
							else
							{
								FINAL_MESSAGE = MESSAGE1+MESSAGE2;
							}
							 
							//log.info("FINAL_MESSAGE--:"+FINAL_MESSAGE);

							cOptionVO.setMessage(FINAL_MESSAGE);

							//cOptionVO.setMsgID(MSG_ID);
						
							cOptionVO.setMsgID(TTKCommon.checkNull(rs.getString("MSG_ID")));
							//added for Mail-SMS Template for Cigna
							cOptionVO.setCignaSmsUrl(TTKCommon.checkNull(rs.getString("CIGNA_SMS_YN")));
									log.info("DEST_MSG_RCPT_SEQ_ID is :"+cOptionVO.getRcptSeqID());
							log.info("getSendMailList MSG_SENT_FROM is 		  :"+cOptionVO.getSentFrom());
							log.info("getSendMailList MESSAGE_TITLE is 		  :"+cOptionVO.getMsgTitle());
							log.info("getSendMailList MESSAGE is 		  	  :"+cOptionVO.getMessage());
							log.info("getSendMailList ENROLLMENT_NUMBER is 	  :"+cOptionVO.getEnrollNum());
							log.info("getSendMailList MSG_TYPE is 		  	  :"+cOptionVO.getMsgType());
							log.info("getSendMailList PRESENT_YN is 		  :"+cOptionVO.getPrsntYN());
							log.info("getSendMailList PRM_RCPT_EMAIL_LIST is  :"+cOptionVO.getPrmRcptEmailList());
							log.info("getSendMailList PRM_RCPT_FAX_NOS is     :"+cOptionVO.getRcptFaxNos());
							log.info("getSendMailList PRM_RCPT_SMS is 		  :"+cOptionVO.getRcptSMS());
							log.info("getSendMailList SEC_RCPT_EMAIL_LIST is  :"+cOptionVO.getSenRcptEmailList());
							log.info("getSendMailList FILE_PATH_NAME is 	  :"+cOptionVO.getFilePathName());

							alMailList.add(cOptionVO);
						}//end of while(rs.next())
					}//end of if(rs != null)
					return alMailList;
				}//end of try
				catch (SQLException sqlExp)
				{
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp)
				{
					throw new TTKException(exp, "webservice");
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
							log.error("Error while closing the Resultset in WebServiceDAOImpl getSendMailList()",sqlExp);
							throw new TTKException(sqlExp, "webservice");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (cStmtObject != null) cStmtObject.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in WebServiceDAOImpl getSendMailList()",sqlExp);
								throw new TTKException(sqlExp, "webservice");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in WebServiceDAOImpl getSendMailList()",sqlExp);
									throw new TTKException(sqlExp, "webservice");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "webservice");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally
			}//end of getSendMailList(String strMsgType)


		public byte[] getPolicyTOBpdf(Long ocoPolicyGrpseqid) throws TTKException {

			BLOB policyTOBblob = null;
			byte[] data=null;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			
			
			try {
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strpolicyTOBdownload);
				cStmtObject.setLong(1,ocoPolicyGrpseqid);
				cStmtObject.registerOutParameter(2,Types.BLOB);
				cStmtObject.execute();
			    policyTOBblob = (BLOB) cStmtObject.getBlob(2);
           
			if(policyTOBblob!=null){
				log.info("Policy PDF  found");
				
				int length = (int) policyTOBblob.length();
				data=policyTOBblob.getBytes(1,length);
			}  
			else{
				data=new String("").getBytes();
				log.info("Policy PDF not found");
				}	
			
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "webservice");
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
						log.error("Error while closing the Statement in WebServiceDAOImpl dailyTipService()",sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in WebServiceDAOImpl dailyTipService()",sqlExp);
							throw new TTKException(sqlExp, "webservice");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "webservice");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
			return data;
		
		}
		
		/* This method method stores the bank search response coming from Vidal wire rest as a String  */
		public ArrayList getbanksearchData(String ocomodule,String ocobankdestination, String ocobankcity,String ocobankarea, String ocobankbranch) throws TTKException
		{		
				 Connection conn = null;
				 PreparedStatement pStmtObject = null;
				 ResultSet rs = null;
				 ArrayList<Object> alSearchList = new ArrayList<Object>();
				 	ArrayList<Object> aldatalist = new ArrayList<Object>();
				 	String alertMsg ="",bankFlag="";
				 try
				 {
					 
													
					conn = ResourceManager.getConnection();
					//ACC_TYPE , BANK_DESTINATION , BANK_STATE, BANK_CITY ,BANK_BRANCH
					if(ocomodule.equals("ACC_TYPE")){            	
						pStmtObject = (PreparedStatement)conn.prepareStatement(strAccountType);
						rs = pStmtObject.executeQuery();
						if(rs != null)
						{
							 while(rs.next())
							 {
				 					JSONObject jsonObject = new JSONObject();
									jsonObject.put("ACC_TYPE_ID",rs.getString("GENERAL_TYPE_ID")!=null?(String)rs.getString("GENERAL_TYPE_ID"):"");
									jsonObject.put("ACC_DESC",rs.getString("DESCRIPTION")!=null?(String)rs.getString("DESCRIPTION"):"");
									aldatalist.add(jsonObject);
							 }
								if(aldatalist != null && aldatalist.size() >0)
								 {
									 alertMsg ="Search Successfully";
									 bankFlag = "Y";
								 }
								 else{
									 alertMsg ="No data Found";
									 bankFlag = "N";
								 }
								 alSearchList.add(aldatalist);
								 alSearchList.add(bankFlag);
								 alSearchList.add(alertMsg);
						 }//end rs
						
						
			            }
					else if (ocomodule.equals("BANK_DESTINATION")){
						pStmtObject = (PreparedStatement)conn.prepareStatement(strBank_destination);
						rs = pStmtObject.executeQuery();
						if(rs != null)
						{
							 while(rs.next())
							 {
				 					JSONObject jsonObject = new JSONObject();
									jsonObject.put("BANK_NAME",rs.getString("BANK_NAME")!=null?(String)rs.getString("BANK_NAME"):"");
									jsonObject.put("BANK_DESC",rs.getString("BANK_NAME")!=null?(String)rs.getString("BANK_NAME"):"");
									aldatalist.add(jsonObject);
							 }
								if(aldatalist != null && aldatalist.size() >0)
								 {
									 alertMsg ="Search Successfully";
									 bankFlag = "Y";
								 }
								 else{
									 alertMsg ="No data Found";
									 bankFlag = "N";
								 }
								 alSearchList.add(aldatalist);
								 alSearchList.add(bankFlag);
								 alSearchList.add(alertMsg);
						 }//end rs
			            }
					else if (ocomodule.equals("BANK_CITY")){
						pStmtObject = (PreparedStatement)conn.prepareStatement(strBank_state);
						pStmtObject.setString(1, ocobankdestination);
						rs = pStmtObject.executeQuery();
						if(rs != null)
						{
							 while(rs.next())
							 {
				 					JSONObject jsonObject = new JSONObject();
									jsonObject.put("CITY_TYPE_ID",rs.getString("STATE_TYPE_ID")!=null?(String)rs.getString("STATE_TYPE_ID"):"");
									jsonObject.put("CITY_NAME",rs.getString("STATE_NAME")!=null?(String)rs.getString("STATE_NAME"):"");
									aldatalist.add(jsonObject);
							 }
								if(aldatalist != null && aldatalist.size() >0)
								 {
									 alertMsg ="Search Successfully";
									 bankFlag = "Y";
								 }
								 else{
									 alertMsg ="No data Found";
									 bankFlag = "N";
								 }
								 alSearchList.add(aldatalist);
								 alSearchList.add(bankFlag);
								 alSearchList.add(alertMsg);
						 }//end rs
			            }
					else if (ocomodule.equals("BANK_AREA")){

						pStmtObject = (PreparedStatement)conn.prepareStatement(strBank_city);
						pStmtObject.setString(1, ocobankcity);
						pStmtObject.setString(2, ocobankdestination);
						
						rs = pStmtObject.executeQuery();
						if(rs != null)
						{
							 while(rs.next())
							 {
				 					JSONObject jsonObject = new JSONObject();
									jsonObject.put("AREA_TYPE_ID",rs.getString("CITY_TYPE_ID")!=null?(String)rs.getString("CITY_TYPE_ID"):"");
									jsonObject.put("AREA_DESCRIPTION",rs.getString("CITY_DESCRIPTION")!=null?(String)rs.getString("CITY_DESCRIPTION"):"");
									aldatalist.add(jsonObject);
							 }
								if(aldatalist != null && aldatalist.size() >0)
								 {
									 alertMsg ="Search Successfully";
									 bankFlag = "Y";
								 }
								 else{
									 alertMsg ="No data Found";
									 bankFlag = "N";
								 }
								 alSearchList.add(aldatalist);
								 alSearchList.add(bankFlag);
								 alSearchList.add(alertMsg);
						 }//end rs
			            
			            }
					else if (ocomodule.equals("BANK_BRANCH")){
						
			            	pStmtObject = (PreparedStatement)conn.prepareStatement(strBank_branch);
			            	
			            	pStmtObject.setString(1, ocobankcity);
			            	pStmtObject.setString(2, ocobankarea);
			            	pStmtObject.setString(3, ocobankdestination);
							rs = pStmtObject.executeQuery();
							if(rs != null)
							{
								 while(rs.next())
								 {
					 					JSONObject jsonObject = new JSONObject();
										jsonObject.put("BRANCH_SEQ_ID",rs.getString("BRANCH_SEQ_ID")!=null?(String)rs.getString("BRANCH_SEQ_ID"):"");
										jsonObject.put("BRANCH_NAME",rs.getString("BRANCH_NAME")!=null?(String)rs.getString("BRANCH_NAME"):"");
										aldatalist.add(jsonObject);
								 }
									if(aldatalist != null && aldatalist.size() >0)
									 {
										 alertMsg ="Search Successfully";
										 bankFlag = "Y";
									 }
									 else{
										 alertMsg ="No data Found";
										 bankFlag = "N";
									 }
									 alSearchList.add(aldatalist);
									 alSearchList.add(bankFlag);
									 alSearchList.add(alertMsg);
							 }//end rs
			            } 
					
					

				 }
				 catch (SQLException | JSONException | TTKException e) 
		 			{
		 				e.printStackTrace();
		 			}
					finally
					{
						try
						{
							if(rs != null)rs.close();
						} 
						catch (SQLException e1) 
						{
							e1.printStackTrace();
						}
						try
						{
							if(pStmtObject != null) pStmtObject.close();
						}catch(SQLException e)
						{
							e.printStackTrace();
						}try
						{
							if(conn != null) conn.close();
						}catch(SQLException e)
						{
							e.printStackTrace();
						}finally{
							rs = null;
							pStmtObject = null;
							conn = null;
						}	
					}
				 return alSearchList; 
		}
					
		
		/* This method method stores the Benifit Type List coming from Vidal wire rest as a String  */
		public ArrayList getBenifitTypeList() throws TTKException
		{
					 Connection conn = null;
					 PreparedStatement pStmtObject = null;
					 ResultSet rs = null;
				    ArrayList<Object> alSearchList = new ArrayList<Object>();
				    	String alertMsg ="",claimFormFlag="";

					 ArrayList<Object> alBenifitTypeList = new ArrayList<Object>();
					 try
					 {
						 conn = ResourceManager.getConnection();
						pStmtObject = (PreparedStatement)conn.prepareStatement(BenifitTypeList);
						rs = pStmtObject.executeQuery();
					
						if(rs != null)
						{
							 while(rs.next())
							 {
								    JSONObject jsonObject = new JSONObject();
									jsonObject.put("GENERAL_TYPE_ID",rs.getString("GENERAL_TYPE_ID")!=null?(String)rs.getString("GENERAL_TYPE_ID"):"");
									jsonObject.put("DESCRIPTION",rs.getString("DESCRIPTION")!=null?(String)rs.getString("DESCRIPTION"):"");
									alBenifitTypeList.add(jsonObject);
							 }
							 if(alBenifitTypeList != null && alBenifitTypeList.size() >0)
							 {
								 alertMsg ="Search Successfully";
								 claimFormFlag = "Y";
													 
							 }
							 else{
								 alertMsg ="No data Found";
								 claimFormFlag = "N";
							 }
							 
							 alSearchList.add(alBenifitTypeList);
							 alSearchList.add(claimFormFlag);
							 alSearchList.add(alertMsg);
							 
						 }	
					 }
					 catch (SQLException | JSONException e) 
			 			{
			 				e.printStackTrace();
			 			}
						finally
						{
							try
							{
								if(rs != null)rs.close();
							} 
							catch (SQLException e1) 
							{
								e1.printStackTrace();
							}
							try
							{
								if(pStmtObject != null) pStmtObject.close();
							}catch(SQLException e)
							{
								e.printStackTrace();
							}try
							{
								if(conn != null) conn.close();
							}catch(SQLException e)
							{
								e.printStackTrace();
							}finally{
								rs = null;
								pStmtObject = null;
								conn = null;
							}	
						}
					 return alSearchList; 	
		}
		 		
		
		  @SuppressWarnings({ "unused", "rawtypes", "null" })
		    public ArrayList getWindowPeriod(Long ocoUserId)  throws TTKException {
			  Connection conn = null;
		 		 PreparedStatement pStmtObject = null;
		 		 ResultSet rs = null;
		 	  	ArrayList<Object> alSearchList = new ArrayList<Object>();
		      	ArrayList<Object> alFinalList = new ArrayList<Object>();
		 		 try
		 		 {
		 			conn = ResourceManager.getConnection();
		 			pStmtObject = (PreparedStatement)conn.prepareStatement(strWindowPeriod);
		 			pStmtObject.setLong(1, ocoUserId);
		 			rs = pStmtObject.executeQuery();
		 		
		 			if(rs != null)
		 			{
		 				 while(rs.next())
		 				 {
		 					 JSONObject jsonObject = new JSONObject();
		 					jsonObject.put("memberWindowPeriodFlag",rs.getString(1));
						
		 					 
							 alSearchList.add(jsonObject);
						 }
						 alFinalList.add(alSearchList);

		 			 }	
		 		 }
		 			catch (SQLException | JSONException e) 
		 			{
		 				e.printStackTrace();
		 			}
		 			finally
		 			{
		 				try
		 				{
		 					if(rs != null)rs.close();
		 				} 
		 				catch (SQLException e1) 
		 				{
		 					e1.printStackTrace();
		 				}
		 				try
		 				{
		 					if(pStmtObject != null) pStmtObject.close();
		 				}catch(SQLException e)
		 				{
		 					e.printStackTrace();
		 				}try
		 				{
		 					if(conn != null) conn.close();
		 				}catch(SQLException e)
		 				{
		 					e.printStackTrace();
		 				}finally{
		 					rs = null;
		 					pStmtObject = null;
		 					conn = null;
		 				}	
		 			}
		 		 return alFinalList; 
		 }
		  
		  public ArrayList saveShortfallSubmission(OnlineIntimationVO onlineIntimationVO)throws TTKException 
		    {
			    ArrayList alResult =new ArrayList();
			    String strResult = "",strBatch_No = "";
			    Connection conn = null;
		        CallableStatement cStmtObject=null;
		        try {
		            
		            conn = ResourceManager.getConnection();
		            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveShortfallDetails);
		            cStmtObject.setLong(1,onlineIntimationVO.getShortfallSeqID());
		            cStmtObject.setBytes(2,onlineIntimationVO.getShortfallSubmission());
		            cStmtObject.setString(3,onlineIntimationVO.getRemarks());
		            cStmtObject.registerOutParameter(4,OracleTypes.VARCHAR);	            
		            cStmtObject.execute();
		           strResult = String.valueOf(cStmtObject.getString(4));
		            alResult.add(strResult);
	      
		        }//end of try
		        catch (SQLException sqlExp)
		        {
		            throw new TTKException(sqlExp, "webservice");
		        }//end of catch (SQLException sqlExp)
		        catch (Exception exp)
		        {
		            throw new TTKException(exp, "webservice");
		        }//end of catch (Exception exp)
		        finally
				{
		        	// Nested Try Catch to ensure resource closure 
		        	try // First try closing the Statement
		        	{
		        		try
		        		{
		        			if (cStmtObject != null) cStmtObject.close();
		        		}//end of try
		        		catch (SQLException sqlExp)
		        		{
		        			log.error("Error while closing the Statement in WebServiceDAOImpl savePolicy()",sqlExp);
		        			throw new TTKException(sqlExp, "webservice");
		        		}//end of catch (SQLException sqlExp)
		        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		        		{
		        			try
		        			{
		        				if(conn != null) conn.close();
		        			}//end of try
		        			catch (SQLException sqlExp)
		        			{
		        				log.error("Error while closing the Connection in WebServiceDAOImpl savePolicy()",sqlExp);
		        				throw new TTKException(sqlExp, "webservice");
		        			}//end of catch (SQLException sqlExp)
		        		}//end of finally Connection Close
		        	}//end of try
		        	catch (TTKException exp)
		        	{
		        		throw new TTKException(exp, "webservice");
		        	}//end of catch (TTKException exp)
		        	finally // Control will reach here in anycase set null to the objects
		        	{
		        		cStmtObject = null;
		        		conn = null;
		        	}//end of finally
				}//end of finally
		        return alResult;
		    }//end of savePolicy(String document)

			
		 //This methos added for alAlhiPreauth()
			
	public String alAlhiPreauth(String preappravalData,String empanelId) throws TTKException {
		// StringBuilder document=new StringBuilder();
		String strResult = "";
		String strFailed ="";
		Connection conn = null;
		CallableStatement cStmtObject = null;
		//FileOutputStream fos = null;
		//BufferedOutputStream bout = null;
		//XMLType xml = null;
		//Document doc = null;
		ResultSet rs = null;
		//ArrayList alResult = new ArrayList();
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = conn.prepareCall("{CALL ALAHLI_PAT_DATA_UPLD_PKG.save_pre_auth_xml(?,?,?)}");
			XMLType preauthXML = null;
			preauthXML = XMLType.createXML(((OracleConnection) ((WrappedConnectionJDK6) conn).getUnderlyingConnection()), preappravalData);
			cStmtObject.setString(1, empanelId);
			cStmtObject.setObject(2, preauthXML);
			cStmtObject.registerOutParameter(3, OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet) cStmtObject.getObject(3);
			if (rs != null) {
				while (rs.next()) {
					if (rs.getString("SUCCESS_YN") != null) {
						if(strResult.trim().equals(""))
						strResult = "RequestedResponse:"+strResult + rs.getString("SUCCESS_YN");
					}
						if (strResult.equalsIgnoreCase("RequestedResponse:SUCCESS")) {
							if (rs.getString("PREAUTH_STATUS") != null) {
								strResult = strResult + "|PreApprovalStatus:"+ rs.getString("PREAUTH_STATUS");
							}
							if (rs.getString("PREAUTH_NO") != null) {
								strResult = strResult + "|PreApprovalNo:"+ rs.getString("PREAUTH_NO");
							}
						} else {
							if (rs.getString("ERROR_MSG") != null) {
								strFailed =strFailed+ "|"+ rs.getString("ERROR_MSG");
							}
						}
					}
				}
				strResult = strResult + strFailed;
			return strResult.trim();
		}// end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "webservice");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "webservice");
		}// end of catch (Exception exp)
		finally {
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the Statement
			{
				try {
					if (rs != null)
						rs.close();
					if (cStmtObject != null)
						cStmtObject.close();
				}// end of try
				catch (SQLException sqlExp) {
					log.error(
							"Error while closing the Statement in WebServiceDAOImpl alAlhiPreauth()",
							sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}// end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches
						// here. Try closing the connection now.
				{
					try {
						if (conn != null)
							conn.close();
					}// end of try

					catch (SQLException sqlExp) {
						log.error(
								"Error while closing the Connection in WebServiceDAOImpl alAlhiPreauth()",
								sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}// end of catch (SQLException sqlExp)

				}// end of finally Connection Close
			}// end of try
			catch (TTKException exp) {
				throw new TTKException(exp, "webservice");
			}// end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the
					// objects
			{
				rs=null;
				cStmtObject = null;
				conn = null;
			}// end of finally
		}// end of finally
	}// end of alAlhiPreauth(String preappravalData)
			
	
	@SuppressWarnings("resource")
	public PreApprovalStatusResponse checkPreapprovalStatus(String preapprovalNo)throws TTKException {
		//System.out.println("In WebServiceDAOImpl checkPreapprovalStatus()");
		Connection conn = null;
		CallableStatement cStmtObject = null;
		ResultSet rs = null;
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = conn.prepareCall("{CALL ALAHLI_PAT_DATA_UPLD_PKG.SELECT_PAT_AUTH(?,?,?)}");
			cStmtObject.setObject(1, preapprovalNo);
			cStmtObject.registerOutParameter(2, OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(3, OracleTypes.CURSOR);
			cStmtObject.execute();
			PreApprovalStatusResponse preApprovalStatusResponse=new PreApprovalStatusResponse();
			
			rs = (java.sql.ResultSet) cStmtObject.getObject(2);
			if (rs != null) {
				while (rs.next()) {
					if (rs.getString("PRE_AUTH_NUMBER") != null) {
						preApprovalStatusResponse.setPreApprovalNo( rs.getString("PRE_AUTH_NUMBER"));
					}
					if (rs.getString("PAT_STATUS_TYPE_ID") != null) {
						preApprovalStatusResponse.setPreApprovalStatus( rs.getString("PAT_STATUS_TYPE_ID"));
					}
					if (rs.getString("AUTH_NUMBER") != null) {
						preApprovalStatusResponse.setAuthorizationNumber(rs.getString("AUTH_NUMBER"));
					}		
					if (rs.getString("TOT_GROSS_AMOUNT") != null) {
						preApprovalStatusResponse.setTotalGrossAmount( rs.getString("TOT_GROSS_AMOUNT"));
					}				
					if (rs.getString("TOT_DISCOUNT_AMOUNT") != null) {
						preApprovalStatusResponse.setTotalDiscountAmount( rs.getString("TOT_DISCOUNT_AMOUNT"));
					}						
					if (rs.getString("TOT_DISC_GROSS_AMOUNT") != null) {
						preApprovalStatusResponse.setTotalDiscountGrossAmount( rs.getString("TOT_DISC_GROSS_AMOUNT"));
					}				
					if (rs.getString("TOT_PATIENT_SHARE_AMOUNT") != null) {
						preApprovalStatusResponse.setTotalPatientShare( rs.getString("TOT_PATIENT_SHARE_AMOUNT"));
					}
					if (rs.getString("TOT_NET_AMOUNT") != null) {
						preApprovalStatusResponse.setTotalNetAmount( rs.getString("TOT_NET_AMOUNT"));
					}
					if (rs.getString("DIS_ALLOWED_AMOUNT") != null) {
						preApprovalStatusResponse.setTotalDisAllowedAmount( rs.getString("DIS_ALLOWED_AMOUNT"));
					}
					if (rs.getString("TOT_ALLOWED_AMOUNT") != null) {
						preApprovalStatusResponse.setTotalAllowedAmount( rs.getString("TOT_ALLOWED_AMOUNT"));
					}				
				}
			}
			
			rs = (java.sql.ResultSet) cStmtObject.getObject(3);
		 List<ActivityDetails>  listActivityDetails = new ArrayList<ActivityDetails>();
		 ActivityDetails activityDetails = null;
			if (rs != null) {
				while (rs.next()) {
					activityDetails = new ActivityDetails();
					if (rs.getString("S_NO") != null) {
						activityDetails.setSlno( rs.getString("S_NO"));
					}
					if (rs.getString("CODE") != null) {
						activityDetails.setActivityCodeServiceCode( rs.getString("CODE"));
					}	
					if (rs.getString("QUANTITY") != null) {
						activityDetails.setQuantity(rs.getString("QUANTITY"));
					}	
					if (rs.getString("APPROVED_QUANTITY") != null) {
						activityDetails.setApprovedQuantity( rs.getString("APPROVED_QUANTITY"));
					}
					if (rs.getString("START_DATE") != null) {
						activityDetails.setStartDate( rs.getString("START_DATE"));
					}
					if (rs.getString("GROSS_AMOUNT") != null) {
						activityDetails.setGrossAmount( rs.getString("GROSS_AMOUNT"));
					}
					if (rs.getString("DISCOUNT_AMOUNT") != null) {
						activityDetails.setDiscountAmount( rs.getString("DISCOUNT_AMOUNT"));
					}
					if (rs.getString("DISC_GROSS_AMOUNT") != null) {
						activityDetails.setDiscountGrossAmount( rs.getString("DISC_GROSS_AMOUNT"));
					}
					if (rs.getString("PATIENT_SHARE_AMOUNT") != null) {
						activityDetails.setPatientShareAmount( rs.getString("PATIENT_SHARE_AMOUNT"));
					}
					if (rs.getString("NET_AMOUNT") != null) {
						activityDetails.setNetAmount( rs.getString("NET_AMOUNT"));
					}
					if (rs.getString("DIS_ALLOWED_AMOUNT") != null) {
						activityDetails.setDisAllowedAmount( rs.getString("DIS_ALLOWED_AMOUNT"));
					}
					if (rs.getString("ALLOWED_AMOUNT") != null) {
						activityDetails.setAllowedAmount( rs.getString("ALLOWED_AMOUNT"));
					}
					if (rs.getString("DENIAL_CODE") != null) {
						activityDetails.setDenialCode( rs.getString("DENIAL_CODE"));
					}
					if (rs.getString("DENIAL_DESC") != null) {
						activityDetails.setDenialCodeDesc( rs.getString("DENIAL_DESC"));
					}	
					listActivityDetails.add(activityDetails);
				}
			}
			preApprovalStatusResponse.setListActivityDetails(listActivityDetails);
			return preApprovalStatusResponse;
		}// end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "webservice");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "webservice");
		}// end of catch (Exception exp)
		finally {
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the Statement
			{
				try {
					if (rs != null)
						rs.close();
					if (cStmtObject != null)
						cStmtObject.close();
				}// end of try
				catch (SQLException sqlExp) {
					log.error(
							"Error while closing the Statement in WebServiceDAOImpl checkPreapprovalStatus()",
							sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}// end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches
						// here. Try closing the connection now.
				{
					try {
						if (conn != null)
							conn.close();
					}// end of try

					catch (SQLException sqlExp) {
						log.error(
								"Error while closing the Connection in WebServiceDAOImpl checkPreapprovalStatus()",
								sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}// end of catch (SQLException sqlExp)

				}// end of finally Connection Close
			}// end of try
			catch (TTKException exp) {
				throw new TTKException(exp, "webservice");
			}// end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the
					// objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}// end of finally
		}// end of finally
	}// end of checkPreapprovalStatus(String preappravalNo)
	
	 @SuppressWarnings({ "unused", "rawtypes", "null" })
	    public ArrayList getProviderNWtype(String ocoPolicyGrpseqid)  throws TTKException {
	       	
	       	ArrayList<Object> alSearchList = new ArrayList<Object>();
	      	ArrayList<Object> alFinalList = new ArrayList<Object>();
	        String alertMsg = "";String providerFlag = "";
	   		
	   		Connection conn = null;	
	   		CallableStatement cStmtObject=null;
	   		ResultSet rs =null;
	        JSONArray jsonArray = new JSONArray();

	   		try {
	   			conn = ResourceManager.getConnection();
	   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(NWtype_Member);
	   			
	   			cStmtObject.setString(1,ocoPolicyGrpseqid);
				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);   
				cStmtObject.execute();					
				rs = (ResultSet)cStmtObject.getObject(2);
				
				if(rs != null)
				{
					 while(rs.next())
					 {
						 
						 JSONObject jsonObject = new JSONObject();
						jsonObject.put("NW_TYPE_ID",rs.getString("NW_TYPE_ID")!=null?(String)rs.getString("NW_TYPE_ID"):"");
						jsonObject.put("NW_TYPE_DESC",rs.getString("NW_TYPE_DESC")!=null?(String)rs.getString("NW_TYPE_DESC"):"");
						 alSearchList.add(jsonObject);
					 }
					 if(alSearchList != null && alSearchList.size() >0)
					 {
						 alertMsg ="Search Successfully";
						 providerFlag = "Y";
					 }
					 else{
						 alertMsg ="No data Found";
						 providerFlag = "N";
					 }
					 
					 alFinalList.add(alSearchList);
					 alFinalList.add(providerFlag);
					 alFinalList.add(alertMsg);
					
				}
	   		}//end of try
	          catch (SQLException sqlExp)
	   	   {
	   		throw new TTKException(sqlExp, "webservice");
	   	    }//end of catch (SQLException sqlExp)
	   	 catch (Exception exp)
	   	{
	   		throw new TTKException(exp, "webservice");
	   	}//end of catch (Exception exp)

	   	finally
	   	{
	   		// Nested Try Catch to ensure resource closure 
	   		try // First try closing the Statement
	   		{
	   			try
	   			{
	   				if (rs != null) rs.close();
	   				if (cStmtObject != null) cStmtObject.close();
	   			}//end of try
	   			catch (SQLException sqlExp)
	   			{
	   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
	   				throw new TTKException(sqlExp, "webservice");
	   			}//end of catch (SQLException sqlExp)
	   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	   			{
	   				try
	   				{
	   					if(conn != null) conn.close();
	   				}//end of try
	   				catch (SQLException sqlExp)
	   				{
	   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
	   					throw new TTKException(sqlExp, "webservice");
	   				}//end of catch (SQLException sqlExp)
	   			}//end of finally Connection Close
	   		}//end of try
	   		catch (TTKException exp)
	   		{
	   			throw new TTKException(exp, "webservice");
	   		}//end of catch (TTKException exp)
	   		finally // Control will reach here in anycase set null to the objects
	   		{
	   			rs = null;
	   			cStmtObject = null;
	   			conn = null;
	   		}//end of finally
	   	}//end of finally
	   		return alFinalList;
	   }//end o
	 
	 
	 public int saveDocUploads(String strPreauthNo,byte[] inputPdfData) throws TTKException {
		 int iResult = 0;
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	try{
	    		conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveUploadFils);
		        cStmtObject.setString(1,strPreauthNo);//preauthseqid
		        if(inputPdfData.length<=100){
		        	cStmtObject.setString(2,"N");
		        }else{
		        	cStmtObject.setString(2,"Y");
		        }
	            cStmtObject.setBytes(3,inputPdfData);//FILE INPUT STREAM   (image_file)
	            cStmtObject.registerOutParameter(4,Types.INTEGER);//ROW_PROCESSED        
	            cStmtObject.execute();            
	            iResult  = cStmtObject.getInt(4);    
	            
	        }//end of try
	    	catch (SQLException sqlExp)
	        {
	            throw new TTKException(sqlExp, "webservice");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "webservice");
	        }//end of catch (Exception exp)
	        finally
			{
	        	// Nested Try Catch to ensure resource closure
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in WebServiceDAOImpl saveDocUploads()",sqlExp);
	        			throw new TTKException(sqlExp, "webservice");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in WebServiceDAOImpl saveDocUploads()",sqlExp);
	        				throw new TTKException(sqlExp, "webservice");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "webservice");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	return iResult;
	    }//


	public ArrayList<Object> getBenifitTypeListNew() throws TTKException{
		System.out.println("================getBenifitTypeListNew=======================");
		 Connection conn = null;
		 PreparedStatement pStmtObject = null;
		 ResultSet rs = null;
	    ArrayList<Object> alSearchList = new ArrayList<Object>();
	    	String alertMsg ="",claimFormFlag="";

		 ArrayList<Object> alBenifitTypeList = new ArrayList<Object>();
		 try
		 {
			 conn = ResourceManager.getConnection();
			pStmtObject = (PreparedStatement)conn.prepareStatement(strBenifitTypeListNew);
			rs = pStmtObject.executeQuery();
		
			if(rs != null)
			{
				 while(rs.next())
				 {
					    JSONObject jsonObject = new JSONObject();
					    jsonObject.put("GENERAL_TYPE_ID",rs.getString("GENERAL_TYPE_ID")!=null?(String)rs.getString("GENERAL_TYPE_ID"):"");
						jsonObject.put("DESCRIPTION",rs.getString("DESCRIPTION")!=null?(String)rs.getString("DESCRIPTION"):"");
						alBenifitTypeList.add(jsonObject);
				 }
				 if(alBenifitTypeList != null && alBenifitTypeList.size() >0)
				 {
					 alertMsg ="Search Successfully";
					 claimFormFlag = "Y";
										 
				 }
				 else{
					 alertMsg ="No data Found";
					 claimFormFlag = "N";
				 }
				 
				 alSearchList.add(alBenifitTypeList);
				 alSearchList.add(claimFormFlag);
				 alSearchList.add(alertMsg);
				 
			 }	
		 }
		 catch (SQLException | JSONException e) 
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(rs != null)rs.close();
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				try
				{
					if(pStmtObject != null) pStmtObject.close();
				}catch(SQLException e)
				{
					e.printStackTrace();
				}try
				{
					if(conn != null) conn.close();
				}catch(SQLException e)
				{
					e.printStackTrace();
				}finally{
					rs = null;
					pStmtObject = null;
					conn = null;
				}	
			}
		 return alSearchList; 	
}


	public ArrayList getBenefitLimit(OnlineIntimationVO onlineIntimationVO) throws TTKException{
	       System.out.println("===============getBenefitLimit================");
       	ArrayList<Object> alSearchList = new ArrayList<Object>();
       	ArrayList<Object> alFinalList = new ArrayList<Object>();
   		Connection conn = null;	
   		CallableStatement cStmtObject=null;
   		ResultSet rs =null;
        JSONArray jsonArray = new JSONArray();

   		//Calendar calendar = Calendar.getInstance();
   		//StringBuffer sb=null;

   		try {
   			conn = ResourceManager.getConnection();
   			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMemberBenefitLimit);
   			cStmtObject.setLong(1,onlineIntimationVO.getMemberSeqID());
   			cStmtObject.setString(2,onlineIntimationVO.getBenifitTypeVal());
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);   
			cStmtObject.execute();					
			rs = (ResultSet)cStmtObject.getObject(3);
			
			if(rs != null)
			{
				 while(rs.next())
				 {	 
					 JSONObject jsonObject = new JSONObject();	
					jsonObject.put("AVA_BENIFIT_LIMIT",rs.getString("AVA_BENIFIT_LIMIT")!=null?(String)rs.getString("AVA_BENIFIT_LIMIT"):"");
					jsonObject.put("UTILISED_AMOUNT",rs.getString("UTILISED_AMOUNT")!=null?(String)rs.getString("UTILISED_AMOUNT"):"");
					jsonObject.put("BALANCE_AMOUNT",rs.getString("BALANCE_AMOUNT")!=null?(String)rs.getString("BALANCE_AMOUNT"):"");
					alSearchList.add(jsonObject);
				 }	 
				 String Status="";
				 String EnrollFlag="";
				 if(alSearchList != null && alSearchList.size() >0)
				 {
					 Status ="Search Successfully";
					 EnrollFlag = "Y";							 
				 }
				 else{
					 Status ="No data Found";
					 EnrollFlag = "N";
				 }	 
				 alFinalList.add(alSearchList);
				 alFinalList.add(Status);
				 alFinalList.add(EnrollFlag);	
			}
   		}//end of try
          catch (SQLException sqlExp)
   	   {
   		throw new TTKException(sqlExp, "webservice");
   	    }//end of catch (SQLException sqlExp)
   	 catch (Exception exp)
   	{
   		throw new TTKException(exp, "webservice");
   	}//end of catch (Exception exp)

   	finally
   	{
   		// Nested Try Catch to ensure resource closure 
   		try // First try closing the Statement
   		{
   			try
   			{
   				if(rs != null) rs.close();
   				if (cStmtObject != null) cStmtObject.close();
   			}//end of try
   			catch (SQLException sqlExp)
   			{
   				log.error("Error while closing the Statement in WebServiceDAOImpl getLoginService()",sqlExp);
   				throw new TTKException(sqlExp, "webservice");
   			}//end of catch (SQLException sqlExp)
   			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
   			{
   				try
   				{
   					if(conn != null) conn.close();
   					
   				}//end of try
   				catch (SQLException sqlExp)
   				{
   					log.error("Error while closing the Connection in WebServiceDAOImpl getLoginService()",sqlExp);
   					throw new TTKException(sqlExp, "webservice");
   				}//end of catch (SQLException sqlExp)
   			}//end of finally Connection Close
   		}//end of try
   		catch (TTKException exp)
   		{
   			throw new TTKException(exp, "webservice");
   		}//end of catch (TTKException exp)
   		finally // Control will reach here in anycase set null to the objects
   		{
   			rs = null;
   			cStmtObject = null;
   			conn = null;
   			
   		}//end of finally
   	}//end of finally
   		return alFinalList;
   }
 
public String alAlhiClaim(String inputPreapprovalXML) throws TTKException{
		// StringBuilder document=new StringBuilder();
		String strResult = "";
		String strFailed ="";
		Connection conn = null;
		CallableStatement cStmtObject = null;
		//FileOutputStream fos = null;
		//BufferedOutputStream bout = null;
		//XMLType xml = null;
		//Document doc = null;
		ResultSet rs = null;
		//ArrayList alResult = new ArrayList();
		
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = conn.prepareCall("{CALL ALAHLI_CLM_DATA_PKG.SAVE_CLAIM_XML(?,?)}");
			XMLType preauthXML = null;
			preauthXML = XMLType.createXML(((OracleConnection) ((WrappedConnectionJDK6) conn).getUnderlyingConnection()), inputPreapprovalXML);
			cStmtObject.setObject(1, preauthXML);
			cStmtObject.registerOutParameter(2, OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet) cStmtObject.getObject(2);
			if (rs != null) {
				while (rs.next()) {
					if (rs.getString("SUCCESS_YN") != null) {
						if(strResult.trim().equals(""))
						strResult = "RequestedResponse:"+strResult + rs.getString("SUCCESS_YN");
					}
						if (strResult.equalsIgnoreCase("RequestedResponse:SUCCESS")) {
							if (rs.getString("CLAIM_STATUS") != null) {
								strResult = strResult + "|ClaimStatus:"+ rs.getString("CLAIM_STATUS");
							}
							if (rs.getString("CLAIM_NO") != null) {
								strResult = strResult + "|ClaimNo:"+ rs.getString("CLAIM_NO");
							}
						} else {
							if (rs.getString("ERROR_MSG") != null) {
								strFailed =strFailed+ "|"+ rs.getString("ERROR_MSG");
							}
						}
					}
				}
				strResult = strResult + strFailed;
			return strResult.trim();
		}// end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "webservice");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "webservice");
		}// end of catch (Exception exp)
		finally {
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the Statement
			{
				try {
					if (rs != null)
						rs.close();
					if (cStmtObject != null)
						cStmtObject.close();
				}// end of try
				catch (SQLException sqlExp) {
					log.error(
							"Error while closing the Statement in WebServiceDAOImpl alAlhiClaim()",
							sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}// end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches
						// here. Try closing the connection now.
				{
					try {
						if (conn != null)
							conn.close();
					}// end of try

					catch (SQLException sqlExp) {
						log.error(
								"Error while closing the Connection in WebServiceDAOImpl alAlhiClaim()",
								sqlExp);
						throw new TTKException(sqlExp, "webservice");
					}// end of catch (SQLException sqlExp)

				}// end of finally Connection Close
			}// end of try
			catch (TTKException exp) {
				throw new TTKException(exp, "webservice");
			}// end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the
					// objects
			{
				rs=null;
				cStmtObject = null;
				conn = null;
			}// end of finally
		}// end of finally
	}



	public ArrayList updateDependentDetails(OnlineIntimationVO onlineIntimationVO) throws TTKException {
		ArrayList<Object> alResult = new ArrayList<Object>();
		Connection conn = null;	
		CallableStatement cStmtObject=null;

		try {
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(update_dependent_details);
				  			
			cStmtObject.setLong(1,onlineIntimationVO.getMemberSeqID());  			
			cStmtObject.setString(2,onlineIntimationVO.getMobileNbr()); 
			cStmtObject.setString(3,onlineIntimationVO.getEmailID()); 
		    cStmtObject.registerOutParameter(4,Types.VARCHAR);
		  
			cStmtObject.execute();
			alResult.add(cStmtObject.getInt(4));
			//alResult.add(cStmtObject.getString(5));//if any exception from backend
		}//end of try
     catch (SQLException sqlExp)
	   {
		throw new TTKException(sqlExp, "webservice");
	    }//end of catch (SQLException sqlExp)
	 catch (Exception exp)
	{
		throw new TTKException(exp, "webservice");
	}//end of catch (Exception exp)

	finally
	{
		// Nested Try Catch to ensure resource closure 
		try // First try closing the Statement
		{
			try
			{
				if (cStmtObject != null) cStmtObject.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Statement in WebServiceDAOImpl updateDependentDetails()",sqlExp);
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in WebServiceDAOImpl updateDependentDetails()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return alResult;
}


	public int sendCertificateMail(String memberSeqId) throws TTKException{
		Connection conn = null;	
		CallableStatement cStmtObject=null;
		int result=0;
		try {
			System.out.println("======================sendCertificateMail=========================");
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(send_certificate_mail);
				  			
			cStmtObject.setString(1,memberSeqId);  			
		    cStmtObject.registerOutParameter(2,Types.VARCHAR);
		  
			cStmtObject.execute();
			result = cStmtObject.getInt(2);

		}//end of try
     catch (SQLException sqlExp)
	   {
		throw new TTKException(sqlExp, "webservice");
	    }//end of catch (SQLException sqlExp)
	 catch (Exception exp)
	{
		throw new TTKException(exp, "webservice");
	}//end of catch (Exception exp)

	finally
	{
		// Nested Try Catch to ensure resource closure 
		try // First try closing the Statement
		{
			try
			{
				if (cStmtObject != null) cStmtObject.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Statement in WebServiceDAOImpl sendCertificateMail()",sqlExp);
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in WebServiceDAOImpl sendCertificateMail()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return result;
}


	public String getAppVersionDetails(OnlineIntimationVO onlineIntimationVO)throws TTKException  {
    	ArrayList<Object> alSearchList = new ArrayList<Object>();
    	ArrayList<Object> alFinalList = new ArrayList<Object>();
		Connection conn = null;	
		CallableStatement cStmtObject=null;
		ResultSet rs =null;
     JSONArray jsonArray = new JSONArray();

		
     String allowFlag="";
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetAppVersionDetails);
			cStmtObject.setString(1,onlineIntimationVO.getApp_version());
			cStmtObject.setString(2,onlineIntimationVO.getDevice_type());
			cStmtObject.registerOutParameter(3,Types.VARCHAR);
			cStmtObject.execute();					
		   allowFlag=cStmtObject.getString(3);
				
		}//end of try
       catch (SQLException sqlExp)
	   {
		throw new TTKException(sqlExp, "webservice");
	    }//end of catch (SQLException sqlExp)
	 catch (Exception exp)
	{
		throw new TTKException(exp, "webservice");
	}//end of catch (Exception exp)

	finally
	{
		// Nested Try Catch to ensure resource closure 
		try // First try closing the Statement
		{
			try
			{
				if(rs != null) rs.close();
				if (cStmtObject != null) cStmtObject.close();
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Statement in WebServiceDAOImpl getAppVersionDetails()",sqlExp);
				throw new TTKException(sqlExp, "webservice");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
					
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in WebServiceDAOImpl getAppVersionDetails()",sqlExp);
					throw new TTKException(sqlExp, "webservice");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "webservice");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects
		{
			rs = null;
			cStmtObject = null;
			conn = null;
			
		}//end of finally
	}//end of finally
		return allowFlag;
}

}//end of WebServiceDAOImpl
