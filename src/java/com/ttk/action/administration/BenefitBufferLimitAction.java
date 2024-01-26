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
import com.ttk.dto.administration.ProdPolicyLimitVO;
import com.ttk.dto.administration.ProductVO;

public class BenefitBufferLimitAction  extends TTKAction{
	
	private static Logger log = Logger.getLogger( BenefitBufferLimitAction.class );
	
	private static final String strBufferLimit="benefitBufferLimit";
	private static final String strProduct="product";
	private static final String strBufferClose="bufferLimitClose";
	
	
	public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
				
		try{
			log.debug("Inside the doDefault method of BenefitBufferLimitAction");
			setLinks(request);
			DynaActionForm frmBufferLimit  =(DynaActionForm)form;
			StringBuffer sbfCaption=new StringBuffer();
			ProductPolicyManager productPolicyObject=this.getProductPolicyManagerObject();
		
		    ProdPolicyLimitVO bufferLimitVo = null;
			 bufferLimitVo = productPolicyObject.getBenefitBufferLimitDtls(TTKCommon.getWebBoardId(request));
			if(bufferLimitVo!=null)
			{
				frmBufferLimit.set("dental",bufferLimitVo.getDental());
				frmBufferLimit.set("optical",bufferLimitVo.getOptical());
				frmBufferLimit.set("opMaternity",bufferLimitVo.getOpMaternity());
				frmBufferLimit.set("outpatient",bufferLimitVo.getOutpatient());
				frmBufferLimit.set("prescription",bufferLimitVo.getPrescription());
				
				frmBufferLimit.set("partnerDental",bufferLimitVo.getPartnerDental());
				frmBufferLimit.set("partnerOptical",bufferLimitVo.getPartnerOptical());
				frmBufferLimit.set("partnerOpMaternity",bufferLimitVo.getPartnerOpMaternity());
				frmBufferLimit.set("partnerOutpatient",bufferLimitVo.getPartnerOutpatient());
				frmBufferLimit.set("partnerPrescription",bufferLimitVo.getPartnerPrescription());
			}
			else{
				
				frmBufferLimit.set("dental","");
				frmBufferLimit.set("optical","");
				frmBufferLimit.set("opMaternity","");
				frmBufferLimit.set("outpatient","");
				frmBufferLimit.set("prescription","");
				
				frmBufferLimit.set("partnerDental","");
				frmBufferLimit.set("partnerOptical","");
				frmBufferLimit.set("partnerOpMaternity","");
				frmBufferLimit.set("partnerOutpatient","");
				frmBufferLimit.set("partnerPrescription","");
			}
			
			DynaActionForm frmProductList = null;
            
            if("Products".equals(TTKCommon.getActiveSubLink(request))){
            	
            	frmProductList = (DynaActionForm)request.getSession().getAttribute("frmProductGeneralInfo");
            	sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
    			sbfCaption.append("[").append(frmProductList.get("productName")).append("]");		
            	
            }
            else{
            	
            	frmProductList = (DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
            	sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
    			sbfCaption.append("[").append(frmProductList.get("policyNbr")).append("]");
            }
			 
            
			frmBufferLimit.set("caption",sbfCaption.toString());
			
			
			request.setAttribute("frmBufferLimit",frmBufferLimit);
			
			
			return this.getForward(strBufferLimit, mapping, request);
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strProduct));
		}//end of catch(Exception exp)
		
		
		
	}
	
	
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
	
	

	
	
	 public ActionForward doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		
		 try{
	    		setLinks(request);
	    		log.debug("Inside BenefitBufferLimitAction doSave");
		
	    		StringBuffer sbfCaption= new StringBuffer();
	    		
	    		ProductPolicyManager productPolicyObject=this.getProductPolicyManagerObject();
	    		DynaActionForm frmBufferLimit =(DynaActionForm)form;
	    		ArrayList<Object> alProdPolicyLimit = new ArrayList<Object>();
	    		ProdPolicyLimitVO bufferLimitVo = null;
	    		
	    		alProdPolicyLimit.add(TTKCommon.getWebBoardId(request));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("dental"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("optical"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("outpatient"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("prescription"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("opMaternity"));
	    		alProdPolicyLimit.add(TTKCommon.getUserSeqId(request));
	    		
	    		alProdPolicyLimit.add(frmBufferLimit.getString("partnerDental"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("partnerOptical"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("partnerOutpatient"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("partnerPrescription"));
	    		alProdPolicyLimit.add(frmBufferLimit.getString("partnerOpMaternity"));
	    		
	    		 int iCount=productPolicyObject.saveBenefitTypeBufferLimit(alProdPolicyLimit);
	    		 request.setAttribute("updated","message.savedSuccessfully");
	    		 if(iCount>0)   
	                {
	    		/* alProdPolicyLimit = productPolicyObject.getBenefitBufferLimitDtls(TTKCommon.getWebBoardId(request));*/
	    		 bufferLimitVo = productPolicyObject.getBenefitBufferLimitDtls(TTKCommon.getWebBoardId(request));
	    		 
	    		   String dental = bufferLimitVo.getDental();
	    		 
	                }

	    		 DynaActionForm frmProductList = null;
	             
	             if("Products".equals(TTKCommon.getActiveSubLink(request))){
	             	
	             	frmProductList = (DynaActionForm)request.getSession().getAttribute("frmProductGeneralInfo");
	             	sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
	     			sbfCaption.append("[").append(frmProductList.get("productName")).append("]");		
	             	
	             }
	             else{
	             	
	             	frmProductList = (DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
	             	sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
	     			sbfCaption.append("[").append(frmProductList.get("policyNbr")).append("]");
	             }
	             
	 			frmBufferLimit.set("caption",sbfCaption.toString());
	 			request.setAttribute("frmBufferLimit",frmBufferLimit);
	    		 
	    		 
	    		return this.getForward(strBufferLimit, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(ETTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strProduct));
	    	}//end of catch(Exception exp)
		
	}
	
	
	
	
public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				   HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doClose method of BenefitBufferLimitAction");
setLinks(request);
return mapping.findForward(strBufferClose);
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
	
	
	
public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		 HttpServletResponse response) throws Exception{
	try{
	setLinks(request);
	log.debug("Inside RenewalDaysConfAction doReset");
	DynaActionForm frmBufferLimit =(DynaActionForm)form;
	StringBuffer sbfCaption= new StringBuffer();
	
	
	 DynaActionForm frmProductList = null;
     
     if("Products".equals(TTKCommon.getActiveSubLink(request))){
     	
     	frmProductList = (DynaActionForm)request.getSession().getAttribute("frmProductGeneralInfo");
     	sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
			sbfCaption.append("[").append(frmProductList.get("productName")).append("]");		
     	
     }
     else{
     	
     	frmProductList = (DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
     	sbfCaption.append("[").append(frmProductList.get("companyName")).append("]");
			sbfCaption.append("[").append(frmProductList.get("policyNbr")).append("]");
     }
	
	
	/*frmBufferLimit.set("dental","");
	frmBufferLimit.set("optical","");
	frmBufferLimit.set("opMaternity","");
	frmBufferLimit.set("outpatient","");
	frmBufferLimit.set("prescription","");*/
	
	
	/*frmBufferLimit.set("caption",sbfCaption.toString());*/
     frmBufferLimit.initialize(mapping); 
     frmBufferLimit.set("caption",sbfCaption.toString()); 
	request.setAttribute("frmBufferLimit",frmBufferLimit);
	return this.getForward(strBufferLimit, mapping, request);
	//return this.getForward(strRenewalConfig, mapping, request);
}//end of try
catch(TTKException expTTK)
	{
	return this.processExceptions(request,mapping,expTTK);
}//end of catch(ETTKException expTTK)
catch(Exception exp)
	{
	return this.processExceptions(request,mapping,new TTKException(exp,strProduct));
}//end of catch(Exception exp)
}	
	
	
	
	
}
