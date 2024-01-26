/**
 * @ (#) RuleManagerBean.java Jul 7, 2006
 * Project 	     : TTK HealthCare Services
 * File          : RuleManagerBean.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Jul 7, 2006
 *
 * @author       :  RamaKrishna K M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.business.administration;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import org.dom4j.Document;

import com.ttk.business.preauth.ruleengine.RuleEngine;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.impl.administration.AdministrationDAOFactory;
import com.ttk.dao.impl.administration.RuleDAOImpl;
import com.ttk.dto.administration.ClauseDiseaseVO;
import com.ttk.dto.administration.LevelConfigurationVO;
import com.ttk.dto.administration.PolicyClauseVO;
import com.ttk.dto.administration.RuleVO;
import com.ttk.dto.displayOfBenefits.BenefitsDetailsVO;
import com.ttk.dto.displayOfBenefits.ListDisplayBenefitsVO;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class RuleManagerBean implements RuleManager{

	private AdministrationDAOFactory administrationDAOFactory = null;
	private RuleDAOImpl ruleDAO = null;

	/**
     * This method returns the instance of the data access object to initiate the required tasks
     * @param strIdentifier String object identifier for the required data access object
     * @return BaseDAO object
     * @exception throws TTKException
     */
    private BaseDAO getRuleDAO(String strIdentifier) throws TTKException
    {
        try
        {
            if (administrationDAOFactory == null)
            	administrationDAOFactory = new AdministrationDAOFactory();
            if(strIdentifier!=null)
            {
                return administrationDAOFactory.getDAO(strIdentifier);
            }//end of if(strIdentifier!=null)
            else
                return null;
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "error."+strIdentifier+".dao");
        }//end of catch(Exception exp)
    }//End of getRuleDAO(String strIdentifier)

	/**
     * This method returns the RuleVO, which contains all the Rule Data Details
     * @param lngProdPolicySeqID long value which contains Product Policy Seq ID to get the Rule Data Details
     * @return RuleVO object which contains all the Rule Data Details
     * @exception throws TTKException
     */
	public ArrayList getProductRuleList(long lngProdPolicySeqID) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getProductRuleList(lngProdPolicySeqID);
	}//end of getProductRuleList(long lngProdPolicySeqID)
	/**
     * This method returns the Benefits of policy that need to be displayed
     * @return ArrayList object which contains all the Benefits to be displayed
     * @exception throws TTKException
     */
	public ListDisplayBenefitsVO getDisplayBenefitsList(long prodPolicySeqId) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getDisplayBenefitsList(prodPolicySeqId);
	}//end of getProductRuleList(long lngProdPolicySeqID)
	
	/**
    * This method saves the Benefits of policy that need to be displayed
    * @param Arraylist containing BenefitsDetails
    * @return int which contains number of rows added
    * @exception throws TTKException
    */	
	public int saveBenefitsDetailsList(ArrayList<Object> benefitsDetailsVO, String finalRemarks, long policySeqId, long userSeqId) throws TTKException{
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.saveBenefitsDetailsList(benefitsDetailsVO,finalRemarks,policySeqId,userSeqId);
	}//end of getProductRuleList(long lngProdPolicySeqID)


	/**
     * This method returns the RuleVO, which contains all the Rule Data details
     * @param lngSeqID long value which contains ProductPolicySeqID/RuleSeqID/Policy Seq ID to get the Rule Data Details
     * @param strFlag String object which contains Flag - P / R / I or C
     * @return RuleVO object which contains all the Rule Data details
     * @exception throws TTKException
     */
	public RuleVO getProdPolicyRule(long lngSeqID,String strFlag) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getProdPolicyRule(lngSeqID,strFlag);
	}//end of getProdPolicyRule(long lngSeqID,String strFlag)

	/**
     * This method saves the Rule Data information
     * @param ruleVO RuleVO contains Rule Data information
     * @param strFlag String object which contains Flag - P / R / I or C
     * @return long value contains Seq ID
     * @exception throws TTKException
     */
	public long saveProdPolicyRule(RuleVO ruleVO,String strFlag) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.saveProdPolicyRule(ruleVO,strFlag);
	}//end of saveProdPolicyRule(RuleVO ruleVO,String strFlag)

	  public Document initiateCheck(Document doc) throws TTKException
	   {
		   RuleEngine ruleEngine = new RuleEngine();
		   return ruleEngine.applyClause(doc);
	   }

	/**
	 * This method returns the RuleVO, which contains all the Rule Data details
	 * @param lngSeqID long value which contains PAT_GEN_DETAIL_SEQ_ID/CLAIM_SEq_ID to get the Rule Data Details
	 * @param strFlag String object which contains Flag - PI / PC
	 * @return RuleVO object which contains all the Rule Data details
	 * @exception throws TTKException
	 */
	public RuleVO getPAClaimsRule(long lngSeqID,String strFlag) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getPAClaimsRule(lngSeqID,strFlag);
	}//end of getPAClaimsRule(long lngSeqID,String strFlag)

	/**
     * This method returns the RuleVO, which contains all the Rule Data details
     * @param strFlag String object which contains Flag - P / C
     * @param lngSeqID long value which contains PAT_GEN_DETAIL_SEQ_ID/CLAIM_SEq_ID to get the Rule Data Details
     * @return Document object which contains Rule Data XML
     * @exception throws TTKException
     */
	public Document processRule(String strFlag,long lngSeqID) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		Document doc=ruleDAO.processRule(strFlag,lngSeqID);
		Document processedDoc=null;
		if(doc !=null)
		{
			RuleEngine ruleEngine = new RuleEngine();
			processedDoc=ruleEngine.applyClause(doc);
		}
		return processedDoc;
	}//end of processRule(String strFlag,long lngSeqID)
	
	/**
     * This method returns the ArrayList, which contains all the Product/Policy Clauses details
     * @param lngProdPolicySeqID long value which contains Seq ID to get the Product/Policy Clauses Details
     * @param strIdentifier String value which contains identifier - Clause/Coverage for fetching the information
     * @return ArrayList object which contains all the Product/Policy Clauses details
     * @exception throws TTKException
     */
	public ArrayList getProdPolicyClause(long lngProdPolicySeqID,String strIdentifier) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getProdPolicyClause(lngProdPolicySeqID,strIdentifier);
	}//end of getProdPolicyClause(long lngProdPolicySeqID,String strIdentifier)
	
	/**
     * This method saves the Product/Policy Clauses information
     * @param ruleVO RuleVO contains Product/Policy Clauses information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
	public int saveProdPolicyClause(PolicyClauseVO policyClauseVO) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.saveProdPolicyClause(policyClauseVO);
	}//end of saveProdPolicyClause(PolicyClauseVO policyClauseVO)
	
	/**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteProdPolicyClause(ArrayList alDeleteList) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.deleteProdPolicyClause(alDeleteList);
	}//end of deleteProdPolicyClause(ArrayList alDeleteList)
	
	
	//added for hyundai buffer
	
	/**
     * This method returns the ArrayList, which contains all the Product/Policy Clauses details
     * @param lngProdPolicySeqID long value which contains Seq ID to get the Product/Policy Clauses Details
     * @param strIdentifier String value which contains identifier - Clause/Coverage for fetching the information
     * @return ArrayList object which contains all the Product/Policy Clauses details
     * @exception throws TTKException
     */
	public ArrayList getPolicyLevelConfiguration(long lngPolicySeqID,long lngUserSeqId) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getPolicyLevelConfiguration(lngPolicySeqID,lngUserSeqId);
	}//end of getProdPolicyClause(long lngProdPolicySeqID,String strIdentifier)
	
	/**
     * This method saves the Product/Policy Clauses information
     * @param ruleVO RuleVO contains Product/Policy Clauses information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
	public ArrayList savePolicyLevelConfiguration(LevelConfigurationVO levelConfigurationVO) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.savePolicyLevelConfiguration(levelConfigurationVO);
	}//end of saveProdPolicyClause(PolicyClauseVO policyClauseVO)
	/**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deletePolicyLevelConfiguration(ArrayList alDeleteList) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.deletePolicyLevelConfiguration(alDeleteList);
	}//end of deleteProdPolicyClause(ArrayList alDeleteList)
	
	/**
     * This method returns the ArrayList, which contains the Daycare procedures belonging to given group
     * @param strDaycareGroupId String Daycare group id
     * @return ArrayList object which contains daycare procedures belonging to this group.
     * @exception throws TTKException
     */
	public ArrayList getDayCareProcedureList(String strDaycareGroupId) throws TTKException{
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getDayCareProcedureList(strDaycareGroupId);
	}//end of getDayCareProcedureList(String strDaycareGroupId)
	
	//added for KOC-1310
	/**
     * This method returns the ArrayList, which contains all the Cancer ICD CODES
     * @param strCancerGroupId String cancer ICD group id
     * @return ArrayList object which contains Cancer ICD codes belonging to this group.
     * @exception throws TTKException
     */
	public ArrayList getCancerICDList(String strCancerGroupId) throws TTKException{
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getCancerICDList(strCancerGroupId);		
	}//ended
	
	/**
     * This method returns the ArrayList, which contains the Validation Errors
     * @param lngErrorCode Long Error Code
     * @return ArrayList object which contains the Validation Errors.
     * @exception throws TTKException
     */
	public ArrayList getValidationErrorList(Long lngErrorCode) throws TTKException{
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getValidationErrorList(lngErrorCode);
	}//end of getValidationErrorList(Long lngErrorCode)
	
	/**
     * This method returns the ArrayList, which contains all the Disease Details
     * @param alSearchCriteria arraylist which contains  all the clause related details
     * @return ArrayList object which contains all the Disease Details
     * @exception throws TTKException
     */
	public ArrayList<ClauseDiseaseVO> getDiseaseList(ArrayList<Object> alSearchCriteria) throws TTKException{
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getDiseaseList(alSearchCriteria);
	}//end of getDiseaseList(ArrayList alDiseaseList)
	
	/**
     * This method is used to Associate/Disassociate a disease to a clause
     * @param alDiseaseList ArrayList which contains disease/clause related information
     * @return int the value which returns greater than zero for successful execution of the task
     * @exception throws TTKException
     */
	public int associateDiseases(ArrayList<Object> alDiseaseList) throws TTKException{
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.associateDiseases(alDiseaseList);
	}//end of associateDiseases(ArrayList alDiseaseList)
	
	
	public long saveProdPolicyRuleVals(Long ProdPolicyRuleSeqID,String RuleFlag) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.saveProdPolicyRuleVals(ProdPolicyRuleSeqID,RuleFlag);
	}//end of saveProdPolicyRule(RuleVO ruleVO,String strFlag)
	
	public ArrayList getPolicyRuleHistory(long lngSeqID, String strFlag) throws TTKException {
		ruleDAO = (RuleDAOImpl)this.getRuleDAO("prodpolicyrule");
		return ruleDAO.getPolicyRuleHistory(lngSeqID,strFlag);
	}
	
}//end of RuleManagerBean
