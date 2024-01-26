package com.ttk.action.fraudcase;


import java.util.ArrayList;

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
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

public class CFDCampaignDetailAction extends TTKAction
{

	private static Logger log = Logger.getLogger(CFDCampaignDetailAction.class);
	private static final String strCFDCampaignDetail="campaignDetails";
	private static final String strCFDCampaignList ="cfdCampaignList";
	private static final String strCFDCampaignDetails ="CFDCampaignDetails";
	
	public ActionForward doDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 
	{
		try {
			log.debug("Inside the doDefault method of CFDCampaignDetailAction");
			setLinks(request);
																
			DynaActionForm frmCampaignDetail = (DynaActionForm) form;
			frmCampaignDetail.initialize(mapping);
			frmCampaignDetail.set("displayCampStatusFlag","N");
			return this.getForward(strCFDCampaignDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignDetail));
		}// end of catch(Exception exp)
	}
		
		public ActionForward doClose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
		{
			try 
			{
				log.debug("Inside the doClose method of CFDCampaignDetailAction");
				setLinks(request);

			     UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession(). getAttribute("UserSecurityProfile");
			     userSecurityProfile.getSecurityProfile().setActiveLink("CounterFraudDept");
			     userSecurityProfile.getSecurityProfile().setActiveSubLink("CFD Campaign");
			     userSecurityProfile.getSecurityProfile().setActiveTab("Search"); 
			     
				DynaActionForm frmFraudCampaign = (DynaActionForm) form;
				frmFraudCampaign.initialize(mapping);
				return this.getForward(strCFDCampaignList, mapping, request);
			}
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignDetail));
			}
		} // end of doClose()
		
		
		public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
				try{
				setLinks(request);
				
				log.debug("Inside the CFDCampaignDetailAction doView method");
				HttpSession session=request.getSession();
				DynaActionForm frmCampaignDetail= (DynaActionForm)form;
				ClaimManager claimManagerObject = this.getClaimManagerObject();
				PreAuthDetailVO preAuthDetailVO 	=	new PreAuthDetailVO();
				
				frmCampaignDetail.initialize(mapping);
				Long campaignDtlSeqId =  (Long)session.getAttribute("campaignDtlSeqId");
				
				preAuthDetailVO = claimManagerObject.getCampaignDetails(campaignDtlSeqId);
				
				if(preAuthDetailVO.getCampaignDtlSeqId() != null){
					frmCampaignDetail.set("campaignDtlSeqId", preAuthDetailVO.getCampaignDtlSeqId().toString());
				}
				else
					frmCampaignDetail.set("campaignDtlSeqId", "");	
				
				frmCampaignDetail.set("campaignName", preAuthDetailVO.getCampaignName());
				
				if(preAuthDetailVO.getProviderSeqId() != null){
					frmCampaignDetail.set("cfdProviderName", preAuthDetailVO.getProviderSeqId().toString());
				}
				else
					frmCampaignDetail.set("cfdProviderName", "");	
				
				frmCampaignDetail.set("campaginStartDate", preAuthDetailVO.getCampaginStartDate());
				 
				frmCampaignDetail.set("campaginEndDate", preAuthDetailVO.getCampaginEndDate());
				 
				frmCampaignDetail.set("campaginStatus", preAuthDetailVO.getCampaginStatus());
				  
				if(preAuthDetailVO.getUtilizedAmount() != null){
					frmCampaignDetail.set("utilizedAmount", preAuthDetailVO.getUtilizedAmount().toString());
				}
				else
					frmCampaignDetail.set("utilizedAmount", "");	
				
				if(preAuthDetailVO.getSavedAmount() != null){
					frmCampaignDetail.set("savedAmount", preAuthDetailVO.getSavedAmount().toString());
				}
				else
					frmCampaignDetail.set("savedAmount", "");
				
				if(preAuthDetailVO.getCfdtotCases() != null){
					frmCampaignDetail.set("totCases", preAuthDetailVO.getCfdtotCases().toString());
				}
				else
					frmCampaignDetail.set("totCases", "");   
				
				frmCampaignDetail.set("campaignRemarks", preAuthDetailVO.getCampaignRemarks());
				frmCampaignDetail.set("otherRemarks", preAuthDetailVO.getOtherRemarks());
				frmCampaignDetail.set("displayCampStatusFlag","Y");
				session.setAttribute("frmCampaignDetail", frmCampaignDetail);
				return this.getForward(strCFDCampaignDetails, mapping, request);
				}//end of try
				catch (TTKException expTTK) 
				{
					return this.processExceptions(request, mapping, expTTK);
				}// end of catch(TTKException expTTK)
				catch (Exception exp) 
				{
					return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignDetail));
				}// end of catch(Exception exp)
		}//end of doView()
		
		public ActionForward doSave(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
				throws Exception 
		{
			try {
				log.debug("Inside the doSave method of CFDCampaignDetailAction");
				setLinks(request);
				
				DynaActionForm frmCampaignDetail = (DynaActionForm)form;
				PreAuthDetailVO preAuthDetailVO 	=	new PreAuthDetailVO();
				ClaimManager claimManagerObject = this.getClaimManagerObject();
				HttpSession session=request.getSession();
			
				String displayCampStatusFlag =  frmCampaignDetail.getString("displayCampStatusFlag");
				String campaignDtlSeqId = frmCampaignDetail.getString("campaignDtlSeqId");
				campaignDtlSeqId = (campaignDtlSeqId == null || campaignDtlSeqId.length() < 1) ? "0"	: campaignDtlSeqId;
				preAuthDetailVO.setCampaignDtlSeqId(new Long(campaignDtlSeqId));
				
				preAuthDetailVO.setCampaignName(frmCampaignDetail.getString("campaignName"));
				
				String prividerSeqId = frmCampaignDetail.getString("cfdProviderName");
				prividerSeqId = (prividerSeqId == null || prividerSeqId.length() < 1) ? "0"	: prividerSeqId;
				preAuthDetailVO.setProviderSeqId(new Long(prividerSeqId));
				
				preAuthDetailVO.setCampaginStartDate(frmCampaignDetail.getString("campaginStartDate"));
				preAuthDetailVO.setCampaginEndDate(frmCampaignDetail.getString("campaginEndDate"));
				preAuthDetailVO.setCampaginStatus(frmCampaignDetail.getString("campaginStatus"));
				
				String utilizedAmount = frmCampaignDetail.getString("utilizedAmount");
				utilizedAmount = (utilizedAmount == null || utilizedAmount.length() < 1) ? "0"	: utilizedAmount;
				preAuthDetailVO.setUtilizedAmount(new Long(utilizedAmount));
				
				String savedAmount = frmCampaignDetail.getString("savedAmount");
				savedAmount = (savedAmount == null || savedAmount.length() < 1) ? "0"	: savedAmount;
				preAuthDetailVO.setSavedAmount(new Long(savedAmount));
				
				String totCases = frmCampaignDetail.getString("totCases");
				totCases = (totCases == null || totCases.length() < 1) ? "0"	: totCases;
				preAuthDetailVO.setCfdtotCases(new Long(totCases));
				
				preAuthDetailVO.setCampaignRemarks(frmCampaignDetail.getString("campaignRemarks"));
				preAuthDetailVO.setOtherRemarks(frmCampaignDetail.getString("otherRemarks"));
				preAuthDetailVO.setAddedBy((TTKCommon.getUserSeqId(request)));  
				preAuthDetailVO.setDisplayCampStatusFlag(displayCampStatusFlag);
				
			
				int res = claimManagerObject.saveCampaignDetails(preAuthDetailVO);
				if(res >0)
				{
					request.setAttribute("successMsg", "Campaign Deatils saved Successfully.");
				}
				frmCampaignDetail.set("displayCampStatusFlag",displayCampStatusFlag);
				session.setAttribute("frmCampaignDetail", frmCampaignDetail);
				return mapping.findForward(strCFDCampaignDetails);
			}// end of try
			catch (TTKException expTTK) 
			{
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) 
			{
				return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignDetail));
			}// end of catch(Exception exp)
		}
		
	private void ArrayList() {
			// TODO Auto-generated method stub
			
		}

	private ClaimManager getClaimManagerObject() throws TTKException
    {
    	ClaimManager claimManager = null;
    	try
    	{
    		if(claimManager == null)
    		{
    			InitialContext ctx = new InitialContext();
    			claimManager = (ClaimManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimManagerBean!com.ttk.business.claims.ClaimManager");
    		}//end if
    	}//end of try
    	catch(Exception exp)
    	{
    		throw new TTKException(exp, strCFDCampaignDetail);
    	}//end of catch
    	return claimManager;
    }
	
	public ActionForward doChangeCampaignStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		try 
		{
			log.debug("Inside the doChangeCampaignStatus method of CFDCampaignDetailAction");
			setLinks(request);
			return this.getForward(strCFDCampaignDetails, mapping, request);
		}
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignDetail));
		}
	} // end of doChangeCampaignStatus()

	public ActionForward addCompagin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 
	{
		try {
			log.debug("Inside the doAddCompagin method of CFDCampaignDetailAction");
			setLinks(request);
			DynaActionForm frmCampaignDetail = (DynaActionForm)form;													
			frmCampaignDetail.initialize(mapping);
			frmCampaignDetail.set("displayCampStatusFlag","N");
			return this.getForward(strCFDCampaignDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignDetail));
		}
	} // end of addCompagin()
} // end of CFDCampaignDetailAction
