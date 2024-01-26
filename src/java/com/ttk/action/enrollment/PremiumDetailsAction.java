package com.ttk.action.enrollment;

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
import com.ttk.business.enrollment.PolicyManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.WebBoardHelper;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.enrollment.PolicyDetailVO;

import formdef.plugin.util.FormUtils;

public class PremiumDetailsAction extends TTKAction{
	
	private static Logger log = Logger.getLogger( PremiumDetailsAction.class );	
	
	
	 public ActionForward addPremiumDtls(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            HttpServletResponse response) throws Exception{
	        try
	        {
	        	log.debug("Inside addPremiumDtls of PremiumDetailsAction");
	        	HttpSession session = request.getSession();	
	        	  DynaActionForm frmPremiumDetails=(DynaActionForm)form;
	        	  PolicyManager policyObject=this.getPolicyManagerObject();
	        	  PolicyDetailVO policyDetailVO=null;
	        	  String successMsg;
	        	  String premiumSeqId=null;
	        	  String minimumAge =(String) frmPremiumDetails.getString("minimumAge").trim();
	        	  String maximumAge =(String) frmPremiumDetails.getString("maximumAge").trim();
	        	  String gender =(String) frmPremiumDetails.getString("gender");
	        	  String premium =(String) frmPremiumDetails.getString("premium").trim();
	        	   premiumSeqId =(String) frmPremiumDetails.getString("premiumSeqId");
	        	  String deleteFlag =(String) request.getParameter("deleteFlag");
	        	  Long policyid = WebBoardHelper.getPolicySeqId(request);
	        	  
	        	  
	        	  policyDetailVO=new PolicyDetailVO();
	        	  policyDetailVO.setMinimumAge(minimumAge);
	        	  policyDetailVO.setMaximumAge(maximumAge);
	        	  policyDetailVO.setGender(gender);
	        	  policyDetailVO.setPremium(premium);
	        	  if("".equals(premiumSeqId)){
	        		  premiumSeqId="0";
	        	  }
	        	  policyDetailVO.setPremiumSeqId(Long.parseLong(premiumSeqId));
	        	  policyDetailVO.setPolicySeqID(policyid);
	        	  policyDetailVO.setDeleteYN(deleteFlag);
	        	  policyDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));//User Id
	        	  Long lngPremiumSeqId=null;
	        	  lngPremiumSeqId=policyObject.addPremiumDetails(policyDetailVO);
	        	  
	        	if(lngPremiumSeqId!=0){
	        		successMsg = "Premium Details Added Successfully";
	        		request.setAttribute("successMsg", successMsg);
	        		
	            ArrayList<String[]>	premiumList= policyObject.getPremiumDetails(lngPremiumSeqId,policyid);
	            session.setAttribute("premiumList", premiumList);
	        	}
	        	
	        	frmPremiumDetails.initialize(mapping);
	        	
	            return this.getForward("selectPremiumDetails", mapping, request);
	        }// end of try
	        catch(TTKException expTTK)
	        {
	            return this.processExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processExceptions(request, mapping, new TTKException(exp, "endorsementdetails"));
	        }//end of catch(Exception exp)
	    }
	    
	    public ActionForward editPremiumDtls(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            HttpServletResponse response) throws Exception{
	        try
	        {
	        	log.debug("Inside editPremiumDtls of PremiumDetailsAction");
	        	
	        	  DynaActionForm frmPremiumDetails=(DynaActionForm)form;
	        	  PolicyManager policyObject=this.getPolicyManagerObject();
	        	  HttpSession session = request.getSession();	
	        	  String premiumSeqId = request.getParameter("premiumSeqId");
	        	  Long policyid = WebBoardHelper.getPolicySeqId(request);
	        	  Long lngPremiumSeqId=Long.parseLong(premiumSeqId);
	        	  
	        	  ArrayList<String[]>	premiumEditList= policyObject.selectPremiumDetails(lngPremiumSeqId,policyid);
	        	  ArrayList<String[]>	premiumList= policyObject.getPremiumDetails(lngPremiumSeqId,policyid);
	        	  
	        	  for(String[] strDetails:premiumEditList){
	        		  
	        		  String hddnPremiunSeqId=strDetails[0];
	        		  String hddnpolicySeqId=strDetails[1];
	        		  String minAge=strDetails[2];
	        		  String maxAge=strDetails[3];
	        		  String gender=strDetails[4];
	        		  String premium=strDetails[5];
	        		  
	        		  frmPremiumDetails.set("premiumSeqId",hddnPremiunSeqId);
	        		  frmPremiumDetails.set("policySeqID",Long.parseLong(hddnpolicySeqId));
	        		  frmPremiumDetails.set("minimumAge",minAge);
	        		  frmPremiumDetails.set("maximumAge",maxAge);
	        		  frmPremiumDetails.set("gender",gender);
	        		  frmPremiumDetails.set("premium",premium);
	        		  request.getSession().setAttribute("frmPremiumDetails",frmPremiumDetails);
	        		  
	        	  }
	        	  session.setAttribute("premiumList", premiumList);
	        	
	            return this.getForward("selectPremiumDetails", mapping, request);
	        }// end of try
	        catch(TTKException expTTK)
	        {
	            return this.processExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processExceptions(request, mapping, new TTKException(exp, "endorsementdetails"));
	        }//end of catch(Exception exp)
	    }//end of doClosePolicyDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	      
	   
	 
	    public ActionForward deletePremiumDtls(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            HttpServletResponse response) throws Exception{
	        try
	        {
	        	log.debug("Inside deletePremiumDtls of PremiumDetailsAction");
	        	
	        	 DynaActionForm frmPremiumDetails=(DynaActionForm)form;
	        	  PolicyManager policyObject=this.getPolicyManagerObject();
	        	  PolicyDetailVO policyDetailVO=null;
	        	  String successMsg;
	        	  HttpSession session = request.getSession();	
	        	  String premiumSeqId = request.getParameter("premiumSeqId");
	        	  String deleteFlag =(String) request.getParameter("deleteFlag");
	        	  Long policyid = WebBoardHelper.getPolicySeqId(request);
	        	  if("".equals(premiumSeqId)){
	        		  premiumSeqId="0";
	        	  }
	        	  
	        	  policyDetailVO=new PolicyDetailVO();
	        	  policyDetailVO.setPremiumSeqId(Long.parseLong(premiumSeqId));
	        	  policyDetailVO.setPolicySeqID(policyid);
	        	  policyDetailVO.setDeleteYN(deleteFlag);
	        	  policyDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));//User Id
	        	  Long lngPremiumSeqId=null;
	        	  lngPremiumSeqId=policyObject.addPremiumDetails(policyDetailVO);
	        	  
	        	if(lngPremiumSeqId!=0){
	        		
	            ArrayList<String[]>	premiumList= policyObject.getPremiumDetails(lngPremiumSeqId,policyid);
	        		successMsg = "Premium Details Deleted Successfully";
	        		request.setAttribute("successMsg", successMsg);
	        		session.setAttribute("premiumList", premiumList);
	        		
	        	}
	        	
	        	frmPremiumDetails.initialize(mapping);
	            return this.getForward("selectPremiumDetails", mapping, request);
	        }// end of try
	        catch(TTKException expTTK)
	        {
	            return this.processExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processExceptions(request, mapping, new TTKException(exp, "endorsementdetails"));
	        }//end of catch(Exception exp)
	    }
	 
	    public ActionForward closePremium(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	            HttpServletResponse response) throws Exception{
	        try
	        {
	        	log.debug("Inside closePremium of PremiumDetailsAction");
	        	DynaActionForm frmPremiumDetails=(DynaActionForm)form;
	        	DynaActionForm frmPolicyDetails=(DynaActionForm)request.getSession().getAttribute("frmPolicyDetails");
	        	frmPremiumDetails.set("minimumAge", "");
	        	frmPremiumDetails.set("maximumAge", "");
	        	frmPremiumDetails.set("premium", "");
	        	request.getSession().setAttribute("frmPremiumDetails",frmPremiumDetails);
	        	request.getSession().setAttribute("frmPolicyDetails",frmPolicyDetails);
	        	return this.getForward("closePremiumDetails", mapping, request);
	        }// end of try
	        catch(TTKException expTTK)
	        {
	            return this.processExceptions(request, mapping, expTTK);
	        }//end of catch(TTKException expTTK)
	        catch(Exception exp)
	        {
	            return this.processExceptions(request, mapping, new TTKException(exp, "endorsementdetails"));
	        }//end of catch(Exception exp)
	    }//end of closePremium
	   
	 
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

}
