package com.ttk.action.preauth;

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
import com.ttk.action.table.TableData;
import com.ttk.business.preauth.PreAuthDashboardManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.AuthorizationDashboardVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.PreDashboardVO;

public class PreAuthDashboardAction extends TTKAction{
	
	private static Logger log = Logger.getLogger(PreAuthDashboardAction.class);
	
	 private static final String strPreauthDashbord="preauthdashboard";
	 private static final String strPreauthManagementDashboard="ManagenetDashboard";
	 private static final String strPreDashboardError="ManagenetDashboard";
	 private static final String strPreDashboardView="preauthViewPage";
	 private static final String strPreauthSearch="preauthsearch";
	 private static final String strPreAuthDetail = "PreAuthDetails";
     private static final String strRegular="Regular";    //identfier for Regular Pre-Auth
     private static final String strForward="Forward";
     private static final String strBackward="Backward";
   //Declarations of constants
   		
   		private static final String strEnhanced="Enhanced";  //identifier for Enhanced Pre-Auth
   		private static final String strRegularPreAuth="PAT";
   		private static final String strEnhancedPreAuth="ICO";
   		private static final int ASSIGN_ICON=9;
	  /**
     * This method is used to initialize the search grid.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
	
	public ActionForward doDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside PreAuthDashboardAction doDefault");
			
		
			
			return this.getForward(strPreauthDashbord, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPreauthDashbord));
		}// end of catch(Exception exp)
	}// end of doDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward doDisplayManaDashbordDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			/*setLinks(request);*/
			log.debug("Inside PreAuthDashboardAction doDisplayManaDashbordDefault");
			request.setAttribute("focusObj","divMainId");
		
		//	setLinks(request);
			Long  userSeqId = TTKCommon.getUserSeqId(request);
			DynaActionForm frmDashboardList= (DynaActionForm)form;  
			frmDashboardList.initialize(mapping);
			PreAuthDashboardManager preAuthDashboardManagerobject= this.getPreAuthDashboardManagerObject();
			
			PreDashboardVO  preDashboardVO = preAuthDashboardManagerobject.getManagementDetails(userSeqId);
			
			
			frmDashboardList.set("coding_cases",preDashboardVO.getCoding_cases());
			frmDashboardList.set("highValuePA",preDashboardVO.getHighValuePA());
			frmDashboardList.set("shortfall_cases", preDashboardVO.getShortfall_cases());
			frmDashboardList.set("shortfall_respond_ases", preDashboardVO.getShortfall_respond_ases());
			frmDashboardList.set("enhanced_shortfall_cases", preDashboardVO.getEnhanced_shortfall_cases());
			frmDashboardList.set("enhanced_shrtfal_res_cases", preDashboardVO.getEnhanced_shrtfal_res_cases());
			
			frmDashboardList.set("approved_cases", preDashboardVO.getApproved_cases());
			frmDashboardList.set("rejected_cases", preDashboardVO.getRejected_cases());
			frmDashboardList.set("forResolution", preDashboardVO.getForResolution());
			frmDashboardList.set("enhance_inp_cases",preDashboardVO.getEnhance_case());
			frmDashboardList.set("appeal_inp_cases",preDashboardVO.getAppeal_case());
			frmDashboardList.set("fress_case",preDashboardVO.getFresh_case());
			frmDashboardList.set("searchType","PID");
			/*request.setAttribute("preDashboardVO", preDashboardVO);*/
			TableData tDUserInfo = TTKCommon.getTableData(request,"tDUserInfo");
			tDUserInfo.createTableInfo("DataEntryUserInfoTable",new ArrayList());
			tDUserInfo.getSearchData().add(userSeqId);
			ArrayList al =preAuthDashboardManagerobject.getDataEntryUserInfo(tDUserInfo.getSearchData());
			
			tDUserInfo.setData(al, "search");
			request.getSession().setAttribute("tDUserInfo",tDUserInfo); 
			
			
			
			TableData tdUserActiveInfo = TTKCommon.getTableData(request,"tDSelfAssignment");
			tdUserActiveInfo.createTableInfo("DataEntryActiveUsers",new ArrayList());
			tdUserActiveInfo.getSearchData().add(userSeqId);
			ArrayList alSelfAssignment = preAuthDashboardManagerobject.getDataEntrySelfAssinmentCase(tdUserActiveInfo.getSearchData());
			
			tdUserActiveInfo.setData(alSelfAssignment, "search");
			request.getSession().setAttribute("tDSelfAssignment",tdUserActiveInfo); 
			
			TableData tableData = TTKCommon.getTableData(request);					 			    				 
			tableData.createTableInfo("PreAuthManaSearchTable",new ArrayList());
			ArrayList<AuthorizationDashboardVO> alDataEntryActiveUserDetails1 = new ArrayList<>();
				
			tableData.setData(alDataEntryActiveUserDetails1, "search");
			request.getSession().setAttribute("tableData",tableData);
			
			
			frmDashboardList.set("search_type_name","Pre-Authorization Requests");
			frmDashboardList.set("search_type_count",0);
			
			
			return this.getForward(strPreauthManagementDashboard, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPreauthManagementDashboard));
		}// end of catch(Exception exp)
	}// end of doDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
	
	public ActionForward doDisplayManaDashbord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
		
			log.debug("Inside PreAuthDashboardAction doDisplayManaDashbord");
			request.setAttribute("focusObj","searchTypeId");
			Long  userSeqId = TTKCommon.getUserSeqId(request);
			DynaActionForm frmDashboardList= (DynaActionForm)form;  
			frmDashboardList.initialize(mapping);
			PreAuthDashboardManager preAuthDashboardManagerobject= this.getPreAuthDashboardManagerObject();
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			String strChild = TTKCommon.checkNull(request.getParameter("child"));			
			PreDashboardVO  preDashboardVO = preAuthDashboardManagerobject.getManagementDetails(userSeqId);
			frmDashboardList.set("coding_cases",preDashboardVO.getCoding_cases());
			frmDashboardList.set("highValuePA",preDashboardVO.getHighValuePA());
			frmDashboardList.set("shortfall_cases", preDashboardVO.getShortfall_cases());
			frmDashboardList.set("shortfall_respond_ases", preDashboardVO.getShortfall_respond_ases());
			frmDashboardList.set("enhanced_shortfall_cases", preDashboardVO.getEnhanced_shortfall_cases());
			frmDashboardList.set("enhanced_shrtfal_res_cases", preDashboardVO.getEnhanced_shrtfal_res_cases());
			
			frmDashboardList.set("approved_cases", preDashboardVO.getApproved_cases());
			frmDashboardList.set("rejected_cases", preDashboardVO.getRejected_cases());
			frmDashboardList.set("forResolution", preDashboardVO.getForResolution());
			frmDashboardList.set("enhance_inp_cases",preDashboardVO.getEnhance_case());
			frmDashboardList.set("appeal_inp_cases",preDashboardVO.getAppeal_case());
			frmDashboardList.set("fress_case",preDashboardVO.getFresh_case());
			frmDashboardList.set("searchType","EID");
			String searchtypename = request.getParameter("searchType");
			Integer searchtypeount =0;
			if(request.getParameter("search_type_count") !=null){
				searchtypeount = Integer.parseInt(request.getParameter("search_type_count"));
			}else if(request.getSession().getAttribute("search_type_count") !=null){
				searchtypeount = (Integer)(request.getSession().getAttribute("search_type_count"));
			}
			
			
			
			request.getSession().setAttribute("search_type_name", searchtypename);
			
			frmDashboardList.set("search_type_name",searchtypename);
			frmDashboardList.set("search_type_count",searchtypeount);
			/*request.setAttribute("preDashboardVO", preDashboardVO);*/
			TableData tDUserInfo = TTKCommon.getTableData(request,"tDUserInfo");
			tDUserInfo.createTableInfo("DataEntryUserInfoTable",new ArrayList());
			tDUserInfo.getSearchData().add(userSeqId);
			ArrayList al =preAuthDashboardManagerobject.getDataEntryUserInfo(tDUserInfo.getSearchData());
			
			tDUserInfo.setData(al, "search");
			request.getSession().setAttribute("tDUserInfo",tDUserInfo); 
			TableData tdUserActiveInfo = TTKCommon.getTableData(request,"tDSelfAssignment");
			if(strChild.equals("") || strChild.equals("tDSelfAssignment")){
				if(!strPageID.equals("") || !strSortID.equals(""))
				{
					if(!strPageID.equals(""))
					{
						
						tdUserActiveInfo.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
						return mapping.findForward(strPreauthManagementDashboard);
					}
					else
					{
						tdUserActiveInfo.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
						tdUserActiveInfo.modifySearchData("sort");//modify the search data
					}
				}
				else{
					tdUserActiveInfo.createTableInfo("DataEntryActiveUsers",new ArrayList());
				}
					
			
			tdUserActiveInfo.getSearchData().add(userSeqId);
			ArrayList alSelfAssignment = preAuthDashboardManagerobject.getDataEntrySelfAssinmentCase(tdUserActiveInfo.getSearchData());
			
			tdUserActiveInfo.setData(alSelfAssignment, "search");
			request.getSession().setAttribute("tDSelfAssignment",tdUserActiveInfo); 
			}
			TableData tableData = TTKCommon.getTableData(request);
			
			
			
			if(strChild.equals("") || strChild.equals("tableData")){
				if(!strPageID.equals("") || !strSortID.equals(""))
				{
					if(!strPageID.equals(""))
					{
						

						tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
						return mapping.findForward(strPreauthManagementDashboard);
					}
					else
					{
						tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
						tableData.modifySearchData("sort");
					}
				}
				else
				{
					 tableData.createTableInfo("PreAuthManaSearchTable",new ArrayList());
					 
					 
				}
			
				  String searchID = request.getParameter("serId");
				  String searchVal = request.getParameter("serVal");
				  String queueStatus = request.getParameter("queueStatus");
				  String forwardpar = request.getParameter("forwardpar");
				 
				
				  request.getSession().setAttribute("searchID", searchID);
				  request.getSession().setAttribute("searchVal", searchVal);
				  request.getSession().setAttribute("queueStatus", queueStatus);
						
					 
					  tableData.getSearchData().add(searchID);
					  tableData.getSearchData().add(searchVal);
					  tableData.getSearchData().add(queueStatus);
					  tableData.modifySearchData("search");
				
				
			//System.out.println("table data.."+tableData.getSearchData());
			ArrayList<PreAuthVO> alDataEntryActiveUserDetails1 = preAuthDashboardManagerobject.getDataEntryActiveUsers(tableData.getSearchData());						
			frmDashboardList.set("search_type_count",searchtypeount);
			tableData.setData(alDataEntryActiveUserDetails1, "search");
			request.getSession().setAttribute("tableData",tableData);
			request.getSession().setAttribute("search_type_count",searchtypeount);
			frmDashboardList.set("searchType",searchID);
			}
			
			return this.getForward(strPreauthManagementDashboard, mapping, request);
		
		
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPreauthManagementDashboard));
		}// end of catch(Exception exp)
	}// end of doDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	

	public ActionForward doViewPreauth(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			
			setLinks(request);
		    String tableName = 	request.getParameter("tableName");
		    TableData preAuthListData = null;
		    if("tDSelfAssignment".equalsIgnoreCase(tableName)){
			 preAuthListData = (TableData)request.getSession().getAttribute("tDSelfAssignment");
		    }else{
		    	preAuthListData = (TableData)request.getSession().getAttribute("tableData");
		    }
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				PreAuthVO preAuthVO=(PreAuthVO)preAuthListData.getRowInfo(Integer.parseInt((String)(frmPreAuthList).get("rownum")));
				
				
				request.getSession().setAttribute("dashboardpreauthseqid", preAuthVO.getPreAuthSeqID());
				request.getSession().setAttribute("preauthviewfromdashbord", "Y");
				request.setAttribute("invoked",null);
				//this.addToWebBoard(preAuthVO, request,strEnhanced);
				this.addToWebBoard(preAuthVO, request,strEnhanced);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			return mapping.findForward(strPreAuthDetail);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPreauthManagementDashboard));
		}// end of catch(Exception exp)
	}// end of doDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	public ActionForward doDisplayManaDashborddobackword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
		
			log.debug("Inside PreAuthDashboardAction doDisplayManaDashbord");
			request.setAttribute("focusObj","searchTypeId");
			Long  userSeqId = TTKCommon.getUserSeqId(request);
			DynaActionForm frmDashboardList= (DynaActionForm)form;  
			frmDashboardList.initialize(mapping);
			PreAuthDashboardManager preAuthDashboardManagerobject= this.getPreAuthDashboardManagerObject();
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			String strChild = TTKCommon.checkNull(request.getParameter("child"));			
			PreDashboardVO  preDashboardVO = preAuthDashboardManagerobject.getManagementDetails(userSeqId);
			frmDashboardList.set("coding_cases",preDashboardVO.getCoding_cases());
			frmDashboardList.set("highValuePA",preDashboardVO.getHighValuePA());
			frmDashboardList.set("shortfall_cases", preDashboardVO.getShortfall_cases());
			frmDashboardList.set("shortfall_respond_ases", preDashboardVO.getShortfall_respond_ases());
			frmDashboardList.set("enhanced_shortfall_cases", preDashboardVO.getEnhanced_shortfall_cases());
			frmDashboardList.set("enhanced_shrtfal_res_cases", preDashboardVO.getEnhanced_shrtfal_res_cases());
			
			frmDashboardList.set("approved_cases", preDashboardVO.getApproved_cases());
			frmDashboardList.set("rejected_cases", preDashboardVO.getRejected_cases());
			frmDashboardList.set("forResolution", preDashboardVO.getForResolution());
			frmDashboardList.set("enhance_inp_cases",preDashboardVO.getEnhance_case());
			frmDashboardList.set("appeal_inp_cases",preDashboardVO.getAppeal_case());
			frmDashboardList.set("fress_case",preDashboardVO.getFresh_case());
			frmDashboardList.set("searchType","EID");
			String searchtypename = request.getParameter("searchType");
			request.getSession().setAttribute("search_type_name", searchtypename);
			frmDashboardList.set("search_type_name",searchtypename);
			Integer searchtypeount =0;
			if(request.getParameter("search_type_count") !=null){
				searchtypeount = Integer.parseInt(request.getParameter("search_type_count"));
			}else if(request.getSession().getAttribute("search_type_count") !=null){
				searchtypeount = (Integer)(request.getSession().getAttribute("search_type_count"));
			}
			frmDashboardList.set("search_type_count",searchtypeount);
			/*request.setAttribute("preDashboardVO", preDashboardVO);*/
			TableData tDUserInfo = TTKCommon.getTableData(request,"tDUserInfo");
			tDUserInfo.createTableInfo("DataEntryUserInfoTable",new ArrayList());
			tDUserInfo.getSearchData().add(userSeqId);
			ArrayList al =preAuthDashboardManagerobject.getDataEntryUserInfo(tDUserInfo.getSearchData());
			
			tDUserInfo.setData(al, "search");
			request.getSession().setAttribute("tDUserInfo",tDUserInfo); 
			TableData tdUserActiveInfo = TTKCommon.getTableData(request,"tDSelfAssignment");
			if(strChild.equals("") || strChild.equals("tDSelfAssignment")){
				if(!strPageID.equals("") || !strSortID.equals(""))
				{
					if(!strPageID.equals(""))
					{
						
						tdUserActiveInfo.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
						return mapping.findForward(strPreauthManagementDashboard);
					}
					else
					{
						tdUserActiveInfo.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
						tdUserActiveInfo.modifySearchData("sort");//modify the search data
					}
				}
				else{
					tdUserActiveInfo.createTableInfo("DataEntryActiveUsers",new ArrayList());
				}
					
			
			tdUserActiveInfo.getSearchData().add(userSeqId);
			ArrayList alSelfAssignment = preAuthDashboardManagerobject.getDataEntrySelfAssinmentCase(tdUserActiveInfo.getSearchData());
			
			tdUserActiveInfo.setData(alSelfAssignment, "search");
			request.getSession().setAttribute("tDSelfAssignment",tdUserActiveInfo); 
			}
			TableData tableData = TTKCommon.getTableData(request);
					
			tableData.modifySearchData(strBackward);//modify the search data
			
			
			ArrayList<PreAuthVO> alDataEntryActiveUserDetails1 = preAuthDashboardManagerobject.getDataEntryActiveUsers(tableData.getSearchData());						
			frmDashboardList.set("search_type_count",searchtypeount);
			tableData.setData(alDataEntryActiveUserDetails1,strBackward);
			request.getSession().setAttribute("tableData",tableData);
			request.getSession().setAttribute("search_type_count",searchtypeount);
			
			
			return this.getForward(strPreauthManagementDashboard, mapping, request);
		
		
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPreauthManagementDashboard));
		}// end of catch(Exception exp)
	}// end of doDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward doDisplayManaDashborddoforword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
		
			log.debug("Inside PreAuthDashboardAction doDisplayManaDashbord");
			request.setAttribute("focusObj","searchTypeId");
			Long  userSeqId = TTKCommon.getUserSeqId(request);
			DynaActionForm frmDashboardList= (DynaActionForm)form;  
			frmDashboardList.initialize(mapping);
			PreAuthDashboardManager preAuthDashboardManagerobject= this.getPreAuthDashboardManagerObject();
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			String strChild = TTKCommon.checkNull(request.getParameter("child"));			
			PreDashboardVO  preDashboardVO = preAuthDashboardManagerobject.getManagementDetails(userSeqId);
			frmDashboardList.set("coding_cases",preDashboardVO.getCoding_cases());
			frmDashboardList.set("highValuePA",preDashboardVO.getHighValuePA());
			frmDashboardList.set("shortfall_cases", preDashboardVO.getShortfall_cases());
			frmDashboardList.set("shortfall_respond_ases", preDashboardVO.getShortfall_respond_ases());
			frmDashboardList.set("enhanced_shortfall_cases", preDashboardVO.getEnhanced_shortfall_cases());
			frmDashboardList.set("enhanced_shrtfal_res_cases", preDashboardVO.getEnhanced_shrtfal_res_cases());
			
			frmDashboardList.set("approved_cases", preDashboardVO.getApproved_cases());
			frmDashboardList.set("rejected_cases", preDashboardVO.getRejected_cases());
			frmDashboardList.set("forResolution", preDashboardVO.getForResolution());
			frmDashboardList.set("enhance_inp_cases",preDashboardVO.getEnhance_case());
			frmDashboardList.set("appeal_inp_cases",preDashboardVO.getAppeal_case());
			frmDashboardList.set("fress_case",preDashboardVO.getFresh_case());
			frmDashboardList.set("searchType","EID");
			String searchtypename = request.getParameter("searchType");
			request.getSession().setAttribute("search_type_name", searchtypename);
			frmDashboardList.set("search_type_name",searchtypename);
			Integer searchtypeount =0;
			if(request.getParameter("search_type_count") !=null){
				searchtypeount = Integer.parseInt(request.getParameter("search_type_count"));
			}else if(request.getSession().getAttribute("search_type_count") !=null){
				searchtypeount = (Integer)(request.getSession().getAttribute("search_type_count"));
			}
			frmDashboardList.set("search_type_count",request.getSession().getAttribute("search_type_count"));
			/*request.setAttribute("preDashboardVO", preDashboardVO);*/
			TableData tDUserInfo = TTKCommon.getTableData(request,"tDUserInfo");
			tDUserInfo.createTableInfo("DataEntryUserInfoTable",new ArrayList());
			tDUserInfo.getSearchData().add(userSeqId);
			ArrayList al =preAuthDashboardManagerobject.getDataEntryUserInfo(tDUserInfo.getSearchData());
			
			tDUserInfo.setData(al, "search");
			request.getSession().setAttribute("tDUserInfo",tDUserInfo); 
			TableData tdUserActiveInfo = TTKCommon.getTableData(request,"tDSelfAssignment");
			if(strChild.equals("") || strChild.equals("tDSelfAssignment")){
				if(!strPageID.equals("") || !strSortID.equals(""))
				{
					if(!strPageID.equals(""))
					{
						
						tdUserActiveInfo.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
						return mapping.findForward(strPreauthManagementDashboard);
					}
					else
					{
						tdUserActiveInfo.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
						tdUserActiveInfo.modifySearchData("sort");//modify the search data
					}
				}
				else{
					tdUserActiveInfo.createTableInfo("DataEntryActiveUsers",new ArrayList());
				}
					
			
			tdUserActiveInfo.getSearchData().add(userSeqId);
			ArrayList alSelfAssignment = preAuthDashboardManagerobject.getDataEntrySelfAssinmentCase(tdUserActiveInfo.getSearchData());
			
			tdUserActiveInfo.setData(alSelfAssignment, "search");
			request.getSession().setAttribute("tDSelfAssignment",tdUserActiveInfo); 
			}
			TableData tableData = TTKCommon.getTableData(request);
					
			tableData.modifySearchData(strForward);//modify the search data
			
			
			ArrayList<PreAuthVO> alDataEntryActiveUserDetails1 = preAuthDashboardManagerobject.getDataEntryActiveUsers(tableData.getSearchData());	
		
			frmDashboardList.set("search_type_count",searchtypeount);
			tableData.setData(alDataEntryActiveUserDetails1,strForward);
			request.getSession().setAttribute("tableData",tableData);
			request.getSession().setAttribute("search_type_count", searchtypeount);
			
			
			return this.getForward(strPreauthManagementDashboard, mapping, request);
		
		
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPreauthManagementDashboard));
		}// end of catch(Exception exp)
	}// end of doDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
	private void addToWebBoard(PreAuthVO preAuthVO, HttpServletRequest request,String strIdentifier)throws TTKException
	{
		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
		CacheObject cacheObject = new CacheObject();
		cacheObject.setCacheId(this.prepareWebBoardId(preAuthVO,strIdentifier)); //set the cacheID
		cacheObject.setCacheDesc(preAuthVO.getPreAuthNo());
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
	
	private String prepareWebBoardId(PreAuthVO preAuthVO,String strIdentifier)throws TTKException
	{
		StringBuffer sbfCacheId=new StringBuffer();
		sbfCacheId.append(preAuthVO.getPreAuthSeqID()!=null? String.valueOf(preAuthVO.getPreAuthSeqID()):" ");
		sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getEnrollmentID()).equals("")? " ":preAuthVO.getEnrollmentID());
		sbfCacheId.append("~#~").append(preAuthVO.getEnrollDtlSeqID()!=null? String.valueOf(preAuthVO.getEnrollDtlSeqID()):" ");
		sbfCacheId.append("~#~").append(preAuthVO.getPolicySeqID()!=null? String.valueOf(preAuthVO.getPolicySeqID()):" ");
		sbfCacheId.append("~#~").append(preAuthVO.getMemberSeqID()!=null? String.valueOf(preAuthVO.getMemberSeqID()):" ");
		if(strIdentifier.equals(strRegular))// to check it is a regular pre auth
		{
			sbfCacheId.append("~#~").append(strRegularPreAuth);//if it is a reular preauth then append with string identifier
		}//end of if(strIdentifier.equals(strRegular))
		else if(strIdentifier.equals(strEnhanced))//to check it is a enhanced preauth
		{
			if(preAuthVO.getEnhanceIconYN().equals("Y"))// to check whethere there is enhanced icon
			{
				sbfCacheId.append("~#~").append(strEnhancedPreAuth);
			}//end of if(preAuthVO.getEnhanceIconYN().equals("Y"))
			else
			{
				sbfCacheId.append("~#~").append(strRegularPreAuth);
			}//end of else
		}// end of else if(strIdentifier.equals(strEnhanced))
		sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getClaimantName()).equals("")? " ":preAuthVO.getClaimantName());
		sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getBufferAllowedYN()).equals("")? " ":preAuthVO.getBufferAllowedYN());
		sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getShowBandYN()).equals("")? " ":preAuthVO.getShowBandYN());
		sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getCoding_review_yn()).equals("")? " ":preAuthVO.getCoding_review_yn());
		return sbfCacheId.toString();
	}//end of prepareWebBoardId(PreAuthVO preAuthVO,String strIdentifier)throws TTKException
	

	
	private PreAuthDashboardManager getPreAuthDashboardManagerObject() throws TTKException
	{
		PreAuthDashboardManager preAuthDashboardManager = null;
		try
		{
			if(preAuthDashboardManager == null)
			{
				InitialContext ctx = new InitialContext();
				preAuthDashboardManager = (PreAuthDashboardManager) ctx.lookup("java:global/TTKServices/business.ejb3/PreAuthDashboardManagerBean!com.ttk.business.preauth.PreAuthDashboardManager");

			}//end of if(PreAuthDashboardManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp,strPreDashboardError );
		}//end of catch
		return preAuthDashboardManager;
	}//end getPreAuthSupportManagerObject()
	
	
	
	
}//End Class
