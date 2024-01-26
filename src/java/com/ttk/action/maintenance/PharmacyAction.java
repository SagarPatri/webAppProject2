/**
 * @ (#)PharmacyAction.java 30th Oct 2017
 * Project      : TTK HealthCare Services
 * File         : PharmacyAction
 * Author       : Kishor kumar S H
 * Company      : Vidal Health TPA
 * Date Created : 30th Oct 2017
 *
 * @author       :  Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.maintenance;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.business.maintenance.MaintenanceManager;
import com.ttk.business.onlineforms.OnlinePreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.HospitalDetailVO;
import com.ttk.dto.empanelment.HospitalVO;
import com.ttk.dto.finance.PharmacySearchVO;
import com.ttk.dto.finance.PharmacyVO;
import com.ttk.dto.maintenance.InvestigationGroupVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;


public class PharmacyAction extends TTKAction {

	private static final Logger log = Logger.getLogger(PharmacyAction.class );
	private static final String strAddPharmacy="addPharmacy";
	private static final String strPharmacylist="pharmacylist";
	private static final String strViewPharmacy="addPharmacy";
	private static final String strBackward="Backward";
	private static final String strForward="Forward";
	private static final String strPharmacyReviewlist="pharmacyreviewlist";
	private static final String strViewPharmacyReview="viewPharmacy";
	
	/**
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
			HttpServletResponse response) throws TTKException{
		try{
			setLinks(request);
			String strForward	=	"";
			log.debug("Inside PharmacyAction doDefault");
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			if(TTKCommon.checkNull(request.getParameter("PharmReview")).equals("Y"))
				strForward	=	"reviewPharmacyDetails";
			else
				strForward	=	"pharmacydetails";
				
			TableData tableData = TTKCommon.getTableData(request);
    		//create the required grid table
    		tableData.createTableInfo("PharmacySearchTable",new ArrayList());
    		
			return this.getForward(strForward, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp, strAddPharmacy));
		}//end of catch(Exception exp)
	}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doSearch method of PharmacyAction");
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
    		TableData tableData = TTKCommon.getTableData(request);
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{
    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward(strPharmacylist));
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
    			tableData.createTableInfo("PharmacySearchTable",null);
    			//fetch the data from the data access layer and set the data to table object
    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form));
    			tableData.modifySearchData("search");
    		}//end of else
    		ArrayList alPharmacy=maintenanceManagerObject.getPharmacyList(tableData.getSearchData());
    		tableData.setData(alPharmacy, "search");
    		request.getSession().setAttribute("tableData",tableData);
    		//finally return to the grid screen
    		return this.getForward(strPharmacylist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	/*
	 * 
	 */
	public ActionForward doAddPharmacy(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doAddPharmacy method of PharmacyAction");
    		DynaActionForm frmPharmacyMaintance =(DynaActionForm)form;
    		frmPharmacyMaintance.initialize(mapping);
    		frmPharmacyMaintance.set("isReviewedByVidal", "N");
    		request.setAttribute("frmPharmacyMaintance", frmPharmacyMaintance);
            return this.getForward(strAddPharmacy, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doAddPharmacy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	
	/**
	 * Saving the Pharmacy Details General basic Details
	 */
    
    public ActionForward doSavePharmacy(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside the doSaveGeneral method of PharmacyAction");
			String phrmRev	=	request.getParameter("phrmRev");
			DynaActionForm frmPharmacyMaintance =(DynaActionForm)form;
			PharmacyVO pharmacyVO = null;
			pharmacyVO	=	(PharmacyVO)FormUtils.getFormValues(frmPharmacyMaintance,"frmPharmacyMaintance",
     				this,mapping,request);
			pharmacyVO.setAddedBy(TTKCommon.getUserSeqId(request));
			
			MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
			long lPharmacySeqId	= 	maintenanceManagerObject.savePharmacyDetails(pharmacyVO);
			PharmacyVO pVo = maintenanceManagerObject.getPharmacyDetail(new Long(lPharmacySeqId),"N");
			frmPharmacyMaintance = (DynaActionForm)FormUtils.setFormValues("frmPharmacyMaintance",
            		pVo,this,mapping,request);

			request.getSession().setAttribute("frmPharmacyMaintance",frmPharmacyMaintance);
			if(lPharmacySeqId>0)
				request.setAttribute("updated", "message.savedSuccessfully");
			request.setAttribute("phrmRev", phrmRev);
			return this.getForward(strAddPharmacy, mapping, request);
		//	return this.getForward(strPreauthInvoice, mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
	}//end of catch(Exception exp)
	}//end of doSaveGeneral(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
    
    /*
     * View Pharmacy Details 
     */
    public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		String phrmRev	=	request.getParameter("phrmRev");
    		log.debug("Inside the doView method of PharmacyAction");
    		MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
    		PharmacyVO pVo	=	null;
    		PharmacySearchVO psVO		=null;
			TableData tableData = TTKCommon.getTableData(request);

            DynaActionForm frmPharmacyMaintance = (DynaActionForm)form;
            if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            {
            	psVO = (PharmacySearchVO)tableData.getRowInfo(Integer.parseInt((String)(frmPharmacyMaintance).get("rownum")));
            	
            	pVo = maintenanceManagerObject.getPharmacyDetail(new Long(psVO.getPharSeqId()),psVO.getReviewedYN());
            	/*if("Y".equals(phrmRev))
            		pVo = maintenanceManagerObject.getPharmacyDetail(new Long(psVO.getPharSeqId()),psVO.getReviewedYN());
            	else
            		pVo = maintenanceManagerObject.getPharmacyDetail(new Long(psVO.getPharSeqId()),psVO.getReviewedYN());*/
                frmPharmacyMaintance.initialize(mapping);
                //TTKCommon.documentViewer(request);
            }//end if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            pVo.setIsReviewedByVidal(psVO.getReviewedYN());
            frmPharmacyMaintance = (DynaActionForm)FormUtils.setFormValues("frmPharmacyMaintance",
            		pVo,this,mapping,request);
            request.setAttribute("phrmRev", phrmRev);
            request.getSession().setAttribute("frmPharmacyMaintance",frmPharmacyMaintance);
            if("Y".equals(phrmRev))
            	return this.getForward(strViewPharmacyReview, mapping, request);
            else
            	return this.getForward(strViewPharmacy, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doBackward method of HospitalSearchAction");
			TableData tableData = TTKCommon.getTableData(request);
			MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alHospital = maintenanceManagerObject.getPharmacyList(tableData.getSearchData());
    		tableData.setData(alHospital, strBackward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
    		return this.getForward(strPharmacylist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		setLinks(request);
    		log.debug("Inside the doForward method of HospitalSearchAction");
			TableData tableData = TTKCommon.getTableData(request);
			MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alHospital = maintenanceManagerObject.getPharmacyList(tableData.getSearchData());
    		tableData.setData(alHospital, strForward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
    		return this.getForward(strPharmacylist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
	/**
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try
		{
			log.debug("Inside the doClose method of PharmacyAction");
			setLinks(request);
			request.getSession().removeAttribute("frmReportList");
			return mapping.findForward("close");
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	/*
	 * Review search Pharmacy
	 */
	public ActionForward doReviewSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doReviewSearch method of PharmacyAction");
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
    		TableData tableData = TTKCommon.getTableData(request);
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{
    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward(strPharmacyReviewlist));
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
    			tableData.createTableInfo("PharmacySearchTable",null);
    			//fetch the data from the data access layer and set the data to table object
    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form));
    			tableData.modifySearchData("search");
    		}//end of else
    		ArrayList alPharmacy=maintenanceManagerObject.getPharmacyList(tableData.getSearchData());
    		tableData.setData(alPharmacy, "search");
    		request.getSession().setAttribute("tableData",tableData);
    		//finally return to the grid screen
    		return this.getForward(strPharmacyReviewlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doReviewSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)	
	
	
	
	
	
	public ActionForward doReviewBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doReviewBackward method of HospitalSearchAction");
			TableData tableData = TTKCommon.getTableData(request);
			MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alHospital = maintenanceManagerObject.getPharmacyList(tableData.getSearchData());
    		tableData.setData(alHospital, strBackward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
    		return this.getForward(strPharmacyReviewlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    public ActionForward doReviewForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doReviewForward method of HospitalSearchAction");
			TableData tableData = TTKCommon.getTableData(request);
			MaintenanceManager maintenanceManagerObject = this.getMaintenanceManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alHospital = maintenanceManagerObject.getPharmacyList(tableData.getSearchData());
    		tableData.setData(alHospital, strForward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
    		return this.getForward(strPharmacyReviewlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strAddPharmacy));
		}//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	/*
	 * Populate Seacrch parameters
	 */
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmPharmacyMaintanceSearch)
    {
        //build the column names along with their values to be searched
        ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPharmacyMaintanceSearch.getString("sDdcCode")));//0
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPharmacyMaintanceSearch.getString("sShortDesc")));//1
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPharmacyMaintanceSearch.getString("sFullDesc")));//2
		alSearchParams.add((String)frmPharmacyMaintanceSearch.getString("sGender"));//3
		alSearchParams.add((String)frmPharmacyMaintanceSearch.getString("sQatarExcYN"));//4
		alSearchParams.add((String)frmPharmacyMaintanceSearch.getString("sStatus"));//5
		alSearchParams.add((String)frmPharmacyMaintanceSearch.getString("sReviewed"));//6
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchHospital)
	
	
	/**
	 * Returns the MaintenanceManager session object for invoking methods on it.
	 * @return MaintenanceManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private MaintenanceManager getMaintenanceManagerObject() throws TTKException
	{
		MaintenanceManager maintenanceManager = null;
		try
		{
			if(maintenanceManager == null)
			{
				InitialContext ctx = new InitialContext();
				maintenanceManager = (MaintenanceManager) ctx.lookup("java:global/TTKServices/business.ejb3/MaintenanceManagerBean!com.ttk.business.maintenance.MaintenanceManager");
			}//end if(maintenanceManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strAddPharmacy);
		}//end of catch
		return maintenanceManager;
	}//end getMaintenanceManagerObject()	
}//end of PharmacyAction
