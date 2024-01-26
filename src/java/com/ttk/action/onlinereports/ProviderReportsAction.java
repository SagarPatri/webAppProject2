 /**
  * @ (#) OnlineReportsAction.java March 8, 2008
  * Project      : TTK HealthCare Services
  * File         : OnlineReportsAction.java
  * Author       : Balaji C R B
  * Company      : Span Systems Corporation
  * Date Created : March 8, 2008
  *
  * @author       :Balaji C R B
  * Modified by   :
  * Modified date :
  * Reason        :
  */

 package com.ttk.action.onlinereports;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InitialContext;
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
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.dom4j.Document;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.enrollment.PolicyManager;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.business.onlineforms.providerLogin.OnlinePbmProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.MOUDocumentVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

 public class ProviderReportsAction extends TTKAction{

	 private static final Logger log = Logger.getLogger( ProviderReportsAction.class );

	 //declaration of forward paths
	 private static final String strForward="Forward";
	 private static final String strBackward="Backward";
	 private static final String strPreAuthSummaryReport="PreAuthSummaryReport";
	 private static final String strPreAuthDetailedReport="PreAuthDetailedReport";
	 private static final String strClaimSummaryReport="ClaimSummaryReport";
	 private static final String strClaimDetailedReport="ClaimDetailedReport";
	 private static final String strSubmission="submission";
	 private static final String strReportdisplay="reportdisplay";
	 
	 //declaration of constants
	 private static final String strProviderReportsList="ProviderReportsList";
	 
	 //Exception Message Identifier
	 private static final String strReportExp="onlinereport";

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
			 //log.debug("Inside the doDefault method of OnlineReportsAction");
			 setOnlineLinks(request);
			 
			 //set the Forward Paths based on the sublink
			 if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				{
					((DynaActionForm)form).initialize(mapping);//reset the form data
				}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				
			 return this.getForward(strProviderReportsList,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processOnlineExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processOnlineExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 
	
	 //doSelectLink  
	 
	 
	 public ActionForward doSelectLink(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 //log.debug("Inside the doDefault method of OnlineReportsAction");
			 setOnlineLinks(request);
			 String strForwardPathList="";
			 String strActiveLink=TTKCommon.getActiveLink(request);
			 String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			 
             TableData tableData = TTKCommon.getTableData(request);
				
				if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				{ 
					((DynaActionForm)form).initialize(mapping);//reset the form data
				}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				((DynaActionForm)form).initialize(mapping);
				//create the required grid table
				tableData = new TableData();//create new table data object
				
				
				String reportId=request.getParameter("reportId");
				
				//((DynaActionForm)form).initialize(mapping);//reset the form data
				DynaActionForm frmProviderReportsList= (DynaActionForm) form;
				
				
				
				
				
			 //set the Forward Paths based on the sublink
			 if("preAuthSummary".equals(reportId))
			 {  //  clmSubmissionTable
				
				 tableData.createTableInfo("PreAuthReportTable",new ArrayList());
				 frmProviderReportsList.set("reportName", "Pre-Approval Summary Report");
				 ((Column)((ArrayList)tableData.getTitle()).get(9)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);	// Discount Amount
				 ((Column)((ArrayList)tableData.getTitle()).get(16)).setVisibility(false);
				 strForwardPathList=strPreAuthSummaryReport;
				 
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 else if("preAuthDetailed".equals(reportId))
			 {   
				 tableData.createTableInfo("PreAuthReportTable",new ArrayList());
				 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);	// Discount Amount
				 frmProviderReportsList.set("reportName", "Pre-Approval Detailed Report");
				 
				 strForwardPathList=strPreAuthDetailedReport;
				 
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strInsCompReports))
			 if("claimSummary".equals(reportId))
			 {  //  clmSubmissionTable
				
				 tableData.createTableInfo("ClaimReportTable",new ArrayList());
				 frmProviderReportsList.set("reportName", "Claim Summary Report");
				 ((Column)((ArrayList)tableData.getTitle()).get(11)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(19)).setVisibility(false);
				 strForwardPathList=strClaimSummaryReport;
				 
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 else if("claimDetailed".equals(reportId))
			 {   
				 tableData.createTableInfo("ClaimReportTable",new ArrayList());
				 frmProviderReportsList.set("reportName", "Claim Detailed Report");
				 
				 strForwardPathList=strClaimDetailedReport;
				 
			 }
			 
			request.getSession().setAttribute("tableData",tableData);
			request.getSession().setAttribute("reportId",reportId);
			request.getSession().setAttribute("frmProviderReportsList",frmProviderReportsList);//reset the form data
			
			
			return mapping.findForward(strForwardPathList);
			
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processOnlineExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processOnlineExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			try{
				log.debug("Inside the doBackward method of ProviderReportAction");
				setLinks(request);
				String reportId=request.getParameter("reportId");
				ArrayList alClmDrugList = null;
				OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
				HttpSession session=request.getSession();
				 
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
				
				Long hospSeqID=userSecurityProfile.getHospSeqId();
				TableData tableData = TTKCommon.getTableData(request);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				tableData.modifySearchData("search");
				tableData.modifySearchData(strBackward);//modify the search data
				//fetch the data from the data access layer and set the data to table object
				if(("preAuthSummary".equals(reportId))||(("preAuthDetailed".equals(reportId))))
				alClmDrugList= onlineAccessManagerObject.getProviderPreAuthReportData(tableData.getSearchData(),hospSeqID,reportId);// Populating Data Which matches the search criteria
				if(("claimSummary".equals(reportId))||(("claimDetailed".equals(reportId))))
				alClmDrugList= onlineAccessManagerObject.getProviderPreAuthReportData(tableData.getSearchData(),hospSeqID,reportId);// Populating Data Which matches the search criteria
					
				tableData.setData(alClmDrugList,strBackward);
				//set the table data object to session
				request.getSession().setAttribute("tableData",tableData);
				
				
				if(("preAuthSummary".equals(reportId)))
					return this.getForward(strPreAuthSummaryReport, mapping, request);
				else if(("preAuthDetailed".equals(reportId)))
					return this.getForward(strPreAuthDetailedReport, mapping, request);
				else if(("claimSummary".equals(reportId)))
					return this.getForward(strClaimSummaryReport, mapping, request);
				else if(("claimDetailed".equals(reportId)))
					return this.getForward(strClaimDetailedReport, mapping, request);

				return null;
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strSubmission));
			}//end of catch(Exception exp)
		}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
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
		public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			try{
				log.debug("Inside the doForward method of ProviderReportAction");
				setLinks(request);
				String reportId=request.getParameter("reportId");
				ArrayList alClmDrugList = null;
				HttpSession session=request.getSession();
				String reportId1="";
				
				if("preAuthSummary".equals(reportId))
					reportId1="PSR";
				else if("preAuthDetailed".equals(reportId))
					reportId1="PDR";
				else if("claimSummary".equals(reportId))
					reportId1="CSR";
				else if("claimDetailed".equals(reportId))
					reportId1="CDR";
				
				 
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
				
				String strForwardPathList="";
				String strReportId=request.getParameter("reportId");
				Long hospSeqID=userSecurityProfile.getHospSeqId();
				OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
				TableData tableData = TTKCommon.getTableData(request);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				tableData.modifySearchData("search");
				tableData.modifySearchData(strForward);//modify the search data
				//fetch the data from the data access layer and set the data to table object
				
				
				if(("preAuthSummary".equals(reportId))||(("preAuthDetailed".equals(reportId))))
				alClmDrugList= onlineAccessManagerObject.getProviderPreAuthReportData(tableData.getSearchData(),hospSeqID,reportId1);// Populating Data Which matches the search criteria
				if(("claimSummary".equals(reportId))||(("claimDetailed".equals(reportId))))
				alClmDrugList= onlineAccessManagerObject.getProviderClaimReportData(tableData.getSearchData(),hospSeqID,reportId1);// Populating Data Which matches the search criteria
				tableData.setData(alClmDrugList,strForward);
				//set the table data object to session
				request.getSession().setAttribute("tableData",tableData);
				
				if(("preAuthSummary".equals(reportId)))
					return this.getForward(strPreAuthSummaryReport, mapping, request);
				else if(("preAuthDetailed".equals(reportId)))
					return this.getForward(strPreAuthDetailedReport, mapping, request);
				else if(("claimSummary".equals(reportId)))
					return this.getForward(strClaimSummaryReport, mapping, request);
				else if(("claimDetailed".equals(reportId)))
					return this.getForward(strClaimDetailedReport, mapping, request);

				return null;
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strSubmission));
			}//end of catch(Exception exp)
		}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	 
	 
	 
	 
	 
	 
	   public ActionForward doBack(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doBack method of ProviderReportsAction");
			setOnlineLinks(request);
			HttpSession session=request.getSession();
			UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			//userSecurityProfile.getSecurityProfile().get
			
			
			return this.getForward(strProviderReportsList,mapping,request);
			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,strProviderReportsList));
		}//end of catch(Exception exp)
	}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)
	 
	
	 
//do search reports data
	 
	 
	 public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			try{
				log.debug("Inside the doSearch method of ProviderReportsAction");
				setLinks(request);
				
				HttpSession session=request.getSession();
				 OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
				session.getAttribute("reportId");
				String strForwardPathList="";
				String strReportId=request.getParameter("reportId");
				Long hospSeqID=userSecurityProfile.getHospSeqId();
				ArrayList alReportList = null;
														
		    	DynaActionForm frmProviderReportsList= (DynaActionForm) form;
				TableData tableData =TTKCommon.getTableData(request);
				
				//clear the dynaform if visting from left links for the first time
				//else get the dynaform data from session
				if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				{
					((DynaActionForm)form).initialize(mapping);//reset the form data
				}// end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				
				//DynaActionForm frmProviderReportsList= (DynaActionForm) form;
				
				String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
				String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
				
				//if the page number or sort id is clicked
				if(!strPageID.equals("") || !strSortID.equals(""))
				{
					if(!strPageID.equals(""))
					{
						tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
						
						 if("preAuthSummary".equals(strReportId))
						 {  //  clmSubmissionTable	
							 strForwardPathList=strPreAuthSummaryReport;
						 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
						 else if("preAuthDetailed".equals(strReportId))
						 {   
							 strForwardPathList=strPreAuthDetailedReport;
			
						 }//end of if(strActiveSubLink.equalsIgnoreCase(strInsCompReports))
						 else if("claimSummary".equals(strReportId))
						 {   
							 strForwardPathList=strClaimSummaryReport;
						 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
						 else if("claimDetailed".equals(strReportId))
						 { 												 							 
							 strForwardPathList=strClaimDetailedReport;
						 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
						 return mapping.findForward(strForwardPathList);
					}///end of if(!strPageID.equals(""))
					else
					{
						tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
						tableData.modifySearchData("sort");//modify the search data
					}//end of else
				}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{ 
					  //create the required grid table
					 if("preAuthSummary".equals(strReportId))
					 {  //  clmSubmissionTable
						 tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
						 tableData.modifySearchData("search");
						 alReportList= onlineAccessManagerObject.getProviderPreAuthReportData(tableData.getSearchData(),hospSeqID,"PSR");	 
						 tableData.createTableInfo("preAuthReportTable",alReportList);
						 ((Column)((ArrayList)tableData.getTitle()).get(9)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);	// Discount Amount
						 ((Column)((ArrayList)tableData.getTitle()).get(16)).setVisibility(false);
						 frmProviderReportsList.set("reportName", "Pre-Approval Summary Report");
						 strForwardPathList=strPreAuthSummaryReport;
						 
					 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
					 else if("preAuthDetailed".equals(strReportId))
					 {   
						 tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
						 tableData.modifySearchData("search");
						 alReportList= onlineAccessManagerObject.getProviderPreAuthReportData(tableData.getSearchData(),hospSeqID,"PDR");	 
						 tableData.createTableInfo("preAuthReportTable",alReportList);
						 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);	// Discount Amount
						 frmProviderReportsList.set("reportName", "Pre-Approval Detailed Report");
						 strForwardPathList=strPreAuthDetailedReport;
						 
					 }//end of if(strActiveSubLink.equalsIgnoreCase(strInsCompReports))
					 else if("claimSummary".equals(strReportId))
					 {   
						 tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
						 tableData.modifySearchData("search");
						 alReportList= onlineAccessManagerObject.getProviderClaimReportData(tableData.getSearchData(),hospSeqID,"CSR");	 
						 tableData.createTableInfo("claimReportTable",alReportList);
						 ((Column)((ArrayList)tableData.getTitle()).get(11)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(19)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(22)).setVisibility(false);
						 frmProviderReportsList.set("reportName", "Claim Summary Report");
						 strForwardPathList=strClaimSummaryReport;
					 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
					 else if("claimDetailed".equals(strReportId))
					 { 
						 tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
						 tableData.modifySearchData("search");
						 alReportList= onlineAccessManagerObject.getProviderClaimReportData(tableData.getSearchData(),hospSeqID,"CDR");	 
						 tableData.createTableInfo("claimReportTable",alReportList);
						 frmProviderReportsList.set("reportName", "Claim Detailed Report");
						 strForwardPathList=strClaimDetailedReport;
						 
					 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
					 
					
			}//end of else
														
				
											
				tableData.setData(alReportList, "search");
				//set the table data object to session
				
				request.getSession().setAttribute("tableData",tableData);
				request.getSession().setAttribute("frmProviderReportsList",frmProviderReportsList);
				return this.getForward(strForwardPathList, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strSubmission));
			}//end of catch(Exception exp)
		}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
	 
	/**
		 * Return the ArrayList populated with Search criteria elements
		 * @param DynaActionForm
		 * @param HttpServletRequest
		 * @return ArrayList
		 */
		private ArrayList<Object> populateSearchCriteria(DynaActionForm frmProviderReportsList,HttpServletRequest request)
		{
			String strReportId=request.getParameter("reportId");
			ArrayList<Object> alSearchParams =new ArrayList<Object>();
			if(("preAuthSummary".equals(strReportId))||(("preAuthDetailed".equals(strReportId)))){
			alSearchParams.add(frmProviderReportsList.getString("preApprovalNo"));//1
			alSearchParams.add(frmProviderReportsList.getString("fromDate"));//2
			alSearchParams.add(frmProviderReportsList.getString("toDate"));//3
			alSearchParams.add(frmProviderReportsList.getString("patientName"));//4
			alSearchParams.add(frmProviderReportsList.getString("authNo"));	//5
			alSearchParams.add(frmProviderReportsList.getString("doctorName"));//6
			alSearchParams.add(frmProviderReportsList.getString("alKootId"));//7
			alSearchParams.add(frmProviderReportsList.getString("benefitType"));//8
			alSearchParams.add(frmProviderReportsList.getString("claimStatus"));//9
			alSearchParams.add(frmProviderReportsList.getString("eventRefNo"));//10
			alSearchParams.add(frmProviderReportsList.getString("qatarId"));//11
			alSearchParams.add(frmProviderReportsList.getString("RefNo"));//12
			}
			if(("claimSummary".equals(strReportId))||(("claimDetailed".equals(strReportId)))){
			alSearchParams.add(frmProviderReportsList.getString("tmtfromDate"));//1
			alSearchParams.add(frmProviderReportsList.getString("tmttoDate"));//2
			alSearchParams.add(frmProviderReportsList.getString("clmFromDate"));//3
			alSearchParams.add(frmProviderReportsList.getString("clmToDate"));//4
			alSearchParams.add(frmProviderReportsList.getString("patientName"));//5
			alSearchParams.add(frmProviderReportsList.getString("claimStatus"));//6
			alSearchParams.add(frmProviderReportsList.getString("invoiceNo"));//7
			alSearchParams.add(frmProviderReportsList.getString("batchNo"));//8
			alSearchParams.add(frmProviderReportsList.getString("alKootId"));//9
			alSearchParams.add(frmProviderReportsList.getString("claimNo"));//10
			alSearchParams.add(frmProviderReportsList.getString("benefitType"));//11
			alSearchParams.add(frmProviderReportsList.getString("eventRefNo"));//12
			alSearchParams.add(frmProviderReportsList.getString("qatarId"));//13
			alSearchParams.add(frmProviderReportsList.getString("payRefNo"));//14
			}

			return alSearchParams;
		}//end of populateSearchCriteria(DynaActionForm frmBatchList,HttpServletRequest request)
		
	 
	 /**
	    * Returns the onlineAccessManager session object for invoking methods on it.
	    * @return onlineAccessManager session object which can be used for method invocation
	    * @exception throws TTKException
	    */
	   private OnlineAccessManager getOnlineAccessManagerObject() throws TTKException
	   {
	   	OnlineAccessManager onlineAccessManager = null;
	   	try
	   	{
	   		if(onlineAccessManager == null)
	   		{
	   			InitialContext ctx = new InitialContext();
	   			onlineAccessManager = (OnlineAccessManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineAccessManagerBean!com.ttk.business.onlineforms.OnlineAccessManager");
	   			log.debug("Inside getOnlineAccessManagerObject: onlineAccessManager: " + onlineAccessManager);
	   		}//end if
	   	}//end of try
	   	catch(Exception exp)
	   	{
	   		throw new TTKException(exp, "failure");
	   	}//end of catch
	   	return onlineAccessManager;
	   }//end of getOnlineAccessManagerObject()

	   
  //Provider report doGenerateReport
    
    public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
  	 try{
	    		log.debug("Inside the doGenerateClimSummaryReport method of HospitalReportsAction");
	    		setLinks(request);
	    		
	    		JasperReport mainJasperReport,emptyReport;
	    		TTKReportDataSource mainTtkReportDataSource = null;
	    		JasperPrint mainJasperPrint = null;
	    		 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	    		
	    		 String strhospSeqId= ""+userSecurityProfile.getHospSeqId();
	    		
	    		 String strReportId=request.getParameter("reportId");
	    		 String strReportId1 = "";
	    		 String strTypeOfReport=null;
	    		 String parameter=request.getParameter("parameter");
	    		 
	    		 //String strParam1=parameter;
	    		 
	    		 if(("preAuthSummary".equals(strReportId))||(("preAuthDetailed".equals(strReportId)))){
		    			strTypeOfReport="providerLoginPreAuthReports";
		    			if("preAuthSummary".equals(strReportId))
		    				strReportId1="PSR";
		    			else
		    				strReportId1="PDR";
		    		}
	    		 
		    	 if(("claimSummary".equals(strReportId))||(("claimDetailed".equals(strReportId)))){
		    			strTypeOfReport="providerLoginClaimReports";
		    			if("claimSummary".equals(strReportId))
		    				strReportId1="CSR";
		    			else
		    				strReportId1="CDR";
		    	 }
	    		 
	         	String strParam1= "|"+ strReportId1+"|"+strhospSeqId+parameter;
	         	
	    		String jrxmlfile=null;
	    		jrxmlfile=request.getParameter("fileName");
	    		try
	    		{
	    			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			
	    			HashMap<String,String> hashMap = new HashMap<String,String>();
	    			
	    			ByteArrayOutputStream boas=new ByteArrayOutputStream();
	    			
	    			
	    			
	    			mainTtkReportDataSource = new TTKReportDataSource(strTypeOfReport, strParam1);
	    			
	    			ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
	    			 
	    			
	    			mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
	    			
	    			if (main_report_RS != null && main_report_RS.next()) {
	    				main_report_RS.beforeFirst();
	    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);
	    				  				
	    			} else {
	    				
	    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
	    			}
	    			
	    			if("EXCEL".equals(request.getParameter("reportType")))
		    		{
	    				
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
	    			
	    			return (mapping.findForward(strReportdisplay));
	    		}
	    		catch(Exception exp)
	        	{
	    			return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	        	}
	    		
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
  
  
  
  
  
  
  
  
 
  
	 
	 
  
	 
	 
	
	 
	 
	 

	 
	 
	
	 
	 
	 
	 
 		
 		
 		
 		
 		
 		
 		
 		
 		

		
	 
	 
