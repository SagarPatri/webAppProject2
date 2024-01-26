/**
 * @ (#)  InvoiceGeneralAction.java Oct 25th, 2007
 * Project      : TTK HealthCare Services
 * File         : InvoiceGeneralAction.java
 * Author       : Krupa J
 * Company      : Span Systems Corporation
 * Date Created : Oct 25th, 2007
 *
 * @author       :  Krupa J
 * Modified by   :  
 * Modified date : 
 * Reason        :  
 * Modified by   : 
 */

package com.ttk.action.finance;

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
import com.ttk.action.table.TableData;
import com.ttk.business.finance.FloatAccountManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.finance.InvoiceVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

public class InvoiceGeneralAction extends TTKAction
{
	private static final Logger log = Logger.getLogger( InvoiceGeneralAction.class );
	
	private static final String strInvoiceDetails="invoiceinfo";
	private static final String strCreditDetails="creditinfo";

	
	private static final String strInvoicesSearch="invoicesdetails";	
	private static final String strInvoice="Invoice";
    private static final String strGroupList="grouplist";
    private static final String strSetInvoiceDetails="setinvoicedetails";
	private static final String strReportdisplay="reportdisplay";

    

	
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
    public ActionForward doView(ActionMapping mapping,ActionForm form,
    									   HttpServletRequest request,HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.info("Inside InvoiceGeneralAction doView------------");
    		DynaActionForm frmInvoiceGeneral=(DynaActionForm)form;
    		HttpSession session=request.getSession();
    		UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");

    		InvoiceVO invoiceVO=new InvoiceVO();
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		StringBuffer strCaption= new StringBuffer();
    		String strAddmode = "";
    		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
    		{
	    		invoiceVO = (InvoiceVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
	    		invoiceVO = floatAccountManagerObject.getInvoiceDetail(invoiceVO.getSeqID(), TTKCommon.getUserSeqId(request));
	    		strCaption.append("Invoice Details  - ").append("Edit");
	    		strAddmode = "EDIT";
    		}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
    		else
    		{
    			TTKException expTTK = new TTKException();
				expTTK.setMessage("error.invoice.required");
				throw expTTK;
    		}//end of else
    		frmInvoiceGeneral= (DynaActionForm)FormUtils.setFormValues("frmInvoiceGeneral", invoiceVO, this, 
    																mapping, request);
    		frmInvoiceGeneral.set("caption",String.valueOf(strCaption));
    		frmInvoiceGeneral.set("addmode",strAddmode);
    		frmInvoiceGeneral.set("paymentTypeFlag",invoiceVO.getPaymentTypeFlag());
    		
    		request.getSession().setAttribute("frmInvoiceGeneral",frmInvoiceGeneral);
    		if(invoiceVO.getPaymentTypeFlag().equalsIgnoreCase("DEL")){
    			 userSecurityProfile.getSecurityProfile().setActiveTab("Credit Note");
    			return this.getForward(strCreditDetails, mapping, request);
    	
    		}else{
    			 userSecurityProfile.getSecurityProfile().setActiveTab("General");
    			return this.getForward(strInvoiceDetails, mapping, request);
    		}
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strInvoice));
    	}//end of catch(Exception exp)
    }//end of doViewTransaction(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
	public ActionForward doSelectPolicy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside InvoiceGeneralAction doSelectPolicy");
			//DynaActionForm frmInvoiceGeneral = (DynaActionForm) form;
    		DynaActionForm frmInvoiceGeneral =(DynaActionForm) request.getSession().getAttribute("frmInvoiceGeneral");
            String paymentType = request.getParameter("paymentType");
			InvoiceVO invoiceVO = new InvoiceVO();
			TableData tableData = (TableData)(request.getSession()).getAttribute("tableDataAssociatePolicy");
			FloatAccountManager floatAccountManagerObject = this.getfloatAccountManagerObject();
			if (!(TTKCommon.checkNull(request.getParameter("rownum"))
					.equals(""))) {
				invoiceVO = (InvoiceVO) tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
			}// end of
			//System.out.println("getPaymentTypeFlag on select---"+invoiceVO.getPaymentTypeFlag());
			//frmInvoiceGeneral= (DynaActionForm)FormUtils.setFormValues("frmInvoiceGeneral", invoiceVO, this, mapping, request);
			frmInvoiceGeneral.set("policyNbr", invoiceVO.getPolicyNbr());
			frmInvoiceGeneral.set("policySeqID", invoiceVO.getPolicySeqID().toString());
			request.getSession().setAttribute("frmInvoiceGeneral",frmInvoiceGeneral);
			
			  if(paymentType.equalsIgnoreCase("ADD")){
					return this.getForward(strInvoiceDetails, mapping, request);

			  }else {
					return this.getForward(strCreditDetails, mapping, request);
	     	    }
			  
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(ETTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strInvoice));
		}// end of catch(Exception exp)
	}// end of doViewTransaction(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
    
	/**
     * This method is called from the struts framework.
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
    		setLinks(request);
    		log.debug("Inside InvoiceGeneralAction doAdd");
    		StringBuffer strCaption= new StringBuffer();
    		 java.util.Date dt = new java.util.Date();
    		 java.text.SimpleDateFormat sdt;
    		 sdt = new java.text.SimpleDateFormat("dd/MM/yyyy");
    		 String strcurrentDate = sdt.format(dt);
    		 
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		String strAddmode = "";
    		DynaActionForm frmInvoiceGeneral=(DynaActionForm)form;
    		frmInvoiceGeneral.initialize(mapping);
    		 String paymentType = request.getParameter("paymentType");
     	    if(paymentType.equalsIgnoreCase("ADD")){
        		strCaption.append("Invoice Details  - ").append("Add");
     	    }else if(paymentType.equalsIgnoreCase("DEL")) {
     	    	strCaption.append("Credit Note Details  - ").append("Add");
     	    }
    		strAddmode = "ADD";
    		frmInvoiceGeneral.set("caption",String.valueOf(strCaption));
    		frmInvoiceGeneral.set("addmode",strAddmode);
    		frmInvoiceGeneral.set("paymentTypeFlag",paymentType);

    		//frmInvoiceGeneral.set("fromDate",TTKCommon.getFormattedDate(floatAccountManagerObject.getNewInvoiceFromDate()));
    		frmInvoiceGeneral.set("currentDate",strcurrentDate);

    		request.getSession().setAttribute("frmInvoiceGeneral",frmInvoiceGeneral);
    		
    		if(paymentType.equalsIgnoreCase("DEL")){
   			return this.getForward(strCreditDetails, mapping, request);
   	
   		}else{
   			return this.getForward(strInvoiceDetails, mapping, request);
   		}
    		
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strInvoice));
    	}//end of catch(Exception exp)
    }//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    /**
     * This method is called from the struts framework.
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
    public ActionForward dogetFromdate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    						   HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.info("Inside dogetFromdate doAdd");
    		StringBuffer strCaption= new StringBuffer();
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		String strAddmode = "";
    		DynaActionForm frmInvoiceGeneral=(DynaActionForm)form;
    		strCaption.append("Invoice Details  - ").append("Add");
    		strAddmode = "ADD";
    		frmInvoiceGeneral.set("caption",String.valueOf(strCaption));
    		frmInvoiceGeneral.set("addmode",strAddmode);
    
    		//frmInvoiceGeneral.set("fromDate",TTKCommon.getFormattedDate(floatAccountManagerObject.getNewInvoiceFromDate()));
    		request.getSession().setAttribute("frmInvoiceGeneral",frmInvoiceGeneral);
    		return this.getForward(strInvoiceDetails, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strInvoice));
    	}//end of catch(Exception exp)
    }//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    		log.info("Inside InvoiceGeneralAction doSave");
    		InvoiceVO invoiceVO=new InvoiceVO();
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		StringBuffer strCaption= new StringBuffer();
    		String strAddmode = "";
    		String paymentType = request.getParameter("paymentType");

    		DynaActionForm frmInvoiceGeneral=(DynaActionForm)form;
    		invoiceVO = (InvoiceVO)FormUtils.getFormValues(frmInvoiceGeneral, this, mapping, request);
    		invoiceVO.setUpdatedBy(TTKCommon.getUserSeqId(request));//User Id
    		
    		invoiceVO.setPaymentTypeFlag(paymentType);
    		
    		long lngcount=floatAccountManagerObject.saveInvoiceDetail(invoiceVO);
    	
    		
    		if(lngcount>0)
    		{
    			if(!(TTKCommon.checkNull((String)frmInvoiceGeneral.get("seqID")).equals("")))
    				//if(transactionVO.getTransSeqID()!=null)
    			{
    				request.setAttribute("updated","message.savedSuccessfully");
    				strCaption.append("Invoice Details  - ").append("Edit");
    				invoiceVO = floatAccountManagerObject.getInvoiceDetail(invoiceVO.getSeqID(),TTKCommon.getUserSeqId(request));
    				strAddmode = "EDIT";
    			}//end of if(transactionVO.getTransSeqId()!=null)
    			else
    			{
    				request.setAttribute("updated","message.addedSuccessfully");
    				strCaption.append("Invoice Details  - ").append("Add");
    				invoiceVO.setSeqID(lngcount);
    				strAddmode = "ADD";
    			}//end of else
    		}//end of if(icount>0)
    		invoiceVO = floatAccountManagerObject.getInvoiceDetail(invoiceVO.getSeqID(),TTKCommon.getUserSeqId(request));
    		frmInvoiceGeneral = (DynaActionForm)FormUtils.setFormValues("frmInvoiceGeneral", invoiceVO, this,
    				mapping, request);
    		frmInvoiceGeneral.set("caption",strCaption.toString());
    		frmInvoiceGeneral.set("addmode",strAddmode);
    		request.getSession().setAttribute("frmInvoiceGeneral",frmInvoiceGeneral);
    		  if(paymentType.equalsIgnoreCase("ADD")){
					return this.getForward(strInvoiceDetails, mapping, request);

			  }else {
					return this.getForward(strCreditDetails, mapping, request);
	     	    }
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strInvoice));
    	}//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    

    
	public ActionForward doSetFormValues(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside InvoiceGeneralAction doSetFormValues");
			 java.util.Date dt = new java.util.Date();
    		 java.text.SimpleDateFormat sdt;
    		 sdt = new java.text.SimpleDateFormat("dd/MM/yyyy");
    		 String strcurrentDate = sdt.format(dt);
    		 
			InvoiceVO invoiceVO = new InvoiceVO();
			FloatAccountManager floatAccountManagerObject = this
					.getfloatAccountManagerObject();
			StringBuffer strCaption = new StringBuffer();
			String strAddmode = "";
			DynaActionForm frmInvoiceGeneral = (DynaActionForm) form;
			invoiceVO = (InvoiceVO) FormUtils.getFormValues(frmInvoiceGeneral,
					this, mapping, request);
			invoiceVO.setUpdatedBy(TTKCommon.getUserSeqId(request));// User Id
		
			frmInvoiceGeneral = (DynaActionForm) FormUtils.setFormValues("frmInvoiceGeneral", invoiceVO, this, mapping, request);
			if(invoiceVO.getPaymentTypeFlag().equalsIgnoreCase("DEL")){
			frmInvoiceGeneral.set("fromDate", strcurrentDate);
			}
			frmInvoiceGeneral.set("caption", strCaption.toString());
			frmInvoiceGeneral.set("addmode", strAddmode);
			request.getSession().setAttribute("frmInvoiceGeneral",frmInvoiceGeneral);
			//return this.getForward(strSetInvoiceDetails, mapping, request);
			 return mapping.findForward(strSetInvoiceDetails);		
			 }// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(ETTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strInvoice));
		}// end of catch(Exception exp)
	}// end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)
    
    /**
     * This method is used to reload the screen when the reset button is pressed.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    							 HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.info("Inside ---------- doReset");
    		DynaActionForm frmInvoiceGeneral=(DynaActionForm)form;
    		InvoiceVO invoiceVO=null;
    		StringBuffer strCaption= new StringBuffer();
    		String strAddmode = "";
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		if(frmInvoiceGeneral.get("seqID")!=null && !frmInvoiceGeneral.get("seqID").equals(""))
    		{
    			strCaption.append("Invoice Details  - ").append("Edit");
    			invoiceVO = floatAccountManagerObject.getInvoiceDetail(TTKCommon.getLong((String)frmInvoiceGeneral.get("seqID")), 
    												   TTKCommon.getUserSeqId(request));
    			strAddmode = "EDIT";
    		}//end of if(frmTransDetails.get("transSeqID")!=null && !frmTransDetails.get("transSeqID").equals(""))
    		else
    		{
    			frmInvoiceGeneral.initialize(mapping);
    			invoiceVO=new InvoiceVO();
    			strCaption.append("Invoice Details  - ").append("Add");
    			strAddmode = "ADD";
    		}//end of else
    		frmInvoiceGeneral = (DynaActionForm)FormUtils.setFormValues("frmInvoiceGeneral", invoiceVO,
    																				this, mapping, request);
    		frmInvoiceGeneral.set("caption",strCaption.toString());
    		frmInvoiceGeneral.set("addmode",strAddmode);
    	//	frmInvoiceGeneral.set("fromDate",TTKCommon.getFormattedDate(floatAccountManagerObject.getNewInvoiceFromDate()));
    		request.getSession().setAttribute("frmInvoiceGeneral",frmInvoiceGeneral);
    		//System.out.println("flag reset----"+invoiceVO.getPaymentTypeFlag());
    		if(invoiceVO.getPaymentTypeFlag().equalsIgnoreCase("ADD")){
        		return this.getForward(strInvoiceDetails, mapping, request);
    		}else {
    			return this.getForward(strCreditDetails, mapping, request);
    		}
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strInvoice));
    	}//end of catch(Exception exp)
    }//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    	try{
    		setLinks(request);
    		log.debug("Inside TransactionSearchAction doReset");
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		if(tableData.getSearchData().size()>1)
    		{
    			ArrayList alTransSearch = floatAccountManagerObject.getInvoiceList(tableData.getSearchData());
    			tableData.setData(alTransSearch, "search");
    			//set the table data object to session
    			request.getSession().setAttribute("tableData",tableData);
    		}//end of if(tableData.getSearchData().size()>1)
    		return this.getForward(strInvoicesSearch, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strInvoice));
    	}//end of catch(Exception exp)
    }//end of Close(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doSelectGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
			try{
			setLinks(request);
			log.debug("Inside PolicyAction doSelectGroup");
			//DynaActionForm frmInvoice=(DynaActionForm)form;
			//frmPolicyAccountInfo.set("frmChanged","changed");
			//request.getSession().setAttribute("frmInvoice",frmInvoice);
			return mapping.findForward(strGroupList);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processOnlineExceptions(request,mapping,expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processOnlineExceptions(request,mapping,new TTKException(exp,"policyList"));
			}//end of catch(Exception exp)
}//end of doSelectGroup(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
	public ActionForward doInvoiceXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
				try{
				setLinks(request);
				log.debug("Inside InvoiceAction doInvoiceXL");
				
				return (mapping.findForward(strReportdisplay));
				}
				catch(TTKException expTTK)
				{
				return this.processExceptions(request,mapping,expTTK);
				}//end of catch(ETTKException expTTK)
				catch(Exception exp)
				{
				return this.processExceptions(request,mapping,new TTKException(exp,strInvoice));
				}//end of catch(Exception exp)
}//end of doInvoiceXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse
														//response)

    /**
	 * Returns the FloatAccountManager session object for invoking methods on it.
	 * @return FloatAccountManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private FloatAccountManager getfloatAccountManagerObject() throws TTKException
	{
		FloatAccountManager floatAccountManager = null;
		try
		{
			if(floatAccountManager == null)
			{
				InitialContext ctx = new InitialContext();
				floatAccountManager = (FloatAccountManager) ctx.lookup("java:global/TTKServices/business.ejb3/FloatAccountManagerBean!com.ttk.business.finance.FloatAccountManager");
			}//end of if(contactManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strInvoice);
		}//end of catch
		return floatAccountManager;
	}//end getfloatAccountManagerObject()
}//end of InvoiceGeneralAction
