package com.ttk.business.onlineforms.providerLogin; 

/**
 * @ (#) OnlineProviderManager.java Jul 19, 2007
 * Project 	     : TTK HealthCare Services
 * File          : OnlineProviderManager.java
 * Author        : Kishor kumar S H
 * Company       : RCS Technologoes
 * Date Created  : Nov 18, 2015
 *
 * @author       :  Kishor kumar S H
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */


import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Clob;
import java.util.ArrayList;

import javax.ejb.Local;

import org.apache.struts.upload.FormFile;

import com.ttk.action.table.onlineforms.providerLogin.OverDueReportTable;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.onlineforms.PartnerClaimSubmissionVo;
import com.ttk.dto.onlineforms.insuranceLogin.DashBoardVO;
import com.ttk.dto.onlineforms.insuranceLogin.InsCorporateVO;
import com.ttk.dto.onlineforms.providerLogin.BatchListVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

@Local

public interface OnlineProviderManager {
	
	
    /*
     * get the details on click on the link in corporate search- leads to corporate focused view 
     */
    public ArrayList getPreAuthSearchList(ArrayList arrayList) throws TTKException;
    

    /*
     * get the details on click on the link in corporate search- leads to corporate focused view 
     */
    public ArrayList getPartnerPreAuthSearchList(ArrayList arrayList) throws TTKException;
    
    
    /*
     * get The Authorization details on click on the table
     */
    
    public PreAuthSearchVO getAuthDetails(String empanelNo,String status,Long patAuthSeqId) throws TTKException;

    
    /*
     * get The Shortfall List on click on the table
     */
    
    public ArrayList getShortfallList(Long patAuthSeqId) throws TTKException;
    
    
    /*
     * To get the Cheque Details
     */
    
    public String[] getChequeDetails(String chequeNumber) throws TTKException;
    
    /*
     * To get the Invoice Details
     */
    
    public String[] getInvoiceDetails(String chequeNumber,String empanelNumber) throws TTKException;

    
    
    
    /*
     * Get Log Details
     */
    
    public ArrayList getLogData(String strUserId) throws TTKException;
    
    /*
     * Get Batch Reconciliation Details
     */
    
    public ArrayList getBatchRenconcilList(ArrayList arrayList) throws TTKException;
    
    
    /*
     * Get Batch Reconciliation Invoice details 
     */
    
    public ArrayList<BatchListVO> getBatchInvDetails(Long BatchSeqId, String strFlag,String EmpnlNo) throws TTKException;
    
    
    /*
     * Get Batch Invoice List
     */
    
    public ArrayList getBatchInvoiceList(ArrayList arrayList) throws TTKException;   
    
    /*
     * Get Batch Invoice report Details
     */
    
    public String[] getBatchInvReportDetails(String invNo,String EmpnlNo) throws TTKException;   
    
    
    /*
     * Get Over Due Report Search List
     */
    
    public ArrayList<BatchListVO> getOverDueList(ArrayList<Object> arrayList) throws TTKException;
    
    
   /*
    *  Finance DashBoard Details
    */
    public String[] getFinanceDahBoard(String empanelNo,String fromDate,String toDate) throws TTKException;
    
   
    /*
     *  Get Recent Reports Downloaded
     */
     public ArrayList<String[]> getRecentReports(String empanelNo,Long userId) throws TTKException;
     
    
    
    public String saveShorfallDocs(Long shortfallSeqID,FormFile formFile) throws TTKException;
    
    public String[] getMemProviderForShortfall(Long shortfallSeqID,String patOrClm) throws TTKException;
    
    //Provider Clams
    public String saveClaimXML(InputStream inputStream2, String fileName, Long userSeqId) throws TTKException;
    public String[] uploadingClaims(String batchRefNo, Long userSeqId) throws TTKException;
    public String[] saveProviderClaims(InputStream inputStream2, String fileName, Long userSeqId) throws TTKException;
    public ArrayList getClaimSearchList(ArrayList arrayList) throws TTKException;
    public String[] getBatchNoForClaims(String hospSeqId,String addedBy,String receiveDate,String receiveTime,String receiveDay,String sourceType) throws TTKException;
    public Object[] getClaimSubmittedDetails(String batchNo) throws TTKException;
    public Object[] getClaimDetails(Long patAuthSeqId) throws TTKException;
    
    //Added for Partner Log Search
    public ArrayList getPartnerClaimSearchList(ArrayList arrayList) throws TTKException;


	public PreAuthSearchVO getPreAuthMetaData(
			CashlessDetailVO cashlessDetailVO,
			UserSecurityProfile userSecurityProfile, String string,
			String string2, Object object, String string3, String string4) throws TTKException;


	public String saveProviderShorfallDocs(ArrayList inputData) throws TTKException;
	public ArrayList<PartnerClaimSubmissionVo> savePartnerClaims(PartnerClaimSubmissionVo partnerClaimSubmissionVo) throws TTKException;
    public String saveReClaimXML(InputStream inputStream2, String fileName, Long userSeqId, String claimsSubmissionTypes) throws TTKException;
    public String[] uploadingClaimsRe(String batchRefNo, Long userSeqId) throws TTKException;


	public String saveAuditClaimUploadXML(FileInputStream inputStream2,String fileName, Long userSeqId)throws TTKException;
	
	 public ArrayList<Object> getAuditDataUploadList(ArrayList<Object> alSearchCriteria) throws TTKException;
	 
    public int deleteUploadedData(String strDeletedPolicyNo, Long userSeqId) throws TTKException;
	 
	 


	public String[] uploadingReSubmissionClaims(String batchRefNo,Long userSeqId)throws TTKException;
	
	public ArrayList getReSubmissionClaimSearchList(ArrayList searchData) throws TTKException;

}//end of OnlineProviderManager
