/**
 * @ (#)  PolicyDaoImpl.java Nov 5, 2005
 * Project      : TTKPROJECT
 * File         : PolicyDaoImpl.java
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
import com.ttk.dto.administration.RuleSynchronizationVO;

import java.io.Serializable;
import java.math.BigDecimal;
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

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.administration.BufferDetailVO;
import com.ttk.dto.administration.BufferVO;
import com.ttk.dto.administration.ConfigCopayVO;
import com.ttk.dto.administration.CustConfigMsgVO;
import com.ttk.dto.administration.DomConfigVO;//added as per KOC 1285  
import com.ttk.dto.administration.InsuranceApproveVO;
import com.ttk.dto.administration.ProdPolicyLimitVO;
import com.ttk.dto.administration.ShortfallDaysConfigVO;//added asper KOC 1179
import com.ttk.dto.administration.SumInsuredRestrictionVO;//added as per KOC 1140 and 1142 and 1165
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.enrollment.PolicyDetailVO;
import com.ttk.dto.enrollment.PolicyVO;
public class PolicyDAOImpl implements BaseDAO,Serializable{
	
	private static Logger log = Logger.getLogger(PolicyDAOImpl.class);
	
	private static final String strPolicyList = "{CALL ADMINISTRATION_PKG.SELECT_ADMIN_POLICY_LIST(?,?,?,?,?,?,?)}";
	private static final String strPolicyDetails = "{CALL ADMINISTRATION_PKG.SELECT_ADMIN_POLICY(?,?,?)}";
    private static final String strSavePolicy = "{CALL ADMINISTRATION_PKG.SAVE_ADMIN_POLICY(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//added two parameter for OPD_4_hosptial // added one parameter for authority product id/ref no  
	private static final String strBroker_Group="SELECT tpa_group_branch.group_branch_seq_id,tpa_office_info.office_code || '-' || tpa_groups.group_name group_name FROM tpa_group_branch,tpa_groups,tpa_office_info WHERE ( tpa_groups.group_seq_id = tpa_group_branch.group_seq_id ) and ( tpa_office_info.tpa_office_seq_id = tpa_group_branch.tpa_office_seq_id ) and ( tpa_office_info.tpa_office_seq_id = ?) and ( tpa_group_branch.group_type = upper('BRO')) order by tpa_groups.group_name";
	//Modification as per KOC 1216B Change request
    private static final String strGroup = "SELECT tpa_group_branch.group_branch_seq_id,tpa_office_info.office_code || '-' || tpa_groups.group_name group_name FROM tpa_group_branch,tpa_groups,tpa_office_info WHERE ( tpa_groups.group_seq_id = tpa_group_branch.group_seq_id ) and ( tpa_office_info.tpa_office_seq_id = tpa_group_branch.tpa_office_seq_id ) and (tpa_groups.GROUP_TYPE is null OR tpa_groups.group_type='TTK') and ( tpa_office_info.tpa_office_seq_id = ?) order by tpa_groups.group_name";//Show only TTK users for User Group in Policy General Screen - kocnewBroker
    private static final String strBufferList = "{CALL ADMINISTRATION_PKG.SELECT_BUFFER_LIST(?,?,?,?,?,?,?)}";
    private static final String strGetBufferDetailAuthority = "SELECT C.DESCRIPTION AS ADMIN_AUTHORITY,D.DESCRIPTION AS BUFFER_ALLOCATION_TYPE FROM TPA_INS_PROD_POLICY A LEFT OUTER JOIN TPA_ENR_POLICY B ON (A.POLICY_SEQ_ID = B.POLICY_SEQ_ID) LEFT OUTER JOIN TPA_GENERAL_CODE C ON(B.ADMIN_AUTH_GENERAL_TYPE_ID = C.GENERAL_TYPE_ID) LEFT OUTER JOIN TPA_GENERAL_CODE D ON(B.BUFFER_ALLOC_GENERAL_TYPE_ID = D.GENERAL_TYPE_ID) WHERE A.PROD_POLICY_SEQ_ID  = ?";
    private static final String strGetBufferDetail = "{CALL ADMINISTRATION_PKG.SELECT_BUFFER_DETAILS(?,?,?,?)}";
    private static final String strSaveBuffer = "{CALL ADMINISTRATION_PKG.SAVE_BUFFER_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//added 7 param for hyundai buffer
    private static final String strGetBufferDescription = "{CALL ADMINISTRATION_PKG.GET_BUFFER_DECRIPTION(?,?)}";
	private static final String strGetCustomMsgInfo ="{CALL ADMINISTRATION_PKG.SELECT_SHOW_CUST_MSG(?,?)}";
	private static final String strSaveCustomMsgInfo ="{CALL ADMINISTRATION_PKG.SAVE_SHOW_CUST_MSG(?,?,?,?,?)}";
	private static final String strGetConfigCopayAmt ="{CALL ADMINISTRATION_PKG.SELECT_COPAY_APPROVED_AMT(?,?)}";
	private static final String strSaveConfigCopayAmt ="{CALL ADMINISTRATION_PKG.SAVE_COPAY_APPROVED_AMT(?,?,?,?,?)}";
    //Changes as paer KOC 1142 1140 and 1165 Change Request[Sum Insured restriction on copay,relation and age changes]
   // private static final String strSaveConfigCopayAmt ="{CALL ADMINISTRATION_PKG.SAVE_COPAY_APPROVED_AMT(?,?,?,?,?,?,?)}";
    private static final String strSaveConfigCopayMaxLimit ="{CALL ADMINISTRATION_PKG.SAVE_COPAY_MAX_LIMIT(?,?,?,?,?,?,?)}";
	private static final String strGetConfigSumInsuredRestrictedAmt ="{CALL ADMINISTRATION_PKG.SELECT_SI_RESTRICTION_REL(?,?)}";
	private static final String strSaveConfigSumInsuredRestrictedAmt ="{CALL ADMINISTRATION_PKG.SI_RESTRICTION_REL(?,?,?,?,?,?,?,?,?,?,?,?)}";
    //Shortfall Changes aded as per KOC1179
	private static final String strSaveShortfallDaysConfig ="{CALL ADMINISTRATION_PKG.SAVE_SHORTFALL_EMAIL_CONFIG(?,?,?,?,?,?,?,?,?,?,?)}";//added 10th parameter by satya1179//shortfall phase1 one parameter we added
	private static final String strSelectShortfallDaysConfig ="{CALL ADMINISTRATION_PKG.SELECT_SHORTFALL_EMAIL_CONFIG(?,?)}";//retrived intimation days by satya1179
	private static final String strSelectPreApprovalLimit ="{CALL ADMINISTRATION_PKG.SELECT_PRE_APPROVAL_LIMIT(?,?)}";
	private static final String strSavePreApprovalLimit ="{CALL ADMINISTRATION_PKG.SAVE_PRE_APPROVAL_LIMIT(?,?,?,?,?,?,?,?,?)}";
	//Changes as paer KOC 1140 Change Request
	
//added as per KOPC 1285 CR
	private static final String strSaveDomicilaryConfig ="{CALL ADMINISTRATION_PKG.SAVE_DOM_CONFIG(?,?,?,?)}";
	private static final String strSelectDomicilaryConfig ="{CALL ADMINISTRATION_PKG.SELECT_DOM_CONFIG(?,?)}";
	//added as per KOPC 1285 CR	
	
	
	private static final String strSaveInsApproveConfig ="{CALL CLAIMS_APPROVAL_PKG.SAVE_INS_APPROVAL_LIMIT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// added 5 paramsbajaj changes save_ins_approval_limit //modified for denial process we added 9 parameter extra
	private static final String strSelectInsApproveConfig ="{CALL CLAIMS_APPROVAL_PKG.SELECT_INS_APPROVAL_LIMIT(?,?)}";// bajaj Changes select_ins_approval_limit
	private static final String strGetProviderCode ="select PACKAGE_ID from app.DHA_BENFIT_PKG_MASTER where PACKAGENAME=?";

	
	
	
	
	private static final String strsaveBenefitTypeBufferLimit ="{CALL ADMINISTRATION_PKG.save_benefit_buffer(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	private static final String strGetBenefitBufferLimitDtls ="SELECT p.dntl_buffer_limit, p.optc_buffer_limit,p.omti_buffer_limit,p.opts_buffer_limit,p.opts_pharma_buffer_limit,p.ptr_dntl_buffer_limit,p.ptr_optc_buffer_limit,  p.ptr_omti_buffer_limit,p.ptr_opts_buffer_limit,p.ptr_opts_pharma_buffer_limit FROM tpa_ins_prod_policy p WHERE p.prod_policy_seq_id = ?";
	private static final String strReinsurancePlanList ="SELECT I.REINS_SEQ_ID,I.TREATY_ID FROM APP.TPA_REINS_INFO I WHERE UPPER(I.REINSURER_ID)=UPPER(?) ORDER BY I.TREATY_ID";
	
	private static final String  strPolicySynchList = "{CALL ADMINISTRATION_PKG.select_member_synclist(?,?,?,?,?,?,?)}";
	private static final String strPolSynchronizeRule = "{CALL ADMINISTRATION_PKG.synch_policyrule_member(?,?,?,?)}";
	private static final String strModifiedMembersList = "{CALL POLICY_ENROLLMENT_PKG.member_rule_modified(?,?,?,?,?,?)}";
	
	private static final int POLICY_SEQ_ID = 1;
    private static final int GROUP_BRANCH_SEQ_ID = 2;
    private static final int GEN_ENRID_GENERAL_TYPE_ID = 3;
    private static final int CHEQUE_ISSUED_GENERAL_TYPE = 4;
    private static final int CARD_CLEARANCE_DAYS = 5;
    private static final int CLAIM_CLEARANCE_DAYS = 6;
    private static final int PA_CLEARANCE_HOURS = 7;
    private static final int ADMIN_STATUS_GENERAL_TYPE_ID = 8;
    private static final int BUFFER_ALLOWED_YN = 9;
    private static final int ADMIN_AUTH_GENERAL_TYPE_ID = 10;
    private static final int BUFFER_ALLOC_GENERAL_TYPE_ID = 11;
    private static final int TEMPLATE_ID = 12;
    private static final int DISCHARGE_VOUCH_MANDATORY_YN=13;
    private static final int TENURE = 14;
    private static final int STOP_PREAUTH_YN = 15;
	private static final int STOP_CLAIM_YN = 16;
    private static final int USER_SEQ_ID = 17;
    //Changes as per 1216B Change request IBM CR
    private static final int MEMBER_BUFFER_YN =18;
	private static final int OPD_BENEFITS_YN = 19; 
    private static final int V_CBH_SUMINS_YN = 20; // KOC 1270 for hospital cash benefit
    private static final int V_CONV_SUMINS_YN = 21; // KOC 1270 for hospital cash benefit
    //added for KOC - 1273
    private static final int CRITICAL_BENEFIT = 22;
    private static final int SURVIVAL_PERIOD = 23;
    private static final int Pat_Enable_YN = 24; // koc 1274A
    private static final int Clm_Enable_YN = 25; // koc 1274A
    private static final int Pat_Mail_To = 26; //   koc 1274A
    private static final int Pat_Mail_CC = 27; //   koc 1274A
    private static final int Clm_Mail_To = 28; //   koc 1274A
    private static final int Clm_Mail_CC = 29; //   koc 1274A
    private static final int hcu_pay_to_general_type=30;//OPD_4_hosptial
    private static final int tpa_name=31;//OPD_4_hosptial
    private static final int BROKER_GROUP_ID = 32;
    private static final int BROKER_YN = 33;
    private static final int HR_APPR_REQUIRED_YN=34;//added for hyundai buffer
    
    private static final int CO_INS=37;
    private static final int DEDUCTIBLE=38;
    private static final int CLASS_ROOM_TYPE=39;                   
    private static final int PLAN_TYPE=40;                    
    private static final int MATERNITY_YN=41;                               
    private static final int MATERNITY_COPAY=42;      
    private static final int OPTICAL_YN=43;                     
    private static final int OPTICAL_COPAY=44;                             
    private static final int DENTAL_YN=45;                       
    private static final int DENTAL_COPAY=46;                              
    private static final int ELIGIBILITY=47;                                         
    private static final int IP_OP_SERVICES=48;                                             
    private static final int PHARMACEUTICALS=49;
    
    private static final int ROW_PROCESSED = 35;
    private static final int auth_product_code = 36;				
    private static final int OPTS_PAT_LIMIT = 50;
    private static final int STOPCASHLESS=51;
    private static final int STOPCLAIMS=52;
    private static final int MAIL_NOTIFICATION_FLAG = 53;
    private static final int TRITY_TYPE_ID =54;
    private static final int REINSURER=55;
    //private static final String strConnection ="Connection";
	
    public Object[] getPreApprovedLimit(long lngProdPolicySeqID) throws TTKException{
		   
		   Object[] objArry=new Object[6];
		   Connection conn = null;
	       CallableStatement cStmtObject=null;
	       ResultSet rs = null;
	       try{
	       	conn = ResourceManager.getConnection();
	       	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectPreApprovalLimit);
	           
	           cStmtObject.setLong(1,lngProdPolicySeqID);
	           cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	           cStmtObject.execute();
	           rs = (java.sql.ResultSet)cStmtObject.getObject(2);
	           if(rs != null){
	           if(rs.next()){
	        	   objArry[0]=rs.getString("VIP_PRE_APRVL_LIMIT_YN");
	        	   
	        	   objArry[1]=rs.getString("NON_VIP_PRE_APRVL_LIMIT_YN");
	        	   
	        	   objArry[2]=rs.getString("VIP_MDT_SRVCE_PRE_APRVL_YN");
	        	   objArry[3]=rs.getBigDecimal("NON_VIP_PRE_APRVL_LIMIT");
	        	   objArry[4]=rs.getBigDecimal("VIP_PRE_APRVL_LIMIT");
	        	   objArry[5]=rs.getString("NON_VIP_MDT_SRVCE_PRE_APRVL_YN");
	           	}//end of if(rs.next()){
	           }//end of if(rs != null)
	       	return objArry;
	       }//end of try
	       catch (SQLException sqlExp)
	       {
	           throw new TTKException(sqlExp, "prodpolicyconfig");
	       }//end of catch (SQLException sqlExp)
	       catch (Exception exp)
	       {
	           throw new TTKException(exp, "prodpolicyconfig");
	       }//end of catch (Exception exp)
	       finally
			{
				 //Nested Try Catch to ensure resource closure 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PolicyDAOImpl getPreApprovedLimit",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyconfig");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getPreApprovedLimit",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyconfig");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getPreApprovedLimit(ProdPolicySeqID)",sqlExp);
								throw new TTKException(sqlExp, "prodpolicyconfig");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "prodpolicyconfig");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		 
	   }//getPreApprovedLimit
	 
	   public Object savePreApprovedLimit(Object[] fieldsData) throws TTKException{
		   Connection conn = null;
	       CallableStatement cStmtObject=null;
	       ResultSet rs = null;
	       try{
		       	conn = ResourceManager.getConnection();
		       	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePreApprovalLimit);
		          
		       	
		       	cStmtObject.setLong(1,(Long)fieldsData[0]);
		           cStmtObject.setString(2,(fieldsData[1]==null||"N".equals(fieldsData[1])?"N":(String)fieldsData[1]));
		           cStmtObject.setString(3,(String)fieldsData[2]);
		           
		           cStmtObject.setString(4,(fieldsData[3]==null||"N".equals(fieldsData[3])?"N":(String)fieldsData[3]));
		           cStmtObject.setString(5,(fieldsData[4]==null||"N".equals(fieldsData[4])?"N":(String)fieldsData[4]));
		           cStmtObject.setString(6,(String)fieldsData[5]);
		           
		           cStmtObject.setString(7,(fieldsData[6]==null||"N".equals(fieldsData[6])?"N":(String)fieldsData[6]));
		           
		           cStmtObject.setLong(8,(Long)fieldsData[7]);
		           cStmtObject.registerOutParameter(9,OracleTypes.NUMBER);
		           cStmtObject.execute();
		          Long rowPro = cStmtObject.getLong(9);
		          
		       	return rowPro;
		       
	       }//end of try
	       catch (SQLException sqlExp)
	       {
	           throw new TTKException(sqlExp, "prodpolicyconfig");
	       }//end of catch (SQLException sqlExp)
	       catch (Exception exp)
	       {
	           throw new TTKException(exp, "prodpolicyconfig");
	       }//end of catch (Exception exp)
	       finally
			{
				 //Nested Try Catch to ensure resource closure 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PolicyDAOImpl savePreApprovedLimit",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyconfig");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getShortfallDaysConfig()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyconfig");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl savePreApprovedLimit(ProdPolicySeqID)",sqlExp);
								throw new TTKException(sqlExp, "prodpolicyconfig");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "prodpolicyconfig");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		 
	   }//savePreApprovedLimit()
	  
    
    /**
     * This method returns the Arraylist of PolicyVO's  which contains the details of enrollment policy details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PolicyVO object which contains the details of enrollment policy details
     * @exception throws TTKException
     */
    public ArrayList getPolicyList(ArrayList alSearchCriteria) throws TTKException {
        Collection<Object> alResultList = new ArrayList<Object>();
        OracleCallableStatement oCstmt = null;
        PolicyVO policyVO = null;
        Connection conn = null;
        ResultSet rs = null;
        try{
            conn = ResourceManager.getConnection();
            oCstmt = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strPolicyList);
            oCstmt.setPlsqlIndexTable(1, alSearchCriteria.get(0), 7,7,OracleTypes.VARCHAR,250);
            oCstmt.setString(2,(String)alSearchCriteria.get(2));
            oCstmt.setString(3,(String)alSearchCriteria.get(3));
            oCstmt.setString(4 ,(String)alSearchCriteria.get(4));
            oCstmt.setString(5 ,(String)alSearchCriteria.get(5));
            oCstmt.setLong(6,(Long)alSearchCriteria.get(1));//USER_SEQ_ID
            oCstmt.registerOutParameter(7,OracleTypes.CURSOR);
            oCstmt.execute();  
            rs = (java.sql.ResultSet)oCstmt.getObject(7);
            if(rs != null){
            
                while (rs.next()) {
                    policyVO = new PolicyVO();
                    if(rs.getString("POLICY_SEQ_ID") != null){
                        policyVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
                    }//end of if(rs.getString("POLICY_SEQ_ID") != null)
                    policyVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
                    policyVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
                    policyVO.setEnrollmentType(TTKCommon.checkNull(rs.getString("ENROL_DESCRIPTION")));
                    policyVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));
                    policyVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
                    policyVO.setStatus(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    policyVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
                    if(rs.getString("PROD_POLICY_SEQ_ID") != null){
                        policyVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
                    }//end of if(rs.getString("PROD_POLICY_SEQ_ID") != null)
                    alResultList.add(policyVO);
                }//end of while(rs.next())
            }//end of if(rs != null) 
            return (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (oCstmt != null)	oCstmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getPolicyList()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getPolicyList()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				oCstmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPolicyList(ArrayList alSearchCriteria)    
    
    /**
     * This method returns the PolicyDetailVO, which contains all the policy details
     * @param lngProdPolicySeqID It contains the Product Policy Seq ID
     * @param lngUserSeqID contains which user has logged in 
     * @return PolicyDetailVO object which contains all the policy details
     * @exception throws TTKException 
     */
    public PolicyDetailVO getPolicyDetail(Long lngProdPolicySeqID,Long lngUserSeqID) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
        ResultSet rs = null;
        PolicyDetailVO policyDetailVO = new PolicyDetailVO();
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPolicyDetails);
            cStmtObject.setLong(1,lngProdPolicySeqID);
            cStmtObject.setLong(2,lngUserSeqID);
            cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(3);
            if(rs != null){
                while(rs.next()){
                	policyDetailVO.setBatchSeqID(rs.getString("ENROL_BATCH_SEQ_ID")!=null ? new Long(rs.getLong("ENROL_BATCH_SEQ_ID")):null);
                	policyDetailVO.setCompanyName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
                	policyDetailVO.setInsuranceSeqID(rs.getString("INS_SEQ_ID")!=null ? new Long(rs.getLong("INS_SEQ_ID")):null );
                	policyDetailVO.setOfficeCode(TTKCommon.checkNull(rs.getString("INS_COMP_CODE_NUMBER")));
                	policyDetailVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));
                	policyDetailVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
                	policyDetailVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
                	
                	policyDetailVO.setHealthAuthority(rs.getString("AUTHORITY_TYPE"));
                	policyDetailVO.setAuthorityProductCode(TTKCommon.checkNull(rs.getString("AUTH_PRODUCT_CODE"))); 
                	policyDetailVO.setEnrollmentType(TTKCommon.checkNull(rs.getString("ENROL_TYPE_ID")));
                	policyDetailVO.setEnrollmentDesc(TTKCommon.checkNull(rs.getString("ENROL_DESCRIPTION")));
                	policyDetailVO.setSubTypeID(TTKCommon.checkNull(rs.getString("POLICY_SUB_GENERAL_TYPE_ID")));
                	policyDetailVO.setPolicySubType(TTKCommon.checkNull(rs.getString("POLICY_SUB_TYPE")));
                	policyDetailVO.setStartDate(rs.getString("EFFECTIVE_FROM_DATE")!=null ? new Date(rs.getTimestamp("EFFECTIVE_FROM_DATE").getTime()):null);
                	policyDetailVO.setEndDate(rs.getString("EFFECTIVE_TO_DATE")!=null ? new Date(rs.getTimestamp("EFFECTIVE_TO_DATE").getTime()):null);
                	policyDetailVO.setIssueDate(rs.getString("POLICY_ISSUE_DATE")!=null ? new Date(rs.getTimestamp("POLICY_ISSUE_DATE").getTime()):null);
                	policyDetailVO.setProductSeqID(rs.getString("PRODUCT_SEQ_ID")!=null ? new Long(rs.getLong("PRODUCT_SEQ_ID")):null);
                	policyDetailVO.setProductName(TTKCommon.checkNull(rs.getString("PRODUCT_NAME")));
                	policyDetailVO.setPolicyStatusID(TTKCommon.checkNull(rs.getString("POLICY_STATUS_GENERAL_TYPE_ID")));
                	policyDetailVO.setPolicyStatus(TTKCommon.checkNull(rs.getString("POLICY_STATUS_DESCRIPTION")));
                    policyDetailVO.setAdminStatusID(TTKCommon.checkNull(rs.getString("ADMIN_STATUS_GENERAL_TYPE_ID")));
                	policyDetailVO.setRecdDate(rs.getString("POLICY_RCVD_DATE")!=null ? new Date(rs.getTimestamp("POLICY_RCVD_DATE").getTime()):null);
                	policyDetailVO.setOfficeSeqID(rs.getString("TPA_OFFICE_SEQ_ID")!=null ? new Long(rs.getLong("TPA_OFFICE_SEQ_ID")):null);
                	policyDetailVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
                	policyDetailVO.setGroupBranchSeqID(rs.getString("GROUP_BRANCH_SEQ_ID")!= null ? new Long(rs.getLong("GROUP_BRANCH_SEQ_ID")):null);
                	policyDetailVO.setBrokerID(rs.getString("BROKER_GROUP_ID")!= null ? new Long(rs.getLong("BROKER_GROUP_ID")):null);
                	

                	
                	policyDetailVO.setEnrollOnEmployeeNbr(TTKCommon.checkNull(rs.getString("GEN_ENRID_GENERAL_TYPE_ID")));
                	policyDetailVO.setCardClearance(rs.getString("CARD_CLEARANCE_DAYS")!=null ? new Integer(rs.getInt("CARD_CLEARANCE_DAYS")):null);
                    policyDetailVO.setClaimClearance(rs.getString("CLAIM_CLEARANCE_DAYS")!= null ? new Integer(rs.getInt("CLAIM_CLEARANCE_DAYS")):null);
                    policyDetailVO.setPreAuthClearance(rs.getString("PA_CLEARANCE_HOURS")!= null ? new Integer(rs.getInt("PA_CLEARANCE_HOURS")):null);
                    policyDetailVO.setIssueChqTypeID(TTKCommon.checkNull(rs.getString("TPA_CHEQUE_ISSUED_GENERAL_TYPE")));
                    policyDetailVO.setPolicySeqID(rs.getString("POLICY_SEQ_ID")!=null ? new Long(rs.getLong("POLICY_SEQ_ID")):null);
                    policyDetailVO.setGroupRegnSeqID(rs.getString("GROUP_REG_SEQ_ID")!=null ? new Long(rs.getLong("GROUP_REG_SEQ_ID")):null);
                    policyDetailVO.setBufferAllowedYN(TTKCommon.checkNull(rs.getString("BUFFER_ALLOWED_YN")));
                    policyDetailVO.setAllocatedTypeID(TTKCommon.checkNull(rs.getString("BUFFER_ALLOC_GENERAL_TYPE_ID")));
                    policyDetailVO.setAdmnAuthorityTypeID(TTKCommon.checkNull(rs.getString("ADMIN_AUTH_GENERAL_TYPE_ID")));
                    policyDetailVO.setDMSRefID(TTKCommon.checkNull(rs.getString("DMS_REFERENCE_ID")));
                    policyDetailVO.setTemplateID(rs.getString("TEMPLATE_ID")!=null ? new Long(rs.getLong("TEMPLATE_ID")):null);
                    policyDetailVO.setDischargeVoucherMandatoryYN(TTKCommon.checkNull(rs.getString("DV_REQD_GENERAL_TYPE_ID")));
                    policyDetailVO.setTenureDays(rs.getString("TENURE")!= null ? new Integer(rs.getInt("TENURE")):null);
                    policyDetailVO.setGrpID(TTKCommon.checkNull(rs.getString("GRP_ID")));
                    policyDetailVO.setStopPreAuthsYN(TTKCommon.checkNull(rs.getString("STOP_PREAUTH_YN")));
                    policyDetailVO.setStopClaimsYN(TTKCommon.checkNull(rs.getString("STOP_CLAIM_YN")));
					policyDetailVO.setCashBenefitYN(TTKCommon.checkNull(rs.getString("V_CBH_SUMINS_YN")));//KOC 1270 for hospital cash benefit
                    policyDetailVO.setConvCashBenefitYN(TTKCommon.checkNull(rs.getString("V_CONV_SUMINS_YN")));//KOC 1270 for hospital cash benefit
                    //policyDetailVO.setBufferRecYN(TTKCommon.checkNull(rs.getString("BUFFER_REC_YN")));
                      //Changes as per KOC 1216B Change request
					policyDetailVO.setMemberBufferYN(TTKCommon.checkNull(rs.getString("MEMBER_BUFFER_YN")));
                	policyDetailVO.setopdClaimsYN(TTKCommon.checkNull(rs.getString("OPD_BENEFITS_YN")));//KOC 1286 for OPD Benefit
  
                   
                  //added for KOC-1273
                  
                  policyDetailVO.setCriticalBenefitYN(TTKCommon.checkNull(rs.getString("Critical_Sumins_Yn")));
                  
                  policyDetailVO.setSurvivalPeriodYN(TTKCommon.checkNull(rs.getString("SURVIVAL_PERIOD_YN")));
				  policyDetailVO.setPatEnableYN(TTKCommon.checkNull(rs.getString("PAT_ALLOWED_YN")));//1274A
				  policyDetailVO.setPatMailTo(TTKCommon.checkNull(rs.getString("PAT_TO_MAIL_ID")));
				  policyDetailVO.setPatMailCC(TTKCommon.checkNull(rs.getString("PAT_CC_MAIL_ID")));
				  policyDetailVO.setClmEnableYN(TTKCommon.checkNull(rs.getString("CLM_ALLOWED_YN")));
				  policyDetailVO.setClmMailTo(TTKCommon.checkNull(rs.getString("CLM_TO_MAIL_ID")));
				  policyDetailVO.setClmMailCC(TTKCommon.checkNull(rs.getString("CLM_CC_MAIL_ID")));//1274A
				  policyDetailVO.setHealthCheckType(TTKCommon.checkNull(rs.getString("hcu_pay_to_general_type")));//OPD_4_hosptial
				  policyDetailVO.setPaymentTo(TTKCommon.checkNull(rs.getString("tpa_name")));//OPD_4_hosptial
                  policyDetailVO.setBrokerYN(TTKCommon.checkNull(rs.getString("broker_yn")));
                  policyDetailVO.setHrAppYN(TTKCommon.checkNull(rs.getString("HR_APPR_REQUIRED_YN")));//OPD_4_hosptial  
                  
                  policyDetailVO.setCoInsurance(TTKCommon.checkNull(rs.getInt("CO_INS")).toString());
                  policyDetailVO.setDeductable(TTKCommon.checkNull(rs.getString("DEDUCTIBLE")));
                  policyDetailVO.setClassRoomType(TTKCommon.checkNull(rs.getString("CLASS")));
                  policyDetailVO.setPlanType(TTKCommon.checkNull(rs.getString("PLAN")));
                  policyDetailVO.setMaternityYN(TTKCommon.checkNull(rs.getString("MATERNITY_YN")));
                  policyDetailVO.setMaternityCopay(TTKCommon.checkNull(rs.getString("MATERNITY_COPAY")));
                  policyDetailVO.setOpticalYN(TTKCommon.checkNull(rs.getString("OPTICAL_YN")));
                  policyDetailVO.setOpticalCopay(TTKCommon.checkNull(rs.getString("OPTICAL_COPAY")));
                  policyDetailVO.setDentalYN(TTKCommon.checkNull(rs.getString("DENTAL_YN")));
                  policyDetailVO.setDentalCopay(TTKCommon.checkNull(rs.getString("DENTAL_COPAY")));
                  policyDetailVO.setEligibility(TTKCommon.checkNull(rs.getString("ELIGIBILITY")));
                  policyDetailVO.setIpopServices(TTKCommon.checkNull(rs.getString("IP_OP_SERVICES")));
                  policyDetailVO.setPharmaceutical(TTKCommon.checkNull(rs.getString("PHARMACEUTICALS")));
                  policyDetailVO.setOutpatientbenfTypeCond(rs.getString("opts_pat_limit"));
                  if(rs.getDate("stop_preauth_date") != null){
                  policyDetailVO.setStopCashlessDate(rs.getDate("stop_preauth_date"));
                  }
                  if(rs.getDate("stop_clm_date") !=null){
                  policyDetailVO.setStopClaimsDate(rs.getDate("stop_clm_date"));
                  }
				 policyDetailVO.setMailNotificationhiddenYN(TTKCommon.checkNull(rs.getString("SEND_BDAYWISH_YN")));
				 policyDetailVO.setNetworkSortNum(TTKCommon.checkNull(rs.getString("NETWORK_SORT_NO")));
				 policyDetailVO.setBenefitTypeProvided(TTKCommon.checkNull(rs.getString("benefit_type")));
                 policyDetailVO.setTreatyType(rs.getString("TREATY_TYPE"));
                 policyDetailVO.setReinsurerId(TTKCommon.checkNull(rs.getString("REINS_SEQ_ID")));
			}//end of while(rs.next())
          }//end of if(rs != null)
            return policyDetailVO;
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getPolicyDetail()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getPolicyDetail()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getPolicyDetail()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }// End of getPolicyDetail(Long lngProdPolicySeqID,Long lngUserSeqID)
    
    /**
     * This method returns the ArrayList, which contains Groups corresponding to TTK Branch
     * @param lngOfficeSeqID It contains the TTK Branch
     * @return ArrayList object which contains Groups corresponding to TTK Branch
     * @exception throws TTKException 
     */
    public ArrayList getGroup(long lngOfficeSeqID) throws TTKException{
    	Connection conn = null;
    	ResultSet rs = null;
        ArrayList<Object> alGroup = new ArrayList<Object>();
        CacheObject cacheObject = null;
        PreparedStatement pStmt = null;
        try{
            conn = ResourceManager.getConnection();
            pStmt=conn.prepareStatement(strGroup);
            pStmt.setLong(1,lngOfficeSeqID);
            rs = pStmt.executeQuery();
            if(rs != null){
                while(rs.next()){
                    cacheObject = new CacheObject();
                    cacheObject.setCacheId((rs.getString("GROUP_BRANCH_SEQ_ID")));
                    cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
                    alGroup.add(cacheObject);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alGroup;
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getGroup()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getGroup()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getGroup()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getGroup(long lngOfficeSeqID)
    
    public ArrayList getBrokerGroup(long lngOfficeSeqID) throws TTKException{
    	Connection conn = null;
    	ResultSet rs = null;
        ArrayList<Object> alGroup = new ArrayList<Object>();
        CacheObject cacheObject = null;
        PreparedStatement pStmt = null;
        try{
            conn = ResourceManager.getConnection();
            pStmt=conn.prepareStatement(strBroker_Group);
            pStmt.setLong(1,lngOfficeSeqID);
            rs = pStmt.executeQuery();
            if(rs != null){
                while(rs.next()){
                    cacheObject = new CacheObject();
                    cacheObject.setCacheId((rs.getString("GROUP_BRANCH_SEQ_ID")));
                    cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
                    alGroup.add(cacheObject);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alGroup;
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getBrokerGroup()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getBrokerGroup()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getBrokerGroup()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getBrokerGroup(long lngOfficeSeqID)
       //ended
    /**
     * This method saves the Enrollment Policy information
     * @param policyDetailVO the object which contains the Policy Details which has to be  saved
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
    public int savePolicy(PolicyDetailVO policyDetailVO) throws TTKException {
        int iResult = 0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePolicy);
            
            if(policyDetailVO.getPolicySeqID() != null){
                cStmtObject.setLong(POLICY_SEQ_ID,policyDetailVO.getPolicySeqID());
            }//end of if(policyDetailVO.getPolicySeqID() != null)
            else{
                cStmtObject.setString(POLICY_SEQ_ID,null);
            }//end of else
            
            if(policyDetailVO.getGroupBranchSeqID() != null){
                cStmtObject.setLong(GROUP_BRANCH_SEQ_ID,policyDetailVO.getGroupBranchSeqID());
            }//end of if(policyDetailVO.getGroupBranchSeqID() != null)
            else{
                cStmtObject.setString(GROUP_BRANCH_SEQ_ID,null);
            }//end of else

            cStmtObject.setString(GEN_ENRID_GENERAL_TYPE_ID,policyDetailVO.getEnrollOnEmployeeNbr());
            cStmtObject.setString(CHEQUE_ISSUED_GENERAL_TYPE,policyDetailVO.getIssueChqTypeID());
            
            if(policyDetailVO.getCardClearance() != null){
                cStmtObject.setInt(CARD_CLEARANCE_DAYS,policyDetailVO.getCardClearance());
            }//end of if(policyDetailVO.getCardClearance() != null)
            else{
                cStmtObject.setString(CARD_CLEARANCE_DAYS,null);
            }//end of else
            
            if(policyDetailVO.getClaimClearance() != null){
                cStmtObject.setInt(CLAIM_CLEARANCE_DAYS,policyDetailVO.getClaimClearance());
            }//end of if(policyDetailVO.getClaimClearance() != null)
            else{
                cStmtObject.setString(CLAIM_CLEARANCE_DAYS,null);
            }//end of else
            
            if(policyDetailVO.getPreAuthClearance() != null){
                cStmtObject.setInt(PA_CLEARANCE_HOURS,policyDetailVO.getPreAuthClearance());
            }//end of if(policyDetailVO.getPreAuthClearance() != null)
            else{
                cStmtObject.setString(PA_CLEARANCE_HOURS,null);
            }//end of else
            
            cStmtObject.setString(ADMIN_STATUS_GENERAL_TYPE_ID,policyDetailVO.getAdminStatusID());
            cStmtObject.setString(BUFFER_ALLOWED_YN,policyDetailVO.getBufferAllowedYN());
            cStmtObject.setString(ADMIN_AUTH_GENERAL_TYPE_ID,policyDetailVO.getAdmnAuthorityTypeID());
            cStmtObject.setString(BUFFER_ALLOC_GENERAL_TYPE_ID,policyDetailVO.getAllocatedTypeID());	
            
            if(policyDetailVO.getTemplateID() != null){
            	cStmtObject.setLong(TEMPLATE_ID,policyDetailVO.getTemplateID());
            }//end of if(policyDetailVO.getTemplateID() != null) 
            else{
            	cStmtObject.setString(TEMPLATE_ID,null);
            }//end of else
            cStmtObject.setString(DISCHARGE_VOUCH_MANDATORY_YN,policyDetailVO.getDischargeVoucherMandatoryYN());
            
            if(policyDetailVO.getTenureDays() != null){
            	cStmtObject.setInt(TENURE,policyDetailVO.getTenureDays());
            }//end of if(policyDetailVO.getTenureDays() != null)
            else{
            	cStmtObject.setString(TENURE,null);
            }//end of else
            
            cStmtObject.setString(STOP_PREAUTH_YN,policyDetailVO.getStopPreAuthsYN());
			cStmtObject.setString(STOP_CLAIM_YN,policyDetailVO.getStopClaimsYN());
            cStmtObject.setLong(USER_SEQ_ID,policyDetailVO.getUpdatedBy());
		//Changes as per IBM Change request 1216B CR
            cStmtObject.setString(MEMBER_BUFFER_YN,policyDetailVO.getMemberBufferYN());
            cStmtObject.setString(OPD_BENEFITS_YN,policyDetailVO.getopdClaimsYN());//KOC 1286 for OPD Benefit
            cStmtObject.setString(V_CBH_SUMINS_YN,policyDetailVO.getCashBenefitYN());// KOC 1270 for hospital cash benefit
            cStmtObject.setString(V_CONV_SUMINS_YN,policyDetailVO.getConvCashBenefitYN());// KOC 1270 for hospital cash benefit
            //added for KOC-1273
            cStmtObject.setString(CRITICAL_BENEFIT,policyDetailVO.getCriticalBenefitYN());
            cStmtObject.setString(SURVIVAL_PERIOD,policyDetailVO.getSurvivalPeriodYN());
			
            cStmtObject.setString(auth_product_code,policyDetailVO.getAuthorityProductCode());		
           	cStmtObject.setString(Pat_Enable_YN,policyDetailVO.getPatEnableYN());
			cStmtObject.setString(Clm_Enable_YN,policyDetailVO.getClmEnableYN());
			cStmtObject.setString(Pat_Mail_To,policyDetailVO.getPatMailTo());
			cStmtObject.setString(Pat_Mail_CC,policyDetailVO.getPatMailCC());
			cStmtObject.setString(Clm_Mail_To,policyDetailVO.getClmMailTo());
			cStmtObject.setString(Clm_Mail_CC,policyDetailVO.getClmMailCC());
			cStmtObject.setString(hcu_pay_to_general_type, policyDetailVO.getHealthCheckType());//OPD_4_hosptial
			cStmtObject.setString(tpa_name, policyDetailVO.getPaymentTo());//OPD_4_hosptial
            if(policyDetailVO.getBrokerID() != null){
                cStmtObject.setLong(BROKER_GROUP_ID,policyDetailVO.getBrokerID());
            }//end of if(policyDetailVO.getGroupBranchSeqID() != null)
            else{
                cStmtObject.setString(BROKER_GROUP_ID,null);
            }//end of else
            cStmtObject.setString(BROKER_YN,policyDetailVO.getBrokerYN());
            cStmtObject.setString(HR_APPR_REQUIRED_YN, policyDetailVO.getHrAppYN());//OPD_4_hosptial
            
            cStmtObject.setString(CO_INS,policyDetailVO.getCoInsurance());
			 cStmtObject.setString(DEDUCTIBLE,policyDetailVO.getDeductable());
			 cStmtObject.setString(CLASS_ROOM_TYPE,policyDetailVO.getClassRoomType());
			 cStmtObject.setString(PLAN_TYPE,policyDetailVO.getPlanType());
			 cStmtObject.setString(MATERNITY_YN,policyDetailVO.getMaternityYN());
			 cStmtObject.setString(MATERNITY_COPAY,policyDetailVO.getMaternityCopay());
			 cStmtObject.setString(OPTICAL_YN,policyDetailVO.getOpticalYN());
			 cStmtObject.setString(OPTICAL_COPAY,policyDetailVO.getOpticalCopay());
			 cStmtObject.setString(DENTAL_YN,policyDetailVO.getDentalYN());
			 cStmtObject.setString(DENTAL_COPAY,policyDetailVO.getDentalCopay());
			 cStmtObject.setString(ELIGIBILITY,policyDetailVO.getEligibility());
			 cStmtObject.setString(IP_OP_SERVICES,policyDetailVO.getIpopServices());
			 cStmtObject.setString(PHARMACEUTICALS,policyDetailVO.getPharmaceutical());
			 cStmtObject.setString(OPTS_PAT_LIMIT, policyDetailVO.getOutpatientbenfTypeCond());
			 if(policyDetailVO.getStopCashlessDate() != null){
				 cStmtObject.setTimestamp(STOPCASHLESS, new Timestamp(policyDetailVO.getStopCashlessDate().getTime()));
				 }else{
					 cStmtObject.setTimestamp(STOPCASHLESS, null); 
				 }
				 if(policyDetailVO.getStopClaimsDate() != null){
				 cStmtObject.setTimestamp(STOPCLAIMS,new Timestamp(policyDetailVO.getStopClaimsDate().getTime()));
				 }else{
					 cStmtObject.setTimestamp(STOPCLAIMS,null);	 
				 }
			cStmtObject.setString(MAIL_NOTIFICATION_FLAG, policyDetailVO.getMailNotificationhiddenYN());
            cStmtObject.setString(TRITY_TYPE_ID, policyDetailVO.getTreatyType());
		    cStmtObject.setString(REINSURER, policyDetailVO.getReinsurerId());
            cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);//ROW_PROCESSED
            cStmtObject.execute(); 
            iResult = cStmtObject.getInt(ROW_PROCESSED);//ROW_PROCESSED
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
        			log.error("Error while closing the Statement in PolicyDAOImpl savePolicy()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PolicyDAOImpl savePolicy()",sqlExp);
        				throw new TTKException(sqlExp, "policy");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "policy");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//end of savePolicy(PolicyDetailVO policyDetailVO)
    
    /**
     * This method returns the ArrayList, which contains the BufferVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of BufferVO'S object's which contains the Buffer details
     * @exception throws TTKException
     */
    public ArrayList getBufferList(ArrayList alSearchObjects) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		BufferVO bufferVO = null;
		
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strBufferList);
			
			if(alSearchObjects.get(0) != null){
				cStmtObject.setLong(1,(Long)alSearchObjects.get(0));
			}//end of if(alSearchObjects.get(0) != null)
			else{
				cStmtObject.setString(1,null);
			}//end of else
			
			cStmtObject.setString(2,(String)alSearchObjects.get(2));
			cStmtObject.setString(3,(String)alSearchObjects.get(3));
			cStmtObject.setString(4,(String)alSearchObjects.get(4));
			cStmtObject.setString(5,(String)alSearchObjects.get(5));
			cStmtObject.setLong(6,(Long)alSearchObjects.get(1));
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();  
			rs = (java.sql.ResultSet)cStmtObject.getObject(7);
			if(rs != null){
				while(rs.next()){
					bufferVO = new BufferVO();
					
					if(rs.getString("BUFFER_SEQ_ID") != null){
						bufferVO.setBufferSeqID(new Long(rs.getLong("BUFFER_SEQ_ID")));
					}//end of if(rs.getString("BUFFER_SEQ_ID") != null)
					
					if(rs.getString("POLICY_SEQ_ID") != null){
						bufferVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("POLICY_SEQ_ID") != null)
					
					bufferVO.setRefNbr(TTKCommon.checkNull(rs.getString("REFERENCE_NO")));
					
					if(rs.getString("BUFFER_ADDED_DATE") != null){
						bufferVO.setBufferDate(new Date(rs.getTimestamp("BUFFER_ADDED_DATE").getTime()));
					}//end of if(rs.getString("BUFFER_ADDED_DATE") != null)
					
					if(rs.getString("BUFFER_AMOUNT") != null){
						bufferVO.setBufferAmt(new BigDecimal(rs.getString("BUFFER_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					else{
						bufferVO.setBufferAmt(new BigDecimal("0.00"));
					} //<!-- added for hyundai buffer by rekha on 19.06.2014 -->
					
					if(rs.getString("MED_BUFFER_AMOUNT") != null){
						bufferVO.setNormMedAmt(new BigDecimal(rs.getString("MED_BUFFER_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					
					else{
						bufferVO.setNormMedAmt(new BigDecimal("0.00"));
					} //<!-- added for hyundai buffer by rekha on 19.06.2014 -->
					
			
					if(rs.getString("CRITICAL_CORP_BUFF_AMOUNT") != null){
						bufferVO.setCritiCorpAmt(new BigDecimal(rs.getString("CRITICAL_CORP_BUFF_AMOUNT")));
					}
					
					else{
						bufferVO.setCritiCorpAmt(new BigDecimal("0.00"));
					} //<!-- added for hyundai buffer by rekha on 19.06.2014 -->
					
					
					
					if(rs.getString("CRITICAL_MED_BUFF_AMOUNT") != null){
						bufferVO.setCritiMedAmt(new BigDecimal(rs.getString("CRITICAL_MED_BUFF_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					else{
						bufferVO.setCritiMedAmt(new BigDecimal("0.00"));
					} 
					
					if(rs.getString("CRITICAL_BUFF_AMOUNT") != null){
						bufferVO.setCriIllBufferAmt(new BigDecimal(rs.getString("CRITICAL_BUFF_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					else{
						bufferVO.setCriIllBufferAmt(new BigDecimal("0.00"));
					} 
				
					
					 //<!-- end added for hyundai buffer by rekha on 19.06.2014 -->
					
					bufferVO.setModeDesc(TTKCommon.checkNull(rs.getString("BUFFER_MODE")));
					
					if(rs.getString("TOTAL_BUFFER_AMOUNT") != null){
						bufferVO.setTotBufferAmt(new BigDecimal(rs.getString("TOTAL_BUFFER_AMOUNT")));
					}//end of if(rs.getString("TOTAL_BUFFER_AMOUNT") != null)
					if(rs.getString("TOT_MED_BUFF_AMOUNT") != null){
						bufferVO.setTotNorMedBufferAmt(new BigDecimal(rs.getString("TOT_MED_BUFF_AMOUNT")));
					}//end of if(rs.getString("TOTAL_BUFFER_AMOUNT") != null)
					if(rs.getString("TOT_CRIT_CORP_BUFF_AMOUNT") != null){
						bufferVO.setTotCriCorBufferAmt(new BigDecimal(rs.getString("TOT_CRIT_CORP_BUFF_AMOUNT")));
					}//end of if(rs.getString("TOTAL_BUFFER_AMOUNT") != null)
					if(rs.getString("TOT_CRIT_MED_BUFF_AMOUNT") != null){
						bufferVO.setTotCriMedBufferAmt(new BigDecimal(rs.getString("TOT_CRIT_MED_BUFF_AMOUNT")));
					}//end of if(rs.getString("TOTAL_BUFFER_AMOUNT") != null)
					if(rs.getString("TOT_CRIT_BUFF_AMOUNT") != null){
						bufferVO.setTotCriIllnessBufferAmt(new BigDecimal(rs.getString("TOT_CRIT_BUFF_AMOUNT")));
					}//end of if(rs.getString("TOTAL_BUFFER_AMOUNT") != null)
					
					alResultList.add(bufferVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp) 
		{
			throw new TTKException(sqlExp, "policy");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp) 
		{
			throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getBufferList()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getBufferList()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getBufferList()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getBufferList(ArrayList alSearchObjects)
    
    /**
     * This method returns the Object[], which contains all the Policy Buffer Admin Authority Details
     * @param lngProdPolicySeqID long value contains Product Policy Seq ID 
     * @return Object[] object which contains all the Policy Buffer Admin Authority Details
     * @exception throws TTKException
     */
    public Object[] getBufferAuthority(long lngProdPolicySeqID) throws TTKException{
    	Object[] objArrayResult = new Object[2];
    	String strAdminAuthority = "";
    	String strBufferAllocType = "";
    	Connection conn = null;
    	PreparedStatement pStmt = null;
    	ResultSet rs = null;
    	
    	try{
    		conn = ResourceManager.getConnection();
            pStmt=conn.prepareStatement(strGetBufferDetailAuthority);
            pStmt.setLong(1,lngProdPolicySeqID);
            rs = pStmt.executeQuery();
            if(rs != null){
            	while(rs.next()){
            		strAdminAuthority = TTKCommon.checkNull(rs.getString("ADMIN_AUTHORITY"));
            		strBufferAllocType = TTKCommon.checkNull(rs.getString("BUFFER_ALLOCATION_TYPE"));
            		objArrayResult[0] = strAdminAuthority;
        			objArrayResult[1] = strBufferAllocType;
            	}//end of while(rs.next())
            }//end of if(rs != null)
            return objArrayResult;
    	}//end of try
    	catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getBufferAuthority()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null) pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getBufferAuthority()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getBufferAuthority()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getBufferAuthority(long lngProdPolicySeqID)
    
    /**
     * This method adds/updates the BufferDetailVO which contains Buffer details
     * @param bufferDetailVO the details which has to be added or updated
     * @return long value, contains Buffer Seq ID
     * @exception throws TTKException
     */
    public long saveBuffer(BufferDetailVO bufferDetailVO) throws TTKException {
    	long lngBufferSeqID = 0;
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ArrayList allOutParam=new ArrayList();
    	try{
    		conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveBuffer);
            
            if(bufferDetailVO.getBufferSeqID() != null){
            	cStmtObject.setLong(1,bufferDetailVO.getBufferSeqID());
            }//end of if(bufferDetailVO.getBufferSeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else
            
            if(bufferDetailVO.getProdPolicySeqID() != null){
            	cStmtObject.setLong(2,bufferDetailVO.getProdPolicySeqID());
            }//end of if(bufferDetailVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setString(2,null);
            }//end of else
            
            if(bufferDetailVO.getBufferDate() != null){
            	cStmtObject.setTimestamp(3,new Timestamp(bufferDetailVO.getBufferDate().getTime()));
            }//end of if(bufferDetailVO.getBufferDate() != null)
            else{
            	cStmtObject.setTimestamp(3,null);
            }//end of else
            
            cStmtObject.setString(4,bufferDetailVO.getModeTypeID());
            
            if(bufferDetailVO.getBufferAmt() != null){
            	cStmtObject.setBigDecimal(5,bufferDetailVO.getBufferAmt());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(5,null);
            }//end of else
            
            cStmtObject.setString(6,bufferDetailVO.getRefNbr());
            cStmtObject.setString(7,bufferDetailVO.getRemarks());
            if(bufferDetailVO.getAllocatedAmt() != null){
            	cStmtObject.setBigDecimal(8,bufferDetailVO.getAllocatedAmt());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(8,null);
            }//end of else
            
           
          //<!added for hyundai buffer by rekha on 19.06.2014 -->
            if(bufferDetailVO.getNormMedAmt() != null){
            	cStmtObject.setBigDecimal(9,bufferDetailVO.getNormMedAmt());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(9,null);
            }//end of else
            if(bufferDetailVO.getCriIllBufferAmt() != null){
            	cStmtObject.setBigDecimal(10,bufferDetailVO.getCriIllBufferAmt());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(10,null);
            }//end of else
            
            if(bufferDetailVO.getCritiCorpAmt() != null){
            	cStmtObject.setBigDecimal(11,bufferDetailVO.getCritiCorpAmt());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(11,null);
            }//end of else
            
            if(bufferDetailVO.getCritiMedAmt() != null){
            	cStmtObject.setBigDecimal(12,bufferDetailVO.getCritiMedAmt());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(12,null);
            }//end of else
           
            
            if(bufferDetailVO.getNrmMedLimit() != null){
            	cStmtObject.setBigDecimal(13,bufferDetailVO.getNrmMedLimit());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(13,null);
            }//end of else
            
            
            if(bufferDetailVO.getCriIllBufferLimit()!= null){
            	cStmtObject.setBigDecimal(14,bufferDetailVO.getCriIllBufferLimit());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(14,null);
            }//end of else
            
            
            if(bufferDetailVO.getCriCorpLimit() != null){
            	cStmtObject.setBigDecimal(15,bufferDetailVO.getCriCorpLimit());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(15,null);
            }//end of else
            
            
            if(bufferDetailVO.getCriMedLimit() != null){
            	cStmtObject.setBigDecimal(16,bufferDetailVO.getCriMedLimit());
            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
            else{
            	cStmtObject.setString(16,null);
            }//end of else
            
            
          //<!-end - added for hyundai buffer by rekha on 19.06.2014 -->
            cStmtObject.setLong(17,bufferDetailVO.getUpdatedBy());
            cStmtObject.registerOutParameter(18,Types.VARCHAR); 
            cStmtObject.registerOutParameter(19,Types.INTEGER);
            cStmtObject.registerOutParameter(1,Types.BIGINT);
            cStmtObject.execute(); 
            lngBufferSeqID = cStmtObject.getLong(1);
            
            String alertMsg = (cStmtObject.getString(4)!=null)?cStmtObject.getString(4):"";
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
            
        }//end of try
    	catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
        			log.error("Error while closing the Statement in PolicyDAOImpl saveBuffer()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PolicyDAOImpl saveBuffer()",sqlExp);
        				throw new TTKException(sqlExp, "policy");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "policy");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return lngBufferSeqID;
    }//end of saveBuffer(BufferDetailVO bufferDetailVO)
    
    /**
	 * This method returns the BufferDetailVO which contains Descriptions of AdminAuthority and Allocated Type for saving Buffer Details
	 * @param lngProdPolicySeqID Product Policy Seq ID 
	 * @return BufferDetailVO which contains Descriptions of AdminAuthority and Allocated Type for saving Buffer Details
	 * @exception throws TTKException
	 */
	public BufferDetailVO getBufferDescription(Long lngProdPolicySeqID) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		BufferDetailVO bufferDetailVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetBufferDescription);
			cStmtObject.setLong(1,lngProdPolicySeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					bufferDetailVO = new BufferDetailVO();
					bufferDetailVO.setAdmnAuthTypeID(TTKCommon.checkNull(rs.getString("ADMIN_AUTH_GENERAL_TYPE_ID")));
					bufferDetailVO.setAdmnAuthDesc(TTKCommon.checkNull(rs.getString("AUTHORITY")));
					bufferDetailVO.setAllocatedTypeID(TTKCommon.checkNull(rs.getString("BUFFER_ALLOC_GENERAL_TYPE_ID")));
					bufferDetailVO.setAllocatedTypeDesc(TTKCommon.checkNull(rs.getString("ALLOCATION_DESCR")));
				}//end of while(rs.next())
			}//end of if(rs != null)
			return bufferDetailVO;
		}//end of try
		catch (SQLException sqlExp) 
		{
			throw new TTKException(sqlExp, "policy");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp) 
		{
			throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getBufferDescription()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getBufferDescription()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getBufferDescription()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getBufferDescription(Long lngProdPolicySeqID)
    
    /**
	 * This method returns the BufferDetailVO which contains Buffer Details
	 * @param ArrayList alBufferDetailParam
	 * @return BufferDetailVO which contains Buffer Details
	 * @exception throws TTKException
	 */
	public BufferDetailVO getBufferDetail(ArrayList alBufferDetailParam) throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		BufferDetailVO bufferDetailVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetBufferDetail);
			log.debug("buffer seq id is " + alBufferDetailParam.get(0));
			log.debug("policy seq id is " + alBufferDetailParam.get(1));
			log.debug("added by " + alBufferDetailParam.get(2));
			cStmtObject.setLong(1,(Long)alBufferDetailParam.get(0));//buffer_seq_id
			if(alBufferDetailParam.get(1)!=null){
				cStmtObject.setLong(2,(Long)alBufferDetailParam.get(1));//policy_seq_id
			}//end of if(alBufferDetailParam.get(1)!=null)
			else {
				cStmtObject.setString(2,null);
			}//end of else			
			cStmtObject.setLong(3,(Long)alBufferDetailParam.get(2));//added_by
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			
			rs = (java.sql.ResultSet)cStmtObject.getObject(4);
			if(rs != null){
				while(rs.next()){
					bufferDetailVO = new BufferDetailVO();
					
					if(rs.getString("BUFFER_SEQ_ID") != null){
						bufferDetailVO.setBufferSeqID(new Long(rs.getLong("BUFFER_SEQ_ID")));
					}//end of if(rs.getString("BUFFER_SEQ_ID") != null)
					if(rs.getString("POLICY_SEQ_ID") != null){
						bufferDetailVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
					}//end of if(rs.getString("BUFFER_SEQ_ID") != null)
					bufferDetailVO.setRefNbr(TTKCommon.checkNull(rs.getString("REFERENCE_NO")));
					
					if(rs.getString("BUFFER_ADDED_DATE") != null){
						bufferDetailVO.setBufferDate(new Date(rs.getTimestamp("BUFFER_ADDED_DATE").getTime()));
					}//end of if(rs.getString("BUFFER_ADDED_DATE") != null)
					
					bufferDetailVO.setAdmnAuthTypeID(TTKCommon.checkNull(rs.getString("ADMIN_AUTH_GENERAL_TYPE_ID")));
					bufferDetailVO.setAdmnAuthDesc(TTKCommon.checkNull(rs.getString("AUTHORITY")));
					bufferDetailVO.setAllocatedTypeID(TTKCommon.checkNull(rs.getString("BUFFER_ALLOC_GENERAL_TYPE_ID")));
					bufferDetailVO.setAllocatedTypeDesc(TTKCommon.checkNull(rs.getString("ALLOCATION_DESCR")));
					bufferDetailVO.setModeTypeID(TTKCommon.checkNull(rs.getString("BUFFER_MODE_GENERAL_TYPE_ID")));
					
					if(rs.getString("BUFFER_AMOUNT") != null){
						bufferDetailVO.setBufferAmt(new BigDecimal(rs.getString("BUFFER_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					
					if(rs.getString("BUFFER_ALLOC_AMOUNT") != null){
						bufferDetailVO.setAllocatedAmt(new BigDecimal(rs.getString("BUFFER_ALLOC_AMOUNT")));
					}//end of if(rs.getString("BUFFER_ALLOC_AMOUNT") != null)
					
					bufferDetailVO.setRemarks(TTKCommon.checkNull(rs.getString("REMARKS")));
					bufferDetailVO.setEditYN(TTKCommon.checkNull(rs.getString("EDIT_YN")));
					//<!- - added for hyundai buffer by rekha on 19.06.2014 -->

					if(rs.getString("MED_BUFFER_AMOUNT") != null){
						bufferDetailVO.setNormMedAmt(new BigDecimal(rs.getString("MED_BUFFER_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					
					if(rs.getString("MED_BUFF_ALLOC_AMOUNT") != null){
						bufferDetailVO.setNrmMedLimit(new BigDecimal(rs.getString("MED_BUFF_ALLOC_AMOUNT")));
					}//end of if(rs.getString("BUFFER_ALLOC_AMOUNT") != null)
					
					if(rs.getString("CRITICAL_CORP_BUFF_AMOUNT") != null){
						bufferDetailVO.setCritiCorpAmt(new BigDecimal(rs.getString("CRITICAL_CORP_BUFF_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					
					if(rs.getString("CRITICAL_CORP_ALLOC_AMOUNT") != null){
						bufferDetailVO.setCriCorpLimit(new BigDecimal(rs.getString("CRITICAL_CORP_ALLOC_AMOUNT")));
					}//end of if(rs.getString("BUFFER_ALLOC_AMOUNT") != null)
					
					
					if(rs.getString("CRITICAL_MED_BUFF_AMOUNT") != null){
						bufferDetailVO.setCritiMedAmt(new BigDecimal(rs.getString("CRITICAL_MED_BUFF_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					
					if(rs.getString("CRITICAL_MED_ALLOC_AMOUNT") != null){
						bufferDetailVO.setCriMedLimit(new BigDecimal(rs.getString("CRITICAL_MED_ALLOC_AMOUNT")));
					}//end of if(rs.getString("BUFFER_ALLOC_AMOUNT") != null)
					
					if(rs.getString("CRITICAL_BUFF_AMOUNT") != null){
						bufferDetailVO.setCriIllBufferAmt(new BigDecimal(rs.getString("CRITICAL_BUFF_AMOUNT")));
					}//end of if(rs.getString("BUFFER_AMOUNT") != null)
					
					if(rs.getString("CRITICAL_BUFF_ALLOC_AMOUNT") != null){
						bufferDetailVO.setCriIllBufferLimit(new BigDecimal(rs.getString("CRITICAL_BUFF_ALLOC_AMOUNT")));
					}//end of if(rs.getString("BUFFER_ALLOC_AMOUNT") != null)
					
					
					
					//<!-end - added for hyundai buffer by rekha on 19.06.2014 -->
				}//end of while(rs.next())
			}//end of if(rs != null)
			return bufferDetailVO;
		}//end of try
		catch (SQLException sqlExp) 
		{
			throw new TTKException(sqlExp, "policy");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp) 
		{
			throw new TTKException(exp, "policy");
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getBufferDetail()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getBufferDetail()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getBufferDetail()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "policy");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getBufferDetail(Long lngBufferSeqID,Long lngUserSeqID)
	
	/**
	 * This method returns the CustConfigMsgVO which contains configuration Details
	 * @param long lngProdPolicySeqId
	 * @return CustConfigMsgVO which contains configuration Details
	 * @exception throws TTKException
	 */
	public CustConfigMsgVO getCustMsgInfo(long lngProdPolicySeqId) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CustConfigMsgVO custConfigMsgVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetCustomMsgInfo);
			cStmtObject.setLong(1,lngProdPolicySeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null)
			{
				while(rs.next())
				{
					custConfigMsgVO = new CustConfigMsgVO();
					custConfigMsgVO.setConfigYN(TTKCommon.checkNull(rs.getString("SHOW_CUST_MSG")));
					custConfigMsgVO.setMessage(TTKCommon.checkNull(rs.getString("MESSAGE")));
				}//end of while(rs.next())
			}//end of (rs != null)
			return custConfigMsgVO;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getCustMsgInfo()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PolicyDAOImpl getCustMsgInfo()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getCustMsgInfo()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	}//end of CustConfigMsgVO getCustMsgInfo(long lngProdPolicySeqId)
	
	/**
     * This method adds/updates the CustConfigMsgVO which contains configuration details
     * @param custConfigMsgVO the details which has to be added or updated
     * @return int value,indicates the rows processed
     * @exception throws TTKException
     */
	public int saveCustMsgInfo(CustConfigMsgVO custConfigMsgVO) throws TTKException{
		int iResult = 0;  
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCustomMsgInfo);
            if(custConfigMsgVO.getProdPolicySeqID()!= null){
            	cStmtObject.setLong(1,custConfigMsgVO.getProdPolicySeqID());
            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else
            cStmtObject.setString(2, custConfigMsgVO.getConfigYN());
            cStmtObject.setString(3, custConfigMsgVO.getMessage());
            cStmtObject.setLong(4,custConfigMsgVO.getUpdatedBy());
            cStmtObject.registerOutParameter(5, Types.INTEGER);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(5);
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
        			log.error("Error while closing the Statement in PolicyDAOImpl savePolicy()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PolicyDAOImpl savePolicy()",sqlExp);
        				throw new TTKException(sqlExp, "policy");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "policy");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
	}//end of saveCustMsgInfo(CustConfigMsgVO custConfigMsgVO)
	
	/**
	 * This method returns the ConfigCopayVO which contains Copay Amount configuration Details
	 * @param long lngProdPolicySeqId
	 * @return ConfigCopayVO which contains Copay Amount configuration Details
	 * @exception throws TTKException
	 */
	public ConfigCopayVO getConfigCopayAmt(long lngProdPolicySeqId) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ConfigCopayVO configCopayVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetConfigCopayAmt);
			cStmtObject.setLong(1,lngProdPolicySeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null)
			{
				while(rs.next())
				{
					configCopayVO = new ConfigCopayVO();
					if(rs.getString("COPAY_APPROVED_AMT") != null){
						configCopayVO.setClaimApprAmt(new BigDecimal(rs.getString("COPAY_APPROVED_AMT")));
					}
					//Changes as per KOC 1142 Change Request
					if(rs.getString("REGN_BSD_PRM_CONFIG") != null){
						configCopayVO.setInsuredRegion(rs.getString("REGN_BSD_PRM_CONFIG"));
					}
					//Changes as per KOC 1142 Change Request
					if(rs.getString("COPAY_YN") != null){
						configCopayVO.setCopaypercentageYN(rs.getString("COPAY_YN"));
					}
			
					if(rs.getString("COPAY_PERC") != null){
						configCopayVO.setCopaypercentage(rs.getString("COPAY_PERC"));
					}
				
					//Changes as per KOC 1142 Change Request
				}//end of while(rs.next())
			}//end of (rs != null)
			return configCopayVO;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getCustMsgInfo()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PolicyDAOImpl getCustMsgInfo()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getCustMsgInfo()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	}//end of getConfigCopayAmt(long lngProdPolicySeqId)
	
	/**
     * This method adds/updates the ConfigCopayVO which contains Copay Amount configuration Details
     * @param configCopayVO the details which has to be added or updated
     * @return int value,indicates the rows processed
     * @exception throws TTKException
     */
	public int saveConfigCopayAmt(ConfigCopayVO configCopayVO) throws TTKException{
		int iResult = 0;  
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
                     
          //  cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveConfigCopayAmt);
             cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveConfigCopayMaxLimit);
            if(configCopayVO.getProdPolicySeqID()!= null){
            	cStmtObject.setLong(1,configCopayVO.getProdPolicySeqID());
            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else.
            if(configCopayVO.getCopaypercentageYN() != null){
            	
            	cStmtObject.setString(2,configCopayVO.getCopaypercentageYN());

            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(2,"N");
            }//end of else
            
            if(configCopayVO.getClaimApprAmt() != null){
            	cStmtObject.setBigDecimal(3,configCopayVO.getClaimApprAmt());
           }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(3,null);
            }//end of else
                 
                   
            if(configCopayVO.getCopaypercentage() != null){
            	cStmtObject.setString(4,configCopayVO.getCopaypercentage());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(4,null);
            }//end of else
            
            cStmtObject.setLong(5,configCopayVO.getUpdatedBy());
            cStmtObject.setString(6,configCopayVO.getInsuredRegion());//1284 change request
            cStmtObject.registerOutParameter(7, Types.INTEGER);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(7);
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
        			log.error("Error while closing the Statement in PolicyDAOImpl savePolicy()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PolicyDAOImpl savePolicy()",sqlExp);
        				throw new TTKException(sqlExp, "policy");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "policy");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
	}//end of saveConfigCopayAmt(ConfigCopayVO configCopayVO)
	/**
     * This method adds/updates the SumInsuredRestrictionVO which contains SumInsuredRestriction Amount configuration Details
     * @param sumInsuredRestrictionVO the details which has to be added or updated
     * @return int value,indicates the rows processed
     * @exception throws TTKException
	 *--Changes as per KOC 1140 and 1142 and 1165 ChangeRequest
     */
	public ArrayList saveConfigSumInsuredRestrictedAmt(SumInsuredRestrictionVO sumInsuredRestrictionVO,String strFlag) throws TTKException{
		int iResult = 0;  
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    ArrayList allOutParam=new ArrayList();
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveConfigSumInsuredRestrictedAmt);
           if(sumInsuredRestrictionVO.getLngProdPolicySeqID()!= null){
            	cStmtObject.setLong(1,sumInsuredRestrictionVO.getLngProdPolicySeqID());
               }//end of if(sumInsuredRestrictionVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else.
           if(sumInsuredRestrictionVO.getSuminsuredrestrictionYN()!= null){
           	cStmtObject.setString(2,sumInsuredRestrictionVO.getSuminsuredrestrictionYN());
           }//end of            if(sumInsuredRestrictionVO.getStrSuminsuredrestrictionYN()!= null){

           else{
           	cStmtObject.setString(2,null);
           }//end of else.
           if(sumInsuredRestrictionVO.getMembersrelation()!= null){
              	cStmtObject.setString(3,sumInsuredRestrictionVO.getMembersrelation());
              }//end of            if(sumInsuredRestrictionVO.getStrSuminsuredrestrictionYN()!= null){

              else{
              	cStmtObject.setString(3,null);
              }//end of else.
           if(sumInsuredRestrictionVO.getFixedAmtRestriction()!= null){
              	cStmtObject.setBigDecimal(4,sumInsuredRestrictionVO.getFixedAmtRestriction());
              }//end of            if(sumInsuredRestrictionVO.getFixedAmtRestriction()!= null){

              else{
              	cStmtObject.setString(4,null);
              }//end of else.
           if(sumInsuredRestrictionVO.getPercentageRestriction()!= null){
              	cStmtObject.setString(5,sumInsuredRestrictionVO.getPercentageRestriction());
              }//end of            if(sumInsuredRestrictionVO.getPercentageRestriction()!= null){

              else{
              	cStmtObject.setString(5,null);
              }//end of else.
           if(strFlag!= null){
             	cStmtObject.setString(6,strFlag);
             }//end of            if(strFlag!= null()!= null){

             else{
             	cStmtObject.setString(6,"FPR");
             }//end of else.
           
             if(sumInsuredRestrictionVO.getStatus() != null){
           	cStmtObject.setString(7,sumInsuredRestrictionVO.getStatus());
           }//end of if(bufferDetailVO.getBufferAmt() != null)
           else{
           	cStmtObject.setString(7,null);
           }//end of else
           
          
            
            if(sumInsuredRestrictionVO.getAgeRestricted()!= null){
                cStmtObject.setString(8,(sumInsuredRestrictionVO.getSuminsuredrestrictionYN().equalsIgnoreCase("Y"))?sumInsuredRestrictionVO.getAgeRestricted():null);
            }
                else{
                    cStmtObject.setString(8,null);
                }
                
                if(sumInsuredRestrictionVO.getFixedAmtRestriction()!= null){
                    cStmtObject.setBigDecimal(9,(sumInsuredRestrictionVO.getSuminsuredrestrictionYN().equalsIgnoreCase("Y"))?sumInsuredRestrictionVO.getAgeRestrictedAmount():null );
                }
                else{
                        cStmtObject.setBigDecimal(9,null);
                    }
                cStmtObject.setLong(10,sumInsuredRestrictionVO.getUpdatedBy());
              
            cStmtObject.registerOutParameter(11, Types.INTEGER);
           cStmtObject.registerOutParameter(12, Types.VARCHAR);
            cStmtObject.execute();
      
            iResult = cStmtObject.getInt(11);
           String alertMsg = (cStmtObject.getString(12)!=null)?cStmtObject.getString(12):"";
           if(!alertMsg.equalsIgnoreCase(""))
            {
            	allOutParam.add(0);
            	allOutParam.add(alertMsg);
            }
            else
            {
            	allOutParam.add(iResult);
            	allOutParam.add(alertMsg);
            }
           
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
        			log.error("Error while closing the Statement in PolicyDAOImpl saveConfigSumInsuredRestrictedAmt()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PolicyDAOImpl saveConfigSumInsuredRestrictedAmt()",sqlExp);
        				throw new TTKException(sqlExp, "policy");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "policy");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return allOutParam;
	}//end of saveConfigSumInsuredRestrictedAmt(SumInsuredRestrictionVO sumInsuredRestrictionVO)
	/**
	 * This method returns the SumInsuredRestrictionVO which contains SumInsuredRestriction Amount configuration Details
	 * @param long lngProdPolicySeqId
	 * @return sumInsuredRestrictionVO which contains SumInsuredRestriction Amount configuration Details
	 * @exception throws TTKException
	 *--Changes as per KOC 1140 and 1142 and 1165 ChangeRequest
	 */
	public SumInsuredRestrictionVO getConfigSumInsuredRestrictedAmt(long lngProdPolicySeqId) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		SumInsuredRestrictionVO sumInsuredRestrictionVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetConfigSumInsuredRestrictedAmt);
			cStmtObject.setLong(1,lngProdPolicySeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null)
			{
			while(rs.next())
				{
					sumInsuredRestrictionVO = new SumInsuredRestrictionVO();
					if(rs.getString("SI_RESTRICT_AMT") != null){
						sumInsuredRestrictionVO.setFixedAmtRestriction(new BigDecimal(rs.getString("SI_RESTRICT_AMT")));
					}
				
					if(rs.getString("SI_RELATION") != null){
						sumInsuredRestrictionVO.setMembersrelation(rs.getString("SI_RELATION"));
					}
					if(rs.getString("SI_RESTRICT_PERC") != null){
						
						sumInsuredRestrictionVO.setPercentageRestriction(rs.getString("SI_RESTRICT_PERC"));
					}
					if(rs.getString("SI_RESTRICT_YN") != null){
						sumInsuredRestrictionVO.setSuminsuredrestrictionYN(rs.getString("SI_RESTRICT_YN"));
					}
					if(rs.getString("SI_REST_LEVEL") != null){
						sumInsuredRestrictionVO.setStatus(rs.getString("SI_REST_LEVEL"));
					}
					 if(rs.getString("REST_AGE") != null){
						
						sumInsuredRestrictionVO.setAgeRestricted(rs.getString("REST_AGE"));
					}
					if(rs.getBigDecimal("REST_AGE_AMT") != null){
						sumInsuredRestrictionVO.setAgeRestrictedAmount(rs.getBigDecimal("REST_AGE_AMT"));
					}
					else{
						sumInsuredRestrictionVO.setAgeRestrictedAmount(null);
					}
					
					
				}//end of while(rs.next())
			}//end of (rs != null)
			return sumInsuredRestrictionVO;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getConfigSumInsuredRestrictedAmt()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PolicyDAOImpl getConfigSumInsuredRestrictedAmt()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getConfigSumInsuredRestrictedAmt()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	}//end of getConfigSumInsuredRestrictedAmt(long lngProdPolicySeqId)   //--Changes as per KOC 1140 and 1142 and 1165 ChangeRequest
	
	//Added as per kOC 1285 Change Request

	public ArrayList saveDomicilaryConfig(DomConfigVO domConfigVO) throws TTKException{
			int iResult = 0;  
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ArrayList allOutParam=new ArrayList();
	    	try{
	    		conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveDomicilaryConfig);
	           
	            /*	cStmtObject.setLong(1,lngProdPolicySeqID);
	                cStmtObject.setString(2,TTKCommon.checkNull(strflag));
	          cStmtObject.registerOutParameter(1, Types.INTEGER);
	            cStmtObject.execute();
	            iResult = cStmtObject.getInt(1);*/
	            
	            if(domConfigVO.getProdPolicySeqID()!= null){
	            	cStmtObject.setLong(1,domConfigVO.getProdPolicySeqID());
	            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
	            else{
	            	cStmtObject.setLong(1,0);
	            }//end of else.
	            if(domConfigVO.getSelectedType() != null){
	            	cStmtObject.setString(2,domConfigVO.getSelectedType());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(2,null);
	            }//end of else
	            cStmtObject.setLong(3,domConfigVO.getUpdatedBy());
	            cStmtObject.registerOutParameter(4, Types.VARCHAR);
	            cStmtObject.execute();
	          //  iResult = cStmtObject.getInt(4);
	            
	           // iResult = cStmtObject.getInt(11);
	            String alertMsg = (cStmtObject.getString(4)!=null)?cStmtObject.getString(4):"";
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
	            
	        }//end of try
	        catch (SQLException sqlExp) 
	        {
	            throw new TTKException(sqlExp, "policy");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp) 
	        {
	            throw new TTKException(exp, "policy");
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
	        			log.error("Error while closing the Statement in PolicyDAOImpl saveDomicilaryConfig()",sqlExp);
	        			throw new TTKException(sqlExp, "policy");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PolicyDAOImpl saveDomicilaryConfig()",sqlExp);
	        				throw new TTKException(sqlExp, "policy");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "policy");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	        return allOutParam;
		}//end of saveDomicilaryConfig(DomConfigVO domConfigVO)
	
	
	/**
	 * This method returns the DomConfigVO which contains Copay Amount configuration Details
	 * @param long lngProdPolicySeqId
	 * @return DomConfigVO which contains Domicilary configuration Details
	 * @exception throws TTKException
	 */
	public DomConfigVO getDomicilaryConfig(long lngProdPolicySeqId) throws TTKException{
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		DomConfigVO domConfigVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectDomicilaryConfig);
			cStmtObject.setLong(1,lngProdPolicySeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null)
			{
				while(rs.next())
				{
					domConfigVO = new DomConfigVO();
					if(rs.getString("DOM_CONFIG_TYPE") != null){
						domConfigVO.setSelectedType((rs.getString("DOM_CONFIG_TYPE")));
					}
				}//end of while(rs.next())
			}//end of (rs != null)
			return domConfigVO;
		 }//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getCustMsgInfo()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PolicyDAOImpl getCustMsgInfo()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getCustMsgInfo()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	}//end of getDomicilaryConfig(long lngProdPolicySeqId)
	 /**bajaj
	 * This method returns the InsuranceApproveVO which contains Copay Amount configuration Details
	 * @param long lngProdPolicySeqId
	 * @return InsuranceApproveVO which contains Domicilary configuration Details i.e Flag Enr or POL || Product
	 * @exception throws TTKException
	 */
	public InsuranceApproveVO getConfigInsuranceApproveData(Long lngProdPolicySeqId)throws TTKException {
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		InsuranceApproveVO insuranceApproveVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectInsApproveConfig);
			cStmtObject.setLong(1,lngProdPolicySeqId);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
		
			if(rs != null)
			{
				while(rs.next())
				{
					insuranceApproveVO = new InsuranceApproveVO();
					//denial process
			
					if(rs.getString("PROD_POLICY_SEQ_ID") != null){
						insuranceApproveVO.setProdPolicySeqID(rs.getLong("PROD_POLICY_SEQ_ID"));
					}
					//added for bajaj enhan1
					if(rs.getString("ins_pat_apr_rej_yn") != null){
						insuranceApproveVO.setInsPatYN(rs.getString("ins_pat_apr_rej_yn"));
					}
					if(rs.getString("INS_PAT_ALLOW_YN") != null){
						insuranceApproveVO.setAllowedPatYN(rs.getString("INS_PAT_ALLOW_YN"));
					}
					if(rs.getString("INS_PAT_OPERATOR") != null){
						insuranceApproveVO.setPatOperator(rs.getString("INS_PAT_OPERATOR"));
					}
					
					if(rs.getString("INS_PAT_APR_LIMIT") != null){
						insuranceApproveVO.setPatApproveAmountLimit(rs.getBigDecimal("INS_PAT_APR_LIMIT"));
					}
					//  
					if(rs.getString("ins_pat_rej_allow_yn") != null){
						insuranceApproveVO.setRejectionYN(rs.getString("ins_pat_rej_allow_yn"));
						}
					if(rs.getString("pat_mail_flag") != null){
						insuranceApproveVO.setNotificationFlag(rs.getString("pat_mail_flag"));
					}
					if(rs.getString("pat_mail_freq_hours") != null){
						insuranceApproveVO.setTimeInHrs(rs.getString("pat_mail_freq_hours"));
					}
					if(rs.getString("pat_mail_freq_mins") != null){
						insuranceApproveVO.setTimeInMins(rs.getString("pat_mail_freq_mins"));
					}
                    
                                       
                    /*
                        if(rs.getString("INS_REJ_ALLOW_YN") != null){
						insuranceApproveVO.setRejectionYN(rs.getString("INS_REJ_ALLOW_YN"));
                        
						 }
                    else{
						insuranceApproveVO.setRejectionYN("Y");
						
					}*/
					
					if(rs.getString("ins_clm_cl_apr_rej_yn") != null){
						insuranceApproveVO.setInsClmYN(rs.getString("ins_clm_cl_apr_rej_yn"));
					}
					if(rs.getString("ins_clm_cl_allow_yn") != null){
						insuranceApproveVO.setAllowedClmYN(rs.getString("ins_clm_cl_allow_yn"));
					}
					if(rs.getString("ins_clm_cl_operator") != null){
						insuranceApproveVO.setClmOperator(rs.getString("ins_clm_cl_operator"));
					}
					if(rs.getString("ins_clm_cl_apr_limit") != null){
						insuranceApproveVO.setClmApproveAmountLimit(rs.getBigDecimal("ins_clm_cl_apr_limit"));
					}
					if(rs.getString("ins_clm_cl_rej_allow_yn") != null){
						insuranceApproveVO.setRejectionClmYN(rs.getString("ins_clm_cl_rej_allow_yn"));
					}
					if(rs.getString("clm_cl_mail_flag") != null){
						insuranceApproveVO.setNotificationClmFlag(rs.getString("clm_cl_mail_flag"));
					}
					//end added for enhan1
					
					/*if(rs.getString("ADDED_BY") != null){
						insuranceApproveVO.setAddedBy(rs.getLong("ADDED_BY"));
					}
					//1274A
*/					
					if(rs.getString("ins_clm_cm_apr_rej_yn") != null){
						insuranceApproveVO.setInsClmRemYN(rs.getString("ins_clm_cm_apr_rej_yn"));
					}
					if(rs.getString("ins_clm_cm_allow_yn") != null){
						insuranceApproveVO.setAllowedClmRemYN(rs.getString("ins_clm_cm_allow_yn"));
                    }                                        
                   
					if(rs.getString("ins_clm_cm_operator") != null){
						insuranceApproveVO.setClmOperatorRem(rs.getString("ins_clm_cm_operator"));
					}
					if(rs.getString("ins_clm_cm_apr_limit") != null){
						insuranceApproveVO.setClmApproveAmountLimitRem(rs.getBigDecimal("ins_clm_cm_apr_limit"));
					}
					if(rs.getString("ins_clm_cm_rej_allow_yn") != null){
						insuranceApproveVO.setRejectionClmRemYN(rs.getString("ins_clm_cm_rej_allow_yn"));
					}
					if(rs.getString("clm_cm_mail_flag") != null){
						insuranceApproveVO.setNotificationClmRemFlag(rs.getString("clm_cm_mail_flag"));
					}	
					if(rs.getString("clm_mail_freq_hours") != null){
						insuranceApproveVO.setTimeInHrsClm(rs.getString("clm_mail_freq_hours"));
					}
					if(rs.getString("clm_mail_freq_mins") != null){
						insuranceApproveVO.setTimeInMinsClm(rs.getString("clm_mail_freq_mins"));
					}
					//denial process
				}//end of while(rs.next())
			}//end of (rs != null)
			return insuranceApproveVO;	// TODO Auto-generated method stub
	 }//end of try
	catch (SQLException sqlExp) 
	{
		throw new TTKException(sqlExp, "policy");
	}//end of catch (SQLException sqlExp)
	catch (Exception exp) 
	{
		throw new TTKException(exp, "policy");
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
				log.error("Error while closing the Resultset in PolicyDAOImpl getConfigInsuranceApproveData()",sqlExp);
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			finally // Even if result set is not closed, control reaches here. Try closing the statement now.
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in PolicyDAOImpl getConfigInsuranceApproveData()",sqlExp);
					throw new TTKException(sqlExp, "policy");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PolicyDAOImpl getConfigInsuranceApproveData()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
		}//end of try
		catch (TTKException exp)
		{
			throw new TTKException(exp, "policy");
		}//end of catch (TTKException exp)
		finally // Control will reach here in anycase set null to the objects 
		{
			rs = null;
			cStmtObject = null;
			conn = null;
		}//end of finally
	}//end of finally
}//end of getDomicilaryConfig(long lngProdPolicySeqId)
/**bajaj
     * This method adds/updates the InsuranceApproveVO which contains SumInsuredRestriction Amount configuration Details
     * @param InsuranceApproveVO the details which has to be added or updated
     * @return int value,indicates the rows processed
     * @exception throws TTKException
     */	

	public int saveConfigInsuranceApprove(InsuranceApproveVO insuranceApproveVO, String string)throws TTKException {
		// TODO Auto-generated method stub  
		int iResult = 0;  
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    
    	try{
    		conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveInsApproveConfig);
           
                
              
            /*
             * v_prod_policy_seq_id      IN tpa_ins_prod_policy.prod_policy_seq_id%TYPE,
                                    v_ins_pat_allow_yn            IN tpa_ins_prod_policy.ins_pat_allow_yn%TYPE,
                                    v_ins_pat_operator            IN tpa_ins_prod_policy.ins_pat_operator%TYPE,
                                    v_ins_pat_appr_limit          IN tpa_ins_prod_policy.ins_pat_apr_limit%TYPE,
                                    v_ins_rej_allow_yn        IN tpa_ins_prod_policy.ins_rej_allow_yn%type, 
                                    v_ins_clm_allow_yn        IN tpa_ins_prod_policy.Ins_Clm_Allow_Yn%TYPE,
                                    v_ins_clm_operator        IN tpa_ins_prod_policy.ins_clm_operator%type,
                                    v_ins_clm_appr_limit      IN tpa_ins_prod_policy.ins_clm_apr_limit%type,
                                    v_ins_pat_apr_rej_yn        IN tpa_ins_prod_policy.ins_pat_apr_rej_yn%type,
                                    v_ins_clm_apr_rej_yn      IN  tpa_ins_prod_policy.ins_clm_apr_rej_yn%type,
                                    v_mail_FLAG                IN tpa_ins_prod_policy.mail_FLAG%type, 
                                    v_mail_freq_hours          IN tpa_ins_prod_policy.mail_freq_hours%type, 
                                    v_mail_freq_mins          IN tpa_ins_prod_policy.mail_freq_mins%type, 
                                    v_added_by                IN tpa_ins_prod_policy.added_by%TYPE,
                                    v_added_date              IN tpa_ins_prod_policy.added_date%TYPE,
                                    v_rows_processed          OUT NUMBER) IS
                                    
                                    

             */
              
              
            if(insuranceApproveVO.getProdPolicySeqID()!= null){
            	cStmtObject.setLong(1,insuranceApproveVO.getProdPolicySeqID());
            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else.
            if(insuranceApproveVO.getInsPatYN()!= null){
            	cStmtObject.setString(2,insuranceApproveVO.getInsPatYN());
            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setString(2,null);
            }//end of else.
            if(insuranceApproveVO.getAllowedPatYN() != null){
            	cStmtObject.setString(3,insuranceApproveVO.getAllowedPatYN());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(3,null);
            }//end of else
            
            if(insuranceApproveVO.getPatOperator() != null){
				cStmtObject.setString(4,insuranceApproveVO.getPatOperator());
			}//end of if(getRejectionYN() != null)
			else{
				cStmtObject.setString(4,null);
			}//end of else
            if(insuranceApproveVO.getPatApproveAmountLimit() != null){
            	cStmtObject.setBigDecimal(5,insuranceApproveVO.getPatApproveAmountLimit());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(5,null);
            }//end of else
            
            
        
				cStmtObject.setString(6,insuranceApproveVO.getRejectionYN().trim());
				
	
            if(insuranceApproveVO.getNotificationFlag() != null){
            	cStmtObject.setString(7,insuranceApproveVO.getNotificationFlag());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(7,null);
            }//end of else
            if(insuranceApproveVO.getTimeInHrs() != null){
				cStmtObject.setString(8,insuranceApproveVO.getTimeInHrs());
			}//end of if(bufferDetailVO.getBufferAmt() != null)
			else{
				cStmtObject.setString(8,null);
			}//end of else

			if(insuranceApproveVO.getTimeInMins() != null){
				cStmtObject.setString(9,insuranceApproveVO.getTimeInMins());
			}//end of if(bufferDetailVO.getAdobeYN() != null)
			else{
				cStmtObject.setString(9,null);
			}//end of else
			
			if(insuranceApproveVO.getInsClmYN()!= null){
            	cStmtObject.setString(10,insuranceApproveVO.getInsClmYN());
            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setString(10,null);
            }//end of else.
            if(insuranceApproveVO.getAllowedClmYN() != null){
            	cStmtObject.setString(11,insuranceApproveVO.getAllowedClmYN());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(11,null);
            }//end of else
            
            if(insuranceApproveVO.getClmOperator() != null){
				cStmtObject.setString(12,insuranceApproveVO.getClmOperator());
			}//end of if(getRejectionYN() != null)
			else{
				cStmtObject.setString(12,null);
			}//end of else
            if(insuranceApproveVO.getClmApproveAmountLimit() != null){
            	cStmtObject.setBigDecimal(13,insuranceApproveVO.getClmApproveAmountLimit());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(13,null);
            }//end of else
            
            
        
				cStmtObject.setString(14,insuranceApproveVO.getRejectionClmYN().trim());
				
	
            if(insuranceApproveVO.getNotificationClmFlag() != null){
            	cStmtObject.setString(15,insuranceApproveVO.getNotificationClmFlag());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(15,null);
            }//end of else
	            
            if(insuranceApproveVO.getInsClmRemYN()!= null){
            	cStmtObject.setString(16,insuranceApproveVO.getInsClmRemYN());
            }//end of if(custConfigMsgVO.getProdPolicySeqID() != null)
            else{
            	cStmtObject.setString(16,null);
            }//end of else.
            if(insuranceApproveVO.getAllowedClmRemYN() != null){
            	cStmtObject.setString(17,insuranceApproveVO.getAllowedClmRemYN());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(17,null);
            }//end of else
            
            if(insuranceApproveVO.getClmOperatorRem() != null){
				cStmtObject.setString(18,insuranceApproveVO.getClmOperatorRem());
			}//end of if(getRejectionYN() != null)
			else{
				cStmtObject.setString(18,null);
			}//end of else
            if(insuranceApproveVO.getClmApproveAmountLimitRem() != null){
            	cStmtObject.setBigDecimal(19,insuranceApproveVO.getClmApproveAmountLimitRem());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(19,null);
            }//end of else
            
            
        
				cStmtObject.setString(20,insuranceApproveVO.getRejectionClmRemYN().trim());
				
	
            if(insuranceApproveVO.getNotificationClmRemFlag() != null){
            	cStmtObject.setString(21,insuranceApproveVO.getNotificationClmRemFlag());
            }//end of if(bufferDetailVO.getBufferAmt() != null)
            else{
            	cStmtObject.setString(21,null);
            }//end of else
            if(insuranceApproveVO.getTimeInHrsClm() != null){
				cStmtObject.setString(22,insuranceApproveVO.getTimeInHrsClm());
			}//end of if(bufferDetailVO.getBufferAmt() != null)
			else{
				cStmtObject.setString(22,null);
			}//end of else

			if(insuranceApproveVO.getTimeInMinsClm() != null){
				cStmtObject.setString(23,insuranceApproveVO.getTimeInMinsClm());
			}//end of if(bufferDetailVO.getAdobeYN() != null)
			else{
				cStmtObject.setString(23,null);
			}//end of else
			
			
			//1274 A
			cStmtObject.setLong(24,insuranceApproveVO.getUpdatedBy());
			//cStmtObject.setTimestamp(15,new Timestamp(TTKCommon.getDate().getTime()));
			cStmtObject.registerOutParameter(25, Types.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(25);
            
            
        }//end of try
        catch (SQLException sqlExp) 
        {
            throw new TTKException(sqlExp, "policy");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp) 
        {
            throw new TTKException(exp, "policy");
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
        			log.error("Error while closing the Statement in PolicyDAOImpl saveConfigInsuranceApprove()",sqlExp);
        			throw new TTKException(sqlExp, "policy");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PolicyDAOImpl saveConfigInsuranceApprove()",sqlExp);
        				throw new TTKException(sqlExp, "policy");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "policy");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
	}//end of saveConfigInsuranceApprove

	
	// Shortfall CR KOC1179
		/**
	     * This method adds/updates the ShortfallDaysConfigVO which contains Shortfall Days Configuration Details
	     * @param ShortfallDays the details which has to be added or updated
	     * @return int value,indicates the rows processed
	     * @exception throws TTKException
	     */
		public long saveShortfallDaysConfig(ShortfallDaysConfigVO shortfallDaysConfigVO) throws TTKException{
			long iResult = 0;  
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	try{
	    		conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveShortfallDaysConfig);
	       String 	remainderReqDays =(!shortfallDaysConfigVO.getRemainderReqDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getRemainderReqDays():"";
	       String 	closureNoticeDays =(!shortfallDaysConfigVO.getClosureNoticeDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getClosureNoticeDays():"";
	       String 	apprClosureNoticeDays =(!shortfallDaysConfigVO.getApprClosureNoticeDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getApprClosureNoticeDays():"";
	       String 	claimClosureDays =(!shortfallDaysConfigVO.getClaimClosureDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getClaimClosureDays():"";
	       String 	claimSubmissionDays =(!shortfallDaysConfigVO.getClaimSubmissionDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getClaimSubmissionDays():"";
	       String 	intimationReqDays =(!shortfallDaysConfigVO.getIntimationReqDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getIntimationReqDays():"";
	       String 	regretLetterDays =(!shortfallDaysConfigVO.getRegretLetterDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getRegretLetterDays():"";
	       String 	postHospitalizationDays =(!shortfallDaysConfigVO.getPostHospitalizationDays().equalsIgnoreCase(""))?shortfallDaysConfigVO.getPostHospitalizationDays():"";//shortfall phase1
	       if(shortfallDaysConfigVO.getShortfallConfigSeqID()== null)
	    		{	cStmtObject.setLong(1, 0);
	        	}
	    		else{
	    			cStmtObject.setLong(1,shortfallDaysConfigVO.getShortfallConfigSeqID());
				    }//end of else
	        		 
	        	// 2
	    		if(shortfallDaysConfigVO.getProdPolicySeqID()== null)
	    		{	cStmtObject.setLong(2, 0);
	    		}
	    		else{
	    			cStmtObject.setLong(2,shortfallDaysConfigVO.getProdPolicySeqID());
				}//end of else
	    		  cStmtObject.setString(3,remainderReqDays);
	       cStmtObject.setString(4,closureNoticeDays);
	       cStmtObject.setString(5,apprClosureNoticeDays);
	       cStmtObject.setString(6,claimClosureDays);
	       cStmtObject.setString(7,claimSubmissionDays);
	       cStmtObject.setString(8,intimationReqDays);
	       cStmtObject.setString(9,regretLetterDays);
	       cStmtObject.setLong(10,shortfallDaysConfigVO.getUpdatedBy());
	       cStmtObject.setString(11,postHospitalizationDays);//shortfall phase1	    
	        cStmtObject.registerOutParameter(1,Types.BIGINT);
	       cStmtObject.execute();
	            iResult = cStmtObject.getLong(1);
	        }//end of try
	        catch (SQLException sqlExp) 
	        {
	            throw new TTKException(sqlExp, "policy");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp) 
	        {
	            throw new TTKException(exp, "policy");
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
	        			log.error("Error while closing the Statement in PolicyDAOImpl saveShortfallDaysConfig(shortfallDaysConfigVO)",sqlExp);
	        			throw new TTKException(sqlExp, "policy");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PolicyDAOImpl saveShortfallDaysConfig(shortfallDaysConfigVO)",sqlExp);
	        				throw new TTKException(sqlExp, "policy");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "policy");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	        return iResult;
		}//end of saveConfigCopayAmt(ConfigCopayVO configCopayVO)
		
		// Changes added for CR KOC1179
		 /**
	    * This method returns the ArrayList, which contains the ShortfallDaysConfigVOs which are populated from the database
	    * @param lngProdPolicySeqID contains the ProdPolicySeqID
	    * @return ArrayList of ShortfallDaysConfigVOs object's which contains the Policy Configure Plan Details
	    * @exception throws TTKException
	    */
	   public ShortfallDaysConfigVO getShortfallDaysConfig(long lngProdPolicySeqID) throws TTKException {
			Collection<Object> alResultList = new ArrayList<Object>();
	       Connection conn = null;
	       CallableStatement cStmtObject=null;
	       ResultSet rs = null;
	       ShortfallDaysConfigVO shortfallDaysConfigVO=null;
	       try{
	       	conn = ResourceManager.getConnection();
	       	cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectShortfallDaysConfig);
	           
	           cStmtObject.setLong(1,lngProdPolicySeqID);
	           cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
	           cStmtObject.execute();
	           rs = (java.sql.ResultSet)cStmtObject.getObject(2);
	           if(rs != null){
	           if(rs.next()){
	           		shortfallDaysConfigVO = new ShortfallDaysConfigVO();
	               	if(rs.getString("CONFIG_SEQ_ID") != null){
	               		shortfallDaysConfigVO.setShortfallConfigSeqID(new Long(rs.getLong("CONFIG_SEQ_ID")));
	               	}//end of if(rs.getString("PLAN_CONFIG_SEQ_ID")
	               	else {
	               		shortfallDaysConfigVO.setShortfallConfigSeqID(null);
	               	}//end of if(rs.getString("PLAN_CONFIG_SEQ_ID") != null)
	               	if(rs.getString("PROD_POLICY_SEQ_ID") != null){
	               		shortfallDaysConfigVO.setProdPolicySeqID(new Long(rs.getLong("PROD_POLICY_SEQ_ID")));
	            	}//end of if(rs.getString("POLICY_SEQ_ID") != null)
	               	shortfallDaysConfigVO.setRemainderReqDays(rs.getString("reminder_letter_days"));
	               	shortfallDaysConfigVO.setClosureNoticeDays(rs.getString("closer_notice_days"));
	               	shortfallDaysConfigVO.setApprClosureNoticeDays(rs.getString("approval_closer_days"));
	               	shortfallDaysConfigVO.setClaimClosureDays(rs.getString("claim_closer_letter_days"));
	               	shortfallDaysConfigVO.setClaimSubmissionDays(rs.getString("clm_docs_submit_in_days"));
	               	shortfallDaysConfigVO.setRegretLetterDays(rs.getString("regrett_letter_days"));
	              	shortfallDaysConfigVO.setIntimationReqDays(rs.getString("clm_intimation_days"));
	              	shortfallDaysConfigVO.setPostHospitalizationDays(rs.getString("post_hospitalization"));//shortfall phase1
	               	
	               	
	             //  	alResultList.add(shortfallDaysConfigVO);
	           	}//end of while(rs.next())
	           }//end of if(rs != null)
	       	return shortfallDaysConfigVO;
	       }//end of try
	       catch (SQLException sqlExp)
	       {
	           throw new TTKException(sqlExp, "prodpolicyconfig");
	       }//end of catch (SQLException sqlExp)
	       catch (Exception exp)
	       {
	           throw new TTKException(exp, "prodpolicyconfig");
	       }//end of catch (Exception exp)
	       finally
			{
				 //Nested Try Catch to ensure resource closure 
				try // First try closing the result set
				{
					try
					{
						if (rs != null) rs.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in PolicyDAOImpl getShortfallDaysConfig() ",sqlExp);
						throw new TTKException(sqlExp, "prodpolicyconfig");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in PolicyDAOImpl getShortfallDaysConfig()",sqlExp);
							throw new TTKException(sqlExp, "prodpolicyconfig");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getPlcyConfigPlanDetail(ProdPolicySeqID)",sqlExp);
								throw new TTKException(sqlExp, "prodpolicyconfig");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "prodpolicyconfig");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of getShortfallDaysConfig(long lngProdPolicySeqID)
	   
	   
	  
	   public ArrayList saveManyBuffer(BufferDetailVO bufferDetailVO) throws TTKException {
	    	long lngBufferSeqID = 0;
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ArrayList allOutParam=new ArrayList();
	    	try{
	    		conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveBuffer);
	            
	            if(bufferDetailVO.getBufferSeqID() != null){
	            	cStmtObject.setLong(1,bufferDetailVO.getBufferSeqID());
	            }//end of if(bufferDetailVO.getBufferSeqID() != null)
	            else{
	            	cStmtObject.setLong(1,0);
	            }//end of else
	            
	            if(bufferDetailVO.getProdPolicySeqID() != null){
	            	cStmtObject.setLong(2,bufferDetailVO.getProdPolicySeqID());
	            }//end of if(bufferDetailVO.getProdPolicySeqID() != null)
	            else{
	            	cStmtObject.setString(2,null);
	            }//end of else
	            
	            if(bufferDetailVO.getBufferDate() != null){
	            	cStmtObject.setTimestamp(3,new Timestamp(bufferDetailVO.getBufferDate().getTime()));
	            }//end of if(bufferDetailVO.getBufferDate() != null)
	            else{
	            	cStmtObject.setTimestamp(3,null);
	            }//end of else
	            
	            cStmtObject.setString(4,bufferDetailVO.getModeTypeID());
	            
	            if(bufferDetailVO.getBufferAmt() != null){
	            	cStmtObject.setBigDecimal(5,bufferDetailVO.getBufferAmt());
	            }//end of if(bufferDetailVO.getBufferAmt() != null)
	            else{
	            	cStmtObject.setString(5,null);
	            }//end of else
	            
	            cStmtObject.setString(6,bufferDetailVO.getRefNbr());
	            cStmtObject.setString(7,bufferDetailVO.getRemarks());
	            if(bufferDetailVO.getAllocatedAmt() != null){
	            	cStmtObject.setBigDecimal(8,bufferDetailVO.getAllocatedAmt());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(8,null);
	            }//end of else
	            
	           
	          //<!added for hyundai buffer by rekha on 19.06.2014 -->
	            if(bufferDetailVO.getNormMedAmt() != null){
	            	cStmtObject.setBigDecimal(9,bufferDetailVO.getNormMedAmt());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(9,null);
	            }//end of else
	            if(bufferDetailVO.getCriIllBufferAmt() != null){
	            	cStmtObject.setBigDecimal(10,bufferDetailVO.getCriIllBufferAmt());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(10,null);
	            }//end of else
	            
	            if(bufferDetailVO.getCritiCorpAmt() != null){
	            	cStmtObject.setBigDecimal(11,bufferDetailVO.getCritiCorpAmt());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(11,null);
	            }//end of else
	            
	            if(bufferDetailVO.getCritiMedAmt() != null){
	            	cStmtObject.setBigDecimal(12,bufferDetailVO.getCritiMedAmt());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(12,null);
	            }//end of else
	           
	            
	            if(bufferDetailVO.getNrmMedLimit() != null){
	            	cStmtObject.setBigDecimal(13,bufferDetailVO.getNrmMedLimit());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(13,null);
	            }//end of else
	            
	            
	            if(bufferDetailVO.getCriIllBufferLimit()!= null){
	            	cStmtObject.setBigDecimal(14,bufferDetailVO.getCriIllBufferLimit());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(14,null);
	            }//end of else
	            
	            
	            if(bufferDetailVO.getCriCorpLimit() != null){
	            	cStmtObject.setBigDecimal(15,bufferDetailVO.getCriCorpLimit());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(15,null);
	            }//end of else
	            
	            
	            if(bufferDetailVO.getCriMedLimit() != null){
	            	cStmtObject.setBigDecimal(16,bufferDetailVO.getCriMedLimit());
	            }//end of if(bufferDetailVO.getAllocatedAmt() != null)
	            else{
	            	cStmtObject.setString(16,null);
	            }//end of else
	            
	            
	          //<!-end - added for hyundai buffer by rekha on 19.06.2014 -->
	            cStmtObject.setLong(17,bufferDetailVO.getUpdatedBy());
	            cStmtObject.registerOutParameter(18,Types.VARCHAR); 
	            cStmtObject.registerOutParameter(19,Types.INTEGER);
	            cStmtObject.registerOutParameter(1,Types.BIGINT);
	            cStmtObject.execute(); 
	            lngBufferSeqID = cStmtObject.getLong(1);
	            
	            String alertMsg = (cStmtObject.getString(18)!=null)?cStmtObject.getString(18):"";
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
	            allOutParam.add(lngBufferSeqID);
	        }//end of try
	    	catch (SQLException sqlExp) 
	        {
	            throw new TTKException(sqlExp, "policy");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp) 
	        {
	            throw new TTKException(exp, "policy");
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
	        			log.error("Error while closing the Statement in PolicyDAOImpl saveBuffer()",sqlExp);
	        			throw new TTKException(sqlExp, "policy");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PolicyDAOImpl saveBuffer()",sqlExp);
	        				throw new TTKException(sqlExp, "policy");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "policy");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	return allOutParam;
	    }//end of saveBuffer(BufferDetailVO bufferDetailVO)
	   
	   /**
	     * 
	     */
		public ArrayList getProductCode(String profId)throws TTKException{
			Connection conn = null;
			PreparedStatement pStmt = null;
			ResultSet rs = null;
			ArrayList<String> alLicense	=	null;
			try
			{
				
				conn = ResourceManager.getConnection();
				pStmt = conn.prepareStatement(strGetProviderCode);
				pStmt.setString(1,profId);  //profId
				rs = pStmt.executeQuery();
				if(rs!=null){
					while(rs.next()){
						alLicense	=	new ArrayList<String>();
						alLicense.add(rs.getString("PACKAGE_ID"));
					}
		            
		            }
				
				return alLicense;
			}//end of try
			catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "hospital");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "hospital");
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
						log.error("Error while closing the Resultset in HospitalDAOImpl getLicenceNumbers()",sqlExp);
						throw new TTKException(sqlExp, "hospital");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (pStmt != null)	pStmt.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in HospitalDAOImpl getLicenceNumbers()",sqlExp);
							throw new TTKException(sqlExp, "hospital");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in HospitalDAOImpl getLicenceNumbers()",sqlExp);
								throw new TTKException(sqlExp, "hospital");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "hospital");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					pStmt = null;
					conn = null;
				}//end of finally
			}//end of finally
		}//end of getProductCode(ArrayList alProfessionals)

	public int saveBenefitTypeBufferLimit(ArrayList<Object> alProdPolicyLimit)throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	int iResult=0;
    	try{
    		conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strsaveBenefitTypeBufferLimit);
			
			cStmtObject.setLong(1,(Long)alProdPolicyLimit.get(0));
			cStmtObject.setString(2,(String)alProdPolicyLimit.get(1));
			cStmtObject.setString(3,(String)alProdPolicyLimit.get(2));
			cStmtObject.setString(4,(String)alProdPolicyLimit.get(3));
			cStmtObject.setString(5,(String)alProdPolicyLimit.get(4));
			cStmtObject.setString(6,(String)alProdPolicyLimit.get(5));
			cStmtObject.setLong(7,(Long)alProdPolicyLimit.get(6));
			
			cStmtObject.setString(8,(String)alProdPolicyLimit.get(7));		// partnerDental
			cStmtObject.setString(9,(String)alProdPolicyLimit.get(8));		// partnerOptical
			cStmtObject.setString(10,(String)alProdPolicyLimit.get(9));		// partnerOutpatient
			cStmtObject.setString(11,(String)alProdPolicyLimit.get(10));	// partnerPrescription
			cStmtObject.setString(12,(String)alProdPolicyLimit.get(11));	// partnerOpMaternity
			cStmtObject.registerOutParameter(13, Types.INTEGER);			// inserted/updated
			cStmtObject.execute();
			iResult = cStmtObject.getInt(13);
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
        			log.error("Error while closing the Statement in PolicyDAOImpl saveBenefitTypeBufferLimit()",sqlExp);
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
        				log.error("Error while closing the Connection in PolicyDAOImpl saveBenefitTypeBufferLimit()",sqlExp);
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
    }

	public ProdPolicyLimitVO getBenefitBufferLimitDtls(Long webBoardId) throws TTKException {
    	
		Connection conn = null;
    	PreparedStatement pStmt = null;
        ResultSet rs = null;
        Collection<Object> resultList = new ArrayList<Object>();
        ProdPolicyLimitVO prodPolicyLimitVO = null;
        try{
        	conn = ResourceManager.getConnection();
        	pStmt = (java.sql.CallableStatement) conn.prepareCall(strGetBenefitBufferLimitDtls);
			
        	pStmt.setLong(1,webBoardId);
			//cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
        	rs = pStmt.executeQuery();
			if(rs != null){
				while(rs.next()){
					prodPolicyLimitVO = new ProdPolicyLimitVO();
					
					prodPolicyLimitVO.setDental(rs.getString("dntl_buffer_limit"));
					prodPolicyLimitVO.setOptical(rs.getString("optc_buffer_limit"));
					prodPolicyLimitVO.setOpMaternity(rs.getString("omti_buffer_limit"));
					prodPolicyLimitVO.setOutpatient(rs.getString("opts_buffer_limit"));
					prodPolicyLimitVO.setPrescription(rs.getString("opts_pharma_buffer_limit"));
					
					prodPolicyLimitVO.setPartnerDental(rs.getString("ptr_dntl_buffer_limit"));
					prodPolicyLimitVO.setPartnerOptical(rs.getString("ptr_optc_buffer_limit"));
					prodPolicyLimitVO.setPartnerOpMaternity(rs.getString("ptr_omti_buffer_limit"));
					prodPolicyLimitVO.setPartnerOutpatient(rs.getString("ptr_opts_buffer_limit"));
					prodPolicyLimitVO.setPartnerPrescription(rs.getString("ptr_opts_pharma_buffer_limit"));
					
					/*
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
					*/
					
					
					//resultList.add(prodPolicyLimitVO);
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
					log.error("Error while closing the Resultset in PolicyDAOImpl getBenefitBufferLimitDtls()",sqlExp);
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
						log.error("Error while closing the Statement in PolicyDAOImpl getBenefitBufferLimitDtls()",sqlExp);
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
							log.error("Error while closing the Connection in PolicyDAOImpl getBenefitBufferLimitDtls()",sqlExp);
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
		return prodPolicyLimitVO;
    }

	 public ArrayList getAdmPolicySynchList(ArrayList alSearchObjects) throws TTKException {
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	        ResultSet rs = null;
	        Collection<Object> resultList = new ArrayList<Object>();
	        RuleSynchronizationVO ruleSynchVO = null;
	        try{
	        	conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strPolicySynchList);
				cStmtObject.setLong(1,(Long)alSearchObjects.get(0));
				cStmtObject.setString(2,(String)alSearchObjects.get(1));
				cStmtObject.setString(3,(String)alSearchObjects.get(2));
				cStmtObject.setString(4,(String)alSearchObjects.get(3));
				cStmtObject.setString(5,(String)alSearchObjects.get(4));
				cStmtObject.setString(6,(String)alSearchObjects.get(5));
				cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
				cStmtObject.execute();
				rs = (java.sql.ResultSet)cStmtObject.getObject(7);
				if(rs != null){
					while(rs.next()){
						ruleSynchVO = new RuleSynchronizationVO();

						if(rs.getString("POLICY_SEQ_ID") != null){
							ruleSynchVO.setPolicySeqID(new Long(rs.getLong("policy_seq_id")));
						}
						
						ruleSynchVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("policy_number")));
						
						if(rs.getString("tpa_enrollment_id") != null){
							ruleSynchVO.setTpaEnrollmentId(new Long(rs.getLong("tpa_enrollment_id")));
						}
						
						ruleSynchVO.setMemberName(TTKCommon.checkNull(rs.getString("mem_name")));
						
						if(rs.getString("member_seq_id") != null){
							ruleSynchVO.setMemberSeqId(new Long(rs.getLong("member_seq_id")));
						}
						resultList.add(ruleSynchVO);
					}//end of while(rs.next())
				}//end of if(rs != null)
	        }//end of try
	        catch (SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp)
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getAdmPolicySynchList()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null)	cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PolicyDAOImpl getAdmPolicySynchList()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getAdmPolicySynchList()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
			return (ArrayList)resultList;
	  }
	    
	 public int policySynchronizeRule(String MemberSeqIDs,Long policySeqID,Long UserSeqId) throws TTKException {
	    	int iResult = 0;
			Connection conn = null;
			CallableStatement cStmtObject=null;
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strPolSynchronizeRule);
				cStmtObject.setLong(1,policySeqID);
				cStmtObject.setString(2,MemberSeqIDs);
				
				cStmtObject.setLong(3,UserSeqId);
				cStmtObject.registerOutParameter(4,Types.BIGINT);
				cStmtObject.execute();
				iResult = cStmtObject.getInt(4);
			}//end of try
			catch (SQLException sqlExp) {
				throw new TTKException(sqlExp, "policy");
			}// end of catch (SQLException sqlExp)
			catch (Exception exp) {
				throw new TTKException(exp, "policy");
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
	        			log.error("Error while closing the Statement in PolicyDAOImpl synchronizeRule()",sqlExp);
	        			throw new TTKException(sqlExp, "policy");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PolicyDAOImpl synchronizeRule()",sqlExp);
	        				throw new TTKException(sqlExp, "policy");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "policy");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	return iResult;
	    }
	 
	 public ArrayList getModifiedMembersList(ArrayList alSearchObjects) throws TTKException {
	    	Collection<Object> alResultList = new ArrayList<Object>();
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
			ResultSet rs = null;
			BufferVO bufferVO = null;
			try{
				conn = ResourceManager.getConnection();
				cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strModifiedMembersList);
				
				if(alSearchObjects.get(0) != null)
				{
					cStmtObject.setLong(1,(Long)alSearchObjects.get(0));
				}
				else
				{
					cStmtObject.setString(1,null);
				}
				cStmtObject.setString(2,(String)alSearchObjects.get(1));
				cStmtObject.setString(3,(String)alSearchObjects.get(2));
				cStmtObject.setString(4,(String)alSearchObjects.get(3));
				cStmtObject.setString(5,(String)alSearchObjects.get(4));
				cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
				cStmtObject.execute();  
				rs = (java.sql.ResultSet)cStmtObject.getObject(6);
				if(rs != null){
					while(rs.next()){
						bufferVO = new BufferVO();
						bufferVO.setMemberId(TTKCommon.checkNull(rs.getString("tpa_enrollment_id")));
						bufferVO.setAlkootId(TTKCommon.checkNull(rs.getString("emirate_id")));
						bufferVO.setRelationship(TTKCommon.checkNull(rs.getString("relship_description")));
						bufferVO.setInceptionDate(TTKCommon.checkNull(rs.getString("Inception_Date")));
						bufferVO.setPolicyNo(TTKCommon.checkNull(rs.getString("policy_number")));
						bufferVO.setPolicyStartDate(TTKCommon.checkNull(rs.getString("Policy_Start_Date")));
						bufferVO.setPolicyEndDate(TTKCommon.checkNull(rs.getString("Policy_end_Date")));
						bufferVO.setGroupId(TTKCommon.checkNull(rs.getString("group_id")));
						bufferVO.setModifiedDate(TTKCommon.checkNull(rs.getString("Modified_Date")));
						bufferVO.setModifiedBy(TTKCommon.checkNull(rs.getString("Modified_By")));
						alResultList.add(bufferVO);
					}//end of while(rs.next())
				}//end of if(rs != null)
				return (ArrayList)alResultList;
			}//end of try
			catch (SQLException sqlExp) 
			{
				throw new TTKException(sqlExp, "policy");
			}//end of catch (SQLException sqlExp)
			catch (Exception exp) 
			{
				throw new TTKException(exp, "policy");
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
						log.error("Error while closing the Resultset in PolicyDAOImpl getModifiedMembersList()",sqlExp);
						throw new TTKException(sqlExp, "policy");
					}//end of catch (SQLException sqlExp)
					finally // Even if result set is not closed, control reaches here. Try closing the statement now.
					{
						try
						{
							if (cStmtObject != null) cStmtObject.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Statement in PolicyDAOImpl getModifiedMembersList()",sqlExp);
							throw new TTKException(sqlExp, "policy");
						}//end of catch (SQLException sqlExp)
						finally // Even if statement is not closed, control reaches here. Try closing the connection now.
						{
							try
							{
								if(conn != null) conn.close();
							}//end of try
							catch (SQLException sqlExp)
							{
								log.error("Error while closing the Connection in PolicyDAOImpl getModifiedMembersList()",sqlExp);
								throw new TTKException(sqlExp, "policy");
							}//end of catch (SQLException sqlExp)
						}//end of finally Connection Close
					}//end of finally Statement Close
				}//end of try
				catch (TTKException exp)
				{
					throw new TTKException(exp, "policy");
				}//end of catch (TTKException exp)
				finally // Control will reach here in anycase set null to the objects 
				{
					rs = null;
					cStmtObject = null;
					conn = null;
				}//end of finally
			}//end of finally
	    }//end of getModifiedMembersList(ArrayList alSearchObjects)
	
    public ArrayList getReInsurancePlans(String reinsuranceid) throws TTKException {
    	Connection conn = null;
    	PreparedStatement pStmt1 = null;
     	ResultSet rs1 = null;
    	ResultSet rs2 = null;
    	CacheObject cacheObject = null;
    	ArrayList<Object> listtreatiesPlan = null;
    	try{
    		conn = ResourceManager.getConnection();
    		pStmt1=conn.prepareStatement(strReinsurancePlanList);
			pStmt1.setString(1,reinsuranceid);
    	    rs1=pStmt1.executeQuery();
    		if(rs1 != null){
    			while(rs1.next()){
    						cacheObject = new CacheObject();
    						if(listtreatiesPlan == null){
    							listtreatiesPlan = new ArrayList<Object>();
    						}
    						cacheObject.setCacheId(TTKCommon.checkNull(rs1.getString("REINS_SEQ_ID")));
    						cacheObject.setCacheDesc(TTKCommon.checkNull(rs1.getString("TREATY_ID")));
    						listtreatiesPlan.add(cacheObject);
    					}//end of while(rs2.next())
    				}//end of if(rs2 != null)
    				
    	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "policy");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "policy");
    	}//end of catch (Exception exp)
    	finally
		{
		
    		if(rs1!=null){
    			try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    		
    		if(pStmt1 !=null){
    			try {
					pStmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    		
    		if(conn!=null){
    			try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
			
		}
    	return listtreatiesPlan;
    } 
}// End of PolicyDAOImpl
