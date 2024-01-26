package com.ttk.action.administration;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.business.administration.ProdPolicyConfigManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.PlanVO;

import formdef.plugin.util.FormUtils;


public class ProviderDiscApplicationAction extends TTKAction
{
	private static Logger log = Logger.getLogger( ProviderDiscApplicationAction.class );

	private static final String strProduct="product";
	private static final String strDiscountClose="discountClose";
	
	
	public ActionForward  doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception 
	{
		try
		{
			log.info("Inside the doDefault method of ProviderDiscApplicationAction");
			setLinks(request);
			
			DynaActionForm frmProviderDiscApp  =(DynaActionForm)form;
			DynaActionForm frmProductList = null;
			frmProductList = (DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
			PlanVO planVO = new PlanVO();
			ProdPolicyConfigManager  productPolicyConfig= null;
			StringBuffer sbfCaption=new StringBuffer();
			
			
			
        	sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
			sbfCaption.append("[").append(frmProductList.get("policyNbr")).append("]");
			frmProviderDiscApp.set("caption",sbfCaption.toString());
			
			Long prodPolicySeqId = TTKCommon.getWebBoardId(request);
			System.out.println("prodPolicySeqId ====="+prodPolicySeqId);
			productPolicyConfig = this.getProductPolicyConfigObject();
			planVO = productPolicyConfig.getProviderDiscountData(prodPolicySeqId);
			
			frmProviderDiscApp.set("applyDiscount", planVO.getApplyDiscount());
		
			if("Y".equals(planVO.getAll()))
					frmProviderDiscApp.set("all","on");
				else
					frmProviderDiscApp.set("all", "off");
			
			if("Y".equals(planVO.getDental()))
				frmProviderDiscApp.set("dental","on");
			else
				frmProviderDiscApp.set("dental", "off");
			
			if("Y".equals(planVO.getOptical()))
				frmProviderDiscApp.set("optical","on");
			else
				frmProviderDiscApp.set("optical", "off");
			
			if("Y".equals(planVO.getOpMaternity()))
				frmProviderDiscApp.set("opMaternity","on");
			else
				frmProviderDiscApp.set("opMaternity", "off");
			
			if("Y".equals(planVO.getIpMaternity()))
				frmProviderDiscApp.set("ipMaternity","on");
			else
				frmProviderDiscApp.set("ipMaternity", "off");
			
			if("Y".equals(planVO.getOpBenefit()))
				frmProviderDiscApp.set("opBenefit","on");
			else
				frmProviderDiscApp.set("opBenefit", "off");
			
			if("Y".equals(planVO.getIpBenefit()))
				frmProviderDiscApp.set("ipBenefit","on");
			else
				frmProviderDiscApp.set("ipBenefit", "off");
		
			
			
			System.out.println("applyDiscount ====="+planVO.getApplyDiscount());
			System.out.println("all ====="+planVO.getAll());
			System.out.println("dental ====="+planVO.getDental());
			System.out.println("optical ====="+planVO.getOptical());
			System.out.println("opMaternity ====="+planVO.getOpMaternity());
			System.out.println("ipMaternity ====="+planVO.getIpMaternity());
			System.out.println("opBenefit ====="+planVO.getOpBenefit());
			System.out.println("ipBenefit ====="+planVO.getIpBenefit());
		
			
			request.setAttribute("frmProviderDiscApp",frmProviderDiscApp);
			return this.getForward("providerDisApp", mapping, request);
		} // end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
		}//end of catch(Exception exp)
	} // end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception 

	
	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try
		{
			log.debug("Inside the doClose method of ProviderDiscApplicationAction");
			setLinks(request);
			return mapping.findForward(strDiscountClose);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	public ActionForward  doSaveProviderdisApp(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception 
	{
		try
		{
			log.info("Inside the doSaveProviderdisApp method of ProviderDiscApplicationAction");
			setLinks(request);
			
			DynaActionForm frmProviderDiscApp  =(DynaActionForm)form;
			Long prodPolicySeqId = TTKCommon.getWebBoardId(request);
			Long getUserSeqId = TTKCommon.getUserSeqId(request);
			
			PlanVO planVO = new PlanVO();
			ProdPolicyConfigManager  productPolicyConfig= null;
			
			// applyDiscount flag 
			String applyDiscount = frmProviderDiscApp.getString("applyDiscount");
			
			// all  
			String all = frmProviderDiscApp.getString("all");
			if("Y".equals(all))
			{	
				planVO.setApplyDiscount(applyDiscount);
				planVO.setAll("ALL");
				planVO.setDental("Y");
				planVO.setOptical("Y");
				planVO.setOpMaternity("Y");
				planVO.setIpMaternity("Y");
				planVO.setOpBenefit("Y");
				planVO.setIpBenefit("Y");
			}	
			else 
			{	
					planVO.setApplyDiscount(applyDiscount);	
					planVO.setAll(null);
					
					// dental  
					String dental = frmProviderDiscApp.getString("dental");
					if("Y".equals(dental))
						planVO.setDental("Y");
					else 
						planVO.setDental("N");
					
					// optical  
					String optical = frmProviderDiscApp.getString("optical");
					if("Y".equals(optical))
						planVO.setOptical("Y");
						
					else 
						planVO.setOptical("N");
					
					// opMaternity  
					String opMaternity = frmProviderDiscApp.getString("opMaternity");
					if("Y".equals(opMaternity))
						planVO.setOpMaternity("Y");
					else 
						planVO.setOpMaternity("N");
					
					// ipMaternity  
					String ipMaternity = frmProviderDiscApp.getString("ipMaternity");
					if("Y".equals(ipMaternity))
						planVO.setIpMaternity("Y");
					else 
						planVO.setIpMaternity("N");
			
					// opBenefit  
					String opBenefit = frmProviderDiscApp.getString("opBenefit");
					if("Y".equals(opBenefit))
						planVO.setOpBenefit("Y");
					else 
						planVO.setOpBenefit("N");
					
					// ipBenefit  
					String ipBenefit = frmProviderDiscApp.getString("ipBenefit");
					if("Y".equals(ipBenefit))
						planVO.setIpBenefit("Y");
					else 
						planVO.setIpBenefit("N");
					
					System.out.println("------------------Action class------------------------------");
					System.out.println("prodPolicySeqId		 ="+prodPolicySeqId);
					System.out.println("getUserSeqId 		="+getUserSeqId);
					System.out.println("applyDiscount flag 	= 	"+applyDiscount);
					System.out.println(" all YN 			=	"+all);
					System.out.println(" dental YN 			=	"+dental);
					System.out.println(" optical YN			=	"+optical);	
					System.out.println(" opMaternity YN		=	"+opMaternity);
					System.out.println(" ipMaternity YN		=	"+ipMaternity);
					System.out.println(" opBenefit YN		=	"+opBenefit);
					System.out.println(" ipBenefit YN		=	"+ipBenefit);
					
				System.out.println("------------------------------------------------");	
			}
		
			planVO.setAddedBy(TTKCommon.getUserSeqId(request));
			planVO.setProdPolicySeqID(prodPolicySeqId);
			
			productPolicyConfig = this.getProductPolicyConfigObject();
			int res = productPolicyConfig.saveProviderdiscountData(planVO);
			if(res > 0)
			{
				request.setAttribute("updated","message.saved");
			}
			frmProviderDiscApp.initialize(mapping);
			
			System.out.println("prodPolicySeqId ="+ prodPolicySeqId);
			planVO = productPolicyConfig.getProviderDiscountData(prodPolicySeqId);
			
			frmProviderDiscApp.set("applyDiscount", planVO.getApplyDiscount());
		
			if("Y".equals(planVO.getAll()))
					frmProviderDiscApp.set("all","on");
				else
					frmProviderDiscApp.set("all", "off");
			
			if("Y".equals(planVO.getDental()))
				frmProviderDiscApp.set("dental","on");
			else
				frmProviderDiscApp.set("dental", "off");
			
			if("Y".equals(planVO.getOptical()))
				frmProviderDiscApp.set("optical","on");
			else
				frmProviderDiscApp.set("optical", "off");
			
			if("Y".equals(planVO.getOpMaternity()))
				frmProviderDiscApp.set("opMaternity","on");
			else
				frmProviderDiscApp.set("opMaternity", "off");
			
			if("Y".equals(planVO.getIpMaternity()))
				frmProviderDiscApp.set("ipMaternity","on");
			else
				frmProviderDiscApp.set("ipMaternity", "off");
			
			if("Y".equals(planVO.getOpBenefit()))
				frmProviderDiscApp.set("opBenefit","on");
			else
				frmProviderDiscApp.set("opBenefit", "off");
			
			if("Y".equals(planVO.getIpBenefit()))
				frmProviderDiscApp.set("ipBenefit","on");
			else
				frmProviderDiscApp.set("ipBenefit", "off");
		
			
			
			System.out.println("applyDiscount ====="+planVO.getApplyDiscount());
			System.out.println("all ====="+planVO.getAll());
			System.out.println("dental ====="+planVO.getDental());
			System.out.println("optical ====="+planVO.getOptical());
			System.out.println("opMaternity ====="+planVO.getOpMaternity());
			System.out.println("ipMaternity ====="+planVO.getIpMaternity());
			System.out.println("opBenefit ====="+planVO.getOpBenefit());
			System.out.println("ipBenefit ====="+planVO.getIpBenefit());
		
			
			request.setAttribute("frmProviderDiscApp",frmProviderDiscApp);
			return this.getForward("providerDisApp", mapping, request);
		} // end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
		}//end of catch(Exception exp)
	}
	
	private ProdPolicyConfigManager getProductPolicyConfigObject() throws TTKException
	{
		ProdPolicyConfigManager productPolicyConfigManager = null;
		try
		{
			if(productPolicyConfigManager == null)
			{
				InitialContext ctx = new InitialContext();
				productPolicyConfigManager = (ProdPolicyConfigManager) ctx.lookup("java:global/TTKServices/business.ejb3/ProdPolicyConfigManagerBean!com.ttk.business.administration.ProdPolicyConfigManager");
			}//end of if(productPolicyConfigManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "product");
		}//end of catch(Exception exp)
		return productPolicyConfigManager;
	}//end of getProductPolicyConfigObject()
	
	
	

}
