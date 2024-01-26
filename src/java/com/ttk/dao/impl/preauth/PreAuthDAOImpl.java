/**
 *  @ (#) PreAuthDAOImpl.java April 10, 2006
 *   Project 	   : TTK HealthCare Services
 *   File          : PreAuthDAOImpl.java
 *   Author        : RamaKrishna K M
 *   Company       : Span Systems Corporation
 *   Date Created  : April 10, 2006
 *
 *   @author       :  RamaKrishna K M
 *   Modified by   :
 *   Modified date :
 *   Reason        :
 */

package com.ttk.dao.impl.preauth;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.rowset.OracleCachedRowSet;
import oracle.sql.BLOB;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import sun.misc.BASE64Encoder;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.MOUDocumentVO;
import com.ttk.dto.administration.ReportVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.DhpoWebServiceVO;
import com.ttk.dto.displayOfBenefits.BenefitsDetailsVO;
import com.ttk.dto.empanelment.HospitalDetailVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.AdditionalDetailVO;
import com.ttk.dto.preauth.AuthorizationVO;
import com.ttk.dto.preauth.BalanceCopayDeductionVO;//added as per KOC 1142 and 1140 Change Request and 1165
import com.ttk.dto.preauth.BalanceSIInfoVO;
import com.ttk.dto.preauth.ClaimantNewVO;
import com.ttk.dto.preauth.ClaimantVO;
import com.ttk.dto.preauth.ClinicianDetailsVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.DiscrepancyVO;
import com.ttk.dto.preauth.EnhancementVO;
import com.ttk.dto.preauth.InsOverrideConfVO;
import com.ttk.dto.preauth.MemberBufferVO;
import com.ttk.dto.preauth.MemberDetailVO;
import com.ttk.dto.preauth.ObservationDetailsVO;
import com.ttk.dto.preauth.PreAuthAssignmentVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthHospitalVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.ProviderDetailsVO;
import com.ttk.dto.preauth.RestorationPreauthClaimVO;
import com.ttk.dto.preauth.SIBreakupInfoVO;
import com.ttk.dto.preauth.SIInfoVO;
import com.ttk.dto.preauth.ShortfallVO;
import com.ttk.dto.preauth.StopPreauthClaimVO;
import com.ttk.dto.preauth.UserAssignVO;

import javax.xml.bind.DatatypeConverter;
public class PreAuthDAOImpl implements BaseDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PreAuthDAOImpl.class );
	 private static final String strClaimInsIntimate = "{CALL CLAIMS_PKG.CLM_INS_INTIMATE(?,?,?,?,?)}";//1274A
    private static final String strPatInsIntimate = "{CALL PRE_AUTH_PKG.PAT_INS_INTIMATE(?,?,?,?,?)}";//1274A
                                                
	private static final String strPatClmFileUpload = "{CALL CLAIMS_APPROVAL_PKG.PAT_CLM_DOC_UPLOAD(?,?,?,?,?,?)}";//1274A
	private static final String strUnFreezePatClm = "{CALL CLAIMS_APPROVAL_PKG.PAT_CLM_OVERRIDE(?,?,?,?)}";//1274A
	private static final String strOverrideConf = "{CALL CLAIMS_APPROVAL_PKG.OVERRIDE_INS_INTIMATION(?,?,?,?,?,?,?)}";//bajaj enhan
	
	//private static final String strPreAuthList = "{CALL PRE_AUTH_SQL_PKG.SELECT_PRE_AUTH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	//private static final String strPreAuthorization = "{CALL PRE_AUTH_SQL_PKG.SELECT_PRE_AUTH(?,?,?,?,?,?)}";
	private static final String strPreAuthList = "{CALL PRE_AUTH_PKG.SELECT_PRE_AUTH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//KOC FOR Grievance cigna 2 parameter
	private static final String strPartnerPreAuthList = "{CALL PARTNER_PREAUTH.ONLINE_PTR_PREAUTH_SUB_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strNetWorkTypes = "{CALL PRE_AUTH_PKG.SELECT_PRE_AUTH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//KOC FOR Grievance cigna 2 parameter
	private static final String strPreAuthEnhancementList = "{CALL AUTHORIZATION_PKG.SELECT_ENHANCE_PAT_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strActivityCodeList = "{CALL AUTHORIZATION_PKG.SELECT_ACTIVITY_LIST(?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strDiagnosisCodeList = "{CALL AUTHORIZATION_PKG.SELECT_ICD_LIST(?,?,?,?,?,?,?,?)}";
	private static final String strProviderList = "{CALL AUTHORIZATION_PKG.SELECT_PROVIDER_LIST(?,?,?,?,?,?,?,?,?)}";
	
	private static final String strMemberList = "{CALL AUTHORIZATION_PKG.SELECT_MEMBER_LIST(?,?,?,?,?,?,?)}";
	private static final String strPreAuthAndClmCount = "{CALL AUTHORIZATION_PKG.GET_PAT_CLM_COUNT(?,?)}";
	
	private static final  String strOverridreAuthDetails = "{CALL AUTHORIZATION_PKG.OVERRIDE_PREAUTH(?,?,?,?,?,?)}";
	
	private static final String strClinicianList = "{CALL AUTHORIZATION_PKG.SELECT_CLINICIAN_LIST(?,?,?,?,?,?,?,?,?,?)}";
	private static final String strProviderClinicianList = "{CALL HOSPITAL_PKG.SELECT_CLINICIAN_LIST(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strActivityCodeTariff = "{CALL AUTHORIZATION_PKG.SELECT_ACTIVITY_CODE(?,?,?,?,?,?,?)}";
	//private static final String strPreAuthorization = "{CALL SELECT_PAT_AUTH(?,?)}";
	private static final String strPreAuthorization = "{CALL AUTHORIZATION_PKG.SELECT_PAT_AUTH_DETAILS(?,?,?,?,?,?,?,?)}";
	private static final String strPartnerPreAuthorization = "{CALL PARTNER_PREAUTH.ONLINE_PTR_PREAUTH_SUB_DTL(?,?)}";
	private static final String strPreAuthorizationHistoryList = "{CALL AUTHORIZATION_PKG.SELECT_PAT_CLM_HISTORY(?,?,?)}";
	private static final String strPreAuthorizationHistoryWithBenefitsList = "{CALL AUTHORIZATION_PKG.SELECT_PAT_CLM_BENEFIT_HISTORY(?,?,?,?,?,?,?,?,?)}";
	private static final String strGetDiagnosisDetails = "{CALL SELECT_DIAGNOSYS_DETAILS(?,?)}";
	private static final String strGetIcdDetails= "{CALL AUTHORIZATION_PKG.SELECT_ICD(?,?,?,?)}";
	private static final String strGetDiagnosis = "{CALL AUTHORIZATION_PKG.SELECT_DIAGNOSYS_DETAILs(?,?,?,?)}";
	private static final String strSaveObservations = "{CALL AUTHORIZATION_PKG.SAVE_OBSERVATION_DETAILS(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strShortfallDetails = "{CALL AUTHORIZATION_PKG.SELECT_SHORTFALL_DETAILS(?,?,?,?,?)}";
	private static final String strGetAllObservations = "{CALL AUTHORIZATION_PKG.SELECT_OBSERVATION_DETAILS(?,?)}";
	private static final String strGetObservation = "{CALL AUTHORIZATION_PKG.SELECT_OBSERVATION_DETAIL(?,?)}";
	private static final String strDeleteObservations = "{CALL AUTHORIZATION_PKG.DELETE_OBSERVATION_DETAILS(?,?,?,?)}";	
	private static final String strSavePreAuthorization = "{CALL PRE_AUTH_PKG.SAVE_PREAUTH(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//added for insurance reference no
	private static final String strAddPreAuthorizationDetails = "{CALL AUTHORIZATION_PKG.SAVE_PAT_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//added for insurance reference no
	private static final String strAddDiagnosisDetails = "{CALL AUTHORIZATION_PKG.SAVE_DIAGNOSYS_DETAILS(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strDeleteDiagnosisDetails = "{CALL AUTHORIZATION_PKG.DELETE_DIAGNOSYS_DETAILS(?,?,?,?,?)}";
	private static final String strDeleteShortfallsDetails = "{CALL AUTHORIZATION_PKG.DELETE_SHORTFALLS(?,?,?,?,?)}";
	private static final String strDeleteActivityDetails = "{CALL AUTHORIZATION_PKG.DELETE_ACTIVITY_DETAILS(?,?,?,?,?)}";
	private static final String strSaveActivityDetails = "{CALL AUTHORIZATION_PKG.SAVE_ACTIVITY_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetActivityDetails = "{CALL AUTHORIZATION_PKG.SELECT_ACTIVITY_DETAILS(?,?)}";
	private static final String strGetCalculatePreauthAmount = "{CALL AUTHORIZATION_PKG.CALCULATE_AUTHORIZATION(?,?,?,?,?)}"; 
	
	private static final String strPreAuthAmountApproved = "{CALL AUTHORIZATION_PKG.SAVE_AUTHORIZATION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetdiagDetails = "SELECT DC.DENIAL_CODE,DC.DENIAL_DESCRIPTION FROM  APP.TPA_DENIAL_CODES DC WHERE MAN_OVERRIDE_YN IS NOT NULL ORDER BY  DC.SORT_NO ASC";
	
	private static final String strEncounterTypes = "{CALL AUTHORIZATION_PKG.SELECT_ENCOUNTER_TYPES(?,?)}";
	private static final String strOverridePreauth = "{CALL PRE_AUTH_PKG.OVERRIDE_PREAUTH(?,?,?,?,?,?,?,?)}";
	private static final String strSaveReview = "{CALL PRE_AUTH_PKG.SET_REVIEW(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSaveAssignUsers = "{CALL AUTHORIZATION_PKG.SAVE_ASSIGN_USERS(?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSaveShortfall = "{CALL AUTHORIZATION_PKG.SAVE_SHORTFALL_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	//private static final String strGetAssignTo = "{CALL PRE_AUTH_SQL_PKG.SELECT_ASSIGN_TO(?,?,?,?)}";
	private static final String strGetAssignTo = "{CALL AUTHORIZATION_PKG.SELECT_ASSIGN_TO(?,?,?,?)}";
	private static final String strPAAssignUserList = "{CALL AUTHORIZATION_PKG.MANUAL_ASSIGN_PREAUTH(?,?)}";
	private static final String strClaimAssignUserList = "{CALL AUTHORIZATION_PKG.MANUAL_ASSIGN_PREAUTH(?,?)}";
	//private static final String strEnrollmentList = "{CALL PRE_AUTH_SQL_PKG.SELECT_ENROLLMENT_ID_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	//private static final String strEnhancementList = "{CALL PRE_AUTH_SQL_PKG.SELECT_ENHANCEMENT_LIST(?,?,?,?,?)}";
	//private static final String strAuthorizationDetails = "{CALL PRE_AUTH_SQL_PKG.SELECT_AUTHORIZATION(?,?,?,?,?)}";
	//private static final String strSettlementDetails = "{CALL CLAIMS_SQL_PKG.SELECT_SETTLEMENT(?,?,?)}";
	private static final String strEnrollmentList = "{CALL PRE_AUTH_PKG.SELECT_ENROLLMENT_ID_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSelectMemberList = "{CALL PRE_AUTH_PKG.SELECT_MEMBER_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSelectEnrollmentID = "{CALL PRE_AUTH_PKG.SELECT_ENROLLMENT_ID(?,?)}";

	private static final String strEnhancementList = "{CALL PRE_AUTH_PKG.SELECT_ENHANCEMENT_LIST(?,?,?,?,?)}"; 
	private static final String strAuthorizationDetails = "{CALL PRE_AUTH_PKG.SELECT_AUTHORIZATION(?,?,?,?,?)}";
	private static final String strSettlementDetails = "{CALL CLAIMS_PKG.SELECT_SETTLEMENT(?,?,?)}";
		//Modification as per KOC 1216b Change request
	//private static final String strSaveAuthorization = "{CALL PRE_AUTH_PKG.SAVE_AUTHORIZATION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    
	//added 1 Parameter for High Deductable CR 
    private static final String strSaveAuthorization = "{CALL PRE_AUTH_PKG.SAVE_AUTHORIZATION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//added 3 parameters as per 1140 and 1142 and 1065
    //added 1 Parameter for Policy Deductable - KOC-1277
    private static final String strSaveSettlement = "{CALL CLAIMS_PKG.SAVE_SETTLEMENT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//added 3 parameters as per 1140 and 1142 and 1065
	private static final String strDeletePATGeneral = "{CALL PRE_AUTH_PKG.DELETE_PAT_GENERAL(?,?,?,?,?,?)}";
	//private static final String strGetDiscrepancy = "{CALL PRE_AUTH_SQL_PKG.SELECT_DISCREPANCY_INFO(?,?,?,?)}";
	private static final String strGetDiscrepancy = "{CALL PRE_AUTH_PKG.SELECT_DISCREPANCY_INFO(?,?,?,?)}";
	private static final String strResolveDiscrepancy = "{CALL PRE_AUTH_PKG.SAVE_DISCREPANCY(";
	private static final String strReturnToCoding = "{CALL CODING_PKG.RETURN_TO_CODING(?,?,?,?,?,?,?,?,?)}";
	private static final String strReAssignEnrID= "{CALL PRE_AUTH_PKG.RE_ASSIGN_ENRL_ID(?,?,?,?,?,?)}";
	//Modified  as per KOC 1216 B change Request 
	//private static final String strBalanceSIInfo= "{CALL PRE_AUTH_PKG.SELECT_CLAIMANT_BALANCE(?,?,?,?,?,?)}";
	private static final String strBalanceSIInfo= "{CALL PRE_AUTH_PKG.SELECT_CLAIMANT_BALANCE(?,?,?,?,?,?,?,?)}";//changes for koc1289_1272
	//Modified as per KOC 1216 B change Request 
	private static final String strPreAuthCopayAdvice="{CALL PRE_AUTH_PKG.COPAY_ADVICED(?,?,?,?,?,?)}";//Changes As Per KOC 1140 && 1142 
	private static final String strClaimsCopayAdvice="{CALL CLAIMS_PKG.COPAY_ADVICED(?,?,?,?,?,?)}";	//Changes As Per KOC 1140 && 1142
	private static final String strGetInsIntimation="{CALL CLAIMS_APPROVAL_PKG.SELECT_INS_INTIMATION(?,?,?)}";	//bajaj enhances
	
	private static final String strPreAuthHistoryList = "{CALL authorization_pkg.create_preauth_xml(?,?)}";
	private static final String strClaimHistoryList = "{CALL claim_pkg.create_claim_xml(?,?)}";
	private static final String strProviderClinicianDetail = 
			"SELECT P.CONTACT_SEQ_ID,P.PROFESSIONAL_ID,P.CONTACT_NAME,GC.GENERAL_TYPE_ID AS CONSULTAITON,CSM.SPECIALTY_ID AS SPECIALITY FROM TPA_HOSP_PROFESSIONALS"
			+" P LEFT OUTER JOIN TPA_HOSP_INFO I     ON (I.HOSP_SEQ_ID = P.HOSP_SEQ_ID)   LEFT OUTER JOIN TPA_GENERAL_CODE GC ON" 
			+"(P.CONSULT_GEN_TYPE = GC.GENERAL_TYPE_ID)  LEFT OUTER JOIN APP.DHA_CLNSN_SPECIALTIES_MASTER CSM   ON (CSM.SPECIALTY_ID =" 
			+"P.SPECIALITY_ID) WHERE P.PROFESSIONAL_ID = ? ";//I.HOSP_SEQ_ID = ? AND
	private static final String strGetProviderDocs = "SELECT I.MOU_DOC_SEQ_ID,GC.DESCRIPTION,to_char(I.added_date,'MM/DD/YYYY HH24:MI:SS' ) as ADDED_DATE, UC.CONTACT_NAME,I.DOCS_PATH,I.HOSP_SEQ_ID,I.FILE_NAME FROM APP.MOU_DOCS_INFO1 I LEFT JOIN APP.TPA_GENERAL_CODE GC ON (I.DOCS_GENTYPE_ID = GC.GENERAL_TYPE_ID) LEFT JOIN APP.TPA_USER_CONTACTS UC ON(UC.CONTACT_SEQ_ID=I.ADDED_BY) WHERE I.PAT_SEQ_ID=? ORDER BY I.MOU_DOC_SEQ_ID";

	
	private static final String strGetProviders = "SELECT I.HOSP_SEQ_ID AS PROVIDER_SEQ_ID, I.HOSP_LICENC_NUMB AS PROVIDER_ID,I.HOSP_NAME AS PROVIDER_NAME FROM APP.TPA_HOSP_INFO I JOIN APP.TPA_HOSP_EMPANEL_STATUS S ON (I.HOSP_SEQ_ID=S.HOSP_SEQ_ID) WHERE S.EMPANEL_STATUS_TYPE_ID='EMP' ORDER BY I.HOSP_LICENC_NUMB";
		
	private static final String strGetDhpoPreauthUpload = "{CALL PAT_XML_LOAD_PKG.SELECT_UPLOAD_PAT_DTLS(?,?)}";
	private static final String strSaveDhpoPreauthlogDetails = "{CALL PAT_XML_LOAD_PKG.SAVE_UPLOAD_PAT_LOGS(?,?,?,?,?,?)}";
	private static final String strGetDhpoPreauthLogDetails = "{CALL PAT_XML_LOAD_PKG.SELECT_UPLOAD_PAT_LOGS(?,?)}";
	private static final String strGetShortfallFile = "SELECT S.UPLOADED_FILE FROM SHORTFALL_DETAILS S WHERE S.DOCS_STATUS = 'Y' AND SHORTFALL_SEQ_ID=?";
	private static final String strGetClaimFile = "{CALL CLAIM_PKG.SELECT_ONL_DOCS(?,?,?)}";


	private static final String strSavePreAuthDentalDetails = "{CALL AUTHORIZATION_PKG.SAVE_ORTHODONTIC_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	//private static final String strGetClaimFile = "{CALL CLAIM_PKG.SELECT_ONL_DOCS(?,?)}";
	private static final String strSaveProviderCopay = "{CALL AUTHORIZATION_PKG.SELECT_PROVIDER_COPAY(?,?,?)}";
	
	private static final String strGetPrePartnerFile = "{CALL PARTNER_PREAUTH.ONL_PTNR_PREAUTH_DOCS(?,?,?)}";
	private static final String strGetbenefitDetails = "{CALL DISPLAY_OF_BENEFITS.GET_POLICY_TOB(?,?,?,?,?,?,?)}";
	
	private static final String strGetPolicyTobFile = "{CALL ADMINISTRATION_PKG.SELECT_POLICY_TOB_DOC(?,?)}";
	
	private static final String strgetPartnersList ="SELECT P.PTNR_SEQ_ID,P.PARTNER_NAME FROM APP.TPA_PARTNER_INFO P JOIN APP.TPA_PARTNER_EMPANEL_STATUS E ON (P.PTNR_SEQ_ID=E.PTNR_SEQ_ID) WHERE E.EMPANEL_STATUS_TYPE_ID = 'EMP'";
	
	
	
	
	
	
    private static final String strSavePreauthUploadFils=	"{CALL AUTHORIZATION_PKG.upld_clm_pat_docs (?,?,?,?,?,?,?,?,?)}";
	
	//private static final String  strGetPreauthUploadFils="SELECT I.MOU_DOC_SEQ_ID,GC.DESCRIPTION,to_char(I.added_date,'MM/DD/YYYY HH24:MI:SS' ) as ADDED_DATE, UC.CONTACT_NAME,I.DOCS_PATH,I.HOSP_SEQ_ID,I.FILE_NAME FROM APP.MOU_DOCS_INFO I JOIN APP.TPA_GENERAL_CODE GC ON (I.DOCS_GENTYPE_ID = GC.GENERAL_TYPE_ID) JOIN APP.TPA_USER_CONTACTS UC ON(UC.CONTACT_SEQ_ID=I.ADDED_BY) WHERE I.HOSP_SEQ_ID=? ORDER BY I.MOU_DOC_SEQ_ID";
    private static final String  strGetPreauthUploadFils="SELECT pc.docs_seq_id,pc.pat_clm_seq_id,g.description as file_desc,pc.file_name,to_char(pc.added_date, 'DD/MM/RRRR HH24:MI:SS') added_date,initcap(uc.contact_name) as contact_name,pc.file_path FROM pat_clm_docs_tab pc JOIN pat_authorization_details p ON (pc.pat_clm_seq_id = p.pat_auth_seq_id)LEFT JOIN tpa_user_contacts uc ON (uc.contact_seq_id = pc.added_by )LEFT JOIN tpa_general_code g ON (g.general_type_id = pc.file_desc) WHERE  pc.source_id = 'PAT' AND pc.pat_clm_seq_id =? order by pc.added_date desc ";
    private static final String  strGetclaimsUploadFils="SELECT pc.docs_seq_id,pc.pat_clm_seq_id,g.description as file_desc,pc.file_name,to_char(pc.added_date, 'DD/MM/RRRR HH24:MI:SS') added_date,initcap(uc.contact_name) as contact_name,pc.file_path FROM pat_clm_docs_tab pc JOIN clm_authorization_details p ON (pc.pat_clm_seq_id = p.claim_seq_id) LEFT JOIN tpa_user_contacts uc ON (uc.contact_seq_id = pc.added_by) LEFT JOIN tpa_general_code g ON (g.general_type_id = pc.file_desc) WHERE  pc.source_id = 'CLM' AND pc.pat_clm_seq_id =? order by pc.added_date desc ";

	
	private static final String strDeleteDocsUpload          =    "{CALL AUTHORIZATION_PKG. delete_pat_clm_upld_details (?,?)}";

    private static final String strDeleteTdsCertificates		=	"{CALL AUTHORIZATION_PKG. delete_pat_clm_upld_details (?,?)}";

    private static final String strGetFile		=	"select p.onl_ext_pre_auth_doc, p.file_name, p.file_desc  from app.preauth_submission p  where p.onl_pre_auth_refno = ? ";
	private static final String strClaimPreAuthDownloadHistoryList="{CALL CLAIM_PKG.dn_ld_clm_dtls(?,?,?,?,?,?)}";
	private static final String strClaimPreAuthDownloadHistoryDetail="{CALL CLAIM_PKG.clm_and_pat_his_dtl(?,?,?)}";
	private static final String str_PolicyDetails="SELECT distinct EP.POLICY_SEQ_ID,EP.POLICY_NUMBER FROM TPA_ENR_POLICY EP JOIN TPA_ENR_POLICY_GROUP PG ON (EP.POLICY_SEQ_ID=PG.POLICY_SEQ_ID) JOIN TPA_ENR_POLICY_MEMBER PM ON (PG.POLICY_GROUP_SEQ_ID=PM.POLICY_GROUP_SEQ_ID) WHERE PM.TPA_ENROLLMENT_ID=? ORDER BY EP.POLICY_SEQ_ID desc";
	private static final String str_PoicyInfo="SELECT to_char(EP.EFFECTIVE_FROM_DATE,'DD-MM-YYYY') AS POLICY_START_DATE,to_char(EP.EFFECTIVE_TO_DATE,'DD-MM-YYYY') AS POLICY_END_DATE,EP.POLICY_SEQ_ID,CASE WHEN (SELECT COUNT(*) FROM TPA_ENR_POLICY P WHERE P.PREV_POLICY_SEQ_ID=?)<1 THEN 'Current Policy' ELSE 'Old Policy' END AS POLICY_FLAG FROM TPA_ENR_POLICY EP WHERE EP.POLICY_SEQ_ID=?";
	private static final String strfraudDeatilsSave = "{CALL CLAIM_PKG.INTERNAL_REMARK_STAT_SAVE(?,?,?,?,?,?,?,?)}";  
	private static final String strgetfraudDeatils = "{CALL claim_pkg. select_int_inv_details(?,?,?,?)}";
	private static final String strfraudDeatilsForCFDSave = "{CALL claim_pkg.save_inv_status(?,?,?,?,?,?,?,?,?,?,?,?,?)}";	
	private static final String strViewFraudHistory = "{CALL CLAIM_PKG.GET_CFD_DETAILS(?,?,?)}";
	private static final String strTatYesterdayReport = "{CALL MIS_V2_REPORTS_PKG.select_pat_tat_prod_report(?,?,?,?,?,?)}";
	private static final String strPreUserDeatils = "{CALL preauth_autoassign_pkg.assign_user_list(?,?)}";
	private static final String strGetBatchAssignTo = "{CALL AUTHORIZATION_PKG.SELECT_BATCH_ASSIGN_TO(?,?)}";
	private static final String strSaveAssignBatch = "{CALL CLAIM_PKG.ASSIGN_BATCH_CLAIMS(?,?,?,?,?,?,?,?)}";
	private static final String strGetOverrideRemarksList = "{CALL authorization_pkg.select_override_rmk_alert(?,?,?)}";
	
	
	private static final int PAT_ENROLL_DETAIL_SEQ_ID = 1;
	private static final int MEMBER_SEQ_ID = 2;
	private static final int TPA_ENROLLMENT_ID = 3;
	private static final int POLICY_NUMBER = 4;
	private static final int POLICY_SEQ_ID = 5;
	private static final int INS_SEQ_ID = 6;
	private static final int GENDER_GENERAL_TYPE_ID = 7;
	private static final int CLAIMANT_NAME = 8;
	private static final int MEM_AGE = 9;
	private static final int DATE_OF_INCEPTION = 10;
	private static final int DATE_OF_EXIT = 11;
	private static final int MEM_TOTAL_SUM_INSURED = 12;
	private static final int CATEGORY_GENERAL_TYPE_ID = 13;
	private static final int INSURED_NAME = 14;
	private static final int PHONE_1 = 15;
	private static final int POLICY_EFFECTIVE_FROM = 16;
	private static final int POLICY_EFFECTIVE_TO = 17;
	private static final int PRODUCT_SEQ_ID = 18;
	private static final int INS_STATUS_GENERAL_TYPE_ID = 19;
	private static final int ENROL_TYPE_ID = 20;
	private static final int POLICY_SUB_GENERAL_TYPE_ID = 21;
	private static final int GROUP_REG_SEQ_ID = 22;
	private static final int DATE_OF_HOSPITALIZATION = 23;
	private static final int TPA_OFFICE_SEQ_ID = 24;
	private static final int HOSP_SEQ_ID = 25;
	private static final int PAT_STATUS_GENERAL_TYPE_ID = 26;
	private static final int PAT_GEN_DETAIL_SEQ_ID = 27;
	private static final int PAT_GENERAL_TYPE_ID =28;
	private static final int PAT_PRIORITY_GENERAL_TYPE_ID = 29;
	private static final int PAT_RECEIVED_DATE =30;
	private static final int PREV_APPROVED_AMOUNT = 31;
	private static final int PAT_REQUESTED_AMOUNT = 32;
	private static final int TREATING_DR_NAME = 33;
	private static final int PAT_RCVD_THRU_GENERAL_TYPE_ID = 34;
	private static final int PHONE_NO_IN_HOSPITALISATION = 35;
	private static final int DISCREPANCY_PRESENT_YN = 36;
	private static final int PARENT_GEN_DETAIL_SEQ_ID = 37;
	private static final int REMARKS = 38;
	private static final int AVA_SUM_INSURED = 39;
	private static final int AVA_CUM_BONUS = 40;
	private static final int ADDITIONAL_DTL_SEQ_ID = 41;
	private static final int RELSHIP_TYPE_ID = 42;
	private static final int EMPLOYEE_NO = 43;
	private static final int EMPLOYEE_NAME = 44;
	private static final int DATE_OF_JOINING = 45;
	private static final int INFO_RECEIVED_DATE = 46;
	private static final int COMM_TYPE_ID = 47;
	private static final int INFO_RCVD_GENERAL_TYPE_ID = 48;
	private static final int REFERENCE_NO = 49;
	private static final int CONTACT_PERSON = 50;
	private static final int ADDITIONAL_REMARKS = 51;
	private static final int AVA_BUFFER_AMOUNT = 52;
	private static final int BUFF_DTL_SEQ_ID = 53;
	private static final int BUFFER_ALLOWED_YN = 54;
	private static final int ADMIN_AUTH_GENERAL_TYPE_ID = 55;
	private static final int COMPLETED_YN = 56;
	private static final int PRE_AUTH_DMS_REFERENCE_ID = 57;
	/*private static final int SELECTION_TYPE = 58;
	private static final int USER_SEQ_ID = 59;
	private static final int ROW_PROCESSED = 60;*/
	private static final int EMAIL_ID = 58;
	private static final int INS_SCHEME = 59;
	private static final int CERTIFICATE_NO	= 60;
	private static final int INS_CUSTOMER_CODE = 61;
	private static final int SELECTION_TYPE = 62;
	private static final int INSUR_REF_NUMBER = 63;
	private static final int USER_SEQ_ID = 64;
	private static final int ROW_PROCESSED = 65;

	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
	 * @exception throws TTKException
	 */
	public ArrayList getPreAuthList(ArrayList alSearchCriteria) throws TTKException {
		
		System.out.println("alSearchCriteria.."+alSearchCriteria);
		
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthVO preAuthVO = null;
		String respSrtfall=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthList);
			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//preNO
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//proID
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//enrollID
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//claimName
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));//rDate
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));//offSEqID
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));//AssgType
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));//contName
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));//reqAmt
			cStmtObject.setString(10,(String)alSearchCriteria.get(9));//recGenType
			cStmtObject.setString(11,(String)alSearchCriteria.get(10));//StatusType
			cStmtObject.setString(12,(String)alSearchCriteria.get(11));//eventSEQ
			cStmtObject.setString(13,(String)alSearchCriteria.get(12));//patGenTape
			cStmtObject.setString(14,(String)alSearchCriteria.get(13));//poNO
			cStmtObject.setString(15,(String)alSearchCriteria.get(14));//emNo
			cStmtObject.setString(16,(String)alSearchCriteria.get(15));//grouName
			cStmtObject.setString(17,(String)alSearchCriteria.get(18));//InsSchema
			
			cStmtObject.setString(18,(String)alSearchCriteria.get(19));//cerNo
			cStmtObject.setString(19,(String)alSearchCriteria.get(20));//insStaus
			cStmtObject.setString(20,(String)alSearchCriteria.get(21));//insSeq
			cStmtObject.setString(21,(String)alSearchCriteria.get(22));//AuthNO
			cStmtObject.setString(22,(String)alSearchCriteria.get(23));	//netMemID

			cStmtObject.setString(23,(String)alSearchCriteria.get(38));	//sortvar		
			cStmtObject.setString(24,(String)alSearchCriteria.get(39));	//sortOrder	
			cStmtObject.setString(25,(String)alSearchCriteria.get(40));//start
			cStmtObject.setString(26,(String)alSearchCriteria.get(41));//end NO
			
			respSrtfall=alSearchCriteria.get(25).toString();
			
			
			
			cStmtObject.setLong(27,(Long)alSearchCriteria.get(16));//AddedBy
			cStmtObject.setString(28,(String)alSearchCriteria.get(24));//processType	
			cStmtObject.setString(29,(String)alSearchCriteria.get(25));//shortfallStatus
			cStmtObject.setString(30,(String)alSearchCriteria.get(26));//Event Ref Number
		
			/*Added by vishwabandhu*/
			cStmtObject.setString(31,(String)alSearchCriteria.get(27));//sLinkedClaimNo
			cStmtObject.setString(32,(String)alSearchCriteria.get(28));//sBenefitType
			cStmtObject.setString(33,(String)alSearchCriteria.get(29));//sAmountRange
			cStmtObject.setString(34,(String)alSearchCriteria.get(30));//sAmountRangeValue
			cStmtObject.setString(35,(String)alSearchCriteria.get(31));//partnerName
			cStmtObject.setString(36,(String)alSearchCriteria.get(32));//QatarID
			
			String sStatus= (String)alSearchCriteria.get(10);
			if("INP".equals(sStatus))
				cStmtObject.setString(37,(String)alSearchCriteria.get(33));//in-progress-Status
			else
				cStmtObject.setString(37,null);//in-progress-Status
			
			
			cStmtObject.setString(38,(String)alSearchCriteria.get(34));
			cStmtObject.setString(39,(String)alSearchCriteria.get(35));
			cStmtObject.setString(40,(String)alSearchCriteria.get(36));
			cStmtObject.registerOutParameter(41,OracleTypes.CURSOR);
			cStmtObject.setString(42,(String)alSearchCriteria.get(37));//priority corporate
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(41);
			if(rs != null){
			
				while(rs.next()){
					preAuthVO = new PreAuthVO();					
					preAuthVO.setPreAuthSeqID(rs.getLong("PAT_AUTH_SEQ_ID"));
					preAuthVO.setPreAuthNo(rs.getString("PRE_AUTH_NUMBER"));
					
					//preAuthVO.setReceivedDate(rs.getDate("PAT_RECEIVED_DATE"));
					preAuthVO.setReceivedDateAsString(rs.getString("PAT_RECEIVED_DATE"));
					
					if(rs.getString("HOSPITALIZATION_DATE") != null){
						preAuthVO.setClaimAdmnDate(new Date(rs.getTimestamp("HOSPITALIZATION_DATE").getTime()));
					}//end of if(rs.getString("HOSPITALIZATION_DATE") != null)
					
					if(rs.getString("DISCHARGE_DATE") != null){
						preAuthVO.setClaimDisrgDate(new Date(rs.getTimestamp("DISCHARGE_DATE").getTime()));
					}//end of if(rs.getString("DISCHARGE_DATE") != null)
					
					//TPA_OFFICE_SEQ_ID
					preAuthVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					
					String priority=rs.getString("PRIORITY_GENERAL_TYPE_ID");
					if("HIG".equals(priority)){
						preAuthVO.setPriorityImageName("HighPriorityIcon");
						preAuthVO.setPriorityImageTitle("High Priority");
					}else if("LOW".equals(priority)){
						preAuthVO.setPriorityImageName("LowPriorityIcon");
						preAuthVO.setPriorityImageTitle("Low Priority");
					}else {
						preAuthVO.setPriorityImageName("MediumPriorityIcon");
						preAuthVO.setPriorityImageTitle("Medium Priority");
					}
					preAuthVO.setHospitalName(rs.getString("HOSP_NAME"));
					
					preAuthVO.setEnrollmentID(rs.getString("TPA_ENROLLMENT_ID"));
					
					preAuthVO.setStrAlternateID(TTKCommon.checkNull(rs.getString("TPA_ALTERNATE_ID")));		// database column name
					
					preAuthVO.setClaimantName(rs.getString("MEM_NAME"));
					if(!((rs.getString("CONTACT_NAME")) == null|| (rs.getString("CONTACT_NAME")).trim().equals("null")||(rs.getString("CONTACT_NAME")).trim().equals("")))
					preAuthVO.setAssignedTo(rs.getString("CONTACT_NAME"));
					
					preAuthVO.setStatusTypeID(rs.getString("PAT_STATUS_TYPE_ID"));
					preAuthVO.setOralORsystemStatus(rs.getString("OralORsystemStatus"));
					
					preAuthVO.setProcessType(rs.getString("PROCESS_TYPE"));
					//preAuthVO.setShortfallYN(TTKCommon.checkNull(respSrtfall);
					preAuthVO.setShortfallYN(TTKCommon.checkNull(rs.getString("SHRTFALL_YN")));
					if(rs.getString("SHRTFALL_YN").equals("Y")){
						preAuthVO.setShortfallImageName("shortfall");
						
						preAuthVO.setShortfallImageTitle("Shortfall Received "+TTKCommon.checkNull(rs.getString("srtfll_updated_date")));
					}//end of if(rs.getString("SHRTFALL_YN").equals("Y"))
					preAuthVO.setEventReferenceNo(rs.getString("event_no"));
					preAuthVO.setStatus(rs.getString("pat_status_type_id"));
					preAuthVO.setConvertedAmount(rs.getBigDecimal("CONVERTED_AMOUNT"));
					preAuthVO.setEnhancedPreauthYN(TTKCommon.checkNull(rs.getString("ENHANCED_PREAUTHYN")));
                	if("Y".equals(preAuthVO.getEnhancedPreauthYN()))
                	{
                		preAuthVO.setPreauthEnhanceImageName("EnhancedPreauth");
                		preAuthVO.setPreauthEnhanceImageTitle("Enhanced PreApproval");
                	}
                	
                	preAuthVO.setsQatarId(rs.getString("QATAR_ID"));
                	if("INP-ENH".equals(rs.getString("IN_PROGESS_STATUS")))
                	{	
                		preAuthVO.setStatus("In-Progress-Enhancement");
                		preAuthVO.setInProImageName("InprogressEnhancement");
                		preAuthVO.setInProImageTitle("InProgress Enhancement");
                	}
                	
                	else if("APL".equals(rs.getString("IN_PROGESS_STATUS")))
                	{
                		preAuthVO.setStatus(rs.getString("pat_status_type_id"));
                		preAuthVO.setInProImageName("InprogressAppeal");
                		preAuthVO.setInProImageTitle("Appeal");
                	}
                	
                	else if("INP-RES".equals(rs.getString("IN_PROGESS_STATUS")))
                	{
                		preAuthVO.setStatus("In-Progress-Shortfall Responded");
                		preAuthVO.setInProImageName("AddIcon");
                		preAuthVO.setInProImageTitle("InProgress Shortfall Responded");
                	}
                	else
                	{
                		preAuthVO.setStatus(rs.getString("pat_status_type_id"));
                		preAuthVO.setInProImageName("Blank");
                		preAuthVO.setInProImageTitle("");
                	}
                	if(rs.getString("fraud_yn").equalsIgnoreCase("Y") && rs.getString("suspect_veri_check").equalsIgnoreCase("N") ){

                        preAuthVO.setFarudimg("suspectedcheckedfraud");
                        preAuthVO.setFraudImgTitle(TTKCommon.checkNull(rs.getString("status_desc")));
                    }else if(rs.getString("suspect_veri_check").equalsIgnoreCase("Y")){
                        preAuthVO.setFarudimg("verifiedclaimsearchAndhistoryimg");
                        preAuthVO.setFraudImgTitle(TTKCommon.checkNull(rs.getString("status_desc")));
                    }
                	preAuthVO.setCorporateName(TTKCommon.checkNull(rs.getString("group_name")));
                	preAuthVO.setPriorityCorporate(TTKCommon.checkNull(rs.getString("PRIORITY_CORPORATE")));
                	preAuthVO.setGroupImageName(TTKCommon.checkNull(rs.getString("GROUPIMAGENAME")));
                	preAuthVO.setGroupImageTitle(TTKCommon.checkNull(rs.getString("GROUPIMAGETITLE")));
                	preAuthVO.setBenefitType(TTKCommon.checkNull(rs.getString("BENEFITTYPE")));
                	
                	preAuthVO.setExternal_pat_yn(TTKCommon.checkNull(rs.getString("external_pat_yn")));
                	if("Y".equalsIgnoreCase(rs.getString("external_pat_yn"))){
                		preAuthVO.setExterLoginImag("exterloginImage");
                    	preAuthVO.setExterLoginDesc("MANDATORY PREAUTH");
                	}
                	
                	
                	
					alResultList.add(preAuthVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPreAuthList(ArrayList alSearchCriteria)
	
	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
	 * @exception throws TTKException
	 */
	public ArrayList getPartnerPreAuthList(ArrayList alSearchCriteria) throws TTKException {
		
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthVO preAuthVO = null;
		
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPartnerPreAuthList);
			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));
			cStmtObject.setString(7,(String)alSearchCriteria.get(7));
			cStmtObject.setString(8,(String)alSearchCriteria.get(8));
			cStmtObject.setString(9,(String)alSearchCriteria.get(9));
			cStmtObject.setString(10,(String)alSearchCriteria.get(10));
			cStmtObject.setString(11,(String)alSearchCriteria.get(11));
			cStmtObject.setString(12,(String)alSearchCriteria.get(12));
			
			if(!(alSearchCriteria.get(13)==null||alSearchCriteria.get(13).equals("")))
			cStmtObject.setLong(13,Long.parseLong((String)alSearchCriteria.get(13)));
			
			if(!(alSearchCriteria.get(14)==null||alSearchCriteria.get(14).equals("")))
				cStmtObject.setLong(14,Long.parseLong((String)alSearchCriteria.get(14)));
			
			
			
			
			
			cStmtObject.registerOutParameter(15,OracleTypes.CURSOR);
			cStmtObject.execute();
			
			rs = (java.sql.ResultSet)cStmtObject.getObject(15);
			ResultSetMetaData resultSetMetaData=rs.getMetaData();
			int count=resultSetMetaData.getColumnCount();
			if(rs != null){
				while(rs.next()){
					preAuthVO = new PreAuthVO();	
					preAuthVO.setPreauthPartnerRefId(rs.getString("onl_pre_auth_refno"));
					preAuthVO.setStatus(rs.getString("onl_pat_status"));
					preAuthVO.setPartnerName(rs.getString("partner_name"));
					preAuthVO.setEnrollmentID(rs.getString("tpa_enrollment_id"));
					preAuthVO.setClaimantName(rs.getString("MEM_NAME"));
					if(rs.getString("PRE_AUTH_NUMBER")!=null)
					preAuthVO.setPreAuthNo(rs.getString("PRE_AUTH_NUMBER"));
					if(rs.getString("PAT_STATUS_TYPE_ID")!=null)
						preAuthVO.setPreauthStatus(rs.getString("PAT_STATUS_TYPE_ID"));
					if(rs.getString("added_date")!=null){
					preAuthVO.setReceivedDate(rs.getDate("added_date"));
					preAuthVO.setReceivedDateAsString(TTKCommon.getFormattedDate(preAuthVO.getReceivedDate()));
					}
					if(rs.getString("admission_date")!=null)
					preAuthVO.setClaimAdmnDate(new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("admission_date")));
					if(rs.getString("discharge_date")!=null)
					preAuthVO.setClaimDisrgDate(new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("discharge_date")));
					
					preAuthVO.setPreAuthSeqID(rs.getLong("pat_auth_seq_id"));
					preAuthVO.setPartnerPreAuthSeqId(rs.getLong("onl_pat_auth_seq_id"));
					preAuthVO.setsQatarId(rs.getString("QATAR_ID"));
					
					alResultList.add(preAuthVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPreAuthList(ArrayList alSearchCriteria)
	
	 /**
     * This method returns the HashMap of String,String  which contains Network Types details
     * @return HashMap of String object which contains Network Types details
     * @exception throws TTKException
     */
	public HashMap<String,String> getNetworkTypeList() throws TTKException {
		
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		HashMap<String,String> networkTypes=new HashMap<>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strNetWorkTypes);
			cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(1);
			if(rs != null){
				while(rs.next()){
					networkTypes.put(rs.getString(""),rs.getString(""));
				}//end of while(rs.next())
			}//end of if(rs != null)
			return networkTypes;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getNetworkTypeList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
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
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getNetworkTypeList()
	
	 /**
     * This method returns the HashMap of String,String  which contains Network Types details
     * @return HashMap of String object which contains Network Types details
     * @exception throws TTKException
     */
	public HashMap<String,String> getPartnersList() throws TTKException {
		
		Connection conn = null;
		HashMap<String,String> partners=new HashMap<>();
		PreparedStatement pStmt 		= 	null;
		ResultSet rs = null;
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
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					throw new TTKException(sqlExp, "preauth");
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
						throw new TTKException(sqlExp, "preauth");
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
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getNetworkTypeList()
	
	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
	 * @exception throws TTKException
	 */
	public ArrayList getPreAuthEnhancementList(ArrayList alSearchCriteria) throws TTKException {
		
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthVO preAuthVO = null;
		
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthEnhancementList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7,(String)alSearchCriteria.get(7));
			cStmtObject.setString(8,(String)alSearchCriteria.get(8));
			cStmtObject.setString(9,(String)alSearchCriteria.get(10));
			cStmtObject.setString(10,(String)alSearchCriteria.get(11));
			cStmtObject.setString(11,(String)alSearchCriteria.get(12));
			cStmtObject.setString(12,(String)alSearchCriteria.get(13));
			cStmtObject.setLong(13,(Long)alSearchCriteria.get(6));
			
			cStmtObject.setString(14,(String)alSearchCriteria.get(9));
			
			cStmtObject.registerOutParameter(15,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(15);
			if(rs != null){
				while(rs.next()){
					preAuthVO = new PreAuthVO();					
					preAuthVO.setPreAuthSeqID(rs.getLong("PAT_AUTH_SEQ_ID"));
					preAuthVO.setPreAuthNo(rs.getString("PRE_AUTH_NUMBER"));
					//preAuthVO.setReceivedDate(rs.getDate("PAT_RECEIVED_DATE"));
					preAuthVO.setReceivedDateAsString(rs.getString("PAT_RECEIVED_DATE"));
			
				
					if(rs.getString("HOSPITALIZATION_DATE") != null){
						preAuthVO.setClaimAdmnDate(new Date(rs.getTimestamp("HOSPITALIZATION_DATE").getTime()));
					}//end of if(rs.getString("HOSPITALIZATION_DATE") != null)
					
					if(rs.getString("DISCHARGE_DATE") != null){
						preAuthVO.setClaimDisrgDate(new Date(rs.getTimestamp("DISCHARGE_DATE").getTime()));
					}//end of if(rs.getString("DISCHARGE_DATE") != null)
					
					
					String priority=rs.getString("PRIORITY_GENERAL_TYPE_ID");
					if("HIG".equals(priority)){
						preAuthVO.setPriorityImageName("HighPriorityIcon");
						preAuthVO.setPriorityImageTitle("High Priority");
					}else if("LOW".equals(priority)){
						preAuthVO.setPriorityImageName("LowPriorityIcon");
						preAuthVO.setPriorityImageTitle("Low Priority");
					}else if("MID".equals(priority)) {
						preAuthVO.setPriorityImageName("MediumPriorityIcon");
						preAuthVO.setPriorityImageTitle("Medium Priority");
					}
					preAuthVO.setHospitalName(rs.getString("HOSP_NAME"));
					preAuthVO.setEnrollmentID(rs.getString("TPA_ENROLLMENT_ID"));
					preAuthVO.setClaimantName(rs.getString("MEM_NAME"));
					preAuthVO.setAssignedTo(rs.getString("CONTACT_NAME"));
					preAuthVO.setStatusTypeID(rs.getString("PAT_STATUS_TYPE_ID"));
					preAuthVO.setsQatarId(rs.getString("QATAR_ID"));
					
					alResultList.add(preAuthVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthEnhancementList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthEnhancementList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthEnhancementList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPreAuthEnhancementList(ArrayList alSearchCriteria)
	
	
	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
	 * @exception throws TTKException
	 */
	public ArrayList getActivityCodeList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ActivityDetailsVO activityDetailsVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strActivityCodeList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));	
			cStmtObject.setString(6,(String)alSearchCriteria.get(7));
			cStmtObject.setString(7,(String)alSearchCriteria.get(8));
			cStmtObject.setString(8,(String)alSearchCriteria.get(9));
			cStmtObject.setString(9,(String)alSearchCriteria.get(10));
			cStmtObject.setLong(10,(Long)alSearchCriteria.get(5));		
			cStmtObject.setString(11,(String)alSearchCriteria.get(6));			
			cStmtObject.registerOutParameter(12,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(12);
			if(rs != null){
				while(rs.next()){
					activityDetailsVO = new ActivityDetailsVO();					
					activityDetailsVO.setActivityCode(rs.getString("ACTIVITY_CODE"));
					activityDetailsVO.setActivityCodeDesc(rs.getString("ACTIVITY_DESCRIPTION"));
					activityDetailsVO.setInternalCode(rs.getString("INTERNAL_CODE"));
					activityDetailsVO.setActivityCodeSeqId(rs.getLong("HOSP_TARIFF_SEQ_ID"));
					if("TAR".equals((String)alSearchCriteria.get(2))){
						activityDetailsVO.setNetworkType(rs.getString("Network_Type"));
					}
					
					alResultList.add(activityDetailsVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getActivityCodeList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getActivityCodeList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getActivityCodeList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getActivityCodeList(ArrayList alSearchCriteria)
	
	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
	 * @exception throws TTKException
	 */
	public ArrayList getDiagnosisCodeList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		DiagnosisDetailsVO diagnosisDetailsVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDiagnosisCodeList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(3));
			cStmtObject.setString(4,(String)alSearchCriteria.get(4));			
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));
			cStmtObject.setLong(7,(Long)alSearchCriteria.get(2));			
			cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(8);
			if(rs != null){
				while(rs.next()){
					diagnosisDetailsVO = new DiagnosisDetailsVO();					
					diagnosisDetailsVO.setIcdCode(rs.getString("ICD_CODE"));
					diagnosisDetailsVO.setAilmentDescription(rs.getString("LONG_DESC"));
					diagnosisDetailsVO.setIcdCodeSeqId(rs.getLong("ICD10_SEQ_ID"));
					diagnosisDetailsVO.setDiagnosiscount(rs.getLong("rcnt"));
					alResultList.add(diagnosisDetailsVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getDiagnosisCodeList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getDiagnosisCodeList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getDiagnosisCodeList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getDiagnosisCodeList(ArrayList alSearchCriteria)
	
	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
	 * @exception throws TTKException
	 */
	public ArrayList getProviderList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ProviderDetailsVO providerDetailsVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strProviderList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(3));
			cStmtObject.setString(4,(String)alSearchCriteria.get(4));
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));			
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));
			cStmtObject.setString(7,(String)alSearchCriteria.get(7));
			cStmtObject.setLong(8,(Long)alSearchCriteria.get(2));			
			cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(9);
			if(rs != null){
				while(rs.next()){
					providerDetailsVO = new ProviderDetailsVO();					
					providerDetailsVO.setProviderSeqId(rs.getLong("HOSP_SEQ_ID"));
					providerDetailsVO.setProviderId(rs.getString("HOSP_LICENC_NUMB"));
					providerDetailsVO.setProviderName(rs.getString("HOSP_NAME"));
					providerDetailsVO.setProviderSpecificRemarks(rs.getString("PROVIDER_SPECIFIC_REMARKS"));
					//providerDetailsVO.setAuthority(rs.getString("AUTHORITY"));
					providerDetailsVO.setCurencyType(rs.getString("CURRENCY_ID"));
					providerDetailsVO.setEmpanelmentNo(TTKCommon.checkNull(rs.getString("EMPANEL_NUMBER")));
					alResultList.add(providerDetailsVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getProviderList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getProviderList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getProviderList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getProviderList(ArrayList alSearchCriteria)
	
	//Ramana
	
	public ArrayList getMemberList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		MemberDetailVO MemberDetailVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strMemberList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(2));
			cStmtObject.setString(3,(String)alSearchCriteria.get(3));			
			cStmtObject.setString(4,(String)alSearchCriteria.get(4));
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));
			cStmtObject.setLong(6,(Long)alSearchCriteria.get(1));			
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(7);
			if(rs != null){
				while(rs.next()){
					MemberDetailVO = new MemberDetailVO();					
					MemberDetailVO.setMemberSeqID(rs.getLong("MEMBER_SEQ_ID"));
					MemberDetailVO.setPolicySeqId(rs.getLong("POLICY_SEQ_ID"));
					MemberDetailVO.setMemberId(rs.getString("tpa_enrollment_id"));
					MemberDetailVO.setPolicyNumber(TTKCommon.checkNull(rs.getString("policy_number")));
					MemberDetailVO.setPolicyStartDate(TTKCommon.checkNull(rs.getString("start_date")));
					MemberDetailVO.setPolicyEndDate(TTKCommon.checkNull(rs.getString("end_date")));
					MemberDetailVO.setPatientName(rs.getString("MEM_NAME"));
					MemberDetailVO.setMemberAge(rs.getInt("MEM_AGE"));
					MemberDetailVO.setEmirateId(rs.getString("EMIRATE_ID"));
					MemberDetailVO.setPayerId((rs.getString("PAYER_ID")));
					MemberDetailVO.setPayerName(rs.getString("INS_COMP_NAME"));
					MemberDetailVO.setInsSeqId(rs.getLong("INS_SEQ_ID"));
					MemberDetailVO.setPatientGender(rs.getString("GENDER"));
					MemberDetailVO.setCorporateName(rs.getString("CORPORATE_NAME"));
					MemberDetailVO.setNationality(rs.getString("NATIONALITY"));
					MemberDetailVO.setSumInsured(rs.getBigDecimal("SUM_INSURED"));
					MemberDetailVO.setAvailableSumInsured(rs.getBigDecimal("AVA_SUM_INSURED"));
					MemberDetailVO.setProductName(rs.getString("PRODUCT_NAME"));
					MemberDetailVO.setProvAuthority(rs.getString("AUTHORITY"));
					MemberDetailVO.setEligibleNetworks(rs.getString("NETWORK_TYPE"));
					MemberDetailVO.setPreMemInceptionDt(rs.getString("MEM_INSP_DATE"));
					MemberDetailVO.setVipYorN(rs.getString("VIP_YN"));
					MemberDetailVO.setBankName(TTKCommon.checkNull(rs.getString("BANK_NAME")));
					MemberDetailVO.setAccountNumber(TTKCommon.checkNull(rs.getString("IBAN_NUMBER")));
					MemberDetailVO.setPaymentMadeFor(TTKCommon.checkNull(rs.getString("PAYMENT_MADE_FOR")));
					MemberDetailVO.setRelationShip(TTKCommon.checkNull(rs.getString("relationship_desc")));
					MemberDetailVO.setRelationFlag(TTKCommon.checkNull(rs.getString("COLOUR_YN")));
					MemberDetailVO.setMemberSpecificRemarks(TTKCommon.checkNull(rs.getString("member_remarks")));
					alResultList.add(MemberDetailVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getMemberList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getMemberList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getMemberList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getProviderList(ArrayList alSearchCriteria)
	
	
	//for getting pre-auth and claim counts;
	public int[] getPreAuthAndClmCount(Long memberSeqId) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		int[] count = new int[2];
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthAndClmCount);
			cStmtObject.setLong(1,memberSeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
				count[0] = rs.getInt("pat_count");
				count[1] = rs.getInt("clm_count");
				}
			}
			return count;
		}catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthAndClmCount()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthAndClmCount()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthAndClmCount()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPreAuthAndClmCount(String id)

	
	
	
	
	
	
	
	
	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
	 * @exception throws TTKException
	 */
	public ArrayList getClinicianList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ClinicianDetailsVO clinicianDetailsVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClinicianList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));			
			cStmtObject.setString(7,(String)alSearchCriteria.get(7));
			cStmtObject.setString(8,(String)alSearchCriteria.get(8));
			cStmtObject.setLong(9,(Long)alSearchCriteria.get(4));			
			cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(10);
			if(rs != null){
				while(rs.next()){
					clinicianDetailsVO = new ClinicianDetailsVO();	
					if(rs.getString("CONTACT_SEQ_ID")!=null){
						clinicianDetailsVO.setClinicianSeqId(rs.getLong("CONTACT_SEQ_ID"));
					}
					if(rs.getString("PROFESSIONAL_ID")!=null){
						clinicianDetailsVO.setClinicianId(rs.getString("PROFESSIONAL_ID"));
					}else{
						clinicianDetailsVO.setClinicianId("");
					}
					if(rs.getString("CONTACT_NAME")!=null){
						clinicianDetailsVO.setClinicianName(rs.getString("CONTACT_NAME"));
					}
					if(rs.getString("CLINICIAN_SPECIALITY")!=null){
						clinicianDetailsVO.setClinicianSpeciality(rs.getString("CLINICIAN_SPECIALITY"));
					}
					alResultList.add(clinicianDetailsVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getClinicianList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getClinicianList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getClinicianList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClinicianList(ArrayList alSearchCriteria)
	
	
	
	public ArrayList getProviderClinicianList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ClinicianDetailsVO clinicianDetailsVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strProviderClinicianList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(6));
			cStmtObject.setString(6,(String)alSearchCriteria.get(7));			
			cStmtObject.setString(7,(String)alSearchCriteria.get(8));
			cStmtObject.setString(8,(String)alSearchCriteria.get(9));
			cStmtObject.setLong(9,(Long)alSearchCriteria.get(4));		
			cStmtObject.setLong(10,(Long)alSearchCriteria.get(5));			
			cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(11);
			if(rs != null){
				while(rs.next()){
					clinicianDetailsVO = new ClinicianDetailsVO();	
					clinicianDetailsVO.setClinicianSeqId(rs.getLong("CONTACT_SEQ_ID"));
					clinicianDetailsVO.setClinicianId(rs.getString("PROFESSIONAL_ID"));
					clinicianDetailsVO.setClinicianName(rs.getString("CONTACT_NAME"));
					clinicianDetailsVO.setClinicianSpeciality(rs.getString("SPECIALITY"));
					clinicianDetailsVO.setClinicianConsultation(rs.getString("CONSULTAITON"));
					alResultList.add(clinicianDetailsVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getClinicianList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getClinicianList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getClinicianList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getProviderClinicianList(ArrayList alSearchCriteria)
	
	
	public ClinicianDetailsVO getProviderClinicianDetails(String clinicianId,Long hospSeqId) throws TTKException {
    	Connection conn = null;
    	ClinicianDetailsVO clinicianDetailsVO = null;	
    	PreparedStatement pStmt 		= 	null;
    	ResultSet rs = null;
    	try {
            conn = ResourceManager.getConnection();
            pStmt=conn.prepareStatement(strProviderClinicianDetail);
           // pStmt.setLong(1, hospSeqId);
            pStmt.setString(1, clinicianId);
            rs = pStmt.executeQuery();
            if(rs != null){
                if(rs.next()){
                	clinicianDetailsVO = new ClinicianDetailsVO();
					clinicianDetailsVO.setClinicianSeqId(rs.getLong("CONTACT_SEQ_ID"));
					clinicianDetailsVO.setClinicianId(rs.getString("PROFESSIONAL_ID"));
					clinicianDetailsVO.setClinicianName(rs.getString("CONTACT_NAME"));
					clinicianDetailsVO.setClinicianSpeciality(rs.getString("SPECIALITY"));
					clinicianDetailsVO.setClinicianConsultation(rs.getString("CONSULTAITON"));
                }//end of while(rs.next())
            }//end of if(rs != null)
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
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
					throw new TTKException(sqlExp, "hospital");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
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
							log.error("Error while closing the Connection in OnlinePreAuthDAOImpl getPreAuthDetails()",sqlExp);
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
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
		return clinicianDetailsVO;
     }//end of getProviderClinicianDetails(String clinicianId,Long hospSeqId)
	
	
	/**
	 * This method returns the ActivityDetailsVO   which contains ActivityDetailsVO details
	 * @param activityDetailsVO ActivityDetailsVO object which contains the Selected data
	 * @return This method returns the ActivityDetailsVO   which contains ActivityDetailsVO details
	 * @exception throws TTKException
	 */
	public ActivityDetailsVO getActivityCodeTariff(String activityCode,String seqID,String activityStartDate,String authType,String benefitType,String searchType) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet resultSet = null;
		ResultSet tarrifrs=null;
		ActivityDetailsVO activityDetailsVO=new ActivityDetailsVO();
		activityDetailsVO.setDisplayMsg("NOTVALID");
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strActivityCodeTariff);
			cStmtObject.setString(1,seqID);
			cStmtObject.setString(2,authType);
			cStmtObject.setString(3,activityCode);
			cStmtObject.setString(4,searchType);
			cStmtObject.setDate(5,new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(activityStartDate).getTime()));
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();
		   resultSet=(ResultSet)cStmtObject.getObject(6);	
			if(resultSet!=null){
			 if(resultSet. next()){
				 activityDetailsVO.setDisplayMsg("VALID");
			activityDetailsVO.setActivityCodeSeqId(resultSet.getLong("ACTIVITY_SEQ_ID"));
			activityDetailsVO.setActivityCode(resultSet.getString("ACTIVITY_CODE"));
			activityDetailsVO.setActivityCodeDesc(resultSet.getString("ACTIVITY_DESCRIPTION"));
			activityDetailsVO.setActivityTypeId(resultSet.getString("ACTIVITY_TYPE_ID"));
			activityDetailsVO.setActivityTariffStatus(resultSet.getString("ACTIVITY_TARIFF_STATUS"));
			
			if(resultSet.getString("TARIFF_YN")!=null){
				activityDetailsVO.setTariffYN(resultSet.getString("TARIFF_YN"));
		     }
			activityDetailsVO.setToothNoReqYN(resultSet.getString("TOOTH_REQ_YN"));//resultSet.getString("TOOTH_REQ_YN")
			if(resultSet.getBigDecimal("granular_unit") == null){
				activityDetailsVO.setGranularUnit(new BigDecimal(0.00));
			}else{
				activityDetailsVO.setGranularUnit(resultSet.getBigDecimal("granular_unit"));
			}
			
			if(resultSet.getBigDecimal("no_of_units") == null){
				activityDetailsVO.setNoOfUnits(new BigDecimal(0.00));
			}else{
				activityDetailsVO.setNoOfUnits(resultSet.getBigDecimal("no_of_units"));
			}
			tarrifrs=(ResultSet)cStmtObject.getObject(7);
			
				if(tarrifrs!=null){
					if(tarrifrs.next()){
						
					
					if(tarrifrs.getBigDecimal("GROSS_AMOUNT")!=null ){
						
						activityDetailsVO.setGrossAmount(tarrifrs.getBigDecimal("GROSS_AMOUNT"));
					}else{
						
						activityDetailsVO.setGrossAmount(new BigDecimal("0.00"));
					}
						
					if(benefitType.equalsIgnoreCase("CB")){
						if(tarrifrs.getBigDecimal("UNIT_PRICE")!=null){
						activityDetailsVO.setUnitPrice(tarrifrs.getBigDecimal("UNIT_PRICE"));
						}else{
							activityDetailsVO.setUnitPrice(new BigDecimal("0.00"));
						}
						
					}else{
						if(tarrifrs.getBigDecimal("GROSS_AMOUNT")!=null){
					        activityDetailsVO.setUnitPrice(tarrifrs.getBigDecimal("GROSS_AMOUNT"));
						}else{
							activityDetailsVO.setUnitPrice(new BigDecimal("0.00"));
						}
						
					}
					
					
				if(tarrifrs.getBigDecimal("DISC_AMOUNT")!=null){
					activityDetailsVO.setDiscount(tarrifrs.getBigDecimal("DISC_AMOUNT"));
				}
				
				if(tarrifrs.getBigDecimal("UNIT_DISCOUNT")!=null){
					activityDetailsVO.setDiscountPerUnit(tarrifrs.getBigDecimal("UNIT_DISCOUNT"));
				}
				
			  
				if(benefitType.equalsIgnoreCase("CB")){
						
					if(tarrifrs.getBigDecimal("GROSS_AMOUNT")!=null){
						activityDetailsVO.setDiscountedGross(tarrifrs.getBigDecimal("GROSS_AMOUNT"));
						activityDetailsVO.setNetAmount(tarrifrs.getBigDecimal("GROSS_AMOUNT"));
					}
					else{
						activityDetailsVO.setDiscountedGross(new BigDecimal("0.00"));
						activityDetailsVO.setNetAmount(new BigDecimal("0.00"));
					}
					
					if(tarrifrs.getBigDecimal("REQ_QUANTITY")!=null){
						activityDetailsVO.setQuantity(new Float(tarrifrs.getFloat("REQ_QUANTITY")));
						activityDetailsVO.setApprovedQuantity(new Float(tarrifrs.getFloat("REQ_QUANTITY")));
					}
					
					if(tarrifrs.getBigDecimal("CONV_UNIT_PRICE")!=null){
						activityDetailsVO.setConvertedUnitPrice(tarrifrs.getBigDecimal("CONV_UNIT_PRICE"));
					}
					else{
						activityDetailsVO.setConvertedUnitPrice(new BigDecimal("0.00"));
					}
					
							
						
				}else{
				
					if(tarrifrs.getString("DISC_GROSS_AMOUNT") != null){
						activityDetailsVO.setDiscountedGross(tarrifrs.getBigDecimal("DISC_GROSS_AMOUNT"));
						activityDetailsVO.setNetAmount(tarrifrs.getBigDecimal("DISC_GROSS_AMOUNT"));
						}
					else{
						activityDetailsVO.setDiscountedGross(new BigDecimal("0.00"));
						activityDetailsVO.setNetAmount(new BigDecimal("0.00"));
					}//end of else
					
					
					
				}
				
			if(tarrifrs.getString("BUNDLE_ID")!=null){
				activityDetailsVO.setBundleId(tarrifrs.getString("BUNDLE_ID"));
			}
			if(tarrifrs.getString("PACKAGE_ID")!=null){
				activityDetailsVO.setPackageId(tarrifrs.getString("PACKAGE_ID"));
		     }
			if(tarrifrs.getString("INTERNAL_CODE")!=null){
				activityDetailsVO.setInternalCode(tarrifrs.getString("INTERNAL_CODE"));
		     }
			activityDetailsVO.setInternalDesc(tarrifrs.getString("INTERNAL_DESC"));
			activityDetailsVO.setAlAhliYN(tarrifrs.getString("AL_AHLI_YN"));
			}
			
			}
			 }
			}
			return activityDetailsVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (resultSet != null) resultSet.close();
					if (tarrifrs != null) tarrifrs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getActivityCodeTariff()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getActivityCodeTariff()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getActivityCodeTariff()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				resultSet = null;
				tarrifrs	=	null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getActivityCodeTariff(ArrayList alSearchCriteria)

	public ArrayList<String[]> getPreauthHistoryList(Long memberSeqID,String authType) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
	    ResultSet ars = null;
		ArrayList<String[]> authList=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthorizationHistoryList);
 
			cStmtObject.setLong(1,memberSeqID);	
			cStmtObject.setString(2,authType);	
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			ars = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(ars!=null){		
				authList=new ArrayList<String[]>();
				while(ars.next()){
					String authSeqId="";
					String preAuthNo="";
					String authNo="";
					if("PAT".equals(authType)){
						authSeqId=ars.getLong("PAT_AUTH_SEQ_ID")==0?"":ars.getLong("PAT_AUTH_SEQ_ID")+"";
						preAuthNo=ars.getString("PRE_AUTH_NUMBER")==null?"":ars.getString("PRE_AUTH_NUMBER");
						 authNo=ars.getString("AUTH_NUMBER")==null?"":ars.getString("AUTH_NUMBER");
					}
					else if("CLM".equals(authType)){
							authSeqId=ars.getLong("CLAIM_SEQ_ID")==0?"":ars.getLong("CLAIM_SEQ_ID")+"";
						    preAuthNo=ars.getString("CLAIM_NUMBER")==null?"":ars.getString("CLAIM_NUMBER");
						    authNo=ars.getString("CLAIM_NUMBER")==null?"":ars.getString("CLAIM_NUMBER");
						}
					
					String hospitalName=ars.getString("HOSP_NAME")==null?"":ars.getString("HOSP_NAME");
					String approvedAmt=ars.getBigDecimal("TOT_APPROVED_AMOUNT")==null?"":ars.getBigDecimal("TOT_APPROVED_AMOUNT").toString();
					String admissionDate=ars.getDate("ADMISSION_DATE")==null?"":ars.getDate("ADMISSION_DATE").toString();
					String status=ars.getString("PAT_STATUS")==null?"":ars.getString("PAT_STATUS");
					authList.add(new String[]{authSeqId,preAuthNo,authNo,hospitalName,approvedAmt,admissionDate,status});			
				   }			
				}
			return authList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (ars != null) ars.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreauthHistoryList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreauthHistoryList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreauthHistoryList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				ars = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPreauthHistoryList(Long memberSeqID)
		
		public ArrayList<String[]> getPreauthHistoryWithBenefitsList(Long memberSeqID,String authType,String memberId,Long policySeqId,String benefitType,String sortBy,String ascOrDesc) throws TTKException{
			Connection conn = null;
			CallableStatement cStmtObject=null;
		    ResultSet ars1 = null;
		    ResultSet ars2 = null;
			ArrayList<String[]> authList=null;
			ArrayList<String[]> returnList = new ArrayList<String[]>();
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthorizationHistoryWithBenefitsList);
	            cStmtObject.setLong(1,memberSeqID);	
				cStmtObject.setString(2,authType);
				cStmtObject.setString(3,memberId);
				cStmtObject.setLong(4,policySeqId);
				cStmtObject.setString(5,benefitType);
				cStmtObject.setString(6,sortBy);
				cStmtObject.setString(7,ascOrDesc);
				cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);
				cStmtObject.execute();
				ars1 = (java.sql.ResultSet)cStmtObject.getObject(8);
				ars2 = (java.sql.ResultSet)cStmtObject.getObject(9);
				authList=new ArrayList<String[]>();
				
				if(ars1!=null){		
					authList=new ArrayList<String[]>();
					while(ars1.next()){
						String authSeqId="";
						String preAuthNo="";
						String authNo="";
						String icdCode = "";
						String icdDescription="";
						String fraudclaim="";
						String suspectVerifyclaim="";
						String suspecteddesc = "";
						String linkedClmNo="";
						String invNo="";
						String enhancedpreauth="";
						 String concatICD_ICDDesc="";
						if("PAT".equals(authType)){
							authSeqId=ars1.getLong("PAT_AUTH_SEQ_ID")==0?"":ars1.getLong("PAT_AUTH_SEQ_ID")+"";
							preAuthNo=ars1.getString("PRE_AUTH_NUMBER")==null?"":ars1.getString("PRE_AUTH_NUMBER");
							icdCode=ars1.getString("diag")==null?"":ars1.getString("diag");
							icdDescription=ars1.getString("ICD_DESC")==null?"":ars1.getString("ICD_DESC");
							String icdCodeDescSplit[] = icdDescription.split("/");
							String icdCodeSplit[] = icdCode.split("/");
							String strhyperlinkICDCode[];
                            strhyperlinkICDCode  = new String[icdCodeSplit.length];
							for(int i=0,j=0;i<icdCodeSplit.length &&j<icdCodeDescSplit.length;i++,j++)
							{
								if((!"".equals(icdCodeSplit[i])))
									strhyperlinkICDCode[i] ="<a  title=\""+icdCodeDescSplit[i]+"\" style=\"COLOR: #1ea0f2; FONT-WEIGHT: bold\">"+icdCodeSplit[i]+"</a>";
								else
									strhyperlinkICDCode[i] ="";
							}
                             int index=0;
							for(int i=0;i<strhyperlinkICDCode.length;i++)
							{
								if(i==3+(4*index))
								{ 
									index++;
									concatICD_ICDDesc=concatICD_ICDDesc+"<br/>";
								}

								if(strhyperlinkICDCode.length-1==i)	
									concatICD_ICDDesc=	concatICD_ICDDesc+strhyperlinkICDCode[i];
								else
									concatICD_ICDDesc=	concatICD_ICDDesc+strhyperlinkICDCode[i]+"/";	

							}
							 authNo=ars1.getString("AUTH_NUMBER")==null?"":ars1.getString("AUTH_NUMBER");
							 linkedClmNo=ars1.getString("CLAIM_NUMBER")==null?"":ars1.getString("CLAIM_NUMBER");
							enhancedpreauth=ars1.getString("ENHANCED_PREAUTHYN")==null?"":ars1.getString("ENHANCED_PREAUTHYN");
						}
						else if("CLM".equals(authType)){
								authSeqId=ars1.getLong("CLAIM_SEQ_ID")==0?"":ars1.getLong("CLAIM_SEQ_ID")+"";
							    preAuthNo=ars1.getString("CLAIM_NUMBER")==null?"":ars1.getString("CLAIM_NUMBER");
							    authNo=ars1.getString("SETTLEMENT_NUMBER")==null?"":ars1.getString("SETTLEMENT_NUMBER");
							     icdCode=ars1.getString("diag")==null?"":ars1.getString("diag");
								icdDescription=ars1.getString("ICD_DESC")==null?"":ars1.getString("ICD_DESC");
								String icdCodeDescSplit[] = icdDescription.split("/");
								String icdCodeSplit[] = icdCode.split("/");
								String strhyperlinkICDCode[];

								strhyperlinkICDCode  = new String[icdCodeSplit.length];
								for(int i=0,j=0;i<icdCodeSplit.length && j<icdCodeDescSplit.length;i++,j++)
								{
									if((!"".equals(icdCodeSplit[i])))
									strhyperlinkICDCode[i] ="<a  title=\""+icdCodeDescSplit[i]+"\" style=\"COLOR: #1ea0f2; FONT-WEIGHT: bold\">"+icdCodeSplit[i]+"</a>";
									else
										strhyperlinkICDCode[i] ="";	
								}
                                 
								int index=0;
								for(int i=0;i<strhyperlinkICDCode.length;i++)
								{
									if(i==3+(4*index))
									{  index++;
										concatICD_ICDDesc=concatICD_ICDDesc+"<br/>";
									}
									if(strhyperlinkICDCode.length-1==i)	
										concatICD_ICDDesc=	concatICD_ICDDesc+strhyperlinkICDCode[i];
									else
										concatICD_ICDDesc=	concatICD_ICDDesc+strhyperlinkICDCode[i]+"/";	
								}
								fraudclaim = ars1.getString("fraud_yn") == null? "":ars1.getString("fraud_yn");
							    suspectVerifyclaim = ars1.getString("suspect_veri_check") == null? "":ars1.getString("suspect_veri_check");
							    suspecteddesc = ars1.getString("status_code_id") == null? "":ars1.getString("status_code_id");
							    linkedClmNo=ars1.getString("PRE_AUTH_NUMBER")==null?"":ars1.getString("PRE_AUTH_NUMBER");
							     invNo=ars1.getString("INVOICE_NUMBER")==null?"":ars1.getString("INVOICE_NUMBER");
							}
						
						String hospitalName=ars1.getString("HOSP_NAME")==null?"":ars1.getString("HOSP_NAME");
						String approvedAmt=ars1.getBigDecimal("TOT_APPROVED_AMOUNT")==null?"":ars1.getBigDecimal("TOT_APPROVED_AMOUNT").toString();
						String admissionDate=ars1.getString("ADMISSION_DATE")==null?"":ars1.getString("ADMISSION_DATE");
						String status=ars1.getString("PAT_STATUS")==null?"":ars1.getString("PAT_STATUS");
						String bType=ars1.getString("BENIFIT_TYPE")==null?"":ars1.getString("BENIFIT_TYPE");
						String payStatus=ars1.getString("PAYMENT_STATUS")==null?"":ars1.getString("PAYMENT_STATUS");
						String balance=ars1.getString("pre_blocked_amount")==null?"":ars1.getString("pre_blocked_amount"); 
						if("SENT_TO_BANK".equals(payStatus))
							payStatus	=	"APPROVED BY FINANCE";
														
						String claimFileNo=ars1.getString("CLAIM_FILE_NO")==null?"":ars1.getString("CLAIM_FILE_NO");
						authList.add(new String[]{authSeqId,preAuthNo,authNo,hospitalName,approvedAmt,bType,admissionDate,status,payStatus,claimFileNo,fraudclaim,suspectVerifyclaim,suspecteddesc,linkedClmNo,invNo,enhancedpreauth,concatICD_ICDDesc,balance});
											   }
				}
					
				if(ars2!=null){	
					ars2.next();
					String sum = ars2.getString("sum_tot_apr_amt");
					authList.add(new String[]{sum});
				}
				return authList;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (ars1 != null) ars1.close();
						if (ars2 != null) ars2.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in PreAuthDAOImpl getPreauthHistoryList()",sqlExp);
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
							log.error("Error while closing the Statement in PreAuthDAOImpl getPreauthHistoryList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PreAuthDAOImpl getPreauthHistoryList()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					ars1 = null;
					ars2 =null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of getPreauthHistoryList(Long memberSeqID)
	
	
	

	/**
	 * This method returns the PreAuthDetailVO, which contains all the Pre-Authorization details
	 * @param preauthVO Object which contains seq id's to get the Pre-Authorization Details
	 * @param strSelectionType String object which contains Selection Type - 'PAT' or 'ICO'
	 * @return PreAuthDetailVO object which contains all the Pre-Authorization details
	 * @exception throws TTKException
	 */
	
	public PreAuthDetailVO getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthDetailVO preAuthDetailVO = null;
		ClaimantVO claimantVO = null;
		AdditionalDetailVO additionalDetailVO = null;
		PreAuthHospitalVO preAuthHospitalVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthorization);

			if(preauthVO.getMemberSeqID() != null){
				cStmtObject.setLong(1,preauthVO.getMemberSeqID());
			}//end of if(preauthVO.getMemberSeqID() != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else

			if(preauthVO.getPreAuthSeqID() != null){
				cStmtObject.setLong(2,preauthVO.getPreAuthSeqID());
			}//end of if(preauthVO.getPreAuthSeqID() != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			if(preauthVO.getEnrollDtlSeqID() != null){
				cStmtObject.setLong(3,preauthVO.getEnrollDtlSeqID());
			}//end of if(preauthVO.getEnrollDtlSeqID() != null)
			else{
				cStmtObject.setString(3,null);
			}//end of else

			cStmtObject.setLong(4,preauthVO.getUpdatedBy());
			cStmtObject.setString(5,strSelectionType);
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(6);
			if(rs != null){
				while(rs.next()){
					preAuthDetailVO = new PreAuthDetailVO();
					claimantVO = new ClaimantVO();
					additionalDetailVO = new AdditionalDetailVO();
					preAuthHospitalVO = new PreAuthHospitalVO();

					if(rs.getString("PAT_ENROLL_DETAIL_SEQ_ID") != null){
						preAuthDetailVO.setEnrollDtlSeqID(new Long(rs.getLong("PAT_ENROLL_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("PAT_ENROLL_DETAIL_SEQ_ID") != null)

					if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null){
						preAuthDetailVO.setPreAuthSeqID(new Long(rs.getLong("PAT_GEN_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null)

					preAuthDetailVO.setPreAuthNo(TTKCommon.checkNull(rs.getString("PRE_AUTH_NUMBER")));
					preAuthDetailVO.setPreAuthTypeID(TTKCommon.checkNull(rs.getString("PAT_GENERAL_TYPE_ID")));

					if(rs.getString("PAT_RECEIVED_DATE") != null){
						preAuthDetailVO.setReceivedDate(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime()));
						preAuthDetailVO.setReceivedTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setReceivedDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("PAT_RECEIVED_DATE") != null)

					if(rs.getString("DATE_OF_HOSPITALIZATION") != null){
						preAuthDetailVO.setAdmissionDate(new Date(rs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime()));
						preAuthDetailVO.setAdmissionTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setAdmissionDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("DATE_OF_HOSPITALIZATION") != null)

					if(rs.getString("PREV_APPROVED_AMOUNT") != null){
						preAuthDetailVO.setPrevApprovedAmount(new BigDecimal(rs.getString("PREV_APPROVED_AMOUNT")));
					}//end of if(rs.getString("PREV_APPROVED_AMOUNT") != null)

					/*if(rs.getString("PAT_REQUESTED_AMOUNT") != null){
						preAuthDetailVO.setRequestAmount(new BigDecimal(rs.getString("PAT_REQUESTED_AMOUNT")));
					}//end of if(rs.getString("PAT_REQUESTED_AMOUNT") != null)
*/
					preAuthDetailVO.setDoctorName(TTKCommon.checkNull(rs.getString("TREATING_DR_NAME")));
					preAuthDetailVO.setStatusTypeID(TTKCommon.checkNull(rs.getString("PAT_STATUS_GENERAL_TYPE_ID")));
					preAuthDetailVO.setPriorityTypeID(TTKCommon.checkNull(rs.getString("PAT_PRIORITY_GENERAL_TYPE_ID")));

					if(rs.getString("TPA_OFFICE_SEQ_ID") != null){
						preAuthDetailVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					}//end of if(rs.getString("TPA_OFFICE_SEQ_ID") != null)

					preAuthDetailVO.setPreAuthRecvTypeID(TTKCommon.checkNull(rs.getString("PAT_RCVD_THRU_GENERAL_TYPE_ID")));
					preAuthDetailVO.setHospitalPhone(TTKCommon.checkNull(rs.getString("PHONE_NO_IN_HOSPITALISATION")));
					preAuthDetailVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
					preAuthDetailVO.setAssignedTo(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
					preAuthDetailVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
					preAuthDetailVO.setPreAuthEnhancedYN(TTKCommon.checkNull(rs.getString("PAT_ENHANCED_YN")));

					if(rs.getString("PARENT_GEN_DETAIL_SEQ_ID") != null){
						preAuthDetailVO.setParentGenDtlSeqID(new Long(rs.getLong("PARENT_GEN_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("PARENT_GEN_DETAIL_SEQ_ID") != null)

					if(rs.getString("EVENT_SEQ_ID") != null){
						preAuthDetailVO.setEventSeqID(new Long(rs.getLong("EVENT_SEQ_ID")));
					}//end of if(rs.getString("EVENT_SEQ_ID") != null)

					if(rs.getString("REQUIRED_REVIEW_COUNT") != null){
						preAuthDetailVO.setRequiredReviewCnt(new Integer(rs.getInt("REQUIRED_REVIEW_COUNT")));
					}//end of if(rs.getString("REQUIRED_REVIEW_COUNT") != null)

					if(rs.getString("REVIEW_COUNT") != null){
						preAuthDetailVO.setReviewCount(new Integer(rs.getInt("REVIEW_COUNT")));
					}//end of if(rs.getString("REVIEW_COUNT") != null)

					preAuthDetailVO.setReview(TTKCommon.checkNull(rs.getString("REVIEW")));
					preAuthDetailVO.setEventName(TTKCommon.checkNull(rs.getString("EVENT_NAME")));
					preAuthDetailVO.setCompletedYN(TTKCommon.checkNull(rs.getString("COMPLETED_YN")));
					//preAuthDetailVO.setProcessCompletedYN(TTKCommon.checkNull(rs.getString("PROCESS_COMPLETED_YN")));
					preAuthDetailVO.setAdminAuthTypeID(TTKCommon.checkNull(rs.getString("ADMIN_AUTH_GENERAL_TYPE_ID")));
					preAuthDetailVO.setDMSRefID(TTKCommon.checkNull(rs.getString("PRE_AUTH_DMS_REFERENCE_ID")));
					preAuthDetailVO.setDiscPresentYN(TTKCommon.checkNull(rs.getString("DISCREPANCY_PRESENT_YN")));
					preAuthDetailVO.setEnrolChangeMsg(TTKCommon.checkNull(rs.getString("ENROLLMENT_CHANGE_MSG")));
					preAuthDetailVO.setShowReAssignIDYN(TTKCommon.checkNull(rs.getString("SHOW_REASSIGN_ID_YN")));

					if(rs.getString("MEMBER_SEQ_ID") != null){
						claimantVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs.getString("MEMBER_SEQ_ID") != null)

					claimantVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					claimantVO.setName(TTKCommon.checkNull(rs.getString("CLAIMANT_NAME")));
					claimantVO.setClaimantNameDisc(TTKCommon.checkNull(rs.getString("DIS_NAME_YN")));
					claimantVO.setGenderTypeID(TTKCommon.checkNull(rs.getString("GENDER_GENERAL_TYPE_ID")));
					claimantVO.setGenderDiscrepancy(TTKCommon.checkNull(rs.getString("DIS_GEND_YN")));

					if(rs.getString("MEM_AGE") != null){
						claimantVO.setAge(new Integer(rs.getString("MEM_AGE")));
					}//end of if(rs.getString("MEM_AGE") != null)
					
					preAuthDetailVO.setSeniorCitizenYN(TTKCommon.checkNull(rs.getString("senior_citizen_yn")));//koc for griavance
					preAuthDetailVO.setBufferNoteyn(TTKCommon.checkNull(rs.getString("buff_note_yn")));//koc for hyundai buffer
					preAuthDetailVO.setBufferRestrictyn(TTKCommon.checkNull(rs.getString("buffer_restrict_yn")));//koc for hyundai buffer
					claimantVO.setAgeDiscrepancy(TTKCommon.checkNull(rs.getString("DIS_AGE_YN")));

					if(rs.getString("DATE_OF_INCEPTION") != null){
						claimantVO.setDateOfInception(new Date(rs.getTimestamp("DATE_OF_INCEPTION").getTime()));
					}//end of if(rs.getString("DATE_OF_INCEPTION") != null)

					if(rs.getString("DATE_OF_EXIT") != null){
						claimantVO.setDateOfExit(new Date(rs.getTimestamp("DATE_OF_EXIT").getTime()));
					}//end of if(rs.getString("DATE_OF_EXIT") != null)

					claimantVO.setSumEnhancedYN(TTKCommon.checkNull(rs.getString("V_SUM_ENHANCED_YN")));

					if(rs.getString("MEM_TOTAL_SUM_INSURED") != null){
						claimantVO.setTotalSumInsured(new BigDecimal(rs.getString("MEM_TOTAL_SUM_INSURED")));
					}//end of if(rs.getString("MEM_TOT_SUM_INSURED") != null)

					if(rs.getString("AVA_SUM_INSURED") != null){
						claimantVO.setAvailSumInsured(new BigDecimal(rs.getString("AVA_SUM_INSURED")));
					}//end of if(rs.getString("AVA_SUM_INSURED") != null)
					else{
						claimantVO.setAvailSumInsured(new BigDecimal("0.00"));
					}//end of else

					if(rs.getString("BUFF_DETAIL_SEQ_ID") != null){
						claimantVO.setBufferDtlSeqID(new Long(rs.getLong("BUFF_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("BUFF_DETAIL_SEQ_ID") != null)

					claimantVO.setBufferAllowedYN(TTKCommon.checkNull(rs.getString("BUFFER_ALLOWED_YN")));

					if(rs.getString("BUFFER_AVA_AMOUNT") != null){
						claimantVO.setAvblBufferAmount(new BigDecimal(rs.getString("BUFFER_AVA_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AVA_AMOUNT") != null)
					else{
						claimantVO.setAvblBufferAmount(new BigDecimal("0.00"));
					}//end of else

					if(rs.getString("AVA_CUM_BONUS") != null){
						claimantVO.setCumulativeBonus(new BigDecimal(rs.getString("AVA_CUM_BONUS")));
					}//end of if(rs.getString("AVA_CUM_BONUS") != null)
					else{
						claimantVO.setCumulativeBonus(new BigDecimal("0.00"));
					}//end of else

					claimantVO.setCategoryTypeID(TTKCommon.checkNull(rs.getString("CATEGORY_GENERAL_TYPE_ID")));
					claimantVO.setCategoryDesc(TTKCommon.checkNull(rs.getString("CATEGORY")));
					claimantVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
					claimantVO.setPolicyNbrDisc(TTKCommon.checkNull(rs.getString("DIS_POLICY_NUM_YN")));

					if(rs.getString("POLICY_SEQ_ID") != null){
						claimantVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)

					claimantVO.setPolicyTypeID(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")));

					if(rs.getString("POLICY_EFFECTIVE_FROM") != null){
						claimantVO.setStartDate(new Date(rs.getTimestamp("POLICY_EFFECTIVE_FROM").getTime()));
					}//end of if(rs.getString("POLICY_EFFECTIVE_FROM") != null)

					if(rs.getString("POLICY_EFFECTIVE_TO") != null){
						claimantVO.setEndDate(new Date(rs.getTimestamp("POLICY_EFFECTIVE_TO").getTime()));
					}//end of if(rs.getString("POLICY_EFFECTIVE_TO") != null)

					if(rs.getString("PRODUCT_SEQ_ID") != null){
						claimantVO.setProductSeqID(new Long(rs.getLong("PRODUCT_SEQ_ID")));
					}//end of if(rs.getString("PRODUCT_SEQ_ID") != null)

					claimantVO.setPolicySubTypeID(TTKCommon.checkNull(rs.getString("POLICY_SUB_GENERAL_TYPE_ID")));
					claimantVO.setPolicyHolderName(TTKCommon.checkNull(rs.getString("INSURED_NAME")));
					claimantVO.setPolicyHolderNameDisc(TTKCommon.checkNull(rs.getString("DIS_POLICY_HOLDER_YN")));
					claimantVO.setTermStatusID(TTKCommon.checkNull(rs.getString("INS_STATUS_GENERAL_TYPE_ID")));
					claimantVO.setPhone(TTKCommon.checkNull(rs.getString("PHONE")));
					claimantVO.setClaimantPhoneNbr(TTKCommon.checkNull(rs.getString("PHONE")));
					if(rs.getString("GROUP_REG_SEQ_ID") != null){
						claimantVO.setGroupRegnSeqID(new Long(rs.getLong("GROUP_REG_SEQ_ID")));
					}//end of if(rs.getString("") != null)

					claimantVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));

					claimantVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));
					//changes on dec 7th & 8th  in PreauthIMPL.java 

					try{
						String VIP_YN=TTKCommon.checkNull(rs.getString("VIP_YN"));

						if( VIP_YN.equals("") || VIP_YN.equalsIgnoreCase("N"))
						{
							claimantVO.setVipYN("N");
						}
						if(VIP_YN.equalsIgnoreCase("Y")) {
							claimantVO.setVipYN("Y");
						}
						
					}
					catch(SQLException e) {
						// TODO: handle exception
						log.error("VIP_YN Column DOES NOT EXIST ");
					}
					/*else if(rs.getString("VIP_YN").equalsIgnoreCase("N") && rs.getString("VIP_YN").equalsIgnoreCase("") )
						{
							claimantVO.setVipYN("N");
						}*/


					//changes on dec 7th in PreauthIMPL.java 

					if(rs.getString("INS_SEQ_ID") != null){
						claimantVO.setInsSeqID(new Long(rs.getLong("INS_SEQ_ID")));
					}//end of if(rs.getString("") != null)

					claimantVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					claimantVO.setCompanyCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
					claimantVO.setEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
					claimantVO.setInsCustCode(TTKCommon.checkNull(rs.getString("INS_CUSTOMER_CODE")));
					claimantVO.setCertificateNo(TTKCommon.checkNull(rs.getString("CERTIFICATE_NO")));
					claimantVO.setInsScheme(TTKCommon.checkNull(rs.getString("INS_SCHEME")));
					claimantVO.setInsuranceRefNo((TTKCommon.checkNull(rs.getString("INSUR_REF_NUMBER"))));//added for insurance reference no

					if(rs.getString("ADDITIONAL_DTL_SEQ_ID") != null){
						additionalDetailVO.setAdditionalDtlSeqID(new Long(rs.getLong("ADDITIONAL_DTL_SEQ_ID")));
					}//end of if(rs.getString("ADDITIONAL_DTL_SEQ_ID") != null)

					additionalDetailVO.setRelationshipTypeID(TTKCommon.checkNull(rs.getString("RELSHIP_TYPE_ID")));
					additionalDetailVO.setEmployeeNbr(TTKCommon.checkNull(rs.getString("EMPLOYEE_NO")));
					additionalDetailVO.setEmployeeName(TTKCommon.checkNull(rs.getString("EMPLOYEE_NAME")));

					if(rs.getString("DATE_OF_JOINING") != null){
						additionalDetailVO.setJoiningDate(new Date(rs.getTimestamp("DATE_OF_JOINING").getTime()));
					}//end of if(rs.getString("DATE_OF_JOINING") != null)

					if(rs.getString("INFO_RECEIVED_DATE") != null){
						additionalDetailVO.setAddReceivedDate(new Date(rs.getTimestamp("INFO_RECEIVED_DATE").getTime()));
						additionalDetailVO.setAddReceivedTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("INFO_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("INFO_RECEIVED_DATE").getTime())).split(" ")[1]:"");
						additionalDetailVO.setAddReceivedDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("INFO_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("INFO_RECEIVED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("INFO_RECEIVED_DATE") != null)

					additionalDetailVO.setSourceTypeID(TTKCommon.checkNull(rs.getString("COMM_TYPE_ID")));
					additionalDetailVO.setReceivedFromType(TTKCommon.checkNull(rs.getString("INFO_RCVD_GENERAL_TYPE_ID")));
					additionalDetailVO.setReferenceNbr(TTKCommon.checkNull(rs.getString("REFERENCE_NO")));
					additionalDetailVO.setContactPerson(TTKCommon.checkNull(rs.getString("CONTACT_PERSON")));
					additionalDetailVO.setAdditionalRemarks(TTKCommon.checkNull(rs.getString("ADDITIONAL_REMARKS")));
					additionalDetailVO.setCertificateNo(TTKCommon.checkNull(rs.getString("CERTIFICATE_NO")));
					additionalDetailVO.setInsScheme(TTKCommon.checkNull(rs.getString("INS_SCHEME")));

					if(rs.getString("HOSP_SEQ_ID") != null){
						preAuthHospitalVO.setHospSeqId(new Long(rs.getLong("HOSP_SEQ_ID")));
					}//end of if(rs.getString("HOSP_SEQ_ID") != null)

					preAuthHospitalVO.setHospitalName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
					preAuthHospitalVO.setEmplNumber(TTKCommon.checkNull(rs.getString("EMPANEL_NUMBER")));

					if(rs.getString("FROM_DATE") != null){
						preAuthHospitalVO.setStartDate(new Date(rs.getTimestamp("FROM_DATE").getTime()));
					}//end of if(rs.getString("FROM_DATE") != null)

					if(rs.getString("TO_DATE") != null){
						preAuthHospitalVO.setEndDate(new Date(rs.getTimestamp("TO_DATE").getTime()));
					}//end of if(rs.getString("TO_DATE") != null)

					preAuthHospitalVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS_1")));
					preAuthHospitalVO.setAddress2(TTKCommon.checkNull(rs.getString("ADDRESS_2")));
					preAuthHospitalVO.setAddress3(TTKCommon.checkNull(rs.getString("ADDRESS_3")));
					preAuthHospitalVO.setPincode(TTKCommon.checkNull(rs.getString("PIN_CODE")));
					preAuthHospitalVO.setCityDesc(TTKCommon.checkNull(rs.getString("CITY_DESCRIPTION")));
					preAuthHospitalVO.setStateName(TTKCommon.checkNull(rs.getString("STATE_NAME")));
					preAuthHospitalVO.setPhoneNbr1(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_1")));
					preAuthHospitalVO.setPhoneNbr2(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_2")));
					preAuthHospitalVO.setFaxNbr(TTKCommon.checkNull(rs.getString("OFFICE_FAX_NO")));
					preAuthHospitalVO.setEmailID(TTKCommon.checkNull(rs.getString("PRIMARY_EMAIL_ID")));
					preAuthHospitalVO.setHospRemarks(TTKCommon.checkNull(rs.getString("HOSP_REMARKS")));
					preAuthHospitalVO.setHospStatus(TTKCommon.checkNull(rs.getString("HOSP_STATUS")));
					preAuthHospitalVO.setRating(TTKCommon.checkNull(rs.getString("RATING")));
					preAuthHospitalVO.setEmpanelStatusTypeID(TTKCommon.checkNull(rs.getString("EMPANEL_TYPE_ID")));
					preAuthHospitalVO.setHospServiceTaxRegnNbr(TTKCommon.checkNull(rs.getString("SERV_TAX_RGN_NUMBER")));
					if(preAuthHospitalVO.getRating() != null){
						if(preAuthHospitalVO.getRating().equals("G")){
							preAuthHospitalVO.setRatingImageName("GoldStar");
							preAuthHospitalVO.setRatingImageTitle("Gold Star");
						}//end of if(preAuthHospitalVO.getRating().equals("G"))

						if(preAuthHospitalVO.getRating().equals("R")){
							preAuthHospitalVO.setRatingImageName("BlueStar");
							preAuthHospitalVO.setRatingImageTitle("Blue Star (Regular)");
						}//end of if(preAuthHospitalVO.getRating().equals("R"))

						if(preAuthHospitalVO.getRating().equals("B")){
							preAuthHospitalVO.setRatingImageName("BlackStar");
							preAuthHospitalVO.setRatingImageTitle("Black Star");
						}//end of if(preAuthHospitalVO.getRating().equals("B"))
					}//end of if(preAuthHospitalVO.getRating() != null)

					preAuthDetailVO.setShowBandYN(TTKCommon.checkNull(rs.getString("SHOW_BAND_YN")));

					preAuthDetailVO.setShowCodingOverrideYN(TTKCommon.checkNull(rs.getString("SHOW_CODING_OVERRIDE")));
					preAuthDetailVO.setInsUnfreezeButtonYN(TTKCommon.checkNull(rs.getString("PAT_INS_STATUS")));//1274A
					preAuthDetailVO.setInsDecisionyn(TTKCommon.checkNull(rs.getString("INS_DECISION_YN")));//baja enhan
					preAuthDetailVO.setClaimantVO(claimantVO);
					preAuthDetailVO.setAdditionalDetailVO(additionalDetailVO);
					preAuthDetailVO.setPreAuthHospitalVO(preAuthHospitalVO);
					
				}//end of while(rs.next())
			}//end of if(rs != null)
			return preAuthDetailVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPreAuthDetail(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
	
	public Object[] getPreAuthDetails(Long preAuthSeqID) throws TTKException{
		
	
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet prs=null;ResultSet drs=null;ResultSet ars = null;ResultSet shrs = null,pcount=null,clmcount=null;
		PreAuthDetailVO preAuthDetailVO  = new PreAuthDetailVO();
		Object[] preauthAllResult=new Object[4];
		ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
		ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
		ArrayList<String[]> shortfalls=null;
		try{
			conn = ResourceManager.getConnection(); 
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthorization);

			cStmtObject.setLong(1,preAuthSeqID);			
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
			cStmtObject.execute();
			prs = (java.sql.ResultSet)cStmtObject.getObject(2);//preauth Details
			drs = (java.sql.ResultSet)cStmtObject.getObject(3);//diagnosis Details
			ars = (java.sql.ResultSet)cStmtObject.getObject(4);//activity Details
			shrs = (java.sql.ResultSet)cStmtObject.getObject(6);//Shortfalls Details
			pcount = (java.sql.ResultSet)cStmtObject.getObject(7);//PreAuth Count.
			clmcount= (java.sql.ResultSet)cStmtObject.getObject(8);//Claim Count.
						
			if(prs != null){
				if(prs.next()){
					
					if(prs.getString("PRODUCT_NAME") !=null)preAuthDetailVO.setProductName(prs.getString("PRODUCT_NAME"));
						preAuthDetailVO.setPreAuthNo(TTKCommon.checkNull(prs.getString("PRE_AUTH_NUMBER")));
						
						if(prs.getString("PAYER_AUTHORITY") !=null)preAuthDetailVO.setPayerAuthority(prs.getString("PAYER_AUTHORITY"));
						preAuthDetailVO.setPreAuthNo(TTKCommon.checkNull(prs.getString("PAYER_AUTHORITY")));	
						
                   if(prs.getLong("PAT_AUTH_SEQ_ID") !=0)preAuthDetailVO.setPreAuthSeqID(prs.getLong("PAT_AUTH_SEQ_ID"));
					preAuthDetailVO.setPreAuthNo(TTKCommon.checkNull(prs.getString("PRE_AUTH_NUMBER")));
					if(prs.getLong("PARENT_PAT_AUTH_SEQ_ID") !=0)preAuthDetailVO.setParentPreAuthSeqID(prs.getLong("PARENT_PAT_AUTH_SEQ_ID"));
					preAuthDetailVO.setPreAuthNo(TTKCommon.checkNull(prs.getString("PRE_AUTH_NUMBER")));
					if(prs.getString("AUTH_NUMBER") !=null){
						preAuthDetailVO.setAuthNum(prs.getString("AUTH_NUMBER"));
					}//
					preAuthDetailVO.setPreAuthRecvTypeID(TTKCommon.checkNull(prs.getString("SOURCE_TYPE_ID")));		
					if(prs.getString("PAT_RECEIVED_DATE") != null){
						preAuthDetailVO.setReceiveDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(prs.getTimestamp("PAT_RECEIVED_DATE").getTime())));
						preAuthDetailVO.setReceiveTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setReceiveDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(prs.getString("PAT_RECEIVED_DATE") != null)
					if(prs.getString("HOSPITALIZATION_DATE") != null){
						preAuthDetailVO.setAdmissionDate(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime()));
						preAuthDetailVO.setAdmissionTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setAdmissionDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ")[2]:"");
					}//end of if(prs.getString("DATE_OF_HOSPITALIZATION") != null)
					if(prs.getString("DISCHARGE_DATE") != null){
						preAuthDetailVO.setDischargeDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())));
						preAuthDetailVO.setDischargeTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setDischargeDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[2]:"");
					}//end of if(prs.getString("DISCHARGE_DATE") != null)
					
					if(prs.getString("TPA_ENROLLMENT_ID") != null){
						preAuthDetailVO.setMemberId(prs.getString("TPA_ENROLLMENT_ID"));
					}//end of if(prs.getString("PAT_REQUESTED_AMOUNT") != null)
					if(prs.getLong("MEMBER_SEQ_ID") !=0){
						preAuthDetailVO.setMemberSeqID(prs.getLong("MEMBER_SEQ_ID"));
					}//end of if(prs.getLong("MEMBER_SEQ_ID") != null)
					if(prs.getString("MEM_NAME") != null){
						preAuthDetailVO.setPatientName(prs.getString("MEM_NAME"));
					}//end of if(prs.getString("MEM_NAME") != null)
					if(prs.getString("COMPLETED_YN") != null){
						preAuthDetailVO.setPreauthCompleteStatus(prs.getString("COMPLETED_YN"));
					}//end of if(prs.getString("COMPLETED_YN") != null)
					
					if(prs.getInt("MEM_AGE") != 0){
						preAuthDetailVO.setMemberAge(prs.getInt("MEM_AGE"));
					}
					
					if(prs.getString("EMIRATE_ID") !=null){
						preAuthDetailVO.setEmirateId(prs.getString("EMIRATE_ID"));
					}	
					if(prs.getString("PAYER_ID") !=null){
						preAuthDetailVO.setPayerId(prs.getString("PAYER_ID"));
					}
					if(prs.getLong("INS_SEQ_ID") !=0){
					preAuthDetailVO.setInsSeqId(prs.getLong("INS_SEQ_ID"));
					}
					if(prs.getLong("POLICY_SEQ_ID") !=0){
						preAuthDetailVO.setPolicySeqId(prs.getLong("POLICY_SEQ_ID"));
					}
					if(prs.getString("INS_COMP_NAME") !=null){
						preAuthDetailVO.setPayerName(prs.getString("INS_COMP_NAME"));
					}
					if(prs.getString("ENCOUNTER_TYPE_ID") !=null){
						preAuthDetailVO.setEncounterTypeId(prs.getString("ENCOUNTER_TYPE_ID"));
					}
					if(prs.getString("ENCOUNTER_FACILITY_ID") !=null){
						preAuthDetailVO.setEncounterFacilityId(prs.getString("ENCOUNTER_FACILITY_ID"));
					}
					if(prs.getString("TREATMENT_TYPE") !=null){
						preAuthDetailVO.setTreatmentTypeID(prs.getString("TREATMENT_TYPE"));
					}
					if(prs.getString("PROVIDER_ID") !=null){
						preAuthDetailVO.setProviderId(prs.getString("PROVIDER_ID"));
					}
					if(prs.getLong("HOSP_SEQ_ID") !=0){
						preAuthDetailVO.setProviderSeqId(prs.getLong("HOSP_SEQ_ID"));
					}					
					if(prs.getString("HOSP_NAME") !=null){
						preAuthDetailVO.setProviderName(prs.getString("HOSP_NAME"));
					}
					if(prs.getString("HOSP_ADDRESS") !=null){
						preAuthDetailVO.setProviderDetails(prs.getString("HOSP_ADDRESS"));
					}
					if(prs.getString("ENCOUNTER_START_TYPE") !=null){
						/*if("DNTL".equals(prs.getString("BENIFIT_TYPES"))){
							preAuthDetailVO.setTreatmentTypeID(prs.getString("ENCOUNTER_START_TYPE"));
						}else{*/
							preAuthDetailVO.setEncounterStartTypeId(prs.getString("ENCOUNTER_START_TYPE"));
						/*}*/
					}
					if(prs.getString("ENCOUNTER_END_TYPE") !=null){
						preAuthDetailVO.setEncounterEndTypeId(prs.getString("ENCOUNTER_END_TYPE"));
					}
					if(prs.getString("CLINICIAN_ID") !=null){
						preAuthDetailVO.setClinicianId(prs.getString("CLINICIAN_ID"));
					}
					if(prs.getString("CLINICIAN_NAME") !=null){
						preAuthDetailVO.setClinicianName(prs.getString("CLINICIAN_NAME"));
					}
					if(prs.getString("SYSTEM_OF_MEDICINE_TYPE_ID") !=null){
						preAuthDetailVO.setSystemOfMedicine(prs.getString("SYSTEM_OF_MEDICINE_TYPE_ID"));
					}
					if(prs.getString("ACCIDENT_RELATED_TYPE_ID") !=null){
						preAuthDetailVO.setAccidentRelatedCase(prs.getString("ACCIDENT_RELATED_TYPE_ID"));
					}
					if(prs.getString("GENDER") !=null){
						preAuthDetailVO.setPatientGender(prs.getString("GENDER"));
					}
					if(prs.getString("PRESENTING_COMPLAINTS") !=null){
						preAuthDetailVO.setPresentingComplaints(prs.getString("PRESENTING_COMPLAINTS"));
					}
					
					//Added for PED
					preAuthDetailVO.setDurAilment(prs.getInt("DUR_AILMENT"));

			
					if(prs.getString("DURATION_FLAG") !=null){
					preAuthDetailVO.setDurationFlag(prs.getString("DURATION_FLAG"));
					}
					
					if(prs.getString("MEDICAL_OPINION_REMARKS") !=null){
						preAuthDetailVO.setMedicalOpinionRemarks(prs.getString("MEDICAL_OPINION_REMARKS"));
					}
					if(prs.getString("PAT_STATUS_TYPE_ID") !=null){
						preAuthDetailVO.setPreauthStatus(prs.getString("PAT_STATUS_TYPE_ID"));
					}
					if(prs.getString("COMPLETED_YN") !=null){
						String viewMode=prs.getString("COMPLETED_YN");
						preAuthDetailVO.setPreauthViewMode("Y".equals(viewMode)?"true":"false");
					}
					if(prs.getString("PRIORITY_GENERAL_TYPE_ID") !=null){					
						preAuthDetailVO.setPriorityTypeID(prs.getString("PRIORITY_GENERAL_TYPE_ID"));
					}
					if(prs.getString("NETWORK_YN") !=null){						
						preAuthDetailVO.setNetworkProviderType(prs.getString("NETWORK_YN"));
					}
                   if(prs.getString("CITY_TYPE_ID") !=null){						
						preAuthDetailVO.setProviderArea(prs.getString("CITY_TYPE_ID"));
					}
                   if(prs.getString("STATE_TYPE_ID") !=null){						
						preAuthDetailVO.setProviderEmirate(prs.getString("STATE_TYPE_ID"));
					}
                   if(prs.getString("COUNTRY_TYPE_ID") !=null){						
						preAuthDetailVO.setProviderCountry(prs.getString("COUNTRY_TYPE_ID"));
					}
                   if(prs.getString("PROVIDER_ADDRESS") !=null){						
						preAuthDetailVO.setProviderAddress(prs.getString("PROVIDER_ADDRESS"));
					}
                   if(prs.getString("PINCODE") !=null){						
						preAuthDetailVO.setProviderPobox(prs.getString("PINCODE"));
					}
					
					if(prs.getBigDecimal("REQUESTED_AMOUNT") !=null){						
						preAuthDetailVO.setRequestedAmount(prs.getBigDecimal("REQUESTED_AMOUNT"));
						}
					if(prs.getString("REMARKS") !=null){
						preAuthDetailVO.setRemarks(prs.getString("REMARKS"));
					}
					if(prs.getString("CLINICIAN_STATUS") !=null){
						preAuthDetailVO.setClinicianStatus(prs.getString("CLINICIAN_STATUS"));
					}
					if(prs.getBigDecimal("TOT_GROSS_AMOUNT") !=null){
						preAuthDetailVO.setGrossAmount(prs.getBigDecimal("TOT_GROSS_AMOUNT"));
					}
					if(prs.getBigDecimal("TOT_DISCOUNT_AMOUNT") !=null){
						preAuthDetailVO.setDiscountAmount(prs.getBigDecimal("TOT_DISCOUNT_AMOUNT"));
					}
					if(prs.getBigDecimal("TOT_DISC_GROSS_AMOUNT") !=null){
						preAuthDetailVO.setDiscountGrossAmount(prs.getBigDecimal("TOT_DISC_GROSS_AMOUNT"));
					}
					
					if(prs.getBigDecimal("TOT_PATIENT_SHARE_AMOUNT") !=null){
						preAuthDetailVO.setPatShareAmount(prs.getBigDecimal("TOT_PATIENT_SHARE_AMOUNT"));
					}
					if(prs.getBigDecimal("TOT_NET_AMOUNT") !=null){
						preAuthDetailVO.setNetAmount(prs.getBigDecimal("TOT_NET_AMOUNT"));
					}
					if(prs.getBigDecimal("TOT_ALLOWED_AMOUNT") !=null){
						preAuthDetailVO.setAllowedAmount(prs.getBigDecimal("TOT_ALLOWED_AMOUNT"));
					}
					if(prs.getBigDecimal("FINAL_APP_AMOUNT") !=null){
						preAuthDetailVO.setApprovedAmount(prs.getBigDecimal("FINAL_APP_AMOUNT"));
					}
					if(prs.getString("POLICY_NUMBER") !=null){
						preAuthDetailVO.setPolicyNumber(prs.getString("POLICY_NUMBER"));
					}
					if(prs.getString("CORP_NAME") !=null){
						preAuthDetailVO.setCorporateName(prs.getString("CORP_NAME"));
					}
					/*if(prs.getString("GRAVIDA") !=null){
						preAuthDetailVO.setGravida(prs.getInt("GRAVIDA"));
					}
					if(prs.getString("LIVE") !=null){
						preAuthDetailVO.setLive(prs.getInt("LIVE"));
					}
					if(prs.getString("PARA") !=null){
						preAuthDetailVO.setPara(prs.getInt("PARA"));
					}
					if(prs.getString("ABORTION") !=null){
						preAuthDetailVO.setAbortion(prs.getInt("ABORTION"));
					}*/
					if(prs.getString("BENIFIT_TYPES") !=null){
						preAuthDetailVO.setBenefitType(prs.getString("BENIFIT_TYPES"));
					}
					
					if(prs.getString("MAT_COMPLCTON_YN") !=null){
						preAuthDetailVO.setMatsubbenefit(prs.getString("MAT_COMPLCTON_YN"));
					}
					
					if(prs.getString("FRESH_YN") !=null){
					  	preAuthDetailVO.setPreAuthNoStatus(("Y".equals(prs.getString("FRESH_YN"))?"FRESH":"ENHANCEMENT"));
					}
					if(prs.getString("CURRENCY_TYPE") !=null){
					  	preAuthDetailVO.setRequestedAmountCurrency(prs.getString("CURRENCY_TYPE"));
					}
					if(prs.getDate("POLICY_START_DATE") !=null){
					  	preAuthDetailVO.setPolicyStartDate(TTKCommon.convertDateAsString("dd/MM/yyyy", prs.getDate("POLICY_START_DATE")));
					}
					if(prs.getDate("POLICY_END_DATE") !=null){
					  	preAuthDetailVO.setPolicyEndDate(TTKCommon.convertDateAsString("dd/MM/yyyy", prs.getDate("POLICY_END_DATE")));
					}
					preAuthDetailVO.setNationality(TTKCommon.checkNull(prs.getString("NATIONALITY")));
					if(prs.getBigDecimal("SUM_INSURED") !=null){
						preAuthDetailVO.setSumInsured(prs.getBigDecimal("SUM_INSURED"));
					}
					if(prs.getBigDecimal("AVA_SUM_INSURED") !=null){
						preAuthDetailVO.setAvailableSumInsured(prs.getBigDecimal("AVA_SUM_INSURED"));
					}
					if(prs.getBigDecimal("DIS_ALLOWED_AMOUNT") !=null){
						preAuthDetailVO.setDisAllowedAmount(prs.getBigDecimal("DIS_ALLOWED_AMOUNT"));
					}
					if(prs.getString("ENROL_TYPE_ID") !=null){
						preAuthDetailVO.setPolicyType(prs.getString("ENROL_TYPE_ID"));
					}
					if(prs.getString("DUP_PAT") !=null){
						preAuthDetailVO.setDuplicatePreauthAlert(prs.getString("DUP_PAT"));
					}
					if(prs.getString("PRODUCT_NAME") !=null){
						preAuthDetailVO.setProductName(prs.getString("PRODUCT_NAME"));
					}
					if(prs.getString("PAYER_AUTHORITY") !=null){
						preAuthDetailVO.setPayerAuthority(prs.getString("PAYER_AUTHORITY"));
						
					}
						
					if(prs.getString("pat_mem_insp_date") !=null){
					preAuthDetailVO.setPreMemInceptionDt(prs.getString("pat_mem_insp_date"));
					
					
					preAuthDetailVO.setEligibleNetworks(prs.getString("ELIGIBLE_NETWORKS"));
					
					preAuthDetailVO.setAssignedTo(prs.getString("ASSIGNED_TO"));
					preAuthDetailVO.setVipYorN(prs.getString("VIP_YN"));
					preAuthDetailVO.setRelationShip(TTKCommon.checkNull(prs.getString("relationship_desc")));
					preAuthDetailVO.setRelationFlag(TTKCommon.checkNull(prs.getString("COLOUR_YN")));
					preAuthDetailVO.setMemberSpecificRemarks(TTKCommon.checkNull(prs.getString("member_remarks")));
					preAuthDetailVO.setDiagnosis(prs.getString("Oraldiagnosis"));
					preAuthDetailVO.setServices(prs.getString("Oralservices"));
					preAuthDetailVO.setOralApprovedAmount(prs.getBigDecimal("OralaAprovedamount"));
					preAuthDetailVO.setRevisedDiagnosis(prs.getString("OralDiagnosisRevised"));
					preAuthDetailVO.setRevisedServices(prs.getString("OralServicesRevised"));
					preAuthDetailVO.setOralRevisedApprovedAmount(prs.getBigDecimal("OralaAprovedAmountRevised"));
					preAuthDetailVO.setOralORsystemStatus(prs.getString("OralORsystemStatus"));
					preAuthDetailVO.setStrHospitalId(prs.getString("hospital_id"));
					preAuthDetailVO.setProviderSpecificRemarks(prs.getString("PROVIDER_SPECIFIC_REMARKS"));			
					
					preAuthDetailVO.setLatMenstraulaPeriod(prs.getString("LMP_DATE")!=null ? new Date(prs.getTimestamp("LMP_DATE").getTime()):null);
					preAuthDetailVO.setModeofdelivery(prs.getString("DELVRY_MOD_TYPE"));
					preAuthDetailVO.setNatureOfConception(prs.getString("CONCEPTION_TYPE"));
					if(prs.getString("onl_pat_auth_seq_id")!=null)
					preAuthDetailVO.setPartnerPreAuthSeqId(Long.parseLong(prs.getString("onl_pat_auth_seq_id")));
					if(prs.getString("MAT_ALERT_MSG") !=null) preAuthDetailVO.setMaternityAlert(prs.getString("MAT_ALERT_MSG"));
				
					preAuthDetailVO.setProviderSpecificRemarks(prs.getString("PROVIDER_SPECIFIC_REMARKS"));	
					
					preAuthDetailVO.setRequestedAmountcurrencyType(TTKCommon.checkNull(prs.getString("REQ_AMT_CURRENCY_TYPE")));
					preAuthDetailVO.setProcessType(TTKCommon.checkNull(prs.getString("process_type")));
										
					preAuthDetailVO.setCurrencyType(prs.getString("CONVERTED_AMOUNT_CURRENCY_TYPE"));
					preAuthDetailVO.setConversionRate(prs.getString("CONVERSION_RATE"));
					preAuthDetailVO.setConvertedAmount(prs.getBigDecimal("CONVERTED_AMOUNT"));
					preAuthDetailVO.setConvertedFinalApprovedAmount(prs.getBigDecimal("converted_final_approved_amt")); 
					preAuthDetailVO.setTpaReferenceNo(prs.getString("oth_tpa_ref_no"));
					preAuthDetailVO.setOverrideRemarks(prs.getString("Override_Remarks"));
					preAuthDetailVO.setProdPolicySeqId(prs.getInt("prod_policy_seq_id"));

					
					preAuthDetailVO.setPartnerName(prs.getString("partner_name"));
					
					if(prs.getString("onl_pat_auth_seq_id")!=null)
					preAuthDetailVO.setPartnerPreAuthSeqId(prs.getLong("onl_pat_auth_seq_id"));
					
					if(prs.getString("onl_pre_auth_refno")!=null)
					preAuthDetailVO.setPreauthPartnerRefId(prs.getString("onl_pre_auth_refno"));
					
					preAuthDetailVO.setInternalRemarks(prs.getString("internal_remarks"));
					if(prs.getString("DECISION_DATE")!=null)
						preAuthDetailVO.setDecisionDt(TTKCommon.getFormattedDate(TTKCommon.getString(prs.getString("DECISION_DATE"))));
					else
						preAuthDetailVO.setDecisionDt("");
					preAuthDetailVO.setProcessedBy(prs.getString("PROCESSED_BY"));
					if(prs.getString("LINE_OF_TREATMENT")!=null)
					preAuthDetailVO.setFinalRemarks(prs.getString("LINE_OF_TREATMENT"));
					
					String alertMsg=prs.getString("ALT_MSG")==null?"":prs.getString("ALT_MSG"); // alertMsg added by govind
					preAuthDetailVO.setAlertMsg(alertMsg);
				
					
					preAuthDetailVO.setAutoAssignYn(TTKCommon.checkNull(prs.getString("auto_assign_yn")));
					preAuthDetailVO.setAssignDate(TTKCommon.checkNull(prs.getString("assigned_date")));
					preAuthDetailVO.setReason(TTKCommon.checkNull(prs.getString("pat_sub_status_type")));
					if(prs.getString("hold_for_min") !=null){
						preAuthDetailVO.setReasonTime(prs.getLong("hold_for_min"));	
						
					}
				}
					DentalOrthoVO orthoVO	=	new DentalOrthoVO();
					orthoVO.setDentoSeqid(prs.getLong("ORTHO_SEQ_ID"));
					orthoVO.setPreAuthSeqid(prs.getLong("SOURCE_SEQ_ID"));
					orthoVO.setDentoclass1(prs.getString("DENTO_CLASS_I"));
					orthoVO.setDentoclass2(prs.getString("DENTO_CLASS_II"));
					orthoVO.setDentoclass2Text(prs.getString("DENTO_CLASS_II_TEXT"));
					orthoVO.setDentoclass3(prs.getString("DENTO_CLASS_III"));
					orthoVO.setDentoclass3Text(prs.getString("DENTO_CLASS_III_TEXT"));
					orthoVO.setSkeletalClass1(prs.getString("SKELE_CLASS_I"));
					orthoVO.setSkeletalClass2(prs.getString("SKELE_CLASS_II"));
					orthoVO.setSkeletalClass3(prs.getString("SKELE_CLASS_III"));
					orthoVO.setOverJet(prs.getString("OVERJET_MM"));
					orthoVO.setReverseJet(prs.getString("REV_OVERJET_MM"));
					orthoVO.setReverseJetYN(prs.getString("REV_OVERJET_YN"));
					orthoVO.setCrossbiteAntrio(prs.getString("CROSSBITE_ANT"));
					orthoVO.setCrossbitePosterior(prs.getString("CROSSBITE_POST"));
					orthoVO.setCrossbiteRetrucontract(prs.getString("CROSSBITE_BETW"));
					orthoVO.setOpenbiteAntrio(prs.getString("OPENBIT_ANT"));
					orthoVO.setOpenbitePosterior(prs.getString("OPENBIT_POST"));
					orthoVO.setOpenbiteLateral(prs.getString("OPENBIT_LATE"));
					orthoVO.setContactPointDisplacement(prs.getString("CONT_POINT_DISP"));
					orthoVO.setOverBite(prs.getString("OVERBITE"));
					orthoVO.setOverbitePalatalYN(prs.getString("OVERBIT_PATA"));
					orthoVO.setOverbiteGingivalYN(prs.getString("OVERBIT_GING"));
					orthoVO.setHypodontiaQuand1Teeth(prs.getString("HYPO_QUAD1"));
					orthoVO.setHypodontiaQuand2Teeth(prs.getString("HYPO_QUAD2"));
					orthoVO.setHypodontiaQuand3Teeth(prs.getString("HYPO_QUAD3"));
					orthoVO.setHypodontiaQuand4Teeth(prs.getString("HYPO_QUAD4"));
					orthoVO.setImpededTeethEruptionNo(prs.getString("OTHERS_IMPEDED"));
					orthoVO.setImpededTeethNo(prs.getString("OTHERS_IMPACT"));
					orthoVO.setSubmergerdTeethNo(prs.getString("OTHERS_SUBMERG"));
					orthoVO.setSupernumeryTeethNo(prs.getString("OTHERS_SUPERNUM"));
					orthoVO.setRetainedTeethNo(prs.getString("OTHERS_RETAINE"));
					orthoVO.setEctopicTeethNo(prs.getString("OTHERS_ECTOPIC"));
					orthoVO.setCranioFacialNo(prs.getString("OTHERS_CRANIO"));
					orthoVO.setCrossbiteAntriomm(prs.getString("CROSSBITE_ANT_MM"));
					orthoVO.setCrossbitePosteriormm(prs.getString("CROSSBITE_PRST_MM"));
					orthoVO.setCrossbiteRetrucontractmm(prs.getString("CROSSBITE_BETW_MM"));
					orthoVO.setContactPointDisplacementmm(prs.getString("CONT_POINT_DISP_MM"));
					orthoVO.setAestheticComp(prs.getString("AC_MARKS"));
					orthoVO.setIotn(prs.getString("IOTN"));
					preAuthDetailVO.setDentalOrthoVO(orthoVO);
					preAuthDetailVO.setEventReferenceNo(TTKCommon.checkNull(prs.getString("event_no")));
					preAuthDetailVO.setVipMemberUserAccessPermissionFlag(TTKCommon.checkNull(prs.getString("vip_pat_rej")));
					if(prs.getString("assigned_to_user")!=null && !prs.getString("assigned_to_user").equals(""))
						preAuthDetailVO.setAssignUserSeqID(Long.parseLong(TTKCommon.checkNull(prs.getString("assigned_to_user"))));
					else
						preAuthDetailVO.setAssignUserSeqID(new Long(0));
					preAuthDetailVO.setOfficeSeqID(Long.parseLong(TTKCommon.checkNull(prs.getString("TPA_OFFICE_SEQ_ID"))));

					preAuthDetailVO.setClinicianEmail(TTKCommon.checkNull(prs.getString("CLINICIAN_EMAIL")));
					
					preAuthDetailVO.setAppealRemarks(TTKCommon.checkNull(prs.getString("Appeal_Remark")));
					preAuthDetailVO.setInProgressStatus(TTKCommon.checkNull(prs.getString("IN_PROGESS_STATUS")));
					preAuthDetailVO.setAppealDocYN(TTKCommon.checkNull(prs.getString("DOC_YN")));
					preAuthDetailVO.setAppealYN(TTKCommon.checkNull(prs.getString("APPEAL_YN")));
					
					if(prs.getString("APPEAL_DATE")!=null)
						preAuthDetailVO.setAppealReceivedDt(TTKCommon.getFormattedDate(TTKCommon.getString(prs.getString("APPEAL_DATE"))));
					else
						preAuthDetailVO.setAppealReceivedDt("");
					preAuthDetailVO.setMemberExitDate(TTKCommon.checkNull(prs.getString("pat_mem_exit_date")));
                    preAuthDetailVO.setSuspectedYesorNOFlag(TTKCommon.checkNull(prs.getString("fraud_yn")));
                    preAuthDetailVO.setPreauthVerifiedForSuspected(TTKCommon.checkNull(prs.getString("suspect_veri_check")));
                    preAuthDetailVO.setOverrideSuspectFlag(TTKCommon.checkNull(prs.getString("fraud_det_yn")));
					if(prs.getString("CLINICIAN_SPECIALITY") !=null){
						preAuthDetailVO.setClinicianSpeciality(prs.getString("CLINICIAN_SPECIALITY"));
					}			
					if(prs.getString("Approval_Alert_log") !=null){
						preAuthDetailVO.setApprovalAlertLog(prs.getString("Approval_Alert_log"));
					}				
					
					preAuthDetailVO.setAppealCount(TTKCommon.checkNull(prs.getString("appeal_count")));
					preAuthDetailVO.setMophDrugYN(TTKCommon.checkNull(prs.getString("moph_yn")));
					preAuthDetailVO.setRevertFlag(TTKCommon.checkNull(prs.getString("cal_act_yn")));
					preAuthDetailVO.setLmrpAlert(TTKCommon.checkNull(prs.getString("lmrp_alert")));
					preAuthDetailVO.setOverrideMainRemarks(TTKCommon.checkNull(prs.getString("override_main_remarks")));				
					preAuthDetailVO.setOverrideSubRemarks(TTKCommon.checkNull(prs.getString("override_sub_remarks")));
					preAuthDetailVO.setBenefitPopUpAlhalli(TTKCommon.checkNull(prs.getString("alahli_benefit_popup")));
				
					preAuthDetailVO.setDiscountFlag(TTKCommon.checkNull(prs.getString("discount_flag")));		// 07
					preAuthDetailVO.setExternal_pat_yn(TTKCommon.checkNull(prs.getString("external_pat_yn")));
				}//prs if
		if(drs!=null){
		while(drs.next()){
			String diagCode=drs.getString("DIAGNOSYS_CODE")==null?"":drs.getString("DIAGNOSYS_CODE");
			String desc=drs.getString("ICD_DESCRIPTION")==null?"":drs.getString("ICD_DESCRIPTION");
			String primAil=drs.getString("PRIMARY_AILMENT_YN")==null?"":drs.getString("PRIMARY_AILMENT_YN");
			Long diagSeqId=drs.getLong("DIAG_SEQ_ID");
			Long icdCodeSeqId=drs.getLong("ICD_CODE_SEQ_ID");			
			diagnosis.add(new DiagnosisDetailsVO(diagCode,desc,primAil,diagSeqId,icdCodeSeqId));
		   }			
		}
		if(ars!=null){
			int sNo=0;
			while(ars.next()){
				
				sNo++;
				String activityCode=ars.getString("CODE")==null?"":ars.getString("CODE");
				
				String modifiers=ars.getString("MODIFIER")==null?"":ars.getString("MODIFIER");
				
				String unityType=ars.getString("UNIT_TYPE")==null?"":ars.getString("UNIT_TYPE");
				
				Float quantity=ars.getFloat("QUANTITY");
				
				float approvedQuantity=ars.getFloat("APPROVED_QUANTITY");
				
				String startDate=ars.getDate("START_DATE")==null?"":new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(ars.getDate("START_DATE").getTime()));
				
				BigDecimal grossAmt=ars.getBigDecimal("GROSS_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("GROSS_AMOUNT");
				
				BigDecimal discount=ars.getBigDecimal("DISCOUNT_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("DISCOUNT_AMOUNT");
				
				BigDecimal discountGross=ars.getBigDecimal("DISC_GROSS_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("DISC_GROSS_AMOUNT");
				
				BigDecimal patientShare=ars.getBigDecimal("PATIENT_SHARE_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("PATIENT_SHARE_AMOUNT");
				
				BigDecimal netAmt=ars.getBigDecimal("NET_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("NET_AMOUNT");			
				
				BigDecimal approAmt=ars.getBigDecimal("APPROVED_AMT")==null?new BigDecimal(0):ars.getBigDecimal("APPROVED_AMT");
				
				//BigDecimal allowedAmt=ars.getBigDecimal("ALLOWED_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("ALLOWED_AMOUNT");
				BigDecimal disAllowedAmt=ars.getBigDecimal("DIS_ALLOWED_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("DIS_ALLOWED_AMOUNT");
				String denialDecs=ars.getString("DENIAL_DESC")==null?"":ars.getString("DENIAL_DESC");//denial_desc
				
				String denialCodes=ars.getString("DENIAL_CODE")==null?"":ars.getString("DENIAL_CODE");//denial_desc
				
				String activityRemarks=ars.getString("REMARKS")==null?"":ars.getString("REMARKS");
				
				Long activitySeqId=ars.getLong("ACTIVITY_SEQ_ID");
			    
				Long activityDtlSeqId=ars.getLong("ACTIVITY_DTL_SEQ_ID");
			   
				String activityCodeDec=ars.getString("ACTIVITY_DESCRIPTION")==null?"":ars.getString("ACTIVITY_DESCRIPTION");//denial_desc
				
				String internalDesc = ars.getString("internal_desc")==null?"":ars.getString("internal_desc");
			
				String preAuthNo	=	"";
				ActivityDetailsVO activityDetailsVO=new ActivityDetailsVO(sNo, activityCode, activityCodeDec, modifiers, unityType, startDate, activityRemarks, denialCodes,denialDecs,
						quantity, approvedQuantity, activitySeqId, activityDtlSeqId,
						grossAmt,discount, discountGross, patientShare, netAmt, approAmt, disAllowedAmt, preAuthNo);
				activityDetailsVO.setOverrideYN(ars.getString("OVERRIDE_YN"));
				activityDetailsVO.setOverrideRemarks(ars.getString("OVERRIDE_REMARKS"));
				activityDetailsVO.setActivityServiceCode(ars.getString("SERVICE_TYPE"));
				if(ars.getString("act_enhanced_yn")!=null){
				activityDetailsVO.setEnhancedFlag(ars.getString("act_enhanced_yn"));
				}
				
				if("PKD".equals(ars.getString("SERVICE_TYPE"))) {
				activityDetailsVO.setActivityServiceCode("Package");
			activityDetailsVO.setActivityServiceCodeDesc("Package");
			}else{
			activityDetailsVO.setActivityServiceCode(TTKCommon.checkNull(ars.getString("SERVICE_CODE")));				
				activityDetailsVO.setActivityServiceCodeDesc(TTKCommon.checkNull(ars.getString("SERVICE_DESCRIPTION")));	
				}
				activityDetailsVO.setNewPharmacyYN(TTKCommon.checkNull(ars.getString("NEW_PHARMACY_YN")));
				activityDetailsVO.setInternalDesc(internalDesc);
				activityDetailsVO.setMophDrugYN(TTKCommon.checkNull(ars.getString("moph_yn")));
				activities.add(activityDetailsVO);
			}	
		}
			if(shrs!=null){		
				shortfalls=new ArrayList<String[]>();
				while(shrs.next()){	
					String preauthSeqId=shrs.getLong("PAT_AUTH_SEQ_ID")==0?"":shrs.getLong("PAT_AUTH_SEQ_ID")+"";
					String shortFallSeqId=shrs.getLong("SHORTFALL_SEQ_ID")==0?"":shrs.getLong("SHORTFALL_SEQ_ID")+"";
					String shortFallNo=shrs.getString("SHORTFALL_ID")==null?"":shrs.getString("SHORTFALL_ID");
					String shortFallsType=shrs.getString("SHORTFALL_TYPE")==null?"":shrs.getString("SHORTFALL_TYPE");
					String shortFallsStatus=shrs.getString("SRTFLL_STATUS_GENERAL_TYPE_ID")==null?"":shrs.getString("SRTFLL_STATUS_GENERAL_TYPE_ID");
					String sendDate=shrs.getString("SRTFLL_SENT_DATE")==null?"":shrs.getString("SRTFLL_SENT_DATE");
					String recdDate=shrs.getString("SRTFLL_RECEIVED_DATE")==null?"":shrs.getString("SRTFLL_RECEIVED_DATE");
					shortfalls.add(new String[]{preauthSeqId,shortFallSeqId,shortFallNo,shortFallsType,shortFallsStatus,sendDate,recdDate});			
				   }			
				}
			
			if(pcount!=null){
				while(pcount.next()){
					preAuthDetailVO.setPreAuthCount(pcount.getInt("pat_count"));
					preAuthDetailVO.setCfdPreauthCount(pcount.getInt("cfd_tag_cnt"));
				}
			}
			
			if(clmcount!=null){
				while(clmcount.next()){
					preAuthDetailVO.setClmCount(clmcount.getInt("clm_count"));
					preAuthDetailVO.setCfdClaimCount(clmcount.getInt("cfd_tag_cnt"));
				}
			}
			
			preauthAllResult[0]=preAuthDetailVO;
			preauthAllResult[1]=diagnosis;
			preauthAllResult[2]=activities;
			preauthAllResult[3]=shortfalls;
		
			
			
		}//end of 	if(prs != null)
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (prs != null) prs.close();
					if (drs != null) drs.close();
					if (ars != null) ars.close();
					if (shrs != null) shrs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				prs = null;
				drs = null;
				ars = null;
				shrs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		
		return preauthAllResult;
	}//end of getPreAuthDetails(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
	
	public Object[] getPartnerPreAuthDetails(Long preAuthSeqID) throws TTKException{
		
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet prs=null;
		PreAuthDetailVO preAuthDetailVO  = new PreAuthDetailVO();
		Object[] preauthAllResult=new Object[4];
		
		try{
			conn = ResourceManager.getConnection(); 
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPartnerPreAuthorization);

			cStmtObject.setLong(1,preAuthSeqID);			
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			prs = (java.sql.ResultSet)cStmtObject.getObject(2);//preauth Details		
			if(prs != null){
				if(prs.next()){
					
						preAuthDetailVO.setPreauthPartnerRefId(TTKCommon.checkNull(prs.getString("onl_pre_auth_refno")));
						
						/*if(prs.getString("PAYER_AUTHORITY") !=null)preAuthDetailVO.setPayerAuthority(prs.getString("PAYER_AUTHORITY"));
						preAuthDetailVO.setPreAuthNo(TTKCommon.checkNull(prs.getString("PAYER_AUTHORITY")));	*/
						
						if(prs.getLong("ptnr_seq_id") !=0)preAuthDetailVO.setPartnerSeqId(prs.getLong("ptnr_seq_id"));
                  
                   if(prs.getLong("onl_pat_auth_seq_id") !=0)preAuthDetailVO.setPartnerPreAuthSeqId(prs.getLong("onl_pat_auth_seq_id"));
                   
                   if(prs.getString("TPA_ENROLLMENT_ID") != null){
						preAuthDetailVO.setMemberId(prs.getString("TPA_ENROLLMENT_ID"));
					}//end of if(prs.getString("PAT_REQUESTED_AMOUNT") != null)
                   
					if(prs.getString("added_date") != null){
						preAuthDetailVO.setReceiveDate(prs.getString("added_date"));
					}
					
					if(prs.getString("submission_category")!= null){
						preAuthDetailVO.setSubmissionCategory(prs.getString("submission_category"));
					}
					if(prs.getString("EMIRATE_ID") !=null){
						preAuthDetailVO.setEmirateId(prs.getString("EMIRATE_ID"));
					}	
					if(prs.getString("MEM_NAME") != null){
						preAuthDetailVO.setPatientName(prs.getString("MEM_NAME"));
					}//end of if(prs.getString("MEM_NAME") != null)
					if(prs.getInt("MEM_AGE") != 0){
						preAuthDetailVO.setMemberAge(prs.getInt("MEM_AGE"));
					}
					if(prs.getString("gender_general_type_id") !=null){
						preAuthDetailVO.setPatientGender(prs.getString("gender_general_type_id"));
					}
					if(prs.getString("POLICY_NUMBER") !=null){
						preAuthDetailVO.setPolicyNumber(prs.getString("POLICY_NUMBER"));
					}
					if(prs.getString("group_name") !=null){
						preAuthDetailVO.setGroupName(prs.getString("group_name"));
					}
					if(prs.getString("PRODUCT_NAME") !=null)
						preAuthDetailVO.setProductName(prs.getString("PRODUCT_NAME"));
					if(prs.getString("product_cat_type_id") !=null)
						preAuthDetailVO.setProductCatTypeId(prs.getString("product_cat_type_id"));
					if(prs.getString("authority_type") !=null)
						preAuthDetailVO.setAuthorityType(prs.getString("authority_type"));
					if(prs.getString("insured_name") !=null)
						preAuthDetailVO.setInsuredName(prs.getString("insured_name"));
					if(prs.getString("effective_from_date") != null)
						preAuthDetailVO.setEffectiveFromDate(prs.getString("effective_from_date"));
					if(prs.getString("effective_to_date") != null)
							preAuthDetailVO.setEffectiveToDate(prs.getString("effective_to_date"));
					if(prs.getLong("sum_insured") != 0)
						preAuthDetailVO.setSumInsured(new BigDecimal(prs.getLong("sum_insured")));
					if(prs.getLong("available_sum_insured") != 0)
						preAuthDetailVO.setAvailableSumInsured(new BigDecimal(prs.getLong("available_sum_insured")));
					if(prs.getLong("estimated_amount") != 0)
						preAuthDetailVO.setRequestedAmount(new BigDecimal(prs.getLong("estimated_amount")));
					if(prs.getString("currency_id")!=null)
						preAuthDetailVO.setRequestedAmountcurrencyType(prs.getString("currency_id"));
				
					if(prs.getLong("country_id") != 0)
						preAuthDetailVO.setProviderCountry((""+prs.getLong("country_id")).trim());
					if(prs.getString("description") != null){
							preAuthDetailVO.setDescription(prs.getString("description"));
						}
						if(prs.getString("VIP") != null){
								preAuthDetailVO.setVipYorN(prs.getString("VIP"));
						}
							if(prs.getString("policy_issue_date") !=null){
								  preAuthDetailVO.setPolicyIssueDate(prs.getString("policy_issue_date"));
								}
								if(prs.getString("HOSP_NAME") !=null){
									preAuthDetailVO.setProviderName(prs.getString("HOSP_NAME"));
								}
								if(prs.getString("country_name") !=null){
									preAuthDetailVO.setCountryName(prs.getString("country_name"));
								}	
								if(prs.getString("BENIFIT_TYPE") !=null){
									preAuthDetailVO.setBenefitType(prs.getString("BENIFIT_TYPE"));
								}
								if(prs.getString("HOSPITALIZATION_DATE") != null){
									preAuthDetailVO.setHospitalzationDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())));
									preAuthDetailVO.setAdmissionTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ")[1]:"");
									preAuthDetailVO.setAdmissionDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("HOSPITALIZATION_DATE").getTime())).split(" ")[2]:"");
								}
								if(prs.getString("DISCHARGE_DATE") != null){
									preAuthDetailVO.setDischargeDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())));
									preAuthDetailVO.setDischargeTime(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[1]:"");
									preAuthDetailVO.setDischargeDay(TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(prs.getTimestamp("DISCHARGE_DATE").getTime())).split(" ")[2]:"");
								}	
								
								if(prs.getString("partner_name") != null){
									preAuthDetailVO.setPartnerName(prs.getString("partner_name"));
								}
								
								if(prs.getString("preauth_number")!=null){
									preAuthDetailVO.setPreAuthSeqID(prs.getLong("preauth_number"));
								}
								
								if(prs.getString("PRE_AUTH_CAT")!=null){
									preAuthDetailVO.setProcessType(prs.getString("PRE_AUTH_CAT"));
								}
								
								if(prs.getString("MEMBER_SEQ_ID")!=null){
									preAuthDetailVO.setMemberSeqID(prs.getLong("MEMBER_SEQ_ID"));
								}
								
								if(prs.getString("INS_SEQ_ID")!=null){
									preAuthDetailVO.setInsSeqId(prs.getLong("INS_SEQ_ID"));
								}
								
								if(prs.getString("POLICY_SEQ_ID")!=null){
									preAuthDetailVO.setPolicySeqId(prs.getLong("POLICY_SEQ_ID"));
								}	
				
								if(prs.getString("ENCOUNTER_START_TYPE")!=null){
									preAuthDetailVO.setEncounterStartTypeId(prs.getString("ENCOUNTER_START_TYPE"));
								}
								
								if(prs.getString("LINE_OF_TREATMENT")!=null){
									preAuthDetailVO.setFinalRemarks(prs.getString("LINE_OF_TREATMENT"));
								}
					}
			preauthAllResult[0]=preAuthDetailVO;
		}//end of 	if(prs != null)
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (prs != null) prs.close();
					
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				prs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		
		return preauthAllResult;
	}//end of getPreAuthDetails(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
	
	
	/**
     * This method returns the ShortfallVO, which contains all the Pre-Authorization/Claims Shortfall Details
     * @param alShortfallList object contains SeqID,Preauth/ClaimSeqID to fetch the Pre-Authorization/Claims Shortfall Details
     * @return ShortfallVO object which contains all the Pre-Authorization Shortfall/Claims Details
     * @exception throws TTKException
     */
    public ShortfallVO getShortfallDetail(ArrayList alShortfallList) throws TTKException {
    	
    	
        ShortfallVO shortfallVO = null;
        Document doc = null;
        XMLType xml = null;
        Connection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strShortfallDetails);
            stmt.setLong(1,(Long)alShortfallList.get(0));

            if(alShortfallList.get(1) != null){
                stmt.setLong(2,(Long)alShortfallList.get(1));
            }//end of if(alShortfallList.get(1) != null)
            else{
                stmt.setString(2,null);
            }//end of else

            if(alShortfallList.get(2) != null){
                stmt.setLong(3,(Long)alShortfallList.get(2));
            }//end of if(alShortfallList.get(2) != null)
            else{
                stmt.setString(3,null);
            }//end of else
            if(alShortfallList.get(3)!=null)
            stmt.setLong(4,(Long)alShortfallList.get(3));
            else
            	stmt.setString(4,null);
            stmt.registerOutParameter(5,OracleTypes.CURSOR);
            
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(5);
            
            if(rs != null){
                while(rs.next()){
                    shortfallVO = new ShortfallVO();

                    if(rs.getString("SHORTFALL_SEQ_ID") != null){
                        shortfallVO.setShortfallSeqID(new Long(rs.getLong("SHORTFALL_SEQ_ID")));
                    }//end of if(rs.getString("SHORTFALL_SEQ_ID") != null)
                    shortfallVO.setShortfallNo(TTKCommon.checkNull(rs.getString("SHORTFALL_ID")));

                    if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null){
                        shortfallVO.setPreAuthSeqID(new Long(rs.getLong("PAT_GEN_DETAIL_SEQ_ID")));
                    }//end of if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null)

                    shortfallVO.setShortfallTypeID(TTKCommon.checkNull(rs.getString("SRTFLL_GENERAL_TYPE_ID")));
                    shortfallVO.setStatusTypeID(TTKCommon.checkNull(rs.getString("SRTFLL_STATUS_GENERAL_TYPE_ID")));

                    /*if(rs.getString("SRTFLL_SENT_DATE") != null){
                        shortfallVO.setSentDate(new java.util.Date(rs.getTimestamp("SRTFLL_SENT_DATE").getTime()));
                        shortfallVO.setSentTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_SENT_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_SENT_DATE").getTime())).split(" ")[1]:"");
                        shortfallVO.setSentDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_SENT_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_SENT_DATE").getTime())).split(" ")[2]:"");
                    }//end of if(rs.getString("SRTFLL_SENT_DATE") != null)
*/            
                   
                    shortfallVO.setShortfallDate(TTKCommon.checkNull(rs.getString("m_correspondence_date")));
                    if(rs.getString("SRTFLL_RECEIVED_DATE") != null){
                        shortfallVO.setReceivedDate(new java.util.Date(rs.getTimestamp("SRTFLL_RECEIVED_DATE").getTime()));
                    }//end of if(rs.getString("SRTFLL_RECEIVED_DATE") != null)
                   
                    if(rs.getString("SRTFLL_RECEIVED_DATE") != null){
                        shortfallVO.setReceivedDate(new Date(rs.getTimestamp("SRTFLL_RECEIVED_DATE").getTime()));
                        shortfallVO.setReceivedTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_RECEIVED_DATE").getTime())).split(" ")[1]:"");
                        shortfallVO.setReceivedDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("SRTFLL_RECEIVED_DATE").getTime())).split(" ")[2]:"");
                    }//end of if(rs.getString("SRTFLL_RECEIVED_DATE") != null)
                    shortfallVO.setShortfallDate(TTKCommon.checkNull(rs.getString("SRTFLL_RECEIVED_DATE")));
                    shortfallVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
                    shortfallVO.setReasonTypeID(TTKCommon.checkNull(rs.getString("SRTFLL_REASON_GENERAL_TYPE_ID")));
                    //shortfallVO.setDMSRefID(TTKCommon.checkNull(rs.getString("PRE_AUTH_DMS_REFERENCE_ID")));
                    
                    /*if(rs.getString("SRTFLL_REMINDER_LOG_SEQ_ID") != null){
                    	shortfallVO.setReminderLogSeqID(new Long(rs.getLong("SRTFLL_REMINDER_LOG_SEQ_ID")));
                    }//end of if(rs.getString("SRTFLL_REMINDER_LOG_SEQ_ID") != null)
                    */
                    if(rs.getString("REMINDER_COUNT") != null){
                    	shortfallVO.setCorrespondenceCount(new Integer(rs.getInt("REMINDER_COUNT")));
                    }//end of if(rs.getString("REMINDER_COUNT") != null)
                    
                    if(rs.getString("CORRESPONDENCE_DATE") != null){
                        shortfallVO.setCorrespondenceDate(new Date(rs.getTimestamp("CORRESPONDENCE_DATE").getTime()));
                        shortfallVO.setCorrespondenceTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("CORRESPONDENCE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("CORRESPONDENCE_DATE").getTime())).split(" ")[1]:"");
                        shortfallVO.setCorrespondenceDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("CORRESPONDENCE_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("CORRESPONDENCE_DATE").getTime())).split(" ")[2]:"");
                    }//end of if(rs.getString("CORRESPONDENCE_DATE") != null)

                    if(rs.getOPAQUE("SHORTFALL_QUESTIONS") != null) {
                        xml = XMLType.createXML(rs.getOPAQUE("SHORTFALL_QUESTIONS"));
                        DOMReader domReader = new DOMReader();
                        doc = xml != null ? domReader.read(xml.getDOM()):null;
                        shortfallVO.setShortfallQuestions(doc);
                  }// End of if(rs.getOPAQUE("SHORTFALL_QUESTIONS")

                    shortfallVO.setEditYN(TTKCommon.checkNull(rs.getString("EDIT_YN")));
                    shortfallVO.setShortfallDesc(TTKCommon.checkNull(rs.getString("SHORTFALL_TYPE")));
                    //added for Mail-SMS Template for Cigna
                    //shortfallVO.setCignaYN(TTKCommon.checkNull(rs.getString("CIGNA_YN")));
                    //added new Implementation for Member claim check
                    //shortfallVO.setMemberClaimYN(TTKCommon.checkNull(rs.getString("MEM_CLM")));
                    //end
					// Shortfall CR KOC1179
                    shortfallVO.setShortfallTemplateType(TTKCommon.checkNull(rs.getString("SHORTFALL_RAISED_FOR")));
                    shortfallVO.setShortfallUnderClause(rs.getLong("CLAUSE_SEQ_ID"));
                    shortfallVO.setClause(TTKCommon.checkNull(rs.getString("ADD_CLAUSE_NUMBER")));
                    shortfallVO.setCurrentShortfallStatus(TTKCommon.checkNull(rs.getString("SHORTFAL_EMAIL_STATUS")));
                    shortfallVO.setPreAuthNo(rs.getString("PRE_AUTH_NUMBER"));
                    shortfallVO.setPreAuthSeqID(rs.getLong("PAT_AUTH_SEQ_ID"));
                    shortfallVO.setEnrollmentID(rs.getString("MEM_ID"));
                    shortfallVO.setClaimantName(rs.getString("MEM_NAME"));
                    shortfallVO.setRecievedStatus(rs.getString("RECIEVED_YN"));
                    shortfallVO.setProviderRespondedYN(rs.getString("PROVIDER_RESPONDED_YN"));
                    
                    // End Shortfall CR KOC1179
                }//end of while(rs.next())
            }//end of if(rs != null)
            return shortfallVO;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "preauth");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getShortfallDetail()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (stmt != null) stmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PreAuthDAOImpl getShortfallDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getShortfallDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				stmt = null;
				conn = null;
			}//end of finally
		}//end of finally
      }//end of getShortfallDetail(ArrayList alShortfallList)
    /**
     * This method saves the Pre-Authorization Shortfall information
     * @param shortfallVO ShortfallVO contains Pre-Authorization Shortfall information
     * @param strSelectionType
     * @return long value which contains Shortfall Seq ID
     * @exception throws TTKException
     */
    public long saveShortfall(ShortfallVO shortfallVO,String strSelectionType) throws TTKException {
    	
    	
        long lngShortfallSeqID = 0;
        XMLType shortfallXML = null;
        Connection conn = null;
        OracleCallableStatement stmt = null;
        try{
            conn = ResourceManager.getConnection();
            stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strSaveShortfall);

            if(shortfallVO.getShortfallSeqID() != null){
                stmt.setLong(1,shortfallVO.getShortfallSeqID());  
            }//end of if(shortfallVO.getShortfallSeqID() != null)
            else{
                stmt.setLong(1,0);
            }//end of else

            if(shortfallVO.getPreAuthSeqID() != null && shortfallVO.getPreAuthSeqID() != 0){
                stmt.setLong(2,shortfallVO.getPreAuthSeqID());
            }//end of if(shortfallVO.getPreAuthSeqID() != null)
            else{
                stmt.setString(2,null);
            }//end of else

            if(shortfallVO.getClaimSeqID() != null && shortfallVO.getClaimSeqID() != 0){
                stmt.setLong(3,shortfallVO.getClaimSeqID());
            }//end of if(shortfallVO.getClaimSeqID() != null)
            else{
                stmt.setString(3,null);
            }//end of else

            stmt.setString(4,shortfallVO.getShortfallTypeID());
            stmt.setString(5,shortfallVO.getStatusTypeID());
            stmt.setString(6,shortfallVO.getReasonTypeID());
            stmt.setString(7,shortfallVO.getRemarks());
            if(shortfallVO.getReceivedDate() != null){
                stmt.setTimestamp(8,new Timestamp(TTKCommon.getOracleDateWithTime(shortfallVO.getPATReceivedDate(),shortfallVO.getReceivedTime(),shortfallVO.getReceivedDay()).getTime()));
            }//end of if(shortfallVO.getReceivedDate() != null)
            else{
                stmt.setTimestamp(8, null);
            }//end of else
            if(shortfallVO.getShortfallQuestions() != null){
                shortfallXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), shortfallVO.getShortfallQuestions().asXML());
            }//end of if(shortfallVO.getShortfallQuestions() != null)

            stmt.setObject(9,shortfallXML);
            
            if(shortfallVO.getCorrespondenceDate() != null){
                stmt.setTimestamp(10,new Timestamp(TTKCommon.getOracleDateWithTime(shortfallVO.getPATCorrespondenceDate(),shortfallVO.getCorrespondenceTime(),shortfallVO.getCorrespondenceDay()).getTime()));
            }//end of if(shortfallVO.getCorrespondenceDate() != null)
            else{
                stmt.setTimestamp(10, null);
            }//end of else
            
            stmt.setString(11,shortfallVO.getCorrespondenceYN());
            
            /*if(shortfallVO.getReminderLogSeqID() != null){
            	stmt.setLong(12,shortfallVO.getReminderLogSeqID());
            }//end of if(shortfallVO.getReminderLogSeqID() != null)
            else{
            	stmt.setString(12,null);
            }//end of else
*/            
            stmt.setString(12,strSelectionType);
			
           if(shortfallVO.getActiveLink().equals("Claims"))
            {
            	if(shortfallVO.getShortfallTemplateType().equals("") || shortfallVO.getShortfallTemplateType()==null)
            	{
            		stmt.setString(13,"OLD");
            	}
            	else
            	{
            	   stmt.setString(13,shortfallVO.getShortfallTemplateType());
            	} 
            	/*if(shortfallVO.getShortfallUnderClause() != null && shortfallVO.getShortfallUnderClause().longValue()!=0){
            		stmt.setLong(15,shortfallVO.getShortfallUnderClause());
            	}//end of if(shortfallVO.getShortfallSeqID() != null)
            	else{
            		stmt.setString(15,null);
            	}//end of else
            stmt.setString(16,shortfallVO.getClause());*/
           }
            else if(shortfallVO.getActiveLink().equals("Pre-Authorization"))
            {
                stmt.setString(13,"OLD");
               // stmt.setString(15,null);
                //.setString(16,"");
            }
			
            stmt.setLong(14,shortfallVO.getUpdatedBy());
            stmt.setString(15,shortfallVO.getRecievedStatus());
            stmt.registerOutParameter(1,Types.BIGINT);
            stmt.registerOutParameter(16,Types.INTEGER);
            stmt.execute();
            lngShortfallSeqID = stmt.getLong(1);
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "preauth");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "preauth");
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
        			log.error("Error while closing the Statement in PreAuthDAOImpl saveShortfall()",sqlExp);
        			throw new TTKException(sqlExp, "preauth");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PreAuthDAOImpl saveShortfall()",sqlExp);
        				throw new TTKException(sqlExp, "preauth");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "preauth");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		stmt = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return lngShortfallSeqID;
    }//end of saveShortfall(ShortfallVO shortfallVO,String strSelectionType)
    
	public Object[] saveObservationDetails(ObservationDetailsVO observationDetailsVO) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet ors = null;
		ArrayList<String[]> observations=new ArrayList<String[]>();
		Object[] results=new Object[3];
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveObservations);
           if(observationDetailsVO.getObservSeqId()==null)cStmtObject.setLong(1,0);
           else cStmtObject.setLong(1,observationDetailsVO.getObservSeqId());
			cStmtObject.setLong(2,observationDetailsVO.getActivityDtlSeqId());
			cStmtObject.setString(3,observationDetailsVO.getAuthType());
			if("PAT".equals(observationDetailsVO.getAuthType())) cStmtObject.setLong(4,observationDetailsVO.getPreAuthSeqID());
			else if("CLM".equals(observationDetailsVO.getAuthType())) cStmtObject.setLong(4,observationDetailsVO.getClaimSeqID());
			cStmtObject.setString(5,observationDetailsVO.getObservType());
			cStmtObject.setString(6,observationDetailsVO.getObservCode());
			cStmtObject.setString(7,observationDetailsVO.getObservValue());
			cStmtObject.setString(8,observationDetailsVO.getObservValueType());
			cStmtObject.setLong(9,observationDetailsVO.getAddedBy());
			cStmtObject.setString(10,observationDetailsVO.getObservRemarks());
			cStmtObject.registerOutParameter(1,Types.BIGINT);
			cStmtObject.registerOutParameter(11,Types.BIGINT);
			cStmtObject.execute();
			results[0]=cStmtObject.getLong(1);
			results[1]=cStmtObject.getLong(11);
			
			
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetAllObservations);
			cStmtObject.setLong(1,observationDetailsVO.getActivityDtlSeqId());
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			ors = (java.sql.ResultSet)cStmtObject.getObject(2);//observ Details
			
		
		if(ors!=null){	
			while(ors.next()){	
				String observSeqId=ors.getLong("OBSERVATION_SEQ_ID")==0?"":ors.getLong("OBSERVATION_SEQ_ID")+"";
			    String activityDtlSeqId=ors.getLong("ACTIVITY_DTL_SEQ_ID")==0?"":ors.getLong("ACTIVITY_DTL_SEQ_ID")+"";
			    String observTypeDesc=ors.getString("TYPE_DESC")==null?"":ors.getString("TYPE_DESC");
			    String observCodeDesc=ors.getString("CODE_DESC")==null?"":ors.getString("CODE_DESC");
			    String observValue=ors.getString("VALUE")==null?"":ors.getString("VALUE");
			    String observValueTypeDesc=ors.getString("VALUE_TYPE_DESC")==null?"":ors.getString("VALUE_TYPE_DESC");
			    String observRemarks=ors.getString("REMARKS")==null?"":ors.getString("REMARKS");
			    observations.add(new String[]{observSeqId,activityDtlSeqId,observTypeDesc,observCodeDesc,observValue,observValueTypeDesc,observRemarks});			
			}			
			}
		results[2]=observations;		
			return results;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (ors != null) ors.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl saveObservationDetails()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl saveObservationDetails()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl saveObservationDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				ors = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of saveObservationDetails(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
	
	public ObservationDetailsVO getObservDetail(Long observSeqId) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet ors = null;
		ObservationDetailsVO observationDetailsVO=new ObservationDetailsVO();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetObservation);
            cStmtObject.setLong(1,observSeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			ors = (java.sql.ResultSet)cStmtObject.getObject(2);//observ Details
			
		
		if(ors!=null){	
			while(ors.next()){
				observationDetailsVO.setObservSeqId(ors.getLong("OBSERVATION_SEQ_ID"));
				observationDetailsVO.setActivityDtlSeqId(ors.getLong("ACTIVITY_DTL_SEQ_ID"));
				observationDetailsVO.setObservType(ors.getString("TYPE"));
				observationDetailsVO.setObservCode(ors.getString("CODE"));
				observationDetailsVO.setObservCodeDesc(ors.getString("CODE_DESC"));
				observationDetailsVO.setObservValue(ors.getString("VALUE"));
				observationDetailsVO.setObservValueType(ors.getString("VALUE_TYPE_ID"));
				observationDetailsVO.setObservValueTypeDesc(ors.getString("VALUE_TYPE_DESC"));
				observationDetailsVO.setObservRemarks(ors.getString("REMARKS"));
			}			
			}
		
			return observationDetailsVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (ors != null) ors.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				ors = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getObservDetails(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
	public Object[] getObservTypeDetails(String observType) throws TTKException{
		Connection conn = null;
		PreparedStatement statement=null;
		ResultSet resultSet = null;
		HashMap<String,String> observCodes=new HashMap<String,String>();
		HashMap<String,String> observValueTypes=new HashMap<String,String>();
		Object[] result=new Object[2];
		try{
		     observType=(observType==null)?"":observType.trim();
		     conn = ResourceManager.getConnection();
		     statement=conn.prepareStatement("SELECT S.OBSERVATION_CODE_ID,S.OBSERVATION_CODE FROM APP.TPA_OBSERVATION_VALUE_CODES S where s.observation_type_id='"+observType+"'");
		     resultSet=statement.executeQuery();
		    if(resultSet!=null){
		     while(resultSet.next())
		    	 observCodes.put(resultSet.getString(1),resultSet.getString(2));		    
		    }
		     statement=conn.prepareStatement("select C.OBS_VALUE_TYPE_CODE_ID,C.VALUE_TYPE from app.TPA_OBSER_VALUE_TYPE_CODES C WHERE C.OBSERVATION_TYPE_ID='"+observType+"'");
		     resultSet=statement.executeQuery();
			    if(resultSet!=null){
			     while(resultSet.next())
			    	 observValueTypes.put(resultSet.getString(1),resultSet.getString(2));		    
			    }
			    result[0]=observCodes;
			    result[1]=observValueTypes;
			return result;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (resultSet != null) resultSet.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getObservTypeDetails()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (statement != null) statement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PreAuthDAOImpl getObservTypeDetails()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getObservTypeDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				resultSet = null;
				statement = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getObservTypeDetails(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
	
	
		public long deleteObservDetails(Long peauthSeqId,String listOfobsvrSeqIds,String mode) throws TTKException{
			Connection conn = null;
			CallableStatement cStmtObject=null;
			long updatedRows;
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteObservations);
	            cStmtObject.setString(1,listOfobsvrSeqIds);
	            cStmtObject.setLong(2,peauthSeqId);
	            cStmtObject.setString(3,mode);
				cStmtObject.registerOutParameter(4,Types.BIGINT);
				cStmtObject.execute();
				updatedRows = cStmtObject.getLong(4);
				
				return updatedRows;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PreAuthDAOImpl deleteObservDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PreAuthDAOImpl deleteObservDetails()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)						
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{					
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of deleteObservDetails(Long peauthSeqId,String listOfobsvrSeqIds,String mode)
		
		public ArrayList<String[]> getAllObservDetails(Long activityDtlSeqId1) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet ors = null;
		ArrayList<String[]> observations=new ArrayList<String[]>();
		
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetAllObservations);
			cStmtObject.setLong(1,activityDtlSeqId1);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			ors = (java.sql.ResultSet)cStmtObject.getObject(2);//observ Details
			
		
		if(ors!=null){	
			while(ors.next()){	
				String observSeqId=ors.getLong("OBSERVATION_SEQ_ID")==0?"":ors.getLong("OBSERVATION_SEQ_ID")+"";
			    String activityDtlSeqId=ors.getLong("ACTIVITY_DTL_SEQ_ID")==0?"":ors.getLong("ACTIVITY_DTL_SEQ_ID")+"";
			    String observTypeDesc=ors.getString("TYPE_DESC")==null?"":ors.getString("TYPE_DESC");
			    String observCodeDesc=ors.getString("CODE_DESC")==null?"":ors.getString("CODE_DESC");
			    String observValue=ors.getString("VALUE")==null?"":ors.getString("VALUE");
			    String observValueTypeDesc=ors.getString("VALUE_TYPE_DESC")==null?"":ors.getString("VALUE_TYPE_DESC");
			    String observRemarks=ors.getString("REMARKS")==null?"":ors.getString("REMARKS");
			    observations.add(new String[]{observSeqId,activityDtlSeqId,observTypeDesc,observCodeDesc,observValue,observValueTypeDesc,observRemarks});			
			}			
			}
			
			return observations;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (ors != null) ors.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				ors = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getAllObservDetails(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)
		
		
	public ArrayList<String[]> getDiagnosisDetails(Long preAuthSeqId) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;		
		ArrayList<String[]> listOfDiagnosis=new ArrayList<String[]>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDiagnosisDetails);
			cStmtObject.setLong(1,preAuthSeqId);
			cStmtObject.setString(2,"PAT");	
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			if(rs != null){
				while(rs.next()){
				String icdCode=rs.getString("DIAGNOSYS_CODE")==null?"":rs.getString("DIAGNOSYS_CODE");
				String icdDesc=rs.getString("ICD_DESCRIPTION")==null?"":rs.getString("ICD_DESCRIPTION");
				String prime=rs.getString("PRIMARY_AILMENT_YN")==null?"":rs.getString("PRIMARY_AILMENT_YN");
				String icdSeqId=rs.getLong("ICD_CODE_SEQ_ID")==0?"":rs.getLong("ICD_CODE_SEQ_ID")+"";
				listOfDiagnosis.add(new String[]{icdCode,icdDesc,prime,icdSeqId});
				}				
			}
			return listOfDiagnosis;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getDiagnosisDetails()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getDiagnosisDetails()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getDiagnosisDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getDiagnosisDetails(long lngPreAuthSeqID)
public DiagnosisDetailsVO getIcdCodeDetails(String icdCode,long seqId,String authType) throws TTKException{
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			DiagnosisDetailsVO diagnosisDetailsVO=new DiagnosisDetailsVO();
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetIcdDetails);
				cStmtObject.setString(1,icdCode);
				cStmtObject.setLong(2,seqId);
				cStmtObject.setString(3,authType);
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(4);	
				if(rs != null){
					while(rs.next()){
						diagnosisDetailsVO.setIcdCodeSeqId(rs.getLong("ICD_CODE_SEQ_ID"));
						diagnosisDetailsVO.setAilmentDescription(rs.getString("ICD_DESCRIPTION"));
						diagnosisDetailsVO.setPrimaryAilment(rs.getString("PRIMARY"));
					}				
				}
				return diagnosisDetailsVO;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Resultset in PreAuthDAOImpl getIcdCodeDetails()",sqlExp);
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
							log.error("Error while closing the Statement in PreAuthDAOImpl getIcdCodeDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PreAuthDAOImpl getIcdCodeDetails()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of getDiagnosisDetails(String icdCode,long lngPreAuthSeqID)
	public DiagnosisDetailsVO getDiagnosis(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		DiagnosisDetailsVO diagnosisDetailsVO2=new DiagnosisDetailsVO();
		ResultSet rs = null;		
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDiagnosis);
			cStmtObject.setLong(1,diagnosisDetailsVO.getPreAuthSeqID());
			cStmtObject.setLong(2,diagnosisDetailsVO.getDiagSeqId());	
			cStmtObject.setString(3,"PAT");	
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(4);
			if(rs != null){
				while(rs.next()){
				String icdCode=rs.getString("DIAGNOSYS_CODE");
				String icdDesc=rs.getString("ICD_DESCRIPTION");
				String prime=rs.getString("PRIMARY_AILMENT_YN");
				Long icdSeqId=rs.getLong("ICD_CODE_SEQ_ID");
				Long diagSeqId=rs.getLong("diag_seq_id");
				diagnosisDetailsVO2=new DiagnosisDetailsVO(prime, icdDesc, icdCode, icdSeqId, null,null, null, diagSeqId);
				}				
			}
			return diagnosisDetailsVO2;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getDiagnosis()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getDiagnosis()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getDiagnosis()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getDiagnosis(long lngPreAuthSeqID,long lngUserSeqID,String strSelectionType)

	
	/**
	 * This method saves the Pre-Authorization information
	 * @param preAuthDetailVO PreAuthDetailVO contains Pre-Authorization information
	 * @param strSelectionType String object which contains Selection Type - 'PAT' or 'ICO'
	 * @return Object[] the values,of  MEMBER_SEQ_ID , PAT_GEN_DETAIL_SEQ_ID and PAT_ENROLL_DETAIL_SEQ_ID
	 * @exception throws TTKException 
	 */
	public String[] addPreauthDetails(PreAuthDetailVO preAuthDetailVO,String uploadedBy) throws TTKException{
		String[] objArrayResult = new String[3];
		long lngMemberSeqID = 0;
		long lngPreAuthSeqID = 0;
		long lngPATEnrollDtlSeqID = 0;
		AdditionalDetailVO additionalDetailVO = null;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			additionalDetailVO = preAuthDetailVO.getAdditionalDetailVO();
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddPreAuthorizationDetails);
			cStmtObject.registerOutParameter(1,Types.BIGINT);//cStmtObject.setString(,"");
			cStmtObject.setString(2,"");
			cStmtObject.setString(3,"");
			cStmtObject.setString(4,"");
			cStmtObject.setString(5,"");
			
			if(preAuthDetailVO.getEnrollDtlSeqID() != null){
				cStmtObject.setLong(PAT_ENROLL_DETAIL_SEQ_ID,preAuthDetailVO.getEnrollDtlSeqID());
			}//end of if(preAuthDetailVO.getEnrollDtlSeqID() != null)
			else{
				cStmtObject.setLong(PAT_ENROLL_DETAIL_SEQ_ID,0);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getMemberSeqID() != null){
				cStmtObject.setLong(MEMBER_SEQ_ID,preAuthDetailVO.getClaimantVO().getMemberSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getMemberSeqID() != null)
			else{
				cStmtObject.setLong(MEMBER_SEQ_ID,0);
			}//end of else

			cStmtObject.setString(TPA_ENROLLMENT_ID,preAuthDetailVO.getClaimantVO().getEnrollmentID());
			cStmtObject.setString(POLICY_NUMBER,preAuthDetailVO.getClaimantVO().getPolicyNbr());

			if(preAuthDetailVO.getClaimantVO().getPolicySeqID() != null){
				cStmtObject.setLong(POLICY_SEQ_ID,preAuthDetailVO.getClaimantVO().getPolicySeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getPolicySeqID() != null)
			else{
				cStmtObject.setString(POLICY_SEQ_ID,null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getInsSeqID() != null){
				cStmtObject.setLong(INS_SEQ_ID,preAuthDetailVO.getClaimantVO().getInsSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getInsSeqID() != null)
			else{
				cStmtObject.setString(INS_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(GENDER_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getGenderTypeID());
			cStmtObject.setString(CLAIMANT_NAME,preAuthDetailVO.getClaimantVO().getName());

			if(preAuthDetailVO.getClaimantVO().getAge() != null){
				cStmtObject.setInt(MEM_AGE,preAuthDetailVO.getClaimantVO().getAge());
			}//end of if(preAuthDetailVO.getClaimantVO().getAge() != null)
			else{
				cStmtObject.setString(MEM_AGE,null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getDateOfInception() != null){
				cStmtObject.setTimestamp(DATE_OF_INCEPTION,new Timestamp(preAuthDetailVO.getClaimantVO().getDateOfInception().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getDateOfInception() != null)
			else{
				cStmtObject.setTimestamp(DATE_OF_INCEPTION, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getDateOfExit() != null){
				cStmtObject.setTimestamp(DATE_OF_EXIT,new Timestamp(preAuthDetailVO.getClaimantVO().getDateOfExit().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getDateOfExit() != null)
			else{
				cStmtObject.setTimestamp(DATE_OF_EXIT, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getTotalSumInsured() != null){
				cStmtObject.setBigDecimal(MEM_TOTAL_SUM_INSURED,preAuthDetailVO.getClaimantVO().getTotalSumInsured());
			}//end of if(preAuthDetailVO.getClaimantVO().getTotalSumInsured() != null)
			else{
				cStmtObject.setString(MEM_TOTAL_SUM_INSURED,null);
			}//end of else

			cStmtObject.setString(CATEGORY_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getCategoryTypeID());
			cStmtObject.setString(INSURED_NAME,preAuthDetailVO.getClaimantVO().getPolicyHolderName());
			cStmtObject.setString(PHONE_1,preAuthDetailVO.getClaimantVO().getPhone());

			if(preAuthDetailVO.getClaimantVO().getStartDate() != null){
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_FROM,new Timestamp(preAuthDetailVO.getClaimantVO().getStartDate().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getStartDate() != null)
			else{
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_FROM, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getEndDate() != null){
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_TO,new Timestamp(preAuthDetailVO.getClaimantVO().getEndDate().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getEndDate() != null)
			else{
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_TO, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getProductSeqID() != null){
				cStmtObject.setLong(PRODUCT_SEQ_ID,preAuthDetailVO.getClaimantVO().getProductSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getProductSeqID() != null)
			else{
				cStmtObject.setString(PRODUCT_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(INS_STATUS_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getTermStatusID());
			cStmtObject.setString(ENROL_TYPE_ID,preAuthDetailVO.getClaimantVO().getPolicyTypeID());
			cStmtObject.setString(POLICY_SUB_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getPolicySubTypeID());

			if(preAuthDetailVO.getClaimantVO().getGroupRegnSeqID() != null){
				cStmtObject.setLong(GROUP_REG_SEQ_ID,preAuthDetailVO.getClaimantVO().getGroupRegnSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getGroupRegSeqID() != null)
			else{
				cStmtObject.setString(GROUP_REG_SEQ_ID,null);
			}//end of else

			if(preAuthDetailVO.getAdmissionDate() != null){

				cStmtObject.setTimestamp(DATE_OF_HOSPITALIZATION,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getHospitalAdmissionDate(),preAuthDetailVO.getAdmissionTime(),preAuthDetailVO.getAdmissionDay()).getTime()));

			}//end of if(preAuthDetailVO.getAdmissionDate() != null)
			else{
				cStmtObject.setTimestamp(DATE_OF_HOSPITALIZATION, null);
			}//end of else

			if(preAuthDetailVO.getOfficeSeqID() != null){
				cStmtObject.setLong(TPA_OFFICE_SEQ_ID,preAuthDetailVO.getOfficeSeqID());
			}//end of if(preAuthDetailVO.getOfficeSeqID() != null)
			else{
				cStmtObject.setString(TPA_OFFICE_SEQ_ID,null);
			}//end of else

			if(preAuthDetailVO.getPreAuthHospitalVO().getHospSeqId() != null){
				cStmtObject.setLong(HOSP_SEQ_ID,preAuthDetailVO.getPreAuthHospitalVO().getHospSeqId());
			}//end of if(preAuthDetailVO.getPreAuthHospitalVO().getHospSeqId() != null)
			else{
				cStmtObject.setString(HOSP_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(PAT_STATUS_GENERAL_TYPE_ID,preAuthDetailVO.getStatusTypeID());

			if(preAuthDetailVO.getPreAuthSeqID()!=null)
			{
				cStmtObject.setLong(PAT_GEN_DETAIL_SEQ_ID,preAuthDetailVO.getPreAuthSeqID());
			}else
			{
				cStmtObject.setLong(PAT_GEN_DETAIL_SEQ_ID,0);
			}

			cStmtObject.setString(PAT_GENERAL_TYPE_ID,preAuthDetailVO.getPreAuthTypeID());
			cStmtObject.setString(PAT_PRIORITY_GENERAL_TYPE_ID,preAuthDetailVO.getPriorityTypeID());

			if(preAuthDetailVO.getReceivedDate() != null){
				cStmtObject.setTimestamp(PAT_RECEIVED_DATE,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getPATReceivedDate(),preAuthDetailVO.getReceivedTime(),preAuthDetailVO.getReceivedDay()).getTime()));
			}//end of if(preAuthDetailVO.getReceivedDate() != null)
			else{
				cStmtObject.setTimestamp(PAT_RECEIVED_DATE, null);
			}//end of else

			if(preAuthDetailVO.getPrevApprovedAmount() != null){
				cStmtObject.setBigDecimal(PREV_APPROVED_AMOUNT,preAuthDetailVO.getPrevApprovedAmount());
			}//end of if(preAuthDetailVO.getPrevApprovedAmount() != null)
			else{
				cStmtObject.setString(PREV_APPROVED_AMOUNT,null);
			}//end of else

			/*if(preAuthDetailVO.getRequestAmount() != null){
				cStmtObject.setBigDecimal(PAT_REQUESTED_AMOUNT,preAuthDetailVO.getRequestAmount());
			}//end of if(preAuthDetailVO.getRequestAmount() != null)
			else{
				cStmtObject.setString(PAT_REQUESTED_AMOUNT,null);
			}//end of else
*/
			cStmtObject.setString(TREATING_DR_NAME,preAuthDetailVO.getDoctorName());
			cStmtObject.setString(PAT_RCVD_THRU_GENERAL_TYPE_ID,preAuthDetailVO.getPreAuthRecvTypeID());
			cStmtObject.setString(PHONE_NO_IN_HOSPITALISATION,preAuthDetailVO.getHospitalPhone());
			cStmtObject.setString(DISCREPANCY_PRESENT_YN,preAuthDetailVO.getDiscPresentYN());
			//cStmtObject.setString(DISCREPANCY_PRESENT_YN,preAuthDetailVO.getDiscPresentYN());
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl addPreauthDetails()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl addPreauthDetails()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return objArrayResult;
	}//end of addPreauthDetails(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
	
public Object[] saveActivityDetails(ActivityDetailsVO activityDetailsVO) throws TTKException {
	Object[] objArrayResult = new Object[2];
	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	try{
		
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveActivityDetails);
		if(activityDetailsVO.getActivityDtlSeqId()==null||activityDetailsVO.getActivityDtlSeqId()==0)cStmtObject.setString(1,null);
		else cStmtObject.setLong(1,activityDetailsVO.getActivityDtlSeqId());
		if("PAT".equals(activityDetailsVO.getAuthType())){
			cStmtObject.setLong(2,activityDetailsVO.getPreAuthSeqID());
			cStmtObject.setString(3,null);
		}else if ("CLM".equals(activityDetailsVO.getAuthType())){
			cStmtObject.setString(2,null);
			cStmtObject.setLong(3,activityDetailsVO.getClaimSeqID());
		}
		
		cStmtObject.setString(4,null);
		if(activityDetailsVO.getActivityCodeSeqId()==null)cStmtObject.setString(5,null);
		else cStmtObject.setLong(5,activityDetailsVO.getActivityCodeSeqId());
		//cStmtObject.setInt(6,activityDetailsVO.getSerialNo());
		//cStmtObject.setDate(6,new java.sql.Date(activityDetailsVO.getActivityStartDate().getTime()));
		//cStmtObject.setString(6,new SimpleDateFormat("MM/dd/yyyy").format(activityDetailsVO.getActivityStartDate()));
		cStmtObject.setDate(6,new java.sql.Date(activityDetailsVO.getActivityStartDate().getTime()));
		cStmtObject.setString(7,activityDetailsVO.getActivityTypeId());
		cStmtObject.setString(8,activityDetailsVO.getActivityCode());
		cStmtObject.setString(9,activityDetailsVO.getUnitType());
		cStmtObject.setString(10,activityDetailsVO.getModifier());
		cStmtObject.setString(11,activityDetailsVO.getInternalCode());
		cStmtObject.setString(12,activityDetailsVO.getPackageId());//cStmtObject.setString(,);
		cStmtObject.setString(13,activityDetailsVO.getBundleId());
		
		if(activityDetailsVO.getQuantity()==null)cStmtObject.setString(14,null);
		else cStmtObject.setFloat(14,activityDetailsVO.getQuantity());
		
		cStmtObject.setBigDecimal(15,activityDetailsVO.getGrossAmount());
		cStmtObject.setBigDecimal(16,activityDetailsVO.getDiscount());
		cStmtObject.setBigDecimal(17,activityDetailsVO.getDiscountedGross());
		cStmtObject.setBigDecimal(18,activityDetailsVO.getPatientShare());
		cStmtObject.setBigDecimal(19,activityDetailsVO.getCopay());
		cStmtObject.setBigDecimal(20,activityDetailsVO.getCoinsurance());
		cStmtObject.setBigDecimal(21,activityDetailsVO.getDeductible());
		cStmtObject.setBigDecimal(22,activityDetailsVO.getOutOfPocket());
		cStmtObject.setBigDecimal(23,activityDetailsVO.getNetAmount());
		BigDecimal allowedAmount=activityDetailsVO.getAllowedAmount();
		BigDecimal approvedAmount=activityDetailsVO.getAllowedAmount();
		allowedAmount=(allowedAmount==null||allowedAmount.intValue()==0)?null:allowedAmount;
		approvedAmount=(approvedAmount==null||approvedAmount.intValue()==0)?null:approvedAmount;
		cStmtObject.setBigDecimal(24,allowedAmount);
		cStmtObject.setBigDecimal(25,approvedAmount);
		cStmtObject.setString(26,activityDetailsVO.getAmountAllowed());
		if(!(activityDetailsVO.getDenialCode()==null||activityDetailsVO.getDenialCode().equals("")))
		cStmtObject.setString(27,activityDetailsVO.getDenialCode());
		else
		cStmtObject.setString(27,"");
		cStmtObject.setString(28,activityDetailsVO.getActivityRemarks());
		cStmtObject.setLong(29,activityDetailsVO.getAddedBy());
		if(activityDetailsVO.getApprovedQuantity()==null)cStmtObject.setString(30,null);
		else cStmtObject.setFloat(30,activityDetailsVO.getApprovedQuantity());
		cStmtObject.setBigDecimal(31,activityDetailsVO.getUnitPrice());
		cStmtObject.setString(32,activityDetailsVO.getClinicianId());
		cStmtObject.setString(33,activityDetailsVO.getOverrideYN());
	//	cStmtObject.setString(34,activityDetailsVO.getOverrideRemarks());	// code 
		if(!(activityDetailsVO.getOverrideRemarksCode()==null||activityDetailsVO.getOverrideRemarksCode().equals("")))
			cStmtObject.setString(34,activityDetailsVO.getOverrideRemarksCode());
			else
			cStmtObject.setString(34,"");
		cStmtObject.setString(35,activityDetailsVO.getActivityStatus());
		if(activityDetailsVO.getMedicationDays()==null)cStmtObject.setString(36,null);
		else cStmtObject.setInt(36,activityDetailsVO.getMedicationDays());
		cStmtObject.setString(37,activityDetailsVO.getPosology());
		cStmtObject.setBigDecimal(38,activityDetailsVO.getProviderRequestedAmt());
		if(!(activityDetailsVO.getDenialCode()==null||activityDetailsVO.getDenialCode().equals("")))
		cStmtObject.setString(39,activityDetailsVO.getDenialDescription());
		else
		cStmtObject.setString(39,"");
		cStmtObject.setString(40,activityDetailsVO.getActivityServiceType());
		cStmtObject.setString(41,activityDetailsVO.getActivityServiceCode());
		//cStmtObject.setString(40,activityDetailsVO.getEnablericopar());
		cStmtObject.setBigDecimal(42,activityDetailsVO.getUcr());
		cStmtObject.setString(43,activityDetailsVO.getDenialRemarks());
		cStmtObject.setBigDecimal(44, activityDetailsVO.getNonNetworkCopay());
		cStmtObject.setBigDecimal(45, activityDetailsVO.getConvertedUnitPrice());	
		cStmtObject.setString(46, activityDetailsVO.getInternalDesc());


		//cStmtObject.registerOutParameter(1,Types.BIGINT);

		cStmtObject.setString(47, activityDetailsVO.getToothNo());
		cStmtObject.setString(48, activityDetailsVO.getMemServiceCode());
		cStmtObject.setString(49, activityDetailsVO.getMemServiceDesc());
		cStmtObject.setString(50, activityDetailsVO.getActivityType());
		if(!(activityDetailsVO.getOverrideRemarksDesc()==null||activityDetailsVO.getOverrideRemarksDesc().equals("")))	// Desc
			cStmtObject.setString(51,activityDetailsVO.getOverrideRemarksDesc());
			else
			cStmtObject.setString(51,"");
		cStmtObject.setString(52,activityDetailsVO.getOtherRemarks());													// other remarks
		if(!(activityDetailsVO.getRemovedDenialCode()==null||activityDetailsVO.getRemovedDenialCode().equals(""))){
			cStmtObject.setString(54,activityDetailsVO.getRemovedDenialCode());
			cStmtObject.setString(55,activityDetailsVO.getRemovedDenialDescription());
		}else{
			cStmtObject.setString(54,"");
			cStmtObject.setString(55,"");
			}
		cStmtObject.registerOutParameter(53,Types.BIGINT);
		cStmtObject.setBigDecimal(56,activityDetailsVO.getProviderQty());
		cStmtObject.execute();
		objArrayResult[0]=cStmtObject.getLong(53);
		//objArrayResult[1]=cStmtObject.getLong(1);
		//return null;
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Statement in PreAuthDAOImpl saveActivityDetails()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in PreAuthDAOImpl saveActivityDetails()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return objArrayResult;
}//end of saveActivityDetails(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
	
	public ActivityDetailsVO getActivityDetails(Long activityDtlSeqId) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet ars = null;
		ActivityDetailsVO  activityDetailsVO=new ActivityDetailsVO();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetActivityDetails);
			cStmtObject.setLong(1,activityDtlSeqId);
			//cStmtObject.setString(2,"PAT");
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			ars = (java.sql.ResultSet)cStmtObject.getObject(2);//activity Details
		if(ars!=null){	
			if(ars.next()){

				activityDetailsVO.setActivityTypeId(ars.getString("ACTIVITY_TYPE"));
				activityDetailsVO.setPreAuthNo(ars.getString("PRE_AUTH_NUMBER"));
				activityDetailsVO.setPreAuthSeqID(ars.getLong("PAT_AUTH_SEQ_ID"));
				activityDetailsVO.setClaimNo(ars.getString("CLAIM_NUMBER"));
				activityDetailsVO.setClaimSeqID(ars.getLong("CLAIM_SEQ_ID"));
				activityDetailsVO.setActivityDtlSeqId(ars.getLong("ACTIVITY_DTL_SEQ_ID"));
				if(ars.getString("ACTIVITY_SEQ_ID")!=null){
				activityDetailsVO.setActivitySeqId(ars.getLong("ACTIVITY_SEQ_ID"));
				activityDetailsVO.setActivityCodeSeqId(ars.getLong("ACTIVITY_SEQ_ID"));
				}else {
					activityDetailsVO.setActivitySeqId(null);
					activityDetailsVO.setActivityCodeSeqId(null);
				}
				activityDetailsVO.setProviderQty(ars.getBigDecimal("provider_requested_quantity"));
				activityDetailsVO.setClinicianId(ars.getString("CLINICIAN_ID"));
				activityDetailsVO.setSerialNo(ars.getInt("S_NO")==0?null:ars.getInt("S_NO"));
				activityDetailsVO.setActivityCode(ars.getString("ACTIVITY_CODE")==null?"":ars.getString("ACTIVITY_CODE"));
				activityDetailsVO.setActivityCodeDesc(ars.getString("ACTIVITY_DESCRIPTION")==null?"":ars.getString("ACTIVITY_DESCRIPTION"));
				activityDetailsVO.setCopay(ars.getBigDecimal("PROVIDER_COPAY"));
				activityDetailsVO.setModifier( ars.getString("MODIFIER"));
				activityDetailsVO.setCoinsurance(ars.getBigDecimal("CO_INS_AMOUNT"));
				activityDetailsVO.setUnitType(ars.getString("UNIT_TYPE"));
				activityDetailsVO.setDeductible(ars.getBigDecimal("DEDUCT_AMOUNT"));
				activityDetailsVO.setQuantity(ars.getFloat("QUANTITY"));
				activityDetailsVO.setApprovedQuantity(ars.getFloat("APPROVED_QUANTITY"));
				activityDetailsVO.setOutOfPocket(ars.getBigDecimal("OUT_OF_POCKET_AMOUNT"));
				activityDetailsVO.setActivityStartDate(ars.getDate("START_DATE"));//StartDate(ars.getDate("START_DATE")==null?"":new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(ars.getDate("START_DATE").getTime())));
				activityDetailsVO.setPatientShare(ars.getBigDecimal("PATIENT_SHARE_AMOUNT"));
				activityDetailsVO.setUnitPrice(ars.getBigDecimal("UNIT_PRICE"));
				
				BigDecimal grossAmt=ars.getBigDecimal("GROSS_AMOUNT")==null?new BigDecimal("0.00"):ars.getBigDecimal("GROSS_AMOUNT");
				activityDetailsVO.setGrossAmount(grossAmt);
				
				BigDecimal netAmount=ars.getBigDecimal("NET_AMOUNT")==null?new BigDecimal("0.00"):ars.getBigDecimal("NET_AMOUNT");
				activityDetailsVO.setNetAmount(netAmount);
				
				activityDetailsVO.setApprovedAmount(ars.getBigDecimal("APPROVED_AMT"));
				activityDetailsVO.setAllowedAmount(ars.getBigDecimal("ALLOWED_AMOUNT"));
				activityDetailsVO.setDiscount(ars.getBigDecimal("DISCOUNT_AMOUNT"));
				activityDetailsVO.setDiscountPerUnit(ars.getBigDecimal("UNIT_DISCOUNT"));
				
				BigDecimal discountGrossAmt=ars.getBigDecimal("DISC_GROSS_AMOUNT")==null?new BigDecimal("0.00"):ars.getBigDecimal("DISC_GROSS_AMOUNT");
				activityDetailsVO.setDiscountedGross(discountGrossAmt);
				
				activityDetailsVO.setPackageId(ars.getString("PACKAGE_ID"));
				activityDetailsVO.setBundleId(ars.getString("BUNDLE_ID"));
				activityDetailsVO.setInternalCode(ars.getString("INTERNAL_CODE"));
				activityDetailsVO.setActivityRemarks(ars.getString("REMARKS"));
				activityDetailsVO.setDenialCode(ars.getString("DENIAL_CODE"));
				activityDetailsVO.setDenialDescription(ars.getString("DENIAL_DESC"));
				activityDetailsVO.setAmountAllowed(ars.getString("ALLOW_YN"));
				activityDetailsVO.setTariffYN(ars.getString("TARIFF_YN"));
				activityDetailsVO.setClinicianId(ars.getString("CLINICIAN_ID"));
				activityDetailsVO.setClinicianName(ars.getString("CLINICIAN_NAME"));
				activityDetailsVO.setRcopay(ars.getBigDecimal("COPAY_AMOUNT"));
				activityDetailsVO.setRoutOfPocket(ars.getBigDecimal("RULE_OUTOF_POCKET"));
				activityDetailsVO.setRcoinsurance(ars.getBigDecimal("RULE_COINSURENCE"));
				activityDetailsVO.setRdeductible(ars.getBigDecimal("RULE_DEDUCTIBLE"));
				activityDetailsVO.setRdisAllowedAmount(ars.getBigDecimal("DIS_ALLOWED_AMOUNT"));
				activityDetailsVO.setOverrideYN(ars.getString("OVERRIDE_YN"));
				activityDetailsVO.setOverrideRemarksCode(ars.getString("OVERRIDE_REMARKS_CODE"));				// OverrideRemarksCode:
				activityDetailsVO.setActivityStatus(ars.getString("APPROVE_YN"));
				activityDetailsVO.setMedicationDays(ars.getInt("POSOLOGY_DURATION"));
				activityDetailsVO.setPosology(ars.getString("POSOLOGY"));
				activityDetailsVO.setProviderRequestedAmt(ars.getBigDecimal("PROVIDER_NET_AMOUNT"));
				if(ars.getBigDecimal("ri_copar")!=null)
				{
				activityDetailsVO.setRicopar(ars.getBigDecimal("ri_copar"));
				}
				if(ars.getBigDecimal("ucr")!=null)
				{
				activityDetailsVO.setUcr(ars.getBigDecimal("ucr"));
				}
				
				activityDetailsVO.setActivityServiceType(ars.getString("SERVICE_TYPE"));
				activityDetailsVO.setActivityServiceCode(ars.getString("SERVICE_CODE"));				
				activityDetailsVO.setDenialRemarks(ars.getString("DENIAL_RES"));
				activityDetailsVO.setNonNetworkCopay(ars.getBigDecimal("NON_NETWORK_CO_PAY"));
				activityDetailsVO.setToothNo(ars.getString("TOOTH_NO"));
				activityDetailsVO.setToothNoReqYN(ars.getString("TOOTH_REQ_YN"));
				activityDetailsVO.setInternalDesc(ars.getString("INTERNAL_DESC"));
				activityDetailsVO.setConvertedUnitPrice(ars.getBigDecimal("converted_acitivty_amt"));
				activityDetailsVO.setProvCopayFlag(ars.getString("PROV_COPAY_FLAG"));
				activityDetailsVO.setCopayDeductFlag(ars.getString("COPAY_DEDUCT_FLAG"));
				activityDetailsVO.setCopayPerc(ars.getString("COPAY_PERC"));
				
				activityDetailsVO.setAreaOfCoverCopay(ars.getString("Area_Of_Cov_Copay"));
				activityDetailsVO.setAreaOfCoverDeduct(ars.getString("Area_Of_Cov_Deduct"));
				activityDetailsVO.setAreaOfCoverFlag(ars.getString("Area_Cp_Dct_Flag"));
				activityDetailsVO.setMemServiceCode(ars.getString("MEM_SERVICE_CODE"));
				activityDetailsVO.setMemServiceDesc(ars.getString("MEM_SERVICE_DESC"));
				activityDetailsVO.setActivityType(ars.getString("ACTIVITY_TYPE"));
				activityDetailsVO.setActivityType(ars.getString("ACTIVITY_TYPE_ID"));
				activityDetailsVO.setAlAhliYN(ars.getString("AL_AHLI_YN"));			
				activityDetailsVO.setActCarryFwdYN(ars.getString("CARRY_FOR_YN"));
				activityDetailsVO.setTpaDenialCode(ars.getString("TPA_DENIAL_CODE"));
				activityDetailsVO.setTpaDenialDescription(ars.getString("TPA_DENIAL_DESC"));
				//activityDetailsVO.setActivityTariffStatus(ars.getString("ACTIVITY_TARIFF_STATUS"));
				activityDetailsVO.setOverrideRemarksDesc(ars.getString("OVERRIDE_REMARKS"));  	// OverrideRemarksDesc
				activityDetailsVO.setOtherRemarks(ars.getString("OTHER_REMARKS"));				// OtherRemarks
				activityDetailsVO.setAlkootRemarks(ars.getString("ALKOOT_REMARKS"));
				activityDetailsVO.setResubRemarks(ars.getString("Resub_Remarks"));
				activityDetailsVO.setProviderInternalCode(ars.getString("HOSP_INTERNAL_CODE"));
				activityDetailsVO.setProviderInternalDescription(ars.getString("HOSP_INTERNAL_CODE_DESC"));		
				if(ars.getBigDecimal("GRANULAR_UNIT") != null){
					activityDetailsVO.setGranularUnit(ars.getBigDecimal("GRANULAR_UNIT"));
					
				}
				if(ars.getBigDecimal("no_of_units") != null){
					activityDetailsVO.setNoOfUnits(ars.getBigDecimal("no_of_units"));
				}
			}			
			}
			return activityDetailsVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (ars != null) ars.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PreAuthDAOImpl getActivityDetails()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getActivityDetails()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getActivityDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				ars = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally			
	}//end of getActivityDetails(Long activtiDtlSeqId)
	
	
	public BigDecimal[] getCalculatedPreauthAmount(Long preauthSeqId,Long hospitalSeqID, Long userSeqId) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		BigDecimal[] calculateAmunts=new BigDecimal[7]; 
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetCalculatePreauthAmount);

			cStmtObject.setLong(1,preauthSeqId);
			cStmtObject.setLong(2,hospitalSeqID);
			cStmtObject.registerOutParameter(3,Types.DECIMAL);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.setLong(5,userSeqId);
			
			cStmtObject.execute();
			//calculateAmunts[0]= cStmtObject.getBigDecimal(3);;
			rs = (java.sql.ResultSet)cStmtObject.getObject(4);
	
			
		if(rs!=null){	
			while(rs.next()){
				calculateAmunts[0]=rs.getBigDecimal("TOT_GROSS_AMOUNT");
				calculateAmunts[1]=rs.getBigDecimal("TOT_DISCOUNT_AMOUNT");
				calculateAmunts[2]=rs.getBigDecimal("TOT_DISC_GROSS_AMOUNT");
				calculateAmunts[3]=rs.getBigDecimal("TOT_PATIENT_SHARE_AMOUNT");
				calculateAmunts[4]=rs.getBigDecimal("TOT_NET_AMOUNT");
				calculateAmunts[5]=rs.getBigDecimal("TOT_ALLOWED_AMOUNT");
				calculateAmunts[6]=rs.getBigDecimal("TOT_APPROVED_AMOUNT");
				
			}
			}
	
			return calculateAmunts;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getCalculatedPreauthAmount()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getCalculatedPreauthAmount()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getCalculatedPreauthAmount()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally			
	}//end of getCalculatedPreauthAmount(Long preauthSeqId)
	
	
	public Object[] saveAndCompletePreauth(PreAuthDetailVO preAuthDetailVO) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Object[] rowProcess=new Object[1]; 
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthAmountApproved);
			cStmtObject.setLong(1,preAuthDetailVO.getPreAuthSeqID());
			if(preAuthDetailVO.getMemberSeqID()==null)cStmtObject.setString(2,null);
			else cStmtObject.setLong(2,preAuthDetailVO.getMemberSeqID());
			cStmtObject.setString(3,preAuthDetailVO.getAuthNum());
			cStmtObject.setTimestamp(4,new Timestamp(TTKCommon.getOracleDateWithTime(new SimpleDateFormat("dd/MM/yyyy").format(preAuthDetailVO.getAdmissionDate()),preAuthDetailVO.getAdmissionTime(),preAuthDetailVO.getAdmissionDay()).getTime()));
			cStmtObject.setBigDecimal(5, preAuthDetailVO.getApprovedAmount());
			cStmtObject.setString(6,preAuthDetailVO.getPreAuthRecvTypeID());
			cStmtObject.setString(7,preAuthDetailVO.getPreauthStatus());
			cStmtObject.setString(8,preAuthDetailVO.getMedicalOpinionRemarks());
			cStmtObject.setLong(9,preAuthDetailVO.getAddedBy());
			cStmtObject.setString(10,preAuthDetailVO.getDenialCode());
			cStmtObject.setString(11,preAuthDetailVO.getOverrideRemarks());
			cStmtObject.setString(12,preAuthDetailVO.getInternalRemarks());
			cStmtObject.setString(13,preAuthDetailVO.getReason());
			if(preAuthDetailVO.getReasonTime() != null){
				cStmtObject.setLong(14,preAuthDetailVO.getReasonTime());
			}else{
				cStmtObject.setString(14,null);
			}
			
			cStmtObject.registerOutParameter(15,Types.BIGINT);
			cStmtObject.execute();
			rowProcess[0]=cStmtObject.getLong(15);
			return rowProcess;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl saveAndCompletePreauth()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl saveAndCompletePreauth()",sqlExp);
						
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl saveAndCompletePreauth()",sqlExp);
							
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally			
	}//end of saveAndCompletePreauth(Long preauthSeqId)	

	public Map<String,String> getOtpResult(String memberId,Long addedBy,String otpNumber){
		Connection con=null;
		CallableStatement statement=null,statement2=null;
		ResultSet resultSet=null;
		Map<String,String> holdResult=new HashMap<String, String>();
	  try{	 
	     con=ResourceManager.getConnection();     
	     statement=con.prepareCall("{CALL AUTHORIZATION_PKG.SELECT_MEMBER(?,?)}");    
	     statement.setString(1,memberId);
	     statement.registerOutParameter(2,OracleTypes.CURSOR);
		statement.execute();
		 resultSet=(ResultSet)statement.getObject(2);	
		if(resultSet==null||!resultSet.next()||resultSet.getLong("MEMBER_SEQ_ID")==0){		
			holdResult.put("MEMBER_SEQ_ID", null);		 
		}else{
			String memberSeqId=TTKCommon.checkNull(resultSet.getLong("MEMBER_SEQ_ID")).toString();
			 holdResult.put("MEMBER_SEQ_ID",TTKCommon.checkNull(memberSeqId));
			 holdResult.put("TPA_ENROLLMENT_ID",TTKCommon.checkNull(resultSet.getString("TPA_ENROLLMENT_ID")));
			 holdResult.put("MEM_NAME",TTKCommon.checkNull(resultSet.getString("MEM_NAME")));
			 holdResult.put("MEM_AGE",TTKCommon.checkNull(resultSet.getString("MEM_AGE")));
			 holdResult.put("GENDER",TTKCommon.checkNull(resultSet.getString("GENDER")));
			 holdResult.put("EMIRATE_ID",TTKCommon.checkNull(resultSet.getString("EMIRATE_ID")));
			 holdResult.put("INS_SEQ_ID",TTKCommon.checkNull(resultSet.getLong("INS_SEQ_ID")).toString());
			 holdResult.put("INS_COMP_NAME",TTKCommon.checkNull(resultSet.getString("INS_COMP_NAME")));
			 holdResult.put("PAYER_ID",TTKCommon.checkNull(TTKCommon.checkNull(resultSet.getString("PAYER_ID"))));
			 holdResult.put("POLICY_SEQ_ID",TTKCommon.checkNull(resultSet.getLong("POLICY_SEQ_ID")).toString());
			 holdResult.put("POLICY_NUMBER",TTKCommon.checkNull(resultSet.getString("POLICY_NUMBER")));
			 holdResult.put("CORPORATE_NAME",TTKCommon.checkNull(resultSet.getString("CORPORATE_NAME")));
			 holdResult.put("POLICY_START_DATE",TTKCommon.convertDateAsString("dd/MM/yyyy",resultSet.getDate("POLICY_START_DATE")));
			 holdResult.put("POLICY_END_DATE",TTKCommon.convertDateAsString("dd/MM/yyyy",resultSet.getDate("POLICY_END_DATE")));
			 holdResult.put("NATIONALITY",TTKCommon.checkNull(resultSet.getString("NATIONALITY")));
			 holdResult.put("SUM_INSURED",TTKCommon.checkNull(resultSet.getString("SUM_INSURED")));
			 holdResult.put("AVA_SUM_INSURED",TTKCommon.checkNull(resultSet.getString("AVA_SUM_INSURED")));
			 holdResult.put("PRODUCT_NAME",TTKCommon.checkNull(resultSet.getString("PRODUCT_NAME")));
			 holdResult.put("PAYER_AUTHORITY",TTKCommon.checkNull(resultSet.getString("PAYER_AUTHORITY")));
			 holdResult.put("VIP_YN",TTKCommon.checkNull(resultSet.getString("VIP_YN")));
			
			 
			 holdResult.put("CLM_MEMB_INCP_DT",TTKCommon.checkNull(resultSet.getString("clm_mem_insp_date")));
				
			 statement2=con.prepareCall("{CALL PAT_XML_LOAD_PKG.VALIDATE_MEM_OTP(?,?,?,?)}");
			 statement2.setString(1,memberSeqId);		 
			 statement2.setString(2, otpNumber);
			 statement2.setLong(3,addedBy);
			 statement2.registerOutParameter(4,Types.VARCHAR);//possible values Y or N
			 statement2.execute();
			 String validationStatus=statement2.getString(4);		
			 holdResult.put("validationStatus",validationStatus);
		   }
		}catch(Exception exception){
			holdResult=null;
			log.error("Error  in PreAuthDAOImpl getOtpResult()",exception);
			exception.printStackTrace();
		}
		finally{
			try{
				if(resultSet!=null)resultSet.close();
				if(statement!=null)statement.close();
				if(statement2!=null)statement2.close();
			    if(con!=null)con.close();
			}catch(Exception exception2){
				log.error("Error while closing the Connection/Statement in PreAuthDAOImpl getOtpResult()",exception2);
				exception2.printStackTrace();
				}
			finally // Control will reach here in anycase set null to the objects 
			{
				resultSet = null;
				statement = null;
				statement2 = null;
				con = null;
			}//end of finally
		 }  
	return holdResult;	
	}//end of getCalculatedPreauthAmount(Long preauthSeqId)
	
public Map<String,String> getEncounterTypes(String benefitId){
	
		Connection con=null;
		CallableStatement statement=null;
		ResultSet resultSet=null;
		Map<String,String> encounterTypes=new LinkedHashMap<String, String>();
	  try{	 
	     con=ResourceManager.getConnection();     
	     statement=con.prepareCall(strEncounterTypes);    
	     statement.setString(1,benefitId);
	     statement.registerOutParameter(2,OracleTypes.CURSOR);
		statement.execute();
		 resultSet=(ResultSet)statement.getObject(2);	
		if(resultSet!=null){
			while(resultSet.next())		
			encounterTypes.put(resultSet.getString("ENCOUNTER_SEQ_ID"),resultSet.getString("DESCRIPTION"));
		   }
		}catch(Exception exception){
			log.error("Error  in PreAuthDAOImpl getEncounterTypes()",exception);
			exception.printStackTrace();
		}
		finally{
			try{
				if(resultSet!=null)resultSet.close();
				if(statement!=null)statement.close();
			    if(con!=null)con.close();
			}catch(Exception exception2){
				log.error("Error while closing the Connection/Statement in PreAuthDAOImpl getEncounterTypes()",exception2);
				exception2.printStackTrace();
				}
			finally // Control will reach here in anycase set null to the objects 
			{
				resultSet = null;
				statement = null;
				con = null;
			}//end of finally
		 }  
	return encounterTypes;	
	}//end of getEncounterTypes(Long preauthSeqId)
			
public Object[] savePreAuth(PreAuthDetailVO preAuthDetailVO,String status) throws TTKException {
	Object[] objArrayResult = new Object[3];
	Connection conn = null;
	CallableStatement cStmtObject=null;
	try{		
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddPreAuthorizationDetails);
		if("new".equals(status)){
			cStmtObject.setLong(1,0);
			cStmtObject.registerOutParameter(1,Types.BIGINT);
			cStmtObject.setLong(2,preAuthDetailVO.getParentPreAuthSeqID()==null?0:preAuthDetailVO.getParentPreAuthSeqID());
		   cStmtObject.setTimestamp(3,new java.sql.Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getReceiveDate(),preAuthDetailVO.getReceiveTime(),preAuthDetailVO.getReceiveDay()).getTime()));
		  if(preAuthDetailVO.getDischargeDate()!=null)cStmtObject.setTimestamp(4,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getDischargeDate(),preAuthDetailVO.getDischargeTime(),preAuthDetailVO.getDischargeDay()).getTime()));
		  else   cStmtObject.setDate(4,null);
		   cStmtObject.setString(5,preAuthDetailVO.getPreAuthRecvTypeID());		  
		   cStmtObject.setTimestamp(6,new Timestamp(TTKCommon.getOracleDateWithTime(new SimpleDateFormat("dd/MM/yyyy").format(preAuthDetailVO.getAdmissionDate()),preAuthDetailVO.getAdmissionTime(),preAuthDetailVO.getAdmissionDay()).getTime()));
		 
		   if(preAuthDetailVO.getMemberSeqID()!=null){
			  cStmtObject.setLong(7,preAuthDetailVO.getMemberSeqID());
		 }else{
			 cStmtObject.setString(7,null);
		 }
		   cStmtObject.setString(8,preAuthDetailVO.getMemberId());
		   cStmtObject.setString(9,preAuthDetailVO.getPreAuthNo());
		   cStmtObject.setString(10,null);
		   cStmtObject.setString(11,preAuthDetailVO.getPatientName());
		   
		   if(preAuthDetailVO.getMemberAge()==null){
			   cStmtObject.setString(12,null);
		   }else {
		   cStmtObject.setInt(12,preAuthDetailVO.getMemberAge());
		   }		 
		   if(preAuthDetailVO.getInsSeqId()!=null){
		   cStmtObject.setLong(13,preAuthDetailVO.getInsSeqId());
		   }else {
			   cStmtObject.setString(13,null);
		   }
		   cStmtObject.setString(14,TTKCommon.checkNull(preAuthDetailVO.getProviderSeqId()).toString());
		   if(preAuthDetailVO.getPolicySeqId()!=null){
			   cStmtObject.setLong(15,preAuthDetailVO.getPolicySeqId());
			   }else {
				   cStmtObject.setString(15,null);
			   }
		   cStmtObject.setString(16,preAuthDetailVO.getEmirateId());
		   cStmtObject.setString(17,preAuthDetailVO.getEncounterTypeId());
		   cStmtObject.setString(18,preAuthDetailVO.getEncounterFacilityId());
		   cStmtObject.setString(19,preAuthDetailVO.getEncounterStartTypeId());
		   cStmtObject.setString(20,preAuthDetailVO.getEncounterEndTypeId());
		   cStmtObject.setString(21,preAuthDetailVO.getRemarks());
		   cStmtObject.setLong(22,preAuthDetailVO.getAddedBy());
		   cStmtObject.setString(23,preAuthDetailVO.getClinicianId());
		   cStmtObject.setString(24,preAuthDetailVO.getSystemOfMedicine());
		   cStmtObject.setString(25,preAuthDetailVO.getAccidentRelatedCase());
		   cStmtObject.setString(26,preAuthDetailVO.getPriorityTypeID());
		   cStmtObject.setString(27,preAuthDetailVO.getNetworkProviderType());
		   cStmtObject.setString(28,preAuthDetailVO.getProviderName());
		   cStmtObject.setString(29,preAuthDetailVO.getProviderId());
		   cStmtObject.setString(30,preAuthDetailVO.getProviderArea());
		   cStmtObject.setString(31,preAuthDetailVO.getProviderEmirate());
		   cStmtObject.setString(32,preAuthDetailVO.getProviderCountry());
		   cStmtObject.setString(33,preAuthDetailVO.getProviderAddress());
		   cStmtObject.setString(34,preAuthDetailVO.getProviderPobox());
		   cStmtObject.setBigDecimal(35,preAuthDetailVO.getRequestedAmount());
		   cStmtObject.setString(36,preAuthDetailVO.getBenefitType());
		   int gravida=preAuthDetailVO.getGravida()==null?0:preAuthDetailVO.getGravida();
		   int para=preAuthDetailVO.getPara()==null?0:preAuthDetailVO.getPara();
		   int live=preAuthDetailVO.getLive()==null?0:preAuthDetailVO.getLive();
		   int abortion=preAuthDetailVO.getAbortion()==null?0:preAuthDetailVO.getAbortion();
		   cStmtObject.setInt(37,gravida);
		   cStmtObject.setInt(38,para);
		   cStmtObject.setInt(39,live);
		   cStmtObject.setInt(40,abortion);	
		   cStmtObject.setString(41,preAuthDetailVO.getEnhancedYN());
		   cStmtObject.setString(42,preAuthDetailVO.getRequestedAmountCurrency());
		   cStmtObject.setString(43,preAuthDetailVO.getClinicianName());
		   cStmtObject.setBigDecimal(44,preAuthDetailVO.getAvailableSumInsured());
		  // if(preAuthDetailVO.getRevisedDiagnosis()==null ||preAuthDetailVO.getRevisedDiagnosis().trim().length()<1 ||preAuthDetailVO.getRevisedDiagnosis()=="")
		   //{
			  
			   cStmtObject.setString(45,preAuthDetailVO.getDiagnosis());
		  // }
		   //else
		  // {
			   
		   
		  // }
		   /*if (preAuthDetailVO.getRevisedServices()==null || preAuthDetailVO.getRevisedServices().trim().length()<1 ||preAuthDetailVO.getRevisedServices()=="") 
		   {*/
			   cStmtObject.setString(46,preAuthDetailVO.getServices());
			
		   //}
		  // else
		   //{
			   
		   
		   //}
		  // if(preAuthDetailVO.getOralRevisedApprovedAmount()==null || preAuthDetailVO.getOralRevisedApprovedAmount().longValue()<=0)
		  
			   cStmtObject.setBigDecimal(47, preAuthDetailVO.getOralApprovedAmount());
			  
		   
		   cStmtObject.setString(48,preAuthDetailVO.getRevisedDiagnosis());
		   cStmtObject.setString(49,preAuthDetailVO.getRevisedServices());
		   cStmtObject.setBigDecimal(50, preAuthDetailVO.getOralRevisedApprovedAmount());
		   cStmtObject.setString(51,preAuthDetailVO.getOralORsystemStatus());
		   cStmtObject.registerOutParameter(9,Types.VARCHAR);
		   cStmtObject.setString(52,preAuthDetailVO.getNatureOfConception());
		   
			if(preAuthDetailVO.getLatMenstraulaPeriod() != null){
				cStmtObject.setTimestamp(53,new Timestamp(preAuthDetailVO.getLatMenstraulaPeriod().getTime()));
            }//end of 
			else{
				cStmtObject.setTimestamp(53, null);
            }//end of else
			
	   cStmtObject.setString(54,preAuthDetailVO.getProcessType());
		    cStmtObject.setString(55,preAuthDetailVO.getRequestedAmountcurrencyType());
		   cStmtObject.setBigDecimal(56,preAuthDetailVO.getConvertedAmount());
		   cStmtObject.setString(57,preAuthDetailVO.getCurrencyType());
		   cStmtObject.setString(58,preAuthDetailVO.getConversionRate());
		   cStmtObject.setString(59,preAuthDetailVO.getTpaReferenceNo());
		cStmtObject.setString(60, preAuthDetailVO.getTreatmentTypeID());
		 cStmtObject.setString(61,preAuthDetailVO.getModeofdelivery());

 		
		 cStmtObject.setString(62,preAuthDetailVO.getPartnerName());
		 
			if(preAuthDetailVO.getPartnerPreAuthSeqId() != null)
				 cStmtObject.setLong(63,preAuthDetailVO.getPartnerPreAuthSeqId());
			 else
				 cStmtObject.setString(63,null);
		 
		 
		 if(!(preAuthDetailVO.getPreauthPartnerRefId()==null||("").equals(preAuthDetailVO.getPreauthPartnerRefId().trim())))
			 cStmtObject.setString(64,preAuthDetailVO.getPreauthPartnerRefId());
		 else
			 cStmtObject.setString(64,null);
		 cStmtObject.setString(65,preAuthDetailVO.getEventReferenceNo());
		 cStmtObject.setString(66,preAuthDetailVO.getMatsubbenefit());
		 cStmtObject.setString(67,preAuthDetailVO.getClinicianEmail());
		 cStmtObject.setString(68,preAuthDetailVO.getClinicianSpeciality());
		   cStmtObject.registerOutParameter(9,Types.VARCHAR);
		   cStmtObject.registerOutParameter(69,Types.INTEGER);
			
		   cStmtObject.execute();
			
					
			objArrayResult[0] = cStmtObject.getLong(1);
			objArrayResult[1] = cStmtObject.getString(9);
			objArrayResult[2] = cStmtObject.getLong(69);
		
		}//end of if(preAuthDetailVO.getEnrollDtlSeqID() != null)
		else{//status=old
			cStmtObject.setLong(1,preAuthDetailVO.getPreAuthSeqID());
			cStmtObject.setLong(2,preAuthDetailVO.getParentPreAuthSeqID()==null?0:preAuthDetailVO.getParentPreAuthSeqID());
		   cStmtObject.setTimestamp(3,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getReceiveDate(),preAuthDetailVO.getReceiveTime(),preAuthDetailVO.getReceiveDay()).getTime()));
		  if(preAuthDetailVO.getDischargeDate()!=null)cStmtObject.setTimestamp(4,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getDischargeDate(),preAuthDetailVO.getDischargeTime(),preAuthDetailVO.getDischargeDay()).getTime()));
		  else   cStmtObject.setDate(4,null);
		   cStmtObject.setString(5,preAuthDetailVO.getPreAuthRecvTypeID());		  
		   cStmtObject.setTimestamp(6,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getHospitalAdmissionDate(),preAuthDetailVO.getAdmissionTime(),preAuthDetailVO.getAdmissionDay()).getTime()));
		  
		   if(preAuthDetailVO.getMemberSeqID()!=null){
			   cStmtObject.setLong(7,preAuthDetailVO.getMemberSeqID());
			 }else{
				 cStmtObject.setString(7,null);
			 }
		   
		   cStmtObject.setString(8,preAuthDetailVO.getMemberId());
		   cStmtObject.setString(9,preAuthDetailVO.getPreAuthNo());
		   cStmtObject.setString(10,preAuthDetailVO.getAuthNum());
		   cStmtObject.setString(11,preAuthDetailVO.getPatientName());
		   
		   if(preAuthDetailVO.getMemberAge()==null){
			   cStmtObject.setString(12,null);
		   }else {
		   cStmtObject.setInt(12,preAuthDetailVO.getMemberAge());
		   }
		 
		   
		   if(preAuthDetailVO.getInsSeqId()!=null){
		   cStmtObject.setLong(13,preAuthDetailVO.getInsSeqId());
		   }else {
			   cStmtObject.setString(13,null);
		   }
		   
		   cStmtObject.setString(14,TTKCommon.checkNull(preAuthDetailVO.getProviderSeqId()).toString());
		   
		   if(preAuthDetailVO.getPolicySeqId()!=null){
			   cStmtObject.setLong(15,preAuthDetailVO.getPolicySeqId());
			   }else {
				   cStmtObject.setString(15,null);
			   }
		   
		   cStmtObject.setString(16,preAuthDetailVO.getEmirateId());
		   cStmtObject.setString(17,preAuthDetailVO.getEncounterTypeId());
		   cStmtObject.setString(18,preAuthDetailVO.getEncounterFacilityId());
		   cStmtObject.setString(19,preAuthDetailVO.getEncounterStartTypeId());
		   cStmtObject.setString(20,preAuthDetailVO.getEncounterEndTypeId());
		   cStmtObject.setString(21,preAuthDetailVO.getRemarks());
		   cStmtObject.setLong(22,preAuthDetailVO.getUpdatedBy());
		   cStmtObject.setString(23,preAuthDetailVO.getClinicianId());
		   cStmtObject.setString(24,preAuthDetailVO.getSystemOfMedicine());
		   cStmtObject.setString(25,preAuthDetailVO.getAccidentRelatedCase());
		   cStmtObject.setString(26,preAuthDetailVO.getPriorityTypeID());
		   cStmtObject.setString(27,preAuthDetailVO.getNetworkProviderType());
		   cStmtObject.setString(28,preAuthDetailVO.getProviderName());
		   cStmtObject.setString(29,preAuthDetailVO.getProviderId());
		   cStmtObject.setString(30,preAuthDetailVO.getProviderArea());
		   cStmtObject.setString(31,preAuthDetailVO.getProviderEmirate());
		   cStmtObject.setString(32,preAuthDetailVO.getProviderCountry());
		   cStmtObject.setString(33,preAuthDetailVO.getProviderAddress());
		   cStmtObject.setString(34,preAuthDetailVO.getProviderPobox());
		   cStmtObject.setBigDecimal(35,preAuthDetailVO.getRequestedAmount());
		   cStmtObject.setString(36,preAuthDetailVO.getBenefitType());
		   int gravida=preAuthDetailVO.getGravida()==null?0:preAuthDetailVO.getGravida();
		   int para=preAuthDetailVO.getPara()==null?0:preAuthDetailVO.getPara();
		   int live=preAuthDetailVO.getLive()==null?0:preAuthDetailVO.getLive();
		   int abortion=preAuthDetailVO.getAbortion()==null?0:preAuthDetailVO.getAbortion();
		   cStmtObject.setInt(37,gravida);
		   cStmtObject.setInt(38,para);
		   cStmtObject.setInt(39,live);
		   cStmtObject.setInt(40,abortion);	
		   cStmtObject.setString(41,preAuthDetailVO.getEnhancedYN());
		   cStmtObject.setString(42,preAuthDetailVO.getRequestedAmountCurrency());
		   cStmtObject.setString(43,preAuthDetailVO.getClinicianName());
		   cStmtObject.setBigDecimal(44,preAuthDetailVO.getAvailableSumInsured());
		// if(preAuthDetailVO.getRevisedDiagnosis()==null ||preAuthDetailVO.getRevisedDiagnosis().trim().length()<1 ||preAuthDetailVO.getRevisedDiagnosis()=="")
		   //{
			  
			   cStmtObject.setString(45,preAuthDetailVO.getDiagnosis());
		  // }
		   //else
		  // {
			   
		   
		  // }
		   /*if (preAuthDetailVO.getRevisedServices()==null || preAuthDetailVO.getRevisedServices().trim().length()<1 ||preAuthDetailVO.getRevisedServices()=="") 
		   {*/
			   cStmtObject.setString(46,preAuthDetailVO.getServices());
			  
			
		   //}
		  // else
		   //{
			   
		   
		   //}
		  // if(preAuthDetailVO.getOralRevisedApprovedAmount()==null || preAuthDetailVO.getOralRevisedApprovedAmount().longValue()<=0)
		  
			   cStmtObject.setBigDecimal(47, preAuthDetailVO.getOralApprovedAmount());
			  
		   
		   cStmtObject.setString(48,preAuthDetailVO.getRevisedDiagnosis());
		   cStmtObject.setString(49,preAuthDetailVO.getRevisedServices());
		   cStmtObject.setBigDecimal(50, preAuthDetailVO.getOralRevisedApprovedAmount());
		   cStmtObject.setString(51,preAuthDetailVO.getOralORsystemStatus());
		   
  // cStmtObject.setString(52,preAuthDetailVO.getModeofdelivery());
		   cStmtObject.setString(52,preAuthDetailVO.getNatureOfConception());
		   
			if(preAuthDetailVO.getLatMenstraulaPeriod() != null){
				cStmtObject.setTimestamp(53,new Timestamp(preAuthDetailVO.getLatMenstraulaPeriod().getTime()));
            }//end of 
			else{
				cStmtObject.setTimestamp(53, null);
            }//end of else		

		 cStmtObject.setString(54,preAuthDetailVO.getProcessType());
		    cStmtObject.setString(55,preAuthDetailVO.getRequestedAmountcurrencyType());
		   cStmtObject.setBigDecimal(56,preAuthDetailVO.getConvertedAmount());
		   cStmtObject.setString(57,preAuthDetailVO.getCurrencyType());
		   cStmtObject.setString(58,preAuthDetailVO.getConversionRate());
		   cStmtObject.setString(59,preAuthDetailVO.getTpaReferenceNo());
		cStmtObject.setString(60, preAuthDetailVO.getTreatmentTypeID());
		   cStmtObject.setString(61,preAuthDetailVO.getModeofdelivery());

		 		cStmtObject.setString(62,preAuthDetailVO.getPartnerName());
		 		
		 		if(preAuthDetailVO.getPartnerPreAuthSeqId() != null)
					 cStmtObject.setLong(63,preAuthDetailVO.getPartnerPreAuthSeqId());
				 else
					 cStmtObject.setString(63,null);
				 
				 
				 if(!(preAuthDetailVO.getPreauthPartnerRefId()==null||("").equals(preAuthDetailVO.getPreauthPartnerRefId().trim())))
					 cStmtObject.setString(64,preAuthDetailVO.getPreauthPartnerRefId());
				 else
					 cStmtObject.setString(64,null);
		
			 cStmtObject.setString(65,preAuthDetailVO.getEventReferenceNo());
			 cStmtObject.setString(66,preAuthDetailVO.getMatsubbenefit());
			 cStmtObject.setString(67,preAuthDetailVO.getClinicianEmail());
			 cStmtObject.setString(68,preAuthDetailVO.getClinicianSpeciality());
		   cStmtObject.registerOutParameter(1,Types.INTEGER);
		   cStmtObject.registerOutParameter(9,Types.VARCHAR);
		   cStmtObject.registerOutParameter(69,Types.INTEGER);
			
		   cStmtObject.execute();
			
			objArrayResult[0] = cStmtObject.getLong(1);
			objArrayResult[1] = cStmtObject.getString(9);
			objArrayResult[2] = cStmtObject.getLong(69);
			
		}//end of else
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Statement in PreAuthDAOImpl savePreAuth()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in PreAuthDAOImpl savePreAuth()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return objArrayResult;
}//end of savePreAuth(PreAuthDetailVO preAuthDetailVO,String strSelectionType)

public Object[] saveDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException {
	Object[] objArrayResult = new Object[2];
	Connection conn = null;
	CallableStatement cStmtObject=null;

	try{		
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAddDiagnosisDetails);
		  
		  if(diagnosisDetailsVO.getDiagSeqId()==null) cStmtObject.setString(1,null);
		  else cStmtObject.setLong(1,diagnosisDetailsVO.getDiagSeqId());
		   if("PAT".equals(diagnosisDetailsVO.getAuthType())){
			   cStmtObject.setLong(2,diagnosisDetailsVO.getPreAuthSeqID());
			   cStmtObject.setString(3,null);
		   } else if("CLM".equals(diagnosisDetailsVO.getAuthType())){
			   cStmtObject.setString(2,null);
			   cStmtObject.setLong(3,diagnosisDetailsVO.getClaimSeqID());
		   }
		   cStmtObject.setLong(4,diagnosisDetailsVO.getIcdCodeSeqId());
		   cStmtObject.setString(5,diagnosisDetailsVO.getIcdCode());
		   if(diagnosisDetailsVO.getPrimaryAilment()==null||diagnosisDetailsVO.getPrimaryAilment().length()<1||"n".equalsIgnoreCase(diagnosisDetailsVO.getPrimaryAilment()))cStmtObject.setString(6,"N");
		   else cStmtObject.setString(6,"Y");
		   cStmtObject.setString(7,diagnosisDetailsVO.getPresentingComplaints());

		   cStmtObject.setInt(8, diagnosisDetailsVO.getDurAilment());
		   cStmtObject.setString(9, diagnosisDetailsVO.getDurationFlag());
		   
		   cStmtObject.setLong(10,diagnosisDetailsVO.getAddedBy());
		   cStmtObject.registerOutParameter(11,Types.INTEGER);
		
		   
		   cStmtObject.execute();			
		  objArrayResult[0] = cStmtObject.getInt(11);
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Statement in PreAuthDAOImpl saveDiagnosisDetails()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in PreAuthDAOImpl saveDiagnosisDetails()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return objArrayResult;
}//end of saveDiagnosisDetails(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
 public int deleteShortfallsDetails(String[] shortfallsDeatils) throws TTKException{
	int updatedRows;
	Connection conn = null;
	CallableStatement cStmtObject=null;
	try{		
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteShortfallsDetails);
		cStmtObject.setLong(1,new Long(shortfallsDeatils[0]));
		   cStmtObject.setLong(2,new Long(shortfallsDeatils[1]));
		   cStmtObject.setString(3,shortfallsDeatils[2]);
		   cStmtObject.setLong(4, new Long(shortfallsDeatils[3]));
		   cStmtObject.registerOutParameter(5,Types.INTEGER);
		   cStmtObject.execute();			
		   updatedRows = cStmtObject.getInt(5);		
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Statement in PreAuthDAOImpl deleteShortfallsDetails()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in PreAuthDAOImpl deleteShortfallsDetails()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return updatedRows;
}//end of deleteShortfallsDetails(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
public Object[] deleteDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException {
	Object[] objArrayResult = new Object[2];
	Connection conn = null;
	CallableStatement cStmtObject=null;
	try{		
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteDiagnosisDetails);
		if("PAT".equals(diagnosisDetailsVO.getAuthType()))cStmtObject.setLong(1,diagnosisDetailsVO.getPreAuthSeqID());
		else if("CLM".equals(diagnosisDetailsVO.getAuthType())) cStmtObject.setLong(1,diagnosisDetailsVO.getClaimSeqID());
			cStmtObject.setLong(2,diagnosisDetailsVO.getDiagSeqId());
		   cStmtObject.setString(3,diagnosisDetailsVO.getAuthType());
		   cStmtObject.setLong(4,diagnosisDetailsVO.getAddedBy());
		   cStmtObject.registerOutParameter(5,Types.INTEGER);
		   cStmtObject.execute();			
		  objArrayResult[0] = cStmtObject.getInt(5);		
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Statement in PreAuthDAOImpl deleteDiagnosisDetails()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in PreAuthDAOImpl deleteDiagnosisDetails()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return objArrayResult;
}//end of deleteDiagnosisDetails(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
public Object[] deleteActivityDetails(long preauthSeqId,long activityDtlSeqId,String authType,long userSeqId) throws TTKException {
	Object[] objArrayResult = new Object[2];
	Connection conn = null;
	CallableStatement cStmtObject=null;
	try{		
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteActivityDetails);
		cStmtObject.setLong(1,preauthSeqId);
		   cStmtObject.setLong(2,activityDtlSeqId);
		   cStmtObject.setString(3,authType);
		   cStmtObject.setLong(4,userSeqId);
		   cStmtObject.registerOutParameter(5,Types.INTEGER);
		   cStmtObject.execute();			
		  objArrayResult[0] = cStmtObject.getInt(5);		
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Statement in PreAuthDAOImpl deleteActivityDetails()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in PreAuthDAOImpl deleteActivityDetails()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return objArrayResult;
}//end of deleteActivityDetails()


public Document getPreAuthHistory(Long lngSeqId)throws TTKException{
    Connection conn = null;
    OracleCallableStatement cStmtObject = null;
    Document doc = null;
    try
    {
        conn = ResourceManager.getConnection();           
       
        	cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strPreAuthHistoryList);
            cStmtObject.setLong(1,lngSeqId);
            cStmtObject.registerOutParameter(2,Types.CLOB);                
            cStmtObject.execute();
            SAXReader saxReader=new SAXReader();
       doc=saxReader.read(cStmtObject.getCharacterStream(2));
        
    }//end of try
    catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "history");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "history");
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
    			log.error("Error while closing the Statement in PreAuthDAOImpl getPreAuthHistory()",sqlExp);
    			throw new TTKException(sqlExp, "history");
    		}//end of catch (SQLException sqlExp)
    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    		{
    			try
    			{
    				if(conn != null) conn.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Connection in PreAuthDAOImpl getPreAuthHistory()",sqlExp);
    				throw new TTKException(sqlExp, "history");
    			}//end of catch (SQLException sqlExp)
    		}//end of finally Connection Close
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "history");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		cStmtObject = null;
    		conn = null;
    	}//end of finally
	}//end of finally
    return doc;
}//end of getPreAuthHistory(String strHistoryType, long lngSeqId, long lngEnrollDtlSeqId)


public Document getClaimHistory(Long lngSeqId)throws TTKException{
    Connection conn = null;
    OracleCallableStatement cStmtObject = null;
    Document doc = null;
    try
    {
        conn = ResourceManager.getConnection();           
       
        	cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strClaimHistoryList);
            cStmtObject.setLong(1,lngSeqId);
            cStmtObject.registerOutParameter(2,Types.CLOB);                
            cStmtObject.execute();
            SAXReader saxReader=new SAXReader();
          doc=saxReader.read(cStmtObject.getCharacterStream(2));
        
    }//end of try
    catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "history");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "history");
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
    			log.error("Error while closing the Statement in PreAuthDAOImpl getClaimHistory()",sqlExp);
    			throw new TTKException(sqlExp, "history");
    		}//end of catch (SQLException sqlExp)
    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
    		{
    			try
    			{
    				if(conn != null) conn.close();
    			}//end of try
    			catch (SQLException sqlExp)
    			{
    				log.error("Error while closing the Connection in PreAuthDAOImpl getClaimHistory()",sqlExp);
    				throw new TTKException(sqlExp, "history");
    			}//end of catch (SQLException sqlExp)
    		}//end of finally Connection Close
    	}//end of try
    	catch (TTKException exp)
    	{
    		throw new TTKException(exp, "history");
    	}//end of catch (TTKException exp)
    	finally // Control will reach here in anycase set null to the objects 
    	{
    		cStmtObject = null;
    		conn = null;
    	}//end of finally
	}//end of finally
    return doc;
}//end of getClaimHistory(String strHistoryType, long lngSeqId, long lngEnrollDtlSeqId)


public Object[] savePreAuth2(PreAuthDetailVO preAuthDetailVO,String strSelectionType) throws TTKException {
		Object[] objArrayResult = new Object[3];
		long lngMemberSeqID = 0;
		long lngPreAuthSeqID = 0;
		long lngPATEnrollDtlSeqID = 0;
		AdditionalDetailVO additionalDetailVO = null;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			additionalDetailVO = preAuthDetailVO.getAdditionalDetailVO();
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePreAuthorization);

			if(preAuthDetailVO.getEnrollDtlSeqID() != null){
				cStmtObject.setLong(PAT_ENROLL_DETAIL_SEQ_ID,preAuthDetailVO.getEnrollDtlSeqID());
			}//end of if(preAuthDetailVO.getEnrollDtlSeqID() != null)
			else{
				cStmtObject.setLong(PAT_ENROLL_DETAIL_SEQ_ID,0);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getMemberSeqID() != null){
				cStmtObject.setLong(MEMBER_SEQ_ID,preAuthDetailVO.getClaimantVO().getMemberSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getMemberSeqID() != null)
			else{
				cStmtObject.setLong(MEMBER_SEQ_ID,0);
			}//end of else

			cStmtObject.setString(TPA_ENROLLMENT_ID,preAuthDetailVO.getClaimantVO().getEnrollmentID());
			cStmtObject.setString(POLICY_NUMBER,preAuthDetailVO.getClaimantVO().getPolicyNbr());

			if(preAuthDetailVO.getClaimantVO().getPolicySeqID() != null){
				cStmtObject.setLong(POLICY_SEQ_ID,preAuthDetailVO.getClaimantVO().getPolicySeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getPolicySeqID() != null)
			else{
				cStmtObject.setString(POLICY_SEQ_ID,null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getInsSeqID() != null){
				cStmtObject.setLong(INS_SEQ_ID,preAuthDetailVO.getClaimantVO().getInsSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getInsSeqID() != null)
			else{
				cStmtObject.setString(INS_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(GENDER_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getGenderTypeID());
			cStmtObject.setString(CLAIMANT_NAME,preAuthDetailVO.getClaimantVO().getName());

			if(preAuthDetailVO.getClaimantVO().getAge() != null){
				cStmtObject.setInt(MEM_AGE,preAuthDetailVO.getClaimantVO().getAge());
			}//end of if(preAuthDetailVO.getClaimantVO().getAge() != null)
			else{
				cStmtObject.setString(MEM_AGE,null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getDateOfInception() != null){
				cStmtObject.setTimestamp(DATE_OF_INCEPTION,new Timestamp(preAuthDetailVO.getClaimantVO().getDateOfInception().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getDateOfInception() != null)
			else{
				cStmtObject.setTimestamp(DATE_OF_INCEPTION, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getDateOfExit() != null){
				cStmtObject.setTimestamp(DATE_OF_EXIT,new Timestamp(preAuthDetailVO.getClaimantVO().getDateOfExit().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getDateOfExit() != null)
			else{
				cStmtObject.setTimestamp(DATE_OF_EXIT, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getTotalSumInsured() != null){
				cStmtObject.setBigDecimal(MEM_TOTAL_SUM_INSURED,preAuthDetailVO.getClaimantVO().getTotalSumInsured());
			}//end of if(preAuthDetailVO.getClaimantVO().getTotalSumInsured() != null)
			else{
				cStmtObject.setString(MEM_TOTAL_SUM_INSURED,null);
			}//end of else

			cStmtObject.setString(CATEGORY_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getCategoryTypeID());
			cStmtObject.setString(INSURED_NAME,preAuthDetailVO.getClaimantVO().getPolicyHolderName());
			cStmtObject.setString(PHONE_1,preAuthDetailVO.getClaimantVO().getPhone());

			if(preAuthDetailVO.getClaimantVO().getStartDate() != null){
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_FROM,new Timestamp(preAuthDetailVO.getClaimantVO().getStartDate().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getStartDate() != null)
			else{
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_FROM, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getEndDate() != null){
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_TO,new Timestamp(preAuthDetailVO.getClaimantVO().getEndDate().getTime()));
			}//end of if(preAuthDetailVO.getClaimantVO().getEndDate() != null)
			else{
				cStmtObject.setTimestamp(POLICY_EFFECTIVE_TO, null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getProductSeqID() != null){
				cStmtObject.setLong(PRODUCT_SEQ_ID,preAuthDetailVO.getClaimantVO().getProductSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getProductSeqID() != null)
			else{
				cStmtObject.setString(PRODUCT_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(INS_STATUS_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getTermStatusID());
			cStmtObject.setString(ENROL_TYPE_ID,preAuthDetailVO.getClaimantVO().getPolicyTypeID());
			cStmtObject.setString(POLICY_SUB_GENERAL_TYPE_ID,preAuthDetailVO.getClaimantVO().getPolicySubTypeID());

			if(preAuthDetailVO.getClaimantVO().getGroupRegnSeqID() != null){
				cStmtObject.setLong(GROUP_REG_SEQ_ID,preAuthDetailVO.getClaimantVO().getGroupRegnSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getGroupRegSeqID() != null)
			else{
				cStmtObject.setString(GROUP_REG_SEQ_ID,null);
			}//end of else

			if(preAuthDetailVO.getAdmissionDate() != null){

				cStmtObject.setTimestamp(DATE_OF_HOSPITALIZATION,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getHospitalAdmissionDate(),preAuthDetailVO.getAdmissionTime(),preAuthDetailVO.getAdmissionDay()).getTime()));

			}//end of if(preAuthDetailVO.getAdmissionDate() != null)
			else{
				cStmtObject.setTimestamp(DATE_OF_HOSPITALIZATION, null);
			}//end of else

			if(preAuthDetailVO.getOfficeSeqID() != null){
				cStmtObject.setLong(TPA_OFFICE_SEQ_ID,preAuthDetailVO.getOfficeSeqID());
			}//end of if(preAuthDetailVO.getOfficeSeqID() != null)
			else{
				cStmtObject.setString(TPA_OFFICE_SEQ_ID,null);
			}//end of else

			if(preAuthDetailVO.getPreAuthHospitalVO().getHospSeqId() != null){
				cStmtObject.setLong(HOSP_SEQ_ID,preAuthDetailVO.getPreAuthHospitalVO().getHospSeqId());
			}//end of if(preAuthDetailVO.getPreAuthHospitalVO().getHospSeqId() != null)
			else{
				cStmtObject.setString(HOSP_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(PAT_STATUS_GENERAL_TYPE_ID,preAuthDetailVO.getStatusTypeID());

			if(preAuthDetailVO.getPreAuthSeqID()!=null)
			{
				cStmtObject.setLong(PAT_GEN_DETAIL_SEQ_ID,preAuthDetailVO.getPreAuthSeqID());
			}else
			{
				cStmtObject.setLong(PAT_GEN_DETAIL_SEQ_ID,0);
			}

			cStmtObject.setString(PAT_GENERAL_TYPE_ID,preAuthDetailVO.getPreAuthTypeID());
			cStmtObject.setString(PAT_PRIORITY_GENERAL_TYPE_ID,preAuthDetailVO.getPriorityTypeID());

			if(preAuthDetailVO.getReceivedDate() != null){
				cStmtObject.setTimestamp(PAT_RECEIVED_DATE,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getPATReceivedDate(),preAuthDetailVO.getReceivedTime(),preAuthDetailVO.getReceivedDay()).getTime()));
			}//end of if(preAuthDetailVO.getReceivedDate() != null)
			else{
				cStmtObject.setTimestamp(PAT_RECEIVED_DATE, null);
			}//end of else

			if(preAuthDetailVO.getPrevApprovedAmount() != null){
				cStmtObject.setBigDecimal(PREV_APPROVED_AMOUNT,preAuthDetailVO.getPrevApprovedAmount());
			}//end of if(preAuthDetailVO.getPrevApprovedAmount() != null)
			else{
				cStmtObject.setString(PREV_APPROVED_AMOUNT,null);
			}//end of else

			/*if(preAuthDetailVO.getRequestAmount() != null){
				cStmtObject.setBigDecimal(PAT_REQUESTED_AMOUNT,preAuthDetailVO.getRequestAmount());
			}//end of if(preAuthDetailVO.getRequestAmount() != null)
			else{
				cStmtObject.setString(PAT_REQUESTED_AMOUNT,null);
			}//end of else
*/
			cStmtObject.setString(TREATING_DR_NAME,preAuthDetailVO.getDoctorName());
			cStmtObject.setString(PAT_RCVD_THRU_GENERAL_TYPE_ID,preAuthDetailVO.getPreAuthRecvTypeID());
			cStmtObject.setString(PHONE_NO_IN_HOSPITALISATION,preAuthDetailVO.getHospitalPhone());
			cStmtObject.setString(DISCREPANCY_PRESENT_YN,preAuthDetailVO.getDiscPresentYN());

			if(strSelectionType.equals("PAT")){
				if(preAuthDetailVO.getParentGenDtlSeqID() != null){
					cStmtObject.setLong(PARENT_GEN_DETAIL_SEQ_ID,preAuthDetailVO.getParentGenDtlSeqID());
				}//end of if(preAuthDetailVO.getParentGenDtlSeqID() != null)
				else{
					cStmtObject.setString(PARENT_GEN_DETAIL_SEQ_ID,null);
				}//end of else
			}//end of if(strSelectionType.equals("PAT"))

			if(strSelectionType.equals("ICO")){
				if(preAuthDetailVO.getPreAuthSeqID() != null){
					cStmtObject.setLong(PARENT_GEN_DETAIL_SEQ_ID,preAuthDetailVO.getPreAuthSeqID());
				}//end of if(preAuthDetailVO.getPreAuthSeqID() != null)
				else{
					cStmtObject.setString(PARENT_GEN_DETAIL_SEQ_ID,null);
				}//end of else
			}//end of if(strSelectionType.equals("ICO"))

			cStmtObject.setString(REMARKS,preAuthDetailVO.getRemarks());

			if(preAuthDetailVO.getClaimantVO().getAvailSumInsured() != null){
				cStmtObject.setBigDecimal(AVA_SUM_INSURED,preAuthDetailVO.getClaimantVO().getAvailSumInsured());
			}//end of if(preAuthDetailVO.getClaimantVO().getAvailSumInsured() != null)
			else{
				cStmtObject.setString(AVA_SUM_INSURED,null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getCumulativeBonus() != null){
				cStmtObject.setBigDecimal(AVA_CUM_BONUS,preAuthDetailVO.getClaimantVO().getCumulativeBonus());
			}//end of if(preAuthDetailVO.getClaimantVO().getCumulativeBonus() != null)
			else{
				cStmtObject.setString(AVA_CUM_BONUS,null);
			}//end of else

			if(additionalDetailVO != null){

				log.debug("Inside Additional Details not null");
				if(preAuthDetailVO.getAdditionalDetailVO().getAdditionalDtlSeqID() != null){
					cStmtObject.setLong(ADDITIONAL_DTL_SEQ_ID,preAuthDetailVO.getAdditionalDetailVO().getAdditionalDtlSeqID());
				}//end of if(preAuthDetailVO.getAdditionalDetailVO().getAdditionalDtlSeqID() != null)
				else{
					cStmtObject.setString(ADDITIONAL_DTL_SEQ_ID,null);
				}//end of else

				cStmtObject.setString(RELSHIP_TYPE_ID,preAuthDetailVO.getAdditionalDetailVO().getRelationshipTypeID());
				cStmtObject.setString(EMPLOYEE_NO,preAuthDetailVO.getAdditionalDetailVO().getEmployeeNbr());
				cStmtObject.setString(EMPLOYEE_NAME,preAuthDetailVO.getAdditionalDetailVO().getEmployeeName());

				if(preAuthDetailVO.getAdditionalDetailVO().getJoiningDate() != null){
					cStmtObject.setTimestamp(DATE_OF_JOINING,new Timestamp(preAuthDetailVO.getAdditionalDetailVO().getJoiningDate().getTime()));
				}//end of if(preAuthDetailVO.getAdditionalDetailVO().getJoiningDate() != null)
				else{
					cStmtObject.setTimestamp(DATE_OF_JOINING, null);
				}//end of else

				if(preAuthDetailVO.getAdditionalDetailVO().getAddReceivedDate() != null){
					cStmtObject.setTimestamp(INFO_RECEIVED_DATE,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getAdditionalDetailVO().getInfoReceivedDate(),preAuthDetailVO.getAdditionalDetailVO().getAddReceivedTime(),preAuthDetailVO.getAdditionalDetailVO().getAddReceivedDay()).getTime()));
				}//end of if(preAuthDetailVO.getAdditionalDetailVO().getAddReceivedDate() != null)
				else{
					cStmtObject.setTimestamp(INFO_RECEIVED_DATE, null);
				}//end of else

				cStmtObject.setString(COMM_TYPE_ID,preAuthDetailVO.getAdditionalDetailVO().getSourceTypeID());
				cStmtObject.setString(INFO_RCVD_GENERAL_TYPE_ID,preAuthDetailVO.getAdditionalDetailVO().getReceivedFromType());
				cStmtObject.setString(REFERENCE_NO,preAuthDetailVO.getAdditionalDetailVO().getReferenceNbr());
				cStmtObject.setString(CONTACT_PERSON,preAuthDetailVO.getAdditionalDetailVO().getContactPerson());
				cStmtObject.setString(ADDITIONAL_REMARKS,preAuthDetailVO.getAdditionalDetailVO().getAdditionalRemarks());

				if(preAuthDetailVO.getPreAuthTypeID().equalsIgnoreCase("MAN")){
					if(preAuthDetailVO.getClaimantVO().getPolicyTypeID().equalsIgnoreCase("NCR")){
						cStmtObject.setString(INS_SCHEME,preAuthDetailVO.getAdditionalDetailVO().getInsScheme());
						log.debug("Scheme name :"+preAuthDetailVO.getAdditionalDetailVO().getInsScheme());
						cStmtObject.setString(CERTIFICATE_NO,preAuthDetailVO.getAdditionalDetailVO().getCertificateNo());
						log.debug("CertificateNo :"+preAuthDetailVO.getAdditionalDetailVO().getCertificateNo());
					}//end of if(preAuthDetailVO.getClaimantVO().getPolicyTypeID().equalsIgnoreCase("NCR"))
					else{
						cStmtObject.setString(INS_SCHEME,null);
						cStmtObject.setString(CERTIFICATE_NO,null);
					}//end of else
				}//end of if(preAuthDetailVO.getPreAuthTypeID().equalsIgnoreCase("MAN"))
				else{
					if(preAuthDetailVO.getClaimantVO().getPolicyTypeID().equalsIgnoreCase("NCR")){
						cStmtObject.setString(INS_SCHEME,preAuthDetailVO.getClaimantVO().getInsScheme());
						log.debug("Scheme name :"+preAuthDetailVO.getClaimantVO().getInsScheme());
						cStmtObject.setString(CERTIFICATE_NO,preAuthDetailVO.getClaimantVO().getCertificateNo());
						log.debug("CertificateNo :"+preAuthDetailVO.getClaimantVO().getCertificateNo());
					}//end of if(preAuthDetailVO.getClaimantVO().getPolicyTypeID().equalsIgnoreCase("NCR"))
					else{
						cStmtObject.setString(INS_SCHEME,null);
						cStmtObject.setString(CERTIFICATE_NO,null);
					}//end of else
				}//end of else
			}//end of if(additionalDetailVO != null)
			else{

				log.debug("Inside Additional Details null");
				cStmtObject.setString(ADDITIONAL_DTL_SEQ_ID,null);
				cStmtObject.setString(RELSHIP_TYPE_ID,null);
				cStmtObject.setString(EMPLOYEE_NO,null);
				cStmtObject.setString(EMPLOYEE_NAME,null);
				cStmtObject.setTimestamp(DATE_OF_JOINING, null);
				cStmtObject.setTimestamp(INFO_RECEIVED_DATE, null);
				cStmtObject.setString(COMM_TYPE_ID,null);
				cStmtObject.setString(INFO_RCVD_GENERAL_TYPE_ID,null);
				cStmtObject.setString(REFERENCE_NO,null);
				cStmtObject.setString(CONTACT_PERSON,null);
				cStmtObject.setString(ADDITIONAL_REMARKS,null);
				cStmtObject.setString(INS_SCHEME,preAuthDetailVO.getClaimantVO().getInsScheme());
				
				log.debug("Scheme name :"+preAuthDetailVO.getClaimantVO().getInsScheme());
				cStmtObject.setString(CERTIFICATE_NO,preAuthDetailVO.getClaimantVO().getCertificateNo());
				log.debug("CertificateNo :"+preAuthDetailVO.getClaimantVO().getCertificateNo());
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getAvblBufferAmount() != null){
				cStmtObject.setBigDecimal(AVA_BUFFER_AMOUNT,preAuthDetailVO.getClaimantVO().getAvblBufferAmount());
			}//end of if(preAuthDetailVO.getClaimantVO().getAvblBufferAmount() != null)
			else{
				cStmtObject.setString(AVA_BUFFER_AMOUNT,null);
			}//end of else

			if(preAuthDetailVO.getClaimantVO().getBufferDtlSeqID() != null){
				cStmtObject.setLong(BUFF_DTL_SEQ_ID,preAuthDetailVO.getClaimantVO().getBufferDtlSeqID());
			}//end of if(preAuthDetailVO.getClaimantVO().getBufferDtlSeqID() != null)
			else{
				cStmtObject.setString(BUFF_DTL_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(BUFFER_ALLOWED_YN,preAuthDetailVO.getClaimantVO().getBufferAllowedYN());
			cStmtObject.setString(ADMIN_AUTH_GENERAL_TYPE_ID,preAuthDetailVO.getAdminAuthTypeID());
			cStmtObject.setString(COMPLETED_YN,preAuthDetailVO.getCompletedYN());
			cStmtObject.setString(PRE_AUTH_DMS_REFERENCE_ID,preAuthDetailVO.getDMSRefID());
			cStmtObject.setString(EMAIL_ID,preAuthDetailVO.getClaimantVO().getEmailID());

			cStmtObject.setString(INS_CUSTOMER_CODE,preAuthDetailVO.getClaimantVO().getInsCustCode());
			cStmtObject.setString(SELECTION_TYPE,strSelectionType);
			
			cStmtObject.setString(INSUR_REF_NUMBER,preAuthDetailVO.getClaimantVO().getInsuranceRefNo());
			cStmtObject.setLong(USER_SEQ_ID,preAuthDetailVO.getUpdatedBy());
			cStmtObject.registerOutParameter(MEMBER_SEQ_ID,Types.BIGINT);
			cStmtObject.registerOutParameter(PAT_GEN_DETAIL_SEQ_ID,Types.BIGINT);
			cStmtObject.registerOutParameter(PAT_ENROLL_DETAIL_SEQ_ID,Types.BIGINT);
			cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);
			cStmtObject.execute();

			lngPreAuthSeqID = cStmtObject.getLong(PAT_GEN_DETAIL_SEQ_ID);
			lngMemberSeqID = cStmtObject.getLong(MEMBER_SEQ_ID);
			lngPATEnrollDtlSeqID = cStmtObject.getLong(PAT_ENROLL_DETAIL_SEQ_ID);
			objArrayResult[0] = new Long(lngMemberSeqID);
			objArrayResult[1] = new Long(lngPreAuthSeqID);
			objArrayResult[2] = new Long(lngPATEnrollDtlSeqID);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl savePreAuth()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl savePreAuth()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return objArrayResult;
	}//end of savePreAuth(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
	

	/**
	 * This method will allow to Override the Preauth information
	 * @param lngPATGenDetailSeqID PATGenDetailSeqID
	 * @param lngPATEnrollDtlSeqID PATEnrollDtlSeqID
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
	public PreAuthDetailVO overridePreauth(long lngPATGenDetailSeqID,long lngPATEnrollDtlSeqID,long lngUserSeqID) throws TTKException {
		String strReview = "";
		Long lngEventSeqID = null;
		Integer intReviewCount = null;
		Integer intRequiredReviewCnt = null;
		String strEventName = "";
		PreAuthDetailVO preauthDetailVO = null;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strOverridePreauth);
			preauthDetailVO = new PreAuthDetailVO();
			cStmtObject.setLong(1,lngPATGenDetailSeqID);
			cStmtObject.setLong(2,lngPATEnrollDtlSeqID);
			cStmtObject.setLong(3,lngUserSeqID);
			cStmtObject.registerOutParameter(4,Types.BIGINT);//EVENT_SEQ_ID
			cStmtObject.registerOutParameter(5,Types.BIGINT);//REVIEW_COUNT
			cStmtObject.registerOutParameter(6,Types.BIGINT);//REQUIRED_REVIEW_COUNT
			cStmtObject.registerOutParameter(7,Types.VARCHAR);
			cStmtObject.registerOutParameter(8,Types.VARCHAR);
			cStmtObject.execute();

			lngEventSeqID = cStmtObject.getLong(4);
			intReviewCount = cStmtObject.getInt(5);
			intRequiredReviewCnt = cStmtObject.getInt(6);
			strEventName = cStmtObject.getString(7);
			strReview = cStmtObject.getString(8);

			preauthDetailVO.setEventSeqID(lngEventSeqID);
			preauthDetailVO.setReviewCount(intReviewCount);
			preauthDetailVO.setRequiredReviewCnt(intRequiredReviewCnt);
			preauthDetailVO.setEventName(strEventName);
			preauthDetailVO.setReview(strReview);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl savePreAuth()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl savePreAuth()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return preauthDetailVO;
	}//end of overridePreauth(long lngPATGenDetailSeqID,long lngPATEnrollDtlSeqID,long lngUserSeqID)

	/**
	 * This method saves the Review information
	 * @param preauthDetailVO the object which contains the Review Details which has to be  saved
	 * @param strMode String object which contains Mode Pre-authorization/Claims - PAT/CLM
	 * @param strType String object which contains Type
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
	public PreAuthDetailVO saveReview(PreAuthDetailVO preauthDetailVO,String strMode,String strType) throws TTKException {

		String strReview = "";
		Long lngEventSeqID = null;
		Integer intReviewCount = null;
		Integer intRequiredReviewCnt = null;
		String strEventName = "";
		String strCodingReviewYN = "";
		String strShowCodingOverride = "";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveReview);

			if(preauthDetailVO.getPreAuthSeqID() != null){
				cStmtObject.setLong(1,preauthDetailVO.getPreAuthSeqID());
			}//end of if(preauthDetailVO.getPreAuthSeqID() != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else

			if(preauthDetailVO.getEventSeqID() != null){

				cStmtObject.setLong(2,preauthDetailVO.getEventSeqID());
			}//end of if(preauthDetailVO.getEventSeqID() != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			if(preauthDetailVO.getReviewCount() != null){
				
				cStmtObject.setInt(3,preauthDetailVO.getReviewCount());
			}//end of if(preauthDetailVO.getReviewCount() != null)
			else{
				cStmtObject.setString(3,null);
			}//end of else

			if(preauthDetailVO.getRequiredReviewCnt() != null){

				cStmtObject.setLong(4,preauthDetailVO.getRequiredReviewCnt());
			}//end of if(preauthDetailVO.getRequiredReviewCnt() != null)
			else{
				cStmtObject.setString(4,null);
			}//end of else

			cStmtObject.setString(5,strMode);

			cStmtObject.setString(6,strType);
			cStmtObject.setLong(7,preauthDetailVO.getUpdatedBy());
			cStmtObject.registerOutParameter(2,Types.BIGINT);//EVENT_SEQ_ID
			cStmtObject.registerOutParameter(3,Types.BIGINT);//REVIEW_COUNT
			cStmtObject.registerOutParameter(4,Types.BIGINT);//REQUIRED_REVIEW_COUNT
			cStmtObject.registerOutParameter(8,Types.VARCHAR);
			cStmtObject.registerOutParameter(9,Types.VARCHAR);
			cStmtObject.registerOutParameter(10,Types.VARCHAR);//CODING_REVIEW_YN
			cStmtObject.registerOutParameter(11,Types.VARCHAR);//SHOW_CODING_OVERRIDE
			cStmtObject.execute();

			lngEventSeqID = cStmtObject.getLong(2);
			intReviewCount = cStmtObject.getInt(3);
			intRequiredReviewCnt = cStmtObject.getInt(4);
			strEventName = cStmtObject.getString(8);
			strReview = cStmtObject.getString(9);
			strCodingReviewYN = cStmtObject.getString(10);
			strShowCodingOverride = cStmtObject.getString(11);

			preauthDetailVO.setEventSeqID(lngEventSeqID);
			preauthDetailVO.setReviewCount(intReviewCount);
			preauthDetailVO.setRequiredReviewCnt(intRequiredReviewCnt);
			preauthDetailVO.setEventName(strEventName);
			preauthDetailVO.setReview(strReview);
			preauthDetailVO.setCoding_review_yn(strCodingReviewYN);
			preauthDetailVO.setShowCodingOverrideYN(strShowCodingOverride);

		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl saveReview()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl saveReview()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return preauthDetailVO;
	}//end of saveReview(PreAuthDetailVO preauthDetailVO,String strMode,String strType)

	/**
	 * This method assigns the Pre-Authorization information to the corresponding Doctor
	 * @param preAuthAssignmentVO PreAuthAssignmentVO which contains Pre-Authorization information to assign the corresponding Doctor
	 * @return long value which contains Assign Users Seq ID
	 * @exception throws TTKException
	 */
	public long assignPreAuth(PreAuthAssignmentVO preAuthAssignmentVO, String strModeValue) throws TTKException {
		long lngAssignUsersSeqID = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveAssignUsers);
			if(preAuthAssignmentVO.getAssignUserSeqID() != null){
				cStmtObject.setLong(1,preAuthAssignmentVO.getAssignUserSeqID());
			}//end of if(preAuthAssignmentVO.getAssignUserSeqID() != null)
			else{
				cStmtObject.setLong(1,0);
			}//end of else

			if(preAuthAssignmentVO.getClaimSeqID() != null){
				cStmtObject.setLong(2,preAuthAssignmentVO.getClaimSeqID());
			}//end of if(preAuthAssignmentVO.getClaimSeqID() != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			if(preAuthAssignmentVO.getPreAuthSeqID() != null){
				cStmtObject.setLong(3,preAuthAssignmentVO.getPreAuthSeqID());
			}//end of if(preAuthAssignmentVO.getPreAuthSeqID() != null)
			else{
				cStmtObject.setString(3,null);
			}//end of else

			if(preAuthAssignmentVO.getOfficeSeqID() != null){
				cStmtObject.setLong(4,preAuthAssignmentVO.getOfficeSeqID());
			}//end of if(preAuthAssignmentVO.getOfficeSeqID() != null)
			else{
				cStmtObject.setString(4,null);
			}//end of else

			if(preAuthAssignmentVO.getDoctor() != null){
				cStmtObject.setLong(5,preAuthAssignmentVO.getDoctor());
			}//end of if(preAuthAssignmentVO.getDoctor() != null)
			else{
				cStmtObject.setString(5,null);
			}//end of else

			cStmtObject.setString(6,null);
			cStmtObject.setString(7,preAuthAssignmentVO.getRemarks());
			cStmtObject.setString(8,"EXT");
			cStmtObject.setString(9,preAuthAssignmentVO.getSuperUserYN());
			cStmtObject.setLong(10,preAuthAssignmentVO.getUpdatedBy());
			cStmtObject.setString(11,strModeValue);
			cStmtObject.registerOutParameter(12,Types.INTEGER);
			cStmtObject.registerOutParameter(1,Types.BIGINT);
			cStmtObject.execute();
			lngAssignUsersSeqID = cStmtObject.getLong(1);//ASSIGN_USERS_SEQ_ID
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl assignPreAuth()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl assignPreAuth()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return lngAssignUsersSeqID;
	}//end of assignPreAuth(PreAuthAssignmentVO preAuthAssignmentVO)

	/**
	 * This method returns the PreAuthAssignmentVO  which contains Assignment details
	 * @param lngAssignUsersSeqID long value contains Assign User Seq ID
	 * @param lngUserSeqID long value contains Logged-in User Seq ID
	 * @param strMode contains PAT/CLM for identifying Pre-Authorization/Claims
	 * @return ArrayList of PreAuthAssignmentVO object which contains Assignment details
	 * @exception throws TTKException
	 */
	public PreAuthAssignmentVO getAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthAssignmentVO preauthAssignmentVO = null;
		ArrayList alAssignUserList = new ArrayList();
		ArrayList<Object> alUserList = new ArrayList<Object>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetAssignTo);
			cStmtObject.setLong(1,lngAssignUsersSeqID);
			cStmtObject.setLong(2,lngUserSeqID);
			cStmtObject.setString(3,strMode);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs=(java.sql.ResultSet)cStmtObject.getObject(4);
			if(rs != null){
				while(rs.next()){
					preauthAssignmentVO = new PreAuthAssignmentVO();

					if(rs.getString("ASSIGN_USERS_SEQ_ID") != null){
						preauthAssignmentVO.setAssignUserSeqID(new Long(rs.getLong("ASSIGN_USERS_SEQ_ID")));
					}//end of if(rs.getString("ASSIGN_USERS_SEQ_ID") != null)

					if(rs.getString("SEQ_ID") != null){
						if(strMode.equals("PAT")){
							preauthAssignmentVO.setPreAuthSeqID(new Long(rs.getLong("SEQ_ID")));
						}//end of if(strMode.equals("PAT"))
						else{
							preauthAssignmentVO.setClaimSeqID(new Long(rs.getLong("SEQ_ID")));
						}//end of else
					}//end of if(rs.getString("SEQ_ID") != null)

					preauthAssignmentVO.setSelectedPreAuthNos(TTKCommon.checkNull(rs.getString("ID_NUMBER")));
					preauthAssignmentVO.setSelectedClaimNos(TTKCommon.checkNull(rs.getString("ID_NUMBER")));
					preauthAssignmentVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					

					if(rs.getString("ASSIGNED_TO_USER") != null){
						preauthAssignmentVO.setDoctor(new Long(rs.getLong("ASSIGNED_TO_USER")));
					}//end of if(rs.getString("ASSIGNED_TO_USER") != null)

					preauthAssignmentVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));

					if(rs.getString("POLICY_SEQ_ID") != null){
						preauthAssignmentVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)

					if(rs.getString("SEQ_ID") != null && rs.getString("TPA_OFFICE_SEQ_ID") != null ){
						alUserList.add(new Long(rs.getLong("SEQ_ID")));

						if(rs.getString("POLICY_SEQ_ID") != null){
							alUserList.add(new Long(rs.getLong("POLICY_SEQ_ID")));
						}//end of if(rs.getString("POLICY_SEQ_ID") != null)
						else{
							alUserList.add(null);
						}//end of else

						alUserList.add(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
						alAssignUserList = getAssignUserList(alUserList,strMode);
						preauthAssignmentVO.setAssignUserList(alAssignUserList);
					}//end of if

				}//end of while(rs.next())
			}//end of if(rs != null)
			return preauthAssignmentVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getAssignTo()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getAssignTo()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getAssignTo()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode)

	/**
	 * This method returns the ArrayList object, which contains all the Users for the Corresponding TTK Branch
	 * @param alAssignUserList ArrayList Object contains PreauthSeqID/ClaimSeqID,PolicySeqID and TTKBranch
	 * @param strMode contains PAT/CLM for identifying the Pre-Authorization/Claims flow
	 * @return ArrayList object which contains all the Users for the Corresponding TTK Branch
	 * @exception throws TTKException
	 */
	public ArrayList getAssignUserList(ArrayList alAssignUserList,String strMode) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ArrayList<Object> alUserList = new ArrayList<Object>();
		CacheObject cacheObject = null;
		try{
			conn = ResourceManager.getConnection();

			if(strMode.equals("PAT")){
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPAAssignUserList);
			}//end of if(strMode.equals("PAT"))
			else if(strMode.equals("CLM")){
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimAssignUserList);
			}//end of if(strMode.equals("CLM"))
			cStmtObject.setLong(1,(Long)alAssignUserList.get(alAssignUserList.size()-1));//Mandatory
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					cacheObject = new CacheObject();
					cacheObject.setCacheId((rs.getString("CONTACT_SEQ_ID")));
					cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
					alUserList.add(cacheObject);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return alUserList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getAssignUserList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getAssignUserList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getAssignUserList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getAssignUserList(ArrayList alAssignUserList,String strMode)

	/**
	 * This method returns the Arraylist of ClaimantVO's  which contains Claimant details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of ClaimantVO object which contains Claimant details
	 * @exception throws TTKException
	 */
	public ArrayList getClaimantList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ClaimantVO claimantVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strEnrollmentList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));
			cStmtObject.setString(10,(String)alSearchCriteria.get(12));
			cStmtObject.setString(11,(String)alSearchCriteria.get(13));
			cStmtObject.setString(12,(String)alSearchCriteria.get(14));
			cStmtObject.setString(13,(String)alSearchCriteria.get(15));
			cStmtObject.setLong(14,(Long)alSearchCriteria.get(9));
			cStmtObject.setString(15,(String)alSearchCriteria.get(10));
			cStmtObject.setString(16,(String)alSearchCriteria.get(11));
			
			cStmtObject.registerOutParameter(17,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(17);
			if(rs != null){
				while(rs.next()){
					claimantVO = new ClaimantVO();

					if(rs.getString("MEMBER_SEQ_ID") != null){
						claimantVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs.getString("MEMBER_SEQ_ID") != null)

					if(rs.getString("POLICY_GROUP_SEQ_ID") != null){
						claimantVO.setPolicyGroupSeqID(new Long(rs.getLong("POLICY_GROUP_SEQ_ID")));
					}//end of if(rs.getString("POLICY_GROUP_SEQ_ID") != null)

					claimantVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					claimantVO.setName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
					claimantVO.setPolicyHolderName(TTKCommon.checkNull(rs.getString("INSURED_NAME")));

					if(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")).equals("COR")){
						claimantVO.setEmployeeName(TTKCommon.checkNull(rs.getString("INSURED_NAME")));
					}//end of if(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")).equals("COR"))

					claimantVO.setEmployeeNbr(TTKCommon.checkNull(rs.getString("EMPLOYEE_NO")));
					claimantVO.setGenderTypeID(TTKCommon.checkNull(rs.getString("GENDER_GENERAL_TYPE_ID")));
					claimantVO.setGenderDescription(TTKCommon.checkNull(rs.getString("GENDER")));

					if(rs.getString("MEM_AGE") != null){
						claimantVO.setAge(new Integer(rs.getString("MEM_AGE")));
					}//end of if(rs.getString("MEM_AGE") != null)

					claimantVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));

					if(rs.getString("INS_SEQ_ID") != null){
						claimantVO.setInsSeqID(new Long(rs.getLong("INS_SEQ_ID")));
					}//end of if(rs.getString("") != null)
					claimantVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					claimantVO.setCompanyCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));

					if(rs.getString("GROUP_REG_SEQ_ID") != null){
						claimantVO.setGroupRegnSeqID(new Long(rs.getLong("GROUP_REG_SEQ_ID")));
					}//end of if(rs.getString("") != null)

					claimantVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					claimantVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));

					if(rs.getString("DATE_OF_INCEPTION") != null){
						claimantVO.setDateOfInception(new Date(rs.getTimestamp("DATE_OF_INCEPTION").getTime()));
					}//end of if(rs.getString("DATE_OF_INCEPTION") != null)

					if(rs.getString("DATE_OF_EXIT") != null){
						claimantVO.setDateOfExit(new Date(rs.getTimestamp("DATE_OF_EXIT").getTime()));
					}//end of if(rs.getString("DATE_OF_EXIT") != null)

					if(rs.getString("MEM_TOT_SUM_INSURED") != null){
						claimantVO.setTotalSumInsured(new BigDecimal(rs.getString("MEM_TOT_SUM_INSURED")));
					}//end of if(rs.getString("MEM_TOT_SUM_INSURED") != null)

					if(rs.getString("AVAIL_SUM_INSURED") != null){
						claimantVO.setAvailSumInsured(new BigDecimal(rs.getString("AVAIL_SUM_INSURED")));
					}//end of if(rs.getString("AVAIL_SUM_INSURED") != null)

					claimantVO.setCategoryTypeID(TTKCommon.checkNull(rs.getString("CATEGORY_GENERAL_TYPE_ID")));
					claimantVO.setCategoryDesc(TTKCommon.checkNull(rs.getString("CATEGORY")));
					claimantVO.setPolicyTypeID(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")));

					if(rs.getString("PRODUCT_SEQ_ID") != null){
						claimantVO.setProductSeqID(new Long(rs.getLong("PRODUCT_SEQ_ID")));
					}//end of if(rs.getString("PRODUCT_SEQ_ID") != null)

					claimantVO.setPolicySubTypeID(TTKCommon.checkNull(rs.getString("POLICY_SUB_GENERAL_TYPE_ID")));
					claimantVO.setTermStatusID(TTKCommon.checkNull(rs.getString("INS_STATUS_GENERAL_TYPE_ID")));
					claimantVO.setPhone(TTKCommon.checkNull(rs.getString("PHONE")));

					if(rs.getString("EFFECTIVE_FROM_DATE") != null){
						claimantVO.setStartDate(new Date(rs.getTimestamp("EFFECTIVE_FROM_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_FROM_DATE") != null)

					if(rs.getString("EFFECTIVE_TO_DATE") != null){
						claimantVO.setEndDate(new Date(rs.getTimestamp("EFFECTIVE_TO_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_TO_DATE") != null)

					if(rs.getString("POLICY_SEQ_ID") != null){
						claimantVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)

					claimantVO.setBufferAllowedYN(TTKCommon.checkNull(rs.getString("BUFFER_ALLOWED_YN")));

					if(rs.getString("TOTAL_BUFFER_AMOUNT") != null){
						claimantVO.setTotBufferAmt(new BigDecimal(rs.getString("TOTAL_BUFFER_AMOUNT")));
					}//end of if(rs.getString("TOTAL_BUFFER_AMOUNT") != null)

					claimantVO.setSchemeCertNbr(TTKCommon.checkNull(rs.getString("SCHEME_OR_CERTFICATE")));

					if(rs.getString("INS_HEAD_OFFICE_SEQ_ID") != null){
						claimantVO.setInsHeadOffSeqID(new Long(rs.getLong("INS_HEAD_OFFICE_SEQ_ID")));
					}//end of if(rs.getString("INS_HEAD_OFFICE_SEQ_ID") != null)

					claimantVO.setRelationTypeID(TTKCommon.checkNull(rs.getString("RELSHIP_TYPE_ID")));
					claimantVO.setEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
					claimantVO.setInsCustCode(TTKCommon.checkNull(rs.getString("INS_CUSTOMER_CODE")));
					claimantVO.setCertificateNo(TTKCommon.checkNull(rs.getString("CERTIFICATE_NO")));
					claimantVO.setInsScheme(TTKCommon.checkNull(rs.getString("INS_SCHEME")));
					claimantVO.setCreditCardNbr(TTKCommon.checkNull(rs.getString("CREDITCARD_NO")));
					
					claimantVO.setsQatarId(TTKCommon.checkNull(rs.getString("QATAR_ID")));
					
					alResultList.add(claimantVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getClaimantList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getClaimantList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getClaimantList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimantList(ArrayList alSearchCriteria)

	/**
	 * This method returns the Arraylist of ClaimantVO's  which contains Claimant details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of ClaimantVO object which contains Claimant details
	 * @exception throws TTKException
	 */
	public ArrayList getEnrollmentList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ClaimantNewVO claimantVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectMemberList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));
			cStmtObject.setString(10,(String)alSearchCriteria.get(11));
			cStmtObject.setString(11,(String)alSearchCriteria.get(12));
			cStmtObject.setString(12,(String)alSearchCriteria.get(13));
			cStmtObject.setString(13,(String)alSearchCriteria.get(14));
			cStmtObject.setString(14,(String)alSearchCriteria.get(15));
			cStmtObject.setLong(15,(Long)alSearchCriteria.get(9));
			cStmtObject.setString(16,(String)alSearchCriteria.get(10));
			cStmtObject.registerOutParameter(17,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(17);
			if(rs != null){
				while(rs.next()){
					claimantVO = new ClaimantNewVO();

					if(rs.getString("MEMBER_SEQ_ID") != null){
						claimantVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs.getString("MEMBER_SEQ_ID") != null)

					claimantVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					claimantVO.setName(TTKCommon.checkNull(rs.getString("MEM_NAME")));

					claimantVO.setGenderDescription(TTKCommon.checkNull(rs.getString("GENDER")));

					if(rs.getString("MEM_AGE") != null){
						claimantVO.setAge(new Integer(rs.getString("MEM_AGE")));
					}//end of if(rs.getString("MEM_AGE") != null)

					claimantVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));

					if(rs.getString("GROUP_REG_SEQ_ID") != null){
						claimantVO.setGroupRegnSeqID(new Long(rs.getLong("GROUP_REG_SEQ_ID")));
					}//end of if(rs.getString("") != null)

					claimantVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));

					//					claimantVO.setInsScheme(TTKCommon.checkNull(rs.getString("SCHEME_OR_CERTFICATE")));
					claimantVO.setSchemeCertNbr(TTKCommon.checkNull(rs.getString("SCHEME_OR_CERTFICATE")));

					if(rs.getString("EFFECTIVE_FROM_DATE") != null){
						claimantVO.setStartDate(new Date(rs.getTimestamp("EFFECTIVE_FROM_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_FROM_DATE") != null)

					if(rs.getString("EFFECTIVE_TO_DATE") != null){
						claimantVO.setEndDate(new Date(rs.getTimestamp("EFFECTIVE_TO_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_TO_DATE") != null)
					
					claimantVO.setsQatarId(TTKCommon.checkNull(rs.getString("QATAR_ID")));
					
					alResultList.add(claimantVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getClaimantList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getClaimantList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getClaimantList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimantList(ArrayList alSearchCriteria)

	/**
	 * This method returns the Arraylist of ClaimantVO's  which contains Claimant details
	 * @param lngMemSeqID String object which contains the search criteria
	 * @return ArrayList of ClaimantVO object which contains Claimant details
	 * @exception throws TTKException
	 */
	public ClaimantVO getSelectEnrollmentID(Long lngMemSeqID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ClaimantVO claimantVO = null;
		try{
			conn = ResourceManager.getConnection();
			log.debug("lngMemSeqID is :"+lngMemSeqID);
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectEnrollmentID);
			cStmtObject.setLong(1,lngMemSeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			claimantVO = new ClaimantVO();
			if(rs != null){
				while(rs.next()){

					if(rs.getString("MEMBER_SEQ_ID") != null){
						claimantVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs.getString("MEMBER_SEQ_ID") != null)

					if(rs.getString("POLICY_GROUP_SEQ_ID") != null){
						claimantVO.setPolicyGroupSeqID(new Long(rs.getLong("POLICY_GROUP_SEQ_ID")));
					}//end of if(rs.getString("POLICY_GROUP_SEQ_ID") != null)

					claimantVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					claimantVO.setName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
					claimantVO.setPolicyHolderName(TTKCommon.checkNull(rs.getString("INSURED_NAME")));

					if(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")).equals("COR")){
						claimantVO.setEmployeeName(TTKCommon.checkNull(rs.getString("INSURED_NAME")));
					}//end of if(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")).equals("COR"))

					claimantVO.setEmployeeNbr(TTKCommon.checkNull(rs.getString("EMPLOYEE_NO")));
					claimantVO.setGenderTypeID(TTKCommon.checkNull(rs.getString("GENDER_GENERAL_TYPE_ID")));
					claimantVO.setGenderDescription(TTKCommon.checkNull(rs.getString("GENDER")));

					if(rs.getString("MEM_AGE") != null){
						claimantVO.setAge(new Integer(rs.getString("MEM_AGE")));
					}//end of if(rs.getString("MEM_AGE") != null)

					claimantVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));

					if(rs.getString("INS_SEQ_ID") != null){
						claimantVO.setInsSeqID(new Long(rs.getLong("INS_SEQ_ID")));
					}//end of if(rs.getString("") != null)
					claimantVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					claimantVO.setCompanyCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));

					if(rs.getString("GROUP_REG_SEQ_ID") != null){
						claimantVO.setGroupRegnSeqID(new Long(rs.getLong("GROUP_REG_SEQ_ID")));
					}//end of if(rs.getString("") != null)

					claimantVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					claimantVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));

					//changes on 9th Dec for KOC1136
					try{
						String VIP_YN=TTKCommon.checkNull(rs.getString("VIP_YN"));
						if( VIP_YN.equals("") || VIP_YN.equalsIgnoreCase("N"))
						{
							claimantVO.setVipYN("N");
						}
						if(VIP_YN.equalsIgnoreCase("Y")) {
							claimantVO.setVipYN("Y");
						}
					
					}
					catch(SQLException e) {
						// TODO: handle exception
						log.error("VIP_YN Column DOES NOT EXIST ");
					}
					//changes on 7th Dec for KOC1136
					//claimantVO.setVipYN(TTKCommon.checkNull(rs.getString("vipYN")));
					//claimantVO.setVipYN("Y");
					//changes on 7th Dec


					if(rs.getString("DATE_OF_INCEPTION") != null){
						claimantVO.setDateOfInception(new Date(rs.getTimestamp("DATE_OF_INCEPTION").getTime()));
					}//end of if(rs.getString("DATE_OF_INCEPTION") != null)

					if(rs.getString("DATE_OF_EXIT") != null){
						claimantVO.setDateOfExit(new Date(rs.getTimestamp("DATE_OF_EXIT").getTime()));
					}//end of if(rs.getString("DATE_OF_EXIT") != null)

					if(rs.getString("MEM_TOT_SUM_INSURED") != null){
						claimantVO.setTotalSumInsured(new BigDecimal(rs.getString("MEM_TOT_SUM_INSURED")));
					}//end of if(rs.getString("MEM_TOT_SUM_INSURED") != null)

					if(rs.getString("AVAIL_SUM_INSURED") != null){
						claimantVO.setAvailSumInsured(new BigDecimal(rs.getString("AVAIL_SUM_INSURED")));
					}//end of if(rs.getString("AVAIL_SUM_INSURED") != null)

					claimantVO.setCategoryTypeID(TTKCommon.checkNull(rs.getString("CATEGORY_GENERAL_TYPE_ID")));
					claimantVO.setCategoryDesc(TTKCommon.checkNull(rs.getString("CATEGORY")));
					claimantVO.setPolicyTypeID(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")));

					if(rs.getString("PRODUCT_SEQ_ID") != null){
						claimantVO.setProductSeqID(new Long(rs.getLong("PRODUCT_SEQ_ID")));
					}//end of if(rs.getString("PRODUCT_SEQ_ID") != null)

					claimantVO.setPolicySubTypeID(TTKCommon.checkNull(rs.getString("POLICY_SUB_GENERAL_TYPE_ID")));
					claimantVO.setTermStatusID(TTKCommon.checkNull(rs.getString("INS_STATUS_GENERAL_TYPE_ID")));
					claimantVO.setPhone(TTKCommon.checkNull(rs.getString("PHONE")));

					if(rs.getString("EFFECTIVE_FROM_DATE") != null){
						claimantVO.setStartDate(new Date(rs.getTimestamp("EFFECTIVE_FROM_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_FROM_DATE") != null)

					if(rs.getString("EFFECTIVE_TO_DATE") != null){
						claimantVO.setEndDate(new Date(rs.getTimestamp("EFFECTIVE_TO_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_TO_DATE") != null)

					if(rs.getString("POLICY_SEQ_ID") != null){
						claimantVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)

					claimantVO.setBufferAllowedYN(TTKCommon.checkNull(rs.getString("BUFFER_ALLOWED_YN")));

					if(rs.getString("TOTAL_BUFFER_AMOUNT") != null){
						claimantVO.setTotBufferAmt(new BigDecimal(rs.getString("TOTAL_BUFFER_AMOUNT")));
					}//end of if(rs.getString("TOTAL_BUFFER_AMOUNT") != null)

					claimantVO.setSchemeCertNbr(TTKCommon.checkNull(rs.getString("SCHEME_OR_CERTFICATE")));

					if(rs.getString("INS_HEAD_OFFICE_SEQ_ID") != null){
						claimantVO.setInsHeadOffSeqID(new Long(rs.getLong("INS_HEAD_OFFICE_SEQ_ID")));
					}//end of if(rs.getString("INS_HEAD_OFFICE_SEQ_ID") != null)

					claimantVO.setRelationTypeID(TTKCommon.checkNull(rs.getString("RELSHIP_TYPE_ID")));
					claimantVO.setEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
					claimantVO.setInsCustCode(TTKCommon.checkNull(rs.getString("INS_CUSTOMER_CODE")));
					claimantVO.setCertificateNo(TTKCommon.checkNull(rs.getString("CERTIFICATE_NO")));
					claimantVO.setInsScheme(TTKCommon.checkNull(rs.getString("INS_SCHEME")));
					claimantVO.setCreditCardNbr(TTKCommon.checkNull(rs.getString("CREDITCARD_NO")));
					//					alResultList.add(claimantVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			//			return (ArrayList)alResultList;
			return claimantVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getClaimantList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getClaimantList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getClaimantList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimantList(ArrayList alSearchCriteria)


	/**
	 * This method returns the Arraylist of EnhancementVO's  which contains SumInsured Enhancement details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of EnhancementVO object which contains SumInsured Enhancement details
	 * @exception throws TTKException
	 */
	public ArrayList getSumInsuredEnhancementList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		EnhancementVO enhancementVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strEnhancementList);
			cStmtObject.setLong(1,(Long)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(4));
			cStmtObject.setString(3,(String)alSearchCriteria.get(5));
			cStmtObject.setLong(4,(Long)alSearchCriteria.get(1));
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(5);
			if(rs != null){
				while(rs.next()){
					enhancementVO = new EnhancementVO();

					if(rs.getString("POLICY_SEQ_ID") != null){
						enhancementVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)

					enhancementVO.setPolicyNo(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));

					if(rs.getString("EFFECTIVE_FROM_DATE") != null){
						enhancementVO.setStartDate(new Date(rs.getTimestamp("EFFECTIVE_FROM_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_FROM_DATE") != null)

					if(rs.getString("EFFECTIVE_TO_DATE") != null){
						enhancementVO.setEndDate(new Date(rs.getTimestamp("EFFECTIVE_TO_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_TO_DATE") != null)

					enhancementVO.setSumInsured(TTKCommon.checkNull(rs.getString("FLOATER_SUM_INSURED")));
					alResultList.add(enhancementVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getSumInsuredEnhancementList()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getSumInsuredEnhancementList()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getSumInsuredEnhancementList()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getSumInsuredEnhancementList(ArrayList alSearchCriteria)

	/**
	 * This method returns the AuthorizationVO, which contains Authorization Details
	 * @param alAuthList ArrayList object which contains MemberSeqID,PAT_GEN_DETAIL_SEQ_ID,PAT_ENROLL_DETAIL_SEQ_ID and USER_SEQ_ID to get the Authorization Details
	 * @param strIdentifier String which contains module identifer Pre-Authorization/Claims
	 * @return AuthorizationVO object which contains Authorization Details
	 * @exception throws TTKException
	 */
	public AuthorizationVO getAuthorizationDetail(ArrayList alAuthList,String strIdentifier) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		AuthorizationVO authorizationVO = null;
		try{
			conn = ResourceManager.getConnection();

			if(strIdentifier.equals("Pre-Authorization"))
			{
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAuthorizationDetails);
				if(alAuthList.get(0) != null){
					cStmtObject.setLong(1,(Long)alAuthList.get(0));//Mandatory
				}//end of if(alAuthList.get(0) != null)
				else{
					cStmtObject.setString(1,null);
				}//end of else

				cStmtObject.setLong(2,(Long)alAuthList.get(1));//Mandatory
				cStmtObject.setLong(3,(Long)alAuthList.get(2));//Mandatory
				cStmtObject.setLong(4,(Long)alAuthList.get(3));//Mandatory
				cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(5);
			}//end of if(strIdentifier.equals("Pre-Authorization"))
			else if(strIdentifier.equals("Claims"))
			{
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSettlementDetails);
				cStmtObject.setLong(1,(Long)alAuthList.get(1));//Mandatory
				cStmtObject.setLong(2,(Long)alAuthList.get(2));//Mandatory
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			}//end of else if(strIdentifier.equals("Claims"))

			if(rs != null){
				while(rs.next()){
					authorizationVO = new AuthorizationVO();

					if(rs.getString("ASSIGN_USERS_SEQ_ID") != null){
						authorizationVO.setAssignUserSeqID(new Long(rs.getLong("ASSIGN_USERS_SEQ_ID")));
					}//end of if(rs.getString("ASSIGN_USERS_SEQ_ID") != null)
					
					authorizationVO.setSeniorCitizenYN(TTKCommon.checkNull(rs.getString("senior_citizen_yn")));//koc for griavance
					

					if(rs.getString("AVA_SUM_INSURED") != null){
						authorizationVO.setAvailSumInsured(new BigDecimal(rs.getString("AVA_SUM_INSURED")));
					}//end of if(rs.getString("AVA_SUM_INSURED") != null)
					else{
						authorizationVO.setAvailSumInsured(new BigDecimal("0.00"));
					}//end of else

					if(rs.getString("AVA_CUM_BONUS") != null){
						authorizationVO.setAvailCumBonus(new BigDecimal(rs.getString("AVA_CUM_BONUS")));
					}//end of if(rs.getString("AVA_CUM_BONUS") != null)
					else{
						authorizationVO.setAvailCumBonus(new BigDecimal("0.00"));
					}//end of else

					authorizationVO.setStatusTypeID(TTKCommon.checkNull(rs.getString("PAT_STATUS_GENERAL_TYPE_ID")));

					if(rs.getString("BUFF_DETAIL_SEQ_ID") != null){
						authorizationVO.setBufferDtlSeqID(new Long(rs.getLong("BUFF_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("BUFF_DETAIL_SEQ_ID") != null)

					authorizationVO.setAuthorizedBy(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
					authorizationVO.setReasonTypeID(TTKCommon.checkNull(rs.getString("RSON_GENERAL_TYPE_ID")));
					authorizationVO.setAuthPermittedBy(TTKCommon.checkNull(rs.getString("PERMISSION_SOUGHT_FROM")));
					authorizationVO.setDiscPresentYN(TTKCommon.checkNull(rs.getString("DISCRIPENCY_PRESENET_YN")));

					if(rs.getString("COMPLETED_DATE") != null){
						authorizationVO.setAuthorizedDate(new Date(rs.getTimestamp("COMPLETED_DATE").getTime()));
						authorizationVO.setAuthorizedTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("COMPLETED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("COMPLETED_DATE").getTime())).split(" ")[1]:"");
						authorizationVO.setAuthorizedDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("COMPLETED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("COMPLETED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("COMPLETED_DATE") != null)

					if(rs.getString("BALANCE_SEQ_ID") != null){
						authorizationVO.setBalanceSeqID(new Long(rs.getLong("BALANCE_SEQ_ID")));
					}//end of if(rs.getString("BALANCE_SEQ_ID") != null)

					if(rs.getString("DISCOUNT_AMOUNT") != null){
						authorizationVO.setDiscountAmount(new BigDecimal(rs.getString("DISCOUNT_AMOUNT")));
					}//end of if(rs.getString("DISCOUNT_AMOUNT") != null)
					else{
						authorizationVO.setDiscountAmount(new BigDecimal("0.00"));
					}//end of else

					if(rs.getString("CO_PAYMENT_AMOUNT") != null){
						authorizationVO.setCopayAmount(new BigDecimal(rs.getString("CO_PAYMENT_AMOUNT")));
					}//end of if(rs.getString("CO_PAYMENT_AMOUNT") != null)
					else{
						authorizationVO.setCopayAmount(new BigDecimal("0.00"));
					}//end of else

					if(rs.getString("MEMBER_SEQ_ID") != null){
						authorizationVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs.getString("MEMBER_SEQ_ID") != null)

					if(rs.getString("POLICY_SEQ_ID") != null){
						authorizationVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)

					authorizationVO.setEnrolTypeID(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")));

					if(rs.getString("DATE_OF_HOSPITALIZATION") != null){
						authorizationVO.setClaimAdmnDate(new Date(rs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime()));
					}//end of if(rs.getString("DATE_OF_HOSPITALIZATION") != null)					

					if(rs.getString("un_available_suminsured") != null){
						authorizationVO.setUnAvailableSuminsured(new Integer(rs.getInt("un_available_suminsured")));
					}//end of if(rs.getString("un_available_suminsured") != null)

					if(rs.getString("POLICY_GROUP_SEQ_ID") !=null){
						authorizationVO.setPolicyGrpSeqID(new Long(rs.getLong("POLICY_GROUP_SEQ_ID")));
					}//end of if(rs.getString("POLICY_GROUP_SEQ_ID") !=null)

					if(strIdentifier.equals("Pre-Authorization")){
						if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null){
							authorizationVO.setPreAuthSeqID(new Long(rs.getLong("PAT_GEN_DETAIL_SEQ_ID")));
						}//end of if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null)

						if(rs.getString("MEM_TOTAL_SUM_INSURED") != null){
							authorizationVO.setTotalSumInsured(new BigDecimal(rs.getString("MEM_TOTAL_SUM_INSURED")));
						}//end of if(rs.getString("MEM_TOTAL_SUM_INSURED") != null)
						else{
							authorizationVO.setTotalSumInsured(new BigDecimal("0.00"));
						}//end of else

						if(rs.getString("PREV_APPROVED_AMOUNT") != null){
							authorizationVO.setPrevApprovedAmount(new BigDecimal(rs.getString("PREV_APPROVED_AMOUNT")));
						}//end of if(rs.getString("PREV_APPROVED_AMOUNT") != null)
						else{
							authorizationVO.setPrevApprovedAmount(new BigDecimal("0.00"));
						}//end of else

						if(rs.getString("PAT_REQUESTED_AMOUNT") != null){
							authorizationVO.setRequestedAmount(new BigDecimal(rs.getString("PAT_REQUESTED_AMOUNT")));
						}//end of if(rs.getString("PAT_REQUESTED_AMOUNT") != null)
						else{
							authorizationVO.setRequestedAmount(new BigDecimal("0.00"));
						}//end of else

						if(rs.getString("BUFFER_APP_AMOUNT") != null){
							authorizationVO.setApprovedBufferAmount(new BigDecimal(rs.getString("BUFFER_APP_AMOUNT")));
						}//end of if(rs.getString("BUFFER_APP_AMOUNT") != null)
						else{
							authorizationVO.setApprovedBufferAmount(new BigDecimal("0.00"));
						}//end of else

						authorizationVO.setAuthorizationNo(TTKCommon.checkNull(rs.getString("AUTH_NUMBER")));
						authorizationVO.setRemarks(TTKCommon.checkNull(rs.getString("AUTHORIZATION_REMARKS")));

						if(rs.getString("TOTAL_APP_AMOUNT") != null){
							authorizationVO.setApprovedAmount(new BigDecimal(rs.getString("TOTAL_APP_AMOUNT")));
						}//end of if(rs.getString("TOTAL_APP_AMOUNT") != null)
						else{
							authorizationVO.setApprovedAmount(new BigDecimal("0.00"));
						}//end of else

						if(rs.getString("MAX_ALLOWED_AMOUNT") != null){
							authorizationVO.setMaxAllowedAmt(new BigDecimal(rs.getString("MAX_ALLOWED_AMOUNT")));
						}//end of if(rs.getString("MAX_ALLOWED_AMOUNT") != null)
						else{
							authorizationVO.setMaxAllowedAmt(new BigDecimal("0.00"));
						}//end of else

						authorizationVO.setCompletedYN(TTKCommon.checkNull(rs.getString("COMPLETED_YN")));
						authorizationVO.setClauseRemarks(TTKCommon.checkNull(rs.getString("REJECTION_REMARKS")));
						authorizationVO.setAuthLtrTypeID(TTKCommon.checkNull(rs.getString("AUTH_LETTER_GENERAL_TYPE_ID")));
					/**Added as per KOC 1216B Changee request
						 *  
						 **/
						
						//1.	preauth_co_pay_buffer_amount, --display in Pre-Auth Details tab
						//2.	co_payment_buffer_amount     --display in  Claims Deductions tab

					/*	if(rs.getString("PREAUTH_CO_PAY_BUFFER_AMOUNT") != null){
							authorizationVO.setPreCopayBufferamount(new BigDecimal(rs.getString("PREAUTH_CO_PAY_BUFFER_AMOUNT")));
						}//end of if(rs.getString("PREAUTH_CO_PAY_BUFFER_AMOUNT") != null)
						else
						{
							authorizationVO.setPreCopayBufferamount(new BigDecimal("0.00"));
						}//end of else
						*/	
						if(rs.getString("CO_PAYMENT_BUFFER_AMOUNT") != null){
							authorizationVO.setCopayBufferamount(new BigDecimal(rs.getString("CO_PAYMENT_BUFFER_AMOUNT")));
						}//end of if(rs.getString("CO_PAYMENT_BUFFER_AMOUNT") != null)
						else
						{
							authorizationVO.setCopayBufferamount(new BigDecimal("0.00"));
						}//end of else
						//added for Policy Deductable - KOC-1277
						if(rs.getString("BAL_DEDUCTABLE_AMT")!=null)
						{
							authorizationVO.setBalanceDedAmount(new BigDecimal(rs.getString("BAL_DEDUCTABLE_AMT")));
						}
						else
						{
							authorizationVO.setBalanceDedAmount(new BigDecimal("0.00"));
						}
							 //Start Modification As per Koc 1140(Sum Insured Restriction) 
							if(rs.getString("MEM_REST_SUM_INSURED") != null)
							{
							   if(rs.getString("MEM_REST_SUM_INSURED") != "" || rs.getString("MEM_REST_SUM_INSURED") != "0"){
							   authorizationVO.setMemberRestrictedSIAmt(new BigDecimal(rs.getString("MEM_REST_SUM_INSURED")));
							    }
							else{
								authorizationVO.setMemberRestrictedSIAmt(new BigDecimal("0.00"));
								}
						    }//end of if(rs.getString("MEM_REST_SUM_INSURED") != null)
						else{
							authorizationVO.setMemberRestrictedSIAmt(new BigDecimal("0.00"));
							}
						if(rs.getString("AVA_REST_SUM_INSURED") != null)
						{
							if(rs.getString("AVA_REST_SUM_INSURED") != "" || rs.getString("AVA_REST_SUM_INSURED") != "0"){
								authorizationVO.setAvaRestrictedSIAmt(new BigDecimal(rs.getString("AVA_REST_SUM_INSURED")));
								}//end of if(rs.getString("AVA_REST_SUM_INSURED") != null)
						    else{
							authorizationVO.setAvaRestrictedSIAmt(new BigDecimal("0.00"));
						   }//end of else(rs.getString("AVA_REST_SUM_INSURED") != null)
						 }
						else{
							authorizationVO.setAvaRestrictedSIAmt(new BigDecimal("0.00"));
					        }//end of else(rs.getString("AVA_REST_SUM_INSURED") != null
                          if(rs.getString("FAM_REST_YN") != null){
							authorizationVO.setFamRestrictedYN(rs.getString("FAM_REST_YN"));
						     }//end of if(rs.getString("FAM_REST_YN") != null)
						else
						{	
						authorizationVO.setFamRestrictedYN("N");
						}
						/* Added as per KOC 1216B Changee request*/
						if(rs.getString("PAT_INS_STATUS")!=null)
						{
							if(rs.getString("PAT_INS_STATUS").equalsIgnoreCase("INP"))
							{
                                authorizationVO.setInsurerApprovedStatus("Awaiting Insurer Advice");//1274A
							}
							else if(rs.getString("PAT_INS_STATUS").equalsIgnoreCase("REJ"))
							{
								authorizationVO.setInsurerApprovedStatus("Revert received from Insurer");//denial process
							}
							else if(rs.getString("PAT_INS_STATUS").equalsIgnoreCase("APR"))
							{
								authorizationVO.setInsurerApprovedStatus("Revert received from Insurer");//denial process
							}
							else if(rs.getString("PAT_INS_STATUS").equalsIgnoreCase("REQ"))
							{
								authorizationVO.setInsurerApprovedStatus("Revert received from Insurer");
							}
							else{
								authorizationVO.setInsurerApprovedStatus(TTKCommon.checkNull(rs.getString("PAT_INS_STATUS")));	
							}
						}
                        authorizationVO.setInsremarks(TTKCommon.checkNull(rs.getString("PAT_INS_REMARKS")));
                 	//added for CR KOC - Mail-SMS Cigna
						authorizationVO.setMailNotifyYN(TTKCommon.checkNull(rs.getString("FINAL_APP_YN")));
						//added for Mail-SMS for Cigna
						authorizationVO.setCigna_Ins_Cust(TTKCommon.checkNull(rs.getString("Cigna_YN")));
						authorizationVO.setInsDecisionyn(TTKCommon.checkNull(rs.getString("INS_DECISION_YN")));//baja enhan
						authorizationVO.setDenialBanyn(TTKCommon.checkNull(rs.getString("DEN_BAN_YN")));//denial process
  					}//end of if(strIdentifier.equals("Pre-Authorization"))

					else if(strIdentifier.equals("Claims")){

						if(rs.getString("CLAIM_SEQ_ID") != null){
							authorizationVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
						}//end of if(rs.getString("CLAIM_SEQ_ID") != null)

						if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null){
							authorizationVO.setClmEnrollDtlSeqID(new Long(rs.getLong("CLM_ENROLL_DETAIL_SEQ_ID")));
						}//end of if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null)

						if(rs.getString("BUFFER_HDR_SEQ_ID") != null){
							authorizationVO.setBufferHdrSeqID(new Long(rs.getLong("BUFFER_HDR_SEQ_ID")));
						}//end of if(rs.getString("BUFFER_HDR_SEQ_ID") != null)

						if(rs.getString("TOT_SUM_INSURED") != null){
							authorizationVO.setTotalSumInsured(new BigDecimal(rs.getString("TOT_SUM_INSURED")));
						}//end of if(rs.getString("MEM_TOTAL_SUM_INSURED") != null)
						else{
							authorizationVO.setTotalSumInsured(new BigDecimal("0.00"));
						}//end of else

						/*if(rs.getString("BUFFER_APP_AMOUNT") != null){
							authorizationVO.setApprovedBufferAmount(new BigDecimal(rs.getString("BUFFER_APP_AMOUNT")));
						}//end of if(rs.getString("BUFFER_APP_AMOUNT") != null)
						else{
							authorizationVO.setApprovedBufferAmount(new BigDecimal("0.00"));
						}//end of else
						 */
						if(rs.getString("PAT_APPROVED_AMOUNT") != null){
							authorizationVO.setPAApprovedAmt(new BigDecimal(rs.getString("PAT_APPROVED_AMOUNT")));
						}//end of if(rs.getString("PAT_APPROVED_AMOUNT") != null)
						else{
							authorizationVO.setPAApprovedAmt(new BigDecimal("0.00"));
						}//end of else

						if(rs.getString("REQUESTED_AMOUNT") != null){
							authorizationVO.setRequestedAmount(new BigDecimal(rs.getString("REQUESTED_AMOUNT")));
						}//end of if(rs.getString("PAT_REQUESTED_AMOUNT") != null)
						else{
							authorizationVO.setRequestedAmount(new BigDecimal("0.00"));
						}//end of else
						if(rs.getString("SERV_TAX_CALC_AMOUNT")!= null){
							authorizationVO.setTaxAmtPaid(new BigDecimal(rs.getString("SERV_TAX_CALC_AMOUNT")));
						}//end of if(rs.getString("SERV_TAX_CALC_AMOUNT")!= null)
						else{
							authorizationVO.setTaxAmtPaid(new BigDecimal("0.00"));
						}// end of else
						if(rs.getString("SER_TAX_REQ_AMT")!= null){
							authorizationVO.setRequestedTaxAmt(new BigDecimal(rs.getString("SER_TAX_REQ_AMT")));
						}//end of if(rs.getString("SERV_TAX_CALC_AMOUNT")!= null)
						else{
							authorizationVO.setRequestedTaxAmt(new BigDecimal("0.00"));
						}// end of else

						if(rs.getString("TOTAL_APP_AMOUNT") != null){
							authorizationVO.setApprovedAmount(new BigDecimal(rs.getString("TOTAL_APP_AMOUNT")));
						}//end of if(rs.getString("TOTAL_APP_AMOUNT") != null)

						else{
							authorizationVO.setApprovedAmount(new BigDecimal("0.00"));
						}//end of else

						//added for KOC-1273
						authorizationVO.setClaimSubGeneralTypeId(TTKCommon.checkNull(rs.getString("claim_sub_general_type_id")));
                         
						if(rs.getString("MAX_ALLOWED_AMOUNT") != null){
							authorizationVO.setMaxAllowedAmt(new BigDecimal(rs.getString("MAX_ALLOWED_AMOUNT")));
						}//end of if(rs.getString("MAX_ALLOWED_AMOUNT") != null)
						else{
							authorizationVO.setMaxAllowedAmt(new BigDecimal("0.00"));
						}//end of else

						authorizationVO.setAuthorizationNo(TTKCommon.checkNull(rs.getString("CLAIM_SETTLEMENT_NUMBER")));

						if(rs.getString("CLAIM_APP_BUFFER_AMOUNT") != null){
							authorizationVO.setApprovedBufferAmount(new BigDecimal(rs.getString("CLAIM_APP_BUFFER_AMOUNT")));
						}//end of if(rs.getString("BUFFER_APP_AMOUNT") != null)
						else{
							authorizationVO.setApprovedBufferAmount(new BigDecimal("0.00"));
						}//end of else
						if(rs.getString("FINAL_APP_AMOUNT") != null){
							authorizationVO.setFinalApprovedAmt(new BigDecimal(rs.getString("FINAL_APP_AMOUNT")));
						}//end of if(rs.getString("FINAL_APP_AMOUNT") != null)
						else
						{
							authorizationVO.setFinalApprovedAmt(new BigDecimal("0.00"));
						}//end of else

						authorizationVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
						authorizationVO.setClaimSubTypeID(TTKCommon.checkNull(rs.getString("CLAIM_SUB_GENERAL_TYPE_ID")));
						authorizationVO.setCompletedYN(TTKCommon.checkNull(rs.getString("COMPLETED_YN")));
						authorizationVO.setCalcButDispYN(TTKCommon.checkNull(rs.getString("CALC_BUTTON_DISP_YN")));
						authorizationVO.setPressButManYN(TTKCommon.checkNull(rs.getString("PRESS_BUTTON_MAN_YN")));
						//added for Mail-SMS for Cigna
						authorizationVO.setCigna_Ins_Cust(TTKCommon.checkNull(rs.getString("Cigna_YN")));

						if(rs.getString("AVA_DOMICILARY_AMOUNT") != null){
							authorizationVO.setAvailDomTrtLimit(new BigDecimal(rs.getString("AVA_DOMICILARY_AMOUNT")));
						}//end of if(rs.getString("AVA_DOMICILARY_AMOUNT") != null)
						else{
							authorizationVO.setAvailDomTrtLimit(new BigDecimal("0.00"));
						}//end of else
						
						//INVESTIGATION UAT
						if(rs.getString("INV_DISALLOWED_AMT") != null){
							authorizationVO.setInvDisallowdedAmt(new BigDecimal(rs.getString("INV_DISALLOWED_AMT")));
						}//end of if(rs.getString("AVA_DOMICILARY_AMOUNT") != null)
						else{
							authorizationVO.setInvDisallowdedAmt(new BigDecimal("0.00"));
						}//end of else 
						//INVESTIGATION UAT
						authorizationVO.setClmapramt(TTKCommon.checkNull(rs.getString("CLM_APR_AMT")));
					
						authorizationVO.setDVMessageYN(TTKCommon.checkNull(rs.getString("DV_MESSAGE_YN")));
						authorizationVO.setClaimTypeID(TTKCommon.checkNull(rs.getString("CLAIM_GENERAL_TYPE_ID")));

						if(rs.getString("PREAUTH_CO_PAYMENT_AMOUNT") != null){
							authorizationVO.setPACopayAmt(new BigDecimal(rs.getString("PREAUTH_CO_PAYMENT_AMOUNT")));
						}//end of if(rs.getString("PREAUTH_CO_PAYMENT_AMOUNT") != null)
						else{
							authorizationVO.setPACopayAmt(new BigDecimal("0.00"));
						}//end of else

						if(rs.getString("PREAUTH_DISCOUNT_AMOUNT") != null){
							authorizationVO.setPADiscountAmt(new BigDecimal(rs.getString("PREAUTH_DISCOUNT_AMOUNT")));
						}//end of if(rs.getString("PREAUTH_DISCOUNT_AMOUNT") != null)
						else{
							authorizationVO.setPADiscountAmt(new BigDecimal("0.00"));
						}//end of else

						if(rs.getString("DEPOSIT_AMOUNT") != null){
							authorizationVO.setDepositAmt(new BigDecimal(rs.getString("DEPOSIT_AMOUNT")));
						}//end of if(rs.getString("DEPOSIT_AMOUNT") != null)
						else{
							authorizationVO.setDepositAmt(new BigDecimal("0.00"));
						}//end of else
						/**Added as per KOC 1216B Changee request
						 *  
						 **/
						
						//1.	preauth_co_pay_buffer_amount, --display in Pre-Auth Details tab
						//2.	co_payment_buffer_amount     --display in  Claims Deductions tab

						if(rs.getString("PREAUTH_CO_PAY_BUFFER_AMOUNT") != null){
							authorizationVO.setPreCopayBufferamount(new BigDecimal(rs.getString("PREAUTH_CO_PAY_BUFFER_AMOUNT")));
						}//end of if(rs.getString("PREAUTH_CO_PAY_BUFFER_AMOUNT") != null)
						else
						{
							authorizationVO.setPreCopayBufferamount(new BigDecimal("0.00"));
						}//end of else
						
						if(rs.getString("CO_PAYMENT_BUFFER_AMOUNT") != null){
							authorizationVO.setCopayBufferamount(new BigDecimal(rs.getString("CO_PAYMENT_BUFFER_AMOUNT")));
						}//end of if(rs.getString("CO_PAYMENT_BUFFER_AMOUNT") != null)
						else
						{
							authorizationVO.setCopayBufferamount(new BigDecimal("0.00"));
						}//end of else
						//Start Modification As per Koc 1140(Sum Insured Restriction) 
						if(rs.getString("MEM_REST_SUM_INSURED") != null)
							{
								if(rs.getString("MEM_REST_SUM_INSURED") != "" || rs.getString("MEM_REST_SUM_INSURED") != "0"){
						       	authorizationVO.setMemberRestrictedSIAmt(new BigDecimal(rs.getString("MEM_REST_SUM_INSURED")));
     						}
							else{
								authorizationVO.setMemberRestrictedSIAmt(new BigDecimal("0.00"));
							}

						}//end of if(rs.getString("MEM_REST_SUM_INSURED") != null)
						else{
							authorizationVO.setMemberRestrictedSIAmt(new BigDecimal("0.00"));
							}
					    if(rs.getString("AVA_REST_SUM_INSURED") != null)
						{
							if(rs.getString("AVA_REST_SUM_INSURED") != "" || rs.getString("AVA_REST_SUM_INSURED") != "0"){
								authorizationVO.setAvaRestrictedSIAmt(new BigDecimal(rs.getString("AVA_REST_SUM_INSURED")));
							}//end of if(rs.getString("AVA_REST_SUM_INSURED") != null)
					    	else{
							authorizationVO.setAvaRestrictedSIAmt(new BigDecimal("0.00"));
						   }//end of else(rs.getString("AVA_REST_SUM_INSURED") != null)
						}//	if(rs.getString("MEM_REST_SUM_INSURED") != null)?"":""
						else{
							authorizationVO.setAvaRestrictedSIAmt(new BigDecimal("0.00"));
						   }//end of else(rs.getString("AVA_REST_SUM_INSURED") != null)
                          if(rs.getString("FAM_REST_YN") != null){
							authorizationVO.setFamRestrictedYN(rs.getString("FAM_REST_YN"));
							}//end of if(rs.getString("FAM_REST_YN") != null)
						else
						{	authorizationVO.setFamRestrictedYN("N");
							}
						//end Modification As per Koc 1140(Sum Insured Restriction) 
						/** Added as per KOC 1216B Changee request
						 *  
						 **/
						// koc1216A
						authorizationVO.setAdditionalDomicialaryYN(TTKCommon.checkNull(rs.getString("additional_domicilary_yn")));	
						//added for Policy_deductable - KOC-1277 
						if(rs.getString("BAL_DEDUCTABLE_AMT")!=null)
						{
							authorizationVO.setBalanceDedAmount(new BigDecimal(rs.getString("BAL_DEDUCTABLE_AMT")));
						}
						else
						{
							authorizationVO.setBalanceDedAmount(new BigDecimal("0.00"));
						}
						//added for Policy Deductable - KOC-1277.
						authorizationVO.setPolicy_deductable_yn(TTKCommon.checkNull(rs.getString("policy_deductable_yn")));
						authorizationVO.setPreauthDifference(TTKCommon.checkNull(rs.getString("pat_diff")));
						authorizationVO.setPreauthYN(TTKCommon.checkNull(rs.getString("pat_yn")));
						//
						//KOC 1286 for OPD
						authorizationVO.setOPDAmountYN(TTKCommon.checkNull(rs.getString("OPD_BENEFITS_YN")));
						if(rs.getString("CLM_INS_STATUS")!=null)
						{
							if(rs.getString("CLM_INS_STATUS").equalsIgnoreCase("INP"))
							{
                                authorizationVO.setInsurerApprovedStatus("Awaiting Insurer Advice");//1274A
							}
							else if(rs.getString("CLM_INS_STATUS").equalsIgnoreCase("REJ"))
							{
								authorizationVO.setInsurerApprovedStatus("Revert received from Insurer");//denial process
							}
							else if(rs.getString("CLM_INS_STATUS").equalsIgnoreCase("APR"))
							{
								authorizationVO.setInsurerApprovedStatus("Revert received from Insurer");//denial process
							}
							else if(rs.getString("CLM_INS_STATUS").equalsIgnoreCase("REQ"))
							{
								authorizationVO.setInsurerApprovedStatus("Revert received from Insurer");
							}
							else{
								authorizationVO.setInsurerApprovedStatus(TTKCommon.checkNull(rs.getString("CLM_INS_STATUS")));	
							}
						}
						authorizationVO.setInsremarks(TTKCommon.checkNull(rs.getString("CLM_INS_REMARKS")));	
						//added for CR KOC - Mail-SMS Cigna
						authorizationVO.setMailNotifyYN(TTKCommon.checkNull(rs.getString("FINAL_APP_YN")));
						authorizationVO.setInsDecisionyn(TTKCommon.checkNull(rs.getString("INS_DECISION_YN")));//baja enhan
						authorizationVO.setDenialBanyn(TTKCommon.checkNull(rs.getString("DEN_BAN_YN")));//denial process
						//KOC 1286 for OPD						
					}//end of else if(strIdentifier.equals("Claims"))
				}//end of while(rs.next())
			}//end of if(rs != null)
			return authorizationVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getAuthorizationDetail()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getAuthorizationDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getAuthorizationDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getAuthorizationDetail(ArrayList alAuthList)

	/**
	 * This method saves the Authorization information
	 * @param authorizationVO AuthorizationVO contains Authorization information
	 * @param strIdentifier Pre-Authorization/Claims
	 * @return long int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int saveAuthorization(AuthorizationVO authorizationVO,String strIdentifier) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();

			if(strIdentifier.equalsIgnoreCase("Pre-Authorization")){
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveAuthorization);
				if(authorizationVO.getPreAuthSeqID() != null){
					cStmtObject.setLong(1,authorizationVO.getPreAuthSeqID());
				}//end of if(authorizationVO.getPreAuthSeqID() != null)
				else{
					cStmtObject.setString(1,null);
				}//end of else

				if(authorizationVO.getEnrollDtlSeqID() != null){
					cStmtObject.setLong(2,authorizationVO.getEnrollDtlSeqID());
				}//end of if(authorizationVO.getEnrollDtlSeqID() != null)
				else{
					cStmtObject.setString(2,null);
				}//end of else

				cStmtObject.setString(3,authorizationVO.getAuthorizationNo());

				if(authorizationVO.getTotalSumInsured() != null){
					cStmtObject.setBigDecimal(4,authorizationVO.getTotalSumInsured());
				}//end of if(authorizationVO.getTotalSumInsured() != null)
				else{
					cStmtObject.setString(4,null);
				}//en of else

				cStmtObject.setString(5,authorizationVO.getStatusTypeID());
				cStmtObject.setString(6,authorizationVO.getReasonTypeID());
				cStmtObject.setString(7,authorizationVO.getRemarks());

				if(authorizationVO.getAssignUserSeqID() != null){
					cStmtObject.setLong(8,authorizationVO.getAssignUserSeqID());
				}//end of if(authorizationVO.getAssignUserSeqID() != null)
				else{
					cStmtObject.setString(8,null);
				}//end of else

				cStmtObject.setString(9,authorizationVO.getAuthPermittedBy());

				if(authorizationVO.getBalanceSeqID() != null){
					cStmtObject.setLong(10,authorizationVO.getBalanceSeqID());
				}//end of if(authorizationVO.getBalanceSeqID() != null)
				else{
					cStmtObject.setString(10,null);
				}//end of else

				if(authorizationVO.getApprovedAmount() != null){
					cStmtObject.setBigDecimal(11,authorizationVO.getApprovedAmount());
				}//end of if(authorizationVO.getApprovedAmount() != null)
				else{
					cStmtObject.setString(11,null);
				}//end of else

				if(authorizationVO.getDiscountAmount() != null){
					cStmtObject.setBigDecimal(12,authorizationVO.getDiscountAmount());
				}//end of if(authorizationVO.getDiscountAmount() != null)
				else{
					cStmtObject.setString(12,null);
				}//end of else

				if(authorizationVO.getCopayAmount() != null){
					cStmtObject.setBigDecimal(13,authorizationVO.getCopayAmount());
				}//end of if(authorizationVO.getCopayAmount() != null)
				else{
					cStmtObject.setString(13,null);
				}//end of else
				//ADDED AS PER KOC 1216B Change Request on 11 jan 2012
				
			  //  v_co_pay_buff_amount                IN  OUT pat_general_details.co_payment_buffer_amount%TYPE, -- new parameter
				if(authorizationVO.getCopayBufferamount() != null){
					cStmtObject.setBigDecimal(14,authorizationVO.getCopayBufferamount());
				}//end of if(authorizationVO.getCopayBufferamount() != null)
				else{
					cStmtObject.setString(14,null);
				}//end of else
				cStmtObject.setLong(15,authorizationVO.getUpdatedBy());
				
				
				//Start Modification as per KOC 1140(SUM INSURED RESTRICTION)
				if(authorizationVO.getMemberRestrictedSIAmt() != null){
				cStmtObject.setBigDecimal(16,authorizationVO.getMemberRestrictedSIAmt());
				
				}//end of if(authorizationVO.getMemberRestrictedSIAmt() != null)
				else{
				cStmtObject.setString(16,null);
				}//end of else getMemberRestrictedSIAmt
				 if(authorizationVO.getAvaRestrictedSIAmt() != null){
				cStmtObject.setBigDecimal(17,authorizationVO.getAvaRestrictedSIAmt());
				
				}//end of if(authorizationVO.getAvaRestrictedSIAmt() != null)
				else{
					cStmtObject.setString(17,null);
				}//end of else getAvaRestrictedSIAmt
        		if(authorizationVO.getFamRestrictedYN() != null){
				cStmtObject.setString(18,authorizationVO.getFamRestrictedYN());
				
				}//end of if(authorizationVOgetFamRestrictedYN() != null)
				  else{
						cStmtObject.setString(18,null);
					}//end of else getFamRestrictedYN
				//END Modification as per KOC 1140(SUM INSURED RESTRICTION)

        		
        		//added for Policy deductable - KOC-1277
				if(authorizationVO.getBalanceDedAmount()!=null)
				{
					cStmtObject.setBigDecimal(19,authorizationVO.getBalanceDedAmount());
				}
				else
				{
					cStmtObject.setString(19,null);
                }
                        		//added for CR KOC Mail-SMS for Cigna
        		
                cStmtObject.setString(20,authorizationVO.getMailNotifyYN());
                cStmtObject.registerOutParameter(21,Types.INTEGER);
				cStmtObject.execute();
                iResult = cStmtObject.getInt(21);
				/*cStmtObject.registerOutParameter(16,Types.INTEGER);
				cStmtObject.execute();
				iResult = cStmtObject.getInt(16);*/
			}//end of if(strIdentifier.equalsIgnoreCase("Pre-Authorization"))

			else if(strIdentifier.equalsIgnoreCase("Claims")){

				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveSettlement);
				if(authorizationVO.getAssignUserSeqID() != null){
					cStmtObject.setLong(1,authorizationVO.getAssignUserSeqID());
				}//end of if(authorizationVO.getAssignUserSeqID() != null)
				else{
					cStmtObject.setString(1,null);
				}//end of else

				if(authorizationVO.getClaimSeqID() != null){
					cStmtObject.setLong(2,authorizationVO.getClaimSeqID());
				}//end of if(authorizationVO.getClaimSeqID() != null)
				else{
					cStmtObject.setString(2,null);
				}//end of else

				if(authorizationVO.getTotalSumInsured() != null){
					cStmtObject.setBigDecimal(3,authorizationVO.getTotalSumInsured());
				}//end of if(authorizationVO.getTotalSumInsured() != null)
				else{
					cStmtObject.setString(3,null);
				}//end of else

				cStmtObject.setString(4,authorizationVO.getRemarks());
				cStmtObject.setString(5,authorizationVO.getAuthorizationNo());
				cStmtObject.setString(6,authorizationVO.getStatusTypeID());
				cStmtObject.setString(7,authorizationVO.getAuthPermittedBy());

				if(authorizationVO.getBalanceSeqID() != null){
					cStmtObject.setLong(8,authorizationVO.getBalanceSeqID());
				}//end of if(authorizationVO.getBalanceSeqID() != null)
				else{
					cStmtObject.setString(8,null);
				}//end of else

				if(authorizationVO.getApprovedAmount() != null){
					cStmtObject.setBigDecimal(9,authorizationVO.getApprovedAmount());
				}//end of if(authorizationVO.getApprovedAmount() != null)
				else{
					cStmtObject.setString(9,null);
				}//end of else

				cStmtObject.setLong(10,authorizationVO.getUpdatedBy());
				cStmtObject.setString(11,authorizationVO.getReasonTypeID());

				if(authorizationVO.getDiscountAmount() != null){
					cStmtObject.setBigDecimal(12,authorizationVO.getDiscountAmount());
				}//end of if(authorizationVO.getDiscountAmount() != null)
				else{
					cStmtObject.setString(12,null);
				}//end of else

				if(authorizationVO.getCopayAmount() != null){
					cStmtObject.setBigDecimal(13,authorizationVO.getCopayAmount());
				}//end of if(authorizationVO.getCopayAmount() != null)
				else{
					cStmtObject.setString(13,null);
				}//end of else

				if(authorizationVO.getDepositAmt() != null){
					cStmtObject.setBigDecimal(14,authorizationVO.getDepositAmt());
				}//end of if(authorizationVO.getDepositAmt() != null)
				else{
					cStmtObject.setString(14,null);
				}//end of else
				if(authorizationVO.getTaxAmtPaid() != null){
					cStmtObject.setBigDecimal(15,authorizationVO.getTaxAmtPaid());
				}//end of if(authorizationVO.getTaxAmtPaid() != null)
				else 
				{
					cStmtObject.setString(15,null);
				}//end of else
				if(authorizationVO.getSerTaxCalPer() != null){
					cStmtObject.setBigDecimal(16,authorizationVO.getSerTaxCalPer());
				}//end of if(authorizationVO.getSerTaxCalPer() != null)
				else
				{
					cStmtObject.setString(16,null);
				}//end of else

				 //Added as per KOC 1216B Change Request in claims 
				
				if(authorizationVO.getCopayBufferamount() != null){
					cStmtObject.setBigDecimal(17,authorizationVO.getCopayBufferamount());
				}//end of if(authorizationVO.getCopayBufferamount() != null)
				else
				{
					cStmtObject.setString(17,null);
				}//end of else
				
				//Start Modification as per KOC 1140(SUM INSURED RESTRICTION)
				if(authorizationVO.getMemberRestrictedSIAmt() != null){
					cStmtObject.setBigDecimal(18,authorizationVO.getMemberRestrictedSIAmt());
					
					}//end of if(authorizationVO.getMemberRestrictedSIAmt() != null)
					else{
					cStmtObject.setString(18,null);
					}//end of else getMemberRestrictedSIAmt
				if(authorizationVO.getAvaRestrictedSIAmt() != null){
					cStmtObject.setBigDecimal(19,authorizationVO.getAvaRestrictedSIAmt());
				     }//end of if(authorizationVO.getAvaRestrictedSIAmt() != null)
				else{
					cStmtObject.setString(19,null);
				     }//end of else getAvaRestrictedSIAmt
				  if(authorizationVO.getFamRestrictedYN() != null){
					cStmtObject.setString(20,authorizationVO.getFamRestrictedYN());
					}//end of if(authorizationVOgetFamRestrictedYN() != null)
				  else{
					cStmtObject.setString(20,null);
					}//end of else getFamRestrictedYN
                  

				//added for POlicy Deductable - KOC-1277
				if(authorizationVO.getBalanceDedAmount()!=null)
				{
					cStmtObject.setBigDecimal(21,authorizationVO.getBalanceDedAmount());
					}
				else
				{
					cStmtObject.setString(21,null);
				}
                                   
                  //added for Mail-SMS Template for Cigna
                
                cStmtObject.setString(22,authorizationVO.getMailNotifyYN()); 

                if(authorizationVO.getInvDisallowdedAmt()!= null){
                    cStmtObject.setBigDecimal(23,authorizationVO.getInvDisallowdedAmt());
				     }//end of if(authorizationVO.getAvaRestrictedSIAmt() != null)
				else{
                    cStmtObject.setString(23,null);
				     }//end of else getAvaRestrictedSIAmt
					
                 cStmtObject.registerOutParameter(24,Types.INTEGER);
                                      
                 
				cStmtObject.execute();
                iResult = cStmtObject.getInt(24);
				//END Modification as per KOC 1140(SUM INSURED RESTRICTION)
				
				/*cStmtObject.registerOutParameter(18,Types.INTEGER);
				cStmtObject.execute();
				iResult = cStmtObject.getInt(18);*/
			}//end of else
			  //Added as per KOC 1216B Change Request in claims 
			}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl saveAuthorization()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl saveAuthorization()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of saveAuthorization(AuthorizationVO authorizationVO)

	/**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	
    
	public int deletePATGeneral(ArrayList alDeleteList) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeletePATGeneral);
			cStmtObject.setString(1, (String)alDeleteList.get(0));//FLAG PAT/AIL/SFL/INV/PED/COU/BUFFER
			cStmtObject.setString(2, (String)alDeleteList.get(1));//CONCATENATED STRING OF  delete SEQ_IDS

			if(alDeleteList.get(2) != null){
				cStmtObject.setLong(3,(Long)alDeleteList.get(2));//Mandatory  PAT_ENROLL_DETAIL_SEQ_ID
			}//end of if(alDeleteList.get(2) != null)
			else{
				cStmtObject.setString(3,null);
			}//end of else

			if(alDeleteList.get(3) != null){
				cStmtObject.setLong(4,(Long)alDeleteList.get(3));//Mandatory  PAT_GENERAL_DETAIL_SEQ_ID
			}//end of if(alDeleteList.get(2) != null)
			else{
				cStmtObject.setString(4,null);
			}//end of else

			cStmtObject.setLong(5,(Long)alDeleteList.get(4));//ADDED_BY
			cStmtObject.registerOutParameter(6, Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.execute();
			iResult = cStmtObject.getInt(6);//ROWS_PROCESSED
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl deletePATGeneral()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl deletePATGeneral()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of deletePATGeneral(ArrayList alDeleteList)

	/**
	 * This method returns the ArrayList, which contains Discrepancy Details
	 * @param alDiscrepancyList lwhich contains PAT_GEN_DETAIL_SEQ_ID,CLAIM_SEQ_ID,USER_SEQ_ID to get the Discrepancy Details
	 * @return ArrayList object which contains Discrepancy Details
	 * @exception throws TTKException
	 */
	public ArrayList getDiscrepancyInfo(ArrayList alDiscrepancyList) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		DiscrepancyVO discrepancyVO = null;
		ArrayList<Object> alDiscrepancyInfo = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDiscrepancy);

			if(alDiscrepancyList.get(0) != null){
				cStmtObject.setLong(1,(Long)alDiscrepancyList.get(0));
			}//end of if(alDiscrepancyList.get(0) != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else

			if(alDiscrepancyList.get(1) != null){
				cStmtObject.setLong(2,(Long)alDiscrepancyList.get(1));
			}//end of if(alDiscrepancyList.get(1) != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			cStmtObject.setLong(3,(Long)alDiscrepancyList.get(2));
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(4);
			alDiscrepancyInfo= new ArrayList<Object>();
			if(rs != null){
				while(rs.next()){
					discrepancyVO = new DiscrepancyVO();

					if(rs.getString("DISCREPANCY_SEQ_ID") != null){
						discrepancyVO.setDiscrepancySeqID(new Long(rs.getLong("DISCREPANCY_SEQ_ID")));
					}//end of if(rs.getString("DISCREPANCY_SEQ_ID") != null)

					discrepancyVO.setDiscrepancyTypeID(TTKCommon.checkNull(rs.getString("GENERAL_TYPE_ID")));

					if(rs.getString("REMARKS") != null){
						discrepancyVO.setDiscrepancy(TTKCommon.checkNull(rs.getString("DISCREPANCY")).concat(" - ").concat(rs.getString("REMARKS")));
					}//end of if(rs.getString("REMARKS") != null)
					else{
						discrepancyVO.setDiscrepancy(TTKCommon.checkNull(rs.getString("DISCREPANCY")));
					}//end of else

					discrepancyVO.setResolvedYN(TTKCommon.checkNull(rs.getString("RESOLVED_YN")));
					discrepancyVO.setResolvableYN(TTKCommon.checkNull(rs.getString("RESOLVABLE_YN")));
					alDiscrepancyInfo.add(discrepancyVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getDiscrepancyInfo()",sqlExp);
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
						log.error("Error while closing the Statement in PreAuthDAOImpl getDiscrepancyInfo()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl getDiscrepancyInfo()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return alDiscrepancyInfo;
	}//end of getDiscrepancyInfo(ArrayList alDiscrepancyList)

	/**
	 * This method resolves the Discrepancy information
	 * @param alDiscrepancyVO ArrayList contains Discrepancy information
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int resolveDiscrepancy(ArrayList alDiscrepancyVO) throws TTKException {
		int iResult = 1;
		StringBuffer sbfSQL = null;
		Statement stmt = null;
		DiscrepancyVO discrepancyVO = null;
		Connection conn = null;
		try{
			conn = ResourceManager.getConnection();
			conn.setAutoCommit(false);
			stmt = (java.sql.Statement)conn.createStatement();
			if(alDiscrepancyVO != null){
				for(int i=0;i<alDiscrepancyVO.size();i++){
					sbfSQL = new StringBuffer();
					discrepancyVO = (DiscrepancyVO)alDiscrepancyVO.get(i);
					sbfSQL = sbfSQL.append("'"+discrepancyVO.getDiscrepancySeqID()+"',");
					sbfSQL = sbfSQL.append("'"+discrepancyVO.getResolvedYN()+"',");
					sbfSQL = sbfSQL.append("'"+discrepancyVO.getUpdatedBy()+"')}");
					stmt.addBatch(strResolveDiscrepancy+sbfSQL.toString());
				}//end of for
			}//end of if(alDiscrepancyVO != null)
			stmt.executeBatch();
			conn.commit();
		}//end of try
		catch (SQLException sqlExp)
		{
			try {
				conn.rollback();
				throw new TTKException(sqlExp, "preauth");
			} //end of try
			catch (SQLException sExp) {
				throw new TTKException(sExp, "preauth");
			}//end of catch (SQLException sExp)
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			try {
				conn.rollback();
				throw new TTKException(exp, "preauth");
			} //end of try
			catch (SQLException sqlExp) {
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the Statement
			{
				try
				{
					if (stmt != null) stmt .close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl resolveDiscrepancy()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl resolveDiscrepancy()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				stmt  = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of resolveDiscrepancy(ArrayList alDiscrepancyVO)

	/**
	 * This method will save the Review Information and Workflow will change to Coding
	 * @param preauthDetailVO the object which contains the Review Details which has to be  saved
	 * @param strMode String object which contains Mode Pre-authorization/Claims - PAT/CLM
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
	public PreAuthDetailVO returnToCoding(PreAuthDetailVO preauthDetailVO,String strMode) throws TTKException {
		String strReview = "";
		Long lngEventSeqID = null;
		Integer intReviewCount = null;
		Integer intRequiredReviewCnt = null;
		String strEventName = "";
		String strCodingReviewYN = "";
		Connection conn = null;
		CallableStatement cStmtObject=null;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReturnToCoding);

			if(strMode.equals("PAT")){
				if(preauthDetailVO.getPreAuthSeqID() != null){
					cStmtObject.setLong(1,preauthDetailVO.getPreAuthSeqID());
				}//end of if(preauthDetailVO.getPreAuthSeqID() != null)
				else{
					cStmtObject.setString(1,null);
				}//end of else
			}//end of if(strMode.equals("PAT"))
			else{
				if(preauthDetailVO.getClaimSeqID() != null){
					cStmtObject.setLong(1,preauthDetailVO.getClaimSeqID());
				}//end of if(preauthDetailVO.getClaimSeqID() != null)
				else{
					cStmtObject.setString(1,null);
				}//end of else
			}//end of else

			cStmtObject.setString(2,strMode);
			cStmtObject.setLong(3,preauthDetailVO.getUpdatedBy());

			if(preauthDetailVO.getEventSeqID() != null){
				cStmtObject.setLong(4,preauthDetailVO.getEventSeqID());
			}//end of if(preauthDetailVO.getEventSeqID() != null)
			else{
				cStmtObject.setString(4,null);
			}//end of else

			if(preauthDetailVO.getReviewCount() != null){
				cStmtObject.setInt(5,preauthDetailVO.getReviewCount());
			}//end of if(preauthDetailVO.getReviewCount() != null)
			else{
				cStmtObject.setString(5,null);
			}//end of else

			if(preauthDetailVO.getRequiredReviewCnt() != null){
				cStmtObject.setLong(6,preauthDetailVO.getRequiredReviewCnt());
			}//end of if(preauthDetailVO.getRequiredReviewCnt() != null)
			else{
				cStmtObject.setString(6,null);
			}//end of else

			cStmtObject.registerOutParameter(4,Types.BIGINT);//EVENT_SEQ_ID
			cStmtObject.registerOutParameter(5,Types.BIGINT);//REVIEW_COUNT
			cStmtObject.registerOutParameter(6,Types.BIGINT);//REQUIRED_REVIEW_COUNT
			cStmtObject.registerOutParameter(7,Types.VARCHAR);//EVENT_NAME
			cStmtObject.registerOutParameter(8,Types.VARCHAR);//REVIEW
			cStmtObject.registerOutParameter(9,Types.VARCHAR);//CODING_REVIEW_YN
			cStmtObject.execute();

			lngEventSeqID = cStmtObject.getLong(4);
			intReviewCount = cStmtObject.getInt(5);
			intRequiredReviewCnt = cStmtObject.getInt(6);
			strEventName = cStmtObject.getString(7);
			strReview = cStmtObject.getString(8);
			strCodingReviewYN = cStmtObject.getString(9);

			preauthDetailVO.setEventSeqID(lngEventSeqID);
			preauthDetailVO.setReviewCount(intReviewCount);
			preauthDetailVO.setRequiredReviewCnt(intRequiredReviewCnt);
			preauthDetailVO.setEventName(strEventName);
			preauthDetailVO.setReview(strReview);
			preauthDetailVO.setCoding_review_yn(strCodingReviewYN);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl returnToCoding()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl returnToCoding()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return preauthDetailVO;
	}//end of returnToCoding(PreAuthDetailVO preauthDetailVO,String strMode)

	/**
	 * This method reassign the enrollment ID
	 * @param alReAssignEnrID the arraylist of details for which have to be reassigned
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int reAssignEnrID(ArrayList alReAssignEnrID) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReAssignEnrID);
			cStmtObject.setLong(1,(Long)alReAssignEnrID.get(0));//PAT_GEN_DETAIL_SEQ_ID
			cStmtObject.setLong(2,(Long)alReAssignEnrID.get(1));//PAT_ENROLL_DETAIL_SEQ_ID			
			cStmtObject.setString(3,(String)alReAssignEnrID.get(2));//PAT_STATUS_GENERAL_TYPE_ID 
			cStmtObject.setLong(4,(Long)alReAssignEnrID.get(3));//MEMBER_SEQ_ID              
			cStmtObject.setLong(5,(Long)alReAssignEnrID.get(4));//ADDED_BY
			cStmtObject.registerOutParameter(6, Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.execute();
			iResult = cStmtObject.getInt(6);//ROWS_PROCESSED
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl reAssignEnrID()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl reAssignEnrID()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of reAssignEnrID(ArrayList alReAssignEnrID)

	/**
	 * This method returns the SIInfoVO, which contains Balance SI Details.
	 * @param lngMemSeqID long value which contains member id to get the Balance SI Details
	 * @param lngPolicyGrpSeqID long value which contains Policy Group Seq ID
	 * @param lngBalanceSeqID long value which contains Balance Seq ID
	 * @return PreAuthMedicalVO object which contains Balance SI Details
	 * @exception throws TTKException
	 */
	public SIInfoVO getBalanceSIDetail(long lngMemSeqID,long lngPolicyGrpSeqID,long lngBalanceSeqID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		//Added as per KOC 1216B change request
		ResultSet rs4 = null;
		ResultSet rs5 = null;////koc1289_1272
		MemberBufferVO memberBufferVO=null;
		//Added as per KOC 1216B change request
		SIInfoVO siInfoVO = new SIInfoVO();
	    BalanceSIInfoVO balanceSIInfoVO = null;
		SIBreakupInfoVO siBreakupInfoVO = null;
		StopPreauthClaimVO stopPreClmVO = null;  
        RestorationPreauthClaimVO restorationPreauthClaimVO = null;  
        ArrayList<Object> alSIBreakupList = new ArrayList<Object>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strBalanceSIInfo);

			cStmtObject.setLong(1,lngMemSeqID);
			cStmtObject.setLong(2,lngPolicyGrpSeqID);
			cStmtObject.setLong(3,lngBalanceSeqID);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			//Added as per kOC 1216 b Change request
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);//koc1289_1272
			//Added as per kOC 1216 b Change request
			cStmtObject.execute();
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(4);
			rs2 = (java.sql.ResultSet)cStmtObject.getObject(5);
			rs3 = (java.sql.ResultSet)cStmtObject.getObject(6);
			//Added as per kOC 1216 b Change request
			rs4 = (java.sql.ResultSet)cStmtObject.getObject(7);
			//Added as per kOC 1216 b Change request
            rs5 = (java.sql.ResultSet)cStmtObject.getObject(8);//koc1289_1272
			if(rs1 != null){
				while(rs1.next()){
					balanceSIInfoVO = new BalanceSIInfoVO();

					if(rs1.getString("SUM_INSURED") != null){
						balanceSIInfoVO.setTotalSumInsured(new BigDecimal(rs1.getString("SUM_INSURED")));
					}//end of if(rs1.getString("SUM_INSURED") != null)
					else{
						balanceSIInfoVO.setTotalSumInsured(new BigDecimal("0.00"));
					}//end of else

					if(rs1.getString("BONUS") != null){
						balanceSIInfoVO.setBonus(new BigDecimal(rs1.getString("BONUS")));
					}//end of if(rs1.getString("BONUS") != null)
					else{
						balanceSIInfoVO.setBonus(new BigDecimal("0.00"));
					}//end of else

					if(rs1.getString("BUFFER_AMOUNT") != null){
						balanceSIInfoVO.setBufferAmt(new BigDecimal(rs1.getString("BUFFER_AMOUNT")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						balanceSIInfoVO.setBufferAmt(new BigDecimal("0.00"));
					}//end of else

					if(rs1.getString("UTILISED_SUM_INSURED") != null){
						balanceSIInfoVO.setUtilizedSumInsured(new BigDecimal(rs1.getString("UTILISED_SUM_INSURED")));
					}//end of if(rs1.getString("UTILISED_SUM_INSURED") != null)
					else{
						balanceSIInfoVO.setUtilizedSumInsured(new BigDecimal("0.00"));
					}//end of else

					if(rs1.getString("UTILISED_CUM_BONUS") != null){
						balanceSIInfoVO.setUtilizedBonus(new BigDecimal(rs1.getString("UTILISED_CUM_BONUS")));
					}//end of if(rs1.getString("UTILISED_CUM_BONUS") != null)
					else{
						balanceSIInfoVO.setUtilizedBonus(new BigDecimal("0.00"));
					}//end of else

					balanceSIInfoVO.setPolSubType(TTKCommon.checkNull(rs1.getString("POLICY_SUB_TYPE")));
					balanceSIInfoVO.setBufferAllocation(TTKCommon.checkNull(rs1.getString("BUFFER_ALLOCATION")));

						//added for Policy Deductable - KOC-1277
					if(rs1.getString("Policy_deductable_amt")!=null)
					{
						balanceSIInfoVO.setPolicyDeductableAmt(new BigDecimal(rs1.getString("POLICY_DEDUCTABLE_AMT")));	
					}
					balanceSIInfoVO.setUtilizedPolicyDedAmt(new BigDecimal(rs1.getString("UTILISED_POLICY_DEDUCTABLE_AMT")));
					//ended
					
					//added for hyundai buffer cr
					
					///set1
					if(rs1.getString("BUFFER_AMOUNT") != null){
						balanceSIInfoVO.setNorCorpusBufAmt(new BigDecimal(rs1.getString("BUFFER_AMOUNT")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						balanceSIInfoVO.setNorCorpusBufAmt(new BigDecimal("0.00"));
					}//end of else///set2
					
					
					if(rs1.getString("MED_BUFFER_AMOUNT") != null){
						balanceSIInfoVO.setNorMedicalBufAmt(new BigDecimal(rs1.getString("MED_BUFFER_AMOUNT")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						balanceSIInfoVO.setNorMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else
					
					if(rs1.getString("CRITICAL_CORP_BUFF_AMOUNT") != null){
						balanceSIInfoVO.setCriCorpusBufAmt(new BigDecimal(rs1.getString("CRITICAL_CORP_BUFF_AMOUNT")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						balanceSIInfoVO.setCriCorpusBufAmt(new BigDecimal("0.00"));
					}//end of else
					
					
					if(rs1.getString("CRITICAL_MED_BUFF_AMOUNT") != null){
						balanceSIInfoVO.setCriMedicalBufAmt(new BigDecimal(rs1.getString("CRITICAL_MED_BUFF_AMOUNT")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						balanceSIInfoVO.setCriMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else
					
					
					if(rs1.getString("CRITICAL_BUFF_AMOUNT") != null){
						balanceSIInfoVO.setCriIllnessBufAmt(new BigDecimal(rs1.getString("CRITICAL_BUFF_AMOUNT")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						balanceSIInfoVO.setCriIllnessBufAmt(new BigDecimal("0.00"));
					}//end of else
					
					//////////////////////////////////
					
					if(rs1.getString("UTILISED_BUFFER") != null){
						balanceSIInfoVO.setUtilizedBufferAmt(new BigDecimal(rs1.getString("UTILISED_BUFFER")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						balanceSIInfoVO.setUtilizedBufferAmt(new BigDecimal("0.00"));
					}//end of else	
					
					if(rs1.getString("UTILISED_MED_BUFFER") != null){
						balanceSIInfoVO.setUtilizedNorMedicalBufAmt(new BigDecimal(rs1.getString("UTILISED_MED_BUFFER")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						balanceSIInfoVO.setUtilizedNorMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					if(rs1.getString("UTILISED_CRIT_BUFFER") != null){
						balanceSIInfoVO.setUtilizedCriIllnessBufAmt(new BigDecimal(rs1.getString("UTILISED_CRIT_BUFFER")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						balanceSIInfoVO.setUtilizedCriIllnessBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					///set2
					
					///set3
					
					if(rs1.getString("UTILISED_CRIT_CORP_BUFFER") != null){
						balanceSIInfoVO.setUtilizedCriCorpusBufAmt(new BigDecimal(rs1.getString("UTILISED_CRIT_CORP_BUFFER")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						balanceSIInfoVO.setUtilizedCriCorpusBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					///set3
					///set4
					
					if(rs1.getString("UTILISED_CRIT_MED_BUFFER") != null){
						balanceSIInfoVO.setUtilizedCriMedicalBufAmt(new BigDecimal(rs1.getString("UTILISED_CRIT_MED_BUFFER")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						balanceSIInfoVO.setUtilizedCriMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					///set4
					///set5
				
					
					///set5
					//end added for hyundai buffer cr
				}//end of while(rs1.next())
				siInfoVO.setBalSIInfoVO(balanceSIInfoVO);
			}//end of if(rs1 != null)

			if(rs2 != null){
				while(rs2.next()){
					siBreakupInfoVO = new SIBreakupInfoVO();

					siBreakupInfoVO.setRowNbr(TTKCommon.checkNull(rs2.getString("RNO")));

					if(rs2.getString("MEM_SUM_INSURED") != null){
						siBreakupInfoVO.setMemSumInsured(new BigDecimal(rs2.getString("MEM_SUM_INSURED")));
					}//end of if(rs2.getString("MEM_SUM_INSURED") != null)
					else{
						siBreakupInfoVO.setMemSumInsured(new BigDecimal("0.00"));
					}//end of else

					if(rs2.getString("MEM_BONUS_AMOUNT") != null){
						siBreakupInfoVO.setMemBonus(new BigDecimal(rs2.getString("MEM_BONUS_AMOUNT")));
					}//end of if(rs2.getString("MEM_BONUS_AMOUNT") != null)
					else{
						siBreakupInfoVO.setMemBonus(new BigDecimal("0.00"));
					}//end of else

					if(rs2.getString("EFFECTIVE_DATE") != null){
						siBreakupInfoVO.setSIEffDate(new Date(rs2.getTimestamp("EFFECTIVE_DATE").getTime()));
					}//end of if(rs2.getString("EFFECTIVE_DATE") != null)

					alSIBreakupList.add(siBreakupInfoVO);
				}//end of while(rs2.next())
				siInfoVO.setSIBreakupList(alSIBreakupList);
			}//end of if(rs2 != null)

			if(rs3 != null){
				while(rs3.next()){		
					stopPreClmVO = new StopPreauthClaimVO();
					stopPreClmVO.setStopPatClmYN(rs3.getString("STOP_PAT_CLM_PROCESS_YN"));
					if(rs3.getString("RECIEVED_AFTER") != null){
						stopPreClmVO.setPatClmRcvdAftr(new Date(rs3.getTimestamp("RECIEVED_AFTER").getTime()));
					}//end of if(rs3.getString("PAT_CLM_RECIEVED_AFTER") != null){
				}//end of while(rs2.next())				
				siInfoVO.setStopPreClmVO(stopPreClmVO);
			}//end of if(rs3 != null)
			//Added as per KOC 1216B Change Request
			if(rs4 != null){
				while(rs4.next()){		
					memberBufferVO = new MemberBufferVO();
					memberBufferVO.setMemberBufferYN(rs4.getString("MEMBER_BUFFER_YN"));//member_buffer_yn
					
					
					
					//Mem_Buffer_Alloc
					if(rs4.getString("MEM_BUFFER_ALLOC") != null){
						memberBufferVO.setBufferAmtMember(new BigDecimal(rs4.getString("MEM_BUFFER_ALLOC")));
					}//end of if(rs4.getString("MEM_BUFFER_ALLOC") != null)
					else{
						memberBufferVO.setBufferAmtMember(new BigDecimal("0.00"));
					}//end of else
					
//added for hyundai buffer cr
					
					
					
					///set2
					if(rs4.getString("MEM_MED_BUFFER_ALLOC") != null){
						memberBufferVO.setNorMedicalBufAmt(new BigDecimal(rs4.getString("MEM_MED_BUFFER_ALLOC")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						memberBufferVO.setNorMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else
					
					
					log.info("getting form value"+memberBufferVO.getNorMedicalBufAmt());
					if(rs4.getString("USED_MED_BUFF_AMOUNT") != null){
						memberBufferVO.setUtilizedNorMedicalBufAmt(new BigDecimal(rs4.getString("USED_MED_BUFF_AMOUNT")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						memberBufferVO.setUtilizedNorMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					///set2
					
					///set3
					if(rs4.getString("MEM_CRIT_CORP_BUFF_ALLOC") != null){
						memberBufferVO.setCriCorpusBufAmt(new BigDecimal(rs4.getString("MEM_CRIT_CORP_BUFF_ALLOC")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						memberBufferVO.setCriCorpusBufAmt(new BigDecimal("0.00"));
					}//end of else
					if(rs4.getString("USED_CRIT_CORP_BUFF_AMOUNT") != null){
						memberBufferVO.setUtilizedCriCorpusBufAmt(new BigDecimal(rs4.getString("USED_CRIT_CORP_BUFF_AMOUNT")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						memberBufferVO.setUtilizedCriCorpusBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					///set3
					///set4
					if(rs4.getString("MEM_CRIT_MED_BUFF_ALLOC") != null){
						memberBufferVO.setCriMedicalBufAmt(new BigDecimal(rs4.getString("MEM_CRIT_MED_BUFF_ALLOC")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						memberBufferVO.setCriMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else
					if(rs4.getString("USED_CRIT_MED_BUFF_AMOUNT") != null){
						memberBufferVO.setUtilizedCriMedicalBufAmt(new BigDecimal(rs4.getString("USED_CRIT_MED_BUFF_AMOUNT")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						memberBufferVO.setUtilizedCriMedicalBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					///set4
					///set5
					if(rs4.getString("MEM_CRIT_BUFF_ALLOC") != null){
						memberBufferVO.setCriIllnessBufAmt(new BigDecimal(rs4.getString("MEM_CRIT_BUFF_ALLOC")));
					}//end of if(rs1.getString("BUFFER_AMOUNT") != null)
					else{
						memberBufferVO.setCriIllnessBufAmt(new BigDecimal("0.00"));
					}//end of else
					if(rs4.getString("USED_CRIT_BUFF_AMOUNT") != null){
						memberBufferVO.setUtilizedCriIllnessBufAmt(new BigDecimal(rs4.getString("USED_CRIT_BUFF_AMOUNT")));
					}//end of if(rs1.getString("UTILISED_BUFFER") != null)
					else{
						memberBufferVO.setUtilizedCriIllnessBufAmt(new BigDecimal("0.00"));
					}//end of else	
					
					///set5
					//end added for hyundai buffer cr
					//used_buff_amount
					if(rs4.getString("USED_BUFF_AMOUNT") != null){
						memberBufferVO.setUtilizedBufferAmtMember(new BigDecimal(rs4.getString("USED_BUFF_AMOUNT")));
					}//end of if(rs4.getString("USED_BUFF_AMOUNT") != null)
					else{
						memberBufferVO.setUtilizedBufferAmtMember(new BigDecimal("0.00"));
					}//end of else
				}//end of while(rs4.next())				
				siInfoVO.setMemberBufferVO(memberBufferVO);
			}//end of if(rs4 != null)
			
			// added by rekha koc1289_1272
			if (rs5 == null ) {
                restorationPreauthClaimVO = new RestorationPreauthClaimVO();
			    restorationPreauthClaimVO.setRestorationYN("N");
			    restorationPreauthClaimVO.setRestSumInsured(new BigDecimal("0.00"));
			    log.info("RESTORATION_YN---------------------"+restorationPreauthClaimVO.getRestorationYN());
			    log.info("restoration---------------------"+restorationPreauthClaimVO.getRestSumInsured());
			    siInfoVO.setRestorationPreauthClaimVO(restorationPreauthClaimVO);
			} 
			else if(rs5 != null){
                if (rs5.next()){        
                restorationPreauthClaimVO = new RestorationPreauthClaimVO(); 
                if(rs5.getString("RESTORATION_YN") != null){
				restorationPreauthClaimVO.setRestorationYN(rs5.getString("RESTORATION_YN").trim());
				
				}
				else{
					restorationPreauthClaimVO.setRestorationYN("N");
				
				}
				if(rs5.getString("MEM_SUM_INSURED") != null){
					
					restorationPreauthClaimVO.setRestSumInsured(new BigDecimal(rs5.getString("MEM_SUM_INSURED")));
				}//end of if(rs5.getString("PAT_CLM_RECIEVED_AFTER") != null){
			}//end of while(rs5.next())	
				else
				{
                            restorationPreauthClaimVO = new RestorationPreauthClaimVO();
						    restorationPreauthClaimVO.setRestorationYN("N");
						    restorationPreauthClaimVO.setRestSumInsured(new BigDecimal("0.00"));
						   
                }
				
			siInfoVO.setRestorationPreauthClaimVO(restorationPreauthClaimVO);
			
		}//end of if(rs5 != null)
			// End added by rekha koc1289_1272
			//Added as per KOC 1216B Change Request	
			 
            
			//Added as per KOC 1216B Change Request			
			return siInfoVO;
       }//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the fourth result set
			{ 
				try
				{
					if (rs5 != null) rs5.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the fourth Resultset in PreAuthDAOImpl getBalanceSIDetail()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally{
			
				try
				{
					if (rs4 != null) rs4.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the fourth Resultset in PreAuthDAOImpl getBalanceSIDetail()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if fourth result set is not closed, control reaches here. Try closing the third resultset now.
				{
					try{
						if (rs3 != null) rs3.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the third Resultset in PreAuthDAOImpl getBalanceSIDetail()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if third result set is not closed, control reaches here. Try closing the second resultset now.
					{
						try{
							if (rs2 != null) rs2.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Second Resultset in PreAuthDAOImpl getBalanceSIDetail()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if second result set is not closed, control reaches here. Try closing the first resultset now.
						{
							try{
								if (rs1 != null) rs1.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Second Resultset in PreAuthDAOImpl getBalanceSIDetail()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
							finally // Even if first result set is not closed, control reaches here. Try closing the statement now.
							{
								try
								{
									if (cStmtObject != null) cStmtObject.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Statement in PreAuthDAOImpl getBalanceSIDetail()",sqlExp);
									throw new TTKException(sqlExp, "preauth");
								}//end of catch (SQLException sqlExp)
								finally // Even if statement is not closed, control reaches here. Try closing the connection now.
								{
									try
									{
										if(conn != null) conn.close();
									}//end of try
									catch (SQLException sqlExp)
									{
										log.error("Error while closing the Connection in PreAuthDAOImpl getBalanceSIDetail()",sqlExp);
										throw new TTKException(sqlExp, "preauth");
									}//end of catch (SQLException sqlExp)
								}//end of finally Connection Close
							}//end of finally Statement Close
						} // end finally rs1
					} // end finally rs2
				}//end of finally rs3
			}//end of finally rs4
			}//end of try 
			
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{   rs5 = null;
				rs4 = null;
				rs3 = null;
				rs2 = null;
				rs1 = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	

	}//end of getBalanceSIDetail(long lngMemSeqID,long lngPolicyGrpSeqID,long lngBalanceSeqID)
//Changes as Per Koc 1142  && KOC 1140Change Request
   /**
	 * This method returns the SIInfoVO, which contains CopayAdvicedDetails.
	  * @param lngPatGenDtlSeqID long value which contains member id to get the CopayAdvicedDetails
	 * @param lngMemSeqID long value which contains member id to get the CopayAdvicedDetails
	 * @param lngPolicyGrpSeqID long value which contains Policy Group Seq ID
	 * @param lngBalanceSeqID long value which contains Balance Seq ID
	 * @return BalanceCopayDeductionVO object which contains CopayAdvicedDetails
	 * @exception throws TTKException
	 */
	public BalanceCopayDeductionVO getcopayAdviced(String strIdentifier,long lngPatGenDtlSeqID,long lngMemSeqID,long lngPolicyGrpSeqID,long lngBalanceSeqID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		
		SIInfoVO siInfoVO = new SIInfoVO();
		BalanceCopayDeductionVO balCopayDeducVO=null;
		try{
			conn = ResourceManager.getConnection();
			
			
			if(strIdentifier.equalsIgnoreCase("Pre-Authorization")){
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreAuthCopayAdvice);
			cStmtObject.setLong(1,lngMemSeqID);
			cStmtObject.setLong(2,lngPolicyGrpSeqID);
			cStmtObject.setLong(3,lngBalanceSeqID);
			cStmtObject.setLong(4,lngPatGenDtlSeqID);
					
			cStmtObject.registerOutParameter(5, Types.DECIMAL);//ROWS_PROCESSED
			cStmtObject.registerOutParameter(6, Types.DECIMAL);//ROWS_PROCESSED
	
			cStmtObject.execute();
						
			
			 balCopayDeducVO=new BalanceCopayDeductionVO();
			
			 if(cStmtObject.getBigDecimal(5)!=null)
			 {
			balCopayDeducVO.setBdApprovedAmt(cStmtObject.getBigDecimal(5));
			 }
			 else {
			 balCopayDeducVO.setBdApprovedAmt(new BigDecimal("0.00"));
			 }
			
			 if(cStmtObject.getBigDecimal(6)!=null)
			 {
			balCopayDeducVO.setBdMaxCopayAmt(cStmtObject.getBigDecimal(6));
			 }
			 else {
				 balCopayDeducVO.setBdMaxCopayAmt(new BigDecimal("0.00"));
			 }
			}
			else if(strIdentifier.equalsIgnoreCase("Claims")){
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimsCopayAdvice);
			     cStmtObject.setLong(1,lngMemSeqID);
				cStmtObject.setLong(2,lngPolicyGrpSeqID);
				cStmtObject.setLong(3,lngBalanceSeqID);
				cStmtObject.setLong(4,lngPatGenDtlSeqID);
				cStmtObject.registerOutParameter(5, Types.DECIMAL);//ROWS_PROCESSED
				cStmtObject.registerOutParameter(6, Types.DECIMAL);//ROWS_PROCESSED
				cStmtObject.execute();
			    balCopayDeducVO=new BalanceCopayDeductionVO();
				 if(cStmtObject.getBigDecimal(5)!=null)
				 {
				balCopayDeducVO.setBdApprovedAmt(cStmtObject.getBigDecimal(5));
				 }
				 else {
				 balCopayDeducVO.setBdApprovedAmt(new BigDecimal("0.00"));
				 }
				
				 if(cStmtObject.getBigDecimal(6)!=null)
				 {
				balCopayDeducVO.setBdMaxCopayAmt(cStmtObject.getBigDecimal(6));
				 }
				 else {
					 balCopayDeducVO.setBdMaxCopayAmt(new BigDecimal("0.00"));
				 }
				 
			}
			//siInfoVO.setBalCopayDeducVO(balCopayDeducVO);
			return balCopayDeducVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
        			log.error("Error while closing the Statement in PreAuthDAOImpl copayAdviced()",sqlExp);
        			throw new TTKException(sqlExp, "preauth");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PreAuthDAOImpl copayAdviced()",sqlExp);
        				throw new TTKException(sqlExp, "preauth");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "preauth");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
	}//end of copayAdviced(long lngMemSeqID,long lngPolicyGrpSeqID,long lngBalanceSeqID)
   //Changes as Per Koc 1142 && 1140 Change Request and 1165
 /**bajaj
     * This method send  the intimation   to insurer 
     * @param authorizationVO AuthorizationVO contains Authorization information
     * @param strIdentifier Pre-Authorization/Claims
     * @return long int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
	public int sendInsIntimate(AuthorizationVO authorizationVO,String strIdentifier) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			
			if(strIdentifier.equalsIgnoreCase("Pre-Authorization")){
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPatInsIntimate);
				if(authorizationVO.getPreAuthSeqID() != null){
					cStmtObject.setLong(1,authorizationVO.getPreAuthSeqID());
				}//end of if(authorizationVO.getPreAuthSeqID() != null)
				else{
					cStmtObject.setString(1,null);
				}//end of else

				if(authorizationVO.getStatusTypeID() != null){
					cStmtObject.setString(2,authorizationVO.getStatusTypeID());
				}//end of if(authorizationVO.getEnrollDtlSeqID() != null)
				else{
					cStmtObject.setString(2,null);
				}//end of else

				
				if(authorizationVO.getTotalSumInsured() != null){
					cStmtObject.setBigDecimal(3,authorizationVO.getTotalSumInsured());
				}//end of if(authorizationVO.getTotalSumInsured() != null)
				else{
					cStmtObject.setString(3,null);
				}//en of else
				cStmtObject.setString(4,"M");
				cStmtObject.setLong(5,authorizationVO.getUpdatedBy());//1274A
				cStmtObject.execute();
				}//end of if(strIdentifier.equalsIgnoreCase("Pre-Authorization"))
				
				else if(strIdentifier.equalsIgnoreCase("Claims")){
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimInsIntimate);
				if(authorizationVO.getClaimSeqID() != null){
					cStmtObject.setLong(1,authorizationVO.getClaimSeqID());
				}//end of if(authorizationVO.getClaimSeqID() != null)
				else{
					cStmtObject.setString(1,null);
				}//end of else
				if(authorizationVO.getStatusTypeID() != null){
					cStmtObject.setString(2,authorizationVO.getStatusTypeID());
				}//end of if(authorizationVO.getAssignUserSeqID() != null)
				else{
					cStmtObject.setString(2,null);
				}//end of else

				if(authorizationVO.getTotalSumInsured() != null){
					cStmtObject.setBigDecimal(3,authorizationVO.getTotalSumInsured());
				}//end of if(authorizationVO.getTotalSumInsured() != null)
				else{
					cStmtObject.setString(3,null);
				}//end of else
				cStmtObject.setString(4,"M");
				cStmtObject.setLong(5,authorizationVO.getUpdatedBy());//1274A
				cStmtObject.execute();
				}//end of  else if(strIdentifier.equalsIgnoreCase("Claims"))
			
			}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
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
					log.error("Error while closing the Statement in PreAuthDAOImpl SendIntimation()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl SendIntimation()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of SendInsIntimate(AuthorizationVO authorizationVO)

	public long saveUpoadedFilename(ArrayList alFileAUploadList)throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		//long  batchSeqId=0;
		//long  batchId=0;
		int rowsprocessed=0;
		try{
		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPatClmFileUpload);
			if(alFileAUploadList.get(0)!=null)
			{
				cStmtObject.setLong(1, (Long)alFileAUploadList.get(0));
				cStmtObject.setString(2, "");
				
			}
			else if(alFileAUploadList.get(1)!=null)
			{
				cStmtObject.setString(1, "");
				cStmtObject.setLong(2, (Long)alFileAUploadList.get(1));
				
			}
			cStmtObject.setString(3, (String)alFileAUploadList.get(3));//filename
			cStmtObject.setString(4, (String)alFileAUploadList.get(4));//remarks
			cStmtObject.setLong(5, (Long)alFileAUploadList.get(2));//addedby
			cStmtObject.registerOutParameter(6,Types.INTEGER);
			cStmtObject.execute();
			rowsprocessed = cStmtObject.getInt(6);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl saveUploadFileName()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl saveUploadFileName()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{

				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally

		return rowsprocessed;

	}

	/**
	 * 1274A
	 * This method saves the Pre-Authorization information
	 * @param preAuthDetailVO PreAuthDetailVO contains Pre-Authorization information
	 * @param strSelectionType String object which contains Selection Type - 'PAT' or 'ICO'
	 * @return int  the values,of  MEMBER_SEQ_ID , PAT_GEN_DETAIL_SEQ_ID and PAT_ENROLL_DETAIL_SEQ_ID
	 * @exception throws TTKException
	 */
	public int accountUnfreeze(ArrayList alUnfreezeList) throws TTKException
	{
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		long rowsprocessed=0;
		try{
			
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strUnFreezePatClm);
			if(alUnfreezeList.get(0)!=null)
			{
				cStmtObject.setLong(1, (Long)alUnfreezeList.get(0));
				cStmtObject.setString(2, null);
			}
			else if(alUnfreezeList.get(1)!=null)
			{
				cStmtObject.setString(1, null);
				cStmtObject.setLong(2, (Long)alUnfreezeList.get(1));
			}
			cStmtObject.setLong(3, (Long)alUnfreezeList.get(2));
			cStmtObject.registerOutParameter(4,Types.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(4);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{

			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl accountUnfreeze()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl accountUnfreeze()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of accountUnfreeze(AuthorizationVO authorizationVO)
	
	
	public int accountinsOverrideConf(ArrayList alUnfreezeList) throws TTKException
	{
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		long rowsprocessed=0;
		try{
			
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strOverrideConf);
			if(alUnfreezeList.get(0)!=null)
			{
				cStmtObject.setLong(1, (Long)alUnfreezeList.get(0));
				cStmtObject.setString(2, null);
			}
			else if(alUnfreezeList.get(1)!=null)
			{
				cStmtObject.setString(1, null);
				cStmtObject.setLong(2, (Long)alUnfreezeList.get(1));
			}
			cStmtObject.setLong(3, (Long)alUnfreezeList.get(2));
			cStmtObject.registerOutParameter(4,Types.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(4);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "preauth");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (Exception exp)
		finally
		{

			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl accountUnfreeze()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl accountUnfreeze()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of accountUnfreeze(AuthorizationVO authorizationVO)
	
	
	public InsOverrideConfVO getConfigDetails(long lngProdPolicySeqId,String Mode) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		InsOverrideConfVO insOverrideConfVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetInsIntimation);
			cStmtObject.setLong(1,lngProdPolicySeqId);
			cStmtObject.setString(2,Mode);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			if(rs != null)
			{
				while(rs.next())
				{
					insOverrideConfVO = new InsOverrideConfVO();
					
					
					if(rs.getString("INS_INTIMATION_REQ_YN") != null){
						insOverrideConfVO.setInsDecYN(rs.getString("INS_INTIMATION_REQ_YN"));
					}
					if(rs.getString("INS_INT_RSON_TYPE_ID") != null){
						insOverrideConfVO.setInsReqType(rs.getString("INS_INT_RSON_TYPE_ID"));
					}
					//Changes as per KOC 1142 Change Request
					if(rs.getString("INS_INT_OVERRIDEN_DATE") != null){
						insOverrideConfVO.setOverriddenDate(rs.getString("INS_INT_OVERRIDEN_DATE"));
					}
					//Changes as per KOC 1142 Change Request
					if(rs.getString("INS_INT_OVERRIDE_REMARKS") != null){
						insOverrideConfVO.setRemarks(rs.getString("INS_INT_OVERRIDE_REMARKS"));
					}
			
					/*if(rs.getString("INS_INT_OVERRIDEN_BY") != null){
						insOverrideConfVO.set(rs.getString("INS_INT_OVERRIDEN_BY"));
					}*/
				
					//Changes as per KOC 1142 Change Request
				}//end of while(rs.next())
			}//end of (rs != null)
			return insOverrideConfVO;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getCustMsgInfo()",sqlExp);
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
							log.error("Error while closing the Statement in PolicyDAOImpl getCustMsgInfo()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getCustMsgInfo()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	}//end of getConfigCopayAmt(long lngProdPolicySeqId)
	
	
	public int saveOverrideConfigDetails(InsOverrideConfVO insOverrideConfVO) throws TTKException{
		int iResult = 0;  
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
                     
          //  cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveConfigCopayAmt);
             cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strOverrideConf);
             
             
             
            // log.debug("seq_id"+insOverrideConfVO.getPatClmSeqID());
            // log.info("getMode"+insOverrideConfVO.getMode());
            // log.info("getInsDecYN"+insOverrideConfVO.getInsDecYN());
          //   log.info("getOverriddenDate"+ (insOverrideConfVO.getOverriddenDate()));
           //  log.info("getRemarks"+insOverrideConfVO.getRemarks());
             
            if(insOverrideConfVO.getPatClmSeqID()!= null){
            	cStmtObject.setLong(1,insOverrideConfVO.getPatClmSeqID());
            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else.
            if(insOverrideConfVO.getMode() != null){
            	
            	cStmtObject.setString(2,insOverrideConfVO.getMode());

            }//end of if(bufferDetailVO.getBufferAmt() != null)
           /* else{
            	cStmtObject.setString(2,"N");
            }//end of else
*/            
            if(insOverrideConfVO.getInsDecYN() != null){
            	cStmtObject.setString(3,insOverrideConfVO.getInsDecYN());
           }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(3,null);
            }//end of else
            
            if(insOverrideConfVO.getInsReqType() != null){
            	cStmtObject.setString(4,insOverrideConfVO.getInsReqType());
           }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(4,null);
            }//end of else     
                   
           /* if(insOverrideConfVO.getOverriddenDate() != null){
            	cStmtObject.setString(5,insOverrideConfVO.getOverriddenDate());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(5,null);
            }//end of else
            */
            
            cStmtObject.setString(5,insOverrideConfVO.getRemarks());//1284 change request
            cStmtObject.setLong(6,insOverrideConfVO.getUpdatedBy());
           
            cStmtObject.registerOutParameter(7, Types.INTEGER);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(7);
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
        			log.error("Error while closing the Statement in PolicyDAOImpl getProviders()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PolicyDAOImpl getProviders()",sqlExp);
        				throw new TTKException(sqlExp, "policy");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "policy");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
	}//end of saveConfigCopayAmt(insOverrideConfVO insOverrideConfVO)
	
	 public HashMap<String,String> getProviders() throws TTKException{

			
	    	Connection conn = null;
	    	PreparedStatement cStmtObject=null;
	    	 ResultSet rs=null;
	    	HashMap<String,String> providerList=new HashMap<>();
	    	try{
	    		conn = ResourceManager.getConnection();
	                     
	             cStmtObject = conn.prepareCall(strGetProviders);
	             
	            rs=cStmtObject.executeQuery();
	            if(rs!=null){
	            	while(rs.next()){
	            		providerList.put(rs.getString("PROVIDER_SEQ_ID")+"@"+rs.getString("PROVIDER_ID"),rs.getString("PROVIDER_NAME"));
	            	}
	            }
	        }//end of try
	        catch (SQLException sqlExp) 
	        {
	            throw new TTKException(sqlExp, "policy");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp) 
	        {
	            throw new TTKException(exp, "policy");
	        }//end of catch (Exception exp) 
	        finally
			{
	        	/* Nested Try Catch to ensure resource closure */ 
	        	try // First try closing the Statement
	        	{
	        		try
	        		{		
	        			if (rs != null) rs.close();
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in PolicyDAOImpl getProviders()",sqlExp);
	        			throw new TTKException(sqlExp, "policy");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PolicyDAOImpl getProviders()",sqlExp);
	        				throw new TTKException(sqlExp, "policy");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "policy");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	        return providerList;
		
	 }//getProviders
	

    /**
     * This method get the DHPO upload preauth details  
     * @param PreAuthSeqID
     * returns DhpoWebServiceVO 
     * @exception throws TTKException
     */
 	public DhpoWebServiceVO getDhpoPreauthDetails(String preauthSeqID) throws TTKException{
 		

		Connection conn = null;
		CallableStatement cStmtObject=null;
		OracleResultSet rs = null;
		DhpoWebServiceVO webServiceVO = new DhpoWebServiceVO();;
		XMLType xml=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDhpoPreauthUpload);
			cStmtObject.setString(1,preauthSeqID);			
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (OracleResultSet)cStmtObject.getObject(2);
			if(rs != null)
			{
				if(rs.next())
				{
					
					 if(rs.getOPAQUE("UPLOAD_FILE") != null) {
						 
						 xml = XMLType.createXML(rs.getOPAQUE("UPLOAD_FILE"));
	                     DOMReader domReader = new DOMReader();
	                    Document doc = xml != null ? domReader.read(xml.getDOM()):null;				 
						
	                        webServiceVO.setFileContent(doc.asXML().getBytes());	                       
	                  }// End of if(rs.getOPAQUE("UPLOAD_FILE")					 
					 webServiceVO.setFileID(rs.getString("FILE_ID"));
					 webServiceVO.setFileName(rs.getString("FILE_NAME"));			
				}//end of while(rs.next())
			}//end of (rs != null)
			return webServiceVO;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getDhpoPreauthDetails()",sqlExp);
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
							log.error("Error while closing the Statement in PolicyDAOImpl getDhpoPreauthDetails()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getDhpoPreauthDetails()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally	
 		
 	}//getDhpoPreauthDetails(String preauthSeqID)
 	
 	/**
     * This method get the DHPO upload preauth log details  
     * @param  DhpoWebServiceVO DHPO upload preauth log details  
     * returns row proccessed 
     * @exception throws TTKException
     */
 	public long saveDhpoPreauthLogDetails(DhpoWebServiceVO dhpoWebServiceVO) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		OracleResultSet rs = null;	
		
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveDhpoPreauthlogDetails);
		
			cStmtObject.setString(1,dhpoWebServiceVO.getPreAuthSeqID());
			
			cStmtObject.setString(2,dhpoWebServiceVO.getFileName());
			
			if(dhpoWebServiceVO.getTransactionResult()!=null){
				cStmtObject.setInt(3,dhpoWebServiceVO.getTransactionResult());
			}else{
				cStmtObject.setString(3,null);
			}
			
			cStmtObject.setString(4,dhpoWebServiceVO.getErrorMessage());
			
			if(dhpoWebServiceVO.getErrorReport()!=null){
				
				cStmtObject.setCharacterStream(5, new StringReader(new String(dhpoWebServiceVO.getErrorReport())), dhpoWebServiceVO.getErrorReport().length);
			
			}else{
				cStmtObject.setCharacterStream(5,null,0);
			}
			cStmtObject.setString(6,dhpoWebServiceVO.getPreAuthUploadStatus());
			int efrw=cStmtObject.executeUpdate();
			return efrw;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl saveDhpoPreauthLogDetails()",sqlExp);
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
							log.error("Error while closing the Statement in PolicyDAOImpl saveDhpoPreauthLogDetails()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl saveDhpoPreauthLogDetails()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally	
 	
 	}//saveDhpoPreauthLogDetails
 	
 	
 	/**
     * This method get the DHPO upload preauth details  
     * @param PreAuthSeqID
     * returns DhpoWebServiceVO 
     * @exception throws TTKException
     */
 	public DhpoWebServiceVO getDhpoPreauthLogDetails(String preauthSeqID) throws TTKException{
 		

		Connection conn = null;
		CallableStatement cStmtObject=null;
		OracleResultSet rs = null;
		DhpoWebServiceVO webServiceVO = new DhpoWebServiceVO();;
		
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDhpoPreauthLogDetails);
			cStmtObject.setString(1,preauthSeqID);			
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (OracleResultSet)cStmtObject.getObject(2);
			
			if(rs != null){
				if(rs.next()){										
					 webServiceVO.setXmlFileReader(rs.getCharacterStream("ERROR_REPORT"));					 
					 webServiceVO.setFileName(rs.getString("FILE_NAME"));
					 webServiceVO.setErrorMessage(rs.getString("ERROR_MESSAGE"));					 
				}//end of while(rs.next())
			}//end of (rs != null)
			return webServiceVO;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getDhpoPreauthLogDetails()",sqlExp);
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
							log.error("Error while closing the Statement in PolicyDAOImpl getDhpoPreauthLogDetails()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getDhpoPreauthLogDetails()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally	
 		
 	}//getDhpoPreauthLogDetails(String preauthSeqID)
	
 	public LinkedHashMap<String, String> getPreauthDiagDetails() throws TTKException
	{
		//HashMap<String, String> hashMap=new HashMap<>();
 		LinkedHashMap<String, String> hashMap=new LinkedHashMap<String, String>();
		Connection conn = null;
		Statement statement=null;
		ResultSet resultSet=null;
		conn=ResourceManager.getConnection();
		//strGetdiagDetails="select d.denial_code, d.denial_description
		try {
			statement=conn.createStatement();
			resultSet=statement.executeQuery(strGetdiagDetails);
			while(resultSet.next())
			{
				
				hashMap.put(resultSet.getString("denial_code"), resultSet.getString("denial_description"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
	    	/* Nested Try Catch to ensure resource closure */ 
	    	try // First try closing the Statement
	    	{   try
		      {
			     if (resultSet != null) resultSet.close();
		       }//end of try
		    catch (SQLException sqlExp)
		    {
			   log.error("Error while closing the Resultset in PreAuthDAOImpl getActivityCodeList()",sqlExp);
			    throw new TTKException(sqlExp, "onlinepreauth");
		     }//end of catch (SQLException sqlExp)
	    		try
	    		{
	    			if (statement != null) statement.close();
	    		}//end of try
	    		catch (SQLException sqlExp)
	    		{
	    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
	    			throw new TTKException(sqlExp, "policy");
	    		}//end of catch (SQLException sqlExp)
	    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	    		{
	    			try
	    			{
	    				if(conn != null) conn.close();
	    			}//end of try
	    			catch (SQLException sqlExp)
	    			{
	    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
	    				throw new TTKException(sqlExp, "policy");
	    			}//end of catch (SQLException sqlExp)
	    		}//end of finally Connection Close
	    	}//end of try
	    	catch (TTKException exp)
	    	{
	    		throw new TTKException(exp, "policy");
	    	}//end of catch (TTKException exp)
	    	finally // Control will reach here in anycase set null to the objects 
	    	{
	    		resultSet = null;
	    		statement = null;
	    		conn = null;
	    	}//end of finally
		}//end of finally	
		return hashMap;
		
	}
	
	
	 public ArrayList<MOUDocumentVO> getProviderDocs(String preauthSeqID)throws TTKException{

			Connection conn = null;
			PreparedStatement cStmtObject=null;
			ResultSet rs=null;
			MOUDocumentVO mouDocumentVO=null;
			ArrayList<MOUDocumentVO> providerDocList=new ArrayList<>();
			try{
				conn = ResourceManager.getConnection();
		                 
		         cStmtObject = conn.prepareCall(strGetProviderDocs);
		       
		         cStmtObject.setString(1, preauthSeqID);
					rs= cStmtObject.executeQuery();
					
					if(rs != null){
						while(rs.next()){
							
							mouDocumentVO = new MOUDocumentVO();
							mouDocumentVO.setDescription(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
			                mouDocumentVO.setMouDocPath(TTKCommon.checkNull(rs.getString("DOCS_PATH")));
			                mouDocumentVO.setMouDocSeqID(((long) rs.getLong("mou_doc_seq_id")));
			                mouDocumentVO.setFileName(TTKCommon.checkNull(rs.getString("FILE_NAME")));
			                if(rs.getString("ADDED_DATE") != null){
			                	mouDocumentVO.setDateTime(rs.getString("ADDED_DATE"));
							}//end of if(rs.getString("ADDED_DATE") != null)
			                mouDocumentVO.setUserId(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
			                providerDocList.add(mouDocumentVO);
						}//end of while(rs.next())
					}//end of if(rs != null)
			    	//return (ArrayList<Object>)alMouList;
					
		    }//end of try
		    catch (SQLException sqlExp) 
		    {
		        throw new TTKException(sqlExp, "policy");
		    }//end of catch (SQLException sqlExp)
		    catch (Exception exp) 
		    {
		        throw new TTKException(exp, "policy");
		    }//end of catch (Exception exp) 
			finally
			{
		    	/* Nested Try Catch to ensure resource closure */ 
		    	try // First try closing the Statement
		    	{   try
			      {
				     if (rs != null) rs.close();
			       }//end of try
			    catch (SQLException sqlExp)
			    {
				   log.error("Error while closing the Resultset in PreAuthDAOImpl getActivityCodeList()",sqlExp);
				    throw new TTKException(sqlExp, "onlinepreauth");
			     }//end of catch (SQLException sqlExp)
		    		try
		    		{
		    			if (rs != null) rs.close();
		    			if (cStmtObject != null) cStmtObject.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
		    			throw new TTKException(sqlExp, "policy");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    		{
		    			try
		    			{
		    				if(conn != null) conn.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
		    				throw new TTKException(sqlExp, "policy");
		    			}//end of catch (SQLException sqlExp)
		    		}//end of finally Connection Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "policy");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		rs = null;
		    		cStmtObject = null;
		    		conn = null;
		    	}//end of finally
			}//end of finally
		    	
		    return providerDocList;
	 }
	 
	 
	 public InputStream getShortfallDBFile(String shortfallSeqID)throws TTKException{
			
//			Connection conn = null;
//			PreparedStatement cStmtObject=null;
//			ResultSet rs=null;
			Blob blob	=	null;
			InputStream iStream	=	null;
			try(Connection conn = ResourceManager.getConnection();
					PreparedStatement cStmtObject = conn.prepareCall(strGetShortfallFile))
			{
		         cStmtObject.setString(1, shortfallSeqID);
		         try(ResultSet rs= cStmtObject.executeQuery()){
		        	 if(rs != null){
							if(rs.next())
								blob	=	rs.getBlob(1);
							if(blob!= null){
							iStream	=	blob.getBinaryStream();
							}
						}
		         }		
		    }//end of try
		    catch (SQLException sqlExp) 
		    {
		        throw new TTKException(sqlExp, "policy");
		    }//end of catch (SQLException sqlExp)
		    catch (Exception exp) 
		    {
		        throw new TTKException(exp, "policy");
		    }//end of catch (Exception exp) 
		    return iStream;
	 }
	 
	 public InputStream getPolicyTobFile(String seqId)throws TTKException{
			
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			Blob blob	=	null;
			InputStream iStream	=	new ByteArrayInputStream(new String("").getBytes());
			try{
				conn = ResourceManager.getConnection();
		                 
		         cStmtObject = conn.prepareCall(strGetPolicyTobFile);
		       
		        cStmtObject.setString(1, seqId);
				cStmtObject.registerOutParameter(2,Types.BLOB);
				cStmtObject.execute();
				
			//	rs = (java.sql.ResultSet)cStmtObject.getObject(2);

				
					//if(rs != null){
					//	if(rs.next()){
						 blob	=	(BLOB) cStmtObject.getBlob(2);
						 iStream	=	blob.getBinaryStream();
						
						
						//}
					//	}//end of if(rs != null)
			    	//return (ArrayList<Object>)alMouList;
						 
		    }//end of try
		    catch (SQLException sqlExp) 
		    {
		        throw new TTKException(sqlExp, "onlineforms");
		    }//end of catch (SQLException sqlExp)
			
			catch (NullPointerException nullExp)
			{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.policyTobFile.notFound");
				throw expTTK;
			}
			
			
		    catch (Exception exp) 
		    {
		        throw new TTKException(exp, "onlineforms");
		    }//end of catch (Exception exp) 
		    finally
			{
		    	/* Nested Try Catch to ensure resource closure */ 
		    	try // First try closing the Statement
		    	{
		    		try
		    		{	
		    			if (rs != null) rs.close();
		    			if (cStmtObject != null) cStmtObject.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
		    			throw new TTKException(sqlExp, "policy");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    		{
		    			try
		    			{
		    				if(conn != null) conn.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
		    				throw new TTKException(sqlExp, "policy");
		    			}//end of catch (SQLException sqlExp)
		    		}//end of finally Connection Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "policy");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		cStmtObject = null;
		    		conn = null;
		    	}//end of finally
			}//end of finally
		    return iStream;
	 }
	 
		public long overridPreAuthDetails(String preAuthSeqID,String overrideRemarks,Long userSeqID,String overrideGenRemarks,String overrideGenSubRemarks) throws TTKException{

			long rowUpd = 0;
			Connection conn = null;
			CallableStatement cStmtObject = null;
			try {
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement) conn
						.prepareCall(strOverridreAuthDetails);
				cStmtObject.setString(1, preAuthSeqID);
				cStmtObject.setLong(2, userSeqID);
				cStmtObject.setString(3, overrideRemarks);
				cStmtObject.setString(4,overrideGenRemarks);		// main dp
				cStmtObject.setString(5,overrideGenSubRemarks);		// sumdp
				cStmtObject.registerOutParameter(6, OracleTypes.NUMBER);
				cStmtObject.execute();
				rowUpd = cStmtObject.getLong(6);
			}// end of try
			catch (SQLException sqlExp) {
				throw new TTKException(sqlExp, "claim");
			}// end of catch (SQLException sqlExp)
			catch (Exception exp) {
				throw new TTKException(exp, "claim");
			}// end of catch (Exception exp)
			finally {
				/* Nested Try Catch to ensure resource closure */
				try // First try closing the Statement
				{
					try {
						if (cStmtObject != null)
							cStmtObject.close();
					}// end of try
					catch (SQLException sqlExp) {
						log.error(
								"Error while closing the Statement in PreAuthDAOImpl overridPreAuthDetails()",
								sqlExp);
						throw new TTKException(sqlExp, "floataccount");
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
									"Error while closing the Connection in PreAuthDAOImpl overridPreAuthDetails()",
									sqlExp);
							throw new TTKException(sqlExp, "floataccount");
						}// end of catch (SQLException sqlExp)
					}// end of finally Connection Close
				}// end of try
				catch (TTKException exp) {
					throw new TTKException(exp, "claim");
				}// end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the
						// objects
				{
					cStmtObject = null;
					conn = null;
				}// end of finally
			}// end of finally
			return rowUpd;
		}
	 
	 public ArrayList getClaimDocDBFile(String ClaimSeqId)throws TTKException{
			
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			Blob blob	=	null;
			ArrayList arraylist = new ArrayList();
			InputStream iStream	=	new ByteArrayInputStream(new String("").getBytes());
			String fileType = "";
			byte[] blobAsBytes = null;
			String imageString = "";
			try{
				conn = ResourceManager.getConnection();
		                 
		         cStmtObject = conn.prepareCall(strGetClaimFile);
		       
		        cStmtObject.setString(1, ClaimSeqId);
				cStmtObject.registerOutParameter(2,Types.BLOB);
				cStmtObject.registerOutParameter(3,Types.VARCHAR);
				cStmtObject.execute();
				
				rs= cStmtObject.executeQuery();
				
				if(rs != null){
					
					blob	=	(BLOB) cStmtObject.getBlob(2);
						if(blob!= null){
						int blobLength = (int) blob.length();  
						blobAsBytes = blob.getBytes(1, blobLength);
						BASE64Encoder encoder = new BASE64Encoder();
					 //   imageString = encoder.encode(blobAsBytes); 
					    imageString = DatatypeConverter.printBase64Binary(blobAsBytes);
						}
						 
					if(blob!= null){
						iStream	=	blob.getBinaryStream();
						 fileType =  TTKCommon.checkNull(cStmtObject.getString(3));
					}
						
				}//end of if(rs != null)
						
					  
					arraylist.add(iStream);//taking for pdf data
					arraylist.add(fileType);
					arraylist.add(imageString);//taking for image data
					
		    }//end of try
		    catch (SQLException sqlExp) 
		    {
		        throw new TTKException(sqlExp, "preauth");
		    }//end of catch (SQLException sqlExp)
		    catch (Exception exp) 
		    {
		        throw new TTKException(exp, "preauth");
		    }//end of catch (Exception exp) 
		    finally
			{
		    	/* Nested Try Catch to ensure resource closure */ 
		    	try // First try closing the Statement
		    	{
		    		try
		    		{	if (rs != null) rs.close();
		    			if (cStmtObject != null) cStmtObject.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
		    			throw new TTKException(sqlExp, "preauth");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    		{
		    			try
		    			{
		    				if(conn != null) conn.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
		    				throw new TTKException(sqlExp, "policy");
		    			}//end of catch (SQLException sqlExp)
		    		}//end of finally Connection Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "preauth");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		rs = null;
		    		cStmtObject = null;
		    		conn = null;
		    	}//end of finally
			}//end of finally
		    return arraylist;
	 }
	 
	 
	 public String saveDentalDetails(DentalOrthoVO dentalOrthoVO,String PatOrClm) throws TTKException{
			Connection conn = null;
			CallableStatement cStmtObject=null;
			OracleResultSet rs = null;	
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePreAuthDentalDetails);
			
				if(dentalOrthoVO.getDentoSeqid()!=null)
					cStmtObject.setLong(1,dentalOrthoVO.getDentoSeqid());
				else
					cStmtObject.setLong(1,0);
				cStmtObject.setLong(2,dentalOrthoVO.getPreAuthSeqid());
				cStmtObject.setString(3,PatOrClm);
				cStmtObject.setString(4,dentalOrthoVO.getDentoclass1());
				cStmtObject.setString(5,dentalOrthoVO.getDentoclass2());
				cStmtObject.setString(6,dentalOrthoVO.getDentoclass2Text());
				cStmtObject.setString(7,dentalOrthoVO.getDentoclass3());
				cStmtObject.setString(8,dentalOrthoVO.getSkeletalClass1());
				cStmtObject.setString(9,dentalOrthoVO.getSkeletalClass2());
				cStmtObject.setString(10,dentalOrthoVO.getSkeletalClass3());
				cStmtObject.setString(11,dentalOrthoVO.getOverJet());
				cStmtObject.setString(12,dentalOrthoVO.getReverseJet());
				cStmtObject.setString(13,dentalOrthoVO.getReverseJetYN());
				cStmtObject.setString(14,dentalOrthoVO.getCrossbiteAntrio());
				cStmtObject.setString(15,dentalOrthoVO.getCrossbitePosterior());
				cStmtObject.setString(16,dentalOrthoVO.getCrossbiteRetrucontract());
				cStmtObject.setString(17,dentalOrthoVO.getOpenbiteAntrio());
				cStmtObject.setString(18,dentalOrthoVO.getOpenbitePosterior());
				cStmtObject.setString(19,dentalOrthoVO.getOpenbiteLateral());
				cStmtObject.setString(20,dentalOrthoVO.getContactPointDisplacement());
				cStmtObject.setString(21,dentalOrthoVO.getOverBite());
				cStmtObject.setString(22,dentalOrthoVO.getOverbitePalatalYN());
				cStmtObject.setString(23,dentalOrthoVO.getOverbiteGingivalYN());
				cStmtObject.setString(24,dentalOrthoVO.getHypodontiaQuand1Teeth());
				cStmtObject.setString(25,dentalOrthoVO.getHypodontiaQuand2Teeth());
				cStmtObject.setString(26,dentalOrthoVO.getHypodontiaQuand3Teeth());
				cStmtObject.setString(27,dentalOrthoVO.getHypodontiaQuand4Teeth());
				cStmtObject.setString(28,dentalOrthoVO.getImpededTeethEruptionNo());
				cStmtObject.setString(29,dentalOrthoVO.getImpededTeethNo());
				cStmtObject.setString(30,dentalOrthoVO.getSubmergerdTeethNo());
				cStmtObject.setString(31,dentalOrthoVO.getSupernumeryTeethNo());
				cStmtObject.setString(32,dentalOrthoVO.getRetainedTeethNo());
				cStmtObject.setString(33,dentalOrthoVO.getEctopicTeethNo());
				cStmtObject.setString(34,dentalOrthoVO.getCranioFacialNo());
				cStmtObject.setString(35,dentalOrthoVO.getAestheticComp());
				cStmtObject.setString(36,dentalOrthoVO.getCrossbiteAntriomm());
				cStmtObject.setString(37,dentalOrthoVO.getCrossbitePosteriormm());
				cStmtObject.setString(38,dentalOrthoVO.getCrossbiteRetrucontractmm());
				cStmtObject.setString(39,dentalOrthoVO.getContactPointDisplacementmm());
				cStmtObject.setString(40,dentalOrthoVO.getDentoclass3Text());
				
				cStmtObject.registerOutParameter(41,Types.VARCHAR);
				cStmtObject.executeUpdate();
				String iotn = cStmtObject.getString(41);
				return iotn;
			 }//end of try
				catch (SQLException sqlExp) 
				{
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				catch (Exception exp) 
				{
					throw new TTKException(exp, "policy");
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
							log.error("Error while closing the Resultset in PreAuthDAOImpl saveDentalDetails()",sqlExp);
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
								log.error("Error while closing the Statement in PreAuthDAOImpl saveDentalDetails()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in PreAuthDAOImpl saveDentalDetails()",sqlExp);
									throw new TTKException(sqlExp, "policy");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "policy");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs = null;
						cStmtObject = null;
						conn = null;
					}//end of finally
				}//end of finally	
	 	
	 	}//long saveDentalDetails(DentalOrthoVO dentalOrthoVO


	 public ArrayList getPreauthDocDBFile(String preAuthRefSeqID,String preAuthRecvTypeID)throws TTKException{
			
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			Blob blob	=	null;
			ArrayList arraylist = new ArrayList();
			InputStream iStream	=	new ByteArrayInputStream(new String("").getBytes());
			String filePresentYN = "";

			try{
				conn = ResourceManager.getConnection();
		                 
		         cStmtObject = conn.prepareCall(strGetPrePartnerFile);
		       
		        cStmtObject.setString(1, preAuthRefSeqID);
		        cStmtObject.setString(2, preAuthRecvTypeID);
				cStmtObject.registerOutParameter(3,Types.BLOB);
				cStmtObject.execute();
				
				rs= cStmtObject.executeQuery();
				
				if(rs != null){
					
						blob	=	(BLOB) cStmtObject.getBlob(3);
					if(blob!= null){
						iStream	=	blob.getBinaryStream();
						filePresentYN =  "Y";
					}else{
						filePresentYN =  "N";
					}
						
				}//end of if(rs != null)
					arraylist.add(iStream);
					arraylist.add(filePresentYN);
		    }//end of try
		    catch (SQLException sqlExp) 
		    {
		        throw new TTKException(sqlExp, "preauth");
		    }//end of catch (SQLException sqlExp)
		    catch (Exception exp) 
		    {
		        throw new TTKException(exp, "preauth");
		    }//end of catch (Exception exp) 
		    finally
			{
		    	/* Nested Try Catch to ensure resource closure */ 
		    	try // First try closing the Statement
		    	{
		    		try
		    		{	if (rs != null) rs.close();
		    			if (cStmtObject != null) cStmtObject.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
		    			throw new TTKException(sqlExp, "preauth");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    		{
		    			try
		    			{
		    				if(conn != null) conn.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
		    				throw new TTKException(sqlExp, "policy");
		    			}//end of catch (SQLException sqlExp)
		    		}//end of finally Connection Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "preauth");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		rs = null;
		    		cStmtObject = null;
		    		conn = null;
		    	}//end of finally
			}//end of finally
		    return arraylist;
	 }
	 
	 
	 public int saveDocUploads(PreAuthDetailVO preAuthDetailVO,ArrayList alFileAUploadList,Long userSeqId,String preAuth_Seq_ID,String origFileName,InputStream inputStream,int formFileSize) throws TTKException {
		 int iResult = 0;
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	try{
	    		conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePreauthUploadFils);

	            cStmtObject.setLong(1,0);//docs_seq_id 
	            if((boolean)alFileAUploadList.get(4).equals("PAT")){
		            cStmtObject.setString(2,preAuth_Seq_ID);//preauthseqid
		            }else{
		            	 cStmtObject.setString(2,preAuth_Seq_ID);//claimseqid
		            }                  
	            cStmtObject.setString(3,(String) alFileAUploadList.get(4));//source_id
	            cStmtObject.setString(4,(String) alFileAUploadList.get(2));//DropDown Value    (docs_desc)
	            cStmtObject.setString(5,preAuthDetailVO.getDocName());//Doc Path (file_path)
	            cStmtObject.setString(6,origFileName);//Doc Name (file_name)
	            cStmtObject.setLong(7,userSeqId);//(added_by )
	            cStmtObject.setBinaryStream(8, inputStream,formFileSize);//FILE INPUT STREAM   (image_file)
	            cStmtObject.registerOutParameter(9,Types.INTEGER);//ROW_PROCESSED        
	            cStmtObject.execute();            
	            iResult  = cStmtObject.getInt(9);    
	        }//end of try
	    	catch (SQLException sqlExp)
	        {
	            throw new TTKException(sqlExp, "preauth");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "preauth");
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
	        			log.error("Error while closing the Statement in PreauthDAOImpl saveDocUploads()",sqlExp);
	        			throw new TTKException(sqlExp, "preauth");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PreauthDAOImpl saveDocUploads()",sqlExp);
	        				throw new TTKException(sqlExp, "preauth");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "preauth");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	return iResult;
	    }//
			 

	 
	 public ArrayList<Object> getPreauthDocsUploadsList(String preAuthSeqID) throws TTKException {
		  
		 int iResult = 0;
	    	Connection conn = null;
	    	PreparedStatement pStmt1 = null;
	    	ArrayList alMouList	=	new ArrayList();
	    	ResultSet rs1 = null;
	    	PreAuthDetailVO preAuthDetailVO=null;
	    	try{
		    	conn = ResourceManager.getConnection();
		    	pStmt1=conn.prepareStatement(strGetPreauthUploadFils);
		    	pStmt1.setString(1, preAuthSeqID);
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
		                alMouList.add(preAuthDetailVO);
					}//end of while(rs.next())
				}//end of if(rs != null)
		    	//return (ArrayList<Object>)alMouList;           
	        }//end of try
	    	catch (SQLException sqlExp)
	        {
	            throw new TTKException(sqlExp, "preauth");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "preauth");
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
	        			log.error("Error while closing the Statement in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
	        			throw new TTKException(sqlExp, "preauth");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
	        				throw new TTKException(sqlExp, "preauth");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "preauth");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		rs1 = null;
	        		pStmt1 = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	 return alMouList;
	    }
	 
	 
	 public ArrayList<Object> getclaimsDocsUploadsList(String preAuthSeqID) throws TTKException {
		 
		 	int iResult = 0;
	    	Connection conn = null;
	    	PreparedStatement pStmt1 = null;
	    	ArrayList alMouList	=	new ArrayList();
	    	ResultSet rs1 = null;
	    	PreAuthDetailVO preAuthDetailVO=null;
	    	MOUDocumentVO mouDocumentVO	=	null;
	    	try{
		    	conn = ResourceManager.getConnection();
		    	pStmt1=conn.prepareStatement(strGetclaimsUploadFils);
		    	pStmt1.setString(1, preAuthSeqID);
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
	            throw new TTKException(sqlExp, "preauth");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "preauth");
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
	        			log.error("Error while closing the Statement in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
	        			throw new TTKException(sqlExp, "preauth");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
	        				throw new TTKException(sqlExp, "preauth");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "preauth");
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
	 	
	 /**
	     * This method deletes the particulr TdsCertificate Info from the database,based on the certificate seq id's present in the arraylist
	     * @param ArrayList alCertificateDelete
	     * @return int It signifies the number of rows deleted in the database. 
	     * @exception throws TTKException
	     */
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
				throw new TTKException(sqlExp, "preauth");
			}//end of catch(SQLException sqlExp)
			catch(Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
	        			log.error("Error while closing the Statement in PreauthDAOImpl deleteDocsUpload()",sqlExp);
	        			throw new TTKException(sqlExp, "preauth");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PreauthDAOImpl deleteDocsUpload()",sqlExp);
	        				throw new TTKException(sqlExp, "preauth");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "preauth");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return iResult;
		}//end of deleteAssocCertificatesInfo(ArrayList<Object> alCertificateDelete)

	 
	 
	 
	 public ArrayList<String> getProviderSpecificCopay(Long preauthSeqID,String PatOrClm)throws TTKException{
		 Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			ArrayList<String> alProvicerCopay	=	new ArrayList();
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveProviderCopay);
				cStmtObject.setLong(1, preauthSeqID);
				cStmtObject.setString(2, PatOrClm);
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(3);
				if(rs != null){
					if(rs.next()){
						alProvicerCopay.add(TTKCommon.checkNull(rs.getString("COPAY_PERC")));
						alProvicerCopay.add(TTKCommon.checkNull(rs.getString("DEDUCT_AMOUNT")));
						alProvicerCopay.add(TTKCommon.checkNull(rs.getString("SUFFIX")));
					}//end of while(rs.next())
				}//end of if(rs != null)
		    	//return (ArrayList<Object>)alMouList;
					
		    }//end of try
		    catch (SQLException sqlExp) 
		    {
		        throw new TTKException(sqlExp, "policy");
		    }//end of catch (SQLException sqlExp)
		    catch (Exception exp) 
		    {
		        throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PreAuthDAOImpl getProviderSpecificCopay()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
		    		try
		    		{
		    			if (cStmtObject != null) cStmtObject.close();
		    		}//end of try
		    		catch (SQLException sqlExp)
		    		{
		    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderSpecificCopay()",sqlExp);
		    			throw new TTKException(sqlExp, "preauth");
		    		}//end of catch (SQLException sqlExp)
		    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
		    		{
		    			try
		    			{
		    				if(conn != null) conn.close();
		    			}//end of try
		    			catch (SQLException sqlExp)
		    			{
		    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderSpecificCopay()",sqlExp);
		    				throw new TTKException(sqlExp, "preauth");
		    			}//end of catch (SQLException sqlExp)
		    		}//end of finally Connection Close
		    	}//end of try
		    	catch (TTKException exp)
		    	{
		    		throw new TTKException(exp, "preauth");
		    	}//end of catch (TTKException exp)
		    	finally // Control will reach here in anycase set null to the objects 
		    	{
		    		cStmtObject = null;
		    		conn = null;
		    	}//end of finally
			}//end of finally
		    return alProvicerCopay;
	 }//ArrayList<String> getProviderSpecificCopay(String preauthSeqID
	 
		public ArrayList<Object> getBenefitDetails(ArrayList<Object> alSearchCriteria) throws TTKException {
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			ResultSet rs1=null;
			ResultSet rs2=null;
			ResultSet rs3=null;
			boolean recors=false;
			ArrayList<Object> alRes=new ArrayList<>();
			Collection<Object> alResultList = new ArrayList<Object>();
			
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetbenefitDetails);
				if(alSearchCriteria.get(0)!=null && !((String)alSearchCriteria.get(0)).trim().equals(""))cStmtObject.setLong(1,new Long((String) alSearchCriteria.get(0)));
				else cStmtObject.setString(1,null);
				if(alSearchCriteria.get(1)!=null && !((String)alSearchCriteria.get(1)).trim().equals(""))cStmtObject.setLong(2,new Long((String) alSearchCriteria.get(1)));
				else cStmtObject.setString(2,null);
				if(alSearchCriteria.get(2)!=null && !((String)alSearchCriteria.get(2)).trim().equals(""))cStmtObject.setString(3, (String) alSearchCriteria.get(2));
				else cStmtObject.setString(3, null);
				if(alSearchCriteria.get(3)!=null && !((String)alSearchCriteria.get(3)).trim().equals("")) cStmtObject.setLong(4 ,new Long((String)alSearchCriteria.get(3)));
				else cStmtObject.setString(4,null);
				if(alSearchCriteria.get(4)!=null && !((String)alSearchCriteria.get(4)).trim().equals("")) cStmtObject.setString(5 ,(String)alSearchCriteria.get(4));
				else cStmtObject.setString(5,null);
				cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			
				
				cStmtObject.execute();
				 rs = (java.sql.ResultSet)cStmtObject.getObject(6);
				 rs1 = (java.sql.ResultSet)cStmtObject.getObject(7);
				

				
				if(rs != null){
					while(rs.next()){
						recors=true;	
						BenefitsDetailsVO benefitDetailVo=new BenefitsDetailsVO();
						benefitDetailVo.setBenefitName(TTKCommon.checkNull(rs.getString("CLAUSE_NAME")));
					//	benefitDetailVo.setSubBenefit(TTKCommon.checkNull(rs.getString("SUB_BENF_NAME")));
					
						benefitDetailVo.setStrConfigration(rs.getString("RULE_CONFIG")==null?"-":rs.getString("RULE_CONFIG"));
					//	benefitDetailVo.setCoverage(rs.getString("COND_NAME")==null?"-":rs.getString("COND_NAME"));
						benefitDetailVo.setCondition(rs.getString("COND_NAME")==null?"-":rs.getString("COND_NAME"));

						benefitDetailVo.setLimit(rs.getString("LIMIT")==null?"-":rs.getString("LIMIT"));
						benefitDetailVo.setCopay(rs.getString("COPAY_PERC")==null?"-":rs.getString("COPAY_PERC"));
						benefitDetailVo.setDeductible(rs.getString("DEDUCTABLE")==null?"-":rs.getString("DEDUCTABLE"));
						benefitDetailVo.setWaitingPeriod(rs.getString("WAITING_PERIOD")==null?"-":rs.getString("WAITING_PERIOD"));
					    benefitDetailVo.setSessionAllowed(rs.getString("SESSIONS_BENIFIT")==null?"-":rs.getString("SESSIONS_BENIFIT"));
						
						benefitDetailVo.setModeType(rs.getString("MODE_OF_BENIFIT")==null?"-":rs.getString("MODE_OF_BENIFIT"));
					//	benefitDetailVo.setOtherRemarks(rs.getString("OTHER_REMARKS"));
						benefitDetailVo.setLimitUtilised(rs.getString("UTILIZED_AMNT")==null?"-":rs.getString("UTILIZED_AMNT"));
						benefitDetailVo.setLimitAvailable(rs.getString("BALNC_AMNT")==null?"-":rs.getString("BALNC_AMNT"));
						alResultList.add(benefitDetailVo);
					}//end of while(rs.next())
				}//end of if(rs != null)
				//if(!recors)	alResultList.add(new BenefitDetailVo());
				
				alRes.add(alResultList);
				recors=false;
				if(rs1 != null){
					while(rs1.next()){
						PreAuthDetailVO preAuthDetailVO = new PreAuthDetailVO();
						recors=true;
				alRes.add(rs1.getString("AVAILABLE_SUM_INSURED"));
				alRes.add(rs1.getString("UTILISED_SUM_INSURED"));
			
				//preAuthDetailVO.setAvailableSuminsured(new BigDecimal(rs1.getString("AVAILABLE_SUM_INSURED")));
				//preAuthDetailVO.setUtilizeSuminsured(new BigDecimal(rs1.getString("UTILISED_SUM_INSURED")));
				//alRes.add(preAuthDetailVO);
					}
				}
				
				if(!recors) alRes.add(null);
			/*	recors=false;
				if(rs3!=null){
					while(rs3.next()){
						recors=true;
				    alRes.add(rs3.getString("OVERALL_REMARKS"));
					}
				}
				 if(!recors) alRes.add(null);*/
				
				return alRes;
				
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "claim");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
						if (rs1 != null) rs1.close();
						if (rs2 != null) rs2.close();
						if (rs3 != null) rs3.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimDAOImpl getClaimList()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in ClaimDAOImpl getClaimList()",sqlExp);
							throw new TTKException(sqlExp, "claim");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in ClaimDAOImpl getClaimList()",sqlExp);
								throw new TTKException(sqlExp, "claim");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "claim");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					rs1 =null;
					rs2 =null;
					rs3 =null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of getBenefitDetails(ArrayList alSearchCriteria)
		
		public ArrayList<Object> getBenefitDetailsRules(ArrayList<Object> alSearchCriteria) throws TTKException {
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			ResultSet rs1=null;
			ResultSet rs2=null;
			ResultSet rs3=null;
			boolean recors=false;
			ArrayList<Object> alRes=new ArrayList<>();
			Collection<Object> alResultList = new ArrayList<Object>();
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetbenefitDetails);
				if(alSearchCriteria.get(0)!=null && !((String)alSearchCriteria.get(0)).trim().equals(""))cStmtObject.setLong(1,new Long((String) alSearchCriteria.get(0)));
				else cStmtObject.setString(1,null);
				if(alSearchCriteria.get(1)!=null && !((String)alSearchCriteria.get(1)).trim().equals(""))cStmtObject.setLong(2,new Long((String) alSearchCriteria.get(1)));
				else cStmtObject.setString(2,null);
				if(alSearchCriteria.get(2)!=null && !((String)alSearchCriteria.get(2)).trim().equals(""))cStmtObject.setString(3, (String) alSearchCriteria.get(2));
				else cStmtObject.setString(3, null);
				if(alSearchCriteria.get(3)!=null && !((String)alSearchCriteria.get(3)).trim().equals("")) cStmtObject.setLong(4 ,new Long((String)alSearchCriteria.get(3)));
				else cStmtObject.setString(4,null);
				if(alSearchCriteria.get(4)!=null && !((String)alSearchCriteria.get(4)).trim().equals("")) cStmtObject.setString(5 ,(String)alSearchCriteria.get(4));
				else cStmtObject.setString(5,null);
				cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);
				
				cStmtObject.execute();
				 rs = (java.sql.ResultSet)cStmtObject.getObject(6);
				 rs1 = (java.sql.ResultSet)cStmtObject.getObject(7);
				 rs2 = (java.sql.ResultSet)cStmtObject.getObject(8);
				 rs3 = (java.sql.ResultSet)cStmtObject.getObject(9);

				
				if(rs != null){
					while(rs.next()){
						recors=true;	
						BenefitsDetailsVO benefitDetailVo=new BenefitsDetailsVO();
						benefitDetailVo.setBenefitName(TTKCommon.checkNull(rs.getString("BENEFIT_NAME")));
						benefitDetailVo.setSubBenefit(TTKCommon.checkNull(rs.getString("SUB_BENF_NAME")));
						benefitDetailVo.setStrConfigration(rs.getString("COVERAGE_PAY_VAL"));
						benefitDetailVo.setCoverage(rs.getString("COVERAGE"));
						benefitDetailVo.setLimit(rs.getString("LIMIT"));
						benefitDetailVo.setCopay(rs.getString("COPAY"));
						benefitDetailVo.setDeductible(rs.getString("DEDUCTABLE"));
						benefitDetailVo.setWaitingPeriod(rs.getString("MEM_WAITING"));
						benefitDetailVo.setSessionAllowed(rs.getString("SESSION_ALLOWED"));
						
						benefitDetailVo.setModeType(rs.getString("type_mode"));
						benefitDetailVo.setOtherRemarks(rs.getString("OTHER_REMARKS"));
						benefitDetailVo.setLimitUtilised(rs.getString("utilized_limit"));
						benefitDetailVo.setLimitAvailable(rs.getString("balance"));
						alResultList.add(benefitDetailVo);
					}//end of while(rs.next())
				}//end of if(rs != null)
				//if(!recors)	alResultList.add(new BenefitDetailVo());
				
				alRes.add(alResultList);
				recors=false;
				if(rs2 != null){
					while(rs2.next()){
						recors=true;
				alRes.add(rs2.getString("AVAILABLE_SUM_INSURED"));
					}
				}
				
				if(!recors) alRes.add(null);
				recors=false;
				if(rs3!=null){
					while(rs3.next()){
						recors=true;
				    alRes.add(rs3.getString("OVERALL_REMARKS"));
					}
				}
				 if(!recors) alRes.add(null);
				return alRes;
				
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "claim");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
						if (rs1 != null) rs1.close();
						if (rs2 != null) rs2.close();
						if (rs3 != null) rs3.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimDAOImpl getClaimList()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in ClaimDAOImpl getClaimList()",sqlExp);
							throw new TTKException(sqlExp, "claim");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in ClaimDAOImpl getClaimList()",sqlExp);
								throw new TTKException(sqlExp, "claim");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "claim");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					rs1 =null;
					rs2 =null;
					rs3 =null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of getBenefitDetails(ArrayList alSearchCriteria)
		

	public ResultSet getFile(String refNo)  throws TTKException{
		
		Connection conn = null;
		ResultSet resultSet = null;
		PreparedStatement pStmt1 = null;
		try{
			conn = ResourceManager.getConnection();
			pStmt1 = conn.prepareStatement(strGetFile);
			pStmt1.setString(1, refNo);
			resultSet= pStmt1.executeQuery();		
	    }//end of try
		catch(SQLException exception){
			throw new TTKException(exception, "preauth");
		}catch (TTKException e) {
			throw new TTKException(e, "preauth");
		}
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (resultSet != null) resultSet.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PolicyDAOImpl getCustMsgInfo()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt1 != null) pStmt1.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getCustMsgInfo()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getCustMsgInfo()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				resultSet = null;
				pStmt1 = null;
				conn = null;
			}//end of finally
		}//end of finally
	    return resultSet;
	}
		
		
		/**
		 * This method assigns the Pre-Authorization information to the corresponding Doctor
		 * @param preAuthAssignmentVO PreAuthAssignmentVO which contains Pre-Authorization information to assign the corresponding Doctor
		 * @return long value which contains Assign Users Seq ID
		 * @exception throws TTKException
		 */
		public long assignMultiple(PreAuthAssignmentVO preAuthAssignmentVO,String strModeValue) throws TTKException {
			long lngAssignUsersSeqID = 0;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveAssignUsers);
				if(preAuthAssignmentVO.getAssignUserSeqID() != null){
					cStmtObject.setLong(1,preAuthAssignmentVO.getAssignUserSeqID());
				}//end of if(preAuthAssignmentVO.getAssignUserSeqID() != null)
				else{
					cStmtObject.setLong(1,0);
				}//end of else

				if(preAuthAssignmentVO.getSelectedClaimsSeqId() != null){
					cStmtObject.setString(2,preAuthAssignmentVO.getSelectedClaimsSeqId());
				}//end of if(preAuthAssignmentVO.getClaimSeqID() != null)
				else{
					cStmtObject.setString(2,null);
				}//end of else

				if(preAuthAssignmentVO.getSelectedPreAuthSeqId() != null){
					cStmtObject.setString(3,preAuthAssignmentVO.getSelectedPreAuthSeqId());
				}//end of if(preAuthAssignmentVO.getPreAuthSeqID() != null)
				else{
					cStmtObject.setString(3,null);
				}//end of else

				if(preAuthAssignmentVO.getOfficeSeqID() != null){
					cStmtObject.setLong(4,preAuthAssignmentVO.getOfficeSeqID());
				}//end of if(preAuthAssignmentVO.getOfficeSeqID() != null)
				else{
					cStmtObject.setString(4,null);
				}//end of else

				if(preAuthAssignmentVO.getDoctor() != null){
					cStmtObject.setLong(5,preAuthAssignmentVO.getDoctor());
				}//end of if(preAuthAssignmentVO.getDoctor() != null)
				else{
					cStmtObject.setString(5,null);
				}//end of else

				cStmtObject.setString(6,null);
				cStmtObject.setString(7,preAuthAssignmentVO.getRemarks());
				cStmtObject.setString(8,"EXT");
				cStmtObject.setString(9,preAuthAssignmentVO.getSuperUserYN());
				cStmtObject.setLong(10,preAuthAssignmentVO.getUpdatedBy());
				cStmtObject.setString(11,strModeValue);
				cStmtObject.registerOutParameter(12,Types.INTEGER);
				cStmtObject.registerOutParameter(1,Types.BIGINT);
				cStmtObject.execute();
				lngAssignUsersSeqID = cStmtObject.getLong(1);//ASSIGN_USERS_SEQ_ID
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Statement in PreAuthDAOImpl assignPreAuth()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PreAuthDAOImpl assignPreAuth()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
			return lngAssignUsersSeqID;
		}//end of assignPreAuth(PreAuthAssignmentVO preAuthAssignmentVO)
		
		
		public PreAuthAssignmentVO getAssignMultipleTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode) throws TTKException {
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			PreAuthAssignmentVO preauthAssignmentVO = null;
			ArrayList alAssignUserList = new ArrayList();
			ArrayList<Object> alUserList = new ArrayList<Object>();
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetAssignTo);
				cStmtObject.setLong(1,lngAssignUsersSeqID);
				cStmtObject.setLong(2,lngUserSeqID);
				cStmtObject.setString(3,strMode);
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs=(java.sql.ResultSet)cStmtObject.getObject(4);
				if(rs != null){
					while(rs.next()){
						preauthAssignmentVO = new PreAuthAssignmentVO();

						if(rs.getString("ASSIGN_USERS_SEQ_ID") != null){
							preauthAssignmentVO.setAssignUserSeqID(new Long(rs.getLong("ASSIGN_USERS_SEQ_ID")));
						}//end of if(rs.getString("ASSIGN_USERS_SEQ_ID") != null)

						if(rs.getString("SEQ_ID") != null){
							if(strMode.equals("PAT")){
								preauthAssignmentVO.setSelectedPreAuthSeqId((rs.getString("SEQ_ID")));
							}//end of if(strMode.equals("PAT"))
							else{
								preauthAssignmentVO.setSelectedClaimsSeqId((rs.getString("SEQ_ID")));
							}//end of else
						}//end of if(rs.getString("SEQ_ID") != null)

						preauthAssignmentVO.setSelectedPreAuthNos(TTKCommon.checkNull(rs.getString("ID_NUMBER")));
						preauthAssignmentVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
						

						if(rs.getString("ASSIGNED_TO_USER") != null){
							preauthAssignmentVO.setDoctor(new Long(rs.getLong("ASSIGNED_TO_USER")));
						}//end of if(rs.getString("ASSIGNED_TO_USER") != null)

						preauthAssignmentVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));

						if(rs.getString("POLICY_SEQ_ID") != null){
							preauthAssignmentVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
						}//end of if(rs.getString("POLICY_SEQ_ID") != null)

						if(rs.getString("SEQ_ID") != null && rs.getString("TPA_OFFICE_SEQ_ID") != null ){
							alUserList.add(new Long(rs.getLong("SEQ_ID")));

							if(rs.getString("POLICY_SEQ_ID") != null){
								alUserList.add(new Long(rs.getLong("POLICY_SEQ_ID")));
							}//end of if(rs.getString("POLICY_SEQ_ID") != null)
							else{
								alUserList.add(null);
							}//end of else

							alUserList.add(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
							alAssignUserList = getAssignUserList(alUserList,strMode);
							preauthAssignmentVO.setAssignUserList(alAssignUserList);
						}//end of if

					}//end of while(rs.next())
				}//end of if(rs != null)
				return preauthAssignmentVO;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Resultset in PreAuthDAOImpl getAssignTo()",sqlExp);
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
							log.error("Error while closing the Statement in PreAuthDAOImpl getAssignTo()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PreAuthDAOImpl getAssignTo()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of getAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode)

		public ArrayList getDownloadHistoryPATCLM(String strhistoryMode,Long lngMemberSeqId,String memberId,Long policySeqId,String benefitType) throws TTKException 	// test pat
		{
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			
			ArrayList<ReportVO> alRresultList	=	null;
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimPreAuthDownloadHistoryList);
				cStmtObject.setLong(1, lngMemberSeqId);
				cStmtObject.setString(2, strhistoryMode);
				cStmtObject.setString(3, memberId);
				cStmtObject.setLong(4, policySeqId);
				cStmtObject.setString(5, benefitType);
				cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);

				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(6);
				
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

				}
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
						log.error("Error while closing the Resultset in TTKReportDAOImpl getClaimPreAuthAuditReport()",sqlExp);
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
							log.error("Error while closing the Statement in TTKReportDAOImpl getClaimPreAuthAuditReport()",sqlExp);
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
								log.error("Error while closing the Connection in TTKReportDAOImpl getClaimPreAuthAuditReport()",sqlExp);
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
		}

		public ArrayList<ReportVO> getDownloadHistoryDetailsPATCLM(String strhistorymode, Long authseqId) throws TTKException {
			// TODO Auto-generated method stub
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			ArrayList<ReportVO> alRresultList	=	null;
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimPreAuthDownloadHistoryDetail);
				cStmtObject.setLong(1, authseqId);
				cStmtObject.setString(2, strhistorymode);
				
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);

				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(3);

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
						log.error("Error while closing the Resultset in TTKReportDAOImpl getClaimPreAuthAuditReport()",sqlExp);
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
							log.error("Error while closing the Statement in TTKReportDAOImpl getClaimPreAuthAuditReport()",sqlExp);
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
								log.error("Error while closing the Connection in TTKReportDAOImpl getClaimPreAuthAuditReport()",sqlExp);
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
		}

		public ArrayList<Object> getPolicyDetails(String memberId) throws TTKException
		{
			Connection conn = null;
			PreparedStatement pStmt 		= 	null;
			ResultSet rs1=null;
			CacheObject cacheObject = null;
		    ArrayList<CacheObject> PolicyDetails = new ArrayList<CacheObject>();
		    ArrayList<Object> allList = new ArrayList<Object>();
			try {
				 conn = ResourceManager.getConnection();
				 pStmt = conn.prepareStatement(str_PolicyDetails);
				 pStmt.setString(1,memberId);
				 rs1 = pStmt.executeQuery();
				if(rs1!=null){
					while(rs1.next()){
						cacheObject = new CacheObject();
						cacheObject.setCacheId((rs1.getString("POLICY_SEQ_ID")));
		                cacheObject.setCacheDesc(TTKCommon.checkNull(rs1.getString("POLICY_NUMBER")));
		                PolicyDetails.add(cacheObject);
					}
				}
				allList.add(PolicyDetails);
				return allList;
			} 
			 catch (SQLException sqlExp)
		     {
		         throw new TTKException(sqlExp, "preauth");
		     }
			
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (Exception exp)
			
			 finally
				{
					// Nested Try Catch to ensure resource closure  
					try // First try closing the result set
					{
						try
						{
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in PreauthDAOImpl getPolicyDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (pStmt != null) pStmt.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in PreauthDAOImpl getPolicyDetails()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in PreauthDAOImpl getPolicyDetails()",sqlExp);
									throw new TTKException(sqlExp, "preauth");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "preauth");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs1 = null;
						pStmt = null;
						conn = null;
					}//end of finally
				}//end of finally
		}
		public ArrayList getPolicyInfo(String memberId,Long policySeqId) throws TTKException
		{
			Connection conn = null;
			PreparedStatement pStmt 		= 	null;
			ResultSet rs1=null;
			ArrayList PolicyDetails = null;
			try {
				 conn = ResourceManager.getConnection();
				 pStmt = conn.prepareStatement(str_PoicyInfo);
				 if(policySeqId == null){
					 pStmt.setString(1,null);
				  	}
				 else  {
				  pStmt.setLong(1,policySeqId);
				  }
				 
				 if(policySeqId == null) {
					  pStmt.setString(2,null);
				 	}	  
				 else {
					  pStmt.setLong(2,policySeqId);
					  }

				 rs1 = pStmt.executeQuery();
					
				if(rs1!=null){
					while(rs1.next()){
						PolicyDetails = new ArrayList();
						PolicyDetails.add((rs1.getString("POLICY_FLAG")));
						PolicyDetails.add((rs1.getString("POLICY_START_DATE")));
						PolicyDetails.add((rs1.getString("POLICY_END_DATE")));
					}
				}
				
				return PolicyDetails;
			} 
			 catch (SQLException sqlExp)
		     {
		         throw new TTKException(sqlExp, "preauth");
		     }
			
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (Exception exp)
			
			 finally
				{
					//  Nested Try Catch to ensure resource closure  
					try // First try closing the result set
					{
						try
						{
							if (rs1 != null) rs1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Resultset in PreauthDAOImpl getPolicyInfo()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if result set is not closed, control reaches here. Try closing the statement now.
						{
							try
							{
								if (pStmt != null) pStmt.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Statement in PreauthDAOImpl getPolicyInfo()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
							finally // Even if statement is not closed, control reaches here. Try closing the connection now.
							{
								try
								{
									if(conn != null) conn.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in PreauthDAOImpl getPolicyInfo()",sqlExp);
									throw new TTKException(sqlExp, "preauth");
								}//end of catch (SQLException sqlExp)
							}//end of finally Connection Close
						}//end of finally Statement Close
					}//end of try
					catch (TTKException exp)
					{
						throw new TTKException(exp, "preauth");
					}//end of catch (TTKException exp)
					finally // Control will reach here in anycase set null to the objects 
					{
						rs1 = null;
						pStmt = null;
						conn = null;
					}//end of finally
				}//end of finally
		}

		public long saveFraudData(ArrayList<String> listOfinputData) throws TTKException {
			Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ResultSet rs = null;
	    	List  fraudDataList = null;
		    PreAuthDetailVO preAuthDetailVO=null;
	    	try{
               
	    		conn = ResourceManager.getConnection();
	    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strfraudDeatilsSave);
	    		Long preauthSeqId = Long.parseLong(listOfinputData.get(0));
				cStmtObject.setLong(1,preauthSeqId);
				cStmtObject.setString(2, listOfinputData.get(1));
				cStmtObject.setString(3, listOfinputData.get(2));
				cStmtObject.setString(4, listOfinputData.get(3));
				cStmtObject.setString(5, listOfinputData.get(4));
				cStmtObject.setString(6, listOfinputData.get(5));
				cStmtObject.setString(7, listOfinputData.get(6));
				cStmtObject.registerOutParameter(8,OracleTypes.NUMBER);
				cStmtObject.execute();
				long result =  cStmtObject.getLong(8);
			
				return result;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}

		public Object[] getFraudDetails(ArrayList<String> listOfinputData) throws TTKException {
			Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ResultSet internalRemarksrs = null;
	    	ResultSet investigationDetailsrs = null;
	    	
		   
		    Object objArr[] = new Object[2];
		    PreAuthDetailVO preAuthDetailVO=null;
		    ArrayList  internalRemarksrsList = new ArrayList<>();
		    ArrayList investigationDetailList = new ArrayList<>();
	    	try{
               
	    		conn = ResourceManager.getConnection();
	    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strgetfraudDeatils);
	    		Long preauthorClaimSeqId = Long.parseLong(listOfinputData.get(1));
	    		cStmtObject.setString(1,listOfinputData.get(0));
	    		cStmtObject.setLong(2,preauthorClaimSeqId);
				
				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
				cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
				cStmtObject.execute();
				
				internalRemarksrs = (ResultSet) cStmtObject.getObject(3);
				investigationDetailsrs = (ResultSet) cStmtObject.getObject(4);
				if(internalRemarksrs != null){
					
					
					while(internalRemarksrs.next()){
						preAuthDetailVO = new PreAuthDetailVO();
						preAuthDetailVO.setInternalRemarkStatusDesc(TTKCommon.checkNull(internalRemarksrs.getString("int_remk_status")));
						preAuthDetailVO.setInternalRemarkStatus(TTKCommon.checkNull(internalRemarksrs.getString("status_code_id")));
						preAuthDetailVO.setRiskLevelDesc(TTKCommon.checkNull(internalRemarksrs.getString("risk_level_desc")));
						preAuthDetailVO.setRiskLevel(TTKCommon.checkNull(internalRemarksrs.getString("risk_level")));
						preAuthDetailVO.setRemarksforinternalstatus(TTKCommon.checkNull(internalRemarksrs.getString("code_remarks")));
						preAuthDetailVO.setInternalRemarksAddedDate(TTKCommon.checkNull(internalRemarksrs.getString("code_added_date")));
						preAuthDetailVO.setUser(TTKCommon.checkNull(TTKCommon.checkNull(internalRemarksrs.getString("code_added_by"))));
						preAuthDetailVO.setSuspectVerified(TTKCommon.checkNull(internalRemarksrs.getString("suspect_veri_check")));
						preAuthDetailVO.setInvestigationstatus(TTKCommon.checkNull(internalRemarksrs.getString("inv_status")));
						preAuthDetailVO.setInvestigationStatusDesc(TTKCommon.checkNull(internalRemarksrs.getString("cur_inv_status")));
						preAuthDetailVO.setInvestigationoutcomecategory(TTKCommon.checkNull(internalRemarksrs.getString("inv_out_category")));
						preAuthDetailVO.setInvestigationOutcomeCatgDesc(TTKCommon.checkNull(internalRemarksrs.getString("cur_inv_out_category")));
						preAuthDetailVO.setCurrecntIntrRemarkStatus(TTKCommon.checkNull(internalRemarksrs.getString("int_remk_status")));
						preAuthDetailVO.setInvestigationTAT(TTKCommon.checkNull(internalRemarksrs.getString("inv_tat")));
						preAuthDetailVO.setRiskRemarks(TTKCommon.checkNull(internalRemarksrs.getString("risk_remarks")));
						internalRemarksrsList.add(preAuthDetailVO);
					}
					
					
				}
				
				if(investigationDetailsrs != null){
				
					while(investigationDetailsrs.next()){
						preAuthDetailVO = new PreAuthDetailVO();
						preAuthDetailVO.setInvestigationStatusDesc(TTKCommon.checkNull(investigationDetailsrs.getString("inv_status")));
						preAuthDetailVO.setInvestigationOutcomeCatgDesc(TTKCommon.checkNull(investigationDetailsrs.getString("inv_out_category")));
						preAuthDetailVO.setAmountutilizationforinvestigation(TTKCommon.checkNull(investigationDetailsrs.getString("amt_util_for_inv")));
						preAuthDetailVO.setAmountsave(TTKCommon.checkNull(investigationDetailsrs.getString("amount_saved")));
						preAuthDetailVO.setInvestmentStartDate(TTKCommon.checkNull(investigationDetailsrs.getString("inv_start_date")));
						preAuthDetailVO.setInvestigationAddedDate(TTKCommon.checkNull(investigationDetailsrs.getString("added_date")));
						preAuthDetailVO.setUser(TTKCommon.checkNull(TTKCommon.checkNull(investigationDetailsrs.getString("added_by"))));
						preAuthDetailVO.setCfdRemarks(TTKCommon.checkNull(TTKCommon.checkNull(investigationDetailsrs.getString("CFD_REMARKS"))));
					    
						investigationDetailList.add(preAuthDetailVO);
					}
					
					
				}
				
				objArr[0]=internalRemarksrsList;
				objArr[1]=investigationDetailList;
				return objArr;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
			}//end of catch (Exception exp)
			finally
			{
				/* Nested Try Catch to ensure resource closure */
				try // First try closing the result set
				{
					try
					{
						if (internalRemarksrs != null) internalRemarksrs.close();
						if(investigationDetailsrs != null) investigationDetailsrs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					internalRemarksrs=null;
					investigationDetailsrs=null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}

		public long saveFraudDataForCFD(ArrayList<String> listOfinputData) throws TTKException {
			Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ResultSet rs = null;
	    	List  fraudDataList = null;
		    PreAuthDetailVO preAuthDetailVO=null;
	    	try{
               
	    		conn = ResourceManager.getConnection();
	    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strfraudDeatilsForCFDSave);
	    		Long preauthSeqId = Long.parseLong(listOfinputData.get(1));
				cStmtObject.setString(1,listOfinputData.get(0));	// preauthorclaimswitchType
				cStmtObject.setLong(2, preauthSeqId);				// claimOrPreauthSeqId
				cStmtObject.setString(3, listOfinputData.get(2));	// investigationstatus
				cStmtObject.setString(4, listOfinputData.get(3));	// investigationoutcomecategory
				cStmtObject.setString(5, listOfinputData.get(4));	// amountutilizationforinvestigation
				cStmtObject.setString(6, listOfinputData.get(5));	// amountsave
				cStmtObject.setString(7, listOfinputData.get(6));	// remarksforinternalstatus
				cStmtObject.setString(8, listOfinputData.get(7));	// dateOfReceivingCompReqInfo
				cStmtObject.setString(9, listOfinputData.get(8));	// userId	
				cStmtObject.setString(10, "");	
				cStmtObject.setString(11, listOfinputData.get(9));	// riskRemarks
				cStmtObject.setString(12, listOfinputData.get(10));	// risk Level
				cStmtObject.registerOutParameter(13,OracleTypes.NUMBER);
				cStmtObject.execute();
				long result =  cStmtObject.getLong(13);
			
				return result;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}
		

		public ArrayList<Object> getFraudHistory(ArrayList alSearchdata)throws TTKException{		
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			PreAuthDetailVO preAuthDetailVO=null;
			try
			{
				ArrayList<Object> alCopayDetails	=	new ArrayList<Object>();
		        conn = ResourceManager.getConnection();
		        cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strViewFraudHistory);
		        String str = (String) alSearchdata.get(1);
		        long memberseqid =Long.parseLong(str);
		        cStmtObject.setString(1,(String) alSearchdata.get(2));
		        cStmtObject.setLong(2,memberseqid);
		        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
		        cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(3);
				if(rs != null){
					while(rs.next()){
						preAuthDetailVO	=	new PreAuthDetailVO();
						preAuthDetailVO.setClaimOrPreauthNumber(rs.getString("CLAIM_NUMBER"));
						preAuthDetailVO.setInternalRemarkStatusDesc(rs.getString("STATUS_DESC"));
						preAuthDetailVO.setRiskLevelDesc(rs.getString("RISK_LEVEL"));
						preAuthDetailVO.setRemarksforinternalstatus(rs.getString("CODE_REMARKS"));
						preAuthDetailVO.setPreSecdiagnosis(rs.getString("DIAGNOSYS_CODES"));
						preAuthDetailVO.setProviderName(rs.getString("PROVIDER_NAME"));
						preAuthDetailVO.setInvestigationStatusDesc(rs.getString("INV_STATUS"));
						preAuthDetailVO.setInvestigationOutcomeCatgDesc(rs.getString("INV_OUT_CATEGORY"));
						preAuthDetailVO.setCfdRemarks(rs.getString("CFD_REMARKS"));
						alCopayDetails.add(preAuthDetailVO);
					}
				}
				return alCopayDetails;
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
						log.error("Error while closing the Resultset in PreAuthDAOImpl getFraudHistory()",sqlExp);
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
							log.error("Error while closing the Statement in PreAuthDAOImpl getFraudHistory()",sqlExp);
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
								log.error("Error while closing the Connection in PreAuthDAOImpl getFraudHistory()",sqlExp);
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
		
		public ArrayList<UserAssignVO> getUserDetails(Long contactSeqID) throws TTKException{		
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			
			try
			{
				ArrayList<UserAssignVO> alCopayDetails	=	new ArrayList<>();
		        conn = ResourceManager.getConnection();
		        cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreUserDeatils);
		       
		        cStmtObject.setLong(1,contactSeqID);
		        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
		        cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
				if(rs != null){
					while(rs.next()){
						UserAssignVO userVo = new UserAssignVO();
						userVo.setContactSeqId(rs.getString("CONTACT_SEQ_ID"));
						userVo.setContactName(rs.getString("CONTACT_NAME"));
						userVo.setUserAssignflag(rs.getString("stus_sort"));						
						alCopayDetails.add(userVo);	
					}
				}
				return alCopayDetails;
			}//end of try
			
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Resultset in PreAuthDAOImpl getUserDetails()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)	cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PreAuthDAOImpl getUserDetails()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PreAuthDAOImpl getUserDetails()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}

		public TTKReportDataSource getTatReportYesterdat(String parameterValue1) throws TTKException {

			
			Connection conn = null;
			CallableStatement cStmtObject=null;
			ResultSet rs = null;
			ResultSet rsSum = null;
			OracleCachedRowSet crs = null;
	        TTKReportDataSource reportDataSource = null;
	        
	        try {
	        	conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTatYesterdayReport);
				cStmtObject.setString(1,parameterValue1);// COR or IND
		        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
		        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
		        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
		        cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
		        cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
		       
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
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "preauth");
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
						log.error("Error while closing the Resultset in PreAuthDAOImpl getTatReportYesterdat()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)	cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PreAuthDAOImpl getTatReportYesterdat()",sqlExp);
							throw new TTKException(sqlExp, "preauth");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PreAuthDAOImpl getTatReportYesterdat()",sqlExp);
								throw new TTKException(sqlExp, "preauth");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "preauth");
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

		
public TTKReportDataSource getProductivityReportYestCnt(String parameterValue1)throws TTKException{

	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	ResultSet rsSum = null;
	OracleCachedRowSet crs = null;
    TTKReportDataSource reportDataSource = null;
    
    try {
    	conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTatYesterdayReport);
		cStmtObject.setString(1,parameterValue1);// COR or IND
		 cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
        
    	cStmtObject.execute();
       rs = (java.sql.ResultSet)cStmtObject.getObject(5);
       crs = new OracleCachedRowSet();
    	
       if(rs !=null)
		{
			reportDataSource = new TTKReportDataSource();
			crs.populate(rs);
			reportDataSource.setData(crs);
		}//end of if(rs !=null)
    	
    	
	} catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Resultset in PreAuthDAOImpl getProductivityReportYestCnt()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl getProductivityReportYestCnt()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl getProductivityReportYestCnt()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
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

public TTKReportDataSource getTatVidalTeamRpt(String parameterValue1) throws TTKException{

	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	ResultSet rsSum = null;
	OracleCachedRowSet crs = null;
    TTKReportDataSource reportDataSource = null;
    
    try {
    	conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTatYesterdayReport);
		cStmtObject.setString(1,parameterValue1);// COR or IND
        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
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
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Resultset in PreAuthDAOImpl getTatVidalTeamRpt()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl getTatVidalTeamRpt()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl getTatVidalTeamRpt()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
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

public TTKReportDataSource getTatAlkootTeamRpt(String parameterValue1) throws TTKException{

	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	ResultSet rsSum = null;
	OracleCachedRowSet crs = null;
    TTKReportDataSource reportDataSource = null;
    
    try {
    	conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTatYesterdayReport);
		cStmtObject.setString(1,parameterValue1);// COR or IND
        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
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
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Resultset in PreAuthDAOImpl getTatAlkootTeamRpt()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl getTatAlkootTeamRpt()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl getTatAlkootTeamRpt()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
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

public TTKReportDataSource getProductivityReportMTDCnt(String parameterValue1) throws TTKException {

	
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	ResultSet rsSum = null;
	OracleCachedRowSet crs = null;
    TTKReportDataSource reportDataSource = null;
    
    try {
    	conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTatYesterdayReport);
		cStmtObject.setString(1,parameterValue1);// COR or IND
        cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
        cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
    	cStmtObject.execute();
       rs = (java.sql.ResultSet)cStmtObject.getObject(6);
       crs = new OracleCachedRowSet();
    	
       if(rs !=null)
		{
			reportDataSource = new TTKReportDataSource();
			crs.populate(rs);
			reportDataSource.setData(crs);
		}//end of if(rs !=null)
    	
    	
	} catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Resultset in PreAuthDAOImpl getProductivityReportMTDCnt()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null)	cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PreAuthDAOImpl getProductivityReportMTDCnt()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl getProductivityReportMTDCnt()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
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

public PreAuthAssignmentVO getBatchAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode) throws TTKException {
	Connection conn = null;
	CallableStatement cStmtObject=null;
	ResultSet rs = null;
	PreAuthAssignmentVO preauthAssignmentVO = null;
	ArrayList alAssignUserList = new ArrayList();
	ArrayList<Object> alUserList = new ArrayList<Object>();
	try{
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetBatchAssignTo);
		cStmtObject.setLong(1,lngAssignUsersSeqID);
		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
		cStmtObject.execute();
		rs=(java.sql.ResultSet)cStmtObject.getObject(2);
		if(rs != null){
			while(rs.next()){
				preauthAssignmentVO = new PreAuthAssignmentVO();
				if(rs.getString("BATCH_ASSIGN_SEQ") != null){
					preauthAssignmentVO.setAssignUserSeqID(new Long(rs.getLong("BATCH_ASSIGN_SEQ")));
				}
				if(rs.getString("SEQ_ID") != null){
						preauthAssignmentVO.setSelectedBatchSeqId(rs.getString("SEQ_ID"));
				}	
				preauthAssignmentVO.setSelectedBatchNos(TTKCommon.checkNull(rs.getString("ID_NUMBER")));
				preauthAssignmentVO.setOfficeSeqID(new Long(rs.getLong("OFFICE_SEQ_ID")));
				if(rs.getString("ASSINGED_TO_USER") != null){
					preauthAssignmentVO.setDoctor(new Long(rs.getLong("ASSINGED_TO_USER")));
				}

				if(rs.getString("POLICY_SEQ_ID") != null){
					preauthAssignmentVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
				}//end of if(rs.getString("POLICY_SEQ_ID") != null)

				//preauthAssignmentVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));

				if(rs.getString("SEQ_ID") != null && rs.getString("OFFICE_SEQ_ID") != null ){
					alUserList.add(new Long(rs.getLong("SEQ_ID")));
				if(rs.getString("POLICY_SEQ_ID") != null){
						alUserList.add(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)
				else{
						alUserList.add(null);
					}//end of else
					alUserList.add(new Long(rs.getLong("OFFICE_SEQ_ID")));
					alAssignUserList = getAssignUserList(alUserList,strMode);
					preauthAssignmentVO.setAssignUserList(alAssignUserList);
				}//end of if

			}//end of while(rs.next())
		}//end of if(rs != null)
		return preauthAssignmentVO;
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Resultset in PreAuthDAOImpl getAssignTo()",sqlExp);
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
					log.error("Error while closing the Statement in PreAuthDAOImpl getAssignTo()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PreAuthDAOImpl getAssignTo()",sqlExp);
						throw new TTKException(sqlExp, "preauth");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
}//end of getAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode)

public long assignBatchSave(PreAuthAssignmentVO preAuthAssignmentVO,String strModeValue) throws TTKException {
	long lngAssignUsersSeqID = 0;
	Connection conn = null;
	CallableStatement cStmtObject=null;
	try{
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveAssignBatch);
		if(preAuthAssignmentVO.getSelectedBatchSeqId() != null){
			cStmtObject.setString(1,preAuthAssignmentVO.getSelectedBatchSeqId());					//Batch seq id
		}//end of if(preAuthAssignmentVO.getAssignUserSeqID() != null)
		else{
			cStmtObject.setString(1,null);
		}//end of else

		if(preAuthAssignmentVO.getOfficeSeqID() != null){
			cStmtObject.setLong(2,preAuthAssignmentVO.getOfficeSeqID());					//office seq id
		}//end of if(preAuthAssignmentVO.getOfficeSeqID() != null)
		else{
			cStmtObject.setString(2,null);
		}//end of else	
		
		if(preAuthAssignmentVO.getDoctor() != null){//
			cStmtObject.setLong(3,preAuthAssignmentVO.getDoctor());								//To whom we are assigning, That user seq id we have to pass
		}//end of if(preAuthAssignmentVO.getDoctor() != null)
		else{
			cStmtObject.setString(3,null);
		}//end of else
		
		cStmtObject.setString(4,preAuthAssignmentVO.getAssignClaimStatus());		//Assign Claim Status																								//Assign Claim Status

		cStmtObject.setString(5,preAuthAssignmentVO.getAssignRemarks());				//Assign remarks which are selected by user
		
		cStmtObject.setString(6,preAuthAssignmentVO.getOtherRemarks());				//Other remarks which are entered by user
		
		cStmtObject.setLong(7,preAuthAssignmentVO.getUpdatedBy());						//Logged on user seq id
		
		cStmtObject.registerOutParameter(8,Types.INTEGER);
		cStmtObject.execute();
		lngAssignUsersSeqID = cStmtObject.getLong(8);//ASSIGN_USERS_SEQ_ID
	}//end of try
	catch (SQLException sqlExp)
	{
		throw new TTKException(sqlExp, "preauth");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp)
	{
		throw new TTKException(exp, "preauth");
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
				log.error("Error while closing the Statement in PreAuthDAOImpl assignPreAuth()",sqlExp);
				throw new TTKException(sqlExp, "preauth");
			}//end of catch (SQLException sqlExp)
			finally // Even if statement is not closed, control reaches here. Try closing the connection now.
			{
				try
				{
					if(conn != null) conn.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Connection in PreAuthDAOImpl assignPreAuth()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
			}//end of finally Connection Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "preauth");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
	return lngAssignUsersSeqID;
}//end of assignPreAuth(PreAuthAssignmentVO preAuthAssignmentVO)

public ArrayList<String[]> getOverrideRemarksList(String flag, Long claimSeqId) throws TTKException {
	 Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		
		ArrayList<String[]> alRemarksList	=	null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetOverrideRemarksList);
			cStmtObject.setString(1, flag);
			cStmtObject.setLong(2, claimSeqId);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(rs != null){
				alRemarksList= new ArrayList<String[]>();
				while(rs.next()){
				String status=TTKCommon.checkNull(rs.getString("status"));	
				String decision=TTKCommon.checkNull(rs.getString("decision"));	
				String decision_time=TTKCommon.checkNull(rs.getString("decision_time"));	
				String decision_date=TTKCommon.checkNull(rs.getString("decision_date"));	
				String REQ_AMT=TTKCommon.checkNull(rs.getString("REQ_AMT"));	
				String net_amt=TTKCommon.checkNull(rs.getString("net_amt"));	
				String alw_amt=TTKCommon.checkNull(rs.getString("alw_amt"));	
				String override_main_remarks=TTKCommon.checkNull(rs.getString("override_main_remarks"));	
				String override_other_remarks=TTKCommon.checkNull(rs.getString("override_other_remarks"));	
				String sno = rs.getString("sno");
				
				alRemarksList.add(new String[]{sno,status,decision,decision_time,decision_date,REQ_AMT,net_amt,alw_amt,override_main_remarks,override_other_remarks});	
				
				}//end of while(rs.next())
			}//end of if(rs != null)
	    	//return (ArrayList<Object>)alMouList;
			 return alRemarksList;	
	    }//end of try
	    catch (SQLException sqlExp) 
	    {
	        throw new TTKException(sqlExp, "policy");
	    }//end of catch (SQLException sqlExp)
	    catch (Exception exp) 
	    {
	        throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PreAuthDAOImpl getOverrideRemarksList()",sqlExp);
					throw new TTKException(sqlExp, "preauth");
				}//end of catch (SQLException sqlExp)
	    		try
	    		{
	    			if (cStmtObject != null) cStmtObject.close();
	    		}//end of try
	    		catch (SQLException sqlExp)
	    		{
	    			log.error("Error while closing the Statement in PolicyDAOImpl getOverrideRemarksList()",sqlExp);
	    			throw new TTKException(sqlExp, "preauth");
	    		}//end of catch (SQLException sqlExp)
	    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	    		{
	    			try
	    			{
	    				if(conn != null) conn.close();
	    			}//end of try
	    			catch (SQLException sqlExp)
	    			{
	    				log.error("Error while closing the Connection in PolicyDAOImpl getOverrideRemarksList()",sqlExp);
	    				throw new TTKException(sqlExp, "preauth");
	    			}//end of catch (SQLException sqlExp)
	    		}//end of finally Connection Close
	    	}//end of try
	    	catch (TTKException exp)
	    	{
	    		throw new TTKException(exp, "preauth");
	    	}//end of catch (TTKException exp)
	    	finally // Control will reach here in anycase set null to the objects 
	    	{
	    		cStmtObject = null;
	    		conn = null;
	    	}//end of finally
		}//end of finally
}

}//end of PreAuthDAOImpl