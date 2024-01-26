/**
 * @ (#) MemberAction.java Feb 2nd, 2006
 * Project 		: TTK HealthCare Services
 * File 		: MemberAction.java
 * Author 		: Krishna K H
 * Company 		: Span Systems Corporation
 * Date Created : Feb 2nd, 2006
 *
 * @author 		: Krishna K H
 * Modified by 	: Arun K N
 * Modified date: May 11, 2007
 * Reason 		: Conversion to Dispatch Action
 */

package com.ttk.action.enrollment;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;














import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.dom4j.Document;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.action.tree.Node;
import com.ttk.action.tree.TreeData;
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.business.administration.RuleManager;
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.business.enrollment.MemberManager;
import com.ttk.business.enrollment.PolicyManager;
import com.ttk.business.webservice.ValidationRuleManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.WebBoardHelper;
import com.ttk.common.exception.TTKException;
import com.ttk.common.security.RuleXMLHelper;
import com.ttk.dao.ResourceManager;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.ProductVO;
import com.ttk.dto.administration.RuleVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.enrollment.DomiciliaryVO;
import com.ttk.dto.enrollment.MemberAddressVO;
import com.ttk.dto.enrollment.MemberCancelVO;
import com.ttk.dto.enrollment.MemberDetailVO;
import com.ttk.dto.enrollment.MemberVO;
import com.ttk.dto.enrollment.PolicyDetailVO;
import com.ttk.dto.enrollment.PolicyVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used to search for members,add/edit member details,approve for card printing,
 *  validate the defined rules for each family in enrollment and endorsement flow.
 * This class also provides option for deleting the enrollment.
 */


public class MemberAction extends TTKAction
{
    private static final String strDelete = "Delete";
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    private static final String strIndMemberDetails="indmemberdetails";
    private static final String strGrpPolicyDetails="grpmemberdetails";
    private static final String strIndAddMemberDetails="indaddnewmember";
    private static final String strCorAddMemberDetails="coraddnewmember";
    private static final String strNonCorAddMemberDetails="noncoraddnewmember";
    private static final String strGrpAddMemberDetails="grpaddnewmember";
    private static final String strCorpMemberDetails="cormemberdetails";
    private static final String strCorpRenewMemberDetails="cormemberrenewdetails";
    private static final String strNonCorpMemberDetails="noncorpmemberdetails";

    // For sub link name
    private static final String strIndividualPolicy="Individual Policy";
    private static final String strIndPolicyasGroup="Ind. Policy as Group";
    private static final String strCorporatePolicy="Corporate Policy";
    private static final String strNonCorporatePolicy="Non-Corporate Policy";

    private static final String strEnrollment="ENM";
    private static final String strEndorsement="END";
    //declaration of other constants used in this class

    //declaration of constants specifying the position of icons to be disabled based on condtions and permissions
    private static final String strCancelEnroll="cancelenrollment";
	 private static final String strReportdisplay="reportdisplay";
	 private static final String strReportExp="onlinereport";
	 private static final String strMemberPolicyRule="MemberPolicyRule";
	 private static final String strAddMemberPolicyRule="AddMemberPolicyRule";
	 private static final String strMemberProductClauseList="memberClauseList";
	 private static final String strFailure="failure";
    private static final int RENEW_ICON=0;
    private static final int ROOT_CARD_ICON=3;
    private static final int ADD_ICON=5;
    private static final int R00T_CANCEL_ICON=6;
    private static final int ROOT_DELETE_ICON=7;
    /*private static final int ROOT_PASS_ICON=8;
    private static final int R00T_UNCHECK_ICON=9;
    private static final int R00T_FAIL_ICON=10;*/
    //private static final int R00T_CHANGEPWD_ICON=11;

    private static final int CHILD_CANCEL_ICON=4;
    private static final int CHILD_DELETE_ICON=5;
    private static final int ROOT_RULE_ICON=2;
    private static final int MEMBER_RULE_ICON=1;
    private static final int CHILD_CARD_ICON=2;
    private static Logger log = Logger.getLogger( MemberAction.class );
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
    	
        try
        {
            log.debug("Inside doDefault method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            String strForwardPath=getForwardPath(strActiveSubLink);
            String strTreeName = getTreeName(strActiveSubLink);
            String strSubLinkCode=getSubLinkCode(strActiveSubLink);
            DynaActionForm frmMember=(DynaActionForm)form;
            frmMember.set("display",null);//this attribute is used in JSP to show the error message.
            if(WebBoardHelper.checkWebBoardId(request)==null)
            {
                TTKException expTTK = new TTKException();
                frmMember.set("display","display");//this attribute is used in JSP to show the error message.
                request.getSession().setAttribute("treeData",null);
                if(strSwitchType.equals(strEnrollment))
                {
                    expTTK.setMessage("error.enrollment.required");
                }//end of if(strSwitchType.equals("ENM"))
                else
                {
                    expTTK.setMessage("error.endorsement.required");
                }//end of else
                throw expTTK;
            }//end of if(WebBoardHelper.checkWebBoardId(request)==null)

            if(strSwitchType.equals(strEndorsement))
            {
                if(WebBoardHelper.getPolicySeqId(request)==null)
                {
                    TTKException expTTK = new TTKException();
                    frmMember.set("display","display");//this attribute is used in JSP to show the error message.
                    expTTK.setMessage("error.enrollment.policyrequired");
                    throw expTTK;
                }//end of if(WebBoardHelper.getPolicySeqId(request)==null)
                if(!WebBoardHelper.getPolicyYN(request).equals("Y"))
                {
                    frmMember.set("display","display");//this attribute is used in JSP to show the error message.
                    TTKException expTTK = new TTKException();
                    if(WebBoardHelper.getPolicyYN(request).equals("N"))
                    {
                        expTTK.setMessage("error.endorsement.policydetails.nopolicy");
                    }//end of if(WebBoardHelper.getPolicyYN(request).equals("N"))
                    if(WebBoardHelper.getPolicyYN(request).equals("O"))
                    {
                    	expTTK.setMessage("error.endorsement.policydetails.anotherworflow" );
                    }//end of if(WebBoardHelper.getPolicyYN(request).equals("O"))
                    throw expTTK;
                }//end of if(!WebBoardHelper.getPolicyYN(request).equals("Y"))
            }//end of if(strSwitchType.equals("END"))

            MemberManager memberManager=this.getMemberManager();
            TreeData treeData = TTKCommon.getTreeData(request);
            ArrayList<Object> alSearchParam = new ArrayList<Object>();
            MemberVO memberVO = null;

            if(strSwitchType.equals(strEnrollment))
            {
                alSearchParam.add(WebBoardHelper.getPolicySeqId(request));
                Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
                if(strActiveSubLink.trim().equals("Corporate Policy"))
                {
                	 String policy_status ="";
                     policy_status =    memberManager.getPolicyStatus(pol_seq_id);
                     strForwardPath=getForwardPath(strActiveSubLink,policy_status);
                     request.getSession().setAttribute("policy_status", policy_status);     
                }
            }//end of if(strSwitchType.equals(strEnrollment))
            else
            {
                alSearchParam.add(WebBoardHelper.getEndorsementSeqId(request));
            }//end of else
            alSearchParam.add(strSwitchType);
            alSearchParam.add(strSubLinkCode);

            if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
            {
                ((DynaActionForm)form).initialize(mapping);//reset the form data
            }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))

            String strCaption="["+TTKCommon.checkNull(WebBoardHelper.getPolicyNumber(request))+"]";
            treeData = new TreeData();
            //create the required tree
            treeData.createTreeInfo(strTreeName,null,true);

            //check the permision and set the tree for not to display respective icon
            this.checkTreePermission(request,treeData,strSwitchType);
            request.getSession().setAttribute("treeData",null);
            frmMember.set("caption",strCaption);
            treeData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,
                    request,strSwitchType,strSubLinkCode));
            treeData.modifySearchData("search");
            frmMember.set("display","display");
            //get the meberlist from database
            ArrayList alMembers = memberManager.getMemberList(treeData.getSearchData());
            
            treeData.setSearchData(null);
            frmMember.set("display",null);//this attribute is used in JSP to show the error message.

            // get the data from database for Individual Policy and Ind. Policy as Group tab
            // For Corporate Policy and Non-Corporate Policy tab on search load the member list
            // If user click on Enrollment No. then policyVO will be in request scope then display that Enrollment detail
        
            if(strActiveSubLink.equals(strIndividualPolicy) || strActiveSubLink.equals(strIndPolicyasGroup)  ||  strActiveSubLink.equals(strCorporatePolicy)
                    ||request.getAttribute("policyVO")!=null)
            {
            
                treeData.setRootData(alMembers);
            }//end of if(strActiveSubLink.equals(strIndividualPolicy) || strActiveSubLink.equals(strIndPolicyasGroup) ||request.getAttribute("policyVO")!=null)

            //set the tree data object to session
            request.getSession().setAttribute("treeData",treeData);
            if(alMembers!=null && alMembers.size()>0)
            {
                memberVO = (MemberVO)alMembers.get(0);
            }//if(alMembers!=null && alMembers.size()>0)
            this.checkRuleValid(treeData,strActiveSubLink);
            this.documentViewer(request, memberVO);
            return this.getForward(strForwardPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    private String getForwardPath(String strActiveSubLink, String policy_status){
		// TODO Auto-generated method stub
    	 String strForwardPath=null;
    	 TTKException expTTK = new TTKException();
    	 if(policy_status != null)
         {
		    	if(strActiveSubLink.equals(strCorporatePolicy)  &&  policy_status.trim().equals("FTS"))
		        {
		            strForwardPath=strCorpMemberDetails;
		
		        } 
		    	//end of if(strActiveSubLink.equals(strCorporatePolicy))
		    	else if(strActiveSubLink.equals(strCorporatePolicy)  &&  policy_status.trim().equals("RTS"))
		        {
		    		
		            strForwardPath=strCorpRenewMemberDetails;
		           
		        } 
		   }
    	 return strForwardPath;
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
    	log.info("Inside doChangeWebBoard method of MemberAction");
    	
        return doDefault(mapping,form,request,response);
    }//end of doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
        	log.debug("Inside doSearch method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            TreeData treeData = TTKCommon.getTreeData(request);

            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            String strForwardPath=getForwardPath(strActiveSubLink);
            String strTreeName = getTreeName(strActiveSubLink);

            String strSubLinkCode=getSubLinkCode(strActiveSubLink);
            DynaActionForm frmMember=(DynaActionForm)form;
            frmMember.set("display",null);//this attribute is used in JSP to show the error message.

            MemberManager memberManager=this.getMemberManager();

            String strCaption="["+TTKCommon.checkNull(WebBoardHelper.getPolicyNumber(request))+"]";
            //if the page number is clicked, display the appropriate page
            if(!TTKCommon.checkNull(request.getParameter("pageId")).equals(""))
            {
                treeData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
                treeData.setSelectedRoot(-1);
                return this.getForward(strForwardPath, mapping, request);
            }//end of if(!TTKCommon.checkNull(request.getParameter("pageId")).equals(""))

            //create the required tree
            treeData = new TreeData();
            treeData.createTreeInfo(strTreeName,null,true);
            //check the permision and set the tree for not to display respective icon
            this.checkTreePermission(request,treeData,strSwitchType);
            treeData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request,
                    strSwitchType,strSubLinkCode));
            treeData.modifySearchData("search");
            //get the meberlist from database
            ArrayList alMembers = memberManager.getMemberList(treeData.getSearchData());
            MemberVO memberVO = null;
            treeData.setData(alMembers, "search");
            //set the tree data object to session
            request.getSession().setAttribute("treeData",treeData);
            frmMember.set("caption",strCaption);
            if(alMembers!=null && alMembers.size()>0)
            {
                memberVO = (MemberVO)alMembers.get(0);
            }//if(alMembers!=null && alMembers.size()>0)
            //Display the appropreate Validate result Icon in tree
            this.checkRuleValid(treeData,strActiveSubLink);
            this.documentViewer(request,memberVO);
            return this.getForward(strForwardPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


    /**
     * This method is used to show/Hide the dependent member list of the family
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doShowHide(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
        	 String strForwardPath = null;
        	log.info("Inside doShowHide method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            TreeData treeData = TTKCommon.getTreeData(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            String strSubLinkCode=getSubLinkCode(strActiveSubLink);
            DynaActionForm frmMember=(DynaActionForm)form;
            frmMember.set("display",null);//this attribute is used in JSP to show the error message.

            MemberManager memberManager=this.getMemberManager();
     	    String policy_status ="";
            Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
            policy_status =    memberManager.getPolicyStatus(pol_seq_id);
            strForwardPath=getForwardPath(strActiveSubLink,policy_status);
            request.getSession().setAttribute("policy_status", policy_status);
            MemberVO memberVO = null;
            String strCaption="["+TTKCommon.checkNull(WebBoardHelper.getPolicyNumber(request))+"]";
            int iSelectedRoot = Integer.parseInt((String)frmMember.get("selectedroot"));
            //create the required tree
            ArrayList alNodeMembers = new ArrayList();
            ArrayList<Object> alSearchParam = new ArrayList<Object>();
            memberVO = ((MemberVO)treeData.getRootData().get(iSelectedRoot));

            //create search parameter to get the dependent list
            alSearchParam.add(memberVO.getPolicyGroupSeqID());
            alSearchParam.add(strSwitchType);
            alSearchParam.add(strSubLinkCode);

            //get the dependent list from database
            alNodeMembers=memberManager.getDependentList(alSearchParam);
          
            if(strActiveSubLink.trim().equals("Corporate Policy"))
            {
                 strForwardPath=getForwardPath(strActiveSubLink,policy_status);
            }
            else
            {
             strForwardPath=getForwardPath(strActiveSubLink);
            }
            this.checkCancelMember(treeData,alNodeMembers,request,strSwitchType);
            treeData.setSelectedRoot(iSelectedRoot);
            ((MemberVO)treeData.getRootData().get(iSelectedRoot)).setMemberList(alNodeMembers);
            treeData.setSelectedRoot(iSelectedRoot);
            request.getSession().setAttribute("treeData",treeData);
            frmMember.set("caption",strCaption);
            return this.getForward(strForwardPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doShowHide(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
        	log.info("Inside doBackward method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);

            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            String strForwardPath=getForwardPath(strActiveSubLink);

            TreeData treeData = TTKCommon.getTreeData(request);
            MemberManager memberManager=this.getMemberManager();
            MemberVO memberVO = null;

            treeData.modifySearchData(strBackward);//modify the search data
            //get the meberlist from database
            ArrayList alMembers = memberManager.getMemberList(treeData.getSearchData());
            treeData.setData(alMembers, strBackward);//set the table data
            request.getSession().setAttribute("treeData",treeData);//set the table data object to session

            this.documentViewer(request, memberVO);
            return this.getForward(strForwardPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
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
        	log.debug("Inside doForward method of MemberAction");
        	DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);

            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            String strForwardPath=getForwardPath(strActiveSubLink);

            TreeData treeData = TTKCommon.getTreeData(request);
            MemberManager memberManager=this.getMemberManager();
            MemberVO memberVO = null;

            treeData.modifySearchData(strForward);//modify the search data
            //get the meberlist from database
            ArrayList alMembers = memberManager.getMemberList(treeData.getSearchData());
            treeData.setData(alMembers, strForward);//set the table data
            request.getSession().setAttribute("treeData",treeData);//set the table data object to session

            this.documentViewer(request, memberVO);
            return this.getForward(strForwardPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


    /**
     * This method is used to Approve the Card printing for the family or selected members
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doApproveCard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
        	log.info("Inside doApproveCard method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);

            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            String strForwardPath=getForwardPath(strActiveSubLink);
            TreeData treeData = TTKCommon.getTreeData(request);
            Long lngSeqID = null;
            DynaActionForm generalForm = (DynaActionForm)form;

            MemberVO memberVO= new MemberVO();
            MemberManager memberManager=this.getMemberManager();
            ArrayList<Object> alSearchParam = new ArrayList<Object>();

            int iResult=0;
            String strSelectedRoot = (String)generalForm.get("selectedroot");
            String strSelectedNode = (String)generalForm.get("selectednode");
            treeData.setSelectedRoot(-1);   //sets the selected rows
            memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode); // from tree with with min data
            if(strActiveSubLink.trim().equals("Corporate Policy"))
            {
            	 String policy_status ="";
            	 Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
                 policy_status =    memberManager.getPolicyStatus(pol_seq_id);
                 strForwardPath=getForwardPath(strActiveSubLink,policy_status);
                 request.getSession().setAttribute("policy_status", policy_status);
            }
            String strFlag="";
            //if selected root is null then the button is clicked to approve all for card printing.
            if(strSelectedRoot==null ||strSelectedRoot.equals(""))
            {
                strFlag="POLICY";
                lngSeqID=WebBoardHelper.getPolicySeqId(request);
            }//end of if(strSelectedRoot==null ||strSelectedRoot.equals(""))
            else if(strSelectedNode==null ||strSelectedNode.equals("")) // if selected node is null then root element is selected else node is selected
            {
                strFlag="GROUP";
                lngSeqID = memberVO.getPolicyGroupSeqID();
            }//end of (strSelectedNode==null || strSelectedNode.equals("")
            else
            {
                strFlag = "MEMBER";
                lngSeqID = memberVO.getMemberSeqID();
            }//end of else
            alSearchParam.add(getPolicyTypeCode(strActiveSubLink));
            alSearchParam.add(lngSeqID);
            alSearchParam.add(strFlag);
            alSearchParam.add(TTKCommon.getUserSeqId(request));
            iResult= memberManager.approveCardPrinting(alSearchParam);
            if(iResult > 0)
            {
                request.setAttribute("updated","message.approveCard");
            }//end of if(iResult == 0)

            return this.getForward(strForwardPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doApproveCard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to validate the defined rules for each family.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doValidateRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
        	log.debug("Inside doValidateRule method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            MemberVO memberVO = null;
            MemberManager memberManager=this.getMemberManager();
            RuleXMLHelper ruleXmlHelper=new RuleXMLHelper();
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            String strForwardPath=getForwardPath(strActiveSubLink);
            TreeData treeData = TTKCommon.getTreeData(request);

            DynaActionForm generalForm = (DynaActionForm)form;
            String strSelectedRoot = (String)generalForm.get("selectedroot");
            String strSelectedNode = (String)generalForm.get("selectednode");
            treeData.setSelectedRoot(-1);   //sets the selected rows
            memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode); // from tree with with min data
            PolicyManager policyObject=this.getPolicyManagerObject();
            Document policyRule = policyObject.validateEnrollment("E",memberVO.getPolicyGroupSeqID());
            if(policyRule==null)
            {
                TTKException expTTK = new TTKException();
                expTTK.setMessage("error.enrollment.policy.rulenotdefined");
                throw expTTK;
            }//end of if(policyRule==null)

            //Merge the policy rule with Base rule to add display nodes
            Document baseRuleDoc=TTKCommon.getDocument("MasterBaseRules.xml");
            policyRule=ruleXmlHelper.mergeDisplayNodes(policyRule,baseRuleDoc);

            ValidationRuleManager validationRuleManager = this.getValidationRuleManagerObject();
            //clear the previous error stored in database
            validationRuleManager.clearRuleErrors(memberVO.getEnrollmentID(),"E");
            ArrayList alErrors = validationRuleManager.executeEnrollmentValidation(policyRule);
            if(alErrors!=null && alErrors.size()>0)
            {
                //Save the error
                validationRuleManager.saveRuleErrors(alErrors);
                //Update status as Rule failed
                validationRuleManager.updateValidationStatus("E",memberVO.getPolicyGroupSeqID(),"F");
            }//end of if(alErrors!=null && alErrors.size()>0)
            //requery to database
           
            if(strActiveSubLink.equals(strIndividualPolicy) || strActiveSubLink.equals(strIndPolicyasGroup)  || strActiveSubLink.equals(strCorporatePolicy))
            {
                treeData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,
                        request,strSwitchType,getSubLinkCode(strActiveSubLink)));
                treeData.modifySearchData("search");
                //get the meberlist from database
                ArrayList alMembers = memberManager.getMemberList(treeData.getSearchData());
                treeData.setRootData(alMembers);
                treeData.setSearchData(null);
            }
            else if(treeData.getSearchData()!=null && treeData.getSearchData().size()>0)
            {
                //get the meberlist from database
                ArrayList alMembers = memberManager.getMemberList(treeData.getSearchData());
                treeData.setRootData(alMembers);
                if(treeData.getSelectedRoot()>=0)
                {
                    memberVO = (MemberVO)treeData.getSelectedObject(String.valueOf(treeData.getSelectedRoot()),null);

                    ArrayList<Object> alSearchParam = new ArrayList<Object>();
                    alSearchParam.add(memberVO.getPolicyGroupSeqID());
                    alSearchParam.add(strSwitchType);
                    alSearchParam.add(getSubLinkCode(strActiveSubLink));

                    //get the dependent list from database
                    ArrayList alNodeMembers=memberManager.getDependentList(alSearchParam);
                    this.checkCancelMember(treeData,alMembers,request,strSwitchType);
                    ((MemberVO)treeData.getRootData().get(treeData.getSelectedRoot())).setMemberList(alNodeMembers);
                }//end of if(treeData.getSelectedRoot()>=0)
                // Search criteria is there only in Corporate Policy and Non-Corporate Policy tab
                if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
                {
                    treeData.setForwardNextLink();
                }//end of if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
                request.getSession().setAttribute("treeData",treeData);//set the table data object to session
            }//end of if(treeData.getSearchData().size()>0)
            //Display the appropreate Validate result Icon in tree
            this.checkRuleValid(treeData,strActiveSubLink);

            return this.getForward(strForwardPath, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doValidateRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to Add a new member to Family
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doAddMember(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
        	log.info("Inside doAddMember method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            DynaActionForm frmMember=(DynaActionForm)form;
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);

            HashMap hmCityList = null;
            ArrayList alCityList = new ArrayList();
            TreeData treeData = TTKCommon.getTreeData(request);

            MemberManager memberManager=this.getMemberManager();
            Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
            if(strActiveSubLink.trim().equals("Corporate Policy"))
            {
            	 String policy_status ="";
                 policy_status =    memberManager.getPolicyStatus(pol_seq_id);
                 request.getSession().setAttribute("policy_status", policy_status);
                 request.getSession().setAttribute("CorporatePolicy","CorporatePolicy");
            }
            HospitalManager hospitalObject=this.getHospitalManagerObject();

            String strCaption=(String)frmMember.get("caption");
            String strHiddenName="";
            String strSecondName="";
            String strFamilyName="";
            String strRelationID="";
            String strSelectedRoot = (String)frmMember.get("selectedroot");
            String strSelectedNode = (String)frmMember.get("selectednode");

            MemberDetailVO memberDetailVO = new MemberDetailVO();   
			MemberAddressVO memberAddressVO=new MemberAddressVO();


            MemberVO memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode);
         
            //get the Policy End Date and Endorsement EffectiveDate From the Root object
            Date dtPolicyEndDate=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getPolicyEndDate();
            Date dtPolicyStartDate=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getPolicyStartDate();
            Date dtEffectiveDate=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getEffectiveDate();

            //get the Relationship list based on the Ins. Company. Select Abbreviation Code from root node
            ArrayList alRelationShip = memberManager.getRelationshipCode(((MemberVO)treeData.
                    getSelectedObject(strSelectedRoot,null)).getAbbrCode());

            //added for IBM Endorsement Age CR
            memberDetailVO = memberManager.getMemberDetail(memberVO.getPolicyGroupSeqID());
            
            String dtDateOfJoining="";//added for IBM AGE CR
            String dtDateOfMarriage="";//added for IBM AGE CR
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String policyStartDate = df.format(dtPolicyStartDate);
            if(memberDetailVO.getDateOfJoining()!=null)
            {dtDateOfJoining = df.format(memberDetailVO.getDateOfJoining());
            }
            if(memberDetailVO.getDateOfMarriage()!=null)
            {dtDateOfMarriage = df.format(memberDetailVO.getDateOfMarriage());
            }
            //Ended IBM Endorsement Age CR
	        request.getSession().setAttribute("policyStartDate",policyStartDate);
            request.getSession().setAttribute("dateOfJoining",dtDateOfJoining);
            request.getSession().setAttribute("dateOfMarriage",dtDateOfMarriage);

            memberDetailVO.setPolicyGroupSeqID(memberVO.getPolicyGroupSeqID());
            memberDetailVO.setEnrollmentNbr(memberVO.getEnrollmentID());
            memberDetailVO.setStatus("POA");     //select status as Active while adding a member
            memberDetailVO.setStatusDesc("Active");
            memberDetailVO.setExitDate(dtPolicyEndDate);
            strCaption="Add ["+TTKCommon.checkNull(WebBoardHelper.getPolicyNumber(request))+"]";

            memberAddressVO.setCountryCode("134");
            memberDetailVO.setMemberAddressVO(memberAddressVO);
            //get the Policy holder name from Root and store in hidden field
         //   strHiddenName=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getName();
            strHiddenName=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getStrFirstName();
            strSecondName=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getStrSecondName();
            strFamilyName=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getStrFamilyName();
            //  
            //set the Policy End Date and Endorsement EffectiveDate to memberDetailVO
            memberDetailVO.setPolicyEndDate(dtPolicyEndDate);
            memberDetailVO.setPolicyStartDate(dtPolicyStartDate);
            memberDetailVO.setEffectiveDate(dtEffectiveDate);

            //in vo name is stored with enrollment no. like xxxxx-xxxx-NAME, take the last part for name
           // strHiddenName=strHiddenName.substring(strHiddenName.lastIndexOf("-")+1);
            DynaActionForm frmAddMember = (DynaActionForm)FormUtils.setFormValues("frmAddMember",memberDetailVO,
                    this,mapping,request);            
          

            //To set the required information to the Add Group screen based on selected node
            frmAddMember = (DynaActionForm)FormUtils.setFormValues("frmAddMember",memberDetailVO,
                    this,mapping,request);
            request.getSession().setAttribute("providerStates",TTKCommon.getStates("134"));
            
           
            memberAddressVO.setStateCode("DOH");
                     
            hmCityList=hospitalObject.getCityInfo("DOH");
            String countryCode	=	(String)(hmCityList.get("CountryId"));
            String isdcode	=	(String)(hmCityList.get("isdcode"));
            String stdcode	=	(String)(hmCityList.get("stdcode"));
    
          
            memberDetailVO.setMemberAddressVO(memberAddressVO);

            if(hmCityList!=null)
            {
                alCityList = (ArrayList)hmCityList.get(memberDetailVO.getMemberAddressVO().getStateCode());
            }//end of if(hmCityList!=null)
            if(alCityList==null)
            {
                alCityList=new ArrayList();
            }//end of if(alCityList==null)
             
            memberAddressVO.setCountryCode(countryCode);
            memberAddressVO.setOff1IsdCode(isdcode);		
            memberAddressVO.setOff2IsdCode(isdcode);		
            memberAddressVO.setHomeIsdCode(isdcode);	
            memberAddressVO.setMobileIsdCode(isdcode);
            
            memberAddressVO.setOff1StdCode(stdcode);
            memberAddressVO.setOff2StdCode(stdcode);
            memberAddressVO.setHomeStdCode(stdcode);
           request.getSession().setAttribute("providerStates",TTKCommon.getStates(memberDetailVO.getMemberAddressVO().getCountryCode()));  
           request.getSession().setAttribute("providerAreas",TTKCommon.getAreas(memberDetailVO.getMemberAddressVO().getStateCode()));

            frmAddMember.set("memberAddressVO", (DynaActionForm)FormUtils.setFormValues("frmMemberAddress",
                    memberDetailVO.getMemberAddressVO(),this,mapping,request));
            
            frmAddMember.set("caption",strCaption);
            frmAddMember.set("alCityList",alCityList);
            frmAddMember.set("alRelationShip",alRelationShip);
            frmAddMember.set("relationID",strRelationID);
            if(!memberDetailVO.getRelationTypeID().equals("NSF#OTH"))
            {
                frmAddMember.set("disableYN","Y");
            }//end of if(!memberDetailVO.getRelationTypeID().equals("NSF#OTH"))
            else
            {
                frmAddMember.set("disableYN","N");
            }//end of else
            frmAddMember.set("hiddenName",strHiddenName.trim());
            frmAddMember.set("hiddensecondName",strSecondName.trim());
            frmAddMember.set("hiddenfamilyName",strFamilyName.trim());
            frmAddMember.set("corporate",getPolicyTypeCode(strActiveSubLink));
            frmAddMember.set("mailNotificationhiddenYN", "Y");
            if(strSwitchType.equals(strEnrollment)&&strActiveSubLink.equals("Individual Policy")){
            	frmAddMember.set("inceptionDate",TTKCommon.getFormattedDate(memberDetailVO.getPolicyStartDate()));
            }//end of if(strSwitchType.equals(strEnrollment)&&strActiveSubLink.equals("Individual Policy"))

            if(strSwitchType.equals(strEndorsement)){
            	frmAddMember.set("inceptionDate",TTKCommon.getFormattedDate(memberDetailVO.getEffectiveDate()));
            }//end of if(strSwitchType.equals(strEndorsement))
			request.getSession().setAttribute("enrollmentNo",memberDetailVO.getEnrollmentID());//added for IBM AGE CR
            /*if(strSwitchType.equals(strEnrollment)&&strActiveSubLink.equals("Individual Policy"))
            {
            PolicyDetailVO policyDetailVO=null;
            PolicyManager policyObject=this.getPolicyManagerObject();
            ArrayList<Object> alPolicy = new ArrayList<Object>();
            alPolicy.add(WebBoardHelper.getPolicySeqId(request));
            alPolicy.add(strSwitchType);//Enrollment or Endorsement
            alPolicy.add(strIndPolicyType);
            policyDetailVO= policyObject.getPolicy(alPolicy);
            frmAddMember.set("inceptionDate",TTKCommon.getFormattedDate(policyDetailVO.getStartDate()));
            }//end of if(strSwitchType.equals(strEnrollment)&&strActiveSubLink.equals("Individual Policy"))
*/            request.getSession().setAttribute("frmAddMember",frmAddMember);
            this.documentViewer(request,memberVO);
            return this.getForward(getMemberDetailPath(strActiveSubLink), mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doAddMember(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to view/edit the  member details.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doViewMember(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
    		log.info("Inside doViewMember method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            DynaActionForm frmMember=(DynaActionForm)form;
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);

            HashMap hmCityList = null;
            ArrayList alCityList = new ArrayList();
            TreeData treeData = TTKCommon.getTreeData(request);

            MemberManager memberManager=this.getMemberManager();
            HospitalManager hospitalObject=this.getHospitalManagerObject();

            String strCaption=(String)frmMember.get("caption");
            Date dtPolicyEndDate=null;
            Date dtPolicyStartDate=null;
            Date dtEffectiveDate=null;
            String dtDateOfJoining="";//added for IBM AGE CR
			String dtDateOfMarriage="";//added for IBM AGE CR
            String strHiddenName="";
            String strRelationID="";
            String strSelectedRoot = (String)frmMember.get("selectedroot");
            //  
            String strSelectedNode = (String)frmMember.get("selectednode");
            MemberVO memberVO= new MemberVO();
            MemberDetailVO memberDetailVO = new MemberDetailVO();
            MemberAddressVO memberAddressVO=new MemberAddressVO();
            memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode); // from tree with min data

            //get the Policy End Date and Endorsement EffectiveDate From the Root object
            dtPolicyEndDate=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getPolicyEndDate();
            dtPolicyStartDate=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getPolicyStartDate();
            dtEffectiveDate=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getEffectiveDate();

            //get the Relationship list based on the Ins. Company. Select Abbreviation Code from root node
            ArrayList alRelationShip = memberManager.getRelationshipCode(((MemberVO)treeData.
                    getSelectedObject(strSelectedRoot,null)).getAbbrCode());

            //if node is selected then it is edit mode else it is add mode
            if(!TTKCommon.checkNull((String)frmMember.get("selectednode")).equals(""))
            {
                //create search parameter to get the member detail

                ArrayList<Object> alSearchParam = new ArrayList<Object>();

                alSearchParam.add(memberVO.getMemberSeqID());
                alSearchParam.add(strSwitchType);
                alSearchParam.add(getSubLinkCode(strActiveSubLink));
                if(memberVO.getMemberSeqID()!=null)
                    memberDetailVO = memberManager.getMember(alSearchParam);
                strRelationID=memberDetailVO.getRelationTypeID();
                if(!memberDetailVO.getRelationTypeID().equals("NSF")) // for self address will be address of enrollment address
                    memberAddressVO=memberDetailVO.getMemberAddressVO();
                memberDetailVO.setRelationTypeID(memberDetailVO.getRelationTypeID()+"#"+memberDetailVO.getGenderYN());
                strCaption="Edit ["+TTKCommon.checkNull(WebBoardHelper.getPolicyNumber(request))+"]";
            }//end of if(!TTKCommon.checkNull((String)frmMemberList.get("selectednode")).equals(""))

			//added for IBM Age CR
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String policyStartDate = df.format(dtPolicyStartDate);
			if(memberDetailVO.getDateOfJoining()!=null)
			{dtDateOfJoining = df.format(memberDetailVO.getDateOfJoining());
			}
			if(memberDetailVO.getDateOfMarriage()!=null)
			{dtDateOfMarriage = df.format(memberDetailVO.getDateOfMarriage());
			}
			request.getSession().setAttribute("policyStartDate",policyStartDate);
			request.getSession().setAttribute("dateOfJoining",dtDateOfJoining);
			request.getSession().setAttribute("dateOfMarriage",dtDateOfMarriage);
		   //Ended IBM Age CR

            memberDetailVO.setMemberAddressVO(memberAddressVO);
            //get the Policy holder name from Root and store in hidden field
            
            strHiddenName=((MemberVO)treeData.getSelectedObject(strSelectedRoot,null)).getName();

            //set the Policy End Date and Endorsement EffectiveDate to memberDetailVO
            memberDetailVO.setPolicyEndDate(dtPolicyEndDate);
            memberDetailVO.setPolicyStartDate(dtPolicyStartDate);
            memberDetailVO.setEffectiveDate(dtEffectiveDate);

            //in vo name is stored with enrollment no. like xxxxx-xxxx-NAME, take the last part for name
            //strHiddenName=strHiddenName.substring(strHiddenName.lastIndexOf("-")+1);
            DynaActionForm frmAddMember = (DynaActionForm)FormUtils.setFormValues("frmAddMember",memberDetailVO,
                    this,mapping,request);

            request.getSession().setAttribute("providerStates",TTKCommon.getStates(memberDetailVO.getMemberAddressVO().getCountryCode()));                 
            request.getSession().setAttribute("providerAreas",TTKCommon.getAreas(memberDetailVO.getMemberAddressVO().getStateCode()));

            frmAddMember.set("memberAddressVO", (DynaActionForm)FormUtils.setFormValues("frmMemberAddress",
                    memberDetailVO.getMemberAddressVO(),this,mapping,request));

            hmCityList=hospitalObject.getCityInfo();
            if(hmCityList!=null)
            {
                alCityList = (ArrayList)hmCityList.get(memberDetailVO.getMemberAddressVO().getStateCode());
            }//end of if(hmCityList!=null)
            if(alCityList==null)
            {
                alCityList=new ArrayList();
            }//end of if(alCityList==null)
            frmAddMember.set("caption",strCaption);
            frmAddMember.set("alCityList",alCityList);
            frmAddMember.set("alRelationShip",alRelationShip);
            frmAddMember.set("relationID",strRelationID);
            request.getSession().setAttribute("enrollmentNo",memberDetailVO.getEnrollmentID());
            if(!memberDetailVO.getRelationTypeID().equals("NSF#OTH"))
            {
                frmAddMember.set("disableYN","Y");
            }//end of if(!memberDetailVO.getRelationTypeID().equals("NSF#OTH"))
            else
            {
                frmAddMember.set("disableYN","N");
            }//end of else
            frmAddMember.set("hiddenName",strHiddenName.trim());
            frmAddMember.set("corporate",getPolicyTypeCode(strActiveSubLink));
            request.getSession().setAttribute("frmAddMember",frmAddMember);            
            this.documentViewer(request,memberVO);
    		 Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
    		
    		  if(strActiveSubLink.trim().equals("Corporate Policy"))
              {
              	   String policy_status ="";
                   policy_status =    memberManager.getPolicyStatus(pol_seq_id);
                  request.getSession().setAttribute("policy_status", policy_status);
                  request.getSession().setAttribute("CorporatePolicy", "CorporatePolicy");
              }
    		  request.getSession().setAttribute("stopcashlessmember", memberDetailVO.getStopPreauthDate());
    		  request.getSession().setAttribute("stopclaimsmember", memberDetailVO.getStopClaimsDate());
            ActionForward actionForward=this.getForward(getMemberDetailPath(strActiveSubLink), mapping, request);
            return actionForward;
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doViewMember(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



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
        	log.info("Inside doDelete method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            DynaActionForm frmMember=(DynaActionForm)form;
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            TreeData treeData = TTKCommon.getTreeData(request);

            MemberManager memberManager=this.getMemberManager();
            String strFlag = "";
            String strSeqID = "";
            int iResult=0;
            Long lngEndorsementSeqID = null;
            ArrayList alMembers = null;
            ArrayList<Object> alSearchParam = new ArrayList<Object>();
            ArrayList<Object> alSearchDepeParam = new ArrayList<Object>();
            String strSelectedRoot = (String)frmMember.get("selectedroot");
            String strSelectedNode = (String)frmMember.get("selectednode");
            //treeData.setSelectedRoot(-1);   //sets the selected rows
            MemberVO memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode);

            //if selected node is null then root element is selected else node is selected
            if(strSelectedNode==null ||strSelectedNode.equals(""))
            {
                strFlag = "GROUP";
                strSeqID = memberVO.getPolicyGroupSeqID().toString();
            }//end of (strSelectedNode==null || strSelectedNode.equals("")
            else
            {
                strFlag = "MEMBER";
                strSeqID = memberVO.getMemberSeqID().toString();
            }//end of else
            if(strSwitchType.equals(strEndorsement)) // for Endorsement flow get the Endorsenet Seq ID from webBoard
            {
                lngEndorsementSeqID = WebBoardHelper.getEndorsementSeqId(request);
            }//end of if(strSwitchType.equals("END"))

            //create parameter to delete the member detail
            alSearchParam.add(strSwitchType);
            alSearchParam.add(getSubLinkCode(strActiveSubLink));
            alSearchParam.add(strFlag);
            alSearchParam.add(strSeqID);
            alSearchParam.add(lngEndorsementSeqID);
            alSearchParam.add(TTKCommon.getUserSeqId(request));

            //call business layer to delete the member or group
            iResult= memberManager.deleteMember(alSearchParam);
            if(iResult>0 && strFlag.equals("GROUP"))
            {
            	if(iResult == treeData.getRootData().size() )
            	{
            		treeData.modifySearchData(strDelete);//modify the search data
            		int iStartRowCount = Integer.parseInt((String)treeData.getSearchData().
            				get(treeData.getSearchData().size()-2));
            		if(iStartRowCount > 0)
            		{
            			alMembers = memberManager.getMemberList(treeData.getSearchData());
            		}//end of if(iStartRowCount > 0)
            	}//end if(iDeleteCount == treeData.getRootData().size())
            	else
            	{
            		alMembers = memberManager.getMemberList(treeData.getSearchData());
            	}//end of else
            	treeData.setRootData(alMembers);
            	//Search criteria is there only in Corporate Policy and Non-Corporate Policy tab
            	if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
            	{
            		treeData.setForwardNextLink();
            	}//end of if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
            	treeData.setSelectedRoot(-1);
            	request.getSession().setAttribute("treeData",treeData);
            }// end of if(iResult>0)
            else
            {
            	alSearchDepeParam = new ArrayList<Object>();
            	alSearchDepeParam.add(memberVO.getPolicyGroupSeqID());
            	alSearchDepeParam.add(strSwitchType);
            	alSearchDepeParam.add(getSubLinkCode(strActiveSubLink));
                //get the dependent list from database
                ArrayList alNodeMembers=memberManager.getDependentList(alSearchDepeParam);
                this.checkCancelMember(treeData,alMembers,request,strSwitchType);
                ((MemberVO)treeData.getRootData().get(Integer.parseInt(strSelectedRoot))).setMemberList(alNodeMembers);
                if(iResult<=0)
                {
                	doSearch(mapping,form,request,response);
                }//end of if(iResult>0)
            }//end of else
           
            Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
            
            if(strActiveSubLink.trim().equals("Corporate Policy"))
           { 
            	 String policy_status ="";
                 policy_status =    memberManager.getPolicyStatus(pol_seq_id);
                 return this.getForward(getForwardPath(strActiveSubLink,policy_status), mapping, request);
           }	
            
            return this.getForward(getForwardPath(strActiveSubLink), mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
        	
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
        	
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    public ActionForward doCancel(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
        	log.info("Inside doCancel method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");

            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            DynaActionForm frmMember=(DynaActionForm)form;
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            TreeData treeData = TTKCommon.getTreeData(request);
            MemberCancelVO memberCancelVO=new MemberCancelVO();
            MemberManager memberManager=this.getMemberManager();

            String strFlag = "";
            String strSeqID = "";
            int iResult=0;
            Long lngEndorsementSeqID = null;
            ArrayList<Object> alSearchParam = new ArrayList<Object>();
            String strSelectedRoot = (String)frmMember.get("selectedroot");
            String strSelectedNode = (String)frmMember.get("selectednode");
            treeData.setSelectedRoot(-1);   //sets the selected rows
            MemberVO memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode);
            //if selected node is null then root element is selected else node is selected
            if(strSelectedNode==null ||strSelectedNode.equals(""))
            {
                strFlag = "GROUP";
                strSeqID = memberVO.getPolicyGroupSeqID().toString();
            }//end of (strSelectedNode==null || strSelectedNode.equals("")
            else
            {
                strFlag = "MEMBER";
                strSeqID = memberVO.getMemberSeqID().toString();
            }//end of else
            if(strSwitchType.equals("END")) // for Endorsement flow get the Endorsenet Seq ID from webBoard
            {
                lngEndorsementSeqID = WebBoardHelper.getEndorsementSeqId(request);
            }//end of if(strSwitchType.equals("END"))

            //create parameter to cancel the member detail
            alSearchParam.add(strSwitchType);
            alSearchParam.add(getSubLinkCode(strActiveSubLink));
            alSearchParam.add(strFlag);
            alSearchParam.add(strSeqID);
            alSearchParam.add(memberVO.getPolicySeqID());
            alSearchParam.add(lngEndorsementSeqID);
            alSearchParam.add(TTKCommon.getUserSeqId(request));
           /* iResult= memberManager.cancelMember(alSearchParam);
            log.debug("Result value is :"+iResult);
            return this.getForward(getForwardPath(strActiveSubLink), mapping, request);*/
         /*  memberCancelVO.setSwitchType(strSwitchType);
            memberCancelVO.setActiveSubLink(strActiveSubLink);
            memberCancelVO.setFlags(strFlag);
            memberCancelVO.setSeqIDs(strSeqID);
            memberCancelVO.setPolicySeqID(memberVO.getPolicySeqID());	
            memberCancelVO.setEndorsementSeqID(lngEndorsementSeqID);
            memberCancelVO.setAddedBy(TTKCommon.getUserSeqId(request));
            frmMember = (DynaActionForm)FormUtils.setFormValues("frmMember",memberCancelVO,this,mapping,request);
        	request.setAttribute("frmMember",frmMember);*/
            
            request.getSession().setAttribute("alSearchParamRetrive",alSearchParam);
			return this.getForward(strCancelEnroll, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doCancel(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


    /**
     * This method is used to navigate to previous screen
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
        	log.info("Inside doClose method of MemberAction");
        	String strSwitchType = "";
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");

            if(frmPolicyList != null){
            	strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            }//end of if(frmPolicyList != null)

            this.setLinks(request,strSwitchType);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            TreeData treeData = TTKCommon.getTreeData(request);
            MemberManager memberManager=this.getMemberManager();
            if(treeData.getSearchData()!=null && treeData.getSearchData().size()>0)
            {
                //get the meberlist from database
                ArrayList alMembers = memberManager.getMemberList(treeData.getSearchData());
                treeData.setRootData(alMembers);
                if(treeData.getSelectedRoot()>=0)
                {
                    MemberVO memberVO = (MemberVO)treeData.getSelectedObject(String.valueOf(treeData.
                    					 getSelectedRoot()),null);
                    ArrayList<Object> alSearchParam = new ArrayList<Object>();
                    alSearchParam.add(memberVO.getPolicyGroupSeqID());
                    alSearchParam.add(strSwitchType);
                    alSearchParam.add(getSubLinkCode(strActiveSubLink));

                    //get the dependent list from database
                    ArrayList alNodeMembers=memberManager.getDependentList(alSearchParam);
                    this.checkCancelMember(treeData,alMembers,request,strSwitchType);
                    checkCancelMember(treeData,alNodeMembers,request,strSwitchType);
                    ((MemberVO)treeData.getRootData().get(treeData.getSelectedRoot())).setMemberList(alNodeMembers);
                }//end of if(treeData.getSelectedRoot()>=0)
                // Search criteria is there only in Corporate Policy and Non-Corporate Policy tab
                if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
                {
                    treeData.setForwardNextLink();
                }//end of if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
                request.getSession().setAttribute("treeData",treeData);//set the table data object to session
            }//end of if(treeData.getSearchData().size()>0)
            //Display the appropreate Validate result Icon in tree
            this.checkRuleValid(treeData,strActiveSubLink);
            Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
            if(strActiveSubLink.trim().equals("Corporate Policy"))
            {   
            	 String policy_status ="";
                 policy_status =    memberManager.getPolicyStatus(pol_seq_id);
                 request.getSession().setAttribute("policy_status", policy_status);
                 return this.getForward(getForwardPath(strActiveSubLink,policy_status), mapping, request);
            }
            return this.getForward(getForwardPath(strActiveSubLink), mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to Clear the associated Family and Member rules for this Policy.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doClearRules(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
            log.info("Inside doClearRules method of MemberAction");
            DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
            String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            MemberManager memberManager=this.getMemberManager();

            //call the bussines to clear the Rules
            int iResultCount=memberManager.clearEnrollmentRules(WebBoardHelper.getPolicySeqId(request),
                    strSwitchType,getPolicyTypeCode(strActiveSubLink));

            if(iResultCount>0)
            {
                request.setAttribute("updated","message.enrollment.rulescleared");
            }//end of if(iResultCount>0)
            Long pol_seq_id = WebBoardHelper.getPolicySeqId(request);
            if(strActiveSubLink.trim().equals("Corporate Policy"))
            {   
            	 String policy_status ="";
                 policy_status =    memberManager.getPolicyStatus(pol_seq_id);
                 request.getSession().setAttribute("policy_status", policy_status);
                 return this.getForward(getForwardPath(strActiveSubLink,policy_status), mapping, request);
            }
            return this.getForward(getForwardPath(strActiveSubLink), mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }//end of doClearRules(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method will add search criteria fields and values to the arraylist and will return it
     * @param frmPolicyList current instance of form bean
     * @param request HttpServletRequest object
     * @param strActiveSubLink current Active sublink
     * @return alSearchObjects ArrayList of search params
     * @throws TTKException
     */
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmSearchUser,HttpServletRequest request,
    		String strSwitchType,String strSubLinkCode)throws TTKException
    {
        //build the column names along with their values to be searched
        ArrayList<Object> alSearchParams = new ArrayList<Object>();
        if(request.getAttribute("policyVO")!=null)
        {
            PolicyVO policyVO =(PolicyVO)request.getAttribute("policyVO");
           
            if(policyVO!=null)
            {
            	
            	
                frmSearchUser.set("sEnrollmentNbr",policyVO.getEnrollmentNbr());
            }//end of if(policyVO!=null)
        }//end of if(request.getParameter("policyVO")!=null)
        if(strSwitchType.equals("ENM"))
        {
            alSearchParams.add(WebBoardHelper.getPolicySeqId(request));
        }//end of if(strSwitchType.equals("ENM"))
        else
        {
            alSearchParams.add(WebBoardHelper.getEndorsementSeqId(request));
        }//end of else

        if(strSubLinkCode.equals("IP") || strSubLinkCode.equals("IG"))
        {
            alSearchParams.add(null); //(String)frmSearchUser.get("sEnrollmentNbr")
            alSearchParams.add(null); //frmSearchUser.get("sMemberName")
            alSearchParams.add(null); //frmSearchUser.get("sEmpNo")
            alSearchParams.add(null); //frmSearchUser.get("sCorpMemberName")
            alSearchParams.add(null);
            alSearchParams.add(null);
            alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sQatarId"))); 
            alSearchParams.add(strSwitchType);
            alSearchParams.add(strSubLinkCode);
            alSearchParams.add("1"); //Start Row
            alSearchParams.add("1"); //End Row
        }//end of if(strSubLinkCode.equals("IP") || strSubLinkCode.equals("IG"))
        else
        {
        	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sEnrollmentNbr")));
            alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sMemberName")));
            if(strSubLinkCode.equals("CP"))
            {
            	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sCorpMemberName")));
            	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sEmployeeNum")));

            }//end of if(strSubLinkCode.equals("CP"))
            else{
            	alSearchParams.add(null); //frmSearchUser.get("sEmpNo")
            	alSearchParams.add(null); //frmSearchUser.get("sCorpMemberName")
            }//end of else
            if(strSubLinkCode.equals("NC"))
            {
            	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sCertificateNumber")));
            	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sOrderNumber")));
            }//end of if(strSubLinkCode.equals("NC"))
            else
            {
            	alSearchParams.add(null);
            	alSearchParams.add(null);
            }//end else
            
            alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchUser.get("sQatarId"))); 
            
            
            alSearchParams.add(strSwitchType);
            alSearchParams.add(strSubLinkCode);
            if(request.getAttribute("policyVO")!=null) // only one enrollment detail has to fetch if policyVO is found in request
            {
                alSearchParams.add("1"); //Start Row
                alSearchParams.add("1"); //End Row
            }//end of if(request.getAttribute("policyVO")!=null)
        }//end of else
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchUser,HttpServletRequest request)

    /**
     * Returns the MemberManager session object for invoking methods on it.
     * @return MemberManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private MemberManager getMemberManager() throws TTKException
    {
        MemberManager memberManager = null;
        try
        {
            if(memberManager == null)
            {
                InitialContext ctx = new InitialContext();
                memberManager = (MemberManager) ctx.lookup("java:global/TTKServices/business.ejb3/MemberManagerBean!com.ttk.business.enrollment.MemberManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "memberdetail");
        }//end of catch
        return memberManager;
    }//end getMemberManager()

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
            throw new TTKException(exp, "memberdetail");
        }//end of catch
        return hospManager;
    }//end getHospitalManagerObject()

    /**
     * Check the Member cancel status and set the display property of icons.
     * @param request The HTTP request we are processing
     * @param treeData TreeData for which permission has to check
     * @throws TTKException
     */
    private void checkCancelMember(TreeData treeData,ArrayList alMembers,HttpServletRequest request,
    		String strSwitchType) throws TTKException
    {
        ArrayList<Object> alNodeSetting=new ArrayList<Object>();
        Node chNode=null;
        if(alMembers!=null)
        {
            for(int i=0;i<alMembers.size();i++)
            {
                chNode=treeData.copyNodeSetting();//new Node();
                if(((MemberVO)alMembers.get(i)).getCancleYN().equalsIgnoreCase("Y"))
                {
                    chNode.setTextColor("red");
                    chNode.setIconVisible(CHILD_CANCEL_ICON,false);
                }//end of if(((MemberVO)alNodeMembers.get(i)).getCancleYN().equalsIgnoreCase("Y"))
                else
                {
                    chNode.setTextColor("black");
                    if(strSwitchType.equals("END") && TTKCommon.isAuthorized(request,"Cancel"))
                    {
                        chNode.setIconVisible(CHILD_CANCEL_ICON,true);
                    }//end of if(strSwitchType.equals("END") && TTKCommon.isAuthorized(request,"Cancel"))
                }//end of else
                alNodeSetting.add(chNode);
            }//end of for(int i=0;i<alNodeMembers.size();i++)
            treeData.setNodeSettings(alNodeSetting);
        }//end of if(alMembers!=null)
    }//end of checkRuleValid(TreeData treeData,ArrayList alMembers)


    /**
     * Check the Validation status and set the display property of icons.
     * @param request The HTTP request we are processing
     * @param treeData TreeData for which permission has to check
     */
    private void checkRuleValid(TreeData treeData,String strActiveSubLink)//,ArrayList alMembers
    {
        ArrayList alMembers = treeData.getRootData();
        if(alMembers!=null && alMembers.size()>0)
        {
            ArrayList<Object> alNodeSetting=new ArrayList<Object>();
            Node chNode=null;
            //String srtValidationStatus="";
            for(int i=0;i<alMembers.size();i++)
            {
                chNode=treeData.copyRootSetting();//new Node();
                //srtValidationStatus = ((MemberVO)alMembers.get(i)).getValidationStatus();
                //For corporate and Non corporate Policy hide the renew Icon
              /*  if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
                {
                    chNode.setIconVisible(RENEW_ICON,false);
                }//end of if(strActiveSubLink.equals(strCorporatePolicy) || strActiveSubLink.equals(strNonCorporatePolicy))
                //if policy TPA Policy status is Renewal then only display Renewal Icon
                else if(!((MemberVO)alMembers.get(i)).getTPAStatusTypeID().equals("TPR"))
                {
                    chNode.setIconVisible(RENEW_ICON,false);
                }*///end of if(!((MemberVO)alMembers.get(0)).getTPAStatusTypeID().equals("TPR"))

                /*chNode.setIconVisible(R00T_UNCHECK_ICON,false);
                chNode.setIconVisible(ROOT_PASS_ICON,false);
                chNode.setIconVisible(R00T_FAIL_ICON,false);*/
                /*if(srtValidationStatus.equalsIgnoreCase("U")||srtValidationStatus.equals(""))
                {
                    chNode.setIconVisible(R00T_UNCHECK_ICON,true);
                }//end of if(((MemberVO)alNodeMembers.get(i)).getCancleYN().equalsIgnoreCase("Y"))
                else if(srtValidationStatus.equalsIgnoreCase("P"))
                {
                    chNode.setIconVisible(ROOT_PASS_ICON,true);
                }//end of else
                else if(srtValidationStatus.equalsIgnoreCase("F"))
                {
                    chNode.setIconVisible(R00T_FAIL_ICON,true);
                }//end of else
*/                alNodeSetting.add(chNode);
            }//end of for(int i=0;i<alNodeMembers.size();i++)
            treeData.setRootSettings(alNodeSetting);
        }
    }//end of checkRuleValid(TreeData treeData,ArrayList alMembers)

    /**
     * Check the ACL permission and set the display property of icons.
     * @param request The HTTP request we are processing
     * @param treeData TreeData for which permission has to check
     */
    private void checkTreePermission(HttpServletRequest request,TreeData treeData,String strSwitchType)
    	throws TTKException
    {
        log.info("--------- inside check Tree permission --------------");
        //String strActiveSubLink=TTKCommon.getActiveSubLink(request);
        if(strSwitchType.equals("")||strSwitchType.equals("ENM"))
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(R00T_CANCEL_ICON,false);
            treeData.getTreeSetting().getChildNodeSetting().setIconVisible(CHILD_CANCEL_ICON,false);

            if(WebBoardHelper.getCompletedYN(request).equals("Y") && TTKCommon.isAuthorized(request,"Add")==true){
                treeData.getTreeSetting().getRootNodeSetting().setIconVisible(ROOT_CARD_ICON,true);
                treeData.getTreeSetting().getChildNodeSetting().setIconVisible(CHILD_CARD_ICON,true);
            }//end of if(WebBoardHelper.getCompletedYN(request).equals("Y"))
            else{
                treeData.getTreeSetting().getRootNodeSetting().setIconVisible(ROOT_CARD_ICON,false);
                treeData.getTreeSetting().getChildNodeSetting().setIconVisible(CHILD_CARD_ICON,false);
            }//end of else
        }//end of if(strSwitchType.equals("")||strSwitchType.equals("ENM"))
        /*if(strSwitchType.equals("ENM")&&!(strActiveSubLink.equals(strIndividualPolicy)|| strActiveSubLink.equals(strIndPolicyasGroup)))
        {
        	if(! (strActiveSubLink.equals(strCorporatePolicy))&&(TTKCommon.isAuthorized(request,"Special Permission")==false) )
	        {
	        	treeData.getTreeSetting().getRootNodeSetting().setIconVisible(R00T_CHANGEPWD_ICON,false);
	        }//end of if(! (strActiveSubLink.equals(strCorporatePolicy))&&(TTKCommon.isAuthorized(request,"Special Permission")==false) )
        }//END OF if(strSwitchType.equals("ENM")&&!(strActiveSubLink.equals(strIndividualPolicy)|| strActiveSubLink.equals(strIndPolicyasGroup)))*/
        // check the permision and set the tree for not to display respective icon
        if(TTKCommon.isAuthorized(request,"Add")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(ADD_ICON,false);
        }//end of if(TTKCommon.isAuthorized(request,"Add")==false)
        if(TTKCommon.isAuthorized(request,"Delete")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(ROOT_DELETE_ICON,false);
            treeData.getTreeSetting().getChildNodeSetting().setIconVisible(CHILD_DELETE_ICON,false);
        }// end of if(TTKCommon.isAuthorized(request,"Delete")==false)
        if(TTKCommon.isAuthorized(request,"Cancel")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(R00T_CANCEL_ICON,false);
            treeData.getTreeSetting().getChildNodeSetting().setIconVisible(CHILD_CANCEL_ICON,false);
        }// end of if(TTKCommon.isAuthorized(request,"Delete")==false)
        if(TTKCommon.isAuthorized(request,"DefineRule")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(ROOT_RULE_ICON,false);
            treeData.getTreeSetting().getChildNodeSetting().setIconVisible(MEMBER_RULE_ICON,false);
        }//end of if(TTKCommon.isAuthorized(request,"DefineRule")==false)
        if(TTKCommon.isAuthorized(request,"Renew Members")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(RENEW_ICON,false);
        }//end of if(TTKCommon.isAuthorized(request,"Renew Members")==false)
        log.debug("---------(Exit) inside check Tree permission --------------");
    }//end of checkTreePermission(HttpServletRequest request,TreeData treeData,String strSwitchType)

    private void documentViewer(HttpServletRequest request, MemberVO memberVO) throws TTKException
    {
        //Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");

        ArrayList<String> alMemberVO = new ArrayList<String>();
        alMemberVO.add("leftlink="+TTKCommon.getActiveLink(request));
        alMemberVO.add("policy_number="+WebBoardHelper.getPolicyNumber(request));
        if(memberVO != null){
            if(memberVO.getDMSRefID()!= null){
                alMemberVO.add("dms_reference_number="+memberVO.getDMSRefID());
            }//end of if(memberVO.getDMSRefID()!= null)
            else{
                alMemberVO.add("dms_reference_number="+null);
            }//end of else
        }
        else{
            alMemberVO.add("dms_reference_number="+null);
        }//end of else
        //toolbar.setDocViewParams(alhosvo);
        if(request.getSession().getAttribute("toolbar")!=null)
            ((Toolbar)request.getSession().getAttribute("toolbar")).setDocViewParams(alMemberVO);
    }//end of documentViewer(HttpServletRequest request)

    /**
     * Returns the PolicyManager session object for invoking methods on it.
     * @return policyManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private PolicyManager getPolicyManagerObject() throws TTKException
    {
        PolicyManager policyManager = null;
        try
        {
            if(policyManager == null)
            {
                InitialContext ctx = new InitialContext();
                policyManager = (PolicyManager) ctx.lookup("java:global/TTKServices/business.ejb3/PolicyManagerBean!com.ttk.business.enrollment.PolicyManager");
            }//end if(policyManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "policydetail");
        }//end of catch
        return policyManager;
    }//end getHospitalManagerObject()

    /**
     * Returns the PolicyManager session object for invoking methods on it.
     * @return policyManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private ValidationRuleManager getValidationRuleManagerObject() throws TTKException
    {
        ValidationRuleManager validationRuleManager = null;
        try
        {
            if(validationRuleManager == null)
            {
                InitialContext ctx = new InitialContext();
                validationRuleManager = (ValidationRuleManager) ctx.lookup("java:global/TTKServices/business.ejb3/ValidationRuleManagerBean!com.ttk.business.webservice.ValidationRuleManager");
            }//end if(policyManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "policydetail");
        }//end of catch
        return validationRuleManager;
    }//end getHospitalManagerObject()


    /**
     * This method returns the forward path for next view based on the Flow
     *
     * @param strActiveSubLink String current sublink
     * @return strForwardPath String forward path for the next view
     */
    private String getForwardPath(String strActiveSubLink)
    {
        String strForwardPath=null;

        if(strActiveSubLink.equals(strIndividualPolicy))
        {
            strForwardPath=strIndMemberDetails;

        }//end of if(strActiveSubLink.equals(strIndividualPolicy))
        else if(strActiveSubLink.equals(strIndPolicyasGroup))
        {
            strForwardPath=strGrpPolicyDetails;

        }//end of if(strActiveSubLink.equals(strIndPolicyasGroup))
        else if(strActiveSubLink.equals(strCorporatePolicy))
        {
            strForwardPath=strCorpMemberDetails;

        } //end of if(strActiveSubLink.equals(strCorporatePolicy))
        else if(strActiveSubLink.equals(strNonCorporatePolicy))
        {
            strForwardPath=strNonCorpMemberDetails;

        }//end of if(strActiveSubLink.equals(strNonCorporatePolicy))
        return strForwardPath;
    }//end of getForwardPath(String strActiveSubLink)


    /**
     * This method returns the forward path for next view based on the Flow
     *
     * @param strActiveSubLink String current sublink
     * @return strForwardPath String forward path for the next view
     */
    private String getMemberDetailPath(String strActiveSubLink)
    {
        String strForwardPath=null;

        if(strActiveSubLink.equals(strIndividualPolicy))
        {
            strForwardPath=strIndAddMemberDetails;

        }//end of if(strActiveSubLink.equals(strIndividualPolicy))
        else if(strActiveSubLink.equals(strIndPolicyasGroup))
        {
            strForwardPath=strGrpAddMemberDetails;

        }//end of if(strActiveSubLink.equals(strIndPolicyasGroup))
        else if(strActiveSubLink.equals(strCorporatePolicy))
        {
            strForwardPath=strCorAddMemberDetails;

        } //end of if(strActiveSubLink.equals(strCorporatePolicy))
        else if(strActiveSubLink.equals(strNonCorporatePolicy))
        {
            strForwardPath=strNonCorAddMemberDetails;

        }//end of if(strActiveSubLink.equals(strNonCorporatePolicy))
        return strForwardPath;
    }//end of getMemberDetailPath(String strActiveSubLink)


    /**
     * This method returns the Tree name to get the data from the session
     *
     * @param strActiveSubLink String current sublink
     * @return strForwardPath String forward path for the next view
     */
    private String getTreeName(String strActiveSubLink)
    {
        String strTreeName=null;

        if(strActiveSubLink.equals(strIndividualPolicy) ||strActiveSubLink.equals(strIndPolicyasGroup))
        {
            strTreeName="MemberTree";
        }//end of if(strActiveSubLink.equals(strIndividualPolicy))
        else if(strActiveSubLink.equals(strCorporatePolicy) ||strActiveSubLink.equals(strNonCorporatePolicy))
        {
            strTreeName="CorporateMemberTree";
        } //end of if(strActiveSubLink.equals(strCorporatePolicy))
        return strTreeName;
    }//end of getForwardPath(String strActiveSubLink)


    /**
     * This method returns the SubLink code based on the Policy flow
     *
     * @param strActiveSubLink String current sublink
     * @return strForwardPath String forward path for the next view
     */
    private String getSubLinkCode(String strActiveSubLink)
    {
        String strSubLinkCode=null;

        if(strActiveSubLink.equals(strIndividualPolicy))
        {
            strSubLinkCode="IP";
        }//end of if(strActiveSubLink.equals(strIndividualPolicy))
        else if(strActiveSubLink.equals(strIndPolicyasGroup))
        {
            strSubLinkCode="IG";
        }//end of if(strActiveSubLink.equals(strIndPolicyasGroup))
        else if(strActiveSubLink.equals(strCorporatePolicy))
        {
            strSubLinkCode="CP";
        } //end of if(strActiveSubLink.equals(strCorporatePolicy))
        else if(strActiveSubLink.equals(strNonCorporatePolicy))
        {
            strSubLinkCode="NC";
        }//end of if(strActiveSubLink.equals(strNonCorporatePolicy))
        return strSubLinkCode;
    }//end of getForwardPath(String strActiveSubLink)

    /**
     * This method returns the Policy Type Code based on the Flow
     *
     * @param strActiveSubLink String current sublink
     * @return strForwardPath String forward path for the next view
     */
    private String getPolicyTypeCode(String strActiveSubLink)
    {
        String strPolicyTypeCode=null;

        if(strActiveSubLink.equals(strIndividualPolicy))
        {
            strPolicyTypeCode="IND";
        }//end of if(strActiveSubLink.equals(strIndividualPolicy))
        else if(strActiveSubLink.equals(strIndPolicyasGroup))
        {
            strPolicyTypeCode="ING";
        }//end of if(strActiveSubLink.equals(strIndPolicyasGroup))
        else if(strActiveSubLink.equals(strCorporatePolicy))
        {
            strPolicyTypeCode="COR";
        } //end of if(strActiveSubLink.equals(strCorporatePolicy))
        else if(strActiveSubLink.equals(strNonCorporatePolicy))
        {
            strPolicyTypeCode="NCR";
        }//end of if(strActiveSubLink.equals(strNonCorporatePolicy))
        return strPolicyTypeCode;
    }//end of getForwardPath(String strActiveSubLink)
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
    
    public ActionForward doPrintCertificate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
     		HttpServletResponse response) throws TTKException{
    	 try
    	 {
    		 setOnlineLinks(request);
    		 log.info("Inside OnlineMemberAction doPrintCertificate");
    		 DynaActionForm frmOnlineDetails=(DynaActionForm)form;
     		MemberVO memberVO =null;
     		 TreeData treeData = TTKCommon.getTreeData(request);
     		frmOnlineDetails.set("selectedroot",TTKCommon.checkNull(request.getParameter("selectedroot")));
     		frmOnlineDetails.set("selectednode",TTKCommon.checkNull(request.getParameter("selectednode")));
     		String strSelectedRoot = (String)request.getParameter("strRootIndex");
 			String strSelectedNode = (String)request.getParameter("strNodeIndex");
    		 memberVO = (MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode);
    		 UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");

    		 Long memseqid=memberVO.getMemberSeqID();
    	//	Long memseqid=userSecurityProfile.getMemSeqID();    		
    		 JasperReport jasperReport,emptyReport;
    		 JasperPrint jasperPrint=null;
    		 ByteArrayOutputStream boas=null;
    		 String strPolicyGrSeqID = null;
    		 String jrxmlfile = "";

    		 strPolicyGrSeqID =memberVO.getPolicyGroupSeqID().toString();
    		 //strPolicyGrSeqID = userSecurityProfile.getPolicyGrpSeqID().toString();
				 try
				 {					
					 jrxmlfile = "providerLogin/MedicalCertificateNew.jrxml";
					 jasperReport = JasperCompileManager.compileReport(jrxmlfile);	 
	    			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			 HashMap hashMap = new HashMap();
	    			 long lngEmpty = 0;
	    			 boas=new ByteArrayOutputStream();
	    			 TTKReportDataSource ttkReportDataSource = new TTKReportDataSource("MedicialCertificate",strPolicyGrSeqID,memseqid,lngEmpty);
	    			 if(strPolicyGrSeqID!=null)
	    			 {	
	    				 ttkReportDataSource.getResultData().beforeFirst();
    					 hashMap.put("MyDatasource",new TTKReportDataSource("MedicialCertificate",strPolicyGrSeqID,memseqid,lngEmpty));
    					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);		     					 
	    			 } 
	    			 else{
	    				 jasperPrint = JasperFillManager.fillReport(emptyReport, hashMap,new JREmptyDataSource());
	    			 }//end of	
  			 
	    			 
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
	    		 return mapping.findForward(strReportdisplay);
	    	 }//end of try
	    	 catch(TTKException expTTK)
	    	 {
	    		 return this.processOnlineExceptions(request, mapping, expTTK);
	    	 }//end of catch(TTKException expTTK)
	    	 catch(Exception exp)
	    	 {
	    		 return this.processOnlineExceptions(request, mapping, new TTKException(exp, "groupdetail"));
	    	 }//end of catch(Exception exp)
	     }//end of doECards(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    
    
    
    
    /**
     * 
     * This method is used to navigate to previous screen .
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doCloseEnrollCancel(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);    	 
    		log.debug("inside doCloseEnrollCancel method of CancelEnrollmentAction");
    		return mapping.findForward(strCorpMemberDetails);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strCorpMemberDetails));
    	}//end of catch(Exception exp)
    }//end of method doCloseEnrollCancel(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception

    
    public ActionForward doSaveEnrollCancel(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);    	 
    		log.debug("inside doSaveEnrollCancel method of CancelEnrollmentAction");
    		DynaActionForm frmCancelEnrollment = (DynaActionForm)form;
    		// ArrayList<Object> alSearchParam = new ArrayList<Object>();
    		ArrayList alSearchParamRetrive=(ArrayList)request.getSession().getAttribute("alSearchParamRetrive");
    		MemberCancelVO memberCancelVO = (MemberCancelVO)FormUtils.getFormValues(frmCancelEnrollment,this,mapping,request);
    		//OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManagerObject();
    		  MemberManager memberManager=this.getMemberManager();
    		Long lngPolicyGroupSeqID = memberCancelVO.getPolicyGroupSeqID();
    		Long lngMemberSeqID = memberCancelVO.getMemberSeqID();
    		memberCancelVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
    		
    		if(memberCancelVO.getRelationTypeID().equals("NSF"))
    		{
    			ActionMessages actionMessages = new ActionMessages();
    			ActionMessage actionMessage=new ActionMessage("onlineforms.cancel.self");
    			actionMessages.add("global.error",actionMessage);
    			saveErrors(request,actionMessages);
    			return (mapping.findForward(strCancelEnroll));
    		}//end of if(memberCancelVO.getRelationTypeID().equals("NSF"))
    		
    		int i = memberManager.cancelMember(alSearchParamRetrive,memberCancelVO);
    		log.debug("Result value is :"+i);
    		request.setAttribute("updated","message.saved");
    		/*if(i>0)
    		{
    			request.setAttribute("updated","message.saved");
    			ArrayList<Long> alSelCanParams = new ArrayList<Long>();
    			alSelCanParams.add(lngPolicyGroupSeqID);
    			alSelCanParams.add(lngMemberSeqID);
    			MemberCancelVO memberCancelVO1 = memberManager.selectCancellation(alSelCanParams);
    			if(memberCancelVO==null)
    			{
    				memberCancelVO = new MemberCancelVO();
    			}//end of if(memberCancelVO==null)
    			memberCancelVO1.setPolicyGroupSeqID(memberCancelVO.getPolicyGroupSeqID());
    			memberCancelVO1.setMemberSeqID(memberCancelVO.getMemberSeqID());
    			memberCancelVO1.setPolicySeqID(memberCancelVO.getPolicySeqID());
    			DynaActionForm frmCancelEnroll1 = (DynaActionForm)FormUtils.setFormValues("frmCancelEnroll",memberCancelVO1,this,mapping,request);												
    			frmCancelEnroll1.set("caption",frmCancelEnroll.get("caption"));
    			request.setAttribute("frmCancelEnroll",frmCancelEnroll1);
    		}//end of if(i>0)
*/    		return this.getForward(strCancelEnroll, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strCancelEnroll));
    	}//end of catch(Exception exp)
    }//end of method doSaveEnrollCancel(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
    
    public ActionForward doViewMemberPolicyDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{

        	log.debug("inside the doViewMemberPolicyDetails() of MemberAction");
            setLinks(request);
            
            RuleVO ruleVO=null;
            Document ruleDocument = null;
            MemberManager memberManager=this.getMemberManager();
            DynaActionForm frmDefineRulesForMember=(DynaActionForm)form;
            TreeData treeData = TTKCommon.getTreeData(request);
            
            String strSelectedRoot = (String)request.getParameter("selectedroot");
            String strSelectedNode = (String)request.getParameter("selectednode");
            MemberVO memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode);
            ruleVO=memberManager.getMemPolicyRule(memberVO.getMemberSeqID());
            String Strforward="";    
           
            // 1 : data with active & inactive member
            if(ruleVO != null)
            {	 
              	 ruleDocument=ruleVO.getRuleDocument();
                 frmDefineRulesForMember = (DynaActionForm)FormUtils.setFormValues("frmDefineRulesForMember",ruleVO, this, mapping, request);
                 frmDefineRulesForMember.set("selectedroot", strSelectedRoot);
                 frmDefineRulesForMember.set("selectednode", strSelectedNode);
                 frmDefineRulesForMember.set("memberSeqID", memberVO.getMemberSeqID()==null?"":memberVO.getMemberSeqID().toString());
                 request.getSession().setAttribute("frmDefineRulesForMember",frmDefineRulesForMember);
                 request.getSession().setAttribute("RuleDocument",ruleDocument);
                 request.getSession().setAttribute("memberSeqID",memberVO.getMemberSeqID()==null?"":memberVO.getMemberSeqID().toString());
                 Strforward=strMemberPolicyRule;							// for existing Rule
            } 
           
            // 2 : no data & active member
            if(ruleVO == null && "N".equals(memberVO.getCancleYN()))
            {
            	frmDefineRulesForMember.set("selectedroot", strSelectedRoot);
                frmDefineRulesForMember.set("selectednode", strSelectedNode);
                frmDefineRulesForMember.set("memberSeqID", memberVO.getMemberSeqID()==null?"":memberVO.getMemberSeqID().toString());
            	request.getSession().setAttribute("frmDefineRulesForMember",frmDefineRulesForMember);
            	Strforward=strAddMemberPolicyRule;
            }
            
            // 3 : not data & not active 
            if(ruleVO == null && "Y".equals(memberVO.getCancleYN()))
            {
            	TTKException expTTK = new TTKException();
				expTTK.setMessage("error.Member.Cancelled");     
				throw expTTK;
				
            }
            return this.getForward(Strforward,mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }
    
    public ActionForward doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
            log.debug(" inside doAdd method of MemberAction...");
            setLinks(request);
            
            DynaActionForm frmDefineRulesForMember= (DynaActionForm)form;
            //initialize the Form bean and remove the unwanted data from the session
            String selectedroot = frmDefineRulesForMember.getString("selectedroot");
       	 	String selectednode = frmDefineRulesForMember.getString("selectednode");
       	 	String memberSeqID = frmDefineRulesForMember.getString("memberSeqID");
            
            frmDefineRulesForMember.initialize(mapping);
            request.getSession().removeAttribute("tableData");
            request.getSession().removeAttribute("RuleDocument");

            //While creating new Rule none of the Clause is selected
            String [] strSelectedClauses = null;
            Document baseRuleDocument=TTKCommon.getDocument("MasterBaseRules.xml"); //get the Master Base Rule
            request.setAttribute("Clauses",strSelectedClauses);
            request.getSession().setAttribute("BaseRuleDocument",baseRuleDocument);
            frmDefineRulesForMember.set("selectedroot",selectedroot);
            frmDefineRulesForMember.set("selectednode",selectednode);
            frmDefineRulesForMember.set("memberSeqID",memberSeqID);
            request.getSession().setAttribute("frmDefineRulesForMember",frmDefineRulesForMember);
            return this.getForward(strMemberProductClauseList,mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }
    
    private RuleManager getRuleManagerObject() throws TTKException
    {
        RuleManager ruleManager = null;
        try
        {
            if(ruleManager == null)
            {
                InitialContext ctx = new InitialContext();
                ruleManager = (RuleManager) ctx.lookup("java:global/TTKServices/business.ejb3/RuleManagerBean!com.ttk.business.administration.RuleManager");
                log.info("Inside RuleManager: RuleManager: " + ruleManager);
            }//end if(ruleManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "ruledata");
        }//end of catch
        return ruleManager;
    }//end of getRuleManagerObject()
    
    private ProductPolicyManager getProductPolicyManagerObject() throws TTKException
    {
        ProductPolicyManager productPolicyManager = null;
        try
        {
            if(productPolicyManager == null)
            {
                InitialContext ctx = new InitialContext();
                productPolicyManager = (ProductPolicyManager) ctx.lookup("java:global/TTKServices/business.ejb3/ProductPolicyManagerBean!com.ttk.business.administration.ProductPolicyManager");
                log.info("Inside ProductPolicyManager: productPolicyManager: " + productPolicyManager);
            }//end if(productPolicyManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "ProductManager");
        }//end of catch
        return productPolicyManager;
    }//end of getProductPolicyManagerObject()
    
    private String buildCaption(HttpServletRequest request)throws TTKException
    {
        StringBuffer sbfCaption=new StringBuffer();
        ProductPolicyManager productPolicyManagerObject=this.getProductPolicyManagerObject();
            PolicyDetailVO policyDetailVO= productPolicyManagerObject.getPolicyDetail(TTKCommon.getWebBoardId(request),TTKCommon.getUserSeqId(request));
            sbfCaption.append("[").append(policyDetailVO.getCompanyName()).append("]");
            sbfCaption.append("[").append(policyDetailVO.getPolicyNbr()).append("]");
            return sbfCaption.toString();
    }
    
    public ActionForward doCloseMember(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
        	log.info("Inside doCloseMember method of MemberAction");
            return this.getForward("cormemberdetails", mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }
    
    
    public ActionForward doMemberReconfigure(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
        	log.info("Inside doMemberReconfigure method of MemberAction");
        	setLinks(request);
        	RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();     //create the instance of Helper
            Document ruleDocument=(Document)request.getSession().getAttribute("RuleDocument");
            if(ruleDocument!=null)
            {
                ruleDocument=ruleXMLHelper.updateRuleDocument(ruleDocument,"EnrollMemRule",request);
            }//end of if(ruleDocument!=null)

            request.getSession().setAttribute("RuleDocument",ruleDocument);
            String [] selectedClauses = ruleXMLHelper.getClausesNodes(ruleDocument);
            request.setAttribute("Clauses",selectedClauses);

            Document baseRuleDocument=TTKCommon.getDocument("MasterBaseRules.xml");   //get the MasterBaseRule
            request.getSession().setAttribute("BaseRuleDocument",baseRuleDocument);
            return this.getForward(strMemberProductClauseList, mapping, request); 
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }
    
    public ActionForward doSaveMember(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
        	 log.info("Inside doSaveMember method of MemberAction");
        	 setLinks(request);
        	 
        	 DynaActionForm frmDefineRulesForMember= (DynaActionForm)form;
        	 String selectedroot = frmDefineRulesForMember.getString("selectedroot");
        	 String selectednode = frmDefineRulesForMember.getString("selectednode");
        	 String memberSeqID = frmDefineRulesForMember.getString("memberSeqID");
             RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();     //create the instance of Helper
             RuleVO ruleVO=null;
             
             Document ruleDocument=(Document)request.getSession().getAttribute("RuleDocument");
             ruleDocument=ruleXMLHelper.updateRuleDocument(ruleDocument,"EnrollMemRule",request);
             
             ruleVO=(RuleVO)FormUtils.getFormValues(frmDefineRulesForMember,this, mapping, request);
             ruleVO.setRuleDocument(ruleDocument);
             ruleVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
             long lngProdPolicyRuleSeqId=0;
             
             TreeData treeData = TTKCommon.getTreeData(request);
             MemberVO memberVO=(MemberVO)treeData.getSelectedObject(selectedroot,selectednode);
             
             RuleManager ruleManagerObject=this.getRuleManagerObject();
             ruleVO.setSeqID(ruleVO.getProdPolicyRuleSeqID());
             
             ruleVO.setMemberSeqID(Long.parseLong(memberSeqID));
             lngProdPolicyRuleSeqId=ruleManagerObject.saveProdPolicyRule(ruleVO,"MEM");						// save 1
             ruleManagerObject.saveProdPolicyRuleVals(ruleVO.getProdPolicyRuleSeqID(),"memberRule");		// save 2
             
             if(lngProdPolicyRuleSeqId>0)
             {
            	 ruleVO=ruleManagerObject.getProdPolicyRule(lngProdPolicyRuleSeqId,"MEM");
             }
             
             request.setAttribute("updated","Message saved Successfully.");
             frmDefineRulesForMember = (DynaActionForm)FormUtils.setFormValues("frmDefineRulesForMember",ruleVO,this,mapping,request);
             frmDefineRulesForMember.set("selectedroot",selectedroot);
             frmDefineRulesForMember.set("selectednode",selectednode);
             frmDefineRulesForMember.set("memberSeqID",memberSeqID);
             frmDefineRulesForMember.set("prodPolicyRuleSeqID",ruleVO.getProdPolicyRuleSeqID()==null?"":ruleVO.getProdPolicyRuleSeqID().toString());
             request.getSession().setAttribute("frmDefineRulesForMember",frmDefineRulesForMember);
             request.getSession().setAttribute("RuleDocument",ruleDocument);
             request.getSession().setAttribute("RuleDocumentReference",ruleDocument);
             return this.getForward(strMemberPolicyRule,mapping,request);		// for existing Rule
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
        }//end of catch(Exception exp)
    }
    
    
    
    
    
    
    
    public ActionForward viewPolicyRuleHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception
            {
        try{
            log.debug("MemberAction Action - inside viewPolicyRuleHistory method....");
            setLinks(request);
            TableData tableData=TTKCommon.getTableData(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            DynaActionForm frmDefineRules= (DynaActionForm)form;
            RuleManager ruleManagerObject=this.getRuleManagerObject();
            Document ruleDocument=null;
            StringBuffer sbfCaption=new StringBuffer();

            //get the caption to be displayed
          //  sbfCaption.append(buildCaption(strActiveSubLink,request));

            if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            {
                RuleVO ruleVO=(RuleVO)tableData.getRowInfo(Integer.parseInt((String)(request).getParameter("rownum")));
                	
                	ruleDocument=ruleVO.getRuleDocument();
                    frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO,this,mapping,request);
                   // frmDefineRules.set("caption",sbfCaption.toString());
                    request.getSession().setAttribute("frmDefineRules",frmDefineRules);
                    request.getSession().setAttribute("RuleDocument",ruleDocument);
                }
            	 return this.getForward("PolicyRuleHistory",mapping,request); 
               
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }
   
    
    
    
    public ActionForward getPolicyRuleHistoryList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception
            {
        try{
            log.debug("MemberAction Action - inside getPolicyRuleHistoryList method....");
            setLinks(request);
            TableData tableData=TTKCommon.getTableData(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            DynaActionForm frmDefineRules= (DynaActionForm)form;
            RuleManager ruleManagerObject=this.getRuleManagerObject();
            Document ruleDocument=null;
            StringBuffer sbfCaption=new StringBuffer();
           // sbfCaption.append(buildCaption(strActiveSubLink,request));

            String prodPoliccyRuleSeqId = frmDefineRules.getString("prodPolicyRuleSeqID");
            
            if(prodPoliccyRuleSeqId==""||prodPoliccyRuleSeqId==null){
            	
            	TTKException expTTK = new TTKException();
				expTTK.setMessage("error.Member.Cancelled");     
				throw expTTK;
            }
            
            
            
            long lngProdPoliccyRuleSeqId=0;
            if(prodPoliccyRuleSeqId!=null){
            	lngProdPoliccyRuleSeqId=Long.parseLong(prodPoliccyRuleSeqId);  
            }
            
                RuleVO ruleVO = new RuleVO();
                ruleVO.setProdPolicyRuleSeqID(lngProdPoliccyRuleSeqId);
                
                //ruleVO=ruleManagerObject.getPolicyRuleHistory(ruleVO.getProdPolicyRuleSeqID(),"R");
              //  ArrayList alProductRule=ruleManagerObject.getPolicyRuleHistoryList(TTKCommon.getWebBoardId(request)); 
                ArrayList alProductRule=ruleManagerObject.getPolicyRuleHistory(ruleVO.getProdPolicyRuleSeqID(),"R");
                
                
                tableData = new TableData();
                tableData.createTableInfo("ProductRulesTable",null);
                tableData.setData(alProductRule,"search");
                ((Column) ((ArrayList)tableData.getTitle()).get(0)).setVisibility(false);
                ((Column) ((ArrayList)tableData.getTitle()).get(1)).setVisibility(false);
                request.getSession().setAttribute("tableData",tableData);
              //  frmDefineRules.set("caption",sbfCaption.toString());
                return this.getForward("policyRulesList",mapping,request);
                
              /*  ruleDocument=ruleVO.getRuleDocument();
                frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO,this,mapping,request);
                frmDefineRules.set("caption",sbfCaption.toString());
                request.getSession().setAttribute("frmDefineRules",frmDefineRules);
                request.getSession().setAttribute("RuleDocument",ruleDocument);*/
            
            
            
          //  return this.getForward("PolicyRuleHistory",mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }   
    
    
    
    
    public ActionForward onHistoryClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception
            {
    	
    	 log.debug("MemberAction Action - inside onHistoryClose method....");
         setLinks(request);
         TableData tableData=TTKCommon.getTableData(request);
         String strActiveSubLink=TTKCommon.getActiveSubLink(request);
         DynaActionForm frmDefineRules= (DynaActionForm)form;
         StringBuffer sbfCaption=new StringBuffer();
         RuleVO ruleVO=null;
         Document ruleDocument = null;
         MemberManager memberManager=this.getMemberManager();
         TreeData treeData = TTKCommon.getTreeData(request);
         String strSelectedRoot = (String)request.getParameter("selectedroot");
         String strSelectedNode = (String)request.getParameter("selectednode");
         MemberVO memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode);
         String memberSeqId =(String) request.getAttribute("memberSeqID");	
         String memberSeqId1 = frmDefineRules.getString("memberSeqID");
         
         Long memberId = Long.parseLong(memberSeqId1);
         
         
        // memberVO.setMemberSeqID(memberId);
         
         ruleVO=memberManager.getMemPolicyRule(memberId);
         ruleDocument=ruleVO.getRuleDocument();
         request.getSession().setAttribute("RuleDocument",ruleDocument);
    	 return this.getForward("policyRulesList",mapping,request);
    	
    	
            } 
    
    
    //doCloseMemberRuleHistory
       
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    
    public ActionForward doAddMDF(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
       	 String strForwardPath = null;
       	log.info("Inside doAddMDF method of MemberAction");
           DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
           String strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
           this.setLinks(request,strSwitchType);
           TreeData treeData = TTKCommon.getTreeData(request);
           String strActiveSubLink=TTKCommon.getActiveSubLink(request);
           String strSubLinkCode=getSubLinkCode(strActiveSubLink);
           DynaActionForm frmMember=(DynaActionForm)form;
           DynaActionForm frmAddMdfMember=(DynaActionForm)form;

           MemberManager memberManager=this.getMemberManager();
           MemberVO memberVO = null;
           String strCaption="["+TTKCommon.checkNull(WebBoardHelper.getPolicyNumber(request))+"]";
           int iSelectedRoot = Integer.parseInt((String)frmMember.get("selectedroot"));
           //create the required tree
           ArrayList<Object> alSearchParam = new ArrayList<Object>();
           memberVO = ((MemberVO)treeData.getRootData().get(iSelectedRoot));

           //create search parameter to get the dependent list
           alSearchParam.add(memberVO.getPolicyGroupSeqID());
           alSearchParam.add(strSwitchType);
           alSearchParam.add(strSubLinkCode);
          
           //get the dependent list from database
      	ArrayList<MemberVO> alNodeMembers=memberManager.getDependentList(alSearchParam);
      	ArrayList<CacheObject> alMembersList	=	new ArrayList<>();
      	CacheObject cacheObject					=	null;
      	for(MemberVO vo : alNodeMembers)
      	{
      		cacheObject	=	new CacheObject();
      		cacheObject.setCacheId(vo.getEnrollmentID()+"-"+vo.getMemberSeqID().toString());
      		cacheObject.setCacheDesc(vo.getName());
      		alMembersList.add(cacheObject);
      	}
      		
		 if(alNodeMembers!=null && alNodeMembers.size()>0)
        {
        	frmAddMdfMember.set("membersDetailsInfo",(MemberVO[])alNodeMembers.toArray(new MemberVO[0]));
        }
		 
		 
		 //GET ADDED MDF FILES FROM TABLE
		 ArrayList<MemberVO> alUploadedMdfFiles	=	new ArrayList<>();
		 alUploadedMdfFiles	=	memberManager.getUploadedMdfFiles(memberVO);
		 
           strForwardPath="addmdf";//getForwardPath(strActiveSubLink);
           frmAddMdfMember.set("caption","Policy No: "+strCaption);
           frmAddMdfMember.set("enrollmentIds",alMembersList);
           request.getSession().setAttribute("alUploadedMdfFiles", alUploadedMdfFiles);
           request.getSession().setAttribute("memberVOforMDF", memberVO);
           request.getSession().setAttribute("frmAddMdfMember",frmAddMdfMember);
           return this.getForward(strForwardPath, mapping, request);
       }//end of try
       catch(TTKException expTTK)
       {
           return this.processExceptions(request, mapping, expTTK);
       }//end of catch(TTKException expTTK)
       catch(Exception exp)
       {
           return this.processExceptions(request, mapping, new TTKException(exp,"addmdf"));
       }//end of catch(Exception exp)
   }
    
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    
public ActionForward doUploadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
    try{
    	log.info("Inside doUploadFiles method of MemberAction");
       this.setLinks(request);
       DynaActionForm frmAddMdfMember=(DynaActionForm)form;
       String[] enrollmentIDs=request.getParameterValues("enrollmentID");
       String[] fileNames=request.getParameterValues("fileName");
       String[] memberSeqIDs=request.getParameterValues("memberSeqID");
       
       MemberManager memberManager=this.getMemberManager();
       String enrollmentId	=	request.getParameter("enrollmentId");
       String tempArr[]		=	new String[2];
       ArrayList<Object> alAddMdfParamsList	=	new ArrayList<>();
       FormFile formFile	=	(FormFile) frmAddMdfMember.get("files");
    // fileName=toDayDate+"-"+TTKCommon.getUserSeqId(request)+"-"+formFile.getFileName();
       FileWriter fileWriter=	null;
       FileOutputStream outputStream = null;
       String fileName=formFile.getFileName();
       String fileExtn	=	"";
       if(formFile.getFileSize()!=0)
		{
			if((formFile.getFileSize()/(1024*1024)<2)){//&& (formFile.getFileSize()/(500)>500)
				fileExtn = TTKCommon.GetFileExtension(formFile.toString());
				fileExtn=fileExtn.toLowerCase();
					if(!"pdf".equals(fileExtn)&&!"png".equals(fileExtn)&&!"jpeg".equals(fileExtn)&&!"jpg".equals(fileExtn)){
						TTKException expTTK = new TTKException();
						expTTK.setMessage("error.file.employee.restricted.format");
						throw expTTK;
					}
			}else{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.mdffile.size.issue");
				throw expTTK;
			}
			
		}
		else
		{
			TTKException expTTK = new TTKException();
			expTTK.setMessage("error.documents.required");
			throw expTTK;
		}
		    	  
      String fileDir=TTKPropertiesReader.getPropertyValue("member.mdf.fileUpload");
		      
		     File file= new File(fileDir);
		    
		     if(!file.exists())file.mkdirs();
		     
		     String strFinalFileNameWithDate=new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+fileName;
		     outputStream = new FileOutputStream(fileDir+"/"+strFinalFileNameWithDate);
		     
		      //outputStream = new FileOutputStream(fileDir+"/"+fileName);
		      outputStream.write(formFile.getFileData());
		      if (outputStream != null)  outputStream.close();
		      
		  tempArr	=	enrollmentId.split("-");
	      alAddMdfParamsList.add(tempArr[1]);
   		   alAddMdfParamsList.add(tempArr[0]);
   		   alAddMdfParamsList.add(fileName);
   		   alAddMdfParamsList.add(strFinalFileNameWithDate);
   		   alAddMdfParamsList.add(TTKCommon.getUserSeqId(request));
       
       int iUpdate	=	memberManager.uploadMemberMdfFiles(alAddMdfParamsList);
       if(iUpdate>0)
    	   request.setAttribute("updated", "message.addedSuccessfully");
       request.getSession().setAttribute("frmAddMdfMember",frmAddMdfMember);
       
       //GETTING THE MDF UPLOADED FILES FROM DB
       MemberVO memberVOforMDF	=	(MemberVO) request.getSession().getAttribute("memberVOforMDF");
       //GET ADDED MDF FILES FROM TABLE
		ArrayList<MemberVO> alUploadedMdfFiles	=	new ArrayList<>();
		alUploadedMdfFiles	=	memberManager.getUploadedMdfFiles(memberVOforMDF);
		request.getSession().setAttribute("alUploadedMdfFiles",alUploadedMdfFiles);
		 
       
       return this.getForward("addmdf", mapping, request);
   }//end of try
   catch(TTKException expTTK)
   {
       return this.processExceptions(request, mapping, expTTK);
   }//end of catch(TTKException expTTK)
   catch(Exception exp)
   {
       return this.processExceptions(request, mapping, new TTKException(exp,"addmdf"));
   }//end of catch(Exception exp)
   }
        

public ActionForward doViewMdfFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
	
	ByteArrayOutputStream baos=null;
	OutputStream sos = null;
	FileInputStream fis= null; 
	File file = null;
	BufferedInputStream bis =null;
	try{
    	log.info("Inside doViewMdfFile method of MemberAction");
       this.setLinks(request);
       
       
       
       
       String fileName	=	request.getParameter("fileName");
       String fileExtn	=	TTKCommon.GetFileExtension(fileName);
       fileExtn	=	fileExtn.toLowerCase();
       String strFileFullName = TTKPropertiesReader.getPropertyValue("member.mdf.fileUpload")+fileName;
       //response.setContentType("application/txt");
       if("pdf".equals(fileExtn))
    	   response.setContentType("application/pdf");
       response.setHeader("Content-Disposition", "attachment;filename="+fileName);

		file = new File(strFileFullName);
		fis = new FileInputStream(file);
		bis = new BufferedInputStream(fis);
		baos=new ByteArrayOutputStream();
		int ch;
		while ((ch = bis.read()) != -1) baos.write(ch);
		sos = response.getOutputStream();
		baos.writeTo(sos);  
		baos.flush();      
		sos.flush(); 
         
       return null;
   }//end of try
   catch(TTKException expTTK)
   {
       return this.processExceptions(request, mapping, expTTK);
   }//end of catch(TTKException expTTK)
   catch(Exception exp)
   {
       return this.processExceptions(request, mapping, new TTKException(exp,"addmdf"));
   }//end of catch(Exception exp)
	finally {
			if(baos!=null)baos.close();                                           
			if(sos!=null)sos.close();
			if(bis!=null)bis.close();
			if(fis!=null)fis.close();
	}
	
}


public ActionForward doDeleteMdfFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
    try{
    	log.info("Inside doDeleteMdfFile method of MemberAction");
       this.setLinks(request);
       DynaActionForm frmAddMdfMember=(DynaActionForm)form;
       
       MemberManager memberManager=this.getMemberManager();
       String memSeqId	=	request.getParameter("memSeqId");
       String mdfSeqId	=	request.getParameter("mdfSeqId");
       
       
       int iUpdate	=	memberManager.deleteMemberMdfFiles(memSeqId,mdfSeqId,TTKCommon.getUserSeqId(request));
       if(iUpdate>0)
    	   request.setAttribute("updated", "message.fileDeletedSuccessfully");
       
       	//GETTING THE MDF UPLOADED FILES FROM DB
       	MemberVO memberVOforMDF	=	(MemberVO) request.getSession().getAttribute("memberVOforMDF");
       	//GET ADDED MDF FILES FROM TABLE
		ArrayList<MemberVO> alUploadedMdfFiles	=	new ArrayList<>();
		alUploadedMdfFiles	=	memberManager.getUploadedMdfFiles(memberVOforMDF);
		request.getSession().setAttribute("alUploadedMdfFiles",alUploadedMdfFiles);
		
       request.getSession().setAttribute("frmAddMdfMember",frmAddMdfMember);
       return this.getForward("addmdf", mapping, request);
   }//end of try
   catch(TTKException expTTK)
   {
       return this.processExceptions(request, mapping, expTTK);
   }//end of catch(TTKException expTTK)
   catch(Exception exp)
   {
       return this.processExceptions(request, mapping, new TTKException(exp,"addmdf"));
   }//end of catch(Exception exp)
}

    


public ActionForward doViewMemberMdfFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{

	ByteArrayOutputStream baos=null;
	OutputStream sos = null;
	FileInputStream fis= null; 
	File file = null;
	BufferedInputStream bis =null;
	
    try{
    	log.info("Inside doViewMemberMdfFile method of MemberAction");
        DynaActionForm frmPolicyList=(DynaActionForm)request.getSession().getAttribute("frmPolicyList");
        String strSwitchType		=	"";
        String patOrClmFlag	=	request.getParameter("patOrClmFlag");
        if(patOrClmFlag==null){//FROM ADD / VIEW MDF
        	strSwitchType=TTKCommon.checkNull((String)frmPolicyList.get("switchType"));
            this.setLinks(request,strSwitchType);
        }
        else//FROM PREAUTH OR CLAIM - KISHOR KUMAR S H
            this.setLinks(request);

        TreeData treeData = TTKCommon.getTreeData(request);
        Long lngSeqID = null;

        MemberVO memberVO= new MemberVO();
        MemberManager memberManager=this.getMemberManager();

        String strSelectedRoot = (String)request.getParameter("selectedroot");
        String strSelectedNode = (String)request.getParameter("selectednode");
        treeData.setSelectedRoot(-1);   //sets the selected rows
        memberVO=(MemberVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode); // from tree with with min data
        

        //THIS SECTION WORKS FROM PREAUTH VIEW AND CLAIM VIEW, USING SAME METHOD TO VIEW THE FILES S T A R T S
    	String memberSeqID	=	request.getParameter("memberSeqID");
    	if(("YES").equals(patOrClmFlag)){
    		memberVO	=	new MemberVO();
    		memberVO.setMemberSeqID(new Long(memberSeqID));
    	}
    	//THIS SECTION WORKS FROM PREAUTH VIEW AND CLAIM VIEW, USING SAME METHOD TO VIEW THE FILES E N D S
    	else
    		memberVO.setPolicyGroupSeqID(lngSeqID);
    	
        ArrayList<MemberVO> alUploadedMdfFiles	=	new ArrayList<>();
		alUploadedMdfFiles	=	memberManager.getUploadedMdfFiles(memberVO);
		request.getSession().setAttribute("alUploadedMdfFiles",alUploadedMdfFiles);
		
		String fileName	=	"FileNotUploaded.png";
		String fileExtn	=	TTKCommon.GetFileExtension(fileName);
		fileExtn	=	fileExtn.toLowerCase();
       System.out.println("size:"+alUploadedMdfFiles.size());
		if(alUploadedMdfFiles.size()==0)
		{
			String strFileFullName = TTKPropertiesReader.getPropertyValue("member.mdf.fileUpload")+fileName;
		       //response.setContentType("application/txt");
		       if("pdf".equals(fileExtn))
		    	   response.setContentType("application/pdf");
		       response.setHeader("Content-Disposition", "attachment;filename="+fileName);

				file = new File(strFileFullName);
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				baos=new ByteArrayOutputStream();
				int ch;
				while ((ch = bis.read()) != -1) baos.write(ch);
				sos = response.getOutputStream();
				baos.writeTo(sos);  
				baos.flush();      
				sos.flush(); 
		}else{
		
			MemberVO memvo	=	alUploadedMdfFiles.get(0);
			fileName	=	memvo.getStrFamilyName();
	       fileExtn	=	TTKCommon.GetFileExtension(fileName);
	       fileExtn	=	fileExtn.toLowerCase();
	       
	       String strFileFullName = TTKPropertiesReader.getPropertyValue("member.mdf.fileUpload")+fileName;
	       //response.setContentType("application/txt");
	       if("pdf".equals(fileExtn))
	    	   response.setContentType("application/pdf");
	       response.setHeader("Content-Disposition", "attachment;filename="+fileName);

			file = new File(strFileFullName);
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			baos=new ByteArrayOutputStream();
			int ch;
			while ((ch = bis.read()) != -1) baos.write(ch);
			sos = response.getOutputStream();
			baos.writeTo(sos);  
			baos.flush();      
			sos.flush(); 
		}
	       return null;
    }//end of try
    catch(TTKException expTTK)
    {
        return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
        return this.processExceptions(request, mapping, new TTKException(exp,"groupdetail"));
    }//end of catch(Exception exp)
}//end of doApproveCard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  //  strMemberPolicyRule
    
    public ActionForward doCloseMemberRuleHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception
            {
    	
    	 log.debug("MemberAction Action - inside onHistoryClose method....");
         setLinks(request);
         TableData tableData=TTKCommon.getTableData(request);
         String strActiveSubLink=TTKCommon.getActiveSubLink(request);
         DynaActionForm frmDefineRules= (DynaActionForm)form;
         StringBuffer sbfCaption=new StringBuffer();
             //  sbfCaption.append(buildCaption(strActiveSubLink,request));
       //  request.getSession().setAttribute("tableData",tableData);
       //  frmDefineRules.set("caption",sbfCaption.toString());
    	 return this.getForward(strMemberPolicyRule,mapping,request);
    	
    	
            } 
    
    
    
}//end of class MemberAction

