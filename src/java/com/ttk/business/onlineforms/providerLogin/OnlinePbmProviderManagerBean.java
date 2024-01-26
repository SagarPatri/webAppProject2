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


import java.io.InputStream;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import org.apache.struts.upload.FormFile;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.onlineforms.OnlineAccessDAOFactory;
import com.ttk.dao.impl.onlineforms.providerLogin.OnlinePbmProviderDAOImpl;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class OnlinePbmProviderManagerBean implements OnlinePbmProviderManager{
	
	private OnlineAccessDAOFactory onlineAccessDAOFactory = null;
	private OnlinePbmProviderDAOImpl onlinePbmProviderDAOImpl = null;
	
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
			if (onlineAccessDAOFactory == null)
				onlineAccessDAOFactory = new OnlineAccessDAOFactory();
			if(strIdentifier!=null)
			{
				return onlineAccessDAOFactory.getDAO(strIdentifier);
			}//end of if(strIdentifier!=null)
			else
				return null;
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "error."+strIdentifier+".dao");
		}//end of catch(Exception exp)
	}//End of getOnlineAccessDAO(String strIdentifier)
	
	/**
     * Add Eligibility Details
     */
	 public Long addPreAuthGeneralDetails(PbmPreAuthVO preAuthVO) throws TTKException{
		 onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
	        return onlinePbmProviderDAOImpl.addPreAuthGeneralDetails(preAuthVO);
	 }
	 /**
	     * member validation
	     */
	 public   PbmPreAuthVO  validateMemberID(PbmPreAuthVO preAuthVO) throws TTKException{
		 onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
	        return onlinePbmProviderDAOImpl.validateMemberID(preAuthVO);
	 }
	  /*
	     * get all preauth  details
	     */
	  public PbmPreAuthVO getAllPreAuthDetails(Long preAuthSeqID) throws TTKException{
		  
		  onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
	        return onlinePbmProviderDAOImpl.getAllPreAuthDetails(preAuthSeqID);
	  }
	  /*
	     * chechClaimElegibility
	     */
	  public String checkClaimElegibility(Long preAuthSeqID) throws TTKException{
		  
		  onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
	        return onlinePbmProviderDAOImpl.checkClaimElegibility(preAuthSeqID);
	  }
	  
	  
	 /**
     * Add Icd Details
     */

	public Integer addIcdDetails(PbmPreAuthVO preAuthVO) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.addIcdDetails(preAuthVO);
    }//end of addIcdDetails(PbmPreAuthVO preAuthVO)
	/**
     * Add drug Details
     */

	public Integer addDrugDetails(PbmPreAuthVO preAuthVO) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.addDrugDetails(preAuthVO);
    }//end of addDrugDetails(PbmPreAuthVO preAuthVO)
	
	public ActivityDetailsVO getTariffDetails(PbmPreAuthVO pbmPreAuthVO) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.getTariffDetails(pbmPreAuthVO);
    }//end of addDrugDetails(PbmPreAuthVO preAuthVO)
	    
	  /*
     * Proccess the preauth request authorization
     */
    public Long requstAuthorization(PbmPreAuthVO preAuthVO) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.requstAuthorization(preAuthVO);
    }
    
     public long requstAuthWebservice(Long preAuthSeqID,PbmPreAuthVO pbmPreAuthVO) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.requstAuthWebservice(preAuthSeqID,pbmPreAuthVO);
    }
    /*
     * Delete diagnosis details
     */
    public Integer deleteDiagnosisDetails(String strIcdDtlSeqID,String preAuthSeqID) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.deleteDiagnosisDetails(strIcdDtlSeqID,preAuthSeqID);
    }
    /*
     * Delete drug details
     */
    public Integer deleteDrugDetails(String striDrugDtlSeqID,String preAuthSeqID) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.deleteDrugDetails(striDrugDtlSeqID,preAuthSeqID);
    }
    public ArrayList<PbmPreAuthVO> getPbmPreAuthList(ArrayList<Object> alData,Long userSeqID, Long hospSeqId) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.getPbmPreAuthList(alData,userSeqID, hospSeqId);
    }
    
    
    /*
     * claim  submit details
     */
    
    public PbmPreAuthVO getPBMSubmitClaim(ArrayList<Object> claimData) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.getPBMSubmitClaim(claimData);
    }
    
    /*
     * PBM file Upload
     */
    
    public long SavePBMUploadFile(String preAuthSeqID,String generateType,FormFile formFile) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.SavePBMUploadFile(preAuthSeqID,generateType,formFile);
    }
    /*
     * PBM claim status search
     */
    public ArrayList getPbmClaimList(ArrayList alSearchCriteria,Long userSeqID) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.getPbmClaimList(alSearchCriteria,userSeqID);
    }
    /*
     * claim  details
     */
    
    public PbmPreAuthVO getClaimDetails(Long claimSeqNo, Long preAuthSeqId) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.getClaimDetails(claimSeqNo,preAuthSeqId);
    }
   
    public long requstComp_preauth(Long preAuthSeqID,PbmPreAuthVO pbmPreAuthVO) throws TTKException{
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.requstComp_preauth(preAuthSeqID,pbmPreAuthVO);
    }
    
    public String saveClaimXML(InputStream inputStream, String fileName, Long userSeqId) throws TTKException{
    	
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.saveClaimXML(inputStream,fileName,userSeqId);
    }
    
    public String[] uploadingClaims(String batchRefNo, Long userSeqId) throws TTKException{
    	
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
        return onlinePbmProviderDAOImpl.uploadingClaims(batchRefNo,userSeqId);
    }
    
    public String saveAppealComments(Long preAuthSeqId,String appealComments) throws TTKException
    {
    	onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
       return onlinePbmProviderDAOImpl.saveAppealComments( preAuthSeqId, appealComments);
    }

	@Override
	public PbmPreAuthVO geMemberDetailsOnEnrollId(String enrollId, String benifitTypeVal, Long hospSeqId) throws TTKException {
		
		onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
	       return onlinePbmProviderDAOImpl.geMemberDetailsOnEnrollId( enrollId, benifitTypeVal,hospSeqId);
	}

	@Override
	public String[] getTobForBenefits(String benifitTypeVal, String enrollId) throws TTKException {
		onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
	       return onlinePbmProviderDAOImpl.getTobForBenefits( benifitTypeVal, enrollId);
	}

	@Override
	public int updateLogTable(ArrayList<String> dataList) throws TTKException {
		onlinePbmProviderDAOImpl = (OnlinePbmProviderDAOImpl)this.getOnlineAccessDAO("pbmProviderLogin");
	       return onlinePbmProviderDAOImpl.updateLogTable(dataList);
		
	}
}//end of OnlineProviderManagerBean
