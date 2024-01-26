/**
 * @ (#) RuleAction.java Jul 8, 2006
 * Project      : TTK HealthCare Services
 * File         : RuleAction.java
 * Author       : Arun K N
 * Company      : Span Systems Corporation
 * Date Created : Jul 8, 2006
 *
 * @author       :  Arun K N
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.administration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.dom4j.Document;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.business.administration.RuleManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.common.security.RuleXMLHelper;
import com.ttk.dto.administration.ProductVO;
import com.ttk.dto.administration.RuleVO;
import com.ttk.dto.displayOfBenefits.BenefitsDetailsVO;
import com.ttk.dto.displayOfBenefits.DisplayBenefitsVO;
import com.ttk.dto.displayOfBenefits.ListDisplayBenefitsVO;
import com.ttk.dto.enrollment.PolicyDetailVO;
import com.ttk.dto.preauth.PreAuthDetailVO;

import formdef.plugin.util.FormUtils;

public class RuleAction extends TTKAction {
    private static Logger log = Logger.getLogger(RuleAction.class); // Getting Logger for this Class file

    //declaration of forward paths
    private static final String strProductClauseList="productclauselist";
    private static final String strPolicyClauseList="policyclauselist";
    private static final String strProductRulelist="productrulelist";
    private static final String strPolicyRule="policyrule";
    private static final String strProductrule="productrule";
    private static final String strProductRuleVerification="productruleverification";
    private static final String strPolicyRuleVerification="policyruleverification";
    private static final String strFailure="failure";
    private static final String strMemberPolicyRule="MemberPolicyRule";

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
            log.debug("RuleAction Action - inside doDefault method....");
            setLinks(request);
            StringBuffer sbfCaption=new StringBuffer();
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            TableData tableData=TTKCommon.getTableData(request);
            Document ruleDocument = null;       //refernce to Rule which will be processed for Product/Policy
            RuleVO ruleVO=null;
            RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();     //create the instance of Helper
            //check for the web board ID
            if(TTKCommon.getWebBoardId(request)==null)
            {
                TTKException expTTK = new TTKException();
                if(strActiveSubLink.equals("Products"))
                {
                    expTTK.setMessage("error.product.required");
                }//end of if(strActiveSubLink.equals("Products"))
                else if(strActiveSubLink.equals("Policies"))
                {
                    expTTK.setMessage("error.policy.required");
                }//end of else if(strActiveSubLink.equals("Policies"))
                throw expTTK;
            }//end of if(TTKCommon.getWebBoardId(request)==null)

            //load the Display nodes to Application scope
            ServletContext sc=servlet.getServletContext();
            HashMap hmDisplayNodes=null;
            HashMap hmCopayResultNodes=null;
            if(sc.getAttribute("RULE_DISPLAY_NODES")==null)
            {
                hmDisplayNodes=ruleXMLHelper.loadDisplayNodes(TTKCommon.getDocument("MasterBaseRules.xml"));
                sc.setAttribute("RULE_DISPLAY_NODES",hmDisplayNodes);
            }//end of if(sc.getAttribute("RULE_DISPLAY_NODES")==null)
            else
            {
                hmDisplayNodes=(HashMap)sc.getAttribute("RULE_DISPLAY_NODES");
            }//end of else
            
            if(sc.getAttribute("RULE_COPAY_RESULT_NODES")==null)
            {
            	hmCopayResultNodes=ruleXMLHelper.loadDisplayNodes(TTKCommon.getDocument("MasterBaseRules.xml"));
                sc.setAttribute("RULE_COPAY_RESULT_NODES",hmCopayResultNodes);
            }//end of if(sc.getAttribute("RULE_COPAY_RESULT_NODES")==null)
            else
            {
            	hmCopayResultNodes=(HashMap)sc.getAttribute("RULE_COPAY_RESULT_NODES");
            }//end of else
            
            //remove the unwanted data from the session
            request.getSession().removeAttribute("tableData");
            request.getSession().removeAttribute("RuleDocument");

            RuleManager ruleManagerObject=this.getRuleManagerObject();

            //get the caption to be displayed
            sbfCaption.append(buildCaption(strActiveSubLink,request));
            
            if(TTKCommon.getActiveSubLink(request).equals("Products"))
            {
                DynaActionForm frmRules= (DynaActionForm)form;
                //get the Rules for the Product
                ArrayList alProductRule=ruleManagerObject.getProductRuleList(TTKCommon.getWebBoardId(request));

                //create the tableData for the Rules
                tableData = new TableData();
                tableData.createTableInfo("ProductRulesTable",null);
                tableData.setData(alProductRule,"search");
                ((Column) ((ArrayList)tableData.getTitle()).get(2)).setVisibility(false);
                ((Column) ((ArrayList)tableData.getTitle()).get(3)).setVisibility(false);
                request.getSession().setAttribute("tableData",tableData);
                frmRules.set("caption",(sbfCaption.substring(0, sbfCaption.indexOf("[", 2))).toString());
                return this.getForward(strProductRulelist,mapping,request);
            }//end of if(TTKCommon.getActiveSubLink(request).equals("Products"))
            else if(TTKCommon.getActiveSubLink(request).equals("Policies"))
            {
                DynaActionForm frmDefineRules= (DynaActionForm)form;
                //call the DAO to get the current policyRule
                ruleVO=ruleManagerObject.getProdPolicyRule(TTKCommon.getWebBoardId(request),"P");
                if(ruleVO==null)
                {
                    TTKException expTTK = new TTKException();
                    expTTK.setMessage("error.administration.policyrule.notdefined");
                    throw expTTK;
                }//end of if(ruleVO==null)
                ruleDocument=ruleVO.getRuleDocument();
                frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO, this, mapping, request);
                //sbfCaption.append("[").append(policyDetailVO.getPolicyNbr()).append("]");   //build caption
               
                frmDefineRules.set("seqId",""+TTKCommon.getWebBoardId(request));
                frmDefineRules.set("caption",(sbfCaption.substring(0, sbfCaption.indexOf("[", 2))).toString());
                request.getSession().setAttribute("frmDefineRules",frmDefineRules);
                request.getSession().setAttribute("RuleDocument",ruleDocument);
                return this.getForward(getRulePath(strActiveSubLink),mapping,request);
            }//end of else if(TTKCommon.getActiveSubLink(request).equals("Policies"))
            else
            {
                //forward to failure page
                return (mapping.findForward(strFailure));
            }//end of else
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
                log.debug("RuleAction Action - inside doChangeWebBoard method....");
                return doDefault(mapping,form,request,response);
    }//end of doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
            log.debug("RuleAction Action - inside doAdd method....");
            setLinks(request);
            DynaActionForm frmDefineRules= (DynaActionForm)form;
            StringBuffer sbfCaption=new StringBuffer();
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);

            //initialize the Form bean and remove the unwanted data from the session
            frmDefineRules.initialize(mapping);
            request.getSession().removeAttribute("tableData");
            request.getSession().removeAttribute("RuleDocument");

            //get the caption to be displayed
            sbfCaption.append(buildCaption(strActiveSubLink,request));

            RuleVO ruleVO=new RuleVO();
            ruleVO.setProdPolicySeqID(TTKCommon.getWebBoardId(request));
            frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO,this, mapping, request);

            frmDefineRules.set("caption",sbfCaption.toString());
            request.getSession().setAttribute("frmDefineRules",frmDefineRules);

            //While creating new Rule none of the Clause is selected
            String [] strSelectedClauses = null;
            Document baseRuleDocument=TTKCommon.getDocument("MasterBaseRules.xml"); //get the Master Base Rule
            request.setAttribute("Clauses",strSelectedClauses);
            request.getSession().setAttribute("BaseRuleDocument",baseRuleDocument);
            return this.getForward(strProductClauseList,mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to navigate to detail screen to view/edit selected record.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doViewRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try{
            log.debug("RuleAction Action - inside doViewRule method....");
            setLinks(request);
            TableData tableData=TTKCommon.getTableData(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            DynaActionForm frmDefineRules= (DynaActionForm)form;
            RuleManager ruleManagerObject=this.getRuleManagerObject();
            Document ruleDocument=null;
            StringBuffer sbfCaption=new StringBuffer();

            //get the caption to be displayed
            sbfCaption.append(buildCaption(strActiveSubLink,request));

            if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            {
                RuleVO ruleVO=(RuleVO)tableData.getRowInfo(Integer.parseInt((String)(request).getParameter("rownum")));

                //call the DAO to get the Rule Details for the selected Rule
                ruleVO=ruleManagerObject.getProdPolicyRule(ruleVO.getProdPolicyRuleSeqID(),"R");
                ruleDocument=ruleVO.getRuleDocument();
                frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO,this,mapping,request);
                frmDefineRules.set("caption",sbfCaption.toString());
                request.getSession().setAttribute("frmDefineRules",frmDefineRules);
                request.getSession().setAttribute("RuleDocument",ruleDocument);
            }//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            return this.getForward(strProductrule,mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doViewRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
            DynaActionForm frmDefineRules= (DynaActionForm)form;
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();     //create the instance of Helper
            RuleVO ruleVO=null;

            //get the xml rule document from the session and update the it with user entered data
            Document ruleDocument=(Document)request.getSession().getAttribute("RuleDocument");
            ruleDocument=ruleXMLHelper.updateRuleDocument(ruleDocument,null,request);
            
           // System.out.println("Rule Document  ::"+ruleDocument.asXML().toString());
            
            ruleVO=(RuleVO)FormUtils.getFormValues(frmDefineRules,this, mapping, request);
            ruleVO.setRuleDocument(ruleDocument);
            ruleVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
            long lngProdPolicyRuleSeqId=0;

            StringBuffer sbfCaption=new StringBuffer();             //get the caption to be displayed
            sbfCaption.append(buildCaption(strActiveSubLink,request));

            RuleManager ruleManagerObject=this.getRuleManagerObject();

            if(ruleVO.getProdPolicyRuleSeqID()!=null)
            {
                //for updating the rule set Seq id to rule seq id and identifier to R
                ruleVO.setSeqID(ruleVO.getProdPolicyRuleSeqID());
                lngProdPolicyRuleSeqId=ruleManagerObject.saveProdPolicyRule(ruleVO,"R");
                Long ProdPolicyRuleSeqID=ruleVO.getProdPolicyRuleSeqID();
                ruleManagerObject.saveProdPolicyRuleVals(ProdPolicyRuleSeqID,"prodPolicyRule");
            }//end of if(ruleVO.getProdPolicyRuleSeqID()!=null)
            else
            {
                //for inserting the rule set Seq id to Product Policy seq id and identifier to P
                ruleVO.setSeqID(TTKCommon.getWebBoardId(request));
                lngProdPolicyRuleSeqId=ruleManagerObject.saveProdPolicyRule(ruleVO,"P");
            }//end of else

            if(lngProdPolicyRuleSeqId>0)
            {
                if(ruleVO.getProdPolicyRuleSeqID()!=null)    //requery after updating the rule
                {
                    ruleVO=ruleManagerObject.getProdPolicyRule(lngProdPolicyRuleSeqId,"R");
                    request.setAttribute("updated","message.savedSuccessfully");
                  Document ruleDoc =   ruleVO.getRuleDocument();
                //  System.out.println("ruleDoc ::"+ruleDoc.asXML().toString());
                }//end of if(ruleVO.getProdPolicyRuleSeqID()!=null)
                else                                         //requery after inserting rule
                {
                    ruleVO=ruleManagerObject.getProdPolicyRule(lngProdPolicyRuleSeqId,"R");
                    request.setAttribute("updated","message.addedSuccessfully");
                }//end of else
            }//end of if(lngProdPolicyRuleSeqId>0)

            frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO,this,mapping,request);
           /* if(strActiveSubLink.equals("Products"))
            {
                sbfCaption.append("[").append(productVO.getProductName()).append("]");
            }
            else
            {
                sbfCaption.append("[").append(policyDetailVO.getPolicyNbr()).append("]");
            }*/
            frmDefineRules.set("seqId",ruleVO.getProdPolicyRuleSeqID().toString());
            frmDefineRules.set("caption",sbfCaption.toString());
            request.getSession().setAttribute("frmDefineRules",frmDefineRules);
            request.getSession().setAttribute("RuleDocument",ruleDocument);
            return this.getForward(getRulePath(strActiveSubLink),mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
        log.debug("RuleAction Action - inside doClose method....");
        return doDefault(mapping,form,request,response);
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to navigate to Reconfigure screen to select/deselect the clauses
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doReconfigure(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {

            log.debug("RuleAction Action - inside doClose method....");
            setLinks(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            //DynaActionForm frmClauseList=(DynaActionForm)form;
            RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();     //create the instance of Helper
            Document ruleDocument=(Document)request.getSession().getAttribute("RuleDocument");
            if(ruleDocument!=null)
            {
                ruleDocument=ruleXMLHelper.updateRuleDocument(ruleDocument,null,request);
            }//end of if(ruleDocument!=null)

            request.getSession().setAttribute("RuleDocument",ruleDocument);
            String [] selectedClauses = ruleXMLHelper.getClausesNodes(ruleDocument);
            request.setAttribute("Clauses",selectedClauses);

            Document baseRuleDocument=TTKCommon.getDocument("MasterBaseRules.xml");   //get the MasterBaseRule
            request.getSession().setAttribute("BaseRuleDocument",baseRuleDocument);

            return this.getForward(getClauseListPath(strActiveSubLink),mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doReconfigure(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to navigate to Reconfigure screen to select/deselect the clauses
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doContinueRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
            log.debug("RuleAction Action - inside doContinueRule method....");
            setLinks(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            DynaActionForm frmDefineRules= (DynaActionForm)form;
            RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();     //create the instance of Helper
            //RuleDocument present in database/session memory
            Document ruleDocument=(Document)request.getSession().getAttribute("RuleDocument");
            String[] strSelectedClauses=request.getParameterValues("chkbox");

            //get the MasterBaseRule
            Document baseRuleDocument=TTKCommon.getDocument("MasterBaseRules.xml");

            /*get the xml document for selected clauses from master base rule and
            merge it with Rule document from database/session memory if exists */
            Document selectedRuleDoc=ruleXMLHelper.getClauses(baseRuleDocument,strSelectedClauses);

            if(ruleDocument!=null)
            {
               selectedRuleDoc=ruleXMLHelper.getMergedRuleDocument(ruleDocument,selectedRuleDoc);
            }//end of if(ruleDocument!=null)

            request.getSession().setAttribute("RuleDocument",selectedRuleDoc);
            frmDefineRules.set("frmChanged","true");
            request.getSession().setAttribute("frmDefineRules",frmDefineRules);
            return this.getForward(getRulePath(strActiveSubLink),mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doContinueRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


    /**
     * This method is used to Revise Rule from the predefined rules of Product
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doReviseRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
            log.debug("RuleAction Action - inside doReviseRule method....");
            setLinks(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            StringBuffer sbfCaption=new StringBuffer();             //get the caption to be displayed
            sbfCaption.append(buildCaption(strActiveSubLink,request));
            //user can define new Rule by copying the contents of the this Rule
            DynaActionForm frmDefineRules= (DynaActionForm)form;
            frmDefineRules.initialize(mapping);
            request.getSession().removeAttribute("tableData");  //remove the unwanted data from the session
            RuleVO ruleVO=new RuleVO();
            Document ruleDocument=(Document)request.getSession().getAttribute("RuleDocument");
            ruleVO.setProdPolicySeqID(TTKCommon.getWebBoardId(request));
            ruleVO.setRuleDocument(ruleDocument);
            frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO,this,mapping,request);
            //sbfCaption.append("[").append(productVO.getProductName()).append("]");  //build the caption
            frmDefineRules.set("caption",sbfCaption.toString());
            request.getSession().setAttribute("frmDefineRules",frmDefineRules);
            return this.getForward(getRulePath(strActiveSubLink),mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doReviseRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to verify the defined rules of Product/Policy
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doVerifyRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
            log.debug("RuleAction Action - inside doVerifyRule method....");
            setLinks(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            //forward to the ruleverification screen
            return mapping.findForward(getRuleVerfiyPath(strActiveSubLink));
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doVerifyRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to navigate back to Rule defination screen when close button is
     * pressed in Verify rule screen.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doCloseVerifyRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
            log.debug("RuleAction Action - inside doCloseVerifyRule method....");
            setLinks(request);
            String strActiveSubLink=TTKCommon.getActiveSubLink(request);
            //forward to the ruleverification screen
            return mapping.findForward(getRulePath(strActiveSubLink));
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doVerifyRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * 
     * 
     */
    public ActionForward getPolicyBenefits(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
            log.debug("RuleAction Action - inside getPolicyBenefits method....");
            //setLinks(request);
            DynaActionForm frmDisplayBenefits = (DynaActionForm)form;
            frmDisplayBenefits.initialize(mapping);
            long prodPolicySeqId= TTKCommon.getWebBoardId(request);
            RuleManager ruleManagerObject=this.getRuleManagerObject();
            
            
            ListDisplayBenefitsVO listDisplayOfBenefitsVO = (ListDisplayBenefitsVO)ruleManagerObject.getDisplayBenefitsList(prodPolicySeqId);
            //displayBenefitsVO.setBenefitsDetailsList(benefitsDetailsList);
            frmDisplayBenefits.set("displayBenefitsList",listDisplayOfBenefitsVO.getDisplayBenefitsList());
            frmDisplayBenefits.set("otherRemarks", listDisplayOfBenefitsVO.getOtherRemarks());
            request.getSession().setAttribute("frmDisplayBenefits", frmDisplayBenefits);
            //request.setAttribute("benefitsDetailsList", benefitsDetailsList);
            
            return this.getForward("getBenefits",mapping,request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doVerifyRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * 
     * 
     */
    public ActionForward doSavePolicyBenefitDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        try
        {
            log.debug("RuleAction Action - inside doSavePolicyBenefitDetails method....");
            //setLinks(request);
            DynaActionForm frmDisplayBenefits = (DynaActionForm)form;
            RuleManager ruleManagerObject=this.getRuleManagerObject();
            long prodPolicySeqId = TTKCommon.getWebBoardId(request);
            long userSeqId = TTKCommon.getUserSeqId(request);
            ArrayList<DisplayBenefitsVO> displayBenefitsList=  (ArrayList<DisplayBenefitsVO>) frmDisplayBenefits.get("displayBenefitsList");
            String finalRemarks = (String)frmDisplayBenefits.get("otherRemarks");
           
            
            
            ArrayList<Object> listBenefitDetailsVO = new ArrayList<Object>();
            
            for(DisplayBenefitsVO displayBenefitsVO:displayBenefitsList){
            	listBenefitDetailsVO.addAll(displayBenefitsVO.getBenefitsDetailsList());
            }
            
             int rowsUpdated = ruleManagerObject.saveBenefitsDetailsList(listBenefitDetailsVO,finalRemarks, prodPolicySeqId, userSeqId);
             if(rowsUpdated >= 1){
            	 request.setAttribute("isUpdated","Y");
            	 return doClosePolicyBenefitDetails(mapping, form, request, response);
             }
             else
            	 throw new TTKException();
            
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processExceptions(request, mapping, new TTKException(exp, "product"));
        }//end of catch(Exception exp)
    }//end of doVerifyRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    public ActionForward doClosePolicyBenefitDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception{
    	if("Y".equals((request).getAttribute("isUpdated")))
        	request.setAttribute("updated","message.addedSuccessfully");
    	
    	return mapping.findForward("backToPolicyRules");
    }
    
	/**
     * Returns the RuleManager session object for invoking methods on it.
     * @return RuleManager session object which can be used for method invokation
     * @exception throws TTKException
     */
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

    /**
     * Returns the ProductPolicyManager session object for invoking methods on it.
     * @return ProductPolicyManager session object which can be used for method invokation
     * @exception throws TTKException
     */
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

    /**
     * This method returns the forward path of next view based on the Flow
     *
     * @param strActiveSubLink String current sublink
     * @return strRulePath String forward path for the next view
     */
    private String getRulePath(String strActiveSubLink)
    {
        String strRulePath="";
        if(strActiveSubLink.equals("Products"))
        {
            strRulePath=strProductrule;
        }//end of if(strActiveSubLink.equals("Products"))
        else if(strActiveSubLink.equals("Policies"))
        {
            strRulePath=strPolicyRule;
        }//end of else if(strActiveSubLink.equals("Policies"))
        return strRulePath;
    }//end of getRulePath(String strActiveSubLink)

    /**
     * This method returns the forward path of next view based on the Flow
     *
     * @param strActiveSubLink String current sublink
     * @return strClauseListPath String forward path for the next view
     */
    private String getClauseListPath(String strActiveSubLink)
    {
        String strClauseListPath="";
        if(strActiveSubLink.equals("Products"))
        {
            strClauseListPath=strProductClauseList;
        }//end of if(strActiveSubLink.equals("Products"))
        else if(strActiveSubLink.equals("Policies"))
        {
            strClauseListPath=strPolicyClauseList;
        }//end of else if(strActiveSubLink.equals("Policies"))
        return strClauseListPath;
    }//end of getClauseListPath(String strActiveSubLink)

    /**
     * This method returns the forward path of next view based on the Flow
     *
     * @param strActiveSubLink String current sublink
     * @return strClauseListPath String forward path for the next view
     */
    private String getRuleVerfiyPath(String strActiveSubLink)
    {
        String strRuleVerifyPath="";
        if(strActiveSubLink.equals("Products"))
        {
            strRuleVerifyPath=strProductRuleVerification;
        }//end of if(strActiveSubLink.equals("Products"))
        else if(strActiveSubLink.equals("Policies"))
        {
            strRuleVerifyPath=strPolicyRuleVerification;
        }//end of else if(strActiveSubLink.equals("Policies"))
        return strRuleVerifyPath;
    }//end of getRuleVerfiyPath(String strActiveSubLink)

    /**
     * This method is prepares the Caption based on the flow and retunrs it
     * @param strActiveSubLink current Active sublink
     * @param request current HttpRequest
     * @return String caption built
     * @throws TTKException
     */
    private String buildCaption(String strActiveSubLink,HttpServletRequest request)throws TTKException
    {
        StringBuffer sbfCaption=new StringBuffer();
        //String strMode=TTKCommon.checkNull(request.getParameter("mode"));
        ProductPolicyManager productPolicyManagerObject=this.getProductPolicyManagerObject();
        if(strActiveSubLink.equals("Products"))
        {
            ProductVO productVO=productPolicyManagerObject.getProductDetails(TTKCommon.getWebBoardId(request));
            sbfCaption.append("[").append(productVO.getCompanyName()).append("]");
            sbfCaption.append("[").append(productVO.getProductName()).append("]");
        }//end of if(strActiveSubLink.equals("Products"))
        else if(strActiveSubLink.equals("Policies"))
        {
            PolicyDetailVO policyDetailVO= productPolicyManagerObject.getPolicyDetail(TTKCommon.getWebBoardId(request),
                    TTKCommon.getUserSeqId(request));
            sbfCaption.append("[").append(policyDetailVO.getCompanyName()).append("]");
            sbfCaption.append("[").append(policyDetailVO.getPolicyNbr()).append("]");
        }//end of else if(strActiveSubLink.equals("Policies"))
        return sbfCaption.toString();
    }//end of buildCaption(String strActiveSubLink,HttpServletRequest request)

public ActionForward doContinueMemberRule(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception
        {
		try
			{
		        log.debug("inside doContinueMemberRule method of RuleAction....");
		        setLinks(request);
		        
		        DynaActionForm frmDefineRulesForMember= (DynaActionForm)form;
		        RuleXMLHelper ruleXMLHelper=new RuleXMLHelper();     //create the instance of Helper
		        //RuleDocument present in database/session memory
		        
		        Document ruleDocument=(Document)request.getSession().getAttribute("RuleDocument");
		        String[] strSelectedClauses=request.getParameterValues("chkbox");
		
		        //get the MasterBaseRule
		        Document baseRuleDocument=TTKCommon.getDocument("MasterBaseRules.xml");
		
		        /*get the xml document for selected clauses from master base rule and
		        merge it with Rule document from database/session memory if exists */
		        Document selectedRuleDoc=ruleXMLHelper.getClauses(baseRuleDocument,strSelectedClauses);
		
		        if(ruleDocument!=null)
		        {
		           selectedRuleDoc=ruleXMLHelper.getMergedRuleDocument(ruleDocument,selectedRuleDoc);
		        }//end of if(ruleDocument!=null)
		
		        request.getSession().setAttribute("RuleDocument",selectedRuleDoc);
		        request.getSession().setAttribute("frmDefineRulesForMember",frmDefineRulesForMember);
		        return this.getForward(strMemberPolicyRule,mapping,request);
			}//end of try
    catch(TTKException expTTK)
    {
        return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
        return this.processExceptions(request, mapping, new TTKException(exp, "product"));
    }//end of catch(Exception exp)
} // end of doContinueMemberRule()





public ActionForward viewPolicyRuleHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception
        {
    try{
        log.debug("RuleAction Action - inside viewPolicyRuleHistory method....");
        setLinks(request);
        TableData tableData=TTKCommon.getTableData(request);
        String strActiveSubLink=TTKCommon.getActiveSubLink(request);
        DynaActionForm frmDefineRules= (DynaActionForm)form;
        RuleManager ruleManagerObject=this.getRuleManagerObject();
        Document ruleDocument=null;
        StringBuffer sbfCaption=new StringBuffer();

        //get the caption to be displayed
        sbfCaption.append(buildCaption(strActiveSubLink,request));

        if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
        {
            RuleVO ruleVO=(RuleVO)tableData.getRowInfo(Integer.parseInt((String)(request).getParameter("rownum")));
            	
            	ruleDocument=ruleVO.getRuleDocument();
                frmDefineRules = (DynaActionForm)FormUtils.setFormValues("frmDefineRules",ruleVO,this,mapping,request);
                frmDefineRules.set("caption",sbfCaption.toString());
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
        log.debug("RuleAction Action - inside viewPolicyRuleHistory method....");
        setLinks(request);
        TableData tableData=TTKCommon.getTableData(request);
        String strActiveSubLink=TTKCommon.getActiveSubLink(request);
        DynaActionForm frmDefineRules= (DynaActionForm)form;
        RuleManager ruleManagerObject=this.getRuleManagerObject();
        Document ruleDocument=null;
        StringBuffer sbfCaption=new StringBuffer();
//prodPolicyRuleSeqID
        sbfCaption.append(buildCaption(strActiveSubLink,request));

        String prodPoliccyRuleSeqId = frmDefineRules.getString("prodPolicyRuleSeqID");
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
            frmDefineRules.set("caption",sbfCaption.toString());
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
	
	 log.debug("RuleAction Action - inside onHistoryClose method....");
     setLinks(request);
     TableData tableData=TTKCommon.getTableData(request);
     String strActiveSubLink=TTKCommon.getActiveSubLink(request);
     DynaActionForm frmDefineRules= (DynaActionForm)form;
     StringBuffer sbfCaption=new StringBuffer();
           sbfCaption.append(buildCaption(strActiveSubLink,request));
     request.getSession().setAttribute("tableData",tableData);
     frmDefineRules.set("caption",sbfCaption.toString());
	 return this.getForward("policyRulesList",mapping,request);
	
	
        }



}