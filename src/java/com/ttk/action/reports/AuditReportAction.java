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
import java.io.FileWriter;
import java.io.InputStream; 
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.sql.rowset.CachedRowSet;

import jxl.write.VerticalAlignment;
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
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
//import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.reports.TTKReportManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDAOImpl;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dao.impl.reports.TTKReportDataSourceMultiple;
import com.ttk.dto.administration.MOUDocumentVO;
import com.ttk.dto.administration.ReportVO;
import com.ttk.dto.empanelment.HospitalVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

 public class AuditReportAction extends TTKAction{

	 private static final Logger log = Logger.getLogger( ReportsAction.class );
	 //declaration of forward paths
	 private static final String strDefaultAudit="defaultAuditReport";
	 private static final String strDefaultGenerateReport="defaultGenerateReport";
	 private static final String strReportdisplay="reportdisplay";
	 private static final String strAuditResultUpload="auditResultUpload";
	 


	 public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 
			 return this.getForward(strDefaultAudit,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultAudit));
		 }
	 }

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
	


	 public ActionForward doDefaultGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doBatchReportsDetail method of ReportAction");
			 setLinks(request);
			 DynaActionForm frmAuditReportList=(DynaActionForm)form;
			 frmAuditReportList.initialize(mapping);
			 frmAuditReportList.set("switchType","CLM");
			 request.getSession().setAttribute("frmAuditReportList", frmAuditReportList);
			 return this.getForward(strDefaultGenerateReport,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultGenerateReport));
		 }//end of catch(Exception exp)
	 }
	 
	 public ActionForward doSwitchTo(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception{
		 try
		 {
			 setLinks(request);
			 DynaActionForm frmAuditReportList=(DynaActionForm)form;
			 String strSwitchType=TTKCommon.checkNull((String)frmAuditReportList.get("switchType"));
			 frmAuditReportList.initialize(mapping);
			 frmAuditReportList.set("switchType",strSwitchType);
			 request.getSession().setAttribute("frmAuditReportList", frmAuditReportList);
			 return this.getForward(strDefaultGenerateReport, mapping, request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,"policyList"));
		 }//end of catch(Exception exp)
	 }
	 
	 public ActionForward doGenerateAuditReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 String switchType=null;
		 try
		 {
			 log.info("Inside the doGenerateClaimAuditReport method of AuditReportAction");
			 setLinks(request);
			 DynaActionForm frmAuditReportList  =(DynaActionForm) form;
			 switchType=frmAuditReportList.getString("switchType");
			 ByteArrayOutputStream baos=null;
			 OutputStream sos = null;
			 FileInputStream fis= null; 
			 File file = null;
			 BufferedInputStream bis =null;
			 ArrayList<String> columnHeader=new ArrayList<>();
			 ArrayList searchData=populateSearchCriteria(frmAuditReportList,request);
			// System.out.println("searchData::"+searchData);
			 TTKReportManager tTKReportManager	=	this.getTTKReportManager();
			 ArrayList<String[]> outputData=tTKReportManager.getAuditReport(searchData, "AuditReport");
			 if("CLM".equals(switchType)){
				 columnHeader.add(("CLAIM_NUMBER"));
				 columnHeader.add(("INVOICE_NUMBER"));
				 columnHeader.add(("PRE_AUTH_NUMBER"));
				 columnHeader.add(("CLAIM_TYPE"));
				 columnHeader.add(("GENDER"));
				 columnHeader.add(("MEMBER_INCEPTION_DATE"));
				 columnHeader.add(("POLICY_NUMBER"));
				 columnHeader.add(("POLICY_START_DATE"));
				 columnHeader.add(("POLICY_END_DATE"));
				 columnHeader.add(("CORPORATE_NAME"));
				 columnHeader.add(("SUBMISSION_CATAGORY"));
				 columnHeader.add(("CLAIM_SOURCE"));
				 columnHeader.add(("OVERRIDE_REMARKS"));
				 columnHeader.add(("OVERRIDE_DATE"));
				 columnHeader.add(("RECEIVED_DATE"));
				 columnHeader.add(("ADDED_DATE"));
				 columnHeader.add(("COMPLETED_DATE"));
				 columnHeader.add(("CLAIM_COMPLETED_YN"));
				 columnHeader.add(("ADMISSION_DATE"));
				 columnHeader.add(("DISCHARGE_DATE"));
				 columnHeader.add(("PROVIDER_NAME"));
				 columnHeader.add(("ENCOUNTER_TYPE"));
				 columnHeader.add(("BENEFIT_TYPE"));
				 columnHeader.add(("CLAIM_STATUS"));
				 columnHeader.add(("DENIAL_CODE"));
				 columnHeader.add(("DENIAL_DESCRIPTION"));
				 columnHeader.add(("TOOTH_NO"));
				 columnHeader.add(("QUANTITY"));
				 columnHeader.add(("PRIMARY_ICD_CODE"));
				 columnHeader.add(("PRIMARY_ICD_DESC"));
				 columnHeader.add(("SECONDARY_ICD_CODE"));
				 columnHeader.add(("SECONDARY_ICD_DESC"));
				 columnHeader.add(("PRESENTING_COMPLAINTS"));
				 columnHeader.add(("ACTIVITY_CODE"));
				 columnHeader.add(("ACTIVITY_DESCRIPTION"));
				 columnHeader.add(("ACTIVITY_INTERNAL_DESCRIPTION"));
				 columnHeader.add(("REQUESTED_AMOUNT_ACTIVITY"));
				 columnHeader.add(("GROSS_AMOUNT"));
				 columnHeader.add(("DISCOUNT_AMOUNT"));
				 columnHeader.add(("DISC_GROSS_AMOUNT"));
				 columnHeader.add(("PATIENT_SHARE_AMOUNT"));
				 columnHeader.add(("NET_AMOUNT"));
				 columnHeader.add(("DISALLOWED_AMOUNT"));
				 columnHeader.add(("ALLOWED_AMOUNT"));
				 columnHeader.add(("APPROVED_AMOUNT"));
				 columnHeader.add(("FINAL_REMARKS"));
				 columnHeader.add(("MEDICAL_OPINION_REMARKS"));
				 columnHeader.add(("SERVICE_DESCRIPTION"));
				 columnHeader.add(("ACTIVITY_QUANTITY_REQ"));
				 columnHeader.add(("ACTIVITY_QUANTITY_APPROVED"));
				 columnHeader.add(("DATA_ENTRY_BY"));
				 columnHeader.add(("LAST_UPDATED_BY"));
				 columnHeader.add(("PROCESSED_BY"));
				 columnHeader.add(("PRIMARY_NETWORK_TYPE"));
				 columnHeader.add(("ENROLMENT_ID"));
				 columnHeader.add(("MEMBER_NAME"));
				 columnHeader.add(("RELSHIP_DESCRIPTION"));
				 columnHeader.add(("DOB"));
				 columnHeader.add(("MEMBER_AGE"));
				 columnHeader.add(("PROVIDER_CATEGORY"));
				 columnHeader.add(("SETTLEMENT_NUMBER"));
				 columnHeader.add(("CHEQUE_NUMBER"));
				 columnHeader.add(("CHEQUE_DATE"));
				 columnHeader.add(("OVERRIDE_YN"));
				 columnHeader.add(("SERVICE_NAME"));
				 columnHeader.add(("PROVIDER_COUNTRY"));
				 columnHeader.add(("CLM_REQUESTED_AMOUNT_QAR"));
				 columnHeader.add(("CLM_APPROVED_AMOUNT_QAR"));
				 columnHeader.add(("TOT_DISALLOWED_AMOUNT"));
				 columnHeader.add(("INTERNAL_REMARK_STATUS"));
				 columnHeader.add(("INTERNAL_REMARK_DESC"));
				 columnHeader.add(("CLINICIAN_ID"));
				 columnHeader.add(("CLINICIAN_NAME"));
				 columnHeader.add(("ACTIVITY_TYPE"));
				 columnHeader.add(("SUM_INSURED"));
				 columnHeader.add(("AVA_SUM_INSURED"));
				 columnHeader.add(("UTILISED_SUM_INSURED"));
				 columnHeader.add(("AUDIT STATUS"));
				 columnHeader.add(("AUDIT FINDINGS"));
				 columnHeader.add(("AUDITED DATE AND TIME"));
				 columnHeader.add(("CLAIMS REMARKS TO AUDIT"));
				 columnHeader.add(("TPA_DENIAL_CODE"));
				 columnHeader.add(("TPA_DENIAL_DESC"));
				 columnHeader.add(("PROVIDER NETWORK CATEGORY"));
				 // Part 1:
				 columnHeader.add(("REMARKS_FOR_THE_PRO_MEMBER"));
				 columnHeader.add(("INTERNAL_REMARKS"));
				 columnHeader.add(("PROVIDER_SPECIFIC_REMARKS"));
				 columnHeader.add(("MEMBER_SPECIFIC_POLICY_REMARKS"));
				 columnHeader.add(("SUBMISSION_TYPE"));
				 columnHeader.add(("BATCH_NO"));
				 columnHeader.add(("MARITAL_STATUS"));
				 columnHeader.add(("REQ_AMOUNT_ORIGINAL"));
				 columnHeader.add(("APR_AMOUNT_ORIGINAL"));
				 columnHeader.add(("ORIGINAL_CURRENCY"));
				 columnHeader.add(("CLAIM_COMPLETED_DATE_TIME"));
				 columnHeader.add(("SPECIALITY"));
				 columnHeader.add(("CONSULTANT_TYPE"));
				 columnHeader.add(("Count"));
				 
				// 2nd part
				columnHeader.add("No of Encounters of Activity");
				columnHeader.add("No of times Approved");
				columnHeader.add("No of times Rejected");
				columnHeader.add("Same activity code in same claim no.");
				columnHeader.add("Same activity code  with same service date");
			//	columnHeader.add("Consultation with same service date and same ICD.(principal ICD)");
				columnHeader.add("Activity status-Rejected/Approve");
				}else{
					columnHeader.add("PREAUTH_NUMBER");
					columnHeader.add("CLAIM_NUMBER");
					columnHeader.add("MEMBER_INCEPTION_DATE");
					columnHeader.add("POLICY_NUMBER");
					columnHeader.add("POLICY_START_DATE");
					columnHeader.add("POLICY_END_DATE");
					columnHeader.add("CORPORATE_NAME");
					columnHeader.add("SOURCE_TYPE");
					columnHeader.add("PREAUTH_RECEIVED_DATE");
					columnHeader.add("LAST_UPDATED_DATE");
					columnHeader.add("PREAUTH_COMPLETED_DATE");
					columnHeader.add("PROVIDER_NAME");
					columnHeader.add("ENCOUNTER_TYPE");
					columnHeader.add("BENEFIT_TYPE");
					columnHeader.add("OVERRIDE_DATE");
					columnHeader.add("OVERRIDE_REMARKS");
					columnHeader.add("DENIAL_CODE");
					columnHeader.add("DENIAL_DESCRIPTION");
					columnHeader.add("TOOTH_NO");
					columnHeader.add("QUANTITY");
					columnHeader.add("PA_STATUS");
					columnHeader.add("PRIMARY_ICD_CODE");
					columnHeader.add("PRIMARY_ICD_DESC");
					columnHeader.add("SECONDARY_ICD_CODE");
					columnHeader.add("SECONDARY_ICD_DESC");
					columnHeader.add("ACTIVITY_TYPE");
					columnHeader.add("ACTIVITY_CODE");
					columnHeader.add("ACTIVITY_DESCRIPTION");
					columnHeader.add("ACTIVITY_INTERNAL_DESCRIPTION");
					columnHeader.add("REQUESTED_AMOUNT_ACTIVITY");
					columnHeader.add("GROSS_AMOUNT");
					columnHeader.add("DISCOUNT_AMOUNT");
					columnHeader.add("DISC_GROSS_AMOUNT");
					columnHeader.add("PATIENT_SHARE_AMOUNT");
					columnHeader.add("NET_AMOUNT");
					columnHeader.add("DISALLOWED_AMOUNT");
					columnHeader.add("ALLOWED_AMOUNT");
					columnHeader.add("APPROVED_AMOUNT");
					columnHeader.add("PA_REQ_AMOUNT_QAR");
					columnHeader.add("PA_APPROVED_AMOUNT_QAR");
					columnHeader.add("TOT_DISALLOWED_AMOUNT");
					columnHeader.add("CLM_REQUESTED_AMOUNT_QAR");
					columnHeader.add("CLM_APPROVED_AMOUNT_QAR");
					columnHeader.add("ASSIGNED_TO");
					columnHeader.add("LAST_UPDATED_BY");
					columnHeader.add("PROCESSED_BY");
					columnHeader.add("ENROLMENT_ID");
					columnHeader.add("MEMBER_NAME");
					columnHeader.add("RELSHIP_DESCRIPTION");
					columnHeader.add("MEMBER_AGE");
					columnHeader.add("GENDER");
					columnHeader.add("PROVIDER_CATEGORY");
					columnHeader.add("OVERRIDE_YN");
					columnHeader.add("SERVICE_NAME");
					columnHeader.add("ACTIVITY_QUANTITY_REQ");
					columnHeader.add("ACTIVITY_QUANTITY_APPROVED");
					columnHeader.add("MEDICAL_OPINION_REMARKS");
					columnHeader.add("CLINICIAN_ID");
					columnHeader.add("CLINICIAN_NAME");
					columnHeader.add("TPA_DENIAL_CODE");
					columnHeader.add("TPA_DENIAL_DESC");
					columnHeader.add("PROVIDER NETWORK CATEGORY");
					columnHeader.add("SUM_INSURED");
					columnHeader.add("AVA_SUM_INSURED");
					columnHeader.add("UTILISED_SUM_INSURED");
					// Part 1:
					columnHeader.add("REQ_AMOUNT_ORIGINAL");
					columnHeader.add("APR_AMOUNT_ORIGINAL");
					columnHeader.add("ORIGINAL_CURRENCY");
					columnHeader.add("PA_COMPLETED_DATE_TIME");
					columnHeader.add("Count");
					columnHeader.add("SPECIALITY");
					columnHeader.add("Consultant_Type");
					
					// 2nd part
					columnHeader.add("No of Encounters of Activity");
					columnHeader.add("No of times Approved");
					columnHeader.add("No of times Rejected");
					columnHeader.add("Same activity code in same claim no.");
					columnHeader.add("Same activity code  with same service date");
				//	columnHeader.add("Consultation with same service date and same ICD.(principal ICD)");
					columnHeader.add("Activity status-Rejected/Approve");
				}
			 request.getSession().setAttribute("columnHeader", columnHeader);
			 request.getSession().setAttribute("sheetColumnData", outputData);
			 request.setAttribute("RepswitchType", switchType);
			 return mapping.findForward("downloadExcelFile");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 request.setAttribute("RepswitchType", switchType);
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 request.setAttribute("RepswitchType", switchType);
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultGenerateReport));
		 }//end of catch(Exception exp)
	 }
	 public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doBatchReportsDetail method of ReportAction");
			 setLinks(request);
			 return mapping.findForward(strDefaultAudit);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultAudit));
		 }//end of catch(Exception exp)
	 }
	 
	 public ActionForward doAuditResultUpload(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.info("Inside the doAuditResultUpload method of AuditReportAction");
			 setLinks(request);
			 DynaActionForm frmAuditReportList=(DynaActionForm)form;
			 frmAuditReportList.initialize(mapping);
			 return this.getForward(strAuditResultUpload,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strAuditResultUpload));
		 }//end of catch(Exception exp)
	 }
	 
	 
	 
	 public ActionForward doshowTemplate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws TTKException {
		 	log.debug("Inside ChequeSearchAction class  doshowChequeTemplate");
			ByteArrayOutputStream baos=null;
			OutputStream sos = null;
			FileInputStream fis= null; 
			File file = null;
			BufferedInputStream bis =null;
			try
			{
				setLinks(request);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=ClaimAuditTemplate.xls");
				String fileName =	TTKPropertiesReader.getPropertyValue("AuditReportDir")+"ClaimAuditTemplate.xls";
				file = new File(fileName);
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				baos=new ByteArrayOutputStream();
				int ch;
				while ((ch = bis.read()) != -1) baos.write(ch);
				sos = response.getOutputStream();
				baos.writeTo(sos);  
				baos.flush();     
				baos.close();
				sos.flush(); 
				sos.close();
				request.setAttribute("boas", baos);
	            return mapping.findForward(strReportdisplay);
//	            return mapping.findForward(strAuditResultUpload);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request,mapping,new TTKException(exp,strAuditResultUpload));
			}//end of catch(Exception exp)
			finally{
				try{
					if(baos!=null)baos.close();                                           
					if(sos!=null)sos.close();
					if(bis!=null)bis.close();
					if(fis!=null)fis.close();

				}
				catch(Exception exp)
				{
					return this.processExceptions(request,mapping,new TTKException(exp,strAuditResultUpload));
				}//                 
			}
		}
	 
	 
	 public ActionForward doUploadClaimAuditReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.info("Inside the doUploadClaimAuditReport method of AuditReportAction");

			 log.debug("Inside ReportAction class  doAuditUploadResultClaimPreAuth method");
			 setLinks(request);
			 String finalFileName="";
			 String path="";
			 int iCounter=0;
			 int update=0;
			 String successYN=null;
			 String switchtype=null;
			 FileOutputStream outputStream = null;
			 String strNotify = null;
			 String noOfClaimSettlementNumber=null;
			 TTKReportManager tTKReportManager = null;
			 Object[] excelData=null;
			 DynaActionForm frmAuditReportList=(DynaActionForm)form;
			 FormFile formFile = null;
			 int fileSize = 3*1024*1024;
			 ArrayList outputData=new ArrayList<>();
			 ArrayList<Object> alPrintCheque = new ArrayList<Object>();
			 tTKReportManager	=	this.getTTKReportManager();
			 formFile = (FormFile)frmAuditReportList.get("resultUploadFile");
			 if(formFile==null||formFile.getFileSize()==0)
			 {
				 strNotify="Please select the xls or xlsx file";
				 request.setAttribute("notifyerror", strNotify);
			 }
			 else
			 {
				 String[] arr=formFile.getFileName().split("[.]");
				 String fileType=arr[arr.length-1];
				 if(!("xls".equalsIgnoreCase(fileType)||"xlsx".equalsIgnoreCase(fileType)))
				 {
					 strNotify="File Type should be xls or xlsx";	        	  
					 request.setAttribute("notifyerror", strNotify);           
				 }
			 }
			 
			 if(fileSize<=formFile.getFileSize())
			 {
				 strNotify="File Length Less than 3MB";	        	  
				 request.setAttribute("notifyerror", strNotify);
			 }
			 if(strNotify!=null && strNotify.length()!=0)
			 {
				 return (mapping.findForward(strAuditResultUpload));
			 }
			 else
			 {

				 String fileName=formFile.getFileName();
				 String[] arr=fileName.split("[.]");
				 String fileType=arr[arr.length-1];
				 excelData=this.getExcelData(request,formFile,fileType,9);
				 
				 String settlementNo=(String) excelData[0];
				 String errorMessage=(String) excelData[2];
				
					if(!"".equals(errorMessage)&&errorMessage!=null){
						 request.setAttribute("notifyerror", errorMessage);
					} else if("0".equals(settlementNo)){
						strNotify="Please check the file, We are not able to read the file.";	        	  
						// request.getSession().setAttribute("notifyerror", strNotify);
						 request.setAttribute("notifyerror", strNotify);
					}
					else{
						noOfClaimSettlementNumber=(String)excelData[0];
						 ArrayList<ArrayList<String>> excelDataRows=(ArrayList<ArrayList<String>>)excelData[1];
						 StringBuilder xmlbuilder = new StringBuilder();
						 String mainxmlElement ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
							 String rootElement="<claimdetails>";
							 String rootendElement="</claimdetails>";
							 String rootSubElement="<claim>";
							 String rootSubendElement="</claim>";
							 String claimnumberelement="<claimnumber>";
							 String claimnumberendelement="</claimnumber>";
							 String Claimsettlementnumberelement="<settlementnumber>";
							 String Claimsettlementnumberendelement="</settlementnumber>";
							 String suminsuredelement="<totalapprovedamount>";
							 String suminsuredendelement="</totalapprovedamount>";
							 String statuselement="<status>";
							 String statusendelement="</status>";
							 String auditRemarks="<auditremarks>";
							 String auditEndRemarks="</auditremarks>";

							 xmlbuilder= xmlbuilder.append(mainxmlElement).append("\n");
							 xmlbuilder.append(rootElement).append("\n");

							 if(excelDataRows != null && excelDataRows.size() > 0 )
							 {

								 Iterator<ArrayList<String>>rit=excelDataRows.iterator();

								 while(rit.hasNext())
								 {
									 ArrayList<String> rlist=rit.next();
									 xmlbuilder.append(rootSubElement).append("\n");
									 if((rlist!=null)&& rlist.size()>=0)
									 {

										 xmlbuilder.append(claimnumberelement);
										 xmlbuilder.append(rlist.get(0));
										 xmlbuilder.append(claimnumberendelement).append("\n");



										 xmlbuilder.append(Claimsettlementnumberelement);
										 xmlbuilder.append(rlist.get(1));
										 xmlbuilder.append(Claimsettlementnumberendelement).append("\n");



										 xmlbuilder.append(suminsuredelement);
										 xmlbuilder.append(rlist.get(2));
										 xmlbuilder.append(suminsuredendelement).append("\n");



										 xmlbuilder.append(statuselement);
										 xmlbuilder.append(rlist.get(3));
										 xmlbuilder.append(statusendelement).append("\n");
										 xmlbuilder.append(auditRemarks);
										 xmlbuilder.append(rlist.get(4));
										 xmlbuilder.append(auditEndRemarks).append("\n");

										 xmlbuilder.append(rootSubendElement).append("\n");
									 } // end of if((strChOpt!=null)&& strChOpt.length!=0)
									 iCounter++;
								 }

							 }	
							 xmlbuilder.append(rootendElement);
							 String xml = xmlbuilder.toString();
							 tTKReportManager	=	this.getTTKReportManager();
							 ArrayList inputData=new ArrayList<>();
							 inputData.add(xml);
							 inputData.add(fileName);
							 inputData.add(TTKCommon.getUserSeqId(request));
							 outputData	=	tTKReportManager.uploadClaimAuditReport(inputData);
							 successYN= (String) outputData.get(0);
					}
					 if("Y".equals(successYN))
					 {
						 strNotify="Claim Audit template uploaded successfully.";	
						 request.setAttribute("successMsg",strNotify);   
					 }else if("N".equals(successYN)){
							 strNotify="Sorry, File is not uploaded. For more details, Please check the log.";
		          	    	 request.setAttribute("notifyerror",strNotify);
					 }
			 }		 
			 return mapping.findForward(strAuditResultUpload);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strAuditResultUpload));
		 }//end of catch(Exception exp)
	 }
	 public ActionForward doSearchUploadLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.info("Inside the doSearchUploadLog method of AuditReportAction");
			 setLinks(request);
			 setLinks(request);
			 String strActiveLink=TTKCommon.getActiveLink(request);
			 DynaActionForm frmAuditReportList=(DynaActionForm)form;
			 String startDate = frmAuditReportList.getString("logStartDate");
			 String endDate = frmAuditReportList.getString("logEndDate");
			 TTKReportDataSource ttkReportDataSource = null;
			 ArrayList alData	=	null; 
			 ArrayList<String> columnHeader=new ArrayList();
			 ArrayList<String[]> outputData=new ArrayList();
			 ttkReportDataSource = new TTKReportDataSource();
			 ArrayList inputData=new ArrayList();
			 inputData.add(startDate);
			 inputData.add(endDate);
			 alData=ttkReportDataSource.getaudituploadtemplateerrorlog(inputData);
			 if(alData.size()==2){
				 columnHeader=(ArrayList<String>)alData.get(0);
				 outputData=(ArrayList<String[]>) alData.get(1);
				 request.getSession().setAttribute("columnHeader", columnHeader);
				 request.getSession().setAttribute("sheetColumnData", outputData); 
			 }
			 if(alData.size()==1){
				 outputData=(ArrayList<String[]>) alData.get(0);
				 request.getSession().setAttribute("sheetColumnData", outputData); 
			 }
			 request.getSession().setAttribute("flag", "errorlog");
			 return mapping.findForward("downloadExcelFile");
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strAuditResultUpload));
		 }//end of catch(Exception exp)
	 }
	 
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmAuditReportList, HttpServletRequest request)
			throws TTKException {
		String switchType=frmAuditReportList.getString("switchType");
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		if("CLM".equals(switchType))
		alSearchParams.add(request.getParameter(("auditStatus")));
		alSearchParams.add(request.getParameter(("sProviderName")));
		if("CLM".equals(switchType)){
		alSearchParams.add(request.getParameter(("sBatchNO")));
		alSearchParams.add(request.getParameter(("sClaimNO")));
		alSearchParams.add(request.getParameter(("sSettlementNO")));
		}if("PAT".equals(switchType)){
			alSearchParams.add(request.getParameter(("preauthNumber")));
			alSearchParams.add(request.getParameter(("authorizationNo")));
		}
		alSearchParams.add(request.getParameter(("sClaimantName")));
		alSearchParams.add(request.getParameter(("sEnrollmentId")));
		alSearchParams.add(request.getParameter(("sStatus")));
		alSearchParams.add(request.getParameter(("sPolicyNumber")));
//		alSearchParams.add(request.getParameter(("productName")));
		if("CLM".equals(switchType)){
		alSearchParams.add(request.getParameter(("claimRecvdFrmDate")));
		alSearchParams.add(request.getParameter(("claimRecvdToDate")));
		alSearchParams.add(request.getParameter(("claimProcessedFrmDate")));
		alSearchParams.add(request.getParameter(("claimProcessedToDate")));
		alSearchParams.add(request.getParameter(("sClaimType")));
		alSearchParams.add(request.getParameter(("claimTreatmentFrmDate")));
		alSearchParams.add(request.getParameter(("claimTreatmentToDate")));
		}if("PAT".equals(switchType)){
			alSearchParams.add(request.getParameter(("preauthRecvdFrmDate")));
			alSearchParams.add(request.getParameter(("preauthRecvdToDate")));
			alSearchParams.add(request.getParameter(("preauthProcessedFrmDate")));
			alSearchParams.add(request.getParameter(("preauthProcessedToDate")));
			alSearchParams.add(request.getParameter(("preauthTreatmentFrmDate")));
			alSearchParams.add(request.getParameter(("preauthTreatmentToDate")));
		}
		alSearchParams.add(switchType);
		return alSearchParams;
	}
	
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
	private Object[] getExcelData(HttpServletRequest request,FormFile formFile, String fileType, int column) throws FileNotFoundException, IOException {
		InputStream fis=null;
    	HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
    	Reader reader		=	null;
    	Object object[]=new Object[3];
    	String errorMessage=null;
    	int numclaimsettlementnumber=0;
 	     ArrayList<ArrayList<String>> excelDatar=new ArrayList<>();
		FileWriter fileWriter=	null;
		HSSFWorkbook workbook = null;
		fis = formFile.getInputStream(); 
		workbook =  (HSSFWorkbook) new HSSFWorkbook(fis);
    	  //log("xls="+wb_hssf.getSheetName(0));
		
		if (workbook.getNumberOfSheets() != 0) {
	        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	           if (workbook.getSheetName(i).equals("ClaimAuditTemplate")) {
	        	   sheet = workbook.getSheet("ClaimAuditTemplate");
	        	   break;
	            }else{
	            	errorMessage="Please check the uploaded file does not having sheet name 'ClaimAuditTemplate'.";
	            	break;
	            } 
	        }
	    }
    	
 	        //Initializing the XML document
 	    	final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
 	        if(sheet==null){
 	        	errorMessage="Please upload proper File";
 	        	request.getSession().setAttribute("notify", "Please upload proper File.");
 	        }
 	        else
 	        {
 	        	  Iterator<?> rows     = sheet.rowIterator ();
 	               ArrayList<String> excelDatac;
 	               
      	        while (rows.hasNext ()) 
      	        {
      	        HSSFRow row =  (HSSFRow) rows.next();
      	        int columnCount=sheet.getRow(0).getLastCellNum();
	 	    	if(columnCount<5){
	 	    		errorMessage="Please check the file, there is some column are missing.";
	 	        	request.getSession().setAttribute("notify", "Please upload proper File.");
				}if(columnCount>5){
	 	    		errorMessage="Please check the file, there is some excess column.";
	 	        	request.getSession().setAttribute("notify", "Please upload proper File.");
				}else{
      	            if(row.getRowNum()==0){
      	            	boolean checkHeader=false;
      	            	for (short c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
      	            		HSSFCell cell	=	row.getCell(c);
    	                      cell = row.getCell(c);
    	                      if (cell == null || (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)) {
    	                    	  checkHeader=true;
    	                      }else if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
    	                    	  String richTextString = cell.getStringCellValue().trim();
  		     	                	richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
  		     	                	if("".equals(richTextString))
  		     	                		checkHeader=true;
    	                      }
      	            	}
      	            	if(checkHeader==true)
      	            		errorMessage="Please check the file, there is some column are missing.";
      	            	else
      	            	continue;
      	            }
      	            	
      	           // Iterator<?> cells = row.cellIterator (); 
      	            ArrayList<String> rowData = new ArrayList<String>();
      	          boolean nonBlankRowFound = false;
  	            	int countEmptySpace=0;
  	            	for (short c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
  	            		HSSFCell cell	=	row.getCell(c);
  	                      cell = row.getCell(c);
//  	                      System.out.println(cell.getStringCellValue());
  	                      if (cell == null || (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)) {
  	                        countEmptySpace++;
  	                      }
  	                      if(countEmptySpace==(row.getLastCellNum())){
  	                    	nonBlankRowFound=true;
  	                      }
  	                  }
  	                   if (nonBlankRowFound == false) {
  	                	 for(short i=0;i<=4;i++)
  	      	            {
  	      	            	HSSFCell cell	=	row.getCell(i);
  	      	            	
  	      	                	if(cell==null)
  	          	            		rowData.add("");
  	          	            	else
  	          	            		{ 
  	          	            		switch (cell.getCellType ())
  	      	     	                {
  	      		     	                case HSSFCell.CELL_TYPE_NUMERIC :
  	      		     	                {
  	      		     	                	if(i==0||i==1){
  	      		     	                	rowData.add((int)cell.getNumericCellValue() + "");
  	      		     	                	}else{
  	      		     	                	rowData.add(cell.getNumericCellValue() + "");
  	      		     	                	}
  	      		     	                    break;
  	      		     	                }
  	      		     	                case HSSFCell.CELL_TYPE_STRING :
  	      		     	                {
  	      		     	                	
  	      		     	                  	String richTextString = cell.getStringCellValue().trim();
  	      		     	                	richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
  	      		     	                    rowData.add(richTextString);
  	      		     	                    break;
  	      		     	                }
  	      		     	                case HSSFCell.CELL_TYPE_BLANK :
  	      		     	                {	
  	      		     	                	String blankCell	=	cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
  	      		     	                	rowData.add(blankCell);
  	      		     	                	break;
  	      		     	                }
  	      		     	                default:
  	      		     	                {
  	      		     	                    // types other than String and Numeric.
  	      		     	                    System.out.println ("Type not supported.");
  	      		     	                    break;
  	      		     	                }
  	      	     	                } // end switch
  	      	            		}//else 
  	      	                 /* }*/
  	      	            }//for
  	                   }   
  	                 if (nonBlankRowFound == false) {
  	                	 excelDatar.add(rowData);//adding Excel data to ArrayList
  	                	 numclaimsettlementnumber++;
  	                 }
				}
      	        } //end while
 	        }
 	        object[0]=numclaimsettlementnumber+"";//adding no. of policies
			object[1]=excelDatar;//adding all rows data
			object[2]=errorMessage;
 		     return object;
	}
	 
 }