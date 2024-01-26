/**
* @ (#) ChequeDetailAction.java Jun 17th, 2006
* Project 		: TTK HealthCare Services
* File 			: ChequeDetailAction.java
* Author 		: Krishna K H
* Company 		: Span Systems Corporation
* Date Created 	: Jun 17th, 2006
*
* @author 		: Krishna K H
* Modified by 	:
* Modified date :
* Reason 		:
*/

package com.ttk.action.finance;

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
import com.ttk.business.empanelment.LogManager;
import com.ttk.business.finance.ChequeManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.empanelment.LogVO;
import com.ttk.dto.empanelment.TdsCertificateVO;
import com.ttk.dto.finance.BankAddressVO;
import com.ttk.dto.finance.ChequeDetailVO;
import com.ttk.dto.preauth.PreAuthDetailVO;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for displaying the Cheque Details.
 * This class also provides option for updating the Cheque Details .
 */
public class ChequeDetailAction extends TTKAction
{
    //action forword
    private static final String strPayments = "payments";
    private static final String strChequeSearch ="chequesearch";
    private static final String strChequedetail ="chequedetail";
    private static Logger log = Logger.getLogger( ChequeDetailAction.class );
 	private static final String strChequeDetail="groupdetail";
 	private static final String strViewDocsUpload="ViewDocsUpload";
	private static final String strViewUploadClose="viewUploadClose";
	private static final String strdeleteDocsUpload="docsFinanceUploadDelete";
 	
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
 			setLinks(request);
 			log.debug("Inside ChequeDetailAction doDefault");
 			TTKException expTTK = new TTKException();
 			if(TTKCommon.getActiveSubLink(request).equals("Cheque Information"))
 			{
 				expTTK.setMessage("error.cheque.required");
 			}//end of if(TTKCommon.getActiveSubLink(request).equals("Policies"))
 			throw expTTK;
 		}//end of try
 		catch(TTKException expTTK)
 		{
 			return this.processExceptions(request, mapping, expTTK);
 		}//end of catch(TTKException expTTK)
 		catch(Exception exp)
 		{
 			return this.processExceptions(request, mapping, new TTKException(exp, strChequeDetail));
 		}//end of catch(Exception exp)
 	}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
 	public ActionForward doViewCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,
 									  HttpServletResponse response) throws Exception{
 		try{
 			setLinks(request);
 			log.debug("Inside ChequeDetailAction doViewCheque");
 			ChequeDetailVO chequeDetailVO = null;
 			BankAddressVO bankAddressVO = null;
 			StringBuffer strCaption = new StringBuffer();
 			DynaActionForm frmChequeDetail =(DynaActionForm)form;
 			ChequeManager chequeObject=this.getChequeManagerObject();
 			chequeDetailVO = new ChequeDetailVO();
 			bankAddressVO = new BankAddressVO();
 			Long lngFloatSeqID = null;
 			if(TTKCommon.getActiveSubLink(request).equals("Float Account"))
 			{
 				lngFloatSeqID = TTKCommon.getWebBoardId(request);
 				strCaption.append("Cheque Printing Details - ["+TTKCommon.getWebBoardDesc(request)+"]");
 				
 			}
 			if(frmChequeDetail.getString("seqID")!=null)
 			{
 				chequeDetailVO=chequeObject.getChequeDetail(TTKCommon.getLong(frmChequeDetail.getString("seqID")),
 															TTKCommon.getLong(frmChequeDetail.getString("paymentSeqId")),
 																	lngFloatSeqID,TTKCommon.getUserSeqId(request));
 			}
 			if(TTKCommon.getActiveSubLink(request).equals("Cheque Information"))
 			{
 				strCaption.append("Payment Transaction Details - ["+chequeDetailVO.getChequeNo()+"]");
 				
 			}
 			if(chequeDetailVO!=null && chequeDetailVO.getBankAddressVO()!=null)
 			{
 				bankAddressVO=chequeDetailVO.getBankAddressVO();
 			}//end of if(chequeDetailVO!=null && chequeDetailVO.getBankAddressVO()!=null)
 			frmChequeDetail = (DynaActionForm)FormUtils.setFormValues("frmChequeDetail",chequeDetailVO,this,mapping,
 																	   request);
 			frmChequeDetail.set("bankAddressVO", (DynaActionForm)FormUtils.setFormValues("frmFinanceBankAddress",
 																			   bankAddressVO,this,mapping,request));
 			frmChequeDetail.set("status",chequeDetailVO.getStatusTypeID());
 			frmChequeDetail.set("caption",strCaption.toString());
 			
 			request.getSession().setAttribute("claimSettlementNo", chequeDetailVO.getClaimSettNo());
 			request.getSession().setAttribute("isAccessable",true);
 			request.getSession().setAttribute("claimSeqId", chequeDetailVO.getClaimSeqId());
 			request.getSession().setAttribute("frmChequeDetail",frmChequeDetail);
 			return (mapping.findForward(strChequedetail));
 		}//end of try
 		catch(TTKException expTTK)
 		{
 			return this.processExceptions(request, mapping, expTTK);
 		}//end of catch(TTKException expTTK)
 		catch(Exception exp)
 		{
 			return this.processExceptions(request, mapping, new TTKException(exp, strChequeDetail));
 		}//end of catch(Exception exp)
 	}//end of doViewCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
 	
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
    	log.debug("Inside ChequeDetailAction doReset");
    	return doViewCheque(mapping,form,request,response);
    }//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    		log.debug("Inside ChequeDetailAction doSave");
    		ChequeDetailVO chequeDetailVO = null;
    		BankAddressVO bankAddressVO = null;
    		DynaActionForm frmChequeDetail =(DynaActionForm)form;
    		ChequeManager chequeObject=this.getChequeManagerObject();
    		StringBuffer strCaption = new StringBuffer();
    		chequeDetailVO = new ChequeDetailVO();
    		bankAddressVO = new BankAddressVO();
    		//saveCheque
    		//get the value from form and store it to the respective VO's
    		chequeDetailVO=(ChequeDetailVO)FormUtils.getFormValues(frmChequeDetail,this,mapping,request);
    		chequeDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
    		ActionForm chequeAddressForm=(ActionForm)frmChequeDetail.get("bankAddressVO");
    		bankAddressVO=(BankAddressVO)FormUtils.getFormValues(chequeAddressForm,"frmFinanceBankAddress",
    														     this,mapping,request);
    		chequeDetailVO.setBankAddressVO(bankAddressVO);
    		long result=chequeObject.saveCheque(chequeDetailVO);
    		if(result>0)
    		{
    			request.setAttribute("updated","message.savedSuccessfully");
    		}//end of if(result>0)
    		if(TTKCommon.getActiveSubLink(request).equals("Float Account"))
    		{
    			strCaption.append("Cheque Printing Details - ["+TTKCommon.getWebBoardDesc(request)+"]");
    			
    		}
    		else if(TTKCommon.getActiveSubLink(request).equals("Cheque Information"))
    		{
    			strCaption.append("Cheque Details - ["+chequeDetailVO.getChequeNo()+"]");
    		}//end of else if(TTKCommon.getActiveSubLink(request).equals("Cheque Information"))
    		frmChequeDetail.set("status",chequeDetailVO.getStatusTypeID());
    		frmChequeDetail.set("caption",strCaption.toString());
    		request.getSession().setAttribute("frmChequeDetail",frmChequeDetail);
    		return (mapping.findForward("savechequedetails"));
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeDetail));
    	}//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    						     HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ChequeDetailAction doClose");
    		String strForword="";
    		if(TTKCommon.getActiveSubLink(request).equals("Float Account"))
    		{
    			
    			request.setAttribute("CLG", "CLG");
                strForword=strPayments;
    		}//end of if(TTKCommon.getActiveSubLink(request).equals("Float Account"))
            else
            {
                strForword=strChequeSearch;
            }//end of else
    		return (mapping.findForward(strForword));
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeDetail));
    	}//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

    /**
     * Returns the ChequeManager session object for invoking methods on it.
     * @return ChequeManager session object which can be used for method invocation
     * @exception throws TTKException
     */
    private ChequeManager getChequeManagerObject() throws TTKException
    {
        ChequeManager chequeManager = null;
        try
        {
            if(chequeManager == null)
            {
                InitialContext ctx = new InitialContext();
                chequeManager = (ChequeManager) ctx.lookup("java:global/TTKServices/business.ejb3/ChequeManagerBean!com.ttk.business.finance.ChequeManager");
            }//end if(chequeManager == null)
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "ChequeSearch");
        }//end of catch
        return chequeManager;
    }//end getChequeManagerObject()
    
    
 
    
    public ActionForward  doviewUploadDocuments(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
	{
		try
		{   
			String claimSettNo	=	(String)request.getParameter("claimSettNo");
			log.debug("Inside the doDefault method of ClaimAction");
			request.getSession().setAttribute("preAuth_seq_id_docs_uploads", claimSettNo);
			setLinks(request);
			String strTable = "";
			StringBuffer sbfCaption= new StringBuffer();	
			String strEmpanelNumber=null;
			String strPreauthNumber=null;

			DynaActionForm frmDocsUpload= null;
			ArrayList<Object> alDocsUploadList = new ArrayList<Object>();
    		ChequeManager chequeObject=null;
    		TableData tableDataDocsUpload =(TableData)request.getSession().getAttribute("tableDataDocsUpload");
	//		frmClaimGeneral.set("caption",sbfCaption.toString());
			//get the table data from session if exists			
			if(tableDataDocsUpload==null){
				//create new table data object
				tableDataDocsUpload = new TableData();
			}//end of if(tableData==null) 	
			//create the required grid table
			strTable = "DocsUploadFilesTable";
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			if(strSortID.equals(""))
			{
				tableDataDocsUpload.createTableInfo(strTable,null);
			//	tableDataDocsUpload.setSearchData(this.populateSearchCriteria(request));
				tableDataDocsUpload.modifySearchData("search");
			}//end of if(strSortID.equals(""))
			else
			{
				tableDataDocsUpload.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
				tableDataDocsUpload.modifySearchData("sort");//modify the search data	
			}// end of else	
			chequeObject=this.getChequeManagerObject();
			alDocsUploadList = chequeObject.getBankDocsUploadsList(claimSettNo);
			tableDataDocsUpload.setData(alDocsUploadList,"search");
			request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
			request.getSession().setAttribute("frmDocsUpload",frmDocsUpload);
			return this.getForward(strViewDocsUpload, mapping, request);
		} // end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strChequeDetail));
		}//end of catch(Exception exp)
	} // end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			//HttpServletResponse response) throws TTKException 
			
    /**
	 * This method is used to navigate to previous screen when close  button is clicked.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doUploadClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException
	{
		try
		{
			log.debug("Inside the doClose method of UploadMOUDocsAction");
			setLinks(request);
			return mapping.findForward(strViewUploadClose);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strChequeDetail));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				//HttpServletResponse response) 
    
	/**
	 * This method is used to delete selected record(s)from the db
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	/*public ActionForward doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{
			
			if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
			{
			setLinks(request);
			log.debug("Inside PreauthAction doDelete");
			StringBuffer sbfDeleteId = new StringBuffer();
			ArrayList <Object>alCertificateDelete=new ArrayList<Object>();
			ArrayList <Object>alAssociatedCertificatesList=null;
			DynaActionForm frmDocsUpload=(DynaActionForm)form;
			StringBuffer sbfCaption= new StringBuffer();
			//String strForwardPath=strDocsdelete;
			sbfCaption = sbfCaption.append(this.buildCaption(request));
			ChequeManager chequeObject=this.getChequeManagerObject();
			TableData tableDataDocsUpload = null;
			//get the Table Data From the session 
			if(request.getSession().getAttribute("tableDataDocsUpload")!=null)
			{
				tableDataDocsUpload=(TableData)(request.getSession()).getAttribute("tableDataDocsUpload");
			}//end of if(request.getSession().getAttribute("tableDataLinkDetails")!=null)
			else
			{
				tableDataDocsUpload=new TableData();
			}//end of else
			sbfDeleteId.append(populateDeleteIds(request, (TableData)request.getSession().getAttribute("tableDataDocsUpload")));
			alCertificateDelete.add(String.valueOf(sbfDeleteId));
			frmDocsUpload.initialize(mapping);
			int iCount=chequeObject.deleteDocsUpload(alCertificateDelete,"tableDataDocsUpload");
			log.info("iCount :"+iCount);
			//refresh the data in order to get the new records if any.
			
			chequeObject=this.getChequeManagerObject();
			//DynaActionForm frmClaimGeneral=null;
			String claimSettNo	=	(String)request.getSession().getAttribute("preAuth_seq_id_docs_uploads");
			  
			frmDocsUpload=(DynaActionForm) request.getSession().getAttribute("frmDocsUpload");
			alAssociatedCertificatesList = chequeObject.getBankDocsUploadsList(claimSettNo);
			tableDataDocsUpload.setData(alAssociatedCertificatesList,"search");
			request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
			request.setAttribute("frmDocsUpload",frmDocsUpload);
			
			}//End of if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
			return mapping.findForward(strdeleteDocsUpload);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strChequeDetail));
			}//end of catch(Exception exp)
		}//end of doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	
	
	*//**
	 * This method  prepares the Caption based on the flow and retunrs it
	 * @param request current HttpRequest
	 * @return String caption built
	 * @throws TTKException
	 *//*
	private String buildCaption(HttpServletRequest request)throws TTKException
	{
		StringBuffer sbfCaption=new StringBuffer();
		sbfCaption.append(request.getSession().getAttribute("ConfigurationTitle"));
		return sbfCaption.toString();
	}//end of buildCaption(HttpServletRequest request)
	
	*//**
	 * Returns a string which contains the | separated sequence id's to be deleted
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 * @param tableData TableData object which contains the value objects
	 * @return String which contains the | separated sequence id's to be delete
	 * @throws TTKException If any run time Excepton occures
	 *//*
	private String populateDeleteIds(HttpServletRequest request, TableData tableDataDocsUpload)throws TTKException
	{
		if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
		{			
			String[] strChk = request.getParameterValues("chkopt");
			StringBuffer sbfDeleteId = new StringBuffer();
			if(strChk!=null&&strChk.length!=0)
			{
				for(int i=0; i<strChk.length;i++)
				{
					if(strChk[i]!=null)
					{
						//extract the sequence id to be deleted from the value object
						if(i == 0)
						{
							sbfDeleteId.append("|").append(String.valueOf(((PreAuthDetailVO)tableDataDocsUpload.getData().get(Integer.parseInt(strChk[i]))).getMouDocSeqID()));
						}//end of if(i == 0)
						else
						{
							sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((PreAuthDetailVO)tableDataDocsUpload.getData().get(Integer.parseInt(strChk[i]))).getMouDocSeqID().intValue()));
						}//end of else
					}//end of if(strChk[i]!=null)
				}//end of for(int i=0; i<strChk.length;i++)
				sbfDeleteId=sbfDeleteId.append("|");
			}//end of if(strChk!=null&&strChk.length!=0)
			return String.valueOf(sbfDeleteId);
		}//end of populateDeleteId(HttpServletRequest request, TableData tableData)
						
	else{
		
		String[] strChk = request.getParameterValues("chkopt");
		StringBuffer sbfDeleteId = new StringBuffer();
		if(strChk!=null&&strChk.length!=0)
		{
			for(int i=0; i<strChk.length;i++)
			{
				if(strChk[i]!=null)
				{
					//extract the sequence id to be deleted from the value object
					if(i == 0)
					{
						sbfDeleteId.append("|").append(String.valueOf(((TdsCertificateVO)tableDataDocsUpload.getData().
								get(Integer.parseInt(strChk[i]))).getCertSeqId()));
					}//end of if(i == 0)
					else
					{
						sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((TdsCertificateVO)tableDataDocsUpload.getData().
								get(Integer.parseInt(strChk[i]))).getCertSeqId().intValue()));
					}//end of else
				}//end of if(strChk[i]!=null)
			}//end of for(int i=0; i<strChk.length;i++)
			sbfDeleteId=sbfDeleteId.append("|");
		}//end of if(strChk!=null&&strChk.length!=0)
		return String.valueOf(sbfDeleteId);
	}//end of populateDeleteId(HttpServletRequest request, TableData tableData)

}//else
*/	
	public ActionForward doLogDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.info("Inside CustomerBankDetailsAction doLogDetails");
		//	Boolean isAccessable = false;
			DynaActionForm frmSearchCheques = (DynaActionForm) form;
			
        //    request.getSession().removeAttribute("isAccessable");
			/*if (request.getSession().getAttribute("isAccessable") != null)
				isAccessable = (Boolean) request.getSession().getAttribute(
						"isAccessable");
			if (isAccessable == true) {
				frmSearchCheques.set("caption", "Alert Details");
				frmSearchCheques.set("sStartDate", "");
				frmSearchCheques.set("sEndDate", "");
				
				request.getSession().setAttribute("frmSearchCheques",
						frmSearchCheques);
				TableData tableDataLog = new TableData();
				// create the required grid table
				tableDataLog.createTableInfo("HospitalLogTable", new ArrayList());
				request.getSession().setAttribute("sStartDate", "");
				request.getSession().setAttribute("sEndDate", "");
				request.getSession().setAttribute("tableDataLog",
						tableDataLog);
			} else {*/
				/*TTKException expTTK = new TTKException();
				expTTK.setMessage("error.cheque.required");
				throw expTTK;*/
				TableData tableData = TTKCommon.getTableData(request);

	            ((DynaActionForm)form).initialize(mapping);//reset the form data
	            //create new table data object
	    		tableData = new TableData();
	    		//create the required grid table
	    		tableData.createTableInfo("AlertClaimsTable",new ArrayList());
	    		request.getSession().setAttribute("tableData",tableData);
				return mapping.findForward("alertchequesearch");
		//	}

		//	return mapping.findForward("chequeInfoAlertLog");
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "BankAccountSearch"));
		}// end of catch(Exception exp)
	}// end of doViewBankAccount(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse//response)
	
	public ActionForward doSearchChequeInfoLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside LogAction doSearch");
			setLinks(request);
			ArrayList alLogList = null;
			DynaActionForm frmHospitalLog = (DynaActionForm) form;
			LogManager logManagerObj=this.getLogManagerObject();
			TableData tableDataLog = null;
			if ((request.getSession()).getAttribute("tableDataLog") == null) {
				tableDataLog = new TableData();
			}// end of if((request.getSession()).getAttribute("tableDataLog") ==
				// null)
			else {
				tableDataLog = (TableData) (request.getSession())
						.getAttribute("tableDataLog");
			}// end of else
			
			String strSortID = TTKCommon.checkNull(request
					.getParameter("sortId"));
			if (!strSortID.equals("")) {
				tableDataLog.setSortData(TTKCommon.checkNull(request
						.getParameter("sortId")));
				tableDataLog.modifySearchData("sort");// modify the search data
			}// end of if(!strSortID.equals(""))
			else {
					tableDataLog.createTableInfo("HospitalLogTable", null);
				tableDataLog.setSearchData(this.populateSearchCriteria((DynaActionForm) form, request));
				tableDataLog.setSortData("0001");
				tableDataLog.setSortOrder("DESC");
				tableDataLog.modifySearchData("search");
			}// end of else
			
			alLogList=logManagerObj.getClaimPreAuthLogList(tableDataLog.getSearchData());
			tableDataLog.setData(alLogList, "search");
			request.getSession().setAttribute("tableDataLog", tableDataLog);
		//	StringBuffer strCaption = buildCaption(request);
			frmHospitalLog.set("caption", "Cheque Info AlertLog");
			return this.getForward("chequeInfoAlertLog", mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "log"));
		}// end of catch(Exception exp)
	}// end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)
	
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmChequeDetail,HttpServletRequest request) throws TTKException
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add("C");
		alSearchParams.add(request.getSession().getAttribute("claimSeqId"));
		alSearchParams.add((String)frmChequeDetail.get("logType"));
		alSearchParams.add((String)frmChequeDetail.get("sStartDate"));
		alSearchParams.add((String)frmChequeDetail.get("sEndDate"));
		
		
		return alSearchParams;
	}//end of populateSearchCriteria(DynaActionForm frmSearchCheques)
	
	private LogManager getLogManagerObject() throws TTKException
	{
		LogManager logManager = null;
		try
		{
			if(logManager == null)
			{
				InitialContext ctx = new InitialContext();
				logManager = (LogManager) ctx.lookup("java:global/TTKServices/business.ejb3/LogManagerBean!com.ttk.business.empanelment.LogManager");
			}//end if(logManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "log");
		}//end of catch(Exception exp)
		return logManager;
	}//end getLogManagerObject()
	
	
	public ActionForward doSaveChequeInfoLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try{
			log.debug("Inside LogAction doSave");
			setLinks(request);
			int iUpdate=0;
			DynaActionForm frmChequeDetail=(DynaActionForm)form;
			ArrayList alLogList=null;
			//get the table data from session if exists
			TableData tableDataLog=null;
			if((request.getSession()).getAttribute("tableDataLog") == null)
			{
				tableDataLog = new TableData();
			}// end of if((request.getSession()).getAttribute("tableDataLog") == null)
			else
			{
				tableDataLog = (TableData)(request.getSession()).getAttribute("tableDataLog");
			}// end of else
			LogManager logManagerObj=this.getLogManagerObject();
			LogVO logVO = new LogVO();
			DynaActionForm frmSaveLog = (DynaActionForm)form;
			String strSubLink=TTKCommon.getActiveSubLink(request);
			String strLink=TTKCommon.getActiveLink(request);
			String strSwitchType="";
			

		   if((strLink.equals("Finance")) && strSubLink.equals("Cheque Information"))
			{
				logVO.setFlag("C");
				logVO.setClaimSeqID(Long.valueOf(String.valueOf(request.getSession().getAttribute("claimSeqId"))));
				logVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
				logVO.setLogDesc(frmChequeDetail.getString("logDesc"));
				iUpdate=logManagerObj.addClaimPreAuthLog(logVO,strLink);
			}// else if((strLink.equals(strClaims))||(strLink.equals(strCoding)&& strSubLink.equals(strClaimsCoding)))
		   frmChequeDetail.initialize(mapping);
			if(iUpdate >0)
			{
				//initialize the formbean after adding
				//frmSaveLog.initialize(mapping);
				request.setAttribute("updated","message.addedSuccessfully");
				tableDataLog = (TableData)(request.getSession()).getAttribute("tableDataLog");
				
				tableDataLog.setSortData("0001");
				tableDataLog.setSortOrder("DESC");
				tableDataLog.setSearchData(populateSearchCriteria(frmChequeDetail, request));
			
				
				 if((strLink.equals("Finance")&& strSubLink.equals("Cheque Information")))
			    	{
					alLogList=logManagerObj.getClaimPreAuthLogList(tableDataLog.getSearchData());
			    	}
				
				tableDataLog.setData(alLogList,"search");
				request.getSession().setAttribute("tableDataLog",tableDataLog);
			
			//StringBuffer strCaption= new StringBuffer();
		//	StringBuffer strCaption=buildCaption(request);
			frmSaveLog.set("caption",String.valueOf(""));
		//	frmHospitalLog.set("caption",String.valueOf(strCaption));
			//  
			
		   }
			return this.getForward("chequeInfoAlertLog", mapping, request);
		}
	
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"log"));
		}//end of catch(Exception exp)
	}// end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)
	
}//end of class AddMemberDetailAction


