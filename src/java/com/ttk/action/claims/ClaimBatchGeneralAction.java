/**
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

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ttk.business.claims.ClaimBatchManager;
import com.ttk.business.claims.ClaimManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.ClaimBatchWebBoardHelper;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.claims.BatchVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.PreAuthAssignmentVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is reused for adding pre-auth/claims in pre-auth and claims flow.
 */

public class ClaimBatchGeneralAction extends TTKAction {

	private static Logger log = Logger.getLogger( ClaimBatchGeneralAction.class );
	
	private static final String strClaimUpload="ClaimUpload";
	private static final String strClaimBatchDetails="ClaimsBatchDetails";
	private static final String strSelectEnrollmentID="SelectEnrollmentID";
	private static final String strAssignClaim="assignclaims";
	private static final String strAssignTo="assignBatch";
	private static final  String strBatchAssignHis="batchAssignHistoy";
	//private static final  String strViewBatchAssignHis="viewclaimbatchlist";


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
	public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
															HttpServletResponse response) throws Exception{
		try{
			setLinks(request);
			log.debug("Inside ClaimBatchGeneralAction doView");
			HttpSession session=request.getSession();
			DynaActionForm frmAddClaimDetails= (DynaActionForm)form;
			DynaActionForm frmClaimBatchGeneral= (DynaActionForm)form;
			frmAddClaimDetails.initialize(mapping);
			StringBuffer strCaption=new StringBuffer();
			BatchVO batchVO=null;
			ClaimBatchManager claimBatchManagerObject=null;
			//TableData tableData =(TableData)session.getAttribute("tableData");
			claimBatchManagerObject=this.getClaimBatchManagerObject();
			batchVO=(BatchVO)session.getAttribute("claimBatchVO");
			session.setAttribute("partnersList", claimBatchManagerObject.getPartnersList());
			
			if(ClaimBatchWebBoardHelper.checkWebBoardId(request)==null)
			{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.search.and.select.batch.details");
				frmClaimBatchGeneral.initialize(mapping);
				session.setAttribute("frmClaimBatchGeneral",frmClaimBatchGeneral);
				session.setAttribute("listOfClaims",null);
				throw expTTK;
			}//end of if(ClaimBatchWebBoardHelper.checkWebBoardId(request)==null)
			
					 Object[] batchDetails= claimBatchManagerObject.getClaimBatchDetails(ClaimBatchWebBoardHelper.getBatchSeqId(request));
					 batchVO=(BatchVO)batchDetails[0];
					 ArrayList<BatchVO> listOfClaims=(ArrayList<BatchVO>)batchDetails[1];
						frmClaimBatchGeneral= (DynaActionForm)FormUtils.setFormValues("frmClaimBatchGeneral",	batchVO, this, mapping, request);
						strCaption.append(" Edit");
						strCaption.append(" [ "+batchVO.getBatchNO()+ " ]");
						frmClaimBatchGeneral.set("caption", strCaption.toString());
						if("CNH".equals(batchVO.getClaimType())){
							session.setAttribute("ProviderLisenceNOForRe",batchVO.getProviderLisenceNO());
							session.setAttribute("ClaimTypeForRe",batchVO.getClaimType());
						}
						
			            Long  strBatchSeqID =batchVO.getBatchSeqID();
			    		TableData tableDataFast =session.getAttribute("tableData")==null?new TableData():(TableData)session.getAttribute("tableData");

			    		//TableData tableDataFast = (TableData) (request.getSession().getAttribute("batchAssignHistory")==null?new TableData():request.getSession().getAttribute("batchAssignHistory"));
			    		tableDataFast.createTableInfo("BatchAssignHistoryTable",null);
			    		ArrayList alBatchAssignHistory	=	claimBatchManagerObject.getBatchAssignHistory(strBatchSeqID);
			    		tableDataFast.setData(alBatchAssignHistory);
			    		//((Column)((ArrayList)tableDataFast.getTitle()).get(6)).setLinkTitle("CHECKHIPERLINK");
			    		request.getSession().setAttribute("tableData",tableDataFast);

						session.setAttribute("frmClaimBatchGeneral", frmClaimBatchGeneral);
						session.setAttribute("frmAddClaimDetails", frmAddClaimDetails);
						session.setAttribute("listOfClaims", listOfClaims);
						this.addToWebBoard(batchVO, request);
						
						request.getSession().setAttribute("batchVOForCompare", batchVO);
			return this.getForward(strClaimBatchDetails, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
			}//end of catch(Exception exp)
			}//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * Adds the selected item to the web board and makes it as the selected item in the web board
	 * @param  preauthVO  object which contains the information of the preauth
	 * * @param String  strIdentifier whether it is preauth or enhanced preauth
	 * @param request HttpServletRequest
	 * @throws TTKException if any runtime exception occures
	 */
	private void addToWebBoard(BatchVO batchVO, HttpServletRequest request)throws TTKException
	{
		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
		CacheObject cacheObject = new CacheObject();
		cacheObject.setCacheId(this.prepareWebBoardId(batchVO)); 
		cacheObject.setCacheDesc(batchVO.getBatchNO());
		alCacheObject.add(cacheObject);
		//if the object(s) are added to the web board, set the current web board id
		toolbar.getWebBoard().addToWebBoardList(alCacheObject);
		toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());

		//webboardinvoked attribute will be set as true in request scope
		//to avoid the replacement of web board id with old value if it is called twice in same request scope
		request.setAttribute("webboardinvoked", "true");
	}//end of addToWebBoard(PreAuthVO preAuthVO, HttpServletRequest request,String strIdentifier)throws TTKException
	
	/**
	 * This method prepares the Weboard id for the selected Policy
	 * @param preAuthVO  preAuthVO for which webboard id to be prepared
	 * * @param String  strIdentifier whether it is preauth or enhanced preauth
	 * @return Web board id for the passedVO
	 */
	private String prepareWebBoardId(BatchVO batchVO)throws TTKException
	{
		StringBuffer sbfCacheId=new StringBuffer();
		sbfCacheId.append(batchVO.getBatchSeqID()!=null? String.valueOf(batchVO.getBatchSeqID()):" ");
		sbfCacheId.append("~#~").append(batchVO.getBatchNO()!=null?batchVO.getBatchNO():" ");
		sbfCacheId.append("~#~").append(batchVO.getSubmissionType()!=null? String.valueOf(batchVO.getSubmissionType()):" ");
		sbfCacheId.append("~#~").append(batchVO.getEncounterTypeId()!=null? String.valueOf(batchVO.getEncounterTypeId()):" ");
		sbfCacheId.append("~#~").append(batchVO.getProviderName()!=null? String.valueOf(batchVO.getProviderName()):" ");
		sbfCacheId.append("~#~").append(batchVO.getClaimType()!=null? String.valueOf(batchVO.getClaimType()):" ");
		sbfCacheId.append("~#~").append(batchVO.getBatchStatus()!=null? String.valueOf(batchVO.getBatchStatus()):" ");
		sbfCacheId.append("~#~").append(batchVO.getProviderName()!=null? String.valueOf(batchVO.getProviderName()):" ");
		sbfCacheId.append("~#~").append(batchVO.getEncounterTypeId()!=null? String.valueOf(batchVO.getEncounterTypeId()):" ");
		sbfCacheId.append("~#~").append(batchVO.getSubmissionType()!=null? String.valueOf(batchVO.getSubmissionType()):" ");
		return sbfCacheId.toString();
	}//end of prepareWebBoardId(BatchVO BatchVO)throws TTKException
	
	/**
	 * This method is used to get the details of the selected record from web-board.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		log.debug("Inside ClaimBatchGenealAction doChangeWebBoard");
		return doView(mapping,form,request,response);
	}//end of doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)

	/**
     * This method is called from the struts framework.
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
public ActionForward addBatch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside ClaimBatchGeneralAction AddBatch ");
DynaActionForm frmClaimBatchGeneral= (DynaActionForm)form;
ClaimBatchManager claimBatchManagerObject = this.getClaimBatchManagerObject();
request.getSession().setAttribute("partnersList", claimBatchManagerObject.getPartnersList());
if("NO".equals(request.getParameter("initialize"))){
	frmClaimBatchGeneral.set("providerName", "");
	frmClaimBatchGeneral.set("providerID", "");
request.getSession().setAttribute("frmClaimBatchGeneral",frmClaimBatchGeneral);
}else{
frmClaimBatchGeneral.initialize(mapping);
frmClaimBatchGeneral.set("caption","Add[]");
request.getSession().setAttribute("frmClaimBatchGeneral",frmClaimBatchGeneral);
request.getSession().setAttribute("frmAddClaimDetails",null);
request.getSession().setAttribute("batchDetails",null);
request.getSession().setAttribute("batchVOForCompare",null);
request.getSession().setAttribute("listOfClaims",null);
}

return this.getForward(strClaimBatchDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
}//end of catch(Exception exp)
}//end of addBatch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

/**
 * This method is called from the struts framework.
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
public ActionForward setNetWorkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside ClaimBatchGeneralAction setNetWorkType");
DynaActionForm frmClaimBatchGeneral= (DynaActionForm)form;
String claimType=frmClaimBatchGeneral.getString("claimType");
String processType = frmClaimBatchGeneral.getString("processType");

//if("CNH".equals(claimType) && !"DBL".equals(processType))frmClaimBatchGeneral.set("netWorkType", "Y");
if("CNH".equals(claimType))frmClaimBatchGeneral.set("netWorkType", "Y");

else frmClaimBatchGeneral.set("netWorkType", "");
//frmClaimBatchGeneral.set("processType", frmClaimBatchGeneral.getString("processType"));

request.getSession().setAttribute("frmClaimBatchGeneral",frmClaimBatchGeneral);
return this.getForward(strClaimBatchDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
}//end of catch(Exception exp)
}//end of setNetWorkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
setLinks(request);
log.debug("Inside ClaimBatchGeneralAction doSave ");
HttpSession session=request.getSession();
DynaActionForm frmClaimBatchGeneral= (DynaActionForm)form;
StringBuffer strCaption=new StringBuffer();
BatchVO batchVO=null,batchVOComp1=null;
ClaimBatchManager claimBatchManagerObject=null;
String successMsg;
DynaActionForm 	frmAddClaimDetails=(DynaActionForm)request.getSession().getAttribute("frmAddClaimDetails");	
claimBatchManagerObject=this.getClaimBatchManagerObject();
       batchVO = (BatchVO)FormUtils.getFormValues(frmClaimBatchGeneral, this, mapping, request);
       batchVOComp1	=	(BatchVO) request.getSession().getAttribute("batchVOForCompare");
       ArrayList<BatchVO> listOfClaims1	=	(ArrayList<BatchVO>) request.getSession().getAttribute("listOfClaims");
if(listOfClaims1!=null && listOfClaims1.size()>0){
       if(batchVOComp1!=null){
		if("DTR".equals(batchVOComp1.getSubmissionType())){
		       	boolean bFlag = true;
				if (request.getSession().getAttribute("batchVOForCompare") != null) {
					bFlag = isModified(batchVO, batchVOComp1);
				}// end of if(request.getSession().getAttribute("alHospInfo")!=null)
				if (bFlag == false) {
					request.setAttribute("flagValidate", "errors.batch.modification");
					return this.getForward(strClaimBatchDetails, mapping, request);
				}// end of if(bFlag==false)
		}
	}
}
		
           batchVO.setAddedBy((TTKCommon.getUserSeqId(request)));
           
           
           	ArrayList<String> comp1	=	new ArrayList<>();
			ArrayList<String> comp2	=	new ArrayList<>();
			if("CNH".equals(batchVO.getClaimType()) && session.getAttribute("ProviderLisenceNOForRe")!=null){
				comp1.add(session.getAttribute("ProviderLisenceNOForRe").toString());
				comp1.add(session.getAttribute("ClaimTypeForRe").toString());
			}
			comp2.add(batchVO.getProviderLisenceNO());	
			comp2.add(batchVO.getClaimType());
			
		long batchSeqID= claimBatchManagerObject.saveClaimBatchDetails(batchVO);
		
		if(batchVO.getBatchSeqID()==null)successMsg="Batch Details Added Successfully";
		else successMsg="Batch Details Updated Successfully";
		
		 Object[] batchDetails= claimBatchManagerObject.getClaimBatchDetails(batchSeqID);
		 batchVO=(BatchVO)batchDetails[0];
		 session.setAttribute("ProviderLisenceNOForRe",batchVO.getProviderLisenceNO());
		 session.setAttribute("ClaimTypeForRe",batchVO.getClaimType());
		 ArrayList<BatchVO> listOfClaims=(ArrayList<BatchVO>)batchDetails[1];
			frmClaimBatchGeneral= (DynaActionForm)FormUtils.setFormValues("frmClaimBatchGeneral",	batchVO, this, mapping, request);
			strCaption.append(" Edit");
			strCaption.append(" [ "+batchVO.getBatchNO()+ " ]");
			frmClaimBatchGeneral.set("caption", strCaption.toString());
			if(!comp1.equals(comp2)){
				frmAddClaimDetails.set("enrollmentID", "");
				request.getSession().setAttribute("previousClaimNums", "");
			}
			request.getSession().setAttribute("frmAddClaimDetails", frmAddClaimDetails);
			request.getSession().setAttribute("batchVOForCompare", batchVO);
			session.setAttribute("frmClaimBatchGeneral", frmClaimBatchGeneral);
			session.setAttribute("listOfClaims", listOfClaims);
			this.addToWebBoard(batchVO, request);
			request.setAttribute("successMsg",successMsg);
return this.getForward(strClaimBatchDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
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
public ActionForward deleteClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside ClaimBatchGeneralAction deleteClaimDetails ");
HttpSession session=request.getSession();
DynaActionForm frmAddClaimDetails= (DynaActionForm)form;
ClaimBatchManager claimBatchManagerObject=null;
String successMsg;
claimBatchManagerObject=this.getClaimBatchManagerObject();
String strBatchSeqID=frmAddClaimDetails.getString("batchSeqID");
String strClaimSeqID=frmAddClaimDetails.getString("claimSeqID");

   claimBatchManagerObject.deleteInvoiceNO(strBatchSeqID, strClaimSeqID);
    successMsg="Deleted Successfully";
	
	 Object[] batchDetails= claimBatchManagerObject.getClaimBatchDetails(new Long(strBatchSeqID));
	// batchVO=(BatchVO)batchDetails[0];
	 ArrayList<BatchVO> listOfClaims=(ArrayList<BatchVO>)batchDetails[1];
	 frmAddClaimDetails.initialize(mapping);
		session.setAttribute("frmAddClaimDetails", frmAddClaimDetails);
		session.setAttribute("listOfClaims", listOfClaims);
		request.setAttribute("successMsg",successMsg);
return this.getForward(strClaimBatchDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
}//end of catch(Exception exp)
}//end of deleteClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
public ActionForward overrideBatchDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside ClaimBatchGeneralAction overrideBatchDetails");
HttpSession session=request.getSession();
StringBuffer strCaption=new StringBuffer();
DynaActionForm	frmClaimBatchGeneral= (DynaActionForm)session.getAttribute("frmClaimBatchGeneral");
String batchOverrideAllowYN =frmClaimBatchGeneral.getString("batchOverrideAllowYN");
BatchVO batchVO=null,batchVOComp1=null;
batchVO = (BatchVO)FormUtils.getFormValues(frmClaimBatchGeneral, this, mapping, request);
batchVOComp1	=	(BatchVO) request.getSession().getAttribute("batchVOForCompare");
ArrayList<BatchVO> listOfClaims1	=	(ArrayList<BatchVO>) request.getSession().getAttribute("listOfClaims");
if(listOfClaims1!=null && listOfClaims1.size()>0)
{
	if(batchVOComp1!=null)
	{
		if("DTR".equals(batchVOComp1.getSubmissionType()))
		{
	       	boolean bFlag = true;
			if (request.getSession().getAttribute("batchVOForCompare") != null) {
				bFlag = isModified(batchVO, batchVOComp1);
			}// end of if(request.getSession().getAttribute("alHospInfo")!=null)
			if (bFlag == false) {
				request.setAttribute("flagValidate", "errors.batch.modification");
				return this.getForward(strClaimBatchDetails, mapping, request);
			}// end of if(bFlag==false)
		}
	}
}

frmClaimBatchGeneral.set("overrideYN", "Y");
frmClaimBatchGeneral.set("batchStatus", "INP");
frmClaimBatchGeneral.set("completedYN", "");

			strCaption.append(" Edit");
			strCaption.append(" [ "+frmClaimBatchGeneral.getString("batchNO")+ " ]");
			frmClaimBatchGeneral.set("caption", strCaption.toString());
			frmClaimBatchGeneral.set("batchOverrideAllowYN",batchOverrideAllowYN);
			session.setAttribute("frmClaimBatchGeneral", frmClaimBatchGeneral);
			//session.setAttribute("listOfClaims", null);
return this.getForward(strClaimBatchDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
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
public ActionForward addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimBatchGeneralAction addClaimDetails ");
			HttpSession session = request.getSession();
			DynaActionForm frmAddClaimDetails = (DynaActionForm) form;
			BatchVO batchVO = null;
			ClaimBatchManager claimBatchManagerObject = null;
			String successMsg;
			ArrayList<Object> alFileAUploadList = new ArrayList<Object>();
			InputStream inputStream=null;
			int formFileSize=0;
			String fileNameWithPath = "";
			String origFileName = "";
			FormFile formFile = null;
			String fileDesc="";
			
			String submissionType = request.getParameter("submissionType");
			if(submissionType.equalsIgnoreCase("DTR")){
				alFileAUploadList.add(origFileName);// 0 File Name
				alFileAUploadList.add(fileNameWithPath);// 1File Path
				alFileAUploadList.add(fileDesc);
			}else{
			TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
			DateFormat df = new SimpleDateFormat("ddMMyyyy HH:mm:ss:S");
			df.setTimeZone(tz);
			//ArrayList<Object> alFileAUploadList = new ArrayList<Object>();
			formFile = (FormFile) frmAddClaimDetails.get("file");
			fileDesc = (String) frmAddClaimDetails.get("description");
			String strTimeStamp = ((df.format(Calendar.getInstance(tz).getTime())).replaceAll(" ", "_")).replaceAll(":", "");
			fileNameWithPath = strTimeStamp + "_" + formFile.getFileName();
			origFileName = formFile.getFileName();
			 inputStream = formFile.getInputStream();// INPUT STREAM FROM FILE UPLOADED
			 formFileSize = formFile.getFileSize();
			alFileAUploadList.add(origFileName);// 0 File Name
			alFileAUploadList.add(fileNameWithPath);// 1File Path
			alFileAUploadList.add(fileDesc);// 2 DropDown Value			
		}
						claimBatchManagerObject = this.getClaimBatchManagerObject();
						batchVO = (BatchVO) FormUtils.getFormValues(frmAddClaimDetails, this, mapping, request);
						batchVO.setAddedBy((TTKCommon.getUserSeqId(request)));
						long batchSeqID = claimBatchManagerObject.addClaimDetails(batchVO,alFileAUploadList,inputStream,formFileSize);
						if (batchVO.getClaimSeqID() == null)
							successMsg = "Added Successfully";
						else
							successMsg = "Updated Successfully";
						Object[] batchDetails = claimBatchManagerObject.getClaimBatchDetails(batchSeqID);
						ArrayList<BatchVO> listOfClaims = (ArrayList<BatchVO>) batchDetails[1];
						frmAddClaimDetails.initialize(mapping);
						session.setAttribute("previousClaimNums", null);
						session.setAttribute("frmAddClaimDetails",frmAddClaimDetails);
						session.setAttribute("listOfClaims", listOfClaims);
						request.setAttribute("successMsg", successMsg);

			return this.getForward(strClaimBatchDetails, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimBatchDetails));
		}// end of catch(Exception exp)
	}//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
public ActionForward doSelectEnrollmentID(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	HttpServletResponse response) throws Exception{
try{
	log.debug("Inside ClaimBatchGeneralAction doSelectEnrollmentID ");
setLinks(request);
TableData  tableData =TTKCommon.getTableData(request);
DynaActionForm frmAddClaimDetails= (DynaActionForm)form;
tableData.createTableInfo("EnrollmentSearchTable",new ArrayList());
request.getSession().setAttribute("tableData",tableData);
request.getSession().setAttribute("frmAddClaimDetails",frmAddClaimDetails);
return this.getForward(strSelectEnrollmentID, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
}//end of catch(Exception exp)
}//end of doSelectEnrollmentID(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
public ActionForward editClaimSubmissionDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside ClaimBatchGeneralAction editClaimSubmissionDetails ");
HttpSession session=request.getSession();

DynaActionForm frmAddClaimDetails= (DynaActionForm)form;
String rownum=request.getParameter("rownum");
   ArrayList<BatchVO> listOfClaims= (ArrayList<BatchVO>)session.getAttribute("listOfClaims");
   BatchVO  batchVO2 =listOfClaims.get(new Integer(rownum));
   frmAddClaimDetails.set("claimSeqID",batchVO2.getClaimSeqID().toString() );
   frmAddClaimDetails.set("providerInvoiceNO",batchVO2.getProviderInvoiceNO() );
   frmAddClaimDetails.set("requestedAmount",TTKCommon.checkNull(batchVO2.getRequestedAmount()).toString() );
	session.setAttribute("frmAddClaimDetails", frmAddClaimDetails);
		//this.addToWebBoard(preAuthDetailVO,request,"PAT");
return this.getForward(strClaimBatchDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
}//end of catch(Exception exp)
}//end of editClaimSubmissionDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
 	
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
public ActionForward editClaimReSubmissionDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside ClaimBatchGeneralAction editClaimReSubmissionDetails ");
HttpSession session=request.getSession();
ClaimManager claimManagerObject=this.getClaimManagerObject();
DynaActionForm frmAddClaimDetails= (DynaActionForm)form;
String rownum=request.getParameter("rownum");
   ArrayList<BatchVO> listOfClaims= (ArrayList<BatchVO>)session.getAttribute("listOfClaims");
   BatchVO  batchVO2 =listOfClaims.get(new Integer(rownum));
   frmAddClaimDetails.set("claimSeqID",batchVO2.getClaimSeqID().toString() );
   frmAddClaimDetails.set("providerInvoiceNO",batchVO2.getProviderInvoiceNO() );
   frmAddClaimDetails.set("previousClaimNO",batchVO2.getPreviousClaimNOSeqID().toString() );
   frmAddClaimDetails.set("enrollmentID",batchVO2.getEnrollmentID() );
   //Map<String,String> previousClaimNums=claimManagerObject.getMemClaimList(batchVO2.getEnrollmentSeqID());
   Map<String,String> previousClaimNums=claimManagerObject.getMemClaimList(batchVO2.getEnrollmentSeqID(),"","","","");
if(previousClaimNums==null)previousClaimNums=new LinkedHashMap<String,String>();
previousClaimNums.put(batchVO2.getPreviousClaimNOSeqID().toString(), batchVO2.getPreviousClaimNO());
	request.getSession().setAttribute("previousClaimNums", previousClaimNums);
   frmAddClaimDetails.set("enrollmentSeqID",TTKCommon.checkNull(batchVO2.getEnrollmentSeqID()).toString());
   frmAddClaimDetails.set("requestedAmount",batchVO2.getRequestedAmount().toString() );
   frmAddClaimDetails.set("resubmissionRemarks",batchVO2.getResubmissionRemarks() );
	session.setAttribute("frmAddClaimDetails", frmAddClaimDetails);
		//this.addToWebBoard(preAuthDetailVO,request,"PAT");
return this.getForward(strClaimBatchDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
}//end of catch(Exception exp)
}//end of editClaimReSubmissionDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
 	

//doClaimUpload

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
      public ActionForward doClaimUpload(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
              try{
               setLinks(request);
              log.debug("Inside ClaimBatchGeneralAction doClaimUpload ");
              

         
             return mapping.findForward(strClaimUpload);
          }//end of try
       catch(TTKException expTTK)
     {
      return this.processExceptions(request, mapping, expTTK);
      }//end of catch(TTKException expTTK)
     catch(Exception exp)
       {
      return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
      }//end of catch(Exception exp)
  }//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



	/**
	 * Returns the ClaimBatchManager session object for invoking methods on it.
	 * @return ClaimBatchManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private ClaimBatchManager getClaimBatchManagerObject() throws TTKException
	{
		ClaimBatchManager claimBatchManager = null;
		try
		{
			if(claimBatchManager == null)
			{
				InitialContext ctx = new InitialContext();
				claimBatchManager = (ClaimBatchManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimBatchManagerBean!com.ttk.business.claims.ClaimBatchManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strClaimBatchDetails);
		}//end of catch
		return claimBatchManager;
	}//end getClaimBatchManagerObject()
	/**
	 * Returns the ClaimManager session object for invoking methods on it.
	 * @return ClaimManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private ClaimManager getClaimManagerObject() throws TTKException
	{
		ClaimManager claimManager = null;
		try
		{
			if(claimManager == null)
			{
				InitialContext ctx = new InitialContext();
				claimManager = (ClaimManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimManagerBean!com.ttk.business.claims.ClaimManager");
			}//end of if(claimManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strClaimBatchDetails);
		}//end of catch
		return claimManager;
	}//end of getClaimManagerObject()


	
	private boolean isModified(BatchVO batchVO, BatchVO batchVOComp1) {
		
		ArrayList<Object> batchData1	=	new ArrayList<>();
		ArrayList<Object> batchData2	=	new ArrayList<>();
		batchData1.add(batchVO.getModeOfClaim());
		batchData1.add(batchVO.getSubmissionType());
		batchData1.add(batchVO.getBatchReceiveDate());
		batchData1.add(batchVO.getReceiveDay());
		batchData1.add(batchVO.getReceivedTime());
		batchData1.add(batchVO.getClaimType());
		batchData1.add(batchVO.getNetWorkType());
		batchData1.add(batchVO.getClaimFrom());
		//batchData1.add(batchVO.getBatchStatus());
		batchData1.add(batchVO.getPaymentTo());
		batchData1.add(batchVO.getProviderSeqID());
		batchData1.add(batchVO.getProviderID());
		batchData1.add(batchVO.getPartnerName());
		
		batchData2.add(batchVOComp1.getModeOfClaim());
		batchData2.add(batchVOComp1.getSubmissionType());
		batchData2.add(batchVOComp1.getBatchReceiveDate());
		batchData2.add(batchVOComp1.getReceiveDay());
		batchData2.add(batchVOComp1.getReceivedTime());
		batchData2.add(batchVOComp1.getClaimType());
		batchData2.add(batchVOComp1.getNetWorkType());
		batchData2.add(batchVOComp1.getClaimFrom());
		//batchData2.add(batchVOComp1.getBatchStatus());
		batchData2.add(batchVOComp1.getPaymentTo());
		batchData2.add(batchVOComp1.getProviderSeqID());
		batchData2.add(batchVOComp1.getProviderID());
		batchData2.add(batchVOComp1.getPartnerName());
		for (int i = 0; i < batchData1.size(); i++) {
			if (!TTKCommon.checkNull(batchData2.get(i)).equals(TTKCommon.checkNull(batchData1.get(i))))
				return false;

		}// end of for(int i=0;i<alHospInfoOld.size();i++)
		return true;
	}// end of isModified(ArrayList alHospInfoNew,ArrayList alHospInfoOld)
	

	public ActionForward doBatchAssign(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try
		{
			setLinks(request);
			log.debug("Inside ClaimBatchGeneralAction  doBatchAssign");
			String strModeValue = "";
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			ArrayList <Object>alAssignUserList=new ArrayList<Object>();
			ArrayList alUserList=null;
			if(strLink.equals("Claims")||strSubLink.equals("Claims"))
			{
				strModeValue="CLM";
			}

			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}
			
			DynaActionForm frmBatchAssign=(DynaActionForm)form;
			PreAuthAssignmentVO preAuthAssignmentVO=null;
			BatchVO batchVO=null;
			TableData tableData = (TableData)request.getSession().getAttribute("tableData");
			
			StringBuffer sbBatchSeqIds	=	new StringBuffer();
				String[] strChk = request.getParameterValues("chkopt");
				if(strChk!=null&&strChk.length!=0)
				{
					for(int k=0;k<strChk.length;k++){
						batchVO=(BatchVO)tableData.getData().get(Integer.parseInt(strChk[k]));
						sbBatchSeqIds.append(batchVO.getBatchSeqID());
						sbBatchSeqIds.append("|");
					}
				}else{
					batchVO	=	(BatchVO) request.getSession().getAttribute("batchVOForCompare");
					sbBatchSeqIds.append(batchVO.getBatchSeqID());
					sbBatchSeqIds.append("|");
				}
	
		
					frmBatchAssign.initialize(mapping);
					preAuthAssignmentVO=new PreAuthAssignmentVO();
					preAuthAssignmentVO.setSelectedBatchNos(batchVO.getBatchNO());
					preAuthAssignmentVO.setOfficeSeqID(batchVO.getOfficeSeqID());
					preAuthAssignmentVO.setSelectedBatchSeqId(sbBatchSeqIds.toString());
					
				alAssignUserList.add(preAuthAssignmentVO.getSelectedBatchSeqId());
				alAssignUserList.add(preAuthAssignmentVO.getPolicySeqID());
				alAssignUserList.add(preAuthAssignmentVO.getOfficeSeqID());
				/*long strVal=1;
				alAssignUserList.add(strVal);*/
				
				alUserList=preAuthObject.getAssignUserList(alAssignUserList,strModeValue);
				
				String selectedBatchSeqId="";
				selectedBatchSeqId=preAuthAssignmentVO.getSelectedBatchSeqId();
				frmBatchAssign=(DynaActionForm)FormUtils.setFormValues("frmBatchAssign", preAuthAssignmentVO, this,mapping, request);
				request.getSession().setAttribute("alUserList",alUserList);
				request.getSession().setAttribute("selectedBatchSeqId",selectedBatchSeqId);
				request.setAttribute("frmBatchAssign",frmBatchAssign);
			return mapping.findForward(strAssignTo);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimBatchDetails));
		}//end of catch(Exception exp)
	}// end of doBatchAssign(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	public ActionForward doBatchAssignSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimBatchGeneralAction doBatchAssignSave");
			String strModeValue = "";
			PreAuthManager preAuthObject = this.getPreAuthManagerObject();
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			ArrayList<Object> alAssignUserList = new ArrayList<Object>();
			ArrayList alUserList = new ArrayList();
			if (strLink.equals("Claims") || strSubLink.equals("Claims")) {
				strModeValue = "CLM";
			}// end of else if(strLink.equals("Claims"))
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) {
				((DynaActionForm) form).initialize(mapping);// reset the form													
			}
			DynaActionForm frmBatchAssign = (DynaActionForm) form;
			PreAuthAssignmentVO preAuthAssignmentVO = (PreAuthAssignmentVO) FormUtils.getFormValues(frmBatchAssign, this, mapping, request);
			preAuthAssignmentVO.setAssignUserSeqID(null);
			preAuthAssignmentVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
			String strSelectedBatchSeqIdval = (String) request.getSession().getAttribute("selectedBatchSeqId");
			preAuthAssignmentVO.setSelectedBatchSeqId(strSelectedBatchSeqIdval);	
			
			String assignClaimStatusVal[]= request.getParameterValues("assignClaimStatus");
			StringBuffer sbAssignClaimStatus	=	new StringBuffer();
			if(assignClaimStatusVal ==null ){	
    			TTKException expTTK = new TTKException();
 				expTTK.setMessage("error.AssignClaimStatus.required"); 
 				throw expTTK;
    		}
			else{
			for(int i=0;i<assignClaimStatusVal.length;i++){
				sbAssignClaimStatus.append(assignClaimStatusVal[i]);
				sbAssignClaimStatus.append("|");
				}
			}
			String strAssignClaimStatus=sbAssignClaimStatus.toString();
			preAuthAssignmentVO.setAssignClaimStatus(strAssignClaimStatus);

			Long lngResult = preAuthObject.assignBatchSave(preAuthAssignmentVO,strModeValue);
			//String strlngResult=lngResult+"";
			//System.out.println("lngResult:::::::::::"+lngResult);
			if (lngResult > 0) {
				request.setAttribute("updated", "message.saved");
				preAuthAssignmentVO = preAuthObject.getBatchAssignTo(lngResult,preAuthAssignmentVO.getUpdatedBy(), strModeValue);
			}// end of if(lngResult >0)
			else{
				request.setAttribute("errorMsg", "message.notSaved");
				//preAuthAssignmentVO = preAuthObject.getBatchAssignTo(lngResult,preAuthAssignmentVO.getUpdatedBy(), strModeValue);
			}
				alAssignUserList.add(preAuthAssignmentVO.getSelectedBatchSeqId());
				alAssignUserList.add(preAuthAssignmentVO.getPolicySeqID());
				alAssignUserList.add(preAuthAssignmentVO.getOfficeSeqID());
				alUserList = preAuthObject.getAssignUserList(alAssignUserList,strModeValue);
				request.getSession().setAttribute("alUserList", alUserList);
				frmBatchAssign = (DynaActionForm) FormUtils.setFormValues("frmBatchAssign",preAuthAssignmentVO, this, mapping, request);
				request.setAttribute("frmBatchAssign", frmBatchAssign);
			return mapping.findForward(strAssignTo);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimBatchDetails));
		}// end of catch(Exception exp)
	}// end of doBatchAssignSave(ActionMapping mapping,ActionForm form,HttpServletRequest
	

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
			throw new TTKException(exp, strClaimBatchDetails);
		}//end of catch
		return preAuthManager;
	}//end getPreAuthManagerObject()
	
	
    public ActionForward doViewBatchAssignHis(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doViewBatchAssignHis method of ClaimBatchGeneralAction");
    		setLinks(request);    		

    		HttpSession session=request.getSession();
    		DynaActionForm frmClaimBatchHistoryList = (DynaActionForm)form;
    		//get the tbale data from session if exists
    		TableData tableData =(session.getAttribute("tableData")==null)?new TableData():(TableData)session.getAttribute("tableData");
    		//clear the dynaform if visiting from left links for the first time
    		if("Y".equals(request.getParameter("Entry"))){
    			frmClaimBatchHistoryList.initialize(mapping);//reset the form data
    		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))

    		tableData.createTableInfo("ClaimBatchAssignHistoryTable",new ArrayList());
    		session.setAttribute("tableData",tableData);
    		frmClaimBatchHistoryList.initialize(mapping);//reset the form data
    		return this.getForward(strBatchAssignHis, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
    	}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
    public ActionForward doHistorySearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doHistorySearch method of ClaimBatchGeneralAction");
    		setLinks(request);
    		HttpSession session=request.getSession();
    		ClaimBatchManager claimBatchManagerObject=this.getClaimBatchManagerObject();
    		//get the tbale data from session if exists
    		TableData tableData =session.getAttribute("tableData")==null?new TableData():(TableData)session.getAttribute("tableData");
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
    				return mapping.findForward(strBatchAssignHis);
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
    			tableData.createTableInfo("ClaimBatchAssignHistoryTable",null);
    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
    			tableData.modifySearchData("search");
    		}//end of else
    		ArrayList alClaimsBatchList= claimBatchManagerObject.getClaimBatchHistoryList(tableData.getSearchData());
    		tableData.setData(alClaimsBatchList, "search");
    		//set the table data object to session
    		session.setAttribute("tableData",tableData);
    		//finally return to the grid screen
    		return this.getForward(strBatchAssignHis, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchDetails));
    	}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmClaimBatchHistoryList,HttpServletRequest request)
    {
    	//build the column names along with their values to be searched
    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
    	alSearchParams.add(frmClaimBatchHistoryList.getString("sBatchNumber"));
    	alSearchParams.add(frmClaimBatchHistoryList.getString("sProviderName"));
    	alSearchParams.add(TTKCommon.checkNull(frmClaimBatchHistoryList.getString("sBatchReceivedFrom")));
    	alSearchParams.add(TTKCommon.checkNull(frmClaimBatchHistoryList.getString("sBatchReceivedTo")));
    	alSearchParams.add(frmClaimBatchHistoryList.getString("sClaimType"));
    	alSearchParams.add(frmClaimBatchHistoryList.getString("sAssignedTo"));
    	alSearchParams.add(frmClaimBatchHistoryList.getString("sAssignedBY"));
		alSearchParams.add(frmClaimBatchHistoryList.getString("sProcessType"));
		alSearchParams.add(frmClaimBatchHistoryList.getString("sSubmissionType"));
    	alSearchParams.add(frmClaimBatchHistoryList.getString("sAssignClaimStatus"));
    	alSearchParams.add(TTKCommon.checkNull(frmClaimBatchHistoryList.getString("sAssignedFromDate")));
    	alSearchParams.add(TTKCommon.checkNull(frmClaimBatchHistoryList.getString("sAssignedToDate")));
    	alSearchParams.add(frmClaimBatchHistoryList.getString("sModeOfClaim"));
    	return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmClaimBatchList,HttpServletRequest request)


	
	}//end of ClaimBatchGeneralAction
