package com.ttk.action.onlineforms.providerLogin; 
/**
 * @ (#) ProviderAction.java 13th Nov 2015
 * Project      : TTK HealthCare Services
 * File         : ProviderAction.java
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 13th Nov 2015
 *
 * @author       :  Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.onlineforms.providerLogin.BatchListVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.preauth.ShortfallVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for Searching the List Policies to see the Online Account Info.
 * This also provides deletion and updation of products.
 */
public class ProviderAction extends TTKAction {

    private static final Logger log = Logger.getLogger( ProviderAction.class );


    // Action mapping forwards.
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    private static final String strViewAuthDetails="viewAuthDetails";
    private static final String strShowShortfall="viewShortfall";
    private static final String strPreAuthDashBoard="preAuthDashBoard";
    private static final String strBatchInvDetails="batchInvoiceDetails";
    private static final String strBatchInvReptDetails="batchInvoiceReptDetails";
    private static final String strOverDueReport="overDueReport";
    private static final String strNotify="notifications";
    private static final String strViewShortfallDetails="viewShortfallDetails";
    
    
   public ActionForward doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doDefaultPreAuth method of ProviderAction");
			setOnlineLinks(request);
			
			DynaActionForm frmOnlinePreAuthLog =(DynaActionForm)form;
			frmOnlinePreAuthLog.initialize(mapping);     
			//((DynaActionForm)form).initialize(mapping);//reset the form data
			
			TableData tableData=TTKCommon.getTableData(request);
			tableData.createTableInfo("PreAuthSearchTable",null);//CorporateDataTable
			
			//Hiding Claims related columns from preauth
			int iTemp[]={2,10,13,14,15,16,17};
			if(iTemp!=null && iTemp.length>0)
	        {
	            for(int i=0;i<iTemp.length;i++)
	            {
	                ((Column)((ArrayList)tableData.getTitle()).get(iTemp[i])).setVisibility(false);
	            }//end of for(int i=0;i<iTemp.length;i++)
	        }//end of if(iTemp!=null && iTemp.length>0)
			
			request.getSession().setAttribute("tableData",tableData);

			return this.getForward("preauthLogSearch", mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
		}//end of catch(Exception exp)
   }//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   
   /**
    * 
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   
   
   public ActionForward doDefaultClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doDefaultClaim method of ProviderAction");
		setOnlineLinks(request);
		
		DynaActionForm frmOnlinePreAuthLog =(DynaActionForm)form;
		frmOnlinePreAuthLog.initialize(mapping);     
		//((DynaActionForm)form).initialize(mapping);//reset the form data
		
		TableData tableData=TTKCommon.getTableData(request);
		tableData.createTableInfo("PreAuthSearchTable",null);//CorporateDataTable
		request.getSession().setAttribute("tableData",tableData);
		
		//Hiding preauth  related columns from Claims
		int iTemp[]={0,1,9,8,11};
		if(iTemp!=null && iTemp.length>0)
        {
            for(int i=0;i<iTemp.length;i++)
            {
                ((Column)((ArrayList)tableData.getTitle()).get(iTemp[i])).setVisibility(false);
            }//end of for(int i=0;i<iTemp.length;i++)
        }//end of if(iTemp!=null && iTemp.length>0)
		((Column)((ArrayList)tableData.getTitle()).get(7)).setIsLink(false);
		
		return this.getForward("claimsLogSearch", mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doDefaultClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

   
   
   /**
    * 
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   
   
   public ActionForward doDefaultDashBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doDefaultDashBoard method of ProviderAction");
		setOnlineLinks(request);
		
		DynaActionForm frmOnlineDashBoard =(DynaActionForm)form;
		frmOnlineDashBoard.initialize(mapping);     
		//((DynaActionForm)form).initialize(mapping);//reset the form data
		
		
		return this.getForward(strPreAuthDashBoard, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doDefaultDashBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

   
   /**
    * 
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public ActionForward doSearchPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doSearchPreAuth method of ProviderAction");
			setOnlineLinks(request);
			
			DynaActionForm frmOnlinePreAuthLog =(DynaActionForm)form;
			ArrayList<Object> alOnlineAccList = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			tableData= TTKCommon.getTableData(request);
		
		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		{
			((DynaActionForm)form).initialize(mapping);//reset the form data
		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(!strPageID.equals(""))
		{
				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return (mapping.findForward("preauthLogSearch"));
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
			tableData.createTableInfo("PreAuthSearchTable",null);
			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,"PAT"));						
			tableData.modifySearchData("search");
		}//end of else
		int iTemp[]={2,10,13,14,15,16,17};
		if(iTemp!=null && iTemp.length>0)
        {
            for(int i=0;i<iTemp.length;i++)
            {
                ((Column)((ArrayList)tableData.getTitle()).get(iTemp[i])).setVisibility(false);
            }//end of for(int i=0;i<iTemp.length;i++)
        }//end of if(iTemp!=null && iTemp.length>0)
		 
		
		if("".equalsIgnoreCase(frmOnlinePreAuthLog.getString("status")) || "APR".equalsIgnoreCase(frmOnlinePreAuthLog.getString("status")) || "REJ".equalsIgnoreCase(frmOnlinePreAuthLog.getString("status")))
		{
			((Column)((ArrayList)tableData.getTitle()).get(20)).setVisibility(true); 
		}
		else
		{
			((Column)((ArrayList)tableData.getTitle()).get(20)).setVisibility(false); 
		}
		
		alOnlineAccList= onlineProviderManager.getPreAuthSearchList(tableData.getSearchData());
		
		tableData.setData(alOnlineAccList, "search");
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		
		return this.getForward("preauthLogSearch", mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
	}//end of catch(Exception exp)
}//end of doSearchPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

   //doStatusChange
   
   
   public ActionForward doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   		HttpServletResponse response) throws Exception{ 
	   	try{
	   		setOnlineLinks(request);
	   		log.debug("Inside the doForwardClm method of ProviderAction");
				
	   		return this.getForward("preauthLogSearch", mapping, request);
	   	}//end of try
	   	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
			}//end of catch(Exception exp)
	   }//end of doForwardClm(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   /**
    * 
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public ActionForward doSearchClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
		try{
			
			log.debug("Inside the doSearchClaim method of ProviderAction");
			setOnlineLinks(request);
			
			DynaActionForm frmOnlineClaimLog =(DynaActionForm)form;
			ArrayList<Object> alOnlineAccList = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			tableData= TTKCommon.getTableData(request);
		
		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		{
			((DynaActionForm)form).initialize(mapping);//reset the form data
		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(!strPageID.equals(""))
		{
				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return (mapping.findForward("claimsLogSearch"));
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
			tableData.createTableInfo("PreAuthSearchTable",null);
			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,"CLM"));						
			tableData.modifySearchData("search");
			
		}//end of else
		
		//Hiding preauth  related columns from Claims
		int iTemp[]={0,1,9,8,11};
		if(iTemp!=null && iTemp.length>0)
        {
            for(int i=0;i<iTemp.length;i++)
            {
                ((Column)((ArrayList)tableData.getTitle()).get(iTemp[i])).setVisibility(false);
            }//end of for(int i=0;i<iTemp.length;i++)
        }//end of if(iTemp!=null && iTemp.length>0)
		((Column)((ArrayList)tableData.getTitle()).get(7)).setIsLink(false);
		
		alOnlineAccList= onlineProviderManager.getClaimSearchList(tableData.getSearchData());
		tableData.setData(alOnlineAccList, "search");
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		
		return this.getForward("claimsLogSearch", mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
	}//end of catch(Exception exp)
}//end of doSearchClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
      
   /*
    *on Forward of search PreAuths 
    */
   public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doForward method of ProviderAction");
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
           
   		tableData.modifySearchData(strForward);//modify the search data
   		//fetch the data from the data access layer and set the data to table object
   		ArrayList<Object> preauthLogSearch = onlineProviderManager.getPreAuthSearchList(tableData.getSearchData());
   		tableData.setData(preauthLogSearch, strForward);//set the table data
   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
   		return this.getForward("preauthLogSearch", mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   
   /*
    *on Backward of search PreAuths 
    */
   public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doBackward method of ProviderAction");
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
           
   		tableData.modifySearchData(strBackward);//modify the search data
   		//fetch the data from the data access layer and set the data to table object
   		ArrayList<Object> preauthLogSearch = onlineProviderManager.getPreAuthSearchList(tableData.getSearchData());
   		tableData.setData(preauthLogSearch, strBackward);//set the table data
   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
   		return this.getForward("preauthLogSearch", mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

 
   /*
    * Do Forward Common for ALL Forwards on provider Login
    */
   public ActionForward doForwardCommon(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   		HttpServletResponse response) throws Exception{
	   	try{
	   			setOnlineLinks(request);
	   			log.debug("Inside the doForwardCommon method of ProviderAction");
				TableData tableData = (TableData) request.getSession().getAttribute("tableData");
				OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
				String strFowardType	=	request.getParameter("strFowardType");
				String strForwardVar	=	"";
				tableData.modifySearchData(strForward);//modify the search data
				//fetch the data from the data access layer and set the data to table object
				ArrayList<Object> preauthLogSearch=null;
				if("batchReconcil".equals(strFowardType)){
					preauthLogSearch	= onlineProviderManager.getBatchRenconcilList(tableData.getSearchData());
					strForwardVar	=	"batchReconiliation";
				}else if("batchInvoice".equals(strFowardType)){
					preauthLogSearch	= onlineProviderManager.getBatchInvoiceList(tableData.getSearchData());
					strForwardVar	=	"batchInvoice";
				}else if("overDue".equals(strFowardType)){
					ArrayList<BatchListVO> alOverDueReport = null;
					alOverDueReport	= onlineProviderManager.getOverDueList(tableData.getSearchData());
					strForwardVar	=	"overDueReport";
					tableData.setData(alOverDueReport, strForward);//set the table data
					return this.getForward(strForwardVar, mapping, request);
				}
				tableData.setData(preauthLogSearch, strForward);//set the table data
				request.getSession().setAttribute("tableData",tableData);//set the table data object to session
				return this.getForward(strForwardVar, mapping, request);
	   	}//end of try
	   	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
			}//end of catch(Exception exp)
	   }//end of doForwardCommon(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	   
   
   /*
    *on Backward of Common for ALL Forwards on provider Login
    */
   public ActionForward doBackwardCommon(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{ 
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doBackwardCommon method of ProviderAction");
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			String strFowardType	=	request.getParameter("strFowardType");
			String strForwardVar	=	"";
   		tableData.modifySearchData(strBackward);//modify the search data
   		//fetch the data from the data access layer and set the data to table object
   		ArrayList<Object> preauthLogSearch=null;
   		if("batchReconcil".equals(strFowardType)){
			preauthLogSearch	= onlineProviderManager.getBatchRenconcilList(tableData.getSearchData());
			strForwardVar	=	"batchReconiliation";
		} else if("batchInvoice".equals(strFowardType)){
			preauthLogSearch	= onlineProviderManager.getBatchInvoiceList(tableData.getSearchData());
			strForwardVar	=	"batchInvoice";
		}else if("overDue".equals(strFowardType)){
			ArrayList<BatchListVO> alOverDueReport = null;
			alOverDueReport	= onlineProviderManager.getOverDueList(tableData.getSearchData());
			strForwardVar	=	"overDueReport";
			tableData.setData(alOverDueReport, strBackward);//set the table data
			return this.getForward(strForwardVar, mapping, request);
		}
   		
   		tableData.setData(preauthLogSearch, strBackward);//set the table data
   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
   		return this.getForward(strForwardVar, mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doBackwardCommon(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   /*
    *on Forward of search PreAuths 
    */
   public ActionForward doForwardClm(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{ 
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doForwardClm method of ProviderAction");
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
           
   		tableData.modifySearchData(strForward);//modify the search data
   		//fetch the data from the data access layer and set the data to table object
   		ArrayList<Object> alOnlineAccList = onlineProviderManager.getClaimSearchList(tableData.getSearchData());
   		tableData.setData(alOnlineAccList, strForward);//set the table data
   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
   		return this.getForward("claimsLogSearch", mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doForwardClm(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   
   /*
    *on Backward of search PreAuths 
    */
   public ActionForward doBackwardClm(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{ 
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doBackward method of ProviderAction");
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
           
   		tableData.modifySearchData(strBackward);//modify the search data
   		//fetch the data from the data access layer and set the data to table object
   		ArrayList<Object> alOnlineAccList = onlineProviderManager.getClaimSearchList(tableData.getSearchData());
   		tableData.setData(alOnlineAccList, strBackward);//set the table data
   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
   		return this.getForward("claimsLogSearch", mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doBackwardClm(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

 
   
   
   /*
    * Do Forward Common for All Forwards From Grid Table
    */
   
   public ActionForward doCommonForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doCommonForward method of ProviderAction");
   		String strVar		=	request.getParameter("strVar");
   		String strVarForward	=	"";

   			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			tableData.modifySearchData(strForward);//modify the search data
			//fetch the data from the data access layer and set the data to table object
			ArrayList<Object> alOnlineAccList	=	null;
			if("".equals(strVar))
			{
				alOnlineAccList = onlineProviderManager.getClaimSearchList(tableData.getSearchData());	
			}
			
   		
			
			tableData.setData(alOnlineAccList, strForward);//set the table data
   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
   		return this.getForward(strVarForward, mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doCommonForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   
		   
   /**on Click on Status link showing the Authorization details 
    * 
   * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
    */
   public ActionForward doViewAuthDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{
   	try{
   		setOnlineLinks(request);
   		DynaActionForm frmAuthDetailsView = (DynaActionForm)form;
   		UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");
   		log.debug("Inside the doViewAuthDetails method of ProviderAction");
   			String rownum	=	(String)request.getParameter("rownum");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			PreAuthSearchVO preAuthSearchVO	=	null;
			preAuthSearchVO = (PreAuthSearchVO)tableData.getRowInfo(Integer.parseInt(rownum));
			PreAuthSearchVO viewAuthDetailsVO	=	onlineProviderManager.getAuthDetails((String)userSecurityProfile.getEmpanelNumber(),preAuthSearchVO.getStatusID(),preAuthSearchVO.getPatAuthSeqId());
           
			frmAuthDetailsView = (DynaActionForm)FormUtils.setFormValues("frmAuthDetailsView",
					viewAuthDetailsVO, this, mapping, request);
			ArrayList<String[]> alShortFallList	=	onlineProviderManager.getShortfallList(preAuthSearchVO.getPatAuthSeqId());
			request.getSession().setAttribute("alShortFallList",alShortFallList);
			
			request.getSession().setAttribute("patAuthSeqId", preAuthSearchVO.getPatAuthSeqId());//using this in showing shortfall details
			request.getSession().setAttribute("frmAuthDetailsView", frmAuthDetailsView);
			
   		return this.getForward(strViewAuthDetails, mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doViewAuthDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   
   /*
    * View Shortfalls from Providers
    */
   public ActionForward doViewShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   		HttpServletResponse response) throws Exception{
	   	try{
	   		setOnlineLinks(request);
	   		log.debug("Inside the doViewShortfall method of ProviderAction");
	   		DynaActionForm frmShortFall = (DynaActionForm)form;
	   		Long shortfallSeqId		=	new Long(request.getParameter("shortfallSeqId"));
	   		Long patAuthSeqId		=	(long) request.getSession().getAttribute("lPreAuthIntSeqId");
	   		
	   		PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	   	  ArrayList<Object> alShortfallList = new ArrayList<Object>();
          alShortfallList.add(shortfallSeqId);
          alShortfallList.add(patAuthSeqId);
          alShortfallList.add(null);
          alShortfallList.add(TTKCommon.getUserSeqId(request));
          ShortfallVO   shortfallVO=preAuthObject.getShortfallDetail(alShortfallList);
          
          //Get Member and Provider Data for Shortfall
          OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
          String[] shortFallData	=	onlineProviderManager.getMemProviderForShortfall(shortfallSeqId,"PAT");
          
          frmShortFall = (DynaActionForm)FormUtils.setFormValues("frmShortFall", shortfallVO, this, mapping, request);

          request.getSession().setAttribute("preauthShortfallQueries",shortfallVO.getShortfallQuestions());
          request.getSession().setAttribute("frmShortFall",frmShortFall);
          request.getSession().setAttribute("shortFallData", shortFallData);
          return this.getForward(strViewShortfallDetails, mapping, request);
	   	}//end of try
	   	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
			}//end of catch(Exception exp)
	   }//end of doViewAuthDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   
   
   /*
    * Save Shortfalls from Providers
    */
   
   public ActionForward doSaveShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   		HttpServletResponse response) throws Exception{
	   	try{
	   		setOnlineLinks(request);
	   		log.debug("Inside the doSaveShortfall method of ProviderAction");
	   		DynaActionForm frmShortFall = (DynaActionForm)form;
	   		UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");
	   		ShortfallVO shortfallVO = (ShortfallVO)FormUtils.getFormValues(frmShortFall, this, mapping, request);
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			
			// UPLOAD FILE STARTS
			FormFile formFile = null;
			formFile = (FormFile)frmShortFall.get("file");
			// 
			ArrayList inputData=new ArrayList();
			inputData.add(shortfallVO.getShortfallSeqID());
			inputData.add(formFile);
			inputData.add(userSecurityProfile.getHosContactSeqID());
			String successYN	=	onlineProviderManager.saveProviderShorfallDocs(inputData);
			if(successYN.equals("Y"))
				request.setAttribute("updated","message.addedSuccessfully");
	   		return this.getForward(strViewShortfallDetails, mapping, request);
	   	}//end of try
	   	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
			}//end of catch(Exception exp)
	   }//end of doSaveShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  
   
   /**on Click on back Button from details Page 
    * 
   * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
    */
   public ActionForward doBackToPreAuthSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doBackToPreAuthSearch method of ProviderAction");
		TableData tableData = (TableData) request.getSession().getAttribute("tableData");
           
   		//fetch the data from the data access layer and set the data to table object
   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
   		return this.getForward("preauthLogSearch", mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doBackToPreAuthSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   
   
   /**on Click on back Button from details Page 
    * 
   * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
    */
   public ActionForward doBackToPreAuthDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doBackToPreAuthDetails method of ProviderAction");
   		DynaActionForm frmAuthDetailsView	=	(DynaActionForm) request.getSession().getAttribute("frmAuthDetailsView");
   		//fetch the data from the data access layer and set the data to table object
   		return this.getForward(strViewAuthDetails, mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doBackToPreAuthDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   
   
   

   /**on Click shortfall shows the shortfall details of the pre auth 
    * 
   * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
    */
   public ActionForward doPreAuthShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{
   	try{
   		setOnlineLinks(request);
   		log.debug("Inside the doPreAuthShortfall method of ProviderAction");
   		OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
   		Long patAuthSeqId	=	(Long) request.getSession().getAttribute("patAuthSeqId");
		@SuppressWarnings("unchecked")
		ArrayList<String[]> alShortFallList	=	onlineProviderManager.getShortfallList(patAuthSeqId);
		request.getSession().setAttribute("alShortFallList",alShortFallList);
   		return this.getForward(strShowShortfall, mapping, request);
   	}//end of try
   	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
   }//end of doPreAuthShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
   
   /*
    * Do Default Finance DashBoard
    */
   

   public ActionForward doFinanceDashBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.info("Inside the doFinanceDashBoard method of ProviderAction");
			setOnlineLinks(request);

			DynaActionForm frmOnlineValidateFinanceDashBoard =(DynaActionForm)form;
			frmOnlineValidateFinanceDashBoard.initialize(mapping);     
			//((DynaActionForm)form).initialize(mapping);//reset the form data
			
			request.getSession().setAttribute("frmOnlineValidateFinanceDashBoard", frmOnlineValidateFinanceDashBoard);
			return this.getForward("financehDashBoard", mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
		}//end of catch(Exception exp)
   }//end of doFinanceDashBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   /**
    * Do Dearch Finance DashBoard
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public ActionForward doSearchFinanceDashBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.info("Inside the doSearchFinanceDashBoard method of ProviderAction");
		setOnlineLinks(request);
		UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");

		DynaActionForm frmOnlineValidateFinanceDashBoard =(DynaActionForm)form;
		//frmOnlineValidateFinanceDashBoard.initialize(mapping);     
		//((DynaActionForm)form).initialize(mapping);//reset the form data
		String fromDate	=	frmOnlineValidateFinanceDashBoard.getString("fromDate");
		String toDate	=	frmOnlineValidateFinanceDashBoard.getString("toDate");
		OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
		String[] strFinanceDashboard	=	onlineProviderManager.getFinanceDahBoard(userSecurityProfile.getEmpanelNumber(),fromDate,toDate);
		request.setAttribute("strFinanceDashboard", strFinanceDashboard);
		
		request.getSession().setAttribute("frmOnlineValidateFinanceDashBoard", frmOnlineValidateFinanceDashBoard);
		return this.getForward("financehDashBoard", mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doSearchFinanceDashBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)



   public ActionForward doChequeWiseReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doChequeWiseReport method of ProviderAction");
			setOnlineLinks(request);
			
			DynaActionForm frmOnlineFinanceDashBoard =(DynaActionForm)form;
			frmOnlineFinanceDashBoard.initialize(mapping);
			return this.getForward("chequewisereport", mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
		}//end of catch(Exception exp)
   }//end of doChequeWiseReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   
   
   public ActionForward doSearchChequeDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doSearchChequeDetails method of ProviderAction");
		setOnlineLinks(request);
		DynaActionForm frmOnlineFinanceDashBoard =(DynaActionForm)form;
		String chequeNumber	=	(String) frmOnlineFinanceDashBoard.get("chequeNumber");
		OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
		String[] strChequeDetails	=	onlineProviderManager.getChequeDetails(chequeNumber);
		request.setAttribute("strChequeDetails", strChequeDetails);
		request.getSession().setAttribute("frmOnlineFinanceDashBoard", frmOnlineFinanceDashBoard);
		return this.getForward("chequewisereport", mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doSearchChequeDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   
   
   
   

   /**
    * Invoice wise Reports
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */

   public ActionForward doInvoiceWiseReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doInvoiceWiseReport method of ProviderAction");
			setOnlineLinks(request);
			DynaActionForm frmOnlineFinanceDashBoard =(DynaActionForm)form;
			frmOnlineFinanceDashBoard.initialize(mapping);
			return this.getForward("invoicewisereport", mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
		}//end of catch(Exception exp)
   }//end of doInvoiceWiseReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   
   public ActionForward doSearchInvoiceDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doSearchInvoiceDetails method of ProviderAction");
		setOnlineLinks(request);
		DynaActionForm frmOnlineFinanceDashBoard =(DynaActionForm)form;
		String chequeNumber	=	(String) frmOnlineFinanceDashBoard.get("chequeNumber");
		UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");
		OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
		String[] strChequeDetails	=	onlineProviderManager.getInvoiceDetails(chequeNumber,userSecurityProfile.getEmpanelNumber());
		request.setAttribute("strChequeDetails", strChequeDetails);
		request.getSession().setAttribute("frmOnlineFinanceDashBoard", frmOnlineFinanceDashBoard); 
		return this.getForward("invoicewisereport", mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doSearchInvoiceDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   
   /*
    * Batch Reconciliation
    */
   
   
   public ActionForward doBatchReconciliation(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doBatchReconciliation method of ProviderAction");
		setOnlineLinks(request);
		
		DynaActionForm frmOnlinePreAuthLog =(DynaActionForm)form;
		frmOnlinePreAuthLog.initialize(mapping);     
		//((DynaActionForm)form).initialize(mapping);//reset the form data
		
		TableData tableData=TTKCommon.getTableData(request);
		tableData.createTableInfo("BatchReconciliationTable",null);//CorporateDataTable
		
		request.getSession().setAttribute("tableData",tableData);
		return this.getForward("batchReconiliation", mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doBatchReconciliation(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

   
   
   /**										BATCH RECONCILIATION
    * 
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public ActionForward doSearchBatchReconciliation(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
		try{
			
			log.debug("Inside the doSearchBatchReconciliation method of ProviderAction");
			setOnlineLinks(request);
			
			ArrayList<Object> alBatchReconcList = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			tableData= TTKCommon.getTableData(request);
		
		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		{
			((DynaActionForm)form).initialize(mapping);//reset the form data
		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(!strPageID.equals(""))
		{
				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return (mapping.findForward("batchReconiliation"));
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
			tableData.createTableInfo("BatchReconciliationTable",null);
			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,"BAT"));						
			tableData.modifySearchData("search");
			
		}//end of else
		
		alBatchReconcList= onlineProviderManager.getBatchRenconcilList(tableData.getSearchData());
		tableData.setData(alBatchReconcList, "search");
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		
		return this.getForward("batchReconiliation", mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
	}//end of catch(Exception exp)
}//end of doSearchBatchReconciliation(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
	/*
	 * Do Show details of Batch reconciliation - Invoice Details like InProgress, Approved...
	 */
	
public ActionForward doViewBatchInvDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
  		HttpServletResponse response) throws Exception{
  	try{
  		setOnlineLinks(request);
  		log.debug("Inside the doViewBatchInvDetails method of ProviderAction");
  		DynaActionForm frmOnlineFinanceDashBoard =(DynaActionForm)form;
		frmOnlineFinanceDashBoard.initialize(mapping);		
		UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");
  			String strFlag	=	request.getParameter("strFlag");
  			String rownum	=	(String)request.getParameter("rownum");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			BatchListVO batchListVO	=	null;
			batchListVO = (BatchListVO)tableData.getRowInfo(Integer.parseInt(rownum));
			ArrayList<BatchListVO> alBatchInvDtls	=	onlineProviderManager.getBatchInvDetails(batchListVO.getBatchSeqId(),strFlag,userSecurityProfile.getEmpanelNumber());
			//Setting these 3 parameters to pass to download report
			request.setAttribute("batchSeqId", batchListVO.getBatchSeqId());
			request.setAttribute("flag", strFlag);
			request.setAttribute("empnlNo", userSecurityProfile.getEmpanelNumber());
			request.setAttribute("alBatchInvDtls", alBatchInvDtls);
  		return this.getForward(strBatchInvDetails, mapping, request);
  	}//end of try
  	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
  }//end of doViewBatchInvDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



   /*
    * Batch Invoice
    */
   
   
   public ActionForward doBatchInvoice(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doBatchInvoice method of ProviderAction");
		setOnlineLinks(request);
		
		DynaActionForm frmOnlinePreAuthLog =(DynaActionForm)form;
		frmOnlinePreAuthLog.initialize(mapping);  
		
		TableData tableData=TTKCommon.getTableData(request);
		tableData.createTableInfo("BatchInvoiceTable",null);//CorporateDataTable
		
		request.getSession().setAttribute("tableData",tableData);
		return this.getForward("batchInvoice", mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doBatchInvoice(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   
   
   
   /**										BATCH INVOICE
    * 
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public ActionForward doSearchBatchInvoice(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
		try{
			
			log.info("Inside the doSearchBatchInvoice method of ProviderAction");
			setOnlineLinks(request);
			
			ArrayList<Object> alBatchInvList = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			tableData= TTKCommon.getTableData(request);
		
		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		{
			((DynaActionForm)form).initialize(mapping);//reset the form data
		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(!strPageID.equals(""))
		{
				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return (mapping.findForward("batchInvoice"));
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
			tableData.createTableInfo("BatchInvoiceTable",null);
			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,"BATINV"));						
			tableData.modifySearchData("search");
			
		}//end of else
		
		alBatchInvList= onlineProviderManager.getBatchInvoiceList(tableData.getSearchData());
		tableData.setData(alBatchInvList, "search");
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		
		return this.getForward("batchInvoice", mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
	}//end of catch(Exception exp)
}//end of doSearchBatchInvoice(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
    
   
   
   
   
	/*
	 * Do Show details of Batch - Invoice Details...
	 */
	
public ActionForward doViewBatchInvReportDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
  		HttpServletResponse response) throws Exception{
  	try{
  		setOnlineLinks(request);
  		log.debug("Inside the doViewBatchInvReportDetails method of ProviderAction");
		UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");
  			String rownum	=	(String)request.getParameter("rownum");
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			BatchListVO batchListVO	=	null;
			batchListVO = (BatchListVO)tableData.getRowInfo(Integer.parseInt(rownum));
			String[] strBatchInvReptDtls	=	onlineProviderManager.getBatchInvReportDetails(batchListVO.getInvNo(),userSecurityProfile.getEmpanelNumber());
          request.setAttribute("strBatchInvReptDtls", strBatchInvReptDtls);
          
          	//Setting these 2 parameters to pass to download report
			request.setAttribute("invNo", batchListVO.getInvNo());
			request.setAttribute("empnlNo", userSecurityProfile.getEmpanelNumber());
			
  		return this.getForward(strBatchInvReptDetails, mapping, request);
  	}//end of try
  	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
  }//end of doViewBatchInvReportDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


/*
 * Do Default for OverDue Report 
 */

public ActionForward doDefaultOverDue(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doDefaultOverDue method of ProviderAction");
		setOnlineLinks(request);
		
		DynaActionForm frmOnlineOverDueReport =(DynaActionForm)form;
		frmOnlineOverDueReport.initialize(mapping);     
		
		TableData tableData=TTKCommon.getTableData(request);
		tableData.createTableInfo("OverDueReportTable",null);//OverDueReportTable
		request.getSession().setAttribute("tableData",tableData);
		
		return this.getForward(strOverDueReport, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doDefaultOverDue(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)


/*
 * Do Default for Notifications 
 */

public ActionForward doDefaultHelpDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doDefaultHelpDocs method of ProviderAction");
		setOnlineLinks(request);
		
		DynaActionForm frmOnlineOverDueReport =(DynaActionForm)form;
		frmOnlineOverDueReport.initialize(mapping);     
		
		return this.getForward(strNotify, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
	}//end of catch(Exception exp)
}//end of doDefaultNotify(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)


/**										OverDue Report SEARCH
 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public ActionForward doSearchOverDueReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
		try{
			
			log.debug("Inside the doSearchOverDueReport method of ProviderAction");
			setOnlineLinks(request);
			
			ArrayList<BatchListVO> alOverDueReport = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			tableData= TTKCommon.getTableData(request);
		
		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		{
			((DynaActionForm)form).initialize(mapping);//reset the form data
		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(!strPageID.equals(""))
		{
				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return (mapping.findForward(strOverDueReport));
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
			tableData.createTableInfo("OverDueReportTable",null);
			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,"ODUE"));						
			tableData.modifySearchData("search");
			
		}//end of else
		
		alOverDueReport= onlineProviderManager.getOverDueList(tableData.getSearchData());
		tableData.setData(alOverDueReport, "search");
		//set the table data object to session
		request.getSession().setAttribute("tableData",tableData);
		
		return this.getForward(strOverDueReport, mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
	}//end of catch(Exception exp)
}//end of doSearchOverDueReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)


	//LOG DETAILS S T A R T S HERE
   /*
    * 
    */
		public ActionForward doLogDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		                HttpServletResponse response) throws Exception{
		try{
		log.debug("Inside the doLogDetails method of ProviderAction");
		setOnlineLinks(request);
		
		DynaActionForm frmInsCorporate =(DynaActionForm)form;
		ArrayList alLogList = null;
		
		OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
		alLogList	= onlineProviderManager.getLogData(TTKCommon.getUserID(request));
		request.setAttribute("alLogList", alLogList);
		
		return this.getForward("providerLogData", mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
		}//end of catch(Exception exp)
	}//end of doLogDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)
		
		

		//Recent Reports Downloaded S T A R T S HERE
	   /*
	    * 
	    */
			public ActionForward doDefaultRecentReports(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			                HttpServletResponse response) throws Exception{
			try{
			log.debug("Inside the doDefaultRecentReports method of ProviderAction");
			setOnlineLinks(request);
			
			DynaActionForm frmOnlineFinanceDashBoard =(DynaActionForm)form;
			UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");
			ArrayList<String[]> alRecentReps = null;
			
			OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
			alRecentReps	= onlineProviderManager.getRecentReports(userSecurityProfile.getEmpanelNumber(),TTKCommon.getUserSeqId(request));
			request.setAttribute("alRecentReps", alRecentReps);
			return this.getForward("recentReports", mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processOnlineExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
			}//end of catch(Exception exp)
		}//end of doDefaultRecentReports(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)
		
			
			

public ActionForward doDownloadHelpDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
    
	 ByteArrayOutputStream baos=null;
    OutputStream sos = null;
    FileInputStream fis = null; 
    BufferedInputStream bis =null;
  try{
	  		String type		=	request.getParameter("type");
	  		String fileName	=	""; 
	  		if("providerGuidelines".equals(type))
	  			fileName	=	"Provider GuideLines";
	  		if("howToUseForPL".equals(type))
	  			fileName	=	"How To Use";
	  		if("circularsForPL".equals(type))
	  			fileName	=	"Circulars";
	  		if("claimFormForPL".equals(type))
	  			fileName	=	"Claim Form";
	  		String strFile	=	TTKPropertiesReader.getPropertyValue(type);
			File file=new File(strFile);
			  response.setContentType("application/pdf");
		      response.setHeader("Content-Disposition", "attachment;filename="+fileName+".pdf");
                   fis= new FileInputStream(file);
                   bis = new BufferedInputStream(fis);
                   baos=new ByteArrayOutputStream();
                   int ch;
                         while ((ch = bis.read()) != -1) baos.write(ch);
                         sos = response.getOutputStream();
                         baos.writeTo(sos);  
                         baos.flush();      
                         sos.flush(); 
            }catch(Exception exp)
            	{
            		return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
            	}//end of catch(Exception exp)
        
          finally{
                   try{
                         if(baos!=null)baos.close();                                           
                         if(sos!=null)sos.close();
                         if(bis!=null)bis.close();
                         if(fis!=null)fis.close();
                         }catch(Exception exception){
                         log.error(exception.getMessage(), exception);
                         }                     
                 }
       return null;		 
}//end of doDefaultNotify(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)


public ActionForward doCommonICDCodes(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
    
	 ByteArrayOutputStream baos=null;
    OutputStream sos = null;
    FileInputStream fis = null; 
    BufferedInputStream bis =null;
  try{
		
		String strFile	=	TTKPropertiesReader.getPropertyValue("commonIcdCodes");
	  
			File file=new File(strFile);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=COMMON_ICD_CODES.xlsx");
                   fis= new FileInputStream(file);
                   bis = new BufferedInputStream(fis);
                   baos=new ByteArrayOutputStream();
                   int ch;
                         while ((ch = bis.read()) != -1) baos.write(ch);
                         sos = response.getOutputStream();
                         baos.writeTo(sos);  
                         baos.flush();      
                         sos.flush(); 
		      
            }catch(Exception exp)
        	{
        		return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
        	}//end of catch(Exception exp)
        
          finally{
                   try{
                         if(baos!=null)baos.close();                                           
                         if(sos!=null)sos.close();
                         if(bis!=null)bis.close();
                         if(fis!=null)fis.close();
                         }catch(Exception exception){
                         log.error(exception.getMessage(), exception);
                         }                     
                 }
       return null;		 
}//end of doCommonICDCodes(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)


   /**
    * this method will add search criteria fields and values to the arraylist and will return it
    * @param frmOnlinePreAuthLog formbean which contains the search fields
    * @return ArrayList contains search parameters
    */
   private ArrayList<Object> populateSearchCriteria(DynaActionForm frmOnlinePreAuthLog,HttpServletRequest request,String strIdentifier) throws TTKException
   {
	   
		UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");

		//	     build the column names along with their values to be searched
	       ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
		if("PAT".equals(strIdentifier)){
       //prepare the search BOX parameters
       //Long lngHospSeqId =userSecurityProfile.getHospSeqId();
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("preAuthNumber")));//0
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("fromDate")));//1
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("toDate")));//2
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("patientName")));//3
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("approvalNumber")));//4
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("doctorName")));//5
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("patientCardId")));//6
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("benefitType")));//7
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("status")));//8
	       	alSearchBoxParams.add((String)userSecurityProfile.getEmpanelNumber());//9 Empanel Number
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("preAuthRefNo")));//10
		
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("emirateID")));//11
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("preShortFallStatus")));//12
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("eventReferenceNo")));//13
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("inProgressStatus")));//14
		}else if("CLM".equals(strIdentifier)){
	       	alSearchBoxParams.add((String)userSecurityProfile.getEmpanelNumber());//0 Empanel Number
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("fromDate")));//1
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("toDate")));//2
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("patientName")));//3
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("patientCardId")));//4
			alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("claimNumber")));//5
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("invoiceNumber")));//6
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("claimType")));//7
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("batchNumber")));//8
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("benefitType")));//9
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("status")));//10
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("chequeNumber")));//11
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("emirateID")));//12
	    	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("eventReferenceNo")));//13
		}else if("BAT".equals(strIdentifier)){
	       	alSearchBoxParams.add((String)userSecurityProfile.getEmpanelNumber());//0 Empanel Number
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("fromDate")));//1
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("toDate")));//2
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("batchNumber")));//3
		}else if("BATINV".equals(strIdentifier)){
	       	alSearchBoxParams.add((String)userSecurityProfile.getEmpanelNumber());//0 Empanel Number
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("fromDate")));//1
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("toDate")));//2
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("batchNumber")));//3
		}else if("ODUE".equals(strIdentifier)){
	       	alSearchBoxParams.add((String)userSecurityProfile.getEmpanelNumber());//0 Empanel Number
	       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("fromDate")));//1
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("toDate")));//2
	   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("invoiceNumber")));//3
		}
		
   	return alSearchBoxParams;
   }//end of populateSearchCriteria(DynaActionForm frmOnlinePreAuthLog,HttpServletRequest request) 
   
   
    private OnlineProviderManager getOnlineAccessManagerObject() throws TTKException
    {
    	OnlineProviderManager onlineProviderManager = null;
        try
        {
            if(onlineProviderManager == null)
            {
                InitialContext ctx = new InitialContext();
                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
                onlineProviderManager = (OnlineProviderManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineProviderManagerBean!com.ttk.business.onlineforms.providerLogin.OnlineProviderManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "onlineproviderinfo");
        }//end of catch
        return onlineProviderManager;
    }//end of getOnlineAccessManagerObject()

          

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
			throw new TTKException(exp, "onlineproviderinfo");
		}//end of catch
		return preAuthManager;
	}//end getPreAuthManagerObject()
}//end of ProviderAction
