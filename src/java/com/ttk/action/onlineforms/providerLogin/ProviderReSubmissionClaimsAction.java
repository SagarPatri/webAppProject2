package com.ttk.action.onlineforms.providerLogin;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.claims.ClaimUploadAction;
import com.ttk.action.claims.claimUploadXml.ClaimsReSubmissionData;
import com.ttk.action.claims.claimUploadXml.ClaimsUpload;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.onlineforms.providerLogin.ClaimDetailVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

public class ProviderReSubmissionClaimsAction extends TTKAction{
	
	
	private static final Logger log = Logger.getLogger( ProviderReSubmissionClaimsAction.class );


    private static final String strResubmissionClaimsUpload="reSubmissionClaimsUpload";
    private static final String strClaimsUploadError="claimsUpload";
    private static final String strClaimsSearch="reSubmissionClaimsSearch";
    private static final String strViewClaimDetails="viewClaimDetails";
    private static final String strBackward="Backward";
    private static final String strForward="Forward";
    private static final String strViewShortfallDetails="viewShortfallDetails";
    private static final String strClaimsUpload="reSubmissionClaimsUpload";

    public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doDefault method of ProviderReSubmissionClaimsAction");
			setOnlineLinks(request);
			
			DynaActionForm frmOnlineClaimLog =(DynaActionForm)form;
			frmOnlineClaimLog.initialize(mapping);     
			
			return this.getForward(strResubmissionClaimsUpload, mapping, request);
		}
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"strReSubmissionClaimsUploadError"));
		}
}
    
    
    
    
   /* public ActionForward  doDownloadClmTemplate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException {
	    
		 ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
	  try{   
				
				String strFile	=	TTKPropertiesReader.getPropertyValue("providerClmTemplate")+"ReSubmissionClaimUploadTemplate.xls";
				response.setContentType("application/vnd.ms-excel");
				response.addHeader("Content-Disposition","attachment; filename=ReSubmissionClaimUploadTemplate.xls");

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
    
    */
    
    public ActionForward doClaimsLog(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doClaimsLog method of ProviderReSubmissionClaimsAction");
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
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"strReSubmissionClaimsUploadError"));
		}
}
    
    
    
    //doUploadClaims
    
    
    public ActionForward doUploadClaims(ActionMapping mapping,ActionForm form,HttpServletRequest request,
       		HttpServletResponse response) throws Exception{
    	
    	

		try {
			setLinks(request);
			log.debug("Inside ProviderReSubmissionClaimsAction doUploadClaims ");
			HttpSession session = request.getSession();
			DynaActionForm frmOnlineClaimUpload = (DynaActionForm) form;
			
			System.out.println("frmOnlineClaimUpload====>"+frmOnlineClaimUpload);
			
			FileWriter fileWriter = null;
			FileOutputStream outputStream = null;
			FormFile formFile = null;
			String fileDir = "";
			String fileName = "";
			String finalFileName = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String toDayDate = dateFormat.format(new Date()); 														
			formFile = (FormFile) frmOnlineClaimUpload.get("file");
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

			fileDir = TTKPropertiesReader.getPropertyValue("providerClmUpload");
			fileDir += "ProviderReSubmission/";
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
			 String hospSeqId  = String.valueOf(((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getHospSeqId());
		     String addedBy  = TTKCommon.getUserSeqId(request).toString();
		     
		     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		 	 Date date = new Date();
		     String sysdate=formatter.format(date);
		     String[] splitDate=sysdate.split(" ");
		     
     		 String receiveDate = splitDate[0];
		     String receiveTime = splitDate[1];
			 String receiveDay = splitDate[2];
			 String claimsSubmissionTypes = "DTR";

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
					return this.getForward(strClaimsUpload, mapping, request);
				}
				 // Extract Claims Data	
				ClaimUploadAction claimUploadAction	=	new ClaimUploadAction();
				Object claimsData[] = claimUploadAction.extractExcelDataForReSubmission(sheet);
				ClaimsUpload claimsUpload = new ClaimsUpload();
				claimsUpload.setHospSeqId(hospSeqId);
				claimsUpload.setAddedBy(addedBy);
				claimsUpload.setReceivedDate(new Timestamp(TTKCommon.getOracleDateWithTime(receiveDate, receiveTime,receiveDay).getTime()).toString());
				claimsUpload.setSourceType("PLCL");
				ArrayList<ClaimsReSubmissionData> claimsAllData = new ArrayList<ClaimsReSubmissionData>();
				claimsAllData = (ArrayList<ClaimsReSubmissionData>) claimsData[0];
				claimsUpload.setClaimsReSubmissionData(claimsAllData);
				claimsUpload.setCount((String) claimsData[1].toString());// No of lines-count
				String path = TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("providerClmUpload"));
				finalFileName = path+ "ProviderReSubmissionClaimUpload-"+ new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date()) + ".xml";
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
				frmOnlineClaimUpload.set("hiddenBatchRefNo", batchRefNo);
				frmOnlineClaimUpload.set("sussessYN", "Y");
				request.getSession().setAttribute("frmOnlineClaimUpload",frmOnlineClaimUpload);
			}// end try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping,new TTKException(exp, strClaimsUpload));
			}// end of catch(Exception exp)
			request.setAttribute("successMsg", "File Uploaded Successfully");
			return mapping.findForward(strClaimsUpload);
		}// end of try
		catch (TTKException expTTK) {
			((DynaActionForm) request.getSession().getAttribute("frmOnlineClaimUpload")).set("hiddenBatchRefNo", "");
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			((DynaActionForm) request.getSession().getAttribute("frmOnlineClaimUpload")).set("hiddenBatchRefNo", "");
			return this.processExceptions(request, mapping, new TTKException(exp, strClaimsUpload));
		}// end of catch(Exception exp)
    	
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
	            throw new TTKException(exp, "strReSubmissionClaimsUploadError");
	        }//end of catch
	        return onlineProviderManager;
	    }//end of getOnlineAccessManagerObject()  
	
	
	
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
	        	   
	   			String batchNo[] 	=	onlineProviderManager.uploadingReSubmissionClaims(BatchRefNo, userSeqId);
	   		    
	   			frmOnlineClaimUpload.set("batchRefNo",batchNo[0] );//xml seq id
	   			frmOnlineClaimUpload.set("batchNo", batchNo[2] );//batch no 
	   			frmOnlineClaimUpload.set("totalNoOfRows",batchNo[3] );//v_tot_rec_cnt 
	   			frmOnlineClaimUpload.set("totalNoOfRowsPassed",batchNo[5] );//v_succ_cnt 
	   			frmOnlineClaimUpload.set("totalNoOfRowsFailed",batchNo[4] );//v_fail_cnt
	   			frmOnlineClaimUpload.set("totalNoOfClaimsUploaded",batchNo[6] );//v_no_of_claims
	   			frmOnlineClaimUpload.set("totalNoOfRecordsUploaded",batchNo[9] );
	   			frmOnlineClaimUpload.set("batchTotalAmount", batchNo[7]);//v_batch_amt
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
						return (mapping.findForward(strClaimsSearch));
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
			//alOnlineClaimsList= onlineProviderManager.getClaimSearchList(tableData.getSearchData());
			alOnlineClaimsList= onlineProviderManager.getReSubmissionClaimSearchList(tableData.getSearchData());
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
	
	   
		   private ArrayList<Object> populateSearchCriteria(DynaActionForm frmOnlinePreAuthLog,HttpServletRequest request) throws TTKException
		   {
				 
					UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");

						//build the column names along with their values to be searched
				       	ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
				       	//prepare the search BOX parameters
				    	alSearchBoxParams.add("DTR");//5
				        alSearchBoxParams.add("");//0
				      	alSearchBoxParams.add("");//1
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
		   
		   public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			   		HttpServletResponse response) throws Exception{
			   	try{
			   		setOnlineLinks(request);
			   		log.debug("Inside the doForward method of ProviderReSubmissionClaimsAction");
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
			   		log.debug("Inside the doBackward method of ProviderReSubmissionClaimsAction");
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


			   public ActionForward doViewClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				   		HttpServletResponse response) throws Exception{
				   	try{
				   		setOnlineLinks(request);
				   		DynaActionForm frmOnlineClaimsDetails = (DynaActionForm)form;
				   		log.debug("Inside the doViewClaimDetails method of ProviderReSubmissionClaimsAction");
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

				   
    
}
