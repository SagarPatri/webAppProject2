/**
 * @ (#) PbmPreauthAction.java 2nd Feb 2006
 * Project      : TTK HealthCare Services
 * File         : PbmPreauthAction.java
 * Author       : Raghavendra T M
 * Company      : Span Systems Corporation
 * Date Created : 2nd Feb 2006
 *
 * @author       : Raghavendra T M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.onlineforms.pbmPharmacy;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.providerLogin.OnlinePbmProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

/**
 * This class is used for searching the List of Batch .
 * This class also provides option for Deletion, Addition and Updation of Batch.
 */
public class PbmPreauthAction extends TTKAction {
	
	private static Logger log = Logger.getLogger( PbmPreauthAction.class );
	
	//Modes
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	
	//Exception Message Identifier
	private static final String strPbmpreauthsearch="pbmpreauthsearch";
	private static final String strPbmPreauthList="pbmPreauthList";
	private static final String strPreAuthDetails="pbmPreauthDetails";
	
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
	public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doDefault method of PbmPreauthAction");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			//clear the dynaform if visting from left links for the first time
			//else get the dynaform data from session
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			
			if(request.getAttribute("KeepTableData")==null){
				//  
			tableData = new TableData();//create new table data object
			tableData.createTableInfo("PbmPreAuthListTable",new ArrayList());
			}
			//create the required grid table
			
			request.getSession().setAttribute("tableData",tableData);
			((DynaActionForm)form).initialize(mapping);//reset the form data
			DynaActionForm frmOnlinePreAuthLog= (DynaActionForm) form;
			request.getSession().setAttribute("frmOnlinePreAuthLog",frmOnlinePreAuthLog);//reset the form data
			
			return this.getForward(strPbmPreauthList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
		}//end of catch(Exception exp)
	}//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
	public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doSearch method of PbmPreauthAction");
			setLinks(request);
			OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
			DynaActionForm frmOnlinePreAuthLog =(DynaActionForm)form;
			HttpSession session=request.getSession();
			UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			Long hospSeqID=userSecurityProfile.getHospSeqId();
			UserSecurityProfile userSecurityProfile4 = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			Long hospSeqID4=userSecurityProfile.getHospSeqId();
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
					return mapping.findForward(strPbmPreauthList);
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				//create the required grid table
				tableData.createTableInfo("PbmPreAuthListTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				//tableData.setSortColumnName("PRE_AUTH_NUMBER");
				//this.setColumnVisiblity(tableData,(DynaActionForm)form,request);
				tableData.modifySearchData("search");
			}//end of else
			
			if("".equalsIgnoreCase(frmOnlinePreAuthLog.getString("status")) || "APR".equalsIgnoreCase(frmOnlinePreAuthLog.getString("status")) || "REJ".equalsIgnoreCase(frmOnlinePreAuthLog.getString("status")))
			{
				((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(true); 
			}
			else
			{
				((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false); 
			}
						
			ArrayList alPreauthList= providerManagerObj.getPbmPreAuthList(tableData.getSearchData(),TTKCommon.getUserSeqId(request), hospSeqID4); // Populating Data Which matches the search criteria
			tableData.setData(alPreauthList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strPbmPreauthList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
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
	public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doBackward method of PbmPreauthAction");
			setLinks(request);
			OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
			/*ArrayList alBatchList = null;*/
			TableData tableData = TTKCommon.getTableData(request);
			HttpSession session=request.getSession();
			UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			Long hospSeqID=userSecurityProfile.getHospSeqId();
			UserSecurityProfile userSecurityProfile1 = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			Long hospSeqID1=userSecurityProfile.getHospSeqId();
			tableData.modifySearchData(strBackward);//modify the search data
			//fetch the data from the data access layer and set the data to table object
			ArrayList alPreauthList= providerManagerObj.getPbmPreAuthList(tableData.getSearchData(),TTKCommon.getUserSeqId(request),hospSeqID1); // Populating Data Which matches the search criteria
			tableData.setData(alPreauthList,strBackward);
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strPbmPreauthList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
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
	public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doForward method of PbmPreauthAction");
			setLinks(request);
			OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
			TableData tableData = TTKCommon.getTableData(request);
			tableData.modifySearchData(strForward);//modify the search data
			//fetch the data from the data access layer and set the data to table object
			HttpSession session=request.getSession();
			UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			Long hospSeqID=userSecurityProfile.getHospSeqId();
			UserSecurityProfile userSecurityProfile2 = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			Long hospSeqID2=userSecurityProfile.getHospSeqId();
			ArrayList alPreauthList= providerManagerObj.getPbmPreAuthList(tableData.getSearchData(),TTKCommon.getUserSeqId(request),hospSeqID2); // Populating Data Which matches the search criteria
			tableData.setData(alPreauthList,strForward);
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strPbmPreauthList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
		}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
    try{
            log.debug("Inside PbmPreauthAction doViewPreauth");
          setLinks(request);
         TableData tableData = TTKCommon.getTableData(request);
          DynaActionForm frmPreAuthList=(DynaActionForm)form; 
       if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
      {
         PbmPreAuthVO pbmPreAuthVO=(PbmPreAuthVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
         request.getSession().setAttribute("preAuthStatus", pbmPreAuthVO.getStatus());
         request.getSession().setAttribute("claimStatus", null);
         	request.getSession().setAttribute("preAuthSeqID", pbmPreAuthVO.getPreAuthSeqID());
             request.setAttribute("pbmPreauthSeqID", pbmPreAuthVO.getPreAuthSeqID());
             request.getSession().setAttribute("isLogSearch", "true");
           }//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
       
       String appealBtn = TTKCommon.checkNull(request.getParameter("appealBtn"));
       String refview = TTKCommon.checkNull(request.getParameter("refview"));
       request.setAttribute("appealBtn",appealBtn);
       request.setAttribute("refview",refview);
         return mapping.findForward(strPreAuthDetails);
       }//end of try
       catch(TTKException expTTK)
     {
         return this.processExceptions(request, mapping, expTTK);
       }//end of catch(TTKException expTTK)
    catch(Exception exp)
     {
          return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
      }//end of catch(Exception exp)
    }//end of doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
  //HttpServletResponse response)

	
	
	
	/**
	 * Return the ArrayList populated with Search criteria elements
	 * @param DynaActionForm
	 * @param HttpServletRequest
	 * @return ArrayList
	 */
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmOnlinePreAuthLog,HttpServletRequest request)
	{
		ArrayList<Object> alSearchParams =new ArrayList<Object>();
		
		alSearchParams.add(frmOnlinePreAuthLog.getString("authorizationId"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("fromDate"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("toDate"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("patientName"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("clinicianName"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("memberId"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("qatarId"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("status"));	
		alSearchParams.add(frmOnlinePreAuthLog.getString("preApprovalNo"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("eventRefNo"));
		alSearchParams.add(frmOnlinePreAuthLog.getString("inProgressStatus"));
		return alSearchParams;
	}//end of populateSearchCriteria(DynaActionForm frmBatchList,HttpServletRequest request)
	

	   OnlinePbmProviderManager  onlinePbmProviderManager= null;
	    private OnlinePbmProviderManager getPbmManagerObject() throws TTKException
	    {
	    	
	        try
	        {
	            if(onlinePbmProviderManager == null)
	            {
	                InitialContext ctx = new InitialContext();
	                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
	                onlinePbmProviderManager = (OnlinePbmProviderManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlinePbmProviderManagerBean!com.ttk.business.onlineforms.providerLogin.OnlinePbmProviderManager");
	            }//end if
	        }//end of try
	        catch(Exception exp)
	        {
	            throw new TTKException(exp, "onlinepbmproviderinfo");
	        }//end of catch
	        return onlinePbmProviderManager;
	    }//end of getHospitalManagerObject()

	    
	    public ActionForward doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   		HttpServletResponse response) throws Exception{ 
		   	try{
		   		setOnlineLinks(request);
		   		log.debug("Inside the doStatusChange method of ProviderAction");
				return this.getForward(strPbmPreauthList, mapping, request);	
		   		}//end of try
		   	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,"onlinepbmproviderinfo"));
				}//end of catch(Exception exp)
		   }
}//end of PbmPreauthAction


