
package com.ttk.action.administration;

import java.util.ArrayList;
import java.util.Arrays;

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
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.common.PreAuthWebBoardHelper;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.BufferDetailVO;
import com.ttk.dto.administration.BufferVO;
import com.ttk.dto.enrollment.PolicyVO;

import formdef.plugin.util.FormUtils;


public class ModifiedMembersAction extends TTKAction {
	
	private static Logger log = Logger.getLogger( ModifiedMembersAction.class );
	
	//Modes
	private static final String strBackward="Backward";
	private static final String strForward="Forward";
	
	//Forwards
	private static final String strModifiedMemberList="modifiedMemberList";
	private static final String strProductExp="product";
	
	public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
													 HttpServletResponse response) throws Exception{
		try{
				log.debug("Inside the doDefault method of ModifiedMembersAction");
				setLinks(request);
			
				if(PreAuthWebBoardHelper.checkWebBoardId(request)==null)
				{
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.policy.required");
					throw expTTK;
				}//end of if(WebBoardHelper.checkWebBoardId(request)==null)
				
			ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
			//get the table data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
			ArrayList alModifiedMemberList= new ArrayList();
			
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
					return (mapping.findForward(strModifiedMemberList));
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else
			{
				//create the required grid table
				tableData.createTableInfo("ModifiedMembersTable",null);
				tableData.setSearchData(this.populateSearchCriteria(request));
				tableData.modifySearchData("search");
			}
			alModifiedMemberList=productpolicyObject.getModifiedMembersList(tableData.getSearchData());
			
			tableData.setData(alModifiedMemberList,"search");
			request.getSession().setAttribute("tableData",tableData);
		//	request.getSession().setAttribute("frmModifiedMembers",frmModifiedMembers);
			return this.getForward(strModifiedMemberList, mapping, request);
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
	
	
	public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws Exception{
		try{
				log.debug("Inside the doBackward method of ModifiedMembersAction");
				setLinks(request);
				ArrayList alModifiedMemberList= new ArrayList();
				ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
				TableData tableData =TTKCommon.getTableData(request);
				tableData.modifySearchData(strBackward);
				
				alModifiedMemberList=productpolicyObject.getModifiedMembersList(tableData.getSearchData());
				tableData.setData(alModifiedMemberList,"search");
				request.getSession().setAttribute("tableData",tableData);
				return this.getForward(strModifiedMemberList, mapping, request);
				
			
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

	
	public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			 HttpServletResponse response) throws Exception
	{
		try{
				log.debug("Inside the doForward method of ModifiedMembersAction");
				setLinks(request);
		
				ArrayList alModifiedMemberList= new ArrayList();
				ProductPolicyManager productpolicyObject=this.getProductPolicyManager();
				TableData tableData =TTKCommon.getTableData(request);
				tableData.modifySearchData(strForward);
				alModifiedMemberList=productpolicyObject.getModifiedMembersList(tableData.getSearchData());
				tableData.setData(alModifiedMemberList,"search");
				request.getSession().setAttribute("tableData",tableData);
				return this.getForward(strModifiedMemberList, mapping, request);
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
	
	
	public ActionForward doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
															HttpServletResponse response) throws Exception
	{
		
		log.debug("Inside the doChangeWebBoard method of ModifiedMembersAction");
		return doDefault(mapping,form,request,response);
	}
	
	
	private ArrayList<Object> populateSearchCriteria(HttpServletRequest request)throws TTKException
	{
		ArrayList<Object> alParameters=new ArrayList<Object>();
		String policySeqID="";
		DynaActionForm frmPolicies = (DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
		if(frmPolicies != null)
		{
			policySeqID =  frmPolicies.getString("policySeqID");
			policySeqID = (policySeqID == null || policySeqID.length() < 1) ? "0" : policySeqID; 
		}
		else
		{
			policySeqID="0";
		}
		alParameters.add(new Long(policySeqID));
	//	alParameters.add(TTKCommon.getUserSeqId(request));
		return alParameters;
	}//end of populateSearchCriteria(DynaActionForm frmPolicyList,HttpServletRequest request)throws TTKException
	
	
	/**
	 * Returns the ProductPolicyManager session object for invoking methods on it.
	 * @return productPolicyManager session object which can be used for method invokation
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
			throw new TTKException(exp, strProductExp);
		}//end of catch
		return productPolicyManager;
	}//end getProductPolicyManager()
	

}//end of class ModifiedMembersAction

