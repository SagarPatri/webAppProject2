/**
 * @ (#) MaintenanceDAOImpl.java Oct 22, 2007
 * Project 	     : TTK HealthCare Services
 * File          : MaintenanceDAOImpl.java
 * Author        : RamaKrishna K M
 * Company       : Span Systems Corporation
 * Date Created  : Oct 22, 2007
 *
 * @author       :  RamaKrishna K M
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.dao.impl.maintenance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
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
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.sql.BLOB;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jboss.jca.adapters.jdbc.jdk6.WrappedConnectionJDK6;

import sun.misc.BASE64Encoder;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.common.CommonClosure;
import com.ttk.dto.administration.CriticalICDDetailVO;
import com.ttk.dto.administration.CriticalProcedureDetailVO;
import com.ttk.dto.administration.ProcedureDetailVO;
import com.ttk.dto.common.DhpoWebServiceVO;
import com.ttk.dto.empanelment.NotifyDetailVO;
import com.ttk.dto.finance.PharmacySearchVO;
import com.ttk.dto.finance.PharmacyVO;
import com.ttk.dto.maintenance.ClaimListVO;
//added for KOC-1273
import com.ttk.dto.maintenance.CriticalGroupVO;
import com.ttk.dto.maintenance.CustomizeConfigVO;
import com.ttk.dto.maintenance.DaycareGroupVO;
import com.ttk.dto.maintenance.ICDCodeVO;
import com.ttk.dto.maintenance.InvestigationGroupVO;
import com.ttk.dto.maintenance.PolicyListVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
//ended
public class MaintenanceDAOImpl implements BaseDAO, Serializable{
	
	private static Logger log = Logger.getLogger(MaintenanceDAOImpl.class );
	
	private static final String strDaycareGroupList = "{CALL MAINTENANCE_PKG.SELECT_DAYCARE_LIST(?,?,?,?,?,?,?)}";
	private static final String strInvestigationGroupList = "{CALL MAINTENANCE_PKG.SELECT_AGENCY_LIST(?,?,?,?,?,?,?)}"; //koc 11 koc11
	//private static final String strCriticalGroupList = "{CALL MAINTENANCE_PKG.SELECT_DAYCARE_LIST(?,?,?,?,?,?,?)}";
	
	//added for KOC-1273 - This is for getting selected records from critical group table
	private static final String strCriticalGroupList = "{CALL MAINTENANCE_PKG.SELECT_CRITICAL_LIST(?,?,?,?,?,?,?)}";

	//added for KOC-1273 - This is for saving added Critical Group Name and Critical Group description
	private static final String strSaveCriticalGroupDetail = "{CALL MAINTENANCE_PKG.SAVE_CRITICAL_GROUP(?,?,?,?,?)}";

	
	private static final String strGetDaycareGroupDetail = "{CALL MAINTENANCE_PKG.SELECT_DAYCARE_GROUP(?,?,?)}";
	private static final String strGetInvestigationGroupDetail = "{CALL MAINTENANCE_PKG.SELECT_AGENCY_DETAILS(?,?)}";  // koc 11 koc11 
	
	//added for KOC-1273 - procedure name needs to be changed according to critical group(done)
	private static final String strGetCriticalGroupDetail = "{CALL MAINTENANCE_PKG.SELECT_CRITICAL_GROUP(?,?,?)}";
	
	private static final String strSaveGroupDetail = "{CALL MAINTENANCE_PKG.SAVE_DAYCARE_GROUP(?,?,?,?,?)}";
	private static final String strSaveInvGroupDetail = "{CALL MAINTENANCE_PKG.SAVE_AGENCY_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strDeleteGroup = "{CALL MAINTENANCE_PKG.DELETE_DAYCARE_GROUP(?,?,?)}";
	private static final String strDeleteInvGroup = "{CALL MAINTENANCE_PKG.DELETE_AGENCY(?,?,?)}"; //koc 11 koc11  
	
	
	//added for KOC-1273 -- This Procedure is used to delete the critical Group
	
	private static final String strDeleteCriticalGroup = "{CALL MAINTENANCE_PKG.DELETE_CRITICAL_GROUP(?,?,?)}";
	
	private static final String strProcedureList = "{CALL MAINTENANCE_PKG.SELECT_PROCEDURE_LIST(?,?,?,?,?,?,?,?,?,?)}";
	
	
	//added for KOC-1273 - selecting the PCS code depending upon the search criteria (Procedure yet to be implemented)
	private static final String strCriticalProceList = "{CALL MAINTENANCE_PKG.SELECT_CRITICAL_PROC_LIST(?,?,?,?,?,?,?,?,?,?)}";
	
	//added for KOC-1273 - selecting the ICD Code depending upon the search criteria
	private static final String strCriticalICDList = "{CALL MAINTENANCE_PKG.SELECT_CRITICAL_ICD_LIST(?,?,?,?,?,?,?,?,?,?)}";
	
	
	private static final String strAssociateProcedure = "{CALL MAINTENANCE_PKG.ASSOCIATE_DAYCARE_PROCS(?,?,?,?)}";
	
	
	//added for KOC-1273 - associating the Critical Procedure from the unassociated List
	private static final String strAssociateCriticalProcedure = "{CALL MAINTENANCE_PKG.ASSOCIATE_CRITICAL_PROCS(?,?,?,?)}";
	
	
	//added for KOC-1273 - associating the critical ICD's from the unassociate List
	private static final String strAssociateCriticalICD = "{CALL MAINTENANCE_PKG.ASSOCIATE_CRITICAL_ICDS(?,?,?,?)}";
	
	
	private static final String strDisAssociateProcedure = "{CALL MAINTENANCE_PKG.DEASSOCIATE_DAYCARE_PROCS(?,?,?,?)}";
	
	//added for KOC-1273 - DisAssociating the Critical Procedure from associated List
	private static final String strDisAssociateCriticalProcedure = "{CALL MAINTENANCE_PKG.DEASSOCIATE_CRITICAL_PROCS(?,?,?,?)}";
	
	
	//added for KOC-1273 - DisAssociating the Critical ICD from associate list
	private static final String strDisAssociateCriticalICD = "{CALL MAINTENANCE_PKG.DEASSOCIATE_CRITICAL_ICDS(?,?,?,?)}";
	
	// Added for ICD Screen implementation by UNNI V MANA on 15-05-2008
	private static final String strSaveICDDetail = "{CALL MAINTENANCE_PKG.save_ped_code(?,?,?,?,?,?,?)}";
//	private static final String strSelectICDList = "{CALL MAINTENANCE_PKG.select_icd_list (?,?,?,?,?,?,?,?,?)}";
	private static final String strSelectICD     = "{CALL MAINTENANCE_PKG.select_icd(?,?)}";
	private static final String strPolicyList = "{CALL MAINTENANCE_PKG.SELECT_POLICY_LIST(?,?,?,?)}";
	private static final String strChangeToNonFloater = "{CALL MAINTENANCE_PKG.CHANGE_TO_PNF(?,?,?)}";
	private static final String strChangeToFloater = "{CALL MAINTENANCE_PKG.CHANGE_TO_PFL(?,?,?)}";
	private static final String strSelectNotifyList = "{CALL MAINTENANCE_PKG.SELECT_NOTIFY_LIST(?,?,?,?,?)}";
	private static final String strSaveNotificationInfo = "{CALL MAINTENANCE_PKG.SAVE_NOTIFICATION_INFO(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSelectNotificationInfo = "{CALL MAINTENANCE_PKG.SELECT_NOTIFICATION_INFO(?,?)}";
	private static final String strSelectCustomConfigList = "{CALL MAINTENANCE_PKG.SELECT_CUSTOM_CONFIG_LIST(?,?,?,?,?,?)}"; 
	
	private static final String strSaveCustomConfigInfo = "{CALL MAINTENANCE_PKG.SAVE_CUSTOM_CONFIG_INFO(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strSelectCustomConfigInfo = "{CALL MAINTENANCE_PKG.SELECT_CUSTOM_CONFIG_INFO(?,?)}";
	private static final String strPcsList = "{CALL MAINTENANCE_PKG.SELECT_PCS_LIST(?,?,?,?,?,?,?,?)}";
	private static final String strPcsDetail = "{CALL MAINTENANCE_PKG.SELECT_PCS(?,?)}";
	private static final String strSavePcsDetail = "{CALL MAINTENANCE_PKG.SAVE_PCS_CODE(?,?,?,?,?,?,?)}";	
	
//	call pending procedures
	private static final String strGetCallpendingByBranch = "{CALL SCHEDULE_PKG.GET_CALLPENDING_BY_BRANCH(?)}";
	private static final String strGetCallpendingByBranchRpt = "{CALL SCHEDULE_PKG.GET_CALLPENDING_BRANCH_REPORT(?,?,?)}";
	private static final String strProcFormMessage ="{CALL GENERATE_MAIL_PKG.PROC_FORM_MESSAGE(?,?,?,?,?,?)}" ;
	private static final String strProcCustomerFormMessage ="{CALL GENERATE_MAIL_PKG.PROC_FORM_MESSAGE(?,?,?,?,?,?,?,?)}" ;
	
	private static final String strGetMRClaimPendConfig ="{CALL SCHEDULE_PKG.GET_MR_CLAIM_PENDING_CONFIG(?,?)}" ;
	private static final String strChgPolicyDOBO = "{CALL MAINTENANCE_PKG.INS_COMP_CHANGE(?,?,?)}";
	private static final String strChgPolicyPeriod = "{CALL MAINTENANCE_PKG.POLICY_PERIOD_CHANGE(?,?,?,?,?)}";
	
	//Maintenance Claims procedures -10th August 2010
	private static final String strSelectClaimReqamt = "{CALL MAINTENANCE_PKG.SELECT_CLAIM_REQAMT(?,?)}";
	private static final String strSaveClaimReqAmt = "{CALL MAINTENANCE_PKG.SAVE_CLAIM_REQAMOUNT(?,?,?,?,?)}";
	
	//intX
	private static final String strGetCallpendingByAttendBy	=	"{CALL SCHEDULE_PKG.PROC_GEN_CALL_PENDING_REPORT(?)}";
	//private static final String strProcPreAuthUpload	=	"{CALL Pat_xml_load_pkg.load_preauth_request(?)}";
	
	//start
	private static final String strDHPOPreAuthUpload	=	"{CALL PAT_XML_LOAD_PKG.LOAD_PRIOR_REQUEST(?,?,?,?)}";
	private static final String strDHPONewTransaction	=	"{CALL CLM_XML_LOAD_PKG.SAVE_DHPO_NEW_TRANSACTIONS(?,?,?,?,?,?,?,?,?)}";
	private static final String strDHPOGlobalNetNewTransaction	="{CALL CLM_XML_LOAD_PKG.SAVE_GN_MAIN_TRANSACTIONS(?,?,?,?,?,?,?,?,?,?)}";
	private static final String strGetDHPOGlobalNetNewTransaction	="{CALL CLM_XML_LOAD_PKG.SELECT_GN_MAIN_TRANSACTIONS(?)}";
	private static final String strSaveBifurcationDetails	         ="{CALL CLM_XML_LOAD_PKG.SAVE_GN_VIDAL_TRANSACTIONS(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strDHPOPreauthDownload	             ="{CALL PAT_XML_LOAD_PKG.SAVE_PAT_DHPO_TRANSACTIONS(?,?,?,?,?,?,?,?,?)}";
	private static final String strDHPOGlobalnetPreauthDownload	             ="{CALL PAT_XML_LOAD_PKG.SAVE_GN_PAT_TRANSACTIONS(?,?,?,?,?,?,?,?,?,?)}";
	
	
	private static final String strGetDHPOGlobalNetPreAuthTransactionDetails	="{CALL PAT_XML_LOAD_PKG.SELECT_GN_PAT_TRANSACTIONS(?)}";
	private static final String strisMemberExist	="{CALL CLM_XML_LOAD_PKG.GN_VALID_MEMBER(?,?)}";
	private static final String strSavePharmacy	="{CALL MAINTENANCE_PKG.SAVE_DRUG_CODE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strPharmacyList	="{CALL MAINTENANCE_PKG.SELECT_DRUG_LIST(?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strPharmacyDetail	="{CALL MAINTENANCE_PKG.SELECT_DRUG_CODE(?,?,?)}";
	
	//Common External Document Upload/Download.
	private static final String strGetDocsUploadsList = "{CALL AUTHORIZATION_PKG.SELECT_DOCS_LIST(?,?,?)}";
	private static final String strSaveUploadFils=	"{CALL AUTHORIZATION_PKG.UPLD_CLM_PAT_DOCS(?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String strDeleteDocsUpload=    "{CALL AUTHORIZATION_PKG.DELETE_UPLD_FILE_DETAILS(?,?)}";
	private static final String strFile_data="SELECT C.IMAGE_FILE FROM APP.PAT_CLM_DOCS_TAB C WHERE C.DOCS_SEQ_ID = ?";
	private static final String strPreAuthStatusChecker="SELECT P.PAT_STATUS_TYPE_ID FROM APP.PAT_AUTHORIZATION_DETAILS P WHERE P.PRE_AUTH_NUMBER = ? ";
	private static final String strClaimStatusChecker="SELECT C.CLM_STATUS_TYPE_ID FROM APP.CLM_AUTHORIZATION_DETAILS C WHERE C.CLAIM_NUMBER = ? ";
	/**
     * This method returns the Arraylist of DaycareGroupVO's  which contains Daycare Group details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of DaycareGroupVO object which contains Daycare Group details
     * @exception throws TTKException
     */
    public ArrayList getGroupList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		DaycareGroupVO daycareGroupVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDaycareGroupList);
			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//GROUP_NAME
			cStmtObject.setString(2,(String)alSearchCriteria.get(2));//SORT_VAR
			cStmtObject.setString(3,(String)alSearchCriteria.get(3));//SORT_ORDER
			cStmtObject.setString(4,(String)alSearchCriteria.get(4));//START_NUM
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));//END_NUM
			cStmtObject.setLong(6,(Long)alSearchCriteria.get(1));//UPDATED_BY
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(7);
			
			if(rs != null){
				while(rs.next()){
					daycareGroupVO = new DaycareGroupVO();

					daycareGroupVO.setGroupID(TTKCommon.checkNull(rs.getString("DAY_CARE_GROUP_ID")));
					daycareGroupVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					daycareGroupVO.setGroupDesc(TTKCommon.checkNull(rs.getString("GROUP_DESCRIPTION")));
					
					alResultList.add(daycareGroupVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getGroupList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getGroupList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getGroupList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getGroupList(ArrayList alSearchCriteria)
    
    
    /**added for KOC-1273
     * This method returns the Arraylist of CriticalGroupVO's  which contains Critical ICD/CS Group details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of CriticalGroupVO object which contains Critical ICD/PCS Group details
     * @exception throws TTKException
     */
    public ArrayList getCriticalGroupList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CriticalGroupVO criticalGroupVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCriticalGroupList);
			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//GROUP_NAME
			cStmtObject.setString(2,(String)alSearchCriteria.get(2));//SORT_VAR
			cStmtObject.setString(3,(String)alSearchCriteria.get(3));//SORT_ORDER
			cStmtObject.setString(4,(String)alSearchCriteria.get(4));//START_NUM
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));//END_NUM
			cStmtObject.setLong(6,(Long)alSearchCriteria.get(1));//UPDATED_BY
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(7);
			
			if(rs != null){
				while(rs.next()){
					criticalGroupVO = new CriticalGroupVO();

					criticalGroupVO.setGroupID(TTKCommon.checkNull(rs.getString("CRITICAL_GROUP_ID")));
					criticalGroupVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					criticalGroupVO.setGroupDesc(TTKCommon.checkNull(rs.getString("GROUP_DESCRIPTION")));
					
					alResultList.add(criticalGroupVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCriticalGroupList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCriticalGroupList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCriticalGroupList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getCriticalGroupList(ArrayList alSearchCriteria)
    
	//koc 11 koc11 s
    public ArrayList getInvGroupList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		InvestigationGroupVO investigationGroupVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strInvestigationGroupList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//aGENCY_NAME
			cStmtObject.setString(2,(String)alSearchCriteria.get(2));//SORT_VAR
			cStmtObject.setString(3,(String)alSearchCriteria.get(3));//SORT_ORDER
			cStmtObject.setString(4,(String)alSearchCriteria.get(4));//START_NUM
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));//END_NUM
			cStmtObject.setLong(6,(Long)alSearchCriteria.get(1));//UPDATED_BY
			cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(7);
			
			if(rs != null){
				while(rs.next()){
					investigationGroupVO = new InvestigationGroupVO();

					investigationGroupVO.setGroupID(TTKCommon.checkNull(rs.getString("INVEST_AGNCY_SEQ_ID")));
					investigationGroupVO.setGroupName(TTKCommon.checkNull(rs.getString("AGENCY_NAME")));
					//daycareGroupVO.setGroupDesc(TTKCommon.checkNull(rs.getString("GROUP_DESCRIPTION")));
					
					alResultList.add(investigationGroupVO);
				}//end of while(rs.next())
			}//end of if(rs != null)	
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getGroupList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getGroupList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getGroupList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getGroupList(ArrayList alSearchCriteria)
    //koc 11 koc11 e
    
    /**
     * This method returns the PreAuthDetailVO, which contains Daycare Group details
     * @param strGroupID GroupID
     * @param lngUserSeqID long value which contains Logged-in User
     * @return DaycareGroupVO object which contains Daycare Group details
     * @exception throws TTKException
     */
    public DaycareGroupVO getGroupDetail(String strGroupID,long lngUserSeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	DaycareGroupVO daycareGroupVO = null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDaycareGroupDetail);
    		
    		cStmtObject.setString(1,strGroupID);
    		cStmtObject.setLong(2,lngUserSeqID);
    		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(rs != null){
				while(rs.next()){
					daycareGroupVO = new DaycareGroupVO();
					
					daycareGroupVO.setGroupID(TTKCommon.checkNull(rs.getString("DAY_CARE_GROUP_ID")));
					daycareGroupVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					daycareGroupVO.setGroupDesc(TTKCommon.checkNull(rs.getString("GROUP_DESCRIPTION")));
				}//end of while(rs.next())
			}//end of if(rs != null)
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getGroupDetail()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getGroupDetail()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getGroupDetail()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    	return daycareGroupVO;
    }//end of getGroupDetail(String strGroupID,long lngUserSeqID)
    
    
    /**added for KOC-1273
     * This method returns the PreAuthDetailVO, which contains Daycare Group details
     * @param strGroupID GroupID
     * @param lngUserSeqID long value which contains Logged-in User
     * @return DaycareGroupVO object which contains Daycare Group details
     * @exception throws TTKException
     */
    public CriticalGroupVO getCriticalGroupDetail(String strGroupID,long lngUserSeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	CriticalGroupVO criticalGroupVO = null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetCriticalGroupDetail);
    		
    		cStmtObject.setString(1,strGroupID);
    		cStmtObject.setLong(2,lngUserSeqID);
    		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(rs != null){
				while(rs.next()){
					criticalGroupVO = new CriticalGroupVO();
					
					criticalGroupVO.setGroupID(TTKCommon.checkNull(rs.getString("CRITICAL_GROUP_ID")));
					criticalGroupVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					criticalGroupVO.setGroupDesc(TTKCommon.checkNull(rs.getString("GROUP_DESCRIPTION")));
				}//end of while(rs.next())
			}//end of if(rs != null)
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCriticalGroupDetail()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCriticalGroupDetail()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCriticalGroupDetail()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    	return criticalGroupVO;
    }//end of getCriticalGroupDetail(String strGroupID,long lngUserSeqID)

	public InvestigationGroupVO getInvGroupDetail(String strGroupID,long lngUserSeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	InvestigationGroupVO investigationGroupVO = null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetInvestigationGroupDetail);
    		
    		cStmtObject.setString(1,strGroupID);
    		//cStmtObject.setLong(2,lngUserSeqID);
    		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			
			if(rs != null){
				while(rs.next()){
					investigationGroupVO = new InvestigationGroupVO();
					
					investigationGroupVO.setGroupID(TTKCommon.checkNull(rs.getString("INVEST_AGNCY_SEQ_ID")));
					investigationGroupVO.setGroupName(TTKCommon.checkNull(rs.getString("AGENCY_NAME")));
				//	investigationGroupVO.setInvActiveYN(TTKCommon.checkNull(rs.getString("AGENCY_EMPANELED_DATE")));agencyEmpanelDate, setAgencyEmpanelTime, 
					if(rs.getString("AGENCY_EMPANELED_DATE") != null){
						investigationGroupVO.setAgencyEmpanelDate(new Date(rs.getTimestamp("AGENCY_EMPANELED_DATE").getTime()));
						investigationGroupVO.setAgencyEmpanelTime(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("AGENCY_EMPANELED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("AGENCY_EMPANELED_DATE").getTime())).split(" ")[1]:"");
						investigationGroupVO.setAgencyEmpanelDay(TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("AGENCY_EMPANELED_DATE").getTime())).split(" ").length >=3 ? TTKCommon.getFormattedDateHour(new Date(rs.getTimestamp("AGENCY_EMPANELED_DATE").getTime())).split(" ")[2]:"");
					}//end of if(rs.getString("AGENCY_EMPANELED_DATE") != null)
					investigationGroupVO.setAddress1(TTKCommon.checkNull(rs.getString("ADDRESS_1")));
					investigationGroupVO.setAddress2(TTKCommon.checkNull(rs.getString("ADDRESS_2")));
					investigationGroupVO.setAddress3(TTKCommon.checkNull(rs.getString("ADDRESS_3")));
					investigationGroupVO.setStateTypeId(TTKCommon.checkNull(rs.getString("STATE_TYPE_ID")));
					investigationGroupVO.setCityTypeId(TTKCommon.checkNull(rs.getString("CITY_TYPE_ID")));
					investigationGroupVO.setPin_code(TTKCommon.checkNull(rs.getString("PIN_CODE")));
					investigationGroupVO.setCountryId(TTKCommon.checkNull(rs.getString("COUNTRY_ID")));
					investigationGroupVO.setOfficePhoneNo1(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_1")));
					investigationGroupVO.setOfficePhoneNo2(TTKCommon.checkNull(rs.getString("OFF_PHONE_NO_2")));
					investigationGroupVO.setMobileNo(TTKCommon.checkNull(rs.getString("MOBILE_NO")));
					investigationGroupVO.setFaxNo(TTKCommon.checkNull(rs.getString("FAX_NO")));
					investigationGroupVO.setEmail1(TTKCommon.checkNull(rs.getString("PRIMARY_EMAIL_ID")));
					investigationGroupVO.setEmail2(TTKCommon.checkNull(rs.getString("SECONDARY_EMAIL_ID")));
					//investigationGroupVO.set(TTKCommon.checkNull(rs.getString("ADDED_BY")));
					investigationGroupVO.setInvActiveYN(TTKCommon.checkNull(rs.getString("ACTIVE_YN")));
                }//end of while(rs.next())
			}//end of if(rs != null)
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getGroupDetail()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getGroupDetail()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getGroupDetail()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    	return investigationGroupVO;
    }//end of getGroupDetail(String strGroupID,long lngUserSeqID)
    
    /**
	 * This method saves the Daycare Group information
	 * @param daycareGroupVO DaycareGroupVO contains Daycare Group information
	 * @return int value, greater than zero indicates successful execution of the task
	 * @exception throws TTKException
	 */
	public int saveGroupDetail(DaycareGroupVO daycareGroupVO) throws TTKException {
		int iResult =0;
	   	Connection conn = null;
	   	CallableStatement cStmtObject=null;
	   	String strGroupID = "";
	   	try{
	   		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSaveGroupDetail);
    		
    		if(daycareGroupVO.getGroupID() != null){
    			cStmtObject.setString(1,daycareGroupVO.getGroupID());
    		}//end of if(daycareGroupVO.getGroupID() != null)
    		else{
    			cStmtObject.setString(1,null);
    		}//end of else
    		
    		cStmtObject.setString(2,daycareGroupVO.getGroupName());
    		cStmtObject.setString(3,daycareGroupVO.getGroupDesc());
    		cStmtObject.setLong(4,daycareGroupVO.getUpdatedBy());
    		cStmtObject.registerOutParameter(5,Types.INTEGER);
            cStmtObject.registerOutParameter(1,Types.VARCHAR);
    		cStmtObject.execute();
    		iResult = cStmtObject.getInt(5);
    		strGroupID = cStmtObject.getString(1);
    		log.debug("strGroupID value is :"+strGroupID);
    	}//end of try
	   	catch (SQLException sqlExp) 
    	{ 
    		throw new TTKException(sqlExp, "maintenancerules");
    	}//end of catch (SQLException sqlExp) 
    	catch (Exception exp) 
    	{
    		throw new TTKException(exp, "maintenancerules");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupDetail()",sqlExp);
        			throw new TTKException(sqlExp, "maintenancerules");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl saveGroupDetail()",sqlExp);
        				throw new TTKException(sqlExp, "maintenancerules");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenancerules");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
	}//end of saveGroupDetail(DaycareGroupVO daycareGroupVO)
	
	public int saveInvGroupDetail(InvestigationGroupVO investigationGroupVO) throws TTKException {
		int iResult =0;
	   	Connection conn = null;
	   	CallableStatement cStmtObject=null;
	   	String strGroupID = "";
	   	try{
	   		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSaveInvGroupDetail);
    		log.info(investigationGroupVO.toString()+"--------------------start---- :"+iResult);
    		if(investigationGroupVO.getGroupID() != null){
    			cStmtObject.setString(1,investigationGroupVO.getGroupID());
    		}//end of if(daycareGroupVO.getGroupID() != null)
    		else{
    			cStmtObject.setString(1,null);
    		}//end of else
    		
    		cStmtObject.setString(2,investigationGroupVO.getGroupName());
    		log.info(investigationGroupVO.getGroupID()+"-----------------"+investigationGroupVO.getGroupName()+"----------------"+investigationGroupVO.getAgencyEmpanelDate());
    		//cStmtObject.setString(3,investigationGroupVO.getAgencyEmpanelDate());
    		 if(investigationGroupVO.getInvestStringDate() != null){
                 cStmtObject.setTimestamp(3,new Timestamp(TTKCommon.getOracleDateWithTime(investigationGroupVO.getInvestStringDate(),investigationGroupVO.getAgencyEmpanelTime(),investigationGroupVO.getAgencyEmpanelDay()).getTime()));
             }//end of if(investigationVO.getMarkedDate() != null)
             else{
                 cStmtObject.setTimestamp(3, null);
             }//end of else  		    		    		
    	//	cStmtObject.setTimestamp(3,new Timestamp(TTKCommon.getOracleDateWithTime(investigationGroupVO.getAgencyEmpanelDate(),investigationGroupVO.getAgencyEmpanelTime(),investigationGroupVO.getAgencyEmpanelDay()).getTime()));//MARKED_DATE    		
    		cStmtObject.setString(4,investigationGroupVO.getAddress1());
    		cStmtObject.setString(5,investigationGroupVO.getAddress2());
    		cStmtObject.setString(6,investigationGroupVO.getAddress3());
    		cStmtObject.setString(7,investigationGroupVO.getStateTypeId());
    		cStmtObject.setString(8,investigationGroupVO.getCityTypeId());
    		cStmtObject.setString(9,investigationGroupVO.getPin_code());
    		cStmtObject.setString(10,investigationGroupVO.getCountryId());
    		cStmtObject.setString(11,investigationGroupVO.getOfficePhoneNo1());
    		cStmtObject.setString(12,investigationGroupVO.getOfficePhoneNo2());
    		cStmtObject.setString(13,investigationGroupVO.getFaxNo());
    		cStmtObject.setString(14,investigationGroupVO.getMobileNo());
    		cStmtObject.setString(15,investigationGroupVO.getEmail1());
    		cStmtObject.setString(16,investigationGroupVO.getEmail2());  
    		cStmtObject.setString(17,investigationGroupVO.getInvActiveYN());  
    		cStmtObject.setLong(18,investigationGroupVO.getUpdatedBy());
    		//cStmtObject.registerOutParameter(5,Types.INTEGER);
            cStmtObject.registerOutParameter(19,Types.INTEGER);
    		cStmtObject.execute();
    		iResult = cStmtObject.getInt(19);
    		//strGroupID = cStmtObject.getString(1);
    		log.info("---------------end  :"+iResult);
    	}//end of try
	   	catch (SQLException sqlExp) 
    	{ 
    		throw new TTKException(sqlExp, "maintenancerules");
    	}//end of catch (SQLException sqlExp) 
    	catch (Exception exp) 
    	{
    		throw new TTKException(exp, "maintenancerules");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveGroupDetail()",sqlExp);
        			throw new TTKException(sqlExp, "maintenancerules");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl saveGroupDetail()",sqlExp);
        				throw new TTKException(sqlExp, "maintenancerules");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenancerules");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
	}//end of saveGroupDetail(DaycareGroupVO daycareGroupVO)
	
	/**added for KOC-1273
	 * This method saves the Critical ICD/PCS Group information
	 * @param criticalGroupVO CriticalGroupVO contains Daycare Group information
	 * @return int value, greater than zero indicates successful execution of the task
	 * @exception throws TTKException
	 */
	public int saveCriticalGroupDetail(CriticalGroupVO criticalGroupVO) throws TTKException {
		int iResult =0;
	   	Connection conn = null;
	   	CallableStatement cStmtObject=null;
	   	String strGroupID = "";
	   	try{
	   		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSaveCriticalGroupDetail);
    		
    		if(criticalGroupVO.getGroupID() != null){
    			cStmtObject.setString(1,criticalGroupVO.getGroupID());
    		}//end of if(daycareGroupVO.getGroupID() != null)
    		else{
    			cStmtObject.setString(1,null);
    		}//end of else
    		
    		cStmtObject.setString(2,criticalGroupVO.getGroupName());
    		cStmtObject.setString(3,criticalGroupVO.getGroupDesc());
    		cStmtObject.setLong(4,criticalGroupVO.getUpdatedBy());
    		cStmtObject.registerOutParameter(5,Types.INTEGER);
            cStmtObject.registerOutParameter(1,Types.VARCHAR);
    		cStmtObject.execute();
    		iResult = cStmtObject.getInt(5);
    		strGroupID = cStmtObject.getString(1);
    		log.debug("strGroupID value is :"+strGroupID);
    	}//end of try
	   	catch (SQLException sqlExp) 
    	{ 
    		throw new TTKException(sqlExp, "maintenancerules");
    	}//end of catch (SQLException sqlExp) 
    	catch (Exception exp) 
    	{
    		throw new TTKException(exp, "maintenancerules");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveCriticalGroupDetail()",sqlExp);
        			throw new TTKException(sqlExp, "maintenancerules");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl saveCriticalGroupDetail()",sqlExp);
        				throw new TTKException(sqlExp, "maintenancerules");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenancerules");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
	}//end of saveCriticalGroupDetail(CriticalGroupVO criticalGroupVO)
	
	
	
	/**
     * This method Deletes the Daycare Group(s).
     * @param alGroupList ArrayList object which contains the daycar group id's to be deleted
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int deleteGroup(ArrayList alGroupList) throws TTKException {
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	 if(alGroupList != null && alGroupList.size() > 0)
             {
                 conn = ResourceManager.getConnection();
                 cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteGroup);
                 
                 cStmtObject.setString(1, (String)alGroupList.get(0));//string of daycare group id's which are separated with | as separator (Note: String should also end with | at the end)
                 cStmtObject.setLong(2, (Long)alGroupList.get(1));//user sequence id
                 cStmtObject.registerOutParameter(3, Types.INTEGER);//out parameter which gives the number of records deleted
                 cStmtObject.execute();
                 iResult = cStmtObject.getInt(3);
             }//end of if(alGroupList != null && alGroupList.size() > 0)
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl deleteGroup()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl deleteGroup()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//end of deleteGroup(ArrayList alGroupList)
	
	public int deleteInvGroup(ArrayList alGroupList) throws TTKException {
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	 if(alGroupList != null && alGroupList.size() > 0)
             {
                 conn = ResourceManager.getConnection();
                 cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteInvGroup);
                 
                 cStmtObject.setString(1, (String)alGroupList.get(0));//string of daycare group id's which are separated with | as separator (Note: String should also end with | at the end)
                 cStmtObject.setLong(2, (Long)alGroupList.get(1));//user sequence id
                 cStmtObject.registerOutParameter(3, Types.INTEGER);//out parameter which gives the number of records deleted
                 cStmtObject.execute();
                 iResult = cStmtObject.getInt(3);
             }//end of if(alGroupList != null && alGroupList.size() > 0)
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl deleteGroup()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl deleteGroup()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//end of deleteGroup(ArrayList alGroupList)
    
    /**added for KOC-1273
     * This method Deletes the Critical Group(s).
     * @param alGroupList ArrayList object which contains the critical group id's to be deleted
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int deleteCriticalGroup(ArrayList alCriticalGroupList) throws TTKException {
    	
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	if(alCriticalGroupList != null && alCriticalGroupList.size() > 0)
            {
        		conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDeleteCriticalGroup);
                log.info("1---(String)alCriticalGroupList.get(0)--:"+(String)alCriticalGroupList.get(0));
                log.info("2---(Long)alCriticalGroupList.get(1)--:"+(Long)alCriticalGroupList.get(1));
                cStmtObject.setString(1, (String)alCriticalGroupList.get(0));//string of critical group id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setLong(2, (Long)alCriticalGroupList.get(1));//user sequence id
                cStmtObject.registerOutParameter(3, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(3);
            }
        	
        }
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl deleteCriticalGroup()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl deleteCriticalGroup()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    
    	
    }
    
    /**
     * This method returns the Arraylist of ProcedureDetailVO's  which contains Daycare Group Procedure details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ProcedureDetailVO object which contains Daycare Group Procedure details
     * @exception throws TTKException
     */
    public ArrayList getProcedureList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		ProcedureDetailVO procedureDetailVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strProcedureList);
			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//DAY_CARE_GROUP_ID
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//PROC_CODE
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//PROC_DESCRIPTION
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//SELECTION_TYPE
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));//SORT_VAR
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));//SORT_ORDER
			cStmtObject.setString(7,(String)alSearchCriteria.get(7));//START_NUM
			cStmtObject.setString(8,(String)alSearchCriteria.get(8));//END_NUM
			cStmtObject.setLong(9,(Long)alSearchCriteria.get(4));//UPDATED_BY
			cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(10);
			
			if(rs != null){
				while(rs.next()){
					procedureDetailVO = new ProcedureDetailVO();
					
					if (rs.getString("PROC_SEQ_ID") != null){
	                    procedureDetailVO.setProcedureID(new Long(rs.getLong("PROC_SEQ_ID")));
                    }//end of if (rs.getString("PROC_SEQ_ID") != null)
	                procedureDetailVO.setProcedureCode(TTKCommon.checkNull(rs.getString("PROC_CODE")));
	                procedureDetailVO.setProcedureDescription(TTKCommon.checkNull(rs.getString("PROC_DESCRIPTION")));
					
	                alResultList.add(procedureDetailVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getGroupList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getGroupList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getGroupList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getProcedureList(ArrayList alSearchCriteria)
    
    
    /**added for KOC-1273
     * This method returns the Arraylist of CriticalProcedureDetailVO's  which contains Critical Group Procedure details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of CriticalProcedureDetailVO object which contains Critical Group Procedure details
     * @exception throws TTKException
     */
    public ArrayList getCriticalProcedureList(ArrayList alSearchCriteria) throws TTKException {
    	
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CriticalProcedureDetailVO criticalprocedureDetailVO = null;	
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCriticalProceList);
    		cStmtObject.setString(1,(String)alSearchCriteria.get(0));//CRITICAL_GROUP_ID
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//PROC_CODE
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//PROC_DESCRIPTION
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//SELECTION_TYPE
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));//SORT_VAR
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));//SORT_ORDER
			cStmtObject.setString(7,(String)alSearchCriteria.get(7));//START_NUM
			cStmtObject.setString(8,(String)alSearchCriteria.get(8));//END_NUM
			cStmtObject.setLong(9,(Long)alSearchCriteria.get(4));//UPDATED_BY
			cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(10);
			if(rs != null){
				while(rs.next()){					
					criticalprocedureDetailVO = new CriticalProcedureDetailVO();
					if (rs.getString("PROC_SEQ_ID") != null){
						criticalprocedureDetailVO.setProcedureID(new Long(rs.getLong("PROC_SEQ_ID")));
                    }//end of if (rs.getString("PROC_SEQ_ID") != null)
					criticalprocedureDetailVO.setProcedureCode(TTKCommon.checkNull(rs.getString("PROC_CODE")));
					criticalprocedureDetailVO.setProcedureDescription(TTKCommon.checkNull(rs.getString("PROC_DESCRIPTION")));
					
	                alResultList.add(criticalprocedureDetailVO);
				}
			}
			return (ArrayList)alResultList;	
    	}
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCriticalProcedureList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCriticalProcedureList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCriticalProcedureList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }
    
    //added for KOC-1273
    
    /**
     * This method returns the Arraylist of CriticalICDDetailVO's  which contains Critical ICD Group details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of CriticalICDDetailVO object which contains Critical ICD Group details
     * @exception throws TTKException
     */
    public ArrayList getCriticalICDList(ArrayList alSearchCriteria) throws TTKException {
    	
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CriticalICDDetailVO criticalICDDetailVO = null;	
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCriticalICDList);
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		cStmtObject.setString(1,(String)alSearchCriteria.get(0));//CRITICAL_GROUP_ID
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//PROC_CODE
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//PROC_DESCRIPTION
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//SELECTION_TYPE
			cStmtObject.setString(5,"ICD_CODE");//SORT_VAR
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));//SORT_ORDER
			cStmtObject.setString(7,(String)alSearchCriteria.get(7));//START_NUM
			cStmtObject.setString(8,(String)alSearchCriteria.get(8));//END_NUM
			cStmtObject.setLong(9,(Long)alSearchCriteria.get(4));//UPDATED_BY
			cStmtObject.registerOutParameter(10,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(10);
			if(rs != null){
				while(rs.next()){					
					criticalICDDetailVO = new CriticalICDDetailVO();
					if (rs.getString("PED_CODE_ID") != null){
						criticalICDDetailVO.setProcedureID(new Long(rs.getLong("PED_CODE_ID")));
                    }//end of if (rs.getString("PROC_SEQ_ID") != null)
					criticalICDDetailVO.setProcedureCode(TTKCommon.checkNull(rs.getString("ICD_CODE")));
					criticalICDDetailVO.setProcedureDescription(TTKCommon.checkNull(rs.getString("ped_description")));
					
	                alResultList.add(criticalICDDetailVO);
				}
			}
			return (ArrayList)alResultList;	
    	}
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCriticalICDList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCriticalICDList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCriticalICDList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }
   
    //ended
    
    
    /**
     * This method associate the procedure(s) to the Group ID.
     * @param alProcedureList ArrayList object which contains the procedure sequence id's to be associated
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int associateProcedure(ArrayList alProcedureList) throws TTKException {
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	if(alProcedureList != null && alProcedureList.size() > 0)
            {
                conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAssociateProcedure);
                
                cStmtObject.setString(1, (String)alProcedureList.get(0));//DAY_CARE_GROUP_ID
                cStmtObject.setString(2, (String)alProcedureList.get(1));//string of proc_seq_id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setLong(3, (Long)alProcedureList.get(2));//user sequence id
                cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(4);
            }//end of if(alProcedureList != null && alProcedureList.size() > 0)
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl associateProcedure()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl associateProcedure()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
    }//end of associateProcedure(ArrayList alProcedureList)
    
    /**added for KOC-1273
     * This method associate the procedure(s) to the Group ID.
     * @param alProcedureList ArrayList object which contains the procedure sequence id's to be associated
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    
    public int associateCriticalProcedure(ArrayList alProcedureList) throws TTKException {
    	
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	if(alProcedureList != null && alProcedureList.size() > 0)
        	{
        		conn = ResourceManager.getConnection();
        		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAssociateCriticalProcedure);
        		cStmtObject.setString(1, (String)alProcedureList.get(0));//DAY_CARE_GROUP_ID
                cStmtObject.setString(2, (String)alProcedureList.get(1));//string of proc_seq_id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setLong(3, (Long)alProcedureList.get(2));//user sequence id
                cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(4);
        	}
        	
        	
        }
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl associateCriticalProcedure()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl associateCriticalProcedure()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
  }
    
    //added for KOC-1273
    
    /**
     * This method associate the ICD(s) to the Group ID.
     * @param alCriticalICDList ArrayList object which contains the ICD sequence id's to be associated
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    
    public int associateCriticalICD(ArrayList alCriticalICDList) throws TTKException {
    	
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	if(alCriticalICDList != null && alCriticalICDList.size() > 0)
        	{
        		conn = ResourceManager.getConnection();
        		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strAssociateCriticalICD);
        		
        		
        		
        		cStmtObject.setString(1, (String)alCriticalICDList.get(0));//DAY_CARE_GROUP_ID
                cStmtObject.setString(2, (String)alCriticalICDList.get(1));//string of proc_seq_id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setLong(3, (Long)alCriticalICDList.get(2));//user sequence id
                cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(4);
        	}
        	
        	
        }
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl associateCriticalICD()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl associateCriticalICD()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
  }
   
    
    //ended
    
    /**
     * This method DisAssociate the procedure(s) to the Group ID.
     * @param alProcedureList ArrayList object which contains the procedure sequence id's to be disassociated
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int disAssociateProcedure(ArrayList alProcedureList) throws TTKException {
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
        	if(alProcedureList != null && alProcedureList.size() > 0)
            {
                conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDisAssociateProcedure);
                
                cStmtObject.setString(1, (String)alProcedureList.get(0));//DAY_CARE_GROUP_ID
                cStmtObject.setString(2, (String)alProcedureList.get(1));//string of proc_seq_id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setLong(3, (Long)alProcedureList.get(2));//user sequence id
                cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(4);
            }//end of if(alProcedureList != null && alProcedureList.size() > 0)
        }//end of try
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl disAssociateProcedure()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl disAssociateProcedure()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
    }//end of disAssociateProcedure(ArrayList alProcedureList)
    
    /**added for KOC-1273
     * This method DisAssociate the procedure(s) to the Group ID.
     * @param alProcedureList ArrayList object which contains the procedure sequence id's to be disassociated
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int disAssociateCriticalProcedure(ArrayList alProcedureList) throws TTKException {
    	
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        
        try
        {
        	if(alProcedureList != null && alProcedureList.size() > 0)
            {
                conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDisAssociateCriticalProcedure);
                
                cStmtObject.setString(1, (String)alProcedureList.get(0));//CRITICAL_GROUP_ID
                cStmtObject.setString(2, (String)alProcedureList.get(1));//string of proc_seq_id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setLong(3, (Long)alProcedureList.get(2));//user sequence id
                cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(4);
            }//end of if(alProcedureList != null && alProcedureList.size() > 0)
        }
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl disAssociateCriticalProcedure()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl disAssociateCriticalProcedure()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;    	
    	
    }
    
    //added for KOC-1273
    
    /**
     * This method DisAssociate the ICD(s) to the Group ID.
     * @param alCriticalICDList ArrayList object which contains the icd sequence id's to be disassociated
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    
public int disAssociateCriticalICD(ArrayList alCriticalICDList) throws TTKException {
    	
    	int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        
        try
        {
        	if(alCriticalICDList != null && alCriticalICDList.size() > 0)
            {
                conn = ResourceManager.getConnection();
                cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strDisAssociateCriticalICD);
                
                cStmtObject.setString(1, (String)alCriticalICDList.get(0));//CRITICAL_GROUP_ID
                cStmtObject.setString(2, (String)alCriticalICDList.get(1));//string of proc_seq_id's which are separated with | as separator (Note: String should also end with | at the end)
                cStmtObject.setLong(3, (Long)alCriticalICDList.get(2));//user sequence id
                cStmtObject.registerOutParameter(4, Types.INTEGER);//out parameter which gives the number of records deleted
                cStmtObject.execute();
                iResult = cStmtObject.getInt(4);
            }//end of if(alProcedureList != null && alProcedureList.size() > 0)
        }
        catch (SQLException sqlExp)
        {
              throw new TTKException(sqlExp, "maintenance");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
              throw new TTKException(exp, "maintenance");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl disAssociateCriticalICD()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl disAssociateCriticalICD()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenance");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;    	
    	
    }
    
    
    //ended
    
    
//    public static void main(String a[]) throws Exception {
//    	MaintenanceDAOImpl maintenanceDAO = new MaintenanceDAOImpl();
    	
    	//maintenanceDAO.getGroupDetail("GR1",new Long(56503));
    	
    	/*DaycareGroupVO daycareGroupVO = new DaycareGroupVO();
    	daycareGroupVO.setGroupName("GROUP 2");
    	daycareGroupVO.setGroupDesc("GROUP 2");
    	daycareGroupVO.setUpdatedBy(new Long(56503));
    	maintenanceDAO.saveGroupDetail(daycareGroupVO);*/
    	
//    	ArrayList<Object> alSearchCriteria = new ArrayList<Object>();
//    	alSearchCriteria.add("100");
//    	alSearchCriteria.add("E110");
//    	alSearchCriteria.add("HE");
//    	alSearchCriteria.add("");
//    	alSearchCriteria.add("");
//    	alSearchCriteria.add("");
//    	alSearchCriteria.add("");
//    	alSearchCriteria.add(new Long(56503));

    	
    	/*ArrayList<Object> alProcedureList = new ArrayList<Object>();
    	alProcedureList.add("GR0004");
    	alProcedureList.add("|1593|00.01|1594|00.02|1595|00.03|");
    	alProcedureList.add(new Long(56503));
    	maintenanceDAO.associateProcedure(alProcedureList);*/
    	
    	/*ArrayList<Object> alProcedureList = new ArrayList<Object>();
    	alProcedureList.add("GR0004");
    	alProcedureList.add("|1595|");
    	alProcedureList.add(new Long(56503));
    	maintenanceDAO.disAssociateProcedure(alProcedureList);*/
    	
//    }//end of main
    
    /**
     * 
     * @return
     * @throws TTKException
     */
    public int saveICDDetails(ICDCodeVO icdCodeVO) throws TTKException
    {
    	int pedCode=-1;
        Connection conn = null;
        CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveICDDetail);

            
            if(icdCodeVO.getPedCode()==null)
            {
            	log.debug("Add scenario ");
            	cStmtObject.setLong(1, 0);
            }
            else
            {
            	log.debug("update scenario "+icdCodeVO.getPedCode().longValue());
            	cStmtObject.setLong(1, icdCodeVO.getPedCode());
            }
            
            cStmtObject.setString(2,icdCodeVO.getIcdCode());
            cStmtObject.setString(3,icdCodeVO.getDescription());
            cStmtObject.setString(4,(icdCodeVO.getMostCommon()==null || "N".equalsIgnoreCase(icdCodeVO.getMostCommon()))?"N":"Y");
            cStmtObject.setLong(5,icdCodeVO.getUpdatedBy());
            if(icdCodeVO.getMasterPedCode()==null || icdCodeVO.getMasterPedCode()==0)
            {
            	cStmtObject.setNull(6, Types.NULL);
            }//end of if(icdCodeVO.getMasterPedCode()==null || icdCodeVO.getMasterPedCode()==0)
            else
            {
            cStmtObject.setLong(6,icdCodeVO.getMasterPedCode());
            }//end of else
            cStmtObject.registerOutParameter(1,Types.INTEGER);
            cStmtObject.registerOutParameter(7,Types.INTEGER);
            cStmtObject.execute();
            pedCode = cStmtObject.getInt(1);
    	} catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "maintenanceicd");
      }//end of catch (SQLException sqlExp)
      catch (Exception exp)
      {
            throw new TTKException(exp, "maintenanceicd");
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
      			log.error("Error while closing the Statement in MaintenanceDAOImpl saveICDDetails()",sqlExp);
      			throw new TTKException(sqlExp, "maintenanceicd");
      		}//end of catch (SQLException sqlExp)
      		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
      		{
      			try
      			{
      				if(conn != null) conn.close();
      			}//end of try
      			catch (SQLException sqlExp)
      			{
      				log.error("Error while closing the Connection in MaintenanceDAOImpl saveICDDetails()",sqlExp);
      				throw new TTKException(sqlExp, "maintenanceicd");
      			}//end of catch (SQLException sqlExp)
      		}//end of finally Connection Close
      	}//end of try
      	catch (TTKException exp)
      	{
      		throw new TTKException(exp, "maintenanceicd");
      	}//end of catch (TTKException exp)
      	finally // Control will reach here in any case set null to the objects 
      	{
      		cStmtObject = null;
      		conn = null;
      	}//end of finally
		}//end of finally
  	return pedCode;
    }
    

    /**
     * This method fetch ICD Code details to be displayed for editing
     * @param pedCode
     * @return ICDCodeVO
     * @throws TTKException
     */	
    public ICDCodeVO selectICD(Long pedCode) throws TTKException
    {
        Connection conn 			  = null;
        CallableStatement cStmtObject = null;
        ICDCodeVO iCDCodeVO 		  = null;
    	try{
    		conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectICD);
			cStmtObject.setLong(1,pedCode); 
			cStmtObject.registerOutParameter(2, OracleTypes.CURSOR);
			cStmtObject.execute();
			ResultSet rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			while(rs.next())
			{
				iCDCodeVO = new ICDCodeVO();
				iCDCodeVO.setPedCode(rs.getLong("PED_CODE_ID"));
				iCDCodeVO.setIcdCode(rs.getString("ICD_CODE"));
				iCDCodeVO.setDescription(rs.getString("PED_DESCRIPTION"));
				iCDCodeVO.setMasterPedCode(rs.getLong("MASTER_PED_CODE_ID"));
				iCDCodeVO.setMostCommon(rs.getString("MOST_COMMON_YN"));
				iCDCodeVO.setMasterIcdCode(rs.getString("MASTER_ICD_CODE"));			}
    	}catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "maintenance");
      }//end of catch (SQLException sqlExp)
      catch (Exception exp)
      {
            throw new TTKException(exp, "maintenance");
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
      			log.error("Error while closing the Statement in MaintenanceDAOImpl selectICD()",sqlExp);
      			throw new TTKException(sqlExp, "maintenance");
      		}//end of catch (SQLException sqlExp)
      		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
      		{
      			try
      			{
      				if(conn != null) conn.close();
      			}//end of try
      			catch (SQLException sqlExp)
      			{
      				log.error("Error while closing the Connection in MaintenanceDAOImpl selectICD()",sqlExp);
      				throw new TTKException(sqlExp, "maintenance");
      			}//end of catch (SQLException sqlExp)
      		}//end of finally Connection Close
      	}//end of try
      	catch (TTKException exp)
      	{
      		throw new TTKException(exp, "maintenance");
      	}//end of catch (TTKException exp)
      	finally // Control will reach here in any case set null to the objects 
      	{
      		cStmtObject = null;
      		conn = null;
      	}//end of finally
		}//end of finally
      return iCDCodeVO;
    }
    /**
     * This method returns the Arraylist of PolicyListVO's  which contains Daycare Group details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of PolicyListVO's object which contains Daycare Group details
     * @exception throws TTKException
     */
    public ArrayList getPolicyList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PolicyListVO policyListVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPolicyList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//POLICY_NUMBER
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//INS_SEQ_ID
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//ENROL_TYPE_ID
			cStmtObject.registerOutParameter(4,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(4);
			
			if(rs != null){
				while(rs.next()){
					policyListVO = new PolicyListVO();

					policyListVO.setPolicySeqID(rs.getLong("POLICY_SEQ_ID"));
					policyListVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
					policyListVO.setPolicyHolderName(TTKCommon.checkNull(rs.getString("POLICY_HOLDER")));
					policyListVO.setEnrollmentType(TTKCommon.checkNull(rs.getString("ENROL_DESCRIPTION")));
					policyListVO.setPolicySubGeneralTypeID(TTKCommon.checkNull(rs.getString("POLICY_SUB_GENERAL_TYPE_ID")));
					policyListVO.setPolicySubType(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
					if(rs.getString("EFFECTIVE_FROM_DATE") != null){
						policyListVO.setEffectiveFromDate(new Date(rs.getTimestamp("EFFECTIVE_FROM_DATE").getTime()));
            		}//end of if(rs.getString("EFFECTIVE_FROM_DATE") != null)
					policyListVO.setInsuranceCompName(TTKCommon.checkNull(rs.getString("INS_COMP_NAME")));
					alResultList.add(policyListVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getPolicyList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getPolicyList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPolicyList(ArrayList alSearchCriteria)
    
    /**
     * This method returns an positive integer on successful execution
     * @param alChangeToPnf ArrayList object which contains the parameters
     * @return positive integer on successful execution
     * @exception throws TTKException
     */
    public int changeToNonFloater(ArrayList alChangeToPnf) throws TTKException
    {
    	int iResult=0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strChangeToNonFloater);
            cStmtObject.setLong(1, (Long)alChangeToPnf.get(0));
            cStmtObject.setLong(2,(Long)alChangeToPnf.get(1));
            cStmtObject.registerOutParameter(3,Types.BIGINT);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(3);
    	} catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "maintenance");
      }//end of catch (SQLException sqlExp)
      catch (Exception exp)
      {
            throw new TTKException(exp, "maintenance");
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
      			log.error("Error while closing the Statement in MaintenanceDAOImpl changeToNonFloater()",sqlExp);
      			throw new TTKException(sqlExp, "maintenance");
      		}//end of catch (SQLException sqlExp)
      		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
      		{
      			try
      			{
      				if(conn != null) conn.close();
      			}//end of try
      			catch (SQLException sqlExp)
      			{
      				log.error("Error while closing the Connection in MaintenanceDAOImpl changeToNonFloater()",sqlExp);
      				throw new TTKException(sqlExp, "maintenance");
      			}//end of catch (SQLException sqlExp)
      		}//end of finally Connection Close
      	}//end of try
      	catch (TTKException exp)
      	{
      		throw new TTKException(exp, "maintenance");
      	}//end of catch (TTKException exp)
      	finally // Control will reach here in any case set null to the objects 
      	{
      		cStmtObject = null;
      		conn = null;
      	}//end of finally
		}//end of finally
  	return iResult;
    }//end of changeToNonFloater(ArrayList alChangeToPnf)
    
    /**
     * This method returns an positive integer on successful execution
     * @param alChangeToPnf ArrayList object which contains the parameters
     * @return positive integer on successful execution
     * @exception throws TTKException
     */
    public int changeToFloater(ArrayList alChangeToPfl) throws TTKException
    {
    	int iResult=0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strChangeToFloater);
            cStmtObject.setLong(1, (Long)alChangeToPfl.get(0));
            cStmtObject.setLong(2,(Long)alChangeToPfl.get(1));
            cStmtObject.registerOutParameter(3,Types.BIGINT);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(3);
    	} catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "maintenance");
      }//end of catch (SQLException sqlExp)
      catch (Exception exp)
      {
            throw new TTKException(exp, "maintenance");
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
      			log.error("Error while closing the Statement in MaintenanceDAOImpl changeToFloater()",sqlExp);
      			throw new TTKException(sqlExp, "maintenance");
      		}//end of catch (SQLException sqlExp)
      		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
      		{
      			try
      			{
      				if(conn != null) conn.close();
      			}//end of try
      			catch (SQLException sqlExp)
      			{
      				log.error("Error while closing the Connection in MaintenanceDAOImpl changeToFloater()",sqlExp);
      				throw new TTKException(sqlExp, "maintenance");
      			}//end of catch (SQLException sqlExp)
      		}//end of finally Connection Close
      	}//end of try
      	catch (TTKException exp)
      	{
      		throw new TTKException(exp, "maintenance");
      	}//end of catch (TTKException exp)
      	finally // Control will reach here in any case set null to the objects 
      	{
      		cStmtObject = null;
      		conn = null;
      	}//end of finally
		}//end of finally
  	return iResult;
    }//end of changeToFloater(ArrayList alChangeToPfl)   
        
    /**
     * This method returns the Arraylist of NotifyDetailVO's  which contains Notification details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of NotifyDetailVO object which contains Notification details
     * @exception throws TTKException
     */
    public ArrayList getNotifyList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		NotifyDetailVO notifyDetailVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectNotifyList);			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//SORT_VAR
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//SORT_ORDER
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//START_NUM
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//END_NUM
			cStmtObject.registerOutParameter(5,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(5);			
			if(rs != null){
				while(rs.next()){
					notifyDetailVO = new NotifyDetailVO();
					notifyDetailVO.setMsgID(TTKCommon.checkNull(rs.getString("MSG_ID")));
					notifyDetailVO.setMsgName(TTKCommon.checkNull(rs.getString("MSG_NAME")));	
					notifyDetailVO.setNotifCategory(TTKCommon.checkNull(rs.getString("NOTIFICATION_CATEGORY")));
					alResultList.add(notifyDetailVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getNotifyList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getNotifyList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getNotifyList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getNotifyList(ArrayList alSearchCriteria)
    
    /**
     * This method saves the Notification details
     * @param notifyDetailVO NotifyDetailVO object which contains the search criteria
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int saveNotificationInfo(NotifyDetailVO notifyDetailVO) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		int iResult = 0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveNotificationInfo);	
			cStmtObject.setString(1,notifyDetailVO.getMsgID());
			cStmtObject.setString(2,notifyDetailVO.getEmailDesc());
			cStmtObject.setString(3,notifyDetailVO.getSMSDesc());
			cStmtObject.setString(4,notifyDetailVO.getPrimaryMailID());
			cStmtObject.setString(5,notifyDetailVO.getSecondaryMailID());
			cStmtObject.setString(6,notifyDetailVO.getMsgTitle());
			if(notifyDetailVO.getConfigParam1()!=null)
			{
				cStmtObject.setInt(7,notifyDetailVO.getConfigParam1());
			}//end of if(notifyDetailVO.getConfigParam1()!=null)
			else
			{
				cStmtObject.setString(7,null);
			}//end of else
			if(notifyDetailVO.getConfigParam2()!=null)
			{
				cStmtObject.setInt(8,notifyDetailVO.getConfigParam2());
			}//end of if(notifyDetailVO.getConfigParam2()!=null)
			else
			{
				cStmtObject.setString(8,null);
			}//end of else
			if(notifyDetailVO.getConfigParam3()!=null)
			{
				cStmtObject.setInt(9,notifyDetailVO.getConfigParam3());
			}//end of if(notifyDetailVO.getConfigParam3()!=null)
			else
			{
				cStmtObject.setString(9,null);
			}//end of else	
			cStmtObject.setString(10,notifyDetailVO.getLevel2EmailID());
			cStmtObject.setString(11,notifyDetailVO.getLevel3EmailID());
			cStmtObject.setLong(12,notifyDetailVO.getAddedBy());
			cStmtObject.registerOutParameter(13,OracleTypes.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(13);
			return iResult;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in MaintenanceDAOImpl saveNotificationInfo()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in MaintenanceDAOImpl saveNotificationInfo()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of saveNotificationInfo(ArrayList alSearchCriteria)
    
    /**
     * This method returns the NotifyDetailVO which contains Notification details
     * @param strMsgID String object which contains the Message ID
     * @return notifyDetailVO of NotifyDetailVO object which contains Notification details
     * @exception throws TTKException
     */
    public NotifyDetailVO getNotificationInfo(String strMsgID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		NotifyDetailVO notifyDetailVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectNotificationInfo);			
			cStmtObject.setString(1,strMsgID);//SORT_VAR
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);			
			if(rs != null){
				while(rs.next()){
					notifyDetailVO = new NotifyDetailVO();
					notifyDetailVO.setMsgID(TTKCommon.checkNull(rs.getString("MSG_ID")));
					notifyDetailVO.setMsgName(TTKCommon.checkNull(rs.getString("MSG_NAME")));
					notifyDetailVO.setNotificationDesc(TTKCommon.checkNull(rs.getString("NOTIFICATION_DESCRIPTION")));
					notifyDetailVO.setMsgTitle(TTKCommon.checkNull(rs.getString("MESSAGE_TITLE")));
					//notifyDetailVO.setEmailDesc(TTKCommon.checkNull(rs.getString("MSG_DESCRIPTION")));
					//changed for cigna mail-sms for combination of 2 messages 					
					notifyDetailVO.setEmailDesc(TTKCommon.checkNull(rs.getString("MSG_DESCRIPTION")/*+rs.getString("MESG2")*/));					
					notifyDetailVO.setSMSDesc(TTKCommon.checkNull(rs.getString("SMS_DESCRIPTION")));
					notifyDetailVO.setPrimaryMailID(TTKCommon.checkNull(rs.getString("PRM_RCPT_EMAIL_LIST")));
					notifyDetailVO.setSecondaryMailID(TTKCommon.checkNull(rs.getString("SEC_RCPT_EMAIL_LIST")));	
					notifyDetailVO.setConfigParam1(rs.getString("CONFIG_PARAM_1")!=null ? new Integer(rs.getInt("CONFIG_PARAM_1")):null);
					notifyDetailVO.setConfigParam2(rs.getString("CONFIG_PARAM_2")!=null ? new Integer(rs.getInt("CONFIG_PARAM_2")):null);
					notifyDetailVO.setConfigParam3(rs.getString("CONFIG_PARAM_3")!=null ? new Integer(rs.getInt("CONFIG_PARAM_3")):null);
					notifyDetailVO.setNotifCategory(TTKCommon.checkNull(rs.getString("NOTIFICATION_CATEGORY")));
					notifyDetailVO.setConfigAllowedYN(TTKCommon.checkNull(rs.getString("PARAM_CONFIG_ALLOWED_YN")));
					notifyDetailVO.setCustConfigAllowedYN(TTKCommon.checkNull(rs.getString("CUSTOMIZED_CONFIG_ALLOWED_YN")));
					notifyDetailVO.setSendEmailYN(TTKCommon.checkNull(rs.getString("SEND_AS_EMAIL_YN")));
					notifyDetailVO.setSendSmsYN(TTKCommon.checkNull(rs.getString("SEND_AS_SMS_YN")));
					notifyDetailVO.setLevel2EmailID(TTKCommon.checkNull(rs.getString("LEVEL2_EMAIL_LIST")));
					notifyDetailVO.setLevel3EmailID(TTKCommon.checkNull(rs.getString("LEVEL3_EMAIL_LIST")));
					notifyDetailVO.setShowMultiLevelYN(TTKCommon.checkNull(rs.getString("SHOW_MULTI_ESCAL_LEVEL_YN")));
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return notifyDetailVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getNotificationInfo()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getNotificationInfo()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getNotificationInfo()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getNotificationInfo(String strMsgID)
    
    /**
     * This method returns the Arraylist of CustomizeConfigVO's  which contains Notification details
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of CustomizeConfigVO object which contains Notification details
     * @exception throws TTKException
     */
    public ArrayList getCustomConfigList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CustomizeConfigVO customizeConfigVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectCustomConfigList);			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));//MSG_ID
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));//SORT_VAR
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));//SORT_ORDER
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));//START_NUM
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));//END_NUM
			cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(6);			
			if(rs != null){
				while(rs.next()){
					customizeConfigVO = new CustomizeConfigVO();
					customizeConfigVO.setCustConfigSeqID(new Long(rs.getLong("CUST_CONFIG_SEQ_ID")));
					customizeConfigVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					customizeConfigVO.setMsgID(TTKCommon.checkNull(rs.getString("MSG_ID")));
					customizeConfigVO.setStrConfigParam1(rs.getString("CONFIG_PARAM_1")!=null ?  (rs.getString("CONFIG_PARAM_1")):"");
					customizeConfigVO.setStrConfigParam2(rs.getString("CONFIG_PARAM_2")!=null ?  (rs.getString("CONFIG_PARAM_2")):"");
					customizeConfigVO.setStrConfigParam3(rs.getString("CONFIG_PARAM_3")!=null ?  (rs.getString("CONFIG_PARAM_3")):"");
					customizeConfigVO.setStrConfigParam4(rs.getString("CONFIG_PARAM_4")!=null ?  (rs.getString("CONFIG_PARAM_4")):""); // koc11 koc 11
					customizeConfigVO.setOfficeName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));					
					alResultList.add(customizeConfigVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCustomConfigList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCustomConfigList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCustomConfigList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getCustomConfigList(ArrayList alSearchCriteria)
    
    /**
     * This method saves the Notification Customized Config details
     * @param customizeConfigVO CustomizeConfigVO object which contains the search criteria
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int saveCustomConfigInfo(CustomizeConfigVO customizeConfigVO) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		int iResult = 0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveCustomConfigInfo);	
			if(customizeConfigVO.getCustConfigSeqID()!=null)
			{
				cStmtObject.setLong(1,customizeConfigVO.getCustConfigSeqID());
			}//end of if(customizeConfigVO.getCustConfigSeqID()!=null)
			else
			{
				cStmtObject.setLong(1,0);
			}//end of else
			cStmtObject.setString(2,customizeConfigVO.getMsgID());
			if(customizeConfigVO.getOfficeSeqID()!=null)
			{
				cStmtObject.setLong(3,customizeConfigVO.getOfficeSeqID());
			}//end of if(customizeConfigVO.getOfficeSeqID()!=null)
			else
			{
				cStmtObject.setString(3,null);
			}//end of else			
			if(customizeConfigVO.getConfigParam1()!=null)
			{
				cStmtObject.setInt(4,customizeConfigVO.getConfigParam1());
			}//end of if(customizeConfigVO.getConfigParam1()!=null)
			else
			{
				cStmtObject.setString(4,null);
			}//end of else
			cStmtObject.setString(5,customizeConfigVO.getPrimaryMailID1());
			if(customizeConfigVO.getConfigParam2()!=null)
			{
				cStmtObject.setInt(6,customizeConfigVO.getConfigParam2());
			}//end of if(customizeConfigVO.getConfigParam2()!=null)
			else
			{
				cStmtObject.setString(6,null);
			}//end of else
			cStmtObject.setString(7,customizeConfigVO.getPrimaryMailID2());
			if(customizeConfigVO.getConfigParam3()!=null)
			{
				cStmtObject.setInt(8,customizeConfigVO.getConfigParam3());
			}//end of if(customizeConfigVO.getConfigParam3()!=null)
			else
			{
				cStmtObject.setString(8,null);
			}//end of else	
			cStmtObject.setString(9,customizeConfigVO.getPrimaryMailID3());
			if(customizeConfigVO.getConfigParam4()!=null)
			{
				cStmtObject.setInt(10,customizeConfigVO.getConfigParam4());
			}//end of if(customizeConfigVO.getConfigParam3()!=null)
			else
			{
				cStmtObject.setString(10,null);
			}//end of else	
			cStmtObject.setString(11,customizeConfigVO.getPrimaryMailID4()); 
			cStmtObject.setLong(12,customizeConfigVO.getAddedBy());
			cStmtObject.registerOutParameter(13,OracleTypes.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(13);
			return iResult;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in MaintenanceDAOImpl saveCustomConfigInfo()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in MaintenanceDAOImpl saveCustomConfigInfo()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of saveCustomConfigInfo(CustomizeConfigVO customizeConfigVO)
    
    /**
     * This method returns the CustomizeConfigVO which contains Notification Customized Config details
     * @param lngCustConfigSeqID Long object which contains the Cust Config Seq ID
     * @return customizeConfigVO of CustomizeConfigVO object which contains Customized Config details
     * @exception throws TTKException
     */
    public CustomizeConfigVO getCustomConfigInfo(Long lngCustConfigSeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CustomizeConfigVO customizeConfigVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectCustomConfigInfo);			
			cStmtObject.setLong(1,lngCustConfigSeqID);//CustConfigSeqID
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);			
			if(rs != null){
				while(rs.next()){
					customizeConfigVO = new CustomizeConfigVO();					
					customizeConfigVO.setCustConfigSeqID(new Long(rs.getLong("CUST_CONFIG_SEQ_ID")));
					customizeConfigVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					customizeConfigVO.setMsgID(TTKCommon.checkNull(rs.getString("MSG_ID")));
					customizeConfigVO.setConfigParam1(rs.getString("CONFIG_PARAM_1")!=null ? new Integer(rs.getInt("CONFIG_PARAM_1")):null);
					customizeConfigVO.setPrimaryMailID1(TTKCommon.checkNull(rs.getString("PARAM_1_EMAIL_LIST")));
					customizeConfigVO.setConfigParam2(rs.getString("CONFIG_PARAM_2")!=null ? new Integer(rs.getInt("CONFIG_PARAM_2")):null);
					customizeConfigVO.setPrimaryMailID2(TTKCommon.checkNull(rs.getString("PARAM_2_EMAIL_LIST")));
					customizeConfigVO.setConfigParam3(rs.getString("CONFIG_PARAM_3")!=null ? new Integer(rs.getInt("CONFIG_PARAM_3")):null);
					customizeConfigVO.setPrimaryMailID3(TTKCommon.checkNull(rs.getString("PARAM_3_EMAIL_LIST")));	
					customizeConfigVO.setConfigParam4(rs.getString("CONFIG_PARAM_4")!=null ? new Integer(rs.getInt("CONFIG_PARAM_4")):null);
					customizeConfigVO.setPrimaryMailID4(TTKCommon.checkNull(rs.getString("PARAM_4_EMAIL_LIST")));
					customizeConfigVO.setPrimaryMailID3(TTKCommon.checkNull(rs.getString("PARAM_3_EMAIL_LIST")));					
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return customizeConfigVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCustomConfigInfo()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCustomConfigInfo()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCustomConfigInfo()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getCustomConfigInfo(Long lngCustConfigSeqID)
    
    /**
     * This method returns the Arraylist of CustomizeConfigVO's  which contains Call pending details
     * @return ArrayList of CustomizeConfigVO object which contains Notification details
     * @exception throws TTKException
     */
    public ArrayList<Object> getCallpendingByBranch() throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CustomizeConfigVO customizeConfigVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetCallpendingByBranch);			
			cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(1);			
			if(rs != null){
				while(rs.next()){
					customizeConfigVO = new CustomizeConfigVO();
					customizeConfigVO.setOfficeCode(rs.getString("OFFICE_CODE"));
					customizeConfigVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					customizeConfigVO.setConfigParam1(rs.getString("CONFIG_PARAM_1")!=null ? new Integer(rs.getInt("CONFIG_PARAM_1")):null);
					customizeConfigVO.setPrimaryMailID1(TTKCommon.checkNull(rs.getString("PARAM_1_EMAIL_LIST")));
					customizeConfigVO.setConfigParam2(rs.getString("CONFIG_PARAM_2")!=null ? new Integer(rs.getInt("CONFIG_PARAM_2")):null);
					customizeConfigVO.setPrimaryMailID2(TTKCommon.checkNull(rs.getString("PARAM_2_EMAIL_LIST")));
					customizeConfigVO.setConfigParam3(rs.getString("CONFIG_PARAM_3")!=null ? new Integer(rs.getInt("CONFIG_PARAM_3")):null);
					customizeConfigVO.setPrimaryMailID3(TTKCommon.checkNull(rs.getString("PARAM_3_EMAIL_LIST")));
					alResultList.add(customizeConfigVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList<Object>)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getCallpendingByBranch(ArrayList alSearchCriteria)
    
    /**
     * This method returns the CustomizeConfigVO's  which contains Call pending details
     * @param strMRClaimsPendConfig MR Claims Pending Config
     * @return CustomizeConfigVO object which contains Notification details
     * @exception throws TTKException
     */
    public CustomizeConfigVO getMrClaimsPendConfig(String strMRClaimsPendConfig) throws TTKException {

    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CustomizeConfigVO customizeConfigVO = new CustomizeConfigVO();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetMRClaimPendConfig);	
			cStmtObject.setString(1,strMRClaimsPendConfig);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);			
			if(rs != null){
				while(rs.next()){
					customizeConfigVO.setConfigParam3(rs.getString("CONFIG_PARAM_3")!=null ? new Integer(rs.getInt("CONFIG_PARAM_3")):null);
					customizeConfigVO.setPrimaryMailID3(TTKCommon.checkNull(rs.getString("LEVEL3_EMAIL_LIST")));
					customizeConfigVO.setConfigParam2(rs.getString("CONFIG_PARAM_2")!=null ? new Integer(rs.getInt("CONFIG_PARAM_2")):null);
					customizeConfigVO.setPrimaryMailID2(TTKCommon.checkNull(rs.getString("LEVEL2_EMAIL_LIST")));
					customizeConfigVO.setConfigParam1(rs.getString("CONFIG_PARAM_1")!=null ? new Integer(rs.getInt("CONFIG_PARAM_1")):null);
					customizeConfigVO.setPrimaryMailID1(TTKCommon.checkNull(rs.getString("PRM_RCPT_EMAIL_LIST")));
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return customizeConfigVO;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getMrClaimsPendConfig()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getMrClaimsPendConfig()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getMrClaimsPendConfig()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally    
    }//end of getMrClaimsPendConfig(String strMRClaimsPendConfig)
    
    /**
     * This method returns the Arraylist of CustomizeConfigVO's  which contains Call pending details
     * @param lngOfficeSeqID Long Office Seq ID
     * @return ArrayList of CustomizeConfigVO object which contains Notification details
     * @exception throws TTKException
     */
    public ArrayList<Object> getCallPendByBranchRpt(Long lngOfficeSeqID) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CustomizeConfigVO customizeConfigVO = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetCallpendingByBranchRpt);	
			cStmtObject.setLong(1,lngOfficeSeqID);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);			
			if(rs != null){
				while(rs.next()){
					customizeConfigVO = new CustomizeConfigVO();
					customizeConfigVO.setOfficeCode(rs.getString("OFFICE_CODE"));
					customizeConfigVO.setOfficeSeqID(new Long(rs.getLong("TPA_OFFICE_SEQ_ID")));
					customizeConfigVO.setConfigParam1(rs.getString("CONFIG_PARAM_1")!=null ? new Integer(rs.getInt("CONFIG_PARAM_1")):null);
					customizeConfigVO.setPrimaryMailID1(TTKCommon.checkNull(rs.getString("PARAM_1_EMAIL_LIST")));
					customizeConfigVO.setConfigParam2(rs.getString("CONFIG_PARAM_2")!=null ? new Integer(rs.getInt("CONFIG_PARAM_2")):null);
					customizeConfigVO.setPrimaryMailID2(TTKCommon.checkNull(rs.getString("PARAM_2_EMAIL_LIST")));
					customizeConfigVO.setConfigParam3(rs.getString("CONFIG_PARAM_3")!=null ? new Integer(rs.getInt("CONFIG_PARAM_3")):null);
					customizeConfigVO.setPrimaryMailID3(TTKCommon.checkNull(rs.getString("PARAM_3_EMAIL_LIST")));
					alResultList.add(customizeConfigVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList<Object>)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getCallpendingByBranch(ArrayList alSearchCriteria)

    /**
     * This method inserts record after generating the report in ReportScheduler   
     * @param strIdentifier String Identifier
     * @param strAddiParam String Additional parameter
     * @param strPrimaryMailID String Primary Mail ID
     * @exception throws TTKException
     */
    public void insertRecord(String strIdentifier, String strAddiParam, String strPrimaryMailID) throws TTKException {
    	Connection conn = null;
		CallableStatement cStmtObject = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject=conn.prepareCall(strProcFormMessage);
			cStmtObject.setString(1,strIdentifier);
			cStmtObject.setString(2,"");
			cStmtObject.setLong(3,new Long("1"));
			cStmtObject.setString(5,strAddiParam);
			cStmtObject.setString(6,strPrimaryMailID);
			cStmtObject.registerOutParameter(4, Types.VARCHAR);
			cStmtObject.execute();		
		
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl insertRecord()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl insertRecord()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    }//end of insertRecord(long lngPatGenDtlSeqID,String strIdentifier,Long lngUserID)
    
    /**
     * This method returns the ArrayList, which contains the ClaimListVO which are populated from the database
     * @param strClaimNbr String object which contains the Claim Number
     * @return ArrayList of ClaimListVO's object's which contains the procedure details
     * @exception throws TTKException
     */ 
    public ArrayList<Object> getClaimReqamt(String strClaimNbr) throws TTKException {
    	ClaimListVO claimListVO = null;
	    Collection<Object> alResultList = new ArrayList<Object>();
	    Connection conn = null;
	    CallableStatement cStmtObject=null;
	    ResultSet rs = null;
	    try{
	    	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSelectClaimReqamt);
			
			cStmtObject.setString(1,strClaimNbr);
			cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);//ROWS_PROCESSED
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			if(rs != null){
				while(rs.next()){
					claimListVO = new ClaimListVO();
					
					if (rs.getString("CLAIM_SEQ_ID") != null){
						claimListVO.setClaimSeqID(new Long(rs.getLong("CLAIM_SEQ_ID")));
                    }//end of if (rs.getString("CLAIM_SEQ_ID") != null)
					claimListVO.setClaimNbr(TTKCommon.checkNull(rs.getString("CLAIM_NUMBER")));
					claimListVO.setPolicyNbr(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
					claimListVO.setEnrollmentID(TTKCommon.checkNull(rs.getString("TPA_ENROLLMENT_ID")));
					claimListVO.setClaimantName(TTKCommon.checkNull(rs.getString("CLAIMANT_NAME")));
					if(rs.getString("REQUESTED_AMOUNT") != null){
						claimListVO.setPreClmReqAmt(new BigDecimal(rs.getString("REQUESTED_AMOUNT")));
                    }//end of if(rs.getString("REQUESTED_AMOUNT") != null)
					
					alResultList.add(claimListVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList<Object>)alResultList;
	    }//end of try
	    catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getClaimReqamt()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getClaimReqamt()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getClaimReqamt()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getClaimReqamt(ArrayList<Object> alSearchCriteria)

    /**
     * This method saves the New Claims Requested Amount
     * @param claimListVO ClaimListVO object which contains the search criteria
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int saveClaimReqAmt(ClaimListVO claimListVO) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		int iResult = 0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveClaimReqAmt);	
			if(claimListVO.getClaimSeqID()!=null)
			{
				cStmtObject.setLong(1,claimListVO.getClaimSeqID());
			}//end of if(claimListVO.getClaimSeqID()!=null)
			else
			{
				cStmtObject.setLong(1,0);
			}//end of else
			cStmtObject.setBigDecimal(2,claimListVO.getNewClmReqAmt());
			
			cStmtObject.setString(3,claimListVO.getRemarks());
			cStmtObject.setLong(4,claimListVO.getAddedBy());
			cStmtObject.registerOutParameter(5,OracleTypes.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(5);
			return iResult;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in MaintenanceDAOImpl saveClaimReqAmt()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in MaintenanceDAOImpl saveClaimReqAmt()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of saveClaimReqAmt(CustomizeConfigVO customizeConfigVO)
    
    /**
	 * This method returns the ProcedureDetailVO, which contains procedure details
	 * @param lngProcSeqID Procedure Seq ID
	 * @return ProcedureDetailVO object which contains procedure details
	 * @exception throws TTKException
	 */
    public ProcedureDetailVO getPcsInfo(long lngProcSeqID) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	ProcedureDetailVO procedureDetailVO = null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPcsDetail);
    		
    		cStmtObject.setLong(1,lngProcSeqID);
    		cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(2);
			
			if(rs != null){
				while(rs.next()){
					procedureDetailVO = new ProcedureDetailVO();
					
					if(rs.getString("PROC_SEQ_ID") != null){
						procedureDetailVO.setProcedureID(new Long(rs.getLong("PROC_SEQ_ID")));
					}//end of if(rs.getString("PROC_SEQ_ID") != null)
					
					procedureDetailVO.setProcedureCode(TTKCommon.checkNull(rs.getString("PROC_CODE")));
					procedureDetailVO.setProcedureDescription(TTKCommon.checkNull(rs.getString("PROC_DESCRIPTION")));
					procedureDetailVO.setShortDesc(TTKCommon.checkNull(rs.getString("SHORT_DESCRIPTION")));
					procedureDetailVO.setMasterProcCode(TTKCommon.checkNull(rs.getString("MASTER_PROCEDURE_CODE")));
				}//end of while(rs.next())
			}//end of if(rs != null)
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getGroupDetail()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getGroupDetail()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getGroupDetail()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    	return procedureDetailVO;
    }//end of getPcsInfo(long lngProcSeqID)
    
    /**
     * This method saves the Procedure details
     * @param procedureDetailVO ProcedureDetailVO object which contains the Procedure Details
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int savePcsInfo(ProcedureDetailVO procedureDetailVO) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		int iResult = 0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSavePcsDetail);	
			
			if(procedureDetailVO.getProcedureID() != null){
				cStmtObject.setLong(1,procedureDetailVO.getProcedureID());
			}//end of if(procedureDetailVO.getProcedureID() != null)
			else{
				cStmtObject.setLong(1,0);
			}//end of else
			
			cStmtObject.setString(2,procedureDetailVO.getProcedureCode());
			cStmtObject.setString(3,procedureDetailVO.getProcedureDescription());
			cStmtObject.setString(4,procedureDetailVO.getShortDesc());
			cStmtObject.setString(5,procedureDetailVO.getMasterProcCode());
			cStmtObject.setLong(6,procedureDetailVO.getUpdatedBy());
			cStmtObject.registerOutParameter(7,OracleTypes.INTEGER);
			cStmtObject.execute();
			iResult = cStmtObject.getInt(7);
			
			return iResult;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in MaintenanceDAOImpl savePcsInfo()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in MaintenanceDAOImpl savePcsInfo()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of finally Statement Close
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of savePcsInfo(ProcedureDetailVO procedureDetailVO)
    
    /**
     * This method will update the policy DO/BO details.
     * @param alUpdateList object which contains the save criteria
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int updatePolicyDOBO(ArrayList<Object> alUpdateList) throws TTKException {

    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		int iUpdateCount = 0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strChgPolicyDOBO);	
			cStmtObject.setString(1,(String)alUpdateList.get(0));
			cStmtObject.setBigDecimal(2,BigDecimal.valueOf((Long)alUpdateList.get(1)));
			cStmtObject.registerOutParameter(3,OracleTypes.INTEGER);
			cStmtObject.execute();
			iUpdateCount = Integer.parseInt(cStmtObject.getObject(3).toString());
			return iUpdateCount;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing CallableStatement statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in MaintenanceDAOImpl updatePolicyDOBO()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in MaintenanceDAOImpl updatePolicyDOBO()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally    
    }//end of updatePolicyDOBO(ArrayList<Object> alUpdateList)
    
    /**
     * This method will update the policy period details.
     * @param alUpdateList object which contains the save criteria
     * @return int value, greater than zero indicates successful execution of the task
     * @exception throws TTKException
     */
    public int updatePolicyPeriod(ArrayList<Object> alUpdateList) throws TTKException {

    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		int iUpdateCount = 0;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strChgPolicyPeriod);	
			cStmtObject.setString(1,(String)alUpdateList.get(0));
			cStmtObject.setBigDecimal(2,BigDecimal.valueOf((Long)alUpdateList.get(1)));			
			cStmtObject.setTimestamp(3,new Timestamp(TTKCommon.getUtilDate(alUpdateList.get(2).toString()).getTime()));
			cStmtObject.setTimestamp(4,new Timestamp(TTKCommon.getUtilDate(alUpdateList.get(3).toString()).getTime()));
			cStmtObject.registerOutParameter(5,OracleTypes.INTEGER);
			cStmtObject.execute();
			iUpdateCount = Integer.parseInt(cStmtObject.getObject(5).toString());
			return iUpdateCount;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
		}//end of catch (Exception exp)
		finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing CallableStatement statement
			{
				try
				{
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Statement in MaintenanceDAOImpl updatePolicyPeriod()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if statement is not closed, control reaches here. Try closing the connection now.
				{
					try
					{
						if(conn != null) conn.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Connection in MaintenanceDAOImpl updatePolicyPeriod()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
				}//end of finally Connection Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally    
    }//end of updatePolicyPeriod(ArrayList<Object> alUpdateList)
    
    /**
     * This method returns the ArrayList, which contains the ProcedureDetailVO which are populated from the database
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of ProcedureDetailVO's object's which contains the procedure details
     * @exception throws TTKException
     */ 
    public ArrayList<Object> getPcsList(ArrayList<Object> alSearchCriteria) throws TTKException {
    	ProcedureDetailVO procedureDetailVO = null;
	    Collection<Object> alResultList = new ArrayList<Object>();
	    Connection conn = null;
	    CallableStatement cStmtObject=null;
	    ResultSet rs = null;
	    try{
	    	conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPcsList);
			
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(3));
			cStmtObject.setString(4,(String)alSearchCriteria.get(4));
			cStmtObject.setString(5,(String)alSearchCriteria.get(5));
			cStmtObject.setString(6,(String)alSearchCriteria.get(6));
			cStmtObject.setLong(7,(Long)alSearchCriteria.get(2));
			cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);//ROWS_PROCESSED
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(8);
			if(rs != null){
				while(rs.next()){
					procedureDetailVO = new ProcedureDetailVO();
					if (rs.getString("PROC_SEQ_ID") != null){
	                    procedureDetailVO.setProcedureID(new Long(rs.getLong("PROC_SEQ_ID")));
                    }//end of if (rs.getString("PROC_SEQ_ID") != null)
	                procedureDetailVO.setProcedureCode(TTKCommon.checkNull(rs.getString("PROC_CODE")));
	                procedureDetailVO.setProcedureDescription(TTKCommon.checkNull(rs.getString("PROC_DESCRIPTION")));
	                
	                alResultList.add(procedureDetailVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList<Object>)alResultList;
	    }//end of try
	    catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getPcsList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null)	cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getPcsList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getPcsList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPcsList(ArrayList<Object> alSearchCriteria)
    
    
    
    
    
    /**
     * This method returns the Arraylist of CustomizeConfigVO's  which contains Call pending details
     * @return ArrayList of CustomizeConfigVO object which contains Notification details
     * @exception throws TTKException
     */
    public ArrayList<Object> getCallPendingByAttendedBy() throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		CustomizeConfigVO customizeConfigVO = null;
		PreparedStatement pStmt = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetCallpendingByAttendBy);			
			cStmtObject.registerOutParameter(1,OracleTypes.CURSOR);
			cStmtObject.execute();
			
	          // pStmt=conn.prepareStatement("SELECT a.call_log_seq_id,to_char(A.CALL_RECORDED_DATE,'DD/MM/YYYY') AS LAST_CORRESPONDENCE_DATE,A.CALL_LOG_NUMBER,C.DESCRIPTION,A.CALL_CONTENT,a.caller_name FROM app.tpa_call_log A,app.tpa_office_info B,app.tpa_general_code C,app.tpa_user_contacts D WHERE A.tpa_office_seq_id = B.tpa_office_seq_id  and A.ACTION_GENERAL_TYPE_ID = C.GENERAL_TYPE_ID AND a.contact_seq_id = d.contact_seq_id AND A.Last_Action_General_Type_Id <> 'SCL' AND A.PARENT_CALL_LOG_SEQ_ID IS NULL AND a.call_bck_yn = 'Y' group by a.call_log_seq_id,a.caller_name,A.CALL_LOG_NUMBER,C.DESCRIPTION,A.CALL_CONTENT,A.CALL_RECORDED_DATE ORDER BY A.CALL_RECORDED_DATE");
	            
	            
			rs = (java.sql.ResultSet)cStmtObject.getObject(1);
	          // rs	=	pStmt.executeQuery();
			if(rs != null){
				while(rs.next()){
					customizeConfigVO = new CustomizeConfigVO();
					//customizeConfigVO.setCallLogSeqId(rs.getString("CALL_LOG_SEQ_ID"));
					//customizeConfigVO.setCallLogNo(rs.getString("CALL_LOG_NUMBER"));
					//customizeConfigVO.setDesc(rs.getString("DESCRIPTION"));
					//customizeConfigVO.setCallContent(TTKCommon.checkNull(rs.getString("CALL_CONTENT")));
					//customizeConfigVO.setCallerName(rs.getString("caller_name"));
					customizeConfigVO.setAddedBy(rs.getLong("ADDED_BY"));
					customizeConfigVO.setPrimaryMailID1(rs.getString("AGENT_EMAIL_ID"));
					alResultList.add(customizeConfigVO);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList<Object>)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getCallpendingByBranch()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getCallPendingByAttendedBy(ArrayList alSearchCriteria)
    
   /* public static void main(String a[]) throws Exception{
    	
//    	MaintenanceDAOImpl maintanaceDAO = new MaintenanceDAOImpl();
//    	maintanaceDAO.getNotificationInfo("MR_CLAIMS_PENDING_REPORT");
    	ProcedureDetailVO procDetailVO = new ProcedureDetailVO();
    	procDetailVO.setProcedureID(new Long(7));
    	procDetailVO.setProcedureCode("47.09");
    	procDetailVO.setProcedureDescription("OTHER APPENDECTOMY");
    	procDetailVO.setShortDesc("OTHER APPENDECTOMY TEST");
    	procDetailVO.setProcMapSeqID(new Long(1393));
    	procDetailVO.setMasterProcCode("47.09");
    	procDetailVO.setUpdatedBy(new Long(56503));
    	maintanaceDAO.savePcsInfo(procDetailVO);
    	
    	//maintanaceDAO.getPcsInfo(new Long(7));
    	
    	ArrayList<Object> alSearchCriteria = new ArrayList<Object>();
    	alSearchCriteria.add("47.09");
    	alSearchCriteria.add("");
    	alSearchCriteria.add(new Long(56503));
    	alSearchCriteria.add("PROC_CODE");
    	alSearchCriteria.add("ASC");
    	alSearchCriteria.add("1");
    	alSearchCriteria.add("10");
    	maintanaceDAO.getPcsList(alSearchCriteria);
    }//end of main
*/    
    
    
    /**
     * This method inserts record after generating the report in ReportScheduler   
     * @param strIdentifier String Identifier
     * @param strAddiParam String Additional parameter
     * @param strPrimaryMailID String Primary Mail ID
     * @exception throws TTKException
     */
    public void insertRecord(String strIdentifier, ArrayList alAddiParam, String strPrimaryMailID) throws TTKException {
    	Connection conn = null;
		CallableStatement cStmtObject = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = conn.prepareCall(strProcFormMessage);//MSG -D
			cStmtObject.setString(1,strIdentifier);//MSG -D
			cStmtObject.setString(2,(String)alAddiParam.get(0));//CALL LOG SEQ ID
			cStmtObject.setLong(3,(Long)alAddiParam.get(2));//ADDED BY
			cStmtObject.registerOutParameter(4, Types.VARCHAR);//OUT PARAM  - DESTINATION MSG SEQ ID
			cStmtObject.setString(5,(String)alAddiParam.get(1));//MAIL ID
			cStmtObject.setString(6,(String)alAddiParam.get(1));//MAIL ID
			cStmtObject.execute();		
		
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl insertRecord()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl insertRecord()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    }//end of insertRecord(long lngPatGenDtlSeqID,String strIdentifier,Long lngUserID)
    
    
    /**
     * This method inserts record after generating the report in ReportScheduler   
     * @param strIdentifier String Identifier
     * @param strAddiParam String Additional parameter
     * @param strPrimaryMailID String Primary Mail ID
     * @exception throws TTKException
     */
    public void insertCustomerRecord(String strIdentifier, ArrayList alAddiParam, String strPrimaryMailID,String strFileName) throws TTKException {
    	Connection conn = null;
		CallableStatement cStmtObject = null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject=conn.prepareCall(strProcCustomerFormMessage);//MSG -D
			cStmtObject.setString(1,strIdentifier);//MSG -D
			cStmtObject.setString(2,(String)alAddiParam.get(0));//CALL LOG SEQ ID
			cStmtObject.setLong(3,0);//ADDED BY
			cStmtObject.registerOutParameter(4, Types.VARCHAR);//OUT PARAM  - DESTINATION MSG SEQ ID
			cStmtObject.setString(5,(String)alAddiParam.get(1));//MAIL ID
			cStmtObject.setString(6,(String)alAddiParam.get(1));//MAIL ID
			cStmtObject.setString(7,"");//MAIL ID
			cStmtObject.setString(8,strFileName);//MAIL ID
			cStmtObject.execute();		
		
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl insertRecord()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl insertRecord()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    }//end of insertCustomerRecord(long lngPatGenDtlSeqID,String strIdentifier,Long lngUserID)
    public HashMap<String,byte[]> getPreAuthProccessedFiles() throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
    	HashMap<String,byte[]> proccedFiles=new HashMap<String,byte[]>();
    	OracleResultSet rs=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPOPreAuthUpload);
			cStmtObject.registerOutParameter(1, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (OracleResultSet)cStmtObject.getObject(1);
			if(rs!=null){
			while(rs.next())	
				if(rs.getOPAQUE("xmlFile")!=null){
				proccedFiles.put(rs.getString("fileName"), rs.getOPAQUE("xmlFile").getBytes());
				}
			}
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
					throw new TTKException(sqlExp, "communication");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in PolicyDAOImpl getPolicyList()",sqlExp);
						throw new TTKException(sqlExp, "communication");
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
							throw new TTKException(sqlExp, "communication");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "communication");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
		return proccedFiles;
    
    
    }
    public HashMap<String,byte[]> getClaimsProccessedFiles() throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
    	HashMap<String,byte[]> proccedFiles=new HashMap<String,byte[]>();
    	OracleResultSet rs=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPOPreAuthUpload);
			cStmtObject.registerOutParameter(1, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (OracleResultSet)cStmtObject.getObject(1);
			if(rs!=null){
			while(rs.next())	
				if(rs.getOPAQUE("xmlFile")!=null){
				proccedFiles.put(rs.getString("fileName"), rs.getOPAQUE("xmlFile").getBytes());
				}
			}
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PolicyDAOImpl getPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "enrollment");
				}//end of catch (SQLException sqlExp)
        		finally{
        			try
            		{
            			if (cStmtObject != null) cStmtObject.close();
            		}//end of try
            		catch (SQLException sqlExp)
            		{
            			log.error("Error while closing the Statement in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            			throw new TTKException(sqlExp, "communication");
            		}//end of catch (SQLException sqlExp)
            		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
            		{
            			try
            			{
            				if(conn != null) conn.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Connection in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            				throw new TTKException(sqlExp, "communication");
            			}//end of catch (SQLException sqlExp)
            		}//end of finally Connection Close
        		}
        		
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs=null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return proccedFiles;
    }//getClaimsProccessedFiles
    public HashMap<String,byte[]> getPersionProccessedFiles() throws TTKException{

    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
    	HashMap<String,byte[]> proccedFiles=new HashMap<String,byte[]>();
    	OracleResultSet rs=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPOPreAuthUpload);
			cStmtObject.registerOutParameter(1, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (OracleResultSet)cStmtObject.getObject(1);
			if(rs!=null){
			while(rs.next())	
				if(rs.getOPAQUE("xmlFile")!=null){
				proccedFiles.put(rs.getString("fileName"), rs.getOPAQUE("xmlFile").getBytes());
				}
			}
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PolicyDAOImpl getPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "enrollment");
				}//end of catch (SQLException sqlExp)
        		finally{
        			try
            		{
            			if (cStmtObject != null) cStmtObject.close();
            		}//end of try
            		catch (SQLException sqlExp)
            		{
            			log.error("Error while closing the Statement in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            			throw new TTKException(sqlExp, "communication");
            		}//end of catch (SQLException sqlExp)
            		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
            		{
            			try
            			{
            				if(conn != null) conn.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Connection in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            				throw new TTKException(sqlExp, "communication");
            			}//end of catch (SQLException sqlExp)
            		}//end of finally Connection Close
        		}
        		
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs=null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return proccedFiles;
    }
    
   
   
    public Object[] updateDhpoUplodedFileStatus(DhpoWebServiceVO dhpoWebServiceVO) throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
        Object obj[]=new Object[1];
    	OracleResultSet rs=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPOPreAuthUpload);
			cStmtObject.registerOutParameter(1, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (OracleResultSet)cStmtObject.getObject(1);
			if(rs!=null){
			}
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PolicyDAOImpl getPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "enrollment");
				}//end of catch (SQLException sqlExp)
        		finally{
        			try
            		{
            			if (cStmtObject != null) cStmtObject.close();
            		}//end of try
            		catch (SQLException sqlExp)
            		{
            			log.error("Error while closing the Statement in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            			throw new TTKException(sqlExp, "communication");
            		}//end of catch (SQLException sqlExp)
            		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
            		{
            			try
            			{
            				if(conn != null) conn.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Connection in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            				throw new TTKException(sqlExp, "communication");
            			}//end of catch (SQLException sqlExp)
            		}//end of finally Connection Close
        		}
        		
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs=null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return obj;
    }//updateDhpoUplodedFileStatus()
    
    public Object[] uploadDhpoGlobalNetNewTransactonDetails(DhpoWebServiceVO dhpoWebServiceVO) throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
		Object[] resObj=new Object[1];
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPOGlobalNetNewTransaction);
			XMLType poXML = null;
			  
			  String xmlFileContent=dhpoWebServiceVO.getXmlFileContent() ;
			  xmlFileContent=xmlFileContent.replaceAll("[^\\x20-\\x7e\n]", "");	
			poXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), xmlFileContent);
			
			cStmtObject.setString(1, null);
			cStmtObject.setString(2, dhpoWebServiceVO.getFileID());			
			cStmtObject.setString(3, dhpoWebServiceVO.getFileName());			
			cStmtObject.setObject(4, poXML);
			cStmtObject.setString(5, dhpoWebServiceVO.getFileType());
			cStmtObject.setString(6, dhpoWebServiceVO.getDownloadStatus());
			cStmtObject.setInt(7, dhpoWebServiceVO.getTransactionResult());
			cStmtObject.setString(8, dhpoWebServiceVO.getErrorMessage());
			cStmtObject.setString(9, "N");
			cStmtObject.registerOutParameter(10, OracleTypes.VARCHAR);
            cStmtObject.execute();
            resObj[0]=cStmtObject.getString(10);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl uploadDhpoGlobalNetNewTransactonDetails()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl uploadDhpoGlobalNetNewTransactonDetails()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return resObj;
    }//uploadDhpoGlobalNetNewTransactonDetails()
    
    
    
    /**
     * This method get the Dhpo New Transacton Details records where bifurcation not done   
     * @return This method get the Dhpo New Transacton Details records where bifurcation not done   
     * @exception throws TTKException
     */
    public List<DhpoWebServiceVO> getDhpoNewTransactonDetails() throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
		ResultSet rs=null;
		DhpoWebServiceVO webServiceVO=null;
		List<DhpoWebServiceVO> listVOs=new ArrayList<DhpoWebServiceVO>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strGetDHPOGlobalNetNewTransaction);
			cStmtObject.registerOutParameter(1, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (OracleResultSet)cStmtObject.getObject(1);
			if(rs!=null){
				while(rs.next()){
					
					webServiceVO=new DhpoWebServiceVO();
					webServiceVO.setFileID(rs.getString("FILE_ID"));
					webServiceVO.setFileName(rs.getString("FILE_NAME"));
					webServiceVO.setFileType(rs.getString("FILE_TYPE"));
					webServiceVO.setDownloadStatus(rs.getString("DOWN_LOAD_STATUS"));
					webServiceVO.setTransactionResult(rs.getInt("RESULT_TYPE"));					
					webServiceVO.setXmlFileReader(rs.getCharacterStream("XML_FILE"));					
					
					listVOs.add(webServiceVO);
				}
			}
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PolicyDAOImpl getPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "enrollment");
				}//end of catch (SQLException sqlExp)
        		finally{
        			try
            		{
            			if (cStmtObject != null) cStmtObject.close();
            		}//end of try
            		catch (SQLException sqlExp)
            		{
            			log.error("Error while closing the Statement in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            			throw new TTKException(sqlExp, "communication");
            		}//end of catch (SQLException sqlExp)
            		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
            		{
            			try
            			{
            				if(conn != null) conn.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Connection in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            				throw new TTKException(sqlExp, "communication");
            			}//end of catch (SQLException sqlExp)
            		}//end of finally Connection Close
        		}
        		
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs=null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return listVOs;
    }//getDhpoNewTransactonDetails
    public Object saveBifurcationDetails(List<DhpoWebServiceVO> dhpoWebServiceVOs) throws TTKException{

    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
		XMLType poXML = null;
		int[]result=null;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strSaveBifurcationDetails);
			
			SAXReader saxReader	=	new SAXReader();
			
			if(dhpoWebServiceVOs!=null&&dhpoWebServiceVOs.size()>0){
				
				for(DhpoWebServiceVO dhpoWebServiceVO:dhpoWebServiceVOs){
									
				   Document document=saxReader.read(new StringReader(dhpoWebServiceVO.getXmlFileContent()));	
				
					poXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), document.asXML());
					
					cStmtObject.setString(1, null);
					cStmtObject.setString(2, dhpoWebServiceVO.getFileID());
					cStmtObject.setString(3, dhpoWebServiceVO.getFileName());
					cStmtObject.setObject(4, poXML);
					cStmtObject.setString(5, dhpoWebServiceVO.getFileType());
					cStmtObject.setString(6, dhpoWebServiceVO.getDownloadStatus());
					cStmtObject.setInt(7, dhpoWebServiceVO.getTransactionResult());
					cStmtObject.setString(8, dhpoWebServiceVO.getErrorMessage());
					cStmtObject.setString(9, dhpoWebServiceVO.getClaimFrom());
					if(dhpoWebServiceVO.getDhpoTxDate()!=null){						
					cStmtObject.setString(10, dhpoWebServiceVO.getDhpoTxDate());
					}else cStmtObject.setString(10, null);
					cStmtObject.setString(11, dhpoWebServiceVO.getDhpoClaimRecCount());
					cStmtObject.setString(12, dhpoWebServiceVO.getGnClaimRecCount());
					cStmtObject.setString(13, dhpoWebServiceVO.getShClaimRecCount());
					//cStmtObject.registerOutParameter(10, OracleTypes.VARCHAR);
					cStmtObject.addBatch();
					
				}
			}
			cStmtObject.executeBatch();			
		}//end of try
		catch (Exception sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveBifurcationDetails()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl saveBifurcationDetails()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return result;
    
    }//saveBifurcationDetails
    
    public Object[] saveDhpoPreauthDownloadDetails(DhpoWebServiceVO dhpoWebServiceVO) throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
		Object[] resObj=new Object[1];
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPOPreauthDownload);
			XMLType poXML = null;
			  
			  String xmlFileContent=dhpoWebServiceVO.getXmlFileContent() ;
			  xmlFileContent=xmlFileContent.replaceAll("[^\\x20-\\x7e\n]", "");	
			poXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), xmlFileContent);
			
			cStmtObject.setString(1, null);
			cStmtObject.setString(2, dhpoWebServiceVO.getFileID());			
			cStmtObject.setString(3, dhpoWebServiceVO.getFileName());			
			cStmtObject.setObject(4, poXML);
			cStmtObject.setString(5, dhpoWebServiceVO.getFileType());
			cStmtObject.setString(6, dhpoWebServiceVO.getDownloadStatus());
			cStmtObject.setInt(7, dhpoWebServiceVO.getTransactionResult());
			cStmtObject.setString(8, dhpoWebServiceVO.getErrorMessage());			
			cStmtObject.registerOutParameter(9, OracleTypes.VARCHAR);
            cStmtObject.execute();
            resObj[0]=cStmtObject.getString(9);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveDhpoPreauthDownloadDetails()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl saveDhpoPreauthDownloadDetails()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return resObj;
    }//saveDhpoPreauthDownloadDetails()
   
    
    public Object[] saveDhpoGlobalnetPreauthDownloadDetails(DhpoWebServiceVO dhpoWebServiceVO) throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
		Object[] resObj=new Object[1];
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPOGlobalnetPreauthDownload);
			XMLType poXML = null;
			  
			  String xmlFileContent=dhpoWebServiceVO.getXmlFileContent() ;
			  xmlFileContent=xmlFileContent.replaceAll("[^\\x20-\\x7e\n]", "");	
			poXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), xmlFileContent);
			
			cStmtObject.setString(1, null);
			cStmtObject.setString(2, dhpoWebServiceVO.getFileID());			
			cStmtObject.setString(3, dhpoWebServiceVO.getFileName());			
			cStmtObject.setObject(4, poXML);
			cStmtObject.setString(5, dhpoWebServiceVO.getFileType());
			cStmtObject.setString(6, dhpoWebServiceVO.getDownloadStatus());
			cStmtObject.setInt(7, dhpoWebServiceVO.getTransactionResult());
			cStmtObject.setString(8, dhpoWebServiceVO.getErrorMessage());
			cStmtObject.setString(9, dhpoWebServiceVO.getBifurcationYN());
			cStmtObject.registerOutParameter(10, OracleTypes.VARCHAR);
            cStmtObject.execute();
            resObj[0]=cStmtObject.getString(10);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveDhpoGlobalnetPreauthDownloadDetails()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl saveDhpoGlobalnetPreauthDownloadDetails()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return resObj;
    }//saveDhpoPreauthDownloadDetails()
    
    
    /**
     * This method get the Dhpo New Transacton Details records where bifurcation not done   
     * @return This method get the Dhpo New Transacton Details records where bifurcation not done   
     * @exception throws TTKException
     */
    public List<DhpoWebServiceVO> getDhpoGlobalnetPreAuthTransactonDetails() throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
		ResultSet rs=null;
		DhpoWebServiceVO webServiceVO=null;
		List<DhpoWebServiceVO> listVOs=new ArrayList<DhpoWebServiceVO>();
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strGetDHPOGlobalNetPreAuthTransactionDetails);
			cStmtObject.registerOutParameter(1, OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (OracleResultSet)cStmtObject.getObject(1);
			if(rs!=null){
				while(rs.next()){
					
					webServiceVO=new DhpoWebServiceVO();
					webServiceVO.setFileID(rs.getString("FILE_ID"));
					webServiceVO.setFileName(rs.getString("FILE_NAME"));
					webServiceVO.setFileType(rs.getString("FILE_TYPE"));
					webServiceVO.setDownloadStatus(rs.getString("DOWN_LOAD_STATUS"));
					webServiceVO.setTransactionResult(rs.getInt("RESULT_TYPE"));					
					webServiceVO.setXmlFileReader(rs.getCharacterStream("XML_FILE"));					
					
					listVOs.add(webServiceVO);
				}
			}
			
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
		}//end of catch (Exception exp)
		finally
		{
        	/* Nested Try Catch to ensure resource closure */ 
        	try // First try closing the Statement
        	{
        		try
				{
					if (rs != null) rs.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in PolicyDAOImpl getPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "enrollment");
				}//end of catch (SQLException sqlExp)
        		finally{
        			try
            		{
            			if (cStmtObject != null) cStmtObject.close();
            		}//end of try
            		catch (SQLException sqlExp)
            		{
            			log.error("Error while closing the Statement in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            			throw new TTKException(sqlExp, "communication");
            		}//end of catch (SQLException sqlExp)
            		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
            		{
            			try
            			{
            				if(conn != null) conn.close();
            			}//end of try
            			catch (SQLException sqlExp)
            			{
            				log.error("Error while closing the Connection in MaintenanceDAOImpl getClaimsProccessedFiles()",sqlExp);
            				throw new TTKException(sqlExp, "communication");
            			}//end of catch (SQLException sqlExp)
            		}//end of finally Connection Close
        		}
        		
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		rs=null;
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return listVOs;
    }//getDhpoNewTransactonDetails
    
    
    
   //================
    public Object[] uploadDhpoNewTransactonDetails(DhpoWebServiceVO dhpoWebServiceVO) throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
		Object[] resObj=new Object[1];
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strDHPONewTransaction);
			XMLType poXML = null;
			  
			  String xmlFileContent=dhpoWebServiceVO.getXmlFileContent() ;
			  xmlFileContent=xmlFileContent.replaceAll("[^\\x20-\\x7e\n]", "");
			poXML = XMLType.createXML(((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()), xmlFileContent);
			
			cStmtObject.setString(1, null);
			cStmtObject.setString(2, dhpoWebServiceVO.getFileID());			
			cStmtObject.setString(3, dhpoWebServiceVO.getFileName());			
			cStmtObject.setObject(4, poXML);
			cStmtObject.setString(5, dhpoWebServiceVO.getFileType());
			cStmtObject.setString(6, dhpoWebServiceVO.getDownloadStatus());
			cStmtObject.setInt(7, dhpoWebServiceVO.getTransactionResult());
			cStmtObject.setString(8, dhpoWebServiceVO.getErrorMessage());	
			cStmtObject.registerOutParameter(9, OracleTypes.VARCHAR);
            cStmtObject.execute();
            resObj[0]=cStmtObject.getString(9);
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl dhpoNewTransacton()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl dhpoNewTransacton()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return resObj;
    }//uploadDhpoNewTransactonDetails()
    
    //=======================
    
    
    public boolean isMemberExist(String memID) throws TTKException{
    	Connection conn = null;
    	OracleCallableStatement cStmtObject = null;
    	boolean isMemExist=false;
		try{
			conn = ResourceManager.getConnection();
			cStmtObject = (OracleCallableStatement)((OracleConnection)((WrappedConnectionJDK6)conn).getUnderlyingConnection()).prepareCall(strisMemberExist);
			
			cStmtObject.setString(1, memID);
			cStmtObject.registerOutParameter(2, OracleTypes.VARCHAR);
            cStmtObject.execute();
            isMemExist=new Boolean(cStmtObject.getString(2));
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "communication");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "communication");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl dhpoNewTransacton()",sqlExp);
        			throw new TTKException(sqlExp, "communication");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl dhpoNewTransacton()",sqlExp);
        				throw new TTKException(sqlExp, "communication");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "communication");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return isMemExist;
    }//uploadDhpoNewTransactonDetails()
    
    
    public long savePharmacyDetails(PharmacyVO pharmacyVO) throws TTKException{
	   	Connection conn = null;
	   	CallableStatement cStmtObject=null;
	   	long lRowProcessed = 0;
	   	try{
	   		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strSavePharmacy);
    		if(pharmacyVO.getPharmacySeqId() != null){
    			cStmtObject.setLong(1,pharmacyVO.getPharmacySeqId());
    		}//end of if(daycareGroupVO.getGroupID() != null)
    		else{
    			cStmtObject.setString(1,null);
    		}//end of else
    		
    		cStmtObject.setString(2,pharmacyVO.getTradeName());
    		cStmtObject.setString(3,pharmacyVO.getScientificName());
    		cStmtObject.setString(4,pharmacyVO.getShortDesc());
    		cStmtObject.setString(5,pharmacyVO.getActivityDesc());
    		if(pharmacyVO.getStartDate()!=null)
    			cStmtObject.setTimestamp(6, new Timestamp(pharmacyVO.getStartDate().getTime()));
    		else
    			cStmtObject.setString(6,null);
    		if(pharmacyVO.getEndDate()!=null)
    			cStmtObject.setTimestamp(7, new Timestamp(pharmacyVO.getEndDate().getTime()));
    		else
    			cStmtObject.setString(7,null);
    		if(pharmacyVO.getAddedDate()!=null)
    			cStmtObject.setTimestamp(8, new Timestamp(pharmacyVO.getAddedDate().getTime()));
    		else
    			cStmtObject.setString(8,null);
    		cStmtObject.setString(9,pharmacyVO.getScientificCode());
    		cStmtObject.setBigDecimal(10,pharmacyVO.getUnitPrice());
    		cStmtObject.setBigDecimal(11,pharmacyVO.getPackagePrice());
    		cStmtObject.setBigDecimal(12,pharmacyVO.getGranularUnit());
    		cStmtObject.setString(13,pharmacyVO.getGender());
    		cStmtObject.setString(14,pharmacyVO.getExclusionYN());
    		//cStmtObject.setString(15,pharmacyVO.getQatarExclusionYN());
    		cStmtObject.setString(15,pharmacyVO.getRecFlag());
    		cStmtObject.setString(16,pharmacyVO.getStatus());
    		cStmtObject.setString(17,pharmacyVO.getRouteOfAdmin());
    		cStmtObject.setString(18,pharmacyVO.getRegisteredOwner());
    		cStmtObject.setString(19,pharmacyVO.getIngStrength());
    		cStmtObject.setString(20,pharmacyVO.getDoasage());
    		cStmtObject.setLong(21,pharmacyVO.getAddedBy());
    		cStmtObject.setString(22,"");//ICD List
    		cStmtObject.setString(23,pharmacyVO.getDdcId());
    		cStmtObject.setString(24,pharmacyVO.getQatarCode());
    		
    		if("on".equalsIgnoreCase(pharmacyVO.getIsReviewedByVidalCheckBox()))
    			pharmacyVO.setIsReviewedByVidalCheckBox("Y");
    		else
    			pharmacyVO.setIsReviewedByVidalCheckBox("N");
    		
    		cStmtObject.setString(25,pharmacyVO.getIsReviewedByVidalCheckBox());
    		cStmtObject.registerOutParameter(26,Types.INTEGER);
    		cStmtObject.execute();
    		lRowProcessed = cStmtObject.getInt(26);
    		log.info("iResult value is :"+lRowProcessed);
    	}//end of try
	   	catch (SQLException sqlExp) 
    	{ 
    		throw new TTKException(sqlExp, "maintenancerules");
    	}//end of catch (SQLException sqlExp) 
    	catch (Exception exp) 
    	{
    		throw new TTKException(exp, "maintenancerules");
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
        			log.error("Error while closing the Statement in MaintenanceDAOImpl savePharmacyDetails()",sqlExp);
        			throw new TTKException(sqlExp, "maintenancerules");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MaintenanceDAOImpl savePharmacyDetails()",sqlExp);
        				throw new TTKException(sqlExp, "maintenancerules");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "maintenancerules");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return lRowProcessed;
	}//savePharmacyDetails()
    
    
    public ArrayList getPharmacyList(ArrayList alSearchObjects) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PharmacySearchVO vo= null;
		try{
			/*for(int i=0; i<alSearchObjects.size();i++)
				  */
			conn = ResourceManager.getConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPharmacyList);
			
			cStmtObject.setString(1,(String)alSearchObjects.get(0));//sDdcCode
			cStmtObject.setString(2,(String)alSearchObjects.get(1));//sShortDesc
			cStmtObject.setString(3,(String)alSearchObjects.get(2));//sFullDesc	
			cStmtObject.setString(4,(String)alSearchObjects.get(3));//sGender
			cStmtObject.setString(5,(String)alSearchObjects.get(4));//sQatarExcYN
			cStmtObject.setString(6,(String)alSearchObjects.get(5));//sReviewed
			cStmtObject.setString(7,(String)alSearchObjects.get(6));//sStatus
			cStmtObject.setString(8,(String)alSearchObjects.get(7));//SORT_VAR
			cStmtObject.setString(9,(String)alSearchObjects.get(8));//SORT_ORDER
			cStmtObject.setString(10,(String)alSearchObjects.get(9));//START_NUM
			cStmtObject.setString(11,(String)alSearchObjects.get(10));//END_NUM
			cStmtObject.registerOutParameter(12,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(12);
			
			if(rs != null){
				while(rs.next()){
					vo = new PharmacySearchVO();
					vo.setPharSeqId(TTKCommon.checkNull(rs.getString("ACT_MAS_DTL_SEQ_ID")));
					vo.setDdcCode(TTKCommon.checkNull(rs.getString("DDC_CODE")));
					vo.setShortDesc(TTKCommon.checkNull(rs.getString("SHORT_DESC")));
					vo.setStartDate(TTKCommon.getFormattedDate(rs.getDate("START_DATE")));
					if(rs.getString("END_DATE")!=null)
						vo.setEndDate(TTKCommon.getFormattedDate(rs.getDate("END_DATE")));
					else
						vo.setEndDate("");
					vo.setGender(TTKCommon.checkNull(rs.getString("GENDER")));
					vo.setQatarExcYN(TTKCommon.checkNull(rs.getString("EXCLUTION_YN")));
					vo.setStatus(TTKCommon.checkNull(rs.getString("STATUS")));
					vo.setReviewedYN(TTKCommon.checkNull(rs.getString("REVIEWD_YN")));
					alResultList.add(vo);
				}//end of while(rs.next())
			}//end of if(rs != null)			
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getPharmacyList()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getPharmacyList()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getPharmacyList()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getGroupList(ArrayList alSearchCriteria)

    
    
    
   
    
    public PharmacyVO getPharmacyDetail(Long pharmacySeqId,String reviewedYN) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
    	PharmacyVO vo = new PharmacyVO();
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPharmacyDetail);
    		cStmtObject.setLong(1,pharmacySeqId);
    		cStmtObject.setString(2,reviewedYN);
    		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(3);
			
			if(rs != null){
				if(rs.next()){
					vo.setTradeName(TTKCommon.checkNull(rs.getString("TRADE_NAME")));
		    		vo.setScientificName(TTKCommon.checkNull(rs.getString("SCIENTIFIC_NAME")));
		    		vo.setShortDesc(TTKCommon.checkNull(rs.getString("SHORT_DESCRIPTION")));
		    		vo.setActivityDesc(TTKCommon.checkNull(rs.getString("ACTIVITY_DESCRIPTION")));
		    		vo.setStartDate(new Date(rs.getTimestamp("START_DATE").getTime()));
		    		if(rs.getTimestamp("END_DATE")!=null)
		    			vo.setEndDate(new Date(rs.getTimestamp("END_DATE").getTime()));
		    		if(rs.getTimestamp("ADDED_DATE")!=null)
		    			vo.setAddedDate(new Date(rs.getTimestamp("ADDED_DATE").getTime()));
		    		vo.setScientificCode(TTKCommon.checkNull(rs.getString("SCIENTIFIC_CODE")));
		    		if(rs.getString("UNIT_PRICE")!=null)
		    			vo.setUnitPrice(new BigDecimal(rs.getString("UNIT_PRICE")));
		    		if(rs.getString("PACKAGE_PRICE")!=null)
		    			vo.setPackagePrice(new BigDecimal(rs.getString("PACKAGE_PRICE")));
		    		if(rs.getString("GRANULAR_UNIT")!=null)
		    			vo.setGranularUnit(new BigDecimal(rs.getString("GRANULAR_UNIT")));
		    		vo.setGender(TTKCommon.checkNull(rs.getString("GENDER")));
		    		vo.setExclusionYN(TTKCommon.checkNull(rs.getString("EXCLUTION_YN")));
		    		vo.setQatarExclusionYN(TTKCommon.checkNull(rs.getString("EXCLUTION_YN")));
		    		vo.setRecFlag(TTKCommon.checkNull(rs.getString("REC_FLAG")));
		    		vo.setStatus(TTKCommon.checkNull(rs.getString("STATUS")));
		    		vo.setRouteOfAdmin(TTKCommon.checkNull(rs.getString("ROUTE_OF_ADMIN")));
		    		vo.setRegisteredOwner(TTKCommon.checkNull(rs.getString("REGISTERED_OWNER")));
		    		vo.setIngStrength(TTKCommon.checkNull(rs.getString("INGREDIENT_STRENGTH")));
		    		vo.setDoasage(TTKCommon.checkNull(rs.getString("DOSAGE_FORM_PACKAGE")));
		    		vo.setDdcId(TTKCommon.checkNull(rs.getString("DDC_ID")));
		    		vo.setQatarCode(TTKCommon.checkNull(rs.getString("QATAR_CODE")));
		    		vo.setActivityCode(TTKCommon.checkNull(rs.getString("ACTIVITY_CODE")));
		    		vo.setIsReviewedByVidal(TTKCommon.checkNull(rs.getString("REVIEWD_YN")));
		    		vo.setPharmacySeqId(new Long(rs.getLong("ACT_MAS_DTL_SEQ_ID")));
				}//end of while(rs.next())
			}//end of if(rs != null)
    	}//end of try
    	catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "maintenance");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "maintenance");
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
					log.error("Error while closing the Resultset in MaintenanceDAOImpl getPharmacyDetail()",sqlExp);
					throw new TTKException(sqlExp, "maintenance");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{

					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MaintenanceDAOImpl getPharmacyDetail()",sqlExp);
						throw new TTKException(sqlExp, "maintenance");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MaintenanceDAOImpl getPharmacyDetail()",sqlExp);
							throw new TTKException(sqlExp, "maintenance");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    	return vo;
    }//end of getPharmacyDetail(Long pharmacySeqId)
    
    public ArrayList<Object> getDocsUploadsList(ArrayList<Object> alDocsUploadList,Long seqID, String typeId) throws TTKException {
		  
		 int iResult = 0;
		 Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	ResultSet rs1 = null;
	    	PreAuthDetailVO preAuthDetailVO=null;
//	    	ArrayList alDocList = new ArrayList();
	    	try{
	    		conn = ResourceManager.getConnection();
	    		cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetDocsUploadsList);
	    		cStmtObject.setLong(1,seqID);
	    		cStmtObject.setString(2,typeId);
	    		cStmtObject.registerOutParameter(3,OracleTypes.CURSOR);
	    		cStmtObject.execute();
				rs1 = (java.sql.ResultSet)cStmtObject.getObject(3);
				if(rs1 != null){
					while(rs1.next()){	
						preAuthDetailVO = new PreAuthDetailVO();
						preAuthDetailVO.setDescription(TTKCommon.checkNull(rs1.getString("file_desc")));
						preAuthDetailVO.setMouDocPath(TTKCommon.checkNull(rs1.getString("file_path")));
						preAuthDetailVO.setMouDocSeqID(((long) rs1.getLong("docs_seq_id")));
						preAuthDetailVO.setFileName(TTKCommon.checkNull(rs1.getString("file_name")));
		                if(rs1.getString("added_date") != null){
		                	preAuthDetailVO.setDateTime(rs1.getString("added_date"));
						}//end of if(rs.getString("ADDED_DATE") != null)
		                preAuthDetailVO.setUserId(TTKCommon.checkNull(rs1.getString("contact_name")));
		                alDocsUploadList.add(preAuthDetailVO);
					}//end of while(rs.next())
				}//end of if(rs != null)
		    	//return (ArrayList<Object>)alMouList;           
	        }//end of try
	    	catch (SQLException sqlExp)
	        {
	            throw new TTKException(sqlExp, "maintenance");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "maintenance");
	        }//end of catch (Exception exp)
	        finally
			{
	        	// Nested Try Catch to ensure resource closure
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if(rs1 != null) rs1.close();
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
	        			throw new TTKException(sqlExp, "maintenance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
	        				throw new TTKException(sqlExp, "maintenance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "maintenance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		rs1 = null;
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	 return alDocsUploadList;
	    }
    
  
    
/*    public ArrayList getClaimDocDBFile(String ClaimSeqId)throws TTKException{
		
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		Blob blob	=	null;
		ArrayList arraylist = new ArrayList();
		InputStream iStream	=	new ByteArrayInputStream(new String("").getBytes());
		String fileType = "";
		byte[] blobAsBytes = null;
		String imageString = "";
		try{
			conn = ResourceManager.getConnection();
	                 
	         cStmtObject = conn.prepareCall(strGetClaimFile);
	       
	        cStmtObject.setString(1, ClaimSeqId);
			cStmtObject.registerOutParameter(2,Types.BLOB);
			cStmtObject.registerOutParameter(3,Types.VARCHAR);
			cStmtObject.execute();
			
			rs= cStmtObject.executeQuery();
			
			if(rs != null){
				
					blob	=	(BLOB) cStmtObject.getBlob(2);
					if(blob!= null){
					int blobLength = (int) blob.length();  
					blobAsBytes = blob.getBytes(1, blobLength);
					BASE64Encoder encoder = new BASE64Encoder();
				 //   imageString = encoder.encode(blobAsBytes); 
				    imageString = DatatypeConverter.printBase64Binary(blobAsBytes);
					}
					 
				if(blob!= null){
					iStream	=	blob.getBinaryStream();
					 fileType =  TTKCommon.checkNull(cStmtObject.getString(3));
				}
					
			}//end of if(rs != null)
					
				  
				arraylist.add(iStream);//taking for pdf data
				arraylist.add(fileType);
				arraylist.add(imageString);//taking for image data
				
	    }//end of try
	    catch (SQLException sqlExp) 
	    {
	        throw new TTKException(sqlExp, "preauth");
	    }//end of catch (SQLException sqlExp)
	    catch (Exception exp) 
	    {
	        throw new TTKException(exp, "preauth");
	    }//end of catch (Exception exp) 
	    finally
		{
	    	 Nested Try Catch to ensure resource closure  
	    	try // First try closing the Statement
	    	{
	    		try
	    		{	if (rs != null) rs.close();
	    			if (cStmtObject != null) cStmtObject.close();
	    		}//end of try
	    		catch (SQLException sqlExp)
	    		{
	    			log.error("Error while closing the Statement in PolicyDAOImpl getProviderDocs()",sqlExp);
	    			throw new TTKException(sqlExp, "preauth");
	    		}//end of catch (SQLException sqlExp)
	    		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	    		{
	    			try
	    			{
	    				if(conn != null) conn.close();
	    			}//end of try
	    			catch (SQLException sqlExp)
	    			{
	    				log.error("Error while closing the Connection in PolicyDAOImpl getProviderDocs()",sqlExp);
	    				throw new TTKException(sqlExp, "policy");
	    			}//end of catch (SQLException sqlExp)
	    		}//end of finally Connection Close
	    	}//end of try
	    	catch (TTKException exp)
	    	{
	    		throw new TTKException(exp, "preauth");
	    	}//end of catch (TTKException exp)
	    	finally // Control will reach here in anycase set null to the objects 
	    	{
	    		rs = null;
	    		cStmtObject = null;
	    		conn = null;
	    	}//end of finally
		}//end of finally
	    return arraylist;
 }*/
    
	 public int saveDocUploads(ArrayList alFileAUploadList,Long userSeqId,String Seq_ID,String origFileName,InputStream inputStream,int formFileSize,Long refSeqId) throws TTKException {
		 int iResult = 0;
	    	Connection conn = null;
	    	CallableStatement cStmtObject=null;
	    	try{
	    		conn = ResourceManager.getConnection();
	            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strSaveUploadFils);

	            cStmtObject.setLong(1,0);//docs_seq_id 
		        cStmtObject.setString(2,Seq_ID);//preauthseqid                
	            cStmtObject.setString(3,(String) alFileAUploadList.get(4));//source_id
	            cStmtObject.setString(4,(String) alFileAUploadList.get(2));//DropDown Value    (docs_desc)
	            cStmtObject.setString(5,(String) alFileAUploadList.get(1));//Doc Path (file_path)
	            cStmtObject.setString(6,origFileName);//Doc Name (file_name)
	            cStmtObject.setLong(7,userSeqId);//(added_by )
	            cStmtObject.setBinaryStream(8,inputStream,formFileSize);//FILE INPUT STREAM   (image_file)
	            if(refSeqId != null)
	            cStmtObject.setLong(9,refSeqId); //refSeqId;
	            else
	            cStmtObject.setString(9, new String(""));
	            
	            if(alFileAUploadList.get(3)!=null && alFileAUploadList.get(3).equals("Y")){
	            	cStmtObject.setString(10, (String)alFileAUploadList.get(3));
	            }else{
	            	cStmtObject.setString(10, null);
	            }
	            cStmtObject.registerOutParameter(11,Types.INTEGER);//ROW_PROCESSED        
	            cStmtObject.execute();            
	            iResult  = cStmtObject.getInt(11);    
	            
	        }//end of try
	    	catch (SQLException sqlExp)
	        {
	            throw new TTKException(sqlExp, "maintenance");
	        }//end of catch (SQLException sqlExp)
	        catch (Exception exp)
	        {
	            throw new TTKException(exp, "maintenance");
	        }//end of catch (Exception exp)
	        finally
			{
	        	// Nested Try Catch to ensure resource closure
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in MaintenanceDAOImpl saveDocUploads()",sqlExp);
	        			throw new TTKException(sqlExp, "maintenance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in MaintenanceDAOImpl saveDocUploads()",sqlExp);
	        				throw new TTKException(sqlExp, "maintenance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "maintenance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
	    	return iResult;
	    }//
	 
		public int deleteDocsUpload(ArrayList<Object> alCertificateDelete,String gateway)throws TTKException
		{
			Connection conn=null;
			int iResult=0;
			CallableStatement cStmtObject=null;
			try{
				conn=ResourceManager.getConnection();		
				cStmtObject=(java.sql.CallableStatement)conn.prepareCall(strDeleteDocsUpload);			
				cStmtObject.setString(1,(String)alCertificateDelete.get(0));
				cStmtObject.registerOutParameter(2,OracleTypes.INTEGER);
				cStmtObject.execute();
				iResult=cStmtObject.getInt(2);
			}//end of try
			catch(SQLException sqlExp)
			{
				throw new TTKException(sqlExp, "maintenance");
			}//end of catch(SQLException sqlExp)
			catch(Exception exp)
			{
				throw new TTKException(exp, "maintenance");
			}//end of catch(Exception exp)
			finally
			{
	        	 //Nested Try Catch to ensure resource closure  
	        	try // First try closing the Statement
	        	{
	        		try
	        		{
	        			if (cStmtObject != null) cStmtObject.close();
	        		}//end of try
	        		catch (SQLException sqlExp)
	        		{
	        			log.error("Error while closing the Statement in MaintenanceDAOImpl deleteDocsUpload()",sqlExp);
	        			throw new TTKException(sqlExp, "maintenance");
	        		}//end of catch (SQLException sqlExp)
	        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
	        		{
	        			try
	        			{
	        				if(conn != null) conn.close();
	        			}//end of try
	        			catch (SQLException sqlExp)
	        			{
	        				log.error("Error while closing the Connection in MaintenanceDAOImpl deleteDocsUpload()",sqlExp);
	        				throw new TTKException(sqlExp, "maintenance");
	        			}//end of catch (SQLException sqlExp)
	        		}//end of finally Connection Close
	        	}//end of try
	        	catch (TTKException exp)
	        	{
	        		throw new TTKException(exp, "maintenance");
	        	}//end of catch (TTKException exp)
	        	finally // Control will reach here in anycase set null to the objects 
	        	{
	        		cStmtObject = null;
	        		conn = null;
	        	}//end of finally
			}//end of finally
			return iResult;
		}//end of deleteAssocCertificatesInfo(ArrayList<Object> alCertificateDelete)


		/*This is method will get the file from databse*/
		public Blob getFile(String docs_seq_id){
	    	Connection connection = null;
	    	PreparedStatement preparedStatement = null;
	    	ResultSet resultSet = null;
	    	try{
	    		connection = ResourceManager.getConnection();
	    		preparedStatement=connection.prepareStatement(strFile_data);
	    		preparedStatement.setString(1, docs_seq_id);
	    		resultSet= preparedStatement.executeQuery();		
	    		Blob blobData=null;
	    		if(resultSet != null){
    				while(resultSet.next()){	
    	            blobData=resultSet.getBlob("IMAGE_FILE");
	    			}
	    		}
	    		return blobData;
		}catch(SQLException exception){
			return null;
		}catch (TTKException e) {
			e.printStackTrace();
			return null;
		}finally{
			try{
			CommonClosure.closeOpenResources(resultSet, preparedStatement,null, connection, null, this, "getFile");
			}catch(TTKException ttkException){
				ttkException.printStackTrace();
			}
		}
		}//End of getFile()


		public PreAuthDetailVO getStatus(String authNo, String type) {
			Connection connection = null;
	    	PreparedStatement preparedStatement = null;
	    	ResultSet resultSet = null;
	    	try{
	    		connection = ResourceManager.getConnection();
	    		if(type.equals("Pre-Auth")){
	    			preparedStatement=connection.prepareStatement(strPreAuthStatusChecker);
	    		}else if(type.equals("Claim")){
	    			preparedStatement=connection.prepareStatement(strClaimStatusChecker);
	    		}
	    		preparedStatement.setString(1, authNo);
	    		resultSet= preparedStatement.executeQuery();		
	    		PreAuthDetailVO preAuthDetailVO=null;
	    		if(resultSet != null){
					while(resultSet.next()){	
						preAuthDetailVO = new PreAuthDetailVO();
						if(type.equals("Pre-Auth")){
						preAuthDetailVO.setPreauthStatus(TTKCommon.checkNull(resultSet.getString("PAT_STATUS_TYPE_ID")));
						}else if(type.equals("Claim")){
							preAuthDetailVO.setClaimStatus(TTKCommon.checkNull(resultSet.getString("CLM_STATUS_TYPE_ID")));	
						}
					}//end of while(rs.next())
				}//end of if(rs != null)
	    		return preAuthDetailVO;
		}catch(SQLException sqlExp)
			{
			throw null;
		}//end of catch(SQLException sqlExp)
		catch(Exception exp)
		{
			throw null;
		}//end of catch(Exception exp)
		finally
		{
        	// Nested Try Catch to ensure resource closure
        	try // First try closing the Statement
        	{
        		try
        		{
        			if(resultSet != null) resultSet.close();
        			if (preparedStatement != null) preparedStatement.close();
        		}//end of try
        		catch (SQLException sqlExp)
        		{
        			log.error("Error while closing the Statement in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
        			throw new TTKException(sqlExp, "maintenance");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(connection != null) connection.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in PreauthDAOImpl getPreauthDocsUploadsList()",sqlExp);
        				throw new TTKException(sqlExp, "maintenance");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw null;
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		resultSet = null;
        		preparedStatement = null;
        		connection = null;
        	}//end of finally
		}//end of finally
		}


		
    
    
}//end of MaintenanceDAOImpl
