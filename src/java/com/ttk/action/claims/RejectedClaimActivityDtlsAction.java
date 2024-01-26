package com.ttk.action.claims;

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

public class RejectedClaimActivityDtlsAction  extends TTKAction{

	
	private static Logger log = Logger.getLogger(RejectedClaimActivityDtlsAction.class);
	
	  private static final String strClaimBatchSearchError="claimbatchsearch";
	    private static final String strAutoRejectDetails="ClaimAutoRejectionDetails";
	    private static final String strSelectEnrollID="selectEnrollmentId";
	    private static final String strActivityDetails="claimActivityDetails";
	    
	
	public ActionForward doEditActivity(ActionMapping mapping, ActionForm form, 	HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.info("Inside RejectedClaimActivityDtlsAction doEditActivity ");

			HttpSession session=request.getSession();
			DynaActionForm frmErrorLogList = (DynaActionForm)form;
			TableData tableData =(session.getAttribute("tableData")==null)?new TableData():(TableData)session.getAttribute("tableData");
			session.setAttribute("tableData",tableData);
			
			 String strParam = request.getParameter("parameter");
			
			String activityDetail[]=strParam.split("\\|");
			int sno=0;
			for(String w:activityDetail){ 
				
				sno++;
			}  
			
			frmErrorLogList.set("internalServiceCode", activityDetail[0]);
			frmErrorLogList.set("parentClaimSetlmntNo", activityDetail[1]);
			frmErrorLogList.set("serviceDate", activityDetail[2]);
			frmErrorLogList.set("activityType", activityDetail[3]);
			frmErrorLogList.set("serviceDescription", activityDetail[4]);
			frmErrorLogList.set("cptCode", activityDetail[5]);
			frmErrorLogList.set("reSubReqAmnt", activityDetail[6]);
			frmErrorLogList.set("quantity", activityDetail[7]);
			frmErrorLogList.set("toothNo", activityDetail[8]);
			frmErrorLogList.set("alkootRemarks", activityDetail[9]);
			frmErrorLogList.set("reSubJustification", activityDetail[10]);
			frmErrorLogList.set("errorLogs", activityDetail[11]);
			frmErrorLogList.set("xmlSeqId", activityDetail[12]);
			frmErrorLogList.set("autoRecjectSeqId", activityDetail[13]);
			frmErrorLogList.set("reSubInterSeqId", activityDetail[14]);
			frmErrorLogList.set("hospSeqId", activityDetail[15]);
			
			session.setAttribute("frmAutoRejCalimActivityDetails", frmErrorLogList);
		
			
			return this.getForward(strActivityDetails, mapping, request);
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
			log.info("Inside RejectedClaimActivityDtlsAction doSave ");
			HttpSession session=request.getSession();
			DynaActionForm frmAutiRejectionDetail = (DynaActionForm)form;
			ClaimManager claimObject = this.getClaimManagerObject();
			ClaimUploadErrorLogVO  autoRejectedVo=new ClaimUploadErrorLogVO();
			String successMsg;
			autoRejectedVo.setXmlSeqId((String) frmAutiRejectionDetail.get("xmlSeqId"));
			autoRejectedVo.setAutoRecjectSeqId((String)frmAutiRejectionDetail.get("autoRecjectSeqId"));
			autoRejectedVo.setServiceDate((String)frmAutiRejectionDetail.get("serviceDate"));
			autoRejectedVo.setActivityType((String)frmAutiRejectionDetail.get("activityType"));
			autoRejectedVo.setInternalServiceCode((String)frmAutiRejectionDetail.get("internalServiceCode"));
			autoRejectedVo.setServiceDescription((String) frmAutiRejectionDetail.get("serviceDescription"));;
			autoRejectedVo.setCptCode((String)frmAutiRejectionDetail.get("cptCode"));
			autoRejectedVo.setReSubReqAmnt((String)frmAutiRejectionDetail.get("reSubReqAmnt"));
			autoRejectedVo.setQuantity((String)frmAutiRejectionDetail.get("quantity"));
			autoRejectedVo.setToothNo((String)frmAutiRejectionDetail.get("toothNo")); 
			autoRejectedVo.setUpdatedBy(TTKCommon.getUserSeqId(request));
			autoRejectedVo.setParentClaimSetlmntNo((String)frmAutiRejectionDetail.get("parentClaimSetlmntNo")); 
			
			
			 claimObject.saveAutoRejectClaimActivityDetails(autoRejectedVo);
			
			 successMsg = "Claim Activity Details Updated Successfully";
			 
			 
			 frmAutiRejectionDetail.set("xmlSeqId", (String) frmAutiRejectionDetail.get("xmlSeqId"));
			// frmAutiRejectionDetail.set("previousClaimNo", (String) frmAutiRejectionDetail.get("parentClaimNo"));
			 
			 
			 
			request.getSession().setAttribute("frmAutoRejCalimActivityDetails",frmAutiRejectionDetail);
			request.setAttribute("successMsg", successMsg);
			return this.getForward(strActivityDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimBatchSearchError));
		}// end of catch(Exception exp)
	}
	
	
	public ActionForward doActivityClose(ActionMapping mapping, ActionForm form, 	HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.info("Inside ClaimAutoRejectionGeneralAction doView ");
	
			DynaActionForm frmAutoRectionClmDtls = (DynaActionForm) form;
			HttpSession session = request.getSession();
			ClaimManager claimObject = this.getClaimManagerObject();
			ClaimUploadErrorLogVO  autoRejectedVo=null;
			String xmlseqid=(String) request.getSession().getAttribute("xmlSeqId");
			String parentClaimNo=(String) request.getSession().getAttribute("parentClaimNo");
			
			String frmxmlseqid= (String) frmAutoRectionClmDtls.get("xmlSeqId");
			String frmparentClaimNo=(String) frmAutoRectionClmDtls.get("parentClaimNo");
			
			Object[] autoRejectionClaimResults = claimObject.getAutoRejectionClaimDetails(xmlseqid,parentClaimNo);
			
			autoRejectedVo = (ClaimUploadErrorLogVO) autoRejectionClaimResults[0];
			autoRejectedVo = autoRejectedVo == null ? new ClaimUploadErrorLogVO()
			: autoRejectedVo;
			
			frmAutoRectionClmDtls.set("xmlSeqId", xmlseqid);
			frmAutoRectionClmDtls.set("alkootId", autoRejectedVo.getAlkootId());
			frmAutoRectionClmDtls.set("previousClaimNo", autoRejectedVo.getParentClaimNo());
			frmAutoRectionClmDtls.set("memberName", autoRejectedVo.getMemberName());
			frmAutoRectionClmDtls.set("reSubRecDate", autoRejectedVo.getRecDate());
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
