/**
 * @ (#) PreAuthManagerBean.java Apr 10, 2006
 * Project 	     : TTK HealthCare Services
 * File          : PreAuthManagerBean.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Apr 10, 2006
 *
 * @author       :  RamaKrishna K M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.business.preauth;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import org.dom4j.Document;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.onlineforms.OnlineAccessDAOImpl;
import com.ttk.dao.impl.preauth.PreAuthDAOFactory;
import com.ttk.dao.impl.preauth.PreAuthDAOImpl;
import com.ttk.dao.impl.preauth.PreAuthMedicalDAOImpl;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.MOUDocumentVO;
import com.ttk.dto.administration.ReportVO;
import com.ttk.dto.claims.AdditionalHospitalDetailVO;
import com.ttk.dto.common.DhpoWebServiceVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.AuthorizationVO;
import com.ttk.dto.preauth.BalanceCopayDeductionVO;//added as per KOC 1140 and 1142(1165)
import com.ttk.dto.preauth.ClaimantVO;
import com.ttk.dto.preauth.ClinicianDetailsVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.ICDPCSVO;
import com.ttk.dto.preauth.InsOverrideConfVO;
import com.ttk.dto.preauth.ObservationDetailsVO;
import com.ttk.dto.preauth.PreAuthAilmentVO;
import com.ttk.dto.preauth.PreAuthAssignmentVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthMedicalVO;
import com.ttk.dto.preauth.PreAuthTariffVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.SIInfoVO;
import com.ttk.dto.preauth.ShortfallVO;
import com.ttk.dto.preauth.UserAssignVO;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class PreAuthManagerBean implements PreAuthManager{

    private PreAuthDAOFactory preAuthDAOFactory = null;
    private PreAuthDAOImpl preAuthDAO = null;
    private PreAuthMedicalDAOImpl preAuthMedicalDAO = null;  

    /**
     * This method returns the instance of the data access object to initiate the required tasks
     * @param strIdentifier String object identifier for the required data access object
     * @return BaseDAO object
     * @exception throws TTKException
     */
    private BaseDAO getPreAuthDAO(String strIdentifier) throws TTKException
    {
        try
        {
            if (preAuthDAOFactory == null)
                preAuthDAOFactory = new PreAuthDAOFactory();
            if(strIdentifier!=null)
            {
                return preAuthDAOFactory.getDAO(strIdentifier);
            }//end of if(strIdentifier!=null)
            else
                return null;
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "error."+strIdentifier+".dao");
        }//end of catch(Exception exp)
    }//End of getPreAuthDAO(String strIdentifier)

    /**
     * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
     * @exception throws TTKException
     */
    public ArrayList getPreAuthList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPreAuthList(alSearchCriteria);
    }//end of getPreAuthList(ArrayList alSearchCriteria)
    
    /**
     * This method returns the Arraylist of PreAuthVO's  which contains Partner Pre-Authorization Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
     * @exception throws TTKException
     */
    public ArrayList getPartnerPreAuthList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPartnerPreAuthList(alSearchCriteria);
    }//end of getPartnerPreAuthList(ArrayList alSearchCriteria)
    
    /**
     * This method returns the HashMap of String,String  which contains Network Types details
     * @return HashMap of String object which contains Network Types details
     * @exception throws TTKException
     */
    public HashMap<String,String> getNetworkTypeList() throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getNetworkTypeList();
    }//end of getNetworkTypeList()
    
    /**
     * This method returns the HashMap of String,String  which contains Network Types details
     * @return HashMap of String object which contains Network Types details
     * @exception throws TTKException
     */
    public HashMap<String,String> getPartnersList() throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPartnersList();
    }//end of getNetworkTypeList()
    
  
    /**
     * This method returns the Arraylist of PreAuthVO's  which contains Pre-Authorization Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Pre-Authorization details
     * @exception throws TTKException
     */
    public ArrayList getActivityCodeList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getActivityCodeList(alSearchCriteria);
    }//end of getActivityCodeList(ArrayList alSearchCriteria)
    public ArrayList getPreAuthEnhancementList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPreAuthEnhancementList(alSearchCriteria);
    }//end of getPreAuthEnhancementList(ArrayList alSearchCriteria)
    
    public ArrayList getDiagnosisCodeList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getDiagnosisCodeList(alSearchCriteria);
    }//end of getDiagnosisCodeList(ArrayList alSearchCriteria)
    public ArrayList getProviderList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getProviderList(alSearchCriteria);
    }//end of getProviderList(ArrayList alSearchCriteria)
    public ArrayList getMemberList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getMemberList(alSearchCriteria);
    }//end of getProviderList(ArrayList alSearchCriteria)
    public ArrayList getClinicianList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getClinicianList(alSearchCriteria);
    }//end of getClinicianList(ArrayList alSearchCriteria)
    public ArrayList getProviderClinicianList(ArrayList alSearchCriteria) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getProviderClinicianList(alSearchCriteria);
    }//end of getClinicianList(ArrayList alSearchCriteria)
    public ClinicianDetailsVO getProviderClinicianDetails(String clinicianId,Long hospSeqId) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getProviderClinicianDetails(clinicianId,hospSeqId);
    }//end of getClinicianList(ArrayList alSearchCriteria)
    
    public ActivityDetailsVO getActivityCodeTariff(String activityCode,String preAuthSeqID,String activityStartDate,String authType,String benefitType,String searchType) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getActivityCodeTariff(activityCode,preAuthSeqID,activityStartDate,authType,benefitType,searchType);
    }//end of getActivityCodeTariff(ArrayList alSearchCriteria)


    /**
     * This method returns the PreAuthDetailVO, which contains all the Pre-Authorization details
     * @param preauthVO Object which contains seq id's to get the Pre-Authorization Details
     * @param strSelectionType String object which contains Selection Type - 'PAT' or 'ICO'
     * @return PreAuthDetailVO object which contains all the Pre-Authorization details
     * @exception throws TTKException
     */
    
    public PreAuthDetailVO getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType) throws TTKException{
         preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.getPreAuthDetail(preauthVO,strSelectionType);
    }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
    public Object[] getPreAuthDetails(Long preAuthSeqId) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPreAuthDetails(preAuthSeqId);
   }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
    public int[] getPreAuthAndClmCount(Long memberSeqId) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPreAuthAndClmCount(memberSeqId);
   }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
    public Object[] getPartnerPreAuthDetails(Long preAuthSeqId) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPartnerPreAuthDetails(preAuthSeqId);
   }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
    public Document getPreAuthHistory(Long lngSeqId) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPreAuthHistory(lngSeqId);
    }//end of getPreAuthHistory(ArrayList alSearchCriteria)
    public Document getClaimHistory(Long lngSeqId) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getClaimHistory(lngSeqId);
    }//end of getClaimHistory(ArrayList alSearchCriteria)
    public ArrayList<String[]> getDiagnosisDetails(Long preAuthSeqId) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getDiagnosisDetails(preAuthSeqId);
   }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
   
    public DiagnosisDetailsVO getIcdCodeDetails(String icdcode,long preauthSeqId,String authType) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getIcdCodeDetails(icdcode,preauthSeqId,authType);
   }//end of getIcdCodeDetails(PreAuthVO preauthVO,String strSelectionType)
    public DiagnosisDetailsVO getDiagnosis(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getDiagnosis(diagnosisDetailsVO);
   }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
    public Object[] saveObservationDetails(ObservationDetailsVO observationDetailsVO) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.saveObservationDetails(observationDetailsVO);
   }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
    /**
     * This method saves the Pre-Authorization Shortfall information
     * @param shortfallVO ShortfallVO contains Pre-Authorization Shortfall information
     * @param strSelectionType
     * @return long value which contains Shortfall Seq ID
     * @exception throws TTKException
     */
    public long saveShortfall(ShortfallVO shortfallVO,String strSelectionType) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.saveShortfall(shortfallVO,strSelectionType);
    }//end of saveShortfall(ShortfallVO shortfallVO,String strSelectionType)

   
     public ShortfallVO getShortfallDetail(ArrayList alShortfallList) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.getShortfallDetail(alShortfallList);
      }//end of getShortfallDetail(ArrayList alShortfallList)/**
     
     public ArrayList<String[]> getPreauthHistoryList(Long memberSeqID,String authType) throws TTKException{
     	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.getPreauthHistoryList(memberSeqID,authType);
     }//end of saveShortfall(ShortfallVO shortfallVO,String strSelectionType)
    
     public ArrayList<String[]> getPreauthHistoryWithBenefitsList(Long memberSeqID,String authType,String memberId,Long policySeqId,String benefitType,String sortBy,String ascOrDesc) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPreauthHistoryWithBenefitsList(memberSeqID,authType,memberId,policySeqId,benefitType,sortBy,ascOrDesc);
    }//end of saveShortfall(ShortfallVO shortfallVO,String strSelectionType)
    public ObservationDetailsVO getObservDetail(Long observSeqId) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getObservDetail(observSeqId);
   }//end of getObservDetails(PreAuthVO preauthVO,String strSelectionType)
    public Object[] getObservTypeDetails(String observType) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getObservTypeDetails(observType);
   }//end of getObservDetails(PreAuthVO preauthVO,String strSelectionType)
    
    
    public long deleteObservDetails(Long preauthSeqId,String listOfobsvrSeqIds,String mode) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.deleteObservDetails(preauthSeqId,listOfobsvrSeqIds,mode);
   }//end of getObservDetails(PreAuthVO preauthVO,String strSelectionType)
    public ArrayList<String[]> getAllObservDetails(Long activityDtlSeqId) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getAllObservDetails(activityDtlSeqId);
   }//end of getObservDetails(PreAuthVO preauthVO,String strSelectionType)
    /**
     * This method saves the Pre-Authorization information
     * @param preAuthDetailVO PreAuthDetailVO contains Pre-Authorization information
     * @param strSelectionType String object which contains Selection Type - 'PAT' or 'ICO'
     * @return Object[] the values,of  MEMBER_SEQ_ID , PAT_GEN_DETAIL_SEQ_ID and PAT_ENROLL_DETAIL_SEQ_ID
     * @exception throws TTKException
     */
    public Object[] savePreAuth(PreAuthDetailVO preAuthDetailVO,String status) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.savePreAuth(preAuthDetailVO,status);
    }//end of savePreAuth(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
    public Object[] saveDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.saveDiagnosisDetails(diagnosisDetailsVO);
    }//end of saveDiagnosisDetails(PreAuthDetailVO preAuthDetailVO)
    public Object[] deleteDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.deleteDiagnosisDetails(diagnosisDetailsVO);
    }//end of deleteDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO)
    public int deleteShortfallsDetails(String[] shortfallsDeatils) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.deleteShortfallsDetails(shortfallsDeatils);
    }//end of deleteShortfallsDetails(DiagnosisDetailsVO diagnosisDetailsVO)
    public Object[] deleteActivityDetails(long preauthSeqId,long activityDtlSeqId,String authType,long userSeqId) throws TTKException{
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.deleteActivityDetails(preauthSeqId,activityDtlSeqId,authType,userSeqId);
    }//end of deleteActivityDetails(DiagnosisDetailsVO diagnosisDetailsVO)
    public String[] addPreauthDetails(PreAuthDetailVO preAuthDetailVO,String uplodedBy) throws TTKException{
    	
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.addPreauthDetails(preAuthDetailVO,uplodedBy);
     }//end of savePreAuth(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
    public Object[] saveActivityDetails(ActivityDetailsVO activityDetailsVO) throws TTKException{    	
   	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.saveActivityDetails(activityDetailsVO);
    }//end of savePreAuth(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
    public ActivityDetailsVO getActivityDetails(Long activityDtlSeqId) throws TTKException{    	
      	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
           return preAuthDAO.getActivityDetails(activityDtlSeqId);
       }//end of savePreAuth(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
    public BigDecimal[] getCalculatedPreauthAmount(Long preAuthSeqId,Long hospitalSeqID, Long userSeqId) throws TTKException{    	
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getCalculatedPreauthAmount(preAuthSeqId,hospitalSeqID,userSeqId);
    }//end of savePreAuth(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
    public Object[] saveAndCompletePreauth(PreAuthDetailVO  preAuthDetailVO) throws TTKException{    	
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.saveAndCompletePreauth(preAuthDetailVO);
    }//end of preAuthAmountApproved(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
    public Map<String,String> getOtpresult(String memberId,Long addedBy,String otpNumber)throws Exception{
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.getOtpResult(memberId,addedBy,otpNumber);     
    }
    public Map<String,String> getEncounterTypes(String benefitId)throws Exception{
   	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getEncounterTypes(benefitId);     
   }
   
    
	@Override	
    /**
	 * This method will allow to Override the Preauth information
	 * @param lngPATGenDetailSeqID PATGenDetailSeqID
	 * @param lngPATEnrollDtlSeqID PATEnrollDtlSeqID
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
    public PreAuthDetailVO overridePreauth(long lngPATGenDetailSeqID,long lngPATEnrollDtlSeqID,long lngUserSeqID) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.overridePreauth(lngPATGenDetailSeqID,lngPATEnrollDtlSeqID,lngUserSeqID);
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
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.saveReview(preauthDetailVO,strMode,strType);
    }//end of saveReview(PreAuthDetailVO preauthDetailVO,String strMode,String strType)

    /**
     * This method returns the ArrayList object, which contains all the Users for the Corresponding TTK Branch
     * @param alAssignUserList ArrayList Object contains PreauthSeqID/ClaimSeqID,PolicySeqID and TTKBranch
     * @param strMode contains PAT/CLM for identifying the Pre-Authorization/Claims flow
     * @return ArrayList object which contains all the Users for the Corresponding TTK Branch
     * @exception throws TTKException
     */
    public ArrayList getAssignUserList(ArrayList alAssignUserList,String strMode) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getAssignUserList(alAssignUserList,strMode);
    }//end of getAssignUserList(ArrayList alAssignUserList,String strMode)

    /**
     * This method assigns the Pre-Authorization information to the corresponding Doctor
     * @param preAuthAssignmentVO PreAuthAssignmentVO which contains Pre-Authorization information to assign the corresponding Doctor
     * @return long value which contains Assign Users Seq ID
     * @exception throws TTKException
     */
    public long assignPreAuth(PreAuthAssignmentVO preAuthAssignmentVO, String strModeValue) throws TTKException {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.assignPreAuth(preAuthAssignmentVO,strModeValue);
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
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getAssignTo(lngAssignUsersSeqID,lngUserSeqID,strMode);
    }//end of getAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode)

    /**
     * This method returns the Arraylist of ClaimantVO's  which contains Claimant details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimantVO object which contains Claimant details
     * @exception throws TTKException
     */
    public ArrayList getClaimantList(ArrayList alSearchCriteria) throws TTKException {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getClaimantList(alSearchCriteria);
    }//end of getClaimantList(ArrayList alSearchCriteria)

    /**
     * This method returns the Arraylist of EnhancementVO's  which contains SumInsured Enhancement details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of EnhancementVO object which contains SumInsured Enhancement details
     * @exception throws TTKException
     */
    public ArrayList getSumInsuredEnhancementList(ArrayList alSearchCriteria) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getSumInsuredEnhancementList(alSearchCriteria);
    }//end of getSumInsuredEnhancementList(ArrayList alSearchCriteria)

    /**
     * This method returns the PreAuthMedicalVO, which contains all the Pre-Authorization Medical Details
     * @param lngPATGenDetailSeqID long value which contains pre-authseq id to get the Pre-Authorization Medical Details
     * @param lngUserSeqID long value which contains Logged-in User
     * @return PreAuthMedicalVO object which contains all the Pre-Authorization Medical details
     * @exception throws TTKException
     */
    public PreAuthMedicalVO getMedicalDetail(long lngPATGenDetailSeqID,long lngUserSeqID) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
        return preAuthMedicalDAO.getMedicalDetail(lngPATGenDetailSeqID,lngUserSeqID);
     }//end of getMedicalDetail(long lngPATGenDetailSeqID,long lngUserSeqID)

    /**
     * This method returns the PreAuthMedicalVO, which contains all the Claims Medical Details
     * @param lngClaimSeqID long value which contains claimseq id to get the Claims Medical Details
     * @param lngUserSeqID long value which contains Logged-in User
     * @return PreAuthMedicalVO object which contains all the Claims Medical details
     * @exception throws TTKException
     */
    public PreAuthMedicalVO getClaimMedicalDetail(long lngClaimSeqID,long lngUserSeqID) throws TTKException{
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getClaimMedicalDetail(lngClaimSeqID,lngUserSeqID);
    }//end of getClaimMedicalDetail(long lngClaimSeqID,long lngUserSeqID)
    
    /**
     * This method returns the String Object, which contains Doctor Opinion
     * @param lngClaimSeqID long value which contains claimseq id to get the Doctor Opinion Information
     * @return String Object, which contains Doctor Opinion
     * @exception throws TTKException
     */
    public String getDoctorOpinion(long lngClaimSeqID) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getDoctorOpinion(lngClaimSeqID);
    }//end of getDoctorOpinion(long lngClaimSeqID)
    
    /**
     * This method saves the Doctor Opinion Information
     * @param lngClaimSeqID long value which contains claimseq id to save the Doctor Opinion Information
     * @param strDoctorOpinion String Object which contains Doctor Opinion Information
     * @param lngUserSeqID long value which contains Logged-in User
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int saveDoctorOpinion(long lngClaimSeqID,String strDoctorOpinion,long lngUserSeqID) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.saveDoctorOpinion(lngClaimSeqID,strDoctorOpinion,lngUserSeqID);
    }//end of saveDoctorOpinion(long lngClaimSeqID,String strDoctorOpinion,long lngUserSeqID)

    /**
     * This method returns the AdditionalHospitalDetailVO, which contains all the Claim Hospital Additional Details
     * @param lngClaimHospAssocSeqID long value which contains claim hosp assoc seq id to get the Claim Hospital Additional Details
     * @param lngUserSeqID long value which contains Logged-in User
     * @return AdditionalHospitalDetailVO object which contains all the Claim Hospital Additional details
     * @exception throws TTKException
     */
    public AdditionalHospitalDetailVO getAdditionalHospitalDetail(long lngClaimHospAssocSeqID,long lngUserSeqID) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getAdditionalHospitalDetail(lngClaimHospAssocSeqID,lngUserSeqID);
    }//end of getAdditionalHospitalDetail(long lngClaimHospAssocSeqID,long lngUserSeqID)

    /**
     * This method saves the Claim Hospital Additional information
     * @param addHospitalDetailVO AdditionalHospitalDetailVO contains Claim Hospital Additional
     * @return long value which contains Add Hospital Dtl Seq ID
     * @exception throws TTKException
     */
    public long saveAdditionalHospitalDetail(AdditionalHospitalDetailVO addHospitalDetailVO) throws TTKException{
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.saveAdditionalHospitalDetail(addHospitalDetailVO);
    }//end of saveAdditionalHospitalDetail(AdditionalHospitalDetailVO addHospitalDetailVO)

	//added for KOC-Decoupling
    /**
     * This method returns the ArrayList, which contains Diagnosis list Details
     * @param alDiagnosisList which contains PAT_GEN_DETAIL_SEQ_ID,CLAIM_SEQ_ID to get the Diagnosis Details
     * @return ArrayList object which contains Diagnosis Details
     * @exception throws TTKException
     */
    public ArrayList getDiagnosisList(ArrayList alDiagnosisList) throws TTKException
    {    	
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getDiagnosisList(alDiagnosisList);
    }
    /**This Method returns PreAuthAilmentVO, which contains all the Ailment Details
     * @param lngDiagSeqId value which contains Diagnosis Seq Id
     * @exception throws TTKException
     */
    public PreAuthAilmentVO getDiagnosisDetail(long lngDiagSeqId) throws TTKException
    {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getDiagnosisDetail(lngDiagSeqId);    	
    }
	
    /**
     * This method returns the PreAuthMedicalVO, which contains all the Pre-Authorization Ailment Details
     * @param lngSeqID long value which contains pre-authseq id/ClaimSEQID to get the Pre-Authorization/Claims Ailment Details
     * @param lngUserSeqID long value which contains Logged-in User seq id
     * @param strMode String Object contains mode for identifying Pre-Authorization/Claims - PAT/CLM
     * @return PreAuthAilmentVO object which contains all the Pre-Authorization/Claims Ailment and Associated Illness details
     * @exception throws TTKException
     */
    public PreAuthAilmentVO getAilmentDetail(long lngSeqID,long lngUserSeqID,String strMode) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
        return preAuthMedicalDAO.getAilmentDetail(lngSeqID,lngUserSeqID,strMode);
     }//end of getAilmentDetail(long lngSeqID,long lngUserSeqID,String strMode)

    /**
     * This method saves the Pre-Authorization Ailment information
     * @param preAuthAilmentVO PreAuthAilmentVO contains Pre-Authorization Ailment information and Associated Illness information
     * @return long value which contains Ailment Seq ID
     * @exception throws TTKException
     */
    public long saveAilment(PreAuthAilmentVO preAuthAilmentVO) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
        return preAuthMedicalDAO.saveAilment(preAuthAilmentVO);
    }//end of saveAilment(PreAuthAilmentVO preAuthAilmentVO)

	/**Added for KOC-Decoupling
     * This method saves the diagnosis Details
     * @param preAuthAilmentVo PreAuthAilmentVO contains Ailment information
     * @return long value which contains Diagnosis Seq ID
     * @Exception throws TTKException
     * 
     */
    public long saveDiagnosis(PreAuthAilmentVO preAuthAilmentVO)throws TTKException
    {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.saveDiagnosis(preAuthAilmentVO);
    }    
	
    /**
     * This method returns the ICDPCSVO, which contains all the ICD Package Details
     * @param lngIcdPcsSeqID long value which contains seq id to get the ICD/PCS Details
     * @return ICDPCSVO object which contains all the ICD/PCS Details
     * @exception throws TTKException
     */
    public ICDPCSVO getICDPCSDetail(long lngIcdPcsSeqID,long lngUserSeqID) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
        return preAuthMedicalDAO.getICDPCSDetail(lngIcdPcsSeqID,lngUserSeqID);
    }//end of getICDPCSDetail(long lngPEDSeqID,long lngUserSeqID)

    /**
     * This method saves the Pre-Authorization ICD Package information
     * @param icdPCSVO ICDPCSVO contains ICD Package information
     * @param strMode String contains Mode for Pre-Authorization/Claims as - PAT / CLM
     * @return long value which contains ICDPCS Seq ID
     * @exception throws TTKException
     */
    public long saveICDPackage(ICDPCSVO icdPCSVO,String strMode) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
        return preAuthMedicalDAO.saveICDPackage(icdPCSVO,strMode);
    }//end of saveICDPackage(ICDPCSVO icdPCSVO,String strMode)

    /**
     * This method deletes the ICD Package information from the database
     * @param alICDPackage arraylist which the the details of the ICD Package has to be deleted
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int deleteICDPackage(ArrayList alICDPackage) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
        return preAuthMedicalDAO.deleteICDPackage(alICDPackage);
    }//end of deleteICDPackage(ArrayList alICDPackage)

	/**added for KOC-Decoupling
     * This method deletes the selected diagnosis information from the database  
     * @param DiagnosisSeqId which the details of the diagnosis to be deleted
     * @return int the value which returns greater than zero for successful execution of the task
     * @exception throws TTKException
     */
    public int deleteDiagnosis(long deleteSeqId)throws TTKException
    {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.deleteDiagnosis(deleteSeqId);
    }  
    
    /**
     * This method returns the AuthorizationVO, which contains Authorization Details
     * @param alAuthList ArrayList object which contains MemberSeqID,PAT_GEN_DETAIL_SEQ_ID,PAT_ENROLL_DETAIL_SEQ_ID and USER_SEQ_ID to get the Authorization Details
     * @param strIdentifier String which contains module identifer Pre-Authorization/Claims
     * @return AuthorizationVO object which contains Authorization Details
     * @exception throws TTKException
     */
    public AuthorizationVO getAuthorizationDetail(ArrayList alAuthList,String strIdentifier) throws TTKException {
         preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.getAuthorizationDetail(alAuthList,strIdentifier);
     }//end of getAuthorizationDetail(ArrayList alAuthList)

    /**
     * This method saves the Authorization information
     * @param authorizationVO AuthorizationVO contains Authorization information
     * @param strIdentifier Pre-Authorization/Claims
     * @return long int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int saveAuthorization(AuthorizationVO authorizationVO,String strIdentifier) throws TTKException {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.saveAuthorization(authorizationVO,strIdentifier);
    }//end of saveAuthorization(AuthorizationVO authorizationVO,String strIdentifier)
  /**
   *bajaj
    * This method saves the intimation information
    * @param authorizationVO AuthorizationVO contains Authorization information
    * @param strIdentifier Pre-Authorization/Claims
    * @return long int the value which returns greater than zero for succcesssful execution of the task
    * @exception throws TTKException
    */
   public int sendInsIntimate(AuthorizationVO authorizationVO,String strIdentifier) throws TTKException {
       preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
       return preAuthDAO.sendInsIntimate(authorizationVO,strIdentifier);
   }//end of saveAuthorization(AuthorizationVO authorizationVO,String strIdentifier)

    /**
     * This method returns the PreAuthTariffVO, which contains Tariff Details
     * @param alTariffList ArrayList contains PAT_GEN_DETAIL_SEQ_ID,Enroll Dtl Seq ID,logged-in User Seq ID and Policy Seq ID
     * @return PreAuthTariffVO object which contains Tariff Details
     * @exception throws TTKException
     */
    public PreAuthTariffVO getTariffDetail(ArrayList alTariffList) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getTariffDetail(alTariffList);
    }//end of getTariffDetail(ArrayList alTariffList)

    /**
     * This method returns the PreAuthTariffVO, which contains Tariff Details
     * @param alTariffList ArrayList contains PAT_GEN_DETAIL_SEQ_ID,Enroll Dtl Seq ID,logged-in User Seq ID and Policy Seq ID
     * @return PreAuthTariffVO object which contains Tariff Details
     * @exception throws TTKException
     */
    public PreAuthTariffVO getPATariffDetail(ArrayList alTariffList) throws TTKException{
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getPATariffDetail(alTariffList);
    }//end of getPATariffDetail(ArrayList alTariffList)

    /**
     * This method saves the Tariff information
     * @param preAuthTariffVO PreAuthTariffVO contains Tariff information
     * @return long value contains Tariff Header Seq ID
     * @exception throws TTKException
     */
    public long saveTariffDetail(PreAuthTariffVO preAuthTariffVO) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.saveTariffDetail(preAuthTariffVO);
    }//end of saveTariffDetail(PreAuthTariffVO preAuthTariffVO)

    /**
     * This method Calculates the Tariff information
     * @param lngPreauthSeqID contains PAT_GEN_DETAIL_SEQ_ID
     * @param lngUserSeqID long value which contains Logged-in User seq id
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int calculateTariff(long lngPreauthSeqID,long lngUserSeqID) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.calculateTariff(lngPreauthSeqID,lngUserSeqID);
    }//end of calculateTariff(long lngPreauthSeqID,long lngUserSeqID)

    /**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 * 
	 */
  
    
	public int deletePATGeneral(ArrayList alDeleteList) throws TTKException {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.deletePATGeneral(alDeleteList);
	}//end of deletePATGeneral(ArrayList alDeleteList)

	/**
     * This method returns the ArrayList, which contains Discrepancy Details
     * @param alDiscrepancyList lwhich contains PAT_GEN_DETAIL_SEQ_ID,CLAIM_SEQ_ID,USER_SEQ_ID to get the Discrepancy Details
     * @return ArrayList object which contains Discrepancy Details
     * @exception throws TTKException
     */
	public ArrayList getDiscrepancyInfo(ArrayList alDiscrepancyList) throws TTKException{
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.getDiscrepancyInfo(alDiscrepancyList);
	}//end of getDiscrepancyInfo(ArrayList alDiscrepancyList)

	/**
     * This method resolves the Discrepancy information
     * @param alDiscrepancyVO ArrayList contains Discrepancy information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
	public int resolveDiscrepancy(ArrayList alDiscrepancyVO) throws TTKException {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.resolveDiscrepancy(alDiscrepancyVO);
	}//end of resolveDiscrepancy(ArrayList alDiscrepancyVO)
	
	/**
     * This method returns the ArrayList, which contains AccountHeads
     * @param lngPreauthSeqID which contains PAT_GEN_DETAIL_SEQ_ID to get the AccountHeads
     * @return ArrayList object which contains AccountHeads Details
     * @exception throws TTKException
     */
	public ArrayList getAccountHeadInfo(long lngPreauthSeqID) throws TTKException {
		preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
		return preAuthMedicalDAO.getAccountHeadInfo(lngPreauthSeqID);
	}//end of getAccountHeadInfo(long lngPreauthSeqID)
	
	/**
     * This method returns the HashMap, which contains AccountHeads
     * @param lngPreauthSeqID which contains PAT_GEN_DETAIL_SEQ_ID to get the AccountHeads
     * @return HashMap object which contains AccountHeads Details
     * @exception throws TTKException
     */
	public HashMap getAccountHeadInfoList(long lngPreauthSeqID) throws TTKException {
		preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
		return preAuthMedicalDAO.getAccountHeadInfoList(lngPreauthSeqID);
	}//end of getAccountHeadInfoList(long lngPreauthSeqID)
	
	/**
     * This method saves the Tariff information
     * @param lngPreauthSeqID contains PAT_GEN_DETAIL_SEQ_ID
     * @param strSelectedSeqID contains Concatenated Selected Seq ID's
     * @param lngUserSeqID long value which contains Logged-in User seq id
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int saveSelectedAccountHeads(long lngPreauthSeqID,String strSelectedSeqID,long lngUserSeqID) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.saveSelectedAccountHeads(lngPreauthSeqID,strSelectedSeqID,lngUserSeqID);
    }//end of saveSelectedAccountHeads(long lngPreauthSeqID,String strSelectedSeqID,long lngUserSeqID)
	
	/**
     * This method returns the PreAuthTariffVO, which contains Tariff Details
     * @param alTariffList ArrayList contains PAT_GEN_DETAIL_SEQ_ID,Enroll Dtl Seq ID,logged-in User Seq ID and Policy Seq ID
     * @return PreAuthTariffVO object which contains Tariff Details
     * @exception throws TTKException
     */
    public PreAuthTariffVO getAccountHeadTariffDetail(ArrayList alTariffList) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.getAccountHeadTariffDetail(alTariffList);
    }//end of getAccountHeadTariffDetail(ArrayList alTariffList)
    
    /**
     * This method saves the Tariff information
     * @param preAuthTariffVO PreAuthTariffVO contains Tariff information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int saveAccountHeadTariffDetail(PreAuthTariffVO preAuthTariffVO) throws TTKException {
    	preAuthMedicalDAO = (PreAuthMedicalDAOImpl)this.getPreAuthDAO("medical");
    	return preAuthMedicalDAO.saveAccountHeadTariffDetail(preAuthTariffVO);
    }//end of saveAccountHeadTariffDetail(PreAuthTariffVO preAuthTariffVO)
    
    /**
	 * This method will save the Review Information and Workflow will change to Coding
	 * @param preauthDetailVO the object which contains the Review Details which has to be  saved
	 * @param strMode String object which contains Mode Pre-authorization/Claims - PAT/CLM
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
    public PreAuthDetailVO returnToCoding(PreAuthDetailVO preauthDetailVO,String strMode) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.returnToCoding(preauthDetailVO,strMode);
    }//end of returnToCoding(PreAuthDetailVO preauthDetailVO,String strMode)
    
    /**
	 * This method reassign the enrollment ID
	 * @param alReAssignEnrID the arraylist of details for which have to be reassigned
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int reAssignEnrID(ArrayList alReAssignEnrID) throws TTKException 
	{
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.reAssignEnrID(alReAssignEnrID);
	}//end of reAssignEnrID(ArrayList alReAssignEnrID)
	
	/**
     * This method returns the Arraylist of ClaimantVO's  which contains Claimant details
     * @param lngMemSeqID String object which contains the search criteria
     * @return ArrayList of ClaimantVO object which contains Claimant details
     * @exception throws TTKException
     */
    public ClaimantVO getSelectEnrollmentID(Long lngMemSeqID) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getSelectEnrollmentID(lngMemSeqID);
    }//end of getSelectEnrollmentID(Long lngMemSeqID)
    
    /**
     * This method returns the Arraylist of ClaimantVO's  which contains Claimant details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimantVO object which contains Claimant details
     * @exception throws TTKException
     */
    public ArrayList getEnrollmentList(ArrayList alSearchCriteria) throws TTKException{
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getEnrollmentList(alSearchCriteria);
    }//end of getEnrollmentList(ArrayList alSearchCriteria) 
    
    /**
     * This method returns the SIInfoVO, which contains Balance SI Details.
     * @param lngMemSeqID long value which contains member id to get the Balance SI Details
     * @param lngPolicyGrpSeqID long value which contains Policy Group Seq ID
     * @param lngBalanceSeqID long value which contains Balance Seq ID
     * @return PreAuthMedicalVO object which contains Balance SI Details
     * @exception throws TTKException
     */
    public SIInfoVO getBalanceSIDetail(long lngMemSeqID,long lngPolicyGrpSeqID,long lngBalanceSeqID) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getBalanceSIDetail(lngMemSeqID,lngPolicyGrpSeqID,lngBalanceSeqID);
    }//end of getBalanceSIDetail(long lngMemSeqID,long lngPolicyGrpSeqID,long lngBalanceSeqID)
	

    /**
     * This method returns the BalanceCopayDeductionVO, which contains Copay Adviced SI Details.
     * @param strIdentifier specifies module
     * @param lngPatGenDtlSeqID long value which contains Preauthorization general detail Seq id
     * @param lngMemSeqID long value which contains Member Seq ID
     * @param lngPolicyGrpSeqID long value which contains Policy Group SeQ Id
     * @param lngBalanceSeqID long value which contains BAlance Seq ID
     * @return PreAuthMedicalVO object which contains Balance SI Details
     * @exception throws TTKException
	 * //Changes As per KOC 1142(Copay restriction) ChangeRequest
     */
      public BalanceCopayDeductionVO getcopayAdviced(String strIdentifier,long lngPatGenDtlSeqID,long lngMemSeqID,long lngPolicyGrpSeqID,long lngBalanceSeqID) throws TTKException
      {
    	  preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
      	return preAuthDAO.getcopayAdviced(strIdentifier,lngPatGenDtlSeqID,lngMemSeqID,lngPolicyGrpSeqID,lngBalanceSeqID);
      }
	  
	  /**1274A
    * This method saves the File uploaded for unfreeze preauth status
    * @param ArrayList alFileAUploadList contains Save information
    * returns long value
    * @exception throws TTKException
    */
	      public long saveUpoadedFilename(ArrayList alFileAUploadList) throws TTKException
      {
    	  preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        	return preAuthDAO.saveUpoadedFilename(alFileAUploadList);
      }

    /**1274A
    * This method saves the File uploaded for unfreeze preauth status
    * @param ArrayList alUnfreezeList contains Save information
    * returns long value
    * @exception throws TTKException
    */
     public int accountUnfreeze(ArrayList alUnfreezeList) throws TTKException
      {
          preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
          return preAuthDAO.accountUnfreeze(alUnfreezeList);
      }//end of accountUnfreeze(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
     
     public int accountinsOverrideConf(ArrayList alUnfreezeList) throws TTKException
     {
         preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.accountinsOverrideConf(alUnfreezeList);
     }//end of accountUnfreeze(PreAuthDetailVO preAuthDetailVO,String strSelectionType)
    
   
     
     public InsOverrideConfVO getConfigDetails(long lngProdPolicySeqId, String Mode) throws TTKException{
    	  preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
     	 return preAuthDAO.getConfigDetails(lngProdPolicySeqId,Mode);
     }//end of getConfigCopayAmt(long lngProdPolicySeqId)
     
     
     public int saveOverrideConfigDetails(InsOverrideConfVO insOverrideConfVO)throws TTKException
     {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
     return preAuthDAO.saveOverrideConfigDetails(insOverrideConfVO);
     }
     
     /**
      * This method get the DHPO upload preauth details  
      * @param PreAuthSeqID
      * returns DhpoWebServiceVO 
      * @exception throws TTKException
      */
  	public DhpoWebServiceVO getDhpoPreauthDetails(String preauthSeqID) throws TTKException{
  		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
  	     return preAuthDAO.getDhpoPreauthDetails(preauthSeqID);
  	}
  	
  	/**
     * This method get the DHPO upload preauth log details  
     * @param  DhpoWebServiceVO DHPO upload preauth log details  
     * returns row proccessed 
     * @exception throws TTKException
     */
 	public long saveDhpoPreauthLogDetails(DhpoWebServiceVO dhpoWebServiceVO) throws TTKException{
 		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
 	     return preAuthDAO.saveDhpoPreauthLogDetails(dhpoWebServiceVO);
 	}
 	/**
     * This method get the DHPO upload preauth details  
     * @param PreAuthSeqID
     * returns DhpoWebServiceVO 
     * @exception throws TTKException
     */
 	public DhpoWebServiceVO getDhpoPreauthLogDetails(String preauthSeqID) throws TTKException{
 		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
 	     return preAuthDAO.getDhpoPreauthLogDetails(preauthSeqID);
 	}
 	

     public HashMap<String, String> getProviders() throws TTKException{
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
         return preAuthDAO.getProviders();
        
     }
     
     
     public ArrayList<MOUDocumentVO> getProviderDocs(String preauthSeqID)throws TTKException
     {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getProviderDocs(preauthSeqID);
     }
     
     public InputStream getShortfallDBFile(String shortfallSeqID)throws TTKException
     {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getShortfallDBFile(shortfallSeqID);
     }
     public InputStream getPolicyTobFile(String seqId) throws TTKException
		{
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
			return preAuthDAO.getPolicyTobFile(seqId);
		}//end of getTobForBenefits(benifitType, enrollId)
     public long overridPreAuthDetails(String preAuthSeqID,String overrideRemarks,Long userSeqID,String overrideGenRemarks,String overrideGenSubRemarks) throws TTKException{
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    		return preAuthDAO.overridPreAuthDetails(preAuthSeqID,overrideRemarks,userSeqID,overrideGenRemarks,overrideGenSubRemarks);
    	}
     public ArrayList getClaimDocDBFile(String ClaimSeqId)throws TTKException
     {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getClaimDocDBFile(ClaimSeqId);
     }
     

     public String saveDentalDetails(DentalOrthoVO dentalOrthoVO,String PatOrClm)throws TTKException
     {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.saveDentalDetails(dentalOrthoVO,PatOrClm);
     }
     public LinkedHashMap<String, String> getPreauthDiagDetails() throws TTKException{
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.getPreauthDiagDetails();
		
	}// end of getPreauthDiagDetails()

     public ArrayList getPreauthDocDBFile(String preAuthRefSeqID,String preAuthRecvTypeID)throws TTKException
     {
        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
        return preAuthDAO.getPreauthDocDBFile(preAuthRefSeqID,preAuthRecvTypeID);
     }
     public ArrayList<String> getProviderSpecificCopay(Long preauthSeqID,String PatOrClm) throws TTKException{
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.getProviderSpecificCopay(preauthSeqID,PatOrClm);
		
	}// end of getPreauthDiagDetails()
     
     
     
     /**
      * This method saves the File Uploads details
      * @param mouDocumentVO mouDocumentVO object which contains the Tds Certificate details
      * @return int value, greater than zero indicates successful execution of the task
      * @exception throws TTKException
      */
 	public int saveDocUploads(PreAuthDetailVO preAuthDetailVO,ArrayList alFileAUploadList,Long userSeqId,String preAuth_Seq_ID,String origFileName,InputStream inputStream,int formFileSize) throws TTKException
     {
 		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
 		return preAuthDAO.saveDocUploads(preAuthDetailVO,alFileAUploadList,userSeqId,preAuth_Seq_ID,origFileName,inputStream,formFileSize);
     }//end of saveMouUploads(MOUDocumentVO mouDocumentVO,ArrayList alFileAUploadList) throws TTKException  
     
 	/**
     * This method Fetches the list of files uploaded details
     * @param mouDocumentVO mouDocumentVO object which contains the Tds Certificate details
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
	public ArrayList<Object> getPreauthDocsUploadsList(String preAuthSeqID) throws TTKException
    {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.getPreauthDocsUploadsList(preAuthSeqID);
    }//end of saveMouUploads(MOUDocumentVO mouDocumentVO,ArrayList alFileAUploadList) throws TTKException

     public ArrayList<Object> getBenefitDetails(ArrayList<Object> alSearchCriteria) throws TTKException {
    	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
     	return preAuthDAO.getBenefitDetails(alSearchCriteria);
     }
	/*
	**
    * This method Fetches the list of files uploaded details
    * @param mouDocumentVO mouDocumentVO object which contains the Tds Certificate details
    * @return int value, greater than zero indicates successful execution of the task
    * @exception throws TTKException
    */
	public ArrayList<Object> getclaimsDocsUploadsList(String preAuthSeqID) throws TTKException
   {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.getclaimsDocsUploadsList(preAuthSeqID);
   }//end of saveMouUploads(MOUDocumentVO mouDocumentVO,ArrayList alFileAUploadList) throws TTKException

	/**
     * This method deletes the particular TdsCertificate Info from the database,based on the certificate seq id's present in the arraylist
     * @param ArrayList alCertificateDelete
     * @return int It signifies the number of rows deleted in the database. 
     * @exception throws TTKException
     */
	public int deleteDocsUpload(ArrayList<Object> alCertificateDelete,String gateway) throws TTKException
	{
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.deleteDocsUpload(alCertificateDelete,gateway);
	}//end of deleteAssocCertificatesInfo(ArrayList<Object> alCertificateDelete)

	public ResultSet getFile(String refNo)  throws TTKException {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
		return preAuthDAO.getFile(refNo);
	}
 	
	 public long assignMultiple(PreAuthAssignmentVO preAuthAssignmentVO,String strModeValue) throws TTKException {
	        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	        return preAuthDAO.assignMultiple(preAuthAssignmentVO,strModeValue);
	    }//end of assignPreAuth(PreAuthAssignmentVO preAuthAssignmentVO)
	 
	 public PreAuthAssignmentVO getAssignMultipleTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode) throws TTKException {
	    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getAssignMultipleTo(lngAssignUsersSeqID,lngUserSeqID,strMode);
	    }//end of getAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode)

	 public ArrayList<ReportVO>getDownloadHistoryPATCLM(String strhistoryMode,Long lngMemberSeqId,String memberId,Long policySeqId,String benefitType) throws TTKException
	 {
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getDownloadHistoryPATCLM(strhistoryMode,lngMemberSeqId,memberId,policySeqId,benefitType); 
	 }
	 
	 public ArrayList<ReportVO> getDownloadHistoryDetailsPATCLM(String strhistorymode, Long authseqId)throws TTKException
	 {
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getDownloadHistoryDetailsPATCLM(strhistorymode,authseqId); 
	 }
	 
	 public ArrayList<Object> getPolicyDetails(String memberId)throws TTKException
	 {
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getPolicyDetails(memberId); 
	 }
	 
	 public ArrayList getPolicyInfo(String memberId,Long policySeqId)throws TTKException
	 {
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getPolicyInfo(memberId, policySeqId); 
	 }
	 
	 public long saveFraudData(ArrayList<String> listOfinputData) throws TTKException{
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
			return preAuthDAO.saveFraudData(listOfinputData);
		}  
		
	public Object[] getFraudDetails(ArrayList<String> listOfinputData) throws TTKException{
			 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
				return preAuthDAO.getFraudDetails(listOfinputData);
			}
			
	public long saveFraudDataForCFD(ArrayList<String> listOfinputData)
			throws TTKException {
		preAuthDAO = (PreAuthDAOImpl) this.getPreAuthDAO("preauth");
		return preAuthDAO.saveFraudDataForCFD(listOfinputData);
	}
	
	
    public ArrayList<Object> getFraudHistory(ArrayList<Object> alSearchCriteria) throws TTKException {
   	 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getFraudHistory(alSearchCriteria);
    }

	
	public TTKReportDataSource getTatReportYesterdat(String parameterValue1) throws TTKException {
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getTatReportYesterdat(parameterValue1);
	}
	
	public TTKReportDataSource getProductivityReportYestCnt(String parameterValue1) throws TTKException {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	return preAuthDAO.getProductivityReportYestCnt(parameterValue1);}

	
	public TTKReportDataSource getTatVidalTeamRpt(String parameterValue1)throws TTKException {
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getTatVidalTeamRpt(parameterValue1);
	}

	
	public TTKReportDataSource getTatAlkootTeamRpt(String parameterValue1)throws TTKException {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getTatAlkootTeamRpt(parameterValue1);
	}

	
	public TTKReportDataSource getProductivityReportMTDCnt(String parameterValue1) throws TTKException {
		preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getProductivityReportMTDCnt(parameterValue1);
	}
		@Override
	public ArrayList<UserAssignVO> getUserDetails(Long contactSeqID) throws TTKException {
		// TODO Auto-generated method stub
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	    	return preAuthDAO.getUserDetails(contactSeqID);
		}	

    public PreAuthAssignmentVO getBatchAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode) throws TTKException {
    	preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
    	return preAuthDAO.getBatchAssignTo(lngAssignUsersSeqID,lngUserSeqID,strMode);
    }//end of getAssignTo(long lngAssignUsersSeqID,long lngUserSeqID,String strMode)

	 public long assignBatchSave(PreAuthAssignmentVO preAuthAssignmentVO,String strModeValue) throws TTKException {
	        preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
	        return preAuthDAO.assignBatchSave(preAuthAssignmentVO,strModeValue);
	    }//end of assignPreAuth(PreAuthAssignmentVO preAuthAssignmentVO)

	@Override
	public ArrayList<String[]> getOverrideRemarksList(String flag, Long claimSeqId) throws TTKException {
		 preAuthDAO = (PreAuthDAOImpl)this.getPreAuthDAO("preauth");
			return preAuthDAO.getOverrideRemarksList(flag,claimSeqId);
	}
	 	

}//end of PreAuthManagerBean


