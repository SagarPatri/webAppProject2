/**
 * @ (#) ContactAction.java Jan 14, 2006
 * Project       : TTK HealthCare Services
 * File          : ContactAction.java
 * Author        : Srikanth H M
 * Company       : Span Systems Corporation
 * Date Created  : Jan 14, 2006
 *
 * @author       : Srikanth H M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.usermanagement;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.quartz.JobExecutionContext;

import sun.misc.BASE64Decoder;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.common.messageservices.CommunicationManager;
import com.ttk.business.empanelment.ContactManager;
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.business.usermanagement.UserManager;
import com.ttk.common.EmailSchedulerPreauth;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.usermanagement.AdditionalInfoVO;
import com.ttk.dto.usermanagement.ContactVO;
import com.ttk.dto.usermanagement.PersonalInfoVO;
import com.ttk.dto.usermanagement.SMSEmailTriggerInfoVO;
import com.ttk.dto.usermanagement.UserAccessVO;
import com.ttk.dto.usermanagement.UserListVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;
import com.ttk.dto.empanelment.BrokerVO;
import com.ttk.dto.empanelment.HospitalDetailVO;
import com.ttk.business.empanelment.PartnerManager;
import com.ttk.dto.empanelment.PartnerDetailVO;

import formdef.plugin.util.FormUtils;

/**
 * This class is reusable for adding contacts in Hospital, Insurance, Group Registration and Banks in
 * Empanlement and Finance flows. Adding User in the User Management flow.
 * This class also provides option for reseting the Password.
 */

public class ContactAction  extends TTKAction  {

	private static Logger log = Logger.getLogger( ContactAction.class );

	//declare forward paths
	private static final String strHospitalContactPath="hospitalcontact";
	private static final String strInsuranceContactPath="insurancecontact";
	private static final String strBrokerContactPath="brokercontact";
	private static final String strAdminContactPath="usercontact";
	private static final String strGroupRegistrationContact="groupregistrationcontact";
	private static final String strAdminContactClose="userlist";
	private static final String strHospitalContactClose="hospitalcontactlist";
	private static final String strInsuranceContactClose="insurancecontactlist";
	private static final String strGroupRegistrationContactClose="groupregistrationcontactlist";
	private static final String strBankClose="bankcontactlist";
	private static final String strBankcontact="bankcontact";
	private static final String strFailure="failure";
	//	declrations of modes
	private static final String strClose="Close";
	private static final String strUserManagement="User Management";
	private static final String strHospital="Hospital";
	private static final String strInsurance="Insurance";
	private static final String strGroupRegistration="Group Registration";
	//added for broker login kocb
	private static final String strBroker="Broker";
	private static final String strBrokerContactClose="brokercontactlist";
	private static final String strBanks="Banks";
	private static final String strDelete="Delete";
	private static final String strEmployee="EmployeeUser";
	private static final String strEmployeecontact="employeeContact";
	
	private static final String strPartnerContactPath="partnercontact";
	private static final String strPartnerContactClose="partnercontactlist";
	private static final String strPartner="Partner";


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
	public ActionForward doViewContacts(ActionMapping mapping,ActionForm form,HttpServletRequest request,
											HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ContactAction doViewContacts");
			setLinks(request);
			String strContactPath="";
			strContactPath=getForwardPath(request);
			com.ttk.dto.empanelment.ContactVO contactVO=null;
			ContactVO userContactVO=null;
			UserListVO userListVO =null;
			StringBuffer strCaption=new StringBuffer();
			TableData tableData =TTKCommon.getTableData(request);
			UserManager userContactObject = this.getUserManagerObject();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			strCaption=buildCaption(request);
			DynaActionForm generalForm = (DynaActionForm)form;
			
			
			String userType = (String) request.getSession().getAttribute("sUserList");
			
			DynaActionForm frmSearchContact=(DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))//Edit flow
			{
				if(strActiveSubLink.equals(strUserManagement))
				{
				  DynaActionForm frmUserLists=(DynaActionForm)request.getSession().getAttribute("frmUserList");
				  userListVO = (UserListVO)tableData.getRowInfo(Integer.parseInt((String)(generalForm).get("rownum")));
				  frmUserLists.set("activeYN",userListVO.getActive_Yn());
				  frmUserLists.set("userAccessYn",userListVO.getAccessYn());
				  
				  if("EMP".equals(userType)){
					  userContactVO=userContactObject.getEmployeeContact(userListVO.getPolicyGroupSeqId(),userType);
					  
					  
					  
				  }
				  else{
					  userContactVO=userContactObject.getContact(userListVO.getContactSeqId());
				  }
				  
				  
				}//end of if(strActiveSubLink.equals("User Management"))
				if(strActiveSubLink.equals(strHospital)||strActiveSubLink.equals(strInsurance) ||strActiveSubLink.equals(strBroker)
						||strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strBanks) ||strActiveSubLink.equals(strPartner))
				{
					contactVO = (com.ttk.dto.empanelment.ContactVO)tableData.getRowInfo(
																Integer.parseInt((String)(generalForm).get("rownum")));
					frmSearchContact.set("activeYN",contactVO.getActiveYn());
					frmSearchContact.set("userAccessYn",contactVO.getUserAccessYn());
					userContactVO=userContactObject.getContact(contactVO.getContactSeqId());
				}//end of if(strActiveSubLink.equals(strHospital)||strActiveSubLink.equals(strInsurance)
					//||strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strBanks))
				if(strActiveSubLink.equals(strPartner))
					if(!(request.getParameter("contactType")==null||request.getParameter("contactType").trim().equals("")))
					userContactVO.getUserAccessInfo().setContactType(request.getParameter("contactType"));
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			DynaActionForm frmUserContact = (DynaActionForm)FormUtils.setFormValues("frmUserContact", userContactVO,
																		this, mapping, request);
			
			frmUserContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
												userContactVO.getPersonalInfo(),this,mapping,request));
			frmUserContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
												userContactVO.getAdditionalInfo(),this,mapping,request));
			frmUserContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
												userContactVO.getUserAccessInfo(),this,mapping,request));
			
			frmUserContact.set("smsEmailTriggerInfoVO",FormUtils.setFormValues("frmSMSEmailTriggerInfo",
					userContactVO.getSmsEmailTriggerInfo(),this,mapping,request));
			
			if(strActiveSubLink.equals(strUserManagement)){
			frmUserContact.set("userId", userListVO.getUserID());
			frmUserContact.set("userType", userType);
			frmUserContact.set("policyGroupSeqId", userListVO.getPolicyGroupSeqId());
			frmUserContact.set("hddn_accn_locked_YN", userContactVO.getAccn_locked_YN());
			}
			
			
			
			
			
			
			
			
			//appending the caption
			strCaption.append("Edit ");
			if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
				strActiveSubLink.equals(strBanks) || strActiveSubLink.equals(strBroker))
			{
				strCaption.append((String)frmSearchContact.getString("caption"));
			}//end of if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
				//strActiveSubLink.equals(strBanks))
			if(strActiveSubLink.equals(strHospital))
			{
				strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			}//end of if(strActiveSubLink.equals(strHospital))
			if(strActiveSubLink.equals(strPartner))
			{
				strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			}//end of if(strActiveSubLink.equals(strPartner))
			frmUserContact.set("caption",strCaption.toString());
			
			
			
			
			if("EMP".equals(userType))
			{
				strContactPath=strEmployeecontact;
				
				request.getSession().setAttribute("frmEmployeeContact",frmUserContact);
			}
			else{
				DynaActionForm personalInfoForm=(DynaActionForm)frmUserContact.get("personalInfoVO");
				String straccn_locked_YN=(String)personalInfoForm.getString("accn_locked_YN");
				if("".equals(straccn_locked_YN) ||"N".equals(straccn_locked_YN))
				{
					straccn_locked_YN="NO";
					frmUserContact.set("accLockYN",straccn_locked_YN);
				}
				else
				{	straccn_locked_YN="YES";
					frmUserContact.set("accLockYN", straccn_locked_YN);
				}
				
				request.getSession().setAttribute("frmUserContact",frmUserContact);
			}
												
			return this.getForward(strContactPath, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doViewContacts(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)

	
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
	public ActionForward doViewProfessionals(ActionMapping mapping,ActionForm form,HttpServletRequest request,
											HttpServletResponse response) throws Exception{
		try{
			String temp	=	(String)request.getParameter("rownum");
			int rownum	=	Integer.parseInt(temp);
			log.debug("Inside ContactAction doViewProfessionals");
			setLinks(request);
			String strContactPath="";
			strContactPath=getForwardPath(request);
			com.ttk.dto.empanelment.ContactVO contactVO=null;
			ContactVO userContactVO=null;
			UserListVO userListVO =null;
			StringBuffer strCaption=new StringBuffer();
			TableData tableData =TTKCommon.getTableData(request);
			UserManager userContactObject = this.getUserManagerObject();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			strCaption.append("Professional Details -");
			DynaActionForm generalForm = (DynaActionForm)form;
			DynaActionForm frmSearchContact=(DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))//Edit flow
			{
				if(strActiveSubLink.equals(strUserManagement))
				{
				  DynaActionForm frmUserLists=(DynaActionForm)request.getSession().getAttribute("frmUserList");
				  userListVO = (UserListVO)tableData.getRowInfo(Integer.parseInt((String)(generalForm).get("rownum")));
				  frmUserLists.set("activeYN",userListVO.getActive_Yn());
				  frmUserLists.set("userAccessYn",userListVO.getAccessYn());
				  userContactVO=userContactObject.getContact(userListVO.getContactSeqId());
				}//end of if(strActiveSubLink.equals("User Management"))
				if(strActiveSubLink.equals(strHospital)||strActiveSubLink.equals(strInsurance) ||strActiveSubLink.equals(strBroker)
						||strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strBanks)||strActiveSubLink.equals(strPartner))
				{
					contactVO = (com.ttk.dto.empanelment.ContactVO)tableData.getRowInfo(rownum);
					frmSearchContact.set("activeYN","Y");
					frmSearchContact.set("userAccessYn","Y");
					userContactVO=userContactObject.getContact(contactVO.getContactSeqId());
				}//end of if(strActiveSubLink.equals(strHospital)||strActiveSubLink.equals(strInsurance)
					//||strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strBanks))
				if(strActiveSubLink.equals(strHospital))
				{
					contactVO = (com.ttk.dto.empanelment.ContactVO)tableData.getRowInfo(rownum);
					frmSearchContact.set("activeYN","Y");
					frmSearchContact.set("userAccessYn","Y");
					userContactVO=userContactObject.getProfessionalContact(contactVO.getContactSeqId());
				}//end of if(strActiveSubLink.equals(strHospital))
				if(strActiveSubLink.equals(strPartner))
				{
					contactVO = (com.ttk.dto.empanelment.ContactVO)tableData.getRowInfo(rownum);
					frmSearchContact.set("activeYN","Y");
					frmSearchContact.set("userAccessYn","Y");
					userContactVO=userContactObject.getProfessionalContact(contactVO.getContactSeqId());
				}//end of if(strActiveSubLink.equals(strPartner))
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			DynaActionForm frmProfessionalContact = (DynaActionForm)FormUtils.setFormValues("frmProfessionalContact", userContactVO,
																		this, mapping, request);

			frmProfessionalContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
												userContactVO.getPersonalInfo(),this,mapping,request));
			frmProfessionalContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
												userContactVO.getAdditionalInfo(),this,mapping,request));
			frmProfessionalContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
												userContactVO.getUserAccessInfo(),this,mapping,request));
			
			frmProfessionalContact.set("startDate", TTKCommon.getFormattedDate(userContactVO.getPersonalInfo().getStartDate()));
			frmProfessionalContact.set("endDate",TTKCommon.getFormattedDate(userContactVO.getPersonalInfo().getEndDate()));
			
			//appending the caption
			strCaption.append("Edit ");
			if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
				strActiveSubLink.equals(strBanks) || strActiveSubLink.equals(strBroker))
			{
				strCaption.append((String)frmSearchContact.getString("caption"));
			}//end of if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
				//strActiveSubLink.equals(strBanks))
			if(strActiveSubLink.equals(strHospital))
			{
				strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			}//end of if(strActiveSubLink.equals(strHospital))
			if(strActiveSubLink.equals(strPartner))
			{
				strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			}//end of if(strActiveSubLink.equals(strPartner))
			frmProfessionalContact.set("caption",strCaption.toString());
			request.getSession().setAttribute("frmProfessionalContact",frmProfessionalContact);
			//return this.getForward("hospProfessionContacts", mapping, request);
			return mapping.findForward("hospProfessionContacts");
			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doViewProfessionals(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)

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
			log.debug("Inside ContactAction doAdd");
			setLinks(request);
			String strContactPath="";
			strContactPath=getForwardPath(request);
			ContactVO userContactVO=null;
			StringBuffer strCaption=new StringBuffer();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			strCaption=buildCaption(request);
			DynaActionForm generalForm = (DynaActionForm)form;
			DynaActionForm frmSearchContact=(DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			userContactVO=new ContactVO();
			generalForm.initialize(mapping);
			PersonalInfoVO personalInfoVO=new PersonalInfoVO();
			AdditionalInfoVO additionalInfoVO=new AdditionalInfoVO();
			UserAccessVO userAccessVO=new UserAccessVO();
			SMSEmailTriggerInfoVO smsEmailTriggerInfoVO = new SMSEmailTriggerInfoVO();
			UserManager userContactObject = this.getUserManagerObject();
			
			if(!strActiveSubLink.equals(strUserManagement) && !strActiveSubLink.equals("Insurance") && !strActiveSubLink.equals("Group Registration") && !strActiveSubLink.equals("Broker")  && !strActiveSubLink.equals("Banks") && !strActiveSubLink.equals("Partner"))
			{		
				ArrayList alStdIsd	=	new ArrayList();
				Long lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
				alStdIsd	=	userContactObject.getStdIsd(lngHospitalSeqId,"");	
		        personalInfoVO.setStdCode((String) alStdIsd.get(0));
				personalInfoVO.setIsdCode((String) alStdIsd.get(1));
			}

			if(strActiveSubLink.equals(strPartner))
			{		
				ArrayList alStdIsd	=	new ArrayList();
				Long lngPartnerSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
				alStdIsd	=	userContactObject.getStdIsd1(lngPartnerSeqId,"");	
		        personalInfoVO.setStdCode((String) alStdIsd.get(0));
				personalInfoVO.setIsdCode((String) alStdIsd.get(1));
			}
			//check for user type
			if(strActiveSubLink.equals(strUserManagement))
			{
				DynaActionForm frmUserList=(DynaActionForm)request.getSession().getAttribute("frmUserList");
				userAccessVO.setUserType((String)frmUserList.getString("sUserList"));
				userAccessVO.setAccessYN("Y");//In add mode, make the User as active
			}//end of if(strActiveSubLink.equals("User Management"))
			if(strActiveSubLink.equals(strHospital))
			{
				userAccessVO.setUserType("HOS");
			}//end of if(strActiveSubLink.equals(strHospital))
			if(strActiveSubLink.equals(strPartner))
			{
				String contactType = request.getParameter("contactType");
				userAccessVO.setContactType(contactType);
				userAccessVO.setUserType("PTR");
			}//end of if(strActiveSubLink.equals(strPartner))
			if("Banks".equals(strActiveSubLink))
			{
				userAccessVO.setUserType("BAK");
				// added for intX to get the std and isd details from main screen
				ArrayList alStdIsd	=	new ArrayList();
				Long lngBankSeqId= new Long((String)request.getSession().getAttribute("bankSeqId"));
				alStdIsd	=	userContactObject.getStdIsd(lngBankSeqId,"BAK");
				personalInfoVO.setStdCode((String) alStdIsd.get(0));
				personalInfoVO.setIsdCode((String) alStdIsd.get(1));
			}//end of if(strActiveSubLink.equals(strInsurance))
			
			if(strActiveSubLink.equals(strInsurance))
			{
				userAccessVO.setUserType("INS");
				// added for intX to get the std and isd details from main screen
				ArrayList alStdIsd	=	new ArrayList();
				Long lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
				alStdIsd	=	userContactObject.getStdIsd(lngHospitalSeqId,"INS");
				personalInfoVO.setStdCode((String) alStdIsd.get(0));
				personalInfoVO.setIsdCode((String) alStdIsd.get(1));
			}//end of if(strActiveSubLink.equals(strInsurance))
			
			if(strActiveSubLink.equals(strGroupRegistration))
			{
				userAccessVO.setUserType("COR");
				ArrayList alStdIsd	=	new ArrayList();
				Long lngHospitalSeqId= Long.valueOf((String) request.getSession().getAttribute("groupSeqId"));
				alStdIsd	=	userContactObject.getStdIsd(lngHospitalSeqId,"COR");
				personalInfoVO.setStdCode((String) alStdIsd.get(0));
				personalInfoVO.setIsdCode((String) alStdIsd.get(1));
			
				
			}//end of if(strActiveSubLink.equals(strGroupRegistration))
        //added for broker login kocb
			if(strActiveSubLink.equals(strBroker))
			{
				userAccessVO.setUserType("BRO");
				BrokerVO brokervo=(BrokerVO) request.getSession().getAttribute("SelectedOffice");
				ArrayList alStdIsd	=	new ArrayList();
				Long lngHospitalSeqId= brokervo.getInsuranceSeqID();
				alStdIsd	=	userContactObject.getStdIsd(lngHospitalSeqId,"BRO");
				personalInfoVO.setStdCode((String) alStdIsd.get(0));
				personalInfoVO.setIsdCode((String) alStdIsd.get(1));
			}//end of if(strActiveSubLink.equals(strGroupRegistration))
			
			// endadded for broker login kocb
			if(strActiveSubLink.equals(strBanks))
			{
				userAccessVO.setUserType("BAK");
			}//end of if(strActiveSubLink.equals(strBanks))
			userContactVO.setPersonalInfo(personalInfoVO);
			userContactVO.setAdditionalInfo(additionalInfoVO);
			userContactVO.setUserAccessInfo(userAccessVO);
			userContactVO.setSmsEmailTriggerInfo(smsEmailTriggerInfoVO);
			DynaActionForm frmUserContact = (DynaActionForm)FormUtils.setFormValues("frmUserContact", userContactVO,
																	 this, mapping, request);
			frmUserContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
															userContactVO.getPersonalInfo(),this,mapping,request));
			frmUserContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
															userContactVO.getAdditionalInfo(),this,mapping,request));
			frmUserContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
															userContactVO.getUserAccessInfo(),this,mapping,request));
			frmUserContact.set("smsEmailTriggerInfoVO",FormUtils.setFormValues("frmSMSEmailTriggerInfo",
					userContactVO.getSmsEmailTriggerInfo(),this,mapping,request));
			//appending the caption
			strCaption.append("Add ");
			if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
					strActiveSubLink.equals(strBanks) || strActiveSubLink.equals(strBroker) )
			{
				strCaption.append((String)frmSearchContact.getString("caption"));
			}//end of if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
				//strActiveSubLink.equals(strBanks))
			if(strActiveSubLink.equals(strHospital))
			{
				strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			}//end of if(strActiveSubLink.equals(strHospital))
			if(strActiveSubLink.equals(strPartner))
			{
				strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			}//end of if(strActiveSubLink.equals(strPartner))
			frmUserContact.set("caption",strCaption.toString());
			request.getSession().setAttribute("flag", "");
			request.getSession().setAttribute("frmUserContact",frmUserContact);
			
			return this.getForward(strContactPath, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
	public ActionForward doAddLicense(ActionMapping mapping,ActionForm form,HttpServletRequest request,
								HttpServletResponse response) throws Exception{
		try{
			log.info("Inside ContactAction doAddLicense");
			setLinks(request);
			String strContactPath="";
			strContactPath=getForwardPath(request);
			ContactVO userContactVO=null;
			StringBuffer strCaption=new StringBuffer();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			strCaption=buildCaption(request);
			DynaActionForm generalForm = (DynaActionForm)form;
			DynaActionForm frmSearchContact=(DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			userContactVO=new ContactVO();
			generalForm.initialize(mapping);
			PersonalInfoVO personalInfoVO=new PersonalInfoVO();
			AdditionalInfoVO additionalInfoVO=new AdditionalInfoVO();
			UserAccessVO userAccessVO=new UserAccessVO();
			
			UserManager userContactObject = this.getUserManagerObject();
			ArrayList alStdIsd	=	new ArrayList();
			Long lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
			alStdIsd	=	userContactObject.getStdIsd(lngHospitalSeqId,"");
			personalInfoVO.setStdCode((String) alStdIsd.get(0));
			personalInfoVO.setIsdCode((String) alStdIsd.get(1));
			
			//check for user type
			if(strActiveSubLink.equals(strUserManagement))
			{
				DynaActionForm frmUserList=(DynaActionForm)request.getSession().getAttribute("frmUserList");
				userAccessVO.setUserType((String)frmUserList.getString("sUserList"));
				userAccessVO.setAccessYN("Y");//In add mode, make the User as active
			}//end of if(strActiveSubLink.equals("User Management"))
			if(strActiveSubLink.equals(strHospital))
			{
				userAccessVO.setUserType("HOS");
			}//end of if(strActiveSubLink.equals(strHospital))
			if(strActiveSubLink.equals(strPartner))
			{
				userAccessVO.setUserType("PTR");
			}//end of if(strActiveSubLink.equals(strPartner))
			
			userContactVO.setPersonalInfo(personalInfoVO);
			userContactVO.setAdditionalInfo(additionalInfoVO);
			userContactVO.setUserAccessInfo(userAccessVO);
			DynaActionForm frmProfessionalContact = (DynaActionForm)FormUtils.setFormValues("frmProfessionalContact", userContactVO,
																	 this, mapping, request);
			frmProfessionalContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
															userContactVO.getPersonalInfo(),this,mapping,request));
			frmProfessionalContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
															userContactVO.getAdditionalInfo(),this,mapping,request));
			frmProfessionalContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
															userContactVO.getUserAccessInfo(),this,mapping,request));
			
			//for Professionals intx
			
			//appending the caption
			strCaption.append("Add ");
			if(strActiveSubLink.equals(strHospital))
			{
				strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			}//end of if(strActiveSubLink.equals(strHospital))
			frmProfessionalContact.set("caption",strCaption.toString());
			request.getSession().setAttribute("frmProfessionalContact",frmProfessionalContact);


			HospitalDetailVO hospitalDetailVO	=	(HospitalDetailVO)request.getSession().getAttribute("HospDetails");
			request.setAttribute("regType", hospitalDetailVO.getRegAuthority());
			//return this.getForward("hospProfessionContacts", mapping, request);
			return mapping.findForward("hospProfessionContacts");
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
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
								 HttpServletResponse response) throws Exception
	{
		try{
			log.debug("Inside ContactAction doSave");
			setLinks(request);
			
			DynaActionForm frmUserContact = (DynaActionForm)form;
			String strContactPath="";
			strContactPath=getForwardPath(request);
			StringBuffer strCaption=new StringBuffer();
			strCaption=buildCaption(request);
			UserManager userContactObject = this.getUserManagerObject();
			//CommunicationManager communicationManager = this.getCommunicationManagerObject();
			DynaActionForm personalInfoForm = (DynaActionForm) frmUserContact.get("personalInfoVO");
			DynaActionForm additionalInfoForm = (DynaActionForm) frmUserContact.get("additionalInfoVO");
			DynaActionForm userAccessForm = (DynaActionForm) frmUserContact.get("userAccessVO");
			DynaActionForm smsEmailTriggerForm = (DynaActionForm) frmUserContact.get("smsEmailTriggerInfoVO");
			String userType = frmUserContact.getString("userType");
			String strFrequencyIDForVipPreauth = request.getParameter("smsEmailTriggerInfoVO.strFrequencyIDForVipPreauth");
			//if personalInfoVO.activeYN checkbox in not selected
			if(request.getParameter("personalInfoVO.activeYN")==null||
					request.getParameter("personalInfoVO.activeYN").equals(""))
			{
				personalInfoForm.set("activeYN","N");
			}//end of if(request.getParameter("personalInfoVO.activeYN")==null||
				//request.getParameter("personalInfoVO.activeYN").equals(""))

			//if userAccessVO.accessYN checkbox in not selected
			if(request.getParameter("userAccessVO.accessYN")==null||
					request.getParameter("userAccessVO.accessYN").equals(""))
			{
				userAccessForm.set("accessYN","N");
			}//end of if(request.getParameter("userAccessVO.accessYN")==null||
				//request.getParameter("userAccessVO.accessYN").equals(""))
			if(((String)userAccessForm.getString("userType")).equals("HOS"))
			{
				// if personalInfoVO.activeYN checkbox in not selected
				if(request.getParameter("additionalInfoVO.residentYN")==null||
						request.getParameter("additionalInfoVO.residentYN").equals(""))
				{
					additionalInfoForm.set("residentYN","N");
				}//end of if(request.getParameter("additionalInfoVO.residentYN")==null||
				  //request.getParameter("additionalInfoVO.residentYN").equals(""))
			}//end of if(((String)userAccessForm.getString("userType")).equals("HOS"))
			if(request.getParameter("additionalInfoVO.softcopyAccessYN")==null||
					request.getParameter("additionalInfoVO.softcopyAccessYN").equals(""))
			{
				log.info("softcopyAccessYN :"+request.getParameter("additionalInfoVO.softcopyAccessYN"));
				additionalInfoForm.set("softcopyAccessYN","N");
			}//end of if(request.getParameter("additionalInfoVO.softcopyAccessYN")==null||
			if(request.getParameter("additionalInfoVO.softcopyOtBranch")==null||
					request.getParameter("additionalInfoVO.softcopyOtBranch").equals(""))
			{
				log.info("softcopyOtBranch :"+request.getParameter("additionalInfoVO.softcopyOtBranch"));
				additionalInfoForm.set("softcopyOtBranch","N");
			}//end of if(request.getParameter("additionalInfoVO.softcopyOtBranch")==null||
            
			if(request.getParameter("smsEmailTriggerInfoVO.userPermissiontoAuthorizedUserClaimRejection")==null ||request.getParameter("smsEmailTriggerInfoVO.userPermissiontoAuthorizedUserClaimRejection").equals("") ){
				smsEmailTriggerForm.set("userPermissiontoAuthorizedUserClaimRejection", "N");
			}
			
			if(request.getParameter("smsEmailTriggerInfoVO.userPermissiontoAuthorizedUserPreapprovalRejection")==null ||request.getParameter("smsEmailTriggerInfoVO.userPermissiontoAuthorizedUserPreapprovalRejection").equals("") ){
				smsEmailTriggerForm.set("userPermissiontoAuthorizedUserPreapprovalRejection", "N");
			}
			
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthSMSTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthSMSTriggerYN")==""){
				smsEmailTriggerForm.set("vipMemberPreauthSMSTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthEmailTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthEmailTriggerYN")==""){
				smsEmailTriggerForm.set("vipMemberPreauthEmailTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthPrimaryContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthPrimaryContactYN")==""){
				smsEmailTriggerForm.set("vipMemberPreauthPrimaryContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthAlternativeContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthAlternativeContactYN")==""){
				smsEmailTriggerForm.set("vipMemberPreauthAlternativeContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthNetworkRequestYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberPreauthNetworkRequestYN")==""){
				smsEmailTriggerForm.set("vipMemberPreauthNetworkRequestYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimSMSTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimSMSTriggerYN")==""){
				smsEmailTriggerForm.set("vipMemberClaimSMSTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimEmailTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimEmailTriggerYN")==""){
				smsEmailTriggerForm.set("vipMemberClaimEmailTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimPrimaryContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimPrimaryContactYN")==""){
				smsEmailTriggerForm.set("vipMemberClaimPrimaryContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimAlternativeContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimAlternativeContactYN")==""){
				smsEmailTriggerForm.set("vipMemberClaimAlternativeContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimMemberRequestYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimMemberRequestYN")==""){
				smsEmailTriggerForm.set("vipMemberClaimMemberRequestYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimNetworkRequestYN")==null || request.getParameter("smsEmailTriggerInfoVO.vipMemberClaimNetworkRequestYN")==""){
				smsEmailTriggerForm.set("vipMemberClaimNetworkRequestYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthSMSTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthSMSTriggerYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberPreauthSMSTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthEmailTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthEmailTriggerYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberPreauthEmailTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthPrimaryContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthPrimaryContactYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberPreauthPrimaryContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthAlternativeContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthAlternativeContactYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberPreauthAlternativeContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthNetworkRequestYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberPreauthNetworkRequestYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberPreauthNetworkRequestYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimSMSTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimSMSTriggerYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberClaimSMSTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimEmailTriggerYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimEmailTriggerYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberClaimEmailTriggerYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimPrimaryContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimPrimaryContactYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberClaimPrimaryContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimAlternativeContactYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimAlternativeContactYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberClaimAlternativeContactYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimMemberRequestYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimMemberRequestYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberClaimMemberRequestYN", "N");
			}
			if(request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimNetworkRequestYN")==null || request.getParameter("smsEmailTriggerInfoVO.nonVIPMemberClaimNetworkRequestYN")==""){
				smsEmailTriggerForm.set("nonVIPMemberClaimNetworkRequestYN", "N");
			}
			if(request.getParameter("additionalInfoVO.autoAssignYN")==null||
					request.getParameter("additionalInfoVO.autoAssignYN").equals(""))
			{
				log.info("autoAssignYN :"+request.getParameter("additionalInfoVO.autoAssignYN"));
				additionalInfoForm.set("autoAssignYN","N");
			}//end of if(request.getParameter("additionalInfoVO.autoAssignYN")==null||
			
			//Changes Added for Password Policy CR KOC 1235
			if(((String)userAccessForm.getString("userType")).equalsIgnoreCase("TTK") || ((String)userAccessForm.getString("userType")).equalsIgnoreCase("CAL") || ((String)userAccessForm.getString("userType")).equalsIgnoreCase("DMC")||((String)userAccessForm.getString("userType")).equalsIgnoreCase("INS")||((String)userAccessForm.getString("userType")).equalsIgnoreCase("HOS")||((String)userAccessForm.getString("userType")).equalsIgnoreCase("COR"))
			{
				if(request.getParameter("personalInfoVO.accn_locked_YN")==null || request.getParameter("personalInfoVO.accn_locked_YN").equals("") || request.getParameter("personalInfoVO.accn_locked_YN").equals("N"))
				{
					personalInfoForm.set("accn_locked_YN","N");
					
				}//end of if(request.getParameter("personalInfoVO.accn_locked_YN")==null || request.getParameter("personalInfoVO.accn_locked_YN").equals("") || request.getParameter("personalInfoVO.accn_locked_YN").equals("N"))
				
			}
			//End changes for Password Policy CR KOC 1235
			
			DynaActionForm frmSearchContact=(DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			AdditionalInfoVO additionalInfoVO=null;
			UserAccessVO userAccessVO=null;
			ContactVO userContactVO=null;
			PersonalInfoVO personalInfoVO=null;
			SMSEmailTriggerInfoVO smsEmailTriggerInfoVO = null;
			userContactVO = (ContactVO) FormUtils.getFormValues(frmUserContact, this, mapping, request);
			personalInfoVO=(PersonalInfoVO)FormUtils.getFormValues(personalInfoForm, "frmPersonalInfo",
													this, mapping, request);
			additionalInfoVO=(AdditionalInfoVO)FormUtils.getFormValues(additionalInfoForm, "frmAdditionInfo",
													this, mapping, request);
			userAccessVO=(UserAccessVO)FormUtils.getFormValues(userAccessForm, "frmUserAccessInfo",
													this, mapping, request);
			
			smsEmailTriggerInfoVO = (SMSEmailTriggerInfoVO) FormUtils.getFormValues(smsEmailTriggerForm, "frmSMSEmailTriggerInfo", this, mapping, request);
			userContactVO.setPersonalInfo(personalInfoVO);
			
			if(!((userAccessVO.getFingerPrint()==null||userAccessVO.getFingerPrint().trim().equals("null")||userAccessVO.getFingerPrint().equals(""))))
				userAccessVO.setFingerPrintFile(this.decodeToImage(userAccessVO.getFingerPrint()));
			
			if(((String)userAccessForm.getString("userType")).equals("HOS"))
			{
				//check for contact type which is not Doctor, having id: 6
				if(!((String)additionalInfoForm.getString("contactTypeID")).equals("6"))
				{
					additionalInfoVO.setRegistrationNbr("");
					additionalInfoVO.setQualification("");
					additionalInfoVO.setResidentYN("");
					additionalInfoVO.setSpecTypeID("");
				}//end of if(!((String)additionalInfoForm.getString("contactTypeID")).equals("6"))
			}//end of if(((String)userAccessForm.getString("userType")).equals("HOS"))
			userContactVO.setUserAccessInfo(userAccessVO);
			userContactVO.setAdditionalInfo(additionalInfoVO);
			userContactVO.setSmsEmailTriggerInfo(smsEmailTriggerInfoVO);
			userContactVO.setUpdatedBy(TTKCommon.getUserSeqId(request));//User Id
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			//Set the appropriate sequenceId depending up on the flow.
			if(strActiveSubLink.equals(strHospital))
			{
				userContactVO.setHospitalSeqID(TTKCommon.getWebBoardId(request));
			}//end of if(strActiveSubLink.equals(strHospital))
			if(strActiveSubLink.equals(strPartner))
			{
				userContactVO.setPartnerSeqID(TTKCommon.getWebBoardId(request));
			}//end of if(strActiveSubLink.equals(strPartner))

			if(strActiveSubLink.equals(strInsurance))
			{
				userContactVO.setInsuranceSeqID(TTKCommon.getLong((String)frmSearchContact.get("insuranceSeqId")));
			}//end of if(strActiveSubLink.equals(strInsurance))
			//added for broker login kocb
			
			if(strActiveSubLink.equals(strBroker))
			{
				userContactVO.setInsuranceSeqID(TTKCommon.getLong((String)frmSearchContact.get("insuranceSeqId")));
				/*userContactVO.setOfficeSeqID(headOffice.getTTKBranchCode());*/
			}//end of if(strActiveSubLink.equals(strInsurance))
			// end added for broker login kocb
			if(strActiveSubLink.equals(strGroupRegistration))
			{
				userContactVO.setGroupRegSeqID(TTKCommon.getLong((String)frmSearchContact.get("groupSeqId")));
			}//end of if(strActiveSubLink.equals(strGroupRegistration))
			if(strActiveSubLink.equals(strBanks))
			{
				userContactVO.setBankSeqID(TTKCommon.getLong((String)frmSearchContact.get("bankSeqId")));
			}//end of if(strActiveSubLink.equals(strBanks))
			//calling business layer to to save the information
			long lUpdate=userContactObject.saveContact(userContactVO,"");
//			EmailScheduler emailScheduler = new EmailScheduler();
			//communicationManager.sendMessage("EMAIL");
			//denial process
			if((userContactVO.getContactSeqID()!=null)&&((strActiveSubLink.equals(strInsurance))||((String)userAccessForm.getString("userType")).equals("INS")))
			{
				
				long lDenMail=userContactObject.saveDenMail(userContactVO.getContactSeqID(),personalInfoVO);	
			}
			//denial process
			if(lUpdate > 0)
			{
				if(userContactVO.getContactSeqID()== null)
				{
					request.setAttribute("updated","message.addedSuccessfully");
					userContactVO=new ContactVO();
					personalInfoVO=new PersonalInfoVO();
					additionalInfoVO=new AdditionalInfoVO();
					smsEmailTriggerInfoVO = new SMSEmailTriggerInfoVO();
					userAccessVO=new UserAccessVO();
					userAccessVO.setUserType((String)userAccessForm.getString("userType"));
					if(strActiveSubLink.equals(strUserManagement))
					{
						DynaActionForm frmUserList=(DynaActionForm)request.getSession().getAttribute("frmUserList");
						userAccessVO.setUserType((String)frmUserList.getString("sUserList"));
						userAccessVO.setAccessYN("Y");//In add mode, make the User as active
					}//end of if(strActiveSubLink.equals("User Management"))
					if(strActiveSubLink.equals(strHospital))
					{
						userAccessVO.setUserType("HOS");
					}//end of if(strActiveSubLink.equals(strHospital))
					
					if(strActiveSubLink.equals(strPartner))
					{
						userAccessVO.setUserType("PTR");
						userAccessVO.setContactType((String)userAccessForm.getString("contactType"));
					}//end of if(strActiveSubLink.equals(strPartner))
					
					if(strActiveSubLink.equals(strInsurance))
					{
						userAccessVO.setUserType("INS");
					}//end of if(strActiveSubLink.equals(strInsurance))
					//added for broker login kocb
					
					if(strActiveSubLink.equals(strBroker))
					{
						userAccessVO.setUserType("BRO");
					}//end of if(strActiveSubLink.equals(strInsurance))
					//end added for broker login kocb
					if(strActiveSubLink.equals(strGroupRegistration))
					{
						userAccessVO.setUserType("COR");
					}//end of if(strActiveSubLink.equals(strGroupRegistration))
					if(strActiveSubLink.equals(strBanks))
					{
						userAccessVO.setUserType("BAK");
					}//end of if(strActiveSubLink.equals(strBanks))

					userContactVO.setPersonalInfo(personalInfoVO);
					userContactVO.setAdditionalInfo(additionalInfoVO);
					userContactVO.setUserAccessInfo(userAccessVO);
					userContactVO.setSmsEmailTriggerInfo(smsEmailTriggerInfoVO);
					frmUserContact.initialize(mapping);
					frmUserContact = (DynaActionForm)FormUtils.setFormValues("frmUserContact", userContactVO,
																this, mapping, request);
					frmUserContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
															userContactVO.getPersonalInfo(),this,mapping,request));
					frmUserContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
															userContactVO.getAdditionalInfo(),this,mapping,request));
					frmUserContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
															userContactVO.getUserAccessInfo(),this,mapping,request));
				    frmUserContact.set("smsEmailTriggerInfoVO",FormUtils.setFormValues("frmSMSEmailTriggerInfo",
							userContactVO.getSmsEmailTriggerInfo(),this,mapping,request));
					strCaption.append("Add ");
				}//end of if(contactVO.getContactSeqID()== null)
				else
				{
					request.setAttribute("updated","message.savedSuccessfully");
					//make a requery
					userContactVO=userContactObject.getContact(userContactVO.getContactSeqID());
					frmUserContact = (DynaActionForm)FormUtils.setFormValues("frmUserContact", userContactVO,
															this, mapping, request);
					frmUserContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
															userContactVO.getPersonalInfo(),this,mapping,request));
					frmUserContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
															userContactVO.getAdditionalInfo(),this,mapping,request));
					frmUserContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
				                                           userContactVO.getUserAccessInfo(),this,mapping,request));
					
					frmUserContact.set("smsEmailTriggerInfoVO",FormUtils.setFormValues("frmSMSEmailTriggerInfo",
							userContactVO.getSmsEmailTriggerInfo(),this,mapping,request));
					strCaption.append("Edit ");
				}//end of else if(contactVO.getContactSeqID()== null)

				if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
						strActiveSubLink.equals(strBanks) || strActiveSubLink.equals(strBroker))
				{
					strCaption.append((String)frmSearchContact.getString("caption"));
				}//end of if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
					//strActiveSubLink.equals(strBanks))

				if(strActiveSubLink.equals(strHospital))
				{
					strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
				}//end of if(strActiveSubLink.equals(strHospital))
				
				if(strActiveSubLink.equals(strPartner))
				{
					strCaption.append("[ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
				}//end of if(strActiveSubLink.equals(strPartner))
				
				frmUserContact.set("caption",strCaption.toString());
				frmUserContact.set("userType",userType);
				
				DynaActionForm personalInfoForm1=(DynaActionForm)frmUserContact.get("personalInfoVO");
				String straccn_locked_YN=(String)personalInfoForm1.getString("accn_locked_YN");
					if("".equals(straccn_locked_YN) ||"N".equals(straccn_locked_YN))
					{
						straccn_locked_YN="NO";
						frmUserContact.set("accLockYN",straccn_locked_YN);
					}
					else
					{	straccn_locked_YN="YES";
						frmUserContact.set("accLockYN", straccn_locked_YN);
					}
				request.getSession().setAttribute("frmUserContact",frmUserContact);
				
			}//end of if(iUpdate > 0)
			return this.getForward(strContactPath, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to add/update the record of Professionals.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doSaveProfessions(ActionMapping mapping,ActionForm form,HttpServletRequest request,
								 HttpServletResponse response) throws Exception
	{
		try{
			log.debug("Inside ContactAction doSaveProfessions");
			setLinks(request);
			DynaActionForm frmProfessionalContact = (DynaActionForm)form;
			DynaActionForm frmUserContact = (DynaActionForm)form;
			String strContactPath="";
			strContactPath=getForwardPath(request);
			StringBuffer strCaption=new StringBuffer();
			strCaption=buildCaption(request);
			UserManager userContactObject = this.getUserManagerObject();
			//CommunicationManager communicationManager = this.getCommunicationManagerObject();
			DynaActionForm personalInfoForm = (DynaActionForm) frmProfessionalContact.get("personalInfoVO");
			DynaActionForm additionalInfoForm = (DynaActionForm) frmProfessionalContact.get("additionalInfoVO");
			DynaActionForm userAccessForm = (DynaActionForm) frmProfessionalContact.get("userAccessVO");
			//if personalInfoVO.activeYN checkbox in not selected
			if(request.getParameter("personalInfoVO.activeYN")==null||
					request.getParameter("personalInfoVO.activeYN").equals(""))
			{
				personalInfoForm.set("activeYN","N");
			}//end of if(request.getParameter("personalInfoVO.activeYN")==null||
				//request.getParameter("personalInfoVO.activeYN").equals(""))

			//if userAccessVO.accessYN checkbox in not selected
			if(request.getParameter("userAccessVO.accessYN")==null||
					request.getParameter("userAccessVO.accessYN").equals(""))
			{
				userAccessForm.set("accessYN","N");
			}//end of if(request.getParameter("userAccessVO.accessYN")==null||
				//request.getParameter("userAccessVO.accessYN").equals(""))
			
			DynaActionForm frmSearchContact=(DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			AdditionalInfoVO additionalInfoVO=null;
			UserAccessVO userAccessVO=null;
			ContactVO userContactVO=null;
			PersonalInfoVO personalInfoVO=null;
			userContactVO = (ContactVO) FormUtils.getFormValues(frmProfessionalContact, this, mapping, request);
			personalInfoVO=(PersonalInfoVO)FormUtils.getFormValues(personalInfoForm, "frmPersonalInfo",
													this, mapping, request);
			additionalInfoVO=(AdditionalInfoVO)FormUtils.getFormValues(additionalInfoForm, "frmAdditionInfo",
													this, mapping, request);
			userAccessVO=(UserAccessVO)FormUtils.getFormValues(userAccessForm, "frmUserAccessInfo",
													this, mapping, request);
			
			personalInfoVO.setStartDate(TTKCommon.getUtilDate( (String) frmProfessionalContact.get("startDate")));
			personalInfoVO.setEndDate(TTKCommon.getUtilDate( (String) frmProfessionalContact.get("endDate")));
			
			///-----------------------------FILE UPLOAD-------------
			int iUpdate	=	0;
			Long lngFileData=null;
			String updated="";
			String strNotify="";
			String fileName="";
			String origFileName	=	"";
			Pattern pattern =null;
			Matcher matcher =null;
			FileOutputStream outputStream = null;
			FormFile formFile = null;
			int fileSize=10*1024*1024;
			TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
			DateFormat df =new SimpleDateFormat("ddMMyyyy HH:mm:ss:S");
			df.setTimeZone(tz);  
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			//Get the FormFile object from ActionForm.
			StringBuffer strCaption1=new StringBuffer();
			ArrayList<Object> alFileAUploadList = new ArrayList<Object>();
			alFileAUploadList.add(TTKCommon.getUserSeqId(request));//0
			Long userSeqId	=	(Long) TTKCommon.getUserSeqId(request);
			String hosp_seq_id_mou_uploads	=	(String)request.getSession().getAttribute("hosp_seq_id_mou_uploads");
			Long hospSeqId	=	userSecurityProfile.getHospSeqId();
			formFile = (FormFile)frmProfessionalContact.get("file");
			userContactVO.setHospitalSeqID(TTKCommon.getWebBoardId(request));
			
			//pattern = Pattern.compile( "[^a-zA-Z0-9\\.\\s*]" );
			pattern = Pattern.compile( "[^a-zA-Z0-9_\\.\\-\\s*]" );
			matcher = pattern.matcher( formFile.getFileName() );
			//String 	strTimeStamp=((df.format(Calendar.getInstance(tz).getTime())).replaceAll(" ", "_")).replaceAll(":", "");
			String 	strTimeStamp=new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date());
			
			if(formFile==null || formFile.getFileSize()>1){
				fileName=strTimeStamp+"_"+formFile.getFileName();
			}
			personalInfoVO.setFile(fileName);
			
			//getting the Contact name and splitting the Professional ID from name to save
			String tempProfname	=	personalInfoVO.getName();
			if(tempProfname.indexOf('[')>0)
			{
				tempProfname	=	tempProfname.substring(0, tempProfname.indexOf('['));
			}
			personalInfoVO.setName(tempProfname);
			
			userContactVO.setPersonalInfo(personalInfoVO);
			userContactVO.setUserAccessInfo(userAccessVO);
			userContactVO.setAdditionalInfo(additionalInfoVO);
			userContactVO.setUpdatedBy(TTKCommon.getUserSeqId(request));//User Id
			
			
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			//Set the appropriate sequenceId depending up on the flow.
			ArrayList alLinkDetailsList=null;
			origFileName	=	formFile.getFileName();
			long lUpdate	=	0;
			if(!matcher.find())
			{
				//userContactVO.setDocName(fileName);

				alFileAUploadList.add(fileName);//1
				alFileAUploadList.add(hospSeqId);//3
				
				//	alFileDetails=this.populateSearchCriteria(webconfigInsCompInfoVO);
				String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("professionalsUpload"));
				//  
				File folder = new File(path);
				if(!folder.exists()){
					folder.mkdir();
				}
    	        String finalPath=(path+fileName);

				String strFileExt=formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1,formFile.getFileName().length());

				
				lUpdate=userContactObject.saveProfessionals(userContactVO,"");//save Contacts
				if(lUpdate > 0)
				{
						request.setAttribute("updated","message.savedSuccessfully");
						//make a re query
						userContactVO=userContactObject.getProfessionalContact(lUpdate);
						frmProfessionalContact = (DynaActionForm)FormUtils.setFormValues("frmProfessionalContact", userContactVO,
																this, mapping, request);
						frmProfessionalContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
																userContactVO.getPersonalInfo(),this,mapping,request));
						frmProfessionalContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
																userContactVO.getAdditionalInfo(),this,mapping,request));
						frmProfessionalContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
																userContactVO.getUserAccessInfo(),this,mapping,request));
						frmProfessionalContact.set("startDate", TTKCommon.getFormattedDate(userContactVO.getPersonalInfo().getStartDate()));
						frmProfessionalContact.set("endDate",TTKCommon.getFormattedDate(userContactVO.getPersonalInfo().getEndDate()));
						strCaption.append("Edit ");

					frmProfessionalContact.set("caption",strCaption.toString());
					request.setAttribute("notify",strNotify);
					request.getSession().setAttribute("frmProfessionalContact",frmProfessionalContact);
				}//end of if(iUpdate > 0)
				
				if(formFile.getFileName()!=null && formFile.getFileName().length()>0)
				{
				if(formFile.getFileSize()<=fileSize && formFile.getFileSize()>0)
					{
					/*if((strFileExt.equalsIgnoreCase("pdf"))   || (strFileExt.equalsIgnoreCase("doc")) 
                            || (strFileExt.equalsIgnoreCase("docx")) || (strFileExt.equalsIgnoreCase("xls"))   
                            || (strFileExt.equalsIgnoreCase("xlsx")))
                            COMMENTING THESE LINES FOR ALLOWING TO UPLOAD ALL KINDS OF FILES
					{  */ 
						
							outputStream = new FileOutputStream(new File(finalPath));
							outputStream.write(formFile.getFileData());

							//alFileAUploadList.add(fileName);//3 This just Adds file name
							alFileAUploadList.add(finalPath);//3 This code adds Total path of the file
							
					/*}//end of if(strFileExt.equalsIgnoreCase("html") || strFileExt.equalsIgnoreCase("mhtml")||strFileExt.equalsIgnoreCase("msg") || strFileExt.equalsIgnoreCase("rar")|| strFileExt.equalsIgnoreCase("zip") || strFileExt.equalsIgnoreCase("pdf") || strFileExt.equalsIgnoreCase("doc") || strFileExt.equalsIgnoreCase("docx") || strFileExt.equalsIgnoreCase("txt") || strFileExt.equalsIgnoreCase("odt") || strFileExt.equalsIgnoreCase("jrxml") || strFileExt.equalsIgnoreCase("xls") || strFileExt.equalsIgnoreCase("xlsx") )
					else{

						strNotify="selected "+formFile.getFileName()+" file should be any of these extensions (.pdf,.doc,.docx,.xls,.xlsx)";
						frmProfessionalContact = (DynaActionForm)FormUtils.setFormValues("frmProfessionalContact",userContactVO,this,mapping,request);
					}//end ofelse(strFileExt.equalsIgnoreCase("pdf"))
					COMMENTING THESE LINES FOR ALLOWING TO UPLOAD ALL KINDS OF FILES */
					}//end of if(formFile.getFileSize()<=fileSize)
					else{
					strNotify="selected "+formFile.getFileName()+" size  Shold be less than or equal to 10 MB";
					}//end of else part if(formFile.getFileSize()<=fileSize)
						}
			}
			else{
				//updated="selected "+formFile.getFileName()+" file is not in pdf format,Please select pdf file";
				strNotify="selected "+formFile.getFileName()+" file Should not have Special Characters like }!@#$%^&amp;*(])[{+";

			}
			
			//--------------------FILE UPLOAD ENDS--------------------
			
			
			frmProfessionalContact = (DynaActionForm)FormUtils.setFormValues("frmProfessionalContact",userContactVO,this,mapping,request);
		
			
			//calling business layer to to save the information
			
//			EmailScheduler emailScheduler = new EmailScheduler();
			//communicationManager.sendMessage("EMAIL");
			//denial process
			if((userContactVO.getContactSeqID()!=null)&&((strActiveSubLink.equals(strInsurance))||((String)userAccessForm.getString("userType")).equals("INS")))
			{
				
				long lDenMail=userContactObject.saveDenMail(userContactVO.getContactSeqID(),personalInfoVO);	
			}
			//denial process
			
			//request.setAttribute("updated",updated);
			if(outputStream!=null)
				outputStream.close();
			request.setAttribute("notify",strNotify);
			//return this.getForward(strContactPath, mapping, request);
			return mapping.findForward(strContactPath);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doSaveProfessions(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
			
			log.debug("Inside ContactAction doReset");
			setLinks(request);
			UserManager userContactObject = this.getUserManagerObject();
			String strContactPath="";
			StringBuffer strCaption=new StringBuffer();
			strCaption=buildCaption(request);
			ContactVO userContactVO=null;
			PersonalInfoVO personalInfoVO=null;
			UserAccessVO userAccessVO=null;
			AdditionalInfoVO additionalInfoVO=null;
			SMSEmailTriggerInfoVO smsEmailTriggerInfoVO;
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			strContactPath=getForwardPath(request);
			DynaActionForm frmContactForm=(DynaActionForm)form;
			DynaActionForm frmSearchContact=(DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			if(!TTKCommon.checkNull((String)frmContactForm.get("contactSeqID")).equals(""))
			{
				Long lContactSeqId=TTKCommon.getLong((String)frmContactForm.get("contactSeqID"));
				userContactVO=userContactObject.getContact(lContactSeqId);
			}//end of if(!TTKCommon.checkNull((String)frmContactForm.get("contactSeqID")).equals(""))
			else
			{
				userContactVO=new ContactVO();
				DynaActionForm frmUserAccessForm=(DynaActionForm)frmContactForm.get("userAccessVO");
				personalInfoVO=new PersonalInfoVO();
				additionalInfoVO=new AdditionalInfoVO();
				userAccessVO=new UserAccessVO();
				smsEmailTriggerInfoVO = new SMSEmailTriggerInfoVO();
				userAccessVO.setUserType(TTKCommon.checkNull((String)frmUserAccessForm.get("userType")));
				if(strActiveSubLink.equals(strUserManagement))
				{
					DynaActionForm frmUserList=(DynaActionForm)request.getSession().getAttribute("frmUserList");
					userAccessVO.setUserType((String)frmUserList.getString("sUserList"));
					userAccessVO.setAccessYN("Y");//In add mode, make the User as active
				}//end of if(strActiveSubLink.equals("User Management"))
				if(strActiveSubLink.equals(strHospital))
				{
					userAccessVO.setUserType("HOS");
				}//end of if(strActiveSubLink.equals(strHospital))
				if(strActiveSubLink.equals(strPartner))
				{
					userAccessVO.setUserType("PTR");
				}//end of if(strActiveSubLink.equals(strPartner))
				if(strActiveSubLink.equals(strInsurance))
				{
					userAccessVO.setUserType("INS");
				}//end of if(strActiveSubLink.equals(strInsurance))
				if(strActiveSubLink.equals(strGroupRegistration))
				{
					userAccessVO.setUserType("COR");
				}//end of if(strActiveSubLink.equals(strGroupRegistration))
				if(strActiveSubLink.equals(strBanks))
				{
					userAccessVO.setUserType("BAK");
				}//end of if(strActiveSubLink.equals(strBanks))
				userContactVO.setPersonalInfo(personalInfoVO);
				userContactVO.setAdditionalInfo(additionalInfoVO);
				userContactVO.setUserAccessInfo(userAccessVO);
				userContactVO.setSmsEmailTriggerInfo(smsEmailTriggerInfoVO);
			}//end of else if(!TTKCommon.checkNull((String)frmContactForm.get("contactSeqID")).equals(""))
			frmContactForm.initialize(mapping);

			DynaActionForm frmUserContact = (DynaActionForm)FormUtils.setFormValues("frmUserContact", userContactVO,
																this, mapping, request);
			frmUserContact.set("personalInfoVO",FormUtils.setFormValues("frmPersonalInfo",
															userContactVO.getPersonalInfo(),this,mapping,request));
			frmUserContact.set("additionalInfoVO",FormUtils.setFormValues("frmAdditionInfo",
															userContactVO.getAdditionalInfo(),this,mapping,request));
			frmUserContact.set("userAccessVO",FormUtils.setFormValues("frmUserAccessInfo",
															userContactVO.getUserAccessInfo(),this,mapping,request));
			frmUserContact.set("smsEmailTriggerInfoVO",FormUtils.setFormValues("frmSMSEmailTriggerInfo",
					userContactVO.getSmsEmailTriggerInfo(),this,mapping,request));
			if(frmUserContact.get("contactSeqID")==null)
			{
				strCaption.append("Add ");
			}//end of if(frmUserContact.get("contactSeqID")==null)
			else
			{
				strCaption.append("Edit ");
			}//end of else if(frmUserContact.get("contactSeqID")==null)

			if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance)||
					strActiveSubLink.equals(strBanks))
			{
				strCaption.append((String)frmSearchContact.getString("caption"));
			}//end of if(strActiveSubLink.equals(strGroupRegistration)||strActiveSubLink.equals(strInsurance))

			//Buidling the label to dispaly in JSP
			frmUserContact.set("caption",strCaption.toString());
			request.getSession().setAttribute("frmUserContact",frmUserContact);
			return this.getForward(strContactPath,mapping,request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
	public ActionForward doDeptChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
									HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ContactAction doReset");
			setLinks(request);
			return this.getForward(strAdminContactPath,mapping,request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doDeptChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	


	/**
	 * This method is used to reset the password.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doResetPassword(ActionMapping mapping,ActionForm form,HttpServletRequest request,
											HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ContactAction doResetPassword");
			setLinks(request);
			String strContactPath="";
			strContactPath=getForwardPath(request);
			DynaActionForm frmContactForm=(DynaActionForm)form;
			UserManager userContactObject = this.getUserManagerObject();
			//CommunicationManager communicationManager = this.getCommunicationManagerObject();
			Long lContactSeqId=TTKCommon.getLong((String)frmContactForm.get("contactSeqID"));
			int iResult =userContactObject.resetPassword(lContactSeqId,TTKCommon.getUserSeqId(request));
			//communicationManager.sendMessage("EMAIL");
			if(iResult>0)
			{
				request.setAttribute("updated","message.resetpassword");
			}//end of if(iResult>0)
			String accLockYN = (String)frmContactForm.get("accLockYN");
			frmContactForm.set("accLockYN",accLockYN);
			return this.getForward(strContactPath, mapping, request);

		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doResetPassword(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)

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
	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
									HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ContactAction doClose");
			setLinks(request);
			DynaActionForm frmUserContact = (DynaActionForm)form;
			String strBlock="";
			TableData tableData =TTKCommon.getTableData(request);
			UserManager userContactObject = this.getUserManagerObject();
			ContactManager contactManagerObject= this.getContactManagerObject();
			//remove frmUserContact from session
			request.getSession().removeAttribute("frmUserContact");
			tableData=(TableData)request.getSession().getAttribute("tableData");
			DynaActionForm frmUserLists=null;
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			Long lngHospitalSeqId	=	null;
			Long lngPartnerSeqId	=	null;
			if("Hospital".equals(strActiveSubLink))
					lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
			if("Partner".equals(strActiveSubLink))
				lngPartnerSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
			
			ArrayList alUserList =null;
			//check entry flow for the contact detail screen, to redirect back to grid screen
			if(strActiveSubLink.equals(strUserManagement))
			{
				frmUserLists = (DynaActionForm)request.getSession().getAttribute("frmUserList");
			}//end of if(strActiveSubLink.equals(strUserManagement))
			else
			{
				frmUserLists = (DynaActionForm)request.getSession().getAttribute("frmSearchContact");
			}//end of if else if(strActiveSubLink.equals(strUserManagement))
			DynaActionForm frmPersonalInfo=(DynaActionForm)frmUserContact.get("personalInfoVO");
			DynaActionForm frmUserAccessInfo=(DynaActionForm)frmUserContact.get("userAccessVO");
			//check whether Activeness/UserAccess permession is changed from the detail screen.
			if(tableData.getSearchData()!= null&& tableData.getSearchData().size()>0)
			{
				if(!((String)frmPersonalInfo.get("activeYN")).equals((String)frmUserLists.get("activeYN"))||
						!((String)frmUserAccessInfo.get("accessYN")).equals((String)frmUserLists.get("userAccessYn")))
				{
					if(strActiveSubLink.equals(strUserManagement))
					{
						alUserList= userContactObject.getUserList(tableData.getSearchData(),"");
					}//end of if(strActiveSubLink.equals(strUserManagement))
					else if(strActiveSubLink.equals("Hospital"))
					{
						alUserList= contactManagerObject.getHospitalContactList(tableData.getSearchData());
						//alUserList= contactManagerObject.getContactListIntx(lngHospitalSeqId);
					}//end of else if(strActiveSubLink.equals(Hospital))
					else if(strActiveSubLink.equals("Partner"))
					{
						String contactType = (String)frmUserAccessInfo.get("contactType");
						if("PAT".equals(contactType))
							alUserList= contactManagerObject.getContactListIntx1(lngPartnerSeqId);
						if("CLM".equals(contactType))
							alUserList= contactManagerObject.getContactListIntx2(lngPartnerSeqId);
						if("ACC".equals(contactType))
							alUserList= contactManagerObject.getContactListIntx3(lngPartnerSeqId);
					}//end of else if(strActiveSubLink.equals(Partner))
					else
					{
						alUserList= contactManagerObject.getContactList(tableData.getSearchData());
					}//end of else 

					if(alUserList!=null&&alUserList.size()==0)
					{
						tableData.modifySearchData(strDelete);
					}//end of if(alUserList!=null&&alUserList.size()==0)
					int iStartRowCount = Integer.parseInt((String)tableData.getSearchData().get(
															tableData.getSearchData().size()-2));
					if(iStartRowCount > 0)
					{
						if(strActiveSubLink.equals(strUserManagement))
						{
							alUserList= userContactObject.getUserList(tableData.getSearchData(),"");
						}//end of if(strActiveSubLink.equals(strUserManagement))
						else if(strActiveSubLink.equals("Hospital"))
						{
							alUserList= contactManagerObject.getHospitalContactList(tableData.getSearchData());
							//alUserList= contactManagerObject.getContactListIntx(lngHospitalSeqId);
						}//end of else if(strActiveSubLink.equals(Hospital))
						else if(strActiveSubLink.equals("Partner"))
						{
							String contactType = (String)frmUserAccessInfo.get("contactType");
							if("PAT".equals(contactType))
								alUserList= contactManagerObject.getContactListIntx1(lngPartnerSeqId);
							if("CLM".equals(contactType))
								alUserList= contactManagerObject.getContactListIntx2(lngPartnerSeqId);
							if("ACC".equals(contactType))
								alUserList= contactManagerObject.getContactListIntx3(lngPartnerSeqId);
						}//end of else if(strActiveSubLink.equals(Partner))
						else
						{
							alUserList= contactManagerObject.getContactList(tableData.getSearchData());
						}//end of else 
					}//end of if(iStartRowCount > 0)
					strBlock=strDelete;
				}//end of if(!((String)frmPersonalInfo.get("activeYN")).equals((String)frmUserLists.get("activeYN"))||
					//!((String)frmUserAccessInfo.get("accessYN")).equals((String)frmUserLists.get("userAccessYn")))
				else
				{
					strBlock=strClose;
					if(strActiveSubLink.equals(strUserManagement))
					{
						alUserList= userContactObject.getUserList(tableData.getSearchData(),"");
					}//end of if(strActiveSubLink.equals(strUserManagement))
					else if(strActiveSubLink.equals("Hospital"))
					{
						alUserList= contactManagerObject.getContactListIntx(lngHospitalSeqId);
					}//end of else if(strActiveSubLink.equals(Hospital))
					else if(strActiveSubLink.equals("Partner"))
					{ //PAT,CLM,ACC
						String contactType = (String)frmUserAccessInfo.get("contactType");
						if("PAT".equals(contactType))
							alUserList= contactManagerObject.getContactListIntx1(lngPartnerSeqId);
						if("CLM".equals(contactType))
							alUserList= contactManagerObject.getContactListIntx2(lngPartnerSeqId);
						if("ACC".equals(contactType))
							alUserList= contactManagerObject.getContactListIntx3(lngPartnerSeqId);
						
					}//end of else if(strActiveSubLink.equals(Hospital))
					else
					{
						alUserList= contactManagerObject.getContactList(tableData.getSearchData());
					}//end of else if(strActiveSubLink.equals(strUserManagement))
				}//end of else if(!((String)frmPersonalInfo.get("activeYN")).equals((String)frmUserLists.get("activeYN")
					//||!((String)frmUserAccessInfo.get("accessYN")).equals((String)frmUserLists.get("userAccessYn")))
				tableData.setData(alUserList,strBlock);
				//set the table data object to session
				request.getSession().setAttribute("tableData",tableData);
			}//end of if(tableData.getSearchData()!= null&& tableData.getSearchData().size()>0)

			//close from the UserManagement flow
			if(strActiveSubLink.equals(strUserManagement))
			{
				return this.getForward(strAdminContactClose, mapping, request);
			}//end of if(strActiveSubLink.equals(strUserManagement))

			//close from the Hospital flow
			if(strActiveSubLink.equals(strHospital))
			{
				return this.getForward(strHospitalContactClose, mapping, request);
			}//end of if(strActiveSubLink.equals(strHospital))
			
			
			if(strActiveSubLink.equals(strPartner))
			{
				return this.getForward(strPartnerContactClose, mapping, request);
			}//end of if(strActiveSubLink.equals(strPartner))

			//close from the Insurance flow
			if(strActiveSubLink.equals(strInsurance))
			{
				return this.getForward(strInsuranceContactClose, mapping, request);
			}//end of if(strActiveSubLink.equals(strInsurance))

			//close from the Broker flow kocb
			if(strActiveSubLink.equals(strBroker))
			{
				return this.getForward(strBrokerContactClose, mapping, request);
			}//end of if(strActiveSubLink.equals(strInsurance))

			//close from the Group Registration flow
			if(strActiveSubLink.equals(strGroupRegistration))
			{
				return this.getForward(strGroupRegistrationContactClose, mapping, request);
			}//end of if(strActiveSubLink.equals(strGroupRegistration))

			//close from the Banks flow
			if(strActiveSubLink.equals(strBanks))
			{
				return this.getForward(strBankClose, mapping, request);
			}//end of if(strActiveSubLink.equals(strBanks))

			return (mapping.findForward(strFailure));
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


	
	/**
	 * Returns the UserManager session object for invoking methods on it.
	 * @return UserManager session object which can be used for method invocation
	 * @exception throws TTKException
	 */
	private UserManager getUserManagerObject() throws TTKException
	{
		UserManager userManager = null;
		try
		{
			if(userManager == null)
			{
				InitialContext ctx = new InitialContext();
				userManager = (UserManager) ctx.lookup("java:global/TTKServices/business.ejb3/UserManagerBean!com.ttk.business.usermanagement.UserManager");
			}//end if(userManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "contactaction");
		}//end of catch
		return userManager;
	}//end getUserManagerObject()


	/**
	 * This method is prepares the Caption based on the flow and retunrs it
	 *
	 * @param request The HTTP request we are processing
	 * @return StringBuffer prepared Caption
	 */
	private StringBuffer buildCaption(HttpServletRequest request)throws TTKException
	{
		StringBuffer strCaption=new StringBuffer();

		try
		{
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			//		Buidling the label to dispaly in JSP
			if(strActiveSubLink.equals(strPartner)||strActiveSubLink.equals(strHospital)||strActiveSubLink.equals(strGroupRegistration)||
					strActiveSubLink.equals(strInsurance)||strActiveSubLink.equals(strBanks))
			{
				strCaption.append("Contacts - ");
			}//end of if(strActiveSubLink.equals(strPartner)||strActiveSubLink.equals(strHospital)||strActiveSubLink.equals(strGroupRegistration)||
				//strActiveSubLink.equals(strInsurance)||strActiveSubLink.equals(strBanks))
			else if(strActiveSubLink.equals(strUserManagement))
			{
				strCaption.append("User Details - ");
			}//end of if(strActiveSubLink.equals(strUserManagement))
		}catch(Exception exp)
		{
			throw new TTKException(exp, "contactAction");
		}//end of catch
		return strCaption;
	}//end of buildCaption(HttpServletRequest request)

	/**
	 * This method is used for getting forward path depending upon the flow.
	 * @param request The HTTP request we are processing.
	 * @return String contains the flow to which it needs to forward.
	 * @throws Exception if any error occurs
	 */
	private String getForwardPath(HttpServletRequest request)throws TTKException
	{
		String strContactPath="";
		try
		{
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			//	checking of the active sub link to forward to appropriate path
			if(strActiveSubLink.equals(strUserManagement))
			{
				strContactPath=strAdminContactPath;
			}//end of if(strActiveSubLink.equals(strUserManagement))
			if(strActiveSubLink.equals(strHospital))
			{
				strContactPath=strHospitalContactPath;
			}//end of if(strActiveSubLink.equals(strHospital))
			if(strActiveSubLink.equals(strPartner))
			{
				strContactPath=strPartnerContactPath;
			}//end of if(strActiveSubLink.equals(strPartner))
			if(strActiveSubLink.equals(strInsurance))
			{
				strContactPath=strInsuranceContactPath;
			}//end of if(strActiveSubLink.equals(strInsurance))
			if(strActiveSubLink.equals(strGroupRegistration))
			{
				strContactPath=strGroupRegistrationContact;
			}//end of if(strActiveSubLink.equals(strGroupRegistration))
			//added for broker login kocb
			if(strActiveSubLink.equals(strBroker))
			{
				strContactPath=strBrokerContactPath;
			}//end of if(strActiveSubLink.equals(strGroupRegistration))
			// end added for broker login kocb
			if(strActiveSubLink.equals(strBanks))
			{
				strContactPath=strBankcontact;
			}//end of if(strActiveSubLink.equals("Banks"))
			
			
		
			
		}catch(Exception exp)
		{
			throw new TTKException(exp, "contactAction");
		}//end of catch
		return strContactPath;
	}//end of getForwardPath(HttpServletRequest request)

	/**
	 * Returns the ContactManager session object for invoking methods on it.
	 * @return ContactManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private ContactManager getContactManagerObject() throws TTKException
	{
		ContactManager contactManager = null;
		try
		{
			if(contactManager == null)
			{
				InitialContext ctx = new InitialContext();
				contactManager = (ContactManager) ctx.lookup("java:global/TTKServices/business.ejb3/ContactManagerBean!com.ttk.business.empanelment.ContactManager");
			}//end of if(contactManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "contactAction");
		}//end of catch
		return contactManager;
	}//end getContactManagerObject()

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
			throw new TTKException(exp, "contactAction");
		}//end of catch
		return communicationManager;
	}//end of getCommunicationManagerObject()

	private byte[] decodeToImage(String imageString) {
        
        byte[] imageByte = null;
       
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageByte;
    }	





	

	public ActionForward doSaveEmployee(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception {
				
		try{
			log.debug("Inside ContactAction doSave");
			setLinks(request);
			
			DynaActionForm frmEmployeeContact = (DynaActionForm)form;
			ContactVO userContactVO=null;
			SMSEmailTriggerInfoVO smsEmailTriggerInfoVO = null;
			UserManager userContactObject = this.getUserManagerObject();
			ContactVO contactVo=null;
			userContactVO = (ContactVO) FormUtils.getFormValues(frmEmployeeContact, this, mapping, request);
			
			String flag = request.getParameter("flag");
			String userid = userContactVO.getUserId();
			String userType1 = userContactVO.getUserType1();
			String userType = userContactVO.getUserType();
			
			
			userContactVO.setAddedBy(TTKCommon.getUserSeqId(request));
			
			long lUpdate=userContactObject.saveEmployeeContact(userContactVO);
			
			request.setAttribute("updated","message.savedSuccessfully");
			
			
			
			
			contactVo=userContactObject.getEmployeeContact(userContactVO.getPolicyGroupSeqId(),userContactVO.getUserType());
			/*DynaActionForm frmempContact = (DynaActionForm)FormUtils.setFormValues("frmEmployeeContact", contactVo,
					this, mapping, request);*/
			
			frmEmployeeContact.set("insuredName", contactVo.getInsuredName());
			frmEmployeeContact.set("strGroupName", contactVo.getStrGroupName());
			frmEmployeeContact.set("strGroupID", contactVo.getStrGroupID());
			frmEmployeeContact.set("policyNumber", contactVo.getPolicyNumber());
			frmEmployeeContact.set("enrollmentNumber", contactVo.getEnrollmentNumber());
			frmEmployeeContact.set("accn_locked_YN", contactVo.getAccn_locked_YN());
			frmEmployeeContact.set("hddn_accn_locked_YN", contactVo.getAccn_locked_YN());
			frmEmployeeContact.set("userId", userid);
			frmEmployeeContact.set("userType1", userType1);
			frmEmployeeContact.set("userType", userType);
			frmEmployeeContact.set("emailId", contactVo.getEmailId());
			frmEmployeeContact.set("policyGroupSeqId", userContactVO.getPolicyGroupSeqId());
		
			//request.getSession().setAttribute("frmEmployeeContact",frmempContact);
			request.getSession().setAttribute("frmEmployeeContact",frmEmployeeContact);
			
			
			
			return this.getForward(strEmployeecontact, mapping, request);	
			
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
		
	}
			 
			 
			 
			 
			 

	public ActionForward doResetEmployeePassword(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception {

		
		try {
			log.debug("Inside ContactAction doResetEmployeePassword");
			setLinks(request);
			DynaActionForm frmEmployeeContact=(DynaActionForm)form;
			Long lContactSeqId=TTKCommon.getLong((String)frmEmployeeContact.get("policyGroupSeqId"));
	    	//int iResult =userContactObject.resetPassword(lContactSeqId,TTKCommon.getUserSeqId(request));
			//communicationManager.sendMessage("EMAIL");
			/*if(iResult>0)
			{
				request.setAttribute("updated","message.resetpassword");
			}*/
			
			ContactVO userContactVO=null;
			SMSEmailTriggerInfoVO smsEmailTriggerInfoVO = null;
			UserManager userContactObject = this.getUserManagerObject();
			ContactVO contactVo=null;
			userContactVO = (ContactVO) FormUtils.getFormValues(frmEmployeeContact, this, mapping, request);
			
			String flag = request.getParameter("flag");
			String userid = userContactVO.getUserId();
			String userType1 = userContactVO.getUserType1();
			String userType = userContactVO.getUserType();
			
			
			userContactVO.setAddedBy(TTKCommon.getUserSeqId(request));
			
			long lUpdate=userContactObject.saveEmployeeContact(userContactVO);//same for save and reset pwd
			
			
			request.setAttribute("updated","message.resetpassword");
			
			
			if("R".equals(flag)){
				/*EmailSchedulerPreauth mail = new EmailSchedulerPreauth();
				JobExecutionContext arg0 = null;
				mail.execute(arg0);*/
				
				CommunicationManager communicationManager = this.getCommunicationManagerObject();
				EmailSchedulerPreauth mail = new EmailSchedulerPreauth();
				communicationManager.sendMessage("EMAIL");
				
				
			}
			
			
			contactVo=userContactObject.getEmployeeContact(userContactVO.getPolicyGroupSeqId(),userContactVO.getUserType());
			/*DynaActionForm frmempContact = (DynaActionForm)FormUtils.setFormValues("frmEmployeeContact", contactVo,
					this, mapping, request);*/
			
			frmEmployeeContact.set("insuredName", contactVo.getInsuredName());
			frmEmployeeContact.set("strGroupName", contactVo.getStrGroupName());
			frmEmployeeContact.set("strGroupID", contactVo.getStrGroupID());
			frmEmployeeContact.set("policyNumber", contactVo.getPolicyNumber());
			frmEmployeeContact.set("enrollmentNumber", contactVo.getEnrollmentNumber());
			frmEmployeeContact.set("accn_locked_YN", contactVo.getAccn_locked_YN());
			frmEmployeeContact.set("hddn_accn_locked_YN", contactVo.getAccn_locked_YN());
			frmEmployeeContact.set("emailId", contactVo.getEmailId());
			frmEmployeeContact.set("userId", userid);
			frmEmployeeContact.set("userType1", userType1);
			frmEmployeeContact.set("userType", userType);
			frmEmployeeContact.set("policyGroupSeqId", userContactVO.getPolicyGroupSeqId());
		
			//request.getSession().setAttribute("frmEmployeeContact",frmempContact);
			request.getSession().setAttribute("frmEmployeeContact",frmEmployeeContact);
			
	return this.getForward(strEmployeecontact, mapping, request);	
			
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
		
		
		
	}
			 
			 
			 
	public ActionForward doCloseEmployee(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception {
		
		try{
			log.debug("Inside ContactAction doCloseEmployee");
			setLinks(request);
			DynaActionForm frmEmployeeContact=(DynaActionForm)form;
		
			
	      return this.getForward("userlist", mapping, request);	
			
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
		
	}
	



	
	
	public ActionForward resetEmployee(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception {
		
try{
	log.debug("Inside ContactAction resetEmployee");
	setLinks(request);
	
	DynaActionForm frmEmployeeContact = (DynaActionForm)form;
	ContactVO userContactVO=null;
	SMSEmailTriggerInfoVO smsEmailTriggerInfoVO = null;
	UserManager userContactObject = this.getUserManagerObject();
	ContactVO contactVo=null;
	userContactVO = (ContactVO) FormUtils.getFormValues(frmEmployeeContact, this, mapping, request);
	
	String flag = request.getParameter("flag");
	String userid = userContactVO.getUserId();
	String userType1 = userContactVO.getUserType1();
	String userType = userContactVO.getUserType();
	
	
	userContactVO.setAddedBy(TTKCommon.getUserSeqId(request));
	
	contactVo=userContactObject.getEmployeeContact(userContactVO.getPolicyGroupSeqId(),userContactVO.getUserType());
	/*DynaActionForm frmempContact = (DynaActionForm)FormUtils.setFormValues("frmEmployeeContact", contactVo,
			this, mapping, request);*/
	
	frmEmployeeContact.set("insuredName", contactVo.getInsuredName());
	frmEmployeeContact.set("strGroupName", contactVo.getStrGroupName());
	frmEmployeeContact.set("strGroupID", contactVo.getStrGroupID());
	frmEmployeeContact.set("policyNumber", contactVo.getPolicyNumber());
	frmEmployeeContact.set("enrollmentNumber", contactVo.getEnrollmentNumber());
	frmEmployeeContact.set("accn_locked_YN", contactVo.getAccn_locked_YN());
	frmEmployeeContact.set("hddn_accn_locked_YN", contactVo.getAccn_locked_YN());
	frmEmployeeContact.set("userId", userid);
	frmEmployeeContact.set("userType1", userType1);
	frmEmployeeContact.set("userType", userType);
	frmEmployeeContact.set("emailId", contactVo.getEmailId());
	frmEmployeeContact.set("policyGroupSeqId", userContactVO.getPolicyGroupSeqId());

	//request.getSession().setAttribute("frmEmployeeContact",frmempContact);
	request.getSession().setAttribute("frmEmployeeContact",frmEmployeeContact);
	
	
	
	return this.getForward(strEmployeecontact, mapping, request);	
	
}
catch(TTKException expTTK)
{
	return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
	return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
}//end of catch(Exception exp)

}
		
	
	
	
	
	public ActionForward doCompanyChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try {
			log.debug("Inside ContactAction doReset");
			setLinks(request);
			return this.getForward(strAdminContactPath, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "contactAction"));
		}// end of catch(Exception exp)
}//end of doDeptChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doCloseContact(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception {
		
		try{
			log.debug("Inside ContactAction doCloseContact");
			setLinks(request);
			DynaActionForm frmUserContact = (DynaActionForm)form;
		
			
			request.getSession().removeAttribute("tableData");
			TableData  tableData =TTKCommon.getTableData(request);
			 tableData = new TableData();
			 tableData.createTableInfo("InsuranceContactTable",new ArrayList());
			 request.getSession().setAttribute("tableData",tableData);
			
			
			
			
			//session.removeAttribute("");
			return this.getForward(strGroupRegistrationContactClose, mapping, request);
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"contactAction"));
		}//end of catch(Exception exp)
		
	}
	



}//end of ContactAction