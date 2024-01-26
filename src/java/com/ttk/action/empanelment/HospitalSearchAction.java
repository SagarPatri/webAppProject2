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
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.empanelment.ContactManager;
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.common.security.Cache;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.DocumentDetailVO;
import com.ttk.dto.empanelment.HospitalAuditVO;
import com.ttk.dto.empanelment.HospitalDetailVO;
import com.ttk.dto.empanelment.HospitalVO;
import com.ttk.dto.empanelment.NetworkTypeVO;
import com.ttk.dto.empanelment.PreRequisiteVO;
import com.ttk.dto.usermanagement.ContactVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching the hospitals and add/edit the hospital information.
 * This class is also used to view discrepancies if any.
 */

public class HospitalSearchAction extends TTKAction {
    private static Logger log = Logger.getLogger( HospitalSearchAction.class );

    //   Modes.
    private static final String strBackward="Backward";
    private static final String strForward="Forward";

    //  Action mapping forwards.
    private static final String strHospitallist="hospitallist";
    private static final String strEdithospital="edithospital";
    private static final String strFrwdDiscrepancy="discrepancy";
    private static final String strConfig="configuration";

    //Exception Message Identifier
    private static final String strHospSearch="hospitalsearch";

    //PreRequisite for intx 
    private static final String strHospitalPreRequisite="hospitalPreRequisite";
    private static final String strProviderDashBoard	=	"providerdashboard";
    private static final String strPreRequisiteLinks	=	"prerequisitelinks";
    private static final String strTotolProviders		=	"totalproviderslist";
    private static final String strAddNetworks			=	"addNetworkTypes";
    private static final String strProviderDiscountCfg="providerDiscountCfg";
    private static final String strProviderDiscountCfgDetails="providerDiscountCfgDetails";
    private static final String strHospgenInfo="hospgeninfo";

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
    		log.debug("Inside the doDefault method of HospitalSearchAction");
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
    		tableData.createTableInfo("HospitalSearchTable",new ArrayList());
    		request.getSession().setAttribute("tableData",tableData);
    		request.getSession().setAttribute("alSubStatus",null);
    		generalForm.set("officeInfo",strDefaultBranchID);
    		TTKCommon.documentViewer(request);
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
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    /**
     * This method is used to initialize the search web page for PreRequisite of Hospitals.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doPreRequisite(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doPrerequisite method of HospitalSearchAction");
    		setLinks(request);
    		/*if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
*/    		
    		DynaActionForm generalForm = (DynaActionForm)form;
    		generalForm.initialize(mapping);
    		//create new table data object
    		request.getSession().setAttribute("frmPreRequisiteHospital", generalForm);
    		return this.getForward(strPreRequisiteLinks, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doPrerequisite(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    /**
     * This method is used to initialize the search web page for PreRequisite of Hospitals.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doPreRequisiteNew(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doPrerequisiteNew method of HospitalSearchAction");
    		setLinks(request);
    		/*if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
*/    		
    		DynaActionForm generalForm = (DynaActionForm)form;
    		generalForm.initialize(mapping);
    		//create new table data object
    		request.getSession().setAttribute("frmPreRequisiteHospital", generalForm);
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
    }//end of doPrerequisite(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
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
    		log.debug("Inside the doSearch method of HospitalSearchAction");
    		if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
    		TableData tableData = TTKCommon.getTableData(request);
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{
    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward("hospitallist"));
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
    			tableData.createTableInfo("HospitalSearchTable",null);
    			//fetch the data from the data access layer and set the data to table object
    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form));
    			tableData.modifySearchData("search");
    		}//end of else
    		ArrayList alHospital=hospitalObject.getHospitalList(tableData.getSearchData());
    		tableData.setData(alHospital, "search");
    		request.getSession().setAttribute("tableData",tableData);
    		//finally return to the grid screen
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
    		setLinks(request);
    		log.debug("Inside the doBackward method of HospitalSearchAction");
			TableData tableData = TTKCommon.getTableData(request);
			HospitalManager hospitalObject=this.getHospitalManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alHospital = hospitalObject.getHospitalList(tableData.getSearchData());
    		tableData.setData(alHospital, strBackward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
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
			HospitalManager hospitalObject=this.getHospitalManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alHospital = hospitalObject.getHospitalList(tableData.getSearchData());
    		tableData.setData(alHospital, strForward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		TTKCommon.documentViewer(request);
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
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
     */
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
    }//end of doCopyToWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		log.debug("Inside the doAdd method of HospitalSearchAction");
			HospitalDetailVO hospitalDetailVO=null;
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().
													 getAttribute("UserSecurityProfile");
			
			ArrayList alCityList = new ArrayList();
    		hospitalDetailVO = new HospitalDetailVO();
    		AddressVO addressVO	=	new AddressVO();
    		addressVO.setStateCode("DOH");//SETTING DUBAI AS DEFAULT
    		hospitalDetailVO.setAddressVO(addressVO);
            DynaActionForm hospitalForm = setFormValues(hospitalDetailVO,mapping,request);
            if(userSecurityProfile.getBranchID()!=null){
            	hospitalForm.set("tpaOfficeSeqId", userSecurityProfile.getBranchID().toString());
            }//end of if(userSecurityProfile.getBranchID()!=null)
            else{
            	hospitalForm.set("tpaOfficeSeqId", "");
            }//end of else
            
            
            //GETTING ALL PAYERS LIST TO ASSOCIATE DEFAULT
            HospitalManager hospitalObject=this.getHospitalManagerObject();
            String strPayersList	=	hospitalObject.getAllPayersList();
             
            //GETTING ALL DUBAI AREAS AS WE SHOWING DUBAI AND UAE AS DEFAULT
            HashMap hmCityList = null;
            hmCityList=hospitalObject.getCityInfo("DOH");
            String countryCode	=	(String)(hmCityList.get("CountryId"));
            String isdcode	=	(String)(hmCityList.get("isdcode"));
            String stdcode	=	(String)(hmCityList.get("stdcode"));
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(hospitalDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)

            if(alCityList==null){
            	alCityList=new ArrayList();
            }//end of if(alCityList==null)
            
            ArrayList<String> alHospitalDetail = new ArrayList<>();
            alHospitalDetail    = hospitalObject.getBenefitLimitForProvider();
			hospitalForm.set("alCityList",alCityList);
			hospitalForm.set("countryCode", countryCode);//SETTING COUNTRY AS DEFAULT
			hospitalForm.set("isdCode",isdcode);//SETTING ISD AS DEFAULT
	        hospitalForm.set("stdCode",stdcode);//SETTING STD AS DEFAULT
			hospitalForm.set("payerCodes", strPayersList);
			//request.setAttribute("stdCodes","stdCodes");
            hospitalForm.set("caption","Add");
            
            hospitalForm.set("dentalBenefitLimit", alHospitalDetail.get(0));
            hospitalForm.set("outPetientBenefitLimit", alHospitalDetail.get(1));
            hospitalForm.set("opticalBenefitLimit", alHospitalDetail.get(2));
            hospitalForm.set("outpetientMaternityBenefitLimit", alHospitalDetail.get(3));
            request.getSession().setAttribute("frmAddHospital",hospitalForm);
            request.getSession().removeAttribute("fastTracktableData");
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
    }//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		log.debug("Inside the doView method of HospitalSearchAction");
    		String strCaption="";
    		HashMap hmCityList = null;
			ArrayList alCityList = new ArrayList();
			HospitalVO hospitalVO=null;
			HospitalDetailVO hospitalDetailVO=null;
			HospitalManager hospitalObject=this.getHospitalManagerObject();
			TableData tableData = TTKCommon.getTableData(request);
            if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
            	((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))

            DynaActionForm generalForm = (DynaActionForm)form;
            if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            {
                hospitalVO = (HospitalVO)tableData.getRowInfo(Integer.parseInt((String)(generalForm).get("rownum")));
                //add the selected item to the web board and make it as default selected
                this.addToWebBoard(hospitalVO, request);
                //calling business layer to get the hospital detail
                hospitalDetailVO = hospitalObject.getHospitalDetail(hospitalVO.getHospSeqId());
                generalForm.initialize(mapping);
                strCaption="Edit";
                //Add the request object to DocumentViewer
                TTKCommon.documentViewer(request);
            }//end if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            else if(TTKCommon.getWebBoardId(request) != null)//take the hospital_seq_id from web board
            {
                //if web board id is found, set it as current web board id
                hospitalVO = new HospitalVO();
                hospitalVO.setHospSeqId(TTKCommon.getWebBoardId(request));
                //calling business layer to get the hospital detail
                hospitalDetailVO = hospitalObject.getHospitalDetail(hospitalVO.getHospSeqId());
                strCaption="Edit";
                //Add the request object to DocumentViewer
                TTKCommon.documentViewer(request);
            }//end of else if(TTKCommon.getWebBoardId(request) != null)
            else
            {
            	
                TTKException expTTK = new TTKException();
                expTTK.setMessage("error.hospital.required");
                throw expTTK;
            	
            }//end of if(strMode.equals("EditHospital"))
           
            hmCityList=hospitalObject.getCityInfo(hospitalDetailVO.getAddressVO().getStateCode());
            String countryCode	=	(String)(hmCityList.get("CountryId"));
          
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(hospitalDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)
        	
            ArrayList<String[]> alAssNetworkTypes = hospitalObject.getHospitalNetworkTypes(hospitalVO.getHospSeqId());
            
            
            
    			String temp	=	"";
    		for(int o=0;o<alAssNetworkTypes.size();o++){
               	String str[]	=	alAssNetworkTypes.get(o);
               	//for(int p=0;p<str.length;p++){
               		//  
               		if(str[1].equals("Y"))
               			temp	=	temp+o+",";
               //	}
    		}
    		if(temp!=null && temp!="" && !temp.equals("") && temp.length()>1)
    			temp	=	temp.substring(0, temp.length()-1);
    		
            DynaActionForm hospitalForm = setFormValues(hospitalDetailVO,mapping,request);
        
            hospitalForm.set("caption",strCaption);
            hospitalForm.set("alCityList",alCityList);
            hospitalForm.set("stopPreAuth",hospitalDetailVO.getStopPreAuthsYN());
            hospitalForm.set("stopClaim",hospitalDetailVO.getStopClaimsYN());

            hospitalForm.set("cnynNet",hospitalDetailVO.getCNYN());
            hospitalForm.set("gnynNet",hospitalDetailVO.getGNYN());
            hospitalForm.set("srnynNet",hospitalDetailVO.getSRNYN());
            hospitalForm.set("rnynNet",hospitalDetailVO.getRNYN());
            hospitalForm.set("wnynNet",hospitalDetailVO.getWNYN());
            hospitalForm.set("provgrpHidden",hospitalDetailVO.getProviderYN());
            
            if(hospitalDetailVO.getPaymentDuration() != null)
            {
            	  hospitalForm.set("oldPaymentDuration",hospitalDetailVO.getPaymentDuration().toString());
            }
            else hospitalForm.set("oldPaymentDuration","");
            if(hospitalDetailVO.getPaymentTermAgrSDate() != null)
            {
            	  hospitalForm.set("oldPaymentTermAgrSDate",hospitalDetailVO.getPaymentTermAgrSDate());
            }
            else hospitalForm.set("oldPaymentTermAgrSDate",new Long(0));
            if(hospitalDetailVO.getPaymentTermAgrEDate() != null)
            {
            	   hospitalForm.set("oldPaymentTermAgrEDate",hospitalDetailVO.getPaymentTermAgrEDate());
            }
            else hospitalForm.set("oldPaymentTermAgrEDate","");
            
            //hospitalForm.set("providerReviewYN",hospitalDetailVO.getProviderReview());
            
         //   hospitalForm.set("countryCode",countryCode);
            request.setAttribute("alAssNetworkTypes",alAssNetworkTypes);
            request.getSession().setAttribute("selectedNetworks",temp);
            request.getSession().setAttribute("frmAddHospital",hospitalForm);
            request.getSession().setAttribute("refDetHospitalName",hospitalDetailVO.getHospitalName());
            request.getSession().setAttribute("hospSeqIdforTariff", hospitalDetailVO.getHospSeqId().toString()); //this we are using in Tariff Empanel screen to get the selected providers only
            request.getSession().setAttribute("AuthLicenseNo", hospitalDetailVO.getIrdaNumber()); 
            request.getSession().setAttribute("HospDetails",hospitalDetailVO);
            request.getSession().setAttribute("empStatus", hospitalDetailVO.getStatus());
               
            // Added For Payment discount configuration Start
            // Added For Fast Track Payment Discount Configuration Details
            Long  hospSeqId1 =hospitalVO.getHospSeqId();
    		TableData tableDataFast = (TableData) (request.getSession().getAttribute("fastTracktableData")==null?new TableData():request.getSession().getAttribute("fastTracktableData"));
    		tableDataFast.createTableInfo("FastTrackPaymentTable",null);
    		ArrayList<Object> alSearchData	=	new ArrayList<Object>();
    		alSearchData.add(hospSeqId1);
    		alSearchData.add("FAST");
    		alSearchData.add("OUT");
    		tableDataFast.setSearchData(alSearchData);
    		ArrayList alFastDiscountDetails	=	hospitalObject.getProviderFastTrackDiscount(tableDataFast.getSearchData());
    		tableDataFast.setData(alFastDiscountDetails);
    		((Column)((ArrayList)tableDataFast.getTitle()).get(6)).setLinkTitle("CHECKHIPERLINK");
    		request.getSession().setAttribute("fastTracktableData",tableDataFast);
    		request.getSession().setAttribute("enableHyperLink", null);
    		// Added For Volume Based Payment Discount Configuration Details
    		TableData tableDataVol = (TableData) (request.getSession().getAttribute("volumeBasedtableData")==null?new TableData():request.getSession().getAttribute("volumeBasedtableData"));
    		tableDataVol.createTableInfo("VolumeBasedPaymentTable",null);
    		ArrayList<Object> alSearchData1	=	new ArrayList<Object>();
    		alSearchData1.add(hospSeqId1);
    		alSearchData1.add("VOL");
    		alSearchData1.add("OUT");
    		tableDataVol.setSearchData(alSearchData1);
    		ArrayList alVolDiscountDetails	=	hospitalObject.getProviderVolumeDiscount(tableDataVol.getSearchData());
    		tableDataVol.setData(alVolDiscountDetails);
    		request.getSession().setAttribute("volumeBasedtableData",tableDataVol);  
    		request.getSession().setAttribute("stopclaimsprovider", hospitalDetailVO.getStopClaimsDate());
    		request.getSession().setAttribute("stopcashlessprovider", hospitalDetailVO.getStopPreauthDate());
    		// Added For Payment discount configuration End
    		request.getSession().setAttribute("hospitalgroupseqid", hospitalDetailVO.getProvGrpListId());
    		request.getSession().setAttribute("indOrGroup", hospitalDetailVO.getIndORgrp());
    		request.getSession().setAttribute("groupName", hospitalDetailVO.getGroupName());
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
    }//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		log.debug("Inside the doSave method of HospitalSearchAction");
    		com.ttk.common.security.Cache.refresh("ProviderList");
			HashMap hmCityList = null;
			ArrayList alCityList = new ArrayList();
			HospitalVO hospitalVO=null;
			HospitalDetailVO hospitalDetailVO=null;
			HospitalManager hospitalObject=this.getHospitalManagerObject();
            DynaActionForm generalForm = (DynaActionForm)form;
            Long  lngHospSeqId=TTKCommon.getLong(TTKCommon.checkNull((String)generalForm.get("hospSeqId")));
            if(lngHospSeqId!=null)
            {
            	/*
            	 * Adding this comment for INTX
            	 * as INTX is not using tds at all
            	 * 
            	 * 
            	 * if(("".equals(TTKCommon.checkNull(generalForm.get("panNmbr").toString())) ||
            	   "".equals(TTKCommon.checkNull(generalForm.get("hospitalStatusID").toString())) ||
            	   "".equals(TTKCommon.checkNull(generalForm.get("categoryID").toString())))
            	   && (generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("EMP") || generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("REN")))
            	{
            		TTKException expTTK = new TTKException();
                    expTTK.setMessage("error.empanelment.general.tdsprocessing");
                    throw expTTK;
            	}*/
            	
                if((request.getSession().getAttribute("flgHospName")!=null))
                {
                    if((!request.getSession().getAttribute("flgHospName").equals(generalForm.get("hospitalName")) ||
                    	!request.getSession().getAttribute("flgPanNumber").equals(generalForm.get("panNmbr")) ||
                    	!request.getSession().getAttribute("flgCategory").equals(generalForm.get("categoryID")))
                    		&& (generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("EMP") || generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("REN")))
                        {
                    		//to make server side validation for Reference Details Screen
                            generalForm.set("flagValidate","true");
                            generalForm.set("hospitalAuditVO", (DynaActionForm)FormUtils.
                            		setFormValues("frmHospitalAuditVO",new HospitalAuditVO(),this,mapping,request));
                            generalForm.set("refdetcaption",request.getSession().getAttribute("refDetHospitalName"));
                            return this.getForward("referencedetail", mapping, request);
                        }//if(!request.getSession().getAttribute("flgHospName").equals(generalForm.get("hospitalName"))&&generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("EMP"))
                }//end of if((request.getSession().getAttribute("flgHospName")!=null))
            }//end of if(lHospSeqId!=null)
            hospitalDetailVO = getFormValues(generalForm,mapping,request);
            //if conditions for stop pre-auth and claim
			/*if("Y".equals(TTKCommon.checkNull(generalForm.get("stopPreAuth"))))
			{
				hospitalDetailVO.setStopPreAuthsYN("Y");
			}
			else
			{
				hospitalDetailVO.setStopPreAuthsYN("N");
			}
			if("Y".equals(TTKCommon.checkNull(generalForm.get("stopClaim"))))
			{
				hospitalDetailVO.setStopClaimsYN("Y");
			}
			else
			{
				hospitalDetailVO.setStopClaimsYN("N");
			}*/
			
			
			//NETWORK TYPES GETTING
			/*String[] serviceType	=	request.getParameterValues("serviceType");
			for(int k=0;k<serviceType.length;k++)
				  */
			
			String[] networkTypes	=	request.getParameterValues("networkTypes");
			/*for(int k=0;k<networkTypes.length;k++)
				  */
			
			String[] hidServiceType	=	request.getParameterValues("hidServiceType");
			String strNetwrkType	=	"";
			for(int l=0;l<hidServiceType.length;l++){
				//  
				//if(hidServiceType[l].equals("") || hidServiceType[l].equals("N"))
				if(hidServiceType[l].equals(""))
					hidServiceType[l]	=	"N";
				
					strNetwrkType	=	strNetwrkType+",|"+networkTypes[l]+"|"+hidServiceType[l]+"|";
			}
			strNetwrkType	=	strNetwrkType+",";
			hospitalDetailVO.setCNYN(strNetwrkType);
			//NETWORK TYPES GETTING
			
			if("Y".equals(TTKCommon.checkNull(generalForm.get("providerReviewYN"))))
				hospitalDetailVO.setProviderReview("Y");
			else
				hospitalDetailVO.setProviderReview("N");
			
			if("Y".equals(TTKCommon.checkNull(generalForm.get("provgrpHidden"))))
				hospitalDetailVO.setProviderYN("Y");
			else
				hospitalDetailVO.setProviderYN("N");
			
			if("Y".equals(TTKCommon.checkNull(request.getParameter(("internetConnYn")))))
				hospitalDetailVO.setInternetConnYn("Y");
			else
				hospitalDetailVO.setInternetConnYn("N");
			
			if("Y".equals(TTKCommon.checkNull(request.getParameter("stopClaimsYN"))))
				hospitalDetailVO.setStopClaimsYN("Y");
			else
				hospitalDetailVO.setStopClaimsYN("N");
			if("Y".equals(TTKCommon.checkNull(request.getParameter("stopPreAuthsYN"))))
				hospitalDetailVO.setStopPreAuthsYN("Y");
			else
				hospitalDetailVO.setStopPreAuthsYN("N");
			//getting the hospital name and splitting the dha ID from name to save
			String tempHospname	=	hospitalDetailVO.getHospitalName();
			if(tempHospname.indexOf('[')>0)
			{
				tempHospname	=	tempHospname.substring(0, tempHospname.indexOf('['));
			}
			hospitalDetailVO.setHospitalName(tempHospname);
			
            //update the hospital details to data base
			lngHospSeqId = hospitalObject.addUpdateHospital(hospitalDetailVO);
			
            //set the appropriate message
            if(lngHospSeqId > 0)
            {
                if(generalForm.get("hospSeqId")!=null && !generalForm.get("hospSeqId").equals(""))
                {
                    request.setAttribute("updated","message.savedSuccessfully");
                    request.setAttribute("cacheId", ""+lngHospSeqId);
                    request.setAttribute("cacheDesc", hospitalDetailVO.getHospitalName());
                    //finally modify the web board details, if the hospital name is changed
                    TTKCommon.modifyWebBoardId(request);
                }//end of if(generalForm.get("hospSeqId")!=null && !generalForm.get("hospSeqId").equals(""))
                else
                {
                    request.setAttribute("updated","message.addedSuccessfully");
                    hospitalVO=new HospitalVO();
                    hospitalVO.setHospSeqId(lngHospSeqId);
                    hospitalVO.setHospitalName((String)generalForm.get("hospitalName"));
                    //add the details to web board
                    this.addToWebBoard(hospitalVO, request);
                    //clear the form object in add mode
                }//end of else
            }//end of if(lngHospSeqId > 0)
            hospitalDetailVO = hospitalObject.getHospitalDetail(lngHospSeqId);
            
            
            
            //GETTING NETWORK TYPES
            ArrayList<String[]> alAssNetworkTypes = hospitalObject.getHospitalNetworkTypes(lngHospSeqId);
            String temp	=	"";
    		for(int o=0;o<alAssNetworkTypes.size();o++){
               	String str[]	=	alAssNetworkTypes.get(o);
               	//for(int p=0;p<str.length;p++){
               		//  
               		if(str[1].equals("Y")){
               			//  
               			temp	=	temp+o+",";
               		}//o is the order of networks showing and its size
               //	}
    		}
    		if(temp!=null && temp!="" && !temp.equals("") && temp.length()>1)
    		temp	=	temp.substring(0, temp.length()-1);
			/*for(int o=0;o<alAssNetworkTypes.size();o++){
            	String str[]	=	alAssNetworkTypes.get(o);
            	for(int p=0;p<str.length;p++)
            		  
			}*/
    		
    		 //GETTING NETWORK TYPES
    		
    		
            generalForm.initialize(mapping);
            hmCityList=hospitalObject.getCityInfo();
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(hospitalDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)

            DynaActionForm hospitalForm = setFormValues(hospitalDetailVO,mapping,request);
            hospitalForm.set("caption","Edit");
            hospitalForm.set("alCityList",alCityList);
            hospitalForm.set("stopPreAuthsYN",hospitalDetailVO.getStopPreAuthsYN());
            hospitalForm.set("stopClaim",hospitalDetailVO.getStopClaimsYN());
            
            hospitalForm.set("cnynNet",hospitalDetailVO.getCNYN());
            hospitalForm.set("gnynNet",hospitalDetailVO.getGNYN());
            hospitalForm.set("srnynNet",hospitalDetailVO.getSRNYN());
            hospitalForm.set("rnynNet",hospitalDetailVO.getRNYN());
            hospitalForm.set("wnynNet",hospitalDetailVO.getWNYN());
            request.setAttribute("alAssNetworkTypes",alAssNetworkTypes);
            request.getSession().setAttribute("selectedNetworks",temp);
            hospitalForm.set("provgrpHidden",hospitalDetailVO.getProviderYN());
            hospitalForm.set("providerReviewYN",hospitalDetailVO.getProviderReview());
            
            if(hospitalDetailVO.getPaymentDuration() != null)
            {
            	  hospitalForm.set("oldPaymentDuration",hospitalDetailVO.getPaymentDuration().toString());
            }
            else hospitalForm.set("oldPaymentDuration","");
            if(hospitalDetailVO.getPaymentTermAgrSDate() != null)
            {
            	  hospitalForm.set("oldPaymentTermAgrSDate",hospitalDetailVO.getPaymentTermAgrSDate());
            }
            else hospitalForm.set("oldPaymentTermAgrSDate",new Long(0));
            if(hospitalDetailVO.getPaymentTermAgrEDate() != null)
            {
            	   hospitalForm.set("oldPaymentTermAgrEDate",hospitalDetailVO.getPaymentTermAgrEDate());
            }
            else hospitalForm.set("oldPaymentTermAgrEDate","");
         
            
          
        
            request.getSession().setAttribute("HospDetails",hospitalDetailVO);
            request.getSession().setAttribute("hospitalgroupseqid", hospitalDetailVO.getProvGrpListId());
            request.getSession().setAttribute("indOrGroup", hospitalDetailVO.getIndORgrp());
            request.getSession().setAttribute("groupName", hospitalDetailVO.getGroupName());
            request.getSession().setAttribute("frmAddHospital",hospitalForm);
            request.getSession().setAttribute("stopclaimsprovider", hospitalDetailVO.getStopClaimsDate());
    		request.getSession().setAttribute("stopcashlessprovider", hospitalDetailVO.getStopPreauthDate());
            Cache.refresh("providerCodeSearch");
            Cache.refresh("providerCode");
            //refreshing groupProviderList if a new group is added
			Cache.refresh("groupProviderList");
			Cache.refresh("payerCode");
			Cache.refresh("payerCodeSearch");
			Cache.refresh("ProviderList");
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
    	log.debug("Inside doChangeWebBoard method of HospitalSearchAction");
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
			HospitalManager hospitalObject=this.getHospitalManagerObject();
			HashMap hmCityList = null;
			ArrayList alCityList = new ArrayList();
			HospitalDetailVO hospitalDetailVO=null;
    		DynaActionForm hospitalForm=(DynaActionForm)form;
    		hospitalDetailVO = getFormValues(hospitalForm,mapping,request);
    		
    		String stateCode	=	"";
            stateCode	=	(String) request.getSession().getAttribute("stateCode");
            //request.getSession().setAttribute(stateCode, "stateCode");
            hmCityList=hospitalObject.getCityInfo(stateCode);
            String countryCode	=	(String)(hmCityList.get("CountryId"));
            String isdcode	=	(String)(hmCityList.get("isdcode"));
            String stdcode	=	(String)(hmCityList.get("stdcode"));
            if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(hospitalDetailVO.getAddressVO().getStateCode());
            }//end of if(hmCityList!=null)

            if(alCityList==null){
            	alCityList=new ArrayList();
            }//end of if(alCityList==null)

            hospitalForm.set("frmChanged","changed");
            hospitalForm.set("countryCode",countryCode);
            hospitalForm.set("isdCode",isdcode);
            hospitalForm.set("stdCode",stdcode);
            hospitalForm.set("alCityList",alCityList);

            //request.setAttribute("stdCodes","stdCodes");
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
    }//end of doChangeState(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
     */
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

    /**
     * This method is used to navigate the user to discrepancy screen if any.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
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

    /**
     * This method is used to resolve the discrepancy if any.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
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

    /**
     * This method is used to navigate to previous screen when closed button is clicked.
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
    	
    /**
     * Returns the HospitalManager session object for invoking methods on it.
     * @return HospitalManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private HospitalManager getHospitalManagerObject() throws TTKException
    {
        HospitalManager hospManager = null;
        try
        {
            if(hospManager == null)
            {
                InitialContext ctx = new InitialContext();
                hospManager = (HospitalManager) ctx.lookup("java:global/TTKServices/business.ejb3/HospitalManagerBean!com.ttk.business.empanelment.HospitalManager");
            }//end if(hospManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp,strHospSearch);
        }//end of catch
        return hospManager;
    }//end getHospitalManagerObject()

    /**
     * Returns the ArrayList which contains the populated search criteria elements
     * @param frmSearchHospital DynaActionForm will contains the values of corresponding fields
     * @param request HttpServletRequest object which contains the search parameter that is built
     * @return alSearchParams ArrayList
     */
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmSearchHospital)
    {
        //build the column names along with their values to be searched
        ArrayList<Object> alSearchParams = new ArrayList<Object>();
        alSearchParams.add(new SearchCriteria("EMPANEL_NUMBER",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("sEmpanelmentNo"))));
        alSearchParams.add(new SearchCriteria("HOSP_NAME",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("sHospitalName"))));
        alSearchParams.add(new SearchCriteria("OFF_INFO.TPA_OFFICE_SEQ_ID",
        		(String)frmSearchHospital.get("officeInfo"),"equals"));
        alSearchParams.add(new SearchCriteria("STATE_TYPE_ID", (String)frmSearchHospital.get("stateCode"),"equals"));
        alSearchParams.add(new SearchCriteria("CITY_TYPE_ID", (String)frmSearchHospital.get("cityCode"),"equals"));
        alSearchParams.add(new SearchCriteria("STATUS_CODE.EMPANEL_STATUS_TYPE_ID",
        		(String)frmSearchHospital.get("empanelStatusCode")));
        alSearchParams.add(new SearchCriteria("GRAD.APPROVED_GRADE_TYPE_ID",
        		(String)frmSearchHospital.get("gradeCode")));
        alSearchParams.add(new SearchCriteria("RSON_CODE.EMPANEL_RSON_TYPE_ID",
        		(String)frmSearchHospital.get("subStatus"))); 
        alSearchParams.add(new SearchCriteria("HOSP_LICENC_NUMB",
                		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("sDHAID"))));//DHA ID,added for INtX
        alSearchParams.add(new SearchCriteria("tcc.country_id", (String)frmSearchHospital.get("countryCode"),"equals"));
        alSearchParams.add(new SearchCriteria("dpt.provider_type_id",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("providerTypeId"))));//added for INtX
        alSearchParams.add(new SearchCriteria("primary_network",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("networkTypeId"))));//added for INtX
        alSearchParams.add(new SearchCriteria("HGI.HOSP_GROUP_SEQ_ID", (String)frmSearchHospital.get("provGrpList"),"equals"));  
        alSearchParams.add(new SearchCriteria("HOSP_INFO.PRIMARY_NETWORK",
        		TTKCommon.replaceSingleQots((String)frmSearchHospital.get("provNtwType"))));
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchHospital)

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
                cacheObject.setCacheId(""+((HospitalVO)tableData.getData().
                		get(Integer.parseInt(strChk[i]))).getHospSeqId());
                cacheObject.setCacheDesc(((HospitalVO)tableData.getData().
                		get(Integer.parseInt(strChk[i]))).getHospitalName());
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
     * @param hospitalVO HospitalVO object which contain the information of hospital
     * @param request HttpServletRequest object which contains information about the selected check boxes
     */
    private void addToWebBoard(HospitalVO hospitalVO, HttpServletRequest request)
    {
        Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
        CacheObject cacheObject = new CacheObject();
        cacheObject.setCacheId(""+hospitalVO.getHospSeqId());
        cacheObject.setCacheDesc(hospitalVO.getHospitalName());
        ArrayList<Object> alCacheObject = new ArrayList<Object>();
        alCacheObject.add(cacheObject);
        //if the object(s) are added to the web board, set the current web board id
        toolbar.getWebBoard().addToWebBoardList(alCacheObject);
        toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());
        //set weboardinvoked as true to avoid change of webboard id twice in same request
        request.setAttribute("webboardinvoked","true");
    }//end of addToWebBoard(HospitalVO hospitalVO, HttpServletRequest request)

    /**
     * Populates the form element to value object .
     * @param hospDetailVO HospitalDetailVO object.
     * @param mapping The ActionMapping used to select this instance.
     * @param request HttpServletRequest  object which contains hospital information.
     * @return hospDetailVO HospitalDetailVO object.
     */
    private HospitalDetailVO getFormValues(DynaActionForm generalForm,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
        try
        {
            HospitalDetailVO hospDetailVO =null;
            AddressVO addressVO = null;
            HospitalAuditVO hospitalAuditVO=null;
            DocumentDetailVO docuDetailVO = null;
            hospDetailVO = (HospitalDetailVO)FormUtils.getFormValues(generalForm,"frmAddHospital",
            				this,mapping,request);
            
            if(request.getParameter("internetConnYn")==null)// if Internet Connection checkbox in not selected
            {
                hospDetailVO.setInternetConnYn("N");
            }//end of if(request.getParameter("internetConnYn")==null)
            if(request.getParameter("intExtApp")==null)// if External Application checkbox in not selected
            {
                hospDetailVO.setIntExtApp("N");
            }//end of if(request.getParameter("intExtApp")==null)
            String providerYN 	=	(String)generalForm.get("providerYN");
            String providerReview 	=	(String)generalForm.get("providerReview");

            if("on".equalsIgnoreCase(providerYN))
            	hospDetailVO.setProviderYN("Y");
            else
            	hospDetailVO.setProviderYN("N");
            
            if("on".equalsIgnoreCase(providerReview))
            	hospDetailVO.setProviderReview("Y");
            else
            	hospDetailVO.setProviderReview("N");
            
            ActionForm addressForm=(ActionForm)generalForm.get("addressVO");
            ActionForm hospitalAuditForm=(ActionForm)generalForm.get("hospitalAuditVO");
            ActionForm documentDetailForm=(ActionForm)generalForm.get("documentDetailVO");
            addressVO=(AddressVO)FormUtils.getFormValues(addressForm,"frmHospitalAddress",this,mapping,request);
            docuDetailVO=(DocumentDetailVO)FormUtils.getFormValues(documentDetailForm,"frmDocumentDetailVO",
            			 this,mapping,request);
            //To bring country based on state selection along with cities
            String stateCode	=	addressVO.getStateCode();
            request.getSession().setAttribute("stateCode", stateCode);
            
            hospDetailVO.setAddressVO(addressVO);
            hospitalAuditVO=(HospitalAuditVO)FormUtils.getFormValues(hospitalAuditForm,"frmHospitalAuditVO",
            				 this,mapping,request);
            hospDetailVO.setHospitalAuditVO(hospitalAuditVO);
            hospDetailVO.setDocumentDetailVO(docuDetailVO);
            hospDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));////User Id
            if(generalForm.get("emplStatusTypeId")!=null && generalForm.
            		get("emplStatusTypeId").toString().equalsIgnoreCase("EMP"))
            {
            	//set the hospital name, pan number and category to session
                request.getSession().setAttribute("flgHospName",generalForm.get("hospitalName"));
                request.getSession().setAttribute("flgPanNumber",generalForm.get("panNmbr"));
                request.getSession().setAttribute("flgCategory",generalForm.get("categoryID"));
            }//end of if(generalForm.get("emplStatusTypeId")!=null && generalForm.get("emplStatusTypeId").toString().equalsIgnoreCase("EMP"))
            return hospDetailVO;
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp,strHospSearch);
        }//end of catch
    }//end of getFormValues(DynaActionForm generalForm,ActionMapping mapping,HttpServletRequest request)

    /**
     * Populates the value object to form element.
     * @param hospDetailVO HospitalDetailVO object.
     * @param mapping The ActionMapping used to select this instance.
     * @param request HttpServletRequest  object which contains hospital information.
     * @return DynaActionForm object.
     */
    private DynaActionForm setFormValues(HospitalDetailVO hospitalDetailVO,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
        try
        {
            DynaActionForm hospitalForm = (DynaActionForm)FormUtils.setFormValues("frmAddHospital",
            							   hospitalDetailVO,this,mapping,request);
            if(hospitalDetailVO.getAddressVO()!=null)
            {
                hospitalForm.set("addressVO", (DynaActionForm)FormUtils.setFormValues("frmHospitalAddress",
                		hospitalDetailVO.getAddressVO(),this,mapping,request));
            }//end of if(hospitalDetailVO.getAddressVO()!=null)
            else
            {
                hospitalForm.set("addressVO", (DynaActionForm)FormUtils.setFormValues("frmHospitalAddress",
                		new AddressVO(),this,mapping,request));
            }//end of else
            hospitalForm.set("hospitalAuditVO", (DynaActionForm)FormUtils.setFormValues("frmHospitalAuditVO",
            		new HospitalAuditVO(),this,mapping,request));
            if(hospitalDetailVO.getDocumentDetailVO()!=null)
            {
                hospitalForm.set("documentDetailVO", (DynaActionForm)FormUtils.setFormValues("frmDocumentDetailVO",
                		hospitalDetailVO.getDocumentDetailVO(),this,mapping,request));
            }//end of if(hospitalDetailVO.getDocumentDetailVO()!=null)
            else
            {
                hospitalForm.set("documentDetailVO", (DynaActionForm)FormUtils.setFormValues("frmDocumentDetailVO",
                		new DocumentDetailVO(),this,mapping,request));
            }//end of else
            //if change in empanelled hospital name found, for capturing the reference detail informaiton
            if(hospitalDetailVO.getEmplStatusTypeId()!=null && hospitalDetailVO.getEmplStatusTypeId().
            		equalsIgnoreCase("EMP"))
            {
                //set the hospital name, pan number and category to session
                request.getSession().setAttribute("flgHospName",hospitalDetailVO.getHospitalName());
                request.getSession().setAttribute("flgPanNumber",hospitalDetailVO.getPanNmbr());
                request.getSession().setAttribute("flgCategory",hospitalDetailVO.getCategoryID());
            }//end of if(hospitalDetailVO.getEmplStatusTypeId()!=null && hospitalDetailVO.getEmplStatusTypeId().equalsIgnoreCase("EMP"))
            return hospitalForm;
        }
        catch(Exception exp)
        {
            throw new TTKException(exp,strHospSearch);
        }//end of catch
    }//end of setFormValues(HospitalDetailVO hospitalDetailVO,ActionMapping mapping,HttpServletRequest request)

    
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
     */
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
     */
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
    
    
    /**
     * This method is used to GenerateIds based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
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

    
    /**
     * This method is Navigate to the Provider DashBoard
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
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
    
    /**
     * This method is used to GenerateIds based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
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

    
    
    /**
     * This method is used to GenerateIds based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
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

    
    
    /**
     * This method is used to doSendMail based on Professional ID and Hospital name 
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
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
    		
    		/**
    		 * Code to check duplicate network orders and Network Code S T A R T S HERE
    		 */
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
    		/**
    		 * Code to check duplicate network orders and Network Code E N D S HERE
    		 */
    		
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
    
    
    
    /*
     * Do Default for Haad Factors -  to show a jsp where user can Enter HAAD Factors
     */
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
    		/*if(iResult>0)
        		return mapping.findForward("haadFactorsExisted");
    		else*/
    		String hosp_seq_id	=	request.getParameter("hosp_seq_id");
    		String primaryNetwork	=	(String) request.getParameter("networkType");
    		ArrayList alHistoryOfTariffUpdates,alHaadCategories	=	null;
    		alHistoryOfTariffUpdates	=	hospitalObject.getHaadFactorsHostory(hosp_seq_id);
    		request.getSession().setAttribute("alHistoryOfTariffUpdates", alHistoryOfTariffUpdates);
    		//  
    		
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

   
    /*
     * 
     */
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
    		
    		/* to retrieve existing data*/
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

    
	public ActionForward doProviderDiscountConfiguration(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside FloatAccountAction doProviderDiscountConfiguration");
			setLinks(request);
			TableData tableData = null;

			if (request.getSession().getAttribute("tableData") != null) {
				tableData = (TableData) (request.getSession()).getAttribute("tableData");
			}// end of if(request.getSession().getAttribute("tableData")!=null)

			String hosp_seq_id = (String) request.getParameter("hosp_seq_id");
			request.getSession().setAttribute("hospSeqId", hosp_seq_id);

			return mapping.findForward(strProviderDiscountCfg);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, strHospSearch));
		}// end of catch(Exception exp)
	}// end of doViewAssociatedCorp(ActionMapping mapping,ActionForm form,


    public ActionForward doviewPayment(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
    	try{
    				log.info("Inside the doDefault doviewPayment of HospitalSearchAction");
    				setLinks(request);
    				//String strActiveSubLink=TTKCommon.getActiveSubLink(request);			
    				DynaActionForm  frmFastTrack = (DynaActionForm)form;
    				frmFastTrack.initialize(mapping);
    				String hosp_seq_id	=	(String)request.getParameter("hosp_seq_id");

    				HospitalManager hospitalManagerObject=this.getHospitalManagerObject();

    				TableData tableData = (TableData) (request.getSession().getAttribute("fastTracktableData")==null?new TableData():request.getSession().getAttribute("fastTracktableData"));
    				TableData tableData1 = (TableData) (request.getSession().getAttribute("volumeBasedtableData")==null?new TableData():request.getSession().getAttribute("volumeBasedtableData"));
    				
    				// create the required grid table
    				tableData.createTableInfo("FastTrackPaymentTable",null);
    				tableData1.createTableInfo("VolumeBasedPaymentTable",null);
    							
    				long hosp_Seq_Id = Long.parseLong(hosp_seq_id);
    	    		ArrayList<Object> alSearchData	=	new ArrayList<Object>();
    	    		alSearchData.add(hosp_Seq_Id);
    	    		alSearchData.add("FAST");
    	    		alSearchData.add("IN");
    	    		tableData.setSearchData(alSearchData);
    	    		ArrayList alFastDiscountDetails	=	hospitalManagerObject.getProviderFastTrackDiscount(tableData.getSearchData());
    	    		tableData.setData(alFastDiscountDetails);
    	    		((Column)((ArrayList)tableData.getTitle()).get(6)).setLinkTitle("CHECKHIPERLINK");
    	    		//For Volume Based
    	    		ArrayList<Object> alSearchData1	=	new ArrayList<Object>();
    	    		alSearchData1.add(hosp_Seq_Id);
    	    		alSearchData1.add("VOL");
    	    		alSearchData1.add("IN");
    	    		tableData1.setSearchData(alSearchData1);
    	    		ArrayList alVolDiscountDetails	=	hospitalManagerObject.getProviderVolumeDiscount(tableData1.getSearchData());
    	    		tableData1.setData(alVolDiscountDetails);

    				request.getSession().setAttribute("fastTracktableData",tableData);
    				request.getSession().setAttribute("volumeBasedtableData",tableData1);
    				request.getSession().setAttribute("hospSeqId", hosp_seq_id);
    				String enableHyperLink="enableHyperLink";
    				request.getSession().setAttribute("enableHyperLink", enableHyperLink);

    				return mapping.findForward(strProviderDiscountCfgDetails);		
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    	}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    public ActionForward doFastTrackSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doFastTrackSave method of HospitalSearchAction");
    		setLinks(request);
    		HospitalManager hospitalManagerObject=this.getHospitalManagerObject();
    		//String strActiveSubLink=TTKCommon.getActiveSubLink(request);
    		int iResult=0;
    		DynaActionForm  frmFastTrack = (DynaActionForm)form;
    		frmFastTrack.set("updatedBy",TTKCommon.getUserSeqId(request).toString());
    		HospitalDetailVO hospitalDetailVO = (HospitalDetailVO)FormUtils.getFormValues(frmFastTrack, this, mapping, request);
    		ArrayList<Object> alHospDiscount	=	new ArrayList();	
    		String hosp_seq_id	=	(String)request.getParameter("hospSeqId");
    		long hosp_Seq_Id = Long.parseLong(hosp_seq_id);
    		String UpdatedBy=TTKCommon.getUserSeqId(request).toString();
    		
    		alHospDiscount.add(hospitalDetailVO.getDiscountSeqId());//0
    		alHospDiscount.add(hosp_Seq_Id);//1
    		alHospDiscount.add("FAST");//2
    		alHospDiscount.add("0");//3 Need to be remove Not in use form UI part
    		alHospDiscount.add(hospitalDetailVO.getfToDays());//4
      		alHospDiscount.add("");//5
      		alHospDiscount.add((long)0);//6
      		alHospDiscount.add((long)0);//7
    		alHospDiscount.add(hospitalDetailVO.getfDiscountPerc());//8
    		alHospDiscount.add(UpdatedBy);//9
    		alHospDiscount.add(hospitalDetailVO.getfStartDate());//10
    		alHospDiscount.add(hospitalDetailVO.getfEndDate());//11
    		alHospDiscount.add(hospitalDetailVO.getfStatus());//12
    		request.getSession().removeAttribute("alHospDiscount");
    		iResult = hospitalManagerObject.saveProviderDiscountCnfgSave(alHospDiscount);
    	
    		if(iResult>0){
    			//saved successfully
    			request.setAttribute("updated","message.saved");
    		}//end of if(iResult>0) 
    		
    		TableData tableData = (TableData) (request.getSession().getAttribute("fastTracktableData")==null?new TableData():request.getSession().getAttribute("fastTracktableData"));
    		ArrayList<Object> alSearchData	=	new ArrayList<Object>();
    		alSearchData.add(hosp_Seq_Id);
    		alSearchData.add("FAST");
    		alSearchData.add("IN");
    		tableData.setSearchData(alSearchData);
    		ArrayList alDiscountDetails	=	hospitalManagerObject.getProviderFastTrackDiscount(tableData.getSearchData());
    		tableData.setData(alDiscountDetails);
    		request.getSession().setAttribute("fastTracktableData",tableData);
    		request.getSession().setAttribute("hospSeqId", hosp_seq_id);
    		frmFastTrack.initialize(mapping);
    		return mapping.findForward(strProviderDiscountCfgDetails);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
    }//end of doFastTrackSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    public ActionForward doVolumeSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doVolumeSave method of HospitalSearchAction");
    		setLinks(request);
    		HospitalManager hospitalManagerObject=this.getHospitalManagerObject();
    		//String strActiveSubLink=TTKCommon.getActiveSubLink(request);
    		int iResult=0;
    		DynaActionForm  frmFastTrack = (DynaActionForm)form;
    		frmFastTrack.set("updatedBy",TTKCommon.getUserSeqId(request).toString());
    		HospitalDetailVO hospitalDetailVO = (HospitalDetailVO)FormUtils.getFormValues(frmFastTrack, this, mapping, request);
    		ArrayList<Object> alHospDiscount	=	new ArrayList();
    		String hosp_seq_id	=	(String)request.getParameter("hospSeqId");	
    		long hosp_Seq_Id = Long.parseLong(hosp_seq_id);
    		String UpdatedBy=TTKCommon.getUserSeqId(request).toString();
    		
    		alHospDiscount.add(hospitalDetailVO.getDiscountSeqId());//0
    		alHospDiscount.add(hosp_Seq_Id);//1
    		alHospDiscount.add("VOL");//2
    		alHospDiscount.add("");//3
      		alHospDiscount.add("");//4
    		alHospDiscount.add(hospitalDetailVO.getvDiscountType());//5
    		alHospDiscount.add(hospitalDetailVO.getvAmountStartRange());//6
    		alHospDiscount.add(hospitalDetailVO.getvAmountEndRange());//7
    		alHospDiscount.add(hospitalDetailVO.getvDiscountPerc());//8
    		alHospDiscount.add(UpdatedBy);//9
      		alHospDiscount.add(hospitalDetailVO.getvStartDate());//10
      		alHospDiscount.add(hospitalDetailVO.getvEndDate());//11
    		alHospDiscount.add(hospitalDetailVO.getvStatus());//12    
    		request.getSession().removeAttribute("alHospDiscount");
    		iResult = hospitalManagerObject.saveProviderDiscountCnfgSave(alHospDiscount);
    		
    		if(iResult>0){
    			//saved successfully
    			request.setAttribute("updated","message.saved");
    		}//end of if(iResult>0) 
    		
    		TableData tableData = (TableData) (request.getSession().getAttribute("volumeBasedtableData")==null?new TableData():request.getSession().getAttribute("volumeBasedtableData"));
    		ArrayList<Object> alSearchData	=	new ArrayList<Object>();
    		alSearchData.add(hosp_Seq_Id);
    		alSearchData.add("VOL");
    		alSearchData.add("IN");
    		tableData.setSearchData(alSearchData);
    		ArrayList alDiscountDetails	=	hospitalManagerObject.getProviderVolumeDiscount(tableData.getSearchData());
    		tableData.setData(alDiscountDetails);
    		request.getSession().setAttribute("volumeBasedtableData",tableData);
    		request.getSession().setAttribute("hospSeqId", hosp_seq_id);
    		frmFastTrack.initialize(mapping);
    		return mapping.findForward(strProviderDiscountCfgDetails);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
    	}//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    public ActionForward doProviderDiscountClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{
			log.info("Inside the doClose method of ConfigurationAction");
			setLinks(request);
			return mapping.findForward(strHospgenInfo);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
	}//end of doProviderDiscountClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	
    
    
    public ActionForward getFastTrackDiscount(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{
			log.info("Inside the doClose method of ConfigurationAction");
			setLinks(request);
			DynaActionForm  frmFastTrack = (DynaActionForm)form;
			TableData tableData = (TableData) (request.getSession().getAttribute("fastTracktableData")==null?new TableData():request.getSession().getAttribute("fastTracktableData"));
			HospitalDetailVO hospitalDetailVO = (HospitalDetailVO)tableData.getRowInfo(Integer.parseInt((String)(frmFastTrack).get("rownum")));
			frmFastTrack.set("fToDays", hospitalDetailVO.getfToDays());
			frmFastTrack.set("fDiscountPerc", hospitalDetailVO.getfDiscountPerc()+"");
			frmFastTrack.set("fStartDate", hospitalDetailVO.getfStartDate());
			frmFastTrack.set("fEndDate", hospitalDetailVO.getfEndDate());
			frmFastTrack.set("discountSeqId", hospitalDetailVO.getDiscountSeqId()+"");
			request.getSession().setAttribute("frmFastTrack", frmFastTrack);
			return mapping.findForward("providerDiscountCfgDetails");
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
	}

    public ActionForward doConfReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			   HttpServletResponse response) throws Exception{
    			log.debug("Inside the doReset method of AdminPoliciesAction");
    			return doProviderDiscountConfiguration(mapping,form,request,response);
    }//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    public ActionForward doDisplayActivityLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{
			log.info("Inside the doDisplayActivityLog() of HospitalSearchAction");
			setLinks(request);
			DynaActionForm  frmAddHospital = (DynaActionForm)form;
			
			HospitalManager hospitalManagerObject=this.getHospitalManagerObject();
			TableData  tableData =TTKCommon.getTableData(request);
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			
			if(!("").equals(strPageID) || !("").equals(strSortID))
			{
				if(!("").equals(strPageID))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return mapping.findForward("displayActivityLog");
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
				tableData.createTableInfo("PaymentActivityLogTable",null);
				tableData.setSearchData(this.populateSearchCriteriaForLog((DynaActionForm)form,request));
				tableData.modifySearchData("search");
			}//end of else
			ArrayList alPolicy= hospitalManagerObject.getActivityLogList(tableData.getSearchData());
			tableData.setData(alPolicy,"search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			
			String hospSeqId =  frmAddHospital.getString("hospSeqId");
			
			
			hospSeqId = (hospSeqId == null || hospSeqId.length() < 1) ? "" : hospSeqId; 
			request.setAttribute("hospSeqId", hospSeqId);
			
			request.setAttribute("oldPaymentDuration", frmAddHospital.getString("oldPaymentDuration"));
			request.setAttribute("oldPaymentTermAgrSDate", frmAddHospital.getString("oldPaymentTermAgrSDate"));
			request.setAttribute("oldPaymentTermAgrEDate", frmAddHospital.getString("oldPaymentTermAgrEDate"));
			
			return mapping.findForward("displayActivityLog");
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
	}
    
    private ArrayList<Object> populateSearchCriteriaForLog(DynaActionForm frmAddHospital,HttpServletRequest request)
    {
		ArrayList<Object> alSearchParam=new ArrayList<Object>();
		String hospSeqId =  frmAddHospital.getString("hospSeqId");
		hospSeqId = (hospSeqId == null || hospSeqId.length() < 1) ? "0" : hospSeqId; 
		alSearchParam.add(new Long(hospSeqId)); //0 :  hospSeqId
		return alSearchParam;
    }
    
    public ActionForward doCloseActivityLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{
			log.info("Inside the doCloseActivityLog method of HospitalSearchAction");
			setLinks(request);
			DynaActionForm frmAddHospital=(DynaActionForm)request.getSession().getAttribute("frmAddHospital");
			String oldPaymentDuration = frmAddHospital.getString("oldPaymentDuration");
			String oldPaymentTermAgrSDate = frmAddHospital.getString("oldPaymentTermAgrSDate");
			String oldPaymentTermAgrEDate =frmAddHospital.getString("oldPaymentTermAgrEDate");
			
			frmAddHospital.set("oldPaymentDuration", oldPaymentDuration);
			frmAddHospital.set("oldPaymentTermAgrSDate", oldPaymentTermAgrSDate);
			frmAddHospital.set("oldPaymentTermAgrEDate", oldPaymentTermAgrEDate);
			
			frmAddHospital.set("paymentDuration", oldPaymentDuration);
			frmAddHospital.set("paymentTermAgrSDate", oldPaymentTermAgrSDate);
			frmAddHospital.set("paymentTermAgrEDate", oldPaymentTermAgrEDate);
			
			request.getSession().setAttribute("frmAddHospital",frmAddHospital);
			
			return mapping.findForward("closeActivityLog");
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strHospSearch));
		}//end of catch(Exception exp)
	} // end of ActionForward doCloseActivityLog()
    
}//end of class HospitalSearchAction