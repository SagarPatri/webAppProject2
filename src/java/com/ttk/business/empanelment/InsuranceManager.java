/**
 * @ (#) Any.java Oct 19, 2005
 * Project      : ttkHealthCareServices
 * File         : InsuranceManager.java
 * Author       : Ravindra
 * Company      : SpanSystem Corp:
 * Date Created : Oct 19, 2005
 *
 * @author       :Ravindra
 
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */
package com.ttk.business.empanelment;

import java.io.FileReader;
import java.util.ArrayList;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.empanelment.InsuranceDAOImpl;
import com.ttk.dto.empanelment.BrokerFeedbackVO;
import com.ttk.dto.empanelment.InsuranceDetailVO;
import com.ttk.dto.empanelment.InsuranceFeedbackVO;
import com.ttk.dto.empanelment.InsuranceProductVO;
import com.ttk.dto.empanelment.NotificationInfoVO;
import com.ttk.dto.empanelment.ReInsuranceDetailVO;
import com.ttk.dto.insurancepricing.InsPricingVO;
import com.ttk.dto.insurancepricing.PolicyConfigVO;
import com.ttk.dto.insurancepricing.SwFinalQuoteVO;
import com.ttk.dto.insurancepricing.SwPolicyConfigVO;
import com.ttk.dto.insurancepricing.SwPricingSummaryVO;

import javax.ejb.*;

import org.apache.struts.upload.FormFile;

@Local

public interface InsuranceManager {
	
	/**
	 * This method returns the ArrayList, which contains the list of InsuranceCompany which are populated from the database
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return ArrayList of InsuranceCompany object's which contains the InsuranceCompany details
	 * @exception throws TTKException
	 */
	public ArrayList getInsuranceCompanyList(ArrayList alSearchObjects) throws TTKException;
	
	/**
	 * This method returns the InsuranceDetailVO, which contains the all the insurancecompany details
	 * @param lInsuranceParentId is Insurance Company Parent Sequence ID 
	 * @param lInsuranceCompanyID is of long type which is unique for each insurance company
	 * @return InsuranceDetailVO object which contains all the insurancecompany details
	 * @exception throws TTKException
	 */
	public InsuranceDetailVO getInsuranceCompanyDetail(Long lInsuranceParentId,Long lInsuranceCompanyID) throws TTKException;
	
	/**
	 * This method adds or updates the insurance company  details   
	 * The method also calls other methods on DAO to insert/update the insurance company  details to the database 
	 * @param insuranceDetailVO InsuranceDetailVO object which contains the insurance comapny  details to be added/updated
	 * @return long value, Insurance Seq Id
	 * @exception throws TTKException
	 */
	public long addUpdateInsuranceCompany(InsuranceDetailVO insuranceDetailVO)  throws TTKException;
	/**
	 * This method delete's the insurance Company  records from the database.  
	 * @param strInsSeqID String object which contains the insurance company  sequence id's to be deleted
	 * @return int  value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int deleteInsuranceCompany(String strInsSeqID)   throws TTKException;
	
	/**
	 * This method returns the ArrayList, which contains the InsuranceCompanyProductVO's which are populated from the database
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return ArrayList of InsuranceCompanyProductVO object's which contains the InsuranceCompany details
	 * @exception throws TTKException
	 */
	public ArrayList getProductList(ArrayList alSearchObjects)  throws TTKException;
	
	/**
	 * This method adds or updates the insurance Products   
	 * The method also calls other methods on DAO to insert/update the insurance details to the database 
	 * @param insuranceProductVO InsuranceProductVO object which contains the insuranceCompanyProduct details to be added/updated
	 * @return long value, Associate Office Sequence Id
	 * @exception throws TTKException
	 */
	public long addUpdateProduct(InsuranceProductVO insuranceProductVO) throws TTKException;
	
	/**
	 * This method delete's the Products  records from the database.  
	 * @param alProductList ArrayList object which contains the Product sequence id's to be deleted
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int  deleteProduct(ArrayList alProductList)  throws TTKException;
	
	/**
	 * This method returns the ArrayList, which contains the InsuranceFeedbackVO's which are populated from the database
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return ArrayList of InsuranceFeedbackVO object's which contains the InsuranceCompany Feedback details
	 * @exception throws TTKException
	 */
	public ArrayList getCompanyFeedbackList(ArrayList alSearchObjects) throws TTKException;
	
	//added for kocb broker login
	public ArrayList getBrokerFeedbackList(ArrayList alSearchObjects) throws TTKException;
	
	
	/**
	 * This method returns the InsuranceFeedbackVO, which contains the InsuranceCompany Feedback details which are populated from the database
	 * @param lngInsFeedbackSeqID long value which contains Feedback Seq ID
	 * @return InsuranceFeedbackVO which contains the InsuranceCompany Feedback details
	 * @exception throws TTKException
	 */
	public InsuranceFeedbackVO getCompanyFeedback(long lngInsFeedbackSeqID) throws TTKException;
	
	public InsuranceFeedbackVO getBrokerFeedback(long lngInsFeedbackSeqID) throws TTKException;
	
	/**
	 * This method adds or updates the insurance Feedback   
	 * The method also calls other methods on DAO to insert/update the insurance details to the database 
	 * @param insuranceFeedbackVO InsuranceFeedbackVO object which contains the insuranceFeedback details to be added/updated
	 * @return long value, Insurance Feedback Sequence Id
	 * @exception throws TTKException
	 */
	public long addUpdateCompanyFeedback(InsuranceFeedbackVO insuranceFeedbackVO) throws TTKException ;
	//added for broker login kocb
	//public long addUpdateBrokerFeedback(BrokerFeedbackVO brokerFeedbackVO) throws TTKException ;
	public long addUpdateBrokerFeedback(InsuranceFeedbackVO insuranceFeedbackVO) throws TTKException ;
	
	/**
	 * This method delete's the Insurance Feedback   records from the database.  
	 * @param strFeedbackSeqID String object which contains the Feedback sequence id's to be deleted
     * @param lnguserSeqID for which user has loggedin
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int deleteCompanyFeedback(String strFeedbackSeqID,long lnguserSeqID) throws TTKException;
/**
	 * This method delete's the Broker Feedback   records from the database.  
	 * @param strFeedbackSeqID String object which contains the Feedback sequence id's to be deleted
     * @param lnguserSeqID for which user has loggedin
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int deleteBrokerFeedback(String strFeedbackSeqID,long lnguserSeqID) throws TTKException;
	/**
	 * This method returns the ArrayList object of ProductVO's, which contains all the details about the products Associated to parent Insurance Company
	 * @param lInsSeqId long Insurance Sequence Id
	 * @return ArrayList object which contains all the details about the Insurance Company Associated products
	 * @exception throws TTKException 
	 */
	public ArrayList getProductCode(long lInsSeqId) throws TTKException;
	
	/**
	 * This method returns the AraayList of InsuranceVO which contains the details of the HeadOffice and regional office and branch offices and regionael offices
	 * @param alSearchObjects ArrayList object which contains the search criteria
	 * @return AraayList of InsuranceVO which contains the details of the HeadOffice and regional office and branch offices and regionael offices
	 * @exception throws TTKException
	 */
	public ArrayList<Object> getCompanyDetails(ArrayList alSearchObjects) throws TTKException;
	
	/**
	 * This method returns the Associated Product Details
	 * @param lngAssociateOfficeSequenceID the associated Office Sequence ID for which the product details are required
	 * @return InsuranceProductVO which contains the Associated Product Details
	 * @exception throws TTKException
	 */
	public InsuranceProductVO getProductDetails(Long  lngAssociateOfficeSequenceID) throws TTKException;
	//added for Mail-SMS Template for Cigna
	/*
	 * Added for CR KOC Mail-SMS Notification.
	 * 
	 */
	public NotificationInfoVO getMailNotificationList(Long insuranceSeqID) throws TTKException;
	/*
	 * Added for CR KOC Mail-SMS Notification.
	 */
	public int saveMailNotificationInfo(NotificationInfoVO notificationInfoVO) throws TTKException;

	/*
	 * method to get the max Abbreviation code
	 */

	public String getMaxAbbrevationCode()throws TTKException;
	
	public Long savePricingList(InsPricingVO insPricingVO) throws TTKException;
	
	public InsPricingVO selectPricingList(Long lpricingSeqId) throws TTKException;
	
	public ArrayList getProfileIncomeList(Long lpricingSeqId) throws TTKException;	
	
	public int saveIncomeProfile(InsPricingVO insPricingVO) throws TTKException;
	public ArrayList getProfileIncomeListvalue(Long lpricingSeqId) throws TTKException;	
	
	public ArrayList getInsuranceProfileList(ArrayList alSearchObjects) throws TTKException;
	
	 public int savePlanDesignConfig(PolicyConfigVO policyConfigVO) throws TTKException;
	 public PolicyConfigVO getPlanDesignConfigData(long webBoardId)throws TTKException;
	 
	 public PolicyConfigVO saveGenerateQuote(PolicyConfigVO policyConfigVO)throws TTKException;
	 
	 public  Object[] selectGenerateQuote(long webBoardId)throws TTKException;
	 
	 //sw pricing started

	 
	public int swSaveIncomeProfile(InsPricingVO insPricingVO) throws TTKException;

	public Long swSavePricingList(InsPricingVO insPricingVO)throws TTKException;

	public InsPricingVO swSelectPricingList(Long lpricingSeqId) throws TTKException;
	
	public ArrayList getBenefitvalueBefore(Long lpricingSeqId) throws TTKException;	

	public ArrayList getBenefitvalueAfter(Long lpricingSeqId) throws TTKException;

	public ArrayList getcpmBeforeCalcultion(InsPricingVO insPricingVO) throws TTKException;

	public ArrayList calculatePlanDesignConfig(SwPolicyConfigVO swpolicyConfigVO)throws TTKException;

	public ArrayList getcpmAfterCalcultion(InsPricingVO insPricingVO) throws TTKException;

	public ArrayList getSwInsuranceProfileList(ArrayList<Object> searchData) throws TTKException;

	public InsPricingVO getfalgPricingvalue(Long group_seq_id) throws TTKException;

	public int swSaveIncomeNatProfile(InsPricingVO insPricingVO) throws TTKException;

	public ArrayList getAfterLoadingData(InsPricingVO insPricingVO) throws TTKException;

	public ArrayList getBeforeLoadingData(InsPricingVO insPricingVO) throws TTKException;

	public int calculateLoading(SwPricingSummaryVO swPricingSummaryVO)  throws TTKException;

	public int saveLoading(SwPricingSummaryVO swPricingSummaryVO) throws TTKException;

	public SwPolicyConfigVO getcpmAfterLoading(InsPricingVO insPricingVO) throws TTKException;

	public SwPricingSummaryVO getcpmAfterLoadingPricing(InsPricingVO insPricingVO) throws TTKException;
	
	public InsPricingVO getPolicyNumberStatus(String policyNumber) throws TTKException;

	public ArrayList getdemographicData(InsPricingVO insPricingVO, String demographicDataFlag) throws TTKException;

	public ArrayList saveDemographicData(SwPolicyConfigVO swpolicyConfigVO) throws TTKException;

	public int swSaveQuotationdetails(SwFinalQuoteVO swFinalQuoteVO) throws TTKException;

	public SwFinalQuoteVO swSelectQuotationdetails(SwFinalQuoteVO swFinalQuoteVO)  throws TTKException;

	public Long swSave_pol_copies(SwFinalQuoteVO swFinalQuoteVO, byte[] data) throws TTKException;

	public ArrayList getPolicyCopiesList(SwFinalQuoteVO swFinalQuoteVO) throws TTKException;

	public ArrayList getViewUploadDocs(long policycopy_seq_id) throws TTKException;

	public String swIssuePolicy(SwFinalQuoteVO swFinalQuoteVO) throws TTKException;

	public ArrayList getAlkootProducts()throws TTKException;

	public String PricingUploadExcel(ArrayList inputData) throws TTKException;

	public Object[] getBenefitvalueAfterExl(Long lpricingSeqId) throws TTKException;

	public InsPricingVO swFetchScreen1(InsPricingVO insPricingVO) throws TTKException;

	public InsPricingVO getPolicyStatusInfo(ArrayList dataList)  throws TTKException;

	public ArrayList getViewFiles(ArrayList inputData)throws TTKException;

	public ArrayList addUpdateReInsurance(ReInsuranceDetailVO insDetailVO) throws TTKException;

	public ArrayList getReInsuranceCompanyList(ArrayList searchData) throws TTKException;

	public ReInsuranceDetailVO getReInsuranceCompanyDetail(ArrayList inputData) throws TTKException;

	
}// End of InsuranceManager




