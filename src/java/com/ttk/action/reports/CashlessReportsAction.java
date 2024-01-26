package com.ttk.action.reports;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;

public class CashlessReportsAction extends TTKAction{
	
	 private static final Logger log = Logger.getLogger( CashlessReportsAction.class );
	
	 private static final String strReportExp="report";
	 private static final String strTATReport="TATReport";
	 private static final String strProductivityReport="ProductivityReport";
	 private static final String strPreapprovalDetailedReport="PreapprovalDetailedReport";
	 private static final String strPreapprovalActivityReport="PreapprovalActivityReport";
	  private static final String strReportdisplay="reportdisplay";
	  private static final String strPreAuthDetail = "PreAuthDetails";
	 
	 public ActionForward doTatReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doTatReport method of CashlessReportsAction");
			setLinks(request);
			/* DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().setAttribute("frmReportList",frmReportList);*/
			// String strReportParameter=strIOBRptDetail;
			 return mapping.findForward(strTATReport);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }
	 
	 
	 public ActionForward doProductivityReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doProductivityReport method of CashlessReportsAction");
			setLinks(request);
			/* DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().setAttribute("frmReportList",frmReportList);*/
			// String strReportParameter=strIOBRptDetail;
			 return mapping.findForward(strProductivityReport);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }
	 
	 
	 public ActionForward doPreapprovalDetailedReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doPreapprovalDetailedReport method of CashlessReportsAction");
			setLinks(request);
			/* DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().setAttribute("frmReportList",frmReportList);*/
			// String strReportParameter=strIOBRptDetail;
			 return mapping.findForward(strPreapprovalDetailedReport);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }
	 
	 
	 
	 
	 public ActionForward doPreapprovalActivityReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doPreapprovalActivityReport method of CashlessReportsAction");
			setLinks(request);
			/* DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().setAttribute("frmReportList",frmReportList);*/
			// String strReportParameter=strIOBRptDetail;
			 return mapping.findForward(strPreapprovalActivityReport);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }
	 
	 
	 
	 
	/* public ActionForward doGenerateTATReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
		 
		 try {
			  log.info("Inside the doGenerateTATReport method of CashlessReportsAction");
	    		setLinks(request);
	    		 DynaActionForm frmCashless =(DynaActionForm)form;
	    		 JasperReport mainJasperReport,emptyReport,mainJasperReport1,vidalJasperReport,alkootJasperReport;
				 JasperPrint mainJasperPrint,mainJasperPrint1;
				 
				 TTKReportDataSource ttkReportDataSource = null;
				 TTKReportDataSource ttkReportDataSource1 = null;
				 TTKReportDataSource ttkReportDataSource2 = null;
				 TTKReportDataSource ttkReportDataSource3 = null;
				 
				 
				 String mainJasperFile="";
				 String vidalJasperFile="";
				 String alkootJasperFile="";
				 String jrxmlfile = "";
				 String jrxmlfile1 = "";
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 ArrayList<JasperPrint> list = null;
				 list = new  ArrayList<JasperPrint>();
		    		String startDate= frmCashless.getString("startDate");
		    		String endDate= frmCashless.getString("endDate");
		    		String reportType= "EXL";
		    		String reportID= "TAT";
		    		String parameterValue= "|"+reportID+"|"+startDate+"|"+endDate+"|";
		    		String parameterValue1= "|"+startDate+"|"+endDate+"|";
				 
		    		jrxmlfile = "reports/cashless/TATReport.jrxml";
		    		
		    		ttkReportDataSource = new TTKReportDataSource("TATRpt",parameterValue);
		    		
		    		
		    		mainJasperFile = "reports/cashless/TATReport-preapprovals.jrxml";
		    		vidalJasperFile = "reports/cashless/TATReport-Vidal.jrxml";
		    		alkootJasperFile = "reports/cashless/TATReport-Alkoot.jrxml";
		    		PreAuthManager preAuthObject = null;
		    		preAuthObject = this.getPreAuthManagerObject(); 
		    		
		    		
		    		
		    		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		    		 ServletOutputStream sos= response.getOutputStream();
		    		 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		    		
		    		 mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
		    		 mainJasperReport1 = JasperCompileManager.compileReport(mainJasperFile);
		    		 vidalJasperReport = JasperCompileManager.compileReport(vidalJasperFile);
		    		 alkootJasperReport = JasperCompileManager.compileReport(alkootJasperFile);
		    		 
		    		 
		    		 
		    		 ttkReportDataSource1 =  preAuthObject.getTatReportYesterdat(parameterValue1);//TAT summary
		    		 ttkReportDataSource2 =  preAuthObject.getTatVidalTeamRpt(parameterValue1);//vidal Team
		    		 ttkReportDataSource3 =  preAuthObject.getTatAlkootTeamRpt(parameterValue1);//Alkoot Team
		    		 
		    		 
		    		 hashMap.put("vidalTeamTATRpt",ttkReportDataSource2);
					 hashMap.put("vidalJasperReport",vidalJasperReport);
					 hashMap.put("alkootTeamTATRpt",ttkReportDataSource3);
					 hashMap.put("alkootJasperReport",alkootJasperReport);
		    		 
		    		 
		    		 if((ttkReportDataSource.getResultData().next()) || (ttkReportDataSource1.getResultData().next()) )
					 {
	    			  
	    			  if( (ttkReportDataSource.getResultData().next()) ){
	    				  ttkReportDataSource.getResultData().beforeFirst();
	    				  mainJasperPrint = JasperFillManager.fillReport( mainJasperReport, hashMap,ttkReportDataSource);
	    				  list.add(mainJasperPrint);
	    			  }
	    			  else{
	    				  mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    				 list.add(mainJasperPrint);  
	    			  }
	    			  if( (ttkReportDataSource1.getResultData().next()) ){
	    				  ttkReportDataSource1.getResultData().beforeFirst();
	    				  mainJasperPrint1 = JasperFillManager.fillReport( mainJasperReport1, hashMap,ttkReportDataSource1);
	    				  list.add(mainJasperPrint1);
	    			  }
	    			  else{
		    				
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    				 list.add(mainJasperPrint);
		    		 
	    			  }
	    			   
					 }
	    		 
	    		 else {
	    				
	    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
	    				 list.add(mainJasperPrint);
						 list.add(mainJasperPrint);
	    		 }
	    		 
	    		 
	    		 if("EXL".equals(reportType)){
	    			 
	    			 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{"TATReport","Preapprovals"});
					 response.addHeader("Content-Disposition","attachment; filename=TATReport.xls");
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
					 request.setAttribute("boas",boas);
	    		 }
		    		 
		    		
		    		
		    		
				 
				 return (mapping.findForward(strReportdisplay));
	 				//return this.getForward(strCollectionSearch, mapping, request);
	} 
	  
	  catch(TTKException expTTK)
 	{
 		return this.processExceptions(request, mapping, expTTK);
 	}
 	catch(Exception exp)
 	{
 		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
 	}
		 
		 
		 
		 
		 
		 
		 
		 
	 }
	 
	 */
	 
	 
	 /* public ActionForward doGenerateProductivityReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
				
		  try {
			  log.info("Inside the doGenerateProductivityReport method of CashlessReportsAction");
	    		setLinks(request);
	    		 DynaActionForm frmCashless =(DynaActionForm)form;
	    		 JasperReport mainJasperReport,mainJasperReport1,emptyReport,mtdJasperReport;
				 JasperPrint mainJasperPrint;
				 JasperPrint mainJasperPrint1;
				 TTKReportDataSource ttkReportDataSource = null;
				 TTKReportDataSource ttkReportDataSource7 = null;
				 TTKReportDataSource ttkReportDataSource8 = null;
				 TTKReportDataSource ttkReportProductivityDataSource = null;
				 String jrxmlfile = "";
				 String mainJrxmlFile = "";
				 String mtdJrxmlFile = "";
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
		    		
		    		String startDate= frmCashless.getString("startDate");
		    		String endDate= frmCashless.getString("endDate");
		    		String reportType= "EXL";
		    		String reportID= "PRDT";
		    		String parameterValue= "|"+reportID+"|"+startDate+"|"+endDate+"|";
		    		String parameterValue1= "|"+startDate+"|"+endDate+"|";
		    		// jrxmlfile = "reports/cashless/PreApprovalActivityReport.jrxml";
		    		 jrxmlfile = "reports/cashless/ProductivityReport.jrxml";
		    		 mainJrxmlFile = "reports/cashless/ProductivityReport-Preapprovals.jrxml";
		    		 mtdJrxmlFile = "reports/cashless/ProductivityReport-MTD.jrxml";
		    		 
		    		 ttkReportDataSource = new TTKReportDataSource("ProductivityRpt",parameterValue);
		    		 HashMap<String, Object> hashMap = new HashMap<String, Object>();
		    		 
		    		 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		    		// ResultSet main_report_RS = ttkReportDataSource.getResultData();
		    		 
		    		 PreAuthManager preAuthObject = null;
		    		 preAuthObject = this.getPreAuthManagerObject();
		    		 ArrayList<JasperPrint> list = null;
					 list = new  ArrayList<JasperPrint>();
		    		 
		    		 mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
		    		 mainJasperReport1 = JasperCompileManager.compileReport(mainJrxmlFile);
		    		 mtdJasperReport = JasperCompileManager.compileReport(mtdJrxmlFile);
		    		 
		    		 ttkReportDataSource7 =  preAuthObject.getProductivityReportYestCnt(parameterValue1);
		    		 
		    		 //ttkReportDataSource8 =  preAuthObject.getProductivityReportMTDCnt(parameterValue1);
		    		 ttkReportProductivityDataSource = new TTKReportDataSource("productivityRpt",parameterValue1,"5");

		    		 hashMap.put("mtdDatasource",new JRResultSetDataSource(ttkReportProductivityDataSource.getResultData(4)));
		    		 //hashMap.put("mtdDatasource",ttkReportDataSource8);  
					 hashMap.put("mtdJasperReport",mtdJasperReport);
		    		 
		    		 SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		    		 Date date = new Date();
		    		 
		    		 String strdate= formater.format(date);
		    		 System.out.println("strdate======>"+strdate);
		    		 hashMap.put("todayDate", strdate);
		    		 
		    		 if (main_report_RS != null && main_report_RS.next()) {
		    				main_report_RS.beforeFirst();
		    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, ttkReportDataSource);
		    				  				
		    			}
		    		 if((ttkReportDataSource.getResultData().next()) || (ttkReportDataSource7.getResultData().next()))
					 {
						 
						 ttkReportDataSource.getResultData().beforeFirst();
						 ttkReportDataSource7.getResultData().beforeFirst();
						 mainJasperPrint = JasperFillManager.fillReport( mainJasperReport, hashMap,ttkReportDataSource);
						 mainJasperPrint1 = JasperFillManager.fillReport(mainJasperReport1, hashMap,ttkReportDataSource7);
						 
						 list.add(mainJasperPrint);
						 list.add(mainJasperPrint1);
						 
					 }
		    		 else {
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    				mainJasperPrint1 = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    				list.add(mainJasperPrint);
							list.add(mainJasperPrint1);
		    		 }
		    		 
		    		 
		    		 if("EXL".equals(reportType)){
		    			 
		    			 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{"ProductivityReport","Preapprovals"});
						 response.addHeader("Content-Disposition","attachment; filename=ProductivityReport.xls");
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						 jExcelApiExporter.exportReport();
						 request.setAttribute("boas",boas);
		    			 
		    			 
		    			 
		    			 
		    			 
				    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			 				jExcelApiExporter.exportReport();	
				    		
						 
		    		 }
		    		 
		    		    request.setAttribute("boas",boas);
		 				request.setAttribute("reportType", reportType);
		 				response.setHeader("Content-Disposition", "attachement; filename = ProductivityReport.xls");
		 				
		 				//frmCollections.set("letterPath", reportLink);
		 				 return (mapping.findForward(strReportdisplay));
		 				//return this.getForward(strCollectionSearch, mapping, request);
		} 
		  
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}
	  }
	 
	 */
	 
	 
	 
	 
	  public ActionForward doGenerateDetailedReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
				
		  try {
			  log.info("Inside the doGenerateDetailedReport method of CashlessReportsAction");
	    		setLinks(request);
	    		 DynaActionForm frmCashless =(DynaActionForm)form;
	    		 JasperReport mainJasperReport,emptyReport;
				 JasperPrint mainJasperPrint;
				 TTKReportDataSource ttkReportDataSource = null;
				 String jrxmlfile = "";
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
		    		
		    		String startDate= frmCashless.getString("startDate");
		    		String endDate= frmCashless.getString("endDate");
		    		String reportType= "EXL";
		    		String reportID= "DTL";
		    		String parameterValue= "|"+reportID+"|"+startDate+"|"+endDate+"|";
		    		// jrxmlfile = "reports/cashless/PreApprovalActivityReport.jrxml";
		    		 jrxmlfile = "reports/cashless/Pre-ApprovalDetailedReport.jrxml";
		    		 
		    		 ttkReportDataSource = new TTKReportDataSource("DetailedRpt",parameterValue);
		    		 HashMap<String,String> hashMap = new HashMap<String,String>();
		    		 
		    		 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		    		 ResultSet main_report_RS = ttkReportDataSource.getResultData();
		    		 mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
		    		 if (main_report_RS != null && main_report_RS.next()) {
		    				main_report_RS.beforeFirst();
		    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, ttkReportDataSource);
		    				  				
		    			} else {
		    				
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    			}
		    		 if("EXL".equals(reportType)){
				    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			 				jExcelApiExporter.exportReport();	
				    		}
		    		 
		    		    request.setAttribute("boas",boas);
		 				request.setAttribute("reportType", reportType);
		 				response.setHeader("Content-Disposition", "attachement; filename = Pre-ApprovalDetailedReport.xls");
		 				
		 				//frmCollections.set("letterPath", reportLink);
		 				 return (mapping.findForward(strReportdisplay));
		 				//return this.getForward(strCollectionSearch, mapping, request);
		} 
		  
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}
	  }
	 
	 
	 
	 
	 
	 
	 
	 
	 

	  public ActionForward doGenerateActivityReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
				
		  try {
			  log.info("Inside the doGenerateActivityReport method of CashlessReportsAction");
	    		setLinks(request);
	    		 DynaActionForm frmCashless =(DynaActionForm)form;
	    		 JasperReport mainJasperReport,emptyReport;
				 JasperPrint mainJasperPrint;
				 TTKReportDataSource ttkReportDataSource = null;
				 String jrxmlfile = "";
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
		    		
		    		String startDate= frmCashless.getString("startDate");
		    		String endDate= frmCashless.getString("endDate");
		    		String reportType= "EXL";
		    		String reportID= "ACT";
		    		String parameterValue= "|"+reportID+"|"+startDate+"|"+endDate+"|";
		    		// jrxmlfile = "reports/cashless/PreApprovalActivityReport.jrxml";
		    		 jrxmlfile = "reports/cashless/PreApprovalActivityReport.jrxml";
		    		 
		    		 ttkReportDataSource = new TTKReportDataSource("ActivityRpt",parameterValue);
		    		 HashMap<String,String> hashMap = new HashMap<String,String>();
		    		 
		    		 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		    		 ResultSet main_report_RS = ttkReportDataSource.getResultData();
		    		 mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
		    		 if (main_report_RS != null && main_report_RS.next()) {
		    				main_report_RS.beforeFirst();
		    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, ttkReportDataSource);
		    				  				
		    			} else {
		    				
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    			}
		    		 if("EXL".equals(reportType)){
				    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			 				jExcelApiExporter.exportReport();	
				    		}
		    		 
		    		    request.setAttribute("boas",boas);
		 				request.setAttribute("reportType", reportType);
		 				response.setHeader("Content-Disposition", "attachement; filename = Pre-ApprovalActivityReport.xls");
		 				
		 				//frmCollections.set("letterPath", reportLink);
		 				 return (mapping.findForward(strReportdisplay));
		 				//return this.getForward(strCollectionSearch, mapping, request);
		} 
		  
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}
	  }
	 
	 
	 
	 
	  private PreAuthManager getPreAuthManagerObject() throws TTKException {
			PreAuthManager preAuthManager = null;
			try {
				if (preAuthManager == null) {
					InitialContext ctx = new InitialContext();
					preAuthManager = (PreAuthManager) ctx
							.lookup("java:global/TTKServices/business.ejb3/PreAuthManagerBean!com.ttk.business.preauth.PreAuthManager");
				}// end if
			}// end of try
			catch (Exception exp) {
				throw new TTKException(exp, strPreAuthDetail);
			}// end of catch
			return preAuthManager;
		}// end getPreAuthManagerObject()
  
	  
	public ActionForward doGenerateTATReport(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws TTKException {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		ArrayList<JasperPrint> reportList = new ArrayList<JasperPrint>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		JasperReport objJasperReport, emptyReport = null;
		try {
			log.info("Inside the doGenerateTATReport method of CashlessReportsAction");
			setLinks(request);
			DynaActionForm frmCashless = (DynaActionForm) form;
			String startDate = frmCashless.getString("startDate");
			String endDate = frmCashless.getString("endDate");
			String reportID = "TAT";
			String flag = "Y";
			String fileFormat = ".xls";
			String parameterValue = "|" + reportID + "|" + startDate + "|"+ endDate + "|";
				 	 
			String mainJasperFile = "reports/cashless/TATReport.jrxml";
			String tatJasperFile = "reports/cashless/TATReport-preapprovals.jrxml";
			String vidalJasperFile = "reports/cashless/TATReport-Vidal.jrxml";
			String alkootJasperFile = "reports/cashless/TATReport-Alkoot.jrxml";
			ArrayList inputParameterList = new ArrayList<>();
			inputParameterList.add(parameterValue);
			TTKReportDataSource ttkReportDataSource = new TTKReportDataSource("TATRpt", inputParameterList, flag);

			ResultSet[] resultSetArray = ttkReportDataSource.getResultSet();
			if (resultSetArray[0] != null) {
				JasperPrint jasperPrint = null;
				try (OracleCachedRowSet coversheetRowSet = (OracleCachedRowSet) resultSetArray[0]) {
					if (coversheetRowSet.next()) {
						objJasperReport = JasperCompileManager.compileReport(mainJasperFile);
						coversheetRowSet.beforeFirst();
						JRDataSource coversheetResultsetDatasource = new JRResultSetDataSource(coversheetRowSet);
						jasperPrint = JasperFillManager.fillReport(objJasperReport, hashMap,coversheetResultsetDatasource);
					} else {
						emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
					}
				}
				reportList.add(jasperPrint);
			}

			if (resultSetArray[1] != null) {
				JasperPrint jasperPrint = null;
				OracleCachedRowSet summary1RowSet = (OracleCachedRowSet) resultSetArray[1];
				if (summary1RowSet.next()) {
					summary1RowSet.beforeFirst();
					while (summary1RowSet.next()) {
					}
					System.out.println("This is 2nd cursor");
					objJasperReport = JasperCompileManager.compileReport(tatJasperFile);
					summary1RowSet.beforeFirst();
					JRDataSource summary1ResultsetDatasource = new JRResultSetDataSource(summary1RowSet);
					jasperPrint = JasperFillManager.fillReport(objJasperReport,hashMap, summary1ResultsetDatasource);
				} else {
					emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
				}
				reportList.add(jasperPrint);
			}

			if (resultSetArray[2] != null) {
				JasperPrint jasperPrint = null;
				OracleCachedRowSet summary2RowSet = (OracleCachedRowSet) resultSetArray[2];
				if (summary2RowSet.next()) {
					summary2RowSet.beforeFirst();
					System.out.println("This is 3rd cursor");
					while (summary2RowSet.next()) {
					}
					objJasperReport = JasperCompileManager.compileReport(vidalJasperFile);
					summary2RowSet.beforeFirst();
					JRDataSource summary2ResultsetDatasource = new JRResultSetDataSource(summary2RowSet);
					jasperPrint = JasperFillManager.fillReport(objJasperReport,hashMap, summary2ResultsetDatasource);
				} else {
					emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
				}
				reportList.add(jasperPrint);
			}
			if (resultSetArray[3] != null) {
				JasperPrint jasperPrint = null;
				OracleCachedRowSet claimsRecoveriesRowSet = (OracleCachedRowSet) resultSetArray[3];
				if (claimsRecoveriesRowSet.next()) {
					System.out.println("This is 4th cursor");
					objJasperReport = JasperCompileManager.compileReport(alkootJasperFile);
					claimsRecoveriesRowSet.beforeFirst();
					JRDataSource claimsRecoveriesResultsetDatasource = new JRResultSetDataSource(claimsRecoveriesRowSet);
					jasperPrint = JasperFillManager.fillReport(objJasperReport,hashMap, claimsRecoveriesResultsetDatasource);
				} else {
					emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
				}
				reportList.add(jasperPrint);

				if (".xls".equals(fileFormat) /*|| ".XLS".equals(fileFormat)*/) {
					String[] sheetNames = { "Coversheet","TATSummary","TATSummaryVidalTeam","TATSummaryAlKootTeam" };
					JRXlsExporter jExcelApiSaver = new JRXlsExporter();
					jExcelApiSaver.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,reportList);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,Boolean.FALSE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
					jExcelApiSaver.exportReport();
				 	 request.setAttribute("boas",boas);
				 	 response.setContentLength(boas.toByteArray().length);
				 	 request.setAttribute("reportID","TAT");
				 	 response.addHeader("Content-Disposition","attachment; filename=TAT_Report.xls");
				}
			}
			return (mapping.findForward(strReportdisplay));
		} catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		} catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
		}
	}
	
	
	 public ActionForward doGenerateProductivityReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
		 	ByteArrayOutputStream boas = new ByteArrayOutputStream();
			ArrayList<JasperPrint> reportList = new ArrayList<JasperPrint>();
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			JasperReport objJasperReport, emptyReport = null;
		  try {
			  log.info("Inside the doGenerateProductivityReport method of CashlessReportsAction");
				setLinks(request);
				DynaActionForm frmCashless = (DynaActionForm) form;
				String startDate = frmCashless.getString("startDate");
				String endDate = frmCashless.getString("endDate");
				String reportID = "PRDT";
				String flag = "Y";
				String fileFormat = ".xls";
				String parameterValue = "|" + reportID + "|" + startDate + "|"+ endDate + "|";
					 	 
				String mainJasperFile = "reports/cashless/ProductivityReport.jrxml";
				String preauthJasperFile = "reports/cashless/ProductivityReport-Preapprovals.jrxml";
				String mtdJasperFile = "reports/cashless/ProductivityReport-MTD.jrxml";
				ArrayList inputParameterList = new ArrayList<>();
				inputParameterList.add(parameterValue);
				TTKReportDataSource ttkReportDataSource = new TTKReportDataSource("ProductivityRpt", inputParameterList, flag);

				ResultSet[] resultSetArray = ttkReportDataSource.getResultSet();
				if (resultSetArray[0] != null) {
					JasperPrint jasperPrint = null;
					try (OracleCachedRowSet coversheetRowSet = (OracleCachedRowSet) resultSetArray[0]) {
						if (coversheetRowSet.next()) {
							objJasperReport = JasperCompileManager.compileReport(mainJasperFile);
							coversheetRowSet.beforeFirst();
							JRDataSource coversheetResultsetDatasource = new JRResultSetDataSource(coversheetRowSet);
							jasperPrint = JasperFillManager.fillReport(objJasperReport, hashMap,coversheetResultsetDatasource);
						} else {
							emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
							jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
						}
					}
					reportList.add(jasperPrint);
				}

				if (resultSetArray[1] != null) {
					JasperPrint jasperPrint = null;
					OracleCachedRowSet summary1RowSet = (OracleCachedRowSet) resultSetArray[1];
					if (summary1RowSet.next()) {
						summary1RowSet.beforeFirst();
						while (summary1RowSet.next()) {
						}
						System.out.println("This is 2nd cursor");
						objJasperReport = JasperCompileManager.compileReport(preauthJasperFile);
						summary1RowSet.beforeFirst();
						JRDataSource summary1ResultsetDatasource = new JRResultSetDataSource(summary1RowSet);
						jasperPrint = JasperFillManager.fillReport(objJasperReport,hashMap, summary1ResultsetDatasource);
					} else {
						emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
					}
					reportList.add(jasperPrint);
				}

				if (resultSetArray[2] != null) {
					JasperPrint jasperPrint = null;
					OracleCachedRowSet claimsRecoveriesRowSet = (OracleCachedRowSet) resultSetArray[2];
					if (claimsRecoveriesRowSet.next()) {
						System.out.println("This is 3rd cursor");
						objJasperReport = JasperCompileManager.compileReport(mtdJasperFile);
						claimsRecoveriesRowSet.beforeFirst();
						JRDataSource claimsRecoveriesResultsetDatasource = new JRResultSetDataSource(claimsRecoveriesRowSet);
						jasperPrint = JasperFillManager.fillReport(objJasperReport,hashMap, claimsRecoveriesResultsetDatasource);
					} else {
						emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
					}
					reportList.add(jasperPrint);

					if (".xls".equals(fileFormat)) {
						String[] sheetNames = { "Coversheet","ProductivityReport ","ProductivityReportMTD" };
						JRXlsExporter jExcelApiSaver = new JRXlsExporter();
						jExcelApiSaver.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,reportList);
						jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
						jExcelApiSaver.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
						jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
						jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,Boolean.FALSE);
						jExcelApiSaver.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames);
						jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
						jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
						jExcelApiSaver.exportReport();
					 	 request.setAttribute("boas",boas);
					 	 response.setContentLength(boas.toByteArray().length);
					 	 request.setAttribute("reportID","PRDT");
					 	 response.addHeader("Content-Disposition","attachment; filename=Productivity_Report.xls");
					}
				}
		 				 return (mapping.findForward(strReportdisplay));
		  	} 
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}
	  }	 
}
