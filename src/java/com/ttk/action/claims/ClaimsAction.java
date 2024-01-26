/**
* @ (#) ClaimsAction.java Jul 14, 2006
* Project       : TTK HealthCare Services
* File          : ClaimsAction.java
* Author        : Chandrasekaran J
* Company       : Span Systems Corporation
* Date Created  : Jul 14, 2006

* @author       : Chandrasekaran J
* Modified by   :
* Modified date :
* Reason :
*/

package com.ttk.action.claims;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ttk.business.claims.ClaimManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.empanelment.TdsCertificateVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.ClinicianDetailsVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.MemberDetailVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.ProviderDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching of Claims
 */

public class ClaimsAction extends TTKAction
{
	private static Logger log = Logger.getLogger(ClaimsAction.class);

	//declrations of modes
	private static final String strForward="Forward";
	private static final String strBackward="Backward";

	//Action mapping forwards.
	private static final  String strClaimslist="claimslist";
	private static final  String strClaimDetail="claimdetail";

    private static final int ASSIGN_ICON=7;

    //Exception Message Identifier
    private static final String strClaimSearchError="hospitalsearch";
    private static final String strProviderList="providerSearchList";
    private static final String strMemberList="memberSearchList";
	private static final String strClinicianList="clinicianSearchList";
	private static final String strDiagnosisCodeList="diagnosisSearchList";
	private static final String strActivityCodeList="activityCodeList";
	private static final String strActivitydetails="activitydetails";
	
	
	
	private static final String strDocsUpload="DocsUpload";	
	private static final String strUpload="upload";
	private static final String strClaimsUploadClose="claimUploadClose";
	private static final String strDocsdelete="docsUploadDelete";

	/**

    /**
     * This method is used to initialize the search grid.
     * Finally it forwards to the appropriate view based on the specified forward mappings
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
    		log.debug("Inside the doDefault method of ClaimsAction");
    		setLinks(request);
    		String strDefaultBranchID  = String.valueOf(((UserSecurityProfile)
    										request.getSession().getAttribute("UserSecurityProfile")).getBranchID());
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
			//clear the dynaform if visiting from left links for the first time
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmClaimsList=(DynaActionForm)form;
			//create new table data object
			tableData = new TableData();
			//create the required grid table
			tableData.createTableInfo("ClaimsTable",new ArrayList());
			request.getSession().setAttribute("tableData",tableData);
			((DynaActionForm)form).initialize(mapping);//reset the form data
			frmClaimsList.set("sTtkBranch",strDefaultBranchID);
			return this.getForward(strClaimslist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
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
    		log.debug("Inside the doSearch method of ClaimsAction");
    		setLinks(request);
    		ClaimManager claimManagerObject=this.getClaimManagerObject();
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
			//PreAuthVO preAuthVO=new PreAuthVO;
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
					return mapping.findForward(strClaimslist);
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
				tableData.createTableInfo("ClaimsTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
                this.setColumnVisiblity(tableData,(DynaActionForm)form,request);
				tableData.modifySearchData("search");
			}//end of else
			
			
			ArrayList alClaimsList= claimManagerObject.getClaimList(tableData.getSearchData());
			tableData.setData(alClaimsList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			//finally return to the grid screen
			
			DynaActionForm frmClaimsList=(DynaActionForm)form;
			if(TTKCommon.isAuthorized(request,"AssignAll"))
    					frmClaimsList.set("AssignAllFlagYN","Y");
			else
				frmClaimsList.set("AssignAllFlagYN","N");
			
			if(TTKCommon.isAuthorized(request,"Assign"))
				frmClaimsList.set("AssignFlagYN","Y");
			else
				frmClaimsList.set("AssignFlagYN","N");
			
			return this.getForward(strClaimslist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
		}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    public ActionForward doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
        try{
            log.debug("Inside the doBackward method of doStatusChange");
           
            return this.getForward(strClaimslist, mapping, request);   //finally return to the grid screen
           }//end of try
         catch(TTKException expTTK)
        {
          return this.processExceptions(request, mapping, expTTK);
            }//end of catch(TTKException expTTK)
         catch(Exception exp)
         {
          return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
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
    public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    										HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doBackward method of ClaimsAction");
    		setLinks(request);
    		ClaimManager claimManagerObject=this.getClaimManagerObject();
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strBackward);//modify the search data
			ArrayList alClaimsList = claimManagerObject.getClaimList(tableData.getSearchData());
			tableData.setData(alClaimsList, strBackward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return this.getForward(strClaimslist, mapping, request);   //finally return to the grid screen
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
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
    		log.debug("Inside the doForward method of ClaimsAction");
    		setLinks(request);
    		ClaimManager claimManagerObject=this.getClaimManagerObject();
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strForward);//modify the search data
			ArrayList alClaimsList = claimManagerObject.getClaimList(tableData.getSearchData());
			tableData.setData(alClaimsList, strForward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return this.getForward(strClaimslist, mapping, request);   //finally return to the grid screen
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
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
    		log.debug("Inside the doCopyToWebBoard method of ClaimsAction");
    		setLinks(request);
    		this.populateWebBoard(request);
			return this.getForward(strClaimslist, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
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
    		setLinks(request);
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		DynaActionForm frmClaimsList=(DynaActionForm)form;
    		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				PreAuthVO preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
				request.getSession().setAttribute("claimStatus",preAuthVO.getStatus());
				request.getSession().setAttribute("preAuthStatus",null);
				request.getSession().setAttribute("fastTrackFlag",preAuthVO.getFastTrackFlag());
				request.getSession().setAttribute("fastTrackMsg",preAuthVO.getFastTrackMsg());	
				this.addToWebBoard(preAuthVO, request);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			return mapping.findForward(strClaimDetail);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
		}//end of catch(Exception exp)
    }//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * this method will add search criteria fields and values to the arraylist and will return it
     * @param frmClaimsList formbean which contains the search fields
     * @param request HttpServletRequest
     * @return ArrayList contains search parameters
     */
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmClaimsList,HttpServletRequest request)
    {
    	//build the column names along with their values to be searched
    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
    	alSearchParams.add(frmClaimsList.getString("sInvoiceNO"));//0
    	alSearchParams.add(frmClaimsList.getString("sBatchNO"));//1
    	alSearchParams.add(frmClaimsList.getString("sPolicyNumber"));//2
    	alSearchParams.add(frmClaimsList.getString("sClaimNO"));//3
    	alSearchParams.add(frmClaimsList.getString("sClaimType"));//4
    	alSearchParams.add(frmClaimsList.getString("sRecievedDate"));//5
    	alSearchParams.add(frmClaimsList.getString("sSettlementNO"));//6
    	alSearchParams.add(frmClaimsList.getString("sEnrollmentId"));//7
    	alSearchParams.add(frmClaimsList.getString("sClaimantName"));//8
		//alSearchParams.add((String)frmClaimsList.getString("sTtkBranch"));
		alSearchParams.add(frmClaimsList.getString("sStatus"));//9
		alSearchParams.add(frmClaimsList.getString("sProviderName"));//10
		alSearchParams.add(frmClaimsList.getString("sPayerName"));//11
		alSearchParams.add(frmClaimsList.getString("sModeOfClaim"));//12
		alSearchParams.add(frmClaimsList.getString("sGlobalNetMemID"));//13
		alSearchParams.add(frmClaimsList.getString("sAssignedTo"));//14
		alSearchParams.add(frmClaimsList.getString("sSpecifyName"));//15
		alSearchParams.add(TTKCommon.getUserSeqId(request));//16
		alSearchParams.add(frmClaimsList.getString("sProcessType"));//17
		alSearchParams.add(frmClaimsList.getString("clmShortFallStatus"));//18
		alSearchParams.add(frmClaimsList.getString("eventReferenceNo"));//19
		alSearchParams.add(frmClaimsList.getString("sCommonFileNo"));//20
		alSearchParams.add(frmClaimsList.getString("internalRemarkStatus"));//21
		alSearchParams.add(frmClaimsList.getString("sPartnerName"));//22
		alSearchParams.add(frmClaimsList.getString("sBenefitType"));//23
		alSearchParams.add(frmClaimsList.getString("sAmountRange"));//24
		alSearchParams.add(frmClaimsList.getString("sAmountRangeValue"));//25
		alSearchParams.add(frmClaimsList.getString("sLinkedPreapprovalNo"));//26
		alSearchParams.add(frmClaimsList.getString("sQatarId"));//27
		alSearchParams.add(frmClaimsList.getString("inProgressStatus"));//28	
		alSearchParams.add(frmClaimsList.getString("riskLevel"));//29
		alSearchParams.add(frmClaimsList.getString("cfdInvestigationStatus"));//30
		alSearchParams.add(((String) frmClaimsList.get("priorityClaims")));//31
		if(frmClaimsList.get("priorityClaims").equals("PRVA"))
		{
			alSearchParams.add(((String)frmClaimsList.get("rangeFrom")));//32
		}//end of if(frmClaims.get("priorityClaims").equals("PRVA"))
		else
		{
			alSearchParams.add(null);
		}//end of else
		if(frmClaimsList.get("priorityClaims").equals("PRVA"))
		{
			alSearchParams.add(((String) frmClaimsList.get("rangeTo")));//33
		}//end of if(frmClaims.get("priorityClaims").equals("PRVA"))
		else
		{
			alSearchParams.add(null);
		}//end of else
		alSearchParams.add(frmClaimsList.getString("priorityCorporate"));//34	
		alSearchParams.add(frmClaimsList.getString("auditStatus"));//35	
		
    	return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmClaimsList,HttpServletRequest request)
    
    
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
			log.debug("Inside ClaimAction providerSearch");
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

	public ActionForward doSelectProviderId(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside ClaimAction doSelectProviderId ");
	HttpSession session=request.getSession();
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
	String sPresentDate = simpleDateFormat.format(cal.getTime()); 
	int hour = cal.get(Calendar.HOUR);
	if(hour == 0)
	hour = 12;
	String sPresentTime = String.format("%02d:%02d", hour, cal.get(Calendar.MINUTE));
	String sAMorPM = "";
	if (cal.get(Calendar.AM_PM) == Calendar.AM)
	sAMorPM = "AM";
	else
	sAMorPM = "PM";
	
	
	
	TableData providerListData = (TableData)session.getAttribute("providerListData");

	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals(""))){
		
		ProviderDetailsVO providerDetailsVO=(ProviderDetailsVO)providerListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));

		String strActiveSubLink=TTKCommon.getActiveSubLink(request);
		String strActiveSubTab=TTKCommon.getActiveTab(request);
		
		
		if("BatchEntry".equals(strActiveSubLink)&&strActiveSubTab.equals("Claim Upload")){
			
			
			DynaActionForm frmClaimUpload=(DynaActionForm)session.getAttribute("frmClaimUpload");
			
			frmClaimUpload.set("providerSeqId", providerDetailsVO.getProviderSeqId().toString());
			frmClaimUpload.set("providerId", providerDetailsVO.getProviderId());
			frmClaimUpload.set("providerName", providerDetailsVO.getProviderName());
			frmClaimUpload.set("receiveDate", sPresentDate);
			frmClaimUpload.set("receivedTime",sPresentTime);
			frmClaimUpload.set("receiveDay", sAMorPM);
			
			session.setAttribute("frmClaimUpload", frmClaimUpload);
			
			DynaActionForm frmPreAuthList=(DynaActionForm)session.getAttribute("frmPreAuthList");
			
			frmPreAuthList.set("sProviderId", "");
			frmPreAuthList.set("sProviderName", "");
			return mapping.findForward("ClaimUploadProvider");
			
		}
		else
	   {   
           DynaActionForm frmClaimGeneral=(DynaActionForm)session.getAttribute("frmClaimGeneral");
	
	     String claimSeqID=(String)frmClaimGeneral.get("claimSeqID");
	
	    frmClaimGeneral.set("providerSeqId", providerDetailsVO.getProviderSeqId().toString());
	    frmClaimGeneral.set("providerId", providerDetailsVO.getProviderId());
	    frmClaimGeneral.set("providerName", providerDetailsVO.getProviderName());
	    frmClaimGeneral.set("provAuthority", providerDetailsVO.getAuthority());
	    frmClaimGeneral.set("providerSpecificRemarks", providerDetailsVO.getProviderSpecificRemarks());
	    frmClaimGeneral.set("requestedAmountcurrencyType",providerDetailsVO.getCurencyType());
	    frmClaimGeneral.set("conversionRate","");

		session.setAttribute("frmClaimGeneral",frmClaimGeneral);
		if(!(claimSeqID==null||claimSeqID.length()<1)){
			session.setAttribute("claimDiagnosis", null);
			session.setAttribute("claimActivities", null);
			session.setAttribute("claimShortfalls", null);	
		      }
		return mapping.findForward("claimdetail");
		
	}
	
	}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	return this.getForward(strClaimDetail, mapping, request);
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
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sEmpanelmentNo")));
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
			log.debug("Inside ClaimAction doProviderForward");
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
			log.debug("Inside ClaimAction doProviderBackward");
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
			log.debug("Inside ClaimAction clinicianSearch");
			setLinks(request);
			HttpSession session=request.getSession();
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			TableData clinicianListData = null;
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
			
			
			if("Y".equals(request.getParameter("Entry"))){
				frmPreAuthList.initialize(mapping);
				DynaActionForm	frmClaimGeneral=(DynaActionForm)session.getAttribute("frmClaimGeneral");
				frmPreAuthList.set("sProviderId",frmClaimGeneral.getString("hiddenHospitalID"));
				frmPreAuthList.set("sProviderName",frmClaimGeneral.getString("providerName"));
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
	log.debug("Inside ClaimAction doSelectclinicianId ");
	HttpSession session=request.getSession();
	TableData clinicianListData = (TableData)session.getAttribute("clinicianListData");
String forward=strClaimDetail;
	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	{
		ClinicianDetailsVO clinicianDetailsVO=(ClinicianDetailsVO)clinicianListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));
		
		if("activityClinicianSearch".equals(session.getAttribute("forwardMode"))){
			DynaActionForm frmActivityDetails=(DynaActionForm)session.getAttribute("frmActivityDetails");
			frmActivityDetails.set("clinicianId", clinicianDetailsVO.getClinicianId());
			session.setAttribute("frmActivityDetails",frmActivityDetails);
			forward="activitydetails";
		}else{
		DynaActionForm frmClaimGeneral=(DynaActionForm)session.getAttribute("frmClaimGeneral");
		String claimSeqID=frmClaimGeneral.getString("claimSeqID");
			
		frmClaimGeneral.set("clinicianId", clinicianDetailsVO.getClinicianId());
		frmClaimGeneral.set("clinicianName", clinicianDetailsVO.getClinicianName());
		frmClaimGeneral.set("clinicianSpeciality", clinicianDetailsVO.getClinicianSpeciality());
			
		   session.setAttribute("frmClaimGeneral",frmClaimGeneral);
			if(!(claimSeqID==null||claimSeqID.length()<1)){
				session.setAttribute("claimDiagnosis", null);
				session.setAttribute("claimActivities", null);
				session.setAttribute("claimShortfalls", null);	
			}
			forward=strClaimDetail;
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
log.debug("Inside ClaimsAction closeClinicians");

	return mapping.findForward("activitydetails");
}//end of try
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,"preauthdetail"));
}//end of catch(Exception exp)
}//end of closeClinicians(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
		log.debug("Inside ClaimsAction activityCodeSearch");
		setLinks(request);
		PreAuthManager preAuthObject=this.getPreAuthManagerObject();
		HttpSession session=request.getSession();
		DynaActionForm frmActivityDetails=(DynaActionForm)session.getAttribute("frmActivityDetails");
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
			activityCodeListData.setSearchData(this.populateActivityCodeSearchCriteria(frmActivityDetails.getString("claimSeqID"),(DynaActionForm)form,request));
			activityCodeListData.modifySearchData("search");				
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
/**
 * this method will add search criteria fields and values to the arraylist and will return it
 * @param frmPreAuthList formbean which contains the search fields
 * @param request HttpServletRequest
 * @return ArrayList contains search parameters
 */
private ArrayList<Object> populateActivityCodeSearchCriteria(String claimSeqID,DynaActionForm frmActivitiesList,HttpServletRequest request)
{
	//build the column names along with their values to be searched
	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivitiesList.getString("sActivityCode")));
	alSearchParams.add((String)frmActivitiesList.getString("sActivityCodeDesc"));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivitiesList.getString("sSearchType")));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivitiesList.getString("sAuthType")));
	alSearchParams.add(claimSeqID);
	alSearchParams.add(TTKCommon.getUserSeqId(request));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivitiesList.getString("sInternalCode")));
	return alSearchParams;
}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)




public ActionForward doSelectActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
	setLinks(request);
	log.debug("Inside ClaimsAction doSelectActivityCode ");

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
		log.debug("Inside ClaimsAction doActivityCodeForward");
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
		log.debug("Inside ClaimsAction doActivityCodeBackward");
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

public ActionForward closeActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside ClaimsAction closeActivityCode ");
((DynaActionForm)form).initialize(mapping);
	return mapping.findForward("ActivityDetails");
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimDetail));
}//end of catch(Exception exp)

}//end of closeActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
			log.debug("Inside ClaimAction doClinicianForward");
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
			log.debug("Inside ClaimAction doClinicianBackward");
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
	public ActionForward diagnosisCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																	HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ClaimAction diagnosisCodeSearch");
			setLinks(request);
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			HttpSession session=request.getSession();
			TableData diagnosisCodeListData = null;
			if(session.getAttribute("diagnosisCodeListData") != null)
			{
				diagnosisCodeListData = (TableData)session.getAttribute("diagnosisCodeListData");
			}//end of if((request.getSession()).getAttribute("icdListData") != null)
			else
			{
				diagnosisCodeListData = new TableData();
			}//end of else
			
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));		
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(strPageID.equals(""))
				{
					diagnosisCodeListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					diagnosisCodeListData.modifySearchData("sort");//modify the search data                    
				}///end of if(!strPageID.equals(""))
				else
				{
				log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
				diagnosisCodeListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return this.getForward(strDiagnosisCodeList, mapping, request);
				}//end of else
	          }//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				diagnosisCodeListData.createTableInfo("DiagnosisCodeListTable",null);
				diagnosisCodeListData.setSearchData(this.populateDiagnosisCodeSearchCriteria((DynaActionForm)form,request));
				diagnosisCodeListData.modifySearchData("search");				
			}//end of else
			
			ArrayList alDiagnosisCodeList=null;
			alDiagnosisCodeList= preAuthObject.getDiagnosisCodeList(diagnosisCodeListData.getSearchData());
			diagnosisCodeListData.setData(alDiagnosisCodeList, "search");
			//set the table data object to session
			session.setAttribute("diagnosisCodeListData",diagnosisCodeListData);
			return this.getForward(strDiagnosisCodeList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strDiagnosisCodeList));
		}//end of catch(Exception exp)
	}//end of diagnosisCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * this method will add search criteria fields and values to the arraylist and will return it
	 * @param frmPreAuthList formbean which contains the search fields
	 * @param request HttpServletRequest
	 * @return ArrayList contains search parameters
	 */
	private ArrayList<Object> populateDiagnosisCodeSearchCriteria(DynaActionForm frmDiagnosisList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmDiagnosisList.getString("sIcdCode")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmDiagnosisList.getString("sAilmentDescription")));
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		return alSearchParams;
	}//end of populateDiagnosisCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

	public ActionForward doSelectDiagnosisCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside ClaimAction doSelectDiagnosisCode ");
	HttpSession session=request.getSession();
	PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	TableData diagnosisCodeListData = (TableData)session.getAttribute("diagnosisCodeListData");

	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	{
		DiagnosisDetailsVO diagnosisDetailsVO=(DiagnosisDetailsVO)diagnosisCodeListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));

		DynaActionForm frmClaimGeneral=(DynaActionForm)session.getAttribute("frmClaimGeneral");
		String claimSeqID=frmClaimGeneral.getString("claimSeqID");
		 
		DiagnosisDetailsVO diagnosisDetailsVO2=preAuthObject.getIcdCodeDetails(diagnosisDetailsVO.getIcdCode(),new Long(claimSeqID),"CLM");
		String primary=diagnosisDetailsVO2.getPrimaryAilment();
		if(primary==null||"".equals(primary)||"YES".equals(primary))frmClaimGeneral.set("primaryAilment","");
		else frmClaimGeneral.set("primaryAilment","Y");
		frmClaimGeneral.set("icdCode",diagnosisDetailsVO.getIcdCode());
		frmClaimGeneral.set("icdCodeSeqId",diagnosisDetailsVO.getIcdCodeSeqId().toString());
		frmClaimGeneral.set("ailmentDescription",diagnosisDetailsVO.getAilmentDescription());
		request.setAttribute("focusObj", request.getParameter("focusObj"));
		session.setAttribute("frmClaimGeneral",frmClaimGeneral);
	}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
	return this.getForward(strClaimDetail, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strDiagnosisCodeList));
	}//end of catch(Exception exp)
	}//end of doSelectDiagnosisCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
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
	public ActionForward doDiagnosisCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ClaimAction doDiagnosisCodeForward");
			setLinks(request);
			TableData diagnosisCodeListData = (TableData)request.getSession().getAttribute("diagnosisCodeListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			diagnosisCodeListData.modifySearchData(strForward);//modify the search data
			ArrayList alDiagnosisList = preAuthObject.getDiagnosisCodeList(diagnosisCodeListData.getSearchData());
			diagnosisCodeListData.setData(alDiagnosisList, strForward);//set the table data
			request.getSession().setAttribute("diagnosisCodeListData",diagnosisCodeListData);   //set the table data object to session
			return this.getForward(strDiagnosisCodeList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strDiagnosisCodeList));
		}//end of catch(Exception exp)
	}//end of doDiagnosisCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
	public ActionForward doDiagnosisCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ClaimAction doDiagnosisCodeBackward");
			setLinks(request);
			TableData diagnosisCodeListData = (TableData)request.getSession().getAttribute("diagnosisCodeListData");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			diagnosisCodeListData.modifySearchData(strBackward);//modify the search data
			ArrayList alDiagnosisList = preAuthObject.getDiagnosisCodeList(diagnosisCodeListData.getSearchData());
			diagnosisCodeListData.setData(alDiagnosisList, strBackward);//set the table data
			request.getSession().setAttribute("diagnosisCodeListData",diagnosisCodeListData);   //set the table data object to session
			return this.getForward(strDiagnosisCodeList, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strDiagnosisCodeList));
		}//end of catch(Exception exp)
	}//end of doDiagnosisCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



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
			log.debug("Inside ClaimAction doClose");
			setLinks(request);
			request.setAttribute("focusObj", request.getParameter("focusObj"));
			String strActiveSubTab=TTKCommon.getActiveTab(request);
			
			
			
			if(strActiveSubTab.equals("Claim Upload")){
				HttpSession session=request.getSession();
				DynaActionForm frmPreAuthList=(DynaActionForm)session.getAttribute("frmPreAuthList");
				
				frmPreAuthList.set("sProviderId", "");
				frmPreAuthList.set("sProviderName", "");
				
				return mapping.findForward("ClaimUploadProvider");
			}
			else	
			return this.getForward(strClaimDetail, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimDetail));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * Returns the PreAuthManager session object for invoking methods on it.
     * @return PreAuthManager session object which can be used for method invokation
     * @exception throws TTKException
     */
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
    		throw new TTKException(exp, strClaimSearchError);
    	}//end of catch
    	return claimManager;
    }//end getClaimManagerObject()
    
  //Ramana
	
    public ActionForward memberSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   			HttpServletResponse response) throws Exception{
           try{
           log.debug("Inside ClaimAction memberSearch");
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

  //Ramana
	
  	public ActionForward doSelectMemberId(ActionMapping mapping,ActionForm form,HttpServletRequest request,
  			HttpServletResponse response) throws Exception{
  	try{
  	setLinks(request);
  	log.debug("Inside ClaimGenealAction doSelectMemberId ");
  	HttpSession session=request.getSession();
  	PreAuthManager preAuthObject=this.getPreAuthManagerObject();
  	TableData memberListData = (TableData)session.getAttribute("memberListData");

  	if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
  	{
  		
  		MemberDetailVO MemberDetailVO=(MemberDetailVO)memberListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));
  		int[] count = preAuthObject.getPreAuthAndClmCount(MemberDetailVO.getMemberSeqID());
  		
  	DynaActionForm frmClaimGeneral=(DynaActionForm)session.getAttribute("frmClaimGeneral");

  	frmClaimGeneral.set("memberSeqID", MemberDetailVO.getMemberSeqID().toString());
  	frmClaimGeneral.set("memberId", MemberDetailVO.getMemberId());
  	frmClaimGeneral.set("patientName", MemberDetailVO.getPatientName());
  	frmClaimGeneral.set("memberAge",MemberDetailVO.getMemberAge().toString());
  	frmClaimGeneral.set("emirateId", MemberDetailVO.getEmirateId());
  	frmClaimGeneral.set("payerName", MemberDetailVO.getPayerName());
  	frmClaimGeneral.set("payerId", MemberDetailVO.getPayerId());
  	frmClaimGeneral.set("insSeqId",MemberDetailVO.getInsSeqId().toString());
  	frmClaimGeneral.set("policySeqId", MemberDetailVO.getPolicySeqId().toString());
  	frmClaimGeneral.set("patientGender", MemberDetailVO.getPatientGender());
  	frmClaimGeneral.set("policyNumber", MemberDetailVO.getPolicyNumber());
  	frmClaimGeneral.set("corporateName",MemberDetailVO.getCorporateName());
  	frmClaimGeneral.set("policyStartDate", MemberDetailVO.getPolicyStartDate());
  	frmClaimGeneral.set("policyEndDate", MemberDetailVO.getPolicyEndDate());
  	frmClaimGeneral.set("nationality", MemberDetailVO.getNationality());
  	frmClaimGeneral.set("sumInsured",MemberDetailVO.getSumInsured().toString());
  	frmClaimGeneral.set("availableSumInsured",MemberDetailVO.getAvailableSumInsured().toString());
  	frmClaimGeneral.set("vipYorN",MemberDetailVO.getVipYorN());
  	frmClaimGeneral.set("relationShip",MemberDetailVO.getRelationShip());
  	frmClaimGeneral.set("relationFlag",MemberDetailVO.getRelationFlag());
  	frmClaimGeneral.set("preMemInceptionDt",MemberDetailVO.getPreMemInceptionDt());
  	frmClaimGeneral.set("productName",MemberDetailVO.getProductName());
  	frmClaimGeneral.set("eligibleNetworks",MemberDetailVO.getEligibleNetworks());
  	frmClaimGeneral.set("payerAuthority",MemberDetailVO.getProvAuthority());//accountNumber
  	frmClaimGeneral.set("paymentMadeFor",MemberDetailVO.getPaymentMadeFor());
  	frmClaimGeneral.set("accountNumber",MemberDetailVO.getAccountNumber());
  	frmClaimGeneral.set("bankName",MemberDetailVO.getBankName());
  	frmClaimGeneral.set("preAuthCount",(""+count[0]).trim());
  	frmClaimGeneral.set("clmCount",(""+count[1]).trim());
  	//start - Deleting preAuth details of previous alkoot Id.
  	frmClaimGeneral.set("authNum","");
  	frmClaimGeneral.set("preAuthSeqID","");
  	frmClaimGeneral.set("preAuthApprAmt","");
  	frmClaimGeneral.set("preAuthApprAmtCurrency","");
	frmClaimGeneral.set("preAuthReceivedDateAsString","");
	frmClaimGeneral.set("memberSpecificRemarks", MemberDetailVO.getMemberSpecificRemarks());
	//end - Deleting preAuth details of previous alkoot Id.
  	session.setAttribute("frmClaimGeneral",frmClaimGeneral);
  		
  		
  	}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
  	return this.getForward(strClaimDetail, mapping, request);
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

  //Ramana
	
  	public ActionForward doMemberForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
  			HttpServletResponse response) throws Exception{
  try{
             log.debug("Inside ClaimAction doMeberForward");
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
    
    //Ramana
  	 public ActionForward doMemberBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		        HttpServletResponse response) throws Exception{
  try{
       log.debug("Inside ClaimAction doMemberBackward");
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
			throw new TTKException(exp, strClaimSearchError);
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
    			cacheObject.setCacheId(this.prepareWebBoardId(preAuthVO));
    			cacheObject.setCacheDesc(preAuthVO.getInvoiceNo());
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
    private void addToWebBoard(PreAuthVO preAuthVO, HttpServletRequest request)throws TTKException
    {
    	Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
    	ArrayList<Object> alCacheObject = new ArrayList<Object>();
    	CacheObject cacheObject = new CacheObject();
    	cacheObject.setCacheId(this.prepareWebBoardId(preAuthVO)); //set the cacheID
    	cacheObject.setCacheDesc(preAuthVO.getInvoiceNo());
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
    private String prepareWebBoardId(PreAuthVO preAuthVO)throws TTKException
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
     * This method supresess the Assign Icon Column by checking against the Assign and Special Permissions
     *  from the User's Role.
     * @param tableData TableData object which contains the Grid Information
     * @param frmClaimList formbean which contains the search fields
     * @param request HttpServletRequest object
     * @throws TTKException if any run time exception occures
     */
    private void setColumnVisiblity(TableData tableData,DynaActionForm frmClaimList,HttpServletRequest request)
    												throws TTKException
    {
    	String strAssignTo=frmClaimList.getString("sAssignedTo");
    	boolean blnVisibility=false;
    	//For Self Check the Assign Permission
    	if(strAssignTo.equals("SLF") && TTKCommon.isAuthorized(request,"Assign"))
    	{
    		blnVisibility=true;
    	}//end of if(strAssignTo.equals("SLF") && TTKCommon.isAuthorized(request,"Assign"))
    	else         //Check for the special Permission to show ICON for Others and Unassigned Claim
    	{
    		if(TTKCommon.isAuthorized(request,"AssignAll"))
    		{
    			blnVisibility=true;
    		}//end of if(TTKCommon.isAuthorized(request,"AssignAll"))
    	}//end of else
    	((Column)((ArrayList)tableData.getTitle()).get(ASSIGN_ICON)).setVisibility(blnVisibility);
    }//end of setColumnVisiblity(TableData tableData,DynaActionForm frmPreAuthList,HttpServletRequest request)
    
       
    
    public ActionForward  doDefaultUpload(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
	{
		try
		{   
			String claimSeqID	=	(String)request.getParameter("claimSeqID");
			log.debug("Inside the doDefault method of ClaimAction");
			request.getSession().setAttribute("preAuth_seq_id_docs_uploads", claimSeqID);
			setLinks(request);
			String strTable = "";
			StringBuffer sbfCaption= new StringBuffer();	
			String strEmpanelNumber=null;
			String strPreauthNumber=null;
			DynaActionForm frmDocsUpload= null;	
			if( "Y".equals( (String)request.getParameter("Entry") ) ) {
				frmDocsUpload = (DynaActionForm)form;
				frmDocsUpload.initialize(mapping);
			}//end of if(request.getParameter("Entry").equals("Y"))
			else {
				frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmClaimGeneral");
				request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
				sbfCaption.append("[").append(frmDocsUpload.get("claimNo")).append("]");	
				//strPreauthNumber=(String)frmClaimGeneral.get("preAuthNo");
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
			alDocsUploadList = preAuthObject.getclaimsDocsUploadsList(claimSeqID);
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
			//ConfigurationManager servConfigurationManager=this.getConfManager();
			StringBuffer sbfCaption= new StringBuffer();
			sbfCaption=sbfCaption.append("- [ ").append(TTKCommon.getWebBoardDesc(request)).append(" ]");
			//..............File Upload from Local System.................
					
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
			int fileSize=4*1024*1024;
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
			Long ClaimSeqID	=	preAuthDetailVO.getClaimSeqID();			
			//Long hospSeqId	=	userSecurityProfile.getHospSeqId();
			formFile = (FormFile)frmDocsUpload.get("file");
			String fileDesc	=	(String)frmDocsUpload.get("description");
			String sourceid	=	(String)frmDocsUpload.get("source_id");				
			//pattern = Pattern.compile( "[^a-zA-Z0-9\\.\\s*]" );
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
					//COMMENTING THESE LINES FOR ALLOWING TO UPLOAD ALL KINDS OF FILES
					  
						
							outputStream = new FileOutputStream(new File(finalPath));
							outputStream.write(formFile.getFileData());

							//alFileAUploadList.add(fileName);//3 This just Adds file name
							alFileAUploadList.add(finalPath);//3 This code adds Total path of the file
							//alFileAUploadList.add(webconfigInsCompInfoVO.getRemarks());//4				            
							iUpdate=preAuthObject.saveDocUploads(preAuthDetailVO,alFileAUploadList,userSeqId,preAuth_Seq_ID,origFileName,inputStream,formFileSize);
							if(iUpdate>0)
							{
								if(frmDocsUpload.get("mouDocSeqID")!=null)
								{
									//set the appropriate message
									request.setAttribute("updated","message.savedSuccessfully");
								}//end of if(frmClaimGeneral.get("configLinkSeqID")!=null)
								else
								{
									//set the appropriate message
									request.setAttribute("updated","message.addedSuccessfully");
								}//end else
								frmDocsUpload.initialize(mapping);
								alLinkDetailsList=preAuthObject.getclaimsDocsUploadsList(preAuth_seq_id_docs_uploads);
								tableDataDocsUpload.setData(alLinkDetailsList);
								frmDocsUpload.set("caption",sbfCaption.toString());
								request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
								request.setAttribute("frmDocsUpload",frmDocsUpload);
							}// end of if(iUpdate>0)
							
							frmDocsUpload = (DynaActionForm)FormUtils.setFormValues("frmDocsUpload",preAuthDetailVO,this,mapping,request);
						
						
					}//end of if(strFileExt.equalsIgnoreCase("html") || strFileExt.equalsIgnoreCase("mhtml")||strFileExt.equalsIgnoreCase("msg") || strFileExt.equalsIgnoreCase("rar")|| strFileExt.equalsIgnoreCase("zip") || strFileExt.equalsIgnoreCase("pdf") || strFileExt.equalsIgnoreCase("doc") || strFileExt.equalsIgnoreCase("docx") || strFileExt.equalsIgnoreCase("txt") || strFileExt.equalsIgnoreCase("odt") || strFileExt.equalsIgnoreCase("jrxml") || strFileExt.equalsIgnoreCase("xls") || strFileExt.equalsIgnoreCase("xlsx") )
					else{
						strNotify="selected  file should be any of these extensions (.pdf,.doc,.docx,.xls,.xlsx)";
						frmDocsUpload = (DynaActionForm)FormUtils.setFormValues("frmDocsUpload",preAuthDetailVO,this,mapping,request);
					}//end ofelse(strFileExt.equalsIgnoreCase("pdf"))
							
							
				}//end of if(formFile.getFileSize()<=fileSize)
				else{
					strNotify="selected file size  Shold be less than or equal to 4 MB";
				}//end of else part if(formFile.getFileSize()<=fileSize)
			/*}//end of if(!matcher.find())
			else{
				strNotify="selected "+formFile.getFileName()+" file Should not have Special Characters like }!@#$%^&amp;*(])[{+";
			}	*/		
			//frmClaimGeneral.set("updated",updated);
			request.getSession().setAttribute("frmUpload", frmDocsUpload);
			request.setAttribute("notify",strNotify);
		
			//frmClaimGeneral.set("caption",strCaption);
			//..............File Upload from Local System Ends...........
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
			return mapping.findForward(strClaimsUploadClose);
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
			//DynaActionForm frmClaimGeneral=null;
			String preAuthSeqID	=	(String)request.getSession().getAttribute("preAuth_seq_id_docs_uploads");
			frmDocsUpload=(DynaActionForm) request.getSession().getAttribute("frmDocsUpload");
			alAssociatedCertificatesList = preAuthObject.getclaimsDocsUploadsList(preAuthSeqID);
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
            log.debug("Inside the doBackward method of doStatusChange");
           
            return this.getForward(strClaimslist, mapping, request);   //finally return to the grid screen
           }//end of try
         catch(TTKException expTTK)
        {
          return this.processExceptions(request, mapping, expTTK);
            }//end of catch(TTKException expTTK)
         catch(Exception exp)
         {
          return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
         }//end of catch(Exception exp)
        }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	 public ActionForward doPriorityChanged(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	        try{
	            log.debug("Inside the doBackward method of doStatusChange");
	           
	            return this.getForward(strClaimslist, mapping, request);   //finally return to the grid screen
	           }//end of try
	         catch(TTKException expTTK)
	        {
	          return this.processExceptions(request, mapping, expTTK);
	            }//end of catch(TTKException expTTK)
	         catch(Exception exp)
	         {
	          return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
	         }//end of catch(Exception exp)
	        }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
}//end of ClaimsAction