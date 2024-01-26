 /* 
 * @ (#) PreAuthGeneralAction.java May 10, 2006
 * Project       : TTK HealthCare Services
 * File          : PreAuthGeneralAction.java
 * Author        : Srikanth H M
 * Company       : Span Systems Corporation
 * Date Created  : May 10, 2006
 *
 * @author       : Srikanth H M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.claims;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.dom4j.Document;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimManager;
import com.ttk.business.common.messageservices.CommunicationManager;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.business.onlineforms.OnlinePreAuthManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.business.reports.TTKReportManager;
import com.ttk.common.ClaimsWebBoardHelper;
import com.ttk.common.PreAuthWebBoardHelper;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.EventVO;
import com.ttk.dto.administration.ReportVO;
import com.ttk.dto.administration.WorkflowVO;
import com.ttk.dto.claims.ClaimDetailVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.AdditionalDetailVO;
import com.ttk.dto.preauth.ClaimantVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.ObservationDetailsVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthHospitalVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.ShortfallVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

import org.apache.commons.codec.binary.Base64;

import javax.xml.bind.DatatypeConverter;


/**
 * This class is reused for adding pre-auth/claims in pre-auth and claims flow.
 */

public class ClaimGeneralAction extends TTKAction {

	private static Logger log = Logger.getLogger(ClaimGeneralAction.class);

	private static final String strClaimDetail = "ClaimDetails";
	private static final String strClaimdetails = "claimdetails";

	//	declare forward paths

	private static final String strPre_Authorization = "Pre-Authorization";
	private static final String strClaims = "Claims";

	private static final String strActivitydetails = "activitydetails";
	private static final String strActivityDetails = "ActivityDetails";
	private static final String strAuthorizationError = "authorizations";
	private static final String strClaimViewHistory = "claimViewHistory";
	private static final String strCLaimHistoryList = "claimHistoryList";
	private static final String strPreAuthViewHistory = "preauthViewHistory";
	private static final String strDisplayOfBenefits = "displayOfBenefits";
    
	private static final String strFraudInternalRemarks = "fraudInternalRemarks";
	private static final String strDefaultClaimReports = "defaultClaimReports";
	private static final String strSearchtClaimReports = "searchClaimReports";
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	private static final String strReportdisplay="reportdisplay";
	private static final String strFraudHistory = "fraudHistory";
	private static final String StrClaimsDiscountActReportList = "claimsDiscountActReportList";
	private static final String StrClaimsDiscountActReport = "claimsDiscountActReport";
	
	/**
	 * This method is used to navigate to detail screen to edit selected record.
	 * Finally it forwards to the appropriate view based on the specified
	 * forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws IOException 
	 * @throws Exception
	 *             if any error occurs
	 */
	
	public ActionForward doView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.info("Inside ClaimGeneralAction doView ");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			HttpSession session = request.getSession();
			ClaimManager claimObject = this.getClaimManagerObject();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			session.setAttribute("partnersList", preAuthObject.getPartnersList());
			PreAuthDetailVO preAuthDetailVO = null;
			StringBuffer strCaption = new StringBuffer();
			strCaption.append(" Edit");
			// check if user trying to hit the tab directly with out selecting
			// the pre-auth
			if (ClaimsWebBoardHelper.checkWebBoardId(request) == null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.Claims.required");
				session.setAttribute("claimDiagnosis", null);
				session.setAttribute("claimActivities", null);
				session.setAttribute("claimShortfalls", null);
				frmClaimGeneral.initialize(mapping);
				
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
				// throw expTTK;
				request.setAttribute("errorMsg", "Please Select Claim Details");
				return this.getForward(strClaimdetails, mapping, request);
			}
			long claimSeqID = ClaimsWebBoardHelper.getClaimsSeqId(request);
				// call the business layer to get the Claim detail
			Object[] claimResults = claimObject.getClaimDetails(claimSeqID);
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			preAuthDetailVO = preAuthDetailVO == null ? new PreAuthDetailVO()
					: preAuthDetailVO;
				preAuthDetailVO.setClaimSeqID(claimSeqID);
				request.getSession().setAttribute("preauthDetail", preAuthDetailVO);
			/*	if("CTM".equals(preAuthDetailVO.getClaimType())){
					if(preAuthDetailVO.getProviderCountry()==null){
						preAuthDetailVO.setProviderCountry("134");
					}
				}*/
			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
			session.setAttribute("suspectedYesorNOFlag", preAuthDetailVO.getSuspectedYesorNOFlag());
			session.setAttribute("completedYN", preAuthDetailVO.getCompletedYNFlag());
			session.setAttribute("OfficeSeqId", preAuthDetailVO.getOfficeSeqID());
			
			ArrayList<String> alProvicerCopay	=	preAuthObject.getProviderSpecificCopay(new Long(claimSeqID),"CLM");
			ArrayList<String[]> overRideAlertList	=	preAuthObject.getOverrideRemarksList("C",new Long(claimSeqID));
			if(alProvicerCopay.size()>0){
				preAuthDetailVO.setProviderCopay(alProvicerCopay.get(0));
				preAuthDetailVO.setProviderDeductible(alProvicerCopay.get(1));
				preAuthDetailVO.setProviderSuffix(alProvicerCopay.get(2));
			}
			session.setAttribute("alProvicerCopay", alProvicerCopay);
			session.setAttribute("overRideAlertList", overRideAlertList);

			/*frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);*/
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			
			if ("N".equals(frmClaimGeneral.getString("networkProviderType"))||"CTM".equals(preAuthDetailVO.getClaimType())) {
				session.setAttribute("providerStates",
						TTKCommon.getStates(frmClaimGeneral
								.getString("providerCountry")));
				session.setAttribute("providerAreas", TTKCommon
						.getAreas(frmClaimGeneral.getString("providerEmirate")));
				  }
			session.setAttribute("encounterTypes",
					preAuthObject.getEncounterTypes(frmClaimGeneral
							.getString("benefitType")));

			frmClaimGeneral.set("caption", strCaption.toString());
			frmClaimGeneral.set("completedYN", preAuthDetailVO.getCompletedYNFlag());
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			
			//CLAIM SETTLEMENT DETAILS S T A R T S
			String[] strClaimSettlementDtls	=	claimObject.getClaimSettleMentDtls(claimSeqID);
			request.getSession().setAttribute("strClaimSettlementDtls", strClaimSettlementDtls);
			request.getSession().setAttribute("vishwa", strClaimSettlementDtls[5]);
			//CLAIM SETTLEMENT DETAILS E N D S
			request.getSession().setAttribute("auditStatus",preAuthDetailVO.getAuditStatus());
			request.getSession().setAttribute("clmSeqId", claimSeqID);
			request.getSession().setAttribute("frmClaimGeneral",
					frmClaimGeneral);
			if("INP".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "In Progress");	
			}else if("APR".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Approved");
			}else if("REJ".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Rejected");
			}else if("PCN".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Cancel");
			}else if("REQ".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Required Information");
			}else{
				request.getSession().setAttribute("claimStatus", preAuthDetailVO.getClaimStatus());
			}
			frmClaimGeneral.set("claimVerifiedforClaim", preAuthDetailVO.getClaimVerifiedforClaim());
			frmClaimGeneral.set("referExceptionFalg", preAuthDetailVO.getReferExceptionFalg());
			
			request.getSession().setAttribute("claimVerifiedforClaim", preAuthDetailVO.getClaimVerifiedforClaim());
			request.getSession().setAttribute("vipmemberuseraccesspermissionflag", preAuthDetailVO.getVipMemberUserAccessPermissionFlag());
			request.getSession().setAttribute("assigntouserseqid", preAuthDetailVO.getAssignUserSeqID());
			request.getSession().setAttribute("ACTIVELINK", TTKCommon.getActiveLink(request));
			request.getSession().setAttribute("claimOrPreauthNumber", preAuthDetailVO.getClaimNo());
			request.getSession().removeAttribute("preAuthStatus");
			session.setAttribute("overrideSuspectFlag", preAuthDetailVO.getOverrideSuspectFlag());
			
			this.documentViewer(request, preAuthDetailVO);
			return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)

	/**
	 * This method is used to get the details of the selected record from
	 * web-board. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
	 */
	public ActionForward doChangeWebBoard(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.debug("Inside ClaimGeneralAction doChangeWebBoard");
		return doView(mapping, form, request, response);
	}// end of doChangeWebBoard(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,
		// HttpServletResponse response)

	/**
	 * This method is called from the struts framework. This method is used to
	 * navigate to detail screen to add a record. Finally it forwards to the
	 * appropriate view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward viewHistory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction viewHistory ");
			DynaActionForm frmPreAuthGeneral = (DynaActionForm) form;
			UserSecurityProfile userSecurityProfile = (UserSecurityProfile) request
					.getSession().getAttribute("UserSecurityProfile");
			ArrayList alPrevClaimList = null;
			PreAuthDetailVO preAuthDetailVO = null;
			PreAuthHospitalVO preAuthHospitalVO = null;
			AdditionalDetailVO additionalDetailVO = null;
			ClaimantVO claimantVO = null;
			ClaimDetailVO claimDetailVO = null;
			StringBuffer strCaption = new StringBuffer();
			String strActiveTab = TTKCommon.getActiveLink(request);
			String strFlowType = "";
			String strDetail = "";
			String strPreAuthFlow = "PRE";
			String strClaimFlow = "CLM";

			if (strActiveTab.equals(strPre_Authorization)) {
				strFlowType = strPreAuthFlow;
				strDetail = "preauthdetail";
			}// end of if(strActiveTab.equals(strPre_Authorization))
			if (strActiveTab.equals(strClaims)) {
				strFlowType = strClaimFlow;
				strDetail = "claimsdetail";
			}// end of if(strActiveTab.equals(strClaims))
			preAuthDetailVO = new PreAuthDetailVO();
			preAuthHospitalVO = new PreAuthHospitalVO();
			additionalDetailVO = new AdditionalDetailVO();
			claimantVO = new ClaimantVO();
			preAuthDetailVO.setDiscPresentYN("N");
			preAuthDetailVO.setCompletedYN("N");
			preAuthDetailVO.setPreAuthTypeID("MAN");// from add mode pre-auth
													// type will be manual.
			preAuthDetailVO.setPreAuthRecvTypeID("FAX");// default is fax
			preAuthDetailVO.setPreAuthHospitalVO(preAuthHospitalVO);
			preAuthDetailVO.setAdditionalDetailVO(additionalDetailVO);
			preAuthDetailVO.setClaimantVO(claimantVO);
			preAuthDetailVO.setPriorityTypeID("MID");// from add mode priority
														// will be medium.
			frmPreAuthGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmPreAuthGeneral", preAuthDetailVO, this, mapping,
					request);
			frmPreAuthGeneral = setFormValues(preAuthDetailVO,mapping,request);
			frmPreAuthGeneral.set("preAuthHospitalVO", FormUtils.setFormValues(
					"frmPreAuthHospital",
					preAuthDetailVO.getPreAuthHospitalVO(), this, mapping,
					request));
			frmPreAuthGeneral.set("claimantDetailsVO", FormUtils.setFormValues(
					"frmClaimantDetails", preAuthDetailVO.getClaimantVO(),
					this, mapping, request));
			frmPreAuthGeneral.set("additionalDetailVO", FormUtils
					.setFormValues("frmAdditionalDetail",
							preAuthDetailVO.getAdditionalDetailVO(), this,
							mapping, request));
			claimDetailVO = new ClaimDetailVO();
			frmPreAuthGeneral.set("claimDetailVO", FormUtils.setFormValues(
					"frmClaimDetail", claimDetailVO, this, mapping, request));
			frmPreAuthGeneral.set("preAuthTypeDesc", "Manual");// to display as
																// manual in the
																// add mode.
			frmPreAuthGeneral.set("flowType", strFlowType.toString());// set the
																		// value
																		// from
																		// which
																		// flow
																		// ur
																		// are.
			strCaption.append("Add");
			if (userSecurityProfile.getBranchID() != null) {
				frmPreAuthGeneral.set("officeSeqID", userSecurityProfile
						.getBranchID().toString());
			}// end of if(userSecurityProfile.getBranchID()!=null)
			else {
				frmPreAuthGeneral.set("officeSeqID", "");
			}// end of else
			frmPreAuthGeneral.set("caption", strCaption.toString());
			frmPreAuthGeneral.set("alPrevClaimList", alPrevClaimList);
			// keep the frmPolicyDetails in session scope
			request.getSession().setAttribute("frmPreAuthGeneral",
					frmPreAuthGeneral);
			this.documentViewer(request, preAuthDetailVO);
			ActionForward actionForward = this.getForward(strDetail, mapping,
					request);
			return actionForward;
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of viewHistory(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward AddClaim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			HttpSession session = request.getSession();
			log.debug("Inside ClaimGeneralAction AddClaim ");
			DynaActionForm frmCalimGeneral = (DynaActionForm) form;
			StringBuffer strCaption = new StringBuffer();
			strCaption.append("Add");
			frmCalimGeneral.initialize(mapping);
			frmCalimGeneral.set("networkProviderType", "Y");
			frmCalimGeneral.set("caption", strCaption.toString());
			frmCalimGeneral.set("preAuthNoStatus", "FRESH");

			session.setAttribute("frmCalimGeneral", frmCalimGeneral);
			session.setAttribute("diagnosis", null);
			session.setAttribute("activities", null);
			session.setAttribute("shortfalls", null);
			// this.documentViewer(request,preAuthDetailVO);
			return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of AddClaim(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)

	/**
	 * This method is used to add/update the record. Finally it forwards to the
	 * appropriate view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
	 * 
	 * 
	 */
	public ActionForward validationOTP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PreAuthManager preAuthObject = this.getPreAuthManagerObject();
		String otpNumber = request.getParameter("otpNumber");
		otpNumber = otpNumber == null ? "" : otpNumber.trim();
		String memberId = request.getParameter("mid");
		memberId = memberId == null ? "" : memberId.trim();
		Map<String, String> holdResult;
		try {
			holdResult = preAuthObject.getOtpresult(memberId,
					TTKCommon.getUserSeqId(request), otpNumber);

		} catch (Exception e) {
			holdResult = null;
		e.printStackTrace();
	 }

		request.setAttribute("holdResult", holdResult);
    return mapping.findForward("preauthOtpValidation");
	}

	/**
	 * This method is used to add/update the record. Finally it forwards to the
	 * appropriate view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward doSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction doSave");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			HttpSession session = request.getSession();
			PreAuthDetailVO preAuthDetailVO = null;
			StringBuffer strCaption = new StringBuffer();
			String successMsg;
			ClaimManager claimObject = this.getClaimManagerObject();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();

			preAuthDetailVO = (PreAuthDetailVO) FormUtils.getFormValues(
					frmClaimGeneral, this, mapping, request);
		    preAuthDetailVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			String claimNo = preAuthDetailVO.getClaimNo();
			Long claimSeqID = preAuthDetailVO.getClaimSeqID();
			if (claimNo == null || claimNo.length() < 1)
				successMsg = "Claim Details Added Successfully";
			else
				successMsg = "Claim Details Updated Successfully";
			
			if(preAuthDetailVO.getEnablericopar()==null)
			{
				preAuthDetailVO.setEnablericopar("N");
			}
			if(!preAuthDetailVO.getBenefitType().endsWith("MTI")){
				preAuthDetailVO.setMatsubbenefit("");		
			}
		
		
         claimObject.saveClaimDetails(preAuthDetailVO);
         //-------------------Save the Dental related data--------------------
         if("DNTL".equals(preAuthDetailVO.getBenefitType())){
			ActionForm dentalForm=(ActionForm)frmClaimGeneral.get("dentalOrthoVO");
			DentalOrthoVO dentalOrthoVO=(DentalOrthoVO)FormUtils.getFormValues(dentalForm,"frmPreAuthDental",this,mapping,request);
			dentalOrthoVO.setPreAuthSeqid(claimSeqID);
			String iotn	=	preAuthObject.saveDentalDetails(dentalOrthoVO,"CLM");
			dentalOrthoVO.setIotn(iotn);
         }
         //---------------------------------------------------------
			
			Object[] claimResults = claimObject.getClaimDetails(claimSeqID);
			
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			
			/*frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
"frmClaimGeneral", preAuthDetailVO, this, mapping, request);*/
			request.getSession().setAttribute("preauthDetail",preAuthDetailVO);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			if ("N".equals(frmClaimGeneral.getString("networkProviderType"))) {
				session.setAttribute("providerStates",
						TTKCommon.getStates(frmClaimGeneral
								.getString("providerCountry")));
				session.setAttribute("providerAreas", TTKCommon
						.getAreas(frmClaimGeneral.getString("providerEmirate")));
			  }
			session.setAttribute("encounterTypes",
					preAuthObject.getEncounterTypes(frmClaimGeneral
							.getString("benefitType")));

			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);
            session.setAttribute("OfficeSeqId", preAuthDetailVO.getOfficeSeqID());
			this.addToWebBoard(preAuthDetailVO, request, "CLM");

			strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());
			frmClaimGeneral.set("completedYN", preAuthDetailVO.getCompletedYNFlag());
			request.getSession().setAttribute("vipmemberuseraccesspermissionflag", preAuthDetailVO.getVipMemberUserAccessPermissionFlag());
			request.getSession().setAttribute("assigntouserseqid", preAuthDetailVO.getAssignUserSeqID());
			request.setAttribute("successMsg", successMsg);
		
			if("INP".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "In Progress");	
			}else if("APR".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Approved");
			}else if("REJ".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Rejected");
			}else if("PCN".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Cancel");
			}else if("REQ".equals(preAuthDetailVO.getClaimStatus())){
				request.getSession().setAttribute("claimStatus", "Required Information");
			}else{
				request.getSession().setAttribute("claimStatus", preAuthDetailVO.getClaimStatus());
			}
			request.getSession().setAttribute("assigntouserseqid", preAuthDetailVO.getAssignUserSeqID());
			request.getSession().setAttribute("vipmemberuseraccesspermissionflag", preAuthDetailVO.getVipMemberUserAccessPermissionFlag());

			request.getSession().setAttribute("preAuthStatus", null);
			return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)

	public ActionForward addDiagnosisDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction addDiagnosisDetails ");
			HttpSession session = request.getSession();
			StringBuffer strCaption = new StringBuffer();
			PreAuthDetailVO preAuthDetailVO = null;
			DiagnosisDetailsVO diagnosisDetailsVO = null;
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			ClaimManager claimObject = this.getClaimManagerObject();
		String successMsg;
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String claimSeqID = frmClaimGeneral.getString("claimSeqID");
			String icdCode = frmClaimGeneral.getString("icdCode");
			String icdCodeSeqId = frmClaimGeneral.getString("icdCodeSeqId");
			String ailmentDescription = frmClaimGeneral
					.getString("ailmentDescription");
			String presentingComplaints = frmClaimGeneral
					.getString("presentingComplaints");
			String authType = frmClaimGeneral.getString("authType");

			String primaryAilment = request.getParameter("primaryAilment");
			String diagSeqId = frmClaimGeneral.getString("diagSeqId");
			
			//Added for PED
			Integer durationOfAlment = Integer.parseInt((String)frmClaimGeneral.get("durAilment"));
			String durationFlag = (String)frmClaimGeneral.get("durationFlag");
			
			Long longDiagSeqId = (diagSeqId == null || diagSeqId.length() < 1) ? null
					: new Long(diagSeqId);

		  diagnosisDetailsVO = new DiagnosisDetailsVO();
		  diagnosisDetailsVO.setAuthType(authType);
		  diagnosisDetailsVO.setDiagSeqId(longDiagSeqId);
		  diagnosisDetailsVO.setClaimSeqID(new Long(claimSeqID));
		  diagnosisDetailsVO.setIcdCode(icdCode);
		  diagnosisDetailsVO.setAilmentDescription(ailmentDescription);
		  diagnosisDetailsVO.setPresentingComplaints(presentingComplaints);
		  diagnosisDetailsVO.setPrimaryAilment(primaryAilment);
		  diagnosisDetailsVO.setIcdCodeSeqId(new Long(icdCodeSeqId));
		  diagnosisDetailsVO.setAddedBy(TTKCommon.getUserSeqId(request));
			//Added for PED
		  diagnosisDetailsVO.setDurAilment(durationOfAlment);
			diagnosisDetailsVO.setDurationFlag(durationFlag);

			successMsg = (diagSeqId == null || diagSeqId.length() < 1) ? "Diagnosis Details Added Successfully"
					: "Diagnosis Details Updated Successfully";

		  preAuthObject.saveDiagnosisDetails(diagnosisDetailsVO);

			Object[] claimResults = claimObject.getClaimDetails(new Long(
					claimSeqID));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			
			frmClaimGeneral.set("icdCode", "");
			frmClaimGeneral.set("icdCodeSeqId", "");
			frmClaimGeneral.set("ailmentDescription", "");
			frmClaimGeneral.set("primaryAilment", "");
			frmClaimGeneral.set("diagSeqId", "");

			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
					session.setAttribute("frmClaimGeneral", frmClaimGeneral);
			frmClaimGeneral.set("completedYN", preAuthDetailVO.getCompletedYNFlag());
			this.addToWebBoard(preAuthDetailVO, request, "CLM");

					strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());
			
			request.setAttribute("successMsg", successMsg);
		return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of addDiagnosisDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward editDiagnosisDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction editDiagnosis ");
			HttpSession session = request.getSession();
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;

			DiagnosisDetailsVO diagnosisDetailsVO2 = ((ArrayList<DiagnosisDetailsVO>) session
					.getAttribute("claimDiagnosis")).get(new Integer(request
					.getParameter("rownum")).intValue());

			frmClaimGeneral.set("primaryAilment",
					diagnosisDetailsVO2.getPrimaryAilment());
			frmClaimGeneral.set("ailmentDescription",
					diagnosisDetailsVO2.getAilmentDescription());
			frmClaimGeneral.set("icdCode", diagnosisDetailsVO2.getIcdCode());
			frmClaimGeneral.set("icdCodeSeqId", diagnosisDetailsVO2
					.getIcdCodeSeqId().toString());
			frmClaimGeneral.set("diagSeqId", diagnosisDetailsVO2.getDiagSeqId()
					.toString());
			
			request.setAttribute("focusObj", request.getParameter("focusObj"));

			request.getSession().setAttribute("frmClaimGeneral",
					frmClaimGeneral);
			
			
			frmClaimGeneral.set("qtyAndDaysAlert", "");  // added by govind

			return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of editDiagnosisDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward deleteDiagnosisDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction deleteDiagnosis ");
			StringBuffer strCaption = new StringBuffer();
			PreAuthDetailVO preAuthDetailVO = null;
			ClaimManager claimObject = this.getClaimManagerObject();
			DiagnosisDetailsVO diagnosisDetailsVO = null;
			HttpSession session = request.getSession();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();

			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String claimSeqID = (String) frmClaimGeneral.get("claimSeqID");

			diagnosisDetailsVO = ((ArrayList<DiagnosisDetailsVO>) session.getAttribute("claimDiagnosis")).get(new Integer(request
					.getParameter("rownum")).intValue());
		  diagnosisDetailsVO.setAuthType("CLM");
		  diagnosisDetailsVO.setClaimSeqID(new Long(claimSeqID));
			diagnosisDetailsVO.setAddedBy(TTKCommon.getUserSeqId(request));

		      preAuthObject.deleteDiagnosisDetails(diagnosisDetailsVO);
			request.setAttribute("successMsg",
					"Diagnosis Details Deleted Successfully");
			
			request.setAttribute("focusObj", request.getParameter("focusObj"));

			Object[] claimResults = claimObject.getClaimDetails(new Long(
					claimSeqID));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);

			this.addToWebBoard(preAuthDetailVO, request, "CLM");

				strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());
			
			
			
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);

			return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of deleteDiagnosisDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * setMaternityMode
	 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
	public ActionForward setMaternityMode(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside ClaimGenealAction setMaternityMode");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			frmClaimGeneral.set("gravida", "");
			frmClaimGeneral.set("para", "");
			frmClaimGeneral.set("live", "");
			frmClaimGeneral.set("abortion", "");
			frmClaimGeneral.set("qtyAndDaysAlert", "");
			request.getSession().setAttribute("encounterTypes",	preAuthObject.getEncounterTypes(frmClaimGeneral.getString("benefitType")));
			
			request.getSession().setAttribute("frmClaimGeneral",frmClaimGeneral);
	return mapping.findForward(strClaimdetails);
		}// end of try
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)

	}// end of doGeneral(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward setProviderMode(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction setProviderMode ");

			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String networkProviderType = (String) frmClaimGeneral
					.get("networkProviderType");
			HttpSession session = request.getSession();
			if ("N".equals(networkProviderType)) {
				frmClaimGeneral.set("networkProviderType", "N");
			} else {
				frmClaimGeneral.set("providerAddress", "");
				frmClaimGeneral.set("providerPobox", "");
				frmClaimGeneral.set("providerArea", "");
				frmClaimGeneral.set("providerEmirate", "");
				frmClaimGeneral.set("providerCountry", "");
				frmClaimGeneral.set("networkProviderType", "Y");
		}
			frmClaimGeneral.set("providerName", "");
			frmClaimGeneral.set("providerId", "");
			frmClaimGeneral.set("providerSeqId", "");
			frmClaimGeneral.set("clinicianName", "");
			frmClaimGeneral.set("clinicianId", "");
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);
			return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of setProviderMode(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward selectActivityCode(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction selectActivityCode ");

			HttpSession session = request.getSession();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			String codeFlag = request.getParameter("codeFlag");
			String searchType = request.getParameter("searchType");
			String benefitType = "";
			String activityType = "";
			String otherRemarks = "";
			String actOrMast	=	(String)request.getAttribute("sSearchType");
			if ("remember".equalsIgnoreCase(codeFlag)) {// executing when
														// entered activity code
														// and onblure event
				DynaActionForm frmActivityDetails = (DynaActionForm) form;
				String providerRequestedAmt	=	frmActivityDetails.getString("providerRequestedAmt");
				
				String providerRequestedQty	=	frmActivityDetails.getString("providerQty");
				String activityStartDate = (String) frmActivityDetails
						.get("activityStartDate");
				String claimSeqID = (String) frmActivityDetails
						.get("claimSeqID");
				String claimNo = (String) frmActivityDetails.get("claimNo");
				String activityCode = "";
				activityType = (String) frmActivityDetails.get("activityType");
				activityCode = (activityCode == null) ? "" : activityCode.trim();
				if("INT".equals(searchType))
					activityCode = (String) frmActivityDetails.get("internalCode");
				else
					activityCode = (String) frmActivityDetails.get("activityCode");
				String claimReceiveDate = frmActivityDetails.getString("claimReceiveDate");		
				String activityDtlSeqId = frmActivityDetails
						.getString("activityDtlSeqId");
				String clinicianId = frmActivityDetails
						.getString("clinicianId");
				String clinicianName = frmActivityDetails
						.getString("clinicianName");
				String authType = frmActivityDetails.getString("authType");
				String overrideYN = frmActivityDetails.getString("overrideYN");
				String networkProviderType = frmActivityDetails
						.getString("networkProviderType");
				 benefitType = frmActivityDetails.getString("benefitType");
				 otherRemarks = frmActivityDetails.getString("otherRemarks");
				 String discountFlag = frmActivityDetails.getString("discountFlag");
				 String claimType = frmActivityDetails.getString("claimtype");
				ActivityDetailsVO activityDetailsVO = preAuthObject
						.getActivityCodeTariff(activityCode, claimSeqID,
								activityStartDate, authType,benefitType,searchType.equals("INT")?"SEARCHOUT":searchType);
				String activityTariffStatus = activityDetailsVO
						.getActivityTariffStatus();
				
			
				
				if (!(activityTariffStatus == null || activityTariffStatus
						.trim().length() < 1)) {
					request.setAttribute("activityTariffStatus",
							activityTariffStatus);
	}
				frmActivityDetails.initialize(mapping);
				frmActivityDetails = (DynaActionForm) FormUtils.setFormValues( "frmActivityDetails", activityDetailsVO, this, mapping, request);

				if("DRG".equals(activityType))
				{
					if ("NOTVALID".equals(activityDetailsVO.getDisplayMsg()))
						if("INT".equals(searchType))
							frmActivityDetails.set("internalCode", activityCode);
						else
							frmActivityDetails.set("activityCode", activityCode);
				}
				
				if("INT".equals(searchType))
					searchType	=	"Internal";
				else
					searchType	=	"Activity";
				if ("VALID".equals(activityDetailsVO.getDisplayMsg()))
					request.setAttribute("successMsg", searchType+" Code Valid");
				else
					request.setAttribute("errorMsg", searchType+" Code Not Valid");

	 //frmActivityDetails.set("activityCode", activityCode);
	 frmActivityDetails.set("amountAllowed", "Y");
	 frmActivityDetails.set("claimSeqID", claimSeqID);
	 frmActivityDetails.set("clinicianName", clinicianName);
	 frmActivityDetails.set("activityStartDate", activityStartDate);
	 frmActivityDetails.set("claimNo", claimNo);
	 frmActivityDetails.set("clinicianId", clinicianId);
	 frmActivityDetails.set("activityDtlSeqId", activityDtlSeqId);
	 frmActivityDetails.set("overrideYN", overrideYN);
	 
	 if ("VALID".equals(activityDetailsVO.getDisplayMsg()))
	 {
		 if((activityDetailsVO.getUnitPrice()==null||activityDetailsVO.getUnitPrice().equals(BigDecimal.ZERO))&& benefitType.equalsIgnoreCase("CB"))
				 {
			 		frmActivityDetails.set("unitPrice", "0.00");
				 }
		 
	if((activityDetailsVO.getGrossAmount()==null||activityDetailsVO.getGrossAmount().equals(BigDecimal.ZERO)) && benefitType.equalsIgnoreCase("CB")){
		 frmActivityDetails.set("grossAmount", "0.00");
		 frmActivityDetails.set("discountedGross", "0.00");
		 frmActivityDetails.set("netAmount", "0.00");
		 }
	 }
	
	 			
				frmActivityDetails.set("networkProviderType",
						networkProviderType);
				frmActivityDetails.set("codeFlag",codeFlag);
				frmActivityDetails.set("activityType", activityType);
				frmActivityDetails.set("providerRequestedAmt",providerRequestedAmt);
				frmActivityDetails.set("providerQty",providerRequestedQty);
				
				frmActivityDetails.set("otherRemarks",otherRemarks);
				LinkedHashMap<String, String> overRemarksList = (LinkedHashMap<String, String>) session.getAttribute("overRemarksList");
				if(overRemarksList == null || overRemarksList.isEmpty())
					frmActivityDetails.set("overrideRemarks", "");	
				else
					frmActivityDetails.set("overrideRemarks","1");
				frmActivityDetails.set("claimReceiveDate",claimReceiveDate);
				frmActivityDetails.set("discountFlag",discountFlag);
				frmActivityDetails.set("claimtype",claimType);
				session.setAttribute("frmActivityDetails", frmActivityDetails);
			} else if ("search".equals(codeFlag)) {// executing when searching
													// activity code selected
				DynaActionForm frmActivityDetails = (DynaActionForm) session
						.getAttribute("frmActivityDetails");
				String providerRequestedAmt	=	frmActivityDetails.getString("providerRequestedAmt");
				String providerRequestedQty=frmActivityDetails.getString("providerQty");
				String activityStartDate = (String) frmActivityDetails
						.get("activityStartDate");
				String claimSeqID = (String) frmActivityDetails
						.get("claimSeqID");
				String claimNo = (String) frmActivityDetails.get("claimNo");
				String activityDtlSeqId = frmActivityDetails
						.getString("activityDtlSeqId");
				String clinicianId = frmActivityDetails
						.getString("clinicianId");
				String clinicianName = frmActivityDetails
						.getString("clinicianName");
				String authType = frmActivityDetails.getString("authType");
				String overrideYN = frmActivityDetails.getString("overrideYN");
				activityType = (String) frmActivityDetails.get("activityType");
				String networkProviderType = frmActivityDetails
						.getString("networkProviderType");
				String discountFlag = frmActivityDetails.getString("discountFlag");
				String claimType = frmActivityDetails.getString("claimtype");
				otherRemarks = frmActivityDetails.getString("otherRemarks");
				TableData activityCodeListData = (TableData) session
						.getAttribute("activityCodeListData");
				ActivityDetailsVO activityDetailsVO = (ActivityDetailsVO) activityCodeListData
						.getRowInfo(Integer.parseInt((String) request
								.getAttribute("rownum")));

				String activityCode = "";
				if(activityDetailsVO.getInternalCode()!=null && !"-".equals(activityDetailsVO.getInternalCode()))
					activityCode = activityDetailsVO.getInternalCode();
				else
					activityCode = activityDetailsVO.getActivityCode();
				
				 benefitType = frmActivityDetails.getString("benefitType");
				String claimReceiveDate = frmActivityDetails.getString("claimReceiveDate");
				 if(actOrMast!=null && actOrMast.equals("TAR")){
					 searchType		=	"INT";
					 activityCode	=	activityDetailsVO.getActivityCodeSeqId().toString();
				 }
				 if(actOrMast!=null && actOrMast.equals("ACT")){
					 searchType		=	"MSTR";
					 activityCode = activityDetailsVO.getActivityCode();
				 }
				 
				activityDetailsVO = preAuthObject.getActivityCodeTariff(
						activityCode, claimSeqID, activityStartDate, authType,benefitType,searchType);

				String activityTariffStatus = activityDetailsVO
						.getActivityTariffStatus();
				if (!(activityTariffStatus == null || activityTariffStatus
						.trim().length() < 1)) {
					request.setAttribute("activityTariffStatus",
							activityTariffStatus);
					
				}
				
				if ("VALID".equals(activityDetailsVO.getDisplayMsg()))
					request.setAttribute("successMsg", "Activity Code Valid");
				else
					request.setAttribute("errorMsg", "Activity Code Not Valid");
				
		frmActivityDetails.initialize(mapping);

				frmActivityDetails = (DynaActionForm) FormUtils.setFormValues(
						"frmActivityDetails", activityDetailsVO, this, mapping,
						request);
	 frmActivityDetails.set("activityCode", activityDetailsVO.getActivityCode());
	 frmActivityDetails.set("amountAllowed", "Y");
	 frmActivityDetails.set("claimSeqID", claimSeqID);
	 frmActivityDetails.set("activityStartDate", activityStartDate);
	 frmActivityDetails.set("claimNo", claimNo);
	 frmActivityDetails.set("clinicianId", clinicianId);
	 frmActivityDetails.set("clinicianName", clinicianName);
	 frmActivityDetails.set("activityDtlSeqId", activityDtlSeqId);
	 frmActivityDetails.set("overrideYN", overrideYN);
	 frmActivityDetails.set("activityType", activityType);
	
	 if ("VALID".equals(activityDetailsVO.getDisplayMsg()))
	 {
		 if((activityDetailsVO.getUnitPrice()==null||activityDetailsVO.getUnitPrice().equals(BigDecimal.ZERO)) && benefitType.equalsIgnoreCase("CB"))
		 {
	 		frmActivityDetails.set("unitPrice", "0.00");
		 }
		if((activityDetailsVO.getGrossAmount()==null||activityDetailsVO.getGrossAmount().equals(BigDecimal.ZERO)) && benefitType.equalsIgnoreCase("CB"))
		{
			frmActivityDetails.set("grossAmount", "0.00");
			frmActivityDetails.set("discountedGross", "0.00");
			frmActivityDetails.set("netAmount", "0.00");
		}
	 }
				frmActivityDetails.set("networkProviderType",
						networkProviderType);
				frmActivityDetails.set("codeFlag",codeFlag);
				frmActivityDetails.set("providerRequestedAmt",providerRequestedAmt);
				frmActivityDetails.set("providerQty",providerRequestedQty);
				
				frmActivityDetails.set("otherRemarks",otherRemarks);
				LinkedHashMap<String, String> overRemarksList = (LinkedHashMap<String, String>) session.getAttribute("overRemarksList");
				
				if(overRemarksList == null || overRemarksList.isEmpty())
					frmActivityDetails.set("overrideRemarks", "");	
				else
					frmActivityDetails.set("overrideRemarks","1");
				frmActivityDetails.set("claimReceiveDate",claimReceiveDate);
				frmActivityDetails.set("discountFlag",discountFlag);
				frmActivityDetails.set("claimtype",claimType);
				
				
				session.setAttribute("frmActivityDetails", frmActivityDetails);
 }//else
			request.setAttribute("showPopUp", "Yes");
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivitydetails));
		}// end of catch(Exception exp)
	}// end of selectActivityCode(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	/**
 * doGeneral forward the based on reforward parameter value
	 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
	public ActionForward doGeneral(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside ClaimGenealAction doGeneral");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String path = "Claims.Processing.General";
			HttpSession session = request.getSession();
			String reforward = request.getParameter("reforward");
			if ("providerSearch".equalsIgnoreCase(reforward)) {
				TableData providerListData = new TableData(); // create new
																// table data
																// object
				providerListData.createTableInfo("ProviderListTable",new ArrayList());
				session.setAttribute("providerListData", providerListData);// create
																			// the
																			// required
																			// grid
																			// table
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
				path = "providerSearchList";
			}  else if ("memberSearch".equalsIgnoreCase(reforward)) {
				
				TableData memberListData = new TableData(); // create new
																// table data
																// object
				memberListData.createTableInfo("MemberListTable",
						new ArrayList());
				session.setAttribute("memberListData", memberListData);// create
																			// the
																			// required
																			// grid
																			// table
				session.setAttribute("frmPreAuthGeneral", frmClaimGeneral);
				path = "memberSearchList";
				return mapping.findForward(path);
			
			}else if ("clinicianSearch".equalsIgnoreCase(reforward)) {
				TableData clinicianListData = new TableData(); // create new
																// table data
																// object
				clinicianListData.createTableInfo("ClinicianListTable",
						new ArrayList());
				session.setAttribute("clinicianListData", clinicianListData);// create
																				// the
																				// required
																				// grid
																				// table
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
				session.setAttribute("forwardMode", "");
				path = "clinicianSearchList";
			} else if ("diagnosisSearch".equalsIgnoreCase(reforward)) {
				
				request.setAttribute("focusObj", request.getParameter("focusObj"));
				
				frmClaimGeneral.set("icdCode", "");
				frmClaimGeneral.set("icdCodeSeqId", "");
				frmClaimGeneral.set("ailmentDescription", "");
				frmClaimGeneral.set("primaryAilment", "");

				TableData diagnosisCodeListData = new TableData(); // create new
																	// table
																	// data
																	// object
				diagnosisCodeListData.createTableInfo("DiagnosisCodeListTable",
						new ArrayList());
				session.setAttribute("diagnosisCodeListData",
						diagnosisCodeListData);// create the required grid table
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
				path = "diagnosisSearchList";
			} else if ("addActivityDetails".equalsIgnoreCase(reforward)) {
				
				
				request.setAttribute("claimReceiveDate",frmClaimGeneral.getString("receiveDate"));
				request.setAttribute("discountFlag",frmClaimGeneral.getString("discountFlag"));
				request.setAttribute("claimType",frmClaimGeneral.getString("claimType"));
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
				request.getSession().setAttribute("dflag", "N");
				
				path = "Claims.Processing.General.ActivityDetails";
			} else if ("activityClinicianSearch".equalsIgnoreCase(reforward)) {
				DynaActionForm frmActivityDetails = (DynaActionForm) form;
				TableData clinicianListData = new TableData(); // create new
																// table data
																// object
				clinicianListData.createTableInfo("ClinicianListTable",
						new ArrayList());
				session.setAttribute("clinicianListData", clinicianListData);// create
																				// the
																				// required
																				// grid
																				// table
				session.setAttribute("forwardMode", "activityClinicianSearch");
				session.setAttribute("frmActivityDetails", frmActivityDetails);
				path = "clinicianSearchList";
			} else if ("activitySearchList".equalsIgnoreCase(reforward)) {
				DynaActionForm frmActivityDetails = (DynaActionForm) form;
				TableData activityCodeListData = new TableData(); // create new
																	// table
																	// data
																	// object
				activityCodeListData.createTableInfo("ActivityCodeListTable",
						new ArrayList());
				
				session.setAttribute("activityCodeListData",
						activityCodeListData);// create the required grid table
				frmActivityDetails.set("providerRequestedAmt",frmActivityDetails.getString("providerRequestedAmt"));
				request.setAttribute("discountFlag",frmActivityDetails.getString("discountFlag"));
				request.setAttribute("claimType",frmActivityDetails.getString("claimtype"));
				session.setAttribute("frmActivityDetails", frmActivityDetails);
				path = "activitySearchList";
			} else if ("viewActivityDetails".equals(reforward)) {
				request.setAttribute("activityDtlSeqId",
						frmClaimGeneral.getString("activityDtlSeqId"));
				request.setAttribute("networkProviderType",
						frmClaimGeneral.getString("networkProviderType"));
				request.setAttribute("claimReceiveDate",
						frmClaimGeneral.getString("receiveDate"));
				request.setAttribute("discountFlag",frmClaimGeneral.getString("discountFlag"));
				request.setAttribute("claimType",frmClaimGeneral.getString("claimType"));
				path = "viewActivityDetails";
			} else if ("overrideActivity".equals(reforward)) {
				request.setAttribute("activityDtlSeqId",
						frmClaimGeneral.getString("activityDtlSeqId"));
				request.setAttribute("networkProviderType",
						frmClaimGeneral.getString("networkProviderType"));
				request.setAttribute("override",
						request.getParameter("override"));
				request.setAttribute("claimReceiveDate",frmClaimGeneral.getString("receiveDate"));
				request.setAttribute("discountFlag",frmClaimGeneral.getString("discountFlag"));
				request.setAttribute("claimType",frmClaimGeneral.getString("claimType"));
				path = "overrideActivity";
			} else if ("claimshortfalls".equalsIgnoreCase(reforward)) {
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
	session.setAttribute("closeShortfalls", "goGeneral");
				path = "claimshortfalls";
			} else if ("viewShortfalls".equalsIgnoreCase(reforward)) {
				String claimSeqID = frmClaimGeneral.getString("claimSeqID");
				String shortFallSeqId = frmClaimGeneral
						.getString("shortFallSeqId");
				ShortfallVO shortfallVO = new ShortfallVO();
	shortfallVO.setClaimSeqID(new Long(claimSeqID));
	shortfallVO.setShortfallSeqID(new Long(shortFallSeqId));
	session.setAttribute("searchShortfallVO", shortfallVO);
	session.setAttribute("closeShortfalls", "goGeneral");
				path = "viewShortfalls";
			} else if ("close".equalsIgnoreCase(reforward)) {
				DynaActionForm dynForm = (DynaActionForm) form;
				String claimSeqID = dynForm.getString("claimSeqID");
	request.setAttribute("claimSeqID", claimSeqID);
	
    path = "Claims.Processing.General.GetAllClaimDetails";
    request.setAttribute("focusObj", request.getParameter("focusObj"));
			} else if ("goShortfallSearch".equalsIgnoreCase(reforward)) {
				path = "Claims.Shortfalls.Search";
			} else if ("selectAuthorizationdetails".equals(reforward)) {
	session.setAttribute("frmClaimGeneral", frmClaimGeneral);
				path = "authorizationList";
	return mapping.findForward(path);
			} else if ("closeHistoryDetails".equalsIgnoreCase(reforward)) {
				path = "claimHistoryList";
	return mapping.findForward(path);
			}
	return mapping.findForward(path);
		}// end of try
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)

	}// end of doGeneral(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward addActivityDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			
			setLinks(request);
			log.debug("Inside ClaimGeneralAction addActivityDetails ");
			HttpSession session = request.getSession();
			DynaActionForm frmClaimGeneral = (DynaActionForm) session
					.getAttribute("frmClaimGeneral");
			String climtype=frmClaimGeneral.getString("claimType");
			
			String claimNo = frmClaimGeneral.getString("claimNo");
			String claimSeqID = frmClaimGeneral.getString("claimSeqID");
			String admissionDate = frmClaimGeneral.getString("admissionDate");
			String encounterTypeId = frmClaimGeneral
					.getString("encounterTypeId");
			String clinicianId = frmClaimGeneral.getString("clinicianId");
			String networkProviderType = frmClaimGeneral
					.getString("networkProviderType");
			String clinicianName = frmClaimGeneral.getString("clinicianName");
			String benefitType = frmClaimGeneral.getString("benefitType");
			boolean dateStatus = false;
			if ("1".equals(encounterTypeId) || "2".equals(encounterTypeId))
				dateStatus = true;
			String claimReceiveDate = (String) request.getAttribute("claimReceiveDate");
			String discountFlag = (String) request.getAttribute("discountFlag");
			String claimType = (String) request.getAttribute("claimType");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
	frmActivityDetails.initialize(mapping);
	//frmActivityDetails.set("claimtype",climtype);
	frmActivityDetails.set("claimNo", claimNo);
	frmActivityDetails.set("claimSeqID", claimSeqID);
	
	frmActivityDetails.set("clinicianId", clinicianId);
			frmActivityDetails.set("clinicianName", clinicianName);
	frmActivityDetails.set("benefitType",benefitType);
	frmActivityDetails.set("networkProviderType", networkProviderType);
			frmActivityDetails.set("activityStartDate", admissionDate);// 1,2--NON-edit
			frmActivityDetails.set("amountAllowed", "Y");// Amount Allowed
			frmActivityDetails.set("claimReceiveDate", claimReceiveDate);
			frmActivityDetails.set("discountFlag", discountFlag);
			frmActivityDetails.set("claimtype", claimType);
			request.setAttribute("focusObj", request.getParameter("focusObj"));														// default Checked
			session.setAttribute("disableQty", "N");
			session.setAttribute("dateStatus", dateStatus);
	session.setAttribute("frmActivityDetails", frmActivityDetails);
	return mapping.findForward(strActivitydetails);

		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of AddActivityDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward overrideActivityDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			setLinks(request);
			log.debug("Inside ClaimGeneralAction editActivityDetails ");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			ClaimManager claimObject = this.getClaimManagerObject();
			String activityDtlSeqId = (String) request
					.getAttribute("activityDtlSeqId");
			String override = (String) request.getAttribute("override");
			String networkProviderType = (String) request
					.getAttribute("networkProviderType");
			String claimReceiveDate = (String) request.getAttribute("claimReceiveDate");
			String discountFlag = (String) request.getAttribute("discountFlag");
			String claimType = (String) request.getAttribute("claimType");
			ActivityDetailsVO activityDetailsVO = preAuthObject
					.getActivityDetails(activityDtlSeqId == null ? 0 : Long
							.parseLong(activityDtlSeqId));
			
			LinkedHashMap<String, String> activityDenialList = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> tpaActivityDenialList = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> overRemarksList = new LinkedHashMap<String, String>();
			// splitting the denial codes and denial desc in case override
			if ("Y".equals(activityDetailsVO.getOverrideYN())) {

				String denialCode = activityDetailsVO.getDenialCode();
				String denialCodeDesc = activityDetailsVO
						.getDenialDescription();
				String tpaDenialCode=activityDetailsVO.getTpaDenialCode();
				String tpaDenialDescription=activityDetailsVO.getTpaDenialDescription();
				String OverrideRemarksCode = activityDetailsVO.getOverrideRemarksCode();
				String OverrideRemarksDesc = activityDetailsVO.getOverrideRemarksDesc();
				if (denialCode != null && denialCodeDesc != null) {
					String denialCodes[] = denialCode.split("[;]");
					String denialCodeDescs[] = denialCodeDesc.split("[;]");

					if (denialCodes.length == denialCodeDescs.length) {
						for (int i = 0; i < denialCodes.length; i++) {
							activityDenialList.put(denialCodes[i].trim(),
									denialCodeDescs[i]);
						}
					}
				}					
					if (OverrideRemarksCode != null && OverrideRemarksDesc != null) 
					{
						String OverrideRemarksCodes[] = OverrideRemarksCode.split("[;]");
						String OverrideRemarksDescs[] = OverrideRemarksDesc.split("[;]");

						if (OverrideRemarksCodes.length == OverrideRemarksDescs.length) 
						{
							for (int i = 0; i < OverrideRemarksCodes.length; i++) 
							{
								overRemarksList.put(OverrideRemarksCodes[i].trim(),OverrideRemarksDescs[i]);
							}
						}
					}
						activityDetailsVO.setDenialCode(null);
						activityDetailsVO.setDenialDescription(null);
						
						activityDetailsVO.setOverrideRemarksCode(null);
						activityDetailsVO.setOverrideRemarksDesc(null);
	
	if(tpaDenialCode!=null&&tpaDenialDescription!=null){

		/*String tpadenialCodes[] = tpaDenialCode.split("[;]");
		String tpadenialCodeDescs[] = tpaDenialDescription.split("[;]");
*/
//		if (tpadenialCodes.length == tpadenialCodes.length) {
			/*for (int i = 0; i < tpadenialCodes.length; i++) {
				tpaActivityDenialList.put(tpadenialCodes[i].trim(),
						tpadenialCodeDescs[i]);
			}*/
//		}
		tpaActivityDenialList.put(tpaDenialCode, tpaDenialDescription);
	}
	activityDetailsVO.setTpaDenialCode(null);
	activityDetailsVO.setTpaDenialDescription(null);
			}

			frmActivityDetails = (DynaActionForm) FormUtils.setFormValues(
					"frmActivityDetails", activityDetailsVO, this, mapping,
					request);
			if(activityDetailsVO.getUnitPrice()!=null){
				if(activityDetailsVO.getUnitPrice().equals(BigDecimal.ZERO))
					 frmActivityDetails.set("unitPrice", "0.00");
				 }
			if(activityDetailsVO.getGrossAmount()!=null){
			if(activityDetailsVO.getGrossAmount().equals(BigDecimal.ZERO))
				 frmActivityDetails.set("grossAmount", "0.00");
			 }
			if(activityDetailsVO.getDiscountedGross()!=null){
				if(activityDetailsVO.getDiscountedGross().equals(BigDecimal.ZERO))
				frmActivityDetails.set("discountedGross", "0.00");
			}
			if(activityDetailsVO.getNetAmount()!=null){
				if(activityDetailsVO.getNetAmount().equals(BigDecimal.ZERO))
			   frmActivityDetails.set("netAmount", "0.00");
			}
			if(activityDetailsVO.getProviderRequestedAmt()!=null){
				if(activityDetailsVO.getProviderRequestedAmt().equals(BigDecimal.ZERO))
			   frmActivityDetails.set("providerRequestedAmt", "0.00");
			}
			frmActivityDetails.set("networkProviderType", networkProviderType);
			frmActivityDetails.set("overrideYN", override);
			frmActivityDetails.set("override", activityDetailsVO.getOverrideYN());
			
			if(overRemarksList == null || overRemarksList.isEmpty())
			{
				frmActivityDetails.set("overrideRemarks", "");	
				frmActivityDetails.set("overrideSubRemarks", "");
			}
			else
			{
				// frmActivityDetails.set("overrideRemarks","1");
				frmActivityDetails.set("overrideRemarks","ACT1");
				ArrayList<Object> subGrpName = null;
				ArrayList<CacheObject> subGrpNameList = null;
				
				 String overrideRemarksId = "ACT1";
				 
				 subGrpName = claimObject.getSubgroupName(overrideRemarksId);
				 
				 subGrpNameList=(ArrayList<CacheObject>)subGrpName.get(0);
				 frmActivityDetails.set("overrideRemarksSub",subGrpNameList);
				 frmActivityDetails.set("overrideSubRemarks","AC1");
			}
			boolean blnExists = overRemarksList.containsKey("16");
			if(blnExists)
				frmActivityDetails.set("otherRemarksYN", "Y");
			else
				frmActivityDetails.set("otherRemarksYN", "N");
	//		frmActivityDetails.set("overrideRemarks", (String)request.getSession().getAttribute("overrideRemarks"));
			request.getSession().setAttribute("activityDenialList",
					activityDenialList);
			request.getSession().setAttribute("tpaActivityDenialList", tpaActivityDenialList);
			request.getSession().setAttribute("overRemarksList",
					overRemarksList);
			frmActivityDetails.set("claimReceiveDate", claimReceiveDate);
			frmActivityDetails.set("discountFlag", discountFlag);
			frmActivityDetails.set("claimtype", claimType);
			request.getSession().setAttribute("frmActivityDetails",
					frmActivityDetails);
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// overrideActivity()


	public ActionForward deleteAuthorizationdetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside PreAuthGenealAction deleteAuthorizationdetails ");
			HttpSession session=request.getSession();
			DynaActionForm frmClaimGeneral= (DynaActionForm) form;
			frmClaimGeneral.set("exceptionFalg",frmClaimGeneral.get("exceptionFalg"));
			frmClaimGeneral.set("authNum","");
			frmClaimGeneral.set("preAuthSeqID","");
		  	frmClaimGeneral.set("preAuthApprAmt","");
		  	frmClaimGeneral.set("preAuthApprAmtCurrency","");
			frmClaimGeneral.set("preAuthReceivedDateAsString","");
			frmClaimGeneral.set("patIncAmnt","");
			frmClaimGeneral.set("patIncCurr","");
			frmClaimGeneral.set("preAuthApprAmt","");
			frmClaimGeneral.set("patReqCurr","");
			request.setAttribute("successMsg", "Authorization Details Deleted Successfully");
			session.setAttribute("frmClaimGeneral",frmClaimGeneral);
			
			return mapping.findForward("ClaimDetails");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}// end of catch(Exception exp)

	}
	
	
	
	
	
	
	
	
	
	
	

	/*public ActionForward overrideActivityDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try {
			setLinks(request);
			

			log.debug("Inside ClaimGeneralAction editActivityDetails ");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			String activityDtlSeqId = (String) request
					.getAttribute("activityDtlSeqId");
			String override = (String) request.getAttribute("override");
			String networkProviderType = (String) request
					.getAttribute("networkProviderType");
			ActivityDetailsVO activityDetailsVO = preAuthObject
					.getActivityDetails(activityDtlSeqId == null ? 0 : Long
							.parseLong(activityDtlSeqId));

			LinkedHashMap<String, String> activityDenialList = new LinkedHashMap<String, String>();
			// splitting the denial codes and denial desc in case override
			if ("Y".equals(activityDetailsVO.getOverrideYN())) {

				String denialCode = activityDetailsVO.getDenialCode();
				String denialCodeDesc = activityDetailsVO
						.getDenialDescription();

				if (denialCode != null && denialCodeDesc != null) {
					String denialCodes[] = denialCode.split("[;]");
					String denialCodeDescs[] = denialCodeDesc.split("[;]");

					if (denialCodes.length == denialCodeDescs.length) {
						for (int i = 0; i < denialCodes.length; i++) {
							activityDenialList.put(denialCodes[i].trim(),
									denialCodeDescs[i]);
		}
	}
				}
	activityDetailsVO.setDenialCode(null);
	activityDetailsVO.setDenialDescription(null);
			}

			frmActivityDetails = (DynaActionForm) FormUtils.setFormValues(
					"frmActivityDetails", activityDetailsVO, this, mapping,
					request);

			frmActivityDetails.set("networkProviderType", networkProviderType);
			frmActivityDetails.set("overrideYN", override);
			request.getSession().setAttribute("activityDenialList",
					activityDenialList);
			request.getSession().setAttribute("frmActivityDetails",
					frmActivityDetails);
		
			return this.getForward(strActivitydetails, mapping, request);
			
		}// end of try
		
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
		
	}// overrideActivity()
*/
	public ActionForward viewActivityDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction editActivityDetails ");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			ClaimManager claimObject = this.getClaimManagerObject();
			String activityDtlSeqId = (String) request
					.getAttribute("activityDtlSeqId");
			String networkProviderType = (String) request
					.getAttribute("networkProviderType");
			String claimReceiveDate = (String) request
					.getAttribute("claimReceiveDate");
			String discountFlag = (String) request.getAttribute("discountFlag");
			String claimType = (String) request.getAttribute("claimType");
			ActivityDetailsVO activityDetailsVO = preAuthObject
					.getActivityDetails(activityDtlSeqId == null ? 0 : Long
							.parseLong(activityDtlSeqId));

			LinkedHashMap<String, String> activityDenialList = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> tpaActivityDenialList = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> overRemarksList = new LinkedHashMap<String, String>();
			// splitting the denial codes and denial desc in case override
			if ("Y".equals(activityDetailsVO.getOverrideYN())) {

				String denialCode = activityDetailsVO.getDenialCode();
				String denialCodeDesc = activityDetailsVO
						.getDenialDescription();
				String tpaDenialCode=activityDetailsVO.getTpaDenialCode();
				String tpaDenialDescription=activityDetailsVO.getTpaDenialDescription();
				String OverrideRemarksCode = activityDetailsVO.getOverrideRemarksCode();
				String OverrideRemarksDesc = activityDetailsVO.getOverrideRemarksDesc();

				if (denialCode != null && denialCodeDesc != null) {
					String denialCodes[] = denialCode.split("[;]");
					String denialCodeDescs[] = denialCodeDesc.split("[;]");

					if (denialCodes.length == denialCodeDescs.length) {
						for (int i = 0; i < denialCodes.length; i++) {
							activityDenialList.put(denialCodes[i].trim(),
									denialCodeDescs[i]);
						}
					}
				}
				
				
	activityDetailsVO.setDenialCode(null);
	activityDetailsVO.setDenialDescription(null);
				
				if (OverrideRemarksCode != null && OverrideRemarksDesc != null) {
					String OverrideRemarksCodes[] = OverrideRemarksCode.split("[;]");
					String OverrideRemarksDescs[] = OverrideRemarksDesc.split("[;]");

					if (OverrideRemarksCodes.length == OverrideRemarksDescs.length) {
						for (int i = 0; i < OverrideRemarksCodes.length; i++) {
							overRemarksList.put(OverrideRemarksCodes[i].trim(),
									OverrideRemarksDescs[i]);
						}
					}
				}
				activityDetailsVO.setDenialCode(null);
				activityDetailsVO.setDenialDescription(null);
				activityDetailsVO.setOverrideRemarksCode(null);
				activityDetailsVO.setOverrideRemarksDesc(null);
			
	

	if(tpaDenialCode!=null&&tpaDenialDescription!=null){
		tpaActivityDenialList.put(tpaDenialCode, tpaDenialDescription);
	}
	activityDetailsVO.setTpaDenialCode(null);
	activityDetailsVO.setTpaDenialDescription(null);
	
			}
activityDetailsVO.setDisableQty("Y");
			frmActivityDetails = (DynaActionForm) FormUtils.setFormValues(
					"frmActivityDetails", activityDetailsVO, this, mapping,
					request);
			if(activityDetailsVO.getUnitPrice()!=null){
				if(activityDetailsVO.getUnitPrice().equals(BigDecimal.ZERO))
					 frmActivityDetails.set("unitPrice", "0.00");
				 }
			if(activityDetailsVO.getGrossAmount()!=null){
			if(activityDetailsVO.getGrossAmount().equals(BigDecimal.ZERO))
				 frmActivityDetails.set("grossAmount", "0.00");
			 }
			if(activityDetailsVO.getDiscountedGross()!=null){
				if(activityDetailsVO.getDiscountedGross().equals(BigDecimal.ZERO))
				frmActivityDetails.set("discountedGross", "0.00");
			}
			if(activityDetailsVO.getNetAmount()!=null){
				if(activityDetailsVO.getNetAmount().equals(BigDecimal.ZERO))
			   frmActivityDetails.set("netAmount", "0.00");
			}
			if(activityDetailsVO.getProviderRequestedAmt()!=null){
				if(activityDetailsVO.getProviderRequestedAmt().equals(BigDecimal.ZERO))
			   frmActivityDetails.set("providerRequestedAmt", "0.00");
			}
			
			frmActivityDetails.set("networkProviderType", networkProviderType);
			request.getSession().setAttribute("activityDenialList",
					activityDenialList);
			request.getSession().setAttribute("tpaActivityDenialList", tpaActivityDenialList);
			request.getSession().setAttribute("frmActivityDetails",
					frmActivityDetails);
			request.getSession().setAttribute("overRemarksList",overRemarksList);
			if(overRemarksList == null || overRemarksList.isEmpty()){
				frmActivityDetails.set("overrideRemarks", "");
				frmActivityDetails.set("overrideSubRemarks", "");
			}
			else
			{	
				frmActivityDetails.set("overrideRemarks","ACT1");
				ArrayList<Object> subGrpName = null;
				ArrayList<CacheObject> subGrpNameList = null;
				
				 String overrideRemarksId = "ACT1";
				 
				 subGrpName = claimObject.getSubgroupName(overrideRemarksId);
				 
				 subGrpNameList=(ArrayList<CacheObject>)subGrpName.get(0);
				 frmActivityDetails.set("overrideRemarksSub",subGrpNameList);
				 frmActivityDetails.set("overrideSubRemarks","AC1");
			}
			boolean blnExists = overRemarksList.containsKey("16");
			if(blnExists)
				frmActivityDetails.set("otherRemarksYN", "Y");
			else
				frmActivityDetails.set("otherRemarksYN", "N");
			frmActivityDetails.set("claimReceiveDate", claimReceiveDate);
			frmActivityDetails.set("discountFlag", discountFlag);
			frmActivityDetails.set("claimtype", claimType);
			request.getSession().setAttribute("dflag", "Y");
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// editActivityDetails()

	public ActionForward deleteActivityDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction deleteActivityDetails ");
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			StringBuffer strCaption = new StringBuffer();
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String claimSeqID = (String) frmClaimGeneral.get("claimSeqID");
			String authType = frmClaimGeneral.getString("authType");
			String activityDtlSeqId = (String) frmClaimGeneral
					.get("activityDtlSeqId");

			preAuthObject.deleteActivityDetails(
					new Long(claimSeqID).longValue(),
					new Long(activityDtlSeqId).longValue(), authType,TTKCommon.getUserSeqId(request));
			request.setAttribute("successMsg",
					"Activity Details Deleted Successfully");
			HttpSession session = request.getSession();
			PreAuthDetailVO preAuthDetailVO = null;
			ClaimManager claimObject = this.getClaimManagerObject();
			  frmClaimGeneral.initialize(mapping);
			Object[] claimResults = claimObject.getClaimDetails(new Long(
					claimSeqID));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
			  			session.setAttribute("frmClaimGeneral", frmClaimGeneral);

			this.addToWebBoard(preAuthDetailVO, request, "CLM");

			  			strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());
			
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			
			  return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}// end of catch(Exception exp)
	}// end of deleteActivityDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward saveActivityDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		setLinks(request);
		log.debug("Inside ClaimGenealAction saveActivityDetails");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			String successMsg;
			ActivityDetailsVO activityDetailsVO = new ActivityDetailsVO();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();

			activityDetailsVO = (ActivityDetailsVO) FormUtils.getFormValues(
					frmActivityDetails, "frmActivityDetails", this, mapping,
					request);
			String discountFlag = frmActivityDetails.getString("discountFlag");
			activityDetailsVO.setAddedBy(TTKCommon.getUserSeqId(request));
			String amountAllowed = request.getParameter("amountAllowed");
			String overrideYN = request.getParameter("overrideYN");
			amountAllowed = (amountAllowed == null || "".equals(amountAllowed)) ? "N"
					: amountAllowed;
			overrideYN = (overrideYN == null || "".equals(overrideYN)) ? "N"
					: overrideYN;
			activityDetailsVO.setAmountAllowed(amountAllowed);
			
			DynaActionForm frmClaimGeneral=(DynaActionForm)request.getSession().getAttribute("frmClaimGeneral");
			
			

			activityDetailsVO.setOverrideYN(overrideYN);
			
			if ("Y".equals(overrideYN)) {
				activityDetailsVO.setDenialCode(null);
				activityDetailsVO.setDenialDescription(null);

				LinkedHashMap<String, String> activityDenialList = (LinkedHashMap<String, String>) request.getSession().getAttribute("activityDenialList");
				LinkedHashMap<String, String> removedDenialList = null;
				if(request.getSession().getAttribute("removedDenialList")!=null)
					removedDenialList=(LinkedHashMap<String, String>) request.getSession().getAttribute("removedDenialList");
				else
					removedDenialList=null;
				
				if (activityDenialList != null && activityDenialList.size() > 0) {
					StringBuilder dcsBuilder = new StringBuilder();
					StringBuilder dcdsBuilder = new StringBuilder();

					Set<Entry<String, String>> set = activityDenialList
							.entrySet();
					for (Entry<String, String> entr : set) {
					 dcsBuilder.append(entr.getKey().trim());
					 dcsBuilder.append(";");
					 dcdsBuilder.append(entr.getValue().trim());
					 dcdsBuilder.append(";");
				 }

					activityDetailsVO.setDenialCode(dcsBuilder.toString());
					activityDetailsVO.setDenialDescription(dcdsBuilder
							.toString());

			}	
				
				LinkedHashMap<String, String> overRemarksList = (LinkedHashMap<String, String>) request.getSession().getAttribute("overRemarksList");
				if(overRemarksList == null || overRemarksList.isEmpty())	
				{
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.override.remarks.not.added");     
					throw expTTK;
				}
				if (overRemarksList != null && overRemarksList.size() > 0) {
					StringBuilder dcsBuilder = new StringBuilder();
					StringBuilder dcdsBuilder = new StringBuilder();

					Set<Entry<String, String>> set = overRemarksList.entrySet();
					for (Entry<String, String> entr : set) {
						dcsBuilder.append(entr.getKey().trim());
						dcsBuilder.append(";");
						dcdsBuilder.append(entr.getValue().trim());
						dcdsBuilder.append(";");
					}
					activityDetailsVO.setOverrideRemarksCode(dcsBuilder.toString());		
					activityDetailsVO.setOverrideRemarksDesc(dcdsBuilder.toString());	
				}
				if (removedDenialList != null && removedDenialList.size() > 0) {
					StringBuilder dcsBuilder = new StringBuilder();
					StringBuilder dcdsBuilder = new StringBuilder();
					Set<Entry<String, String>> set = removedDenialList.entrySet();
					int count=1;
					for (Entry<String, String> entr : set) {
						dcsBuilder.append(entr.getKey().trim() );
						if(count<set.size())
							dcsBuilder.append(";");						
						dcdsBuilder.append(entr.getValue().trim());	
						if(count<set.size())	
							dcdsBuilder.append(";");
						count++;
					}
					activityDetailsVO.setRemovedDenialCode(dcsBuilder.toString());
					activityDetailsVO.setRemovedDenialDescription(dcdsBuilder.toString());
				}
			}
			//System.out.println("action : overrideYN ="+overrideYN);
			if ("N".equals(overrideYN)) {
				activityDetailsVO.setOtherRemarks(null);
			}	
			preAuthObject.saveActivityDetails(activityDetailsVO);
			successMsg = (activityDetailsVO.getActivityDtlSeqId() == null || activityDetailsVO
					.getActivityDtlSeqId() == 0) ? "Activity Details Added Successffully"
					: "Activity Details Updated Successffully";
			String providerRequestedQty	=	frmActivityDetails.getString("providerQty");
			
			String claimNo = frmActivityDetails.getString("claimNo");
			String claimSeqID = frmActivityDetails.getString("claimSeqID");
			String activityStartDate = frmActivityDetails
					.getString("activityStartDate");
			String clinicianId = frmActivityDetails.getString("clinicianId");
			String clinicianName = frmActivityDetails
					.getString("clinicianName");
			String networkProviderType = frmActivityDetails
					.getString("networkProviderType");
			// String overrideYN=frmActivityDetails.getString("overrideYN");
			frmActivityDetails.initialize(mapping);

			frmActivityDetails.set("claimNo", claimNo);
			frmActivityDetails.set("claimSeqID", claimSeqID);
			frmActivityDetails.set("clinicianId", clinicianId);
			frmActivityDetails.set("clinicianName", clinicianName);
			// frmActivityDetails.set("overrideYN", overrideYN);
			frmActivityDetails.set("networkProviderType", networkProviderType);
			// frmActivityDetails.set("activityDtlSeqId", activityDtlSeqId);
			frmActivityDetails.set("activityStartDate", activityStartDate);// 1,2--NON-edit
			frmActivityDetails.set("amountAllowed", "Y");// Amount Allowed
			frmActivityDetails.set("discountFlag", discountFlag); // discountFlag
			frmActivityDetails.set("providerQty", providerRequestedQty); // discountFlag
			
			// default Checked
			request.getSession().setAttribute("frmActivityDetails",
					frmActivityDetails);
			request.getSession().setAttribute("activityDenialList", null);
			request.getSession().setAttribute("overRemarksList", null);
			request.getSession().setAttribute("removedDenialList",null);
			request.setAttribute("successMsg", successMsg);
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivityDetails));
		}// end of catch(Exception exp)
	}// end of saveActivityDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	
	public ActionForward doChangeServiceType(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction doChangeServiceType ");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			String claimNo = frmActivityDetails.getString("claimNo");
			String claimSeqID = frmActivityDetails.getString("claimSeqID");
			String activityStartDate = frmActivityDetails .getString("activityStartDate");
			String clinicianId = frmActivityDetails.getString("clinicianId");
			String clinicianName = frmActivityDetails .getString("clinicianName");
			String networkProviderType = frmActivityDetails .getString("networkProviderType");
			String activityDtlSeqId = frmActivityDetails.getString("activityDtlSeqId");
			String activityServiceType = frmActivityDetails.getString("activityServiceType");
			String activityType = frmActivityDetails.getString("activityType");
			String claimReceiveDate = frmActivityDetails.getString("claimReceiveDate");
			String claimtype = frmActivityDetails.getString("claimtype");
			
			frmActivityDetails.initialize(mapping);
			
			frmActivityDetails.set("claimtype", claimtype);
			frmActivityDetails.set("activityDtlSeqId", activityDtlSeqId);
			frmActivityDetails.set("activityServiceType", activityServiceType);
			frmActivityDetails.set("claimNo", claimNo);
			frmActivityDetails.set("claimSeqID", claimSeqID);
			frmActivityDetails.set("clinicianId", clinicianId);
			frmActivityDetails.set("clinicianName", clinicianName);
			frmActivityDetails.set("networkProviderType", networkProviderType);
			frmActivityDetails.set("activityStartDate", activityStartDate);// 1,2--NON-edit
			frmActivityDetails.set("amountAllowed", "Y");// Amount Allowed				
			frmActivityDetails.set("activityType", activityType);// activityType	
			frmActivityDetails.set("claimReceiveDate", claimReceiveDate); // claimReceiveDate	
			request.getSession().setAttribute("frmActivityDetails", frmActivityDetails);
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivitydetails));
		}// end of catch(Exception exp)
	}// viewActivityDetails()
	
	public ActionForward addDenialDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
		setLinks(request);
		log.debug("Inside ClaimGenealAction addDenialDesc");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			String denialCode = request.getParameter("denialCode");
			denialCode = (denialCode == null) ? "" : denialCode;
			String denialDesc = request.getParameter("denialDescription");
			denialDesc = (denialDesc == null) ? "" : denialDesc;
			HttpSession session = request.getSession();
			LinkedHashMap<String, String> activityDenialList = (LinkedHashMap<String, String>) session
					.getAttribute("activityDenialList");
			if (activityDenialList == null) {
				activityDenialList = new LinkedHashMap<String, String>();
		}
			activityDenialList.put(denialCode, denialDesc);
			frmActivityDetails.set("denialCode", "");
			frmActivityDetails.set("denialDescription", "");
			session.setAttribute("frmActivityDetails", frmActivityDetails);
			session.setAttribute("activityDenialList", activityDenialList);
			request.setAttribute("successMsg", "Added Successfully");
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivitydetails));
		}// end of catch(Exception exp)
	}// end of addDenialDesc(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward deleteDenialDesc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		setLinks(request);
		log.debug("Inside ClaimGenealAction deleteDenialDesc");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			String denialCode = request.getParameter("denialCode");
			String denialDescription=null;
			HttpSession session = request.getSession();
			LinkedHashMap<String, String> activityDenialList = (LinkedHashMap<String, String>) session
					.getAttribute("activityDenialList");
			LinkedHashMap<String, String> removedDenialList =null; 
			if(session.getAttribute("removedDenialList")!=null)
				removedDenialList=(LinkedHashMap<String, String>)session.getAttribute("removedDenialList");
			else
				removedDenialList=new LinkedHashMap<String, String>();
			denialDescription=activityDenialList.get(denialCode);
			removedDenialList.put(denialCode, denialDescription);
			
			if (activityDenialList != null) {
			activityDenialList.remove(denialCode);
			session.setAttribute("activityDenialList", activityDenialList);
		}
			if (removedDenialList != null) {
				session.setAttribute("removedDenialList", removedDenialList);
			}
		request.setAttribute("successMsg", "Deleted Successfully");
		frmActivityDetails.set("denialCode", "");
		frmActivityDetails.set("denialDescription", "");
		session.setAttribute("frmActivityDetails", frmActivityDetails);
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivitydetails));
		}// end of catch(Exception exp)
	}// end of deleteDenialDesc(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward resetActivityDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		setLinks(request);
		log.debug("Inside ClaimGenealAction resetActivityDetails");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			String claimNo = frmActivityDetails.getString("claimNo");
			String claimSeqID = frmActivityDetails.getString("claimSeqID");
			String activityStartDate = frmActivityDetails
					.getString("activityStartDate");
			String clinicianId = frmActivityDetails.getString("clinicianId");
			String clinicianName = frmActivityDetails
					.getString("clinicianName");
			String networkProviderType = frmActivityDetails
					.getString("networkProviderType");
			String discountFlag = frmActivityDetails.getString("discountFlag");
			String claimType = frmActivityDetails.getString("claimtype");
			// String
			// activityDtlSeqId=frmActivityDetails.getString("activityDtlSeqId");
			frmActivityDetails.initialize(mapping);
			frmActivityDetails.set("claimNo", claimNo);
			frmActivityDetails.set("claimSeqID", claimSeqID);
			// frmActivityDetails.set("activityDtlSeqId", activityDtlSeqId);
			frmActivityDetails.set("activityStartDate", activityStartDate);// 1,2--NON-edit
			frmActivityDetails.set("clinicianId", clinicianId);
			frmActivityDetails.set("networkProviderType", networkProviderType);
			frmActivityDetails.set("clinicianName", clinicianName);
			frmActivityDetails.set("amountAllowed", "Y");// Amount Allowed
			frmActivityDetails.set("discountFlag", discountFlag);
			frmActivityDetails.set("claimtype", claimType);											
			request.getSession().setAttribute("frmActivityDetails",
					frmActivityDetails);
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivityDetails));
		}// end of catch(Exception exp)
	}// end of resetActivityDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	//Ramana
	
	public ActionForward deleteMember(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside PreAuthGenealAction deleteMember ");
			HttpSession session=request.getSession();
			DynaActionForm frmClaimGeneral= (DynaActionForm) form;
			frmClaimGeneral.set("memberSeqID", "");
			frmClaimGeneral.set("memberId","");
			frmClaimGeneral.set("patientName", "");
			frmClaimGeneral.set("memberAge","");
			frmClaimGeneral.set("emirateId", "");
			frmClaimGeneral.set("payerName", "");
			frmClaimGeneral.set("payerId", "");
			frmClaimGeneral.set("insSeqId","");
			frmClaimGeneral.set("policySeqId", "");
			frmClaimGeneral.set("patientGender", "");
			frmClaimGeneral.set("policyNumber", "");
			frmClaimGeneral.set("corporateName","");
			frmClaimGeneral.set("policyStartDate", "");
			frmClaimGeneral.set("policyEndDate", "");
			frmClaimGeneral.set("nationality", "");
			frmClaimGeneral.set("sumInsured","");
			frmClaimGeneral.set("availableSumInsured","");
			frmClaimGeneral.set("vipYorN","");
			frmClaimGeneral.set("preMemInceptionDt","");
			frmClaimGeneral.set("productName","");
			frmClaimGeneral.set("eligibleNetworks","");
			frmClaimGeneral.set("payerAuthority","");
			frmClaimGeneral.set("preAuthCount","");
		  	frmClaimGeneral.set("clmCount","");			
			//Start - Deleting PreAuth Details of respective enrollment Id
			frmClaimGeneral.set("authNum","");
			frmClaimGeneral.set("preAuthSeqID","");
		  	frmClaimGeneral.set("preAuthApprAmt","");
		  	frmClaimGeneral.set("preAuthApprAmtCurrency","");
			frmClaimGeneral.set("preAuthReceivedDateAsString","");
			//end - Deleting PreAuth Details of respective enrollment Id
			
			request.setAttribute("successMsg", "Member Details Deleted Successfully");
			session.setAttribute("frmClaimGeneral",frmClaimGeneral);
			
			return mapping.findForward("ClaimDetails");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}// end of catch(Exception exp)

	}
	
	
	
	
	
	
	
	
	
	

	public ActionForward observWindow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction addObserv ");
			String preAuthSeqID = (String) request.getParameter("pstatussi");
			String activityDtlSeqId = (String) request
					.getParameter("activityDtlSeqId");
			DynaActionForm frmObservDetails = (DynaActionForm) form;
	frmObservDetails.initialize(mapping);
	frmObservDetails.set("activityDtlSeqId", activityDtlSeqId);
	frmObservDetails.set("preAuthSeqID", preAuthSeqID);
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			ArrayList<String[]> observations = preAuthObject
					.getAllObservDetails(new Long(activityDtlSeqId));
			request.getSession().setAttribute("observations", observations);
	return mapping.findForward("addObservations");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)

	}// end of addObserv(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward editObserDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction editObserDetails ");
			String observSeqId = (String) request.getParameter("observSeqId");
			DynaActionForm frmObservDetails = (DynaActionForm) form;
			String preAuthSeqID = frmObservDetails.get("preAuthSeqID")
					.toString();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			ObservationDetailsVO observationDetailsVO = preAuthObject
					.getObservDetail(new Long(observSeqId));
	observationDetailsVO.setPreAuthSeqID(new Long(preAuthSeqID));
			frmObservDetails = (DynaActionForm) FormUtils.setFormValues(
					"frmObservDetails", observationDetailsVO, this, mapping,
					request);

			request.getSession().setAttribute("frmObservDetails",
					frmObservDetails);
	return mapping.findForward("addObservations");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)

	}// end of editObserDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward closeActivities(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction closeActivities ");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			// frmActivityDetails.set("preAuthSeqID", preAuthSeqID);
			String preAuthNo = frmActivityDetails.get("preAuthNo") == null ? ""
					: frmActivityDetails.get("preAuthNo").toString();
			String preAuthSeqID = frmActivityDetails.get("preAuthSeqID") == null ? "0"
					: frmActivityDetails.get("preAuthSeqID").toString();
			request.setAttribute("preAuthNo", preAuthNo);
			request.setAttribute("preAuthSeqID", preAuthSeqID);
			
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			
	return mapping.findForward("preauthdetail");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)

	}// end of closeActivities(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward getMemberDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction getMemberDetails ");
			ClaimManager claimObject = this.getClaimManagerObject();
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String memberId = frmClaimGeneral.getString("memberId");
			memberId = (memberId == null) ? "" : memberId.trim();
			PreAuthDetailVO preAuthDetailVO = claimObject
					.getMemberDetails(memberId);
			//frmClaimGeneral.set("preAuthSeqID", "");
			//frmClaimGeneral.set("authNum", "");
			if (preAuthDetailVO == null) {
				request.setAttribute("errorMsg", "Given Member ID Is Invalid");
				frmClaimGeneral.set("memberSeqID", "");
				frmClaimGeneral.set("patientName", "");
				frmClaimGeneral.set("memberAge", "");
				frmClaimGeneral.set("emirateId", "");
				frmClaimGeneral.set("payerId", "");
				frmClaimGeneral.set("insSeqId", "");
				frmClaimGeneral.set("policySeqId", "");
				frmClaimGeneral.set("payerName", "");
				frmClaimGeneral.set("patientGender", "");
				frmClaimGeneral.set("policyNumber", "");
				frmClaimGeneral.set("corporateName", "");
				frmClaimGeneral.set("policyStartDate", "");
				frmClaimGeneral.set("policyEndDate", "");
				frmClaimGeneral.set("nationality", "");
				frmClaimGeneral.set("sumInsured", "");
				frmClaimGeneral.set("availableSumInsured", "");
				frmClaimGeneral.set("policyType", "");
  frmClaimGeneral.set("emirateId",""); 
				frmClaimGeneral.set("productName", "");
				frmClaimGeneral.set("payerAuthority", "");
frmClaimGeneral.set("vipYorN","");
frmClaimGeneral.set("clmMemInceptionDate","");
				frmClaimGeneral.set("memCoveredAlert", "Patient is not a covered member"); 
				
  }else{
	  request.setAttribute("successMsg","Member ID Is Valid");

				frmClaimGeneral.set("memberIDValidYN", "Y");
				frmClaimGeneral.set("memberSeqID",
						TTKCommon.checkNull(preAuthDetailVO.getMemberSeqID())
								.toString());
				frmClaimGeneral.set("patientName",
						preAuthDetailVO.getClaimantName());
				frmClaimGeneral.set("memberAge",
						TTKCommon.checkNull(preAuthDetailVO.getMemberAge())
								.toString());
				frmClaimGeneral	.set("emirateId", preAuthDetailVO.getEmirateId());
				frmClaimGeneral.set("payerId", preAuthDetailVO.getPayerId());
				frmClaimGeneral.set("insSeqId",
						TTKCommon.checkNull(preAuthDetailVO.getInsSeqId())
								.toString());
				frmClaimGeneral.set("policySeqId",
						TTKCommon.checkNull(preAuthDetailVO.getPolicySeqId())
								.toString());
				frmClaimGeneral
						.set("payerName", preAuthDetailVO.getPayerName());
				frmClaimGeneral.set("patientGender",
						preAuthDetailVO.getPatientGender());
				frmClaimGeneral.set("policyNumber",
						preAuthDetailVO.getPolicyNumber());
				frmClaimGeneral.set("corporateName",
						preAuthDetailVO.getCorporateName());
				frmClaimGeneral.set("policyStartDate",
						preAuthDetailVO.getPolicyStartDate());
				frmClaimGeneral.set("policyEndDate",
						preAuthDetailVO.getPolicyEndDate());
				frmClaimGeneral.set("nationality",
						preAuthDetailVO.getNationality());
				frmClaimGeneral.set("sumInsured",
						TTKCommon.checkNull(preAuthDetailVO.getSumInsured())
								.toString());
				frmClaimGeneral.set(
						"availableSumInsured",
						TTKCommon.checkNull(
								preAuthDetailVO.getAvailableSumInsured())
								.toString());
				frmClaimGeneral.set("policyType",
						preAuthDetailVO.getPolicyType());
				frmClaimGeneral.set("productName",preAuthDetailVO.getProductName());
				frmClaimGeneral.set("payerAuthority", preAuthDetailVO.getPayerAuthority());
				frmClaimGeneral.set("vipYorN",preAuthDetailVO.getVipYorN());
	  
	 
				frmClaimGeneral.set("clmMemInceptionDate",preAuthDetailVO.getClmMemInceptionDate());
	  frmClaimGeneral.set("memCoveredAlert", ""); 
  }
			request.getSession().setAttribute("frmClaimGeneral",
					frmClaimGeneral);
			return mapping.findForward("claimdetails");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// getMemberDetails

	public ActionForward calculateClaimAmount(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction calculateClaimAmount ");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			HttpSession session = request.getSession();
			String claimSeqID = (String) frmClaimGeneral.get("claimSeqID");
			String hospitalSeqID = (String) frmClaimGeneral
					.get("providerSeqId");
				hospitalSeqID = (hospitalSeqID == null || hospitalSeqID.length() < 1) ? "0"
					: hospitalSeqID;
			StringBuffer strCaption = new StringBuffer();
			PreAuthDetailVO preAuthDetailVO = null;
			ClaimManager claimObject = this.getClaimManagerObject();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			
			// calculate amounts
			claimObject.calculateClaimAmount(new Long(claimSeqID), new Long(
					hospitalSeqID), TTKCommon.getUserSeqId(request));

			Object[] claimResults = claimObject.getClaimDetails(new Long(
					claimSeqID));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			
			preAuthDetailVO.setQtyAndDaysAlert(preAuthDetailVO.getAlertMsg());  // added by govind
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
			session.setAttribute("completedYN", preAuthDetailVO.getCompletedYNFlag());
			ArrayList<String> alProvicerCopay	=	preAuthObject.getProviderSpecificCopay(new Long(claimSeqID),"CLM");
			if(alProvicerCopay.size()>0){
				preAuthDetailVO.setProviderCopay(alProvicerCopay.get(0));
				preAuthDetailVO.setProviderDeductible(alProvicerCopay.get(1));
				preAuthDetailVO.setProviderSuffix(alProvicerCopay.get(2));
			}
			session.setAttribute("alProvicerCopay", alProvicerCopay);
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);
			this.addToWebBoard(preAuthDetailVO, request, "CLM");

			strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());
			request.setAttribute("successMsg", "Record updated successfully!");
			
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			
			return this.getForward(strClaimdetails, mapping, request);

		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of calculateClaimAmount(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward saveAndCompleteClaim(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction saveAndCompleteClaim");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;			
			Calendar calendar = Calendar.getInstance();       
		    Date startTim = (Date) calendar.getTime();

			String claimStatus = frmClaimGeneral.getString("claimStatus");
			String approvedAmount = frmClaimGeneral.getString("approvedAmount");
			approvedAmount = (approvedAmount == null || approvedAmount.length() < 1) ? "0"
					: approvedAmount;

			if ("APR".equals(claimStatus)
					&& (new Double(approvedAmount).doubleValue() <= 0)) {

				request.setAttribute("errorMsg",
						"Approved Amount Should Be Greater Than Zero");
	return this.getForward(strClaimdetails, mapping, request);
			}
			PreAuthDetailVO preAuthDetailVO = new PreAuthDetailVO();
			preAuthDetailVO = (PreAuthDetailVO) FormUtils.getFormValues(
					frmClaimGeneral, this, mapping, request);
			ClaimManager claimObject = this.getClaimManagerObject();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			StringBuffer strCaption = new StringBuffer();
			HttpSession session = request.getSession();
	preAuthDetailVO.setAddedBy(TTKCommon.getUserSeqId(request));

    
	
	claimObject.saveAndCompleteClaim(preAuthDetailVO);

			Object[] claimResults = claimObject.getClaimDetails(new Long(
					frmClaimGeneral.getString("claimSeqID")));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			
			
			request.getSession().setAttribute("preAuthStatus", null);
			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
	         session.setAttribute("completedYN", preAuthDetailVO.getCompletedYNFlag());
	//CLAIM SETTLEMENT DETAILS S T A R T S
	String[] strClaimSettlementDtls	=	claimObject.getClaimSettleMentDtls(new Long(frmClaimGeneral.getString("claimSeqID")));
	ArrayList<String[]> overRideAlertList	=	preAuthObject.getOverrideRemarksList("C",new Long(frmClaimGeneral.getString("claimSeqID")));
	session.setAttribute("overRideAlertList", overRideAlertList);
	request.setAttribute("strClaimSettlementDtls", strClaimSettlementDtls);
	request.getSession().setAttribute("vishwa", strClaimSettlementDtls[5]);
	//CLAIM SETTLEMENT DETAILS E N D S

			this.addToWebBoard(preAuthDetailVO, request, "CLM");

				strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());

			request.setAttribute("successMsg", "Claim completed successfully!");

			request.setAttribute("focusObj", request.getParameter("focusObj"));
			String claimStatusforCheck=preAuthDetailVO.getClaimStatus();
			if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("APR"))){
				request.getSession().setAttribute("claimStatus", "Approved");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("INP"))){
				request.getSession().setAttribute("claimStatus", "In Progress");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("REJ"))){
				request.getSession().setAttribute("claimStatus", "Rejected");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("PCN"))){
				request.getSession().setAttribute("claimStatus", "Canceled");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("REQ"))){
				request.getSession().setAttribute("claimStatus", "Required Information");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("PCO"))){
				request.getSession().setAttribute("claimStatus", "Closed");
			}else{
				request.getSession().setAttribute("claimStatus", preAuthDetailVO.getClaimStatus());
			}
			request.getSession().setAttribute("auditStatus",preAuthDetailVO.getAuditStatus());
			request.getSession().setAttribute("assigntouserseqid", preAuthDetailVO.getAssignUserSeqID());
			return this.getForward(strClaimdetails, mapping, request);

		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of saveAndCompleteClaim(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward deleteShortfallsDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction deleteShortfallsDetails ");
			PreAuthDetailVO preAuthDetailVO = null;
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			ClaimManager claimObject = this.getClaimManagerObject();
			HttpSession session = request.getSession();
			StringBuffer strCaption = new StringBuffer();
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String claimSeqID = (String) frmClaimGeneral.get("claimSeqID");
			String shortFallSeqId = (String) frmClaimGeneral
					.get("shortFallSeqId");

			preAuthObject.deleteShortfallsDetails(new String[] { claimSeqID,
					shortFallSeqId, "CLM" , TTKCommon.getUserSeqId(request).toString() });
			request.setAttribute("successMsg",
					"Shortfalls Details Deleted Successfully");

			Object[] claimResults = claimObject.getClaimDetails(new Long(
					frmClaimGeneral.getString("claimSeqID")));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);

			this.addToWebBoard(preAuthDetailVO, request, "CLM");

				strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());
			
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			
		return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of deleteShortfallsDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to add/update the record. Finally it forwards to the
	 * appropriate view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
 */
	
	public ActionForward generateClaimLetterMember(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String claimSeqID = request.getParameter("claimSeqID");
			String claimSettelmentNo = request.getParameter("claimSettelmentNo");
			String claimStatus =  request.getParameter("claimStatus");
			String Format=request.getParameter("format");
			JasperReport mainJasperReport = null;
			JasperReport diagnosisJasperReport = null;
			JasperReport activityJasperReport = null;
			JasperPrint mainJasperPrint = null;
		String parameter = "";
		String mainJrxmlfile="";
		String activityJrxmlfile="";
		String strPdfFile="";
		//String diagnosisJrxmlfile="";
		TTKReportDataSource mainTtkReportDataSource = null;
		TTKReportDataSource diagnosisTtkReportDataSource = null;
		TTKReportDataSource activityTtkReportDataSource = null;
		 HashMap<String, Object> hashMap = new HashMap<String, Object>();
		parameter="|"+claimSeqID+"|"+claimStatus+"|CLM|";
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		
			
			if("GENERAL".equalsIgnoreCase(Format) ||"ASCANA".equalsIgnoreCase(Format))
			{
				
		     mainJrxmlfile = "generalreports/MemberClaimApprovalOrDenialLetter.jrxml";
			 activityJrxmlfile = "generalreports/MemberClaimActivitiesDoc.jrxml";
			 mainTtkReportDataSource=new TTKReportDataSource("MemberClaimLetterFormat",parameter);
			 String diagnosisJrxmlfile = "generalreports/ClaimDiagnosisDoc.jrxml";
			 

			 diagnosisJrxmlfile = "generalreports/ClaimDiagnosisDoc.jrxml";
			 

				 boas = new ByteArrayOutputStream();

				 strPdfFile = TTKPropertiesReader
						.getPropertyValue("authorizationrptdir")
						+ claimSettelmentNo + ".pdf";
				JasperReport emptyReport = JasperCompileManager
						.compileReport("generalreports/EmptyReprot.jrxml");
				//mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
		/* HashMap<String, Object> hashMap = new HashMap<String, Object>();*/
			
			
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
				//JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
				JasperExportManager.exportReportToPdfFile(mainJasperPrint,strPdfFile);
			}
			else
			{
				switch (Format) {
				case "TAKAFUL":
					 mainJrxmlfile = "generalreports/ClaimTakafulLetter.jrxml";
					break;
				case "NLGI":
					 mainJrxmlfile = "generalreports/ClaimNlgiLetter.jrxml";
					break;
				case "OMAN":
					 mainJrxmlfile = "generalreports/ClaimOmanLetter.jrxml";
					break;

				default:
					break;
				}
				
			
			 mainTtkReportDataSource=new TTKReportDataSource("TakafulLetter",parameter);
			 
			 boas = new ByteArrayOutputStream();

			 strPdfFile = TTKPropertiesReader
					.getPropertyValue("authorizationrptdir")
					+ claimSettelmentNo + ".pdf";
			JasperReport emptyReport = JasperCompileManager
					.compileReport("generalreports/EmptyReprot.jrxml");
			
		ResultSet main_report_RS=mainTtkReportDataSource.getResultData();
		
		mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
		
	 
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
			//JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
			JasperExportManager.exportReportToPdfFile(mainJasperPrint,
					strPdfFile);
			 
			}
			
	 frmClaimGeneral.set("letterPath", strPdfFile);
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
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of generateClaimLetter(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,
		// HttpServletResponse response)
	
	
	
	
	
	
	
	
	
	public ActionForward generateClaimLetter(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		setLinks(request);
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			
			String claimSeqID = frmClaimGeneral.getString("claimSeqID");
			String claimSettelmentNo = frmClaimGeneral
					.getString("claimSettelmentNo");
			String claimStatus = frmClaimGeneral.getString("claimStatus");
			String claimType 	= frmClaimGeneral.getString("claimType");
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
					.getPropertyValue("authorizationrptdir")
					+ claimSettelmentNo + ".pdf";
			JasperReport emptyReport = JasperCompileManager
					.compileReport("generalreports/EmptyReprot.jrxml");
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
		 String compPhnNO=main_report_RS.getString("COMP_PHN_NO");
		 hashMap.put("compPhnNO", compPhnNO);
		 main_report_RS.beforeFirst();
				mainJasperPrint = JasperFillManager.fillReport(
						mainJasperReport, hashMap, mainTtkReportDataSource);
			}// end of else
			JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
			JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
			JasperExportManager.exportReportToPdfFile(mainJasperPrint,
					strPdfFile);
	 frmClaimGeneral.set("letterPath", strPdfFile);
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
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of generateClaimLetter(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,
		// HttpServletResponse response)

	/**
	 * This method is used to Send the Authorization Details generated Approval
	 * or Rejected letters to recipient. Finally it forwards to the appropriate
	 * view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
 */
	public ActionForward sendClaimLetter(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		try {
		setLinks(request);
		log.debug("inside ClaimGeneralAction sendClaimLetter");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String claimStatus = frmClaimGeneral.getString("claimStatus");
			String claimSettelmentNo = frmClaimGeneral
					.getString("claimSettelmentNo");
			String claimSeqID = frmClaimGeneral.getString("claimSeqID");
		String strIdentifier = "";
			CommunicationManager commManagerObject = this
					.getCommunicationManagerObject();

			if ("REJ".equals(claimStatus)) {
				strIdentifier = "CLAIM_MR_REJECTED";
			} else if ("APR".equals(claimStatus)) {
				strIdentifier = "CLAIM_MR_APPROVE";
			}

			// CommunicationOptionVO communicationOptionVO = null;
		Long lngUserID = TTKCommon.getUserSeqId(request);
			String strAuthpdf = TTKPropertiesReader
					.getPropertyValue("authorizationrptdir")
					+ claimSettelmentNo + ".pdf";
			// String[] strCommArray = {"SMS","EMAIL","FAX"};
		File file = new File(strAuthpdf);

			if (file.exists()) {
				commManagerObject.sendAuthorization(
						new Long(claimSeqID).longValue(), strIdentifier,
						lngUserID);
				request.setAttribute("successMsg", "Letter Sent Successfully");
			}// end of if(file.exists())
			else {
				request.setAttribute("errorMsg",
						"Please Generate The Letter Before Sending.");
				/*
				 * TTKException expTTK = new TTKException();
				 * expTTK.setMessage("error.authpdf"); throw expTTK;
				 */
			}// end of else
				// StringBuffer strCaption= new StringBuffer();

		return this.getForward(strClaimDetail, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of sendPreAuthLetter(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,
		// HttpServletResponse response)

	public ActionForward getAllClaimDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction getAllClaimDetails ");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			Object objClaimSeqID = request.getAttribute("claimSeqID");
			String claimSeqID = (objClaimSeqID==null||objClaimSeqID.toString().trim().equals("")) ? "0" : objClaimSeqID.toString();
			StringBuffer strCaption = new StringBuffer();
			frmClaimGeneral.initialize(mapping);

			HttpSession session = request.getSession();
			PreAuthDetailVO preAuthDetailVO = null;
			ClaimManager claimObject = this.getClaimManagerObject();
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();

			Object[] claimResults = claimObject.getClaimDetails(new Long(
					claimSeqID));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			if ("N".equals(frmClaimGeneral.getString("networkProviderType"))) {
				session.setAttribute("providerStates",
						TTKCommon.getStates(frmClaimGeneral
								.getString("providerCountry")));
				session.setAttribute("providerAreas", TTKCommon
						.getAreas(frmClaimGeneral.getString("providerEmirate")));
			}
			session.setAttribute("encounterTypes",
					preAuthObject.getEncounterTypes(frmClaimGeneral
							.getString("benefitType")));

			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);

			this.addToWebBoard(preAuthDetailVO, request, "CLM");

			strCaption.append(" Edit");
			strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
					+ " ]");
			strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
					+ " ]");
			frmClaimGeneral.set("caption", strCaption.toString());

			return this.getForward(strClaimdetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}// end of catch(Exception exp)

	}// end of getAllClaimDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	public ActionForward historyList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction historyList");
			HttpSession session = request.getSession();
			DynaActionForm frmHistoryList = (DynaActionForm) form;
			PreAuthManager preAuthObject = null;
			preAuthObject = this.getPreAuthManagerObject();
			

			if ("Y".equals(request.getParameter("Entry"))){
				frmHistoryList.set("historyMode", "CLM");
				frmHistoryList.set("policyDet", "");
				frmHistoryList.set("policyNo", "");
				frmHistoryList.set("policyStartDt", "");
				frmHistoryList.set("policyEndDt", "");
				frmHistoryList.set("sortBy", "claim_number");
				frmHistoryList.set("benefitType", "");
			}
			DynaActionForm frmClaimGeneral = (DynaActionForm) session .getAttribute("frmClaimGeneral");
			
			if (frmClaimGeneral == null
					|| frmClaimGeneral.getString("memberSeqID") == null
					|| frmClaimGeneral.getString("memberSeqID").length() < 1) {
				session.setAttribute("claimHistoryList", null);
				frmHistoryList.set("memberSeqID", null);
				frmHistoryList.set("sum",null);
				session.setAttribute("claimHistoryList", new ArrayList<String[]>());
				session.setAttribute("frmHistoryList", frmHistoryList);
			request.setAttribute("errorMsg", "Save Claim Details");
				return this.getForward(strCLaimHistoryList, mapping, request);
			}// end of if(PreAuthWebBoardHelper.checkWebBoardId(request)==null)
		// call the business layer to get the Pre-Auth detail
				// frmHistoryList.set("preAuthSeqID",
				// frmPreAuthGeneral.getString("preAuthSeqID"));
			
		/*	if("PAT".equals(frmHistoryList.getString("historyMode")))
			{
				frmHistoryList.set("sortBy", "pre_auth_number");
			}
			if("CLM".equals(frmHistoryList.getString("historyMode")))
			{
				frmHistoryList.set("sortBy", "claim_number");
			}*/
			
			Long memberSeqID = new Long(frmClaimGeneral.getString("memberSeqID"));
			String memberId	=	frmClaimGeneral.getString("memberId");
			String PolicyNo = frmClaimGeneral.getString("policyNumber");
			String strPolicySeqId="";
					
			if (frmHistoryList.getString("policyDet").equals(""))
			{
				strPolicySeqId = frmClaimGeneral.getString("policySeqId");
			 	strPolicySeqId = (strPolicySeqId == null || strPolicySeqId.length() < 1) ? "0" : strPolicySeqId;
			}
			else
			{
				strPolicySeqId = frmHistoryList.getString("policyDet"); 
				strPolicySeqId = (strPolicySeqId == null || strPolicySeqId.length() < 1) ? "0" : strPolicySeqId;
			}
			
			Long policySeqId = Long.parseLong(strPolicySeqId);
			ArrayList<String[]> authorizationList = preAuthObject
					.getPreauthHistoryWithBenefitsList(
							new Long(frmClaimGeneral.getString("memberSeqID")),
							frmHistoryList.getString("historyMode"),
							memberId, policySeqId,
							frmHistoryList.getString("benefitType"),frmHistoryList.getString("sortBy"),frmHistoryList.getString("ascOrDesc"));
			frmHistoryList.set("memberSeqID", memberSeqID);
			frmHistoryList.set("memberId", memberId);
			frmHistoryList.set("PolicySeqId", strPolicySeqId);
			frmHistoryList.set("policyNumber", PolicyNo);
			
			frmHistoryList.set("sum", ((String[])authorizationList.get(authorizationList.size()-1))[0]);
			session.setAttribute("claimHistoryList", authorizationList);
			
						ArrayList<Object> PolicyDet = null;
						ArrayList<Object> policydetail = null;
						PolicyDet = preAuthObject.getPolicyDetails(memberId);
						policydetail=(ArrayList<Object>)PolicyDet.get(0);
						frmHistoryList.set("policyDetails", policydetail);
			
						ArrayList policydesc = new ArrayList();
						policydesc = preAuthObject.getPolicyInfo(memberId,policySeqId);
						if(policydesc != null && policydesc.size()>=1)
						{	
							frmHistoryList.set("policyStatus", (String)policydesc.get(0));
							frmHistoryList.set("policyStartDt", (String)policydesc.get(1));
							frmHistoryList.set("policyEndDt", (String)policydesc.get(2));
						}
						if (TTKCommon.checkNull(frmHistoryList.getString("policyDet")).length()< 1)
						{
							String[] frmPolicyNo = PolicyNo.split("\\(");
							frmHistoryList.set("policyNo",frmPolicyNo[0]);
						}
						if (TTKCommon.checkNull(frmHistoryList.getString("policyDet")).length()> 1)
						{
							frmHistoryList.set("policyNo",frmHistoryList.getString("policyNoLabel"));
						}
			session.setAttribute("frmHistoryList", frmHistoryList);
	return this.getForward(strCLaimHistoryList, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strCLaimHistoryList));
		}// end of catch(Exception exp)
	}// end of historyList(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward doViewHistory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction doViewHistory");
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			DynaActionForm frmHistoryList = (DynaActionForm) form;
			
			// check if user trying to hit the tab directly with out selecting
			// the hospital
			Document historyDoc = null;
			String authSeqID = request.getParameter("authSeqID");
			authSeqID = (authSeqID == null || authSeqID.length() < 1) ? "0"
					: authSeqID;
			String historyMode = frmHistoryList.getString("historyMode");
			if ("PAT".equals(historyMode)) {
				historyDoc = preAuthObject
						.getPreAuthHistory(new Long(authSeqID));
				frmHistoryList.set("preauthseqid",authSeqID);
				frmHistoryList.set("historyMode",historyMode);
				request.getSession().setAttribute("frmHistoryList", frmHistoryList);
				request.setAttribute("preAuthHistoryDoc", historyDoc);
	return this.getForward(strPreAuthViewHistory, mapping, request);
			} else if ("CLM".equals(historyMode)) {
				historyDoc = preAuthObject.getClaimHistory(new Long(authSeqID));
				frmHistoryList.set("clmseqid",authSeqID);
				frmHistoryList.set("historyMode",historyMode);
				request.getSession().setAttribute("frmHistoryList", frmHistoryList);
				request.setAttribute("claimHistoryDoc", historyDoc);
	return this.getForward(strClaimViewHistory, mapping, request);
			}
			return this.getForward(strPreAuthViewHistory, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strCLaimHistoryList));
		}// end of catch(Exception exp)
	}// end of doViewHistory(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to Override. Finally it forwards to the appropriate
	 * view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
	 */
	public ActionForward doOverride(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction doOverride ");
			DynaActionForm frmPreAuthGeneral = (DynaActionForm) form;
			PreAuthDetailVO preAuthDetailVO = null;
			PreAuthManager preAuthObject = null;
			ClaimManager claimObject = null;
			String strActiveTab = TTKCommon.getActiveLink(request);
			String strDetail = "";
			if (strActiveTab.equals(strPre_Authorization)) {
				strDetail = "preauthdetail";
				preAuthObject = this.getPreAuthManagerObject();
				//	call the bussiness layer to override the completed PreAuth
				preAuthDetailVO = preAuthObject.overridePreauth(
						PreAuthWebBoardHelper.getPreAuthSeqId(request),
						PreAuthWebBoardHelper.getEnrollmentDetailId(request),
						TTKCommon.getUserSeqId(request));
			}// end of if(strActiveTab.equals(strPre_Authorization))
			if (strActiveTab.equals(strClaims)) {
				strDetail = "claimsdetail";
				claimObject = this.getClaimManagerObject();
				// call the bussiness layer to override the completed Claims
				preAuthDetailVO = claimObject.overrideClaim(
						ClaimsWebBoardHelper.getClaimsSeqId(request),
						TTKCommon.getUserSeqId(request));
			}// end of if(strActiveTab.equals(strClaims))

			if (preAuthDetailVO != null) {
				// set the review information back to form
				frmPreAuthGeneral.set("eventSeqID", preAuthDetailVO
						.getEventSeqID().toString());
				frmPreAuthGeneral.set("reviewCount", preAuthDetailVO
						.getReviewCount().toString());
				frmPreAuthGeneral.set("requiredReviewCnt", preAuthDetailVO
						.getRequiredReviewCnt().toString());
				frmPreAuthGeneral.set("review", preAuthDetailVO.getReview()
						.toString());
				frmPreAuthGeneral.set("eventName", preAuthDetailVO
						.getEventName().toString());

				// If Workflow is overrided set flag as N
				if (!(preAuthDetailVO.getEventName().contains("Complete"))) {
					frmPreAuthGeneral.set("showCodingOverrideYN", "Y");
					frmPreAuthGeneral.set("completedYN", "N");
				}// end of if("Complete".equals(preAuthDetailVO.getEventName()))
			}// end of if(preAuthDetailVO!=null)
			return this.getForward(strDetail, mapping, request);
		}// end of try

		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of doOverride(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to Override. Finally it forwards to the appropriate
	 * view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
	 */
	public ActionForward overridClaimDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction overridClaimDetails");

			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			String overrideRemarks = frmClaimGeneral
					.getString("overrideRemarks");
			String claimSeqID = frmClaimGeneral.getString("claimSeqID");
			String overrideGenRemarks=request.getParameter("overrideGenRemarks");
			String overrideGenSubRemarks=request.getParameter("overrideGenSubRemarks");
			
			 if("undefined".equals(overrideGenSubRemarks) || overrideGenSubRemarks==null  || "".equals(overrideGenSubRemarks))
				 overrideGenSubRemarks="";
			HttpSession session = request.getSession();
			PreAuthDetailVO preAuthDetailVO = null;
			ClaimManager claimObject = null;
				claimObject = this.getClaimManagerObject();
		String overrideSuspectFlagFromDB = (String) session.getAttribute("overrideSuspectFlag");
		long rowUpd=0;
		if(!TTKCommon.isAuthorized(request, "OverrideForFraudStatus") && overrideSuspectFlagFromDB.equalsIgnoreCase("Y")){
			request.setAttribute("errorMsg",
					"No Access to Override this Suspect Claim");
		}else{
			 rowUpd = claimObject.overridClaimDetails(claimSeqID,
					overrideRemarks, TTKCommon.getUserSeqId(request),overrideGenRemarks,overrideGenSubRemarks);
		}
			if (rowUpd >= 1)
				request.setAttribute("successMsg",
						"Claim Details Override Successfully");

			Object[] claimResults = claimObject.getClaimDetails(new Long(
					frmClaimGeneral.getString("claimSeqID")));
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
					"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
			frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
			String claimStatusforCheck=preAuthDetailVO.getClaimStatus();
			if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("APR"))){
				request.getSession().setAttribute("claimStatus", "Approved");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("INP"))){
				request.getSession().setAttribute("claimStatus", "In Progress");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("REJ"))){
				request.getSession().setAttribute("claimStatus", "Rejected");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("PCN"))){
				request.getSession().setAttribute("claimStatus", "Canceled");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("REQ"))){
				request.getSession().setAttribute("claimStatus", "Required Information");
			}else if(claimStatusforCheck!=null&&(claimStatusforCheck.equals("PCO"))){
				request.getSession().setAttribute("claimStatus", "Closed");
			}else{
				request.getSession().setAttribute("claimStatus", preAuthDetailVO.getClaimStatus());
			}
			request.getSession().setAttribute("preAuthStatus", null);
			session.setAttribute("claimDiagnosis", claimResults[1]);
			session.setAttribute("claimActivities", claimResults[2]);
			session.setAttribute("claimShortfalls", claimResults[3]);
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
		   session.setAttribute("completedYN", preAuthDetailVO.getCompletedYNFlag());
		   session.setAttribute("auditStatus",preAuthDetailVO.getAuditStatus());
				return this.getForward(strClaimDetail, mapping, request);
		}// end of try

		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}// end of doOverride(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward rejectClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws TTKException
	{
		//ClaimManager claimObject=this.getClaimManagerObject();
		
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction saveAndCompleteClaim");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
            //frmClaimGeneral.initialize(mapping);
			String claimStatus = frmClaimGeneral.getString("claimStatus");
			String approvedAmount = frmClaimGeneral.getString("approvedAmount");
			approvedAmount = (approvedAmount == null || approvedAmount.length() < 1) ? "0"
					: approvedAmount;

			if ("APR".equals(claimStatus)
					&& (new Double(approvedAmount).doubleValue() <= 0)) {

				request.setAttribute("errorMsg",
						"Approved Amount Should Be Greater Than Zero");
	return this.getForward(strClaimdetails, mapping, request);
			}
			PreAuthDetailVO preAuthDetailVO = new PreAuthDetailVO();
			preAuthDetailVO = (PreAuthDetailVO) FormUtils.getFormValues(
					frmClaimGeneral, this, mapping, request);
			String checkFlag				=	request.getParameter("checkFlag");
        	String medicalOpinionRemarks	=	request.getParameter("medicalOpinionRemarks");
        	String overrideRemarks			=	request.getParameter("overrideRemarks");
        	String finalRemarks				=	request.getParameter("finalRemarks");
        	String internalRemarks			=	request.getParameter("internalRemarks");
        	if("YES".equals(checkFlag)){
        		preAuthDetailVO.setMedicalOpinionRemarks(medicalOpinionRemarks);
        		preAuthDetailVO.setOverrideRemarks(overrideRemarks);
        		preAuthDetailVO.setFinalRemarks(finalRemarks);
        		preAuthDetailVO.setInternalRemarks(internalRemarks);
        	}
			ClaimManager claimObject = this.getClaimManagerObject();
			StringBuffer strCaption = new StringBuffer();
			HttpSession session = request.getSession();
	preAuthDetailVO.setAddedBy(TTKCommon.getUserSeqId(request));
	claimObject.getRejectedClaimDetails(new Long(request.getParameter("claimSeqID")),request.getParameter("denailcode"),medicalOpinionRemarks,overrideRemarks,finalRemarks,TTKCommon.getUserSeqId(request),internalRemarks);

		Object[] claimResults = claimObject.getClaimDetails(new Long(
				request.getParameter("claimSeqID")));
		preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
		frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues(
				"frmClaimGeneral", preAuthDetailVO, this, mapping, request);
		frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
		session.setAttribute("claimDiagnosis", claimResults[1]);
		session.setAttribute("claimActivities", claimResults[2]);
		session.setAttribute("claimShortfalls", claimResults[3]);
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);
         session.setAttribute("completedYN", preAuthDetailVO.getCompletedYNFlag());
		this.addToWebBoard(preAuthDetailVO, request, "CLM");

			strCaption.append(" Edit");
		strCaption.append(" [ " + frmClaimGeneral.getString("patientName")
				+ " ]");
		strCaption.append(" [ " + frmClaimGeneral.getString("memberId")
				+ " ]");
		frmClaimGeneral.set("caption", strCaption.toString());

		request.setAttribute("successMsg", "Claim completed successfully!");
		
		if("INP".equals(preAuthDetailVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "In Progress");	
		}else if("APR".equals(preAuthDetailVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Approved");
		}else if("REJ".equals(preAuthDetailVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Rejected");
		}else if("PCN".equals(preAuthDetailVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Cancel");
		}else if("REQ".equals(preAuthDetailVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Required Information");
		}else{
			request.getSession().setAttribute("claimStatus", preAuthDetailVO.getClaimStatus());
		}
		request.getSession().setAttribute("preAuthStatus", null);
		return this.getForward(strClaimdetails, mapping, request);

	}// end of try
	catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
	}// end of catch(TTKException expTTK)
	catch (Exception exp) {
		return this.processExceptions(request, mapping, new TTKException(
				exp, strClaimDetail));
	}// end of catch(Exception exp)
		
		
	}

	/**
	 * Returns the PreAuthManager session object for invoking methods on it.
	 * 
	 * @return PreAuthManager session object which can be used for method
	 *         invokation
	 * @exception throws TTKException
	 */
	public LinkedHashMap<String, String> getClaimDiagDetails() throws TTKException
	{
		ClaimManager claimManager=this.getClaimManagerObject();
		return claimManager.getClaimDiagDetails();
		
	}
	
	/*
	 * Reading and Displaying File Stored in DB
	 */
	
	 public ActionForward viewShortfalldoc(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
		    
			 ByteArrayOutputStream baos=null;
		    OutputStream sos = null;
		    FileInputStream fis = null; 
		    BufferedInputStream bis =null;
	    	String strFileNoRecords = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/noRecordsFound.pdf";
	    	

		  try{
				
			  
			  	PreAuthManager preAuthObject = this.getPreAuthManagerObject();
				String shortfallSeqID = request.getParameter("shortfallSeqID");		
				
				InputStream iStream	=	preAuthObject.getShortfallDBFile(shortfallSeqID);
				 if((iStream!=null) && (!iStream.equals("")))
				  {
               bis = new BufferedInputStream(iStream);
               baos=new ByteArrayOutputStream();
	             response.setContentType("application/pdf");
				 response.addHeader("Content-Disposition","attachment; filename=noRecordsFound.pdf");
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
           	    		response.setContentType("application/pdf");
    					response.addHeader("Content-Disposition","attachment; filename=ShortfallReport.pdf");
           	    		sos = response.getOutputStream();
           	    		baos.writeTo(sos);
         			  }
		            }catch(Exception exp)
		            	{
		            		return this.processExceptions(request, mapping, new TTKException(exp,strClaimDetail));
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
	 
	 
	 
	 /*
	  * Change treatMent Type
	  */
	 public ActionForward doChangeTreatmentType(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			try {
				setLinks(request);
				log.debug("Inside ClaimGeneralAction doChangeTreatmentType ");
				DynaActionForm frmClaimGeneral = (DynaActionForm) form;
				HttpSession session = request.getSession();
				PreAuthDetailVO preAuthDetailVO = (PreAuthDetailVO) FormUtils.getFormValues(frmClaimGeneral, this, mapping, request);
				preAuthDetailVO.setDentalOrthoVO(new DentalOrthoVO());
				frmClaimGeneral = setFormValues(preAuthDetailVO,mapping,request);
				session.setAttribute("frmClaimGeneral", frmClaimGeneral);
				return mapping.findForward(strClaimdetails); // this.getForward(strDetail, mapping,// request);
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strClaimdetails));
			}// end of catch(Exception exp)
		}// end of doChangeTreatmentType(ActionMapping mapping,ActionForm
			// form,HttpServletRequest request,HttpServletResponse response)
	 
	 
	 public ActionForward  doViewPolicyTOB(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws TTKException {
		    
			 ByteArrayOutputStream baos=null;
		    OutputStream sos = null;
		    FileInputStream fis = null; 
		    BufferedInputStream bis =null;
		  try{   
				String strFile	=	request.getParameter("filePath");
				PreAuthManager preAuthObject = this.getPreAuthManagerObject();
				 
					
				  String policySeqId = request.getParameter("policySeqId");	
				 
				 
				   InputStream iStream	=	preAuthObject.getPolicyTobFile(policySeqId);
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
			  						return this.processOnlineExceptions(request, mapping, expTTK);
	        				}//end of catch(TTKException expTTK)
		  
		  					catch(Exception exp)
			            	{
			            		return this.processExceptions(request, mapping, new TTKException(exp,strClaimDetail));
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
	 
	private PreAuthManager getPreAuthManagerObject() throws TTKException {
		PreAuthManager preAuthManager = null;
		try {
			if (preAuthManager == null) {
				InitialContext ctx = new InitialContext();
				preAuthManager = (PreAuthManager) ctx
						.lookup("java:global/TTKServices/business.ejb3/PreAuthManagerBean!com.ttk.business.preauth.PreAuthManager");
			}// end if
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, strClaimDetail);
		}// end of catch
		return preAuthManager;
	}// end getPreAuthManagerObject()

	
	 
	/**
	 * Returns the ClaimManager session object for invoking methods on it.
	 * 
	 * @return ClaimManager session object which can be used for method
	 *         invokation
	 * @exception throws TTKException
	 */
	private ClaimManager getClaimManagerObject() throws TTKException {
		ClaimManager claimManager = null;
		try {
			if (claimManager == null) {
				InitialContext ctx = new InitialContext();
				claimManager = (ClaimManager) ctx
						.lookup("java:global/TTKServices/business.ejb3/ClaimManagerBean!com.ttk.business.claims.ClaimManager");
			}// end if
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, strClaimDetail);
		}// end of catch
		return claimManager;
	}// end getClaimManagerObject()

	 /**
	 * Returns the CommunicationManager session object for invoking methods on
	 * it.
	 * 
	 * @return CommunicationManager session object which can be used for method
	 *         invocation
		 * @exception throws TTKException
		 */
	private CommunicationManager getCommunicationManagerObject()
			throws TTKException {
			CommunicationManager communicationManager = null;
		try {
			if (communicationManager == null) {
					InitialContext ctx = new InitialContext();
				communicationManager = (CommunicationManager) ctx
						.lookup("java:global/TTKServices/business.ejb3/CommunicationManagerBean!com.ttk.business.common.messageservices.CommunicationManager");
			}// end if
		}// end of try
		catch (Exception exp) {
				throw new TTKException(exp, strAuthorizationError);
		}// end of catch
			return communicationManager;
	}// end of getCommunicationManagerObject()

	/**
	 * This method for document viewer information
	 * 
	 * @param request
	 *            HttpServletRequest object which contains Pre-Authorization
	 *            information.
	 * @param preAuthDetailVO
	 *            PreAuthDetailVO object which contains Pre-Authorization
	 *            information.
	 * @exception throws TTKException
	 */
	private void documentViewer(HttpServletRequest request,
			PreAuthDetailVO preAuthDetailVO) throws TTKException {
		ArrayList<String> alDocviewParams = new ArrayList<String>();
		alDocviewParams.add("leftlink=" + TTKCommon.getActiveLink(request));
		if (TTKCommon.getActiveLink(request).equalsIgnoreCase(
				strPre_Authorization)) {
			alDocviewParams.add("pre_auth_number="
					+ TTKCommon.getWebBoardDesc(request));
			alDocviewParams.add("dms_reference_number="
					+ TTKCommon.checkNull(preAuthDetailVO.getDMSRefID()));
		}// end of
			// if(TTKCommon.getActiveLink(request).equalsIgnoreCase(strPre_Authorization))
		else if (TTKCommon.getActiveLink(request).equalsIgnoreCase(strClaims)) {
			// alDocviewParams.add("claim_number="+TTKCommon.getWebBoardDesc(request));
			alDocviewParams
					.add("claimno=" + TTKCommon.getWebBoardDesc(request));
			alDocviewParams.add("dms_reference_number="
					+ preAuthDetailVO.getDMSRefID());
			// added for KOC-1267
			alDocviewParams.add("userid=" + preAuthDetailVO.getUser());
			
			alDocviewParams.add("roleid=INTERNAL");
			alDocviewParams.add("claimSeqid="
					+ preAuthDetailVO.getClaimSeqID());
		}// end of else

		if (request.getSession().getAttribute("toolbar") != null) {
			((Toolbar) request.getSession().getAttribute("toolbar"))
					.setDocViewParams(alDocviewParams);
		}// end of if(request.getSession().getAttribute("toolbar")!=null)
	}// end of documentViewer(HttpServletRequest request,PreAuthDetailVO
		// preAuthDetailVO)

	/**
	 * This method prepares the Weboard id for the selected Policy
	 * 
	 * @param preAuthDetailVO
	 *            preAuthVO for which webboard id to be prepared
	 * @param String
	 *            strIdentifier whether it is preauth or enhanced preauth
	 * @param String
	 *            strModule identifier Pre-Authorization/Claims
	 * @return Web board id for the passedVO
	 */
	private String prepareWebBoardId(PreAuthVO preAuthVO, String strIdentifier,
			String strModule) throws TTKException {
		StringBuffer sbfCacheId = new StringBuffer();
		if (strModule.equals(strPre_Authorization)) {
			sbfCacheId.append(preAuthVO.getPreAuthSeqID() != null ? String
					.valueOf(preAuthVO.getPreAuthSeqID()) : " ");
			sbfCacheId.append("~#~")
					.append(TTKCommon.checkNull(preAuthVO.getEnrollmentID())
							.equals("") ? " " : preAuthVO.getEnrollmentID());
			sbfCacheId.append("~#~").append(
					preAuthVO.getEnrollDtlSeqID() != null ? String
							.valueOf(preAuthVO.getEnrollDtlSeqID()) : " ");
			sbfCacheId.append("~#~").append(
					preAuthVO.getPolicySeqID() != null ? String
							.valueOf(preAuthVO.getPolicySeqID()) : " ");
			sbfCacheId.append("~#~").append(
					preAuthVO.getMemberSeqID() != null ? String
							.valueOf(preAuthVO.getMemberSeqID()) : " ");
			sbfCacheId.append("~#~").append(strIdentifier);
			sbfCacheId.append("~#~")
					.append(TTKCommon.checkNull(preAuthVO.getClaimantName())
							.equals("") ? " " : preAuthVO.getClaimantName());
			sbfCacheId.append("~#~").append(
					TTKCommon.checkNull(preAuthVO.getBufferAllowedYN()).equals(
							"") ? " " : preAuthVO.getBufferAllowedYN());
			sbfCacheId.append("~#~")
					.append(TTKCommon.checkNull(preAuthVO.getShowBandYN())
							.equals("") ? " " : preAuthVO.getShowBandYN());
			sbfCacheId
					.append("~#~")
					.append(TTKCommon
							.checkNull(preAuthVO.getCoding_review_yn()).equals(
									"") ? " " : preAuthVO.getCoding_review_yn());
		}// end of if(strModule.equals(strPre_Authorization))
		else if (strModule.equals(strClaims)) {
			sbfCacheId.append(preAuthVO.getClaimSeqID() != null ? String
					.valueOf(preAuthVO.getClaimSeqID()) : " ");
			sbfCacheId.append("~#~")
					.append(TTKCommon.checkNull(preAuthVO.getEnrollmentID())
							.equals("") ? " " : preAuthVO.getEnrollmentID());
			sbfCacheId.append("~#~").append(
					preAuthVO.getEnrollDtlSeqID() != null ? String
							.valueOf(preAuthVO.getEnrollDtlSeqID()) : " ");
			sbfCacheId.append("~#~").append(
					preAuthVO.getPolicySeqID() != null ? String
							.valueOf(preAuthVO.getPolicySeqID()) : " ");
			sbfCacheId.append("~#~").append(
					preAuthVO.getMemberSeqID() != null ? String
							.valueOf(preAuthVO.getMemberSeqID()) : " ");
			sbfCacheId.append("~#~")
					.append(TTKCommon.checkNull(preAuthVO.getClaimantName())
							.equals("") ? " " : preAuthVO.getClaimantName());
			sbfCacheId.append("~#~").append(
					TTKCommon.checkNull(preAuthVO.getBufferAllowedYN()).equals(
							"") ? " " : preAuthVO.getBufferAllowedYN());
			sbfCacheId.append("~#~").append(
					preAuthVO.getClmEnrollDtlSeqID() != null ? String
							.valueOf(preAuthVO.getClmEnrollDtlSeqID()) : " ");
			sbfCacheId.append("~#~").append(
					preAuthVO.getAmmendmentYN() != null ? String
							.valueOf(preAuthVO.getAmmendmentYN()) : " ");
			sbfCacheId
					.append("~#~")
					.append(TTKCommon
							.checkNull(preAuthVO.getCoding_review_yn()).equals(
									"") ? " " : preAuthVO.getCoding_review_yn());
		}// end of else
		return sbfCacheId.toString();
	}// end of prepareWebBoardId(PreAuthVO preAuthVO,String strIdentifier)throws
		// TTKException

	/**
	 * Adds the selected item to the web board and makes it as the selected item
	 * in the web board
	 * 
	 * @param preauthVO
	 *            object which contains the information of the preauth * @param
	 *            String strIdentifier whether it is preauth or enhanced preauth
	 * @param request
	 *            HttpServletRequest
	 * @throws TTKException
	 *             if any runtime exception occures
	 */
	private void addToWebBoard(PreAuthVO preAuthVO, HttpServletRequest request,
			String strIdentifier) throws TTKException {
		Toolbar toolbar = (Toolbar) request.getSession()
				.getAttribute("toolbar");
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
		CacheObject cacheObject = new CacheObject();
		cacheObject.setCacheId(this.prepareWebBoardId(preAuthVO, strIdentifier,
				TTKCommon.getActiveLink(request))); // set the cacheID
		cacheObject.setCacheDesc(preAuthVO.getInvoiceNo());

		alCacheObject.add(cacheObject);
		// if the object(s) are added to the web board, set the current web
		// board id
		toolbar.getWebBoard().addToWebBoardList(alCacheObject);
		toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());

		// webboardinvoked attribute will be set as true in request scope
		// to avoid the replacement of web board id with old value if it is
		// called twice in same request scope
		request.setAttribute("webboardinvoked", "true");
	}// end of addToWebBoard(PreAuthVO preAuthVO, HttpServletRequest
		// request,String strIdentifier)throws TTKException

	
	
	private DynaActionForm setFormValues(PreAuthDetailVO preAuthDetailVO,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
        try
        {
            DynaActionForm frmClaimGeneral = (DynaActionForm)FormUtils.setFormValues("frmClaimGeneral",
            		preAuthDetailVO,this,mapping,request);
            if(preAuthDetailVO.getDentalOrthoVO()!=null)
            {
            	frmClaimGeneral.set("dentalOrthoVO", (DynaActionForm)FormUtils.setFormValues("frmPreAuthDental",
                		preAuthDetailVO.getDentalOrthoVO(),this,mapping,request));
            }//end of if(preAuthDetailVO.getDentalOrthoVO()!=null)
            else
            {
            	frmClaimGeneral.set("dentalOrthoVO", (DynaActionForm)FormUtils.setFormValues("frmPreAuthDental",
                		new DentalOrthoVO(),this,mapping,request));
            }//end of else
            return frmClaimGeneral;
        }
        catch(Exception exp)
        {
            throw new TTKException(exp,strClaimdetails);
        }//end of catch
    }//end of setFormValues(PreAuthDetailVO preAuthDetailVO,ActionMapping mapping,HttpServletRequest request)

	
	
		/**
	 * Methods checks whether user is having permession for the next Event.
	 * 
	 * @param lngEventSeqId
	 *            Long Event_Seq_Id.
	 * @param strSwitchType
	 *            String SwitchType.
	 * @param request
	 *            HttpServletRequest object.
	 * @return blnPermession boolean.
	 */
	private boolean checkReviewPermession(Long lngEventSeqId,
			HttpServletRequest request, String strActiveTab) {
		boolean blnPermession = false;
		WorkflowVO workFlowVO = null;
		ArrayList alEventId = null;
		// get the HashMap from UserSecurityProfile
		HashMap hmWorkFlow = ((UserSecurityProfile) request.getSession()
				.getAttribute("UserSecurityProfile")).getWorkFlowMap();
		if (strActiveTab.equals(strPre_Authorization)) {
			if (hmWorkFlow != null && hmWorkFlow.containsKey(new Long(3)))
				workFlowVO = (WorkflowVO) hmWorkFlow.get(new Long(3));// to get
																		// the
																		// work
																		// flow
																		// of
																		// pre-auth
		}// end of if(strActiveTab.equals(strPre_Authorization))
		if (strActiveTab.equals(strClaims)) {
			if (hmWorkFlow != null && hmWorkFlow.containsKey(new Long(4)))
				workFlowVO = (WorkflowVO) hmWorkFlow.get(new Long(4));// to get
																		// the
																		// work
																		// flow
																		// of
																		// claims
		}// end of if(strActiveTab.equals(strClaims))
			// get the arrayList which is having event information of the
			// particular user.
		if (workFlowVO != null) {
			alEventId = workFlowVO.getEventVO();
		}// end of if(workFlowVO!=null)
			// compare the current policy EventSeqId with the User permession.
		if (alEventId != null) {
			for (int i = 0; i < alEventId.size(); i++) {
				if (lngEventSeqId == ((EventVO) alEventId.get(i))
						.getEventSeqID()) {
					blnPermession = true;
					break;
				}// end of
					// if(lngEventSeqId==((EventVO)alEventId.get(i)).getEventSeqID())
			}// end of for(iRactnt i=0;i<alEventId.size();i++)
		}// end of if(alEventId!=null)
		return blnPermession;
	}// end of checkReviewPermession(Long lngEventSeqId,HttpServletRequest
		// request,String strActiveTab)
	
	
	
	public ActionForward  doViewUploadDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException {
	    
		 ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
	   
	  try{   
		
			
			String strFile	=	request.getParameter("filePath");
			String strFileName	=	"fileName.jpeg";
	    	String strFileNoRecords = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/noRecordsFound.pdf";
	    	String strFileerror = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/fileImproper.pdf";


			  PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			  String claimSeqID = request.getParameter("claimSeqID");	
			  ArrayList arraylist = new ArrayList();
			 
			  arraylist	=	preAuthObject.getClaimDocDBFile(claimSeqID);
			  InputStream  iStream= (InputStream) arraylist.get(0);
			  String fileType = (String) arraylist.get(1);
			
	          String imageDataString =  (String) arraylist.get(2);
			  
			  if((fileType!=null) && (!fileType.equals("")))
			  {
	           if(fileType.equalsIgnoreCase("pdf"))
	           {
	        	 bis = new BufferedInputStream(iStream);
		         baos=new ByteArrayOutputStream();
	           response.setContentType("application/pdf");
				response.addHeader("Content-Disposition","attachment; filename=OnlineClaimDocs.pdf");
	           int ch;
               while ((ch = bis.read()) != -1) baos.write(ch);
               sos = response.getOutputStream();
               baos.writeTo(sos);  
               baos.flush();      
               sos.flush(); 
	           
	           
	           }else if ((fileType.equalsIgnoreCase("jpeg")|| (fileType.equalsIgnoreCase("jpg"))|| (fileType.equalsIgnoreCase("gif")) ||(fileType.equalsIgnoreCase("png")))){
	      
	        	  // byte[] imageByteArray = Base64.decodeBase64(imageDataString);
	        	   byte[] imageByteArray = DatatypeConverter.parseBase64Binary(imageDataString);
	        	   // Write a image byte array into file system
	            /*   FileOutputStream imageOutFile = new FileOutputStream(
	                       "C:/Users/shruthi/Desktop/upload/Untitled4.jpg");
	                imageOutFile.write(imageByteArray);
		            imageOutFile.close(); */
		           response.setContentType("image/jpeg");
	               sos = response.getOutputStream();
	               sos.write(imageByteArray);
	               sos.flush(); 
	                         
	               
	           }else{
			            File f = new File(strFileerror);
		   	    		if(f.isFile() && f.exists()){
		   	    			fis = new FileInputStream(f);
		   	    		}//end of if(strFile !="")
		   	    		BufferedInputStream bist = new BufferedInputStream(fis);
		   	    		baos=new ByteArrayOutputStream();
		   	    		response.setContentType("application/pdf");
						response.addHeader("Content-Disposition","attachment; filename=OnlineClaimDocs.pdf");
		   	    		int ch;
		   	    		while ((ch = bist.read()) != -1)
		   	    		{
		   	    			baos.write(ch);
		   	    		}//end of while ((ch = bis.read()) != -1)
		   	    		sos = response.getOutputStream();
		   	    		baos.writeTo(sos);
	               
	        	    }
			  }else{
				
  				File f = new File(strFileNoRecords);
  	    		if(f.isFile() && f.exists()){
  	    			fis = new FileInputStream(f);
  	    		}//end of if(strFile !="")
  	    		BufferedInputStream bist = new BufferedInputStream(fis);
  	    		baos=new ByteArrayOutputStream();
  	    		response.setContentType("application/pdf");
				response.addHeader("Content-Disposition","attachment; filename=OnlineClaimDocs.pdf");
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
		            		return this.processExceptions(request, mapping, new TTKException(exp,strClaimDetail));
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
	
	public ActionForward dosaveFraud(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws TTKException{
		
		try{
			TableData tableData = TTKCommon.getTableData(request);
		DynaActionForm frmFraudCase = (DynaActionForm)form;
		PreAuthDetailVO preAuthDetailVO=null;
		String userId  = String.valueOf(((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getUSER_ID());
		tableData = new TableData();
		
		String clmSeqId = request.getParameter("clmSeqId");
		
	
		String internalRemarkStatus = frmFraudCase.getString("internalRemarkStatus");
		String remarksforinternalstatus = frmFraudCase.getString("remarksforinternalstatus");
		String riskLevel = frmFraudCase.getString("riskLevel");
		String suspectVerified = request.getParameter("claimVerifiedforClaimIdValue");
		ArrayList<String> frdInputData = new ArrayList<>();
		frdInputData.add(clmSeqId);
		frdInputData.add(internalRemarkStatus);
		frdInputData.add(userId);
		frdInputData.add(remarksforinternalstatus);
		frdInputData.add(suspectVerified);
		frdInputData.add(riskLevel);
		frdInputData.add("CLM");
		
		
		PreAuthManager preAuthManagerObject= this.getPreAuthManagerObject();
		
		long  frdOutputScreen = preAuthManagerObject.saveFraudData(frdInputData);
		
		if(frdOutputScreen > 0){
		
			request.setAttribute("successMsg","Invesitigation Details Updated Successfully");
			
		}
		
		ArrayList listtoGetInternalRemarksDetails = new ArrayList<>();
		listtoGetInternalRemarksDetails.add("CLM");
		listtoGetInternalRemarksDetails.add(clmSeqId);
		tableData.createTableInfo("InternalRemarksStatusTable", new ArrayList());
		
		Object[] suspectData = preAuthManagerObject.getFraudDetails(listtoGetInternalRemarksDetails);
		ArrayList internalRemarksDetailsList =(ArrayList) suspectData[0];
		ArrayList investigationDetailsList =(ArrayList) suspectData[1];
		
		if(!internalRemarksDetailsList.isEmpty()){
			preAuthDetailVO = (PreAuthDetailVO) internalRemarksDetailsList.get(0);
			frmFraudCase.set("internalRemarkStatus", preAuthDetailVO.getInternalRemarkStatus());
			frmFraudCase.set("riskLevel", preAuthDetailVO.getRiskLevel());
			frmFraudCase.set("currecntIntrRemarkStatus", preAuthDetailVO.getCurrecntIntrRemarkStatus());
			frmFraudCase.set("suspectVerified", preAuthDetailVO.getSuspectVerified());
			request.getSession().setAttribute("internalRemarkStatus", preAuthDetailVO.getInternalRemarkStatus());
			if(preAuthDetailVO.getInternalRemarkStatus().equals("SUSP"))
				request.getSession().setAttribute("SUSPECTFLAG", "DISABLED");
		}
		
		tableData.setData(internalRemarksDetailsList, "search");
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		request.setAttribute("WEBBORDHIDEFLAG", "Y");
		return this.getForward(strFraudInternalRemarks, mapping, request);
		}catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "InternalRemarks"));
		}// end of catch(Exception exp)
		
	}

	public ActionForward viewBenefitDetails(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction viewBenefitDetails ");
			HttpSession session = request.getSession();
			String benefitType = (String)request.getParameter("benefitType");
			DynaActionForm frmClaimGeneral = (DynaActionForm) session.getAttribute("frmClaimGeneral");
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
		//	PreAuthDetailVO preAuthDetailVO = null;
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
			
			//PreAuthVO preAuthVO=new PreAuthVO;
			//clear the dynaform if visting from left links for the first time
			//else get the dynaform data from session
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}// end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return mapping.findForward("");
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
				tableData.createTableInfo("DisplayBenefitsTable",null);
				
				ArrayList<Object> allinfo=new ArrayList<Object>();
				allinfo.add(frmClaimGeneral.getString("policySeqId"));
				allinfo.add(frmClaimGeneral.getString("memberSeqID"));
				allinfo.add("C");//claims module
				allinfo.add(frmClaimGeneral.getString("claimSeqID"));
				allinfo.add(benefitType);
				tableData.setSearchData(allinfo);
				tableData.modifySearchData("search");
			}//end of else
			
		
		ArrayList<Object> alBenefitList= preAuthObject.getBenefitDetails(tableData.getSearchData());
		tableData.setData((ArrayList<Object>)alBenefitList.get(0),"search");
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		
		//preAuthDetailVO = (PreAuthDetailVO) alBenefitList.get(1);
		request.setAttribute("availableSumInsured",alBenefitList.get(1));	
		request.setAttribute("utilizeSuminsured",alBenefitList.get(2));	
		if("OPTS".equals(benefitType))
			benefitType="OPT";
		if("DNTL".equals(benefitType))
			benefitType="DNT";
		if("IMTI".equals(benefitType)||"OMTI".equals(benefitType))
			benefitType="MAT";
		if("OPTC".equals(benefitType))
			benefitType="OPL";
		if("OPTS".equals(benefitType))
		benefitType="OPT";
		request.setAttribute("benefitType",benefitType);
		//frmClaimGeneral = (DynaActionForm) FormUtils.setFormValues("frmClaimGeneral", preAuthDetailVO, this, mapping, request);
		//session.setAttribute("frmClaimGeneral", frmClaimGeneral);
		//request.setAttribute("otherRemarks",alBenefitList.get(2));
		return mapping.findForward(strDisplayOfBenefits);

		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}// end of catch(Exception exp)
	}// end of AddActivityDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward viewFraudData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws TTKException{
		
		try{
		
		DynaActionForm frmClaimGeneral = (DynaActionForm)form;
		
		String clmSeqId = request.getParameter("clmSeqId");
		
		
		ClaimManager claimManagerObject= this.getClaimManagerObject();
		
		ArrayList<String> frdOutputScreen = claimManagerObject.fetchFraudData(clmSeqId);
		
		if(frdOutputScreen.size()>0){
		
		
		
		frmClaimGeneral.set("internalRemarkStatus", frdOutputScreen.get(0));
		frmClaimGeneral.set("suspectedRemarks", frdOutputScreen.get(1));
		frmClaimGeneral.set("userid", frdOutputScreen.get(2));
		frmClaimGeneral.set("completedYN", frdOutputScreen.get(4));
		frmClaimGeneral.set("claimVerifiedforClaim", frdOutputScreen.get(5));
		frmClaimGeneral.set("suspectedUpdatededDate", frdOutputScreen.get(3));
		
		}
		/*request.getSession().setAttribute("fraudstatus", frdOutputScreen.get(2));*/
		/*return mapping.findForward("InternalRemarks");*/
		request.getSession().setAttribute("claimVerifiedforClaim", frdOutputScreen.get(5));
	   return mapping.findForward("InternalRemarks"); /*mapping.findForward("claimdetails")*/
		}catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "InternalRemarks"));
		}// end of catch(Exception exp)		
		}
	
	
	public ActionForward doGenerateDownloadHistoryPATCLM(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		try
		{
			setLinks(request);
			log.debug("Inside ClaimGeneralAction doGenerateDownloadHistoryPATCLM");
			HttpSession session = request.getSession();
			DynaActionForm frmHistoryList = (DynaActionForm) form;
			DynaActionForm frmClaimGeneral = (DynaActionForm) session .getAttribute("frmClaimGeneral");
			String strhistoryMode = request.getParameter("historyMode");
			String strMemberSeqId = request.getParameter("memberSeqID");
			String memberId	=	frmHistoryList.getString("memberId");
			String benefitType	=	frmHistoryList.getString("benefitType");
			strMemberSeqId = (strMemberSeqId == null || strMemberSeqId.length() < 1) ? "0"
					: strMemberSeqId;
			
			String strPolicySeqId="";
			if (frmHistoryList.getString("policyDet").equals(""))
			{
				strPolicySeqId = frmHistoryList.getString("policySeqId");
			 	strPolicySeqId = (strPolicySeqId == null || strPolicySeqId.length() < 1) ? "0" : strPolicySeqId;
			}
			else
			{
				strPolicySeqId = frmHistoryList.getString("policyDet"); 
				strPolicySeqId = (strPolicySeqId == null || strPolicySeqId.length() < 1) ? "0" : strPolicySeqId;
			}
			
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			if("PAT".equals(strhistoryMode))
			{
				 ArrayList<ReportVO> alData	=	null; 
				 if( !"0".equals(strMemberSeqId))
				 {
					 Long lngMemberSeqId = Long.parseLong(strMemberSeqId);
					 Long policySeqId = Long.parseLong(strPolicySeqId);
				     alData	=	preAuthObject.getDownloadHistoryPATCLM(strhistoryMode,lngMemberSeqId,memberId,policySeqId,benefitType);
				     request.setAttribute("alData", alData);
					 request.setAttribute("historyMode", strhistoryMode);
					 request.getSession().setAttribute("flag", "generatereport");
					 return mapping.findForward("preauthclaimhistorydownload");
				 }
				 else
				 {
					    TTKException expTTK = new TTKException();
						expTTK.setMessage("error.preauthnumber.required");
						throw expTTK;
					 
				 }
			}	
			else if("CLM".equals(strhistoryMode))
			{
				ArrayList<ReportVO> alData	=	null; 
				if(!"".equals(strMemberSeqId) || !"0".equals(strMemberSeqId))
				 {
					 Long lngMemberSeqId = Long.parseLong(strMemberSeqId);
					 Long policySeqId = Long.parseLong(strPolicySeqId);
				     alData	=	preAuthObject.getDownloadHistoryPATCLM(strhistoryMode,lngMemberSeqId,memberId,policySeqId,benefitType);
				     request.setAttribute("alData", alData);
					 request.setAttribute("historyMode", strhistoryMode);
					 request.getSession().setAttribute("flag", "generatereport");
					 return mapping.findForward("preauthclaimhistorydownload");
				 }
				 else
				 {
					   TTKException expTTK = new TTKException();
						expTTK.setMessage("error.claimnumber.required");
						throw expTTK;
					 
				 }
				
			}
			
			return mapping.findForward(strCLaimHistoryList);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strCLaimHistoryList));
		}// end of catch(Exception exp)
	}
	
	public ActionForward doDownloadHistoryPATCLM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction doDownloadHistoryPATCLM");
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			DynaActionForm frmHistoryList = (DynaActionForm) form;
			
		    Document historyDoc = null;
		
		    String strhistorymode = frmHistoryList.getString("historyMode");
			
			 ArrayList<ReportVO> alData	=	null; 
			if("CLM".equals(strhistorymode))
			{
				//String strclmseqId = request.getParameter("clamseqID");
				String strclmseqId = frmHistoryList.getString("clmseqid");
				strclmseqId = (strclmseqId == null || strclmseqId.length() < 1) ? "0"
						: strclmseqId;
				historyDoc = preAuthObject
						.getPreAuthHistory(new Long(strclmseqId));
				request.setAttribute("claimHistoryDoc", historyDoc);
				alData	=	preAuthObject.getDownloadHistoryDetailsPATCLM(strhistorymode,new Long(strclmseqId));
				
				 request.setAttribute("alData", alData);
				 request.setAttribute("historyMode", strhistorymode);
				 request.getSession().setAttribute("flag", "generatereporthistorydetails");
				 return mapping.findForward("preauthclaimhistorydownload");
			}
			else if("PAT".equals(strhistorymode))
			{
				String strpreauthseqId = frmHistoryList.getString("preauthseqid");
				strpreauthseqId = (strpreauthseqId == null || strpreauthseqId.length() < 1) ? "0"
						: strpreauthseqId;
				historyDoc = preAuthObject
						.getPreAuthHistory(new Long(strpreauthseqId));
				request.setAttribute("preAuthHistoryDoc", historyDoc);
				alData	=	preAuthObject.getDownloadHistoryDetailsPATCLM(strhistorymode,new Long(strpreauthseqId));
				
				request.setAttribute("alData", alData);
				 request.setAttribute("historyMode", strhistorymode);
				 request.getSession().setAttribute("flag", "generatereporthistorydetails");
				 return mapping.findForward("preauthclaimhistorydownload");
			}
			
			
			return mapping.findForward("strClaimViewHistory");// it's
																			// not
																			// reach
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimViewHistory));
		}// end of catch(Exception exp)
	}// end of doViewHistory(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)	
   
	public ActionForward markClaimAsSuspect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try {
			log.debug("Inside the doDefault method of ClaimsAction");
			setLinks(request);
			request.getSession().removeAttribute("INVESTTABLEFLAG");
			request.getSession().removeAttribute("SUSPECTFLAG");
			request.getSession().removeAttribute("tableDataForInvesigation");
			String claimSeqID = (String)request.getParameter("claimSeqID");
			// get the tbale data from session if exists
			TableData tableData = TTKCommon.getTableData(request);
			// clear the dynaform if visiting from left links for the first time
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) {
				((DynaActionForm) form).initialize(mapping);// reset the form
															// data
			}// end of
				// if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			// create new table data object
			tableData = new TableData();
			// create the required grid table
			tableData.createTableInfo("InternalRemarksStatusTable", new ArrayList());
			request.getSession().setAttribute("tableData", tableData);
			request.getSession().setAttribute("internalRemarkStatus",null);
			request.getSession().removeAttribute("SUSPECTFLAG");
			request.getSession().setAttribute("claimSeqID", claimSeqID);
			request.setAttribute("WEBBORDHIDEFLAG", "Y");
			((DynaActionForm) form).initialize(mapping);// reset the form data
			
			return this.getForward(strFraudInternalRemarks, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimViewHistory));
		}// end of catch(Exception exp)
	}// end of doViewHistory(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)	
	
	public ActionForward doStatusChangeForRemarks(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doBackward method of doStatusChange");
			DynaActionForm frmFraudCase = (DynaActionForm) form;
			
			request.getSession().setAttribute("internalRemarkStatus", frmFraudCase.get("internalRemarkStatus"));
			frmFraudCase.set("internalRemarkStatus", frmFraudCase.get("internalRemarkStatus"));
			request.getSession().setAttribute("frmFraudCase", frmFraudCase);
			return this.getForward(strFraudInternalRemarks, mapping, request); // finally
																		// return
																		// to
																		// the
																		// grid
																		// screen
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strFraudInternalRemarks));
		}// end of catch(Exception exp)
	}// end of doBackward(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	


	
	public ActionForward addOverrideRemarks(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside PreAuthGenealAction addOverrideRemarks()");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
		//	String overrideRemarks = request.getParameter("overrideRemarks");
			String overrideSubRemarks = request.getParameter("overrideSubRemarks");
			overrideSubRemarks = (overrideSubRemarks == null) ? "" : overrideSubRemarks;		//	id overrideSubRemarks 
			String overrideSubRemarksDesc = request.getParameter("overrideSubRemarksDesc");		// overrideSubRemarksDesc description.
			overrideSubRemarksDesc = (overrideSubRemarksDesc == null) ? "" : overrideSubRemarksDesc;
			HttpSession session = request.getSession();
			LinkedHashMap<String,String> overRemarksList = (LinkedHashMap<String,String>) session.getAttribute("overRemarksList");
			
			if (overRemarksList == null) {
				overRemarksList = new LinkedHashMap<String, String>();
			}
			overRemarksList.put(overrideSubRemarks, overrideSubRemarksDesc);
			boolean blnExists = overRemarksList.containsKey("16");
			if(blnExists)
				frmActivityDetails.set("otherRemarksYN", "Y");
			else
				frmActivityDetails.set("otherRemarksYN", "N");
		//	frmActivityDetails.set("overrideRemarks", "");
			frmActivityDetails.set("overrideRemarksDesc", "");
			session.setAttribute("frmActivityDetails", frmActivityDetails);
			session.setAttribute("overRemarksList", overRemarksList);
		//	session.setAttribute("overrideRemarks", overrideRemarks);
			session.setAttribute("overrideSubRemarks", overrideSubRemarks);
			request.setAttribute("successMsg", "Added Successfully.");
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivitydetails));
		}
	}
	
	
	public ActionForward deleteOverrideRemarks(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside PreAuthGenealAction deleteOverrideRemarks");
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
		//	String overrideRemarks = request.getParameter("overrideRemarks");
			String keyVal = request.getParameter("keyVal");
			
			HttpSession session = request.getSession();
			LinkedHashMap<String, String> overRemarksList = (LinkedHashMap<String, String>) session.getAttribute("overRemarksList");
			if (overRemarksList != null) {
				overRemarksList.remove(keyVal);
				session.setAttribute("overRemarksList", overRemarksList);
			}
			request.setAttribute("successMsg", "Deleted Successfully.");
			if(overRemarksList == null || overRemarksList.isEmpty())	
			{
				frmActivityDetails.set("overrideRemarks", "");
				frmActivityDetails.set("overrideSubRemarks", "");
				
			}
			/*else
			{
				frmActivityDetails.set("overrideRemarks","ACT1");
				frmActivityDetails.set("overrideSubRemarks","AC1");
				
			}*/
			boolean blnExists = overRemarksList.containsKey("16");
			if(blnExists)
				frmActivityDetails.set("otherRemarksYN", "Y");
			else
				frmActivityDetails.set("otherRemarksYN", "N");
		//	frmActivityDetails.set("overrideRemarksDesc", "");
			frmActivityDetails.set("overrideSubRemarksDesc", "");
			
			session.setAttribute("frmActivityDetails", frmActivityDetails);
			return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strActivitydetails));
		}
	}
public ActionForward dogetFraud(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws TTKException {

		try {
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			TableData tableDataForInvesigation = TTKCommon.getTableData(request);
			DynaActionForm frmFraudCase = (DynaActionForm) form;
			frmFraudCase.initialize(mapping);
			tableData = new TableData();
			tableDataForInvesigation = new TableData();
			PreAuthDetailVO preAuthDetailVO=null;
			String claimSeqID = request.getParameter("claimSeqID");

			PreAuthManager preAuthManagerObject = this
					.getPreAuthManagerObject();

			ArrayList listtoGetInternalRemarksDetails = new ArrayList<>();
			listtoGetInternalRemarksDetails.add("CLM");
			listtoGetInternalRemarksDetails.add(claimSeqID);
			tableData.createTableInfo("InternalRemarksStatusTable",new ArrayList());
			tableDataForInvesigation.createTableInfo("InternalRemarksInvestigationTable", new ArrayList<>());
			Object[] suspectData = preAuthManagerObject
					.getFraudDetails(listtoGetInternalRemarksDetails);
			ArrayList internalRemarksDetailsList =(ArrayList) suspectData[0];
			if(!internalRemarksDetailsList.isEmpty()){
			preAuthDetailVO = (PreAuthDetailVO) internalRemarksDetailsList.get(0);
			frmFraudCase.set("internalRemarkStatus", preAuthDetailVO.getInternalRemarkStatus());
			frmFraudCase.set("riskLevel", preAuthDetailVO.getRiskLevel());
			frmFraudCase.set("currecntIntrRemarkStatus", preAuthDetailVO.getCurrecntIntrRemarkStatus());
			frmFraudCase.set("investigationOutcomeCatgDesc", preAuthDetailVO.getInvestigationOutcomeCatgDesc());
			frmFraudCase.set("investigationStatusDesc", preAuthDetailVO.getInvestigationStatusDesc());
			frmFraudCase.set("investigationTAT", preAuthDetailVO.getInvestigationTAT());
			request.getSession().setAttribute("internalRemarkStatus", preAuthDetailVO.getInternalRemarkStatus());
			frmFraudCase.set("suspectVerified", preAuthDetailVO.getSuspectVerified());
			if(preAuthDetailVO.getInternalRemarkStatus().equals("SUSP"))
				request.getSession().setAttribute("SUSPECTFLAG", "DISABLED");
			request.getSession().setAttribute("tableDataForInvesigation", tableDataForInvesigation);
			}
			ArrayList investigationDetailsList =(ArrayList) suspectData[1];
			if(!investigationDetailsList.isEmpty()){
				request.getSession().setAttribute("INVESTTABLEFLAG", "display");
			}
			tableData.setData(internalRemarksDetailsList, "search");
			tableDataForInvesigation.setData(investigationDetailsList,"search");
			// set the table data object to session
			request.getSession().setAttribute("tableData", tableData);
			request.getSession().setAttribute("frmFraudCase", frmFraudCase);
			request.setAttribute("WEBBORDHIDEFLAG", "Y");
			return this.getForward(strFraudInternalRemarks, mapping, request);
		} catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "InternalRemarks"));
		}// end of catch(Exception exp)
}
	
	public ActionForward doCloseInternalRemarks(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws TTKException {
		try {
			doView(mapping, form, request, response);
			setLinks(request);
			return mapping.findForward(strClaimDetail);
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}// end of catch(Exception exp)
	}
	public ActionForward doDefaultReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws TTKException {
		try {
			setLinks(request); 
			DynaActionForm frmClaimDetailedReports=(DynaActionForm) form;
			frmClaimDetailedReports.initialize(mapping);
			TableData tableData = new TableData();	
			tableData.createTableInfo("ClaimDetailedReport",new ArrayList());
			request.getSession().setAttribute("claimDetailedReport", tableData);
			request.getSession().setAttribute("frmClaimDetailedReports", frmClaimDetailedReports);
			return mapping.findForward(strDefaultClaimReports);
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimDetail));
		}
	}
	
	public ActionForward doClmDetailedReportSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
				try{
				log.debug("Inside the doSearch method of ClaimsAction");
				setLinks(request);
				boolean isAuthorized=TTKCommon.isAuthorized(request, "Search");
				if(isAuthorized==true){
					DynaActionForm frmClaimDetailedReports=(DynaActionForm) form;
					ClaimManager claimObject = this.getClaimManagerObject();
					ArrayList alReportList = null;
					//get the tbale data from session if exists
					TableData tableData =(request.getSession().getAttribute("claimDetailedReport")!=null?(TableData)request.getSession().getAttribute("claimDetailedReport"):new TableData());
					if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
					{
					((DynaActionForm)form).initialize(mapping);//reset the form data
					}
					String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
					String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
					
					//if the page number or sort id is clicked
					if(!strPageID.equals("") || !strSortID.equals(""))
					{
					if(!strPageID.equals(""))
					{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return mapping.findForward(strSearchtClaimReports);
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
					tableData.createTableInfo("ClaimDetailedReport",null);
					tableData.setSearchData(this.populateSearchCriteria(frmClaimDetailedReports,request));
					tableData.modifySearchData("search");
					}//end of else
					
					if("QPR".equalsIgnoreCase(frmClaimDetailedReports.getString("switchType")))
					{
						((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(true); 
					}
					else
					{
						((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false); 
					}
					
					alReportList= claimObject.getClaimDetailedReportData(tableData.getSearchData());	 
					tableData.setData(alReportList, "search");
/*					((Column)((ArrayList)tableData.getTitle()).get()).setVisibility(false);*/
					request.getSession().setAttribute("claimDetailedReport",tableData);
					request.getSession().setAttribute("frmClaimDetailedReports",frmClaimDetailedReports);
					return mapping.findForward(strSearchtClaimReports);
				}else{
					return this.getForward(strSearchtClaimReports, mapping, request);
				}
				}//end of try
				catch(TTKException expTTK)
				{
				return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
				return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
				}//end of catch(Exception exp)
				}
	
	public ActionForward  doClmDetailedReportBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
			try{
			log.debug("Inside the doBackward method of ClaimsAction");
			setLinks(request);
			//get the tbale data from session if exists+
			ClaimManager claimObject = this.getClaimManagerObject();
			TableData tableData =(request.getSession().getAttribute("claimDetailedReport")!=null?(TableData)request.getSession().getAttribute("claimDetailedReport"):new TableData());
			tableData.modifySearchData(strBackward);//modify the search data
			ArrayList alReportList = claimObject.getClaimDetailedReportData(tableData.getSearchData());	 
			tableData.setData(alReportList, strBackward);//set the table data
			request.getSession().setAttribute("claimDetailedReport",tableData);
			return mapping.findForward(strSearchtClaimReports);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
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
			public ActionForward doClmDetailedReportForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
									HttpServletResponse response) throws Exception{
			try{
			log.debug("Inside the doForward method of ClaimsAction");
			setLinks(request);
			ClaimManager claimObject = this.getClaimManagerObject();
			//get the tbale data from session if exists
			TableData tableData =(request.getSession().getAttribute("claimDetailedReport")!=null?(TableData)request.getSession().getAttribute("claimDetailedReport"):new TableData());
			tableData.modifySearchData(strForward);
			ArrayList alReportList = claimObject.getClaimDetailedReportData(tableData.getSearchData());
			tableData.setData(alReportList, strForward);
			request.getSession().setAttribute("claimDetailedReport",tableData);
			return mapping.findForward(strSearchtClaimReports); 
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
			}//end of catch(Exception exp)
			}

			
			public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
					  HttpServletResponse response) throws TTKException{
		  	 try{
			    		log.debug("Inside the doGenerateClimSummaryReport method of HospitalReportsAction");
			    		setLinks(request);
			    		JasperReport mainJasperReport,emptyReport;
			    		TTKReportDataSource mainTtkReportDataSource = null;
			    		JasperPrint mainJasperPrint = null;
			    		String parameter=request.getParameter("parameter");	
			    		DynaActionForm frmClaimDetailedReports=(DynaActionForm) form;
			    		String jrxmlfile="onlinereports/provider/claimDetailedReport.jrxml";
			    		String jrxmlfile2="onlinereports/provider/claimDetailedReportInternational.jrxml";
			    		try
			    		{
			    			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			    			HashMap<String,String> hashMap = new HashMap<String,String>();
			    			ByteArrayOutputStream boas=new ByteArrayOutputStream();		
			    			mainTtkReportDataSource = new TTKReportDataSource("ClaimDetailedReport", parameter);
			    			ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
			    			if("QPR".equalsIgnoreCase(frmClaimDetailedReports.getString("switchType")))
			    			{
			    				mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
			    			}
			    			else
			    			{
			    				mainJasperReport = JasperCompileManager.compileReport(jrxmlfile2);
			    			}
			    				
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
			    			request.setAttribute("reportType","EXCEL");
			    			response.setContentType("application/vnd.ms-excel");
			    			response.addHeader("Content-Disposition","attachment; filename=claimDetailedReport.xls");
			    			return (mapping.findForward(strReportdisplay));
			    		}
			    		catch(Exception exp)
			        	{
			    			return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
			        	}
			    	}
			    	catch(TTKException expTTK)
			    	{
			    		return this.processExceptions(request, mapping, expTTK);
			    	}
			    	catch(Exception exp)
			    	{
			    		return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
			    	}
		    }
			
	
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmClaimsDiscountActReport,HttpServletRequest request)
	{
		ArrayList<Object> alSearchParams =new ArrayList<Object>();	
		alSearchParams.add(frmClaimsDiscountActReport.getString("tmtfromDate"));//1
		alSearchParams.add(frmClaimsDiscountActReport.getString("tmttoDate"));//2
		alSearchParams.add(frmClaimsDiscountActReport.getString("clmFromDate"));//3
		alSearchParams.add(frmClaimsDiscountActReport.getString("clmToDate"));//4
		alSearchParams.add(frmClaimsDiscountActReport.getString("patientName"));//5
		alSearchParams.add(frmClaimsDiscountActReport.getString("claimStatus"));//6
		alSearchParams.add(frmClaimsDiscountActReport.getString("invoiceNo"));//7
		alSearchParams.add(frmClaimsDiscountActReport.getString("batchNo"));//8
		alSearchParams.add(frmClaimsDiscountActReport.getString("alKootId"));//9
		alSearchParams.add(frmClaimsDiscountActReport.getString("claimNo"));//10
		alSearchParams.add(frmClaimsDiscountActReport.getString("benefitType"));//11
		alSearchParams.add(frmClaimsDiscountActReport.getString("eventRefNo"));//12
		alSearchParams.add(frmClaimsDiscountActReport.getString("qatarId"));//13
		alSearchParams.add(frmClaimsDiscountActReport.getString("payRefNo"));//14
		alSearchParams.add(frmClaimsDiscountActReport.getString("sProviderName"));//15
		if("QPR".equals(frmClaimsDiscountActReport.getString("switchType")))
			alSearchParams.add("CDR");//16
		else
			alSearchParams.add("PTR");//16
		alSearchParams.add(frmClaimsDiscountActReport.getString("finBatchNo"));//17
		alSearchParams.add(frmClaimsDiscountActReport.getString("financeStatus"));//18
		alSearchParams.add(frmClaimsDiscountActReport.getString("partnerNm"));//19
		return alSearchParams;
	}
	
	public ActionForward doChangeSwitchType(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception
			{
				try
				{
					log.debug("Inside the doChangeSwitchType method of ClaimGeneralAction");
					setLinks(request);
					DynaActionForm frmClaimDetailedReports=(DynaActionForm) form;
					String switchType = (String)frmClaimDetailedReports.get("switchType");
					frmClaimDetailedReports.initialize(mapping);
					TableData tableData = new TableData();	
					tableData.createTableInfo("ClaimDetailedReport",new ArrayList());
					request.getSession().setAttribute("claimDetailedReport", tableData);
					frmClaimDetailedReports.set("switchType", switchType);
					if("QPR".equals(switchType))
					{
						frmClaimDetailedReports.set("partnerNm", "");
					}
					if("IPTR".equals(switchType))
					{
						frmClaimDetailedReports.set("finBatchNo", "");
						frmClaimDetailedReports.set("financeStatus", "");
					}
					request.getSession().setAttribute("frmClaimDetailedReports", frmClaimDetailedReports);
					return this.getForward(strSearchtClaimReports, mapping, request);
				}
				catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
				}
			}
	
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
	   }
	
	public ActionForward viewClaimFraudHistroy(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction viewClaimFraudHistroy ");
			HttpSession session = request.getSession();
			DynaActionForm frmClaimGeneral = (DynaActionForm) session.getAttribute("frmClaimGeneral");
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			TableData tableData =TTKCommon.getTableData(request);
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}// end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return mapping.findForward("");
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
				tableData.createTableInfo("FraudHistoryTable",null);	
				ArrayList<Object> allinfo=new ArrayList<Object>();
				allinfo.add(frmClaimGeneral.getString("policySeqId"));
				allinfo.add(frmClaimGeneral.getString("memberSeqID"));
				allinfo.add("CLM");//claims module	
				allinfo.add(frmClaimGeneral.getString("claimSeqID"));
				tableData.setSearchData(allinfo);		
			}//end of else
			ArrayList<Object> alFraudHistory= preAuthObject.getFraudHistory(tableData.getSearchData());
			tableData.setData(alFraudHistory);
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		return mapping.findForward(strFraudHistory);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}// end of catch(Exception exp)
	}// end of AddActivityDetails(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward viewPreauthFraudHistroy(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction viewPreauthFraudHistroy ");
			HttpSession session = request.getSession();
			DynaActionForm frmClaimGeneral = (DynaActionForm) session.getAttribute("frmClaimGeneral");
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			TableData tableData =TTKCommon.getTableData(request);
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}// end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return mapping.findForward("");
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
				tableData.createTableInfo("PreauthFraudHistoryTable",null);	
				ArrayList<Object> allinfo=new ArrayList<Object>();
				allinfo.add(frmClaimGeneral.getString("policySeqId"));
				allinfo.add(frmClaimGeneral.getString("memberSeqID"));
				allinfo.add("PAT");//claims module	
				allinfo.add(frmClaimGeneral.getString("claimSeqID"));
				tableData.setSearchData(allinfo);		
			}//end of else
			ArrayList<Object> alFraudHistory= preAuthObject.getFraudHistory(tableData.getSearchData());
			tableData.setData(alFraudHistory);
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		return mapping.findForward(strFraudHistory);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}// end of catch(Exception exp)
	}// end of AddActivityDetails(ActionMapping mapping,ActionForm
	
	public ActionForward displayOverrideRemarksSub(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimGeneralAction displayOverrideRemarksSub()");
			
			DynaActionForm frmActivityDetails = (DynaActionForm) form;
			ClaimManager claimObject = this.getClaimManagerObject();
			ArrayList<Object> subGrpName = null;
			ArrayList<CacheObject> subGrpNameList = null;
			
			 String overrideRemarksId = (String) frmActivityDetails.get("overrideRemarks");
			 
			 subGrpName = claimObject.getSubgroupName(overrideRemarksId);
			 
			 subGrpNameList=(ArrayList<CacheObject>)subGrpName.get(0);
			 frmActivityDetails.set("overrideRemarksSub",subGrpNameList);
			 request.getSession().setAttribute("frmActivityDetails", frmActivityDetails);
			 return this.getForward(strActivitydetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) 
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimdetails));
		}
	} // end of displayOverrideRemarksSub()
	
		public ActionForward doOverrideClaim(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
				throws Exception 
		{
			try 
			{
				log.debug("Inside ClaimGeneralAction doOverrideClaim()");
				return mapping.findForward("claimGeneralOverride");
			}// end of try
			catch (Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(
						exp, strClaimdetails));
			}
		} // end of doOverrideClaim()
	
		public ActionForward getOverrideGenSubRemarks(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			try {	
					log.debug("Inside ClaimGeneralAction getOverrideGenSubRemarks()");
					DynaActionForm frmClaimGeneral=(DynaActionForm)request.getSession().getAttribute("frmClaimGeneral");
					ClaimManager claimObject = this.getClaimManagerObject();
					ArrayList<Object> subGrpName = null;
					ArrayList<CacheObject> subGrpNameList = null;
					
					String overrideGenRemarksId = request.getParameter("overrideGenRemarks"); 	
					
					subGrpName = claimObject.getGenOverrideSubgroupName(overrideGenRemarksId);
					 
					subGrpNameList=(ArrayList<CacheObject>)subGrpName.get(0);
					frmClaimGeneral.set("overrideRemarksSub",subGrpNameList);
					
					request.getSession().setAttribute("frmClaimGeneral", frmClaimGeneral);
					return mapping.findForward("claimGeneralOverride");
				}// end of try
				catch (TTKException expTTK) 
				{
					return this.processExceptions(request, mapping, expTTK);
				}
				catch (Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp, strClaimdetails));
				}
		} // end of getOverrideGenSubRemarks()
		
	public ActionForward doDefaultDiscountActReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws TTKException {
		try {
			setLinks(request); 
			DynaActionForm frmClaimsDiscountActReport=(DynaActionForm) form;
			frmClaimsDiscountActReport.initialize(mapping);
			request.getSession().setAttribute("frmClaimsDiscountActReport", frmClaimsDiscountActReport);
			return mapping.findForward(StrClaimsDiscountActReportList);
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimDetail));
		}
	}
	
	public ActionForward doViewClmDiscountActivityReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws TTKException {
		try {
			setLinks(request); 
			DynaActionForm frmClaimsDiscountActReport=(DynaActionForm) form;
			frmClaimsDiscountActReport.initialize(mapping);
			TableData tableData = new TableData();	
			tableData.createTableInfo("ClaimsDiscountActReportTable",new ArrayList());
			request.getSession().setAttribute("tableData", tableData);
			request.getSession().setAttribute("frmClaimsDiscountActReport", frmClaimsDiscountActReport);
			return mapping.findForward(StrClaimsDiscountActReport);
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimDetail));
		}
	}
	
	public ActionForward doSwitchTypeChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception
			{
				try
				{
					log.debug("Inside the doSwitchTypeChange method of ClaimGeneralAction");
					setLinks(request);
					DynaActionForm frmClaimsDiscountActReport=(DynaActionForm) form;
					String switchType = (String)frmClaimsDiscountActReport.get("switchType");
					frmClaimsDiscountActReport.initialize(mapping);
					TableData tableData = new TableData();	
					tableData.createTableInfo("ClaimsDiscountActReportTable",new ArrayList());
					request.getSession().setAttribute("tableData", tableData);
					frmClaimsDiscountActReport.set("switchType", switchType);
					if("QPR".equals(switchType))
					{
						frmClaimsDiscountActReport.set("partnerNm", "");
					}
					if("IPTR".equals(switchType))
					{
						frmClaimsDiscountActReport.set("finBatchNo", "");
						frmClaimsDiscountActReport.set("financeStatus", "");
					}
					request.getSession().setAttribute("frmClaimsDiscountActReport", frmClaimsDiscountActReport);
					return this.getForward(StrClaimsDiscountActReport, mapping, request);
				}
				catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
				}
			}// endo f doSwitchTypeChange()
	
	
	public ActionForward doClose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws TTKException {
		try {
			setLinks(request); 
			
			DynaActionForm frmClaimsDiscountActReport=(DynaActionForm) form;
			frmClaimsDiscountActReport.initialize(mapping);
			request.getSession().setAttribute("frmClaimsDiscountActReport", frmClaimsDiscountActReport);
			return mapping.findForward(StrClaimsDiscountActReportList);
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimDetail));
		}
	}
	
	public ActionForward doClmDiscountActReportSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
		log.debug("Inside the doClmDiscountActReportSearch method of ClaimsAction");
		setLinks(request);
	/*	boolean isAuthorized=TTKCommon.isAuthorized(request, "Search");
		if(isAuthorized==true){*/
			DynaActionForm frmClaimsDiscountActReport=(DynaActionForm) form;
			ClaimManager claimObject = this.getClaimManagerObject();
			ArrayList alReportList = null;
			//get the tbale data from session if exists
			TableData tableData =(request.getSession().getAttribute("tableData")!=null?(TableData)request.getSession().getAttribute("tableData"):new TableData());
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
			((DynaActionForm)form).initialize(mapping);//reset the form data
			}
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
			if(!strPageID.equals(""))
			{
			tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			return mapping.findForward(StrClaimsDiscountActReport);
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
				tableData.createTableInfo("ClaimsDiscountActReportTable",null);
			tableData.setSearchData(this.populateSearchCriteriaForExceptionalRpt(frmClaimsDiscountActReport,request));
			tableData.modifySearchData("search");
			}//end of else
			
			if("QPR".equalsIgnoreCase(frmClaimsDiscountActReport.getString("switchType")))
			{
				((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(true); 
			}
			else
			{
				((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(false); 
			}
			
			alReportList= claimObject.getClaimsDiscountActReport(tableData.getSearchData());	 
			tableData.setData(alReportList, "search");
/*					((Column)((ArrayList)tableData.getTitle()).get()).setVisibility(false);*/
			request.getSession().setAttribute("tableData",tableData);
			request.getSession().setAttribute("frmClaimsDiscountActReport",frmClaimsDiscountActReport);
			return mapping.findForward(StrClaimsDiscountActReport);
		/*}else{
			return this.getForward(strSearchtClaimReports, mapping, request);
		}*/
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
		}//end of catch(Exception exp)
	} // end of doClmDiscountActReportSearch()
	
	private ArrayList<Object> populateSearchCriteriaForExceptionalRpt(DynaActionForm frmClaimsDiscountActReport,HttpServletRequest request)
	{
		ArrayList<Object> alSearchParams =new ArrayList<Object>();	
		alSearchParams.add(frmClaimsDiscountActReport.getString("tmtfromDate"));//1
		alSearchParams.add(frmClaimsDiscountActReport.getString("tmttoDate"));//2
		alSearchParams.add(frmClaimsDiscountActReport.getString("clmFromDate"));//3
		alSearchParams.add(frmClaimsDiscountActReport.getString("clmToDate"));//4
		alSearchParams.add(frmClaimsDiscountActReport.getString("patientName"));//5
		alSearchParams.add(frmClaimsDiscountActReport.getString("claimStatus"));//6
		alSearchParams.add(frmClaimsDiscountActReport.getString("invoiceNo"));//7
		alSearchParams.add(frmClaimsDiscountActReport.getString("batchNo"));//8
		alSearchParams.add(frmClaimsDiscountActReport.getString("alKootId"));//9
		alSearchParams.add(frmClaimsDiscountActReport.getString("claimNo"));//10
		alSearchParams.add(frmClaimsDiscountActReport.getString("benefitType"));//11
		alSearchParams.add(frmClaimsDiscountActReport.getString("eventRefNo"));//12
		alSearchParams.add(frmClaimsDiscountActReport.getString("qatarId"));//13
		alSearchParams.add(frmClaimsDiscountActReport.getString("payRefNo"));//14
		alSearchParams.add(frmClaimsDiscountActReport.getString("sProviderName"));//15
		if("QPR".equals(frmClaimsDiscountActReport.getString("switchType")))
			alSearchParams.add("CDR");//16
		else
			alSearchParams.add("PTR");//16
		alSearchParams.add(frmClaimsDiscountActReport.getString("finBatchNo"));//17
		alSearchParams.add(frmClaimsDiscountActReport.getString("financeStatus"));//18
		alSearchParams.add(frmClaimsDiscountActReport.getString("partnerNm"));//19
		return alSearchParams;
	} // end of populateSearchCriteriaForExceptionalRpt()
	
	public ActionForward  doClmDisActRptBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
		log.debug("Inside the doClmDisActRptBackward method of ClaimsAction");
		setLinks(request);
		//get the tbale data from session if exists+
		ClaimManager claimObject = this.getClaimManagerObject();
		TableData tableData =(request.getSession().getAttribute("tableData")!=null?(TableData)request.getSession().getAttribute("tableData"):new TableData());
		tableData.modifySearchData(strBackward);//modify the search data
		ArrayList alReportList = claimObject.getClaimsDiscountActReport(tableData.getSearchData());	 
		tableData.setData(alReportList, strBackward);//set the table data
		request.getSession().setAttribute("tableData",tableData);
		return mapping.findForward(StrClaimsDiscountActReport);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
		}//end of catch(Exception exp)
	}//end of doClmDisActRptBackward()
	
	public ActionForward doClmDisActRptForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
		log.debug("Inside the doClmDisActRptForward  method of ClaimsAction");
		setLinks(request);
		ClaimManager claimObject = this.getClaimManagerObject();
		//get the tbale data from session if exists
		TableData tableData =(request.getSession().getAttribute("tableData")!=null?(TableData)request.getSession().getAttribute("tableData"):new TableData());
		tableData.modifySearchData(strForward);
		ArrayList alReportList = claimObject.getClaimsDiscountActReport(tableData.getSearchData());
		tableData.setData(alReportList, strForward);
		request.getSession().setAttribute("tableData",tableData);
		return mapping.findForward(StrClaimsDiscountActReport); 
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strSearchtClaimReports));
		}//end of catch(Exception exp)
	} // end of doClmDisActRptForward()
	
	
	public ActionForward doGenerateClmDisActReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
	 try{
	    		log.debug("Inside the doGenerateClmDisActReport method of ClaimGeneralAction");
	    		setLinks(request);
	    		JasperReport mainJasperReport,emptyReport;
	    		TTKReportDataSource mainTtkReportDataSource = null;
	    		JasperPrint mainJasperPrint = null;
	    		String parameter=request.getParameter("parameter");	
	    		DynaActionForm frmClaimsDiscountActReport=(DynaActionForm) form;
	    		String jrxmlfile="generalreports/ClaimsDiscountActivityReport.jrxml";
	    		String jrxmlfile2="generalreports/ClaimsDiscountActivityReportInternational.jrxml";
	    		try
	    		{
	    			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			HashMap<String,String> hashMap = new HashMap<String,String>();
	    			ByteArrayOutputStream boas=new ByteArrayOutputStream();		
	    			mainTtkReportDataSource = new TTKReportDataSource("ClaimDiscountActivityReport", parameter);
	    			ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
	    			if("QPR".equalsIgnoreCase(frmClaimsDiscountActReport.getString("switchType")))
	    			{
	    				mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
	    			}
	    			else
	    			{
	    				mainJasperReport = JasperCompileManager.compileReport(jrxmlfile2);
	    			}
	    				
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
	    			request.setAttribute("reportType","EXCEL");
	    			response.setContentType("application/vnd.ms-excel");
	    			response.addHeader("Content-Disposition","attachment; filename=ClaimsDiscountActivityReport.xls");
	    			return (mapping.findForward(strReportdisplay));
	    		}
	    		catch(Exception exp)
	        	{
	    			return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
	        	}
	    	}
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
	    	}
  } // end of doGenerateClmDisActReport()
	
	
	
	public ActionForward getExceptionalClaimFlag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the getExceptionalClaimFlag method of ClaimGeneralAction");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			HttpSession session = request.getSession();
			ClaimManager claimObject = this.getClaimManagerObject();
			
			String claimStatus=frmClaimGeneral.getString("claimStatus");
			long claimSeqID = ClaimsWebBoardHelper.getClaimsSeqId(request);
			String exceptionFlag="YES";
			PreAuthDetailVO preauthVO=new PreAuthDetailVO();
			preauthVO = claimObject.getExceptionFlag(claimSeqID,exceptionFlag);
			
			
			frmClaimGeneral.set("referExceptionFalg", preauthVO.getExceptionFalg());
			frmClaimGeneral.set("exceptionalWarningMsg", preauthVO.getExceptionalWarningMsg());
			
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);
			
			return this.getForward(strClaimdetails, mapping, request);
			}
			catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}
		}
	
	
	
	public ActionForward doExceptionalHandling(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doExceptionalHandling method of ClaimGeneralAction");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			HttpSession session = request.getSession();
			
			String referExceptionFalg=frmClaimGeneral.getString("referExceptionFalg");
			
			frmClaimGeneral.set("exceptionFalg", referExceptionFalg);
			
			session.setAttribute("frmClaimGeneral", frmClaimGeneral);
			
			return this.getForward(strClaimdetails, mapping, request);
			}
			catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimDetail));
		}
		}
	
	
	
	public ActionForward doDefaultResubReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws TTKException {
		try {
			setLinks(request); 
			DynaActionForm frmClaimDetailedReports=(DynaActionForm) form;
			frmClaimDetailedReports.initialize(mapping);
			
			return mapping.findForward("resubmissionClaimReport");
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimDetail));
		}
	}	
	
	
	public ActionForward doGenerateClmResubmissionReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
	 try{
	    		log.debug("Inside the doGenerateClmResubmissionReport method of ClaimGeneralAction");
	    		setLinks(request);
	    		JasperReport mainJasperReport,mainJasperReport1,mainJasperReport2,emptyReport1,emptyReport2,emptyReport3;
	    		TTKReportDataSource mainTtkReportDataSource1 = null;
	    		TTKReportDataSource mainTtkReportDataSource2 = null;
	    		TTKReportDataSource mainTtkReportDataSource3 = null;
	    		JasperPrint mainJasperPrint = null;
	    		JasperPrint mainJasperPrint1 = null;
	    		JasperPrint mainJasperPrint2 = null;
	    		String parameter=request.getParameter("parameter");	
	    		System.out.println("parameter=====>"+parameter);
	    		DynaActionForm frmClaimDetailedReports=(DynaActionForm) form;
	    		String jrxmlfile="onlinereports/provider/claimDetailedReport.jrxml";
	    		String jrxmlfile1="reports/claims/claimAutoRejectionBatchDeailRepotr.jrxml";
	    		String jrxmlfile2="onlinereports/provider/claimResubmissionErrolLogReport.jrxml";
	    		ArrayList<JasperPrint> list = null;
	   		 list = new  ArrayList<JasperPrint>();
	    		try
	    		{
	    			ClaimManager claimObject = this.getClaimManagerObject();
	    			emptyReport1 = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			emptyReport2 = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			emptyReport3 = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			HashMap<String,String> hashMap = new HashMap<String,String>();
	    			ByteArrayOutputStream boas=new ByteArrayOutputStream();		
	    			mainTtkReportDataSource1 = claimObject.getDetailedResubmissionClaimDetails(parameter);
	    			mainTtkReportDataSource2=claimObject.getOverAllBatchResubmissionDeatils(parameter);
	    			mainTtkReportDataSource3=claimObject.getResubmissionErrorLogDetails(parameter);
	    			
	    			
	    			
	    			ServletOutputStream sos= response.getOutputStream();
	      			response.setContentType("application/vnd.ms-excel");
	      			
	      			
	      			
	      			ResultSet main_report_RS = mainTtkReportDataSource1.getResultData();
	      			ResultSet main_report_RS1 = mainTtkReportDataSource2.getResultData();
	      			ResultSet main_report_RS2 = mainTtkReportDataSource3.getResultData();
	      			
	      			mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
	      			mainJasperReport1 = JasperCompileManager.compileReport(jrxmlfile1);
	      			mainJasperReport2 = JasperCompileManager.compileReport(jrxmlfile2);
	      			if (main_report_RS != null && main_report_RS.next()) {
	      				main_report_RS.beforeFirst();
	      				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource1);  	
	      				 list.add(mainJasperPrint);
	      			} else {
	      				mainJasperPrint = JasperFillManager.fillReport(emptyReport1,hashMap, new JREmptyDataSource());
	      				 list.add(mainJasperPrint);
	      			}
	      			
	      			if (main_report_RS1 != null && main_report_RS1.next()) {
	      				main_report_RS1.beforeFirst();
	      				mainJasperPrint1 = JasperFillManager.fillReport(mainJasperReport1, hashMap, mainTtkReportDataSource2);  	
	      				 list.add(mainJasperPrint1);
	      			} else {
	      				mainJasperPrint1 = JasperFillManager.fillReport(emptyReport2,hashMap, new JREmptyDataSource());
	      				 list.add(mainJasperPrint1);
	      			}
	      			
	      			
	      			if (main_report_RS2 != null && main_report_RS2.next()) {
	      				main_report_RS2.beforeFirst();
	      				mainJasperPrint2 = JasperFillManager.fillReport(mainJasperReport2, hashMap, mainTtkReportDataSource3);  	
	      				 list.add(mainJasperPrint2);
	      			} else {
	      				mainJasperPrint2 = JasperFillManager.fillReport(emptyReport3,hashMap, new JREmptyDataSource());
	      				 list.add(mainJasperPrint2);
	      			}
	      			
	      			
	      			
	    	    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
	    	    		
	    	    		jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, list);
	    	    		jExcelApiExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{"Detailed Claim Report","Over all batch report","Error Log"}); 
	    	    		response.addHeader("Content-Disposition","attachment; filename=claimRe-SubmissionDetailedReport.xls");

	    	    		 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
	    				 jExcelApiExporter.exportReport();	
	    				   // request.setAttribute("boas",boas);
			    			request.setAttribute("reportType","EXCEL");
			    			//response.addHeader("Content-Disposition","attachment; filename=ResubmissionClaimDetailedReport.xls");
			    			sos.flush();
			  			
			  		     	sos.close();
			    			
			      	 		return null;
	      		
	    		}
	    		catch(Exception exp)
	        	{
	    			return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
	        	}
	    	}
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportdisplay));
	    	}
  }
	
	
	
	
	
	
	
	
	
	
}// end of ClaimGeneralAction

