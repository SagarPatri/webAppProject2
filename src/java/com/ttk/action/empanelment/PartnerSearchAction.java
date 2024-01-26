/**
 * @ (#) HospitalSearchAction.java Sep 20, 2005
 * Project       : TTK HealthCare Services
 * File          : HospitalSearchAction.java
 * Author        : Srikanth H M
 * Company       : Span Systems Corporation
 * Date Created  : Sep 20, 2005
 *
 * @author       : Srikanth H M
 * Modified by   : Ramakrishna K M,Swaroop Kaushik D.S
 * Modified date : May 01, 2007
 * Reason        : Changes to Dispatch Action,added doConfiguration();
 */

package com.ttk.action.empanelment;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.nt.NTEventLogAppender;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.empanelment.ContactManager;
import com.ttk.business.empanelment.PartnerManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.common.security.Cache;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.DocumentDetailVO;
import com.ttk.dto.empanelment.PartnerAuditVO;
import com.ttk.dto.empanelment.PartnerDetailVO;
import com.ttk.dto.empanelment.PartnerVO;
import com.ttk.dto.empanelment.NetworkTypeVO;
import com.ttk.dto.empanelment.PreRequisiteVO;
import com.ttk.dto.usermanagement.ContactVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching the hospitals and add/edit the hospital information.
 * This class is also used to view discrepancies if any.
 */

public class PartnerSearchAction extends TTKAction {
    private static Logger log = Logger.getLogger( PartnerSearchAction.class );

    //   Modes.
    private static final String strBackward="Backward";
    private static final String strForward="Forward";

    //  Action mapping forwards.
    private static final String strPartnerlist="partnerlist";
    private static final String strEditpartner="editpartner";
    private static final String strFrwdDiscrepancy="discrepancy";
    private static final String strConfig="configuration";

    //Exception Message Identifier
    private static final String strPtnrSearch="partnersearch";

    //PreRequisite for intx 
   /* private static final String strHospitalPreRequisite="hospitalPreRequisite";
    private static final String strProviderDashBoard	=	"providerdashboard";
    private static final String strPreRequisiteLinks	=	"prerequisitelinks";
    private static final String strTotolProviders		=	"totalproviderslist";
    private static final String strAddNetworks			=	"addNetworkTypes";*/
    

	private static final String DynaActionForm = null;
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
    		log.debug("Inside the doDefault method of PartnerSearchAction");
    		setLinks(request);
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		TableData tableData = TTKCommon.getTableData(request);
    		String strDefaultBranchID  = String.valueOf(((UserSecurityProfile)request.getSession().
    				getAttribute("UserSecurityProfile")).getBranchID());
    		DynaActionForm generalForm = (DynaActionForm)form;
    		//create new table data object
    		tableData = new TableData();
    		//create the required grid table
    		tableData.createTableInfo("PartnerSearchTable",new ArrayList());
    		request.getSession().setAttribute("tableData",tableData);
    		request.getSession().setAttribute("alSubStatus",null);
    		generalForm.set("officeInfo",strDefaultBranchID);
    		TTKCommon.documentViewer(request);
    		return this.getForward(strPartnerlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
		}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
    
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
    		setLinks(request);
    		log.debug("Inside the doSearch method of PartnerSearchAction");
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		PartnerManager partnerObject=this.getPartnerManagerObject();
    		TableData tableData = TTKCommon.getTableData(request);
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{
    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward("partnerlist"));
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
    			tableData.createTableInfo("PartnerSearchTable",null);
    			//fetch the data from the data access layer and set the data to table object
    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form));
    			tableData.modifySearchData("search");
    		}//end of else
    		ArrayList alPartner=partnerObject.getPartnerList(tableData.getSearchData());
    		tableData.setData(alPartner, "search");
    		request.getSession().setAttribute("tableData",tableData);
    		//finally return to the grid screen
    		return this.getForward(strPartnerlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
		}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    /**
     * Returns the HospitalManager session object for invoking methods on it.
     * @return HospitalManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private PartnerManager getPartnerManagerObject() throws TTKException
    {
        PartnerManager ptnrManager = null;
        try
        {
            if(ptnrManager == null)
            {
                InitialContext ctx = new InitialContext();
                ptnrManager = (PartnerManager) ctx.lookup("java:global/TTKServices/business.ejb3/PartnerManagerBean!com.ttk.business.empanelment.PartnerManager");
            }//end if(hospManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp,strPtnrSearch);
        }//end of catch
        return ptnrManager;
    }//end getHospitalManagerObject()
    
    /**
     * Returns the ArrayList which contains the populated search criteria elements
     * @param frmSearchHospital DynaActionForm will contains the values of corresponding fields
     * @param request HttpServletRequest object which contains the search parameter that is built
     * @return alSearchParams ArrayList
     */
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmSearchPartner)
    {
        //build the column names along with their values to be searched
    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(new SearchCriteria("EMPANELED_DATE", (String)frmSearchPartner.get("sEmpanelDate")));
		//  
		alSearchParams.add(new SearchCriteria("PARTNER_NAME",TTKCommon.replaceSingleQots((String)
				frmSearchPartner.get("sPartnerName"))));
    	
    	
       /* ArrayList<Object> alSearchParams = new ArrayList<Object>();
        alSearchParams.add(new SearchCriteria("EMPANEL_NUMBER",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("sEmpanelmentNo"))));
        alSearchParams.add(new SearchCriteria("HOSP_NAME",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("sHospitalName"))));
        alSearchParams.add(new SearchCriteria("OFF_INFO.TPA_OFFICE_SEQ_ID",
        		(String)frmSearchHospital.get("officeInfo"),"equals"));
        alSearchParams.add(new SearchCriteria("STATE_TYPE_ID", (String)frmSearchHospital.get("stateCode")));
        alSearchParams.add(new SearchCriteria("CITY_TYPE_ID", (String)frmSearchHospital.get("cityCode"),"equals"));
        alSearchParams.add(new SearchCriteria("STATUS_CODE.EMPANEL_STATUS_TYPE_ID",
        		(String)frmSearchHospital.get("empanelStatusCode")));
        alSearchParams.add(new SearchCriteria("GRAD.APPROVED_GRADE_TYPE_ID",
        		(String)frmSearchHospital.get("gradeCode")));
        alSearchParams.add(new SearchCriteria("RSON_CODE.EMPANEL_RSON_TYPE_ID",
        		(String)frmSearchHospital.get("subStatus"))); 
        alSearchParams.add(new SearchCriteria("HOSP_LICENC_NUMB",
                		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("sDHAID"))));//DHA ID,added for INtX

        alSearchParams.add(new SearchCriteria("tcc.country_id",
                		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("countryCode"))));//added for INtX
        alSearchParams.add(new SearchCriteria("dpt.provider_type_id",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("providerTypeId"))));//added for INtX
        alSearchParams.add(new SearchCriteria("primary_network",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("networkTypeId"))));//added for INtX
*/        
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchHospital)

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
    		log.debug("Inside the doView method of PartnerSearchAction");
    		String strCaption="";
    		HashMap hmCityList = null;
			ArrayList alCityList = new ArrayList();
			PartnerVO partnerVO=null;
			PartnerDetailVO partnerDetailVO=null;
			PartnerManager partnerObject=this.getPartnerManagerObject();
			TableData tableData = TTKCommon.getTableData(request);
            if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))

            DynaActionForm generalForm = (DynaActionForm)form;
            if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            {
            	partnerVO = (PartnerVO)tableData.getRowInfo(Integer.parseInt((String)(generalForm).get("rownum")));
                //add the selected item to the web board and make it as default selected
                this.addToWebBoard(partnerVO, request);
                //calling business layer to get the hospital detail
                partnerDetailVO =  partnerObject.getPartnerDetail(partnerVO.getPtnrSeqId());
                generalForm.initialize(mapping);
                strCaption="Edit";
                //Add the request object to DocumentViewer
                TTKCommon.documentViewer(request);
            }//end if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            else if(TTKCommon.getWebBoardId(request) != null)//take the hospital_seq_id from web board
            {
                //if web board id is found, set it as current web board id
            	partnerVO = new PartnerVO();
            	partnerVO.setPtnrSeqId(TTKCommon.getWebBoardId(request));
                //calling business layer to get the hospital detail
            	partnerDetailVO = partnerObject.getPartnerDetail(partnerVO.getPtnrSeqId());
                strCaption="Edit";
                //Add the request object to DocumentViewer
                TTKCommon.documentViewer(request);
            }//end of else if(TTKCommon.getWebBoardId(request) != null)
            else
            {
            	
                TTKException expTTK = new TTKException();
                expTTK.setMessage("error.partner.required");
                throw expTTK;
            	
            }//end of if(strMode.equals("EditHospital"))
           
            hmCityList=partnerObject.getCityInfo(partnerDetailVO.getAddressVO().getStateCode());
            String countryCode	=	(String)(hmCityList.get("CountryId"));          
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(partnerDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)
            DynaActionForm partnerForm = setFormValues(partnerDetailVO,mapping,request);
            partnerForm.set("caption",strCaption);
            partnerForm.set("alCityList",alCityList);
            partnerDetailVO.getAddressVO().setCountryCode(countryCode);
            request.getSession().setAttribute("frmAddPartner",partnerForm);
            request.getSession().setAttribute("refDetPartnerName",partnerDetailVO.getPartnerName());
            request.getSession().setAttribute("AuthLicenseNo", partnerDetailVO.getIrdaNumber()); 
            request.getSession().setAttribute("PtnrDetails",partnerDetailVO);
            return this.getForward(strEditpartner, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
		}//end of catch(Exception exp)
    }//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
/**
     * Adds the selected item to the web board and makes it as the selected item in the web board
     * @param hospitalVO HospitalVO object which contain the information of hospital
     * @param request HttpServletRequest object which contains information about the selected check boxes
     */
    private void addToWebBoard(PartnerVO partnerVO, HttpServletRequest request)
    {
        Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
        CacheObject cacheObject = new CacheObject();
        cacheObject.setCacheId(""+partnerVO.getPtnrSeqId());
        cacheObject.setCacheDesc(partnerVO.getPartnerName());
        ArrayList<Object> alCacheObject = new ArrayList<Object>();
        alCacheObject.add(cacheObject);
        //if the object(s) are added to the web board, set the current web board id
        toolbar.getWebBoard().addToWebBoardList(alCacheObject);
        toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());
        //set weboardinvoked as true to avoid change of webboard id twice in same request
        request.setAttribute("webboardinvoked","true");
    }//end of addToWebBoard(HospitalVO hospitalVO, HttpServletRequest request)

    
    /**
     * Populates the value object to form element.
     * @param hospDetailVO HospitalDetailVO object.
     * @param mapping The ActionMapping used to select this instance.
     * @param request HttpServletRequest  object which contains hospital information.
     * @return DynaActionForm object.
     */
    private DynaActionForm setFormValues(PartnerDetailVO partnerDetailVO,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
        try
        {
            DynaActionForm partnerForm = (DynaActionForm)FormUtils.setFormValues("frmAddPartner",
            		partnerDetailVO,this,mapping,request);
            if(partnerDetailVO.getAddressVO()!=null)
            {
            	partnerForm.set("addressVO", (DynaActionForm)FormUtils.setFormValues("frmPartnerAddress",
            			partnerDetailVO.getAddressVO(),this,mapping,request));
            }//end of if(hospitalDetailVO.getAddressVO()!=null)
            else
            {
            	partnerForm.set("addressVO", (DynaActionForm)FormUtils.setFormValues("frmPartnerAddress",
                		new AddressVO(),this,mapping,request));
            }//end of else
            //partnerForm.set("partnerAuditVO", (DynaActionForm)FormUtils.setFormValues("frmPartnerAuditVO",
            	//	new PartnerAuditVO(),this,mapping,request));
            if(partnerDetailVO.getDocumentDetailVO()!=null)
            {
            	partnerForm.set("documentDetailVO", (DynaActionForm)FormUtils.setFormValues("frmDocumentDetailVO",
            			partnerDetailVO.getDocumentDetailVO(),this,mapping,request));
            }//end of if(hospitalDetailVO.getDocumentDetailVO()!=null)
            else
            {
            	partnerForm.set("documentDetailVO", (DynaActionForm)FormUtils.setFormValues("frmDocumentDetailVO",
                		new DocumentDetailVO(),this,mapping,request));
            }//end of else
            //if change in empanelled hospital name found, for capturing the reference detail informaiton
            if(partnerDetailVO.getEmplStatusTypeId()!=null && partnerDetailVO.getEmplStatusTypeId().
            		equalsIgnoreCase("EMP"))
            {
                //set the hospital name, pan number and category to session
                request.getSession().setAttribute("flgPtnrName",partnerDetailVO.getPartnerName());
                request.getSession().setAttribute("flgPanNumber",partnerDetailVO.getPanNmbr());
                request.getSession().setAttribute("flgCategory",partnerDetailVO.getCategoryID());
            }//end of if(hospitalDetailVO.getEmplStatusTypeId()!=null && hospitalDetailVO.getEmplStatusTypeId().equalsIgnoreCase("EMP"))
            return partnerForm;
        }
        catch(Exception exp)
        {
            throw new TTKException(exp,strPtnrSearch);
        }//end of catch
    }//end of setFormValues(HospitalDetailVO hospitalDetailVO,ActionMapping mapping,HttpServletRequest request)

    /**
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
    public ActionForward doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doAdd method of PartnerSearchAction");
			PartnerDetailVO partnerDetailVO=null;
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().
													 getAttribute("UserSecurityProfile");
			ArrayList alCityList = new ArrayList();
			partnerDetailVO = new PartnerDetailVO();
    		AddressVO addressVO	=	new AddressVO();
    		addressVO.setStateCode("DOH");//SETTING DUBAI AS DEFAULT
    		partnerDetailVO.setAddressVO(addressVO);
            DynaActionForm partnerForm = setFormValues(partnerDetailVO,mapping,request);
            if(userSecurityProfile.getBranchID()!=null){
            	partnerForm.set("tpaOfficeSeqId", userSecurityProfile.getBranchID().toString());
            }//end of if(userSecurityProfile.getBranchID()!=null)
            else{
            	partnerForm.set("tpaOfficeSeqId", "");
            }//end of else
            
            
            //GETTING ALL PAYERS LIST TO ASSOCIATE DEFAULT
            PartnerManager partnerObject=this.getPartnerManagerObject();
           // String strPayersList	=	partnerObject.getAllPayersList();
            //GETTING ALL DUBAI AREAS AS WE SHOWING DUBAI AND UAE AS DEFAULT
            HashMap hmCityList = null;
            hmCityList=partnerObject.getCityInfo("DOH");
            String countryCode	=	(String)(hmCityList.get("CountryId"));
            String isdcode	=	(String)(hmCityList.get("isdcode"));
            String stdcode	=	(String)(hmCityList.get("stdcode"));
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(partnerDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)

            if(alCityList==null){
            	alCityList=new ArrayList();
            }//end of if(alCityList==null)
            
            
			partnerForm.set("alCityList",alCityList);
			//partnerForm.set("countryCode", countryCode);//SETTING COUNTRY AS DEFAULT
			partnerForm.set("isdCode",isdcode);//SETTING ISD AS DEFAULT
			partnerForm.set("stdCode",stdcode);//SETTING STD AS DEFAULT
			partnerForm.set("isdCode1",isdcode);
			partnerForm.set("stdCode1",stdcode);
		//	partnerForm.set("payerCodes", strPayersList);
			//request.setAttribute("stdCodes","stdCodes");
			partnerForm.set("caption","Add");
            request.getSession().setAttribute("frmAddPartner",partnerForm);
            return this.getForward(strEditpartner, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
		}//end of catch(Exception exp)
    }//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
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
    		//  

    		setLinks(request);
    		log.debug("Inside the doBackward method of PartnerSearchAction");
			TableData tableData = TTKCommon.getTableData(request);
			PartnerManager partnerObject=this.getPartnerManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alPartner = partnerObject.getPartnerList(tableData.getSearchData());
    		tableData.setData(alPartner, strBackward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
    		return this.getForward(strPartnerlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
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
    		//  
    		setLinks(request);
    		log.debug("Inside the doForward method of PartnerSearchAction");
			TableData tableData = TTKCommon.getTableData(request);
			PartnerManager partnerObject=this.getPartnerManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alPartner = partnerObject.getPartnerList(tableData.getSearchData());
    		tableData.setData(alPartner, strForward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
    		return this.getForward(strPartnerlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
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
    		setLinks(request);
    		log.debug("Inside the doStatusChange method of HospitalSearchAction");
    		this.populateWebBoard(request);
            return this.getForward(strPartnerlist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
		}//end of catch(Exception exp)
    }//end of doCopyToWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
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
            //loop through to populate delete sequence id's and get the value from session for the matching check box value
            for(int i=0; i<strChk.length;i++)
            {
                cacheObject = new CacheObject();
                cacheObject.setCacheId(""+((PartnerVO)tableData.getData().
                		get(Integer.parseInt(strChk[i]))).getPtnrSeqId());
                cacheObject.setCacheDesc(((PartnerVO)tableData.getData().
                		get(Integer.parseInt(strChk[i]))).getPartnerName());
                alCacheObject.add(cacheObject);
            }//end of for(int i=0; i<strChk.length;i++)
        }//end of if(strChk!=null&&strChk.length!=0)
        if(toolbar != null)
        {
            toolbar.getWebBoard().addToWebBoardList(alCacheObject);
        }//end of if(toolbar != null)
    }//end of populateWebBoard(HttpServletRequest request)

    /**
     * Populates the form element to value object .
     * @param hospDetailVO HospitalDetailVO object.
     * @param mapping The ActionMapping used to select this instance.
     * @param request HttpServletRequest  object which contains hospital information.
     * @return hospDetailVO HospitalDetailVO object.
     */
    private PartnerDetailVO getFormValues(DynaActionForm generalForm,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
        try
        {
        	PartnerDetailVO ptnrDetailVO =null;
            AddressVO addressVO = null;
            PartnerAuditVO partnerAuditVO=null;
            DocumentDetailVO docuDetailVO = null;
            ptnrDetailVO = (PartnerDetailVO)FormUtils.getFormValues(generalForm,"frmAddPartner",this,mapping,request);
            
           //  
            
            ActionForm addressForm=(ActionForm)generalForm.get("addressVO");
         //   ActionForm partnerAuditForm=(ActionForm)generalForm.get("partnerAuditVO");
            ActionForm documentDetailForm=(ActionForm)generalForm.get("documentDetailVO");
            addressVO=(AddressVO)FormUtils.getFormValues(addressForm,"frmPartnerAddress",this,mapping,request);
            docuDetailVO=(DocumentDetailVO)FormUtils.getFormValues(documentDetailForm,"frmDocumentDetailVO",
            			 this,mapping,request);
            //To bring country based on state selection along with cities
            String stateCode	=	addressVO.getStateCode();
            request.getSession().setAttribute("stateCode", stateCode);
            
            ptnrDetailVO.setAddressVO(addressVO);
       //     partnerAuditVO=(PartnerAuditVO)FormUtils.getFormValues(partnerAuditForm,"frmPartnerAuditVO",
            			//	 this,mapping,request);
            ptnrDetailVO.setPartnerAuditVO(partnerAuditVO);
            ptnrDetailVO.setDocumentDetailVO(docuDetailVO);
            ptnrDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));////User Id
            if(generalForm.get("emplStatusTypeId")!=null && generalForm.
            		get("emplStatusTypeId").toString().equalsIgnoreCase("EMP"))
            {
            	//set the hospital name, pan number and category to session
                request.getSession().setAttribute("flgPtnrName",generalForm.get("partnerName"));
                request.getSession().setAttribute("flgPanNumber",generalForm.get("panNmbr"));
                request.getSession().setAttribute("flgCategory",generalForm.get("categoryID"));
            }//end of if(generalForm.get("emplStatusTypeId")!=null && generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("EMP"))
            return ptnrDetailVO;
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp,strPtnrSearch);
        }//end of catch
    }//end of getFormValues(DynaActionForm generalForm,ActionMapping mapping,HttpServletRequest request)

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
    		log.debug("Inside the doSave method of PartnerSearchAction");
    		com.ttk.common.security.Cache.refresh("PartnerList");
			HashMap hmCityList = null;
			ArrayList alCityList = new ArrayList();
			PartnerVO partnerVO=null;
			PartnerDetailVO partnerDetailVO=null;
			PartnerManager partnerObject=this.getPartnerManagerObject();
            DynaActionForm generalForm = (DynaActionForm)form;
              Long  lngPtnrSeqId=TTKCommon.getLong(TTKCommon.checkNull((String)generalForm.get("ptnrSeqId")));
           /*    if(lngPtnrSeqId!=null)
            {
                if((request.getSession().getAttribute("flgPtnrName")!=null))
                {
                    if((!request.getSession().getAttribute("flgPtnrName").equals(generalForm.get("partnerName")) ||
                    	!request.getSession().getAttribute("flgPanNumber").equals(generalForm.get("panNmbr")) ||
                    	!request.getSession().getAttribute("flgCategory").equals(generalForm.get("categoryID")))
                    		&& (generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("EMP") || generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("REN")))
                        {
                    		//to make server side validation for Reference Details Screen
                            generalForm.set("flagValidate","true");
                            generalForm.set("partnerAuditVO", (DynaActionForm)FormUtils.
                            		setFormValues("frmPartnerAuditVO",new PartnerAuditVO(),this,mapping,request));
                            generalForm.set("refdetcaption",request.getSession().getAttribute("refDetPartnerName"));
                            return this.getForward("referencedetail", mapping, request);
                        }//if(!request.getSession().getAttribute("flgPtnrName").equals(generalForm.get("hospitalName"))&&generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("EMP"))
                }//end of if((request.getSession().getAttribute("flgPtnrName")!=null))
            }//end of if(lPtnrSeqId!=null)
            */   
             partnerDetailVO = getFormValues(generalForm,mapping,request);
			
			//getting the partner name and splitting the qha ID from name to save
			String tempPtnrname	=	partnerDetailVO.getPartnerName();
			String isd1 = partnerDetailVO.getIsdCode1();
			String std1 = partnerDetailVO.getStdCode1();
			String secondatryPh = partnerDetailVO.getOfficePhone2();
			if("Phone No".equals(secondatryPh))
				partnerDetailVO.setOfficePhone2("");
			
			if("ISD".equals(isd1))
				partnerDetailVO.setIsdCode1("");
			
			if("STD".equals(std1))
				partnerDetailVO.setStdCode1("");
			
			if(tempPtnrname.indexOf('[')>0)
			{
				tempPtnrname	=	tempPtnrname.substring(0, tempPtnrname.indexOf('['));
			}
			partnerDetailVO.setPartnerName(tempPtnrname);
			
			String comments = partnerDetailVO.getPartnerComments();
			if(comments==""||comments==null){
				partnerDetailVO.setPartnerComments("<p> </p><p> </p><p> </p><p> </p><p> </p>");
			}
			else{
				partnerDetailVO.setPartnerComments(comments);
			}
			
            //update the partner details to data base
				lngPtnrSeqId = partnerObject.addUpdatePartner(partnerDetailVO);
            //set the appropriate message
                       if(lngPtnrSeqId > 0)
            {
                if(generalForm.get("ptnrSeqId")!=null && !generalForm.get("ptnrSeqId").equals(""))
                {
                    request.setAttribute("updated","message.savedSuccessfully");
                    request.setAttribute("cacheId", ""+lngPtnrSeqId);
                    request.setAttribute("cacheDesc", partnerDetailVO.getPartnerName());
                    //finally modify the web board details, if the hospital name is changed
                    TTKCommon.modifyWebBoardId(request);
                }//end of if(generalForm.get("hospSeqId")!=null && !generalForm.get("hospSeqId").equals(""))
                else
                {
                    request.setAttribute("updated","message.addedSuccessfully");
                    partnerVO=new PartnerVO();
                    partnerVO.setPtnrSeqId(lngPtnrSeqId);
                    partnerVO.setPartnerName((String)generalForm.get("partnerName"));
                    //add the details to web board
                    this.addToWebBoard(partnerVO, request);
                    //clear the form object in add mode
                }//end of else
            }//end of if(lngHospSeqId > 0)
            partnerDetailVO = partnerObject.getPartnerDetail(lngPtnrSeqId);
            generalForm.initialize(mapping);
            hmCityList=partnerObject.getCityInfo();
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(partnerDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)
            DynaActionForm partnerForm = setFormValues(partnerDetailVO,mapping,request);
            partnerForm.set("caption","Edit");
            partnerForm.set("alCityList",alCityList);
            request.getSession().setAttribute("PtnrDetails",partnerDetailVO);       
            request.getSession().setAttribute("frmAddPartner",partnerForm);
			Cache.refresh("PartnerList");
            return this.getForward(strEditpartner, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
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
    	log.debug("Inside doChangeWebBoard method of PartnerSearchAction");
    	//ChangeWebBoard method will call doView() method internally.
    	return doView(mapping,form,request,response);
    }//end of doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to load cities based on the selected state.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doChangeState(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doChangeState method of HospitalSearchAction");
			PartnerManager partnerObject=this.getPartnerManagerObject();
			HashMap hmCityList = null;
			ArrayList alCityList = new ArrayList();
			PartnerDetailVO partnerDetailVO=null;
    		DynaActionForm hospitalForm=(DynaActionForm)form;
    		partnerDetailVO = getFormValues(hospitalForm,mapping,request);
    		
    		String stateCode	=	"";
            stateCode	=	(String) request.getSession().getAttribute("stateCode");
            //request.getSession().setAttribute(stateCode, "stateCode");
            hmCityList=partnerObject.getCityInfo(stateCode);
            String countryCode	=	(String)(hmCityList.get("CountryId"));
            String isdcode	=	(String)(hmCityList.get("isdcode"));
            String stdcode	=	(String)(hmCityList.get("stdcode"));
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(partnerDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)

            if(alCityList==null){
            	alCityList=new ArrayList();
            }//end of if(alCityList==null)

            hospitalForm.set("frmChanged","changed");
            hospitalForm.set("isdCode",isdcode);
            hospitalForm.set("stdCode",stdcode);
            hospitalForm.set("alCityList",alCityList);
            partnerDetailVO.getAddressVO().setCountryCode(countryCode);

            //request.setAttribute("stdCodes","stdCodes");
            return this.getForward(strEditpartner,mapping,request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPtnrSearch));
		}//end of catch(Exception exp)
    }//end of doChangeState(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


    
  /**
     * This method is used to load the sub status based on the selected status.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doStatusChange method of HospitalSearchAction");
			HospitalManager hospitalObject=this.getHospitalManagerObject();
    		DynaActionForm generalForm=(DynaActionForm)form;
            HashMap hmSubStatus = null;
            ArrayList alSubStatus = null;
            if(hmSubStatus==null)
            {
                hmSubStatus=hospitalObject.getReasonInfo();
            }//end of if(hmSubStatus==null)
            String StrEmpanelStatusCode=(String)generalForm.get("empanelStatusCode");
            alSubStatus=(ArrayList)hmSubStatus.get(StrEmpanelStatusCode);
            //setting the substatus into the request
            request.getSession().setAttribute("alSubStatus",alSubStatus);
            return this.getForward(strHospitallist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    */
 
 
/**
     * This method is used to add the reference information.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doReferenceDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doReferenceDetail method of HospitalSearchAction");
			HospitalManager hospitalObject=this.getHospitalManagerObject();
    		DynaActionForm generalForm = (DynaActionForm)form;
            HospitalDetailVO hospDetailVO = new HospitalDetailVO();
            hospDetailVO = getFormValues((DynaActionForm)form,mapping,request);
            hospitalObject.addUpdateHospital(hospDetailVO);
            //finally modify the web board details, if the hospital name is changed
            request.setAttribute("cacheId", ""+hospDetailVO.getHospSeqId());
            request.setAttribute("cacheDesc", hospDetailVO.getHospitalName());
            generalForm.set("flagValidate",null);//to make server side validation for Reference Details Screen
            TTKCommon.modifyWebBoardId(request);
            request.setAttribute("updated","message.savedSuccessfully");
            return this.getForward(strEdithospital, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doReferenceDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    *//**
     * This method is used to reload the screen when the reset button is pressed.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doReset method of HospitalSearchAction");
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().
													 getAttribute("UserSecurityProfile");
			HospitalVO hospitalVO=null;
			HospitalDetailVO hospitalDetailVO=null;
			ArrayList alCityList = new ArrayList();
			HashMap hmCityList = null;
			HospitalManager hospitalObject=this.getHospitalManagerObject();
    		DynaActionForm generalForm=(DynaActionForm)form;
            String strCaption = (String)generalForm.get("caption");
            hospitalDetailVO = new HospitalDetailVO();
            AddressVO addressVO=new AddressVO();
            hospitalDetailVO.setAddressVO(addressVO);
            if(generalForm.get("hospSeqId")!=null && !generalForm.get("hospSeqId").equals(""))
            {
                hospitalVO=new HospitalVO();
                hospitalVO.setHospSeqId(TTKCommon.getLong((String)generalForm.get("hospSeqId")));
                //calling business layer to get the hospital detail
                hospitalDetailVO = hospitalObject.getHospitalDetail(hospitalVO.getHospSeqId());
            }//end of if(!generalForm.get("hospSeqId").equals(""))
            generalForm.initialize(mapping);
            hmCityList=hospitalObject.getCityInfo();

            if((hmCityList!=null)&& hospitalDetailVO != null){
            	alCityList = (ArrayList)hmCityList.get(hospitalDetailVO.getAddressVO().getStateCode());
            }//end of if((hmCityList!=null)&& hospitalDetailVO != null)

            if(alCityList==null){
            	alCityList=new ArrayList();
            }//end of if(alCityList==null)

            DynaActionForm hospitalForm = setFormValues(hospitalDetailVO,mapping,request);
            if(userSecurityProfile.getBranchID()!=null){
            	hospitalForm.set("tpaOfficeSeqId", userSecurityProfile.getBranchID().toString());
            }//end of if(userSecurityProfile.getBranchID()!=null)
            else{
            	hospitalForm.set("tpaOfficeSeqId", "");
            }//end of else

            hospitalForm.set("caption",strCaption);
            hospitalForm.set("alCityList",alCityList);
            request.getSession().setAttribute("frmAddHospital",hospitalForm);
            return this.getForward(strEdithospital,mapping,request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    *//**
     * This method is used to navigate the user to discrepancy screen if any.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doDiscrepancy(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doDiscrepancy method of HospitalSearchAction");
			HospitalManager hospitalObject=this.getHospitalManagerObject();
			HospitalVO hospitalVO= new HospitalVO();
    		hospitalVO.setHospSeqId(TTKCommon.getWebBoardId(request));
            ArrayList alDiscrepancy =hospitalObject.getHospitalDiscrepancyList(hospitalVO);
            request.setAttribute("alDiscrepancy",alDiscrepancy);
            return this.getForward(strFrwdDiscrepancy, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doDiscrepancy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    *//**
     * This method is used to resolve the discrepancy if any.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doResolveDiscrepancy(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doResolveDiscrepancy method of HospitalSearchAction");
			HospitalVO hospitalVO=null;
			HospitalDetailVO hospitalDetailVO=null;
			HospitalManager hospitalObject=this.getHospitalManagerObject();
    		String strTarget=strHospitallist;
            DynaActionForm generalForm = (DynaActionForm)form;
            hospitalVO = new HospitalVO();
            hospitalVO.setHospSeqId(TTKCommon.getWebBoardId(request));
            String flagInvalidate=(String)generalForm.get("flagInvalidate");
            log.debug(" flagInvalidate :"+flagInvalidate);
            if(flagInvalidate.equals("Y"))
            {
                strTarget=strHospitallist;
                hospitalVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
                hospitalObject.updateDiscrepancyInfo("INVALIDATE",hospitalVO);
            }//end of if(flagInvalidate.equals("Y"))
            else
            {
                strTarget=strEdithospital;
                hospitalVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
                hospitalObject.updateDiscrepancyInfo("IGNORE",hospitalVO);
                hospitalDetailVO = hospitalObject.getHospitalDetail(hospitalVO.getHospSeqId());
                log.debug("hospitalDetailVO value is :"+hospitalDetailVO);
            }//end of if else if(flagInvalidate.equals("Y"))
            return this.getForward(strTarget, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doResolveDiscrepancy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    *//**
     * This method is used to navigate to previous screen when closed button is clicked.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside the doClose method of HospitalSearchAction");
    		DynaActionForm generalForm=(DynaActionForm)form;
            generalForm.set("flagValidate",null);
            request.getSession().setAttribute("frmAddHospital",generalForm);
            return this.getForward(strEdithospital, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    *//**
	 * This method is used to bring out the Configuration List screen
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 *//*	
	public ActionForward doConfiguration(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{
			log.debug("Inside the doConfiguration method of HospitalSearchAction");
			setLinks(request);				
			StringBuffer sbfCaption= new StringBuffer();	
			String strEmpanelNumber=null;
			DynaActionForm frmAddHospital = (DynaActionForm)request.getSession().getAttribute("frmAddHospital");
			sbfCaption.append("[").append(frmAddHospital.get("hospitalName")).append("]");	
			strEmpanelNumber=(String)frmAddHospital.get("emplNumber");
			request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
			request.getSession().setAttribute("EmpanelNumber",strEmpanelNumber);
			return this.getForward(strConfig, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
	}//end of doConfiguration(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//	HttpServletResponse response)
    	
  

   
    */


 /**
     * This method is used to getLicenceNumbers based on professional ID
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward getLicenceNumbers(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the getLicenceNumbers method of HospitalSearchAction");
            		setLinks(request);
            		ArrayList alProfessionals	=	null;
            		HospitalManager hospitalObject=this.getHospitalManagerObject();
            		String prviderId	=	request.getParameter("ProviderId");
            		String provName		=	request.getParameter("provName");
            		String strIdentifier=	request.getParameter("strIdentifier");
            		if("GetNameByLicence".equals(strIdentifier))
            		{
            			alProfessionals= hospitalObject.getLicenceNumbers(prviderId,provName,strIdentifier);
	            		PrintWriter out = response.getWriter();  
	        	        response.setContentType("text/xml");  
	        	        response.setHeader("Cache-Control", "no-cache");  
	        	        response.setStatus(HttpServletResponse.SC_OK);  
	        	        if(alProfessionals!=null)
	        	        	if(alProfessionals.get(0)!=null || alProfessionals.get(0)!="")
	        	        		out.write(alProfessionals.get(0)+"");
	        	        out.flush();  
            		}
            		else{
	            		alProfessionals= hospitalObject.getLicenceNumbers(prviderId,provName,strIdentifier);
	            		PrintWriter out = response.getWriter();  
	        	        response.setContentType("text/xml");  
	        	        response.setHeader("Cache-Control", "no-cache");  
	        	        response.setStatus(HttpServletResponse.SC_OK);  
	        	        if(alProfessionals!=null)
	        	        	if(alProfessionals.get(0)!=null || alProfessionals.get(0)!="")
	        	        		out.write(alProfessionals.get(0)+"");  
	        	        out.flush();  
            		}
            		return null;
            	}//end of try
            	catch(TTKException expTTK)
        		{
        			return this.processExceptions(request, mapping, expTTK);
        		}//end of catch(TTKException expTTK)
        		catch(Exception exp)
        		{
        			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
        		}//end of catch(Exception exp)
            }//end of getLicenceNumbers(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    *//**
     * This method is used to getLicenceNumbers based on professional ID
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward getLicenceNoForPreEmp(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the getLicenceNoForPreEmp method of HospitalSearchAction");
            		setLinks(request);
            		ArrayList alProfessionals	=	null;
            		HospitalManager hospitalObject=this.getHospitalManagerObject();
            		String provName		=	request.getParameter("provName");
            		alProfessionals= hospitalObject.getLicenceNoForPreEmp(provName);
            		PrintWriter out = response.getWriter();  
        	        response.setContentType("text/xml");  
        	        response.setHeader("Cache-Control", "no-cache");  
        	        response.setStatus(HttpServletResponse.SC_OK);  
        	        if(alProfessionals!=null)
        	        	if(alProfessionals.get(0)!=null || alProfessionals.get(0)!="")
        	        		out.write(alProfessionals.get(0)+"");  
        	        out.flush();  
            		
            		return null;
            	}//end of try
            	catch(TTKException expTTK)
        		{
        			return this.processExceptions(request, mapping, expTTK);
        		}//end of catch(TTKException expTTK)
        		catch(Exception exp)
        		{
        			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
        		}//end of catch(Exception exp)
            }//end of getLicenceNoForPreEmp(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    *//**
     * This method is used to GenerateIds based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doGenerateIds(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doGenerateIds method of HospitalSearchAction");
            		setOnlineLinks(request);
            		ArrayList alProfessionals	=	null;
            		
            		String strContactSeqId  = String.valueOf(((UserSecurityProfile)request.getSession().
            				getAttribute("UserSecurityProfile")).getUSER_SEQ_ID());
            		
            		DynaActionForm frmPreRequisiteHospital = (DynaActionForm)form;
            		
            		PreRequisiteVO	preRequisiteVO	=	new PreRequisiteVO();
            		PreRequisiteVO	preRequisiteVO1	=	new PreRequisiteVO();
            		
            		preRequisiteVO = (PreRequisiteVO)FormUtils.getFormValues(frmPreRequisiteHospital,"frmPreRequisiteHospital",
            				this,mapping,request);
            		HospitalManager hospitalObject=this.getHospitalManagerObject();
            		preRequisiteVO1	=	hospitalObject.generateCredentialsforPreRequisite(preRequisiteVO,strContactSeqId);
            		//long tempMobile		=	preRequisiteVO1.getMobileNo();
            		//String strtempMobile=	""+tempMobile;
            		

            		frmPreRequisiteHospital.initialize(mapping);
            		frmPreRequisiteHospital = (DynaActionForm)FormUtils.setFormValues("frmPreRequisiteHospital",
            				preRequisiteVO,this,mapping,request);
            		
            		request.setAttribute("updated", "message.intx.credentials.Sent");
            		request.getSession().setAttribute("iContactSeqId", preRequisiteVO1.getContactSeqId());
            		request.getSession().setAttribute("frmPreRequisiteHospital",frmPreRequisiteHospital);
            		
            		return this.getForward(strHospitalPreRequisite, mapping, request);
        	}//end of try
            	catch(TTKException expTTK)
        		{
        			return this.processExceptions(request, mapping, expTTK);
        		}//end of catch(TTKException expTTK)
        		catch(Exception exp)
        		{
        			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
        		}//end of catch(Exception exp)
            }//end of doGenerateIds(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    *//**
     * This method is Navigate to the Provider DashBoard
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doProviderDashBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			
			setLinks(request);
			log.info("Inside the doProviderDashBoard method of HospitalSearchAction");
			DynaActionForm frmProviderDashBoard=(DynaActionForm)form;
			
			HospitalManager hospitalObject=this.getHospitalManagerObject();
    		int iResult	=	0;
    		ArrayList alProvDashBoard	=	null;
    		alProvDashBoard	=	hospitalObject.getProviderDashBoard();

			frmProviderDashBoard.set("caption", "Dashboard");
			request.setAttribute("alProvDashBoard", alProvDashBoard);
	        request.getSession().setAttribute("frmProviderDashBoard",frmProviderDashBoard);
	        return this.getForward(strProviderDashBoard, mapping, request);
	        }//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp, strHospSearch));
		}//end of catch(Exception exp)
	}//end of doViewServicesList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    *//**
     * This method is used to GenerateIds based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doShowTotalProviders(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doShowTotalProviders method of HospitalSearchAction");
            		setLinks(request);
            		ArrayList alTotlaProviders	=	null;
            		
            		DynaActionForm frmProviderDashBoardTotalProviders = (DynaActionForm)form;
            		PreRequisiteVO	preRequisiteVO	=	new PreRequisiteVO();
            		HospitalManager hospitalObject=this.getHospitalManagerObject();
            		alTotlaProviders	=	hospitalObject.getTotalProviders();

        			request.setAttribute("alTotlaProviders", alTotlaProviders);

            		frmProviderDashBoardTotalProviders.initialize(mapping);
            		frmProviderDashBoardTotalProviders.set("caption", "List of Total Providers");
            		frmProviderDashBoardTotalProviders = (DynaActionForm)FormUtils.setFormValues("frmProviderDashBoardTotalProviders",
            				preRequisiteVO,this,mapping,request);
            		
            		request.getSession().setAttribute("frmProviderDashBoardTotalProviders",frmProviderDashBoardTotalProviders);
            		return this.getForward(strTotolProviders, mapping, request);
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doShowTotalProviders(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    *//**
     * This method is used to GenerateIds based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doCreateGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doCreateGroup method of HospitalSearchAction");
    		setLinks(request);
    		return this.getForward(strTotolProviders, mapping, request);
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doCreateGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    *//**
     * This method is used to doSendMail based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doSendMail(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doSendMail method of HospitalSearchAction");
        	setOnlineLinks(request);
            		ArrayList alProfessionals	=	null;
            		
            		String strContactSeqId  = String.valueOf(((UserSecurityProfile)request.getSession().
            				getAttribute("UserSecurityProfile")).getUSER_SEQ_ID());
            		
            		DynaActionForm preRequisiteForm = (DynaActionForm)form;
            		String iContactSeqId	=	(String)request.getSession().getAttribute("iContactSeqId");
            		PreRequisiteVO	preRequisiteVO	=	new PreRequisiteVO();

            		
            		preRequisiteVO.setHospName(TTKCommon.checkNull((String)preRequisiteForm.get("hospName")));
            		preRequisiteVO.setLicenceNo(TTKCommon.checkNull((String)preRequisiteForm.get("licenceNumber")));
            		preRequisiteVO.setHospMail(TTKCommon.checkNull((String)preRequisiteForm.get("hospmailid")));
            		preRequisiteVO.setUserId(TTKCommon.checkNull((String)preRequisiteForm.get("hospUserId")));
            		preRequisiteVO.setPassword(TTKCommon.checkNull((String)preRequisiteForm.get("hospUserPwd")));
            		
            		
            		HospitalManager hospitalObject=this.getHospitalManagerObject();
            		int iResult	=	0;
            		
            		iResult	=	hospitalObject.sendCredentialsforPreRequisite(preRequisiteVO,strContactSeqId,iContactSeqId);
            		
            		return this.getForward(strHospitalPreRequisite, mapping, request);
        	}//end of try
            	catch(TTKException expTTK)
        		{
        			return this.processExceptions(request, mapping, expTTK);
        		}//end of catch(TTKException expTTK)
        		catch(Exception exp)
        		{
        			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
        		}//end of catch(Exception exp)
            }//end of doSendMail(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    public ActionForward doAddNetworks(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.info("Inside the doAddNetworks method of HospitalSearchAction");
    		setLinks(request);
    		
    		DynaActionForm frmAddNetworkTypes	=	(DynaActionForm)form;
    		frmAddNetworkTypes.initialize(mapping);
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		ArrayList<NetworkTypeVO> alNetworkTypeList 	=	null; 
    		alNetworkTypeList	=	hospitalObject.getNetworkTypeList();
    		
    		//to GET Data from History table
    		ArrayList<NetworkTypeVO> alNetworkHistory	=	hospitalObject.getNetworkHistory();
    		
    		request.getSession().setAttribute("alNetworkTypeList", alNetworkTypeList);
    		request.getSession().setAttribute("alNetworkHistory", alNetworkHistory);
    		return this.getForward(strAddNetworks, mapping, request);
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doCreateGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    
    public ActionForward doSaveNetworkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doSaveNetworkType method of HospitalSearchAction");
    		setLinks(request);
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		DynaActionForm frmAddNetworkTypes	=	(DynaActionForm)form;
    		NetworkTypeVO networkTypeVO			=	new NetworkTypeVO();
    		
    		networkTypeVO.setNetworkCode(frmAddNetworkTypes.getString("sNetworkCode"));
    		networkTypeVO.setNetworkName(frmAddNetworkTypes.getString("sNetworkName"));
    		networkTypeVO.setNetworkOrder(frmAddNetworkTypes.getString("sNetworkOrder"));
    		
    		
    		int iResult	=	hospitalObject.saveNetworkType(networkTypeVO,TTKCommon.getUserSeqId(request));
    		if(iResult>0)
    			request.setAttribute("updated", "message.savedSuccessfully");
    		
    		frmAddNetworkTypes.initialize(mapping);
    		ArrayList<NetworkTypeVO> alNetworkTypeList 	=	null; 
    		alNetworkTypeList		=	hospitalObject.getNetworkTypeList();
    		request.getSession().setAttribute("alNetworkTypeList", alNetworkTypeList);
    		
    		Cache.refresh("primaryNetwork");
    		return this.getForward(strAddNetworks, mapping, request);
    	}//end of try
        	catch(TTKException expTTK)
        	{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doCreateGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doEditNetworkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.info("Inside the doEditNetworkType method of HospitalSearchAction");
    		setLinks(request);
    		
    		request.setAttribute("edit", "edit");
    		return this.getForward(strAddNetworks, mapping, request);
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doCreateGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    public ActionForward doModifyNetworkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doModifyNetworkType method of HospitalSearchAction");
    		setLinks(request);
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		DynaActionForm frmAddNetworkTypes	=	(DynaActionForm)form;
    		NetworkTypeVO networkTypeVO			=	new NetworkTypeVO();
    		
    		
    		String[] networkCode	=	request.getParameterValues("networkCode");
    		String[] networkName	=	request.getParameterValues("networkName");
    		String[] networkOrder	=	request.getParameterValues("networkOrder");
    		String[] seqId			=	request.getParameterValues("seqId");
    		

    		String[] strResult	=	new String[networkCode.length];
    		for(int k=0;k<networkCode.length;k++)
    			strResult[k]	=	"|"+seqId[k]+"|"+networkCode[k].toUpperCase()+"|"+networkName[k]+"|"+networkOrder[k]+"|";
    		
    		*//**
    		 * Code to check duplicate network orders and Network Code S T A R T S HERE
    		 *//*
    		HashSet h=new HashSet();
    		HashSet checkDupNetCodeSet=new HashSet();
    		int b;
    		String  checkDupNetCode;
    		for(int i=0;i<strResult.length;i++)
    		{
    			b				=	Integer.parseInt(networkOrder[i]);
    			checkDupNetCode	=	networkCode[i];
    			h.add(b);
    			checkDupNetCodeSet.add(checkDupNetCode);
    		}
    		if(h.size()!=networkOrder.length)
    		{
    			TTKException expTTK = new TTKException();
                expTTK.setMessage("error.duplicateNetOrderValue");
    			request.setAttribute("edit", "edit");
                throw expTTK;
    			//request.setAttribute("updated","message.duplicateNetOrderValue");
        		//return this.getForward(strAddNetworks, mapping, request);
    		}
    		if(checkDupNetCodeSet.size()!=networkCode.length)
    		{
    			TTKException expTTK = new TTKException();
                expTTK.setMessage("error.duplicateNetCodeValue");
    			request.setAttribute("edit", "edit");
                throw expTTK;
    			//request.setAttribute("updated","message.duplicateNetCodeValue");
        		//return this.getForward(strAddNetworks, mapping, request);
    		}
    		*//**
    		 * Code to check duplicate network orders and Network Code E N D S HERE
    		 *//*
    		
    		int iResult	=	hospitalObject.modifyNetworkType(strResult,TTKCommon.getUserSeqId(request));
    		if(iResult>0)
    			request.setAttribute("updated", "message.savedSuccessfully");
    		
    		//to GET Data from History table
    		ArrayList<NetworkTypeVO> alNetworkHistory	=	hospitalObject.getNetworkHistory();
    		
    		frmAddNetworkTypes.initialize(mapping);
    		ArrayList<NetworkTypeVO> alNetworkTypeList 	=	null; 
    		alNetworkTypeList		=	hospitalObject.getNetworkTypeList();
    		request.getSession().setAttribute("alNetworkTypeList", alNetworkTypeList);
    		request.getSession().setAttribute("alNetworkHistory", alNetworkHistory);
    		Cache.refresh("primaryNetwork");
    		return this.getForward(strAddNetworks, mapping, request);
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doModifyNetworkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    
     * Do Default for Haad Factors -  to show a jsp where user can Enter HAAD Factors
     
    public ActionForward doHaadFactorDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doHaadFactorDefault method of HospitalSearchAction");
			String strForward	=	"";
    		setLinks(request);
    		String hosp_seq_id	=	request.getParameter("hosp_seq_id");
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		String primaryNetwork;
    		ArrayList alHaadCategories,alHistoryOfTariffUpdates	=	null;
    		primaryNetwork	=	hospitalObject.getHaadCategories(hosp_seq_id);
    		DynaActionForm frmAddHaadFactors = (DynaActionForm)form;
    		frmAddHaadFactors.initialize(mapping);

    		alHaadCategories	=	hospitalObject.getHaadCategories(hosp_seq_id,"ExistingCategories",primaryNetwork);
    		strForward			=	"haadFactorDefalut";	
    		ArrayList<Object> alColumnHeaders	=	hospitalObject.getHaadColumnHEaders();//to display the column headers
    		
    		ArrayList<CacheObject>alEligibleNetworks 	=	new ArrayList<CacheObject>();
    		alEligibleNetworks	=	hospitalObject.getEligibleNetworks(hosp_seq_id);
    		
    		alHistoryOfTariffUpdates	=	hospitalObject.getHaadFactorsHostory(hosp_seq_id);
    		
    		frmAddHaadFactors.set("networkType", primaryNetwork);
			request.setAttribute("alHaadCategories", alHaadCategories);
			request.getSession().setAttribute("alColumnHeaders", alColumnHeaders);
			request.getSession().setAttribute("alEligibleNetworks", alEligibleNetworks);
			request.getSession().setAttribute("frmAddHaadFactors", frmAddHaadFactors);
			request.getSession().setAttribute("primaryNetwork", primaryNetwork);
			request.getSession().setAttribute("alHistoryOfTariffUpdates", alHistoryOfTariffUpdates);
    		return mapping.findForward(strForward);
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doCreateGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    public ActionForward doModifyMultipleHaadFactors(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.info("Inside the doModifyMultipleHaadFactors method of HospitalSearchAction");
    		setLinks(request);
    		
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		
    		String[] cacheId	=	request.getParameterValues("groupName");
    		String[] factor		=	request.getParameterValues("factor");
    		String[] baseRate	=	request.getParameterValues("baseRate");
    		String[] gap		=	request.getParameterValues("gap");
    		String[] margin		=	request.getParameterValues("margin");
    		//String networkType	=	request.getParameter("eligibleNetworks");
    		String networkType		=	(String) request.getParameter("networkType");
    		String haadTarrifStartDt		=	(String) request.getParameter("haadTarrifStartDt");
    		String haadTarrifEndDt		=	(String) request.getParameter("haadTarrifEndDt");
    		
    		ArrayList alColumnHeaders=(ArrayList)request.getSession().getAttribute("alColumnHeaders");
    		
    		int iResult	=	hospitalObject.saveHaadCategories(alColumnHeaders,cacheId,factor,baseRate,gap,margin,
    				request.getParameter("hosp_seq_id"),TTKCommon.getUserSeqId(request),networkType,haadTarrifStartDt,haadTarrifEndDt);
			
    		request.setAttribute("hosp_seq_id",request.getParameter("hosp_seq_id"));
    		if(iResult>0)
        		return mapping.findForward("haadFactorsExisted");
    		else
    		String hosp_seq_id	=	request.getParameter("hosp_seq_id");
    		String primaryNetwork	=	(String) request.getParameter("networkType");
    		ArrayList alHistoryOfTariffUpdates,alHaadCategories	=	null;
    		alHistoryOfTariffUpdates	=	hospitalObject.getHaadFactorsHostory(hosp_seq_id);
    		request.getSession().setAttribute("alHistoryOfTariffUpdates", alHistoryOfTariffUpdates);
    		  
    		
    		alHaadCategories	=	hospitalObject.getHaadCategories(hosp_seq_id,"ExistingCategories",primaryNetwork);
    		request.setAttribute("alHaadCategories", alHaadCategories);
    		
    		return mapping.findForward("haadFactorDefalut");
    		
    		
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doSaveHaadFactors(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
    
     * 
     
    public ActionForward doUpdateHaadFactors(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doUpdateHaadFactors method of HospitalSearchAction");
    		setLinks(request);
    
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		
    		String eligibleNetworks	=	"|"+request.getParameter("eligibleNetworks")+"|";
    		String haadGroup	=	"|"+request.getParameter("haadGroup")+"|";
    		String haadfactor	=	request.getParameter("haadfactor");
    		String haadTarrifStartDt	=	request.getParameter("haadTarrifStartDt");
    		String haadTarrifEndDt	=	request.getParameter("haadTarrifEndDt");
    		String factorVal	=	request.getParameter("factorVal");
    		String hosp_seq_id	=	request.getParameter("hosp_seq_id");
    		
    		
    		int iResult	=	hospitalObject.updateHaadCategories(eligibleNetworks,haadGroup,haadfactor,haadTarrifStartDt,haadTarrifEndDt,factorVal,hosp_seq_id,TTKCommon.getUserSeqId(request));
    		if(iResult>0)
    			request.setAttribute("updated", "message.savedSuccessfully");
    		request.setAttribute("hosp_seq_id",request.getParameter("hosp_seq_id"));
    		
    		 to retrieve existing data
    		ArrayList alHaadCategories	=	null;
    		String strForward			=	"";
    		String primaryNetwork	=	(String) request.getSession().getAttribute("primaryNetwork");
    		alHaadCategories	=	hospitalObject.getHaadCategories(hosp_seq_id,"ExistingCategories",primaryNetwork);
    		DynaActionForm generalForm = (DynaActionForm)form;
    		request.setAttribute("alHaadCategories", alHaadCategories);
    		ArrayList alHistoryOfTariffUpdates	=	null;
    		alHistoryOfTariffUpdates	=	hospitalObject.getHaadFactorsHostory(hosp_seq_id);
    		request.getSession().setAttribute("alHistoryOfTariffUpdates", alHistoryOfTariffUpdates);
    		return mapping.findForward("haadFactorDefalut");
    		
    		
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doUpdateHaadFactors(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    
    public ActionForward doEditValues(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.info("Inside the doEditValues method of HospitalSearchAction");
    		setLinks(request);
    		//DynaActionForm frmAddHaadFactors = (DynaActionForm)form;
    		//frmAddHaadFactors.set("", request.getParameter("editFlag"));
    		request.setAttribute("editFlag", request.getParameter("editFlag"));
    		String hosp_seq_id	=	request.getParameter("hosp_seq_id");
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		String primaryNetwork	=	(String) request.getParameter("networkType");
    		ArrayList alHaadCategories = 	hospitalObject.getHaadCategories(hosp_seq_id,"ExistingCategories",primaryNetwork);
			request.getSession().setAttribute("alHaadCategories", alHaadCategories);

    		return mapping.findForward("haadFactorDefalut");
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doEditValues(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    public ActionForward doChangeNetworkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.info("Inside the doChangeNetworkType method of HospitalSearchAction");
    		setLinks(request);
    		request.setAttribute("editFlag", request.getParameter("editFlag"));
    		String hosp_seq_id	=	request.getParameter("hosp_seq_id");
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		String primaryNetwork	=	(String) request.getParameter("networkType");
    		ArrayList alHaadCategories = 	hospitalObject.getHaadCategories(hosp_seq_id,"ExistingCategories",primaryNetwork);
			request.getSession().setAttribute("alHaadCategories", alHaadCategories);

    		return mapping.findForward("haadFactorDefalut");
    	}//end of try
        	catch(TTKException expTTK)
    		{
    			return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    		}//end of catch(Exception exp)
        }//end of doEditValues(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   */ 
}//end of class HospitalSearchAction