/**
 * @ (#) CopaymentAction.java
 * Project       : TTK HealthCare Services
 * File          : CopaymentAction.java
 * Author        : Balaji C R B
 * Company       : Span Systems Corporation
 * Date Created  : Nov 03, 2008
 * @author       : Balaji C R B
 * Modified by   : 
 * Modified date : 
 * Reason        :
 */

package com.ttk.action.administration;

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
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.HospitalCopayVO;
import com.ttk.dto.empanelment.HospitalVO;

import formdef.plugin.util.FormUtils;


/**
 * This class is used to bring Copyment screen in Administration-Products/Polices 
 */

public class CopaymentAction extends TTKAction {
	 //Getting Logger for this Class file
	private static Logger log = Logger.getLogger( CopaymentAction.class ); 
	
	// Action mapping forwards.	
	private static final String strCopaymentchargesproduct="copaymentchargesproduct"; 
	private static final String strCopaymentchargespolicy="copaymentchargespolicy";
	
	//Exception Message Identifier
	private static final String strCopay="copayment";
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    private static final String strTableData = "tableDataSync";
	private static final String strCopaySynclist="copaysynclist";
	
	/**
	 * This method is used to initialize copayment screen
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
			log.info("Inside the doDefault method of CopaymentAction");
			setLinks(request);
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);			
			DynaActionForm  frmCopayment = (DynaActionForm)form;
			frmCopayment.initialize(mapping);
			HospitalVO hospitalVO =null;
			StringBuffer sbfCaption = new StringBuffer();
			if(request.getSession().getAttribute("hospitalVO")!=null){
				hospitalVO = (HospitalVO)request.getSession().getAttribute("hospitalVO");
				request.getSession().setAttribute("hospSeqId",hospitalVO.getHospSeqId());
				log.debug("hospseqid value is " +hospitalVO.getHospSeqId() );
				frmCopayment.set("hospSeqID",hospitalVO.getHospSeqId().toString());
				if(hospitalVO.getCopayAmt()!=null){
					log.debug("copay amt is " + hospitalVO.getCopayAmt());
					frmCopayment.set("copayAmt",hospitalVO.getCopayAmt().toString());
				}//end of if(hospitalVO.getCopayAmt()!=null)
				if(hospitalVO.getCopayPerc()!=null){
					log.debug("copayPerc is " + hospitalVO.getCopayPerc());
					frmCopayment.set("copayPerc",hospitalVO.getCopayPerc().toString());
				}//end of if(hospitalVO.getCopayPerc()!=null)
				frmCopayment.set("caption"," ["+hospitalVO.getHospitalName()+"]");
				log.debug("hospital name is " + hospitalVO.getHospitalName());
			}//end of if(request.getAttribute("lngProdHospSeqID")!=null)
			else {
				sbfCaption.append(" [Network Hospitals]");
				frmCopayment.set("caption",sbfCaption.toString());				
			}//end of else	
			if(request.getAttribute("alHospcopay")!=null){
				ArrayList alHospcopay = (ArrayList)request.getAttribute("alHospcopay");
				request.getSession().setAttribute("alHospcopay",alHospcopay);
			}//end of if(request.getAttribute("alHospcopay"))!=null)
			frmCopayment.set("copayPerc", "");
			//request.getSession().setAttribute("frmCopayment",frmCopayment);
			HospitalManager hospitalManagerObject=this.getHospitalManagerObject();
			Long prodHospSeqId	=	new Long(TTKCommon.getWebBoardId(request).toString());
			
			TableData tableData = (TableData) (request.getSession().getAttribute("copaytableData")==null?new TableData():request.getSession().getAttribute("copaytableData"));
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{
    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward(strCopaymentchargespolicy));
    			}///end of if(!strPageID.equals(""))
    			else
    			{
    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
    				tableData.modifySearchData("sort");//modify the search data
    			}//end of else
    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
    		else
    		{
    			// create the required grid table
    			tableData.createTableInfo("ProvierWiseCopayTable",null);
    			//fetch the data from the data access layer and set the data to table object
    			tableData.setSearchData(this.populateSearchCriteria(frmCopayment, request,hospitalVO.getHospSeqId(),prodHospSeqId,""));
    			tableData.modifySearchData("search");
    		}//end of else
    		
    		ArrayList alCopayDetails	=	hospitalManagerObject.getProviderCopay(tableData.getSearchData());
    		/*request.getSession().setAttribute("alCopayDetails",alCopayDetails);*/

    		tableData.setData(alCopayDetails, "search");
    		request.getSession().setAttribute("copaytableData",tableData);
			if("Products".equals(strActiveSubLink)){
				return mapping.findForward(strCopaymentchargesproduct);
			}//end of if("Products".equals(strActiveSubLink))
			else {
				return mapping.findForward(strCopaymentchargespolicy);
			}//end of else			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
		}//end of catch(Exception exp)
	}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to save Copyament details
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
			log.debug("Inside the doSave method of CopaymentAction");
			setLinks(request);
			HospitalManager hospitalManagerObject=this.getHospitalManagerObject();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			int iResult=0;
			DynaActionForm  frmCopayment = (DynaActionForm)form;
			frmCopayment.set("prodPolicySeqID",TTKCommon.getWebBoardId(request).toString());
			frmCopayment.set("updatedBy",TTKCommon.getUserSeqId(request).toString());
			HospitalCopayVO hospitalCopayVO = (HospitalCopayVO)FormUtils.getFormValues(frmCopayment, this, mapping, request);
			ArrayList<Object> alHospcopay	=	new ArrayList();
			StringBuffer strProviderCopayBenefitsList	=	new StringBuffer();
			StringBuffer strServiceType	=	new StringBuffer();

			String strAssociatedNotifyList[] =(String[])frmCopayment.get("providerCopayBenefitsList");
			String strServiceTypeList[] =(String[])frmCopayment.get("serviceTypeList");
			
			//System.out.println(request.getSession().getAttribute("hospSeqId"));
			Long hospSeqId	=	(Long) request.getSession().getAttribute("hospSeqId");
			/*if(request.getSession().getAttribute("alHospcopay")!=null){
				alHospcopay = (ArrayList)request.getSession().getAttribute("alHospcopay");*/
				alHospcopay.add(hospSeqId+"");//0
				alHospcopay.add(hospitalCopayVO.getProdPolicySeqID());//0
				strProviderCopayBenefitsList.append("|");
				for(int i=0; i<strAssociatedNotifyList.length; i++)
				{
					strProviderCopayBenefitsList.append(String.valueOf(strAssociatedNotifyList[i])).append("|");
				}//end of for(int i=0; i<alAssNotificationList.size(); i++)

				strServiceType.append("|");
				for(int i=0; i<strServiceTypeList.length; i++)
				{
					strServiceType.append(String.valueOf(strServiceTypeList[i])).append("|");
				}//end of for(int i=0; i<alAssNotificationList.size(); i++)
				
				alHospcopay.add(strProviderCopayBenefitsList.toString());//1 -- hospitalCopayVO.getProviderCopayBenefits()
				alHospcopay.add(strServiceType.toString());//2 -- hospitalCopayVO.getServiceType()
				alHospcopay.add(hospitalCopayVO.getCopayPerc());//3
				alHospcopay.add(hospitalCopayVO.getDeductible());//4
				alHospcopay.add(hospitalCopayVO.getSuffix());//5
				alHospcopay.add(hospitalCopayVO.getApplyRule());//6
				alHospcopay.add(hospitalCopayVO.getUpdatedBy());//7
				request.getSession().removeAttribute("alHospcopay");
				iResult = hospitalManagerObject.saveAssocHospCopay(alHospcopay);
			//}//end of if(request.getAttribute("alHospcopay")!=null)
			if(iResult>0){
				//saved successfully
				request.setAttribute("updated","message.saved");
			}//end of if(iResult>0) 
			//System.out.println(request.getSession().getAttribute("hospSeqId"));
			//System.out.println(TTKCommon.getWebBoardId(request));
			hospitalCopayVO.setHospSeqID(hospSeqId);
			Long prodHospSeqId	=	new Long(TTKCommon.getWebBoardId(request).toString());
			TableData tableData = (TableData) (request.getSession().getAttribute("copaytableData")==null?new TableData():request.getSession().getAttribute("copaytableData"));
			ArrayList<Object> alSearchData	=	new ArrayList<Object>();
			alSearchData.add(hospSeqId);
			alSearchData.add(prodHospSeqId);
			alSearchData.add(tableData.getSortColumnNumber()+"");
			alSearchData.add(tableData.getSortOrder()+"");
			alSearchData.add(1+"");
			alSearchData.add(100+"");
			
			tableData.setSearchData(alSearchData);
			ArrayList alCopayDetails	=	hospitalManagerObject.getProviderCopay(tableData.getSearchData());
			tableData.setData(alCopayDetails);
    		request.getSession().setAttribute("copaytableData",tableData);
			/*request.getSession().setAttribute("alCopayDetails",alCopayDetails);*/
			frmCopayment.initialize(mapping);
			if("Products".equals(strActiveSubLink)){
				return mapping.findForward(strCopaymentchargesproduct);
			}//end of if("Products".equals(strActiveSubLink))
			else {
				return mapping.findForward("copayment");
			}//end of else
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to close the Copayment screen and traverse to Hospital list screen
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
													HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doClose method of CopaymentAction");
			setLinks(request);
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			if("Products".equals(strActiveSubLink)){
				return mapping.findForward(strCopaymentchargesproduct);
			}//end of if("Products".equals(strActiveSubLink))
			else {
				return mapping.findForward(strCopaymentchargespolicy);
			}//end of else			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)	
	
	
	/*
	 * 
	 * Delete Provider Copay
	 */
			public ActionForward doDeleteProviderCopay(ActionMapping mapping,ActionForm form,HttpServletRequest request,
						HttpServletResponse response) throws Exception{
				try{
				log.debug("Inside the doDeleteProviderCopay method of CopaymentAction");
				setLinks(request);
				HospitalManager hospitalManagerObject=this.getHospitalManagerObject();
				String strActiveSubLink=TTKCommon.getActiveSubLink(request);
				TableData tableData = (TableData) (request.getSession().getAttribute("copaytableData")==null?new TableData():request.getSession().getAttribute("copaytableData"));
				HospitalCopayVO hospitalCopayVO = null;
				DynaActionForm  frmCopayment = (DynaActionForm)form;
				if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	            {
					hospitalCopayVO = (HospitalCopayVO)tableData.getRowInfo(Integer.parseInt((String)(frmCopayment).get("rownum")));
	            }
				int iResult=0;
				Long addedBy	=	new Long(TTKCommon.getUserSeqId(request).toString());
				iResult = hospitalManagerObject.deleteProviderCopay(hospitalCopayVO.getCopaySeqId(),hospitalCopayVO.getBenefitTypeId(),addedBy);
				hospitalCopayVO = (HospitalCopayVO)FormUtils.getFormValues(frmCopayment, this, mapping, request);
				if(iResult>0){
					request.setAttribute("updated","message.removedSuccessfully");
				}//end of if(iResult>0) 
				Long prodHospSeqId	=	new Long(TTKCommon.getWebBoardId(request).toString());
				Long hospSeqId 		=	new Long(request.getSession().getAttribute("hospSeqId").toString());
				tableData.modifySearchData("search");
				ArrayList<Object> alSearchData	=	new ArrayList<Object>();
	            
				alSearchData.add(hospSeqId);
				alSearchData.add(prodHospSeqId);
				alSearchData.add(tableData.getSortColumnNumber()+"");
				alSearchData.add(tableData.getSortOrder()+"");
				alSearchData.add(1+"");
				alSearchData.add(100+"");
				
				tableData.setSearchData(alSearchData);
				
				ArrayList alCopayDetails	=	hospitalManagerObject.getProviderCopay(tableData.getSearchData());
	    		tableData.setData(alCopayDetails, "search");
				request.getSession().setAttribute("copaytableData",tableData);
				frmCopayment.initialize(mapping);
				if("Products".equals(strActiveSubLink)){
					return mapping.findForward(strCopaymentchargesproduct);
				}//end of if("Products".equals(strActiveSubLink))
				else {
						return mapping.findForward(strCopaymentchargespolicy);
					}//end of else
				}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
			}//end of catch(Exception exp)
			}//end of doDeleteProviderCopay(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

			
			
			
			// Do Search
			
			public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		    		HttpServletResponse response) throws Exception{
		    	try{
		    		setLinks(request);
		    		log.info("Inside the doSearch method of CopaymentAction");
					Long prodHospSeqId	=	new Long(TTKCommon.getWebBoardId(request).toString());
					Long hospSeqId 		=	new Long(request.getSession().getAttribute("hospSeqId").toString());
		    		HospitalManager hospitalObject=this.getHospitalManagerObject();
		    		TableData tableData = (TableData) (request.getSession().getAttribute("copaytableData")==null?new TableData():request.getSession().getAttribute("copaytableData"));
		    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		    		if(!strPageID.equals("") || !strSortID.equals(""))
		    		{
		    			if(!strPageID.equals(""))
		    			{
		    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
		    				return (mapping.findForward(strCopaymentchargespolicy));
		    			}///end of if(!strPageID.equals(""))
		    			else
		    			{
		    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
		    				tableData.modifySearchData("sort");//modify the search data
		    			}//end of else
		    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		    		else
		    		{
		    			// create the required grid table
		    			tableData.createTableInfo("ProvierWiseCopayTable",null);
		    			//fetch the data from the data access layer and set the data to table object
		    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form, request,hospSeqId,prodHospSeqId,""));
		    			tableData.modifySearchData("search");
		    		}//end of else
		    		ArrayList alHospital=hospitalObject.getProviderCopay(tableData.getSearchData());
		    		tableData.setData(alHospital, "search");
		    		request.getSession().setAttribute("copaytableData",tableData);
		    		//finally return to the grid screen
		    		return this.getForward(strCopaymentchargespolicy, mapping, request);
		    	}//end of try
		    	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
				}//end of catch(Exception exp)
		    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			
			//Do Backward from search			
			public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		    		HttpServletResponse response) throws Exception{
		    	try{
		    		setLinks(request);
		    		log.debug("Inside the doBackward method of CopaymentAction");
					TableData tableData = (TableData) (request.getSession().getAttribute("copaytableData")==null?new TableData():request.getSession().getAttribute("copaytableData"));
					HospitalManager hospitalObject=this.getHospitalManagerObject();
		    		tableData.modifySearchData(strBackward);//modify the search data
		    		//fetch the data from the data access layer and set the data to table object
					ArrayList alCopayDetails	=	hospitalObject.getProviderCopay(tableData.getSearchData());
		    		tableData.setData(alCopayDetails, strBackward);//set the table data
		    		request.getSession().setAttribute("copaytableData",tableData);//set the table data object to session
		    		TTKCommon.documentViewer(request);
		    		return this.getForward(strCopaymentchargespolicy, mapping, request);
		    	}//end of try
		    	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
				}//end of catch(Exception exp)
		    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

			//Do Forward from search
		    public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		    		HttpServletResponse response) throws Exception{
		    	try{
		    		setLinks(request);
		    		log.info("Inside the doForward method of CopaymentAction");
					TableData tableData = (TableData) (request.getSession().getAttribute("copaytableData")==null?new TableData():request.getSession().getAttribute("copaytableData"));
					HospitalManager hospitalObject=this.getHospitalManagerObject();
		    		tableData.modifySearchData(strForward);//modify the search data
		    		ArrayList alCopayDetails = hospitalObject.getProviderCopay(tableData.getSearchData());
		    		tableData.setData(alCopayDetails, strForward);//set the table data
		    		request.getSession().setAttribute("copaytableData",tableData);   //set the table data object to session
		    		return this.getForward(strCopaymentchargespolicy, mapping, request);
		    	}//end of try
		    	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
				}//end of catch(Exception exp)
		    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		    
		    
		    
public ActionForward doSynchronize(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
		log.info("Inside the doSynchronize method of CopaymentAction");
		setLinks(request);
		String strTable = "";
		DynaActionForm  frmCopayment = (DynaActionForm)form;
		frmCopayment.initialize(mapping);
		//get the tbale data from session if exists
		TableData tableData = new TableData();
		//create the required grid table
		strTable = "HospitalTable";
		tableData.createTableInfo(strTable,new ArrayList());
		//frmProductSyncList.set("caption",buildCaption(request));
		HospitalVO hospitalVO	=	(HospitalVO) request.getSession().getAttribute("hospitalVO");
		frmCopayment.set("caption"," ["+hospitalVO.getHospitalName()+"]");
		request.getSession().setAttribute(strTableData,tableData);
		request.getSession().setAttribute("frmCopayment",frmCopayment);
		return this.getForward(strCopaySynclist, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
	}//end of catch(Exception exp)
}//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)



public ActionForward doSearchSyncList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
		setLinks(request);
		log.debug("Inside the doSearchSyncList method of CopaymentAction");
		DynaActionForm  frmCopayment = (DynaActionForm)form;
		Long prodHospSeqId	=	new Long(TTKCommon.getWebBoardId(request).toString());
		HospitalVO hospitalVO 		=	(HospitalVO) (request.getSession().getAttribute("hospitalVO"));
		HospitalManager hospitalObject=this.getHospitalManagerObject();
		TableData tableData = (TableData) (request.getSession().getAttribute(strTableData)==null?new TableData():request.getSession().getAttribute(strTableData));
		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(!strPageID.equals(""))
			{
				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return (mapping.findForward(strCopaySynclist));
			}///end of if(!strPageID.equals(""))
			else
			{
				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
				tableData.modifySearchData("sort");//modify the search data
			}//end of else
		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		else
		{
			// create the required grid table
			tableData.createTableInfo("HospitalTable",null);
			//fetch the data from the data access layer and set the data to table object
			tableData.setSearchData(this.populateSearchCriteria(frmCopayment, request,hospitalVO.getHospSeqId(),prodHospSeqId,"SYNC"));
			tableData.modifySearchData("search");
		}//end of else
		ArrayList alCopaySyncList=hospitalObject.getAssProviderSyncList(tableData.getSearchData());
		tableData.setData(alCopaySyncList, "search");
		((Column)((ArrayList)tableData.getTitle()).get(5)).setVisibility(false);
		((Column)((ArrayList)tableData.getTitle()).get(6)).setVisibility(false);
		((Column)((ArrayList)tableData.getTitle()).get(0)).setIsLink(false);
		request.getSession().setAttribute(strTableData,tableData);
		//finally return to the grid screen
		return this.getForward(strCopaySynclist, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
	}//end of catch(Exception exp)
}//end of doSearchSyncList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


//Do Backward from search			
public ActionForward doBackwardSync(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
		setLinks(request);
		log.debug("Inside the doBackwardSync method of CopaymentAction");
		TableData tableData = (TableData) (request.getSession().getAttribute(strTableData)==null?new TableData():request.getSession().getAttribute(strTableData));
		HospitalManager hospitalObject=this.getHospitalManagerObject();
		tableData.modifySearchData(strBackward);//modify the search data
		//fetch the data from the data access layer and set the data to table object
		ArrayList alCopaySyncList	=	hospitalObject.getAssProviderSyncList(tableData.getSearchData());
		tableData.setData(alCopaySyncList, strBackward);//set the table data
		request.getSession().setAttribute(strTableData,tableData);//set the table data object to session
		TTKCommon.documentViewer(request);
		return this.getForward(strCopaySynclist, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
	}//end of catch(Exception exp)
}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

//Do Forward from search
public ActionForward doForwardSync(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
		setLinks(request);
		log.info("Inside the doBackwardSync method of CopaymentAction");
		TableData tableData = (TableData) (request.getSession().getAttribute(strTableData)==null?new TableData():request.getSession().getAttribute(strTableData));
		HospitalManager hospitalObject=this.getHospitalManagerObject();
		tableData.modifySearchData(strForward);//modify the search data
		ArrayList alCopaySyncList = hospitalObject.getAssProviderSyncList(tableData.getSearchData());
		tableData.setData(alCopaySyncList, strForward);//set the table data
		request.getSession().setAttribute(strTableData,tableData);   //set the table data object to session
		return this.getForward(strCopaySynclist, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
	}//end of catch(Exception exp)
}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
/*
 * Synchronise ALL
 */
public ActionForward doSynchronizeAll(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		try{
			log.info("Inside the doSynchronizeAll method of CopaymentAction");
			setLinks(request);
			HospitalManager hospitalManagerObject=this.getHospitalManagerObject();
			int iResult=0;
			DynaActionForm  frmCopayment = (DynaActionForm)form;
			Long prodHospSeqId	=	new Long(TTKCommon.getWebBoardId(request).toString());
			Long addedBy	=	new Long(TTKCommon.getUserSeqId(request).toString());
			HospitalVO hospitalVO	=	(HospitalVO) request.getSession().getAttribute("hospitalVO");
			Long hospSeqId	=	hospitalVO.getHospSeqId();
			TableData tableData = (TableData)request.getSession().getAttribute(strTableData);
			StringBuffer sbHospIds	=	new StringBuffer();
			
			String[] strChk = request.getParameterValues("chkopt");
			if(strChk!=null&&strChk.length!=0)
			{
				for(int k=0;k<strChk.length;k++){
					sbHospIds.append("|");
					hospitalVO=(HospitalVO)tableData.getData().get(Integer.parseInt(strChk[k]));
					sbHospIds.append(hospitalVO.getHospSeqId());
					sbHospIds.append("|");
				}
				hospitalVO	=	null;
			}
			
			iResult	=	hospitalManagerObject.synchronizeAll(prodHospSeqId,addedBy,hospSeqId,sbHospIds.toString());
			if(iResult>0)
				request.setAttribute("updated","message.savedSuccessfully");
			
			return mapping.findForward(strCopaySynclist);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strCopay));
		}//end of catch(Exception exp)
}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

		    
	/**
	 * Returns the HospitalManager session object for invoking methods on it.
	 * @return HospitalManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private HospitalManager getHospitalManagerObject() throws TTKException
	{
		HospitalManager hospitalManager = null;
		try
		{
			if(hospitalManager == null)
			{
				InitialContext ctx = new InitialContext();
				hospitalManager = (HospitalManager) ctx.lookup("java:global/TTKServices/business.ejb3/HospitalManagerBean!com.ttk.business.empanelment.HospitalManager");
			}//end if(hospitalManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "Copay");
		}//end of catch
		return hospitalManager;
	}//end getHospitalManagerObject()
	
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmCopayment,HttpServletRequest request,Long hospSeqId, Long prodHospSeqId,String synvYN)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(hospSeqId);
		alSearchParams.add(prodHospSeqId);
		if("SYNC".equals(synvYN)){
			alSearchParams.add(TTKCommon.replaceSingleQots((String)frmCopayment.get("sEmpanelmentNo")));
			alSearchParams.add(TTKCommon.replaceSingleQots((String)frmCopayment.get("sProviderName")));
			alSearchParams.add(TTKCommon.replaceSingleQots((String)frmCopayment.get("sAlkootBranch")));
		}
		return alSearchParams;
	}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

} // end of CopaymentAction