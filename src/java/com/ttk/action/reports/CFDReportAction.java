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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import net.sf.jasperreports.engine.JRDataSource;
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

 public class CFDReportAction extends TTKAction{

	 private static final Logger log = Logger.getLogger( ReportsAction.class );
	 //declaration of forward paths
	 private static final String strDefaultCFD="defaultCFDReport";
	 private static final String strDefaultGenerateReport="defaultCFDGenerateReport";
	 private static final String strReportdisplay="reportdisplay";
	 private static final String strCFDResultUpload="cfdResultUpload";
	 private static final String strCompaginReportList="compaginReportList";


	 public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 System.out.println("===Inside the doDefault method of CFDReportAction===");
			 setLinks(request);
			 log.debug("Inside the doDefault method of CFDReportAction");

			 return this.getForward(strDefaultCFD,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultCFD));
		 }
	 }
	 

	 public ActionForward doDefaultGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doDefaultGenerateReport method of CFDReportAction");
			 setLinks(request);
			 DynaActionForm frmCFDReportList=(DynaActionForm)form;
			 frmCFDReportList.initialize(mapping);
			 //frmCFDReportList.set("switchType","CLM");
			 request.getSession().setAttribute("frmCFDReportList", frmCFDReportList);
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
	 
	 
	 public ActionForward doCFDResultUpload(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 System.out.println("===Inside the doCFDResultUpload method of CFDReportAction===");
			 log.info("Inside the doCFDResultUpload method of CFDReportAction");
			 setLinks(request);
			 DynaActionForm frmAuditReportList=(DynaActionForm)form;
			 frmAuditReportList.initialize(mapping);
			 return this.getForward(strCFDResultUpload,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strCFDResultUpload));
		 }//end of catch(Exception exp)
	 }
	 
	 
	 public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 System.out.println("===Inside the doClose method of CFDReportAction===");
			 log.debug("Inside the doClose method of ReportAction");
			 setLinks(request);
			 return mapping.findForward(strDefaultCFD);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultCFD));
		 }//end of catch(Exception exp)
	 }
	  
	 
	 public ActionForward doshowTemplate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws TTKException {
		 System.out.println("===Inside the doshowTemplate method of CFDReportAction===");
		 	log.debug("Inside the doshowTemplate method of CFDReportAction");
			ByteArrayOutputStream baos=null;
			OutputStream sos = null;
			FileInputStream fis= null; 
			File file = null;
			BufferedInputStream bis =null;
			try
			{
				setLinks(request);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=CFDInvestigationTemplate.xls");
				String fileName =	TTKPropertiesReader.getPropertyValue("CFDReportDir")+"CFDInvestigationTemplate.xls";
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
//	            return mapping.findForward(strCFDResultUpload);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request,mapping,new TTKException(exp,strCFDResultUpload));
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
					return this.processExceptions(request,mapping,new TTKException(exp,strCFDResultUpload));
				}//                 
			}
		}
	 
	 
	 public ActionForward doUploadCFDInvestigationReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			log.info("Inside the doUploadClaimAuditReport method of AuditReportAction");
			System.out.println("===Inside the doUploadCFDInvestigationReport method of CFDReportAction===");
			setLinks(request);
			// String finalFileName="";
			// String path="";
			 int iCounter=0;
			// int update=0;
			String successYN = null;
			String switchtype = null;
			FileOutputStream outputStream = null;
			String strNotify = null;
			String noOfClaimSettlementNumber = null;
			TTKReportManager tTKReportManager = null;
			Object[] excelData = null;
			DynaActionForm frmCFDReportList = (DynaActionForm) form;
			FormFile formFile = null;
			int fileSize = 3 * 1024 * 1024;
			ArrayList outputData = new ArrayList<>();
			ArrayList<Object> alPrintCheque = new ArrayList<Object>();
			tTKReportManager = this.getTTKReportManager();
			formFile = (FormFile) frmCFDReportList.get("resultUploadFile");
			
			if (formFile == null || formFile.getFileSize() == 0) {
				strNotify = "Please select the xls or xlsx file";
				request.setAttribute("notifyerror", strNotify);
			} else {
				String[] arr = formFile.getFileName().split("[.]");
				String fileType = arr[arr.length - 1];
				if (!("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType))) {
					strNotify = "File Type should be xls or xlsx";
					request.setAttribute("notifyerror", strNotify);
				}
			}

			if (fileSize <= formFile.getFileSize()) {
				strNotify = "File Length Less than 3MB";
				request.setAttribute("notifyerror", strNotify);
			}
			if (strNotify != null && strNotify.length() != 0) {
				return (mapping.findForward(strCFDResultUpload));
			} else {
				String fileName = formFile.getFileName();
				String[] arr = fileName.split("[.]");
				String fileType = arr[arr.length - 1];
				excelData = this.getExcelData(request, formFile, fileType, 9);

				String settlementNo = (String) excelData[0];
				String errorMessage = (String) excelData[2];

				if (!"".equals(errorMessage) && errorMessage != null) {
					request.setAttribute("notifyerror", errorMessage);
				} else if ("0".equals(settlementNo)) {
					strNotify = "Please check the file, We are not able to read the file.";
					// request.getSession().setAttribute("notifyerror",
					// strNotify);
					request.setAttribute("notifyerror", strNotify);
				}
					else{
						noOfClaimSettlementNumber=(String)excelData[0];
						 ArrayList<ArrayList<String>> excelDataRows=(ArrayList<ArrayList<String>>)excelData[1];
						 StringBuilder xmlbuilder = new StringBuilder();
						 String mainxmlElement ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
						 
							 String rootElement="<cfdinvestigationdetails>";
							 String rootendElement="</cfdinvestigationdetails>";
							 
							 String rootSubElement="<investigationdetails>";
							 String rootSubendElement="</investigationdetails>";
							
							 String slNoElement="<slno>";
							 String slNoEndElement="</slno>";
							 
							 String claimPreauthNoElement="<claimpreauthno>";
							 String claimPreauthNoEndElement="</claimpreauthno>";
							 
							 String srcFlagElement="<srcflag>";
							 String srcFlagEndElement="</srcflag>";
							 
							 String invStatusElement="<invstatus>";
							 String invStatusEndElement="</invstatus>";
							 
							 String invOutCatElement="<invoutcat>";
							 String invOutCatEndElement="</invoutcat>";		 
							 
							 String invAmountUtilizedElement="<invamountutilized>";
							 String invAmountUtilizedEndElement="</invamountutilized>";
							 
							 String amtSavedElement="<amtsaved>";
							 String amtSavedEndElement="</amtsaved>";
							 
							 String dateOfReceivingElement="<dateofreceiving>";
							 String dateOfReceivingEndelement="</dateofreceiving>";
							 
							 String cfdRemarksElement="<cfdremarks>";
							 String cfdRemarksEndElement="</cfdremarks>";

							 xmlbuilder= xmlbuilder.append(mainxmlElement).append("\n");
							 xmlbuilder.append(rootElement).append("\n");

							 if(excelDataRows != null && excelDataRows.size() > 0 ){
								 Iterator<ArrayList<String>>rit=excelDataRows.iterator();
								 while(rit.hasNext()){
									 ArrayList<String> rlist=rit.next();
									 xmlbuilder.append(rootSubElement).append("\n");
									 if((rlist!=null)&& rlist.size()>=0){
										 
										 xmlbuilder.append(slNoElement);
										 xmlbuilder.append(rlist.get(0));
										 xmlbuilder.append(slNoEndElement).append("\n");

										 xmlbuilder.append(claimPreauthNoElement);
										 xmlbuilder.append(rlist.get(1));
										 xmlbuilder.append(claimPreauthNoEndElement).append("\n");

										 xmlbuilder.append(srcFlagElement);
										 xmlbuilder.append(rlist.get(2));
										 xmlbuilder.append(srcFlagEndElement).append("\n");

										 xmlbuilder.append(invStatusElement);
										 xmlbuilder.append(rlist.get(3));
										 xmlbuilder.append(invStatusEndElement).append("\n");
										 
										 xmlbuilder.append(invOutCatElement);
										 xmlbuilder.append(rlist.get(4));
										 xmlbuilder.append(invOutCatEndElement).append("\n");
										 
										 xmlbuilder.append(invAmountUtilizedElement);
										 xmlbuilder.append(rlist.get(5));
										 xmlbuilder.append(invAmountUtilizedEndElement).append("\n");
										 
										 xmlbuilder.append(amtSavedElement);
										 xmlbuilder.append(rlist.get(6));
										 xmlbuilder.append(amtSavedEndElement).append("\n");
										 
										 xmlbuilder.append(dateOfReceivingElement);
										 System.out.println("dateOfReceivingElement:::::::::"+rlist.get(7));
										 xmlbuilder.append(rlist.get(7));
										// xmlbuilder.append("27-07-2020");
										 xmlbuilder.append(dateOfReceivingEndelement).append("\n");
										 
										 xmlbuilder.append(cfdRemarksElement);
										 xmlbuilder.append(rlist.get(8));
										 xmlbuilder.append(cfdRemarksEndElement).append("\n");

										 xmlbuilder.append(rootSubendElement).append("\n");
									 } // end of if((strChOpt!=null)&& strChOpt.length!=0)
									 iCounter++;
								 }

							 }	
							 xmlbuilder.append(rootendElement);
							 String xml = xmlbuilder.toString();
							 System.out.println("==========xml Value============");
							 System.out.println(xml);
							 tTKReportManager	=	this.getTTKReportManager();
							 ArrayList inputData=new ArrayList<>();
							 inputData.add(xml);
							 inputData.add(fileName);
							 inputData.add(TTKCommon.getUserSeqId(request));
							 outputData	=	tTKReportManager.uploadCFDReport(inputData);
							 successYN= (String) outputData.get(0);
							 System.out.println("successYN:::::::::::::::Action:::"+successYN);
					}
					 if("Y".equals(successYN))
					 {
						 strNotify="CFD Investigation Uploaded Successfully.";	
						 request.setAttribute("successMsg",strNotify);   
					 }else if("N".equals(successYN)){
							 strNotify="Sorry, File is not uploaded. For more details, Please check the log.";
		          	    	 request.setAttribute("notifyerror",strNotify);
					 }
			 }		 
			 return mapping.findForward(strCFDResultUpload);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strCFDResultUpload));
		 }//end of catch(Exception exp)
	 } 
	 
	 public ActionForward doSearchUploadLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			System.out.println("===Inside the doSearchUploadLog method of CFDReportAction===");
			 log.info("Inside the doSearchUploadLog method of AuditReportAction");
			 setLinks(request);
			 String strActiveLink=TTKCommon.getActiveLink(request);
			 DynaActionForm frmCFDReportList=(DynaActionForm)form;
			 String startDate = frmCFDReportList.getString("logStartDate");
			 String endDate = frmCFDReportList.getString("logEndDate");
			 TTKReportDataSource ttkReportDataSource = null;
			 ArrayList alData	=	null; 
			 ArrayList<String> columnHeader=new ArrayList();
			 ArrayList<String[]> outputData=new ArrayList();
			 ttkReportDataSource = new TTKReportDataSource();
			 ArrayList inputData=new ArrayList();
			 inputData.add(startDate);
			 inputData.add(endDate);
			 alData=ttkReportDataSource.getCFDUploadTemplateErrorLog(inputData);
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
			 return this.processExceptions(request, mapping, new TTKException(exp,strCFDResultUpload));
		 }//end of catch(Exception exp)
	 } 
	 
	 
	 public ActionForward doGenerateCFDReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 String strFlag="CLMPTA";
		 try
		 {
			 log.info("Inside the doGenerateCFDReport method of CFDReportAction");
			 setLinks(request);
			 DynaActionForm frmCFDReportList  =(DynaActionForm) form;
			 ArrayList<String> columnHeader=new ArrayList<>();
			 String fileFormat = ".xls";
			 ByteArrayOutputStream boas = new ByteArrayOutputStream();
			 ArrayList<JasperPrint> reportList = new ArrayList<JasperPrint>();
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			 JasperReport objJasperReport, emptyReport = null;
			 String mainJasperFile = "reports/fraud/CFDReports.jrxml";
			 String campaginJasperFile = "reports/fraud/CampaginReport.jrxml";
			 ArrayList searchData=populateSearchCriteria(frmCFDReportList,request);
			 TTKReportManager tTKReportManager	=	this.getTTKReportManager();
			 ResultSet[] outputData=tTKReportManager.getCFDReport(searchData, "CFDReport");
			 
				 /*columnHeader.add(("Country"));
				 columnHeader.add(("Alerts"));
				 columnHeader.add(("Claim/Preauth No"));
				 columnHeader.add(("Member ID"));
				 columnHeader.add(("Insured Name"));
				 columnHeader.add(("Group Name"));
				 columnHeader.add(("Provider Code"));
				 columnHeader.add(("Provider Name"));
				 columnHeader.add(("Doctor Name"));
				 columnHeader.add(("Source of Referral"));
				 columnHeader.add(("Type of TX"));
				 columnHeader.add(("Detection Date"));
				 columnHeader.add(("Detection Month"));
				 columnHeader.add(("Date Receipt Of Required Document"));
				 columnHeader.add(("Referred By (Internal)"));
				 columnHeader.add(("Status"));
				 columnHeader.add(("Fraud Type"));
				 columnHeader.add(("Date of Decl. as Fraud/Not Fraud"));
				 columnHeader.add(("Fraud Unit Remarks"));
				 columnHeader.add(("Exgratia"));
				 columnHeader.add(("QR"));
				 columnHeader.add(("Claim Gross Amount"));
				 columnHeader.add(("Claim Paid Amount"));
				 columnHeader.add(("Investigation Cost"));
				 columnHeader.add(("Gross Fraud Saving"));
				 columnHeader.add(("Net Fraud Saving"));
				 columnHeader.add(("Saving Month"));
				 columnHeader.add(("Audit Sample"));
				 columnHeader.add(("TAT"));
				 columnHeader.add(("Investigator"));
				// strFlag="CLMPTA";	 
			 request.getSession().setAttribute("columnHeader", columnHeader);
			 request.getSession().setAttribute("sheetColumnData", outputData);
			 request.setAttribute("RepswitchType", strFlag);
			 return mapping.findForward("downloadExcelFile");*/
			 if (outputData[0] != null) {
					JasperPrint jasperPrint = null;
					try (OracleCachedRowSet coversheetRowSet = (OracleCachedRowSet) outputData[0]) {
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
			 
			 if (outputData[1] != null) {
					JasperPrint jasperPrint = null;
					OracleCachedRowSet summary1RowSet = (OracleCachedRowSet) outputData[1];
					if (summary1RowSet.next()) {
						summary1RowSet.beforeFirst();
						while (summary1RowSet.next()) {
						}
				//		System.out.println("This is 2nd cursor");
						objJasperReport = JasperCompileManager.compileReport(campaginJasperFile);
						summary1RowSet.beforeFirst();
						JRDataSource summary1ResultsetDatasource = new JRResultSetDataSource(summary1RowSet);
						jasperPrint = JasperFillManager.fillReport(objJasperReport,hashMap, summary1ResultsetDatasource);
					} else {
						emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
					}
					reportList.add(jasperPrint);
				}
			 if (".xls".equals(fileFormat)) {
					String[] sheetNames = { "CFDReport","CampaginReport"};
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
				 	 response.addHeader("Content-Disposition","attachment; filename=CFD_Report.xls");
				}
			
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 request.setAttribute("RepswitchType", strFlag);
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 request.setAttribute("RepswitchType", strFlag);
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultGenerateReport));
		 }//end of catch(Exception exp)
	 }
	
	
	
	 
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmCFDReportList, HttpServletRequest request)
			throws TTKException {
		//String switchType=frmCFDReportList.getString("switchType");
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
			alSearchParams.add(request.getParameter(("sClaimPreauthNO")));
			alSearchParams.add(request.getParameter(("switchType")));
			alSearchParams.add(request.getParameter(("sAuthorizationNo")));
			alSearchParams.add(request.getParameter(("sBatchNO")));
			alSearchParams.add(request.getParameter(("sProviderName")));
			alSearchParams.add(request.getParameter(("sClaimType")));
			alSearchParams.add(request.getParameter(("sInternalRemarkStatus")));
			alSearchParams.add(request.getParameter(("sSettlementNO")));
			alSearchParams.add(request.getParameter(("sPolicyNumber")));
			alSearchParams.add(request.getParameter(("sPartnerName")));
			alSearchParams.add(request.getParameter(("sSubmissionType")));
			alSearchParams.add(request.getParameter(("sRiskLevel")));
			alSearchParams.add(request.getParameter(("sEnrollmentId")));
			alSearchParams.add(request.getParameter(("sMemberName")));
			alSearchParams.add(request.getParameter(("sBenefitType")));
			alSearchParams.add(request.getParameter(("sInvoiceNo")));
			alSearchParams.add(request.getParameter(("sPolicyType")));
			alSearchParams.add(request.getParameter(("sClaimPreauthStatus")));
			alSearchParams.add(request.getParameter(("sCFDInvestigationStatus")));
			alSearchParams.add(request.getParameter(("sCFDReceivedFromDate")));
			alSearchParams.add(request.getParameter(("sCFDReceivedtoDate")));
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
	           if (workbook.getSheetName(i).equals("CFDInvestigationTemplate")) {
	        	   sheet = workbook.getSheet("CFDInvestigationTemplate");
	        	   break;
	            }else{
	            	errorMessage="Please check the uploaded file does not having sheet name 'CFDInvestigationTemplate'.";
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
      	        System.out.println("columnCount::::::::::"+columnCount);
	 	    	if(columnCount<9){
	 	    		errorMessage="Please check the file, there is some column are missing.";
	 	        	request.getSession().setAttribute("notify", "Please upload proper File.");
				}if(columnCount>9){
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
  					String temp = "";
  					DecimalFormat formatter = new DecimalFormat("0.000");
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
  	                	 for(short i=0;i<=8;i++)
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
  	      		     	                	if (HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)) {
  	      		     	                		temp = new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
  	      		     	                		rowData.add(temp);
  	      		     	                	} else {
  	      		     	                		String strValue = formatter.format(cell.getNumericCellValue());
  	      		     	                		if (i == 7) {
  	      		     	                			Date javaDate = HSSFDateUtil.getJavaDate(Double.parseDouble(strValue));
  	      		     	                			temp = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
  	      		     	                			rowData.add(temp);
  	      		     	                		} else {
  	      		     	                			String values[] = strValue.split("[.]");
  	      		     	                			if (new Integer(values[1]) > 0){
  	      		     	                				temp = strValue;
  	      		     	                				rowData.add(temp);
  	      		     	                			}else{
  	      		     	                				temp = values[0];
  	      		     	                				rowData.add(temp);
  	      		     	                			}
  	      		     	                		}
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
	 
	public ActionForward doChangeFlag(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 try
		 {
			 log.debug("Inside the doDefaultGenerateReport method of CFDReportAction");
			 setLinks(request);
			 DynaActionForm frmCFDReportList=(DynaActionForm)form;
			 String switchType= frmCFDReportList.getString("switchType");
			 frmCFDReportList.initialize(mapping);
			  if(switchType.equalsIgnoreCase("CAM")){
			 frmCFDReportList.set("switchType","CAM");
			  }else{
				  frmCFDReportList.set("switchType",switchType);  
			  }
			 
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
	
	public ActionForward doGenerateCampaginReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 String strFlag="CLMPTA";
		 try
		 {
			 log.info("Inside the doGenerateCFDReport method of CFDReportAction");
			 setLinks(request);
			 DynaActionForm frmCFDReportList  =(DynaActionForm) form;
			 ArrayList<String> columnHeader=new ArrayList<>();
			 String fileFormat = ".xls";
			 ByteArrayOutputStream boas = new ByteArrayOutputStream();
			 ArrayList<JasperPrint> reportList = new ArrayList<JasperPrint>();
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			 JasperReport objJasperReport, emptyReport = null;
			 
			 String campaginJasperFile = "reports/fraud/CampaginReport.jrxml";
			 
				String campName = request.getParameter("campName");
				String flag = request.getParameter("flag");
				String sProviderName = request.getParameter("sProviderName");
				String campStatus = request.getParameter("campStatus");
				String campReceivedFromDate = request.getParameter("campReceivedFromDate");
				String campReceivedToDate = request.getParameter("campReceivedToDate");
				String campStartDate = request.getParameter("campStartDate");
				String campEndDate = request.getParameter("campEndDate");
			 
			 	ArrayList searchData = new ArrayList<>();
			 	searchData.add(campName);
			 	searchData.add(sProviderName);
			 	searchData.add(campStatus);
			 	searchData.add(campReceivedFromDate);
			 	searchData.add(campReceivedToDate);
			 	searchData.add(campStartDate);
			 	searchData.add(campEndDate);
			 	searchData.add(null);
				searchData.add(null);
				searchData.add(null);
				searchData.add(null);
		
			 TTKReportManager tTKReportManager	=	this.getTTKReportManager();
			 ResultSet[] outputData=tTKReportManager.getCampaginReport(searchData, "CampaginReport");
			 
			 if (outputData[0] != null) {
					JasperPrint jasperPrint = null;
					OracleCachedRowSet summary1RowSet = (OracleCachedRowSet) outputData[0];
					if (summary1RowSet.next()) {
						summary1RowSet.beforeFirst();
						while (summary1RowSet.next()) {
						}
				//		System.out.println("This is 1st cursor");
						objJasperReport = JasperCompileManager.compileReport(campaginJasperFile);
						summary1RowSet.beforeFirst();
						JRDataSource summary1ResultsetDatasource = new JRResultSetDataSource(summary1RowSet);
						jasperPrint = JasperFillManager.fillReport(objJasperReport,hashMap, summary1ResultsetDatasource);
					} else {
						emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						jasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
					}
					reportList.add(jasperPrint);
				}
			 if (".xls".equals(fileFormat)) {
					String[] sheetNames = {"CampaginReport"};
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
				 	 response.addHeader("Content-Disposition","attachment; filename=CampaginReport.xls");
				}
			
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 request.setAttribute("RepswitchType", strFlag);
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 request.setAttribute("RepswitchType", strFlag);
			 return this.processExceptions(request, mapping, new TTKException(exp,strDefaultGenerateReport));
		 }//end of catch(Exception exp)
	 }
 }