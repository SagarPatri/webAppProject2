/**
 * @ (#) ProductSearchAction.java Feb 27, 2006
 * Project      : Vidal Health TPA Services
 * File         : AdminPoliciesAction.java
 * Author       : Pradeep R
 * Company      : Span Systems Corporation
 * Date Created : Feb 27, 2006
 *
 * @author       :  Pradeep R
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.administration;

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
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.enrollment.PolicyDetailVO;
import com.ttk.dto.enrollment.PolicyVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

import com.ttk.dto.administration.RuleSynchronizationVO;
/**
 * This class is used for searching of List of Policies.
 * This class also provides option for Saving the details.
 */
public class AdminPoliciesAction extends TTKAction {

	private static Logger log = Logger.getLogger( AdminPoliciesAction.class );
	//Modes
	private static final String strBackward="Backward";
	private static final String strForward="Forward";

	//Action Mappings
	private static final String strPolicyList="policylist";
	private static final String strPolicyDetail="policydetail";
	private static final String strPolicyClauses="policyClauses";
	private static final String strPolicyCoverage="policycoverage";
	private static final String strConfig="policyconfiguration";
	private static final String strGenerateXLReport="generateXLReport";
	private static final String strRownum="rownum";
	private static final String strSync="policysync";
	private static final String strTableData = "tableDataSync";

	//Exception Message Identifier
	private static final String strProductExp="product";
	private static final String strPolicySynclist="policysynchronizeList";
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
	public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
													 HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doDefault method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPolicies =(DynaActionForm)form;
			String strDefaultBranchID  = String.valueOf(((UserSecurityProfile)request.getSession().
											   getAttribute("UserSecurityProfile")).getBranchID());
			TableData tableData =TTKCommon.getTableData(request);//get the table data from session if exists
			tableData = new TableData();//create new table data object
			//create the required grid table
			tableData.createTableInfo("PoliciesTable",new ArrayList());
			request.getSession().setAttribute("tableData",tableData);
			//make query to get user group to load to combo box
			//reset the form data
			((DynaActionForm)form).initialize(mapping);
			((DynaActionForm)form).set("sEnrollmentType","COR");
			frmPolicies.set("sTTKBranch",strDefaultBranchID);
			request.getSession().setAttribute("frmPoliciesSearch",frmPolicies);
			return this.getForward(strPolicyList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to search the data with the given search criteria.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
													HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doSearch method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPolicies =(DynaActionForm)form;
			//get the session bean from the bean pool for each excecuting threadl
			ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
			//get the table data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
			ArrayList alPolicyList=null;
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(strPageID.equals(""))
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");//modify the search data
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return (mapping.findForward(strPolicyList));
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else
			{
				//create the required grid table
				tableData.createTableInfo("PoliciesTable",null);
				tableData.setSearchData(this.populateSearchCriteria(frmPolicies,request));
				tableData.modifySearchData("search");
			}//end of else
			//call the DAO to get the records
			alPolicyList = productpolicyObject.getPolicyList(tableData.getSearchData());
			tableData.setData(alPolicyList,"search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			//finally return to the grid screen
			request.getSession().setAttribute("frmPoliciesSearch",frmPolicies);
			return this.getForward(strPolicyList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to get the previous set of records with the given search criteria.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
													  HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doBackward method of AdminPoliciesAction");
			setLinks(request);
			//get the session bean from the bean pool for each excecuting threadl
			ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
			TableData tableData = TTKCommon.getTableData(request);
			//modify the search data
			tableData.modifySearchData(strBackward);
			ArrayList alPolicyList = productpolicyObject.getPolicyList(tableData.getSearchData());
			tableData.setData(alPolicyList, strBackward);
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);

			return (mapping.findForward(strPolicyList));
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)

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
	public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
													 HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doForward method of AdminPoliciesAction");
			setLinks(request);
			//get the session bean from the bean pool for each excecuting threadl
			ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
			TableData tableData = TTKCommon.getTableData(request);
			//modify the search data
			tableData.modifySearchData(strForward);
			ArrayList alPolicyList = productpolicyObject.getPolicyList(tableData.getSearchData());
			//set the table data object to session
			tableData.setData(alPolicyList, strForward);
			//finally return to the grid screen
			request.getSession().setAttribute("tableData",tableData);
			return (mapping.findForward(strPolicyList));
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to copy the selected records to web-board.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doCopyToWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
														    HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doCopyToWebBoard method of AdminPoliciesAction");
			setLinks(request);
			this.populateWebBoard(request);
			return this.getForward(strPolicyList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of CopyToWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
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
	public ActionForward doViewPolicies(ActionMapping mapping,ActionForm form,HttpServletRequest request,
														  HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doViewPolicies method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPolicies =(DynaActionForm)form;
			//get the session bean from the bean pool for each excecuting threadl
			ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
			TableData tableData =TTKCommon.getTableData(request);
			String strRowNum=request.getParameter("rownum");
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				//reset the form data
				((DynaActionForm)form).initialize(mapping);
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			///edit flow
			if(!(TTKCommon.checkNull(strRowNum).equals("")))
			{
				int iRowNum = TTKCommon.getInt(strRowNum);
				PolicyVO policyVO = (PolicyVO)tableData.getRowInfo(iRowNum);
				policyVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
				PolicyDetailVO policyDetailVO=productpolicyObject.getPolicyDetail(policyVO.getProdPolicySeqID(),
																			   TTKCommon.getUserSeqId(request));
				this.addToWebBoard(policyVO, request);
				//make query to get user group to load to combo box
				ArrayList alUserGroup=new ArrayList();
				ArrayList alBrokerGroup = new ArrayList();
				if(policyDetailVO.getOfficeSeqID()!=null)
				{
					alUserGroup=productpolicyObject.getGroup(policyDetailVO.getOfficeSeqID());
					alBrokerGroup = productpolicyObject.getBrokerGroup(policyDetailVO.getOfficeSeqID());
				}//end of if(policyDetailVO.getOfficeSeqID()!=null)
				ArrayList listtreatiesPlan=null;
				if(policyDetailVO.getReinsurerId()!=null && policyDetailVO.getReinsurerId()!=""){
					listtreatiesPlan =	productpolicyObject.getReInsurancePlans(policyDetailVO.getReinsurerId());
				}
				request.getSession().setAttribute("alUserGroup",alUserGroup);
				request.getSession().setAttribute("alBrokerGroup",alBrokerGroup);
				request.getSession().setAttribute("stoppreauth", policyDetailVO.getStopCashlessDate());
				request.getSession().setAttribute("stopclaim", policyDetailVO.getStopClaimsDate());
				this.documentViewer(request,policyDetailVO);
				frmPolicies = (DynaActionForm)FormUtils.setFormValues("frmPoliciesEdit",policyDetailVO,this,
																						   mapping,request);
				//isBufferAllowedSaved used to keep the Buffer Allowed checkbox readonly
				//if it is checked and saved before.
				// added as per KOC 1216B Change request
				frmPolicies.set("memberBuffer",policyDetailVO.getMemberBufferYN());
				// added as per KOC 1216B Change request
				
				frmPolicies.set("isBufferAllowedSaved",policyDetailVO.getBufferRecYN());
				frmPolicies.set("isBrokerAllowedSaved",policyDetailVO.getBrokerRecYN());
				frmPolicies.set("stopPreAuth",policyDetailVO.getStopPreAuthsYN());
				frmPolicies.set("stopClaim",policyDetailVO.getStopClaimsYN());
				frmPolicies.set("opdClaim",policyDetailVO.getopdClaimsYN());//KOC 1286 for OPD Benefit
				frmPolicies.set("cashBenefit",policyDetailVO.getCashBenefitYN());//KOC 1270 for hospital cash benefit
				frmPolicies.set("convCashBenefit",policyDetailVO.getConvCashBenefitYN());//KOC 1270 for hospital cash benefit
				//added for KOC-1273
				frmPolicies.set("criticalBenefit",policyDetailVO.getCriticalBenefitYN());
				frmPolicies.set("mailNotificationhiddenYN", policyDetailVO.getMailNotificationhiddenYN());
				frmPolicies.set("survivalPeriod",policyDetailVO.getSurvivalPeriodYN());
                frmPolicies.set("listtreatiesPlan", listtreatiesPlan);
				request.getSession().setAttribute("reinsurerid", policyDetailVO.getReinsurerId());
				request.getSession().setAttribute("productNetworkSortNo",policyDetailVO.getNetworkSortNum());
				request.getSession().setAttribute("policySeqID",policyDetailVO.getPolicySeqID());
			}//end of if(!(TTKCommon.checkNull(strRowNum).equals("")))
			//on click of of general tab
			else if(TTKCommon.getWebBoardId(request) != null)
			{
				Long lngProductPolicySeqId=TTKCommon.getWebBoardId(request);
				//get the Policy details from the Dao object
				PolicyDetailVO policyDetailVO=productpolicyObject.getPolicyDetail(lngProductPolicySeqId,
																	   TTKCommon.getUserSeqId(request));
				ArrayList alUserGroup=new ArrayList();
				ArrayList alBrokerGroup = new ArrayList();
				//make query to get user group to load to combo box
				if(policyDetailVO.getOfficeSeqID()!=null)
				{
					alUserGroup=productpolicyObject.getGroup(policyDetailVO.getOfficeSeqID());
					alBrokerGroup = productpolicyObject.getBrokerGroup(policyDetailVO.getOfficeSeqID());

				}//end of  if(policyDetailVO.getOfficeSeqID()!=null)
				request.getSession().setAttribute("alUserGroup",alUserGroup);
				request.getSession().setAttribute("alBrokerGroup",alBrokerGroup);

				frmPolicies = (DynaActionForm)FormUtils.setFormValues("frmPoliciesEdit",policyDetailVO,
																			     this,mapping,request);
				//isBufferAllowedSaved used to keep the Buffer Allowed checkbox readonly
				//if it is checked and saved before.
				
				// added as per KOC 1216B Change request
				frmPolicies.set("memberBuffer",policyDetailVO.getMemberBufferYN());
				// added as per KOC 1216B Change request
				
				frmPolicies.set("isBufferAllowedSaved",policyDetailVO.getBufferRecYN());
				frmPolicies.set("isBrokerAllowedSaved",policyDetailVO.getBrokerRecYN());
				frmPolicies.set("stopPreAuth",policyDetailVO.getStopPreAuthsYN());
				frmPolicies.set("stopClaim",policyDetailVO.getStopClaimsYN());
				frmPolicies.set("opdClaim",policyDetailVO.getopdClaimsYN());//KOC 1286 for OPD Benefit
				frmPolicies.set("cashBenefit",policyDetailVO.getCashBenefitYN());// KOc 1270 for Hospital cash benefit
				frmPolicies.set("convCashBenefit",policyDetailVO.getConvCashBenefitYN());// KOc 1270 for Hospital cash benefit
				//added for KOC-1273
				frmPolicies.set("criticalBenefit",policyDetailVO.getCriticalBenefitYN());
				frmPolicies.set("survivalPeriod",policyDetailVO.getSurvivalPeriodYN());
				request.getSession().setAttribute("productNetworkSortNo",policyDetailVO.getNetworkSortNum());
				request.getSession().setAttribute("frmPoliciesEdit",frmPolicies);
				this.documentViewer(request,policyDetailVO);
			}//end of if(TTKCommon.getWebBoardId(request) != null)
			else
			{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.policy.required");
				throw expTTK;
			}//end of else
			request.getSession().setAttribute("frmPoliciesEdit",frmPolicies);
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doViewPolicies(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)

	/**
	 * This method is used to navigate to clause screen to view selected record.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doViewPolicyClause(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try
		{
			log.debug("Inside the doViewPolicyClause method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPoliciesSearch =(DynaActionForm)form;
			frmPoliciesSearch.set("identifier", request.getParameter("identifier"));
			request.getSession().setAttribute("frmPoliciesSearch",frmPoliciesSearch);
			return mapping.findForward(strPolicyClauses);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doViewPolicyClause(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to navigate to clause screen to view selected record.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doViewPolicyCoverage(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try
		{
			log.debug("Inside the doViewPolicyCoverage method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPoliciesSearch =(DynaActionForm)form;
			frmPoliciesSearch.set("identifier", request.getParameter("identifier"));
			request.getSession().setAttribute("frmPoliciesSearch",frmPoliciesSearch);
			return mapping.findForward(strPolicyCoverage);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doViewPolicyCoverage(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
			log.debug("Inside the doSave method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPolicies =(DynaActionForm)form;
			//get the session bean from the bean pool for each excecuting threadl
			ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
			PolicyDetailVO policyDetailVO =new PolicyDetailVO();
			policyDetailVO= (PolicyDetailVO)FormUtils.getFormValues(frmPolicies,this,mapping,request);
			//if Buffer Amount is not allowed then clear other Buffer Related Details
			if(!policyDetailVO.getBufferAllowedYN().equals("Y"))
			{
				policyDetailVO.setAdmnAuthorityTypeID("");
				policyDetailVO.setAllocatedTypeID("");
				policyDetailVO.setMemberBufferYN("N");//Changes as per 1216 B Change request				
				

			}//end of if(!policyDetailVO.getBufferAllowedYN().equals("Y"))
			
			if(!policyDetailVO.getBrokerYN().equals("Y"))
			{
				policyDetailVO.setBrokerYN("N");//Changes as per Broker Login				
				

			}
			
			if(!policyDetailVO.getBrokerYN().equals("Y"))
			{
				policyDetailVO.setBrokerID(new Long(0));
			
			
			}

			//if conditions for stop pre-auth and claim
			if("Y".equals(TTKCommon.checkNull(frmPolicies.get("stopPreAuth"))))
			{
				policyDetailVO.setStopPreAuthsYN("Y");
			}
			else
			{
				policyDetailVO.setStopPreAuthsYN("N");
			}
			if("Y".equals(TTKCommon.checkNull(frmPolicies.get("stopClaim"))))
			{
				policyDetailVO.setStopClaimsYN("Y");
			}
			else
			{
				policyDetailVO.setStopClaimsYN("N");
			}
			
			//KOC 1286 for OPD Benefit
			
			if("Y".equals(TTKCommon.checkNull(frmPolicies.get("opdClaim"))))
			{
				
				policyDetailVO.setopdClaimsYN("Y");
			}
			else
			{
				policyDetailVO.setopdClaimsYN("N");
			}
			//KOC 1286 for OPD Benefit
			
			// KOC 1270 for hospital cash benefit
			
			if("Y".equals(TTKCommon.checkNull(frmPolicies.get("cashBenefit"))))
			{
				
				policyDetailVO.setCashBenefitYN("Y");
			}
			else
			{
				
				policyDetailVO.setCashBenefitYN("N");
			}
			
			if("Y".equals(TTKCommon.checkNull(frmPolicies.get("convCashBenefit"))))
			{
				
				policyDetailVO.setConvCashBenefitYN("Y");
			}
			else
			{
				
				policyDetailVO.setConvCashBenefitYN("N");
			}
			
			if("Y".equals(TTKCommon.checkNull(request.getParameter("mailNotificationhiddenYN"))))
			{
				
				policyDetailVO.setMailNotificationhiddenYN("Y");
			}
			else
			{
				
				policyDetailVO.setMailNotificationhiddenYN("N");
			}
			
			// KOC 1270 for hospital cash benefit
			 //Changes as per KOC 1216B Change requesst 
			
log.info("herer   frmPolicies "+(TTKCommon.checkNull((String)frmPolicies.get("memberBuffer"))));
			if("Y".equals(TTKCommon.checkNull((String)frmPolicies.get("memberBuffer"))))
			{
				policyDetailVO.setMemberBufferYN("Y");
				
			}
			else
			{
				policyDetailVO.setMemberBufferYN("N");
			}
			//added for KOC-1273
			if("Y".equals(TTKCommon.checkNull((String)frmPolicies.get("criticalBenefit"))))
			{
				policyDetailVO.setCriticalBenefitYN("Y");
				
			}
			else
			{
				policyDetailVO.setCriticalBenefitYN("N");
			}
			if("Y".equals(TTKCommon.checkNull((String)frmPolicies.get("survivalPeriod"))))
			{
				policyDetailVO.setSurvivalPeriodYN("Y");
			}
			else
			{
				policyDetailVO.setSurvivalPeriodYN("N");
			}
			//ended
			//Changes as per KOC 1216B Change requesst 
			
			policyDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
			///call the DAO to save the policy details
			int iCount=productpolicyObject.savePolicy(policyDetailVO);
			if(iCount>0)
			{
				//get the Policy details from the Dao object
				policyDetailVO=productpolicyObject.getPolicyDetail(TTKCommon.getWebBoardId(request),
																   TTKCommon.getUserSeqId(request));
				ArrayList alUserGroup=new ArrayList();
				//make query to get user group to load to combo box
				if(policyDetailVO.getOfficeSeqID()!=null)
				{
					alUserGroup=productpolicyObject.getGroup(policyDetailVO.getOfficeSeqID());
				}//end of  if(policyDetailVO.getOfficeSeqID()!=null)
				request.getSession().setAttribute("alUserGroup",alUserGroup);
				frmPolicies = (DynaActionForm)FormUtils.setFormValues("frmPoliciesEdit",policyDetailVO,
																				 this,mapping,request);
				//isBufferAllowedSaved used to keep the Buffer Allowed checkbox readonly
				//if it is checked and saved before.
				//1216B cR
				log.info("memberBuffer:::"+policyDetailVO.getMemberBufferYN());
				frmPolicies.set("memberBuffer",policyDetailVO.getMemberBufferYN());
				//1216B cR
				frmPolicies.set("isBufferAllowedSaved",policyDetailVO.getBufferRecYN());
				frmPolicies.set("isBrokerAllowedSaved",policyDetailVO.getBrokerRecYN());
				frmPolicies.set("stopPreAuth",policyDetailVO.getStopPreAuthsYN());
				frmPolicies.set("stopClaim",policyDetailVO.getStopClaimsYN());
				frmPolicies.set("opdClaim",policyDetailVO.getopdClaimsYN());//KOC 1286 for OPD Benefit
				frmPolicies.set("cashBenefit",policyDetailVO.getCashBenefitYN()); //KOC 1270 for hospital cash benefit
				frmPolicies.set("convCashBenefit",policyDetailVO.getConvCashBenefitYN()); //KOC 1270 for hospital cash benefit
				frmPolicies.set("criticalBenefit",policyDetailVO.getCriticalBenefitYN());
				frmPolicies.set("survivalPeriod",policyDetailVO.getSurvivalPeriodYN());
				frmPolicies.set("treatiesPlanId", policyDetailVO.getTreatiesPlanId());
				request.getSession().setAttribute("frmPoliciesEdit",frmPolicies);
				request.getSession().setAttribute("stoppreauth", policyDetailVO.getStopCashlessDate());
				request.getSession().setAttribute("stopclaim", policyDetailVO.getStopClaimsDate());
				this.documentViewer(request,policyDetailVO);
				request.setAttribute("updated","message.savedSuccessfully");
			}//end of if(iCount>0)
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
		try{
			log.debug("Inside the doChangeWebBoard method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPolicies =(DynaActionForm)form;
			ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
			//if web board id is found, set it as current web board id
			frmPolicies.initialize(mapping);
			Long lngProductPolicySeqId=TTKCommon.getWebBoardId(request);
			//get the Policy details from the Dao object
			PolicyDetailVO policyDetailVO=productpolicyObject.getPolicyDetail(lngProductPolicySeqId,
																   TTKCommon.getUserSeqId(request));
			ArrayList alUserGroup=new ArrayList();
			//make query to get user group to load to combo box
			if(policyDetailVO.getOfficeSeqID()!=null)
			{
				alUserGroup=productpolicyObject.getGroup(policyDetailVO.getOfficeSeqID());
			}//end of  if(policyDetailVO.getOfficeSeqID()!=null)
			request.getSession().setAttribute("alUserGroup",alUserGroup);
			frmPolicies = (DynaActionForm)FormUtils.setFormValues("frmPoliciesEdit",policyDetailVO,
																			 this,mapping,request);
			//isBufferAllowedSaved used to keep the Buffer Allowed checkbox readonly
			//if it is checked and saved before.

			//1216B cR
			frmPolicies.set("memberBuffer",policyDetailVO.getMemberBufferYN());
			//1216B cR
			frmPolicies.set("isBufferAllowedSaved",policyDetailVO.getBufferRecYN());
			frmPolicies.set("isBrokerAllowedSaved",policyDetailVO.getBrokerRecYN());
			frmPolicies.set("stopPreAuth",policyDetailVO.getStopPreAuthsYN());
			frmPolicies.set("stopClaim",policyDetailVO.getStopClaimsYN());
			frmPolicies.set("opdClaim",policyDetailVO.getopdClaimsYN());//KOC 1286 for OPD Benefit
			frmPolicies.set("cashBenefit",policyDetailVO.getCashBenefitYN());// KOC 1270 for hospital cash benefit
			frmPolicies.set("convCashBenefit",policyDetailVO.getConvCashBenefitYN());// KOC 1270 for hospital cash benefit
			frmPolicies.set("criticalBenefit",policyDetailVO.getCriticalBenefitYN());
			frmPolicies.set("survivalPeriod",policyDetailVO.getSurvivalPeriodYN());
			request.getSession().setAttribute("frmPoliciesEdit",frmPolicies);
			this.documentViewer(request,policyDetailVO);
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of ChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)

	/**
	 * This method is used to generate a xl sheet.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doGenerateXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,
														HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doGenerateXL method of AdminPoliciesAction");
			setLinks(request);
			DynaActionForm frmPolicies =(DynaActionForm)form;
			PolicyDetailVO policyDetailVO =new PolicyDetailVO();
			policyDetailVO= (PolicyDetailVO)FormUtils.getFormValues(frmPolicies,this,mapping,request);
			String strGenerateXL="/ReportsAction.do?mode=doGenerateAdminXL&parameter="+policyDetailVO.getPolicySeqID()
										  +"&fileName=generalreports/RenewalMembersXL.jrxml&reportID=GenRenMemXLs&reportType=EXL";
			request.setAttribute("GenerateXL",strGenerateXL);
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}//end of doGenerateXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)

	/**
	 * This method is used to bring out the Configuration List screen
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doConfiguration(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doConfiguration method of AdminPoliciesAction");
			setLinks(request);
			StringBuffer sbfCaption= new StringBuffer();
			DynaActionForm frmPoliciesEdit = (DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
			sbfCaption.append("[").append(frmPoliciesEdit.get("companyName")).append("]");
			sbfCaption.append("[").append(frmPoliciesEdit.get("policyNbr")).append("]");
			request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
			return this.getForward(strConfig, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPolicyList));
		}//end of catch(Exception exp)
	}//end of doConfiguration(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//	HttpServletResponse response)

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
		log.debug("Inside the doReset method of AdminPoliciesAction");
		return doViewPolicies(mapping,form,request,response);
	}//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method will add search criteria fields and values to the arraylist and will return it
	 * @param frmPolicyList current instance of form bean
	 * @param request HttpServletRequest object
	 * @return alSearchObjects ArrayList of search params
	 * @throws TTKException
	 */
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmPolicyList,
							HttpServletRequest request)
	{
		ArrayList<Object> alSearchParam=new ArrayList<Object>();
		String strSearchObjects[]={(TTKCommon.replaceSingleQots(frmPolicyList.getString("sPolicyNO"))),frmPolicyList.
			       getString("sEnrollmentType"),frmPolicyList.getString("sInsuranceCompany"),frmPolicyList.getString(
			             "sTTKBranch"),frmPolicyList.getString("sStatus"),(TTKCommon.replaceSingleQots(frmPolicyList.
			    	   getString("sGroupID"))),(TTKCommon.replaceSingleQots(frmPolicyList.getString("sGroupName")))};
		alSearchParam.add(strSearchObjects);
		alSearchParam.add(TTKCommon.getUserSeqId(request));
		return alSearchParam;
	}//end of populateSearchCriteria(DynaActionForm frmPolicyList,HttpServletRequest request)throws TTKException

	private ArrayList<Object> populateSearchCriteria1(DynaActionForm frmPolicySyncList,
			HttpServletRequest request)
{
		ArrayList<Object> alSearchParam=new ArrayList<Object>();
		String policySeqID =  frmPolicySyncList.getString("policySeqID");
		policySeqID = (policySeqID == null || policySeqID.length() < 1) ? "0" : policySeqID; 
		alSearchParam.add(new Long(policySeqID)); //0
		alSearchParam.add(frmPolicySyncList.getString("enrollmentId")); //1
return alSearchParam;
}
	/**
	 * Populates the web board in the session with the selected items in the grid
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 */
	private void populateWebBoard(HttpServletRequest request)
	{
		String[] strChk = request.getParameterValues("chkopt");
		TableData tableData = (TableData)request.getSession().getAttribute("tableData");
		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
		CacheObject cacheObject = null;
		if(strChk!=null&&strChk.length!=0)
		{
			//loop through to populate delete sequence id's and get the value from session for the matching
			//check box value
			for(int i=0; i<strChk.length;i++)
			{
				cacheObject = new CacheObject();
				cacheObject.setCacheId(""+((PolicyVO)tableData.getData().get(Integer.parseInt(strChk[i]))).
																					 getProdPolicySeqID());
				cacheObject.setCacheDesc(((PolicyVO)tableData.getData().get(Integer.parseInt(strChk[i]))).
																						  getPolicyNbr());
				alCacheObject.add(cacheObject);
			}//end of for(int i=0; i<strChk.length;i++)
		}//end of if(strChk!=null&&strChk.length!=0)
		if(toolbar != null)
		{
			toolbar.getWebBoard().addToWebBoardList(alCacheObject);
		}//end of if(toolbar != null)
	}//end of populateWebBoard(HttpServletRequest request)

	/**
	 * Adds the selected item to the web board and makes it as the selected item in the web board
	 * @param  productVO ProductVO object which contains the information of the products
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 */
	private void addToWebBoard(PolicyVO policyVO, HttpServletRequest request)
	{
		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		CacheObject cacheObject = new CacheObject();
		cacheObject.setCacheId(String.valueOf(policyVO.getProdPolicySeqID()));
		cacheObject.setCacheDesc(policyVO.getPolicyNbr());
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
		alCacheObject.add(cacheObject);
		//if the object(s) are added to the web board, set the current web board id
		//if(toolbar.getWebBoard().addToWebBoardList(alCacheObject))
		toolbar.getWebBoard().addToWebBoardList(alCacheObject);
		toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());
	}//end of addToWebBoard(HospitalVO hospitalVO, HttpServletRequest request)

	/**
	 * Returns the GroupRegistrationManager session object for invoking methods on it.
	 * @return GroupRegistrationManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private ProductPolicyManager getProductPolicyManager() throws TTKException
	{
		ProductPolicyManager productPolicyManager = null;
		try
		{
			if(productPolicyManager == null)
			{
				InitialContext ctx = new InitialContext();
				productPolicyManager = (ProductPolicyManager) ctx.lookup("java:global/TTKServices/business.ejb3/ProductPolicyManagerBean!com.ttk.business.administration.ProductPolicyManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strPolicyDetail);
		}//end of catch
		return productPolicyManager;
	}//end getProductPolicyManager()

	/**
	 * This method for document viewer information
	 * @param request HttpServletRequest object which contains hospital information.
	 * @param policyDetailVO PolicyDetailVO object which contains policy information.
	 * @exception throws TTKException
	 */
	private void documentViewer(HttpServletRequest request,PolicyDetailVO policyDetailVO)
	{
		//Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		ArrayList<String> alDocviewParams = new ArrayList<String>();
		alDocviewParams.add("leftlink=Enrollment");
		alDocviewParams.add("policy_number="+policyDetailVO.getPolicyNbr());
		alDocviewParams.add("dms_reference_number="+policyDetailVO.getDMSRefID());

		if(request.getSession().getAttribute("toolbar")!=null){
			((Toolbar)request.getSession().getAttribute("toolbar")).setDocViewParams(alDocviewParams);
		}//end of if(request.getSession().getAttribute("toolbar")!=null)
	}//end of documentViewer(HttpServletRequest request,PolicyDetailVO policyDetailVO)

	public ActionForward doGenerateXLReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception
	{
		try
		{
				log.debug("Inside the doGeneralXLReport method of AdminPoliciesAction");
				setLinks(request);
				DynaActionForm frmPoliciesEdit =(DynaActionForm)form;
				String policySeqID = (String)request.getParameter("policySeqID");
				frmPoliciesEdit.set("polSeqID", policySeqID);
				request.getSession().setAttribute("frmPoliciesEdit",frmPoliciesEdit);
				return this.getForward(strGenerateXLReport, mapping, request);
		 }
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}
	} // End of doGeneralXLReport()	


	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception
	{
		try
		{
				log.debug("Inside the doClose method of AdminPoliciesAction");
				setLinks(request);
				return this.getForward(strPolicyDetail, mapping, request);
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}
	} // End of doGeneralXLReport()	
	
	public ActionForward doViewPolicySynclist(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception
	{
		try
		{
				log.debug("Inside the doDefault method of doViewPolicySynclist");
				setLinks(request);
				TableData tableData =TTKCommon.getTableData(request);
				DynaActionForm frmPolicySyncList = (DynaActionForm)form;
				PolicyVO plicyVO = new PolicyVO();
				String strRowNum=request.getParameter("rownum");
			
				frmPolicySyncList.initialize(mapping);
				if(!(TTKCommon.checkNull(strRowNum).equals("")))
				{
					int iRowNum = TTKCommon.getInt(strRowNum);
					 plicyVO = (PolicyVO)tableData.getRowInfo(iRowNum);
				}
				
				tableData = new TableData();
				tableData.createTableInfo("AdminPolicySyncTable",new ArrayList());
			
				frmPolicySyncList.set("policySeqID",plicyVO.getPolicySeqID()==null ?"":plicyVO.getPolicySeqID().toString());
				request.getSession().setAttribute(strTableData,tableData);
				request.getSession().setAttribute("frmPolicySyncList",frmPolicySyncList);
				return this.getForward(strPolicySynclist, mapping, request);
				
		 }
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}
	} 
	
	public ActionForward doClosePolicySynk(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception
	{
		try
		{
				log.debug("Inside the doClosePolicySynk method of AdminPoliciesAction");
				setLinks(request);
				
				HttpSession session = request.getSession();	
				DynaActionForm frmPolicies = (DynaActionForm) session.getAttribute("frmPoliciesSearch");
				ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
				
				//get the table data from session if exists
				TableData tableData =TTKCommon.getTableData(request);
				ArrayList alPolicyList=null;
				
				//create the required grid table
				tableData.createTableInfo("PoliciesTable",null);
				tableData.setSearchData(this.populateSearchCriteria(frmPolicies,request));
				tableData.modifySearchData("search");
			
				//call the DAO to get the records
				alPolicyList = productpolicyObject.getPolicyList(tableData.getSearchData());
				tableData.setData(alPolicyList,"search");
				
				//set the table data object to session
				request.getSession().setAttribute("tableData",tableData);
				request.getSession().setAttribute("frmPoliciesSearch",frmPolicies);
				//finally return to the grid screen
				return this.getForward(strPolicyList, mapping, request);
				
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}
	} 
	
	public ActionForward doSearchPolicySynk(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception
	{
		try
		{
					log.debug("Inside the doSearchPolicySynk method of AdminPoliciesAction");
					setLinks(request);
					DynaActionForm frmPolicySyncList = (DynaActionForm)form;
					ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
					TableData tableData =TTKCommon.getTableData(request);
					ArrayList alPolicySynkList=null;
				
					String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
					String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
					//if the page number or sort id is clicked
					if(!strPageID.equals("") || !strSortID.equals(""))
					{
						if(strPageID.equals(""))
						{
							tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
							tableData.modifySearchData("sort");//modify the search data
						}///end of if(!strPageID.equals(""))
						else
						{
							tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
							return (mapping.findForward(strPolicySynclist));
						}//end of else
					}//end of if(!strPageID.equals("") || !strSortID.equals(""))
					else
					{
						//create the required grid table
						tableData.createTableInfo("AdminPolicySyncTable",null);
						tableData.setSearchData(this.populateSearchCriteria1((DynaActionForm)form,request));
						tableData.modifySearchData("search");
					}//end of else
				
					alPolicySynkList = productpolicyObject.getAdmPolicySynchList(tableData.getSearchData());
					tableData.setData(alPolicySynkList, "search");
				
					request.getSession().setAttribute(strTableData,tableData);
					request.getSession().setAttribute("frmPolicySyncList",frmPolicySyncList);
					return this.getForward(strPolicySynclist, mapping, request);
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}
	}

	public ActionForward doAddPolicySync(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception
	{
		try
		{
				log.debug("Inside the doAddPolicySync method of AdminPoliciesAction");
				DynaActionForm frmPolicySyncList = (DynaActionForm)form;
				String  policySeqID= (String)frmPolicySyncList.get("policySeqID");
				
				ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
				//get the session bean from thpolicySeqIDe bean pool for each excecuting thread
				TableData tableData = ProductSearchSyncAction.getTableData(request,strTableData);
				String[] strMemberSeqIDs = populateMeberSeqId(request,tableData);
				String strDenialsyn="";//denial process
				int iProdSync = productpolicyObject.policySynchronizeRule("|"+strMemberSeqIDs[0]+"|",new Long(policySeqID),TTKCommon.getUserSeqId(request));//denial process
				if(iProdSync > 0)
				{
					request.setAttribute("updated","message.SyncSuccessfully");
				}
				return this.getForward(strPolicySynclist, mapping, request);
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}
	} 
	
	private String[] populateMeberSeqId(HttpServletRequest request, TableData tableData)
	{ 
		String[] strChk = request.getParameterValues("chkopt");
		String[] strMemberSeqIds = {"",""};
		if(strChk!=null&&strChk.length!=0)
		{
			//loop through to populate delete sequence id's and get the value from session for the matching 
			//check box value
			for(int i=0; i<strChk.length;i++)
			{
				if(strChk[i]!=null)
				{    
					//extract the sequence id to be deleted from the value object
					if(i == 0)
					{
						strMemberSeqIds[0] = String.valueOf(((RuleSynchronizationVO)tableData.getData().get(Integer.parseInt(strChk[i]))).getMemberSeqId());
					}//end of if(i==0)
					else
					{
						strMemberSeqIds[0] = strMemberSeqIds[0] +"|"+ ((RuleSynchronizationVO)tableData.getData().get(Integer.parseInt(strChk[i]))).getMemberSeqId();
					}//end of else
				}//end of if(strChk[i]!=null)
			}//end of for(int i=0; i<strChk.length;i++) 
		}//end of if(strChk!=null&&strChk.length!=0) 
		return strMemberSeqIds;
	}
	
	public ActionForward doBackwardPolSynkList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception{
		try{
				log.debug("Inside the doBackwardPolSynkList method of AdminPoliciesAction");
				setLinks(request);
				//get the session bean from the bean pool for each excecuting threadl
				ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
				TableData tableData = TTKCommon.getTableData(request);
				//modify the search data
				tableData.modifySearchData(strBackward);
				ArrayList alPolicySynkList = productpolicyObject.getAdmPolicySynchList(tableData.getSearchData());
				tableData.setData(alPolicySynkList, strBackward);
				//set the table data object to session
				request.getSession().setAttribute(strTableData,tableData);
				return this.getForward(strPolicySynclist, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
		}//end of catch(Exception exp)
	}

	
	public ActionForward doForwardPolSynkList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception
	{
		try{
				log.debug("Inside the doForwardPolSynkList method of AdminPoliciesAction");
				setLinks(request);
				//get the session bean from the bean pool for each excecuting threadl
				ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
				TableData tableData = TTKCommon.getTableData(request);
				//modify the search data
				tableData.modifySearchData(strForward);
				ArrayList alPolicySynkList = productpolicyObject.getAdmPolicySynchList(tableData.getSearchData());
				//set the table data object to session
				tableData.setData(alPolicySynkList, strForward);
				//finally return to the grid screen
				request.getSession().setAttribute(strTableData,tableData);
				return this.getForward(strPolicySynclist, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductExp));
			}//end of catch(Exception exp)
	}
	
   public ActionForward doChangeTrity(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
        try{
            log.debug("Inside the doBackward method of doStatusChange");
         DynaActionForm policyform =    (DynaActionForm) form;
            request.getSession().setAttribute("reinsurerid", policyform.get("reinsurerId"));
            return this.getForward(strPolicyDetail, mapping, request);   //finally return to the grid screen
           }//end of try
         catch(TTKException expTTK)
        {
          return this.processExceptions(request, mapping, expTTK);
            }//end of catch(TTKException expTTK)
         catch(Exception exp)
         {
          return this.processExceptions(request, mapping, new TTKException(exp,strPolicyDetail));
         }//end of catch(Exception exp)
        }
}//end of class AdminPoliciesAction
