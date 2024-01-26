package com.ttk.business.onlineforms.providerLogin; 

/**
 * @ (#) OnlinePbmProviderManager.java April 02, 2017
 * Project 	     : 
 * File          : OnlinePbmProviderManager.java
 * Author        : Nagababu Kamadi
 * Company       : RCS Technologoes
 * Date Created  : April 02, 2017
 *
 * @author       :  Nagababu Kamadi
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */


import java.io.InputStream;
import java.util.ArrayList;

import javax.ejb.Local;

import org.apache.struts.upload.FormFile;

import com.ttk.common.exception.TTKException;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.preauth.ActivityDetailsVO;

@Local

public interface OnlinePbmProviderManager {
	
    /*
     * Add Eligibility details
     */
    public Long addPreAuthGeneralDetails(PbmPreAuthVO preAuthVO) throws TTKException;
    /*
     * Add member validation
     */
    public PbmPreAuthVO  validateMemberID(PbmPreAuthVO preAuthVO) throws TTKException;
    /*
     * get all preauth  details
     */
    public PbmPreAuthVO getAllPreAuthDetails(Long preAuthSeqID) throws TTKException;
    /*
     * chechClaimElegibility
     */
    public String checkClaimElegibility(Long preAuthSeqID) throws TTKException;
    /*
     * Add icd details
     */
    public Integer addIcdDetails(PbmPreAuthVO preAuthVO) throws TTKException;
    
    /*
     * Add Drug details
     */
    public Integer addDrugDetails(PbmPreAuthVO preAuthVO) throws TTKException;
    
    /*
     * Proccess the preauth request authorization
     */
    public Long requstAuthorization(PbmPreAuthVO preAuthVO) throws TTKException;
    
    
    /*
     * Delete diagnosis details
     */
    public Integer deleteDiagnosisDetails(String strIcdDtlSeqID,String preAuthSeqID) throws TTKException;
    
    /*
     * Delete drug details
     */
    public Integer deleteDrugDetails(String strDrugDtlSeqID,String preAuthSeqID) throws TTKException;
    
    /*
     * Preauth list 
     */
    public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData,Long userSeqID, Long hospSeqId) throws TTKException;
    
    /*
     * Claim list 
     */
    public ArrayList<PbmPreAuthVO> getPbmClaimList(ArrayList alSearchCriteria,Long userSeqID) throws TTKException;
    
    public ActivityDetailsVO getTariffDetails(PbmPreAuthVO pbmPreAuthVO) throws TTKException;
	public long requstAuthWebservice(Long preAuthSeqID,	PbmPreAuthVO pbmPreAuthVO) throws TTKException;
	/*
     * get all claim  details
     */
    public PbmPreAuthVO getPBMSubmitClaim(ArrayList<Object> claimData) throws TTKException;
    
    /*
     * save PBM file
     */
    public long SavePBMUploadFile(String preAuthSeqID,String generateType,FormFile formFile) throws TTKException;
    /*
     * get all claim  details
     */
    public PbmPreAuthVO getClaimDetails(Long claimSeqNo,Long preAuthSeqId) throws TTKException;
    
    
	public long requstComp_preauth(Long preAuthSeqID,	PbmPreAuthVO pbmPreAuthVO) throws TTKException;
	
	public String saveClaimXML(InputStream inputStream2, String fileName, Long userSeqId) throws TTKException;
	
	public String[] uploadingClaims(String batchRefNo, Long userSeqId) throws TTKException;
	
	public String saveAppealComments(Long preAuthSeqId,String appealComments) throws TTKException;
	public PbmPreAuthVO geMemberDetailsOnEnrollId(String enrollId,String benifitTypeVal, Long hospSeqId) throws TTKException;
	public String[] getTobForBenefits(String benifitTypeVal, String enrollId)throws TTKException;
	public int updateLogTable(ArrayList<String> dataList)throws TTKException;
   }//end of OnlinePbmProviderManager
