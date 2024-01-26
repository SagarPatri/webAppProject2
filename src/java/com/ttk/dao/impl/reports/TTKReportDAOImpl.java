/**
 * @ (#) TTKReportDAO.java June 28, 2006
 * Project       : TTK HealthCare Services
 * File          : TTKReportDAO.java
 * Author        : Krishna K H
 * Company       : Span Systems Corporation
 * Date Created  : June 28, 2006
 *
 * @author       : Krishna K H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dao.impl.reports;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.rowset.OracleCachedRowSet;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.administration.ReportVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.empanelment.HospitalDetailVO;

import oracle.xdb.XMLType;
import oracle.jdbc.driver.OracleConnection;

import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

public class TTKReportDAOImpl implements BaseDAO, Serializable {
	
	private static Logger log = Logger.getLogger(TTKReportDAOImpl.class);
	private static String strAppRejePreAuthsList="CLAIMS_APPROVAL_PKG.GEN_PAT_APR_REJ_RPT";//1274A
	private static String strAppRejeClaimsList="CLAIMS_APPROVAL_PKG.GEN_CLM_APR_REJ_RPT";//1274A
//Added as per Hospital Login
	private static String strTAKAFUL_LETTER="PAT_CLM_REPORTS_PKG.TAKAFUL_PO_LETTER";
	private static String strHospitalBillsPend="HOSPITAL_PKG.BILLS_PENDING_RPT";
	private static String strHospitalClmRegd="HOSPITAL_PKG.CLAIMS_DETAILED_RPT";
	private static String strHospitalOnlineSummaryRptClaims="HOSPITAL_PKG.CLAIMS_SUMMARY_RPT";
			
	private static String strInsurancePreAuthGenerateLetter="CLAIMS_APPROVAL_PKG.PAT_GENERATE_LETTER";
    private static String strPatGenPolicyHistory="CLAIMS_APPROVAL_PKG.PAT_GEN_POLICY_HIST";
    private static String strPatPreAuthHistory="CLAIMS_APPROVAL_PKG.PAT_PREV_HISTORY";
    private static String strPatBillDetails="CLAIMS_APPROVAL_PKG.PAT_BILL_DETAILS";
 
    private static String strInsClaimsGenerateLetter="CLAIMS_APPROVAL_PKG.CLM_GENERATE_LETTER";
    private static String  strClaimsPolicyHistory="CLAIMS_APPROVAL_PKG.CLM_GEN_POLICY_HIST";
    private static String strPrevClaimsHistory="CLAIMS_APPROVAL_PKG.CLM_PREV_HISTORY";
    private static String strClaimsBillHistory="CLAIMS_APPROVAL_PKG.CLM_BILL_DETAILS";
    private static String strClaimsIntimateSend="CLAIMS_APPROVAL_PKG.CLAIMS_INTIMATE_SEND_IAR";
    private static String strPreAuthIntimateSend="CLAIMS_APPROVAL_PKG.PAT_INTIMATE_SEND_IAR";
   // claims_intimate_send_ar
    
    //Mail-SMS for Cigna
    private static final String strCLAIM_CLOSURE_LTR_ADVISOR="PRE_CLM_REPORTS_PKG.CIGNA_CLOSURE_REMINDER"; 
    //added for mail-sms template for cign
    private static final String strCLAIM_SUB_APP_LTR_ADV = "PRE_CLM_REPORTS_PKG.cigna_get_bill_dtls";
        
    private static final String strCLAIM_REJECT_LTR_PO="PRE_CLM_REPORTS_PKG.CIGNA_CLAIM_REJECTION";
    private static final String strCLAIM_APP_LTR_ADV="PRE_CLM_REPORTS_PKG.Claim_settlement_ltr_Advisor";
    private static final String strCLAIM_SETTLEMENT_LTR_PO="PRE_CLM_REPORTS_PKG.cigna_claim_settlement";
    
   
    //Enrollment Reports
    private static String strPR_HOSP_DOC_RCVD_REPORT="HOSPITAL_EMPANEL_REPORTS_PKG.PR_HOSP_DOC_RCVD_REPORT";
    private static String strIMAGE_NOT_UPLOADED="ENROLLMENT_REPORTS_PKG.IMAGE_NOT_UPLOADED";
    private static String strPOLICIES_AWAITING_WORKFLOW="ENROLLMENT_REPORTS_PKG.POLICIES_AWAITING_WORKFLOW";
    private static String strCARDS_PRINTED="ENROLLMENT_REPORTS_PKG.CARDS_PRINTED";
    private static String strCARDS_NOT_DELIVERED="ENROLLMENT_REPORTS_PKG.CARDS_NOT_DELIVERED";
    private static String strGROUPS_RENEWAL="ENROLLMENT_REPORTS_PKG.GROUPS_RENEWAL";
    private static String strTIMETAKEN_TO_RECEIVE_POLICY="ENROLLMENT_REPORTS_PKG.TIMETAKEN_TO_RECEIVE_POLICY";
    private static String strDISPATCH_REPORT="ENROLLMENT_REPORTS_PKG.DISPATCH_REPORT";
    private static String strENDORSEMENTS="ENROLLMENT_REPORTS_PKG.ENDORSEMENTS";
    private static String strTIMETAKEN_TO_PROCESS_POLICY="ENROLLMENT_REPORTS_PKG.TIMETAKEN_TO_PROCESS_POLICY";
    private static String strDISPATCH_LIST="ENROLLMENT_REPORTS_PKG.DISPATCH_LIST";
    private static String strBOI_REPORT="ENROLLMENT_REPORTS_PKG.BOI_DETAIL_REPORT";
    private static String strIMPACT_REPORT = "ENDORSEMENT_REPORTS_PKG.ENDORSEMENT_IMPACT_REPORT"; 
    private static String strPREMIUM_CHANGE_REPORT = "ENDORSEMENT_REPORTS_PKG.PREMIUM_CHANGE_REPORT";
    private static String strEFT_COV_LETTER_ANNEXURE = "BATCH_REPORT_PKG.EFT_COV_LETTER_ANNEXURE";
    private static String strEFT_COVERING_LETTER = "BATCH_REPORT_PKG.EFT_COVERING_LETTER";
    private static String strGET_MEMBERS_ADDED_LIST = "ENROLLMENT_REPORTS_PKG.GET_MEMBERS_ADDED_LIST";
    private static String StrSOFTCOPYUPLOAD_ERROR_DETAILS="ENROLLMENT_REPORTS_PKG.SELECT_CORP_BATCH_ERROR_LOG";
    //Added for IBM.....9
    private static String strDELETION_CUTOFF ="APP.ENROLLMENT_REPORTS_PKG.DELETION_CUT_OFF_REPORT";//sri Krishna
    private static String strADDITION_CUTOFF="APP.ENROLLMENT_REPORTS_PKG.ADDITION_CUT_OFF_REPORT";
    private static String strNEW_IBM_DELETION_CUTOFF="APP.ENROLLMENT_REPORTS_PKG.IBM_DELETION_CUTOFF_NEW";
    //Ended.
    private static String strGENERATE_RENEWAL_MEMBERS="ADMINISTRATION_PKG.GENERATE_RENEWAL_MEMBERS";
    private static String strGENERATE_POLICY_RENEWAL_MEMBERS="ADMINISTRATION_PKG.gen_renewal_mbrs_admin";
    private static String strPREAUTH_LETTER_FORMAT="PAT_CLM_REPORTS_PKG.PRE_AUTH_GENERATE_LETTER";
    private static String strCLAIM_LETTER_FORMAT="PAT_CLM_REPORTS_PKG.CLAIM_COMPUTATION";
    private static String strDIAGNOSIS_DETAILS="PAT_CLM_REPORTS_PKG.GET_PAT_DIAGNOSIS";
    private static String strACTIVITY_DETAILS="PAT_CLM_REPORTS_PKG.GET_PAT_ACTIVITY";
    //SoftCopy Upload Reprots
    private static String strERROR_DETAILS="ENROLLMENT_REPORTS_PKG.ERROR_DETAILS";

    //Pre-auth Reports
    private static String strPRE_AUTH_RPT="PRE_CLM_REPORTS_PKG.PRE_AUTH_RPT ";
    private static String strMANUAL_PREATH_RPT="PRE_AUTH_PKG.MANUAL_PREATH_RPT";
    private static String strSHORTFALL_GENERATE_LETTER_preauth = "PAT_CLM_REPORTS_PKG.SHORTFALL_GENERATE_LETTER";
    private static String strPreAuthClmDtlRpt ="PRE_CLM_REPORTS_PKG.SELECT_PAT_CLM_REPORT";
    
    //Claims Report
    private static String stCLAIM_INTIMATIN_RPT="call_center_pkg.p_ins_sms_report";//KOC 1339 for mail
    private static String strMANUAL_CLAIM_RPT="CLAIMS_PKG.MANUAL_CLAIM_RPT";
    private static String strPROCESS_TIME_ANALYSIS="PRE_CLM_REPORTS_PKG.PROCESS_TIME_DETAIL";
    private static String strCLOSURE_LETTER="PRE_CLM_REPORTS_PKG.CLOSURE_LETTER";
    //private static String strSHORTFALL_GENERATE_LETTER_claims = "PRE_CLM_REPORTS_PKG.SHORTFALL_LETTER_INFO";
    private static String strSHORTFALL_GENERATE_LETTER_claims = "PAT_CLM_REPORTS_PKG.SHORTFALL_GENERATE_LETTER";

    private static String strClaims_OUTSTANDING_LIABILITY = "PRE_CLM_REPORTS_PKG.CLM_DETAIL_RPT";
    private static String strRejectionLetter="PRE_CLM_REPORTS_PKG.REJECTION_LETTER_DATA";
    private static String strMedicalOpinionSheet="PRE_CLM_REPORTS_PKG.MEDICAL_OPINION_DATA";
	 // Shortfall CR KOC1179
    private static String strSHORTFALL_GENERATE_SHORTFALL_LETTER_CLAIMS = "PRE_CLM_REPORTS_PKG.GENERATE_SHORTFALL_LETTER";
    // Shortfall CR KOC1179
    
  //added for Mail-SMS for Cigna
    private static String strCIGNA_SHORTFALL_GENERATE_LETTER_claims = "PRE_CLM_REPORTS_PKG.CIGNA_SHORTFALL_REPORT";    
  //ADDED FOR Mail-SMS Template for Cigna
    private static String strGENERATE_CIGNA_SHORTFALL_SCHEDULER_REQUEST = "PRE_CLM_REPORTS_PKG.gen_cigna_shrtfal_req_ltr";
  //Added for Mail-SMS for Cigna
    private static String strCIGNA_SHORTFALL_GENERATE_SHORTFALL_REQUEST_LETTER_CLAIMS = "PRE_CLM_REPORTS_PKG.gen_cigna_shrtfal_req_ltr1"; 

    //ADDED FOR CIGNA CLAIMHISTORY REPORT
    private static String strCIGNA_SHORTFALL_CLAIM_HISTORY = "PRE_CLM_REPORTS_PKG.CIGNA_CLM_SHRTFL_HISTORY";
    //ENDED
  //added for Mail-SMS 
    private static String strCIGNA_SUBREPORT_SHORTFALL_GENERATE_SHORTFALL_REQUEST_LETTER_CLAIMS = "PRE_CLM_REPORTS_PKG.GET_SHORTFAL_DOCS";
  		
    
    private static String strSHORTFALL_GENERATE_SHORTFALL_REQUEST_LETTER_CLAIMS="PRE_CLM_REPORTS_PKG.GENERATE_SHRTFALL_REQUEST_LTR";
        
    // Shortfall CR KOC1179 Claim Shortfall Summary Report
    private static String strCLAIMS_SHORTFALL_SUMMARY_REPORT="PRE_CLM_REPORTS_PKG.GET_SHORTFAL_REPORT";
	 //Shortfall CR KOC1179
    private static String strGENERATE_SHORTFALL_SCHEDULER_REQUEST="PRE_CLM_REPORTS_PKG.GENERATE_SHRTFALL_REQUEST_LTR";
    

    //Finance Reprots
    private static String strFinanceBatch="BATCH_REPORT_PKG.BATCH_REPORT_LIST";
    private static String strCOVERING_LETTER="BATCH_REPORT_PKG.COVERING_LETTER";
    private static String strRECONCILATION_REPORT_TEST="BATCH_REPORT_PKG.RECONCILATION_REPORT";
    private static String strBANK_TRANSACTION_REPORT="BATCH_REPORT_PKG.BANK_TRANSACTION_REPORT";
    private static String strFLOAT_TRANSACTION_REPORT="BATCH_REPORT_PKG.FLOAT_TRANSACTION_REPORT";
    private static String strCLAIMS_DETAILS_REPORT="BATCH_REPORT_PKG.CLAIMS_DETAILS_REPORT";
    private static String strCITI_CLAIMS_DETAILS_REPORT="BATCH_REPORT_PKG.CITI_CLAIMS_DETAILS_REPORT";
    
    private static String strEFT_CLAIMS_DETAILS_REPORT="BATCH_REPORT_PKG.EFT_CLAIMS_DETAILS_REPORT";
    private static String strEFT_CLAIMS_PENDING_REPORT="BATCH_REPORT_PKG.EFT_CLAIMS_PENDING_REPORT";    
    private static String strMANUAL_BATCH_LIST ="FLOAT_ACCOUNTS_PKG.MANUAL_BATCH_LIST";
    private static String strFLOAT_STATEMENT_REPORT ="BATCH_REPORT_PKG.FLOAT_STATEMENT_REPORT";
    private static String strCLAIMS_PENDING_REPORT ="BATCH_REPORT_PKG.CLAIMS_PENDING_REPORT";
	 //Changes added for Debit Note CR KOC1163
    private static String strDEBITNOTE_IBM_DAKSH_REPORT ="BATCH_REPORT_PKG.DEBIT_NOTE_FOR_IBM_DAKSH";
    // End changes added for Debit Note CR KOC1163
    private static String strTPA_COMMISSION_REPORT ="BATCH_REPORT_PKG.TPA_COMMISSION_REPORT";
    private static String strSEVENTY_SIX_COLUMN_REPORT ="BATCH_REPORT_PKG.GET_76_COL_RPT_DATA";
    //private static String strTPA_COMISSION_REPORT_SHEETS="BATCH_REPORT_PKG.TPA_COMMISSION";
    private static String strTPA_COMISSION_REPORT_SHEETS="FLOAT_ACCOUNTS_PKG.TPA_COMMISSION";
    private static String strTPA_COMISSION_INVOICE="FLOAT_ACCOUNTS_PKG.GET_INVOICE_DETAILS";

    private static String strFUT_GEN_CLAIMS_DETAILS_REPORT="BATCH_REPORT_PKG.FUT_GEN_CLAIMS_DETAILS_REPORT";
    private static String strFIN_CLAIM_COMPUTATION="BATCH_REPORT_PKG.CLAIM_COMPUTATION";
    private static String strFIN_CLAIM_COMPUTATION_LINEITEMS="BATCH_REPORT_PKG.CLAIM_COMPUTATION_LINEITEMS";
    private static String strFIN_SELECT_PENDING_DREMIT_RPT="TDS_PKG.SELECT_PENDING_DREMIT_RPT";
    private static String strFIN_SELECT_SETTLED_DREMIT_RPT="TDS_PKG.SELECT_SETTLED_DREMIT_RPT";

    
    //Coding Reports
    private static String strPendingCases_Report="CODING_PKG.PENDING_CASE_REPORT";
    private static String strCodeCleanupStatusReport="CODE_CLEANUP_PKG.CODE_CLEANUP_STATUS_RPT";

    //Call Center Reports
    private static String strCARD_HISTORY="PRE_CLM_REPORTS_PKG.CARD_HISTORY";
    private static String strPRE_AUTH_HISTORY ="PRE_CLM_REPORTS_PKG.PRE_AUTH_HISTORY";
    private static String strCLAIM_HISTORY ="PRE_CLM_REPORTS_PKG.CLAIM_HISTORY";
    private static String strPOLICY_HISTORY ="PRE_CLM_REPORTS_PKG.POLICY_HISTORY";
    private static String strPOLICY_HISTORY_SUB="PRE_CLM_REPORTS_PKG.POLICY_SUMINSURED_BREAKUP_RPT";
    private static String strCALL_HISTORY_DETAIL ="PRE_CLM_REPORTS_PKG.CALL_HISTORY_DETAIL";
    private static String strCALL_HISTORY_LIST ="PRE_CLM_REPORTS_PKG.CALL_HISTORY_LIST";
    private static String strCALL_HISTORY_ASSOCIATE_DETAIL ="PRE_CLM_REPORTS_PKG.CALL_HISTORY_ASSOCIATE_DETAIL";
    private static String strDISALLOWED_BILL_LIST="PRE_CLM_REPORTS_PKG.CLM_DISALLOWED_AMT_RPT"; 
    private static String strCLAIM_NOTIFICATIONS_DETAIL ="PRE_CLM_REPORTS_PKG.CLAIM_NOTIFICATIONS_DETAIL";
    private static String strENDORSEMENT_LIST = "ACCOUNT_INFO_PKG.SELECT_ENDORSEMENT_LIST";
    private static String strGET_REJECTION_INFO = "ONLINE_ENROLMENT_PKG.GET_REJECTION_INFO";

    private static String strCOURIER_DETAILS="PRE_CLM_REPORTS_PKG.COURIER_DETAILS";
    private static String strPRINT_CHEQUE = "FLOAT_ACCOUNTS_PKG.PRINT_CHECK";
    
    private static String strPRE_AUTH_GENERATE_LETTER ="PRE_CLM_REPORTS_PKG.PRE_AUTH_GENERATE_LETTER";
    private static String strVOUCHER_LIST ="PRE_CLM_REPORTS_PKG.VOUCHER_LIST";
    private static String strCLAIM_COMPUTATION ="PRE_CLM_REPORTS_PKG.CLAIM_COMPUTATION";
    private static String strCLAIM_COMPUTATION_LINEITEMS ="PRE_CLM_REPORTS_PKG.CLAIM_COMPUTATION_LINEITEMS";
    private static String strPRINT_ACKNOWLEDGEMENT ="PRE_CLM_REPORTS_PKG.PRINT_ACKNOWLEDGEMENT";
    private static String strPRINT_ACK_CHECK_LIST ="PRE_CLM_REPORTS_PKG.PRINT_ACK_CHECK_LIST";
    private static String strPRE_AUTH_CITI_GENERATE_LETTER = "PRE_CLM_REPORTS_PKG.PRE_AUTH_CITI_GENERATE_LETTER";
    
    //private static String strCALLCENTERCARDPRINT ="PRE_CLM_REPORTS_PKG.CALLCENTERCARDPRINT";

    private static final String strGENERATE_CARD_LIST = "PRE_CLM_REPORTS_PKG.GENERATE_CARD_LIST";
    private static String strPRE_AUTH_HIST_SHORTFALL_LIST = "PRE_CLM_REPORTS_PKG.PRE_AUTH_HIST_SHORTFALL_LIST";
    private static String strPRE_AUTH_HISTORY_QUESTIONS = "PRE_CLM_REPORTS_PKG.PRE_AUTH_HISTORY_QUESTIONS";
    private static String strPRE_AUTH_HISTORY_INVEST_LIST = "PRE_CLM_REPORTS_PKG.PRE_AUTH_HIST_INVEST_LIST";
    private static String strINVESTIGATION_DETAIL = "PRE_CLM_REPORTS_PKG.INVESTIGATION_DETAIL";
    private static String strPRE_CLM_HIST_BUFFER_LIST = "PRE_CLM_REPORTS_PKG.PRE_CLM_HIST_BUFFER_LIST";
    private static String strPRE_CLM_HIST_BUFFER_DETAIL= "PRE_CLM_REPORTS_PKG.PRE_CLM_HIST_BUFFER_DETAIL";
    private static String strCALL_HOSPITAL_LIST = "PRE_CLM_REPORTS_PKG.CALL_HOSPITAL_LIST";
    
//  web reports
    private static String strWEBLOGIN_HOSPITAL_LIST= "PRE_CLM_REPORTS_PKG.WEBLOGIN_HOSPITAL_LIST";
    
    
    //For E-card reports
    private static String strSELECT_DATA_FOR_CARD_PRINT= "ECARD_PKG.SELECT_DATA_FOR_CARD_PRINT";
    
    private static String strSELECT_DATA_FOR_CARD_PRINTInd= "ECARD_PKG.SELECT_DATA_FOR_CARD_PRINT_MEM";
    
   
//	Fax Status Report
	private static final String strFAX_STATUS_RPT = "PRE_CLM_REPORTS_PKG.FAX_STATUS_RPT";
	private static final String strPROC_GEN_CALL_PENDING_REPORT = "{CALL SCHEDULE_PKG.PROC_GEN_CALL_PENDING_REPORT(?)}";
	private static final String strPROC_GEN_MRCLM_PENDING = "SCHEDULE_PKG.PROC_GEN_MRCLM_PENDING";
	private static final String strPROC_GEN_MRCLM_PENDING_SRTFLL = "SCHEDULE_PKG.PROC_GEN_MRCLM_PENDING_SRTFLL";
	
	private static final String strGET_CALLPENDING_BRANCH_REPORT = "SCHEDULE_PKG.GET_CALLPENDING_BRANCH_REPORT";

//for Online Reports
	private static final String strEMP_CRDN_RPT_FOR_HR = "ONLINE_REPORTS_PKG.EMP_CRDN_RPT_FOR_HR";
	private static final String strCLAIMS_REGISTERED = "ONLINE_REPORTS_PKG.CLAIMS_REGISTERED_RPT";
	private static final String strBILLS_PENDING = "ONLINE_REPORTS_PKG.CASHLESS_BILLS_PENDING_HR";
	private static final String strLIST_OF_EMP_AND_DEPENDENTS = "ONLINE_REPORTS_PKG.LIST_OF_EMP_AND_DEPENDENTS";
	private static final String strINS_PREAUTH_REPORT="ONLINE_REPORTS_PKG.INS_PREAUTH_REPORT";
	private static final String strINS_CLM_REGISTER_RPT="ONLINE_REPORTS_PKG.INS_CLM_REGISTER_RPT";
	private static final String strINS_CLM_REGISTER_DTL_RPT="ONLINE_REPORTS_PKG.INS_CLM_REGISTER_DTL_RPT";
	
	//On select IDs kocbroker
	private static final String strBRO_PREAUTH_REPORT="ONLINE_REPORTS_PKG.BRO_PREAUTH_REPORT";
	private static final String strBRO_CLM_REGISTER_RPT="ONLINE_REPORTS_PKG.BRO_CLM_REGISTER_RPT";
	private static final String strBRO_CLM_REGISTER_DTL_RPT="ONLINE_REPORTS_PKG.BRO_CLM_REGISTER_DTL_RPT";
	private static final String strBRO_PREAUTH_SUMMARY_RPT="ONLINE_REPORTS_PKG.PRE_AUTH_SUMMARY_RPT";
	private static final String strDashBoard_REPORT="ONLINE_REPORTS_PKG.DASHBOARD_RPT";
	private static final String strDashBoardSub_REPORT="ONLINE_REPORTS_PKG.dashboardsub_rpt";
	private static final String strBRO_Emp_Dep_REPORT="ONLINE_REPORTS_PKG.bro_list_of_emp_and_dependents";//added new for kocbroker
	
	private static final String strONLINE_CLM_INTIMATION="APP.CALL_CENTER_PKG.P_WEB_SMS_REPORT";//KOC 1339 for mail
	
	private static final String strCheque_Covering_Letter="BATCH_REPORT_PKG.CHEQUE_COVERING_LETTER";
	private static final String strAddress_Label="BATCH_REPORT_PKG.ADDRESS_LABEL";
	private static final String strGRP_SUMMARY_REPORT="ONLINE_REPORTS_PKG.SELECT_POLICY_PLAN_SUMMARY";
	private static final String strPOLICY_DETAILS_REPORT="ONLINE_REPORTS_PKG.ACTIVE_POLICY_DETAIL";
	private static final String strPREAUTH_GRP_REPORT="ONLINE_REPORTS_PKG.SELECT_PREAUTH_DETAIL";
	private static final String strONLINE_TAT_REPORT="ONLINE_REPORTS_PKG.SELECT_ONLINE_TAT_REPORT";
	//IBM Reports
	private static final String strIBMPREAUTH_REPORT="ENROLLMENT_REPORTS_PKG.IBM_PREAUTH_REPORT";
    private static final String strIBMCLAIM_REPORT="ENROLLMENT_REPORTS_PKG.IBM_CLAIM_REPORT";
    private static final String strIBMCHILDBORN_REPORT="ENROLLMENT_REPORTS_PKG.EMP_GIVEN_BIRTH_TO_CHILD";
    private static final String strIBMREPOTIN_REPORT="ENROLLMENT_REPORTS_PKG.IBM_REOPTIN_REPORT";
    private static final String strIBMRECON_REPORT="ENROLLMENT_REPORTS_PKG.MONTHLY_RECON_REPORT";
    private static final String strIBMDAILY_OPTOUT_REPORT="ENROLLMENT_REPORTS_PKG.OPTOUT_REPORT";
    private static final String strIBMDAILY_ADDITTIONAL_COVERAGE_REPORT="ENROLLMENT_REPORTS_PKG.ADDITTIONAL_COVERAGE_REPORT";
    private static final String strIBMDAILY_PARENTAL_COVERAGE_REPORT="ENROLLMENT_REPORTS_PKG.PARENTAL_COVERAGE_REPORT";
	
	//Added for KOC-1255
    private static final String strACCENTURE_DASHBOARD_MC_REPORT = "ONLINE_REPORTS_PKG.DASHBOARD_MC";
    private static final String strACCENTURE_DASHBOARD_CC_REPORT = "ONLINE_REPORTS_PKG.DASHBOARD_CC";
    private static final String strACCENTURE_DASHBOARD_SMC_REPORT = "ONLINE_REPORTS_PKG.DASHBOARD_SMC";
    private static final String strACCENTURE_DASHBOARD_SCC_REPORT = "ONLINE_REPORTS_PKG.DASHBOARD_SCC";
    private static final String strACCENTURE_DASHBOARD_PREAUTH_REPORT = "ONLINE_REPORTS_PKG.DASHBOARD_PREAUTH";
    private static final String strACCENTURE_DASHBOARD_CALLCENTER_REPORT = "ONLINE_REPORTS_PKG.DASHBOARD_CALLCENTER";
	//Ended For KOC-1255
    //citibank reports
	private static final String strSOFT_COPY_UPLOAD_REPORT="CITIBANK_REPORTS.SOFT_COPY_UPLOAD_REPORT";
	private static final String strSOFT_COPY_CANCELLATION_REPORT="CITIBANK_REPORTS.SOFT_COPY_CANCELLATION_REPORT";
	private static final String strNOT_CANCELLED_REPORT="CITIBANK_REPORTS.NOT_CANCELLED_REPORT";	
	
	//IOB Report
	private static final String strIOB_DETAIL_REPORT= "ENROLLMENT_REPORTS_PKG.IOB_DETAIL_REPORT";
	private static final String strHDFC_CERTIFICATE= "ENROLLMENT_REPORTS_PKG.HDFC_CERTIFICATE";
	private static final String strHDFC_CERTIFICATE_MEMBER= "ENROLLMENT_REPORTS_PKG.HDFC_CERTIFICATE_MEMBER";
	
	//Coverage Reports
	private static final String strCALL_COVERAGE_LIST = "PRE_CLM_REPORTS_PKG.CALL_COVERAGE_LIST";
	private static final String strCALL_CLAUSE_ASSOC_DOC_LIST = "PRE_CLM_REPORTS_PKG.CALL_CLAUSE_ASSOC_DOC_LIST";
	
	//TDS Daily Report
	private static final String strTDS_DAILY_RPT = "{CALL BATCH_REPORT_PKG.TDS_DAILY_RPT(?)}";
	private static final String strTDS_DETAIL_REPORT = "BATCH_REPORT_PKG.TDS_DETAIL_REPORT";
	private static final String strSELECT_EMP_WITHOUT_SUM_LIST = "ADMINISTRATION_PKG.SELECT_EMP_WITHOUT_SUM_LIST";
	private static final String strDAILY_TRANSFER_SUMMARY_REPORT = "BATCH_REPORT_PKG.DAILY_TRANSFER_SUMMARY_REPORT";
	private static final String strMONTHLY_REMIT_SUMARY_REPORT = "BATCH_REPORT_PKG.MONTHLY_REMIT_SUMARY_REPORT";
	private static final String strANNEXURE_I26Q_REPORT = "BATCH_REPORT_PKG.ANNEXURE_I26Q_REPORT";
	private static final String strCHALLAN_DETAILS_Q_REPORT = "BATCH_REPORT_PKG.CHALLAN_DETAILS_Q_REPORT";
		
	//TDS Hospital Report
	private static final String strHOSPITAL_TDS__RPT = "{CALL TDS_PKG.SELECT_HOSPITAL_TDS_RPT(?)}";
	
	//Universal Sampo Pending Report - 29/01/2010
	private static final String strUNIVERSAL_SOMPO_PENDING_REPORT = "MIS_V2_FIN_REPORTS_PKG.UNIVERSAL_SOMPO_PENDING_REPORT";
	
	//Cheque printing in Maintanance module -04/05/2010
	private static final String strPrintChequeInfo=" BATCH_REPORT_PKG.PRINT_CHEQUE_INFO";
	
	//TDS Certificate Generation -11/05/2010
	private static final String strSelectTdsCert= "TDS_PKG.SELECT_TDS_CERTIFICATE";
	private static final String strAckQuarterInfo= "TDS_PKG.GET_ACK_QUARTER_INFO";
	private static final String strTdsAnneSummary= "TDS_PKG.TDS_ANNEXURE_SUMMARY";
	private static final String strAnneClmWiseBreakup= "TDS_PKG.ANNEXURE_CLAIM_WISE_BREAKUP";
	private static final String strTdsCovLetter= "TDS_PKG.SELECT_TDS_COV_LETTER";
	
	private static final String strJOB_STATUS_RPT ="SCHEDULE_PKG.SELECT_JOB_DETAIL";
	// Summary Report CR KOC1224
	private static final String strONLINE_SUMMARY_REPORT = "ONLINE_REPORTS_PKG.EMP_MEM_SUMMARY";
	//added for 2koc 
	private static final String strCourierDetails = "COURIER_PKG.GENERATE_COURIER_LETTER";
	//end added for 2koc 
	//KOC 1353 for payment report
	private static final String strONLINE_PAYMENT_INS_REPORT = "ONLINE_REPORTS_PKG.INS_PAYMENT_REPORT";
	//KOCPreauthreport 
	private static final String strINS_PREAUTH_CIGNA_REPORT = "ONLINE_REPORTS_PKG.INS_PREAUTH_CIGNA_REPORT";
	private static final String strINS_CLAIM_CIGNA_REPORT = "ONLINE_REPORTS_PKG.INS_CLAIM_CIGNA_REPORT";
	private static final String strINS_POLICY_CIGNA_REPORT = "ONLINE_REPORTS_PKG.INS_POLICY_CIGNA_REPORT";
	//KOC 1353 for payment report
	
	//SELF FUND
    private static String strPRE_AUTH_LETTER ="HOSP_DIAGNOSYS_PKG.gen_bill_report1";
    private static String strDiagonisTariffHistory ="HOSP_DIAGNOSYS_PKG.gen_bill_report";
	
    //intX
    private static String strCUSTOMERCALLBACKREPORT	=	"SCHEDULE_PKG.PROC_GEN_CALL_PENDING_REPORT";
	private static final String strPREAUTHUTILIZATION = "{CALL online_reports_pkg.authutilazation_rpt(?,?,?,?,?)}";
	private static final String strPRPAUTHUTILIZATION = "{CALL PKG_MIS_REPORTS.mis_report_preauth(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strCLRAUTHUTILIZATION = "{CALL PKG_MIS_REPORTS.mis_report_clms(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strERPAUTHUTILIZATION = "{CALL PKG_MIS_REPORTS.mis_report_Enrolmnt(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	//insLoginReports
	private static final String strCLAIMUTILIZATIONREP   = "ONLINE_EXTERNAL_INFO_PKG.CLAIMUTILAZATION_RPT";
	private static final String strAUTHUTILIZATIONREP 	 = "ONLINE_EXTERNAL_INFO_PKG.AUTHUTILAZATION_RPT";
	private static final String strTATFORCLAIMS			 = "ONLINE_EXTERNAL_INFO_PKG.CLAIM_TAT_RPT1";
	private static final String strTATForPreAuth			 = "ONLINE_EXTERNAL_INFO_PKG.PREAUTH_TAT_RPT";
	private static final String strTRRForPreAuth		 = "ONLINE_EXTERNAL_INFO_PKG.TRR_Premium_rpt";
	private static final String strTATFSCORPOARTE		 = "ONLINE_EXTERNAL_INFO_PKG.Enrollment_COR_TAT_rpt";
	private static final String strTATFSINDIVIDUAL		 = "ONLINE_EXTERNAL_INFO_PKG.Enrollment_IND_TAT_rpt";


	private static final String strDETAILEDREPORT	  = "{CALL BATCH_REPORT_PKG.CLAIMS_DETAILS_REPORT(?,?)}";
	private static final String strPENDINGREPORT	  = "{CALL BATCH_REPORT_PKG.CLAIMS_PENDING_REPORT(?,?)}";
	private static final String strBANKHOSPITALREPORT = "{CALL BATCH_REPORT_PKG.HOSP_BANK_DTL_REPORT(?,?)}";
	private static final String strBANKPOLICYREPORT	  = "{CALL BATCH_REPORT_PKG.POLICY_BANK_DTL_REPORT(?,?)}";
	private static final String strEXCHANGEREPORT	= 	"{CALL BATCH_REPORT_PKG.GENERATE_CRNCY_EXCHNG_RPT(?,?)}";
	
	private static final String strCLAIM_SELECT_REIM_CLAIM_LETTER="PAT_CLM_REPORTS_PKG.REIM_CLM_LETTE";
	private static final String  strMEMBER_DIAGNOSIS_DETAILS="PAT_CLM_REPORTS_PKG.get_pat_diagnosis";
	
	private static final String strCHEQUEWISEREPORT = "HOSPITAL_PKG.SELECT_CHKWISE_REPORT";
	private static final String strBATCHRECONCILIATION	= "HOSPITAL_PKG.SELECT_BATCH_RECON_SMRY_RPT";
	private static final String strBATCHINVOICE			= "HOSPITAL_PKG.SELECT_BATCH_INVOICE_RPT";
	private static final String strGENERATEPREAUTHLETTER= "HOSPITAL_PKG.SELECT_PREAUTH_DETAILS";
	private static final String strFINACNEDASHBOARD		= "HOSPITAL_PKG.DASHBOARD_ACCOUNT";
	private static final String strINVOICEWISEREPORT	= "HOSPITAL_PKG.SELECT_INVOICEWISE_REPORT";
	private static final String strOVERDUEREPORT		= "HOSPITAL_PKG.SELECT_OVERDUE_RPT";
	private static final String strONLINEPREAUTHFORM	= "HOSP_DIAGNOSYS_PKG.GET_HOSP_DETAILS";
	private static final String strONLINEDENTALFORM	= "HOSP_DIAGNOSYS_PKG.dental_form_event";//Added For Print Dental Form

	private static final String strPREAUTH_REFERRAL_REP	= "PREAUTH_REFERRAL_LETTER_PKG.GENERATE_REFERRAL_LETTER";
	
	private static final String strBRO_CLAIMUTILIZATIONREP = "ONLINE_EXTERNAL_INFO_PKG.CLAIMUTILAZATION_BRO_RPT";
	private static final String strBRO_AUTHUTILIZATIONREP = "ONLINE_EXTERNAL_INFO_PKG.AUTHUTILAZATION_BRO_RPT";
	private static final String strBRO_TATFORCLAIMS = "ONLINE_EXTERNAL_INFO_PKG.CLAIM_TAT_BRO_RPT";
	
	private static final String strBRO_TATFORPREAUTH = "ONLINE_EXTERNAL_INFO_PKG.PREAUTH_TAT_BRO_RPT";
	private static final String strBRO_TECHNICALRESULTREPORT = "ONLINE_EXTERNAL_INFO_PKG.TRR_PREMIUM_BRO_RPT";
	private static final String strBRO_TATFSCORPOARTE = "ONLINE_EXTERNAL_INFO_PKG.ENROLLMENT_COR_TAT_BRO_RPT";
	private static final String strBRO_TATFSINDIVIDUAL = "ONLINE_EXTERNAL_INFO_PKG.ENROLLMENT_IND_TAT_BRO_RPT";
	private static final String strBRO_HIPA = "ONLINE_EXTERNAL_INFO_PKG.HIPA";
	private static final String strHEALTH_INSURANCE_QUOTE = "online_policy_copy_issue.generate_policy_quote";
	private static final String strBENEFIT_DESIGN_SECTION = "online_policy_copy_issue.get_benifit_details";
	private static final String strPREMIUM_RATES_SECTION = "Intx.premium_calculation.select_calculate_premium";
	private static final String strProviderPreAuthReports = "hospital_pkg.PRVDR_PAT_RPT";
	private static final String strProviderClaimReports = "hospital_pkg.PRVDR_CLM_RPT";
	
    private static String strSELECT_MOB_CARD_PRINT= "VIRE_MOB_APP_PCK.ECARD_DETAILS";
    private static String strVIEW_INPUT_SUMMARY= "POLICY_COPY_ISSUE_AUTOMATION.GENERATE_QUOTE";
    
    private static String strSELECT_MEDICAL_CERTIFICATE= "POLICY_ENROLLMENT_PKG.dwnld_travelcertificate";//Added for MEDICAL CERTIFICATE
    private static String strFIN_CLM_SETLD_RPT= "BATCH_REPORT_PKG.CLAIMS_SETTELED_REPORT ";
    private static String strFIN_CLM_INP_RPT= "BATCH_REPORT_PKG.CLAIMS_INPROGRESS_REPORT ";
    private static String strFIN_CLM_OUT_RPT= "BATCH_REPORT_PKG.CLAIMS_OUTSTANDING_REPORT ";
    private static String strVIEW_QUOTATION= "POLICY_COPY_ISSUE_AUTOMATION.GENERATE_POL_COPY";  

    private static final String strPBM_REPORTS_ALL	= "hosp_pbm_pkg.PBM_CLM_DRUG_RPT";
    
    private static final String strClaim_Upload_Error_Log = "CLAIM_BULK_UPLOAD.GET_ERROR_LOG";
    private static final String strTariff_Upload_Error_Log = "TARIFF_BULK_UPLOAD.GET_ERROR_LOG";

    
    private static final String strEmplExportExcelData = "MEMBER_LOGIN_PKG.GET_NETWORK_PROVIDER_LIST";
    private static final String strClaimDetailedReport = "CLAIM_PKG.PRVDR_CLM_RPT";
	//private static final String strConnection ="Connection";
	//private static final String strEcardConnection ="EcardConnection";
	
    private static String strRoutine_REPORT ="BATCH_REPORT_PKG.CLAIMS_ROUTINE_REPORT";
    
    private static final String strAuditClaimReportGenerate = "{CALL BATCH_REPORT_PKG.AUDIT_CLAIM_REPORT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String strAuditPreAuthReportGenerate = "{CALL BATCH_REPORT_PKG.AUDIT_PREAUTH_REPORT(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String strUploadClaimAuditReport = "{CALL AUDIT_REPORT_UPLOAD.SAVE_AUDIT_XML(?,?,?,?,?)}";
    private static final String strUploadClaimAuditReportLog = "{CALL AUDIT_REPORT_UPLOAD.GET_AUDIT_LOG(?,?,?)}";
    private static final String strUploadCFDReport = "{CALL CLAIM_PKG.SAVE_CFD_XML(?,?,?,?,?)}";
    private static final String strUploadCFDReportLog = "{CALL CLAIM_PKG.GET_CFD_ERROR_LOG(?,?,?)}";
     private static final String strCFDReportGenerate = "{CALL CLAIM_PKG.GENERATE_REPORTS_FOR_CFD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    
    private static final String strTATRpt	= "MIS_V2_REPORTS_PKG.select_pat_tat_report";
    private static final String strProductivityRpt	= "MIS_V2_REPORTS_PKG.select_pat_tat_report";
    private static final String strDetailedRpt	= "MIS_V2_REPORTS_PKG.SELECT_PAT_ACT_DETLD_RPRT";
    private static final String strActivityRpt	= "MIS_V2_REPORTS_PKG.SELECT_PAT_ACT_DETLD_RPRT";
    
    private static final String strPreApprovalReports	= "MIS_V2_REPORTS_PKG.select_pat_tat_prod_report";
    
   private static final String strTATReports = "{CALL MIS_V2_REPORTS_PKG.SELECT_PAT_TAT_REPORT(?,?,?,?,?)}";
   private static final String strProductivityReports = "{CALL MIS_V2_REPORTS_PKG.SEL_PAT_PRODUCTIVITY_RPRT(?,?,?,?)}";
   private static final String strFinancepreauthReport	= "BATCH_REPORT_PKG.pre_approval_report";
   private static final String strUnderwritingReport ="batch_report_pkg.claim_Underwriting_Year_report";
   private static final String str_PolicyNoList= "SELECT A.POLICY_SEQ_ID,A.POLICY_NUMBER FROM TPA_ENR_POLICY A JOIN TPA_GROUP_REGISTRATION B ON(A.GROUP_REG_SEQ_ID=B.GROUP_REG_SEQ_ID) WHERE B.GROUP_ID=? AND A.COMPLETED_YN='Y' AND A.DELETED_YN='N'";
   private static final String strActivityLogReports = "hospital_empanel_pkg.select_payment_dur_logs";
   private static final String strGetPaymentTermAgr = "SELECT DISTINCT L.HOSP_SEQ_ID,L.PAYMENT_DUR_AGR_DAYS FROM TPA_HOSP_PMNT_AGRD_TRM_LOG L JOIN TPA_HOSP_INFO HI ON (L.HOSP_SEQ_ID=HI.HOSP_SEQ_ID) WHERE TRUNC(SYSDATE) BETWEEN L.START_DATE AND NVL(L.END_DATE,TRUNC(SYSDATE+1)) AND HI.HOSP_SEQ_ID=?";
   private static final String strActivityLogList	="{CALL hospital_empanel_pkg.select_payment_dur_logs(?,?)}";
   private static final String strBordereauReports = "{CALL REINSURER_EMPANEL_PKG.VIEW_REINS_REPORTS(?,?,?,?,?,?,?,?)}";
   private static final String strCamReportGenerate = "{CALL CLAIM_PKG.Generate_campaign_report(?,?,?,?,?,?,?,?,?,?,?,?)}";
   
   private static final String strClaimDiscountActivityReport = "claim_pkg.PRVDR_CLM_DISC_RPT";
   private static final String strPBMClaimReports = "hosp_pbm_pkg.PBM_CLM_RPT";
   /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strReportID,String strParameter) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        String strProcedureName="";
       
        strProcedureName =getProcedureName(strReportID);
        

        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }//end of if(strProcedureName==null)
        OracleCachedRowSet crs = null;
        try{
            String strCall = "{CALL "+strProcedureName.trim()+"(?,?)}";
           
            log.info("strCall:::::::::::::"+strCall);
            log.info("strParameter:::::::::::::"+strParameter);
            System.out.println("strReportID::::::::::::::"+strReportID);
            crs = new OracleCachedRowSet();
            if(strReportID.equalsIgnoreCase("FinClmSetldRpt") || strReportID.equalsIgnoreCase("FinClmInpRpt")//For Finance Vings
            		||strReportID.equalsIgnoreCase("FinClmOutRpt") || strReportID.equalsIgnoreCase("FinDetRpt")//For Finance Vings
            		||strReportID.equalsIgnoreCase("FinPenRpt") || strReportID.equalsIgnoreCase("RoutineRpt")//For Finance Vings      		
            		||strReportID.equalsIgnoreCase("providerLoginPreAuthReports") || strReportID.equalsIgnoreCase("providerLoginClaimReports")//For Provider Login
            		||strReportID.equalsIgnoreCase("pbmClaimDrugReport")//For PBM Login 
            		||strReportID.equalsIgnoreCase("ActivePolicyDetails") || strReportID.equalsIgnoreCase("ListEmpDepPeriod")//For Hr Login
            		||strReportID.equalsIgnoreCase("GrpPreAuthRpt") || strReportID.equalsIgnoreCase("OnlineSummaryRpt") || strReportID.equalsIgnoreCase("ClaimDetailedReport") || strReportID.equalsIgnoreCase("ClmRegd")|| strReportID.equalsIgnoreCase("ClaimDiscountActivityReport")
            		||strReportID.equalsIgnoreCase("ActivityLogRpt")){//For Hr Login
            		conn = ResourceManager.getMISConnection();
                      		
            }else{
            		conn = ResourceManager.getConnection();//getTestConnection();
            }
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,strParameter);
            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(2); 
            
            if(rs !=null)
            {       
            	crs.populate(rs);
            	 
            }//end of if(rs !=null)
            return crs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getReport()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;        		
        	}//end of finally
        }//end of finally
    }//end of getReport(String strReportID,String strParameter)
    //Added for IBM....9.2

	//ADDED BY PRAVEEN only for IBM Reports.
		public ResultSet getReport(String strReportID,String strParameter1,String strParameter2,String strParameter3) throws TTKException {
		    	Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs = null;
		        String strProcedureName="";
		        strProcedureName =getProcedureName(strReportID);
		        if(strProcedureName==null)
		        {
		        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
		        }//end of if(strProcedureName==null)
		        OracleCachedRowSet crs = null;
		        try{
		            String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?)}";
		            crs = new OracleCachedRowSet();
		            conn = ResourceManager.getConnection();//getTestConnection();
		            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
		            cStmtObject.setString(1,strParameter1);
		            cStmtObject.setString(2,strParameter2);
		            cStmtObject.setString(3,strParameter3);

		            cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
		            cStmtObject.execute();
		            rs = (java.sql.ResultSet) cStmtObject.getObject(4);
		            if(rs !=null)
		            {
		            	crs.populate(rs);
		            }//end of if(rs !=null)
		            return crs;
		        }//end of try
		        catch (SQLException sqlExp)
		        {
		            throw new TTKException(sqlExp, "tTkReports");
		        }//end of catch (SQLException sqlExp)
		        catch (Exception exp)
		        {
		            throw new TTKException(exp, "tTkReports");
		        }//end of catch (Exception exp)
		        finally
		        {
		        	/* Nested Try Catch to ensure resource closure */
		        	try // First try closing the Statement
		        	{
		        		try
		        		{
		        			if (rs != null) rs.close();
		        		}//end of try
		        		catch (SQLException sqlExp)
		        		{
		        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
		        			throw new TTKException(sqlExp, "policy");
		        		}//end of catch (SQLException sqlExp)
		        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		        		{
		        			try
		        			{
		        				if (cStmtObject != null) cStmtObject.close();
		        			}//end of try
		        			catch (SQLException sqlExp)
		        			{
		        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
		        				throw new TTKException(sqlExp, "tTkReports");
		        			}//end of catch (SQLException sqlExp)
		        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		        			{
		        				try
		        				{
		        					if(conn != null) conn.close();
		        				}//end of try
		        				catch (SQLException sqlExp)
		        				{
		        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
		        					throw new TTKException(sqlExp, "tTkReports");
		        				}//end of catch (SQLException sqlExp)
		        			}//end of finally Connection Close
		        		}//end of try
		        	}//end of try
		        	catch (TTKException exp)
		        	{
		        		throw new TTKException(exp, "tTkReports");
		        	}//end of catch (TTKException exp)
		        	finally // Control will reach here in anycase set null to the objects
		        	{
		        		rs = null;
		        		cStmtObject = null;
		        		conn = null;
		        	}//end of finally
		        }//end of finally
		    }//end of getReport(String strReportID,String strParameter)




	//Ended..
		
		//ADDED BY PRAVEEN only for IBM Reports.
		public ResultSet getReport(String strReportID,String strParameter1,String strParameter2,String strParameter3,String strParameter4) throws TTKException {

		    	Connection conn = null;
		    	CallableStatement cStmtObject=null;
		        ResultSet rs = null;
		        String strProcedureName="";
		        strProcedureName =getProcedureName(strReportID);
                             
		        if(strProcedureName==null)
		        {
		        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
		        }//end of if(strProcedureName==null)
		        OracleCachedRowSet crs = null;
		        try{
		            String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?)}";
		            crs = new OracleCachedRowSet();
		            conn = ResourceManager.getConnection();//getTestConnection();
		            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
		            cStmtObject.setString(1,strParameter1);
		            cStmtObject.setString(2,strParameter2);
		            cStmtObject.setString(3,strParameter3);
		            cStmtObject.setString(4,strParameter4);

		            cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
		            cStmtObject.execute();
		            rs = (java.sql.ResultSet) cStmtObject.getObject(5);
		            if(rs !=null)
		            {
		            	crs.populate(rs);
		            }//end of if(rs !=null)
		            return crs;
		        }//end of try
		        catch (SQLException sqlExp)
		        {
		            throw new TTKException(sqlExp, "tTkReports");
		        }//end of catch (SQLException sqlExp)
		        catch (Exception exp)
		        {
		            throw new TTKException(exp, "tTkReports");
		        }//end of catch (Exception exp)
		        finally
		        {
		        	/* Nested Try Catch to ensure resource closure */
		        	try // First try closing the Statement
		        	{
		        		try
		        		{
		        			if (rs != null) rs.close();
		        		}//end of try
		        		catch (SQLException sqlExp)
		        		{
		        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
		        			throw new TTKException(sqlExp, "policy");
		        		}//end of catch (SQLException sqlExp)
		        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
		        		{
		        			try
		        			{
		        				if (cStmtObject != null) cStmtObject.close();
		        			}//end of try
		        			catch (SQLException sqlExp)
		        			{
		        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
		        				throw new TTKException(sqlExp, "tTkReports");
		        			}//end of catch (SQLException sqlExp)
		        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		        			{
		        				try
		        				{
		        					if(conn != null) conn.close();
		        				}//end of try
		        				catch (SQLException sqlExp)
		        				{
		        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
		        					throw new TTKException(sqlExp, "tTkReports");
		        				}//end of catch (SQLException sqlExp)
		        			}//end of finally Connection Close
		        		}//end of try
		        	}//end of try
		        	catch (TTKException exp)
		        	{
		        		throw new TTKException(exp, "tTkReports");
		        	}//end of catch (TTKException exp)
		        	finally // Control will reach here in anycase set null to the objects
		        	{
		        		rs = null;
		        		cStmtObject = null;
		        		conn = null;
		        	}//end of finally
		        }//end of finally
		    }//end of getReport(String strReportID,String strParameter)




	//Ended..
	
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strReportID) throws TTKException
    {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        OracleCachedRowSet crs = null;
        try{
            crs = new OracleCachedRowSet();
            conn = ResourceManager.getConnection();
            //cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPROC_GEN_CALL_PENDING_REPORT);
            if(strReportID.equals("TDSSchedRpt"))
            {
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTDS_DAILY_RPT);
            }//end of if(strReportID.equals("CallPenRpt"))
            else if(strReportID.equals("HospitalTDSRpt"))
            {
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strHOSPITAL_TDS__RPT);
            }//end of else if(strReportID.equals("HospitalTDSRpt"))
            else if (strReportID.equals("PreAuthUtilization"))
    		{
    			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPREAUTHUTILIZATION);
    		}//end of else if (strReportID.equals("CustCallBack"))//added as per intX preauth Utilization report 
            else
            {
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPROC_GEN_CALL_PENDING_REPORT);
            }//end of else
            
            cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(1);            
            if(rs !=null)
            {
             	crs.populate(rs);
            }//end of if(rs !=null)
            return crs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;        		
        	}//end of finally
        }//end of finally
    }//end of getReport(String strReportID)
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getEcardReport(String strReportID,String strParameter) throws TTKException {
    	
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        String strProcedureName="";
        strProcedureName =getProcedureName(strReportID);        
        
        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }//end of if(strProcedureName==null)
        OracleCachedRowSet crs = null;
        try{
            String strCall = "{CALL "+strProcedureName.trim()+"(?,?)}";
            //log.debug("strCall is " + strCall);
            crs = new OracleCachedRowSet();
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,strParameter);
            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(2);
            if(rs !=null)
            {
             	crs.populate(rs);
            }//end of if(rs !=null)
            return crs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getEcardReport()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getEcardReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getEcardReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
        }//end of finally
    }//end of getEcardReport(String strReportID,String strParameter)
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getIEcardReport(String strReportID,String strParameter,long strMemParameter) throws TTKException {
    	
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        String strProcedureName="";
        strProcedureName =getProcedureName(strReportID);        
        
        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }//end of if(strProcedureName==null)
        OracleCachedRowSet crs = null;
        try{
            String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?)}";
            //log.debug("strCall is " + strCall);
            crs = new OracleCachedRowSet();
          //  conn = ResourceManager.getEcardConnection();
            conn = ResourceManager.getConnection();

            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,strParameter);
            cStmtObject.setLong(2,strMemParameter);
            cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(3);
            if(rs !=null)
            {
             	crs.populate(rs);
            }//end of if(rs !=null)
            return crs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getEcardReport()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getEcardReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getEcardReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
        }//end of finally
    }//end of getEcardReport(String strReportID,String strParameter)
//Added as per KOC 1179 change Request
	    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strReportID,String strParameter,StringBuffer shotfallType) throws TTKException {
    	
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        String strProcedureName="";
        strProcedureName =getProcedureName(strReportID);
        
        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }//end of if(strProcedureName==null)
        OracleCachedRowSet crs = null;
        try{
            String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?)}";
            crs = new OracleCachedRowSet();
            conn = ResourceManager.getConnection();//getTestConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,strParameter);
			 cStmtObject.setString(2,shotfallType.toString());
            cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(3);  
            if(rs !=null)
            {
            	crs.populate(rs);
            }//end of if(rs !=null)
            return crs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;        		
        	}//end of finally
        }//end of finally
    }//end of getReport(String strReportID,String strParameter,StringBuffer shotfallType)

	    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strReportID,String strParameter,int indexCursor) throws TTKException {
    	
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	String strProcedureName="";
    	String strCall = null;
    	strProcedureName =getProcedureName(strReportID);
    	if(strProcedureName==null)
    	{
    		throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
    	}//end of if(strProcedureName==null)
    	OracleCachedRowSet crs = null;
    	try{
    		if(strReportID.equals("EndrImpactReport") || strReportID.equals("EFTAcceAnne")){
    			strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?)}";
    		}//end of if(strReportID.equals("CallImpactReport"))
    		if(strReportID.equals("PremiumRates"))
    		{
    			strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?)}";
    		}
    		else {
    			strCall = "{CALL "+strProcedureName.trim()+"(?,?,?)}";
    		}//end of else
    		crs = new OracleCachedRowSet();
    		conn = ResourceManager.getConnection();//getTestConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
    		cStmtObject.setString(1,strParameter);
    		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
    		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
    		if(strReportID.equals("EndrImpactReport") || strReportID.equals("EFTAcceAnne"))
    		{
    			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
    			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
    		}//end of if(strReportID.equals("EndrImpactReport"))
    		
    		if(strReportID.equals("PremiumRates"))
    		{
    			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
    		}
    		cStmtObject.execute();
    		rs = (java.sql.ResultSet) cStmtObject.getObject(indexCursor);
    		if(rs !=null)
    		{
    			crs.populate(rs);
    		}//end of if(rs !=null)
    		return crs;
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "tTkReports");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "tTkReports");
    	}//end of catch (Exception exp)
    	finally
    	{
    		/* Nested Try Catch to ensure resource closure */ 
    		try // First try closing the Statement
    		{
    			try
    			{
    				if (rs != null) rs.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
    				throw new TTKException(sqlExp, "policy");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
    			{
    				try
    				{
    					if (cStmtObject != null) cStmtObject.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
    					throw new TTKException(sqlExp, "tTkReports");
    				}//end of catch (SQLException sqlExp)
    				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    				{
    					try
    					{
    						if(conn != null) conn.close();
    					}//end of try
    					catch (SQLException sqlExp)
    					{
    						log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
    						throw new TTKException(sqlExp, "tTkReports");
    					}//end of catch (SQLException sqlExp)
    				}//end of finally Connection Close
    			}//end of try
    		}//end of try
    		catch (TTKException exp)
    		{
    			throw new TTKException(exp, "tTkReports");
    		}//end of catch (TTKException exp)
    		finally // Control will reach here in anycase set null to the objects 
    		{
    			rs = null;
    			cStmtObject = null;
    			conn = null;
    		}//end of finally
    	}//end of finally
    }//end of getReport(String strReportID,String strParameter)
    
    /**
     * This method returns the ResultSet Array, which contains Reports data which are populated from the database
     * @param strReportID String which is a ReportID which has to be called to get data.
     * @param strParameter String which is a concatinated Parameters which has to be called to get data.
     * @param strNoOfCursors String which contains the No. Of Cursors.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */    
    public ResultSet[] getReport(String strReportID,String strParameter, String strNoOfCursors) throws TTKException {

    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs[] = null;
    	String strProcedureName="";
    	strProcedureName =getProcedureName(strReportID);
    	int intNoOfCursors = Integer.parseInt(strNoOfCursors);
    	
   
    	if(strProcedureName==null)
    	{
    		throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
    	}//end of if(strProcedureName==null)
    	OracleCachedRowSet crs[] = null;

    	try{
    		if(strReportID.equals("TPACommissionRpt"))
    		{
        		StringTokenizer strTok = new StringTokenizer(strParameter,"|");
        		String strArray[] = new String[5];
        		for(int i=0;i<5 && strTok.hasMoreTokens();i++) {
        		
        	        strArray[i] = (String)strTok.nextElement();
        	        
        	     }//end of for(int i=0;i<4 && strTok.hasMoreTokens();i++)
        		
        		String strInvoiceSeqID = strArray[0];
        		String strPolicySeqID = strArray[1];
        		String strStartDate = strArray[2];
        		String strEndDate = strArray[3];
        		
        		Float flAddedBy = new Float(strArray[4]);
        		

        		String strInputParameter = "|"+strInvoiceSeqID+"|"+strPolicySeqID+"|";
        		
        		String strCall = "{CALL "+strProcedureName.trim();
        		System.out.println("strCall:::::::::::"+strCall);
                String strParameters = "(?,?,?,?";
                for(int i=0;i<intNoOfCursors+1;i++){ //one more for OUT parameter for batch seq id 
                	strParameters += ",?";
                }//end of for(int i=0;i<intNoOfCursors+1;i++)
                strParameters += ")}";
                strCall += strParameters;
               
                
                crs = new OracleCachedRowSet[intNoOfCursors];
                rs = new ResultSet[intNoOfCursors];
                conn = ResourceManager.getConnection();//getTestConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                cStmtObject.setString(1,strInputParameter);
                cStmtObject.setString(2,strStartDate);
                cStmtObject.setString(3,strEndDate);
                cStmtObject.setFloat(4,flAddedBy);
                for(int i=5;i<intNoOfCursors+5;i++){
                	cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
                }//end of for(int i=5;i<intNoOfCursors+5;i++)
                cStmtObject.registerOutParameter(8,OracleTypes.NUMBER);//17 before comment
                cStmtObject.execute();
                for(int index=0;index<intNoOfCursors;index++) {
                	rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+5);
                	crs[index] = new OracleCachedRowSet();
                	if(rs[index]!=null){
                		crs[index].populate(rs[index]);
                	}//end of if(rs[index]!=null){ 
                }//end of for(int index=0;index<intNoOfCursors;index++)
                return crs;
            }//end of if(strReportID.equals("TPACommissionRpt"))
    		else if(strReportID.equals("FinTPAComm"))
    		{
    			String strCall = "{CALL "+strProcedureName.trim();
    			System.out.println("strCall:::::::::::"+strCall);
    			String strParameters = "(?";
    			for(int i=0;i<intNoOfCursors;i++){ //one more for OUT parameter for batch seq id 
    				strParameters += ",?";
    			}//end of for(int i=0;i<intNoOfCursors+1;i++)
    			strParameters += ")}";
    			strCall += strParameters;
    			crs = new OracleCachedRowSet[intNoOfCursors];
    			rs = new ResultSet[intNoOfCursors];
    			conn = ResourceManager.getConnection();//getTestConnection();
    			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
    			cStmtObject.setString(1,strParameter);
    			for(int i=2;i<intNoOfCursors+2;i++){
    				cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
    			}//end of for(int i=2;i<intNoOfCursors+2;i++)
    			cStmtObject.execute();
    			for(int index=0;index<intNoOfCursors;index++) {
    				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+2);
    				crs[index] = new OracleCachedRowSet();
    				if(rs[index]!=null){
    					crs[index].populate(rs[index]);
    				}//end of if(rs[index]!=null)
    			}//end of for(int index=0;index<intNoOfCursors;index++)
    			return crs;    			
    		}//end of else if(strReportID.equals("FinTPAComm"))
			// Summary Report KOC1224
    		else if(strReportID.equals("OnlineSummaryRpt"))
        	{
        			String strCall = "{CALL "+strProcedureName.trim();
        			System.out.println("strCall:::::::::::"+strCall);
        			String strParameters = "(?";
        			for(int i=0;i<intNoOfCursors;i++){ //one more for OUT parameter for batch seq id 
        				strParameters += ",?";
        			}//end of for(int i=0;i<intNoOfCursors+1;i++)
        			strParameters += ")}";
        			
        			strCall += strParameters;
        			crs = new OracleCachedRowSet[intNoOfCursors];
        			rs = new ResultSet[intNoOfCursors];
        			conn = ResourceManager.getConnection();//getTestConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
        			cStmtObject.setString(1,strParameter);
        			for(int i=2;i<intNoOfCursors+2;i++){
        				
        				cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
        			}//end of for(int i=2;i<intNoOfCursors+2;i++)
        			cStmtObject.execute();
        			for(int index=0;index<intNoOfCursors;index++) {
        				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+2);
        				crs[index] = new OracleCachedRowSet();
        				if(rs[index]!=null){
        					crs[index].populate(rs[index]);
        				}//end of if(rs[index]!=null)
        			}//end of for(int index=0;index<intNoOfCursors;index++)
        			return crs;    			
        		}//end of else if(strReportID.equals("OnlineSummaryReport"))
			 else if(strReportID.equalsIgnoreCase("claimintimation") || strReportID.equalsIgnoreCase("preauthintimation") )
        	{
        		String strCall = "{CALL "+strProcedureName.trim();
        		System.out.println("strCall:::::::::::"+strCall);
    			String strParameters = "(?";
    			for(int i=0;i<intNoOfCursors;i++){
    				strParameters += ",?";
    			}//end of for(int i=0;i<intNoOfCursors;i++)
    			strParameters += ")}";
    			strCall += strParameters;
    		
    			crs = new OracleCachedRowSet[intNoOfCursors];
    			rs = new ResultSet[intNoOfCursors];
    			conn = ResourceManager.getConnection();//getTestConnection();
    			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
    			cStmtObject.setString(1,strParameter);
    			for(int i=2;i<intNoOfCursors+2;i++){
    				cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
    			}//end of for(int i=2;i<intNoOfCursors+2;i++)
    			cStmtObject.execute();
    			for(int index=0;index<intNoOfCursors;index++) {
    				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+2);
    				crs[index] = new OracleCachedRowSet();
    				if(rs[index]!=null){
    					crs[index].populate(rs[index]);
    				}//end of if(rs[index]!=null)
    			}//end of for(int index=0;index<intNoOfCursors;index++)
    			
    			return crs;  
         	}// end of 	else if(strReportID.equalsIgnoreCase("Intimation"))
            
            else if(strReportID.equals("AddressingDocuments"))
        	{
    			   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?,?,?)}";
    			   System.out.println("strCall:::::::::::"+strCall);
        			crs = new OracleCachedRowSet[intNoOfCursors];
        			rs = new ResultSet[intNoOfCursors];
        			conn = ResourceManager.getConnection();//getTestConnection();
        			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
        			cStmtObject.setString(1,"AddressingDocuments");
        			cStmtObject.setString(2,"AddressingDocuments");
        			for(int i=3;i<intNoOfCursors+3;i++){
        				cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
        			}//end of for(int i=2;i<intNoOfCursors+2;i++)
        			cStmtObject.execute();
        			for(int index=0;index<intNoOfCursors;index++) {
        				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+3);
        				crs[index] = new OracleCachedRowSet();
        				if(rs[index]!=null){
        					crs[index].populate(rs[index]);
        				}//end of if(rs[index]!=null)
        			}//end of for(int index=0;index<intNoOfCursors;index++)
        			return crs;    			
        		}//end of else if(strReportID.equals("AddressingDocuments"))
    		
    		//added for Cigna
    		/*
    		else if(strReportID.equals("productivityRpt"))
         	{
     			   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?)}";
         			crs = new OracleCachedRowSet[intNoOfCursors];
         			rs = new ResultSet[intNoOfCursors];
         			conn = ResourceManager.getConnection();//getTestConnection();
         			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
         			cStmtObject.setString(1,strParameter);
         			for(int i=2;i<=intNoOfCursors;i++){
         				cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
         			}//end of for(int i=2;i<intNoOfCursors+2;i++)
         			cStmtObject.execute();
         			for(int index=0;index<=intNoOfCursors;index++) {
         				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+2);
         				crs[index] = new OracleCachedRowSet();
         				if(rs[index]!=null){
         					crs[index].populate(rs[index]);
         				}//end of if(rs[index]!=null)
         			}//end of for(int index=0;index<intNoOfCursors;index++)
         			return crs;    			
         		}//end of else if(strReportID.equals("CignaAddressingDocuments"))
    		*/
    		
    		
    		else if(strReportID.equals("CignaAddressingDocuments"))
         	{
     			   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?)}";
     			  System.out.println("strCall:::::::::::"+strCall);
         			crs = new OracleCachedRowSet[intNoOfCursors];
         			rs = new ResultSet[intNoOfCursors];
         			conn = ResourceManager.getConnection();//getTestConnection();
         			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
         			cStmtObject.setString(1,"CignaAddressingDocuments");
         			cStmtObject.setString(2,"CignaAddressingDocuments");
         			for(int i=3;i<intNoOfCursors+3;i++){
         				cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
         			}//end of for(int i=2;i<intNoOfCursors+2;i++)
         			cStmtObject.execute();
         			for(int index=0;index<intNoOfCursors;index++) {
         				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+3);
         				crs[index] = new OracleCachedRowSet();
         				if(rs[index]!=null){
         					crs[index].populate(rs[index]);
         				}//end of if(rs[index]!=null)
         			}//end of for(int index=0;index<intNoOfCursors;index++)
         			return crs;    			
         		}//end of else if(strReportID.equals("CignaAddressingDocuments"))
    		
    		
    		
    		
    		
    		
    		
    		
    		else {
    			
    			
    			
    			String strCall = "{CALL "+strProcedureName.trim();
    			System.out.println("strCall:::::::::::"+strCall);
    			String strParameters = "(?";
    			for(int i=0;i<intNoOfCursors;i++){
    				strParameters += ",?";
    			}//end of for(int i=0;i<intNoOfCursors;i++)
    			strParameters += ")}";
    			
    			strCall += strParameters;
    			crs = new OracleCachedRowSet[intNoOfCursors];
    			rs = new ResultSet[intNoOfCursors];
    			conn = ResourceManager.getConnection();//getTestConnection();
    			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
    			System.out.println("strCall=====>"+strCall);
    			cStmtObject.setString(1,strParameter);
    			for(int i=2;i<intNoOfCursors+2;i++){
                 cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
    			}//end of for(int i=2;i<intNoOfCursors+2;i++)
    			
    			cStmtObject.execute();
    			for(int index=0;index<intNoOfCursors;index++) {
    				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+2);
    				crs[index] = new OracleCachedRowSet();
    				if(rs[index]!=null){
    					crs[index].populate(rs[index]);
    				}//end of if(rs[index]!=null)
    			}//end of for(int index=0;index<intNoOfCursors;index++)
    			return crs;
    		}//end of else
    	}//end of try        
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "tTkReports");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "tTkReports");
    	}//end of catch (Exception exp)
    	finally
    	{
    		/* Nested Try Catch to ensure resource closure */ 
    		try // First try closing the Statement
    		{
    			try
    			{
    				for(int i=0;i<intNoOfCursors;i++){  
    					if (rs != null) rs[i].close();
        			}//end of for(int i=0;i<intNoOfCursors+1;i++)    				
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
    				throw new TTKException(sqlExp, "policy");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
    			{
    				try
    				{
    					if (cStmtObject != null) cStmtObject.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
    					throw new TTKException(sqlExp, "tTkReports");
    				}//end of catch (SQLException sqlExp)
    				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    				{
    					try
    					{
    						if(conn != null) conn.close();
    					}//end of try
    					catch (SQLException sqlExp)
    					{
    						log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
    						throw new TTKException(sqlExp, "tTkReports");
    					}//end of catch (SQLException sqlExp)
    				}//end of finally Connection Close
    			}//end of try
    		}//end of tryc
    		catch (TTKException exp)
    		{
    			throw new TTKException(exp, "tTkReports");
    		}//end of catch (TTKException exp)
    		finally // Control will reach here in anycase set null to the objects 
    		{
    			for(int i=0;i<intNoOfCursors;i++){  
					rs[i] = null;
    			}//end of for(int i=0;i<intNoOfCursors+1;i++)
    			cStmtObject = null;
    			conn = null;
    		}//end of finally
    	}//end of finally
    }//end of getReport(String strReportID,String strParameter)
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param alFaxStatusList ArrayList which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(ArrayList alFaxStatusList) throws TTKException {
    	
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        String strProcedureName="";
        String strReportID = (String)alFaxStatusList.get(0);
        strProcedureName =getProcedureName(strReportID);
        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }//end of if(strProcedureName==null)
        OracleCachedRowSet crs = null;
        try{
            String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?)}";
            crs = new OracleCachedRowSet();
            conn = ResourceManager.getConnection();//getTestConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,(String)alFaxStatusList.get(1));
            cStmtObject.setString(2,(String)alFaxStatusList.get(2));
            cStmtObject.setString(3,(String)alFaxStatusList.get(3));
            cStmtObject.setString(4,(String)alFaxStatusList.get(4));
            if(alFaxStatusList.get(5) != null)
            {
            	cStmtObject.setLong(5,(Long)alFaxStatusList.get(5));
            }//end of if(alFaxStatusList.get(5) != null)
            else
            {
            	cStmtObject.setString(5,null);
            }//end of else            
            cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(6);
            if(rs !=null)
            {
             	crs.populate(rs);
            }//end of if(rs !=null)
            return crs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
        }//end of finally
    }//end of getReport(String strReportID,String strParameter)
    
     /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strReportID String which procedure name which has to be called to get data.
     * @param alCallPendList ArrayList which contains the config param and primary mail list.
     * @exception throws TTKException
     */
    public ResultSet getReport(String strReportID, ArrayList alCallPendList) throws TTKException {
    	
    	
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        String strProcedureName="";
        strProcedureName =getProcedureName(strReportID);
        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }//end of if(strProcedureName==null)
        OracleCachedRowSet crs = null;
        try{
        	
        	if(strReportID.equalsIgnoreCase("EmplExcelExporter"))
        	{
        		   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                   crs = new OracleCachedRowSet();
                   conn = ResourceManager.getConnection();//getTestConnection();
                   cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                   if(alCallPendList.get(0)!=null)
               		cStmtObject.setString(1, (String)alCallPendList.get(0));//country_id
               		else{
               			cStmtObject.setString(1, null);
               		}
               		if(alCallPendList.get(1)!=null)
               			cStmtObject.setString(2, (String)alCallPendList.get(1)); //state_id
               		else
               		cStmtObject.setString(2, null);
               		if(alCallPendList.get(2)!=null){
               			 cStmtObject.setString(3, (String)alCallPendList.get(2)); //city_type
               		}
               		else
               			cStmtObject.setString(3, null);
               		
               		if(alCallPendList.get(3)!=null){
               			 cStmtObject.setString(4, (String)alCallPendList.get(3)); //prov_type
               		}
               		else
               			cStmtObject.setString(4, null);
               		
               		if(alCallPendList.get(5)!=null)
               			cStmtObject.setString(5, (String)alCallPendList.get(4));//speciality_type
               			else
               				cStmtObject.setString(5, null);
               			if(alCallPendList.get(5)!=null)
               				cStmtObject.setString(6, (String)alCallPendList.get(5)); //network_type
               			else
               			cStmtObject.setString(6, null);
               			cStmtObject.setString(7, (String)alCallPendList.get(7)); //sort_var
               			cStmtObject.setString(8, (String)alCallPendList.get(8)); //sort_order
               			cStmtObject.setString(9, (String)alCallPendList.get(9)); //start_num
               			cStmtObject.setString(10, (String)alCallPendList.get(10)); //end_num
               			cStmtObject.setString(12, "EXL"); //flag
               			cStmtObject.setString(13, String.valueOf(alCallPendList.get(6)));
                   cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
                   cStmtObject.execute();
                   rs = (java.sql.ResultSet) cStmtObject.getObject(11);
                   if(rs !=null)
                   {
                    	crs.populate(rs);
                   }//end of if(rs !=null)
                   return crs;
        	}
        	if(strReportID.equalsIgnoreCase("PatpolHistoryApprej") || strReportID.equalsIgnoreCase("ClmPolicyHistoryApprej"))
        	{
        		
        		   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?)}";
        		   
                   crs = new OracleCachedRowSet();
                   conn = ResourceManager.getConnection();//getTestConnection();
                   cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                   cStmtObject.setString(1,(String)alCallPendList.get(0));
                   cStmtObject.setString(2,((String)alCallPendList.get(1)));
                   cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
                   cStmtObject.execute();
                   rs = (java.sql.ResultSet) cStmtObject.getObject(3);
                   if(rs !=null)
                   {
                    	crs.populate(rs);
                   }//end of if(rs !=null)
                   return crs;
        	}
        	else if("BroClaimUtilizationRep".equals(strReportID)||"BroAuthUtilizationRep".equals(strReportID)
        			||"BroTATForClaims".equals(strReportID)||"BroTATForPreAuth".equals(strReportID)
        			||"BroTATFSCORPOARTE".equals(strReportID)){

     		   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?,?,?)}";
     		   
                crs = new OracleCachedRowSet();
                conn = ResourceManager.getConnection();//getTestConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                cStmtObject.setString(1,(String)alCallPendList.get(0));
                cStmtObject.setString(2,(String)alCallPendList.get(1));
                cStmtObject.setString(3,(String)alCallPendList.get(2));
                cStmtObject.setString(4,(String)alCallPendList.get(3));
                cStmtObject.setString(5,(String)alCallPendList.get(4));
                cStmtObject.setString(6,(String)alCallPendList.get(5));
                cStmtObject.setString(7,(String)alCallPendList.get(6));
                cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
                cStmtObject.execute();
                rs = (java.sql.ResultSet) cStmtObject.getObject(8);
                if(rs !=null)
                {
                 	crs.populate(rs);
                }//end of if(rs !=null)
                return crs;
        	}else if("BroTechnicalResultReport".equals(strReportID)){

     		   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?)}";
     		   
                crs = new OracleCachedRowSet();
                conn = ResourceManager.getConnection();//getTestConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                cStmtObject.setString(1,(String)alCallPendList.get(0));
                cStmtObject.setString(2,(String)alCallPendList.get(1));
                cStmtObject.setString(3,(String)alCallPendList.get(2));
                cStmtObject.setString(4,(String)alCallPendList.get(3));
                cStmtObject.setString(5,(String)alCallPendList.get(7));
                cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
                cStmtObject.execute();
                rs = (java.sql.ResultSet) cStmtObject.getObject(6);
                if(rs !=null)
                {
                 	crs.populate(rs);
                }//end of if(rs !=null)
                return crs;
        	}else if("BroTATFSINDIVIDUAL".equals(strReportID)){

      		   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?,?)}";
      		   
                 crs = new OracleCachedRowSet();
                 conn = ResourceManager.getConnection();//getTestConnection();
                 cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                 cStmtObject.setString(1,(String)alCallPendList.get(0));
                 cStmtObject.setString(2,(String)alCallPendList.get(1));
                 cStmtObject.setString(3,(String)alCallPendList.get(2));
                 cStmtObject.setString(4,(String)alCallPendList.get(4));
                 cStmtObject.setString(5,(String)alCallPendList.get(5));
                 cStmtObject.setString(6,(String)alCallPendList.get(6));
                 cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
                 cStmtObject.execute();
                 rs = (java.sql.ResultSet) cStmtObject.getObject(7);
                 if(rs !=null)
                 {
                  	crs.populate(rs);
                 }//end of if(rs !=null)
                 return crs;
         	
        	}else if(strReportID.equalsIgnoreCase("ClaimUtilizationRep") || strReportID.equalsIgnoreCase("AuthUtilizationRep") || strReportID.equalsIgnoreCase("TATForClaims")
        			|| strReportID.equalsIgnoreCase("TATForPreAuth") ||  strReportID.equalsIgnoreCase("TATFSCORPOARTE"))//Insurance Login Reports
        	{
     		    String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?,?,?)}";
                crs = new OracleCachedRowSet();
                conn = ResourceManager.getConnection();//getTestConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                cStmtObject.setString(1,(String)alCallPendList.get(0));
                cStmtObject.setString(2,((String)alCallPendList.get(1)));
                cStmtObject.setString(3,(String)alCallPendList.get(2));
                cStmtObject.setString(4,((String)alCallPendList.get(3)));
                cStmtObject.setString(5,(String)alCallPendList.get(4));
                cStmtObject.setString(6,((String)alCallPendList.get(5)));
                cStmtObject.setString(7,((String)alCallPendList.get(6)));
                cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
                cStmtObject.execute();
                rs = (java.sql.ResultSet) cStmtObject.getObject(8);
                if(rs !=null)
                {
                 	crs.populate(rs);
                }//end of if(rs !=null)
                return crs;
        	}
        	else if(strReportID.equalsIgnoreCase("TATFSINDIVIDUAL"))//Insurance Login Reports
        	{
     		    String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?,?)}";
                crs = new OracleCachedRowSet();
                conn = ResourceManager.getConnection();//getTestConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                cStmtObject.setString(1,(String)alCallPendList.get(0));
                cStmtObject.setString(2,((String)alCallPendList.get(1)));
                cStmtObject.setString(3,(String)alCallPendList.get(2));
                cStmtObject.setString(4,(String)alCallPendList.get(4));
                cStmtObject.setString(5,((String)alCallPendList.get(5)));
                cStmtObject.setString(6,((String)alCallPendList.get(6)));
                cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
                cStmtObject.execute();
                rs = (java.sql.ResultSet) cStmtObject.getObject(7);
                if(rs !=null)
                {
                 	crs.populate(rs);
                }//end of if(rs !=null)
                return crs;
        	}
        	else if(strReportID.equalsIgnoreCase("TechnicalResultReport"))//Insurance Login Reports
        	{
     		    String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?)}";
                crs = new OracleCachedRowSet();
                conn = ResourceManager.getConnection();//getTestConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                cStmtObject.setString(1,(String)alCallPendList.get(0));
                cStmtObject.setString(2,((String)alCallPendList.get(1)));
                cStmtObject.setString(3,(String)alCallPendList.get(2));
                cStmtObject.setString(4,((String)alCallPendList.get(3)));
                cStmtObject.setString(5,(String)alCallPendList.get(4));
                cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
                cStmtObject.execute();
                rs = (java.sql.ResultSet) cStmtObject.getObject(6);
                if(rs !=null)
                {
                 	crs.populate(rs);
                }//end of if(rs !=null)
                return crs;
        	}
        	if(strReportID.equalsIgnoreCase("batchInvoice"))
        	{
        		
        		   String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?)}";
        		   
                   crs = new OracleCachedRowSet();
                   conn = ResourceManager.getConnection();//getTestConnection();
                   cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
                   cStmtObject.setString(1,(String)alCallPendList.get(0));
                   cStmtObject.setString(2,((String)alCallPendList.get(1)));
                   cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
                   cStmtObject.execute();
                   rs = (java.sql.ResultSet) cStmtObject.getObject(3);
                   if(rs !=null)
                   {
                    	crs.populate(rs);
                   }//end of if(rs !=null)
                   return crs;
        	}
        	else
        		{
        		String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?)}";
            crs = new OracleCachedRowSet();
            conn = ResourceManager.getConnection();//getTestConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,(String)alCallPendList.get(0));
            cStmtObject.setString(2,((String)alCallPendList.get(1)));
            cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(3);
            if(rs !=null)
            {
             	crs.populate(rs);
            }//end of if(rs !=null)
            return crs;
        		}
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
        }//end of finally
    }//end of getReport(String strReportID,String strParameter)
    

    
public ArrayList getPreAuthUtilizationReport(String parameter,String strSdate,String strEdate,String strPayer,String strProvider,String strCorps,String csStartDate,String csEndDate,String sAgentCode,String sType,String eType,String sStatus,String insCompanyCode,String sGroupPolicyNo) throws TTKException 
{ 	
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ArrayList<ReportVO> alRresultList	=	null;
        try{
            conn = ResourceManager.getConnection();
            if("ERP".equals(parameter)) 
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strERPAUTHUTILIZATION);
            else if("PRP".equals(parameter))
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPRPAUTHUTILIZATION);  
            else if("CLR".equals(parameter))
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCLRAUTHUTILIZATION);
            
            cStmtObject.setString(1, strPayer);
            cStmtObject.setString(2, strProvider);
            cStmtObject.setString(3, strCorps);//ERP
            cStmtObject.setString(4, insCompanyCode);
            cStmtObject.setString(5, sAgentCode);//ERP
            if("ERP".equals(parameter)) 
            	cStmtObject.setString(6, eType);
            else
            	cStmtObject.setString(6, eType);
            cStmtObject.setString(7, sStatus);//ERP
            cStmtObject.setString(8, sType);
            cStmtObject.setString(9, sGroupPolicyNo);//ERP
            cStmtObject.setString(10, csStartDate);
            cStmtObject.setString(11, strSdate);//ERP
            cStmtObject.setString(12, strEdate);//ERP
            cStmtObject.registerOutParameter(13,OracleTypes.CURSOR);
            
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(13);
            
            if("CSM".equals(parameter) || "PRM".equals(parameter))
            {
            	 rs1 = (java.sql.ResultSet)cStmtObject.getObject(2);
            }
            
            	
            ReportVO reportVO = null;
            if(rs != null){
            	alRresultList	=	new ArrayList<ReportVO>();
            	//GEETING META DATA FOR COLUMN HEADS
            	 ArrayList<String> alResult	=	null;
                 ResultSetMetaData metaData	=	rs.getMetaData();
                 int colmCount				=	metaData.getColumnCount();
                 if(colmCount>0)
                 	alResult = new ArrayList<String>();
                 for(int k=0;k<colmCount;k++)
                 {
                 	alResult.add(metaData.getColumnName(k+1));
                 }
                 reportVO	=	new ReportVO();
                 reportVO.setAlColumns(alResult);
                 alRresultList.add(reportVO);
            	
               //THIS BLOCK IS DATA FROM QUERY
            	while (rs.next()) {
            		alResult = new ArrayList<String>();
                	reportVO = new ReportVO();
                	
                	for(int l=1;l<=colmCount;l++)
                		alResult.add(rs.getString(l)==null?"":rs.getString(l));
                	
                	reportVO.setAlColumns(alResult);
                    alRresultList.add(reportVO);
                }//end of while(rs.next())
            	
            	
            	//START OF SECOND RESULT SET
            	if(rs1 != null){
            		//GEETING META DATA FOR COLUMN HEADS
                    metaData	=	rs1.getMetaData();
                    colmCount	=	metaData.getColumnCount();
                    if(colmCount>0)
                    	alResult = new ArrayList<String>();
                    for(int k=0;k<colmCount;k++)
                    {
                    	alResult.add(metaData.getColumnName(k+1));
                    }
                    reportVO	=	new ReportVO();
                    reportVO.setAlColumns(alResult);
                    alRresultList.add(reportVO);
               	
                  //THIS BLOCK IS DATA FROM QUERY
            		while (rs1.next()) {
                		alResult = new ArrayList<String>();
                    	reportVO = new ReportVO();
                    	
                    	for(int l=1;l<colmCount;l++)
                    		alResult.add(rs1.getString(l)==null?"":rs1.getString(l));
                    	
                    	reportVO.setAlColumns(alResult);
                        alRresultList.add(reportVO);
                    }//end of while(rs1.next())
            		
            	}//end of if(rs1 != null)
            }//end of if(rs != null)
            
            return (ArrayList)alRresultList;
        }
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs1 != null) rs1.close();
        			if (rs != null) rs.close();
        			if(rs1!=null) rs1.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs1 = null;
        		rs = null;
        		rs1=null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
        }//end of finally
    }//end of getPreAuthUtilizationReport()
    

/*
 * MIS Finance Reports - Kishor kumar S H
 */

public ArrayList getDetailedReport(String parameter,String repType) throws TTKException {
    
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        ArrayList<ReportVO> alRresultList	=	null;
        try{
            conn = ResourceManager.getConnection();
            if("detail".equals(repType))
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDETAILEDREPORT);
            else if("pending".equals(repType))
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPENDINGREPORT);
            else if("hospital".equals(repType))
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strBANKHOSPITALREPORT);
            else if("policy".equals(repType))
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strBANKPOLICYREPORT);
            else if("exchangeRate".equals(repType))
            	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strEXCHANGEREPORT);
        	cStmtObject.setString(1, parameter);
        	cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(2);
            
            ReportVO reportVO = null;
            if(rs != null){
            	alRresultList	=	new ArrayList<ReportVO>();
            	//GEETING META DATA FOR COLUMN HEADS
            	 ArrayList<String> alResult	=	null;
                 ResultSetMetaData metaData	=	rs.getMetaData();
                 int colmCount				=	metaData.getColumnCount();
                 if(colmCount>0)
                 	alResult = new ArrayList<String>();
                 for(int k=0;k<colmCount;k++)
                 {
                 	alResult.add(metaData.getColumnName(k+1));
                 }
                 reportVO	=	new ReportVO();
                 reportVO.setAlColumns(alResult);
                 alRresultList.add(reportVO);
            	
               //THIS BLOCK IS DATA FROM QUERY
            	while (rs.next()) {
            		alResult = new ArrayList<String>();
                	reportVO = new ReportVO();
                	
                	for(int l=1;l<=colmCount;l++){
                		alResult.add(rs.getString(l)==null?"":rs.getString(l));

                	}
                	reportVO.setAlColumns(alResult);
                    alRresultList.add(reportVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            
            return (ArrayList)alRresultList;
        }
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
        }//end of finally
    }//end of getDetailedReport(String parameter
    

    

	/**
	 * @param strReportID
	 * @return
	 */
	private String getProcedureName(String strReportID) {
	
		if(strReportID.equals("QuotationInvoice"))
		{
			return(strTPA_COMISSION_INVOICE);
		}
		
		if(strReportID.equals("QuotationInvoicesub"))
		{
			return(strTPA_COMISSION_INVOICE);
		}
		
		
		if("TakafulLetter".equals(strReportID))
		{
			return(strTAKAFUL_LETTER);
		}

		if(strReportID.equals("PolicyCopy"))
		{
			return(strVIEW_QUOTATION);
		}
		
		if(strReportID.equals("GenerateQauotation"))
		{
			return(strVIEW_QUOTATION);
		}
		
		if(strReportID.equals("ViewInputPricing"))
		{
			return(strVIEW_INPUT_SUMMARY);
		}
		
		if(strReportID.equals("EmplExcelExporter"))
		{
			return(strEmplExportExcelData);
		}

		if(strReportID.equals("CardPrintMobileApp"))
		{
			return(strSELECT_MOB_CARD_PRINT);
		}
		
		if(strReportID.equals("PremiumRates"))
		{
			return(strPREMIUM_RATES_SECTION);
		}
		if(strReportID.equals("BenefitDesignSection"))
		{
			return(strBENEFIT_DESIGN_SECTION);
		}
		if(strReportID.equals("HealthInsuranceQuote"))
		{
			return(strHEALTH_INSURANCE_QUOTE);
		}
		else if(strReportID.equals("BenefitDetails"))
		{
			return(strHEALTH_INSURANCE_QUOTE);
		}
		else if(strReportID.equals("GeneralTermsAndConditions"))
		{
			return(strHEALTH_INSURANCE_QUOTE);
		}
		else if(strReportID.equals("ClientApproval"))
		{
			return(strHEALTH_INSURANCE_QUOTE);
		}
		else if(strReportID.equals("PreauthLetterFormat"))
		{
			return(strPREAUTH_LETTER_FORMAT);
		}
		else if("ClaimLetterFormat".equals(strReportID))
		{
			return(strCLAIM_LETTER_FORMAT);
		}	
		else if(strReportID.equals("DiagnosisDetails"))
		{
			return(strDIAGNOSIS_DETAILS);
		}
		else if(strReportID.equals("ActivityDetails"))
		{
			return(strACTIVITY_DETAILS);
		}
		else if(strReportID.equals("GenRenMemXLs"))
		{
			return(strGENERATE_RENEWAL_MEMBERS);
		}
		else if(strReportID.equals("GenPolicyRenMemXLs"))
		{
			return(strGENERATE_POLICY_RENEWAL_MEMBERS);
		}
		else if(strReportID.equals("AccentureRpt"))
		{
			return(strGET_MEMBERS_ADDED_LIST);
		}else if(strReportID.equals("IBMDeletion"))
		{
			return(strDELETION_CUTOFF); // This procedure is to be changed for IBM
		}else if(strReportID.equals("IBMAdditionMaxRec"))
		{
			return(strADDITION_CUTOFF);
		}else if(strReportID.equals("NewIBMDeletion")){
			return(strNEW_IBM_DELETION_CUTOFF);
		}
		else if(strReportID.equals("IBMGrpPreAuthRpt"))
		{
			return strIBMPREAUTH_REPORT;
		}//else if(strReportID.equals("IBMGrpPreAuthRpt"))
		else if(strReportID.equals("IBMClaimRpt"))
		{
			return strIBMCLAIM_REPORT;
		}//else if(strReportID.equals("IBMClaimRpt"))
		else if(strReportID.equals("ChildBorn"))
		{
			return strIBMCHILDBORN_REPORT;
		}//else if(strReportID.equals("IBMClaimRpt"))
		else if(strReportID.equals("Reoptin"))
		{
			return strIBMREPOTIN_REPORT;
		}//else if(strReportID.equals("Reoptin"))
		else if(strReportID.equals("MontlyRecon"))
		{
			return strIBMRECON_REPORT;
		}//else if(strReportID.equals("MontlyRecon"))
		else if(strReportID.equals("DailyReport"))
		{
			return strIBMDAILY_OPTOUT_REPORT;
		}//else if(strReportID.equals("DailyReport"))
		else if(strReportID.equals("DailyAdditionalCoverage"))
		{
			return strIBMDAILY_ADDITTIONAL_COVERAGE_REPORT;
		}//else if(strReportID.equals("DailyReport"))
		else if(strReportID.equals("DailyParentalCoverage"))
		{
			return strIBMDAILY_PARENTAL_COVERAGE_REPORT;
		}//else if(strReportID.equals("DailyReport"))
		//Added for KOC-1255
		else if(strReportID.equals("Accenture_Report_MC"))
		{
			return strACCENTURE_DASHBOARD_MC_REPORT;
		}
		else if(strReportID.equals("Accenture_Report_CC"))
		{
			return strACCENTURE_DASHBOARD_CC_REPORT;
		}
		else if(strReportID.equals("Accenture_Report_SMC"))
		{
			return strACCENTURE_DASHBOARD_SMC_REPORT;
		}
		else if(strReportID.equals("Accenture_Report_SCC"))
		{
			return strACCENTURE_DASHBOARD_SCC_REPORT;
		}
		else if(strReportID.equals("Accenture_Report_PRE"))
		{
			return strACCENTURE_DASHBOARD_PREAUTH_REPORT;
		}
		else if(strReportID.equals("Accenture_Report_CALL"))
		{
			return strACCENTURE_DASHBOARD_CALLCENTER_REPORT;
		}	
		//Ended For KOC-1255
		//Ended.
		if(strReportID.equals("EnrDaiImg"))
		{
			return(strIMAGE_NOT_UPLOADED);
		}//end of if(strReportID.equals("EnrDaiImg"))
		if(strReportID.equals("EnrDaiPolAwtWork"))
		{
			return(strPOLICIES_AWAITING_WORKFLOW);
		}//end of if(strReportID.equals("EnrDaiPolAwtWork"))
		if(strReportID.equals("EnrDaiCardPrint"))
		{
			return(strCARDS_PRINTED);
		}//end of if(strReportID.equals("EnrDaiCardPrint"))
		if(strReportID.equals("EnrDaiCardsNotDelr"))
		{
			return(strCARDS_NOT_DELIVERED);
		}//end of if(strReportID.equals("EnrDaiCardsNotDelr"))
		if(strReportID.equals("EnrDaiGrpRen"))
		{
			return(strGROUPS_RENEWAL);
		}//end of if(strReportID.equals("EnrDaiGrpRen"))
		else if(strReportID.equals("EnrDaiTimeTakToRecvPol"))
		{
			return(strTIMETAKEN_TO_RECEIVE_POLICY);
		}//end of else if(strReportID.equals("EnrDaiTimeTakToRecvPol"))
		else if(strReportID.equals("EnrDaiDispRpt"))
		{
			return(strDISPATCH_REPORT);
		}//end of else if(strReportID.equals("EnrDaiDispRpt"))
		else if(strReportID.equals("EnrDaiEndors"))
		{
			return(strENDORSEMENTS);
		}//end of else if(strReportID.equals("EnrDaiEndors"))
		else if(strReportID.equals("EnrDaiPolProcTime"))
		{
			return(strTIMETAKEN_TO_PROCESS_POLICY);
		}//end of else if(strReportID.equals("EnrDaiPolProcTime"))
		else if(strReportID.equals("SupDispRpt"))
		{
			return(strDISPATCH_LIST);
		}//end of else if(strReportID.equals("SupDispRpt"))
		else if(strReportID.equals("SoftCopyUpload"))
		{
			return(strERROR_DETAILS);
		}//end of else if(strReportID.equals("SoftCopyUpload"))
		else if(strReportID.equals("SoftCopyUploadError"))
		{
			return(StrSOFTCOPYUPLOAD_ERROR_DETAILS);
		}//end of else if(strReportID.equals("SoftCopyUploadError"))
		else if(strReportID.equals("EnrDaiBOI"))
		{
			return(strBOI_REPORT);
		}//end of else if(strReportID.equals("EnrDaiBOI"))
		else if(strReportID.equals("EndrImpactReport"))
		{
			return(strIMPACT_REPORT);
		}//end of else if(strReportID.equals("EndrImpactReport"))
		else if(strReportID.equals("EFTAcceAnne"))
		{
			return(strEFT_COV_LETTER_ANNEXURE);
		}//end of else if(strReportID.equals("EFTAcceAnne"))
		else if(strReportID.equals("EFTFinBat"))
		{
			return(strEFT_COVERING_LETTER);
		}//end of else if(strReportID.equals("EFTAcceAnne"))
		else if(strReportID.equals("PremChangeRpt"))
		{
			return(strPREMIUM_CHANGE_REPORT);
		}//end of else if(strReportID.equals("PremChangeRpt"))
		else if(strReportID.equals("CodeRejClus"))
		{
			return(strGET_REJECTION_INFO);
		}//end of else if(strReportID.equals("CodeRejClus"))
		
		//pre-auth reports
		else if(strReportID.equals("PreAuthRpt"))
		{
			return(strPRE_AUTH_RPT);
		}//end of else if(strReportID.equals("PreAuthRpt"))
		else if(strReportID.equals("PreAuthManul"))
		{
			return(strMANUAL_PREATH_RPT);
		}//end of else if(strReportID.equals("PreAuthManul"))
		//claims reports
		else if(strReportID.equals("ClaimsManul"))
		{
			return(strMANUAL_CLAIM_RPT);
		}//end of else if(strReportID.equals("ClaimsManul"))
		
		//self fund
		else if(strReportID.equals("PreAuthDiagLetter"))
		{
			return(strPRE_AUTH_LETTER);
		}//end of else if(strReportID.equals("PreautViewHis"))
		
		else if(strReportID.equals("DiagonisTariffDetails"))
		{
			return(strDiagonisTariffHistory);
		}//end of else if(strReportID.equals("PreautViewHis"))
		else if(strReportID.equals("ProcessTimeAnalysis"))
		{
			return(strPROCESS_TIME_ANALYSIS);
		}//end of else if(strReportID.equals("ProcessTimeAnalysis"))
		//KOC 1339 for mail
		//KOC 1353 for payment report
		else if(strReportID.equals("INSPaymentReport"))
		{
			return(strONLINE_PAYMENT_INS_REPORT);
		}//end of else if(strReportID.equals("ClaimsIntimations"))
		else if(strReportID.equals("PreauthReport"))//KOCPreauthreport 
		{
			return(strINS_PREAUTH_CIGNA_REPORT);
		}//end of else if(strReportID.equals("PreauthReport"))
		else if(strReportID.equals("ClaimReport"))//KOCPreauthreport 
		{
			return(strINS_CLAIM_CIGNA_REPORT);
		}//end of else if(strReportID.equals("ClaimReport"))
		else if(strReportID.equals("PolicyReport"))//KOCPreauthreport 
		{
			return(strINS_POLICY_CIGNA_REPORT);
		}//end of else if(strReportID.equals("PolicyReport"))
		//KOC 1353 for payment report
		else if(strReportID.equals("ClaimsIntimation"))
		{
			return(stCLAIM_INTIMATIN_RPT);
		}//end of else if(strReportID.equals("ClaimsIntimation"))
		//KOC 1339 for mail
		else if(strReportID.equals("ClosureFormat"))
		{
			return (strCLOSURE_LETTER);
		}//end of else if(strReportID.equals("ClosureFormat"))
		//added for Mail-SMS for Cigna
		else if(strReportID.equals("CignaClaimClosureLtrAdvisor"))
		{
			return(strCLAIM_CLOSURE_LTR_ADVISOR);
		}	
		else if(strReportID.equals("CignaClaimSubAppvlLtrAdv"))
		{
		   return(strCLAIM_SUB_APP_LTR_ADV); 
		}		
		else if(strReportID.equals("CignaClaimRejectLtrPO"))
		{
			return(strCLAIM_REJECT_LTR_PO);
		}
		else if(strReportID.equals("CignaClaimAppvlLtrAdv"))
		{
			return(strCLAIM_APP_LTR_ADV); 
		}
		else if(strReportID.equals("ClaimSettlementLtrPO"))
		{
			return(strCLAIM_SETTLEMENT_LTR_PO); 
		}
		//ended
		else if(strReportID.equals("RejectionLetter"))
		{
			return (strRejectionLetter);
		}//end of else if(strReportID.equals("RejectionLetter"))
		else if(strReportID.equals("ClaimsOutstandingLiability"))
		{
			return(strClaims_OUTSTANDING_LIABILITY);
		}//end of else if(strReportID.equals("ClaimsOutstandingLiability"))
		else if(strReportID.equals("MedicalOpinion"))
		{
			return(strMedicalOpinionSheet);
		}//end of else if(strReportID.equals("MedicalOpinion"))
		else if(strReportID.equals("PreAuthClmDetail"))
		{
			return(strPreAuthClmDtlRpt);
		}//end of else if(strReportID.equals("PreAuthClmDetail"))
		
		//finance reports
		else if(strReportID.equals("FinDaiBat"))
		{
			return(strFinanceBatch);
		}//end of else if(strReportID.equals("FinDaiBat"))
		else if(strReportID.equals("FinDaiCovLetter"))
		{
			return(strCOVERING_LETTER);
		}//end of else if(strReportID.equals("FinDaiCovLetter"))
		else if(strReportID.equals("FinBanRec"))
		{
			return(strRECONCILATION_REPORT_TEST);
		}//end of else if(strReportID.equals("FinBanRec"))
		else if(strReportID.equals("FinBanRecBankTran"))
		{
			return(strBANK_TRANSACTION_REPORT);
		}//end of else if(strReportID.equals("FinBanRecBankTran"))
		else if(strReportID.equals("FinBanRecFloatTran"))
		{
			return(strFLOAT_TRANSACTION_REPORT);
		}//end of else if(strReportID.equals("FinBanRecFloatTran"))
		else if(strReportID.equals("FinDetRpt"))
		{
			return(strCLAIMS_DETAILS_REPORT);
		}//end of else if(strReportID.equals("FinDetRpt"))
		else if(strReportID.equals("CitiFinDetRpt"))
		{
			return(strCITI_CLAIMS_DETAILS_REPORT);
		}//end of else if(strReportID.equals("CitiFinDetRpt"))
		else if(strReportID.equals("FinDetEFTRpt"))
		{
			return(strEFT_CLAIMS_DETAILS_REPORT);
		}//end of else if(strReportID.equals("FinDetEFTRpt"))	
		else if(strReportID.equals("FinClmPenEFTRpt"))
		{
			return(strEFT_CLAIMS_PENDING_REPORT);
		}//end of else if(strReportID.equals("FinClmPenEFTRpt"))
		else if(strReportID.equals("FinBatChck")|| strReportID.equals("FinBatChckManual")) 
		{
			return(strMANUAL_BATCH_LIST);
		}//end of else if(strReportID.equals("FinBatChck"))
		else if(strReportID.equals("FltStmtRpt"))
		{
			return(strFLOAT_STATEMENT_REPORT);
		}//end of else if(strReportID.equals("FltStmtRpt"))
		else if(strReportID.equals("FinPenRpt"))
		{
			return(strCLAIMS_PENDING_REPORT);
		}//end of else if(strReportID.equals("FinPenRpt"))	
		else if(strReportID.equals("RoutineRpt"))
		{
			return(strRoutine_REPORT);
		}//end of else if(strReportID.equals("FinPenRpt"))	
		// Changes added for Debit Note CR KOC1163
		else if(strReportID.equals("IbmDakshRpt"))
		{
			return(strDEBITNOTE_IBM_DAKSH_REPORT);
		}//end of else if(strReportID.equals("FinPenRpt"))
		// End changes added for Debit Note CR KOC1163
		else if(strReportID.equals("FinTPAComm"))
		{
			return(strTPA_COMMISSION_REPORT);
		}//end of else if(strReportID.equals("FinTPAComm"))
		else if(strReportID.equals("76ColPend"))
		{
			return(strSEVENTY_SIX_COLUMN_REPORT);
		}//end of else if(strReportID.equals("76ColPend"))
		else if(strReportID.equals("TPACommissionRpt"))
		{
			return(strTPA_COMISSION_REPORT_SHEETS);
		}//end of else if(strReportID.equals("TPACommissionRpt"))
		else if(strReportID.equals("FurGenRpt"))
		{
			return(strFUT_GEN_CLAIMS_DETAILS_REPORT);
		}//end of else if(strReportID.equals("FurGenRpt"))
		else if(strReportID.equals("FinMediClaimCom"))
		{
			return(strFIN_CLAIM_COMPUTATION);
		}//end of else if(strReportID.equals("FinMediClaimCom"))
		else if(strReportID.equals("FinMediClaimComLineItems"))
		{
			return(strFIN_CLAIM_COMPUTATION_LINEITEMS);
		}//end of else if(strReportID.equals("FinMediClaimComLineItems"))
		else if(strReportID.equals("DailyTransferPending"))
		{
			return(strFIN_SELECT_PENDING_DREMIT_RPT);
		}//end of else if(strReportID.equals("DailyTransferPending"))
		else if(strReportID.equals("DailyTransferComplete"))
		{
			return(strFIN_SELECT_SETTLED_DREMIT_RPT);
		}//end of else if(strReportID.equals("DailyTransferComplete"))
		
		
		
		else if(strReportID.equals("MemberClaimLetterFormat"))
		{
			return(strCLAIM_SELECT_REIM_CLAIM_LETTER);
		}//end of else if(strReportID.equals("MemberClaimLetterFormat")
		else if(strReportID.equals("MemberDiagnosisDetails")){
			return(strMEMBER_DIAGNOSIS_DETAILS);
		}//end of else if(strReportID.equals("MemberActivityDetails")
	
		//Coding Reports
		else if(strReportID.equals("PenCaseRpt"))
		{
			return(strPendingCases_Report);
		}//end of else if(strReportID.equals("PenCaseRpt"))
		else if(strReportID.equals("CCSRpt"))
		{
			return(strCodeCleanupStatusReport);
		}//end of else if(strReportID.equals("CCSRpt"))
				
		//call Center Reports
		else if(strReportID.equals("CardViewHis"))
		{
			return(strCARD_HISTORY);
		}//end of else if(strReportID.equals("CardViewHis"))
		else if(strReportID.equals("PreautViewHis"))
		{
			return(strPRE_AUTH_HISTORY);
		}//end of else if(strReportID.equals("PreautViewHis"))
		else if(strReportID.equals("ClaimViewHis"))
		{
			return(strCLAIM_HISTORY);
		}//end of else if(strReportID.equals("ClaimViewHis"))
		else if(strReportID.equals("PolViewHis"))
		{
			return(strPOLICY_HISTORY);
		}//end of else if(strReportID.equals("PolViewHis"))
		else if(strReportID.equals("PolicyHistorySub"))
		{
			return(strPOLICY_HISTORY_SUB);
		}//end of else if(strReportID.equals("PolicyHistorySub"))
		else if(strReportID.equals("CallViewHis"))
		{
			return(strCALL_HISTORY_DETAIL);
		}//end of else if(strReportID.equals("CallViewHis"))
		else if(strReportID.equals("CallViewHisSub"))
		{
			return(strCALL_HISTORY_LIST);
		}//end of else if(strReportID.equals("CallViewHisSub"))
		else if(strReportID.equals("SupCardLb"))
		{
			return(strGENERATE_CARD_LIST);
		}//end of else if(strReportID.equals("SupCardLb"))
		else if(strReportID.equals("CallHisAssDet"))
		{
			return(strCALL_HISTORY_ASSOCIATE_DETAIL);
		}//end of else if(strReportID.equals("CallHisAssDet"))
		else if(strReportID.equals("CallClmNotDet"))
		{
			return(strCLAIM_NOTIFICATIONS_DETAIL);
		}//end of else if(strReportID.equals("CallClmNotDet"))
		else if(strReportID.equals("PreAuthHistoryList"))
		{
			return (strPRE_AUTH_HIST_SHORTFALL_LIST);
		}//end of else if(strReportID.equals("PreAuthHistoryList"))
		else if(strReportID.equals("DisallowedBillList"))
		{
			return (strDISALLOWED_BILL_LIST);
		}//end of else if(strReportID.equals("DisallowedBillList"))
		else if(strReportID.equals("DisallowedBillList1"))
		{
			return (strDISALLOWED_BILL_LIST);
		}//end of else if(strReportID.equals("DisallowedBillList1"))
		else if(strReportID.equals("ShortfallQuestions"))
		{
			return (strPRE_AUTH_HISTORY_QUESTIONS);
		}//end of else if(strReportID.equals("ShortfallQuestions"))
		else if(strReportID.equals("PreAuthHistoryInvestigationList"))
		{
			return (strPRE_AUTH_HISTORY_INVEST_LIST);
		}//end of else if(strReportID.equals("PreAuthHistoryInvestigationList"))
		else if(strReportID.equals("InvestigationDetails"))
		{
			return (strINVESTIGATION_DETAIL);
		}//end of else if(strReportID.equals("InvestigationDetails"))
		else if(strReportID.equals("ClaimHistoryList"))
		{
			return(strPRE_AUTH_HIST_SHORTFALL_LIST);
		}//end of else if(strReportID.equals("ClaimHistoryList"))
		else if(strReportID.equals("ClaimHistoryInvestigationList"))
		{
			return(strPRE_AUTH_HISTORY_INVEST_LIST);
		}//end of else if(strReportID.equals("ClaimHistoryInvestigationList"))
		else if(strReportID.equals("BufferList"))
		{
			return(strPRE_CLM_HIST_BUFFER_LIST);
		}//end of else if(strReportID.equals("BufferList"))
		else if(strReportID.equals("BufferDetails"))
		{
			return(strPRE_CLM_HIST_BUFFER_DETAIL);
		}//end of else if(strReportID.equals("BufferDetails"))
		else if(strReportID.equals("EndorsementList"))
		{
			return(strENDORSEMENT_LIST);
		}//end of else if(strReportID.equals("EndorsementList"))
			
		//other reports
		else if(strReportID.equals("CourierDtl"))
		{
			return(strCOURIER_DETAILS);
		}//end of else if(strReportID.equals("CourierDtl"))
		else if(strReportID.equals("PrintCheque"))
		{
			return(strPRINT_CHEQUE);
		}//end of else if(strReportID.equals("PrintCheque"))
		else if(strReportID.equals("Shortfall"))
		{
			return(strSHORTFALL_GENERATE_LETTER_claims);
		}//end of else if(strReportID.equals("Shortfall"))
		//Shortfall CR KOC1179
		else if(strReportID.equals("AddressingShortfall"))
		{
			return(strSHORTFALL_GENERATE_SHORTFALL_LETTER_CLAIMS);
		}//end of else if(strReportID.equals("AddressingShortfall"))
		
		//added for Mail-SMS for Cigna
		else if(strReportID.equals("CignaShortfall"))
		{
			return(strCIGNA_SHORTFALL_GENERATE_LETTER_claims);
		}//end of else if(strReportID.equals("Shortfall"))
		//added for cignaclaimHistoryreport
		else if(strReportID.equals("CignaClaimShortfall"))
		{
			return(strCIGNA_SHORTFALL_CLAIM_HISTORY);
		}
		//Shortfall CR KOC1179
		else if(strReportID.equals("AddressingShortfallRequests"))
		{
			return(strSHORTFALL_GENERATE_SHORTFALL_REQUEST_LETTER_CLAIMS);
		}//end of else if(strReportID.equals("AddressingShortfallRequests"))
		
		//added for Mail-SMS Cigna Template
		else if(strReportID.equals("CignaAddressingShortfallRequests"))
		{
			return (strCIGNA_SHORTFALL_GENERATE_SHORTFALL_REQUEST_LETTER_CLAIMS);
		}	
		// Shortfall CR KOC1179
		else if(strReportID.equals("AddressingDocuments"))
		{
			return(strGENERATE_SHORTFALL_SCHEDULER_REQUEST);
		}//end of else if(strReportID.equals("AddressingDocuments"))
		//added for Cigna-mail SMS 
		else if(strReportID.equals("CignaAddressingDocuments"))
		{
			return(strGENERATE_CIGNA_SHORTFALL_SCHEDULER_REQUEST);
		}//end of else if(strReportID.equals("AddressingDocuments"))
		
		else if(strReportID.equals("CignaSubAddressingShortfallRequests"))
		{
			return (strCIGNA_SUBREPORT_SHORTFALL_GENERATE_SHORTFALL_REQUEST_LETTER_CLAIMS);
		}
		
		// Shortfall CR KOC1179 Claim Shortfall Summary Report
		else if(strReportID.equals("ClaimShortfallSummary"))
		{
			
			return(strCLAIMS_SHORTFALL_SUMMARY_REPORT);
		}//end of else if(strReportID.equals("ClaimShortfallSummary"))
		else if(strReportID.equals("ShortfallIns"))
		{
			return(strSHORTFALL_GENERATE_LETTER_preauth);
		}//end of else if(strReportID.equals("ShortfallIns"))
		else if(strReportID.equals("ShortfallMid"))
		{
			return(strSHORTFALL_GENERATE_LETTER_preauth);
		}//end of else if(strReportID.equals("ShortfallMid"))
		else if(strReportID.equals("ShortfallINM"))
		{
			return(strSHORTFALL_GENERATE_LETTER_preauth);
		}//end of else if(strReportID.equals("ShortfallINM"))
		else if(strReportID.equals("AuthLetter"))
		{
			return(strPRE_AUTH_GENERATE_LETTER);
		}//end of else if(strReportID.equals("AuthLetter"))
		else if(strReportID.equals("CitiAuthLetter"))
		{
			return(strPRE_AUTH_CITI_GENERATE_LETTER);
		}//end of else if(strReportID.equals("CitiAuthLetter"))		
		else if(strReportID.equals("DischargeVoucher"))
		{
			return(strVOUCHER_LIST);
		}//end of else if(strReportID.equals("DischargeVoucher"))
		else if(strReportID.equals("MediClaimCom"))
		{
			return(strCLAIM_COMPUTATION);
		}//end of else if(strReportID.equals("MediClaimCom"))
		else if(strReportID.equals("MediClaimComLineItems"))
		{
			return(strCLAIM_COMPUTATION_LINEITEMS);
		}//end of else if(strReportID.equals("MediClaimComLineItems"))
		else if(strReportID.equals("ClaimsACK"))
		{
			return(strPRINT_ACKNOWLEDGEMENT);
		}//end of else if(strReportID.equals("ClaimsACK"))
		else if(strReportID.equals("ClaimsACKSub"))
		{
			return(strPRINT_ACK_CHECK_LIST);
		}//end of else if(strReportID.equals("ClaimsACKSub"))
		else if(strReportID.equals("CardPrint"))
		{
			return(strSELECT_DATA_FOR_CARD_PRINT);
		}//end of else if(strReportID.equals("CardPrint"))
		
		else if(strReportID.equals("CardPrint1"))
		{
			return(strSELECT_DATA_FOR_CARD_PRINTInd);
		}//end of else if(strReportID.equals("CardPrint"))
		
		
//		Online Reports
		else if(strReportID.equals("EmpCredential"))
		{
			return(strEMP_CRDN_RPT_FOR_HR);
		}//end of else if(strReportID.equals("EmpCredential"))
		else if(strReportID.equals("ClmRegd"))
		{
			return(strCLAIMS_REGISTERED);
		}//end of else if(strReportID.equals("ClmRegd"))
		else if(strReportID.equals("BiilsPend"))
		{
			return(strBILLS_PENDING);
		}//end of else if(strReportID.equals("BiilsPend"))
		else if(strReportID.equals("ListEmpDepPeriod"))
		{
			return(strLIST_OF_EMP_AND_DEPENDENTS);
		}//end of else if(strReportID.equals("ListEmpDepPeriod"))
		//Summary CR KOC1224
		else if(strReportID.equals("OnlineSummaryRpt"))
		{
			return(strONLINE_SUMMARY_REPORT);
		}//end of else if(strReportID.equals("OnlineSummaryRpt"))
		else if(strReportID.equals("ListEmpDepTill"))
		{
			return(strLIST_OF_EMP_AND_DEPENDENTS);
		}//end of else if(strReportID.equals("ListEmpDepTill"))
		else if(strReportID.equals("OnlinePreAuthRpt"))
		{
			return(strINS_PREAUTH_REPORT);
		}//end of else if(strReportID.equals("OnlinePreAuthRpt"))
		
		//added new for kocbroker
		
		else if(strReportID.equals("DashBoardRpt"))
		{
			return(strDashBoard_REPORT);
		}//end of else if(strReportID.equals("DashBoard"))
		else if(strReportID.equals("Dashboardsub"))
		{
			return(strDashBoardSub_REPORT);
		}//end of else if(strReportID.equals("DashBoard"))
		
	
		//On select IDs kocbroker
		else if(strReportID.equals("ReportOnlinePreAuthBroRpt"))
		{
			return(strBRO_PREAUTH_REPORT);
		}//end of else if(strReportID.equals("ReportOnlinePreAuthBroRpt"))
		else if(strReportID.equals("ReportPreAuthBroSummary"))
		{
			return(strBRO_PREAUTH_SUMMARY_RPT);
		}//end of else if(strReportID.equals("ReportPreAuthBroSummary"))
		else if(strReportID.equals("ReportClmRegBroSummary"))
		{
			return(strBRO_CLM_REGISTER_RPT);
		}//end of else if(strReportID.equals("ReportClmRegBroSummary"))	
		else if(strReportID.equals("ReportClmRegBroDetail"))
		{
			return(strBRO_CLM_REGISTER_DTL_RPT);
		}//end of else if(strReportID.equals("ReportClmRegBroDetail"))	
		else if(strReportID.equals("OnlinePreAuthBroRpt"))
		{
			return(strBRO_PREAUTH_REPORT);
		}//end of else if(strReportID.equals("OnlinePreAuthBroRpt"))
		else if(strReportID.equals("PreAuthBroSummary"))
		{
			return(strBRO_PREAUTH_SUMMARY_RPT);
		}//end of else if(strReportID.equals("PreAuthBroSummary"))
		else if(strReportID.equals("ClmRegBroSummary"))
		{
			return(strBRO_CLM_REGISTER_RPT);
		}//end of else if(strReportID.equals("ClmRegBroSummary"))
		else if(strReportID.equals("ClmRegBroDetail"))
		{
			return(strBRO_CLM_REGISTER_DTL_RPT);
		}//end of else if(strReportID.equals("ClmRegBroDetail"))	
		
		//added new for kocbroker
		else if(strReportID.equals("ListBroEmpDepPeriod"))
		{
			return(strBRO_Emp_Dep_REPORT);
		}//end of else if(strReportID.equals("ListBroEmpDepPeriod"))
		else if(strReportID.equals("ReportListBroEmpDepPeriod"))
		{
			return(strBRO_Emp_Dep_REPORT);
		}//end of else if(strReportID.equals("ReportListBroEmpDepPeriod"))
		
		//End of broker reports
		
		else if(strReportID.equals("ClmRegSummary"))
		{
			return(strINS_CLM_REGISTER_RPT);
		}//end of else if(strReportID.equals("ClmRegSummary"))		
		else if(strReportID.equals("ClmRegDetail"))
		{
			return(strINS_CLM_REGISTER_DTL_RPT);
		}//end of else if(strReportID.equals("ClmRegDetail"))
		//KOC 1339 for mail
		
		else if(strReportID.equals("ClaimsIntimations"))
		{
			return(strONLINE_CLM_INTIMATION);
		}//end of else if(strReportID.equals("ClaimsIntimations"))
		
		//KOC 1339 for mail
		else if(strReportID.equals("CheqCovrLett"))
		{
			return(strCheque_Covering_Letter);
		}//end of else if(strReportID.equals("CheqCovrLett"))
		else if(strReportID.equals("FinAddreLebel"))
		{
			return(strAddress_Label);
		}//end of else if(strReportID.equals("FinAddreLebel"))
		
//		web reports
		else if(strReportID.equals("GenWebHospXLs"))
		{
			return(strWEBLOGIN_HOSPITAL_LIST);
		}//end of else if(strReportID.equals("GenWebHospXLs"))
		else if(strReportID.equals("GenFaxStatRpt"))
		{
			return(strFAX_STATUS_RPT);
		}//end of else if(strReportID.equals("GenFaxStatRpt"))
		else if(strReportID.equals("CallPenRpt"))
		{
			return(strPROC_GEN_CALL_PENDING_REPORT);
		}//end of else if(strReportID.equals("CallPenRpt"))		
		else if(strReportID.equals("CallPenByBranRpt"))
		{
			return(strGET_CALLPENDING_BRANCH_REPORT);
		}//end of else if(strReportID.equals("CallPenByBranRpt"))
//		citi bank reports
		else if(strReportID.equals("CitiSoftCpyUpl"))
		{
			return(strSOFT_COPY_UPLOAD_REPORT);
		}//end of else if(strReportID.equals("CitiSoftCpyUpl"))		
		else if(strReportID.equals("CitiSoftCpyCncl"))
		{
			return(strSOFT_COPY_CANCELLATION_REPORT);
		}//end of else if(strReportID.equals("CitiSoftCpyCncl"))
		else if(strReportID.equals("CitiSoftCpyNtCncl"))
		{
			return(strNOT_CANCELLED_REPORT);
		}//end of else if(strReportID.equals("CitiSoftCpyNtCncl"))	
		//IOB Report
		else if(strReportID.equals("IOBBatRpt"))
		{
			return(strIOB_DETAIL_REPORT);
		}//end of else if(strReportID.equals("IOBBatRpt"))
		else if(strReportID.equals("HDFCCert"))
		{
			return(strHDFC_CERTIFICATE);
		}//end of else if(strReportID.equals("HDFCCert"))	
		else if(strReportID.equals("HDFCCertMem"))
		{
			return(strHDFC_CERTIFICATE_MEMBER);
		}//end of else if(strReportID.equals("HDFCCertMem"))
		else if(strReportID.equals("CallCoverList"))
		{
			return(strCALL_COVERAGE_LIST);
		}//end of else if(strReportID.equals("ClaAssDocList"))
		else if(strReportID.equals("ClaAssDocList"))
		{
			return(strCALL_CLAUSE_ASSOC_DOC_LIST);
		}//end of else if(strReportID.equals("ClaAssDocList"))
		else if(strReportID.equals("CallHosList"))
		{
			return(strCALL_HOSPITAL_LIST);
		}//end of else if(strReportID.equals("CallHosList"))
		else if(strReportID.equals("CallPendMRRpt"))
		{
			return(strPROC_GEN_MRCLM_PENDING);
		}//end of else if(strReportID.equals("CallPendMRRpt"))
		else if(strReportID.equals("CallPendMRShrtfallRpt"))
		{
			return(strPROC_GEN_MRCLM_PENDING_SRTFLL);
		}//end of else if(strReportID.equals("CallPendMRShrtfallRpt"))
		else if(strReportID.equals("TDSRemittanceRpt"))
		{
			return(strTDS_DETAIL_REPORT);
		}//end of else if(strReportID.equals("TDSRemittanceRpt"))	
		else if(strReportID.equals("FamiliesWOSI"))
		{
			return(strSELECT_EMP_WITHOUT_SUM_LIST);
		}//end of else if(strReportID.equals("FamiliesWOSI"))
		else if(strReportID.equals("MiscePreAuthsandClaims"))
		{
			return(strSELECT_EMP_WITHOUT_SUM_LIST);
		}//end of else if(strReportID.equals("FamiliesWOSI"))
		else if(strReportID.equals("MisceBufferUtil"))
		{
			return(strSELECT_EMP_WITHOUT_SUM_LIST);
		}//end of else if(strReportID.equals("FamiliesWOSI"))
		else if(strReportID.equals("MisceSIUtil"))
		{
			return(strSELECT_EMP_WITHOUT_SUM_LIST);
		}//end of else if(strReportID.equals("FamiliesWOSI"))
		else if(strReportID.equals("UniSampoPenRpt"))
		{
			return(strUNIVERSAL_SOMPO_PENDING_REPORT);
		}//end of else if(strReportID.equals("UniSampoPenRpt"))
		else if(strReportID.equals("DailyTansferRpt"))
		{
			return(strDAILY_TRANSFER_SUMMARY_REPORT);
		}//end of else if(strReportID.equals("DailyTansferRpt"))
		else if(strReportID.equals("MonthlyRemitRpt"))
		{
			return(strMONTHLY_REMIT_SUMARY_REPORT);
		}//end of else if(strReportID.equals("MonthlyRemitRpt"))
		else if(strReportID.equals("Annexure126qRpt"))
		{
			return(strANNEXURE_I26Q_REPORT);
		}//end of else if(strReportID.equals("Annexure126qRpt"))
		else if(strReportID.equals("ChallanDetailsRpt"))
		{
			return(strCHALLAN_DETAILS_Q_REPORT);
		}//end of else if(strReportID.equals("ChallanDetailsRpt"))
		else if(strReportID.equals("CheBatchNo"))
		{
			return(strPrintChequeInfo);
		}//end of else if(strReportID.equals("CheBatchNo"))
		else if(strReportID.equals("TDSCertGen"))
		{
			return(strSelectTdsCert);
		}//end of else if(strReportID.equals("TDSCertGen"))
		else if(strReportID.equals("AckQuaterInfo"))
		{
			return(strAckQuarterInfo);
		}//end of else if(strReportID.equals("AckQuaterInfo"))
		else if(strReportID.equals("TDSAnneSum"))
		{
			return(strTdsAnneSummary);
		}//end of else if(strReportID.equals("TDSAnneSum"))		
		else if(strReportID.equals("TDSAnneClmWise"))
		{
			return(strAnneClmWiseBreakup);
		}//end of else if(strReportID.equals("TDSAnneClmWise"))
		else if(strReportID.equals("TDSCovLett"))
		{
			return(strTdsCovLetter);
		}//end of else if(strReportID.equals("TDSCovLett"))
		/*else if(strReportID.equals("EnrDaiEndorse"))
		{
			return(strENDORSEMENTS);
		}//end of else if(strReportID.equals("EnrDaiEndorse"))
*/		else if(strReportID.equals("JobStatusRpt"))
		{
			return(strJOB_STATUS_RPT);
		}//end of else if(strReportID.equals(""))
		else if(strReportID.equals("GrpEnrollRpt"))
		{  
			return strGRP_SUMMARY_REPORT;
		}//else if(strReportID.equals("GrpEnrollRpt"))
		else if(strReportID.equals("ActivePolicyDetails"))
		{
			return strPOLICY_DETAILS_REPORT;
		}//else if(strReportID.equals("ActivePolicyDetails"))
		
		else if(strReportID.equals("GrpPreAuthRpt"))
		{
			return strPREAUTH_GRP_REPORT;
		}//else if(strReportID.equals("GrpPreAuthRpt"))
		else if (strReportID.equals("GrpOnlineTATRpt"))
		{
			return strONLINE_TAT_REPORT;
		}//end of else if (strReportID.equals("GrpOnlineTATRpt"))
		//added as per Bajaj Change Request...........................................................
		else if (strReportID.equals("PatpolHistoryApprej"))
		{
			return strPatGenPolicyHistory ;
		}//end of else if (strReportID.equals("PatpolHistoryApprej"))
		
		else if (strReportID.equals("PatHistoryApprej"))
		{
			return strPatPreAuthHistory;
		}//end of else if (strReportID.equals("PatHistoryApprej"))
		
		else if (strReportID.equals("PatBillHistory"))
		{
			return strPatBillDetails;
		}//end of else if (strReportID.equals("PatBillHistory"))
		
		else if (strReportID.equals("PatApproveRejectForm"))
		{
			return strInsurancePreAuthGenerateLetter;
		}//end of else if (strReportID.equals("PatApproveRejectForm"))
		else if (strReportID.equals("ClaimApproveRejectForm"))
		{
			return strInsClaimsGenerateLetter;
		}//end of else if (strReportID.equals("ClaimApproveRejectForm"))
		else if (strReportID.equals("ClmPolicyHistoryApprej"))
		{
			return strClaimsPolicyHistory;
		}//end of else if (strReportID.equals("ClmPolicyHistoryApprej"))
		else if (strReportID.equals("ClmHistoryApprej"))
		{
			return strPrevClaimsHistory;
		}//end of else if (strReportID.equals("ClmHistoryApprej"))
		else if (strReportID.equals("ClmBillHistory"))
		{
			return strClaimsBillHistory;
		}//end of 		else if (strReportID.equals("ClmBillHistory"))
		else if (strReportID.equals("claimintimation"))
		{
			return strClaimsIntimateSend;
		}//end of else if (strReportID.equals("claimintimation"))
		else if (strReportID.equals("preauthintimation"))
		{
			return strPreAuthIntimateSend;
		}//end of else if (strReportID.equals("preauthintimation"))//end of bajaj
		
		//added for 2koc
		else if (strReportID.equals("InOutBound"))
		{
			return strCourierDetails;
		}//end of else if (strReportID.equals("preauthintimation"))
		
		//end added for 2koc
			else if (strReportID.equals("AppRejPreAuthList"))//1274A
		{
			return strAppRejePreAuthsList ;
		}//end of else if (strReportID.equals("AppRejPreAuthList"))
		else if (strReportID.equals("AppRejClaimsList"))
		{
			return strAppRejeClaimsList;
		}//end of else if (strReportID.equals("AppRejClaimsList"))//end of 1274A
		else if (strReportID.equals("HospitalBillsPend"))//added as per Hospital login
		{
			return strHospitalBillsPend;
		}//end of else if (strReportID.equals("HospitalBillsPend"))
		else if (strReportID.equals("HospitalClmRegd"))
		{
			return strHospitalClmRegd;
		}//end of else if (strReportID.equals("HospitalClmRegd"))
		else if (strReportID.equals("HospitalOnlineSummaryRpt"))
		{
			return strHospitalOnlineSummaryRptClaims;
		}//end of else if (strReportID.equals("HospitalOnlineSummaryRpt"))//added as per Hospital login 
		else if (strReportID.equals("CustCallBack"))
		{
			return strCUSTOMERCALLBACKREPORT;
		}//end of else if (strReportID.equals("CustCallBack"))//added as per Insurance Login
		else if (strReportID.equals("ClaimUtilizationRep"))
		{
			return strCLAIMUTILIZATIONREP;
		}//end of else if (strReportID.equals("CustCallBack"))//added as per Insurance Login
		else if (strReportID.equals("AuthUtilizationRep"))
		{
			return strAUTHUTILIZATIONREP;
		}//end of else if (strReportID.equals("CustCallBack"))//added as per Insurance Login
		else if (strReportID.equals("TATForClaims"))
		{
			return strTATFORCLAIMS;
		}//end of else if (strReportID.equals("CustCallBack"))//added as per Insurance Login
		else if (strReportID.equals("TATForPreAuth"))
		{
			return strTATForPreAuth;
		}//end of else if (strReportID.equals("CustCallBack"))//added as per Insurance Login
		else if (strReportID.equals("TechnicalResultReport"))
		{
			return strTRRForPreAuth;
		}//end of else if (strReportID.equals("CustCallBack"))//added as per Insurance Login
		else if (strReportID.equals("TATFSCORPOARTE"))
		{
			return strTATFSCORPOARTE;
		}//end of else if (strReportID.equals("CustCallBack"))//added as per Insurance Login
		
		else if (strReportID.equals("TATFSINDIVIDUAL"))
		{
			return strTATFSINDIVIDUAL;
		}//	if (strReportID.equals("TATFSINDIVIDUAL"))
		 else if (strReportID.equals("chequeWiseReport"))
 		{
			 return strCHEQUEWISEREPORT;
 		}//end of else if (strReportID.equals("CustCallBack"))//added as per intX preauth Utilization report 
		 else if (strReportID.equals("batchReconciliation"))
 		{
			 return strBATCHRECONCILIATION;
 		}//end of else if (strReportID.equals("batchReconciliation"))//added as per intX provider Login 
		 else if (strReportID.equals("batchInvoice"))
 		{
			 return strBATCHINVOICE;
 		}//end of else if (strReportID.equals("batchReconciliation"))//added as per intX provider Login 
		 else if (strReportID.equals("generatePreAuthLetter"))
		{
			 return strGENERATEPREAUTHLETTER;
		}//end of 
		else if (strReportID.equals("financeDashBoard"))//added as per intX provider Login else if (strReportID.equals("generatePreAuthLetter"))
		{
			 return strFINACNEDASHBOARD;
		}//end of else if (strReportID.equals("batchReconciliation"))//added as per intX provider Login 
		else if (strReportID.equals("InvoiceWiseReport"))//added as per intX provider Login else if (strReportID.equals("generatePreAuthLetter"))
		{
			 return strINVOICEWISEREPORT;
		}//end of else if (strReportID.equals("batchReconciliation"))//added as per intX provider Login 
		else if (strReportID.equals("overdueReport"))//added as per intX provider Login else if (strReportID.equals("generatePreAuthLetter"))
		{
			 return strOVERDUEREPORT;
		}//end of else if (strReportID.equals("batchReconciliation"))//added as per intX provider Login 
		else if (strReportID.equals("OnlinePreAuthForm"))//added as per intX provider Login else if (strReportID.equals("OnlinePreAuthForm"))
		{
			 return strONLINEPREAUTHFORM;
		}//end of else if (strReportID.equals("batchReconciliation"))//added as per intX provider Login 
		else if (strReportID.equals("OnlineDentalForm"))//added as per intX provider Login else if (strReportID.equals("OnlineDentalForm"))
		{
			 return strONLINEDENTALFORM;
		}//end of else if (strReportID.equals("batchReconciliation"))//added as per intX provider Login 
		else if("BroClaimUtilizationRep".equals(strReportID)){
			return strBRO_CLAIMUTILIZATIONREP;
		}		
		else if("BroAuthUtilizationRep".equals(strReportID)){
			return strBRO_AUTHUTILIZATIONREP;
		}		
		else if("BroTATForClaims".equals(strReportID)){
			return strBRO_TATFORCLAIMS;
		}
		else if("BroTATForPreAuth".equals(strReportID)){
			return strBRO_TATFORPREAUTH;
		}
		else if("BroTechnicalResultReport".equals(strReportID)){
			return strBRO_TECHNICALRESULTREPORT;
		}
		else if("BroTATFSCORPOARTE".equals(strReportID)){
			return strBRO_TATFSCORPOARTE;
		}
		else if("BroTATFSINDIVIDUAL".equals(strReportID)){
			return strBRO_TATFSINDIVIDUAL;
		}
		else if("BroHIPA".equals(strReportID)){
			return strBRO_HIPA;
		}		
		else  if (strReportID.equals("preAuthReferral"))
		{
			return(strPREAUTH_REFERRAL_REP);
		}
		else  if (strReportID.equals("providerLoginPreAuthReports"))
		{
			return(strProviderPreAuthReports);
		}
		else  if (strReportID.equals("providerLoginClaimReports"))
		{
			return(strProviderClaimReports);
		}
		//end of else		
		else  if ("FinClmSetldRpt".equals(strReportID))
		{
			return(strFIN_CLM_SETLD_RPT);
		}else  if ("FinClmInpRpt".equals(strReportID))
		{
			return(strFIN_CLM_INP_RPT);
		}else  if ("FinClmOutRpt".equals(strReportID))
		{
			return(strFIN_CLM_OUT_RPT);
		}	
		else if(strReportID.equals("MedicialCertificate"))
		{
			return(strSELECT_MEDICAL_CERTIFICATE);
		}//end of else if(strReportID.equals("CardPrint"))
		else if(strReportID.equals("claimUploadErrorLog"))
		{
			return(strClaim_Upload_Error_Log);
		}//end of else if(strReportID.equals("CardPrint"))
		else if(strReportID.equals("tariffUploadErrorLog"))
		{
			return(strTariff_Upload_Error_Log);
		}//end of else if(strReportID.equals("CardPrint"))
		else  if (strReportID.equals("ClaimDetailedReport"))
		{
			return(strClaimDetailedReport);
		}
		
		else  if (strReportID.equals("pbmClaimDrugReport"))
		{
			return(strPBM_REPORTS_ALL);
		}//end of else 
		
		else  if (strReportID.equals("TATRpt"))
		{
			return(strTATRpt);
		}
		else if(strReportID.equals("UnderwritingRpt"))
		{

			return(strUnderwritingReport);	
		}
		else if(strReportID.equals("FinPreAuthRpt"))
		{

			return(strFinancepreauthReport);
		}
		else  if (strReportID.equals("ProductivityRpt"))
		{
			return(strProductivityRpt);
		}
		else  if (strReportID.equals("DetailedRpt"))
		{
			return(strDetailedRpt);
		}
		else  if (strReportID.equals("ActivityRpt"))
		{
			return(strActivityRpt);
		}
		else  if (strReportID.equals("productivityRpt"))
		{
			return(strPreApprovalReports);
		}
		else  if (strReportID.equals("ActivityLogRpt"))
		{
			return(strActivityLogReports);
		}
		else  if (strReportID.equals("ClaimDiscountActivityReport"))
		{
			return(strClaimDiscountActivityReport);
		}
		else  if (strReportID.equals("PBMLoginClaimReports"))
		{
			return(strPBMClaimReports);
		}
		{
			return(strPR_HOSP_DOC_RCVD_REPORT);
		}//end of else		
		
	}//end of getProcedureName(String strReportID)
	
	
	
public ArrayList<String[]> getClaimsReportForXls(String strReportID,String strParameter) throws TTKException {
		
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        String strProcedureName="";
        ArrayList<String[]> claimsData = new ArrayList<>();
        
        strProcedureName =getProcedureName(strReportID);
        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }//end of if(strProcedureName==null)
        OracleCachedRowSet crs = null;
        try{
            String strCall = "{CALL "+strProcedureName.trim()+"(?,?)}";
           
            log.debug("strCall:::::::::::::"+strCall);
            log.debug("strParameter:::::::::::::"+strParameter);
            crs = new OracleCachedRowSet();
            conn = ResourceManager.getConnection();//getTestConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,strParameter);
            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(2); 
            
            if(rs !=null)
            {           	            	
            	String[] strTemp	=	null;
				while(rs.next()){
					strTemp	=	new String[100];
					strTemp[0]	=	TTKCommon.checkNull(rs.getString("ins_comp_code_number"));
					strTemp[1]	=	TTKCommon.checkNull(rs.getString("office_code"));
					strTemp[2]	=	TTKCommon.checkNull(rs.getString("abbrevation_code"));
					strTemp[3]	=	null; //rs.getString(null); // start date
					strTemp[4]	=	null;//rs.getString(null); // end date
					strTemp[5]	=	TTKCommon.checkNull(rs.getString("office_name"));
					strTemp[6]	=	TTKCommon.checkNull(rs.getString("ins_comp_name"));
					strTemp[7]	=	TTKCommon.checkNull(rs.getString("tpa_id_no")); // alkoot id
					strTemp[8]	=	TTKCommon.checkNull(rs.getString("group_name"));
					strTemp[9]	=	TTKCommon.checkNull(rs.getString("policy_number"));
					strTemp[10]	=	TTKCommon.checkNull(rs.getString("policy_agent_code"));
					strTemp[11] =   TTKCommon.checkNull(rs.getString("claim_settlement_no"));
					strTemp[12] =  TTKCommon.checkNull(rs.getString("claim_Intimation_Date"));
					strTemp[13] =  TTKCommon.checkNull(rs.getString("claim_approved_date"));
					strTemp[14] = TTKCommon.checkNull(rs.getString("insured_name"));
					strTemp[15] = TTKCommon.checkNull(rs.getString("claimant_name"));
					strTemp[16] = TTKCommon.checkNull(rs.getString("mem_age"));
					strTemp[17] = TTKCommon.checkNull(rs.getString("relship_description"));
					strTemp[18] = TTKCommon.checkNull(rs.getString("hosp_name"));
					strTemp[19] = TTKCommon.checkNull(rs.getString("ailment"));
					strTemp[20] = TTKCommon.checkNull(rs.getString("date_of_admission"));
					strTemp[21] = TTKCommon.checkNull(rs.getString("date_of_discharge"));
					strTemp[22] = TTKCommon.checkNull(rs.getString("requested_amount"));
					strTemp[23] = TTKCommon.checkNull(rs.getString("INCURRED_CURRENCY"));
					strTemp[24] = TTKCommon.checkNull(rs.getString("converted_amount"));
					strTemp[25] = TTKCommon.checkNull(rs.getString("convertd_currency"));
					strTemp[26] = TTKCommon.checkNull(rs.getString("CONVERSION_RATE_TO_QAR"));
					strTemp[27] = TTKCommon.checkNull(rs.getString("disc_amt"));
					strTemp[28] = TTKCommon.checkNull(rs.getString("remarks"));
					strTemp[29] = TTKCommon.checkNull(rs.getString("total_app_amount"));
//					strTemp[30] = TTKCommon.checkNull(rs.getString("convertd_currency"));
					strTemp[30] = TTKCommon.checkNull(rs.getString("Amount_Payable_in_Incrd_curncy"));
					strTemp[31] = TTKCommon.checkNull(rs.getString("Payable_in_Incrd_curncy"));
					strTemp[32] = TTKCommon.checkNull(rs.getString("usd_amount"));
					strTemp[33] = TTKCommon.checkNull(rs.getString("amt_paid_beneficiary"));
					strTemp[34] = TTKCommon.checkNull(rs.getString("amt_paid_hospital"));
					strTemp[35] = TTKCommon.checkNull(rs.getString("balance_sum_insured"));
					strTemp[36] = TTKCommon.checkNull(rs.getString("sum_insured"));
					strTemp[37] = TTKCommon.checkNull(rs.getString("emp_no"));
					strTemp[38] = TTKCommon.checkNull(rs.getString("bill_nos"));
					strTemp[39] = TTKCommon.checkNull(rs.getString("policy_start_date"));
					strTemp[40] = TTKCommon.checkNull(rs.getString("policy_end_date"));
					
					
					strTemp[43] = TTKCommon.checkNull(rs.getString("claim_No"));
					
					if("FinPenRpt".equalsIgnoreCase(strReportID)){
					strTemp[41] = TTKCommon.checkNull(rs.getString("claim_payment_status"));
					strTemp[42] = TTKCommon.checkNull(rs.getString("claim_type"));
					strTemp[44] = TTKCommon.checkNull(rs.getString("Payment_mode"));
					strTemp[45] = TTKCommon.checkNull(rs.getString("cheque_date"));
					strTemp[46] = TTKCommon.checkNull(rs.getString("Batch_No"));
					strTemp[47] = TTKCommon.checkNull(rs.getString("trans_ref_num"));
					strTemp[48] = TTKCommon.checkNull(rs.getString("partner_name"));
					strTemp[49] = TTKCommon.checkNull(rs.getString("payment_ref_no"));
					strTemp[50] = TTKCommon.checkNull(rs.getString("IBANNo"));
					strTemp[51] = TTKCommon.checkNull(rs.getString("ageing_recived_date"));
					strTemp[52] = TTKCommon.checkNull(rs.getString("ageing_approved_date"));
					strTemp[53] = TTKCommon.checkNull(rs.getString("CLAIM_BATCH_NO"));//
					strTemp[54] = TTKCommon.checkNull(rs.getString("EMPANEL_NUMBER"));	
					strTemp[55] = TTKCommon.checkNull(rs.getString("BANK_NAME"));
					strTemp[56] = TTKCommon.checkNull(rs.getString("ACC_NUM"));
					strTemp[57] = TTKCommon.checkNull(rs.getString("SWIFT_CODE"));
					strTemp[58] = TTKCommon.checkNull(rs.getString("ACC_NAME"));
					}
					if("FinDetRpt".equalsIgnoreCase(strReportID)){
						strTemp[44] = TTKCommon.checkNull(rs.getString("cheque_amount"));  //rs.getString("total_app_amount_company_code_sum");
						strTemp[45] = TTKCommon.checkNull(rs.getString("deposit_to"));    //rs.getString("total_app_amount_company_name_sum");
						strTemp[46] = TTKCommon.checkNull(rs.getString("batch_number")); // rs.getString("total_app_amount_office_name_sum");
						strTemp[47] = TTKCommon.checkNull(rs.getString("payment_mode")); // rs.getString("claim_total_app_amount_grand_sum");
						strTemp[48] = TTKCommon.checkNull(rs.getString("check_num"));
						
						strTemp[49] = TTKCommon.checkNull(rs.getString("CHEQUE_STATUS"));
						strTemp[50] = TTKCommon.checkNull(rs.getString("check_date"));
						strTemp[51] = TTKCommon.checkNull(rs.getString("payee_name"));
						strTemp[52] = TTKCommon.checkNull(rs.getString("trans_ref_num"));
						strTemp[53] = TTKCommon.checkNull(rs.getString("partner_name"));
					}
					claimsData.add(strTemp);
            	 
            }//end of if(rs !=null)
            
            }
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
        }//end of catch (Exception exp)
        finally
        {
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (rs != null) rs.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Resultset in TTKReportDAOImpl getPolicyList()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
        		{
        			try
        			{
        				if (cStmtObject != null) cStmtObject.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        			{
        				try
        				{
        					if(conn != null) conn.close();
        				}//end of try
        				catch (SQLException sqlExp)
        				{
        					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
        					throw new TTKException(sqlExp, "tTkReports");
        				}//end of catch (SQLException sqlExp)
        			}//end of finally Connection Close
        		}//end of try
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs = null;
        		cStmtObject = null;
        		conn = null;        		
        	}//end of finally
        }//end of finally
	  return claimsData;
	}

public ArrayList<String[]> getFinanceReport(String repType, String parameter) throws TTKException {
	ArrayList<String[]> financeDataList=new ArrayList<>();
	String strProcedureName=getProcedureName(repType);
	String strCall = "{CALL "+strProcedureName.trim()+"(?,?)}";
	log.info("parameter::"+parameter);
	log.info("strProcedureName::"+strCall);
	
	try(Connection connection = ResourceManager.getMISConnection();
		CallableStatement callableStatement=connection.prepareCall(strCall)){
		callableStatement.setString(1,parameter);
		callableStatement.registerOutParameter(2,OracleTypes.CURSOR);
		callableStatement.execute();
         try(ResultSet resultSet = (java.sql.ResultSet) callableStatement.getObject(2)){
        	 log.info("Execution has done......");      	
            String[] strTemp	=	null;
            while(resultSet.next()){
			strTemp	=	new String[56];
			strTemp[0]	=	TTKCommon.checkNull(resultSet.getString("Corporate_Name"));
			strTemp[1]	=	TTKCommon.checkNull(resultSet.getString("policy_number"));
			strTemp[2]	=	TTKCommon.checkNull(resultSet.getString("policy_start_date"));
			strTemp[3]	=	TTKCommon.checkNull(resultSet.getString("policy_end_date"));
			strTemp[4]	=	TTKCommon.checkNull(resultSet.getString("alkootid"));// alkoot id
			strTemp[5]	=	TTKCommon.checkNull(resultSet.getString("EMPLOYEE_NO"));// employee_no
			strTemp[6]	=	TTKCommon.checkNull(resultSet.getString("principal_name")); 
			strTemp[7]	=	TTKCommon.checkNull(resultSet.getString("member_name"));
			strTemp[8]	=	TTKCommon.checkNull(resultSet.getString("relation"));
			strTemp[9]	=	TTKCommon.checkNull(resultSet.getString("name_of_partner"));
			strTemp[10] =   TTKCommon.checkNull(resultSet.getString("name_of_provider"));
			strTemp[11] =  TTKCommon.checkNull(resultSet.getString("empanelment_id"));
			strTemp[12] =  TTKCommon.checkNull(resultSet.getString("invoice_number"));
			strTemp[13] = TTKCommon.checkNull(resultSet.getString("dt_of_admission"));
			strTemp[14] = TTKCommon.checkNull(resultSet.getString("dt_of_discharge"));
			strTemp[15] = TTKCommon.checkNull(resultSet.getString("claim_type"));
			strTemp[16] = TTKCommon.checkNull(resultSet.getString("claim_batch_no"));
			strTemp[17] = TTKCommon.checkNull(resultSet.getString("claim_from"));
			strTemp[18] = TTKCommon.checkNull(resultSet.getString("claim_received_date"));
			strTemp[19] = TTKCommon.checkNull(resultSet.getString("claim_added_date"));
			strTemp[20] = TTKCommon.checkNull(resultSet.getString("claim_number"));
			strTemp[21] = TTKCommon.checkNull(resultSet.getString("total_claimed_amt"));
			strTemp[22] = TTKCommon.checkNull(resultSet.getString("TOT_NET_AMOUNT"));
			strTemp[23] = TTKCommon.checkNull(resultSet.getString("incurred_currency"));
			/*strTemp[24] = TTKCommon.checkNull(resultSet.getString("total_claim_amount_qar"));*/
			strTemp[24] = TTKCommon.checkNull(resultSet.getString("conversion_rate"));
			strTemp[25] = TTKCommon.checkNull(resultSet.getString("claim_status"));
			strTemp[26] = TTKCommon.checkNull(resultSet.getString("completed_date"));
			strTemp[27] = TTKCommon.checkNull(resultSet.getString("claim_settlement_no"));
			strTemp[28] = TTKCommon.checkNull(resultSet.getString("amount_payable_qar"));
			strTemp[29] = TTKCommon.checkNull(resultSet.getString("discount_amount"));
			strTemp[30] = TTKCommon.checkNull(resultSet.getString("payable_amt"));
			strTemp[31] = TTKCommon.checkNull(resultSet.getString("amount_payable_icur"));
			strTemp[32] = TTKCommon.checkNull(resultSet.getString("amount_payable_usd"));
			strTemp[33] = TTKCommon.checkNull(resultSet.getString("PAY_AMT_IN_GBP"));
			strTemp[34] = TTKCommon.checkNull(resultSet.getString("PAY_AMT_IN_EURO"));
			strTemp[35] = TTKCommon.checkNull(resultSet.getString("finance_status"));
			strTemp[36] = TTKCommon.checkNull(resultSet.getString("ageing_as_per_rec_date"));
			strTemp[37] = TTKCommon.checkNull(resultSet.getString("ageing_band"));
			strTemp[38] = TTKCommon.checkNull(resultSet.getString("ageing_as_per_apr_date"));
			strTemp[39] = TTKCommon.checkNull(resultSet.getString("PAYMENT_DUR_AGR"));
			strTemp[40] = TTKCommon.checkNull(resultSet.getString("bank_location"));
			strTemp[41] = TTKCommon.checkNull(resultSet.getString("benf_bank_name"));
			strTemp[42] = TTKCommon.checkNull(resultSet.getString("Payment_To"));
			strTemp[43] = TTKCommon.checkNull(resultSet.getString("benf_name"));
			strTemp[44] = TTKCommon.checkNull(resultSet.getString("cust_iban_no"));
			strTemp[45] = TTKCommon.checkNull(resultSet.getString("cust_acc_num"));			
			strTemp[46] = TTKCommon.checkNull(resultSet.getString("cust_swift_code"));
			strTemp[47] = TTKCommon.checkNull(resultSet.getString("bank_city"));
			strTemp[48] = TTKCommon.checkNull(resultSet.getString("bank_country"));
			strTemp[49] = TTKCommon.checkNull(resultSet.getString("batch_date"));
			strTemp[50] = TTKCommon.checkNull(resultSet.getString("finance_batch_no"));
			strTemp[51] = TTKCommon.checkNull(resultSet.getString("transaction_reference_no"));
			strTemp[52] = TTKCommon.checkNull(resultSet.getString("payment_reference_no"));
			strTemp[53] = TTKCommon.checkNull(resultSet.getString("check_date"));
			strTemp[54] = TTKCommon.checkNull(resultSet.getString("Vidal_reference_number"));
			
			financeDataList.add(strTemp);
 }//end of if(rs !=null)
        }
	}catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	return financeDataList;
}


public ArrayList<String[]> getChequeInformationReport(String repType, String parameter) throws TTKException {
	ArrayList<String[]> financeDataList=new ArrayList<>();
	//String strProcedureName=getProcedureName(repType);
	//String strCall = "{CALL "+strProcedureName.trim()+"(?,?)}";
	String strCall ="{CALL CHEQUE_INFO_PKG.EXPORT_CHEQUE_INFORMATION(?,?)}";
	
	log.info("reportId::"+repType);
	log.info("parameter::"+parameter);
	log.info("strProcedureName::"+strCall);
	
	try(
		Connection connection = ResourceManager.getMISConnection();
		CallableStatement callableStatement=connection.prepareCall(strCall)){
		callableStatement.setString(1,parameter);
		callableStatement.registerOutParameter(2,OracleTypes.CURSOR);
		callableStatement.execute();
         try(
        	ResultSet resultSet = (java.sql.ResultSet) callableStatement.getObject(2)){
        	 log.info("Execution has done......");      	
            String[] strTemp	=	null;
            while(resultSet.next()){
			strTemp	=	new String[9];
			strTemp[0]	=	TTKCommon.checkNull(resultSet.getString("CHECK_NUM"));
			strTemp[1]	=	TTKCommon.checkNull(resultSet.getString("CHECK_STATUS"));
			strTemp[2]	=	TTKCommon.checkNull(resultSet.getString("CHECK_DATE"));//Date Type
			strTemp[3]	=	TTKCommon.checkNull(resultSet.getString("CLAIM_SETTLEMENT_NO"));
			strTemp[4]	=	TTKCommon.checkNull(resultSet.getString("CLAIM_TYPE"));
			strTemp[5]	=	TTKCommon.checkNull(resultSet.getString("IBAN_NO"));
			strTemp[6]	=	TTKCommon.checkNull(resultSet.getString("BATCH_DATE"));//Date Type
			strTemp[7]	=	TTKCommon.checkNull(resultSet.getString("APPROVED_AMOUNT"));
			strTemp[8]	=	TTKCommon.checkNull(resultSet.getString("TRANSFER_CURRENCY"));//BigDecimal
			financeDataList.add(strTemp);
 }//end of if(rs !=null)
        }
	}catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	return financeDataList;
}




public ArrayList<String[]> getAuditReport(ArrayList searchData, String reportType) throws TTKException {	
	String switchType=(String)searchData.get(searchData.size()-1);
	ArrayList<String[]> outputData=new ArrayList<>();
	String procedure=null;
	int coutParam=0;
	if("CLM".equals(switchType)){
		procedure=strAuditClaimReportGenerate;
		coutParam=17;
	}else{
		procedure=strAuditPreAuthReportGenerate;
		coutParam=14;
	}	
		try(Connection connection=ResourceManager.getConnection();
				OracleCachedRowSet cachedRowSet=new OracleCachedRowSet();	
				CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(procedure);){
				cStmtObject.setString(1, (searchData.get(0)==null)?"":(String) searchData.get(0));
				cStmtObject.setString(2, (searchData.get(1)==null)?"":(String) searchData.get(1));
				cStmtObject.setString(3, (searchData.get(2)==null)?"":(String) searchData.get(2));
				cStmtObject.setString(4, (searchData.get(3)==null)?"":(String) searchData.get(3));
				cStmtObject.setString(5, (searchData.get(4)==null)?"":(String) searchData.get(4));
				cStmtObject.setString(6, (searchData.get(5)==null)?"":(String) searchData.get(5));
				cStmtObject.setString(7, (searchData.get(6)==null)?"":(String) searchData.get(6));
				cStmtObject.setString(8, (searchData.get(7)==null)?"":(String) searchData.get(7));
				cStmtObject.setString(9, (searchData.get(8)==null)?"":(String) searchData.get(8));
				cStmtObject.setString(10, (searchData.get(9)==null)?"":(String) searchData.get(9));
				cStmtObject.setString(11, (searchData.get(10)==null)?"":(String) searchData.get(10));
				cStmtObject.setString(12, (searchData.get(11)==null)?"":(String) searchData.get(11));
				cStmtObject.setString(13, (searchData.get(12)==null)?"":(String) searchData.get(12));
				
				if("CLM".equals(switchType)){
					cStmtObject.setString(14, (searchData.get(13)==null)?"":(String) searchData.get(13));
					cStmtObject.setString(15, (searchData.get(14)==null)?"":(String) searchData.get(14));
					cStmtObject.setString(16, (searchData.get(15)==null)?"":(String) searchData.get(15));
				}
				cStmtObject.registerOutParameter(coutParam,OracleTypes.CURSOR);
		        cStmtObject.execute();
		        if("CLM".equals(switchType)){
		        	try(ResultSet rs = (java.sql.ResultSet) cStmtObject.getObject(coutParam)){
				        if(rs!=null){	
			         	String[] strTemp	=	null;
							while(rs.next()){
								strTemp	=	new String[104];
								strTemp[0]	=	TTKCommon.checkNull(rs.getString("CLAIM_NUMBER"));
								strTemp[1]	=	TTKCommon.checkNull(rs.getString("INVOICE_NUMBER"));
								strTemp[2]	=	TTKCommon.checkNull(rs.getString("PRE_AUTH_NUMBER"));
								strTemp[3]	=	TTKCommon.checkNull(rs.getString("CLAIM_TYPE"));
								strTemp[4]	=	TTKCommon.checkNull(rs.getString("GENDER"));
								strTemp[5]	=	TTKCommon.checkNull(rs.getString("MEMBER_INCEPTION_DATE"));
								strTemp[6]	=	TTKCommon.checkNull(rs.getString("POLICY_NUMBER"));
								strTemp[7]	=	TTKCommon.checkNull(rs.getString("POLICY_START_DATE"));
								strTemp[8]	=	TTKCommon.checkNull(rs.getString("POLICY_END_DATE"));
								strTemp[9]	=	TTKCommon.checkNull(rs.getString("CORPORATE_NAME"));
								strTemp[10]	=	TTKCommon.checkNull(rs.getString("SUBMISSION_CATAGORY"));
								strTemp[11]	=	TTKCommon.checkNull(rs.getString("CLAIM_SOURCE"));
								strTemp[12]	=	TTKCommon.checkNull(rs.getString("OVERRIDE_REMARKS"));
								strTemp[13]	=	TTKCommon.checkNull(rs.getString("OVERRIDE_DATE"));
								strTemp[14]	=	TTKCommon.checkNull(rs.getString("RECEIVED_DATE"));
								strTemp[15]	=	TTKCommon.checkNull(rs.getString("ADDED_DATE"));
								strTemp[16]	=	TTKCommon.checkNull(rs.getString("COMPLETED_DATE"));
								strTemp[17]	=	TTKCommon.checkNull(rs.getString("CLAIM_COMPLETED_YN"));
								strTemp[18]	=	TTKCommon.checkNull(rs.getString("ADMISSION_DATE"));
								strTemp[19]	=	TTKCommon.checkNull(rs.getString("DISCHARGE_DATE"));
								strTemp[20]	=	TTKCommon.checkNull(rs.getString("PROVIDER_NAME"));
								strTemp[21]	=	TTKCommon.checkNull(rs.getString("ENCOUNTER_TYPE"));
								strTemp[22]	=	TTKCommon.checkNull(rs.getString("BENEFIT_TYPE"));
								strTemp[23]	=	TTKCommon.checkNull(rs.getString("CLAIM_STATUS"));
								strTemp[24]	=	TTKCommon.checkNull(rs.getString("DENIAL_CODE"));
								strTemp[25]	=	TTKCommon.checkNull(rs.getString("DENIAL_DESCRIPTION"));
								strTemp[26]	=	TTKCommon.checkNull(rs.getString("TOOTH_NO"));
								strTemp[27]	=	TTKCommon.checkNull(rs.getString("QUANTITY"));
								strTemp[28]	=	TTKCommon.checkNull(rs.getString("PRIMARY_ICD_CODE"));
								strTemp[29]	=	TTKCommon.checkNull(rs.getString("PRIMARY_ICD_DESC"));
								strTemp[30]	=	TTKCommon.checkNull(rs.getString("SECONDARY_ICD_CODE"));
								strTemp[31]	=	TTKCommon.checkNull(rs.getString("SECONDARY_ICD_DESC"));
								strTemp[32]	=	TTKCommon.checkNull(rs.getString("PRESENTING_COMPLAINTS"));
								strTemp[33]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_CODE"));
								strTemp[34]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_DESCRIPTION"));
								strTemp[35]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_INTERNAL_DESCRIPTION"));
								strTemp[36]	=	TTKCommon.checkNull(rs.getString("REQUESTED_AMOUNT_ACTIVITY"));
								strTemp[37]	=	TTKCommon.checkNull(rs.getString("GROSS_AMOUNT"));
								strTemp[38]	=	TTKCommon.checkNull(rs.getString("DISCOUNT_AMOUNT"));
								strTemp[39]	=	TTKCommon.checkNull(rs.getString("DISC_GROSS_AMOUNT"));
								strTemp[40]	=	TTKCommon.checkNull(rs.getString("PATIENT_SHARE_AMOUNT"));
								strTemp[41]	=	TTKCommon.checkNull(rs.getString("NET_AMOUNT"));
								strTemp[42]	=	TTKCommon.checkNull(rs.getString("DISALLOWED_AMOUNT"));
								strTemp[43]	=	TTKCommon.checkNull(rs.getString("ALLOWED_AMOUNT"));
								strTemp[44]	=	TTKCommon.checkNull(rs.getString("APPROVED_AMOUNT"));
								strTemp[45]	=	TTKCommon.checkNull(rs.getString("FINAL_REMARKS"));
								strTemp[46]	=	TTKCommon.checkNull(rs.getString("MEDICAL_OPINION_REMARKS"));
								strTemp[47]	=	TTKCommon.checkNull(rs.getString("SERVICE_DESCRIPTION"));
								strTemp[48]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_QUANTITY_REQ"));
								strTemp[49]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_QUANTITY_APPROVED"));
								strTemp[50]	=	TTKCommon.checkNull(rs.getString("DATA_ENTRY_BY"));
								strTemp[51]	=	TTKCommon.checkNull(rs.getString("LAST_UPDATED_BY"));
								strTemp[52]	=	TTKCommon.checkNull(rs.getString("PROCESSED_BY"));
								strTemp[53]	=	TTKCommon.checkNull(rs.getString("PRIMARY_NETWORK_TYPE"));
								strTemp[54]	=	TTKCommon.checkNull(rs.getString("ENROLMENT_ID"));
								strTemp[55]	=	TTKCommon.checkNull(rs.getString("MEMBER_NAME"));
								strTemp[56]	=	TTKCommon.checkNull(rs.getString("RELSHIP_DESCRIPTION"));
								strTemp[57]	=	TTKCommon.checkNull(rs.getString("DOB"));
								strTemp[58]	=	TTKCommon.checkNull(rs.getString("MEMBER_AGE"));
								strTemp[59]	=	TTKCommon.checkNull(rs.getString("PROVIDER_CATEGORY"));
								strTemp[60]	=	TTKCommon.checkNull(rs.getString("SETTLEMENT_NUMBER"));
								strTemp[61]	=	TTKCommon.checkNull(rs.getString("CHEQUE_NUMBER"));
								strTemp[62]	=	TTKCommon.checkNull(rs.getString("CHEQUE_DATE"));
								strTemp[63]	=	TTKCommon.checkNull(rs.getString("OVERRIDE_YN"));
								strTemp[64]	=	TTKCommon.checkNull(rs.getString("SERVICE_NAME"));
								strTemp[65]	=	TTKCommon.checkNull(rs.getString("PROVIDER_COUNTRY"));
								strTemp[66]	=	TTKCommon.checkNull(rs.getString("CLM_REQUESTED_AMOUNT_QAR"));
								strTemp[67]	=	TTKCommon.checkNull(rs.getString("CLM_APPROVED_AMOUNT_QAR"));
								strTemp[68]	=	TTKCommon.checkNull(rs.getString("TOT_DISALLOWED_AMOUNT"));
								strTemp[69]	=	TTKCommon.checkNull(rs.getString("INTERNAL_REMARK_STATUS"));
								strTemp[70]	=	TTKCommon.checkNull(rs.getString("INTERNAL_REMARK_DESC"));
								strTemp[71]	=	TTKCommon.checkNull(rs.getString("CLINICIAN_ID"));
								strTemp[72]	=	TTKCommon.checkNull(rs.getString("CLINICIAN_NAME"));
								strTemp[73]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_TYPE"));
								strTemp[74]	=	TTKCommon.checkNull(rs.getString("SUM_INSURED"));
								strTemp[75]	=	TTKCommon.checkNull(rs.getString("AVA_SUM_INSURED"));
								strTemp[76]	=	TTKCommon.checkNull(rs.getString("UTILISED_SUM_INSURED"));
								strTemp[77]	=	TTKCommon.checkNull(rs.getString("AUDIT_STATUS"));
								strTemp[78]	=	TTKCommon.checkNull(rs.getString("AUDIT_FINDINGS"));
								strTemp[79]	=	TTKCommon.checkNull(rs.getString("AUDITED_DATE"));
								strTemp[80]	=	TTKCommon.checkNull(rs.getString("CLAIMS_REMARKS_TO_AUDIT"));
								strTemp[81]	=	TTKCommon.checkNull(rs.getString("TPA_DENIAL_CODE"));
								strTemp[82]	=	TTKCommon.checkNull(rs.getString("TPA_DENIAL_DESC"));
								strTemp[83]	=	TTKCommon.checkNull(rs.getString("PROVIDER_NETWORK_CATEGORY"));
								
								// Part 1 : 
								strTemp[84]	=	TTKCommon.checkNull(rs.getString("REMARKS_FOR_THE_PRO_MEMBER"));
								strTemp[85]	=	TTKCommon.checkNull(rs.getString("INTERNAL_REMARKS"));
								strTemp[86]	=	TTKCommon.checkNull(rs.getString("PROVIDER_SPECIFIC_REMARKS"));
								strTemp[87]	=	TTKCommon.checkNull(rs.getString("MEMBER_SPECIFIC_POLICY_REMARKS"));
								strTemp[88]	=	TTKCommon.checkNull(rs.getString("SUBMISSION_TYPE"));
								strTemp[89]	=	TTKCommon.checkNull(rs.getString("BATCH_NO"));
								strTemp[90]	=	TTKCommon.checkNull(rs.getString("MARITALSTATUS"));
								strTemp[91]	=	TTKCommon.checkNull(rs.getString("REQ_AMOUNT_ORIGINAL"));
								strTemp[92]	=	TTKCommon.checkNull(rs.getString("APR_AMOUNT_ORIGINAL"));
								strTemp[93]	=	TTKCommon.checkNull(rs.getString("ORIGINAL_CURRENCY"));
								strTemp[94]	=	TTKCommon.checkNull(rs.getString("CLAIM_COMPLETED_DATETIME"));
								strTemp[95]	=	TTKCommon.checkNull(rs.getString("SPECIALITY"));
								strTemp[96]	=	TTKCommon.checkNull(rs.getString("CONSULTANT_TYPE"));
								strTemp[97]	=	TTKCommon.checkNull(rs.getString("Count"));
								 
								// Part 2 : 
								strTemp[98]	=	TTKCommon.checkNull(rs.getString("no_of_activities"));		// No of Encounters of Activity
								strTemp[99]	=	TTKCommon.checkNull(rs.getString("no_of_approved"));		// No of times Approved	
								strTemp[100]	=	TTKCommon.checkNull(rs.getString("no_of_rejected"));	// No of times Rejected
								strTemp[101]	=	TTKCommon.checkNull(rs.getString("act_count_yn"));		// Same activity code in same claim no.	
								strTemp[102]	=	TTKCommon.checkNull(rs.getString("act_date_count_yn"));	// Same activity code  with same service date	
							//	strTemp[103]	=	TTKCommon.checkNull(rs.getString("same_icd_yn"));		// Consultation with same service date and same ICD.(principal ICD)
								strTemp[103]	=	TTKCommon.checkNull(rs.getString("act_status"));		// Activity status-Rejected/Approve...
								outputData.add(strTemp);
							}			 
				        	}
		        } 
		        }else{
		        try(ResultSet rs = (java.sql.ResultSet) cStmtObject.getObject(coutParam)){
				if(rs!=null){		
				String[] strTemp	=	null;
			    while(rs.next()){
				strTemp	=	new String[78];
				strTemp[0]	=	TTKCommon.checkNull(rs.getString("PRE_AUTH_NUMBER"));
				strTemp[1]	=	TTKCommon.checkNull(rs.getString("CLAIM_NUMBER"));
				strTemp[2]	=	TTKCommon.checkNull(rs.getString("MEMBER_INCEPTION_DATE"));
				strTemp[3]	=	TTKCommon.checkNull(rs.getString("POLICY_NUMBER"));
				strTemp[4]	=	TTKCommon.checkNull(rs.getString("POLICY_START_DATE"));
				strTemp[5]	=	TTKCommon.checkNull(rs.getString("POLICY_END_DATE"));
				strTemp[6]	=	TTKCommon.checkNull(rs.getString("CORPORATE_NAME"));
				strTemp[7]	=	TTKCommon.checkNull(rs.getString("SOURCE_TYPE"));
				strTemp[8]	=	TTKCommon.checkNull(rs.getString("PREAUTH_RECEIVED_DATE"));
				strTemp[9]	=	TTKCommon.checkNull(rs.getString("LAST_UPDATED_DATE"));
				strTemp[10]	=	TTKCommon.checkNull(rs.getString("PREAUTH_COMPLETED_DATE"));
				strTemp[11]	=	TTKCommon.checkNull(rs.getString("PROVIDER_NAME"));
				strTemp[12]	=	TTKCommon.checkNull(rs.getString("ENCOUNTER_TYPE"));
				strTemp[13]	=	TTKCommon.checkNull(rs.getString("BENEFIT_TYPE"));
				strTemp[14]	=	TTKCommon.checkNull(rs.getString("OVERRIDE_DATE"));
				strTemp[15]	=	TTKCommon.checkNull(rs.getString("OVERRIDE_REMARKS"));
				strTemp[16]	=	TTKCommon.checkNull(rs.getString("DENIAL_CODE"));
				strTemp[17]	=	TTKCommon.checkNull(rs.getString("DENIAL_DESCRIPTION"));
				strTemp[18]	=	TTKCommon.checkNull(rs.getString("TOOTH_NO"));
				strTemp[19]	=	TTKCommon.checkNull(rs.getString("QUANTITY"));
				strTemp[20]	=	TTKCommon.checkNull(rs.getString("PA_STATUS"));
				strTemp[21]	=	TTKCommon.checkNull(rs.getString("PRIMARY_ICD_CODE"));
				strTemp[22]	=	TTKCommon.checkNull(rs.getString("PRIMARY_ICD_DESC"));
				strTemp[23]	=	TTKCommon.checkNull(rs.getString("SECONDARY_ICD_CODE"));
				strTemp[24]	=	TTKCommon.checkNull(rs.getString("SECONDARY_ICD_DESC"));
				strTemp[25]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_TYPE"));
				strTemp[26]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_CODE"));
				strTemp[27]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_DESCRIPTION"));
				strTemp[28]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_INTERNAL_DESCRIPTION"));
				strTemp[29]	=	TTKCommon.checkNull(rs.getString("REQUESTED_AMOUNT_ACTIVITY"));
				strTemp[30]	=	TTKCommon.checkNull(rs.getString("GROSS_AMOUNT"));
				strTemp[31]	=	TTKCommon.checkNull(rs.getString("DISCOUNT_AMOUNT"));
				strTemp[32]	=	TTKCommon.checkNull(rs.getString("DISC_GROSS_AMOUNT"));
				strTemp[33]	=	TTKCommon.checkNull(rs.getString("PATIENT_SHARE_AMOUNT"));
				strTemp[34]	=	TTKCommon.checkNull(rs.getString("NET_AMOUNT"));
				strTemp[35]	=	TTKCommon.checkNull(rs.getString("DISALLOWED_AMOUNT"));
				strTemp[36]	=	TTKCommon.checkNull(rs.getString("ALLOWED_AMOUNT"));
				strTemp[37]	=	TTKCommon.checkNull(rs.getString("APPROVED_AMOUNT"));
				strTemp[38]	=	TTKCommon.checkNull(rs.getString("PA_REQ_AMOUNT_QAR"));
				strTemp[39]	=	TTKCommon.checkNull(rs.getString("PA_APPROVED_AMOUNT_QAR"));
				strTemp[40]	=	TTKCommon.checkNull(rs.getString("TOT_DISALLOWED_AMOUNT"));
				strTemp[41]	=	TTKCommon.checkNull(rs.getString("CLM_REQUESTED_AMOUNT_QAR"));
				strTemp[42]	=	TTKCommon.checkNull(rs.getString("CLM_APPROVED_AMOUNT_QAR"));
				strTemp[43]	=	TTKCommon.checkNull(rs.getString("ASSIGNED_TO"));
				strTemp[44]	=	TTKCommon.checkNull(rs.getString("LAST_UPDATED_BY"));
				strTemp[45]	=	TTKCommon.checkNull(rs.getString("PROCESSED_BY"));
				strTemp[46]	=	TTKCommon.checkNull(rs.getString("ENROLMENT_ID"));
				strTemp[47]	=	TTKCommon.checkNull(rs.getString("MEMBER_NAME"));
				strTemp[48]	=	TTKCommon.checkNull(rs.getString("RELSHIP_DESCRIPTION"));
				strTemp[49]	=	TTKCommon.checkNull(rs.getString("MEMBER_AGE"));
				strTemp[50]	=	TTKCommon.checkNull(rs.getString("GENDER"));
				strTemp[51]	=	TTKCommon.checkNull(rs.getString("PROVIDER_CATEGORY"));
				strTemp[52]	=	TTKCommon.checkNull(rs.getString("OVERRIDE_YN"));
				strTemp[53]	=	TTKCommon.checkNull(rs.getString("SERVICE_NAME"));
				strTemp[54]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_QUANTITY_REQ"));
				strTemp[55]	=	TTKCommon.checkNull(rs.getString("ACTIVITY_QUANTITY_APPROVED"));
				strTemp[56]	=	TTKCommon.checkNull(rs.getString("MEDICAL_OPINION_REMARKS"));
				strTemp[57]	=	TTKCommon.checkNull(rs.getString("CLINICIAN_ID"));
				strTemp[58]	=	TTKCommon.checkNull(rs.getString("CLINICIAN_NAME"));
				strTemp[59]    =    TTKCommon.checkNull(rs.getString("TPA_DENIAL_CODE"));
                strTemp[60]    =    TTKCommon.checkNull(rs.getString("TPA_DENIAL_DESC"));
                strTemp[61]    =    TTKCommon.checkNull(rs.getString("PROVIDER_NETWORK_CATEGORY"));
                strTemp[62]    =    TTKCommon.checkNull(rs.getString("SUM_INSURED"));
                strTemp[63]    =    TTKCommon.checkNull(rs.getString("AVA_SUM_INSURED"));
                strTemp[64]    =    TTKCommon.checkNull(rs.getString("UTILISED_SUM_INSURED"));
                
             // Part 1:
                strTemp[65]    =    TTKCommon.checkNull(rs.getString("REQ_AMOUNT_ORIGINAL"));
                strTemp[66]    =    TTKCommon.checkNull(rs.getString("APR_AMOUNT_ORIGINAL"));
                strTemp[67]    =    TTKCommon.checkNull(rs.getString("ORIGINAL_CURRENCY"));
                strTemp[68]    =    TTKCommon.checkNull(rs.getString("PA_COMPLETED_DATETIME"));
                strTemp[69]    =    TTKCommon.checkNull(rs.getString("Count"));
                strTemp[70]    =    TTKCommon.checkNull(rs.getString("SPECIALITY"));
                strTemp[71]    =    TTKCommon.checkNull(rs.getString("Consultant_Type"));
                
                // Part 2 : 
                strTemp[72]	=	TTKCommon.checkNull(rs.getString("no_of_activities"));		// No of Encounters of Activity
				strTemp[73]	=	TTKCommon.checkNull(rs.getString("no_of_approved"));		// No of times Approved	
				strTemp[74]	=	TTKCommon.checkNull(rs.getString("no_of_rejected"));		// No of times Rejected
				strTemp[75]	=	TTKCommon.checkNull(rs.getString("act_count_yn"));			// Same activity code in same claim no.	
				strTemp[76]	=	TTKCommon.checkNull(rs.getString("act_date_count_yn"));		// Same activity code  with same service date	
			//	strTemp[77]	=	TTKCommon.checkNull(rs.getString("same_icd_yn"));			// Consultation with same service date and same ICD.(principal ICD)
				strTemp[77]	=	TTKCommon.checkNull(rs.getString("act_status"));			// Activity status-Rejected/Approve
                outputData.add(strTemp);
			}
				        }
		        	}
		        }
		        
			}catch (SQLException sqlExp)
		    {
				outputData.clear();
		        outputData.add(new String[]{"sql.error"});
		        throw new TTKException(sqlExp, "tTkReports");
		        
		    }//end of catch (SQLException sqlExp)
		    catch (Exception exp)
		    {
		    	outputData.clear();
		        outputData.add(new String[]{"sql.error"});
		        throw new TTKException(exp, "tTkReports");
		    }//end of catch (Exception exp)
		return outputData;
	}

public ArrayList uploadClaimAuditReport(ArrayList inputData) throws TTKException{
	ArrayList outputData=new ArrayList();
	String successYN=null;
	try(Connection connection=ResourceManager.getConnection();
		CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(strUploadClaimAuditReport);){
		XMLType poXML = null;
		if(inputData.get(0) != null)
		{
			poXML = XMLType.createXML (((OracleConnection)((WrappedConnectionJDK6)connection).getUnderlyingConnection()), (String)inputData.get(0));
		}
		cStmtObject.setString(1, null);
		//System.out.println("poXML::"+poXML.getStringVal());
		cStmtObject.setObject(2, poXML);
		cStmtObject.setString(3, (String)inputData.get(1));
		cStmtObject.setString(4, String.valueOf(inputData.get(2)));
		cStmtObject.registerOutParameter(5, OracleTypes.VARCHAR);
		cStmtObject.execute();
		successYN =(String)cStmtObject.getObject(5);
		outputData.add(successYN);
	}catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	return outputData;
}

public ArrayList getAuditUploadTemplateErrorLog(ArrayList inputData) throws TTKException{
	ArrayList alRresultList	=	new ArrayList<>();
	try(Connection connection = ResourceManager.getConnection();
		CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(strUploadClaimAuditReportLog);){
		if(inputData.get(0)!=null&&!"".equals(inputData.get(0)))
	    	cStmtObject.setString(1, (String)inputData.get(0));
	        else
	        	cStmtObject.setString(1, null);
	        if(inputData.get(1)!=null&&!"".equals(inputData.get(1)))
	        	cStmtObject.setString(2, (String)inputData.get(1));
	            else
	            	cStmtObject.setString(2, null);
	    	cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.execute();
	        try(ResultSet rs = (java.sql.ResultSet)cStmtObject.getObject(3);){
	            if(rs != null){
	            	//GEETING META DATA FOR COLUMN HEADS
	            	 ArrayList<String> alResult	=	null;
	                 ResultSetMetaData metaData	=	rs.getMetaData();
	                 int colmCount				=	metaData.getColumnCount();
	                 if(colmCount>0)
	                 	alResult = new ArrayList<String>();
	                 for(int k=0;k<colmCount;k++)
	                 {
	                 	alResult.add(metaData.getColumnName(k+1));
	                 }
	                 alRresultList.add(alResult);
	            	ArrayList<String[]> newArray=new ArrayList<>();
	               //THIS BLOCK IS DATA FROM QUERY
	            	while (rs.next()) {
	            		alResult = new ArrayList<String>();
	                	String[] temp=new String[colmCount];                	
	                	for(int l=1;l<=colmCount;l++){
	                		temp[l-1]=rs.getString(l)==null?"":rs.getString(l);
	                	}
	                	newArray.add(temp);
	                }//end of while(rs.next())
	            	alRresultList.add(newArray);
	            }//end of if(rs != null)
	        }
        
    }
    catch (SQLException sqlExp)
    {
    	alRresultList.clear();
    	alRresultList.add(new String[]{"sql.error"});
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
    	alRresultList.clear();
    	alRresultList.add(new String[]{"sql.error"});
        throw new TTKException(exp, "tTkReports");
    }//end of catch (Exception exp)
	return alRresultList;
}


// Policies Member List Report
public ResultSet getPolicyReport(String strReportID,String strParameter,Long  policySeqId,String memberStatus) throws TTKException {
	Connection conn = null;
	CallableStatement cStmtObject=null;
    ResultSet rs = null;
    String strProcedureName="";
    strProcedureName =getProcedureName(strReportID);
    if(strProcedureName==null)
    {
    	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
    }//end of if(strProcedureName==null)
    OracleCachedRowSet crs = null;
    try{
        String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?)}";
       
     //   log.info("strCall		:	"+strCall);
    //    log.info("strParameter	:	"+strParameter);
    //    log.info("strReportID	:	"+strReportID);
        crs = new OracleCachedRowSet();
   		conn = ResourceManager.getConnection();	//getTestConnection();
    
        cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
        cStmtObject.setString(1,strParameter);
        cStmtObject.setString(2,memberStatus);
        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        cStmtObject.execute();
        rs = (java.sql.ResultSet) cStmtObject.getObject(3); 
        
        if(rs !=null)
        {       
        	crs.populate(rs);
        	 
        }//end of if(rs !=null)
        return crs;
    }//end of try
    catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }//end of catch (Exception exp)
    finally
    {
    	/* Nested Try Catch to ensure resource closure */ 
    	try // First try closing the Statement
    	{
    		try
    		{
    			if (rs != null) rs.close();
    		}//end of try
    		catch (SQLException sqlExp)
    		{
    			log.error("Error while closing the Resultset in TTKReportDAOImpl getReport()",sqlExp);
    			throw new TTKException(sqlExp, "tTkReports");
    		}//end of catch (SQLException sqlExp)
    		finally // Even if result set is not closed, control reaches here. Try closing the statement now.
    		{
    			try
    			{
    				if (cStmtObject != null) cStmtObject.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Statement in TTKReportDAOImpl getReport()",sqlExp);
    				throw new TTKException(sqlExp, "tTkReports");
    			}//end of catch (SQLException sqlExp)
    			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    			{
    				try
    				{
    					if(conn != null) conn.close();
    				}//end of try
    				catch (SQLException sqlExp)
    				{
    					log.error("Error while closing the Connection in TTKReportDAOImpl getReport()",sqlExp);
    					throw new TTKException(sqlExp, "tTkReports");
    				}//end of catch (SQLException sqlExp)
    			}//end of finally Connection Close
    		}//end of try
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "tTkReports");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		rs = null;
    		cStmtObject = null;
    		conn = null;        		
    	}//end of finally
    }//end of finally
} // END OF getReport()

public ResultSet[] getTATReport(String str, ArrayList inputData,String flag) throws TTKException {
	try{
		ResultSet[] resultSetList=new ResultSet[9];
		
		try(Connection connection = ResourceManager.getConnection();
				CallableStatement cStmtObject=(java.sql.CallableStatement) connection.prepareCall(strTATReports);){
					cStmtObject.setString(1, String.valueOf(inputData.get(0)));
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
					cStmtObject.execute();
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(2); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
						crs.populate(resultSet);
						resultSetList[0]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(3); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[1]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(4); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[2]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(5); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[3]=crs;
					}
				} 
		return resultSetList;
	}
	catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	
}

public ArrayList uploadCFDReport(ArrayList inputData) throws TTKException{
	System.out.println("================uploadCFDReport==============");
	ArrayList outputData=new ArrayList();
	String successYN=null;
	try(Connection connection=ResourceManager.getConnection();
		CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(strUploadCFDReport);){
		XMLType poXML = null;
		if(inputData.get(0) != null)
		{
			poXML = XMLType.createXML (((OracleConnection)((WrappedConnectionJDK6)connection).getUnderlyingConnection()), (String)inputData.get(0));
		}
		cStmtObject.setString(1, null);
		System.out.println("poXML::"+poXML.getStringVal());
		cStmtObject.setObject(2, poXML);
		cStmtObject.setString(3, (String)inputData.get(1));
		cStmtObject.setString(4, String.valueOf(inputData.get(2)));
		cStmtObject.registerOutParameter(5, OracleTypes.VARCHAR);
		cStmtObject.execute();
		successYN =(String)cStmtObject.getObject(5);
		System.out.println("successYN:::::::::::"+successYN);
		outputData.add(successYN);
	}catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	return outputData;
}

public ResultSet[] getProductivityReport(String str, ArrayList inputData,String flag) throws TTKException {
	try{
		ResultSet[] resultSetList=new ResultSet[9];
		
		try(Connection connection = ResourceManager.getConnection();
				CallableStatement cStmtObject=(java.sql.CallableStatement) connection.prepareCall(strProductivityReports);){
					cStmtObject.setString(1, String.valueOf(inputData.get(0)));
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
					cStmtObject.execute();
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(2); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
						crs.populate(resultSet);
						resultSetList[0]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(3); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[1]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(4); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[2]=crs;
					}
				} 
		return resultSetList;
	}
	catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	
}
public ArrayList<String[]> getFinancePreauthReport(String repType,List inputList) throws TTKException {
	
	ArrayList<String[]> financeDataList=new ArrayList<>();
	String strProcedureName=getProcedureName(repType);
	String strCall = "{CALL "+strProcedureName.trim()+"(?,?,?,?,?,?,?)}";
	
	log.info("strProcedureName::"+strCall);
	
	try(Connection connection = ResourceManager.getMISConnection();
		CallableStatement callableStatement=connection.prepareCall(strCall)){
		callableStatement.setString(1,String.valueOf(inputList.get(0)));
		if(inputList.get(1).equals("")){
			callableStatement.setString(2,null);
		}else{
			callableStatement.setLong(2,Long.valueOf(String.valueOf(inputList.get(1))));
		}
		
		callableStatement.setString(3,String.valueOf(inputList.get(2)));
		callableStatement.setString(4,String.valueOf(inputList.get(3)));
		callableStatement.setString(5,String.valueOf(inputList.get(4)));
		callableStatement.setString(6,String.valueOf(inputList.get(5)));
		callableStatement.registerOutParameter(7,OracleTypes.CURSOR);
		callableStatement.execute();
         try(ResultSet resultSet = (java.sql.ResultSet) callableStatement.getObject(7)){
        	 log.info("Execution has done......");      	
            String[] strTemp	=	null;
            
            while(resultSet.next()){
			strTemp	=	new String[25];
			strTemp[0]	=	TTKCommon.checkNull(resultSet.getString("PREAUTH_NUMBER"));
			strTemp[1]	=	TTKCommon.checkNull(resultSet.getString("CLAIM_NO"));
			strTemp[2]	=	TTKCommon.checkNull(resultSet.getString("CORPORATE_NAME"));
			strTemp[3]	=	TTKCommon.checkNull(resultSet.getString("STATUS"));
			strTemp[4]	=	TTKCommon.checkNull(resultSet.getString("REQUESTED_AMOUNT_QAR"));
			strTemp[5]	=	TTKCommon.checkNull(resultSet.getString("APPROVED_AMOUNT_QAR"));
			strTemp[6]	=	TTKCommon.checkNull(resultSet.getString("PROVIDER_NAME")); 
			strTemp[7]	=	TTKCommon.checkNull(resultSet.getString("ENROLMENT_ID"));
			strTemp[8]	=	TTKCommon.checkNull(resultSet.getString("MEMBER_NAME"));
			strTemp[9]	=	TTKCommon.checkNull(resultSet.getString("PROCESSED_BY"));
			strTemp[10] =   TTKCommon.checkNull(resultSet.getString("POLICY_NUMBER"));
			strTemp[11] =  TTKCommon.checkNull(resultSet.getString("POLICY_START_DATE"));
			strTemp[12] =  TTKCommon.checkNull(resultSet.getString("POLICY_END_DATE"));
			strTemp[13] = TTKCommon.checkNull(resultSet.getString("BENEFIT_TYPE"));
			strTemp[14] = TTKCommon.checkNull(resultSet.getString("PREAUTH_RECEIVED_DATE"));
			strTemp[15] = TTKCommon.checkNull(resultSet.getString("APPEAL_DATE"));
			strTemp[16] = TTKCommon.checkNull(resultSet.getString("PREAUTH_ADDED_DATE"));
			strTemp[17] = TTKCommon.checkNull(resultSet.getString("LAST_UPDATED_DATE"));
			strTemp[18] = TTKCommon.checkNull(resultSet.getString("FIRST_MODIFIED_DATE"));
			strTemp[19] = TTKCommon.checkNull(resultSet.getString("PREAUTH_COMPLETED_DATE"));
			strTemp[20] = TTKCommon.checkNull(resultSet.getString("PREAUTH_OVERRIDE_DATE"));
			strTemp[21] = TTKCommon.checkNull(resultSet.getString("ADMISSION_DATE"));
			strTemp[22] = TTKCommon.checkNull(resultSet.getString("DISCHARGE_DATE"));
			strTemp[23] = TTKCommon.checkNull(resultSet.getString("AGEING_AS_PER_RECEIVED_DATE"));
			strTemp[24] = TTKCommon.checkNull(resultSet.getString("AGEING_AS_PER_COMPLETED_DATE"));
			
			
			financeDataList.add(strTemp);
     }//end of if(rs !=null)
         
        }
	}catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	return financeDataList;

}




public ArrayList getCFDUploadTemplateErrorLog(ArrayList inputData) throws TTKException{
	System.out.println("===Inside the getCFDUploadTemplateErrorLog method of TTKReportDAOImpl===");
	ArrayList alRresultList	=	new ArrayList<>();
	try(Connection connection = ResourceManager.getConnection();
		CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(strUploadCFDReportLog);){
		if(inputData.get(0)!=null&&!"".equals(inputData.get(0)))
	    	cStmtObject.setString(1, (String)inputData.get(0));
	        else
	        	cStmtObject.setString(1, null);
	        if(inputData.get(1)!=null&&!"".equals(inputData.get(1)))
	        	cStmtObject.setString(2, (String)inputData.get(1));
	            else
	            	cStmtObject.setString(2, null);
	    	cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.execute();
	        try(ResultSet rs = (java.sql.ResultSet)cStmtObject.getObject(3);){
	            if(rs != null){
	            	//GEETING META DATA FOR COLUMN HEADS
	            	 ArrayList<String> alResult	=	null;
	                 ResultSetMetaData metaData	=	rs.getMetaData();
	                 int colmCount				=	metaData.getColumnCount();
	                 if(colmCount>0)
	                 	alResult = new ArrayList<String>();
	                 for(int k=0;k<colmCount;k++)
	                 {
	                 	alResult.add(metaData.getColumnName(k+1));
	                 }
	                 alRresultList.add(alResult);
	            	ArrayList<String[]> newArray=new ArrayList<>();
	               //THIS BLOCK IS DATA FROM QUERY
	            	while (rs.next()) {
	            		alResult = new ArrayList<String>();
	                	String[] temp=new String[colmCount];                	
	                	for(int l=1;l<=colmCount;l++){
	                		temp[l-1]=rs.getString(l)==null?"":rs.getString(l);
	                	}
	                	newArray.add(temp);
	                }//end of while(rs.next())
	            	alRresultList.add(newArray);
	            }//end of if(rs != null)
	        }
        
    }
    catch (SQLException sqlExp)
    {
    	alRresultList.clear();
    	alRresultList.add(new String[]{"sql.error"});
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
    	alRresultList.clear();
    	alRresultList.add(new String[]{"sql.error"});
        throw new TTKException(exp, "tTkReports");
    }//end of catch (Exception exp)
	return alRresultList;
}



public ResultSet[] getCFDReport(ArrayList searchData, String reportType) throws TTKException {	
   //	ArrayList<String[]> outputData=new ArrayList<>();
	ResultSet resultSetList[] = new ResultSet[2];
	String procedure=null;
	procedure=strCFDReportGenerate;
	for(int i=0;i<searchData.size();i++){
	//	System.out.println("value of   "+i+"     "+searchData.get(i));
	}
		try(Connection connection=ResourceManager.getConnection();
				OracleCachedRowSet cachedRowSet=new OracleCachedRowSet();	
				CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(procedure);){
				cStmtObject.setString(1, (searchData.get(0)==null)?"":(String) searchData.get(0));
				cStmtObject.setString(2, (searchData.get(1)==null)?"":(String) searchData.get(1));
				cStmtObject.setString(3, (searchData.get(2)==null)?"":(String) searchData.get(2));
				cStmtObject.setString(4, (searchData.get(3)==null)?"":(String) searchData.get(3));
				
				cStmtObject.setString(5, (searchData.get(4)==null)?"":(String) searchData.get(4));
				//cStmtObject.setLong(5, 0);
				
				cStmtObject.setString(6, (searchData.get(5)==null)?"":(String) searchData.get(5));
				cStmtObject.setString(7, (searchData.get(6)==null)?"":(String) searchData.get(6));
				cStmtObject.setString(8, (searchData.get(7)==null)?"":(String) searchData.get(7));
				cStmtObject.setString(9, (searchData.get(8)==null)?"":(String) searchData.get(8));
				
				//cStmtObject.setLong(10, 0);
				cStmtObject.setString(10, (searchData.get(9)==null)?"":(String) searchData.get(9));
				
				cStmtObject.setString(11, (searchData.get(10)==null)?"":(String) searchData.get(10));
				cStmtObject.setString(12, (searchData.get(11)==null)?"":(String) searchData.get(11));
				cStmtObject.setString(13, (searchData.get(12)==null)?"":(String) searchData.get(12));
				cStmtObject.setString(14, (searchData.get(13)==null)?"":(String) searchData.get(13));
				cStmtObject.setString(15, (searchData.get(14)==null)?"":(String) searchData.get(14));
				cStmtObject.setString(16, (searchData.get(15)==null)?"":(String) searchData.get(15));
				cStmtObject.setString(17, (searchData.get(16)==null)?"":(String) searchData.get(16));
				cStmtObject.setString(18, (searchData.get(17)==null)?"":(String) searchData.get(17));
				cStmtObject.setString(19, (searchData.get(18)==null)?"":(String) searchData.get(18));
				cStmtObject.setString(20, (searchData.get(19)==null)?"":(String) searchData.get(19));
				cStmtObject.setString(21, (searchData.get(20)==null)?"":(String) searchData.get(20));
				cStmtObject.registerOutParameter(22,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(23,OracleTypes.CURSOR);
		        cStmtObject.execute();
		        	try(ResultSet rs = (java.sql.ResultSet) cStmtObject.getObject(22)){
				        /*if(rs!=null){	
			         	String[] strTemp	=	null;
							while(rs.next()){
								strTemp	=	new String[30];
								strTemp[0]	=	TTKCommon.checkNull(rs.getString("COUNTRY_NAME"));
								strTemp[1]	=	TTKCommon.checkNull(rs.getString("ALERTS"));
								strTemp[2]	=	TTKCommon.checkNull(rs.getString("CLAIM_NUMBER"));
								strTemp[3]	=	TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID"));
								strTemp[4]	=	TTKCommon.checkNull(rs.getString("MEM_NAME"));
								strTemp[5]	=	TTKCommon.checkNull(rs.getString("GROUP_NAME"));
								strTemp[6]	=	TTKCommon.checkNull(rs.getString("EMPANEL_NUMBER"));
								strTemp[7]	=	TTKCommon.checkNull(rs.getString("HOSP_NAME"));
								strTemp[8]	=	TTKCommon.checkNull(rs.getString("DOCTOR_NAME"));
								strTemp[9]	=	TTKCommon.checkNull(rs.getString("SOURCE_OF_REFERAL"));
								strTemp[10]	=	TTKCommon.checkNull(rs.getString("BENIFIT_TYPE"));
								strTemp[11]	=	TTKCommon.checkNull(rs.getString("DETECTION_DATE"));
								System.out.println("DETECTION_DATE::::::::::::"+rs.getString("DETECTION_DATE"));
								strTemp[12]	=	TTKCommon.checkNull(rs.getString("DETECTION_MONTH"));
								strTemp[13]	=	TTKCommon.checkNull(rs.getString("DOC_UPLD_DATE"));
								System.out.println("DOC_UPLD_DATE::::::::::::"+rs.getString("DOC_UPLD_DATE"));
								strTemp[14]	=	TTKCommon.checkNull(rs.getString("REFFER_BY"));
								strTemp[15]	=	TTKCommon.checkNull(rs.getString("INV_STATUS"));
								strTemp[16]	=	TTKCommon.checkNull(rs.getString("INV_OUT_CATEGORY"));
								strTemp[17]	=	TTKCommon.checkNull(rs.getString("FRAUD_DETECTED_DATE"));
								strTemp[18]	=	TTKCommon.checkNull(rs.getString("INV_REMARKS"));
								strTemp[19]	=	TTKCommon.checkNull(rs.getString("EXGRATIA"));
								strTemp[20]	=	TTKCommon.checkNull(rs.getString("CURRENCY"));
								strTemp[21]	=	TTKCommon.checkNull(rs.getString("GROSS_AMOUNT"));
								strTemp[22]	=	TTKCommon.checkNull(rs.getString("PAID_AMOUNT"));
								strTemp[23]	=	TTKCommon.checkNull(rs.getString("AMT_UTIL_FOR_INV"));
								strTemp[24]	=	TTKCommon.checkNull(rs.getString("GROSS_FRAUD_SAV"));
								strTemp[25]	=	TTKCommon.checkNull(rs.getString("NET_FRAUD_SAV"));
								strTemp[26]	=	TTKCommon.checkNull(rs.getString("SAVING_MONTH"));
								strTemp[27]	=	TTKCommon.checkNull(rs.getString("AUDIT_SAMPLE"));
								strTemp[28]	=	TTKCommon.checkNull(rs.getString("TAT"));
								strTemp[29]	=	TTKCommon.checkNull(rs.getString("INVESTIGATOR_NAME"));
								outputData.add(strTemp);
							}			 
		        		} */
		        		
		        		OracleCachedRowSet crs = new OracleCachedRowSet();
						if(rs !=null)
						crs.populate(rs);
						resultSetList[0]=crs;
		        }
		        
		        	try(ResultSet rs = (java.sql.ResultSet) cStmtObject.getObject(23)){
		        		OracleCachedRowSet crs = new OracleCachedRowSet();
						if(rs !=null)
						crs.populate(rs);
						resultSetList[1]=crs;
		        	}
			}catch (SQLException sqlExp)
		    {
			//	outputData.clear();
		    //    outputData.add(new String[]{"sql.error"});
		        throw new TTKException(sqlExp, "tTkReports");
		        
		    }//end of catch (SQLException sqlExp)
		    catch (Exception exp)
		    {
		    //	outputData.clear();
		      //  outputData.add(new String[]{"sql.error"});
		        throw new TTKException(exp, "tTkReports");
		    }//end of catch (Exception exp)
		return resultSetList;
	}

public ArrayList<Object> getPolicyNoList(String groupId) throws TTKException
	{
		Connection conn = null;
		PreparedStatement pStmt 		= 	null;
		ResultSet rs1=null;
		CacheObject cacheObject = null;
	    ArrayList<CacheObject> policyList = new ArrayList<CacheObject>();
	    ArrayList<Object> allList = new ArrayList<Object>();
		try {
			 conn = ResourceManager.getConnection();
			 pStmt = conn.prepareStatement(str_PolicyNoList);
			 pStmt.setString(1,groupId);
			 rs1 = pStmt.executeQuery();
				
			if(rs1!=null){
				while(rs1.next()){
					cacheObject = new CacheObject();
					cacheObject.setCacheId((rs1.getString("POLICY_SEQ_ID")));
	                cacheObject.setCacheDesc(TTKCommon.checkNull(rs1.getString("POLICY_NUMBER")));
	                policyList.add(cacheObject);
				}
			}
			allList.add(policyList);
			return allList;
		} 
		 catch (SQLException sqlExp)
	     {
	         throw new TTKException(sqlExp, "tTkReports");
	     }
		
		catch (Exception exp)
		{
			throw new TTKException(exp, "tTkReports");
		}//end of catch (Exception exp)
		
		 finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs1 != null) rs1.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in getPolicyNoList TTKReportDAOImpl()",sqlExp);
						throw new TTKException(sqlExp, "tTkReports");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (pStmt != null) pStmt.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in getPolicyNoList TTKReportDAOImpl()",sqlExp);
							throw new TTKException(sqlExp, "tTkReports");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in getPolicyNoList TTKReportDAOImpl()",sqlExp);
								throw new TTKException(sqlExp, "tTkReports");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "tTkReports");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs1 = null;
					pStmt = null;
					conn = null;
				}//end of finally
			}//end of finally
	} // en of getPolicyNoList(String groupId) throws TTKException

   public ResultSet[] getBordereauReport(String str, ArrayList inputData) throws TTKException {
	try{
		ResultSet[] resultSetList=new ResultSet[9];
		
		try(Connection connection = ResourceManager.getMISConnection();
				CallableStatement cStmtObject=(java.sql.CallableStatement) connection.prepareCall(strBordereauReports);){
					cStmtObject.setString(1, String.valueOf(inputData.get(0)));
					cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
					cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
					cStmtObject.execute();
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(2); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
						crs.populate(resultSet);
						resultSetList[0]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(3); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[1]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(4); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[2]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(5); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[3]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(6); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[4]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(7); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[5]=crs;
					}
					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(8); ){
						OracleCachedRowSet crs = new OracleCachedRowSet();
						if(resultSet !=null)
							crs.populate(resultSet);
						resultSetList[6]=crs;
					}
//					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(9); ){
//						OracleCachedRowSet crs = new OracleCachedRowSet();
//						if(resultSet !=null)
//							crs.populate(resultSet);
//						resultSetList[7]=crs;
//					}
//					try(ResultSet resultSet= (java.sql.ResultSet) cStmtObject.getObject(10); ){
//						OracleCachedRowSet crs = new OracleCachedRowSet();
//						if(resultSet !=null)
//							crs.populate(resultSet);
//						resultSetList[8]=crs;
//					}
				} 
		return resultSetList;
	}
	catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "tTkReports");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "tTkReports");
    }
	
}

   public ResultSet[] getCampaginReport(ArrayList searchData, String reportType) throws TTKException {
	   //	ArrayList<String[]> outputData=new ArrayList<>();
		ResultSet resultSetList[] = new ResultSet[1];
		String procedure=null;
		procedure=strCamReportGenerate;
		
			try(Connection connection=ResourceManager.getConnection();
					OracleCachedRowSet cachedRowSet=new OracleCachedRowSet();	
					CallableStatement cStmtObject = (java.sql.CallableStatement)connection.prepareCall(procedure);){
					cStmtObject.setString(1, (searchData.get(0)==null)?"":(String) searchData.get(0));	// campName
					cStmtObject.setString(2, (searchData.get(1)==null)?"":(String) searchData.get(1));	// sProviderName
					cStmtObject.setString(3, (searchData.get(2)==null)?"":(String) searchData.get(2));	// campStatus
					cStmtObject.setString(4, (searchData.get(3)==null)?"":(String) searchData.get(3));	// campReceivedFromDate
					
					cStmtObject.setString(5, (searchData.get(4)==null)?"":(String) searchData.get(4));	// campReceivedToDate
					//cStmtObject.setLong(5, 0);
					
					cStmtObject.setString(6, (searchData.get(5)==null)?"":(String) searchData.get(5));	// campStartDate
					cStmtObject.setString(7, (searchData.get(6)==null)?"":(String) searchData.get(6));	// campEndDate
					cStmtObject.setString(8, (searchData.get(7)==null)?null:(String) searchData.get(7));	//  v_sort_var
					cStmtObject.setString(9, (searchData.get(8)==null)?null:(String) searchData.get(8));	//  v_sort_order
					cStmtObject.setString(10, (searchData.get(9)==null)?null:(String) searchData.get(9));	// v_start_num
					cStmtObject.setString(11, (searchData.get(10)==null)?null:(String) searchData.get(10));	//  v_end_num
					cStmtObject.registerOutParameter(12,OracleTypes.CURSOR);
					
			        cStmtObject.execute();
			        	try(ResultSet rs = (java.sql.ResultSet) cStmtObject.getObject(12)){
			        		OracleCachedRowSet crs = new OracleCachedRowSet();
							if(rs !=null)
							crs.populate(rs);
							resultSetList[0]=crs;
			        }
 	
				}catch (SQLException sqlExp)
			    {
				//	outputData.clear();
			    //    outputData.add(new String[]{"sql.error"});
			        throw new TTKException(sqlExp, "tTkReports");
			        
			    }//end of catch (SQLException sqlExp)
			    catch (Exception exp)
			    {
			    //	outputData.clear();
			      //  outputData.add(new String[]{"sql.error"});
			        throw new TTKException(exp, "tTkReports");
			    }//end of catch (Exception exp)
			return resultSetList;
		}
public String getPaymentTermAgr(String hospitalSeqId) throws TTKException {
	
	Connection conn = null;
	PreparedStatement pStmt = 	null;
	ResultSet rs = null;
	String paymentTermAgree="";
    try {
        conn = ResourceManager.getConnection();
        pStmt=conn.prepareStatement(strGetPaymentTermAgr);
        if(hospitalSeqId == null || "".equals(hospitalSeqId))
        	 pStmt.setString(1,null);
        else
        	 pStmt.setLong(1,new Long(hospitalSeqId));	
        pStmt.setString(1,hospitalSeqId);
        rs = pStmt.executeQuery();
		if(rs != null){
			while(rs.next())
			{
				paymentTermAgree = TTKCommon.checkNull(rs.getString("PAYMENT_DUR_AGR_DAYS"));
			}//end of while(rs.next())
		}//end of if(rs != null) =
		return paymentTermAgree;
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "tTkReports");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "tTkReports");
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
				log.error("Error while closing the Resultset in TTKReportDAOImpl getPaymentTermAgr()",sqlExp);
				throw new TTKException(sqlExp, "tTkReports");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{

				try
				{
					if (pStmt != null) pStmt.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in TTKReportDAOImpl getPaymentTermAgr()",sqlExp);
					throw new TTKException(sqlExp, "tTkReports");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in TTKReportDAOImpl getPaymentTermAgr()",sqlExp);
						throw new TTKException(sqlExp, "tTkReports");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "tTkReports");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			rs = null;
			pStmt = null;
			conn = null;
		}//end of finally
	}//end of finally
}//end of getPaymentTermAgr(String hospitalSeqId)
	
/* 007 */
public ArrayList getActivityLogList(String hospitalSeqId) throws TTKException {
	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	HospitalDetailVO hospitalDetailVO = null;
	Collection<Object> alResultList = new ArrayList<Object>();
	try{
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strActivityLogList);
		 
		if(hospitalSeqId == null || "".equals(hospitalSeqId))
			 cStmtObject.setString(1,null);
        else
        	cStmtObject.setLong(1,new Long(hospitalSeqId));	
		 cStmtObject.setString(1,hospitalSeqId);
		
		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
		cStmtObject.execute();
		rs = (java.sql.ResultSet)cStmtObject.getObject(2);
		if(rs != null){
		
			while(rs.next()){
				hospitalDetailVO = new HospitalDetailVO();					
				 
				if(rs.getString("HOSP_SEQ_ID") != null){
                	hospitalDetailVO.setHospSeqId(new Long(rs.getLong("HOSP_SEQ_ID")));
                 }
				
				hospitalDetailVO.setPayTermAgrDays(TTKCommon.checkNull(rs.getString("PAYMENT_DUR_AGR_DAYS")));	
				hospitalDetailVO.setsDateTime(TTKCommon.checkNull(rs.getString("START_DATE")));
				hospitalDetailVO.seteDateTime(TTKCommon.checkNull(rs.getString("END_DATE")));
				hospitalDetailVO.setStrUpdatedBy(TTKCommon.checkNull(rs.getString("MODIFIED_BY")));
				hospitalDetailVO.setStrStatus(TTKCommon.checkNull(rs.getString("STATUS")));	
				alResultList.add(hospitalDetailVO);
			}//end of while(rs.next())
		}//end of if(rs != null)
		return (ArrayList)alResultList;
	}//end of try
	
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "tTkReports");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "tTkReports");
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
				log.error("Error while closing the Resultset in TTKReportDAOImpl getActivityLogList()",sqlExp);
				throw new TTKException(sqlExp, "tTkReports");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{

				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in TTKReportDAOImpl getActivityLogList()",sqlExp);
					throw new TTKException(sqlExp, "tTkReports");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in TTKReportDAOImpl getActivityLogList()",sqlExp);
						throw new TTKException(sqlExp, "tTkReports");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "tTkReports");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
		}//end of finally
	}//end ofgetActivityLogList(String hospitalSeqId)
}//end of TTKReportDAO