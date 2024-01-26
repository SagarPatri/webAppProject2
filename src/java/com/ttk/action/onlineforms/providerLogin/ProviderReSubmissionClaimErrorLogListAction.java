package com.ttk.action.onlineforms.providerLogin;

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

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimBatchManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.claims.ClaimUploadErrorLogVO;

public class ProviderReSubmissionClaimErrorLogListAction extends TTKAction {
	private static Logger log = Logger.getLogger(ProviderReSubmissionClaimErrorLogListAction.class);

	//declrations of modes
	private static final String strForward="Forward";
	private static final String strBackward="Backward";

	//Action mapping forwards.
	private static final  String strErrorLogList="reSubmissionErrorLoglist";

	//Exception Message Identifier
    private static final String strClaimBatchSearchError="claimbatchsearch";
    private static final String strClaimBatchDetails="ClaimsBatchDetails";
  
    
	private static final String strRownum="rownum";
	private static final String strViewErrorLog = "viewErrorLog";

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
    		log.debug("Inside the doDefault method of ProviderReSubmissionClaimErrorLogListAction");
    		setLinks(request);    		
    		
    		HttpSession session=request.getSession();
    		DynaActionForm frmErrorLogList = (DynaActionForm)form;
    		//get the tbale data from session if exists
			TableData tableData =(session.getAttribute("tableData")==null)?new TableData():(TableData)session.getAttribute("tableData");
			//clear the dynaform if visiting from left links for the first time
			if("Y".equals(request.getParameter("Entry"))){
				frmErrorLogList.initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			
			tableData.createTableInfo("ClaimUploadErrorLogList",new ArrayList());
			session.setAttribute("tableData",tableData);
			frmErrorLogList.initialize(mapping);//reset the form data
			return this.getForward(strErrorLogList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimBatchSearchError));
		}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		log.debug("Inside the doSearch method of ProviderReSubmissionClaimViewLogListAction");
    		setLinks(request);
    		HttpSession session=request.getSession();
    		ClaimBatchManager claimBatchManagerObject=this.getClaimBatchManagerObject();
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
				tableData.createTableInfo("ClaimUploadErrorLogList",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
                tableData.modifySearchData("search");
                tableData.getSearchData().set(tableData.getSearchData().size()-3, "DESC");
			}//end of else
			ArrayList alClaimsBatchList= claimBatchManagerObject.getClaimUploadErrorLogList(tableData.getSearchData());
			tableData.setData(alClaimsBatchList, "search");
			//set the table data object to session
			session.setAttribute("tableData",tableData);
			//finally return to the grid screen
			return this.getForward(strErrorLogList, mapping, request);
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
     * This method is used to navigate to detail screen to edit selected record.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     *//*
    public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    												HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doView method of ClaimBatchAction");
    		setLinks(request);
    		HttpSession session=request.getSession();
    		//get the tbale data from session if exists
			TableData tableData =(TableData)session.getAttribute("tableData");//TTKCommon.getTableData(request);
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				BatchVO batchVO=(BatchVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
				//session.setAttribute("claimBatchVO", batchVO);
				this.addToWebBoard(batchVO, request);
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			return mapping.findForward(strClaimBatchDetails);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strClaimsBatchlist));
		}//end of catch(Exception exp)
    }//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  


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
    		ClaimBatchManager claimBatchManagerObject=this.getClaimBatchManagerObject();
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strBackward);//modify the search data
			ArrayList alClaimsBatchList = claimBatchManagerObject.getClaimUploadErrorLogList(tableData.getSearchData());
			tableData.setData(alClaimsBatchList, strBackward);//set the table data
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
    		ClaimBatchManager claimBenefitManagerObject=this.getClaimBatchManagerObject();
    		//get the tbale data from session if exists
			TableData tableData =TTKCommon.getTableData(request);
    		tableData.modifySearchData(strForward);//modify the search data
			ArrayList alClaimsBatchList = claimBenefitManagerObject.getClaimUploadErrorLogList(tableData.getSearchData());
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
	    		
	    		String strReportId="claimUploadErrorLog";
	    		String parameter = claimUploadErrorLogVO.getReferenceNo();
	    		String sourceType = claimUploadErrorLogVO.getSourceTypeId(); 
	    		String jrxmlfile=null;
	    		if(sourceType!=null){
	    			if(sourceType.equals("PBCL")){
						jrxmlfile = "reports/claims/PBMClaimUploadErrorLog.jrxml";
					}
	    			else{
						jrxmlfile = "reports/claims/claimReUploadErrorLog.jrxml";
					}
	    		}
	    		else{
					//jrxmlfile = "reports/claims/ClaimUploadErrorLog.jrxml";
	    			jrxmlfile="reports/claims/claimReUploadErrorLog.jrxml";
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
    	alSearchParams.add((""+TTKCommon.checkNull(request.getSession().getAttribute("hospSeqID"))).trim());
    	alSearchParams.add("DTR");
    	return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmClaimBatchList,HttpServletRequest request)

    /**
	 * Returns the ClaimBatchManager session object for invoking methods on it.
	 * @return ClaimBatchManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private ClaimBatchManager getClaimBatchManagerObject() throws TTKException
	{
		ClaimBatchManager claimBatchManager = null;
		try
		{
			if(claimBatchManager == null)
			{
				InitialContext ctx = new InitialContext();
				claimBatchManager = (ClaimBatchManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimBatchManagerBean!com.ttk.business.claims.ClaimBatchManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strClaimBatchDetails);
		}//end of catch
		return claimBatchManager;
	}//end getClaimBatchManagerObject()
}
