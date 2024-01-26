/**
 * @ (#) OnlinePreAuthManagerBean.java Jul 19, 2007
 * Project 	     : TTK HealthCare Services
 * File          : OnlinePreAuthManagerBean.java
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

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.ttk.common.exception.TTKException;
import com.ttk.common.security.Cache;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.onlineforms.OnlineAccessDAOFactory;
import com.ttk.dao.impl.onlineforms.OnlinePreAuthDAOImpl;
import com.ttk.dao.impl.preauth.PreAuthDAOImpl;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.empanelment.LabServiceVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.DrugDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;
@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class OnlinePreAuthManagerBean implements OnlinePreAuthManager{
	
	private OnlineAccessDAOFactory onlineAccessDAOFactory = null;
	private OnlinePreAuthDAOImpl onlinePreAuthDAO = null;
	
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
				return onlineAccessDAOFactory.getPreAuthDAO(strIdentifier);
			}//end of if(strIdentifier!=null)
			else
				return null;
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "error."+strIdentifier+".dao");
		}//end of catch(Exception exp)
	}//End of getOnlineAccessDAO(String strIdentifier)
	
	@Override
	/**
	 * This method returns the instance of the data access object to initiate the required tasks
	 * @param strIdentifier String object identifier for the required data access object
	 * @return BaseDAO object
	 * @exception throws TTKException
	 */
	 public String getValidateVidalId(String vidalId)throws TTKException{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.getValidateVidalId(vidalId);		
	}//getHospitalPreAuthDashBoard(Long lngHospitalSeqid,String Type)getValidateSearchOpt
	
	
	/*
	 * This perocdure is to get the enrollment member details
	 */
	public CashlessDetailVO geMemberDetailsOnEnrollId(String EnrollId,String benifitTypeVal) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.geMemberDetailsOnEnrollId(EnrollId,benifitTypeVal);
	}//end of geMemberDetailsOnEnrollId(Long hospseqId)
	
	
	/*
	 * This perocdure is to get the enrollment member details
	 */
	public ArrayList getSelectedLabDetails(String selectedLabIds,Long PAT_INTIMATION_SEQ_ID,String enrollId) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.getSelectedLabDetails(selectedLabIds,PAT_INTIMATION_SEQ_ID,enrollId);
	}//end of geMemberDetailsOnEnrollId(Long hospseqId)
	
	
	/*
	 * This procedure is to get the Consumable details
	 */
	public LabServiceVO getConsumableDetails(String consDesc) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.getConsumableDetails(consDesc);
	}//end of geMemberDetailsOnEnrollId(Long hospseqId)
	
	
	/*
	 * This procedure is to save the Online Preauth details
	 */
	public String[] savePreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,UserSecurityProfile userSecurityProfile,String benifitTypeVal,String SubmitYN,String loginFlag,HttpServletRequest request) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.savePreAuthDetails(cashlessDetailVO,prescriptions,userSecurityProfile, benifitTypeVal,SubmitYN,loginFlag,request);
	}//end of savePreAuthDetails(Long hospseqId)
	
	
	/*
	 * This procedure is to save the Partial Online Preauth details
	 */
	public String[] savePartialPreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,UserSecurityProfile userSecurityProfile,String benifitTypeVal,String enhanceYN) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.savePartialPreAuthDetails(cashlessDetailVO,prescriptions,userSecurityProfile, benifitTypeVal,enhanceYN);
	}//end of savePreAuthDetails(Long hospseqId)
	
	
	/*
	 * This procedure is to save the Partial Online partner Preauth details
	 */
	public String[] savePartnerPartialPreAuthDetails(CashlessDetailVO cashlessDetailVO, UserSecurityProfile userSecurityProfile,String benifitTypeVal,String enhanceYN,FormFile formFile,String fileName,String description) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.savePartnerPartialPreAuthDetails(cashlessDetailVO,userSecurityProfile, benifitTypeVal,enhanceYN,formFile,fileName,description);
	}//end of savePreAuthDetails(Long hospseqId)
	
	/*
	 * Get the PreAuth Details after saving and passing the pat intimation sequence ID
	 */
	public CashlessDetailVO getPreAuthDetails(Long lPreAuthIntSeqId) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.getPreAuthDetails(lPreAuthIntSeqId);
	}//end of savePreAuthDetails(Long hospseqId)
		
	
	
	
	/*
	 * This procedure is to save the Online Preauth details Consumables
	 */
	public int savePreAuthConsumables(LabServiceVO labServiceVO,Long intimationSeqID) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.savePreAuthConsumables(labServiceVO,intimationSeqID);
	}//end of savePreAuthConsumables(LabServiceVO labServiceVO)
	
	
	/*
	 * This procedure is to get the Consumables and price Deatails
	 */
	public ArrayList getPreAuthConsumables(Long intimationSeqID) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.getPreAuthConsumables(intimationSeqID);
	}//end of geMemberDetailsOnEnrollId(Long hospseqId)
	
	/*
	 * This procedure is to get the Pharmacy and price and other Deatails
	 */
	public LabServiceVO getPreAuthPharamcyDetails(String pharmacyDesc) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.getPreAuthPharamcyDetails(pharmacyDesc);
	}//end of getPreAuthPharamcyDetails(String pharmacyDesc)
	
	
	/*
	 * This procedure is to get the Pharmacy and price and other Deatails
	 */
	public int saveDiagnosisDetails(ArrayList<DiagnosisDetailsVO> diagnosis,Long lPreAuthIntSeqId,Long userSeqId) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.saveDiagnosisDetails(diagnosis,lPreAuthIntSeqId,userSeqId);
	}//end of DiagnosisDetailsVO saveDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO)
	
	
	
	public int saveActivitiesDetails(ArrayList<ActivityDetailsVO> alActivityDetails,Long lPreAuthIntSeqId,Long userSeqId,String treatmentDate,String clinicianId,String loginFlag,HttpServletRequest request) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.saveActivitiesDetails(alActivityDetails,lPreAuthIntSeqId,userSeqId,treatmentDate,clinicianId,loginFlag,request);
	}//end of DiagnosisDetailsVO saveActivitiesDetails(DiagnosisDetailsVO diagnosisDetailsVO)
	
	public int saveDrugDetails(ArrayList<DrugDetailsVO> alDrugDetails,Long lPreAuthIntSeqId,Long userSeqId,String treatmentDate,String clinicianId,String loginFlag,HttpServletRequest request) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.saveDrugDetails(alDrugDetails,lPreAuthIntSeqId,userSeqId,treatmentDate,clinicianId,loginFlag,request);
	}//end of DiagnosisDetailsVO saveActivitiesDetails(DiagnosisDetailsVO diagnosisDetailsVO)

	/*
	 * This procedure is to get the Pharmacy and price and other Deatails
	 */
	public ArrayList<DiagnosisDetailsVO> getDiagDetails(Long lPreAuthIntSeqId) throws TTKException
	{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
		return onlinePreAuthDAO.getDiagDetails(lPreAuthIntSeqId);
	}//end of DiagnosisDetailsVO getDiagDetails(Long lPreAuthIntSeqId)
	
	public Object[] deleteDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO) throws TTKException{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
        return onlinePreAuthDAO.deleteDiagnosisDetails(diagnosisDetailsVO);
    }//end of deleteDiagnosisDetails(DiagnosisDetailsVO diagnosisDetailsVO)
	
	
	public Object[] getOnlinePreAuthDetails(Long preAuthSeqId) throws TTKException{
		onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
        return onlinePreAuthDAO.getOnlinePreAuthDetails(preAuthSeqId);
   }//end of getPreAuthDetail(PreAuthVO preauthVO,String strSelectionType)
	
	 public ArrayList getActivityCodeList(ArrayList alSearchCriteria,String actOrDrug) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getActivityCodeList(alSearchCriteria,actOrDrug);
	    }//end of getActivityCodeList(ArrayList alSearchCriteria)

	 public ArrayList getDrugCodeList(ArrayList alSearchCriteria) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getDrugCodeList(alSearchCriteria);
	    }//end of getActivityCodeList(ArrayList alSearchCriteria)

	 public ArrayList getDiagCodeList(ArrayList alSearchCriteria) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getDiagCodeList(alSearchCriteria);
	    }//end of getActivityCodeList(ArrayList alSearchCriteria)
	 
	 
	 public ActivityDetailsVO getActivityCodeDetails(Long hospSeqId,String enrollId,Long activitySeqId,Date treatmentDate,String searchType,String actOrDrug) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getActivityCodeDetails(hospSeqId,enrollId,activitySeqId,treatmentDate,searchType,actOrDrug);
	    }//end of getActivityCodeList(ArrayList alSearchCriteria)

	 public DrugDetailsVO getDrugCodeDetails(Long hospSeqId,String enrollId,Long drugSeqId,Date treatmentDate,String actOrDrug) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getDrugCodeDetails(hospSeqId,enrollId,drugSeqId,treatmentDate,actOrDrug);
	    }//end of getActivityCodeList(ArrayList alSearchCriteria)
	 
	 public Object[] getOnlinePartialPreAuthDetails(Long preAuthSeqId) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getOnlinePartialPreAuthDetails(preAuthSeqId);
	    }//end of getOnlinePreAuthDetails(String preAuthRefNo)
	 
	 public Object[] getOnlinePartnerPartialPreAuthDetails(Long preAuthSeqId) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getOnlinePartnerPartialPreAuthDetails(preAuthSeqId);
	    }//end of getOnlinePartnerPartialPreAuthDetails(String preAuthRefNo)
	 
	 public long deleteExistngDataForPreAuth(String seqIds,Long lPreAuthIntSeqId,String flag,String seqQty,Long LhospSeqId,Long userSeqId,String loginFlag,HttpServletRequest request) throws TTKException{
		 onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.deleteExistngDataForPreAuth(seqIds,lPreAuthIntSeqId, flag,seqQty,LhospSeqId,userSeqId,loginFlag,request);
	    }//end of getOnlinePreAuthDetails(String preAuthRefNo)	 
	 
	  public InputStream getProviderUploadedDocsDBFile(String fileSeqId)throws TTKException
	     {
		  onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getProviderUploadedDocsDBFile(fileSeqId);
	     }
	 
	  public ArrayList<CacheObject> getEnounterTypes(String benefitType)throws TTKException
	     {
		  onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.getEnounterTypes(benefitType);
	     }
	  public String saveDentalDetails(DentalOrthoVO dentalOrthoVO,String PatOrClm,Long lPreAuthIntSeqId,Long userSeqId,String loginFlag,HttpServletRequest request)throws TTKException
	     {
		  onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.saveDentalDetails(dentalOrthoVO,PatOrClm,lPreAuthIntSeqId,userSeqId,loginFlag,request);
	     }
	  
	  public String saveAppealComments(Long preAuthSeqId,String appealComments) throws TTKException
	     {
		  onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        return onlinePreAuthDAO.saveAppealComments( preAuthSeqId, appealComments);
	     }
	  
	  public void deleteProviderDetail(Long preAuthSeqId,Long userSeqId) throws TTKException
	     {
		  	onlinePreAuthDAO = (OnlinePreAuthDAOImpl)this.getOnlineAccessDAO("onlinepreauth");
	        onlinePreAuthDAO.deleteProviderDetail(preAuthSeqId,userSeqId);
	     }
 public String getVipYN(String enrollid) throws TTKException{
		  
		  return    onlinePreAuthDAO.getVipDetail(enrollid);
	  }
}//end of OnlinePreAuthManagerBean
