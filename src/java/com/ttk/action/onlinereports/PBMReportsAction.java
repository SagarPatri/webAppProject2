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

 public class PBMReportsAction extends TTKAction{

	 private static final Logger log = Logger.getLogger( PBMReportsAction.class );

	 //declaration of forward paths
	 private static final String strForward="Forward";
		private static final String strBackward="Backward";
	 private static final String strSubmission="submission";
	 private static final String strPayment="payment";
	 private static final String strDispense="dispense";
	 private static final String strBrokerRpt="brokerreports";

	 private static final String strAccentureCustomizedHRParameter="customisedreportsparameter";

	 private static final String strInsCompParameter="inscompreportsparameter";
	 private static final String strReportdisplay="reportdisplay";
	 private static final String strCustomisedRpt="customisedreports";
	 private static final String strBrokerParameter="brokerreportsparameter";
	 

	 //declaration of constants
	 private static final String strHRReports="HR";
	 private static final String strInsCompReports="Insurance Company";
	 private static final String strCustomisedReports="Customize Reports";
	 private static final String strBrokerReports="View Reports";

	 private static final String strHospitalReports="Hospital";
	 private static final String strPBMReportsList="PBMReportsList";
	 private static final String strHospitalParameter="hospreportsparameter";


	 //Exception Message Identifier
	 private static final String strReportExp="onlinereport";
	 private static final String strClaimSummery="claimsummery";
	 private static final String strClaimDetails="claimdetails";
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
				
			 return this.getForward(strPBMReportsList,mapping,request);
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
				DynaActionForm frmPBMReportsList= (DynaActionForm) form;
				
				
				
				
				
			 //set the Forward Paths based on the sublink
			 if("clmSubmissionId".equals(reportId))
			 {  //  clmSubmissionTable
				
				 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
				 frmPBMReportsList.set("reportName", "Claim wise submission report");
				 
				 ((Column)((ArrayList)tableData.getTitle()).get(7)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(16)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(18)).setVisibility(false);
				 strForwardPathList=strSubmission;
				 
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 else if("drugSubmissionId".equals(reportId))
			 {   
				 frmPBMReportsList.set("reportName", "Drug wise claim submission report");
				 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
				 ((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(18)).setVisibility(false);
				 
				 strForwardPathList=strSubmission;
				 
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strInsCompReports))
			 else if("clmPaymentId".equals(reportId))
			 {  
				 frmPBMReportsList.set("reportName", "Claim wise payment report");
				 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
				 
				 ((Column)((ArrayList)tableData.getTitle()).get(7)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(16)).setVisibility(false);
				 strForwardPathList=strSubmission;
				 
			 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
			 else if("drugPaymentId".equals(reportId))
			 { 
				 frmPBMReportsList.set("reportName", "Drug wise payment report");
				 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
				 ((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
				 strForwardPathList=strSubmission;
				 
			 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
			 
			 else if("clmDispenseId".equals(reportId))
			 { 
				 frmPBMReportsList.set("reportName", "Claim wise dispense status report");
				 tableData.createTableInfo("PbmClaimListTable",new ArrayList());
				 
				 ((Column)((ArrayList)tableData.getTitle()).get(0)).setVisibility(false);
				  ((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false);
				  ((Column)((ArrayList)tableData.getTitle()).get(9)).setVisibility(false);
				  ((Column)((ArrayList)tableData.getTitle()).get(14)).setVisibility(false);
				 strForwardPathList=strDispense;
				 
			 }//end of else if((strActiveLink.equals(strBrokerReports))
			 else if("drugDispenseId".equals(reportId))
			 {  
				 frmPBMReportsList.set("reportName", "Drug wise dispense status report");
				 
				 tableData.createTableInfo("PbmClaimListTable",new ArrayList());
				 ((Column)((ArrayList)tableData.getTitle()).get(0)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(14)).setVisibility(false);
				 strForwardPathList=strDispense;
				 
			 }//end of else if((strActiveLink.equals(strBrokerReports))
			 else if("claimSummary".equals(reportId))
			 {  //  clmSubmissionTable
				
				 tableData.createTableInfo("PBMClaimsReportTable",new ArrayList());
				 frmPBMReportsList.set("reportName", "Claim Summary Report");
				 ((Column)((ArrayList)tableData.getTitle()).get(11)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);
				 ((Column)((ArrayList)tableData.getTitle()).get(19)).setVisibility(false);
				 strForwardPathList=strClaimSummery;
				 
			 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
			 else if("claimDetailed".equals(reportId))
			 {   
				 tableData.createTableInfo("PBMClaimsReportTable",new ArrayList());
				 frmPBMReportsList.set("reportName", "Claim Detailed Report");
				 
				 strForwardPathList=strClaimDetails;
				 
			 }
			 request.getSession().setAttribute("tableData",tableData);
			
			 request.getSession().setAttribute("reportId",reportId);
			request.getSession().setAttribute("frmPBMReportsList",frmPBMReportsList);//reset the form data
			
			
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
				log.debug("Inside the doBackward method of PbmPreauthAction");
				setLinks(request);
				String reportId=request.getParameter("reportId");
				OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
				HttpSession session=request.getSession();
				 
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
				
				Long hospSeqID=userSecurityProfile.getHospSeqId();
				TableData tableData = TTKCommon.getTableData(request);
				tableData.modifySearchData(strBackward);//modify the search data
				//fetch the data from the data access layer and set the data to table object
				ArrayList alClmDrugList= onlineAccessManagerObject.getPbmReportData(tableData.getSearchData(),hospSeqID,reportId);  // Populating Data Which matches the search criteria
				tableData.setData(alClmDrugList,strBackward);
				//set the table data object to session
				request.getSession().setAttribute("tableData",tableData);
				
				if("clmSubmissionId".equals(reportId)||"drugSubmissionId".equals(reportId)||"clmPaymentId".equals(reportId)||"drugPaymentId".equals(reportId))
				 {
				
					return this.getForward(strSubmission, mapping, request);
				 }
				
				return this.getForward(strDispense, mapping, request);
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
				log.debug("Inside the doForward method of PbmPreauthAction");
				setLinks(request);
				String reportId=request.getParameter("reportId");
				HttpSession session=request.getSession();
				 
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
				
				String strForwardPathList="";
				String strReportId=request.getParameter("reportId");
				Long hospSeqID=userSecurityProfile.getHospSeqId();
				OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
				TableData tableData = TTKCommon.getTableData(request);
				tableData.modifySearchData(strForward);//modify the search data
				//fetch the data from the data access layer and set the data to table object
				ArrayList alClmDrugList= onlineAccessManagerObject.getPbmReportData(tableData.getSearchData(),hospSeqID,strReportId); // Populating Data Which matches the search criteria
				tableData.setData(alClmDrugList,strForward);
				//set the table data object to session
				request.getSession().setAttribute("tableData",tableData);
				if("clmSubmissionId".equals(reportId)||"drugSubmissionId".equals(reportId)||"clmPaymentId".equals(reportId)||"drugPaymentId".equals(reportId))
				 {
				
					return this.getForward(strSubmission, mapping, request);
				 }
				
				return this.getForward(strDispense, mapping, request);
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
			log.debug("Inside the doBack method of PbmPharmacyGeneralAction");
			setOnlineLinks(request);
			HttpSession session=request.getSession();
			UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			//userSecurityProfile.getSecurityProfile().get
			
			
			return this.getForward(strPBMReportsList,mapping,request);
			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPBMReportsList));
		}//end of catch(Exception exp)
	}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)
	 
	
	 
//do search reports data
	 
	 
	 public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			try{
				log.debug("Inside the doSearch method of PbmPreauthAction");
				setLinks(request);
				
				HttpSession session=request.getSession();
				 OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
				session.getAttribute("reportId");
				String strForwardPathList="";
				String strReportId=request.getParameter("reportId");
				Long hospSeqID=userSecurityProfile.getHospSeqId();
				ArrayList alReportList = null;										
		    	DynaActionForm frmPBMReportsList= (DynaActionForm) form;
				TableData tableData =TTKCommon.getTableData(request);
				//clear the dynaform if visting from left links for the first time
				//else get the dynaform data from session
				if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				{
					((DynaActionForm)form).initialize(mapping);//reset the form data
				}// end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				
				//DynaActionForm frmPBMReportsList= (DynaActionForm) form;
				
				String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
				String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
				//if the page number or sort id is clicked
				if(!strPageID.equals("") || !strSortID.equals(""))
				{
					if(!strPageID.equals(""))
					{
												
						
						tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
						
						 if("clmSubmissionId".equals(strReportId))
						 {  //  clmSubmissionTable
							
						 strForwardPathList=strSubmission;
							 
						 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
						 else if("drugSubmissionId".equals(strReportId))
						 {   
													 						 
							 strForwardPathList=strSubmission;
			
						 }//end of if(strActiveSubLink.equalsIgnoreCase(strInsCompReports))
						 else if("clmPaymentId".equals(strReportId))
						 {   
							 
							 strForwardPathList=strSubmission;
							 
						 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
						 else if("drugPaymentId".equals(strReportId))
						 { 
							 														 							 
							 strForwardPathList=strSubmission;
							 
						 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
						 
						 else if("clmDispenseId".equals(strReportId))
						 { 
							 
							 strForwardPathList=strDispense;
							 
						 }//end of else if((strActiveLink.equals(strBrokerReports))
						 else if("drugDispenseId".equals(strReportId))
						 {  
							
							 strForwardPathList=strDispense;
							 
						 }//end of else if((strActiveLink.equals(strBrokerReports))
						 else if("claimSummary".equals(strReportId))
						 {   
							 strForwardPathList=strClaimSummery;
						 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
						 else if("claimDetailed".equals(strReportId))
						 { 												 							 
							 strForwardPathList=strClaimDetails;
						 }
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
					 if("clmSubmissionId".equals(strReportId))
					 {  //  clmSubmissionTable
						
						 
						 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
						 frmPBMReportsList.set("reportName", "Claim wise submission report");
						 
						 ((Column)((ArrayList)tableData.getTitle()).get(7)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(16)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(18)).setVisibility(false);
						 strForwardPathList=strSubmission;
						 
					 }//end of if(strActiveSubLink.equalsIgnoreCase(strEnrollment))
					 else if("drugSubmissionId".equals(strReportId))
					 {   
						
					 
						 frmPBMReportsList.set("reportName", "Drug wise claim submission report");
						 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
						 ((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(18)).setVisibility(false);
						 
						 strForwardPathList=strSubmission;
						 
					 }//end of if(strActiveSubLink.equalsIgnoreCase(strInsCompReports))
					 else if("clmPaymentId".equals(strReportId))
					 {   
						 
						 
						 frmPBMReportsList.set("reportName", "Claim wise payment report");
						 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
						 
						 ((Column)((ArrayList)tableData.getTitle()).get(7)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(16)).setVisibility(false);
						 
						 strForwardPathList=strSubmission;
						 
					 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
					 else if("drugPaymentId".equals(strReportId))
					 { 
						 
						
						 
						 frmPBMReportsList.set("reportName", "Drug wise payment report");
						 tableData.createTableInfo("clmDrugDetailsTable",new ArrayList());
						 ((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
						 strForwardPathList=strSubmission;
						 
					 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
					 
					 else if("clmDispenseId".equals(strReportId))
					 { 
						 frmPBMReportsList.set("reportName", "Claim wise dispense status report");
						 tableData.createTableInfo("PbmClaimListTable",new ArrayList());
						 
						  ((Column)((ArrayList)tableData.getTitle()).get(0)).setVisibility(false);
						  ((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false);
						  ((Column)((ArrayList)tableData.getTitle()).get(9)).setVisibility(false);
						  ((Column)((ArrayList)tableData.getTitle()).get(14)).setVisibility(false);
						  
						  ((Column)((ArrayList)tableData.getTitle()).get(7)).setIsLink(false);
						  ((Column)((ArrayList)tableData.getTitle()).get(13)).setIsLink(false);
						 strForwardPathList=strDispense;
						 
					 }//end of else if((strActiveLink.equals(strBrokerReports))
					 else if("drugDispenseId".equals(strReportId))
					 {  
						 frmPBMReportsList.set("reportName", "Drug wise dispense status report");
						 
						 tableData.createTableInfo("PbmClaimListTable",new ArrayList());
						 ((Column)((ArrayList)tableData.getTitle()).get(0)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(14)).setVisibility(false);
						
						 ((Column)((ArrayList)tableData.getTitle()).get(7)).setIsLink(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(13)).setIsLink(false);
						 
						 strForwardPathList=strDispense;
						 
					 }//end of else if((strActiveLink.equals(strBrokerReports))
					 
					// tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
					// tableData.modifySearchData("search");
					 
					 else if("claimSummary".equals(strReportId))
					 {   
						 tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
						 tableData.modifySearchData("search");
						 alReportList= onlineAccessManagerObject.getPbmReportData(tableData.getSearchData(),hospSeqID,"CSR");	 
						 tableData.createTableInfo("PBMClaimsReportTable",alReportList);
						 ((Column)((ArrayList)tableData.getTitle()).get(11)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(19)).setVisibility(false);
						 ((Column)((ArrayList)tableData.getTitle()).get(22)).setVisibility(false);
						 frmPBMReportsList.set("reportName", "Claim Summary Report");
						 strForwardPathList=strClaimSummery;
					 }//end of else if((strActiveLink.equals(strCustomisedReports)) && (strActiveSubLink.equals(strCustomisedReports)))
					 else if("claimDetailed".equals(strReportId))
					 { 
						 tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
						 tableData.modifySearchData("search");
						 alReportList= onlineAccessManagerObject.getPbmReportData(tableData.getSearchData(),hospSeqID,"CDR");	 
						 tableData.createTableInfo("PBMClaimsReportTable",alReportList);
						 frmPBMReportsList.set("reportName", "Claim Detailed Report");
						 strForwardPathList=strClaimDetails;
						 
					 }
			}//end of else
														
			//	ArrayList alClaimList= onlineAccessManagerObject.getPbmReportData(tableData.getSearchData(),hospSeqID,strReportId);
											
				tableData.setData(alReportList, "search");
				//set the table data object to session
				
				request.getSession().setAttribute("tableData",tableData);
				request.getSession().setAttribute("frmPBMReportsList",frmPBMReportsList);
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
		private ArrayList<Object> populateSearchCriteria(DynaActionForm frmPBMReportsList,HttpServletRequest request)
		{/*
			ArrayList<Object> alSearchParams =new ArrayList<Object>();
			
			alSearchParams.add(frmPBMReportsList.getString("trtmtFromDate"));//1
			alSearchParams.add(frmPBMReportsList.getString("trtmtToDate"));//2
			alSearchParams.add(frmPBMReportsList.getString("clmFromDate"));//3
			alSearchParams.add(frmPBMReportsList.getString("clmToDate"));//4
			alSearchParams.add(frmPBMReportsList.getString("patientName"));//5
			alSearchParams.add(frmPBMReportsList.getString("claimStatus"));//6
			alSearchParams.add(frmPBMReportsList.getString("invoiceNumber"));//7
			alSearchParams.add(frmPBMReportsList.getString("claimNumber"));	//8
			alSearchParams.add(frmPBMReportsList.getString("alKootId"));//9
			alSearchParams.add(frmPBMReportsList.getString("eventRefNo"));//10
			alSearchParams.add(frmPBMReportsList.getString("authNo"));	//11
			alSearchParams.add(frmPBMReportsList.getString("clmDispStatus"));//12
			alSearchParams.add(frmPBMReportsList.getString("preApprovalNo"));//13 
			alSearchParams.add(frmPBMReportsList.getString("batchNo"));
			alSearchParams.add(frmPBMReportsList.getString("clmPayStatus"));
			alSearchParams.add(frmPBMReportsList.getString("paymentRefNo"));
			
			
			return alSearchParams;
		*/

			String strReportId=request.getParameter("reportId");
			ArrayList<Object> alSearchParams =new ArrayList<Object>();
			if(("claimSummary".equals(strReportId))||(("claimDetailed".equals(strReportId)))){
			alSearchParams.add(frmPBMReportsList.getString("tmtfromDate"));//1
			alSearchParams.add(frmPBMReportsList.getString("tmttoDate"));//2
			alSearchParams.add(frmPBMReportsList.getString("clmFromDate"));//3
			alSearchParams.add(frmPBMReportsList.getString("clmToDate"));//4
			alSearchParams.add(frmPBMReportsList.getString("patientName"));//5
			alSearchParams.add(frmPBMReportsList.getString("claimStatus"));//6
			alSearchParams.add(frmPBMReportsList.getString("invoiceNo"));//7
			alSearchParams.add(frmPBMReportsList.getString("batchNo"));//8
			alSearchParams.add(frmPBMReportsList.getString("alKootId"));//9
			alSearchParams.add(frmPBMReportsList.getString("claimNo"));//10
			alSearchParams.add(frmPBMReportsList.getString("benefitType"));//11
			alSearchParams.add(frmPBMReportsList.getString("eventRefNo"));//12
			alSearchParams.add(frmPBMReportsList.getString("qatarId"));//13
			alSearchParams.add(frmPBMReportsList.getString("payRefNo"));//14
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

	   
  //PBM report doGenerateReport
    
    public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
  	 try{
	    		log.debug("Inside the doGenerateClimSummaryReport method of HospitalReportsAction");
	    		setLinks(request);
	    		
	    		JasperReport mainJasperReport,emptyReport;
	    		TTKReportDataSource mainTtkReportDataSource = null;
	    		JasperPrint mainJasperPrint = null;
	    		 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	    		
	    		 String strEmpanelNumber = userSecurityProfile.getEmpanelNumber();
	    		 String strhospSeqId= ""+userSecurityProfile.getHospSeqId();
	    		 String strReportId=request.getParameter("reportId");
	    		 String strTypeOfReport=null;
	    		 String strReportId1 = "";
	    		 String parameter=request.getParameter("parameter");
	    		 
	    		 //String strParam1=parameter;
	    		 
	    		 if(("claimSummary".equals(strReportId))||(("claimDetailed".equals(strReportId)))){
		    			strTypeOfReport="PBMLoginClaimReports";
		    			if("claimSummary".equals(strReportId))
		    				strReportId1="CSR";
		    			else
		    				strReportId1="CDR";
		    	 }
	    		 
	         	String strParam1= "|"+ strReportId1+"|"+strhospSeqId+parameter;
	         	
	    		 
	      //   	String strParam1= "|"+ strReportId+"|"+strEmpanelNumber+parameter;
	         	
	    		String jrxmlfile=null;
	    		jrxmlfile=request.getParameter("fileName");
	    		try
	    		{
	    			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			
	    			HashMap<String,String> hashMap = new HashMap<String,String>();
	    			
	    			ByteArrayOutputStream boas=new ByteArrayOutputStream();
	    			
	    		//	strTypeOfReport="pbmClaimDrugReport";
	    			
	    			/*if("clmSubmissionId".equals(strReportId))
	    			{
	    				strTypeOfReport="claimSubmissionReport";
	    			}
	    			else if("drugSubmissionId".equals(strReportId))
	    			{
	    				strTypeOfReport="drugSubmissionReport";
	    			}
	    			else if("clmPaymentId".equals(strReportId))
	    			{
	    				strTypeOfReport="claimPaymentReport";
	    			}
	    			else if("drugPaymentId".equals(strReportId))
	    			{
	    				strTypeOfReport="drugPaymentReport";
	    			}
	    			else if("clmDispenseId".equals(strReportId))
	    			{
	    				strTypeOfReport="claimDispenseReport";
	    			}
	    			else if("drugDispenseId".equals(strReportId))
	    			{
	    				strTypeOfReport="drugDispenseReport";
	    			}
	    			*/
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
	    			
	    			
	    		}
	    		catch(Exception exp)
	        	{
	    			return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
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
	
  
  
  
  
  
  
  
 
  
	 
	 
  
	 
	 
	
	 
	 
	 

	 
	 
	
	 
	 
	 
	 
 		
 		
 		
 		
 		
 		
 		
 		
 		

		
	 
	 
 }//end of OnlineReportsAction