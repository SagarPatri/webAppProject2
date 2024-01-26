/**
* @ (#) BenefitListAction.java
* Project       : TTK HealthCare Services
* File          : BenefitListAction.java
* Author        : Balaji C R B
* Company       : Span Systems Corporation
* Date Created  : July 02,2008

* @author       : Balaji C R B
* Modified by   :
* Modified date :
* Reason :
*/

package com.ttk.action.empanelment;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.business.administration.TariffManager;
import com.ttk.business.claims.ClaimBatchManager;
import com.ttk.business.claims.ClaimBenefitManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.ProductVO;
import com.ttk.dto.claims.BatchVO;
import com.ttk.dto.claims.ClaimBenefitVO;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching of Claims
 */

public class TariffErrorLogListAction extends TTKAction
{
	private static Logger log = Logger.getLogger(TariffErrorLogListAction.class);

	//declrations of modes
	private static final String strForward="Forward";
	private static final String strBackward="Backward";

	//Action mapping forwards.
	private static final  String strErrorLogList="errorLoglist";

	//Exception Message Identifier
    private static final String strClaimBatchSearchError="claimbatchsearch";
    private static final String strClaimBatchDetails="ClaimsBatchDetails";
  
    
	private static final String strRownum="rownum";
	private static final String strViewErrorLog = "viewErrorLog";
	private static final String strHospTariffError = "hospitalTariff";


   

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
    		log.info("Inside the doSearch method of TariffLogListAction");
    		setLinks(request);
    		HttpSession session=request.getSession();
    		TariffManager tariffObject=this.getTariffManager();
    		//get the tbale data from session if exists
    		TableData tableData =session.getAttribute("tableData")==null?new TableData():(TableData)session.getAttribute("tableData");
			//clear the dynaform if visting from left links for the first time
			//else get the dynaform data from session
    		
    		
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}// end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return mapping.findForward(strErrorLogList);
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else
			{
				//create the required grid table
				tableData.createTableInfo("TariffUploadErrorLogList",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
                tableData.modifySearchData("search");
                tableData.getSearchData().set(tableData.getSearchData().size()-3, "DESC");
			}//end of else
			ArrayList alClaimsBatchList= tariffObject.getTariffUploadErrorLogList(tableData.getSearchData());
			tableData.setData(alClaimsBatchList, "search");
			//set the table data object to session
			session.setAttribute("tableData",tableData);
			//finally return to the grid screen
		 return mapping.findForward(strErrorLogList);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strErrorLogList));
		}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  

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
    		log.debug("Inside the doBackward method of ClaimBatchAction");
    		setLinks(request);
    		TariffManager tariffObject=this.getTariffManager();
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strBackward);//modify the search data
			ArrayList alClaimsBatchList= tariffObject.getTariffUploadErrorLogList(tableData.getSearchData());
			tableData.setData(alClaimsBatchList, strBackward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return mapping.findForward(strErrorLogList);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchSearchError));
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
    		log.debug("Inside the doForward method of ClaimsAction");
    		setLinks(request);
    		TariffManager tariffObject=this.getTariffManager();
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strForward);//modify the search data
			ArrayList alClaimsBatchList= tariffObject.getTariffUploadErrorLogList(tableData.getSearchData());
			tableData.setData(alClaimsBatchList, strForward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return this.getForward(strErrorLogList, mapping, request);   //finally return to the grid screen
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchSearchError));
		}//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
	 * This method is used to print to Error Log  edit selected record.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doViewErrorLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
														  HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doViewErrorLog method of ClaimErrorLogListAction");
			//get the session bean from the bean pool for each excecuting thread
			TableData tableData = TTKCommon.getTableData(request);
			DynaActionForm frmErrorLogList = (DynaActionForm)form;
			//create a new Product object
			ClaimUploadErrorLogVO claimUploadErrorLogVO = new ClaimUploadErrorLogVO();
			if(!(TTKCommon.checkNull(request.getParameter(strRownum)).equals("")))
			{
				claimUploadErrorLogVO = (ClaimUploadErrorLogVO)tableData.getRowInfo(Integer.parseInt((String)request.getParameter(strRownum)));
				//claimUploadErrorLog
				JasperReport mainJasperReport,emptyReport;
	    		TTKReportDataSource mainTtkReportDataSource = null;
	    		JasperPrint mainJasperPrint = null;
	    		
	    		String strReportId="tariffUploadErrorLog";
	    		String parameter = claimUploadErrorLogVO.getReferenceNo();
	    		String tariffType = claimUploadErrorLogVO.getTariffType();

	    		 
	    		String jrxmlfile=null;
	    		if("PHARMA".equals(tariffType)){
	    		jrxmlfile="reports/empanelment/TariffPharmaUploadErrorLog.jrxml";
	    		}else if("ACT".equals(tariffType)){
	    		jrxmlfile="reports/empanelment/TariffActivityUploadErrorLog.jrxml";
	    		}
	    		try
	    		{
	    			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    			HashMap<String,String> hashMap = new HashMap<String,String>();
	    			ByteArrayOutputStream boas=new ByteArrayOutputStream();
	    			mainTtkReportDataSource = new TTKReportDataSource(strReportId, parameter);
	    			ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
	    			 
	    			mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
	    			
	    			if (main_report_RS != null && main_report_RS.next()) {
	    				main_report_RS.beforeFirst();
	    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);
	    				  				
	    			} else {
	    				
	    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
	    			}
	    			
	    			if("EXCEL".equals(request.getParameter("reportType")))
		    		{
	    				
	    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				jExcelApiExporter.exportReport();
		    	}
	    			
	    			request.setAttribute("boas",boas);
	    			
	    			
	    		}
	    		catch(Exception exp)
	        	{
	    			return this.processExceptions(request, mapping, new TTKException(exp, strClaimBatchSearchError));
	        	}
	    		return (mapping.findForward(strViewErrorLog));
			
	    	}
								
			else
			{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.product.required");
				throw expTTK;
			}//end of else
			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchSearchError));
		}//end of catch(Exception exp)
	}//end of doViewProducts(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				//HttpServletResponse response)
    
  
	
	
	
    /**
     * this method will add search criteria fields and values to the arraylist and will return it
     * @param frmClaimsList formbean which contains the search fields
     * @param request HttpServletRequest
     * @return ArrayList contains search parameters
     */
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmErrorLogList,HttpServletRequest request)
    {
    	//build the column names along with their values to be searched
    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
    	alSearchParams.add(frmErrorLogList.getString("iBatchRefNO"));
    	alSearchParams.add(frmErrorLogList.getString("sFileName"));
    	alSearchParams.add(TTKCommon.checkNull(frmErrorLogList.getString("formDate")));
    	alSearchParams.add(TTKCommon.checkNull(frmErrorLogList.getString("toDate")));
		String hospitalseqid  = (String)request.getSession().getAttribute("hospSeqIdforTariff");
		//System.out.println("hospitalseqid-----"+hospitalseqid);
		if(hospitalseqid !=null){
    	alSearchParams.add(hospitalseqid);
		}else{
	    alSearchParams.add("");
		}
		if((request.getSession().getAttribute("hospitalgroupseqid")!=null) && (!request.getSession().getAttribute("hospitalgroupseqid").equals(""))){
			alSearchParams.add(request.getSession().getAttribute("hospitalgroupseqid"));
			alSearchParams.add("GROP");
		}else{
			alSearchParams.add("");
			alSearchParams.add("HOSP");
		}
		
    	return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmClaimBatchList,HttpServletRequest request)

    /**
	 * Returns the TariffManager session object for invoking methods on it.
	 * 
	 * @return TariffManager session object which can be used for method
	 *         invokation
	 * @exception throws TTKException
	 */
	private TariffManager getTariffManager() throws TTKException {
		TariffManager hospTariff = null;
		try {
			if (hospTariff == null) {
				InitialContext ctx = new InitialContext();
				hospTariff = (TariffManager) ctx.lookup("java:global/TTKServices/business.ejb3/TariffManagerBean!com.ttk.business.administration.TariffManager");

			}// end if
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, strHospTariffError);
		}// end of catch
		return hospTariff;
	}// end getTariffManager()
}//end of BenefitListAction