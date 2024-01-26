/**
 * @ (#) ClaimManagerBean.java Jul 15, 2006
 * Project 	     : TTK HealthCare Services
 * File          : ClaimManagerBean.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Jul 15, 2006
 *
 * @author       :  RamaKrishna K M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.business.claims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.claims.ClaimDAOFactory;
import com.ttk.dao.impl.claims.ClaimDAOImpl;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.claims.ClaimInwardDetailVO;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;
import com.ttk.dto.claims.ClauseVO;
import com.ttk.dto.claims.ShortfallRequestDetailsVO;//ADDED AS PER 1179
import com.ttk.dto.preauth.AuthorizationVO;
import com.ttk.dto.preauth.PreAuthDetailVO;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class ClaimManagerBean implements ClaimManager {

	private ClaimDAOFactory claimDAOFactory = null;
	private ClaimDAOImpl claimDAO = null;

	/**
     * This method returns the instance of the data access object to initiate the required tasks
     * @param strIdentifier String object identifier for the required data access object
     * @return BaseDAO object
     * @exception throws TTKException
     */
    private BaseDAO getClaimDAO(String strIdentifier) throws TTKException
    {
        try
        {
            if (claimDAOFactory == null)
            	claimDAOFactory = new ClaimDAOFactory();
            if(strIdentifier!=null)
            {
               return claimDAOFactory.getDAO(strIdentifier);
            }//end of if(strIdentifier!=null)
            else{
            	return null;
            }//end of else
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "error."+strIdentifier+".dao");
        }//end of catch(Exception exp)
    }//End of getClaimDAO(String strIdentifier)

	/**
     * This method returns the Arraylist of ClaimInwardVO's  which contains Claim Inward Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimInwardVO object which contains Claim Inward details
     * @exception throws TTKException
     */
    public ArrayList getClaimInwardList(ArrayList alSearchCriteria) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClaimInwardList(alSearchCriteria);
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
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClaimInwardDetail(lngClaimInwardSeqID,strClaimTypeID,lngUserSeqID);
    }//end of getClaimInwardDetail(long lngClaimInwardSeqID,String strClaimTypeID,long lngUserSeqID)

    /**
     * This method returns the Arraylist of ClaimantVO's which contains Claim Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimantVO object which contains Claim details
     * @exception throws TTKException
     */
    public ArrayList getClaimShortfallList(ArrayList alSearchCriteria) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClaimShortfallList(alSearchCriteria);
    }//end of getClaimShortfallList(ArrayList alSearchCriteria)

    /**
     * This method returns the Arraylist of ShortfallVO's which contains Claim Shortfall Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ShortfallVO object which contains Claim Shortfall details
     * @exception throws TTKException
     */
    public ArrayList getInwardShortfallDetail(ArrayList alSearchCriteria) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getInwardShortfallDetail(alSearchCriteria);
    }//end of getInwardShortfallDetail(ArrayList alSearchCriteria)

    /**
     * This method returns the Arraylist of DocumentChecklistVO's  which contains Claim Document Details
     * @param strClaimTypeID contains Claim Type ID
     * @return ArrayList of DocumentChecklistVO object which contains Claim Inward Document details
     * @exception throws TTKException
     */
    public ArrayList getClaimDocumentList(String strClaimTypeID) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClaimDocumentList(strClaimTypeID);
    }//end of getClaimDocumentList(String strClaimTypeID)

    /**
	 * This method saves the Claim Inward information
	 * @param claimInwardDetailVO the object which contains the Claim Inward Details which has to be saved
	 * @return long the value contains Claim Inward Seq ID
	 * @exception throws TTKException
	 */
	public long saveClaimInwardDetail(ClaimInwardDetailVO claimInwardDetailVO) throws TTKException {
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.saveClaimInwardDetail(claimInwardDetailVO);
	}//end of saveClaimInwardDetail(ClaimInwardDetailVO claimInwardDetailVO)

	/**
     * This method returns the Arraylist of PreAuthVO's  which contains Claim Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Claim details
     * @exception throws TTKException
     */
    public ArrayList getClaimList(ArrayList alSearchCriteria) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClaimList(alSearchCriteria);
    }//end of getClaimList(ArrayList alSearchCriteria)
    /**
     * This method returns the PreAuthDetailVO, which contains all the Claim details
     * @param alClaimList contains Claim seq id,Member Seq ID to get the Claim Details
     * @return PreAuthDetailVO object which contains all the Claim details
     * @exception throws TTKException
     */
    public Object[] getClaimDetails(Long claimSeqId) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClaimDetails(claimSeqId);
    }//end of getClaimDetail(Long alClaimList)
    
    /**
     * This method returns the PreAuthDetailVO, which contains all the Claim details
     * @param alClaimList contains Claim seq id,Member Seq ID to get the Claim Details
     * @return PreAuthDetailVO object which contains all the Claim details
     * @exception throws TTKException
     */
    public PreAuthDetailVO getMemberDetails(String memberID) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getMemberDetails(memberID);
    }//end of getMemberDetails(String memberID)
    
    /**
     * This method returns the PreAuthDetailVO, which contains all the Claim details
     * @param alClaimList contains Claim seq id,Member Seq ID to get the Claim Details
     * @return PreAuthDetailVO object which contains all the Claim details
     * @exception throws TTKException
     */
    public PreAuthDetailVO getClaimDetail(ArrayList alClaimList) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClaimDetail(alClaimList);
    }//end of getClaimDetail(ArrayList alClaimList)
    /**
     * This method returns the PreAuthDetailVO, which contains all the Claim details
     * @param alClaimList contains Claim seq id,Member Seq ID to get the Claim Details
     * @return PreAuthDetailVO object which contains all the Claim details
     * @exception throws TTKException
     */
    public Map<String, String> getMemClaimList(Long  enrollSeqID,String claimType, String hospSeqId,String submissionCatagory,String paymentTo) throws TTKException{
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getMemClaimList(enrollSeqID,claimType,hospSeqId,submissionCatagory,paymentTo);
    }//end of getClaimDetail(ArrayList alClaimList)
   
    /**
     * This method releases the Preauth Associated to NHCPClaim
     * @param lngPATEnrollDtlSeqID PAT Enroll Dtl Seq ID
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int releasePreauth(long lngPATEnrollDtlSeqID) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.releasePreauth(lngPATEnrollDtlSeqID);
    }//end of releasePreauth(long lngPATEnrollDtlSeqID)

    /**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public long saveClaimDetail(PreAuthDetailVO preauthDetailVO) throws TTKException {
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.saveClaimDetail(preauthDetailVO);
	}//end of saveClaimDetail(PreAuthDetailVO preauthDetailVO)
	
	/**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public long saveClaimDetails(PreAuthDetailVO preauthDetailVO) throws TTKException {
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.saveClaimDetails(preauthDetailVO);
	}//end of saveClaimDetails(PreAuthDetailVO preauthDetailVO)

	
	/**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public Object[] calculateClaimAmount(Long claimSeqId,Long hositalSeqId,Long addedBy) throws TTKException {
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.calculateClaimAmount(claimSeqId,hositalSeqId,addedBy);
	}//end of calculateClaimAmount(PreAuthDetailVO preauthDetailVO)
	
	/**
	 * This method saves the Claim information
	 * @param preauthDetailVO the object which contains the Claim Details which has to be saved
	 * @return long the value contains Claim Seq ID
	 * @exception throws TTKException
	 */
	public Object[] saveAndCompleteClaim(PreAuthDetailVO preAuthDetailVO) throws TTKException {
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.saveAndCompleteClaim(preAuthDetailVO);
	}//end of saveAndCompleteClaim(PreAuthDetailVO preauthDetailVO)
	
	
	/**
     * This method returns the Arraylist of PreAuthDetailVO's which contains Preauthorization Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthDetailVO object which contains Preauthorization details
     * @exception throws TTKException
     */
    public ArrayList getPreauthList(ArrayList alSearchCriteria) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getPreauthList(alSearchCriteria);
    }//end of getPreauthList(ArrayList alSearchCriteria)

    /**
     * This method returns the Arraylist of Cache Object's which contains Previous Claim Seq ID's
     * @param lngMemberSeqID Member Seq ID
     * @return ArrayList of Cache Object's Previous Claim Seq ID's
     * @exception throws TTKException
     */
    public ArrayList getPrevClaim(long lngMemberSeqID) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getPrevClaim(lngMemberSeqID);
    }//end of getPrevClaim(long lngMemberSeqID)

    /**
	 * This method saves the Review information
	 * @param preauthDetailVO the object which contains the Review Details which has to be  saved
	 * @param strMode String object which contains Mode Pre-authorization/Claims - PAT/CLM
	 * @param strType String object which contains Type
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
    public PreAuthDetailVO saveReview(PreAuthDetailVO preauthDetailVO,String strMode,String strType) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.saveReview(preauthDetailVO,strMode,strType);
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
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getPrevHospList(lngMemberSeqID,strMode,lngClaimSeqID);
    }//end of getPrevHospList(long lngMemberSeqID,String strMode,long lngClaimSeqID)

    /**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteClaimGeneral(ArrayList alDeleteList) throws TTKException {
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.deleteClaimGeneral(alDeleteList);
	}//end of deleteClaimGeneral(ArrayList alDeleteList)

	/**
     * This method returns the ArrayList object, which contains all the Users for the Corresponding TTK Branch
     * @param alAssignUserList ArrayList Object contains ClaimSeqID,PolicySeqID and TTKBranch
     * @return ArrayList object which contains all the Users for the Corresponding TTK Branch
     * @exception throws TTKException
     */
    public ArrayList getAssignUserList(ArrayList alAssignUserList) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getAssignUserList(alAssignUserList);
    }//end of getAssignUserList(ArrayList alAssignUserList)

    /**
	 * This method will allow to Override the Claim information
	 * @param lngClaimSeqID ClaimSeqID
	 * @param lngUserSeqID long value which contains Logged-in User
	 * @return PreAuthDetailVO object which contains Review Information
	 * @exception throws TTKException
	 */
    public PreAuthDetailVO overrideClaim(long lngClaimSeqID,long lngUserSeqID) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.overrideClaim(lngClaimSeqID,lngUserSeqID);
    }//end of overrideClaim(long lngClaimSeqID,long lngUserSeqID)

    /**
     * This method returns the ClauseVO, which contains all the Clause details
     * @param alClauseList contains Claim seq id,Policy Seq ID,EnrolTypeID,AdmissionDate to get the Clause Details
     * @return ClauseVO object which contains all the Clause details
     * @exception throws TTKException
     */
    public ClauseVO getClauseDetail(ArrayList alClauseList) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getClauseDetail(alClauseList);
    }//end of getClauseDetail(ArrayList alClauseList)

    /**
	 * This method saves the Clause Details
	 * @param alClauseList the arraylist which contains the Clause Details which has to be saved
	 * @return String value which contains clauses description
	 * @exception throws TTKException
	 */
	public String saveClauseDetail(ArrayList alClauseList) throws TTKException {
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.saveClauseDetail(alClauseList);
	}//end of saveClauseDetail(ArrayList alClauseList)
	
	/**
     * This method returns the ArrayList object, which contains list of Letters to be sent for Claims Rejection
     * @param alLetterList ArrayList Object contains ClaimTypeID and Ins Seq ID
     * @return ArrayList object which contains list of Letters to be sent for Claims Rejection
     * @exception throws TTKException
     */
    public ArrayList getRejectionLetterList(ArrayList alLetterList) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getRejectionLetterList(alLetterList);
    }//end of getRejectionLetterList(ArrayList alLetterList)
    
    /**
	 * This method reassign the enrollment ID
	 * @param alReAssignEnrID the arraylist of details for which have to be reassigned
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int reAssignEnrID(ArrayList alReAssignEnrID) throws TTKException
	{
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.reAssignEnrID(alReAssignEnrID);
	}//end of reAssignEnrID(ArrayList alReAssignEnrID)
	
	 /**
	 * This method gets the service tax  amount
	 * @param AuthorizationVO It contains all the authorization details
	 * @return Object[] the values,of Service tax amt to be paid,Fianl approved amount and service tax percentage
	 * @exception throws TTKException
	 */
	public Object[] getServTaxCal(AuthorizationVO authorizationVO) throws TTKException{
		claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
		return claimDAO.getServTaxCal(authorizationVO);
	}//getServTaxCal(AuthorizationVO authorizationVO)
	// Shortfall CR KOC1179
    
    /**
     * This method returns the  ShortfallRequestDetailsVO's  which contains Shortfall Request Details
     * @param shortfallSeqID String object which contains the search criteria
     * @return ShortfallRequestDetailsVO object which contains Claim Inward details
     * @exception throws TTKException
     */
    public ShortfallRequestDetailsVO generateShortfallRequestDetails(String shortfallSeqID) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.generateShortfallRequestDetails(shortfallSeqID);
    }//end of getClaimInwardList(String shortfallSeqID)
	
	 // Shortfall CR KOC1179
    
    /**
     * This method returns the long  which contains rows processed 
     * @param ShortfallTypeDesc,ShortfallEmailSeqID,ShortfallType and UserSeqId object which contains the search criteria
     * @return returns the long  which contains rows processed
     * @exception throws TTKException
     */
    public long resendShortfallEmails(String ShortfallTypeDesc,String ShortfallEmailSeqID,String ShortfallType,long UserSeqId) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.resendShortfallEmails(ShortfallTypeDesc,ShortfallEmailSeqID,ShortfallType,UserSeqId);
    }//end of resendShortfallEmails(String ShortfallTypeDesc,String ShortfallEmailSeqID,String ShortfallType,long UserSeqId)
	
	//Shortfall CR koc1179
 //added for Mail-SMS Template for Cigna
    /**
     * This method returns the long  which contains rows processed 
     * @param ShortfallTypeDesc,ShortfallEmailSeqID,ShortfallType and UserSeqId object which contains the search criteria
     * @return returns the long  which contains rows processed
     * @exception throws TTKException
     */
    public long resendCignaShortfallEmails(String shortfallEmailSeqID,String ShortfallTypeDesc, String ShortfallType, long UserSeqId) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.resendCignaShortfallEmails(shortfallEmailSeqID,ShortfallTypeDesc,ShortfallType,UserSeqId);
    }//end of resendShortfallEmails(String ShortfallTypeDesc,String ShortfallEmailSeqID,String ShortfallType,long UserSeqId)
	//ended
    /**
     * This method returns the Arraylist of ShortfallStatusVO's  which contains Shortfall Status
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ShortfallStatusVO's object which contains Shortfall Status
     * @exception throws TTKException
     */
    public ArrayList getShortfallRequests(ArrayList alSearchCriteria) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getShortfallRequests(alSearchCriteria);
    }//end of getClaimList(ArrayList alSearchCriteria)
    
    //Shortfall CR koc1179
    /**
     * This method returns the Arraylist of PreAuthVO's  which contains Claim Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PreAuthVO object which contains Claim details
     * @exception throws TTKException
     */
    public ArrayList getShortfallGenerateSend(ArrayList alSearchCriteria) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.getShortfallGenerateSend(alSearchCriteria);
    }//end of getClaimList(ArrayList alSearchCriteria)
    // Shortfall CR KOC1179
    /**
     * This method returns the Arraylist of ClaimInwardVO's  which contains Claim Inward Details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ClaimInwardVO object which contains Claim Inward details
     * @exception throws TTKException
     */
    public long sendShortfallDetails(String strSfEmailSeqId ,String strSfRequestType,String strSentBy,long strAddedBy) throws TTKException {
    	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
    	return claimDAO.sendShortfallDetails(strSfEmailSeqId,strSfRequestType,strSentBy,strAddedBy);
    }//end of getClaimInwardList(ArrayList alSearchCriteria)
	
	   //KOC 1179 
  
	   
		public long saveShortFallFileName(String fileName,long UserSeqID,String strShortfallNoSeqID,String shortfalltype) throws TTKException{
  
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.saveShortFallFileName(fileName,UserSeqID,strShortfallNoSeqID,shortfalltype);
		}
	//public long saveShortFallFileName(String fileName) throws TTKException{
	//claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	//return claimDAO.saveShortFallFileName(fileName);
	//return claimDAO.saveShortFallFileName(fileName,UserSeqID,strShortfallNoSeqIDArr);

//KOC 1179 
public ArrayList viewShortFallAdvice(ArrayList alSearchCriteria) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.viewShortFallAdvice(alSearchCriteria);
}


public String getBatchFileName(String strBatchNo) throws TTKException{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getBatchFileName(strBatchNo);
}
public long overridClaimDetails(String claimSeqID,String overrideRemarks,Long userSeqID,String overrideGenRemarks,String overrideGenSubRemarks) throws TTKException{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.overridClaimDetails(claimSeqID,overrideRemarks,userSeqID,overrideGenRemarks,overrideGenSubRemarks);
}

public void getRejectedClaimDetails(Long claimSeqId,String denialCode,String medicalOpinionRemarks,String overrideRemarks,String finalRemarks,Long addedBy,String internalRemarks) throws TTKException
{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	claimDAO.getRejectedClaimDetails(claimSeqId,denialCode,medicalOpinionRemarks,overrideRemarks,finalRemarks,addedBy,internalRemarks);
}//end of getClaimDetail(Long alClaimList)



public LinkedHashMap<String, String> getClaimDiagDetails() throws TTKException{
	claimDAO=(ClaimDAOImpl) this.getClaimDAO("claim");
	return claimDAO.getClaimDiagDetails();
	
}


public ArrayList getPatDetails(String claimSeqID, String preauthSeqId) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getPatDetails(claimSeqID,preauthSeqId);
}//end of getPreauthList(ArrayList alSearchCriteria)


public String[] getClaimSettleMentDtls(Long claimSeqID) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getClaimSettleMentDtls(claimSeqID);
}//end of getPreauthList(ArrayList alSearchCriteria)

public ArrayList<String> saveFraudData(ArrayList<String> listOfinputData) throws TTKException{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.saveFraudData(listOfinputData);
}

public ArrayList<String> fetchFraudData(String clmSeqId) throws TTKException{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.fetchFraudData(clmSeqId);
}

public ArrayList getClaimAndPreauthList(ArrayList alSearchCriteria) throws TTKException{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getClaimAndPreauthList(alSearchCriteria);
}

@Override
public ArrayList getClaimDetailedReportData(ArrayList<Object> searchData) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getClaimDetailedReportData(searchData);
}

public ArrayList getClaimListForAlert(ArrayList alSearchCriteria) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getClaimListForAlert(alSearchCriteria);
}

@Override
public ArrayList getProcessedClaimList(ArrayList<Object> searchData)
		throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getProcessedClaimList(searchData);
}


public Object[] getProcessedClaimDetails(String claimSeqID) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getProcessedClaimDetails(claimSeqID);
}
public ArrayList getCFDCompaginList(ArrayList alSearchCriteria) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getCFDCompaginList(alSearchCriteria);
}

public int saveCampaignDetails(PreAuthDetailVO preAuthDetailVO) throws TTKException 
{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.saveCampaignDetails(preAuthDetailVO);
}

public PreAuthDetailVO getCampaignDetails(Long campaignDtlSeqId) throws TTKException 
{
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getCampaignDetails(campaignDtlSeqId);
}


public ArrayList<Object> getSubgroupName(String overrideRemarksId) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getSubgroupName(overrideRemarksId);
}

public ArrayList<Object> getGenOverrideSubgroupName(String overrideGenRemarksId) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getGenOverrideSubgroupName(overrideGenRemarksId);
}

public ArrayList getClaimsDiscountActReport(ArrayList<Object> searchData) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getClaimsDiscountActReport(searchData);
}

public Object[] getAutoRejectionClaimDetails(String xmlseqid, String parentClaimNo) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getAutoRejectionClaimDetails(xmlseqid,parentClaimNo);
}


public long saveAutoRejectClaimDetails(ClaimUploadErrorLogVO autoRejectedVo)throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.saveAutoRejectClaimDetails(autoRejectedVo);
}

public long saveAutoRejectClaimActivityDetails(ClaimUploadErrorLogVO autoRejectedVo)throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.saveAutoRejectClaimActivityDetails(autoRejectedVo);
}


public int doRevaluationClaim(String xmlseqid, String parentClaimNo,long updatedBy) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.doRevaluationClaim(xmlseqid,parentClaimNo,updatedBy);
}

@Override
public TTKReportDataSource getDetailedResubmissionClaimDetails(String parameter) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getDetailedResubmissionClaimDetails(parameter);
}

public PreAuthDetailVO getExceptionFlag(long claimSeqID, String exceptionFlag) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getExceptionFlag(claimSeqID,exceptionFlag);
}
@Override
public TTKReportDataSource getOverAllBatchResubmissionDeatils(String parameter) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getOverAllBatchResubmissionDeatils(parameter);
}

@Override
public TTKReportDataSource getResubmissionErrorLogDetails(String parameter) throws TTKException {
	claimDAO = (ClaimDAOImpl)this.getClaimDAO("claim");
	return claimDAO.getResubmissionErrorLogDetails(parameter);
}



}//end of ClaimManagerBean

