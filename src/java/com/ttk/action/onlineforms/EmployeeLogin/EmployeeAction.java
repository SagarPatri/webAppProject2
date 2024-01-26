/**
 * @ (#) OnlinePoliciesAction.java July 24,2006
 * Project       : TTK HealthCare Services
 * File          : OnlinePoliciesAction.java
 * Author        : Harsha Vardhan B N
 * Company       : Span Systems Corporation
 * Date Created  : July 24,2006
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.onlineforms.EmployeeLogin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;
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
import org.apache.struts.upload.FormFile;
import org.dom4j.Document;

import com.asprise.util.pdf.ar;
import com.asprise.util.pdf.em;
import com.asprise.util.pdf.ex;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.action.tree.TreeData;
import com.ttk.business.administration.RuleManager;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.empanelment.AccountDetailVO;
import com.ttk.dto.enrollment.HRFilesDetailsVO;
import com.ttk.dto.enrollment.MemberAddressVO;
import com.ttk.dto.enrollment.MemberDetailVO;
import com.ttk.dto.enrollment.MemberVO;
import com.ttk.dto.enrollment.OnlineAccessPolicyVO;
import com.ttk.dto.onlineforms.DependentDetailVO;
import com.ttk.dto.onlineforms.MemberPermVO;
import com.ttk.dto.onlineforms.OnlineIntimationVO;
import com.ttk.dto.onlineforms.insuranceLogin.EmployeeShortfall;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.ShortfallVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;
import com.ttk.dto.enrollment.MemberDetailVO;

import formdef.plugin.util.FormUtils;


/**
 * This class is used to view the List of Policies
 */
public class EmployeeAction extends TTKAction {
	private static Logger log = Logger.getLogger( EmployeeAction.class );
	//  Action mapping forwards.
	private static final String strForward ="Forward";
	private static final String strBackward ="Backward";
    private static final String strDashBoard="dashBoard"; 
    private static final String strSearch="search"; 
    private static final String strCorporateList="corporatelist";
    //Exception Message Identifier
    private static final String strOnlinePolicies="onlinePolicies";
    private static final String strOnlinePolicyDetails="onlinepolicydetails";
    private static final String strFailure="failure";
    private static final String strEmplHome="onlineEmpHome";
    private static final String strEmplSearchHome="EmployeePreAuthSearchHome";
    private static final String strEmplSearchData="EmployeePreAuthSearchData";
    private static final String strEmplClaimSearchHome="EmployeeClaimSearchHome";
    private static final String strEmplClaimSearchData="EmployeeClaimSearchData";
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
    
    public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws TTKException{
    	try{
    		setOnlineLinks(request);
    		DynaActionForm employeeHomeForm= (DynaActionForm) form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
    		TableData tableData=new TableData();
    		tableData.createTableInfo("EmployeeDataTable",null);	
//    		ArrayList<Object> homeData = onlineAccessObject.getOnlineHomeDetails(null,null,userSecurityProfile.getPolicyGrpSeqID(),"EMPL",userSecurityProfile.getUSER_ID());
    		ArrayList<Object> homeData=onlineAccessObject.getOnlineHomeDetails(null, null, userSecurityProfile.getPolicyGrpSeqID(), userSecurityProfile.getLoginType(), userSecurityProfile.getUSER_ID());
    		for (Object object : (ArrayList) homeData.get(1)) {
				MemberVO memberVO=(MemberVO) object;
				if("Cancelled".equals(memberVO.getEmpStatusDesc())){
					((Column)((ArrayList)tableData.getTitle()).get(8)).setLinkParamMethodName("CANCELLEDMEMBER");
				}
			}
    		if(homeData.size()>1){
        		tableData.setData((ArrayList) homeData.get(1), "search");
			request.getSession().setAttribute("employeeDataTable",tableData);
			MemberDetailVO memberDetailVO= (MemberDetailVO) homeData.get(0);
			request.getSession().setAttribute("homeMemberDetail", memberDetailVO);
			employeeHomeForm = (DynaActionForm) FormUtils.setFormValues("employeeHomeForm", memberDetailVO, this, mapping,request);
			request.getSession().setAttribute("employeeHomeForm", employeeHomeForm);
			request.getSession().setAttribute("prodPolicySeqID", memberDetailVO.getPolicySeqID());
			request.setAttribute("onlinehome", request.getSession().getAttribute("onlinehome"));
    		}    		
    		return this.getForward(strEmplHome, mapping, request);
    		
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    public ActionForward  doViewPolicyTOB(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException { 
		log.debug("Inside OnlineMemberDetails viewEmpBenefitDetails");
		ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
	    setOnlineLinks(request);
	  try{  
			 OnlineAccessManager onlineAccessManager	=	null;
			 onlineAccessManager = this.getOnlineAccessEmplManagerObject();		
			  String prodPolicySeqID = request.getParameter("prodPolicySeqID");		 
			   InputStream iStream	=	onlineAccessManager.getEmpPolicyTobFile(prodPolicySeqID);
			   bis = new BufferedInputStream(iStream);
	           baos=new ByteArrayOutputStream();
	           response.setContentType("application/pdf");
	           int ch;
	                 while ((ch = bis.read()) != -1) baos.write(ch);
	                 sos = response.getOutputStream();
	                 baos.writeTo(sos);  
	                 baos.flush();      
	                 sos.flush(); 
	  	}
	  					catch(TTKException expTTK)
        				{
		  					request.setAttribute("isDoViewPolicyTOB", true);	
	  						return this.processOnlineExceptions(request, mapping, expTTK);
        				}//end of catch(TTKException expTTK)
	  
	  					catch(Exception exp)
		            	{
	  						request.setAttribute("isDoViewPolicyTOB", "true");
	  						return this.processExceptions(request, mapping, new TTKException(exp,""));
		            	}//end of catch(Exception exp)
		          finally{
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
    
    private OnlineAccessManager getOnlineAccessEmplManagerObject() throws TTKException
    {
    	OnlineAccessManager onlineAccessManager = null;
    	try
    	{
    		if(onlineAccessManager == null)
    		{
    			InitialContext ctx = new InitialContext();
    			onlineAccessManager = (OnlineAccessManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineAccessManagerBean!com.ttk.business.onlineforms.OnlineAccessManager");
    		}//end if
    	}//end of try
    	catch(Exception exp)
    	{
    		throw new TTKException(exp, strFailure);
    	}//end of catch
    	return onlineAccessManager;
    }//end of getOnlineAccessManagerObject()
    public ActionForward doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws TTKException{
    	try{
    		setOnlineLinks(request);
    		DynaActionForm employeeHomeForm= (DynaActionForm) form;
    		((DynaActionForm)employeeHomeForm).initialize(mapping);
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
    		TableData empPreAuthTable=new TableData();
    		ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
    		MemberDetailVO memberDetailVO= new MemberDetailVO();
    		if(userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claims")||userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claim Submission"))
    		empPreAuthTable.createTableInfo("EmpClaimHistoryTable",new ArrayList());
    		else
    			empPreAuthTable.createTableInfo("EmpPreAuthTable",new ArrayList());
    		TreeData treeData =(TreeData)request.getSession().getAttribute("treeData");
    		String selectedroot=request.getParameter("selectedroot");
    		String selectednode=request.getParameter("selectednode");
    		treeData =(TreeData)request.getSession().getAttribute("treeData") ;
			int iSelectedRoot= TTKCommon.getInt(selectedroot);
//			frmHistoryDetail.set("selectedroot",TTKCommon.checkNull(request.getParameter("selectedroot")));
//   		 frmHistoryDetail.set("selectednode",TTKCommon.checkNull(request.getParameter("selectednode")));
//   		 String strSelectedRoot = (String)frmHistoryDetail.get("selectedroot");
//   		 String strSelectedNode = (String)frmHistoryDetail.get("selectednode");
   		MemberVO memberVO=new MemberVO();
   		 if(!TTKCommon.checkNull(selectedroot).equals(""))
   		 {
   			 memberVO = (MemberVO)treeData.getSelectedObject(selectedroot,selectednode);
//   			 String strName1 =  memberVO.getName();
//   			 strClaimantName =  strName1.substring(strName1.indexOf("/")+1,strName1.lastIndexOf("/")-1).trim();
   		 }//end of if(!TTKCommon.checkNull(strSelectedRoot).equals("")) 
//			MemberVO memberVO=(MemberVO)treeData.getRootData().get(iSelectedRoot);
			String captionData=memberVO.getName();
			captionData=captionData.substring(0, captionData.indexOf("/")).trim();
			alAddEnrollment.add(memberVO.getPolicyGroupSeqID());
				alAddEnrollment.add(memberVO.getPolicySeqID());				
				alAddEnrollment.add(memberVO.getMemberSeqID());
				memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);
				ArrayList providerNameList=null;
				if(userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claims")||userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claim Submission"))
					providerNameList=onlineAccessObject.getProviderNameList(memberDetailVO.getMemberSeqID(),"CLAIM");
				else
					providerNameList=onlineAccessObject.getProviderNameList(memberDetailVO.getMemberSeqID(),"PREAUTH");
//    		employeeHomeForm.set("providerNameList", providerNameList);
    		if(userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claims")||userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claim Submission"))
    			request.getSession().setAttribute("EmpClaimHistoryTable", empPreAuthTable);
    		else
    		request.getSession().setAttribute("EmpPreAuthTable", empPreAuthTable);
    		request.getSession().setAttribute("providerNameList", providerNameList);
    		request.getSession().setAttribute("memberDetailVO", memberDetailVO);
    		request.getSession().setAttribute("memberVO", memberVO);
    		employeeHomeForm.set("caption", captionData);
    		request.getSession().setAttribute("employeeHomeForm", employeeHomeForm);
//    		return this.getForward(strEmplSearchHome, mapping, request);
    		if(userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claims")||userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claim Submission")){
    			return mapping.findForward(strEmplClaimSearchHome);
    		}else
    		return mapping.findForward(strEmplSearchHome);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws TTKException{
    	try{
    		log.info("This is doSearch method of EmployeeAction");
    		setOnlineLinks(request);
    		DynaActionForm employeeHomeForm= (DynaActionForm) form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
    		TableData empPreAuthTable=null;
    		if(request.getSession().getAttribute("EmpPreAuthTable")!=null)
    			empPreAuthTable=(TableData) request.getSession().getAttribute("EmpPreAuthTable");
    		else
    			empPreAuthTable=new TableData();
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		{
    			((DynaActionForm)form).initialize(mapping);//reset the form data
    		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		//if the page number or sort id is clicked
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{    
    				empPreAuthTable.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return mapping.findForward(strEmplSearchData);	
    			}///end of if(!strPageID.equals(""))
    			else
    			{
    				empPreAuthTable.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
    				empPreAuthTable.modifySearchData("sort");//modify the search data
    			}//end of else
    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
    		else
    		{
    			//create the required grid table
    			empPreAuthTable.createTableInfo("EmpPreAuthTable",null);
    			MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
    			empPreAuthTable.setSearchData(this.populateSearchCriteria(employeeHomeForm,memberDetailVO.getMemberSeqID()));
    			empPreAuthTable.setSortColumnName("TPA_ENROLLMENT_ID");
    			empPreAuthTable.modifySearchData("search");
    		}//end of else 
    		ArrayList custBankLogDetails=  onlineAccessObject.getMemberPreAuthDetails(empPreAuthTable.getSearchData(),"Administration");
    		empPreAuthTable.setData(custBankLogDetails, "search");
    		request.getSession().setAttribute("sStartDate", request.getParameter("sStartDate"));
    		request.getSession().setAttribute("sEndDate", request.getParameter("sEndDate"));
    		request.getSession().setAttribute("EmpPreAuthTable",empPreAuthTable);
    		return mapping.findForward(strEmplSearchData);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    
    public ActionForward doReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws TTKException{
    	try{
    		setOnlineLinks(request);
    		((DynaActionForm)form).initialize(mapping);
    		DynaActionForm employeeHomeForm= (DynaActionForm) form;
    		DynaActionForm claimSubmissionForm= (DynaActionForm) form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
    		ArrayList reportData=null;
    		if(TTKCommon.getActiveSubLink(request).equals("Pre-Approval"))
    			reportData=onlineAccessObject.getAndUpdateReportData("PAT",memberDetailVO.getPreAuthSeqId(),null,null);
    		
    		else
    			reportData=onlineAccessObject.getAndUpdateReportData("CLM",memberDetailVO.getClaimSeqID(),null,null);
    			memberDetailVO=(MemberDetailVO)reportData.get(1);
    			request.getSession().setAttribute("enr_address_seqId", memberDetailVO.getEndorsementSeqID());
    		employeeHomeForm = (DynaActionForm)FormUtils.setFormValues("employeeHomeForm",memberDetailVO,this,mapping,request);		
    		claimSubmissionForm = (DynaActionForm)FormUtils.setFormValues("claimSubmissionForm",memberDetailVO,this,mapping,request);		
    		employeeHomeForm.set("caption", request.getSession().getAttribute("strCaption"));
    		request.getSession().setAttribute("employeeHomeForm", employeeHomeForm);
    		request.getSession().setAttribute("editable", null);    		
    		request.getSession().setAttribute("claimSubmissionForm", claimSubmissionForm);
    	}catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
		}
    		/*return this.getForward(strEmplHome, mapping, request);*/
    		return mapping.findForward("forwardToReport");
    		
    	}//end of try
    
    public ActionForward doBackToPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws TTKException{
		try {
			setOnlineLinks(request);
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
			request.setAttribute("UserId", userSecurityProfile.getUSER_ID());
			((DynaActionForm) form).initialize(mapping);
			DynaActionForm employeeHomeForm = (DynaActionForm) form;
			request.setAttribute("historyDoc", request.getSession().getAttribute("historyDoc"));
			/*request.getSession().setAttribute("preauthDiagnosis",request.getSession().getAttribute("historyDoc"));
			request.getSession().setAttribute("preauthActivities",request.getSession().getAttribute("preauthActivities"));
			request.getSession().setAttribute("memberDetailVO",request.getSession().getAttribute("memberDetailVO"));
			request.getSession().setAttribute("requestedAmount",request.getSession().getAttribute("requestedAmount"));
			request.getSession().setAttribute("approvedAmount",request.getSession().getAttribute("approvedAmount"));*/
			request.getSession().setAttribute("employeeHomeForm",employeeHomeForm);
		}catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
		}
    		return mapping.findForward("newMemberPreAuthDetails");
    		
    	}//end of try 
    public ActionForward doSubmitReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	PrintWriter	writer= response.getWriter();
    		try{
    		setOnlineLinks(request);
//    		DynaActionForm employeeHomeForm=(DynaActionForm)form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		String finalRemarks=request.getParameter("finalRemarks");
    		MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
    		ArrayList reportData=null;
    		if(TTKCommon.getActiveSubLink(request).equals("Pre-Approval"))
    			reportData=onlineAccessObject.getAndUpdateReportData("PAT",memberDetailVO.getPreAuthSeqId(),finalRemarks,null);
    		else if(TTKCommon.getActiveSubLink(request).equals("Claims History"))
    			reportData=onlineAccessObject.getAndUpdateReportData("CLM",memberDetailVO.getClaimSeqID(),finalRemarks,null);
    		else if(TTKCommon.getActiveSubLink(request).equals("Contact Us")){
    			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
    			String queryType=request.getParameter("queryCategory");
    			reportData=onlineAccessObject.getAndUpdateReportData("HAQ",userSecurityProfile.getPolicyGrpSeqID(),finalRemarks,queryType);
    		}String row_proceed=(String) reportData.get(0);
    		memberDetailVO=(MemberDetailVO)reportData.get(1);
//    		employeeHomeForm = (DynaActionForm)FormUtils.setFormValues("employeeHomeForm",memberDetailVO,this,mapping,request);
//    		request.getSession().setAttribute("employeeHomeForm", employeeHomeForm);
    		if("Y".equals(row_proceed)){
    			writer.print("Your Query Will be attended <br/>by our representative shortly");
    		}else{
    			writer.print("Sorry, There is something went wrong. Please try later or call to Al koot Customer care.");
    		}
    		
    		}catch(TTKException expTTK)
    		{
    			writer.print("Something went wrong Please Contact Administrator");
    			return this.processOnlineExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			writer.print("Something went wrong Please Contact Administrator");
    			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
    		}finally{
    			writer.flush();
    			writer.close();
    		}
    		return null;    		
    	}//end of try
    public ActionForward doClaimHistorySearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws TTKException{
    	try{
    		setOnlineLinks(request);
    		log.info("This is doClaimHistorySearch method of EmployeeAction");
    		DynaActionForm employeeHomeForm= (DynaActionForm) form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
    		TableData empClaimHistoryTable=null;
    		if(request.getSession().getAttribute("EmpClaimHistoryTable")!=null)
    			empClaimHistoryTable=(TableData) request.getSession().getAttribute("EmpClaimHistoryTable");
    		else
    			empClaimHistoryTable=new TableData();
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		{
    			((DynaActionForm)form).initialize(mapping);//reset the form data
    		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		//if the page number or sort id is clicked
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{    
    				empClaimHistoryTable.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return mapping.findForward("EmployeeClaimSearchData");	
    			}///end of if(!strPageID.equals(""))
    			else
    			{
    				empClaimHistoryTable.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
    				empClaimHistoryTable.modifySearchData("sort");//modify the search data
    			}//end of else
    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
    		else
    		{
    			//create the required grid table
    			empClaimHistoryTable.createTableInfo("EmpClaimHistoryTable",null);
    			MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
    			empClaimHistoryTable.setSearchData(this.populateClaimHistorySearchCriteria(employeeHomeForm,memberDetailVO.getMemberSeqID()));
    			empClaimHistoryTable.setSortColumnName("TPA_ENROLLMENT_ID");
    			empClaimHistoryTable.modifySearchData("search");
    		}//end of else 
    		ArrayList claimHistotyLogData=  onlineAccessObject.getMemberClaimHistoryDetails(empClaimHistoryTable.getSearchData(),"Administration");
    		empClaimHistoryTable.setData(claimHistotyLogData, "search");
    		request.getSession().setAttribute("EmpClaimHistoryTable",empClaimHistoryTable);
    		return mapping.findForward(strEmplClaimSearchData);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    public ActionForward doPreAuthForward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside PreAuthAction doForward");
			setOnlineLinks(request);
			TableData empPreAuthTable=null;
    		if(request.getSession().getAttribute("EmpPreAuthTable")!=null)
    			empPreAuthTable=(TableData) request.getSession().getAttribute("EmpPreAuthTable");
    		else
    			empPreAuthTable=new TableData();
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			empPreAuthTable.modifySearchData(strForward);// modify the search data
			ArrayList claimHistotyLogData=  onlineAccessObject.getMemberPreAuthDetails(empPreAuthTable.getSearchData(),"Administration");
			empPreAuthTable.setData(claimHistotyLogData, strForward);
			request.getSession().setAttribute("EmpPreAuthTable",empPreAuthTable);
			return mapping.findForward(strEmplSearchData);
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}

	public ActionForward doPreAuthBackward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside PreAuthAction doBackward");
			setOnlineLinks(request);
			TableData empPreAuthTable=null;
    		if(request.getSession().getAttribute("EmpPreAuthTable")!=null)
    			empPreAuthTable=(TableData) request.getSession().getAttribute("EmpPreAuthTable");
    		else
    			empPreAuthTable=new TableData();
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			empPreAuthTable.modifySearchData(strBackward);// modify the search data
			ArrayList claimHistotyLogData=  onlineAccessObject.getMemberPreAuthDetails(empPreAuthTable.getSearchData(),"Administration");
			empPreAuthTable.setData(claimHistotyLogData, strBackward);// set the table data
			request.getSession().setAttribute("EmpClaimHistoryTable",empPreAuthTable);
			return mapping.findForward(strEmplSearchData);
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	public ActionForward doClaimHistoryForward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside PreAuthAction doForward");
			setOnlineLinks(request);
			TableData empClaimHistoryTable=null;
    		if(request.getSession().getAttribute("EmpClaimHistoryTable")!=null)
    			empClaimHistoryTable=(TableData) request.getSession().getAttribute("EmpClaimHistoryTable");
    		else
    			empClaimHistoryTable=new TableData();
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			empClaimHistoryTable.modifySearchData(strForward);// modify the search data
			ArrayList claimHistotyLogData=  onlineAccessObject.getMemberClaimHistoryDetails(empClaimHistoryTable.getSearchData(),"Administration");
			empClaimHistoryTable.setData(claimHistotyLogData, strForward);
			request.getSession().setAttribute("EmpClaimHistoryTable",empClaimHistoryTable);
			return mapping.findForward(strEmplClaimSearchData);
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}

	public ActionForward doClaimHistoryBackward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside PreAuthAction doBackward");
			setOnlineLinks(request);
			TableData empClaimHistoryTable=null;
    		if(request.getSession().getAttribute("EmpClaimHistoryTable")!=null)
    			empClaimHistoryTable=(TableData) request.getSession().getAttribute("EmpClaimHistoryTable");
    		else
    			empClaimHistoryTable=new TableData();
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			empClaimHistoryTable.modifySearchData(strBackward);// modify the search data
			ArrayList claimHistotyLogData=  onlineAccessObject.getMemberClaimHistoryDetails(empClaimHistoryTable.getSearchData(),"Administration");
			empClaimHistoryTable.setData(claimHistotyLogData, strBackward);// set the table data
			request.getSession().setAttribute("EmpClaimHistoryTable",empClaimHistoryTable);
			return mapping.findForward(strEmplClaimSearchData);
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	
	public ActionForward doShortfallList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			log.debug("Inside PreAuthAction doBackward");
			setOnlineLinks(request);
			DynaActionForm employeeHomeForm= (DynaActionForm) form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		String paramData=null;
    		String type=null;
    		String caption=null;
    		MemberDetailVO memberDetailVO =null;
    		if(request.getSession().getAttribute("memberDetailVO")!=null)
    			memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
    		if(request.getSession().getAttribute("strCaption")!=null)
    			caption=(String) request.getSession().getAttribute("strCaption");
    		if("N".equals(request.getParameter("Entry"))){
    			paramData=request.getParameter("param");
    			type=paramData.substring(paramData.indexOf("|")+1,paramData.lastIndexOf("|"));
        		paramData="|"+paramData;
    		}	
    		else{
    			paramData=(String)request.getAttribute("param");
    			if(paramData.contains("PAT"))
    				type="PAT";
    			else
    				type="CLM";
    		}
    			
    		String subActiveLink=TTKCommon.getActiveSubLink(request);
    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
    		TableData empShortfallTable=null;
    		if(request.getSession().getAttribute("empShortfallDetails")!=null)
    			empShortfallTable=(TableData) request.getSession().getAttribute("empShortfallDetails");
    		else
    			empShortfallTable=new TableData();
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		{
    			((DynaActionForm)form).initialize(mapping);//reset the form data
    		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		//if the page number or sort id is clicked
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{    
    				empShortfallTable.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return mapping.findForward("EmployeeShortfallDetails");	
    			}///end of if(!strPageID.equals(""))
    			else
    			{
    				empShortfallTable.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
    				empShortfallTable.modifySearchData("sort");//modify the search data
    			}//end of else
    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
    		else
    		{
    			//create the required grid table
    			empShortfallTable.createTableInfo("EmpShortfallTable",new ArrayList());
    			memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
    			ArrayList arrayList=new ArrayList();
    			arrayList.add(paramData);
    			empShortfallTable.setSearchData(arrayList);
    			empShortfallTable.setSortColumnName("TPA_ENROLLMENT_ID");
    			empShortfallTable.modifySearchData("search");
    		}//end of else 
    		ArrayList shortfallList= null;
    		if("Pre-Approval".equals(subActiveLink))
    			shortfallList=onlineAccessObject.getMemberShortfallDetails(empShortfallTable.getSearchData(),"PAT");
    		else if("Claims History".equals(subActiveLink))
    			shortfallList=onlineAccessObject.getMemberShortfallDetails(empShortfallTable.getSearchData(),"CLM");
    		empShortfallTable.setData(shortfallList, "search");
    		((Column)((ArrayList)empShortfallTable.getTitle()).get(5)).setLinkTitle("ViewFileVisibility");
    		request.setAttribute("type", type);
    		request.getSession().setAttribute("paramData", paramData);
    		if("Pre-Approval".equals(subActiveLink))
    		employeeHomeForm.set("caption", request.getSession().getAttribute("strCaption")+" - ["+memberDetailVO.getPreAuthNumber()+"]");
    		else if("Claims History".equals(subActiveLink))
    			employeeHomeForm.set("caption", request.getSession().getAttribute("strCaption")+" - ["+memberDetailVO.getClaimNumber()+"]");
    		request.getSession().setAttribute("employeeHomeForm", employeeHomeForm);
    		request.getSession().setAttribute("empShortfallDetails",empShortfallTable);
    		return mapping.findForward("EmployeeShortfallDetails");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	public ActionForward doClaimSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			log.debug("Inside doClaimSubmission() of EmployeeAction");
			setOnlineLinks(request);
			DynaActionForm claimSubmissionForm= (DynaActionForm) form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		ArrayList arrayList=new ArrayList<>();
    		String selectedroot=request.getParameter("selectedroot");
    		String selectednode=request.getParameter("selectednode");
    		TreeData treeData =(TreeData)request.getSession().getAttribute("treeData");	
	   		MemberVO memberVO=new MemberVO();
	   		 if(!TTKCommon.checkNull(selectedroot).equals(""))
	   		 {
	   			 memberVO = (MemberVO)treeData.getSelectedObject(selectedroot,selectednode);
	   		 }
	   		arrayList.add(memberVO.getMemberSeqID());
    		ArrayList claimSubInfoData=onlineAccessObject.getClaimSubmissionInfo(arrayList);
    		MemberDetailVO memberDetailVO= (MemberDetailVO)claimSubInfoData.get(0);
    		claimSubmissionForm = (DynaActionForm)FormUtils.setFormValues("claimSubmissionForm", memberDetailVO, this, mapping, request);
    		/*Getting Bank info starts here*/
    		HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;

			//hmCityList = customerBankDetailsManager.getbankStateInfo();
			hmCityList = onlineAccessObject.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(claimSubmissionForm.get("bankname")));
			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;
			hmDistList = onlineAccessObject.getBankCityInfo(memberDetailVO.getBankState(),memberDetailVO.getBankname());
			String bankState = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankState"));			
			String bankName = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankcity"));
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(claimSubmissionForm.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}
			//hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			hmBranchList = onlineAccessObject.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(claimSubmissionForm.get("bankcity")));

			}// end of if(hmCityList!=null)
							
			 if (alCityList == null) {
					alCityList = new ArrayList();
				}// end of if(alCityList==null)
			 if (alDistList == null) {
				 alDistList = new ArrayList();
				}// end of if(alCityList==null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			claimSubmissionForm.set("caption", memberDetailVO.getEnrollmentID());
			claimSubmissionForm.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			claimSubmissionForm.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			claimSubmissionForm.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			request.getSession().setAttribute("editable", null);
			request.getSession().setAttribute("claimMemberSeqId", memberDetailVO.getMemberSeqID());
    		request.getSession().setAttribute("claimSubmissionForm", claimSubmissionForm);
    		return mapping.findForward("EmployeeClaimSubmission");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	
	
	public ActionForward doSaveAccountInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			log.info("Inside doClaimSubmission() of EmployeeAction");
			setOnlineLinks(request);
			DynaActionForm claimSubmissionForm= (DynaActionForm) form;
			String editable=request.getParameter("editable");
			
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
			MemberDetailVO memberDetailVO = (MemberDetailVO) FormUtils.getFormValues(claimSubmissionForm, this, mapping, request);
			memberDetailVO.setAddedBy(userSecurityProfile.getUSER_SEQ_ID());
			memberDetailVO.setCategoryTypeID(editable);
			if(!userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claim Submission")){
				memberDetailVO.setEndorsementSeqID((Long) request.getSession().getAttribute("enr_address_seqId"));
			}
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			String successYN=onlineAccessObject.getUpdateMemberInfo(memberDetailVO);
			if(successYN!=null)
				request.setAttribute("successMsg", "message.savedSuccessfully");
    		request.getSession().setAttribute("claimSubmissionForm", claimSubmissionForm);
    		request.getSession().setAttribute("editable", null);
			if(!userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claim Submission"))
				return mapping.findForward("GetContactInfo");
			else
    		return mapping.findForward("EmployeeClaimSubmission");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	public ActionForward doNewClose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			log.info("Inside doClaimSubmission() of EmployeeAction");
			setOnlineLinks(request);
			DynaActionForm claimSubmissionForm= (DynaActionForm) form;
    		request.getSession().setAttribute("claimSubmissionForm", claimSubmissionForm);
    		return mapping.findForward("EmployeeClaimSubmission");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	public ActionForward doDefaultHelpDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
    try{
    		log.debug("Inside the doDefaultHelpDocs method of PartnerAction");
    		setOnlineLinks(request);
    		
//    		DynaActionForm claimSubmissionForm =(DynaActionForm)form;
//    		claimSubmissionForm.initialize(mapping);     
    		request.getSession().setAttribute("editable", null);
    		return mapping.findForward("notifications");
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    	{
    			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
    	}//end of catch(Exception exp)
    }//end of doDefaultNotify(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    //HttpServletResponse response)
	
	public ActionForward doViewShortfallDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside PreAuthAction doBackward");
			setOnlineLinks(request);
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			DynaActionForm frmShortFall = (DynaActionForm)form;
			DynaActionForm employeeHomeForm = (DynaActionForm)form;
			String activeSubLink=TTKCommon.getActiveSubLink(request);
	   		TableData tableData=(TableData) request.getSession().getAttribute("empShortfallDetails");
	   		EmployeeShortfall employeeShortfall=(EmployeeShortfall)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
	   		MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
	   		PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	   	  ArrayList<Object> alShortfallList = new ArrayList<Object>();
          alShortfallList.add(employeeShortfall.getShortfallSeqId());
          if("Pre-Approval".equals(activeSubLink)){
        	  alShortfallList.add(employeeShortfall.getPreAuthSeqId());
          }
          else if("Claims History".equals(activeSubLink)){
          alShortfallList.add(employeeShortfall.getClaimSeqId());
          }
          alShortfallList.add(null);
          alShortfallList.add(TTKCommon.getUserSeqId(request));
          ShortfallVO   shortfallVO=preAuthObject.getShortfallDetail(alShortfallList);
          
          //Get Member and Provider Data for Shortfall
          OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
          String[] shortFallData	=	new String[17];
          if("Pre-Approval".equals(activeSubLink))
        	  shortFallData=onlineProviderManager.getMemProviderForShortfall(employeeShortfall.getShortfallSeqId(),"PAT");
          else
        	  shortFallData=onlineProviderManager.getMemProviderForShortfall(employeeShortfall.getShortfallSeqId(),"CLM");
          frmShortFall = (DynaActionForm)FormUtils.setFormValues("frmShortFall", shortfallVO, this, mapping, request);
          employeeHomeForm.set("caption", employeeHomeForm.get("caption")+" - ["+employeeShortfall.getShortfallNo()+"]");
          request.getSession().setAttribute("preauthShortfallQueries",shortfallVO.getShortfallQuestions());
          request.getSession().setAttribute("frmShortFall",frmShortFall);
          request.getSession().setAttribute("shortFallData", shortFallData);
          request.getSession().setAttribute("shortfallSeqId",employeeShortfall.getShortfallSeqId());
          if("Claims History".equals(activeSubLink) && memberDetailVO.getClaim_type().equals("Member"))
          request.getSession().setAttribute("shortfallStatus", employeeShortfall.getStatus());
          else
        	  request.getSession().setAttribute("shortfallStatus", null);
          request.getSession().setAttribute("employeeHomeForm", employeeHomeForm);
			return mapping.findForward("viewShortfallDetails");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	public ActionForward doViewFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		setOnlineLinks(request);
		 ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
    	String strFileNoRecords = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/noRecordsFound.pdf"; 

	  try{
		  	PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			String shortfallSeqID = request.getParameter("rownum");	
			TableData tableData=(TableData)request.getSession().getAttribute("empShortfallDetails");
			EmployeeShortfall employeeShortfall=(EmployeeShortfall) tableData.getRowInfo(Integer.parseInt(shortfallSeqID));
			InputStream iStream	=	preAuthObject.getShortfallDBFile(String.valueOf(employeeShortfall.getShortfallSeqId()));
			 if((iStream!=null) && (!iStream.equals("")))
			  {
        bis = new BufferedInputStream(iStream);
        baos=new ByteArrayOutputStream();
             response.setContentType("application/pdf");
        int ch;
              while ((ch = bis.read()) != -1) baos.write(ch);
              sos = response.getOutputStream();
              baos.writeTo(sos);  
              baos.flush();      
              sos.flush(); 
			  } else{
    				File f = new File(strFileNoRecords);
    	    		if(f.isFile() && f.exists()){
    	    			fis = new FileInputStream(f);
    	    		}//end of if(strFile !="")
    	    		BufferedInputStream bist = new BufferedInputStream(fis);
    	    		baos=new ByteArrayOutputStream();
    	    		int ch;
    	    		while ((ch = bist.read()) != -1)
    	    		{
    	    			baos.write(ch);
    	    		}//end of while ((ch = bis.read()) != -1)
    	    		sos = response.getOutputStream();
    	    		baos.writeTo(sos);
  			  }
	            }catch(Exception exp)
	            	{
	            		return this.processExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
	            	}//end of catch(Exception exp)
	        
	          finally{
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
	
	public ActionForward doBackToShortfallList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside doDefaultNetwork of EmployeeAction");
			setOnlineLinks(request);
			String activeSubLink=TTKCommon.getActiveSubLink(request);
			String param=(String) request.getSession().getAttribute("paramData");
			request.setAttribute("param", param);
			if("Pre-Approval".equals(activeSubLink)){
				request.setAttribute("type", "PAT");
			}else{
				request.setAttribute("type", "CLM");
			}
			return mapping.findForward("EmployeeShortfallClose");
//			return mapping.findForward("EmployeeShortfallDetails");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	
	 public ActionForward doShowPreAuthShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) 
			 throws Exception{
		 setOnlineLinks(request);
		 try{
			 log.debug("Inside the doShowPreAuthShortfall method of ReportsAction");
			 	ByteArrayOutputStream baos=null;
	    		OutputStream sos = null;
	    		FileInputStream fis = null;
			 	response.setContentType("application/octet-stream");
		 		String fileName = request.getParameter("fileName");
		 		String reportType		=	request.getParameter("reportType");
		 		String strFile = "";
		 		String[] strFileName=null;
		 		String strOSName= System.getProperty("os.name");
		 		
		 		int indexDirNo	=	0;
	 		if("recentReports".equals(reportType))
	 		{
	 			strFile	=	TTKPropertiesReader.getPropertyValue("downloadHistoryDir")+fileName;
	 			indexDirNo	=	Integer.parseInt(TTKPropertiesReader.getPropertyValue("downloadHistoryDirNo"));
	 		}else{
	 			strFile	=	TTKPropertiesReader.getPropertyValue("shortfallrptdir")+fileName+".pdf";
	 			indexDirNo	=	Integer.parseInt(TTKPropertiesReader.getPropertyValue("shortfallrptdirNo"));
	 		}//else - For ShortFalls
		//  
		//This part is common for all Downloads
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
 			response.addHeader("Content-Disposition","attachment; filename=shortfallLetter.pdf");
 		}//end of else if(strFile.endsWith("pdf"))
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
		bis.close();
		baos.close();
		fis.close();
// 		 return (mapping.findForward(strReportdisplay));
		return null;//returning null - need to show the downloaded file
 		}//end of try
		 catch(Exception exp)
		 {
			  
			 return this.processExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
		 }//end of catch(Exception exp)
	 }//end of doShowPreAuthShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 public ActionForward doSaveShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   		HttpServletResponse response) throws Exception{
		   	try{
		   		setOnlineLinks(request);
		   		log.debug("Inside the doSaveShortfall method of ProviderAction");
		   		DynaActionForm frmShortFall = (DynaActionForm)form;
		   		Long shortfallSeqId=(Long)request.getSession().getAttribute("shortfallSeqId");
				OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
				// UPLOAD FILE STARTS
				FormFile formFile1 = null;
				FormFile formFile2 = null;
				FormFile formFile3 = null;
//				formFile1 = (FormFile)frmShortFall.get("file");
//				if(frmShortFall.get("file01")!=null)
//				formFile2 = (FormFile)frmShortFall.get("file01");
//				if(frmShortFall.get("file02")!=null)
//				formFile3 = (FormFile)frmShortFall.get("file02");
				ArrayList shortfallInfoList=new ArrayList();
				
				/*PDFMergerUtility mergerUtility = new PDFMergerUtility();
				ByteArrayOutputStream mergeOutputStream=new ByteArrayOutputStream();
				ArrayList shortfallInfoList=new ArrayList();
				if(formFile1!=null)
				mergerUtility.addSource(formFile1.getInputStream());
				if(formFile2!=null)
					mergerUtility.addSource(formFile2.getInputStream());
				if(formFile3!=null)
					mergerUtility.addSource(formFile3.getInputStream());
//				mergerUtility.setDestinationFileName("D:\\mytestfiles\\myTest.pdf");
				mergerUtility.setDestinationStream(mergeOutputStream);
				mergerUtility.mergeDocuments();
				byte[] bytes = mergeOutputStream.toByteArray();*/
//				ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
				ArrayList<FormFile> formFileList=new ArrayList<FormFile>();
				String fileExtn	=	"";
				for(int inc=0;inc<3;inc++){
					
					if(inc==0)
					formFile1 = (FormFile)frmShortFall.get("file");
					else
						formFile1 = (FormFile)frmShortFall.get("file0"+inc);
					if(formFile1!=null&&formFile1.getFileSize()!=0)
					formFileList.add(formFile1);
				}
				if(formFileList.size()!=0){
					for (FormFile formFile : formFileList) {
						if(formFile.getFileSize()!=0)
						{
							if(formFile.getFileSize()/(1024*1024)<3){
								fileExtn = GetFileExtension(formFile.toString());
								fileExtn=fileExtn.toLowerCase();
									if(!"pdf".equals(fileExtn)&&!"png".equals(fileExtn)&&!"jpeg".equals(fileExtn)&&!"jpg".equals(fileExtn)){
										TTKException expTTK = new TTKException();
										expTTK.setMessage("error.file.employee.restricted.format");
										throw expTTK;
									}
							}else{
								TTKException expTTK = new TTKException();
								expTTK.setMessage("error.file.size.issue");
								throw expTTK;
							}
							
						}
						else
						{
							TTKException expTTK = new TTKException();
							expTTK.setMessage("error.documents.required");
							throw expTTK;
						}
					}
				}else
				{
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.documents.required");
					throw expTTK;
				}
				PDFMergerUtility mergerUtility = new PDFMergerUtility();
				ArrayList<InputStream> convertedFormFile=new ArrayList();
				for (int i = 0; i < formFileList.size(); i++) {
					FormFile inputFormFile=formFileList.get(i);
					InputStream convertedStream=null;
					if(inputFormFile.getFileName().contains(".png")||inputFormFile.getFileName().contains(".PNG")
							||inputFormFile.getFileName().contains(".JPEG")||inputFormFile.getFileName().contains(".jpeg")
							||inputFormFile.getFileName().contains(".JPG")||inputFormFile.getFileName().contains(".jpg"))
						convertedStream=imagetoPDF(inputFormFile.getFileData());
					else
						convertedStream=inputFormFile.getInputStream();
					if(convertedStream!=null)
					convertedFormFile.add(convertedStream);
				}
				ByteArrayOutputStream mergeOutputStream=new ByteArrayOutputStream();
				for (int i = 0; i < convertedFormFile.size(); i++) {
					mergerUtility.addSource(convertedFormFile.get(i));
				}
//				mergerUtility.setDestinationFileName("D:\\mytestfiles\\myTest.pdf");
				mergerUtility.setDestinationStream(mergeOutputStream);
				try{
					mergerUtility.mergeDocuments();
					}catch(IOException io){
						TTKException expTTK = new TTKException();
						expTTK.setMessage("error.documents.corrupted");
						throw expTTK;
					}
				byte[] bytes = mergeOutputStream.toByteArray();
				
				
				shortfallInfoList.add(shortfallSeqId);
				shortfallInfoList.add(bytes);
//				String successYN	=	onlineAccessObject.saveShorfallDocs(shortfallSeqId,formFile);
				String successYN	=	onlineAccessObject.saveMultiShorfallDocs(shortfallInfoList);
				if(successYN.equals("Y"))
					request.setAttribute("updated","message.addedSuccessfully");
				request.getSession().setAttribute("shortfallStatus","Responded");
		   		return mapping.findForward("viewShortfallDetails");
		   	}//end of try
		   	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
				}//end of catch(Exception exp)
		   }//end of doSaveShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	 
	 public ActionForward doCloseEmplClaim(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			try {
				log.debug("Inside doDefaultNetwork of EmployeeAction");
				setOnlineLinks(request);
				DynaActionForm claimSubmissionForm=(DynaActionForm) form;
				
//				DynaActionForm claimSubmissionForm = (DynaActionForm) request.getSession().getAttribute("claimSubmissionForm");
				request.getSession().setAttribute("claimSubmissionForm", claimSubmissionForm);
				return mapping.findForward("EmployeeClaimSubmission");
			}// end of try
			catch(TTKException expTTK)
	    	{
	    		return this.processOnlineExceptions(request, mapping, expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
	    	}
		}
	  
	public ActionForward doDefaultNetwork(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside doDefaultNetwork of EmployeeAction");
			setOnlineLinks(request);
            ((DynaActionForm)form).initialize(mapping);//reset the form data
            DynaActionForm frmSearchHospital = (DynaActionForm)form;
            UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
            OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
            ArrayList providerTypeList = onlineAccessObject.getnetworkProviderList();
            ArrayList specialityList=onlineAccessObject.getSpecialityList();
            ArrayList networkTypeList=onlineAccessObject.getNetworkTypeList(userSecurityProfile.getPolicySeqID());
    		String strDefaultBranchID  = String.valueOf(((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getBranchID());
    		frmSearchHospital.set("officeInfo",strDefaultBranchID);
			TableData networkProviderTable=new TableData();
			networkProviderTable.createTableInfo("NetworkProviderTable",new ArrayList());
			frmSearchHospital.set("providerTypeList", providerTypeList);
			frmSearchHospital.set("specialityList", specialityList);
			frmSearchHospital.set("networkTypeList", networkTypeList);
			frmSearchHospital.set("stateCodeList", new ArrayList());
			frmSearchHospital.set("cityCodeList", new ArrayList<>());
    		request.getSession().setAttribute("networkProviderTable", networkProviderTable);
    		request.getSession().setAttribute("providerTypeList", providerTypeList);
    		request.getSession().setAttribute("specialityList", specialityList);
    		request.getSession().setAttribute("stateCodeList", new ArrayList());
    		request.getSession().setAttribute("cityCodeList", new ArrayList());
    		request.getSession().setAttribute("networkTypeList", networkTypeList);
    		request.getSession().setAttribute("frmSearchHospital", frmSearchHospital);
			return mapping.findForward("EmployeeNetworkHome");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	 
	public ActionForward doDefaultContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside doDefaultNetwork of EmployeeAction");
			setOnlineLinks(request);
            ((DynaActionForm)form).initialize(mapping);//reset the form data
			return mapping.findForward("EmployeeContactHome");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	public ActionForward doEmployeeQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside doDefaultNetwork of EmployeeAction");
			setOnlineLinks(request);
			DynaActionForm employeeContactForm=(DynaActionForm)form;
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
            OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
            ArrayList reportData=null;
			reportData=onlineAccessObject.getAndUpdateReportData("HAQ",userSecurityProfile.getPolicyGrpSeqID(),null,null);
			MemberDetailVO memberDetailVO=(MemberDetailVO)reportData.get(1);
			employeeContactForm = (DynaActionForm)FormUtils.setFormValues("employeeContactForm",memberDetailVO,this,mapping,request);		
			request.getSession().setAttribute("employeeContactForm", employeeContactForm);
			return mapping.findForward("EmployeeQuerySubmission");
		}// end of try
		catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}
	}
	
	 public ActionForward doEditable(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			try {
				setOnlineLinks(request);
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
				request.getSession().setAttribute("editable", request.getParameter("editable"));
				if(!userSecurityProfile.getSecurityProfile().getActiveLink().equals("Employee Claim Submission"))
					return mapping.findForward("forwardToReport");
				else
				return mapping.findForward("EmployeeClaimSubmission");
			}// end of try
			catch(TTKException expTTK)
	    	{
	    		return this.processOnlineExceptions(request, mapping, expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
	    	}
		}
	
	private OnlineProviderManager getOnlineAccessManagerObject() throws TTKException
    {
    	OnlineProviderManager onlineProviderManager = null;
        try
        {
            if(onlineProviderManager == null)
            {
                InitialContext ctx = new InitialContext();
                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
                onlineProviderManager = (OnlineProviderManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineProviderManagerBean!com.ttk.business.onlineforms.providerLogin.OnlineProviderManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "onlineproviderinfo");
        }//end of catch
        return onlineProviderManager;
    }//end of getOnlineAccessManagerObject()
	
	private PreAuthManager getPreAuthManagerObject() throws TTKException
	{
		PreAuthManager preAuthManager = null;
		try
		{
			if(preAuthManager == null)
			{
				InitialContext ctx = new InitialContext();
				preAuthManager = (PreAuthManager) ctx.lookup("java:global/TTKServices/business.ejb3/PreAuthManagerBean!com.ttk.business.preauth.PreAuthManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "onlineproviderinfo");
		}//end of catch
		return preAuthManager;
	}//end getPreAuthManagerObject()
    private ArrayList<Object> populateClaimHistorySearchCriteria(DynaActionForm employeeHomeForm, Long memberSeqID) {
    	ArrayList arrayList=new ArrayList<>();
		arrayList.add(memberSeqID);
		arrayList.add(employeeHomeForm.get("treatementStartDate"));
		arrayList.add(employeeHomeForm.get("treatementEndDate"));
		arrayList.add(employeeHomeForm.get("providerName"));
		arrayList.add(employeeHomeForm.get("claimNo"));
		arrayList.add(employeeHomeForm.get("invoiceNo"));
		arrayList.add(employeeHomeForm.get("sClaimType"));
		arrayList.add(employeeHomeForm.get("sStatus"));
		arrayList.add(employeeHomeForm.get("claimBatchNumber"));
		return arrayList;
	}

	private ArrayList populateSearchCriteria(DynaActionForm employeeHomeForm,long memberSeqID) {
		ArrayList arrayList=new ArrayList<>();
		arrayList.add(memberSeqID);
		arrayList.add(employeeHomeForm.get("treatementStartDate"));
		arrayList.add(employeeHomeForm.get("treatementEndDate"));
		arrayList.add(employeeHomeForm.get("preAuthNo"));
		arrayList.add(employeeHomeForm.get("providerName"));
		arrayList.add(employeeHomeForm.get("sStatus"));
		return arrayList;
	}

	private OnlineAccessManager getOnlineAccessManager() throws TTKException
	{
		OnlineAccessManager onlineAccessManager = null;
		try
		{
			if(onlineAccessManager == null)
			{
				InitialContext ctx = new InitialContext();
				onlineAccessManager = (OnlineAccessManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineAccessManagerBean!com.ttk.business.onlineforms.OnlineAccessManager");
			}//end ofif(onlineAccessManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strEmplHome);
		}//end of catch
		return onlineAccessManager;
	}
	public ActionForward doChangeBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		setOnlineLinks(request);
		try {
			//	setLinks(request);
			//checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AccountsAction");
			String focusID	=	request.getParameter("focusID");
			AccountDetailVO accountDetailVO = null;
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;
			DynaActionForm claimSubmissionForm = (DynaActionForm) form;
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
						
			hmCityList = onlineAccessObject.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(claimSubmissionForm.get("bankname")));
				
			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			claimSubmissionForm.set("frmChanged", "changed");
			claimSubmissionForm.set("alCityList", alCityList);
			
			//get the bank code based on bank code
			String bankCode	=		onlineAccessObject.getBankCode((String)TTKCommon.checkNull(claimSubmissionForm.get("bankName")));
			
			request.getSession().setAttribute("alCityList", alCityList);
			
			ArrayList alDistList = new ArrayList();
			request.getSession().setAttribute("alDistList", alDistList);
			
			ArrayList alBranchList = new ArrayList();
			request.getSession().setAttribute("alBranchList", alBranchList);
			
			claimSubmissionForm.set("neft", bankCode);
			request.setAttribute("focusID", focusID);
			request.getSession().setAttribute("editable", "AccountInfo");
//			return mapping.findForward("EmployeeClaimSubmission");
			return mapping.findForward("getSwiftcodeFromBranchs");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strEmplHome));
		}// end of catch(Exception exp)
	}// end of doChangeState(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward doChangeState(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setOnlineLinks(request);
		//	checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AccountsAction");
			String focusID	=	request.getParameter("focusID");
			AccountDetailVO accountDetailVO = null;
			HashMap hmDistList = null;
			ArrayList alDistList = null;
			ArrayList alCityCode = null;
			DynaActionForm claimSubmissionForm = (DynaActionForm) form;
			String bankState = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankState"));
			String bankName = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankname"));
			
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();			
			hmDistList = onlineAccessObject.getBankCityInfo(bankState,bankName);
			if (hmDistList != null) {
				
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(claimSubmissionForm.get("bankState")));
				
			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			claimSubmissionForm.set("frmChanged", "changed");
			claimSubmissionForm.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
			request.setAttribute("focusID", focusID);
			request.getSession().setAttribute("editable", "AccountInfo");
			return mapping.findForward("EmployeeClaimSubmission");
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strEmplHome));
		}// end of catch(Exception exp)
	}// end of doChangeState(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doChangeCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			setOnlineLinks(request);
			//checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AddEnrollmentAction");
			String focusID	=	request.getParameter("focusID");
			AccountDetailVO accountDetailVO = null;
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			// ArrayList alCityCode = null;
			DynaActionForm claimSubmissionForm = (DynaActionForm) form;
			String bankState = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankState"));
			String bankName = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(claimSubmissionForm.get("bankcity"));
			
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			hmBranchList = onlineAccessObject.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(claimSubmissionForm.get("bankcity")));
				
			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			claimSubmissionForm.set("frmChanged", "changed");
			claimSubmissionForm.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
			request.setAttribute("focusID", focusID);
			request.getSession().setAttribute("editable", "AccountInfo");
			return mapping.findForward("EmployeeClaimSubmission");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strEmplHome));
		}// end of catch(Exception exp)
	}// end of doChangeState(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward dogetRelatioship(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	PrintWriter	writer= response.getWriter();
    		try{
    		setOnlineLinks(request);
    		String rownum = request.getParameter("rownum");	
			TableData employeeDataTable=(TableData)request.getSession().getAttribute("employeeDataTable");
			MemberVO memberVO=(MemberVO) employeeDataTable.getRowInfo(Integer.parseInt(rownum));
    		String relationship=memberVO.getRelationship();
    		writer.print(relationship);
    		}catch(TTKException expTTK)
    		{
    			writer.print("Something went wrong Please Contact Administrator");
    			return this.processOnlineExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			writer.print("Something went wrong Please Contact Administrator");
    			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
    		}finally{
    			writer.flush();
    		}
    		return null;    		
    	}//end of try
	public ActionForward doChangeBranch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			setOnlineLinks(request);
		//	checkWebboardVisabulity(request);
			log.debug("Inside doChangeBranch method of AccountsAction");
		
			DynaActionForm accountForm = (DynaActionForm) form;
			AccountDetailVO accountDetailVO = null;
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
			// ArrayList alCityCode = null;
			DynaActionForm frmAddEnrollment = (DynaActionForm) form;
			MemberDetailVO memberDetailVO = null;
			memberDetailVO=(MemberDetailVO)FormUtils.getFormValues(frmAddEnrollment,this,mapping,request);
			
			String bankState = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankState"));
			String bankName = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankcity"));
			String bankBranch = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankBranch"));
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			
			memberDetailVO	= onlineAccessObject.getBankIfscInfoOnBankName(bankName);
			
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmAddEnrollment.get("bankBranch")));
				
			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			String micr = TTKCommon.checkNull(memberDetailVO.getMicr());
			String ifsc = TTKCommon.checkNull(memberDetailVO.getIfsc());
			
			//request.getSession().setAttribute("micr", micr);
			request.getSession().setAttribute("ifsc", ifsc);
			frmAddEnrollment.set("frmChanged", "changed");
			//frmCustomerBankAcctGeneral.set("micr", micr);
			frmAddEnrollment.set("ifsc", ifsc);
//			return mapping.findForward("getSwiftcodeFromBranchs");
				return mapping.findForward("EmployeeClaimSubmission");
		
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strOnlinePolicies));
		}// end of catch(Exception exp)
	}// end of doChangeState(ActionMapping mapping,ActionForm
	public ActionForward doViewPolicy(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);
    		log.debug("Inside OnlinePoliciesAction doViewPolicy");
    		DynaActionForm frmMemberDetails=(DynaActionForm)form;
    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
    		MemberVO memberVO =null;
    		Document historyDoc=null;
    		String strClaimantName = "";
    		DependentDetailVO dependentDetailVO =null;
    		String rownum = request.getParameter("rownum");	
    		TableData employeeDataTable=(TableData)request.getSession().getAttribute("employeeDataTable");
    		memberVO=(MemberVO) employeeDataTable.getRowInfo(Integer.parseInt(rownum));
    		String strName1 =  memberVO.getMemName();
			strClaimantName =  strName1;
			OnlineAccessManager onlineAccessManager=this.getOnlineAccessManager();
    		ArrayList<Object> alParams = new ArrayList<Object>();
    		String strCaption="";
    		strCaption="[ "+strClaimantName+" ]"+"[ "+TTKCommon.checkNull(memberVO.getEnrollmentID())+" ]";
			alParams.add(null);
			alParams.add(null);
			alParams.add(memberVO.getMemberSeqID());
			historyDoc=onlineAccessManager.getOnlinePolicyDetail(alParams);
			request.getSession().setAttribute("historyDoc", historyDoc);
    		userSecurityProfile.setMemSeqID(memberVO.getMemberSeqID());
    		request.getSession().setAttribute("UserSecurityProfile",userSecurityProfile);
    		request.setAttribute("logintype",userSecurityProfile.getLoginType());
    		dependentDetailVO=onlineAccessManager.selectDependent(memberVO.getMemberSeqID(),userSecurityProfile.getLoginType());
    		dependentDetailVO.setRelationTypeID(dependentDetailVO.getRelationTypeID()+"#"+dependentDetailVO.getGenderYN());
			request.getSession().setAttribute("photoYN", dependentDetailVO.getPhotoYN());
    		frmMemberDetails = (DynaActionForm)FormUtils.setFormValues("frmMemberDetails",dependentDetailVO,this,mapping,request);
    		ArrayList alRelationShip = onlineAccessManager.getRelationshipCode(dependentDetailVO.getPolicySeqID(),"A1");
//    		ArrayList<Object> alFieldInfo=onlineAccessManager.getFieldInfo(dependentDetailVO.getPolicySeqID(),dependentDetailVO.getPolicyGroupSeqID());
    		/*MemberPermVO[] alFieldInfoM =alFieldInfo.toArray(new MemberPermVO[0]);
    		if(alFieldInfoM.length>0)
    		{
	    		for(int i= 0; i<alFieldInfoM.length; i++)
	    		{
	    			MemberPermVO memberPermVO = new MemberPermVO();
	    			memberPermVO=(MemberPermVO)alFieldInfoM[i];
	    			setFieldDisplay(memberPermVO,frmMemberDetails);
	    		}//end of for(int i= 0; i<alFieldInfo.size(); i++)
	    		frmMemberDetails.set("display","display");
    		}//end of if(alFieldInfo.length>0)
    		
    		if(frmMemberDetails.get("ageStatus").equals("HIDE"))
    		{
    			frmMemberDetails.set("age",null);
    		}//end of if(frmMemberDetails.get("ageStatus").equals("HIDE"))
    		if(frmMemberDetails.get("dobStatus").equals("HIDE"))
    		{
    			frmMemberDetails.set("dateOfBirth",null);
    		}//end of if(frmMemberDetails.get("dobStatus").equals("HIDE"))
    		//build the caption based on the type of the Logged in User
    		frmMemberDetails.set("alFieldInfo",(MemberPermVO[])alFieldInfoM);*/
    		frmMemberDetails.set("caption",strCaption);
    		frmMemberDetails.set("alRelationShip", alRelationShip);
    		request.getSession().setAttribute("alRelationShip", alRelationShip);
    		request.getSession().setAttribute("frmMemberDetails", frmMemberDetails);
    		return mapping.findForward("onlinepolicyempldetails");
    		}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}//end of catch(Exception exp)
    }//end of doViewPolicy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
	
	public ActionForward doSaveDependentInfo(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
			try{
			setOnlineLinks(request);
			log.info("do EmployeeAction doSaveDependentInfo");
			OnlineAccessManager onlineAccessManager=this.getOnlineAccessManager();
			long iUpdate=0;
			FormFile formFile = null;
			Document historyDoc=null;
			int fileSize=3*1024*1024;
			String strNotify="";
			DynaActionForm frmMemberDetails=(DynaActionForm)form;
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			String strLoginType=userSecurityProfile.getLoginType();
			StringBuffer sbfCaption=new StringBuffer();
			DependentDetailVO dependentDetailVO1 = new DependentDetailVO();
			dependentDetailVO1=(DependentDetailVO)FormUtils.getFormValues(frmMemberDetails,this, mapping, request);
			String strFamilyIconYN=(String)frmMemberDetails.get("familySumIconYN");
			BigDecimal bgDefaultSumInsured=TTKCommon.getBigDecimal((String)frmMemberDetails.get("defaultSumInsured"));
			String strInsuredName=(String)frmMemberDetails.get("insuredName");
			TableData  tableData =TTKCommon.getTableData(request);
			DependentDetailVO dependentDetailVO = new DependentDetailVO();
			dependentDetailVO=(DependentDetailVO)FormUtils.getFormValues(frmMemberDetails,this, mapping, request);
			String strRelationTypeID = dependentDetailVO.getRelationTypeID();
			strRelationTypeID=strRelationTypeID.substring(0,strRelationTypeID.indexOf("#"));
			dependentDetailVO.setRelationTypeID(strRelationTypeID);			
			sbfCaption=sbfCaption.append("[").append(userSecurityProfile.getPolicyNo()).append("] [").
			append(dependentDetailVO1.getEnrollmentNbr()).append("] [").append(strInsuredName).append("]");
			dependentDetailVO.setUpdatedBy(new Long(1));
			if(dependentDetailVO.getProdPlanSeqID()==null && dependentDetailVO.getAddPremium().intValue()==0 && dependentDetailVO.getTotalSumInsured().intValue()==0){
			dependentDetailVO.setTotalSumInsured(null);
			}
			if(frmMemberDetails.get("premiumDeductOption")!=null)
			dependentDetailVO.setPremiumDeductionOption((String)frmMemberDetails.get("premiumDeductOption"));
			String Declaration = request.getParameter("confirm");
			dependentDetailVO.setDeclaration(Declaration);
			String strgroupid=(String)request.getSession().getAttribute("groupId");//Koc Netsol
			formFile = (FormFile)frmMemberDetails.get("file");
			String strFileExt=formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1,formFile.getFileName().length());
			if(!(strFileExt.equalsIgnoreCase(""))){
			if(((strFileExt.equalsIgnoreCase("jpg"))   || (strFileExt.equalsIgnoreCase("png"))) && (formFile.getFileSize()<=fileSize))
			{
			dependentDetailVO.setJpgInputStream(formFile.getInputStream());	
			}
			else{
			throw new TTKException("error.hrLogin.file");
			}
			}
			ArrayList alRelationShip = onlineAccessManager.getRelationshipCode(dependentDetailVO.getPolicySeqID(),dependentDetailVO.getAbbrCode());
			iUpdate=onlineAccessManager.emplSaveDependentInfo(dependentDetailVO,formFile);
			if((iUpdate<=0)){
			// start Changes as per KOC 1159 and 1160
			frmMemberDetails.set("combinationMembersLimit",dependentDetailVO.getCombinationMembersLimit());
			if(iUpdate<0){
			RuleManager ruleManager=this.getRuleManagerObject();
			ArrayList alValidationError=ruleManager.getValidationErrorList(new Long(iUpdate));
			request.setAttribute("BUSINESS_ERRORS",alValidationError);
//			return this.getForward("onlinepolicyempldetails", mapping, request);
			}}//end of Addition
			if(iUpdate>0)
			{
			frmMemberDetails.initialize(mapping);
			frmMemberDetails.set("memberTypeID",dependentDetailVO1.getMemberTypeID());
			if(dependentDetailVO.getMemberSeqID()!=null)
			{
			//set the appropriate message
			request.setAttribute("updated","message.savedSuccessfully");
			}//end of if(chequeVO.getSeqID()!=null)
			else
			{
			request.setAttribute("updated","message.addedSuccessfully");
			}//end else		
			}//end of if(iUpdate>0)
			dependentDetailVO=onlineAccessManager.selectDependent(dependentDetailVO.getMemberSeqID(),userSecurityProfile.getLoginType());
    		dependentDetailVO.setRelationTypeID(dependentDetailVO.getRelationTypeID()+"#"+dependentDetailVO.getGenderYN());
			request.getSession().setAttribute("photoYN", dependentDetailVO.getPhotoYN());
    		frmMemberDetails = (DynaActionForm)FormUtils.setFormValues("frmMemberDetails",dependentDetailVO,this,mapping,request);
			frmMemberDetails.set("employeeNbr", dependentDetailVO1.getEmployeeNbr());
			frmMemberDetails.set("policySeqID",dependentDetailVO1.getPolicySeqID().toString());
			frmMemberDetails.set("policyGroupSeqID",dependentDetailVO1.getPolicyGroupSeqID().toString());
			frmMemberDetails.set("insuredName",strInsuredName);
			frmMemberDetails.set("caption",sbfCaption.toString());
			frmMemberDetails.set("alRelationShip", alRelationShip);
			frmMemberDetails.set("enrollmentNbr",dependentDetailVO1.getEnrollmentNbr());		
			request.setAttribute("notify",strNotify);	
			request.getSession().setAttribute("alRelationShip", alRelationShip);
			request.getSession().setAttribute("frmMemberDetails",frmMemberDetails);
			//end of try
		/*	ArrayList<Object> alParams = new ArrayList<Object>();
			alParams.add(null);
			alParams.add(null);
			alParams.add(dependentDetailVO1.getMemberSeqID());
			historyDoc=onlineAccessManager.getOnlinePolicyDetail(alParams);
    		request.setAttribute("historyDoc",historyDoc);*/
			return (mapping.findForward("onlinepolicyempldetails"));
			}
			catch(TTKException expTTK)
			{
			return this.processOnlineExceptions(request,mapping,expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processOnlineExceptions(request,mapping,new TTKException(exp,strOnlinePolicies));
			}//end of catch(Exception exp)
			}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	
	 public ActionForward doProviderSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws TTKException{
	    	try{
	    		log.info("This is doProviderSearch method of EmployeeAction");
	    		setOnlineLinks(request);
	    		DynaActionForm frmSearchHospital= (DynaActionForm) form;
	    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
	    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
	    		TableData networkProviderTable=null;
	    		if(request.getSession().getAttribute("networkProviderTable")!=null)
	    			networkProviderTable=(TableData) request.getSession().getAttribute("networkProviderTable");
	    		else
	    			networkProviderTable=new TableData();
	    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
	    		{
	    			((DynaActionForm)form).initialize(mapping);//reset the form data
	    		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
	    		
	    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	    		//if the page number or sort id is clicked
	    		if(!strPageID.equals("") || !strSortID.equals(""))
	    		{
	    			if(!strPageID.equals(""))
	    			{    
	    				networkProviderTable.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
	    				return mapping.findForward("EmployeeNetworkHome");	
	    			}///end of if(!strPageID.equals(""))
	    			else
	    			{
	    				networkProviderTable.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
	    				networkProviderTable.modifySearchData("sort");//modify the search data
	    			}//end of else
	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
	    		else
	    		{
	    			//create the required grid table
	    			networkProviderTable.createTableInfo("NetworkProviderTable",null);
	    			MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
	    			networkProviderTable.setSearchData(this.populateProviderSearchCriteria(frmSearchHospital,userSecurityProfile.getPolicySeqID()));
	    			networkProviderTable.setSortColumnName("HOSP_NAME");
	    			networkProviderTable.modifySearchData("search");
	    		}//end of else 
	    		ArrayList custBankLogDetails=  onlineAccessObject.getProviderNetworkList(networkProviderTable.getSearchData(),"Administration");
	    		networkProviderTable.setData(custBankLogDetails, "search");
	    		((Column)((ArrayList)networkProviderTable.getTitle()).get(5)).setLinkParamMethodName("DESIGNPROVIDEREMAIL");
	    		request.getSession().setAttribute("networkProviderTable",networkProviderTable);
	    		return mapping.findForward("EmployeeNetworkHome");
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processOnlineExceptions(request, mapping, expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
	    	}//end of catch(Exception exp)
	    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 public ActionForward doProviderForward(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			try {
				log.debug("Inside PreAuthAction doForward");
				setOnlineLinks(request);
				TableData networkProviderTable=null;
	    		if(request.getSession().getAttribute("networkProviderTable")!=null)
	    			networkProviderTable=(TableData) request.getSession().getAttribute("networkProviderTable");
	    		else
	    			networkProviderTable=new TableData();
				OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
				networkProviderTable.modifySearchData(strForward);// modify the search data
				ArrayList claimHistotyLogData=  onlineAccessObject.getProviderNetworkList(networkProviderTable.getSearchData(),"Administration");
				networkProviderTable.setData(claimHistotyLogData, strForward);
				request.getSession().setAttribute("networkProviderTable",networkProviderTable);
				return mapping.findForward("EmployeeNetworkHome");
			}// end of try
			catch(TTKException expTTK)
	    	{
	    		return this.processOnlineExceptions(request, mapping, expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
	    	}
		}

		public ActionForward doProviderBackward(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			try {
				log.debug("Inside EmployeeAction doProviderBackward");
				setOnlineLinks(request);
				TableData networkProviderTable=null;
	    		if(request.getSession().getAttribute("networkProviderTable")!=null)
	    			networkProviderTable=(TableData) request.getSession().getAttribute("networkProviderTable");
	    		else
	    			networkProviderTable=new TableData();
				OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
				networkProviderTable.modifySearchData(strBackward);// modify the search data
				ArrayList networkProviderData=  onlineAccessObject.getProviderNetworkList(networkProviderTable.getSearchData(),"Administration");
				networkProviderTable.setData(networkProviderData, strBackward);// set the table data
				request.getSession().setAttribute("networkProviderTable",networkProviderTable);
				return mapping.findForward("EmployeeNetworkHome");
			}// end of try
			catch(TTKException expTTK)
	    	{
	    		return this.processOnlineExceptions(request, mapping, expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
	    	}
		}
		
		
		public ActionForward doExportToExcel(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			try {
				log.debug("Inside EmployeeAction doExportToExcel");
				setOnlineLinks(request);
				OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
				TableData networkProviderTable=null;
	    		if(request.getSession().getAttribute("networkProviderTable")!=null)
	    			networkProviderTable=(TableData) request.getSession().getAttribute("networkProviderTable");
	    		else
	    			networkProviderTable=new TableData();
	    		int iRowCount = networkProviderTable.obj1.getRowCount();
	    		if(networkProviderTable.obj1.getRowCount()==-1)
        			iRowCount = networkProviderTable.getData().size();
	    		int startindex = (networkProviderTable.obj1.getCurrentPage()-1) * iRowCount + 1;
        		if(startindex > networkProviderTable.getData().size())
        		{
        			startindex = (networkProviderTable.obj1.getCurrentPage()-2) * networkProviderTable.obj1.getRowCount() + 1;
        			networkProviderTable.obj1.setCurrentPage((networkProviderTable.obj1.getCurrentPage()-1) <= 0 ? 1 : networkProviderTable.obj1.getCurrentPage()-1);
        		}
        		if(startindex < 0)
        		{
        			startindex = 1;
        		}//end of if(startindex < 0)
        		int endindex = networkProviderTable.obj1.getCurrentPage() * iRowCount > networkProviderTable.getData().size() ? networkProviderTable.getData().size() : networkProviderTable.obj1.getCurrentPage() * iRowCount;
				ArrayList networkProviderData=new ArrayList<>();
				for(int i=startindex-1;i<endindex;i++){
					networkProviderData.add(networkProviderTable.getRowInfo(i));
				}
				//System.out.println("Data size is::"+networkProviderData.size());
				return mapping.findForward("EmployeeNetworkHome");
			}// end of try
			catch(TTKException expTTK)
	    	{
	    		return this.processOnlineExceptions(request, mapping, expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
	    	}
		}
		
		public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				  HttpServletResponse response) throws TTKException{
	  	 try{
		    		log.debug("Inside the doGenerateClimSummaryReport method of HospitalReportsAction");
		    		setOnlineLinks(request);
		    		TableData networkProviderTable=null;
		    		if(request.getSession().getAttribute("networkProviderTable")!=null)
		    			networkProviderTable=(TableData) request.getSession().getAttribute("networkProviderTable");
		    		else
		    			networkProviderTable=new TableData();
		    		int iRowCount = networkProviderTable.obj1.getRowCount();
		    		
		    		if(networkProviderTable.obj1.getRowCount()==-1)
	        			iRowCount = networkProviderTable.getData().size();
		    		/*ifint startindex = (networkProviderTable.obj1.getCurrentPage()-1) * iRowCount + 1;
	        		if(startindex > networkProviderTable.getData().size())
	        		{
	        			startindex = (networkProviderTable.obj1.getCurrentPage()-2) * networkProviderTable.obj1.getRowCount() + 1;
	        			networkProviderTable.obj1.setCurrentPage((networkProviderTable.obj1.getCurrentPage()-1) <= 0 ? 1 : networkProviderTable.obj1.getCurrentPage()-1);
	        		}
	        		if(startindex < 0)
	        		{
	        			startindex = 1;
	        		}//end of if(startindex < 0)
	        		int endindex = networkProviderTable.obj1.getCurrentPage() * iRowCount > networkProviderTable.getData().size() ? networkProviderTable.getData().size() : networkProviderTable.obj1.getCurrentPage() * iRowCount;*/
	        		ArrayList searchData=networkProviderTable.getSearchData();
		    		/*String stInd=(String) searchData.get(8);
		    		String endInd=(String) searchData.get(9);
		    		int stIndex=(Integer.parseInt(stInd)+startindex)-1;
		    		int enIndex=(Integer.parseInt(stInd)+endindex)-1;*/
//		    		searchData.add(8, String.valueOf(0));
//		    		searchData.add(9, String.valueOf(networkProviderTable.getData().size()));
	        		JasperReport mainJasperReport,emptyReport;
		    		TTKReportDataSource mainTtkReportDataSource = null;
		    		JasperPrint mainJasperPrint = null;
		    		 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
		    		 String strReportId1 = "";
		    		 String parameter=request.getParameter("parameter");
		    		try
		    		{
		    		    emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		    			HashMap<String,String> hashMap = new HashMap<String,String>();
		    			ByteArrayOutputStream boas=new ByteArrayOutputStream();
		    			ResultSet main_report_RS =null;
		    			if(searchData.size()!=0){
		    			mainTtkReportDataSource = new TTKReportDataSource("EmplExcelExporter",searchData);
		    			main_report_RS= mainTtkReportDataSource.getResultData();
		    			}
		    			mainJasperReport = JasperCompileManager.compileReport("onlinereports/employeeLogin/providerlist.jrxml");
		    			if (main_report_RS != null && main_report_RS.next()) {
		    				main_report_RS.beforeFirst();
		    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);				
		    			} else {
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    			}
			    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
						jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
						jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
						jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						jExcelApiExporter.exportReport();
		    			request.setAttribute("boas",boas);
		    			request.setAttribute("reportType", "EXL");
		    			response.setContentType("application/vnd.ms-excel");
		    			response.addHeader("Content-Disposition","attachment; filename=NETWORKPROVIDERSLIST.xls");
		    			return mapping.findForward("reportdisplay");
		    		}
		    		catch(Exception exp)
		        	{
		    			return this.processExceptions(request, mapping, new TTKException(exp, "reportdisplay"));
		        	}
		    		
		    	}
		    	catch(TTKException expTTK)
		    	{
		    		return this.processExceptions(request, mapping, expTTK);
		    	}
		    	catch(Exception exp)
		    	{
		    		return this.processExceptions(request, mapping, new TTKException(exp, "reportdisplay"));
		    	}
	    }
	
	 private ArrayList<Object> populateProviderSearchCriteria(DynaActionForm frmSearchHospital, Long policySeqId) {
		 ArrayList arrayList=new ArrayList<>();
			arrayList.add(frmSearchHospital.get("countryCode"));
			arrayList.add(frmSearchHospital.get("stateCode"));
			arrayList.add(frmSearchHospital.get("cityCode"));
			arrayList.add(frmSearchHospital.get("providerTypeId"));
			arrayList.add(frmSearchHospital.get("speciality"));
			arrayList.add(frmSearchHospital.get("networkType"));
			arrayList.add(policySeqId);
			return arrayList;
	}

	public ActionForward doGetProviderState(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			try {
				setOnlineLinks(request);
			//	checkWebboardVisabulity(request);
				log.debug("Inside doChangeState method of AccountsAction");
				String focusID	=	request.getParameter("focusID");
				AccountDetailVO accountDetailVO = null;
				ArrayList alStateCode = null;
				DynaActionForm frmSearchHospital = (DynaActionForm) form;
				String countryCode = (String) TTKCommon.checkNull(frmSearchHospital.get("countryCode"));
				OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();			
				alStateCode = onlineAccessObject.getProviderState(countryCode,focusID);
				if (alStateCode == null) {
					alStateCode = new ArrayList();
				}// end of if(alCityList==null)
				frmSearchHospital.set("frmChanged", "changed");
				frmSearchHospital.set("stateCodeList", alStateCode);
				request.getSession().setAttribute("stateCodeList", alStateCode);
				request.setAttribute("focusID", focusID);
				return mapping.findForward("EmployeeNetworkHome");
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strEmplHome));
			}// end of catch(Exception exp)
		}// end of doChangeState(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	 
	 public ActionForward doGetProviderCity(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
			try {
				setOnlineLinks(request);
			//	checkWebboardVisabulity(request);
				log.debug("Inside doChangeState method of AccountsAction");
				String focusID	=	request.getParameter("focusID");
				DynaActionForm frmSearchHospital = (DynaActionForm) form;
				String cityCode = (String) TTKCommon.checkNull(frmSearchHospital.get("stateCode"));
				ArrayList cityList=null;
				OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();			
				cityList = onlineAccessObject.getCityList(cityCode,focusID);
			
				if (cityList == null) {
					cityList = new ArrayList();
				}// end of if(alCityList==null)
				frmSearchHospital.set("frmChanged", "changed");
				frmSearchHospital.set("cityCodeList", cityList);
				request.getSession().setAttribute("cityCodeList", cityList);
				// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
				request.setAttribute("focusID", focusID);
				return mapping.findForward("EmployeeNetworkHome");
				
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strEmplHome));
			}// end of catch(Exception exp)
		}// end of doChangeState(ActionMapping mapping,ActionForm
	 public ActionForward doSubmitFile(ActionMapping mapping, ActionForm form,HttpServletRequest request, 
				HttpServletResponse response)throws Exception {

		try {
			setOnlineLinks(request);
			log.info("Inside the doSubmitFile method of EmpClaimSubmissionAction");
			request.getSession().setAttribute("editable", null);
			OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManager();
			DynaActionForm frmEmpClaimSubmission = (DynaActionForm) form;
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
			ArrayList alResult =new ArrayList();
			    LinkedHashMap<String, String> mapObject = new LinkedHashMap<String, String>();
				
				OnlineIntimationVO onlineIntimationVO = null;
				FileOutputStream outputStream = null;
				int fileSize = 2 * 1024 * 1024;
				String fileExtn	=	"";
				FormFile formFile1 = null;
				ArrayList<FormFile> formFileList=new ArrayList<FormFile>();
				for(int inc=0;inc<5;inc++){
					
					if(inc==0)
					formFile1 = (FormFile)frmEmpClaimSubmission.get("file");
					else
						formFile1 = (FormFile)frmEmpClaimSubmission.get("file"+inc);
					if(formFile1!=null&&formFile1.getFileSize()!=0)
					formFileList.add(formFile1);
				}
				if(formFileList.size()!=0){
					for (FormFile formFile : formFileList) {
						if(formFile.getFileSize()!=0)
						{
							if(formFile.getFileSize()/(1024*1024)<3){
								fileExtn = GetFileExtension(formFile.toString());
								fileExtn=fileExtn.toLowerCase();
								if("EMPL".equals(userSecurityProfile.getLoginType())){
									if(!"pdf".equals(fileExtn)&&!"png".equals(fileExtn)&&!"jpeg".equals(fileExtn)&&!"jpg".equals(fileExtn)){
										TTKException expTTK = new TTKException();
										expTTK.setMessage("error.file.employee.restricted.format");
										throw expTTK;
									}
									
								}else{
								if(!"pdf".equals(fileExtn)){
									TTKException expTTK = new TTKException();
									expTTK.setMessage("error.pdf.only.required");
									throw expTTK;
								}}
							}else{
								TTKException expTTK = new TTKException();
								expTTK.setMessage("error.file.size.issue");
								throw expTTK;
							}
							
						}
						else
						{
							TTKException expTTK = new TTKException();
							expTTK.setMessage("error.documents.required");
							throw expTTK;
						}
					}
				}else
				{
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.documents.required");
					throw expTTK;
				}
				PDFMergerUtility mergerUtility = new PDFMergerUtility();
				ArrayList<InputStream> convertedFormFile=new ArrayList();
				for (int i = 0; i < formFileList.size(); i++) {
					FormFile inputFormFile=formFileList.get(i);
					InputStream convertedStream=null;
					if(inputFormFile.getFileName().contains(".png")||inputFormFile.getFileName().contains(".PNG")
							||inputFormFile.getFileName().contains(".JPEG")||inputFormFile.getFileName().contains(".jpeg")
							||inputFormFile.getFileName().contains(".JPG")||inputFormFile.getFileName().contains(".jpg"))
						convertedStream=imagetoPDF(inputFormFile.getFileData());
					else
						convertedStream=inputFormFile.getInputStream();
					if(convertedStream!=null)
					convertedFormFile.add(convertedStream);
				}
				ByteArrayOutputStream mergeOutputStream=new ByteArrayOutputStream();
				for (int i = 0; i < convertedFormFile.size(); i++) {
					mergerUtility.addSource(convertedFormFile.get(i));
				}
//				mergerUtility.setDestinationFileName("D:\\mytestfiles\\myTest.pdf");
				mergerUtility.setDestinationStream(mergeOutputStream);
				try{
				mergerUtility.mergeDocuments();
				}catch(IOException io){
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.documents.corrupted");
					throw expTTK;
				}
				byte[] bytes = mergeOutputStream.toByteArray();
				InputStream inputStream = new ByteArrayInputStream(bytes);
				onlineIntimationVO = (OnlineIntimationVO)FormUtils.getFormValues(frmEmpClaimSubmission, this, mapping, request);
				onlineIntimationVO.setPdfInputStream(inputStream);
				onlineIntimationVO.setClaimBatchSeqId((Long) frmEmpClaimSubmission.get("claimBatchSeqId"));		
				onlineIntimationVO.setClaimSeqId((Long) frmEmpClaimSubmission.get("claimSeqId"));		
				onlineIntimationVO.setClaimBatchNumber((String) frmEmpClaimSubmission.get("claimBatchNumber"));		
				onlineIntimationVO.setMemberClaimSeqID((Long) request.getSession().getAttribute("claimMemberSeqId"));		
				onlineIntimationVO.setInvoiceno((String) frmEmpClaimSubmission.get("invoiceno"));		
			//	onlineIntimationVO.setRequestedamount((Long) frmEmpClaimSubmission.get("requestedamount"));		
				onlineIntimationVO.setCurrencyAccepted((String) frmEmpClaimSubmission.get("currencyAccepted"));
				onlineIntimationVO.setFileType("pdf");
				alResult=onlineAccessManagerObject.saveOnlineEmployeeClaimSubmission(onlineIntimationVO, formFile1);
				request.setAttribute("successMsg", "message.savedSuccessfully");
				if("EMPL".equals(userSecurityProfile.getLoginType())){
				frmEmpClaimSubmission.set("requestedAmount", null);
				frmEmpClaimSubmission.set("invoiceno", "");
				frmEmpClaimSubmission.set("currencyAccepted", "");
				mergeOutputStream.close();
				}
				if(alResult != null && alResult.size() >0)
	       		{
					frmEmpClaimSubmission.set("alertMsg",(String) alResult.get(0));
	             //  System.out.println("alResult.get(0)   "+alResult.get(0));
	               
	               frmEmpClaimSubmission.set("Batch_No",(String) alResult.get(1));
	               //System.out.println("alResult.get(1)   "+alResult.get(1));
	               request.setAttribute("Batch_Seq_ID", (String) alResult.get(1));
	               mapObject.put("claimSubFlag","Y");
	       		}
	               else{
	            	   frmEmpClaimSubmission.set("alertMsg","No data found");
		               mapObject.put("claimSubFlag","N");
	               }
				if("EMPL".equals(userSecurityProfile.getLoginType())){
				request.getSession().setAttribute("frmEmpClaimSubmission", frmEmpClaimSubmission);
				}
				if("EMPL".equals(userSecurityProfile.getLoginType()))
					return mapping.findForward("EmployeeClaimSubmission");
				else
					return this.getForward("claimsubmission", mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processOnlineExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp, strEmplHome));
			}//end of catch(Exception exp)
		}//end of method doEnrollCancel(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	    
	 
	 public ActionForward generatePreauthLetter(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			try {
				setOnlineLinks(request);
				
				DynaActionForm frmPreAuthGeneral = (DynaActionForm) form;
				MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
				String preAuthSeqID = String.valueOf(memberDetailVO.getPreAuthSeqId());
				String preauthStatus = memberDetailVO.getStatus();
				if("Approved".equals(preauthStatus))
					preauthStatus="APR";
				else if("Rejected".equals(preauthStatus))
					preauthStatus="REJ";
				String preAuthNo	=	memberDetailVO.getPreAuthNumber(); 

				JasperReport mainJasperReport = null;
				JasperReport diagnosisJasperReport = null;
				JasperReport activityJasperReport = null;
				JasperPrint mainJasperPrint = null;
				String parameter = "";
				
				String mainJrxmlfile = "generalreports/PreAuthApprovalOrDenialLetter.jrxml";
				String diagnosisJrxmlfile = "generalreports/DiagnosisDoc.jrxml";
				String activityJrxmlfile = "generalreports/ActivitiesDoc.jrxml";
				TTKReportDataSource mainTtkReportDataSource = null;
				TTKReportDataSource diagnosisTtkReportDataSource = null;
				TTKReportDataSource activityTtkReportDataSource = null;
				ByteArrayOutputStream boas = new ByteArrayOutputStream();
				String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir") + preAuthNo + ".pdf"; 
				JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				  // mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				parameter = "|" + preAuthSeqID + "|" + preauthStatus + "|PAT|";
				mainTtkReportDataSource = new TTKReportDataSource("PreauthLetterFormat", parameter);
				diagnosisTtkReportDataSource = new TTKReportDataSource("DiagnosisDetails", parameter);
				activityTtkReportDataSource = new TTKReportDataSource("ActivityDetails", parameter);
				ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
				mainJasperReport = JasperCompileManager	.compileReport(mainJrxmlfile);
				diagnosisJasperReport = JasperCompileManager.compileReport(diagnosisJrxmlfile);
				activityJasperReport = JasperCompileManager.compileReport(activityJrxmlfile);
				hashMap.put("diagnosisDataSource", diagnosisTtkReportDataSource);
				hashMap.put("diagnosis", diagnosisJasperReport);
				hashMap.put("activityDataSource", activityTtkReportDataSource);
				hashMap.put("activity", activityJasperReport);
				// JasperFillManager.fillReport(activityJasperReport, hashMap,
				// activityTtkReportDataSource);

				if (main_report_RS == null & !main_report_RS.next()) {
					
					mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
				
				} else {
					String authorisedBy=main_report_RS.getString("AUTHORISED_BY");
					
					hashMap.put("authorisedBy", authorisedBy);
					
					main_report_RS.beforeFirst();
					mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);
				}// end of else
				
				JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
				JasperExportManager.exportReportToPdfFile(mainJasperPrint,strPdfFile);
				request.setAttribute("boas", boas);
				return mapping.findForward("reportdisplay");// This forward goes to
															// the in web.xml file
															// BinaryServlet
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(exp, strEmplHome));
			}// end of catch(Exception exp)
		}
	 
	 public ActionForward generateClaimLetter(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			try {
				setOnlineLinks(request);
				DynaActionForm frmClaimGeneral = (DynaActionForm) form;
				MemberDetailVO memberDetailVO=(MemberDetailVO) request.getSession().getAttribute("memberDetailVO");
				String claimSeqID = String.valueOf(memberDetailVO.getClaimSeqID());
				String claimSettelmentNo = memberDetailVO.getSerialNumber();
				String claimStatus = memberDetailVO.getStatus();
				if("Approved".equalsIgnoreCase(claimStatus))
					claimStatus="APR";
				else if("Rejected".equalsIgnoreCase(claimStatus))
					claimStatus="REJ";
				String claimType 	= memberDetailVO.getClaim_type();
				if("Member".equalsIgnoreCase(claimType))
					claimType="CTM";
				else
					claimType="CNH";
				JasperReport mainJasperReport = null;
				JasperReport diagnosisJasperReport = null;
				JasperReport activityJasperReport = null;
				JasperPrint mainJasperPrint = null;
			String parameter = "";
			String mainJrxmlfile="";
			String activityJrxmlfile="";
			TTKReportDataSource mainTtkReportDataSource = null;
				TTKReportDataSource diagnosisTtkReportDataSource = null;
				TTKReportDataSource activityTtkReportDataSource = null;
			parameter="|"+claimSeqID+"|"+claimStatus+"|CLM|";
					 if("APR".equals(claimStatus))
						 mainJrxmlfile = "generalreports/ClaimApprovalOrDenialLetter.jrxml";
					 else
						 mainJrxmlfile = "generalreports/ClaimDenialLetter.jrxml";
					 if("CTM".equals(claimType))
						 activityJrxmlfile = "generalreports/MemberClaimActivities.jrxml";
					 else
						 activityJrxmlfile = "generalreports/ClaimActivitiesDoc.jrxml";
				 mainTtkReportDataSource=new TTKReportDataSource("ClaimLetterFormat",parameter);
				
			 
				 String diagnosisJrxmlfile = "generalreports/ClaimDiagnosisDoc.jrxml";
			 

				ByteArrayOutputStream boas = new ByteArrayOutputStream();

				String strPdfFile = TTKPropertiesReader
						.getPropertyValue("authorizationrptdir")+ claimSettelmentNo + ".pdf";
				JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				//mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
		 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
			
			diagnosisTtkReportDataSource = new TTKReportDataSource("DiagnosisDetails",parameter);  		
			activityTtkReportDataSource = new TTKReportDataSource("ActivityDetails",parameter);

			ResultSet main_report_RS=mainTtkReportDataSource.getResultData();
			mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
			diagnosisJasperReport = JasperCompileManager.compileReport(diagnosisJrxmlfile);
			activityJasperReport = JasperCompileManager.compileReport(activityJrxmlfile);			  
			
				 hashMap.put("diagnosisDataSource",diagnosisTtkReportDataSource);
				 hashMap.put("diagnosis",diagnosisJasperReport);		
				 hashMap.put("activityDataSource",activityTtkReportDataSource);		
				 hashMap.put("activity",activityJasperReport);
				 //JasperFillManager.fillReport(activityJasperReport, hashMap, activityTtkReportDataSource);						 
		 
		 if (main_report_RS == null&!main_report_RS.next())
		 {
			 mainJasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
		 }
		 else
		 {
			 main_report_RS.beforeFirst();
					mainJasperPrint = JasperFillManager.fillReport(
							mainJasperReport, hashMap, mainTtkReportDataSource);
				}// end of else
				JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
				JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
				JasperExportManager.exportReportToPdfFile(mainJasperPrint,
						strPdfFile);
//		 frmClaimGeneral.set("letterPath", strPdfFile);
				request.setAttribute("boas", boas);
				return mapping.findForward("reportdisplay");// This forward goes to
															// the in web.xml file
															// BinaryServlet
			}// end of try
			catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strOnlinePolicies));
			}// end of catch(Exception exp)
		}
			private RuleManager getRuleManagerObject() throws TTKException
		    {
		        RuleManager ruleManager = null;
		        try
		        {
		            if(ruleManager == null)
		            {
		                InitialContext ctx = new InitialContext();
		                ruleManager = (RuleManager) ctx.lookup("java:global/TTKServices/business.ejb3/RuleManagerBean!com.ttk.business.administration.RuleManager");
		            }//end if(ruleManager == null)
		        }//end of try
		        catch(Exception exp)
		        {
		            throw new TTKException(exp, "memberdetail");
		        }//end of catch
		        return ruleManager;
		    }//end of getRuleManagerObject()
			public static String GetFileExtension(String fname2)
			{
			    String fileName = fname2;
			    String fname="";
			    String ext="";
			    int mid= fileName.lastIndexOf(".");
			    fname=fileName.substring(0,mid);
			    ext=fileName.substring(mid+1,fileName.length());
			    return ext;
			} 	
			
			public ActionForward doChangeQatarYN(ActionMapping mapping, ActionForm form,
					HttpServletRequest request, HttpServletResponse response) throws Exception {
				try {
//					setOnlineLinks(request);
		    		log.info("Inside doChangeQatarYN method of AddEnrollmentAction");
					DynaActionForm accountForm = (DynaActionForm)form;
					HttpSession session = request.getSession();
					UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
					String qatarYN = accountForm.getString("bankAccountQatarYN");
					MemberAddressVO memberAddressVO= new MemberAddressVO();
					accountForm.set("bankAccountQatarYN", qatarYN);
					accountForm.set("bankname", "");
					accountForm.set("bankState", "");
					accountForm.set("bankcity", "");
					accountForm.set("bankBranchText", "");
					accountForm.set("bankBranch", "");
					accountForm.set("ifsc", "");
					accountForm.set("neft", "");
					accountForm.set("bankAccNbr", "");
					accountForm.set("bankPhoneno", "");
					accountForm.set("address1", "");
					if("N".equals(qatarYN)){
						accountForm.set("bankCountry", "");
					}
					else{
						accountForm.set("bankCountry", "134");
					}
					
					memberAddressVO.setEmailID("");
					session.setAttribute("frmAddEnrollment", accountForm);
					//accountForm.reset(request, response); 
					return mapping.findForward("EmployeeClaimSubmission");
					
				}catch(Exception exp)
				{
					return this.processOnlineExceptions(request,mapping,new TTKException(exp,"EmployeeClaimSubmission"));
				}//end of catch(Exception exp)
		    }//end doChangeQatarYN
			 public static InputStream  imagetoPDF(byte[] input) {  
				 InputStream convertedStream =null;
		            try {
		    	    	Image image = Image.getInstance(input);
		    	    	com.itextpdf.text.Document document = new com.itextpdf.text.Document(image);
		    	    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    	    	PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		              document.open();
		              image.setAbsolutePosition(0, 0);
		              document.add(image);
		              document.close();
		              byte[] readed=outputStream.toByteArray();
		              convertedStream = new ByteArrayInputStream(readed);
		              outputStream.close();
		            }
		            catch (Exception e) {
		              e.printStackTrace();
		            }
		            return convertedStream;
		          }

}//end of class OnlinePoliciesAction
