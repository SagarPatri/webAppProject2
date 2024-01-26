/**

 * @ (#) ClaimDAOImpl.java Jul 15, 2006
 * Project 	     : TTK HealthCare Services
 * File          : ClaimDAOImpl.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Jul 15, 2006
 *
 * @author       :  RamaKrishna K M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dao.impl.claims;



import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import java.util.Map;

import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.common.CommonClosure;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.PolicyClauseVO;
import com.ttk.dto.claims.BulkGeneratedPDFVO;
import com.ttk.dto.claims.ClaimDetailVO;
import com.ttk.dto.claims.ClaimInwardDetailVO;
import com.ttk.dto.claims.ClaimInwardVO;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;
import com.ttk.dto.claims.ClauseVO;
import com.ttk.dto.claims.DocumentChecklistVO;
import com.ttk.dto.claims.HospitalizationDetailVO;
import com.ttk.dto.claims.ShortfallRequestDetailsVO;
import com.ttk.dto.claims.ShortfallStatusVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.finance.ChequeVO;
import com.ttk.dto.hospital.ProviderClaimsVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.AuthorizationVO;
import com.ttk.dto.preauth.ClaimantVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthHospitalVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.ShortfallVO;

public class ClaimDAOImpl implements BaseDAO, Serializable{

	private static Logger log = Logger.getLogger(ClaimDAOImpl.class );

	/*private static final String strGetClaimInwardList = "{CALL CLAIMS_SQL_PKG.SELECT_CLM_INWARD_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetClaimInwardDetail = "{CALL CLAIMS_SQL_PKG.SELECT_CLM_INWARD_DTL(?,?,?,?,?)}";
	private static final String strGetClaimDocumentList = "{CALL CLAIMS_SQL_PKG.SELECT_CLM_INWARD_DOCUMENT(?,?)}";*/
	private static final String strGetClaimInwardList = "{CALL CLAIMS_PKG.SELECT_CLM_INWARD_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetClaimInwardDetail = "{CALL CLAIMS_PKG.SELECT_CLM_INWARD_DTL(?,?,?,?,?)}";
	private static final String strClaimShortfallList = "{CALL CLAIM_PKG.SELECT_CLM_SHORTFALL_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strInwardShortfallDetails = "{CALL CLAIMS_PKG.SELECT_OPEN_SHORTFALL_LIST(?,?,?)}";
	private static final String strGetClaimDocumentList = "{CALL CLAIMS_PKG.SELECT_CLM_INWARD_DOCUMENT(?,?)}";
	private static final String strSaveClaimInwardDetail = "{CALL CLAIMS_PKG.SAVE_CLM_INWARD_ENTRY(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSaveDocumentChecklist = "{CALL CLAIMS_PKG.SAVE_CLM_DOCUMENT(";
	/*private static final String strGetClaimList = "{CALL CLAIMS_SQL_PKG.SELECT_CLAIMS_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetClaimDetail = "{CALL CLAIMS_SQL_PKG.SELECT_CLAIM(?,?,?,?,?)}";*/
	private static final String strGetClaimList = "{CALL CLAIM_PKG.SELECT_CLAIMS_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetMemberDetails = "{CALL AUTHORIZATION_PKG.SELECT_MEMBER(?,?)}";
	private static final String strGetClaimDetail = "{CALL CLAIM_PKG.SELECT_CLAIM_DETAILS(?,?,?,?,?,?,?,?)}";
	private static final String strGetMemClaimList = "{CALL CLAIM_PKG.SELECT_PREV_CLAIM(?,?,?,?,?,?)}";
	private static final String strReleasePreauth = "{CALL CLAIMS_PKG.RELEASE_PREAUTH(?)}";
    private static final String strSaveClaimDetail = "{CALL CLAIMS_PKG.SAVE_CLAIM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//one parameter for OPD_4_hosptial//added as per KOC  1285 //1 param for  koc insurance reference No
    private static final String strSaveClaimDetails = "{CALL CLAIM_PKG.SAVE_CLM_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String strCalculateClaimAmounts = "{CALL CLAIM_PKG.CALCULATE_AUTHORIZATION(?,?,?,?,?)}";
    private static final String strClaimAmountApproved = "{CALL CLAIM_PKG.SAVE_SETTLEMENT(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    //private static final String strSaveClaimDetail = "{CALL CLAIMS_PKG.SAVE_CLAIM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//added as per KOC  1285 //1 param for  koc insurance reference No
	/*private static final String strGetPreauthList = "{CALL CLAIMS_SQL_PKG.SELECT_PREAUTH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strPrevClaimList = "{CALL CLAIMS_SQL_PKG.SELECT_PREV_CLAIM(?,?)}";*/
	private static final String strGetPreauthList = "{CALL CLAIM_PKG.SELECT_PREAUTH_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strPrevClaimList = "{CALL CLAIMS_PKG.SELECT_PREV_CLAIM(?,?)}";
	private static final String strSaveReview = "{CALL CLAIMS_PKG.SET_REVIEW(?,?,?,?,?,?,?,?,?,?,?)}";
	//private static final String strPrevHospList = "{CALL CLAIMS_SQL_PKG.SELECT_PREV_HOSPS(?,?,?)}";
	private static final String strPrevHospList = "{CALL CLAIMS_PKG.SELECT_PREV_HOSPS(?,?,?,?)}";
	//private static final String strNHCPPrevHospList = "{CALL CLAIMS_PKG.SELECT_HOSP_NHCP(?,?)}";
	//private static final String strPrevHospDetails = "{CALL CLAIMS_SQL_PKG.SELECT_HOSP_ALL(?,?)}";
	private static final String strPrevHospDetails = "{CALL CLAIMS_PKG.SELECT_HOSP_ALL(?,?)}";
	private static final String strDeleteClaimGeneral = "{CALL CLAIMS_PKG.DELETE_CLM_GENERAL(?,?,?,?,?,?)}";
	private static final String strAssignUserList = "{CALL CLAIMS_PKG.MANUAL_ASSIGN_PREAUTH(?,?,?,?)}";
	private static final String strOverrideClaim = "{CALL CLAIMS_PKG.OVERRIDE_CLAIMS(?,?,?,?,?,?,?)}";
	private static final String strGetClauseDetail = "{CALL CLAIMS_PKG.SELECT_REJECTION_CLAUSES(?,?,?,?,?,?,?)}";
	private static final String strSaveClauseDetail = "{CALL CLAIMS_PKG.SAVE_REJECTION_CLAUSES(?,?,?,?,?,?,?,?,?)}";
	private static final String strRejectionLetterList = "{CALL CLAIMS_PKG.GET_LETTER_TO(?,?,?)}";
	private static final String strReAssignEnrID = "{CALL CLAIMS_PKG.RE_ASSIGN_ENRL_ID(?,?,?,?,?)} ";
	private static final String strServTaxCal ="{CALL CLAIMS_PKG.CALC_SERVICE_TAX(?,?,?,?,?,?)}";
	//Shortfall CR KOC1179
	private static final String strSendShortfallRequestList = "{CALL PRE_CLM_REPORTS_PKG.SEND_SHRTFALL_REQUEST(?,?,?,?,?)}";
	//added for Mail-SMS Template for Cigna
	private static final String strSendCignaShortfallRequestList = "{CALL PRE_CLM_REPORTS_PKG.send_cigna_shrtfall_request(?,?,?,?,?)}";
    //ended
	//Shortfall CR KOC1179
	private static final String strGetShortfallRequestDetails = "{CALL PRE_CLM_REPORTS_PKG.GET_SHORTFAL_REQUEST_DTLS(?,?)}";
	//Shortfall CR KOC1179
	private static final String strResendShortfallEmailRequest = "{CALL GENERATE_MAIL_PKG.GENERATE_CLM_SHORTFAL_MAILS(?,?,?,?,?,?)}";

	//added for Mail-SMS Template for Cigna
	private static final String strResendCignaShortfallEmailRequest = "{CALL PRE_CLM_REPORTS_PKG.cigna_shrtfl_req_for_resend(?,?,?,?,?)}";
    //ended
	//Shortfall CR KOC1179
	private static final String strGetShortfallRequestList = "{CALL PRE_CLM_REPORTS_PKG.SELECT_SHORTFAL_REQUESTS(?,?,?,?,?,?,?,?,?,?,?)}";
	//Shortfall CR KOC1179
	private static final String strGetShortfallGenerateSendList = "{CALL PRE_CLM_REPORTS_PKG.GENERATE_SHRTFALL_REQUEST_LTR(?,?,?,?)}";
	//Shortfall CR KOC1179
	private static final  String strSaveBulkPDFname = "{CALL PRE_CLM_REPORTS_PKG.SAVE_SRTFLL_FILE_NAME(?,?,?,?,?,?)}";
    //Shortfall CR KOC1179
	private static final  String strviewShortFallAdvice="{CALL PRE_CLM_REPORTS_PKG.VIEW_SHORTFALL_ADVICE(?,?,?,?,?,?,?,?)}";
	private static final  String strGetBulkPDFilename = "{CALL PRE_CLM_REPORTS_PKG.GET_SRTFLL_FILE_NAME(?,?)}";
	private static final  String strOverridClaimDetails = "{CALL CLAIM_PKG.OVERRIDE_CLAIM(?,?,?,?,?,?)}";
	private static final String strGetdiagDetails="SELECT DC.DENIAL_CODE,DC.DENIAL_DESCRIPTION FROM  APP.TPA_DENIAL_CODES DC WHERE MAN_OVERRIDE_YN IS NOT NULL ORDER BY  DC.SORT_NO ASC";
	private static final String strRejectGetClaimDetail="{CALL claim_pkg.control_reject_claims(?,?,?,?,?,?,?)} ";
	
	private static final String strPat_details = "{CALL AUTHORIZATION_PKG.SELECT_PAT_AUTH(?,?,?)}";
	private static final String strClmSettlementDtls = "{CALL CLAIM_PKG.CLAIM_PAYMENT_DETAILS(?,?)}";
	private static final String strfraudDeatilsSave = "{CALL CLAIM_PKG.INTERNAL_REMARK_STAT_SAVE(?,?,?,?,?,?)}";
	
	private static final String strGetfraudDetails = "select li.user_id as code_added_by,to_char(code_added_date,'dd/mm/yyyy hh:mi AM') code_added_date,status_code_id,clm.code_remarks,nvl(clm.completed_yn,'N')  AS completed_yn, nvl(clm.suspect_veri_check,'N') AS suspect_veri_check from clm_authorization_details clm join tpa_login_info li on(li.contact_seq_id=clm.code_added_by)where clm.claim_seq_id=?";
	private static final String strSelectClaimAndPreauthList = "{CALL CLAIM_PKG.select_cfd_pat_clm_list(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strClaimDetailedReportData = "{CALL claim_pkg.select_clm_disc_rpt_list(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	private static final String strGetProcessedClaimList ="{CALL Processed_claims_pkg.select_Prc_claims_list(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetProcessedClaimDetail = "{CALL Processed_claims_pkg. select_claim_details(?,?,?,?)}";
	

	private static final String strCFDCompaginList = "{CALL claim_pkg.select_cfd_campaign_list(?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSaveCampaignDetail = "{CALL claim_pkg.save_cfd_campaign_dtls(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetCampaignDetails = "{CALL claim_pkg.select_cfd_campaign(?,?)}";


	
	
	private static final String strOverrideRemarksSubGrpName = "SELECT GENERAL_TYPE_ID,DESCRIPTION FROM APP.TPA_GENERAL_CODE GC WHERE GC.HEADER_TYPE=? ORDER BY GC.SORT_NO";
	private static final String strOverrideGenRemarksSubGrpList = "SELECT GENERAL_TYPE_ID,DESCRIPTION FROM APP.TPA_GENERAL_CODE GC WHERE GC.HEADER_TYPE=? ORDER BY GC.SORT_NO";
	
	private static final String strGetAutoRejectedClaimDetail = "{CALL CLAIM_BULK_UPLOAD.select_auto_rej_clm_dtls(?,?,?,?)}";
	private static final String strSaveAutoRejectedClaimDetails = "{CALL CLAIM_BULK_UPLOAD.save_changed_data(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strRevaluateClaimDtls = "{CALL CLAIM_BULK_UPLOAD.re_evaluate(?,?,?,?)}";
	private static final String strGetExceptionalFlag = "{CALL CLAIM_PKG.save_Exception_details(?,?,?)}";
	
	private static final String strReSubClaimReportDetails="{CALL CLAIM_BULK_UPLOAD.resub_claim_search_report(?,?,?,?)}";
	
	private static final int CLAIMS_INWARD_SEQ_ID = 1;
	private static final int TPA_OFFICE_SEQ_ID = 2;
	private static final int BARCODE_NO = 3;
	private static final int DOCUMENT_GENERAL_TYPE_ID = 4;
	private static final int RCVD_DATE = 5;
	private static final int SOURCE_GENERAL_TYPE_ID = 6;
	private static final int CLAIM_GENERAL_TYPE_ID = 7;
	private static final int REQUESTED_AMOUNT = 8;
	private static final int MEMBER_SEQ_ID = 9;
	private static final int TPA_ENROLLMENT_ID = 10;
	private static final int CLAIMANT_NAME = 11;
	private static final int POLICY_SEQ_ID = 12;
	private static final int POLICY_NUMBER = 13;
	private static final int INWARD_INS_SEQ_ID = 14;
	private static final int CORPORATE_NAME = 15;
	private static final int POLICY_HOLDER_NAME = 16;
	private static final int EMPLOYEE_NO =17;
	private static final int EMPLOYEE_NAME = 18;
	private static final int INWARD_STATUS_GENERAL_TYPE_ID = 19;
	private static final int REMARKS = 20;
	private static final int COURIER_SEQ_ID = 21;
	private static final int PARENT_CLAIM_SEQ_ID = 22;
	private static final int CLAIM_NUMBER = 23;
	private static final int SHORTFALL_ID = 24;
	private static final int CLAIM_SEQ_ID = 25;
	private static final int CLM_ENROLL_DETAIL_SEQ_ID = 26;
	private static final int EMAIL_ID = 27;
	private static final int NOTIFICATION_PHONE_NUMBER = 28;
	private static final int INS_SCHEME = 29;
	private static final int CERTIFICATE_N0 = 30;
	private static final int INS_CUSTOMER_CODE = 31;
	private static final int USER_SEQ_ID = 32;
	private static final int ROW_PROCESSED = 33;
 //koc1179
	
    /**
     * This method returns the Arraylist of PreAuthVO's  which contains Claim Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Claim details
     * @exception throws TTKException
     */
    public ArrayList getShortfallGenerateSend(ArrayList alSearchCriteria) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        Collection<Object> alResultList = new ArrayList<Object>();
        ShortfallStatusVO shortfallStatusVO=null;
        String strGroupID="";
        String strPolicyGrpID ="";
        ArrayList alUserGroup = new ArrayList();


        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetShortfallGenerateSendList);
             cStmtObject.setString(1,(String)alSearchCriteria.get(0));
            cStmtObject.setString(2,(String)alSearchCriteria.get(1));
            cStmtObject.setString(3,(String)alSearchCriteria.get(2));
            cStmtObject.setString(4,(String)alSearchCriteria.get(3));
             cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(4);
            if(rs != null){
                while(rs.next()){


                  //  alResultList.add();
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getShortfallGenerateSend(ArrayList alSearchCriteria)

	/**
	 * This method returns the Arraylist of ClaimInwardVO's  which contains Claim Inward Details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of ClaimInwardVO object which contains Claim Inward details
	 * @exception throws TTKException
	 */
	public ArrayList getClaimInwardList(ArrayList alSearchCriteria) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Collection<Object> alResultList = new ArrayList<Object>();
		ClaimInwardVO claimInwardVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimInwardList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));
			cStmtObject.setString(10,(String)alSearchCriteria.get(10));
			cStmtObject.setString(11,(String)alSearchCriteria.get(11));
			cStmtObject.setString(12,(String)alSearchCriteria.get(12));
			cStmtObject.setString(13,(String)alSearchCriteria.get(13));
			cStmtObject.setString(14,(String)alSearchCriteria.get(14));
			cStmtObject.setString(15,(String)alSearchCriteria.get(15));
			cStmtObject.setLong(16,(Long)alSearchCriteria.get(9));
			cStmtObject.registerOutParameter(17,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(17);
			if(rs != null){
				while(rs.next()){
					claimInwardVO = new ClaimInwardVO();

					if(rs.getString("CLAIMS_INWARD_SEQ_ID") != null){
						claimInwardVO.setInwardSeqID(new Long(rs.getLong("CLAIMS_INWARD_SEQ_ID")));
					}//end of if(rs.getString("CLAIMS_INWARD_SEQ_ID") != null)

					claimInwardVO.setInwardNbr(TTKCommon.checkNull(rs.getString("CLAIMS_INWARD_NO")));

					if(rs.getString("RCVD_DATE") != null){
						claimInwardVO.setReceivedDate(new Date(rs.getTimestamp("RCVD_DATE").getTime()));
					}//end of if(rs.getString("RCVD_DATE") != null)

					if(rs.getString("CLAIM_SEQ_ID") != null){
						claimInwardVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
					}//end of if(rs.getString("CLAIM_SEQ_ID") != null)

					if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null){
						claimInwardVO.setClmEnrollDtlSeqID(new Long(rs.getLong("CLM_ENROLL_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null)
					claimInwardVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					claimInwardVO.setClaimantName(TTKCommon.checkNull(rs.getString("CLAIMANT_NAME")));
					claimInwardVO.setGroupName(TTKCommon.checkNull(rs.getString("CORPORATE_NAME")));
					claimInwardVO.setClaimTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					claimInwardVO.setClaimTypeID(TTKCommon.checkNull(rs.getString("CLAIM_GENERAL_TYPE_ID")));
					claimInwardVO.setDocumentTypeID(TTKCommon.checkNull(rs.getString("DOCUMENT_GENERAL_TYPE_ID")));
					alResultList.add(claimInwardVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimInwardList()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimInwardList()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimInwardList()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimInwardList(ArrayList alSearchCriteria)

	/**
	 * This method returns the ClaimInwardDetailVO, which contains all the Claim Inward details
	 * @param lngClaimInwardSeqID long value contains seq id to get the Claim Inward Details
	 * @param strClaimTypeID contains Claim Type
	 * @param lngUserSeqID long value contains Logged-in User Seq ID
	 * @return ClaimInwardDetailVO object which contains all the Claim Inward details
	 * @exception throws TTKException
	 */
	public ClaimInwardDetailVO getClaimInwardDetail(long lngClaimInwardSeqID,String strClaimTypeID,long lngUserSeqID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ClaimInwardDetailVO claimInwardDetailVO = new ClaimInwardDetailVO();
		ClaimantVO claimantVO = null;
		ArrayList<Object> alDocumentList = new ArrayList<Object>();
		ArrayList alPrevClaimList = new ArrayList();
		ArrayList<Object> alShortfallList = new ArrayList<Object>();
		ArrayList alShortfallDetails = new ArrayList();
		DocumentChecklistVO documentChecklistVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimInwardDetail);
			cStmtObject.setLong(1,lngClaimInwardSeqID);
			cStmtObject.setString(2,strClaimTypeID);
			cStmtObject.setLong(3,lngUserSeqID);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(4);
			rs2 = (java.sql.ResultSet)cStmtObject.getObject(5);
			if(rs1 != null){
				while(rs1.next()){

					claimantVO = new ClaimantVO();

					if(rs1.getString("CLAIMS_INWARD_SEQ_ID") != null){
						claimInwardDetailVO.setInwardSeqID(new Long(rs1.getLong("CLAIMS_INWARD_SEQ_ID")));
					}//end of if(rs1.getString("CLAIMS_INWARD_SEQ_ID") != null)

					claimInwardDetailVO.setInwardNbr(TTKCommon.checkNull(rs1.getString("CLAIMS_INWARD_NO")));
					claimInwardDetailVO.setBarCodeNbr(TTKCommon.checkNull(rs1.getString("BARCODE_NO")));
					claimInwardDetailVO.setDocumentTypeID(TTKCommon.checkNull(rs1.getString("DOCUMENT_GENERAL_TYPE_ID")));
					claimInwardDetailVO.setReceivedDate(rs1.getString("RCVD_DATE")!=null ? new Date(rs1.getTimestamp("RCVD_DATE").getTime()):null);
					claimInwardDetailVO.setSourceTypeID(TTKCommon.checkNull(rs1.getString("SOURCE_GENERAL_TYPE_ID")));

					if(rs1.getString("COURIER_SEQ_ID") != null){
						claimInwardDetailVO.setCourierSeqID(new Long(rs1.getLong("COURIER_SEQ_ID")));
					}//end of if(rs1.getString("COURIER_SEQ_ID") != null)

					claimInwardDetailVO.setClaimTypeID(TTKCommon.checkNull(rs1.getString("CLAIM_GENERAL_TYPE_ID")));

					if(rs1.getString("REQUESTED_AMOUNT") != null){
						claimInwardDetailVO.setRequestedAmt(new BigDecimal(rs1.getString("REQUESTED_AMOUNT")));
					}//end of if(rs1.getString("REQUESTED_AMOUNT") != null)

					claimantVO.setEnrollmentID(TTKCommon.checkNull(rs1.getString("TPA_ENROLLMENT_ID")));
					claimantVO.setName(TTKCommon.checkNull(rs1.getString("CLAIMANT_NAME")));
					claimantVO.setPolicyNbr(TTKCommon.checkNull(rs1.getString("POLICY_NUMBER")));

					if(rs1.getString("INWARD_INS_SEQ_ID") != null){
						claimantVO.setInsSeqID(new Long(rs1.getLong("INWARD_INS_SEQ_ID")));
					}//end of if(rs1.getString("INWARD_INS_SEQ_ID") != null)

					if(rs1.getString("TPA_OFFICE_SEQ_ID") != null){
						claimInwardDetailVO.setOfficeSeqID(new Long(rs1.getLong("TPA_OFFICE_SEQ_ID")));
					}//end of if(rs1.getString("TPA_OFFICE_SEQ_ID") != null)

					claimInwardDetailVO.setOfficeName(TTKCommon.checkNull(rs1.getString("OFFICE_NAME")));
					claimantVO.setGroupName(TTKCommon.checkNull(rs1.getString("CORPORATE_NAME")));
					claimantVO.setPolicyHolderName(TTKCommon.checkNull(rs1.getString("POLICY_HOLDER_NAME")));
					claimantVO.setEmployeeName(TTKCommon.checkNull(rs1.getString("EMPLOYEE_NAME")));
					claimantVO.setEmployeeNbr(TTKCommon.checkNull(rs1.getString("EMPLOYEE_NO")));
					claimantVO.setEmailID(TTKCommon.checkNull(rs1.getString("EMAIL_ID")));
					claimantVO.setNotifyPhoneNbr(TTKCommon.checkNull(rs1.getString("NOTIFICATION_PHONE_NUMBER")));
					claimantVO.setInsScheme(TTKCommon.checkNull(rs1.getString("INS_SCHEME")));
					claimantVO.setCertificateNo(TTKCommon.checkNull(rs1.getString("CERTIFICATE_NO")));
					claimantVO.setInsCustCode(TTKCommon.checkNull(rs1.getString("INS_CUSTOMER_CODE")));

					if(rs1.getString("POLICY_SEQ_ID") != null){
						claimantVO.setPolicySeqID(new Long(rs1.getLong("POLICY_SEQ_ID")));
					}//end of if(rs1.getString("POLICY_SEQ_ID") != null)

					if(rs1.getString("MEMBER_SEQ_ID") != null){
						claimantVO.setMemberSeqID(new Long(rs1.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs1.getString("MEMBER_SEQ_ID") != null)

					if(rs1.getString("CLAIM_SEQ_ID") != null){
						claimInwardDetailVO.setClaimSeqID(new Long(rs1.getLong("CLAIM_SEQ_ID")));
					}//end of if(rs1.getString("CLAIM_SEQ_ID") != null)

					if(rs1.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null){
						claimInwardDetailVO.setClmEnrollDtlSeqID(new Long(rs1.getLong("CLM_ENROLL_DETAIL_SEQ_ID")));
					}//end of if(rs1.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null)

					claimInwardDetailVO.setStatusTypeID(TTKCommon.checkNull(rs1.getString("INWARD_STATUS_GENERAL_TYPE_ID")));
					claimInwardDetailVO.setRemarks(TTKCommon.checkNull(rs1.getString("REMARKS")));

					if(rs1.getString("SHORTFALL_SEQ_ID") != null){
						claimInwardDetailVO.setShortfallSeqID(new Long(rs1.getLong("SHORTFALL_SEQ_ID")));
					}//end of if(rs1.getString("SHORTFALL_SEQ_ID") != null)

					claimInwardDetailVO.setShortfallID(TTKCommon.checkNull(rs1.getString("SHORTFALL_ID")));
					if(rs1.getString("SHORTFALL_CLAIM_SEQ_ID") != null){
						claimantVO.setClmSeqID(new Long(rs1.getLong("SHORTFALL_CLAIM_SEQ_ID")));
					}//end of if(rs1.getLong("SHORTFALL_CLAIM_SEQ_ID") != null)

					if(TTKCommon.checkNull(rs1.getString("DOCUMENT_GENERAL_TYPE_ID")).equals("DTS")){
						claimantVO.setClaimNbr(TTKCommon.checkNull(rs1.getString("CLAIM_NUMBER")));
					}//end of if(TTKCommon.checkNull(rs1.getString("DOCUMENT_GENERAL_TYPE_ID")).equals("DTS"))
					else{
						claimInwardDetailVO.setClaimNbr(TTKCommon.checkNull(rs1.getString("CLAIM_NUMBER")));
					}//end of else

					claimInwardDetailVO.setDocumentTypeDesc(TTKCommon.checkNull(rs1.getString("DOC_TYPE")));
					claimInwardDetailVO.setCourierNbr(TTKCommon.checkNull(rs1.getString("COURIER_ID")));
					claimInwardDetailVO.setCurrentClaimNbr(TTKCommon.checkNull(rs1.getString("CURRENT_CLAIM_NUMBER")));

					if(rs1.getString("PARENT_CLAIM_SEQ_ID") != null){
						claimInwardDetailVO.setParentClmSeqID(new Long(rs1.getLong("PARENT_CLAIM_SEQ_ID")));
					}//end of if(rs1.getString("PARENT_CLAIM_SEQ_ID") != null)

					if(TTKCommon.checkNull(rs1.getString("DOCUMENT_GENERAL_TYPE_ID")).equalsIgnoreCase("DTA")){
						alPrevClaimList = getPrevClaim(rs1.getLong("MEMBER_SEQ_ID"));
						claimInwardDetailVO.setPrevClaimList(alPrevClaimList);
					}//end of if(TTKCommon.checkNull(rs1.getString("DOCUMENT_GENERAL_TYPE_ID")).equalsIgnoreCase("DTA"))

					if(TTKCommon.checkNull(rs1.getString("DOCUMENT_GENERAL_TYPE_ID")).equalsIgnoreCase("DTS")){
						alShortfallList.add((Long)rs1.getLong("SHORTFALL_CLAIM_SEQ_ID"));
						alShortfallList.add(rs1.getString("SHORTFALL_ID"));
						alShortfallDetails = getInwardShortfallDetail(alShortfallList);
						claimInwardDetailVO.setShortfallVO(alShortfallDetails);
					}//end of if(TTKCommon.checkNull(rs1.getString("DOCUMENT_GENERAL_TYPE_ID")).equalsIgnoreCase("DTS"))

					claimInwardDetailVO.setClaimantVO(claimantVO);
				}//end of while(rs1.next())
			}//end of if(rs1 != null)

			if(rs2 != null){
				while(rs2.next()){
					documentChecklistVO = new DocumentChecklistVO();

					if(rs2.getString("DOCU_RCVD_SEQ_ID") != null){
						documentChecklistVO.setDocumentRcvdSeqID(new Long(rs2.getLong("DOCU_RCVD_SEQ_ID")));
					}//end of if(rs2.getString("DOCU_RCVD_SEQ_ID") != null)

					documentChecklistVO.setDocumentListType(TTKCommon.checkNull(rs2.getString("DOCU_LIST_TYPE_ID")));
					documentChecklistVO.setSheetsCount(TTKCommon.checkNull(rs2.getString("SHEETS_COUNT")));
					documentChecklistVO.setDocumentTypeID(TTKCommon.checkNull(rs2.getString("DOC_GENERAL_TYPE_ID")));
					documentChecklistVO.setReasonTypeID(TTKCommon.checkNull(rs2.getString("REASON_GENERAL_TYPE_ID")));
					documentChecklistVO.setRemarks(TTKCommon.checkNull(rs2.getString("REMARKS")));
					documentChecklistVO.setDocumentRcvdYN(TTKCommon.checkNull(rs2.getString("DOCUMENT_RCVD_YN")));
					documentChecklistVO.setDocumentName(TTKCommon.checkNull(rs2.getString("DOCU_NAME")));
					alDocumentList.add(documentChecklistVO);
				}//end of while(rs2.next())
				claimInwardDetailVO.setClaimDocumentVOList(alDocumentList);
			}//end of if(rs2 != null)
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
					if (rs2 != null) rs2.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimInwardDetail()",sqlExp);
					throw new TTKException(sqlExp, "claim");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the second resultset now.
				{
					try{
						if (rs1 != null) rs1.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimDAOImpl getClaimInwardDetail()",sqlExp);
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
							log.error("Error while closing the Statement in ClaimDAOImpl getClaimInwardDetail()",sqlExp);
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
								log.error("Error while closing the Connection in ClaimDAOImpl getClaimInwardDetail()",sqlExp);
								throw new TTKException(sqlExp, "claim");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of finally
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs2 = null;
				rs1 = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return claimInwardDetailVO;
	}//end of getClaimInwardDetail(long lngClaimInwardSeqID,String strClaimTypeID,long lngUserSeqID)

	/**
	 * This method returns the Arraylist of ClaimantVO's which contains Claim Details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of ClaimantVO object which contains Claim details
	 * @exception throws TTKException
	 */
	public ArrayList getClaimShortfallList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ShortfallVO shortfallVO=null;
		//ShortfallVO shortfallVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimShortfallList);
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
			cStmtObject.setLong(14,(Long)alSearchCriteria.get(9));
			cStmtObject.setString(15,(String)alSearchCriteria.get(10));
			cStmtObject.registerOutParameter(16,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(16);
			if(rs != null){
				while(rs.next()){
					shortfallVO = new ShortfallVO();
					if(rs.getString("CLAIM_SEQ_ID") != null){
						shortfallVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
					}//end of if(rs.getString("CLAIM_SEQ_ID") != null)
					if(rs.getString("SHORTFALL_SEQ_ID") != null){
						shortfallVO.setShortfallSeqID(new Long(rs.getLong("SHORTFALL_SEQ_ID")));
					}//end of if(rs.getString("SHORTFALL_SEQ_ID") != null)

					shortfallVO.setShortfallNo(TTKCommon.checkNull(rs.getString("SHORTFALL_ID")));

					shortfallVO.setInvoiceNo(TTKCommon.checkNull(rs.getString("INVOICE_NUMBER")));

					shortfallVO.setBatchNo(TTKCommon.checkNull(rs.getString("BATCH_NO")));

					shortfallVO.setClaimNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));

					shortfallVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));

					shortfallVO.setClaimType(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));

					shortfallVO.setPolicyNo(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));

					//shortfallVO.setAssignedTo(TTKCommon.checkNull(rs.getString("Ass")));

					shortfallVO.setReceivedDateAsString(TTKCommon.convertDateAsString("dd/MM/yyyy",rs.getDate("RECEIVED_DATE")));

					shortfallVO.setStatusDesc(TTKCommon.checkNull(rs.getString("STATUS")));
					
					
					shortfallVO.setsQatarId(TTKCommon.checkNull(rs.getString("QATAR_ID")));
					

					alResultList.add(shortfallVO);
				}//end of while(rs.next())
			}//end of if(rs != null)

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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimShortfallList()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimShortfallList()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimShortfallList()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return (ArrayList)alResultList;
	}//end of getClaimShortfallList(ArrayList alSearchCriteria)

	/**
	 * This method returns the Arraylist of ShortfallVO's which contains Claim Shortfall Details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of ShortfallVO object which contains Claim Shortfall details
	 * @exception throws TTKException
	 */
	public ArrayList getInwardShortfallDetail(ArrayList alSearchCriteria) throws TTKException {
		ArrayList<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		//ShortfallVO shortfallVO = null;
		CacheObject cacheObject = null;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strInwardShortfallDetails);

			if(alSearchCriteria.get(0) != null){
				cStmtObject.setLong(1,(Long)alSearchCriteria.get(0));
			}//end of if(alSearchCriteria.get(0) != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else
			//cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			if(rs != null){
				while(rs.next()){
					cacheObject = new CacheObject();

					cacheObject.setCacheId(TTKCommon.checkNull(rs.getString("SHORTFALL_SEQ_ID")));
					cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("SHORTFALL_ID")));

					alResultList.add(cacheObject);
				}//end of while(rs.next())
			}//end of if(rs != null)
		}//end of try
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getInwardShortfallDetail()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getInwardShortfallDetail()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getInwardShortfallDetail()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return alResultList;
	}//end of getInwardShortfallDetail(ArrayList alSearchCriteria)

	/**
	 * This method returns the Arraylist of DocumentChecklistVO's  which contains Claim Document Details
	 * @param strClaimTypeID contains Claim Type ID
	 * @return ArrayList of DocumentChecklistVO object which contains Claim Inward Document details
	 * @exception throws TTKException
	 */
	public ArrayList getClaimDocumentList(String strClaimTypeID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		Collection<Object> alResultList = new ArrayList<Object>();
		ResultSet rs = null;
		DocumentChecklistVO documentChecklistVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimDocumentList);
			cStmtObject.setString(1,strClaimTypeID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					documentChecklistVO = new DocumentChecklistVO();
					documentChecklistVO.setDocumentListType(TTKCommon.checkNull(rs.getString("DOCU_LIST_TYPE_ID")));
					documentChecklistVO.setDocumentName(TTKCommon.checkNull(rs.getString("DOCU_NAME")));
					alResultList.add(documentChecklistVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimDocumentList()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimDocumentList()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimDocumentList()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimDocumentList(String strClaimTypeID)

	/**
	 * This method saves the Claim Inward information
	 * @param claimInwardDetailVO the object which contains the Claim Inward Details which has to be saved
	 * @return long the value contains Claim Inward Seq ID
	 * @exception throws TTKException
	 */
	public long saveClaimInwardDetail(ClaimInwardDetailVO claimInwardDetailVO) throws TTKException {
		long lngClaimInwardSeqID = 0;
		ClaimantVO claimantVO = null;
		StringBuffer sbfSQL = null;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		Statement stmt = null;
		DocumentChecklistVO documentChecklistVO = null;
		ArrayList alDocumentChecklist  = new ArrayList();
		try{
			conn = ResourceManager.getConnection();
			conn.setAutoCommit(false);
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveClaimInwardDetail);
			stmt = (java.sql.Statement)conn.createStatement();
			claimantVO = claimInwardDetailVO.getClaimantVO();
			if(claimInwardDetailVO.getClaimDocumentVOList() != null){
				alDocumentChecklist = claimInwardDetailVO.getClaimDocumentVOList();
			}//end of if(claimInwardDetailVO.getClaimDocumentVOList() != null)

			if(claimInwardDetailVO.getInwardSeqID() != null){
				cStmtObject.setLong(CLAIMS_INWARD_SEQ_ID,claimInwardDetailVO.getInwardSeqID());
			}//end of if(claimInwardDetailVO.getInwardSeqID() != null)
			else{
				cStmtObject.setLong(CLAIMS_INWARD_SEQ_ID,0);
			}//end of else

			if(claimInwardDetailVO.getOfficeSeqID() != null){
				cStmtObject.setLong(TPA_OFFICE_SEQ_ID,claimInwardDetailVO.getOfficeSeqID());
			}//end of if(claimInwardDetailVO.getOfficeSeqID() != null)
			else{
				cStmtObject.setString(TPA_OFFICE_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(BARCODE_NO,claimInwardDetailVO.getBarCodeNbr());
			cStmtObject.setString(DOCUMENT_GENERAL_TYPE_ID,claimInwardDetailVO.getDocumentTypeID());

			if(claimInwardDetailVO.getReceivedDate() != null){
				cStmtObject.setTimestamp(RCVD_DATE,new Timestamp(claimInwardDetailVO.getReceivedDate().getTime()));//RCVD_DATE
			}//end of if(claimInwardDetailVO.getReceivedDate() != null)
			else{
				cStmtObject.setTimestamp(RCVD_DATE, null);//RCVD_DATE
			}//end of else
			cStmtObject.setString(SOURCE_GENERAL_TYPE_ID,claimInwardDetailVO.getSourceTypeID());
			cStmtObject.setString(CLAIM_GENERAL_TYPE_ID,claimInwardDetailVO.getClaimTypeID());

			if(claimInwardDetailVO.getRequestedAmt() != null){
				cStmtObject.setBigDecimal(REQUESTED_AMOUNT,claimInwardDetailVO.getRequestedAmt());
			}//end of if(claimInwardDetailVO.getRequestedAmt() != null)
			else{
				cStmtObject.setString(REQUESTED_AMOUNT,null);
			}//end of else

			if(claimantVO.getMemberSeqID() != null){
				cStmtObject.setLong(MEMBER_SEQ_ID,claimantVO.getMemberSeqID());
			}//end of if(claimantVO.getMemberSeqID() != null)
			else{
				cStmtObject.setString(MEMBER_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(TPA_ENROLLMENT_ID,claimantVO.getEnrollmentID());
			cStmtObject.setString(CLAIMANT_NAME,claimantVO.getName());

			if(claimInwardDetailVO.getClaimantVO().getPolicySeqID() != null){
				cStmtObject.setLong(POLICY_SEQ_ID,claimantVO.getPolicySeqID());
			}//end of if(claimInwardDetailVO.getClaimantVO().getPolicySeqID() != null)
			else{
				cStmtObject.setString(POLICY_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(POLICY_NUMBER,claimantVO.getPolicyNbr());

			if(claimantVO.getInsSeqID() != null){
				cStmtObject.setLong(INWARD_INS_SEQ_ID,claimantVO.getInsSeqID());
			}//end of if(claimantVO.getInsSeqID() != null)
			else{
				cStmtObject.setString(INWARD_INS_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(CORPORATE_NAME,claimantVO.getGroupName());
			cStmtObject.setString(POLICY_HOLDER_NAME,claimantVO.getPolicyHolderName());
			cStmtObject.setString(EMPLOYEE_NO,claimantVO.getEmployeeNbr());
			cStmtObject.setString(EMPLOYEE_NAME,claimantVO.getEmployeeName());
			cStmtObject.setString(INWARD_STATUS_GENERAL_TYPE_ID,claimInwardDetailVO.getStatusTypeID());
			cStmtObject.setString(REMARKS,claimInwardDetailVO.getRemarks());

			if(claimInwardDetailVO.getCourierSeqID() != null){
				cStmtObject.setLong(COURIER_SEQ_ID,claimInwardDetailVO.getCourierSeqID());
			}//end of if(claimInwardDetailVO.getCourierSeqID() != null)
			else{
				cStmtObject.setString(COURIER_SEQ_ID,null);
			}//end of else

			if(claimInwardDetailVO.getParentClmSeqID() != null){
				cStmtObject.setLong(PARENT_CLAIM_SEQ_ID,claimInwardDetailVO.getParentClmSeqID());
			}//end of if(claimInwardDetailVO.getParentClmSeqID() != null)
			else{
				cStmtObject.setString(PARENT_CLAIM_SEQ_ID,null);
			}//end of else

			//cStmtObject.setString(CLAIM_NUMBER,claimInwardDetailVO.getClaimNbr());
			if(claimInwardDetailVO.getDocumentTypeID().equals("DTS")){
				cStmtObject.setString(CLAIM_NUMBER,claimantVO.getClaimNbr());
			}//end of if(claimInwardDetailVO.getDocumentTypeID().equals("DTS"))
			else{
				cStmtObject.setString(CLAIM_NUMBER,claimInwardDetailVO.getClaimNbr());
			}//end of else

			cStmtObject.setString(SHORTFALL_ID,claimInwardDetailVO.getShortfallID());

			if(claimInwardDetailVO.getClaimSeqID() != null){
				cStmtObject.setLong(CLAIM_SEQ_ID,claimInwardDetailVO.getClaimSeqID());
			}//end of if(claimInwardDetailVO.getClaimSeqID() != null)
			else{
				cStmtObject.setString(CLAIM_SEQ_ID,null);
			}//end of else

			if(claimInwardDetailVO.getClmEnrollDtlSeqID() != null){
				cStmtObject.setLong(CLM_ENROLL_DETAIL_SEQ_ID,claimInwardDetailVO.getClmEnrollDtlSeqID());
			}//end of if(claimInwardDetailVO.getClmEnrollDtlSeqID() != null)
			else{
				cStmtObject.setString(CLM_ENROLL_DETAIL_SEQ_ID,null);
			}//end of else

			cStmtObject.setString(EMAIL_ID,claimantVO.getEmailID());
			cStmtObject.setString(NOTIFICATION_PHONE_NUMBER,claimantVO.getNotifyPhoneNbr());
			cStmtObject.setString(INS_SCHEME,claimantVO.getInsScheme());
			cStmtObject.setString(CERTIFICATE_N0,claimantVO.getCertificateNo());
			cStmtObject.setString(INS_CUSTOMER_CODE,claimantVO.getInsCustCode());
			cStmtObject.setLong(USER_SEQ_ID,claimInwardDetailVO.getUpdatedBy());
			cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);
			cStmtObject.registerOutParameter(CLAIMS_INWARD_SEQ_ID,Types.BIGINT);
			cStmtObject.execute();
			lngClaimInwardSeqID = cStmtObject.getLong(CLAIMS_INWARD_SEQ_ID);

			if(alDocumentChecklist != null){
				for(int i=0;i<alDocumentChecklist.size();i++){
					sbfSQL = new StringBuffer();
					documentChecklistVO =(DocumentChecklistVO)alDocumentChecklist.get(i);

					if(documentChecklistVO.getDocumentRcvdSeqID() == null){
						sbfSQL = sbfSQL.append("0,");
					}//end of if(documentChecklistVO.getDocumentRcvdSeqID()== null)
					else{
						sbfSQL = sbfSQL.append("'").append(documentChecklistVO.getDocumentRcvdSeqID()).append("',");
					}//end of else

					sbfSQL = sbfSQL.append("'").append(documentChecklistVO.getDocumentRcvdYN()).append("',");
					sbfSQL = sbfSQL.append("'").append(documentChecklistVO.getDocumentListType()).append("',");

					if(documentChecklistVO.getSheetsCount()== null){
						sbfSQL = sbfSQL.append("null,");
					}//end of if(documentChecklistVO.getSheetsCount()==null)
					else{
						sbfSQL = sbfSQL.append("'").append(documentChecklistVO.getSheetsCount()).append("',");
					}//end of else

					sbfSQL = sbfSQL.append("'").append(documentChecklistVO.getDocumentTypeID()).append("',");
					sbfSQL = sbfSQL.append("'").append(documentChecklistVO.getReasonTypeID()).append("',");
					sbfSQL = sbfSQL.append("null,");
					sbfSQL = sbfSQL.append("").append(lngClaimInwardSeqID).append(",");
					sbfSQL = sbfSQL.append("'").append(documentChecklistVO.getRemarks()).append("',");
					sbfSQL = sbfSQL.append("'").append(claimInwardDetailVO.getUpdatedBy()).append("')}");
					stmt.addBatch(strSaveDocumentChecklist+sbfSQL.toString());
				}//end of for(int i=0;i<alDocumentChecklist.size();i++)
			}//end of if(alDocumentChecklist != null)
			stmt.executeBatch();
			conn.commit();
		}//end of try
		catch (SQLException sqlExp)
		{
			try {
				conn.rollback();
				throw new TTKException(sqlExp, "claim");
			} //end of try
			catch (SQLException sExp) {
				throw new TTKException(sExp, "claim");
			}//end of catch (SQLException sExp)
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			try {
				conn.rollback();
				throw new TTKException(exp, "claim");
			} //end of try
			catch (SQLException sqlExp) {
				throw new TTKException(sqlExp, "claim");
			}//end of catch (SQLException sqlExp)
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the Statement
			{
				try{
					if (stmt != null) stmt.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveClaimInwardDetail()",sqlExp);
					throw new TTKException(sqlExp, "claim");
				}//end of catch (SQLException sqlExp)
				finally{ // Even if statement is not closed, control reaches here. Try closing the Callabale Statement now.
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ClaimDAOImpl saveClaimInwardDetail()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl saveClaimInwardDetail()",sqlExp);
							throw new TTKException(sqlExp, "claim");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				stmt = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return lngClaimInwardSeqID;
	}//end of saveClaimInwardDetail(ClaimInwardDetailVO claimInwardDetailVO)

	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Claim Details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Claim details
	 * @exception throws TTKException
	 */
	public PreAuthDetailVO getMemberDetails(String memberID) throws TTKException {

		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthDetailVO preAuthDetailVO = null;

		try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetMemberDetails);
			cStmtObject.setString(1,memberID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
           rs=(ResultSet)cStmtObject.getObject(2);

			if(rs != null){
				while(rs.next()){

					preAuthDetailVO = new PreAuthDetailVO();
					    preAuthDetailVO.setMemberSeqID(rs.getLong("MEMBER_SEQ_ID"));
					    preAuthDetailVO.setClaimantName(rs.getString("MEM_NAME"));
					    preAuthDetailVO.setMemberAge(rs.getInt("MEM_AGE"));
					    preAuthDetailVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("EMIRATE_ID")));
					    preAuthDetailVO.setPayerId(TTKCommon.checkNull(rs.getString("PAYER_ID")));
					    preAuthDetailVO.setInsSeqId(rs.getLong("INS_SEQ_ID"));
					    preAuthDetailVO.setPolicySeqId(rs.getLong("POLICY_SEQ_ID"));
					    preAuthDetailVO.setPayerName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					    preAuthDetailVO.setPatientGender(TTKCommon.checkNull(rs.getString("GENDER")));
					    preAuthDetailVO.setPolicyNumber(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
					    preAuthDetailVO.setCorporateName(TTKCommon.checkNull(rs.getString("CORPORATE_NAME")));
					    preAuthDetailVO.setPolicyStartDate(TTKCommon.convertDateAsString("dd/MM/yyyy",rs.getDate("POLICY_START_DATE")));
                        preAuthDetailVO.setPolicyEndDate(TTKCommon.convertDateAsString("dd/MM/yyyy",rs.getDate("POLICY_END_DATE")));
					    preAuthDetailVO.setNationality(TTKCommon.checkNull(rs.getString("NATIONALITY")));
					    preAuthDetailVO.setSumInsured(rs.getBigDecimal("SUM_INSURED"));
					    preAuthDetailVO.setAvailableSumInsured(rs.getBigDecimal("AVA_SUM_INSURED"));
					    preAuthDetailVO.setPolicyType(rs.getString("ENROL_TYPE_ID"));
					    preAuthDetailVO.setEmirateId(rs.getString("EMIRATE_ID"));

					    preAuthDetailVO.setProductName(rs.getString("PRODUCT_NAME"));
                        preAuthDetailVO.setPayerAuthority(rs.getString("PAYER_AUTHORITY"));
					    preAuthDetailVO.setVipYorN(rs.getString("VIP_YN"));

                        preAuthDetailVO.setClmMemInceptionDate(rs.getString("clm_mem_insp_date"));



				}//end of while(rs.next())
			}//end of if(rs != null)
            return preAuthDetailVO;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getMemberDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getMemberDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getMemberDetails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getMemberDetails(ArrayList alSearchCriteria)

	/**
	 * This method returns the Arraylist of PreAuthVO's  which contains Claim Details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthVO object which contains Claim details
	 * @exception throws TTKException
	 */
	public ArrayList getClaimList(ArrayList alSearchCriteria) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Collection<Object> alResultList = new ArrayList<Object>();
		PreAuthVO preauthVO = null;
		//String strGroupID="";
		//String strPolicyGrpID ="";
		//ArrayList alUserGroup = new ArrayList();
		// strSuppressLink[]={"0","7","8"};
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//sInvoiceNO
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//sBatchNO
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//sPolicyNumber
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//sClaimNO
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));//sClaimType
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));//sRecievedDate
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));//sSettlementNO
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));//sEnrollmentId
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));//sClaimantName
			cStmtObject.setString(10,(String)alSearchCriteria.get(9));//sStatus
			cStmtObject.setString(11,(String)alSearchCriteria.get(10));//sProviderName
			cStmtObject.setString(12,"");//v_ins_seq_id
			cStmtObject.setString(13,(String)alSearchCriteria.get(12));//sModeOfClaim
			cStmtObject.setString(14,(String)alSearchCriteria.get(13));	//sGlobalNetMemID	
            cStmtObject.setString(15,(String)alSearchCriteria.get(14));//sAssignedTo
			cStmtObject.setString(16,(String)alSearchCriteria.get(15));//sSpecifyName
			cStmtObject.setLong(17,(Long)alSearchCriteria.get(16));//Added By
			cStmtObject.setString(18,(String)alSearchCriteria.get(17));//sProcessType
			cStmtObject.setString(19,(String)alSearchCriteria.get(18));//clmShortFallStatus
			cStmtObject.setString(20,(String)alSearchCriteria.get(19));//eventReferenceNo
			cStmtObject.setString(21,(String)alSearchCriteria.get(20));//sCommonFileNo
			cStmtObject.setString(22,(String)alSearchCriteria.get(21));//internalRemarkStatus
			cStmtObject.setString(23,(String)alSearchCriteria.get(22));//sPartnerName
			cStmtObject.setString(24,(String)alSearchCriteria.get(23));//sBenefitType
			cStmtObject.setString(25,(String)alSearchCriteria.get(24));//sAmountRange
			cStmtObject.setString(26,(String)alSearchCriteria.get(25));//sAmountRangeValue
			cStmtObject.setString(27,(String)alSearchCriteria.get(26));//sLinkedPreapprovalNo
			cStmtObject.setString(28,(String)alSearchCriteria.get(27));//sQatarId
			String sStatus	= (String)alSearchCriteria.get(9);
			if("INP".equals(sStatus))					// sStatus
				cStmtObject.setString(29,(String)alSearchCriteria.get(28));	// in-progress-Status
			else
				cStmtObject.setString(29,null);
			cStmtObject.setString(30,(String)alSearchCriteria.get(29));//riskLevel
			cStmtObject.setString(31,(String)alSearchCriteria.get(30));//cfdInvestigationStatus
			cStmtObject.setString(32,(String)alSearchCriteria.get(31));//priorityClaims

			cStmtObject.setString(33,(String)alSearchCriteria.get(32));//rangeFrom
			cStmtObject.setString(34,(String)alSearchCriteria.get(33));//rangeTo
			cStmtObject.setString(35,(String)alSearchCriteria.get(36));//v_sort_var
			cStmtObject.setString(36,(String)alSearchCriteria.get(37));//v_sort_order
			cStmtObject.setString(37,(String)alSearchCriteria.get(38));//v_start_num
			cStmtObject.setString(38,(String)alSearchCriteria.get(39));//v_end_num
			cStmtObject.registerOutParameter(39,OracleTypes.CURSOR);
			cStmtObject.setString(40,(String)alSearchCriteria.get(34));
			cStmtObject.setString(41,(String)alSearchCriteria.get(35));
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(39);

			if(rs != null){
				while(rs.next()){
					preauthVO = new PreAuthVO();
						preauthVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
					    preauthVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					    preauthVO.setStrAlternateID(TTKCommon.checkNull(rs.getString("TPA_ALTERNATE_ID")));  // database column name
                        preauthVO.setClaimantName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
					    preauthVO.setClaimNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					    preauthVO.setInvoiceNo(TTKCommon.checkNull(rs.getString("INVOICE_NUMBER")));
					    preauthVO.setBatchNo(TTKCommon.checkNull(rs.getString("BATCH_NO")));
					    preauthVO.setClaimType(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
					    preauthVO.setHospitalName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
					    preauthVO.setAssignedTo(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
					    preauthVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					    preauthVO.setStatus(rs.getString("description"));
					if(rs.getString("RECEIVED_DATE") != null){
						preauthVO.setReceivedDateAsString(new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("RECEIVED_DATE").getTime())));
					}//end of if(rs.getString("DATE_OF_ADMISSION") != null)
                    preauthVO.setStatus(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    preauthVO.setProcessType(TTKCommon.checkNull(rs.getString("PROCESS_TYPE")));
                    preauthVO.setShortfallYN(TTKCommon.checkNull(rs.getString("SHRTFALL_YN")));
                    
                  
					if(rs.getString("SHRTFALL_YN").equals("Y")){
						preauthVO.setShortfallImageName("shortfall");
						preauthVO.setShortfallImageTitle("Shortfall Received "+TTKCommon.checkNull(rs.getString("srtfll_updated_date")));
					}//end of if(rs.getString("SHRTFALL_YN").equals("Y"))
					
					  if(rs.getString("fraud_yn").equalsIgnoreCase("Y") && rs.getString("suspect_veri_check").equalsIgnoreCase("N") ){

	                    	preauthVO.setFarudimg("suspectedcheckedfraud");
	                    	preauthVO.setFraudImgTitle(TTKCommon.checkNull(rs.getString("status_desc")));
	                    }else if(rs.getString("suspect_veri_check").equalsIgnoreCase("Y")){
	                    	preauthVO.setFarudimg("verifiedclaimsearchAndhistoryimg");
	                    	preauthVO.setFraudImgTitle(TTKCommon.checkNull(rs.getString("status_desc")));
	                    }
                    preauthVO.setEventReferenceNo(TTKCommon.checkNull(rs.getString("event_no")));
                    preauthVO.setConvertedAmount(rs.getBigDecimal("CONVERTED_AMOUNT"));
                    
                    preauthVO.setsQatarId(TTKCommon.checkNull(rs.getString("QATAR_ID")));
                    if("INP-ENH".equals(rs.getString("IN_PROGESS_STATUS")))
                	{	
                		preauthVO.setStatus("In-Progress-Resubmission"); 
                		preauthVO.setInProImageName("InprogressAppeal");
                		preauthVO.setInProImageTitle("InProgress Resubmission");
                	}
                    else if("INP-RES".equals(rs.getString("IN_PROGESS_STATUS")))
                	{
                		preauthVO.setStatus("In-Progress-Shortfall Responded");
                		preauthVO.setInProImageName("AddIcon");
                		preauthVO.setInProImageTitle("InProgress Shortfall Responded");
                	}
                    else
                    {
                    	preauthVO.setStatus(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    	preauthVO.setInProImageName("Blank");
                		preauthVO.setInProImageTitle("");
                    }
                    preauthVO.setPriorityCorporate(TTKCommon.checkNull(rs.getString("PRIORITY_CORPORATE")));
                    if(preauthVO.getPriorityCorporate()!= null && preauthVO.getPriorityCorporate().equals("Y")){
                    preauthVO.setPriorityImageName(TTKCommon.checkNull(rs.getString("GROUPIMAGENAME")));
                    preauthVO.setPriorityImageTitle(TTKCommon.checkNull(rs.getString("GROUPIMAGETITLE")));
                    }
                    			
                    preauthVO.setFastTrackFlag(rs.getString("FAST_TRACK"));
                    preauthVO.setFastTrackMsg(rs.getString("FAST_TRACK_MESSEGE"));
	                    if("Y".equals(preauthVO.getFastTrackFlag()))
	        			{
	                    	preauthVO.setFastTrackFlagImageName("fastTrackImg");
	                    	preauthVO.setFastTrackFlagImageTitle(""+rs.getString("FAST_TRACK_MESSEGE"));
	                    }				
	                    preauthVO.setClaimAge(TTKCommon.checkNull(rs.getString("CLAIM_AGE")));
	                    preauthVO.setProviderAgreedDays(new Long(rs.getLong("PAYMENT_DUR_AGR")));
	                    preauthVO.setApproveAmount(TTKCommon.checkNull(rs.getString("approved_amount")));
	                    
	                    preauthVO.setResubmissionFlag(TTKCommon.checkNull(rs.getString("RESUBMISSION_FLAG")));
	                    if(preauthVO.getResubmissionFlag()!= null && preauthVO.getResubmissionFlag().equals("Y")){
	                        preauthVO.setResubmissionImageName("ResubmissionIcon1");
	                        preauthVO.setResubmissionImageTitle("Claim Resubmission");
	                        }
	                    
					alResultList.add(preauthVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimList(ArrayList alSearchCriteria)
	/**
	 * This method returns the PreAuthDetailVO, which contains all the Claim details
	 * @param alClaimList contains Claim seq id,Member Seq ID to get the Claim Details
	 * @return PreAuthDetailVO object which contains all the Claim details
	 * @exception throws TTKException
	 */
	public PreAuthDetailVO getClaimDetail(ArrayList alClaimList) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthDetailVO preauthDetailVO = null;
		PreAuthHospitalVO preauthHospitalVO = null;
		ClaimantVO claimantVO = null;
		ClaimDetailVO claimDetailVO = null;
		ArrayList alPrevClaimList = new ArrayList();
		HashMap hmPrevHospDetails = new HashMap();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimDetail);


			if(alClaimList.get(0) != null){

				cStmtObject.setLong(1,(Long)alClaimList.get(0));
			}//end of if(alClaimList.get(0) != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else

			if(alClaimList.get(1) != null){
				cStmtObject.setLong(2,(Long)alClaimList.get(1));
			}//end of if(alClaimList.get(1) != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			if(alClaimList.get(2) != null){
				cStmtObject.setLong(3,(Long)alClaimList.get(2));
			}//end of if(alClaimList.get(2) != null)
			else{
				cStmtObject.setString(3,null);
			}//end of else
			cStmtObject.setLong(4,(Long)alClaimList.get(3));
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(5);
			if(rs != null){
				while(rs.next()){
					preauthDetailVO = new PreAuthDetailVO();
					claimantVO = new ClaimantVO();
					preauthHospitalVO = new PreAuthHospitalVO();
					claimDetailVO = new ClaimDetailVO();

					if(rs.getString("CLAIM_SEQ_ID") != null){
						preauthDetailVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
					}//end of if(rs.getString("CLAIM_SEQ_ID") != null)

					if(rs.getString("PARENT_CLAIM_SEQ_ID") != null){
						preauthDetailVO.setClmParentSeqID(new Long(rs.getLong("PARENT_CLAIM_SEQ_ID")));
					}//end of if(rs.getString("PARENT_CLAIM_SEQ_ID") != null)

					preauthDetailVO.setPrevClaimNbr(TTKCommon.checkNull(rs.getString("PREV_CLAIM_NUMBER")));

					if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null){
						preauthDetailVO.setClmEnrollDtlSeqID(new Long(rs.getLong("CLM_ENROLL_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("CLM_ENROLL_DETAIL_SEQ_ID") != null)

					if(rs.getString("CLAIMS_INWARD_SEQ_ID") != null){
						preauthDetailVO.setInwardSeqID(new Long(rs.getLong("CLAIMS_INWARD_SEQ_ID")));
					}//end of if(rs.getString("CLAIMS_INWARD_SEQ_ID") != null)

					if(rs.getString("MEMBER_SEQ_ID") != null){
						claimantVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs.getString("MEMBER_SEQ_ID") != null)

					claimantVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));

					claimantVO.setName(TTKCommon.checkNull(rs.getString("CLAIMANT_NAME")));
					//changes on dec 9th koc1136
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

					//changes on dec 9th koc1136




					claimantVO.setClaimantNameDisc(TTKCommon.checkNull(rs.getString("DIS_NAME_YN")));
					claimantVO.setGenderTypeID(TTKCommon.checkNull(rs.getString("GENDER_GENERAL_TYPE_ID")));
					claimantVO.setGenderDiscrepancy(TTKCommon.checkNull(rs.getString("DIS_GEND_YN")));

					if(rs.getString("MEM_AGE") != null){
						claimantVO.setAge(new Integer(rs.getString("MEM_AGE")));
					}//end of if(rs.getString("MEM_AGE") != null)

					preauthDetailVO.setSeniorCitizenYN(TTKCommon.checkNull(rs.getString("senior_citizen_yn")));//koc for griavance

					preauthDetailVO.setBufferNoteyn(TTKCommon.checkNull(rs.getString("buff_note_yn")));//koc for hyundai buffer
					preauthDetailVO.setBufferRestrictyn(TTKCommon.checkNull(rs.getString("buffer_restrict_yn")));//koc for hyundai buffer


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

					claimantVO.setEmployeeNbr(TTKCommon.checkNull(rs.getString("EMPLOYEE_NO")));
					claimantVO.setEmployeeName(TTKCommon.checkNull(rs.getString("EMPLOYEE_NAME")));
					claimantVO.setRelationTypeID(TTKCommon.checkNull(rs.getString("RELSHIP_TYPE_ID")));
					claimantVO.setClaimantPhoneNbr(TTKCommon.checkNull(rs.getString("CLAIMANT_PHONE_NUMBER")));
					claimantVO.setEmailID(TTKCommon.checkNull(rs.getString("EMAIL_ID")));
					claimantVO.setNotifyPhoneNbr(TTKCommon.checkNull(rs.getString("MOBILE_NO")));

					preauthDetailVO.setPreAuthNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					claimDetailVO.setClaimFileNbr(TTKCommon.checkNull(rs.getString("CLAIM_FILE_NUMBER")));
					claimDetailVO.setRequestTypeID(TTKCommon.checkNull(rs.getString("REQUEST_GENERAL_TYPE_ID")));
					claimDetailVO.setRequestTypeDesc(TTKCommon.checkNull(rs.getString("REQUEST_TYPE")));
					claimDetailVO.setReopenTypeID(TTKCommon.checkNull(rs.getString("RE_OPEN_TYPE")));
					claimDetailVO.setReopenTypeDesc(TTKCommon.checkNull(rs.getString("RE_OPEN_TYPE_DESCRIPTION")));
					claimDetailVO.setClaimTypeID(TTKCommon.checkNull(rs.getString("CLAIM_GENERAL_TYPE_ID")));
					claimDetailVO.setClaimTypeDesc(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
					claimDetailVO.setClaimSubTypeID(TTKCommon.checkNull(rs.getString("CLAIM_SUB_GENERAL_TYPE_ID")));
					//added as per KOC 1285 Change Request
					claimDetailVO.setDomicilaryReason(TTKCommon.checkNull(rs.getString("DOM_REASON_GEN_TYPE_ID")));
					claimDetailVO.setDoctorCertificateYN(TTKCommon.checkNull(rs.getString("DOC_CERT_DOM_YN")));
					//added as per KOC 1285 Change Request
					if(rs.getString("REQUESTED_AMOUNT") != null){
						preauthDetailVO.setClaimRequestAmount(new BigDecimal(rs.getString("REQUESTED_AMOUNT")));
					}//end of if(rs.getString("REQUESTED_AMOUNT") != null)

					if(rs.getString("INTIMATION_DATE") != null){
						claimDetailVO.setIntimationDate(new Date(rs.getTimestamp("INTIMATION_DATE").getTime()));
					}//end of if(rs.getString("INTIMATION_DATE") != null)

					claimDetailVO.setModeTypeID(TTKCommon.checkNull(rs.getString("MODE_GENERAL_TYPE_ID")));
					claimDetailVO.setModeTypeDesc(TTKCommon.checkNull(rs.getString("MODE_TYPE")));
					claimDetailVO.setPaymentType(TTKCommon.checkNull(rs.getString("pay_to_general_type_id")));//OPD_4_hosptial

					if(rs.getString("RCVD_DATE") != null){
						claimDetailVO.setClaimReceivedDate(new Date(rs.getTimestamp("RCVD_DATE").getTime()));
					}//end of if(rs.getString("RCVD_DATE") != null)
					claimDetailVO.setDoctorRegnNbr(TTKCommon.checkNull(rs.getString("DOCTOR_REGISTRATION_NO")));
					preauthDetailVO.setDoctorName(TTKCommon.checkNull(rs.getString("TREATING_DR_NAME")));
					claimDetailVO.setInPatientNbr(TTKCommon.checkNull(rs.getString("IN_PATIENT_NO")));
					preauthDetailVO.setOfficeName(TTKCommon.checkNull(rs.getString("TTK_BRANCH")));
					claimDetailVO.setSourceDesc(TTKCommon.checkNull(rs.getString("SOURCE_TYPE")));
					claimDetailVO.setClaimRemarks(TTKCommon.checkNull(rs.getString("CLAIMS_REMARKS")));

					if(rs.getString("ASSIGN_USERS_SEQ_ID") != null){
						preauthDetailVO.setAssignUserSeqID(new Long(rs.getLong("ASSIGN_USERS_SEQ_ID")));
					}//end of if(rs.getString("ASSIGN_USERS_SEQ_ID") != null)

					preauthDetailVO.setAssignedTo(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
					preauthDetailVO.setProcessingBranch(TTKCommon.checkNull(rs.getString("PROCESSING_BRANCH")));
					preauthDetailVO.setAuthNbr(TTKCommon.checkNull(rs.getString("AUTH_NUMBER")));
					preauthDetailVO.setPreAuthTypeID(TTKCommon.checkNull(rs.getString("PAT_GENERAL_TYPE_ID")));
					preauthDetailVO.setPreauthTypeDesc(TTKCommon.checkNull(rs.getString("PREAUTH_TYPE")));

					if(rs.getString("PAT_ENROLL_DETAIL_SEQ_ID") != null){
						preauthDetailVO.setEnrollDtlSeqID(new Long(rs.getLong("PAT_ENROLL_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("PAT_ENROLL_DETAIL_SEQ_ID") != null)

					if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null){
						preauthDetailVO.setPreAuthSeqID(new Long(rs.getLong("PAT_GEN_DETAIL_SEQ_ID")));
					}//end of if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null)

					if(rs.getString("PAT_RECEIVED_DATE") != null){
						preauthDetailVO.setReceivedDate(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime()));
						preauthDetailVO.setReceivedTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ")[1]:"");
						preauthDetailVO.setReceivedDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("PAT_RECEIVED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("PAT_RECEIVED_DATE") != null)

					if(rs.getString("TOTAL_APP_AMOUNT") != null){
						preauthDetailVO.setApprovedAmt(new BigDecimal(rs.getString("TOTAL_APP_AMOUNT")));
					}//end of if(rs.getString("TOTAL_APP_AMOUNT") != null)

					if(rs.getString("DATE_OF_ADMISSION") != null){
						preauthDetailVO.setClmAdmissionDate(new Date(rs.getTimestamp("DATE_OF_ADMISSION").getTime()));
						preauthDetailVO.setClmAdmissionTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ")[1]:"");
						preauthDetailVO.setAdmissionDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("DATE_OF_ADMISSION") != null)

					if(rs.getString("DATE_OF_DISCHARGE") != null){
						//preauthDetailVO.setDischargeDate(new Date(rs.getTimestamp("DATE_OF_DISCHARGE").getTime()));
						preauthDetailVO.setDischargeTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ")[1]:"");
						preauthDetailVO.setDischargeDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("DATE_OF_DISCHARGE") != null)

					preauthDetailVO.setEnrolChangeMsg(TTKCommon.checkNull(rs.getString("ENROLLMENT_CHANGE_MSG")));

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

					claimantVO.setPolicySubTypeID(TTKCommon.checkNull(rs.getString("POLICY_SUB_GENERAL_TYPE_ID")));
					claimantVO.setPolicyHolderName(TTKCommon.checkNull(rs.getString("POLICY_HOLDER_NAME")));
					claimantVO.setPolicyHolderNameDisc(TTKCommon.checkNull(rs.getString("DIS_POLICY_HOLDER_YN")));
					claimantVO.setTermStatusID(TTKCommon.checkNull(rs.getString("INS_STATUS_GENERAL_TYPE_ID")));
					claimantVO.setPhone(TTKCommon.checkNull(rs.getString("PHONE_1")));
					claimantVO.setInsScheme(TTKCommon.checkNull(rs.getString("INS_SCHEME")));
					claimantVO.setCertificateNo(TTKCommon.checkNull(rs.getString("CERTIFICATE_NO")));
					claimantVO.setInsCustCode(TTKCommon.checkNull(rs.getString("INS_CUSTOMER_CODE")));
					claimantVO.setInsuranceRefNo((TTKCommon.checkNull(rs.getString("INSUR_REF_NUMBER"))));

					if(rs.getString("GROUP_REG_SEQ_ID") != null){
						claimantVO.setGroupRegnSeqID(new Long(rs.getLong("GROUP_REG_SEQ_ID")));
					}//end of if(rs.getString("") != null)

					claimantVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					claimantVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));

					if(rs.getString("INS_SEQ_ID") != null){
						claimantVO.setInsSeqID(new Long(rs.getLong("INS_SEQ_ID")));
					}//end of if(rs.getString("") != null)

					claimantVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					claimantVO.setCompanyCode(TTKCommon.checkNull(rs.getString("COMPANY_CODE")));

					if(rs.getString("CLM_HOSP_ASSOC_SEQ_ID") != null){
						preauthDetailVO.setHospAssocSeqID(new Long(rs.getLong("CLM_HOSP_ASSOC_SEQ_ID")));
					}//end of if(rs.getString("CLM_HOSP_ASSOC_SEQ_ID") != null)

					if(rs.getString("HOSP_SEQ_ID") != null){
						preauthHospitalVO.setHospSeqId(new Long(rs.getLong("HOSP_SEQ_ID")));
					}//end of if(rs.getString("HOSP_SEQ_ID") != null)

					preauthHospitalVO.setHospitalName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
					preauthHospitalVO.setEmplNumber(TTKCommon.checkNull(rs.getString("EMPANEL_NUMBER")));
					preauthHospitalVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS_1")));
					preauthHospitalVO.setAddress2(TTKCommon.checkNull(rs.getString("ADDRESS_2")));
					preauthHospitalVO.setAddress3(TTKCommon.checkNull(rs.getString("ADDRESS_3")));
					preauthHospitalVO.setCityDesc(TTKCommon.checkNull(rs.getString("CITY_NAME")));
					preauthHospitalVO.setStateName(TTKCommon.checkNull(rs.getString("STATE_NAME")));
					preauthHospitalVO.setPincode(TTKCommon.checkNull(rs.getString("PIN_CODE")));
					preauthHospitalVO.setPhoneNbr1(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_1")));
					preauthHospitalVO.setPhoneNbr2(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_2")));
					preauthHospitalVO.setFaxNbr(TTKCommon.checkNull(rs.getString("OFFICE_FAX_NO")));
					preauthHospitalVO.setEmailID(TTKCommon.checkNull(rs.getString("PRIMARY_EMAIL_ID")));
					preauthHospitalVO.setHospRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
					preauthHospitalVO.setHospServiceTaxRegnNbr(TTKCommon.checkNull(rs.getString("SERV_TAX_RGN_NUMBER")));
					preauthHospitalVO.setHospStatus(TTKCommon.checkNull(rs.getString("EMPANEL_DESCRIPTION")));
					preauthHospitalVO.setEmpanelStatusTypeID(TTKCommon.checkNull(rs.getString("EMPANEL_TYPE_ID")));
					preauthHospitalVO.setRating(TTKCommon.checkNull(rs.getString("RATING")));

					if(preauthHospitalVO.getRating() != null){
						if(preauthHospitalVO.getRating().equals("G")){
							preauthHospitalVO.setRatingImageName("GoldStar");
							preauthHospitalVO.setRatingImageTitle("Gold Star");
						}//end of if(preAuthHospitalVO.getRating().equals("G"))

						if(preauthHospitalVO.getRating().equals("R")){
							preauthHospitalVO.setRatingImageName("BlueStar");
							preauthHospitalVO.setRatingImageTitle("Blue Star (Regular)");
						}//end of if(preAuthHospitalVO.getRating().equals("R"))

						if(preauthHospitalVO.getRating().equals("B")){
							preauthHospitalVO.setRatingImageName("BlackStar");
							preauthHospitalVO.setRatingImageTitle("Black Star");
						}//end of if(preAuthHospitalVO.getRating().equals("B"))
					}//end of if(preAuthHospitalVO.getRating() != null)

					if(TTKCommon.checkNull(rs.getString("REQUEST_GENERAL_TYPE_ID")).equalsIgnoreCase("DTA")){
						alPrevClaimList = getPrevClaim(rs.getLong("MEMBER_SEQ_ID"));
						preauthDetailVO.setPrevClaimList(alPrevClaimList);
					}//end of if(TTKCommon.checkNull(rs.getString("REQUEST_GENERAL_TYPE_ID")).equalsIgnoreCase("DTA"))

					if(rs.getString("CLAIM_GENERAL_TYPE_ID") != null && rs.getString("MEMBER_SEQ_ID") != null){
						hmPrevHospDetails = (HashMap)getPrevHospList(rs.getLong("MEMBER_SEQ_ID"),rs.getString("CLAIM_GENERAL_TYPE_ID"),rs.getLong("CLAIM_SEQ_ID"));
						preauthDetailVO.setPrevHospDetails(hmPrevHospDetails);
					}//end of if(rs.getString("CLAIM_GENERAL_TYPE_ID") != null && rs.getString("MEMBER_SEQ_ID") != null)

					if(rs.getString("EVENT_SEQ_ID") != null){
						preauthDetailVO.setEventSeqID(new Long(rs.getLong("EVENT_SEQ_ID")));
					}//end of if(rs.getString("EVENT_SEQ_ID") != null)

					if(rs.getString("REQUIRED_REVIEW_COUNT") != null){
						preauthDetailVO.setRequiredReviewCnt(new Integer(rs.getInt("REQUIRED_REVIEW_COUNT")));
					}//end of if(rs.getString("REQUIRED_REVIEW_COUNT") != null)

					if(rs.getString("REVIEW_COUNT") != null){
						preauthDetailVO.setReviewCount(new Integer(rs.getInt("REVIEW_COUNT")));
					}//end of if(rs.getString("REVIEW_COUNT") != null)

					preauthDetailVO.setReview(TTKCommon.checkNull(rs.getString("REVIEW")));
					preauthDetailVO.setEventName(TTKCommon.checkNull(rs.getString("EVENT_NAME")));
					preauthDetailVO.setCompletedYN(TTKCommon.checkNull(rs.getString("COMPLETED_YN")));

					if(rs.getString("PREV_HOSP_CLAIM_SEQ_ID") != null){
						preauthDetailVO.setPrevHospClaimSeqID(new Long(rs.getLong("PREV_HOSP_CLAIM_SEQ_ID")));
					}//end of if(rs.getString("PREV_HOSP_CLAIM_SEQ_ID") != null)

					preauthDetailVO.setDiscPresentYN(TTKCommon.checkNull(rs.getString("DISCREPANCY_PRESENT_YN")));
					preauthDetailVO.setAmmendmentYN(TTKCommon.checkNull(rs.getString("AMMENDMENT_YN")));
					preauthDetailVO.setDMSRefID(TTKCommon.checkNull(rs.getString("CLAIM_DMS_REFERENCE_ID")));
					preauthDetailVO.setShowCodingOverrideYN(TTKCommon.checkNull(rs.getString("SHOW_CODING_OVERRIDE")));
					preauthDetailVO.setShowReAssignIDYN(TTKCommon.checkNull(rs.getString("SHOW_REASSIGN_ID_YN")));
					//added for Critical Illness CR- KOC-1273
                    preauthDetailVO.setShowCriticalMsg(TTKCommon.checkNull(rs.getString("V_CRITICAL_MSG")));
					preauthDetailVO.setInsUnfreezeButtonYN(TTKCommon.checkNull(rs.getString("CLM_INS_STATUS")));//1274A
                    preauthDetailVO.setInsDecisionyn(TTKCommon.checkNull(rs.getString("INS_DECISION_YN")));//baja enhan
				//	preauthDetailVO.setShowCriticalMsg(TTKCommon.checkNull(rs.getString("V_CRITICAL_MSG")));
					//ended
                    preauthDetailVO.setPaymentTo(TTKCommon.checkNull(rs.getString("PYMNT_TO_TYPE_ID")));
					preauthDetailVO.setPartnerName(TTKCommon.checkNull(rs.getString("PTNR_SEQ_ID")));
					preauthDetailVO.setCommonFileNo(TTKCommon.checkNull(rs.getString("COMMON_FILE_NO")));
					
					
					

					preauthDetailVO.setClaimDetailVO(claimDetailVO);
					preauthDetailVO.setClaimantVO(claimantVO);
					preauthDetailVO.setPreAuthHospitalVO(preauthHospitalVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimDetail()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimDetail()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimDetail()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return preauthDetailVO;
	}//end of getClaimDetail(ArrayList alClaimList)
	/**
	 * This method returns the PreAuthDetailVO, which contains all the Claim details
	 * @param alClaimList contains Claim seq id,Member Seq ID to get the Claim Details
	 * @return PreAuthDetailVO object which contains all the Claim details
	 * @exception throws TTKException
	 */
	public Map<String,String> getMemClaimList(Long enrollSeqID,String claimType, String hospSeqId,String submissionCatagory,String paymentTo) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Map<String,String> memClaimList = new LinkedHashMap<String,String>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetMemClaimList);

				cStmtObject.setLong(1,enrollSeqID);
				cStmtObject.setString(2,claimType);
				cStmtObject.setString(3,hospSeqId);
				cStmtObject.setString(4,submissionCatagory);
				cStmtObject.setString(5,paymentTo);
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(6);
			if(rs != null){
				while(rs.next()){
					memClaimList.put(rs.getString("CLAIM_SEQ_ID"), rs.getString("CLAIM_NUMBER"));
				}//end of while(rs.next())
			}//end of if(rs != null)
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimDetail()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimDetail()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimDetail()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return memClaimList;
	}//end of getMemClaimList(Long memSeqID)
	/**
	 * This method returns the PreAuthDetailVO, which contains all the Claim details
	 * @param alClaimList contains Claim seq id,Member Seq ID to get the Claim Details
	 * @return PreAuthDetailVO object which contains all the Claim details
     * @throws TTKException
	 * @exception throws TTKException
     *
     *
     *
     *

	 */

	/*public int saveActivityDetails(ArrayList l) throws TTKException
	{
		  

		String pro="{CALL CLAIM_PKG.UPDATE_ACTIVITY_COPAY (?,?,?,?,?,?,?)}";


		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet clmrs=null;ResultSet drs=null;ResultSet ars = null;ResultSet shrs = null;ResultSet sum = null;
		Boolean res = null;
		try{
			//  
			conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(pro);
			for(int i=0;i<l.size();i++){
			ActivityDetailsVO a=(ActivityDetailsVO) l.get(i);
			cStmtObject.setLong(1,a.getActivityDtlSeqId());
			  
			cStmtObject.setBigDecimal(2,a.getNetAmount());
			  
			cStmtObject.setBigDecimal(3,a.getDisAllowedAmount());
			cStmtObject.setBigDecimal(3,a.getAllowedAmount());
			  
			cStmtObject.setBigDecimal(4,a.getApprovedAmount());
			  
			cStmtObject.setBigDecimal(5,a.getPatientShare());
			  
			cStmtObject.setLong(6, a.getClaimSeqID());
			         
			 cStmtObject.setBigDecimal(7,a.getCopay());
			   
			res=cStmtObject.execute();
			}

			}
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "claim");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "claim");
			}


		  
		return 0;


	}*/




	public Object[] getClaimDetails(Long claimSeqId) throws TTKException {		// 007
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet clmrs=null;ResultSet drs=null;ResultSet ars = null;ResultSet shrs = null;ResultSet sum = null;ResultSet pcount = null;ResultSet clmcount = null;
		PreAuthDetailVO preAuthDetailVO  = new PreAuthDetailVO();
		ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
		ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
		ArrayList<String[]> shortfalls=null;
		Object[] results=new Object[5];
		try{

			conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimDetail);
            cStmtObject.setLong(1,claimSeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
            cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
            cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
            cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
			//cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();
			clmrs = (java.sql.ResultSet)cStmtObject.getObject(2);//claim Details
			drs = (java.sql.ResultSet)cStmtObject.getObject(3);//diagnosis Details
			ars = (java.sql.ResultSet)cStmtObject.getObject(4);//activity Details
			shrs = (java.sql.ResultSet)cStmtObject.getObject(6);//activity Details
			pcount = (java.sql.ResultSet)cStmtObject.getObject(7);//pre-auth count
			clmcount = (java.sql.ResultSet)cStmtObject.getObject(8);//claims count
			
			//sum = (java.sql.ResultSet)cStmtObject.getObject(9);

			if(clmrs != null){
				if(clmrs.next()){
					if(clmrs.getString("CLAIM_SEQ_ID") !=null)preAuthDetailVO.setClaimSeqID(clmrs.getLong("CLAIM_SEQ_ID"));

					if(clmrs.getString("PARENT_CLAIM_SEQ_ID") !=null)preAuthDetailVO.setParentClaimSeqID(clmrs.getLong("PARENT_CLAIM_SEQ_ID"));

					if(clmrs.getString("CLM_BATCH_SEQ_ID") !=null)preAuthDetailVO.setBatchSeqID(clmrs.getLong("CLM_BATCH_SEQ_ID"));

					if(clmrs.getString("BATCH_NO") !=null)preAuthDetailVO.setBatchNo(clmrs.getString("BATCH_NO"));
					if(clmrs.getString("CLAIM_TYPE") !=null)preAuthDetailVO.setClaimType(clmrs.getString("CLAIM_TYPE"));

					if(clmrs.getString("INVOICE_NUMBER") !=null)preAuthDetailVO.setInvoiceNo(clmrs.getString("INVOICE_NUMBER"));

					if(clmrs.getLong("PAT_AUTH_SEQ_ID") !=0)preAuthDetailVO.setPreAuthSeqID(clmrs.getLong("PAT_AUTH_SEQ_ID"));

					preAuthDetailVO.setClaimNo(TTKCommon.checkNull(clmrs.getString("CLAIM_NUMBER")));
					preAuthDetailVO.setClaimStatus(TTKCommon.checkNull(clmrs.getString("CLM_STATUS_TYPE_ID")));
					//preAuthDetailVO.setRequestedAmount(requestedAmount);
					if(clmrs.getString("AUTH_NUMBER") !=null) preAuthDetailVO.setAuthNum(clmrs.getString("AUTH_NUMBER"));

					if(clmrs.getString("PAT_RECEIVED_DATE") != null){
						preAuthDetailVO.setPreAuthReceivedDateAsString(TTKCommon.convertDateAsString("dd/MM/yyyy hh::mm", clmrs.getDate("PAT_RECEIVED_DATE")));
					}//end of if(rs.getString("PAT_RECEIVED_DATE") != null)

					if(clmrs.getString("PAT_APPROVED_AMOUNT") != null){
						preAuthDetailVO.setPreAuthApprAmt(new BigDecimal(clmrs.getString("PAT_APPROVED_AMOUNT")));
					}//end of if(rs.getString("TOTAL_APP_AMOUNT") != null)


					if(clmrs.getString("CLAIM_NUMBER") !=null) preAuthDetailVO.setClaimNo(clmrs.getString("CLAIM_NUMBER"));
					if(clmrs.getString("SETTLEMENT_NUMBER") !=null) preAuthDetailVO.setClaimSettelmentNo(clmrs.getString("SETTLEMENT_NUMBER"));
					preAuthDetailVO.setModeOfClaim(TTKCommon.checkNull(clmrs.getString("SOURCE_TYPE_ID")));
					if(clmrs.getString("CLM_RECEIVED_DATE") != null){
						preAuthDetailVO.setReceiveDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(clmrs.getTimestamp("CLM_RECEIVED_DATE").getTime())));
						preAuthDetailVO.setReceiveTime(TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("CLM_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("CLM_RECEIVED_DATE").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setReceiveDay(TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("CLM_RECEIVED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("CLM_RECEIVED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(clmrs.getString("PAT_RECEIVED_DATE") != null)
					if(clmrs.getString("DATE_OF_HOSPITALIZATION") != null){
						preAuthDetailVO.setAdmissionDate(new Date(clmrs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime()));
						preAuthDetailVO.setAdmissionTime(TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setAdmissionDay(TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_HOSPITALIZATION").getTime())).split(" ")[2]:"");
					}//end of if(clmrs.getString("DATE_OF_HOSPITALIZATION") != null)
					if(clmrs.getString("DATE_OF_DISCHARGE") != null){
						preAuthDetailVO.setDischargeDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(clmrs.getTimestamp("DATE_OF_DISCHARGE").getTime())));
						preAuthDetailVO.setDischargeTime(TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ")[1]:"");
						preAuthDetailVO.setDischargeDay(TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(clmrs.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ")[2]:"");
					}//end of if(clmrs.getString("DISCHARGE_DATE") != null)

					if(clmrs.getString("TPA_ENROLLMENT_ID") != null){
						preAuthDetailVO.setMemberId(clmrs.getString("TPA_ENROLLMENT_ID"));
						preAuthDetailVO.setHiddenMemberID(clmrs.getString("TPA_ENROLLMENT_ID"));
					}//end of if(clmrs.getString("PAT_REQUESTED_AMOUNT") != null)
					if(clmrs.getLong("MEMBER_SEQ_ID") !=0){
						preAuthDetailVO.setMemberSeqID(clmrs.getLong("MEMBER_SEQ_ID"));
					}//end of if(clmrs.getLong("MEMBER_SEQ_ID") != null)
					if(clmrs.getString("MEM_NAME") != null){
						preAuthDetailVO.setPatientName(clmrs.getString("MEM_NAME"));
					}//end of if(clmrs.getString("MEM_NAME") != null)
					if(clmrs.getString("COMPLETED_YN") != null){
						preAuthDetailVO.setClaimCompleteStatus(clmrs.getString("COMPLETED_YN"));
					}//end of if(clmrs.getString("COMPLETED_YN") != null)

					if(clmrs.getInt("MEM_AGE") != 0){
						preAuthDetailVO.setMemberAge(clmrs.getInt("MEM_AGE"));
					}

					if(clmrs.getString("EMIRATE_ID") !=null){
						preAuthDetailVO.setEmirateId(clmrs.getString("EMIRATE_ID"));
                    }
					if(clmrs.getString("PAYER_ID") !=null){
						preAuthDetailVO.setPayerId(clmrs.getString("PAYER_ID"));
					}
					if(clmrs.getLong("INS_SEQ_ID") !=0){
					preAuthDetailVO.setInsSeqId(clmrs.getLong("INS_SEQ_ID"));
					}
					if(clmrs.getLong("POLICY_SEQ_ID") !=0){
						preAuthDetailVO.setPolicySeqId(clmrs.getLong("POLICY_SEQ_ID"));
                    }//
					if(clmrs.getString("INS_COMP_NAME") !=null){
						preAuthDetailVO.setPayerName(clmrs.getString("INS_COMP_NAME"));
					}
					if(clmrs.getString("ENCOUNTER_TYPE_ID") !=null){
						preAuthDetailVO.setEncounterTypeId(clmrs.getString("ENCOUNTER_TYPE_ID"));
					}//
					if(clmrs.getString("ENCOUNTER_FACILITY_ID") !=null){
						preAuthDetailVO.setEncounterFacilityId(clmrs.getString("ENCOUNTER_FACILITY_ID"));
					}//
					if(clmrs.getString("PROVIDER_ID") !=null){
						preAuthDetailVO.setProviderId(clmrs.getString("PROVIDER_ID"));
					}//
					if(clmrs.getString("HOSP_NAME") !=null){
						preAuthDetailVO.setProviderName(clmrs.getString("HOSP_NAME"));
					}//
					if(clmrs.getLong("HOSP_SEQ_ID") !=0){
						preAuthDetailVO.setProviderSeqId(clmrs.getLong("HOSP_SEQ_ID"));
					}//

					if(clmrs.getString("HOSP_ADDRESS") !=null){
						preAuthDetailVO.setProviderDetails(clmrs.getString("HOSP_ADDRESS"));
					}//
					if(clmrs.getString("ENCOUNTER_START_TYPE") !=null){
						preAuthDetailVO.setEncounterStartTypeId(clmrs.getString("ENCOUNTER_START_TYPE"));
					}//
					if(clmrs.getString("ENCOUNTER_END_TYPE") !=null){
						preAuthDetailVO.setEncounterEndTypeId(clmrs.getString("ENCOUNTER_END_TYPE"));
					}//
					if(clmrs.getString("CLINICIAN_ID") !=null){
						preAuthDetailVO.setClinicianId(clmrs.getString("CLINICIAN_ID"));
					}//
					if(clmrs.getString("CLINICIAN_NAME") !=null){
						preAuthDetailVO.setClinicianName(clmrs.getString("CLINICIAN_NAME"));
					}//
					if(clmrs.getString("SYSTEM_OF_MEDICINE_TYPE_ID") !=null){
						preAuthDetailVO.setSystemOfMedicine(clmrs.getString("SYSTEM_OF_MEDICINE_TYPE_ID"));
					}//
					if(clmrs.getString("ACCIDENT_RELATED_TYPE_ID") !=null){
						preAuthDetailVO.setAccidentRelatedCase(clmrs.getString("ACCIDENT_RELATED_TYPE_ID"));
					}//
					if(clmrs.getString("GENDER") !=null){
						preAuthDetailVO.setPatientGender(clmrs.getString("GENDER"));
					}//
					if(clmrs.getString("PRESENTING_COMPLAINTS") !=null){
						preAuthDetailVO.setPresentingComplaints(clmrs.getString("PRESENTING_COMPLAINTS"));
					}//
//Added For PED
					preAuthDetailVO.setDurAilment(clmrs.getInt("DUR_AILMENT"));
				
				if(clmrs.getString("DURATION_FLAG") !=null){
						preAuthDetailVO.setDurationFlag(clmrs.getString("DURATION_FLAG"));
				}
				if(clmrs.getString("MEDICAL_OPINION_REMARKS") !=null){
						preAuthDetailVO.setMedicalOpinionRemarks(clmrs.getString("MEDICAL_OPINION_REMARKS"));
					}
					if(clmrs.getString("PRODUCT_NAME") !=null){
						preAuthDetailVO.setProductName(clmrs.getString("PRODUCT_NAME"));
					}
					if(clmrs.getString("PAYER_AUTHORITY") !=null){
						preAuthDetailVO.setPayerAuthority(clmrs.getString("PAYER_AUTHORITY"));

					}


					if(clmrs.getString("COMPLETED_YN") !=null){
						String viewMode=clmrs.getString("COMPLETED_YN");
						preAuthDetailVO.setPreauthViewMode("Y".equals(viewMode)?"true":"false");
					}
                    if(clmrs.getString("PRIORITY_GENERAL_TYPE_ID") !=null){
						preAuthDetailVO.setPriorityTypeID(clmrs.getString("PRIORITY_GENERAL_TYPE_ID"));
					}
                    if(clmrs.getString("NETWORK_YN") !=null){
						preAuthDetailVO.setNetworkProviderType(clmrs.getString("NETWORK_YN"));
					}
                   if(clmrs.getString("CITY_TYPE_ID") !=null){
						preAuthDetailVO.setProviderArea(clmrs.getString("CITY_TYPE_ID"));
					}
                   if(clmrs.getString("STATE_TYPE_ID") !=null){
						preAuthDetailVO.setProviderEmirate(clmrs.getString("STATE_TYPE_ID"));
					}
                   if(clmrs.getString("COUNTRY_TYPE_ID") !=null){
						preAuthDetailVO.setProviderCountry(clmrs.getString("COUNTRY_TYPE_ID"));
					}
                   if(clmrs.getString("PROVIDER_ADDRESS") !=null){
						preAuthDetailVO.setProviderAddress(clmrs.getString("PROVIDER_ADDRESS"));
					}
                   if(clmrs.getString("PIN_CODE") !=null){
						preAuthDetailVO.setProviderPobox(clmrs.getString("PIN_CODE"));
					}

                    if(clmrs.getBigDecimal("REQUESTED_AMOUNT") !=null){
						preAuthDetailVO.setRequestedAmount(clmrs.getBigDecimal("REQUESTED_AMOUNT"));
						}
					if(clmrs.getString("RE_SUBMISSION_REMARKS") !=null){
						preAuthDetailVO.setRemarks(clmrs.getString("RE_SUBMISSION_REMARKS"));
					}
					if(clmrs.getString("CLINICIAN_STATUS") !=null){
						preAuthDetailVO.setClinicianStatus(clmrs.getString("CLINICIAN_STATUS"));
					}
					if(clmrs.getBigDecimal("TOT_GROSS_AMOUNT") !=null){
						preAuthDetailVO.setGrossAmount(clmrs.getBigDecimal("TOT_GROSS_AMOUNT"));
					}
					if(clmrs.getBigDecimal("TOT_DISCOUNT_AMOUNT") !=null){
						preAuthDetailVO.setDiscountAmount(clmrs.getBigDecimal("TOT_DISCOUNT_AMOUNT"));
					}
					if(clmrs.getBigDecimal("TOT_DISC_GROSS_AMOUNT") !=null){
						preAuthDetailVO.setDiscountGrossAmount(clmrs.getBigDecimal("TOT_DISC_GROSS_AMOUNT"));
					}

					if(clmrs.getBigDecimal("TOT_PATIENT_SHARE_AMOUNT") !=null){
						preAuthDetailVO.setPatShareAmount(clmrs.getBigDecimal("TOT_PATIENT_SHARE_AMOUNT"));
					}
					if(clmrs.getBigDecimal("TOT_NET_AMOUNT") !=null){
						preAuthDetailVO.setNetAmount(clmrs.getBigDecimal("TOT_NET_AMOUNT"));
					}
					if(clmrs.getBigDecimal("TOT_ALLOWED_AMOUNT") !=null){
						preAuthDetailVO.setAllowedAmount(clmrs.getBigDecimal("TOT_ALLOWED_AMOUNT"));
					}

					if(clmrs.getBigDecimal("TOTAL_APP_AMOUNT") !=null){
						preAuthDetailVO.setApprovedAmount(clmrs.getBigDecimal("TOTAL_APP_AMOUNT"));
					}
					if(clmrs.getBigDecimal("FINAL_APP_AMOUNT") !=null){
						preAuthDetailVO.setFinalApprovedAmount(clmrs.getBigDecimal("FINAL_APP_AMOUNT"));
					}
					if(clmrs.getString("POLICY_NUMBER") !=null){
						preAuthDetailVO.setPolicyNumber(clmrs.getString("POLICY_NUMBER"));
					}
					if(clmrs.getString("CORP_NAME") !=null){
						preAuthDetailVO.setCorporateName(clmrs.getString("CORP_NAME"));
					}
					/*
					if(clmrs.getInt("GRAVIDA") !=0){
						preAuthDetailVO.setGravida(clmrs.getInt("GRAVIDA"));
					}
					if(clmrs.getInt("LIVE") !=0){
						preAuthDetailVO.setLive(clmrs.getInt("LIVE"));
					}
					if(clmrs.getInt("PARA") !=0){
						preAuthDetailVO.setPara(clmrs.getInt("PARA"));
					}
					if(clmrs.getInt("ABORTION") !=0){
						preAuthDetailVO.setAbortion(clmrs.getInt("ABORTION"));
					}
					*/
					/*if(clmrs.getString("GRAVIDA") !=null){
						preAuthDetailVO.setGravida(clmrs.getInt("GRAVIDA"));
					}
					if(clmrs.getString("LIVE") !=null){
						preAuthDetailVO.setLive(clmrs.getInt("LIVE"));
					}
					if(clmrs.getString("PARA") !=null){
						preAuthDetailVO.setPara(clmrs.getInt("PARA"));
					}
					if(clmrs.getString("ABORTION") !=null){
						preAuthDetailVO.setAbortion(clmrs.getInt("ABORTION"));
					}*/
					if(clmrs.getString("BENIFIT_TYPES") !=null){
						preAuthDetailVO.setBenefitType(clmrs.getString("BENIFIT_TYPES"));
						preAuthDetailVO.setBenefitTyperef(clmrs.getString("BENIFIT_TYPES"));
					}
										
					if(clmrs.getString("MAT_COMPLCTON_YN") !=null){
					  	preAuthDetailVO.setMatsubbenefit(clmrs.getString("MAT_COMPLCTON_YN"));
					}

					if(clmrs.getString("FRESH_YN") !=null)preAuthDetailVO.setPreAuthNoStatus(("Y".equals(clmrs.getString("FRESH_YN"))?"FRESH":"ENHANCEMENT"));

					if(clmrs.getString("CURRENCY_TYPE") !=null){
					  	preAuthDetailVO.setRequestedAmountCurrency(clmrs.getString("CURRENCY_TYPE"));
					}
					if(clmrs.getDate("POLICY_START_DATE") !=null){
					  	preAuthDetailVO.setPolicyStartDate(TTKCommon.convertDateAsString("dd/MM/yyyy", clmrs.getDate("POLICY_START_DATE")));
					}
					if(clmrs.getDate("POLICY_END_DATE") !=null){
					  	preAuthDetailVO.setPolicyEndDate(TTKCommon.convertDateAsString("dd/MM/yyyy", clmrs.getDate("POLICY_END_DATE")));
					}
					preAuthDetailVO.setNationality(TTKCommon.checkNull(clmrs.getString("NATIONALITY")));
					if(clmrs.getBigDecimal("SUM_INSURED") !=null){
						preAuthDetailVO.setSumInsured(clmrs.getBigDecimal("SUM_INSURED"));
					}
					if(clmrs.getBigDecimal("AVA_SUM_INSURED") !=null){
						preAuthDetailVO.setAvailableSumInsured(clmrs.getBigDecimal("AVA_SUM_INSURED"));
					}
					if(clmrs.getBigDecimal("DIS_ALLOWED_AMOUNT") !=null){
						preAuthDetailVO.setDisAllowedAmount(clmrs.getBigDecimal("DIS_ALLOWED_AMOUNT"));
					}
					if(clmrs.getString("ENROL_TYPE_ID") !=null){
						preAuthDetailVO.setPolicyType(clmrs.getString("ENROL_TYPE_ID"));
					}
					if(clmrs.getString("HOSP_ID") !=null){
						preAuthDetailVO.setHiddenHospitalID(clmrs.getString("HOSP_ID"));
					}


					if(clmrs.getString("clm_mem_insp_date") !=null){
						preAuthDetailVO.setClmMemInceptionDate(clmrs.getString("clm_mem_insp_date"));

					}


					preAuthDetailVO.setClaimSubmissionType(clmrs.getString("SUBMISSION_TYPE_ID"));
					preAuthDetailVO.setClaimFrom(clmrs.getString("CLAIMFROM_GENTYPE_ID"));
					preAuthDetailVO.setFinalRemarks(clmrs.getString("FINAL_REMARKS"));
					preAuthDetailVO.setDuplicateClaimAlert(clmrs.getString("CLM_DUP"));
					preAuthDetailVO.setOverrideRemarks(clmrs.getString("OVERRIDE_REMARKS"));
					preAuthDetailVO.setEmirateId(clmrs.getString("EMIRATE_ID"));
					//preAuthDetailVO.setProvAuthority(clmrs.getString("PROVIDER_AUTHORITY"));
                    preAuthDetailVO.setEligibleNetworks(clmrs.getString("ELIGIBLE_NETWORKS"));
				//	  


					preAuthDetailVO.setAssignedTo(clmrs.getString("ASSIGNED_TO"));
					preAuthDetailVO.setVipYorN(clmrs.getString("VIP_YN"));
					preAuthDetailVO.setRelationShip(TTKCommon.checkNull(clmrs.getString("relationship_desc")));
					preAuthDetailVO.setRelationFlag(TTKCommon.checkNull(clmrs.getString("COLOUR_YN")));
					preAuthDetailVO.setMemberSpecificRemarks(TTKCommon.checkNull(clmrs.getString("member_remarks")));
					preAuthDetailVO.setEnablericopar(clmrs.getString("ri_copar_flag"));
					preAuthDetailVO.setEnableucr(clmrs.getString("ucr_flag"));
					
					if(clmrs.getString("CLAIM_NUMBER")!=null){
					preAuthDetailVO.setMemCoveredAlert(clmrs.getString("MEM_ALERT"));
					}
					
					preAuthDetailVO.setCurrencyType(clmrs.getString("fnl_amt_currency_type"));
					preAuthDetailVO.setProviderSpecificRemarks(clmrs.getString("PROVIDER_SPECIFIC_REMARKS"));	
                    preAuthDetailVO.setClinicianWarning(clmrs.getString("clinician_warning"));
					
					preAuthDetailVO.setTakafulClaimRefNo(clmrs.getString("takaful_ref_no"));
					preAuthDetailVO.setTakafulYN(clmrs.getString("ins_flag"));
					
					
					preAuthDetailVO.setLatMenstraulaPeriod(clmrs.getString("LMP_DATE")!=null ? new Date(clmrs.getTimestamp("LMP_DATE").getTime()):null);
					preAuthDetailVO.setModeofdelivery(clmrs.getString("DELVRY_MOD_TYPE"));
					preAuthDetailVO.setNatureOfConception(clmrs.getString("CONCEPTION_TYPE"));
					preAuthDetailVO.setTreatmentTypeID(clmrs.getString("TREATMENT_TYPE"));	
					preAuthDetailVO.setProdPolicySeqId(clmrs.getInt("PROD_POLICY_SEQ_ID"));
					
					String alertMsg=clmrs.getString("ALT_MSG")==null?"":clmrs.getString("ALT_MSG"); // alertMsg added by govind
					preAuthDetailVO.setAlertMsg(alertMsg);
					preAuthDetailVO.setRevertFlag(TTKCommon.checkNull(clmrs.getString("cal_act_yn")));
					
					DentalOrthoVO orthoVO	=	new DentalOrthoVO();
					orthoVO.setDentoSeqid(clmrs.getLong("ORTHO_SEQ_ID"));
					orthoVO.setPreAuthSeqid(clmrs.getLong("SOURCE_SEQ_ID"));
					orthoVO.setDentoclass1(clmrs.getString("DENTO_CLASS_I"));
					orthoVO.setDentoclass2(clmrs.getString("DENTO_CLASS_II"));
					orthoVO.setDentoclass2Text(clmrs.getString("DENTO_CLASS_II_TEXT"));
					orthoVO.setDentoclass3(clmrs.getString("DENTO_CLASS_III"));
					orthoVO.setDentoclass3Text(clmrs.getString("DENTO_CLASS_III_TEXT"));
					orthoVO.setSkeletalClass1(clmrs.getString("SKELE_CLASS_I"));
					orthoVO.setSkeletalClass2(clmrs.getString("SKELE_CLASS_II"));
					orthoVO.setSkeletalClass3(clmrs.getString("SKELE_CLASS_III"));
					orthoVO.setOverJet(clmrs.getString("OVERJET_MM"));
					orthoVO.setReverseJet(clmrs.getString("REV_OVERJET_MM"));
					orthoVO.setReverseJetYN(clmrs.getString("REV_OVERJET_YN"));
					orthoVO.setCrossbiteAntrio(clmrs.getString("CROSSBITE_ANT"));
					orthoVO.setCrossbitePosterior(clmrs.getString("CROSSBITE_POST"));
					orthoVO.setCrossbiteRetrucontract(clmrs.getString("CROSSBITE_BETW"));
					orthoVO.setOpenbiteAntrio(clmrs.getString("OPENBIT_ANT"));
					orthoVO.setOpenbitePosterior(clmrs.getString("OPENBIT_POST"));
					orthoVO.setOpenbiteLateral(clmrs.getString("OPENBIT_LATE"));
					orthoVO.setContactPointDisplacement(clmrs.getString("CONT_POINT_DISP"));
					orthoVO.setOverBite(clmrs.getString("OVERBITE"));
					orthoVO.setOverbitePalatalYN(clmrs.getString("OVERBIT_PATA"));
					orthoVO.setOverbiteGingivalYN(clmrs.getString("OVERBIT_GING"));
					orthoVO.setHypodontiaQuand1Teeth(clmrs.getString("HYPO_QUAD1"));
					orthoVO.setHypodontiaQuand2Teeth(clmrs.getString("HYPO_QUAD2"));
					orthoVO.setHypodontiaQuand3Teeth(clmrs.getString("HYPO_QUAD3"));
					orthoVO.setHypodontiaQuand4Teeth(clmrs.getString("HYPO_QUAD4"));
					orthoVO.setImpededTeethEruptionNo(clmrs.getString("OTHERS_IMPEDED"));
					orthoVO.setImpededTeethNo(clmrs.getString("OTHERS_IMPACT"));
					orthoVO.setSubmergerdTeethNo(clmrs.getString("OTHERS_SUBMERG"));
					orthoVO.setSupernumeryTeethNo(clmrs.getString("OTHERS_SUPERNUM"));
					orthoVO.setRetainedTeethNo(clmrs.getString("OTHERS_RETAINE"));
					orthoVO.setEctopicTeethNo(clmrs.getString("OTHERS_ECTOPIC"));
					orthoVO.setCranioFacialNo(clmrs.getString("OTHERS_CRANIO"));
					orthoVO.setCrossbiteAntriomm(clmrs.getString("CROSSBITE_ANT_MM"));
					orthoVO.setCrossbitePosteriormm(clmrs.getString("CROSSBITE_PRST_MM"));
					orthoVO.setCrossbiteRetrucontractmm(clmrs.getString("CROSSBITE_BETW_MM"));
					orthoVO.setContactPointDisplacementmm(clmrs.getString("CONT_POINT_DISP_MM"));
					orthoVO.setAestheticComp(clmrs.getString("AC_MARKS"));
					orthoVO.setIotn(clmrs.getString("IOTN"));
					preAuthDetailVO.setDentalOrthoVO(orthoVO);
					if(clmrs.getString("MAT_ALERT_MSG") !=null) preAuthDetailVO.setMaternityAlert(clmrs.getString("MAT_ALERT_MSG"));
					
					preAuthDetailVO.setRequestedAmountcurrencyType(TTKCommon.checkNull(clmrs.getString("REQ_AMT_CURRENCY_TYPE")));
					preAuthDetailVO.setCurrencyType(clmrs.getString("CONVERTED_AMOUNT_CURRENCY_TYPE"));
					preAuthDetailVO.setConversionRate(clmrs.getString("CONVERSION_RATE"));
					preAuthDetailVO.setConvertedAmount(clmrs.getBigDecimal("CONVERTED_AMOUNT"));
					preAuthDetailVO.setConvertedFinalApprovedAmount(clmrs.getBigDecimal("converted_final_approved_amt")); 
					preAuthDetailVO.setTpaReferenceNo(clmrs.getString("oth_tpa_ref_no"));
					preAuthDetailVO.setProcessType(TTKCommon.checkNull(clmrs.getString("process_type")));
					preAuthDetailVO.setPaymentTo(TTKCommon.checkNull(clmrs.getString("PYMNT_TO_TYPE_ID")));
					preAuthDetailVO.setPartnerName(TTKCommon.checkNull(clmrs.getString("PTNR_SEQ_ID")));
					preAuthDetailVO.setEventReferenceNo(TTKCommon.checkNull(clmrs.getString("event_no")));
					preAuthDetailVO.setInternalRemarks(TTKCommon.checkNull(clmrs.getString("internal_remarks")));
					
					if(clmrs.getString("DECISION_DATE")!=null)
						preAuthDetailVO.setDecisionDt(TTKCommon.getFormattedDate(TTKCommon.getString(clmrs.getString("DECISION_DATE"))));
					else
						preAuthDetailVO.setDecisionDt("");
					preAuthDetailVO.setProcessedBy(clmrs.getString("PROCESSED_BY"));
					preAuthDetailVO.setStatusCheck(TTKCommon.checkNull(clmrs.getString("CLM_STATUS_TYPE_ID")));
					preAuthDetailVO.setPaymentToEmb(TTKCommon.checkNull(clmrs.getString("PAYMENT_TO_EMB")));
					preAuthDetailVO.setUsdAmount(TTKCommon.checkNull(clmrs.getString("USD_AMOUNT")));
					preAuthDetailVO.setPatReqCurr(TTKCommon.checkNull(clmrs.getString("PAT_REQ_CURR")));
					preAuthDetailVO.setPatIncAmnt(TTKCommon.checkNull(clmrs.getString("PAT_INC_AMNT")));
					preAuthDetailVO.setPatIncCurr(TTKCommon.checkNull(clmrs.getString("PAT_INC_CURR")));
					preAuthDetailVO.setSuspectedYesorNOFlag(TTKCommon.checkNull(clmrs.getString("fraud_yn")));
					preAuthDetailVO.setCompletedYNFlag(TTKCommon.checkNull(clmrs.getString("clm_complete")));
					preAuthDetailVO.setClaimVerifiedforClaim(TTKCommon.checkNull(clmrs.getString("suspect_veri_check")));
					//System.out.println("vip YN----->>"+clmrs.getString("vip_clm_rej"));
					preAuthDetailVO.setVipMemberUserAccessPermissionFlag(TTKCommon.checkNull(clmrs.getString("vip_clm_rej")));
					if(clmrs.getString("assigned_to_user") != null){
					preAuthDetailVO.setAssignUserSeqID(Long.parseLong(TTKCommon.checkNull(clmrs.getString("assigned_to_user"))));
					}
					if(clmrs.getString("TPA_OFFICE_SEQ_ID") != null){
					preAuthDetailVO.setOfficeSeqID(Long.parseLong(TTKCommon.checkNull(clmrs.getString("TPA_OFFICE_SEQ_ID"))));
					}
					preAuthDetailVO.setBankName(TTKCommon.checkNull(clmrs.getString("BANK_NAME")));
					preAuthDetailVO.setAccountNumber(TTKCommon.checkNull(clmrs.getString("IBAN_NUMBER")));
					preAuthDetailVO.setPaymentMadeFor(TTKCommon.checkNull(clmrs.getString("PAYMENT_MADE_FOR")));

					preAuthDetailVO.setCommonFileNo(TTKCommon.checkNull(clmrs.getString("COMMON_FILE_NO")));
					preAuthDetailVO.setInProgressStatus(TTKCommon.checkNull(clmrs.getString("IN_PROGESS_STATUS")));
					preAuthDetailVO.setOverrideSuspectFlag(TTKCommon.checkNull(clmrs.getString("fraud_det_yn")));
					preAuthDetailVO.setMemberExitDate(TTKCommon.checkNull(clmrs.getString("clm_mem_exit_date")));
					preAuthDetailVO.setEuroAmount(TTKCommon.checkNull(clmrs.getString("PAY_AMT_IN_EURO")));
					preAuthDetailVO.setGbpAmount(TTKCommon.checkNull(clmrs.getString("PAY_AMT_IN_GBP")));
					preAuthDetailVO.setAuditStatus(TTKCommon.checkNull(clmrs.getString("AUDIT_STATUS")));
					preAuthDetailVO.setAuditRemarks(TTKCommon.checkNull(clmrs.getString("AUDIT_REMARKS")));
					preAuthDetailVO.setRecheckRemarks(TTKCommon.checkNull(clmrs.getString("CLM_AUDIT_REMARKS")));
					if(clmrs.getString("CLINICIAN_SPECIALITY") !=null){
						preAuthDetailVO.setClinicianSpeciality(clmrs.getString("CLINICIAN_SPECIALITY"));
					}//
				    preAuthDetailVO.setPaymentToLayOver(TTKCommon.checkNull(clmrs.getString("PAYMENT_TO")));
				    
				    preAuthDetailVO.setBankDeatailsFlag(TTKCommon.checkNull(clmrs.getString("mem_accnt_valid_yn")));
				    preAuthDetailVO.setParentClaimNo(TTKCommon.checkNull(clmrs.getString("PARENT_CLAIM_NO")));
					preAuthDetailVO.setResubmissionCount(TTKCommon.checkNull(clmrs.getString("SUBMISSION_COUNT")));
					preAuthDetailVO.setMophDrugYN(TTKCommon.checkNull(clmrs.getString("moph_yn")));
					if(clmrs.getString("Approval_Alert_log") !=null){
						preAuthDetailVO.setApprovalAlertLog(clmrs.getString("Approval_Alert_log"));
					}
					preAuthDetailVO.setLmrpAlert(TTKCommon.checkNull(clmrs.getString("lmrp_alert")));
					preAuthDetailVO.setProviderPreAppRemarks(TTKCommon.checkNull(clmrs.getString("Preauth_remarks_for_provider")));
					preAuthDetailVO.setProviderInternalRemarks(TTKCommon.checkNull(clmrs.getString("Preauth_internal_remarks")));
					preAuthDetailVO.setOverrideMainRemarks(TTKCommon.checkNull(clmrs.getString("override_main_remarks")));	
					preAuthDetailVO.setOverrideSubRemarks(TTKCommon.checkNull(clmrs.getString("override_sub_remarks")));
					preAuthDetailVO.setDiscountFlag(TTKCommon.checkNull(clmrs.getString("discount_flag")));		// 07
					preAuthDetailVO.setExceptionalWarningMsg(TTKCommon.checkNull(clmrs.getString("exception_warning")));
					preAuthDetailVO.setReferExceptionFalg(TTKCommon.checkNull(clmrs.getString("exception_yn")));
					  
				}//clmrs.while
				}//clmrs if
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

				BigDecimal allowedAmt=ars.getBigDecimal("ALLOWED_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("ALLOWED_AMOUNT");
				BigDecimal disAllowedAmt=ars.getBigDecimal("DIS_ALLOWED_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("DIS_ALLOWED_AMOUNT");

				BigDecimal providerReqAmt=ars.getBigDecimal("PROVIDER_NET_AMOUNT")==null?new BigDecimal(0):ars.getBigDecimal("PROVIDER_NET_AMOUNT");

				String denialDecs=ars.getString("DENIAL_DESC")==null?"":ars.getString("DENIAL_DESC");//denial_desc

				String denialCodes=ars.getString("DENIAL_CODE")==null?"":ars.getString("DENIAL_CODE");//denial_desc

				String activityRemarks=ars.getString("REMARKS")==null?"":ars.getString("REMARKS");

				Long activitySeqId=ars.getLong("ACTIVITY_SEQ_ID");

				Long activityDtlSeqId=ars.getLong("ACTIVITY_DTL_SEQ_ID");

                String activityCodeDec=ars.getString("ACTIVITY_DESCRIPTION")==null?"":ars.getString("ACTIVITY_DESCRIPTION");       //denial_desc

				String preAuthNo	=	ars.getString("PREAUTHNO");
				
				String internalDesc = ars.getString("INTERNAL_DESC")==null?"":ars.getString("INTERNAL_DESC");//denial_desc
				
				ActivityDetailsVO activityDetailsVO=new ActivityDetailsVO(sNo, activityCode, activityCodeDec, modifiers, unityType, startDate, activityRemarks, denialCodes,denialDecs,
						quantity, approvedQuantity, activitySeqId, activityDtlSeqId,
						grossAmt,discount, discountGross, patientShare, netAmt, approAmt, disAllowedAmt, preAuthNo);
				activityDetailsVO.setOverrideYN(ars.getString("OVERRIDE_YN"));
				activityDetailsVO.setOverrideRemarks(ars.getString("OVERRIDE_REMARKS"));
				activityDetailsVO.setClaimSeqID(ars.getLong("CLAIM_SEQ_iD"));
				activityDetailsVO.setCopay(ars.getBigDecimal("p_copay_amt"));
				activityDetailsVO.setAllowedAmount(allowedAmt);

				activityDetailsVO.setProviderRequestedAmt(providerReqAmt);
				activityDetailsVO.setToothNoReqYN(ars.getString("TOOTH_REQ_YN"));
				activityDetailsVO.setNewPharmacyYN(TTKCommon.checkNull(ars.getString("NEW_PHARMACY_YN")));
				activityDetailsVO.setInternalDesc(internalDesc);
				activityDetailsVO.setInternalCode(TTKCommon.checkNull(ars.getString("internal_code")));
				activityDetailsVO.setMophDrugYN(TTKCommon.checkNull(ars.getString("moph_yn")));
				activities.add(activityDetailsVO);

		}
        }
        if(shrs!=null){
			shortfalls=new ArrayList<String[]>();
            while(shrs.next()){
				String claimSeqID=shrs.getLong("ClAIM_SEQ_ID")==0?"":shrs.getLong("CLAIM_SEQ_ID")+"";
				String shortFallSeqId=shrs.getLong("SHORTFALL_SEQ_ID")==0?"":shrs.getLong("SHORTFALL_SEQ_ID")+"";
				String shortFallNo=shrs.getString("SHORTFALL_ID")==null?"":shrs.getString("SHORTFALL_ID");
				String shortFallsType=shrs.getString("SHORTFALL_TYPE")==null?"":shrs.getString("SHORTFALL_TYPE");
				String shortFallsStatus=shrs.getString("SRTFLL_STATUS_GENERAL_TYPE_ID")==null?"":shrs.getString("SRTFLL_STATUS_GENERAL_TYPE_ID");
				String sendDate=shrs.getString("SRTFLL_SENT_DATE")==null?"":shrs.getString("SRTFLL_SENT_DATE");
				String recdDate=shrs.getString("SRTFLL_RECEIVED_DATE")==null?"":shrs.getString("SRTFLL_RECEIVED_DATE");
                shortfalls.add(new String[]{claimSeqID,shortFallSeqId,shortFallNo,shortFallsType,shortFallsStatus,sendDate,recdDate});
			}
            }
				/*if(sum!=null)
				{
					while(sum.next())
					{
						if(sum.getBigDecimal("DIS_ALLOWED_AMOUNT") !=null){
							preAuthDetailVO.setDisAllowedAmount(sum.getBigDecimal("DIS_ALLOWED_AMOUNT"));
						}

						if(sum.getBigDecimal("TOT_PATIENT_SHARE_AMOUNT") !=null){
							preAuthDetailVO.setPatShareAmount(sum.getBigDecimal("TOT_PATIENT_SHARE_AMOUNT"));
						}
						if(sum.getBigDecimal("TOT_NET_AMOUNT") !=null){
							preAuthDetailVO.setNetAmount(sum.getBigDecimal("TOT_NET_AMOUNT"));
						}
					if(sum.getBigDecimal("TOT_ALLOWED_AMOUNT") !=null){
							  
							preAuthDetailVO.setAllowedAmount(sum.getBigDecimal("TOT_ALLOWED_AMOUNT"));
						}

					if(sum.getBigDecimal("TOTAL_APP_AMOUNT") !=null){
							  
							preAuthDetailVO.setApprovedAmount(sum.getBigDecimal("TOTAL_APP_AMOUNT"));
						}
						if(sum.getBigDecimal("FINAL_APP_AMOUNT") !=null){
							  
							preAuthDetailVO.setFinalApprovedAmount(sum.getBigDecimal("FINAL_APP_AMOUNT"));


					}

				}
				}*/

        if(pcount!=null){
        	while(pcount.next()){
        		preAuthDetailVO.setPreAuthCount(pcount.getInt("PAT_COUNT"));
        		preAuthDetailVO.setCfdPreauthCount(pcount.getInt("cfd_tag_cnt"));
        	}
        	}
        
        if(clmcount!=null){
        	while(clmcount.next()){
        		preAuthDetailVO.setClmCount(clmcount.getInt("CLM_COUNT"));
        		preAuthDetailVO.setCfdClaimCount(clmcount.getInt("cfd_tag_cnt")); 
        	}
        	}
        		

        
		results[0]=preAuthDetailVO;
		results[1]=diagnosis;
		results[2]=activities;
		results[3]=shortfalls;

			return results;
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
					if (clmrs != null) clmrs.close();
					if (drs != null) drs.close();
					if (ars != null) ars.close();
					if (shrs != null) shrs.close();
					if (sum != null) sum.close();
					if (pcount != null) pcount.close();
					if (clmcount != null) clmcount.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimDetail()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimDetail()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimDetail()",sqlExp);
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
            	clmcount = null;
            	pcount = null;
            	sum = null;
            	shrs = null;
            	ars = null;
            	drs=null;
            	clmrs=null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimDetails(Long ClaimSeqID)

	/**
	 * This method will allow to Override the Claim information
	 * @param lngClaimSeqID ClaimSeqID
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
	public PreAuthDetailVO overrideClaim(long lngClaimSeqID,long lngUserSeqID) throws TTKException {
		String strReview = "";
		Long lngEventSeqID = null;
		Integer intReviewCount = null;
		Integer intRequiredReviewCnt = null;
		String strEventName = "";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		PreAuthDetailVO preauthDetailVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strOverrideClaim);
			preauthDetailVO = new PreAuthDetailVO();
			cStmtObject.setLong(1,lngClaimSeqID);
			cStmtObject.setLong(2,lngUserSeqID);
			cStmtObject.registerOutParameter(3,Types.BIGINT);//EVENT_SEQ_ID
			cStmtObject.registerOutParameter(4,Types.BIGINT);//REVIEW_COUNT
			cStmtObject.registerOutParameter(5,Types.BIGINT);//REQUIRED_REVIEW_COUNT
			cStmtObject.registerOutParameter(6,Types.VARCHAR);//EVENT_NAME
			cStmtObject.registerOutParameter(7,Types.VARCHAR);//REVIEW
			cStmtObject.execute();

			lngEventSeqID = cStmtObject.getLong(3);
			intReviewCount = cStmtObject.getInt(4);
			intRequiredReviewCnt = cStmtObject.getInt(5);
			strEventName = cStmtObject.getString(6);
			strReview = cStmtObject.getString(7);

			preauthDetailVO.setEventSeqID(lngEventSeqID);
			preauthDetailVO.setReviewCount(intReviewCount);
			preauthDetailVO.setRequiredReviewCnt(intRequiredReviewCnt);
			preauthDetailVO.setEventName(strEventName);
			preauthDetailVO.setReview(strReview);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl overrideClaim()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl overrideClaim()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return preauthDetailVO;
	}//end of overrideClaim(long lngClaimSeqID,long lngUserSeqID)

	/**
	 * This method releases the Preauth Associated to NHCPClaim
	 * @param lngPATEnrollDtlSeqID PAT Enroll Dtl Seq ID
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int releasePreauth(long lngPATEnrollDtlSeqID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		int iResult = 1;
		try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReleasePreauth);
			cStmtObject.setLong(1,lngPATEnrollDtlSeqID);
			cStmtObject.execute();
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl releasePreauth()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl releasePreauth()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of releasePreauth(long lngPATEnrollDtlSeqID)

	/**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public long saveClaimDetail(PreAuthDetailVO preauthDetailVO) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		long lngClaimSeqID = 0;
		ClaimDetailVO claimDetailVO = null;
		ClaimantVO claimantVO = null;
		PreAuthHospitalVO preauthHospitalVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveClaimDetail);

			claimDetailVO = preauthDetailVO.getClaimDetailVO();
			claimantVO = preauthDetailVO.getClaimantVO();
			preauthHospitalVO = preauthDetailVO.getPreAuthHospitalVO();
			if(preauthDetailVO.getClmEnrollDtlSeqID() != null){
				cStmtObject.setLong(1,preauthDetailVO.getClmEnrollDtlSeqID());
			}//end of if(preauthDetailVO.getClmEnrollDtlSeqID() != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else

			if(preauthDetailVO.getClaimSeqID() != null){
				cStmtObject.setLong(2,preauthDetailVO.getClaimSeqID());
			}//end of if(preauthDetailVO.getClaimSeqID() != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			cStmtObject.setString(3,claimantVO.getGenderTypeID());

			if(claimantVO.getMemberSeqID() != null){
				cStmtObject.setLong(4,claimantVO.getMemberSeqID());
			}//end of if(claimantVO.getMemberSeqID() != null)
			else{
				cStmtObject.setString(4,null);
			}//end of else

			cStmtObject.setString(5,claimantVO.getEnrollmentID());

			if(claimantVO.getPolicySeqID() != null){
				cStmtObject.setLong(6,claimantVO.getPolicySeqID());
			}//end of if(claimantVO.getPolicySeqID() != null)
			else{
				cStmtObject.setString(6,null);
			}//end of else

			cStmtObject.setString(7,claimantVO.getPolicyHolderName());
			cStmtObject.setString(8,claimantVO.getEmployeeNbr());
			cStmtObject.setString(9,claimantVO.getEmployeeName());

			if(claimantVO.getAge() != null){
				cStmtObject.setInt(10,claimantVO.getAge());
			}//end of if(claimantVO.getAge() != null)
			else{
				cStmtObject.setInt(10,0);
			}//end of else

			cStmtObject.setString(11,claimantVO.getName());

			if(claimantVO.getDateOfInception() != null){
				cStmtObject.setTimestamp(12,new Timestamp(claimantVO.getDateOfInception().getTime()));
			}//end of if(claimantVO.getDateOfInception() != null)
			else{
				cStmtObject.setTimestamp(12,null);
			}//end of else

			if(claimantVO.getDateOfExit() != null){
				cStmtObject.setTimestamp(13,new Timestamp(claimantVO.getDateOfExit().getTime()));
			}//end of if(claimantVO.getDateOfExit() != null)
			else{
				cStmtObject.setTimestamp(13,null);
			}//end of else

			cStmtObject.setString(14,claimantVO.getRelationTypeID());
			cStmtObject.setString(15,claimantVO.getPolicyNbr());
			cStmtObject.setString(16,claimantVO.getClaimantPhoneNbr());

			if(claimantVO.getTotalSumInsured() != null){
				cStmtObject.setBigDecimal(17,claimantVO.getTotalSumInsured());
			}//end of if(claimantVO.getTotalSumInsured() != null)
			else{
				cStmtObject.setString(17,null);
			}//end of else

			cStmtObject.setString(18,claimantVO.getPolicyTypeID());
			cStmtObject.setString(19,claimantVO.getPolicySubTypeID());
			cStmtObject.setString(20,claimantVO.getPhone());

			if(claimantVO.getStartDate() != null){
				cStmtObject.setTimestamp(21,new Timestamp(claimantVO.getStartDate().getTime()));
			}//end of if(claimantVO.getStartDate() != null)
			else{
				cStmtObject.setTimestamp(21,null);
			}//end of else

			if(claimantVO.getEndDate() != null){
				cStmtObject.setTimestamp(22,new Timestamp(claimantVO.getEndDate().getTime()));
			}//end of if(claimantVO.getEndDate() != null)
			else{
				cStmtObject.setTimestamp(22,null);
			}//end of else

			cStmtObject.setString(23,claimantVO.getTermStatusID());

			if(claimantVO.getInsSeqID() != null){
				cStmtObject.setLong(24,claimantVO.getInsSeqID());
			}//end of if(claimantVO.getInsSeqID() != null)
			else{
				cStmtObject.setString(24,null);
			}//end of else

			if(claimantVO.getGroupRegnSeqID() != null){
				cStmtObject.setLong(25,claimantVO.getGroupRegnSeqID());
			}//end of if(claimantVO.getGroupRegnSeqID() != null)
			else{
				cStmtObject.setString(25,null);
			}//end of else

			if(preauthDetailVO.getInwardSeqID() != null){
				cStmtObject.setLong(26,preauthDetailVO.getInwardSeqID());
			}//end of if(preauthDetailVO.getInwardSeqID() != null)
			else{
				cStmtObject.setString(26,null);
			}//end of else

			if(preauthDetailVO.getClaimRequestAmount() != null){
				cStmtObject.setBigDecimal(27,preauthDetailVO.getClaimRequestAmount());
			}//end of if(preauthDetailVO.getClaimRequestAmount() != null)
			else{
				cStmtObject.setString(27,null);
			}//end of else

			if(preauthDetailVO.getEnrollDtlSeqID() != null){
				cStmtObject.setLong(28,preauthDetailVO.getEnrollDtlSeqID());
			}//end of if(preauthDetailVO.getEnrollDtlSeqID() != null)
			else{
				cStmtObject.setString(28,null);
			}//end of else

			cStmtObject.setString(29,preauthDetailVO.getAuthNbr());
			cStmtObject.setString(30,claimDetailVO.getRequestTypeID());
			cStmtObject.setString(31,claimDetailVO.getClaimSubTypeID());
			if(claimDetailVO.getDomicilaryReason()!= ""){
				cStmtObject.setString(32,claimDetailVO.getDomicilaryReason());
			}//end of if(preauthDetailVO.getDomicilaryReason() != null)
			else{
				cStmtObject.setString(32,null);
			}//end of else

			if(claimDetailVO.getDoctorCertificateYN() != ""){
				cStmtObject.setString(33,claimDetailVO.getDoctorCertificateYN());
			}//end of if(preauthDetailVO.getDoctorCertificateYN() != null)
			else{
				cStmtObject.setString(33,null);
			}//end of else

			cStmtObject.setString(34,claimDetailVO.getModeTypeID());
			cStmtObject.setString(35,preauthDetailVO.getDoctorName());
			cStmtObject.setString(36,claimDetailVO.getInPatientNbr());
			cStmtObject.setString(37,claimDetailVO.getClaimRemarks());

			if(claimantVO.getAvailSumInsured() != null){
				cStmtObject.setBigDecimal(38,claimantVO.getAvailSumInsured());
			}//end of if(claimantVO.getAvailSumInsured() != null)
			else{
				cStmtObject.setString(38,null);
			}//end of else

			if(claimantVO.getCumulativeBonus() != null){
				cStmtObject.setBigDecimal(39,claimantVO.getCumulativeBonus());
			}//end of if(claimantVO.getCumulativeBonus() != null)
			else{
				cStmtObject.setString(39,null);
			}//end of else

			if(preauthDetailVO.getClmAdmissionDate() != null){
				cStmtObject.setTimestamp(40,new Timestamp(TTKCommon.getOracleDateWithTime(preauthDetailVO.getClmHospAdmissionDate(),preauthDetailVO.getClmAdmissionTime(),preauthDetailVO.getAdmissionDay()).getTime()));
			}//end of if(preAuthDetailVO.getAdmissionDate() != null)
			else{
				cStmtObject.setTimestamp(40, null);
			}//end of else

			if(preauthDetailVO.getDischargeDate() != null){
				//cStmtObject.setTimestamp(41,new Timestamp(TTKCommon.getOracleDateWithTime(preauthDetailVO.getClaimDischargeDate(),preauthDetailVO.getDischargeTime(),preauthDetailVO.getDischargeDay()).getTime()));
			}//end of if(preAuthDetailVO.getDischargeDate() != null)
			else{
				cStmtObject.setTimestamp(41, null);
			}//end of else

			if(preauthDetailVO.getClmParentSeqID() != null){
				cStmtObject.setLong(42,preauthDetailVO.getClmParentSeqID());
			}//end of if(preauthDetailVO.getClmParentSeqID() != null)
			else{
				cStmtObject.setString(42,null);
			}//end of else


			if(preauthDetailVO.getHospAssocSeqID() != null){
				cStmtObject.setLong(43,preauthDetailVO.getHospAssocSeqID());
			}//end of if(preauthDetailVO.getHospAssocSeqID() != null)
			else{
				cStmtObject.setString(43,null);
			}//end of else

			if(preauthHospitalVO.getHospSeqId() != null){
				cStmtObject.setLong(44,preauthHospitalVO.getHospSeqId());
			}//end of if(preauthHospitalVO.getHospSeqId() != null)
			else{
				cStmtObject.setString(44,null);
			}//end of else

			cStmtObject.setString(45,preauthHospitalVO.getEmplNumber());
			cStmtObject.setString(46,preauthHospitalVO.getHospitalName());
			cStmtObject.setString(47,preauthHospitalVO.getAddress1());
			cStmtObject.setString(48,preauthHospitalVO.getAddress2());
			cStmtObject.setString(49,preauthHospitalVO.getAddress3());
			cStmtObject.setString(50,preauthHospitalVO.getStateName());
			cStmtObject.setString(51,preauthHospitalVO.getCityDesc());
			cStmtObject.setString(52,preauthHospitalVO.getPincode());
			cStmtObject.setString(53,preauthHospitalVO.getPhoneNbr1());
			cStmtObject.setString(54,preauthHospitalVO.getPhoneNbr2());
			cStmtObject.setString(55,preauthHospitalVO.getFaxNbr());
			cStmtObject.setString(56,preauthHospitalVO.getHospRemarks());
			cStmtObject.setString(57, preauthHospitalVO.getHospServiceTaxRegnNbr());
			if(preauthDetailVO.getPrevHospClaimSeqID() != null){
				cStmtObject.setLong(58,preauthDetailVO.getPrevHospClaimSeqID());
			}//end of preauthDetailVO.getPrevHospClaimSeqID()
			else{
				cStmtObject.setString(58,null);
			}//end of else

			cStmtObject.setString(59,preauthDetailVO.getDMSRefID());

			if(preauthDetailVO.getApprovedAmt() != null){
				cStmtObject.setBigDecimal(60,preauthDetailVO.getApprovedAmt());
			}//end of if(preauthDetailVO.getApprovedAmt() != null)
			else{
				cStmtObject.setString(60,null);
			}//end of else

			cStmtObject.setString(61,claimDetailVO.getReopenTypeID());
			cStmtObject.setString(62,claimDetailVO.getDoctorRegnNbr());
			cStmtObject.setString(63,claimantVO.getEmailID());
			cStmtObject.setString(64,claimantVO.getNotifyPhoneNbr());
			cStmtObject.setString(65,claimantVO.getInsScheme());
			cStmtObject.setString(66,claimantVO.getCertificateNo());
			cStmtObject.setString(67,claimantVO.getInsCustCode());
			//added for koc insurance reference No
			log.info("REFER--------"+claimantVO.getInsuranceRefNo());
			cStmtObject.setString(68,claimantVO.getInsuranceRefNo());
			//OPD_4_hosptial
			cStmtObject.setString(69,claimDetailVO.getPaymentType());
			//OPD_4_hosptial
			cStmtObject.setLong(70,preauthDetailVO.getUpdatedBy());

			cStmtObject.registerOutParameter(71,Types.INTEGER);
			cStmtObject.registerOutParameter(2,Types.BIGINT);
			cStmtObject.execute();
			lngClaimSeqID = cStmtObject.getLong(2);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveClaimDetail()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveClaimDetail()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return lngClaimSeqID;
	}//end of saveClaimDetail(PreAuthDetailVO preauthDetailVO)

	/**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public long saveClaimDetails(PreAuthDetailVO preAuthDetailVO) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		long lngClaimSeqID = 0;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveClaimDetails);
			if(preAuthDetailVO.getClaimSeqID()==null)cStmtObject.setString(1,null);
			else cStmtObject.setLong(1,preAuthDetailVO.getClaimSeqID());

			if(preAuthDetailVO.getBatchSeqID()==null)cStmtObject.setString(2,null);
			else cStmtObject.setLong(2,preAuthDetailVO.getBatchSeqID());

			if(preAuthDetailVO.getPreAuthSeqID()==null)cStmtObject.setString(3,null);
			else cStmtObject.setLong(3,preAuthDetailVO.getPreAuthSeqID());

			if(preAuthDetailVO.getParentClaimSeqID()==null)cStmtObject.setString(4,null);
			else cStmtObject.setLong(4,preAuthDetailVO.getParentClaimSeqID());
			cStmtObject.setString(5,preAuthDetailVO.getClaimNo());

			cStmtObject.setString(6,null);

			cStmtObject.setString(7,preAuthDetailVO.getClaimSettelmentNo());

			cStmtObject.setTimestamp(8,new java.sql.Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getReceiveDate(),preAuthDetailVO.getReceiveTime(),preAuthDetailVO.getReceiveDay()).getTime()));
			cStmtObject.setString(9,preAuthDetailVO.getModeOfClaim());

			cStmtObject.setTimestamp(10,new Timestamp(TTKCommon.getOracleDateWithTime(new SimpleDateFormat("dd/MM/yyyy").format(preAuthDetailVO.getAdmissionDate()),preAuthDetailVO.getAdmissionTime(),preAuthDetailVO.getAdmissionDay()).getTime()));

			if(preAuthDetailVO.getDischargeDate()!=null)cStmtObject.setTimestamp(11,new Timestamp(TTKCommon.getOracleDateWithTime(preAuthDetailVO.getDischargeDate(),preAuthDetailVO.getDischargeTime(),preAuthDetailVO.getDischargeDay()).getTime()));
		    else   cStmtObject.setDate(11,null);
		   cStmtObject.setString(12,preAuthDetailVO.getClaimType());

		   cStmtObject.setString(13,null);

			if(preAuthDetailVO.getMemberSeqID()==null)cStmtObject.setString(14,null);
			else cStmtObject.setLong(14,preAuthDetailVO.getMemberSeqID());

		   cStmtObject.setString(15,preAuthDetailVO.getMemberId());

		   cStmtObject.setString(16,preAuthDetailVO.getPatientName());

		   if(preAuthDetailVO.getMemberAge()==null)cStmtObject.setString(17,null);
			else cStmtObject.setInt(17,preAuthDetailVO.getMemberAge());

		   if(preAuthDetailVO.getInsSeqId()==null)cStmtObject.setString(18,null);
			else cStmtObject.setLong(18,preAuthDetailVO.getInsSeqId());

		   if(preAuthDetailVO.getPolicySeqId()==null)cStmtObject.setString(19,null);
		   else cStmtObject.setLong(19,preAuthDetailVO.getPolicySeqId());

		   cStmtObject.setString(20,null);

		   cStmtObject.setString(21,preAuthDetailVO.getEmirateId());

		   cStmtObject.setString(22,preAuthDetailVO.getEncounterTypeId());

		   cStmtObject.setString(23,preAuthDetailVO.getEncounterStartTypeId());

		   cStmtObject.setString(24,preAuthDetailVO.getEncounterEndTypeId());

		   cStmtObject.setString(25,preAuthDetailVO.getEncounterFacilityId());

		   cStmtObject.setString(26,preAuthDetailVO.getPayerId());

		   cStmtObject.setBigDecimal(27,preAuthDetailVO.getAvailableSumInsured());

		   cStmtObject.setString(28,preAuthDetailVO.getRequestedAmountCurrency());

		   cStmtObject.setString(29,null);

		   cStmtObject.setString(30,preAuthDetailVO.getRemarks());

		   cStmtObject.setString(31,preAuthDetailVO.getInvoiceNo());

		   cStmtObject.setBigDecimal(32,preAuthDetailVO.getRequestedAmount());

		   cStmtObject.setString(33,preAuthDetailVO.getClinicianId());

		   cStmtObject.setString(34,preAuthDetailVO.getSystemOfMedicine());

		   cStmtObject.setString(35,preAuthDetailVO.getAccidentRelatedCase());

		   cStmtObject.setString(36,preAuthDetailVO.getPriorityTypeID());

		   cStmtObject.setString(37,preAuthDetailVO.getNetworkProviderType());

		   cStmtObject.setString(38,preAuthDetailVO.getBenefitType());

		     int gravida=preAuthDetailVO.getGravida()==null?0:preAuthDetailVO.getGravida();
		   int para=preAuthDetailVO.getPara()==null?0:preAuthDetailVO.getPara();
		   int live=preAuthDetailVO.getLive()==null?0:preAuthDetailVO.getLive();
		   int abortion=preAuthDetailVO.getAbortion()==null?0:preAuthDetailVO.getAbortion();
		   cStmtObject.setInt(39,gravida);
		   cStmtObject.setInt(40,para);
		   cStmtObject.setInt(41,live);
           cStmtObject.setInt(42,abortion);


		   /*
		   if(preAuthDetailVO.getGravida()==null)cStmtObject.setString(39,null);
			else cStmtObject.setInt(39,preAuthDetailVO.getGravida());

		   if(preAuthDetailVO.getPara()==null)cStmtObject.setString(40,null);
		   else cStmtObject.setInt(40,preAuthDetailVO.getPara());

		   if(preAuthDetailVO.getLive()==null)cStmtObject.setString(41,null);
		   else cStmtObject.setInt(41,preAuthDetailVO.getLive());

		   if(preAuthDetailVO.getAbortion()==null)cStmtObject.setString(42,null);
		   else cStmtObject.setInt(42,preAuthDetailVO.getAbortion());
		   */
		   cStmtObject.setString(43,preAuthDetailVO.getPresentingComplaints());

		   cStmtObject.setString(44,preAuthDetailVO.getMedicalOpinionRemarks());

		   if(preAuthDetailVO.getProviderSeqId()==null)cStmtObject.setString(45,null);
		   else cStmtObject.setLong(45,preAuthDetailVO.getProviderSeqId());

		   cStmtObject.setString(46,preAuthDetailVO.getProviderName());

		   cStmtObject.setString(47,preAuthDetailVO.getProviderAddress());

		   cStmtObject.setString(48,preAuthDetailVO.getProviderArea());

		   cStmtObject.setString(49,preAuthDetailVO.getProviderEmirate());

		   cStmtObject.setString(50,preAuthDetailVO.getProviderPobox());

		   cStmtObject.setString(51,preAuthDetailVO.getProviderOfficePhNo());

		   cStmtObject.setString(52,preAuthDetailVO.getProviderOfficeFaxNo());

		   cStmtObject.setString(53,preAuthDetailVO.getProviderId());

		   cStmtObject.setString(54,preAuthDetailVO.getProviderCountry());
		   cStmtObject.setString(55,preAuthDetailVO.getClinicianName());

		   cStmtObject.setLong(56,preAuthDetailVO.getAddedBy()==null?0:preAuthDetailVO.getAddedBy());
		   
		  
		   //cStmtObject.setString(57,preAuthDetailVO.getProvAuthority());
		   cStmtObject.setString(57,preAuthDetailVO.getEnablericopar());
		   cStmtObject.setString(58,preAuthDetailVO.getEnableucr());
		    cStmtObject.setString(59,preAuthDetailVO.getTakafulClaimRefNo());
		   
		   if(preAuthDetailVO.getNatureOfConception() != null){ 
		   cStmtObject.setString(60,preAuthDetailVO.getNatureOfConception());
		   }else{
			   cStmtObject.setString(60, ""); 
		   }
			if(preAuthDetailVO.getLatMenstraulaPeriod() != null){
				cStmtObject.setTimestamp(61,new Timestamp(preAuthDetailVO.getLatMenstraulaPeriod().getTime()));
            }else{
				cStmtObject.setTimestamp(61, null);
			}
			
			   cStmtObject.setString(62,preAuthDetailVO.getRequestedAmountcurrencyType());
			   cStmtObject.setBigDecimal(63,preAuthDetailVO.getConvertedAmount());
			   cStmtObject.setString(64,preAuthDetailVO.getCurrencyType());
			   cStmtObject.setString(65,preAuthDetailVO.getConversionRate());
			   cStmtObject.setString(66,preAuthDetailVO.getProcessType());
			   cStmtObject.setString(67,preAuthDetailVO.getTpaReferenceNo());
			  cStmtObject.setString(68,preAuthDetailVO.getTreatmentTypeID());
			   cStmtObject.setString(69,preAuthDetailVO.getModeofdelivery());
			   cStmtObject.setString(70,preAuthDetailVO.getEventReferenceNo());
			   cStmtObject.setString(71,preAuthDetailVO.getPaymentToEmb());
			   cStmtObject.setString(72,preAuthDetailVO.getBankName());
			   cStmtObject.setString(73,preAuthDetailVO.getAccountNumber());
			   cStmtObject.setString(74,preAuthDetailVO.getPaymentMadeFor());
			   cStmtObject.setString(75,preAuthDetailVO.getMatsubbenefit());
				 cStmtObject.setString(76,preAuthDetailVO.getClinicianSpeciality());
			   cStmtObject.setString(77,preAuthDetailVO.getPaymentToLayOver());
			   
			   if("YES".equals(preAuthDetailVO.getExceptionFalg()))
				   cStmtObject.setString(78,preAuthDetailVO.getExceptionFalg());
			   else
				   cStmtObject.setString(78,"NO");
			  
			   cStmtObject.registerOutParameter(1,Types.BIGINT);
			   cStmtObject.registerOutParameter(79,Types.INTEGER);
		       cStmtObject.execute();
			lngClaimSeqID = cStmtObject.getLong(1);
			int intRoePro = cStmtObject.getInt(79);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveClaimDetails()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveClaimDetails()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return lngClaimSeqID;
	}//end of saveClaimDetails(PreAuthDetailVO preauthDetailVO)

	/**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public Object[] calculateClaimAmount(Long claimSeqId,Long hospitalSeqId,Long addedBy) throws TTKException {
	
		Connection conn = null;
		CallableStatement cStmtObject=null;
		Object[] result=new Object[1];

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCalculateClaimAmounts);

			cStmtObject.setLong(1,claimSeqId);
			cStmtObject.setLong(2,hospitalSeqId);
		   cStmtObject.registerOutParameter(3,Types.DECIMAL);
		   cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
		   cStmtObject.setLong(5,addedBy);
			
		   cStmtObject.execute();
		   result[0]=cStmtObject.getBigDecimal(3);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl calculateClaimAmount()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl calculateClaimAmount()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return result;
	}//end of calculateClaimAmount(PreAuthDetailVO preauthDetailVO)
	/**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public Object[] saveAndCompleteClaim(PreAuthDetailVO  preAuthDetailVO) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		Object[] result=new Object[1];

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimAmountApproved);

			cStmtObject.setLong(1,preAuthDetailVO.getClaimSeqID());
			if(preAuthDetailVO.getMemberSeqID()==null)cStmtObject.setString(2,null);
			else cStmtObject.setLong(2,preAuthDetailVO.getMemberSeqID());
			cStmtObject.setString(3,preAuthDetailVO.getClaimSettelmentNo());
			cStmtObject.setTimestamp(4,new Timestamp(TTKCommon.getOracleDateWithTime(new SimpleDateFormat("dd/MM/yyyy").format(preAuthDetailVO.getAdmissionDate()),preAuthDetailVO.getAdmissionTime(),preAuthDetailVO.getAdmissionDay()).getTime()));
			cStmtObject.setBigDecimal(5, preAuthDetailVO.getApprovedAmount());
			cStmtObject.setString(6,preAuthDetailVO.getPreAuthRecvTypeID());
			cStmtObject.setString(7,preAuthDetailVO.getClaimStatus());
			cStmtObject.setString(8,preAuthDetailVO.getMedicalOpinionRemarks());
			cStmtObject.setLong(9,preAuthDetailVO.getAddedBy());
			cStmtObject.setString(10, preAuthDetailVO.getCurrencyType());
			cStmtObject.setString(11, preAuthDetailVO.getFinalRemarks());
			cStmtObject.setString(12, preAuthDetailVO.getInternalRemarks());
			cStmtObject.registerOutParameter(13,Types.BIGINT);
			cStmtObject.setString(14, preAuthDetailVO.getRecheckRemarks());
			cStmtObject.execute();
			result[0]=cStmtObject.getLong(13);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveAndCompleteClaim()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveAndCompleteClaim()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return result;
	}//end of saveAndCompleteClaim(PreAuthDetailVO preauthDetailVO)

	/**
	 * This method returns the Arraylist of PreAuthDetailVO's which contains Preauthorization Details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PreAuthDetailVO object which contains Preauthorization details
	 * @exception throws TTKException
	 */
	public ArrayList getPreauthList(ArrayList alSearchCriteria) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthDetailVO preauthDetailVO=null;
		Collection<Object> alResultList = new ArrayList<Object>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetPreauthList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			if((String)alSearchCriteria.get(2) == null || (String)alSearchCriteria.get(2) == ""){
				cStmtObject.setString(3,(String)alSearchCriteria.get(8));

			}else{

			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			}
			
			
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7,(String)alSearchCriteria.get(9));
			cStmtObject.setString(8,(String)alSearchCriteria.get(10));
			cStmtObject.setString(9,(String)alSearchCriteria.get(11));
			cStmtObject.setString(10,(String)alSearchCriteria.get(12));
			cStmtObject.setLong(11,(Long)alSearchCriteria.get(6));
			cStmtObject.setString(13,(String)alSearchCriteria.get(7));
			
			cStmtObject.registerOutParameter(12,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(12);
			String dateFormat="dd/MM/yyyy/ hh:mm";

			if(rs != null){
				while(rs.next()){
					preauthDetailVO = new PreAuthDetailVO();

					if(rs.getString("PAT_AUTH_SEQ_ID") != null){
						preauthDetailVO.setPreAuthSeqID(new Long(rs.getLong("PAT_AUTH_SEQ_ID")));
					}//end of if(rs.getString("PAT_AUTH_SEQ_ID") != null)

					preauthDetailVO.setAuthNum(TTKCommon.checkNull(rs.getString("AUTH_NUMBER")));
					preauthDetailVO.setHospitalName(TTKCommon.checkNull(rs.getString("HOSP_NAME")));
					preauthDetailVO.setClaimantName(TTKCommon.checkNull(rs.getString("MEM_NAME")));

					if(rs.getString("HOSPITALIZATION_DATE") != null){
						preauthDetailVO.setClmAdmissionTime(TTKCommon.convertDateAsString(dateFormat, rs.getDate("HOSPITALIZATION_DATE")));
					}//end of if(rs.getString("DATE_OF_HOSPITALIZATION") != null)

					if(rs.getString("PAT_RECEIVED_DATE") != null){
						preauthDetailVO.setReceivedDateAsString(TTKCommon.convertDateAsString(dateFormat, rs.getDate("PAT_RECEIVED_DATE")));
					}//end of if(rs.getString("PAT_RECEIVED_DATE") != null)

					if(rs.getString("TOT_APPROVED_AMOUNT") != null){
						preauthDetailVO.setApprovedAmt(new BigDecimal(rs.getString("TOT_APPROVED_AMOUNT")));
					}//end of if(rs.getString("TOTAL_APP_AMOUNT") != null)
					
					preauthDetailVO.setPatReqCurr(TTKCommon.checkNull(rs.getString("PAT_REQ_CURR")));
					preauthDetailVO.setPatIncAmnt(TTKCommon.checkNull(rs.getString("PAT_INC_AMNT")));
					preauthDetailVO.setPatIncCurr(TTKCommon.checkNull(rs.getString("PAT_INC_CURR")));

					alResultList.add(preauthDetailVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getPreauthList()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getPreauthList()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getPreauthList()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPreauthList(ArrayList alSearchCriteria)

	/**
	 * This method returns the Arraylist of Cache Object's which contains Previous Claim Seq ID's
	 * @param lngMemberSeqID Member Seq ID
	 * @return ArrayList of Cache Object's Previous Claim Seq ID's
	 * @exception throws TTKException
	 */
	public ArrayList getPrevClaim(long lngMemberSeqID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ArrayList<Object> alPrevClaimList = new ArrayList<Object>();
		CacheObject cacheObject = null;
		try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPrevClaimList);
			cStmtObject.setLong(1,lngMemberSeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					cacheObject = new CacheObject();
					cacheObject.setCacheId(TTKCommon.checkNull(rs.getString("CLAIM_SEQ_ID")));
					cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					alPrevClaimList.add(cacheObject);
				}//end of while(rs.next())
			}//end of if(rs != null)
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getPrevClaim()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getPrevClaim()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getPrevClaim()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return alPrevClaimList;
	}//end of getPrevClaim(long lngMemberSeqID)

	/**
	 * This method saves the Review information
	 * @param preauthDetailVO the object which contains the Review Details which has to be  saved
	 * @param strMode String object which contains Mode PAT
	 * @param strType String object which contains Type
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
	public PreAuthDetailVO saveReview(PreAuthDetailVO preauthDetailVO,String strMode,String strType) throws TTKException{
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
			if(preauthDetailVO.getClaimSeqID() != null){
				cStmtObject.setLong(1,preauthDetailVO.getClaimSeqID());
			}//end of if(preauthDetailVO.getClaimSeqID() != null)
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
			throw new TTKException(sqlExp, "claim");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "claim");
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
					log.error("Error while closing the Statement in ClaimDAOImpl saveReview()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveReview()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
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
	 * This method returns the HashMap contains ArrayList of HospitalizationVO's which contains Previous Hospitalization Details
	 * @param lngMemberSeqID long value contains Member Seq ID
	 * @param strMode contains CTM - > MR / CNH -> NHCP
	 * @param lngClaimSeqID long value contains Claim Seq ID
	 * @return ArrayList of CacheVO object which contains Previous Hospitalization details
	 * @exception throws TTKException
	 */
	public HashMap getPrevHospList(long lngMemberSeqID,String strMode,long lngClaimSeqID) throws TTKException {
		Connection conn1 = null;
		CallableStatement cStmt = null;
		CallableStatement cStmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		HashMap<Object,Object> hmPrevHospList = null;
		ArrayList<Object> alPrevHospList = null;
		long lngPrevHospClaimSeqID = 0;
		HospitalizationDetailVO hospitalizationDetailVO = null;
		PreAuthHospitalVO preauthHospitalVO = null;
		try{

			conn1 = ResourceManager.getConnection();
			cStmt = (java.sql.CallableStatement)conn1.prepareCall(strPrevHospList);
			cStmt1 = (java.sql.CallableStatement)conn1.prepareCall(strPrevHospDetails);

			cStmt.setString(1,strMode);
			cStmt.setLong(2,lngMemberSeqID);
			cStmt.registerOutParameter(3,OracleTypes.CURSOR);
			cStmt.setLong(4,lngClaimSeqID);
			cStmt.execute();
			rs = (java.sql.ResultSet)cStmt.getObject(3);
			if(rs != null){
				while(rs.next()){

					if(hmPrevHospList == null){
						hmPrevHospList=new HashMap<Object,Object>();
					}//end of if(hmPrevHospList == null)
						lngPrevHospClaimSeqID = rs.getLong("CLAIM_SEQ_ID");
					cStmt1.setLong(1,lngPrevHospClaimSeqID);
					cStmt1.registerOutParameter(2,OracleTypes.CURSOR);
					cStmt1.execute();
					rs1 = (java.sql.ResultSet)cStmt1.getObject(2);
					alPrevHospList = null;
					if(rs1 != null){
						while(rs1.next()){
							hospitalizationDetailVO = new HospitalizationDetailVO();
							preauthHospitalVO = new PreAuthHospitalVO();

							if(alPrevHospList == null){
								alPrevHospList = new ArrayList<Object>();
							}//end of if(alPrevHospList == null)

								if(rs1.getString("CLAIM_SEQ_ID") != null){
									hospitalizationDetailVO.setPrevHospClaimSeqID(new Long(rs1.getLong("CLAIM_SEQ_ID")));
								}//end of if(rs1.getString("CLAIM_SEQ_ID") != null)

								hospitalizationDetailVO.setPrevHospDesc(TTKCommon.checkNull(rs1.getString("PREV_HOSP")));

								if(rs1.getString("DATE_OF_ADMISSION") != null){
									hospitalizationDetailVO.setAdmissionDate(new Date(rs1.getTimestamp("DATE_OF_ADMISSION").getTime()));
									hospitalizationDetailVO.setAdmissionTime(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ")[1]:"");
									hospitalizationDetailVO.setAdmissionDay(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_ADMISSION").getTime())).split(" ")[2]:"");
								}//end of if(rs.getString("DATE_OF_ADMISSION") != null)

								if(rs1.getString("DATE_OF_DISCHARGE") != null){
									hospitalizationDetailVO.setDischargeDate(new Date(rs1.getTimestamp("DATE_OF_DISCHARGE").getTime()));
									hospitalizationDetailVO.setDischargeTime(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ")[1]:"");
									hospitalizationDetailVO.setDischargeDay(TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs1.getTimestamp("DATE_OF_DISCHARGE").getTime())).split(" ")[2]:"");
								}//end of if(rs.getString("DATE_OF_DISCHARGE") != null)

								if(rs1.getString("HOSP_SEQ_ID") != null){
									preauthHospitalVO.setHospSeqId(new Long(rs1.getLong("HOSP_SEQ_ID")));
								}//end of if(rs1.getString("HOSP_SEQ_ID") != null)

								preauthHospitalVO.setHospitalName(TTKCommon.checkNull(rs1.getString("HOSP_NAME")));
								preauthHospitalVO.setEmplNumber(TTKCommon.checkNull(rs1.getString("EMPANEL_NUMBER")));
								preauthHospitalVO.setAddress1(TTKCommon.checkNull(rs1.getString("ADDRESS_1")));
								preauthHospitalVO.setAddress2(TTKCommon.checkNull(rs1.getString("ADDRESS_2")));
								preauthHospitalVO.setAddress3(TTKCommon.checkNull(rs1.getString("ADDRESS_3")));
								preauthHospitalVO.setCityDesc(TTKCommon.checkNull(rs1.getString("CITY_DESCRIPTION")));
								preauthHospitalVO.setStateName(TTKCommon.checkNull(rs1.getString("STATE_NAME")));
								preauthHospitalVO.setPincode(TTKCommon.checkNull(rs1.getString("PIN_CODE")));
								preauthHospitalVO.setEmailID(TTKCommon.checkNull(rs1.getString("PRIMARY_EMAIL_ID")));
								preauthHospitalVO.setPhoneNbr1(TTKCommon.checkNull(rs1.getString("OFF_PHONE_NO_1")));
								preauthHospitalVO.setPhoneNbr2(TTKCommon.checkNull(rs1.getString("OFF_PHONE_NO_2")));
								preauthHospitalVO.setFaxNbr(TTKCommon.checkNull(rs1.getString("OFFICE_FAX_NO")));
								preauthHospitalVO.setHospStatus(TTKCommon.checkNull(rs1.getString("EMPANEL_DESCRIPTION")));
								preauthHospitalVO.setRating(TTKCommon.checkNull(rs1.getString("RATING")));

								if(preauthHospitalVO.getRating() != null){
									if(preauthHospitalVO.getRating().equals("G")){
										preauthHospitalVO.setRatingImageName("GoldStar");
										preauthHospitalVO.setRatingImageTitle("Gold Star");
									}//end of if(preAuthHospitalVO.getRating().equals("G"))

									if(preauthHospitalVO.getRating().equals("R")){
										preauthHospitalVO.setRatingImageName("BlueStar");
										preauthHospitalVO.setRatingImageTitle("Blue Star (Regular)");
									}//end of if(preAuthHospitalVO.getRating().equals("R"))

									if(preauthHospitalVO.getRating().equals("B")){
										preauthHospitalVO.setRatingImageName("BlackStar");
										preauthHospitalVO.setRatingImageTitle("Black Star");
									}//end of if(preAuthHospitalVO.getRating().equals("B"))
								}//end of if(preAuthHospitalVO.getRating() != null)

								hospitalizationDetailVO.setPreauthHospitalVO(preauthHospitalVO);
								hmPrevHospList.put(lngPrevHospClaimSeqID,hospitalizationDetailVO);
						}//end of inner while(rs1.next())
					}//end of inner if(rs1 != null)
					if (rs1 != null){
						rs1.close();
					}//end of if (rs1 != null)
					rs1 = null;
				}//end of outer while(rs.next())
			}//end of outer if(rs != null)
			if (cStmt1 != null){
				cStmt1.close();
			}//end of if (cStmt1 != null)
			cStmt1 = null;
			return hmPrevHospList;
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
					if (rs1 != null) rs1.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Second Resultset in ClaimDAOImpl getPrevHospList()",sqlExp);
					throw new TTKException(sqlExp, "claim");
				}//end of catch (SQLException sqlExp)
				finally // Even if second result set is not closed, control reaches here. Try closing the first resultset now.
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the First Resultset in ClaimDAOImpl getPrevHospList()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
					finally // Even if First Resultset is not closed, control reaches here. Try closing the Second Statement now.
					{
						try
						{
							if(cStmt1 != null) cStmt1.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Second Statement in ClaimDAOImpl getPrevHospList()",sqlExp);
							throw new TTKException(sqlExp, "claim");
						}//end of catch (SQLException sqlExp)
						finally // Even if Second Statement is not closed, control reaches here. Try closing the First Statement now.
						{
							try
							{
								if(cStmt != null) cStmt.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the FirstStatement in ClaimDAOImpl getPrevHospList()",sqlExp);
								throw new TTKException(sqlExp, "claim");
							}//end of catch (SQLException sqlExp)
							finally // Even if First Statement is not closed, control reaches here. Try closing the Connection now.
							{
								try{
									if(conn1 != null) conn1.close();
								}//end of try
								catch (SQLException sqlExp)
								{
									log.error("Error while closing the Connection in ClaimDAOImpl getPrevHospList()",sqlExp);
									throw new TTKException(sqlExp, "claim");
								}//end of catch (SQLException sqlExp)
							}//end of finally
						}//end of finally
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs1 = null;
				rs = null;
				cStmt1 = null;
				cStmt = null;
				conn1 = null;
			}//end of finally
		}//end of finally
	}//end of getPrevHospList(long lngMemberSeqID,String strMode)

	/**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteClaimGeneral(ArrayList alDeleteList) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteClaimGeneral);
			cStmtObject.setString(1, (String)alDeleteList.get(0));//FLAG PAT/AIL/SFL/INV/PED/COU/BUFFER
			cStmtObject.setString(2, (String)alDeleteList.get(1));//CONCATENATED STRING OF  delete SEQ_IDS

			if(alDeleteList.get(2) != null){
				cStmtObject.setLong(3,(Long)alDeleteList.get(2));//Mandatory  CLM_ENROLL_DETAIL_SEQ_ID
			}//end of if(alDeleteList.get(2) != null)
			else{
				cStmtObject.setString(3,null);
			}//end of else

			if(alDeleteList.get(3) != null){
				cStmtObject.setLong(4,(Long)alDeleteList.get(3));//Mandatory CLAIM_SEQ_ID
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
			throw new TTKException(sqlExp, "claim");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "claim");
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
					log.error("Error while closing the Statement in ClaimDAOImpl deleteClaimGeneral()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl deleteClaimGeneral()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteClaimGeneral(ArrayList alDeleteList)

	/**
	 * This method returns the ArrayList object, which contains all the Users for the Corresponding TTK Branch
	 * @param alAssignUserList ArrayList Object contains ClaimSeqID,PolicySeqID and TTKBranch
	 * @return ArrayList object which contains all the Users for the Corresponding TTK Branch
	 * @exception throws TTKException
	 */
	public ArrayList getAssignUserList(ArrayList alAssignUserList) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ArrayList<Object> alUserList = new ArrayList<Object>();
		CacheObject cacheObject = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAssignUserList);
			cStmtObject.setLong(1,(Long)alAssignUserList.get(0));//Mandatory
			if(alAssignUserList.get(1) != null){
				cStmtObject.setLong(2,(Long)alAssignUserList.get(1));
			}//end of if(alAssignUserList.get(1) != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			cStmtObject.setLong(3,(Long)alAssignUserList.get(2));//Mandatory
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(4);
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getPrevClaim()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getAssignUserList()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getAssignUserList()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getAssignUserList(ArrayList alAssignUserList)

	/**
	 * This method returns the ClauseVO, which contains all the Clause details
	 * @param alClauseList contains Claim seq id,Policy Seq ID,EnrolTypeID,AdmissionDate to get the Clause Details
	 * @return ClauseVO object which contains all the Clause details
	 * @exception throws TTKException
	 */
	public ClauseVO getClauseDetail(ArrayList alClauseList) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ClauseVO clauseVO = new ClauseVO();
		ArrayList<Object> alClauseDetail = new ArrayList<Object>();
		PolicyClauseVO policyClauseVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClauseDetail);

			if(alClauseList.get(0) != null){
				cStmtObject.setLong(1,(Long)alClauseList.get(0));
			}//end of if(alClauseList.get(0) != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else
			if(alClauseList.get(1) != null){
				cStmtObject.setLong(2,(Long)alClauseList.get(1));
			}//end of if(alClauseList.get(1) != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else
			cStmtObject.setLong(3,(Long)alClauseList.get(2));
			cStmtObject.setString(4,(String)alClauseList.get(3));
			//cStmtObject.setString(4,(String)alClauseList.get(3));
			if(alClauseList.get(4) != null && alClauseList.get(4) != ""){

				cStmtObject.setTimestamp(5,new Timestamp(TTKCommon.getUtilDate(alClauseList.get(4).toString()).getTime()));
			}//end of if(alClauseList.get(4) != null)
			else{
				cStmtObject.setString(5,null);
			}//end of else

			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(6);

			if(alClauseList.get(0) != null){
				rs2 = (java.sql.ResultSet)cStmtObject.getObject(7);
			}//end of if(alClauseList.get(0) != null)

			if(rs1 != null){
				while(rs1.next()){
					policyClauseVO = new PolicyClauseVO();

					if(rs1.getString("CLAUSE_SEQ_ID") != null){
						policyClauseVO.setClauseSeqID(new Long(rs1.getLong("CLAUSE_SEQ_ID")));
					}//end of if(rs1.getString("CLAUSE_SEQ_ID") != null)

					policyClauseVO.setClauseNbr(TTKCommon.checkNull(rs1.getString("CLAUSE_NUMBER")));
					policyClauseVO.setClauseDesc(TTKCommon.checkNull(rs1.getString("CLAUSE_DESCRIPTION")));
					policyClauseVO.setSelectedYN(TTKCommon.checkNull(rs1.getString("SELECTED_YN")));

					if(rs1.getString("PROD_POLICY_SEQ_ID") != null){
						policyClauseVO.setProdPolicySeqID(new Long(rs1.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs1.getString("PROD_POLICY_RULE_SEQ_ID") != null)

					alClauseDetail.add(policyClauseVO);
				}//end of while(rs1.next())
				clauseVO.setPolicyClauseVO(alClauseDetail);
			}//end of if(rs1 != null)

			if(rs2 != null){
				while(rs2.next()){
					clauseVO.setRejHeaderInfo(TTKCommon.checkNull(rs2.getString("REJ_HEADER_INFO")));
					clauseVO.setRejFooterInfo(TTKCommon.checkNull(rs2.getString("REJ_FOOTER_INFO")));
					clauseVO.setLetterTypeID(TTKCommon.checkNull(rs2.getString("LTR_GENERAL_TYPE_ID")));
					clauseVO.setClaimTypeID(TTKCommon.checkNull(rs2.getString("CLAIM_GENERAL_TYPE_ID")));
					if(rs2.getString("INS_SEQ_ID") != null){
						clauseVO.setInsSeqID(new Long(rs2.getLong("INS_SEQ_ID")));
					}//end of if(rs2.getString("INS_SEQ_ID") != null)
				}//end of while(rs2.next())
			}//end of if(rs2 != null)
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
					if (rs2 != null) rs2.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClauseDetail()",sqlExp);
					throw new TTKException(sqlExp, "claim");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the second resultset now.
				{
					try{
						if (rs1 != null) rs1.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimDAOImpl getClauseDetail()",sqlExp);
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
							log.error("Error while closing the Statement in ClaimDAOImpl getClauseDetail()",sqlExp);
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
								log.error("Error while closing the Connection in ClaimDAOImpl getClauseDetail()",sqlExp);
								throw new TTKException(sqlExp, "claim");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of finally
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs2 = null;
				rs1 = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return clauseVO;
	}//end of getClauseDetail(ArrayList alClauseList)

	/**
	 * This method saves the Clause Details
	 * @param alClauseList the arraylist which contains the Clause Details which has to be saved
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public String saveClauseDetail(ArrayList alClauseList) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		String strClauses="";
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveClauseDetail);

			if(alClauseList.get(0) != null){
				cStmtObject.setLong(1,(Long)alClauseList.get(0));
			}//end of if(alClauseList.get(0) != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else
			if(alClauseList.get(1) != null){
				cStmtObject.setLong(2,(Long)alClauseList.get(1));
			}//end of if(alClauseList.get(1) != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else
			cStmtObject.setString(3,(String)alClauseList.get(2));
			cStmtObject.setString(4,(String)alClauseList.get(3));
			cStmtObject.setString(5,(String)alClauseList.get(4));
			cStmtObject.setString(6,(String)alClauseList.get(5));
			cStmtObject.setLong(7,(Long)alClauseList.get(6));
			cStmtObject.registerOutParameter(8, Types.VARCHAR);
			cStmtObject.registerOutParameter(9, Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.execute();
			iResult = cStmtObject.getInt(9);//ROWS_PROCESSED
			log.debug("iResult value is :"+iResult);
			strClauses = cStmtObject.getString(8);//Clauses which are savd
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveClauseDetail()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveClauseDetail()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return strClauses;
	}//end of saveClauseDetail(ArrayList alClauseList)

	/**
	 * This method returns the ArrayList object, which contains list of Letters to be sent for Claims Rejection
	 * @param alLetterList ArrayList Object contains ClaimTypeID and Ins Seq ID
	 * @return ArrayList object which contains list of Letters to be sent for Claims Rejection
	 * @exception throws TTKException
	 */
	public ArrayList getRejectionLetterList(ArrayList alLetterList) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CacheObject cacheObject = null;
		ArrayList<Object> alRejectionLetterList = new ArrayList<Object>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strRejectionLetterList);

			cStmtObject.setString(1,(String)alLetterList.get(0));

			if(alLetterList.get(1) != null && alLetterList.get(0).equals("CTM")){
				cStmtObject.setLong(2,(Long)alLetterList.get(1));
			}//end of if(alLetterList.get(1) != null)
			else{
				cStmtObject.setString(2,null);
			}//end of else

			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			if(rs != null){
				while(rs.next()){
					cacheObject = new CacheObject();

					cacheObject.setCacheId(TTKCommon.checkNull(rs.getString("GENERAL_TYPE_ID")));
					cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					alRejectionLetterList.add(cacheObject);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return alRejectionLetterList;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getRejectionLetterList()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getRejectionLetterList()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getRejectionLetterList()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getRejectionLetterList(ArrayList alLetterList)

	/**
	 * This method reassign the enrollment ID
	 * @param alReAssignEnrID the arraylist of details for which have to be reassigned
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int reAssignEnrID(ArrayList alReAssignEnrID) throws TTKException
	{
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReAssignEnrID);
			cStmtObject.setLong(1,(Long)alReAssignEnrID.get(0));//CLAIM_SEQ_ID
            cStmtObject.setString(2,(String)alReAssignEnrID.get(1));//CLM_STATUS_GENERAL_TYPE_ID
            cStmtObject.setLong(3,(Long)alReAssignEnrID.get(2));//MEMBER_SEQ_ID
			cStmtObject.setLong(4,(Long)alReAssignEnrID.get(3));//ADDED_BY
			cStmtObject.registerOutParameter(5, Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.execute();
			iResult = cStmtObject.getInt(5);//ROWS_PROCESSED
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
					log.error("Error while closing the Statement in ClaimDAOImpl reAssignEnrID()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl reAssignEnrID()",sqlExp);
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
	 * This method Gets the service Tax amount to be paid
	 * @param authorizationVO AuthorizationVO contains Settlement(Authorization) information
	 * @return Object[] the values,of  SERV_TAX_CALC_AMOUNT , ApprovedAmount and SERV_TAX_CALC_PERCENTAGE
	 * @exception throws TTKException
	 */
	public Object[] getServTaxCal(AuthorizationVO authorizationVO) throws TTKException
	{

		Object[] objArrayResult = new Object[3];
		BigDecimal bdApprovedAmount = null;
		BigDecimal bdTaxAmtPaid = null;
		BigDecimal bdSerTaxCalPer = null;
		Connection conn = null;
		CallableStatement cStmtObject=null;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strServTaxCal);
			if(authorizationVO.getClaimSeqID() != null)
			{
				cStmtObject.setLong(1, authorizationVO.getClaimSeqID());
			}//end of if(authorizationVO.getClaimSeqID() != null)
				else
				{
					cStmtObject.setLong(1,0);
				}//end of else
			if(authorizationVO.getCopayAmount() != null)
			{
				cStmtObject.setBigDecimal(2, authorizationVO.getCopayAmount());
			}//end of if(authorizationVO.getCopayAmount() != null)
			else
			{
				cStmtObject.setBigDecimal(2,new BigDecimal("0.00"));
			}//end of else
			if(authorizationVO.getDiscountAmount() != null)
			{
				cStmtObject.setBigDecimal(3,authorizationVO.getDiscountAmount());
            }//end of if(authorizationVO.getDiscountAmount() != null)
			else
			{
				cStmtObject.setBigDecimal(3,new BigDecimal("0.00"));
			}//end of else
			/*if(authorizationVO.getTaxAmtPaid() != null)
            {
            	cStmtObject.setBigDecimal(4,authorizationVO.getTaxAmtPaid());
            }//end of if(authorizationVO.getTaxAmtPaid() != null)
            else
            {
            	cStmtObject.setBigDecimal(4,new BigDecimal("0.00"));
            }//end of else
			 */            cStmtObject.registerOutParameter(4,Types.BIGINT);
			 cStmtObject.registerOutParameter(5,Types.BIGINT);
			 cStmtObject.registerOutParameter(6,Types.BIGINT);
			 cStmtObject.execute();
			 bdTaxAmtPaid = (BigDecimal)cStmtObject.getBigDecimal(4);
			 bdApprovedAmount =(BigDecimal) cStmtObject.getBigDecimal(5);
			 bdSerTaxCalPer =(BigDecimal) cStmtObject.getBigDecimal(6);
			 objArrayResult[0] = (bdTaxAmtPaid);
			 objArrayResult[1] = (bdApprovedAmount);
			 objArrayResult[2] = (bdSerTaxCalPer);

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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in Claim DAOImpl getServTaxCal()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl getServTaxCal()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
            finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
        return objArrayResult;
	}//end of getServTaxCal(AuthorizationVO authorizationVO)
 // Shortfall CR KOC1179
    /**
     * This method returns the long  which contains Claim Shortfall Details
     * @param strSfEmailSeqId,strSfRequestType,strSentBy strAddedBy ArrayList object which contains the search criteria
     * @return long of rows processed object which contains  Claim Shortfall Details
     * @exception throws TTKException
     */
    public long sendShortfallDetails(String strSfEmailSeqId ,String strSfRequestType,String strSentBy,long strAddedBy) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	Collection<Object> alResultList = new ArrayList<Object>();
    	ClaimInwardVO claimInwardVO = null;
    	int recordCount=0;
    	try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSendShortfallRequestList);
			cStmtObject.setString(1,strSfEmailSeqId);
			cStmtObject.setString(2,strSfRequestType);
			cStmtObject.setString(3,strSentBy);
			cStmtObject.setLong(4,strAddedBy);
			cStmtObject.registerOutParameter(4,OracleTypes.INTEGER);
			cStmtObject.registerOutParameter(5,OracleTypes.BIGINT);
			cStmtObject.execute();
			recordCount =cStmtObject.getInt(5);
			return recordCount;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl sendShortfallDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl sendShortfallDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl sendShortfallDetails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of sendShortfallDetails(String strSfEmailSeqId ,String strSfRequestType,String strSentBy,long strAddedBy)


  //Mail-SMS Template for Cigna
    /**
     * This method returns the long  which contains Claim Shortfall Details
     * @param strSfEmailSeqId,strSfRequestType,strSentBy strAddedBy ArrayList object which contains the search criteria
     * @return long of rows processed object which contains  Claim Shortfall Details
     * @exception throws TTKException
     */
    public long sendCignaShortfallDetails(String strSfEmailSeqId ,String strSfRequestType,String strSentBy,long strAddedBy) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	Collection<Object> alResultList = new ArrayList<Object>();
    	ClaimInwardVO claimInwardVO = null;
    	int recordCount=0;
    	try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSendCignaShortfallRequestList);
			cStmtObject.setString(1,strSfEmailSeqId);
			cStmtObject.setString(2,strSfRequestType);
			cStmtObject.setString(3,strSentBy);
			cStmtObject.setLong(4,strAddedBy);
			cStmtObject.registerOutParameter(4,OracleTypes.INTEGER);
			cStmtObject.registerOutParameter(5,OracleTypes.BIGINT);
			cStmtObject.execute();
			recordCount =cStmtObject.getInt(5);
			return recordCount;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl sendCignaShortfallDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl sendCignaShortfallDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl sendCignaShortfallDetails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of sendCignaShortfallDetails(String strSfEmailSeqId ,String strSfRequestType,String strSentBy,long strAddedBy)
    //ended


    // Shortfall CR KOC1179
    /**
     * This method returns the ShortfallRequestDetailsVO of ShortfallRequestDetailsVO'S  which contains Claim Shortfall Details
     * @param alSearchCriteria String object which contains the shortfallSeqID
     * @return ShortfallRequestDetailsVO of ShortfallRequestDetailsVO'S object which contains  Claim Shortfall Details
     * @exception throws TTKException
     */
    public ShortfallRequestDetailsVO generateShortfallRequestDetails(String shortfallSeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	Collection<Object> alResultList = new ArrayList<Object>();

    	int recordCount=0;
    	ShortfallRequestDetailsVO  shortfallRequestDetailsVO=null;
    	try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetShortfallRequestDetails);
			cStmtObject.setString(1,shortfallSeqID);
			//cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs!=null)
			{
				while(rs.next())
				{
				shortfallRequestDetailsVO= new ShortfallRequestDetailsVO();
				shortfallRequestDetailsVO.setInitialDoc(TTKCommon.checkNull(rs.getString("ITL_DOC")));
				shortfallRequestDetailsVO.setReminderDoc(TTKCommon.checkNull(rs.getString("RER_DOC")));
				shortfallRequestDetailsVO.setCloserDoc(TTKCommon.checkNull(rs.getString("CNE_DOC")));
				shortfallRequestDetailsVO.setCloserArrrovalDoc(TTKCommon.checkNull(rs.getString("CLA_DOC")));
				shortfallRequestDetailsVO.setClosureLetterDoc(TTKCommon.checkNull(rs.getString("CLL_DOC")));
				shortfallRequestDetailsVO.setRegretLetterDoc(TTKCommon.checkNull(rs.getString("REG_DOC")));
				shortfallRequestDetailsVO.setReopenRecDoc(TTKCommon.checkNull(rs.getString("ROR_DOC")));
				shortfallRequestDetailsVO.setReopenNonRecDoc(TTKCommon.checkNull(rs.getString("RON_DOC")));
                shortfallRequestDetailsVO.setRecommendingWaiverDoc(TTKCommon.checkNull(rs.getString("RWD_DOC")));
				shortfallRequestDetailsVO.setInitialDocSentDate(TTKCommon.checkNull(rs.getString("INTL_SHORTFAL_SNT_DATE")));
				shortfallRequestDetailsVO.setReminderDocSentDate(TTKCommon.checkNull(rs.getString("RMDR_REQ_SNT_DATE")));
				shortfallRequestDetailsVO.setCloserDocSentDate(TTKCommon.checkNull(rs.getString("CLSR_NOTICE_SNT_DATE")));
                shortfallRequestDetailsVO.setCloserArrrovalDocSentDate(TTKCommon.checkNull(rs.getString("APRV_CLSR_SNT_DATE")));
				shortfallRequestDetailsVO.setClosureLetterDocSentDate(TTKCommon.checkNull(rs.getString("CLM_CLSR_LETTER_SNT_DATE")));
				shortfallRequestDetailsVO.setRegretLetterDocSentDate(TTKCommon.checkNull(rs.getString("REGRET_LETTER_SNT_DATE")));
				shortfallRequestDetailsVO.setReopenRecDocSentDate(TTKCommon.checkNull(rs.getString("REOPEN_RECOMAND_SNT_DATE")));
				shortfallRequestDetailsVO.setReopenNonRecDocSentDate(TTKCommon.checkNull(rs.getString("NON_REOPEN_RECOMAND_SNT_DATE")));
				shortfallRequestDetailsVO.setRecommendingWaiverDocSentDate(TTKCommon.checkNull(rs.getString("WAVIER_REQ_SNT_DATE")));

                shortfallRequestDetailsVO.setInitialDocSentBy(TTKCommon.checkNull(rs.getString("INTL_SHORTFAL_SNT_BY")));
                shortfallRequestDetailsVO.setReminderDocSentBy(TTKCommon.checkNull(rs.getString("RMDR_REQ_SNT_BY")));
                shortfallRequestDetailsVO.setCloserDocSentBy(TTKCommon.checkNull(rs.getString("CLSR_NOTICE_SNT_BY")));
				shortfallRequestDetailsVO.setCloserArrrovalDocSentBy(TTKCommon.checkNull(rs.getString("APRV_CLSR_SNT_BY")));
				shortfallRequestDetailsVO.setClosureLetterDocSentBy(TTKCommon.checkNull(rs.getString("CLM_CLSR_LETTER_SNT_BY")));
				shortfallRequestDetailsVO.setRegretLetterDocSentBy(TTKCommon.checkNull(rs.getString("REGRET_LETTER_SNT_BY")));
				shortfallRequestDetailsVO.setReopenRecDocSentBy(TTKCommon.checkNull(rs.getString("REOPEN_RECOMAND_SNT_BY")));
				shortfallRequestDetailsVO.setReopenNonRecDocSentBy(TTKCommon.checkNull(rs.getString("NON_REOPEN_RECOMAND_SNT_BY")));
				shortfallRequestDetailsVO.setRecommendingWaiverDocSentBy(TTKCommon.checkNull(rs.getString("NON_REOPEN_RECOMAND_SNT_BY")));
				shortfallRequestDetailsVO.setviewDetails(TTKCommon.checkNull(rs.getString("REQUEST_SNT_DTL")));

				shortfallRequestDetailsVO.setInsuredEmailId(TTKCommon.checkNull(rs.getString("INSURED_EMAIL_ID")));
				shortfallRequestDetailsVO.setInsurerEmailId(TTKCommon.checkNull(rs.getString("INSURER_EMAIL_ID")));
				shortfallRequestDetailsVO.setshrtEmailSeqID(TTKCommon.checkNull(rs.getString("shortfal_email_seq_id")));
				shortfallRequestDetailsVO.setshrtSeqID(TTKCommon.checkNull(rs.getString("shortfall_seq_id")));

				//added for Mail-SMS Template for Cigna
				shortfallRequestDetailsVO.setCignaYN(TTKCommon.checkNull(rs.getString("CIGNA_YN")));
				shortfallRequestDetailsVO.setMemberClaimYN(TTKCommon.checkNull(rs.getString("MEM_CLM")));
                //ended

				}

			}
			return shortfallRequestDetailsVO;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of generateShortfallRequestDetails(ArrayList alSearchCriteria)

    //koc1179
    /**
     * This method returns the Arraylist of claim no,shortfall no,emailid and status  which contains Claim Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Claim details
     * @exception throws TTKException
     */
    public ArrayList getShortfallRequests(ArrayList alSearchCriteria) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        Collection<Object> alResultList = new ArrayList<Object>();
        ShortfallStatusVO shortfallStatusVO=null;
        String strGroupID="";
        String strPolicyGrpID ="";
        ArrayList alUserGroup = new ArrayList();
      //  String strSuppressLink[]={"0","7","8"};
        try{

            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetShortfallRequestList);
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
            //cStmtObject.setString(11,(String)alSearchCriteria.get(10));//added for Mail-SMS for Cigna
            cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
            cStmtObject.execute();
            //get the user group list
            rs = (java.sql.ResultSet)cStmtObject.getObject(11);
            if(rs != null){
                while(rs.next()){

                	shortfallStatusVO	=new ShortfallStatusVO();
                	shortfallStatusVO.setShortfallEmailSeqId(TTKCommon.checkNull(rs.getString("SHORTFAL_EMAIL_SEQ_ID")));
                	shortfallStatusVO.setShortfallNumber(TTKCommon.checkNull(rs.getString("SHORTFALL_ID")));
                	shortfallStatusVO.setClaimNumber(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
                	shortfallStatusVO.setShortfallStatus(TTKCommon.checkNull(rs.getString("SHORTFALL_EMAIL_STATUS")));
                	shortfallStatusVO.setEmailIDStatus(TTKCommon.checkNull(rs.getString("TO_EMAIL_ID")));
                    shortfallStatusVO.setMobileNo(TTKCommon.checkNull(rs.getString("MOBILE_NO")));
                	shortfallStatusVO.setBranch(TTKCommon.checkNull(rs.getString("TTK_BRANCH")));
                	//added for Mail-SMS Cigna
                	/*if(rs.getString("CIGNA_YN")!=null)
                	{
                		shortfallStatusVO.setCignaYN(TTKCommon.checkNull(rs.getString("CIGNA_YN")));
                	}*/
                	alResultList.add(shortfallStatusVO);

                  //  alResultList.add();
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;

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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getShortfallRequests(ArrayList alSearchCriteria)



    // Shortfall CR KOC1179
    /**
     * This method returns the rowsprocessed of Shortfall Email's  which contains Claim Shortfall Email Details
     * @param ShortfallTypeDesc string value contains ShortfallTypeDesc
     * @param ShortfallEmailSeqID string value contains ShortfallEmailSeqID
     * @param ShortfallType string value contains ShortfallType
     * @param UserSeqId long UserSeqId
     * @return rowsprocessed of Shortfall object which contains Claim Shortfall Email Details
     * @exception throws TTKException
     */
    public long resendShortfallEmails(String ShortfallTypeDesc,String ShortfallEmailSeqID,String ShortfallType,long UserSeqId) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
    	long rowsprocessed=0;
    	try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strResendShortfallEmailRequest);
			cStmtObject.setString(1,ShortfallTypeDesc);
			cStmtObject.setString(2,ShortfallEmailSeqID);
			cStmtObject.setString(3,ShortfallType);
			cStmtObject.setLong(5,UserSeqId);
			cStmtObject.registerOutParameter(4,OracleTypes.VARCHAR);
			cStmtObject.registerOutParameter(6,OracleTypes.BIGINT);
			cStmtObject.execute();
			rowsprocessed = cStmtObject.getLong(6);


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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl resendShortfallEmails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl resendShortfallEmails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl resendShortfallEmails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally

		return rowsprocessed;
    }//end of resendShortfallEmails(String ShortfallTypeDesc,String ShortfallEmailSeqID,String ShortfallType,long UserSeqId)

    /**
     * This method returns the rowsprocessed of Shortfall Email's  which contains Claim Shortfall Email Details
     * @param ShortfallTypeDesc string value contains ShortfallTypeDesc
     * @param ShortfallEmailSeqID string value contains ShortfallEmailSeqID
     * @param ShortfallType string value contains ShortfallType
     * @param UserSeqId long UserSeqId
     * @return rowsprocessed of Shortfall object which contains Claim Shortfall Email Details
     * @exception throws TTKException
     */
    public long resendCignaShortfallEmails(String shortfallEmailSeqID,String ShortfallTypeDesc,String ShortfallType,long UserSeqId) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
    	long rowsprocessed=0;
    	try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strResendCignaShortfallEmailRequest);
			cStmtObject.setString(1,shortfallEmailSeqID);
			cStmtObject.setString(2,ShortfallTypeDesc);
			cStmtObject.setString(3,ShortfallType);
			cStmtObject.setLong(4,UserSeqId);
			cStmtObject.registerOutParameter(5,OracleTypes.BIGINT);
			cStmtObject.execute();
            rowsprocessed = cStmtObject.getLong(5);

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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl resendCignaShortfallEmails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl resendCignaShortfallEmails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl resendCignaShortfallEmails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally

		return rowsprocessed;
    }//end of resendCignaShortfallEmails(String ShortfallTypeDesc,String ShortfallEmailSeqID,String ShortfallType,long UserSeqId)



   // public long saveShortFallFileName(String fileName) throws TTKException{
   	public long saveShortFallFileName(String fileName,long UserSeqID,String strShortfallNoSeqID,String shortfalltype) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    		long  batchSeqId=0;
    		long  batchId=0;
    	long rowsprocessed=0;
    	try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveBulkPDFname);

			 cStmtObject.setString(1, strShortfallNoSeqID);
			  cStmtObject.setString(2, shortfalltype);
			  cStmtObject.setLong(3, UserSeqID);
			  cStmtObject.setString(4, fileName);
			  cStmtObject.registerOutParameter(5,OracleTypes.BIGINT);
			cStmtObject.registerOutParameter(6,OracleTypes.INTEGER);
			cStmtObject.execute();
			batchId = cStmtObject.getLong(5);
			rowsprocessed = cStmtObject.getLong(6);
			if(rowsprocessed>0)
			{
				batchSeqId=batchId;
			}
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
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ClaimDAOImpl resendShortfallEmails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl resendShortfallEmails()",sqlExp);
							throw new TTKException(sqlExp, "claim");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{

				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally

    	return batchSeqId;


    }



    //koc1179
    /**
     * This method returns the Arraylist of claim no,shortfall no,emailid and status  which contains Claim Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Claim details
     * @exception throws TTKException
     */
    public ArrayList viewShortFallAdvice(ArrayList alSearchCriteria) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        Collection<Object> alResultList = new ArrayList<Object>();
        BulkGeneratedPDFVO bulkGeneratedPDFVO=null;
        String strGroupID="";
        String strPolicyGrpID ="";
        ArrayList alUserGroup = new ArrayList();
      //  String strSuppressLink[]={"0","7","8"};

      /*  v_file_name           IN  OUT app.SHORTFALL_BATCH.SHORTFALL_FILE_NAME%TYPE,
        v_batch_number        IN  app.SHORTFALL_BATCH.BATCH_FILE_NO%TYPE,
        v_batch_date          IN  VARCHAR2,
        v_sort_var            IN  VARCHAR2,
        v_sort_order          IN  VARCHAR2 ,
        v_start_num           IN  NUMBER ,
        v_end_num             IN  NUMBER ,
        v_resultset           OUT SYS_REFCURSOR*/

        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strviewShortFallAdvice);
             cStmtObject.setString(1,(String)alSearchCriteria.get(0));
            cStmtObject.setString(2,(String)alSearchCriteria.get(1));
           // cStmtObject.setString(3,(String)alSearchCriteria.get(2));
            Date batchDate=TTKCommon.getUtilDate((String)alSearchCriteria.get(2));
            cStmtObject.setTimestamp(3,batchDate!=null ? new Timestamp((batchDate).getTime()):null);
            cStmtObject.setString(4,(String)alSearchCriteria.get(3));


            cStmtObject.setString(5,(String)alSearchCriteria.get(4));
            cStmtObject.setString(6,(String)alSearchCriteria.get(5));
            cStmtObject.setString(7,(String)alSearchCriteria.get(6));

            /* cStmtObject.setString(8,(String)alSearchCriteria.get(7));
            cStmtObject.setString(9,(String)alSearchCriteria.get(8));
            cStmtObject.setString(10,(String)alSearchCriteria.get(9));*/
          /*  cStmtObject.setString(11,(String)alSearchCriteria.get(10));*/



            cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
            cStmtObject.execute();
            //get the user group list
            //alUserGroup = (ArrayList)alSearchCriteria.get(19);
            rs = (java.sql.ResultSet)cStmtObject.getObject(8);
            if(rs != null){
                while(rs.next()){

                	bulkGeneratedPDFVO=new BulkGeneratedPDFVO();
                	bulkGeneratedPDFVO.setFileName(TTKCommon.checkNull(rs.getString("BATCH_FILE_NAME")));
                	bulkGeneratedPDFVO.setBatchNumber(TTKCommon.checkNull(rs.getString("BATCH_NUMBER")));
                	bulkGeneratedPDFVO.setBatchDate(new Date(rs.getTimestamp("BATCH_DATE").getTime()));
					bulkGeneratedPDFVO.setBatchDate1(new Date(rs.getTimestamp("BATCH_DATE").getTime()));
                	alResultList.add(bulkGeneratedPDFVO);

                  //  alResultList.add();
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl viewShortFallAdvice()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl viewShortFallAdvice()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl viewShortFallAdvice()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getShortfallRequests(ArrayList alSearchCriteria)

  /*  PROCEDURE get_srtfll_file_name (
    	    v_batch_file_no          IN app.SHORTFALL_BATCH.BATCH_FILE_NO%TYPE,
    	    v_SHORTFALL_SAVE_FILE_NAME  OUT VARCHAR2
    	  );*/

	/**
	 * This method returns the String which contains the Batch file name to save the file with the Batch file name
	 * @param strBatchNo long value which contains Batch number to get the Batch file name
	 * @return String this contains the Batch file name
	 * @exception throws TTKException
	 */
	public String getBatchFileName(String strBatchNo) throws TTKException
	{
		String strResult = "";
		Connection conn = null;
		CallableStatement cStmtObject=null;
    	try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetBulkPDFilename);
			cStmtObject.setString(1,(String)strBatchNo);
			cStmtObject.registerOutParameter(2,OracleTypes.VARCHAR);
			cStmtObject.execute();
			strResult =(String)cStmtObject.getObject(2);
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
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in ClaimDAOImpl getBatchFileName()",sqlExp);
        			throw new TTKException(sqlExp, "floataccount");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ClaimDAOImpl getBatchFileName()",sqlExp);
        				throw new TTKException(sqlExp, "floataccount");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "claim");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return strResult;

	}//end of getBatchFileName(String strBatchNo)

	public long overridClaimDetails(String claimSeqID,String overrideRemarks,Long userSeqID,String overrideGenRemarks,String overrideGenSubRemarks) throws TTKException{

		long rowUpd =0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
    	try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strOverridClaimDetails);
			cStmtObject.setString(1,claimSeqID);
			cStmtObject.setLong(2,userSeqID);
			cStmtObject.setString(3,overrideRemarks);			
			cStmtObject.setString(4,overrideGenRemarks);		// main dp
			cStmtObject.setString(5,overrideGenSubRemarks);		// sumdp
			cStmtObject.registerOutParameter(6,OracleTypes.NUMBER);
			cStmtObject.execute();
			  rowUpd=cStmtObject.getLong(6);
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
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in ClaimDAOImpl overridClaimDetails()",sqlExp);
        			throw new TTKException(sqlExp, "floataccount");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ClaimDAOImpl overridClaimDetails()",sqlExp);
        				throw new TTKException(sqlExp, "floataccount");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "claim");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return rowUpd;


	}
	
	public void getRejectedClaimDetails(Long claimSeqId, String denialCode,String medicalOpinionRemarks,String overrideRemarks,String finalRemarks,Long addedBy,String internalRemarks) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		boolean result;
		ResultSet clmrs=null;ResultSet drs=null;ResultSet ars = null;ResultSet shrs = null;
		PreAuthDetailVO preAuthDetailVO  = new PreAuthDetailVO();
		ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
		ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
		ArrayList<String[]> shortfalls=null;
		Object[] results=new Object[5];
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strRejectGetClaimDetail); 
			cStmtObject.setLong(1,claimSeqId);			
			cStmtObject.setString(2, denialCode);	
			cStmtObject.setString(3, medicalOpinionRemarks);	
			cStmtObject.setString(4, overrideRemarks);
			cStmtObject.setString(5, finalRemarks);
			cStmtObject.setLong(6, addedBy);
			cStmtObject.setString(7, internalRemarks);
			 result=cStmtObject.execute();
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
					if (shrs != null) shrs.close();		
					if (ars != null) ars.close();
					if (drs != null) drs.close();
					if (clmrs != null) clmrs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimDetail()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimDetail()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimDetail()",sqlExp);
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
				clmrs = null;
				drs = null;
				ars = null;
				shrs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	
	}//end of getClaimDetails(Long ClaimSeqID)
	
	
	public LinkedHashMap<String, String> getClaimDiagDetails() throws TTKException
	{
		//HashMap<String, String> hashMap=new HashMap<>();
		LinkedHashMap<String, String> linkedhashMap=new LinkedHashMap<String, String>();
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
				
				linkedhashMap.put(resultSet.getString("denial_code"), resultSet.getString("denial_description"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			CommonClosure.closeOpenResources(resultSet, null, statement, conn, null, this, "getClaimDiagDetails");
		}
		return linkedhashMap;
		
	}

	
	 public ArrayList getPatDetails(String claimSeqID, String preauthSeqId) throws TTKException {
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ResultSet rs = null;
	    	ArrayList alResultList = new ArrayList();
	    	PreAuthDetailVO preAuthDetailVO = null;
	    	try{

				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPat_details);
				cStmtObject.setString(1,preauthSeqId);
				cStmtObject.setString(2,claimSeqID);

				cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(3);
				if(rs!=null)
				{
					while(rs.next())
					{
						preAuthDetailVO= new PreAuthDetailVO();
					    preAuthDetailVO.setAvailableSumInsured(rs.getBigDecimal("AVA_SUM_INSURED"));
					}

				}
				alResultList.add(preAuthDetailVO.getAvailableSumInsured());
				return alResultList;
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
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
								log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	    }//end of generateShortfallRequestDetails(ArrayList alSearchCriteria)
	 
	 
	 public String[] getClaimSettleMentDtls(Long claimSeqID) throws TTKException {
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ResultSet rs = null;
	    	String[]  strArr = null;
	    	try{

	    		conn = ResourceManager.getConnection();
	    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClmSettlementDtls);
				cStmtObject.setLong(1,claimSeqID);

				cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(2);
				if(rs!=null)
				{
					strArr	=	new String[10];
					if(rs.next())
					{
						strArr[0]	=	TTKCommon.checkNull(rs.getString("PAYEE_NAME"));
						strArr[1]	=	TTKCommon.checkNull(rs.getString("CHECK_NUM"));
						strArr[2]	=	TTKCommon.checkNull(rs.getString("CHECK_AMOUNT"));
						strArr[3]	=	TTKCommon.checkNull(rs.getString("PAYEE_TYPE"));
						strArr[4]	=	TTKCommon.checkNull(rs.getString("CHECK_DATE"));
						if("SENT_TO_BANK".equals(TTKCommon.checkNull(rs.getString("CHECK_STATUS"))))
							strArr[5]	=	"APPROVED BY FINANCE";
						else
							strArr[5]	=	TTKCommon.checkNull(rs.getString("CHECK_STATUS"));
						strArr[6]	=	TTKCommon.checkNull(rs.getString("TRANSFERRED_CURRENCY"));
						strArr[7]	=	TTKCommon.checkNull(rs.getString("INCURRED_cURRENCY"));
					}

				}
				return strArr;
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
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
								log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	    }//end of getClaimSettleMentDtls(String claimSeqID)
	 
	public ArrayList<String> saveFraudData(ArrayList<String> listOfinputData) throws TTKException {
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	ArrayList<String>  strArr = null;
	
    	try{

    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strfraudDeatilsSave);
    		Long clmSeqId = Long.parseLong(listOfinputData.get(0));
			cStmtObject.setLong(1,clmSeqId);
			cStmtObject.setString(2, listOfinputData.get(1));
			cStmtObject.setString(3, listOfinputData.get(2));
			cStmtObject.setString(4, listOfinputData.get(3));
			cStmtObject.setString(5, listOfinputData.get(4));
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(6);
			if(rs!=null)
			{
				strArr	=	new ArrayList<>();
				if(rs.next())
				{
					strArr.add(TTKCommon.checkNull(rs.getString("code_added_by")));
					strArr.add(TTKCommon.checkNull(rs.getString("code_added_date")));
					strArr.add(TTKCommon.checkNull(rs.getString("status_code_id")));
					strArr.add(TTKCommon.checkNull(rs.getString("code_remarks")));
					strArr.add(TTKCommon.checkNull(rs.getString("completed_yn")));
					strArr.add(TTKCommon.checkNull(rs.getString("fraud_yn")));
					strArr.add(TTKCommon.checkNull(rs.getString("suspect_veri_check")));
				}

			}
			return strArr;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}

	public ArrayList<String> fetchFraudData(String clmSeqId) throws TTKException {
		
		
		Connection conn = null;
		PreparedStatement statement=null;
		ResultSet resultSet=null;
		conn=ResourceManager.getConnection();
		ArrayList<String> strList = new ArrayList<>();
		try {
			statement=conn.prepareStatement(strGetfraudDetails);
			statement.setString(1, clmSeqId);
			resultSet=statement.executeQuery();
			while(resultSet.next())
			{
			
				strList.add(TTKCommon.checkNull(resultSet.getString("status_code_id")));
				strList.add(TTKCommon.checkNull(resultSet.getString("code_remarks")));
				strList.add(TTKCommon.checkNull(resultSet.getString("code_added_by")));
				strList.add(TTKCommon.checkNull(resultSet.getString("code_added_date")));
				strList.add(TTKCommon.checkNull(resultSet.getString("completed_yn")));
				strList.add(TTKCommon.checkNull(resultSet.getString("suspect_veri_check")));
			}
			
			return strList;
			
		}catch (SQLException sqlExp)
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
					if (resultSet != null) resultSet.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
					throw new TTKException(sqlExp, "claim");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (statement != null) statement.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
				resultSet = null;
				statement = null;
				conn = null;
			}//end of finally
		}//end of finally
	}
	 
	public ArrayList getClaimAndPreauthList(ArrayList alSearchCriteria) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet resultSet=null;
		conn=ResourceManager.getConnection();
		ArrayList strList = new ArrayList<>();
		PreAuthDetailVO preAuthDetailVO=null;
		try{
			cStmtObject=conn.prepareCall(strSelectClaimAndPreauthList);
			cStmtObject.setString(1, (String)alSearchCriteria.get(0));
			cStmtObject.setString(2, (String)alSearchCriteria.get(1));
			cStmtObject.setString(3, (String)alSearchCriteria.get(2));
			cStmtObject.setString(4, (String)alSearchCriteria.get(3));
			cStmtObject.setString(5, (String)alSearchCriteria.get(4));
			cStmtObject.setString(6, (String)alSearchCriteria.get(5));
			cStmtObject.setString(7, (String)alSearchCriteria.get(6));
			cStmtObject.setString(8, (String)alSearchCriteria.get(7));
			cStmtObject.setString(9, (String)alSearchCriteria.get(8));
			cStmtObject.setString(10, (String)alSearchCriteria.get(9));
			cStmtObject.setString(11, (String)alSearchCriteria.get(10));
			cStmtObject.setString(12, (String)alSearchCriteria.get(11));
			cStmtObject.setString(13, (String)alSearchCriteria.get(12));
			cStmtObject.setString(14, (String)alSearchCriteria.get(13));
			cStmtObject.setString(15, (String)alSearchCriteria.get(14));
			cStmtObject.setString(16, (String)alSearchCriteria.get(15));
			cStmtObject.setString(17, (String)alSearchCriteria.get(16));
			cStmtObject.setString(18, (String)alSearchCriteria.get(17));
			cStmtObject.setString(19, (String)alSearchCriteria.get(18));
			cStmtObject.setString(20, (String)alSearchCriteria.get(19));
			cStmtObject.setString(21, (String)alSearchCriteria.get(20));
			cStmtObject.setString(22, (String)alSearchCriteria.get(21));
			cStmtObject.setString(23, (String)alSearchCriteria.get(22));
			cStmtObject.setString(24, (String)alSearchCriteria.get(23));
			cStmtObject.registerOutParameter(25,OracleTypes.CURSOR);
			cStmtObject.execute();
			resultSet = (java.sql.ResultSet)cStmtObject.getObject(25);
			
			if(resultSet != null){
				while(resultSet.next()){
					preAuthDetailVO = new PreAuthDetailVO();
					preAuthDetailVO.setClaimorpreauthseqId(resultSet.getLong("seq_id"));
					preAuthDetailVO.setClaimOrPreauthNumber(TTKCommon.checkNull(resultSet.getString("pat_clm_no")));
					preAuthDetailVO.setInternalRemarkStatus(TTKCommon.checkNull(resultSet.getString("internal_remaks_status")));
					preAuthDetailVO.setRiskLevel(TTKCommon.checkNull(resultSet.getString("risk_level")));
					preAuthDetailVO.setEnrollmentId(TTKCommon.checkNull(resultSet.getString("tpa_enrollment_id")));
					preAuthDetailVO.setClaimantName(TTKCommon.checkNull(resultSet.getString("mem_name")));
					preAuthDetailVO.setProviderName(TTKCommon.checkNull(resultSet.getString("hosp_name")));
					preAuthDetailVO.setClaimType(TTKCommon.checkNull(resultSet.getString("claim_type")));
					preAuthDetailVO.setPolicyType(TTKCommon.checkNull(resultSet.getString("pol_type")));
					preAuthDetailVO.setStatus(TTKCommon.checkNull(resultSet.getString("pat_clm_status")));
					
					if(alSearchCriteria.get(18).equals("PAT")){
					preAuthDetailVO.setEnhancedPreauthYN(TTKCommon.checkNull(resultSet.getString("ENHANCED_PREAUTHYN")));
                	if("Y".equals(preAuthDetailVO.getEnhancedPreauthYN()))
                	{
                		preAuthDetailVO.setPreauthEnhanceImageName("EnhancedPreauth");
                		preAuthDetailVO.setPreauthEnhanceImageTitle("Enhanced PreApproval");
                	}
				}
					strList.add(preAuthDetailVO);	
				}
			}
		    return strList;
		}catch (SQLException sqlExp)
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
					if (resultSet != null) resultSet.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl generateShortfallRequestDetails()",sqlExp);
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
				resultSet = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}

	public ArrayList getClaimDetailedReportData(ArrayList<Object> alSearchCriteria) throws TTKException{
		ArrayList<ProviderClaimsVO> alClaimsSearchVOs	=	new ArrayList<ProviderClaimsVO>();
		try(Connection connection=ResourceManager.getConnection();
			CallableStatement callableStatement=connection.prepareCall(strClaimDetailedReportData)){
            callableStatement.setString(1,(String) alSearchCriteria.get(0));//1 TfromDate
            callableStatement.setString(2,(String) alSearchCriteria.get(1));//2 TtoDate
            callableStatement.setString(3,(String) alSearchCriteria.get(2));//3 CfromDate
            callableStatement.setString(4,(String) alSearchCriteria.get(3));//4 CtoDate
            callableStatement.setString(5,(String) alSearchCriteria.get(4));//5 patientName
            callableStatement.setString(6,(String) alSearchCriteria.get(5));//6 status
            callableStatement.setString(7,(String) alSearchCriteria.get(6));//7 invoiceNumber
            callableStatement.setString(8,(String) alSearchCriteria.get(7));//8 batchNo
            callableStatement.setString(9,(String) alSearchCriteria.get(8));//9 memberId
            callableStatement.setString(10,(String) alSearchCriteria.get(9));//10  claimNumber
            callableStatement.setString(11,(String) alSearchCriteria.get(10));//11 benefitType
            callableStatement.setString(12,(String) alSearchCriteria.get(11));//12 refNo
            callableStatement.setString(13,(String) alSearchCriteria.get(12));//13 qatarId
            callableStatement.setString(14,(String) alSearchCriteria.get(13));//14 pay_ref_no
            callableStatement.setString(15,(String) alSearchCriteria.get(14));//15 Hospital_Seq_Id
            callableStatement.setString(16,(String) alSearchCriteria.get(15));//16 report_Tye
            callableStatement.setString(17,(String) alSearchCriteria.get(16));//17 finBatchNo
            callableStatement.setString(18,(String) alSearchCriteria.get(17));//18 financeStatus
            if("".equals((String)alSearchCriteria.get(18)))  //19 partnerNm
            	callableStatement.setString(19,"");			//19 partnerNm
            else
            	callableStatement.setLong(19,new Long((String) alSearchCriteria.get(18)));//19 partnerNm
            
            callableStatement.setString(20,(String) alSearchCriteria.get(19));//20 sort_var
            callableStatement.setString(21,(String) alSearchCriteria.get(20));//21 sort_order
            callableStatement.setString(22,(String) alSearchCriteria.get(21));//22 start_num
            callableStatement.setString(23,(String) alSearchCriteria.get(22));//23 end_num
            callableStatement.registerOutParameter(24,OracleTypes.CURSOR);	  //24 Cursor
            callableStatement.execute();
            try(ResultSet rs = (java.sql.ResultSet)callableStatement.getObject(24)){
                while (rs.next()) {
                	ProviderClaimsVO providerClaimsVO 	=	new ProviderClaimsVO();
                	providerClaimsVO.setBatchNo(TTKCommon.checkNull(rs.getString("BATCH_NO")));
					providerClaimsVO.setInvoiceNo(TTKCommon.checkNull(rs.getString("INVOICE_NUMBER")));
					providerClaimsVO.setAlkootId(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					providerClaimsVO.setPatientName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
                	providerClaimsVO.setClaimNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					providerClaimsVO.setBenefitType(TTKCommon.checkNull(rs.getString("BENIFIT_TYPE")));
					providerClaimsVO.setStatus(TTKCommon.checkNull(rs.getString("CLM_STATUS_TYPE_ID")));
                	if(rs.getString("TOT_APPROVED_AMOUNT")!=null)
			        providerClaimsVO.setApprovedAmt(""+rs.getBigDecimal("TOT_APPROVED_AMOUNT"));
					else
					providerClaimsVO.setApprovedAmt("");
	                providerClaimsVO.setClaimPaymentStatus(TTKCommon.checkNull(rs.getString("CLAIM_PAYMENT_STATUS")));
	                providerClaimsVO.setFinanceBatchNo(TTKCommon.checkNull(rs.getString("finance_batch_no")));
                	alClaimsSearchVOs.add(providerClaimsVO); 
                }//end of while(rs.next())
            }
			return alClaimsSearchVOs;
		}catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "claim");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "claim");
		}
	}
	
	public ArrayList getClaimListForAlert(ArrayList alSearchCriteria) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Collection<Object> alResultList = new ArrayList<Object>();
		ChequeVO chequeVO = null;
		//String strGroupID="";
		//String strPolicyGrpID ="";
		//ArrayList alUserGroup = new ArrayList();
		// strSuppressLink[]={"0","7","8"};
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetClaimList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//sInvoiceNO
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//sBatchNO
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//sPolicyNumber
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//sClaimNO
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));//sClaimType
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));//sRecievedDate
			cStmtObject.setString(7,(String)alSearchCriteria.get(6));//sSettlementNO
			cStmtObject.setString(8,(String)alSearchCriteria.get(7));//sEnrollmentId
			cStmtObject.setString(9,(String)alSearchCriteria.get(8));//sClaimantName
			cStmtObject.setString(10,(String)alSearchCriteria.get(9));//sStatus
			cStmtObject.setString(11,(String)alSearchCriteria.get(10));//sProviderName
			cStmtObject.setString(12,"");//v_ins_seq_id
			cStmtObject.setString(13,(String)alSearchCriteria.get(12));//sModeOfClaim
			cStmtObject.setString(14,(String)alSearchCriteria.get(13));	//sGlobalNetMemID	
            cStmtObject.setString(15,(String)alSearchCriteria.get(14));//sAssignedTo
			cStmtObject.setString(16,(String)alSearchCriteria.get(15));//sSpecifyName
			cStmtObject.setLong(17,(Long)alSearchCriteria.get(16));//Added By
			cStmtObject.setString(18,(String)alSearchCriteria.get(17));//sProcessType
			cStmtObject.setString(19,(String)alSearchCriteria.get(18));//clmShortFallStatus
			cStmtObject.setString(20,(String)alSearchCriteria.get(19));//eventReferenceNo
			cStmtObject.setString(21,(String)alSearchCriteria.get(20));//sCommonFileNo
			cStmtObject.setString(22,(String)alSearchCriteria.get(21));//internalRemarkStatus
			cStmtObject.setString(23,(String)alSearchCriteria.get(22));//sPartnerName
			cStmtObject.setString(24,(String)alSearchCriteria.get(23));//sBenefitType
			cStmtObject.setString(25,(String)alSearchCriteria.get(24));//sAmountRange
			cStmtObject.setString(26,(String)alSearchCriteria.get(25));//sAmountRangeValue
			cStmtObject.setString(27,(String)alSearchCriteria.get(26));//sLinkedPreapprovalNo
			cStmtObject.setString(28,(String)alSearchCriteria.get(27));//sQatarId
			String sStatus	= (String)alSearchCriteria.get(9);
			if("INP".equals(sStatus))					// sStatus
				cStmtObject.setString(29,(String)alSearchCriteria.get(28));	// in-progress-Status
			else
				cStmtObject.setString(29,null);
			cStmtObject.setString(30,(String)alSearchCriteria.get(29));//riskLevel
			cStmtObject.setString(31,(String)alSearchCriteria.get(30));//cfdInvestigationStatus
			cStmtObject.setString(32,(String)alSearchCriteria.get(31));//priorityClaims

			cStmtObject.setString(33,(String)alSearchCriteria.get(32));//rangeFrom
			cStmtObject.setString(34,(String)alSearchCriteria.get(33));//rangeTo
			cStmtObject.setString(35,(String)alSearchCriteria.get(36));//v_sort_var
			cStmtObject.setString(36,(String)alSearchCriteria.get(37));//v_sort_order
			cStmtObject.setString(37,(String)alSearchCriteria.get(38));//v_start_num
			cStmtObject.setString(38,(String)alSearchCriteria.get(39));//v_end_num
			cStmtObject.registerOutParameter(39,OracleTypes.CURSOR);
			cStmtObject.setString(40,(String)alSearchCriteria.get(34));
			cStmtObject.setString(41,(String)alSearchCriteria.get(35));
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(39);

			if(rs != null){
				while(rs.next()){
					chequeVO = new ChequeVO();
					    chequeVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
					    chequeVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
                        chequeVO.setClaimNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					   
                        chequeVO.setClaimType(TTKCommon.checkNull(rs.getString("CLAIM_TYPE")));
					if(rs.getString("RECEIVED_DATE") != null){
						chequeVO.setReceivedDateAsString(new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("RECEIVED_DATE").getTime())));
					}//end of if(rs.getString("DATE_OF_ADMISSION") != null)
					chequeVO.setStatus(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    if("INP-ENH".equals(rs.getString("IN_PROGESS_STATUS")))
                	{	
                    	chequeVO.setStatus("In-Progress-Resubmission"); 
                	}
                    else if("INP-RES".equals(rs.getString("IN_PROGESS_STATUS")))
                	{
                    	chequeVO.setStatus("In-Progress-Shortfall Responded");
                	}
                    else
                    {
                    	chequeVO.setStatus(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    }
                    chequeVO.setApproveAmount(TTKCommon.checkNull(rs.getString("approved_amount"))); 
                    chequeVO.setAssignedTo(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));

					alResultList.add(chequeVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getClaimList(ArrayList alSearchCriteria)

	public ArrayList getProcessedClaimList(ArrayList<Object> alSearchCriteria) throws TTKException {

		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Collection<Object> alResultList = new ArrayList<Object>();
		PreAuthVO preauthVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetProcessedClaimList);
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
			//cStmtObject.setLong(19,(Long)alSearchCriteria.get(18));
			
			cStmtObject.setString(19,(String)alSearchCriteria.get(18));//v_sort_var
			cStmtObject.setString(20,(String)alSearchCriteria.get(19));//v_sort_order
			cStmtObject.setString(21,(String)alSearchCriteria.get(20));//v_start_num
			cStmtObject.setString(22,(String)alSearchCriteria.get(21));//v_end_num
			cStmtObject.registerOutParameter(23,OracleTypes.CURSOR);
			
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(23);
			
			if(rs != null){
				while(rs.next()){
					
					preauthVO = new PreAuthVO();
					
					 preauthVO.setClaimSeqID(new Long(rs.getLong("clm_intm_seq_id")));
					 preauthVO.setInvoiceNo(TTKCommon.checkNull(rs.getString("Invoice_No")));
					 preauthVO.setBatchNo(TTKCommon.checkNull(rs.getString("Batch_No")));
					 preauthVO.setClaimNo(TTKCommon.checkNull(rs.getString("Claim_No")));
					 preauthVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("Alkoot_ID")));
					 preauthVO.setHospitalName(TTKCommon.checkNull(rs.getString("Provider_Name")));
					 preauthVO.setClaimantName(TTKCommon.checkNull(rs.getString("Member_Name")));
					 preauthVO.setClaimType(TTKCommon.checkNull(rs.getString("Claim_Type")));
					 preauthVO.setAssignedTo(TTKCommon.checkNull(rs.getString("Assigned_to")));
					 
					/* if(rs.getString("Received_Date") != null){
							preauthVO.setReceivedDateAsString(new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("Received_Date").getTime())));
						}*/
					 
					 preauthVO.setReceivedDateAsString(rs.getString("Received_Date"));
					 preauthVO.setApproveAmount(TTKCommon.checkNull(rs.getString("Approved_amount_in_QAR")));
					 preauthVO.setStatus(TTKCommon.checkNull(rs.getString("Status")));
					 
					 
					 
					/*preauthVO.setClaimSeqID(123456L);
					 preauthVO.setInvoiceNo("1234567");
					 preauthVO.setBatchNo("Batch12345");
					 preauthVO.setClaimNo("CLM12345");
					 preauthVO.setEnrollmentID("ENRID-12345");
					 preauthVO.setHospitalName("ALHALLI HOSPITAL");
					 preauthVO.setClaimantName("DEEPTHI");
					 preauthVO.setClaimType("IN-PATIENT");
					 preauthVO.setAssignedTo("VEDA");
					 
					 if(rs.getString("Received_Date") != null){
							preauthVO.setReceivedDateAsString("10/04/2020");
						}
					 preauthVO.setApproveAmount("1000");
					 preauthVO.setStatus("APPROVED");					
					
					
					
					
					*/
					 
					 
						alResultList.add(preauthVO); 
				}
				}
			
			return (ArrayList)alResultList;
		}
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "processedClaim");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
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
					log.error("Error while closing the Resultset in ClaimDAOImpl getProcessedClaimList()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getProcessedClaimList()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getProcessedClaimList()",sqlExp);
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

	public Object[] getProcessedClaimDetails(String claimSeqID) throws TTKException{

		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet clmrs=null;
		ResultSet drs = null;
		ResultSet ars = null;
		PreAuthDetailVO preAuthDetailVO  = new PreAuthDetailVO();
		ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
		ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
		ArrayList<String[]> shortfalls=null;
		Object[] results=new Object[5];
		
		try {
			
			conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetProcessedClaimDetail);
            cStmtObject.setLong(1,Long.parseLong(claimSeqID));
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			
			clmrs = (java.sql.ResultSet)cStmtObject.getObject(2);//claim Details
			drs = (java.sql.ResultSet)cStmtObject.getObject(3);//diagnosis Details
			ars = (java.sql.ResultSet)cStmtObject.getObject(4);//activity Details
			
			if(clmrs != null){
				if(clmrs.next()){
					//Member Details
					preAuthDetailVO.setPatientName(clmrs.getString("Member_Name"));
					preAuthDetailVO.setRelationTypeID(clmrs.getString("Relationship"));
					preAuthDetailVO.setQatarId(clmrs.getString("Qatar_ID"));
					preAuthDetailVO.setPatientGender(clmrs.getString("Gender"));
					preAuthDetailVO.setDob(clmrs.getString("Date_of_Birth"));
					preAuthDetailVO.setPolicyNo(clmrs.getString("Policy_No"));//doubt case
					preAuthDetailVO.setPolicyStartDate(clmrs.getString("Policy_Start_Date"));
					preAuthDetailVO.setSumInsured(clmrs.getBigDecimal("Sum_Insured"));//   getBigDecimal
					preAuthDetailVO.setAvailableSumInsured(clmrs.getBigDecimal("Available_sum_Insured"));
					preAuthDetailVO.setEnrollmentId(clmrs.getString("Enrolment_ID"));
					preAuthDetailVO.setMemberInceptionDate(clmrs.getString("Member_Inception_Date"));
					preAuthDetailVO.setMaritalStatus(clmrs.getString("Marital_Status"));
					
					String age = clmrs.getString("Age");
					if(age!=null)
					preAuthDetailVO.setMemberAge(Integer.parseInt(age));
					
					preAuthDetailVO.setCorporateName(clmrs.getString("Corporate_name"));
					preAuthDetailVO.setPolicyEndDate(clmrs.getString("Policy_End_Date"));
					preAuthDetailVO.setUtilizeSuminsured(clmrs.getBigDecimal("Utilized_Sum_Insured"));
					
					//Claim Details
					preAuthDetailVO.setProcessType(clmrs.getString("Submission_Category"));
					preAuthDetailVO.setClaimNo(clmrs.getString("Claim_No"));
					preAuthDetailVO.setSubmissionType(clmrs.getString("Submission_Type"));
					preAuthDetailVO.setSubmissionTypeFlag(clmrs.getString("Submission_Type_Flag"));
					preAuthDetailVO.setClaimSource(clmrs.getString("Claim_Source"));
					preAuthDetailVO.setBenefitType(clmrs.getString("Benefit_Type"));
					preAuthDetailVO.setEncounterTypeId(clmrs.getString("Encounter_Type"));
					preAuthDetailVO.setStrAdmissionDate(clmrs.getString("Admission_Date"));
					preAuthDetailVO.setStrAddedDate(clmrs.getString("Added_date"));
					/*if(clmrs.getString("Added_date") != null){
						preAuthDetailVO.setReceivedDateAsString(new SimpleDateFormat("dd/MM/yyyy").format(new Date(clmrs.getTimestamp("Added_date").getTime())));
					}*/
					preAuthDetailVO.setPreauthNo(clmrs.getString("Pre_Auth_Number"));
					preAuthDetailVO.setClaimType(clmrs.getString("Claim_Type"));
					preAuthDetailVO.setBatchNo(clmrs.getString("Batch_No"));
					preAuthDetailVO.setReceiveDate(clmrs.getString("Claim_Received_Date"));
					preAuthDetailVO.setInvoiceNo(clmrs.getString("Invoice_No"));
					preAuthDetailVO.setServiceDate(clmrs.getString("Service_Date"));
					preAuthDetailVO.setDischargeDate(clmrs.getString("Discharge_Date"));
					preAuthDetailVO.setCompletedDate(clmrs.getString("Completed_Date"));
					preAuthDetailVO.setClaimStatus(clmrs.getString("Claim_Status"));
					
					
					//Provider details
					preAuthDetailVO.setProviderName(clmrs.getString("Provider_Name"));
					preAuthDetailVO.setProviderInternalCode(clmrs.getString("Provider_Internal_Code"));
					preAuthDetailVO.setProviderInternalDesc(clmrs.getString("Provider_Internal_Description"));
					preAuthDetailVO.setEmpanelmentId(clmrs.getString("Empanelment_ID"));
					preAuthDetailVO.setClinicianId(clmrs.getString("Clinician_ID"));
					preAuthDetailVO.setClinicianName(clmrs.getString("Clinician_Name"));
					preAuthDetailVO.setPartnerName(clmrs.getString("Partner_Name"));
					
					
					//Remars Details
					preAuthDetailVO.setInternalRemarkStatus(clmrs.getString("Internal_Remark_Status"));
					preAuthDetailVO.setInternalRemakDesc(clmrs.getString("Internal_Remark_Description"));
					preAuthDetailVO.setFinalRemarks(clmrs.getString("Final_Remarks"));
					preAuthDetailVO.setInternalRemarks(clmrs.getString("Internal_Remarks"));
					preAuthDetailVO.setRemarksProviderMem(clmrs.getString("Remarks_for_the_ProviderMember"));
					preAuthDetailVO.setOverrideRemarks(clmrs.getString("Override_Remarks"));
					preAuthDetailVO.setProviderSpecificRemarks(clmrs.getString("Provider_Specific_remarks"));
					preAuthDetailVO.setMemberSpecificPolicyRemarks(clmrs.getString("Member_specific_Policy_Remarks"));
					preAuthDetailVO.setResubmissionRemarks(clmrs.getString("Resubmission_Remarks"));
					preAuthDetailVO.setAuditRemarks(clmrs.getString("Audit_Remarks"));
					preAuthDetailVO.setRecheckRemarks(clmrs.getString("Recheck_Done_Remarks"));
					preAuthDetailVO.setAlkootRemarks(clmrs.getString("Alkoot_Remarks"));
					
					
					//Finance Remarks
					preAuthDetailVO.setSettlementNO(clmrs.getString("Settlement_Number"));
					preAuthDetailVO.setPaymentStatus(clmrs.getString("Payment_Status"));
					preAuthDetailVO.setChequeNo(clmrs.getString("Cheque_Number"));
					preAuthDetailVO.setChequeDate(clmrs.getString("Cheque_Date"));
					preAuthDetailVO.setOverrideYN(clmrs.getString("Override_YN"));
					preAuthDetailVO.setProviderCountry(clmrs.getString("Provider_Country"));
					preAuthDetailVO.setReqAmnt(clmrs.getString("Requested_amount_Original"));
					preAuthDetailVO.setApprovedAmnt(clmrs.getString("Approved_amount_Original"));
					preAuthDetailVO.setOriginalCurrency(clmrs.getString("Original_Currency"));
					preAuthDetailVO.setAuditStatus(clmrs.getString("Audit_Status"));
					preAuthDetailVO.setClmReqAmntQAR(clmrs.getString("Claim_Requested_amount_QAR"));
					preAuthDetailVO.setClmApprAmntQAR(clmrs.getString("Claim_Aproved_amount_QAR"));
					preAuthDetailVO.setTotalDisAllowedAmnt(clmrs.getString("Total_Disallowed_amount"));
					
					
					
					//Processor Details
					preAuthDetailVO.setDataEntryBy(clmrs.getString("Data_Entry_By"));
					preAuthDetailVO.setProcessedBy(clmrs.getString("Processed_By"));
					preAuthDetailVO.setLastUpdatedBy(clmrs.getString("Last_Updated_By"));
					
				}
				}
			
	        if(drs!=null){
	            while(drs.next()){
	            	
	            	String perComplaints=drs.getString("Presenting_complaints")==null?"":drs.getString("Presenting_complaints");
	    			String pIcdCode=drs.getString("Primary_ICD_Code")==null?"":drs.getString("Primary_ICD_Code");
	    			String sIcdCode=drs.getString("Secondary_ICD_Code")==null?"":drs.getString("Secondary_ICD_Code");
	    			String toothNo=drs.getString("Tooth_Number")==null?"":drs.getString("Tooth_Number");
	    			String pIcdDesc=drs.getString("Primary_ICD_Description")==null?"":drs.getString("Primary_ICD_Description");
	    			String sIcdDesc=drs.getString("Secondary_ICD_Description")==null?"":drs.getString("Secondary_ICD_Description");
	    			String quantity=drs.getString("Quantity")==null?"":drs.getString("Quantity");
	            	
	    			 diagnosis.add(new DiagnosisDetailsVO(perComplaints,pIcdCode,pIcdDesc,sIcdCode,sIcdDesc,toothNo,quantity));
	            	
	            }
	            }
			
			
	    	if(ars!=null){
				int sNo=0;
	            while(ars.next()){
	        
	            	sNo++;
	            	
	            	String Internal_Code=ars.getString("Internal_Code")==null?"":ars.getString("Internal_Code");
	            	String Internal_description=ars.getString("activity_internal_description")==null?"":ars.getString("activity_internal_description");
	            	String Activity_Code=ars.getString("Activity_Code")==null?"":ars.getString("Activity_Code");
	            	String Activity_description=ars.getString("activity_description")==null?"":ars.getString("activity_description");
	    			String Activity_Type=ars.getString("Activity_Type")==null?"":ars.getString("Activity_Type");
	    			String Activity_Quantity=ars.getString("Activity_Quantity")==null?"":ars.getString("Activity_Quantity");
	    			String Approved_Quantity=ars.getString("Approved_Quantity")==null?"":ars.getString("Approved_Quantity");
	    			String Service_Date=ars.getString("Service_Date")==null?"":ars.getString("Service_Date");
	    			String Requested_Amount=ars.getString("Requested_Amount")==null?"":ars.getString("Requested_Amount");
	    			String Gross_amount=ars.getString("Gross_amount")==null?"":ars.getString("Gross_amount");
	    			String Discounted_amount=ars.getString("Discounted_amount")==null?"":ars.getString("Discounted_amount");
	            	String Discounted_Gross_amount=ars.getString("Discounted_Gross_amount")==null?"":ars.getString("Discounted_Gross_amount");
	    			String Patient_Share=ars.getString("Patient_Share")==null?"":ars.getString("Patient_Share");
	    			String Net_amount=ars.getString("Net_amount")==null?"":ars.getString("Net_amount");
	    			String Disallowed_Amount=ars.getString("Disallowed_Amount")==null?"":ars.getString("Disallowed_Amount");
	    			String Allowed_Amount=ars.getString("Allowed_Amount")==null?"":ars.getString("Allowed_Amount");
	    			String Denial_Code=ars.getString("Denial_Code")==null?"":ars.getString("Denial_Code");
	    			String denial_description=ars.getString("denial_description")==null?"":ars.getString("denial_description");
	    			String Over_ride_Date=ars.getString("Over_ride_Date")==null?"":ars.getString("Over_ride_Date");
	    			String Approved_amount=ars.getString("Approved_amount")==null?"":ars.getString("Approved_amount");
	            
	    			activities.add(new ActivityDetailsVO(sNo,Internal_Code,Internal_description,Activity_Code,Activity_description,Activity_Type,Activity_Quantity,Approved_Quantity,
	    					Service_Date,Requested_Amount,Gross_amount,Discounted_amount,Discounted_Gross_amount,
	    					Patient_Share,Net_amount,Disallowed_Amount,Allowed_Amount,Denial_Code,denial_description,Over_ride_Date,Approved_amount));
	    			
	            
	            }
	    	}
	        
	    	results[0]=preAuthDetailVO;
			results[1]=diagnosis;
			results[2]=activities;
			
			
			
			return results;
			
		}
		
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
					if (clmrs != null) clmrs.close();
					if (drs != null) drs.close();
					if (ars != null) ars.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getProcessedClaimDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getProcessedClaimDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getProcessedClaimDetails()",sqlExp);
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
            	ars = null;
            	drs=null;
            	clmrs=null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		
		
		
		
	}


	
	public ArrayList getCFDCompaginList(ArrayList<Object> alSearchCriteria) throws TTKException
	{
		ArrayList<PreAuthDetailVO> alCampaignSearchVo	=	new ArrayList<PreAuthDetailVO>();
		try(Connection connection=ResourceManager.getConnection();
			CallableStatement callableStatement=connection.prepareCall(strCFDCompaginList))
			{
            callableStatement.setString(1,(String) alSearchCriteria.get(0)); // campaign_name 
            callableStatement.setString(2,(String) alSearchCriteria.get(1)); // hosp_seq_id 
            callableStatement.setString(3,(String) alSearchCriteria.get(2)); // recieved_from
            callableStatement.setString(4,(String) alSearchCriteria.get(3)); // recieved_to
            callableStatement.setString(5,(String) alSearchCriteria.get(4)); // campaign_status
            callableStatement.setString(6,(String) alSearchCriteria.get(5)); // sort_var
            callableStatement.setString(7,(String) alSearchCriteria.get(6)); // sort_order 
            callableStatement.setString(8,(String) alSearchCriteria.get(7)); // start_num
            callableStatement.setString(9,(String) alSearchCriteria.get(8)); // end_num
            callableStatement.registerOutParameter(10,OracleTypes.CURSOR);	 // result_set  
            callableStatement.execute();
            try(ResultSet rs = (java.sql.ResultSet)callableStatement.getObject(10)){
                while (rs.next()) {
                	PreAuthDetailVO preAuthDetailVO 	=	new PreAuthDetailVO();
                	
                	if(rs.getString("CAMPAIGN_DTLS_SEQ_ID") != null){
                		preAuthDetailVO.setCampaignDtlSeqId(rs.getLong("CAMPAIGN_DTLS_SEQ_ID"));
                	}
                	
                	preAuthDetailVO.setCampaignName(TTKCommon.checkNull(rs.getString("CAMPAIGN_NAME")));
                	preAuthDetailVO.setCfdProviderName(TTKCommon.checkNull(rs.getString("PROVIDER_NAME")));
                	preAuthDetailVO.setCampaginStartDate(TTKCommon.checkNull(rs.getString("START_DATE")));
                	preAuthDetailVO.setCampaginEndDate(TTKCommon.checkNull(rs.getString("END_DATE")));
                	
                	if(rs.getString("UTILIGED_AMOUNT") != null){
                		preAuthDetailVO.setUtilizedAmount(rs.getLong("UTILIGED_AMOUNT"));
                	}
                	
                	if(rs.getString("SAVED_AMOUNT") != null){
                		preAuthDetailVO.setSavedAmount(rs.getLong("SAVED_AMOUNT"));
                	}
                	
                	
                	preAuthDetailVO.setCampaginStatus(TTKCommon.checkNull(rs.getString("CAMPAIGN_STATUS")));
                	alCampaignSearchVo.add(preAuthDetailVO); 
                }//end of while(rs.next())
            }
			return alCampaignSearchVo;
		}catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "claim");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "claim");
		}
	} // end of getCFDCompaginList()
	
	public int saveCampaignDetails(PreAuthDetailVO preAuthDetailVO) throws TTKException 
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		int res  = 0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCampaignDetail);
			
			if(preAuthDetailVO.getCampaignDtlSeqId() != null && preAuthDetailVO.getCampaignDtlSeqId() != 0)
			cStmtObject.setLong(1,preAuthDetailVO.getCampaignDtlSeqId());
			else
				cStmtObject.setString(1,null);
			
			cStmtObject.setString(2,preAuthDetailVO.getCampaignName());
			
			if(preAuthDetailVO.getProviderSeqId() !=null && preAuthDetailVO.getProviderSeqId() !=0)
			cStmtObject.setLong(3,preAuthDetailVO.getProviderSeqId());
			else
				cStmtObject.setString(3,null);	
			cStmtObject.setString(4,preAuthDetailVO.getCampaginStartDate());
			cStmtObject.setString(5,preAuthDetailVO.getCampaginEndDate());
			if("N".equals(preAuthDetailVO.getDisplayCampStatusFlag()))
				cStmtObject.setString(6,"OPEN");							// getCampaginStatus
			else
				cStmtObject.setString(6,preAuthDetailVO.getCampaginStatus());	// getCampaginStatus
			if(preAuthDetailVO.getUtilizedAmount() != null && preAuthDetailVO.getUtilizedAmount() !=0)
			cStmtObject.setLong(7,preAuthDetailVO.getUtilizedAmount());
			else
				cStmtObject.setString(7,null);	
			
			if(preAuthDetailVO.getSavedAmount() != null && preAuthDetailVO.getSavedAmount() !=0)
			cStmtObject.setLong(8,preAuthDetailVO.getSavedAmount());
			else
				cStmtObject.setString(8,null);	
			
			if(preAuthDetailVO.getCfdtotCases() !=null && preAuthDetailVO.getCfdtotCases()!=0)
			cStmtObject.setLong(9,preAuthDetailVO.getCfdtotCases());
			else
				cStmtObject.setString(9,null);
			
			cStmtObject.setString(10,preAuthDetailVO.getCampaignRemarks());
			cStmtObject.setString(11,preAuthDetailVO.getOtherRemarks());
			cStmtObject.setLong(12,preAuthDetailVO.getAddedBy());
			cStmtObject.registerOutParameter(13, Types.INTEGER);//ROWS_PROCESSED
			cStmtObject.execute();
			res = cStmtObject.getInt(13);//ROWS_PROCESSED
		//	System.out.println("dao save :res ="+res);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveCampaignDetails()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveCampaignDetails()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return res;
	}//end of saveCampaignDetails()
	
	// select compaign details
	public PreAuthDetailVO getCampaignDetails(Long campaignDtlSeqId) throws TTKException 
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PreAuthDetailVO preAuthDetailVO = null;
		try{

			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetCampaignDetails);
			if(campaignDtlSeqId != null)
				cStmtObject.setLong(1,campaignDtlSeqId);
			else
				cStmtObject.setString(1,null);	
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
           rs=(ResultSet)cStmtObject.getObject(2);

			if(rs != null){
				while(rs.next()){

					preAuthDetailVO = new PreAuthDetailVO();
					
						if(rs.getString("campaign_dtls_seq_id") != null){
							preAuthDetailVO.setCampaignDtlSeqId(rs.getLong("campaign_dtls_seq_id"));
						}
					    
						preAuthDetailVO.setCampaignName(TTKCommon.checkNull(rs.getString("campaign_name")));
					    
					    if(rs.getString("hosp_seq_id") != null){
					    	  preAuthDetailVO.setProviderSeqId(rs.getLong("hosp_seq_id"));
						}
					    
					    preAuthDetailVO.setCampaginStartDate(TTKCommon.checkNull(rs.getString("campaign_start_date")));
					    preAuthDetailVO.setCampaginEndDate(TTKCommon.checkNull(rs.getString("campaign_closed_date")));
					    preAuthDetailVO.setCampaginStatus(TTKCommon.checkNull(rs.getString("campaign_status")));
					    
					    if(rs.getString("util_amount_inv") != null){
					    	 preAuthDetailVO.setUtilizedAmount(rs.getLong("util_amount_inv"));
						}
					    
					    if(rs.getString("saved_amount") != null){
					    	 preAuthDetailVO.setSavedAmount(rs.getLong("saved_amount"));
						}
					    
					    if(rs.getString("total_cases") != null){
					    	 preAuthDetailVO.setCfdtotCases(rs.getLong("total_cases"));
						}
					    
					    preAuthDetailVO.setCampaignRemarks(TTKCommon.checkNull(rs.getString("remarks_gen_type_id")));
					    preAuthDetailVO.setOtherRemarks(TTKCommon.checkNull(rs.getString("other_remarks")));
				}//end of while(rs.next())
			}//end of if(rs != null)
            return preAuthDetailVO;
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getCampaignDetails()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getCampaignDetails()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getCampaignDetails()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getCampaignDetails()
	
	
	public ArrayList<Object> getSubgroupName(String overrideRemarksId) throws TTKException
  	{
  		Connection conn = null;
  		PreparedStatement pStmt = null;
  		ResultSet rs1=null;
  		CacheObject cacheObject = null;
  	    ArrayList<CacheObject> denailDesc = new ArrayList<CacheObject>();
  	    ArrayList<Object> allList = new ArrayList<Object>();
  		try {
  			 conn = ResourceManager.getConnection();
  			 pStmt = conn.prepareStatement(strOverrideRemarksSubGrpName);
  			 pStmt.setString(1,overrideRemarksId);
  			 rs1 = pStmt.executeQuery();
  			if(rs1!=null){
  				while(rs1.next()){
  					cacheObject = new CacheObject();
  					cacheObject.setCacheId((rs1.getString("GENERAL_TYPE_ID")));
  	                cacheObject.setCacheDesc(TTKCommon.checkNull(rs1.getString("DESCRIPTION")));
  	                denailDesc.add(cacheObject);
  				}
  			}
  			allList.add(denailDesc);
  			return allList;
  		} 
  		 catch (SQLException sqlExp)
  	     {
  	         throw new TTKException(sqlExp, "claim");
  	     }
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
  						if (rs1 != null) rs1.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Resultset in ClaimDAOImpl getSubgroupName()",sqlExp);
  						throw new TTKException(sqlExp, "claim");
  					}//end of catch (SQLException sqlExp)
  					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  					{
  						try
  						{
  							if (pStmt != null) pStmt.close();
  						}//end of try
  						catch (SQLException sqlExp)
  						{
  							log.error("Error while closing the Statement in ClaimDAOImpl getSubgroupName()",sqlExp);
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
  								log.error("Error while closing the Connection in ClaimDAOImpl getSubgroupName()",sqlExp);
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
  					rs1 = null;
  					pStmt = null;
  					conn = null;
  				}//end of finally
  			}//end of finally
  	} // end of getSubgroupName()
	
	public ArrayList<Object> getGenOverrideSubgroupName(String overrideGenRemarksId) throws TTKException
  	{
  		Connection conn = null;
  		PreparedStatement pStmt = null;
  		ResultSet rs1=null;
  		CacheObject cacheObject = null;
  	    ArrayList<CacheObject> denailDesc = new ArrayList<CacheObject>();
  	    ArrayList<Object> allList = new ArrayList<Object>();
  		try {
  			 conn = ResourceManager.getConnection();
  			 pStmt = conn.prepareStatement(strOverrideGenRemarksSubGrpList);
  			 pStmt.setString(1,overrideGenRemarksId);
  			 rs1 = pStmt.executeQuery();
  			if(rs1!=null){
  				while(rs1.next()){
  					cacheObject = new CacheObject();
  					cacheObject.setCacheId((rs1.getString("GENERAL_TYPE_ID")));
  	                cacheObject.setCacheDesc(TTKCommon.checkNull(rs1.getString("DESCRIPTION")));
  	                denailDesc.add(cacheObject);
  				}
  			}
  			allList.add(denailDesc);
  			return allList;
  		} 
  		 catch (SQLException sqlExp)
  	     {
  	         throw new TTKException(sqlExp, "claim");
  	     }
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
  						if (rs1 != null) rs1.close();
  					}//end of try
  					catch (SQLException sqlExp)
  					{
  						log.error("Error while closing the Resultset in ClaimDAOImpl getGenOverrideSubgroupName()",sqlExp);
  						throw new TTKException(sqlExp, "claim");
  					}//end of catch (SQLException sqlExp)
  					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
  					{
  						try
  						{
  							if (pStmt != null) pStmt.close();
  						}//end of try
  						catch (SQLException sqlExp)
  						{
  							log.error("Error while closing the Statement in ClaimDAOImpl getGenOverrideSubgroupName()",sqlExp);
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
  								log.error("Error while closing the Connection in ClaimDAOImpl getGenOverrideSubgroupName()",sqlExp);
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
  					rs1 = null;
  					pStmt = null;
  					conn = null;
  				}//end of finally
  			}//end of finally
  	} // end of getGenOverrideSubgroupName()

	@SuppressWarnings("null")
	public Object[] getAutoRejectionClaimDetails(String xmlseqid, String parentClaimNo) throws TTKException{

		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet clmrs=null;
		ResultSet ars = null;
	//	PreAuthDetailVO preAuthDetailVO  = new PreAuthDetailVO();
		ClaimUploadErrorLogVO  autoRejectedVo=new ClaimUploadErrorLogVO();
		//ArrayList<DiagnosisDetailsVO> diagnosis=new ArrayList<DiagnosisDetailsVO>();
		//ArrayList<ActivityDetailsVO> activities=new ArrayList<ActivityDetailsVO>();
		ArrayList<String[]> activities=null;
		Object[] results=new Object[5];
		
		try {
			conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetAutoRejectedClaimDetail);
            cStmtObject.setString(1,xmlseqid.trim());
            cStmtObject.setString(2,parentClaimNo.trim());
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			
			clmrs = (java.sql.ResultSet)cStmtObject.getObject(3);//claim Details
			ars = (java.sql.ResultSet)cStmtObject.getObject(4);//claim Details
			
			if(clmrs != null){
				while(clmrs.next()){
					
					
					autoRejectedVo.setAlkootId(clmrs.getString("alkoot_id"));
					autoRejectedVo.setParentClaimNo(clmrs.getString("Parent_Claim_No"));
					autoRejectedVo.setMemberName(clmrs.getString("member_name"));
					//autoRejectedVo.setRecDate(clmrs.getString("received_date"));
					autoRejectedVo.setXmlSeqId(clmrs.getString("XML_SEQ_ID"));
					autoRejectedVo.setHospSeqId(clmrs.getString("hosp_seq_id"));
					autoRejectedVo.setStrAddedDate(clmrs.getString("added_date"));
					
					autoRejectedVo.setClaimAction(clmrs.getString("rej_action"));
					autoRejectedVo.setRejectionReason(clmrs.getString("rej_remarks"));
					
					String recDateTime=TTKCommon.checkNull(clmrs.getString("received_date")) ;
					String[] recDate=recDateTime.split(" ");
					autoRejectedVo.setRecDate(recDate[0]);
					
				}
			}
			if(ars!=null){
				int sNo=0;
				activities=new ArrayList<String[]>();
	            while(ars.next()){

					sNo++;
					//String parent_clm_set_no="12345678";
					String internal_code=ars.getString("internal_code")==null?"":ars.getString("internal_code");
					String parent_clm_set_no=ars.getString("parent_clm_set_no")==null?"":ars.getString("parent_clm_set_no");
					String service_date=ars.getString("service_date")==null?"":ars.getString("service_date");
					String act_type=ars.getString("act_type")==null?"":ars.getString("act_type");
					String serv_desc=ars.getString("serv_desc")==null?"":ars.getString("serv_desc");
					String cpt_code=ars.getString("cpt_code")==null?"":ars.getString("cpt_code");
					String resub_req_amt=ars.getString("resub_req_amt")==null?"":ars.getString("resub_req_amt");
					String quantity=ars.getString("quantity")==null?"":ars.getString("quantity");
					String tooth_no=ars.getString("tooth_no")==null?"":ars.getString("tooth_no");
					String alkoot_remarks=ars.getString("alkoot_remarks")==null?"":ars.getString("alkoot_remarks");
					String resub_just_rem=ars.getString("resub_just_rem")==null?"":ars.getString("resub_just_rem");
					String sys_remarks=ars.getString("sys_remarks")==null?"":ars.getString("sys_remarks");
					String XML_SEQ_ID=ars.getString("XML_SEQ_ID")==null?"":ars.getString("XML_SEQ_ID");
					String clm_autoreject_seq_id=ars.getString("clm_autoreject_seq_id")==null?"":ars.getString("clm_autoreject_seq_id");
					String resub_inter_seq=ars.getString("resub_inter_seq")==null?"":ars.getString("resub_inter_seq");
					String hosp_seq_id=ars.getString("hosp_seq_id")==null?"":ars.getString("hosp_seq_id");
					String added_date=ars.getString("added_date")==null?"":ars.getString("added_date");
	            	
					activities.add(new String[]{internal_code,parent_clm_set_no,service_date,act_type,serv_desc,
							cpt_code,resub_req_amt,quantity,tooth_no,alkoot_remarks,resub_just_rem,sys_remarks,XML_SEQ_ID,clm_autoreject_seq_id,
							resub_inter_seq,hosp_seq_id,added_date});
					
					
			
							
	            }
			
			}
			results[0]=autoRejectedVo;
			results[1]=activities;

			return results;
			
			
			
		}
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
					if (clmrs != null) clmrs.close();
					if (ars != null) ars.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimDAOImpl getClaimDetail()",sqlExp);
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
						log.error("Error while closing the Statement in ClaimDAOImpl getClaimDetail()",sqlExp);
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
							log.error("Error while closing the Connection in ClaimDAOImpl getClaimDetail()",sqlExp);
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
            	ars = null;
            	clmrs=null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}

	public long saveAutoRejectClaimDetails(ClaimUploadErrorLogVO autoRejectedVo) throws TTKException{

		Connection conn = null;
		CallableStatement cStmtObject=null;
		long lngClaimSeqID = 0;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveAutoRejectedClaimDetails);
			
			cStmtObject.setString(1,"CLM");
			cStmtObject.setString(2,autoRejectedVo.getXmlSeqId());
			cStmtObject.setString(3,autoRejectedVo.getHiddenPreviousClamNo());
			cStmtObject.setString(4,autoRejectedVo.getAlkootId());
			cStmtObject.setString(5,autoRejectedVo.getPreviousClaimNo());
			cStmtObject.setString(6,"");
			cStmtObject.setString(7,"");
			cStmtObject.setString(8,"");
			cStmtObject.setString(9,"");
			cStmtObject.setString(10,"");
			cStmtObject.setString(11,"");
			cStmtObject.setString(12,"");
			cStmtObject.setString(13,"");
			cStmtObject.setString(14,"");
			cStmtObject.setLong(15,autoRejectedVo.getUpdatedBy());
			cStmtObject.setString(16,autoRejectedVo.getMemberName());
			cStmtObject.setString(17,"");
			cStmtObject.setString(18,autoRejectedVo.getClaimAction());
			cStmtObject.setString(19,autoRejectedVo.getRejectionReason());
			
			//   cStmtObject.registerOutParameter(1,Types.BIGINT);
			//   cStmtObject.registerOutParameter(78,Types.INTEGER);
		       cStmtObject.execute();
			lngClaimSeqID = 1;
		//	int intRoePro = cStmtObject.getInt(78);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveClaimDetails()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveClaimDetails()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return lngClaimSeqID;
	}

	public long saveAutoRejectClaimActivityDetails(ClaimUploadErrorLogVO autoRejectedVo)  throws TTKException {

		Connection conn = null;
		CallableStatement cStmtObject=null;
		long lngClaimSeqID = 0;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveAutoRejectedClaimDetails);
			
			cStmtObject.setString(1,"ACT");
			cStmtObject.setString(2,autoRejectedVo.getXmlSeqId());
			cStmtObject.setString(3,"");
			cStmtObject.setString(4,"");
			cStmtObject.setString(5,"");
			cStmtObject.setString(6,autoRejectedVo.getAutoRecjectSeqId());
			cStmtObject.setString(7,autoRejectedVo.getServiceDate());
			cStmtObject.setString(8,autoRejectedVo.getActivityType());
			cStmtObject.setString(9,autoRejectedVo.getInternalServiceCode());
			cStmtObject.setString(10,autoRejectedVo.getServiceDescription());
			cStmtObject.setString(11,autoRejectedVo.getCptCode());
			cStmtObject.setString(12,autoRejectedVo.getReSubReqAmnt());
			cStmtObject.setString(13,autoRejectedVo.getQuantity());
			cStmtObject.setString(14,autoRejectedVo.getToothNo());
			cStmtObject.setLong(15,autoRejectedVo.getUpdatedBy());
			cStmtObject.setString(16,"");
			cStmtObject.setString(17,autoRejectedVo.getParentClaimSetlmntNo());
			cStmtObject.setString(18,"");
			cStmtObject.setString(19,"");
			//   cStmtObject.registerOutParameter(1,Types.BIGINT);
			//   cStmtObject.registerOutParameter(78,Types.INTEGER);
		       cStmtObject.execute();
			lngClaimSeqID = 1;
		//	int intRoePro = cStmtObject.getInt(78);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveAutoRejectClaimActivityDetails()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveAutoRejectClaimActivityDetails()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return lngClaimSeqID;
	}

	public int doRevaluationClaim(String xmlseqid, String parentClaimNo,long updatedBy) throws TTKException {

		Connection conn = null;
		CallableStatement cStmtObject=null;
		int outCount = 0;

		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strRevaluateClaimDtls);
			
			cStmtObject.setString(1,xmlseqid);
			cStmtObject.setString(2,parentClaimNo);
			cStmtObject.setLong(3,updatedBy);
			cStmtObject.registerOutParameter(4,Types.BIGINT);
		       cStmtObject.execute();
		       outCount = cStmtObject.getInt(4);
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl saveAutoRejectClaimActivityDetails()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl saveAutoRejectClaimActivityDetails()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return outCount;
	}
	
	
	public ArrayList getClaimsDiscountActReport(ArrayList<Object> alSearchCriteria) throws TTKException{
	//	System.out.println("alSearchCriteria = "+ alSearchCriteria);
		ArrayList<ProviderClaimsVO> alClaimsSearchVOs	=	new ArrayList<ProviderClaimsVO>();
		try(Connection connection=ResourceManager.getConnection();
			CallableStatement callableStatement=connection.prepareCall(strClaimDetailedReportData)){
            callableStatement.setString(1,(String) alSearchCriteria.get(0));//1 TfromDate
            callableStatement.setString(2,(String) alSearchCriteria.get(1));//2 TtoDate
            callableStatement.setString(3,(String) alSearchCriteria.get(2));//3 CfromDate
            callableStatement.setString(4,(String) alSearchCriteria.get(3));//4 CtoDate
            callableStatement.setString(5,(String) alSearchCriteria.get(4));//5 patientName
            callableStatement.setString(6,(String) alSearchCriteria.get(5));//6 status
            callableStatement.setString(7,(String) alSearchCriteria.get(6));//7 invoiceNumber
            callableStatement.setString(8,(String) alSearchCriteria.get(7));//8 batchNo
            callableStatement.setString(9,(String) alSearchCriteria.get(8));//9 memberId
            callableStatement.setString(10,(String) alSearchCriteria.get(9));//10  claimNumber
            callableStatement.setString(11,(String) alSearchCriteria.get(10));//11 benefitType
            callableStatement.setString(12,(String) alSearchCriteria.get(11));//12 refNo
            callableStatement.setString(13,(String) alSearchCriteria.get(12));//13 qatarId
            callableStatement.setString(14,(String) alSearchCriteria.get(13));//14 pay_ref_no
            callableStatement.setString(15,(String) alSearchCriteria.get(14));//15 Hospital_Seq_Id
            callableStatement.setString(16,(String) alSearchCriteria.get(15));//16 report_Tye
            callableStatement.setString(17,(String) alSearchCriteria.get(16));//17 finBatchNo
            callableStatement.setString(18,(String) alSearchCriteria.get(17));//18 financeStatus
            if("".equals((String)alSearchCriteria.get(18)))  //19 partnerNm
            	callableStatement.setString(19,"");			//19 partnerNm
            else
            	callableStatement.setLong(19,new Long((String) alSearchCriteria.get(18)));//19 partnerNm
            
            callableStatement.setString(20,(String) alSearchCriteria.get(19));//20 sort_var
            callableStatement.setString(21,(String) alSearchCriteria.get(20));//21 sort_order
            callableStatement.setString(22,(String) alSearchCriteria.get(21));//22 start_num
            callableStatement.setString(23,(String) alSearchCriteria.get(22));//23 end_num
            callableStatement.registerOutParameter(24,OracleTypes.CURSOR);	  //24 Cursor
            callableStatement.execute();
            try(ResultSet rs = (java.sql.ResultSet)callableStatement.getObject(24)){
                while (rs.next()) {
                	ProviderClaimsVO providerClaimsVO 	=	new ProviderClaimsVO();
                	providerClaimsVO.setBatchNo(TTKCommon.checkNull(rs.getString("BATCH_NO")));
					providerClaimsVO.setInvoiceNo(TTKCommon.checkNull(rs.getString("INVOICE_NUMBER")));
					providerClaimsVO.setAlkootId(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					providerClaimsVO.setPatientName(TTKCommon.checkNull(rs.getString("MEM_NAME")));
                	providerClaimsVO.setClaimNo(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					providerClaimsVO.setBenefitType(TTKCommon.checkNull(rs.getString("BENIFIT_TYPE")));
					providerClaimsVO.setStatus(TTKCommon.checkNull(rs.getString("CLM_STATUS_TYPE_ID")));
                	if(rs.getString("TOT_APPROVED_AMOUNT")!=null)
			        providerClaimsVO.setApprovedAmt(""+rs.getBigDecimal("TOT_APPROVED_AMOUNT"));
					else
					providerClaimsVO.setApprovedAmt("");
	                providerClaimsVO.setClaimPaymentStatus(TTKCommon.checkNull(rs.getString("CLAIM_PAYMENT_STATUS")));
	                providerClaimsVO.setFinanceBatchNo(TTKCommon.checkNull(rs.getString("finance_batch_no")));
                	alClaimsSearchVOs.add(providerClaimsVO); 
                }//end of while(rs.next())
            }
			return alClaimsSearchVOs;
		}catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "claim");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "claim");
		}
	} // end of getClaimsDiscountActReport()

	public PreAuthDetailVO getExceptionFlag(long claimSeqID, String exceptionFlag) throws TTKException {
		String exceptionFlg = "";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		PreAuthDetailVO preauthVo=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetExceptionalFlag);
				cStmtObject.setLong(1,claimSeqID);
			cStmtObject.setString(2,exceptionFlag);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			ResultSet rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(rs!=null){
				while(rs.next()){
					preauthVo=new PreAuthDetailVO();
					
					preauthVo.setExceptionFalg(rs.getString("exception_yn"));
					preauthVo.setExceptionalWarningMsg(rs.getString("exception_warning"));
				}
			}
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
			try // First try closing the Statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in ClaimDAOImpl getExceptionFlag()",sqlExp);
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
						log.error("Error while closing the Connection in ClaimDAOImpl getExceptionFlag()",sqlExp);
						throw new TTKException(sqlExp, "claim");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "claim");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return preauthVo;
	}

	public TTKReportDataSource getDetailedResubmissionClaimDetails(String parameter)throws TTKException {

		
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		OracleCachedRowSet crs = null;
	    TTKReportDataSource reportDataSource =null;
	    
	    try {
	    	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReSubClaimReportDetails);
			cStmtObject.setString(1,parameter);
			 cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
					throw new TTKException(sqlExp, "claim");
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
							log.error("Error while closing the Connection in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return reportDataSource;

	}

	public TTKReportDataSource getOverAllBatchResubmissionDeatils(String parameter)throws TTKException {

		
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		OracleCachedRowSet crs = null;
	    TTKReportDataSource reportDataSource =null;
	    
	    try {
	    	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReSubClaimReportDetails);
			cStmtObject.setString(1,parameter);
			 cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
					throw new TTKException(sqlExp, "claim");
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
							log.error("Error while closing the Connection in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return reportDataSource;

	}

	public TTKReportDataSource getResubmissionErrorLogDetails(String parameter)throws TTKException {

		
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		OracleCachedRowSet crs = null;
	    TTKReportDataSource reportDataSource =null;
	    
	    try {
	    	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strReSubClaimReportDetails);
			cStmtObject.setString(1,parameter);
			 cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	        cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
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
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
					throw new TTKException(sqlExp, "claim");
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
							log.error("Error while closing the Connection in ClaimBatchDAOImpl getAutoRejectClaimBatchReport()",sqlExp);
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
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return reportDataSource;

	}
	
}//end of ClaimDAOImpl
