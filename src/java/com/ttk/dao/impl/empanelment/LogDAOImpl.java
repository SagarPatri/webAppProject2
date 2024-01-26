/**
 * @ (#) LogDAOImpl.java Sep 27, 2005
 * Project      :
 * File         : LogDAOImpl.java
 * Author       : Suresh.M
 * Company      :
 * Date Created : Sep 27, 2005
 *
 * @author       :Suresh.M
 * Modified by   : Ramakrishna K M
 * Modified date : Dec 28, 2005
 * Reason        : To make the change in the search query
 */

package com.ttk.dao.impl.empanelment;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import oracle.jdbc.driver.OracleTypes;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.administration.WebConfigLinkVO;
import com.ttk.dto.empanelment.LogVO;

public class LogDAOImpl implements BaseDAO,Serializable{
	
	private static Logger log = Logger.getLogger(LogDAOImpl.class);

	private static final String strHopitalLogList= "SELECT * FROM(SELECT LOG_SEQ_ID,A.HOSP_SEQ_ID,A.SYSTEM_GEN_YN,A.LOG_TYPE_ID,A.MOD_REASON_TYPE_ID,A.REFERENCE_DATE,A.REFERENCE_NO,A.REMARKS,B.CONTACT_NAME,A.ADDED_BY,A.ADDED_DATE,A.UPDATED_BY,A.UPDATED_DATE,C.DESCRIPTION,DENSE_RANK() OVER (ORDER BY #, ROWNUM) Q FROM TPA_HOSP_LOG A,TPA_USER_CONTACTS B,TPA_HOSP_LOG_CODE C";
	private static final String strAddUpdateLog = "{CALL TTK_UTIL_PKG.PR_USER_LOG(?,?,?,?,?,?,?,?,?,?)}";
    //private static final String strPolicylogList = "{CALL POLICY_ENROLLMENT_SQL_PKG.SELECT_POLICY_LOG(?,?,?,?,?,?,?)}";
	//private static final String strPolicylogList = "{CALL POLICY_ENROLLMENT_PKG.SELECT_POLICY_LOG(?,?,?,?,?,?,?)}";
	private static final String strPolicylogList = "{CALL policy_enrollment_pkg.select_policy_alert(?,?,?,?,?,?,?)}";
   // private static final String strAddPolicylog = "{CALL POLICY_ENROLLMENT_PKG.SAVE_POLICY_LOG(?,?,?,?,?,?,?,?)}";
    
	 private static final String strAddPolicylog = "INSERT INTO app.TPA_ENR_POLICY_LOG(POLICY_LOG_SEQ_ID,POLICY_SEQ_ID,ENDORSEMENT_SEQ_ID,REMARKS,SYSTEM_GEN_YN,ADDED_BY,ADDED_DATE) VALUES(tpa_enr_policy_log_seq.NEXTVAL,?,?,?,'N',?,SYSDATE)";
    
    
    private static final String strAddPreAuthLog = "{CALL PRE_AUTH_PKG.SAVE_PAT_LOG(?,?,?,?,?,?,?,?,?,?)}";
    //private static final String strPreauthLogList = "{CALL PRE_AUTH_SQL_PKG.SELECT_PAT_LOG(?,?,?,?,?,?,?,?)}";
    private static final String strPreauthLogList = "{CALL PRE_AUTH_PKG.SELECT_PAT_LOG(?,?,?,?,?,?,?,?)}";


    private static final String strAddUpdatePartnerLog = "{CALL ttk_util_pkg.ptnr_user_log(?,?,?,?,?,?,?,?,?,?)}";
	private static final String strPartnerLogList= "SELECT * FROM(SELECT LOG_SEQ_ID,A.PTNR_SEQ_ID,A.SYSTEM_GEN_YN,A.LOG_TYPE_ID,A.MOD_REASON_TYPE_ID,A.REFERENCE_DATE, A.REFERENCE_NO,A.REMARKS,B.CONTACT_NAME,A.ADDED_BY,A.ADDED_DATE,A.UPDATED_BY,A.UPDATED_DATE, C.DESCRIPTION,DENSE_RANK() OVER (ORDER BY #  , ROWNUM) Q FROM TPA_PARTNER_LOG A,TPA_USER_CONTACTS B,TPA_PARTNER_LOG_CODE C";
	private static final String strTariffActivityQuery = "{CALL hospital_empanel_pkg.Tariff_updation_Log(?,?,?,?,?,?)}";
	
	  private static final String strClaimPreauthLogList = "{CALL AUTHORIZATION_PKG.select_alert(?,?,?,?,?,?)}";
	  private static final String strAddClaimPreAuthLog = "{CALL AUTHORIZATION_PKG.user_create_log(?,?,?,?,?)}";
	  private static final String strTOBAlertLogList = "{CALL ADMINISTRATION_PKG.get_tob_alert(?,?,?,?,?,?,?,?,?)}";
	  
	
	private static final int LOG_SEQ_ID = 1;
	private static final int HOSP_SEQ_ID = 2;
	private static final int SYSTEM_GEN_YN = 3;
	private static final int LOG_TYPE_ID = 4;
	private static final int MOD_REASON_TYPE = 5;
	private static final int REFERENCE_DATE = 6;
	private static final int REFERENCE_NO = 7;
	private static final int REMARKS = 8;
	private static final int USER_SEQ_ID  = 9;
	private static final int ROW_PROCESSED = 10;
	private static final int PTNR_SEQ_ID = 2;

	/**
     * This method returns the ArrayList, which contains the LogVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of LogVO object's which contains the log details
     * @exception throws TTKException
     */
    public ArrayList getLogList(ArrayList alSearchObjects) throws TTKException
    {
        String strStaticQuery = strHopitalLogList;
        StringBuffer sbfDynamicQuery = new StringBuffer();
        LogVO logVO = null;
        Collection<Object> resultList = new ArrayList<Object>();
        Connection conn = null;
        CallableStatement cStmtObject=null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        String strInternalCode =null;
        String strUserTypeId =null;
        String strHospSeqId =null;
        String strFromDate=null;
        String strEndDate=null;  
        if(alSearchObjects != null && alSearchObjects.size() > 0)
        {
            strHospSeqId = TTKCommon.checkNull((String)alSearchObjects.get(0));
            strUserTypeId= TTKCommon.checkNull((String)alSearchObjects.get(1));
            strFromDate = TTKCommon.checkNull((String)alSearchObjects.get(2));
            strEndDate = TTKCommon.checkNull((String)alSearchObjects.get(3));
            if("TAR".equals(strUserTypeId))
            strInternalCode = TTKCommon.checkNull((String)alSearchObjects.get(4));
            //strBuildSql = strBuildSql+" WHERE A.HOSP_SEQ_ID ="+strHospSeqId+" AND A.added_by = B.contact_seq_id AND nvl('"+strUserTypeId+"', '-1') = decode('"+strUserTypeId+"', NULL, '-1', log_type_id)  AND trunc(A.added_date) BETWEEN nvl(to_date('"+strFromDate+"','dd/mm/yyyy'), to_date('01/01/2005','dd/mm/yyyy') ) AND nvl(to_date('"+strEndDate+"','dd/mm/yyyy'), SYSDATE) ";
            sbfDynamicQuery.append(" WHERE A.HOSP_SEQ_ID ="+strHospSeqId+" AND A.added_by = B.contact_seq_id AND nvl('"+strUserTypeId+"', '-1') = decode('"+strUserTypeId+"', NULL, '-1', A.log_type_id)  AND trunc(A.added_date) BETWEEN nvl(to_date('"+strFromDate+"','dd/mm/yyyy'), to_date('01/01/2005','dd/mm/yyyy') ) AND nvl(to_date('"+strEndDate+"','dd/mm/yyyy'), SYSDATE) AND A.LOG_TYPE_ID = C.LOG_TYPE_ID");
        }//end of if(alSearchCriteria != null && alSearchCriteria.size() > 0)
        //build the Order By Clause
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        //build the row numbers to be fetched
        sbfDynamicQuery.append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        try{
            conn = ResourceManager.getConnection();
            if(!"TAR".equals(strUserTypeId)){
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            }
            else{
            cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strTariffActivityQuery);
            cStmtObject.setString(1, strHospSeqId);
            cStmtObject.setString(2, strUserTypeId);
            cStmtObject.setString(3, strFromDate);
            cStmtObject.setString(4, strEndDate);
            cStmtObject.setString(5, strInternalCode);
            cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(6);
            }
            if(rs != null){
                while(rs.next()){
                    logVO = new LogVO();
                    if (rs.getTimestamp("ADDED_DATE") != null)
                    {
                        logVO.setLogDate(new java.util.Date(rs.getTimestamp("ADDED_DATE").getTime()));
                    }//end of if (rs.getTimestamp("ADDED_DATE") != null)
                    logVO.setLogDesc(TTKCommon.checkNull(rs.getString("REMARKS")));
                    if (rs.getString("LOG_SEQ_ID") != null)
                    {
                        logVO.setLogId(new Long(rs.getLong("LOG_SEQ_ID")));
                    }//end of if (rs.getString("LOG_SEQ_ID") != null)
                    logVO.setLogType(TTKCommon.checkNull(rs.getString("LOG_TYPE_ID")));
                    logVO.setUserName(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
                    if(rs.getString("HOSP_SEQ_ID") != null)
                    {
                        logVO.setHospSeqId(new Long(rs.getString("HOSP_SEQ_ID")));
                    }//end of if(rs.getString("HOSP_SEQ_ID") != null)
                    logVO.setSystemGenYN(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")));
                    if(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")).equalsIgnoreCase("Y"))
                    {
                        logVO.setImageName("SystemIcon");
                        logVO.setImageTitle("System Log");
                    }//end of else if(rs.getString("LOG_TYPE").equals("SYS"))
                    
                    logVO.setLogTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    if("TAR".equals(strUserTypeId)){
                    	logVO.setInternalCode(TTKCommon.checkNull(rs.getString("INTERNAL_CODE")));
                    }
                    resultList.add(logVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList) resultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
        }//end of catch (Exception exp)
        finally
		{
			/* Nested Try Catch to ensure resource closure */ 
			try // First try closing the result set
			{
				try
				{
					if (rs != null) rs.close();
					if (cStmtObject != null) cStmtObject.close();
				}//end of try
				catch (SQLException sqlExp)
				{
					log.error("Error while closing the Resultset in LogDAOImpl getLogList()",sqlExp);
					throw new TTKException(sqlExp, "log");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in LogDAOImpl getLogList()",sqlExp);
						throw new TTKException(sqlExp, "log");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in LogDAOImpl getLogList()",sqlExp);
							throw new TTKException(sqlExp, "log");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw exp;
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				cStmtObject=null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getLogList(ArrayList alSearchObjects)
    
    /**
     * This method returns the ArrayList, which contains the LogVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of LogVO object's which contains the log details
     * @exception throws TTKException
     */
    public ArrayList getPartnerLogList(ArrayList alSearchObjects) throws TTKException
    {
        String strStaticQuery = strPartnerLogList;
        StringBuffer sbfDynamicQuery = new StringBuffer();
        LogVO logVO = null;
        Collection<Object> resultList = new ArrayList<Object>();
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;     
        if(alSearchObjects != null && alSearchObjects.size() > 0)
        {
            String strPtnrSeqId = TTKCommon.checkNull((String)alSearchObjects.get(0));
            String strUserTypeId = TTKCommon.checkNull((String)alSearchObjects.get(1));
            String strFromDate = TTKCommon.checkNull((String)alSearchObjects.get(2));
            String strEndDate = TTKCommon.checkNull((String)alSearchObjects.get(3));
            //strBuildSql = strBuildSql+" WHERE A.HOSP_SEQ_ID ="+strHospSeqId+" AND A.added_by = B.contact_seq_id AND nvl('"+strUserTypeId+"', '-1') = decode('"+strUserTypeId+"', NULL, '-1', log_type_id)  AND trunc(A.added_date) BETWEEN nvl(to_date('"+strFromDate+"','dd/mm/yyyy'), to_date('01/01/2005','dd/mm/yyyy') ) AND nvl(to_date('"+strEndDate+"','dd/mm/yyyy'), SYSDATE) ";
            sbfDynamicQuery.append(" WHERE A.PTNR_SEQ_ID ="+strPtnrSeqId+" AND A.added_by = B.contact_seq_id AND nvl('"+strUserTypeId+"', '-1') = decode('"+strUserTypeId+"', NULL, '-1', A.log_type_id)  AND trunc(to_date(A.added_date,'dd/mm/RRRR')) BETWEEN nvl(to_date('"+strFromDate+"','dd/mm/RRRR') ,to_date('01/01/2005','dd/mm/RRRR') ) and  nvl(to_date('"+strEndDate+"','dd/mm/RRRR'), to_date(SYSDATE,'dd/mm/RRRR')) AND A.LOG_TYPE_ID = C.LOG_TYPE_ID");
        }//end of if(alSearchCriteria != null && alSearchCriteria.size() > 0)
        //build the Order By Clause
        strStaticQuery = TTKCommon.replaceInString(strStaticQuery,"#", (String)alSearchObjects.get(alSearchObjects.size()-4)+" "+(String)alSearchObjects.get(alSearchObjects.size()-3));
        //build the row numbers to be fetched
        sbfDynamicQuery.append( " )A WHERE Q >= "+(String)alSearchObjects.get(alSearchObjects.size()-2)+ " AND Q <= "+(String)alSearchObjects.get(alSearchObjects.size()-1));
        strStaticQuery = strStaticQuery + sbfDynamicQuery.toString();
        try{
            conn = ResourceManager.getConnection();
            pStmt = conn.prepareStatement(strStaticQuery);
            rs = pStmt.executeQuery();
            if(rs != null){
                while(rs.next()){
                    logVO = new LogVO();
                    if (rs.getTimestamp("ADDED_DATE") != null)
                    {
                        logVO.setLogDate(new java.util.Date(rs.getTimestamp("ADDED_DATE").getTime()));
                    }//end of if (rs.getTimestamp("ADDED_DATE") != null)
                    logVO.setLogDesc(TTKCommon.checkNull(rs.getString("REMARKS")));
                    if (rs.getString("LOG_SEQ_ID") != null)
                    {
                        logVO.setLogId(new Long(rs.getLong("LOG_SEQ_ID")));
                    }//end of if (rs.getString("LOG_SEQ_ID") != null)
                    logVO.setLogType(TTKCommon.checkNull(rs.getString("LOG_TYPE_ID")));
                    logVO.setUserName(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
                    if(rs.getString("PTNR_SEQ_ID") != null)
                    {
                        logVO.setPtnrSeqId(new Long(rs.getString("PTNR_SEQ_ID")));
                    }//end of if(rs.getString("HOSP_SEQ_ID") != null)
                    logVO.setSystemGenYN(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")));
                    if(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")).equalsIgnoreCase("Y"))
                    {
                        logVO.setImageName("SystemIcon");
                        logVO.setImageTitle("System Log");
                    }//end of else if(rs.getString("LOG_TYPE").equals("SYS"))
                    
                    logVO.setLogTypeDesc(TTKCommon.checkNull(rs.getString("DESCRIPTION")));
                    resultList.add(logVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList) resultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
					log.error("Error while closing the Resultset in LogDAOImpl getLogList()",sqlExp);
					throw new TTKException(sqlExp, "log");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (pStmt != null)	pStmt.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in LogDAOImpl getLogList()",sqlExp);
						throw new TTKException(sqlExp, "log");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in LogDAOImpl getLogList()",sqlExp);
							throw new TTKException(sqlExp, "log");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw exp;
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				pStmt = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getLogList(ArrayList alSearchObjects)
    
   /**
     * This method adds the log details
     * The method also calls another method on DAO to insert the log details to the database
     * @param logVO LogVO object which contains the log details to be added
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int addLog(LogVO logVO) throws TTKException{
    	int iResult =0;
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strAddUpdateLog);
    		if (logVO.getLogId()!= null)
    		{
    			cStmtObject.setLong(LOG_SEQ_ID,logVO.getLogId());
    		}//end of if (logVO.getLogId()!= null)
    		else
    		{
    			cStmtObject.setLong(LOG_SEQ_ID,0);
    		}//end of else
    		cStmtObject.setLong(HOSP_SEQ_ID,logVO.getHospSeqId());
    		cStmtObject.setString(SYSTEM_GEN_YN,"N");
    		cStmtObject.setString(LOG_TYPE_ID,"USR");
    		cStmtObject.setString(MOD_REASON_TYPE,null);
    		cStmtObject.setTimestamp(REFERENCE_DATE,new Timestamp(TTKCommon.getDate().getTime()));
    		cStmtObject.setString(REFERENCE_NO,null);
    		cStmtObject.setString(REMARKS,logVO.getLogDesc());
    		cStmtObject.setLong(USER_SEQ_ID,logVO.getUpdatedBy());
     		cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);
    		cStmtObject.execute();
    		iResult = cStmtObject.getInt(ROW_PROCESSED);
     	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "log");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "log");
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
        			log.error("Error while closing the Statement in LogDAOImpl addLog()",sqlExp);
        			throw new TTKException(sqlExp, "log");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LogDAOImpl addLog()",sqlExp);
        				throw new TTKException(sqlExp, "log");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "log");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
    }//end of addLog(LogVO logVO)
    
   
    
    
    
    /**
     * This method adds the log details
     * The method also calls another method on DAO to insert the log details to the database
     * @param logVO LogVO object which contains the log details to be added
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int addPartnerLog(LogVO logVO) throws TTKException{
    	int iResult =0;
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	try{
    		conn = ResourceManager.getConnection();
    		cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strAddUpdatePartnerLog);
    		if (logVO.getLogId()!= null)
    		{
    			cStmtObject.setLong(LOG_SEQ_ID,logVO.getLogId());
    			  
    		}//end of if (logVO.getLogId()!= null)
    		else
    		{
    			cStmtObject.setLong(LOG_SEQ_ID,0);
    		}//end of else
    		cStmtObject.setLong(PTNR_SEQ_ID, logVO.getPtnrSeqId());
			  

    		cStmtObject.setString(SYSTEM_GEN_YN,"N");
			  

    		cStmtObject.setString(LOG_TYPE_ID,"USR");
			  


    		cStmtObject.setString(MOD_REASON_TYPE,null);
		
			  

    		cStmtObject.setTimestamp(REFERENCE_DATE,new Timestamp(TTKCommon.getDate().getTime()));
		
			  

    		cStmtObject.setString(REFERENCE_NO,null);
			  


    		cStmtObject.setString(REMARKS,logVO.getLogDesc());
			  

    		cStmtObject.setLong(USER_SEQ_ID,logVO.getUpdatedBy());
			  

     		cStmtObject.registerOutParameter(ROW_PROCESSED,Types.INTEGER);
    		cStmtObject.execute();
    		iResult = cStmtObject.getInt(ROW_PROCESSED);
     	}//end of try
    	catch (SQLException sqlExp)
    	{
    		throw new TTKException(sqlExp, "log");
    	}//end of catch (SQLException sqlExp)
    	catch (Exception exp)
    	{
    		throw new TTKException(exp, "log");
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
        			log.error("Error while closing the Statement in LogDAOImpl addLog()",sqlExp);
        			throw new TTKException(sqlExp, "log");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LogDAOImpl addLog()",sqlExp);
        				throw new TTKException(sqlExp, "log");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "log");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    	return iResult;
    }//end of addLog(LogVO logVO)
    
    

    /**
     * This method returns the ArrayList, which contains the LogVO's which are populated from the database
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of LogVO'S object's which contains Log details
     * @exception throws TTKException
     */
    public ArrayList getPolicyLogList(ArrayList alSearchCriteria) throws TTKException{
        Collection<Object> alResultList = new ArrayList<Object>();
        Connection conn = null;
        CallableStatement cStmtObject=null;
        ResultSet rs = null;
        LogVO logVO = null;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPolicylogList);


            cStmtObject.setLong(1,(Long)alSearchCriteria.get(0));
            cStmtObject.setString(2,(String)alSearchCriteria.get(1));
            cStmtObject.setString(3,(String)alSearchCriteria.get(2));
            cStmtObject.setString(4,(String)alSearchCriteria.get(3));
           /* cStmtObject.setString(5,(String)alSearchCriteria.get(4));
            cStmtObject.setString(6,(String)alSearchCriteria.get(5));*/
            cStmtObject.setString(5,"log_date");
            cStmtObject.setString(6,"DESC");
            
            cStmtObject.registerOutParameter(7,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(7);
            if(rs != null){
                while(rs.next()){
                    logVO = new LogVO();
                   /* if(rs.getString("POLICY_LOG_SEQ_ID") != null){
                        logVO.setPolicyLogSeqID(new Long(rs.getLong("POLICY_LOG_SEQ_ID")));
                    }//end of if(rs.getString("POLICY_LOG_SEQ_ID") != null)
                    if(rs.getString("POLICY_SEQ_ID") != null){
                        logVO.setPolicySeqID(new Long(rs.getLong("POLICY_SEQ_ID")));
                    }//end of if(rs.getString("POLICY_SEQ_ID") != null)
                    if(rs.getString("MEMBER_SEQ_ID") != null){
                        logVO.setMemberSeqID(new Long(rs.getLong("MEMBER_SEQ_ID")));
                    }//end of if(rs.getString("MEMBER_SEQ_ID") != null)
                    logVO.setUserName(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));
                    if(rs.getString("ENDORSEMENT_SEQ_ID") != null){
                        logVO.setEndorsementSeqID(new Long(rs.getLong("ENDORSEMENT_SEQ_ID")));
                    }//end of if(rs.getString("ENDORSEMENT_SEQ_ID") != null)
                    logVO.setLogDesc(TTKCommon.checkNull(rs.getString("REMARKS")));
                    if (rs.getString("ADDED_DATE") != null){
                        logVO.setLogDate(new java.util.Date(rs.getTimestamp("ADDED_DATE").getTime()));
                    }//end of if (rs.getString("ADDED_DATE") != null)
                    if(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")).equalsIgnoreCase("Y")){
                        logVO.setImageName("SystemIcon");
                        logVO.setImageTitle("System Log");
                    }//end of if(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")).equalsIgnoreCase("Y"))
*/
                    
                    if (rs.getString("log_date") != null){
                        logVO.setLogDate(new java.util.Date(rs.getTimestamp("log_date").getTime()));
                    }
                    logVO.setLogTypeDesc(TTKCommon.checkNull(rs.getString("log_type")));
                    logVO.setLogDesc(TTKCommon.checkNull(rs.getString("remarks")));
                    logVO.setUserName(TTKCommon.checkNull(rs.getString("user_name")));
                    if(TTKCommon.checkNull(rs.getString("system_gen_yn")).equalsIgnoreCase("Y")){
                        logVO.setImageName("SystemIcon");
                        logVO.setImageTitle("System Log");
                    }
                    
                    alResultList.add(logVO);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
					log.error("Error while closing the Resultset in LogDAOImpl getPolicyLogList()",sqlExp);
					throw new TTKException(sqlExp, "log");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in LogDAOImpl getPolicyLogList()",sqlExp);
						throw new TTKException(sqlExp, "log");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in LogDAOImpl getPolicyLogList()",sqlExp);
							throw new TTKException(sqlExp, "log");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "log");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPolicyLogList(ArrayList alSearchCriteria)

    /**
     * This method adds the log details
     * @param logVO LogVO object which contains the log details to be added
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int addPolicyLog(LogVO logVO) throws TTKException {
        int iResult =0;
        Connection conn = null;
        CallableStatement cStmtObject=null;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strAddPolicylog);
            
            
            cStmtObject.setLong(1,logVO.getPolicySeqID());
            if(logVO.getEndorsementSeqID() != null){
                cStmtObject.setLong(2,logVO.getEndorsementSeqID());
            }
            else{
                cStmtObject.setString(2,null);
            }
            cStmtObject.setString(3,logVO.getLogDesc());
            cStmtObject.setLong(4,logVO.getUpdatedBy());
            cStmtObject.execute();
            iResult =1;
            
            

           /* if (logVO.getPolicyLogSeqID()!= null){
                cStmtObject.setLong(1,logVO.getPolicyLogSeqID());
            }//end of if (logVO.getPolicyLogSeqID()!= null)
            else{
                cStmtObject.setLong(1,0);
            }//end of else

            if(logVO.getPolicySeqID() != null){
                cStmtObject.setLong(2,logVO.getPolicySeqID());
            }//end of if(logVO.getPolicySeqID() != null)
            else{
                cStmtObject.setString(2,null);
            }//end of else

            if(logVO.getMemberSeqID() != null){
                cStmtObject.setLong(3,logVO.getMemberSeqID());
            }//end of if(logVO.getMemberSeqID() != null)
            else{
                cStmtObject.setString(3,null);
            }//end of else

            if(logVO.getEndorsementSeqID() != null){
                cStmtObject.setLong(4,logVO.getEndorsementSeqID());
            }//end of if(logVO.getEndorsementSeqID() != null)
            else{
                cStmtObject.setString(4,null);
            }//end of else
            cStmtObject.setString(5,"N");
            cStmtObject.setString(6,logVO.getLogDesc());
            cStmtObject.setLong(7,logVO.getUpdatedBy());
            cStmtObject.registerOutParameter(8,Types.INTEGER);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(8);
*/
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
        			log.error("Error while closing the Statement in LogDAOImpl addPolicyLog()",sqlExp);
        			throw new TTKException(sqlExp, "log");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LogDAOImpl addPolicyLog()",sqlExp);
        				throw new TTKException(sqlExp, "log");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "log");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//end of addPolicyLog(LogVO logVO)

    /**
     * This method returns the ArrayList, which contains the LogVO's which are populated from the database
     * @param alSearchCriteria ArrayList object which contains the search criteria
     * @return ArrayList of LogVO'S object's which contains Pre-Authorization Log details
     * @exception throws TTKException
     */
    public ArrayList getPreAuthLogList(ArrayList alSearchCriteria) throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        LogVO logVO = null;
        try{
        	conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strPreauthLogList);
              
              
              
              
              
              
              
              

            if(alSearchCriteria.get(0) != null){
            	cStmtObject.setLong(1,(Long)alSearchCriteria.get(0));//Mandatory
            }//end of if(alSearchCriteria.get(0) != null)
            else{
            	cStmtObject.setString(1,null);
            }//end of else

            if(alSearchCriteria.get(1) != null){
            	cStmtObject.setLong(2,(Long)alSearchCriteria.get(1));//Mandatory
            }//end of if(alSearchCriteria.get(1) != null)
            else{
            	cStmtObject.setString(2,null);
            }//end of else

            cStmtObject.setString(3,(String)alSearchCriteria.get(2));
            cStmtObject.setString(4,(String)alSearchCriteria.get(3));
            cStmtObject.setString(5,(String)alSearchCriteria.get(4));
            cStmtObject.setString(6,(String)alSearchCriteria.get(5));
            cStmtObject.setString(7,(String)alSearchCriteria.get(6));
            cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(8);
            if(rs != null){
            	while(rs.next()){
            		logVO = new LogVO();

            		if(rs.getString("PAT_LOG_SEQ_ID") != null){
            			logVO.setLogId(new Long(rs.getLong("PAT_LOG_SEQ_ID")));
            			  
            		}//end of if(rs.getString("PAT_LOG_SEQ_ID") != null)

            		if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null){
            			logVO.setSeqID(new Long(rs.getLong("PAT_GEN_DETAIL_SEQ_ID")));
            			  
            		}//end of if(rs.getString("PAT_GEN_DETAIL_SEQ_ID") != null)

            		logVO.setUserName(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));  
            		logVO.setLogDesc(TTKCommon.checkNull(rs.getString("REMARKS")));  

            		if (rs.getString("ADDED_DATE") != null){
                        logVO.setLogDate(new java.util.Date(rs.getTimestamp("ADDED_DATE").getTime()));  
                    }//end of if (rs.getString("ADDED_DATE") != null)

            		if(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")).equalsIgnoreCase("Y")){
                        logVO.setImageName("SystemIcon");
                        logVO.setImageTitle("System Log");
                    }//end of if(TTKCommon.checkNull(rs.getString("SYSTEM_GEN_YN")).equalsIgnoreCase("Y"))
            		
            		logVO.setLogTypeDesc(TTKCommon.checkNull(rs.getString("LOG_TYPE")));
            		  

            		alResultList.add(logVO);
            	}//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
					log.error("Error while closing the Resultset in LogDAOImpl getPreAuthLogList()",sqlExp);
					throw new TTKException(sqlExp, "log");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in LogDAOImpl getPreAuthLogList()",sqlExp);
						throw new TTKException(sqlExp, "log");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in LogDAOImpl getPreAuthLogList()",sqlExp);
							throw new TTKException(sqlExp, "log");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "log");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getPreAuthLogList(ArrayList alSearchCriteria)

    /**
     * This method adds the log details   
     * The method also calls another method on DAO to insert the Pre-Authorization Log details to the database 
     * @param logVO LogVO object which contains the Pre-Authorization Log details to be added
     * @param strIdentifier object which contains Identifier - Coding/Pre-Authorization/Claims
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int addPreAuthLog(LogVO logVO,String strIdentifier) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        int iResult =0;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strAddPreAuthLog);

            if (logVO.getLogId()!= null){
            	cStmtObject.setLong(1,logVO.getLogId());
            }//end of if (logVO.getLogId()!= null)
            else{
            	cStmtObject.setLong(1,0);
            }//end of else

            if(logVO.getClaimSeqID() != null){
            	cStmtObject.setLong(2,logVO.getClaimSeqID());
            }//end of if(logVO.getClaimSeqID() != null)
            else{
            	cStmtObject.setString(2,null);
            }//end of else

            if(logVO.getSeqID() != null){
            	cStmtObject.setLong(3,logVO.getSeqID());
            }//end of if(logVO.getSeqID() != null)
            else{
            	cStmtObject.setString(3,null);
            }//end of else
            
            /*if(strIdentifier.equalsIgnoreCase("Coding")){
            	cStmtObject.setString(4,"COL");
            }//end of if(strIdentifier.equalsIgnoreCase("Coding"))
            else{
            	cStmtObject.setString(4,"NAR");
            }//end of else
*/          
            cStmtObject.setString(4,logVO.getLogType());
            cStmtObject.setString(5,"N");
            cStmtObject.setTimestamp(6,new Timestamp(TTKCommon.getDate().getTime()));
            cStmtObject.setString(7,logVO.getLogDesc());
            cStmtObject.setLong(8,logVO.getUpdatedBy());
            cStmtObject.setString(9,"EXT");
            cStmtObject.registerOutParameter(10,Types.INTEGER);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(10);
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
        			log.error("Error while closing the Statement in LogDAOImpl addPreAuthLog()",sqlExp);
        			throw new TTKException(sqlExp, "log");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LogDAOImpl addPreAuthLog()",sqlExp);
        				throw new TTKException(sqlExp, "log");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "log");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }//end of addPreAuthLog(LogVO logVO,String strIdentifier)

	public ArrayList getClaimPreAuthLogList(ArrayList<Object> alSearchCriteria) throws TTKException{
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        LogVO logVO = null;
        
        System.out.println("alSearchCriteria===>"+alSearchCriteria);
        
        try{
        	conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strClaimPreauthLogList);
              
              
              
            cStmtObject.setString(1,(String)alSearchCriteria.get(0));//flag
            cStmtObject.setLong(2,(Long)alSearchCriteria.get(1));//seq id
            cStmtObject.setString(3,(String)alSearchCriteria.get(2));//log type
            cStmtObject.setString(4,(String)alSearchCriteria.get(3));//start date
            cStmtObject.setString(5,(String)alSearchCriteria.get(4));//end date
            cStmtObject.registerOutParameter(6,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(6);
            if(rs != null){
            	while(rs.next()){
            		logVO = new LogVO();
            		
            		logVO.setLogDate(new java.util.Date(rs.getTimestamp("ADDED_DATE").getTime())); 
            		logVO.setLogTypeDesc(TTKCommon.checkNull(rs.getString("description")));
            		logVO.setLogDesc(TTKCommon.checkNull(rs.getString("REMARKS")));  
            		logVO.setUserName(TTKCommon.checkNull(rs.getString("CONTACT_NAME")));  
            		if(TTKCommon.checkNull(rs.getString("system_yn")).equalsIgnoreCase("Y")){
                        logVO.setImageName("SystemIcon");
                        logVO.setImageTitle("System Log");
                    }
            		
            		
            		  

            		alResultList.add(logVO);
            	}//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
					log.error("Error while closing the Resultset in LogDAOImpl getPreAuthLogList()",sqlExp);
					throw new TTKException(sqlExp, "log");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in LogDAOImpl getPreAuthLogList()",sqlExp);
						throw new TTKException(sqlExp, "log");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in LogDAOImpl getPreAuthLogList()",sqlExp);
							throw new TTKException(sqlExp, "log");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "log");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }

	public int addClaimPreAuthLog(LogVO logVO, String strIdentifier) throws TTKException{
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
        int iResult =0;
        try{
            conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement) conn.prepareCall(strAddClaimPreAuthLog);
            
            
            cStmtObject.setString(1,logVO.getFlag());
            cStmtObject.setLong(2,logVO.getClaimSeqID());
            //cStmtObject.setString(3,logVO.getLogType());
            cStmtObject.setString(3,logVO.getLogDesc());
            cStmtObject.setLong(4,logVO.getUpdatedBy());
            
            cStmtObject.registerOutParameter(5,Types.INTEGER);
            cStmtObject.execute();
            iResult = cStmtObject.getInt(5);
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
        			log.error("Error while closing the Statement in LogDAOImpl addClaimPreAuthLog()",sqlExp);
        			throw new TTKException(sqlExp, "log");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in LogDAOImpl addClaimPreAuthLog()",sqlExp);
        				throw new TTKException(sqlExp, "log");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "log");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects 
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
        return iResult;
    }

	public ArrayList getPolicyTOBLogList(ArrayList<Object> alSearchCriteria)throws TTKException {
    	Collection<Object> alResultList = new ArrayList<Object>();
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        LogVO logVO = null;
        
        try{
        	conn = ResourceManager.getConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strTOBAlertLogList);
           
            cStmtObject.setLong(1,(Long)alSearchCriteria.get(0));//seq id
            cStmtObject.setString(2,(String)alSearchCriteria.get(1));//flag  log type
            cStmtObject.setString(3,(String)alSearchCriteria.get(2));//start date
            cStmtObject.setString(4,(String)alSearchCriteria.get(3));//end date
            cStmtObject.setString(5,(String)alSearchCriteria.get(4));//v_sort_var      
            cStmtObject.setString(6,(String)alSearchCriteria.get(5));//v_sort_order    
            cStmtObject.setString(7,(String)alSearchCriteria.get(6));//v_start_num     
            cStmtObject.setString(8,(String)alSearchCriteria.get(7));//v_end_num       
            cStmtObject.registerOutParameter(9,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(9);
            if(rs != null){
            	while(rs.next()){
            		logVO = new LogVO();
            		//logVO.setLogDate(new java.util.Date(rs.getTimestamp("log_date").getTime())); 
            		String date=TTKCommon.checkNull(rs.getString("log_date"));
            		if(date!=null){
            			 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(date);  
                		 logVO.setLogDate(date1); 
            		}
            		logVO.setLogTypeDesc("TOB Details");
            		logVO.setLogDesc(TTKCommon.checkNull(rs.getString("REMARKS")));  
            		logVO.setUserName(TTKCommon.checkNull(rs.getString("user_name")));  
            		//if(TTKCommon.checkNull(rs.getString("system_yn")).equalsIgnoreCase("Y")){
                        logVO.setImageName("SystemIcon");
                        logVO.setImageTitle("System Log");
                  //  }
            		alResultList.add(logVO);
            	}//end of while(rs.next())
            }//end of if(rs != null)
            return (ArrayList)alResultList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "log");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "log");
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
					log.error("Error while closing the Resultset in LogDAOImpl getPolicyTOBLogList()",sqlExp);
					throw new TTKException(sqlExp, "log");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in LogDAOImpl getPolicyTOBLogList()",sqlExp);
						throw new TTKException(sqlExp, "log");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in LogDAOImpl getPolicyTOBLogList()",sqlExp);
							throw new TTKException(sqlExp, "log");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "log");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }
    
    
   
   
}//end of LogDAOImpl
