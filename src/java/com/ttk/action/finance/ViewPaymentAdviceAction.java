/**
 * @ (#) Viewlog.debug Oct 27, 2006
 * Project      : TTK HealthCare Services
 * File         : Viewlog.debug
 * Author       : Arun K.M
 * Company      : Span Systems Corporation
 * Date Created : Oct 27, 2006
 *
 * @author       : Arun K.M
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */

package com.ttk.action.finance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.asprise.util.pdf.ex;
import com.ibm.icu.text.SimpleDateFormat;
import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.finance.FloatAccountManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.ChequeVO;
import com.ttk.dao.impl.reports.TTKReportDataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
/**
 * This class is used for searching of List of payments.
 * This class also allows to view the xl report
 */
public class ViewPaymentAdviceAction extends TTKAction
{
    private static Logger log = Logger.getLogger(ViewPaymentAdviceAction.class );
    //Modes of Float List
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    //Action mapping forwards
    private static final String strclaimslistdetails="claimslistdetails";
    private static final String strReportdisplay="reportdisplay";
    private static final String strBank="bank";

  /**
     * This method is used to initialize the search grid.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ViewPaymentAdviceAction doDefault");
    		if(TTKCommon.getWebBoardId(request) == null)
            {
                TTKException expTTK = new TTKException();
                expTTK.setMessage("error.floatno.required");
                throw expTTK;
            }//end of if(TTKCommon.getWebBoardId(request) == null)
    		TableData  tableData =TTKCommon.getTableData(request);
    		//create new table data object
    		tableData = new TableData();
    		//create the required grid table
    		tableData.createTableInfo("PaymentBatchTable",new ArrayList());
    		request.getSession().setAttribute("tableData",tableData);
    		((DynaActionForm)form).initialize(mapping);//reset the form data
    		request.getSession().setAttribute("searchparam", null);
    		return this.getForward(strclaimslistdetails, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
    	}//end of catch(Exception exp)
    }//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to get the details of the selected record from web-board.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	log.debug("Inside ViewPaymentAdviceAction doChangeWebBoard");
    	return doDefault(mapping,form,request,response);
    }//end of ChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to navigate to detail screen to edit selected record.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doViewPaymentXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.info("Inside ViewPaymentAdviceAction doViewPaymentXL");
    		log.debug("Inside ViewPaymentAdviceAction doViewPaymentXL");
    		return (mapping.findForward(strReportdisplay));
    	}
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
    	}//end of catch(Exception exp)
    }//end of doViewPaymentXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to navigate to detail screen to edit selected record.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doViewFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.info("Inside ViewPaymentAdviceAction doViewFile");
    		String strBatchFileName = "";
    		String strDEFLBatchFileName="";
    		String strALKLBatchFileName="";
    		String strALKFBatchFileName="";
    		String strQNBABatchFileName="";
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		TableData  tableData =TTKCommon.getTableData(request);
    		strBatchFileName = ((ChequeVO)tableData.getRowInfo(Integer.parseInt(
    															request.getParameter("rownum")))).getFileName();
    		DynaActionForm frmFloatAccDetails=(DynaActionForm) request.getSession().getAttribute("frmFloatAccDetails");
    		String strNewFile = null;
    		String strAlternateFileName=null;
    		request.setAttribute("alternateFileName",null);
    		
    		long enbdCount=floatAccountManagerObject.getENBDCountandAccNum(Long.parseLong( (String) frmFloatAccDetails.get("floatAcctSeqID")),strBatchFileName);
//    		doSearch(mapping, form, request, response);
    		
    		if(strBatchFileName.startsWith("ENBD"))
    		{
    		if(strBatchFileName.endsWith("Consolidated"))
    		{
    			request.setAttribute("alternateFileName",strBatchFileName+".xls");
    			strBatchFileName=strBatchFileName.substring(0,24);
    			strBatchFileName=strBatchFileName+"Consolidated";
    			
    		}
    		else if (strBatchFileName.endsWith("Detail")) {
    			request.setAttribute("alternateFileName",strBatchFileName+".xls");
    			strBatchFileName=strBatchFileName.substring(0,24);
    			strBatchFileName=strBatchFileName+"Detail";
			}
    		else {
    			request.setAttribute("alternateFileName",strBatchFileName+".txt");
    			strBatchFileName=strBatchFileName.substring(0,24);
			}
    	
    		}// End of 	if(strBatchFileName.startsWith("ENBD"))
    		
    		else if(strBatchFileName.startsWith("DEFL"))
    		{
    			strDEFLBatchFileName=strBatchFileName;
    			if(strBatchFileName.contains("SummaryPDF"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryPDF");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    			if(strBatchFileName.contains("SummaryEXCEL"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryEXCEL");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    		
    		 if (strBatchFileName.endsWith("Detail")) {
    			request.setAttribute("alternateFileName",strBatchFileName+".xls");
    			strBatchFileName=strBatchFileName.substring(0,24);
    			strBatchFileName=strBatchFileName+"Detail";
			}
    		 else if (strBatchFileName.endsWith("CSV")) {
     			request.setAttribute("alternateFileName",strBatchFileName+".csv");
     			strBatchFileName=strBatchFileName.substring(0,24);
     			strBatchFileName=strBatchFileName+"CSV";
 			}
    		 else if (strBatchFileName.endsWith("Consolidated")) {
      			request.setAttribute("alternateFileName",strBatchFileName+".xls");
      			strBatchFileName=strBatchFileName.substring(0,24);
      			strBatchFileName=strBatchFileName+"Consolidated";
  			}
    		else {
    			request.setAttribute("alternateFileName",strBatchFileName+".txt");
    			strBatchFileName=strBatchFileName.substring(0,24);
			}
    		
    		}// End of 	else if(strBatchFileName.startsWith("DEFL"))
    		else if(strBatchFileName.startsWith("ALKL"))
    		{
    			strALKLBatchFileName=strBatchFileName;
    			if(strBatchFileName.contains("SummaryPDF"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryPDF");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    			if(strBatchFileName.contains("SummaryEXCEL"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryEXCEL");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    		
    		 if (strBatchFileName.endsWith("Detail")) {
    			request.setAttribute("alternateFileName",strBatchFileName+".xls");
    			strBatchFileName=strBatchFileName.substring(0,24);
    			strBatchFileName=strBatchFileName+"Detail";
			}
    		 else if (strBatchFileName.endsWith("CSV")) {
     			request.setAttribute("alternateFileName",strBatchFileName+".csv");
     			strBatchFileName=strBatchFileName.substring(0,24);
     			strBatchFileName=strBatchFileName+"CSV";
 			}
    		 else if (strBatchFileName.endsWith("Consolidated")) {
      			request.setAttribute("alternateFileName",strBatchFileName+".xls");
      			strBatchFileName=strBatchFileName.substring(0,24);
      			strBatchFileName=strBatchFileName+"Consolidated";
  			}
    		else {
    			request.setAttribute("alternateFileName",strBatchFileName+".txt");
    			strBatchFileName=strBatchFileName.substring(0,24);
			}
    		
    		}//End Of 	else if(strBatchFileName.startsWith("ALKL"))
    		else if(strBatchFileName.startsWith("ALKF"))
    		{
    			strALKFBatchFileName=strBatchFileName;
    			if(strBatchFileName.contains("SummaryPDF"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryPDF");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    			if(strBatchFileName.contains("SummaryEXCEL"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryEXCEL");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    		
    		 if (strBatchFileName.endsWith("Detail")) {
    			request.setAttribute("alternateFileName",strBatchFileName+".xls");
    			strBatchFileName=strBatchFileName.substring(0,24);
    			strBatchFileName=strBatchFileName+"Detail";
			}
    		 else if (strBatchFileName.endsWith("CSV")) {
     			request.setAttribute("alternateFileName",strBatchFileName+".csv");
     			strBatchFileName=strBatchFileName.substring(0,24);
     			strBatchFileName=strBatchFileName+"CSV";
 			}
    		 else if (strBatchFileName.endsWith("Consolidated")) {
      			request.setAttribute("alternateFileName",strBatchFileName+".xls");
      			strBatchFileName=strBatchFileName.substring(0,24);
      			strBatchFileName=strBatchFileName+"Consolidated";
  			}
    		else {
    			request.setAttribute("alternateFileName",strBatchFileName+".txt");
    			strBatchFileName=strBatchFileName.substring(0,24);
			}
    		
    		}//End Of 	else if(strBatchFileName.startsWith("ALKF"))
    		else if(strBatchFileName.startsWith("QNBA"))
    		{
    			strQNBABatchFileName=strBatchFileName;
    			if(strBatchFileName.contains("SummaryPDF"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryPDF");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    			if(strBatchFileName.contains("SummaryEXCEL"))
    			{
    				int lenbatchfile=strBatchFileName.length();
    				int indexconsolidated =strBatchFileName.indexOf("SummaryEXCEL");
    				strBatchFileName = strBatchFileName.substring(0, indexconsolidated);
    				strBatchFileName=strBatchFileName+"-Detail";
    			}
    		
    		 if (strBatchFileName.endsWith("Detail")) {
    			request.setAttribute("alternateFileName",strBatchFileName+".xls");
    			strBatchFileName=strBatchFileName.substring(0,24);
    			strBatchFileName=strBatchFileName+"Detail";
			}
    		 else if (strBatchFileName.endsWith("CSV")) {
     			request.setAttribute("alternateFileName",strBatchFileName+".csv");
     			strBatchFileName=strBatchFileName.substring(0,24);
     			strBatchFileName=strBatchFileName+"CSV";
 			}
    		 else if (strBatchFileName.endsWith("Consolidated")) {
      			request.setAttribute("alternateFileName",strBatchFileName+".xls");
      			strBatchFileName=strBatchFileName.substring(0,24);
      			strBatchFileName=strBatchFileName+"Consolidated";
  			}
    		else {
    			request.setAttribute("alternateFileName",strBatchFileName+".txt");
    			strBatchFileName=strBatchFileName.substring(0,24);
			}
    		
    		}//End Of  else if(strBatchFileName.startsWith("QNBA"))
    	
			// Change added for BOA CR KOC1220
    		 if(strBatchFileName.startsWith("DEFL") || strBatchFileName.startsWith("CITI")|| strBatchFileName.startsWith("ENBD")|| strBatchFileName.startsWith("ALKL")|| strBatchFileName.startsWith("ALKF") || strBatchFileName.startsWith("QNBA"))
    		{
    			String accnumber = null;
        		if(frmFloatAccDetails.get("accountNO")!=null)
        		accnumber=(String) frmFloatAccDetails.get("accountNO");
        		accnumber=accnumber.substring(accnumber.length()-3);
        		SimpleDateFormat dateFormater=new SimpleDateFormat("ddMMYYYY");
    			
    			if(strBatchFileName.contains("Detail") || strBatchFileName.contains("Cons"))
    			{
    				strNewFile = strBatchFileName+".xls";
    			}
    			else if(strBatchFileName.contains("CSV"))
    			{
    				strNewFile = strBatchFileName+".csv";
    			}
    			else
    			{
    				strNewFile = strBatchFileName+".txt";
    			}
    		} //End change added for BOA CR KOC1220
    		else
    		{
    			strNewFile = strBatchFileName+".xls";
    		}//end of else
    		
    		File file = null;
    		
    		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
    		{
    		   String strAuthpdf = TTKPropertiesReader.getPropertyValue("Invoicerptdir")+strNewFile;
    		   
    			if(strDEFLBatchFileName.startsWith("DEFL")  && (strDEFLBatchFileName.contains("SummaryPDF") || strDEFLBatchFileName.contains("SummaryEXCEL")))
    			{
    				File files = new File(strAuthpdf);
    				 JasperPrint mainJasperPrint=null;
//    				 final OutputStream outStream = response.getOutputStream();
    				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
    				
    				mainJasperPrint=ReadConsolidatedFile(request,response,strAuthpdf,strDEFLBatchFileName);				
    				if(mainJasperPrint!=null){
    					
    					JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
    	    			
    	    			if(strDEFLBatchFileName.contains("SummaryPDF"))
    	    			{
    	    				response.setContentType("application/x-pdf");
        	    			response.setHeader("Content-disposition",
        	    					"inline; filename=DEFLConsolidated.pdf");
        	
    	    			
    	    			JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
    	    			
    	    			}
    	    			else if(strDEFLBatchFileName.contains("SummaryEXCEL"))
    	    			{
    	    				
    	    				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
    						 jExcelApiExporter.exportReport();
    						 response.setContentType("application/vnd.ms-excel");
     	        			response.setHeader("Content-disposition",
     	        					"attachment; filename=DEFLConsolidated.xls");
    					
    	    			}
    	    			
    				}
    				
        				boas.flush();
        				boas.close();
        				response.flushBuffer();
    					request.setAttribute("boas",boas);
    					return (mapping.findForward(strReportdisplay));
    			}// End Of 	if(strDEFLBatchFileName.startsWith("DEFL")  && (strDEFLBatchFileName.contains("SummaryPDF") || strDEFLBatchFileName.contains("SummaryEXCEL")))
    			else if(strALKLBatchFileName.startsWith("ALKL")  && (strALKLBatchFileName.contains("SummaryPDF") || strALKLBatchFileName.contains("SummaryEXCEL")))
    			{
    				File files = new File(strAuthpdf);
    				 JasperPrint mainJasperPrint=null;
//    				 final OutputStream outStream = response.getOutputStream();
    				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
    				
    				mainJasperPrint=ReadConsolidatedFile(request,response,strAuthpdf,strALKLBatchFileName);				
    				if(mainJasperPrint!=null){
    					
    					JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
    	    			
    	    			if(strALKLBatchFileName.contains("SummaryPDF"))
    	    			{
    	    				response.setContentType("application/x-pdf");
        	    			response.setHeader("Content-disposition",
        	    					"inline; filename=ALKLConsolidated.pdf");
        	
    	    			
    	    			JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
    	    			
    	    			}
    	    			else if(strALKLBatchFileName.contains("SummaryEXCEL"))
    	    			{
    	    				
    	    				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
    						 jExcelApiExporter.exportReport();
    						 response.setContentType("application/vnd.ms-excel");
     	        			response.setHeader("Content-disposition",
     	        					"attachment; filename=ALKLConsolidated.xls");
    					
    	    			}
    	    			
    				}
    				
        				boas.flush();
        				boas.close();
        				response.flushBuffer();
    					request.setAttribute("boas",boas);
    					return (mapping.findForward(strReportdisplay));
    			}// End Of else if(strALKLBatchFileName.startsWith("ALKL")  && (strALKLBatchFileName.contains("SummaryPDF") || strALKLBatchFileName.contains("SummaryEXCEL")))
    			else if(strALKFBatchFileName.startsWith("ALKF")  && (strALKFBatchFileName.contains("SummaryPDF") || strALKFBatchFileName.contains("SummaryEXCEL")))
    			{
    				File files = new File(strAuthpdf);
    				 JasperPrint mainJasperPrint=null;
//    				 final OutputStream outStream = response.getOutputStream();
    				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
    				
    				mainJasperPrint=ReadConsolidatedFile(request,response,strAuthpdf,strALKFBatchFileName);				
    				if(mainJasperPrint!=null){
    					
    					JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
    	    			
    	    			if(strALKFBatchFileName.contains("SummaryPDF"))
    	    			{
    	    				response.setContentType("application/x-pdf");
        	    			response.setHeader("Content-disposition",
        	    					"inline; filename=ALKFConsolidated.pdf");
        	
    	    			
    	    			JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
    	    			
    	    			}
    	    			else if(strALKFBatchFileName.contains("SummaryEXCEL"))
    	    			{
    	    				
    	    				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
    						 jExcelApiExporter.exportReport();
    						 response.setContentType("application/vnd.ms-excel");
     	        			response.setHeader("Content-disposition",
     	        					"attachment; filename=ALKFConsolidated.xls");
    					
    	    			}
    	    			
    				}
    				
        				boas.flush();
        				boas.close();
        				response.flushBuffer();
    					request.setAttribute("boas",boas);
    					return (mapping.findForward(strReportdisplay));
    			}// End Of 	else if(strALKFBatchFileName.startsWith("ALKF")  && (strALKFBatchFileName.contains("SummaryPDF") || strALKFBatchFileName.contains("SummaryEXCEL")))
    			else if(strQNBABatchFileName.startsWith("QNBA")  && (strQNBABatchFileName.contains("SummaryPDF") || strQNBABatchFileName.contains("SummaryEXCEL")))
    			{
    				File files = new File(strAuthpdf);
    				 JasperPrint mainJasperPrint=null;
//    				 final OutputStream outStream = response.getOutputStream();
    				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
    				
    				mainJasperPrint=ReadConsolidatedFile(request,response,strAuthpdf,strQNBABatchFileName);				
    				if(mainJasperPrint!=null){
    					
    					JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
    	    			
    	    			if(strQNBABatchFileName.contains("SummaryPDF"))
    	    			{
    	    				response.setContentType("application/x-pdf");
        	    			response.setHeader("Content-disposition",
        	    					"inline; filename=QNBAConsolidated.pdf");
        	
    	    			
    	    			JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
    	    			
    	    			}
    	    			else if(strQNBABatchFileName.contains("SummaryEXCEL"))
    	    			{
    	    				
    	    				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
    						 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
    						 jExcelApiExporter.exportReport();
    						 response.setContentType("application/vnd.ms-excel");
     	        			response.setHeader("Content-disposition",
     	        					"attachment; filename=QNBAConsolidated.xls");
    					
    	    			}
    	    			
    				}
    				
        				boas.flush();
        				boas.close();
        				response.flushBuffer();
    					request.setAttribute("boas",boas);
    					return (mapping.findForward(strReportdisplay));
    			}
    			else
    			{
    			file = new File(strAuthpdf);
    			if(file.exists())
    			{
    				log.info("Inside exist");
    				strAuthpdf = TTKPropertiesReader.getPropertyValue("Invoicerptdir")+strNewFile;
    				
    				request.setAttribute("fileName",strAuthpdf);
    				
    			}//end of if(file.exists())
    			}
    		}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
    		return this.getForward("claimslistdetails", mapping, request);//finally return to the grid screen
    	}
    	catch(TTKException expTTK)
    	{
    		request.setAttribute("flagerror", "Y");
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		request.setAttribute("flagerror", "Y");
    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
    	}//end of catch(Exception exp)
    }//end of doViewFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    private JasperPrint ReadConsolidatedFile(HttpServletRequest request,HttpServletResponse response,
    		String strAuthpdf,String strDEFLBatchFileName) {
    	JasperPrint mainJasperPrint = null;
    	try
    	{
    	ArrayList<ChequeVO> dataBeanList = new ArrayList<ChequeVO>();
    	ChequeVO cto=null;
    	ByteArrayOutputStream boas=null;
    	JasperReport mainJasperReport = null;
    	JasperReport diagnosisJasperReport = null;
    	JasperReport activityJasperReport = null;
    	
    	String strTransferAmount = "";
    	String strDebitAccountIBAN = "";
    	String strTransferCurrency = "";
    	String strBeneficiaryName = "";
    	String strCountryName = "";
    	String strValueDate = "";
    	String strBeneficiaryBankAddress2 = "";
    	String strBeneficiaryBankSwiftCode = "";
    	String strBeneficiaryBankAccountNumber = "";
    	String strCustomerRefereanceNumber = "";
    	String strflagpdf="";
    	File f = new File(strAuthpdf);
    	if(f.exists())
    	{
    		FileInputStream inputStream = new FileInputStream(new File(strAuthpdf));
    		HSSFWorkbook workbook = null;
    		HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
    		workbook =  (HSSFWorkbook) new HSSFWorkbook(inputStream);
    		sheet = workbook.getSheetAt(0);
    		final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
    		if(sheet==null)
    			request.setAttribute("updated", "Please upload proper File");
    		else{
    			Iterator<?> rows     = sheet.rowIterator ();
    			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    			while (rows.hasNext ()) 
    			{
    				HSSFRow row =  (HSSFRow) rows.next(); 

    				if(row.getRowNum()==0)
    					continue;
    				Iterator<?> cells = row.cellIterator (); 
    				ArrayList<String> rowData = new ArrayList<String>();
    				for(short i=0;i<=21;i++)
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
    							// Date CELL TYPE
    							if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)){
    								rowData.add(new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue()));
    							}
    							else {// NUMERIC CELL TYPE
    								rowData.add(cell.getNumericCellValue () + "");
    							}break;
    						}
    						case HSSFCell.CELL_TYPE_STRING :
    						{
    							// STRING CELL TYPE
    							//String richTextString = cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    							String richTextString = cell.getStringCellValue().trim();
    							richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
    							rowData.add(richTextString);
    							break;
    						}
    						case HSSFCell.CELL_TYPE_BLANK :
    						{	//HSSFRichTextString blankCell	=	cell.get.getRichStringCellValue();
    							String blankCell	=	cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    							rowData.add(blankCell);
    							break;
    						}
    						default:
    						{
    							// types other than String and Numeric.
    							//System.out.println ("Type not supported.");
    							break;
    						}
    						} // end switch
    					}//else

    				}//for

    				cto = new ChequeVO();
    				strTransferAmount=rowData.get(1);
    				cto.setTransferAmount(strTransferAmount);
    				strDebitAccountIBAN=rowData.get(2);
    				cto.setDebitAccountIBAN(strDebitAccountIBAN);
    				strTransferCurrency=rowData.get(4);
    				cto.setTransferCurrency(strTransferCurrency);
    				strBeneficiaryName=rowData.get(5);
    				cto.setBeneficiaryName(strBeneficiaryName);
    				strCountryName=rowData.get(9);
    				cto.setCountryName(strCountryName);
    				strValueDate=rowData.get(10);
    				cto.setValueDate(strValueDate);
    				strBeneficiaryBankAddress2=rowData.get(13);
    				cto.setBeneficiaryBankAddress2(strBeneficiaryBankAddress2);
    				strBeneficiaryBankSwiftCode=rowData.get(15);
    				cto.setBeneficiaryBankSwiftCode(strBeneficiaryBankSwiftCode);
    				strBeneficiaryBankAccountNumber = rowData.get(16);
    				cto.setBeneficiaryBankAccountNumber(strBeneficiaryBankAccountNumber);
    				strCustomerRefereanceNumber= rowData.get(20);
    				cto.setCustomerRefereanceNumber(strCustomerRefereanceNumber);
    				if(strDEFLBatchFileName.contains("SummaryPDF"))
        			{
    					cto.setFlagpdf("Y");
        			}
    				else if(strDEFLBatchFileName.contains("SummaryEXCEL"))
        			{
    					cto.setFlagpdf("N");
        			}
    				dataBeanList.add(cto);


    			} //end while

    			String mainJrxmlfile = "reports/finance/DEFLConsolidatedPDF.jrxml";

    			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataBeanList);
    			Map parameters = new HashMap();
    			boas = new ByteArrayOutputStream();
    			mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
    			mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, parameters, beanColDataSource);
    		
    			//JasperExportManager.exportReportToPdfFile(mainJasperPrint,"C:/Users/rishi.sharma/Desktop/sample_report.pdf");

    		}
    		
    	}else{
    		JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
    		Map parameters = new HashMap();
    		mainJasperPrint = JasperFillManager.fillReport(emptyReport, parameters, new JREmptyDataSource());
    	}
    	
    	}catch(Exception e)
    	{
    		request.setAttribute("flagerror", "Y");
    		e.printStackTrace();
    	}
    	return mainJasperPrint;
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
    public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ViewPaymentAdviceAction doSearch");
    		log.info("Inside ViewPaymentAdviceAction doSearch");
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		//if the page number or sort id is clicked
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{
    				log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward(strclaimslistdetails));
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
    			tableData.createTableInfo("PaymentBatchTable",null);
    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
    			tableData.setSortData("0101");
    			tableData.setSortColumnName("BATCH_DATE");
    			tableData.setSortOrder("DESC");
    			tableData.modifySearchData("search");
    		}//end of else

    		ArrayList alTransaction= floatAccountManagerObject.getViewPaymentAdviceList(tableData.getSearchData());
    		tableData.setData(alTransaction, "search");
    		//set the table data object to session
    		request.getSession().setAttribute("tableData",tableData);
    		//finally return to the grid screen
    		return this.getForward(strclaimslistdetails, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
    	}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to get the next set of records with the given search criteria.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ViewPaymentAdviceAction doForward");
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		ArrayList alBankList = floatAccountManagerObject.getViewPaymentAdviceList(tableData.getSearchData());
    		tableData.setData(alBankList, strForward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
    		return this.getForward(strclaimslistdetails, mapping, request);//finally return to the grid screen
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
    	}//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to get the previous set of records with the given search criteria.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ViewPaymentAdviceAction doBackward");
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
    		ArrayList alBankList = floatAccountManagerObject.getViewPaymentAdviceList(tableData.getSearchData());
    		tableData.setData(alBankList, strBackward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
    		return this.getForward(strclaimslistdetails, mapping, request);//finally return to the grid screen
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
    	}//end of catch(Exception exp)
    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * Returns the ContactManager session object for invoking methods on it.
     * @return ContactManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private FloatAccountManager getfloatAccountManagerObject() throws TTKException
    {
        FloatAccountManager floatAccountManager = null;
        try
        {
            if(floatAccountManager == null)
            {
                InitialContext ctx = new InitialContext();
                floatAccountManager = (FloatAccountManager) ctx.lookup("java:global/TTKServices/business.ejb3/FloatAccountManagerBean!com.ttk.business.finance.FloatAccountManager");
            }//end of if(contactManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "floataccount");
        }//end of catch
        return floatAccountManager;
    }//end getfloatAccountManagerObject()
    
    
    
    /**
     * this method will add search criteria fields and values to the arraylist and will return it
     * @param frmViewPayments formbean which contains the search fields
     * @param request HttpServletRequest
     * @return ArrayList contains search parameters
     */
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmViewPayments,HttpServletRequest request)
    throws TTKException
    {
        //build the column names along with their values to be searched
        ArrayList<Object> alSearchParams = new ArrayList<Object>();
        alSearchParams.add(TTKCommon.getWebBoardId(request));
        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sFileName")));
        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sBatchNum")));
        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sClaimSettelmentNumber")));
        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sBatchDate")));
        alSearchParams.add(TTKCommon.getUserSeqId(request));
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmFloatAccounts,HttpServletRequest request)
}// end of ViewPaymentSearchAction.java

