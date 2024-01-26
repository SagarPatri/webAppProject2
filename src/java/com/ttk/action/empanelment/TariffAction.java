/**
 * @ (#) TariffAction.java Oct 21, 2005
 * Project       : TTK HealthCare Services
 * File          : TariffAction.java
 * Author        : Srikanth H M
 * Company       : Span Systems Corporation
 * Date Created  : Oct 21, 2005
 *
 * @author       : Srikanth H M
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.empanelment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.empanelment.TariffXML.PackageDetails;
import com.ttk.action.empanelment.TariffXML.PharmacyPackageDetails;
import com.ttk.action.empanelment.TariffXML.PharmacyTariffXMLTag;
import com.ttk.action.empanelment.TariffXML.Pricerefdetails;
import com.ttk.action.empanelment.TariffXML.TariffXMLTag;
import com.ttk.action.empanelment.TariffXML.Tariffdetails;
import com.ttk.action.table.TableData;
import com.ttk.business.administration.TariffManager;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.TariffUploadVO;
import com.ttk.dto.common.SearchCriteria;
//import com.ttk.dto.empanelment.ActivityDetailsVO;
import com.ttk.dto.empanelment.InsuranceVO;
import com.ttk.dto.preauth.ActivityDetailsVO;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching the hospital tariffs.
 */

public class TariffAction extends TTKAction {
	private static Logger log = Logger.getLogger(TariffAction.class);

	// Action mapping forwards.
	private static final String strHospitalTariffList = "tarifflist";
	private static final String strRevisionHistoryList = "revisionhistory";

    //  Modes.
	private static final String strBackward = "Backward";
	private static final String strForward = "Forward";

	// Exception Message Identifier
	private static final String strHospTariffError = "hospitalTariff";
    private static final String strTariffUploaded	=	"tariffUploads";

	// Tariff for Adminidtration
	private static final String	strUploadTariff		=	"uploadtariff";
	private static final String	strSelectCorporates	=	"selctCorporates";
	private static final String	strTariffUploadedEmpanelment	=	"tariffUploadsEmpanelment";

	// Tariff For Empanelment
	private static final String	strUploadTariffEmpanelment		=	"uploadtariffEmpanelment";
	private static final String	strHospitalTariffEmpanelmentList		=	"tarifflistEmpanelment";
	private static final String	strSearchTariffEmpanelment		=	"searchTariffEmpanelment";
	private static final String strSearchPharmacyTariffEmpanelment = "searchPharmacyTariffEmpanelment";

	private static final String strActivityCodeList="activityCodeList";
	private static final String strEditTariffItemEmpanelment	=	"editTariffEmpanelment";
	private static final String strActivitydetails	=	"activitydetails";
	private static final String strActivityCodeMod	=	"activitycodemod";

	/**
	 * This method is used to initialize the search grid. Finally it forwards to
	 * the appropriate view based on the specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward doDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
    		log.debug("Inside the doDefault method of TariffAction");
    		setLinks(request);
			if (TTKCommon.getWebBoardId(request) == null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.hospital.required");
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request)==null)
			ArrayList alAssociatedTo = null;
			// get the session bean from the bean pool for each excecuting
			// thread
			TariffManager tariffObject = this.getTariffManager();
			Long lHospitalSeqId = new Long(TTKCommon.getWebBoardId(request));// get
																				// the
																				// web
																				// board
																				// id
			TableData tableData = TTKCommon.getTableData(request);
			// create new table data object
            tableData = new TableData();
			// create the required grid table
			tableData.createTableInfo("HospitalTariff", new ArrayList());
			request.getSession().setAttribute("tableData", tableData);
			request.getSession().setAttribute("searchparam", null);
			// make query to get associated company list to load to combo box
			alAssociatedTo = tariffObject
					.getAssociatedCompanyList(lHospitalSeqId);
			request.getSession().setAttribute("alAssociatedTo", alAssociatedTo);
			((DynaActionForm) form).initialize(mapping);// reset the form data
            TTKCommon.documentViewer(request);
			if ("Tariff".equals(TTKCommon.getActiveSubLink(request)))
            	return this.getForward(strHospitalTariffList, mapping, request);
            else
				return this.getForward(strHospitalTariffEmpanelmentList,
						mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

    /**
     * This method is used to search the data with the given search criteria.
	 * Finally it forwards to the appropriate view based on the specified
	 * forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward doSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
    		log.debug("Inside the doSearch method of TariffAction");
    		setLinks(request);
			// get the session bean from the bean pool for each excecuting
			// thread
			TariffManager tariffObject = this.getTariffManager();
			Long lHospitalSeqId = new Long(TTKCommon.getWebBoardId(request));// get
																				// the
																				// web
																				// board
																				// id
			TableData tableData = TTKCommon.getTableData(request);
			String strPageID = TTKCommon.checkNull(request
					.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request
					.getParameter("sortId"));
			if (!strPageID.equals("") || !strSortID.equals("")) {
				if (!strPageID.equals("")) {
					tableData.setCurrentPage(Integer.parseInt(TTKCommon
							.checkNull(request.getParameter("pageId"))));
    				return (mapping.findForward(strHospitalTariffList));
				}// /end of if(!strPageID.equals(""))
				else {
					tableData.setSortData(TTKCommon.checkNull(request
							.getParameter("sortId")));
					tableData.modifySearchData("sort");// modify the search data
				}// end of else
			}// end of if(!strPageID.equals("") || !strSortID.equals(""))
			else {
    			// create the required grid table
				tableData.createTableInfo("HospitalTariff", null);
				// fetch the data from the data access layer and set the data to
				// table object
				tableData.setSearchData(this.populateSearchCriteria(
						(DynaActionForm) form, lHospitalSeqId, request));
    			tableData.modifySearchData("search");
			}// end of else
			ArrayList alHospTariff = tariffObject
					.getHospitalTariffDetailList(tableData.getSearchData());
    		tableData.setData(alHospTariff, "search");
			request.getSession().setAttribute("tableData", tableData);
			// finally return to the grid screen
    		return this.getForward(strHospitalTariffList, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest
		// request,HttpServletResponse response)

    /**
	 * This method is used to get the previous set of records with the given
	 * search criteria. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward doBackward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
    		log.debug("Inside the doBackward method of TariffAction");
    		setLinks(request);
			// get the session bean from the bean pool for each excecuting
			// thread
			TariffManager tariffObject = this.getTariffManager();
			ArrayList alHospTariff = null;
			TableData tableData = TTKCommon.getTableData(request);
			// fetch the data from the data access layer and set the data to
			// table object
			tableData.modifySearchData(strBackward);// modify the search data
			// fetch the data from the data access layer and set the data to
			// table object
			alHospTariff = tariffObject.getHospitalTariffDetailList(tableData
					.getSearchData());
			tableData.setData(alHospTariff, strBackward);// set the table data
			request.getSession().setAttribute("tableData", tableData);// set the
																		// table
																		// data
																		// object
																		// to
																		// session
    		TTKCommon.documentViewer(request);
    		//	finally return to the grid screen
		 	return this.getForward(strHospitalTariffList, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doBackward(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

    /**
	 * This method is used to get the next set of records with the given search
	 * criteria. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward doForward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
    		log.debug("Inside the doForward method of TariffAction");
    		setLinks(request);
			// get the session bean from the bean pool for each excecuting
			// thread
			TariffManager tariffObject = this.getTariffManager();
			ArrayList alHospTariff = null;
			TableData tableData = TTKCommon.getTableData(request);
			// fetch the data from the data access layer and set the data to
			// table object
			tableData.modifySearchData(strForward);// modify the search data
			// fetch the data from the data access layer and set the data to
			// table object
			alHospTariff = tariffObject.getHospitalTariffDetailList(tableData
					.getSearchData());
			tableData.setData(alHospTariff, strForward);// set the table data
			request.getSession().setAttribute("tableData", tableData);// set the
																		// table
																		// data
																		// object
																		// to
																		// session
    		TTKCommon.documentViewer(request);
    		//	finally return to the grid screen
		 	return this.getForward(strHospitalTariffList, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doForward(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

    /**
	 * This method is used to get the details of the selected record from
	 * web-board. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward doChangeWebBoard(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	log.debug("Inside doChangeWebBoard method of TariffAction");
		// ChangeWebBoard method will call doDefault() method internally.
		if ("Tariff".equals(TTKCommon.getActiveSubLink(request)))
			return doDefault(mapping, form, request, response);
    	else
			return doDefaultEmpnlTariff(mapping, form, request, response);
	}// end of doChangeWebBoard(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

    /**
	 * This method is used to get the details of the selected record from
	 * web-board. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	public ActionForward doViewRevisionHistory(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
    		log.debug("Inside the doViewRevisionHistory method of TariffAction");
    		setLinks(request);
    		TableData tableData = TTKCommon.getTableData(request);
			if (request.getParameter("rownum") != null) {
				InsuranceVO insuranceVO = (InsuranceVO) tableData
						.getRowInfo(Integer.parseInt((String) request
								.getParameter("rownum")));
				request.getSession().setAttribute("insuranceVO", insuranceVO);
			}// end of if(request.getParameter("rownum")!=null)
            return mapping.findForward(strRevisionHistoryList);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doViewRevisionHistory(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

    /*
     * 
     * 
     */
	public ActionForward doShowUploadTariff(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		log.debug("Inside the doShowUploadTariff method of TariffAction");
		setLinks(request);
			((DynaActionForm) form).initialize(mapping);// reset the form data
	//	return this.getForward(strUploadTariff, mapping, request);
			if ("Tariff".equals(TTKCommon.getActiveSubLink(request)))
			return mapping.findForward(strUploadTariff);
		else
			return mapping.findForward(strUploadTariffEmpanelment);
		}// end of try
		catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doShowUploadTariff(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

    /**
     * @param frmHospitalTariff
     * @param lHospitalSeqId
     * @param request
     * @return
     */
    public ActionForward doUploadTariff(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{ 
    	    String finalFileName	=	"";
    		Reader reader		=	null;
    		FileWriter fileWriter=	null;
        	try{ 
        		
        		log.info("Inside the doUploadTariff method of TariffAction");
        		setLinks(request);        		
        		
    			DynaActionForm frmTariff=(DynaActionForm)form;
    			FormFile formFile = null;
    			formFile = (FormFile)frmTariff.get("file");
    			String fileName=formFile.getFileName();
    			String networkType	=	"";
    			String uploadType = "ACT";
    			String tariffType	=	(String) frmTariff.get("tariffType");
    			String payerCode	=	(String) frmTariff.get("payerCode");
    			String providerCode	=	(String) frmTariff.get("providerCode");
    			String insuranceCode	=	(String) frmTariff.get("payerCode");
    			String hospitalseqid  = (String)request.getSession().getAttribute("hospSeqIdforTariff");
    			String networkCategory=""; 
    			String providerGroupName ="";
    			String providerForNetwork ="";
    			Long hospGroupSeqId=null;
    			if("HOSP".equals(tariffType)){
    				networkType	=	(String) frmTariff.get("networkType");
    				
    			}else if("GROP".equals(tariffType)){
    				networkType	=	(String) frmTariff.get("networkType");
    				networkCategory = (String) frmTariff.get("networkCategory");
    				providerGroupName = (String) frmTariff.get("providerGroupName");
    				providerForNetwork = (String) frmTariff.get("providerForNetwork");
    			    hospGroupSeqId= Long.valueOf(String.valueOf(request.getSession().getAttribute("hospitalgroupseqid")));
    			}
    			else{
    				networkType	=	(String) frmTariff.get("networkType1");
    			  }
    			String corpCode		=	"|"+(String) frmTariff.get("corpCode")+"|";
    			String discAt		=	(String)frmTariff.get("discAt");

    			long addedBy		=	TTKCommon.getUserSeqId(request);
    			//String priceRefId	=	request.getParameter("priceRefId");
        		String fileExtn 	= 	GetFileExtension(formFile.toString());
        		
        	HSSFWorkbook workbook = null;
        	HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
        	    
        		/**
        		 * extract data from excel and create xml
        		 */
        		InputStream inputStream = formFile.getInputStream(); 
        		
      	      if (fileExtn.equalsIgnoreCase("xls"))
      	      {
      	    	  //POIFSFileSystem fs = new POIFSFileSystem(inputStream);
      	    	workbook =  (HSSFWorkbook) new HSSFWorkbook(inputStream);
      	    	  //log("xls="+wb_hssf.getSheetName(0));
      	    	 sheet = workbook.getSheetAt(0);
      	      }
        	  
        	    try {
        	        //Initializing the XML document
        	    	final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
        	        if(sheet==null)
        	        	request.setAttribute("updated", "Please upload proper File");
        	        else{
        	        Iterator<?> rows     = sheet.rowIterator ();

        	        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        	        ArrayList<TariffXMLTag> arrayList	=	null;
        	        TariffXMLTag tariffXMLTag			=	null;
        	        Tariffdetails tariffdetails			=	null;
        	        arrayList	=	new ArrayList<TariffXMLTag>();
					BigDecimal bdecimalVal	=null;
					String bdecimalstrVal	=	"";
		        	

        	        while (rows.hasNext ()) 
        	        {
        	        HSSFRow row =  (HSSFRow) rows.next(); 

        	            if(row.getRowNum()==0)
        	            	continue;
        	            
        	        // int rowNumber = row.getRowNum ();
        	            // display row number

        	            // get a row, iterate through cells.
        	            Iterator<?> cells = row.cellIterator (); 
        	            ArrayList<String> rowData = new ArrayList<String>();
						String temp	=	"";
        	            for(short i=0;i<=10;i++)
        	            {
        	            	HSSFCell cell	=	row.getCell(i);
        	            	
        	            	if(cell==null)
        	            		rowData.add("");
        	            	else
        	            		{
        	            		switch (cell.getCellType ())
    	     	                {
    		     	                case HSSFCell.CELL_TYPE_NUMERIC :
    		     	                {
		     	                    // Date CELL TYPE
		     	                    if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)){
		     	                    	//OLD CODE rowData.add(new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue()));
		     	                    	temp	=	new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue());
		     	                    }
		     	                    else // NUMERIC CELL TYPE
		     	                    {
		     	                    	temp	=	cell.getNumericCellValue () + "";
   		     	                 		//Convert Exponent value to Big Decimals
   										bdecimalVal	=	new BigDecimal(temp);
   										bdecimalstrVal	=	bdecimalVal.toPlainString();
   										if(i==0 || i==1)
   											temp	=	bdecimalstrVal.toString();
   										
										if(bdecimalstrVal.lastIndexOf('.')!=-1)
											if(".0".equals(bdecimalstrVal.substring(bdecimalstrVal.lastIndexOf('.'))))
												temp	=	bdecimalVal.longValue()+"";
		     	                    	//OLD CODE rowData.add(cell.getNumericCellValue () + "");
		     	                    }
		     	                   rowData.add(temp);
		     	                    break;
		     	                }

    		     	                case HSSFCell.CELL_TYPE_STRING :
    		     	                {
    		     	                    // STRING CELL TYPE
    		     	                    //String richTextString = cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    		     	                	String richTextString = cell.getStringCellValue().trim();
    		     	                	richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
    		     	                    rowData.add(richTextString);
    		     	                    break;
    		     	                }
    		     	                case HSSFCell.CELL_TYPE_BLANK :
    		     	                {	//HSSFRichTextString blankCell	=	cell.get.getRichStringCellValue();
    		     	                	String blankCell	=	cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    		     	                	rowData.add(blankCell);
    		     	                	break;
    		     	                }
    		     	                default:
    		     	                {
    		     	                    // types other than String and Numeric.
    		     	                    //System.out.println ("Type not supported.");
    		     	                    break;
    		     	                }
    	     	                } // end switch
    	            		}//else
        	            }//for
        	            	
        	            //JAXB
        	            double discPerc	=	0.0d;
        	            
        	            if(rowData.get(8)!=null && rowData.get(8)!="" &&
        	            		rowData.get(7)!=null && rowData.get(7)!=""/*
        	            		&& rowData.get(7).matches("[0-9]+")==true && rowData.get(6).matches("[0-9]+")==true*/)
        	            {
        	            	discPerc	=	new Double(rowData.get(8));
        	            	discPerc	=	(discPerc/100)*new Double(rowData.get(7));
        	            }
        	            	//ADDING THE CONTENT OF EXCEL CELLS TO TARIFF JAVA FILE
        	            try{
        	            	arrayList.add(new TariffXMLTag(rowData.get(0), rowData.get(1), rowData.get(2), rowData.get(3), 
        	            			rowData.get(4), rowData.get(5), rowData.get(6), (rowData.get(7)==null || rowData.get(7)=="")?null:new BigDecimal(rowData.get(7)), new BigDecimal(discPerc).setScale(3, BigDecimal.ROUND_CEILING), 
        	            			rowData.get(9), rowData.get(10),(rowData.get(8)==null || rowData.get(8)=="")?null:new BigDecimal(rowData.get(8))));
        	            
        	            }catch(java.lang.NumberFormatException numberExp){
        	            	TTKException expTTK = new TTKException();
        	                expTTK.setMessage("error.hospital.tariffUploadError");
        	                throw expTTK;
        	            }
        	            if(rowData.hashCode()!= 0)
        	            data.add(rowData);//adding Excel data to ArrayList
        	        	

        	        } //end while
        	        
        	        //JAXB
        	        tariffdetails	=	new Tariffdetails();
        	        PackageDetails packageDetails  	=	new PackageDetails(arrayList);
        	        Pricerefdetails pricerefdetails	=	null;
        	        
        	        if("Tariff".equals(TTKCommon.getActiveSubLink(request)))
        	        	pricerefdetails	=	new Pricerefdetails(corpCode, payerCode, providerCode,addedBy, tariffType, networkType, packageDetails,discAt,uploadType);
        	        else if("GROP".equals(tariffType))
        	        	pricerefdetails	=	new Pricerefdetails(corpCode, payerCode, providerCode,addedBy, "GROP", networkType, packageDetails,discAt,uploadType,providerForNetwork,networkCategory);
        	        else
        	        	pricerefdetails	=	new Pricerefdetails(corpCode, payerCode, providerCode,addedBy, "HOSP", networkType, packageDetails,discAt,uploadType);
        	 	   tariffdetails.setPricerefdetails(pricerefdetails);
        	 	   
        			FileOutputStream outputStream = null;
            		String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("tariffAdminUpload"));
        	        File folder = new File(path);
    				if(!folder.exists()){
    					folder.mkdir();
    				}
    	        	 

        	        String finalPath=(path+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+formFile);
        	        
        	     
    				outputStream = new FileOutputStream(new File(finalPath));
    				outputStream.write(formFile.getFileData());//Excel file Upload backUp
    				
    				
    				try {
    	       	 		 
    					finalFileName	=	path+"tariff-"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xml";
        	 			File file = new File(finalFileName);
        	 			JAXBContext jaxbContext = JAXBContext.newInstance(Tariffdetails.class);
        	 			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        				

        	 			// output pretty printed
        	 			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        	 	 
        	 			jaxbMarshaller.marshal(tariffdetails, file);
        	 			//jaxbMarshaller.marshal(tariffdetails, System.out);
        				

        	 		      } catch (JAXBException e) {
        	 		    	//  System.out.println("catch---"+e);
        	 			e.printStackTrace();
        	 		      }
    				
    	        	  

        	    
        	    TariffManager tariffObject=this.getTariffManager();
        	    
        	    File file 	=	new File(finalFileName);
        	    FileInputStream inputStream2	=	new FileInputStream(file); 
        	    FileReader xmlFile	=	new FileReader(file);
        	    Clob clob = null;
        	  //  Clob clob	=	tariffObject.uploadTariffEmpanelment(xmlFile,(int)file.length());
         	 

        	    String batchRefNo 	=	tariffObject.uploadTariffEmpanelment(inputStream2,fileName,TTKCommon.getUserSeqId(request),hospitalseqid,insuranceCode,tariffType,networkCategory,providerForNetwork,hospGroupSeqId);
        	    frmTariff.set("hiddenBatchRefNo",batchRefNo);
        	   // frmTariff.set("sussessYN", "Y");
        	    request.setAttribute("updated", "Tariff data Uploaded Successfully");
      		 	 request.getSession().setAttribute("frmTariffUploadItemEmpanelment", frmTariff);
        	    
        	    /*log.info("in Tariff Upload Action");
        	       String defaultMsg="There is a Problem, Please Contact AdminStrator";
                int fileLength=(clob==null)?defaultMsg.length():(int)clob.length();
                char []carData=new char[fileLength]; 
               
                //creating Error log File
                reader=(clob==null)?new StringReader(defaultMsg):clob.getCharacterStream();                          
                                 reader.read(carData);
               String newFileName	=	"LogFile"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xls";
                File file2=new File(TTKPropertiesReader.getPropertyValue("tariffErrorLogs")+newFileName);
                 fileWriter=new FileWriter(file2);                           
                 fileWriter.write("Date::"+new Date()+"\n");                   
                 fileWriter.write("========================================"+"\n");
                 fileWriter.write(carData);
                 fileWriter.flush();  
                 if(clob!=null)
                	 request.setAttribute("updated", "Tariff data Uploaded Successfully");
                 frmTariff.initialize(mapping);
                 frmTariff.set("newFileName", newFileName);
                 request.setAttribute("newFileName", newFileName);*/
        	        }//end else
               
                
        	    //return null;
        	       
        	        }//end try
        	    catch(IOException e)
        	    {
        	       //   
        	    }
        	    System.out.println("active Link--activity-->"+TTKCommon.getActiveSubLink(request));
        	    request.getSession().setAttribute("tariffType", tariffType);
        	    if("Tariff".equals(TTKCommon.getActiveSubLink(request)))
        	    	return mapping.findForward(strTariffUploaded);
        	    else
        	    	return mapping.findForward(strTariffUploadedEmpanelment);
        	}//end of try
        	catch(TTKException expTTK)
    		{
    			//log.info(expTTK.printStackTrace());
        		return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospTariffError));
    		}//end of catch(Exception exp)
        	finally{
        		try{
    				if(reader!=null)
    					reader.close();
    			}
    			catch(IOException ioExp)
    			{
    				log.error("Error in Reader");
    			}
    			try{
    				if(fileWriter!=null)
    					fileWriter.close();
    			}catch(IOException ioExp)
    			{
    				log.error("Error in fileWriter");
    			}
        	}
    }// end of doUploadTariff(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
    
  //doCreate files from uploaded files.
  	public ActionForward doCreateTariffFromUploadedFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
  	           HttpServletResponse response) throws Exception{
            try{
             setLinks(request);
             log.info("Inside doCreateTariffFromUploadedFile");
             HttpSession session = request.getSession();
             DynaActionForm frmTariffUploadItemEmpanelment=(DynaActionForm)form;
             String tariffSwitch = request.getParameter("tariffSwitch");
            
  	        
  	        String BatchRefNo=(String)frmTariffUploadItemEmpanelment.get("hiddenBatchRefNo");
  	        Long userSeqId = TTKCommon.getUserSeqId(request);
  	       
  	         TariffManager tariffObject=this.getTariffManager();
         	   
    			String batchNo[] 	=	tariffObject.uploadingTariff(BatchRefNo, userSeqId);
    		    
    			
    			
    			frmTariffUploadItemEmpanelment.set("batchRefNo",batchNo[0] );
    			//frmTariffUploadItemEmpanelment.set("batchNo", batchNo[2] );
    			frmTariffUploadItemEmpanelment.set("totalNoOfRows",batchNo[1] );
    			frmTariffUploadItemEmpanelment.set("totalNoOfRowsPassed",batchNo[2] );
    			frmTariffUploadItemEmpanelment.set("totalNoOfRowsFailed",batchNo[3] );
    		/*	frmTariffUploadItemEmpanelment.set("totalNoOfClaimsUploaded",batchNo[6] );
    			frmTariffUploadItemEmpanelment.set("totalNoOfRecordsUploaded",batchNo[8] );
	  		  	frmTariffUploadItemEmpanelment.set("batchTotalAmount", batchNo[9]);*/
	  		  	frmTariffUploadItemEmpanelment.set("sussessYN", "Y");
	  		  	frmTariffUploadItemEmpanelment.set("hiddenBatchRefNo", "");
	  		
    		 	
    		    request.getSession().setAttribute("frmTariffUploadItemEmpanelment", frmTariffUploadItemEmpanelment);
     		    request.setAttribute("successMsg", "File Uploaded Successfully");
     		    request.setAttribute("totalNoOfRowsFailed", batchNo[3]);
     		   if("ACT".equals(tariffSwitch)){
     			  return mapping.findForward(strTariffUploaded);
     		   }else{
     			  return mapping.findForward(strTariffUploadedEmpanelment);
     		   }
             	       
            }//end of try
           catch(TTKException expTTK)
            {
          	 ((DynaActionForm)request.getSession().getAttribute("frmTariffUploadItemEmpanelment")).set("hiddenBatchRefNo", "");
          	 return this.processExceptions(request, mapping, expTTK);
            }//end of catch(TTKException expTTK)
       catch(Exception exp)
      {
      	 ((DynaActionForm)request.getSession().getAttribute("frmTariffUploadItemEmpanelment")).set("hiddenBatchRefNo", "");
      	 return this.processExceptions(request, mapping, new TTKException(exp,strTariffUploaded));
        }//end of catch(Exception exp)
      }//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    /**
     * 
     */
    public ActionForward doUploadPharmacyTariff(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{ 
    	    String finalFileName	=	"";
    		Reader reader		=	null;
    		FileWriter fileWriter=	null;
        	try{ 
        		
        		log.info("Inside the doUploadPharmacyTariff method of TariffAction");
        		setLinks(request);        		
        		
    			DynaActionForm frmTariff=(DynaActionForm)form;
    			FormFile formFile = null;
    			formFile = (FormFile)frmTariff.get("file");
    			String fileName=formFile.getFileName();
    			String networkType	=	"";
    			
    			String tariffType	=	(String) frmTariff.get("tariffType");
    			String payerCode	=	(String) frmTariff.get("payerCode"); //"|"+(String) frmTariff.get("payerCode")+"|";
    			String providerCode	=	(String) frmTariff.get("providerCode"); //"|"+(String) frmTariff.get("providerCode")+"|";
    			
    			String insuranceCode	=	(String) frmTariff.get("payerCode");
    			String hospitalseqid  = (String)request.getSession().getAttribute("hospSeqIdforTariff");
    		    String networkCategory="";
    			String providerGroupName="";
    			String providerForGroup="";
    			Long hospGroupSeqId=null;
    			if("HOSP".equals(tariffType))
    				networkType	=	(String) frmTariff.get("networkType");
    			else if("GROP".equals(tariffType)){
    				networkType	=	(String) frmTariff.get("networkType");
    				networkCategory = (String) frmTariff.get("networkCategory");
    				providerGroupName = (String) frmTariff.get("providerGroupName");
    				providerForGroup = (String) frmTariff.get("providerForNetwork");
    			    hospGroupSeqId= Long.valueOf(String.valueOf(request.getSession().getAttribute("hospitalgroupseqid")));
    			}
    			else
    				networkType	=	(String) frmTariff.get("networkType1");
    			String corpCode		=	"|"+(String) frmTariff.get("corpCode")+"|";
    			String discAt		=	(String)frmTariff.get("discAt");

    			long addedBy		=	TTKCommon.getUserSeqId(request);
    			//String priceRefId	=	request.getParameter("priceRefId");
        		String fileExtn 	= 	GetFileExtension(formFile.toString());
        		String uploadType = "PHARMA";
        		
        	HSSFWorkbook workbook = null;
        	HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
        	    
        		/**
        		 * extract data from excel and create xml
        		 */
        		InputStream inputStream = formFile.getInputStream(); 
        		
      	      if (fileExtn.equalsIgnoreCase("xls"))
      	      {
      	    	  //POIFSFileSystem fs = new POIFSFileSystem(inputStream);
      	    	workbook =  (HSSFWorkbook) new HSSFWorkbook(inputStream);
      	    	  //log("xls="+wb_hssf.getSheetName(0));
      	    	 sheet = workbook.getSheetAt(0);
      	      }
        	 
        	    try {
        	        //Initializing the XML document
        	    	final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
        	        if(sheet==null)
        	        	request.setAttribute("updated", "Please upload proper File");
        	        else{
        	        Iterator<?> rows     = sheet.rowIterator ();

        	        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        	        ArrayList<PharmacyTariffXMLTag> arrayList	=	null;
        	        Tariffdetails tariffdetails			=	null;
        	        arrayList	=	new ArrayList<PharmacyTariffXMLTag>();
					BigDecimal bdecimalVal	=null;
					String bdecimalstrVal	=	"";
        	        while (rows.hasNext ()) 
        	        {
        	        HSSFRow row =  (HSSFRow) rows.next(); 

        	            if(row.getRowNum()==0)
        	            	continue;
        	            
        	        // int rowNumber = row.getRowNum ();
        	            // display row number

        	            // get a row, iterate through cells.
        	            Iterator<?> cells = row.cellIterator (); 
        	            ArrayList<String> rowData = new ArrayList<String>();
						String temp	=	"";
        	            for(short i=0;i<=10;i++)
        	            {
        	            	HSSFCell cell	=	row.getCell(i);
        	            	
        	            	if(cell==null)
        	            		rowData.add("");
        	            	else
        	            		{
        	            		switch (cell.getCellType ())
    	     	                {
    		     	                case HSSFCell.CELL_TYPE_NUMERIC:
    		     	                {
		     	                    // Date CELL TYPE
		     	                    if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)){
		     	                    	//OLD CODE rowData.add(new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue()));
		     	                    	temp	=	new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue());
		     	                    }
		     	                    else // NUMERIC CELL TYPE
		     	                    {
		     	                    	//DecimalFormat(NEW CODE)
		     	                    	 DecimalFormat df = new DecimalFormat("#");
		     	                          df.setMaximumFractionDigits(8);
		     	                          temp=df.format(cell.getNumericCellValue());
		     	                    	/*OLD CODE
		     	                    	bdecimalVal	=	new BigDecimal(cell.getNumericCellValue ());
		     	                    	temp	=bdecimalVal.longValue()+"";
   		     	                 		*/
		     	                          //Convert Exponent value to Big Decimals
   										/*bdecimalVal	=	new BigDecimal(temp);
   										bdecimalstrVal	=	bdecimalVal.toPlainString();
   										if(i==0 || i==1)
   											temp	=	bdecimalVal.toString();
   										
										if(bdecimalstrVal.lastIndexOf('.')!=-1)
											if(".0".equals(bdecimalstrVal.substring(bdecimalstrVal.lastIndexOf('.'))))
												temp	=	bdecimalVal.longValue()+"";*/

		     	                    	//OLD CODE rowData.add(cell.getNumericCellValue () + "");
		     	                    }
		     	                   rowData.add(temp);
		     	                    break;
		     	                }

    		     	                case HSSFCell.CELL_TYPE_STRING :
    		     	                {
    		     	                    // STRING CELL TYPE
    		     	                    //String richTextString = cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    		     	                	String richTextString = cell.getStringCellValue().trim();
    		     	                	richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
    		     	                    rowData.add(richTextString);
    		     	                    break;
    		     	                }
    		     	                case HSSFCell.CELL_TYPE_BLANK :
    		     	                {	//HSSFRichTextString blankCell	=	cell.get.getRichStringCellValue();
    		     	                	String blankCell	=	cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    		     	                	rowData.add(blankCell);
    		     	                	break;
    		     	                }
    		     	                default:
    		     	                {
    		     	                    // types other than String and Numeric.
    		     	                    //System.out.println ("Type not supported.");
    		     	                    break;
    		     	                }
    	     	                } // end switch
    	            		}//else
        	            }//for
        	            	
        	            //JAXB
        	            	//ADDING THE CONTENT OF EXCEL CELLS TO TARIFF JAVA FILE
        	            try{
        	            	arrayList.add(new PharmacyTariffXMLTag(rowData.get(0), rowData.get(1), rowData.get(2), rowData.get(3),rowData.get(4), 
        	            			(rowData.get(5)==null || rowData.get(5)=="")?null:new BigDecimal(rowData.get(5)),
        	            			(rowData.get(6)==null || rowData.get(6)=="")?null:new BigDecimal(rowData.get(6)),
        	            			(rowData.get(7)==null || rowData.get(7)=="")?null:new BigDecimal(rowData.get(7)),
        	            			(rowData.get(8)==null || rowData.get(8)=="")?null:new BigDecimal(rowData.get(8)),
        	            			 rowData.get(9), rowData.get(10)));
        	            }catch(java.lang.NumberFormatException numberExp){
        	            	TTKException expTTK = new TTKException();
        	                expTTK.setMessage("error.hospital.tariffUploadError");
        	                throw expTTK;
        	            }
        	            
        	            if(rowData.hashCode()!= 0)
        	            data.add(rowData);//adding Excel data to ArrayList

        	        } //end while
        	        
        	        //JAXB
        	        tariffdetails =	new Tariffdetails();
        	        PharmacyPackageDetails pharmacyPackageDetails  	=	new PharmacyPackageDetails(arrayList);
        	        Pricerefdetails pricerefdetails	=	null;
        	        
        	        pricerefdetails	=	new Pricerefdetails(corpCode, payerCode, providerCode,addedBy, "HOSP", networkType, pharmacyPackageDetails,discAt,uploadType);
        	        
        	 	   tariffdetails.setPricerefdetails(pricerefdetails);
        	 	   
        			FileOutputStream outputStream = null;
            		String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("tariffAdminUpload"));
        	        File folder = new File(path);
    				if(!folder.exists()){
    					folder.mkdir();
    				}

        	        String finalPath=(path+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+formFile);
        	        
    				outputStream = new FileOutputStream(new File(finalPath));
    				outputStream.write(formFile.getFileData());//Excel file Upload backUp
        	        
    				
    				try {
    	       	 		 
    					finalFileName	=	path+"tariff-"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xml";
        	 			File file = new File(finalFileName);
        	 		
        	 			JAXBContext jaxbContext = JAXBContext.newInstance(Tariffdetails.class);
        	 			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        	 	 
        	 			// output pretty printed
        	 			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        	 	 
        	 			
        	 			jaxbMarshaller.marshal(tariffdetails, file);
        	 			
        	 			
        	 			//jaxbMarshaller.marshal(tariffdetails, System.out);
        	 	 
        	 		      } catch (JAXBException e) {
        	 			e.printStackTrace();
        	 		      }
    				
        	   
        	    
        	    TariffManager tariffObject=this.getTariffManager();
        	    
        	    File file 	=	new File(finalFileName);
        	    FileInputStream inputStream2	=	new FileInputStream(file); 
        	    FileReader xmlFile	=	new FileReader(file);
        	    
        	  //  Clob clob	=	tariffObject.uploadTariffEmpanelment(xmlFile,(int)file.length());
        	  //  Clob clob	=	tariffObject.uploadPharmacyTariffEmpanelment(inputStream2);
        	    
        	    String batchRefNo 	=	tariffObject.uploadTariffEmpanelment(inputStream2,fileName,TTKCommon.getUserSeqId(request),hospitalseqid,insuranceCode,tariffType,networkCategory,providerForGroup,hospGroupSeqId);//common procedure for activity and pharmacy
         	    frmTariff.set("hiddenBatchRefNo",batchRefNo);
         	 
        	    request.setAttribute("updated", "Tariff data Uploaded Successfully");
      		 	 request.getSession().setAttribute("frmTariffUploadItemEmpanelment", frmTariff);
        	    log.debug("in Tariff Upload Action");
        	    
        	    
        	    
        	   /* String defaultMsg="There is a Problem, Please Contact AdminStrator";
                int fileLength=(clob==null)?defaultMsg.length():(int)clob.length();
                char []carData=new char[fileLength]; 
               
                //creating Error log File
                reader=(clob==null)?new StringReader(defaultMsg):clob.getCharacterStream();                          
                                 reader.read(carData);
                String newFileName	=	"LogFile"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xls";
                File file2=new File(TTKPropertiesReader.getPropertyValue("tariffErrorLogs")+newFileName);
                 fileWriter=new FileWriter(file2);                           
                 fileWriter.write("Date::"+new Date()+"\n");                   
                 fileWriter.write("========================================"+"\n");
                 fileWriter.write(carData);
                 fileWriter.flush();  
                 if(clob!=null)
                	 request.setAttribute("updated", "Tariff data Uploaded Successfully");
                 frmTariff.initialize(mapping);
                 frmTariff.set("newFileName", newFileName);
                 request.setAttribute("newFileName", newFileName);*/
        	        }//end else
               
                
        	    //return null;
        	       
        	        }//end try
        	    catch(IOException e)
        	    {
        	       // System.out.println("IOException " + e.getMessage());
        	    }
        	    System.out.println("active Link--pharma-->");
        	    request.getSession().setAttribute("tariffType", tariffType);
        	    	return mapping.findForward(strTariffUploadedEmpanelment);
        	}//end of try
        	catch(TTKException expTTK)
    		{
    			//log.info(expTTK.printStackTrace());
        		return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospTariffError));
    		}//end of catch(Exception exp)
        	finally{
        		try{
    				if(reader!=null)
    					reader.close();
    			}
    			catch(IOException ioExp)
    			{
    				log.error("Error in Reader");
    			}
    			try{
    				if(fileWriter!=null)
    					fileWriter.close();
    			}catch(IOException ioExp)
    			{
    				log.error("Error in fileWriter");
    			}
        	}
    }// end of doUploadTariff(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private String getCellValue(HSSFCell cell) {
		if (cell == null)
			return "";
		else
			switch (cell.getCellType()) {

			case HSSFCell.CELL_TYPE_BLANK:
				return "";
			case HSSFCell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case HSSFCell.CELL_TYPE_NUMERIC: {
				if (org.apache.poi.hssf.usermodel.HSSFDateUtil
						.isCellDateFormatted(cell))
					return dateFormat.format(cell.getDateCellValue());
				else {
					long longg = new Double(cell.getNumericCellValue())
							.longValue();
					return (longg < 1) ? "" : longg + "";
				}
			}
			case HSSFCell.CELL_TYPE_ERROR:
				return "";
			case HSSFCell.CELL_TYPE_FORMULA:
				return "";

			default:
				return "";
			}
	}

	// This method is used to get the extension of the file attached
	// DonE for INTX - KISHOR KUMAR S H
	private static String GetFileExtension(String fname2) {
        String fileName = fname2;
		String fname = "";
		String ext = "";
		int mid = fileName.lastIndexOf(".");
		fname = fileName.substring(0, mid);
		ext = fileName.substring(mid + 1, fileName.length());
        return ext;
    }

	private static void log(String message) {
              
    }

	/**
	 * DO SEARCH FOR EMPANELMENT TARIFFS This method is used to initialize the
	 * search grid. Finally it forwards to the appropriate view based on the
	 * specified forward mappings
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this
	 *         request is processed
	 * @throws Exception
	 *             if any error occurs
     */
	
	public ActionForward doDefaultEmpnlTariff(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside the doDefaultEmpnlTariff method of TarifAction");
			setLinks(request);
			if (TTKCommon.getWebBoardId(request) == null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.hospital.required");
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request)==null)
			String switchToVal= (String) request.getSession().getAttribute("switchToVal");
			String forwardTo=null;
			TableData tableData = TTKCommon.getTableData(request);
			// create new table data object
			tableData = (TableData) request.getSession().getAttribute(
					"tableData");
			// create the required grid table
			tableData.createTableInfo("TariffSearchTable", new ArrayList());
		//	((DynaActionForm)form).initialize(mapping);//reset the form data
			request.getSession().setAttribute("tableData", tableData);
			DynaActionForm frmSearchTariffEmpanelment = (DynaActionForm) form;

			frmSearchTariffEmpanelment.set("providerCodeSearch", request
					.getSession().getAttribute("hospSeqIdforTariff"));
			frmSearchTariffEmpanelment.set("switchTo", "ACT");
			frmSearchTariffEmpanelment.set("activityCode","");
			request.getSession().setAttribute("frmSearchTariffItem",frmSearchTariffEmpanelment);
			frmSearchTariffEmpanelment.set("switchTo", "ACT");
			request.getSession().setAttribute("frmSearchTariffItem",
					frmSearchTariffEmpanelment);
			/*if(switchToVal!=null&&switchToVal.equals("PHARMACY/CONSUMABLE TARIFF")){
				request.getSession().setAttribute("switchToVal", "PHARMACY/CONSUMABLE TARIFF");
				forwardTo="searchPharmacyTariffEmpanelment";
			}
			else{*/
				request.getSession().setAttribute("switchToVal", "ACTIVITY TARIFF");
				forwardTo=strSearchTariffEmpanelment;
//			}
//				return this.getForward(strSearchTariffEmpanelment, mapping, request);
			return this.getForward(forwardTo, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doDefaultEmpnlTariff(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
	
	public ActionForward doDefaultEmpnlPharmacyTariff(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			log.debug("Inside the doDefaultEmpnlPharmacyTariff method of TarifAction");
			setLinks(request);
			if (TTKCommon.getWebBoardId(request) == null) {
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.hospital.required");
				throw expTTK;
			}// end of if(TTKCommon.getWebBoardId(request)==null)
			TableData tableData = TTKCommon.getTableData(request);
			// create new table data object
			tableData = (TableData) request.getSession().getAttribute(
					"tableData");
			// create the required grid table
			tableData.createTableInfo("PharmacyTariffSearchTable", new ArrayList());
		//	((DynaActionForm)form).initialize(mapping);//reset the form data
			request.getSession().setAttribute("tableData", tableData);
			DynaActionForm frmSearchTariffEmpanelment = (DynaActionForm) form;
			frmSearchTariffEmpanelment.initialize(mapping);
			frmSearchTariffEmpanelment.set("switchTo", "PAC");
			frmSearchTariffEmpanelment.set("providerCodeSearch", request
					.getSession().getAttribute("hospSeqIdforTariff"));
			request.getSession().setAttribute("frmSearchTariffItem",
					frmSearchTariffEmpanelment);
			return this
					.getForward(strSearchPharmacyTariffEmpanelment, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doDefaultEmpnlTariff(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
	/*
     * 
     * 
     */
	public ActionForward doCorporateDefault(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		log.debug("Inside the doCorporateDefault method of TariffAction");
		setLinks(request);
			((DynaActionForm) form).initialize(mapping);// reset the form data
		return mapping.findForward(strSelectCorporates);
		}// end of try
		catch (TTKException expTTK) {
		return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strHospTariffError));
		}// end of catch(Exception exp)
	}// end of doCorporateDefault(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward activityCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
			try{
			log.debug("Inside ClaimsAction activityCodeSearch");
			setLinks(request);
			TariffManager tariffObject=this.getTariffManager();
			HttpSession session=request.getSession();
			String switchToVal= (String) request.getSession().getAttribute("switchToVal");
			if(switchToVal!=null&&switchToVal.equals("ACTIVITY TARIFF")){
				switchToVal="ACTIVITY";
			}else{
				switchToVal="PHARMACY";
			}
			DynaActionForm frmActivityDetails=(DynaActionForm)session.getAttribute("frmActivityDetails");
			TableData activityCodeListData = null;
			if(session.getAttribute("activityCodeListData") != null)
			{
			activityCodeListData = (TableData)session.getAttribute("activityCodeListData");
			}//end of if((request.getSession()).getAttribute("icdListData") != null)
			else
			{
			activityCodeListData = new TableData();
			}//end of else
			
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
			if(strPageID.equals(""))
			{
			activityCodeListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
			activityCodeListData.modifySearchData("sort");//modify the search data                    
			}///end of if(!strPageID.equals(""))
			else
			{
			log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
			activityCodeListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			return mapping.findForward(strActivityCodeList);
			}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
			activityCodeListData.createTableInfo("ActivityCodeListTable",null);
			activityCodeListData.setSearchData(this.populateActivityCodeSearchCriteria((DynaActionForm)form,request));
			activityCodeListData.modifySearchData("search");				
			}//end of else
			
			ArrayList alActivityCodeList=null;
			alActivityCodeList= tariffObject.getActivityCodeList(activityCodeListData.getSearchData(),switchToVal);
			activityCodeListData.setData(alActivityCodeList, "search");
			//set the table data object to session
			request.getSession().setAttribute("activityCodeListData",activityCodeListData);
			return mapping.findForward(strActivityCodeList);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
			}//end of catch(Exception exp)
	}//end of activityCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doUploadBulkModifyTariff(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{ 
    	    String finalFileName	=	"";
    		Reader reader		=	null;
    		FileWriter fileWriter=	null;
        	try{ 
        		
        		log.debug("Inside the doUploadBulkModifyTariff method of TariffAction");
        		setLinks(request);        		
        		
    			DynaActionForm frmTariffUploadItemEmpanelment=(DynaActionForm)form;
    			FormFile formFile = null;
    			formFile = (FormFile)frmTariffUploadItemEmpanelment.get("file");
    			String networkType	=	"";
    			String switchToVal= (String) request.getSession().getAttribute("switchToVal");
    			String uploadType = null;
    			if(switchToVal!=null&&switchToVal.equals("ACTIVITY TARIFF"))
    			uploadType = "ACT";
    			else
    			uploadType = "PHARMA";
    			String tariffType	=	(String) frmTariffUploadItemEmpanelment.get("tariffType");
    			String payerCode	=	"|"+(String) frmTariffUploadItemEmpanelment.get("payerCode")+"|";
    			String providerCode	=	"|"+(String) frmTariffUploadItemEmpanelment.get("providerCode")+"|";
    			if("HOSP".equals(tariffType))
    				networkType	=	"|"+(String) frmTariffUploadItemEmpanelment.get("networkType")+"|";
    			else
    				networkType	=	"|"+(String) frmTariffUploadItemEmpanelment.get("networkType1")+"|";
    			String corpCode		=	"|"+(String) frmTariffUploadItemEmpanelment.get("corpCode")+"|";
    			String discAt		=	(String)frmTariffUploadItemEmpanelment.get("discAt");

    			long addedBy		=	TTKCommon.getUserSeqId(request);
        		String fileExtn 	= 	GetFileExtension(formFile.toString());
        		
        	HSSFWorkbook workbook = null;
        	HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
        	    
        		/**
        		 * extract data from excel and create xml
        		 */
        		InputStream inputStream = formFile.getInputStream(); 
        		
      	      if (fileExtn.equalsIgnoreCase("xls"))
      	      {
      	    	  //POIFSFileSystem fs = new POIFSFileSystem(inputStream);
      	    	workbook =  (HSSFWorkbook) new HSSFWorkbook(inputStream);
      	    	  //log("xls="+wb_hssf.getSheetName(0));
      	    	 sheet = workbook.getSheetAt(0);
      	      }  
        	    try {
        	        //Initializing the XML document
        	    	final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
        	        if(sheet==null)
        	        	request.setAttribute("errorMsg", "Please upload proper File");
        	        else{
        	        Iterator<?> rows     = sheet.rowIterator ();
        	        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        	        ArrayList<TariffXMLTag> arrayList	=	null;
        	        TariffXMLTag tariffXMLTag			=	null;
        	        Tariffdetails tariffdetails			=	null;
        	        arrayList	=	new ArrayList<TariffXMLTag>();
					BigDecimal bdecimalVal	=null;
					String bdecimalstrVal	=	"";
        	        while (rows.hasNext ()) 
        	        {
        	        HSSFRow row =  (HSSFRow) rows.next(); 
        	            if(row.getRowNum()==0)
        	            	continue;
        	            // get a row, iterate through cells.
        	            Iterator<?> cells = row.cellIterator (); 
        	            ArrayList<String> rowData = new ArrayList<String>();
						String temp	=	"";
        	            for(short i=0;i<=6;i++)
        	            {
        	            	HSSFCell cell	=	row.getCell(i);
        	            	
        	            	if(cell==null)
        	            		rowData.add("");
        	            	else
        	            		{
        	            		switch (cell.getCellType ())
    	     	                {
    		     	                case HSSFCell.CELL_TYPE_NUMERIC :
    		     	                {
		     	                    // Date CELL TYPE
		     	                    if(HSSFDateUtil.isCellDateFormatted(cell)){
		     	                    	//OLD CODE rowData.add(new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue()));
		     	                    	temp	=	new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue());
		     	                    }
		     	                    else // NUMERIC CELL TYPE
		     	                    {
		     	                    	temp	=	cell.getNumericCellValue () + "";
   		     	                 		//Convert Exponent value to Big Decimals
   										bdecimalVal	=	new BigDecimal(temp);
   										bdecimalstrVal	=	bdecimalVal.toPlainString();
   										if(i==0 || i==1)
   											temp	=	bdecimalstrVal.toString();
   										
										if(bdecimalstrVal.lastIndexOf('.')!=-1)
											if(".0".equals(bdecimalstrVal.substring(bdecimalstrVal.lastIndexOf('.'))))
												temp	=	bdecimalVal.longValue()+"";

		     	                    	//OLD CODE rowData.add(cell.getNumericCellValue () + "");
		     	                    }
		     	                   rowData.add(temp);
		     	                    break;
		     	                }

    		     	                case HSSFCell.CELL_TYPE_STRING :
    		     	                {
    		     	                    // STRING CELL TYPE
    		     	                    //String richTextString = cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    		     	                	String richTextString = cell.getStringCellValue().trim();
    		     	                	richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
    		     	                    rowData.add(richTextString);
    		     	                    break;
    		     	                }
    		     	                case HSSFCell.CELL_TYPE_BLANK :
    		     	                {	//HSSFRichTextString blankCell	=	cell.get.getRichStringCellValue();
    		     	                	String blankCell	=	cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
    		     	                	rowData.add(blankCell);
    		     	                	break;
    		     	                }
    		     	                default:
    		     	                {
    		     	                    // types other than String and Numeric.
    		     	                    //System.out.println ("Type not supported.");
    		     	                    break;
    		     	                }
    	     	                } // end switch
    	            		}//else
        	            }//for
        	            	
        	            //JAXB
        	            double discPerc	=	0.0d;
        	            	//ADDING THE CONTENT OF EXCEL CELLS TO TARIFF JAVA FILE
        	            try{
        	            	arrayList.add(new TariffXMLTag(rowData.get(0), rowData.get(1), rowData.get(2), rowData.get(3), 
        	            			rowData.get(4), rowData.get(5), rowData.get(6)));
        	            
        	            }catch(java.lang.NumberFormatException numberExp){
        	            	TTKException expTTK = new TTKException();
        	                expTTK.setMessage("error.hospital.tariffUploadError");
        	                throw expTTK;
        	            }
        	            if(rowData.hashCode()!= 0)
        	            data.add(rowData);//adding Excel data to ArrayList
        	        } //end while
        	        tariffdetails	=	new Tariffdetails();
        	        PackageDetails packageDetails  	=	new PackageDetails(arrayList);
        	        Pricerefdetails pricerefdetails	=	null;
        	        
        	        if("Tariff".equals(TTKCommon.getActiveSubLink(request)))
        	        	pricerefdetails	=	new Pricerefdetails(corpCode, payerCode, providerCode,addedBy, tariffType, networkType, packageDetails,discAt,uploadType);
        	        else 
        	        	pricerefdetails	=	new Pricerefdetails(corpCode, payerCode, providerCode,addedBy, "HOSP", networkType, packageDetails,discAt,uploadType);
        	 	   tariffdetails.setPricerefdetails(pricerefdetails);
        			FileOutputStream outputStream = null;
            		String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("tariffAdminUpload"));
            		File folder = new File(path);
    				if(!folder.exists()){
    					folder.mkdir();
    				}
        	        String finalPath=(path+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+formFile);
        	        outputStream = new FileOutputStream(new File(finalPath));
    				outputStream.write(formFile.getFileData());//Excel file Upload backUp
    				try {
    					finalFileName	=	path+"tariff-"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xml";
        	 			File file = new File(finalFileName);
        	 			JAXBContext jaxbContext = JAXBContext.newInstance(Tariffdetails.class);
        	 			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        	 			// output pretty printed
        	 			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        	 			jaxbMarshaller.marshal(tariffdetails, file);
        	 			//jaxbMarshaller.marshal(tariffdetails, System.out);
        	 	 
        	 		      } catch (JAXBException e) {
        	 			e.printStackTrace();
        	 		      }

        	    TariffManager tariffObject=this.getTariffManager();
        	    File file 	=	new File(finalFileName);
        	    FileInputStream inputStream2	=	new FileInputStream(file); 
        	    FileReader xmlFile	=	new FileReader(file);
        	    
        	  //  Clob clob	=	tariffObject.uploadTariffEmpanelment(xmlFile,(int)file.length());
        	    Clob clob	=	tariffObject.uploadBulkModifyTariff(inputStream2);

        	    log.debug("in Tariff Upload Action");
        	    String defaultMsg="There is a Problem, Please Contact AdminStrator";
                int fileLength=(clob==null)?defaultMsg.length():(int)clob.length();
                char []carData=new char[fileLength]; 
               
                //creating Error log File
                reader=(clob==null)?new StringReader(defaultMsg):clob.getCharacterStream();                          
                                 reader.read(carData);
                String newFileName	=	"LogFile"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xls";
                File bulkFolder=new File(TTKPropertiesReader.getPropertyValue("BulkModifyErrorLogs"));
                		
                if(!bulkFolder.exists()){
                	bulkFolder.mkdir();
				} 
             //   System.out.println("bulkFolder.getAbsolutePath()+\\+newFileName::"+bulkFolder.getAbsolutePath()+"\\"+newFileName);
                File file2=new File(bulkFolder.getAbsolutePath()+"/"+newFileName);
                fileWriter=new FileWriter(file2);                           
                 fileWriter.write("Date::"+new Date()+"\n");                   
                 fileWriter.write("========================================"+"\n");
                 fileWriter.write(carData);
                 fileWriter.flush();  
                 if(clob!=null)
                	 request.setAttribute("updated", "Tariff data Uploaded Successfully");
                 frmTariffUploadItemEmpanelment.initialize(mapping);
                 frmTariffUploadItemEmpanelment.set("newFileName", newFileName);
                 request.setAttribute("newFileName", newFileName);
        	        }//end else
               
                
        	    //return null;
        	       
        	        }//end try
        	    catch(IOException e)
        	    {
        	       //   
        	    }
        	    if("Tariff".equals(TTKCommon.getActiveSubLink(request)))
        	    	return mapping.findForward(strTariffUploaded);
        	    else
        	    	return mapping.findForward(strTariffUploadedEmpanelment);
        	}//end of try
        	catch(TTKException expTTK)
    		{
    			//log.info(expTTK.printStackTrace());
        		return this.processExceptions(request, mapping, expTTK);
    		}//end of catch(TTKException expTTK)
    		catch(Exception exp)
    		{
    			return this.processExceptions(request, mapping, new TTKException(exp,strHospTariffError));
    		}//end of catch(Exception exp)
        	finally{
        		try{
    				if(reader!=null)
    					reader.close();
    			}
    			catch(IOException ioExp)
    			{
    				log.error("Error in Reader");
    			}
    			try{
    				if(fileWriter!=null)
    					fileWriter.close();
    			}catch(IOException ioExp)
    			{
    				log.error("Error in fileWriter");
    			}
        	}
    }// end of doUploadTariff(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
	
	private ArrayList<Object> populateActivityCodeSearchCriteria(DynaActionForm frmActivitiesList,HttpServletRequest request)
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivitiesList.getString("sActivityCode")));
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivitiesList.getString("sActivityCodeDesc")));
		alSearchParams.add(TTKCommon.getUserSeqId(request));		
		return alSearchParams;
	}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

	public ActionForward closeActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
		setLinks(request);
		log.debug("Inside ClaimsAction closeActivityCode ");
		((DynaActionForm)form).initialize(mapping);
			return mapping.findForward(strEditTariffItemEmpanelment);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strEditTariffItemEmpanelment));
		}//end of catch(Exception exp)
	
		}//end of closeActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doActivityCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
		log.debug("Inside ClaimsAction doActivityCodeForward");
		setLinks(request);
		TableData activityCodeListData = (TableData)request.getSession().getAttribute("activityCodeListData");
		TariffManager tariffObject=this.getTariffManager();
		String switchToVal= (String) request.getSession().getAttribute("switchToVal");
		if(switchToVal!=null&&switchToVal.equals("ACTIVITY TARIFF")){
			switchToVal="ACTIVITY";
		}else{
			switchToVal="PHARMACY";
		}
		activityCodeListData.modifySearchData(strForward);//modify the search data
		ArrayList alPreauthList = tariffObject.getActivityCodeList(activityCodeListData.getSearchData(),switchToVal);
		activityCodeListData.setData(alPreauthList, strForward);//set the table data
		request.getSession().setAttribute("activityCodeListData",activityCodeListData);   //set the table data object to session
		return mapping.findForward(strActivityCodeList);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
		}//end of catch(Exception exp)
		}//end of doActivityCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward doSelectActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
	try{
		setLinks(request);
		log.debug("Inside ClaimsAction doSelectActivityCode ");

		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
		{
			request.setAttribute("rownum",request.getParameter("rownum"));
		  }//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
		((DynaActionForm)form).initialize(mapping);
		return mapping.findForward(strActivitydetails);
//		return this.getForward(strActivitydetails, mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
	}//end of catch(Exception exp)
	}//end of doSelectActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	
	public ActionForward selectActivityCode(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		try {
			setLinks(request);
			log.debug("Inside ClaimGenealAction selectActivityCode ");
				
				TableData activityCodeListData = (TableData) session.getAttribute("activityCodeListData");
				ActivityDetailsVO activityDetailsVO = (ActivityDetailsVO) activityCodeListData.getRowInfo(Integer.parseInt((String) request.getAttribute("rownum")));
				TariffUploadVO tariffUploadVO=(TariffUploadVO) session.getAttribute("tariffUploadVO");
				tariffUploadVO.setActivityCode(activityDetailsVO.getActivityCode());
				tariffUploadVO.setActivitySeqId(activityDetailsVO.getActivitySeqId());
				tariffUploadVO.setActivityTypeSeqId(Long.parseLong(activityDetailsVO.getActivityTypeId()));
				tariffUploadVO.setServiceSeqId(Long.valueOf(activityDetailsVO.getServiceInternalCode()));
				
				DynaActionForm TariffItemForm = (DynaActionForm)FormUtils.setFormValues("frmSearchTariffItem",
	            		tariffUploadVO,this,mapping,request);
				TariffItemForm.set("activityDesc", activityDetailsVO.getActivityCodeDesc());
	            TariffItemForm.set("caption","Edit");
	            TariffItemForm.set("serviceSeqId",String.valueOf(tariffUploadVO.getServiceSeqId()));
	            TariffItemForm.set("strInternalCode",tariffUploadVO.getInternalCode());
	            session.setAttribute("frmSearchTariffItem",TariffItemForm);
	            session.setAttribute("isActCodeChanged", "true");
			request.setAttribute("showPopUp", "Yes");
			return this.getForward(strActivityCodeMod, mapping, request);
		}// end of try
		catch (TTKException expTTK) {
			session.setAttribute("isActCodeChanged", "false");
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			session.setAttribute("isActCodeChanged", "false");
			return this.processExceptions(request, mapping, new TTKException(exp, strActivitydetails));
		}// end of catch(Exception exp)
	}// end of selectActivityCode(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
	
	
				public ActionForward doActivityCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
						HttpServletResponse response) throws Exception{
					try{
					log.debug("Inside ClaimsAction doActivityCodeBackward");
					setLinks(request);
					TableData activityCodeListData = (TableData)request.getSession().getAttribute("activityCodeListData");
					TariffManager tariffObject=this.getTariffManager();
					String switchToVal= (String) request.getSession().getAttribute("switchToVal");
					if(switchToVal!=null&&switchToVal.equals("ACTIVITY TARIFF")){
					switchToVal="ACTIVITY";
					}else{
					switchToVal="PHARMACY";
					}
					activityCodeListData.modifySearchData(strBackward);//modify the search data
					ArrayList alPreauthList = tariffObject.getActivityCodeList(activityCodeListData.getSearchData(),switchToVal);
					activityCodeListData.setData(alPreauthList, strBackward);//set the table data
					request.getSession().setAttribute("activityCodeListData",activityCodeListData);   //set the table data object to session
					return mapping.findForward(strActivityCodeList);
					}//end of try
					catch(TTKException expTTK)
					{
					return this.processExceptions(request, mapping, expTTK);
					}//end of catch(TTKException expTTK)
					catch(Exception exp)
					{
					return this.processExceptions(request, mapping, new TTKException(exp,strActivityCodeList));
					}//end of catch(Exception exp)
			}//end of doActivityCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

			
				
    /*
	 * Returns the ArrayList which contains the populated search criteria
	 * elements.
	 * 
	 * @param frmHospitalTariff DynaActionForm will contains the values of
	 * corresponding fields.
	 * 
	 * @param lHospitalSeqId Long which contains hospital sequence id.
	 * 
	 * @param request HttpServletRequest object which contains the search
	 * parameter that is built.
	 * 
	 * @return alSearchParams ArrayList
	 */
	private ArrayList<Object> populateSearchCriteria(
			DynaActionForm frmHospitalTariff, Long lHospitalSeqId,
			HttpServletRequest request) {
		// build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(new SearchCriteria("HOSP_SEQ_ID", lHospitalSeqId
				.toString()));
		alSearchParams.add(new SearchCriteria("ASSOCIATED_SEQ_ID",
				(String) frmHospitalTariff.get("associatedTo"), "equals"));
		alSearchParams
				.add(new SearchCriteria("PRODUCT_POLICY_NO", TTKCommon
						.replaceSingleQots((String) frmHospitalTariff
								.get("proPolNo"))));
		// request.getSession().setAttribute("searchparam",alSearchParams);
		return alSearchParams;
	}// end of populateSearchCriteria

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
				hospTariff = (TariffManager) ctx
						.lookup("java:global/TTKServices/business.ejb3/TariffManagerBean!com.ttk.business.administration.TariffManager");
			}// end if
		}// end of try
		catch (Exception exp) {
			throw new TTKException(exp, strHospTariffError);
		}// end of catch
		return hospTariff;
	}// end getTariffManager()
}// end of class TariffAction