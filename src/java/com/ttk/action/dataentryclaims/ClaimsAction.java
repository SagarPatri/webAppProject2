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

package com.ttk.action.dataentryclaims;

import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.dataentryclaims.ParallelClaimManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

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
	private static final  String strClaimslist="dataentryclaimslist";
	private static final  String strClaimDetail="dataentryclaimdetail";
	
	//added for CR KOC-Decoupling
	private static final String strClaimMedicalDetail="dataentryclaimmedicaldetail";
	private static final String strClaimBillDetail="dataentrybilldetail";

    private static final int ASSIGN_ICON=8;//Koc Decoupling

    //Exception Message Identifier
    private static final String strClaimSearchError="dataentryhospitalsearch";

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
    		ParallelClaimManager claimManagerObject=this.getClaimManagerObject();
    		//get the tbale data from session if exists
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
    		ParallelClaimManager claimManagerObject=this.getClaimManagerObject();
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
    		ParallelClaimManager claimManagerObject=this.getClaimManagerObject();
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
				PreAuthVO preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt((String)
																		(frmClaimsList).get("rownum")));
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

    
    //added for CR KOC-Decoupling
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
    public ActionForward doMedicalView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    												HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doMedicalView method of ClaimsAction");
    		setLinks(request);
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		DynaActionForm frmClaimsList=(DynaActionForm)form;
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				PreAuthVO preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt((String)
																		(frmClaimsList).get("rownum")));
				this.addToWebBoard(preAuthVO, request);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			return mapping.findForward(strClaimMedicalDetail);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
		}//end of catch(Exception exp)
    }//end of doMedicalView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   
    //ended
    
    //added for CR KOC-Decoupling
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
    public ActionForward doBillsView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    												HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doBillsView method of ClaimsAction");
    		setLinks(request);
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		DynaActionForm frmClaimsList=(DynaActionForm)form;
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				PreAuthVO preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt((String)
																		(frmClaimsList).get("rownum")));
				this.addToWebBoard(preAuthVO, request);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			return mapping.findForward(strClaimBillDetail);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimSearchError));
		}//end of catch(Exception exp)
    }//end of doMedicalView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    //ended
    
    
    
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
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sInwardNumber")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sClaimNumber")));
    	alSearchParams.add((String)frmClaimsList.getString("sClaimType"));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sClaimSettelmentNumber")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sClaimFileNumber")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sClaimantName")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sEnrollmentId")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sPolicyNumber")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sCorporateName")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sEmployeeNumber")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sEmployeeName")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sPolicyHolderName")));
    	alSearchParams.add((String)frmClaimsList.getString("sAssignedTo"));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sSpecifyName")));
    	alSearchParams.add((String)frmClaimsList.getString("sStatus"));
    	alSearchParams.add((String)frmClaimsList.getString("sMode"));
    	alSearchParams.add((String)frmClaimsList.getString("sTtkBranch"));
    	alSearchParams.add((String)frmClaimsList.getString("sWorkFlow"));
    	alSearchParams.add(TTKCommon.getUserSeqId(request));
    	alSearchParams.add(TTKCommon.getUserGroupList(request));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sSchemeName")));
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sCertificateNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaimsList.getString("sInsurerAppStatus")));//bajaj
    	return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmClaimsList,HttpServletRequest request)

    /**
     * Returns the PreAuthManager session object for invoking methods on it.
     * @return PreAuthManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private ParallelClaimManager getClaimManagerObject() throws TTKException
    {
    	ParallelClaimManager claimManager = null;
    	try
    	{
    		if(claimManager == null)
    		{
    			InitialContext ctx = new InitialContext();
    			claimManager = (ParallelClaimManager) ctx.lookup("java:global/TTKServices/business.ejb3/ParallelClaimManagerBean!com.ttk.business.dataentryclaims.ParallelClaimManager");
    		}//end if
    	}//end of try
    	catch(Exception exp)
    	{
    		throw new TTKException(exp, strClaimSearchError);
    	}//end of catch
    	return claimManager;
    }//end getClaimManagerObject()

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
    private void addToWebBoard(PreAuthVO preAuthVO, HttpServletRequest request)throws TTKException
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
}//end of ClaimsAction