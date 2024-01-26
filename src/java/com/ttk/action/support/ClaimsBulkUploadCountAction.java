package com.ttk.action.support;
import java.io.PrintWriter;
import java.io.Reader;
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
import com.ttk.business.claims.ClaimIntimationSmsManager;
import com.ttk.business.support.SupportManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.DhpoWebServiceVO;
import com.ttk.dto.onlineforms.insuranceLogin.DashBoardVO;

import formdef.plugin.util.FormUtils;



public class ClaimsBulkUploadCountAction extends TTKAction {

	private  Logger log = Logger.getLogger( ClaimsBulkUploadCountAction.class ); // Getting Logger for this Class file
	
	private static final String strClaimsBulkUploadCount="ClaimsBulkUploadCount";
	private static final String strBulkUploadCount="bulkUploadCount";
	
	public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
										HttpServletResponse response) throws Exception{
		try
		{
			log.debug("Inside ClaimsBulkUploadCountAction doDefault");
			setLinks(request);
			return this.getForward(strBulkUploadCount,mapping,request);
		}
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsBulkUploadCount"));
		}
	}//end of doDefault()

	
	 public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
             HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doGenerateReport method of ClaimsBulkUploadCountAction");
			setOnlineLinks(request);
			
			DynaActionForm frmBulkUploadCount =(DynaActionForm)form;
			DashBoardVO dashBoardVO	=	null;
			String sStartDate = frmBulkUploadCount.getString("sStartDate");
			String sEndDate = frmBulkUploadCount.getString("sEndDate");
			SupportManager supportManager = this.getSupportManagerObject();;	
			dashBoardVO	=	supportManager.getDashBoardDetails(frmBulkUploadCount.getString("sStartDate"),frmBulkUploadCount.getString("sEndDate"));
			frmBulkUploadCount = (DynaActionForm)FormUtils.setFormValues("frmBulkUploadCount",dashBoardVO,this,mapping,request);
			frmBulkUploadCount.set("sStartDate", sStartDate);
			frmBulkUploadCount.set("sEndDate", sEndDate);
			request.getSession().setAttribute("frmBulkUploadCount", frmBulkUploadCount);
			return this.getForward(strBulkUploadCount, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsBulkUploadCount"));
		}
	 }	
	 
	 
	 public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
             HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doReset method of ClaimsBulkUploadCountAction");
			setOnlineLinks(request);
			
			DynaActionForm frmBulkUploadCount =(DynaActionForm)form;
			frmBulkUploadCount.initialize(mapping);
			
			request.getSession().setAttribute("frmBulkUploadCount", frmBulkUploadCount);
			return this.getForward(strBulkUploadCount, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsBulkUploadCount"));
		}
	 }	
	 
	 
		private SupportManager getSupportManagerObject() throws TTKException
	
	{
		SupportManager supportManager = null;
		try
		{
			if(supportManager == null)
			{
				InitialContext ctx = new InitialContext();
				supportManager = (SupportManager) ctx.lookup("java:global/TTKServices/business.ejb3/SupportManagerBean!com.ttk.business.support.SupportManager");
			}//end of if(supportManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strClaimsBulkUploadCount);
		}//end of catch
		return supportManager;
	}//end of getsupportManagerObject()
}//end of CardPrintingAction