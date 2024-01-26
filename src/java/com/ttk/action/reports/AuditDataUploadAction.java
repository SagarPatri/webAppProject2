package com.ttk.action.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import com.ttk.action.claims.claimUploadXml.ClaimsUpload;
import com.ttk.action.processedcliams.AuditClaimData;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.hospital.PreAuthVO;

public class AuditDataUploadAction extends TTKAction{
	
	 private static final Logger log = Logger.getLogger( AuditDataUploadAction.class );
	 private static final String strDefaultAudit="auditDataUpload";
	 private static final String strClaimUpload="AuditClaimUpload";
	 private static final String strDefaultAuditDataUpload="auditDataUploadSearch";
	 private static final String strTableData="tableData";
	 private static final String strForward="Forward";
		private static final String strBackward="Backward";
	 
	 
	 public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 setLinks(request);
			 
			 return this.getForward(strDefaultAudit,mapping,request);
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
		 }
	 } 
	 
	public ActionForward doAuditUploadFiles(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			
		
			setLinks(request);
			log.debug("Inside AuditDataUploadAction doAuditUploadFiles ");

			DynaActionForm frmClaimUpload = (DynaActionForm) form;
			FileOutputStream outputStream = null;
			FormFile formFile = null;
			String fileName = "";
			String fileDir = "";
			String finalFileName = "";
			//int fileSize = 1024 * 1024;
			int fileSize = 40 * 1024 * 1024;
			String strNotify = null;
			formFile = (FormFile) frmClaimUpload.get("excelFile");
			fileName = formFile.getFileName();
			
			if (formFile == null || formFile.getFileSize() < 1) {
				TTKException ttkExc = new TTKException();
				ttkExc.setMessage("error.file.data.empty");
				throw ttkExc;
			}

			String[] dots = formFile.getFileName().split("[.]");
			String fileExce = dots[dots.length - 1];
			String fileExtn = GetFileExtension(formFile.toString());
			if (!"xls".equalsIgnoreCase(fileExce)) {
				TTKException ttkExc = new TTKException();
				ttkExc.setMessage("error.file.xls.type");
				throw ttkExc;
			}
			if (fileExtn.equalsIgnoreCase("xls")) {
				if (fileSize <= formFile.getFileSize()) {
			TTKException ttkExc = new TTKException();
			ttkExc.setMessage("error.file.xls.file.size");
			throw ttkExc;
				}
			}

			fileDir = TTKPropertiesReader
					.getPropertyValue("AuditUpload.UploadExcelFile");
			fileDir += "UploadFiles/";

			File file = new File(fileDir);

			if (!file.exists())
				file.mkdirs();

			outputStream = new FileOutputStream(fileDir + "/" + fileName);
			outputStream.write(formFile.getFileData());

			if (outputStream != null)
				outputStream.close();

				OnlineProviderManager onlineProviderManager = this
					.getOnlineAccessManagerObject();
				InputStream inputStream = formFile.getInputStream();

				HSSFWorkbook workbook = null; // (HSSFWorkbook) new
				HSSFSheet sheet = null;// workbook.getSheetAt(0);
				workbook = (HSSFWorkbook) new HSSFWorkbook(inputStream);
				sheet = workbook.getSheetAt(0);

				if (sheet == null) {
					request.setAttribute("errorMsg",
							"Please upload proper File");
					return this.getForward(strClaimUpload, mapping, request);
				}

			final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");

			Object claimsData[] = null;
			claimsData = extractExcelData(sheet);
			ClaimsUpload claimsUpload = new ClaimsUpload();
			claimsUpload.setAddedBy(TTKCommon.getUserSeqId(request).toString());
			ArrayList<AuditClaimData> claimsAllData = new ArrayList<AuditClaimData>();
			claimsAllData = (ArrayList<AuditClaimData>) claimsData[0];
			claimsUpload.setAuditClaimData(claimsAllData);
			claimsUpload.setCount((String) claimsData[1].toString());
			String path = TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("Claim.UploadExcelFile"));
			finalFileName = path+ "AuditDataClaimUpload-"+ new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date()) + ".xml";
			File file1 = new File(finalFileName);
			JAXBContext jaxbContext = JAXBContext
					.newInstance(ClaimsUpload.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(claimsUpload, file1);

			FileInputStream inputStream2 = new FileInputStream(file1);
			if(inputStream != null) inputStream.close();
			String batchRefNo = onlineProviderManager.saveAuditClaimUploadXML(
					inputStream2, fileName, TTKCommon.getUserSeqId(request));

			frmClaimUpload.set("sussessYN", "Y");
			
			request.setAttribute("successMsg", "File Uploaded Successfully");

			return mapping.findForward(strDefaultAudit);
		} catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strClaimUpload));
		}// end of catch(Exception exp)

		finally {
		}

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
	            throw new TTKException(exp, "onlineproviderinfo");
	        }//end of catch
	        return onlineProviderManager;
	    }
	 
	  public Object[] extractExcelData(HSSFSheet sheet) throws TTKException{
		   
		   ArrayList<AuditClaimData> claimsData	=	new ArrayList<AuditClaimData>();
		   ArrayList<String> rowData = new ArrayList<String>();
		        
		        DecimalFormat  formatter = new DecimalFormat("0.000");
		        int count	=	0;
		        int columnCount=104;
		        final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
		        if(sheet.getRow(0)==null){
		        	TTKException ttkException = new TTKException();
		        	ttkException.setMessage("error.wrong.excel.sheet");
		        	throw ttkException;
		        	
		        }
		        
		        int noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
                 if(noOfColumns != 104){
		        	
		        	TTKException ttkException = new TTKException();
		        	ttkException.setMessage("error.wrong.formatfile");
		        	throw ttkException;
		        }
                 else {	
		        	 Iterator<?> rows     = sheet.rowIterator ();
	 	              ArrayList<String> excelDatac;
		        	
		        while (rows.hasNext ()) 
		        {
		        	 HSSFRow row =  (HSSFRow) rows.next();
		            
		         
		             if(row.getRowNum()==0)
			            	continue;
		             
		             
		             count++;
		             rowData = new ArrayList<String>();

		             String temp	=	"";
			            for(short i=0;i<=103;i++)//No Of Excel Cells
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
				                        	if(i==12 || i==14 || i==15 ||i==20 || i==21 ||i==22 ||i==23 ||i==25 || i==26 || i==27 ||i==69 ){
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
			            	
			            }
			            	rowData.add((temp.replaceAll("[^\\x00-\\x7F]","")).trim());
			            } 
		            
		            AuditClaimData data	=	new AuditClaimData();
					  data.setClaimNo(rowData.get(0));
				        data.setAlkootRemarks(rowData.get(1));
				        data.setResubRemarks(rowData.get(2));
				        data.setPartnerName(rowData.get(3));
				        data.setBatchNo(rowData.get(4));
				        data.setEmpanelmentId(rowData.get(5));
				        data.setSubFlagType(rowData.get(6));
				        data.setSubType(rowData.get(7));
				        data.setInvNo(rowData.get(8));
				        data.setPreauthNo(rowData.get(9));
				        data.setClaimType(rowData.get(10));
				        data.setGender(rowData.get(11));
				        data.setMemInceptionDate(rowData.get(12));
				        data.setPolicyNo(rowData.get(13));
				        data.setPolicyStartDate(rowData.get(14));
				        data.setPolicyEndDate(rowData.get(15));
				        data.setCorporateName(rowData.get(16));
				        data.setSubCategory(rowData.get(17));
				        data.setClaimSource(rowData.get(18));
				        data.setOverrideRemarks(rowData.get(19));
				        data.setOverrideDate(rowData.get(20));
				        data.setReceivedDate(rowData.get(21));
				        data.setAddedDate(rowData.get(22));
				        data.setCompletedDate(rowData.get(23));
				        data.setClaimCompletedYN(rowData.get(24));
				        data.setServiceDate(rowData.get(25));
				        data.setAdmissionDate(rowData.get(26));
				        data.setDischargeDate(rowData.get(27));
				        data.setProviderName(rowData.get(28));
				        data.setEncounterType(rowData.get(29));
				        data.setBenefitType(rowData.get(30));
				        data.setClaimStatus(rowData.get(31));
				        data.setDenialCode(rowData.get(32));
				        data.setDenialDescription(rowData.get(33));
				        data.setTpaDenialCode(rowData.get(34));
				        data.setTpaDenialDesc(rowData.get(35));
				        data.setToothNo(rowData.get(36));
				        data.setQuantity(rowData.get(37));
				        data.setPrimaryIcdCode(rowData.get(38));
				        data.setPrimaryIcdDesc(rowData.get(39));
				        data.setSecondaryIcdCode(rowData.get(40));
				        data.setSecondaryIcdDesc(rowData.get(41));
				        data.setPresentingComplaints(rowData.get(42));
				        data.setActivityType(rowData.get(43));
				        data.setActivityCode(rowData.get(44));
				        data.setActivityDesc(rowData.get(45));
				        data.setInternalCode(rowData.get(46));
				        data.setActivityInternalDesc(rowData.get(47));
				        data.setRequestedAmountActivity(rowData.get(48));
				        data.setGrossAmnt(rowData.get(49));
				        data.setDiscountAmnt(rowData.get(50));
				        data.setDiscGrossAmnt(rowData.get(51));
				        data.setPatientShareAmnt(rowData.get(52));
				        data.setNetAmnt(rowData.get(53).trim());
				        data.setDisallowedAmnt(rowData.get(54));
				        data.setAllowedAmnt(rowData.get(55));
				        data.setApprovedAmnt(rowData.get(56));
				        data.setServiceDesc(rowData.get(57));
				        data.setActivityQuantityReq(rowData.get(58));
				        data.setActivityQuantityAppr(rowData.get(59));
				        data.setDataEntryBy(rowData.get(60));
				        data.setLastUpdatedBy(rowData.get(61));
				        data.setProcessedBy(rowData.get(62));
				        data.setPrimaryNtwrkType(rowData.get(63));
				        data.setEnrolmentId(rowData.get(64));
				        data.setMemberName(rowData.get(65));
				        data.setMaritalStatus(rowData.get(66));
				        data.setQatarId(rowData.get(67));
				        data.setRelationshipDesc(rowData.get(68));
				        data.setDob(rowData.get(69));
				        data.setMemberAge(rowData.get(70));
				        data.setProviderCategory(rowData.get(71));
				        data.setSettlementNo(rowData.get(72));
				        data.setPaymentStatus(rowData.get(73));
				        data.setChequeNo(rowData.get(74));
				        data.setChequeDate(rowData.get(75));
				        data.setOverrideYN(rowData.get(76));
				        data.setServiceName(rowData.get(77));
				        data.setProviderCountry(rowData.get(78));
				        data.setReqAmntOriginal(rowData.get(79));
				        data.setApprAmntOriginal(rowData.get(80));
				        data.setOriginalCurrency(rowData.get(81));
				        data.setClaimReqAmntQAR(rowData.get(82));
				        data.setClaimApprAmntQAR(rowData.get(83));
				        data.setTotalDisallowedAmnt(rowData.get(84));
				        data.setInternalRemaksStatus(rowData.get(85));
				        data.setInternalRemarksDesc(rowData.get(86));
				        data.setClinicianId(rowData.get(87));
				        data.setClinicianName(rowData.get(88));
				        data.setActivityTypeCode(rowData.get(89));
				        data.setSumInsured(rowData.get(90));
				        data.setAvailableSumInsured(rowData.get(91));
				        data.setUtilisedSumInsured(rowData.get(92));
				        data.setFinalRemarks(rowData.get(93));
				        data.setRemarksProMember(rowData.get(94));
				        data.setInternalRemarks(rowData.get(95));
				        data.setProviderSpecificRemarks(rowData.get(96));
				        data.setMemberSpecificPolicyRemarks(rowData.get(97));
				        data.setResubmissionRemarks(rowData.get(98));
				        data.setAuditRemarks(rowData.get(99));
				        data.setRecheckDoneRemarks(rowData.get(100));
				        data.setAuditStatus(rowData.get(101));
				        data.setProviderInternalCode(rowData.get(102));
				        data.setProviderInternalDesc(rowData.get(103));
				        
				        
				        if(0 != data.hashCode())
					        claimsData.add(data);
		        } //end while
		       
	   }
		        
		        Object returnVals[]	=	new Object[2];
		        returnVals	[0]	=	claimsData;
		        returnVals	[1]	=	count;
		        return returnVals;
	   } 


	  public ActionForward doDownloadAuditSampleFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			try{
			setOnlineLinks(request);
			log.debug("Inside AuditDataUploadAction doDownLoadFiles");
			 
			String filePath=TTKPropertiesReader.getPropertyValue("auditClaimDataUploadTemp");
			String fileName="";
			ServletOutputStream sos=response.getOutputStream();
			
	    
		     filePath+="auditClaimUploadTemplate.xls";
		      fileName="auditClaimUploadTemplate.xls";
	        
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
	    }


	  public ActionForward doDefaultUploadSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
			 try{
				 setLinks(request);
				 	log.debug("Inside AuditDataUploadAction doDefaultUploadSearch() ");
				 	DynaActionForm frmAuditDataUpload =(DynaActionForm)form;
					TableData tableData =TTKCommon.getTableData(request);
					//create new table data object
					tableData = new TableData();
					//create the required grid table
					tableData.createTableInfo("AuditDataUploadTable",null);
					request.getSession().setAttribute("tableData",tableData);
					((DynaActionForm)form).initialize(mapping);//reset the form data
					request.getSession().setAttribute("frmAuditDataUpload",frmAuditDataUpload);
					return this.getForward(strDefaultAuditDataUpload,mapping,request);
			 }//end of try
			 catch(TTKException expTTK)
			 {
				 return this.processExceptions(request, mapping, expTTK);
			 }
			 catch(Exception exp)
			 {
				 return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
			 }
		 } 

	  
	  public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
				try{
				log.debug("Inside the doSearch method of AuditDataUploadAction");
				setLinks(request);
				
				HttpSession session=request.getSession();
				OnlineProviderManager onlineProviderManager = this.getOnlineAccessManagerObject();
				TableData tableData =session.getAttribute("tableData")==null?new TableData():(TableData)session.getAttribute("tableData");
				
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
				return mapping.findForward(strDefaultAuditDataUpload);
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
				tableData.createTableInfo("AuditDataUploadTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				tableData.modifySearchData("search");
				}//end of else
				ArrayList alAuditDataUploadList= onlineProviderManager.getAuditDataUploadList(tableData.getSearchData());
				tableData.setData(alAuditDataUploadList, "search");
				//set the table data object to session
				session.setAttribute("tableData",tableData);
				//finally return to the grid screen
				return this.getForward(strDefaultAuditDataUpload, mapping, request);
				}//end of try
				catch(TTKException expTTK)
				{
				return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
				return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
				}//end of catch(Exception exp)
	}
	  
	  private ArrayList<Object> populateSearchCriteria(DynaActionForm frmAuditDataUpload,HttpServletRequest request)
	    {
	    	//build the column names along with their values to be searched
	    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	    	alSearchParams.add(frmAuditDataUpload.getString("policyNumber"));
	    	alSearchParams.add(frmAuditDataUpload.getString("corporateName"));
	    	alSearchParams.add(frmAuditDataUpload.getString("uploadDateFrom"));
	    	alSearchParams.add(frmAuditDataUpload.getString("uploadDateTo"));
	    	return alSearchParams;
	    }
	 
	  
	  public ActionForward removeUploadList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			try
			{
				log.debug("Inside the doDeleteList method of AuditDataUploadAction");
				setLinks(request);
				
				OnlineProviderManager onlineProviderManager = this.getOnlineAccessManagerObject();
				TableData tableData = TTKCommon.getTableData(request);
				String strDeletedPolicyNo ="";
				HttpSession session=request.getSession();
				
				strDeletedPolicyNo = populateDeleteId(request);
				
				Long userSeqId =TTKCommon.getUserSeqId(request);
				int iCount = onlineProviderManager.deleteUploadedData(strDeletedPolicyNo,userSeqId);
				if(iCount>0)
				{
					request.setAttribute("delete", "Records Deleted Successfully.");
				}//end of if(iCount>0)
				
				tableData.createTableInfo("AuditDataUploadTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				tableData.modifySearchData("search");
				ArrayList alAuditDataUploadList= onlineProviderManager.getAuditDataUploadList(tableData.getSearchData());
				
				tableData.setData(alAuditDataUploadList, "search");
				//set the table data object to session
				session.setAttribute("tableData",tableData);
				//finally return to the grid screen
				return this.getForward(strDefaultAuditDataUpload, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
			}//end of catch(Exception exp)
	  }
	  
	  
	  private String populateDeleteId(HttpServletRequest request) 
	  {
		//	ArrayList <Object>alHospitalList=new ArrayList<Object>();
			StringBuffer sbfId=new StringBuffer();
			String strChOpt[]=request.getParameterValues("chkopt");
			TableData tableData=(TableData)request.getSession().getAttribute("tableData");
			
			if((strChOpt!=null)&&strChOpt.length!=0)
			{
				for(int i=0; i<strChOpt.length;i++)
				{
					if(strChOpt[i]!=null)
					{	
						
							if(i==0)
							{
								sbfId=sbfId.append("|").append(String.valueOf(((PreAuthVO)tableData.getRowInfo(Integer.parseInt(strChOpt[i]))).getPolicyNumber())).append("|");
							}
							else
							{
								sbfId=sbfId.append(String.valueOf(((PreAuthVO)tableData.getRowInfo(Integer.parseInt(strChOpt[i]))).getPolicyNumber())).append("|");
							}//end of else
					}
				}
				
			}
			String deletedPolNo = sbfId.toString();
			return deletedPolNo;
		}//end of populateDeleteId(HttpServletRequest request)
	  
	  public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception
				{
				  try
				  {
					log.debug("Inside AuditDataUploadAction doForward");
					setLinks(request);
					TableData tableData = TTKCommon.getTableData(request);
					OnlineProviderManager onlineProviderManager = this.getOnlineAccessManagerObject();
					HttpSession session=request.getSession();
					
					tableData.modifySearchData(strForward);//modify the search data
					ArrayList alAuditDataUploadList= onlineProviderManager.getAuditDataUploadList(tableData.getSearchData());
					tableData.setData(alAuditDataUploadList, "search");
					//set the table data object to session
					session.setAttribute("tableData",tableData);
					//finally return to the grid screen
					return this.getForward(strDefaultAuditDataUpload, mapping, request);
				}//end of try
					catch(TTKException expTTK)
					{
					return this.processExceptions(request, mapping, expTTK);
					}//end of catch(TTKException expTTK)
					catch(Exception exp)
					{
					return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
					}//end of catch(Exception exp)
			} //end of doForward()
	  
	  public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception
				{
				  try
				  {
					log.debug("Inside AuditDataUploadAction doForward");
					setLinks(request);
					TableData tableData = TTKCommon.getTableData(request);
					OnlineProviderManager onlineProviderManager = this.getOnlineAccessManagerObject();
					HttpSession session=request.getSession();
					
					tableData.modifySearchData(strBackward);//modify the search data
					ArrayList alAuditDataUploadList= onlineProviderManager.getAuditDataUploadList(tableData.getSearchData());
					tableData.setData(alAuditDataUploadList, "search");
					//set the table data object to session
					session.setAttribute("tableData",tableData);
					//finally return to the grid screen
					return this.getForward(strDefaultAuditDataUpload, mapping, request);
				}//end of try
					catch(TTKException expTTK)
					{
					return this.processExceptions(request, mapping, expTTK);
					}//end of catch(TTKException expTTK)
					catch(Exception exp)
					{
					return this.processExceptions(request, mapping, new TTKException(exp,strClaimUpload));
					}//end of catch(Exception exp)
			} //end of doBackward()
	  
	} // end of AuditDataUploadAction class
