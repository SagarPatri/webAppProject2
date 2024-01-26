/**
 * @ (#) ClaimBatchManager.java July 7, 2015
 * Project 	     : ProjectX
 * File          : ClaimBatchManager.java
 * Author        : Nagababu K
 * Company       : RCS Technologies
 * Date Created  : July 7, 2015
 *
 * @author       :  Nagababu K
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.business.claims;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Local;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.claims.BatchVO;

@Local

/**
 * @author Nagababu K
 *
 */
public interface ClaimBatchManager {
	
	/**
     * This method returns the Arraylist of ClaimBenefitVO's  which contains Claim Benefit Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimBenefitVO object which contains Claim Benefit details
     * @exception throws TTKException
     */
    public ArrayList<Object> getClaimBatchList(ArrayList<Object> alSearchCriteria) throws TTKException;
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public long saveClaimBatchDetails(BatchVO batchVO) throws TTKException;
    
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public long deleteInvoiceNO(String batchSeqID,String claimSeqID) throws TTKException;
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public Object[] getClaimBatchDetails(long batchSeqID) throws TTKException;
    
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public long addClaimDetails(BatchVO batchVO,ArrayList alFileAUploadList,InputStream inputStream,int formFileSize) throws TTKException;
    
    /**
     * This method returns the HashMap of String,String  which contains Network Types details
     * @return HashMap of String object which contains Network Types details
     * @exception throws TTKException
     */
    public HashMap<String,String> getPartnersList() throws TTKException;
    
    /**
     * This method Create teh Claim Cash Benefit Details
     * @param lngPrvClaimsNbr Long object which contains the previous claims number
     * @return Long lngAddedBy Long object which contains the user seq id
     * @exception throws TTKException
     */
    public BatchVO getClaimAmountDetails(String claimSeqID) throws TTKException;
    
    /**
     * This method returns the Arraylist of ClaimUploadErrorLogVO's  which contains Claim Benefit Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimUploadErrorLogVO object which contains Claim Upload Error logs
     * @exception throws TTKException
     */
    public ArrayList<Object> getClaimUploadErrorLogList(ArrayList<Object> alSearchCriteria) throws TTKException;
    
    public ArrayList getBatchAssignHistory(long batchSeqID) throws TTKException;

    public ArrayList<Object> getClaimBatchHistoryList(ArrayList<Object> alSearchCriteria) throws TTKException;
	public ArrayList getClaimAutoRejectionList(ArrayList<Object> alSearchCriteria) throws TTKException;
	public String getProviderId(String providerseqid)throws TTKException;
	public TTKReportDataSource getAutoRejectClaimBatchReport(String parameter)throws TTKException;
	public TTKReportDataSource getAutoRejectClaimBatchDetailReport(String parameter)throws TTKException;

    
}//end of ClaimBatchManager
