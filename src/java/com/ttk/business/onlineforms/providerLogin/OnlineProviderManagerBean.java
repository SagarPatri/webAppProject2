package com.ttk.business.onlineforms.providerLogin; 
/**
 * @ (#) OnlineProviderManagerBean.java 20 Nov 2015
 * Project 	     : TTK HealthCare Services
 * File          : OnlineProviderManagerBean.java
 * Author        : Kishor kumar S H
 * Company       : RCS TEchnologies
 * Date Created  : 20 Nov 2015
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

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import org.apache.struts.upload.FormFile;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.onlineforms.OnlineAccessDAOFactory;
import com.ttk.dao.impl.onlineforms.providerLogin.ClaimsDAOImpl;
import com.ttk.dao.impl.onlineforms.providerLogin.OnlineClaimDAOFactory;
import com.ttk.dao.impl.onlineforms.providerLogin.OnlineProviderDAOImpl;
import com.ttk.dto.onlineforms.PartnerClaimSubmissionVo;
import com.ttk.dto.onlineforms.providerLogin.BatchListVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;
@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class OnlineProviderManagerBean implements OnlineProviderManager{

    private OnlineClaimDAOFactory onlineClaimDAOFactory = null;
	private OnlineAccessDAOFactory onlineAccessDAOFactory = null;
	private OnlineProviderDAOImpl onlineProviderDAOImpl = null;

    private ClaimsDAOImpl claimsDAO = null;
	/**
	 * This method returns the instance of the data access object to initiate the required tasks
	 * @param strIdentifier String object identifier for the required data access object
	 * @return BaseDAO object
	 * @exception throws TTKException
	 */
	private BaseDAO getOnlineAccessDAO(String strIdentifier) throws TTKException
	{
		try
		{
			
			if("providerClaim".equals(strIdentifier))
        	{
        		 if (onlineClaimDAOFactory == null)
        			 onlineClaimDAOFactory = new OnlineClaimDAOFactory();
                 if(strIdentifier!=null)
                 {
                     return onlineClaimDAOFactory.getDAO(strIdentifier);
                 }//end of if(strIdentifier!=null)
                 else
                     return null;
        		
        	}else{
        		
			if (onlineAccessDAOFactory == null)
				onlineAccessDAOFactory = new OnlineAccessDAOFactory();
			if(strIdentifier!=null)
			{
				return onlineAccessDAOFactory.getDAO(strIdentifier);
			}//end of if(strIdentifier!=null)
			else
				return null;
        	}
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "error."+strIdentifier+".dao");
		}//end of catch(Exception exp)
	}//End of getOnlineAccessDAO(String strIdentifier)
	
	
	 /**
     * DashBoard Details
     */

    public ArrayList getPreAuthSearchList(ArrayList alSearchCriteria) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getPreAuthSearchList(alSearchCriteria);
    }//end of getCorpFocusClaimsDetails(String enrollmentId)


	 /**
    * DashBoard Details
    */

   public ArrayList getPartnerPreAuthSearchList(ArrayList alSearchCriteria) throws TTKException {
   	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
       return onlineProviderDAOImpl.getPartnerPreAuthSearchList(alSearchCriteria);
   }//end of getCorpFocusClaimsDetails(String enrollmentId)
    
    
    
    
    
    /**
     * 
     * get The Authorization details on click on the table
     */

    public PreAuthSearchVO getAuthDetails(String empanelNo,String status,Long patAuthSeqId) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getAuthDetails(empanelNo,status,patAuthSeqId);
    }//end of PreAuthSearchVO getAuthDetails(String empanelNo,String status)
    
    
    /**
     * 
     * get The Authorization details on click on the table
     */

    public ArrayList getShortfallList(Long patAuthSeqId) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getShortfallList(patAuthSeqId);
    }//end of PreAuthSearchVO getAuthDetails(String empanelNo,String status)
    
    
    public String[] getChequeDetails(String chequeNumber) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getChequeDetails(chequeNumber);
    }//end of getCorpFocusClaimsDetails(String enrollmentId)
    
    
    
    public String[] getInvoiceDetails(String empanelNo,String empanelNumber) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getInvoiceDetails(empanelNo,empanelNumber);
    }//end of getCorpFocusClaimsDetails(String empanelNo)
    
    /*
     * Get Log Details
     */
    public ArrayList getLogData(String strUserId) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
 	       return onlineProviderDAOImpl.getLogData(strUserId);
 	   }//end of getOnlinePolicyList(ArrayList alSearchCriteria)
    /*
     * Get Recent Reports Downloaded
     */
    public ArrayList getBatchRenconcilList(ArrayList alSearchCriteria) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getBatchRenconcilList(alSearchCriteria);
    }//end of getBatchRenconcilList(ArrayList alSearchCriteria)
    
    
    
    /*
     * Get Batch Reconciliation Invoice details 
     */
    
    public ArrayList<BatchListVO> getBatchInvDetails(Long BatchSeqId, String strFlag, String EmpnlNo) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getBatchInvDetails(BatchSeqId,strFlag,EmpnlNo);
    }//end of getBatchInvDetails(Long BatchSeqId, String strVar)
    
    
    /*
     * Get getBatch Invoice List
     */
    public ArrayList getBatchInvoiceList(ArrayList alSearchCriteria) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getBatchInvoiceList(alSearchCriteria);
    }//end of getBatchInvoiceList(ArrayList alSearchCriteria)
    
    
    /*To Get the Invoice Report Details
     * (non-Javadoc)
     * @see com.ttk.business.onlineforms.providerLogin.OnlineProviderManager#getBatchInvReportDetails(java.lang.String, java.lang.String)
     */
    
    public String[] getBatchInvReportDetails(String invNo,String EmpnlNo) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getBatchInvReportDetails(invNo, EmpnlNo);
    }//end of getCorpFocusClaimsDetails(String enrollmentId)
    
    
    
    /*
     * Get getBatch Invoice List
     */
    public ArrayList<BatchListVO> getOverDueList(ArrayList<Object> arrayList) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getOverDueList(arrayList);
    }//end of getBatchInvoiceList(ArrayList alSearchCriteria)
    
    
    /*
     * Get Finance DashBoard Details
     */
    public String[] getFinanceDahBoard(String empanelNo,String fromDate,String toDate) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getFinanceDahBoard(empanelNo,fromDate,toDate);
    }//end of String[] getFinanceDahBoard(String empanelNo)
    
    
    /*
     * Get Recent Reports Downloaded
     */
    public ArrayList<String[]> getRecentReports(String empanelNo,Long userId) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getRecentReports(empanelNo,userId);
    }//end of getBatchInvoiceList(ArrayList alSearchCriteria)
    
    public String saveShorfallDocs(Long shortfallSeqID,FormFile formFile) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.saveShorfallDocs(shortfallSeqID,formFile);
    }//end of getBatchInvoiceList(ArrayList alSearchCriteria)
    
    public String[] getMemProviderForShortfall(Long shortfallSeqID,String patOrClm) throws TTKException {
    	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
        return onlineProviderDAOImpl.getMemProviderForShortfall(shortfallSeqID,patOrClm);
    }//end of getBatchInvoiceList(ArrayList alSearchCriteria)
    
    
    
  	/**
     * This method is to save the Claims Data which is uploading thru Excel 
     * @param    
     * returns a positive Integer 
     * @exception throws TTKException
     */
    
 	
 	public String[] uploadingClaims(String batchRefNo, Long userSeqId) throws TTKException{
 		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	     return claimsDAO.uploadingClaims(batchRefNo, userSeqId);
 	}
 	
 	public String saveClaimXML(InputStream inputStream2, String fileName, Long userSeqId) throws TTKException{
 		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	     return claimsDAO.saveClaimXML(inputStream2,fileName,userSeqId);
 	}
 	
    //Provider Claims
 	public String[] saveProviderClaims(InputStream inputStream2, String fileName, Long userSeqId) throws TTKException{
 		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	     return claimsDAO.saveProviderClaims(inputStream2,fileName,userSeqId);
 	}
 	
   public ArrayList getClaimSearchList(ArrayList alSearchCriteria) throws TTKException {
   	claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
       return claimsDAO.getClaimSearchList(alSearchCriteria);
   }//end of getCorpFocusClaimsDetails(String enrollmentId)
    
   //Added for Partner Log Search
   public ArrayList  getPartnerClaimSearchList(ArrayList alSearchCriteria) throws TTKException {
	   	claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	       return claimsDAO.getPartnerClaimSearchList(alSearchCriteria);
	   }//end of getCorpFocusClaimsDetails(String enrollmentId)

   
   
   public String[] getBatchNoForClaims(String hospSeqId,String addedBy,String receiveDate,String receiveTime,String receiveDay,String sourceType) throws TTKException {
	   claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
       return claimsDAO.getBatchNoForClaims(hospSeqId,addedBy,receiveDate,receiveTime,receiveDay,sourceType);
   }//end of getBatchNoForClaims(String hospSeqId,String addedBy)
   
   public Object[] getClaimSubmittedDetails(String batchNo) throws TTKException {
	   claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
       return claimsDAO.getClaimSubmittedDetails(batchNo);
   }//end of Object[] getClaimSubmittedDetails(String batchNo)
   
   public Object[] getClaimDetails(Long patAuthSeqId) throws TTKException {
	   claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
       return claimsDAO.getClaimDetails(patAuthSeqId);
   }//end of Object[] getClaimSubmittedDetails(String batchNo)


@Override
public PreAuthSearchVO getPreAuthMetaData(CashlessDetailVO cashlessDetailVO,
		UserSecurityProfile userSecurityProfile, String string, String string2,
		Object object, String string3, String string4) throws TTKException{
	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
    return onlineProviderDAOImpl.getPreAuthMetaData(cashlessDetailVO,userSecurityProfile,string,string2,object,string3,string4);
}


@Override
public String saveProviderShorfallDocs(ArrayList inputData) throws TTKException {
	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
    return onlineProviderDAOImpl.saveProviderShorfallDocs(inputData);
}


@Override
public ArrayList<PartnerClaimSubmissionVo> savePartnerClaims(PartnerClaimSubmissionVo partnerClaimSubmissionVo) throws TTKException {
	onlineProviderDAOImpl = (OnlineProviderDAOImpl)this.getOnlineAccessDAO("providerLogin");
    return onlineProviderDAOImpl.savePartnerClaims(partnerClaimSubmissionVo);
}

public String saveReClaimXML(InputStream inputStream2, String fileName, Long userSeqId, String claimsSubmissionTypes) throws TTKException{
 		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	     return claimsDAO.saveReClaimXML(inputStream2,fileName,userSeqId,claimsSubmissionTypes);
 	}
	
	public String[] uploadingClaimsRe(String batchRefNo, Long userSeqId) throws TTKException{
 		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	     return claimsDAO.uploadingClaimsRe(batchRefNo, userSeqId);
 	}


	
	public String saveAuditClaimUploadXML(FileInputStream inputStream2,String fileName, Long userSeqId) throws TTKException {
		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	     return claimsDAO.saveAuditClaimUploadXML(inputStream2,fileName,userSeqId);
	}
	
	public ArrayList<Object> getAuditDataUploadList(ArrayList<Object> alSearchCriteria) throws TTKException {
		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	    	return claimsDAO.getAuditDataUploadList(alSearchCriteria);
	    }
	
	public int deleteUploadedData(String strDeletedPolicyNo, Long userSeqId) throws TTKException {
		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	    	return claimsDAO.deleteUploadedData(strDeletedPolicyNo,userSeqId);
	    }


	@Override
	public String[] uploadingReSubmissionClaims(String batchRefNo,Long userSeqId) throws TTKException {
		claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	     return claimsDAO.uploadingReSubmissionClaims(batchRefNo,userSeqId);
	}
	
	@Override
	public ArrayList getReSubmissionClaimSearchList(ArrayList alSearchCriteria) throws TTKException {
	 	claimsDAO = (ClaimsDAOImpl)this.getOnlineAccessDAO("providerClaim");
	       return claimsDAO.getReSubmissionClaimSearchList(alSearchCriteria);
	}
}//end of OnlineProviderManagerBean
