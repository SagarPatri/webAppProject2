package com.ttk.action.finance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.business.empanelment.PartnerManager;
import com.ttk.business.finance.BankAccountManager;
import com.ttk.business.finance.CustomerBankDetailsManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.empanelment.AccountDetailVO;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.HospitalAuditVO;
import com.ttk.dto.finance.BankAccountDetailVO;
import com.ttk.dto.finance.CustomerBankDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is added for cr koc 1103 added eft
 */
public class CustomerBankAcctGeneralAction extends TTKAction {
	private static Logger log = Logger.getLogger(CustomerBankAcctGeneralAction.class);
	// Exception Message Identifier
	private static final String strBankAcctDetails = "BankAcctDetails";
	// declare forward paths
	private static final String strBankacctdetail = "bankacctdetail";

	private static final String strAccountsError = "accounts";
    private static final String strBankHospAccountReviewDetail="hospReviewDetails";    
    private static final String strEmbassyDetails="embassyDetails";
    private static final String strPartnerbankacctdetail = "partnerbankacctdetail";
	
	public ActionForward doViewPolicyBankAccount(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			
			//setLinks(request);
			log.info("Inside BankAccountGeneral doViewBankAccount");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			StringBuffer strCaption = new StringBuffer();
			String strEditMode = "";
			CustomerBankDetailsVO customerBankDetailsVO = null;
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			strSwitchType=strSwitchType.concat("Polc");
			this.setLinks(request,strSwitchType);
			
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			frmCustomerBankAcctGeneral.initialize(mapping);
			if (TTKCommon.getWebBoardId(request) == null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.policyaccountno.required"); 
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request) == null)
			strCaption.append("- Edit");
			strEditMode = "Edit";

			customerBankDetailsVO = customerBankDetailsManager.getPolicyBankAccountDetail(TTKCommon.getWebBoardId(request), TTKCommon.getUserSeqId(request));
			
			request.getSession().setAttribute("policySeqId", customerBankDetailsVO.getPolicySeqID());
			request.getSession().setAttribute("stoppreauthdate", customerBankDetailsVO.getStopPreauthDate());
			request.getSession().setAttribute("stopclaimdate", customerBankDetailsVO.getStopClaimsDate());
			// TTKCommon.getWebBoardId(request),
			
			frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
			setFormValues(customerBankDetailsVO, mapping, request);
			

			//TTKCommon.deleteWebBoardId(request);

			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;

			hmCityList = customerBankDetailsManager.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon
						.checkNull(frmCustomerBankAcctGeneral.get("bankname")));

			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;

			
			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));
			
			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			
			hmDistList = customerBankDetailsManager.getBankCityInfo(bankState,
					bankName);
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity")));

			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			
			frmCustomerBankAcctGeneral.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			frmCustomerBankAcctGeneral.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			frmCustomerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			// request.getSession().setAttribute("alCityList",new ArrayList());
			frmCustomerBankAcctGeneral.set("editmode", strEditMode);
			frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
			request.getSession().setAttribute("switchType", "POLC");
			request.getSession().setAttribute("isAccessable", true);
			String cCode = customerBankDetailsManager.getCountryCode(frmCustomerBankAcctGeneral.getString("countryCode"));
			frmCustomerBankAcctGeneral.set("towDigitCountryCode",cCode); 
			request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			// request.getSession().removeAttribute("toolbar");
			return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}// end of doViewBankAccount(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,

	// HttpServletResponse response)
	
	
	
	public ActionForward doViewMemberAccountReviewDetails(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			
			//setLinks(request);
			log.debug("Inside BankAccountGeneral doViewMemberAccountReviewDetails");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			
			frmCustomerBankAcctGeneral.set("caption", "Review Provider Accounts");

			request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			// request.getSession().removeAttribute("toolbar");
			return this.getForward(strBankHospAccountReviewDetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}// end of doViewMemberAccountReviewDetails(ActionMapping mapping,ActionForm

	/**
	 * This method is used to reload the screen when the reset button is
	 * pressed. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doReset(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			checkWebboardVisabulity(request);
			log.debug("Inside BankAccountGeneral doReset");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			CustomerBankDetailsVO customerBankDetailsVO = new CustomerBankDetailsVO();
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			StringBuffer strCaption = new StringBuffer();
			// CustomerBankDetailsVO customerBankDetailsVO=null;
			String strEditMode = "";
			// BankAccountManager
			// bankAcctObject=this.getBankAccountManagerObject();
			CustomerBankDetailsManager customerBankDetailsManager = this
			.getCustomerBankDetailsManagerObject();

			strCaption.append("- Edit");
			/*
			 * bankAccountDetailVO=bankAcctObject.getBankAccountDetail(TTKCommon.
			 * getWebBoardId(request), TTKCommon.getUserSeqId(request));
			 */
			if (!TTKCommon.checkNull(customerBankDetailsVO.getInsurenceSeqId()).equals("")) {
				customerBankDetailsVO = customerBankDetailsManager.getPolicyBankAccountDetail((Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicySeqID()), TTKCommon.getUserSeqId(request));
			} else if (!TTKCommon.checkNull(customerBankDetailsVO.getPolicyGroupSeqID()).equals("")) {
				customerBankDetailsVO = customerBankDetailsManager.getMemberBankAccountDetail((Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicyGroupSeqID()), TTKCommon.getUserSeqId(request));
			} else if (!TTKCommon.checkNull(
					customerBankDetailsVO.getHospitalSeqId()).equals("")) {
				customerBankDetailsVO = customerBankDetailsManager.getHospBankAccountDetail(new Long(TTKCommon.checkNull(customerBankDetailsVO.getHospitalSeqId())), TTKCommon.getUserSeqId(request));
			}else if (!TTKCommon.checkNull(customerBankDetailsVO.getEmbassySeqID()).equals("")) {
				customerBankDetailsVO = customerBankDetailsManager.getEmbassyAccountDetail((Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicyGroupSeqID()));
			}

			

			frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
			strEditMode = "Edit";
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;


			hmCityList = customerBankDetailsManager.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname")));

			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			//
			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));
			// String
			// bankState=(String)TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			HashMap hmDistList = null;
			ArrayList alDistList = null;
			hmDistList = customerBankDetailsManager.getBankCityInfo(bankState,bankName);
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity")));

			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			frmCustomerBankAcctGeneral.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			frmCustomerBankAcctGeneral.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			frmCustomerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			frmCustomerBankAcctGeneral.set("reviewYN",customerBankDetailsVO.getReviewedYN());

			// keep the frmBankAcctGeneral in session scope
			request.getSession().setAttribute("frmCustomerBankAcctGeneral",
					frmCustomerBankAcctGeneral);
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strEmbassyDetails, mapping, request);
			else
				return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}// end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)

	public ActionForward doViewHospBankAccount(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			
			//setLinks(request);
			log.debug("Inside CustmerBankAccGeneral doViewHospBankAccount");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			StringBuffer strCaption = new StringBuffer();
			String strEditMode = "";
			CustomerBankDetailsVO customerBankDetailsVO = null;
			String strSwitchType="HOSP";
			this.setLinks(request,strSwitchType);
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			frmCustomerBankAcctGeneral.initialize(mapping);
			if (TTKCommon.getWebBoardId(request) == null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.hospaccountno.required");
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request) == null)
			strCaption.append("- Edit");
			strEditMode = "Edit";

			customerBankDetailsVO = customerBankDetailsManager.getHospBankAccountDetail(TTKCommon.getWebBoardId(request),TTKCommon.getUserSeqId(request));
			request.getSession().setAttribute("hospSeqId", customerBankDetailsVO.getHospitalSeqId());
			setHospFormValues(customerBankDetailsVO, mapping, request);

			frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);

			//TTKCommon.deleteWebBoardId(request);
			// request.getSession().setAttribute("alCityList",new ArrayList());
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;

			hmCityList = customerBankDetailsManager.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname")));

			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;

			customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();

			
			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));

			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			
			hmDistList = customerBankDetailsManager.getBankCityInfo(bankState,bankName);
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity")));

			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			frmCustomerBankAcctGeneral.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			frmCustomerBankAcctGeneral.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			frmCustomerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			frmCustomerBankAcctGeneral.set("editmode", strEditMode);
			frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
			request.getSession().setAttribute("switchType", strSwitchType);
			request.getSession().setAttribute("isAccessable", true);
			frmCustomerBankAcctGeneral.set("reviewYN",customerBankDetailsVO.getReviewedYN());
			String cCode = customerBankDetailsManager.getCountryCode(frmCustomerBankAcctGeneral.getString("countryCode"));
			frmCustomerBankAcctGeneral.set("towDigitCountryCode",cCode);	
			// keep the frmBankAcctGeneral in session scope
			request.getSession().setAttribute("frmCustomerBankAcctGeneral",
					frmCustomerBankAcctGeneral);
			request.getSession().setAttribute("stoppreauthdate", customerBankDetailsVO.getStopPreauthDate());
			request.getSession().setAttribute("stopclaimdate", customerBankDetailsVO.getStopClaimsDate());
			return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}// end of doViewBankAccount(ActionMapping mapping,ActionForm
	private void setHospFormValues(CustomerBankDetailsVO customerBankDetailsVO,
			ActionMapping mapping, HttpServletRequest request) throws TTKException {
		ArrayList<Object> alHospInfofinance = new ArrayList<Object>();
		try {
			
			// Store Hospital Bank Account Information in ArrayList, to check
			// whether any changes is made while updating
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getHospitalAccountINameOf()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankname()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankAccno()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getPolicyNumber()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankBranch()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankAccType()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getMicr()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getIfsc()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getNeft()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankcity()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankState()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankPhoneno()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getAddress1()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getAddress2()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getAddress3()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getPinCode()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getCountryCode()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getIssueChqToHosp()));	
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate=null;
			String endDate=null;
			if(customerBankDetailsVO.getStartDate()!=null)
				strDate=formatter.format(customerBankDetailsVO.getStartDate());	
			if(customerBankDetailsVO.getEndDate()!=null)
				endDate=formatter.format(customerBankDetailsVO.getEndDate());
			alHospInfofinance.add(TTKCommon.checkNull(strDate));
			alHospInfofinance.add(TTKCommon.checkNull(endDate));			
			 request.getSession().setAttribute("alHospInfofinance",alHospInfofinance);

		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
		
	}
	// form,HttpServletRequest request,

	// HttpServletResponse response)

	public ActionForward doViewMemberAccount(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			
			//setLinks(request);
			log.info("Inside BankAccountGeneral doViewBankAccount");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			StringBuffer strCaption = new StringBuffer();
			String strEditMode = "";
			CustomerBankDetailsVO customerBankDetailsVO = null;
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			strSwitchType=strSwitchType.concat("Mem");
			this.setLinks(request,strSwitchType);
			
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);

			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			frmCustomerBankAcctGeneral.initialize(mapping);
			if (TTKCommon.getWebBoardId(request) == null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.memberaccountno.required");
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request) == null)
			strCaption.append("- Edit");
			strEditMode = "Edit";

			
			String switchType=(String) request.getSession().getAttribute("switchType");
			customerBankDetailsVO = customerBankDetailsManager.getMemberBankAccountDetail(TTKCommon.getWebBoardId(request), TTKCommon.getUserSeqId(request));
			if(switchType!=null&&switchType.equals("POLC")){
			String strCheckIssuedTo=(String)request.getSession().getAttribute("strCheckIssuedTo");
			if(strCheckIssuedTo!=null&&strCheckIssuedTo.equals("Individual"))
			request.getSession().setAttribute("policySeqId", customerBankDetailsVO.getPolicyGroupSeqID());
			else
				request.getSession().setAttribute("policySeqId", customerBankDetailsVO.getPolicySeqID());
			}else if(switchType!=null&&switchType.equals("CLAM")){
				request.getSession().setAttribute("claimSeqId", customerBankDetailsVO.getPolicyGroupSeqID());
			}			
			frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);// TTKCommon.getWebBoardId(request),
			setFormValues(customerBankDetailsVO, mapping, request);
			//TTKCommon.deleteWebBoardId(request);
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;
			// DynaActionForm frmCustomerBankAcctGeneral= (DynaActionForm)form;

			hmCityList = customerBankDetailsManager.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname")));

			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;

			customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();

			
			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));
			
			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			
			hmDistList = customerBankDetailsManager.getBankCityInfo(bankState,
					bankName);
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity")));

			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			frmCustomerBankAcctGeneral.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			frmCustomerBankAcctGeneral.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			frmCustomerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			// request.getSession().setAttribute("alCityList",new ArrayList());
			frmCustomerBankAcctGeneral.set("editmode", strEditMode);
			frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
			request.getSession().setAttribute("switchType", switchType);
			request.getSession().setAttribute("isAccessable", true);
			String cCode = customerBankDetailsManager.getCountryCode(frmCustomerBankAcctGeneral.getString("countryCode"));
			frmCustomerBankAcctGeneral.set("towDigitCountryCode",cCode);	////
			// keep the frmBankAcctGeneral in session scope
			request.getSession().setAttribute("stoppreauthdate", customerBankDetailsVO.getStopPreauthDate());
			request.getSession().setAttribute("stopclaimdate", customerBankDetailsVO.getStopClaimsDate());
			request.getSession().setAttribute("frmCustomerBankAcctGeneral",
					frmCustomerBankAcctGeneral);
			return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}// end of doViewBankAccount(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,

	
	
	
	@SuppressWarnings("unused")
	public ActionForward doViewEmbassyDtls(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			
			//setLinks(request);
			log.info("Inside CustmerBankAccGeneral  kish doViewEmbassyDtls 2");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			StringBuffer strCaption = new StringBuffer();
			String strEditMode = "";
			CustomerBankDetailsVO customerBankDetailsVO = null;
			String strSwitchType="EMBS";
			//strSwitchType=strSwitchType.concat("Mem");
			this.setLinks(request,strSwitchType);

			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			frmCustomerBankAcctGeneral.initialize(mapping);
			
			
			if (TTKCommon.getWebBoardId(request)== null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.embassy.required");
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request) == null)
			strCaption.append("- Edit");
			strEditMode = "Edit";

			customerBankDetailsVO = customerBankDetailsManager.getEmbassyAccountDetail(TTKCommon.getWebBoardId(request));
			request.getSession().setAttribute("embassySeqId", customerBankDetailsVO.getEmbassySeqID());
			setFormEmbassyValues(customerBankDetailsVO, mapping, request);

			frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);

			//TTKCommon.deleteWebBoardId(request);
			// request.getSession().setAttribute("alCityList",new ArrayList());
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;

			hmCityList = customerBankDetailsManager.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname")));

			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;

			customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();

			
			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));

			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			
			hmDistList = customerBankDetailsManager.getBankCityInfo(bankState,bankName);
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alDistList == null) {
				alDistList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity")));

			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			frmCustomerBankAcctGeneral.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			frmCustomerBankAcctGeneral.set("alDistList", alDistList);
			request.getSession().setAttribute("alDistList", alDistList);
			frmCustomerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			frmCustomerBankAcctGeneral.set("editmode", strEditMode);
			frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
			request.getSession().setAttribute("switchType", strSwitchType);
			request.getSession().setAttribute("isAccessable", true);
			frmCustomerBankAcctGeneral.set("reviewYN",customerBankDetailsVO.getReviewedYN());
			frmCustomerBankAcctGeneral.set("switchType", strSwitchType);
			String cCode = customerBankDetailsManager.getCountryCode(frmCustomerBankAcctGeneral.getString("countryCode"));
			frmCustomerBankAcctGeneral.set("towDigitCountryCode",cCode);	
			// keep the frmBankAcctGeneral in session scope
			request.getSession().setAttribute("frmCustomerBankAcctGeneral",
					frmCustomerBankAcctGeneral);
			return this.getForward(strEmbassyDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strEmbassyDetails));
		}// end of catch(Exception exp)
	}// end of doViewEmbassyDtls(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,
	
	// HttpServletResponse response)

	// added For EFT
	/**
	 * This method is used to add/update the record. Finally it forwards to the
	 * appropriate view based on the specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param reques The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doSave(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			strSwitchType=strSwitchType.concat("Polc");
			this.setLinks(request,strSwitchType);
			log.debug("Inside CustomerBankAccGeneral doSave");
			// BankAccountDetailVO bankAccountDetailVO=null;
			String strTarget = "";
			CustomerBankDetailsVO customerBankDetailsVO = new CustomerBankDetailsVO();
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			String towDigitCountryCode = frmCustomerBankAcctGeneral.getString("towDigitCountryCode");
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			String strEditMode = "";
			StringBuffer strCaption = new StringBuffer();
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			customerBankDetailsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// User
						String switchTypeFinance = (String) request.getAttribute("switchTypeFinance");

			ArrayList<Object> alModifiedHospInfo = new ArrayList<Object>();
			alModifiedHospInfo = populateAccountInfo(frmCustomerBankAcctGeneral,mapping, request);

			boolean bFlag = true;
			// call the checkDifference method to check whether any change is
			// made in the bank information
			if (request.getSession().getAttribute("alHospInfofinance") != null) {
				
				bFlag = isModified(alModifiedHospInfo, (ArrayList) request.getSession().getAttribute("alHospInfofinance"));
				
			}// end of if(request.getSession().getAttribute("alHospInfo")!=null)
			
			if("Y".equals(TTKCommon.checkNull(request.getParameter("stopPreAuthsYN")))){
				customerBankDetailsVO.setStopPreAuthsYN("Y");
				frmCustomerBankAcctGeneral.set("stopPreAuthsYN", "Y");
			}else{
				customerBankDetailsVO.setStopPreAuthsYN("N");
				frmCustomerBankAcctGeneral.set("stopPreAuthsYN", "N");
			}
	      if("Y".equals(TTKCommon.checkNull(request.getParameter("stopClaimsYN")))){
	    	  customerBankDetailsVO.setStopClaimsYN("Y");
	    	  frmCustomerBankAcctGeneral.set("stopClaimsYN", "Y");
	      }else{
				customerBankDetailsVO.setStopClaimsYN("N");
				frmCustomerBankAcctGeneral.set("stopClaimsYN", "N");
	      }
			
			if (bFlag == false) {
				strTarget = "referencedetail";
				frmCustomerBankAcctGeneral.set("flagValidate", "true");
				return this.getForward(strTarget, mapping, request);
			}// end of if(bFlag==false)
			// Long
			// iUpdate=bankAcctObject.saveBankAccountIfsc(customerBankDetailsVO);
			Long iUpdate = customerBankDetailsManager.saveBankAccountIfsc(customerBankDetailsVO);
			// set the appropriate message
			if (iUpdate > 0) {
				if (frmCustomerBankAcctGeneral.get("policySeqID") != null && !frmCustomerBankAcctGeneral.get("policySeqID").equals("")) {
					request.setAttribute("updated","message.savedSuccessfully");
					request.setAttribute("cacheId", "" + iUpdate);
					request.setAttribute("cacheDesc", customerBankDetailsVO.getInsureName());
					// finally modify the web board details, if the account no.
					// is changed
					TTKCommon.modifyWebBoardId(request);

				}// end of if(frmBankAcctGeneral.get("accountSeqID")!=null &&
				
				else {
					request.setAttribute("updated","message.addedSuccessfully");
					customerBankDetailsVO.setPolicySeqID(iUpdate);
					// bankAccountDetailVO.setAccountSeqID(iUpdate);
					this.addToWebBoard(customerBankDetailsVO, request);
				}// end of else if(frmBankAcctGeneral.get("accountSeqID")!=null
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getPolicyBankAccountDetail((Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicySeqID()), TTKCommon.getUserSeqId(request));
				request.getSession().setAttribute("stoppreauthdate", customerBankDetailsVO.getStopPreauthDate());
				request.getSession().setAttribute("stopclaimdate", customerBankDetailsVO.getStopClaimsDate());
				strEditMode = "Edit";
				strCaption.append("- Edit");
				frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
				if (frmCustomerBankAcctGeneral.get("policySeqID") == null || frmCustomerBankAcctGeneral.get("policySeqID").equals("")) {
					frmCustomerBankAcctGeneral.set("policySeqID", iUpdate.toString());
				} else {
					// frmCustomerBankAcctGeneral.set("status",customerBankDetailsVO.getStatusTypeID());
				}
				frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
				frmCustomerBankAcctGeneral.set("editmode", strEditMode);
				frmCustomerBankAcctGeneral.set("towDigitCountryCode", towDigitCountryCode);
				// keep the frmBankAcctGeneral in session scope
				request.getSession().setAttribute("frmCustomerBankAcctGeneral",
						frmCustomerBankAcctGeneral);
				request.getSession().setAttribute("bankSeqId", customerBankDetailsVO.getBankSeqId());
			}// end of if(iUpdate > 0)
			return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}// end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)

	/**
	 * This method is used to get the details of the selected record from
	 * web-board. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doChangeWebBoard(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.debug("Inside BankAccountGeneral doChangeWebBoard");
		HttpSession session=request.getSession();
		String strSwitchType="";
		DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
		strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
		
		if(strSwitchType.equals("HOSP"))
		{
			return	doViewHospBankAccount(mapping, form, request, response);
		}else if(strSwitchType.equals("EMBS"))
		{
			return	doViewEmbassyDtls(mapping, form, request, response);
		}
		else if(strSwitchType.equals("PTNR"))
		{
			return	doViewPartnerBankDetails(mapping, form, request, response);
		}
		else
		{
		return doViewMemberAccount(mapping, form, request, response);
		}
	}// end of ChangeWebBoard(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,

	// HttpServletResponse response)

	/**
	 * This method is used to add/update the record. Finally it forwards to the
	 * appropriate view based on the specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doSaveMember(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			strSwitchType=strSwitchType.concat("Mem");
			this.setLinks(request,strSwitchType);
			log.debug("Inside BankAccountGeneral doSave");
			// BankAccountDetailVO bankAccountDetailVO=null;
			CustomerBankDetailsVO customerBankDetailsVO = new CustomerBankDetailsVO();
			String strTarget = "";
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			// BankAccountManager
			// bankAcctObject=this.getBankAccountManagerObject();
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			String strEditMode = "";
			StringBuffer strCaption = new StringBuffer();
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils
			.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			customerBankDetailsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// User
			// Id
   			// calling business layer to save the bank account detail
			String switchTypeFinance = (String) request.getAttribute("switchTypeFinance");
			ArrayList<Object> alModifiedHospInfo = new ArrayList<Object>();
			alModifiedHospInfo = populateAccountInfo(frmCustomerBankAcctGeneral,mapping, request);
			String towDigitCountryCode = request.getParameter("towDigitCountryCode");
			boolean bFlag = true;
			// call the checkDifference method to check whether any change is
			// made in the bank information
			if (request.getSession().getAttribute("alHospInfofinance") != null) {
				
				
				bFlag = isModified(alModifiedHospInfo, (ArrayList) request.getSession().getAttribute("alHospInfofinance"));
				
			}// end of if(request.getSession().getAttribute("alHospInfo")!=null)
			
			if("Y".equals(TTKCommon.checkNull(request.getParameter("stopPreAuthsYN")))){
				customerBankDetailsVO.setStopPreAuthsYN("Y");
				frmCustomerBankAcctGeneral.set("stopPreAuthsYN", "Y");
			}
			else{
				customerBankDetailsVO.setStopPreAuthsYN("N");
				frmCustomerBankAcctGeneral.set("stopPreAuthsYN", "N");
			}
	      if("Y".equals(TTKCommon.checkNull(request.getParameter("stopClaimsYN")))){
	    	  customerBankDetailsVO.setStopClaimsYN("Y");
	    	  frmCustomerBankAcctGeneral.set("stopClaimsYN", "Y");
	      }else{
				customerBankDetailsVO.setStopClaimsYN("N");
				frmCustomerBankAcctGeneral.set("stopClaimsYN", "N");
	      }
	      
			if (bFlag == false) {
				strTarget = "referencedetail";
				frmCustomerBankAcctGeneral.set("flagValidate", "true");
				frmCustomerBankAcctGeneral.set("towDigitCountryCode", towDigitCountryCode);
				return this.getForward(strTarget, mapping, request);
			}// end of if(bFlag==false)
			// Long
			// iUpdate=bankAcctObject.saveMemberBankAccountIfsc(customerBankDetailsVO);
			
			Long iUpdate = customerBankDetailsManager.saveMemberBankAccountIfsc(customerBankDetailsVO);
			// set the appropriate message;
			if (iUpdate > 0) {
				
				if (frmCustomerBankAcctGeneral.get("policySeqID") != null && !frmCustomerBankAcctGeneral.get("policySeqID").equals("")) {
					request.setAttribute("updated","message.savedSuccessfully");
					request.setAttribute("cacheId", "" + iUpdate);
					request.setAttribute("cacheDesc", customerBankDetailsVO.getInsureName());
					request.getSession().setAttribute("bankSeqId", customerBankDetailsVO.getBankSeqId());
					// finally modify the web board details, if the account no.
					// is changed
					TTKCommon.modifyWebBoardId(request);

				}// end of if(frmBankAcctGeneral.get("accountSeqID")!=null &&
				// !frmBankAcctGeneral.get("accountSeqID")
				// .equals(""))
				else {
					request.setAttribute("updated","message.addedSuccessfully");
					
					this.addToWebBoard(customerBankDetailsVO, request);
				}// end of else if(frmBankAcctGeneral.get("accountSeqID")!=null
				
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getMemberBankAccountDetail((Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicyGroupSeqID()), TTKCommon.getUserSeqId(request));
				// getMemberBankAccountdetail1(TTKCommon.getWebBoardId(request),TTKCommon.getUserSeqId(request));
				strEditMode = "Edit";
				strCaption.append("- Edit");
				frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
				if (frmCustomerBankAcctGeneral.get("policySeqID") == null || frmCustomerBankAcctGeneral.get("policySeqID").equals("")) {
					frmCustomerBankAcctGeneral.set("policyGroupSeqID", iUpdate.toString());
				} else {
					// frmCustomerBankAcctGeneral.set("status",customerBankDetailsVO.getStatusTypeID());
				}
				setFormValues(customerBankDetailsVO, mapping, request);
				frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
				frmCustomerBankAcctGeneral.set("editmode", strEditMode);
				frmCustomerBankAcctGeneral.set("towDigitCountryCode", towDigitCountryCode);
				// keep the frmBankAcctGeneral in session scope
				request.getSession().setAttribute("frmCustomerBankAcctGeneral",
						frmCustomerBankAcctGeneral);
				request.getSession().setAttribute("stoppreauthdate", customerBankDetailsVO.getStopPreauthDate());
				request.getSession().setAttribute("stopclaimdate", customerBankDetailsVO.getStopClaimsDate());
			}// end of if(iUpdate > 0)
			return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}// end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)
	/**
	 * This method is used to add/update the record. Finally it forwards to the
	 * appropriate view based on the specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doSaveHosp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			this.setLinks(request,strSwitchType);
			String strTarget = "";
			log.debug("Inside BankAccountGeneral doSave");
			// BankAccountDetailVO bankAccountDetailVO=null;
			CustomerBankDetailsVO customerBankDetailsVO = new CustomerBankDetailsVO();
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			// BankAccountManager
			// bankAcctObject=this.getBankAccountManagerObject();
			CustomerBankDetailsManager customerBankDetailsManager = this
			.getCustomerBankDetailsManagerObject();
			String strEditMode = "";
			StringBuffer strCaption = new StringBuffer();
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils
			.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			customerBankDetailsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// User
			// Id

			// calling business layer to save the bank account detail
			

			ArrayList<Object> alModifiedHospInfo = new ArrayList<Object>();

			// Call populateAccountInfo to store Hospital Bank Account
			// Information in arraylist
			alModifiedHospInfo = populateHospAccountInfo(frmCustomerBankAcctGeneral,mapping, request);

			boolean bFlag = true;
			// call the checkDifference method to check whether any change is
			// made in the bank information
			if("Y".equals(TTKCommon.checkNull(request.getParameter("stopPreAuthsYN")))){
				customerBankDetailsVO.setStopPreAuthsYN("Y");
				frmCustomerBankAcctGeneral.set("stopPreAuthsYN", "Y");
			}
			else{
				customerBankDetailsVO.setStopPreAuthsYN("N");
				frmCustomerBankAcctGeneral.set("stopPreAuthsYN", "N");
			}
	      if("Y".equals(TTKCommon.checkNull(request.getParameter("stopClaimsYN")))){
	    	  customerBankDetailsVO.setStopClaimsYN("Y");
	    	  frmCustomerBankAcctGeneral.set("stopClaimsYN", "Y");
	      }
			else{
				customerBankDetailsVO.setStopClaimsYN("N");
				frmCustomerBankAcctGeneral.set("stopClaimsYN", "N");
			}
			
			if (request.getSession().getAttribute("alHospInfofinance") != null) {
				
				bFlag = isModified(alModifiedHospInfo, (ArrayList) request.getSession().getAttribute("alHospInfofinance"));
				
			}// end of if(request.getSession().getAttribute("alHospInfo")!=null)

			if (bFlag == false) {
				
				strTarget = "referencedetail";
				frmCustomerBankAcctGeneral.set("flagValidate", "true");
				return this.getForward(strTarget, mapping, request);
			}// end of if(bFlag==false
			
			Long iUpdate = customerBankDetailsManager.saveHospBankAccountIfsc(customerBankDetailsVO);
			
			// set the appropriate message
			if (iUpdate >0) {
				
				if (frmCustomerBankAcctGeneral.get("hospitalSeqId") != null && !frmCustomerBankAcctGeneral.get("hospitalSeqId").equals("")) {
					request.setAttribute("updated","message.savedSuccessfully");
					request.setAttribute("cacheId", "" + iUpdate);
					request.setAttribute("cacheDesc", customerBankDetailsVO.getEmpanalDescription());
					request.getSession().setAttribute("bankSeqId", customerBankDetailsVO.getBankSeqId());
					// finally modify the web board details, if the account no.
					// is changed
					TTKCommon.modifyWebBoardId(request);

				}// end of if(frmBankAcctGeneral.get("accountSeqID")!=null &&
				// !frmBankAcctGeneral.get("accountSeqID")
				// .equals(""))
				else {
					request.setAttribute("updated",	"message.addedSuccessfully");
					
				}// end of else if(frmBankAcctGeneral.get("accountSeqID")!=null
				String towDigitCountryCode = frmCustomerBankAcctGeneral.getString("towDigitCountryCode");
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getHospBankAccountDetail(new Long(TTKCommon.checkNull(customerBankDetailsVO.getHospitalSeqId())), TTKCommon.getUserSeqId(request));
				// getMemberBankAccountdetail1(TTKCommon.getWebBoardId(request),TTKCommon.getUserSeqId(request));
				strEditMode = "Edit";
				strCaption.append("- Edit");
				frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);

				frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
				frmCustomerBankAcctGeneral.set("editmode", strEditMode);
				frmCustomerBankAcctGeneral.set("reviewYN",customerBankDetailsVO.getReviewedYN());
				frmCustomerBankAcctGeneral.set("towDigitCountryCode",towDigitCountryCode);
				// keep the frmBankAcctGeneral in session scope
				request.getSession().setAttribute("frmCustomerBankAcctGeneral",
						frmCustomerBankAcctGeneral);
				request.getSession().setAttribute("stoppreauthdate", customerBankDetailsVO.getStopPreauthDate());
				request.getSession().setAttribute("stopclaimdate", customerBankDetailsVO.getStopClaimsDate());
			}// end of if(iUpdate > 0)
			return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}

	private ArrayList<Object> populateHospAccountInfo(
			DynaActionForm frmCustomerBankAcctGeneral, ActionMapping mapping,
			HttpServletRequest request) throws TTKException {
		ArrayList<Object> alAccountInfoNew = new ArrayList<Object>();
		AddressVO bankAddressVO = null;
		// ActionForm
		// bankAddressForm=(ActionForm)accountForm.get("bankAddressVO");
		try {
			// bankAddressVO=(AddressVO)FormUtils.getFormValues(bankAddressForm,"frmBankAddress",this,mapping,request);			
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("hospitalAccountINameOf")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("bankname")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("bankAccno")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("policyNumber")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("bankBranch")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("bankAccType")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("micr")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("ifsc")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("neft")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("bankcity")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("bankState")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("bankPhoneno")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("address1")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("address2")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("address3")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("pinCode")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("countryCode")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("issueChqToHosp")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("startDate")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmCustomerBankAcctGeneral.getString("endDate")));
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
		return alAccountInfoNew;
	}
	/**
	 * This method is used to add the reference information. Finally it forwards
	 * to the appropriate view based on the specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doReferenceDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			//setLinks(request);
			checkWebboardVisabulity(request);
			log.info("Inside doReferenceDetail method of AccountsAction");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			String strEditMode = "";
			StringBuffer strCaption = new StringBuffer();
			DynaActionForm generalForm1=(DynaActionForm)request.getSession().getAttribute("frmCustomerBankDetailsSearch");
			String strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			String towDigitCountryCode= frmCustomerBankAcctGeneral.getString("towDigitCountryCode");
			String stopPreauthDate="";
			String stopClaimsDate="";
			// BankAccountManager
			// bankAcctObject=this.getBankAccountManagerObject();
			CustomerBankDetailsManager customerBankDetailsManager = this
			.getCustomerBankDetailsManagerObject();
			AccountDetailVO accountDetailVO = null;
			CustomerBankDetailsVO customerBankDetailsVO = null;

			
			ArrayList<Object> alHospInfo = new ArrayList<Object>();
			// Call populateAccountInfo to store Hospital Bank Account
			// Information in arraylist
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				alHospInfo = populateEmbassyInfo(frmCustomerBankAcctGeneral, mapping,request);
			else if("Partner Bank Details".equals(TTKCommon.getActiveTab(request))){
				alHospInfo = populatePartnerInfo(frmCustomerBankAcctGeneral, mapping, request);
			}else
				alHospInfo = populateAccountInfo(frmCustomerBankAcctGeneral, mapping,request);
			// Set the alHospInfo into session
			request.getSession().setAttribute("alHospInfo", alHospInfo);
			customerBankDetailsVO = getFormValues((DynaActionForm) form,
					mapping, request);
			// logs
			if("Y".equals(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("stopPreAuthsYN"))))
				customerBankDetailsVO.setStopPreAuthsYN("Y");
			else
				customerBankDetailsVO.setStopPreAuthsYN("N");
	      if("Y".equals(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("stopClaimsYN"))))
	    	  customerBankDetailsVO.setStopClaimsYN("Y");
			else
				customerBankDetailsVO.setStopClaimsYN("N");
			// end logs
//			if (!TTKCommon.checkNull(customerBankDetailsVO.getPolicySeqID()).equals("")&&(request.getSession().getAttribute("switchType")==null||request.getSession().getAttribute("switchType").equals("POLC"))) {
				if ("POLC".equals(request.getSession().getAttribute("switchType"))) {
				String strCheckIssuedTo=(String)request.getSession().getAttribute("strCheckIssuedTo");
				if(strCheckIssuedTo!=null&&strCheckIssuedTo.equals("Individual"))
				{
					customerBankDetailsVO.setSwitchType("MEM");
					customerBankDetailsVO.setPolicySeqID(customerBankDetailsVO.getPolicyGroupSeqID());
				}else
					customerBankDetailsVO.setSwitchType("POLC");
				
				stopPreauthDate = frmCustomerBankAcctGeneral.getString("stopPreauthDate");
				stopClaimsDate = frmCustomerBankAcctGeneral.getString("stopClaimsDate");
				Long iUpdate = customerBankDetailsManager.saveMemberBankAccountIfsc(customerBankDetailsVO);
				frmCustomerBankAcctGeneral.initialize(mapping);
				
				if(strCheckIssuedTo!=null&&strCheckIssuedTo.equals("Individual"))
				customerBankDetailsVO = customerBankDetailsManager.getMemberBankAccountDetail(customerBankDetailsVO.getPolicyGroupSeqID(), TTKCommon.getUserSeqId(request));
				else
				customerBankDetailsVO = customerBankDetailsManager.getPolicyBankAccountDetail(customerBankDetailsVO.getPolicySeqID(), TTKCommon.getUserSeqId(request));
			}
//				else if (!TTKCommon.checkNull(customerBankDetailsVO.getPolicyGroupSeqID()).equals("")&&(request.getSession().getAttribute("switchType")!=null&&request.getSession().getAttribute("switchType").equals("CLAM"))) {
				else if ("CLAM".equals(request.getSession().getAttribute("switchType"))) {
				customerBankDetailsVO.setSwitchType("MEM");
				Long iUpdate = customerBankDetailsManager.saveMemberBankAccountIfsc(customerBankDetailsVO);
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getMemberBankAccountDetail((Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicyGroupSeqID()), TTKCommon.getUserSeqId(request));
				request.getSession().setAttribute("claimSeqId",customerBankDetailsVO.getPolicyGroupSeqID());
			}else if (!TTKCommon.checkNull(customerBankDetailsVO.getInsurenceSeqId()).equals("")) {
				
				Long iUpdate = customerBankDetailsManager.saveBankAccountIfsc(customerBankDetailsVO);
				frmCustomerBankAcctGeneral.initialize(mapping);
				// customerBankDetailsVO=bankAcctObject.getPolicyBankAccountDetail((Long)TTKCommon.checkNull(customerBankDetailsVO.getPolicySeqID()),TTKCommon.getUserSeqId(request));
				customerBankDetailsVO = customerBankDetailsManager.getPolicyBankAccountDetail((Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicySeqID()), TTKCommon.getUserSeqId(request));
			}else if (customerBankDetailsVO.getHospitalSeqId()!=null&&!customerBankDetailsVO.getHospitalSeqId().equals("")) {
				
				Long iUpdate = customerBankDetailsManager
				.saveHospBankAccountIfsc(customerBankDetailsVO);
				stopPreauthDate = frmCustomerBankAcctGeneral.getString("stopPreauthDate");
				stopClaimsDate = frmCustomerBankAcctGeneral.getString("stopClaimsDate");
				frmCustomerBankAcctGeneral.initialize(mapping);
				
				customerBankDetailsVO = customerBankDetailsManager.getHospBankAccountDetail(new Long(TTKCommon.checkNull(customerBankDetailsVO.getHospitalSeqId())), TTKCommon.getUserSeqId(request));
			}else if ("EMBS".equals(request.getSession().getAttribute("switchType"))) {
				Long iUpdate = customerBankDetailsManager.saveEmbassyDetails(customerBankDetailsVO);
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getEmbassyAccountDetail(new Long(TTKCommon.checkNull(customerBankDetailsVO.getEmbassySeqID())));
			}else if (customerBankDetailsVO.getPartnerSeqId()!=null&&!customerBankDetailsVO.getPartnerSeqId().equals("")||"PTNR".equals(request.getSession().getAttribute("switchType"))) {
				customerBankDetailsVO.setPartnerBankSeqId(Long.parseLong(customerBankDetailsVO.getBankSeqId()));
				customerBankDetailsVO.setPtnrSeqID((Long) request.getSession().getAttribute("partnerSeqId"));
				Long iUpdate = customerBankDetailsManager.savePartnerDetails(customerBankDetailsVO);
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getPartnerBankDetail(customerBankDetailsVO.getPtnrSeqID());
			}
			
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;
			
			String bankName = (String) TTKCommon.checkNull(customerBankDetailsVO.getBankname());
			hmCityList = customerBankDetailsManager.getbankStateInfo();
			if("PTNR".equals(request.getSession().getAttribute("switchType"))){
				hmCityList = customerBankDetailsManager.getCityInfo(customerBankDetailsVO.getBankState());
				if (hmCityList != null) {
					alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(customerBankDetailsVO.getBankState()));
				}if (alCityList == null) {
					alCityList = new ArrayList();
				}
			}else{
				if (hmCityList != null) {
					alCityList = (ArrayList) hmCityList.get(bankName);
					
				}// end of if(hmCityList!=null)
				if (alCityList == null) {
					alCityList = new ArrayList();
				}// end of if(alCityList==null)
			}
			if("PTNR".equals(request.getSession().getAttribute("switchType")))
			frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmPartnerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
			else
			frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
			
			frmCustomerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			
			strEditMode = "Edit";
			strCaption.append("- Edit");
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				setFormEmbassyValues(customerBankDetailsVO, mapping, request);
			else if("Partner Bank Details".equals(TTKCommon.getActiveTab(request))){
				setPartnerFormValues(customerBankDetailsVO, mapping, request);
			}
			else
				setFormValues(customerBankDetailsVO, mapping, request);
			frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
			frmCustomerBankAcctGeneral.set("editmode", strEditMode);
			frmCustomerBankAcctGeneral.set("switchType", strSwitchType);
			frmCustomerBankAcctGeneral.set("towDigitCountryCode",towDigitCountryCode);
			frmCustomerBankAcctGeneral.set("stopPreAuthsYN", customerBankDetailsVO.getStopPreAuthsYN());
			frmCustomerBankAcctGeneral.set("stopPreauthDate",stopPreauthDate);
			frmCustomerBankAcctGeneral.set("stopClaimsYN", customerBankDetailsVO.getStopClaimsYN());
			frmCustomerBankAcctGeneral.set("stopClaimsDate", stopClaimsDate);
			
			if("PTNR".equals(request.getSession().getAttribute("switchType"))){
				frmCustomerBankAcctGeneral.set("flagValidate", "false");
				frmCustomerBankAcctGeneral.set("bankcity", customerBankDetailsVO.getBankcity());
				
				request.getSession().setAttribute("frmPartnerBankAcctGeneral",frmCustomerBankAcctGeneral);
			}else
				request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
				request.setAttribute("updated", "message.savedSuccessfully");			
			request.getSession().setAttribute("bankSeqId", customerBankDetailsVO.getBankSeqId());
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strEmbassyDetails, mapping, request);
			else if("Partner Bank Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strPartnerbankacctdetail, mapping, request);
			else
				return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
		}// end of catch(Exception exp)
	}// end of doReferenceDetail(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to navigate to previous screen when closed button is
	 * clicked. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doClose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			checkWebboardVisabulity(request);
			log.info("Inside doClose method of AccountsAction");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			frmCustomerBankAcctGeneral.set("flagValidate", null);
			if("Partner Bank Details".equals(TTKCommon.getActiveTab(request)))
			request.getSession().setAttribute("frmPartnerBankAcctGeneral",frmCustomerBankAcctGeneral);
			else
				request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strEmbassyDetails, mapping, request);
			else if("Partner Bank Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strPartnerbankacctdetail, mapping, request);
			else
				return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
		}// end of catch(Exception exp)
	}// end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)

	/**
	 * This method is used to navigate to previous screen when closed button is
	 * clicked. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doCloseFinance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			
			checkWebboardVisabulity(request);
			
			//setLinks(request);
			log.debug("Inside doClose method of CustomerBankAccGeneral");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			// frmCustomerBankAcctGeneral.set("flagValidate",null);
			// request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			String strBankAccountList = "bankaccountlist";
			return this.getForward(strBankAccountList, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
		}// end of catch(Exception exp)
	}// end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)

	
	
	
	/**
	 * This method is used to navigate to previous screen when closed button is
	 * clicked. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doCloseHospReview(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			
		//	checkWebboardVisabulity(request);
			this.setLinks(request,"HOSP");
			//setLinks(request);
			log.debug("Inside doCloseHospReview method of CustomerBankAccGeneral");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			// frmCustomerBankAcctGeneral.set("flagValidate",null);
			// request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			
			return this.getForward("validateAccounts", mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
		}// end of catch(Exception exp)
	}// end of doCloseHospReview(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)
	
	
	/*
	 * Close Embassy
	 */
	public ActionForward doCloseEmbassy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			
			this.setLinks(request,"EMBS");
			//setLinks(request);
			log.debug("Inside doCloseEmbassy method of CustomerBankAccGeneral");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			// frmCustomerBankAcctGeneral.set("flagValidate",null);
			// request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			
			return this.getForward("closeEmbassy", mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
		}// end of catch(Exception exp)
	}// end of doCloseHospReview(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)
	
	
	/*
	 * Save Embassy Details
	 */
	public ActionForward doSaveEmbassy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			this.setLinks(request,strSwitchType);
			String strTarget = "";
			log.info("Inside CustomerBankAcctGeneralAction doSaveEmbassy");
			CustomerBankDetailsVO customerBankDetailsVO = new CustomerBankDetailsVO();
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			String towDigitCountryCode = frmCustomerBankAcctGeneral.getString("towDigitCountryCode");
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			String strEditMode = "";
			StringBuffer strCaption = new StringBuffer();
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			customerBankDetailsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// User
			ArrayList<Object> alPopulateEmbassyInfo = new ArrayList<Object>();
			alPopulateEmbassyInfo = populateEmbassyInfo(frmCustomerBankAcctGeneral,mapping, request);
			
			boolean bFlag = true;
			if (request.getSession().getAttribute("alHospInfofinance") != null) {
				bFlag = isModified(alPopulateEmbassyInfo, (ArrayList) request.getSession().getAttribute("alHospInfofinance"));
			}// end of if(request.getSession().getAttribute("alHospInfo")!=null)
			if (bFlag == false) {
				strTarget = "referencedetail";
				frmCustomerBankAcctGeneral.set("flagValidate", "true");
				return this.getForward(strTarget, mapping, request);
			}// end of if(bFlag==false
			Long iUpdate = customerBankDetailsManager.saveEmbassyDetails(customerBankDetailsVO);
			// set the appropriate message
			if (iUpdate >0) {
				if (frmCustomerBankAcctGeneral.get("embassySeqID") != null && !frmCustomerBankAcctGeneral.get("embassySeqID").equals("")) {
					request.setAttribute("updated","message.savedSuccessfully");
					request.setAttribute("cacheId", "" + iUpdate);
					request.setAttribute("cacheDesc", customerBankDetailsVO.getEmpanalDescription());
					request.getSession().setAttribute("bankSeqId", customerBankDetailsVO.getBankSeqId());
					// finally modify the web board details, if the account no.
					// is changed
					TTKCommon.modifyWebBoardId(request);
				}// end of if(frmBankAcctGeneral.get("accountSeqID")!=null &&
				// !frmBankAcctGeneral.get("accountSeqID")
				// .equals(""))
				else {
					request.setAttribute("updated",	"message.addedSuccessfully");
				}// end of else if(frmBankAcctGeneral.get("accountSeqID")!=null
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getEmbassyAccountDetail(new Long(TTKCommon.checkNull(customerBankDetailsVO.getEmbassySeqID())));
				strEditMode = "Edit";
				strCaption.append("- Edit");
				frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmCustomerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
				frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
				frmCustomerBankAcctGeneral.set("editmode", strEditMode);
				frmCustomerBankAcctGeneral.set("reviewYN",customerBankDetailsVO.getReviewedYN());
				// keep the frmBankAcctGeneral in session scope
				frmCustomerBankAcctGeneral.set("switchType", "EMBS");
				request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			}// end of if(iUpdate > 0)
			if("Embassy Details".equals(TTKCommon.getActiveTab(request))){
				frmCustomerBankAcctGeneral.set("towDigitCountryCode",towDigitCountryCode);
				return this.getForward(strEmbassyDetails, mapping, request);}
			else
				return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}
	
	
	/**
	 * Adds the selected item to the web board and makes it as the selected item in the web board
	 * @param customerBankDetailsVO CustomerBankDetailsVO object which contain the information of BankAccount
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 */
	private void addToWebBoard(CustomerBankDetailsVO customerBankDetailsVO,
			HttpServletRequest request) {
		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		CacheObject cacheObject = new CacheObject();
		
		Long l = (Long) TTKCommon.checkNull(customerBankDetailsVO.getPolicyGroupSeqID());
		if (l.equals(null) || l.equals(" ")) {
			cacheObject.setCacheId("" + customerBankDetailsVO.getPolicySeqID());
			cacheObject.setCacheDesc(customerBankDetailsVO.getPolicyNumber());
			
		} else {
			cacheObject.setCacheId(""+customerBankDetailsVO.getPolicyGroupSeqID());
			cacheObject.setCacheDesc(customerBankDetailsVO.getPolicyNumber());
			
		}

		// cacheObject.setCacheDesc(customerBankDetailsVO.getInsureName());
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
		alCacheObject.add(cacheObject);

		toolbar.getWebBoard().addToWebBoardList(alCacheObject);

		toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());
		
		request.getSession().setAttribute("toolbar", toolbar);
		// set weboardinvoked as true to avoid change of webboard id twice in
		// same request
		request.setAttribute("webboardinvoked", "true");
	}// end of addToWebBoard(BankAccountVO bankAccountVO, HttpServletRequest
	// request)

	private void setWebBoardId(HttpServletRequest request) {
		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		
		toolbar.getWebBoard().setCurrentId("");
		toolbar.getWebBoard().addToWebBoardList(null);
		request.getSession().setAttribute("toolbar", toolbar);
	}// end of setWebBoardId(HttpServletRequest request)


	/**
	 * Populates the value object to form element.
	 * @param customerBankDetailsVO CustomerBankDetailsVO object.
	 * @param mapping The ActionMapping used to select this instance.
	 * @param request HttpServletRequest object which contains hospital information.
	 * @return DynaActionForm object.
	 */
	private void setFormValues(CustomerBankDetailsVO customerBankDetailsVO,
			ActionMapping mapping, HttpServletRequest request)
	throws TTKException {
		ArrayList<Object> alHospInfofinance = new ArrayList<Object>();
		try {
			
			// Store Hospital Bank Account Information in ArrayList, to check
			// whether any changes is made while updating
			alHospInfofinance.add(customerBankDetailsVO.getBankname());
			alHospInfofinance.add(customerBankDetailsVO.getBankAccno());
			alHospInfofinance.add(customerBankDetailsVO.getPolicyNumber());
			alHospInfofinance.add(customerBankDetailsVO.getBankBranch());
			alHospInfofinance.add(customerBankDetailsVO.getBankAccType());
			alHospInfofinance.add(customerBankDetailsVO.getMicr());
			alHospInfofinance.add(customerBankDetailsVO.getIfsc());
			alHospInfofinance.add(customerBankDetailsVO.getNeft());
			alHospInfofinance.add(customerBankDetailsVO.getBankcity());
			alHospInfofinance.add(customerBankDetailsVO.getBankState());
			alHospInfofinance.add(customerBankDetailsVO.getBankPhoneno());
			
			 request.getSession().setAttribute("alHospInfofinance",alHospInfofinance);

		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
	}// end of setFormValues(HospitalDetailVO hospitalDetailVO,ActionMapping
	// mapping,HttpServletRequest request)
	
	public void setPartnerFormValues(CustomerBankDetailsVO customerBankDetailsVO,
			ActionMapping mapping, HttpServletRequest request)
	throws TTKException {
		ArrayList<Object> alHospInfofinance = new ArrayList<Object>();
		try {
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getAccountinNameOf()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankname()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankAccno()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getPolicyNumber()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankBranch()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankAccType()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getMicr()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getIfsc()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getNeft()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankState()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBankcity()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getBranchName()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getAddress1()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getAddress2()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getAddress3()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getPinCode()));
			alHospInfofinance.add(TTKCommon.checkNull(customerBankDetailsVO.getCountryCode()));
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate=null;
			String endDate=null;
			if(customerBankDetailsVO.getStartDate()!=null)
				strDate=formatter.format(customerBankDetailsVO.getStartDate());	
			if(customerBankDetailsVO.getEndDate()!=null)
				endDate=formatter.format(customerBankDetailsVO.getEndDate());
			alHospInfofinance.add(TTKCommon.checkNull(strDate));
			alHospInfofinance.add(TTKCommon.checkNull(endDate));
			request.getSession().setAttribute("alPartnerInfofinance",alHospInfofinance);
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
	}// end of setFormValues(HospitalDetailVO hospitalDetailVO,ActionMapping
	// mapping,HttpServletRequest request)
	
	
	private void setFormEmbassyValues(CustomerBankDetailsVO customerBankDetailsVO,
			ActionMapping mapping, HttpServletRequest request)
	throws TTKException {
		ArrayList<Object> alHospInfofinance = new ArrayList<Object>();
		try {
			
			// Store Embassy Bank Account Information in ArrayList, to check
			alHospInfofinance.add(customerBankDetailsVO.getBankAccType());
			alHospInfofinance.add(customerBankDetailsVO.getBankname());
			alHospInfofinance.add(customerBankDetailsVO.getBankState());
			alHospInfofinance.add(customerBankDetailsVO.getBankcity());
			alHospInfofinance.add(customerBankDetailsVO.getBankBranch());
			alHospInfofinance.add(customerBankDetailsVO.getIfsc());
			alHospInfofinance.add(customerBankDetailsVO.getNeft());
			alHospInfofinance.add(customerBankDetailsVO.getMicr());
			alHospInfofinance.add(customerBankDetailsVO.getBankPhoneno());
			
			 request.getSession().setAttribute("alHospInfofinance",alHospInfofinance);

		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
	}// end of setFormEmbassyValues(HospitalDetailVO hospitalDetailVO,ActionMapping
	// mapping,HttpServletRequest request)

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
			//setLinks(request);
			checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AccountsAction");
			
			DynaActionForm accountForm = (DynaActionForm) form;
			AccountDetailVO accountDetailVO = null;
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			CustomerBankDetailsManager customerBankDetailsManager = this
			.getCustomerBankDetailsManagerObject();
			
			
			hmCityList = customerBankDetailsManager.getbankStateInfo();
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname")));
				
			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			frmCustomerBankAcctGeneral.set("frmChanged", "changed");
			frmCustomerBankAcctGeneral.set("alCityList", alCityList);
			
			//get the bank code based on bank code
			String bankCode	=		customerBankDetailsManager.getBankCode((String)TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname")));
			
			request.getSession().setAttribute("alCityList", alCityList);
			frmCustomerBankAcctGeneral.set("neft", bankCode);
			
			// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
			//return this.getForward(strBankacctdetail, mapping, request);
            return mapping.findForward("getSwiftcodeFromBranch");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
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
			checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AccountsAction");
			
			DynaActionForm accountForm = (DynaActionForm) form;
			AccountDetailVO accountDetailVO = null;
			HashMap hmDistList = null;
			ArrayList alDistList = null;
			ArrayList alCityCode = null;
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));
			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String towDigitCountryCode = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("towDigitCountryCode"));
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			
			hmDistList = customerBankDetailsManager.getBankCityInfo(bankState,bankName);
			if (hmDistList != null) {
				alDistList = (ArrayList) hmDistList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState")));
				
			}// end of if(hmCityList!=null)
			if (alDistList == null)
				alDistList = new ArrayList();
			// end of if(alCityList==null)
			frmCustomerBankAcctGeneral.set("frmChanged", "changed");
			frmCustomerBankAcctGeneral.set("alDistList", alDistList);
			frmCustomerBankAcctGeneral.set("towDigitCountryCode", towDigitCountryCode);
			request.getSession().setAttribute("alDistList", alDistList);
			// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strEmbassyDetails, mapping, request);
			else if("Partner Bank Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strPartnerbankacctdetail, mapping, request);
			else 
				return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
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
	//doChangeCity
	//doChangeBranch
	public ActionForward doChangeCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			//setLinks(request);
			checkWebboardVisabulity(request);
			log.debug("Inside doChangeState method of AccountsAction");
			
			DynaActionForm accountForm = (DynaActionForm) form;
			AccountDetailVO accountDetailVO = null;
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			// ArrayList alCityCode = null;
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));
			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			hmBranchList = customerBankDetailsManager.getBankBranchtInfo(bankState,bankName, bankDistict);
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity")));
				
			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			frmCustomerBankAcctGeneral.set("frmChanged", "changed");
			frmCustomerBankAcctGeneral.set("alBranchList", alBranchList);
			request.getSession().setAttribute("alBranchList", alBranchList);
			// frmCustomerBankAcctGeneral.set("alCityCode",alCityCode);
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strEmbassyDetails, mapping, request);
			else
				return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
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
			checkWebboardVisabulity(request);
			log.debug("Inside doChangeBranch method of AccountsAction");
		
			DynaActionForm accountForm = (DynaActionForm) form;
			AccountDetailVO accountDetailVO = null;
			HashMap hmBranchList = null;
			ArrayList alBranchList = null;
			// ArrayList alCityCode = null;
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			CustomerBankDetailsVO customerBankDetailsVO = null;
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils
			.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			// customerBankDetailsVO=bankAcctObject.getPolicyBankAccountDetail(TTKCommon.getWebBoardId(request),TTKCommon.getUserSeqId(request));
			// TTKCommon.getWebBoardId(request),

			String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));
			String bankName = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankcity"));
			String bankBranch = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankBranch"));
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			
			if(request.getParameter("stopPreAuthsYN") != null && request.getParameter("stopPreAuthsYN").equals("Y")){
				accountForm.set("stopPreAuthsYN", "Y");
			}else{
				accountForm.set("stopPreAuthsYN", "N");
			}
			
			if(request.getParameter("stopClaimsYN") != null && request.getParameter("stopClaimsYN").equals("Y")){
				accountForm.set("stopClaimsYN", "Y");
			}else{
				accountForm.set("stopClaimsYN", "N");
			}
			//customerBankDetailsVO = customerBankDetailsManager.getBankIfscInfo(bankState, bankName, bankDistict, bankBranch);
			customerBankDetailsVO	= customerBankDetailsManager.getBankIfscInfoOnBankName(bankName);
			
			if (hmBranchList != null) {
				alBranchList = (ArrayList) hmBranchList.get(TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankBranch")));
				
			}// end of if(hmCityList!=null)
			if (alBranchList == null) {
				alBranchList = new ArrayList();
			}// end of if(alCityList==null)
			String micr = TTKCommon.checkNull(customerBankDetailsVO.getMicr());
			String ifsc = TTKCommon.checkNull(customerBankDetailsVO.getIfsc());
			
			//request.getSession().setAttribute("micr", micr);
			request.getSession().setAttribute("ifsc", ifsc);
			frmCustomerBankAcctGeneral.set("frmChanged", "changed");
			//frmCustomerBankAcctGeneral.set("micr", micr);
			frmCustomerBankAcctGeneral.set("ifsc", ifsc);
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strEmbassyDetails, mapping, request);
			else
				return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
		}// end of catch(Exception exp)
	}// end of doChangeState(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * Add the required account information to arrayList to check whether any changes are made
	 * @param accountForm DynaActionForm object to which contain the accounts information.
	 * @param request HttpServletRequest object which contains accounts information.
	 * @param mapping The ActionMapping used to select this instance
	 * @param request The HTTP request we are processing
	 * @return alAccountInfoNew ArrayList which contian accounts information.
	 */
	private ArrayList<Object> populateAccountInfo(DynaActionForm frmCustomerBankAcctGeneral, ActionMapping mapping,HttpServletRequest request) throws TTKException {
		ArrayList<Object> alAccountInfoNew = new ArrayList<Object>();
		AddressVO bankAddressVO = null;
		// ActionForm
		// bankAddressForm=(ActionForm)accountForm.get("bankAddressVO");
		try {
			// bankAddressVO=(AddressVO)FormUtils.getFormValues(bankAddressForm,"frmBankAddress",this,mapping,request);
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankname"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankAccno"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("policyNumber"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankBranch"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankAccType"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("micr"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("ifsc"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("neft"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankcity"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankState"));
			alAccountInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankPhoneno"));
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
		return alAccountInfoNew;
	}// end of populateAccountInfo(HttpServletRequest request,DynaActionForm
	// accountForm)

	
	private ArrayList<Object> populateEmbassyInfo(DynaActionForm frmCustomerBankAcctGeneral, ActionMapping mapping,HttpServletRequest request) throws TTKException {
		ArrayList<Object> aEmbassyInfoNew = new ArrayList<Object>();
		AddressVO bankAddressVO = null;
		try {
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankAccType"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankname"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankState"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankcity"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankBranch"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("ifsc"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("neft"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("micr"));
			aEmbassyInfoNew.add((String) frmCustomerBankAcctGeneral.getString("bankPhoneno"));
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
		return aEmbassyInfoNew;
	}// end of populateEmbassyInfo(HttpServletRequest request,DynaActionForm
	// accountForm)
	
	/**
	 * Populates the form element to value object .
	 * @param customerBankDetailsVO CustomerBankDetailsVO object.
	 * @param mapping The ActionMapping used to select this instance.
	 * @param request HttpServletRequest object which contains hospital information.
	 * @return hospDetailVO HospitalDetailVO object.
	 */
	private CustomerBankDetailsVO getFormValues(DynaActionForm frmCustomerBankAcctGeneral, ActionMapping mapping,HttpServletRequest request) throws TTKException {
		try {
			//setLinks(request);
			CustomerBankDetailsVO customerBankDetailsVO = null;

			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, "frmCustomerBankAcctGeneral",
					this, mapping, request);
			customerBankDetailsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// //User
			// Id
			return customerBankDetailsVO;
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, strAccountsError);
		}// end of catch
	}// end of getFormValues(DynaActionForm generalForm,ActionMapping
	// mapping,HttpServletRequest request)

	/**
	 * Returns the boolean value by comparing the two arraylist.
	 * @param alHospInfoNew ArrayList which contains account new information.
	 * @param alHospInfoOld ArrayList which contains the account old information.
	 * @return false when change is found when comparing the two ArrayList else return true
	 */
	private boolean isModified(ArrayList alHospInfoNew, ArrayList alHospInfoOld) {
		for (int i = 0; i < alHospInfoOld.size(); i++) {
			if (!TTKCommon.checkNull(alHospInfoNew.get(i)).equals(TTKCommon.checkNull(alHospInfoOld.get(i))))
				return false;

		}// end of for(int i=0;i<alHospInfoOld.size();i++)
		return true;
	}// end of isModified(ArrayList alHospInfoNew,ArrayList alHospInfoOld)

	/**
	 * Returns the CustomerBankDetailsManager session object for invoking methods on it.
	 * @return CustomerBankDetailsManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private CustomerBankDetailsManager getCustomerBankDetailsManagerObject()
	throws TTKException {
		CustomerBankDetailsManager customerBankDetailsManager = null;
		try {
			if (customerBankDetailsManager == null) {
				InitialContext ctx = new InitialContext();
				customerBankDetailsManager = (CustomerBankDetailsManager) ctx.lookup("java:global/TTKServices/business.ejb3/CustomerBankDetailsManagerBean!com.ttk.business.finance.CustomerBankDetailsManager");
			}// end if
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, strAccountsError);
		}// end of catch
		return customerBankDetailsManager;
	}// end getHospitalManagerObject()
	/**
	 * this method update webboard visabulity after request
	 * @exception throws TTKException
	 */
  private void checkWebboardVisabulity(HttpServletRequest request)throws TTKException
  {
	   TableData tableData = (TableData)request.getSession().getAttribute("tableData");
		HttpSession session=request.getSession();
		String strSwitchType="";
		DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
		strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
		if(strSwitchType.equals("HOSP"))
		{
		
		}
		else
		{
		if(((Column)((ArrayList)tableData.getTitle()).get(1)).isLink())
		{
			strSwitchType=strSwitchType.concat("Mem");
		}
		else if(((Column)((ArrayList)tableData.getTitle()).get(0)).isLink())
			{
				strSwitchType=strSwitchType.concat("Polc");
			}
		else if(((Column)((ArrayList)tableData.getTitle()).get(0)).isLink() && strSwitchType.equals("PTNR"))
		{
			strSwitchType=strSwitchType.concat("").trim();
		}
		}//else
		this.setLinks(request,strSwitchType);
  }//if checkWebboardVisabulity
  
  public ActionForward doViewPartnerBankDetails(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			
			//setLinks(request);
			log.debug("Inside CustmerBankAccGeneral doViewHospBankAccount");
			DynaActionForm frmPartnerBankAcctGeneral = (DynaActionForm) form;
			StringBuffer strCaption = new StringBuffer();
			String strEditMode = "";
			CustomerBankDetailsVO customerBankDetailsVO = null;
			String strSwitchType="PTNR";
			this.setLinks(request,strSwitchType);
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmPartnerBankAcctGeneral, this, mapping, request);
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			frmPartnerBankAcctGeneral.initialize(mapping);
			
			
		//	System.out.println(" request.getParameter(webBoard) "+ request.getParameter("webBoard"));
		//	Long webpartnerSeqId =	TTKCommon.getWebBoardId(request);
			
			if (TTKCommon.getWebBoardId(request)== null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.partner.required");
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request) == null)
			
		//	String partnerSeqId =  (String) request.getSession().getAttribute("partnerSeqId");
			
		    
			strCaption.append("- Edit");
			strEditMode = "Edit";

			customerBankDetailsVO = customerBankDetailsManager.getPartnerBankDetailAccounts(String.valueOf(TTKCommon.getWebBoardId(request)));
			setPartnerFormValues(customerBankDetailsVO, mapping, request);
			
			//	setFormValues(customerBankDetailsVO, mapping, request);
        //    customerBankDetailsVO.setPartnerSeqId(partnerSeqId);
			frmPartnerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmPartnerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
			
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;
			String bankName = (String) TTKCommon.checkNull(frmPartnerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmPartnerBankAcctGeneral.get("bankcity"));
			String statecode = (String)frmPartnerBankAcctGeneral.get("bankState");
			hmCityList = customerBankDetailsManager.getCityInfo(statecode);
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmPartnerBankAcctGeneral.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;

		//	String bankState = (String) TTKCommon.checkNull(frmCustomerBankAcctGeneral.get("bankState"));
			String cCode = customerBankDetailsManager.getCountryCode(frmPartnerBankAcctGeneral.getString("countryCode"));
			frmPartnerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			request.getSession().setAttribute("partnerSeqId", customerBankDetailsVO.getPtnrSeqID());
//			request.getSession().setAttribute("bankSeqId", customerBankDetailsVO.getBankSeqId());
			frmPartnerBankAcctGeneral.set("editmode", strEditMode);
			frmPartnerBankAcctGeneral.set("pinCode", customerBankDetailsVO.getPinCode());
			frmPartnerBankAcctGeneral.set("caption", strCaption.toString());
			frmPartnerBankAcctGeneral.set("partnerSeqId", String.valueOf(TTKCommon.getWebBoardId(request)));
			frmPartnerBankAcctGeneral.set("towDigitCountryCode",cCode );
			request.getSession().setAttribute("switchType", strSwitchType);
			request.getSession().setAttribute("isAccessable", true);
			// keep the frmBankAcctGeneral in session scope
			request.getSession().setAttribute("frmPartnerBankAcctGeneral",
					frmPartnerBankAcctGeneral);
			return this.getForward(strPartnerbankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPartnerbankacctdetail));
		}// end of catch(Exception exp)
	}
  
public ActionForward doChangeAreaForPartner(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   HttpServletResponse response) throws Exception{
	try{
		setLinks(request);
		log.debug("Inside doChangeState method of AccountsAction");
		DynaActionForm accountForm = (DynaActionForm)form;
		AccountDetailVO accountDetailVO=null;
		HashMap hmCityList = null;
		ArrayList alCityList = null;
		ArrayList alCityCode = null;
		HashMap hmDistList = null;
		ArrayList alDistList = null;
		PartnerManager partnerObject=this.getPartnerManagerObject();

		DynaActionForm frmPartnerBankAcctGeneral = (DynaActionForm) form;
	
		String bankState = (String) TTKCommon.checkNull(frmPartnerBankAcctGeneral.get("bankState"));
		
		CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();

		hmCityList=partnerObject.getCityInfo(bankState);

		String countryCode	=	"";

		if(hmCityList!=null)
		{
			countryCode	= (String)(hmCityList.get("CountryId"));
			alCityList = (ArrayList)hmCityList.get(bankState);
		}//end of if(hmCityList!=null)
		if(alCityList==null)
		{
			alCityList=new ArrayList();
		}//end of if(alCityList==null)
		if(alCityCode==null){
			alCityCode=new ArrayList();
		}
	//	frmPartnerBankAccountList.set("partnerSeqId", partnerSeqId);
		String cCode = customerBankDetailsManager.getCountryCode(countryCode);
		frmPartnerBankAcctGeneral.set("towDigitCountryCode",cCode);	
		frmPartnerBankAcctGeneral.set("countryCode",countryCode);
		frmPartnerBankAcctGeneral.set("frmChanged","changed");
		frmPartnerBankAcctGeneral.set("alCityList",alCityList);
		request.getSession().setAttribute("alCityList", alCityList);
		request.getSession().setAttribute("alCityCode", alCityCode);

		request.getSession().setAttribute("frmPartnerBankAcctGeneral",frmPartnerBankAcctGeneral);

   return this.getForward(strPartnerbankacctdetail, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request, mapping, new TTKException(exp, strAccountsError));
	}//end of catch(Exception exp)
	}
  
  private PartnerManager getPartnerManagerObject() throws TTKException
  {
      PartnerManager ptnrManager = null;
      try
      {
          if(ptnrManager == null)
          {
              InitialContext ctx = new InitialContext();
              ptnrManager = (PartnerManager) ctx.lookup("java:global/TTKServices/business.ejb3/PartnerManagerBean!com.ttk.business.empanelment.PartnerManager");
          }//end if(hospManager == null)
      }//end of try
      catch(Exception exp)
      {
          throw new TTKException(exp,strAccountsError);
      }//end of catch
      return ptnrManager;
  }//end getHospitalManagerObject()
  
  
  public ActionForward doSavePartner(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			this.setLinks(request,strSwitchType);
			String strTarget = "";
			log.info("Inside CustomerBankAcctGeneralAction doSavePartner");
			CustomerBankDetailsVO customerBankDetailsVO = new CustomerBankDetailsVO();
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			String towDigitCountryCode = frmCustomerBankAcctGeneral.getString("towDigitCountryCode");
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			String strEditMode = "";
			StringBuffer strCaption = new StringBuffer();
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmCustomerBankAcctGeneral, this, mapping, request);
			customerBankDetailsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// User
			ArrayList<Object> alModifiedHospInfo = new ArrayList<Object>();
			alModifiedHospInfo = populatePartnerInfo(frmCustomerBankAcctGeneral,mapping, request);
			boolean bFlag = true;
			// call the checkDifference method to check whether any change is
			// made in the bank information
			if (request.getSession().getAttribute("alPartnerInfofinance") != null) {
				bFlag = isModified(alModifiedHospInfo, (ArrayList) request.getSession().getAttribute("alPartnerInfofinance"));	
			}// end of if(request.getSession().getAttribute("alHospInfo")!=null)
			if (bFlag == false) {
				strTarget = "referencedetail";
				frmCustomerBankAcctGeneral.set("flagValidate", "true");
				frmCustomerBankAcctGeneral.set("switchType", "PTNR");
				request.getSession().setAttribute("remarksRequired", "true");
				request.getSession().setAttribute("frmPartnerBankAcctGeneral",frmCustomerBankAcctGeneral);
				
				return this.getForward(strTarget, mapping, request);
			}// end of if(bFlag==false)
			
			String value = (String) frmCustomerBankAcctGeneral.getString("partnerSeqId");
			Long partnerSeqId= Long.valueOf(value);
//			customerBankDetailsVO.setPtnrSeqID(customerBankDetailsVO.getPtnrSeqID());
			customerBankDetailsVO.setPartnerName(frmCustomerBankAcctGeneral.getString("partnerName"));
			customerBankDetailsVO.setPartnerEmnalNumber(frmCustomerBankAcctGeneral.getString("sEmpanelmentNo"));
		
			Long iUpdate = customerBankDetailsManager.savePartnerDetails(customerBankDetailsVO);
			// set the appropriate message
			if (iUpdate >0) {
				if (frmCustomerBankAcctGeneral.get("partnerSeqId") != null && !frmCustomerBankAcctGeneral.get("partnerSeqId").equals("")) {
					request.setAttribute("updated","message.savedSuccessfully");
					request.setAttribute("cacheId", "" + iUpdate);
					request.setAttribute("cacheDesc", customerBankDetailsVO.getPartnerEmpnalDesc());
					request.getSession().setAttribute("bankSeqId", customerBankDetailsVO.getBankSeqId());
				}// end of if(frmBankAcctGeneral.get("accountSeqID")!=null &&
				// !frmBankAcctGeneral.get("accountSeqID")
				else {
					request.setAttribute("updated",	"message.addedSuccessfully");
				}// end of else if(frmBankAcctGeneral.get("accountSeqID")!=null
				frmCustomerBankAcctGeneral.initialize(mapping);
				customerBankDetailsVO = customerBankDetailsManager.getPartnerBankDetail(partnerSeqId);
				/*customerBankDetailsVO = customerBankDetailsManager.getPartnerBankDetailAccounts( TTKCommon.checkNull(customerBankDetailsVO.getPartnerSeqId()));*/
				strEditMode = "Edit";
				strCaption.append("- Edit");
				frmCustomerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmPartnerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
				frmCustomerBankAcctGeneral.set("caption", strCaption.toString());
				frmCustomerBankAcctGeneral.set("editmode", strEditMode);
				frmCustomerBankAcctGeneral.set("partnerSeqId", String.valueOf(customerBankDetailsVO.getPtnrSeqID()));
				frmCustomerBankAcctGeneral.set("reviewYN",customerBankDetailsVO.getReviewedYN());
				frmCustomerBankAcctGeneral.set("towDigitCountryCode",towDigitCountryCode);
				// keep the frmBankAcctGeneral in session scope
				frmCustomerBankAcctGeneral.set("switchType", "PTNR");
				request.getSession().setAttribute("frmPartnerBankAcctGeneral",frmCustomerBankAcctGeneral);
				
			}// end of if(iUpdate > 0)
				return this.getForward(strPartnerbankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}// end of catch(Exception exp)
	}
  
	private ArrayList<Object> populatePartnerInfo(
		DynaActionForm frmPartnerBankAcctGeneral, ActionMapping mapping,
		HttpServletRequest request) throws TTKException {
		ArrayList<Object> alAccountInfoNew = new ArrayList<Object>();
		try {
			// bankAddressVO=(AddressVO)FormUtils.getFormValues(bankAddressForm,"frmBankAddress",this,mapping,request);
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("accountinNameOf")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("bankname")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("bankAccno")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("policyNumber")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("bankBranch")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("bankAccType")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("micr")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("ifsc")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("neft")));
			
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("bankState")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("bankcity")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("branchName")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("address1")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("address2")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("address3")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("pinCode")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("countryCode")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("startDate")));
			alAccountInfoNew.add(TTKCommon.checkNull((String) frmPartnerBankAcctGeneral.getString("endDate")));
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, "");
		}// end of catch
		return alAccountInfoNew;
}
	public ActionForward doClosePartner(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			
			this.setLinks(request,"PTNR");
			//setLinks(request);
			log.debug("Inside doClosePartner method of CustomerBankAccGeneral");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			
			return this.getForward("closePartner", mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPartnerbankacctdetail));
		}// end of catch(Exception exp)
	}// end of doCloseHospReview(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)
	
	
	
	public ActionForward doResetForPartner(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			HttpSession session=request.getSession();
			String strSwitchType="";
			DynaActionForm generalForm1=(DynaActionForm)session.getAttribute("frmCustomerBankDetailsSearch");
			strSwitchType=TTKCommon.checkNull((String)generalForm1.get("switchType"));
			this.setLinks(request,strSwitchType);
			log.debug("Inside BankAccountGeneral doReset");
			DynaActionForm frmPartnerBankAcctGeneral = (DynaActionForm) form;
			CustomerBankDetailsVO customerBankDetailsVO = new CustomerBankDetailsVO();
			customerBankDetailsVO = (CustomerBankDetailsVO) FormUtils.getFormValues(frmPartnerBankAcctGeneral, this, mapping, request);
			frmPartnerBankAcctGeneral.initialize(mapping);
			StringBuffer strCaption = new StringBuffer();
			// CustomerBankDetailsVO customerBankDetailsVO=null;
			String strEditMode = "";
			// BankAccountManager
			// bankAcctObject=this.getBankAccountManagerObject();
			CustomerBankDetailsManager customerBankDetailsManager = this
			.getCustomerBankDetailsManagerObject();

			strCaption.append("- Edit");
			
			
			/*
			 * bankAccountDetailVO=bankAcctObject.getBankAccountDetail(TTKCommon.
			 * getWebBoardId(request), TTKCommon.getUserSeqId(request));
			 */
			if (!TTKCommon.checkNull(customerBankDetailsVO.getPartnerSeqId()).equals("")) {
				customerBankDetailsVO = customerBankDetailsManager.getPartnerBankDetailAccounts(customerBankDetailsVO.getPartnerSeqId());
			}

			

			frmPartnerBankAcctGeneral = (DynaActionForm) FormUtils.setFormValues("frmPartnerBankAcctGeneral", customerBankDetailsVO, this,mapping, request);
			
			HashMap hmCityList = null;
			ArrayList alCityList = null;
			ArrayList alCityCode = null;
			String bankName = (String) TTKCommon.checkNull(frmPartnerBankAcctGeneral.get("bankname"));
			String bankDistict = (String) TTKCommon.checkNull(frmPartnerBankAcctGeneral.get("bankcity"));
			String statecode = (String)frmPartnerBankAcctGeneral.get("bankState");
			hmCityList = customerBankDetailsManager.getCityInfo(statecode);
			if (hmCityList != null) {
				alCityList = (ArrayList) hmCityList.get(TTKCommon.checkNull(frmPartnerBankAcctGeneral.get("bankState")));

			}// end of if(hmCityList!=null)
			if (alCityList == null) {
				alCityList = new ArrayList();
			}// end of if(alCityList==null)
			HashMap hmDistList = null;
			ArrayList alDistList = null;
			
			
		
			
			
			frmPartnerBankAcctGeneral.set("alCityList", alCityList);
			request.getSession().setAttribute("alCityList", alCityList);
			frmPartnerBankAcctGeneral.set("caption", strCaption.toString());
			frmPartnerBankAcctGeneral.set("editmode", strEditMode);
			frmPartnerBankAcctGeneral.set("partnerSeqId", String.valueOf(customerBankDetailsVO.getPtnrSeqID()));
			frmPartnerBankAcctGeneral.set("reviewYN",customerBankDetailsVO.getReviewedYN());
			// keep the frmBankAcctGeneral in session scope
			frmPartnerBankAcctGeneral.set("switchType", "PTNR");
			request.getSession().setAttribute("frmPartnerBankAcctGeneral",
					frmPartnerBankAcctGeneral);
			
			return this.getForward(strPartnerbankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPartnerbankacctdetail));
		}// end of catch(Exception exp)
	}// end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)
	
	
	
	
	public ActionForward doChangeQatarYN(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		try {
			
			checkWebboardVisabulity(request);
			log.debug("Inside doChangeQatarYN method of AccountsAction");
			HttpSession session = request.getSession();
			DynaActionForm accountForm = (DynaActionForm) form;
			String qatarYN = accountForm.getString("bankAccountQatarYN");
			
			String mode = request.getParameter("reforward");
			accountForm.set("bankAccountQatarYN", qatarYN);
			accountForm.set("bankname", "");
			accountForm.set("bankAccno", "");
			accountForm.set("bankAccType", "");
			accountForm.set("micr", "");
			accountForm.set("bankState", "");
			accountForm.set("bankState", "");
			accountForm.set("bankState", "");
			accountForm.set("bankcity", "");
			accountForm.set("bankBranchText", "");
			accountForm.set("ifsc", "");
			accountForm.set("bankBranch", "");
			accountForm.set("neft", "");
			accountForm.set("bankBranchText", "");
			accountForm.set("bankPhoneno", "");
			accountForm.set("emailID", "");
			accountForm.set("address1", "");
			accountForm.set("branchName", "");
			accountForm.set("address2", "");
			accountForm.set("address3", "");
			accountForm.set("pinCode", "");
			accountForm.set("startDate", "");
			accountForm.set("endDate", "");
			if("N".equals(qatarYN)){
				accountForm.set("countryCode", "");
				accountForm.set("towDigitCountryCode", "");
			}
			else{
				accountForm.set("countryCode", "134");
				accountForm.set("towDigitCountryCode", "QA");
			}
			session.setAttribute("frmCustomerBankAcctGeneral", accountForm);
			
			if("EMBSY".equals(mode)){
				
				return this.getForward(strEmbassyDetails, mapping, request);
			}
			else if("PAT".equals(mode)){
				
				return this.getForward(strPartnerbankacctdetail, mapping, request);
			}
			else{
				return this.getForward(strBankacctdetail, mapping, request);
			}
			
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAccountsError));
		}// end of catch(Exception exp)
		
	}	
	
	
	public ActionForward doGetCountryCode(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		try 
		{
			log.debug("Inside CustomerBankAcctGeneralAction : doGetCountryCode()");
			DynaActionForm frmCustomerBankAcctGeneral = (DynaActionForm) form;
			String countryCode = request.getParameter("countryCode");
			
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			String cCode = customerBankDetailsManager.getCountryCode(countryCode);
			frmCustomerBankAcctGeneral.set("towDigitCountryCode",cCode);	
			request.getSession().setAttribute("frmCustomerBankAcctGeneral",frmCustomerBankAcctGeneral);
			if("Embassy Details".equals(TTKCommon.getActiveTab(request)))
				return this.getForward(strEmbassyDetails, mapping, request);
			else
			return this.getForward(strBankacctdetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}
	}// end of doGetCountryCode()
	
	public ActionForward doGetCountryCodeForPartner(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		try 
		{
			log.debug("Inside CustomerBankAcctGeneralAction : doGetCountryCodeForPartner()");
			DynaActionForm frmPartnerBankAcctGeneral = (DynaActionForm) form;
			String countryCode = request.getParameter("countryCode");
			CustomerBankDetailsManager customerBankDetailsManager = this.getCustomerBankDetailsManagerObject();
			String cCode = customerBankDetailsManager.getCountryCode(countryCode);
			frmPartnerBankAcctGeneral.set("towDigitCountryCode",cCode);	
			return this.getForward(strPartnerbankacctdetail, mapping, request);	
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strBankAcctDetails));
		}
	}// end of doGetCountryCodeForPartner()
}	
	

