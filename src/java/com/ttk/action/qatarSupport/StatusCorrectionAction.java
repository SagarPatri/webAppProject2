package com.ttk.action.qatarSupport;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.ibm.icu.text.DecimalFormat;
import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.support.SupportManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.qatarSupport.StatusCorrectionVO;

public class StatusCorrectionAction extends TTKAction{

	private static final Logger log = Logger.getLogger( StatusCorrectionAction.class );
	private static final String strStatusCorrection="statusCorrection";
	private static final String strReportdisplay="reportdisplay";
    private static final String strAddressExp="ststusCorrection"; 	
    private static final String strBackward="Backward";
	private static final String strForward="Forward";
	private static final String struploadbatchdetaillog="uploadbatchdetail";
	
public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			   HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside StatusCorrectionAction doDefault");
DynaActionForm frmAddressLabel=(DynaActionForm)form;
frmAddressLabel.initialize(mapping);
TableData  tableData =TTKCommon.getTableData(request);
tableData = new TableData();
tableData.createTableInfo("StatusCorrectionTable",new ArrayList());
request.getSession().setAttribute("tableData",tableData);
request.getSession().setAttribute("uploadFlag", "N");
return this.getForward(strStatusCorrection, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request,mapping,expTTK);
}//end of catch(ETTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
}//end of catch
}//end of doDefault
	
public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   HttpServletResponse response) throws Exception{
	
	try{
		setLinks(request);
		log.debug("Inside StatusCorrectionAction doSearch");
		DynaActionForm frmAddressLabel=(DynaActionForm)form;
		SupportManager supportManager = this.getSupportManagerObject();
		ArrayList alCorrectionLst = null;
		TableData tableData=TTKCommon.getTableData(request);
		
		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(strPageID.equals(""))
			{
				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
				tableData.modifySearchData("sort");				
			}
			else
			{
				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
				return (mapping.findForward(strStatusCorrection));
			}
		}//end
		else
		{
			//create the required grid table
			tableData.createTableInfo("StatusCorrectionTable",null);
			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
			tableData.modifySearchData("search");
			
		}//end of else
		
		String flag = (String) request.getSession().getAttribute("uploadFlag");
		if("Y".equals(flag)){
			alCorrectionLst = new ArrayList<>();
		}
		else{
			alCorrectionLst= supportManager.getCorrectionList(tableData.getSearchData());
		}
		
		tableData.setData(alCorrectionLst, "search");
		request.getSession().setAttribute("tableData",tableData);
		frmAddressLabel.set("remarks", "");
		request.getSession().removeAttribute("uploadFlag");
		request.getSession().setAttribute("uploadFlag", "N");
		frmAddressLabel.set("sussessYN", "N");
		return this.getForward(strStatusCorrection, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request,mapping,expTTK);
	}//end of catch(ETTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
	}//end of catch
	
}//end of doSearch
	
	
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
		throw new TTKException(exp, strAddressExp);
	}//end of catch
	return supportManager;
}//end of getsupportManagerObject()


private ArrayList<Object> populateSearchCriteria(DynaActionForm frmQatarSprt,HttpServletRequest request)
{
	//build the column names along with their values to be searched
	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	alSearchParams.add((String)frmQatarSprt.get("claimSettleNumber"));
	alSearchParams.add((String)frmQatarSprt.get("paymentMethod"));
	alSearchParams.add((String)frmQatarSprt.get("claimType"));
	alSearchParams.add((String)frmQatarSprt.get("batchNo"));
	alSearchParams.add((String)frmQatarSprt.get("batchDate"));
	alSearchParams.add((String)frmQatarSprt.get("financeStatus"));
	
	//alSearchParams.add(new Long(TTKCommon.getUserSeqId(request).toString()));
	return alSearchParams;
}


public ActionForward doStatusCorrection(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   HttpServletResponse response) throws Exception{
	
	try{
		setLinks(request);
		log.debug("Inside StatusCorrectionAction doStatusCorrection");
		DynaActionForm frmAddressLabel=(DynaActionForm)form;
		ArrayList<Object> alGenerateXL = new ArrayList<Object>();
		TableData  tableData =TTKCommon.getTableData(request);
		//String strBankType = getBankType(request,(TableData)request.getSession().getAttribute("tableData"));
		SupportManager supportManager = this.getSupportManagerObject();
		String strClmSettlementNum = getConcatenatedSeqID(request,(TableData)request.getSession().getAttribute("tableData"));
		
		alGenerateXL.add(strClmSettlementNum);
		alGenerateXL.add(TTKCommon.getUserSeqId(request));
		alGenerateXL.add(frmAddressLabel.getString("remarks"));
		
		int result = supportManager.doStatusCorrection(alGenerateXL);
		
		if(result  > 0 ){
			 request.setAttribute("updated","message.savedSuccessfully"); 
		}
		
		StatusCorrectionAction statusObj = new StatusCorrectionAction();
		statusObj.doSearch(mapping, form, request, response);
		return this.getForward(strStatusCorrection, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
		return this.processExceptions(request,mapping,expTTK);
		}//end of catch(ETTKException expTTK)
		catch(Exception exp)
		{
		return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
		}//end of catch
	
}

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
				sbfConcatenatedSeqId.append(String.valueOf(((StatusCorrectionVO)tableData.getRowInfo(
														Integer.parseInt(strChOpt[iCounter]))).getClaimSettleNumber()));
					
				}//end of if(iCounter==0)
				else
				{
					sbfConcatenatedSeqId.append("|").append(String.valueOf(((StatusCorrectionVO)tableData.getRowInfo
															(Integer.parseInt(strChOpt[iCounter]))).getClaimSettleNumber()));
				}//end of else
			} // end of if(strChOpt[iCounter]!=null)
		} //end of for(int iCounter=0;iCounter<strChOpt.length;iCounter++)
	} // end of if((strChOpt!=null)&& strChOpt.length!=0)
	sbfConcatenatedSeqId.append("|");
	return sbfConcatenatedSeqId.toString();
} // end of getConcatenatedSeqID

public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside StatusCorrectionAction doForward");
TableData  tableData =TTKCommon.getTableData(request);
SupportManager supportManager = this.getSupportManagerObject();
tableData.modifySearchData(strForward);//modify the search data
ArrayList alCorrectionLst = null;
alCorrectionLst= supportManager.getCorrectionList(tableData.getSearchData());
tableData.setData(alCorrectionLst, strForward);//set the table data
request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
return this.getForward(strStatusCorrection, mapping, request);//finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
}//end of catch(Exception exp)
}

public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
setLinks(request);
log.debug("Inside StatusCorrectionAction doBackward");
TableData  tableData =TTKCommon.getTableData(request);
SupportManager supportManager = this.getSupportManagerObject();
tableData.modifySearchData(strBackward);//modify the search data
ArrayList alCorrectionLst = null;
alCorrectionLst= supportManager.getCorrectionList(tableData.getSearchData());
tableData.setData(alCorrectionLst, strBackward);//set the table data
request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
return this.getForward(strStatusCorrection, mapping, request);//finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
}//end of catch(Exception exp)
}


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
        return (mapping.findForward(strStatusCorrection));
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
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
			return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
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
		TableData  tableData =null;
		if(request.getSession().getAttribute("tableData")!=null){
			tableData=(TableData)request.getSession().getAttribute("tableData");
		}else{
			tableData=new TableData();
		}
		setLinks(request);
		Object[] excelData=null;
		DynaActionForm frmClaims=(DynaActionForm)form;
		FormFile formFile = null;
		String appendClaimsettlementnumber=null;
		String[] summaryData	=	null;
		ArrayList<Object> summaryDataobj	=	null;
		ArrayList<Object> alPrintCheque = new ArrayList<Object>();
		//FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
		SupportManager supportManager = this.getSupportManagerObject();
		//String uploadFile = (String) frmClaims.get("stmFile");
		formFile = (FormFile)frmClaims.get("stmFile");
	//	File file = new File (uploadFile);
		if(formFile==null||formFile.getFileSize()==0)
		{
//			strNotify="Please select the xls or xlsx file";
			strNotify="Please select the xls file";
			request.getSession().setAttribute("notify", strNotify);
		}
		else
		{
			String[] arr=formFile.getFileName().split("[.]");
			String fileType=arr[arr.length-1];
//			if(!("xls".equalsIgnoreCase(fileType)||"xlsx".equalsIgnoreCase(fileType)))
			if(!("xls".equalsIgnoreCase(fileType)))
			{
//				strNotify="File Type should be xls or xlsx";
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
			return (mapping.findForward(strStatusCorrection));
		}
		else
		{
			String[] arr=formFile.getFileName().split("[.]");
			String fileType=arr[arr.length-1];
			String paymethod=frmClaims.getString("paymentMethod");
			Long userSeqId = TTKCommon.getUserSeqId(request);
			String financeStatus=frmClaims.getString("financeStatus");
			if(paymethod.equalsIgnoreCase("EFT") || paymethod.equalsIgnoreCase("PCA"))
			{
				try{
					excelData=this.getExcelData(request,formFile,fileType,9);
					}catch(IllegalArgumentException | IOException ioException){
						strNotify="File format is not proper, Please check the format and upload.";
						request.getSession().setAttribute("notify", strNotify);  
						return (mapping.findForward(strStatusCorrection));
					}
				if(excelData!=null){
					noOfClaimSettlementNumber=(String)excelData[0];
					if(Integer.parseInt(noOfClaimSettlementNumber)>899)//900 records
					{
						request.getSession().setAttribute("notify", "Maximum excel upload limit exceeded, limit is 900.");
						frmClaims.initialize(mapping);
						return (mapping.findForward(strStatusCorrection));
					}
					ArrayList<ArrayList<String>> excelDataRows=(ArrayList<ArrayList<String>>)excelData[1];
					appendClaimsettlementnumber =  this.getConcatenatedClaimSettlementNumber(excelDataRows);
					
					alPrintCheque.add(appendClaimsettlementnumber);
					alPrintCheque.add(paymethod);
					alPrintCheque.add(financeStatus);
					alPrintCheque.add(userSeqId);
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
							return (mapping.findForward(strStatusCorrection));    				
						}//end of else
					}//end of if(!strPageID.equals("") || !strSortID.equals(""))
					else{
						tableData.createTableInfo("StatusCorrectionTable",null);
						tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
						tableData.modifySearchData("search");
					}
					ArrayList alClaimstest = null;
					 alClaimstest= supportManager.setRevertPaymentUploadExcel(alPrintCheque);
					
					summaryDataobj	=	supportManager.getSummaryRevertPaymentUplad(alPrintCheque);
					summaryData		=	(String[]) summaryDataobj.get(0);
					
					request.getSession().setAttribute("uploadLog", summaryDataobj.get(1));
					request.getSession().setAttribute("uploadFlag", "Y");
					if(summaryData!=null){
						frmClaims.set("totalNoOfRows", summaryData[0]);
						frmClaims.set("totalNoOfRowsPassed", summaryData[1]);
						frmClaims.set("totalNoOfRowsFailed", summaryData[2]);
						frmClaims.set("sussessYN", "Y");
					}
					
					/*if(alClaimstest != null && alClaimstest.size() > 0 )
	        		{
	    				String reviewFlag =null;
	        			for(int i=0 ;i<alClaimstest.size() ;i++)
	        			{
	        				 reviewFlag =((StatusCorrectionVO)alClaimstest.get(i)).getReviewFlag();
	        				 
	        			}
	        			System.out.println("reviewFlag==>"+reviewFlag);
	        			//request.setAttribute("reviewFlag", reviewFlag);
	        			request.getSession().setAttribute("reviewFlag",reviewFlag);
	        			request.setAttribute("CLG1", "CLG1");
	        		}*/
					tableData.setData(alClaimstest, "search");
					request.getSession().setAttribute("tableData",tableData);
				}
			}
			
		}
		return (mapping.findForward(strStatusCorrection));
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
	}//end of catch(Exception exp)
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

public ActionForward doDownloadUploadLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws TTKException {

	log.debug("Inside ClaimsSearchAction doDownloadUploadLog");
	try
	{
		setLinks(request);
		DynaActionForm frmClaims=(DynaActionForm)form;
		request.getSession().setAttribute("alData", request.getSession().getAttribute("uploadLog"));
		return (mapping.findForward(struploadbatchdetaillog));
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processExceptions(request,mapping,new TTKException(exp,strAddressExp));
	}//end of catch(Exception exp)

}


}	
	

