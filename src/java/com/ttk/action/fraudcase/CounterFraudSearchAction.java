package com.ttk.action.fraudcase;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

public class CounterFraudSearchAction extends TTKAction{

	private static Logger log = Logger.getLogger(CounterFraudSearchAction.class);
	
	private static final String strFraudCaseListlist ="FraudCaseList";
	private static final String strClaimSearchError="hospitalsearch";
	private static final  String strClaimsPreauthlist="claimslist";
	private static final int ASSIGN_ICON=7;
	private static final String strClaimDetail = "claimDetail";
	private static final String strFraudInternalRemarks = "fraudInternalRemarks";
	private static final String strPreAuthDetail = "preauthdetail";
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	private static final String strClaimDetailHistory = "claimDetailHistory";
	private static final String strPreAuthDetailHistory = "preauthdetailHistory";
	
	public ActionForward doDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doDefault method of ClaimsAction");
			setLinks(request);
			String strDefaultBranchID = String
					.valueOf(((UserSecurityProfile) request.getSession()
							.getAttribute("UserSecurityProfile")).getBranchID());
			// get the tbale data from session if exists
			TableData tableData = TTKCommon.getTableData(request);
			// clear the dynaform if visiting from left links for the first time
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) {
				((DynaActionForm) form).initialize(mapping);// reset the form
															// data
			}// end of
				// if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmFraudCase = (DynaActionForm) form;
			// create new table data object
			tableData = new TableData();
			// create the required grid table
			tableData.createTableInfo("SuspectedFraudListTable", new ArrayList());
			request.getSession().setAttribute("tableData", tableData);
			((DynaActionForm) form).initialize(mapping);
			String activeLink = TTKCommon.getActiveLink(request);
			request.getSession().setAttribute("ACTIVELINK", activeLink);
			request.getSession().setAttribute("claimorpreauthswitchtype", "CLM");
			return this.getForward(strFraudCaseListlist, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strFraudCaseListlist));
		}// end of catch(Exception exp)
	}
	
	public ActionForward doSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doSearch method of ClaimsAction");
			setLinks(request);
			ClaimManager claimManagerObject = this.getClaimManagerObject();
			// get the tbale data from session if exists
			TableData tableData = TTKCommon.getTableData(request);
			// PreAuthVO preAuthVO=new PreAuthVO;
			// clear the dynaform if visting from left links for the first time
			// else get the dynaform data from session
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) {
				((DynaActionForm) form).initialize(mapping);// reset the form
															// data
			}// end of
				// if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			String strPageID = TTKCommon.checkNull(request
					.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request
					.getParameter("sortId"));

			// if the page number or sort id is clicked
			if (!strPageID.equals("") || !strSortID.equals("")) {
				if (!strPageID.equals("")) {
					tableData.setCurrentPage(Integer.parseInt(TTKCommon
							.checkNull(request.getParameter("pageId"))));
					return mapping.findForward(strFraudCaseListlist);
				}// /end of if(!strPageID.equals(""))
				else {
					tableData.setSortData(TTKCommon.checkNull(request
							.getParameter("sortId")));
					tableData.modifySearchData("sort");// modify the search data
				}// end of else
			}// end of if(!strPageID.equals("") || !strSortID.equals(""))
			else {
				// create the required grid table
				tableData.createTableInfo("SuspectedFraudListTable", null);
				tableData.setSearchData(this.populateSearchCriteria(
						(DynaActionForm) form, request));

				/*this.setColumnVisiblity(tableData, (DynaActionForm) form,
						request);*/
				tableData.modifySearchData("search");
			}// end of else
            DynaActionForm frmFraudCase = (DynaActionForm)form;
			ArrayList alClaimsList = claimManagerObject.getClaimAndPreauthList(tableData
					.getSearchData());
			tableData.setData(alClaimsList, "search");
			// set the table data object to session
			request.getSession().setAttribute("tableData", tableData);
			// finally return to the grid screen
			if(frmFraudCase.get("claimorpreauthswitchtype").equals("PAT")){
				request.getSession().setAttribute("preauthorclaimswitchType", "PAT");
				
			}else if(frmFraudCase.get("claimorpreauthswitchtype").equals("CLM")){
				request.getSession().setAttribute("preauthorclaimswitchType", "CLM");
			}
			return this.getForward(strFraudCaseListlist, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strFraudCaseListlist));
		}// end of catch(Exception exp)
	}// end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)
	
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
    		throw new TTKException(exp, strFraudCaseListlist);
    	}//end of catch
    	return claimManager;
    }//end getClaimManagerObject()
	
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmFraudCase,HttpServletRequest request)
    {
    	//build the column names along with their values to be searched
    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
    	alSearchParams.add(frmFraudCase.getString("claimNO"));//0
    	alSearchParams.add(frmFraudCase.getString("batchNO"));//1
    	alSearchParams.add(frmFraudCase.getString("providerName"));//2
    	alSearchParams.add(frmFraudCase.getString("claimType"));//3
    	alSearchParams.add(frmFraudCase.getString("internalRemarkStatus"));//4
    	alSearchParams.add(frmFraudCase.getString("settlementNO"));//5
    	alSearchParams.add(frmFraudCase.getString("providerNamesId"));//6
    	alSearchParams.add(frmFraudCase.getString("partnerName"));//7
    	alSearchParams.add(frmFraudCase.getString("submissionType"));//8
		alSearchParams.add(frmFraudCase.getString("riskLevel"));//9
		alSearchParams.add(frmFraudCase.getString("enrollmentId"));//10
		alSearchParams.add(frmFraudCase.getString("claimantName"));//11
		alSearchParams.add(frmFraudCase.getString("benefitType"));//12
		alSearchParams.add(frmFraudCase.getString("preapprovalNo"));//13
		alSearchParams.add(frmFraudCase.getString("authorizationNo"));//14
		alSearchParams.add(frmFraudCase.getString("invoiceNO"));//15
		alSearchParams.add(frmFraudCase.getString("status"));//16
		alSearchParams.add(frmFraudCase.getString("policyType"));//17
		alSearchParams.add(frmFraudCase.getString("claimorpreauthswitchtype"));//18
		alSearchParams.add(frmFraudCase.getString("cfdInvestigationStatus"));//19
		
		
		
    	return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmClaimsList,HttpServletRequest request)
	
	/*private void setColumnVisiblity(TableData tableData,
			DynaActionForm frmClaimList, HttpServletRequest request)
			throws TTKException {
		String strAssignTo = frmClaimList.getString("sAssignedTo");
		boolean blnVisibility = false;
		// For Self Check the Assign Permission
		if (strAssignTo.equals("SLF")
				&& TTKCommon.isAuthorized(request, "Assign")) {
			blnVisibility = true;
		}// end of if(strAssignTo.equals("SLF") &&
			// TTKCommon.isAuthorized(request,"Assign"))
		else // Check for the special Permission to show ICON for Others and
				// Unassigned Claim
		{
			if (TTKCommon.isAuthorized(request, "AssignAll")) {
				blnVisibility = true;
			}// end of if(TTKCommon.isAuthorized(request,"AssignAll"))
		}// end of else
		((Column) ((ArrayList) tableData.getTitle()).get(ASSIGN_ICON))
				.setVisibility(blnVisibility);
	}// end of setColumnVisiblity(TableData tableData,DynaActionForm
		// frmPreAuthList,HttpServletRequest request)
	*/
	
	public ActionForward doStatusChange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doBackward method of doStatusChange");
			DynaActionForm frmFraudCase = (DynaActionForm) form;
			
			request.getSession().setAttribute("claimorpreauthswitchtype", frmFraudCase.get("claimorpreauthswitchtype"));
			return this.getForward(strFraudCaseListlist, mapping, request); // finally
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
					exp, strClaimSearchError));
		}// end of catch(Exception exp)
	}// end of doBackward(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward doCopyToWebBoard(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside the doCopyToWebBoard method of ClaimsAction");
			setLinks(request);
			this.populateWebBoard(request);
			return this.getForward(strFraudCaseListlist, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strFraudCaseListlist));
		}// end of catch(Exception exp)
	}// end of doCopyToWebBoard(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	private void populateWebBoard(HttpServletRequest request)throws TTKException
    {
    	String[] strChk = request.getParameterValues("chkopt");
    	TableData tableData = (TableData)request.getSession().getAttribute("tableData");
    	Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
    	ArrayList<Object> alCacheObject = new ArrayList<Object>();
    	CacheObject cacheObject = null;
    	PreAuthDetailVO preAuthDetailVO=null;

    	if(strChk!=null&&strChk.length!=0)
    	{
    		for(int i=0; i<strChk.length;i++)
    		{
    			cacheObject = new CacheObject();
    			preAuthDetailVO=(PreAuthDetailVO)tableData.getData().get(Integer.parseInt(strChk[i]));
    			cacheObject.setCacheId(this.prepareWebBoardId(preAuthDetailVO));
    			cacheObject.setCacheDesc(preAuthDetailVO.getClaimOrPreauthNumber());
    			alCacheObject.add(cacheObject);
    		}//end of for(int i=0; i<strChk.length;i++)
    	}//end of if(strChk!=null&&strChk.length!=0)
    	if(toolbar != null)
    	{
    		toolbar.getWebBoard().addToWebBoardList(alCacheObject);
    	}//end of if(toolbar != null)
    }//end of populateWebBoard(HttpServletRequest request)
	
	private String prepareWebBoardId(PreAuthDetailVO preAuthDetailVO)throws TTKException
    {
    	StringBuffer sbfCacheId=new StringBuffer();
    	sbfCacheId.append(preAuthDetailVO.getClaimorpreauthseqId()!=null? String.valueOf(preAuthDetailVO.getClaimorpreauthseqId()):" ");
    	return sbfCacheId.toString();
    }//end of prepareWebBoardId(PreAuthVO preAuthVO,String strIdentifier)throws TTKException
	
	private void addToWebBoard(PreAuthDetailVO preAuthDetailVO, HttpServletRequest request)throws TTKException
    {
    	Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
    	ArrayList<Object> alCacheObject = new ArrayList<Object>();
    	CacheObject cacheObject = new CacheObject();
    	cacheObject.setCacheId(this.prepareWebBoardId(preAuthDetailVO)); //set the cacheID
    	cacheObject.setCacheDesc(preAuthDetailVO.getClaimOrPreauthNumber());
    	alCacheObject.add(cacheObject);
    	//if the object(s) are added to the web board, set the current web board id
    	toolbar.getWebBoard().addToWebBoardList(alCacheObject);
    	toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());

    	//webboardinvoked attribute will be set as true in request scope
    	//to avoid the replacement of web board id with old value if it is called twice in same request scope
    	request.setAttribute("webboardinvoked", "true");
    }//end of addToWebBoard(PreAuthVO preAuthVO, HttpServletRequest request,String strIdentifier)throws TTKException
	
	public ActionForward doView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doView method of ClaimsAction");
			setLinks(request);
			// get the tbale data from session if exists
			PreAuthDetailVO preAuthDetailVO=null;
			TableData tableData = TTKCommon.getTableData(request);
			DynaActionForm frmFraudCase = (DynaActionForm) form;
			frmFraudCase.initialize(mapping);
			if (!(TTKCommon.checkNull(request.getParameter("rownum"))
					.equals(""))) {
				preAuthDetailVO = (PreAuthDetailVO) tableData.getRowInfo(Integer
						.parseInt(request.getParameter("rownum")));
				request.getSession().setAttribute("claimStatus",
						preAuthDetailVO.getStatus());
				request.getSession().setAttribute("claimOrPreauthSeqId", preAuthDetailVO.getClaimorpreauthseqId());
			//	request.getSession().setAttribute("preAuthStatus", null);
				this.addToWebBoard(preAuthDetailVO, request);
			}// end of
				// if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			if(request.getSession().getAttribute("preauthorclaimswitchType")!=null){
				if(request.getSession().getAttribute("preauthorclaimswitchType").equals("PAT")){
					return mapping.findForward(strPreAuthDetail);
				}else if(request.getSession().getAttribute("preauthorclaimswitchType").equals("CLM")){
					return mapping.findForward(strClaimDetail);
				}
			}
			return mapping.findForward(strClaimDetail);
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimSearchError));
		}// end of catch(Exception exp)
	}// end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)
	
	public ActionForward viewCFDPreauthAndClaimAsSuspect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try {
			log.debug("Inside the doDefault method of ClaimsAction");
			setLinks(request);
			String strDefaultBranchID = String
					.valueOf(((UserSecurityProfile) request.getSession()
							.getAttribute("UserSecurityProfile")).getBranchID());
			// get the tbale data from session if exists
			TableData tableData = TTKCommon.getTableData(request);
			TableData tableDataForInvesigation = TTKCommon.getTableData(request);
			
			PreAuthManager preAuthManagerObject= this.getPreAuthManagerObject();
			// clear the dynaform if visiting from left links for the first time
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) {
				((DynaActionForm) form).initialize(mapping);// reset the form
															// data
			}// end of
				// if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmFraudCase = (DynaActionForm) form;
			String clmSeqId = frmFraudCase.getString("clmSeqId");
			String preauthSeqId = frmFraudCase.getString("preAuthSeqID");
			// create new table data object
			tableData = new TableData();
			tableDataForInvesigation = new TableData();
			// create the required grid table
			ArrayList listtoGetInternalRemarksDetails = new ArrayList<>();
			if(clmSeqId!= null && !clmSeqId.equals("")){
			listtoGetInternalRemarksDetails.add("CLM");
			listtoGetInternalRemarksDetails.add(clmSeqId);
			request.getSession().setAttribute("claimOrPreauthSeqId",clmSeqId);
			}else if(preauthSeqId !=null && !preauthSeqId.equals("") ){
				listtoGetInternalRemarksDetails.add("PAT");
				listtoGetInternalRemarksDetails.add(preauthSeqId);
				request.getSession().setAttribute("claimOrPreauthSeqId",preauthSeqId);
			}
			tableData.createTableInfo("InternalRemarksStatusTable", new ArrayList());
			tableDataForInvesigation.createTableInfo("InternalRemarksInvestigationTable", new ArrayList<>());
			
			Object[] suspectData = preAuthManagerObject.getFraudDetails(listtoGetInternalRemarksDetails);
			ArrayList internalRemarksDetailsList=(ArrayList) suspectData[0];
			ArrayList investigationDetailsList=(ArrayList) suspectData[1];
			tableData.setData(internalRemarksDetailsList, "search");
			tableDataForInvesigation.setData(investigationDetailsList, "search");
			if(!internalRemarksDetailsList.isEmpty()){
			PreAuthDetailVO preAuthDetailVO = (PreAuthDetailVO)internalRemarksDetailsList.get(0);
			frmFraudCase.set("internalRemarkStatus",preAuthDetailVO.getInternalRemarkStatus());
			frmFraudCase.set("riskLevel",preAuthDetailVO.getRiskLevel());
			if(preAuthDetailVO.getRiskRemarks() != null || !"".equals(preAuthDetailVO.getRiskRemarks()))
			{
				if("Investigation outcome".equals(preAuthDetailVO.getRiskRemarks()))
				{
					frmFraudCase.set("riskRemarks","INVO");
				}
				if("Business Decision".equals(preAuthDetailVO.getRiskRemarks()))
				{	
					frmFraudCase.set("riskRemarks","BUSS");
				}		
				if("Wrong risk level selected".equals(preAuthDetailVO.getRiskRemarks()))
				{
					frmFraudCase.set("riskRemarks","WRLS");
				}
			}
			frmFraudCase.set("investigationstatus","DOC");
			
			/*frmFraudCase.set("investigationstatus", preAuthDetailVO.getInvestigationstatus());
			frmFraudCase.set("investigationoutcomecategory", preAuthDetailVO.getInvestigationoutcomecategory());*/
			}
			request.getSession().setAttribute("frmFraudCase", frmFraudCase);
			request.getSession().setAttribute("tableData", tableData);
			request.getSession().setAttribute("tableDataForInvesigation", tableDataForInvesigation);
			request.setAttribute("WEBBORDHIDEFLAG", "Y");
			/*frmClaimsList.set("sTtkBranch", strDefaultBranchID);*/
			return this.getForward(strFraudInternalRemarks, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strFraudInternalRemarks));
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
			throw new TTKException(exp, strPreAuthDetail);
		}// end of catch
		return preAuthManager;
	}// end getPreAuthManagerObject()															// save
																
public ActionForward dosaveSuspectPreauthOrClaim(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws TTKException{
		
		try{
			TableData tableData = TTKCommon.getTableData(request);
			TableData tableDataForInvesigation = TTKCommon.getTableData(request);
		DynaActionForm frmFraudCase = (DynaActionForm)form;
		String userId  = String.valueOf(((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getUSER_SEQ_ID());
		tableData = new TableData();
		tableDataForInvesigation = new TableData();
		String claimOrPreauthSeqId = request.getParameter("claimOrPreauthSeqId");
		String preauthorclaimswitchType = request.getParameter("preauthorclaimswitchType");
	    
		String internalRemarkStatus = frmFraudCase.getString("internalRemarkStatus");
		String remarksforinternalstatus = frmFraudCase.getString("remarksforinternalstatus");
		String riskLevel = frmFraudCase.getString("riskLevel");
		String suspectVerified = frmFraudCase.getString("suspectVerified");
		String investigationstatus = frmFraudCase.getString("investigationstatus");				//	investigationstatus
		String investigationoutcomecategory = frmFraudCase.getString("investigationoutcomecategory");
		String amountutilizationforinvestigation = frmFraudCase.getString("amountutilizationforinvestigation");
		String amountsave = frmFraudCase.getString("amountsave");
		String dateOfReceivingCompReqInfo = frmFraudCase.getString("dateOfReceivingCompReqInfo");
		String riskRemarks = frmFraudCase.getString("riskRemarks");
		
		
		ArrayList<String> frdInputData = new ArrayList<>();
		if(preauthorclaimswitchType.equals("PAT")){
			frdInputData.add("PAT");
			}else{
				frdInputData.add("CLM");	
			}
		frdInputData.add(claimOrPreauthSeqId);
		frdInputData.add(investigationstatus);
		frdInputData.add(investigationoutcomecategory);
		frdInputData.add(amountutilizationforinvestigation);
		frdInputData.add(amountsave);
		frdInputData.add(remarksforinternalstatus);
		frdInputData.add(dateOfReceivingCompReqInfo);
		frdInputData.add(userId);
		frdInputData.add(riskRemarks);
		frdInputData.add(riskLevel);
		
		PreAuthManager preAuthManagerObject= this.getPreAuthManagerObject();
		long  frdOutputScreen=0;
		/*if(request.getSession().getAttribute("preAuthStatus") != null && (request.getSession().getAttribute("preAuthStatus").equals("In Progress") || request.getSession().getAttribute("preAuthStatus").equals("Required Information"))){*/
			frdOutputScreen = preAuthManagerObject.saveFraudDataForCFD(frdInputData);
		//	System.out.println("frdOutputScreen:::::::::::::::"+frdOutputScreen);
	/*	}else if(request.getSession().getAttribute("claimStatus") != null && (request.getSession().getAttribute("claimStatus").equals("In Progress") || request.getSession().getAttribute("claimStatus").equals("Required Information"))){
			frdOutputScreen = preAuthManagerObject.saveFraudDataForCFD(frdInputData);
		}else{
			request.setAttribute("errorMsg","Preauth/Claim already processed.");
		}*/
		if(frdOutputScreen > 0){
		
			request.setAttribute("successMsg","Invesitigation Details Updated Successfully");
		}
		
		ArrayList listtoGetInternalRemarksDetails = new ArrayList<>();
		if(preauthorclaimswitchType.equals("PAT")){
			listtoGetInternalRemarksDetails.add("PAT");
			}else{
				listtoGetInternalRemarksDetails.add("CLM");	
			}
		
		listtoGetInternalRemarksDetails.add(claimOrPreauthSeqId);
		tableData.createTableInfo("InternalRemarksStatusTable", new ArrayList());
		tableDataForInvesigation.createTableInfo("InternalRemarksInvestigationTable", new ArrayList());
		Object[] suspectData = preAuthManagerObject.getFraudDetails(listtoGetInternalRemarksDetails);
		ArrayList internalRemarksDetailsList = (ArrayList) suspectData[0];
		ArrayList investigationDetailsList = (ArrayList) suspectData[1];
		tableData.setData(internalRemarksDetailsList, "search");
		tableDataForInvesigation.setData(investigationDetailsList,"search");
		
		if("DOC".equals(investigationstatus))
		{	
			((Column)((ArrayList)tableDataForInvesigation.getTitle()).get(1)).setVisibility(false);
			((Column)((ArrayList)tableDataForInvesigation.getTitle()).get(3)).setVisibility(false);
			((Column)((ArrayList)tableDataForInvesigation.getTitle()).get(4)).setVisibility(false);
			((Column)((ArrayList)tableDataForInvesigation.getTitle()).get(6)).setVisibility(false);
		}
		if("II".equals(investigationstatus)) 
		{	
			((Column)((ArrayList)tableDataForInvesigation.getTitle()).get(1)).setVisibility(false);
			((Column)((ArrayList)tableDataForInvesigation.getTitle()).get(3)).setVisibility(false);
			((Column)((ArrayList)tableDataForInvesigation.getTitle()).get(4)).setVisibility(false);
		}
		
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		request.getSession().setAttribute("tableDataForInvesigation",tableDataForInvesigation);
		
		frmFraudCase.initialize(mapping);
		if(!internalRemarksDetailsList.isEmpty()){
			PreAuthDetailVO preAuthDetailVO = (PreAuthDetailVO)internalRemarksDetailsList.get(0);
			frmFraudCase.set("internalRemarkStatus",preAuthDetailVO.getInternalRemarkStatus());
			frmFraudCase.set("riskLevel",preAuthDetailVO.getRiskLevel());
			if(preAuthDetailVO.getRiskRemarks() != null || !"".equals(preAuthDetailVO.getRiskRemarks()))
			{
				if("Investigation outcome".equals(preAuthDetailVO.getRiskRemarks()))
				{
					frmFraudCase.set("riskRemarks","INVO");
				}
				if("Business Decision".equals(preAuthDetailVO.getRiskRemarks()))
				{	
					frmFraudCase.set("riskRemarks","BUSS");
				}		
				if("Wrong risk level selected".equals(preAuthDetailVO.getRiskRemarks()))
				{
					frmFraudCase.set("riskRemarks","WRLS");
				}
			}
			frmFraudCase.set("investigationstatus","DOC");
			}
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

public ActionForward doCloseInternalRemarks(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws TTKException {
	try {
		setLinks(request);
		DynaActionForm frmFraudCase = (DynaActionForm) form;
		
        
			if(request.getParameter("preauthorclaimswitchType").equals("PAT")){
				
				return mapping.findForward(strPreAuthDetail);
			}else if(request.getParameter("preauthorclaimswitchType").equals("CLM")){
				
				return mapping.findForward(strClaimDetail);
			}
		
		
		return mapping.findForward(strClaimDetail);
		
	}// end of try
	catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
	}// end of catch(TTKException expTTK)
	catch (Exception exp) {
		return this.processExceptions(request, mapping, new TTKException(
				exp, strClaimSearchError));
	}// end of catch(Exception exp)
}// end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest
	// request,HttpServletResponse response)

	public ActionForward doForward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside PreAuthAction doForward");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			ClaimManager claimManagerObject = this.getClaimManagerObject();
			tableData.modifySearchData(strForward);// modify the search data
			ArrayList alClaimsList = claimManagerObject.getClaimAndPreauthList(tableData
					.getSearchData());
			tableData.setData(alClaimsList, strForward);// set the table data
			request.getSession().setAttribute("tableData", tableData); // set
																		// the
																		// table
																		// data
																		// object
																		// to
																		// session
			return this.getForward(strFraudCaseListlist, mapping, request); // finally
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
					exp, strFraudCaseListlist));
		}// end of catch(Exception exp)
	}// end of doForward(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward doBackward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside PreAuthAction doForward");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			ClaimManager claimManagerObject = this.getClaimManagerObject();
			tableData.modifySearchData(strBackward);// modify the search data
			ArrayList alClaimsList = claimManagerObject.getClaimAndPreauthList(tableData
					.getSearchData());
			tableData.setData(alClaimsList, strBackward);// set the table data
			request.getSession().setAttribute("tableData", tableData); // set
																		// the
																		// table
																		// data
																		// object
																		// to
																		// session
			return this.getForward(strFraudCaseListlist, mapping, request); // finally
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
					exp, strFraudCaseListlist));
		}// end of catch(Exception exp)
	}// end of doForward(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
  
	public ActionForward doViewHistory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doViewHistory method of CounterFraudSearchAction");
			setLinks(request);
			DynaActionForm frmFraudCase = (DynaActionForm) form;
			frmFraudCase.initialize(mapping);				
			if(request.getSession().getAttribute("preauthorclaimswitchType")!=null){
				if(request.getSession().getAttribute("preauthorclaimswitchType").equals("PAT")){
					return mapping.findForward(strPreAuthDetailHistory);
				}else if(request.getSession().getAttribute("preauthorclaimswitchType").equals("CLM")){
					return mapping.findForward(strClaimDetailHistory);
				}
			}
			return mapping.findForward(strClaimDetailHistory);
			
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimSearchError));
		}// end of catch(Exception exp)
	}// end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest
	
	public ActionForward doChangeInvStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws TTKException
	{
		try 
		{
				log.debug("Inside the doChangeInvStatus method of CounterFraudSearchAction");
				setLinks(request);
				return mapping.findForward(strFraudInternalRemarks);
		}// end of try
		catch (TTKException expTTK) 
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) 
		{
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimSearchError));
		}
	} // end of doChangeInvStatus()
	
	public ActionForward doChangeRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws TTKException
	{
		try 
		{		log.debug("Inside the doChangeRiskLevel method of CounterFraudSearchAction");
				setLinks(request);
				DynaActionForm frmFraudCase = (DynaActionForm) form;
				frmFraudCase.set("riskLevelChangeYN", "Y");
				
				return mapping.findForward(strFraudInternalRemarks);
		}// end of try
		catch (TTKException expTTK) 
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) 
		{
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimSearchError));
		}
	} // end of doChangeInvStatus()
	
}
