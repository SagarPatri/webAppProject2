/**
 * @ (#) InvoicesAction.java 26 Oct 2007
 * Project       : TTK HealthCare Services
 * File          : InvoicesAction.java
 * Author        : Yogesh S.C
 * Company       : Span Systems Corporation
 * Date Created  : 26 Oct 2007
 *
 * @author       : Yogesh S.C
 * Modified by   :
 * Modified date :
 * Reason        :
 */

 package com.ttk.action.finance;

 import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.finance.FloatAccountManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.ChequeVO;

 public class BordereauAction extends TTKAction{
	private static Logger log = Logger.getLogger(InvoicesAction.class );
//	Modes of Float List
	private static final String strBackward="Backward";
	private static final String strForward="Forward";
	private static final String strReportdisplay="reportdisplay";
	//Action mapping forwards
	private static final String strBordereauList="bordereauList";
	private static final String strReportList="ReportList";
	private static final String straddinvoices="generateLetter";	
	//Exception Message Identifier
	private static final String strInvoices="Invoices";
	private static final String strViewReports="viewReport";

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
			log.debug("Inside InvoicesAction doDefault");
			TableData  tableData =TTKCommon.getTableData(request);
			DynaActionForm frmInvoices = (DynaActionForm)form;			
            frmInvoices.initialize(mapping);//reset the form data
            //create new table data object
			tableData = new TableData();
			FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
			//create the required grid table
			tableData.createTableInfo("BordereauxTable",new ArrayList());
			ArrayList alInvoices= floatAccountManagerObject.getBordereauxReportList(tableData.getSearchData());
			tableData.setData(alInvoices, "search");
			//set the table data object to session
			request.getSession().setAttribute("BordereauxTableData",tableData);
						
			return this.getForward(strBordereauList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strInvoices));
		}//end of catch(Exception exp)
	}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
	public ActionForward doGetInvoiceList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
							HttpServletResponse response) throws Exception{
		try{
			setLinks(request);
			log.debug("Inside InvoiceAction doSearch");
			
			TableData  tableData =(request.getSession().getAttribute("BordereauxTableData")!=null)?(TableData)request.getSession().getAttribute("BordereauxTableData"):new TableData();
			FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			String from = request.getParameter("from");
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					if(from.equalsIgnoreCase("Search")){
					return (mapping.findForward(strBordereauList));
					}else{
						return (mapping.findForward(strViewReports));
					}
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else
			{
				log.debug("inside search action");
				//create the required grid table
				tableData.createTableInfo("BordereauxTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				tableData.setSortData("0001");
				tableData.setSortColumnName("INVOICE_SEQ_ID");
				tableData.setSortOrder("DESC");
				tableData.modifySearchData("search");
			}//end of else
			
			floatAccountManagerObject.generateBordereauxReport(tableData.getSearchData());
			ArrayList alInvoices= floatAccountManagerObject.getBordereauxReportList(tableData.getSearchData());
			tableData.setData(alInvoices, "search");
			//set the table data object to session
			request.getSession().setAttribute("BordereauxTableData",tableData);
			//finally return to the grid screen
			return this.getForward(strBordereauList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			//expTTK.getRootCause().printStackTrace();
			return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
			//exp.printStackTrace();
			return this.processExceptions(request,mapping,new TTKException(exp,strInvoices));
		}//end of catch(Exception exp)
	}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	public ActionForward doSearchReportList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			setLinks(request);
			log.debug("Inside InvoiceAction doSearch");
			
			TableData  tableData =(request.getSession().getAttribute("BordereauxTableData")!=null)?(TableData)request.getSession().getAttribute("BordereauxTableData"):new TableData();
			FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return (mapping.findForward(strReportList));
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else
			{
				log.debug("inside search action");
				//create the required grid table
				tableData.createTableInfo("BordereauxTable",null);
				tableData.setSearchData(this.populateSearchCriteriaForReportList((DynaActionForm)form,request));
				tableData.setSortData("0001");
				tableData.setSortColumnName("INVOICE_SEQ_ID");
				tableData.setSortOrder("DESC");
				tableData.modifySearchData("search");
			}//end of else
			ArrayList alInvoices= floatAccountManagerObject.getBordereauxReportSearchList(tableData.getSearchData());
			tableData.setData(alInvoices, "search");
			//set the table data object to session
			request.getSession().setAttribute("BordereauxTableData",tableData);
			//finally return to the grid screen
			return mapping.findForward(strReportList);
		}//end of try
		catch(TTKException expTTK)
		{
			//expTTK.getRootCause().printStackTrace();
			return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
			//exp.printStackTrace();
			return this.processExceptions(request,mapping,new TTKException(exp,strInvoices));
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
    		log.debug("Inside InvoicesAction doForward");
    		String from = request.getParameter("form");
    		TableData  tableData =(request.getSession().getAttribute("BordereauxTableData")!=null)?(TableData)request.getSession().getAttribute("BordereauxTableData"):new TableData();
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
			ArrayList alInvoiceList = floatAccountManagerObject.getBordereauxReportSearchList(tableData.getSearchData());
			tableData.setData(alInvoiceList, strForward);//set the table data
			request.getSession().setAttribute("BordereauxTableData",tableData);//set the tableData object to session
			if(from.equalsIgnoreCase("Search")){
				return (mapping.findForward(strBordereauList));
				}else{
					return (mapping.findForward(strReportList));
				}
		}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strInvoices));
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
    		String from = request.getParameter("from");
    		TableData  tableData =(request.getSession().getAttribute("BordereauxTableData")!=null)?(TableData)request.getSession().getAttribute("BordereauxTableData"):new TableData();
    		log.debug("Inside InvoicesAction doBackward");
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
			ArrayList alInvoiceList = floatAccountManagerObject.getBordereauxReportSearchList(tableData.getSearchData());
			tableData.setData(alInvoiceList, strBackward);//set the table data
			request.getSession().setAttribute("BordereauxTableData",tableData);//set the tableData object to session
			if(from.equalsIgnoreCase("Search")){
				return (mapping.findForward(strBordereauList));
				}else{
					return (mapping.findForward(strReportList));
				}
		}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strInvoices));
		}//end of catch(Exception exp)
    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


    
		public ActionForward doViewGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
			try{
			int rowNum=Integer.parseInt(request.getParameter("rownum"));
			String fileName=request.getParameter("fileName");
			TableData  tableData =(request.getSession().getAttribute("BordereauxTableData")!=null)?(TableData)request.getSession().getAttribute("BordereauxTableData"):new TableData();
			ChequeVO chequeVO = (ChequeVO)tableData.getRowInfo(rowNum);
			fileName = chequeVO.getReportName();
//			ByteArrayOutputStream boas=new ByteArrayOutputStream();	
			ByteArrayOutputStream boas=doGenerateLetter(request, response,chequeVO.getSeqID(), fileName);
			request.setAttribute("boas",boas);
			String fileFormat=fileName.substring(fileName.lastIndexOf("."));
			if(".xls".equals(fileFormat)||".XLS".equals(fileFormat)){
				request.setAttribute("reportType","EXL");
				//response.setContentType("application/vnd.ms-excel");
				//response.addHeader("Content-Disposition","attachment; filename=BordereauxReports.xls");
			}
			else{
				//response.setContentType("application/pdf");
				//response.addHeader("Content-Disposition","attachment; filename=BordereauxReports.pdf");
				request.setAttribute("reportType","pdf");
			}
			/*if (outputStream != null) {
            	outputStream.close();
            }*/
			return (mapping.findForward(strReportdisplay));
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request,mapping,expTTK);
			}//end of catch(ETTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request,mapping,new TTKException(exp,strInvoices));
			}//end of catch(Exception exp)
			}
    
    public ByteArrayOutputStream doGenerateLetter(HttpServletRequest request, HttpServletResponse response,Long SeqId, String fileName) throws Exception{
    	ByteArrayOutputStream boas=new ByteArrayOutputStream();
    	ArrayList<JasperPrint> reportList = new  ArrayList<JasperPrint>();
    	HashMap<String, Object> hashMap = new HashMap<String, Object>();
    	JasperReport objJasperReport,emptyReport=null;
    	String fileFormat=fileName.substring(fileName.lastIndexOf("."));
    	File outFile=new File(TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+fileName);
    	FileOutputStream foFileOutputStream=null;
    	try{
		if(!outFile.exists()){
			foFileOutputStream=new FileOutputStream(outFile);
			log.info("Inside InvoiceGeneralAction doAdd");
			String jrxmlfileMem ="reports/finance/bordereau/coversheet.jrxml";
			String summary1Format ="reports/finance/bordereau/summary1.jrxml";
			String summary2Format ="reports/finance/bordereau/summary2.jrxml";
			String acctgPremBordereau ="reports/finance/bordereau/AcctgPremBordereau.jrxml";
			String cashPremBordereau ="reports/finance/bordereau/CashPremBordereau.jrxml";
			String claimsBordereau ="reports/finance/bordereau/ClaimsBordereau.jrxml";
//			String claimsRecoveries ="reports/finance/bordereau/ClaimsRecoveries.jrxml";
//			String acctgPremBordereauxPdf ="reports/finance/bordereau/AcctgPremBordereauxPdf.jrxml";
			ArrayList inputList=new ArrayList<>();
			inputList.add(SeqId);
			TTKReportDataSource ttkReportDataSource = new TTKReportDataSource("BordereauReport",inputList);
			ResultSet[] resultSetArray=ttkReportDataSource.getResultSet();
			if(resultSetArray[0]!=null){
				JasperPrint jasperPrint =null;
				try(OracleCachedRowSet coversheetRowSet=(OracleCachedRowSet) resultSetArray[0]){
					if(coversheetRowSet.next())
					 {
						 objJasperReport=JasperCompileManager.compileReport(jrxmlfileMem);
						 coversheetRowSet.beforeFirst();
						 JRDataSource coversheetResultsetDatasource = new JRResultSetDataSource(coversheetRowSet);
						 jasperPrint= JasperFillManager.fillReport(objJasperReport,hashMap,coversheetResultsetDatasource);				 
					 }
					 else{
						 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }
				}
				reportList.add(jasperPrint);
				File mkFile=new File(TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory"));
				if(!mkFile.exists()){
					mkFile.mkdir();
				 }
//				 String strFilePath = TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+"Coversheet/";
//				 JasperExportManager.exportReportToPdfFile(jasperPrint,strFilePath+"Coversheet"+new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date())+".pdf");

			}
//			if(resultSetArray[2]!=null){
//				
//				OracleCachedRowSet summary1SubRowSet=(OracleCachedRowSet) resultSetArray[2];
//					if(summary1SubRowSet.next())
//					 {
//						 summary1SubRowSet.beforeFirst();
//						while(summary1SubRowSet.next()){
//							System.out.println("summary1 sub result data::"+summary1SubRowSet.getString(1));
//						}
//						System.out.println("This is Third cursor");
//						 objJasperReport=JasperCompileManager.compileReport(summary1SubReport);
//						 summary1SubRowSet.beforeFirst();
//						 JRDataSource summary1SubResultsetDatasource = new JRResultSetDataSource(summary1SubRowSet);
//						 subReportHashMap.put("subReportHashMap", summary1SubResultsetDatasource);
//						 subReportHashMap.put("summary2Format", objJasperReport);			 	 
//					 }
//			}
			
			if(resultSetArray[1]!=null){
				JasperPrint jasperPrint =null;
				OracleCachedRowSet summary1RowSet=(OracleCachedRowSet) resultSetArray[1];
					if(summary1RowSet.next())
					 {
						summary1RowSet.beforeFirst();
						while(summary1RowSet.next()){
							System.out.println("summary1 main result data::"+summary1RowSet.getString(1));
						}
						System.out.println("This is second cursor");
						 objJasperReport=JasperCompileManager.compileReport(summary1Format);
						 summary1RowSet.beforeFirst();
						 JRDataSource summary1ResultsetDatasource = new JRResultSetDataSource(summary1RowSet);
						 System.out.println("before.............");
						 jasperPrint= JasperFillManager.fillReport(objJasperReport,hashMap,summary1ResultsetDatasource);	
						 System.out.println("after.............");
					 }
					 else{
						 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }
//				}
					reportList.add(jasperPrint);
//					String strFilePath = TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+"SummaryReport1/";
//					JasperExportManager.exportReportToPdfFile(jasperPrint,strFilePath+"SummaryReport1"+new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date())+".pdf");

			}
//			if(resultSetArray[4]!=null){
//				OracleCachedRowSet fourthRowSet=(OracleCachedRowSet) resultSetArray[4];
//					if(fourthRowSet.next())
//					 {
//						fourthRowSet.beforeFirst();
//						while(fourthRowSet.next()){
//							System.out.println("summary2 sub result data::"+fourthRowSet.getString("MEDX_NETPREM_QS_TREETY"));
//						}
//						System.out.println("This is Fifth cursor");
//						 objJasperReport=JasperCompileManager.compileReport(summary2SubReport);
//						 fourthRowSet.beforeFirst();
//						 JRDataSource fourthResultsetDatasource = new JRResultSetDataSource(fourthRowSet);
//						 subReportHashMap.put("subReportHashMap", fourthResultsetDatasource);
//						 subReportHashMap.put("summary2Format", objJasperReport);			 	 
//					 }
//			}
			
			if(resultSetArray[2]!=null){
				JasperPrint jasperPrint =null;
				OracleCachedRowSet summary2RowSet=(OracleCachedRowSet) resultSetArray[2];
					if(summary2RowSet.next())
					 {
						summary2RowSet.beforeFirst();
						System.out.println("This is third cursor");
						while(summary2RowSet.next()){
							System.out.println("summary2 main result data::"+summary2RowSet.getString("MEDX_NETPREM_QS_TREETY"));
						}
						 objJasperReport=JasperCompileManager.compileReport(summary2Format);
						 summary2RowSet.beforeFirst();
						 JRDataSource summary2ResultsetDatasource = new JRResultSetDataSource(summary2RowSet);
						 jasperPrint= JasperFillManager.fillReport(objJasperReport,hashMap,summary2ResultsetDatasource);				 
					 }
					 else{
						 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }
					reportList.add(jasperPrint);
//					String strFilePath = TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+"SummaryReport2/";				
//				 JasperExportManager.exportReportToPdfFile(jasperPrint,strFilePath+"SummaryReport2"+new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date())+".pdf");
			}
			if(resultSetArray[3]!=null){
				JasperPrint jasperPrint =null;
				OracleCachedRowSet acctPremRowSet=(OracleCachedRowSet) resultSetArray[3];
					if(acctPremRowSet.next())
					 {
						System.out.println("This is sixth cursor");
						 objJasperReport=JasperCompileManager.compileReport(acctgPremBordereau);
						 acctPremRowSet.beforeFirst();
						 JRDataSource acctPremResultsetDatasource = new JRResultSetDataSource(acctPremRowSet);
						 jasperPrint= JasperFillManager.fillReport(objJasperReport,hashMap,acctPremResultsetDatasource);
					 }
					 else{
						 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }
					reportList.add(jasperPrint);
//					String strFilePath = TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+"AcctPremBordereau/";
//				 JasperExportManager.exportReportToPdfFile(jasperPrint,strFilePath+"AcctPremBordereau"+new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date())+".pdf");

			}
			if(resultSetArray[4]!=null){
				JasperPrint jasperPrint =null;
				OracleCachedRowSet cashPremRowSet=(OracleCachedRowSet) resultSetArray[4];
					if(cashPremRowSet.next())
					 {
						System.out.println("This is seventh cursor");
						 objJasperReport=JasperCompileManager.compileReport(cashPremBordereau);
						 cashPremRowSet.beforeFirst();
						 JRDataSource cashPremResultsetDatasource = new JRResultSetDataSource(cashPremRowSet);
						 jasperPrint= JasperFillManager.fillReport(objJasperReport,hashMap,cashPremResultsetDatasource);				 
					 }
					 else{
						 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }
					reportList.add(jasperPrint);
//					String strFilePath = TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+"CashPremBordereau/";
//				 JasperExportManager.exportReportToPdfFile(jasperPrint,strFilePath+"CashPremBordereau"+new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date())+".pdf");
			}
			if(resultSetArray[5]!=null){
				JasperPrint jasperPrint =null;
				OracleCachedRowSet  claimsRowSet=(OracleCachedRowSet) resultSetArray[5];
					if(claimsRowSet.next())
					 {
						System.out.println("This is eighth cursor");
						 objJasperReport=JasperCompileManager.compileReport(claimsBordereau);
						 claimsRowSet.beforeFirst();
						 JRDataSource ClaimsResultsetDatasource = new JRResultSetDataSource(claimsRowSet);
						 jasperPrint= JasperFillManager.fillReport(objJasperReport,hashMap,ClaimsResultsetDatasource);				 
					 }
					 else{
						 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
						 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
					 }
					reportList.add(jasperPrint);
//					String strFilePath = TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+"ClaimsBordereaux/";
//				 JasperExportManager.exportReportToPdfFile(jasperPrint,strFilePath+"ClaimsBordereaux"+new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date())+".pdf");
			}
			/*if(resultSetArray[6]!=null){
				JasperPrint jasperPrint =null;
				OracleCachedRowSet  claimsRecoveriesRowSet=(OracleCachedRowSet) resultSetArray[6];
				if(claimsRecoveriesRowSet.next())
				{
					System.out.println("This is ninth cursor");
					objJasperReport=JasperCompileManager.compileReport(claimsRecoveries);
					claimsRecoveriesRowSet.beforeFirst();
					JRDataSource claimsRecoveriesResultsetDatasource = new JRResultSetDataSource(claimsRecoveriesRowSet);
					jasperPrint= JasperFillManager.fillReport(objJasperReport,hashMap,claimsRecoveriesResultsetDatasource);				 
				}
				else{
					emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
					jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				}
				reportList.add(jasperPrint);
			}*/
				if(".xls".equals(fileFormat)||".XLS".equals(fileFormat)){
					String[] sheetNames={"Coversheet","SummaryReport1","SummaryReport2","AcctPremBordereau","CashPremBordereau","ClaimsBordereau"/*,"ClaimsRecoveries"*/};
					JRXlsExporter jExcelApiSaver = new JRXlsExporter();
					jExcelApiSaver.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, reportList);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, foFileOutputStream);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.FALSE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					jExcelApiSaver.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					jExcelApiSaver.exportReport();
//					byte[] encodedData = Files.readAllBytes(Paths.get(TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+fileName+fileFormat));
//					boas.write(encodedData);
				}
				if(".pdf".equals(fileFormat)||".PDF".equals(fileFormat)){
				JRExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, reportList);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, foFileOutputStream);
				exporter.exportReport();
//				byte[] encodedData = Files.readAllBytes(Paths.get(TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+fileName+fileFormat));
//				boas.write(encodedData);
				}
				
		System.out.println("File not exist");	
		}
//		else{
//			byte[] encoded = Files.readAllBytes(Paths.get(TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+fileName+fileFormat));
//			boas.write(encoded);
//			System.out.println("File exist"+encoded.length);
//		}
		
		byte[] encoded = Files.readAllBytes(Paths.get(TTKPropertiesReader.getPropertyValue("BordereauxReportDirectory")+fileName));
		boas.write(encoded);
		}
		catch(TTKException expTTK)
		{
//			JasperPrint jasperPrint =null;
//			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
//			jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
			System.out.println("expTTK::"+expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
//			JasperPrint jasperPrint =null;
//			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
//			jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
			System.out.println("exp::"+exp);
		}//end of catch(Exception exp)

		return boas;
		}
	public ActionForward doAddInvoices(ActionMapping mapping,ActionForm form,HttpServletRequest request,
												 HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doAddInvoices method of InvoicesAction");
			setLinks(request);
			DynaActionForm frmInvoices = (DynaActionForm)form;
			frmInvoices.initialize(mapping);			
			return this.getForward(straddinvoices, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInvoices));
		}//end of catch(Exception exp)
	}//end of doAddInvoices(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				//HttpServletResponse response)
	
    /**
	 * Returns the FloatAccountManager session object for invoking methods on it.
	 * @return FloatAccountManager session object which can be used for method invokation
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
			throw new TTKException(exp, strInvoices);
		}//end of catch
		return floatAccountManager;
	}//end getfloatAccountManagerObject()
	
	public ActionForward doViewDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
		//create the required grid table
		TableData  tableData =new TableData(); 
		tableData.createTableInfo("BordereauxTable",new ArrayList());
		ArrayList alInvoices= floatAccountManagerObject.getBordereauxReportList(tableData.getSearchData());
		tableData.setData(alInvoices, "search");
		request.getSession().setAttribute("BordereauxTableData",tableData);
		return (mapping.findForward(strViewReports));
		}
	
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmInvoices,HttpServletRequest request)throws TTKException
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();		
		alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("reinsurerId")));
		/*alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("sFromDate")));
		alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("sToDate")));
		alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("sStatus")));*/
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		/*alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("groupID")));
		alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("groupName")));
		alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("strPolicyNbr")));*/
		return alSearchParams;
	}//end of populateSearchCriteria(DynaActionForm frmFloatAccounts,HttpServletRequest request)

	private ArrayList<Object> populateSearchCriteriaForReportList(DynaActionForm frmInvoices,HttpServletRequest request)throws TTKException
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("sFromDate")));
		alSearchParams.add(TTKCommon.replaceSingleQots(frmInvoices.getString("sToDate")));
		
		return alSearchParams;
	}
}//end of class InvoicesAction
