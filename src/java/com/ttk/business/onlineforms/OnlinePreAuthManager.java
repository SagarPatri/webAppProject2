/**
 * @ (#) OnlinePreAuthManager.java 29th April 2015
 * Project 	     : TTK HealthCare Services
 * File          : OnlinePreAuthManager.java
 * Author        : Kishor kumar S H
 * Company       : RCS Technologies
 * Date Created  : 29th April 2015
 *
 * @author       :  Kishor kumar S H
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.business.onlineforms;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.empanelment.LabServiceVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.DrugDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;


@Local

public interface OnlinePreAuthManager {

	/*
	 * getValidate Vidal ID from Pre Auth Screen
	 */
    public String getValidateVidalId(String vidalId) throws TTKException;
    
    
    /*
	 * This perocdure is to get the enrollment member details
	 */
	public CashlessDetailVO geMemberDetailsOnEnrollId(String EnrollId,String benifitTypeVal) throws TTKException;
   	
	/**
	 * 
	 */
	public ArrayList getSelectedLabDetails(String selectedLabIds,Long PAT_INTIMATION_SEQ_ID,String enrollId) throws TTKException;
	
	/**
	 * 
	 */
	public LabServiceVO getConsumableDetails(String consDesc) throws TTKException;

	
	
	/**
	 * Save Online Partial PreAuth Details
	 */
	
	public String[] savePreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,UserSecurityProfile userSecurityProfile,String medicalIds,String SubmitYN,String loginFlag,HttpServletRequest request) throws TTKException;
	
	/**
	 * Save Online PreAuth Details
	 */
	
	public String[] savePartialPreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,UserSecurityProfile userSecurityProfile,String medicalIds,String enhanceYN) throws TTKException;
	
	/**
	 * Save Online partner PreAuth Details
	 */
	
	public String[] savePartnerPartialPreAuthDetails(CashlessDetailVO cashlessDetailVO,UserSecurityProfile userSecurityProfile,String medicalIds,String enhanceYN,FormFile formFile,String fileName,String description) throws TTKException;
	
	/**
	 * 
	 * @param iPreAuthIntSeqId
	 * @return
	 * @throws TTKException
	 */
	public int savePreAuthConsumables(LabServiceVO labServiceVO,Long intimationSeqID) throws TTKException;
	
	
	
	/*
	 * Get the PreAuth Details after saving and passing the pat intimation sequence ID
	 */
	public CashlessDetailVO getPreAuthDetails(Long lPreAuthIntSeqId) throws TTKException;
	
	
	/**
	 * Get The Inserted data for consumables
	 */
	public ArrayList getPreAuthConsumables(Long intimationSeqID) throws TTKException;
	
	public LabServiceVO getPreAuthPharamcyDetails(String pharmacyDesc) throws TTKException;
	
	
	public int saveDiagnosisDetails(ArrayList<DiagnosisDetailsVO> diagnosis,Long lPreAuthIntSeqId,Long userSeqId) throws TTKException;
	
	public int saveActivitiesDetails(ArrayList<ActivityDetailsVO> alActivityDetails,Long lPreAuthIntSeqId,Long userSeqId,String treatmentDate,String clinicianId,String loginFlag,HttpServletRequest request) throws TTKException;

	public int saveDrugDetails(ArrayList<DrugDetailsVO> alDrugDetails,Long lPreAuthIntSeqId,Long userSeqId,String treatmentDate,String clinicianId,String loginFlag,HttpServletRequest request) throws TTKException;

	public ArrayList<DiagnosisDetailsVO> getDiagDetails(Long lPreAuthIntSeqId)throws TTKException;

	public Object[] deleteDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException;
	
    public Object[] getOnlinePreAuthDetails(Long preAuthSeqId) throws TTKException;

    public ArrayList getActivityCodeList(ArrayList alSearchCriteria,String actOrDrug) throws TTKException;
    
    public ArrayList getDrugCodeList(ArrayList alSearchCriteria) throws TTKException;
    
    public ArrayList getDiagCodeList(ArrayList alSearchCriteria) throws TTKException;
    
    public ActivityDetailsVO getActivityCodeDetails(Long hospSeqId,String mode,Long activitySeqId,Date treatmentDate,String searchType,String actOrDrug) throws TTKException;
    
    public DrugDetailsVO getDrugCodeDetails(Long hospSeqId,String mode,Long drugSeqId,Date treatmentDate,String actOrDrug) throws TTKException;
    
    public Object[] getOnlinePartialPreAuthDetails(Long preAuthSeqId) throws TTKException;
    
    public Object[] getOnlinePartnerPartialPreAuthDetails(Long preAuthSeqId) throws TTKException;
    
    public long deleteExistngDataForPreAuth(String seqIds,Long lPreAuthIntSeqId,String flag,String seqQty,Long LhospSeqId,Long userSeqId,String loginFlag,HttpServletRequest request) throws TTKException;
        
    public InputStream getProviderUploadedDocsDBFile(String fileSeqId)throws TTKException;
    
    public ArrayList<CacheObject> getEnounterTypes(String benefitType) throws TTKException;
    
    public String saveDentalDetails(DentalOrthoVO dentalOrthoVO,String PatOrClm,Long lPreAuthIntSeqId,Long userSeqId,String loginFlag,HttpServletRequest request) throws TTKException;
    
    public String saveAppealComments(Long preAuthSeqId,String appealComments) throws TTKException;
    
    public void deleteProviderDetail(Long preAuthSeqId,Long userSeqId) throws TTKException;	

    public String  getVipYN(String enrollid)throws TTKException;
}//end of OnlinePreAuthManager
