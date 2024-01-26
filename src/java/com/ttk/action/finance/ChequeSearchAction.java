/**
 * @ (#) ChequeSearchAction.java June 12, 2006
 * Project       : TTK HealthCare Services
 * File          : ChequeSearchAction.java
 * Author        : Harsha Vardhan B N
 * Company       : Span Systems Corporation
 * Date Created  : June 12, 2006
 *
 * @author       :
 * Modified by   : Harsha Vardhan B N
 * Modified date : June 13, 2006
 * Reason        :
 */

package com.ttk.action.finance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimManager;
import com.ttk.business.finance.ChequeManager;
import com.ttk.business.reports.TTKReportManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.BankAddressVO;
import com.ttk.dto.finance.ChequeDetailVO;
import com.ttk.dto.finance.ChequeVO;

import formdef.plugin.util.FormUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
/**
 * This class is used for searching the  List of Cheques.
 */
public class ChequeSearchAction extends TTKAction {
	private static Logger log = Logger.getLogger( ChequeSearchAction.class );
	//   Modes.
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    //  Action mapping forwards.
    private static final String strChequeList="chequelist";
    private static final String strChequeDetail="chequedetail";
    //Exception Message Identifier
    private static final String strChequeSearch="ChequeSearch";
    private static final String strReportdisplay="reportdisplay";
    private static int MAX_LENGTH=65536;
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
    		log.debug("Inside ChequeSearchAction doDefault ");
    		TableData tableData = TTKCommon.getTableData(request);

            ((DynaActionForm)form).initialize(mapping);//reset the form data
            //create new table data object
    		tableData = new TableData();
    		//create the required grid table
    		tableData.createTableInfo("ChequeListTable",new ArrayList());
    		request.getSession().setAttribute("tableData",tableData);
    		return this.getForward(strChequeList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
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
    		setLinks(request);
    		log.debug("Inside ChequeSearchAction doSearch ");
    		TableData tableData = TTKCommon.getTableData(request);
    		ChequeManager chequeObject=this.getChequeManagerObject();
    		DynaActionForm frmSearchCheques =(DynaActionForm)form;
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
    		if(!strPageID.equals("") || !strSortID.equals(""))
    		{
    			if(!strPageID.equals(""))
    			{
    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward("chequelist"));
    			}///end of if(!strPageID.equals(""))
    			else
    			{
    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
    				tableData.modifySearchData("sort");//modify the search data
    			}//end of else
    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
    		else
    		{
    			// create the required grid table
    			tableData.createTableInfo("ChequeListTable",null);
    			//fetch the data from the data access layer and set the data to table object
    			tableData.setSearchData(this.populateSearchCriteria(frmSearchCheques,request));
    			tableData.modifySearchData("search");
    		}//end of else
    		ArrayList alCheque=chequeObject.getChequeList(tableData.getSearchData());
    		tableData.setData(alCheque, "search");
    		request.getSession().setAttribute("tableData",tableData);
    		//finally return to the grid screen
    		return this.getForward(strChequeList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
    	}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		setLinks(request);
    		log.debug("Inside ChequeSearchAction doForward ");
    		TableData tableData = TTKCommon.getTableData(request);
    		ChequeManager chequeObject=this.getChequeManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alCheque = chequeObject.getChequeList(tableData.getSearchData());
    		tableData.setData(alCheque, strForward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		//TTKCommon.documentViewer(request);
    		return this.getForward(strChequeList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
    	}//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		setLinks(request);
    		log.debug("Inside ChequqSearchAction doViewCheque");
    		TableData tableData = TTKCommon.getTableData(request);
    		ChequeManager chequeObject=this.getChequeManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
    		//fetch the data from the data access layer and set the data to table object
    		ArrayList alCheque = chequeObject.getChequeList(tableData.getSearchData());
    		tableData.setData(alCheque, strBackward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
    		//TTKCommon.documentViewer(request);
    		return this.getForward(strChequeList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
    	}//end of catch(Exception exp)
    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		log.debug("Inside ChequqSearchAction doViewCheque");
    		ChequeVO chequeVO=null;
    		TableData tableData = TTKCommon.getTableData(request);
    		DynaValidatorForm frmChequeDetail = (DynaValidatorForm)form;
    		//create a new Cheque object
    		chequeVO = new ChequeVO();
    		ChequeDetailVO chequeDetailVO = new ChequeDetailVO();
    		BankAddressVO bankAddressVO = new BankAddressVO ();
    		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
    		{
    			chequeVO = (ChequeVO)tableData.getRowInfo(Integer.parseInt((String)(frmChequeDetail).get("rownum")));
    			chequeDetailVO.setSeqID(chequeVO.getSeqID());
    			chequeDetailVO.setPaymentSeqId(chequeVO.getPaymentSeqId());
    			frmChequeDetail = (DynaValidatorForm)FormUtils.setFormValues("frmChequeDetail",chequeDetailVO,
    																		  this, mapping, request);
    			frmChequeDetail.set("bankAddressVO", (DynaActionForm)FormUtils.setFormValues("frmFinanceBankAddress",
    																		   bankAddressVO,this,mapping,request));
    			frmChequeDetail.set("caption","Cheque Details");
    			request.getSession().setAttribute("frmChequeDetail",frmChequeDetail);
    		}//end if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
    		else
    		{
    			TTKException expTTK = new TTKException();
    			expTTK.setMessage("error.Cheque.required");
    			throw expTTK;
    		}//end of else
    		return mapping.findForward(strChequeDetail);//this.getForward(strChequeDetail, mapping, request);
    	}
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
    	}//end of catch(Exception exp)
    }//end of doViewCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

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
    		TableData tableData = TTKCommon.getTableData(request);
    		ChequeManager chequeObject=this.getChequeManagerObject();
    		if(tableData.getSearchData().size()>1)
    		{
    			ArrayList alCheque=chequeObject.getChequeList(tableData.getSearchData());
    			tableData.setData(alCheque, "search");
    			//set the table data object to session
    			request.getSession().setAttribute("tableData",tableData);
    		}//end of if(callcentertableData.getSearchData().size()>1)
    		return this.getForward(strChequeList, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
    	}//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
	 * Returns the ArrayList which contains the populated search criteria elements
	 * @param frmSearchCheques DynaActionForm will contains the values of corresponding fields
	 * @param request HttpServletRequest object which contains the search parameter that is built
	 * @return alSearchParams ArrayList
	 */
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmSearchCheques,HttpServletRequest request) throws TTKException
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sChequeNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sFloatAccountNumber")));
		alSearchParams.add((String)frmSearchCheques.get("sStatus"));
		alSearchParams.add((String)frmSearchCheques.get("sStartDate"));
		alSearchParams.add((String)frmSearchCheques.get("sEndDate"));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sClaimSettleNumber")));
		alSearchParams.add((String)frmSearchCheques.get("sClaimType"));
		alSearchParams.add((String)frmSearchCheques.get("sInsuranceCompany"));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sCompanyCode")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sBatchNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sPolicyNumber")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sEnrollmentId")));
		alSearchParams.add((String)frmSearchCheques.get("sPaymentMethod"));
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		alSearchParams.add((String)frmSearchCheques.get("sQatarId"));
		
		return alSearchParams;
	}//end of populateSearchCriteria(DynaActionForm frmSearchCheques)

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
	
	
	
	
	
	public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
	 try{
	    		log.debug("Inside the doGenerateClimSummaryReport method of HospitalReportsAction");
	    		setLinks(request);
				 JasperReport jasperReport,emptyReport;
				 JasperPrint jasperPrint;
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
		    	String parameter=request.getParameter("parameter");	
				String  reportID	=	"'chequeInformationReport";		
	    		try
	    		{
	    			TTKReportManager tTKReportManager	=	this.getTTKReportManager();
	    			 ArrayList<String[]>  financeOutData = tTKReportManager.getChequeInformationReport("chequeInformationReport",request.getParameter("parameter"));
	    			 System.out.println("financeOutData size is::"+financeOutData.size());
	    			 if(financeOutData!=null&&financeOutData.size()>0){
	    				 HSSFWorkbook hssfWorkbook=createFinanceReport(financeOutData,reportID);
	    				 boas = new ByteArrayOutputStream();
	    				 hssfWorkbook.write(boas);
	    				 boas.close();
	    			 	 hssfWorkbook=null;
	    			 	 request.setAttribute("boas",boas);
	    			 	 request.setAttribute("reportID","RoutineRpt");
	    			 	 response.setContentLength(boas.toByteArray().length);
	    			 	 response.addHeader("Content-Disposition","attachment; filename=financeChequeInformationReport.xls");
	    			 	 log.info("Done.........");
	    			 }else{
	    				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
	    				 HashMap<String,String> hashMap = new HashMap<String,String>();
	    				 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
	    				 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
	    				 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
	    				 jExcelApiExporter.exportReport();
	    				 request.setAttribute("boas",boas);
	    				 response.setContentLength(boas.toByteArray().length);
	    				 request.setAttribute("reportID","RoutineRpt");
	    			 	 response.addHeader("Content-Disposition","attachment; filename=financeChequeInformationReport.xls");
	    			 }
	    			return (mapping.findForward(strReportdisplay));
	    		}
	    		catch(Exception exp)
	        	{
	    			return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
	        	}
	    	}
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strChequeSearch));
	    	}
  }
	
	
	  private TTKReportManager getTTKReportManager() throws TTKException
	     {
	         TTKReportManager tTKReportManager = null;
	         try
	         {
	             if(tTKReportManager == null)
	             {
	                 InitialContext ctx = new InitialContext();
	                 tTKReportManager = (TTKReportManager) ctx.lookup("java:global/TTKServices/business.ejb3/TTKReportManagerBean!com.ttk.business.reports.TTKReportManager");
	             }//end if
	         }//end of try
	         catch(Exception exp)
	         {
	             throw new TTKException(exp, "tTKReport");
	         }//end of catch
	         return tTKReportManager;
	     }//end getTTKReportManager() 	
	
	  public static HSSFWorkbook createFinanceReport(ArrayList<String[]> financeDataList, String reportId) throws IOException {
	 		HSSFWorkbook workbook	=	new HSSFWorkbook();
	 		HSSFCellStyle headerStyle = workbook.createCellStyle();		
	 		HSSFFont font = workbook.createFont();
	 		font.setFontName(HSSFFont.FONT_ARIAL);
	 		font.setFontHeightInPoints((short) 10);
	 		font.setBoldweight((short) 40);
	 		font.setColor(HSSFColor.WHITE.index);
	 		font.setFontHeightInPoints((short) 12);
	 		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	 		
	 		//Defing style for header
	 		headerStyle.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
	 		headerStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
	 		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 		headerStyle.setFont(font);
	 		
	 		//Defing style for Body
	 		HSSFCellStyle bodyDataStyle = workbook.createCellStyle();
	 		bodyDataStyle.setWrapText(true);
	 		bodyDataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 		
	 		//Header Data
	 		ArrayList<String> headerDataList=null;
	 		//if("chequeInformationReport".equals(reportId))
	 			headerDataList=getRoutineHeaderList();

	 		/*Find the number of sheet have to create logic Starts*/
	 		double dupSheetCount=financeDataList.size()/MAX_LENGTH;
	 		int dupDivisable=financeDataList.size()%MAX_LENGTH;
	 		int dupIntsheetCount=(int) dupSheetCount;
	 		if(dupDivisable!=0)
	 			dupIntsheetCount++;
	 		double actSheetCount=(financeDataList.size()+dupIntsheetCount)/MAX_LENGTH;
	 		int actDivisable=(financeDataList.size()+dupIntsheetCount)%MAX_LENGTH;
	 		int sheetCount=(int) actSheetCount;
	 		if(actDivisable!=0)
	 			sheetCount++;
	 		/*Find the number of sheet have to create logic End*/
	 		
	 		/*Creating sheets starts*/
	 		HSSFSheet errSheet =null;
	 		int count=1;
	 		while(sheetCount!=0){
	 			errSheet=workbook.createSheet("sheet"+count);
	 			sheetCount--;
	 			count++;
	 		}
	 		/*Creating sheets end*/
	 		log.info("Size is::"+financeDataList.size());
	 		HSSFRow row =null;
	 		int index=0;
	 		int sheetData=0;
	 		int cout=0;
	 		log.info("before starts");
	 		for(index=0;index<financeDataList.size()+cout;index++){
	 			System.out.println("index::"+index);
	 			/*Creating rows for sheet logic starts*/
	 			double decimalcurrentSheet=index/MAX_LENGTH;
	 			int curDivisable=index%MAX_LENGTH;
	 			int currentSheet=(int) decimalcurrentSheet;
	 			if(curDivisable!=0 &&index>MAX_LENGTH)
	 				sheetCount++;
	 			errSheet=workbook.getSheetAt(currentSheet);
	 			if(index%MAX_LENGTH!=0)
	 			row=errSheet.createRow(index-(currentSheet*MAX_LENGTH));
	 			else{
	 				/*Creating header logic starts*/
	 				row=errSheet.createRow(0);
	 				row=setHeader(errSheet,row,headerDataList,headerStyle);	
	 				cout++;
	 				continue;
	 				/*Creating header logic end*/
	 			}
	 			sheetData=index-cout;
	 			/*Creating rows for sheet logic ends*/
	 			
	 			/*Importing data to body cell logic starts*/
	 			String[] arr=financeDataList.get(sheetData);
	 				for(int i=0;i<arr.length;i++)
	 	 			{
	 	  	    			HSSFCell bodyCell = row.createCell((short)i);
	 	  	    			bodyCell.setCellStyle(bodyDataStyle);
	 	  	    			bodyCell.setCellValue(arr[i]);
	 	  	    			arr[i]=null;
	 	 			}   
	 				/*Importing data to body cell logic ends*/
	 		}
	 		log.info("No.Of records done..."+financeDataList.size());
	 		financeDataList=null;
	 		return workbook;
	 	}
	 private static ArrayList<String> getRoutineHeaderList() {
	 	ArrayList<String> headerDataList=new ArrayList<>();
	 	headerDataList.add(" Payment Trans Ref No.");
	 	headerDataList.add("Status");
	 	headerDataList.add("Cheque Date");
	 	headerDataList.add("Claim Settlement No.");
	 	headerDataList.add("Claim Type");
	 	headerDataList.add("Account No/IBAN No");
	 	headerDataList.add("Batch date");
	 	headerDataList.add("Amount paid");
	 	headerDataList.add("Paid Currency");
	 	return headerDataList;
	 }
	 private static HSSFRow setHeader(HSSFSheet errSheet,HSSFRow sheetRow, ArrayList<String> headerDataList,HSSFCellStyle headerStyle) {
	 	for (int inc=0;inc<headerDataList.size();inc++) {
	 		HSSFCell headerCell = sheetRow.createCell((short)inc);
	 		headerCell.setCellStyle(headerStyle);
	 		headerCell.setCellValue(headerDataList.get(inc));
	 		errSheet.autoSizeColumn((short)inc);
	 	}
	 	return sheetRow;
	 }
	 
		public ActionForward doViewChequeForAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ChequqSearchAction doViewCheque");
			ChequeVO chequeVO = null;
			TableData tableData = TTKCommon.getTableData(request);
			DynaValidatorForm frmChequeDetail = (DynaValidatorForm) form;
			
			// create a new Cheque object
			chequeVO = new ChequeVO();
			ChequeDetailVO chequeDetailVO = new ChequeDetailVO();
			if (!(TTKCommon.checkNull(request.getParameter("rownum"))
					.equals(""))) {
				chequeVO = (ChequeVO) tableData.getRowInfo(Integer
						.parseInt((String) (frmChequeDetail).get("rownum")));
				chequeDetailVO.setSeqID(chequeVO.getClaimSeqID());
				request.getSession().setAttribute("claimSeqId",chequeVO.getClaimSeqID());
			}
			frmChequeDetail.initialize(mapping);
			tableData = new TableData();
    		//create the required grid table
           tableData.createTableInfo("HospitalLogTable",new ArrayList());
           request.getSession().setAttribute("tableDataLog",tableData);
			request.getSession().setAttribute("isAccessable", true);
			return mapping.findForward("chequedetailforalert");
		} catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "chequedetailforalert"));
		}// end of catch(Exception exp)
	}
	
	public ActionForward doSearchForAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			log.debug("Inside the doSearch method of ClaimsAction");
			setLinks(request);
			ClaimManager claimManagerObject = this.getClaimManagerObject();
			// get the tbale data from session if exists
			TableData tableData = TTKCommon.getTableData(request);
			// PreAuthVO preAuthVO=new PreAuthVO;
			// clear the dynaform if visting from left links for the first time
			// else get the dynaform data from session
			if (TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")) {
				((DynaActionForm) form).initialize(mapping);// reset the form
															// data
			}// end of
				// if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			String strPageID = TTKCommon.checkNull(request
					.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request
					.getParameter("sortId"));

			// if the page number or sort id is clicked
			if (!strPageID.equals("") || !strSortID.equals("")) {
				if (!strPageID.equals("")) {
					tableData.setCurrentPage(Integer.parseInt(TTKCommon
							.checkNull(request.getParameter("pageId"))));
					return mapping.findForward("alertchequesearch");
				}// /end of if(!strPageID.equals(""))
				else {
					tableData.setSortData(TTKCommon.checkNull(request
							.getParameter("sortId")));
					tableData.modifySearchData("sort");// modify the search data
				}// end of else
			}// end of if(!strPageID.equals("") || !strSortID.equals(""))
			else {
				// create the required grid table
				tableData.createTableInfo("AlertClaimsTable", null);
				tableData.setSearchData(this.populateSearchCriteriaForAlert(
						(DynaActionForm) form, request));
				tableData.modifySearchData("search");
			}// end of else

			ArrayList alClaimsList = claimManagerObject.getClaimListForAlert(tableData
					.getSearchData());
			tableData.setData(alClaimsList, "search");
			// set the table data object to session
			request.getSession().setAttribute("tableData", tableData);
			// finally return to the grid screen

			return this.getForward("alertchequesearch", mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, "alertchequesearch"));
		}// end of catch(Exception exp)
	}// end of doSearch

	private ClaimManager getClaimManagerObject() throws TTKException
    {
    	ClaimManager claimManager = null;
    	try
    	{
    		if(claimManager == null)
    		{
    			InitialContext ctx = new InitialContext();
    			claimManager = (ClaimManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimManagerBean!com.ttk.business.claims.ClaimManager");
    		}//end if
    	}//end of try
    	catch(Exception exp)
    	{
    		throw new TTKException(exp, "ChequeSearch");
    	}//end of catch
    	return claimManager;
    }
	
	private ArrayList<Object> populateSearchCriteriaForAlert(DynaActionForm frmSearchCheques,HttpServletRequest request) throws TTKException
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		
		
		
		
    	alSearchParams.add("");//0
    	alSearchParams.add("");//1
    	alSearchParams.add("");//2
    	alSearchParams.add(frmSearchCheques.getString("claimNO"));//3
    	alSearchParams.add("");//4
    	alSearchParams.add("");//5
    	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmSearchCheques.get("sClaimSettleNumber")));//6
    	alSearchParams.add("");//7
    	alSearchParams.add("");//8
		//alSearchParams.add((String)frmClaimsList.getString("sTtkBranch"));
		alSearchParams.add("");//9
		alSearchParams.add(null);//10
		alSearchParams.add(null);//11
		alSearchParams.add(null);//12
		alSearchParams.add(null);//13
		alSearchParams.add(null);//14
		alSearchParams.add(null);//15
		alSearchParams.add(TTKCommon.getUserSeqId(request));//16
		alSearchParams.add(null);//17
		alSearchParams.add(null);//18
		alSearchParams.add(null);//19
		alSearchParams.add(null);//20
		alSearchParams.add(null);//21
		alSearchParams.add(null);//22
		alSearchParams.add(null);//23
		alSearchParams.add(null);//24
		alSearchParams.add(null);//25
		alSearchParams.add(null);//26
		alSearchParams.add(null);//27
		alSearchParams.add(null);//28	
		alSearchParams.add(null);//29
		alSearchParams.add(null);//30
		alSearchParams.add(null);//31
		
		alSearchParams.add(null);
		
		alSearchParams.add(null);
		
		alSearchParams.add(null);//34	
		alSearchParams.add(null);//35	
		
		return alSearchParams;
	}



	
}//end of class ChequeSearchAction