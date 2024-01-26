package com.ttk.action.claims;

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
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;

public class ClaimAutoRejectionGeneralAction extends TTKAction{
	
	private static Logger log = Logger.getLogger(ClaimAutoRejectionGeneralAction.class);
	
	  private static final String strClaimBatchSearchError="claimbatchsearch";
	    private static final String strAutoRejectDetails="ClaimAutoRejectionDetails";
	    
	
	public ActionForward doView(ActionMapping mapping, ActionForm form, 	HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.info("Inside ClaimAutoRejectionGeneralAction doView ");
	
			DynaActionForm frmAutoRectionClmDtls = (DynaActionForm) form;
			HttpSession session = request.getSession();
			ClaimManager claimObject = this.getClaimManagerObject();
			ClaimUploadErrorLogVO  autoRejectedVo=null;
			String xmlseqid=(String) request.getSession().getAttribute("xmlSeqId");
			String parentClaimNo=(String) request.getSession().getAttribute("parentClaimNo");
			Object[] autoRejectionClaimResults = claimObject.getAutoRejectionClaimDetails(xmlseqid,parentClaimNo);
			
			autoRejectedVo = (ClaimUploadErrorLogVO) autoRejectionClaimResults[0];
			autoRejectedVo = autoRejectedVo == null ? new ClaimUploadErrorLogVO()
			: autoRejectedVo;
			
			frmAutoRectionClmDtls.set("xmlSeqId", xmlseqid);
			frmAutoRectionClmDtls.set("alkootId", autoRejectedVo.getAlkootId());
			frmAutoRectionClmDtls.set("previousClaimNo", autoRejectedVo.getParentClaimNo());
			frmAutoRectionClmDtls.set("memberName", autoRejectedVo.getMemberName());
			frmAutoRectionClmDtls.set("reSubRecDate", autoRejectedVo.getRecDate());
			frmAutoRectionClmDtls.set("hiddenPreviousClamNo", autoRejectedVo.getParentClaimNo());
			frmAutoRectionClmDtls.set("hiddenClaimAction", autoRejectedVo.getClaimAction());
			
			frmAutoRectionClmDtls.set("claimAction", autoRejectedVo.getClaimAction());
			frmAutoRectionClmDtls.set("rejectionReason", autoRejectedVo.getRejectionReason());
			
			
			session.setAttribute("frmAutiRejectionDetail", frmAutoRectionClmDtls);
			session.setAttribute("activityDetails", autoRejectionClaimResults[1]);
			return this.getForward(strAutoRejectDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimBatchSearchError));
		}// end of catch(Exception exp)
	}
	

	public ActionForward doSave(ActionMapping mapping, ActionForm form, 	HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//setLinks(request);
			log.info("Inside ClaimAutoRejectionGeneralAction doSave ");
			HttpSession session=request.getSession();
			DynaActionForm frmAutiRejectionDetail = (DynaActionForm)form;
			ClaimManager claimObject = this.getClaimManagerObject();
			ClaimUploadErrorLogVO  autoRejectedVo=new ClaimUploadErrorLogVO();
			String successMsg;
			String errorMsg;
			autoRejectedVo.setXmlSeqId((String) frmAutiRejectionDetail.get("xmlSeqId"));;
			autoRejectedVo.setAlkootId((String)frmAutiRejectionDetail.get("alkootId"));
			autoRejectedVo.setPreviousClaimNo((String)frmAutiRejectionDetail.get("previousClaimNo"));
			autoRejectedVo.setMemberName((String)frmAutiRejectionDetail.get("memberName"));
			autoRejectedVo.setReSubRecDate((String)frmAutiRejectionDetail.get("reSubRecDate"));
			autoRejectedVo.setHiddenPreviousClamNo((String)frmAutiRejectionDetail.get("hiddenPreviousClamNo"));
			autoRejectedVo.setUpdatedBy(TTKCommon.getUserSeqId(request));
			autoRejectedVo.setClaimAction((String)frmAutiRejectionDetail.get("claimAction"));
			autoRejectedVo.setRejectionReason((String)frmAutiRejectionDetail.get("rejectionReason"));
			
			 claimObject.saveAutoRejectClaimDetails(autoRejectedVo);
			 
			 if("REJA".equals(frmAutiRejectionDetail.get("claimAction"))){
				 errorMsg="The claim has been rejected, It cannot be edited further";
				 request.setAttribute("errorMsg", errorMsg);
			 }
			 else{
				 successMsg = "Claim Details Updated Successfully"; 
				 request.setAttribute("successMsg", successMsg);
			 }
			 
			 
			 
			 frmAutiRejectionDetail.set("hiddenClaimAction", frmAutiRejectionDetail.get("claimAction"));
			request.getSession().setAttribute("frmAutiRejectionDetail",frmAutiRejectionDetail);
			
			return this.getForward(strAutoRejectDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimBatchSearchError));
		}// end of catch(Exception exp)
	}
	
	
	
	
	public ActionForward doReEvaluate(ActionMapping mapping, ActionForm form, 	HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.info("Inside ClaimAutoRejectionGeneralAction doReEvaluate ");
	
			DynaActionForm frmAutoRectionClmDtls = (DynaActionForm) form;
			HttpSession session = request.getSession();
			ClaimManager claimObject = this.getClaimManagerObject();
			String xmlseqid=(String) request.getSession().getAttribute("xmlSeqId");
			//String parentClaimNo=(String) request.getSession().getAttribute("parentClaimNo");
			String parentClaimNo=(String) frmAutoRectionClmDtls.get("previousClaimNo");
			long updatedBy=TTKCommon.getUserSeqId(request);
			String successMsg;
		int revaluate = claimObject.doRevaluationClaim(xmlseqid,parentClaimNo,updatedBy);
			if(revaluate>0){
				 successMsg = "Moved claim to Re-submission processing";
					request.setAttribute("successMsg", successMsg);
			}
			
			return this.getForward(strAutoRejectDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimBatchSearchError));
		}// end of catch(Exception exp)
	}

	
	
	
	
	public ActionForward doChangeAction(ActionMapping mapping, ActionForm form, 	HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//setLinks(request);
			log.info("Inside ClaimAutoRejectionGeneralAction doChangeAction ");
			DynaActionForm frmAutiRejectionDetail = (DynaActionForm)form;
			
			
			String claimAction=frmAutiRejectionDetail.getString("claimAction");
			frmAutiRejectionDetail.set("claimAction", claimAction);
			
			request.getSession().setAttribute("frmAutiRejectionDetail",frmAutiRejectionDetail);
			return this.getForward(strAutoRejectDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimBatchSearchError));
		}// end of catch(Exception exp)
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
			throw new TTKException(exp, strClaimBatchSearchError);
		}// end of catch
		return claimManager;
	}
}
