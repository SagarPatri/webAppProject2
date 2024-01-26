package com.ttk.action.processedcliams;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.business.claims.ClaimManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.preauth.PreAuthDetailVO;

import formdef.plugin.util.FormUtils;

public class ProcessedClaimGeneralAction extends TTKAction{
	
	private static final  String strProcessedClaimDetails="processedClaimdetail";
	private static final String strProcessedClaims="ProcessedClaims";
	
	
	@SuppressWarnings("unused")
	public ActionForward doView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		try{
			setLinks(request);
			log.info("Inside ProcessedClaimGeneralAction doView ");
			DynaActionForm frmClaimGeneral = (DynaActionForm) form;
			HttpSession session = request.getSession();
			ClaimManager claimObject = this.getClaimManagerObject();
			PreAuthDetailVO preAuthDetailVO = null;
			
			String claimSeqID = null;
			
			
			Long clmid = (Long) request.getSession().getAttribute("claimSeqID");
			
			
			if(clmid==null){
				
				frmClaimGeneral.initialize(mapping);
				
				return this.getForward(strProcessedClaimDetails, mapping, request);
			}
			
			else{
				
				 claimSeqID =  (String) request.getSession().getAttribute("claimSeqID").toString();
			}
			
			
			
			Object[] claimResults = claimObject.getProcessedClaimDetails(claimSeqID);
			
			preAuthDetailVO = (PreAuthDetailVO) claimResults[0];
			preAuthDetailVO = preAuthDetailVO == null ? new PreAuthDetailVO()
					: preAuthDetailVO;
				preAuthDetailVO.setClaimSeqID(Long.parseLong(claimSeqID));
			
				session.setAttribute("claimDiagnosis", claimResults[1]);
				session.setAttribute("claimActivities", claimResults[2]);
				
				  frmClaimGeneral = (DynaActionForm)FormUtils.setFormValues("frmProcessedClaimGeneral",
		            		preAuthDetailVO,this,mapping,request);
				  
				  request.getSession().setAttribute("frmProcessedClaimGeneral",frmClaimGeneral);
			
			return this.getForward(strProcessedClaimDetails, mapping, request);
		}
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strProcessedClaims));
		}
		
	}
	
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
			throw new TTKException(exp, strProcessedClaims);
		}// end of catch
		return claimManager;
	}
}
