/**
 * @ (#)  ProductDaoImpl.java Nov 5, 2005
 * Project      : TTKPROJECT
 * File         : ProductDaoImpl.java
 * Author       : Suresh.M
 * Company      : Span Systems Corporation
 * Date Created : Nov 5, 2005
 *
 * @author       :  Suresh.M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.dao.impl.administration;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.administration.ProdPolicyLimitVO;
import com.ttk.dto.administration.EscalationLimitVO;
import com.ttk.dto.administration.ProductVO;
import com.ttk.dto.administration.RuleSynchronizationVO;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.security.GroupVO;

public class ProductDAOImpl implements BaseDAO,Serializable{

	private static Logger log = Logger.getLogger(ProductDAOImpl.class);

	private static final String strProductList="SELECT * FROM(SELECT TPA_INS_PRODUCT.PRODUCT_SEQ_ID, PROD_POLICY_SEQ_ID,PROD_GENERAL_TYPE_ID,PRODUCT_NAME,TPA_INS_PRODUCT.DESCRIPTION PROD_DESC,INS_COMP_NAME,TPA_GENERAL_CODE.DESCRIPTION STATUSDESC,TPA_INS_PRODUCT.INS_SEQ_ID,PROD_STATUS_GENERAL_TYPE_ID, TPA_INS_PRODUCT.INS_PRODUCT_CODE, ABBREVATION_CODE, DENSE_RANK() OVER (ORDER BY #, ROWNUM)Q FROM TPA_INS_PROD_POLICY,TPA_INS_PRODUCT, TPA_INS_INFO, TPA_GENERAL_CODE WHERE TPA_INS_PROD_POLICY.PRODUCT_SEQ_ID = TPA_INS_PRODUCT.PRODUCT_SEQ_ID AND TPA_INS_PRODUCT.INS_SEQ_ID = TPA_INS_INFO.INS_SEQ_ID AND TPA_INS_PRODUCT.PROD_STATUS_GENERAL_TYPE_ID = TPA_GENERAL_CODE.GENERAL_TYPE_ID";
	private static final String strSynchPolicyList = "{CALL PRODUCT_ADMIN_PKG.GET_SYNCH_POLICY_LIST(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSynchronizeRule = "{CALL PRODUCT_ADMIN_PKG.SYNCHRONIZE_RULE(?,?,?,?)}";//denial process one parameter added
    private static final String strAddUpdateProduct="{CALL PRODUCT_ADMIN_PKG.PR_PRODUCT_SAVE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//KOC 1286 added one parameter // added as per koc 1274A
    private static final String strDeleteProduct="{CALL PRODUCT_ADMIN_PKG.PR_PRODUCTS_DELETE(?,?,?)}";
    private static final String strGetProduct = "{CALL PRODUCT_ADMIN_PKG.GET_PRODUCT_DETAILS(?,?)}";//"SELECT B.PRODUCT_SEQ_ID, A.PROD_POLICY_SEQ_ID, B.PROD_GENERAL_TYPE_ID, B.PRODUCT_NAME, B.DESCRIPTION PROD_DESC, C.INS_COMP_NAME,C.ABBREVATION_CODE,D.DESCRIPTION STATUSDESC,B.INS_SEQ_ID, B.PROD_STATUS_GENERAL_TYPE_ID, B.CARD_CLEARANCE_DAYS, B.CLAIM_CLEARANCE_DAYS, B.PA_CLEARANCE_HOURS , A.TEMPLATE_ID, A.DV_REQD_GENERAL_TYPE_ID,B.INS_PRODUCT_CODE,A.TENURE,B.AUTH_LETTER_GENERAL_TYPE_ID,A.STOP_PREAUTH_YN,A.STOP_CLAIM_YN,B.SURGERY_GEN_TYPE_ID, nvl(b.CBH_SUMINS_YN,'N') as CBH_SUMINS_YN, nvl(b.CONV_SUMINS_YN,'N') as CONV_SUMINS_YN,A.OPD_BENEFITS_YN,PAT_ALLOWED_YN,CLM_ALLOWED_YN,PAT_TO_MAIL_ID,PAT_CC_MAIL_ID,CLM_TO_MAIL_ID,CLM_CC_MAIL_ID,NVL(B.Critical_Sumins_Yn,'N') as Critical_Sumins_Yn,NVL(B.SURVIVAL_PRD,'N') AS SURVIVAL_PERIOD_YN,b.product_cat_type_id,b.auth_product_code  FROM TPA_INS_PROD_POLICY A JOIN TPA_INS_PRODUCT B ON (A.PRODUCT_SEQ_ID = B.PRODUCT_SEQ_ID) JOIN TPA_INS_INFO C ON (B.INS_SEQ_ID = C.INS_SEQ_ID) JOIN TPA_GENERAL_CODE D ON (B.PROD_STATUS_GENERAL_TYPE_ID = D.GENERAL_TYPE_ID) WHERE A.PROD_POLICY_SEQ_ID=?"; //added B.Critical_Sumins_Yn this for KOC-1273 6 param for bajaj koc 1274A
    //added B.Critical_Sumins_Yn this for KOC-1273
	private static final String strSaveUserGroup = "{CALL CONTACT_PKG.SAVE_PRODUCT_GROUPS(?,?,?,?)}";
	private static final String strGetUserGroup ="SELECT * FROM ( WITH INS AS ( SELECT C.INS_COMP_NAME FROM TPA_INS_PROD_POLICY A JOIN TPA_INS_PRODUCT B ON (A.PRODUCT_SEQ_ID = B.PRODUCT_SEQ_ID AND A.PROD_POLICY_SEQ_ID = ?) JOIN TPA_INS_INFO C ON ( B.INS_SEQ_ID = C.INS_SEQ_ID )) SELECT A.GROUP_SEQ_ID , A.GROUP_NAME, C.GROUP_BRANCH_SEQ_ID, D.TPA_OFFICE_SEQ_ID, CASE WHEN F.TPA_OFFICE_GENERAL_TYPE_ID = 'THO' THEN 'TZO' ELSE D.TPA_OFFICE_GENERAL_TYPE_ID END TPA_OFFICE_GENERAL_TYPE_ID , D.OFFICE_NAME , E.INS_COMP_NAME , D.TPA_PARENT_SEQ_ID FROM TPA_GROUPS A JOIN TPA_INS_PROD_POLICY B ON (A.PROD_POLICY_SEQ_ID = B.PROD_POLICY_SEQ_ID AND B.PROD_POLICY_SEQ_ID = ?) JOIN TPA_GROUP_BRANCH C ON ( A.GROUP_SEQ_ID = C.GROUP_SEQ_ID ) RIGHT OUTER JOIN TPA_OFFICE_INFO D ON ( C.TPA_OFFICE_SEQ_ID = D.TPA_OFFICE_SEQ_ID ) CROSS JOIN INS E LEFT OUTER JOIN TPA_OFFICE_INFO F ON ( D.TPA_PARENT_SEQ_ID = F.TPA_OFFICE_SEQ_ID )) START WITH TPA_OFFICE_GENERAL_TYPE_ID = 'THO' CONNECT BY PRIOR tpa_office_seq_id = tpa_parent_seq_id";
	private static final String strCopyProduct = "{CALL PRODUCT_ADMIN_PKG.PR_COPY_PRODUCT_RULES(?,?,?,?)}";
	private static final String strAssInsProduct = "{CALL PRODUCT_ADMIN_PKG.ASSOC_INS_PRODUCT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSaveProdPolicyLimit = "{CALL PRODUCT_ADMIN_PKG.SAVE_PROD_POLICY_LIMITS(?,?,?,?)}";
	private static final String strGetProdPolicyLimit = "{CALL PRODUCT_ADMIN_PKG.SELECT_PROD_POLICY_LIMITS(?,?)}";
	//added for bajaj
	private static final String strSaveProdPolicyEscalateLimit = "{CALL CLAIMS_APPROVAL_PKG.SAVE_PROD_POL_ESCALATION(?,?,?,?)}";
	private static final String strGetProdPolicyEscalateLimit = "{CALL CLAIMS_APPROVAL_PKG.SELECT_PROD_POL_ESCALATION(?,?,?)}";

	
	private static final int PROD_POLICY_SEQ_ID = 1;
	private static final int INS_SEQ_ID = 2;
	private static final int PRODUCT_TYPE_ID = 3;
	private static final int PRODUCT_NAME = 4;
	private static final int DESCRIPTION = 5;
	private static final int PROD_STATUS_GENERAL_TYPE_ID = 6;
	private static final int USER_SEQ_ID = 7;
	private static final int INSCOMP_ABBR_CODE = 8;
	private static final int CARD_CLEARANCE_DAYS = 9;
	private static final int CLAIM_CLEARANCE_DAYS = 10;
	private static final int PA_CLEARANCE_HOURS = 11;
	private static final int TEMPLATE_ID = 12;
	private static final int DISCHARGE_VOUCH_MANDATORY_YN = 13;
	private static final int INS_PRODUCT_CODE = 14;
	private static final int TENURE = 15;
	private static final int AUTH_LETTER_GENERAL_TYPE_ID = 16;
	private static final int STOP_PREAUTH_YN = 17;
	private static final int STOP_CLAIM_YN = 18;
	private static final int SURGERY_GNRL_TYPE_ID =19;
	private static final int OPD_BENEFITS_YN =20; //KOC 1286 FOR OPD
    private static final int CBH_SUMINS_YN = 21; // KOC 1270 for hospital cash benefit
    private static final int CONV_SUMINS_YN = 22; // KOC 1270 for hospital cash benefit
    //added for KOC-1273
    private static final int CRITICAL_ILLNESS_BENEFIT = 23;
    private static final int SURVIVAL_PERIOD = 24;
    private static final int Pat_Enable_YN = 25; // koc 1274A
    private static final int Clm_Enable_YN = 26; // koc 1274A
    private static final int Pat_Mail_To = 27; //   koc 1274A
    private static final int Pat_Mail_CC = 28; //   koc 1274A
    private static final int Clm_Mail_To = 29; //   koc 1274A
    private static final int Clm_Mail_CC = 30; //   koc 1274A
    private static final int PRODUCT_NETWORK_TYPE = 31;
    private static final int HEALTH_AUTHORITY = 32;
    private static final int CO_INS=33;
    private static final int DEDUCTIBLE=34;
    private static final int CLASS_ROOM_TYPE=35;                   
    private static final int PLAN_TYPE=36;                    
    private static final int MATERNITY_YN=37;                               
    private static final int MATERNITY_COPAY=38;      
    private static final int OPTICAL_YN=39;                     
    private static final int OPTICAL_COPAY=40;                             
    private static final int DENTAL_YN=41;                       
    private static final int DENTAL_COPAY=42;                              
    private static final int ELIGIBILITY=43;                                         
    private static final int IP_OP_SERVICES=44;                                             
    private static final int PHARMACEUTICALS=45;
    private static final int ROWS_PROCESSED = 46;
    private static final int V_AUTH_PRODUCT_CODE = 47;
	
	/**
     * This method returns the ArrayList, which contains the ProductVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of ProductVO'S object's which contains the details of the insurance products
     * @exception throws TTKException
     */
    public ArrayList getProductList(ArrayList alSearchObjects) throws TTKException {
    	Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        StringBuffer sbfDynamicQuery = new StringBuffer();
        String strStaticQuery = strProductList;
        ProductVO productVO = null;
        Collection<Object> resultList = new ArrayList<Object>();
        if(alSearchObjects != null && alSearchObjects.size() > 0)
        {
            for(int i=0; i < alSearchObjects.size()-4; i++)
            {
                if(!((SearchCriteria)alSearchObjects.get(i)).getValue().equals(""))
                {
                    if (((SearchCriteria)alSearchObjects.get(i)).getOperator().equalsIgnoreCase("equals"))
                    {
                        sbfDynamicQuery.append(" AND "+((SearchCriteria)alSearchObjects.get(i)).getName()+" = '"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"' ");
                    }//end of if (((SearchCriteria)alSearchObjects.get(i)).getOperator().equalsIgnoreCase("equals"))
                    else
                    {
                        sbfDynamicQuery.append(" AND UPPER("+ ((SearchCriteria)alSearchObjects.get(i)).getName()+") LIKE UPPER('"+((SearchCriteria)alSearchObjects.get(i)).getValue()+"%')");
                    }//end of else
                }//end of if(!((SearchCriteria)alSearchCriteria.get(i)).getValue().equals(""))
            }//end of for()
        }//end of if(alSearchCriteria != null && alSearchCriteria.size() > 0)
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        sbfDynamicQuery = sbfDynamicQuery .append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        
        try{
            conn = ResourceManager.getConnection();
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            if(rs != null){
                while (rs.next()) {
                    productVO = new ProductVO();
                    if (rs.getString("PRODUCT_SEQ_ID")!=null){
                        productVO.setProdSeqId(new Long(rs.getLong("PRODUCT_SEQ_ID")));
                    }//end of if (rs.getString("PRODUCT_SEQ_ID")!=null)
                    if (rs.getString("PROD_POLICY_SEQ_ID")!=null){
                        productVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
                    }//end of if (rs.getString("PROD_POLICY_SEQ_ID")!=null)

                    productVO.setProdTypeId(TTKCommon.checkNull(rs.getString("PROD_GENERAL_TYPE_ID")));
                    productVO.setProductName(TTKCommon.checkNull(rs.getString("PRODUCT_NAME")));
                    productVO.setProdDesc(TTKCommon.checkNull(rs.getString("PROD_DESC")));
                    productVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
                    productVO.setCompanyAbbreviation(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
                    productVO.setProductCode(TTKCommon.checkNull(rs.getString("INS_PRODUCT_CODE")));

                    if (rs.getString("INS_SEQ_ID") != null){
                        productVO.setInsSeqId(new Long(rs.getLong("INS_SEQ_ID")));
                    }//end of if (rs.getString("INS_SEQ_ID") != null)

                    productVO.setStatus(TTKCommon.checkNull(rs.getString("STATUSDESC")));
                    productVO.setProdStatTypeId(TTKCommon.checkNull(rs.getString("PROD_STATUS_GENERAL_TYPE_ID")));
                    resultList.add(productVO);
                }//end of while
            }//end of if(rs != null)
            return (ArrayList)resultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "product");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "product");
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
					log.error("Error while closing the Resultset in ProductDAOImpl getProductList()",sqlExp);
					throw new TTKException(sqlExp, "product");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProductDAOImpl getProductList()",sqlExp);
						throw new TTKException(sqlExp, "product");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProductDAOImpl getProductList()",sqlExp);
							throw new TTKException(sqlExp, "product");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "product");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}// End of getProductList(ArrayList alSearchObjects)

    /**
     * This method returns the ArrayList, which contains the RuleSynchronizationVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of RuleSynchronizationVO'S object's which contains the details of the Policies to Synchronize the Rules
     * @exception throws TTKException
     */
    public ArrayList getSynchPolicyList(ArrayList alSearchObjects) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        Collection<Object> resultList = new ArrayList<Object>();
        RuleSynchronizationVO ruleSynchVO = null;
        try{
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSynchPolicyList);

			cStmtObject.setLong(1,(Long)alSearchObjects.get(0));
			cStmtObject.setString(2,(String)alSearchObjects.get(1));
			cStmtObject.setString(3,(String)alSearchObjects.get(2));
			cStmtObject.setString(4,(String)alSearchObjects.get(3));
			cStmtObject.setString(5,(String)alSearchObjects.get(4));
			cStmtObject.setString(6,(String)alSearchObjects.get(6));
			cStmtObject.setString(7,(String)alSearchObjects.get(7));
			cStmtObject.setString(8,(String)alSearchObjects.get(8));
			cStmtObject.setString(9,(String)alSearchObjects.get(9));
			cStmtObject.setLong(10,(Long)alSearchObjects.get(5));
			cStmtObject.registerOutParameter(11,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(11);
			if(rs != null){
				while(rs.next()){
					ruleSynchVO = new RuleSynchronizationVO();

					if(rs.getString("POLICY_SEQ_ID") != null){
						ruleSynchVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)

					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						ruleSynchVO.setProdpolicySeqId(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)

					ruleSynchVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
					ruleSynchVO.setPrevPolicyNbr(TTKCommon.checkNull(rs.getString("RENEWAL_POLICY_NUMBER")));
					ruleSynchVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));
					ruleSynchVO.setCorporateName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));

					if(rs.getString("EFFECTIVE_FROM_DATE") != null){
						ruleSynchVO.setStartDate(new Date(rs.getTimestamp("EFFECTIVE_FROM_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_FROM_DATE") != null)

					if(rs.getString("EFFECTIVE_TO_DATE") != null){
						ruleSynchVO.setEndDate(new Date(rs.getTimestamp("EFFECTIVE_TO_DATE").getTime()));
					}//end of if(rs.getString("EFFECTIVE_TO_DATE") != null)
					
					if(rs.getString("RULE_SYNCH_DATE") != null){
						ruleSynchVO.setSynchDate(new Date(rs.getTimestamp("RULE_SYNCH_DATE").getTime()));
					}//end of if(rs.getString("RULE_SYNCH_DATE") != null)
					//denial process					
					if(rs.getString("DEN_SYNC_DATE") != null){
						ruleSynchVO.setInsurerSynchDate(new Date(rs.getTimestamp("DEN_SYNC_DATE").getTime()));
					}//end of if(rs.getString("RULE_SYNCH_DATE") != null)
					//denial process
					resultList.add(ruleSynchVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "product");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "product");
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
					log.error("Error while closing the Resultset in ProductDAOImpl getSynchPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "product");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProductDAOImpl getSynchPolicyList()",sqlExp);
						throw new TTKException(sqlExp, "product");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProductDAOImpl getSynchPolicyList()",sqlExp);
							throw new TTKException(sqlExp, "product");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "product");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return (ArrayList)resultList;
    }//end of getSynchPolicyList(ArrayList alSearchObjects)

    /**
     * This method synchronizes the rules of Products to Policies
     * @param strSeqID contains concatenated ProdpolicySeqID's of Policies
     * @param lngProdPolicySeqID contains productpolicySeqID of Product
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int synchronizeRule(String strSeqID,long lngProdPolicySeqID,String strDenialsyn) throws TTKException {
    	int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSynchronizeRule);
			cStmtObject.setString(1,strSeqID);
			cStmtObject.setLong(2,lngProdPolicySeqID);
			cStmtObject.setString(3,strDenialsyn);//denial process
			cStmtObject.registerOutParameter(4,Types.BIGINT);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(4);
			
			
		}//end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl synchronizeRule()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl synchronizeRule()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
    }//end of synchronizeRule(String strSeqID,long lngProdPolicySeqID)

	/**
	 * This method adds/updates the ProductVO which contains insurance products details
	 * @param productVO the details of insurance product which has to be added or updated
	 * @return Object[] the values,of  Product Policy Sequence Id and InsAbberevation Code
	 * @exception throws TTKException
	 */
	public Object[] addUpdateProduct(ProductVO productVO) throws TTKException {
		Object[] objArrayResult = new Object[2];
		long lResult = 0;
		String strAbbrCode = "";
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strAddUpdateProduct);
			
			if (productVO.getProdPolicySeqID()!=null){
				cStmtObject.setLong(PROD_POLICY_SEQ_ID,productVO.getProdPolicySeqID());
			}//end of if (productVO.getProdPolicySeqID()!=null)
			else{
				cStmtObject.setLong(PROD_POLICY_SEQ_ID,0);
			}//end of else

			if (productVO.getInsSeqId()!=null){
				cStmtObject.setLong(INS_SEQ_ID,productVO.getInsSeqId());
			}//end of if (productVO.getInsSeqId()!=null)
			else{
				cStmtObject.setLong(INS_SEQ_ID,0);
			}//end of else

			cStmtObject.setString(PRODUCT_TYPE_ID,productVO.getProdTypeId());
			cStmtObject.setString(PRODUCT_NAME,productVO.getProductName());
			cStmtObject.setString(V_AUTH_PRODUCT_CODE,productVO.getAuthorityProductCode());
			cStmtObject.setString(DESCRIPTION,productVO.getProdDesc());
			cStmtObject.setLong(USER_SEQ_ID,productVO.getUpdatedBy());
			cStmtObject.setString(PROD_STATUS_GENERAL_TYPE_ID,productVO.getProdStatTypeId());
			cStmtObject.setString(PRODUCT_NETWORK_TYPE,productVO.getProductNetworkType());

			if (productVO.getCardClearanceDays()!=null){
				cStmtObject.setLong(CARD_CLEARANCE_DAYS,productVO.getCardClearanceDays());
			}//end of if (productVO.getCardClearanceDays()!=null)
			else{
				cStmtObject.setString(CARD_CLEARANCE_DAYS,null);
			}//end of else

			if (productVO.getClaimClearanceDays()!=null){
				cStmtObject.setLong(CLAIM_CLEARANCE_DAYS,productVO.getClaimClearanceDays());
			}//end of if (productVO.getClaimClearanceDays()!=null)
			else{
				cStmtObject.setString(CLAIM_CLEARANCE_DAYS,null);
			}//end of else

			if (productVO.getPaClearanceHours()!=null){
				cStmtObject.setLong(PA_CLEARANCE_HOURS,productVO.getPaClearanceHours());
			}//end of if (productVO.getPaClearanceHours()!=null)
			else{
				cStmtObject.setString(PA_CLEARANCE_HOURS,null);
			}//end of else

			if(productVO.getTemplateID() != null){
				cStmtObject.setLong(TEMPLATE_ID,productVO.getTemplateID());
			}//end of if(productVO.getTemplateID() != null)
			else{
				cStmtObject.setString(TEMPLATE_ID,null);
			}//end of else

			cStmtObject.setString(DISCHARGE_VOUCH_MANDATORY_YN,productVO.getDischargeVoucherMandatoryYN());
			cStmtObject.setString(INS_PRODUCT_CODE,productVO.getInsProductCode());
			
			if(productVO.getTenure() != null){
				cStmtObject.setInt(TENURE,productVO.getTenure());
			}//end of if(productVO.getTenure() != null)
			else{
				cStmtObject.setString(TENURE,null);
			}//end of else
			
			cStmtObject.setString(AUTH_LETTER_GENERAL_TYPE_ID,productVO.getAuthLtrTypeID());
			cStmtObject.setString(STOP_PREAUTH_YN,productVO.getStopPreAuthsYN());
			cStmtObject.setString(STOP_CLAIM_YN,productVO.getStopClaimsYN());
			cStmtObject.setString(SURGERY_GNRL_TYPE_ID,productVO.getSurgeryMandtryID());
			cStmtObject.setString(OPD_BENEFITS_YN,productVO.getopdClaimsYN());//KOC 1286 for OPD Benefit
			cStmtObject.setString(CBH_SUMINS_YN,productVO.getCashBenefitsYN());  //KOC 1270 for hospital cash benefit
			cStmtObject.setString(CONV_SUMINS_YN,productVO.getConvCashBenefitsYN());  //KOC 1270 for hospital cash benefit
			//added for KOC-1273
			cStmtObject.setString(CRITICAL_ILLNESS_BENEFIT,productVO.getCriticalBenefitYN());
			cStmtObject.setString(SURVIVAL_PERIOD,productVO.getSurvivalPeriodYN());
			//ended
			
            
			 
            cStmtObject.setString(Pat_Enable_YN,productVO.getPatEnableYN());
            cStmtObject.setString(Clm_Enable_YN,productVO.getClmEnableYN());
            cStmtObject.setString(Pat_Mail_To,productVO.getPatMailTo());
            cStmtObject.setString(Pat_Mail_CC,productVO.getPatMailCC());
            cStmtObject.setString(Clm_Mail_To,productVO.getClmMailTo());
            cStmtObject.setString(Clm_Mail_CC,productVO.getClmMailCC());			
			cStmtObject.registerOutParameter(INSCOMP_ABBR_CODE,Types.VARCHAR );
			cStmtObject.registerOutParameter(PROD_POLICY_SEQ_ID,Types.BIGINT);
			 cStmtObject.setString(HEALTH_AUTHORITY,productVO.getHealthAuthority());
			 
			 cStmtObject.setString(CO_INS,productVO.getCoInsurance());
			 cStmtObject.setString(DEDUCTIBLE,productVO.getDeductable());
			 cStmtObject.setString(CLASS_ROOM_TYPE,productVO.getClassRoomType());
			 cStmtObject.setString(PLAN_TYPE,productVO.getPlanType());
			 cStmtObject.setString(MATERNITY_YN,productVO.getMaternityYN());
			 cStmtObject.setString(MATERNITY_COPAY,productVO.getMaternityCopay());
			 cStmtObject.setString(OPTICAL_YN,productVO.getOpticalYN());
			 cStmtObject.setString(OPTICAL_COPAY,productVO.getOpticalCopay());
			 cStmtObject.setString(DENTAL_YN,productVO.getDentalYN());
			 cStmtObject.setString(DENTAL_COPAY,productVO.getDentalCopay());
			 cStmtObject.setString(ELIGIBILITY,productVO.getEligibility());
			 cStmtObject.setString(IP_OP_SERVICES,productVO.getIpopServices());
			 cStmtObject.setString(PHARMACEUTICALS,productVO.getPharmaceutical());
			 cStmtObject.registerOutParameter(ROWS_PROCESSED,Types.INTEGER);
			cStmtObject.execute();
			lResult = cStmtObject.getLong(PROD_POLICY_SEQ_ID);
			strAbbrCode = cStmtObject.getString(INSCOMP_ABBR_CODE);
			objArrayResult[0] = new Long(lResult);
			objArrayResult[1] = strAbbrCode;
		}// end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl addUpdateProduct()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl addUpdateProduct()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return objArrayResult;
	}// End of addUpdateProduct(ProductVO productVO)

	/**
	 * This method deletes concerned InsuranceProduct details from the database
	 * @param alProductList which contains the id's of the Insurance Product's
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int deleteProduct(ArrayList alProductList) throws TTKException{
		int iResult = 0;
		Connection conn = null;
		CallableStatement cStmtObject=null;
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strDeleteProduct);
			cStmtObject.setString(1,TTKCommon.checkNull((String)alProductList.get(0)));  //Concatenated Product Sequence IDs.
			cStmtObject.setLong(2, (Long)alProductList.get(1));//user sequence id
			cStmtObject.registerOutParameter(3, Types.INTEGER);//out parameter which gives the number of records deleted
			cStmtObject.execute();
			iResult = cStmtObject.getInt(3);
		}// end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl deleteProduct()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl deleteProduct()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}// End of deleteProduct(ArrayList alProductList)

	/**
	 * This method returns the ProductVO, which contains all the product details
	 * @param lngProdPolicySeqID the productpolicySequenceID for which the product Details has to be fetched
	 * @return ProductVO object which contains all the Product details
	 * @exception throws TTKException
	 */
	public ProductVO getProductDetails(long lngProdPolicySeqID) throws TTKException
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ProductVO productVO = null;
		try
		{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetProduct);
			cStmtObject.setLong(1,lngProdPolicySeqID);
			
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while (rs.next()) {
					productVO = new ProductVO();

					if (rs.getString("PRODUCT_SEQ_ID")!=null){
						productVO.setProdSeqId(new Long(rs.getLong("PRODUCT_SEQ_ID")));
					}//end of if (rs.getString("PRODUCT_SEQ_ID")!=null)

					if (rs.getString("PROD_POLICY_SEQ_ID")!=null){
						productVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if (rs.getString("PROD_POLICY_SEQ_ID")!=null)

					productVO.setProdTypeId(TTKCommon.checkNull(rs.getString("PROD_GENERAL_TYPE_ID")));
					productVO.setProductName(TTKCommon.checkNull(rs.getString("PRODUCT_NAME")));
					productVO.setAuthorityProductCode(TTKCommon.checkNull(rs.getString("auth_product_code"))); 
					productVO.setProdDesc(TTKCommon.checkNull(rs.getString("PROD_DESC")));
					productVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					productVO.setCompanyAbbreviation(TTKCommon.checkNull(rs.getString("ABBREVATION_CODE")));
					
					productVO.setProductNetworkType(TTKCommon.checkNull(rs.getString("product_cat_type_id")));

					if (rs.getString("INS_SEQ_ID") != null){
						productVO.setInsSeqId(new Long(rs.getLong("INS_SEQ_ID")));
					}//end of if (rs.getString("INS_SEQ_ID") != null)

					productVO.setStatus(TTKCommon.checkNull(rs.getString("STATUSDESC")));
					productVO.setProdStatTypeId(TTKCommon.checkNull(rs.getString("PROD_STATUS_GENERAL_TYPE_ID")));
					productVO.setCardClearanceDays(rs.getString("CARD_CLEARANCE_DAYS")!=null ? new Long(rs.getLong("CARD_CLEARANCE_DAYS")):null);
					productVO.setClaimClearanceDays(rs.getString("CLAIM_CLEARANCE_DAYS")!=null ? new Long(rs.getLong("CLAIM_CLEARANCE_DAYS")):null);
					productVO.setPaClearanceHours(rs.getString("PA_CLEARANCE_HOURS")!=null ? new Long(rs.getLong("PA_CLEARANCE_HOURS")):null);
					productVO.setDischargeVoucherMandatoryYN(TTKCommon.checkNull(rs.getString("DV_REQD_GENERAL_TYPE_ID")));
					if(rs.getString("TEMPLATE_ID") != null){
						productVO.setTemplateID(new Long(rs.getLong("TEMPLATE_ID")));
					}//end of if(rs.getString("TEMPLATE_ID") != null)
					productVO.setInsProductCode(TTKCommon.checkNull(rs.getString("INS_PRODUCT_CODE")));
					
					if(rs.getString("TENURE") != null){
						productVO.setTenure(new Integer(rs.getInt("TENURE")));
					}//end of if(rs.getString("TENURE") != null)
					
					productVO.setAuthLtrTypeID(TTKCommon.checkNull(rs.getString("AUTH_LETTER_GENERAL_TYPE_ID")));
					productVO.setStopPreAuthsYN(TTKCommon.checkNull(rs.getString("STOP_PREAUTH_YN")));
					productVO.setStopClaimsYN(TTKCommon.checkNull(rs.getString("STOP_CLAIM_YN")));
					productVO.setSurgeryMandtryID(TTKCommon.checkNull(rs.getString("SURGERY_GEN_TYPE_ID")));
					productVO.setopdClaimsYN(TTKCommon.checkNull(rs.getString("OPD_BENEFITS_YN")));//KOC 1286 for OPD Benefit
					productVO.setCashBenefitsYN(TTKCommon.checkNull(rs.getString("CBH_SUMINS_YN"))); // KOC 1270 for hospital cash benefit
					productVO.setConvCashBenefitsYN(TTKCommon.checkNull(rs.getString("CONV_SUMINS_YN"))); // KOC 1270 for hospital cash benefit
					//added for KOC-1273
					productVO.setCriticalBenefitYN(TTKCommon.checkNull(rs.getString("Critical_Sumins_Yn")));
					productVO.setSurvivalPeriodYN(TTKCommon.checkNull(rs.getString("SURVIVAL_PERIOD_YN")));
					 productVO.setSurvivalPeriodYN(TTKCommon.checkNull(rs.getString("SURVIVAL_PERIOD_YN")));
                    //1274A
                    productVO.setPatEnableYN(TTKCommon.checkNull(rs.getString("PAT_ALLOWED_YN")));
					productVO.setPatMailTo(TTKCommon.checkNull(rs.getString("PAT_TO_MAIL_ID")));
					productVO.setPatMailCC(TTKCommon.checkNull(rs.getString("PAT_CC_MAIL_ID")));
					productVO.setClmEnableYN(TTKCommon.checkNull(rs.getString("CLM_ALLOWED_YN")));
					productVO.setClmMailTo(TTKCommon.checkNull(rs.getString("CLM_TO_MAIL_ID")));
                    productVO.setClmMailCC(TTKCommon.checkNull(rs.getString("CLM_CC_MAIL_ID")));
                    productVO.setHealthAuthority(TTKCommon.checkNull(rs.getString("AUTHORITY_TYPE")));
			
                    productVO.setCoInsurance(TTKCommon.checkNull(rs.getInt("CO_INS")).toString());
                    productVO.setDeductable(TTKCommon.checkNull(rs.getString("DEDUCTIBLE")));
                    productVO.setClassRoomType(TTKCommon.checkNull(rs.getString("CLASS")));
                    productVO.setPlanType(TTKCommon.checkNull(rs.getString("PLAN")));
                    productVO.setMaternityYN(TTKCommon.checkNull(rs.getString("MATERNITY_YN")));
                    productVO.setMaternityCopay(TTKCommon.checkNull(rs.getString("MATERNITY_COPAY")));
                    productVO.setOpticalYN(TTKCommon.checkNull(rs.getString("OPTICAL_YN")));
                    productVO.setOpticalCopay(TTKCommon.checkNull(rs.getString("OPTICAL_COPAY")));
                    productVO.setDentalYN(TTKCommon.checkNull(rs.getString("DENTAL_YN")));
                    productVO.setDentalCopay(TTKCommon.checkNull(rs.getString("DENTAL_COPAY")));
                    productVO.setEligibility(TTKCommon.checkNull(rs.getString("ELIGIBILITY")));
                    productVO.setIpopServices(TTKCommon.checkNull(rs.getString("IP_OP_SERVICES")));
                    productVO.setPharmaceutical(TTKCommon.checkNull(rs.getString("PHARMACEUTICALS")));
                    productVO.setNetworkSortNum(TTKCommon.checkNull(rs.getString("NETWORK_SORT_NO")));
				}//end of while(rs.next())
			}//end of if(rs != null)
			return productVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "product");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "product");
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
					log.error("Error while closing the Resultset in ProductDAOImpl getProductDetails()",sqlExp);
					throw new TTKException(sqlExp, "product");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProductDAOImpl getProductDetails()",sqlExp);
						throw new TTKException(sqlExp, "product");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProductDAOImpl getProductDetails()",sqlExp);
							throw new TTKException(sqlExp, "product");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "product");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getProductDetails(long lngProdPolicySeqID)

    /**
	 * This method saves the associated groups information to the product in to the database
	 * @param lngProductPolicySeqID Product Policy Sequence ID for which the user groups are associated
	 * @param strOfficeSeqID the assciated office sequence ID's
	 * @param lngUserSeqID the user adding the information
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int saveUserGroup(Long lngProductPolicySeqID, String strOfficeSeqID,Long lngUserSeqID) throws TTKException
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		int iResult = 0;
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSaveUserGroup);
			cStmtObject.setLong(1,lngProductPolicySeqID);
			cStmtObject.setString(2, strOfficeSeqID);//Pipe Concatenated UserGroup Office Sequence ID's
			cStmtObject.setLong(3, lngUserSeqID);//User Sequence ID
			cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
			cStmtObject.execute();
			iResult = cStmtObject.getInt(4);
		}// end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl saveUserGroup()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl saveUserGroup()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}// End of saveUserGroup(Long lngProductSeqID, String strOfficeSeqID,Long lngUserSeqID)

	/**
	 * This method returns the ArrayList of GroupVO's which are populated from the database
	 * @param lngProductSeqID the Product Sequence ID for which the user groups associated to
	 * @return ArrayList Which contains the GroupVO's which are associated with the particular product sequence id
	 * @exception throws TTKException
	 */
	public ArrayList getUserGroup(Long lngProductSeqID) throws TTKException	{
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		GroupVO groupVO = null;
		ArrayList<Object> alResultList = new ArrayList<Object>();
		try
		{
			conn = ResourceManager.getConnection();
			pStmt = conn.prepareStatement(strGetUserGroup);
			pStmt.setLong(1,lngProductSeqID);
			pStmt.setLong(2,lngProductSeqID);
			rs = pStmt.executeQuery();
			if(rs != null){
				while (rs.next()) {
					groupVO = new GroupVO();
					groupVO.setGroupSeqID(rs.getString("GROUP_SEQ_ID")!=null ? new Long(rs.getLong("GROUP_SEQ_ID")):null);
					groupVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					groupVO.setGroupBranchSeqID(rs.getString("GROUP_BRANCH_SEQ_ID")!=null ? new Long(rs.getLong("GROUP_BRANCH_SEQ_ID")):null);
					groupVO.setOfficeSeqID(rs.getString("TPA_OFFICE_SEQ_ID")!=null ? new Long(rs.getLong("TPA_OFFICE_SEQ_ID")):null);
					groupVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
					groupVO.setOfficeCode(TTKCommon.checkNull(rs.getString("TPA_OFFICE_GENERAL_TYPE_ID")));
					groupVO.setInsCompName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					alResultList.add(groupVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "product");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "product");
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
					log.error("Error while closing the Resultset in ProductDAOImpl getUserGroup()",sqlExp);
					throw new TTKException(sqlExp, "product");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProductDAOImpl getUserGroup()",sqlExp);
						throw new TTKException(sqlExp, "product");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProductDAOImpl getUserGroup()",sqlExp);
							throw new TTKException(sqlExp, "product");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "product");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
	}// End of getUserGroup(Long lngProductSeqID)

	 /**
	 * This method saves the associated groups information to the product in to the database
	 * @param lngProductPolicySeqID Product Policy Sequence ID for which the user groups are associated
	 * @param strOfficeSeqID the assciated office sequence ID's
	 * @param lngUserSeqID the user adding the information
	 * @return int value, greater than zero indicates sucessfull execution of the task
	 * @exception throws TTKException
	 */
	public int copyProductRules(Long lngProductSeqID,Long lngProdPolicySeqId,Long lngUserSeqID) throws TTKException
	{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		int iResult = 0;
		try {
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strCopyProduct);
			cStmtObject.setLong(1,lngProductSeqID);
			cStmtObject.setLong(2, lngProdPolicySeqId);
			cStmtObject.setLong(3, lngUserSeqID);//User Sequence ID
			cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
			cStmtObject.execute();
			iResult = cStmtObject.getInt(4);
		}// end of try
		catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl copyProductRules()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl copyProductRules()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}// End of copyProductRules(Long lngProductSeqID, String strOfficeSeqID,Long lngUserSeqID)
	
	/**
     * This method saves the associated procedure information to the insurance company into the database
     * @param alAssValues ArrayList object which contains the form values
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int getAssociateExecute(ArrayList alAssValues) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	int iResult=0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strAssInsProduct);
			cStmtObject.setLong(1,(Long)alAssValues.get(0));  //Concatenated Policy Sequence IDs.
			cStmtObject.setString(2, TTKCommon.checkNull((String)alAssValues.get(1)));//head office flag
			if(!TTKCommon.checkNull((String)alAssValues.get(2)).equals(""))
			{
				//cStmtObject.setTimestamp(3, new Timestamp((Long)alAssValues.get(2)));//head office effective date
				cStmtObject.setTimestamp(3, new Timestamp(TTKCommon.getUtilDate((String)alAssValues.get(2)).getTime()));
				//cStmtObject.setTimestamp(3, new Timestamp((Long)alAssValues.get(2)));
			}//end of if(alAssValues.get(2) != null)
			else
			{
				cStmtObject.setTimestamp(3, null);
			}//end of else
			cStmtObject.setString(4, TTKCommon.checkNull((String)alAssValues.get(3)));//reginal office flag
			cStmtObject.setString(5, TTKCommon.checkNull((String)alAssValues.get(4)));//pipe concatenated list OF INS_SEQ_IDS
			if(!TTKCommon.checkNull((String)alAssValues.get(5)).equals(""))
			{
				//cStmtObject.setTimestamp(6, new Timestamp((Long)alAssValues.get(5)));//reginal office effective date
				cStmtObject.setTimestamp(6, new Timestamp(TTKCommon.getUtilDate((String)alAssValues.get(5)).getTime()));
				//cStmtObject.setTimestamp(6, new Timestamp((Long)alAssValues.get(5)));
			}//end of if(alAssValues.get(5) != null)
			else
			{
				cStmtObject.setTimestamp(6, null);
			}//end of else
			cStmtObject.setString(7, TTKCommon.checkNull((String)alAssValues.get(6)));//divisional office flag
			cStmtObject.setString(8, TTKCommon.checkNull((String)alAssValues.get(7)));//pipe concatenated list OF INS_SEQ_IDS
			if(!TTKCommon.checkNull((String)alAssValues.get(8)).equals(""))
			{
				//cStmtObject.setTimestamp(9, new Timestamp((Long)alAssValues.get(8)));//divisional office effective date
				cStmtObject.setTimestamp(9, new Timestamp(TTKCommon.getUtilDate((String)alAssValues.get(8)).getTime()));
			}//end of if(alAssValues.get(8) != null)
			else
			{
				cStmtObject.setTimestamp(9, null);
			}//end of else
			cStmtObject.setString(10, TTKCommon.checkNull((String)alAssValues.get(9)));//branch office flag
			cStmtObject.setString(11, TTKCommon.checkNull((String)alAssValues.get(10)));//pipe concatenated list OF INS_SEQ_IDS
			if(!TTKCommon.checkNull((String)alAssValues.get(11)).equals(""))
			{
				cStmtObject.setTimestamp(12, new Timestamp(TTKCommon.getUtilDate((String)alAssValues.get(11)).getTime()));
				//cStmtObject.setTimestamp(12, new Timestamp((Long)alAssValues.get(11)));//branch office effective date
			}//end of if(alAssValues.get(11) != null)
			else
			{
				cStmtObject.setTimestamp(12, null);
			}//end of if(alAssValues.get(11) != null)
			cStmtObject.setString(13, TTKCommon.checkNull((String)alAssValues.get(12)));//enroll types and commissions with pipe
			cStmtObject.setLong(14, (Long)alAssValues.get(13));//updated by
			cStmtObject.registerOutParameter(15, Types.INTEGER);//out parameter which gives the number of records deleted
			cStmtObject.execute();
			iResult = cStmtObject.getInt(15);
		}catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl saveUserGroup()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl saveUserGroup()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
    }//public ArrayList getAssociateExecute(ArrayList alSearchObjects)
    
    /**
     * This method saves the Prod Policy Limit information for the corresponding Enrollment Type
     * @param alProdPolicyLimit ArrayList object which contains the form values
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int saveProdPolicyLimit(ArrayList alProdPolicyLimit) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	int iResult=0;
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSaveProdPolicyLimit);
			
			cStmtObject.setLong(1,(Long)alProdPolicyLimit.get(0));
			cStmtObject.setString(2,(String)alProdPolicyLimit.get(1));
			cStmtObject.setLong(3,(Long)alProdPolicyLimit.get(2));
			cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records inserted/updated
			cStmtObject.execute();
			iResult = cStmtObject.getInt(4);
		}//end of try
    	catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl saveProdPolicyLimit()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl saveProdPolicyLimit()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
    }//end of saveProdPolicyLimit(ArrayList alProdPolicyLimit)
    
    /**
     * This method returns the ArrayList, which contains the ProdPolicyLimitVO's which are populated from the database
     * @param lngProdPolicySeqID long value which contains ProdPolicySeqID
     * @return ArrayList of ProdPolicyLimitVO'S object's which contains the Buffer details
     * @exception throws TTKException
     */
    public ArrayList<Object> getProdPolicyLimit(long lngProdPolicySeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        Collection<Object> resultList = new ArrayList<Object>();
        ProdPolicyLimitVO prodPolicyLimitVO = null;
        try{
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetProdPolicyLimit);
			
			cStmtObject.setLong(1,lngProdPolicySeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					prodPolicyLimitVO = new ProdPolicyLimitVO();
					
					if(rs.getString("INS_PROD_POLICY_LIMITS_SEQ_ID") != null){
						prodPolicyLimitVO.setLimitSeqID(new Long(rs.getLong("INS_PROD_POLICY_LIMITS_SEQ_ID")));
					}//end of if(rs.getString("INS_PROD_POLICY_LIMITS_SEQ_ID") != null)
					else{
						prodPolicyLimitVO.setLimitSeqID(null);
					}//end of else
					
					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						prodPolicyLimitVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
					
					prodPolicyLimitVO.setEnrolTypeID(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")));
					prodPolicyLimitVO.setEnrolDesc(TTKCommon.checkNull(rs.getString("ENROL_DESCRIPTION")));
					prodPolicyLimitVO.setFlag(TTKCommon.checkNull(rs.getString("V_FLAG")));
					
					if(rs.getString("RENEWAL_INTERVAL_DAYS") != null){
						prodPolicyLimitVO.setRenewalDays(new Integer(rs.getInt("RENEWAL_INTERVAL_DAYS")));
					}//end of if(rs.getString("RENEWAL_INTERVAL_DAYS") != null)
					else{
						prodPolicyLimitVO.setRenewalDays(null);
					}//end of else
					
					resultList.add(prodPolicyLimitVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "product");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "product");
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
					log.error("Error while closing the Resultset in ProductDAOImpl getProdPolicyLimit()",sqlExp);
					throw new TTKException(sqlExp, "product");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProductDAOImpl getProdPolicyLimit()",sqlExp);
						throw new TTKException(sqlExp, "product");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProductDAOImpl getProdPolicyLimit()",sqlExp);
							throw new TTKException(sqlExp, "product");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "product");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return (ArrayList<Object>)resultList;
    }//end of getProdPolicyLimit(long lngProdPolicySeqID)


	/*public static void main(String a[]) throws Exception {
		//ProductDAOImpl productDAO = new ProductDAOImpl();
		//getAssociateExecute
		
		ArrayList alSearchCriteria = new ArrayList();
		alSearchCriteria.add("9211");
		alSearchCriteria.add("|110|");
		alSearchCriteria.add(null);
		alSearchCriteria.add("");
		alSearchCriteria.add("");//5
		//alSearchCriteria.add(new Timestamp((Long)new Date().getTime()));
		alSearchCriteria.add(null);
		alSearchCriteria.add("");
		alSearchCriteria.add("");
		alSearchCriteria.add(null);
		alSearchCriteria.add("");//10
		alSearchCriteria.add("");
		alSearchCriteria.add(null);
		alSearchCriteria.add("|IND|7.7|");
		alSearchCriteria.add("1");
		int i = productDAO.getAssociateExecute(alSearchCriteria);
		
		productDAO.getSynchPolicyList(alSearchCriteria);
		String strSeqID = "|322561|";
		productDAO.synchronizeRule(strSeqID,new Long(320391));
		
		ArrayList<Object> alProdPolicyLimit = new ArrayList<Object>();
		alProdPolicyLimit.add(new Long(320391));
		//alProdPolicyLimit.add("||Y|IND|5||Y|ING|5||Y|COR|5|");
		alProdPolicyLimit.add("|1|Y|IND|6|2|Y|ING|7|3|N|COR|5|");
		alProdPolicyLimit.add(new Long(56503));
		productDAO.saveProdPolicyLimit(alProdPolicyLimit);
		
		//productDAO.getProdPolicyLimit(new Long(320391));
	}//end of main
*/
    
    
    //added for baja enhan
    public ArrayList<Object> getProdPolicyEscalateLimit(long lngProdPolicySeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Collection<Object> resultList = new ArrayList<Object>();
        EscalationLimitVO escalationLimitVO = null;
        EscalationLimitVO escalationLimitVO1 = null;
        try{
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetProdPolicyEscalateLimit);
			
			cStmtObject.setLong(1,lngProdPolicySeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(rs != null){
				while(rs.next()){
                                    
					escalationLimitVO = new EscalationLimitVO();
					
					if(rs.getString("PROD_POL_ESC_SEQ_ID") != null){
						escalationLimitVO.setLimitSeqID(new Long(rs.getLong("PROD_POL_ESC_SEQ_ID")));
					}//end of if(rs.getString("INS_PROD_POLICY_LIMITS_SEQ_ID") != null)
					else{
						escalationLimitVO.setLimitSeqID(null);
					}//end of else
					
					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						escalationLimitVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
					
					escalationLimitVO.setPatClmTypeID(TTKCommon.checkNull(rs.getString("PAT_CLM_TYPE")));
                    escalationLimitVO.setFlag(TTKCommon.checkNull(rs.getString("FREQ_FLAG")));
					escalationLimitVO.setRemType(TTKCommon.checkNull(rs.getString("REMAINDER_TYPE")));
					if(rs.getString("REMAINDER_VALUE") != null){
						escalationLimitVO.setEscalateDays(new Integer(rs.getInt("REMAINDER_VALUE")));
					}//end of if(rs.getString("RENEWAL_INTERVAL_DAYS") != null)
					else{
						escalationLimitVO.setEscalateDays(null);
					}//end of else
					
					resultList.add(escalationLimitVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			
			
		/*	if(rs1 != null){
				while(rs1.next()){
					escalationLimitVO1 = new EscalationLimitVO();
					
					if(rs1.getString("PROD_POL_ESC_SEQ_ID") != null){
						escalationLimitVO1.setLimitSeqID(new Long(rs1.getLong("PROD_POL_ESC_SEQ_ID")));
					}//end of if(rs.getString("INS_PROD_POLICY_LIMITS_SEQ_ID") != null)
					else{
						escalationLimitVO1.setLimitSeqID(null);
					}//end of else
					
					if(rs1.getString("PROD_POLICY_SEQ_ID") != null){
						escalationLimitVO1.setProdPolicySeqID(new Long(rs1.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
					
					escalationLimitVO1.setPatClmTypeID(TTKCommon.checkNull(rs1.getString("PAT_CLM_TYPE")));
                    escalationLimitVO1.setFlag(TTKCommon.checkNull(rs1.getString("FREQ_FLAG")));
					escalationLimitVO1.setRemType(TTKCommon.checkNull(rs1.getString("REMAINDER_TYPE")));
					if(rs1.getString("REMAINDER_VALUE") != null){
						escalationLimitVO1.setEscalateDays(new Integer(rs1.getInt("REMAINDER_VALUE")));
					}//end of if(rs.getString("RENEWAL_INTERVAL_DAYS") != null)
					else{
						escalationLimitVO1.setEscalateDays(null);
					}//end of else
					
					resultList.add(escalationLimitVO1);
				}//end of while(rs.next())
			}//end of if(rs != null)
*/			
		
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "product");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "product");
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
					log.error("Error while closing the Resultset in ProductDAOImpl getProdPolicyLimit()",sqlExp);
					throw new TTKException(sqlExp, "product");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProductDAOImpl getProdPolicyLimit()",sqlExp);
						throw new TTKException(sqlExp, "product");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProductDAOImpl getProdPolicyLimit()",sqlExp);
							throw new TTKException(sqlExp, "product");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "product");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs1 = null;
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return (ArrayList<Object>)resultList;
    }//end of getProdPolicyLimit(long lngProdPolicySeqID)
    
    
    public ArrayList<Object> getProdPolicyEscalateLimitclm(long lngProdPolicySeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Collection<Object> resultList = new ArrayList<Object>();
        EscalationLimitVO escalationLimitVO = null;
        EscalationLimitVO escalationLimitVO1 = null;
        try{
        	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strGetProdPolicyEscalateLimit);
			
			cStmtObject.setLong(1,lngProdPolicySeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
		
		/*	if(rs != null){
				while(rs.next()){
					log.info("here");
					escalationLimitVO = new EscalationLimitVO();
					
					if(rs.getString("PROD_POL_ESC_SEQ_ID") != null){
						escalationLimitVO.setLimitSeqID(new Long(rs.getLong("PROD_POL_ESC_SEQ_ID")));
					}//end of if(rs.getString("INS_PROD_POLICY_LIMITS_SEQ_ID") != null)
					else{
						escalationLimitVO.setLimitSeqID(null);
					}//end of else
					
					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						escalationLimitVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
					
					escalationLimitVO.setPatClmTypeID(TTKCommon.checkNull(rs.getString("PAT_CLM_TYPE")));
                    escalationLimitVO.setFlag(TTKCommon.checkNull(rs.getString("FREQ_FLAG")));
					escalationLimitVO.setRemType(TTKCommon.checkNull(rs.getString("REMAINDER_TYPE")));
					if(rs.getString("REMAINDER_VALUE") != null){
						escalationLimitVO.setEscalateDays(new Integer(rs.getInt("REMAINDER_VALUE")));
					}//end of if(rs.getString("RENEWAL_INTERVAL_DAYS") != null)
					else{
						escalationLimitVO.setEscalateDays(null);
					}//end of else
					
					resultList.add(escalationLimitVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			*/
			
		if(rs1 != null){
				while(rs1.next()){
					
				
					escalationLimitVO1 = new EscalationLimitVO();
					
					if(rs1.getString("PROD_POL_ESC_SEQ_ID") != null){
						escalationLimitVO1.setLimitSeqID(new Long(rs1.getLong("PROD_POL_ESC_SEQ_ID")));
					}//end of if(rs.getString("INS_PROD_POLICY_LIMITS_SEQ_ID") != null)
					else{
						escalationLimitVO1.setLimitSeqID(null);
					}//end of else
					
					if(rs1.getString("PROD_POLICY_SEQ_ID") != null){
						escalationLimitVO1.setProdPolicySeqID(new Long(rs1.getLong("PROD_POLICY_SEQ_ID")));
					}//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
					
					escalationLimitVO1.setPatClmTypeID(TTKCommon.checkNull(rs1.getString("PAT_CLM_TYPE")));
                    escalationLimitVO1.setFlag(TTKCommon.checkNull(rs1.getString("FREQ_FLAG")));
					escalationLimitVO1.setRemType(TTKCommon.checkNull(rs1.getString("REMAINDER_TYPE")));
					if(rs1.getString("REMAINDER_VALUE") != null){
						escalationLimitVO1.setEscalateDays(new Integer(rs1.getInt("REMAINDER_VALUE")));
					}//end of if(rs.getString("RENEWAL_INTERVAL_DAYS") != null)
					else{
						escalationLimitVO1.setEscalateDays(null);
					}//end of else
					
					resultList.add(escalationLimitVO1);
				}//end of while(rs.next())
			}//end of if(rs != null)
			
		
        }//end of try
        catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "product");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "product");
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
					log.error("Error while closing the Resultset in ProductDAOImpl getProdPolicyLimit()",sqlExp);
					throw new TTKException(sqlExp, "product");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in ProductDAOImpl getProdPolicyLimit()",sqlExp);
						throw new TTKException(sqlExp, "product");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in ProductDAOImpl getProdPolicyLimit()",sqlExp);
							throw new TTKException(sqlExp, "product");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "product");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs1 = null;
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return (ArrayList<Object>)resultList;
    }//end of getProdPolicyLimit(long lngProdPolicySeqID)
    public int saveProdPolicyEscalateLimit(ArrayList alProdPolicyEscalateLimit) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	int iResult=0;
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSaveProdPolicyEscalateLimit);
		
			cStmtObject.setLong(1,(Long)alProdPolicyEscalateLimit.get(0));
			cStmtObject.setString(2,(String)alProdPolicyEscalateLimit.get(1));
			cStmtObject.setLong(3,(Long)alProdPolicyEscalateLimit.get(2));
			cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records inserted/updated
			cStmtObject.execute();
			iResult = cStmtObject.getInt(4);
		}//end of try
    	catch (SQLException sqlExp) {
			throw new TTKException(sqlExp, "product");
		}// end of catch (SQLException sqlExp)
		catch (Exception exp) {
			throw new TTKException(exp, "product");
		}// end of catch (Exception exp)
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
        			log.error("Error while closing the Statement in ProductDAOImpl saveProdPolicyLimit()",sqlExp);
        			throw new TTKException(sqlExp, "product");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in ProductDAOImpl saveProdPolicyLimit()",sqlExp);
        				throw new TTKException(sqlExp, "product");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "product");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
    }//end of saveProdPolicyLimit(ArrayList alProdPolicyLimit)
    
    
}// End of class ProductDAOImpl
