/**
 * @ (#) ClaimsSearchAction.java June 12th, 2006
 * Project      : TTK HealthCare Services
 * File         : ClaimsSearchAction.java
 * Author       : Krupa J
 * Company      : Span Systems Corporation
 * Date Created : June 12th, 2006
 *
 * @author       :  Krupa J
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.finance;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

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
import org.apache.struts.validator.DynaValidatorForm;

import com.ibm.icu.text.DecimalFormat;
import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.finance.FloatAccountManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.BankAddressVO;
import com.ttk.dto.finance.ChequeDetailVO;
import com.ttk.dto.finance.ChequeVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching the List of Claims.
 * This class also provides option for printing the Cheque.
 */
public class ClaimsSearchAction extends TTKAction
{
	private static final Logger log = Logger.getLogger( ClaimsSearchAction.class );
	private static final String strBackward="Backward";
	private static final String strForward="Forward";
	//forwards
	private static final String strClaims="claimslist";
	private static final String strChequeDetail="chequedetail";
	private static final String strChequesPrinting="printcheque";
	private static final String strReportdisplay="reportdisplay";
	private static final String strDebitList="debitlist";
	private static final String struploadbatchdetaillog="uploadbatchdetail";
	
//	Exception Message Identifier
	private static final String strBank="bank";
	 private static final String strPreAuth="Pre-Authorization";
	private static final String strReportExp="report";
	
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
			FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
			DynaActionForm frmClaims=(DynaActionForm)form;
			log.debug("Inside ClaimsSearchAction doDefault");
			if(TTKCommon.getWebBoardId(request) == null)
			{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.floatno.required");
				throw expTTK;
			}//end of if(TTKCommon.getWebBoardId(request) == null)
			String strFloatType="";
			TableData  tableData =TTKCommon.getTableData(request);
			//create new table data object
			tableData = new TableData();
			//create the required grid table
			tableData.createTableInfo("ClaimsSearchTable",new ArrayList());
			strFloatType= floatAccountManagerObject.getFloatType(TTKCommon.getWebBoardId(request));
			request.getSession().setAttribute("tableData",tableData);
			frmClaims.initialize(mapping);//reset the form data
			frmClaims.set("floatType",strFloatType);
			request.getSession().setAttribute("searchparam", null);
			return this.getForward(strClaims, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
	}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	/**
     * This method is used to get the details of the selected record from web-board.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    									  HttpServletResponse response) throws Exception{
    	log.debug("Inside ClaimsSearchAction doChangeWebBoard");
    	return doDefault(mapping,form,request,response);
    }//end of doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    		log.debug("Inside ClaimsSearchAction doSearch");
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		StringBuffer strClaimsAmt = new StringBuffer();
    		StringBuffer strConvertedClaimsAmt = new StringBuffer();
			StringBuffer strConvertedUSDAmt = new StringBuffer();
			StringBuffer strIncurredCurr = new StringBuffer();
    		String strClaimTypeDesc = "";
    		StringBuffer strDiscountedAmount = new StringBuffer();
    		StringBuffer strPayableAmount = new StringBuffer();
    		DynaActionForm frmClaims=(DynaActionForm)form;
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
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
    				return (mapping.findForward(strClaims));    				
    			}//end of else
    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
    		else
    		{
    			//create the required grid table
    			tableData.createTableInfo("ClaimsSearchTable",null);
    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
    			tableData.modifySearchData("search");
    		}//end of else
    		ArrayList alClaims= floatAccountManagerObject.getClaimList(tableData.getSearchData());
    		tableData.setData(alClaims, "search");
    		

    		//impl for loading claim amount in the grid
    		if(alClaims != null && alClaims.size() > 0 )
    		{
    			String strComma="";
    			String stopClaimYN=null;
    			for(int i=0 ;i<alClaims.size() ;i++)
    			{
    				if(i!=0)
    				{
    					strComma=",";
    				}//end of if(i!=0)
    				if(((ChequeVO)alClaims.get(i)).getClaimAmt()!=null)
    				{
    					strClaimsAmt  = strClaimsAmt.append(strComma+"\""+((ChequeVO)alClaims.get(i)).getClaimAmt().toString()+"\"");
    				}//end of if(((ChequeVO)alClaims.get(i)).getClaimAmt()!=null)
    				else
    				{
    					strClaimsAmt  = strClaimsAmt.append(strComma+"\""+"0"+"\"");
    				}//end of else
    				
    				if(((ChequeVO)alClaims.get(i)).getConvertedApprovedAmount()!=null)
    				{
    					strConvertedClaimsAmt=strConvertedClaimsAmt.append(strComma+"\""+((ChequeVO)alClaims.get(i)).getConvertedApprovedAmount().toString()+"\"");
						strConvertedUSDAmt=strConvertedUSDAmt.append(strComma+"\""+((ChequeVO)alClaims.get(i)).getUsdAmount().toString()+"\"");
						strIncurredCurr=strIncurredCurr.append(strComma+"\""+((ChequeVO)alClaims.get(i)).getIncuredCurencyFormat().toString()+"\"");
						strDiscountedAmount  = strDiscountedAmount.append(strComma+"\""+((ChequeVO)alClaims.get(i)).getDiscountedAmount().toString()+"\"");
						strPayableAmount  = strPayableAmount.append(strComma+"\""+((ChequeVO)alClaims.get(i)).getPayableAmount().toString()+"\"");
    				}//end of if(((ChequeVO)alClaims.get(i)).getClaimAmt()!=null)
    				else
    				{
    					strConvertedClaimsAmt=strConvertedClaimsAmt.append(strComma+"\""+"0"+"\"");
    					strConvertedUSDAmt=strConvertedUSDAmt.append(strComma+"\""+"0"+"\"");
    					strIncurredCurr=strIncurredCurr.append(strComma+"\""+"0"+"\"");
    					strDiscountedAmount  = strDiscountedAmount.append(strComma+"\""+"0"+"\"");
						strPayableAmount  = strPayableAmount.append(strComma+"\""+"0"+"\"");
    				}//end of else
    				strClaimTypeDesc = "\""+((ChequeVO)alClaims.get(0)).getClaimTypeDesc().toString()+"\"";
    				//String reviewFlag=((ChequeVO)alClaims.get(i)).getReviewFlag().toString();//bk
    				//request.getSession().setAttribute("reviewFlag", reviewFlag);//bk
    				stopClaimYN =((ChequeVO)alClaims.get(i)).getStopClaimsYN();
    			}//end of for
    			//bk
    			request.setAttribute("stopClaimYN", stopClaimYN);
    			if(alClaims != null && alClaims.size() > 0 )
        		{
    				/*String reviewFlag =null;
        			for(int i=0 ;i<alClaims.size() ;i++)
        			{
        				 reviewFlag =((ChequeVO)alClaims.get(i)).getReviewFlag();
        				
        			}*/
        			//request.setAttribute("reviewFlag", ((ChequeVO)alClaims.get(0)).getReviewFlag());
    				request.setAttribute("CLG","CLG");
    				request.getSession().setAttribute("reviewFlag",((ChequeVO)alClaims.get(0)).getReviewFlag());
        			
        		}
    			//end bk
    			frmClaims.set("currencyType",((ChequeVO)alClaims.get(0)).getCurrencyType());
    			frmClaims.set("strClaimsAmt",strClaimsAmt.toString());
    			frmClaims.set("strConvertedClaimsAmt",strConvertedClaimsAmt.toString());
    			frmClaims.set("strConvertedUSDAmt",strConvertedUSDAmt.toString());
    			frmClaims.set("strIncurredCurr",strIncurredCurr.toString());
    			frmClaims.set("strClaimTypeDesc",strClaimTypeDesc.toString());
    			frmClaims.set("sussessYN", "N"); 
    			frmClaims.set("strDiscountedAmount",strDiscountedAmount.toString());
    			frmClaims.set("strPayableAmount",strPayableAmount.toString());
    			//displaying available float balance
    			if(((ChequeVO)alClaims.get(0)).getAvblFloatBalance()!= null)
    			{
    				frmClaims.set("avblFloatBalance",((ChequeVO)alClaims.get(0)).getAvblFloatBalance().toString());
    			}//end of if(((ChequeVO)alClaims.get(0)).getAvblFloatBalance()!= null)
    			else
    			{
    				frmClaims.set("avblFloatBalance","0");
    			}//end of else
    		}//end of if(alClaims != null && alClaims.size() > 0 )
    		//set the table data object to session
    		request.getSession().setAttribute("tableData",tableData);
    		//finally return to the grid screen
    		return this.getForward(strClaims, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
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
    		log.debug("Inside ClaimsSearchAction doForward");
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		ArrayList alClaims = floatAccountManagerObject.getClaimList(tableData.getSearchData());
    		tableData.setData(alClaims, strForward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
    		return this.getForward(strClaims, mapping, request);//finally return to the grid screen
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
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
    		log.debug("Inside ClaimsSearchAction doBackward");
    		TableData  tableData =TTKCommon.getTableData(request);
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
    		ArrayList alClaims = floatAccountManagerObject.getClaimList(tableData.getSearchData());
    		tableData.setData(alClaims, strBackward);//set the table data
    		request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
    		return this.getForward(strClaims, mapping, request);//finally return to the grid screen
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
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
    		log.debug("Inside ClaimsSearchAction doViewCheque");
    		TableData  tableData =TTKCommon.getTableData(request);
    		DynaValidatorForm frmChequeDetail = (DynaValidatorForm)form;
    		//create a new Cheque object
    		ChequeVO chequeVO = new ChequeVO();
    		ChequeDetailVO chequeDetailVO = new ChequeDetailVO();
    		BankAddressVO bankAddressVO = new BankAddressVO ();
    		chequeVO = (ChequeVO)tableData.getRowInfo(Integer.parseInt((String)(frmChequeDetail).get("rownum")));
    		chequeDetailVO.setSeqID(chequeVO.getSeqID());
    		chequeDetailVO.setPaymentSeqId(new Long(0));
    		frmChequeDetail = (DynaValidatorForm)FormUtils.setFormValues("frmChequeDetail",chequeDetailVO, 
    																	  this, mapping, request);
    		frmChequeDetail.set("bankAddressVO", (DynaActionForm)FormUtils.setFormValues("frmFinanceBankAddress",
    																		  bankAddressVO,this,mapping,request));
    		frmChequeDetail.set("caption","Cheque Details");
    		request.getSession().setAttribute("frmChequeDetail",frmChequeDetail);
    		return mapping.findForward(strChequeDetail);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
    }//end of doViewCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
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
    public ActionForward doClaimsCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    									HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ClaimsSearchAction doClaimsCheque");
    		return (mapping.findForward(strReportdisplay));
    	}
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
    }//end of doClaimsCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to print the check.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doPrintCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    								   HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ClaimsSearchAction doPrintCheque");
    		DynaValidatorForm frmCheque = (DynaValidatorForm)form;
    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
    		TableData  tableData =TTKCommon.getTableData(request);
    		JasperReport jasperReport,emptyReport;
    		ArrayList<Object> alPrintCheque = new ArrayList<Object>();
    		StringBuffer JrxmlFileName = null;
    		JasperPrint jasperPrint;
    		TTKReportDataSource reportDataSource = null;
    		String strPaymentSeqID = getConcatenatedSeqID(request,(TableData)request.getSession().
    													  		   getAttribute("tableData"));
    		if(frmCheque.get("paymethod").equals("PCA"))
    		{
    			alPrintCheque.add(strPaymentSeqID);
    			alPrintCheque.add(TTKCommon.getWebBoardId(request));
    			alPrintCheque.add(frmCheque.getString("paymethod"));
    			alPrintCheque.add(TTKCommon.getUserSeqId(request));
    			int intResult=floatAccountManagerObject.generateChequeAdvice(alPrintCheque);
    			if(intResult>0)
    			{
    				request.setAttribute("updated","message.chequePrintedSuccessfully");
    			}//end of if(intResult>0)
    		}//end of if(frmCheque.get("paymethod").equals("PCA"))
    		else if(frmCheque.get("paymethod").equals("EFT"))
    		{
    			//int intResult = 0;
    			alPrintCheque.add(strPaymentSeqID);
    			alPrintCheque.add(TTKCommon.getWebBoardId(request));
    			alPrintCheque.add(frmCheque.getString("remarks"));
    			alPrintCheque.add(frmCheque.getString("paymethod"));
    			alPrintCheque.add(TTKCommon.getUserSeqId(request));
    			int intResult=floatAccountManagerObject.setFundTransfer(alPrintCheque);
//    			String strFinBatchReport="/ReportsAction.do?mode=doGenerateReport&parameter="+
//    					strPaymentSeqID+"&fileName=generalreports/FinanceBatchEFTReport.jrxml&reportID=FinBatChck&reportType=PDF";

    			String strFinBatchReport="/ClaimsSearchAction.do?mode=doGenerateReport&fileName=generalreports/FinanceBatchEFTReport.jrxml&reportID=FinBatChck&reportType=PDF";
    			
    			if(intResult>0)
    			{
    				request.setAttribute("updated","message.chequePrintedSuccessfully");
    				request.setAttribute("FinBatchReport",strFinBatchReport);
    				frmCheque.set("parameterValue",strPaymentSeqID);
    			}//end of if(intResult>0)
    		}//end of else if(frmCheque.get("paymethod").equals("EFT"))
    		else if(frmCheque.get("paymethod").equals("PMM"))
    		{
    			Date dtCurrentDate = new Date();
    			alPrintCheque.add(strPaymentSeqID);
    			alPrintCheque.add(TTKCommon.getWebBoardId(request));
    			alPrintCheque.add(frmCheque.getString("startNo"));
    			alPrintCheque.add(frmCheque.getString("paymethod"));
    			alPrintCheque.add(TTKCommon.getUserSeqId(request));
    			alPrintCheque.add(new Timestamp(dtCurrentDate.getTime()));
    			if(((ChequeVO)tableData.getRowInfo(0)).getFileName()!=null)
    			{
    				JrxmlFileName = new StringBuffer(((ChequeVO)tableData.getRowInfo(0)).getFileName().toString());
    				File file = new File("generalreports/"+JrxmlFileName.append(".jrxml").toString());
    				if(file.exists())
    				{
    					reportDataSource = floatAccountManagerObject.generateCheque(alPrintCheque);
    				}//end of if(file.exists())
    				else
    				{
    					TTKException expTTK = new TTKException();
    					expTTK.setMessage("error.jrxmlfile.required");
    					throw expTTK;
    				}//end of else
    			}//end of if(((ChequeVO)tableData.getRowInfo(0)).getFileName()!=null)
    			
    			ByteArrayOutputStream boas=new ByteArrayOutputStream();
    			  
    			jasperReport = JasperCompileManager.compileReport("generalreports/"+JrxmlFileName.toString());
    			emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
    			HashMap hashMap = new HashMap();
    			
    			 String fileName="";
    			 ResultSet rs=reportDataSource.getResultData();
    			if(rs!=null && rs.next()){
    				fileName= rs.getString("file_name");
					 rs.beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,reportDataSource);	
       		}
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }
    			
    		    	String strPath=TTKPropertiesReader.getPropertyValue("PaymentManualChequePrint");
    			    File file = new File(strPath);
		            file.mkdirs();
		            String strChequeName =fileName+".pdf";
		            JasperExportManager.exportReportToPdfFile(jasperPrint, strPath +strChequeName);
		            request.setAttribute("fileName",strPath +strChequeName);
    			JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
    			//request.getSession().setAttribute("boas",boas);
//    			String strFinBatchReport="/ReportsAction.do?mode=doGenerateReport&parameter="+strPaymentSeqID+
//    										"&fileName=generalreports/FinanceBatchReport.jrxml&reportID=FinBatChck&reportType=PDF";
    			String strFinBatchReport="/ClaimsSearchAction.do?mode=doGenerateReport&fileName=generalreports/FinanceBatchReport.jrxml&reportID=FinBatChckManual&reportType=PDF";
    			//if(!(reportDataSource.equals("null")))
				reportDataSource.getResultData().beforeFirst();
    			if(reportDataSource !=null)
    			{
    				request.setAttribute("updated","message.chequePrintedSuccessfully");
    				request.setAttribute("FinBatchReport",strFinBatchReport);
    				request.setAttribute("popup","popup");
    				request.getSession().setAttribute("boas",boas);
    				frmCheque.set("parameterValue",strPaymentSeqID);
    			}//end of if(!(reportDataSource.equals("null")))
    		}//end of else if(frmCheque.get("paymethod").equals("PMM"))
    		return mapping.findForward(strChequesPrinting);
    	}//end of try
    	catch (JRException e) {
    		//  
    		e.printStackTrace();
    		TTKException expTTK = new TTKException();
    		if(e.getCause() instanceof FileNotFoundException)
    		{
    			expTTK.setMessage("error.jrxmlfile.required");
    		}//end of if(e.getCause() instanceof FileNotFoundException)
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch (JRException e)
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
    }//end of doPrintCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to navigate to debit list screen to edit selected record.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doSelectDebitNote(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    									HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ClaimsSearchAction doClearDebitNote");
    		return (mapping.findForward(strDebitList));
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
    }//end of doSelectDebitNote(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to navigate to debit list screen to edit selected record.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doClearDebitNote(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    									HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		log.debug("Inside ClaimsSearchAction doClearDebitNote");
    		DynaActionForm frmClaims=(DynaActionForm)form;
			
			String strFloatType="";
			TableData  tableData =TTKCommon.getTableData(request);
			//create new table data object
			tableData = new TableData();
			//create the required grid table
			tableData.createTableInfo("ClaimsSearchTable",new ArrayList());
			strFloatType= frmClaims.getString("floatType");
			request.getSession().setAttribute("tableData",tableData);
			frmClaims.initialize(mapping);//reset the form data
			frmClaims.set("floatType",strFloatType);
			request.getSession().setAttribute("searchparam", null);
			return this.getForward(strClaims, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
    }//end of doClaimsCheque(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    public ActionForward doShowClaimFinanceStatusTemplate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException {
		ByteArrayOutputStream baos=null;
		OutputStream sos = null;
		FileInputStream fis= null; 
		File file = null;
		BufferedInputStream bis =null;

		try
		{
			setLinks(request);
			response.setContentType("application/txt");
			response.setHeader("Content-Disposition", "attachment;filename=PaymentTemplate.xls");

			String fileName =	TTKPropertiesReader.getPropertyValue("Invoicerptdir")+"ClaimSettlementNumber.xls";
			file = new File(fileName);
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			baos=new ByteArrayOutputStream();
			int ch;
			while ((ch = bis.read()) != -1) baos.write(ch);
			sos = response.getOutputStream();
			baos.writeTo(sos);  
			baos.flush();      
			sos.flush(); 
            return (mapping.findForward(strClaims));
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
		finally{
			try{
				if(baos!=null)baos.close();                                           
				if(sos!=null)sos.close();
				if(bis!=null)bis.close();
				if(fis!=null)fis.close();

			}
			catch(Exception exp)
			{
				return this.processExceptions(request,mapping,new TTKException(exp,strBank));
			}//                 
		}
	}
    
    public ActionForward doPaymentUploadBatchDetail(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside ClaimsSearchAction doPaymentUploadBatchDetail");
			String strNotify = null;
			String incurredCurrencyFormat=null;
			String payableCurrency=null;
			String noOfClaimSettlementNumber=null;
			StringBuffer strClaimsAmt = new StringBuffer();
			StringBuffer strConvertedClaimsAmt = new StringBuffer();
			StringBuffer strConvertedUSDAmt = new StringBuffer();
			StringBuffer strIncurredCurr = new StringBuffer();
    		String strClaimTypeDesc = "";
			TableData  tableData =TTKCommon.getTableData(request);
			setLinks(request);
			Object[] excelData=null;
			DynaActionForm frmClaims=(DynaActionForm)form;
			FormFile formFile = null;
			String appendClaimsettlementnumber=null;
			String[] summaryData	=	null;
			ArrayList<Object> summaryDataobj	=	null;
			ArrayList<Object> alPrintCheque = new ArrayList<Object>();
			FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
			formFile = (FormFile)frmClaims.get("stmFile");
			if(formFile==null||formFile.getFileSize()==0)
			{
//				strNotify="Please select the xls or xlsx file";
				strNotify="Please select the xls file";
				request.getSession().setAttribute("notify", strNotify);
			}
			else
			{
				String[] arr=formFile.getFileName().split("[.]");
				String fileType=arr[arr.length-1];
//				if(!("xls".equalsIgnoreCase(fileType)||"xlsx".equalsIgnoreCase(fileType)))
				if(!("xls".equalsIgnoreCase(fileType)))
				{
//					strNotify="File Type should be xls or xlsx";
					strNotify="File Type should be xls";
					request.getSession().setAttribute("notify", strNotify);           
				}

			}
			if(formFile.getFileSize()>(1024*1024*1024)) 
			{
				strNotify="File Length Lessthan 3GB";	        	  
				request.getSession().setAttribute("notify", strNotify);

			}
			if(strNotify!=null && strNotify.length()!=0)
			{
				return (mapping.findForward(strClaims));
			}
			else
			{
				String[] arr=formFile.getFileName().split("[.]");
				String fileType=arr[arr.length-1];
				incurredCurrencyFormat=frmClaims.getString("incuredCurencyFormat");
				String paymethod=frmClaims.getString("paymethod");
				payableCurrency=frmClaims.getString("payableCurrency");
				if(paymethod.equalsIgnoreCase("EFT") || paymethod.equalsIgnoreCase("PCA"))
				{
					try{
						excelData=this.getExcelData(request,formFile,fileType,9);
						}catch(IllegalArgumentException | IOException ioException){
							strNotify="File format is not proper, Please check the format and upload.";
							request.getSession().setAttribute("notify", strNotify);  
							return (mapping.findForward(strClaims));
						}
					if(excelData!=null){
						noOfClaimSettlementNumber=(String)excelData[0];
						//frmClaims.set("noofclaimssettlementnum",noOfClaimSettlementNumber);
						if(Integer.parseInt(noOfClaimSettlementNumber)>899)//900 records
						{
							request.getSession().setAttribute("notify", "Maximum excel upload limit exceeded, limit is 900.");
							frmClaims.initialize(mapping);
							return (mapping.findForward(strClaims));
						}
						ArrayList<ArrayList<String>> excelDataRows=(ArrayList<ArrayList<String>>)excelData[1];
						appendClaimsettlementnumber =  this.getConcatenatedClaimSettlementNumber(excelDataRows);
						alPrintCheque.add(appendClaimsettlementnumber);
						alPrintCheque.add(TTKCommon.getWebBoardId(request));
						alPrintCheque.add(incurredCurrencyFormat);
						alPrintCheque.add(paymethod);
						alPrintCheque.add(payableCurrency);
						String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
						String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
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
								return (mapping.findForward(strClaims));    				
							}//end of else
						}//end of if(!strPageID.equals("") || !strSortID.equals(""))
						ArrayList alClaimstest= floatAccountManagerObject.setPaymentUploadExcel(alPrintCheque);
						int batchNo	=	(int) alClaimstest.get(1);
						alClaimstest	=	(ArrayList) alClaimstest.get(0);
						summaryDataobj	=	floatAccountManagerObject.getSummaryPaymentUplad("P",batchNo);
						summaryData		=	(String[]) summaryDataobj.get(0);
						
						request.getSession().setAttribute("paymentUploadLog", summaryDataobj.get(1));
						if(summaryData!=null){
							frmClaims.set("totalNoOfRows", summaryData[0]);
							frmClaims.set("totalNoOfRowsPassed", summaryData[1]);
							frmClaims.set("noofclaimssettlementnum",summaryData[1]);
							frmClaims.set("totalNoOfRowsFailed", summaryData[2]);
							frmClaims.set("sussessYN", "Y");
						}

						tableData.setData(alClaimstest, "search");
						//impl for loading claim amount in the grid
						if(alClaimstest != null && alClaimstest.size() > 0 )
						{
							String strComma="";
							for(int i=0 ;i<alClaimstest.size() ;i++)
							{
								if(i!=0)
								{
									strComma=",";
								}//end of if(i!=0)
								if(((ChequeVO)alClaimstest.get(i)).getClaimAmt()!=null)
								{
									strClaimsAmt  = strClaimsAmt.append(strComma+"\""+((ChequeVO)alClaimstest.get(i)).getClaimAmt().toString()+"\"");
									strConvertedClaimsAmt=strConvertedClaimsAmt.append(strComma+"\""+((ChequeVO)alClaimstest.get(i)).getConvertedApprovedAmount().toString()+"\"");
									strConvertedUSDAmt=strConvertedUSDAmt.append(strComma+"\""+((ChequeVO)alClaimstest.get(i)).getUsdAmount().toString()+"\"");
								}//end of if(((ChequeVO)alClaims.get(i)).getClaimAmt()!=null)
								else
								{
									strClaimsAmt  = strClaimsAmt.append(strComma+"\""+"0"+"\"");
									strConvertedClaimsAmt=strConvertedClaimsAmt.append(strComma+"\""+"0"+"\"");
									strConvertedUSDAmt=strConvertedUSDAmt.append(strComma+"\""+"0"+"\"");
								}//end of else
			    				strClaimTypeDesc = "\""+((ChequeVO)alClaimstest.get(0)).getClaimTypeDesc().toString()+"\"";
			    				
			    				if(((ChequeVO)alClaimstest.get(i)).getIncuredCurencyFormat()!=null && !"".equals(((ChequeVO)alClaimstest.get(i)).getIncuredCurencyFormat())){
			    					strIncurredCurr  = strIncurredCurr.append(strComma+"\""+((ChequeVO)alClaimstest.get(i)).getIncuredCurencyFormat().toString()+"\"");
			    				}
							}//end of for
							//bk
			    			if(alClaimstest != null && alClaimstest.size() > 0 )
			        		{
			    				String reviewFlag =null;
			    				String stopClaimYN= null;
			        			for(int i=0 ;i<alClaimstest.size() ;i++)
			        			{
			        				 reviewFlag =((ChequeVO)alClaimstest.get(i)).getReviewFlag();
			        				 stopClaimYN = ((ChequeVO)alClaimstest.get(i)).getStopClaimsYN();
			        			}
			        			request.setAttribute("reviewFlag", reviewFlag);
			        			request.setAttribute("stopClaimYN", stopClaimYN);
			        		}
			    			//end bk
							if(strClaimsAmt.toString()!=null && strConvertedClaimsAmt.toString()!=null)
							{
							frmClaims.set("strClaimsAmt",strClaimsAmt.toString());
							frmClaims.set("strConvertedClaimsAmt",strConvertedClaimsAmt.toString());
							frmClaims.set("strConvertedUSDAmt",strConvertedUSDAmt.toString());
			    			frmClaims.set("strClaimTypeDesc",strClaimTypeDesc.toString());
			    			frmClaims.set("strIncurredCurr",strIncurredCurr.toString());
							}
							
							//displaying available float balance
			    			if(((ChequeVO)alClaimstest.get(0)).getAvblFloatBalance()!= null)
			    			{
			    				frmClaims.set("avblFloatBalance",((ChequeVO)alClaimstest.get(0)).getAvblFloatBalance().toString());
			    			}//end of if(((ChequeVO)alClaims.get(0)).getAvblFloatBalance()!= null)
			    			else
			    			{
			    				frmClaims.set("avblFloatBalance","0");
			    			}//end of else
					}
					
	
					}
				}
				else if(paymethod.equalsIgnoreCase("PMM"))
				{
					strNotify="Please select only EFT Advice or Cheque Advice";	        	  
					request.getSession().setAttribute("notify", strNotify);    
				}
				
			}
			return (mapping.findForward(strClaims));
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)
	}

    private String getConcatenatedClaimSettlementNumber(ArrayList<ArrayList<String>> excelDataRows) {
		// TODO Auto-generated method stub
		StringBuffer sbfConcatenatedClaimSettlementNum=new StringBuffer("|");
		Iterator<ArrayList<String>>rit=excelDataRows.iterator();
		int iCounter=0;
		while(rit.hasNext())
		{
			ArrayList<String> rlist=rit.next();
             if((rlist!=null)&& rlist.size()!=0)
			{
				if(rlist.get(0)!=null)
				{
					if(iCounter==0)
					{
						sbfConcatenatedClaimSettlementNum.append(rlist.get(0));
                    }//end of if(iCounter==0)
					else
					{
						sbfConcatenatedClaimSettlementNum.append("|").append(rlist.get(0));
					}//end of else
				} // end of if(strChOpt[iCounter]!=null)
					} // end of if((strChOpt!=null)&& strChOpt.length!=0)
			iCounter++;
		}
		sbfConcatenatedClaimSettlementNum.append("|");
		return sbfConcatenatedClaimSettlementNum.toString();
	}
    
    
    private Object[] getExcelData(HttpServletRequest request,FormFile formFile, String fileType, int column) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		InputStream fis=null;
    	HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
    	Reader reader		=	null;
    	Object object[]=new Object[2];
    	int numclaimsettlementnumber=0;
 	     ArrayList<ArrayList<String>> excelDatar=new ArrayList<>();
		FileWriter fileWriter=	null;
		HSSFWorkbook workbook = null;
		fis = formFile.getInputStream(); 
		try{
			workbook =  (HSSFWorkbook) new HSSFWorkbook(fis);
			}catch(IllegalArgumentException | IOException ioException){
				throw ioException;
			}
    	  //log("xls="+wb_hssf.getSheetName(0));
    	 sheet = workbook.getSheetAt(0);
    	 String temp	=	"";
	        DecimalFormat  formatter = new DecimalFormat("0.000");
 	        //Initializing the XML document
 	    	final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
 	        if(sheet==null)
 	        	request.getSession().setAttribute("notify", "Please upload proper File");
 	        else
 	        {
 	        	  Iterator<?> rows     = sheet.rowIterator ();
 	               ArrayList<String> excelDatac;
      	        while (rows.hasNext ()) 
      	        {
      	        HSSFRow row =  (HSSFRow) rows.next(); 

      	            if(row.getRowNum()==0)
      	            	continue;
      	          
      	            Iterator<?> cells = row.cellIterator (); 
      	            ArrayList<String> rowData = new ArrayList<String>();
      	            for(short i=0;i<=9;i++)
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
  		     	                    	rowData.add(new SimpleDateFormat("dd-MM-YYYY").format(cell.getDateCellValue()).trim());
  		     	                    }
  		     	                    else // NUMERIC CELL TYPE
  		     	                    {
  		     	                    	String strValue	=	 formatter.format(cell.getNumericCellValue ());
   		     	                 		//Convert Exponent value to Big Decimals
   										/*BigDecimal bdecimalVal	=	new BigDecimal(temp);
										temp	=	bdecimalVal.toString();
  		     	                    	rowData.add(temp);*/
  		     	                    	String values[]=strValue.split("[.]");
			                        	if(new Integer(values[1])>0) temp=strValue; 
			                        	else temp=values[0];
			                        	
			                        	rowData.add(temp);
  		     	                    }
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
  		     	                	rowData.add(blankCell.trim());
  		     	                	break;
  		     	                }
  		     	                default:
  		     	                {
  		     	                    // types other than String and Numeric.
  		     	                   // System.out.println ("Type not supported.");
  		     	                    break;
  		     	                }
  	     	                } // end switch
  	            		}//else
      	            	
      	            }//for
      	          
      	          excelDatar.add(rowData);//adding Excel data to ArrayList
      	        numclaimsettlementnumber++; 
      	        } //end while
      	     
 	        }
 	        object[0]=numclaimsettlementnumber+"";//adding no. of policies
			object[1]=excelDatar;//adding all rows data
 		     return object;
	}
    
    
    
    public ActionForward doLogDetailsExcelUploads(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException {

		log.debug("Inside ClaimsSearchAction doLogDetailsExcelUploads");
		try
		{
			setLinks(request);
			String strActiveLink=TTKCommon.getActiveLink(request);
			DynaActionForm frmClaims=(DynaActionForm)form;
			String startDate = frmClaims.getString("startDate");
			String endDate = frmClaims.getString("endDate");
			String flag=request.getParameter("Flag");
			ArrayList alData	=	null; 
			FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
			alData=floatAccountManagerObject.getLogDetailsExcelUpload(startDate,endDate,flag);
			request.getSession().setAttribute("alData", alData);
			return (mapping.findForward(struploadbatchdetaillog));
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)

	}
    
    
    public ActionForward doDownloadPaymentUploadLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException {

		log.debug("Inside ClaimsSearchAction doDownloadPaymentUploadLog");
		try
		{
			setLinks(request);
			DynaActionForm frmClaims=(DynaActionForm)form;
			request.getSession().setAttribute("alData", request.getSession().getAttribute("paymentUploadLog"));
			return (mapping.findForward(struploadbatchdetaillog));
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strBank));
		}//end of catch(Exception exp)

	}
    
	/**Returns the String of concatenated string of contact_seq_id delimeted by '|'.
	 * @param HttpServletRequest
	 * @param TableData
	 * @return String
	 */
	private String getConcatenatedSeqID(HttpServletRequest request,TableData tableData) {
		StringBuffer sbfConcatenatedSeqId=new StringBuffer("|");
		String strChOpt[]=request.getParameterValues("chkopt");
		if((strChOpt!=null)&& strChOpt.length!=0)
		{
			for(int iCounter=0;iCounter<strChOpt.length;iCounter++)
			{
				if(strChOpt[iCounter]!=null)
				{
					if(iCounter==0)
					{
						sbfConcatenatedSeqId.append(String.valueOf(((ChequeVO)tableData.getRowInfo(
																Integer.parseInt(strChOpt[iCounter]))).getSeqID()));
					}//end of if(iCounter==0)
					else
					{
						sbfConcatenatedSeqId.append("|").append(String.valueOf(((ChequeVO)tableData.getRowInfo(Integer.
																		parseInt(strChOpt[iCounter]))).getSeqID()));
					}//end of else
				} // end of if(strChOpt[iCounter]!=null)
			} //end of for(int iCounter=0;iCounter<strChOpt.length;iCounter++)
		} // end of if((strChOpt!=null)&& strChOpt.length!=0)
		sbfConcatenatedSeqId.append("|");
		return sbfConcatenatedSeqId.toString();
	} // end of getConcatenatedSeqID(HttpServletRequest request,TableData tableData)

	
	
	public ActionForward doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 DynaActionForm frmCheque=(DynaActionForm) form;
			 String strActiveLink=TTKCommon.getActiveLink(request);
			  //added for KOC-1276
			 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			 //add ended
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint = null;
			 String strPath = "";
			 String strUserID="";//added for KOC-1267
			 TTKReportDataSource ttkReportDataSource = null;
			 String strParam = "";
			 String strFinParam = "";
			//   
			 if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 {
				 if(request.getParameter("parameter")!=null)
				 strParam = request.getParameter("parameter").replaceAll("__","&");
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 else {
//				 strParam = request.getParameter("parameter");
				 strParam = frmCheque.getString("parameterValue");
			 }//end of else
			 strFinParam = request.getParameter("parameter")+"F|";
			 String jrxmlfile=request.getParameter("fileName");
			 log.debug("doGenerateReport jrxmlfile doGenerateReport>>"+jrxmlfile);
			 String strComplete = request.getParameter("completedYN");
			 if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 {
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("SupDispRpt"))
			 else if(request.getParameter("reportID").equalsIgnoreCase("76ColPend") || request.getParameter("reportID").equalsIgnoreCase("FinPenRpt"))
			 {
				 log.debug("76 column report parameter"+strFinParam);
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strFinParam);
			 }//end of else if(request.getParameter("reportID").equalsIgnoreCase("76ColPend") || request.getParameter("reportID").equalsIgnoreCase("FinPenRpt"))
			 else if(request.getParameter("reportID").equalsIgnoreCase("CitiFinDetRpt"))
			 {
				if(request.getParameter("reportType").equals("EXL"))
				 {
					 jrxmlfile = "reports/finance/CitibankClaimsDetailEXL.jrxml";
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else if(request.getParameter("reportType").equals("PDF"))
				 {
					 jrxmlfile = "reports/finance/CitibankClaimsDetail.jrxml";
				 }//end of else if(request.getParameter("reportType").equals("PDF"))
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("CitiFinDetRpt"))
			 else if(request.getParameter("reportID").equalsIgnoreCase("FurGenRpt"))
			 {
				 jrxmlfile = "reports/finance/FutureGeneraliReport.jrxml";
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }//end of if(request.getParameter("reportID").equalsIgnoreCase("FurGenRpt"))
			  //Added for KOC-1267
			 else if(request.getParameter("reportID").equalsIgnoreCase("ClaimViewHis"))
			 {
				 
				 strUserID = userSecurityProfile.getUSER_ID();
				 //strParam = strParam.concat((new StringBuffer(strUserID+"|")).toString());
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }
			 else if(request.getParameter("reportID").equalsIgnoreCase("PreAuthUtilization"))//intX
			 {
				 jrxmlfile = "reports/intX/PreAuthUtilization.jrxml";
				 strUserID = userSecurityProfile.getUSER_ID();
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"));
			 }
			 //Ended
			 else
			 {
				 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParam);
			 }//end of else
			 
			 String strPdfFile = TTKPropertiesReader.getPropertyValue("authorizationrptdir")+request.getParameter("authorizationNo")+".pdf";
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, String> hashMap = new HashMap<String, String>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
				 hashMap.put("Start Date",request.getParameter("startDate"));
				 hashMap.put("End Date",request.getParameter("endDate"));
				 if(request.getParameter("reportID").equals("GenRenMemXLs")) {
					 hashMap.put("parameter",strParam);
				 }//end of if(request.getParameter("reportID").equals("GenRenMemXLs")
				 else if(request.getParameter("reportID").equals("EnrDaiBOI")){
					 hashMap.put("Policy Number",request.getParameter("policyNumber"));					 
				 }//end of else if(request.getParameter("reportID").equals("")){
				 else if(request.getParameter("reportID").equals("SoftCopyUploadError"))
				 {
					 hashMap.put("Status", request.getParameter("status"));
				 }//end of else if(request.getParameter("reportID").equals("SoftCopyUploadError"))
				 else {
					 hashMap.put("parameter",strParam.substring(2,strParam.length()));
				 }//end of else
				 strPath = TTKPropertiesReader.getPropertyValue("SignatureImgPath");
				 hashMap.put("SigPath",strPath);	
				 
				 
				 String batchNo="";
	        		
	        		if(request.getParameter("reportID").equalsIgnoreCase("FinBatChckManual")){
	        			 ResultSet rs=ttkReportDataSource.getResultData();
	        				if(rs!=null && rs.next()){
	        					 batchNo= rs.getString("BATCH_NUMBER");
	        					 rs.beforeFirst();
	        					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);	
	    	        		}
	        				 else
		    				 {
		    					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
		    				 }
	        		}
	        		
	        		else{
	        			 if(ttkReportDataSource.getResultData().next())
	    				 {
	    					 ttkReportDataSource.getResultData().beforeFirst();
	    					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
	    				 }//end of if(ttkReportDataSource.getResultData().next()))
	        			 else
	    				 {
	    					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
	    				 }
	        			
	        		}
				 
				 
				 if(request.getParameter("reportType").equals("EXL"))//if the report type is Excel
				 {
					//   
					 JRXlsExporter jExcelApiExporter = new JRXlsExporter();
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					 jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					 jExcelApiExporter.exportReport();
				 }//end of if(request.getParameter("reportType").equals("EXL"))
				 else//if report type if PDF
				 {
					
		        	if(request.getParameter("reportID").equalsIgnoreCase("FinBatChckManual")){
		        		
		        		 String strPath1=TTKPropertiesReader.getPropertyValue("PaymentManualChequePrint");
		    			  File file = new File(strPath1);
				          file.mkdirs();
				          SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HHmmss");  
				          Date date = new Date();  
				         //String strChequeName = "FinanceBatchReport"+"_"+formatter.format(date)+".pdf";
				          String strChequeName = "BatchReport"+"_"+batchNo+".pdf";
				         JasperExportManager.exportReportToPdfFile(jasperPrint, strPath1 +strChequeName);
		        	}
		        	JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
		        		
					 if((request.getParameter("reportID").equalsIgnoreCase("AuthLetter")) && (strActiveLink.equals(strPreAuth) 
							 && strComplete.equals("Y")))
					 {
						 JasperExportManager.exportReportToPdfFile(jasperPrint,strPdfFile);
					 }//end of if((request.getParameter("reportID").equalsIgnoreCase("AuthLetter")) && (strActiveLink.equals(strPreAuth)
				 }//end of else
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strReportExp));
		 }//end of catch(Exception exp)
	 }//end of doGenerateReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	
	/**
	 * Returns the ContactManager session object for invoking methods on it.
	 * @return ContactManager session object which can be used for method invokation
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
			throw new TTKException(exp, "floataccount");
		}//end of catch
		return floatAccountManager;
	}//end getfloatAccountManagerObject().

	/**
	 * this method will add search criteria fields and values to the arraylist and will return it
	 * @param frmClaims DynaActionForm
	 * @return ArrayList contains search parameters
	 */

	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmClaims,HttpServletRequest request)
	{
		// build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(TTKCommon.getWebBoardId(request));//0
		if(frmClaims.get("floatType").equals("FTD"))
		{
			alSearchParams.add(Long.parseLong((String)frmClaims.get("debitSeqID")));//1
		}//end of if(frmClaims.get("floatType").equals("FTD"))
		else
		{
			alSearchParams.add(null);
		}//end of else
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaims.get("claimSettNo")));//2
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaims.get("enrollmentId")));//3
		//start changes for cr koc1103 and 1105
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaims.get("paymethod1")));//4
		//end changes for cr koc1103 and 1105
		alSearchParams.add(((String) frmClaims.get("dvReceivedDate")));//5
        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaims.get("corporateName")));//6
        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaims.get("claimantName")));//7
		alSearchParams.add(((String) frmClaims.get("claimType")));//8
		alSearchParams.add(((String) frmClaims.get("policyType")));//9
		alSearchParams.add(TTKCommon.replaceSingleQots((String) frmClaims.get("payee")));//10
		alSearchParams.add(TTKCommon.getUserSeqId(request));//11
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaims.get("incuredCurencyFormat")));//12
		alSearchParams.add(((String) frmClaims.get("receivedDate")));//13
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmClaims.get("sQatarId")));//14
		alSearchParams.add(((String) frmClaims.get("payableCurrency")));//15
		//Added For CR -0224 and CR-0228
		if(frmClaims.get("claimType").equals("CNH"))
		{
			alSearchParams.add(((String)frmClaims.get("sProviderNameSeq")));//16A
		}//end of if(frmClaims.get("claimType").equals("CNH"))
		else if(frmClaims.get("claimType").equals("PTN"))
		{
			alSearchParams.add(((String)frmClaims.get("sPartnerSeqId")));//16B
		}//end of else if(frmClaims.get("claimType").equals("PTN"))
		else
		{
			alSearchParams.add(null);
		}//end of else
		
		alSearchParams.add(((String) frmClaims.get("priorityClaims")));//17

		if(frmClaims.get("priorityClaims").equals("PRVA"))
		{
			alSearchParams.add(((String)frmClaims.get("rangeFrom")));//18
		}//end of if(frmClaims.get("priorityClaims").equals("PRVA"))
		else
		{
			alSearchParams.add(null);
		}//end of else
		
		if(frmClaims.get("priorityClaims").equals("PRVA"))
		{
			alSearchParams.add(((String) frmClaims.get("rangeTo")));//19
		}//end of if(frmClaims.get("priorityClaims").equals("PRVA"))
		else
		{
			alSearchParams.add(null);
		}//end of else
		return alSearchParams;
	}// end of populateSearchCriteria(DynaActionForm frmTransaction,HttpServletRequest request)
	
	
	 public ActionForward doClaimChanged(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	        try{
	            log.debug("Inside the doBackward method of doClaimChanged");
	           
	            return this.getForward(strClaims, mapping, request);   //finally return to the grid screen
	           }//end of try
	         catch(TTKException expTTK)
	        {
	          return this.processExceptions(request, mapping, expTTK);
	            }//end of catch(TTKException expTTK)
	         catch(Exception exp)
	         {
	          return this.processExceptions(request, mapping, new TTKException(exp,strBank));
	         }//end of catch(Exception exp)
	        }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	 
	 public ActionForward doPriorityChanged(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	        try{
	            log.debug("Inside the doBackward method of doPriorityChanged");
	           
	            return this.getForward(strClaims, mapping, request);   //finally return to the grid screen
	           }//end of try
	         catch(TTKException expTTK)
	        {
	          return this.processExceptions(request, mapping, expTTK);
	            }//end of catch(TTKException expTTK)
	         catch(Exception exp)
	         {
	          return this.processExceptions(request, mapping, new TTKException(exp,strBank));
	         }//end of catch(Exception exp)
	        }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

}//end of ClaimsSearchAction
