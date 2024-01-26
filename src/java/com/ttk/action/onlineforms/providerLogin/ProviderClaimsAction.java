
package com.ttk.action.onlineforms.providerLogin; 
/**
 * @ (#) ProviderClaimsAction.java 17th Nov 2017
 * Project      : TTK HealthCare Services
 * File         : ProviderClaimsAction.java
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 17th Nov 2017
 *
 * @author       :  Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

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

import com.ibm.icu.math.BigDecimal;
import com.ttk.action.TTKAction;
import com.ttk.action.claims.ClaimUploadAction;
import com.ttk.action.claims.claimUploadXml.ClaimsData;
import com.ttk.action.claims.claimUploadXml.ClaimsUpload;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.OnlinePreAuthManager;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.onlineforms.providerLogin.ClaimDetailVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.DrugDetailsVO;
import com.ttk.dto.preauth.ShortfallVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for Searching the List Policies to see the Online Account Info.
 * This also provides deletion and updation of products.
 */
public class ProviderClaimsAction extends TTKAction {

    private static final Logger log = Logger.getLogger( ProviderClaimsAction.class );


    private static final String strClaimsUpload="claimsUpload";
    private static final String strClaimsUploadError="claimsUpload";
    private static final String strClaimsSearch="claimsSearch";
    private static final String strViewClaimDetails="viewClaimDetails";
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    private static final String strViewShortfallDetails="viewShortfallDetails";

    
/*
 *     (non-Javadoc)
 * @see com.ttk.action.TTKAction#doDefault(org.apache.struts.action.ActionMapping, 
 * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * 
 * Do Default method to show claims uploads screen
 * 
 */
   public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doDefault method of ProviderClaimsAction");
			setOnlineLinks(request);
			
			DynaActionForm frmOnlineClaimLog =(DynaActionForm)form;
			frmOnlineClaimLog.initialize(mapping);     
			
			return this.getForward(strClaimsUpload, mapping, request);
		}
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
		}
   }

   
   
   /*
    * Claims Upload Thru Excel
    */
   
   public ActionForward doUploadClaims(ActionMapping mapping,ActionForm form,HttpServletRequest request,
   		HttpServletResponse response) throws Exception{

       	try{
       		log.info("Inside the doUploadClaims method of ProviderClaimsAction");
       		setOnlineLinks(request);        		
   			DynaActionForm frmOnlineClaimUpload=(DynaActionForm)form;
   			
   			//Declaring the Variables
   	   	    String finalFileName	=	"";
      		FileOutputStream outputStream = null;
    		FormFile formFile = null;
     		String fileDir="";
       		String fileName="";
   			formFile = (FormFile)frmOnlineClaimUpload.get("file");
   		    fileName=formFile.getFileName();

   		    //Checking Empty file
   			if(formFile==null||formFile.getFileSize()<1){
 		    	  TTKException ttkExc =new TTKException();
 		    	  ttkExc.setMessage("error.file.data.empty");
 		    	  throw ttkExc;
 		      }
   			
   			//Checking Extension of file
       		String fileExtn 	=  GetFileExtension(formFile.toString());
       		if (!"xls".equalsIgnoreCase(fileExtn)){
		    	  TTKException ttkExc =new TTKException();
		    	  ttkExc.setMessage("error.file.xls.type");  
		    	  throw ttkExc; 
		      }
       		
       		//Storing The Original File
   		    fileDir=TTKPropertiesReader.getPropertyValue("providerClmUpload");
		    fileDir+="OriginalFile/";
		    File file= new File(fileDir);
		    if(!file.exists())file.mkdirs();
  		     String strFinalFileNameWithDate=new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+fileName;
  		     outputStream = new FileOutputStream(fileDir+"/"+strFinalFileNameWithDate);
		    //outputStream = new FileOutputStream(fileDir+"/"+fileName);
  		     outputStream.write(formFile.getFileData());
  		     if (outputStream != null)  outputStream.close();

		    //Collecting Input Parameter (hospSeqId and addedBys)
	        ArrayList<String> rowData = new ArrayList<String>();
	        String hospSeqId  = String.valueOf(((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getHospSeqId());
	        String addedBy  = TTKCommon.getUserSeqId(request).toString();
      	   	OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
        	  
       		 //extract data from excel and create xml
       		InputStream inputStream = formFile.getInputStream(); 
	       	HSSFWorkbook workbook = null;
	       	HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
     	      if (fileExtn.equalsIgnoreCase("xls"))
     	      {
     	    	  workbook =  (HSSFWorkbook) new HSSFWorkbook(inputStream);
     	    	  sheet = workbook.getSheetAt(0);
     	      }
       	    
       	    try {
       	    	final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
       	    	if(sheet==null){
       	    		request.setAttribute("clinicianUploadStatus", "Please upload proper File");
       	    		return this.getForward(strClaimsUpload, mapping, request);
       	    	}
       	     
       	    	 // Extract Claims Data
       	    	ClaimUploadAction claimUploadAction	=	new ClaimUploadAction();
       	    	Object claimsData[]		=	claimUploadAction.extractExcelData(sheet);//extractExcelData(sheet,batchNos,hospSeqId,addedBy);
       	    	ClaimsUpload claimsUpload	=	new ClaimsUpload();
       	    	claimsUpload.setHospSeqId(hospSeqId);
       	    	claimsUpload.setAddedBy(addedBy);
       	    	claimsUpload.setSourceType("PLCL");
       	    	claimsUpload.setClaimsData((ArrayList)claimsData[0]);
       	    	claimsUpload.setCount((String)claimsData[1].toString());// No of lines  -  count

       	    	//Save The XML FIle Before Sending To Database
      		   	String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("providerClmUpload"));
      		   	finalFileName	=	path+"ClaimUpload-"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+".xml";
       	    	//finalFileName=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("providerClmUpload"))+formFile.toString();
  		   	
       	    	File file1 = new File(finalFileName);
       	    	JAXBContext jaxbContext = JAXBContext.newInstance(ClaimsUpload.class);
       	    	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
       	    	
       	    	// output pretty printed
       	    	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
       	    	jaxbMarshaller.marshal(claimsUpload, file1);
       	    	
       	        //Calling BackEnd Procedure
       	    	FileInputStream inputStream2	=	new FileInputStream(file1); 
       	    	String batchRefNo 	=	onlineProviderManager.saveClaimXML(inputStream2,formFile.toString(),TTKCommon.getUserSeqId(request));
       	    	//String onlineProviderManager.saveProviderClaims(claimsData);//Save Claims Data
       	   
       	    	frmOnlineClaimUpload.set("sussessYN", "Y");
       	    	frmOnlineClaimUpload.set("totalNoOfRowsFailed","0");
       	    	frmOnlineClaimUpload.set("hiddenBatchRefNo",batchRefNo);
       	    	frmOnlineClaimUpload.set("file", formFile);
	       	
       	    	request.getSession().setAttribute("frmOnlineClaimUpload", frmOnlineClaimUpload);
       	    	}//end try
       	    
	       	catch(TTKException expTTK)
	   		{
	   			//log.info(expTTK.printStackTrace());
	       		return this.processExceptions(request, mapping, expTTK);
	   		}//end of catch(TTKException expTTK)
	   		catch(Exception exp)
	   		{
	   			return this.processExceptions(request, mapping, new TTKException(exp,strClaimsUploadError));
	   		}//end of catch(Exception exp)
       	}// end of doUploadClaims(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
       	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
		}
       	return this.getForward(strClaimsUpload, mapping, request);
   }
   
	     
 //doCreate files from uploaded files.
 	public ActionForward doCreateClaimsFromUploadedFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
 	           HttpServletResponse response) throws Exception{
           try{
            setLinks(request);
            log.debug("Inside doCreateClaimsFromUploadedFile  ");
            HttpSession session = request.getSession();
    		   DynaActionForm frmOnlineClaimUpload = (DynaActionForm) form;
 	        
 	        String BatchRefNo=(String)frmOnlineClaimUpload.get("hiddenBatchRefNo");
 	        Long userSeqId = TTKCommon.getUserSeqId(request);
  	       
      	   	OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
        	   
   			String batchNo[] 	=	onlineProviderManager.uploadingClaims(BatchRefNo, userSeqId);
   		    
   			frmOnlineClaimUpload.set("batchRefNo",batchNo[0] );
   			frmOnlineClaimUpload.set("batchNo", batchNo[2] );
   			frmOnlineClaimUpload.set("totalNoOfRows",batchNo[3] );
   			frmOnlineClaimUpload.set("totalNoOfRowsPassed",batchNo[5] );
   			frmOnlineClaimUpload.set("totalNoOfRowsFailed",batchNo[4] );
   			frmOnlineClaimUpload.set("totalNoOfClaimsUploaded",batchNo[6] );
   			frmOnlineClaimUpload.set("totalNoOfRecordsUploaded",batchNo[8] );
   			frmOnlineClaimUpload.set("batchTotalAmount", batchNo[9]);
   			frmOnlineClaimUpload.set("sussessYN", "Y");
   			frmOnlineClaimUpload.set("hiddenBatchRefNo", "");
   		 	
   		    request.getSession().setAttribute("frmOnlineClaimUpload", frmOnlineClaimUpload);
    		    request.setAttribute("successMsg", "File Uploaded Successfully");
            	        return mapping.findForward(strClaimsUpload);
           }//end of try
          catch(TTKException expTTK)
           {
          	 ((DynaActionForm)request.getSession().getAttribute("frmOnlineClaimUpload")).set("hiddenBatchRefNo", "");
          	 return this.processExceptions(request, mapping, expTTK);
           }//end of catch(TTKException expTTK)
      catch(Exception exp)
     {
      	 ((DynaActionForm)request.getSession().getAttribute("frmOnlineClaimUpload")).set("hiddenBatchRefNo", "");
      	 return this.processExceptions(request, mapping, new TTKException(exp,strClaimsUpload));
       }//end of catch(Exception exp)
     }//end of addClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   
	   
	   private boolean validateCellVal(String validateData){
		   boolean bool	=	true;;
		   
		   if(validateData.equals("")  || validateData.equals(" ") || validateData==null)
			   bool	=	false;
		   
		   return bool;
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
          
	
	/*
	 * Provider Login Claims Log Search screen
	 */

	   public ActionForward doClaimsLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		           HttpServletResponse response) throws Exception{
		try{
				log.debug("Inside the doClaimsLog method of ProviderClaimsAction");
				setOnlineLinks(request);
				
				DynaActionForm frmOnlineClaimLog =(DynaActionForm)form;
				frmOnlineClaimLog.initialize(mapping);     
				
				TableData tableData=TTKCommon.getTableData(request);
				tableData.createTableInfo("ProviderClaimSearchTable",null);//ClaimSearchTable
				request.getSession().setAttribute("tableData",tableData);
				
				return this.getForward(strClaimsSearch, mapping, request);
			}
			catch(TTKException expTTK)
			{
				return this.processOnlineExceptions(request, mapping, expTTK);
			}
				catch(Exception exp)
			{
					return this.processOnlineExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
			}
	   }
	   
	   
	   /**
	    * 
	    * @param mapping
	    * @param form
	    * @param request
	    * @param response
	    * @return
	    * @throws Exception
	    */
	   public ActionForward doSearchClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		           HttpServletResponse response) throws Exception{
			try{
				
				log.debug("Inside the doSearchClaim method of ProviderClaimsAction");
				setOnlineLinks(request);
				
				DynaActionForm frmOnlineClaimLog =(DynaActionForm)form;
				ArrayList<Object> alOnlineClaimsList = null;
				TableData tableData=null;
				String strPageID =""; 
				String strSortID ="";	
				
				OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
				
				tableData= TTKCommon.getTableData(request);
			
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);
			}
			strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
					if(!strPageID.equals(""))
				{
						tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
						return (mapping.findForward("claimsSearch"));
				}
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");
				}
			}
			else
			{
				tableData.createTableInfo("ProviderClaimSearchTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));						
				tableData.modifySearchData("search");
				
			}
			alOnlineClaimsList= onlineProviderManager.getClaimSearchList(tableData.getSearchData());
			tableData.setData(alOnlineClaimsList, "search");
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strClaimsSearch, mapping, request);
			}//end of try
		catch(TTKException expTTK)
		{
		return this.processOnlineExceptions(request, mapping, expTTK);
		}
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
		}//end of catch(Exception exp)
	}//end of doSearchClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	   
	   
	   /*
	    *on Forward of search Claims 
	    */
	   public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   		HttpServletResponse response) throws Exception{
	   	try{
	   		setOnlineLinks(request);
	   		log.debug("Inside the doForward method of ProviderClaimsAction");
				TableData tableData = (TableData) request.getSession().getAttribute("tableData");
				OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
	           
	   		tableData.modifySearchData(strForward);//modify the search data
	   		//fetch the data from the data access layer and set the data to table object
	   		ArrayList<Object> claimLogSearch = onlineProviderManager.getClaimSearchList(tableData.getSearchData());
	   		tableData.setData(claimLogSearch, strForward);//set the table data
	   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
	   		return this.getForward(strClaimsSearch, mapping, request);
	   	}//end of try
	   	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
			}//end of catch(Exception exp)
	   }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	   
	   
	   /*
	    *on Backward of search Claims 
	    */
	   public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   		HttpServletResponse response) throws Exception{
	   	try{
	   		setOnlineLinks(request);
	   		log.debug("Inside the doBackward method of ProviderClaimsAction");
				TableData tableData = (TableData) request.getSession().getAttribute("tableData");
				OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
	           
	   		tableData.modifySearchData(strBackward);//modify the search data
	   		//fetch the data from the data access layer and set the data to table object
	   		ArrayList<Object> claimLogSearch = onlineProviderManager.getClaimSearchList(tableData.getSearchData());
	   		tableData.setData(claimLogSearch, strBackward);//set the table data
	   		request.getSession().setAttribute("tableData",tableData);//set the table data object to session
	   		return this.getForward(strClaimsSearch, mapping, request);
	   	}//end of try
	   	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
			}//end of catch(Exception exp)
	   }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	   
	   //doStatusChange
	  public ActionForward doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	        try{
	            log.debug("Inside the doBackward method of doStatusChange");
	           
	            return this.getForward(strClaimsSearch, mapping, request);   //finally return to the grid screen
	           }//end of try
	         catch(TTKException expTTK)
	        {
	          return this.processExceptions(request, mapping, expTTK);
	            }//end of catch(TTKException expTTK)
	         catch(Exception exp)
	         {
	          return this.processExceptions(request, mapping, new TTKException(exp,strClaimsUploadError));
	         }//end of catch(Exception exp)
	        }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	   
	   
	   /**on Click on Status link showing the Claim details 
	    * 
	   * @param mapping The ActionMapping used to select this instance
	     * @param form The optional ActionForm bean for this request (if any)
	     * @param request The HTTP request we are processing
	     * @param response The HTTP response we are creating
	     * @return ActionForward Where the control will be forwarded, after this request is processed
	     * @throws Exception if any error occurs
	    */
	   public ActionForward doViewClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	   		HttpServletResponse response) throws Exception{
	   	try{
	   		setOnlineLinks(request);
	   		DynaActionForm frmOnlineClaimsDetails = (DynaActionForm)form;
	   		log.debug("Inside the doViewClaimDetails method of ProviderClaimsAction");
	   			String rownum	=	(String)request.getParameter("rownum");
				OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
				TableData tableData = (TableData) request.getSession().getAttribute("tableData");
				PreAuthSearchVO preAuthSearchVO	=	null;
				preAuthSearchVO = (PreAuthSearchVO)tableData.getRowInfo(Integer.parseInt(rownum));
				Object[] objects	=	onlineProviderManager.getClaimDetails(preAuthSearchVO.getClmSeqId());
	           
				ClaimDetailVO claimDetailVO	=	(ClaimDetailVO) objects[0];
				frmOnlineClaimsDetails = (DynaActionForm)FormUtils.setFormValues("frmOnlineClaimsDetails",
						claimDetailVO, this, mapping, request);
				
				ArrayList<DiagnosisDetailsVO> diagnosis = (ArrayList<DiagnosisDetailsVO>) objects[1];
				ArrayList<ActivityDetailsVO> alActivityDetails = (ArrayList<ActivityDetailsVO>) objects[2];
				ArrayList<String[]> alShortFallList	=	(ArrayList<String[]>) objects[3];
				request.getSession().setAttribute("claimDiagnosis", diagnosis);
				request.getSession().setAttribute("claimActivities", alActivityDetails);
				request.getSession().setAttribute("alShortFallList", alShortFallList);
				request.getSession().setAttribute("clmSeqId", preAuthSearchVO.getClmSeqId());
				request.getSession().setAttribute("claimStatus", preAuthSearchVO.getStatus());
				request.getSession().setAttribute("preAuthStatus", null);
				request.getSession().setAttribute("frmOnlineClaimsDetails", frmOnlineClaimsDetails);
				
	   		return this.getForward(strViewClaimDetails, mapping, request);
	   	}//end of try
	   	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
			}//end of catch(Exception exp)
	   }//end of doViewClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	   

	   /*
	    * View Shortfalls from Providers
	    */
	   public ActionForward doViewShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   		HttpServletResponse response) throws Exception{
		   	try{
		   		setOnlineLinks(request);
		   		log.debug("Inside the doViewShortfall method of ProviderClaimsAction");
		   		DynaActionForm frmShortFall = (DynaActionForm)form;
		   		Long shortfallSeqId		=	new Long(request.getParameter("shortfallSeqId"));
		   		Long clmSeqId				=	(long) request.getSession().getAttribute("clmSeqId");
		   		
		   		PreAuthManager preAuthObject=this.getPreAuthManagerObject();
		   	  ArrayList<Object> alShortfallList = new ArrayList<Object>();
	          alShortfallList.add(shortfallSeqId);
	          alShortfallList.add(clmSeqId);
	          alShortfallList.add(null);
	          alShortfallList.add(TTKCommon.getUserSeqId(request));
	          ShortfallVO   shortfallVO=preAuthObject.getShortfallDetail(alShortfallList);
	          
	          //Get Member and Provider Data for Shortfall
	          OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
	          String[] shortFallData	=	onlineProviderManager.getMemProviderForShortfall(shortfallSeqId,"CLM");
	          frmShortFall = (DynaActionForm)FormUtils.setFormValues("frmShortFall", shortfallVO, this, mapping, request);

	          request.getSession().setAttribute("preauthShortfallQueries",shortfallVO.getShortfallQuestions());
	          request.getSession().setAttribute("frmShortFall",frmShortFall);
	          request.getSession().setAttribute("shortFallData", shortFallData);
	          return this.getForward(strViewShortfallDetails, mapping, request);
		   	}//end of try
		   	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
				}//end of catch(Exception exp)
		   }//end of doViewAuthDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	   
	   
	   /*
	    * Save Shortfalls from Providers
	    */
	   
	   public ActionForward doSaveShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   		HttpServletResponse response) throws Exception{
		   	try{
		   		setOnlineLinks(request);
		   		log.info("Inside the doSaveShortfall method of ProviderClaimsAction");
		   		DynaActionForm frmShortFall = (DynaActionForm)form;
		   		ShortfallVO shortfallVO = (ShortfallVO)FormUtils.getFormValues(frmShortFall, this, mapping, request);
				OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject();
				
				// UPLOAD FILE STARTS
				FormFile formFile = null;
				formFile = (FormFile)frmShortFall.get("file");
				String successYN	=	onlineProviderManager.saveShorfallDocs(shortfallVO.getShortfallSeqID(),formFile);
				//  
				if(successYN.equals("Y"))
					request.setAttribute("updated","message.addedSuccessfully");
		   		return this.getForward(strViewShortfallDetails, mapping, request);
		   	}//end of try
		   	catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
				}//end of catch(Exception exp)
		   }//end of doSaveShortfall(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	  
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
				
				String strFile	=	TTKPropertiesReader.getPropertyValue("providerClmErrLogDir")+request.getParameter("batchNo")+".xls";
    		    
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
		            		return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
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
	   
	   /*
	    * On CLick To Download Claoms Upload template 
	    */
	   
	   public ActionForward  doDownloadClmTemplate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws TTKException {
		    
			 ByteArrayOutputStream baos=null;
		    OutputStream sos = null;
		    FileInputStream fis = null; 
		    BufferedInputStream bis =null;
		  try{   
			 // DynaActionForm frmForm=new DynaActionForm();
			 String flagYN =request.getParameter("flagYN");
			 System.out.println("flagYN=======>"+flagYN);
			 String strFile;
				if("SUB".equals(flagYN)){
					 strFile	=	TTKPropertiesReader.getPropertyValue("providerClmTemplate")+"claimUploadTemplate.xls";
	    		    
					response.setContentType("application/vnd.ms-excel");
					response.addHeader("Content-Disposition","attachment; filename=claimUploadTemplate.xls");
					
				}
				else{
					 strFile	=	TTKPropertiesReader.getPropertyValue("providerClmTemplate")+"ReSubmissionClaimUploadTemplate.xls";
	    		    
					response.setContentType("application/vnd.ms-excel");
					response.addHeader("Content-Disposition","attachment; filename=ReSubmissionClaimUploadTemplate.xls");
				}
			/*	String strFile	=	TTKPropertiesReader.getPropertyValue("providerClmTemplate")+"ReSubmissionClaimUploadTemplate.xls";
    		    
				response.setContentType("application/vnd.ms-excel");
				response.addHeader("Content-Disposition","attachment; filename=claimUploadTemplate.xls");*/

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
		            		return this.processExceptions(request, mapping, new TTKException(exp,"strClaimsUploadError"));
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
	   
	   
	   /*
	    * Generate Claim Letter
	    */
	   public ActionForward generateClaimLetter(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			try {
				setOnlineLinks(request);
				DynaActionForm frmOnlineClaimsDetails = (DynaActionForm) form;
				
				String claimSeqID = frmOnlineClaimsDetails.getString("clmSeqId");
				String claimSettelmentNo = frmOnlineClaimsDetails
						.getString("settlementNo");
				String claimStatus = frmOnlineClaimsDetails.getString("clmStatus");
				JasperReport mainJasperReport = null;
				JasperReport diagnosisJasperReport = null;
				JasperReport activityJasperReport = null;
				JasperPrint mainJasperPrint = null;
			String parameter = "";
			String mainJrxmlfile="";
			String activityJrxmlfile="";
			TTKReportDataSource mainTtkReportDataSource = null;
				TTKReportDataSource diagnosisTtkReportDataSource = null;
				TTKReportDataSource activityTtkReportDataSource = null;
			parameter="|"+claimSeqID+"|"+claimStatus+"|CLM|";
					 
				 mainJrxmlfile = "generalreports/ClaimApprovalOrDenialLetter.jrxml";
				 activityJrxmlfile = "generalreports/ClaimActivitiesDoc.jrxml";
				 mainTtkReportDataSource=new TTKReportDataSource("ClaimLetterFormat",parameter);
				
			 
			String diagnosisJrxmlfile = "generalreports/ClaimDiagnosisDoc.jrxml";
			 

				ByteArrayOutputStream boas = new ByteArrayOutputStream();

				String strPdfFile = TTKPropertiesReader
						.getPropertyValue("authorizationrptdir")
						+ claimSettelmentNo + ".pdf";
				JasperReport emptyReport = JasperCompileManager
						.compileReport("generalreports/EmptyReprot.jrxml");
				//mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
		 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
			
			diagnosisTtkReportDataSource = new TTKReportDataSource("DiagnosisDetails",parameter);  		
			activityTtkReportDataSource = new TTKReportDataSource("ActivityDetails",parameter);

			ResultSet main_report_RS=mainTtkReportDataSource.getResultData();
			
			mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
			diagnosisJasperReport = JasperCompileManager.compileReport(diagnosisJrxmlfile);
			activityJasperReport = JasperCompileManager.compileReport(activityJrxmlfile);			  
			
				 hashMap.put("diagnosisDataSource",diagnosisTtkReportDataSource);
				 hashMap.put("diagnosis",diagnosisJasperReport);		
				 hashMap.put("activityDataSource",activityTtkReportDataSource);		
				 hashMap.put("activity",activityJasperReport);
				 //JasperFillManager.fillReport(activityJasperReport, hashMap, activityTtkReportDataSource);						 
		 
		 if (main_report_RS == null&!main_report_RS.next())
		 {
			 mainJasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
		 }
		 else
		 {
			 main_report_RS.beforeFirst();
					mainJasperPrint = JasperFillManager.fillReport(
							mainJasperReport, hashMap, mainTtkReportDataSource);
				}// end of else
				JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
				JasperExportManager.exportReportToPdfStream(mainJasperPrint, boas);
				JasperExportManager.exportReportToPdfFile(mainJasperPrint,
						strPdfFile);
				request.setAttribute("boas", boas);
				return mapping.findForward("reportdisplay");// This forward goes to
															// the in web.xml file
															// BinaryServlet
			}// end of try
			catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, "onlineproviderinfo"));
			}// end of catch(Exception exp)
		}// end of generateClaimLetter(ActionMapping mapping,ActionForm
			// form,HttpServletRequest request,
			// HttpServletResponse response)
	   /**
	    * this method will add search criteria fields and values to the arraylist and will return it
	    * @param frmOnlinePreAuthLog formbean which contains the search fields
	    * @return ArrayList contains search parameters
	    */
	   private ArrayList<Object> populateSearchCriteria(DynaActionForm frmOnlinePreAuthLog,HttpServletRequest request) throws TTKException
	   {
		 
			UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");

				//build the column names along with their values to be searched
		       	ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
		       	//prepare the search BOX parameters
		        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("trtmtFromDate")));//0
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("trtmtToDate")));//1
		   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("clmFromDate")));//2
		        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("clmToDate")));//3
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("patientName")));//4
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("status")));//5
		   	 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("invoiceNumber")));//6
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("batchNumber")));//7
		        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("patientCardId")));//8
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("claimNumber")));//9
		       	alSearchBoxParams.add((String)userSecurityProfile.getEmpanelNumber());//10 Empanel Number
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("eventReferenceNo")));//11
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("preShortFallStatus")));//12
		       	
		       	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("emirateID")));//13	
		    	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePreAuthLog.get("inProgressStatus")));//14
		       	
	   	return alSearchBoxParams;
	   }//end of populateSearchCriteria(DynaActionForm frmOnlinePreAuthLog,HttpServletRequest request) 
	   
	   
	   //getManagerObject
	private PreAuthManager getProviderClaimsManagerObject() throws TTKException
	{
		PreAuthManager preAuthManager = null;
		try
		{
			if(preAuthManager == null)
			{
				InitialContext ctx = new InitialContext();
				preAuthManager = (PreAuthManager) ctx.lookup("java:global/TTKServices/business.ejb3/PreAuthManagerBean!com.ttk.business.preauth.PreAuthManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "onlineproviderinfo");
		}//end of catch
		return preAuthManager;
	}//end getPreAuthManagerObject()
	
	
	
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
	 
	 private PreAuthManager getPreAuthManagerObject() throws TTKException
		{
			PreAuthManager preAuthManager = null;
			try
			{
				if(preAuthManager == null)
				{
					InitialContext ctx = new InitialContext();
					preAuthManager = (PreAuthManager) ctx.lookup("java:global/TTKServices/business.ejb3/PreAuthManagerBean!com.ttk.business.preauth.PreAuthManager");
				}//end if
			}//end of try
			catch(Exception exp)
			{
				throw new TTKException(exp, "onlineproviderinfo");
			}//end of catch
			return preAuthManager;
		}//end getPreAuthManagerObject()
	 
}//end of ProviderClaimsAction
