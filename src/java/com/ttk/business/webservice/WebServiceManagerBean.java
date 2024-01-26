/**
 * @ (#) WebServiceManagerBean.java Jun 14, 2006
 * Project       : TTK HealthCare Services
 * File          : WebServiceManagerBean.java
 * Author        :
 * Company       : Span Systems Corporation
 * Date Created  : Jun 14, 2006
 * @author       : Krishna K. H
 * Modified by   :
 * Modified date :
 * Reason        :
 */
package com.ttk.business.webservice;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import oracle.xdb.XMLType;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.common.messageservices.EmailHelper;
import com.ttk.common.messageservices.NotificationHelper;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.webservice.PreApprovalStatusResponse;
import com.ttk.dao.impl.webservice.WebServiceDAOFactory;
import com.ttk.dao.impl.webservice.WebServiceDAOImpl;
import com.ttk.dto.common.CommunicationOptionVO;
import com.ttk.dto.webservice.AuthenticationVO;
import com.ttk.dto.webservice.OnlineIntimationVO;
import com.ttk.dto.webservice.VidalClaimWireRestVO;
import com.ttk.dto.webservice.VidalWireRestVO;
import com.ttk.dao.impl.onlineforms.OnlineAccessDAOImpl;

import org.dom4j.Document;

import java.io.InputStream;
import java.util.ArrayList;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class WebServiceManagerBean implements WebServiceManager{

    private WebServiceDAOFactory webserviceDAOFactory = null;
    private WebServiceDAOImpl webserviceDAO=null;
    private static final Logger log = Logger.getLogger( WebServiceManagerBean.class);
    /**
     * This method returns the instance of the data access object to initiate the required tasks
     * @param strIdentifier String object identifier for the required data access object
     * @return BaseDAO object
     * @exception throws TTKException
     */
    private BaseDAO getWebServiceDAO(String strIdentifier) throws TTKException
    {
        try
        {
            if (webserviceDAOFactory == null)
                webserviceDAOFactory = new WebServiceDAOFactory();
            if(strIdentifier!=null)
            {
                return webserviceDAOFactory.getDAO(strIdentifier);
            }//end of if(strIdentifier!=null)
            else
                return null;
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "error."+strIdentifier+".dao");
        }//end of catch(Exception exp)
    }//End of getWebServiceDAO(String strIdentifier)

    /**
     * This method saves the Policy information.
     * @param String object which contains the policy and member details in XML format.
     * @return String object which contains Policy information.
     * @exception throws TTKException.
     */
    public String savePolicy(String document) throws TTKException {
        webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
        return webserviceDAO.savePolicy(document);
    }//end of savePolicy(String document)

    public String savePolicy(String document,String strCompAbbr)throws TTKException {
        webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
        return webserviceDAO.savePolicy(document,strCompAbbr);
    }//end of savePolicy(String document)

    /**
     * This method returns the consolidated list of Policy Number.
     * @param String object which contains the parameter in XML format.
     * @return String object which contains Policy Numbers.
     * @exception throws TTKException.
     */
    public String getConsolidatedPolicyList(String strFromDate,String strToDate,long lngInsSeqID,long lngProductSeqID,long lngOfficeSeqID,String strEnrTypeID) throws TTKException {
        webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
        return webserviceDAO.getConsolidatedPolicyList( strFromDate, strToDate, lngInsSeqID, lngProductSeqID, lngOfficeSeqID, strEnrTypeID);
    }//end of getConsolidatedPolicyList(String strValue)

    /**
     * This method method saves the Number of policy uploaded and number of policy rejected in softcopy upload.
     * @param String object which contains the softcopy upolad status in XML format.
     * @return String object which contains .
     * @exception throws TTKException.
     */
    public String  saveUploadStatus(String strBatchNumber,long lngNum_of_policies_rcvd) throws TTKException {
        webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
        return webserviceDAO.saveUploadStatus(strBatchNumber,lngNum_of_policies_rcvd);
    }//end of saveUploadStatus(String strValue)

    /**
     * This method method returns the data from TTKOffice,Product or Branch.
     * @param String object which contains the table name for which data in is required.
     * @return String object which contains .
     * @exception throws TTKException.
     */
    public String getTableData(String strIdentifier,String strID) throws TTKException{
        webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
        return webserviceDAO.getTableData(strIdentifier,strID);
    }//end of getTableData(String strIdentifier)
    
    /**
     * This method method returns the Rule Errors.
     * @param String object which contains the Batch Number for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String getRuleErrors(String strBatchNbr) throws TTKException {
    	webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
    	return webserviceDAO.getRuleErrors(strBatchNbr);
    }//end of getRuleErrors(String strBatchNbr)

    /**
	 * This method method returns the USerID And Vidal ID as A String 
	 * @param String object which contains the Policy Number for which data in is required.
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */

	public String individualLoginService(String strVidalID,String strIMEINumber)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.individualLoginService(strVidalID,strIMEINumber);
	}
	
	//ProjectX
	/**
	 * This method method returns the get the Xml and saves it in a folder to test and confirm the web service is working fine. 
	 * @param String object which contains the Policy NInsurance Idumber for which data in is required.
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */

	public String getXmlandSaveXml(String strInsID)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getXmlandSaveXml(strInsID);
	}
	
	
	/**
	 * This method method returns theSuccess Message
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the Policy Number for which data in is required.
	 * @param String object which contains the Diagnostic Details for which data in is required.
	 * @param String object which contains the ClaimAmount for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String claimIntimationService(String strVidalId,String strPolicyNumber,String strDiagnosticDetails,String strClaimAmount,String strHospitalName,String strHospitalAddress,String strDtAdmission,String strDtDischarge,String strIMEINumber)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.claimIntimationService(strVidalId,strPolicyNumber,strDiagnosticDetails,strClaimAmount,strHospitalName,strHospitalAddress,strDtAdmission,strDtDischarge,strIMEINumber);
	}
	
	/**
	 * This method method returns the List of Claims and Policies as a form of String
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the Policy Number for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String policySearchService(String strVidalId,String strIMEINumber)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.policySearchService(strVidalId,strIMEINumber);
	}
	
	/**
	 * This method method returns the Claim Details
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the Policy Number for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String claimHistoryService(String strVidalId, String strPolicyNumber,String strMode,String strIMEINumber)throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.claimHistoryService(strVidalId,strPolicyNumber,strMode,strIMEINumber);
	}
	
	/**
	 * This method method returns the Claim status as a String
	 * @param String object which contains the Vidal Id for which data in is required.
	 * @param String object which contains the Policy Number for which data in is required.
	 * @return String object which contains Rule Errors.
	 * @exception throws TTKException.
	 */
	public String claimStatusService(String strVidalId, String strPolicyNumber,String strClaimNumber,String strIMEINumber)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.claimStatusService(strVidalId,strPolicyNumber,strClaimNumber,strIMEINumber);
	}

	/**
     * This method method returns the Text 
     * @param String object which contains the Vidal Id for which data in is required.
     *  @param String object which contains the strHospID for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	
	public String feedBackService(String strVidalId,String strIMEINumber,String strHospId,int intRating)throws TTKException
	{
	webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
	return webserviceDAO.feedBackService(strVidalId,strIMEINumber,strHospId,intRating);
	}

	/**
     * This method method returns the Text 
     * @param String object which contains the Vidal Id for which data in is required.
     *  @param String object which contains the strPolicyNumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String getDependentDetailsService(String strVidalId, String strPolicyNumber,String strIMEINumber)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getDependentDetailsService(strVidalId,strPolicyNumber,strIMEINumber);
	}
	 /**
     * This method returns the list of hospitals matches with longitude and latitude 
     * @param int object which contains the intLatitude for which data in is required.
     * @param int object which contains the intLongitude for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String getHospInfoService(int startNum,int endNum,String intLatitude ,String intLongitude,String strOperator,double dbKilometers,String strIMEINumber)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getHospInfoService(startNum,endNum,intLatitude,intLongitude,strOperator,dbKilometers,strIMEINumber);
	}

	/**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String getHospDetailsService(String searchValue,int startNum,int intNoOfRows,String strIMEINumber,String strStateID,String strCityID)throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getHospDetailsService(searchValue,startNum,intNoOfRows,strIMEINumber,strStateID,strCityID);
	}
	
	
	/**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public StringBuffer getEcardTemplate(String strVidalId,String strPolicyNumber,String strIMEINumber)throws TTKException  {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getEcardTemplate(strVidalId,strPolicyNumber,strIMEINumber);
	}
	
	/**
     * This method method returns the Text ,Color and details
     * @param String object which contains the strDate for which data in is required.
	 * @param String object which contains the strIMEINumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String dailyTipService(String strDate,String strIMEINumber) throws TTKException  {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.dailyTipService(strDate,strIMEINumber);
	}
	/**
     * This method method returns the byte[] code
     * @param String object which contains the strDate for which data in is required.
	 * @param String object which contains the strIMEINumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	
	public byte[] dailyTipImageService(String strDate,String strIMEINumber) throws TTKException  {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.dailyTipImageService(strDate,strIMEINumber);
	}
/**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String docAroundClockStatusService() throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.docAroundClockStatusService();
    }
    /**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String claimFeedBackService(String strVidalId,String strIMEINumber,String strClaimNumber,String strFeedback) throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.claimFeedBackService(strVidalId,strIMEINumber,strClaimNumber,strFeedback);
    }
    /**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String askExpertOpnionService(String strExpertType,String strUserEmail,String strUserQuery,String strVidalId,String strIMEINumber) throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.askExpertOpnionService(strExpertType,strUserEmail,strUserQuery,strVidalId,strIMEINumber);
    }
    
    /**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String getValidatePhoneNumber(String strValue) throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getValidatePhoneNumber(strValue);
    }

    /**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String getProviderList(String genTypeId, String hospName, String StateTypeID, String cityDesc, String location) throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getProviderList(genTypeId, hospName, StateTypeID, cityDesc, location);
    }
    
    /**
     * This method method returns the Text 
     * @param String object which contains the searchValue for which data in is required search Criteria.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
    public String getDiagTests(Long hospSeqId) throws TTKException {
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getDiagTests(hospSeqId);
    }
    
    /**
     * This method method returns the Text 
     * @param String object which contains the Vidal Id for which data in is required.
     *  @param String object which contains the strPolicyNumber for which data in is required.
     * @return String object which contains Rule Errors.
     * @exception throws TTKException.
     */
	public String getPreAuthClaimsCount(String strVidalId, String strPolicyNumber,String strIMEINumber)throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getPreAuthClaimsCount(strVidalId,strPolicyNumber,strIMEINumber);
	}
    

	  //Mobile App Webservices ////////
		public VidalWireRestVO getLoginRegister(VidalWireRestVO vidalwirerestvo) throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getLoginRegister(vidalwirerestvo);
		}
		
		public VidalWireRestVO getLoginRegisterOTP(VidalWireRestVO vidalwirerestvo) throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getLoginRegisterOTP(vidalwirerestvo);
		}
		
		public VidalWireRestVO getLoginForgetPwdService(VidalWireRestVO vidalwirerestvo) throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getLoginForgetPwdService(vidalwirerestvo);
		}
		
		
		public VidalWireRestVO getLoginService(VidalWireRestVO vidalwirerestvo) throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getLoginService(vidalwirerestvo);
		}
		
	 	public ArrayList getDependentList(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getDependentList(onlineIntimationVO);
		  }
	 	
		public ArrayList getIntervalTypeList() throws TTKException {
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getIntervalTypeList();
		}
		
		public ArrayList getMedTimeList() throws TTKException {
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getMedTimeList();
		}
		
		public int saveMedReminder(OnlineIntimationVO onlineIntimationVO) throws TTKException {
			 webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.saveMedReminder(onlineIntimationVO);
		}

		public ArrayList getMedReminder(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getMedReminder(onlineIntimationVO);
		  }
	 	
		
		
		public int deleteMedReminder(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.deleteMedReminder(onlineIntimationVO);
		  }
		
		
		public ArrayList getMedReminderHistory(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getMedReminderHistory(onlineIntimationVO);
		  }
		
		  public ArrayList getPreAuthList(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getPreAuthList(onlineIntimationVO);
	      }
		  public ArrayList getPreAuthDetails(VidalClaimWireRestVO vidalClaimWireRestVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getPreAuthDetails(vidalClaimWireRestVO);
	      }
		
		public ArrayList getPolicyDetails(Long ocoPolicyGrpseqid) throws TTKException {
			 webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getPolicyDetails(ocoPolicyGrpseqid);
		}
	  
		
		  public ArrayList getClaimList(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getClaimList(onlineIntimationVO);
		  }
		  public ArrayList getClaimDetails(VidalClaimWireRestVO vidalClaimWireRestVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getClaimDetails(vidalClaimWireRestVO);
		  }
		  
		  public ArrayList getMyProfile(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getMyProfile(onlineIntimationVO);
		  }
		
		  
		  public ArrayList updateMyprofile(OnlineIntimationVO onlineIntimationVO) throws TTKException{
			  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.updateMyprofile(onlineIntimationVO);
		  }
		  
	  
		public byte[] getHealthTips(OnlineIntimationVO onlineIntimationVO) throws TTKException  {
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getHealthTips(onlineIntimationVO);
		}

		public byte[] getPolicyTOBpdf(Long ocoPolicyGrpseqid) throws TTKException {
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getPolicyTOBpdf(ocoPolicyGrpseqid);
		}
		
		
		public ArrayList getFAQ() throws TTKException  {
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getFAQ();
		}

		public ArrayList saveOnlineClaimSubmission(OnlineIntimationVO onlineIntimationVO) throws TTKException {
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.saveOnlineClaimSubmission(onlineIntimationVO);
		}
		
		public ArrayList getOnlineClaimSubmission(OnlineIntimationVO onlineIntimationVO) throws TTKException {
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getOnlineClaimSubmission(onlineIntimationVO);
		}

		public ArrayList claimSubmissionTrigger(OnlineIntimationVO onlineIntimationVO) throws TTKException {
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.claimSubmissionTrigger(onlineIntimationVO);
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Object> getCountryList() throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getCountryList();
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Object> getStateList(String alkCountryId) throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getStateList(alkCountryId);
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Object> getAreaList(String alkStateId) throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getAreaList(alkStateId);
		}
		
			
		@SuppressWarnings("unchecked")
		public ArrayList<Object> getProTypeSpeciality(String module) throws TTKException{
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getProTypeSpeciality(module);
		}
				
		public ArrayList searchProviderList( String stralkCountry,String stralkState,String stralkProviderType,String alkSpeciality,String alkNetworkType) throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.searchProviderList(stralkCountry,stralkState,stralkProviderType,alkSpeciality,alkNetworkType);
		}
		
		public int saveContactUs(String alAlkootId,String alMobileNo,String alEmailId, String alSubModule, String alIssueDesc)  throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.saveContactUs(alAlkootId,alMobileNo,alEmailId,alSubModule,alIssueDesc);
		}
		public Long getSeqIds(String empanelId,String getType)  throws TTKException{
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getSeqIds(empanelId,"HOS")	;
		}
		
		public ArrayList<Object> getbanksearchData(String ocomodule,String ocobankdestination, String ocobankcity,String ocobankarea, String ocobankbranch)  throws TTKException{		
		webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		return webserviceDAO.getbanksearchData(ocomodule,ocobankdestination,ocobankcity,ocobankarea,ocobankbranch);
		}
		public ArrayList<Object> getProviderNWtype(String ocoPolicyGrpseqid)  throws TTKException{		
			webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			return webserviceDAO.getProviderNWtype(ocoPolicyGrpseqid);
			}
		
		/**
		 * This method calls the Notification methods based on Message Type and Message present status
		 * @param strMsgType The String Object which contains the type of the Message
		 * @exception throws TTKException
		 */
		public void sendSmsImmediate(String strMsgType) throws TTKException
		{
			

			ArrayList alMailList = new ArrayList();
			CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
			NotificationHelper notificationHelper = null;
			try
			{
				alMailList = getSendMailList(strMsgType);
				if(alMailList != null)
				{
				notificationHelper = new NotificationHelper();
					for(int i=0;i<alMailList.size();i++)
					{
						cOptionVO = (CommunicationOptionVO)alMailList.get(i);
						if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("SMS") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
						{
							notificationHelper.sendSMS(cOptionVO);
						}//end of if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
					}//end for(int i=0;i<alMailList.size();i++)
				}//end of if(alMailList != null)
			}//end of try
			catch (Exception mex)
			{
				throw new TTKException(mex, "error.message");
			}//end of MessagingException block
		}//end of sendSMS(String strMsgType)
		
		//sms preauth schedular
		
		
		 public ArrayList getSendMailList(String strMsgType) throws TTKException {
			 webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		        return webserviceDAO.getSendMailList(strMsgType);
		    }//end of getSendMailList(String strMsgType)
		 
		 
		 /**
			 * This method calls the Notification methods based on Message Type and Message present status
			 * @param strMsgType The String Object which contains the type of the Message
			 * @exception throws TTKException
			 */
			public void sendEmailImmediate(String strMsgType) throws TTKException
			{
				ArrayList alMailList = new ArrayList();
				CommunicationOptionVO cOptionVO = new CommunicationOptionVO();
				try
				{
					alMailList = getSendMailList(strMsgType);
					//System.out.println("alMailList size::"+alMailList.size());
				//	System.out.println("strMsgType"+strMsgType);
					if(alMailList != null)
		            {
					//	System.out.println("message count"+alMailList.size());
						for(int i=0;i<alMailList.size();i++)
						{
							cOptionVO = (CommunicationOptionVO)alMailList.get(i);
							if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
							{
								try{
								EmailHelper.message(cOptionVO);	
								}
								catch(Exception mex)
								{
									log.info("Mail Log--->"+mex);
								}						
							}//end of if(TTKCommon.checkNull(cOptionVO.getMsgType()).equalsIgnoreCase("EMAIL") && TTKCommon.checkNull(cOptionVO.getPrsntYN()).equalsIgnoreCase("Y"))
						}//end for(int i=0;i<alMailList.size();i++)
					}//end of if(alMailList != null)
				}//end of try
				catch (Exception mex)
				{
					throw new TTKException(mex, "error.message");                                                    
				}//end of MessagingException block
			}//end of sendMessage(String strMsgType)
			
			@SuppressWarnings("unchecked")
			public ArrayList<Object> getBenifitTypeList() throws TTKException{
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getBenifitTypeList();
			}
			@Override
			public String alAlhiPreauth(String preappravalData,String empanelId) throws TTKException {
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.alAlhiPreauth(preappravalData,empanelId);
			}
			
			@Override
			public PreApprovalStatusResponse checkPreapprovalStatus(String preapprovalNo) throws TTKException {
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.checkPreapprovalStatus(preapprovalNo);
			}
		 
			  public ArrayList getWindowPeriod(Long ocoUserId) throws TTKException{
				  webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
					return webserviceDAO.getWindowPeriod(ocoUserId);
		      }
			  public ArrayList saveShortfallSubmission(OnlineIntimationVO onlineIntimationVO) throws TTKException {
					webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
					return webserviceDAO.saveShortfallSubmission(onlineIntimationVO);
				}
			  
		  
			  public int saveDocUploads(String strPreauthNo,byte[] inputPdfData) throws TTKException{
					webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
			    	return webserviceDAO.saveDocUploads(strPreauthNo, inputPdfData);
			    }

			
			public ArrayList<Object> getBenifitTypeListNew() throws TTKException {
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getBenifitTypeListNew();
			}

			public ArrayList getBenefitLimit(OnlineIntimationVO onlineIntimationVO) throws TTKException {
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
				return webserviceDAO.getBenefitLimit(onlineIntimationVO);}
			
			@Override
			public ArrayList updateDependentDetails(OnlineIntimationVO onlineIntimationVO) throws TTKException {
				
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		    	return webserviceDAO.updateDependentDetails(onlineIntimationVO);
			}

			@Override
			public int sendCertificateMail(String memberSeqId) throws TTKException {
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		    	return webserviceDAO.sendCertificateMail(memberSeqId);
			}

			public String alAlhiClaim(String inputPreapprovalXML) throws TTKException {
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		    	return webserviceDAO.alAlhiClaim(inputPreapprovalXML);
			}

			@Override
			public String getAppVersionDetails(OnlineIntimationVO onlineIntimationVO) throws TTKException {
				webserviceDAO = (WebServiceDAOImpl)this.getWebServiceDAO("policy");
		    	return webserviceDAO.getAppVersionDetails(onlineIntimationVO);
			}
}// End of WebServiceManagerBean
