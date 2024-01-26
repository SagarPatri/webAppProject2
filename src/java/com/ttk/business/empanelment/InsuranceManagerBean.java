/**
 * @ (#) Any.java Oct 19, 2005
 * Project      : ttkHealthCareServices
 * File         : InsuranceManagerBean.java
 * Author       : Ravindra
 * Company      : SpanSystem Corp:
 * Date Created : Oct 19, 2005
 *
 * @author       :  Ravindra

 * Modified by   :
 * Modified date :
 * Reason        :
 */
package com.ttk.business.empanelment;

import java.io.FileReader;
import java.util.ArrayList;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.empanelment.EmpanelmentDAOFactory;
//added for Mail-SMS Template for Cigna
import com.ttk.dao.impl.empanelment.GroupRegistrationDAOImpl;
import com.ttk.dto.empanelment.NotificationInfoVO;
import com.ttk.dao.impl.empanelment.InsuranceDAOImpl;
import com.ttk.dao.impl.empanelment.InsuranceFeedbackDAOImpl;
import com.ttk.dao.impl.empanelment.InsuranceProductDAOImpl;
import com.ttk.dao.impl.preauth.PreAuthDAOImpl;
import com.ttk.dto.empanelment.InsuranceDetailVO;
import com.ttk.dto.empanelment.InsuranceFeedbackVO;
import com.ttk.dto.empanelment.BrokerFeedbackVO;
import com.ttk.dto.empanelment.InsuranceProductVO;
import com.ttk.dto.empanelment.ReInsuranceDetailVO;
import com.ttk.dto.insurancepricing.InsPricingVO;
import com.ttk.dto.insurancepricing.PolicyConfigVO;
import com.ttk.dto.insurancepricing.SwFinalQuoteVO;
import com.ttk.dto.insurancepricing.SwPolicyConfigVO;
import com.ttk.dto.insurancepricing.SwPricingSummaryVO;

import javax.ejb.*;

import org.apache.struts.upload.FormFile;
@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public  class InsuranceManagerBean implements InsuranceManager {
	
	private EmpanelmentDAOFactory empanelmentDAOFactory = null;
	private InsuranceDAOImpl insuranceDAO = null;
	private InsuranceFeedbackDAOImpl insuranceFeedbackDAO = null;
	private InsuranceProductDAOImpl insuranceProductDAO = null;
	//added for Mail-SMS Template for Cigna
	private GroupRegistrationDAOImpl groupRegistrationDAO = null;
	/**
	 * This method returns the instance of the data access object to initiate the required tasks
	 * @param strIdentifier String object identifier for the required data access object
	 * @return BaseDAO object
	 * @exception throws TTKException
	 */
	private BaseDAO getEmpanelmentDAO(String strIdentifier) throws TTKException
	{
		try
		{
			if(empanelmentDAOFactory == null)
				empanelmentDAOFactory = new EmpanelmentDAOFactory();
			if(strIdentifier!=null)
			{
				return empanelmentDAOFactory.getDAO(strIdentifier);
			}//end of if(strIdentifier!=null)
			else
				return null;
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "error."+strIdentifier+".dao");
		}//end of catch(Exception exp)
	}//end getEmpanelmentDAO(String strIdentifier)

	/**
     * This method returns the ArrayList, which contains the list of InsuranceCompany which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of InsuranceCompany object's which contains the InsuranceCompany details
     * @exception throws TTKException
     */
    public ArrayList getInsuranceCompanyList(ArrayList alSearchObjects) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.getInsuranceCompanyList(alSearchObjects);
	}// End of getInsuranceList(ArrayList alSearchObjects)

	/**
	 * This method returns the InsuranceDetailVO, which contains the all the insurancecompany details
	 * @param lInsuranceParentId is Insurance Company Parent Sequence ID
	 * @param lInsuranceCompanyID is of long type which is unique for each insurance company
	 * @return InsuranceCompanyDetailVO object which contains all the insurancecompany details
	 * @exception throws TTKException
	 **/
	public InsuranceDetailVO getInsuranceCompanyDetail(Long lInsuranceParentId,Long lInsuranceCompanyID) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.getInsuranceCompanyDetail(lInsuranceParentId,lInsuranceCompanyID);
	}// End of InsuranceDetailVO getInsuranceCompanyDetail(Long lInsuranceCompanyID)

	/**
	 * This method adds or updates the insurance company  details
	 * The method also calls other methods on DAO to insert/update the insurance company  details to the database
	 * @param insuranceDetailVO InsuranceDetailVO object which contains the insurance comapny  details to be added/updated
	 * @return long value, Insurance Seq Id
	 * @exception throws TTKException
	 */
	
	public long addUpdateInsuranceCompany(InsuranceDetailVO insuranceDetailVO) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.addUpdateInsuranceCompany(insuranceDetailVO);
	}//end of addUpdateInsuranceCompany(InsuranceDetailVO insuranceDetailVO)

	/**
	 * This method delete's the insurance Company  records from the database.
	 * @param strInsSeqID String object which contains the insurance company  sequence id's to be deleted
	 * @return int  value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int deleteInsuranceCompany(String strInsSeqID) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.deleteInsuranceCompany(strInsSeqID);
	}// End of deleteInsuranceCompany(String strInsSeqID)

	/**
	 * This method returns the ArrayList, which contains the InsuranceCompanyProductVO's which are populated from the database
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return ArrayList of InsuranceCompanyProductVO object's which contains the InsuranceCompany details
	 * @exception throws TTKException
	 */
	public ArrayList getProductList(ArrayList alSearchObjects) throws TTKException{
		insuranceProductDAO = (InsuranceProductDAOImpl)this.getEmpanelmentDAO("insuranceproduct");
		return insuranceProductDAO.getProductList(alSearchObjects);
	}// End of getProductList(ArrayList alSearchObjects)

	/**
	 * This method adds or updates the insurance Products
	 * The method also calls other methods on DAO to insert/update the insurance details to the database
	 * @param insuranceProductVO InsuranceProductVO object which contains the insuranceCompanyProduct details to be added/updated
	 * @return long value, Associate Office Sequence Id
	 * @exception throws TTKException
	 */
	public long addUpdateProduct(InsuranceProductVO insuranceProductVO) throws TTKException{
		insuranceProductDAO = (InsuranceProductDAOImpl)this.getEmpanelmentDAO("insuranceproduct");
		return insuranceProductDAO.addUpdateProduct(insuranceProductVO);
	}// End of addUpdateProduct(InsuranceProductVO insuranceProductVO)
	/**
	 * This method delete's the Products  records from the database.
	 * @param alProductList ArrayList object which contains the Product sequence id's to be deleted
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int deleteProduct(ArrayList alProductList)  throws TTKException{
		insuranceProductDAO = (InsuranceProductDAOImpl)this.getEmpanelmentDAO("insuranceproduct");
		return insuranceProductDAO.deleteProduct(alProductList);
	}// End of deleteProduct(ArrayList alProductList)  throws TTKException

	/**
	 * This method returns the ArrayList object of ProductVO's, which contains all the details about the products Associated to parent Insurance Company
	 * @param lInsSeqId long Insurance Sequence Id
	 * @return ArrayList object which contains all the details about the Insurance Company Associated products
	 * @exception throws TTKException
	 */
	public ArrayList getProductCode(long lInsSeqId) throws TTKException{
		insuranceProductDAO = (InsuranceProductDAOImpl)this.getEmpanelmentDAO("insuranceproduct");
		return insuranceProductDAO.getProductCode(lInsSeqId);
	}// End of getProductCode(long lInsSeqId)

	/**
	 * This method returns the ArrayList, which contains the InsuranceFeedbackVO's which are populated from the database
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return ArrayList of InsuranceFeedbackVO object's which contains the InsuranceCompany Feedback details
	 * @exception throws TTKException
	 */
	public ArrayList  getCompanyFeedbackList(ArrayList alSearchObjects) throws TTKException{
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.getCompanyFeedbackList(alSearchObjects);
	}// End of getCompanyFeedbackList(ArrayList alSearchObjects)
	
	
	//added for kocb broker login
	public ArrayList getBrokerFeedbackList(ArrayList alSearchObjects) throws TTKException{
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.getBrokerFeedbackList(alSearchObjects);
	}// End of getCompanyFeedbackList(ArrayList alSearchObjects)
	// end added for kocb broker login
	/**
	 * This method returns the InsuranceFeedbackVO, which contains the InsuranceCompany Feedback details which are populated from the database
	 * @param lngInsFeedbackSeqID long value which contains Feedback Seq ID
	 * @return InsuranceFeedbackVO which contains the InsuranceCompany Feedback details
	 * @exception throws TTKException
	 */
	public InsuranceFeedbackVO getCompanyFeedback(long lngInsFeedbackSeqID) throws TTKException{
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.getCompanyFeedback(lngInsFeedbackSeqID);
	}//end of getCompanyFeedback(long lngInsFeedbackSeqID)

	//added for kocb broker login
	public InsuranceFeedbackVO getBrokerFeedback(long lngInsFeedbackSeqID) throws TTKException{
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.getBrokerFeedback(lngInsFeedbackSeqID);
	}//end of getCompanyFeedback(long lngInsFeedbackSeqID)

	/**
	 * This method adds or updates the insurance Feedback
	 * The method also calls other methods on DAO to insert/update the insurance details to the database
	 * @param insuranceFeedbackVO InsuranceFeedbackVO object which contains the insuranceFeedback details to be added/updated
	 * @return long value, Insurance Feedback Sequence Id
	 * @exception throws TTKException
	 */
	public long addUpdateCompanyFeedback(InsuranceFeedbackVO insuranceFeedbackVO) throws TTKException {
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.addUpdateCompanyFeedback(insuranceFeedbackVO);
	}// End of addUpdateCompanyFeedback(InsuranceFeedbackVO insuranceFeedbackVO)

	
	//added for broker login kocb
/*	public long addUpdateBrokerFeedback(BrokerFeedbackVO brokerFeedbackVO) throws TTKException {
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.addUpdateBrokerFeedback(brokerFeedbackVO);
	}// End of addUpdateCompanyFeedback(InsuranceFeedbackVO insuranceFeedbackVO)
*/	
	public long addUpdateBrokerFeedback(InsuranceFeedbackVO insuranceFeedbackVO) throws TTKException {
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.addUpdateBrokerFeedback(insuranceFeedbackVO);
	}// End of addUpdateCompanyFeedback(InsuranceFeedbackVO insuranceFeedbackVO)
	/**
     * This method delete's the Insurance Feedback   records from the database.
     * @param strFeedbackSeqID String object which contains the Feedback sequence id's to be deleted
     * @param lnguserSeqID for which user has loggedin
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int deleteCompanyFeedback(String strFeedbackSeqID,long lnguserSeqID) throws TTKException{
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.deleteCompanyFeedback(strFeedbackSeqID,lnguserSeqID);
	}// End of deleteCompanyFeedback(String strFeedbackSeqID,long lnguserSeqID)

    //added for kocb 
    
    public int deleteBrokerFeedback(String strFeedbackSeqID,long lnguserSeqID) throws TTKException{
		insuranceFeedbackDAO = (InsuranceFeedbackDAOImpl)this.getEmpanelmentDAO("insurancefeedback");
		return insuranceFeedbackDAO.deleteBrokerFeedback(strFeedbackSeqID,lnguserSeqID);
	}// End of deleteCompanyFeedback(String strFeedbackSeqID,long lnguserSeqID)
	/**
	 * This method returns the AraayList of InsuranceVO which contains the details of the HeadOffice and regional office and branch offices and regionael offices
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return AraayList of InsuranceVO which contains the details of the HeadOffice and regional office and branch offices and regionael offices
	 * @exception throws TTKException
	 */
	public ArrayList<Object> getCompanyDetails(ArrayList alSearchObjects) throws TTKException
	{
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.getCompanyDetails(alSearchObjects);
	}//End of getCompanyDetails(ArrayList alSearchObjects)

	/**
	 * This method returns the Associated Product Details
	 * @param lngAssociateOfficeSequenceID the associated Office Sequence ID for which the product details are required
	 * @return InsuranceProductVO which contains the Associated Product Details
	 * @exception throws TTKException
	 */
	public InsuranceProductVO getProductDetails(Long  lngAssociateOfficeSequenceID) throws TTKException {
		insuranceProductDAO = (InsuranceProductDAOImpl)this.getEmpanelmentDAO("insuranceproduct");
		return insuranceProductDAO.getProductDetails(lngAssociateOfficeSequenceID);
	}//getProductDetails(Long  lngAssociateOfficeSequenceID)

	//added for Mail-SMS Template for Cigna
	/*
	 * Added for CR Mail-SMS Notification for Cigna
	 * 
	 */
	public NotificationInfoVO getMailNotificationList(Long insuranceSeqID) throws TTKException {
		
		groupRegistrationDAO = (GroupRegistrationDAOImpl)this.getEmpanelmentDAO("groupregistration");
		return groupRegistrationDAO.getMailNotificationList(insuranceSeqID);
		
	}
	/*
	 * Added for CR Mail-SMS Notification for Cigna
	 * 
	 */
	public int saveMailNotificationInfo(NotificationInfoVO notificationInfoVO) throws TTKException
	{
		groupRegistrationDAO = (GroupRegistrationDAOImpl)this.getEmpanelmentDAO("groupregistration");
		return groupRegistrationDAO.saveMailNotificationInfo(notificationInfoVO);
	}
	
	/*
	 * method to get the max Abbreviation code
	 */

	public String getMaxAbbrevationCode() throws TTKException
	{
		groupRegistrationDAO = (GroupRegistrationDAOImpl)this.getEmpanelmentDAO("groupregistration");
		return groupRegistrationDAO.getMaxAbbrevationCode();
	
	}
	
	public Long savePricingList(InsPricingVO insPricingVO) throws TTKException{
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.savePricingList(insPricingVO);
	}// End of addUpdateProduct(InsuranceProductVO insuranceProductVO)
	
	public InsPricingVO selectPricingList(Long lpricingSeqId) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.selectPricingList(lpricingSeqId);
	}
	
	public ArrayList getProfileIncomeList(Long lpricingSeqId) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.getProfileIncomeList(lpricingSeqId);
	}
	
	public int saveIncomeProfile(InsPricingVO insPricingVO) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.saveIncomeProfile(insPricingVO);
	}
	
	
	
	
	
	public ArrayList getProfileIncomeListvalue(Long lpricingSeqId) throws TTKException {
		insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		return insuranceDAO.getProfileIncomeListvalue(lpricingSeqId);
	}
	
	 public ArrayList getInsuranceProfileList(ArrayList alSearchObjects) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getInsuranceProfileList(alSearchObjects);
		}
	 public int savePlanDesignConfig(PolicyConfigVO policyConfigVO)throws TTKException{
		 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
	    	return insuranceDAO.savePlanDesignConfig(policyConfigVO);
		}
	 
	 public PolicyConfigVO getPlanDesignConfigData(long webBoardId)throws TTKException{
		 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
	   	 return insuranceDAO.getPlanDesignConfigData(webBoardId);
	    }
	 
	 public PolicyConfigVO saveGenerateQuote(PolicyConfigVO policyConfigVO)throws TTKException{
		 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
	   	 return insuranceDAO.saveGenerateQuote(policyConfigVO);
	    }
	 
	 public  Object[] selectGenerateQuote(long webBoardId)throws TTKException{
		 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
	   	 return insuranceDAO.selectGenerateQuote(webBoardId);
	    }
	 
	 //sw pricing started
	 public Long swSavePricingList(InsPricingVO insPricingVO) throws TTKException{
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swSavePricingList(insPricingVO);
		}// End of addUpdateProduct(InsuranceProductVO insuranceProductVO)
	 
	 public InsPricingVO swSelectPricingList(Long lpricingSeqId) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swSelectPricingList(lpricingSeqId);
		}
	 
	 public ArrayList getBenefitvalueBefore(Long lpricingSeqId) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getBenefitvalueBefore(lpricingSeqId);
		}
	 
	 public InsPricingVO getfalgPricingvalue(Long lpricingSeqId) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getfalgPricingvalue(lpricingSeqId);
		}
	 
	 
	
	 public int swSaveIncomeProfile(InsPricingVO insPricingVO) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swSaveIncomeProfile(insPricingVO);
		}
	 
	 public int swSaveIncomeNatProfile(InsPricingVO insPricingVO) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swSaveIncomeNatProfile(insPricingVO);
		}

		public ArrayList getBenefitvalueAfter(Long lpricingSeqId) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getBenefitvalueAfter(lpricingSeqId);
		}
		
		 public Object[] getBenefitvalueAfterExl(Long lpricingSeqId) throws TTKException {
				insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
				return insuranceDAO.getBenefitvalueAfterExl(lpricingSeqId);
			}
		
		public ArrayList getcpmBeforeCalcultion(InsPricingVO insPricingVO)throws TTKException{
		 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
	   	 return insuranceDAO.getcpmBeforeCalcultion(insPricingVO);
	    }
		

		public ArrayList calculatePlanDesignConfig(SwPolicyConfigVO swpolicyConfigVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.calculatePlanDesignConfig(swpolicyConfigVO);
		    }
		
		public ArrayList saveDemographicData(SwPolicyConfigVO swpolicyConfigVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.saveDemographicData(swpolicyConfigVO);
		    }
		
		public int calculateLoading(SwPricingSummaryVO swPricingSummaryVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.calculateLoading(swPricingSummaryVO);
		    }
		
		public int saveLoading(SwPricingSummaryVO swPricingSummaryVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.saveLoading(swPricingSummaryVO);
		    }
		
		public SwPolicyConfigVO getcpmAfterLoading(InsPricingVO insPricingVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.getcpmAfterLoading(insPricingVO);
		    }
		
		public SwPricingSummaryVO getcpmAfterLoadingPricing(InsPricingVO insPricingVO) throws TTKException{
		 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
	   	 return insuranceDAO.getcpmAfterLoadingPricing(insPricingVO);
	    }
		
		public ArrayList getcpmAfterCalcultion(InsPricingVO insPricingVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.getcpmAfterCalcultion(insPricingVO);
		    }
		public ArrayList getdemographicData(InsPricingVO insPricingVO, String demographicDataFlag)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.getdemographicData(insPricingVO,demographicDataFlag);
		    }
		
		
		public ArrayList getAfterLoadingData(InsPricingVO insPricingVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.getAfterLoadingData(insPricingVO);
		    }
		
		public ArrayList getBeforeLoadingData(InsPricingVO insPricingVO)throws TTKException{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
		   	 return insuranceDAO.getBeforeLoadingData(insPricingVO);
		    }
			
		public ArrayList getSwInsuranceProfileList(ArrayList alSearchObjects) throws TTKException {
				insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
				return insuranceDAO.getSwInsuranceProfileList(alSearchObjects);
			}
		
		public InsPricingVO getPolicyNumberStatus(String policyNumber) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getPolicyNumberStatus(policyNumber);
		}
		 
		public int swSaveQuotationdetails(SwFinalQuoteVO swFinalQuoteVO) throws TTKException{
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swSaveQuotationdetails(swFinalQuoteVO);
		}
		
		public SwFinalQuoteVO swSelectQuotationdetails(SwFinalQuoteVO swFinalQuoteVO) throws TTKException{
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swSelectQuotationdetails(swFinalQuoteVO);
		}

		public Long swSave_pol_copies(SwFinalQuoteVO swFinalQuoteVO,byte[] data) throws TTKException{
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swSave_pol_copies(swFinalQuoteVO,data);
		}
		 
		
		public ArrayList getPolicyCopiesList(SwFinalQuoteVO swFinalQuoteVO) throws TTKException{
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getPolicyCopiesList(swFinalQuoteVO);
		}
		 
		
	    public ArrayList getViewUploadDocs(long policycopy_seq_id)throws TTKException
	     {
	    	insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getViewUploadDocs(policycopy_seq_id);
	     }

	    
	    public String swIssuePolicy(SwFinalQuoteVO swFinalQuoteVO) throws TTKException{
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.swIssuePolicy(swFinalQuoteVO);
		}
	    
	    public ArrayList getAlkootProducts()throws TTKException
	     {
	    	insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getAlkootProducts();
	     }
	    
		 public String PricingUploadExcel(ArrayList inputData)throws TTKException
			{
			 insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
				return insuranceDAO.PricingUploadExcel(inputData);
			}
		 
		 public InsPricingVO swFetchScreen1(InsPricingVO insPricingVO) throws TTKException {
				insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
				
				return insuranceDAO.swFetchScreen1(insPricingVO);
			}

		@Override
		public InsPricingVO getPolicyStatusInfo(ArrayList dataList) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getPolicyStatusInfo(dataList);
		}

		@Override
		public ArrayList getViewFiles(ArrayList inputData) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getViewFiles(inputData);
		}

		
		@Override
		public ArrayList addUpdateReInsurance(ReInsuranceDetailVO insDetailVO) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.addUpdateReInsurance(insDetailVO);
		}

		@Override
		public ArrayList getReInsuranceCompanyList(ArrayList searchData) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getReInsuranceCompanyList(searchData);
		}

		
		@Override
		public ReInsuranceDetailVO getReInsuranceCompanyDetail (ArrayList inputData) throws TTKException {
			insuranceDAO = (InsuranceDAOImpl)this.getEmpanelmentDAO("insurance");
			return insuranceDAO.getReInsuranceCompanyDetail(inputData);
		}
		 

}// End of InsuranceManagerBean


