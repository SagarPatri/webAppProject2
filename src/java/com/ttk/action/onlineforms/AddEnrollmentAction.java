/**
 * @ (#) AddEnrollmentAction.java Jan 16, 2008
 * Project      : TTK HealthCare Services
 * File         : AddEnrollmentAction.java
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : Jan 16, 2008
 *
 * @author       :  Chandrasekaran J
 * Modified by   :
 * Modified date :
 * Reason        :
 * Modified by   :
 */
package com.ttk.action.onlineforms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.action.tree.TreeData;
import com.ttk.business.administration.RuleManager;
import com.ttk.business.common.messageservices.CommunicationManager;
//import com.ttk.business.empanelment.HospitalManager;
//import com.ttk.business.finance.CustomerBankDetailsManager;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.empanelment.AccountDetailVO;
import com.ttk.dto.enrollment.MemberAddressVO;
import com.ttk.dto.enrollment.MemberDetailVO;
import com.ttk.dto.enrollment.MemberVO;
import com.ttk.dto.enrollment.OnlineAccessPolicyVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AddEnrollmentAction extends TTKAction
{
	private static Logger log = Logger.getLogger( AddEnrollmentAction.class );
	//for forwarding
	private static final String strOnlineEmpDetails="onlineEmpDetails";
	private static final String strOnlineEmpInfo="onlineEmpInfo";
	private static final String strSaveOnlineEmpDetails="saveOnlineEmpDetails";
	private static final String strSendOnlineEmpDetails="sendOnlineEmpDetails";


	private static final String strClose="closeAddEnrollment";
	//  Exception Message Identifier
    private static final String strEnrollment="enrollment";
    private static final String strEnrollmentView="Enrollment";
    private static final String strPreAuth="Pre-Auth";
    private static final String strClaims="Claims";
    private static final String strMemberInfo="Member Info";   

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
    public ActionForward doViewEmpDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    										HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);
    		log.debug("Inside AddEnrollmentAction doViewEmpDetails");
    		DynaActionForm frmAddEnrollment= (DynaActionForm) form;
    		DynaActionForm frmMember=(DynaActionForm) form;
    		String strActiveSubLink=TTKCommon.getActiveSubLink(request);
    		String strForward="";
//            HospitalManager hospitalObject=this.getHospitalManagerObject();
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
		//	CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
    		UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
    		StringBuffer sbfCaption=new StringBuffer();
    		OnlineAccessPolicyVO onlinePolicyVO=null;
 			String strSelectedRoot=request.getParameter("selectedroot");
 			TreeData treeData=null;
 			String selectedroot=null;
 			MemberDetailVO memberDetailVO= new MemberDetailVO();
 			MemberAddressVO memberAddressVO=new MemberAddressVO();
 			memberDetailVO.setMemberAddressVO(memberAddressVO);
 			ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
 			MemberVO memberVO=null;
// 			OnlineAccessPolicyVO onlinePolicyVO =null;
// 			onlinePolicyVO = (OnlineAccessPolicyVO)request.getSession().getAttribute("SelectedPolicy");
// 			frmMember.set("empaddyn",TTKCommon.checkNull(onlinePolicyVO.getEmployeeAddYN()));
 			if("EMPL".equals(userSecurityProfile.getLoginType())){
 				String rowNum=request.getParameter("rownum");
 				TableData tableData=(TableData) request.getSession().getAttribute("employeeDataTable");
 				memberVO=(MemberVO) tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
 				selectedroot=memberVO.getEnrollmentID();
 			}else{
 				selectedroot=request.getParameter("selectedroot");
 				treeData =(TreeData)request.getSession().getAttribute("treeData") ;
 				int iSelectedRoot= TTKCommon.getInt(selectedroot);
 				memberVO=(MemberVO)treeData.getRootData().get(iSelectedRoot);
 			}
 			if((strSelectedRoot!=null&&!strSelectedRoot.equals(""))||"EMPL".equals(userSecurityProfile.getLoginType()))// on clicking the edit link
 			{
 				alAddEnrollment.add(memberVO.getPolicyGroupSeqID());
 				alAddEnrollment.add(memberVO.getPolicySeqID());				
 				alAddEnrollment.add(memberVO.getMemberSeqID());
 				memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);
 				if(userSecurityProfile.getLoginType().equals("H"))
 	 			{
 					onlinePolicyVO = (OnlineAccessPolicyVO)request.getSession().getAttribute("SelectedPolicy");
 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(onlinePolicyVO.getPolicyNbr()).append("] [").
 	 				append(userSecurityProfile.getGroupID()).append("] [").append(onlinePolicyVO.getGroupName()).append("]");
 	 			}//end of if(userSecurityProfile.getLoginType().equals("H"))
 	 			else if(userSecurityProfile.getLoginType().equals("E")||userSecurityProfile.getLoginType().equals("EMPL"))
 	 			{
 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(userSecurityProfile.getPolicyNo()).append("] [").
 					append(userSecurityProfile.getGroupID()).append("] [").append(memberVO.getGroupName()).append("]");
 	 				request.setAttribute("frmMember", frmMember);
 	 			}//end of else if(userSecurityProfile.getLoginType().equals("E"))
 	 			else if(userSecurityProfile.getLoginType().equals("OCI"))
 	 			{
 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(userSecurityProfile.getPolicyNo()).append("]");
 	 			}//end of else if(userSecurityProfile.getLoginType().equals("OCI"))
 			}//end of if(!strSelectedRoot.equals(""))
 			frmAddEnrollment= (DynaActionForm)FormUtils.setFormValues("frmAddEnrollment",memberDetailVO,
	 					this,mapping,request);	
	 		frmAddEnrollment.set("memberAddressVO", FormUtils.setFormValues("frmMemberAddress",
	 					memberDetailVO.getMemberAddressVO(),this,mapping,request));
	 		if(memberDetailVO.getEmpStatusTypeID().equals("POC"))
	 		{
	 			request.setAttribute("statusinfo","error.onlineforms.cancelled");
	 		}//end of if(memberDetailVO.getEmpStatusTypeID().equals("POC"))
 			ArrayList alLocation=new ArrayList();
 			alLocation = onlineAccessObject.getLocation(memberVO.getPolicySeqID());
			request.getSession().setAttribute("alLocation",alLocation);
			if(userSecurityProfile.getLoginType().equals("OCI"))
			{
				frmAddEnrollment.set("allowCityBankYN","Y");
			}//end of if(userSecurityProfile.getLoginType().equals("OCI"))
			else
			{
				frmAddEnrollment.set("allowCityBankYN","N");
			}//end of else
			frmAddEnrollment.set("allowModiYN",TTKCommon.checkNull(memberVO.getAllowModiYN()));
 			frmAddEnrollment.set("selectedRoot",strSelectedRoot);
 			frmAddEnrollment.set("caption",sbfCaption.toString());

 			//Added for IBM...1

			frmAddEnrollment.set("OPT",memberDetailVO.getStopOPtInYN());//Getting the checkbox values.
			frmAddEnrollment.set("windowsOPTYN",memberDetailVO.getwindowsOPTYN());//Getting windowsperiod
			frmAddEnrollment.set("chkpreauthclaims",memberDetailVO.getchkpreauthclaims());//Getting Pre-Auth/claim existence

			frmAddEnrollment.set("EmailPh",memberDetailVO.getValidEmailPhYN()); // Added for KOC-1216


			if(memberDetailVO.getchkpreauthclaims().equalsIgnoreCase("Y"))
			{
				request.setAttribute("chkPreAuth","message.chkPreAuthClaim");//Msg Display on Preauth-ADDED BY PRAVEEN
			}
			else if(!(memberDetailVO.getwindowsOPTYN().equalsIgnoreCase("Y")))
			{
				request.setAttribute("chkWindowsPeriod","message.chkWindowsPeriod");//Msg Display on WindowsPeriod-ADDED BY PRAVEEN
			}
			
			frmAddEnrollment.set("EmailId2",memberDetailVO.getMemberAddressVO().getEmailID2());
		//	frmAddEnrollment.set("MobileNo",memberDetailVO.getMemberAddressVO().getMobileNbr());
			frmAddEnrollment.set("MarriageHideYN",memberDetailVO.getMarriageHideYN());//koc for griavance
			
			/*	memberAddressVO.setStateCode("DOH");
	            HashMap hmCityLists = null;    
	            ArrayList alCityLists = new ArrayList();
	           hmCityLists=onlineAccessObject.getCityInfo("DOH");
	         	
	            String countryCode	=	(String)(hmCityLists.get("CountryId"));
	            String isdcode	=	(String)(hmCityLists.get("isdcode"));
	            String stdcode	=	(String)(hmCityLists.get("stdcode"));
	          
	            memberDetailVO.setMemberAddressVO(memberAddressVO);

	            if(hmCityLists!=null)
	            {
	                alCityLists = (ArrayList)hmCityLists.get(memberDetailVO.getMemberAddressVO().getStateCode());
	            }//end of if(hmCityList!=null)
	            if(alCityLists==null)
	            {
	                alCityLists=new ArrayList();
	            }//end of if(alCityList==null)
	                      
	            memberAddressVO.setCountryCode(countryCode);
	            memberAddressVO.setOff1IsdCode(isdcode);		
	            memberAddressVO.setOff2IsdCode(isdcode);		
	            memberAddressVO.setHomeIsdCode(isdcode);	
	            memberAddressVO.setMobileIsdCode(isdcode);
	            
	            memberAddressVO.setOff1StdCode(stdcode);
	            memberAddressVO.setOff2StdCode(stdcode);
	            memberAddressVO.setHomeStdCode(stdcode);*/
			
	            request.getSession().setAttribute("providerStates",TTKCommon.getStates(memberDetailVO.getMemberAddressVO().getCountryCode()));   
	            request.getSession().setAttribute("providerAreas",TTKCommon.getAreas(memberDetailVO.getMemberAddressVO().getStateCode())); 
			
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;

			//hmCityList = customerBankDetailsManager.getbankStateInfo();
			hmCityList = onlineAccessObject.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmAddEnrollment.get("bankName")));
			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;
			
			String bankState = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankState"));			
			String bankName = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankName"));
			String bankDistict = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankcity"));
			
		//hmDistList = customerBankDetailsManager.getBankCityInfo(bankState,bankName);
			hmDistList = onlineAccessObject.getBankCityInfo(bankState,bankName);
			
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmAddEnrollment.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			//hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			hmBranchList = onlineAccessObject.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmAddEnrollment.get("bankcity")));

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
			frmAddEnrollment.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			frmAddEnrollment.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			frmAddEnrollment.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);			
			String photoYN = memberDetailVO.getPhotoYN();
			request.getSession().setAttribute("photoYN", photoYN);
			frmAddEnrollment.set("photoYN", photoYN);
			frmAddEnrollment.set("empaddyn", "Y");
			//End of IBM....
 			request.getSession().setAttribute("frmAddEnrollment",frmAddEnrollment);
 			if(strActiveSubLink.equals(strEnrollmentView))
			{
				strForward="onlineEmpDetails";
			}
 			if(strActiveSubLink.equals(strPreAuth))
			{
				strForward="onlineEmpDetailsPreAuth";
			}
 			if(strActiveSubLink.equals(strClaims))
			{
				strForward="onlineEmpDetailsClaims";
			}
 			
 			request.getSession().setAttribute("alImg",memberDetailVO.getImageData());
 			request.getSession().removeAttribute("BankSeqIdForEmployeeAndHR");
 			request.getSession().setAttribute("principalName",memberDetailVO.getInsuredName());
 			if("EMPL".equals(userSecurityProfile.getLoginType())){
 				return mapping.findForward("onlineEmplEmpDetails");
 			}else
    		return mapping.findForward(strOnlineEmpDetails);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request,mapping,expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request,mapping,new TTKException(exp,strEnrollment));
    	}//end of catch(Exception exp)
    }//end of doViewEmpDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse
    																									//response)
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
    public ActionForward doViewEmpInfo(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    										HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);
    		log.debug("Inside AddEnrollmentAction doViewEmpDetails");
    		DynaActionForm frmAddEnrollment= (DynaActionForm) form;
    		OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
    		StringBuffer sbfCaption=new StringBuffer();
    		OnlineAccessPolicyVO onlinePolicyVO=null;
    		Long policyseqid = null;
			Long policygroupseqid = null;
    		
    		//---------------------------------------------------------
    		TableData  tableData =TTKCommon.getTableData(request);
			 tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,userSecurityProfile.getLoginType()));
				ArrayList alPolicyList=onlineAccessObject.getOnlineEnrollmentList(tableData.getSearchData());
				 //  
				 MemberVO c1 = new MemberVO();
				 if(alPolicyList!=null && alPolicyList.size()>0)
					{
						c1=(MemberVO)alPolicyList.get(0);
						  policyseqid = c1.getPolicySeqID();
						  policygroupseqid = c1.getPolicyGroupSeqID();
					}
    		//--------------------------------------------------------
 			 
				 
 			MemberDetailVO memberDetailVO= new MemberDetailVO();
 			MemberAddressVO memberAddressVO=new MemberAddressVO();
 			memberDetailVO.setMemberAddressVO(memberAddressVO);
 			ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
 			 			 
 				alAddEnrollment.add(policygroupseqid);
 				alAddEnrollment.add(policyseqid);
		
 				alAddEnrollment.add(memberDetailVO.getMemberSeqID());
 				memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);
 				if(userSecurityProfile.getLoginType().equals("H"))
 	 			{
 					onlinePolicyVO = (OnlineAccessPolicyVO)request.getSession().getAttribute("SelectedPolicy");
 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(onlinePolicyVO.getPolicyNbr()).append("] [").
 	 				append(userSecurityProfile.getGroupID()).append("] [").append(onlinePolicyVO.getGroupName()).append("]");
 	 			}//end of if(userSecurityProfile.getLoginType().equals("H"))
 	 			else if(userSecurityProfile.getLoginType().equals("E"))
 	 			{
 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(userSecurityProfile.getPolicyNo()).append("] [").
 					append(userSecurityProfile.getGroupID()).append("] [").append(c1.getGroupName()).append("]");
 	 			}//end of else if(userSecurityProfile.getLoginType().equals("E"))
 	 			else if(userSecurityProfile.getLoginType().equals("OCI"))
 	 			{
 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(userSecurityProfile.getPolicyNo()).append("]");
 	 			}//end of else if(userSecurityProfile.getLoginType().equals("OCI"))
 			 
 			frmAddEnrollment= (DynaActionForm)FormUtils.setFormValues("frmAddEnrollment",memberDetailVO,
	 					this,mapping,request);
	 		frmAddEnrollment.set("memberAddressVO", FormUtils.setFormValues("frmMemberAddress",
	 					memberDetailVO.getMemberAddressVO(),this,mapping,request));
	 		 
	 		if(memberDetailVO.getEmpStatusTypeID().equals("POC"))
	 		{
	 			request.setAttribute("statusinfo","error.onlineforms.cancelled");
	 		}//end of if(memberDetailVO.getEmpStatusTypeID().equals("POC"))
 			ArrayList alLocation=new ArrayList();
 			alLocation = onlineAccessObject.getLocation(policyseqid);
			request.getSession().setAttribute("alLocation",alLocation);
			if(userSecurityProfile.getLoginType().equals("OCI"))
			{
				frmAddEnrollment.set("allowCityBankYN","Y");
			}//end of if(userSecurityProfile.getLoginType().equals("OCI"))
			else
			{
				frmAddEnrollment.set("allowCityBankYN","N");
			}//end of else
			//frmAddEnrollment.set("allowModiYN",TTKCommon.checkNull(memberVO.getAllowModiYN()));
 			 
 			frmAddEnrollment.set("caption",sbfCaption.toString());

 			//Added for IBM...1

			frmAddEnrollment.set("OPT",memberDetailVO.getStopOPtInYN());//Getting the checkbox values.
			frmAddEnrollment.set("windowsOPTYN",memberDetailVO.getwindowsOPTYN());//Getting windowsperiod
			frmAddEnrollment.set("chkpreauthclaims",memberDetailVO.getchkpreauthclaims());//Getting Pre-Auth/claim existence

			frmAddEnrollment.set("EmailPh",memberDetailVO.getValidEmailPhYN()); // Added for KOC-1216


			if(memberDetailVO.getchkpreauthclaims().equalsIgnoreCase("Y"))
			{
				request.setAttribute("chkPreAuth","message.chkPreAuthClaim");//Msg Display on Preauth-ADDED BY PRAVEEN
			}
			else if(!(memberDetailVO.getwindowsOPTYN().equalsIgnoreCase("Y")))
			{
				request.setAttribute("chkWindowsPeriod","message.chkWindowsPeriod");//Msg Display on WindowsPeriod-ADDED BY PRAVEEN
			}
			
			frmAddEnrollment.set("EmailId2",memberDetailVO.getMemberAddressVO().getEmailID2());
			frmAddEnrollment.set("MobileNo",memberDetailVO.getMemberAddressVO().getMobileNbr());
			frmAddEnrollment.set("MarriageHideYN",memberDetailVO.getMarriageHideYN());//koc for griavance
			

			//End of IBM....
 			request.getSession().setAttribute("frmAddEnrollment",frmAddEnrollment);
    		return this.getForward(strOnlineEmpInfo,mapping,request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request,mapping,expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request,mapping,new TTKException(exp,strEnrollment));
    	}//end of catch(Exception exp)
    }//end of doViewEmpDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse
    														
    /**
	 *
	 * This method is used to navigate to detail screen to add a record.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside AddEnrollmentAction doAdd");
			setOnlineLinks(request);
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
//            HospitalManager hospitalObject=this.getHospitalManagerObject();
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			String strSelectedRoot=request.getParameter("selectedroot");
			OnlineAccessPolicyVO onlinePolicyVO=null;
			StringBuffer sbfCaption=new StringBuffer();
			if(userSecurityProfile.getLoginType().equals("H"))
			{
				onlinePolicyVO = (OnlineAccessPolicyVO)request.getSession().getAttribute("SelectedPolicy");
			}//end of if(userSecurityProfile.getLoginType().equals("H"))
			sbfCaption=sbfCaption.append("Employee Details - Add - [").append(onlinePolicyVO.getPolicyNbr()).append("] [").
			append(userSecurityProfile.getGroupID()).append("] [").append(onlinePolicyVO.getGroupName()).append("]");
			MemberDetailVO memberDetailVO= new MemberDetailVO();		
			MemberAddressVO memberAddressVO=new MemberAddressVO();
			memberDetailVO.setMemberAddressVO(memberAddressVO);
			ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
			alAddEnrollment.add(memberDetailVO.getPolicyGroupSeqID());
			alAddEnrollment.add(onlinePolicyVO.getPolicySeqID());
			
			//Added for Hr Login
			alAddEnrollment.add(memberDetailVO.getMemberSeqID());
			memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);					
			Date dtPolicyEndDate=memberDetailVO.getPolicyEndDate();
			memberDetailVO.setDateOfExit(dtPolicyEndDate);
			Date dtPolicyStartDate=memberDetailVO.getPolicyStartDate();
			Long lnSumInsured =memberDetailVO.getSumInsured();
			String empStatusDesc =memberDetailVO.getEmpStatusDesc();			
			DynaActionForm frmAddEnrollment= (DynaActionForm)FormUtils.setFormValues("frmAddEnrollment",memberDetailVO,
					this,mapping,request);
			frmAddEnrollment.set("memberAddressVO", FormUtils.setFormValues("frmMemberAddress",
					memberDetailVO.getMemberAddressVO(),this,mapping,request));
			frmAddEnrollment.set("selectedRoot",strSelectedRoot);
			frmAddEnrollment.set("caption",sbfCaption.toString());
			//Added for Hr Login
			  DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			 String policyStartDate = df.format(dtPolicyStartDate);
			 String policyEndDate = df.format(dtPolicyEndDate);				
		            memberAddressVO.setStateCode("DOH");
		            HashMap hmCityList = null;    
		            ArrayList alCityLists = new ArrayList();
		            hmCityList=onlineAccessObject.getCityInfo("DOH");
		         	
		            String countryCode	=	(String)(hmCityList.get("CountryId"));
		            String isdcode	=	(String)(hmCityList.get("isdcode"));
		            String stdcode	=	(String)(hmCityList.get("stdcode"));
	          
		            memberDetailVO.setMemberAddressVO(memberAddressVO);

		            if(hmCityList!=null)
		            {
		                alCityLists = (ArrayList)hmCityList.get(memberDetailVO.getMemberAddressVO().getStateCode());
		            }//end of if(hmCityList!=null)
		            if(alCityLists==null)
		            {
		                alCityLists=new ArrayList();
		            }//end of if(alCityList==null)
		                      
		            memberAddressVO.setCountryCode(countryCode);
		            memberAddressVO.setOff1IsdCode(isdcode);		
		            memberAddressVO.setOff2IsdCode(isdcode);		
		            memberAddressVO.setHomeIsdCode(isdcode);	
		            memberAddressVO.setMobileIsdCode(isdcode);
		            
		            memberAddressVO.setOff1StdCode(stdcode);
		            memberAddressVO.setOff2StdCode(stdcode);
		            memberAddressVO.setHomeStdCode(stdcode);
		            request.getSession().setAttribute("providerStates",TTKCommon.getStates(memberDetailVO.getMemberAddressVO().getCountryCode()));   
		            request.getSession().setAttribute("providerAreas",TTKCommon.getAreas(memberDetailVO.getMemberAddressVO().getStateCode()));
			 		 
			 ArrayList alCityList = null;
				ArrayList alDistList = null;
				ArrayList	alBranchList=null;
				
			 if (alCityList == null) {
					alCityList = new ArrayList();
				}// end of if(alCityList==null)
			 if (alDistList == null) {
				 alDistList = new ArrayList();
				}// end of if(alCityList==null)
			 if (alBranchList == null) {
				 alBranchList = new ArrayList();
				}// end of if(alCityList==null)
			 
			ArrayList alLocation=new ArrayList();
			alLocation = onlineAccessObject.getLocation(onlinePolicyVO.getPolicySeqID());
			request.getSession().setAttribute("alLocation",alLocation);
			request.getSession().setAttribute("frmAddEnrollment",frmAddEnrollment);
		
			 frmAddEnrollment.set("alBranchList",alBranchList);
			 request.getSession().setAttribute("alBranchList",alBranchList);
			 
			 frmAddEnrollment.set("alDistList",alDistList);
			 request.getSession().setAttribute("alDistList",alDistList);
			 
			 frmAddEnrollment.set("alCityList",alCityList);
			 request.getSession().setAttribute("alCityList",alCityList); 		
			//Added for Hr Login
			 request.getSession().setAttribute("policyStartDate",policyStartDate);
			 request.getSession().setAttribute("policyEndDate",policyEndDate);
			 request.getSession().setAttribute("lnSumInsured",lnSumInsured);
			 request.getSession().setAttribute("empStatusDesc",empStatusDesc);
			 request.getSession().setAttribute("principalName","");
			return this.getForward(strOnlineEmpDetails,mapping,request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strEnrollment));
		}//end of catch(Exception exp)
	}//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to add/update the record.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
								HttpServletResponse response) throws Exception{
		try{

			log.debug("Inside AddEnrollmentAction doSave");
//			setOnlineLinks(request);
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			DynaActionForm frmAddEnrollment =(DynaActionForm)form;
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
 			int iSelectedRoot= TTKCommon.getInt(request.getParameter("selectedroot"));
			OnlineAccessPolicyVO onlinePolicyVO=null;
			String strUserType=null;
			FormFile formFile = null;
			String validateFlag=request.getParameter("validateFlag");
			
			int fileSize=2*1024*1024;
			String strNotify="";
			//for Individual login no privilege to save enrollment
			if(userSecurityProfile.getLoginType().equals("H"))
			{
				onlinePolicyVO = (OnlineAccessPolicyVO)request.getSession().getAttribute("SelectedPolicy");
				strUserType="HR";
			}//end of if(userSecurityProfile.getLoginType().equals("H"))
			else if(userSecurityProfile.getLoginType().equals("E"))
			{
				strUserType="EMP";
			}//end of else if(userSecurityProfile.getLoginType().equals("E"))
			else if(userSecurityProfile.getLoginType().equals("EMPL"))
			{
				strUserType="EMPL";
			}
			StringBuffer sbfCaption=new StringBuffer();
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
           // HospitalManager hospitalObject=this.getHospitalManagerObject();
			MemberDetailVO memberDetailVO = new MemberDetailVO();
			MemberAddressVO memberAddressVO = new MemberAddressVO();
			String strSelectedRoot=frmAddEnrollment.getString("selectedRoot");
			memberDetailVO=(MemberDetailVO)FormUtils.getFormValues(frmAddEnrollment,this,mapping,request);
			ActionForm frmMemberAddress=(ActionForm)frmAddEnrollment.get("memberAddressVO");
			memberAddressVO=(MemberAddressVO)FormUtils.getFormValues(frmMemberAddress,"frmMemberAddress",
					this,mapping,request);
			memberDetailVO.setMemberAddressVO(memberAddressVO);
			
			memberDetailVO.setEmailID2((String)frmAddEnrollment.get("EmailId2"));
		//	memberDetailVO.setMobileNo((String)frmAddEnrollment.get("MobileNo"));


			if(userSecurityProfile.getLoginType().equals("H"))
			{
				memberDetailVO.setPolicySeqID(onlinePolicyVO.getPolicySeqID());
				memberDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
				if(request.getSession().getAttribute("BankSeqIdForEmployeeAndHR")!=null){
                	memberDetailVO.setBankSeqID(Long.parseLong(String.valueOf(request.getSession().getAttribute("BankSeqIdForEmployeeAndHR"))));
                }
			}//end of if(userSecurityProfile.getLoginType().equals("H"))
			else if(userSecurityProfile.getLoginType().equals("E")||userSecurityProfile.getLoginType().equals("EMPL"))
			{
				memberDetailVO.setPolicySeqID(memberDetailVO.getPolicySeqID());
				if(request.getSession().getAttribute("BankSeqIdForEmployeeAndHR")!=null){
                	memberDetailVO.setBankSeqID(Long.parseLong(String.valueOf(request.getSession().getAttribute("BankSeqIdForEmployeeAndHR"))));
                }
//				memberDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
				memberDetailVO.setUpdatedBy(new Long(1));
			}//end of else if(userSecurityProfile.getLoginType().equals("E"))

			if(strSelectedRoot!=null&&!strSelectedRoot.equals(""))//on save from edit mode
			{
				//For Editing this particular seq id.
				memberDetailVO.setPolicyGroupSeqID(memberDetailVO.getPolicyGroupSeqID());
			}//end of if(!strSelectedRoot.equals(""))
			else
			{
				if(userSecurityProfile.getLoginType().equals("EMPL"))
					memberDetailVO.setPolicyGroupSeqID(memberDetailVO.getPolicyGroupSeqID());
				else
				memberDetailVO.setPolicyGroupSeqID(null);//adding new enrollment
			}//end of else

			//Added for IBM....2

			// added by Rekha 13.07.2012
			//if("Y".equals(TTKCommon.checkNull(frmAddEnrollment.get("OPT"))))
			if("N".equals(TTKCommon.checkNull(frmAddEnrollment.get("OPT"))))//Make checkbox enable or disable overwritten by Praveen Nov8.2012
			{
				//memberDetailVO.setStopOPtInYN("Y");
				memberDetailVO.setStopOPtInYN("N");//overwritten by Praveen Nov8.2012
			}
			else
			{
				//memberDetailVO.setStopOPtInYN("N");
				memberDetailVO.setStopOPtInYN("Y");//overwritten by Praveen Nov8.2012
			}
			// end added by Rekha 13.07.2012
			if("N".equals(TTKCommon.checkNull(frmAddEnrollment.get("EmailPh"))))//checked
			{
				memberDetailVO.setValidEmailPhYN("N");
			}
			else
			{
				memberDetailVO.setValidEmailPhYN("Y");//unchecked
			}
				
			formFile = (FormFile)frmAddEnrollment.get("file");
			String strFileExt=formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1,formFile.getFileName().length());
			if(!(strFileExt.equalsIgnoreCase(""))){
				if(((strFileExt.equalsIgnoreCase("jpg"))   || (strFileExt.equalsIgnoreCase("png"))) && (formFile.getFileSize()<=fileSize))
				{
					memberDetailVO.setJpgInputStream(formFile.getInputStream());	
				}
			else{
				throw new TTKException("error.hrLogin.file");
				}
		}else{	
			log.info("else of strFileExt");
		}			
			memberDetailVO.setAbbrCode(validateFlag);
			//Add Ended......
			long lCount=onlineAccessObject.saveEnrollment(memberDetailVO,strUserType,formFile);
			if(strSelectedRoot!=null&&strSelectedRoot.equals(""))// for initalizing the form on click of save button in add flow.
			{
				memberDetailVO= new MemberDetailVO();
				memberAddressVO=new MemberAddressVO();
				memberDetailVO.setMemberAddressVO(memberAddressVO);
				ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
				alAddEnrollment.add(memberDetailVO.getPolicyGroupSeqID());
				alAddEnrollment.add(onlinePolicyVO.getPolicySeqID());
				memberAddressVO.setStateCode("DOH");
	            HashMap hmCityList = null;    
	            ArrayList alCityList = new ArrayList();
	            hmCityList=onlineAccessObject.getCityInfo("DOH");
	         	
	            String countryCode	=	(String)(hmCityList.get("CountryId"));
	            String isdcode	=	(String)(hmCityList.get("isdcode"));
	            String stdcode	=	(String)(hmCityList.get("stdcode"));
	          
	            memberDetailVO.setMemberAddressVO(memberAddressVO);

	            if(hmCityList!=null)
	            {
	                alCityList = (ArrayList)hmCityList.get(memberDetailVO.getMemberAddressVO().getStateCode());
	            }//end of if(hmCityList!=null)
	            if(alCityList==null)
	            {
	                alCityList=new ArrayList();
	            }//end of if(alCityList==null)
	                      
	            memberAddressVO.setCountryCode(countryCode);
	            memberAddressVO.setOff1IsdCode(isdcode);		
	            memberAddressVO.setOff2IsdCode(isdcode);		
	            memberAddressVO.setHomeIsdCode(isdcode);	
	            memberAddressVO.setMobileIsdCode(isdcode);
	            
	            memberAddressVO.setOff1StdCode(stdcode);
	            memberAddressVO.setOff2StdCode(stdcode);
	            memberAddressVO.setHomeStdCode(stdcode);
	            request.getSession().setAttribute("providerStates",TTKCommon.getStates(memberDetailVO.getMemberAddressVO().getCountryCode())); 
				request.getSession().setAttribute("providerAreas",TTKCommon.getAreas(memberDetailVO.getMemberAddressVO().getStateCode()));
							
				alAddEnrollment.add(memberDetailVO.getMemberSeqID());
				memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);
				frmAddEnrollment= (DynaActionForm)FormUtils.setFormValues("frmAddEnrollment",memberDetailVO,
						this,mapping,request);
				frmAddEnrollment.set("memberAddressVO", FormUtils.setFormValues("frmMemberAddress",
						memberDetailVO.getMemberAddressVO(),this,mapping,request));
				request.setAttribute("updated","message.addedSuccessfully");
				sbfCaption=sbfCaption.append("Employee Details - Add - [").append(onlinePolicyVO.getPolicyNbr()).append("] [").
				append(userSecurityProfile.getGroupID()).append("] [").append(onlinePolicyVO.getGroupName()).append("]");
				
			}//end of if(strSelectedRoot.equals(""))
			// Added Rule related validations as per Sreeraj instructions on 27/02/2008
			if(lCount <0){
					//log.info("Errors are there..........");
					RuleManager ruleManager=this.getRuleManagerObject();
					//RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();
					ArrayList alValidationError=ruleManager.getValidationErrorList(new Long(lCount));

					//prepare Error messages
					//ArrayList alErrorMessage=ruleXMLHelper.prepareErrorMessage(alValidationError,request);
					request.setAttribute("BUSINESS_ERRORS",alValidationError);
					if(userSecurityProfile.getLoginType().equals("EMPL")){
						DynaActionForm frmMember =(DynaActionForm)form;
						request.setAttribute("frmMember", frmMember);
						/*return this.getForward("saveOnlineNewEmpDetails", mapping, request);*/
						return mapping.findForward("saveOnlineNewEmpDetails");
					}else
					return this.getForward(strSaveOnlineEmpDetails, mapping, request);
			}//end of addition

			if(lCount>0)
			{
				if(strSelectedRoot!=null&&!strSelectedRoot.equals(""))//on save from edit mode
				{
					request.setAttribute("updated","message.savedSuccessfully");
					if(userSecurityProfile.getLoginType().equals("H"))
	 	 			{
						ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
						alAddEnrollment.add(memberDetailVO.getPolicyGroupSeqID());
						alAddEnrollment.add(onlinePolicyVO.getPolicySeqID());
						alAddEnrollment.add(memberDetailVO.getMemberSeqID());
						memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);			
						request.getSession().setAttribute("alImg",memberDetailVO.getImageData());
						request.getSession().setAttribute("BankSeqIdForEmployeeAndHR", memberDetailVO.getBankSeqID());
	 					//onlinePolicyVO = (OnlineAccessPolicyVO)request.getSession().getAttribute("SelectedPolicy");
	 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(onlinePolicyVO.getPolicyNbr()).append("] [").
	 	 				append(userSecurityProfile.getGroupID()).append("] [").append(onlinePolicyVO.getGroupName()).append("]");
	 	 			}//end of if(userSecurityProfile.getLoginType().equals("H"))
	 	 			else if(userSecurityProfile.getLoginType().equals("E") ||userSecurityProfile.getLoginType().equals("EMPL"))
	 	 			{
	 	 				TreeData treeData =(TreeData)request.getSession().getAttribute("treeData") ;
	 	 				MemberVO memberVO=(MemberVO)treeData.getRootData().get(iSelectedRoot);
	 	 				ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
						alAddEnrollment.add(memberDetailVO.getPolicyGroupSeqID());
						alAddEnrollment.add(memberDetailVO.getPolicySeqID());
						alAddEnrollment.add(memberDetailVO.getMemberSeqID());
						memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);
	 	 				memberDetailVO.setPolicySeqID(memberDetailVO.getPolicySeqID());
	 	 				memberDetailVO.setPolicyGroupSeqID(memberDetailVO.getPolicyGroupSeqID());
	 	 				memberDetailVO.setMemberSeqID(memberDetailVO.getMemberSeqID());
	 	 				request.getSession().setAttribute("BankSeqIdForEmployeeAndHR", memberDetailVO.getBankSeqID());
	 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(userSecurityProfile.getPolicyNo()).append("] [").
	 					append(userSecurityProfile.getGroupID()).append("] [").append(memberVO.getGroupName()).append("]");
	 	 			}//end of else if(userSecurityProfile.getLoginType().equals("E"))
				}//end of if(!strSelectedRoot.equals(""))
				else{
					if(userSecurityProfile.getLoginType().equals("EMPL")){
						TableData employeeDataTable=(TableData)request.getSession().getAttribute("employeeDataTable");
						MemberVO memberVO=(MemberVO)employeeDataTable.getRowInfo(TTKCommon.getInt(request.getParameter("rownum")));
	 	 				sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(userSecurityProfile.getPolicyNo()).append("] [").
	 					append(userSecurityProfile.getGroupID()).append("] [").append(memberVO.getGroupName()).append("]");
						request.setAttribute("updated","message.savedSuccessfully");
					}
				}
				
			}//end of if(iCount>0)			
			ArrayList alLocation=new ArrayList();
			if(userSecurityProfile.getLoginType().equals("H"))
			{
				alLocation = onlineAccessObject.getLocation(onlinePolicyVO.getPolicySeqID());
			}//end of if(userSecurityProfile.getLoginType().equals("H"))
			else if(userSecurityProfile.getLoginType().equals("E")||userSecurityProfile.getLoginType().equals("EMPL"))
			{
				alLocation = onlineAccessObject.getLocation(memberDetailVO.getPolicySeqID());
			}//end of else if(userSecurityProfile.getLoginType().equals("E"))
			request.getSession().setAttribute("alLocation",alLocation);
			frmAddEnrollment.set("caption",sbfCaption.toString());
			request.getSession().setAttribute("frmAddEnrollment",frmAddEnrollment);
			request.getSession().setAttribute("principalName",memberDetailVO.getInsuredName());
			request.setAttribute("notify",strNotify);
			if(userSecurityProfile.getLoginType().equals("EMPL")){
				DynaActionForm frmMember =(DynaActionForm)form;
				request.setAttribute("frmMember", frmMember);
				/*return this.getForward("saveOnlineNewEmpDetails", mapping, request);*/
				return mapping.findForward("saveOnlineNewEmpDetails");
			}else
			return this.getForward(strSaveOnlineEmpDetails, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request,mapping,expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request,mapping,new TTKException(exp,strEnrollment));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to add/update the record.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doSend(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doSend method of WebConfigAction");
			setOnlineLinks(request);
//			DynaActionForm frmAddEnrollment =(DynaActionForm)request.getSession().getAttribute("frmAddEnrollment");
			TreeData treeData =(TreeData)request.getSession().getAttribute("treeData") ;
 			int iSelectedRoot= TTKCommon.getInt(request.getParameter("selectedroot"));
			MemberVO memberVO=(MemberVO)treeData.getRootData().get(iSelectedRoot);
			MemberDetailVO memberDetailVO = new MemberDetailVO();
			ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
    		UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			alAddEnrollment.add(memberVO.getPolicyGroupSeqID());
			alAddEnrollment.add(memberVO.getPolicySeqID());
			
			
			
			
			
			alAddEnrollment.add(memberDetailVO.getMemberSeqID());
			memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);
			Long lngUserID = null;
			if(userSecurityProfile.getLoginType().equals("H"))
	 		{
				lngUserID = TTKCommon.getUserSeqId(request);
	 		}//end of if(userSecurityProfile.getLoginType().equals("H"))
			else
	 		{
	 			//lngUserID = TTKCommon.getLong(userSecurityProfile.getUSER_ID());
	 			lngUserID =new Long(1);
	 		}//end of else
			log.debug("memberVO tree datat ***************:"+memberVO.getPolicyGroupSeqID());
			String strOnlineEnrollment ="NEW_ONLINE_ENR_EMPLOYEE";
			String strEmailID = memberDetailVO.getMemberAddressVO().getEmailID();
			log.debug("email id is *(**********"+strEmailID);
			if(strEmailID==null || strEmailID=="")
			{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.weblogin.emailrequired");
				throw expTTK;
			}//end of if(CodingWebBoardHelper.checkWebBoardId(request)==null)

			Long lngPolicyGrpSeqID = Long.valueOf(memberVO.getPolicyGroupSeqID());

//			WebConfigInfoVO webConfigInfoVO = new WebConfigInfoVO();
			CommunicationManager communicationManager = this.getCommunicationManagerObject();
			/*log.info("lngPolicyGrpSeqID is :"+lngPolicyGrpSeqID);
			log.info("strOnlineEnrollment is :"+strOnlineEnrollment);
			log.info("User seq ID is :"+TTKCommon.getUserSeqId(request));*/

			communicationManager.sendOnlineSoftCopy(strOnlineEnrollment,lngPolicyGrpSeqID,lngUserID);
			request.setAttribute("updated","message.sendSuccessfully");
			return this.getForward(strSendOnlineEmpDetails, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strEnrollment));
		}//end of catch(Exception exp)
	}//end of doSend(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to reload the screen when the reset button is pressed.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside doReset of AddEnrollmentAction");
			setOnlineLinks(request);
			DynaActionForm frmAddEnrollment=(DynaActionForm)form;
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			OnlineAccessPolicyVO onlinePolicyVO=null;
			if(userSecurityProfile.getLoginType().equals("H"))
			{
				onlinePolicyVO = (OnlineAccessPolicyVO)request.getSession().getAttribute("SelectedPolicy");
			}//end of if(userSecurityProfile.getLoginType().equals("H"))
			StringBuffer sbfCaption=new StringBuffer();
			String strSelectedRoot=frmAddEnrollment.getString("selectedRoot");
			MemberDetailVO memberDetailVO= new MemberDetailVO();
			MemberAddressVO memberAddressVO=new MemberAddressVO();
			memberDetailVO.setMemberAddressVO(memberAddressVO);
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
			TreeData treeData =(TreeData)request.getSession().getAttribute("treeData") ;
			ArrayList<Object> alAddEnrollment=new ArrayList<Object>();
			if(strSelectedRoot!=null&&!strSelectedRoot.equals(""))// on clicking the edit link
			{
				MemberVO memberVO=(MemberVO)treeData.getRootData().get(TTKCommon.getInt(strSelectedRoot));
				alAddEnrollment.add(memberVO.getPolicyGroupSeqID());
				alAddEnrollment.add(memberVO.getPolicySeqID());
				
				
				
				
				alAddEnrollment.add(memberDetailVO.getMemberSeqID());
				memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);

				if(userSecurityProfile.getLoginType().equals("H")){
					sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(onlinePolicyVO.getPolicyNbr()).append("] [").
					append(userSecurityProfile.getGroupID()).append("] [").append(onlinePolicyVO.getGroupName()).append("]");
				}//end of if(userSecurityProfile.getLoginType().equals("H"))
				else{
					sbfCaption=sbfCaption.append("Employee Details - Edit - [").append(userSecurityProfile.getPolicyNo()).append("] [").
					append(userSecurityProfile.getGroupID()).append("] [").append(memberVO.getGroupName()).append("]");
				}//end of else
			}//end of if(!strSelectedRoot.equals(""))
			else
			{
				alAddEnrollment.add(memberDetailVO.getPolicyGroupSeqID());
				alAddEnrollment.add(onlinePolicyVO.getPolicySeqID());
				alAddEnrollment.add(memberDetailVO.getMemberSeqID());
				memberDetailVO=onlineAccessObject.selectEnrollment(alAddEnrollment);
				sbfCaption=sbfCaption.append("Employee Details - Add - [").append(onlinePolicyVO.getPolicyNbr()).append("] [").
				append(userSecurityProfile.getGroupID()).append("] [").append(onlinePolicyVO.getGroupName()).append("]");
			}//end of else
			frmAddEnrollment= (DynaActionForm)FormUtils.setFormValues("frmAddEnrollment",memberDetailVO,
					this,mapping,request);
			frmAddEnrollment.set("memberAddressVO", FormUtils.setFormValues("frmMemberAddress",
					memberDetailVO.getMemberAddressVO(),this,mapping,request));
			frmAddEnrollment.set("selectedRoot",strSelectedRoot);
			frmAddEnrollment.set("caption",sbfCaption.toString());
			request.getSession().setAttribute("frmAddEnrollment",frmAddEnrollment);
			if(userSecurityProfile.getLoginType().equals("EMPL"))
				return this.getForward("onlineEmplEmpDetails",mapping,request);
			else
			return this.getForward(strOnlineEmpDetails,mapping,request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strEnrollment));
		}//end of catch(Exception exp)
	}//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to close the current page and return to previous page.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    								HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);
    		log.debug("Inside doClose of AddEnrollmentAction");
    		return mapping.findForward(strClose);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strEnrollment));
		}//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmMember,HttpServletRequest request,String strLoginType)
    {
   	 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)
   	 request.getSession().getAttribute("UserSecurityProfile");
   	 OnlineAccessPolicyVO onlinePolicyVO = null;
   	 //build the column names along with their values to be searched
   	 ArrayList<Object> alSearchParams = new ArrayList<Object>();
   	 if(userSecurityProfile.getLoginType().equals("E"))  //for corporate login
   	 {
   		 alSearchParams.add(userSecurityProfile.getUSER_ID());
			 //alSearchParams.add("120100/05092011");
   		 alSearchParams.add(userSecurityProfile.getPolicyNo());
			 alSearchParams.add(userSecurityProfile.getGroupID());
   		 alSearchParams.add(null);
   		 alSearchParams.add(userSecurityProfile.getLoginType());
   		 alSearchParams.add(TTKCommon.getUserSeqId(request));
   		 alSearchParams.add(null);
   	 }//end of if(userSecurityProfile.getLoginType().equals("E"))
   	 return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchBankAccount)
    /**
	 * Returns the OnlineAccessManager session object for invoking methods on it.
	 * @return OnlineAccessManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
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
			throw new TTKException(exp, strEnrollment);
		}//end of catch
		return onlineAccessManager;
	}//end getOnlineAccessManager()

	 // Added the following piece of code for integrating Rule related validation
    // as per Sreeraj's instruction
	/**
     * Returns the RuleManager session object for invoking methods on it.
     * @return RuleManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private RuleManager getRuleManagerObject() throws TTKException
    {
        RuleManager ruleManager = null;
        try
        {
            if(ruleManager == null)
            {
                InitialContext ctx = new InitialContext();
                ruleManager = (RuleManager) ctx.lookup("java:global/TTKServices/business.ejb3/RuleManagerBean!com.ttk.business.administration.RuleManager");
                log.debug("Inside RuleManager: RuleManager: " + ruleManager);
            }//end if(ruleManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "memberdetail");
        }//end of catch
        return ruleManager;
    }//end of getRuleManagerObject()

    /**
	 * Returns the CommunicationManager session object for invoking methods on it.
	 * @return CommunicationManager session object which can be used for method invocation
	 * @exception throws TTKException
	 */
	private CommunicationManager getCommunicationManagerObject() throws TTKException
	{
		CommunicationManager communicationManager = null;
		try
		{
			if(communicationManager == null)
			{
				InitialContext ctx = new InitialContext();
				communicationManager = (CommunicationManager) ctx.lookup("java:global/TTKServices/business.ejb3/CommunicationManagerBean!com.ttk.business.common.messageservices.CommunicationManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "memberdetail");
		}//end of catch
		return communicationManager;
	}//end of getCommunicationManagerObject()	
		
	
	/**
	 * This method is used to load cities based on the selected state. Finally
	 * it forwards to the appropriate view based on the specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	//doChangeBank
	//doChangeState
	public ActionForward doChangeBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			//	setLinks(request);
			//checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AccountsAction");
			String focusID	=	request.getParameter("focusID");
			DynaActionForm accountForm = (DynaActionForm) form;
			AccountDetailVO accountDetailVO = null;
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;
			DynaActionForm frmAddEnrollment = (DynaActionForm) form;
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
						
			hmCityList = onlineAccessObject.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmAddEnrollment.get("bankName")));
				
			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			frmAddEnrollment.set("frmChanged", "changed");
			frmAddEnrollment.set("alCityList", alCityList);
			
			//get the bank code based on bank code
			String bankCode	=		onlineAccessObject.getBankCode((String)TTKCommon.checkNull(frmAddEnrollment.get("bankName")));
			
			request.getSession().setAttribute("alCityList", alCityList);
			
			ArrayList alDistList = new ArrayList();
			request.getSession().setAttribute("alDistList", alDistList);
			
			ArrayList alBranchList = new ArrayList();
			request.getSession().setAttribute("alBranchList", alBranchList);
			
			frmAddEnrollment.set("neft", bankCode);
			request.setAttribute("focusID", focusID);
			// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
			  return mapping.findForward("getSwiftcodeFromBranchs");
		//	return this.getForward(strSaveOnlineEmpDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strEnrollment));
		}// end of catch(Exception exp)
	}// end of doChangeState(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,HttpServletResponse response)
    
    
    /**
	 * This method is used to load cities based on the selected state. Finally
	 * it forwards to the appropriate view based on the specified forward mappings
	 * @param mapping  The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	//doChangeState
	//doChangeDistict
	public ActionForward doChangeState(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			//setLinks(request);
		//	checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AccountsAction");
			String focusID	=	request.getParameter("focusID");
			DynaActionForm accountForm = (DynaActionForm) form;
			AccountDetailVO accountDetailVO = null;
			HashMap hmDistList = null;
			ArrayList alDistList = null;
			ArrayList alCityCode = null;
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
			DynaActionForm frmAddEnrollment = (DynaActionForm) form;
			String bankState = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankState"));
			String bankName = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankName"));
			
			OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();			
			hmDistList = onlineAccessObject.getBankCityInfo(bankState,bankName);
			if (hmDistList != null) {
				
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmAddEnrollment.get("bankState")));
				
			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			frmAddEnrollment.set("frmChanged", "changed");
			frmAddEnrollment.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
			request.setAttribute("focusID", focusID);
			if("EMPL".equals(userSecurityProfile.getLoginType()))
				return mapping.findForward("onlineEmplEmpDetails");
			else
			return this.getForward(strSaveOnlineEmpDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strEnrollment));
		}// end of catch(Exception exp)
	}// end of doChangeState(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,HttpServletResponse response)   
    
	//doChangeCity
		//doChangeBranch
		public ActionForward doChangeCity(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			try {
				//setLinks(request);
				//checkWebboardVisabulity(request);
				log.debug("Inside doChangeState method of AddEnrollmentAction");
				String focusID	=	request.getParameter("focusID");
				DynaActionForm accountForm = (DynaActionForm) form;
				AccountDetailVO accountDetailVO = null;
				HashMap hmBranchList = null;
				ArrayList alBranchList = null;
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
				// ArrayList alCityCode = null;
				DynaActionForm frmAddEnrollment = (DynaActionForm) form;
				String bankState = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankState"));
				String bankName = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankName"));
				String bankDistict = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankcity"));
				
				OnlineAccessManager onlineAccessObject=this.getOnlineAccessManager();
				hmBranchList = onlineAccessObject.getBankBranchtInfo(bankState,bankName, bankDistict);
				if (hmBranchList != null) {
					alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmAddEnrollment.get("bankcity")));
					
				}// end of if(hmCityList!=null)
				if (alBranchList == null) {
					alBranchList = new ArrayList();
				}// end of if(alCityList==null)
				frmAddEnrollment.set("frmChanged", "changed");
				frmAddEnrollment.set("alBranchList", alBranchList);
				request.getSession().setAttribute("alBranchList", alBranchList);
				// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
				request.setAttribute("focusID", focusID);
				if("EMPL".equals(userSecurityProfile.getLoginType())){
					return mapping.findForward("onlineEmplEmpDetails");
				}else
				return this.getForward(strSaveOnlineEmpDetails, mapping, request);
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strEnrollment));
			}// end of catch(Exception exp)
		}// end of doChangeState(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
		/**
		 * This method is used to load cities based on the selected state. Finally
		 * it forwards to the appropriate view based on the specified forward mappings
		 * @param mapping The ActionMapping used to select this instance
		 * @param form The optional ActionForm bean for this request (if any)
		 * @param request The HTTP request we are processing
		 * @param response The HTTP response we are creating
		 * @return ActionForward Where the control will be forwarded, after this request is processed
		 * @throws Exception if any error occurs
		 */
		//doChangeBranch
		//doChangeIfsc
		public ActionForward doChangeBranch(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			try {
				//setLinks(request);
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
				String bankName = (String) TTKCommon.checkNull(frmAddEnrollment.get("bankName"));
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
				if("EMPL".equals(userSecurityProfile.getLoginType()))
					return mapping.findForward("onlineEmplEmpDetails");
				else
				return this.getForward(strSaveOnlineEmpDetails, mapping, request);
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strEnrollment));
			}// end of catch(Exception exp)
		}// end of doChangeState(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)	
		
		public ActionForward doChangeQatarYN(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			try {
//				setOnlineLinks(request);
	    		log.info("Inside doChangeQatarYN method of AddEnrollmentAction");
				DynaActionForm accountForm = (DynaActionForm)form;
				HttpSession session = request.getSession();
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile"));
				String qatarYN = accountForm.getString("bankAccountQatarYN");
				MemberAddressVO memberAddressVO= new MemberAddressVO();
				accountForm.set("bankAccountQatarYN", qatarYN);
				accountForm.set("bankName", "");
				accountForm.set("bankState", "");
				accountForm.set("bankcity", "");
				accountForm.set("bankBranchText", "");
				accountForm.set("bankBranch", "");
				accountForm.set("ifsc", "");
				accountForm.set("neft", "");
				accountForm.set("micr", "");
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
				if("EMPL".equals(userSecurityProfile.getLoginType()))
				return mapping.findForward("onlineEmplEmpDetails");
				else
					return this.getForward(strSaveOnlineEmpDetails, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processOnlineExceptions(request,mapping,expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processOnlineExceptions(request,mapping,new TTKException(exp,strEnrollment));
			}//end of catch(Exception exp)
	    }//end doChangeQatarYN
		
		
		}//end of AddEnrollmentAction()
