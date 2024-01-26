/**
 * @ (#) AssignToAction.java Aug 10, 2006
 * Project      : TTK HealthCare Services
 * File         : AssignToAction.java
 * Author       : Krupa J
 * Company      : Span Systems Corporation
 * Date Created : Aug 10, 2006
 *
 * @author      :  Krupa J
 * Modified by  :
 * Modified date:
 * Reason       :
 */

package com.ttk.action.preauth;

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
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimManager;
import com.ttk.business.coding.CodingManager;
import com.ttk.business.preauth.PreAuthDashboardManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.PreAuthAssignmentVO;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.preauth.UserAssignVO;

import formdef.plugin.util.FormUtils;

/**
 * This class is reusable for assiging the user to claims,pre-auth in Claims and Pre-Auth flow.
 */

public class PreauthDashBordAssignToAction extends TTKAction{

	private static Logger log = Logger.getLogger(PreauthDashBordAssignToAction.class);
    private static final String strAssignDashPreAuth ="assigntodashpreauth";
    private static final String strDashbordpreList ="dashbordpreList";
	/**
	 * This method is used to Assign the users.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doAssign(ActionMapping mapping,ActionForm form,HttpServletRequest request,
															HttpServletResponse response) throws Exception
	{
		try
		{
			setLinks(request);
			log.debug("Inside AssignToAction doAssign");
			String strModeValue = "";
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			TableData tableData =TTKCommon.getTableData(request);
			ArrayList <Object>alAssignUserList=new ArrayList<Object>();
			//ArrayList alUserList=new ArrayList();
			ArrayList alUserList=null;
			String strAssignTo=null;
			
			if(strLink.equals("Pre-Authorization")||strSubLink.equals("Dashbord"))
			{
				strModeValue="PAT";
			}
			//clear the dynaform if visiting from left links for the first time
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmDashbordAssign=(DynaActionForm)form;
			PreAuthAssignmentVO preAuthAssignmentVO=null;
			PreAuthVO preAuthVO=null;
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt((String)(frmDashbordAssign).get("rownum")));
				if(preAuthVO.getAssignUserSeqID()!=null)
				{
					preAuthAssignmentVO=preAuthObject.getAssignTo(preAuthVO.getAssignUserSeqID(),
																		TTKCommon.getUserSeqId(request),strModeValue);
				}//end of if(preAuthVO.getAssignUserSeqID()!=null)
				else
				{
					frmDashbordAssign.initialize(mapping);
					preAuthAssignmentVO=new PreAuthAssignmentVO();
					preAuthAssignmentVO.setSelectedPreAuthNos(preAuthVO.getPreAuthNo());
					preAuthAssignmentVO.setPreAuthSeqID(preAuthVO.getPreAuthSeqID());
				//	preAuthAssignmentVO.setOfficeSeqID(preAuthVO.getOfficeSeqID());
					
				//	frmDashbordAssign.set("selectedPreAuthNos", preAuthVO.getPreAuthNo());
				//	frmDashbordAssign.set("preAuthSeqID", preAuthVO.getPreAuthSeqID());
				}//end of else
				
				if(strLink.equals("Pre-Authorization") && strSubLink.equals("Dashbord")){
					alAssignUserList.add(preAuthAssignmentVO.getPreAuthSeqID());
					strAssignTo=strAssignDashPreAuth;
				}
			//	alAssignUserList.add(1l);
			//	alUserList=preAuthObject.getAssignUserList(alAssignUserList,strModeValue);
				frmDashbordAssign=(DynaActionForm)FormUtils.setFormValues("frmDashbordAssign", preAuthAssignmentVO, this,mapping, request);
				request.getSession().setAttribute("alUserList",alUserList);
				request.getSession().setAttribute("frmDashbordAssign",frmDashbordAssign);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			
			
			Long  userSeqId = TTKCommon.getUserSeqId(request);
			ArrayList<UserAssignVO> userList = preAuthObject.getUserDetails(userSeqId);
			request.getSession().setAttribute("userList", userList);
			
			return mapping.findForward(strAssignTo);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp, strAssignDashPreAuth));
		}//end of catch(Exception exp)
	}// end of doAssign(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
	 * This method is used to change the Assign Users
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doChangeUsers(ActionMapping mapping,ActionForm form,HttpServletRequest request,
															HttpServletResponse response) throws Exception
	{
		try
		{
			setLinks(request);
			log.debug("Inside AssignToAction doChangeUsers");
			String strModeValue = "";
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			ArrayList <Object>alAssignUserList=new ArrayList<Object>();
			ArrayList alUserList=new ArrayList();
			String strAssignTo=null;
			
			if(strLink.equals("Pre-Authorization") && strSubLink.equals("Dashbord"))
			{
				strModeValue="PAT";
			}
			//clear the dynaform if visiting from left links for the first time
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmAssign = (DynaActionForm)form;
			PreAuthAssignmentVO preAuthAssignmentVO=(PreAuthAssignmentVO)FormUtils.getFormValues(frmAssign,this,
																								mapping, request);
			if(preAuthAssignmentVO.getOfficeSeqID()!=null)
			{
			 if(strLink.equals("Pre-Authorization")&& strSubLink.equals("Dashbord"))
				{
					alAssignUserList.add(preAuthAssignmentVO.getPreAuthSeqID());	
					strAssignTo=strAssignDashPreAuth;
				}
			//	alAssignUserList.add(preAuthAssignmentVO.getPolicySeqID());
				alAssignUserList.add(preAuthAssignmentVO.getOfficeSeqID());
				alUserList=preAuthObject.getAssignUserList(alAssignUserList,strModeValue);
			}//end of if(preAuthAssignmentVO.getOfficeSeqID()!=null)
			else{
				alAssignUserList.add(preAuthAssignmentVO.getPreAuthSeqID());	
				strAssignTo=strAssignDashPreAuth;
				alAssignUserList.add(preAuthAssignmentVO.getOfficeSeqID());
				alUserList=preAuthObject.getAssignUserList(alAssignUserList,strModeValue);
			}
			request.getSession().setAttribute("alUserList",alUserList);
			frmAssign.set("frmChanged","changed");
			request.setAttribute("frmAssign",frmAssign);
			return  mapping.findForward(strAssignTo);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp, strAssignDashPreAuth));
		}//end of catch(Exception exp)
	}// end of doChangeUsers(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
	/*public ActionForward doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
														HttpServletResponse response) throws Exception
	{
		try
		{
			setLinks(request);
			log.debug("Inside AssignToAction doSave");
			String strModeValue = "";
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			ArrayList <Object>alAssignUserList=new ArrayList<Object>();
			ArrayList alUserList=new ArrayList();
			String strAssignTo=null;
			
		if(strLink.equals("Pre-Authorization")||strSubLink.equals("Dashbord"))
			{
				strModeValue="PAT";
			}
			//clear the dynaform if visiting from left links for the first time
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmAssign=(DynaActionForm)form;
			PreAuthAssignmentVO preAuthAssignmentVO=(PreAuthAssignmentVO)FormUtils.getFormValues(frmAssign,this,
																					mapping, request);
			preAuthAssignmentVO.setAssignUserSeqID(null);
			preAuthAssignmentVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// get the Logged in USER_SEQ_ID from session
			//if user is Super User set Flag to Y
			if(TTKCommon.isAuthorized(request,"AssignAll") || TTKCommon.isAuthorized(request,"Assign")){
				preAuthAssignmentVO.setSuperUserYN("Y");
			}//end of if(TTKCommon.isAuthorized(request,"AssignAll"))
			else{
				preAuthAssignmentVO.setSuperUserYN("N");
			}//end of else
			Long lngResult = preAuthObject.assignPreAuth(preAuthAssignmentVO,strModeValue);
			if(lngResult >0)
			{
				request.setAttribute("updated","message.saved");
				preAuthAssignmentVO=preAuthObject.getAssignTo(lngResult,preAuthAssignmentVO.getUpdatedBy(),strModeValue);
			}//end of if(lngResult >0)
			
			if(strLink.equals("Pre-Authorization") && strSubLink.equals("Dashbord")){
				alAssignUserList.add(preAuthAssignmentVO.getPreAuthSeqID());
				strAssignTo=strAssignDashPreAuth;
			}
		//	alAssignUserList.add(preAuthAssignmentVO.getPolicySeqID());
			alAssignUserList.add(preAuthAssignmentVO.getOfficeSeqID());
			alUserList=preAuthObject.getAssignUserList(alAssignUserList,strModeValue);
			request.getSession().setAttribute("alUserList",alUserList);
			frmAssign=(DynaActionForm)FormUtils.setFormValues("frmAssign", preAuthAssignmentVO, this, mapping, request);
			request.setAttribute("frmAssign",frmAssign);
			return  mapping.findForward(strAssignTo);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp, strAssignDashPreAuth));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
       */
	public ActionForward doSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Inside PreAuthDashboardAction doDisplayManaDashbord");
		String strAssignTo ="";
		try {
			DynaActionForm frmAssign=(DynaActionForm)form;
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			setLinks(request);
			Long  userSeqId = TTKCommon.getUserSeqId(request);
			PreAuthAssignmentVO preAuthAssignmentVO=(PreAuthAssignmentVO)FormUtils.getFormValues(frmAssign,this,
																					mapping, request);
			preAuthAssignmentVO.setUpdatedBy(userSeqId);
			PreAuthDashboardManager preAuthDashboardManagerobject= this.getPreAuthDashboardManagerObject();
			Long result =preAuthDashboardManagerobject.saveAssignedUser(preAuthAssignmentVO);
			System.out.println("result..."+result);		
			request.setAttribute("updated","message.saved");
			 
		}catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strAssignDashPreAuth));
		}
		return mapping.findForward(strAssignDashPreAuth);
	}
	/**
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping u     * This method is used to reload the screen when the reset button is pressed.
sed to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
	public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
														HttpServletResponse response) throws Exception
	{
		try
		{
			setLinks(request);
			log.debug("Inside AssignToAction doReset");
			String strModeValue = "";
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			ArrayList <Object>alAssignUserList=new ArrayList<Object>();
			ArrayList alUserList=new ArrayList();
			String strAssignTo=null;
			if(strLink.equals("Pre-Authorization") && strSubLink.equals("Dashbord")){
				strModeValue="PAT";
			}
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			DynaActionForm frmAssign = (DynaActionForm)form;
			PreAuthAssignmentVO preAuthAssignmentVO=null;
			if(frmAssign.get("assignUserSeqID")!=null && !frmAssign.get("assignUserSeqID").equals(""))
			{
				preAuthAssignmentVO=preAuthObject.getAssignTo(TTKCommon.getLong(((String)
									frmAssign.get("assignUserSeqID"))),TTKCommon.getUserSeqId(request),strModeValue);
			}//end of if(preAuthVO.getAssignUserSeqID()!=null)
			else
			{
				preAuthAssignmentVO=new PreAuthAssignmentVO();
				preAuthAssignmentVO.setSelectedPreAuthNos((String)frmAssign.getString("selectedPreAuthNos"));
				preAuthAssignmentVO.setPreAuthSeqID(TTKCommon.getLong((String)frmAssign.getString("preAuthSeqID")));
				preAuthAssignmentVO.setClaimSeqID(TTKCommon.getLong((String)frmAssign.getString("claimSeqID")));
				preAuthAssignmentVO.setPolicySeqID(TTKCommon.getLong((String)frmAssign.getString("policySeqID")));
				preAuthAssignmentVO.setOfficeSeqID(TTKCommon.getLong((String)frmAssign.getString("officeSeqID")));
			}//end of else
			
			if(strLink.equals("Pre-Authorization") && strSubLink.equals("Dashbord")){
				alAssignUserList.add(preAuthAssignmentVO.getPreAuthSeqID());
				strAssignTo=strAssignDashPreAuth;
			}
			alAssignUserList.add(preAuthAssignmentVO.getPolicySeqID());
			alAssignUserList.add(preAuthAssignmentVO.getOfficeSeqID());
			alUserList=preAuthObject.getAssignUserList(alAssignUserList,strModeValue);
			frmAssign=(DynaActionForm)FormUtils.setFormValues("frmAssign", preAuthAssignmentVO, this, mapping, request);
			request.getSession().setAttribute("alUserList",alUserList);
			request.setAttribute("frmAssign",frmAssign);
			return  mapping.findForward(strAssignTo);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp, strAssignDashPreAuth));
		}//end of catch(Exception exp)
	}// end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
															HttpServletResponse response) throws Exception
	{
		try
		{
			setLinks(request);
			log.debug("Inside AssignToAction doClose");
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			String strLink = TTKCommon.getActiveLink(request);
			String strSubLink = TTKCommon.getActiveSubLink(request);
			String strFwdMode = null;
			TableData tableData =TTKCommon.getTableData(request);
			//clear the dynaform if visiting from left links for the first time
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			if(tableData.getSearchData() != null && tableData.getSearchData().size() > 0)
			{
				ArrayList alGeneralList=null;
				if(strLink.equals("Pre-Authorization") && strSubLink.equals("Dashbord")){
					strFwdMode = strDashbordpreList;
					return mapping.findForward(strFwdMode);
				}
			//	tableData.setData(alGeneralList,"search");
			//	request.getSession().setAttribute("tableData",tableData);
			}//end of if(tableData.getSearchData() != null && tableData.getSearchData().size() > 0)
			else{
				strFwdMode = strDashbordpreList;
				return mapping.findForward(strFwdMode);
			}
			return this.getForward(strFwdMode, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp, strDashbordpreList));
		}//end of catch(Exception exp)
	}// end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
			throw new TTKException(exp, strAssignDashPreAuth);
		}//end of catch
		return preAuthManager;
	}//end getPreAuthManagerObject()

	private PreAuthDashboardManager getPreAuthDashboardManagerObject() throws TTKException
	{
		PreAuthDashboardManager preAuthDashboardManager = null;
		try
		{
			if(preAuthDashboardManager == null)
			{
				InitialContext ctx = new InitialContext();
				preAuthDashboardManager = (PreAuthDashboardManager) ctx.lookup("java:global/TTKServices/business.ejb3/PreAuthDashboardManagerBean!com.ttk.business.preauth.PreAuthDashboardManager");

			}//end of if(PreAuthDashboardManager == null)
		}//end of try
		catch(Exception exp)
		{
			
		}//end of catch
		return preAuthDashboardManager;
	}
}//end of AssignToAction