/**
 * @ (#) PreAuthAction.java Apr 27, 2006
 * Project      : TTK HealthCare Services
 * File         : PreAuthAction.java
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : Apr 27, 2006
 *
 * @author       :  Chandrasekaran J
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.preauth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.PreAuthWebBoardHelper;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.MOUDocumentVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.empanelment.TdsCertificateVO;
import com.ttk.dto.enrollment.MemberVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.ClinicianDetailsVO;
import com.ttk.dto.preauth.MemberDetailVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.ProviderDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

import java.util.regex.Pattern;
import java.text.DateFormat;
import java.util.regex.Matcher;

/**
 * This class is used for searching of pre-auth.
 * This class also provides option for deletion of pre-auth.
 */

public class PreAuthAction extends TTKAction
{
	private static Logger log = Logger.getLogger(PreAuthAction.class);
	//declrations of modes
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	private static final String strDeleteList="DeleteList";
	private static final String strPreauthSearch="preauthsearch";

	//Declarations of constants
	private static final String strRegular="Regular";    //identfier for Regular Pre-Auth
	private static final String strEnhanced="Enhanced";  //identifier for Enhanced Pre-Auth
	private static final String strRegularPreAuth="PAT";
	private static final String strEnhancedPreAuth="ICO";
	private static final int ASSIGN_ICON=9;

	//Action mapping forwards.
	private static final String strPreauthlist="preauthlist";
	private static final String strOralPreAuthDetail="oralPreAuthDetail";
	private static String strPreAuthDetails;
	private static final String strPreAuthDetail="preauthdetail";
	private static final String strProviderList="providerSearchList";
	private static final String strMemberList="memberSearchList";
	private static final String strClinicianList="clinicianSearchList";
	private static final String strPreauthEnhancementList="preauthEnhancementList";
	private static final String strActivityCodeList="activityCodeList";
	private static final String strActivitydetails="activitydetails";
	private static final String strActivityDetails="ActivityDetails";
	
	
	private static final String strDocsUpload="DocsUpload";
	private static final String strUpload="upload";
	
	private static final String strPreauthUploadClose="preauthUploadClose";
	private static final String strDocsdelete="docsUploadDelete";


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
																HttpServletResponse response)throws Exception {
		try{
			setLinks(request);
			log.debug("Inside PreAuthAction doDefault");
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			frmPreAuthList.set("activeSubLink",TTKCommon.getActiveSubLink(request));
			TableData tableData =TTKCommon.getTableData(request);
			String strDefaultBranchID  = String.valueOf(((UserSecurityProfile)
											request.getSession().getAttribute("UserSecurityProfile")).getBranchID());
			//create new table data object
			tableData = new TableData();
			//create the required grid table
			tableData.createTableInfo("PreAuthTable",new ArrayList());
			request.getSession().setAttribute("tableData",tableData);
			((DynaActionForm)form).initialize(mapping);//reset the form data
			frmPreAuthList.set("sTtkBranch",strDefaultBranchID);
			frmPreAuthList.set("activeSubLink",TTKCommon.getActiveSubLink(request));
			request.getSession().setAttribute("frmPreAuthList",frmPreAuthList);
			return this.getForward(strPreauthlist, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
	public ActionForward doDefaultEnhancement(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			setLinks(request);
			log.debug("Inside PreAuthAction doDefaultEnhancement");
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			TableData tableData =new TableData();
			//create the required grid table
			tableData.createTableInfo("PreAuthEnhancementTable",new ArrayList());
			request.getSession().setAttribute("preAuthEnhancementListData",tableData);
			((DynaActionForm)form).initialize(mapping);//reset the form data
			request.getSession().setAttribute("frmPreAuthList",frmPreAuthList);
			return this.getForward(strPreauthEnhancementList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthEnhancementList));
		}//end of catch(Exception exp)
	}//end of doDefaultEnhancement(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
			log.debug("Inside PreAuthAction doSearch");
			setLinks(request);
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			TableData tableData =TTKCommon.getTableData(request);
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
					return mapping.findForward(strPreauthlist);
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					
					tableData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				//create the required grid table
				tableData.createTableInfo("PreAuthTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				tableData.setSortColumnName("preauth_priority,received_date");
				
				//tableData.setSortColumnName("PRE_AUTH_NUMBER");
				//this.setColumnVisiblity(tableData,(DynaActionForm)form,request);
				tableData.modifySearchData("search");
			}//end of else
			
			ArrayList alPreauthList= preAuthObject.getPreAuthList(tableData.getSearchData());
			for (Object object :  alPreauthList) {
				PreAuthVO preAuthVO=(PreAuthVO) object;
				if("Y".equals(preAuthVO.getPriorityCorporate())){
					((Column)((ArrayList)tableData.getTitle()).get(11)).setLinkParamMethodName("PreAuthPrioryCorp");
					((Column)((ArrayList)tableData.getTitle()).get(11)).setImageName("getGroupImageName");
					((Column)((ArrayList)tableData.getTitle()).get(11)).setImageTitle("getGroupImageTitle");
				}
			} 
			tableData.setData(alPreauthList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			if(TTKCommon.isAuthorized(request,"AssignAll"))
				frmPreAuthList.set("AssignAllFlagYN","Y");
			else
				frmPreAuthList.set("AssignAllFlagYN","N");
	
			if(TTKCommon.isAuthorized(request,"Assign"))
				frmPreAuthList.set("AssignFlagYN","Y");
			else
				frmPreAuthList.set("AssignFlagYN","N");
			//finally return to the grid screen
			return this.getForward(strPreauthlist, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward activityCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																	HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction activityCodeSearch");
			setLinks(request);
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			HttpSession session=request.getSession();
			DynaActionForm frmActivityDetails=(DynaActionForm)session.getAttribute("frmActivityDetails");
			
			String searchType=((DynaActionForm) form).getString("sSearchType");
			TableData activityCodeListData = null;
			if(session.getAttribute("activityCodeListData") != null)
			{
				activityCodeListData = (TableData)session.getAttribute("activityCodeListData");
			}//end of if((request.getSession()).getAttribute("icdListData") != null)
			else
			{
				activityCodeListData = new TableData();
			}//end of else
			
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(strPageID.equals(""))
				{
					activityCodeListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					activityCodeListData.modifySearchData("sort");//modify the search data                    
				}///end of if(!strPageID.equals(""))
				else
				{
				log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
				activityCodeListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return this.getForward(strActivityCodeList, mapping, request);
				}//end of else
	          }//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				activityCodeListData.createTableInfo("ActivityCodeListTable",null);
				activityCodeListData.setSearchData(this.populateActivityCodeSearchCriteria(frmActivityDetails.getString("preAuthSeqID"),(DynaActionForm)form,request));
				activityCodeListData.modifySearchData("search");	
				if("ACT".equals(searchType)){
					((Column)((ArrayList)activityCodeListData.getTitle()).get(3)).setVisibility(false);
				}
				
			}//end of else
			
			ArrayList alActivityCodeList=null;
			alActivityCodeList= preAuthObject.getActivityCodeList(activityCodeListData.getSearchData());
			activityCodeListData.setData(alActivityCodeList, "search");
			//set the table data object to session
			request.getSession().setAttribute("activityCodeListData",activityCodeListData);
			return this.getForward(strActivityCodeList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
		}//end of catch(Exception exp)
	}//end of activityCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
       try{
                setLinks(request);
                log.debug("Inside PreAuthAction doStatusChange");
               
              return this.getForward(strPreauthlist, mapping, request);
         }//end of try
         catch(TTKException expTTK)
        {
         return this.processExceptions(request, mapping, expTTK);
          }//end of catch(TTKException expTTK)
       catch(Exception exp)
         {
          return this.processExceptions(request, mapping, new TTKException(exp,strPreauthEnhancementList));
           }//end of catch(Exception exp)
     }//end of doDefaultEnhancement(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	
	
	/**
	 * this method will add search criteria fields and values to the arraylist and will return it
	 * @param frmPreAuthList formbean which contains the search fields
	 * @param request HttpServletRequest
	 * @return ArrayList contains search parameters
	 */
	private ArrayList<Object> populateActivityCodeSearchCriteria(String preAuthSeqID,DynaActionForm frmActivityList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sActivityCode")));
		alSearchParams.add((String)frmActivityList.getString("sActivityCodeDesc"));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sSearchType")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sAuthType")));
		//alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sNetworkType")));
		//alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sServiceType")));
		//alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sActivityType")));
		alSearchParams.add(preAuthSeqID);
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sInternalCode")));
		return alSearchParams;
	}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

	public ActionForward doSelectActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside PreAuthAction doSelectActivityCode ");

	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	{
		request.setAttribute("rownum",request.getParameter("rownum"));
		request.setAttribute("sSearchType", request.getParameter("sSearchType"));
	  }//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
((DynaActionForm)form).initialize(mapping);
	return mapping.findForward(strActivitydetails);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
	}//end of catch(Exception exp)
	}//end of doSelectActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
	public ActionForward doActivityCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doActivityCodeForward");
			setLinks(request);
			TableData activityCodeListData = (TableData)request.getSession().getAttribute("activityCodeListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			activityCodeListData.modifySearchData(strForward);//modify the search data
			ArrayList alPreauthList = preAuthObject.getActivityCodeList(activityCodeListData.getSearchData());
			activityCodeListData.setData(alPreauthList, strForward);//set the table data
			request.getSession().setAttribute("activityCodeListData",activityCodeListData);   //set the table data object to session
			return this.getForward(strActivityCodeList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
		}//end of catch(Exception exp)
	}//end of doActivityCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
	public ActionForward doActivityCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doActivityCodeBackward");
			setLinks(request);
			TableData activityCodeListData = (TableData)request.getSession().getAttribute("activityCodeListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			activityCodeListData.modifySearchData(strBackward);//modify the search data
			ArrayList alPreauthList = preAuthObject.getActivityCodeList(activityCodeListData.getSearchData());
			activityCodeListData.setData(alPreauthList, strBackward);//set the table data
			request.getSession().setAttribute("activityCodeListData",activityCodeListData);   //set the table data object to session
			return this.getForward(strActivityCodeList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
		}//end of catch(Exception exp)
	}//end of doActivityCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward closeActivityCodes(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside PreAuthGenealAction closeActivityCodes");
	((DynaActionForm)form).initialize(mapping);
		return mapping.findForward(strActivityDetails);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strPreAuthDetail));
	}//end of catch(Exception exp)

	}//end of closeActivityCodes(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
	public ActionForward doEnhancementSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																	HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doEnhancementSearch");
			setLinks(request);
			HttpSession session=request.getSession();
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			TableData preAuthEnhancementListData = null;
			if(session.getAttribute("preAuthEnhancementListData") != null)
			{
				preAuthEnhancementListData = (TableData)session.getAttribute("preAuthEnhancementListData");
			}//end of if((request.getSession()).getAttribute("icdListData") != null)
			else
			{
				preAuthEnhancementListData = new TableData();
			}//end of else
			
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(strPageID.equals(""))
				{
					preAuthEnhancementListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					preAuthEnhancementListData.modifySearchData("sort");//modify the search data                    
				}///end of if(!strPageID.equals(""))
				else
				{
				log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
				preAuthEnhancementListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return this.getForward(strPreauthEnhancementList, mapping, request);
				}//end of else
	          }//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				preAuthEnhancementListData.createTableInfo("PreAuthEnhancementTable",null);
				preAuthEnhancementListData.setSearchData(this.populateEnhancementSearchCriteria((DynaActionForm)form,request));
				preAuthEnhancementListData.modifySearchData("search");				
			}//end of else
			
			ArrayList alPreAuthEnhancementList=null;
			alPreAuthEnhancementList= preAuthObject.getPreAuthEnhancementList(preAuthEnhancementListData.getSearchData());
			preAuthEnhancementListData.setData(alPreAuthEnhancementList, "search");
			//set the table data object to session
			session.setAttribute("preAuthEnhancementListData",preAuthEnhancementListData);
			session.setAttribute("frmPreAuthList",frmPreAuthList);
			return this.getForward(strPreauthEnhancementList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthEnhancementList));
		}//end of catch(Exception exp)
	}//end of doEnhancementSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward clinicianSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																	HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction clinicianSearch");
			setLinks(request);
			HttpSession session=request.getSession();
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			TableData clinicianListData = null;
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			if("Y".equals(request.getParameter("Entry"))){
				DynaActionForm	frmPreAuthGeneral=(DynaActionForm)session.getAttribute("frmPreAuthGeneral");
				frmPreAuthList.set("sProviderId",frmPreAuthGeneral.getString("providerId"));
				frmPreAuthList.set("sProviderName",frmPreAuthGeneral.getString("providerName"));
			}
			if((request.getSession()).getAttribute("clinicianListData") != null)
			{
				clinicianListData = (TableData)session.getAttribute("clinicianListData");
			}//end of if((request.getSession()).getAttribute("icdListData") != null)
			else
			{
				clinicianListData = new TableData();
			}//end of else
			
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));		
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(strPageID.equals(""))
				{
					clinicianListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					clinicianListData.modifySearchData("sort");//modify the search data                    
				}///end of if(!strPageID.equals(""))
				else
				{
				log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
				clinicianListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return this.getForward(strClinicianList, mapping, request);
				}//end of else
	          }//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				clinicianListData.createTableInfo("ClinicianListTable",null);
				clinicianListData.setSearchData(this.populateClinicianSearchCriteria(frmPreAuthList,request));
				clinicianListData.modifySearchData("search");				
			}//end of else
			
			ArrayList alClinicianList=null;
			alClinicianList= preAuthObject.getClinicianList(clinicianListData.getSearchData());
			clinicianListData.setData(alClinicianList, "search");
			//set the table data object to session
			session.setAttribute("clinicianListData",clinicianListData);
			return this.getForward(strClinicianList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClinicianList));
		}//end of catch(Exception exp)
	}//end of clinicianSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


	public ActionForward doSelectClinicianId(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside PreAuthGenealAction doSelectclinicianId ");
	HttpSession session=request.getSession();
	TableData clinicianListData = (TableData)session.getAttribute("clinicianListData");
String forward=strPreAuthDetail;
	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	{
		ClinicianDetailsVO clinicianDetailsVO=(ClinicianDetailsVO)clinicianListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));
		
		if("activityClinicianSearch".equals(session.getAttribute("forwardMode"))){
			DynaActionForm frmActivityDetails=(DynaActionForm)session.getAttribute("frmActivityDetails");
			frmActivityDetails.set("clinicianId", clinicianDetailsVO.getClinicianId());
			session.setAttribute("frmActivityDetails",frmActivityDetails);
			forward="activitydetails";
		}else{
		
		DynaActionForm frmPreAuthGeneral=(DynaActionForm)session.getAttribute("frmPreAuthGeneral");
		String preAuthSeqID=frmPreAuthGeneral.getString("preAuthSeqID");
			
		frmPreAuthGeneral.set("clinicianId", clinicianDetailsVO.getClinicianId());
		frmPreAuthGeneral.set("clinicianName", clinicianDetailsVO.getClinicianName());
		frmPreAuthGeneral.set("clinicianSpeciality", clinicianDetailsVO.getClinicianSpeciality());
			
		   session.setAttribute("frmPreAuthGeneral",frmPreAuthGeneral);
			if(!(preAuthSeqID==null||preAuthSeqID.length()<1)){
				session.setAttribute("diagnosis", null);
				session.setAttribute("activities", null);
				session.setAttribute("shortfalls", null);	
			}
			forward=strPreAuthDetail;
		}
	}
	return this.getForward(forward, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strClinicianList));
	}//end of catch(Exception exp)
	}//end of doSelectclinicianId(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * this method will add search criteria fields and values to the arraylist and will return it
	 * @param frmPreAuthList formbean which contains the search fields
	 * @param request HttpServletRequest
	 * @return ArrayList contains search parameters
	 */
	private ArrayList<Object> populateClinicianSearchCriteria(DynaActionForm frmPreAuthList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sClinicianId")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sClinicianName")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProviderId")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProviderName")));
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		return alSearchParams;
	}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

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
	public ActionForward doClinicianForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doClinicianForward");
			setLinks(request);
			TableData clinicianListData = (TableData)request.getSession().getAttribute("clinicianListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			clinicianListData.modifySearchData(strForward);//modify the search data
			ArrayList alClinicianList = preAuthObject.getClinicianList(clinicianListData.getSearchData());
			clinicianListData.setData(alClinicianList, strForward);//set the table data
			request.getSession().setAttribute("clinicianListData",clinicianListData);   //set the table data object to session
			return this.getForward(strClinicianList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClinicianList));
		}//end of catch(Exception exp)
	}//end of doClinicianForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward doClinicianBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doClinicianBackward");
			setLinks(request);
			TableData clinicianListData = (TableData)request.getSession().getAttribute("clinicianListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			clinicianListData.modifySearchData(strBackward);//modify the search data
			ArrayList alClinicianList = preAuthObject.getClinicianList(clinicianListData.getSearchData());
			clinicianListData.setData(alClinicianList, strBackward);//set the table data
			request.getSession().setAttribute("clinicianListData",clinicianListData);   //set the table data object to session
			return this.getForward(strClinicianList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClinicianList));
		}//end of catch(Exception exp)
	}//end of doClinicianBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
	public ActionForward providerSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																	HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction providerSearch");
			setLinks(request);
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			TableData providerListData = null;
			if((request.getSession()).getAttribute("providerListData") != null)
			{
				providerListData = (TableData)(request.getSession()).getAttribute("providerListData");
			}//end of if((request.getSession()).getAttribute("icdListData") != null)
			else
			{
				providerListData = new TableData();
			}//end of else
			
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));		
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(strPageID.equals(""))
				{
					providerListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					providerListData.modifySearchData("sort");//modify the search data                    
				}///end of if(!strPageID.equals(""))
				else
				{
				log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
				providerListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return this.getForward(strProviderList, mapping, request);
				}//end of else
	          }//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				providerListData.createTableInfo("ProviderListTable",null);
				providerListData.setSearchData(this.populateProvidersSearchCriteria((DynaActionForm)form,request));
				providerListData.modifySearchData("search");				
			}//end of else
			
			ArrayList alProviderList=null;
			alProviderList= preAuthObject.getProviderList(providerListData.getSearchData());
			providerListData.setData(alProviderList, "search");
			//set the table data object to session
			request.getSession().setAttribute("providerListData",providerListData);
			return this.getForward(strProviderList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProviderList));
		}//end of catch(Exception exp)
	}//end of providerSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
//Ramana
	
 public ActionForward memberSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
        try{
        log.debug("Inside PreAuthAction memberSearch");
               setLinks(request);
        PreAuthManager preAuthObject=this.getPreAuthManagerObject();
        TableData memberListData = null;
           if((request.getSession()).getAttribute("memberListData") != null)
            {
        	   memberListData = (TableData)(request.getSession()).getAttribute("memberListData");
            }//end of if((request.getSession()).getAttribute("icdListData") != null)
         else
          {
        	 memberListData = new TableData();
           }//end of else

         String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
         String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));		
         //if the page number or sort id is clicked
      if(!strPageID.equals("") || !strSortID.equals(""))
       {
        if(strPageID.equals(""))
         {
        	memberListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
        	memberListData.modifySearchData("sort");//modify the search data                    
         }///end of if(!strPageID.equals(""))
       else
        {
                log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
                memberListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
              return this.getForward(strMemberList, mapping, request);
           }//end of else
        }//end of if(!strPageID.equals("") || !strSortID.equals(""))
    else{
    	memberListData.createTableInfo("MemberListTable",null);
    	memberListData.setSearchData(this.populateMembersSearchCriteria((DynaActionForm)form,request));
    	memberListData.modifySearchData("search");				
   }//end of else

   ArrayList alMemberList=null;
   alMemberList= preAuthObject.getMemberList(memberListData.getSearchData());
   memberListData.setData(alMemberList, "search");
   //set the table data object to session
   request.getSession().setAttribute("memberListData",memberListData);
   return this.getForward(strMemberList, mapping, request);
   }//end of try
   catch(TTKException expTTK)
   {
       return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
      return this.processExceptions(request, mapping, new TTKException(exp,strMemberList));
     }//end of catch(Exception exp)
   }
	
	
	
	
	
	
	
	public ActionForward doSelectProviderId(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside PreAuthGenealAction doSelectProviderId ");
	HttpSession session=request.getSession();
	TableData providerListData = (TableData)session.getAttribute("providerListData");

	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	{
		ProviderDetailsVO providerDetailsVO=(ProviderDetailsVO)providerListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));

	DynaActionForm frmPreAuthGeneral=(DynaActionForm)session.getAttribute("frmPreAuthGeneral");
	String preAuthSeqID=(String)frmPreAuthGeneral.get("preAuthSeqID");

		frmPreAuthGeneral.set("providerSeqId", providerDetailsVO.getProviderSeqId().toString());
		frmPreAuthGeneral.set("providerId", providerDetailsVO.getProviderId());
		frmPreAuthGeneral.set("providerName", providerDetailsVO.getProviderName());
		frmPreAuthGeneral.set("providerSpecificRemarks",providerDetailsVO.getProviderSpecificRemarks());
		frmPreAuthGeneral.set("requestedAmountcurrencyType", providerDetailsVO.getCurencyType());
		frmPreAuthGeneral.set("conversionRate","");
		
		session.setAttribute("frmPreAuthGeneral",frmPreAuthGeneral);
		if(!(preAuthSeqID==null||preAuthSeqID.length()<1)){
			session.setAttribute("diagnosis", null);
			session.setAttribute("activities", null);
			session.setAttribute("shortfalls", null);	
		}
		
	}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	return this.getForward(strPreAuthDetail, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strProviderList));
	}//end of catch(Exception exp)
	}//end of doSelectProviderId(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	//Ramana
	
	public ActionForward doSelectMemberId(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside PreAuthGenealAction doSelectMemberId ");
	HttpSession session=request.getSession();
	PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	TableData memberListData = (TableData)session.getAttribute("memberListData");
	String strForward="";
	
	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	{
		
		MemberDetailVO MemberDetailVO=(MemberDetailVO)memberListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));
		int[] count = preAuthObject.getPreAuthAndClmCount(MemberDetailVO.getMemberSeqID());
		String strActTab=TTKCommon.getActiveTab(request);
		
		
		if("Oral Preauth Approval".equals(strActTab)){
			DynaActionForm frmPreAuthoral=(DynaActionForm)session.getAttribute("frmPreAuthoral");

			frmPreAuthoral.set("memberSeqID", MemberDetailVO.getMemberSeqID().toString());
			frmPreAuthoral.set("memberId", MemberDetailVO.getMemberId());
			frmPreAuthoral.set("patientName", MemberDetailVO.getPatientName());
			frmPreAuthoral.set("memberAge",MemberDetailVO.getMemberAge().toString());
			frmPreAuthoral.set("emirateId", MemberDetailVO.getEmirateId());
			frmPreAuthoral.set("payerName", MemberDetailVO.getPayerName());
			frmPreAuthoral.set("payerId", MemberDetailVO.getPayerId());
			frmPreAuthoral.set("insSeqId",MemberDetailVO.getInsSeqId().toString());
			frmPreAuthoral.set("policySeqId", MemberDetailVO.getPolicySeqId().toString());
			frmPreAuthoral.set("patientGender", MemberDetailVO.getPatientGender());
			frmPreAuthoral.set("policyNumber", MemberDetailVO.getPolicyNumber());
			frmPreAuthoral.set("corporateName",MemberDetailVO.getCorporateName());
			frmPreAuthoral.set("policyStartDate", MemberDetailVO.getPolicyStartDate());
			frmPreAuthoral.set("policyEndDate", MemberDetailVO.getPolicyEndDate());
			frmPreAuthoral.set("nationality", MemberDetailVO.getNationality());
			frmPreAuthoral.set("sumInsured",MemberDetailVO.getSumInsured().toString());
			frmPreAuthoral.set("availableSumInsured",MemberDetailVO.getAvailableSumInsured().toString());
			frmPreAuthoral.set("vipYorN",MemberDetailVO.getVipYorN());
			frmPreAuthoral.set("relationShip",MemberDetailVO.getRelationShip());
			frmPreAuthoral.set("preMemInceptionDt",MemberDetailVO.getPreMemInceptionDt());
			frmPreAuthoral.set("productName",MemberDetailVO.getProductName());
			frmPreAuthoral.set("eligibleNetworks",MemberDetailVO.getEligibleNetworks());
			frmPreAuthoral.set("payerAuthority",MemberDetailVO.getProvAuthority());
			frmPreAuthoral.set("memberSpecificRemarks", MemberDetailVO.getMemberSpecificRemarks());
			
			session.setAttribute("frmPreAuthoral",frmPreAuthoral);
			strForward=strOralPreAuthDetail;
		}else{
		
	DynaActionForm frmPreAuthGeneral=(DynaActionForm)session.getAttribute("frmPreAuthGeneral");

		frmPreAuthGeneral.set("memberSeqID", MemberDetailVO.getMemberSeqID().toString());
		frmPreAuthGeneral.set("memberId", MemberDetailVO.getMemberId());
		frmPreAuthGeneral.set("patientName", MemberDetailVO.getPatientName());
		frmPreAuthGeneral.set("memberAge",MemberDetailVO.getMemberAge().toString());
		frmPreAuthGeneral.set("emirateId", MemberDetailVO.getEmirateId());
		frmPreAuthGeneral.set("payerName", MemberDetailVO.getPayerName());
		frmPreAuthGeneral.set("payerId", MemberDetailVO.getPayerId());
		frmPreAuthGeneral.set("insSeqId",MemberDetailVO.getInsSeqId().toString());
		frmPreAuthGeneral.set("policySeqId", MemberDetailVO.getPolicySeqId().toString());
		frmPreAuthGeneral.set("patientGender", MemberDetailVO.getPatientGender());
		frmPreAuthGeneral.set("policyNumber", MemberDetailVO.getPolicyNumber());
		frmPreAuthGeneral.set("corporateName",MemberDetailVO.getCorporateName());
		frmPreAuthGeneral.set("policyStartDate", MemberDetailVO.getPolicyStartDate());
		frmPreAuthGeneral.set("policyEndDate", MemberDetailVO.getPolicyEndDate());
		frmPreAuthGeneral.set("nationality", MemberDetailVO.getNationality());
		frmPreAuthGeneral.set("sumInsured",MemberDetailVO.getSumInsured().toString());
		frmPreAuthGeneral.set("availableSumInsured",MemberDetailVO.getAvailableSumInsured().toString());
		frmPreAuthGeneral.set("vipYorN",MemberDetailVO.getVipYorN());
		frmPreAuthGeneral.set("relationShip",MemberDetailVO.getRelationShip());
		frmPreAuthGeneral.set("relationFlag",MemberDetailVO.getRelationFlag());
		frmPreAuthGeneral.set("memberSpecificRemarks",MemberDetailVO.getMemberSpecificRemarks());
		frmPreAuthGeneral.set("preMemInceptionDt",MemberDetailVO.getPreMemInceptionDt());
		frmPreAuthGeneral.set("productName",MemberDetailVO.getProductName());
		frmPreAuthGeneral.set("eligibleNetworks",MemberDetailVO.getEligibleNetworks());
		frmPreAuthGeneral.set("payerAuthority",MemberDetailVO.getProvAuthority());
		frmPreAuthGeneral.set("preAuthCount",(""+count[0]).trim());
		frmPreAuthGeneral.set("clmCount",(""+count[1]).trim());
		
		
		session.setAttribute("frmPreAuthGeneral",frmPreAuthGeneral);
		strForward=strPreAuthDetail;
		
		}
		
	}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	return this.getForward(strForward, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strMemberList));
	}//end of catch(Exception exp)
	}//end of doSelectProviderId(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	
	/**
	 * this method will add search criteria fields and values to the arraylist and will return it
	 * @param frmPreAuthList formbean which contains the search fields
	 * @param request HttpServletRequest
	 * @return ArrayList contains search parameters
	 */
	private ArrayList<Object> populateProvidersSearchCriteria(DynaActionForm frmPreAuthList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProviderId")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProviderName")));
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		alSearchParams.add("");
		return alSearchParams;
	}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

	//Ramana
	
	private ArrayList<Object> populateMembersSearchCriteria(DynaActionForm frmPreAuthList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sEnrollmentId")));
        alSearchParams.add(TTKCommon.getUserSeqId(request));
		return alSearchParams;
	}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

	
	
	
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
	public ActionForward doProviderForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doProviderForward");
			setLinks(request);
			TableData providerListData = (TableData)request.getSession().getAttribute("providerListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			providerListData.modifySearchData(strForward);//modify the search data
			ArrayList alProviderList = preAuthObject.getProviderList(providerListData.getSearchData());
			providerListData.setData(alProviderList, strForward);//set the table data
			request.getSession().setAttribute("providerListData",providerListData);   //set the table data object to session
			return this.getForward(strProviderList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProviderList));
		}//end of catch(Exception exp)
	}//end of doProviderForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward doProviderBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doProviderBackward");
			setLinks(request);
			TableData providerListData = (TableData)request.getSession().getAttribute("providerListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			providerListData.modifySearchData(strBackward);//modify the search data
			ArrayList alProviderList = preAuthObject.getProviderList(providerListData.getSearchData());
			providerListData.setData(alProviderList, strBackward);//set the table data
			request.getSession().setAttribute("providerListData",providerListData);   //set the table data object to session
			return this.getForward(strProviderList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProviderList));
		}//end of catch(Exception exp)
	}//end of doProviderBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   //Ramana
	
	public ActionForward doMemberForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
try{
           log.debug("Inside PreAuthAction doMeberForward");
           setLinks(request);
           TableData memberListData = (TableData)request.getSession().getAttribute("memberListData");
           PreAuthManager preAuthObject=this.getPreAuthManagerObject();
           memberListData.modifySearchData(strForward);//modify the search data
           ArrayList alMemberList = preAuthObject.getMemberList(memberListData.getSearchData());
           memberListData.setData(alMemberList, strForward);//set the table data
         request.getSession().setAttribute("memberListData",memberListData);   //set the table data object to session
         return this.getForward(strMemberList, mapping, request);   //finally return to the grid screen
         }//end of try
      catch(TTKException expTTK)
          {
           return this.processExceptions(request, mapping, expTTK);
           }//end of catch(TTKException expTTK)
       catch(Exception exp)
        {
           return this.processExceptions(request, mapping, new TTKException(exp,strMemberList));
        }//end of catch(Exception exp)
      }//end of doProviderForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
        public ActionForward doMemberBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			        HttpServletResponse response) throws Exception{
         try{
              log.debug("Inside PreAuthAction doMemberBackward");
              setLinks(request);
              TableData memberListData = (TableData)request.getSession().getAttribute("memberListData");
              PreAuthManager preAuthObject=this.getPreAuthManagerObject();
              memberListData.modifySearchData(strBackward);//modify the search data
              ArrayList alMemberList = preAuthObject.getMemberList(memberListData.getSearchData());
              memberListData.setData(alMemberList, strBackward);//set the table data
               request.getSession().setAttribute("memberListData",memberListData);   //set the table data object to session
                return this.getForward(strMemberList, mapping, request);   //finally return to the grid screen
              }//end of try
          catch(TTKException expTTK)
          {
                return this.processExceptions(request, mapping, expTTK);
              }//end of catch(TTKException expTTK)
          catch(Exception exp)
          {
           return this.processExceptions(request, mapping, new TTKException(exp,strMemberList));
            }//end of catch(Exception exp)
         }//end of doProviderBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
			log.debug("Inside PreAuthAction doForward");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			tableData.modifySearchData(strForward);//modify the search data
			ArrayList alPreauthList = preAuthObject.getPreAuthList(tableData.getSearchData());
			tableData.setData(alPreauthList, strForward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return this.getForward(strPreauthlist, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doClose");
			setLinks(request);
			String strActTab=TTKCommon.getActiveTab(request);
			
			
			if("Oral Preauth Approval".equals(strActTab))
				
			return this.getForward(strOralPreAuthDetail, mapping, request);
			else
				return this.getForward(strPreAuthDetail, mapping, request);	
				
				
				//finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
			log.debug("Inside PreAuthAction doBackward");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			tableData.modifySearchData(strBackward);//modify the search data
			ArrayList alPreauthList = preAuthObject.getPreAuthList(tableData.getSearchData());
			tableData.setData(alPreauthList, strBackward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return this.getForward(strPreauthlist, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward doEnhancementBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doEnhancementBackward");
			setLinks(request);
			TableData  preAuthEnhancementListData = (TableData)(request.getSession()).getAttribute("preAuthEnhancementListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			preAuthEnhancementListData.modifySearchData(strBackward);//modify the search data
			ArrayList alPreAuthEnhancementList = preAuthObject.getPreAuthList(preAuthEnhancementListData.getSearchData());
			preAuthEnhancementListData.setData(alPreAuthEnhancementList, strBackward);//set the table data
			request.getSession().setAttribute("preAuthEnhancementListData",preAuthEnhancementListData);   //set the table data object to session
			return this.getForward(strPreauthEnhancementList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthEnhancementList));
		}//end of catch(Exception exp)
	}//end of doEnhancementBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward doEnhancementForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doEnhancementForward");
			setLinks(request);
			TableData  preAuthEnhancementListData = (TableData)(request.getSession()).getAttribute("preAuthEnhancementListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			preAuthEnhancementListData.modifySearchData(strForward);//modify the search data
			ArrayList alPreAuthEnhancementList = preAuthObject.getPreAuthList(preAuthEnhancementListData.getSearchData());
			preAuthEnhancementListData.setData(alPreAuthEnhancementList, strForward);//set the table data
			request.getSession().setAttribute("preAuthEnhancementListData",preAuthEnhancementListData);   //set the table data object to session
			return this.getForward(strPreauthEnhancementList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthEnhancementList));
		}//end of catch(Exception exp)
	}//end of doEnhancementForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * This method is used to get the delete records from the grid screen.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doDeleteList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
													HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doDeleteList");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			StringBuffer sbfDeleteId = new StringBuffer("|");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			int iCount=0;
			//populate the delete string which contains the sequence id's to be deleted
			sbfDeleteId.append(populateDeleteId(request,(TableData)request.getSession().getAttribute("tableData")));
			ArrayList <Object>alDeleteMode=new ArrayList<Object>();
			alDeleteMode.add(strRegularPreAuth);
			alDeleteMode.add(sbfDeleteId.toString());
			alDeleteMode.add(null);//PAT_ENROLL_DETAIL_SEQ_ID is not required.
			alDeleteMode.add(null);//PAT_GENERAL_DETAIL_SEQ_ID is not required
			alDeleteMode.add(TTKCommon.getUserSeqId(request));//User id
			//delete the selected preauth based on the flow
			iCount = preAuthObject.deletePATGeneral(alDeleteMode);

			//refresh the grid with search data in session
			ArrayList alPreauthList = null;
			//fetch the data from previous set of rowcounts, if all the records are deleted for the current set
			//of search criteria
			if(iCount == tableData.getData().size())
			{
				tableData.modifySearchData(strDeleteList);//modify the search data
				int iStartRowCount = Integer.parseInt((String)tableData.getSearchData().get(
																			tableData.getSearchData().size()-2));
				if(iStartRowCount > 0)
				{
					alPreauthList= preAuthObject.getPreAuthList(tableData.getSearchData());
				}//end of if(iStartRowCount > 0)
			}//end if(iCount == tableData.getData().size())
			else
			{
				alPreauthList= preAuthObject.getPreAuthList(tableData.getSearchData());
			}//end of else
			tableData.setData(alPreauthList, strDeleteList);
			if(iCount>0)
			{
				//delete the Pre-Auth from the web board if any
				request.setAttribute("cacheId",sbfDeleteId.append("|").toString());
				PreAuthWebBoardHelper.deleteWebBoardId(request,"SeqId");
			}//end of if(iCount>0)
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strPreauthlist, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doDeleteList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)

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
			log.debug("Inside PreAuthAction doCopyToWebBoard");
			setLinks(request);
			this.populateWebBoard(request);
			return this.getForward(strPreauthlist, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
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
	public ActionForward doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
														HttpServletResponse response) throws Exception{
		try{ 
			log.debug("Inside PreAuthAction doViewPreauth");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			HttpSession session = request.getSession();
			session.removeAttribute("preauthviewfromdashbord");
			session.removeAttribute("dashboardpreauthseqid");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			session.setAttribute("partnersList", preAuthObject.getPartnersList());
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				PreAuthVO preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt((String)frmPreAuthList.get("rownum")));
				if("ORAL".equals(preAuthVO.getOralORsystemStatus()))
				{
					request.setAttribute("invoked",null);
					request.setAttribute("preAuthVO",preAuthVO);
					strPreAuthDetails="preauthoraldetail";
				}
				else
				{
					strPreAuthDetails="preauthdetail";
					this.addToWebBoard(preAuthVO, request,strRegular);
				}
				session.setAttribute("preAuthStatus", preAuthVO.getStatus());
				session.setAttribute("claimStatus", null);
				session.setAttribute("isPtrPat", false);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			return mapping.findForward(strPreAuthDetails);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
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
	public ActionForward doViewEnhanced(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside PreAuthAction doViewEnhanced");
			setLinks(request);
			TableData preAuthEnhancementListData = (TableData)request.getSession().getAttribute("preAuthEnhancementListData");
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				PreAuthVO preAuthVO=(PreAuthVO)preAuthEnhancementListData.getRowInfo(Integer.parseInt((String)(frmPreAuthList).get("rownum")));
				request.getSession().setAttribute("enhancementPreAuthSeqID",preAuthVO.getPreAuthSeqID());
				request.setAttribute("invoked",null);
				//this.addToWebBoard(preAuthVO, request,strEnhanced);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			return mapping.findForward(strPreAuthDetail);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPreauthSearch));
		}//end of catch(Exception exp)
	}//end of doViewEnhanced(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)


/**
 * closeClinicians forward 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public ActionForward closeClinicians(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside PreAuthGenealAction closeClinicians");
	return mapping.findForward("activitydetails");
}//end of try
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,"preauthdetail"));
}//end of catch(Exception exp)
}//end of closeClinicians(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * this method will add search criteria fields and values to the arraylist and will return it
	 * @param frmPreAuthList formbean which contains the search fields
	 * @param request HttpServletRequest
	 * @return ArrayList contains search parameters
	 */
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmPreAuthList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sPreAuthNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProviderName")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sEnrollmentId")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sClaimantName")));
		alSearchParams.add((String)frmPreAuthList.getString("sRecievedDate"));
		alSearchParams.add((String)frmPreAuthList.getString("sTtkBranch"));
		alSearchParams.add((String)frmPreAuthList.getString("sAssignedTo"));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sSpecifyName")));
		alSearchParams.add((String)frmPreAuthList.getString("sAmount"));
		alSearchParams.add((String)frmPreAuthList.getString("sSource"));
		alSearchParams.add((String)frmPreAuthList.getString("sStatus"));
		alSearchParams.add((String)frmPreAuthList.getString("sWorkFlow"));
		alSearchParams.add((String)frmPreAuthList.getString("sPreAuthType"));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sPolicyNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sEmployeeNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sCorporateName")));
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		alSearchParams.add(TTKCommon.getUserGroupList(request));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sSchemeName")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sCertificateNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sInsurerAppStatus")));//bajaj
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sPayerName")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sAuthorizationNO")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sGlobalNetMemID")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProcessType")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("preShortFallStatus")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("eventReferenceNo")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sLinkedClaimNo")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sBenefitType")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sAmountRange")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sAmountRangeValue")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sPartnerName")));
    	
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sQatarId")));
    	alSearchParams.add((String)frmPreAuthList.getString("inProgressStatus"));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("internalRemarkStatus")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("riskLevel")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("cfdInvestigationStatus")));
    	alSearchParams.add((String)frmPreAuthList.getString("priorityCorporate"));
		return alSearchParams;
	}//end of populateSearchCriteria(DynaActionForm frmProductList)

	/**
	 * this method will add search criteria fields and values to the arraylist and will return it
	 * @param frmPreAuthList formbean which contains the search fields
	 * @param request HttpServletRequest
	 * @return ArrayList contains search parameters
	 */
	private ArrayList<Object> populateEnhancementSearchCriteria(DynaActionForm frmPreAuthList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sPreAuthNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sClaimantName")));
		alSearchParams.add((String)frmPreAuthList.getString("sRecievedDate"));
		alSearchParams.add((String)frmPreAuthList.getString("sStatus"));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sPolicyNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sEnrollmentId")));		
		alSearchParams.add(TTKCommon.getUserSeqId(request));
    	return alSearchParams;
	}//end of populateEnhancementSearchCriteria(DynaActionForm frmProductList)

	
	/**
	 * This method supresess the Assign Icon Column by checking against the Assign and Special Permissions
	 * from the User's Role.
	 * @param tableData TableData object which contains the Grid Information
	 * @param frmPreAuthList formbean which contains the search fields
	 * @param request HttpServletRequest object
	 * @throws TTKException if any run time exception occures
	 */
	private void setColumnVisiblity(TableData tableData,DynaActionForm frmPreAuthList,HttpServletRequest request)
																									throws TTKException
	{
		String strAssignTo=frmPreAuthList.getString("sAssignedTo");
		boolean blnVisibility=false;
		if(strAssignTo.equals("SLF") && TTKCommon.isAuthorized(request,"Assign"))//For Self Check the Assign Permission
		{
			blnVisibility=true;
		}//end of if(strAssignTo.equals("SLF") && TTKCommon.isAuthorized(request,"Assign"))
		else        //Check for the special Permission to show ICON for Others and Unassigned Pre-auth
		{
			if(TTKCommon.isAuthorized(request,"AssignAll"))
			{
				blnVisibility=true;
			}//end of if(TTKCommon.isAuthorized(request,"AssignAll"))
		}//end of else
		((Column)((ArrayList)tableData.getTitle()).get(8)).setVisibility(true); //koc 11 koc11
		((Column)((ArrayList)tableData.getTitle()).get(ASSIGN_ICON)).setVisibility(blnVisibility);
	}//end of setColumnVisiblity(TableData tableData,DynaActionForm frmPreAuthList,HttpServletRequest request)

	/**
	 * This method returns a string which contains the comma separated sequence id's to be deleted,
	 * in Enrollmemt flow pipe seperated policy seq ids and in endorsement flow pipe seperated endorsement seq ids
	 * are sent to the called method
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 * @param policyListData TableData object which contains the value objects
	 * @return String which contains the comma separated sequence id's to be deleted
	 */
	private String populateDeleteId(HttpServletRequest request, TableData tableData)
	{
		String[] strChk = request.getParameterValues("chkopt");
		StringBuffer sbfDeleteId = new StringBuffer();
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
						sbfDeleteId.append(String.valueOf(((PreAuthVO)tableData.getData().get(
																	Integer.parseInt(strChk[i]))).getPreAuthSeqID()));
					}// end of if(i == 0)
					else
					{
						sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((PreAuthVO)
											tableData.getData().get(Integer.parseInt(strChk[i]))).getPreAuthSeqID()));
					}// end of else
				}//end of if(strChk[i]!=null)
			}//end of for(int i=0; i<strChk.length;i++)
			sbfDeleteId.append("|");
		}//end of if(strChk!=null&&strChk.length!=0)
		log.debug("DELETE IDS !!!!!!! "+sbfDeleteId);
		return sbfDeleteId.toString();
	}//end of populateDeleteId(HttpServletRequest request, TableData tableData)

	/**
	 * Returns the PreAuthManager session object for invoking methods on it.
	 * @return PreAuthManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
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
			throw new TTKException(exp, strPreauthSearch);
		}//end of catch
		return preAuthManager;
	}//end getPreAuthManagerObject()

	/**
	 * Populates the web board in the session with the selected items in the grid
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 * @throws TTKException If any run time Excepton occures
	 */
	private void populateWebBoard(HttpServletRequest request)throws TTKException
	{
		String[] strChk = request.getParameterValues("chkopt");
		TableData tableData = (TableData)request.getSession().getAttribute("tableData");
		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
		CacheObject cacheObject = null;
		PreAuthVO preAuthVO=null;

		if(strChk!=null&&strChk.length!=0)
		{
			for(int i=0; i<strChk.length;i++)
			{
				cacheObject = new CacheObject();
				preAuthVO=(PreAuthVO)tableData.getData().get(Integer.parseInt(strChk[i]));
				cacheObject.setCacheId(this.prepareWebBoardId(preAuthVO,strRegular));
				cacheObject.setCacheDesc(preAuthVO.getPreAuthNo());
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
	 * @param  preauthVO  object which contains the information of the preauth
	 * * @param String  strIdentifier whether it is preauth or enhanced preauth
	 * @param request HttpServletRequest
	 * @throws TTKException if any runtime exception occures
	 */
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
	
	
	public ActionForward  doDefaultUpload(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
	{
		try
		{   
			String preAuthSeqID	=	(String)request.getParameter("preAuthSeqID");
			log.debug("Inside the doDefault method of UploadMOUDocsAction");
			request.getSession().setAttribute("preAuth_seq_id_docs_uploads", preAuthSeqID);
			setLinks(request);
			String strTable = "";
			StringBuffer sbfCaption= new StringBuffer();	
			DynaActionForm frmDocsUpload= null;
			
			if( "Y".equals( (String)request.getParameter("Entry") ) ) {
				frmDocsUpload = (DynaActionForm)form;
				frmDocsUpload.initialize(mapping);
			}//end of if(request.getParameter("Entry").equals("Y"))
			else {		
				frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmPreAuthGeneral");
				request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
				sbfCaption.append("[").append(frmDocsUpload.get("preAuthNo")).append("]");	
				request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
			//	request.getSession().setAttribute("EmpanelNumber",strEmpanelNumber);
			}//end of else			
			ArrayList<Object> alDocsUploadList = new ArrayList<Object>();	
			PreAuthManager preAuthObject=null;
			TableData tableDataDocsUpload =TTKCommon.getTableData(request);
			frmDocsUpload.set("caption",sbfCaption.toString());
			//get the table data from session if exists			
			if(tableDataDocsUpload==null){
				//create new table data object
				tableDataDocsUpload = new TableData();
			}//end of if(tableData==null) 	
			//create the required grid table
			strTable = "DocsUploadFilesTable";
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			if(strSortID.equals(""))
			{
				tableDataDocsUpload.createTableInfo(strTable,null);
			//	tableDataDocsUpload.setSearchData(this.populateSearchCriteria(request));
				tableDataDocsUpload.modifySearchData("search");
			}//end of if(strSortID.equals(""))
			else
			{
				tableDataDocsUpload.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
				tableDataDocsUpload.modifySearchData("sort");//modify the search data	
			}// end of else	
			preAuthObject=this.getPreAuthManagerObject();
			alDocsUploadList = preAuthObject.getPreauthDocsUploadsList(preAuthSeqID);
			tableDataDocsUpload.setData(alDocsUploadList,"search");
			request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
			request.getSession().setAttribute("frmDocsUpload",frmDocsUpload);
			return this.getForward(strDocsUpload, mapping, request);
		} // end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strUpload));
		}//end of catch(Exception exp)
	} // end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			//HttpServletResponse response) throws TTKException 

	private ArrayList<Object> populateSearchCriteria(HttpServletRequest request) throws TTKException
	{
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.getWebBoardId(request));
		return alSearchParams;
	}//end of populateSearchCriteria(request)
	
	
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
	public ActionForward preauthDocUploads(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try{
			PreAuthDetailVO preAuthDetailVO	=	new PreAuthDetailVO();
			DynaActionForm frmDocsUpload=(DynaActionForm)form;
			preAuthDetailVO=(PreAuthDetailVO)FormUtils.getFormValues(frmDocsUpload,this, mapping, request);
			String preAuth_Seq_ID	=	(String)request.getSession().getAttribute("preAuth_seq_id_docs_uploads");
			preAuthDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
			ArrayList alLinkDetailsList=null;
			TableData tableDataDocsUpload = null;
			if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
			{
				tableDataDocsUpload=(TableData)(request.getSession()).getAttribute("tableDataDocsUpload");
			}//end of if(request.getSession().getAttribute("tableDataMouCertificates")!=null)
			else
			{
				tableDataDocsUpload=new TableData();
			}//end of else
			preAuthDetailVO=(PreAuthDetailVO)FormUtils.getFormValues(frmDocsUpload,this, mapping, request);
			preAuthDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
			StringBuffer sbfCaption= new StringBuffer();
			sbfCaption=sbfCaption.append("- [ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
		
			int iUpdate	=	0;
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			Long lngFileData=null;
			String updated="";
			String strNotify="";
			String fileName="";
			String origFileName	=	"";
			Pattern pattern =null;
			Matcher matcher =null;
			FileOutputStream outputStream = null;
			FormFile formFile = null;
			int fileSize=2*1024*1024;
			TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
			DateFormat df =new SimpleDateFormat("ddMMyyyy HH:mm:ss:S");
			df.setTimeZone(tz);  
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			preAuthDetailVO =(PreAuthDetailVO)FormUtils.getFormValues(frmDocsUpload, "frmDocsUpload",this, mapping, request);
			//Get the FormFile object from ActionForm.
			StringBuffer strCaption=new StringBuffer();
			ArrayList<Object> alFileAUploadList = new ArrayList<Object>();
			alFileAUploadList.add(TTKCommon.getUserSeqId(request));//0
			Long userSeqId	=	(Long) TTKCommon.getUserSeqId(request);
			String preAuth_seq_id_docs_uploads	=	(String)request.getSession().getAttribute("preAuth_seq_id_docs_uploads");
			Long preAuthSeqID	=	preAuthDetailVO.getPreAuthSeqID();
			formFile = (FormFile)frmDocsUpload.get("file");
			String fileDesc	=	(String)frmDocsUpload.get("description");
			String sourceid	=	(String)frmDocsUpload.get("source_id");
			pattern = Pattern.compile( "[^a-zA-Z0-9_\\.\\-\\s*]" );
			matcher = pattern.matcher( formFile.getFileName() );
			String 	strTimeStamp=((df.format(Calendar.getInstance(tz).getTime())).replaceAll(" ", "_")).replaceAll(":", ""); 
			fileName=strTimeStamp+"_"+formFile.getFileName();
			origFileName	=	formFile.getFileName();
			InputStream inputStream	=	 formFile.getInputStream();//INPUT STREAM  FROM FILE UPLOADED
			int formFileSize	=	formFile.getFileSize();
			/*if(!matcher.find())
			{*/
				preAuthDetailVO.setDocName(fileName);

				alFileAUploadList.add(fileName);//1
				alFileAUploadList.add(fileDesc);//2
				alFileAUploadList.add(preAuth_Seq_ID);//3
				alFileAUploadList.add(sourceid);//4
				
				//	alFileDetails=this.populateSearchCriteria(webconfigInsCompInfoVO);
				String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("preauthDocsUploads"));
				if(path.equals(""))	{
					path="D:\\\\home\\\\jboss\\\\jboss-as-7.1.1.Final\\\\bin\\\\preauthDocUpload\\\\";
				}//
				File folder = new File(path);
				if(!folder.exists()){
					folder.mkdir();
				}
				String finalPath=(path+fileName);
				String strFileExt=formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1,formFile.getFileName().length());

				if(formFile.getFileSize()<=fileSize)
				{				
					if((strFileExt.equalsIgnoreCase("pdf"))   || (strFileExt.equalsIgnoreCase("doc")) 
                            || (strFileExt.equalsIgnoreCase("docx")) || (strFileExt.equalsIgnoreCase("xls"))   
                            || (strFileExt.equalsIgnoreCase("xlsx")))
					{ 
							outputStream = new FileOutputStream(new File(finalPath));
							outputStream.write(formFile.getFileData());
							alFileAUploadList.add(finalPath);//3 This code adds Total path of the file      
							iUpdate=preAuthObject.saveDocUploads(preAuthDetailVO,alFileAUploadList,userSeqId,preAuth_Seq_ID,origFileName,inputStream,formFileSize);
							if(iUpdate>0)
							{
								if(frmDocsUpload.get("mouDocSeqID")!=null)
								{
									//set the appropriate message
									request.setAttribute("updated","message.savedSuccessfully");
								}//end of if(frmDocsUpload.get("configLinkSeqID")!=null)
								else
								{
									//set the appropriate message
									request.setAttribute("updated","message.addedSuccessfully");
								}//end else
								frmDocsUpload.initialize(mapping);
								alLinkDetailsList=preAuthObject.getPreauthDocsUploadsList(preAuth_seq_id_docs_uploads);
								tableDataDocsUpload.setData(alLinkDetailsList);
								frmDocsUpload.set("caption",sbfCaption.toString());
								request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
								request.setAttribute("frmDocsUpload",frmDocsUpload);
							}// end of if(iUpdate>0)
							
							frmDocsUpload = (DynaActionForm)FormUtils.setFormValues("frmDocsUpload",preAuthDetailVO,this,mapping,request);					
					}//end of if(strFileExt.equalsIgnoreCase("html") || strFileExt.equalsIgnoreCase("mhtml")||strFileExt.equalsIgnoreCase("msg") || strFileExt.equalsIgnoreCase("rar")|| strFileExt.equalsIgnoreCase("zip") || strFileExt.equalsIgnoreCase("pdf") || strFileExt.equalsIgnoreCase("doc") || strFileExt.equalsIgnoreCase("docx") || strFileExt.equalsIgnoreCase("txt") || strFileExt.equalsIgnoreCase("odt") || strFileExt.equalsIgnoreCase("jrxml") || strFileExt.equalsIgnoreCase("xls") || strFileExt.equalsIgnoreCase("xlsx") )
					else{
						strNotify="selected file should be any of these extensions (.pdf,.doc,.docx,.xls,.xlsx)";
						frmDocsUpload = (DynaActionForm)FormUtils.setFormValues("frmDocsUpload",preAuthDetailVO,this,mapping,request);
					}//end ofelse(strFileExt.equalsIgnoreCase("pdf"))
				}//end of if(formFile.getFileSize()<=fileSize)
				else{
					strNotify="selected file size  Shold be less than or equal to 2 MB";
				}//end of else part if(formFile.getFileSize()<=fileSize)
			/*}//end of if(!matcher.find())
			else{
				//updated="selected "+formFile.getFileName()+" file is not in pdf format,Please select pdf file";
				strNotify="selected "+formFile.getFileName()+" file Should not have Special Characters like }!@#$%^&amp;*(])[{+";
			}	*/	
			request.getSession().setAttribute("frmUpload", frmDocsUpload);
			request.setAttribute("notify",strNotify);	
			return mapping.findForward(strDocsUpload);
		}//end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strUpload));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				//HttpServletResponse response)
	
	
	/**
	 * This method is used to navigate to previous screen when close  button is clicked.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doUploadClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException
	{
		try
		{
			log.debug("Inside the doClose method of UploadMOUDocsAction");
			setLinks(request);
			return mapping.findForward(strPreauthUploadClose);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strUpload));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				//HttpServletResponse response)
	
	
	/**
	 * This method is used to delete selected record(s)from the db
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
	
			try{
				if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
				{
				setLinks(request);
				log.debug("Inside PreauthAction doDelete");
				StringBuffer sbfDeleteId = new StringBuffer();
				ArrayList <Object>alCertificateDelete=new ArrayList<Object>();
				ArrayList <Object>alAssociatedCertificatesList=null;
				DynaActionForm frmDocsUpload=(DynaActionForm)form;
				StringBuffer sbfCaption= new StringBuffer();
				String strForwardPath=strDocsdelete;
				sbfCaption = sbfCaption.append(this.buildCaption(request));
				PreAuthManager preAuthObject=this.getPreAuthManagerObject();
				TableData tableDataDocsUpload = null;
				//get the Table Data From the session 
				if(request.getSession().getAttribute("tableDataDocsUpload")!=null)
				{
					tableDataDocsUpload=(TableData)(request.getSession()).getAttribute("tableDataDocsUpload");
				}//end of if(request.getSession().getAttribute("tableDataLinkDetails")!=null)
				else
				{
					tableDataDocsUpload=new TableData();
				}//end of else
				sbfDeleteId.append(populateDeleteIds(request, (TableData)request.getSession().getAttribute("tableDataDocsUpload")));
				alCertificateDelete.add(String.valueOf(sbfDeleteId));
				frmDocsUpload.initialize(mapping);
				int iCount=preAuthObject.deleteDocsUpload(alCertificateDelete,"tableDataDocsUpload");
				log.info("iCount :"+iCount);
				//refresh the data in order to get the new records if any.
				
				preAuthObject=this.getPreAuthManagerObject();
				//DynaActionForm frmPreAuthGeneral=null;
				String preAuthSeqID	=	(String)request.getSession().getAttribute("preAuth_seq_id_docs_uploads");
				frmDocsUpload=(DynaActionForm) request.getSession().getAttribute("frmDocsUpload");
				alAssociatedCertificatesList = preAuthObject.getPreauthDocsUploadsList(preAuthSeqID);
				tableDataDocsUpload.setData(alAssociatedCertificatesList,"search");
				request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
				request.setAttribute("frmDocsUpload",frmDocsUpload);
				
				}//End of if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
				return mapping.findForward(strDocsUpload);
				}//end of try
				catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,strUpload));
				}//end of catch(Exception exp)
			}//end of doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	
	
	
	/**
	 * This method  prepares the Caption based on the flow and retunrs it
	 * @param request current HttpRequest
	 * @return String caption built
	 * @throws TTKException
	 */
	private String buildCaption(HttpServletRequest request)throws TTKException
	{
		StringBuffer sbfCaption=new StringBuffer();
		sbfCaption.append(request.getSession().getAttribute("ConfigurationTitle"));
		return sbfCaption.toString();
	}//end of buildCaption(HttpServletRequest request)
	
	/**
	 * Returns a string which contains the | separated sequence id's to be deleted
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 * @param tableData TableData object which contains the value objects
	 * @return String which contains the | separated sequence id's to be delete
	 * @throws TTKException If any run time Excepton occures
	 */
	private String populateDeleteIds(HttpServletRequest request, TableData tableDataDocsUpload)throws TTKException
	{
		if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
		{
			String[] strChk = request.getParameterValues("chkopt");
			StringBuffer sbfDeleteId = new StringBuffer();
			if(strChk!=null&&strChk.length!=0)
			{
				for(int i=0; i<strChk.length;i++)
				{
					if(strChk[i]!=null)
					{
						//extract the sequence id to be deleted from the value object
						if(i == 0)
						{
							sbfDeleteId.append("|").append(String.valueOf(((PreAuthDetailVO)tableDataDocsUpload.getData().get(Integer.parseInt(strChk[i]))).getMouDocSeqID()));
						}//end of if(i == 0)
						else
						{
							sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((PreAuthDetailVO)tableDataDocsUpload.getData().get(Integer.parseInt(strChk[i]))).getMouDocSeqID().intValue()));
						}//end of else
					}//end of if(strChk[i]!=null)
				}//end of for(int i=0; i<strChk.length;i++)
				sbfDeleteId=sbfDeleteId.append("|");
			}//end of if(strChk!=null&&strChk.length!=0)
			return String.valueOf(sbfDeleteId);
		}//end of populateDeleteId(HttpServletRequest request, TableData tableData)		
	else{
		
		String[] strChk = request.getParameterValues("chkopt");
		StringBuffer sbfDeleteId = new StringBuffer();
		if(strChk!=null&&strChk.length!=0)
		{
			for(int i=0; i<strChk.length;i++)
			{
				if(strChk[i]!=null)
				{
					//extract the sequence id to be deleted from the value object
					if(i == 0)
					{
						sbfDeleteId.append("|").append(String.valueOf(((TdsCertificateVO)tableDataDocsUpload.getData().
								get(Integer.parseInt(strChk[i]))).getCertSeqId()));
					}//end of if(i == 0)
					else
					{
						sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((TdsCertificateVO)tableDataDocsUpload.getData().
								get(Integer.parseInt(strChk[i]))).getCertSeqId().intValue()));
					}//end of else
				}//end of if(strChk[i]!=null)
			}//end of for(int i=0; i<strChk.length;i++)
			sbfDeleteId=sbfDeleteId.append("|");
		}//end of if(strChk!=null&&strChk.length!=0)
		return String.valueOf(sbfDeleteId);
	}//end of populateDeleteId(HttpServletRequest request, TableData tableData)

}//else
	public ActionForward doRangeChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
       try{
                setLinks(request);
                log.debug("Inside PreAuthAction doStatusChange");
               
              return this.getForward(strPreauthlist, mapping, request);
         }//end of try
         catch(TTKException expTTK)
        {
         return this.processExceptions(request, mapping, expTTK);
          }//end of catch(TTKException expTTK)
       catch(Exception exp)
         {
          return this.processExceptions(request, mapping, new TTKException(exp,strPreauthEnhancementList));
           }//end of catch(Exception exp)
     }//end of doDefaultEnhancement(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
}//end of PreAuthAction