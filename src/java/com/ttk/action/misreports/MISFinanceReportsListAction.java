/**
  * @ (#) MISFinanceReportsListAction.java
  * Project      :Vidal Health TPA
  * File         : MISFinanceReportsListAction.java
  * Author       : Balaji C R B
  * Company      :Vidal Health TPA Services
  * Date Created : 5th August,2008
  *
  * @author      : Balaji C R B
  * Modified by  : Balakrishna Erram
  * Modified date: April 15, 2009
  * Company      : Span Infotech Pvt.Ltd.
  * Reason       : Code Review
  */

package com.ttk.action.misreports;

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
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.business.finance.FloatAccountManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.ReportVO;

/**
 * This action class is used to display the Corporate Reports.
 */
public class MISFinanceReportsListAction extends TTKAction {

	private static final Logger log = Logger.getLogger( MISFinanceReportsListAction.class );

	//String for forwarding
    private static final String strAllFinanceReportsList="allfinancereportslist";
    private static final String strDOBOClaimsDetailReports="doboclaimsdetailreport";
    private static final String strFGPendingReports="fgpendingreport";
    private static final String strOrientalPaymentAdviceReport="orientalpaymentadvicereport";
    private static final String strCitibankClaimsDetailRpt="citibankclaimsdetailrpt";
    private static final String strUniSampoPendRpt="unisampopendrpt";

    private static final String strDetailReports="detailedReports";
    private static final String strBankAccReports="bankAccReports";
    
	private static final String strExchangeRate="exchangeRate";
	//  Exception Message Identifier
    private static final String strAddressExp="address";   
	private static final String strTableData="tableData";
	private static final String strBackward="Backward";
	private static final String strForward="Forward";

    /**
	  * This method is used to initialize the screen.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  *
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
    public ActionForward doDefault(ActionMapping mapping, ActionForm form,HttpServletRequest request,
    		                       HttpServletResponse response) throws TTKException {
    	try {
    		log.debug("Inside the doDefault method of MISFinanceReportsListAction");
    		setLinks(request);
    		if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		{
    			((DynaActionForm) form).initialize(mapping);// reset the form data
    		}//end of if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
    		return this.getForward(strAllFinanceReportsList, mapping, request);
    	}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
		}// end of catch(Exception exp)
	}// end of doDefault

    /**
	  * This method is used to initialize the report parameters screen.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
    public ActionForward doViewClaimsDetail(ActionMapping mapping, ActionForm form,HttpServletRequest request,
    		                         HttpServletResponse response) throws TTKException {
		try {
			log.debug("Inside the doViewClaimsDetail method of MISFinanceReportsListAction");
			setLinks(request);
			((DynaActionForm) form).initialize(mapping);// reset the form data
			return this.getForward(strDOBOClaimsDetailReports, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
		}// end of catch(Exception exp)
	}// end of doViewClaimsDetail()

    /**
	  * This method is used to initialize the report parameters screen.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
   public ActionForward doViewFGPendingReport(ActionMapping mapping, ActionForm form,HttpServletRequest request,
   		                         HttpServletResponse response) throws TTKException {
		try {
			log.debug("Inside the doViewFGPendingReport method of MISFinanceReportsListAction");
			setLinks(request);
			((DynaActionForm) form).initialize(mapping);// reset the form data
			return this.getForward(strFGPendingReports, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
		}// end of catch(Exception exp)
	}// end of doViewFGPendingReport()
   /**
	  * This method is used to initialize the report parameters screen.
	  * Finally it forwards to the appropriate view based on the specified forward mappings
	  * @param mapping The ActionMapping used to select this instance
	  * @param form The optional ActionForm bean for this request (if any)
	  * @param request The HTTP request we are processing
	  * @param response The HTTP response we are creating
	  * @return ActionForward Where the control will be forwarded, after this request is processed
	  * @throws Exception if any error occurs
	  */
 public ActionForward doViewOrientalPaymentAdvice(ActionMapping mapping, ActionForm form,HttpServletRequest request,
 		                         HttpServletResponse response) throws TTKException {
		try {
			log.debug("Inside the doViewOrientalPaymentAdvice method of MISFinanceReportsListAction");
			setLinks(request);
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
    			((DynaActionForm) form).initialize(mapping);// reset the form data
			}//end of if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			return this.getForward(strOrientalPaymentAdviceReport, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
		}// end of catch(Exception exp)
	}// end of doViewOrientalPaymentAdvice()
 
 /**
  * This method is used to initialize the Citibank Claims Detail report parameters screen.
  * Finally it forwards to the appropriate view based on the specified forward mappings
  * @param mapping The ActionMapping used to select this instance
  * @param form The optional ActionForm bean for this request (if any)
  * @param request The HTTP request we are processing
  * @param response The HTTP response we are creating
  * @return ActionForward Where the control will be forwarded, after this request is processed
  * @throws TTKException if any error occurs
  */
public ActionForward doCitibankClaimsDetailRpt(ActionMapping mapping, ActionForm form,HttpServletRequest request,
		                         HttpServletResponse response) throws TTKException {
	try {
		log.debug("Inside the doCitibankClaimsDetailRpt method of MISFinanceReportsListAction");
		setLinks(request);
		if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		{
			((DynaActionForm) form).initialize(mapping);// reset the form data
		}//end of if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		return this.getForward(strCitibankClaimsDetailRpt, mapping, request);
	}// end of try
	catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
	}// end of catch(TTKException expTTK)
	catch (Exception exp) {
		return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
	}// end of catch(Exception exp)
}// end of doCitibankClaimsDetailRpt()

/**
 * This method is used to initialize the Citibank Claims Detail report parameters screen.
 * Finally it forwards to the appropriate view based on the specified forward mappings
 * @param mapping The ActionMapping used to select this instance
 * @param form The optional ActionForm bean for this request (if any)
 * @param request The HTTP request we are processing
 * @param response The HTTP response we are creating
 * @return ActionForward Where the control will be forwarded, after this request is processed
 * @throws TTKException if any error occurs
 */
public ActionForward doUniversalSampo(ActionMapping mapping, ActionForm form,HttpServletRequest request,
		                         HttpServletResponse response) throws TTKException {
	try {
		log.info("Inside the doUniversalSampo method of MISFinanceReportsListAction");
		setLinks(request);
		if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		{
			((DynaActionForm) form).initialize(mapping);// reset the form data
		}//end of if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		return this.getForward(strUniSampoPendRpt, mapping, request);
	}// end of try
	catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
	}// end of catch(TTKException expTTK)
	catch (Exception exp) {
		return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
	}// end of catch(Exception exp)
}// end of doUniversalSampo()



/**
 * This method is used to initialize the report parameters screen.
 * Finally it forwards to the appropriate view based on the specified forward mappings
 * @param mapping The ActionMapping used to select this instance
 * @param form The optional ActionForm bean for this request (if any)
 * @param request The HTTP request we are processing
 * @param response The HTTP response we are creating
 * @return ActionForward Where the control will be forwarded, after this request is processed
 * @throws Exception if any error occurs
 */
public ActionForward doDetailReport(ActionMapping mapping, ActionForm form,HttpServletRequest request,
		                         HttpServletResponse response) throws TTKException {
	try {
		log.debug("Inside the doDetailReport method of MISFinanceReportsListAction");
		setLinks(request);
		((DynaActionForm) form).initialize(mapping);// reset the form data
		request.setAttribute("logicType", request.getParameter("logicType"));
		request.getSession().setAttribute("repType", request.getParameter("repType"));
		if (request.getParameter("logicType").equalsIgnoreCase("exchangeRate")){
			
			String strTable = "";
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		
			TableData tableData =TTKCommon.getTableData(request);
			//create new table data object
			tableData = new TableData();
			//create the required grid table
			strTable = "ExchangeRateTable";
			tableData.createTableInfo(strTable,new ArrayList());
			request.getSession().setAttribute(strTableData,tableData);
			

		}
		
		return this.getForward(strDetailReports, mapping, request);
	}// end of try
	catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
	}// end of catch(TTKException expTTK)
	catch (Exception exp) {
		return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
	}// end of catch(Exception exp)
}// end of doDetailReport()
 


/**
 * This method is used to initialize the report parameters screen.
 * Finally it forwards to the appropriate view based on the specified forward mappings
 * @param mapping The ActionMapping used to select this instance
 * @param form The optional ActionForm bean for this request (if any)
 * @param request The HTTP request we are processing
 * @param response The HTTP response we are creating
 * @return ActionForward Where the control will be forwarded, after this request is processed
 * @throws Exception if any error occurs
 */
public ActionForward doBankAccountReport(ActionMapping mapping, ActionForm form,HttpServletRequest request,
		                         HttpServletResponse response) throws TTKException {
	try {
		log.debug("Inside the doBankAccountReport method of MISFinanceReportsListAction");
		setLinks(request);
		DynaActionForm frmMISFinanceReports	=	(DynaActionForm)form;
		frmMISFinanceReports.initialize(mapping);
		frmMISFinanceReports.set("switchType", "Hospital");
		request.setAttribute("logicType", request.getParameter("logicType"));
		return this.getForward(strBankAccReports, mapping, request);
	}// end of try
	catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
	}// end of catch(TTKException expTTK)
	catch (Exception exp) {
		return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
	}// end of catch(Exception exp)
}// end of doDetailReport()
 



/*
 * Do Switch btn Policy and Hospitals
 */

public ActionForward doSwitchTo(ActionMapping mapping, ActionForm form,HttpServletRequest request,
		        HttpServletResponse response) throws TTKException {
	try {
		log.debug("Inside the doSwitchTo method of MISFinanceReportsListAction");
		setLinks(request);
		DynaActionForm frmMISFinanceReports	=	(DynaActionForm)form;
		String switchType	=	(String) frmMISFinanceReports.get("switchType");
		frmMISFinanceReports.initialize(mapping);
		frmMISFinanceReports.set("switchType", switchType);
		return this.getForward(strBankAccReports, mapping, request);
	}// end of try
	catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
	}// end of catch(TTKException expTTK)
	catch (Exception exp) {
	return this.processExceptions(request, mapping, new TTKException(exp, "reportdetail"));
	}// end of catch(Exception exp)
}// end of doDetailReport()


public ActionForward doSearchExchangeRate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		  HttpServletResponse response) throws Exception{
		try{
		setLinks(request);
		log.info("Inside Exchange Rates doSearch");
		TableData  tableData =TTKCommon.getTableData(request);
		String Cigna_YN = "";
		DynaActionForm frmMISFinanceReports =(DynaActionForm)form;

		FloatAccountManager floatAccountManagerObject=this.getFloatAccountManagerObject();
		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		request.setAttribute("logicType","exchangeRate");
		request.getSession().setAttribute("repType", "exchange Rate");

		
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
		if(strPageID.equals(""))
		{
			tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
			tableData.modifySearchData("sort");//modify the search data
		}///end of if(!strPageID.equals(""))
		else
		{
			tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			return (mapping.findForward(strExchangeRate));
		}//end of else
		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		else
		{
		//create the required grid table
		tableData.createTableInfo("ExchangeRateTable",null);
		//fetch the data from the data access layer and set the data to table object
		tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
		tableData.modifySearchData("search");
		}//end of else
		ArrayList alBatchClaim=floatAccountManagerObject.getExchangeRates(tableData.getSearchData());
		//added for Mail-SMS Template for Cigna
		/*ChequeVO chequeVO = new ChequeVO();
		for(int i=0;i<alBatchClaim.size();i++)
		{
		chequeVO = (ChequeVO)alBatchClaim.get(i);
		}
		Cigna_YN = chequeVO.getCigna_YN();
		logicType
		*/tableData.setData(alBatchClaim, "search");
		//frmMISFinanceReports.set("logicType","exchangeRate");
		


		request.getSession().setAttribute("frmMISFinanceReports",frmMISFinanceReports);

		//request.getSession().setAttribute("Cigna_YN",Cigna_YN);
		request.getSession().setAttribute("tableData",tableData);
		//finally return to the grid screen
		return this.getForward(strExchangeRate, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
		}//end of catch(Exception exp)
		}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

		private ArrayList<Object> populateSearchCriteria(DynaActionForm frmTransSearch,HttpServletRequest request) 
		{
			//build the column names along with their values to be searched
		    ArrayList<Object> alSearchParams = new ArrayList<Object>();
		    alSearchParams.add(frmTransSearch.get("sExchangeToDate"));
		    alSearchParams.add(frmTransSearch.get("sCountry"));
		    return alSearchParams;
		}//end of populateSearchCriteria(DynaActionForm frmTransSearch,HttpServletRequest request)
		
		public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				  HttpServletResponse response) throws Exception{
					try{
					log.debug("Inside the doBackward method of AdminPoliciesAction");
					setLinks(request);
					//get the session bean from the bean pool for each excecuting threadl
					FloatAccountManager floatAccountManagerObject=this.getFloatAccountManagerObject();
					TableData tableData = TTKCommon.getTableData(request);
					//modify the search data
					tableData.modifySearchData(strBackward);
					ArrayList alBatchClaim=floatAccountManagerObject.getExchangeRates(tableData.getSearchData());
					tableData.setData(alBatchClaim, strBackward);
					//set the table data object to session
					request.getSession().setAttribute("tableData",tableData);
					request.setAttribute("logicType","exchangeRate");

					
					return (mapping.findForward(strExchangeRate));
					}//end of try
					catch(TTKException expTTK)
					{
					return this.processExceptions(request, mapping, expTTK);
					}//end of catch(TTKException expTTK)
					catch(Exception exp)
					{
					return this.processExceptions(request, mapping, new TTKException(exp,strAddressExp));
					}//end of catch(Exception exp)
					}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
					//HttpServletResponse response)

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
				log.info("Inside the doForward method of AdminPoliciesAction");
				setLinks(request);
				//get the session bean from the bean pool for each excecuting threadl
				FloatAccountManager floatAccountManagerObject=this.getFloatAccountManagerObject();
				TableData tableData = TTKCommon.getTableData(request);
				//modify the search data
				tableData.modifySearchData(strForward);
				ArrayList alBatchClaim=floatAccountManagerObject.getExchangeRates(tableData.getSearchData());
				//set the table data object to session
				tableData.setData(alBatchClaim, strForward);
				//finally return to the grid screen
				request.getSession().setAttribute("tableData",tableData);
				request.setAttribute("logicType","exchangeRate");

				return (mapping.findForward(strExchangeRate));
				}//end of try
				catch(TTKException expTTK)
				{
				return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
				return this.processExceptions(request, mapping, new TTKException(exp,strAddressExp));
				}//end of catch(Exception exp)
				}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)								

/**
	 * Returns the FloatAccountManager session object for invoking methods on it.
	 * @return FloatAccountManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private FloatAccountManager getFloatAccountManagerObject() throws TTKException
	{
		FloatAccountManager floatAccountManager = null;
		try
		{
			if(floatAccountManager == null)
			{
				InitialContext ctx = new InitialContext();
				floatAccountManager = (FloatAccountManager) ctx.lookup("java:global/TTKServices/business.ejb3/FloatAccountManagerBean!com.ttk.business.finance.FloatAccountManager");
			}//end if(hospManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strAddressExp);
		}//end of catch
		return floatAccountManager;
	}//end getFloatAccountList()

}//end of MISFinanceReportsListAction
