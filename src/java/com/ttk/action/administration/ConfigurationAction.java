/**
 * @ (#) ConfigurationAction.java Feb 27, 2006
 * Project      : TTK HealthCare Services
 * File         : ConfigurationAction.java
 * Author       : Balaji C R B
 * Company      : Span Systems Corporation
 * Date Created : July 1,2008
 *
 * @author       : Balaji C R B
 * Modified by   :
 * Modified date :
 * Reason        :
 */
package com.ttk.action.administration;

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
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.ProductVO;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.enrollment.PolicyDetailVO;

import formdef.plugin.util.FormUtils;

public class ConfigurationAction extends TTKAction{
	
	private static Logger log = Logger.getLogger( ConfigurationAction.class );
	
	//Action mapping forwards.	
	private static final String strProdConfigurationList="prodconfigurationlist";
	private static final String strPolicyConfigurationList="policyconfigurationlist";
	private static final String strProductGeneralInfo="productgeneralinfo";
	private static final String strPolicyGeneralInfo="policydetail";
	
	//Exception Message Identifier
	private static final String strProductPolicy="productpolicy";
	
	//Changes Added for Password Policy CR KOC 1235
	private static final String strUserGeneralInfo="userconfinfo";
	//End changes for Password Policy CR KOC 1235

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
			log.info("Inside the doChangeWebBoard method of ConfigurationAction");
			setLinks(request);
			//if web board id is found, set it as current web board id
			//TTKCommon.setWebBoardId(request);
			//get the session bean from the bean pool for each excecuting thread
			ProductPolicyManager productPolicyObject=this.getProductPolicyManagerObject();
			DynaActionForm frmProdOrPolicy = (DynaActionForm)form;			
			Long lngProductPolicySeqId=TTKCommon.getWebBoardId(request);
			String strActiveSubLink = TTKCommon.getActiveSubLink(request);
			ProductVO productVO = null;
			PolicyDetailVO policyDetailVO=null;
			
			if("Products".equals(strActiveSubLink)){
				productVO = new ProductVO();			
				frmProdOrPolicy.initialize(mapping);
				//get the product details from the Dao object
				productVO=productPolicyObject.getProductDetails(lngProductPolicySeqId);
				frmProdOrPolicy = (DynaActionForm)FormUtils.setFormValues("frmProductGeneralInfo",
						productVO, this, mapping, request);
				frmProdOrPolicy.set("caption","Edit");
				request.setAttribute("frmProductGeneralInfo",frmProdOrPolicy);
				//add the product to session
				request.getSession().setAttribute("productVO",productVO);
				this.documentViewer(request,productVO);	
				return this.getForward(strProdConfigurationList, mapping, request);
			}//end of if("Products".equals(strActiveSubLink))
			else {
				//then Active Sub Link is Policies
//				if web board id is found, set it as current web board id
				frmProdOrPolicy.initialize(mapping);			
				//get the Policy details from the Dao object
				policyDetailVO=productPolicyObject.getPolicyDetail(lngProductPolicySeqId,
						TTKCommon.getUserSeqId(request));
				ArrayList alUserGroup=new ArrayList();
				//make query to get user group to load to combo box
				if(policyDetailVO.getOfficeSeqID()!=null)
				{
					alUserGroup=productPolicyObject.getGroup(policyDetailVO.getOfficeSeqID());
				}//end of  if(policyDetailVO.getOfficeSeqID()!=null)
				request.getSession().setAttribute("alUserGroup",alUserGroup);
				frmProdOrPolicy = (DynaActionForm)FormUtils.setFormValues("frmPoliciesEdit",policyDetailVO,
						this,mapping,request);
				//isBufferAllowedSaved used to keep the Buffer Allowed checkbox readonly 
				//if it is checked and saved before.
				frmProdOrPolicy.set("isBufferAllowedSaved",policyDetailVO.getBufferAllowedYN());
				request.getSession().setAttribute("frmPoliciesEdit",frmProdOrPolicy);
				this.documentViewer(request,policyDetailVO);
				return this.getForward(strPolicyConfigurationList, mapping, request);
			}//end of else if("Policies".equals(strActiveSubLink))			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductPolicy));
		}//end of catch(Exception exp)
	}//end of ChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			//HttpServletResponse response)
	
	/**
	 * This method is used to go back to the Product/Policy General screen on click of
	 * close button in Configuration list screen
	 * Finally it forwards to the appropriate view based on the specified forward mappings
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
			log.info("Inside the doClose method of ConfigurationAction");
			setLinks(request);
			//Changes Added for Password Policy CR KOC 1235
			if("User Management".equals(TTKCommon.getActiveSubLink(request)))
			{
				return mapping.findForward(strUserGeneralInfo);
			}//end of if("User Management".equals(TTKCommon.getActiveSubLink(request)))
			else if(!("Products".equals(TTKCommon.getActiveSubLink(request))))
			{
				return mapping.findForward(strPolicyGeneralInfo);
			}//end of else if(!("Products".equals(TTKCommon.getActiveSubLink(request))))
			else 
			{
				return mapping.findForward(strProductGeneralInfo);
			}//end of else			
			//End changes for Password Policy CR KOC 1235		
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProductPolicy));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response) throws Exception
	/**
	 * This menthod for document viewer information
	 * @param request HttpServletRequest object which contains hospital information.
	 * @param policyDetailVO PolicyDetailVO object which contains policy information.
	 * @exception throws TTKException
	 */
	private void documentViewer(HttpServletRequest request, ProductVO productVO) throws TTKException
	{
//		Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		ArrayList<String> alDocviewParams = new ArrayList<String>();
		alDocviewParams.add("leftlink="+TTKCommon.getActiveLink(request));
		alDocviewParams.add("product_seq_id="+productVO.getProdSeqId());
		if(request.getSession().getAttribute("toolbar")!=null)
		{
			((Toolbar)request.getSession().getAttribute("toolbar")).setDocViewParams(alDocviewParams);
		}//end of if(request.getSession().getAttribute("toolbar")!=null)
	}//end of documentViewer(HttpServletRequest request, ProductVO productVO)
	
	/**
	 * This method for document viewer information
	 * @param request HttpServletRequest object which contains hospital information.
	 * @param policyDetailVO PolicyDetailVO object which contains policy information.
	 * @exception throws TTKException
	 */
	private void documentViewer(HttpServletRequest request,PolicyDetailVO policyDetailVO) throws TTKException
	{
		//Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
		ArrayList<String> alDocviewParams = new ArrayList<String>();
		alDocviewParams.add("leftlink=Enrollment");
		alDocviewParams.add("policy_number="+policyDetailVO.getPolicyNbr());
		alDocviewParams.add("dms_reference_number="+policyDetailVO.getDMSRefID());
		
		if(request.getSession().getAttribute("toolbar")!=null){
			((Toolbar)request.getSession().getAttribute("toolbar")).setDocViewParams(alDocviewParams);
		}//end of if(request.getSession().getAttribute("toolbar")!=null)
	}//end of documentViewer(HttpServletRequest request,PolicyDetailVO policyDetailVO)
	
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
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "product");
		}//end of catch
		return productPolicyManager;
	}//end getProductManagerObject()

}//end of ConfigurationAction
