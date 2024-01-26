/**
 * @ (#) TTKReportManagerBean.java June 28, 2006
 * Project       : TTK HealthCare Services
 * File          : TTKReportManagerBean.java
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

import javax.ejb.Stateless;
import javax.ejb.TransactionManagementType;

import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDAOImpl;

@Stateless
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)

public class TTKReportManagerBean implements TTKReportManager{

    TTKReportDAOImpl tTKReportDAOImpl = new TTKReportDAOImpl();
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param strParameter String which contains the concatinated string and which is pass to the procedure
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strProcedureName,String strParameter) throws TTKException {
        return tTKReportDAOImpl.getReport(strProcedureName,strParameter);
    }//end of getReport(String strProcedureName,String strParameter)
    
	//Added for IBM....9
    //added by Praveen only for IBM Reports
	public ResultSet getReport(String strProcedureName,String strParameter1,String strParameter2,String strParameter3) throws TTKException {
		        return tTKReportDAOImpl.getReport(strProcedureName,strParameter1,strParameter2,strParameter3);
	}//end of getReport(String strProcedureName,String strParameter)
	 
    //Added for IBM....9
    //added by Praveen only for IBM Reports
	public ResultSet getReport(String strProcedureName,String strParameter1,String strParameter2,String strParameter3,String strParameter4) throws TTKException {
		        return tTKReportDAOImpl.getReport(strProcedureName,strParameter1,strParameter2,strParameter3,strParameter4);
	}//end of getReport(String strProcedureName,String strParameter)


    //Ended..
	/* This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param strParameter String which contains the concatinated string and which is pass to the procedure
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
	 //KOC 1179.......................
    public ResultSet getReport(String strProcedureName,String strParameter,StringBuffer shrtfallType) throws TTKException {
        return tTKReportDAOImpl.getReport(strProcedureName,strParameter,shrtfallType);
    }//end of getReport(String strProcedureName,String strParameter)
    	
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getEcardReport(String strReportID,String strParameter) throws TTKException {
    	return tTKReportDAOImpl.getEcardReport(strReportID,strParameter);
    }//end of getEcardReport(String strReportID,String strParameter)
    
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param hMap HashMap which contains the parameter for the procedure.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getIEcardReport(String strReportID,String strParameter,long strMemParameter) throws TTKException {
    	return tTKReportDAOImpl.getIEcardReport(strReportID,strParameter,strMemParameter);
    }//end of getEcardReport(String strReportID,String strParameter)
    
    /**
     * This method returns the List of ResultSet objects, which contains Reports data which are populated from the database
     * @param strReportID String which contains the Report ID to identify the procedure
     * @param strParameter String which contains the concatinated string and which is pass to the procedure 
     * @param intIndexCursor int which contains the cursor index
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strProcedureName,String strParameter,int intIndexCursor) throws TTKException {
        return tTKReportDAOImpl.getReport(strProcedureName,strParameter,intIndexCursor);
    }//end of getReport(String strProcedureName,String strParameter,int intIndexCursor)
    
    /**
     * This method returns the List of ResultSet objects, which contains Reports data which are populated from the database
     * @param strProcedureName String which procedure name which has to be called to get data.
     * @param strParameter String which contains the concatinated string and which is pass to the procedure
     * @return ResultSet[] List of ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet[] getReport(String strProcedureName,String strParameter,String strNoOfCursors) throws TTKException {
        return tTKReportDAOImpl.getReport(strProcedureName,strParameter,strNoOfCursors);
    }//end of getReport(String strProcedureName,String strParameter,String strNoOfCursors)
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param alFaxStatusList ArrayList which procedure name which has to be called to get data.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(ArrayList alFaxStatusList) throws TTKException {
        return tTKReportDAOImpl.getReport(alFaxStatusList);
    }//end of getReport(ArrayList alFaxStatusList))
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strReportID String which procedure name which has to be called to get data.
     * @return ResultSet object's which contains the details of the Reports
     * @exception throws TTKException
     */
    public ResultSet getReport(String strReportID) throws TTKException
    {
    	return tTKReportDAOImpl.getReport(strReportID);
    }//end of getReport(String strReportID)
    
    /**
     * This method returns the ResultSet, which contains Reports data which are populated from the database
     * @param strReportID String which procedure name which has to be called to get data.
     * @param alCallPendList ArrayList which contains the config param and primary mail list.
     * @exception throws TTKException
     */    
    public ResultSet getReport(String strReportID,ArrayList alCallPendList) throws TTKException
    {
    	return tTKReportDAOImpl.getReport(strReportID,alCallPendList);
    }//end of getReport(String strReportID,ArrayList alCallPendList)
    

    public ArrayList getPreAuthUtilizationReport(String parameter,String strSdate,String strEdate,String strPayer,String strProvider,String strCorps,String csStartDate,String csEndDate,String sAgentCode,String sType,String eType,String sStatus,String insCompanyCode,String sGroupPolicyNo) throws TTKException
    {
    	return tTKReportDAOImpl.getPreAuthUtilizationReport(parameter,strSdate,strEdate,strPayer,strProvider,strCorps,csStartDate,csEndDate,sAgentCode,sType,eType,sStatus,insCompanyCode,sGroupPolicyNo);
    }//end of getPreAuthUtilizationReport()

    
    public ArrayList getDetailedReport(String parameter, String repType) throws TTKException
    {
    	return tTKReportDAOImpl.getDetailedReport(parameter,repType);
    }//end of getDetailedReport(String parameter)
    
    @Override
	public ArrayList<String[]> getAuditReport(ArrayList searchData, String reportyType) throws TTKException {
		return tTKReportDAOImpl.getAuditReport(searchData,reportyType);
	}

	@Override
	public ArrayList<String[]> getFinanceReport(String repType, String parameter) throws TTKException {
		
		return tTKReportDAOImpl.getFinanceReport(repType,parameter);
	}

	@Override
	public ArrayList<String[]> getChequeInformationReport(String repType, String parameter) throws TTKException {
		
		return tTKReportDAOImpl.getChequeInformationReport(repType,parameter);
	}
	
	@Override
	public ArrayList uploadClaimAuditReport(ArrayList inputData) throws TTKException {
		
		return tTKReportDAOImpl.uploadClaimAuditReport(inputData);
	}

	@Override
	public ArrayList getAuditUploadTemplateErrorLog(ArrayList inputData) throws TTKException {
		return tTKReportDAOImpl.getAuditUploadTemplateErrorLog(inputData);
	}
	
	 public ResultSet getPolicyReport(String strProcedureName,String strParameter, Long policySeqId ,String memberStatus) throws TTKException {
	        return tTKReportDAOImpl.getPolicyReport(strProcedureName,strParameter,policySeqId,memberStatus);
	    }
	  
		@Override
		public ResultSet[] getTATReport(String strReportID, ArrayList arrayList,String flag) throws TTKException {
			 return tTKReportDAOImpl.getTATReport(strReportID,arrayList,flag);
		}

		@Override
		public ResultSet[] getProductivityReport(String strReportID, ArrayList arrayList,String flag) throws TTKException {
			 return tTKReportDAOImpl.getProductivityReport(strReportID,arrayList,flag);
		}
	  
		
		@Override
		public ArrayList<String[]> getFinancePreauthReport(String repType,
			List inputList) throws TTKException {
		
		return tTKReportDAOImpl.getFinancePreauthReport(repType,inputList);
		}
 
		@Override
		public ArrayList uploadCFDReport(ArrayList inputData) throws TTKException {	
			return tTKReportDAOImpl.uploadCFDReport(inputData);
		}
	 
		@Override
		public ArrayList getCFDUploadTemplateErrorLog(ArrayList inputData) throws TTKException {
			return tTKReportDAOImpl.getCFDUploadTemplateErrorLog(inputData);
		}
		
	    @Override
		public ResultSet[] getCFDReport(ArrayList searchData, String reportyType) throws TTKException {
			return tTKReportDAOImpl.getCFDReport(searchData,reportyType);
		}
		
		public ArrayList<Object> getPolicyNoList(String groupId) throws TTKException
		{
				return tTKReportDAOImpl.getPolicyNoList(groupId);
		}
		
       @Override
	public ResultSet[] getBordereauReport(String strReportID, ArrayList arrayList) throws TTKException {
		 return tTKReportDAOImpl.getBordereauReport(strReportID,arrayList);
	}
       
       @Override
		public ResultSet[] getCampaginReport(ArrayList searchData, String reportyType) throws TTKException {
			return tTKReportDAOImpl.getCampaginReport(searchData,reportyType);
		}  
		public String getPaymentTermAgr(String hospitalSeqId) throws TTKException
		{
				return tTKReportDAOImpl.getPaymentTermAgr(hospitalSeqId);
		}
		
		public ArrayList getActivityLogList(String hospitalSeqId) throws TTKException
		{
				return tTKReportDAOImpl.getActivityLogList(hospitalSeqId);
		}
}//end of TTKReportManagerBean