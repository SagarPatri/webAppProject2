
/**
 * as per Hospital Login
 * @ (#) OnlineCashlessHospAction Mar 24, 2014
 *  Author       :Kishor kumar S h
 * Company      : Rcs Technologies
 * Date Created : Oct 29 ,2014
 *
 * @author       :Kishor kumar S h
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.hospital;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.dom4j.Document;

import sun.misc.BASE64Decoder;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.common.HospPreAuthWebBoardHelper;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.CashlessVO;
//import com.ttk.dto.onlineforms.OnlineInsPolicyVO;
//import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;
//kocnewhosp1
//kocnewhosp1

/**
 * This class is used for Searching the List Policies to see the Online Account Info.
 * This also provides deletion and updation of products.
 */
public class OnlineCashlessHospAction extends TTKAction {

    private static final Logger log = Logger.getLogger( OnlineCashlessHospAction.class );

    //Modes.
    private static final String strBackward="Backward";
    private static final String strForward="Forward";

    // Action mapping forwards.
    private static final String strOnlineClaimList="cashlessAdd";
    private static final String strOnlineOptList="onlinoptlist";
    private static final String strShowTests="showtests";
    private static final String strOnlineEnterList="diagenteramounts";
    //Exception Message Identifier
    private static final String strHospSerachInfo="onlinehospitalinfo";
    private static final String strHosClaimDetail="hospclaimdetail";
    private static final String strHosNotify="hospnotify";
    private static final String strOTPVAlidate="otpvalidate";
    private static final String strPreAuthOpt="Cashless Intimation";
    private static final String strMemberVitals="memberVitals";
    private static final String strAddLaboratories="addLaboratories";
    private static final String pageUnderDevelop="pageUnderDevelop";
    
    
	 private static final String strReportExp="onlinereport";
	 private static final String strReportdisplay="reportdisplay";
	 
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
            log.info("Inside the doDefault method of OnlineCashlessHospAction");
            setOnlineLinks(request);
            request.getSession().removeAttribute("memberAuthenticated");
            DynaActionForm frmCashlessAdd =(DynaActionForm)form;
            frmCashlessAdd.initialize(mapping);     //reset the form data
			request.getSession().setAttribute("frmCashlessAdd", frmCashlessAdd);
			request.getSession().removeAttribute("benifitTypeID");
            return this.getForward(strOnlineClaimList, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processOnlineExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
        }//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            //HttpServletResponse response)

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
            setOnlineLinks(request);
            log.debug("Inside the doSearch method of OnlineClmSearchHospAction");
			OnlineAccessManager onlineAccessManager = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			ArrayList alOnlineAccList = null;
			onlineAccessManager = this.getOnlineAccessManagerObject();
			tableData=(TableData)request.getSession().getAttribute("tableData");
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
					return (mapping.findForward(strOnlineClaimList));
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
				tableData.createTableInfo("HospClaimsTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));						
				tableData.modifySearchData("search");
				//sorting based on investSeqID in descending order													
			}//end of else
			alOnlineAccList= onlineAccessManager.getHospClaimsList(tableData.getSearchData());
			tableData.setData(alOnlineAccList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			//finally return to the grid screen
			return this.getForward(strOnlineClaimList, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processOnlineExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
        }//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                            //HttpServletResponse response)
    
    
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
    public ActionForward doDefaultOptDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception{
        try{
            log.info("Inside the doDefaultOptDetails method of OnlineCashlessHospAction");
            setOnlineLinks(request);
            TableData tableData =null;

            DynaActionForm frmCashlessAdd =(DynaActionForm)form;
            frmCashlessAdd.initialize(mapping);     //reset the form data
            //get the tbale data from session if exists
           /* tableData =TTKCommon.getTableData(request);
            //create new table data object
            tableData = new TableData();
            //create the required grid table
            tableData.createTableInfo("HospCashlessTable",new ArrayList());
            request.getSession().setAttribute("tableData",tableData);*/
            //((DynaActionForm)form).initialize(mapping);//reset the form data
            return this.getForward(strOnlineOptList, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processOnlineExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
        }//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            //HttpServletResponse response)
    
    
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
	public ActionForward doSearchOpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside the doSearchOpt method of OnlineCashlessHospAction");
			OnlineAccessManager onlineAccessManager = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			ArrayList alOnlineAccList = null;
			onlineAccessManager = this.getOnlineAccessManagerObject();
			tableData=(TableData)request.getSession().getAttribute("tableData");
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
					return (mapping.findForward(strOnlineOptList));
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
				tableData.createTableInfo("HospCashlessTable",null);
				tableData.setSearchData(this.populateSearchCriteriaOpt((DynaActionForm)form,request));	
				tableData.modifySearchData("search");
				//sorting based on investSeqID in descending order													
			}//end of else
			alOnlineAccList= onlineAccessManager.getHospCashlessOptList(tableData.getSearchData());
			tableData.setData(alOnlineAccList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			
			//finally return to the grid screen
			return this.getForward(strOnlineOptList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
		}//end of catch(Exception exp)
	}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)


    
    
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
            log.debug("Inside the doBackward method of OnlineClmSearchHospAction");
            setOnlineLinks(request);
            //get the session bean from the bean pool for each excecuting thread
            OnlineAccessManager onlineAccessManager=null;
            TableData tableData =null;
            ArrayList alPolicyList =null;
            onlineAccessManager =this.getOnlineAccessManagerObject();
            tableData = TTKCommon.getTableData(request);
            tableData.modifySearchData(strBackward);
            alPolicyList = onlineAccessManager.getHospClaimsList(tableData.getSearchData());
            tableData.setData(alPolicyList, strBackward);
            request.getSession().setAttribute("tableData",tableData);
            return this.getForward(strOnlineClaimList, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processOnlineExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
        }//end of catch(Exception exp)
    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest
            //request,HttpServletResponse response)

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
            log.debug("Inside the doForward method of OnlineClmSearchHospAction");
            setOnlineLinks(request);
            //get the session bean from the bean pool for each excecuting thread
            OnlineAccessManager onlineAccessManager=null;
            TableData tableData =null;
            ArrayList alPolicyList =null;
            onlineAccessManager =this.getOnlineAccessManagerObject();
            tableData = TTKCommon.getTableData(request);
            tableData.modifySearchData(strForward);
            alPolicyList = onlineAccessManager.getHospClaimsList(tableData.getSearchData());
            tableData.setData(alPolicyList, strForward);
            request.getSession().setAttribute("tableData",tableData);
            return this.getForward(strOnlineClaimList, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processOnlineExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
        }//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            //HttpServletResponse response)
    
   
    private OnlineAccessManager getOnlineAccessManagerObject() throws TTKException
    {
    	OnlineAccessManager onlineAccessManager = null;
        try
        {
            if(onlineAccessManager == null)
            {
                InitialContext ctx = new InitialContext();
                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
                onlineAccessManager = (OnlineAccessManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineAccessManagerBean!com.ttk.business.onlineforms.OnlineAccessManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, strHospSerachInfo);
        }//end of catch
        return onlineAccessManager;
    }//end of getOnlineAccessManagerObject()

    
    
    
    
    private ArrayList<Object> populateDashBoardSearch(HttpServletRequest request) throws TTKException
	{
		// build the column names along with their values to be searched
		ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
		UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
		//prepare the search BOX parameters
		Long lngHospSeqId =userSecurityProfile.getHospSeqId();
		alSearchBoxParams.add(null);
		alSearchBoxParams.add(null);
		alSearchBoxParams.add(null);
		alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)request.getParameter("sStatus")));
		alSearchBoxParams.add(null);
		alSearchBoxParams.add(null);
		alSearchBoxParams.add(null);
		alSearchBoxParams.add(lngHospSeqId);
		return alSearchBoxParams;
	}//end of populateSearchCriteria(DynaActionForm frmPatHospSearch,HttpServletRequest request)  
	
    
    
        /**
	     * this method will add search criteria fields and values to the arraylist and will return it
	     * @param frmClmHospSearch formbean which contains the search fields
	     * @return ArrayList contains search parameters
	     */
	    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmClmHospSearch,HttpServletRequest request) throws TTKException
	    {
	//      build the column names along with their values to be searched
	        ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
	        UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	        //prepare the search BOX parameters
	      //  Long lngHospSeqId =userSecurityProfile.getHospSeqId();
    	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sClaimNumber")));
    	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sAuthNumber")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sEnrollmentNumber")));
	    	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sStatus")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sPolicyNumber")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sDOA")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sDateOfClm")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sClmStartDate")));
			alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sClmEndDate")));   //sPatStartDate,sPatEndDate,sClmStartDate,sClmEndDate
	        alSearchBoxParams.add(userSecurityProfile.getHospSeqId());
	        //new Req
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sPatientName")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sDischargeDate")));
	    	return alSearchBoxParams;
	    }//end of populateSearchCriteria(DynaActionForm frmClmHospSearch,HttpServletRequest request) 
	    
	    
	    /**
	     * this method will add search criteria fields and values to the arraylist and will return it
	     * @param frmCashlessAdd formbean which contains the search fields
	     * @return ArrayList contains search parameters
	     */
	    private ArrayList<Object> populateSearchCriteriaOpt(DynaActionForm frmCashlessAdd,HttpServletRequest request) throws TTKException
	    {
	//      build the column names along with their values to be searched
	        ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
	        UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	        //prepare the search BOX parameters
	      //  Long lngHospSeqId =userSecurityProfile.getHospSeqId();
    	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sCashlessNumber")));
    	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sAuthNumber")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sMemberName")));
	    	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sEnrollmentNumber")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sOtpReq")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sBillsPending")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sStartDate")));
	        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmCashlessAdd.getString("sEndDate")));
	        alSearchBoxParams.add(userSecurityProfile.getHospSeqId());
	        
	    	return alSearchBoxParams;
	    }//end of populateSearchCriteria(DynaActionForm frmClmHospSearch,HttpServletRequest request) 
	    
	    
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
	    		log.debug("Inside the doCopyToWebBoard method of ClaimsAction");
	    		setLinks(request);
	    		this.populateWebBoard(request);
				return this.getForward(strOnlineClaimList, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
			}//end of catch(Exception exp)
	    }//end of doCopyToWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
	    		log.debug("Inside the doView method of ClaimsAction");
	    		setOnlineLinks(request);
	    		//get the tbale data from session if exists
				TableData tableData =TTKCommon.getTableData(request);
	    		DynaActionForm frmCashlessAdd=(DynaActionForm)form;
	    		String rownum=request.getParameter("rownum");
				if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
				{
					CashlessVO preAuthVO=(CashlessVO)tableData.getRowInfo(Integer.parseInt((String)
																			(request.getParameter("rownum"))));
					String diagid=""+preAuthVO.getDiagGenSeqId();
					request.setAttribute("diagSeqIdForSelfFund",diagid);
					//frmCashlessAdd.set("diagSeqIdForSelfFund",diagid);
					this.addToWebBoard(preAuthVO, request);
				}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
				return mapping.findForward(strHosClaimDetail);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
			}//end of catch(Exception exp)
	    }//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	    /**
	     * This method is used to navigate to detail screen to view selected record.
	     * Finally it forwards to the appropriate view based on the specified forward mappings
	     *
	     * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	     */
	    public ActionForward doViewOpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception {
						try
						{
							setLinks(request);
							log.info("Inside HistoryAction doView");
							Toolbar toolBar = (Toolbar)request.getSession().getAttribute("toolbar");
							OnlineAccessManager onlineAccessManager=null;
							ArrayList alOnlineAccList = null;
							DynaActionForm frmCashlessAdd =(DynaActionForm)form;
				            frmCashlessAdd.initialize(mapping); 
				            onlineAccessManager =this.getOnlineAccessManagerObject();
							UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
							//check if user trying to hit the tab directly with out selecting the hospital
							String strLink = TTKCommon.getActiveSubLink(request);
							
							//DynaActionForm frmHistoryDetail=(DynaActionForm)request.getAttribute("frmHistoryDetail");
							Document historyDoc=null;
							String strFwdHistory = null;
							String strEnrollmentId = "";
							String strWebBoardDesc = "";
							String strEnrollmentID = "";
							StringBuffer strCaption=new StringBuffer();
							String strForward="";
							//DynaActionForm frmHosHistoryDetail=(DynaActionForm)form;
							
							TableData tableData=TTKCommon.getTableData(request);
							
							if(strLink.equals(strPreAuthOpt))//if it is from PreAuth flow
							{
							if(HospPreAuthWebBoardHelper.checkWebBoardId(request)==null)
							{
								TTKException expTTK = new TTKException();
								expTTK.setMessage("error.PreAuthorization.required");
								throw expTTK;
							}//end of if(PreAuthWebBoardHelper.checkWebBoardId(request)==null)
							}
							
								strWebBoardDesc = HospPreAuthWebBoardHelper.getWebBoardDesc(request);
								String enrollmentId=HospPreAuthWebBoardHelper.getEnrollmentId(request);
								
								String seqid=(String) request.getAttribute("diagSeqIdForSelfFund");
								ArrayList alDiagnosysEnrolResult	= onlineAccessManager.getValidateSearchOpt(new Long(seqid));
								ArrayList alDiagnosysTotalResult= onlineAccessManager.getDiagOptTestTotalAmnts(new Long(seqid));
								
								CashlessDetailVO c2	=	new CashlessDetailVO();
								CashlessVO c1	=	new CashlessVO();
								
								
								if(alDiagnosysTotalResult!=null && alDiagnosysTotalResult.size()>0)
								{
									c1=(CashlessVO)alDiagnosysTotalResult.get(0);
									String enrollId=c1.getEnrollmentID();
									String status=c1.getStatus();
									request.getSession().setAttribute("enrollId", enrollId);
									frmCashlessAdd.set("enrollId", enrollId);
								
									if(status.equalsIgnoreCase("N"))
									{
										strForward	=	"otpvalidate";
									}
									if(status.equalsIgnoreCase("Y"))
									{
										strForward	=	"showtests";
									}
								
								}
								if(alDiagnosysEnrolResult!=null && alDiagnosysEnrolResult.size()>0)
								{	
									request.getSession().setAttribute("alDiagnosysEnrolResult",alDiagnosysEnrolResult);	
									request.getSession().setAttribute("alDiagnosysTotalResult",alDiagnosysTotalResult);	
									strForward	=	"cashlessAddTotal";
								}	
							return this.getForward(strForward,mapping,request);
						}//end of try
						catch(TTKException expTTK)
						{
						return this.processExceptions(request, mapping, expTTK);
						}//end of catch(TTKException expTTK)
						catch(Exception exp)
						{
						return this.processExceptions(request, mapping, new TTKException(exp, strHospSerachInfo));
						}//end of catch(Exception exp)
	    }//end of doViewOpt(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    
	    
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
	    	CashlessVO preAuthVO=null;

	    	if(strChk!=null&&strChk.length!=0)
	    	{
	    		for(int i=0; i<strChk.length;i++)
	    		{
	    			cacheObject = new CacheObject();
	    			preAuthVO=(CashlessVO)tableData.getData().get(Integer.parseInt(strChk[i]));
	    			cacheObject.setCacheId(this.prepareWebBoardId(preAuthVO));
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
	    private void addToWebBoard(CashlessVO preAuthVO, HttpServletRequest request)throws TTKException
	    {
	    	Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
	    	ArrayList<Object> alCacheObject = new ArrayList<Object>();
	    	CacheObject cacheObject = new CacheObject();
	    	cacheObject.setCacheId(this.prepareWebBoardId(preAuthVO)); //set the cacheID
	    	cacheObject.setCacheDesc(preAuthVO.getPreAuthNo());
	    	alCacheObject.add(cacheObject);
	    	//if the object(s) are added to the web board, set the current web board id
	    	toolbar.getWebBoard().addToWebBoardList(alCacheObject);
	    	toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());

	    	//webboardinvoked attribute will be set as true in request scope
	    	//to avoid the replacement of web board id with old value if it is called twice in same request scope
	    	request.setAttribute("webboardinvoked", "true");
	    }//end of addToWebBoard(HospPreAuthVO preAuthVO, HttpServletRequest request,String strIdentifier)throws TTKException

	    /**
	     * This method prepares the Weboard id for the selected Policy
	     * @param HospPreAuthVO  preAuthVO for which webboard id to be prepared
	     * * @param String  strIdentifier whether it is preauth or enhanced preauth
	     * @return Web board id for the passedVO
	     */
	    private String prepareWebBoardId(CashlessVO preAuthVO)throws TTKException
	    {
	    	StringBuffer sbfCacheId=new StringBuffer();
	    	sbfCacheId.append(preAuthVO.getClaimSeqID()!=null? String.valueOf(preAuthVO.getClaimSeqID()):" ");
	    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getEnrollmentID()).equals("")?" ":preAuthVO.getEnrollmentID());
	    	sbfCacheId.append("~#~").append(preAuthVO.getEnrollDtlSeqID()!=null?String.valueOf(preAuthVO.getEnrollDtlSeqID()):" ");
	    	sbfCacheId.append("~#~").append(preAuthVO.getPolicySeqID()!=null? String.valueOf(preAuthVO.getPolicySeqID()):" ");
	    	sbfCacheId.append("~#~").append(preAuthVO.getMemberSeqID()!=null? String.valueOf(preAuthVO.getMemberSeqID()):" ");
	    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getClaimantName()).equals("")? " ":preAuthVO.getClaimantName());
	    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getBufferAllowedYN()).equals("")? " ":preAuthVO.getBufferAllowedYN());
	    	sbfCacheId.append("~#~").append(preAuthVO.getClmEnrollDtlSeqID()!=null? String.valueOf(preAuthVO.getClmEnrollDtlSeqID()):" ");
	    	sbfCacheId.append("~#~").append(preAuthVO.getAmmendmentYN()!=null? String.valueOf(preAuthVO.getAmmendmentYN()):" ");
	    	sbfCacheId.append("~#~").append(TTKCommon.checkNull(preAuthVO.getCoding_review_yn()).equals("")? " ":preAuthVO.getCoding_review_yn());
	    	return sbfCacheId.toString();
	    }//end of prepareWebBoardId(PreAuthVO preAuthVO,String strIdentifier)throws TTKException



	    
	    
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
	    public ActionForward doDashBoardDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                                                     HttpServletResponse response) throws Exception{
	        try{
	            log.info("Inside the doDefault method of OnlineClmSearchHospAction");
	            setOnlineLinks(request);
	            TableData tableData =null;

	            DynaActionForm frmClmHospSearch =(DynaActionForm)form;
	            
	            return this.getForward("claimdashboard", mapping, request);//claimdashboard
	        }//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strHospSerachInfo));
	        }//end of catch(Exception exp)
	    }//end of doDashBoardDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            //HttpServletResponse response)

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
	    public ActionForward doValidate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                                                    HttpServletResponse response) throws Exception{
	        try{
	            setOnlineLinks(request);
	            log.debug("Inside the doValidate method of OnlineCashlessHospAction");
	            DynaActionForm frmCashlessAdd =(DynaActionForm) form;//(request.getSession().getAttribute("frmCashlessAdd")==null?(DynaActionForm)form:request.getSession().getAttribute("frmCashlessAdd"));
	            
	            //frmCashlessAdd.initialize(mapping);     //reset the form data
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
				OnlineAccessManager onlineAccessManager = null;
				CashlessDetailVO cashlessDetailVO	= null;
				ArrayList alOnlineAccList = null;
				String eligibility=null;
				String treatmentDate=frmCashlessAdd.getString("treatmentDate");
				onlineAccessManager = this.getOnlineAccessManagerObject();
				String modeType	=	 (String) (frmCashlessAdd.get("modeType")==""?request.getSession().getAttribute("modeType"):frmCashlessAdd.get("modeType"));
				String enrollId	=	 (String) (frmCashlessAdd.get("enrollId"));//==null?request.getSession().getAttribute("enrollId"):frmCashlessAdd.get("enrollId"));
				//String benifitType	=	(String) (request.getSession().getAttribute("benifitTypeID")==null?frmCashlessAdd.get("benifitType"):request.getSession().getAttribute("benifitTypeID"));
				
				String benefitText		=	(String) (request.getParameter("benifit")==null?request.getSession().getAttribute("benefitText"):request.getParameter("benifit"));
				String benifitTypeVal	=	(String) (request.getParameter("benifitTypeID")==null?request.getSession().getAttribute("benifitTypeVal"):request.getParameter("benifitTypeID"));
				
				String flag	=	"";
				alOnlineAccList	= onlineAccessManager.getValidateEnrollId(enrollId,userSecurityProfile.getHospSeqId(),userSecurityProfile.getUSER_SEQ_ID(),frmCashlessAdd.getString("treatmentDate"));
				flag	=	(String)alOnlineAccList.get(1);
				CashlessVO	cv	=	new CashlessVO();
				cv=(CashlessVO)alOnlineAccList.get(0);
				Long memSeqIDForSelfFund	=	null;
				Long diagSeqIdForSelfFund	=	null;
				memSeqIDForSelfFund	=	cv.getMemberSeqID();
				diagSeqIdForSelfFund=	cv.getDiagGenSeqId();
				
				//this code is to get the member and other details based on the enroll id
				cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollId(enrollId,benifitTypeVal,userSecurityProfile.getHospSeqId());
				//cashlessDetailVO.setEnrollId(enrollId);
				cashlessDetailVO.setBenifitType(benefitText);
				cashlessDetailVO.setBenifitTypeCode(benifitTypeVal);
				
				//TOB S T A R T S
				String[] tobBenefits	=	onlineAccessManager.getTobForBenefits(benifitTypeVal,enrollId);
				cashlessDetailVO.setCoPay(tobBenefits[0]);
				cashlessDetailVO.setEligibility1(tobBenefits[1]);
				cashlessDetailVO.setDeductible(tobBenefits[2]);
				//request.setAttribute("tobBenefits","tobBenefits");
				//TOB E N D S
				
				
				frmCashlessAdd = (DynaActionForm)FormUtils.setFormValues("frmCashlessAdd",cashlessDetailVO,this,mapping,request);
				//NETWORKS VALIDATION
				String netArry[]=	new String[2];
				netArry	=	cashlessDetailVO.getNetworksArray();
				String netArrys[]	=	new String[cashlessDetailVO.getAssNetworksArray().size()];
				Map<String,String> map	=	cashlessDetailVO.getAssNetworksArray();
				
				Set keySet	=	map.keySet();
				Set valSet	=	map.keySet();
				Iterator it	=	 keySet.iterator();
				Iterator it1			=	 valSet.iterator();
				int	temp				=	0;
				int	compTemp			=	0;
				String compToPolicyNet	=	"";
				String compToPolicyNetVal=	"";
				String policyNet		=	"";
				String provNet			=	"";
				if(netArry!=null){
					policyNet	=	netArry[1];
					provNet		=	netArry[0];
				}
				ArrayList<String> networkOrder	=	new ArrayList<String>();
				
				while(it.hasNext() && it1.hasNext()){
					compToPolicyNet	=	(String)(it.next());
					if(compToPolicyNet.equals(policyNet))
						compTemp	=	temp;
					compToPolicyNetVal	=	map.get(it1.next());
					
					//  

					networkOrder.add(compToPolicyNet);//adding Network names to arrayList of all networks
					netArrys[temp++]=	compToPolicyNetVal;//adding Y/N to array of all networks
					
				}
				if(temp>1)
					temp	=	temp-1;
				
				int tempPol		=	0;
				int tempProv	=	0;
				for(int i=0;i<=networkOrder.size()-1;i++)
				{
					if(policyNet.equals(networkOrder.get(i))){
						tempPol	=	i;
					}
					if(provNet.equals(networkOrder.get(i))){
						tempProv	=	i;
					}
				}
				
				System.out.println("cashlessDetailVO.getAssociateYN()===>"+cashlessDetailVO.getAssociateYN());
				System.out.println("cashlessDetailVO.getEligibility()===>"+cashlessDetailVO.getEligibility());
				
				
			/*	if(netArrys[compTemp].trim().equals("Y") && "YES".equals(cashlessDetailVO.getEligibility().trim())){//IF SELECTED OTHERS AT EMPANELMENT LEVEL
					request.setAttribute("eligibility", "YES");
					frmCashlessAdd.set("eligibility", "YES");
					eligibility="YES";
				}else*/ if(tempProv>=tempPol && "YES".equals(cashlessDetailVO.getEligibility())){//IF POLICY NETWORK IS GREATER THAN PROVIDER NETWORK
					request.setAttribute("eligibility", "YES");
					frmCashlessAdd.set("eligibility", "YES");
					eligibility="YES";
				}else if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
						&& tempProv==tempPol)// MATERNITY CASE
					{
					request.setAttribute("eligibility", "YES");
					frmCashlessAdd.set("eligibility", "YES");
					eligibility="YES";
					}
				else if("YES".equals(cashlessDetailVO.getAssociateYN()) && "YES".equals(cashlessDetailVO.getEligibility())){
						request.setAttribute("eligibility", "YES");
						frmCashlessAdd.set("eligibility", "YES");
						eligibility="YES";
					}
				/*	else if("NO".equals(cashlessDetailVO.getAssociateYN()) && "YES".equals(cashlessDetailVO.getEligibility())){
						request.setAttribute("eligibility", "YES");
						frmCashlessAdd.set("eligibility", "YES");
						eligibility="YES";
					}*/
					
					else {//REJECTION
					request.setAttribute("eligibility", "NO");
					eligibility="NO";
					frmCashlessAdd.set("eligibility", "NO");
					
					if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
							&& tempProv==tempPol){
						frmCashlessAdd.set("reasonForRejection",request.getParameter("benifit")+" benefit is Not Covered for the Policy.");
					}else if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
							&& tempProv<tempPol){
						frmCashlessAdd.set("reasonForRejection","For "+request.getParameter("benifit")+" network is Not Eligible for the Policy.");
					}else if(netArrys[compTemp].trim().equals("N") && "YES".equals(cashlessDetailVO.getEligibility().trim())
							&& tempProv<tempPol){
						frmCashlessAdd.set("reasonForRejection","For "+request.getParameter("benifit")+" network is Not Eligible for the Policy.");
					}else if(netArrys[compTemp].trim().equals("N") && "NO".equals(cashlessDetailVO.getEligibility().trim())
							&& tempProv==tempPol)// MATERNITY CASE
					{
						frmCashlessAdd.set("reasonForRejection","For "+ request.getParameter("benifit")+" member is not Eligible for this Benefit");
					}
					else if(tempProv<tempPol){
						frmCashlessAdd.set("reasonForRejection","For "+request.getParameter("benifit")+" benefit is Not Covered for the Policy.");
					}
					}//NETWORKS VALIDATION ENDS
				
				/* Capturing log start*/
				ArrayList<String> dataList=new ArrayList<>();
				dataList.add("PROVIDER");
				dataList.add(enrollId);
				dataList.add(benifitTypeVal);
				dataList.add(treatmentDate);
				dataList.add(eligibility);
				dataList.add(userSecurityProfile.getHospSeqId()+"");
				dataList.add(null);
				dataList.add(null);
				dataList.add(null);
				dataList.add(null);
				dataList.add(null);
				dataList.add(userSecurityProfile.getUSER_SEQ_ID()+"");
				dataList.add(userSecurityProfile.getHospitalName()+"");
				dataList.add(userSecurityProfile.getEmpanelNumber()+"");
				dataList.add(null);
				dataList.add(frmCashlessAdd.getString("reasonForRejection"));
				onlineAccessManager.updateLogTable(dataList);
				/* Capturing log end*/
				
				request.getSession().setAttribute("memSeqIDForSelfFund", memSeqIDForSelfFund);
				request.getSession().setAttribute("diagSeqIdForSelfFund", diagSeqIdForSelfFund);
				request.getSession().setAttribute("enrollId", cashlessDetailVO.getEnrollId());
				request.getSession().setAttribute("benifitTypeVal", benifitTypeVal);
				request.getSession().setAttribute("benefitText", benefitText);
				/*
				request.getSession().setAttribute("benifitType", cashlessDetailVO.getBenifitType());
				*/
				request.getSession().setAttribute("modeType", modeType);
				request.getSession().setAttribute("provMemName", cashlessDetailVO.getMemberName());
	    		request.getSession().setAttribute("fromFlag", request.getParameter("fromFlag"));
				request.getSession().setAttribute("tobBenefitsForMemElig", tobBenefits);
				/*frmCashlessAdd.set("flag", flag);*/
				if("true".equals((String)request.getParameter("memberAuthenticated")))
				request.getSession().setAttribute("memberAuthenticated", "true");
				request.setAttribute("flag",flag);
				request.setAttribute("logicVar","true");
				request.getSession().setAttribute("loginTypeProvider", "providerLogin");
				
				//frmCashlessAdd.set("benifitType", benifitTypeVal);
				frmCashlessAdd.set("enrollId", cashlessDetailVO.getEnrollId()); 
				frmCashlessAdd.set("treatmentDate", frmCashlessAdd.get("treatmentDate"));
				
				frmCashlessAdd.set("bufferWarning",cashlessDetailVO.getBufferFlag());
				
				//frmCashlessAdd.set("vip",cashlessDetailVO.getVip());
				
				request.getSession().setAttribute("frmCashlessAdd",frmCashlessAdd);
				
				//E N D S 
				String vip=null;
				if(cashlessDetailVO.getVip().compareTo("Y")==0) vip="YES";
				else vip="NO";
				
				request.getSession().setAttribute("vip", vip);
				
				if(!"ONL1".equals(modeType))
				{
					request.setAttribute("OTP_Done","Done");
				}
				request.getSession().removeAttribute("lpreAuthIntSeqId");
				if(cashlessDetailVO.getOptspatlimit()!=null){
					request.getSession().setAttribute("optspreauthlimits", cashlessDetailVO.getOptspatlimit());	
				}else{
					request.getSession().setAttribute("optspreauthlimits","");	
				}
				
				if(cashlessDetailVO.getOptsAvailableLimit()!=null){
					request.getSession().setAttribute("optspreauthavailablelimits", cashlessDetailVO.getOptsAvailableLimit());	
				}else{
					request.getSession().setAttribute("optspreauthavailablelimits","");	
				}
				
				return this.getForward(strOTPVAlidate, mapping, request);
	        }//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	        }//end of catch(Exception exp)
	    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                            //HttpServletResponse response)
	    
	    
	    /*
	     * Check Eligibility
	     */
	    
	    //doCheckEligibility
	    public ActionForward dogetEmployeeDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
	    	try{
	    		//  
	    		setOnlineLinks(request);
	    		log.debug("Inside the doValidate method of OnlineCashlessHospAction");
	    		DynaActionForm frmCashlessAdd =(DynaActionForm) (request.getSession().getAttribute("frmCashlessAdd")==null?(DynaActionForm)form:request.getSession().getAttribute("frmCashlessAdd"));
				frmCashlessAdd.set("showEmpDetail","N");
	    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
	    		OnlineAccessManager onlineAccessManager = null;
	    		CashlessDetailVO cashlessDetailVO	= null;
	    		ArrayList alOnlineAccList = null;
	    		onlineAccessManager = this.getOnlineAccessManagerObject();
	    		String enrollId	=	 (String) (frmCashlessAdd.get("enrollId"));//==null?request.getSession().getAttribute("enrollId"):frmCashlessAdd.get("enrollId"));
	    		alOnlineAccList	= onlineAccessManager.getValidateEnrollId(enrollId,userSecurityProfile.getHospSeqId(),userSecurityProfile.getUSER_SEQ_ID(),frmCashlessAdd.getString("treatmentDate"));
	    		if(alOnlineAccList.size()>0){
					cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollId(enrollId,"",userSecurityProfile.getHospSeqId());
					frmCashlessAdd = (DynaActionForm)FormUtils.setFormValues("frmCashlessAdd",
							cashlessDetailVO,this,mapping,request);
					frmCashlessAdd.set("benifitType","");
					frmCashlessAdd.set("showEmpDetail","Y");
					frmCashlessAdd.set("enrollId",enrollId);
	    		}else{
	    			frmCashlessAdd.set("showEmpDetail","N");
	    			frmCashlessAdd.set("enrollId",enrollId);
	    		}
				request.getSession().setAttribute("frmCashlessAdd",frmCashlessAdd);
				return this.getForward(strOnlineClaimList, mapping, request);
	    	}//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	        }//end of catch(Exception exp)
	    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                            //HttpServletResponse response)
				
	    /**
	     * 
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
	    public ActionForward doValidateOTP(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                                                    HttpServletResponse response) throws Exception{
	        try{
	            setOnlineLinks(request);
	            log.debug("Inside the doValidateOTP method of OnlineCashlessHospAction");
	            DynaActionForm frmCashlessAdd =(DynaActionForm)form;
	            //frmCashlessAdd.initialize(mapping);     //reset the form data
				
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
				OnlineAccessManager onlineAccessManager = null;
				
				ArrayList alOnlineAccList = null;
				onlineAccessManager = this.getOnlineAccessManagerObject();
				
				Long memSeqIDForSelfFund	=	(Long)request.getSession().getAttribute("memSeqIDForSelfFund");
				Long diagSeqIDForSelfFund	=	(Long)request.getSession().getAttribute("diagSeqIdForSelfFund");
				String flag	=	"";
				
				//this code is to get the member and other details based on the enroll id
				CashlessDetailVO cashlessDetailVO	=	null;
				cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollId((String)request.getSession().getAttribute("enrollId"),(String)request.getSession().getAttribute("benifitTypeVal"),new Long(0));
				frmCashlessAdd = (DynaActionForm)FormUtils.setFormValues("frmCashlessAdd",
						cashlessDetailVO,this,mapping,request);
				
				alOnlineAccList	= onlineAccessManager.getValidateOTP(diagSeqIDForSelfFund,request.getParameter("otp"),userSecurityProfile.getUSER_SEQ_ID());
				String outdatedYN	=	(String)alOnlineAccList.get(0);
				String blockedYN	=	(String)alOnlineAccList.get(1);
				//String blockedYN	=	"Y";
				String wrongYN		=	(String)alOnlineAccList.get(2);
				String validatedYN	=	(String)alOnlineAccList.get(3);
				String strForward	=	"";
				
				if(outdatedYN.equalsIgnoreCase("Y"))
				{
					strForward	=	"cashlessAdd";
					frmCashlessAdd.set("outdatedOtp", "Entered OTP number is outdated, Please generate new OTP number");
					request.setAttribute("outdated", "outdated");
				}
				if(blockedYN.equalsIgnoreCase("Y"))
				{
					strForward	=	"cashlessAdd";
					frmCashlessAdd.set("blockedOtp", "Entered OTP number is blocked, Please generate new OTP number");
					request.setAttribute("blocked", "blocked");
				}
				if(wrongYN.equalsIgnoreCase("Y"))
				{
					strForward	=	"otpvalidate";
					frmCashlessAdd.set("wrongOtp", "Entered OTP number is Wrong, Please enter valid OTP number");
					request.setAttribute("wrong", "wrong");
					
				}
				if(validatedYN.equalsIgnoreCase("Y"))
				{
					//strForward	=	"showtests";
					strForward		=	strOTPVAlidate;
					request.setAttribute("OTP_Done","Done");
				}
				/*request.getSession().removeAttribute("frmCashlessAdd");*/
				request.getSession().setAttribute("frmCashlessAdd",frmCashlessAdd);
				frmCashlessAdd.set("enrollId", request.getSession().getAttribute("enrollId"));
				frmCashlessAdd.set("benifitType", request.getSession().getAttribute("benifitType"));
				frmCashlessAdd.set("flag", flag);
				request.setAttribute("flag",flag);
				request.setAttribute("logicVar","true");
				
				//finally return to the grid screen
				return this.getForward(strForward, mapping, request);
	        }//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	        }//end of catch(Exception exp)
	    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                            //HttpServletResponse response)
		
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
		public ActionForward onCloseReqAmnts(ActionMapping mapping,ActionForm form,HttpServletRequest request,
						   HttpServletResponse response) throws TTKException{
			try{
			log.debug("Inside the onCloseReqAmnts method of doClose");
			 setOnlineLinks(request);

			return this.getForward(strShowTests, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strShowTests));
			}//end of catch(Exception exp)
			}//end of Close(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
	   
		
		public ActionForward doSubmitTests(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
	    	try{
	            log.info("Inside the doSubmitTests method of OnlineClmSearchHospAction");
	            setOnlineLinks(request);
	            
	            DynaActionForm frmCashlessAdd = (DynaActionForm)form;
	            frmCashlessAdd.initialize(mapping);     //reset the form data
				log.info("off id " +frmCashlessAdd.get("sOffIds"));
				request.getSession().setAttribute("sOffIds", request.getParameter("sOffIds"));
				
				String sOffIds=request.getParameter("sOffIds");
				/*String splitIds	=	sOffIds.substring(1,sOffIds.length()-1);
				splitIds	=	splitIds.replace("|","','");
				splitIds	=	"'"+splitIds+"'";
				  */
				
				OnlineAccessManager onlineAccessManager = null;
				
				onlineAccessManager = this.getOnlineAccessManagerObject();
				
				ArrayList alDiagDataList= onlineAccessManager.getDiagDetails(sOffIds);
				request.setAttribute("alDiagDataList",alDiagDataList);	
				request.getSession().setAttribute("alDiagDataListSession",alDiagDataList);
				/*frmCashlessAdd.set("flag", request.getSession().getAttribute("flag"));*/
				frmCashlessAdd.set("enrollId", request.getSession().getAttribute("enrollId"));
	            frmCashlessAdd.set("flag", "true");
	            request.setAttribute("flag","true");
	            frmCashlessAdd.set("logicValidateVar", "logicValidateVar1");
	            request.setAttribute("logicValidateVar","logicValidateVar1");
	            //get the session bean from the bean pool for each excecuting thread
	            return this.getForward(strOnlineEnterList, mapping, request);
	        }//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	        }//end of catch(Exception exp)
	    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            //HttpServletResponse response)
	    
	    
	    
	    public ActionForward doShowTests(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
	    	try{
	            log.info("Inside the doShowTests method of OnlineClmSearchHospAction");
	            setOnlineLinks(request);
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
				DynaActionForm frmCashlessAdd = (DynaActionForm)form;
	            frmCashlessAdd.initialize(mapping);     //reset the form data
	            frmCashlessAdd.set("flag", "true");
	            //get the session bean from the bean pool for each excecuting thread
	            String GroupID	=	request.getParameter("GroupID");
	           /*   
	            request.getSession().setAttribute("GroupID", GroupID);*/
	            
	            
	            /*
	             * DynaActionForm frmCashlessAdd = (DynaActionForm)form;
	            frmCashlessAdd.initialize(mapping);
	            TableData tableTestsDetails = null;
	    		//get the Table Data From the session 
	    		if(request.getSession().getAttribute("tableTestsDetails")!=null)
	    		{
	    			tableTestsDetails=(TableData)(request.getSession()).getAttribute("tableTestsDetails");
	    		}//end of if(request.getSession().getAttribute("tableDataLinkDetails")!=null)
	    		else
	    		{
	    			tableTestsDetails=new TableData();
	    		}//end of else
	    		
	    		ArrayList alLinkDetailsList=null;
	    		
	    		
	    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	    		if(!strSortID.equals(""))
	    		{
	    			tableTestsDetails.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
	    			tableTestsDetails.modifySearchData("sort");//modify the search data
	    		}// end of if(!strSortID.equals(""))
	    		else
	    		{
	    			tableTestsDetails.createTableInfo("DcTestsDetails",null);
	    			tableTestsDetails.setSearchData(this.populateSearchCriteriaNew(request));
	    			tableTestsDetails.modifySearchData("search");
	    		}// end of else
	    		OnlineAccessManager onlineAccessManagerObject = null;
	    		onlineAccessManagerObject = this.getOnlineAccessManagerObject();
	    		Long tempHospSeqId	=	userSecurityProfile.getHospSeqId();
	    		String hospSeqId	=	""+tempHospSeqId;
	    		
	    		alLinkDetailsList=onlineAccessManagerObject.getTestsForDC(hospSeqId);
	    		tableTestsDetails.setData(alLinkDetailsList);
	    		request.getSession().setAttribute("tableTestsDetails",tableTestsDetails);
	    		request.setAttribute("frmCashlessAdd",frmCashlessAdd);
	    		*/
	    		
	    		
	            return this.getForward(strShowTests, mapping, request);
	        }//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	        }//end of catch(Exception exp)
	    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            //HttpServletResponse response)

	    
	    /**
		 * This method builds all the search parameters to ArrayList and places them in session
		 * @param request The HTTP request we are processing
		 * @return alSearchParams ArrayList
		 */
	    public ActionForward doSubmitEnteredRates(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
	    	try{
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
	            log.info("Inside the doSubmitEnteredRates method of OnlineCashlessHospAction");
	            setOnlineLinks(request);
	            //get the session bean from the bean pool for each excecuting thread
	           String reqAmnt	=	request.getParameter("reqAmnt");
	           String reqAmnts	=	request.getParameter("reqAmnts");
	           //  
	           
	           	DynaActionForm frmCashlessAdd =(DynaActionForm)form;
	           	frmCashlessAdd.initialize(mapping);     //reset the form data
	           	//CashlessDetailVO.set("submitRates",TTKCommon.checkNull(reqAmnt));
	           	request.setAttribute("submitRates",reqAmnt);	
	           	request.setAttribute("reqAmnts",reqAmnts);
	           	
	           	//calling a method to save and calc amounts
	           	ArrayList alDiagDataListSession=(ArrayList)request.getSession().getAttribute("alDiagDataListSession");
	           	OnlineAccessManager onlineAccessManager = null;
				onlineAccessManager = this.getOnlineAccessManagerObject();
				Long memSeqIDForSelfFund	=	(Long)request.getSession().getAttribute("memSeqIDForSelfFund");
				Long diagSeqIdForSelfFund	=	(Long)request.getSession().getAttribute("diagSeqIdForSelfFund");
				
				//Long diagSeqIdForSelfFund	=(Long) request.getAttribute("diagSeqIdForSelfFund");
				
				ArrayList alDiagnosysEnrolResult= onlineAccessManager.saveDiagTestAmnts(alDiagDataListSession,reqAmnts,memSeqIDForSelfFund,diagSeqIdForSelfFund,userSecurityProfile.getUSER_SEQ_ID());
				
				//ArrayList alDiagnosysEnrolResult= onlineAccessManager.GetBillDetails(diagSeqIdForSelfFund);
				//ArrayList alDiagnosysTotalResult= onlineAccessManager.getDiagTestTotalAmnts(diagSeqIdForSelfFund);
				
				ArrayList alDiagnosysApprovedResult= onlineAccessManager.SubmitApproveBills(memSeqIDForSelfFund,diagSeqIdForSelfFund,userSecurityProfile.getUSER_SEQ_ID(),userSecurityProfile.getHospSeqId());
				
				ArrayList alDiagnosysTotalResult= onlineAccessManager.getDiagTestTotalAmnts(diagSeqIdForSelfFund);

				String preAuthNumb	=	(String)alDiagnosysApprovedResult.get(0);
				
				request.setAttribute("alDiagnosysEnrolResult",alDiagnosysEnrolResult);	
				request.getSession().setAttribute("alDiagnosysEnrolResult",alDiagnosysEnrolResult);
				request.getSession().setAttribute("alDiagnosysTotalResult",alDiagnosysTotalResult);
	            frmCashlessAdd.set("flag", "true");		
	            request.setAttribute("flag","true");
	            frmCashlessAdd.set("enrollId", request.getSession().getAttribute("enrollId"));
	          
	            request.setAttribute("logicValidateVar","logicValidateVar1");
	            frmCashlessAdd.set("preAuthNumb", preAuthNumb);
	            return this.getForward("cashlessAddTotal", mapping, request);
	        }//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	        }//end of catch(Exception exp)
	    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            //HttpServletResponse response)
	    
	    
	    
	    /**
		 * This method builds all the search parameters to ArrayList and places them in session
		 * @param request The HTTP request we are processing
		 * @return alSearchParams ArrayList
		 */
	   /* public ActionForward doSubmitApproveBills(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
	    	try{
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
	            log.info("Inside the doSubmitApproveBills method of OnlineCashlessHospAction");
	            setOnlineLinks(request);
	            
	           	DynaActionForm frmCashlessAdd =(DynaActionForm)form;
	           	frmCashlessAdd.initialize(mapping);     //reset the form data
	           	
	           	//calling a method to save and calc amounts
	           	ArrayList alDiagnosysTotalResult=(ArrayList)request.getSession().getAttribute("alDiagnosysTotalResult");
	           	OnlineAccessManager onlineAccessManager = null;
				onlineAccessManager = this.getOnlineAccessManagerObject();
				Long memSeqIDForSelfFund	=	(Long)request.getSession().getAttribute("memSeqIDForSelfFund");
				Long diagSeqIdForSelfFund	=	(Long)request.getSession().getAttribute("diagSeqIdForSelfFund");
				
				ArrayList alDiagnosysEnrolResult= onlineAccessManager.SubmitApproveBills(memSeqIDForSelfFund,diagSeqIdForSelfFund,userSecurityProfile.getUSER_SEQ_ID(),userSecurityProfile.getHospSeqId());
								
				request.setAttribute("alDiagnosysEnrolResult",alDiagnosysEnrolResult);	
				request.getSession().setAttribute("alDiagnosysEnrolResult",alDiagnosysEnrolResult);
	            frmCashlessAdd.set("enrollId", request.getSession().getAttribute("enrollId"));
	            request.setAttribute("flag","true");
	            frmCashlessAdd.set("logicValidateVar", "logicValidateVar1");
	            request.setAttribute("logicValidateVar","logicValidateVar1");
	            
	            return this.getForward(strOnlineClaimList, mapping, request);
	        }//end of try
	        catch(TTKException expTTK)
	        {
	            return this.processOnlineExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	        }//end of catch(Exception exp)
	    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            //HttpServletResponse response)
	    */
	    
	    
	    
	    
	    /**
		 * This method is used to navigate to Member Vital screen.
		 * Finally it forwards to the appropriate view based on the specified forward mappings
		 *
		 * @param mapping The ActionMapping used to select this instance
		 * @param form The optional ActionForm bean for this request (if any)
		 * @param request The HTTP request we are processing
		 * @param response The HTTP response we are creating
		 * @return ActionForward Where the control will be forwarded, after this request is processed
		 * @throws Exception if any error occurs
		 */
		public ActionForward doMemberVitals(ActionMapping mapping,ActionForm form,HttpServletRequest request,
						   HttpServletResponse response) throws TTKException{
			try{
				log.info("Inside the onMemberVitals method of doMemberVitals");
				 setOnlineLinks(request);
				 DynaActionForm frmCashlessMemberVitals =(DynaActionForm)form;
				 frmCashlessMemberVitals.initialize(mapping);     //reset the form data
				 request.getSession().setAttribute("frmCashlessMemberVitals", frmCashlessMemberVitals);
				return this.getForward(strMemberVitals, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
			}//end of catch(Exception exp)
			}//end of Close(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
		
		/*
		 * 
		 * Skip Page
		 * 
		 */
		public ActionForward doSkipVitals(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				   HttpServletResponse response) throws TTKException{
		try{
		log.debug("Inside the doSkipVitals method of doClose");
		 setOnlineLinks(request);
		 DynaActionForm frmCashlessMemberVitals =(DynaActionForm)form;
		 frmCashlessMemberVitals.initialize(mapping);     //reset the form data
		 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
		 
		//this code is to get the member and other details based on the enroll id
		OnlineAccessManager onlineAccessManager	=	null;
		onlineAccessManager	=	this.getOnlineAccessManagerObject();
		CashlessDetailVO cashlessDetailVO	=	null;
		cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollId((String)request.getSession().getAttribute("enrollId"),(String)request.getSession().getAttribute("benifitTypeVal"), new Long(0));
		frmCashlessMemberVitals = (DynaActionForm)FormUtils.setFormValues("frmCashlessMemberVitals",
				cashlessDetailVO,this,mapping,request);
		frmCashlessMemberVitals.set("enrollId", request.getSession().getAttribute("enrollId"));
		frmCashlessMemberVitals.set("providerName", userSecurityProfile.getHospitalName());
		request.setAttribute("frmCashlessMemberVitals", frmCashlessMemberVitals);
		return this.getForward("prescriptionDetails", mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
		}//end of catch(Exception exp)
		}//end of doSkipVitals(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
		
		/*
		 * 
		 * Save Page
		 * 
		 */
		public ActionForward doVitalsSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				   HttpServletResponse response) throws TTKException{
		try{
		log.info("Inside the doSaveVitals method of doClose");
		 setOnlineLinks(request);
		 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
		 DynaActionForm frmCashlessMemberVitals =(DynaActionForm)form;
		 OnlineAccessManager onlineAccessManager	=	null;
		onlineAccessManager = this.getOnlineAccessManagerObject();
		CashlessDetailVO	cashlessDetailVO	=	new CashlessDetailVO();
		String enrollId=(String) request.getSession().getAttribute("enrollId");
		cashlessDetailVO = (CashlessDetailVO)FormUtils.getFormValues(frmCashlessMemberVitals,"frmCashlessMemberVitals",
				this,mapping,request);
		int iResult	=	0;
		iResult	= onlineAccessManager.saveMemberVitals(enrollId,cashlessDetailVO,userSecurityProfile.getUSER_SEQ_ID());
		if(iResult>0)
			request.setAttribute("updated","message.addedSuccessfully");
		
		//this code is to get the member and other details based on the enroll id
		onlineAccessManager	=	this.getOnlineAccessManagerObject();
		cashlessDetailVO	=	null;
		cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollId((String)request.getSession().getAttribute("enrollId"),(String)request.getSession().getAttribute("benifitTypeVal"),new Long(0));
		frmCashlessMemberVitals = (DynaActionForm)FormUtils.setFormValues("frmCashlessMemberVitals",
				cashlessDetailVO,this,mapping,request);
		frmCashlessMemberVitals.set("enrollId", request.getSession().getAttribute("enrollId"));
		frmCashlessMemberVitals.set("providerName", userSecurityProfile.getHospitalName());
		request.setAttribute("frmCashlessMemberVitals", frmCashlessMemberVitals);

		return this.getForward("prescriptionDetails", mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
		}//end of catch(Exception exp)
		}//end of doSaveVitals(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			
		
		
		
		/**
		 * 
		 * @param mapping The ActionMapping used to select this instance
		 * @param form The optional ActionForm bean for this request (if any)
		 * @param request The HTTP request we are processing
		 * @param response The HTTP response we are creating
		 * @return Selected labs
		 * @throws Exception, if any error occurs
		 */
		public ActionForward doLabsAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
		try{
			log.info("Inside the doAddLabs method of OnlineCashlessHospAction");
			setOnlineLinks(request);
			
			DynaActionForm frmCashlessPrescription =(DynaActionForm)form;
			frmCashlessPrescription.initialize(mapping);     //reset the form data
			
			 OnlineAccessManager onlineAccessManager	=	null;
			 onlineAccessManager = this.getOnlineAccessManagerObject();
				
				
			request.getSession().setAttribute("frmCashlessPrescription", frmCashlessPrescription);
			return this.getForward(strAddLaboratories, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
		}//end of catch(Exception exp)
		}//end of doAddLabs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)
		
		
		
		//DownLoad Online PreAuth Form
		public ActionForward doPrintForms(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			 try{
				 log.debug("Inside the doGeneratePreAuthLetter method of OnlineReportsAction");
				 setOnlineLinks(request);
				 JasperReport jasperReport,emptyReport;
				 JasperPrint jasperPrint;
				 TTKReportDataSource ttkReportDataSource = null;
				 ResultSet rs = null;
				 String jrxmlfile= null;		 
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
				 ttkReportDataSource = new TTKReportDataSource("OnlinePreAuthForm",(String)request.getSession().getAttribute("enrollId"),(String)request.getSession().getAttribute("benifitTypeVal"),userSecurityProfile.getHospSeqId().toString());
				 
				OnlineAccessManager onlineAccessManager = null;
				onlineAccessManager = this.getOnlineAccessManagerObject();

				CashlessDetailVO cashlessDetailVO	=	new CashlessDetailVO();
				cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollIdNew((String)request.getSession().getAttribute("enrollId"));

				 rs = ttkReportDataSource.getResultData();	
				 try
				 {
					 ByteArrayOutputStream boas=new ByteArrayOutputStream();
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
					 
					 hashMap.put("MEM_GENDER", cashlessDetailVO.getGender());
					 hashMap.put("MEM_NAME", cashlessDetailVO.getMemberName());
					 hashMap.put("MEM_AGE", cashlessDetailVO.getAge());
					 hashMap.put("MEM_DOB", cashlessDetailVO.getPolicyEnDt());
					 hashMap.put("MEM_ENROLID", cashlessDetailVO.getEnrollId());
					 hashMap.put("MEM_POLICY", cashlessDetailVO.getPolicyNo());
					 hashMap.put("MEM_MONTHS", cashlessDetailVO.getEligibility());
					 hashMap.put("MEM_EMPLOYEE_NO", cashlessDetailVO.getEnrollmentID());
					 hashMap.put("COMPANY_NAME", cashlessDetailVO.getInsurredName());
					 hashMap.put("MOBILE", cashlessDetailVO.getModeType());
					 hashMap.put("EMIRATE_ID", cashlessDetailVO.getEmirateID());
					 hashMap.put("EMAIL_ID", cashlessDetailVO.geteMailId());
					 hashMap.put("event_no", cashlessDetailVO.getEventReferenceNo());					 
					 if(rs.next()){
							 jrxmlfile = "providerLogin/OnlinePreAuthForm.jrxml";
							 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
							 rs.previous();
							 jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,new TTKReportDataSource(rs));
					 }//if(rs.next())
					 else {
						 jrxmlfile = "generalreports/EmptyReprot.jrxml";
						 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new JREmptyDataSource());
					 }//end of else
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
					 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
					 request.setAttribute("boas",boas);
				 }//end of try
				 catch (JRException e)
				 {
					 e.printStackTrace();
				 }//end of catch(JRException e)
				 catch (Exception e)
				 {
					 e.printStackTrace();
				 }//end of catch (Exception e)
				 return (mapping.findForward(strReportdisplay));
			 }//end of try
			 catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
			 catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			 }//end of catch(Exception exp)
		 }//end of doPrintForms(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		 
		
		
		//DownLoad Online PreAuth Form
		public ActionForward doPrintDentalForms(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			 try{
				 log.debug("Inside the doPrintDentalForms method of OnlineReportsAction");
				 setOnlineLinks(request);
				 JasperReport jasperReport,emptyReport,jasperReport1;
				 JasperPrint jasperPrint;
				 TTKReportDataSource ttkReportDataSource = null;
				 ResultSet rs = null;
				 String jrxmlfile,jrxmlfile1= null;		 
				 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
				 ttkReportDataSource = new TTKReportDataSource("OnlineDentalForm",(String)request.getSession().getAttribute("enrollId"),(String)request.getSession().getAttribute("benifitTypeVal"),userSecurityProfile.getHospSeqId().toString());
				 
				OnlineAccessManager onlineAccessManager = null;
				onlineAccessManager = this.getOnlineAccessManagerObject();

				CashlessDetailVO cashlessDetailVO	=	new CashlessDetailVO();
				cashlessDetailVO	= onlineAccessManager.geMemberDetailsOnEnrollIdNewDental((String)request.getSession().getAttribute("enrollId"));

				 rs = ttkReportDataSource.getResultData();	
				 try
				 {
					 ByteArrayOutputStream boas=new ByteArrayOutputStream();
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
					 hashMap.put("MEM_GENDER", cashlessDetailVO.getGender());
					 hashMap.put("MEM_NAME", cashlessDetailVO.getMemberName());
					 hashMap.put("MEM_AGE", cashlessDetailVO.getAge());
					 hashMap.put("MEM_DOB", cashlessDetailVO.getPolicyEnDt());
					 hashMap.put("MEM_ENROLID", cashlessDetailVO.getEnrollId());
					 hashMap.put("MEM_POLICY", cashlessDetailVO.getPolicyNo());
					 hashMap.put("MEM_MONTHS", cashlessDetailVO.getEligibility());
					 hashMap.put("MEM_EMPLOYEE_NO", cashlessDetailVO.getEnrollmentID());
					 hashMap.put("COMPANY_NAME", cashlessDetailVO.getInsurredName());
					 hashMap.put("MOBILE", cashlessDetailVO.getModeType());
					 hashMap.put("EMIRATE_ID", cashlessDetailVO.getEmirateID());
					 hashMap.put("EMAIL_ID", cashlessDetailVO.geteMailId());
					 hashMap.put("event_no", cashlessDetailVO.getEventReferenceNo());					 
					 if(rs.next()){
						 jrxmlfile = "providerLogin/OnlineDentalForm.jrxml";
						 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
						 rs.previous();
						 jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap,new TTKReportDataSource(rs));
				 }//if(rs.next())
					 else {
						 jrxmlfile = "generalreports/EmptyReprot.jrxml";
						 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
						 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,new JREmptyDataSource());
					 }//end of else
					 JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
					 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
					 request.setAttribute("boas",boas);
				 }//end of try
				 catch (JRException e)
				 {
					 e.printStackTrace();
				 }//end of catch(JRException e)
				 catch (Exception e)
				 {
					 e.printStackTrace();
				 }//end of catch (Exception e)
				 return (mapping.findForward(strReportdisplay));
			 }//end of try
			 catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }//end of catch(TTKException expTTK)
			 catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
			 }//end of catch(Exception exp)
		 }//end of doPrintForms(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		 
		
		
		
		
		
		
		
		

	/*	public ActionForward doPrintDentalForms(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		        HttpServletResponse response) throws Exception{
		    
			 ByteArrayOutputStream baos=null;
		    OutputStream sos = null;
		    FileInputStream fis = null; 
		    BufferedInputStream bis =null;
		  try{
				String strFile	=	"/home/tipsint/jboss-as-7.1.3.Final/bin/ProjectX/ProviderLogin/DENTAL PRE APPROVAL FORM.pdf";
				File file=new File(strFile);
				
					  response.setContentType("application/pdf");
				      response.setHeader("Content-Disposition", "attachment;filename=DENTAL PRE APPROVAL FORM.pdf");
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
		}//end of doPrintDentalForms(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)
*/		
		 public ActionForward doBackToEligibility(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                 HttpServletResponse response) throws Exception{
	try{
		setOnlineLinks(request);
		log.debug("Inside the doBackToEligibility method of OnlineCashlessHospAction");
		DynaActionForm frmCashlessAdd =(DynaActionForm) request.getSession().getAttribute("frmCashlessAdd");
		
		CashlessDetailVO cashlessDetailVO = (CashlessDetailVO)FormUtils.getFormValues(frmCashlessAdd,"frmCashlessMemberVitals",
				this,mapping,request);
		frmCashlessAdd = (DynaActionForm)FormUtils.setFormValues("frmCashlessAdd",
				   cashlessDetailVO,this,mapping,request);
		
		request.getSession().setAttribute("frmCashlessAdd",frmCashlessAdd);
		
		request.getSession().setAttribute("cashlessDetailVO",cashlessDetailVO);
		return this.getForward(strOTPVAlidate, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
	}//end of catch(Exception exp)
}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
		
	 	 /**
		     * This method is used to Authenticate member using finger print. If successful we will allow to check
		     * Eligibility else A option to register is given.
		     *
		     * @param mapping The ActionMapping used to select this instance
		     * @param form The optional ActionForm bean for this request (if any)
		     * @param request The HTTP request we are processing
		     * @param response The HTTP response we are creating
		     * @return ActionForward Where the control will be forwarded, after this request is processed
		     * @throws Exception if any error occurs
		     */
    public ActionForward doAuthenticateMemberFingerPrint(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		                                                    HttpServletResponse response) throws Exception{
 	   String result="";
 	   try{
 	          setOnlineLinks(request);
 	          log.debug("Inside the doAuthenticateMemberFingerPrint method of OnlineCashlessHospAction");
 	          DynaActionForm frmCashlessAdd =(DynaActionForm) form;//(request.getSession().getAttribute("frmCashlessAdd")==null?(DynaActionForm)form:request.getSession().getAttribute("frmCashlessAdd"));
 	          
 				OnlineAccessManager onlineAccessManager = null;
 				byte[] fingerPrintFile =null;
 				onlineAccessManager = this.getOnlineAccessManagerObject();
 				
 				String enrollId	= ((String)(frmCashlessAdd.get("enrollId"))).trim();
 				String fingerPrintStr =(String)(frmCashlessAdd.get("fingerPrintStr"));
 				
 				if(enrollId==null||enrollId.trim().equals("")||enrollId.trim().equals("Al Koot Id / Qatar Id"))
 					throw new TTKException("error.selffund.validateVidalID");
 				
 				if(!(fingerPrintStr==null||fingerPrintStr.trim().equals(""))){
 				fingerPrintFile = this.decodeToImage(fingerPrintStr);
 				result = onlineAccessManager.getAuthenticateMemberFingerPrint(enrollId, fingerPrintFile);
 				
 				request.setAttribute("fingerPrintAuthentication", result);
 				
 				if(("notValidAlkootId").equals(result.trim())){
 					throw new TTKException("error.invalidAlkootId");
 				}
 				
 				if(("notRegistered").equals(result.trim())){
 					request.setAttribute("enrollId", (String)(frmCashlessAdd.get("enrollId")));	
 					throw new TTKException("error.notRegistered");	
 				}
 				
 				if(("matched").equals(result.trim())){
 				request.setAttribute("enrollId", (String)(frmCashlessAdd.get("enrollId")));
 				request.setAttribute("memberFingerPrintAuthenticated", "true");
 				request.setAttribute("updated","message.authenticationSuccessfull");
 				}else
 					throw new TTKException("error.authenticationFailure");
 				}
 				
 				return this.getForward(strOnlineClaimList, mapping, request);
 	      }//end of try
 	      catch(TTKException expTTK)
 	      {
 	          return this.processOnlineExceptions(request, mapping, expTTK);
 	      }//end of catch(TTKException expTTK)
 	      catch(Exception exp)
 	      {
 	          return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
 	      }//end of catch(Exception exp)
 	  }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
 	                          //HttpServletResponse response)  	
		
    
    
	 	/**
	     * This method is used to Register member's finger print.
	     *
	     * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	     */
public ActionForward doRegisterMemberFingerPrint(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	                                                    HttpServletResponse response) throws Exception{
	  int result = 0;
	  try{
       setOnlineLinks(request);
       log.debug("Inside the doRegisterMemberFingerPrint method of OnlineCashlessHospAction");
       DynaActionForm frmCashlessAdd =(DynaActionForm) form;//(request.getSession().getAttribute("frmCashlessAdd")==null?(DynaActionForm)form:request.getSession().getAttribute("frmCashlessAdd"));
       
			OnlineAccessManager onlineAccessManager = null;
			byte[] fingerPrintFile =null;
			onlineAccessManager = this.getOnlineAccessManagerObject();
			
			String enrollId	=	 (String) (frmCashlessAdd.get("enrollId"));
			String fingerPrintStr =(String)(frmCashlessAdd.get("fingerPrintStr"));
			
			if(enrollId==null||enrollId.trim().equals("")||enrollId.trim().equals("Al Koot Id / Qatar Id"))
					throw new TTKException("error.selffund.validateVidalID");
			
			if(!(fingerPrintStr==null||fingerPrintStr.trim().equals("null")||fingerPrintStr.trim().equals(""))){
			fingerPrintFile = this.decodeToImage(fingerPrintStr);
			result = onlineAccessManager.getRegisterMemberFingerPrint(enrollId, fingerPrintFile);
			}
			
			if(result==1){
				request.setAttribute("fingerPrintAuthentication", result);
				request.setAttribute("memberFingerPrintAuthenticated", "true");
				request.setAttribute("enrollId", (String)(frmCashlessAdd.get("enrollId")));
				request.setAttribute("fingerPrintAuthentication", "matched");
				request.setAttribute("updated","message.registrationSuccessfull");
			}
			
			
			return this.getForward(strOnlineClaimList, mapping, request);
   }//end of try
   catch(TTKException expTTK)
   {
       return this.processOnlineExceptions(request, mapping, expTTK);
   }//end of catch(TTKException expTTK)
   catch(Exception exp)
   {
       return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
   }//end of catch(Exception exp)
}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                       //HttpServletResponse response)  	
	    	
		 
		 
	    /**
		 * This method builds all the search parameters to ArrayList and places them in session
		 * @param request The HTTP request we are processing
		 * @return alSearchParams ArrayList
		 */
		private ArrayList<Object> populateSearchCriteriaNew(HttpServletRequest request)
		{
			//build the column names along with their values to be searched
			ArrayList<Object> alSearchParams = new ArrayList<Object>();
			alSearchParams.add(TTKCommon.getWebBoardId(request));
			alSearchParams.add(TTKCommon.getUserSeqId(request));
			return alSearchParams;
		}//end of populateSearchCriteria(Long lngMemberSeqId)
		

		
		public ActionForward doPageUnderDevelop(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside the doPageUnderDevelop method of OnlineCashlessHospAction");
			return mapping.findForward(pageUnderDevelop);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlineClaimList));
		}//end of catch(Exception exp)
		}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)
		
		 private byte[] decodeToImage(String imageString) {
		        
		        byte[] imageByte = null;
		       
		        try {
		            BASE64Decoder decoder = new BASE64Decoder();
		            imageByte = decoder.decodeBuffer(imageString);
		            
		            /*ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		            BufferedImage image = ImageIO.read(bis);
		            bis.close();
		            File temp = new File("C:/Image/img.jpg");
		            temp.createNewFile();
					InputStream in = new ByteArrayInputStream(imageByte);
					BufferedImage bImageFromConvert = ImageIO.read(in);
					ImageIO.write(bImageFromConvert,"jpg", temp);*/
		        
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return imageByte;
		    }
}//end of OnlineClmSearchHospAction
