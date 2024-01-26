package com.ttk.action.claims;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimBatchManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;

public class ClaimAutoRejectionAction extends TTKAction{
	
	private static Logger log = Logger.getLogger(ClaimAutoRejectionAction.class);

	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	private static final String strRownum="rownum";
	private static final String strAutoRejectionList="autoRejectionSearch";
	private static final String strAutoRejectionSearch="autoRejectionSearchError";
	  private static final String strClaimBatchSearchError="claimbatchsearch";
	    private static final String strClaimBatchDetails="ClaimsBatchDetails";
	    private static final String strClaimAutoRejectionDetail="claimAutoRejectionDetail";   
	    private static final String strConfigurationList="ConfigurationList";   
	    private static final String strBatchReport="batchReport";   	    
	    private static final String strReportdisplay="reportdisplay";    
	       
	
public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doDefault method of ClaimAutoRejectionAction");

setLinks(request);    		

HttpSession session=request.getSession();
DynaActionForm frmErrorLogList = (DynaActionForm)form;
//get the tbale data from session if exists
TableData tableData =(session.getAttribute("tableData")==null)?new TableData():(TableData)session.getAttribute("tableData");
//clear the dynaform if visiting from left links for the first time
/*if("Y".equals(request.getParameter("Entry"))){
frmErrorLogList.initialize(mapping);//reset the form data
}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
*/
tableData.createTableInfo("ClaimAutoRejectionTable",new ArrayList());
session.setAttribute("tableData",tableData);
frmErrorLogList.initialize(mapping);//reset the form data
return this.getForward(strAutoRejectionList, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strAutoRejectionSearch));
}//end of catch(Exception exp)
}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	 
	
	
	 public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
	
	log.debug("Inside the doSearch method of ClaimAutoRejectionAction");
	setLinks(request);
	HttpSession session=request.getSession();
	ClaimBatchManager claimBatchManagerObject=this.getClaimBatchManagerObject();
	//get the tbale data from session if exists
	TableData tableData =session.getAttribute("tableData")==null?new TableData():(TableData)session.getAttribute("tableData");
	
	if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
	{
		((DynaActionForm)form).initialize(mapping);//reset the form data
	}
	
	String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	
	if(!strPageID.equals("") || !strSortID.equals(""))
	{
		if(!strPageID.equals(""))
		{
			tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			return mapping.findForward(strAutoRejectionList);
		}///end of if(!strPageID.equals(""))
		else
		{
			tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
			tableData.modifySearchData("sort");//modify the search data
		}//end of else
	}//end of if(!strPageID.equals("") || !strSortID.equals(""))
	else
	{
		//create the required grid table
		tableData.createTableInfo("ClaimAutoRejectionTable",null);
		tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
        tableData.modifySearchData("search");
        tableData.getSearchData().set(tableData.getSearchData().size()-3, "DESC");
	}//end of else
	ArrayList alClaimsBatchList= claimBatchManagerObject.getClaimAutoRejectionList(tableData.getSearchData());
	tableData.setData(alClaimsBatchList, "search");
	//set the table data object to session
	session.setAttribute("tableData",tableData);
	
	return this.getForward(strAutoRejectionList, mapping, request);
}//end of try
catch(TTKException expTTK)
{
	return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
	return this.processExceptions(request, mapping, new TTKException(exp,strAutoRejectionSearch));
}//end of catch(Exception exp)
}
	
	 
	 public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doBackward method of ClaimAutoRejectionAction");
setLinks(request);
ClaimBatchManager claimBatchManagerObject=this.getClaimBatchManagerObject();
//get the tbale data from session if exists
TableData tableData =TTKCommon.getTableData(request);
tableData.modifySearchData(strBackward);//modify the search data
ArrayList alClaimsBatchList = claimBatchManagerObject.getClaimAutoRejectionList(tableData.getSearchData());
tableData.setData(alClaimsBatchList, strBackward);//set the table data
request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
return this.getForward(strAutoRejectionList, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchSearchError));
}//end of catch(Exception exp)
}
	  
	  public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doForward method of ClaimAutoRejectionAction");
setLinks(request);
ClaimBatchManager claimBenefitManagerObject=this.getClaimBatchManagerObject();
//get the tbale data from session if exists
TableData tableData =TTKCommon.getTableData(request);
tableData.modifySearchData(strForward);//modify the search data
ArrayList alClaimsBatchList = claimBenefitManagerObject.getClaimAutoRejectionList(tableData.getSearchData());
tableData.setData(alClaimsBatchList, strForward);//set the table data
request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
return this.getForward(strAutoRejectionList, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchSearchError));
}//end of catch(Exception exp)
}	  
	  

public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doView method of ClaimAutoRejectionAction");
setLinks(request);
//get the tbale data from session if exists
TableData tableData =TTKCommon.getTableData(request);
DynaActionForm frmErrorLogList=(DynaActionForm)form;
if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
{
	ClaimUploadErrorLogVO preAuthVO=(ClaimUploadErrorLogVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
request.getSession().setAttribute("xmlSeqId",preAuthVO.getXmlSeqId());
request.getSession().setAttribute("parentClaimNo",preAuthVO.getParentClaimNo());
//request.getSession().setAttribute("fastTrackFlag",preAuthVO.getFastTrackFlag());
//request.getSession().setAttribute("fastTrackMsg",preAuthVO.getFastTrackMsg());	
//this.addToWebBoard(preAuthVO, request);
}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
return mapping.findForward(strClaimAutoRejectionDetail);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchSearchError));
}//end of catch(Exception exp)
}	  
	
	


public ActionForward doConfiguration(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doConfiguration method of ClaimAutoRejectionAction");

setLinks(request);    		
HttpSession session=request.getSession();
DynaActionForm frmErrorLogList = (DynaActionForm)form;
return this.getForward(strConfigurationList, mapping, request);
}
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strAutoRejectionSearch));
}
}






public ActionForward doBatchReports(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doBatchReports method of ClaimAutoRejectionAction");

setLinks(request);    		
HttpSession session=request.getSession();
DynaActionForm frmErrorLogList = (DynaActionForm)form;
return this.getForward(strBatchReport, mapping, request);
}
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strAutoRejectionSearch));
}
}





public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doClose method of ClaimAutoRejectionAction");

HttpSession session=request.getSession();
DynaActionForm frmErrorLogList = (DynaActionForm)form;
TableData tableData =(session.getAttribute("tableData")==null)?new TableData():(TableData)session.getAttribute("tableData");
tableData.createTableInfo("ClaimAutoRejectionTable",new ArrayList());
session.setAttribute("tableData",tableData);
frmErrorLogList.initialize(mapping);//reset the form data
return this.getForward(strAutoRejectionList, mapping, request);
}
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strAutoRejectionSearch));
}
}

public ActionForward doClosebatchReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doClosebatchReport method of ClaimAutoRejectionAction");

HttpSession session=request.getSession();
DynaActionForm frmErrorLogList = (DynaActionForm)form;

frmErrorLogList.initialize(mapping);//reset the form data
return this.getForward(strConfigurationList, mapping, request);
}
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strAutoRejectionSearch));
}
}


public ActionForward doGenerateXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws TTKException{
 try{
  		log.debug("Inside the doGenerateXL method of ClaimAutoRejectionAction");
  		setLinks(request);
  		JasperReport mainJasperReport,mainJasperReportDetail,emptyReport;
  		TTKReportDataSource mainTtkReportDataSource = null;
  		TTKReportDataSource mainTtkReportDataSourceDetail = null;
  		 ArrayList<JasperPrint> list = null;
		 list = new  ArrayList<JasperPrint>();
  		
  		
  		JasperPrint mainJasperPrint = null;
  		JasperPrint mainJasperPrintDetail = null;
  		ClaimBatchManager claimBenefitManagerObject=this.getClaimBatchManagerObject();
  		String parameter=request.getParameter("parameter");
  		String reportType=request.getParameter("reportType");
  		
  		
  		DynaActionForm frmClaimDetailedReports=(DynaActionForm) form;
  		String jrxmlfile="reports/claims/claimAutoRejectionBatchReport.jrxml";
  		String jrxmlfileDetail="reports/claims/claimAutoRejectionBatchDeailRepotr.jrxml";
  		try
  		{
  			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
  			HashMap<String,String> hashMap = new HashMap<String,String>();
  			ByteArrayOutputStream boas=new ByteArrayOutputStream();		
  		//	mainTtkReportDataSource = new TTKReportDataSource("ClaimDetailedReport", parameter);
  			ServletOutputStream sos= response.getOutputStream();
  			response.setContentType("application/vnd.ms-excel");
  			
  			mainTtkReportDataSource=claimBenefitManagerObject.getAutoRejectClaimBatchReport(parameter);
  			mainTtkReportDataSourceDetail=claimBenefitManagerObject.getAutoRejectClaimBatchDetailReport(parameter);
  			
  			ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
  			ResultSet main_report_RS_Detail = mainTtkReportDataSourceDetail.getResultData();
  			
  			
  			mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
  			mainJasperReportDetail= JasperCompileManager.compileReport(jrxmlfileDetail);
  			if (main_report_RS != null && main_report_RS.next()) {
  				main_report_RS.beforeFirst();
  				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);  	
  				 list.add(mainJasperPrint);
  			} else {
  				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
  				 list.add(mainJasperPrint);
  			}
  			
  			if (main_report_RS_Detail != null && main_report_RS_Detail.next()) {
  				main_report_RS_Detail.beforeFirst();
  				mainJasperPrintDetail = JasperFillManager.fillReport(mainJasperReportDetail, hashMap, mainTtkReportDataSourceDetail);  	
  				 list.add(mainJasperPrintDetail);
  			} else {
  				mainJasperPrintDetail = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
  				 list.add(mainJasperPrintDetail);
  			}
  			
  			
	    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
	    		
	    		jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
	    		jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{"Batch Details","Over all batch report"}); 
	    		response.addHeader("Content-Disposition","attachment; filename=claimAutoRejectionBatchReport.xls");
	    		
	    		
	    		 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();	
  			//request.setAttribute("boas",boas);
  			request.setAttribute("reportType",reportType);
  			
  			//return (mapping.findForward(strReportdisplay));
  			
  			
  			sos.flush();
  			 
  	 		return null;
  		}
  		catch(Exception exp)
      	{
  			return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
      	}
  	}
  	catch(TTKException expTTK)
  	{
  		return this.processExceptions(request, mapping, expTTK);
  	}
  	catch(Exception exp)
  	{
  		return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
  	}
}






public ActionForward setProviderID(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
try{
log.debug("Inside the setProviderID method of ClaimAutoRejectionAction");

setLinks(request);    		
HttpSession session=request.getSession();
DynaActionForm frmErrorLogList = (DynaActionForm)form;
//get the tbale data from session if exists
TableData tableData =(session.getAttribute("tableData")==null)?new TableData():(TableData)session.getAttribute("tableData");


ClaimBatchManager claimBatchManagerObject=this.getClaimBatchManagerObject();
String providerseqid=(String) frmErrorLogList.get("providerName");
String providerId=claimBatchManagerObject.getProviderId(providerseqid);

tableData.createTableInfo("ClaimAutoRejectionTable",new ArrayList());
session.setAttribute("tableData",tableData);
frmErrorLogList.set("providerId", providerId);
request.getSession().setAttribute("frmAutiRejectionLogList",frmErrorLogList);


return this.getForward(strAutoRejectionList, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strAutoRejectionSearch));
}//end of catch(Exception exp)
}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	 private ArrayList<Object> populateSearchCriteria(DynaActionForm frmAutiRejectionLogList,HttpServletRequest request)
	    {
	    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	    	alSearchParams.add(frmAutiRejectionLogList.getString("iBatchRefNO"));
	    	alSearchParams.add(frmAutiRejectionLogList.getString("batchNO"));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("providerName")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("formDate")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("toDate")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("memberID")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("parentClaimNo")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("ClaimAgeBand")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("claimAction")));
	    	
	    	return alSearchParams;
	    }
	 
	 
	 
	 
	 private ClaimBatchManager getClaimBatchManagerObject() throws TTKException
		{
			ClaimBatchManager claimBatchManager = null;
			try
			{
				if(claimBatchManager == null)
				{
					InitialContext ctx = new InitialContext();
					claimBatchManager = (ClaimBatchManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimBatchManagerBean!com.ttk.business.claims.ClaimBatchManager");
				}//end if
			}//end of try
			catch(Exception exp)
			{
				throw new TTKException(exp, strClaimBatchDetails);
			}//end of catch
			return claimBatchManager;
		}	
	
}
