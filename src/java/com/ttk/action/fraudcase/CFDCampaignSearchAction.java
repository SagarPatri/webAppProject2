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

import formdef.plugin.util.FormUtils;

public class CFDCampaignSearchAction extends TTKAction
{

	private static Logger log = Logger.getLogger(CFDCampaignSearchAction.class);
	private static final String strCFDCampaignSearch="campaignSearch";
	
	private static final String strCFDCampaignList ="CampaignList";
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	 private static final String strCampaignBatchDetails="CampaignBatchDetails";
	
	
	public ActionForward doDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 
	{
		try {
			log.debug("Inside the doDefault method of CFDCampaignSearchAction");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) 
			{
				((DynaActionForm) form).initialize(mapping);
															
			}
				
			DynaActionForm frmFraudCampaign = (DynaActionForm) form;
			// create new table data object
			tableData = new TableData();
			// create the required grid table
			tableData.createTableInfo("CFDCampaignSearchTable", new ArrayList());
			request.getSession().setAttribute("tableData", tableData);
			((DynaActionForm) form).initialize(mapping);
			return this.getForward(strCFDCampaignList, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignSearch));
		}// end of catch(Exception exp)
	}
	
	
	public ActionForward doSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doSearch method of CFDCampaignSearchAction");
			setLinks(request);
			
			ClaimManager claimManagerObject = this.getClaimManagerObject();
			TableData tableData = TTKCommon.getTableData(request);
			
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) {
				((DynaActionForm) form).initialize(mapping);
			}
				
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			String campaignStatus = TTKCommon.checkNull(request.getParameter("campaignStatus"));
			

			// if the page number or sort id is clicked
			if (!strPageID.equals("") || !strSortID.equals("")) 
			{
				if (!strPageID.equals("")) {
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return mapping.findForward(strCFDCampaignList);
				}// /end of if(!strPageID.equals(""))
				else {
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");// modify the search data
				}// end of else
			}
			else
			{
				// create the required grid table
				tableData.createTableInfo("CFDCampaignSearchTable", null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm) form, request));
				tableData.modifySearchData("search");
			}// end of else
			ArrayList alClaimsList = claimManagerObject.getCFDCompaginList(tableData.getSearchData());
			
			tableData.setData(alClaimsList, "search");
			if("OPEN".equals(campaignStatus))
			{	
				((Column)((ArrayList)tableData.getTitle()).get(3)).setVisibility(false);
				((Column)((ArrayList)tableData.getTitle()).get(4)).setVisibility(false);
				((Column)((ArrayList)tableData.getTitle()).get(5)).setVisibility(false);
			}
			if("CLOSED".equals(campaignStatus))
			{	
				((Column)((ArrayList)tableData.getTitle()).get(3)).setVisibility(true);
				((Column)((ArrayList)tableData.getTitle()).get(4)).setVisibility(true);
				((Column)((ArrayList)tableData.getTitle()).get(5)).setVisibility(true);
			}
			
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strCFDCampaignList, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignList));
		}
	}// end of doSearch()
	
		private ArrayList<Object> populateSearchCriteria(DynaActionForm frmFraudCampaign,
			HttpServletRequest request) {
			ArrayList<Object> alSearchParams = new ArrayList<Object>();
	    	alSearchParams.add(frmFraudCampaign.getString("campaignName"));//0 campaign_name 
	    	alSearchParams.add(frmFraudCampaign.getString("cfdProviderSeqId"));//1 hosp_seq_id 
	    	alSearchParams.add(frmFraudCampaign.getString("campaignReceivedFrom"));//2 recieved_from
	    	alSearchParams.add(frmFraudCampaign.getString("campaignReceivedTo"));//3 recieved_to
	    	alSearchParams.add(frmFraudCampaign.getString("campaignStatus"));//4 campaign_status
		return alSearchParams;
	}

		
	
	
	public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
					HttpServletResponse response) throws Exception
	{
			try
			{
				log.debug("Inside the doForward method of CFDCampaignSearchAction");
				setLinks(request);
				ClaimManager claimManagerObject = this.getClaimManagerObject();
				TableData tableData = TTKCommon.getTableData(request);
				tableData.modifySearchData(strForward);
				ArrayList alClaimsList = claimManagerObject.getCFDCompaginList(tableData.getSearchData());
				tableData.setData(alClaimsList, strForward);
				request.getSession().setAttribute("tableData",tableData);
				return this.getForward(strCFDCampaignList, mapping, request);
			}//end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignSearch));
			}
	}//end of doForward()
	
	
		public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
						HttpServletResponse response) throws Exception
		{
				try
				{
					log.debug("Inside the doForward method of CFDCampaignSearchAction");
					setLinks(request);
					ClaimManager claimManagerObject = this.getClaimManagerObject();
					TableData tableData = TTKCommon.getTableData(request);
					tableData.modifySearchData(strBackward);
					ArrayList alClaimsList = claimManagerObject.getCFDCompaginList(tableData.getSearchData());
					tableData.setData(alClaimsList, strBackward);
					request.getSession().setAttribute("tableData",tableData);
					return this.getForward(strCFDCampaignList, mapping, request);
				}//end of try
				catch (TTKException expTTK) {
					return this.processExceptions(request, mapping, expTTK);
				}
				catch (Exception exp) {
					return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignSearch));
				}
		}//end of doBackward()
	
		
		public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
				try{
						log.debug("Inside the doView method of CFDCampaignSearchAction");
						setLinks(request);
						HttpSession session=request.getSession();
						//get the tbale data from session if exists
						TableData tableData =(TableData)session.getAttribute("tableData");//TTKCommon.getTableData(request);
						if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
						{
							PreAuthDetailVO preAuthDetailVO=(PreAuthDetailVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
							session.setAttribute("campaignDtlSeqId", preAuthDetailVO.getCampaignDtlSeqId());
						}
						return mapping.findForward(strCampaignBatchDetails);
				}//end of try
				catch (TTKException expTTK) {
					return this.processExceptions(request, mapping, expTTK);
				}
				catch (Exception exp) {
					return this.processExceptions(request, mapping, new TTKException(exp, strCFDCampaignSearch));
				}
		}//end of doView()
		
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
    		throw new TTKException(exp, strCFDCampaignSearch);
    	}//end of catch
    	return claimManager;
    }

} // end of CFDCampaignSearchAction
