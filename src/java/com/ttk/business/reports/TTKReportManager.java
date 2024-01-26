/**
 * @ (#) TTKReportManager.java June 28, 2006
 * Project       : TTK HealthCare Services
 * File          : TTKReportManager.java
 * Author        : Krishna K H
 * Company       : Span Systems Corporation
 * Date Created  : June 28, 2006
 *
 * @author       : Krishna K H
 * Modified by   :
 * Modified date :
 * Reason        :
 */
package com.ttk.business.reports;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import javax.ejb.Local;

import com.ttk.common.exception.TTKException;

@Local
public interface TTKReportManager {

	 /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param strParameter String which contains the concatinated string and which is pass to the procedure
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strProcedureName,String strParameter) throws TTKException;
    //Added for IBM......8
    //added by Praveen only for IBMReports
	   public ResultSet getReport(String strProcedureName,String strParameter1,String strParameter2,String strParameter3) throws TTKException;
	   // added
   //Ended

	//Added for IBM......8
    //added by Praveen only for IBMReports
	   public ResultSet getReport(String strProcedureName,String strParameter1,String strParameter2,String strParameter3,String strParameter4) throws TTKException;
	   // added
   //Ended 
	   /**
	     * This method returns the ResultSet, which contains Reports data which are populated from the database
	     * @param strProcedureName String which procedure name which has to be called to get data.
	     * @param hMap HashMap which contains the parameter for the procedure.
	     * @return ResultSet object's which contains the details of the Reports
	     * @exception throws TTKException
	     */   
	 //Added as per KOC  KOC1179
	    public ResultSet getReport(String strProcedureName,String strParameter,StringBuffer shortfallType) throws TTKException;

		
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getEcardReport(String strReportID,String strParameter) throws TTKException;
    
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getIEcardReport(String strReportID,String strParameter,long strMemParameter) throws TTKException;
    
    /**
     * This method returns the List of ResultSet objects, which contains Reports data which are populated from the database
     * @param strReportID String which contains the Report ID to identify the procedure
     * @param strParameter String which contains the concatinated string and which is pass to the procedure 
     * @param intIndexCursor int which contains the cursor index
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strProcedureName,String strParameter,int intIndexCursor) throws TTKException;
    
    /**
     * This method returns the List of ResultSet objects, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param strParameter String which contains the concatinated string and which is pass to the procedure
     * @return ResultSet[] List of ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet[] getReport(String strProcedureName,String strParameter,String strNoOfCursors) throws TTKException;
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param alFaxStatusList ArrayList which procedure name which has to be called to get data.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(ArrayList alFaxStatusList) throws TTKException;
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strReportID String which procedure name which has to be called to get data.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strReportID) throws TTKException;
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strReportID String which procedure name which has to be called to get data.
     * @param alCallPendList ArrayList which contains the config param and primary mail list.
     * @exception throws TTKException
     */    
    public ResultSet getReport(String strReportID,ArrayList alCallPendList) throws TTKException;
    
    public ArrayList getPreAuthUtilizationReport(String parameter,String strSdate,String strEdate,String strPayer,String strProvider,String strCorps,String csStartDate,String csEndDate,String sAgentCode,String sType,String eType,String sStatus,String insCompanyCode,String sGroupPolicyNo) throws TTKException;
        
    public ArrayList getDetailedReport(String parameter,String repType) throws TTKException;
    
	public ArrayList<String[]> getFinanceReport(String string, String parameter) throws TTKException;
	
	  public ResultSet getPolicyReport(String strProcedureName,String strParameter, Long policySeqId, String memberStatus) throws TTKException;
    
    public ArrayList<String[]> getAuditReport(ArrayList searchData, String reportType) throws TTKException;
	public ArrayList uploadClaimAuditReport(ArrayList inputData) throws TTKException;
	public ArrayList getAuditUploadTemplateErrorLog(ArrayList inputData) throws TTKException;
	
	public ArrayList<String[]> getChequeInformationReport(String string, String parameter) throws TTKException;
	public ArrayList<String[]> getFinancePreauthReport(String string,List inputList) throws TTKException;

	public ArrayList uploadCFDReport(ArrayList inputData) throws TTKException;

	public ArrayList getCFDUploadTemplateErrorLog(ArrayList inputData) throws TTKException;

    public ResultSet[] getCFDReport(ArrayList searchData, String reportType) throws TTKException;

    
	public ResultSet[] getTATReport(String strReportID,ArrayList alFaxStatusList,String flag) throws TTKException;
	
	public ResultSet[] getProductivityReport(String strReportID,ArrayList alFaxStatusList,String flag) throws TTKException;
	
	 public ArrayList<Object> getPolicyNoList(String groupId) throws TTKException;	
	 
	public String getPaymentTermAgr(String hospitalSeqId) throws TTKException;	
	public ArrayList getActivityLogList(String hospitalSeqId) throws TTKException;	
	
    public ResultSet[] getBordereauReport(String strReportID, ArrayList arrayList) throws TTKException;
    
    public ResultSet[] getCampaginReport(ArrayList searchData, String reportType) throws TTKException;
}//end of TTKReportManager