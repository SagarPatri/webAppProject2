 /** 
  * @ (#) ReportsAction.java Jun 22, 2006
  * Project      : TTK HealthCare Services
  * File         : ReportsAction.java
  * Author       : Arun K N
  * Company      : Span Systems Corporation
  * Date Created : Jun 22, 2006
  *
  * @author       :  Arun K N
  * Modified by   :
  * Modified date :
  * Reason        :
  */

 package com.ttk.action.reports;

 import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
//added for mail-sms template
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream; 
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.PageSize;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;




//ended
import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.JXLException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
//import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimBatchManager;
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.business.finance.ChequeManager;
import com.ttk.business.reports.TTKReportManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDAOImpl;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dao.impl.reports.TTKReportDataSourceMultiple;
import com.ttk.dto.administration.MOUDocumentVO;
import com.ttk.dto.administration.ReportVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.empanelment.HospitalVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

 public class ReportsAction extends TTKAction{

	 private static final Logger log = Logger.getLogger( ReportsAction.class );
	 //declaration of forward paths
	 private static final String strEnrollReportList="enrollreportlist";
	 private static final String strPreAuthReportList="preauthreportlist";
	 private static final String strEmpanelReportList="empanelreportlist";
	 private static final String strClaimsReportList="claimsreportlist";
	 private static final String strFinanceReportList="financereportlist";
	 private static final String strCodingReportList="codingreportlist";
	 private static final String strFaxStatusList="faxstatuslist";
	 private static final String strCallCenterList="callcenterlist";
	 private static final String strCitibankList="citibanklist";
	 private static final String strFinBatchReportList="finbatchreportlist";
	 private static final String strTDSRemitList="tdsremitlist";
	 private static final String strUniSampoList="unisampolist";
	 private static final String strInsLoginReports="insReportList";
	 private static final String strDailyRemit="dailyremitrpt";
	 private static final String strEnrollParameter="enrollparameter";
	 private static final String strPreAuthParameter="preauthparameter";
	 private static final String strEmpanelParameter="empparameter";
	 private static final String strClaimsParameter="claimsparameter";
	 private static final String strFinanceParameter="financeparameter";	 
	 private static final String strCodingParameter="codingparameter";
	 private static final String strCodeCleanupReportParameter="codecleanupparameter";
	 private static final String strReportdisplay="reportdisplay";
	 private static final String strTPAComParameter="tpacomparameter";
	 private static final String strFaxStatusParameter="faxstatusparameter";
	 private static final String strCitibankParameter="citibankparameter";
	 private static final String strTDSRemitParameter="tdsremitparameter";
	 private static final String strUniSampoParameter="unisampoparameter";
	 private static final String strBatchDetail="batchdetail";
	 private static final String strBankAccNo="bankaccno";
	 private static final String strClaimPendRptDetail="claimpendrptdetail";
	 private static final String strClaimsReportDetail="claimsreportdetail";
	 private static final String strIOBRptDetail="iobrptdetail";
	 private static final String strAccentureRptDetail="accenturerptdetail";
	 private static final String strPreAuthClmDetail="preauthclmdetail";
	// private static final String strFaxStatusReportList="faxstatusparameter";
	 //declaration of constants
	 private static final String strEnrollment="Enrollment";
	 private static final String strEmpanelment="Empanelment";
	 private static final String strPreAuth="Pre-Authorization";
	 private static final String strClaims="Claims";
	 private static final String strFinance="Finance";
	 private static final String strCoding="Coding";
	 private static final String strCodeCleanup="Code CleanUp";
	 private static final String strFaxStatus="Common";
	 private static final String strCallCenter="Call Center";
	 private static final String strCitiBank="Citibank";
	 private static final String strTDSRemittance="TDS";
	 private static final String strComprUniSampo="Comprehensive";
	 private static final String strInsReports="DashBoard";
	 //Exception Message Identifier
	 private static final String strReportExp="report";
//added for IBM
	 private static final String strCorporateReportsList = "ibmcorporatereportlist";
	 private static final String strCorporate="Corporate";
	 private static final String strIBMRptDetail="ibmrptdetail";
	private static final String strIBMAdditioncutoffMaxRecRptDetail = "ibmadditioncutoffmaxrecrptdetail";
	private static final String strIBMPreAuthRptDetail="ibmpreauthrptdetail";
	private static final String strIBMReoptinRptDetail="ibmreoptinrptdetail";
	private static final String strIBMMontlyReconRptDetail="ibmmontlyreconrptdetail";
	private static final String strIBMChildBornRptDetail="ibmchildbornrptdetail";
	private static final String strIBMDailyRptDetail="ibmdailyrptdetail";
	private static final String strIBMNewDeletionRptDetail="ibmnewdeletionrptdetail";
	private static final String strGenerateQuote="generatequote";
	
	 private static final String strnewClaimsReportDetail="newclaimsreportdetail";
	 private static final String strReportdisplayforClaimPaid = "ReportdisplayforClaimPaid";	
	private static final String straddeditinvoice="addeditinvoice";
	private static final String straddeditcreditnote="addeditcreditnote";
	 private static final String strRoutineReport="routineReport";
	 private static final String strPreApprovalReport="preapprovalReport";
	 private static final String strUnderwritingReport="underwritingReport";
	 private static int MAX_LENGTH=65536;

	 /**
	  * This method is used to initialize the screen.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 String strForwardPathList="";
			 String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			 String strActiveLink=TTKCommon.getActiveLink(request);
			 //set the Forward Paths based on the sublink
			 if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 {
				 strForwardPathList=strEnrollReportList;
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 else if(strActiveSubLink.equalsIgnoreCase(strEmpanelment))
			 {
				 strForwardPathList=strEmpanelReportList;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strEmpanelment))
			 //added for ibm
			 else if(strActiveSubLink.equalsIgnoreCase(strCorporate))
			 {
				 strForwardPathList=strCorporateReportsList;
			 }//else if(strActiveSubLink.equalsIgnoreCase(strCorporate))
			 else if(strActiveSubLink.equalsIgnoreCase(strPreAuth))
			 {
				 strForwardPathList=strPreAuthReportList;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strPreAuth))
			 else if(strActiveSubLink.equalsIgnoreCase(strClaims))
			 {
				 strForwardPathList=strClaimsReportList;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strClaims))
			 else if(strActiveSubLink.equalsIgnoreCase(strFinance))
			 {
				 strForwardPathList=strFinanceReportList;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strFinance))
			 else if(strActiveSubLink.equalsIgnoreCase(strCoding))
			 {
				 strForwardPathList=strCodingReportList;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strCoding))
			 else if(strActiveSubLink.equalsIgnoreCase(strCodeCleanup))
			 {
				 strForwardPathList=strCodeCleanup;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strCoding))
			 else if(strActiveSubLink.equalsIgnoreCase(strFaxStatus))
			 {
				 strForwardPathList=strFaxStatusList;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strFaxStaust))
			 else if(strActiveLink.equals("Reports") && strActiveSubLink.equals(strCallCenter))
			 {
				 strForwardPathList=strCallCenterList;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strCallCenter))
			 else if(strActiveSubLink.equalsIgnoreCase(strCitiBank))
			 {
				 strForwardPathList=strCitibankList;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strFaxStaust))
			 else if(strActiveSubLink.equalsIgnoreCase(strTDSRemittance))
			 {
				 strForwardPathList=strTDSRemitList;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strTDSRemittance))
			 else if(strActiveSubLink.equalsIgnoreCase(strComprUniSampo))
			 {
				 strForwardPathList=strUniSampoList;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strEmerUniSampo))			 
			 else if(strActiveSubLink.equalsIgnoreCase(strInsReports))
			 {
				 strForwardPathList=strInsLoginReports;
			 }//end of  else if(strActiveSubLink.equalsIgnoreCase(strEmerUniSampo))	
			 org.dom4j.Document reportsListDoc=null;
			 ((DynaActionForm)form).initialize(mapping);//reset the form data
			 //get the reports list Document
			 reportsListDoc=TTKCommon.getDocument("ReportsList.xml");
			 request.setAttribute("ReportsListDoc",reportsListDoc);
			 return this.getForward(strForwardPathList,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 String strReportParameter="";
			 String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			 //set the Forward Paths based on the sublink
			 if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 {
				 strReportParameter=strEnrollParameter;
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 else if(strActiveSubLink.equalsIgnoreCase(strEmpanelment))
			 {
				 strReportParameter=strEmpanelParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strEmpanelment))
			 else if(strActiveSubLink.equalsIgnoreCase(strPreAuth))
			 {
				 strReportParameter=strPreAuthParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strPreAuth))
			 else if(strActiveSubLink.equalsIgnoreCase(strClaims))
			 {
				 strReportParameter=strClaimsParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strClaims))
			 else if(strActiveSubLink.equalsIgnoreCase(strFinance))
			 {
				 strReportParameter=strFinanceParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strFinance))
			 else if(strActiveSubLink.equalsIgnoreCase(strCoding))
			 {
				 strReportParameter=strCodingParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strCoding))
			 else if(strActiveSubLink.equalsIgnoreCase(strCodeCleanup))
			 {
				 strReportParameter=strCodeCleanupReportParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strCoding))
			 else if(strActiveSubLink.equalsIgnoreCase(strCitiBank))
			 {
				 strReportParameter=strCitibankParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strCoding))
			 else if(strActiveSubLink.equalsIgnoreCase(strTDSRemittance))
			 {
				 strReportParameter=strTDSRemitParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strTDSRemittance))		
			 else if(strActiveSubLink.equalsIgnoreCase(strComprUniSampo))
			 {
				 strReportParameter=strUniSampoParameter;
			 }//end of else if(strActiveSubLink.equalsIgnoreCase(strEmerUniSampo))
			 DynaActionForm frmReport=(DynaActionForm)form;
			 frmReport.set("reportName",request.getParameter("reportName"));
			 org.dom4j.Document parameterDoc=TTKCommon.getDocument(request.getParameter("fileName"));
			 request.setAttribute("parameterDoc",parameterDoc);
			 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doBatchReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doBatchReportsDetail method of ReportAction");
			 setLinks(request);
			 DynaActionForm frmBatchReport=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmBatchReport.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strFinBatchReportList,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doBatchReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doTDSReports(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doDefualt method of TDSReportAction");
			 setLinks(request);
			 DynaActionForm frmDailyRemitReport=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmDailyRemitReport.set("reportName",request.getParameter("reportName"));
			 if(request.getParameter("reportName").equals("Daily Transfer Report"))
			 {
				 return this.getForward(strDailyRemit,mapping,request);
			 }//end of if(request.getParameter("reportName").equals("Daily Remittance Report"))
			 else if(request.getParameter("reportName").equals("Challan Details Report"))
			 {
				 return this.getForward("challandetailsrpt",mapping,request);
			 }//end of if(request.getParameter("reportName").equals("Challan Details Report"))
			 else
			 {
				 return this.getForward("annexurechallanrpt",mapping,request);
			 }//end of else		
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doBatchReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	/**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doTPAComReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doReportsDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strTPAComParameter;
			 log.debug("Report Name is ::::::::::"+request.getParameter("reportName"));			 
			 DynaActionForm frmTpaComReport=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmTpaComReport.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doTPAComReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doClaimPendRptDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doClaimPendRptDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strClaimPendRptDetail;
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doClaimPendRptDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doClaimsReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doClaimsReportDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strClaimsReportDetail;
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doClaimsReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 
	 
	 
	 
	 
	 
	 /**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doNewClaimsReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doNewClaimsReportDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strnewClaimsReportDetail;
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doClaimsReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 
	 /**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doIOBReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().setAttribute("frmReportList",frmReportList);
			 String strReportParameter=strIOBRptDetail;
			 return mapping.findForward(strReportParameter);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doIOBReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doAccentureReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().setAttribute("frmReportList",frmReportList);
			 String strReportParameter=strAccentureRptDetail;
			 return mapping.findForward(strReportParameter);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doAccentureReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	//Added for IBM....10.2
		/**
				  * This method is used to view the IBMAdditioncutoff report details.
				  * Finally it forwards to the appropriate view based on the specified forward mappings
				  *
				  * @param mapping The ActionMapping used to select this instance
				  * @param form The optional ActionForm bean for this request (if any)
				  * @param request The HTTP request we are processing
				  * @param response The HTTP response we are creating
				  * @return ActionForward Where the control will be forwarded, after this request is processed
				  * @throws Exception if any error occurs
				  */
				 public ActionForward doIBMReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					 try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMRptDetail;
						 return mapping.findForward(strReportParameter);
					 }//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
		 }//end of doIBMReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


		//Ended..


		//Added for IBM...10.3
		/**
				  * This method is used to view the IBMLoginChildBornReportDetail report details.
				  * Finally it forwards to the appropriate view based on the specified forward mappings
				  *
				  * @param mapping The ActionMapping used to select this instance
				  * @param form The optional ActionForm bean for this request (if any)
				  * @param request The HTTP request we are processing
				  * @param response The HTTP response we are creating
				  * @return ActionForward Where the control will be forwarded, after this request is processed
				  * @throws Exception if any error occurs
				  */

				public ActionForward doIBMAdditioncutoffMaxRecReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMAdditioncutoffMaxRecRptDetail;
						 return mapping.findForward(strReportParameter);
					 }
						//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
				 }//end of doIBMDailyCancellationReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

				
				public ActionForward doGenerateGrpPreAuthRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMPreAuthRptDetail;
						 return mapping.findForward(strReportParameter);
					 }
						//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
				 }//end of doIBMDailyCancellationReportDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
				public ActionForward doIBMReoptinRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMReoptinRptDetail;
						 return mapping.findForward(strReportParameter);
					 }
						//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
				 }//end of doIBMReoptinRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			
		//Ended..
				public ActionForward doIBMMontlyReconRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMMontlyReconRptDetail;
						 return mapping.findForward(strReportParameter);
					 }
						//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
				 }//end of strIBMMontlyReconRptDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
				
				public ActionForward doIBMChildBornRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMChildBornRptDetail;
						 return mapping.findForward(strReportParameter);
					 }
						//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
				 }//end of strIBMMontlyReconRptDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
					
				public ActionForward doIBMDailyRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMDailyRptDetail;
						 return mapping.findForward(strReportParameter);
					 }
						//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
				 }//end of doIBMDailyRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
				
				public ActionForward doNewIBMDeletion(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
					try{
						 setLinks(request);
						 DynaActionForm frmReportList=(DynaActionForm)form;
						 request.getSession().setAttribute("frmReportList",frmReportList);
						 String strReportParameter=strIBMNewDeletionRptDetail;
						 return mapping.findForward(strReportParameter);
					 }
						//end of try
					 catch(TTKException expTTK)
					 {
						 return this.processExceptions(request, mapping, expTTK);
					 }//end of catch(TTKException expTTK)
					 catch(Exception exp)
					 {
						 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
					 }//end of catch(Exception exp)
				 }//end of doIBMDailyRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
				/**
	  * This method is used to view the report details.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doFaxStatusReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doReportsDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strFaxStatusParameter;
			 log.debug("Report Name is ::::::::::"+request.getParameter("reportName"));			 
			 DynaActionForm frmTpaComReport=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmTpaComReport.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doFaxStatusReportsDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**
	  * This method is used to display the generated report set as ByteArrayOutputStream
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doDisplayReport(ActionMapping mapping,ActionForm form,
			 HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 request.setAttribute("boas",request.getAttribute("boas"));			
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doDisplayReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 String strActiveLink=TTKCommon.getActiveLink(request);
			  //added for KOC-1276
			 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			 //add ended
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 String strPath = "";
			 String strUserID="";//added for KOC-1267
			 TTKReportDataSource ttkReportDataSource = null;
			 String strParam = "";
			 String strFinParam = "";
			//   
			 if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 {
				 strParam = request.getParameter("parameter").replaceAll("__","&");
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 else {
				 strParam = request.getParameter("parameter");
			 }//end of else
			 strFinParam = request.getParameter("parameter")+"F|";
			 String jrxmlfile=request.getParameter("fileName");
			 log.debug("doGenerateReport jrxmlfile doGenerateReport>>"+jrxmlfile);
			 String strComplete = request.getParameter("completedYN");
			 if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 {
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 else if(request.getParameter("reportID").equalsIgnoreCase("76ColPend") || request.getParameter("reportID").equalsIgnoreCase("FinPenRpt"))
			 {
				 log.debug("76 column report parameter"+strFinParam);
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strFinParam);
			 }//end of else if(request.getParameter("reportID").equalsIgnoreCase("76ColPend") || request.getParameter("reportID").equalsIgnoreCase("FinPenRpt"))
			 else if(request.getParameter("reportID").equalsIgnoreCase("CitiFinDetRpt"))
			 {
				if(request.getParameter("reportType").equals("EXL"))
				 {
					 jrxmlfile = "reports/finance/CitibankClaimsDetailEXL.jrxml";
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else if(request.getParameter("reportType").equals("PDF"))
				 {
					 jrxmlfile = "reports/finance/CitibankClaimsDetail.jrxml";
				 }//end of else if(request.getParameter("reportType").equals("PDF"))
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("CitiFinDetRpt"))
			 else if(request.getParameter("reportID").equalsIgnoreCase("FurGenRpt"))
			 {
				 jrxmlfile = "reports/finance/FutureGeneraliReport.jrxml";
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("FurGenRpt"))
			  //Added for KOC-1267
			 else if(request.getParameter("reportID").equalsIgnoreCase("ClaimViewHis"))
			 {
				 
				 strUserID = userSecurityProfile.getUSER_ID();
				 //strParam = strParam.concat((new StringBuffer(strUserID+"|")).toString());
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }
			 else if(request.getParameter("reportID").equalsIgnoreCase("PreAuthUtilization"))//intX
			 {
				 jrxmlfile = "reports/intX/PreAuthUtilization.jrxml";
				 strUserID = userSecurityProfile.getUSER_ID();
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"));
			 }
			 //Ended
			 else
			 {
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
			 }//end of else
			 
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+request.getParameter("authorizationNo")+".pdf";
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, String> hashMap = new HashMap<String, String>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("Start Date",request.getParameter("startDate"));
				 hashMap.put("End Date",request.getParameter("endDate"));
				 if(request.getParameter("reportID").equals("GenRenMemXLs")) {
					 hashMap.put("parameter",strParam);
				 }//end of if(request.getParameter("reportID").equals("GenRenMemXLs")
				 else if(request.getParameter("reportID").equals("EnrDaiBOI")){
					 hashMap.put("Policy Number",request.getParameter("policyNumber"));					 
				 }//end of else if(request.getParameter("reportID").equals("")){
				 else if(request.getParameter("reportID").equals("SoftCopyUploadError"))
				 {
					 hashMap.put("Status", request.getParameter("status"));
				 }//end of else if(request.getParameter("reportID").equals("SoftCopyUploadError"))
				 else {
					 hashMap.put("parameter",strParam.substring(2,strParam.length()));
				 }//end of else
				 strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
				 hashMap.put("SigPath",strPath);				 
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 /*if(request.getParameter("reportID").equalsIgnoreCase("76ColPend") || request.getParameter("reportID").equalsIgnoreCase("FinPenRpt"))
					 {
						 log.debug("76 column report parameter jasperPrint    ::::::::::"+strFinParam);
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new TTKReportDataSource(request.getParameter("reportID"),strFinParam));
					 }//end of if(request.getParameter("reportID").equalsIgnoreCase("76ColPend") || request.getParameter("reportID").equalsIgnoreCase("FinPenRpt"))
					 else if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
					 {
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new TTKReportDataSource(request.getParameter("reportID"),strParam));
					 }//end of if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
					 else
					 {
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter")));
					 }//end of else
					 */
				 }//end of if(ttkReportDataSource.getResultData().next())
				 
				 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
				 {
					//   
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else//if report type if PDF
				 {
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
					 if((request.getParameter("reportID").equalsIgnoreCase("AuthLetter")) && (strActiveLink.equals(strPreAuth) 
							 && strComplete.equals("Y")))
					 {
						 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
					 }//end of if((request.getParameter("reportID").equalsIgnoreCase("AuthLetter")) && (strActiveLink.equals(strPreAuth)
				 }//end of else
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 
	 /**
	  * THIS METHOD TO GENERATE EXCEL REPORTS FOR INTX - MIS REPORTS
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 
	 @SuppressWarnings("deprecation")
	public ActionForward doGenerateExcelReportIntX(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			//   
			 setLinks(request);
			 String strActiveLink=TTKCommon.getActiveLink(request);
			 String parameter	=	request.getParameter("parameter");
			  //added for KOC-1276
			 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			 //add ended
			 TTKReportManager tTKReportManager = null;
			 try
			 { 
				 DynaActionForm frmMISReports	=	(DynaActionForm) form;
				 String strSdate	=	"";
				 String strEdate	=	"";
				 String strPayer	=	request.getParameter("payerCodes");
				 String strProvider	=	request.getParameter("providerCodes");
				 String strCorps	=	request.getParameter("corporateCodes");
				 if("PRP".equals(parameter)){
					 strSdate	=	request.getParameter("startDate");
					 strEdate	=	request.getParameter("endDate");
				 }else{
					 strSdate	=	request.getParameter("sStartDate");
					 strEdate	=	request.getParameter("sEndDate");
				 }
				 String csStartDate	=	request.getParameter("csStartDate");
				 String csEndDate	=	request.getParameter("csEndDate");
				 String sAgentCode  =   request.getParameter("sAgentCode");
				 String sType  =   request.getParameter("sType");
				 String eType  =   request.getParameter("eType");
				 String sStatus  =   request.getParameter("sStatus");
				 String insCompanyCode  =   request.getParameter("insCompanyCode");
				 String sGroupPolicyNo  =   request.getParameter("sGroupPolicyNo");

				 tTKReportManager	=	this.getTTKReportManager();
				 ArrayList alData	=	null;
				 alData				=	tTKReportManager.getPreAuthUtilizationReport(parameter,strSdate,strEdate,strPayer,strProvider,strCorps,csStartDate,csEndDate,sAgentCode,sType,eType,sStatus,insCompanyCode,sGroupPolicyNo);
				 request.setAttribute("alData", alData);
				 request.setAttribute("reportType",request.getParameter("reportType"));
				 return (mapping.findForward("preauthutilization"));
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateExcelReportIntX(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 
	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateAuthReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doGenerateAuthReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport,emptyReport,jasperSubReport;
			 JasperPrint jasperPrint;
			 String strActiveLink=TTKCommon.getActiveLink(request);
			 String jrxmlfile=request.getParameter("fileName");
			 String strComplete = request.getParameter("completedYN");
			 String strPath = "";
			 TTKReportDataSource ttkReportDataSource=null;
			 TTKReportDataSource ttkSubReportDataSource=null;
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
			 ttkSubReportDataSource = new TTKReportDataSource("AuthLetter",request.getParameter("parameter"));
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+request.getParameter("authorizationNo")+".pdf";
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 log.info("jrxmlfile>>"+jrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 jasperSubReport = JasperCompileManager.compileReport("generalreports/CitibankAuthAprLetterSub.jrxml");

				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("MyDatasource",ttkSubReportDataSource);
				 hashMap.put("CitibankAuthSubReport",jasperSubReport);
				 strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
				 hashMap.put("SigPath",strPath);
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
				 }//end of if(ttkReportDataSource.getResultData().next())
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 if(strActiveLink.equals(strPreAuth) && strComplete.equals("Y"))
				 {
					 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
				 }//end of if(strActiveLink.equals(strPreAuth) && strComplete.equals("Y"))
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateAuthReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateClaimsDetailsReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 
		 String strReportID ="";
		 try{
			 //  
			 log.debug("Inside the doGenerateClaimsDetailsReport method of ReportsAction");
			 setLinks(request);
			 
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile = "";
			 
			 if(request.getParameter("selectRptType").equalsIgnoreCase("CAC"))
			 {
				 jrxmlfile = "reports/finance/EFTClaimsDetailReport.jrxml";
				 strReportID = "FinDetEFTRpt";
			 }//end of if(request.getParameter("selectRptType").equalsIgnoreCase("CAC"))
			 else
			 {
				 strReportID = "FinDetRpt";
				 if(request.getParameter("reportType").equals("EXL"))
				 {
					 jrxmlfile = "reports/finance/ClaimsDetailEXL.jrxml";
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else if(request.getParameter("reportType").equals("PDF"))
				 {
					 jrxmlfile = "reports/finance/ClaimsDetail.jrxml";
				 }//end of else if(request.getParameter("reportType").equals("PDF"))
			 }//end of else
			 
			 ttkReportDataSource = new TTKReportDataSource(strReportID,request.getParameter("parameterValue"));
			 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			 hashMap.put("Start Date",request.getParameter("startDate"));
			 hashMap.put("End Date",request.getParameter("endDate"));
			 hashMap.put("parameter",request.getParameter("parameterValue"));
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();

			 if(ttkReportDataSource.getResultData().next())
			 {
				 ttkReportDataSource.getResultData().beforeFirst();
				 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);
				 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
			 }//end of if(ttkReportDataSource.getResultData().next())
			 
			 if(strReportID.equals("FinDetEFTRpt")){
			 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
			 {
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
			 }//end of if(request.getParameter("reportType").equals("EXL"))
			 else//if report type if PDF
			 {
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
			 }//end of else
				 
		 }
		 if(strReportID.equals("FinDetRpt")){
			 
			 if(request.getParameter("reportType").equals("EXL")){
				 TTKReportDAOImpl daoImpl = new TTKReportDAOImpl();
					
					ArrayList<String[]> list =  daoImpl.getClaimsReportForXls(strReportID, request.getParameter("parameterValue"));
					
				    String value[] = new String[list.size()];
				    
				 if(!list.isEmpty()) {  
		     	 value = list.get(0);
				 for(int j=0; j<value.length; j++){
				    	value[0] = value[0];
				    	value[1] = value[1];
				    	value[2] = value[2];
				    	value[3] = value[3];
				    	value[4] = value[4];
				    	value[5]=  value[5];
				    	value[6]=  value[6];
				    }
				
				//	list.add(value);
					request.setAttribute("listofclaimpendingreport", list);
					request.setAttribute("COUNTRYNAME", value[6]);
					
					request.setAttribute("OFFICECODE", value[1]);
					request.setAttribute("ABBRIVIATIONCODE", value[2]);
					request.setAttribute("STARTDATE", request.getParameter("startDate"));
					request.setAttribute("ENDDATE", request.getParameter("endDate"));
					request.setAttribute("OFFICENAME", value[5]);
					request.setAttribute("COMPCODE", value[0]); 
				 }	 
		      }else{
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
		      }
		 }
			 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
			 request.setAttribute("boas",boas);
			 log.debug("report generated sucessfully ");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)	
		 
		 if(request.getParameter("reportType").equals("EXL") && strReportID.equals("FinDetRpt")){
			 return  mapping.findForward(strReportdisplayforClaimPaid); 
		 }else{
		 return (mapping.findForward(strReportdisplay));
		 }
	 }//end of doGenerateClaimsDetailsReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateFinanceBatchReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateFinanceBatchReport method of ReportsAction");
			 setLinks(request);
			 
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile, strReportID = "" ;
			 
			 String selectRptType = request.getParameter("selectRptType");
			 if(selectRptType.equals("CAC"))
			 {
				 jrxmlfile = "reports/finance/AccentureClaimsPendingReport.jrxml";
				 strReportID = "FinPenRpt";
			 }//end of if(selectRptType.equals("CAC"))
			 else
			 {
				 jrxmlfile = request.getParameter("fileName");
				 strReportID = request.getParameter("reportID");
			 }//end of else

			 ttkReportDataSource = new TTKReportDataSource(strReportID,request.getParameter("parameter"));
			 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			 hashMap.put("Start Date",request.getParameter("startDate"));
			 hashMap.put("End Date",request.getParameter("endDate"));
			 hashMap.put("parameter",request.getParameter("parameter"));
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();

			 if(ttkReportDataSource.getResultData().next())
			 {
				 ttkReportDataSource.getResultData().beforeFirst();
				 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);				 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
			 }//end of if(ttkReportDataSource.getResultData().next())
			 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
			 {
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
			 }//end of if(request.getParameter("reportType").equals("EXL"))
			 else//if report type if PDF
			 {
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
			 }//end of else
			 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
			 request.setAttribute("boas",boas);
			 log.debug("report generated sucessfully ");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)		
		 return (mapping.findForward(strReportdisplay));
	 }//end of doGenerateFinanceBatchReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to set selectRptType, report name to the form.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doSelectType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
       try
		  {
			log.debug("Inside the doSelectType method of ReportsAction");
			setLinks(request);
			String selectRptType="";
			DynaActionForm frmReportList= (DynaActionForm) form;
			selectRptType = request.getParameter("selectRptType");
			
			((DynaActionForm)form).initialize(mapping);		//reset the form data
			frmReportList.set("selectRptType",selectRptType); 
			frmReportList.set("reportName","Finance Batch Report");
			return this.getForward(strFinBatchReportList, mapping, request);
		  }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }//end of doSelectType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) 

	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateClaimsPendingReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		 String reportID ="";
		 try{
			 log.debug("Inside the doGenerateClaimsPendingReport method of ReportsAction");
			 setLinks(request);

			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile = "";
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 String selectRptType = request.getParameter("selectRptType");
			 String parameterValue = request.getParameter("parameterValue");
			 reportID	=	request.getParameter("reportID");
			 if(selectRptType.equals("CAC"))
			 {
				 jrxmlfile = "reports/finance/AccentureClaimsPendingReport.jrxml";
				 ttkReportDataSource = new TTKReportDataSource("FinClmPenEFTRpt",request.getParameter("parameterValue"));
			 }//end of if(selectRptType.equals("CAC"))
			 else
			 {
				 if("FinClmSetldRpt".equals(reportID)){
					 jrxmlfile = "reports/finance/ClaimsSettledReport.jrxml";
					 ttkReportDataSource = new TTKReportDataSource("FinClmSetldRpt",request.getParameter("parameterValue"));
				 }else  if("FinClmInpRpt".equals(reportID)){ 
					 jrxmlfile = "reports/finance/ClaimsINPReport.jrxml";
					 ttkReportDataSource = new TTKReportDataSource("FinClmInpRpt",request.getParameter("parameterValue"));
				 }else if("FinClmOutRpt".equals(reportID)){
					 jrxmlfile = "reports/finance/ClaimsOutStdngReport.jrxml";
					 ttkReportDataSource = new TTKReportDataSource("FinClmOutRpt",request.getParameter("parameterValue"));
				 }else if("RoutineRpt".equals(reportID) && !request.getParameter("reportType").equals("EXL")){									
					 jrxmlfile = "reports/finance/RoutineReport.jrxml";
					 ttkReportDataSource = new TTKReportDataSource("RoutineRpt",request.getParameter("parameterValue"));
				 }
				 else if(!"RoutineRpt".equals(reportID)){
					 jrxmlfile = "reports/finance/ClaimsPending.jrxml";
					 ttkReportDataSource = new TTKReportDataSource("FinPenRpt",request.getParameter("parameterValue"));
				 }
			 }//end of else
			 
			 if(!"RoutineRpt".equals(reportID) || request.getParameter("reportType").equals("PDF")){
			 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap<String,String> hashMap = new HashMap<String,String>();
			 
			 String[] parameters = parameterValue.split("\\|");
			 
			 hashMap.put("Start Date",parameters[3]);
 			 hashMap.put("End Date",parameters[4]);
			 if(ttkReportDataSource.getResultData().next())
			 {
				 ttkReportDataSource.getResultData().beforeFirst();
				 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);				 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
			 }//end of if(ttkReportDataSource.getResultData().next())
			 
			 if(reportID.equals("FinClmSetldRpt") || reportID.equals("FinClmInpRpt") || reportID.equals("FinClmOutRpt")|| (reportID.equals("RoutineRpt") && request.getParameter("reportType").equals("PDF"))){
			 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
			 {
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
			 }//end of if(request.getParameter("reportType").equals("EXL"))
			 else//if report type if PDF
			 {
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
			 }//end of else
		 }
			 
			 
		if(reportID.equals("FinPenRpt")){
			
			if(request.getParameter("reportType").equals("EXL")){
				TTKReportDAOImpl daoImpl = new TTKReportDAOImpl();
				
				ArrayList<String[]> list =  daoImpl.getClaimsReportForXls(reportID, parameterValue);
				
			    String value[] = new String[list.size()];
			    
			   /* HSSFWorkbook workbook = new HSSFWorkbook();
			     workbook = createClaimPaindingData(workbook,list);
			     
			     response.setHeader("Content-Disposition", "attachement; filename = claimPendingData.xls");
			     ServletOutputStream out = response.getOutputStream();
			     workbook.write(out);
			     out.flush();
			     out.close();*/
			 if(!list.isEmpty()) {  
	     	 value = list.get(0);
			 for(int j=0; j<value.length; j++){
			    	value[0] = value[0];
			    	value[1] = value[1];
			    	value[2] = value[2];
			    	value[3] = value[3];
			    	value[4] = value[4];
			    	value[5]=  value[5];
			    	value[6]=  value[6];
			    }
			 	
				
				
			//	list.add(value);
				request.setAttribute("listofclaimpendingreport", TTKCommon.checkNull(list));
				
				request.setAttribute("summaryReport", TTKCommon.checkNull(list));
				
				
				request.setAttribute("COUNTRYNAME", value[6]);
				
				request.setAttribute("OFFICECODE", value[1]);
				request.setAttribute("ABBRIVIATIONCODE", value[2]);
				request.setAttribute("STARTDATE", parameters[3]);
				request.setAttribute("ENDDATE", parameters[4]);
				request.setAttribute("OFFICENAME", value[5]);
				request.setAttribute("COMPCODE", value[0]); 
					
			 }	
		           }else{
				
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
		           }
			    }
			 }
		if(reportID.equals("RoutineRpt") && request.getParameter("reportType").equals("EXL")){
			TTKReportManager tTKReportManager	=	this.getTTKReportManager();
			 ArrayList<String[]>  financeOutData = tTKReportManager.getFinanceReport("RoutineRpt",request.getParameter("parameterValue"));
			 System.out.println("financeOutData size is::"+financeOutData.size());
			 if(financeOutData!=null&&financeOutData.size()>0){
				 HSSFWorkbook hssfWorkbook=createFinanceReport(financeOutData,reportID);
				 boas = new ByteArrayOutputStream();
				 hssfWorkbook.write(boas);
				 boas.close();
			 	 hssfWorkbook=null;
			 	 request.setAttribute("boas",boas);
			 	 request.setAttribute("reportID","RoutineRpt");
			 	 response.setContentLength(boas.toByteArray().length);
			 	 response.addHeader("Content-Disposition","attachment; filename=financeRoutineReport.xls");
			 	 log.info("Done.........");
			 }else{
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String,String> hashMap = new HashMap<String,String>();
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
				 request.setAttribute("boas",boas);
				 response.setContentLength(boas.toByteArray().length);
				 request.setAttribute("reportID","RoutineRpt");
			 	 response.addHeader("Content-Disposition","attachment; filename=financeRoutineReport.xls");
			 }
		}
			 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
			request.setAttribute("boas",boas);
			 log.debug("report generated sucessfully ");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)	
		 if(request.getParameter("reportType").equals("EXL") && reportID.equals("FinPenRpt")){
			 return  mapping.findForward("printxlsforClaimsPendingReport");
		 }else{
		 return (mapping.findForward(strReportdisplay));
		 }
	 }//end of doGenerateClaimsPendingReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateMedicalOpinionReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateMedicalOpinionReport method of ReportsAction");
			 setLinks(request);
			 String strPath = "";
			 JasperReport jasperReport,emptyReport,jasperReportSub;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null,ttkReportDataSourceSub = null;
			 String strParam = request.getParameter("parameter");
			 String jrxmlfile=request.getParameter("fileName");
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),2);
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 jasperReportSub = JasperCompileManager.compileReport("generalreports/MedicalOpinionSub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 ttkReportDataSourceSub = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),3);
				 hashMap.put("Start Date",request.getParameter("startDate"));
				 hashMap.put("End Date",request.getParameter("endDate"));
				 hashMap.put("parameter",strParam);
				 hashMap.put("MyDataSource",ttkReportDataSourceSub);
				 hashMap.put("MedicalOpinionSub",jasperReportSub);
				 strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
				 hashMap.put("SigPath",strPath);
				 
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
				 }//end of if(ttkReportDataSource.getResultData().next())
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				//keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateMedicalOpinionReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doDebitNoteReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception{
		 setLinks(request);
		 String strReportType = request.getParameter("reportTypeID");
		 String strJrxmlfile = "";
		 String strReportID = "";
		 if(strReportType.equals("FinPenRpt"))
		 {
			 strJrxmlfile ="reports/finance/ClaimsPending.jrxml";
			 strReportID = "FinPenRpt";
		 }//end of if(strReportType.equals("FinPenRpt"))
		  // Changes added for Debit Note CR KOC1163
		 else if(strReportType.equals("IbmDakshRpt"))
		 {
			 strJrxmlfile ="reports/finance/IBMDakshReport.jrxml";
			 strReportID = "IbmDakshRpt";
		 }
		 // End changes added for Debit Note CR KOC1163
		 else
		 {
			 strJrxmlfile ="reports/finance/DebitPending.jrxml";
			 strReportID="76ColPend";
		 }//end of else
		 JasperReport jasperReport,emptyReport;
		 JasperPrint jasperPrint;
		 TTKReportDataSource ttkReportDataSource = null;
		 String strParam = request.getParameter("parameter");
		 ttkReportDataSource = new TTKReportDataSource(strReportID,strParam);
		 try
		 {
			 jasperReport = JasperCompileManager.compileReport(strJrxmlfile);
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap hashMap = new HashMap();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();

			 if(ttkReportDataSource.getResultData().next())
			 {
				 ttkReportDataSource.getResultData().beforeFirst();
				 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);				 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
			 }//end of if(ttkReportDataSource.getResultData().next())
			 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
			 {
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
			 }//end of if(request.getParameter("reportType").equals("EXL"))
			 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
			 request.setAttribute("boas",boas);
			 log.debug("report generated sucessfully ");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)		

		 return (mapping.findForward(strReportdisplay));
	 }//ene of doDebitNoteReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   //HttpServletResponse response) throws Exception{

	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGeneratePolicyHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGeneratePolicyHistory method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport,jasperSubReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String subReportParam = request.getParameter("parameter").substring(2, request.getParameter("parameter").length()-1);
			 // String strParam = request.getParameter("parameter").replaceAll("_","&");
			 String jrxmlfile=request.getParameter("fileName");
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));			 
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 jasperSubReport  = JasperCompileManager.compileReport("generalreports/PolicyHistorySub.jrxml");	
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("Start Date",request.getParameter("startDate"));
				 hashMap.put("End Date",request.getParameter("endDate"));
				 hashMap.put("MyDataSource",new TTKReportDataSource("PolicyHistorySub",subReportParam));
				 hashMap.put("PoilcyHistorySub", jasperSubReport);
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of if(ttkReportDataSource.getResultData().next())
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGeneratePolicyHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateClosureReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateClosureReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport = null ,emptyReport = null;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSourceMain = null;
			 String jrxmlfile=request.getParameter("fileName");
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap hashMap = new HashMap();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 ttkReportDataSourceMain = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
				 // ttkReportDataSourceSub = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
				 if(ttkReportDataSourceMain.getResultData().next())
				 {
					 ttkReportDataSourceMain.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSourceMain);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
				 }//end of if(ttkReportDataSource.getResultData().next())
				 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
				 {
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else//if report type if PDF
				 {
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
				 }//end of else
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateClosureReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	//added for Checking CIGNA-SMS
	 //public byte[] generateReport(JasperPrint jasperPrint1, JasperPrint jasperPrint2) throws JRException {
	 public ByteArrayOutputStream generateReport(JasperPrint jasperPrint1, JasperPrint jasperPrint2) throws JRException {
		  //throw the JasperPrint Objects in a list
		  java.util.List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		  jasperPrintList.add(jasperPrint1);
		  jasperPrintList.add(jasperPrint2);


		  ByteArrayOutputStream baos = new ByteArrayOutputStream();     
		  JRPdfExporter exporter = new JRPdfExporter();     
		  //Add the list as a Parameter
		  exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		  //this will make a bookmark in the exported PDF for each of the reports
		  exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
		  exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);       
		  exporter.exportReport();      
		  //return baos.toByteArray();
		  return baos;
		}
	 
	 
	 public String generateReport1(JasperPrint jasperPrint1, JasperPrint jasperPrint2,String strPdfFile) throws JRException {
		  //throw the JasperPrint Objects in a list
		  java.util.List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		  jasperPrintList.add(jasperPrint1);
		  jasperPrintList.add(jasperPrint2);

		  

		  ByteArrayOutputStream baos = new ByteArrayOutputStream();     
		  JRPdfExporter exporter = new JRPdfExporter();     
		  //Add the list as a Parameter
		  exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		  //this will make a bookmark in the exported PDF for each of the reports
		//  exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
		  try {
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream(new File(strPdfFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  //exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);       
		  exporter.exportReport();      
		  //return baos.toByteArray();
		  return strPdfFile;
		}
	 
	 //added for Cigna Mail-SMS 
	// doGenerateCignaClosureReport
	 
	 public ActionForward doGenerateCignaClosureReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCignaClosureReport method of ReportsAction");
			 JasperReport jasperReportSub;
			 JasperReport jasperReport = null ,emptyReport = null;
			 JasperReport jasperReport2 = null;
			 TTKReportDataSource ttkReportDataSource = null;
			 TTKReportDataSource ttkReportDataSource2 = null;
			 TTKReportDataSource ttkReportDataSourceSub = null;	
			 JasperPrint jasperPrint; JasperPrint jasperPrint2;
			 String jrxmlfile=request.getParameter("fileName");
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+request.getParameter("authorizationNo")+".pdf";
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),2);
			 ttkReportDataSource2 = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),2);
			 
			 
			try{
				
				
			 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
			 jasperReport2 = JasperCompileManager.compileReport("generalreports/ClaimClosureLtrPO.jrxml");
			 
			 jasperReportSub = JasperCompileManager.compileReport("generalreports/ClaimClosureEmailPOSub.jrxml");
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			
			 
			 ttkReportDataSourceSub = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),3);
			 
			 
			 hashMap.put("parameter",request.getParameter("parameter"));
			 hashMap.put("MyDataSource",ttkReportDataSourceSub);
			 hashMap.put("ClaimClosureEmailPOSub",jasperReportSub);
			// strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
			/// hashMap.put("SigPath",strPath);
			 
			 if(ttkReportDataSource.getResultData().next())
			 {
				 ttkReportDataSource.getResultData().beforeFirst();
				 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
			 }//end of if(ttkReportDataSource.getResultData().next())
			 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
			//keep the byte array out stream in request scope to write in the BinaryStreamServlet
			 
			 if(ttkReportDataSource2.getResultData().next())
			 {
				 ttkReportDataSource2.getResultData().beforeFirst();
				 jasperPrint2 = JasperFillManager.fillReport( jasperReport2, hashMap,ttkReportDataSource2);					 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 jasperPrint2 = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
			 }//end of if(ttkReportDataSource.getResultData().next())
			 
			 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
			 JasperExportManager.exportReportToPdfStream(jasperPrint2,boas);			 
			 boas = generateReport(jasperPrint,jasperPrint2);
			 request.setAttribute("boas",boas);
			 
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateCignaClosureReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 
	 
	 //doGenerateCignaRejectReport - Authorization screen
	 public ActionForward doGenerateCignaRejectReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCignaRejectReport method of ReportsAction");
			 JasperReport jasperReportSub;
			 JasperReport jasperReport = null ,emptyReport = null;
			 TTKReportDataSource ttkReportDataSource = null;
			 TTKReportDataSource ttkReportDataSourceSub = null;	
			 JasperPrint jasperPrint;
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+request.getParameter("authorizationNo")+".pdf";
			 String jrxmlfile=request.getParameter("fileName");
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),2);
			 
			 
			try{
				
				
			 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
			 
			 
			 jasperReportSub = JasperCompileManager.compileReport("generalreports/ClaimRejectionEmailPOSub.jrxml");
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 ttkReportDataSourceSub = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),3);
			 
			 
			 hashMap.put("parameter",request.getParameter("parameter"));
			 hashMap.put("MyDataSource",ttkReportDataSourceSub);
			 hashMap.put("ClaimRejectionEmailPOSub",jasperReportSub);
			// strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
			/// hashMap.put("SigPath",strPath);
			 
			 if(ttkReportDataSource.getResultData().next())
			 {
				 ttkReportDataSource.getResultData().beforeFirst();
				 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
			 }//end of if(ttkReportDataSource.getResultData().next())
			 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
			 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
			//keep the byte array out stream in request scope to write in the BinaryStreamServlet
			 request.setAttribute("boas",boas);
			 
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateCignaRejectReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 //doGenerateCignaApprovalReport
	 public ActionForward doGenerateCignaApprovalReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
	 try{
		 log.debug("Inside the doGenerateCignaApprovalReport method of ReportsAction");
		 //JasperReport jasperReportSub;
		 JasperReport jasperReport = null ,emptyReport = null;
		 TTKReportDataSource ttkReportDataSource = null;
		 //TTKReportDataSource ttkReportDataSourceSub = null;	
		 JasperPrint jasperPrint;
		 String jrxmlfile=request.getParameter("fileName");
		 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
		 
		 
		try{
			
			
		 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
		 
		 //jasperReportSub = JasperCompileManager.compileReport("generalreports/ClaimClosureEmailPOSub.jrxml");
		 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		 HashMap<String, Object> hashMap = new HashMap<String, Object>();
		 ByteArrayOutputStream boas=new ByteArrayOutputStream();
		// ttkReportDataSourceSub = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),3);
		 
		 
		 hashMap.put("parameter",request.getParameter("parameter"));
		// hashMap.put("MyDataSource",ttkReportDataSourceSub);
		// hashMap.put("ClaimClosureEmailPOSub",jasperReportSub);
		// strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
		/// hashMap.put("SigPath",strPath);
		 
		 if(ttkReportDataSource.getResultData().next())
		 {
			 ttkReportDataSource.getResultData().beforeFirst();
			 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
		 }//end of if(ttkReportDataSource.getResultData().next()))
		 else
		 {
			 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
		 }//end of if(ttkReportDataSource.getResultData().next())
		 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
		//keep the byte array out stream in request scope to write in the BinaryStreamServlet
		 request.setAttribute("boas",boas);
		 
		 }//end of try
		 catch (JRException e)
		 {
			 e.printStackTrace();
		 }//end of catch(JRException e)
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }//end of catch (Exception e)
		 return (mapping.findForward(strReportdisplay));
	 }//end of try
	 catch(TTKException expTTK)
	 {
		 return this.processExceptions(request, mapping, expTTK);
	 }//end of catch(TTKException expTTK)
	 catch(Exception exp)
	 {
		 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
	 }//end of catch(Exception exp)
 }//end of doGenerateCignaApprovalReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to generate the report in XML format .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateXmlReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateXmlReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, xmljasperReport,xmljasperReport2,diagnosisJasperReport, emptyReport;
			 JasperPrint jasperPrint;
			 String strPath="";
			 String parameter2="";
			 TTKReportDataSource ttkReportDataSource = null;
			 TTKReportDataSource diagnosisTtkReportDataSource= null;
				String diagnosisJrxmlfile = "generalreports/DiagnosisDoc.jrxml";
			 String jrxmlfile=request.getParameter("fileName");
			 //  
			 if("CLM".equals(request.getParameter("authType")))
				 jrxmlfile	=	"generalreports/ClaimShortfallMedDoc.jrxml";
			 
			 //  
			 if("CLM".equals(request.getParameter("authType")))parameter2="|"+request.getParameter("claimSeqID")+"|REQ|CLM|";
			 else if("PAT".equals(request.getParameter("authType")))parameter2="|"+request.getParameter("preAuthSeqID")+"|REQ|PAT|";
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("shortfallrptdir")+request.getParameter("shortfallNo")+".pdf";
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 try{
				 strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 xmljasperReport = JasperCompileManager.compileReport("generalreports/ShortfallQuestions.jrxml");
				 xmljasperReport2 = JasperCompileManager.compileReport("generalreports/InvestigationQuestions.jrxml");
				 diagnosisJasperReport = JasperCompileManager.compileReport(diagnosisJrxmlfile);
				 diagnosisTtkReportDataSource = new TTKReportDataSource("DiagnosisDetails",parameter2); 
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
				 org.w3c.dom.Document document = null;
				
				 if(ttkReportDataSource.getResultData()!=null && ttkReportDataSource.getResultData().next())
				 {
					// String strQuery = ttkReportDataSource.getResultData().getString("questions");
					// ttkReportDataSource.getResultData().getClob("questions");
					Clob clobQuery= ttkReportDataSource.getResultData().getClob("questions");
					
					
					 StringBuffer str = new StringBuffer();
				       String strng;
				                
				      
				     BufferedReader bufferRead = new BufferedReader(clobQuery.getCharacterStream());
				     
				     while ((strng=bufferRead .readLine())!=null)
				     str.append(strng);
				     
				     String strQuery = str.toString();
					
					
				
					
					 SAXReader saxReader2=new SAXReader();
					 org.dom4j.Document document2=saxReader2.read(new StringReader(strQuery));
					
					 List<Node> nodes=document2.selectNodes("shortfall/section/subsection[@name!='OtherQueries']/query");
					 Node otherNode=document2.selectSingleNode("shortfall/section/subsection[@name='OtherQueries']/query");
					String otherQueriesValue=(otherNode==null)?"":otherNode.valueOf("@value");
					//otherQueriesValue=(otherQueriesValue==null||otherQueriesValue.length()<1)?"":"1."+otherQueriesValue;
					otherQueriesValue=(otherQueriesValue==null||otherQueriesValue.length()<1)?"":otherQueriesValue;
					//  
					for(Node node:nodes){	
						if("YES".equalsIgnoreCase(node.valueOf("@received"))){
							node.detach();
						}
					}
					if(otherNode!=null)otherNode.detach();
					strQuery=document2.asXML();
					 if(strQuery != null)
					 {
						 document = JRXmlUtils.parse(new ByteArrayInputStream(strQuery.getBytes()));
						 hashMap.put("MyDataSource",new JRXmlDataSource(document,"//query"));
						 hashMap.put("MyDataSource",new JRXmlDataSource(document,"shortfall/section/subsection[@name='MedicalQueries']/query"));
						 hashMap.put("MyDataSource2",new JRXmlDataSource(document,"shortfall/section/subsection[@name='InvestigationQueries']/query"));
						 JasperFillManager.fillReport(xmljasperReport, hashMap, new JRXmlDataSource(document,"//query"));
						 JasperFillManager.fillReport(xmljasperReport, hashMap, new JRXmlDataSource(document,"shortfall/section/subsection[@name='MedicalQueries']/query"));
						 JasperFillManager.fillReport(xmljasperReport2, hashMap, new JRXmlDataSource(document,"shortfall/section/subsection[@name='InvestigationQueries']/query"));
						 hashMap.put("shortfalltest",xmljasperReport);
						 hashMap.put("shortfalltest2",xmljasperReport2);
						 hashMap.put("diagnosisDataSource",diagnosisTtkReportDataSource);
						 hashMap.put("diagnosis",diagnosisJasperReport);
						 hashMap.put("SigPath",strPath);
						 hashMap.put("otherQueriesValue",otherQueriesValue);
					 }//end of if(strQuery == null || strQuery.trim().length() == 0)
				 }//end of if(ttkReportDataSource.getResultData()!=null && ttkReportDataSource.getResultData().next())
				 String strQuery =ttkReportDataSource.getResultData().getString("questions");
				 if (strQuery == null || strQuery.trim().length() == 0)
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of if (strQuery == null || strQuery.trim().length() == 0)
				 else
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);
				 }//end of else
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateXmlReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

//Added for Mail-SMS Template for Cigna
	 /**
	  * This method is used to generate the report in XML format .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */

public ActionForward doGenerateCignaXmlReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCignaXmlReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, xmljasperReport, emptyReport;
			 JasperReport jasperReport2;
			 JasperReport jasperReportSub;
			 JasperPrint jasperPrint; JasperPrint jasperPrint2;
			 String strPath="";
			 TTKReportDataSource ttkReportDataSource = null;
			 TTKReportDataSource ttkReportDataSource2 = null;
			 TTKReportDataSource ttkReportDataSourceSub = null;
			 String jrxmlfile=request.getParameter("fileName");
			 
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("shortfallrptdir")+request.getParameter("shortfallNo")+".pdf";
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 try
			 {
				 	 //request.setAttribute("boas",boas);//strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 //xmljasperReport = JasperCompileManager.compileReport("generalreports/ShortfallQuestions.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),2);
				 //org.w3c.dom.Document document = null;
				 if(ttkReportDataSource.getResultData().next())
				 {
					
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
				 }//end of if(ttkReportDataSource.getResultData().next())
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 //JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
				 
				 
				//2nd report
				 jasperReport2 = JasperCompileManager.compileReport("generalreports/CignaDocumentShortfallLtrPO.jrxml");
				 //xmljasperReport = JasperCompileManager.compileReport("generalreports/ShortfallQuestions.jrxml");
				 ttkReportDataSource2 = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),2);
				 //org.w3c.dom.Document document = null;
				 
				 jasperReportSub = JasperCompileManager.compileReport("generalreports/CignaDocumentShortAdvSub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 ttkReportDataSourceSub = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"),3);
				 
				 
				 hashMap.put("parameter",request.getParameter("parameter"));
				 hashMap.put("MyDataSource",ttkReportDataSourceSub);
				 hashMap.put("DocumentShortAdvSub",jasperReportSub);
			
				 
				 if(ttkReportDataSource2.getResultData().next())
				 {
					
					 ttkReportDataSource2.getResultData().beforeFirst();
					 jasperPrint2 = JasperFillManager.fillReport( jasperReport2, hashMap,ttkReportDataSource2);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint2 = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
				 }//end of if(ttkReportDataSource.getResultData().next())
				 JasperExportManager.exportReportToPdfStream(jasperPrint2,boas);
				 //JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
			
				 boas = generateReport(jasperPrint,jasperPrint2);
				 String filename = generateReport1(jasperPrint,jasperPrint2,strPdfFile);
				 
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateCignaXmlReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
 
 /**
	  * This method is used to generate the report in XML format .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */



public ActionForward doOnlineShorfallXmlReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
 try{
			 log.debug("Inside the doOnlineShorfallXmlReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, xmljasperReport, emptyReport;
			 JasperReport jasperReportAD;JasperReport jasperReportPO;
			 JasperPrint jasperPrint;JasperPrint jasperPrintAD;JasperPrint jasperPrintPO = null;
			 TTKReportDataSource ttkReportDataSource = null;
			 TTKReportDataSource ttkReportDataSourcePO = null;
			 String shortfalltype=request.getParameter("shorfalltype");
			 String cignayn = request.getParameter("cignayn");
			 //cignayn = "Y";
			 String shortfallTemplateType="";
			 String strParam = "";
			 String strReportID="";
			 String jrxmlfile="";
			 String strParameterValue = request.getParameter("param");

			 if(strParameterValue.contains("|PAT|"))
			 {
				 if(shortfalltype.equals("Medical"))
				 {
					 jrxmlfile="generalreports/ShortfallMedDoc.jrxml";
					 strReportID="ShortfallMid";				 
				 }//end of if(shortfalltype.equals("Medical"))
				 else if(shortfalltype.equals("Insurance"))
				 {
					 jrxmlfile="generalreports/ShortfallInsDoc.jrxml";
					 strReportID="ShortfallIns";
				 }//end of else if(shortfalltype.equals("Insurance"))
				 strParam="|PAT";
			 }//end of if(TTKCommon.getActiveLink(request).equals("Pre-Authorization"))
			 else if(strParameterValue.contains("|CLM|"))
			 {
				 if(shortfalltype.equals("Medical"))
				 {
					 if(cignayn.equals("Y"))
					 {		
						 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
					 }
					 else
					 {
						 jrxmlfile="generalreports/ClaimShortfallMedDoc.jrxml";
					 }					 
				 }//end of if(shortfalltype.equals("Medical"))
				 else if(shortfalltype.equals("Insurance"))
				 {
					 if(cignayn.equals("Y"))
					 {	
						 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
					 }
					 else
					 {
						 jrxmlfile="generalreports/ShortfallDocumentIns.jrxml";
					 }					 
				 }//end of else if(shortfalltype.equals("Insurance"))
				 else if(shortfalltype.equals("Missing Documents"))
				 {
					 if(cignayn.equals("Y"))
					 {	
						 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
					 }
					 else
					 {
						 jrxmlfile="generalreports/ShortfallDocument.jrxml";
					 }
					 
				 }//end of else if(shortfalltype.equals("Missing Documents"))
				else if(shortfalltype.equalsIgnoreCase("Addressing Documents"))
					{
					shortfallTemplateType=request.getParameter("templateType");
						if(shortfallTemplateType.equalsIgnoreCase("NITN"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentNITN.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("DITN"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentDITN.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("NIDS"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentNIDS.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("DISO"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentDISO.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("NIOS"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentNIOS.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("DIOS"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentDIOS.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("NDSO"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentNDSO.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("DIDS"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentDIDS.jrxml";
							}							
						}
						else if(shortfallTemplateType.equalsIgnoreCase("DSCL"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentDSCL.jrxml";
							}							
						}	
						else if(shortfallTemplateType.equalsIgnoreCase("DSCS"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentDSCS.jrxml";
							}							
						}	
						else if(shortfallTemplateType.equalsIgnoreCase("DSFO"))
						{
							if(cignayn.equals("Y"))
							 {	
								 jrxmlfile="generalreports/CignaDocumentShortfallLtrAdvisor.jrxml";
							 }
							else
							{
								jrxmlfile="generalreports/ShortfallDocumentDSFO.jrxml";
							}							
						}	
					} 
				 if(shortfalltype.equalsIgnoreCase("Addressing Documents"))
				 {
					 if(cignayn.equals("Y"))
					 {
						 strReportID="CignaClaimShortfall";
					 }
					 else
					 {
						 strReportID="AddressingShortfall";
					 }					 
				 }
				 else{
					 if(cignayn.equals("Y"))
					 {
						 strReportID="CignaClaimShortfall";
					 }
					 else
					 {
						 strReportID="Shortfall";
					 }					 
				 }
				
				 strParam="|CLM";
			 }//end of else if(TTKCommon.getActiveLink(request).equals("Claims"))
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 try
			 {
				 if(cignayn.equals("Y"))
				 {
					 
					 xmljasperReport = JasperCompileManager.compileReport("generalreports/ShortfallQuestions.jrxml");
					 //JasperReport jasperReportSub = JasperCompileManager.compileReport("generalreports/CignaDocumentShortAdvSub.jrxml");
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
					 hashMap.put("param", strParam);
					 ByteArrayOutputStream boas=new ByteArrayOutputStream();
					 ttkReportDataSource = new TTKReportDataSource(strReportID,request.getParameter("parameter"));
					 ttkReportDataSourcePO = new TTKReportDataSource(strReportID,request.getParameter("parameter"));
					 org.w3c.dom.Document document = null;
					 if(ttkReportDataSource.getResultData()!=null && ttkReportDataSource.getResultData().next())
					 {
						 String strQuery = ttkReportDataSource.getResultData().getString("questions");
						 if(strQuery != null)
						 {
							 document = JRXmlUtils.parse(new ByteArrayInputStream(strQuery.getBytes()));
							 hashMap.put("MyDataSource",new JRXmlDataSource(document,"//query"));
							 JasperFillManager.fillReport(xmljasperReport, hashMap, new JRXmlDataSource(document,"//query"));
							 hashMap.put("DocumentShortAdvSub",xmljasperReport);
						 }//end of if(strQuery == null || strQuery.trim().length() == 0)
					 }//end of if(ttkReportDataSource.getResultData()!=null && ttkReportDataSource.getResultData().next())
					 String strQuery = ttkReportDataSource.getResultData().getString("questions");
					 if (strQuery == null || strQuery.trim().length() == 0)
					 {
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }//end of if (strQuery == null || strQuery.trim().length() == 0)
					 else
					 {
						 jasperReportAD = JasperCompileManager.compileReport(jrxmlfile);
						 ttkReportDataSource.getResultData().beforeFirst();
						 jasperPrintAD = JasperFillManager.fillReport( jasperReportAD, hashMap, ttkReportDataSource);
						 jasperReportPO = JasperCompileManager.compileReport("generalreports/CignaDocumentShortfallLtrPO.jrxml");
						 ttkReportDataSourcePO.getResultData().beforeFirst();
						 jasperPrintPO = JasperFillManager.fillReport( jasperReportPO, hashMap, ttkReportDataSourcePO);
						 boas = generateReport(jasperPrintAD,jasperPrintPO);
						 request.setAttribute("boas",boas);
					 }					 
				 }
				 else
				 {
					 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
					 xmljasperReport = JasperCompileManager.compileReport("generalreports/ShortfallQuestions.jrxml");
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
					 hashMap.put("param", strParam);
					 ByteArrayOutputStream boas=new ByteArrayOutputStream();
					 ttkReportDataSource = new TTKReportDataSource(strReportID,request.getParameter("parameter"));
					 org.w3c.dom.Document document = null;
					 if(ttkReportDataSource.getResultData()!=null && ttkReportDataSource.getResultData().next())
					 {
						 String strQuery = ttkReportDataSource.getResultData().getString("questions");
						 if(strQuery != null)
						 {
							 document = JRXmlUtils.parse(new ByteArrayInputStream(strQuery.getBytes()));
							 hashMap.put("MyDataSource",new JRXmlDataSource(document,"//query"));
							 JasperFillManager.fillReport(xmljasperReport, hashMap, new JRXmlDataSource(document,"//query"));
							 hashMap.put("shortfalltest",xmljasperReport);
						 }//end of if(strQuery == null || strQuery.trim().length() == 0)
					 }//end of if(ttkReportDataSource.getResultData()!=null && ttkReportDataSource.getResultData().next())
					 String strQuery = ttkReportDataSource.getResultData().getString("questions");
					 if (strQuery == null || strQuery.trim().length() == 0)
					 {
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }//end of if (strQuery == null || strQuery.trim().length() == 0)
					 else
					 {
						 ttkReportDataSource.getResultData().beforeFirst();
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);
					 }//end of else
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
					 request.setAttribute("boas",boas);
				 }//end of else
			}//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doOnlineShorfallXmlReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 	 /**
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateFinReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateFinReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, jasperSubReport, xmljasperReport, emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile=request.getParameter("fileName");
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 xmljasperReport = JasperCompileManager.compileReport("generalreports/BankAccSub.jrxml");
				 jasperSubReport = JasperCompileManager.compileReport("generalreports/FloatAccSub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("MyDataSource",new TTKReportDataSource("FinBanRecBankTran",request.getParameter("parameter")));
				 hashMap.put("MyDataSourceFloat",new TTKReportDataSource("FinBanRecFloatTran",request.getParameter("parameter")));
				 hashMap.put("BankAccSubreport",xmljasperReport);
				 hashMap.put("FloatAccSubreport",jasperSubReport);
				 hashMap.put("Month",request.getParameter("month"));
				 hashMap.put("Year",request.getParameter("year"));
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);				 
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateFinReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateCompReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCompReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, jasperSubReport, emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile=request.getParameter("fileName");
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+request.getParameter("authorizationNo")+".pdf";	
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 jasperSubReport = JasperCompileManager.compileReport("generalreports/MediClaimSub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("MyDataSource",new TTKReportDataSource("MediClaimComLineItems",request.getParameter("parameter")));
				 hashMap.put("MediClaimSubReport",jasperSubReport);
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateCompReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


	 /**
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateCallViewHis(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCallViewHist method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, jasperSubReport, emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile=request.getParameter("fileName");
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 jasperSubReport = JasperCompileManager.compileReport("generalreports/CallHistorySub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("MyDataSource",new TTKReportDataSource("CallViewHisSub",request.getParameter("parameter")));
				 hashMap.put("CallHistorySubReport",jasperSubReport);
				 if(ttkReportDataSource.getResultData().next())
				 {			
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateCompReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**
	  * This method is used to generate the Endorsement Impact report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateImpactReport(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
		{
		 try{ 
			 log.debug("Inside the doGenerateImpactReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport = null, jasperSubReport1,jasperSubReport2,jasperSubReport3,jasperSubReport4, emptyReport;
			 JasperPrint jasperPrint = null;
			 
			 //TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile=request.getParameter("fileName");
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 TTKReportDataSource ttkReportDataSource = null;
			 ttkReportDataSource = new TTKReportDataSource("EndrImpactReport",request.getParameter("parameter"),"4");
			 if(ttkReportDataSource.isResultSetArrayEmpty())
			 {
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap,new JREmptyDataSource());
			 }//end of if(ttkReportDataSource.isResultSetArrayEmpty())
			 else
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 jasperSubReport1 = JasperCompileManager.compileReport("reports/enrollment/EnrollmentSubReport.jrxml");
				 jasperSubReport2 = JasperCompileManager.compileReport("reports/enrollment/PreAuthSubReport.jrxml");
				 jasperSubReport3 = JasperCompileManager.compileReport("reports/enrollment/ClaimsSubReport.jrxml");
				 jasperSubReport4 = JasperCompileManager.compileReport("reports/enrollment/FinanceSubReport.jrxml");
				 
				 hashMap.put("EnrollmentDataSource",new JRResultSetDataSource(ttkReportDataSource.getResultData(0)));
				 hashMap.put("PreAuthDataSource",new JRResultSetDataSource(ttkReportDataSource.getResultData(1)));
				 hashMap.put("ClaimsDataSource",new JRResultSetDataSource(ttkReportDataSource.getResultData(2)));
				 hashMap.put("FinanceDataSource",new JRResultSetDataSource(ttkReportDataSource.getResultData(3)));
				 
				 hashMap.put("EnrollmentCompileSource",jasperSubReport1);
				 hashMap.put("PreAuthCompileSource",jasperSubReport2);
				 hashMap.put("ClaimsCompileSource",jasperSubReport3);
				 hashMap.put("FinanceCompileSource",jasperSubReport4);
				 
				 hashMap.put("Start Date",request.getParameter("startDate"));
				 hashMap.put("End Date",request.getParameter("endDate"));
				 jasperPrint = JasperFillManager.fillReport(jasperReport,hashMap,new JREmptyDataSource());
			 }//end of else			
			 try{
				 
				 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
				 {
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else//if report type if PDF
				 {
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
				 }//end of else
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));	 
		 }//end of try
			catch(TTKException expTTK)
			{	
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			}//end of catch(Exception exp)
			
		}//end of method doGenerateImpactReport(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	 
	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateAdminXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 
			 setLinks(request);
			 int iNo = 3;
			 JasperReport objJasperReport = null;
			 JasperPrint objJasperPrint[] = new JasperPrint[iNo];
			 ArrayList<Object> alJasperPrintList = new ArrayList<Object>();
			 String strParams[] = new String[] {"|"+request.getParameter("parameter")+"|1|40000|","|"+request.getParameter("parameter")+"|40001|80000|","|"+request.getParameter("parameter")+"|80001|120000|"					 
			 };
			 String jrxmlfile=request.getParameter("fileName");
			 String strSheetNames[] = new String[] {
						"GenerateXL1",
						"GenerateXL2",
						"GenerateXL3"};
			 
				 /*objJasperReport = JasperCompileManager.compileReport(jrxmlfile);							 
				 HashMap hashMap = new HashMap();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 for(int i=0; i<iNo; i++){
					 objJasperPrint[i] = JasperFillManager.fillReport(objJasperReport,hashMap,new TTKReportDataSource(request.getParameter("reportID"),strParams[i]));
					 alJasperPrintList.add(objJasperPrint[i]);
				 }//end of for(int i=0; i<iNo; i++)
*/				
				 	JasperReport emptyReport = null;
				 	JasperPrint jasperPrint;
				 	TTKReportDataSource ttkReportsDataSource = null;

				 	ttkReportsDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParams[0]);
				 	try
				 	{
				 		objJasperReport = JasperCompileManager.compileReport(jrxmlfile);							 
				 		HashMap<String,String> hashMap = new HashMap<String, String>();
				 		ByteArrayOutputStream boas=new ByteArrayOutputStream();
				if(!(ttkReportsDataSource.getResultData().next()))
			    {
					emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap,new JREmptyDataSource());
					alJasperPrintList.add(jasperPrint);
			    }//end of if(!(ttkReportsDataSource.getResultData().next()))
				else
				{
					for(int i=0; i<iNo; i++)
					{
						objJasperPrint[i] = JasperFillManager.fillReport(objJasperReport,hashMap,new TTKReportDataSource(request.getParameter("reportID"),strParams[i]));
						alJasperPrintList.add(objJasperPrint[i]);
					}//end of for
				}// end of else
				 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
				 {
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,alJasperPrintList);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "c:\\hay.xls");
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
				 }//end of if(request.getParameter("reportType").equals("EXL"))				
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateAdminXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to generate the report.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doGenerateReportMultiple(ActionMapping mapping,ActionForm form,
			    HttpServletRequest request,HttpServletResponse response)
	throws Exception{
		
		try{
			log.debug("Inside the doGenerateReportMultiple method of ReportsAction");
			setLinks(request);
			JasperReport jasperReport = null;
			JasperPrint  jasperPrint  = null;
			TTKReportManager tTKReportManager = null;
			try{
				ByteArrayOutputStream boas = new ByteArrayOutputStream();
				ResultSet rs = null;
				HashMap hm = new HashMap();
				String strParameter = "|"+request.getParameter("parameter")+"|1|300000|";
				String strReportID = request.getParameter("reportID");
				String jrxmlfile=request.getParameter("fileName");
				int maxCount = 60000;
				//int maxCount = 6;
				tTKReportManager = this.getTTKReportManager();
				rs = tTKReportManager.getReport(strReportID,strParameter);
				ArrayList<Object> alJasperPrint = new ArrayList<Object>();    
				jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				Boolean bCheck = true;
				//String strSheets[] = new String[]{"done1","done2","done3"};
				int count=0;
				for(int i=1;bCheck;i=i+maxCount,rs.absolute(i-1),count++){
					TTKReportDataSourceMultiple obj = new TTKReportDataSourceMultiple(rs,i);
					jasperPrint = JasperFillManager.fillReport(jasperReport,hm,obj);
					bCheck = obj.getIsNextSheet();
					alJasperPrint.add(jasperPrint);
				} //end of for(int i=1;bCheck;i=i+15,rs.absolute(i-1))
				String strSheets[] = new String[count];
				for(int i=0;i<count;i++){
					strSheets[i]="RenewalMembers"+i;
				}//end of for(int i=0;i<count;i++)
				JRXlsExporter jrxlsexporter1 = new JRXlsExporter();
				jrxlsexporter1.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,alJasperPrint);   
				jrxlsexporter1.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,boas);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.SHEET_NAMES,strSheets);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				jrxlsexporter1.exportReport();
				request.setAttribute("boas",boas);			
			}//end of try
			catch (JRException e)
			{
				e.printStackTrace();
			}//end of catch(JRException e)
			catch (Exception e)
			{
				e.printStackTrace();
			}//end of catch (Exception e)
			return (mapping.findForward(strReportdisplay));
		}//end of try
		catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	}//end of doGenerateReportMultiple(ActionMapping mapping,ActionForm form,
		//HttpServletRequest request,HttpServletResponse response)
	 
	/**
	  * This method is used to generate the TPACommissionRpt report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateTPACommissionRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
			try{
				log.debug("Inside doGenerateTPACommissionRpt of ReportsAction");
			
				setLinks(request);
				int iNoOfCursor = 3;//12
				JasperReport objJasperReport[] =new JasperReport[iNoOfCursor];
				 JasperReport jasperReportMem = null;
					JasperReport jasperReportMemBro=null;
					JasperReport jasperReportBroXL=null;
					
				JasperReport emptyReport;
				JasperPrint objJasperPrint[] = new JasperPrint[iNoOfCursor];
				JasperPrint jasperPrint;
				JasperPrint jasperBrokerPrint=null;
				JasperPrint jasperPrintMem = null;
				JasperPrint jasperPrintMemBro=null;
				JasperPrint jasperPrintBroXL=null;
				
				ArrayList<Object> alJasperPrintList = new ArrayList<Object>();
				ArrayList<Object> alJasperPrintListBro = new ArrayList<Object>();
				ByteArrayOutputStream boas=new ByteArrayOutputStream();
				String strTPARpt = "",strTPARptpdf = "";
				String strTPARptBro="";
				String paymentType = request.getParameter("paymentType");
				List<InputStream> fileData = new ArrayList<InputStream>();
				List<InputStream> fileData2 = new ArrayList<InputStream>();
				 JasperReport jasperReport,jasperSubReport;
				 JasperReport jasperBrokerReport;
				 TTKReportDataSource ttkReportDataSourcepdf = null , ttkSubReportDataSourcepdf = null;
				 TTKReportDataSource ttkBrokerDataSourcepdf=null;
				 Long lnAddedBy = TTKCommon.getUserSeqId(request);
				 String strInvoiceSeqID = request.getParameter("seqID");
				 String strPolicySeqID = request.getParameter("policySeqID");
				 String strParam = "|"+strInvoiceSeqID+"|"+strPolicySeqID+"|";
				 
				 //String strParam = strPolicySeqID;//before strInvoiceSeqID
				
				 try{
				 String strReportID ="QuotationInvoice";	
				 String strSubReportID = "QuotationInvoicesub";
				 String jrxmlfile="reports/quotation/Invoice_Generate.jrxml";
				// String brokerjrxmlFile="reports/quotation/Invoice_Generate.jrxml";
					
				 String brokerjrxmlFile="reports/quotation/broker_invoice.jrxml";
				 
					 
				 String subjrxmlfile = "reports/quotation/SubInvoiceReport.jrxml";
				 ttkReportDataSourcepdf = new TTKReportDataSource(strReportID,strParam);
				 ttkSubReportDataSourcepdf = new TTKReportDataSource(strSubReportID,strParam);
				 ttkBrokerDataSourcepdf = new TTKReportDataSource(strReportID,strParam);
					
				 
				 jasperBrokerReport=JasperCompileManager.compileReport(brokerjrxmlFile);
				 
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 jasperSubReport = JasperCompileManager.compileReport(subjrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMappdf = new HashMap<String, Object>();
				 HashMap<String, Object> hashMappdfbroker = new HashMap<String, Object>();
				 hashMappdf.put("MyDataSource",ttkSubReportDataSourcepdf);
				 hashMappdfbroker.put("MyDataSource",ttkSubReportDataSourcepdf);
				 hashMappdf.put("premiumDataSource",jasperSubReport);
				 ByteArrayOutputStream boaspdf=new ByteArrayOutputStream();
				 ByteArrayOutputStream boasbrokerpdf=new ByteArrayOutputStream();
				 
				 ByteArrayOutputStream boasmempdf=new ByteArrayOutputStream();
				 ByteArrayOutputStream boasmembropdf=new ByteArrayOutputStream();
				 String brokerInvoice=null;
				
				 if(ttkReportDataSourcepdf.getResultData().next())
				 {  long number = ttkReportDataSourcepdf.getResultData().getLong("NET_PREMIUM");
					// long number = Long.valueOf(numberstr);
					brokerInvoice=ttkReportDataSourcepdf.getResultData().getString("BRK_INV_NUMBER");
					//System.out.println("BRO ONB:::::::::::"+ttkReportDataSourcepdf.getResultData().getString("BRK_INV_NUMBER"));
					 TTKCommon.convert(number);
					 String convertedword =   TTKCommon.convert(number);
					 hashMappdf.put("NET_PREMIUM_WORD_JAVA",convertedword.toUpperCase());
					 ttkReportDataSourcepdf.getResultData().beforeFirst();
					 jasperBrokerPrint=JasperFillManager.fillReport( jasperBrokerReport, hashMappdfbroker,ttkBrokerDataSourcepdf);
					 
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMappdf,ttkReportDataSourcepdf);					 
				 //new broker invoice
					 
						
					 /*String pdfpath=TTKPropertiesReader.getPropertyValue("Invoicerptdir")
				               +"Broker-BAT-"+strInvoiceSeqID+"-"+".pdf"; 
					
					 JasperExportManager.exportReportToPdfFile(jasperBrokerPrint, pdfpath);*/
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {		
					// System.out.println("no data found ele loop----");
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMappdf,new JREmptyDataSource());					
				 }//end of else
				 
				  java.util.List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
				  jasperPrintList.add(jasperPrint);
				 // jasperPrintList.add(jasperBrokerPrint);
					String strStartDate = request.getParameter("startDate");
					String strEndDate = request.getParameter("endDate");
					String jrxmlfileMem="";
					String jrxmlfileMemBro="reports/finance/brokerXL.jrxml";
					String brokerjrxmlXL="reports/finance/brokerReportXL.jrxml";
		
		
					if("ADD".equals(paymentType)){
						jrxmlfileMem ="reports/finance/TPACommissionRenMemberspdf.jrxml";
						strTPARptpdf =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
					               +"BATCH-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+"-"+strEndDate.replaceAll("/","")+".pdf"; 
						
						/*jrxmlfileMem ="reports/finance/TPACommissionRenMemberspdf.jrxml";
						strTPARptpdf =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
					               +"BATCH-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+"-"+strEndDate.replaceAll("/","")+".pdf"; 
						*/
						
						
						}else if ("DEL".equals(paymentType)){
							jrxmlfileMem ="reports/finance/TPACommissionRenMemberscredtpdf.jrxml";
							strTPARptpdf =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
						               +"BATCH-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+".pdf"; 
							strTPARptBro =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
						               +"BATCH-BCN-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+".pdf"; 

						
						}
					
					
				String jrxmlFiles[] = new String[]
				                                 {
						//"reports/finance/TPACommissionIndEnr.jrxml",
						//"reports/finance/TPACommissionIndEnd.jrxml",
						//"reports/finance/TPACommissionCorEnr.jrxml",  -------->commented in collections cr
						//"reports/finance/TPACommissionCorEnd.jrxml",
						//"reports/finance/TPACommissionNonCorEnr.jrxml",
						//"reports/finance/TPACommissionNonCorEnd.jrxml",
						//"reports/finance/TPACommissionIndGrpEnr.jrxml",
						//"reports/finance/TPACommissionIndGrpEnd.jrxml",
						//"reports/finance/TPACommissionPolStDt.jrxml",
						//"reports/finance/TPACommissionBodo.jrxml",
					//	"reports/finance/TPACommissionSummary.jrxml",
						//"reports/finance/TPACommissionTotSummary.jrxml"	
						"reports/finance/TPACommissionRenMembersXL.jrxml",
						"reports/finance/TPACommissionMemClm.jrxml",
						//"reports/finance/brokerXL.jrxml"
				        
				                                 };
				String strSheetNames[] = new String[]
				                                    {
						//"ind_enr",
						//"ind_end",
						//"Summary", -------->commented in collections cr
					//	"cor_end",
						//"non_cor_enr",
						//"non_cor_end",
						//"ind_g_enr",
						//"ind_g_end",
					//	"pol_st_date",
					//	"bodo",
					//	"summary",
					//	"total_summary"
						"Member_details",
						"Member_Claims"
				                                    
				                                    };
				
				
				String strBrkSheetNames[] = new String[]
                        {
//"ind_enr",
//"ind_end",
//"Summary", -------->commented in collections cr
//	"cor_end",
//"non_cor_enr",
//"non_cor_end",
//"ind_g_enr",
//"ind_g_end",
//	"pol_st_date",
//	"bodo",
//	"summary",
//	"total_summary"
"Broker_details",

                        
                        };

				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				HashMap<String, Object> hashMapbro = new HashMap<String, Object>();
				
				//parameters to pass for the procedure
			//	String strStartDate = request.getParameter("startDate");
			//	String strEndDate = request.getParameter("endDate");
			
				if("ADD".equals(paymentType)){
				strTPARpt =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
			               +"BATCH-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+"-"+strEndDate.replaceAll("/","")+".xls"; 
				
				strTPARptBro =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
			               +"BATCH-BRK-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+"-"+strEndDate.replaceAll("/","")+".xls"; 
				
				}else if ("DEL".equals(paymentType)){
					strTPARpt =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
				               +"BATCH-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+".xls"; 
					strTPARptBro =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
				               +"BATCH-BCN-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+".xls"; 
					
				}
				
				//strTPARptpdf =TTKPropertiesReader.getPropertyValue("Invoicerptdir")+"BATCH-"+strStartDate.replaceAll("/","")+"-"+strEndDate.replaceAll("/","")+".xls"; 
				
				hashMap.put("Start Date",strStartDate);
				hashMap.put("End Date",strEndDate);
				hashMapbro.put("Start Date",strStartDate);
				hashMapbro.put("End Date",strEndDate);
				String strParameter = "";
				
				if("ADD".equals(paymentType)){
					 strParameter = "|"+strInvoiceSeqID+"|"+strPolicySeqID+"|"+strStartDate+"|"+strEndDate+"|"+lnAddedBy+"|";
				}else if("DEL".equals(paymentType)){
					 strParameter = "|"+strInvoiceSeqID+"|"+strPolicySeqID+"|"+strStartDate+"|"+strStartDate+"|"+lnAddedBy+"|";
				}
			
				
				for(int i=0;i<iNoOfCursor-1;i++)
				{
					
					objJasperReport[i] = JasperCompileManager.compileReport(jrxmlFiles[i]);
					if(i==0){
						System.out.println("inside loop 1---"+jrxmlfileMem);
						jasperReportMemBro =JasperCompileManager.compileReport(jrxmlfileMemBro);
						jasperReportMem = JasperCompileManager.compileReport(jrxmlfileMem);
					
					}
				}//end of for(int i=0;i<iNoOfCursor;i++)
				TTKReportDataSource ttkReportDataSource = null;
				ttkReportDataSource = new TTKReportDataSource("TPACommissionRpt",strParameter,(iNoOfCursor+""));
				if(ttkReportDataSource.isResultSetArrayEmpty())
				{
					emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap,new JREmptyDataSource());
					alJasperPrintList.add(jasperPrint);
				}//end of if(ttkReportDataSource.isResultSetArrayEmpty())
				else 
				{
					int ifinalNoOfCursor;
					if("ADD".equals(paymentType)){
						ifinalNoOfCursor = iNoOfCursor;
					}else{
						ifinalNoOfCursor = iNoOfCursor;
					}
					for(int i=0;i<ifinalNoOfCursor;i++){
						//log.debug("i value is :" + i);
						ResultSet rs = ttkReportDataSource.getResultData(i);
						if(rs.next())
						{	
							 if(i==2){
								jasperReportBroXL = JasperCompileManager.compileReport(brokerjrxmlXL);
								
							 ResultSet rsbro = ttkReportDataSource.getResultData(i);	 		
							 rsbro.beforeFirst();
								  	if(rsbro.next()){
								  		rsbro.beforeFirst();
									jasperPrintMemBro = JasperFillManager.fillReport(jasperReportMemBro,hashMapbro,new JRResultSetDataSource(rsbro));//new JREmptyDataSource());
									rs.beforeFirst();
									jasperPrintBroXL=JasperFillManager.fillReport(jasperReportBroXL,hashMapbro,new JRResultSetDataSource(rs));
							
								  	}
								  	else{

										emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
										jasperPrintMemBro = JasperFillManager.fillReport(emptyReport,hashMapbro,new JREmptyDataSource());
								  	}
							 }

						  	rs.beforeFirst();
						  	if(i!=2){
							objJasperPrint[i] = JasperFillManager.fillReport(objJasperReport[i],hashMap,new JRResultSetDataSource(rs));//new JREmptyDataSource());
						  	}
							if(i==0){
								rs.beforeFirst();
								jasperPrintMem = JasperFillManager.fillReport(jasperReportMem,hashMap,new JRResultSetDataSource(rs));//new JREmptyDataSource());
							
										
							}

						}//end of if(rs.next())
						else 
							
						{
							if(i!=2){
							objJasperPrint[i] = JasperFillManager.fillReport(objJasperReport[i],hashMap,new JREmptyDataSource());
							}
														if(i==0){
							emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
							jasperPrintMem = JasperFillManager.fillReport(emptyReport,hashMap,new JREmptyDataSource());
							
							}
						
						}//end of else
						if(i==2) break;
						alJasperPrintList.add(objJasperPrint[i]);
						
					}//end of for(int i=0;i<iNoOfCursor;i++){
				}//end of else
				
				alJasperPrintListBro.add(jasperPrintBroXL);
				
				 if(brokerInvoice!=""&&brokerInvoice!=null){
				JRXlsExporter jrxlsexporter1 = new JRXlsExporter();
				jrxlsexporter1.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,alJasperPrintListBro);			
				jrxlsexporter1.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, strTPARptBro);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.SHEET_NAMES, strBrkSheetNames);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				jrxlsexporter1.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				jrxlsexporter1.exportReport();				
				 }
				log.debug("strTpaRpt is :" + strTPARpt);
				//for eporting to stream
				JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,alJasperPrintList);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "c:\\hay.xls");
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				jExcelApiExporter.exportReport();
				
				//for exporting as a file in directory
				JRXlsExporter jrxlsexporter = new JRXlsExporter();
				jrxlsexporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,alJasperPrintList);			
				jrxlsexporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, strTPARpt);
				jrxlsexporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				jrxlsexporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
				jrxlsexporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				jrxlsexporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				jrxlsexporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				jrxlsexporter.exportReport();				
				
			
				/*JRPdfExporter exporter = new JRPdfExporter();     
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, boaspdf);    
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream(new File(strTPARptpdf)));
			
				exporter.exportReport();*/
				 if(brokerInvoice!=""&&brokerInvoice!=null){
				JasperExportManager.exportReportToPdfStream(jasperBrokerPrint, boasbrokerpdf);
				ByteArrayInputStream is3 = new ByteArrayInputStream(boasbrokerpdf.toByteArray());
				JasperExportManager.exportReportToPdfStream(jasperPrintMemBro, boasmembropdf);
				ByteArrayInputStream is4 = new ByteArrayInputStream(boasmembropdf.toByteArray());
				fileData2.add(is3);
				fileData2.add(is4);
				
				 }
				
				JasperExportManager.exportReportToPdfStream(jasperPrint, boaspdf);
				ByteArrayInputStream is1 = new ByteArrayInputStream(boaspdf.toByteArray());
				
				JasperExportManager.exportReportToPdfStream(jasperPrintMem, boasmempdf);
				ByteArrayInputStream is2 = new ByteArrayInputStream(boasmempdf.toByteArray());
				
				fileData.add(is1);
				fileData.add(is2);
				
			
				
				//fileData2.add(is3);
				String strTPARptpdfpath = "";
				String strTPABrokerRptpdfpath = "";
				
				if("ADD".equals(paymentType)){
					strTPARptpdfpath =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
				               +"BATCH-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+"-"+strEndDate.replaceAll("/","")+".pdf"; 
					strTPABrokerRptpdfpath=TTKPropertiesReader.getPropertyValue("Invoicerptdir")
				               +"BATCH-BRK-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+"-"+strEndDate.replaceAll("/","")+".pdf"; 
					
					}else if ("DEL".equals(paymentType)){
						strTPARptpdfpath =TTKPropertiesReader.getPropertyValue("Invoicerptdir")
					               +"BATCH-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+".pdf"; 
						strTPABrokerRptpdfpath=TTKPropertiesReader.getPropertyValue("Invoicerptdir")
					               +"BATCH-BCN-"+strInvoiceSeqID+"-"+strStartDate.replaceAll("/","")+".pdf"; 
						
					}
				
				 
				
				OutputStream outputStream = new FileOutputStream(strTPARptpdfpath);
				
			//	JasperExportManager.exportReportToPdfFile(jasperPrint, strTPARptpdf);
				 doPdfMergeBytes(request,fileData,outputStream);
				 if(brokerInvoice!=""&&brokerInvoice!=null){
					 OutputStream outputStream2 = new FileOutputStream(strTPABrokerRptpdfpath);
						 
					 doPdfMergeBytes(request,fileData2,outputStream2);
					 }
				} catch (JRException e)
				 {
					 e.printStackTrace();
				 }//end of catch(JRException e)
				 catch (Exception e)
				 {
					 e.printStackTrace();
				 }//end of catch (Exception e)
				request.setAttribute("fileName",strTPARpt);
				//request.setAttribute("boas",boas);
				if("ADD".equals(paymentType)){
				return (mapping.findForward(straddeditinvoice));	
				}else{
				return (mapping.findForward(straddeditcreditnote));	 
				}
				//return (mapping.findForward(strReportdisplay));	 
				
			}catch(TTKException expTTK)
			{
				return this.processExceptions(request , mapping ,expTTK);
			}
			catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			 }//end of catch(Exception exp)
		}//end of doGenerateTPACommissionRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 
	 
	 @SuppressWarnings("static-access")
	public static void doPdfMergeBytes(HttpServletRequest request,List<InputStream> list, OutputStream outputStream){

	        Document document = new Document();
	       try{

	        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
	        writer.open();
	        document.open();
	        PdfContentByte cb = writer.getDirectContent();
	       
	        for (InputStream inputStream : list) {
	        
	            PdfReader reader = new PdfReader(inputStream);
	            int n = reader.getNumberOfPages();
	            for (int i = 1; i <=reader.getNumberOfPages(); i++){
	                document.newPage();
	               
	               // ColumnText.showTextAligned(cb, Element.ALIGN_BOTTOM,new Phrase(String.format("page %s of %s", i, n)), 559, 806, 0);
	                
	                //import the page from source pdf
	                PdfImportedPage page = null;
	              
	                 page = writer.getImportedPage(reader, i);
	                //add the page to the destination pdf
	                cb.addTemplate(page, 0, 0);
	            }//end of for (int i = 1; i <=reader.getNumberOfPages(); i++)
	        }//end of for (InputStream inputStream : list)
	        outputStream.flush();
	       }//end of try
	       catch(Exception exception){
	           log.error("PDF merging some Problem Occured "+exception);;
	       }//end of catch(Exception exception){
	       finally{
	        if(document != null)document.close();
	           try {
	               if(outputStream != null)outputStream.close();
	           }catch (IOException e) {
	               e.printStackTrace();
	           }
	       }//end of finally
	 }
	 
	/**
	  * This method is used to generate Claims Acknowledgement Report
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateClaimAcknowledgement(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
//			 String strActiveLink=TTKCommon.getActiveLink(request);
			 JasperReport jasperReport,emptyReport,jasperSubReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null , ttkSubReportDataSource = null;
			 String strParam = request.getParameter("parameter");
			 String strReportID =request.getParameter("reportID");	
			 String strSubReportID = "ClaimsACKSub";
			 String jrxmlfile=request.getParameter("fileName");
			 String subjrxmlfile = "generalreports/AckCheckListSub.jrxml";
			 ttkReportDataSource = new TTKReportDataSource(strReportID,strParam);
			 ttkSubReportDataSource = new TTKReportDataSource(strSubReportID,strParam);
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 jasperSubReport = JasperCompileManager.compileReport(subjrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 hashMap.put("MyDataSource",ttkSubReportDataSource);
				 hashMap.put("AckCheckListSubReport",jasperSubReport);
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {		
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
				 }//end of else
				 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
				 {
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else//if report type if PDF
				 {
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);					 
					 request.setAttribute("boas",boas);
				 }//end of else
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateClaimAcknowledgement(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 /**
	  * This method is used to generate the TPACommissionRpt report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doTPACommissionRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
			try{
				log.debug("Inside doGenerateTPACommissionRpt of ReportsAction");
				setLinks(request);
				int iNoOfCursor = 6;
				JasperReport objJasperReport[] =new JasperReport[iNoOfCursor];
				JasperReport emptyReport;
				JasperPrint objJasperPrint[] = new JasperPrint[iNoOfCursor];
				JasperPrint jasperPrint;
				ArrayList<Object> alJasperPrintList = new ArrayList<Object>();
				ByteArrayOutputStream boas=new ByteArrayOutputStream();
				
				String jrxmlFiles[] = new String[]
				                                 {
						"reports/finance/TPACommissionIndvlEnr.jrxml",
						"reports/finance/TPACommissionIndvlEnd.jrxml",
						"reports/finance/TPACommissionGroupEnr.jrxml",
						"reports/finance/TPACommissionGroupEnd.jrxml",
						"reports/finance/TPACommissionTotalSum.jrxml",
						"reports/finance/TPACommissionInvoice.jrxml"														
                                                   };
				String strSheetNames[] = new String[]{"ind_enr","ind_end","group_enr","group_end","total_summary","invoice"};
				HashMap hashMap = new HashMap();				
				//parameters to pass for the procedure
				String strParameter = request.getParameter("parameter");
				//String strParameter = "|"+strInvoiceNbr+"|"+strInsuranceSeqID+"|";
				log.debug("Tpa commission report parameters are :"+strParameter);
				for(int i=0;i<iNoOfCursor;i++)
					objJasperReport[i] = JasperCompileManager.compileReport(jrxmlFiles[i]);
				TTKReportDataSource ttkReportDataSource = null;
				ttkReportDataSource = new TTKReportDataSource("FinTPAComm",strParameter,(iNoOfCursor+""));
				if(ttkReportDataSource.isResultSetArrayEmpty())
				{
					emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap,new JREmptyDataSource());
					alJasperPrintList.add(jasperPrint);
				}//end of if(ttkReportDataSource.isResultSetArrayEmpty())
				else 
				{
					for(int i=0;i<iNoOfCursor;i++){
						//log.debug("i value is :" + i);
						ResultSet rs = ttkReportDataSource.getResultData(i);
						if(rs.next())
						{
							rs.beforeFirst();
							objJasperPrint[i] = JasperFillManager.fillReport(objJasperReport[i],hashMap,new JRResultSetDataSource(rs));//new JREmptyDataSource());
						}//end of if(rs.next())
						else 
						{
							objJasperPrint[i] = JasperFillManager.fillReport(objJasperReport[i],hashMap,new JREmptyDataSource());
						}//end of else
						alJasperPrintList.add(objJasperPrint[i]);
					}//end of for(int i=0;i<iNoOfCursor;i++){
				}//end of else
				//for eporting to stream
				JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,alJasperPrintList);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "c:\\hay.xls");
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				jExcelApiExporter.exportReport();				
				request.setAttribute("boas",boas);
				return (mapping.findForward(strReportdisplay));	 
			}catch(TTKException expTTK)
			{
				return this.processExceptions(request , mapping ,expTTK);
			}
			catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			 }//end of catch(Exception exp)
		}//end of doTPACommissionRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 
	 /**
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doReportLink(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doReportLink method of ReportsAction");
			 setLinks(request);
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doReportLink(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to navigate to previous screen when closed button is clicked.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 log.debug("Inside the doClose method of ReportsAction");
		 return doDefault(mapping,form,request,response);
	 }//end of Close(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**
	  * This method is used to navigate to  batchdetails screen when Batch Number Icon is clicked.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doBatchNumber(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
       try
		  {
			 log.debug("Inside the doBatchNumber method of ReportsAction");
			 setLinks(request);
			 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)
				request.getSession().getAttribute("UserSecurityProfile");
			 if(userSecurityProfile.getSecurityProfile().getActiveSubLink().equals("TDS"))
			 {
				 return (mapping.findForward(strBankAccNo));
			 }//end of if(userSecurityProfile.getSecurityProfile().getSubLinks().equals("Finance"))
			 else
			 {
				 return (mapping.findForward(strBatchDetail));
			 }//end of else
		  }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }//end of doBatchNumber(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) 

	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateRejectionReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateRejectionReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport;
			 JasperPrint jasperPrint;
			 String strParam = null;			 
			 TTKReportDataSource ttkReportDataSource = null;
			 ResultSet rs = null;
			 strParam = request.getParameter("parameter");
			 String jrxmlfile= null;		 
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));
			 rs = ttkReportDataSource.getResultData();			 
			 try
			 {
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("Start Date",request.getParameter("startDate"));
				 hashMap.put("End Date",request.getParameter("endDate"));
				 hashMap.put("parameter",strParam.substring(2,strParam.length()));				 
				 if(rs.next()){	
					 String strTemplateName = rs.getString("TEMPLATE_NAME");
					 log.debug("Template Name is " + strTemplateName);
					 if(strTemplateName!=null)
					 {
						 jrxmlfile = "reports/rejectionformat/"+rs.getString("TEMPLATE_NAME")+".jrxml";
					 }//end of if(strTemplateName!=null)
					 rs.previous();
					 File f = new File(jrxmlfile);
					 if(f.exists()){
						 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
						 jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,new TTKReportDataSource(rs));
					 }//end of  if(f.exists())   compiling the template file
					 else {//template does not exists
						 jasperReport = JasperCompileManager.compileReport("generalreports/tna.jrxml");
						 jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,new JREmptyDataSource());
					 }//end of else					 
				 }//if(rs.next())
				 else {
					 jrxmlfile = "generalreports/EmptyReprot.jrxml";
					 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new JREmptyDataSource());
				 }//end of else			 
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateRejectionReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	
	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateWebHospXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateWebHospXL method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, emptyReport;
			 JasperPrint jasperPrint = null;
			 //String strParam =null;
			 String strJrxml ="generalreports/HospComp.jrxml";
			 String strParams = request.getParameter("parameter");
			 String strType = strParams.substring(1,strParams.substring(0,strParams.substring(0,strParams.lastIndexOf("|")).lastIndexOf("|")).lastIndexOf("|"));
			 TTKReportDataSource ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParams);
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(strJrxml);	
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String,String> hashMap = new HashMap<String,String>();
				 hashMap.put("Type",strType);
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport(jasperReport,hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);		
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
				 return (mapping.findForward(strReportdisplay));
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateWebHospXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 /**
	  * This method is used to generate the report.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateFaxStatusReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateReport method of FaxStatusAction");
			 setLinks(request);			 
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 
			 TTKReportDataSource ttkReportDataSource = null;			 			
					 
			 String jrxmlfile="reports/common/FaxStatusReport.jrxml";
			 String strReportID = "GenFaxStatRpt";
			 ArrayList<Object> alFaxParams = new ArrayList<Object>();
			 alFaxParams.add(strReportID);	
			 alFaxParams.add(request.getParameter("generatedBy"));
			 alFaxParams.add(request.getParameter("faxStatus"));
			 alFaxParams.add(request.getParameter("startDate"));
			 alFaxParams.add(request.getParameter("endDate"));
			 alFaxParams.add(TTKCommon.getUserSeqId(request));		
			 log.debug(" Report ID is :"+strReportID);
			 log.debug(" generated by :"+request.getParameter("generatedBy"));
			 log.debug(" faxStatus    :"+request.getParameter("faxStatus"));
			 log.debug(" startDate    :"+request.getParameter("startDate"));
			 log.debug(" endDate      :"+request.getParameter("endDate"));
			 log.debug(" Added by     :"+TTKCommon.getUserSeqId(request));
			 
			 ttkReportDataSource = new TTKReportDataSource(alFaxParams);
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 hashMap.put("Start Date",request.getParameter("startDate"));
				 hashMap.put("End Date",request.getParameter("endDate"));
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {	
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());					
				 }//end of if(ttkReportDataSource.getResultData().next())
				 
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);											 				
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateFaxStatusReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
 //added as per bajaj Change Request ....................................................................................................
	 /**
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 /*
	 public ActionForward doGenerateApproveClaimsReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCompReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, jasperSubReport1,jasperSubReport2,jasperSubReport3, emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile="generalreports/ClaimsApprovalForm.jrxml";
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+"satya_preauthclaimsapprove"+".pdf";	
			 ttkReportDataSource = new TTKReportDataSource("MediClaimCom","|1953850|");
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				
				 jasperSubReport1 = JasperCompileManager.compileReport("generalreports/PolicyHistorySUB1.jrxml");
				 jasperSubReport2 = JasperCompileManager.compileReport("generalreports/ClaimHistorySub.jrxml");
				 jasperSubReport3 = JasperCompileManager.compileReport("generalreports/MediClaimSub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("MyDataSource1",new TTKReportDataSource("policyHistory","441200/2007/1124"));
				 hashMap.put("MediClaimSubReport1",jasperSubReport1);
				
				 
				 hashMap.put("MyDataSource2",new TTKReportDataSource("claimHistory","KOC-NC-0000-00-16930-2"));
				 hashMap.put("MediClaimSubReport2",jasperSubReport2);
				
				 
				 hashMap.put("MyDataSource3",new TTKReportDataSource("MediClaimComLineItems","|1953850|"));
				 hashMap.put("MediClaimSubReport3",jasperSubReport3);
				 
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateCompReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
*/
	 
	  //added as per bajaj Change Request ....................................................................................................
	 /**
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateAppRejClmReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCompReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, jasperSubReport1,jasperSubReport2,jasperSubReport3, emptyReport;
			 JasperPrint jasperPrint;
			 ArrayList alParamList =new ArrayList();
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile="generalreports/ClaimsApprovalForm.jrxml";
			 String strParameter= (String)request.getParameter("parameter");
				String strVidalHealthId= (String)request.getParameter("vidalttkId");
				String strEnrollNumber= (String)request.getParameter("enrollNumber");
				String strPolicyNo= (String)request.getParameter("policyNo");
			//	 

				 alParamList.add(strPolicyNo);
				 alParamList.add(strVidalHealthId);
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+strVidalHealthId+"_"+strParameter+".pdf";	
			 ttkReportDataSource = new TTKReportDataSource("ClaimApproveRejectForm",strParameter);
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				
				 jasperSubReport1 = JasperCompileManager.compileReport("generalreports/PolicyHistorySUB1.jrxml");
				 jasperSubReport2 = JasperCompileManager.compileReport("generalreports/ClaimHistorySub.jrxml");
				 jasperSubReport3 = JasperCompileManager.compileReport("generalreports/MediclaimAppRejSub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("MyDataSource1",new TTKReportDataSource("ClmPolicyHistoryApprej",alParamList));
				 hashMap.put("MediClaimSubReport1",jasperSubReport1);
				
				 
				 hashMap.put("MyDataSource2",new TTKReportDataSource("ClmHistoryApprej",strVidalHealthId));
				 hashMap.put("MediClaimSubReport2",jasperSubReport2);
				
				 
				 hashMap.put("MyDataSource3",new TTKReportDataSource("ClmBillHistory",strParameter));
				 hashMap.put("MediClaimSubReport3",jasperSubReport3);
				 
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateAppRejClmReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 
	 
	 
	 /**bajaj
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	 public ActionForward doGenerateAppRejPatReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateCompReport method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport, jasperSubReport1,jasperSubReport2,jasperSubReport3, emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 ArrayList alParamList=new ArrayList();
			 
			 String jrxmlfile="generalreports/PreAuthApprovalForm.jrxml";
			 
			String strParameter= (String)request.getParameter("parameter");
			String strVidalHealthId= (String)request.getParameter("vidalttkId");
			String strEnrollNumber= (String)request.getParameter("enrollNumber");
			String strPolicyNo= (String)request.getParameter("policyNo");
		     alParamList.add(strPolicyNo);
			 alParamList.add(strVidalHealthId);
			
			 
		//	 
		
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+strVidalHealthId+"_"+strParameter+".pdf";	
			 ttkReportDataSource = new TTKReportDataSource("PatApproveRejectForm",strParameter);
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				
				 jasperSubReport1 = JasperCompileManager.compileReport("generalreports/PolicyHistorySUB1.jrxml");
				 jasperSubReport2 = JasperCompileManager.compileReport("generalreports/PatHistorySub.jrxml");
				 jasperSubReport3 = JasperCompileManager.compileReport("generalreports/MediclaimPatAppRejSub.jrxml");
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("MyDataSource1",new TTKReportDataSource("PatpolHistoryApprej",alParamList));
				 hashMap.put("MediClaimSubReport1",jasperSubReport1);                                                                                                                                 
				
				 
				 hashMap.put("MyDataSource2",new TTKReportDataSource("PatHistoryApprej",strVidalHealthId));
				 hashMap.put("MediClaimSubReport2",jasperSubReport2);
				
				 
				 hashMap.put("MyDataSource3",new TTKReportDataSource("PatBillHistory",strParameter));
				 hashMap.put("MediClaimSubReport3",jasperSubReport3);
				 
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateAppRejPatReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 /**Self Fund
	  * This method is used to generate the report .
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
	//self fund nethra
		 public ActionForward doGenerateDiagReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			 try{
				 log.debug("Inside the doGenerateDiagReport method of ReportsAction");
				 setLinks(request);
				 JasperReport jasperReport, jasperSubReport, xmljasperReport, emptyReport;
				 JasperPrint jasperPrint;
				 TTKReportDataSource ttkReportDataSource = null;
				 String jrxmlfile = "generalreports/AuthorizationLetter.jrxml";
				 Long diagSeqIdForSelfFund	=	(Long)request.getSession().getAttribute("diagSeqIdForSelfFund");
				 String diagseqid=""+diagSeqIdForSelfFund;
				 //String jrxmlfile=request.getParameter("fileName");
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),diagseqid);
				 try
				 {
					 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
					 //xmljasperReport = JasperCompileManager.compileReport("generalreports/BankAccSub.jrxml");
					 jasperSubReport = JasperCompileManager.compileReport("generalreports/AuthorizationSub.jrxml");
					 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
					 ByteArrayOutputStream boas=new ByteArrayOutputStream();
					 hashMap.put("MyDataSource",new TTKReportDataSource("DiagonisTariffDetails",diagseqid));
					 //hashMap.put("MyDataSourceFloat",new TTKReportDataSource("FinBanRecFloatTran",request.getParameter("parameter")));
					 //hashMap.put("BankAccSubreport",xmljasperReport);
					 hashMap.put("AuthorizationSubReport",jasperSubReport);
					 //hashMap.put("Month",request.getParameter("month"));
					 //hashMap.put("Year",request.getParameter("year"));
					 if(ttkReportDataSource.getResultData().next())
					 {
						 ttkReportDataSource.getResultData().beforeFirst();
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
					 }//end of if(ttkReportDataSource.getResultData().next()))
					 else{
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }//end of else
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);				 
					 request.setAttribute("boas",boas);
				 }//end of try
				 catch (Exception e)
				 {
					 e.printStackTrace();
				 }//end of catch (Exception e)
				 return (mapping.findForward(strReportdisplay));
			 }catch(TTKException expTTK)
				{
					return this.processExceptions(request , mapping ,expTTK);
				}
				catch(Exception exp)
				 {
					 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
				 }//end of catch(Exception exp)
			}//end of doTPACommissionRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 
	 /**
	     * This method is used to search the data with the given search criteria.
	     * Finally it forwards to the appropriate view based on the specified forward mappings
	     *
	     * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	     */
	    public ActionForward doWebdoc(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{	    		
	    		log.debug("inside Reports Action doWebdoc method");
	    		setOnlineLinks(request);
	    		ByteArrayOutputStream baos=null;
	    		OutputStream sos = null;
	    		FileInputStream fis = null;
	    		response.setContentType("application/octet-stream");
	    		String strModule = request.getParameter("module");
	    		String strFile = "";
	    		String[] strFileName=null;
	    		if(strModule.equals("associate"))
	    		{
	    			//strFile =TTKPropertiesReader.getPropertyValue("AssociatedDocuments")+"/"+request.getParameter("fileName");
	    			strFile =request.getParameter("fileName");
	    		}//end of if(strModule.equals("associate"))
	    		else
	    		{
	    			strFile = request.getParameter("fileName");
	    			String strOSName= System.getProperty("os.name");
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else

	    		}//end of else 
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebdocsDirno"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebdocsDirno"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebdocsDirno"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
	    		BufferedInputStream bis = new BufferedInputStream(fis);
	    		baos=new ByteArrayOutputStream();
	    		int ch;
	    		while ((ch = bis.read()) != -1)
	    		{
	    			baos.write(ch);
	    		}//end of while ((ch = bis.read()) != -1)
	    		sos = response.getOutputStream();
	    		baos.writeTo(sos);
	    	}//end of try
	    	catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}//end of catch(Exception exp)
	    	return null;
	    }//end of doWebdoc(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	    
	    
	    
	    /**
	     * This method is used to search the data with the given search criteria.
	     * Finally it forwards to the appropriate view based on the specified forward mappings
	     *
	     * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	     */
	    public ActionForward doViewFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{	    		
	    		log.info("inside Reports Action doViewFiles method");
	    		setOnlineLinks(request);
	    		ByteArrayOutputStream baos=null;
	    		OutputStream sos = null;
	    		FileInputStream fis = null;
	    		response.setContentType("application/octet-stream");
	    		String strModule = request.getParameter("module");
	    		String strFile = "";
	    		String[] strFileName=null;
	    		String rownum	=	(String)request.getParameter("rownum");
	    		
	    		DynaActionForm generalForm = (DynaActionForm)form;
	    		MOUDocumentVO mouDocumentVO	=	null;
	    		TableData tableData	=	null;
	    		if("preauth".equals(strModule))
	    			tableData = (TableData) request.getSession().getAttribute("tableDataMouCertificates");
	    		else
	    			tableData = TTKCommon.getTableData(request);
	    		mouDocumentVO = (MOUDocumentVO)tableData.getRowInfo(Integer.parseInt(rownum));
               
	    		if(strModule.equals("associate"))
	    		{
	    			//strFile =TTKPropertiesReader.getPropertyValue("AssociatedDocuments")+"/"+request.getParameter("fileName");
	    			strFile =request.getParameter("fileName");
	    		}//end of if(strModule.equals("associate"))
	    		/*else if(strModule.equals("mou"))
	    		{
	    			strFile =TTKPropertiesReader.getPropertyValue("mouUploads")+"/"+mouDocumentVO.getFileName();
	    			
	    		}//end of if(strModule.equals("mou"))
	    		*/
	    		else
	    		{
	    			//strFile = request.getParameter("fileName");
	    			strFile	=	TTKPropertiesReader.getPropertyValue("mouUploads")+mouDocumentVO.getMouDocPath();
	    			String strOSName= System.getProperty("os.name");
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		    
	    		    
	    		}//end of else 
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			//  

	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
	    		BufferedInputStream bis = new BufferedInputStream(fis);
	    		baos=new ByteArrayOutputStream();
	    		int ch;
	    		while ((ch = bis.read()) != -1)
	    		{
	    			baos.write(ch);
	    		}//end of while ((ch = bis.read()) != -1)
	    		sos = response.getOutputStream();
	    		baos.writeTo(sos);
	    	}//end of try
	    	catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}//end of catch(Exception exp)
	    	return null;
	    }//end of doViewFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	    
	    /**
	     * This method is used to search the data with the given search criteria.
	     * Finally it forwards to the appropriate view based on the specified forward mappings
	     *
	     * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	     */
	    public ActionForward doViewProfessionFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{	    		
	    		log.debug("inside Reports Action doViewProfessionFiles method");
	    		setOnlineLinks(request);
	    		ByteArrayOutputStream baos=null;
	    		OutputStream sos = null;
	    		FileInputStream fis = null;
	    		response.setContentType("application/octet-stream");
	    		String strModule = request.getParameter("module");
	    		String strFile = "";
	    		String[] strFileName=null;
	    		
	    			//strFile = request.getParameter("fileName");
    			strFile	=	TTKPropertiesReader.getPropertyValue("professionalsUpload")+request.getParameter("fileName");

	    			String strOSName= System.getProperty("os.name");
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		    

	    		
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
	    		BufferedInputStream bis = new BufferedInputStream(fis);
	    		baos=new ByteArrayOutputStream();
	    		int ch;
	    		while ((ch = bis.read()) != -1)
	    		{
	    			baos.write(ch);
	    		}//end of while ((ch = bis.read()) != -1)
	    		sos = response.getOutputStream();
	    		baos.writeTo(sos);
	    	}//end of try
	    	catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}//end of catch(Exception exp)
	    	return null;
	    }//end of doViewProfessionFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    
 public ActionForward doPolicyFileDownload(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
    		log.debug("inside Reports Action doPolicyFileDownload method");

    		String rownum	=	(String)request.getParameter("rownum");
    		
    		MOUDocumentVO mouDocumentVO	=	null;
			TableData tableData = TTKCommon.getTableData(request);
    		mouDocumentVO = (MOUDocumentVO)tableData.getRowInfo(Integer.parseInt(rownum));
    		
    		String fileName=mouDocumentVO.getFileName();
            String filaPath=TTKPropertiesReader.getPropertyValue("policyPrososalsUpload");
            ByteArrayOutputStream baos=null;
            OutputStream sos = null;
            FileInputStream fis = null; 
            BufferedInputStream bis =null;
             response.setContentType("application/txt");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            File file=new File(filaPath+"/"+fileName);
            if(fileName==null|| fileName.length()<1 || !file.exists()){
            	sos = response.getOutputStream();
                  sos.write("File Not Exist".getBytes());
                  sos.flush(); 
            }else{
              fis= new FileInputStream(file);
              bis = new BufferedInputStream(fis);
              baos=new ByteArrayOutputStream();
              int ch;
                    while ((ch = bis.read()) != -1) baos.write(ch);
                    sos = response.getOutputStream();
                    baos.writeTo(sos);  
                    baos.flush();      
                    sos.flush(); 
                    try{
                        if(baos!=null)baos.close();                                           
                        if(sos!=null)sos.close();
                        if(bis!=null)bis.close();
                        if(fis!=null)fis.close();
                        }catch(Exception exception){
                        log.error(exception.getMessage(), exception);
                        }     
            }
            return null;

	    }
	    /**
	     * This method is used to search the data with the given search criteria.
	     * Finally it forwards to the appropriate view based on the specified forward mappings
	     *
	     * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	     */
	    public ActionForward doViewCommonForAll(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("inside Reports Action doViewCommonForAll method");
	    		setLinks(request);
	    		ByteArrayOutputStream baos=null;
	    		OutputStream sos = null;
	    		FileInputStream fis = null;
	    		response.setContentType("application/octet-stream");
	    		String strModule = request.getParameter("module");
	    		String fileName = request.getParameter("fileName");
	    		String strFile = "";
	    		String[] strFileName=null;
	    		String strOSName= System.getProperty("os.name");
	    		if("tariffUploadLogs".equals(strModule))
	    		{
		    			strFile	=	TTKPropertiesReader.getPropertyValue("tariffErrorLogs")+fileName;
		    		    if(strOSName.contains("Windows"))
		    		    {
		    		      strFile.replaceAll("\\\\","\\\\\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    if(strOSName.contains("Windows")){
		    		     strFileName=strFile.split("\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    else
		    		    {
		    		     strFileName=strFile.split("/");
		    		    }//end of else
		    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
		    		{
		    			response.setContentType("application/msword");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffErrorLogDirNo"))]);
		    		}//end of if(strFile.endsWith("doc"))
		    		else if(strFile.endsWith("pdf"))
		    		{
		    			response.setContentType("application/pdf");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffErrorLogDirNo"))]);
		    		}//end of else if(strFile.endsWith("pdf"))
		    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		    		{
		    			response.setContentType("application/vnd.ms-excel");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffErrorLogDirNo"))]);
		    		}//end of else if(strFile.endsWith("xls"))
		    		else if(strFile.endsWith("txt")){
				    		response.setContentType("text/plain");
				    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffErrorLogDirNo"))]);
		    		}
		    		File f = new File(strFile);
		    		if(f.isFile() && f.exists()){
		    			fis = new FileInputStream(f);
		    		}//end of if(strFile !="")
		    		else
		    		{
		    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		    		}//end of else
	    		}
	    		else if("bulkModifyLogs".equals(strModule))
	    		{
	    			strFile	=	TTKPropertiesReader.getPropertyValue("BulkModifyErrorLogs")+fileName;
	    			if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
				if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+fileName);
	    		}//end of else if(strFile.endsWith("xls"))
	    		else if(strFile.endsWith("txt")){
			    		response.setContentType("text/plain");
			    		response.setHeader("Content-Disposition","attachment;filename="+fileName);
	    		}
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
    		}
	    		else if("financeBanks".equals(strModule))
	    		{
	    			strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		else if(strFile.endsWith("txt")){
			    		response.setContentType("text/plain");
			    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
    		}else if("mouUploads".equals(strModule))//Finance view Docs
    		{
    			strFile	=	TTKPropertiesReader.getPropertyValue("mouUploads")+fileName;
    		    if(strOSName.contains("Windows"))
    		    {
    		      strFile.replaceAll("\\\\","\\\\\\\\");
    		    }//end of if(strOSName.contains("Windows"))
    		    if(strOSName.contains("Windows")){
    		     strFileName=strFile.split("\\\\");
    		    }//end of if(strOSName.contains("Windows"))
    		    else
    		    {
    		     strFileName=strFile.split("/");
    		    }//end of else
    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
    		{
    			response.setContentType("application/msword");
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
    		}//end of if(strFile.endsWith("doc"))
    		else if(strFile.endsWith("pdf"))
    		{
    			response.setContentType("application/pdf");
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
    		}//end of else if(strFile.endsWith("pdf"))
    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
    		{
    			response.setContentType("application/vnd.ms-excel");
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
    		}//end of else if(strFile.endsWith("xls"))
    		else if(strFile.endsWith("txt")){
		    		response.setContentType("text/plain");
		    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
    		}File f = new File(strFile);
    		if(f.isFile() && f.exists()){
    			fis = new FileInputStream(f);
    		}//end of if(strFile !="")
    		else
    		{
    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
    		}//end of else
		}
    		
	    		else if("circulars".equals(strModule))
	    		{
    			
    			String rownum	=	(String)request.getParameter("rownum");
	    		
	    		MOUDocumentVO mouDocumentVO	=	null;
				TableData tableData = TTKCommon.getTableData(request);
	    		mouDocumentVO = (MOUDocumentVO)tableData.getRowInfo(Integer.parseInt(rownum));
	    		
    			strFile	=	TTKPropertiesReader.getPropertyValue("policyPrososalsUpload")+mouDocumentVO.getFileName();
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		else if(strFile.endsWith("txt")){
			    		response.setContentType("text/plain");
			    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}
    		File f = new File(strFile);
    		if(f.isFile() && f.exists()){
    			fis = new FileInputStream(f);
    		}//end of if(strFile !="")
    		else
    		{
    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
    		}//end of else
		}
    		else if("policyDocs".equals(strModule))
    		{
    			
    			String rownum	=	(String)request.getParameter("rownum");
	    		
	    		MOUDocumentVO mouDocumentVO	=	null;
				TableData tableData = TTKCommon.getTableData(request);
	    		mouDocumentVO = (MOUDocumentVO)tableData.getRowInfo(Integer.parseInt(rownum));
	    		
    			strFile	=	TTKPropertiesReader.getPropertyValue("policyPrososalsUpload")+mouDocumentVO.getFileName();
    		    if(strOSName.contains("Windows"))
    		    {
    		      strFile.replaceAll("\\\\","\\\\\\\\");
    		    }//end of if(strOSName.contains("Windows"))
    		    if(strOSName.contains("Windows")){
    		     strFileName=strFile.split("\\\\");
    		    }//end of if(strOSName.contains("Windows"))
    		    else
    		    {
    		     strFileName=strFile.split("/");
    		    }//end of else
    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
    		{
    			response.setContentType("application/msword");
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
    		}//end of if(strFile.endsWith("doc"))
    		else if(strFile.endsWith("pdf"))
    		{
    			response.setContentType("application/pdf");
    			
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
    		}//end of else if(strFile.endsWith("pdf"))
    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
    		{
    			response.setContentType("application/vnd.ms-excel");
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
    		}//end of else if(strFile.endsWith("xls"))
    		else if(strFile.endsWith("txt")){
		    		response.setContentType("text/plain");
		    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
    		}
    		File f = new File(strFile);
    		if(f.isFile() && f.exists()){
    			fis = new FileInputStream(f);
    		}//end of if(strFile !="")
    		else
    		{
    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
    		}//end of else
		}
	    		else if("policyDocs".equals(strModule))
	    		{
	    			
	    			String rownum	=	(String)request.getParameter("rownum");
		    		MOUDocumentVO mouDocumentVO	=	null;
					TableData tableData = (TableData) request.getSession().getAttribute("tablePolicyDocs");
		    		mouDocumentVO = (MOUDocumentVO)tableData.getRowInfo(Integer.parseInt(rownum));
		    		
	    			strFile	=	TTKPropertiesReader.getPropertyValue("policyPrososalsUpload")+mouDocumentVO.getFileName();
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		else if(strFile.endsWith("txt")){
			    		response.setContentType("text/plain");
			    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("productCircularUploadDir"))]);
	    		}
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
			}
	    		
	    		//PREAUTH REFERRAL LETTER VIEW S T A R T S
	    		else if("preAuthReferrrals".equals(strModule))
	    		{
	    			strFile	=	TTKPropertiesReader.getPropertyValue("preAuthReferralDir")+fileName+".pdf";
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("preAuthReferralDirNo"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
			}
	    		//PREAUTH REFERRAL LETTER VIEW E N D S
	    		
	    		//PharmacyTariffUploadFormat View begins
	    		else if("pharmacyTariffUpload".equals(strModule))
	    		{
	    			//strFile = request.getParameter("fileName");
    			strFile	=	TTKPropertiesReader.getPropertyValue("uploadFormats")+"pharmacyTariffUploadFormat.xls";
    			
    			//System.out.println(" strFile  ==> "+strFile);
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		    	
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		      	
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		    
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		    
	    		  
		    		
	    		if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+"pharmacyTariffUploadFormat.xls");
	    		}//end of else if(strFile.endsWith("xls"))
	    		
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
    		
	    		}
	    		else if("bulktariffmodify".equals(strModule)||"bulkpharmacymodify".equals(strModule)){

	    			//strFile = request.getParameter("fileName");
	    			String myFileName=null;
	    			if("bulktariffmodify".equals(strModule))
	    			{
	    				myFileName="ACTIVITY TYPE TARIFF ACTIVITY CODE.xls";
	    				strFile	=	TTKPropertiesReader.getPropertyValue("bulkModifyFormats")+myFileName;
	    			}else{
	    				myFileName="PHARMACY CONSUMABLE TYPE AC.xls";
	    				strFile	=	TTKPropertiesReader.getPropertyValue("bulkModifyFormats")+myFileName;
	    			}
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		    
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+myFileName);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+myFileName);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+myFileName);
	    		}//end of else if(strFile.endsWith("xls"))
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
    		
	    		
	    			
	    		}
	    		//PharmacyTariffUploadFormat View ends
	    		else if("collectionDocs".equals(strModule))
	    		{
	    			strFile	=	TTKPropertiesReader.getPropertyValue("collectionDocsUpload")+fileName;
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);
	    			//response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);
	    			//response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);
	    			//response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("collectionDocsUpload"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		else if(strFile.endsWith("txt")){
			    		response.setContentType("text/plain");
			    		response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);
			    		//response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
    		}
	    		
	    		else{
		    			//strFile = request.getParameter("fileName");
	    			strFile	=	TTKPropertiesReader.getPropertyValue("uploadFormats")+"tariffUploadFormat.xls";
		    		    if(strOSName.contains("Windows"))
		    		    {
		    		      strFile.replaceAll("\\\\","\\\\\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    if(strOSName.contains("Windows")){
		    		     strFileName=strFile.split("\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    else
		    		    {
		    		     strFileName=strFile.split("/");
		    		    }//end of else
		    			
		    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
		    		{
		    			response.setContentType("application/msword");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffFormatDirno"))]);
		    		}//end of if(strFile.endsWith("doc"))
		    		else if(strFile.endsWith("pdf"))
		    		{
		    			response.setContentType("application/pdf");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffFormatDirno"))]);
		    		}//end of else if(strFile.endsWith("pdf"))
		    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		    		{
		    			response.setContentType("application/vnd.ms-excel");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);
//		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffFormatDirno"))]);
		    		}//end of else if(strFile.endsWith("xls"))
		    		File f = new File(strFile);
		    		if(f.isFile() && f.exists()){
		    			fis = new FileInputStream(f);
		    		}//end of if(strFile !="")
		    		else
		    		{
		    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		    		}//end of else
	    		}
	    		BufferedInputStream bis = new BufferedInputStream(fis);
	    		baos=new ByteArrayOutputStream();
	    		int ch;
	    		while ((ch = bis.read()) != -1)
	    		{
	    			baos.write(ch);
	    		}//end of while ((ch = bis.read()) != -1)
	    		sos = response.getOutputStream();
	    		baos.writeTo(sos);
	    		bis.close();
	    		baos.close();
	    		fis.close();
	    	}//end of try
	    	catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}//end of catch(Exception exp)
	    	return null;
	    }//end of doViewCommonForAll(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    

	    
	    
	    
	    
	    
	    
	    public ActionForward doViewInsReports(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("inside Reports Action doViewInsReports method");
	    		setLinks(request);
	    		ByteArrayOutputStream baos=null;
	    		OutputStream sos = null;
	    		FileInputStream fis = null;
	    		response.setContentType("application/octet-stream");
	    		String strModule = request.getParameter("module");
	    		String fileName = request.getParameter("fileName");
	    		String strFile = "";
	    		String[] strFileName=null;
	    		
	    		if("tariffUploadLogs".equals(strModule))
	    		{
		    			strFile	=	TTKPropertiesReader.getPropertyValue("tariffErrorLogs")+fileName;
		    			String strOSName= System.getProperty("os.name");
		    		    if(strOSName.contains("Windows"))
		    		    {
		    		      strFile.replaceAll("\\\\","\\\\\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    if(strOSName.contains("Windows")){
		    		     strFileName=strFile.split("\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    else
		    		    {
		    		     strFileName=strFile.split("/");
		    		    }//end of else
		    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
		    		{
		    			response.setContentType("application/msword");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebdocsDirno"))]);
		    		}//end of if(strFile.endsWith("doc"))
		    		else if(strFile.endsWith("pdf"))
		    		{
		    			response.setContentType("application/pdf");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebdocsDirno"))]);
		    		}//end of else if(strFile.endsWith("pdf"))
		    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		    		{
		    			response.setContentType("application/vnd.ms-excel");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebdocsDirno"))]);
		    		}//end of else if(strFile.endsWith("xls"))
		    		else if(strFile.endsWith("txt")){
				    		response.setContentType("text/plain");
				    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebdocsDirno"))]);
		    		}
		    		File f = new File(strFile);
		    		if(f.isFile() && f.exists()){
		    			fis = new FileInputStream(f);
		    		}//end of if(strFile !="")
		    		else
		    		{
		    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		    		}//end of else
	    		}
	    		else if("financeBanks".equals(strModule))
	    		{
	    			strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
	    			String strOSName= System.getProperty("os.name");
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			response.setContentType("application/pdf");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}//end of else if(strFile.endsWith("xls"))
	    		else if(strFile.endsWith("txt")){
			    		response.setContentType("text/plain");
			    		response.setHeader("Content-Disposition","attachment;filename"+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	    		}
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
    		}else if("insReports".equals(strModule))
    		{
    			strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
    			String strOSName= System.getProperty("os.name");
    		    if(strOSName.contains("Windows"))
    		    {
    		      strFile.replaceAll("\\\\","\\\\\\\\");
    		    }//end of if(strOSName.contains("Windows"))
    		    if(strOSName.contains("Windows")){
    		     strFileName=strFile.split("\\\\");
    		    }//end of if(strOSName.contains("Windows"))
    		    else
    		    {
    		     strFileName=strFile.split("/");
    		    }//end of else
    		if(strFile.endsWith("pdf"))
    		{
    			response.setContentType("application/pdf");
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
    		}//end of else if(strFile.endsWith("pdf"))
    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
    		{
    			response.setContentType("application/vnd.ms-excel");
    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
    		}//end of else if(strFile.endsWith("xls"))
    		File f = new File(strFile);
    		if(f.isFile() && f.exists()){
    			fis = new FileInputStream(f);
    		}//end of if(strFile !="")
    		else
    		{
    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
    		}//end of else
		}else if("TechnicalResults".equals(strModule))
		{
			strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
			String strOSName= System.getProperty("os.name");
		    if(strOSName.contains("Windows"))
		    {
		      strFile.replaceAll("\\\\","\\\\\\\\");
		    }//end of if(strOSName.contains("Windows"))
		    if(strOSName.contains("Windows")){
		     strFileName=strFile.split("\\\\");
		    }//end of if(strOSName.contains("Windows"))
		    else
		    {
		     strFileName=strFile.split("/");
		    }//end of else
		if(strFile.endsWith("pdf"))
		{
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("pdf"))
		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		{
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("xls"))
		File f = new File(strFile);
		if(f.isFile() && f.exists()){
			fis = new FileInputStream(f);
		}//end of if(strFile !="")
		else
		{
			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		}//end of else
	}else if("TreatyStatistics".equals(strModule))
	{
		strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
		String strOSName= System.getProperty("os.name");
	    if(strOSName.contains("Windows"))
	    {
	      strFile.replaceAll("\\\\","\\\\\\\\");
	    }//end of if(strOSName.contains("Windows"))
	    if(strOSName.contains("Windows")){
	     strFileName=strFile.split("\\\\");
	    }//end of if(strOSName.contains("Windows"))
	    else
	    {
	     strFileName=strFile.split("/");
	    }//end of else
	if(strFile.endsWith("pdf"))
	{
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	}//end of else if(strFile.endsWith("pdf"))
	else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	{
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
	}//end of else if(strFile.endsWith("xls"))
	File f = new File(strFile);
	if(f.isFile() && f.exists()){
		fis = new FileInputStream(f);
	}//end of if(strFile !="")
	else
	{
		fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	}//end of else
	}else if("Burning".equals(strModule))
	{
		strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
		String strOSName= System.getProperty("os.name");
	    if(strOSName.contains("Windows"))
	    {
	      strFile.replaceAll("\\\\","\\\\\\\\");
	    }//end of if(strOSName.contains("Windows"))
	    if(strOSName.contains("Windows")){
	     strFileName=strFile.split("\\\\");
	    }//end of if(strOSName.contains("Windows"))
	    else
	    {
	     strFileName=strFile.split("/");
	    }//end of else
		if(strFile.endsWith("pdf"))
		{
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("pdf"))
		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		{
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("xls"))
		File f = new File(strFile);
		if(f.isFile() && f.exists()){
			fis = new FileInputStream(f);
		}//end of if(strFile !="")
		else
		{
			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		}//end of else
		}
		    		
		else if("HIPA".equals(strModule))
		{
			strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
			String strOSName= System.getProperty("os.name");
		    if(strOSName.contains("Windows"))
		    {
		      strFile.replaceAll("\\\\","\\\\\\\\");
		    }//end of if(strOSName.contains("Windows"))
		    if(strOSName.contains("Windows")){
		     strFileName=strFile.split("\\\\");
		    }//end of if(strOSName.contains("Windows"))
		    else
		    {
		     strFileName=strFile.split("/");
		    }//end of else
		if(strFile.endsWith("pdf"))
		{
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("pdf"))
		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		{
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("xls"))
		File f = new File(strFile);
		if(f.isFile() && f.exists()){
			fis = new FileInputStream(f);
		}//end of if(strFile !="")
		else
		{
			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		}//end of else
		}
		else if("DoctorDailyReport".equals(strModule))
		{
			strFile	=	TTKPropertiesReader.getPropertyValue("bankDocsUpload")+fileName;
			String strOSName= System.getProperty("os.name");
		    if(strOSName.contains("Windows"))
		    {
		      strFile.replaceAll("\\\\","\\\\\\\\");
		    }//end of if(strOSName.contains("Windows"))
		    if(strOSName.contains("Windows")){
		     strFileName=strFile.split("\\\\");
		    }//end of if(strOSName.contains("Windows"))
		    else
		    {
		     strFileName=strFile.split("/");
		    }//end of else
		if(strFile.endsWith("pdf"))
		{
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("pdf"))
		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		{
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("FinanceBankUploads"))]);
		}//end of else if(strFile.endsWith("xls"))
		File f = new File(strFile);
		if(f.isFile() && f.exists()){
			fis = new FileInputStream(f);
		}//end of if(strFile !="")
		else
		{
			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		}//end of else
		}    		
	    		
	  		else{
		    			//strFile = request.getParameter("fileName");
	    			strFile	=	TTKPropertiesReader.getPropertyValue("uploadFormats")+"tariffUploadFormat.xls";
	    			
		    			String strOSName= System.getProperty("os.name");
		    		    if(strOSName.contains("Windows"))
		    		    {
		    		      strFile.replaceAll("\\\\","\\\\\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    if(strOSName.contains("Windows")){
		    		     strFileName=strFile.split("\\\\");
		    		    }//end of if(strOSName.contains("Windows"))
		    		    else
		    		    {
		    		     strFileName=strFile.split("/");
		    		    }//end of else
		    		    
		    			
		    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
		    		{
		    			response.setContentType("application/msword");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffFormatDirno"))]);
		    		}//end of if(strFile.endsWith("doc"))
		    		else if(strFile.endsWith("pdf"))
		    		{
		    			response.setContentType("application/pdf");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffFormatDirno"))]);
		    		}//end of else if(strFile.endsWith("pdf"))
		    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
		    		{
		    			response.setContentType("application/vnd.ms-excel");
		    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("TariffFormatDirno"))]);
		    		}//end of else if(strFile.endsWith("xls"))
		    		File f = new File(strFile);
		    		if(f.isFile() && f.exists()){
		    			fis = new FileInputStream(f);
		    		}//end of if(strFile !="")
		    		else
		    		{
		    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
		    		}//end of else
	    		}
	    		BufferedInputStream bis = new BufferedInputStream(fis);
	    		baos=new ByteArrayOutputStream();
	    		int ch;
	    		while ((ch = bis.read()) != -1)
	    		{
	    			baos.write(ch);
	    		}//end of while ((ch = bis.read()) != -1)
	    		sos = response.getOutputStream();
	    		baos.writeTo(sos);
	    		bis.close();
	    		baos.close();
	    		fis.close();
	    	}//end of try
	    	catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}//end of catch(Exception exp)
	    	return null;
	    }//end of doViewInsReports(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	    
	    
	    
	    //Finance MIS Reports

public ActionForward doGenerateDetailReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws TTKException{
	try{
		
		 setLinks(request);
		  //added for KOC-1276
		 try
		 { 
			 TTKReportManager tTKReportManager	=	null;
			 DynaActionForm frmMISReports	=	(DynaActionForm) form;
			 tTKReportManager	=	this.getTTKReportManager();
			 ArrayList alData	=	null;
			 String argList		=	request.getParameter("argList");
			 String repType		=	request.getParameter("repType");
			 alData				=	tTKReportManager.getDetailedReport(argList,repType);
			 	/*ReportVO reportVO	=	new ReportVO();
				ArrayList<String> al 	=	new ArrayList<>();
				ArrayList<ReportVO> al1 	=	new ArrayList<ReportVO>();
				al.add("kishor");
				al.add("kumar");
				reportVO.setAlColumns(al);
				al1.add(reportVO);*/
				request.setAttribute("alData", alData);
			 
			 return (mapping.findForward("preauthutilization"));
		 }//end of try
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }//end of catch (Exception e)
		 return (mapping.findForward(strReportdisplay));
	 }//end of try
	 catch(TTKException expTTK)
	 {
		 return this.processExceptions(request, mapping, expTTK);
	 }//end of catch(TTKException expTTK)
	 catch(Exception exp)
	 {
		 return this.processExceptions(request, mapping, new TTKException(exp,"reportdetail"));
	 }//end of catch(Exception exp)
}


	    
	    
	    
	    
	    /**When the user clicks on the Download button in print Cheques - provider Login
		  * This method is used to generate the report .
		  * Finally it forwards to the appropriate view based on the specified forward mappings
		  *
		  * @param mapping The ActionMapping used to select this instance
		  * @param form The optional ActionForm bean for this request (if any)
		  * @param request The HTTP request we are processing
		  * @param response The HTTP response we are creating
		  * @return ActionForward Where the control will be forwarded, after this request is processed
		  * @throws Exception if any error occurs
		  */
		 public ActionForward doDownLoadChequeReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			 try{
				 log.debug("Inside the doDownLoadChequeReport method of ReportsAction");
				 setOnlineLinks(request);
				 JasperReport jasperReport;
				 JasperPrint jasperPrint;
				 String strParam = null;			 
				 TTKReportDataSource ttkReportDataSource = null;
				 ResultSet rs = null;
				 String jrxmlfile= null;		 
				 ttkReportDataSource = new TTKReportDataSource("chequeWiseReport","11111");
				 rs = ttkReportDataSource.getResultData();			 
				 try
				 {
					 ByteArrayOutputStream boas=new ByteArrayOutputStream();
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
					  if(rs.next()){	
						 String strTemplateName = rs.getString("CHECK_NUM");
						 log.debug("Template Name is " + strTemplateName);
						 if(strTemplateName!=null)
						 {
							 jrxmlfile = "generalreports/ChequeWiseReport.jrxml";
						 }//end of if(strTemplateName!=null)
						 rs.previous();
						 File f = new File(jrxmlfile);
						 if(f.exists()){
							 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
							 jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,new TTKReportDataSource(rs));
						 }//end of  if(f.exists())   compiling the template file
						 else {//template does not exists
							 jasperReport = JasperCompileManager.compileReport("generalreports/tna.jrxml");
							 jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,new JREmptyDataSource());
						 }//end of else					 
					 }//if(rs.next())
					 else {
						 jrxmlfile = "generalreports/EmptyReprot.jrxml";
						 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new JREmptyDataSource());
					 }//end of else
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
					 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
					 request.setAttribute("boas",boas);
					 
					 
					 /*jrxmlfile = "generalreports/EmptyReprot.jrxml";
					 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new JREmptyDataSource());
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
					 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
					 request.setAttribute("boas",boas);*/
				 }//end of try
				 catch (JRException e)
				 {
					 e.printStackTrace();
				 }//end of catch(JRException e)
				 catch (Exception e)
				 {
					 e.printStackTrace();
				 }//end of catch (Exception e)
				 return (mapping.findForward(strReportdisplay));
			 }//end of try
			 catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
			 catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			 }//end of catch(Exception exp)
		 }//end of doDownLoadChequeReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    /**
		  * This method is used to view the report generating screen.
		  * Finally it forwards to the appropriate view based on the specified forward mappings
		  *
		  * @param mapping The ActionMapping used to select this instance
		  * @param form The optional ActionForm bean for this request (if any)
		  * @param request The HTTP request we are processing
		  * @param response The HTTP response we are creating
		  * @return ActionForward Where the control will be forwarded, after this request is processed
		  * @throws Exception if any error occurs
		  */
		 public ActionForward doPreAuthClmDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			 try
			 {
				 setLinks(request);
				 DynaActionForm frmReportList=(DynaActionForm)form;
				 request.getSession().setAttribute("frmReportList",frmReportList);
				 String strReportParameter=strPreAuthClmDetail;
				 return mapping.findForward(strReportParameter);
			 }//end of try
			 catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
			 catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			 }//end of catch(Exception exp)
		 }//end of doPreAuthClmDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		 
	 
		 public ActionForward doTobFileDownload(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		    		HttpServletResponse response) throws Exception{
	    		log.debug("inside Reports Action doPolicyFileDownload method");

	    		String rownum	=	(String)request.getParameter("rownum");
	    		MOUDocumentVO mouDocumentVO	=	null;
				TableData tableData = (TableData) request.getSession().getAttribute("tablePolicyDocs");
	    		mouDocumentVO = (MOUDocumentVO)tableData.getRowInfo(Integer.parseInt(rownum));
	    		
	    		
	    		String fileName=mouDocumentVO.getFileName();
	            String filaPath=TTKPropertiesReader.getPropertyValue("policyPrososalsUpload");
	            ByteArrayOutputStream baos=null;
	            OutputStream sos = null;
	            FileInputStream fis = null; 
	            BufferedInputStream bis =null;
	             response.setContentType("application/txt");
	            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
	            File file=new File(filaPath+"/"+fileName);
	            if(fileName==null|| fileName.length()<1 || !file.exists()){
	            	sos = response.getOutputStream();
	                  sos.write("File Not Exist".getBytes());
	                  sos.flush(); 
	            }else{
	              fis= new FileInputStream(file);
	              bis = new BufferedInputStream(fis);
	              baos=new ByteArrayOutputStream();
	              int ch;
	                    while ((ch = bis.read()) != -1) baos.write(ch);
	                    sos = response.getOutputStream();
	                    baos.writeTo(sos);  
	                    baos.flush();      
	                    sos.flush(); 
	                    try{
	                        if(baos!=null)baos.close();                                           
	                        if(sos!=null)sos.close();
	                        if(bis!=null)bis.close();
	                        if(fis!=null)fis.close();
	                        }catch(Exception exception){
	                        log.error(exception.getMessage(), exception);
	                        }     
	            }
	            return null;

		    }
		 
		 /**
		  * This method is used to generate the Endorsement Impact report .
		  * Finally it forwards to the appropriate view based on the specified forward mappings
		  *
		  * @param mapping The ActionMapping used to select this instance
		  * @param form The optional ActionForm bean for this request (if any)
		  * @param request The HTTP request we are processing
		  * @param response The HTTP response we are creating
		  * @return ActionForward Where the control will be forwarded, after this request is processed
		  * @throws Exception if any error occurs
		  */
		 public ActionForward doGenerateQuoteReport(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
			{
			 try{
				 log.debug("Inside the doGenerateCompReport method of ReportsAction");
				 setLinks(request);
				 JasperReport jasperReport, jasperSubReport1,jasperReport2,jasperReport3, jasperReport4,jasperReport5,jasperReport6,jasperReport7,jasperReport8,jasperReport9,emptyReport;
				 JasperReport jasperReport10, jasperReport11,jasperReport12,jasperReport13, jasperReport14,jasperReport15,jasperReport16,jasperReport17,jasperReport18,jasperReport19,jasperReport20,jasperReport21,jasperReport22,jasperReportSub;
				 JasperPrint jasperPrint;
				 JasperPrint jasperPrint1 = null;
				 JasperPrint jasperPrint2 = null;
				 JasperPrint jasperPrint3 = null;
				 JasperPrint jasperPrint4 = null; 
				 JasperPrint jasperPrint5= null; 
				 JasperPrint jasperPrint6= null; 
				 JasperPrint jasperPrint7= null; 
				 JasperPrint jasperPrint8 = null;
				 JasperPrint jasperPrint9 = null;
				 JasperPrint jasperPrint10 = null;
				 JasperPrint jasperPrint11= null;
				 JasperPrint jasperPrint12 = null;
				 JasperPrint jasperPrint13 = null; 
				 JasperPrint jasperPrint14 = null; 
				 JasperPrint jasperPrint15 = null; 
				 JasperPrint jasperPrint16 = null; 
				 JasperPrint jasperPrint17 = null; 
				 JasperPrint jasperPrint18 = null; 
				 JasperPrint jasperPrint19 = null; 
				 JasperPrint jasperPrint20 = null;
				 JasperPrint jasperPrint21 = null;
				 TTKReportDataSource ttkReportDataSource = null,ttkReportDataSourceSub = null;;
				 TTKReportDataSource ttkReportDataSource1 = null;
				 String strParameter=request.getParameter("parameter");
				//    
				 
				 
				 try
				 {
					
					 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
					/* String strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
					 hashMap.put("SigPath",strPath);*/	
	/*Title Section*/
					 ttkReportDataSource = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport = JasperCompileManager.compileReport("generalreports/TitleSectionQuote.jrxml"); 
					 if(ttkReportDataSource.getResultData().next())
					 { 
						 ttkReportDataSource.getResultData().beforeFirst();
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap, ttkReportDataSource);					 
					 }//end of if(ttkReportDataSource.getResultData().next()))
					 else{
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }//end of else
					 
					 String strPdfFile = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_title.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
					 
	/*Letter Section*/
					 
					 ttkReportDataSource1 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport2 = JasperCompileManager.compileReport("generalreports/LetterSection.jrxml"); 
					 if(ttkReportDataSource1.getResultData().next())
					 { 
						 ttkReportDataSource1.getResultData().beforeFirst();
						  jasperPrint1 = JasperFillManager.fillReport(jasperReport2, hashMap, ttkReportDataSource1);
					 }
					 String strPdfFile1 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_lettersection.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint1,strPdfFile1); 
					 
					 
	/*Notes for Letter Section*/
					
					
					 TTKReportDataSource ttkReportDataSource2 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport3 = JasperCompileManager.compileReport("generalreports/NotesForLetterSection.jrxml"); 
					 if(ttkReportDataSource2.getResultData().next())
					 { 
						 ttkReportDataSource2.getResultData().beforeFirst();
						 jasperPrint2 = JasperFillManager.fillReport(jasperReport3, hashMap, ttkReportDataSource2);
					 }
					 String strPdfFile2 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_noteslettersection.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint2,strPdfFile2); 
					 
   /*Notes for Letter Section*/ 
					 TTKReportDataSource ttkReportDataSource3 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport4 = JasperCompileManager.compileReport("generalreports/NotesForLetterSection1.jrxml"); 
					 if(ttkReportDataSource3.getResultData().next())
					 { 
						 ttkReportDataSource3.getResultData().beforeFirst();
						 jasperPrint3 = JasperFillManager.fillReport(jasperReport4, hashMap, ttkReportDataSource3);
					 }
					 String strPdfFile3 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_noteslettersection1.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint3,strPdfFile3); 
					 
	/*Benefit design*/
						
					 TTKReportDataSource ttkReportDataSource4 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport5 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection.jrxml"); 
					 if(ttkReportDataSource4.getResultData().next())
					 { 
						 
						 ttkReportDataSource4.getResultData().beforeFirst();
						 jasperPrint4 = JasperFillManager.fillReport(jasperReport5, hashMap, ttkReportDataSource4);
					 }
					 String strPdfFile4 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint4,strPdfFile4); 
					 
	/*Benefit design1*/
					
					  
					 TTKReportDataSource ttkReportDataSource5 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport6 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection1.jrxml"); 
					 if(ttkReportDataSource5.getResultData().next())
					 { 
						 
						 ttkReportDataSource5.getResultData().beforeFirst();
						 jasperPrint5 = JasperFillManager.fillReport(jasperReport6, hashMap, ttkReportDataSource5);
					 }
					 String strPdfFile5 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign1.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint5,strPdfFile5); 
					 
	/*Benefit design2*/
						
					 TTKReportDataSource ttkReportDataSource6 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport7 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection2.jrxml"); 
					 if(ttkReportDataSource6.getResultData().next())
					 {
						 ttkReportDataSource6.getResultData().beforeFirst();
						 jasperPrint6 = JasperFillManager.fillReport(jasperReport7, hashMap, ttkReportDataSource6);
					 }
					 String strPdfFile6 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign2.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint6,strPdfFile6); 
					 
	/*Benefit design9*/
						
					 TTKReportDataSource ttkReportDataSource19 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport20 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection9.jrxml"); 
					 if(ttkReportDataSource19.getResultData().next())
					 {
						 ttkReportDataSource19.getResultData().beforeFirst();
						 jasperPrint19 = JasperFillManager.fillReport(jasperReport20, hashMap, ttkReportDataSource19);
					 }
					 String strPdfFile19 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign9.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint19,strPdfFile19); 
					 
	/*Benefit design3*/
						
					 TTKReportDataSource ttkReportDataSource7 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport8 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection3.jrxml"); 
					 if(ttkReportDataSource7.getResultData().next())
					 { 
						 ttkReportDataSource7.getResultData().beforeFirst();
						 jasperPrint7 = JasperFillManager.fillReport(jasperReport8, hashMap, ttkReportDataSource7);
					 }
					 String strPdfFile7 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign3.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint7,strPdfFile7); 
					 
	/*Benefit design4*/
						
					 TTKReportDataSource ttkReportDataSource8 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport9 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection4.jrxml"); 
					 if(ttkReportDataSource8.getResultData().next())
					 { 
						 ttkReportDataSource8.getResultData().beforeFirst();
						 jasperPrint8 = JasperFillManager.fillReport(jasperReport9, hashMap, ttkReportDataSource8);
					 }
					 String strPdfFile8 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign4.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint8,strPdfFile8); 
					 
	/*Benefit design5*/
						
					 TTKReportDataSource ttkReportDataSource9 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport10 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection5.jrxml"); 
					 if(ttkReportDataSource9.getResultData().next())
					 { 
						 ttkReportDataSource9.getResultData().beforeFirst();
						 jasperPrint9 = JasperFillManager.fillReport(jasperReport10, hashMap, ttkReportDataSource9);
					 }
					 String strPdfFile9 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign5.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint9,strPdfFile9);
					 
	/*Benefit design6*/
						
					 TTKReportDataSource ttkReportDataSource10 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport11 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection6.jrxml"); 
					 if(ttkReportDataSource10.getResultData().next())
					 { 
						 ttkReportDataSource10.getResultData().beforeFirst();
						 jasperPrint10 = JasperFillManager.fillReport(jasperReport11, hashMap, ttkReportDataSource10);
					 }
					 String strPdfFile10 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign6.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint10,strPdfFile10);
					 
	/*Benefit design10*/
						
					 TTKReportDataSource ttkReportDataSource20 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport21 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection10.jrxml"); 
					 if(ttkReportDataSource20.getResultData().next())
					 { 
						 ttkReportDataSource20.getResultData().beforeFirst();
						 jasperPrint20 = JasperFillManager.fillReport(jasperReport21, hashMap, ttkReportDataSource20);
					 }
					 String strPdfFile20 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign10.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint20,strPdfFile20);
					 
	/*Benefit design7*/
						
					 TTKReportDataSource ttkReportDataSource11 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport12 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection7.jrxml"); 
					 if(ttkReportDataSource11.getResultData().next())
					 { 
						 ttkReportDataSource11.getResultData().beforeFirst();
						 jasperPrint11 = JasperFillManager.fillReport(jasperReport12, hashMap, ttkReportDataSource11);
					 }
					 String strPdfFile11 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign7.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint11,strPdfFile11);
					 
	/*Benefit design8*/
						
					 TTKReportDataSource ttkReportDataSource16 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport17 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection8.jrxml"); 
					 if(ttkReportDataSource16.getResultData().next())
					 { 
						 ttkReportDataSource16.getResultData().beforeFirst();
						 jasperPrint16 = JasperFillManager.fillReport(jasperReport17, hashMap, ttkReportDataSource16);
					 }
					 String strPdfFile16 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign8.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint16,strPdfFile16);
					 
	/*Benefit design8*/
						
					 TTKReportDataSource ttkReportDataSource21 = new TTKReportDataSource("BenefitDesignSection",strParameter);
					 jasperReport22 = JasperCompileManager.compileReport("generalreports/BenefitDesignSection11.jrxml"); 
					 if(ttkReportDataSource21.getResultData().next())
					 { 
						 ttkReportDataSource21.getResultData().beforeFirst();
						 jasperPrint21 = JasperFillManager.fillReport(jasperReport22, hashMap, ttkReportDataSource21);
					 }
					 String strPdfFile21= TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_benefitdesign11.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint21,strPdfFile21);
					 
	/*Premium Rates*/
					
					 TTKReportDataSource ttkReportDataSource12 = new TTKReportDataSource("PremiumRates",strParameter,2);
					 jasperReport13 = JasperCompileManager.compileReport("generalreports/PremiumRates.jrxml"); 
					 jasperReportSub = JasperCompileManager.compileReport("generalreports/PremiumRatesSub.jrxml");
					 ttkReportDataSourceSub = new TTKReportDataSource("PremiumRates",strParameter,3);
					 hashMap.put("MyDataSource",ttkReportDataSourceSub);
					 hashMap.put("PremiumRatesSubReport",jasperReportSub);
					 if(ttkReportDataSource12.getResultData().next())
					 { 
						 ttkReportDataSource12.getResultData().beforeFirst();
						 jasperPrint12 = JasperFillManager.fillReport(jasperReport13, hashMap, ttkReportDataSource12);
					 }
					 else if(ttkReportDataSourceSub.getResultData().next())
					 { 
						 ttkReportDataSourceSub.getResultData().beforeFirst();
						 jasperPrint12 = JasperFillManager.fillReport(jasperReport13, hashMap, ttkReportDataSourceSub);
					 }
					 String strPdfFile12 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_premiumrates.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint12,strPdfFile12); 
					 
	/*General Terms and COnditions*/
						
					 TTKReportDataSource ttkReportDataSource13 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport14 = JasperCompileManager.compileReport("generalreports/GeneralTermsAndConditions.jrxml"); 
					 if(ttkReportDataSource13.getResultData().next())
					 { 
						 ttkReportDataSource13.getResultData().beforeFirst();
						 jasperPrint13 = JasperFillManager.fillReport(jasperReport14, hashMap, ttkReportDataSource13);
					 }
					 String strPdfFile13 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_generalTermsConditions.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint13,strPdfFile13); 
					 
	/*General Terms and COnditions 1*/
						
					 TTKReportDataSource ttkReportDataSource14 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport15 = JasperCompileManager.compileReport("generalreports/GeneralTermsAndConditions1.jrxml"); 
					if(ttkReportDataSource14.getResultData().next())
					 { 
						 ttkReportDataSource14.getResultData().beforeFirst();
						 jasperPrint14 = JasperFillManager.fillReport(jasperReport15, hashMap, ttkReportDataSource14);
					 }
					 String strPdfFile14 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_generalTermsConditions1.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint14,strPdfFile14); 
					 
		/*General Terms and COnditions 2*/
						
					 TTKReportDataSource ttkReportDataSource15 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport16 = JasperCompileManager.compileReport("generalreports/GeneralTermsAndConditions2.jrxml"); 
					if(ttkReportDataSource15.getResultData().next())
					 { 
						ttkReportDataSource15.getResultData().beforeFirst();
						 jasperPrint15 = JasperFillManager.fillReport(jasperReport16, hashMap, ttkReportDataSource15);
					 }
					 String strPdfFile15 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_generalTermsConditions2.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint15,strPdfFile15); 
					 
		/*Client Approval*/
						
					 TTKReportDataSource ttkReportDataSource17 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport18 = JasperCompileManager.compileReport("generalreports/ClientApprovalReport.jrxml"); 
					if(ttkReportDataSource17.getResultData().next())
					 { 
						ttkReportDataSource17.getResultData().beforeFirst();
						 jasperPrint17= JasperFillManager.fillReport(jasperReport18, hashMap, ttkReportDataSource17);
					 }
					 String strPdfFile17= TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_ClientApprovalReport.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint17,strPdfFile17); 
					 
		 /*Client Approval*/
						
					 TTKReportDataSource ttkReportDataSource18 = new TTKReportDataSource("HealthInsuranceQuote",strParameter);
					 jasperReport19 = JasperCompileManager.compileReport("generalreports/ConfirmationTerms.jrxml"); 
					if(ttkReportDataSource18.getResultData().next())
					 { 
						ttkReportDataSource18.getResultData().beforeFirst();
						 jasperPrint18= JasperFillManager.fillReport(jasperReport19, hashMap, ttkReportDataSource18);
					 }
					 String strPdfFile18= TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_ConfirmationTerms.pdf";
					 JasperExportManager.exportReportToPdfFile(jasperPrint18,strPdfFile18); 
					 
					 
					 
					 String fileName2="";
					 File file = null;
					 String path1=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("authorizationrptdir1"))+"FinalOutput/";
					 fileName2=strParameter+".pdf";
					 String finalPath2=(path1+fileName2);
					 
					 
					 List<InputStream> list = new ArrayList<InputStream>();
					 FileOutputStream outputStream = null;
					 list.add(new FileInputStream(new File(strPdfFile)));
					 list.add(new FileInputStream(new File(strPdfFile1)));
			         list.add(new FileInputStream(new File(strPdfFile2)));
			         list.add(new FileInputStream(new File(strPdfFile3)));
			         list.add(new FileInputStream(new File(strPdfFile4)));
			         list.add(new FileInputStream(new File(strPdfFile5)));
			         list.add(new FileInputStream(new File(strPdfFile6)));
			         list.add(new FileInputStream(new File(strPdfFile19)));
			         list.add(new FileInputStream(new File(strPdfFile7)));
			         list.add(new FileInputStream(new File(strPdfFile8)));
			         list.add(new FileInputStream(new File(strPdfFile9)));
			         list.add(new FileInputStream(new File(strPdfFile10)));
			         list.add(new FileInputStream(new File(strPdfFile20)));
			         list.add(new FileInputStream(new File(strPdfFile11)));
			         list.add(new FileInputStream(new File(strPdfFile16)));
			         list.add(new FileInputStream(new File(strPdfFile21)));
			         list.add(new FileInputStream(new File(strPdfFile12)));
			         list.add(new FileInputStream(new File(strPdfFile13)));
			         list.add(new FileInputStream(new File(strPdfFile14)));
			         list.add(new FileInputStream(new File(strPdfFile15)));
			         list.add(new FileInputStream(new File(strPdfFile17)));
			         list.add(new FileInputStream(new File(strPdfFile18)));
			         
			         outputStream =new FileOutputStream(new File(finalPath2));
					    
				     doMergePDF(list, outputStream);
					   
				     file = new File(finalPath2);
					 if(file.exists())
		    			{
						 	finalPath2 = (path1+fileName2);
						 	//  
		    				request.setAttribute("fileName",finalPath2);
		    			}//end of if(file.exists())
					// JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
					 
					 
					 
					// request.setAttribute("boas",boas);
				 }//end of try
				 catch (JRException e)
				 {
					 e.printStackTrace();
				 }//end of catch(JRException e)
				 catch (Exception e)
				 {
					 e.printStackTrace();
				 }//end of catch (Exception e)
				 return this.getForward(strGenerateQuote, mapping, request);
			 }//end of try
			 catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
			 catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			 }//end of catch(Exception exp)
			}//end of method doGenerateImpactReport(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
		 
		 
		 public static void doMergePDF(List<InputStream> list, OutputStream outputStream)
					throws DocumentException, IOException {
						Document document = new Document();
						PdfWriter writer = PdfWriter.getInstance(document, outputStream);
						writer.open();
						document.open();

						PdfContentByte cb = writer.getDirectContent();
					
						for (InputStream in : list) {
						
							PdfReader reader = new PdfReader(in);
							
							for (int i = 1; i <=reader.getNumberOfPages(); i++) {
								
								document.newPage();
								//import the page from source pdf
								PdfImportedPage page = writer.getImportedPage(reader, i);
								//add the page to the destination pdf
								cb.addTemplate(page, 0, 0);
							}
						}
						outputStream.flush();
						document.close();
						outputStream.close();
					}
		 
		 public ActionForward doPrintAcknowledgement(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		    		HttpServletResponse response) throws Exception{
		    	try{
		    		setLinks(request);
		    		return (mapping.findForward(strReportdisplay));
		    	}
		    	catch(TTKException expTTK)
		    	{
		    		return this.processExceptions(request,mapping,expTTK);
		    	}//end of catch(ETTKException expTTK)
		    	catch(Exception exp)
		    	{
		    		return this.processExceptions(request,mapping,new TTKException(exp,strReportExp));
		    	}//end of catch(Exception exp)
		    }//end of doViewPaymentXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		 
		 /**
      * Returns the TTKReportManager session object for invoking methods on it.
      * @return TTKReportManager session object which can be used for method invokation
      * @exception throws TTKException
      */
     private TTKReportManager getTTKReportManager() throws TTKException
     {
         TTKReportManager tTKReportManager = null;
         try
         {
             if(tTKReportManager == null)
             {
                 InitialContext ctx = new InitialContext();
                 tTKReportManager = (TTKReportManager) ctx.lookup("java:global/TTKServices/business.ejb3/TTKReportManagerBean!com.ttk.business.reports.TTKReportManager");
             }//end if
         }//end of try
         catch(Exception exp)
         {
             throw new TTKException(exp, "tTKReport");
         }//end of catch
         return tTKReportManager;
     }//end getTTKReportManager() 
	    
     
     /**
	     * This method is used to search the data with the given search criteria.
	     * Finally it forwards to the appropriate view based on the specified forward mappings
	     *
	     * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	     */

     public ActionForward doViewUploadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{	    		
	    		log.info("inside Reports Action doViewFiles method");
	    		setOnlineLinks(request);
	    		ByteArrayOutputStream baos=null;
	    		OutputStream sos = null;
	    		FileInputStream fis = null;
	    		response.setContentType("application/octet-stream");
	    		String strModule = request.getParameter("module");
	    		String strFile = "";
	    		String[] strFileName=null;
	    		String rownum	=	(String)request.getParameter("rownum");
	    		
	    		DynaActionForm generalForm = (DynaActionForm)form;
	    		PreAuthDetailVO preAuthDetailVO	=null;
	    		TableData tableData	=	null;
	    		if("preauth".equals(strModule))
	    			tableData = (TableData) request.getSession().getAttribute("tableDataDocsUpload");
	    		else
	    			//tableData = TTKCommon.getTableData(request);
	    			tableData = (TableData) request.getSession().getAttribute("tableDataDocsUpload");
	    		preAuthDetailVO = (PreAuthDetailVO)tableData.getRowInfo(Integer.parseInt(rownum));            
	    		if(strModule.equals("associate"))
	    		{
	    			//strFile =TTKPropertiesReader.getPropertyValue("AssociatedDocuments")+"/"+request.getParameter("fileName");
	    			strFile =request.getParameter("fileName");
	    		}//end of if(strModule.equals("associate"))
	    		else
	    		{
	    			//strFile = request.getParameter("fileName");
	    			strFile	=	TTKPropertiesReader.getPropertyValue("preauthDocsUploads")+preAuthDetailVO.getMouDocPath();
	    			String strOSName= System.getProperty("os.name");
	    		    if(strOSName.contains("Windows"))
	    		    {
	    		      strFile.replaceAll("\\\\","\\\\\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    if(strOSName.contains("Windows")){
	    		     strFileName=strFile.split("\\\\");
	    		    }//end of if(strOSName.contains("Windows"))
	    		    else
	    		    {
	    		     strFileName=strFile.split("/");
	    		    }//end of else
	    		    
	    		    
	    		}//end of else 
	    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
	    		{
	    			response.setContentType("application/msword");
	    			/*	response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);*/	    		
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);

	    		}//end of if(strFile.endsWith("doc"))
	    		else if(strFile.endsWith("pdf"))
	    		{
	    			//  
	    			response.setContentType("application/pdf");
	    			/*	response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);*/	 
	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);

	    		}//end of else if(strFile.endsWith("pdf"))
	    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
/*	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[Integer.parseInt(TTKPropertiesReader.getPropertyValue("WebMOUdocsDirno"))]);
*/	    			response.addHeader("Content-Disposition","attachment; filename="+strFileName[0]);

	    		}//end of else if(strFile.endsWith("xls"))
	    		File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
	    		BufferedInputStream bis = new BufferedInputStream(fis);
	    		baos=new ByteArrayOutputStream();
	    		int ch;
	    		while ((ch = bis.read()) != -1)
	    		{
	    			baos.write(ch);
	    		}//end of while ((ch = bis.read()) != -1)
	    		sos = response.getOutputStream();
	    		baos.writeTo(sos);
	    	}//end of try
	    	catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}//end of catch(Exception exp)
	    	return null;
	    }//end of doViewFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
     public ActionForward doRoutineReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doClaimPendRptDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strRoutineReport;
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doClaimPendRptDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
     
     
     
     public static HSSFWorkbook createFinanceReport(ArrayList<String[]> financeDataList, String reportId) throws IOException {
 		HSSFWorkbook workbook	=	new HSSFWorkbook();
 		HSSFCellStyle headerStyle = workbook.createCellStyle();
 		
 		HSSFFont font = workbook.createFont();
 		font.setFontName(HSSFFont.FONT_ARIAL);
 		font.setFontHeightInPoints((short) 10);
 		font.setBoldweight((short) 40);
 		font.setColor(HSSFColor.WHITE.index);
 		font.setFontHeightInPoints((short) 12);
 		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
 		
 		//Defing style for header
 		headerStyle.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
 		headerStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
 		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
 		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
 		headerStyle.setFont(font);
 		
 		//Defing style for Body
 		HSSFCellStyle bodyDataStyle = workbook.createCellStyle();
 		bodyDataStyle.setWrapText(true);
 		bodyDataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
 		
 		//Header Data
 		ArrayList<String> headerDataList=null;
 		if("RoutineRpt".equals(reportId)){
 			headerDataList=getRoutineHeaderList();
 		}else if("FinPreAuthRpt".equals(reportId)){
 			headerDataList=getFinPreauthHeaderList();
 		}
 		
 		/*Find the number of sheet have to create logic Starts*/
 		double dupSheetCount=financeDataList.size()/MAX_LENGTH;
 		int dupDivisable=financeDataList.size()%MAX_LENGTH;
 		int dupIntsheetCount=(int) dupSheetCount;
 		if(dupDivisable!=0)
 			dupIntsheetCount++;
 		double actSheetCount=(financeDataList.size()+dupIntsheetCount)/MAX_LENGTH;
 		int actDivisable=(financeDataList.size()+dupIntsheetCount)%MAX_LENGTH;
 		int sheetCount=(int) actSheetCount;
 		if(actDivisable!=0)
 			sheetCount++;
 		/*Find the number of sheet have to create logic End*/
 		
 		/*Creating sheets starts*/
 		HSSFSheet errSheet =null;
 		int count=1;
 		while(sheetCount!=0){
 			errSheet=workbook.createSheet("sheet"+count);
 			sheetCount--;
 			count++;
 		}
 		/*Creating sheets end*/
 		log.info("Size is::"+financeDataList.size());
 		HSSFRow row =null;
 		int index=0;
 		int sheetData=0;
 		int cout=0;
 		log.info("before starts");
 		for(index=0;index<financeDataList.size()+cout;index++){
// 			System.out.println("index::"+index);
 			/*Creating rows for sheet logic starts*/
 			double decimalcurrentSheet=index/MAX_LENGTH;
 			int curDivisable=index%MAX_LENGTH;
 			int currentSheet=(int) decimalcurrentSheet;
 			if(curDivisable!=0 &&index>MAX_LENGTH)
 				sheetCount++;
 			errSheet=workbook.getSheetAt(currentSheet);
 			if(index%MAX_LENGTH!=0)
 			row=errSheet.createRow(index-(currentSheet*MAX_LENGTH));
 			else{
 				/*Creating header logic starts*/
 				row=errSheet.createRow(0);
 				row=setHeader(errSheet,row,headerDataList,headerStyle);	
 				cout++;
 				continue;
 				/*Creating header logic end*/
 			}
 			sheetData=index-cout;
 			/*Creating rows for sheet logic ends*/
 			
 			/*Importing data to body cell logic starts*/
 			String[] arr=financeDataList.get(sheetData);
 				for(int i=0;i<arr.length;i++)
 	 			{
 	  	    			HSSFCell bodyCell = row.createCell((short)i);
 	  	    			bodyCell.setCellStyle(bodyDataStyle);
 	  	    			bodyCell.setCellValue(arr[i]);
 	  	    			arr[i]=null;
 	 			}   
 				/*Importing data to body cell logic ends*/
 		}
 		log.info("No.Of records done..."+financeDataList.size());
 		financeDataList=null;
 		return workbook;
 	}
 private static ArrayList<String> getRoutineHeaderList() {
 	ArrayList<String> headerDataList=new ArrayList<>();
 	headerDataList.add("Corporate Name");
 	headerDataList.add("Policy No.");
 	headerDataList.add("Policy Start Date");
 	headerDataList.add("Policy End Date");
 	headerDataList.add("Al Koot ID");
 	headerDataList.add("Emp No.");
 	headerDataList.add("Principal Name");
 	headerDataList.add("Member Name");
 	headerDataList.add("Relation");
 	headerDataList.add("Name of Partner");
 	headerDataList.add("Name of Provider");
 	headerDataList.add("Empanelment ID");
 	headerDataList.add("Invoice No.");
 	headerDataList.add("Date of Admission");
 	headerDataList.add("Date Of Discharge");
 	headerDataList.add("Claim Type");
 	headerDataList.add("Claim Batch No.");
 	headerDataList.add("Claim From");
 	headerDataList.add("Claim Received Date");
 	headerDataList.add("Claim Added Date");
 	headerDataList.add("Claim No.");
 	headerDataList.add("Total Claimed Amt.");
	headerDataList.add("Net Amount");
 	headerDataList.add("Incurred Currency");
 /*	headerDataList.add("Total Claim Amount (QAR)");*/
 	headerDataList.add("Conversion Rate");
 	headerDataList.add("Claim Status");
 	headerDataList.add("Completed Date");
 	headerDataList.add("Claim Settlement No.");
 	headerDataList.add("Amount Payable (QAR)");
 	headerDataList.add("Fast Track Discount");
 	headerDataList.add("Amount Payable after Discount");
 	headerDataList.add("Amount Payable as per Incurred Currency");
 	headerDataList.add("Amount Payable (USD)");
 	headerDataList.add("Amount Payable (GBP)");
 	headerDataList.add("Amount Payable (EUR)");
 	headerDataList.add("Finance Status");
 	headerDataList.add("Ageing as per received date");
 	headerDataList.add("Ageing Band"); 
 	headerDataList.add("Ageing as per Approved date");
 	headerDataList.add("Provider Term Agreed(in Days)");
 	headerDataList.add("Bank Location");
 	headerDataList.add("Bank Name");
 	headerDataList.add("Payment To");
 	headerDataList.add("Beneficiary Name");
 	headerDataList.add("IBAN Number");
 	headerDataList.add("Bank Account Number");
 	headerDataList.add("Swift Code");
 	headerDataList.add("City");
 	headerDataList.add("Country");
 	headerDataList.add("Finance Batch Date");
 	headerDataList.add("Finance Batch No.");
 	headerDataList.add("Transaction Reference Number");
 	headerDataList.add("Payment Reference No");
 	headerDataList.add("Cheque Date");
 	headerDataList.add("Vidal Reference Number");
 	return headerDataList;
 }
 private static HSSFRow setHeader(HSSFSheet errSheet,HSSFRow sheetRow, ArrayList<String> headerDataList,HSSFCellStyle headerStyle) {
 	for (int inc=0;inc<headerDataList.size();inc++) {
 		HSSFCell headerCell = sheetRow.createCell((short)inc);
 		headerCell.setCellStyle(headerStyle);
 		headerCell.setCellValue(headerDataList.get(inc));
 		errSheet.autoSizeColumn((short)inc);
 	}
 	return sheetRow;
 }
 
 // admin polices member list
 public ActionForward doGenerateAdminPolMemList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
	 try{
		 
		 setLinks(request);
		 int iNo = 3;
		 JasperReport objJasperReport = null;
		 JasperPrint objJasperPrint[] = new JasperPrint[iNo];
		 ArrayList<Object> alJasperPrintList = new ArrayList<Object>();
		 String policySeqID = (String) request.getParameter("parameter");
		 Long policySeqId = Long.parseLong(policySeqID);
		 String memberStatus = (String) request.getParameter("memberStatus");
		 String strParams[] = new String[] {"|"+request.getParameter("parameter")+"|1|40000|","|"+request.getParameter("parameter")+"|40001|80000|","|"+request.getParameter("parameter")+"|80001|120000|"					 
		 };
		 String jrxmlfile=request.getParameter("fileName");
		 String strSheetNames[] = new String[] {
					"GenerateXL1",
					"GenerateXL2",
					"GenerateXL3"};
		 
				JasperReport emptyReport = null;
			 	JasperPrint jasperPrint;
			 	TTKReportDataSource ttkReportsDataSource = null;

			 	ttkReportsDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParams[0],policySeqId,memberStatus);
			 	try
			 	{
			 		objJasperReport = JasperCompileManager.compileReport(jrxmlfile);							 
			 		HashMap<String,String> hashMap = new HashMap<String, String>();
			 		ByteArrayOutputStream boas=new ByteArrayOutputStream();
			if(!(ttkReportsDataSource.getResultData().next()))
		    {
				emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap,new JREmptyDataSource());
				alJasperPrintList.add(jasperPrint);
		    }//end of if(!(ttkReportsDataSource.getResultData().next()))
			else
			{
				for(int i=0; i<iNo; i++)
				{
					objJasperPrint[i] = JasperFillManager.fillReport(objJasperReport,hashMap,new TTKReportDataSource(request.getParameter("reportID"),strParams[i],policySeqId,memberStatus));
					alJasperPrintList.add(objJasperPrint[i]);
				}//end of for
			}// end of else
			 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
			 {
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST,alJasperPrintList);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "c:\\hay.xls");
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
			 }//end of if(request.getParameter("reportType").equals("EXL"))				
			 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
			 request.setAttribute("boas",boas);
			 response.setHeader("Content-Disposition", "attachement; filename = PolicyMemberListReport.xls");
		 }//end of try
		 catch (JRException e)
		 {
			 e.printStackTrace();
		 }//end of catch(JRException e)
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }//end of catch (Exception e)
		 return (mapping.findForward(strReportdisplay));
	 }//end of try
	 catch(TTKException expTTK)
	 {
		 return this.processExceptions(request, mapping, expTTK);
	 }//end of catch(TTKException expTTK)
	 catch(Exception exp)
	 {
		 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
	 }//end of catch(Exception exp)
 }// end of doGenerateAdminPolMemList()
 
 public ActionForward doPendingSummaryRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doClaimPendRptDetail method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 ChequeManager chequeObject=this.getChequeManagerObject();
			 HttpSession session=request.getSession();
			 
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 frmReportList.set("searchBasedOn","any");
			 session.setAttribute("partnersList", chequeObject.getPartnersList());
			 return this.getForward("pendingSummaryRpt",mapping,request); 
		 }
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }
     
     
     
     public ActionForward pendingMemberRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the pendingMemberRpt method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 ServletOutputStream sos= response.getOutputStream();
			 response.setContentType("application/vnd.ms-excel");  
			 TTKReportDataSource mainTtkReportDataSource1 = null;
	    	
	    		ChequeManager chequeObject=this.getChequeManagerObject();
	    		
	    		 JasperPrint jasperPrint1,jasperPrint2,jasperPrint3,jasperPrint4,jasperPrintEmpty;
				 JasperReport mainJasperReport1,mainJasperReport2,mainJasperReport3,mainJasperReport4,emptyReport;
			
			 String claimTypeID = request.getParameter("claimTypeID");
			 String providername = request.getParameter("providername");
			 String partnerName = request.getParameter("partnerName");
			 
			 String parameterValue = request.getParameter("parameterValue");
			 String reportID	=	request.getParameter("reportID");
			 String reportType	=	request.getParameter("reportType");
			 
			 String jrxmlfile1 = "";
			 String jrxmlfile2 = "";
			 String jrxmlfile3 = "";
			 String jrxmlfile4 = "";
			 
			 HashMap<String,String> hashMap = new HashMap<String,String>();
			 ArrayList<JasperPrint> list = null;
			 list = new  ArrayList<JasperPrint>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 
			 
			 if("CTM".equals(claimTypeID)){
				 
				 jrxmlfile1 = "reports/finance/MemberClaimsPaymentSummaryRpt.jrxml";
				 String[] strSheetNames ={"Member Claims","","",""};
				 mainTtkReportDataSource1 = chequeObject.pendingMemberClaimsRpt(parameterValue,reportID);
				 
				 mainJasperReport1 = JasperCompileManager.compileReport(jrxmlfile1);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 
				 if(mainTtkReportDataSource1.getResultData().next())
				 {
					 mainTtkReportDataSource1.getResultData().beforeFirst();
					 jasperPrint1 = JasperFillManager.fillReport( mainJasperReport1, hashMap, mainTtkReportDataSource1);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint1 = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 
				  if(("EXCEL".equals(request.getParameter("reportType"))))
					 {
					     JRXlsExporter jExcelApiExporter = new JRXlsExporter();
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint1);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						 jExcelApiExporter.exportReport();
					  
					 }
				  request.setAttribute("boas",boas);
				  response.addHeader("Content-Disposition","attachment; filename="+"MemberClaims.xls");
			 }
			 return (mapping.findForward(strReportdisplay));
		 }
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }
     
     
     
     public ActionForward pendingNetworkProviderRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the pendingNetworkRpt method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 ServletOutputStream sos= response.getOutputStream();
			 response.setContentType("application/vnd.ms-excel");  
			 TTKReportDataSource mainTtkReportDataSource1 = null;
	    	
	    		ChequeManager chequeObject=this.getChequeManagerObject();
	    		
	    		 JasperPrint jasperPrint1,jasperPrint2,jasperPrint3,jasperPrint4,jasperPrintEmpty;
				 JasperReport mainJasperReport1,mainJasperReport2,mainJasperReport3,mainJasperReport4,emptyReport;
			
			 String claimTypeID = request.getParameter("claimTypeID");
			 String providername = request.getParameter("providername");
			 String partnerName = request.getParameter("partnerName");
			
			 String parameterValue = request.getParameter("parameterValue");
			 String reportID	=	request.getParameter("reportID");
			 String reportType	=	request.getParameter("reportType");
			 String providerText = request.getParameter("providerText");
			 String jrxmlfile1 = "";
			 String jrxmlfile2 = "";
			 String jrxmlfile3 = "";
			 String jrxmlfile4 = "";
			 
			 HashMap<String,String> hashMap = new HashMap<String,String>();
			 ArrayList<JasperPrint> list = null;
			 list = new  ArrayList<JasperPrint>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 
			 if(providername!=""){
				 
				 hashMap.put("providername", providerText);
			 }
			 else{
				 hashMap.put("providername", "");
			 }
			
			
			 if("CNH".equals(claimTypeID)){
				 
				 jrxmlfile1 = "reports/finance/NetworkClaimsPaymentSummaryRpt.jrxml";
				 String[] strSheetNames ={"Provider Claims","","",""};
				 mainTtkReportDataSource1 = chequeObject.pendingNetWorkClaimsRpt(parameterValue,reportID);
				 
				 mainJasperReport1 = JasperCompileManager.compileReport(jrxmlfile1);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 
				 if(mainTtkReportDataSource1.getResultData().next())
				 {
					 mainTtkReportDataSource1.getResultData().beforeFirst();
					 jasperPrint1 = JasperFillManager.fillReport( mainJasperReport1, hashMap, mainTtkReportDataSource1);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint1 = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 
				  if(("EXCEL".equals(request.getParameter("reportType"))))
					 {
					     JRXlsExporter jExcelApiExporter = new JRXlsExporter();
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint1);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						 jExcelApiExporter.exportReport();
					  
					 }
				  request.setAttribute("boas",boas);
				  response.addHeader("Content-Disposition","attachment; filename="+"PartnerClaims.xls");
			 }
			 return (mapping.findForward(strReportdisplay));
		 }
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }
     
     public ActionForward pendingNetworkRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the pendingNetworkRpt method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 ServletOutputStream sos= response.getOutputStream();
			 response.setContentType("application/vnd.ms-excel");  
			 TTKReportDataSource mainTtkReportDataSource1 = null;
			 TTKReportDataSource mainTtkReportDataSource2 = null;
	    	
	    		ChequeManager chequeObject=this.getChequeManagerObject();
	    		
	    		 JasperPrint jasperPrint1,jasperPrint2,jasperPrint3,jasperPrint4,jasperPrintEmpty;
				 JasperReport mainJasperReport1,mainJasperReport2,mainJasperReport3,mainJasperReport4,emptyReport;
			
			 String claimTypeID = request.getParameter("claimTypeID");
			 String providername = request.getParameter("providername");
			 String partnerName = request.getParameter("partnerName");
			 
			 String parameterValue = request.getParameter("parameterValue");
			 String reportID	=	request.getParameter("reportID");
			 String reportType	=	request.getParameter("reportType");
			 String providerText = request.getParameter("providerText");
			 
			 
			 String jrxmlfile1 = "";
			 String jrxmlfile2 = "";
			 String jrxmlfile3 = "";
			 String jrxmlfile4 = "";
			 
			 HashMap<String,String> hashMap = new HashMap<String,String>();
			 ArrayList<JasperPrint> list = null;
			 list = new  ArrayList<JasperPrint>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
		
			 
			 if("CNH".equals(claimTypeID)){
				 
				 jrxmlfile1 = "reports/finance/NetworkClaimsPaymentSummaryRpt.jrxml";
				 jrxmlfile2 = "reports/finance/PartnerClaimsPaymentSummaryRpt.jrxml";
				// String[] strSheetNames ={"NetWork Claims","Partner Claims","",""};
				 mainTtkReportDataSource1 = chequeObject.pendingNetWorkClaimsRpt(parameterValue,reportID);
				 mainTtkReportDataSource2 = chequeObject.pendingPartnerClaimsRpt(parameterValue,reportID);
				 
				 mainJasperReport1 = JasperCompileManager.compileReport(jrxmlfile1);
				 mainJasperReport2 = JasperCompileManager.compileReport(jrxmlfile2);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 
				 if((mainTtkReportDataSource1.getResultData().next()) && (mainTtkReportDataSource2.getResultData().next()) )
				 {
					                   
					 mainTtkReportDataSource1.getResultData().beforeFirst();
					 mainTtkReportDataSource2.getResultData().beforeFirst();
					 jasperPrint1 = JasperFillManager.fillReport( mainJasperReport1, hashMap,mainTtkReportDataSource1);
					 jasperPrint2 = JasperFillManager.fillReport( mainJasperReport2, hashMap,mainTtkReportDataSource2);	
					 
					 list.add(jasperPrint1);
					 list.add(jasperPrint2);
					 
				 }
				 else
				 {
					 jasperPrintEmpty = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());	
					 list.add(jasperPrintEmpty);
					 list.add(jasperPrintEmpty);
					 
				 }
				 
				  if(("EXCEL".equals(request.getParameter("reportType"))))
					 {
					  
					  
					  JRXlsExporter jExcelApiExporter = new JRXlsExporter();
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
					  
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{"NetWork Claims","Partner Claims"}); 
						 response.addHeader("Content-Disposition","attachment; filename="+"NetWorkClaims.xls");
						
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						 jExcelApiExporter.exportReport();
					 }
			 }
			// return (mapping.findForward(strReportdisplay));
			 sos.flush();
				return null;
		 }
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }  
     
     
     public ActionForward pendingNetworkPartnerRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the pendingNetworkPartnerRpt method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 ServletOutputStream sos= response.getOutputStream();
			 response.setContentType("application/vnd.ms-excel");  
			 TTKReportDataSource mainTtkReportDataSource1 = null;
	    	
	    		ChequeManager chequeObject=this.getChequeManagerObject();
	    		
	    		 JasperPrint jasperPrint1,jasperPrint2,jasperPrint3,jasperPrint4,jasperPrintEmpty;
				 JasperReport mainJasperReport1,mainJasperReport2,mainJasperReport3,mainJasperReport4,emptyReport;
			
			 String claimTypeID = request.getParameter("claimTypeID");
			 String providername = request.getParameter("providername");
			 String partnerName = request.getParameter("partnerName");
			 
			 String parameterValue = request.getParameter("parameterValue");
			 String reportID	=	request.getParameter("reportID");
			 String reportType	=	request.getParameter("reportType");
			 
			 String jrxmlfile1 = "";
			 String jrxmlfile2 = "";
			 String jrxmlfile3 = "";
			 String jrxmlfile4 = "";
			 
			 HashMap<String,String> hashMap = new HashMap<String,String>();
			 ArrayList<JasperPrint> list = null;
			 list = new  ArrayList<JasperPrint>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 
			
			 if("CNH".equals(claimTypeID)){
				 
				 jrxmlfile1 = "reports/finance/PartnerClaimsPaymentSummaryRpt.jrxml";
				 String[] strSheetNames ={"Partner Claims","","",""};
				 mainTtkReportDataSource1 = chequeObject.pendingPartnerClaimsRpt(parameterValue,reportID);
				 
				 mainJasperReport1 = JasperCompileManager.compileReport(jrxmlfile1);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 
				 if(mainTtkReportDataSource1.getResultData().next())
				 {
					 mainTtkReportDataSource1.getResultData().beforeFirst();
					 jasperPrint1 = JasperFillManager.fillReport( mainJasperReport1, hashMap, mainTtkReportDataSource1);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else{
					 jasperPrint1 = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of else
				 
				  if(("EXCEL".equals(request.getParameter("reportType"))))
					 {
					     JRXlsExporter jExcelApiExporter = new JRXlsExporter();
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint1);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, strSheetNames);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						 jExcelApiExporter.exportReport();
					  
					 }
				  request.setAttribute("boas",boas);
				  response.addHeader("Content-Disposition","attachment; filename="+"PartnerClaims.xls");
			 }
			 return (mapping.findForward(strReportdisplay));
		 }
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }  
      
     public ActionForward doGeneratePaymentSummaryRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doGeneratePaymentSummaryRpt method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 ServletOutputStream sos= response.getOutputStream();
			 response.setContentType("application/vnd.ms-excel");  
			 TTKReportDataSource mainTtkReportDataSource1 = null;
			 TTKReportDataSource mainTtkReportDataSource2 = null;
			 TTKReportDataSource mainTtkReportDataSource3 = null;
	    	
	    		ChequeManager chequeObject=this.getChequeManagerObject();
	    		
	    		 JasperPrint jasperPrint1,jasperPrint2,jasperPrint3,jasperPrint4,jasperPrintEmpty;
				 JasperReport mainJasperReport1,mainJasperReport2,mainJasperReport3,mainJasperReport4,emptyReport;
			
			 String claimTypeID = request.getParameter("claimTypeID");
			 String providername = request.getParameter("providername");
			 String partnerName = request.getParameter("partnerName");
			 
			 String parameterValue = request.getParameter("parameterValue");
			 String reportID	=	request.getParameter("reportID");
			 String reportType	=	request.getParameter("reportType");
			 
			 String jrxmlfile1 = "";
			 String jrxmlfile2 = "";
			 String jrxmlfile3 = "";
			 String jrxmlfile4 = "";
			 
			 HashMap<String,String> hashMap = new HashMap<String,String>();
			 ArrayList<JasperPrint> list = null;
			 list = new  ArrayList<JasperPrint>();
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 
			 
			     jrxmlfile1 = "reports/finance/NetworkClaimsPaymentSummaryRpt.jrxml";
			     jrxmlfile2 = "reports/finance/MemberClaimsPaymentSummaryRpt.jrxml";
				 jrxmlfile3 = "reports/finance/PartnerClaimsPaymentSummaryRpt.jrxml";
				 
				
				 mainTtkReportDataSource1= chequeObject.pendingNetWorkClaimsRpt(parameterValue,reportID);
				 mainTtkReportDataSource2 = chequeObject.pendingMemberClaimsRpt(parameterValue,reportID);
				 mainTtkReportDataSource3 = chequeObject.pendingPartnerClaimsRpt(parameterValue,reportID);
				 
				 mainJasperReport1 = JasperCompileManager.compileReport(jrxmlfile1);
				 mainJasperReport2 = JasperCompileManager.compileReport(jrxmlfile2);
				 mainJasperReport3 = JasperCompileManager.compileReport(jrxmlfile3);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 
				 if((mainTtkReportDataSource1.getResultData().next()) && (mainTtkReportDataSource2.getResultData().next()) && (mainTtkReportDataSource3.getResultData().next()))
				 {
					 
					 mainTtkReportDataSource1.getResultData().beforeFirst();
					 mainTtkReportDataSource2.getResultData().beforeFirst();
					 mainTtkReportDataSource3.getResultData().beforeFirst();
					 jasperPrint1 = JasperFillManager.fillReport( mainJasperReport1, hashMap,mainTtkReportDataSource1);
					 jasperPrint2 = JasperFillManager.fillReport( mainJasperReport2, hashMap,mainTtkReportDataSource2);	
					 jasperPrint3 = JasperFillManager.fillReport( mainJasperReport3, hashMap,mainTtkReportDataSource3);	
					 
					 list.add(jasperPrint1);
					 list.add(jasperPrint2);
					 list.add(jasperPrint3);
				 }
				 else
				 {
					 jasperPrintEmpty = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());	
					 list.add(jasperPrintEmpty);
					 list.add(jasperPrintEmpty);
					 list.add(jasperPrintEmpty);
				 }
				 
				  if(("EXCEL".equals(request.getParameter("reportType"))))
					 {
					  
					  
					  JRXlsExporter jExcelApiExporter = new JRXlsExporter();
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
					  
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{"NetWork Claims","Member Claims","Partner Claims"}); 
						 response.addHeader("Content-Disposition","attachment; filename="+"PendingSummaryRpt.xls");
						
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						 jExcelApiExporter.exportReport();
					 }
				//  request.setAttribute("boas",boas);
				 // return (mapping.findForward(strReportdisplay));
				  sos.flush();
					return null;
		 }
		 
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
     }  
 private ChequeManager getChequeManagerObject() throws TTKException
     {
         ChequeManager chequeManager = null;
         try
         {
             if(chequeManager == null)
             {
                 InitialContext ctx = new InitialContext();
                 chequeManager = (ChequeManager) ctx.lookup("java:global/TTKServices/business.ejb3/ChequeManagerBean!com.ttk.business.finance.ChequeManager");
             }//end if(chequeManager == null)
         }//end of try
         catch(Exception exp)
         {
             throw new TTKException(exp, "finmainchequeinfo");
         }//end of catch
         return chequeManager;
     }//end of getChequeManagerObject()
     
  
     public ActionForward doChangeSearchBasedOn(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{

			 log.info("Inside the doClaimPendRptDetail method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 HttpSession session = request.getSession();
			 String seachBasedOn = frmReportList.getString("searchBasedOn");
			 
			 frmReportList.set("searchBasedOn",seachBasedOn);
			 
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 session.setAttribute("frmReportList", frmReportList);
			 return this.getForward("pendingSummaryRpt",mapping,request); 
	 }
	 catch(TTKException expTTK)
	 {
		 return this.processExceptions(request, mapping, expTTK);
	 }//end of catch(TTKException expTTK)
	 catch(Exception exp)
	 {
		 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
	 }//end of catch(Exception exp)
 }
 
   
     public ActionForward doChangeClaimType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{

			 log.info("Inside the doChangeClaimType method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 HttpSession session = request.getSession();
			 String claimTypeID = frmReportList.getString("claimTypeID");
			 
			 frmReportList.set("claimTypeID",claimTypeID);
			 
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 
			 
			 session.setAttribute("frmReportList", frmReportList);
			 
			 return this.getForward("pendingSummaryRpt",mapping,request); 
	 }
	 catch(TTKException expTTK)
	 {
		 return this.processExceptions(request, mapping, expTTK);
	 }//end of catch(TTKException expTTK)
	 catch(Exception exp)
	 {
		 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
	 }//end of catch(Exception exp)
 }    
     
     
     public ActionForward doChangeProviderOrPartner(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{

			 log.info("Inside the doChangeClaimType method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 HttpSession session = request.getSession();
			 String providername = frmReportList.getString("providername");
			 String partnerName = frmReportList.getString("partnerName");
			 
			 if(providername !=""){
				 frmReportList.set("providername",providername);
				 frmReportList.set("partnerName","");
			 }
			 else if(partnerName !=""){
				 frmReportList.set("providername","");
				 frmReportList.set("partnerName",partnerName);
			 }
			 
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 
			 
			 session.setAttribute("frmReportList", frmReportList);
			 
			 return this.getForward("pendingSummaryRpt",mapping,request); 
	 }
	 catch(TTKException expTTK)
	 {
		 return this.processExceptions(request, mapping, expTTK);
	 }//end of catch(TTKException expTTK)
	 catch(Exception exp)
	 {
		 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
	 }//end of catch(Exception exp)
 }    
     
     public ActionForward doChangeClmType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
     {
		 try{
				 log.info("Inside the doChangeClmType method of ReportsAction");
				 setLinks(request);
				 String strReportParameter=strRoutineReport;
				 DynaActionForm frmReportList=(DynaActionForm)form;
				// request.getSession().getAttribute(request.getParameter("reportName"));
				 frmReportList.set("reportName",request.getParameter("reportName"));
				 frmReportList.set("partnerName","");
				 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 } // end of doChangeClmType()
  
     public ActionForward doUnderwritingRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doClaimPendRptDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strUnderwritingReport;
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
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
     
     public ActionForward doGenerateUnderWritingReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
 		
		 String reportID ="";
		 try{
			 log.debug("Inside the doGenerateClaimsPendingReport method of ReportsAction");
			 setLinks(request);

			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile = "";
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 String selectRptType = request.getParameter("selectRptType");
			 String parameterValue = request.getParameter("parameterValue");
			 reportID	=	request.getParameter("reportID");
			 
			  if("UnderwritingRpt".equals(reportID)){									
					 jrxmlfile = "reports/finance/Underwriting.jrxml";
					 ttkReportDataSource = new TTKReportDataSource("UnderwritingRpt",request.getParameter("parameterValue"));
				 }
				
			 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
			 
			 HashMap<String,String> hashMap = new HashMap<String,String>();
			 if(ttkReportDataSource.getResultData().next())
			 {
				 ttkReportDataSource.getResultData().beforeFirst();
				 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);				 
			 }//end of if(ttkReportDataSource.getResultData().next()))
			 else
			 {
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
			 }//end of if(ttkReportDataSource.getResultData().next())
			 
			 
			 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
			 {
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
			 }//end of if(request.getParameter("reportType").equals("EXL"))
			 else//if report type if PDF
			 {
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);	 
			 }//end of else
		
	    
		
			 
			 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
			request.setAttribute("boas",boas);
			 log.debug("report generated sucessfully ");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)	
		
		 return (mapping.findForward(strReportdisplay));
		
	 }
     
     
     
     
     
     
     public ActionForward doCloseCashlessRpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doCloseCashlessRpt method of ReportsAction");
			setLinks(request);
			/* DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().setAttribute("frmReportList",frmReportList);*/
			// String strReportParameter=strIOBRptDetail;
			 return mapping.findForward("preauthreportlist");
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
     

     public ActionForward doPreApprovalReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doClaimPendRptDetail method of ReportsAction");
			 setLinks(request);
			 String strReportParameter=strPreApprovalReport;
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 request.getSession().getAttribute(request.getParameter("reportName"));
			 frmReportList.set("reportName",request.getParameter("reportName"));
			 return this.getForward(strReportParameter,mapping,request);
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
     
     public ActionForward doGenerateFinancePreauthReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
 		
		 String reportID ="";
		 try{
			 log.debug("Inside the doGenerateFinancePreauthReport method of ReportsAction");
			 setLinks(request);
             System.out.println("Report form :"+form);
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			 String jrxmlfile = "";
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			 reportID	=	request.getParameter("reportID");
			 String startDate = request.getParameter("startDate");
			 String endDate = request.getParameter("endDate");
			 String corporatename = request.getParameter("corporatename");
			 String providername = request.getParameter("providername");
			 String preauthStatus = request.getParameter("preauthStatus");
			 String preauthSearchBased = request.getParameter("preauthSearchBased");
			List inputList = new ArrayList<>();
			inputList.add(preauthStatus);
			inputList.add(providername);
			inputList.add(corporatename);
			inputList.add(startDate);
			inputList.add(endDate);
			inputList.add(preauthSearchBased);
		if(reportID.equals("FinPreAuthRpt") && request.getParameter("reportType").equals("EXL")){
			TTKReportManager tTKReportManager	=	this.getTTKReportManager();
			 ArrayList<String[]>  financeOutData = tTKReportManager.getFinancePreauthReport("FinPreAuthRpt",inputList);
			 System.out.println("financeOutData size is::"+financeOutData.size());
			 if(financeOutData!=null&&financeOutData.size()>0){
				 HSSFWorkbook hssfWorkbook=createFinanceReport(financeOutData,reportID);
				 boas = new ByteArrayOutputStream();
				 hssfWorkbook.write(boas);
				 boas.close();
			 	 hssfWorkbook=null;
			 	 request.setAttribute("boas",boas);
			 	 request.setAttribute("reportID","FinPreAuthRpt");
			 	 response.setContentLength(boas.toByteArray().length);
			 	 response.addHeader("Content-Disposition","attachment; filename=financePreauthReport.xls");
			 	 log.info("Done.........");
			 }else{
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String,String> hashMap = new HashMap<String,String>();
				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				 jExcelApiExporter.exportReport();
				 request.setAttribute("boas",boas);
				 response.setContentLength(boas.toByteArray().length);
				 request.setAttribute("reportID","FinPreAuthRpt");
			 	 response.addHeader("Content-Disposition","attachment; filename=financeRoutineReport.xls");
			 }
		}
			 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
			request.setAttribute("boas",boas);
			 log.debug("report generated sucessfully ");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)	
		 
		 return (mapping.findForward(strReportdisplay));
		 
	 }
     
     private static ArrayList<String> getFinPreauthHeaderList() {
    	 

    	 	ArrayList<String> headerDataList=new ArrayList<>();
    	 	headerDataList.add("PREAUTH_NUMBER");
    	 	headerDataList.add("CLAIM_NO");
    	 	headerDataList.add("CORPORATE_NAME");
    	 	headerDataList.add("STATUS");
    	 	headerDataList.add("REQUESTED_AMOUNT_QAR");
    	 	headerDataList.add("APPROVED_AMOUNT_QAR");
    	 	headerDataList.add("PROVIDER_NAME");
    	 	headerDataList.add("ENROLMENT_ID");
    	 	headerDataList.add("MEMBER_NAME");
    	 	headerDataList.add("PROCESSED_BY");
    	 	headerDataList.add("POLICY_NUMBER");
    	 	headerDataList.add("POLICY_START_DATE");
    	 	headerDataList.add("POLICY_END_DATE");
    	 	headerDataList.add("BENEFIT_TYPE");
    	 	headerDataList.add("PREAUTH_RECEIVED_DATE");
    	 	headerDataList.add("APPEAL_DATE");
    	 	headerDataList.add("PREAUTH_ADDED_DATE");
    	 	headerDataList.add("LAST_UPDATED_DATE");
    	 	headerDataList.add("FIRST_MODIFIED_DATE");
    	 	headerDataList.add("PREAUTH_COMPLETED_DATE");
    	 	headerDataList.add("PREAUTH_OVERRIDE_DATE");
    	 	headerDataList.add("ADMISSION_DATE");
    	 	headerDataList.add("DISCHARGE_DATE");
    	 	headerDataList.add("AGEING_AS_PER_RECEIVED_DATE");
    	 	headerDataList.add("AGEING_AS_PER_COMPLETED_DATE");
    	 	return headerDataList;
    	
     }
     
     public ActionForward doEmpDependent(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doEmpDependent method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
			 return this.getForward("empDependentRpt",mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }
	 }
     
     // display policy number list
     public ActionForward doDisplayPolicyNoList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doDisplayPolicyNoList method of ReportsAction");
			 setLinks(request);
			 DynaActionForm frmReportList=(DynaActionForm)form;
		
			 String groupId = (String) frmReportList.get("grpList");
			 ArrayList<Object> policyNo = null;
			 ArrayList<CacheObject> policyNoLis = null;
			 
			 TTKReportManager tTKReportManager = null;
			  tTKReportManager	=	this.getTTKReportManager();
			  
			  policyNo = tTKReportManager.getPolicyNoList(groupId);
			  policyNoLis=(ArrayList<CacheObject>)policyNo.get(0);
			  frmReportList.set("policyNoList",policyNoLis);
			  request.getSession().setAttribute("frmReportList", frmReportList);
			 return this.getForward("empDependentRpt",mapping,request);
		 }//end of trytTKReportManager
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }
     
     public ActionForward doCloseEmpDependent(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doCloseEmpDependent method of ReportsAction");
			 setLinks(request);
			 return this.getForward("enrollmentRptList",mapping,request);
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
     
     public ActionForward doGenerateEmpDependentReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doGenerateEmpDependentReport method of ReportsAction");
			 setOnlineLinks(request);
			 
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
				 JasperReport jasperReport=null,emptyReport,insjasperReport=null;
				 JasperPrint jasperPrint;
				 TTKReportDataSource ttkReportDataSource = null;
				 DynaActionForm frmReportList = (DynaActionForm) form;
				 String strParam = ""; 	 String reportType = "";  String reportID = ""; String fromDate = ""; String toDate = "";
			
				 strParam =  request.getParameter("strParam");
				 reportType =  request.getParameter("reportType");
				 reportID =  request.getParameter("reportID");
				 fromDate =  request.getParameter("fromDate");
				 toDate =  request.getParameter("toDate");
			 
				 String jrxmlfile= null;
				
				 if(reportType.equals("EXL")){
				 jrxmlfile =  "onlinereports/hr/EmpDependentListPerXLS.jrxml";
				 }
				 if(reportType.equals("PDF"))
				 {
					 jrxmlfile = "onlinereports/hr/EmpDependentListPerPDF.jrxml";
				 }
			
				 ttkReportDataSource = new TTKReportDataSource(reportID,strParam);
				
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("parameter",strParam.substring(2,strParam.length()));
				 hashMap.put("From Date",fromDate);
				 hashMap.put("To Date",toDate);
			
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);				 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport(emptyReport, hashMap,new JREmptyDataSource());
				 }
				
				 if(reportType.equals("EXL"))//if the report type is Excel
				 {
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE , Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
					 response.addHeader("Content-Disposition","attachment; filename=EmployeeDependentReport.xls");
				 }//end of if(request.getParameter("reportType").equals("EXL"))				
							
				 else//if report type if PDF
				 {
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
                 }//end of else				
				 request.setAttribute("boas",boas);
			 }//end of try
		 
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)	
		 
		 return (mapping.findForward(strReportdisplay));
	 } // end of doGenerateEmpDependentReport()

     public ActionForward doGenerateActivityLogReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
     {
		 try{
			 	log.debug("Inside the doGenerateActivityLogReport method of ReportsAction");
			 	setOnlineLinks(request);
			 
				 JasperReport jasperReport=null,emptyReport,insjasperReport=null;
				 JasperPrint jasperPrint;
				 TTKReportDataSource ttkReportDataSource = null;
				 String strParam = ""; 	 String reportType = "";  String reportID = "";
			
				 strParam =  request.getParameter("strParam");
				 reportType =  request.getParameter("reportType");
				 reportID =  request.getParameter("reportID");
			 
				 String jrxmlfile= null;
				 jrxmlfile =  "generalreports/ActivityLogReportXLS.jrxml";
			
				 ttkReportDataSource = new TTKReportDataSource(reportID,strParam);
				
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				
			
				 if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);				 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport(emptyReport, hashMap,new JREmptyDataSource());
				 }
				
				
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE , Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
					 response.addHeader("Content-Disposition","attachment; filename=ActivityLogReport.xls");
						
					 request.setAttribute("boas",boas);
			 }//end of try
		 
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)	
		 
		 return (mapping.findForward(strReportdisplay));
	 } // end of doGenerateActivityLogReport()
     
     public ActionForward doChangeProvider(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
     {
		 try{
				 log.info("Inside the doChangeProvider method of ReportsAction");
				 setLinks(request);
				
				 DynaActionForm frmReportList=(DynaActionForm)form;
				 TTKReportManager tTKReportManager = null;
				 tTKReportManager	=	this.getTTKReportManager();
				 String strReportParameter=strRoutineReport;
				 String paymentDurAgr = "";
				 
				 String hospitalSeqId = frmReportList.getString("providername");
				 
				 paymentDurAgr = tTKReportManager.getPaymentTermAgr(hospitalSeqId); 
				 frmReportList.set("paymentTermAgr", paymentDurAgr);
			
				 request.getSession().setAttribute("frmReportList", frmReportList);
				 return this.getForward(strReportParameter,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 } // doChangeProvider
     
     public ActionForward doDisplayActivityLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
 			HttpServletResponse response) throws TTKException{
 		try{
 			log.info("Inside the doDisplayActivityLog() of ReportsAction");
 			setLinks(request);
 			
 			DynaActionForm frmReportList=(DynaActionForm)form;
 			TTKReportManager tTKReportManager = null;
			tTKReportManager	=	this.getTTKReportManager();
			
			 String hospitalSeqId = frmReportList.getString("providername");
			
			TableData  tableData =TTKCommon.getTableData(request); 
			//create the required grid table
			tableData.createTableInfo("FinanceActivityLogTable",null);
			
			
 			ArrayList alPolicy= tTKReportManager.getActivityLogList(hospitalSeqId);
 			tableData.setData(alPolicy,"search");
 			//set the table data object to session
 			request.getSession().setAttribute("tableData",tableData);
 			
 			return mapping.findForward("routineRptActivityLog");
 		}//end of try
 		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 } // end of doDisplayActivityLog()
     
     public ActionForward doCloseActivityLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
 			HttpServletResponse response) throws TTKException{
 		try{
 			log.info("Inside the doCloseActivityLog method of ReportsAction");
 			setLinks(request);
 			return mapping.findForward("closeRoutineRptActivityLog");
 		}//end of try
 		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
 	} // end of ActionForward doCloseActivityLog()
     
 }//end of ReportsAction ()