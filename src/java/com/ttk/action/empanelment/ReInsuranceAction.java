/**
* @ (#) InsuranceCompanyAction.java Nov 21, 2005
* Project 		: TTK HealthCare Services
* File 			: InsuranceCompanyAction.java
* Author 		: Pradeep R
* Company 		: Span Systems Corporation
* Date Created 	: Nov 21, 2005
*
* @author 		: Pradeep R
* Modified by 	: Lancy A
* Modified date : Mar 10, 2006
* Reason 		: Changes in coding standards
*/

package com.ttk.action.empanelment;

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
import com.ttk.business.empanelment.InsuranceManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.common.security.Cache;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.InsuranceDetailVO;
import com.ttk.dto.empanelment.InsuranceVO;
import com.ttk.dto.empanelment.MailNotificationVO;
import com.ttk.dto.empanelment.ReInsuranceDetailVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
* This class is used to search the insurance company,add
* that to webboard. and also has the mode add/edit and delete the
* insurance companies at various level.
*/

public class ReInsuranceAction extends TTKAction {

	private static Logger log = Logger.getLogger( InsuranceCompanyAction.class );
    //declaration of modes
    private static final String strForward="Forward";
    private static final String strBackward="Backward";
    //declaration of forward paths
    private static final String strInsuranceList="reinsurancelist";
    private static final String strCompanySummary="companysummary";
    private static final String strInscompanyDetail="editinscompany";
    private static final String strAddInscompanyDetail="addinscompany";
       
    //Exception Message Identifier
    private static final String strInsError="insurance";
    
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
    		log.debug("Inside the doDefault method of ReInsuranceAction");
    		setLinks(request);
    		TableData tableData = null;
    		tableData=(request.getSession().getAttribute("ReInsuranceTable")!=null?
    				(TableData)request.getSession().getAttribute("ReInsuranceTable"):new TableData());
            tableData.createTableInfo("ReInsuranceTable",new ArrayList());
            request.getSession().setAttribute("ReInsuranceTableData",tableData);
            ((DynaActionForm)form).initialize(mapping);//reset the form data
            return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doAddReInsure(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doAdd method of InsuranceCompanyAction");
    		setLinks(request);
    		DynaActionForm frmReInsCompanyDetails = (DynaActionForm)form;
    		frmReInsCompanyDetails.initialize(mapping);
    		ArrayList alCityList = new ArrayList();
    		HashMap hmCityList = null;
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
            hmCityList=hospitalObject.getCityInfo("DOH");
            String countryCode	=	(String)(hmCityList.get("CountryId"));
            int isdcode	=	Integer.parseInt((hmCityList.get("isdcode").toString()));
            int stdcode	=	0;
            if(!"".equals(hmCityList.get("stdcode").toString()))
            stdcode	=	Integer.parseInt((hmCityList.get("stdcode").toString()));
            AddressVO addressVO=new AddressVO();
    		addressVO.setStateCode("DOH");
            addressVO.setCountryCode(countryCode);		
            addressVO.setIsdCode(isdcode);		
            addressVO.setStdCode(stdcode);
			request.getSession().setAttribute("alStateCode",TTKCommon.getStates("134"));			
    		if(hmCityList!=null){
            	alCityList = (ArrayList)hmCityList.get(addressVO.getStateCode());
            }//end of if(hmCityList!=null)

            if(alCityList==null){
            	alCityList=new ArrayList();
            }
            frmReInsCompanyDetails.set("addressVO", FormUtils.setFormValues("frmInsCompanyAddress",addressVO,this,mapping,request));
    		frmReInsCompanyDetails.set("alCityList",alCityList);
    		frmReInsCompanyDetails.set("caption", "Add");
            request.getSession().setAttribute("frmReInsCompanyDetails",frmReInsCompanyDetails);
            return this.getForward(strAddInscompanyDetail, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    
    public ActionForward doChangeState(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doChangeState method of InsuranceCompanyAction");
    		setLinks(request);
    		DynaActionForm frmReInsCompanyDetails = (DynaActionForm)form;
    		ReInsuranceDetailVO insDetailVO=null;
    		HospitalManager hospitalObject=this.getHospitalManagerObject();
            HashMap hmCityList = null;
            ArrayList alCityList = null;
            
    		insDetailVO=this.getFormValues(frmReInsCompanyDetails,mapping,request);
    		String stateCode	=	(String)insDetailVO.getAddressVO().getStateCode();
            request.getSession().setAttribute(stateCode, "stateCode");
            hmCityList=hospitalObject.getCityInfo(stateCode);
    		//hmCityList=hospitalObject.getCityInfo();
    		if(hmCityList!=null){
    			alCityList = (ArrayList)hmCityList.get(insDetailVO.getAddressVO().getStateCode());
    		}//end of if(hmCityList!=null)
    		if(alCityList==null){
    			alCityList=new ArrayList();
    		}//end of if(alCityList==null)
            String countryCode	=	(String)(hmCityList.get("CountryId"));
            int isdcode	=	TTKCommon.getInt((String)(hmCityList.get("isdcode")));
            int stdcode	=	TTKCommon.getInt((String)(hmCityList.get("stdcode")));
            
            if(insDetailVO.getAddressVO()!=null)
            {
            	insDetailVO.getAddressVO().setCountryCode(countryCode);
            	insDetailVO.getAddressVO().setIsdCode(isdcode);
            	insDetailVO.getAddressVO().setStdCode(stdcode);
            	frmReInsCompanyDetails.set("addressVO", (DynaActionForm)FormUtils.setFormValues("frmInsCompanyAddress",insDetailVO.getAddressVO(),this,mapping,request));
            }//end of if(insDetailVO.getAddress()!=null)
            
//            frmReInsCompanyDetails.set("frmChanged","changed");
            frmReInsCompanyDetails.set("alCityList",alCityList);
    		return this.getForward(strAddInscompanyDetail,mapping,request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doClose method of InsuranceCompanyAction");
    		setLinks(request);
    		return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    public ActionForward doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doSave method of InsuranceCompanyAction");
    		setLinks(request);
    		InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			HospitalManager hospitalObject=this.getHospitalManagerObject();
            DynaActionForm frmReInsCompanyDetails = (DynaActionForm)form;
            ReInsuranceDetailVO insDetailVO=new ReInsuranceDetailVO();
            insDetailVO=this.getFormValues(frmReInsCompanyDetails,mapping,request);
            HashMap hmCityList = null;
            ArrayList alCityList = new ArrayList();
			Long lHeadOffInsSeqId=null;
			StringBuffer sbfCaption=new StringBuffer();
			HashMap<Object,Object> hmOfficeType=new HashMap<Object,Object>();
            //prepare the vo to add/edit the compnay
            
            //call the DAO method to update the Compnay
//            Long lInsSeqId=0L;
			if(insDetailVO.getReins_seq_id()==null){
				insDetailVO.setReins_seq_id(null);
				insDetailVO.setAddr_seq_id(null);
				insDetailVO.setReins_bank_seq_id(null);
				insDetailVO.setTpa_office_seq_id(null);
				insDetailVO.setEmpanelmentDate(null);
				insDetailVO.setActiveYN(null);
				insDetailVO.setInactivatedDate(null);
			}
			insDetailVO.setAddedBy(TTKCommon.getUserSeqId(request));
            int lInsSeqId=0;
            int reinsSeqId=0;
            ArrayList outputData=insuranceObject.addUpdateReInsurance(insDetailVO);
            if(outputData.size()>=2){
            	reinsSeqId=(Integer)outputData.get(0);
            	lInsSeqId=(Integer)outputData.get(1);
            }
            
            if(lInsSeqId>0)
            {
                if(reinsSeqId==0)    //executed when office is added
                {
                    String strCompanyName=insDetailVO.getCompanyName();
                    //initialize the form bean and InsuranceDetailVO
                    frmReInsCompanyDetails.initialize(mapping);
                    sbfCaption.append("Add");
                    request.setAttribute("updated","message.addedSuccessfully");
                }//end of if(frmCompanyDetails.get("insCompSeqId").equals(""))
                else
                {
                	ArrayList inputData=new ArrayList();
                	inputData.add(reinsSeqId);
                    insDetailVO=insuranceObject.getReInsuranceCompanyDetail(inputData);
                    //this.valueObjectToForm(insDetailVO,frmCompanyDetails,request);
                    sbfCaption.append("Edit");
                    if(insDetailVO.getOfficeType().equals("IHO"))
                    {
                        Cache.refresh("insuranceCompany");
                        request.setAttribute("cacheId",String.valueOf(lInsSeqId));
                        request.setAttribute("cacheDesc",insDetailVO.getCompanyName());
                        // to modify the changes in webboard for chenges made here
                        TTKCommon.modifyWebBoardId(request);     
                    }//end of if(insDetailVO.getOfficeType().equals("IHO"))
                    else
                    {
                        //if office type is other than the Head office add office info to the caption
//                        sbfCaption.append(" [").append(insDetailVO.getCompanyName()).append("-").append(insDetailVO.getCompanyCode()).append("]");
                    }//end of else
                    hmCityList=hospitalObject.getCityInfo();
                    if(hmCityList!=null){
                    	alCityList = (ArrayList)hmCityList.get(insDetailVO.getAddressVO().getStateCode());
                    }//end of if(hmCityList!=null)

                    if(alCityList==null){
                    	alCityList=new ArrayList();
                    }
                    request.setAttribute("updated","message.savedSuccessfully");
                }//end of else
                frmReInsCompanyDetails=this.setFormValues(insDetailVO,mapping,request);
                frmReInsCompanyDetails.set("caption",sbfCaption.toString());
                frmReInsCompanyDetails.set("alCityList",alCityList);
                request.getSession().setAttribute("frmReInsCompanyDetails",frmReInsCompanyDetails);
                //REFRESH THE CACHE TO GET THE UPDATED PAYER LIST IN DROP LISTS
    			Cache.refresh("payerCode");
    			Cache.refresh("payerCodeSearch");
    			
            }//end of if(lInsSeqId>0)
            return this.getForward(strInscompanyDetail,mapping,request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doSearch method of InsuranceCompanyAction");
    		setLinks(request);
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
            String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
           
            TableData tableData=(request.getSession().getAttribute("ReInsuranceTableData")!=null?
    				(TableData)request.getSession().getAttribute("ReInsuranceTableData"):new TableData());
            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
            //if the page number or sort id is clicked
            if(!strPageID.equals("") || !strSortID.equals(""))
            {
            	if(!strPageID.equals(""))
                {
            		tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
                    return mapping.findForward(strInsuranceList);
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
                tableData.createTableInfo("ReInsuranceTable",null);
                tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
                tableData.modifySearchData("search");
            }//end of else
            ArrayList alCompanies= insuranceObject.getReInsuranceCompanyList(tableData.getSearchData());
            tableData.setData(alCompanies, "search");
			//set the table data object to session
			request.getSession().setAttribute("ReInsuranceTableData",tableData);
			//finally return to the grid screen
			return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsuranceList));
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
    		log.debug("Inside the doBackward method of InsuranceCompanyAction");
    		setLinks(request);
    		TableData tableData=(request.getSession().getAttribute("ReInsuranceTableData")!=null?
    				(TableData)request.getSession().getAttribute("ReInsuranceTableData"):new TableData());
            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
            ArrayList alCompanies = insuranceObject.getReInsuranceCompanyList(tableData.getSearchData());
            tableData.setData(alCompanies, strBackward);//set the table data
            request.getSession().setAttribute("ReInsuranceTableData",tableData);//set the table data object to session
            return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsuranceList));
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
    		log.debug("Inside the doForward method of InsuranceCompanyAction");
    		setLinks(request);
    		TableData tableData=(request.getSession().getAttribute("ReInsuranceTableData")!=null?
    				(TableData)request.getSession().getAttribute("ReInsuranceTableData"):new TableData());
            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
            ArrayList alCompanies = insuranceObject.getReInsuranceCompanyList(tableData.getSearchData());
            tableData.setData(alCompanies, strForward);//set the table data
            request.getSession().setAttribute("ReInsuranceTableData",tableData);//set the table data object to session
            return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsuranceList));
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
    		log.debug("Inside the doCopyToWebBoard method of InsuranceCompanyAction");
    		setLinks(request);
    		this.populateWebBoard(request);
			return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    public ActionForward doViewCompanySummary(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doViewCompanySummary method of InsuranceCompanyAction");
    		setLinks(request);
    		if(request.getSession().getAttribute("reInsuranceDetailVO")==null)
            {
                request.setAttribute("ErrorDisplay",true);//when web board is empty we are setting the attribute
                TTKException expTTK = new TTKException();
                expTTK.setMessage("error.reinurancecompnay.required");
                throw expTTK;
            }else{
            	ArrayList alCityList = new ArrayList();
        		DynaActionForm frmReInsCompanyDetails=(DynaActionForm)form;
                ReInsuranceDetailVO insuranceVO=(ReInsuranceDetailVO) request.getSession().getAttribute("reInsuranceDetailVO");
                InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
                ReInsuranceDetailVO reInsuranceDetailVO=null;
                this.addToWebBoard(insuranceVO, request);
                ArrayList inputList=new ArrayList<>();
                inputList.add(insuranceVO.getReins_seq_id());
                reInsuranceDetailVO=insuranceObject.getReInsuranceCompanyDetail(inputList);
                frmReInsCompanyDetails=this.setFormValues(reInsuranceDetailVO,mapping,request);
                HashMap hmCityList = null;
        		HospitalManager hospitalObject=this.getHospitalManagerObject();
                hmCityList=hospitalObject.getCityInfo(reInsuranceDetailVO.getAddressVO().getStateCode());
                if(hmCityList!=null){
                	alCityList = (ArrayList)hmCityList.get(reInsuranceDetailVO.getAddressVO().getStateCode());
                }//end of if(hmCityList!=null)

                if(alCityList==null){
                	alCityList=new ArrayList();
                }
                frmReInsCompanyDetails.set("alCityList",alCityList);
                frmReInsCompanyDetails.set("caption", "Edit-"+reInsuranceDetailVO.getReinsurerName());
                request.getSession().setAttribute("frmReInsCompanyDetails", frmReInsCompanyDetails);
                return mapping.findForward(strAddInscompanyDetail);
            }
    		
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doEdit method of InsuranceCompanyAction");
    		setLinks(request);
    		TableData tableData=(request.getSession().getAttribute("ReInsuranceTableData")!=null?
    				(TableData)request.getSession().getAttribute("ReInsuranceTableData"):new TableData());
    		DynaActionForm frmReInsCompanyDetails=(DynaActionForm)form;
    		ReInsuranceDetailVO reInsuranceDetailVO=null;
    		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            {
    			reInsuranceDetailVO = (ReInsuranceDetailVO)tableData.getRowInfo(Integer.parseInt((String)(frmReInsCompanyDetails).get("rownum")));
                //add the selected item to the web board and make it as default selected
//                this.addToWebBoard(reInsuranceDetailVO, request);
            }
    		request.getSession().setAttribute("reInsuranceDetailVO", reInsuranceDetailVO);
            return mapping.findForward(strInscompanyDetail);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInscompanyDetail));
		}//end of catch(Exception exp)
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * This method is called from the struts framework.
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
    //end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    //end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to delete the selected record(s) in Search Grid.
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
    		log.debug("Inside the doDeleteList method of InsuranceCompanyAction");
    		setLinks(request);
    		InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    		TableData tableData = TTKCommon.getTableData(request);
    		String strDeleteId = "";
            //populate the delete string which contains the sequence id's to be deleted
            strDeleteId = populateDeleteId(request, (TableData)request.getSession().getAttribute("tableData"));
            //delete the Insurance Details
            int iCount = insuranceObject.deleteInsuranceCompany(strDeleteId);
            //refresh the grid with search data in session
            ArrayList alInsurance = null;
            //fetch the data from previous set of rowcounts, if all the records are deleted for the current set of search criteria
            if(iCount == tableData.getData().size())
            {
            	tableData.modifySearchData("DeleteList");//modify the search data
                int iStartRowCount = Integer.parseInt((String)tableData.getSearchData().get(tableData.
                					 getSearchData().size()-2));
                if(iStartRowCount > 0)
                {
                	alInsurance = insuranceObject.getInsuranceCompanyList(tableData.getSearchData());
                }//end of if(iStartRowCount > 0)
            }//end if(iCount == tableData.getData().size())
            else
            {
            	alInsurance = insuranceObject.getInsuranceCompanyList(tableData.getSearchData());
            }//end of else
            tableData.setData(alInsurance, "DeleteList");
            request.getSession().setAttribute("tableData",tableData);
            return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }//end of doDeleteList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to delete the selected record.
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
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doDelete method of InsuranceCompanyAction");
    		setLinks(request);
    		InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    		TableData tableData = TTKCommon.getTableData(request);
    		String strDeleteId = "";
            strDeleteId = ""+((InsuranceVO)((TableData)request.getSession().getAttribute("tableData")).
            			  getData().get(Integer.parseInt(request.getParameter("rownum")))).getInsuranceSeqID();
            //delete the Insurance Details
            int iCount = insuranceObject.deleteInsuranceCompany(strDeleteId);
            //refresh the grid with search data in session
            ArrayList alInsurance = null;
            //fetch the data from previous set of rowcounts, if all the records are deleted for the current set of search criteria
            if(iCount == tableData.getData().size())
            {
            	tableData.modifySearchData("Delete");//modify the search data
                int iStartRowCount = Integer.parseInt((String)tableData.getSearchData().
                					 get(tableData.getSearchData().size()-2));
                if(iStartRowCount > 0)
                {
                	alInsurance = insuranceObject.getInsuranceCompanyList(tableData.getSearchData());
                }//end of if(iStartRowCount > 0)
            }//end if(iCount == tableData.getData().size())
            else
            {
            	alInsurance = insuranceObject.getInsuranceCompanyList(tableData.getSearchData());
            }//end of else
            tableData.setData(alInsurance, "Delete");
            request.getSession().setAttribute("tableData",tableData);
            return this.getForward(strInsuranceList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }//end of doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * Returns a string which contains the comma separated sequence id's to be deleted
     * @param request HttpServletRequest object which contains information about the selected check boxes
     * @param tableData TableData object which contains the value objects
     * @return String which contains the comma separated sequence id's to be deleted
     * @throws TTKException If any run time Excepton occures
     */
    private String populateDeleteId(HttpServletRequest request, TableData tableData)throws TTKException
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
                        sbfDeleteId.append("|").append(String.valueOf(((InsuranceVO)tableData.getData().
                        			get(Integer.parseInt(strChk[i]))).getInsuranceSeqID().intValue()));
                    }//end of if(i == 0)
                    else
                    {
                        sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((InsuranceVO)tableData.
                        			  getData().get(Integer.parseInt(strChk[i]))).getInsuranceSeqID().intValue()));
                    }//end of else
                }//end of if(strChk[i]!=null)
            }//end of for(int i=0; i<strChk.length;i++)
            sbfDeleteId=sbfDeleteId.append("|");
        }//end of if(strChk!=null&&strChk.length!=0)
        return sbfDeleteId.toString();
    }//end of populateDeleteId(HttpServletRequest request, TableData tableData)

    /**
     * Returns the InsuranceManager session object for invoking methods on it.
     * @return InsuranceManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private InsuranceManager getInsuranceManagerObject() throws TTKException
	{
		InsuranceManager insuremanager = null;
		try
		{
			if(insuremanager == null)
			{
				InitialContext ctx = new InitialContext();
				insuremanager = (InsuranceManager) ctx.lookup("java:global/TTKServices/business.ejb3/InsuranceManagerBean!com.ttk.business.empanelment.InsuranceManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strInsError);
		}//end of catch
		return insuremanager;
	} // end of private InsuranceManager getInsManagerObject() throws TTKException

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
			throw new TTKException(exp, strInsError);
		}//end of catch
		return hospManager;
	}//end getHospitalManagerObject()

    /**
     * Return the ArrayList populated with Search criteria elements
     * @param DynaActionForm will contain the values that are entered in the corresponding fields
     * @param HttpServletRequest object will contain the searchparameter that is built.
     * @return ArrayList
     * @throws TTKException If any run time Excepton occures
     */
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmSearchInsurance,
			HttpServletRequest request)throws TTKException
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add((String)frmSearchInsurance.get("sCompanyName"));
		alSearchParams.add((String)frmSearchInsurance.get("sEmpanelDate"));
		alSearchParams.add((String)frmSearchInsurance.get("sCompanyCodeNbr"));
    	return alSearchParams;
	}//end of populateSearchCriteria(DynaActionForm userForm)

    /**
	 * Populates the web board in the session with the selected items in the grid
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 * @throws TTKException If any run time Excepton occures
     */
//	private void populateWebBoard(HttpServletRequest request)throws TTKException
//	{
//		String[] strChk = request.getParameterValues("chkopt");
//		TableData tableData = (TableData)request.getSession().getAttribute("ReInsuranceTableData");
//		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
//		ArrayList<Object> alCacheObject = new ArrayList<Object>();
//		CacheObject cacheObject = null;
//		
//		System.out.println("strChk.length :"+strChk.length);
//		if(strChk!=null&&strChk.length!=0)
//		{
//			for(int i=0; i<strChk.length;i++)
//			{
//				cacheObject = new CacheObject();
//				cacheObject.setCacheId(String.valueOf(((ReInsuranceDetailVO)tableData.getData().get(Integer.parseInt(strChk[i]))).getReins_seq_id()));
//				cacheObject.setCacheDesc(((ReInsuranceDetailVO)tableData.getData().get(Integer.parseInt(strChk[i]))).getReinsurerName());
//				alCacheObject.add(cacheObject);
//			}//end of for(int i=0; i<strChk.length;i++)
//		}//end of if(strChk!=null&&strChk.length!=0)
//		if(toolbar != null)
//			toolbar.getWebBoard().addToWebBoardList(alCacheObject);
//	}//end of populateWebBoard(HttpServletRequest request)

    /**
     * Adds the selected item to the web board and makes it as the selected item in the web board
     * @param  insuranceVO InsuranceVO object which contains the information of the Insurance Office
     * @param request HttpServletRequest
     * @throws TTKException if any runtime exception occures
     */
   /* private void addToWebBoard(ReInsuranceDetailVO insuranceVO, HttpServletRequest request)throws TTKException
    {
        Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
        CacheObject cacheObject = new CacheObject();
        cacheObject.setCacheId(String.valueOf(insuranceVO.getReins_seq_id()));
        cacheObject.setCacheDesc(insuranceVO.getReinsurerName());
        ArrayList<Object> alCacheObject = new ArrayList<Object>();
        alCacheObject.add(cacheObject);
        
        //if the object(s) are added to the web board, set the current web board id
        toolbar.getWebBoard().addToWebBoardList(alCacheObject);
        toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());
        request.setAttribute("webboardinvoked", "true");
    }//end of addToWebBoard(InsuranceDetailVO insDetailVO, HttpServletRequest request)
*/
    private void addToWebBoard(ReInsuranceDetailVO insuranceVO, HttpServletRequest request)throws TTKException
    {
    	Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
    	ArrayList<Object> alCacheObject = new ArrayList<Object>();
    	CacheObject cacheObject = new CacheObject();
    	cacheObject.setCacheId(this.prepareWebBoardId(insuranceVO)); //set the cacheID
    	cacheObject.setCacheDesc(insuranceVO.getReinsurerName());
    	alCacheObject.add(cacheObject);
    	//if the object(s) are added to the web board, set the current web board id
    	toolbar.getWebBoard().addToWebBoardList(alCacheObject);
    	toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());

    	//webboardinvoked attribute will be set as true in request scope
    	//to avoid the replacement of web board id with old value if it is called twice in same request scope
    	request.setAttribute("webboardinvoked", "true");
    }
    
    private String prepareWebBoardId(ReInsuranceDetailVO insuranceVO)throws TTKException
    {
    	StringBuffer sbfCacheId=new StringBuffer();
    	sbfCacheId.append(insuranceVO.getReins_seq_id()!=null? String.valueOf(insuranceVO.getReins_seq_id()):" ");
    	sbfCacheId.append("~#~").append(insuranceVO.getReinsurerName()!=null? String.valueOf(insuranceVO.getReinsurerName()):" ");
//    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(insuranceVO.getEnrollmentID()).equals("")?" ":insuranceVO.getEnrollmentID());
//    	sbfCacheId.append("~#~").append(preAuthVO.getEnrollDtlSeqID()!=null?String.valueOf(preAuthVO.getEnrollDtlSeqID()):" ");
//    	sbfCacheId.append("~#~").append(preAuthVO.getPolicySeqID()!=null? String.valueOf(preAuthVO.getPolicySeqID()):" ");
//    	sbfCacheId.append("~#~").append(preAuthVO.getMemberSeqID()!=null? String.valueOf(preAuthVO.getMemberSeqID()):" ");
//    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getClaimantName()).equals("")? " ":preAuthVO.getClaimantName());
//    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getBufferAllowedYN()).equals("")? " ":preAuthVO.getBufferAllowedYN());
//    	sbfCacheId.append("~#~").append(preAuthVO.getClmEnrollDtlSeqID()!=null? String.valueOf(preAuthVO.getClmEnrollDtlSeqID()):" ");
//    	sbfCacheId.append("~#~").append(preAuthVO.getAmmendmentYN()!=null? String.valueOf(preAuthVO.getAmmendmentYN()):" ");
//    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getCoding_review_yn()).equals("")? " ":preAuthVO.getCoding_review_yn());
    	return sbfCacheId.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void populateWebBoard(HttpServletRequest request)throws TTKException
    {
    	String[] strChk = request.getParameterValues("chkopt");
    	TableData tableData = (TableData)request.getSession().getAttribute("ReInsuranceTableData");
    	Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
    	ArrayList<Object> alCacheObject = new ArrayList<Object>();
    	CacheObject cacheObject = null;
    	ReInsuranceDetailVO reInsuranceDetailVO=null;

    	if(strChk!=null&&strChk.length!=0)
    	{
    		for(int i=0; i<strChk.length;i++)
    		{
    			cacheObject = new CacheObject();
    			reInsuranceDetailVO=(ReInsuranceDetailVO)tableData.getData().get(Integer.parseInt(strChk[i]));
    			cacheObject.setCacheId(this.prepareWebBoardId(reInsuranceDetailVO));
    			cacheObject.setCacheDesc(reInsuranceDetailVO.getReinsurerName());
    			alCacheObject.add(cacheObject);
    		}//end of for(int i=0; i<strChk.length;i++)
    	}//end of if(strChk!=null&&strChk.length!=0)
    	if(toolbar != null)
    	{
    		toolbar.getWebBoard().addToWebBoardList(alCacheObject);
    	}//end of if(toolbar != null)
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Populates the value object to form element.
     * @param insDetailVO InsuranceDetailVO object.
     * @param mapping The ActionMapping used to select this instance.
     * @param request HttpServletRequest  current request object
     * @return DynaActionForm object.
     */
    private DynaActionForm setFormValues(ReInsuranceDetailVO insDetailVO,ActionMapping mapping, HttpServletRequest request) throws TTKException
    {
        try
        {
            DynaActionForm frmInsCompanyDetails = (DynaActionForm)FormUtils.setFormValues("frmReInsCompanyDetails",insDetailVO,this,mapping,request);
            if(insDetailVO.getAddressVO()!=null)
            {
                frmInsCompanyDetails.set("addressVO", (DynaActionForm)FormUtils.setFormValues("frmInsCompanyAddress",insDetailVO.getAddressVO(),this,mapping,request));
            }//end of if(insDetailVO.getAddress()!=null)
            else
            {
                frmInsCompanyDetails.set("addressVO", (DynaActionForm)FormUtils.setFormValues("frmInsCompanyAddress",new AddressVO(),this,mapping,request));
            }//end of else
            //added for CR KOC Mail-SMS Notification
           
            return frmInsCompanyDetails;
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, strInsError);
        }//end of catch
    }//end of setFormValues()

    /**
     * Populates the form elements to value object .
     * @param frmInsCompanyDetails DynaActionForm.
     * @param mapping The ActionMapping used to select this instance.
     * @param request HttpServletRequest  cuurent request object
     * @return insDetailVO InsuranceDetailVO object.
     */
    private ReInsuranceDetailVO getFormValues(DynaActionForm frmReInsCompanyDetails,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
        try
        {
        	ReInsuranceDetailVO insDetailVO=null;
            AddressVO addressVO=null;
//            MailNotificationVO mailnotificationVO = null;
           /* if(request.getParameter("companyStatus")==null)
            {
            	frmReInsCompanyDetails.set("companyStatus","N");
            }//end of if(request.getParameter("companyStatus")==null)
*/            insDetailVO=(ReInsuranceDetailVO)FormUtils.getFormValues(frmReInsCompanyDetails,"frmReInsCompanyDetails",this,mapping,request);
            
            ActionForm addressForm=(ActionForm)frmReInsCompanyDetails.get("addressVO");
            //added for CR KOC Mail-SMS Notification for Cigna
            /*ActionForm mailNotificationForm = (ActionForm)frmInsCompanyDetails.get("mailnotificationVO");*/
//            mailnotificationVO = (MailNotificationVO)FormUtils.getFormValues(mailNotificationForm,"frmInsCompanyMailNotification",this,mapping,request);
            addressVO=(AddressVO)FormUtils.getFormValues(addressForm,"frmInsCompanyAddress",this,mapping,request);
            insDetailVO.setAddressVO(addressVO);

            insDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
            return insDetailVO;
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "insurance");
        }//end of catch
    }//end of getFormValues()
} //end of InsuranceCompanyAction