/**
 * @ (#) PreAuthGeneralAction.java May 10, 2006
 * Project       : TTK HealthCare Services
 * File          : PreAuthGeneralAction.java
 * Author        : Srikanth H M
 * Company       : Span Systems Corporation
 * Date Created  : May 10, 2006
 *
 * @author       : Srikanth H M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.claims;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ibm.icu.text.SimpleDateFormat;
import com.ttk.action.TTKAction;
import com.ttk.action.claims.claimUploadXml.ClaimsData;
import com.ttk.action.claims.claimUploadXml.ClaimsReSubmissionData;
import com.ttk.action.claims.claimUploadXml.ClaimsUpload;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
/**
 * This class is reused for adding pre-auth/claims in pre-auth and claims flow.
 */

public class ClaimUploadAction extends TTKAction {

	private static Logger log = Logger.getLogger( ClaimBatchGeneralAction.class );
	
	private static final String strClaimUpload="ClaimUpload";
	private static final String strClaimBatchDetails="ClaimsBatchDetails";
	
	private static final String strProviderSearch="ProviderSearch";
	
	
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
	//doClaimUpload
	public ActionForward doClaimUpload(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
              try{
               setLinks(request);
              log.debug("Inside ClaimUploadAction doClaimUpload ");
              
              DynaActionForm frmClaimUpload=(DynaActionForm)form;
              
              frmClaimUpload.initialize(mapping);
              
              
             return mapping.findForward(strClaimUpload);
          }//end of try
       catch(TTKException expTTK)
     {
      return this.processExceptions(request, mapping, expTTK);
      }//end of catch(TTKException expTTK)
     catch(Exception exp)
       {
      return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
      }//end of catch(Exception exp)
  }//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	public ActionForward doGeneral(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
           try{
            setLinks(request);
           log.debug("Inside ClaimUploadAction doGeneral ");
           DynaActionForm frmClaimUpload=(DynaActionForm)form;
           request.getSession().setAttribute("frmClaimUpload", frmClaimUpload);
           TableData providerListData = new TableData();  //create new table data object
           providerListData.createTableInfo("ProviderListTable",new ArrayList()); 
			request.getSession().setAttribute("providerListData",providerListData);//create the required grid table
           
          return mapping.findForward(strProviderSearch);
       }//end of try
    catch(TTKException expTTK)
  {
   return this.processExceptions(request, mapping, expTTK);
   }//end of catch(TTKException expTTK)
  catch(Exception exp)
    {
   return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
   }//end of catch(Exception exp)
}//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
//doBack	
	public ActionForward doBack(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
        try{
         setLinks(request);
        log.debug("Inside ClaimUploadAction doBack ");
        
        
         
       return mapping.findForward("ClaimUploadBack");
    }//end of try
 catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
 {
return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
}//end of catch(Exception exp)
}//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   
	
	//doUploadFiles
	public ActionForward doUploadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
          try{
           setLinks(request);
           log.debug("Inside ClaimUploadAction doBack ");
           HttpSession session = request.getSession();
   		DynaActionForm frmClaimUpload = (DynaActionForm) form;
   	     
   	 
   		FileWriter fileWriter=	null;
   		 FileOutputStream outputStream = null;
   		  FormFile formFile = null;
   		  String fileDir="";
   		  String fileName="";
   		String finalFileName="";
   		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
   		  String toDayDate=dateFormat.format(new Date()); //2016/11/16 12:08:43
   		      
   		  formFile = (FormFile)frmClaimUpload.get("excelFile");
   		  
   		 
   		    
   		 // fileName=toDayDate+"-"+TTKCommon.getUserSeqId(request)+"-"+formFile.getFileName();
   		    fileName=formFile.getFileName();
   		  
   		      if(formFile==null||formFile.getFileSize()<1){
   		    	  TTKException ttkExc =new TTKException();
   		    	  ttkExc.setMessage("error.file.data.empty");
   		    	  
   		    	  throw ttkExc;
   		      }
   		      
   		     String [] dots= formFile.getFileName().split("[.]");
   		     String fileExce=dots[dots.length-1];
   		      if (!"xls".equalsIgnoreCase(fileExce)){
   		    	 
   		    	  TTKException ttkExc =new TTKException();
   		    	  ttkExc.setMessage("error.file.xls.type");
   		    	  
   		    	  throw ttkExc; 
   		      }
   		    	  
   		    fileDir=TTKPropertiesReader.getPropertyValue("Claim.UploadExcelFile");
   		    	  fileDir+="UploadFiles/";
   		    	  
   		    	
   		      
   		     
   		      /*fileDir=fileDir+groupID;*/
   		      
   		     File file= new File(fileDir);
   		    
   		     if(!file.exists())file.mkdirs();
   		     
   		     String strFinalFileNameWithDate=new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+fileName;
   		     outputStream = new FileOutputStream(fileDir+"/"+strFinalFileNameWithDate);
   		     
   		      //outputStream = new FileOutputStream(fileDir+"/"+fileName);
   		      outputStream.write(formFile.getFileData());
   		      
   		      if (outputStream != null)  outputStream.close();
   		 
   		    
   		  
   		   String fileExtn 	=  GetFileExtension(formFile.toString());
   		   
   		   
   		   // read Excel Data   
   		   ArrayList<String> rowData = new ArrayList<String>();
	        
	     
	        String hospSeqId=(String)frmClaimUpload.get("providerSeqId");
	        String addedBy  = TTKCommon.getUserSeqId(request).toString();
	        String receiveDate  = (String)frmClaimUpload.get("receiveDate");
	        String receiveTime  = (String)frmClaimUpload.get("receivedTime");
	        String receiveDay  	= (String)frmClaimUpload.get("receiveDay");
	       
     	   	OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
       	  
              InputStream inputStream = formFile.getInputStream(); 
  		               HSSFWorkbook workbook =null; // (HSSFWorkbook) new HSSFWorkbook(inputStream);
  		        		HSSFSheet sheet=null;//workbook.getSheetAt(0);
  		        		 if (fileExtn.equalsIgnoreCase("xls"))
  		      	      {
  		      	    	  workbook =  (HSSFWorkbook) new HSSFWorkbook(inputStream);
  		      	    	  sheet = workbook.getSheetAt(0);
  		      	      }
  		        	    
  		        	    try {
  		        	    	final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
  		        	    	
  		        	     if(sheet==null){
  		  	        	request.setAttribute("errorMsg", "Please upload proper File");
  		  	        	return this.getForward(strClaimUpload, mapping, request);
  		  	        }
  		        	     
        	     /*
        	      * Extract Claims Data
        	      */
        	   /*ArrayList<ClaimsData>*/Object claimsData[]		=	extractExcelData(sheet);
  		    	    	  
	        	 ClaimsUpload claimsUpload	=	new ClaimsUpload();
  		   		claimsUpload.setHospSeqId(hospSeqId);
  		   		claimsUpload.setAddedBy(addedBy);
  		   		claimsUpload.setReceivedDate(new Timestamp(TTKCommon.getOracleDateWithTime(receiveDate,receiveTime,receiveDay).getTime()).toString());
  		   		
  		   		claimsUpload.setSourceType("INTL");
  		   		
  		   		ArrayList<ClaimsData> claimsAllData	=	new ArrayList<ClaimsData>();
  		   		claimsAllData	=	(ArrayList<ClaimsData>) claimsData[0];
  		   		claimsUpload.setClaimsData(claimsAllData);
  		   		claimsUpload.setCount((String)claimsData[1].toString());// No of lines  -  count
  		   		
  		   		
  		   	String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("Claim.UploadExcelFile"));
  		   	finalFileName	=	path+"ClaimUpload-"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xml";
  		   	File file1 = new File(finalFileName);
  			JAXBContext jaxbContext = JAXBContext.newInstance(ClaimsUpload.class);
  			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
  			// output pretty printed
  			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
  			jaxbMarshaller.marshal(claimsUpload, file1);
  			//jaxbMarshaller.marshal(claimsUpload, System.out);  -to show data in console
  		    	    	
	        //Calling BackEnd Procedure
    	    FileInputStream inputStream2	=	new FileInputStream(file1);
    	    
  			String batchRefNo 	=	onlineProviderManager.saveClaimXML(inputStream2,fileName,TTKCommon.getUserSeqId(request));
  		    /*onlineProviderManager.saveProviderClaims(claimsData);//Save Claims Data*/
			/*Object[] objects	=	onlineProviderManager.getClaimSubmittedDetails(batchNo[1]);//Get Claim Submitted Data
		   	 Long temp	=	0L;
		   	 if(objects[2]!=null && objects[1]!=null)
	   		temp	=	new Long(objects[1].toString())+new Long(objects[2].toString());*/
			 
		   	
		   	frmClaimUpload.set("hiddenBatchRefNo",batchRefNo);
  		 	frmClaimUpload.set("sussessYN", "Y");
  		 	
  		             request.getSession().setAttribute("frmClaimUpload", frmClaimUpload);
  		          
  		           /* ArrayList<String[]> alErrorMsg	=	new ArrayList<String[]>();
  		            alErrorMsg	=	(ArrayList<String[]>) objects[0];
  		 	               
  		                
  		                 * Write error log to File S T A R T S
  		                 
  		 	            HSSFWorkbook errWorkbook	=	new HSSFWorkbook();
  		 	            errWorkbook		=	createErrorLog(errWorkbook,alErrorMsg);
  		 	   			
  		 	   			String FILENAME = TTKPropertiesReader.getPropertyValue("ClmErrLogDir")+batchNo[2]+".xls";
  		 	   			FileOutputStream fileOut = new FileOutputStream(FILENAME);
  		 	   			errWorkbook.write(fileOut);
  		 	            fileOut.close();
  		 	   			
  		                 * Write error log to File E N D S
  		                 
  		 	               
  		 	               if(fileWriter!=null)
  		 	            	   fileWriter.close();*/ 
  		        	        }//end try
 		        	  catch(TTKException expTTK)
  			   		{
  			   			//log.info(expTTK.printStackTrace());
  			       		return this.processExceptions(request, mapping, expTTK);
  			   		}//end of catch(TTKException expTTK)
  			   		catch(Exception exp)
  			   		{
  			   			return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
  			   		}//end of catch(Exception exp)
  		       	
  		        	  
  		     
   		    request.setAttribute("successMsg", "File Uploaded Successfully");
           	        return mapping.findForward(strClaimUpload);
          }//end of try
         catch(TTKException expTTK)
          {
        	 ((DynaActionForm)request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
        	 return this.processExceptions(request, mapping, expTTK);
          }//end of catch(TTKException expTTK)
     catch(Exception exp)
    {
      ((DynaActionForm)request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
      return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
      }//end of catch(Exception exp)
    }//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	//doCreate files from uploaded files.
	public ActionForward doCreateClaimsFromUploadedFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
          try{
           setLinks(request);
           log.debug("Inside doCreateClaimsFromUploadedFile  ");
           HttpSession session = request.getSession();
   		   DynaActionForm frmClaimUpload = (DynaActionForm) form;
	        
	        String BatchRefNo=(String)frmClaimUpload.get("hiddenBatchRefNo");
	        Long userSeqId = TTKCommon.getUserSeqId(request);
	       
     	   	OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
       	   
  			String batchNo[] 	=	onlineProviderManager.uploadingClaims(BatchRefNo, userSeqId);
  		    
  			frmClaimUpload.set("batchRefNo",batchNo[0] );
  			frmClaimUpload.set("batchNo", batchNo[2] );
		   	frmClaimUpload.set("totalNoOfRows",batchNo[3] );
		   	frmClaimUpload.set("totalNoOfRowsPassed",batchNo[5] );
		   	frmClaimUpload.set("totalNoOfRowsFailed",batchNo[4] );
		   	frmClaimUpload.set("totalNoOfClaimsUploaded",batchNo[6] );
		   	frmClaimUpload.set("totalNoOfRecordsUploaded",batchNo[8] );
			frmClaimUpload.set("batchTotalAmount", batchNo[9]);
  		 	frmClaimUpload.set("sussessYN", "Y");
  		 	frmClaimUpload.set("hiddenBatchRefNo", "");
  		 	
  		    request.getSession().setAttribute("frmClaimUpload", frmClaimUpload);
   		    request.setAttribute("successMsg", "File Uploaded Successfully");
           	        return mapping.findForward(strClaimUpload);
          }//end of try
         catch(TTKException expTTK)
          {
        	 ((DynaActionForm)request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
        	 return this.processExceptions(request, mapping, expTTK);
          }//end of catch(TTKException expTTK)
     catch(Exception exp)
    {
    	 ((DynaActionForm)request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
    	 return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
      }//end of catch(Exception exp)
    }//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/*
	    * Extract Claims Data
	    */
	   
	   public Object[] extractExcelData(HSSFSheet sheet) throws TTKException{
		   
		   ArrayList<ClaimsData> claimsData	=	new ArrayList<ClaimsData>();
		   ArrayList<String> rowData = new ArrayList<String>();
		        Iterator<?> rows     = sheet.rowIterator ();
		        
		        DecimalFormat  formatter = new DecimalFormat("0.000");
		        int count	=	0;
		        
		       /* 
		        if(sheet.getRow(0)==null){
		        	TTKException ttkException = new TTKException();
		        	ttkException.setMessage("error.wrong.excel.sheet");
		        	throw ttkException;
		        	
		        }*/
		        
		        
		        
		        int noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
                  if(noOfColumns != 34){
		        	
		        	TTKException ttkException = new TTKException();
		        	ttkException.setMessage("error.wrong.formatfile");
		        	throw ttkException;
		        }else {		        
		        while (rows.hasNext ()) 
		        {
		        	HSSFRow row =  (HSSFRow) rows.next(); 
		        	
		            if(row.getRowNum()==0)
		            	continue;
		         /*  To avoid first row of Excel	
	          	*/
		            
		           count++;
		             rowData = new ArrayList<String>();

		            String temp	=	"";
		            for(short i=0;i<=33;i++)//No Of Excel Cells
		            {
		            	HSSFCell cell	=	row.getCell(i);
		            	if(cell ==null)temp="";
		            	else 
		            	switch (cell.getCellType ())
		                {
		                    case HSSFCell.CELL_TYPE_NUMERIC :
		                    {
		                        // Date CELL TYPE
		                       
		                        if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)){
		 	                    	temp	=	new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
		 	                    }
		                        else{
		                        	String strValue=formatter.format(cell.getNumericCellValue());
		                        	if(i==5 || i==6 || i==20 ||i==21 || i==22 ||i==30){
			                    		Date javaDate= HSSFDateUtil.getJavaDate(Double.parseDouble(strValue));
			                    		
			                    		if(javaDate==null){
			                    			TTKException ttkExc = new TTKException();
			                				ttkExc.setMessage("error.claim.upload.wrong.date.format");
			                				throw ttkExc;
			                    		}
			                    		temp	=	new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
			                    	}else{
			                    		String values[]=strValue.split("[.]");
			                        	if(new Integer(values[1])>0) temp=strValue; 
			                        	else temp=values[0];
			                    	}
		                        }
		                        break;
		                    }
		                    case HSSFCell.CELL_TYPE_STRING :
		                    {
		                    	temp=TTKCommon.checkNull(cell.getStringCellValue()).replaceAll("'", "");
		                        break;
		                    }
		                    case HSSFCell.CELL_TYPE_BLANK :
		                    {	
		                    	
		                    	temp="";
		                    	break;
		                    }
		                    default:
		                    {
		                    	temp="";
		                        break;
		                    }
		                } // end switch
		            	
		            	//to remove all non-UTF-8 characters.
		            	rowData.add((temp.replaceAll("[^\\x00-\\x7F]","")).trim());
		            	
		            }//for
		            ClaimsData data	=	new ClaimsData();
		            data.setSlno(rowData.get(0));
			        data.setInvNo(rowData.get(1));
			        data.setMemName(rowData.get(2));
			        data.setMemId(rowData.get(3));
			        data.setPreAprNo(rowData.get(4));
			        data.setDtOfTrtmt(rowData.get(5));
			        data.setDtDisch(rowData.get(6));
			        data.setSysOfMed(rowData.get(7));
			        data.setBenType(rowData.get(8));
			        data.setEncType(rowData.get(9));
			        data.setClinicianId(rowData.get(10));
			        data.setClinicianName(rowData.get(11));
			        data.setSymptoms(rowData.get(12));
			        data.setPrinciICdCode(rowData.get(13));
			        data.setPrinciICDesc(rowData.get(14));
			        data.setSecIcd1(rowData.get(15));
			        data.setSecIcd2(rowData.get(16));
			        data.setSecIcd3(rowData.get(17));
			        data.setSecIcd4(rowData.get(18));
			        data.setSecIcd5(rowData.get(19));
			        data.setFirstIncDt(rowData.get(20));
			        data.setFirstRepDt(rowData.get(21));
			        data.setServDt(rowData.get(22));
			        data.setActType(rowData.get(23));
			        data.setInternalCode(rowData.get(24));
			        data.setServDesc(rowData.get(25));
			        data.setCptCode(rowData.get(26));
			        data.setAmntClmnd(rowData.get(27));
			        data.setQty(rowData.get(28));
			        data.setToothNo(rowData.get(29));
			        data.setDtOfLMP(rowData.get(30));
			        data.setNatOfConep(rowData.get(31));
			        data.setObservtn(rowData.get(32));
			        data.setEvntRefNo(rowData.get(33));
			        
			        if(0 != data.hashCode())
			        claimsData.add(data);
		        } //end while
		       
	   }
		        
		        Object returnVals[]	=	new Object[2];
		        returnVals	[0]	=	claimsData;
		        returnVals	[1]	=	count;
		        return returnVals;
	   }

	   
	   /*
	    * Create Error Log
	    */
	   
	   public HSSFWorkbook createErrorLog(HSSFWorkbook errWorkbook,ArrayList<String[]> alErrorMsg){
		   HSSFSheet errSheet			=	errWorkbook.createSheet("ErrorLog"); 
				HSSFRow errRow				=	null;
				String[] tempStrArrObj			=	null;
				
			HSSFCellStyle style			    =	errWorkbook.createCellStyle();
			HSSFFont font					=	errWorkbook.createFont();
			
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short)10);;
			font.setBoldweight((short)20);
			font.setColor(HSSFColor.WHITE.index);
			style.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
			style.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setFont(font);
				
				//Creating Headings
			
				errRow	=	errSheet.createRow(0);
				
				errRow.createCell((short) 0).setCellValue("Sl No.");
				errRow.createCell((short) 1).setCellValue("INVOICE_NO."); 
				errRow.createCell((short) 2).setCellValue("MEMBER_NAME"); 
				errRow.createCell((short) 3).setCellValue("ALKOOT_ID");
				errRow.createCell((short) 4).setCellValue("PREAPPROVAL_NO");
				errRow.createCell((short) 5).setCellValue("DATE_OF_TREATMEMT");
				errRow.createCell((short) 6).setCellValue("DATE_OF_DISCHARGE");
				errRow.createCell((short) 7).setCellValue("SYSTEM_OF_MEDICINE");
				errRow.createCell((short) 8).setCellValue("BENEFIT_TYPE");
				errRow.createCell((short) 9).setCellValue("ENCOUNTER_TYPE");
				errRow.createCell((short) 10).setCellValue("CLINICIAN_ID");
				errRow.createCell((short) 11).setCellValue("CLINICIAN_NAME");
				errRow.createCell((short) 12).setCellValue("SYMPTOMS");
				errRow.createCell((short) 13).setCellValue("PRINCIPAL_ICD_CODE");
				errRow.createCell((short) 14).setCellValue("ICD_DESCRIPTION");
				errRow.createCell((short) 15).setCellValue("SECONDARY_ICD_CODE1");
				errRow.createCell((short) 16).setCellValue("SECONDARY_ICD_CODE2");
				errRow.createCell((short) 17).setCellValue("SECONDARY_ICD_CODE3");
				errRow.createCell((short) 18).setCellValue("SECONDARY_ICD_CODE4");
				errRow.createCell((short) 19).setCellValue("SECONDARY_ICD_CODE5");
				errRow.createCell((short) 20).setCellValue("FIRST_INCIDENT_DATE");
				errRow.createCell((short) 21).setCellValue("FIRST_REPORTED_DATE");
				errRow.createCell((short) 22).setCellValue("SERVICE_DATE");
				errRow.createCell((short) 23).setCellValue("ACTIVITY_TYPE");
				errRow.createCell((short) 24).setCellValue("INTERNAL_SERVICE_CODE");
				errRow.createCell((short) 25).setCellValue("SERVICE_DESCRIPTION");
				errRow.createCell((short) 26).setCellValue("CPT_CODE");
				errRow.createCell((short) 27).setCellValue("AMOUNT_CLAIMED");
				errRow.createCell((short) 28).setCellValue("QUANTITY");
				errRow.createCell((short) 29).setCellValue("TOOTH NO");
				errRow.createCell((short) 30).setCellValue("DATE_OF_LMP");
				errRow.createCell((short) 31).setCellValue("NATURE_OF_CONCEPTION");
				errRow.createCell((short) 32).setCellValue("OBSERVATION");
				errRow.createCell((short) 33).setCellValue("EVENT_REF_NO");
				errRow.createCell((short) 34).setCellValue("ERROR_MESSAGE");
				
				Iterator its	=	(Iterator) errRow.cellIterator();
				HSSFCell ccell	=	null;
				short k	=	0;
				while(its.hasNext())
				{
					ccell	=	(HSSFCell) its.next();
					ccell.setCellStyle(style);
					k++;
				}
				
				for(int j=0;j<alErrorMsg.size();j++){
				tempStrArrObj		=	alErrorMsg.get(j);
				errRow	=	errSheet.createRow(j+1);
				for(int i=0;i<tempStrArrObj.length;i++){
					errRow.createCell((short) i).setCellValue(tempStrArrObj[i]);
				}
			}
				return errWorkbook;
	   }
	
	private static String GetFileExtension(String fname2) {
	       String fileName = fname2;
			String fname = "";
			String ext = "";
			int mid = fileName.lastIndexOf(".");
			fname = fileName.substring(0, mid);
			ext = fileName.substring(mid + 1, fileName.length());
	       return ext;
	   }

	
	
	//doDownLoadFiles 
    public ActionForward doDownLoadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlineDashBoardAction doDashBoardEnrollmentRefresh");
		
		FormFile formFile = null;
System.out.println("===================ClaimUploadAction  doDownLoadFiles===================  ");
		 
		String filePath=TTKPropertiesReader.getPropertyValue("providerClmTemplate");
		String fileName="";
		ServletOutputStream sos=response.getOutputStream();
		
    
	     filePath+="claimUploadTemplate.xls";
	      fileName="claimUploadTemplate.xls";
        
		response.setContentType("application/vnd.ms-excel");

		response.addHeader("Content-Disposition","attachment; filename="+fileName);
    
		FileInputStream fis=new FileInputStream(filePath);

		                byte[] excelData=new byte[fis.available()];
		                fis.read(excelData);

		                sos.write(excelData);
		                sos.flush();
            if(fis!=null)fis.close();
		                
	         return null;   
		                
		
		}//end of try
		catch(TTKException expTTK)
			{
			return this.processOnlineExceptions(request,mapping,expTTK);
				}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request,mapping,new TTKException(exp,strClaimUpload));
		}//end of catch(Exception exp)
    }//end of doSelectCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
    /*
	    * On CLick on Error log link in claims Uploads 
	    */
	   
	   public ActionForward  doDownloadClmLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws TTKException {
		    
			 ByteArrayOutputStream baos=null;
		    OutputStream sos = null;
		    FileInputStream fis = null; 
		    BufferedInputStream bis =null;
		  try{   
				
				String strFile	=	TTKPropertiesReader.getPropertyValue("ClmErrLogDir")+request.getParameter("batchNo")+".xls";
 		    
				response.setContentType("application/vnd.ms-excel");
				response.addHeader("Content-Disposition","attachment; filename="+request.getParameter("batchNo")+".xls");

 			File f = new File(strFile);
	    		if(f.isFile() && f.exists()){
	    			
	    			fis = new FileInputStream(f);
	    		}//end of if(strFile !="")
	    		else
	    		{ 
	    			fis = new FileInputStream(TTKPropertiesReader.getPropertyValue("WebLoginDocs")+"/default/"+"Test.doc");
	    		}//end of else
		    		
 			bis = new BufferedInputStream(fis);
	    		baos=new ByteArrayOutputStream();
	    		int ch;
	    		while ((ch = bis.read()) != -1)
	    		{
	    			baos.write(ch);
	    		}//end of while ((ch = bis.read()) != -1)
	    		sos = response.getOutputStream();
	    		baos.writeTo(sos);
				      
		            }catch(Exception exp)
		            	{
		            		return this.processExceptions(request, mapping, new TTKException(exp,"strClaimUpload"));
		            	}//end of catch(Exception exp)
		          finally{
		                   try{
		                         if(baos!=null)baos.close();                                           
		                         if(sos!=null)sos.close();
		                         if(bis!=null)bis.close();
		                         if(fis!=null)fis.close();
		                         }catch(Exception exception){
		                         log.error(exception.getMessage(), exception);
		                         }                     
		                 }
		       return null;		 
		}
	   
   
    
    
    
    
    
    private OnlineProviderManager getOnlineAccessManagerObject() throws TTKException
    {
    	OnlineProviderManager onlineProviderManager = null;
        try
        {
            if(onlineProviderManager == null)
            {
                InitialContext ctx = new InitialContext();
                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
                onlineProviderManager = (OnlineProviderManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineProviderManagerBean!com.ttk.business.onlineforms.providerLogin.OnlineProviderManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "onlineproviderinfo");
        }//end of catch
        return onlineProviderManager;
    }//end of getOnlineAccessManagerObject()
    
   public Object[] extractExcelDataForPBM(HSSFSheet sheet) throws TTKException{
	
	   ArrayList<ClaimsData> claimsData	=	new ArrayList<ClaimsData>();
	   ArrayList<String> rowData = new ArrayList<String>();
	        Iterator<?> rows     = sheet.rowIterator ();
	        DecimalFormat  formatter = new DecimalFormat("0.000");
	        int count	=	0;
	        
	        int noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
	        if(noOfColumns != 26){
	        	
	        	TTKException ttkException = new TTKException();
	        	ttkException.setMessage("error.wrong.formatfile");
	        	throw ttkException;
	        }else{
	        
	        
	        while (rows.hasNext ()) 
	        {
	        	HSSFRow row =  (HSSFRow) rows.next(); 
	        	
	            if(row.getRowNum()==0)
	            	continue;
	         /*  To avoid first row of Excel	
          	*/
	            
	           count++;
	             rowData = new ArrayList<String>();

	            String temp	=	"";
	            for(short i=0;i<=25;i++)//No Of Excel Cells
	            {
	            	HSSFCell cell	=	row.getCell(i);
	            	if(cell ==null)temp="";
	            	else 
	            	switch (cell.getCellType ())
	                {
	                    case HSSFCell.CELL_TYPE_NUMERIC :
	                    {
	                        // Date CELL TYPE
	                       
	                        if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)){
	 	                    	temp	=	new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
	 	                    }
	                        else{
	                        	String strValue=formatter.format(cell.getNumericCellValue ());
	                        	if(i==5){
		                    		Date javaDate= HSSFDateUtil.getJavaDate(Double.parseDouble(strValue));
		                    		temp	=	new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
		                    	}else{
		                    		String values[]=strValue.split("[.]");
		                        	if(new Integer(values[1])>0) temp=strValue; 
		                        	else temp=values[0];
		                    	}
	                        }
	                        break;
	                    }
	                    case HSSFCell.CELL_TYPE_STRING :
	                    {
	                    	temp=TTKCommon.checkNull(cell.getStringCellValue()).replaceAll("'", "");
	                        break;
	                    }
	                    case HSSFCell.CELL_TYPE_BLANK :
	                    {	
	                    	
	                    	temp="";
	                    	break;
	                    }
	                    default:
	                    {
	                    	temp="";
	                        break;
	                    }
	                } // end switch
	            	
	            	//to remove all non-UTF-8 characters.
	            	rowData.add((temp.replaceAll("[^\\x00-\\x7F]","")).trim());
	            	
	            }//for
	            ClaimsData data	=	new ClaimsData();
	            data.setSlno(rowData.get(0));
		        data.setInvNo(rowData.get(1));
		        data.setMemName(rowData.get(2));
		        data.setMemId(rowData.get(3));
		        data.setPreAprNo(rowData.get(4));
		        data.setDateOfPrescription(rowData.get(5));
		        data.setSysOfMed(rowData.get(6));
		        /*data.setBenType(rowData.get(7));
		        data.setEncType(rowData.get(8));*/
		        data.setClinicianId(rowData.get(7));
		        data.setClinicianName(rowData.get(8));
		        data.setPrinciICdCode(rowData.get(9));
		        data.setPrinciICDesc(rowData.get(10));
		        data.setSecIcd1(rowData.get(11));
		        data.setSecIcd2(rowData.get(12));
		        data.setSecIcd3(rowData.get(13));
		        data.setSecIcd4(rowData.get(14));
		        data.setSecIcd5(rowData.get(15));
		        data.setActType(rowData.get(16));
		        data.setInternalCode(rowData.get(17));
		        data.setDrugdescription(rowData.get(18));
		        data.setMophCode(rowData.get(19));
		        
		        if(rowData.get(20).equals("") ||rowData.get(20).equals(null) ){
		        	data.setAmntClmnd(rowData.get(20));
		        }else{
		        Double clmAmount = Double.valueOf(rowData.get(20));
		        Double clmAmountroundOff = Math.round(clmAmount * 100.0) / 100.0;
		        String clmAmountRoundOffValue = String.valueOf(clmAmountroundOff);
		        data.setAmntClmnd(clmAmountRoundOffValue);
		        }
		        
		        if(rowData.get(21).equals("") ||rowData.get(21).equals(null) ){
		        	data.setQty(rowData.get(21));	
		        }else{
		        Double quantity = Double.valueOf(rowData.get(21));
		        Double quantityroundOff = Math.round(quantity * 100.0) / 100.0;
		        String quantityRoundOffValue = String.valueOf(quantityroundOff);
		        
		        data.setQty(quantityRoundOffValue);
		        }
		        
		        
		        data.setUnitType(rowData.get(22));
		        data.setDurationOfMedicine(rowData.get(23));
		        data.setEvntRefNo(rowData.get(24));
		         data.setObservtn(rowData.get(25));
		        
		        
		        if(0 != data.hashCode())
		        claimsData.add(data);
	        } //end while
	     }
	       
	        
	        Object returnVals[]	=	new Object[2];
	        returnVals	[0]	=	claimsData;
	        returnVals	[1]	=	count;
	        return returnVals;
   
   }
	

   public ActionForward doUploadReSubmissionFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			setLinks(request);
			log.debug("Inside ClaimUploadAction doBack ");
			HttpSession session = request.getSession();
			DynaActionForm frmClaimUpload = (DynaActionForm) form;
			FileWriter fileWriter = null;
			FileOutputStream outputStream = null;
			FormFile formFile = null;
			String fileDir = "";
			String fileName = "";
			String finalFileName = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String toDayDate = dateFormat.format(new Date()); 														
			formFile = (FormFile) frmClaimUpload.get("excelFile");
			fileName = formFile.getFileName();
			
			if (formFile == null || formFile.getFileSize() < 1) {
				TTKException ttkExc = new TTKException();
				ttkExc.setMessage("error.file.data.empty");
				throw ttkExc;
			}

			String[] dots = formFile.getFileName().split("[.]");
			String fileExce = dots[dots.length - 1];
			if (!"xls".equalsIgnoreCase(fileExce)) {
				TTKException ttkExc = new TTKException();
				ttkExc.setMessage("error.file.xls.type");
				throw ttkExc;
			}

			fileDir = TTKPropertiesReader.getPropertyValue("Claim.UploadExcelFile");
			fileDir += "ReSubmission/";
			File file = new File(fileDir);
			
			if (!file.exists())
				file.mkdirs();

  		     String strFinalFileNameWithDate=new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+fileName;
  		     outputStream = new FileOutputStream(fileDir+"/"+strFinalFileNameWithDate);
			
			//outputStream = new FileOutputStream(fileDir + "/" + fileName);
			outputStream.write(formFile.getFileData());

			if (outputStream != null)
				outputStream.close();

			String fileExtn = GetFileExtension(formFile.toString());

			// read Excel Data
			ArrayList<String> rowData = new ArrayList<String>();
			String hospSeqId = (String) frmClaimUpload.get("providerSeqId");
			String addedBy = TTKCommon.getUserSeqId(request).toString();
			String receiveDate = (String) frmClaimUpload.get("receiveDate");
			String receiveTime = (String) frmClaimUpload.get("receivedTime");
			String receiveDay = (String) frmClaimUpload.get("receiveDay");
			String claimsSubmissionTypes = (String) frmClaimUpload.get("claimsSubmissionTypes");

			OnlineProviderManager onlineProviderManager = this.getOnlineAccessManagerObject();
			InputStream inputStream = formFile.getInputStream();
			HSSFWorkbook workbook = null; 						
			HSSFSheet sheet = null;
			if (fileExtn.equalsIgnoreCase("xls")) {
				workbook = (HSSFWorkbook) new HSSFWorkbook(inputStream);
				sheet = workbook.getSheetAt(0);
			}

			try {
				final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");

				if (sheet == null) {
					request.setAttribute("errorMsg","Please upload proper File");
					return this.getForward(strClaimUpload, mapping, request);
				}
				 // Extract Claims Data	
				Object claimsData[] = extractExcelDataForReSubmission(sheet);
				ClaimsUpload claimsUpload = new ClaimsUpload();
				claimsUpload.setHospSeqId(hospSeqId);
				claimsUpload.setAddedBy(addedBy);
				claimsUpload.setReceivedDate(new Timestamp(TTKCommon.getOracleDateWithTime(receiveDate, receiveTime,receiveDay).getTime()).toString());
				claimsUpload.setSourceType("INTL");
				ArrayList<ClaimsReSubmissionData> claimsAllData = new ArrayList<ClaimsReSubmissionData>();
				claimsAllData = (ArrayList<ClaimsReSubmissionData>) claimsData[0];
				claimsUpload.setClaimsReSubmissionData(claimsAllData);
				claimsUpload.setCount((String) claimsData[1].toString());// No of lines-count
				String path = TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("Claim.UploadExcelFile"));
				finalFileName = path+ "ClaimReSubmissionUpload-"+ new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date()) + ".xml";
				File file1 = new File(finalFileName);
				JAXBContext jaxbContext = JAXBContext.newInstance(ClaimsUpload.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
				jaxbMarshaller.marshal(claimsUpload, file1);
				// jaxbMarshaller.marshal(claimsUpload, System.out); -to show
				// data in console

				// Calling BackEnd Procedure
				FileInputStream inputStream2 = new FileInputStream(file1);
				String batchRefNo = onlineProviderManager.saveReClaimXML(inputStream2, fileName,TTKCommon.getUserSeqId(request),claimsSubmissionTypes);
				frmClaimUpload.set("hiddenBatchRefNo", batchRefNo);
				frmClaimUpload.set("sussessYN", "Y");
				request.getSession().setAttribute("frmClaimUpload",frmClaimUpload);
			}// end try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping,new TTKException(exp, strClaimUpload));
			}// end of catch(Exception exp)
			request.setAttribute("successMsg", "File Uploaded Successfully");
			return mapping.findForward(strClaimUpload);
		}// end of try
		catch (TTKException expTTK) {
			((DynaActionForm) request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			((DynaActionForm) request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimUpload));
		}// end of catch(Exception exp)
	}//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   
	public Object[] extractExcelDataForReSubmission(HSSFSheet sheet)throws TTKException {
		System.out.println("==============Inside extractExcelDataForReSubmission========================= ");
		ArrayList<ClaimsReSubmissionData> claimsReSubmissionData = new ArrayList<ClaimsReSubmissionData>();
		ArrayList<String> rowData = new ArrayList<String>();
		Iterator<?> rows = sheet.rowIterator();
		DecimalFormat formatter = new DecimalFormat("0.000");
		int count = 0;
		
		int noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();	
		if (noOfColumns != 15) {
			TTKException ttkException = new TTKException();
			ttkException.setMessage("error.wrong.formatfile");
			throw ttkException;
		} else {
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				if (row.getRowNum() == 0)
					continue;
				/*
				 * To avoid first row of Excel
				 */
				count++;
				rowData = new ArrayList<String>();
				String temp = "";
				for (short i = 0; i <= 14; i++)// No Of Excel Cells
				{
					HSSFCell cell = row.getCell(i);
					if (cell == null)
						temp = "";
					else
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_NUMERIC: {
							// Date CELL TYPE
							
							if (HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)) {
								temp = new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
							} else {
								String strValue = formatter.format(cell.getNumericCellValue());
								if (i == 5) {
									Date javaDate = HSSFDateUtil.getJavaDate(Double.parseDouble(strValue));
									if(javaDate==null){
		                    			TTKException ttkExc = new TTKException();
		                				ttkExc.setMessage("error.claim.upload.wrong.date.format");
		                				throw ttkExc;
		                    		}
									temp = new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
								} else {
									String values[] = strValue.split("[.]");
									if (new Integer(values[1]) > 0)
										temp = strValue;
									else
										temp = values[0];
								}
							}
							break;
						}
						case HSSFCell.CELL_TYPE_STRING: {
							temp = TTKCommon.checkNull(cell.getStringCellValue()).replaceAll("'","");
							break;
						}
						case HSSFCell.CELL_TYPE_BLANK: {
							temp = "";
							break;
						}
						default: {
							temp = "";
							break;
						}
						} // end switch
					// to remove all non-UTF-8 characters.
					rowData.add((temp.replaceAll("[^\\x00-\\x7F]", "")).trim());
				}// for
				ClaimsReSubmissionData data = new ClaimsReSubmissionData();
				data.setSlno(rowData.get(0));
				data.setMemName(rowData.get(1));
				data.setMemId(rowData.get(2));
				data.setParentClaimNumber(rowData.get(3));
				data.setParentClaimSettelmentNumber(rowData.get(4));
				data.setServDt(rowData.get(5));
				data.setActType(rowData.get(6));
				data.setInternalCode(rowData.get(7));
				data.setServDesc(rowData.get(8));
				data.setCptCode(rowData.get(9));
				data.setResubmissionRequestedAmount(rowData.get(10));
				data.setQty(rowData.get(11));
				data.setToothNo(rowData.get(12));
				data.setAlKootRemarks(rowData.get(13));
				data.setResubmissionJustification(rowData.get(14));

				if (0 != data.hashCode())
					claimsReSubmissionData.add(data);
			} // end while
		}
		Object returnVals[] = new Object[2];
		returnVals[0] = claimsReSubmissionData;
		returnVals[1] = count;
		return returnVals;
	}

	
	public ActionForward doCreateClaimsReSubFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
       try{
     	  System.out.println("==================Inside doCreateClaimsReSubFile================");
        setLinks(request);
        log.debug("Inside doCreateClaimsFromUploadedFile  ");
        HttpSession session = request.getSession();
		   DynaActionForm frmClaimUpload = (DynaActionForm) form;
	        
	        String BatchRefNo=(String)frmClaimUpload.get("hiddenBatchRefNo");
	        Long userSeqId = TTKCommon.getUserSeqId(request);
	       
	        OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
    	   
			String batchNo[] 	=	onlineProviderManager.uploadingClaimsRe(BatchRefNo, userSeqId);
		    
			frmClaimUpload.set("batchRefNo",batchNo[0] );
			frmClaimUpload.set("batchNo", batchNo[2] );
		   	frmClaimUpload.set("totalNoOfRows",batchNo[3] );
			frmClaimUpload.set("totalNoOfRowsFailed",batchNo[4] );
		   	frmClaimUpload.set("totalNoOfRowsPassed",batchNo[5] );	   
		   	frmClaimUpload.set("totalNoOfClaimsUploaded",batchNo[6] );
		   	frmClaimUpload.set("totalNoOfRecordsUploaded","" );
			frmClaimUpload.set("batchTotalAmount", batchNo[7]);
			frmClaimUpload.set("totalNoOfAutoRejectedClaims", batchNo[8]);
			
		 	frmClaimUpload.set("sussessYN", "Y");
		 	frmClaimUpload.set("hiddenBatchRefNo", "");
		 	
		    request.getSession().setAttribute("frmClaimUpload", frmClaimUpload);
		    request.setAttribute("successMsg", "File Uploaded Successfully");
        	        return mapping.findForward(strClaimUpload);
       }//end of try
      catch(TTKException expTTK)
       {
     	 ((DynaActionForm)request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
     	 return this.processExceptions(request, mapping, expTTK);
       }//end of catch(TTKException expTTK)
  catch(Exception exp)
 {
 	 ((DynaActionForm)request.getSession().getAttribute("frmClaimUpload")).set("hiddenBatchRefNo", "");
 	 return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
   }//end of catch(Exception exp)
 }//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	 public ActionForward doDownLoadReSubFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			try{
				System.out.println("===============Inside ClaimUploadAction doDownLoadReSubFiles==================");
			setOnlineLinks(request);
			log.debug("Inside ClaimUploadAction doDownLoadReSubFiles");
			FormFile formFile = null; 
			String filePath=TTKPropertiesReader.getPropertyValue("providerClmTemplate");
			String fileName="";
			ServletOutputStream sos=response.getOutputStream();
		   // filePath+="claimResubmissionUploadTemplate.xls";
		   // fileName="claimResubmissionUploadTemplate.xls";  
			 filePath+="ReSubmissionClaimUploadTemplate.xls";
		    fileName="ReSubmissionClaimUploadTemplate.xls";  
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition","attachment; filename="+fileName);    
			FileInputStream fis=new FileInputStream(filePath);
			                byte[] excelData=new byte[fis.available()];
			                fis.read(excelData);
			                sos.write(excelData);
			                sos.flush();
	            if(fis!=null)fis.close();	                
		         return null;   	
			}//end of try
			catch(TTKException expTTK)
				{
				return this.processOnlineExceptions(request,mapping,expTTK);
					}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processOnlineExceptions(request,mapping,new TTKException(exp,strClaimUpload));
			}//end of catch(Exception exp)
	    }//end of doSelectCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,

	

}//end of ClaimBatchGeneralAction
