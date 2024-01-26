/**
  * @ (#) MisReportDAOImpl.java May 29, 2007
  * Project      : TTK HealthCare Services
  * File         : MisReportDAOImpl.java
  * Author       : Ajay Kumar
  * Company      : WebEdge Technologies Pvt.Ltd.
  * Date Created :
  *
  * @author       :  Ajay Kumar
  * Modified by   :
  * Modified date :
  * Reason        :
  */

package com.ttk.dao.impl.misreports;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.BaseDAO;
import com.ttk.dao.ReportResourceManager;
import com.ttk.dao.ResourceManager;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.enrollment.PolicyGroupVO;
import com.ttk.dto.misreports.ReportDetailVO;

public class MisReportDAOImpl implements BaseDAO, Serializable {

	private static Logger log = Logger.getLogger(MisReportDAOImpl.class );

	//private static String strInCLAIMS_REGISTER_REPORTS="MIS_REPORTS.SELECT_CLAIM_STATUS";
    private static String strCLAIMS_STANDARD_REPORTS="MIS_REPORTS.CALIMS_STANDARD_REPORT";
    private static String strHOSPITAL_MONITOR_REPORTS="MIS_REPORTS.HOSPITAL_MONITOR";
    private static String strCorporate_MONITOR_REPORTS="MIS_REPORTS.CORPORATE_MONITOR";
    private static String strECARD_IMAGE_VIEW_REPORTS="REPORTS_MTEST_PKG.ECARD_IMAGE_VIEW";
    private static String strClaims_OUTSTANDING_LIABILITY = "PRE_CLM_REPORTS_PKG.CLM_DETAIL_RPT";
    private static String strPreAuthorization_REPORTS="MIS_REPORTS.PAT_INFO";
    private static String strENROLMENT_DETAILS_REPORTS="MIS_REPORTS.ENROLMENT_DETAILS";
	private static String strINVESTIGAT_DETAILS_REPORTS="MIS_REPORTS.PROC_INVESTIGATE_CLAIMS"; //koc11 koc 11
    //private static String strINVESTIGAT_DETAILS_REPORTS="MIS_REPORTS.PROC_INVESTIGATE_CLAIMS";
    private static String strCODING_REPORTS="MIS_V2_REPORTS_PKG.CODING_38_COLUMN_REPORT";
    private static String strCLAIM_INTIMATION_REPORT="PRE_CLM_REPORTS_PKG.CLAIM_INTIMATION_REPORT";
    private static String strCLAIM_REJECTION_REPORT="PRE_CLM_REPORTS_PKG.CLAIM_REJECTION_REPORT";
    private static String strCLAIM_SETTLED_REPORT="PRE_CLM_REPORTS_PKG.CLAIM_SETTLED_REPORT";
    private static String strCALLPENDING_REPORT="MIS_V2_REPORTS_PKG.GET_CALL_CENTRE_STATUS";
	private static String strREP_REPORT_LOG="{CALL MIS_REPORTS.REPORT_LOGS_DETAIL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//koc 11 koc11
    //private static String strREP_REPORT_LOG="{CALL MIS_REPORTS.REPORT_LOGS_DETAIL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static String strDOBO_CLAIMS_DETAIL="MIS_V2_FIN_REPORTS_PKG.BO_DO_CLAIMS_DETAILS_REPORT";
    private static String strFG_PENDING_REPORT="MIS_REPORTS.PAYMENT_PENDING_REPORT";
    private static String strCITI_CLAIMS_DETAILS_REPORT="MIS_V2_FIN_REPORTS_PKG.CITI_CLAIMS_DETAILS_REPORT";
    private static String strUNIVERSAL_SOMPO_PENDING_REPORT="MIS_V2_FIN_REPORTS_PKG.UNIVERSAL_SOMPO_PENDING_REPORT";
    private static String strACC_SUMMARY_REPORT ="MIS_V2_REPORTS_PKG.SELECT_POLICY_PLAN_SUMMARY";
    private static String strGetReportPolicyList = "{CALL MIS_V2_REPORTS_PKG.GET_COR_POLICY_REP_LIST(?,?)}";
   // private static String strGetReportFromTo = "{CALL MIS_V2_REPORTS_PKG.GET_REPORT_FROM_TO(?,?)}";
	private static String strGroupList = "{CALL MIS_V2_REPORTS_PKG.SELECT_GROUP_LIST(?,?,?,?,?,?,?,?)}";
	private static String strPREAUTH_GRP_REPORT ="MIS_V2_REPORTS_PKG.SELECT_PREAUTH_DETAIL";
	private static String strPreAuthSMSReport="MIS_V2_REPORTS_PKG.SELECT_PREAUTH_SMS_REPORT";

    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReports(String strReportID,String strParameter) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs = null;
        String strProcedureName="";
        strProcedureName =getProcedureName(strReportID);
        if(strProcedureName==null)
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        OracleCachedRowSet crs = null;
        try{
            String strCall = "{CALL "+strProcedureName.trim()+"(?,?)}";
              
            crs = new OracleCachedRowSet();
            conn = ReportResourceManager.getReportConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
            cStmtObject.setString(1,strParameter);
            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet) cStmtObject.getObject(2);
            if(rs !=null)
            {
             	crs.populate(rs);
            }//end of if(rs !=null)
            return crs;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
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
        			log.error("Error while closing the Statement in MisReportDAOImpl getReport()",sqlExp);
        			throw new TTKException(sqlExp, "tTkReports");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MisReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    }//end of getReport(String strProcedureName,HashMap hMap)

    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet[] getReports(String strReportID,String strParameter,String strNoOfCursors) throws TTKException {
    	Connection conn = null;
    	CallableStatement cStmtObject=null;
    	ResultSet rs[] = null;
        String strProcedureName="";
        int intNoOfCursors = Integer.parseInt(strNoOfCursors);
        strProcedureName =getProcedureName(strReportID);
        if(strProcedureName==null)
        {
        	throw new TTKException(new Exception("No procedure is found for this report !"), "tTkReports");
        }
        OracleCachedRowSet crs[] = null;
        try{
        	String strCall = "{CALL "+strProcedureName.trim();
			String strParameters = "(?";
			for(int i=0;i<intNoOfCursors;i++){
				strParameters += ",?";
			}//end of for(int i=0;i<intNoOfCursors;i++)
			strParameters += ")}";
			strCall += strParameters;
			crs = new OracleCachedRowSet[intNoOfCursors];
			rs = new ResultSet[intNoOfCursors];
			conn = ReportResourceManager.getReportConnection();//getTestConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strCall);
			cStmtObject.setString(1,strParameter);
			for(int i=2;i<intNoOfCursors+2;i++){
				cStmtObject.registerOutParameter(i,OracleTypes.CURSOR);
			}//end of for(int i=2;i<intNoOfCursors+2;i++)
			cStmtObject.execute();
			for(int index=0;index<intNoOfCursors;index++) {
				rs[index]= (java.sql.ResultSet) cStmtObject.getObject(index+2);
				crs[index] = new OracleCachedRowSet();
				if(rs[index]!=null){
					crs[index].populate(rs[index]);
				}//end of if(rs[index]!=null)
			}//end of for(int index=0;index<intNoOfCursors;index++)
			return crs;
		
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
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
        			log.error("Error while closing the Statement in MisReportDAOImpl getReport()",sqlExp);
        			throw new TTKException(sqlExp, "tTkReports");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MisReportDAOImpl getReport()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
    }
    /**
	 * @param strReportID
	 * @return
	 */
	private String getProcedureName(String strReportID) {
		if(strReportID.equals("CSD"))
			return(strCLAIMS_STANDARD_REPORTS);
		else if(strReportID.equals("HMR"))
			return(strHOSPITAL_MONITOR_REPORTS);
		else if(strReportID.equals("HMRCI"))
			return(strHOSPITAL_MONITOR_REPORTS);
		else if(strReportID.equals("HMRPRI"))
			return(strHOSPITAL_MONITOR_REPORTS);
		else if(strReportID.equals("CMREI"))
			return(strCorporate_MONITOR_REPORTS);
		else if(strReportID.equals("CMRCI"))
			return(strCorporate_MONITOR_REPORTS);
		else if(strReportID.equals("CMRPRI"))
			return(strCorporate_MONITOR_REPORTS);
		else if(strReportID.equals("CMRPOI"))
			return(strCorporate_MONITOR_REPORTS);
		else if(strReportID.equals("PAPRI"))
				return(strPreAuthorization_REPORTS);
	   else if(strReportID.equals("EID"))
			return(strECARD_IMAGE_VIEW_REPORTS);
		else if(strReportID.equals("ClaimsOutstandingLiability"))
			return(strClaims_OUTSTANDING_LIABILITY);
		else if(strReportID.equals("EMPR"))
			return(strENROLMENT_DETAILS_REPORTS);
		else if(strReportID.equals("EMMR"))
			return(strENROLMENT_DETAILS_REPORTS);
		else if(strReportID.equals("IMCIR"))
			return(strINVESTIGAT_DETAILS_REPORTS);
		else if(strReportID.equals("IMPAIR"))
			return(strINVESTIGAT_DETAILS_REPORTS);
		else if(strReportID.equals("CODRPT"))
			return(strCODING_REPORTS);
		else  if(strReportID.equals("ClaimsIntimation"))
			return(strCLAIM_INTIMATION_REPORT);
		else  if(strReportID.equals("ClaimsRejection"))
			return(strCLAIM_REJECTION_REPORT);
		else  if(strReportID.equals("ClaimsSettlement"))
			return(strCLAIM_SETTLED_REPORT);
		else if(strReportID.equals("CALLPENDINGRPT"))
			return(strCALLPENDING_REPORT);
		else if(strReportID.equals("DoBOClaimsDetail"))
			return(strDOBO_CLAIMS_DETAIL);
		else if(strReportID.equals("FGPR"))
			return(strFG_PENDING_REPORT);
		else if(strReportID.equals("CitiFinDetRpt"))
		{
			return strCITI_CLAIMS_DETAILS_REPORT;
		}//end of else if(strReportID.equals("CitiFinDetRpt"))
		else if(strReportID.equals("UniSampoPenRpt"))
		{
			return strUNIVERSAL_SOMPO_PENDING_REPORT;
		}//end of else if(strReportID.equals("UniSampoPenRpt"))
		else if(strReportID.equals("AccentureReport"))
		{
			return strACC_SUMMARY_REPORT;
		}//end of else if(strReportID.equals("AccentureReport"))
		else if(strReportID.equals("GrpPreauthReport"))
		{
			return strPREAUTH_GRP_REPORT;
		}//end of else if(strReportID.equals("GrpPreauthReport"))
		else if(strReportID.equals("PreAuthSMSReport"))
		{
			return strPreAuthSMSReport;
		}//end of else if(strReportID.equals("PreAuthSMSReport"))
		else
		{
			return null;
		}//end of else
	}//end of getProcedureName(String strReportID)

	/**
     * This method saves the Report Details information
     * @param reportDetailVO the object which contains the Report Details which has to be  saved
     * @return int the value which returns greater than zero for succcesssful execution of the task
     * @exception throws TTKException
     */
	public int saveReportDetails(ReportDetailVO reportDetailVO)throws TTKException {
		int iResult = 0;
		Connection conn=null;
		CallableStatement cStmtObject=null;
		try {
			conn=ReportResourceManager.getReportConnection();
			cStmtObject=(java.sql.CallableStatement)conn.prepareCall(strREP_REPORT_LOG);
			if(reportDetailVO.getReportSeqId()!= null)
			{
            	cStmtObject.setLong(1,reportDetailVO.getReportSeqId());//report_seq_id
			}// end of if(reportDetailVO.getReportSeqId()!= null)
           	else
           	{
            	cStmtObject.setLong(1,0);//report_seq_id
           	}//end of else
			cStmtObject.setString(2,reportDetailVO.getReportId());
			cStmtObject.setString(3,reportDetailVO.getReportLink());
			cStmtObject.setString(4,reportDetailVO.getReportName());
			cStmtObject.setString(5,reportDetailVO.getReportType());
			if(reportDetailVO.getTTKBranchName() == "")
			{
			cStmtObject.setString(6,"Null");
			}//end of if(reportDetailVO.getTTKBranchName() == "")
			else
			{
			cStmtObject.setString(6,reportDetailVO.getTTKBranchName());
			}//end of else
			if(reportDetailVO.getInsCompanyName() == "")
			{
			cStmtObject.setString(7,"Null");
			}//end of if(reportDetailVO.getInsCompanyName() == "")
			else
			{
			cStmtObject.setString(7,reportDetailVO.getInsCompanyName());
			}//end of else
			if(reportDetailVO.getInsDoBOCode() == "")
			{
			cStmtObject.setString(8,"Null");
			}//end of if(reportDetailVO.getInsDoBOCode() == "")
			else
			{
			cStmtObject.setString(8,reportDetailVO.getInsDoBOCode());
			}//end of else
			if(reportDetailVO.getSType() == "")
			{
			cStmtObject.setString(9,"Null");
			}//end of if(reportDetailVO.getSType() == "")
			else
			{
			cStmtObject.setString(9,reportDetailVO.getSType());
			}
			if(reportDetailVO.geteType() == "")
			{
			cStmtObject.setString(10,"Null");
			}// end of if(reportDetailVO.geteType() == "")
			else
			{
			cStmtObject.setString(10,reportDetailVO.geteType());
			}//end of else
			cStmtObject.setString(11,reportDetailVO.getSStatus());
			cStmtObject.setString(12,reportDetailVO.getStartDate());
			cStmtObject.setString(13,reportDetailVO.getEndDate());
			if(reportDetailVO.getGroupPolicyNo() == "")
			{
				cStmtObject.setString(14,"Null");
			}// end of if(reportDetailVO.getGroupPolicyNo() == "")
           	else
           	{
           		cStmtObject.setString(14,reportDetailVO.getGroupPolicyNo());
           	}//end of else
			if(reportDetailVO.getAgentCode()== "")
			{
				cStmtObject.setString(15,"Null");
			}// end of if(reportDetailVO.getAgentCode()== "")
           	else
           	{
           		cStmtObject.setString(15,reportDetailVO.getAgentCode());
           	}//end of else
			if(reportDetailVO.getsClaimsType() == "")
			{
			cStmtObject.setString(16,"Null");
			}// end of if(reportDetailVO.getsClaimsType() == "")
			else
			{
			cStmtObject.setString(16,reportDetailVO.getsClaimsType());
			}//end of else
			if(reportDetailVO.getsDomiciOption() == "")
			{
			cStmtObject.setString(17,"Null");
			}//end of if(reportDetailVO.getsDomiciOption() == "")
			else
			{
			cStmtObject.setString(17,reportDetailVO.getsDomiciOption());
			}//end of else
			if(reportDetailVO.gettInwardRegister() == "")
			{
			cStmtObject.setString(18,"Null");
			}//end of if(reportDetailVO.gettInwardRegister() == "")
			else
			{
			cStmtObject.setString(18,reportDetailVO.gettInwardRegister());
			}//end of else
			if(reportDetailVO.getsHospitalName() == "")
			{
			cStmtObject.setString(19,"Null");
			}//end of if(reportDetailVO.getsHospitalName() == "")
			else
			{
			cStmtObject.setString(19,reportDetailVO.getsHospitalName());
			}//end of else
			if(reportDetailVO.getInvestAgencyName() == "")
			{
			cStmtObject.setString(20,"Null");
			}//end of if(reportDetailVO.getInvestAgencyName() == "")
			else
			{
			cStmtObject.setString(20,reportDetailVO.getInvestAgencyName());
			}//end of else
			cStmtObject.setString(21,reportDetailVO.getUSER_ID());
			cStmtObject.setString(22,reportDetailVO.getUserName());
			cStmtObject.setString(23,reportDetailVO.getUserLocation());
			cStmtObject.registerOutParameter(1,Types.BIGINT);
			cStmtObject.registerOutParameter(24,Types.INTEGER);
			if(reportDetailVO.getEnrolmentId() == "")
			{
				cStmtObject.setString(25,"Null");
			}// end of if(reportDetailVO.getGroupPolicyNo() == "")
           	else
           	{
           		cStmtObject.setString(25,reportDetailVO.getEnrolmentId());
           	}//end of else
			if(reportDetailVO.getCorInsurer()== "")
			{
				cStmtObject.setString(26,"Null");
			}// end of if(reportDetailVO.getAgentCode()== "")
           	else
           	{
           		cStmtObject.setString(26,reportDetailVO.getCorInsurer());
           	}//end of else
			cStmtObject.execute();
			iResult = cStmtObject.getInt(1);//ROW_PROCESSED
		} //END OF TRY
		catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
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
        			log.error("Error while closing the Statement in MisReportDAOImpl saveReportDetails()",sqlExp);
        			throw new TTKException(sqlExp, "tTkReports");
        		}//end of catch (SQLException sqlExp)
        		finally // Even if statement is not closed, control reaches here. Try closing the connection now.
        		{
        			try
        			{
        				if(conn != null) conn.close();
        			}//end of try
        			catch (SQLException sqlExp)
        			{
        				log.error("Error while closing the Connection in MisReportDAOImpl saveReportDetails()",sqlExp);
        				throw new TTKException(sqlExp, "tTkReports");
        			}//end of catch (SQLException sqlExp)
        		}//end of finally Connection Close
        	}//end of try
        	catch (TTKException exp)
        	{
        		throw new TTKException(exp, "tTkReports");
        	}//end of catch (TTKException exp)
        	finally // Control will reach here in anycase set null to the objects
        	{
        		cStmtObject = null;
        		conn = null;
        	}//end of finally
		}//end of finally
		return iResult;
	}//end of public int saveReportDetails(ReportDetailVO reportDetailVO)
 
	/**
     * This method returns the Arraylist of Cache object which contains Policy details for corresponding Group
     * @param lngGrpRegSeqID long value which contains Group Reg Seq ID
     * @return ArrayList of Cache object which contains Policy details for corresponding Group
     * @exception throws TTKException
     */
    public ArrayList getReportPolicyList(long lngGrpRegSeqID) throws TTKException {
    	Connection conn = null;
        CallableStatement cStmtObject=null;
        ResultSet rs = null;
        CacheObject cacheObject = null;
        ArrayList<Object> alReportPolicyList = new ArrayList<Object>();
        try{
            conn = ReportResourceManager.getReportConnection();
            cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGetReportPolicyList);
            cStmtObject.setLong(1,lngGrpRegSeqID);
            cStmtObject.registerOutParameter(2,OracleTypes.CURSOR);
            cStmtObject.execute();
            rs = (java.sql.ResultSet)cStmtObject.getObject(2);
            if(rs != null){
                while(rs.next()){
                    cacheObject = new CacheObject();
                    cacheObject.setCacheId(TTKCommon.checkNull(rs.getString("POLICY_SEQ_ID")));
                    cacheObject.setCacheDesc(TTKCommon.checkNull(rs.getString("POLICY_NUMBER")));
                    alReportPolicyList.add(cacheObject);
                }//end of while(rs.next())
            }//end of if(rs != null)
            return alReportPolicyList;
        }//end of try
        catch (SQLException sqlExp)
        {
            throw new TTKException(sqlExp, "tTkReports");
        }//end of catch (SQLException sqlExp)
        catch (Exception exp)
        {
            throw new TTKException(exp, "tTkReports");
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
					log.error("Error while closing the Resultset in MisReportDAOImpl getReportPolicyList()",sqlExp);
					throw new TTKException(sqlExp, "webconfig");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MisReportDAOImpl getReportPolicyList()",sqlExp);
						throw new TTKException(sqlExp, "tTkReports");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MisReportDAOImpl getReportPolicyList()",sqlExp);
							throw new TTKException(sqlExp, "tTkReports");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tTkReports");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
    }//end of getReportPolicyList(long lngGrpRegSeqID)
    
    /**
	 * This method returns the Arraylist of PolicyGroupVO's  which contains Policy Corporate Group details
	 * @param alSearchCriteria ArrayList object which contains the search criteria
	 * @return ArrayList of PolicyGroupVO object which contains Policy Corporate Group details
	 * @exception throws TTKException
	 */
	public ArrayList getGroupList(ArrayList alSearchCriteria) throws TTKException {
		Collection<Object> alResultList = new ArrayList<Object>();
		Connection conn = null;
		CallableStatement cStmtObject=null;
		ResultSet rs = null;
		PolicyGroupVO policyGroupVO = null;
		try {
			conn = ReportResourceManager.getReportConnection();
			cStmtObject = (java.sql.CallableStatement)conn.prepareCall(strGroupList);
			cStmtObject.setString(1,(String)alSearchCriteria.get(0));
			cStmtObject.setString(2,(String)alSearchCriteria.get(1));
			cStmtObject.setString(3,(String)alSearchCriteria.get(2));
			cStmtObject.setString(4,(String)alSearchCriteria.get(3));
			cStmtObject.setString(5,(String)alSearchCriteria.get(4));
			cStmtObject.setString(6,(String)alSearchCriteria.get(5));
			cStmtObject.setString(7 ,(String)alSearchCriteria.get(6));
			cStmtObject.registerOutParameter(8,OracleTypes.CURSOR);
			cStmtObject.execute();
			rs = (java.sql.ResultSet)cStmtObject.getObject(8);
			if(rs != null){
				while (rs.next()) {
					policyGroupVO = new PolicyGroupVO();
					if(rs.getString("GROUP_REG_SEQ_ID") != null){
						policyGroupVO.setGroupRegnSeqID(new Long(rs.getLong("GROUP_REG_SEQ_ID")));
					}//end of if(rs.getString("GROUP_REG_SEQ_ID") != null)
					policyGroupVO.setGroupID(TTKCommon.checkNull(rs.getString("GROUP_ID")));
					policyGroupVO.setGroupName(TTKCommon.checkNull(rs.getString("GROUP_NAME")));
					policyGroupVO.setBranchName(TTKCommon.checkNull(rs.getString("OFFICE_NAME")));
					alResultList.add(policyGroupVO);
				}//end of while(rs.next())
			}//end of if(rs != null)
			return (ArrayList)alResultList;
		}//end of try
		catch (SQLException sqlExp)
		{
			throw new TTKException(sqlExp, "tTkReports");
		}//end of catch (SQLException sqlExp)
		catch (Exception exp)
		{
			throw new TTKException(exp, "tTkReports");
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
					log.error("Error while closing the Resultset in MisReportDAOImpl getGroupList()",sqlExp);
					throw new TTKException(sqlExp, "tTkReports");
				}//end of catch (SQLException sqlExp)
				finally // Even if result set is not closed, control reaches here. Try closing the statement now.
				{
					try
					{
						if (cStmtObject != null) cStmtObject.close();
					}//end of try
					catch (SQLException sqlExp)
					{
						log.error("Error while closing the Statement in MisReportDAOImpl getGroupList()",sqlExp);
						throw new TTKException(sqlExp, "tTkReports");
					}//end of catch (SQLException sqlExp)
					finally // Even if statement is not closed, control reaches here. Try closing the connection now.
					{
						try
						{
							if(conn != null) conn.close();
						}//end of try
						catch (SQLException sqlExp)
						{
							log.error("Error while closing the Connection in MisReportDAOImpl getGroupList()",sqlExp);
							throw new TTKException(sqlExp, "tTkReports");
						}//end of catch (SQLException sqlExp)
					}//end of finally Connection Close
				}//end of finally Statement Close
			}//end of try
			catch (TTKException exp)
			{
				throw new TTKException(exp, "tTkReports");
			}//end of catch (TTKException exp)
			finally // Control will reach here in anycase set null to the objects 
			{
				rs = null;
				cStmtObject = null;
				conn = null;
			}//end of finally
		}//end of finally
	}//end of getGroupList(ArrayList alSearchCriteria)
	
	
}//end of MisReportDAOImpl
