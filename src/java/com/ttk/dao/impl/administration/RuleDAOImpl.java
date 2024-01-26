/**
 * @ (#) RuleDAOImpl.java Jul 7, 2006
 * Project 	     : TTK HealthCare Services
 * File          : RuleDAOImpl.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Jul 7, 2006
 *
 * @author       :  RamaKrishna K M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dao.impl.administration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.administration.ClauseDiseaseVO;
import com.ttk.dto.administration.ErrorLogVO;
import com.ttk.dto.administration.LevelConfigurationVO;
import com.ttk.dto.administration.PolicyClauseVO;
import com.ttk.dto.administration.RuleVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.displayOfBenefits.BenefitsDetailsVO;
import com.ttk.dto.displayOfBenefits.DisplayBenefitsVO;
import com.ttk.dto.displayOfBenefits.ListDisplayBenefitsVO;

public class RuleDAOImpl implements BaseDAO, Serializable {

	private static Logger log = Logger.getLogger(RuleDAOImpl.class);

	private static final String strGetProductRuleList = "{CALL DEFINE_RULE_PKG.PROD_POLICY_RULES_LIST(?,?)}";
	private static final String strGetProdPolicyRule = "{CALL DEFINE_RULE_PKG.SELECT_PROD_POLICY_RULES(?,?,?,?)}";
//modification as per KOC 1099	
private static final String strSaveProdPolicyRule = "{CALL DEFINE_RULE_PKG.SAVE_PROD_POLICY_RULES(?,?,?,?,?,?,?)}";
	private static final String strGetPAClaimsRule = "{CALL DEFINE_RULE_PKG.SELECT_PA_CLAIMS_RULES(?,?,?)}";
	private static final String strProcessRule = "{CALL PROD_POLICY_RULES_PKG.PROCESS_RULE(?,?,?)}";
	private static final String strGetProdPolicyClause = "{CALL ADMINISTRATION_PKG.SELECT_PROD_POLICY_CLAUSES(?,?,?)}";
	
	//Modified according to Shortfall Cr 1179
	//private static final String strSaveProdPolicyClause = "{CALL ADMINISTRATION_PKG.SAVE_PROD_POLICY_CLAUSES(?,?,?,?,?,?,?)}";
	private static final String strSaveProdPolicyClause = "{CALL ADMINISTRATION_PKG.SAVE_PROD_POLICY_CLAUSES(?,?,?,?,?,?,?,?,?)}";
	//added for hyundai buffer
	private static final String strDeleteLevelConfiguration = "{CALL ADMINISTRATION_PKG.DEL_BUFFER_LEVEL_LIMITS(?,?,?,?,?)}";
	private static final String strGetLevelConfiguration = "{CALL ADMINISTRATION_PKG.SELECT_BUFF_LEVEL_LIMITS_LIST(?,?,?)}";
	private static final String strSaveLevelConfiguration = "{CALL ADMINISTRATION_PKG.SAVE_BUFFER_LEVEL_LIMITS(?,?,?,?,?,?,?,?,?)}";//added for hyundai buffer
	private static final String strDeleteProdPolicyClause = "{CALL ADMINISTRATION_PKG.DELETE_CLAUSES(?,?,?)}";
	private static final String strDAYCARE_PROCEDURES="SELECT A.PROC_SEQ_ID,A.PROC_CODE ||' | '||SUBSTR(A.SHORT_DESCRIPTION,0,50) AS DESCRIPTION FROM TPA_HOSP_PROCEDURE_CODE A JOIN TPA_DAY_CARE_PROCEDURE B ON (A.PROC_SEQ_ID=B.PROC_SEQ_ID) WHERE B.DAY_CARE_GROUP_ID=?";
	private static final String strGetValidationErrorList="{CALL VALIDATION_FIELD_DATA_PKG.GET_VALIDATION_ERRORS(?,?)}";
	private static final String strGetDiseaseList="{CALL ADMINISTRATION_PKG.GET_DISEASE_CODES(?,?,?,?,?,?)}";
	private static final String strAssociateDiseases="{CALL ADMINISTRATION_PKG.CLAUSE_DISEASE_BINDING(?,?,?,?,?)}";
	
	//ADDED FOR koc-1310
	private static final String strCANCERICDPROCEDURES = "SELECT A.PED_CODE_ID,A.ICD_CODE ||' | '||SUBSTR(A.PED_DESCRIPTION,0,50) AS DESCRIPTION FROM TPA_PED_CODE A JOIN TPA_CANCER_ICD_CODE B ON (A.PED_CODE_ID=B.PED_CODE_ID) WHERE B.DAY_CARE_GROUP_ID=? ORDER BY A.ICD_CODE";
	private static final String strDisplayOfBenefits="SELECT A.BENEFIT_NAME,C.SUB_BENF_NAME,C.SUB_BENF_DTL_SEQ_ID AS SUB_BENF_SEQ_ID FROM APP.TPA_MASTER_BENEFIT A JOIN APP.TPA_MASTER_SUBBENEFIT B ON (A.BENF_SEQ_ID = B.BENF_SEQ_ID) JOIN APP.TPA_SUBBENEFIT_DETAILS C ON (B.SUB_BENF_SEQ_ID = C.SUB_BENF_SEQ_ID) ORDER BY BENEFIT_NAME";
	private static final String strSaveBenefitsDetails = "{CALL DISPLAY_OF_BENEFITS.SAVE_POLICY_DATA_INFO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSaveOverallRemarks = "{CALL DISPLAY_OF_BENEFITS.overall_remarks(?,?,?)}";
	private static final String strGetBenefitsDetails="{CALL DISPLAY_OF_BENEFITS.DISPLAY_POLICY_INFO(?,?,?)}";
	
	private static final String strSaveProdPolicyRuleVals = "{CALL PROD_POLICY_RULES_EXTRACT_PKG.SAVE_PROD_POLICY_RULE_VALS(?,?)}";
	private static final String strSaveMemberPolicyRuleVals = "{CALL PROD_POLICY_RULES_EXTRACT_PKG.save_member_rule_vals(?,?)}";
	
	/**
     * This method returns the RuleVO, which contains all the Rule Data Details
     * @param lngProdPolicySeqID long value which contains Product Policy Seq ID to get the Rule Data Details
     * @return RuleVO object which contains all the Rule Data Details
     * @exception throws TTKException
     */
	public ArrayList getProductRuleList(long lngProdPolicySeqID) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		RuleVO ruleVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetProductRuleList);
			cStmtObject.setLong(1,lngProdPolicySeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					ruleVO = new RuleVO();

					if(rs.getString("PROD_POLICY_RULE_SEQ_ID") != null){
						ruleVO.setProdPolicyRuleSeqID(new Long(rs.getLong("PROD_POLICY_RULE_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_RULE_SEQ_ID") != null)

					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						ruleVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)

					if(rs.getString("VALID_FROM") != null){
						ruleVO.setFromDate(new Date(rs.getTimestamp("VALID_FROM").getTime()));
					}//end of if(rs.getString("VALID_FROM") != null)

					if(rs.getString("VALID_TO") != null){
						ruleVO.setEndDate(new Date(rs.getTimestamp("VALID_TO").getTime()));
					}//end of if(rs.getString("VALID_FROM") != null)

					alResultList.add(ruleVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getProductRuleList()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getProductRuleList()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getProductRuleList()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getProductRuleList(long lngProdPolicySeqID)
	
	/**
     This method returns the Benefits of policy that need to be displayed
     * @return ArrayList object which contains all the Benefits to be displayed
     * @exception throws TTKException
     */
	public ListDisplayBenefitsVO getDisplayBenefitsList(long prodPolicySeqId) throws TTKException {Connection conn = null;
	PreparedStatement pStmt = null;
	
	ResultSet rs = null;
	ResultSet rs1 = null;
	CallableStatement cStmtObject=null;
	ArrayList<Object> displayBenefitsList = new ArrayList<Object>();
	DisplayBenefitsVO displayBenefitsVO = null;
	BenefitsDetailsVO benefitsDetailsVO = null;
	ListDisplayBenefitsVO listDisplayBenefitsVO = new ListDisplayBenefitsVO();
	try{
		conn = ResourceManager.getConnection();
		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetBenefitsDetails);
		cStmtObject.setLong(1,prodPolicySeqId);
		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
		cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
			if(rs!=null&&rs.next()){
				
				displayBenefitsVO = new DisplayBenefitsVO();
            	displayBenefitsVO.setBenefitName(rs.getString("BENEFIT_NAME"));
            	displayBenefitsVO.setBenefitsDetailsList(new ArrayList<Object>());
				benefitsDetailsVO = new BenefitsDetailsVO();
            	benefitsDetailsVO.setId(rs.getInt("RULE_BENEFIT_SEQ_ID"));
				benefitsDetailsVO.setSubBenefitSeqId(rs.getLong("SUB_BENF_SEQ_ID"));
            	benefitsDetailsVO.setBenefitName(rs.getString("BENEFIT_NAME"));
            	benefitsDetailsVO.setSubBenefit(rs.getString("SUB_BENF_NAME"));
            	benefitsDetailsVO.setConfigration(rs.getLong("COVERAGE_PAY_VAL"));
            	benefitsDetailsVO.setCoverage(rs.getString("COVERAGE"));
            	if(rs.getString("LIMIT")!=null)
            	benefitsDetailsVO.setLimit(rs.getDouble("LIMIT")+"".trim());
            	if(rs.getString("COPAY")!=null)
            	benefitsDetailsVO.setCopay(rs.getString("COPAY"));
            	if(rs.getString("DEDUCTABLE")!=null)
            	benefitsDetailsVO.setDeductible(rs.getDouble("DEDUCTABLE")+"".trim());
            	if(rs.getString("MEM_WAITING")!=null)
            	benefitsDetailsVO.setWaitingPeriod(rs.getLong("MEM_WAITING")+"".trim());
            	if(rs.getString("SESSION_ALLOWED")!=null)
            	benefitsDetailsVO.setSessionAllowed(rs.getLong("SESSION_ALLOWED")+"".trim());
            	benefitsDetailsVO.setModeType(rs.getString("TYPE_MODE"));
            	benefitsDetailsVO.setOtherRemarks(rs.getString("OTHER_REMARKS"));
            	displayBenefitsVO.getBenefitsDetailsList().add(benefitsDetailsVO);
				
            	while(true) {
                	rs.next();
                	
                if(rs.isAfterLast()){
                	displayBenefitsList.add(displayBenefitsVO);
                	break;
                	}
				
                if(!(displayBenefitsVO.getBenefitName().equals(rs.getString("BENEFIT_NAME")))){
            		displayBenefitsList.add(displayBenefitsVO);
            		displayBenefitsVO = new DisplayBenefitsVO();
            		displayBenefitsVO.setBenefitName(rs.getString("BENEFIT_NAME"));
            		displayBenefitsVO.setBenefitsDetailsList(new ArrayList<Object>());
            	}
				
				benefitsDetailsVO = new BenefitsDetailsVO();
            	benefitsDetailsVO.setId(rs.getInt("RULE_BENEFIT_SEQ_ID"));
				benefitsDetailsVO.setSubBenefitSeqId(rs.getLong("SUB_BENF_SEQ_ID"));
            	benefitsDetailsVO.setBenefitName(rs.getString("BENEFIT_NAME"));
            	benefitsDetailsVO.setSubBenefit(rs.getString("SUB_BENF_NAME"));
            	benefitsDetailsVO.setConfigration(rs.getLong("COVERAGE_PAY_VAL"));
            	benefitsDetailsVO.setCoverage(rs.getString("COVERAGE"));
            	if(rs.getString("LIMIT")!=null)
                	benefitsDetailsVO.setLimit(rs.getDouble("LIMIT")+"".trim());
                if(rs.getString("COPAY")!=null)
                	benefitsDetailsVO.setCopay(rs.getString("COPAY"));
                if(rs.getString("DEDUCTABLE")!=null)
                	benefitsDetailsVO.setDeductible(rs.getDouble("DEDUCTABLE")+"".trim());
                if(rs.getString("MEM_WAITING")!=null)
                	benefitsDetailsVO.setWaitingPeriod(rs.getLong("MEM_WAITING")+"".trim());
                if(rs.getString("SESSION_ALLOWED")!=null)
                	benefitsDetailsVO.setSessionAllowed(rs.getLong("SESSION_ALLOWED")+"".trim());
            	benefitsDetailsVO.setModeType(rs.getString("TYPE_MODE"));
            	benefitsDetailsVO.setOtherRemarks(rs.getString("OTHER_REMARKS"));
            	displayBenefitsVO.getBenefitsDetailsList().add(benefitsDetailsVO);
            }//end of while(rs.next())
           listDisplayBenefitsVO.setDisplayBenefitsList(displayBenefitsList);
           
           rs1.next();
           listDisplayBenefitsVO.setOtherRemarks(rs1.getString("overall_remarks"));
           
		return listDisplayBenefitsVO;
	}
	else
	{
		rs.close();
		rs = null;
		pStmt = conn.prepareStatement(strDisplayOfBenefits);
        rs = pStmt.executeQuery();
        if(rs != null){
        	
            while(true) {
            	rs.next();
            	
            	if(rs.isAfterLast()){
            		displayBenefitsList.add(displayBenefitsVO);
            		break;
            	}
            	
            	if(rs.isFirst()||(!(displayBenefitsVO.getBenefitName().equals(rs.getString("BENEFIT_NAME"))))){
            		
            		if(!rs.isFirst())
            		displayBenefitsList.add(displayBenefitsVO);
            		
            		displayBenefitsVO = new DisplayBenefitsVO();
            		displayBenefitsVO.setBenefitName(rs.getString("BENEFIT_NAME"));
            		displayBenefitsVO.setBenefitsDetailsList(new ArrayList<Object>());
            	}
            	
            	benefitsDetailsVO = new BenefitsDetailsVO();
            	benefitsDetailsVO.setSubBenefitSeqId(rs.getLong("SUB_BENF_SEQ_ID"));
            	benefitsDetailsVO.setBenefitName(rs.getString("BENEFIT_NAME"));
            	benefitsDetailsVO.setSubBenefit(rs.getString("SUB_BENF_NAME"));
            	displayBenefitsVO.getBenefitsDetailsList().add(benefitsDetailsVO);            	
            }//end of while(rs.next())
            listDisplayBenefitsVO.setDisplayBenefitsList(displayBenefitsList);
            listDisplayBenefitsVO.setOtherRemarks("");
        }//end of if(rs != null)
		return listDisplayBenefitsVO;
	}
	}// end of try
	catch (SQLException sqlExp)
    {
        throw new TTKException(sqlExp, "getBenefits");
    }//end of catch (SQLException sqlExp)
    catch (Exception exp)
    {
        throw new TTKException(exp, "getBenefits");
    }//end of catch (Exception exp)
    finally
	{
		/* Nested Try Catch to ensure resource closure */ 
		try // First try closing the result set
		{
			try
			{
				if (rs1 != null) rs1.close();
				if (rs != null) rs.close();
				
			}//end of try
			catch (SQLException sqlExp)
			{
				log.error("Error while closing the Resultset in RuleDAOImpl getDisplayBenefitsList()",sqlExp);
				throw new TTKException(sqlExp, "getBenefits");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (pStmt != null) pStmt.close();
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement and Procedure in RuleDAOImpl getDisplayBenefitsList()",sqlExp);
					throw new TTKException(sqlExp, "getBenefits");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in RuleDAOImpl getDisplayBenefitsList()",sqlExp);
						throw new TTKException(sqlExp, "getBenefits");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "getBenefits");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			rs1 = null;
			rs = null;
			pStmt = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
		}//end of finally}
	}//end of getProductRuleList(long lngProdPolicySeqID)
	
	/**
	  * This method saves the Benefits of policy that need to be displayed
	  * @param Arraylist containing BenefitsDetails
	  * @return int which contains number of rows added
	  * @exception throws TTKException
	  */	
	public int saveBenefitsDetailsList(ArrayList<Object> benefitDetailsVOList,String finalRemarks, long prodPolicySeqId, long userSeqId) throws TTKException{
		
		Connection conn = null;
		Connection conn1 = null;
		CallableStatement cStmtObject=null;
		CallableStatement cStmtObject1=null;
		OracleResultSet rs = null;
		BenefitsDetailsVO benefitsDetailsVO = null;
		
			conn = ResourceManager.getConnection();
			ResourceManager.setAutoCommit(conn, false);
			try{
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveBenefitsDetails);
			if(!(benefitDetailsVOList.isEmpty())){
				
				for(Object object:benefitDetailsVOList){
					
				benefitsDetailsVO = (BenefitsDetailsVO)object;
					
			if(benefitsDetailsVO.getId()!= 0)
			cStmtObject.setLong(1,benefitsDetailsVO.getId());
			else
			cStmtObject.setLong(1,0);
			
			cStmtObject.setLong(2,prodPolicySeqId);
			
			cStmtObject.setString(3,benefitsDetailsVO.getBenefitName());
			cStmtObject.setString(4,benefitsDetailsVO.getSubBenefit());
			cStmtObject.setLong(5,benefitsDetailsVO.getSubBenefitSeqId());
			
			
			if(null != benefitsDetailsVO.getConfigration())
			cStmtObject.setLong(6,benefitsDetailsVO.getConfigration());
			else
			cStmtObject.setString(6,null);
			
			if((new Long(3)).equals(benefitsDetailsVO.getConfigration())){
			
			cStmtObject.setString(7,benefitsDetailsVO.getCoverage());
			
			if(!(benefitsDetailsVO.getLimit()==null||benefitsDetailsVO.getLimit().trim().equals("")))
			cStmtObject.setDouble(8,Double.parseDouble(benefitsDetailsVO.getLimit()));
			else
			cStmtObject.setString(8,null);
			
			if(!(benefitsDetailsVO.getCopay()==null||benefitsDetailsVO.getCopay().trim().equals("")))
			cStmtObject.setString(9,benefitsDetailsVO.getCopay());
			else
			cStmtObject.setString(9,null);
			
			if(!(benefitsDetailsVO.getDeductible()==null||benefitsDetailsVO.getDeductible().trim().equals("")))
			cStmtObject.setDouble(10,Double.parseDouble(benefitsDetailsVO.getDeductible()));
			else
			cStmtObject.setString(10,null);
			
			if(!(benefitsDetailsVO.getWaitingPeriod()==null||benefitsDetailsVO.getWaitingPeriod().trim().equals("")))
			cStmtObject.setLong(11,Long.parseLong(benefitsDetailsVO.getWaitingPeriod()));
			else
			cStmtObject.setString(11,null);
			
			if(!(benefitsDetailsVO.getSessionAllowed()==null||benefitsDetailsVO.getSessionAllowed().trim().equals("")))
			cStmtObject.setLong(12,Long.parseLong(benefitsDetailsVO.getSessionAllowed()));
			else
			cStmtObject.setString(12,null);
			
			
			if(!(benefitsDetailsVO.getModeType()==null||benefitsDetailsVO.getModeType().trim().equals("")))
			cStmtObject.setString(13,benefitsDetailsVO.getModeType());
			else
			cStmtObject.setString(13,null);	
			
			cStmtObject.setString(14,benefitsDetailsVO.getOtherRemarks());
			}else{
				cStmtObject.setString(7,null);
				cStmtObject.setString(8,null);
				cStmtObject.setString(9,null);
				cStmtObject.setString(10,null);
				cStmtObject.setString(11,null);
				cStmtObject.setString(12,null);
				cStmtObject.setString(13,null);
				cStmtObject.setString(14,null);
			}
			
			cStmtObject.setLong(15,userSeqId);
			cStmtObject.registerOutParameter(16,Types.NUMERIC);
			cStmtObject.executeUpdate();
		   }
			ResourceManager.Commit(conn);	
		}
			
			//for saving Overall Remarks
			conn1 = ResourceManager.getConnection();
			cStmtObject1 = (java.sql.CallableStatement)conn1.prepareCall(strSaveOverallRemarks);
			cStmtObject1.setLong(1,prodPolicySeqId);
			cStmtObject1.setString(2,finalRemarks);
			cStmtObject1.registerOutParameter(3,Types.NUMERIC);
			cStmtObject1.executeUpdate();
			
			int result = cStmtObject.getInt(16);
			return result;
		 }//end of try
			catch (SQLException sqlExp) 
			{
					ResourceManager.RollBack(conn);
					throw new TTKException(sqlExp, "getBenefits");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				ResourceManager.RollBack(conn);
				throw new TTKException(exp, "getBenefits");
			}//end of catch (Exception exp) 
			finally
			{
				/* Nested Try Catch to ensure resource closure */ 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in RuleDAOImpl saveBenefitsDetailsList()",sqlExp);
						throw new TTKException(sqlExp, "getBenefits");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject1 != null) cStmtObject1.close();
							if (cStmtObject != null) cStmtObject.close();
							
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl saveBenefitsDetailsList()",sqlExp);
							throw new TTKException(sqlExp, "getBenefits");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn1 != null) conn1.close();
								if(conn != null) conn.close();
								
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in RuleDAOImpl saveBenefitsDetailsList()",sqlExp);
								throw new TTKException(sqlExp, "getBenefits");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "getBenefits");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject1 = null;
					cStmtObject = null;
					conn1 = null;
					conn = null;
					
				}//end of finally
			}//end of finally	
 	}

	/**
     * This method returns the RuleVO, which contains all the Rule Data details
     * @param lngSeqID long value which contains ProductPolicySeqID/RuleSeqID/Policy Seq ID to get the Rule Data Details
     * @param strFlag String object which contains Flag - P / R / I or C
     * @return RuleVO object which contains all the Rule Data details
     * @exception throws TTKException
     */
	public RuleVO getProdPolicyRule(long lngSeqID,String strFlag) throws TTKException {
		Document doc = null;
        XMLType xml = null;
        Connection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        OracleResultSet rs1 = null;
        RuleVO ruleVO = null;
        try{
        	conn = ResourceManager.getConnection();
			stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strGetProdPolicyRule);
			stmt.setLong(1,lngSeqID);
			stmt.setString(2,strFlag);
			stmt.registerOutParameter(3,OracleTypes.CURSOR);
			stmt.registerOutParameter(4,OracleTypes.CURSOR);
			stmt.execute();
			rs = (OracleResultSet)stmt.getObject(3);
			rs1 = (OracleResultSet)stmt.getObject(4);
			if(rs != null){
				while(rs.next()){
					ruleVO = new RuleVO();
					if(!"MEM".equals(strFlag)){
					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						ruleVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
					}
					if(rs.getString("PROD_POLICY_RULE_SEQ_ID") != null){
						ruleVO.setProdPolicyRuleSeqID(new Long(rs.getLong("PROD_POLICY_RULE_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_RULE_SEQ_ID") != null)

					if(rs.getOPAQUE("PROD_POLICY_RULE") != null) {
                        xml = XMLType.createXML(rs.getOPAQUE("PROD_POLICY_RULE"));
                        DOMReader domReader = new DOMReader();
                        doc = xml != null ? domReader.read(xml.getDOM()):null;
                        ruleVO.setRuleDocument(doc);
				    }// End of if(rs.getOPAQUE("PROD_POLICY_RULE")

					if(rs.getString("VALID_FROM") != null){
						ruleVO.setFromDate(new Date(rs.getTimestamp("VALID_FROM").getTime()));
					}//end of if(rs.getString("VALID_FROM") != null)

					if(rs.getString("VALID_TO") != null){
						ruleVO.setEndDate(new Date(rs.getTimestamp("VALID_TO").getTime()));
					}//end of if(rs.getString("VALID_FROM") != null)
				}//end of while(rs.next())
			}//end of if(rs != null)
			return ruleVO;
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
					if (rs1 != null) rs1.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getProdPolicyRule()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (stmt != null) stmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getProdPolicyRule()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getProdPolicyRule()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				rs1 = null;
				stmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getProdPolicyRule(long lngSeqID,String strFlag)

	/**
     * This method saves the Rule Data information
     * @param ruleVO RuleVO contains Rule Data information
     * @param strFlag String object which contains Flag - P / R / I or C
     * @return long value contains Seq ID
     * @exception throws TTKException
     */
	public long saveProdPolicyRule(RuleVO ruleVO,String strFlag) throws TTKException {
      
XMLType ruleXML = null;
    	Connection conn = null;
         /** TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
	        DateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:S");
			df.setTimeZone(tz); 
    	 * 
    	 */
    	TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
        DateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:S");
		df.setTimeZone(tz); 
		
		OracleCallableStatement stmt = null;
		long lngSeqID = 0;
		try{
			conn = ResourceManager.getConnection();
log.info("In RuleDAOImpl Before saveProdPolicyRule PROC   :"+ df.format(Calendar.getInstance(tz).getTime()));
    		stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strSaveProdPolicyRule);
   log.info("In RuleDAOImpl After saveProdPolicyRule PROC   :"+ df.format(Calendar.getInstance(tz).getTime())); 		
if(ruleVO.getSeqID() != null){
    			stmt.setLong(1,ruleVO.getSeqID());//PROD_POLICY_RULE_SEQ_ID/PROD_POLICY_SEQ_ID/POLICY_SEQ_ID
    		}//end of if(ruleVO.getSeqID() != null)
    		else{
    			stmt.setLong(1,0);
    		}//end of else

    		stmt.setString(2,strFlag);

    		if(ruleVO.getRuleDocument() != null){
    			ruleXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), ruleVO.getRuleDocument().asXML());
			}//end of if(ruleVO.getRuleDocument() != null)
    		stmt.setObject(3,ruleXML);

    		if(ruleVO.getFromDate() != null){
    			stmt.setTimestamp(4,new Timestamp(ruleVO.getFromDate().getTime()));
    		}//end of if(ruleVO.getFromDate() != null)
    		else{
    			stmt.setTimestamp(4,null);
    		}//end of else

    		stmt.setLong(5,ruleVO.getUpdatedBy());

    		if(ruleVO.getOverrideCompletedYN()!= null && ruleVO.getOverrideCompletedYN().equalsIgnoreCase("Y") ){
    			
    			stmt.setString(6,ruleVO.getOverrideCompletedYN());//OverrideCompletedYN Status Y OR N
    		}//end of if(ruleVO.getSeqID() != null)
    		else{
    			
    			stmt.setString(6,ruleVO.getOverrideCompletedYN());
    		}
    		
    		if("MEM".equals(strFlag))
    		{
	    		if(ruleVO.getMemberSeqID() != null){
	    			stmt.setLong(7,ruleVO.getMemberSeqID());
	    		}
	    		else{
	    			stmt.setLong(7,0);
	    		}
    		}
    		else
    		{
    			stmt.setLong(7,0);
    		}

    		stmt.registerOutParameter(1,Types.BIGINT);
    		stmt.execute();
    		lngSeqID = stmt.getLong(1);
    	}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (stmt != null) stmt.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl saveProdPolicyRule()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl saveProdPolicyRule()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		stmt = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return lngSeqID;
	}//end of saveProdPolicyRule(RuleVO ruleVO,String strFlag)

	/**
	 * This method returns the RuleVO, which contains all the Rule Data details
	 * @param lngSeqID long value which contains PAT_GEN_DETAIL_SEQ_ID/CLAIM_SEQ_ID to get the Rule Data Details
	 * @param strFlag String object which contains Flag - PR /CR
	 * @return RuleVO object which contains all the Rule Data details
	 * @exception throws TTKException
	 */
	public RuleVO getPAClaimsRule(long lngSeqID,String strFlag) throws TTKException {
		Document doc = null;
        XMLType xml = null;
        Connection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        RuleVO ruleVO = null;
TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
	        DateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:S");
			df.setTimeZone(tz);
        try{
        	conn = ResourceManager.getConnection();
log.info("Before executing RuleDAOImpl getPAClaimsRule Proc :"+ df.format(Calendar.getInstance(tz).getTime()));
			stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strGetPAClaimsRule);
log.info("After executing RuleDAOImpl getPAClaimsRule Proc :"+ df.format(Calendar.getInstance(tz).getTime()));			
stmt.setLong(1,lngSeqID);
			stmt.setString(2,strFlag);
			stmt.registerOutParameter(3,OracleTypes.CURSOR);
			stmt.execute();
			rs = (OracleResultSet)stmt.getObject(3);
			if(rs != null){
				while(rs.next()){
					ruleVO = new RuleVO();

					if(rs.getString("RULE_DATA_SEQ_ID") != null){
						ruleVO.setRuleDataSeqID(new Long(rs.getLong("RULE_DATA_SEQ_ID")));
					}//end of if(rs.getString("RULE_DATA_SEQ_ID") != null)
					if(rs.getString("RULE_EXECUTION_FLAG")!=null)
                    {
                        ruleVO.setRuleExecutionFlag(new Integer(rs.getInt("RULE_EXECUTION_FLAG")));
                    }//end of if(rs.getString("RULE_EXECUTION_FLAG")!=null)
					if(rs.getOPAQUE("RULE") != null) {
                        xml = XMLType.createXML(rs.getOPAQUE("RULE"));
                        DOMReader domReader = new DOMReader();
                        doc = xml != null ? domReader.read(xml.getDOM()):null;
                        ruleVO.setRuleDocument(doc);
				    }// End of if(rs.getOPAQUE("RULE_DATA")
				}//end of while(rs.next())
			}//end of if(rs != null)
	log.info("Before return ruleVo :"+ df.format(Calendar.getInstance(tz).getTime()));		
return ruleVO;
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getPAClaimsRule()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (stmt != null) stmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getPAClaimsRule()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getPAClaimsRule()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				stmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getPAClaimsRule(long lngSeqID,String strFlag)

	/**
     * This method returns the RuleVO, which contains all the Rule Data details
     * @param strFlag String object which contains Flag - P / C
     * @param lngSeqID long value which contains PAT_GEN_DETAIL_SEQ_ID/CLAIM_SEq_ID to get the Rule Data Details
     * @return Document object which contains Rule Data XML
     * @exception throws TTKException
     */
	public Document processRule(String strFlag,long lngSeqID) throws TTKException {
		Document doc = null;
        XMLType xml = null;
        Connection conn = null;
        OracleCallableStatement stmt = null;
  /*
        import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;*/
		TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
	        DateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:S");
			df.setTimeZone(tz); 
        try{
        	conn = ResourceManager.getConnection();
	log.info("Before executing RuleDAOImpl processRule Proc :"+ df.format(Calendar.getInstance(tz).getTime()));
    		stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strProcessRule);
    		log.info("After executing RuleDAOImpl processRule Proc :"+ df.format(Calendar.getInstance(tz).getTime()));	
stmt.setString(1,strFlag);
    		stmt.setLong(2,lngSeqID);
    		stmt.registerOutParameter(3,OracleTypes.OPAQUE,"SYS.XMLTYPE");
    		stmt.execute();
    		xml = XMLType.createXML(stmt.getOPAQUE(3));
    		DOMReader domReader = new DOMReader();
            doc = xml != null ? domReader.read(xml.getDOM()):null;
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (stmt != null) stmt.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl processRule()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl processRule()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		stmt = null;
        		conn = null;
        	}//end of finally
		}//end of finally
log.info("Befor returning Document :"+ df.format(Calendar.getInstance(tz).getTime()));
		return doc;
	}//end of processRule(String strFlag,long lngSeqID)
	
	/**
     * This method returns the ArrayList, which contains all the Product/Policy Clauses details
     * @param lngProdPolicySeqID long value which contains Seq ID to get the Product/Policy Clauses Details
     * @param strIdentifier String value which contains identifier - Clause/Coverage for fetching the information
     * @return ArrayList object which contains all the Product/Policy Clauses details
     * @exception throws TTKException
     */
	public ArrayList getProdPolicyClause(long lngProdPolicySeqID,String strIdentifier) throws TTKException {
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	PolicyClauseVO policyClauseVO = null;
    	Collection<Object> alResultList = new ArrayList<Object>();
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetProdPolicyClause);
			cStmtObject.setLong(1,lngProdPolicySeqID);
			cStmtObject.setString(2,strIdentifier);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			if(rs != null){
				while(rs.next()){
					policyClauseVO = new PolicyClauseVO();
					
					if(rs.getString("CLAUSE_SEQ_ID") != null){
						policyClauseVO.setClauseSeqID(new Long(rs.getLong("CLAUSE_SEQ_ID")));
					}//end of if(rs.getString("CLAUSE_SEQ_ID") != null)
					
					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						policyClauseVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
					
					policyClauseVO.setClauseNbr(TTKCommon.checkNull(rs.getString("CLAUSE_NUMBER")));
					policyClauseVO.setClauseDesc(TTKCommon.checkNull(rs.getString("CLAUSE_DESCRIPTION")));
					policyClauseVO.setClauseYN(TTKCommon.checkNull(rs.getString("CLAUSE_YN")));
					policyClauseVO.setActiveYN(TTKCommon.checkNull(rs.getString("ACTIVE_YN")));
				    if(rs.getString("CLAUSE_YN").equalsIgnoreCase("Y")){
						if(TTKCommon.checkNull(rs.getString("CLAUSE_TYPE")).equalsIgnoreCase("") || rs.getString("CLAUSE_TYPE")!=null )					{
							if(rs.getString("CLAUSE_TYPE").equalsIgnoreCase("SHORTFALL")){
								policyClauseVO.setClauseForDesc(TTKCommon.checkNull(rs.getString("CLAUSE_TYPE")));
								policyClauseVO.setClauseFor("SRT");
								policyClauseVO.setClauseSubType(TTKCommon.checkNull(rs.getString("CLAUSE_SUB_TYPE")));
							}
							else{
								policyClauseVO.setClauseFor("OTH");
								policyClauseVO.setClauseForDesc(TTKCommon.checkNull(rs.getString("CLAUSE_TYPE")));
							}
						}

					}
					//Added as per Shortfall 1179 CR 
					alResultList.add(policyClauseVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getProdPolicyClause()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getProdPolicyClause()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getProdPolicyClause()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return (ArrayList)alResultList;
	}//end of getProdPolicyClause(long lngProdPolicySeqID)
	
	/**
     * This method saves the Product/Policy Clauses information
     * @param ruleVO RuleVO contains Product/Policy Clauses information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
	public int saveProdPolicyClause(PolicyClauseVO policyClauseVO) throws TTKException {
		int iResult = 0;
		Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveProdPolicyClause);
            
            if(policyClauseVO.getClauseSeqID() != null){
            	cStmtObject.setLong(1,policyClauseVO.getClauseSeqID());
            }//end of if(policyClauseVO.getClauseSeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else
            
            cStmtObject.setLong(2,policyClauseVO.getProdPolicySeqID());
            cStmtObject.setString(3,policyClauseVO.getClauseYN());
            cStmtObject.setString(4,policyClauseVO.getClauseNbr());
            cStmtObject.setString(5,policyClauseVO.getClauseDesc());
            cStmtObject.setLong(6,policyClauseVO.getUpdatedBy());
             //CHANGES AA PER KOC1179 Shortfall Cr
			if(policyClauseVO.getClauseYN().equalsIgnoreCase("Y"))
			{
				cStmtObject.setString(7,policyClauseVO.getClauseFor());
				if(policyClauseVO.getClauseFor().equalsIgnoreCase("SRT")){
					cStmtObject.setString(8,policyClauseVO.getClauseSubType());
				}
				else{
					cStmtObject.setString(8,null);
				}
			}
			else{
				
				cStmtObject.setString(7,null);
				cStmtObject.setString(8,null);
			}
			cStmtObject.registerOutParameter(9,Types.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(9);
			//CHANGES AA PER KOC1179 Shortfall Cr
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "prodpolicyrule");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "prodpolicyrule");
        }//end of catch (Exception exp)
        finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl saveProdPolicyClause()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl saveProdPolicyClause()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of saveProdPolicyClause(PolicyClauseVO policyClauseVO)
	
	/**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deleteProdPolicyClause(ArrayList alDeleteList) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			 conn = ResourceManager.getConnection();
	         cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteProdPolicyClause);
	         cStmtObject.setString(1,(String)alDeleteList.get(0));//Concatenated String of Clause Seq ID's
	         cStmtObject.setLong(2,(Long)alDeleteList.get(1));//UPDATED_BY
	         cStmtObject.registerOutParameter(3, Types.INTEGER);//ROWS_PROCESSED
			 cStmtObject.execute();
			 iResult = cStmtObject.getInt(3);//ROWS_PROCESSED
		}//end of try
		catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "prodpolicyrule");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "prodpolicyrule");
        }//end of catch (Exception exp)
        finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl deleteProdPolicyClause()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl deleteProdPolicyClause()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteProdPolicyClause(ArrayList alDeleteList)
	
	/**
     * This method returns the ArrayList, which contains the Daycare procedures belonging to given group
     * @param strDaycareGroupId String Daycare group id
     * @return ArrayList object which contains daycare procedures belonging to this group.
     * @exception throws TTKException
     */
	public ArrayList getDayCareProcedureList(String strDaycareGroupId) throws TTKException {
		Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
    	ArrayList<Object> alDaycareProcedure = null;
    	CacheObject cacheObject=null;
    	try
    	{
    		conn = ResourceManager.getConnection();
            pStmt = conn.prepareStatement(strDAYCARE_PROCEDURES);
                pStmt.setString(1,strDaycareGroupId);
            
            rs = pStmt.executeQuery();
            if(rs != null){
                while (rs.next()) {
                	cacheObject = new CacheObject();
                    if(alDaycareProcedure == null)
                    {
                        alDaycareProcedure = new ArrayList<Object>();
                    }//end of if(alDaycareProcedure == null)
                    cacheObject.setCacheId(TTKCommon.checkNull(rs.getString(1)));
                    cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString(2)));
                    alDaycareProcedure.add(cacheObject);
    			}//end of while(rs.next())
            }//end of if(rs != null)
    		return alDaycareProcedure;
    	}// end of try
    	catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "prodpolicyrule");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "prodpolicyrule");
        }//end of catch (Exception exp)
        finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getDayCareProcedureList()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getDayCareProcedureList()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getDayCareProcedureList()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getDayCareProcedureList
	
	//added for KOC-1310
	/**
     * This method returns the ArrayList, which contains all the Cancer ICD CODES
     * @param strCancerGroupId String cancer ICD group id
     * @return ArrayList object which contains Cancer ICD codes belonging to this group.
     * @exception throws TTKException
     */
	public ArrayList getCancerICDList(String strCancerGroupId)throws TTKException {
		
		Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
    	ArrayList<Object> alCancerICDCode = null;
    	CacheObject cacheObject=null;
    	try
    	{
    		conn = ResourceManager.getConnection();
            pStmt = conn.prepareStatement(strCANCERICDPROCEDURES);
            pStmt.setString(1,strCancerGroupId);
            
            rs = pStmt.executeQuery();
            if(rs != null){
                while (rs.next()) {
                	cacheObject = new CacheObject();
                    if(alCancerICDCode == null)
                    {
                    	alCancerICDCode = new ArrayList<Object>();
                    }//end of if(alDaycareProcedure == null)
                    cacheObject.setCacheId(TTKCommon.checkNull(rs.getString(1)));
                    cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString(2)));
                    alCancerICDCode.add(cacheObject);
    			}//end of while(rs.next())
            }//end of if(rs != null)
    		return alCancerICDCode;
    	}// end of try
    	catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "prodpolicyrule");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "prodpolicyrule");
        }//end of catch (Exception exp)
        finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getCancerICDList()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getCancerICDList()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getCancerICDList()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}
	//ended
	
	
	/**
	 * This method returns List of business errors occured during the rule engine execution in Enrollment and softcopy upload
	 * @param lngErrorCode long value which contains Product Policy Seq ID to get the Rule Data Details
	 * @return alResultList ArrayList list of business errors
	 * @exception throws TTKException
	 */
	public ArrayList getValidationErrorList(long lngErrorCode) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ErrorLogVO errorLogVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetValidationErrorList);
			cStmtObject.setLong(1,lngErrorCode);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					errorLogVO = new ErrorLogVO();
					
					if(rs.getString("ERROR_CODE") != null){
						errorLogVO.setErrorCode(new Long(rs.getLong("ERROR_CODE")));
					}//end of if(rs.getString("ERROR_CODE") != null)
					
					if(rs.getString("POLICY_SEQ_ID") != null){
						errorLogVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)
					
					if(rs.getString("POLICY_GROUP_SEQ_ID") != null){
						errorLogVO.setPolicyGroupSeqID(new Long(rs.getLong("POLICY_GROUP_SEQ_ID")));
					}//end of if(rs.getString("POLICY_GROUP_SEQ_ID") != null)
					
					if(rs.getString("MEMBER_SEQ_ID") != null){
						errorLogVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
					}//end of if(rs.getString("MEMBER_SEQ_ID") != null)
					
					errorLogVO.setConditionID(TTKCommon.checkNull(rs.getString("CONDITION_ID")));
					errorLogVO.setOperator(TTKCommon.checkNull(rs.getString("OP")));
					errorLogVO.setOperatorType(TTKCommon.checkNull(rs.getString("OPTYPE")));
					errorLogVO.setMethodName(TTKCommon.checkNull(rs.getString("METHOD")));
					errorLogVO.setConfiguredValue(TTKCommon.checkNull(rs.getString("CONFIGURED_VALUE")));
					errorLogVO.setComputedValue(TTKCommon.checkNull(rs.getString("COMPUTED_VALUE")));
					errorLogVO.setUnit(TTKCommon.checkNull(rs.getString("UNIT")));
					
					// Added for constructing proper error message by Unni on 03-04-2008
					errorLogVO.setErrorMessage(rs.getString("MESSAGE"));
					// End of Adding
					
					//Fields related to softcopy upload
//					if(rs.getString("TPA_OFFICE_SEQ_ID") != null){
//						errorLogVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
//					}//end of if(rs.getString("ERROR_CODE") != null)
//					
//					if(rs.getString("INS_SEQ_ID") != null){
//						errorLogVO.setInsSeqID(new Long(rs.getLong("INS_SEQ_ID")));
//					}//end of if(rs.getString("ERROR_CODE") != null)
//					
//					errorLogVO.setAbbrevationCode(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
//					errorLogVO.setInsCompCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
//					errorLogVO.setBatchNbr(TTKCommon.checkNull(rs.getString("BATCH_NUMBER")));
//					errorLogVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
//					errorLogVO.setEndorsementNbr(TTKCommon.checkNull(rs.getString("CUST_ENDORSEMENT_NUMBER")));
//					errorLogVO.setEmployeeNbr(TTKCommon.checkNull(rs.getString("EMPLOYEE_NO")));
//					errorLogVO.setPolicyHolder(TTKCommon.checkNull(rs.getString("POLICY_HOLDER")));
//					errorLogVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
//					errorLogVO.setEnrollmentNbr(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_NUMBER")));
					alResultList.add(errorLogVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getValidationErrorList()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getValidationErrorList()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getValidationErrorList()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getValidationErrorList(long lngProdPolicySeqID)
	
	/**
     * This method returns the ArrayList, which contains all the Disease Details
     * @param alSearchCriteria arraylist which contains  all the clause related details
     * @return ArrayList object which contains all the Disease Details
     * @exception throws TTKException
     */
	public ArrayList<ClauseDiseaseVO> getDiseaseList(ArrayList<Object> alSearchCriteria) throws TTKException
	{
		log.debug ("Inside getdisease list method");
		ArrayList<ClauseDiseaseVO> alDiseaseList = new ArrayList<ClauseDiseaseVO>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ClauseDiseaseVO  clauseDiseaseVO = null;
		try
		{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDiseaseList);
			cStmtObject.setLong(1,(Long)alSearchCriteria.get(0));
			cStmtObject.setLong(2,(Long)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5, (String)alSearchCriteria.get(4));
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(6);
			if(rs != null)
			{
				while(rs.next())
				{
					clauseDiseaseVO = new ClauseDiseaseVO();
					if(rs.getString("INS_SPECIFIC_CODE_SEQ_ID") != null){
						clauseDiseaseVO.setInsSpecificSeqID(new Long(rs.getString("INS_SPECIFIC_CODE_SEQ_ID")));
					}//end of if(rs.getString("INS_SPECIFIC_CODE_SEQ_ID") != null)
					clauseDiseaseVO.setDiseaseCode(TTKCommon.checkNull(rs.getString("CODE")));
					clauseDiseaseVO.setDiseaseDesc(TTKCommon.checkNull(rs.getString("CODE_DESCRIPTION")));
					alDiseaseList.add(clauseDiseaseVO);
					
				}//end of while(rs.next())
			}//end of if(rs != null)
		return (ArrayList<ClauseDiseaseVO>) alDiseaseList;
	    }//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getDiseaseList()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getDiseaseList()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getDiseaseList()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getDiseaseList(ArrayList alSearchCriteria)
	
	/**
     * This method is used to Associate/Disassociate a disease to a clause
     * @param alDiseaseList ArrayList which contains disease/clause related information
     * @return int the value which returns greater than zero for successful execution of the task
     * @exception throws TTKException
     */
	public int associateDiseases(ArrayList<Object> alDiseaseList) throws TTKException
	{
		log.debug("Inside associateDiseases method in RuleDAOImpl ");
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAssociateDiseases);
			
			if(alDiseaseList != null){
				cStmtObject.setLong(1,(Long)alDiseaseList.get(0));
				cStmtObject.setString(2,(String)alDiseaseList.get(1));
				cStmtObject.setString(3,(String)alDiseaseList.get(2));
				cStmtObject.setLong(4,(Long)alDiseaseList.get(3));
				cStmtObject.registerOutParameter(5, Types.INTEGER);//ROWS_PROCESSED
				cStmtObject.execute();
				iResult = cStmtObject.getInt(5);//ROWS_PROCESSED
			}//end of if(alDiseaseList != null)
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl associateDiseases()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl associateDiseases()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of associateDiseases(ArrayList alDiseaseList)
//added for hyundai buffer
	
	
	/**
     * This method saves the Product/Policy Clauses information
     * @param ruleVO RuleVO contains Product/Policy Clauses information
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
	public ArrayList savePolicyLevelConfiguration(LevelConfigurationVO levelConfigurationVO) throws TTKException {
		int iResult = 0;
		Connection conn = null;
        CallableStatement cStmtObject=null;
        ArrayList allOutParam=new ArrayList();
        try{
        	conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveLevelConfiguration);
            
            if(levelConfigurationVO.getBufferSeqId() != null){
            	cStmtObject.setLong(1,levelConfigurationVO.getBufferSeqId());
            }//end of if(policyClauseVO.getClauseSeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else
            log.info("trimmmmm"+levelConfigurationVO.getBufferType().trim());
              cStmtObject.setLong(2,levelConfigurationVO.getProdPolicySeqID());
              cStmtObject.setString(3,levelConfigurationVO.getBufferType().trim());
              cStmtObject.setString(4,levelConfigurationVO.getLevelType());
              cStmtObject.setBigDecimal(5,levelConfigurationVO.getLevelsLimit());
               cStmtObject.setString(6,levelConfigurationVO.getRemarks());
              cStmtObject.setLong(7,levelConfigurationVO.getUpdatedBy());
             //CHANGES AA PER KOC1179 Shortfall Cr
              cStmtObject.registerOutParameter(8,Types.VARCHAR);
			cStmtObject.registerOutParameter(9,Types.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(9);
			
			
			 String alertMsg = (cStmtObject.getString(8)!=null)?cStmtObject.getString(8):"";
	            if(!alertMsg.equalsIgnoreCase(""))
	            {
	            	allOutParam.add(0);
	            	allOutParam.add(alertMsg);
	            }
	            else
	            {
	            	allOutParam.add(1);
	            	allOutParam.add(alertMsg);
	            }
	            allOutParam.add(iResult);
			//CHANGES AA PER KOC1179 Shortfall Cr
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "prodpolicyrule");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "prodpolicyrule");
        }//end of catch (Exception exp)
        finally
		{
        	/* Nested Try Catch to ensure resource closure */
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl saveProdPolicyClause()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl saveProdPolicyClause()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return allOutParam;
	}//end of saveProdPolicyClause(PolicyClauseVO policyClauseVO)
	/**
	 * This method deletes the requested information from the database
	 * @param alDeleteList the arraylist of details of which has to be deleted
	 * @return int the value which returns greater than zero for succcesssful execution of the task
	 * @exception throws TTKException
	 */
	public int deletePolicyLevelConfiguration(ArrayList alDeleteList) throws TTKException {
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			 conn = ResourceManager.getConnection();
	         cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteLevelConfiguration);
	         cStmtObject.setString(1,(String)alDeleteList.get(0));//Concatenated String of Clause Seq ID's
	         cStmtObject.setLong(2,(Long)alDeleteList.get(1));//UPDATED_BY
	         cStmtObject.setString(3,(String)alDeleteList.get(2));//UPDATED_BY
	         cStmtObject.setLong(4,(Long)alDeleteList.get(3));//UPDATED_BY
	         cStmtObject.registerOutParameter(5, Types.INTEGER);//ROWS_PROCESSED
			 cStmtObject.execute();
			 iResult = cStmtObject.getInt(5);//ROWS_PROCESSED
		}//end of try
		catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "prodpolicyrule");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "prodpolicyrule");
        }//end of catch (Exception exp)
        finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl deleteProdPolicyClause()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl deleteProdPolicyClause()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteProdPolicyClause(ArrayList alDeleteList)
	
	
	
	/**
     * This method returns the ArrayList, which contains all the Product/Policy Clauses details
     * @param lngProdPolicySeqID long value which contains Seq ID to get the Product/Policy Clauses Details
     * @param strIdentifier String value which contains identifier - Clause/Coverage for fetching the information
     * @return ArrayList object which contains all the Product/Policy Clauses details
     * @exception throws TTKException
     */
	public ArrayList getPolicyLevelConfiguration(long lngPolicySeqID,long lngUserSeqId) throws TTKException {
		Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	LevelConfigurationVO levelConfigurationVO = null;
    	Collection<Object> alResultList = new ArrayList<Object>();
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetLevelConfiguration);
			cStmtObject.setLong(1,lngPolicySeqID);
			//cStmtObject.setString(2,strIdentifier);
			cStmtObject.setLong(2,lngUserSeqId);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			if(rs != null){
				while(rs.next()){
					levelConfigurationVO = new LevelConfigurationVO();
					
					if(rs.getString("BUFF_LEVEL_SEQ_ID") != null){
						levelConfigurationVO.setBufferSeqId(new Long(rs.getLong("BUFF_LEVEL_SEQ_ID")));
					}//end of if(rs.getString("CLAUSE_SEQ_ID") != null)
					
					/*if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						levelConfigurationVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
*/					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						levelConfigurationVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
                    
                   levelConfigurationVO.setBufferType(TTKCommon.checkNull(rs.getString("GENERAL_TYPE_ID").trim()));//code
                   levelConfigurationVO.setGeneralType(TTKCommon.checkNull(rs.getString("BUFFER_TYPE").trim()));//description
                    levelConfigurationVO.setLevelType(TTKCommon.checkNull(rs.getString("LEVEL_TYPE").trim()));
                    levelConfigurationVO.setLevelDesc(TTKCommon.checkNull(rs.getString("LEVEL_DESC").trim()));
					levelConfigurationVO.setLevelsLimit((BigDecimal) TTKCommon.checkNull(rs.getBigDecimal("LEVEL_LIMIT")));
					levelConfigurationVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
				
					//Added as per Shortfall 1179 CR 
					alResultList.add(levelConfigurationVO);
					log.info("alResultList"+alResultList.toString());
				}//end of while(rs.next())
			}//end of if(rs != null)
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getProdPolicyClause()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getProdPolicyClause()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getProdPolicyClause()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return (ArrayList)alResultList;
	}//end of getProdPolicyClause(long lngProdPolicySeqID)
	
	
	public long saveProdPolicyRuleVals(Long ProdPolicyRuleSeqID, String RuleFlag) throws TTKException {
		long iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			 conn = ResourceManager.getConnection();
			 if(RuleFlag == "memberRule")
				 cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveMemberPolicyRuleVals); 
			 else
				 cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveProdPolicyRuleVals);
	         if(ProdPolicyRuleSeqID !=null)
	         {
	        	 cStmtObject.setLong(1,ProdPolicyRuleSeqID);
	         }
	         else
	        	 cStmtObject.setLong(1,0);
	         
	         cStmtObject.registerOutParameter(2, Types.BIGINT);
			 cStmtObject.execute();
			 iResult = cStmtObject.getLong(2);//ROWS_PROCESSED
		}//end of try
		catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "prodpolicyrule");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "prodpolicyrule");
        }//end of catch (Exception exp)
        finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
        		{
        			if (cStmtObject != null) cStmtObject.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in RuleDAOImpl saveProdPolicyRuleVals()",sqlExp);
        			throw new TTKException(sqlExp, "prodpolicyrule");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in RuleDAOImpl saveProdPolicyRuleVals()",sqlExp);
        				throw new TTKException(sqlExp, "prodpolicyrule");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "prodpolicyrule");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of deleteProdPolicyClause(ArrayList alDeleteList)
	
	
	
	
	public ArrayList getPolicyRuleHistory(long lngSeqID,String strFlag) throws TTKException {
		Document doc = null;
        XMLType xml = null;
        Connection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        OracleResultSet rs1 = null;
        RuleVO ruleVO = null;
        Collection<Object> alResultList = new ArrayList<Object>();
        try{
        	conn = ResourceManager.getConnection();
			stmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strGetProdPolicyRule);
			stmt.setLong(1,lngSeqID);
			stmt.setString(2,strFlag);
			stmt.registerOutParameter(3,OracleTypes.CURSOR);
			stmt.registerOutParameter(4,OracleTypes.CURSOR);
			stmt.execute();
			rs1 = (OracleResultSet)stmt.getObject(3);
			rs = (OracleResultSet)stmt.getObject(4);
			if(rs != null){
				System.out.println("if(rs != null){");
				while(rs.next()){
					System.out.println("while(rs.next()){");
					ruleVO = new RuleVO();
					
					if(rs.getOPAQUE("v_old_rule_xml") != null) {
                        xml = XMLType.createXML(rs.getOPAQUE("v_old_rule_xml"));
                        DOMReader domReader = new DOMReader();
                        doc = xml != null ? domReader.read(xml.getDOM()):null;
                        ruleVO.setRuleDocument(doc);
				    }// End of if(rs.getOPAQUE("PROD_POLICY_RULE")

					ruleVO.setModifiedDate(rs.getString("modified_date"));
					ruleVO.setModifiedBy(rs.getString("modified_by"));
					
					
					alResultList.add(ruleVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList) alResultList;
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "prodpolicyrule");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "prodpolicyrule");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
					if (rs1 != null) rs1.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in RuleDAOImpl getProdPolicyRule()",sqlExp);
					throw new TTKException(sqlExp, "prodpolicyrule");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (stmt != null) stmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in RuleDAOImpl getProdPolicyRule()",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyrule");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in RuleDAOImpl getProdPolicyRule()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyrule");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "prodpolicyrule");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				rs1 = null;
				stmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}
	
	
	
	
	
}//end of RuleDAOImpl
