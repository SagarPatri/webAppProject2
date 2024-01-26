/**
 * @ (#) HospitalContactAction.java Sep 24, 2005
 * Project      : TTK HealthCare Services
 * File         : HospitalContactAction.java
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : Sep 24, 2005
 *
 * @author       :  Chandrasekaran J
 * Modified by   : Arun K N
 * Modified date : 4th Nov 2005
 * Reason        : to refresh data when contact type is changed
 */

package com.ttk.action.empanelment;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.empanelment.ContactManager;
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.empanelment.ContactVO;
import com.ttk.dto.empanelment.HospitalDetailVO;
import com.ttk.dto.empanelment.InsuranceVO;
import com.ttk.dto.empanelment.BrokerVO;


/**
 * This class is reusable for adding contacts in Hospital, Insurance, Group Registration and Banks in
 * Empanlement and Finance flows. Adding User in the User Management flow.
 * This class also provides option for reseting the Password.
 */

public class HospitalContactAction extends TTKAction
{
    private static Logger log = Logger.getLogger( HospitalContactAction.class );
    
    //Modes.
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    private static final String strClose="Cancel";
    
    private static final String strHospitalContactPath="hospitalcontactlist";
    private static final String strInsuranceContactPath="insurancecontactlist";
    private static final String strGroupRegistrationContact="groupregistrationcontactlist";
    private static final String strBrokerContactPath="brokercontactlist";//added for broker login kocb
    private static final String strBankContact="bankcontactlist";
    private static final String strCompanySummary="displaycompanysummary";
    private static final String strBrokerSummary="displaybrokersummary";//added for broker login kocb
    private static final String strPartnerContactPath="partnercontactlist";
    
    
    //Exception Message Identifier
    private static final String strContactError="contact";
    
    //intx
    private static String strGeneral		=	"GEN";
    private static String strProfessional	=	"PRO";
    private static final String strReportdisplay="reportdisplay";
    private static final String strReportExp="report";
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
    		log.debug("Inside the doDefault method of HospitalContactAction");
    		setLinks(request);
    		String strSubLink=TTKCommon.getActiveSubLink(request);
    		String strCaption = "";
    		InsuranceVO insuranceVO=null;
    		BrokerVO brokerVO=null;
    		Long lngInsuranceSeqID = null;
            if(strSubLink.equals("Hospital"))
            {
                if(TTKCommon.getWebBoardId(request)==null)
                {
                    TTKException expTTK = new TTKException();
                    expTTK.setMessage("error.hospital.required");
                    throw expTTK;
                }//end of if(TTKCommon.getWebBoardId(request)==null)
            }//end of  if(strSubLink=="Hospital")
    		String strContactPath=this.getForwardPath(request);
    		strCaption = this.buildCaption(request);
    		TableData  tableData =TTKCommon.getTableData(request);
    		//Getting the group seq id from group list action
    		String strGroupSeqId=(String)request.getSession().getAttribute("groupSeqId");
    		//Getting the bank seq id from banktree list action
            String strBankSeqId=(String)request.getSession().getAttribute("bankSeqId");
    		DynaActionForm generalForm = (DynaActionForm)form;
            ((DynaActionForm)form).initialize(mapping);//reset the form data
            //create new table data object
            tableData = new TableData();
            if(request.getAttribute("caption")!=null)
            {
            	generalForm.set("caption",strCaption); //Setting the caption to the form
                generalForm.set("groupSeqId",strGroupSeqId); //Setting the group sequence id to the form
                generalForm.set("bankSeqId",strBankSeqId);
            }//end of if(request.getAttribute("caption")!=null)
            generalForm.set("caption",strCaption);
            if(strSubLink.equals("Hospital"))
            {
                //create the required grid table
               // tableData.createTableInfo("HospitalContactTable",new ArrayList());
                tableData.createTableInfo("HospitalContactTable",new ArrayList());
            }// end of if(strSubLink.equals("Hospital"))
            else
            {
                //create the required grid table
                tableData.createTableInfo("InsuranceContactTable",new ArrayList());
            }// end of else
            request.getSession().setAttribute("tableData",tableData);
            if(strSubLink.equals("Group Registration"))
            {
            	generalForm.set("groupSeqId",strGroupSeqId);
            }//end of if(strSubLink.equals("Group Registration"))
            if(strSubLink.equals("Insurance"))
            {
            	//get the insurance sequence id from session
                insuranceVO = (InsuranceVO)request.getSession().getAttribute("SelectedOffice");
                if(insuranceVO != null){
                	lngInsuranceSeqID = insuranceVO.getInsuranceSeqID();
                }//end of if(insuranceVO != null)
                generalForm.set("insuranceSeqId",lngInsuranceSeqID.toString());
            }//end of if(strSubLink.equals("Insurance"))
            //added for broker login kocb
            if(strSubLink.equals("Broker"))
            {
            	//get the insurance sequence id from session
            	brokerVO = (BrokerVO)request.getSession().getAttribute("SelectedOffice");
            	//log.info(brokerVO);
                if(brokerVO != null){
                	lngInsuranceSeqID = brokerVO.getInsuranceSeqID();
                }//end of if(insuranceVO != null)
                generalForm.set("insuranceSeqId",lngInsuranceSeqID.toString());
             }//end of if(strSubLink.equals("Broker"))
            //end added for  broker login kocb
            if(strSubLink.equals("Banks"))
            {
            	generalForm.set("bankSeqId",strBankSeqId);
            }//end of if(strSubLink.equals("Banks"))
            //log.info("strContactPath>>>"+strContactPath);
            //log.info("strCaption>>>"+strCaption);
            request.getSession().setAttribute("searchparam", null);
            
            
          //-------------------S T A R T intx----------------
            //if the page number or sort id is clicked
         if(strSubLink.equals("Hospital"))
         {
        	 
        	 DynaActionForm frmSearchContact=(DynaActionForm)form; 
        	 tableData.createTableInfo("HospitalContactTable",null);
        	 frmSearchContact.set("caption","General Contacts");
             frmSearchContact.set("buttonLogic","GEN");
        	 
        	 
        	 
        	 
        	 
        	 /*   ContactManager contactObject=this.getContactManagerObject();
              Long lngHospitalSeqId=null;
              String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
              String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
              //GET THE HOSPITAL INFORMATION FROM THE SESSION
              lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
              if(!strPageID.equals("") || !strSortID.equals(""))
              {
                  if(!strPageID.equals(""))
                  {
                      tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
                      return (mapping.findForward(strContactPath));
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
              	//create the required grid table based on the active sublink
                  if(strSubLink.equals("Hospital"))
                  {
                      tableData.createTableInfo("HospitalContactTable",null);
                  }//end of if(strSubLink.equals("Hospital"))
                  
                  tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,
                  		lngHospitalSeqId,lngInsuranceSeqID,null,null));
                  tableData.modifySearchData("search");
              }//end of else
              ArrayList alContact= contactObject.getContactList(tableData.getSearchData());
              DynaActionForm frmSearchContact=(DynaActionForm)form;  //get the DynaActionForm instance
              frmSearchContact.set("caption","General Contacts");
              frmSearchContact.set("buttonLogic","GEN");
              tableData.setData(alContact, "search");
         */}
              
              //---------------E N D intx------------------
            return this.getForward(strContactPath, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    /**
     * This method is used to Switch between Enrollment flow and Endorsement flow
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    
    //intx
    public ActionForward doSwitchTo(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doSwitchTo method of HospitalContactAction");
            DynaActionForm frmSearchContact=(DynaActionForm)form;  //get the DynaActionForm instance
            String strSwitchType=TTKCommon.checkNull((String)frmSearchContact.get("switchType"));
            //call the setlinks method
            //this.setLinks(request,strSwitchType);
            setLinks(request);
            String strSubLink=TTKCommon.getActiveSubLink(request);
            if(strSubLink.equals("Hospital"))
            {
                if(TTKCommon.getWebBoardId(request)==null)
                {
                    TTKException expTTK = new TTKException();
                    expTTK.setMessage("error.hospital.required");
                    throw expTTK;
                }//end of if(TTKCommon.getWebBoardId(request)==null)
            }//end of  if(strSubLink=="Hospital")
            String strContactPath=this.getForwardPath(request);
            //initialize the formbean when user switches the mode
            //frmSearchContact.initialize(mapping);
            frmSearchContact.set("switchType",strSwitchType);
            
           /* TableData tableData=null;
            Long lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
    		Long lngInsuranceSeqID = null;

            //get the table data from session if exists
            if((request.getSession()).getAttribute("tableData") != null)
            {
                tableData = (TableData)(request.getSession()).getAttribute("tableData");
            }//end of if((request.getSession()).getAttribute("tableData") != null)
            else
            {
                tableData = new TableData();
            }//end of else
            String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
            String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
            //if the page number or sort id is clicked
            
            ContactManager contactObject=this.getContactManagerObject();
            ArrayList alContact= new ArrayList();
            
            tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,
              		lngHospitalSeqId,lngInsuranceSeqID,null,null));
            tableData.setSortData("0001");
            
            if(!strPageID.equals("") || !strSortID.equals(""))
            {
                if(!strPageID.equals(""))
                {
                    tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.
                    		getParameter("pageId"))));
                    return (mapping.findForward(strContactPath));
                }///end of if(!strPageID.equals(""))
                else
                {
                    tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
                    tableData.modifySearchData("sort");//modify the search data
                }//end of else
            }//end of if(!strPageID.equals("") || !strSortID.equals(""))
            else
            {
                //create the required grid table based on the SwitchType
                if(strSwitchType.equals(strGeneral))
                {
                    tableData.createTableInfo("HospitalContactTable",null);
                    alContact= contactObject.getContactListIntx(lngHospitalSeqId);
                    tableData.setData(alContact, "search");
                }//end of  if(strSwitchType.equals(strEnrollment))
                else if(strSwitchType.equals(strProfessional))
                {
                    tableData.createTableInfo("ProffessionalDetailTable",null);
                    alContact= contactObject.getLicenseList(lngHospitalSeqId);
                    tableData.setData(alContact, "search");
                }//end of else if(strSwitchType.equals(strEndorsement))

                tableData.setSortOrder("ASC");
                tableData.modifySearchData("search");
            }//end of else
            
            //build the caption
            if(strSwitchType.equals(strGeneral))
            {
                frmSearchContact.set("caption","General Contacts");
            }//end of if(strSwitchType.equals(strGeneral))
            else if(strSwitchType.equals(strProfessional))
            {
                frmSearchContact.set("caption","Clinician Contacts");
            }//end of else if(strSwitchType.equals(strProfessional))
            
            request.getSession().setAttribute("tableData",tableData);
            request.getSession().setAttribute("frmSearchContact",frmSearchContact);
            request.setAttribute("tableData",tableData);

            frmSearchContact.set("buttonLogic",strSwitchType);
*/
            
            
            TableData tableData=null;
            tableData = new TableData();
            if(strSwitchType.equals(strGeneral))
            {
                tableData.createTableInfo("HospitalContactTable",new ArrayList());
                frmSearchContact.set("caption","General Contacts");
            }
            else if(strSwitchType.equals(strProfessional))
            {
                tableData.createTableInfo("ProffessionalDetailTable",new ArrayList());
                frmSearchContact.set("caption","Clinician Contacts");
            }
            
            request.getSession().setAttribute("tableData",tableData);
            request.getSession().setAttribute("frmSearchContact",frmSearchContact);
            request.setAttribute("tableData",tableData);

            frmSearchContact.set("buttonLogic",strSwitchType);
            
            
            
            
            
			//return mapping.findForward(strContactPath);
            return this.getForward(strContactPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
        }//end of catch(Exception exp)
    }//end of doSwitchTo(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    /**
     * This method is used to getProfessionalDetails based on rofessional ID
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward getProfessionalDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the getProfessionalDetails method of HospitalContactAction");
            		setLinks(request);
            		ArrayList alProfessionals	=	null;
            		ContactManager contactObject=this.getContactManagerObject();
            		String profId	=	request.getParameter("profId");
            		
            		alProfessionals= contactObject.getProfessionalDetails(profId);
            		
            		PrintWriter out = response.getWriter();  
        	        response.setContentType("text/xml");  
        	        response.setHeader("Cache-Control", "no-cache");  
        	        response.setStatus(HttpServletResponse.SC_OK);  
        	        if(alProfessionals!=null)
        	        	if(alProfessionals.get(0)!=null || alProfessionals.get(0)!="" || alProfessionals.get(1)!=null || alProfessionals.get(1)!=""
        	        	|| alProfessionals.get(2)!=null || alProfessionals.get(2)!="")
        	        out.write(alProfessionals.get(0)+","+alProfessionals.get(1)+","+alProfessionals.get(2));  
        	        out.flush();  
            		
            		return null;
            	}//end of try
            	catch(TTKException expTTK)
        		{
        			return this.processExceptions(request, mapping, expTTK);
        		}//end of catch(TTKException expTTK)
        		catch(Exception exp)
        		{
        			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
        		}//end of catch(Exception exp)
            }//end of getProfessionalDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to Navigate the Contact Details.
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
    		log.debug("Inside the doSearch method of HospitalContactAction");
    		setLinks(request);
    		InsuranceVO insuranceVO=null;
    		BrokerVO brokerVO=null;
    		Long lngInsuranceSeqID = null;
            Long lngHospitalSeqId=null;
            //added for broker login kocb
            Long lngBrokerSeqID=null;
            ArrayList alContact=null;
            Long lngGroupRegSeqId=null;
            Long lngBankSeqId=null;
    		String strSubLink=TTKCommon.getActiveSubLink(request);
    		//Getting the group seq id from group list action
    		String strGroupSeqId=(String)request.getSession().getAttribute("groupSeqId");
    		//Getting the bank seq id from banktree list action
            String strBankSeqId=(String)request.getSession().getAttribute("bankSeqId");
    		TableData  tableData =TTKCommon.getTableData(request);
    		String strContactPath=this.getForwardPath(request);
    		ContactManager contactObject=this.getContactManagerObject();
    		if(strSubLink.equals("Insurance"))
            {
                //get the insurance sequence id from session
                insuranceVO = (InsuranceVO)request.getSession().getAttribute("SelectedOffice");
                if(insuranceVO != null)
                {
                	lngInsuranceSeqID = insuranceVO.getInsuranceSeqID();
                }//end of if(insuranceVO != null)
            }//end of if (strSubLink=="Insurance")
    		if(strSubLink.equals("Broker"))
            {
                //get the insurance sequence id from session
    			brokerVO = (BrokerVO)request.getSession().getAttribute("SelectedOffice");
                if(brokerVO != null)
                {
                	lngInsuranceSeqID = brokerVO.getInsuranceSeqID();
                }//end of if(insuranceVO != null)
            }//end of if (strSubLink=="Insurance")
            else if(strSubLink.equals("Group Registration"))
            {
            	lngGroupRegSeqId=TTKCommon.getLong(strGroupSeqId); //groupRegistrationVO.getGroupRegSeqID();
            }// end of else if(strSubLink.equals("Group Registration"))
            else if(strSubLink.equals("Banks"))
            {
            	lngBankSeqId=TTKCommon.getLong(strBankSeqId);
            }//end of else if(strSubLink.equals("Banks"))
            else if(strSubLink.equals("Hospital"))
            {
                if(TTKCommon.getWebBoardId(request)==null)
                {
                    TTKException expTTK = new TTKException();
                    expTTK.setMessage("error.hospital.required");
                    throw expTTK;
                }//end of if(TTKCommon.getWebBoardId(request)==null)
                //GET THE HOSPITAL INFORMATION FROM THE SESSION
                lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
            }//end of  else if(strSubLink=="Hospital")
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
            String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
            //if the page number or sort id is clicked
            if(!strPageID.equals("") || !strSortID.equals(""))
            {
                if(!strPageID.equals(""))
                {
                    tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
                    return (mapping.findForward(strContactPath));
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
            	//create the required grid table based on the active sublink
                if(strSubLink.equals("Hospital"))
                {
                    DynaActionForm frmSearchContact=(DynaActionForm)form;  //get the DynaActionForm instance
                    String strSwitchType=TTKCommon.checkNull((String)frmSearchContact.get("switchType"));
                    frmSearchContact.set("switchType",strSwitchType);
        			
              	        if(strSwitchType.equals(strGeneral))
                          {
                              tableData.createTableInfo("HospitalContactTable",null);
                              frmSearchContact.set("caption","General Contacts");
                              
                              tableData.setSearchData(this.populateSearchCriteriaHospital((DynaActionForm)form,request,
                              		lngHospitalSeqId,lngInsuranceSeqID,lngGroupRegSeqId,lngBankSeqId));
                              
                              alContact= contactObject.getHospitalContactList(tableData.getSearchData()); 
                              
                          }
                          else if(strSwitchType.equals(strProfessional))
                          {
                              tableData.createTableInfo("ProffessionalDetailTable",null);
                              frmSearchContact.set("caption","Clinician Contacts");
                              
                              
                              tableData.setSearchData(this.populateSearchCriteriaHospital((DynaActionForm)form,request,
                                		lngHospitalSeqId,lngInsuranceSeqID,lngGroupRegSeqId,lngBankSeqId));
                                
                                alContact= contactObject.getHospitalContactList(tableData.getSearchData()); 
                              
                          }	
                    
                    
                    
                    
                }//end of if(strSubLink.equals("Hospital"))
                else
                {
                    tableData.createTableInfo("InsuranceContactTable",null);
                    
                    tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,
                    		lngHospitalSeqId,lngInsuranceSeqID,lngGroupRegSeqId,lngBankSeqId));
                    tableData.modifySearchData("search");
                     alContact= contactObject.getContactList(tableData.getSearchData());
                }//end of else
               
                tableData.modifySearchData("search");
            }//end of else
           
            tableData.setData(alContact, "search");
            //set the table data object to session
            request.getSession().setAttribute("tableData",tableData);
            //finally return to the grid screen
            return (mapping.findForward(strContactPath));
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    private ArrayList<Object> populateSearchCriteriaHospital(
			DynaActionForm searchContactForm, HttpServletRequest request,
			Long lngHospitalSeqId, Long lngInsuranceSeqID,
			Long lngGroupRegSeqId, Long lngBankSeqId)throws Exception {

    	
    	String strSubLink=TTKCommon.getActiveSubLink(request);
        ArrayList<Object> alSearchParams = new ArrayList<Object>();		

        String strSwitchType=TTKCommon.checkNull((String)searchContactForm.get("switchType"));
      
        
        
       
         alSearchParams.add(lngHospitalSeqId.toString());
         alSearchParams.add((String)searchContactForm.getString("switchType"));
        if(strSwitchType.equals(strGeneral))
        {
        	 alSearchParams.add((String)searchContactForm.getString("contactName"));
        	 alSearchParams.add((String)searchContactForm.getString("designation"));
        	 alSearchParams.add((String)searchContactForm.getString("userRole"));
        	 alSearchParams.add((String)searchContactForm.getString("department"));
        	 alSearchParams.add((String)searchContactForm.getString("userId"));
        }
        else if(strSwitchType.equals(strProfessional))
        {
            
        	 alSearchParams.add((String)searchContactForm.getString("clinicianName"));
        	 alSearchParams.add((String)searchContactForm.getString("speciality"));
        	 alSearchParams.add((String)searchContactForm.getString("consultationType"));
        	 alSearchParams.add((String)searchContactForm.getString("ClinicianId"));
        }	
        
        alSearchParams.add((String)searchContactForm.getString("emailId"));
        alSearchParams.add((String)searchContactForm.getString("status"));
        
			 request.getSession().setAttribute("searchparam",alSearchParams);
        return alSearchParams;
	}


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
    		log.debug("Inside the doChangeWebBoard method of HospitalContactAction");
    		setLinks(request);
    		String strSubLink=TTKCommon.getActiveSubLink(request);
    		String strCaption = "";
    		Long lInsuranceSeqID = null;
    		Long lBrokerSeqID = null;// added for broker login kocb
    		
            if(strSubLink.equals("Hospital"))
            {
                if(TTKCommon.getWebBoardId(request)==null)
                {
                    TTKException expTTK = new TTKException();
                    expTTK.setMessage("error.hospital.required");
                    throw expTTK;
                }//end of if(TTKCommon.getWebBoardId(request)==null)
            }//end of  if(strSubLink=="Hospital")
    		String strContactPath=this.getForwardPath(request);
    		strCaption = this.buildCaption(request);
    		TableData  tableData =TTKCommon.getTableData(request);
    		//Getting the group seq id from group list action
    		String strGroupSeqId=(String)request.getSession().getAttribute("groupSeqId");
    		//Getting the bank seq id from banktree list action
            String strBankSeqId=(String)request.getSession().getAttribute("bankSeqId");
    		DynaActionForm generalForm = (DynaActionForm)form;
    		String switchType="";
    		if(strSubLink.equals("Hospital"))
             {
    		 switchType = generalForm.getString("switchType");
    		if("GEN".equals(switchType)){
    			strCaption="General Contacts";
    		}
    		else{
    			strCaption="Clinician Contacts";
    		}
             }		
    		if(request.getAttribute("caption")!=null)
            {
            	generalForm.set("caption",strCaption); //Setting the caption to the form
                generalForm.set("groupSeqId",strGroupSeqId); //Setting the group sequence id to the form
                generalForm.set("bankSeqId",strBankSeqId);
            }//end of if(request.getAttribute("caption")!=null)
            if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
                ((DynaActionForm)form).initialize(mapping);//reset the form data
    		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))    
            //create new table data object
            tableData = new TableData();
            generalForm.set("caption",strCaption);
            if(strSubLink.equals("Hospital"))
            {
            if(switchType.equals(strGeneral))
            {
                tableData.createTableInfo("HospitalContactTable",new ArrayList());
                generalForm.set("caption","General Contacts");
            }
            else if(switchType.equals(strProfessional))
            {
                tableData.createTableInfo("ProffessionalDetailTable",new ArrayList());
                generalForm.set("caption","Clinician Contacts");
            }	
            }// end of if(strSubLink.equals("Hospital"))
            else
            {
                //create the required grid table
                tableData.createTableInfo("InsuranceContactTable",new ArrayList());
            }// end of else
            request.getSession().setAttribute("tableData",tableData);
            if(strSubLink.equals("Group Registration"))
            {
            	generalForm.set("groupSeqId",strGroupSeqId);
            }//end of if(strSubLink.equals("Group Registration"))
            if(strSubLink.equals("Insurance"))
            {
               generalForm.set("insuranceSeqId",lInsuranceSeqID.toString());
            }//end of if(strSubLink.equals("Insurance"))
            //added for  broker login kocb
            
            if(strSubLink.equals("Broker"))
            {
               generalForm.set("brokerSeqId",lBrokerSeqID.toString());
            }//end of if(strSubLink.equals("Insurance"))
            // end added for  broker login kocb
            if(strSubLink.equals("Banks"))
            {
            	generalForm.set("bankSeqId",strBankSeqId);
            }//end of if(strSubLink.equals("Banks"))
            request.getSession().setAttribute("searchparam", null);
            return this.getForward(strContactPath, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    		log.debug("Inside the doBackward method of HospitalContactAction");
    		setLinks(request);
    		ContactManager contactObject=this.getContactManagerObject();
    		String strContactPath=this.getForwardPath(request);
    		TableData  tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strBackward);//modify the search data
            ArrayList alContact = contactObject.getContactList(tableData.getSearchData());
            tableData.setData(alContact, strBackward);//set the table data
            request.getSession().setAttribute("tableData",tableData);//set the table data object to session
            //finally return to the grid screen
            return (mapping.findForward(strContactPath));
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
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
    		log.debug("Inside the doForward method of HospitalContactAction");
    		setLinks(request);
    		ContactManager contactObject=this.getContactManagerObject();
    		String strContactPath=this.getForwardPath(request);
    		TableData  tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strForward);//modify the search data
            ArrayList alContact = contactObject.getContactList(tableData.getSearchData());
            tableData.setData(alContact, strForward);//set the table data
            request.getSession().setAttribute("tableData",tableData);//set the table data object to session
            //finally return to the grid screen
            return (mapping.findForward(strContactPath));
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to visible/invisible some fields based on the selected Contact Type.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doContactTypeChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doContactTypeChange method of HospitalContactAction");
    		setLinks(request);
    		String strContactPath=this.getForwardPath(request);
    		DynaActionForm hospitalContactForm=(DynaActionForm)form;
            String strContactType=(String)hospitalContactForm.get("contactTypeCode");
            hospitalContactForm.set("contactTypeCode",strContactType);
            return this.getForward(strContactPath,mapping,request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doContactTypeChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to Activate/Deactivate the Contacts/Users.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doActivateInactivate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doActivateInactivate method of HospitalContactAction");
    		setLinks(request);
    		String strActivateInactivateId = "";
            ArrayList<Object> alContactList=new ArrayList<Object>();
            ContactManager contactObject=this.getContactManagerObject();
    		String strContactPath=this.getForwardPath(request);
    		TableData  tableData =TTKCommon.getTableData(request);
            //populate the ActivateInactivate string which contains user sequence id's to be Activated or inactivated
            //if Activate or inactivate from grid screen, get the populated "chkopt" values.
            strActivateInactivateId=(populateActivateInactivateId(request, (TableData)request.getSession().
            		getAttribute("tableData")));
            alContactList.add("|"+strActivateInactivateId+"|");
            alContactList.add(request.getParameter("activeInactive"));
            alContactList.add(TTKCommon.getUserSeqId(request));
            //Activate or inactivate the Contact details
            int iCount = contactObject.activateInActivateContact(alContactList);
            //refresh the grid with search data in session
            /**THIS LINE COMMENTING- BCOZ WE ARE NOT USING TABLE GRID FOR SEARCH THE GENERAL CONTACTS 
             * ArrayList alContact = contactObject.getContactList(tableData.getSearchData());
             */
            Long lngHospitalSeqId	=	TTKCommon.getWebBoardId(request);
           // ArrayList alContact = contactObject.getContactListIntx(lngHospitalSeqId);
            ArrayList  alContact= contactObject.getHospitalContactList(tableData.getSearchData()); 
           //   
            if(alContact.size() == 0 || iCount == tableData.getData().size())
      	    {
      	    	tableData.modifySearchData("Delete");
                int iStartRowCount = Integer.parseInt((String)tableData.getSearchData().get(tableData.
                					 getSearchData().size()-2));
                if(iStartRowCount > 0)
                 {
                	//alContact = contactObject.getContactList(tableData.getSearchData());.
                	//alContact = contactObject.getContactListIntx(lngHospitalSeqId);
                 
                 
                /* tableData.setSearchData(this.populateSearchCriteriaHospital((DynaActionForm)form,request,
                   		lngHospitalSeqId,lngInsuranceSeqID,lngGroupRegSeqId,lngBankSeqId));*/
                   
                   alContact= contactObject.getHospitalContactList(tableData.getSearchData()); 
                   
                 
                 }//end of if(iStartRowCount > 0)
             }//end if(alContact.size() == 0 || iCount == tableData.getData().size())
            tableData.setData(alContact, "Delete");
            request.getSession().setAttribute("tableData",tableData);
            return this.getForward(strContactPath, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doActivateInactivate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    		log.debug("Inside the doClose method of HospitalContactAction");
    		setLinks(request);
    		ContactManager contactObject=this.getContactManagerObject();
    		String strContactPath=this.getForwardPath(request);
    		TableData  tableData =TTKCommon.getTableData(request);
    		if(tableData.getSearchData() != null && tableData.getSearchData().size() > 0)
            {
                //refresh the data in cancel mode, in order to get the new records if any.
                ArrayList alContact = contactObject.getContactList(tableData.getSearchData());
                tableData.setData(alContact, strClose);
                request.getSession().setAttribute("tableData",tableData);
            }//end if

        	return (mapping.findForward(strContactPath));
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to navigate to Company Summary Details.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doCompanySummary(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		return mapping.findForward(strCompanySummary);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doCompanySummary(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
  
    
    //added for broker login kocb
    public ActionForward doBrokerSummary(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		return mapping.findForward(strBrokerSummary);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
		}//end of catch(Exception exp)
    }//end of doCompanySummary(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    //end added for broker login kocb
    
    
    /**
     * Returns a string which contains the Forward Path
     * @param request HttpServletRequest object which contains information required for buiding the Forward Path
     * @return String which contains the comma separated sequence id's to be deleted
     */
    private String getForwardPath(HttpServletRequest request) throws TTKException{
    	String strSubLink=TTKCommon.getActiveSubLink(request);
    	String strContactPath="";
    	try{
    		if(strSubLink.equals("Hospital")){
    			strContactPath=strHospitalContactPath;
    		}//end of if(strSubLink.equals("Hospital"))
    		if(strSubLink.equals("Insurance")){
    			strContactPath=strInsuranceContactPath;
    		}//end of if(strSubLink.equals("Insurance"))
    		if(strSubLink.equals("Group Registration")){
    			strContactPath=strGroupRegistrationContact;
    		}//end of if(strSubLink.equals("Group Registration"))
    		//added for broker login kocb
    		if(strSubLink.equals("Broker")){
    			strContactPath=strBrokerContactPath;
    		}//end of if(strSubLink.equals("Broker"))
    		//added for broker login kocb
    		if(strSubLink.equals("Banks")){
    			strContactPath=strBankContact;
    		}//end of if(strSubLink.equals("Banks"))
    		if(strSubLink.equals("Partner")){
    			strContactPath=strPartnerContactPath;
    		}//end of if(strSubLink.equals("Partner"))
    	}//end of try
    	catch(Exception exp)
        {
            throw new TTKException(exp, strContactError);
        }//end of catch
        return strContactPath;
    }//end of getForwardPath(HttpServletRequest request)
    
    /**
     * Returns a string which contains the Caption
     * @param request HttpServletRequest object which contains information required for building Caption
     * @return String which contains the comma separated sequence id's to be deleted
     */
    public String buildCaption(HttpServletRequest request) throws TTKException{
    	String strSubLink=TTKCommon.getActiveSubLink(request);
    	String strCaption= "";
    	InsuranceVO  insuranceVO=null;
    	BrokerVO  brokerVO=null;
    	String strCompanyName = "";
    	if(strSubLink.equals("Insurance"))
        {
    		insuranceVO = (InsuranceVO)request.getSession().getAttribute("SelectedOffice");
            if(insuranceVO != null){
            	strCompanyName="[";
                strCompanyName+=insuranceVO.getBranchName()+"]";
            }//end of if(insuranceVO != null)
            strCaption = strCompanyName;
    	}//end of if(strSubLink.equals("Insurance")) 
    	else if(strSubLink.equals("Broker"))// CAption Display for Broker in Managing contacts, Added by Kishor 
        {
    		brokerVO = (BrokerVO)request.getSession().getAttribute("SelectedOffice");
            if(brokerVO != null){
            	strCompanyName="[";
                strCompanyName+=brokerVO.getBranchName()+"]";
            }//end of if(insuranceVO != null)
            strCaption = strCompanyName;
    	}//end of if(strSubLink.equals("Broker")) 
    	else{
    		strCaption=(String)request.getAttribute("caption"); //Getting the caption value from grouplist action
    	}//end of else
    	return strCaption;
    }//end of buildCaption(HttpServletRequest request)
    
    /**
     * Returns a string which contains the comma separated sequence id's to be deleted
     * @param request HttpServletRequest object which contains information about the selected check boxes
     * @param tableData TableData object which contains the value objects
     * @return String which contains the comma separated sequence id's to be deleted
     */
    private String populateActivateInactivateId(HttpServletRequest request, TableData tableData)
    {
        String[] strChk = request.getParameterValues("chkopt");
        StringBuffer sbfActivateInactivate = new StringBuffer();
        if(strChk!=null&&strChk.length!=0)
        {
            //loop through to populate ActivateInactivate sequence id's and get the value from session for the matching check box value
            for(int i=0; i<strChk.length;i++)
            {
                if(strChk[i]!=null)
                {
                    //extract the sequence id to be Activated or Inactivated from the value object
                    if(i == 0)
                    {
                        sbfActivateInactivate.append(((ContactVO)tableData.getData().get(Integer.
                        		parseInt(strChk[i]))).getContactSeqId().intValue());
                    }//end of if(i == 0)
                    else
                    {
                        sbfActivateInactivate.append("|").append(((ContactVO)tableData.getData().
                        		get(Integer.parseInt(strChk[i]))).getContactSeqId().intValue());
                    }//end of else
                }//end of if(strChk[i]!=null)
            }//end of for(int i=0; i<strChk.length;i++)
        }//end of if(strChk!=null&&strChk.length!=0)
        return sbfActivateInactivate.toString();
    }//end of populateActivateInactivateId(HttpServletRequest request, TableData tableData)

    /**
     * This method builds all the search parameters to ArrayList and places them in session
     * @param searchContactForm DynaActionForm
     * @param request HttpServletRequest
     * @param lHospitalSeqId which will have long value
     * @param lInsuranceSeqID which will have long value
     * @return alSearchParams ArrayList
     */
    private ArrayList<Object> populateSearchCriteria(DynaActionForm searchContactForm,HttpServletRequest request, Long lHospitalSeqId, Long lInsuranceSeqID, Long lGroupRegSeqId, Long lBankSeqId) throws TTKException
    {
        String strSubLink=TTKCommon.getActiveSubLink(request);
        ArrayList<Object> alSearchParams = new ArrayList<Object>();
        if(strSubLink.equals("Hospital"))
        {
            //build the column names along with their values to be searched
            alSearchParams.add(strSubLink);
            alSearchParams.add(new SearchCriteria("HOSP_SEQ_ID",lHospitalSeqId.toString()));
            String strContactTypeId=(String)searchContactForm.get("contactTypeCode");
            alSearchParams.add(new SearchCriteria("A.CONTACT_TYPE_ID", strContactTypeId));
            alSearchParams.add(new SearchCriteria("CONTACT_NAME", TTKCommon.replaceSingleQots((String)searchContactForm.get("name"))));
            if(searchContactForm.get("userStatus").equals("ACT"))
            {
                alSearchParams.add(new SearchCriteria("ACTIVE_YN","Y","equals"));
            }//end of if(searchContactForm.get("userStatus").equals("ACT"))
            else
            {
                alSearchParams.add(new SearchCriteria("ACTIVE_YN","N","equals"));
            }//end of else
            alSearchParams.add(new SearchCriteria("PROVIDE_ACCESS_USER_YN", (String)searchContactForm.get("applUser1"),"equals"));
            if(!strContactTypeId.equals(""))
            {
                String strSpecialityCode=(String)searchContactForm.get("drSpecialityCode");
                String strDesignationCode=(String)searchContactForm.get("designationHOS");
                String strSpecialization=(String)request.getParameter("specialization");
                //Select designation or specialization according to Contact Type
                if (strSpecialization!=null && (strSpecialization.equals("Y")&& !strSpecialityCode.equals("")))
                {
                    alSearchParams.add(new SearchCriteria("SPEC_TYPE_ID",(String)searchContactForm.get("drSpecialityCode")));
                }//end of if (strSpecialization!=null && (strSpecialization.equals("Y")&& !strSpecialityCode.equals("")))
                else if(strSpecialization!=null && (strSpecialization.equals("N") && !strDesignationCode.equals("")))
                {
                    alSearchParams.add(new SearchCriteria("A.DESIGNATION_TYPE_ID",(String)searchContactForm.get("designationHOS")));
                }//end of else if(strSpecialization!=null && (strSpecialization.equals("N") && !strDesignationCode.equals("")))
            }// end of if(!strContactTypeId.equals(""))
        }//end of if(strSubLink=="Hospital")
        else if(strSubLink.equals("Insurance"))
        {
            alSearchParams.add(strSubLink);
            //build the column names along with their values to be searched
            alSearchParams.add(new SearchCriteria("INS_SEQ_ID",""+lInsuranceSeqID));
            alSearchParams.add(new SearchCriteria("A.DESIGNATION_TYPE_ID",(String)searchContactForm.get("designationINS"),"equals"));
        }//end of else if(strSubLink=="Insurance")
        //added for broker login kocb
        else if(strSubLink.equals("Broker"))
        {
            alSearchParams.add(strSubLink);
            //build the column names along with their values to be searched
            alSearchParams.add(new SearchCriteria("INS_SEQ_ID",""+lInsuranceSeqID));
            alSearchParams.add(new SearchCriteria("A.DESIGNATION_TYPE_ID",(String)searchContactForm.get("designationBRO"),"equals"));
        }//end of else if(strSubLink=="Insurance")
        
      // end added for broker login kocb
        
        else if(strSubLink.equals("Group Registration"))
        {
            alSearchParams.add(strSubLink);
			//modified  Group_REG_SEQ_ID to A.Group_REG_SEQ_ID as per 11G Migration
            alSearchParams.add(new SearchCriteria("A.Group_REG_SEQ_ID",""+lGroupRegSeqId));
            alSearchParams.add(new SearchCriteria("A.DESIGNATION_TYPE_ID",(String)searchContactForm.get("designationCOR"),"equals"));
        }// end of else if(strSubLink.equals("Group Registration"))

        else if(strSubLink.equals("Banks"))
        {
            alSearchParams.add(strSubLink);
            alSearchParams.add(new SearchCriteria("BANK_SEQ_ID",""+lBankSeqId));
            alSearchParams.add(new SearchCriteria("A.DESIGNATION_TYPE_ID",(String)searchContactForm.get("designationBAK"),"equals"));
        }// end of else if(strSubLink.equals("Group Registration"))
        alSearchParams.add(new SearchCriteria("CONTACT_NAME", TTKCommon.replaceSingleQots((String)searchContactForm.get("name"))));
        if(searchContactForm.get("userStatus").equals("ACT"))
        {
            alSearchParams.add(new SearchCriteria("ACTIVE_YN","Y","equals"));
        }//end of if(searchContactForm.get("userStatus").equals("ACT"))
        else
        {
            alSearchParams.add(new SearchCriteria("ACTIVE_YN","N","equals"));
        }//end of else
        if(strSubLink.equals("Banks"))
        {
        	alSearchParams.add(new SearchCriteria("PROVIDE_ACCESS_USER_YN",""));
        }//end of if(strSubLink.equals("Banks"))
        else
        {
        	alSearchParams.add(new SearchCriteria("PROVIDE_ACCESS_USER_YN", (String)searchContactForm.get("applUser1"),"equals"));
        }//end of else
        request.getSession().setAttribute("searchparam",alSearchParams);
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm searchContactForm,HttpServletRequest request, Long lHospitalSeqId, Long lInsuranceSeqID) throws TTKException

    /**
     * Returns the ContactManager session object for invoking methods on it.
     * @return ContactManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private ContactManager getContactManagerObject() throws TTKException
    {
        ContactManager contactManager = null;
        try
        {
            if(contactManager == null)
            {
                InitialContext ctx = new InitialContext();
                contactManager = (ContactManager) ctx.lookup("java:global/TTKServices/business.ejb3/ContactManagerBean!com.ttk.business.empanelment.ContactManager");
            }//end of if(contactManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "contact");
        }//end of catch
        return contactManager;
    }//end getContactManagerObject()






    public ActionForward doExportReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
    	
    	
    	
		
		  try {
			  log.info("Inside the doExportReport method of CollectionsSearchAction");
	    		setLinks(request);

	       	    DynaActionForm searchContactForm =(DynaActionForm)form;
	   		    JasperReport mainJasperReport,emptyReport;
	    		TTKReportDataSource mainTtkReportDataSource = null;
	    		JasperPrint mainJasperPrint = null;
	    		ContactManager contactObject=this.getContactManagerObject();
	    		HashMap<String,String> hashMap = new HashMap<String,String>();
	    		  Long lngHospitalSeqId=null;
	    		 lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
	    		 String reportType = request.getParameter("reportType");
	    		 String strContactPath=this.getForwardPath(request);
	    		 ArrayList<Object> alSearchParams = new ArrayList<Object>();		
	    	        String strSwitchType=TTKCommon.checkNull((String)searchContactForm.get("switchType"));
	    	         alSearchParams.add(lngHospitalSeqId.toString());
	    	         alSearchParams.add((String)searchContactForm.getString("switchType"));
	    	        if(strSwitchType.equals(strGeneral))
	    	        {
	    	        	 alSearchParams.add((String)searchContactForm.getString("contactName"));
	    	        	 alSearchParams.add((String)searchContactForm.getString("designation"));
	    	        	 alSearchParams.add((String)searchContactForm.getString("userRole"));
	    	        	 alSearchParams.add((String)searchContactForm.getString("department"));
	    	        	 alSearchParams.add((String)searchContactForm.getString("userId"));
	    	        }
	    	        else if(strSwitchType.equals(strProfessional))
	    	        {
	    	            
	    	        	 alSearchParams.add((String)searchContactForm.getString("clinicianName"));
	    	        	 alSearchParams.add((String)searchContactForm.getString("speciality"));
	    	        	 alSearchParams.add((String)searchContactForm.getString("consultationType"));
	    	        	 alSearchParams.add((String)searchContactForm.getString("ClinicianId"));
	    	        }	
	    	        alSearchParams.add((String)searchContactForm.getString("emailId"));
	    	        alSearchParams.add((String)searchContactForm.getString("status"));
	    		
		    		 
	    		
	    		
		    		
	    	        String fileName=null;
	    	        if(strSwitchType.equals(strGeneral))
	    	        {
	    	        	 fileName = "reports/empanelment/HospitalGeneralContactsList.jrxml";
	    	        }
	    	        else{
	    	        	 fileName = "reports/empanelment/HospitalClinicianContactsList.jrxml";
	    	        }
		    		 
		    		 String jrxmlfile=null;
			    		jrxmlfile=fileName;
		    		 
			    		emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		    		 
			    		ByteArrayOutputStream boas=new ByteArrayOutputStream();
		    		 
			    		mainTtkReportDataSource = contactObject.getContactListExport(alSearchParams);
			    		ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
			    		
			    		
			    		mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
			    		
			    		if (main_report_RS != null && main_report_RS.next()) {
		    				main_report_RS.beforeFirst();
		    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);
		    				  				
		    			} else {
		    				
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    			}
			    		
			    		if("EXCEL".equals(reportType)){
			    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		 				jExcelApiExporter.exportReport();	
			    		}
			    		
		 				request.setAttribute("boas",boas);
		 				request.setAttribute("reportType", reportType);
		 				response.setHeader("Content-Disposition", "attachement; filename = HospitalContactsList.xls");
		 				//frmCollections.set("letterPath", reportLink);
		 				 return (mapping.findForward(strReportdisplay));
		 				//return this.getForward(strCollectionSearch, mapping, request);
		} 
		  
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}
	  
    }

    
    
    
    
    
    public ActionForward doCloseClinician(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.debug("Inside the doCloseClinician method of HospitalContactAction");
            DynaActionForm frmSearchContact=(DynaActionForm)form;  //get the DynaActionForm instance
            String strSwitchType=TTKCommon.checkNull((String)frmSearchContact.get("switchType"));
            setLinks(request);
            String strSubLink=TTKCommon.getActiveSubLink(request);
            String strContactPath=this.getForwardPath(request);
            frmSearchContact.set("switchType",strSwitchType);
Long lngInsuranceSeqID = null;
Long lngHospitalSeqId=null;
Long lngBrokerSeqID=null;
ArrayList alContact=null;
Long lngGroupRegSeqId=null;
Long lngBankSeqId=null;
TableData  tableData =TTKCommon.getTableData(request);
ContactManager contactObject=this.getContactManagerObject();
lngHospitalSeqId= new Long(TTKCommon.getWebBoardId(request));//get the web board id
      
              tableData.createTableInfo("ProffessionalDetailTable",null);
              frmSearchContact.set("caption","Clinician Contacts");
              
              tableData.setSearchData(this.populateSearchCriteriaHospital((DynaActionForm)form,request,
                		lngHospitalSeqId,lngInsuranceSeqID,lngGroupRegSeqId,lngBankSeqId));
                
                alContact= contactObject.getHospitalContactList(tableData.getSearchData()); 
    
                tableData.setData(alContact, "search");
                request.getSession().setAttribute("tableData",tableData);
         
            return this.getForward(strContactPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,strContactError));
        }//end of catch(Exception exp)
    }//end of doSwitchTo(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

}// end of HospitalContactAction class
