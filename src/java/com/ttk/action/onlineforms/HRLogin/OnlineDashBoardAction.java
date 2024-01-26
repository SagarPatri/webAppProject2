/**
 * @ (#) OnlinePoliciesAction.java July 24,2006
 * Project       : TTK HealthCare Services
 * File          : OnlinePoliciesAction.java
 * Author        : Harsha Vardhan B N
 * Company       : Span Systems Corporation
 * Date Created  : July 24,2006
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.onlineforms.HRLogin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Member;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.enrollment.HRFilesDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;


/**
 * This class is used to view the List of Policies
 */
public class OnlineDashBoardAction extends TTKAction {
	private static Logger log = Logger.getLogger( OnlineDashBoardAction.class );
	//  Action mapping forwards.
	private static final String strForward ="Forward";
	private static final String strBackward ="Backward";
    private static final String strDashBoard="dashBoard"; 
    private static final String strSearch="search"; 
    private static final String strCorporateList="corporatelist";
    //Exception Message Identifier
    private static final String strOnlinePolicies="onlinePolicies";
    private static final String strOnlinePolicyDetails="onlinepolicydetails";
    private static final String strFailure="failure";
    
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
    
    public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);
    		log.debug("Inside OnlinePoliciesAction doDefault");
    		OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManagerObject();
    		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)
    				request.getSession().getAttribute("UserSecurityProfile"));
    		
    		String strActiveTab=TTKCommon.getActiveTab(request);
    		
    		if("Dashboard".equals(strActiveTab)){
    			
    		String strGroupID = userSecurityProfile.getGroupID();
    		
    		HashMap<String,String> hmEndorsementDetails =new HashMap<String,String>();
    		
    		hmEndorsementDetails=onlineAccessManagerObject.getPolicyAndEndorsementDetails(strGroupID);
    		
    		ArrayList<Object> policyList = new ArrayList<>();
		    policyList = onlineAccessManagerObject.getPolicyList(strGroupID);
		    request.getSession().setAttribute("policyListCollection", policyList);
    		
    		request.getSession().setAttribute("hmEndorsementDetails",hmEndorsementDetails);
    		
    		
    		}
    		return this.getForward(strDashBoard, mapping, request);
    		
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
    	}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
    
//kocbroker
    public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception{
        try{
            setOnlineLinks(request);
            log.debug("Inside the doSearch method of OnlinePoliciesAction");
			OnlineAccessManager onlineAccessManager = null;
			TableData tableData=null;
			String strPageID =""; 
			String strSortID ="";	
			ArrayList alOnlineAccList = null;
			
			OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManagerObject();
			tableData=(request.getSession().getAttribute("tableData")!=null)?(TableData)request.getSession().getAttribute("tableData"):new TableData();
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return (mapping.findForward(strSearch));
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
				tableData.createTableInfo("OnlinePoliciesSearchTable",null);
				tableData.setSearchData(this.populateSearchCriteriaBro((DynaActionForm)form,request));						
				tableData.modifySearchData("Policies");
				//sorting based on investSeqID in descending order													
			}//end of else
			alOnlineAccList=onlineAccessManagerObject.getOnlinePolicySearchList(tableData.getSearchData());
			tableData.setData(alOnlineAccList, "Policies");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			//finally return to the grid screen
			return this.getForward(strDashBoard, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processOnlineExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
        	return this.processOnlineExceptions(request, mapping, new TTKException(exp, strOnlinePolicies));
        }//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                            //HttpServletResponse response)
    
    private ArrayList<Object> populateSearchCriteriaBro(DynaActionForm frmOnlinePolicies,HttpServletRequest request) throws TTKException
    {
//      build the column names along with their values to be searched
        ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
        UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
        //prepare the search BOX parameters
        String strUserID = userSecurityProfile.getUSER_ID();
        alSearchBoxParams.add(strUserID);
        alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePolicies.getString("sPolicyNumber")));
    	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePolicies.getString("sGroupId")));
    	alSearchBoxParams.add(userSecurityProfile.getLoginType());
    	alSearchBoxParams.add(TTKCommon.getUserSeqId(request));
    	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmOnlinePolicies.getString("sGroupName")));
		
    	
    	return alSearchBoxParams;
    }//end of populateSearchCriteria(DynaActionForm frmOnlineAccountInfo,HttpServletRequest request)

    
    public ActionForward doDashBoardEnrollmentRefresh(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlineDashBoardAction doDashBoardEnrollmentRefresh");
		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)
				request.getSession().getAttribute("UserSecurityProfile"));
		
		OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManagerObject();
		
		String strGroupID = userSecurityProfile.getGroupID();
		HashMap<String,String> hmEndorsementDetails =new HashMap<String,String>();
		
		hmEndorsementDetails=onlineAccessManagerObject.getPolicyAndEndorsementDetails(strGroupID);
		
		request.getSession().setAttribute("hmEndorsementDetails",hmEndorsementDetails);
		
		return mapping.findForward(strDashBoard);
		}//end of try
		catch(TTKException expTTK)
			{
			return this.processOnlineExceptions(request,mapping,expTTK);
				}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request,mapping,new TTKException(exp,strDashBoard));
		}//end of catch(Exception exp)
    }//end of doSelectCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   //doDownLoadFiles 
    public ActionForward doDownLoadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlineDashBoardAction doDashBoardEnrollmentRefresh");
		
		

		
		String filePath=TTKPropertiesReader.getPropertyValue("HRLogin.UploadSampleFileFormats");
		String fileName="";
		ServletOutputStream sos=response.getOutputStream();
		
    if("ADD".equals(request.getParameter("fileTypeDownload"))){
	     filePath+="addition.xls";
	      fileName="addition.xls";
        }
     else if("DEL".equals(request.getParameter("fileTypeDownload"))){
	       filePath+="deletion.xls";
	       fileName="deletion.xls";
          }
      else if("MOD".equals(request.getParameter("fileTypeDownload"))){
	      filePath+="modification.xls";
	     fileName="modification.xls";
      }
      else
      {
	      filePath+="renewal.xls";
	     fileName="renewal.xls";
      }
		response.setContentType("application/vnd.ms-excel");

		response.addHeader("Content-Disposition","attachment; filename="+fileName);
    //   
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
		return this.processOnlineExceptions(request,mapping,new TTKException(exp,strDashBoard));
		}//end of catch(Exception exp)
    }//end of doSelectCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
 
    //doUpLoadFiles  
    public ActionForward doUpLoadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlineDashBoardAction doDashBoardEnrollmentRefresh");
		//FileUploadForm
		HttpSession session = request.getSession();
		DynaActionForm frmOnlinePolicies = (DynaActionForm) form;
		session.setAttribute("frmOnlinePolicies", frmOnlinePolicies);
		 FileOutputStream outputStream = null;
		  FormFile formFile = null;
		  String fileDir="";
		  String fileName="";
		  String fileUploadMode="";
		  UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)	session.getAttribute("UserSecurityProfile"));
		 String groupID=userSecurityProfile.getGroupID();
		 
		 String hrUserID=userSecurityProfile.getUSER_ID();
		 
		 //  
		 
		  //  
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		  String toDayDate=dateFormat.format(new Date()); //2016/11/16 12:08:43
		      
		  formFile = (FormFile)frmOnlinePolicies.get("excelFile");
		  
		  fileName=toDayDate+"-"+TTKCommon.getUserSeqId(request)+"-"+formFile.getFileName();
		  
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
		    	  
		    	  
		      
		        
		    		   
		      if("ADD".equals(request.getParameter("fileTypeUpload"))){
		    	     
		    	     
		    	 
		    	  fileDir=TTKPropertiesReader.getPropertyValue("HRLogin.UploadExcelFile");
		    	  fileDir+="addition/";
		    	   //   
		    	   fileUploadMode ="Addition";
		    	   
		         }
		      else if("DEL".equals(request.getParameter("fileTypeUpload"))){
		    	 //   
		    	  fileDir=TTKPropertiesReader.getPropertyValue("HRLogin.UploadExcelFile");
		    	  fileDir+="deletion/";
		    	  fileUploadMode ="Deletion";
		    	  //System.out.println("Uploaded deletion file successfully"); 
		           }
		       else if("MOD".equals(request.getParameter("fileTypeUpload"))){
		    	   
		    	   fileDir=TTKPropertiesReader.getPropertyValue("HRLogin.UploadExcelFile");
		    	   fileDir+="modification/";
		    	    fileUploadMode ="Modification";
		    	  // System.out.println("Uploaded modification file successfully"); 
		 	      
		       }
		       else
		       {   
		    	   fileDir=TTKPropertiesReader.getPropertyValue("HRLogin.UploadExcelFile");
		    	   fileDir+="renewal/";
		    	   fileUploadMode ="Renewal";
		    	    
		       }
		     String uploadMode=request.getParameter("fileTypeUpload");
		      fileDir=fileDir+groupID;
		      
		     File file= new File(fileDir);
		    
		     if(!file.exists())file.mkdirs();
		     
		     
		      outputStream = new FileOutputStream(fileDir+"/"+fileName);
		      outputStream.write(formFile.getFileData());
		      
		      if (outputStream != null)  outputStream.close();
		 
		      //   
		  
		      HRFilesDetailsVO hrFilesDetailsVO = new HRFilesDetailsVO();
		      SimpleDateFormat dateFormatUpload = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			  String toDayDateUpload=dateFormatUpload.format(new Date());
		      
			  Long policySequenceId = (Long) frmOnlinePolicies.get("listofPolicysequenceId");
		      OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManagerObject();
		      
		      //  
		      //  
		      //  
		      //  
		      hrFilesDetailsVO.setStrHRUserID(hrUserID);
		      hrFilesDetailsVO.setStrFileName(fileName);
		      hrFilesDetailsVO.setStrFilePath(fileDir+"/"+fileName);
		      hrFilesDetailsVO.setStrUploadMode(uploadMode);
		      hrFilesDetailsVO.setStrGroupID(groupID);
		      hrFilesDetailsVO.setHrUploadDate(toDayDateUpload);
		      hrFilesDetailsVO.setPolicySeqID(policySequenceId);
		      
		      
		     
		      
		      /*ArrayList<Object> policyList = new ArrayList<>();
			  policyList = onlineAccessManagerObject.getPolicyList(groupID);
			  request.getSession().setAttribute("policyListCollection", policyList);*/
		      
		     int updateCount= onlineAccessManagerObject.saveHRFileDetails(hrFilesDetailsVO);
		
		request.setAttribute("successMsg", "File Uploaded Successfully");
		frmOnlinePolicies.initialize(mapping);
        return mapping.findForward(strDashBoard);
		
		}//end of try
		catch(TTKException expTTK)
			{
			return this.processOnlineExceptions(request,mapping,expTTK);
				}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request,mapping,new TTKException(exp,strDashBoard));
		}//end of catch(Exception exp)
    }//end of doSelectCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
 //addition get details from excel
   /* public  ArrayList<Member> getAdditionDetails(InputStream inputStream) throws Exception{
    	
    	
    	HSSFWorkbook workbook =  (HSSFWorkbook) new HSSFWorkbook(inputStream);
		HSSFSheet sheet=workbook.getSheetAt(0);
		java.util.Iterator<?> rows     = sheet.rowIterator();
		//Member member=new Member();
    	if(rows.hasNext()){
    		  rows.next();
    				     
    			//iterating rows    
    		  while(rows.hasNext()){
    					
    					 HSSFRow row =  (HSSFRow) rows.next();
    					 
    					
    					 System.out.print(getCellValue(row.getCell(new Short("1"))));	
    					 System.out.print(" ");	
    					 System.out.print(getCellValue(row.getCell(new Short("2"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("3"))));	
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("4"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("5"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("6"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("7"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("8"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("9"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("10"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("11"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("12"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("13"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("14"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("15"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("16"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("17"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("18"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("19"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("20"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("21"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("22"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("23"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("24"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("25"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("26"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("27"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("28"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("29"))));
    					 System.out.print(" ");
    					 System.out.print(getCellValue(row.getCell(new Short("30"))));
    					 
    					   	
    	
    		        }
    	        
	      }	else{//if(rows.hasNext()){
		  
		     TTKException ttkExc =new TTKException();
		    ttkExc.setMessage("error.file.xls.norows");
		  
		  throw ttkExc;
	  }
    	
    	
    	
    	return null;
    }
    */
    
//end kocbroker    
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
			log.debug("Inside the doBackward method of OnlinePoliciesAction");
			setOnlineLinks(request);
			//Get the session bean from the beand pool for each thread being excuted.
			OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManagerObject();
			TableData  tableData =TTKCommon.getTableData(request);
			tableData.modifySearchData(strBackward);//modify the search data
			ArrayList alPolicyList=onlineAccessManagerObject.getOnlinePolicyList(tableData.getSearchData());
			tableData.setData(alPolicyList, strBackward);//set the table data
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strSearch, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
		}//end of catch(Exception exp)
	}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)
	
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
			log.debug("Inside the doForward method of OnlinePoliciesAction");
			setOnlineLinks(request);
			//Get the session bean from the beand pool for each thread being excuted.
			OnlineAccessManager onlineAccessManagerObject=this.getOnlineAccessManagerObject();
			TableData tableData = TTKCommon.getTableData(request);
			//modify the search data
			tableData.modifySearchData(strForward);
			ArrayList alPolicyList=onlineAccessManagerObject.getOnlinePolicyList(tableData.getSearchData());
			//set the table data
			tableData.setData(alPolicyList, strForward);
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strSearch, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePolicies));
		}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)
    
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
   
    public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setOnlineLinks(request);
    		log.debug("Inside OnlinePoliciesAction doClose");
    		return mapping.findForward("onlinemember");
    	}//end of try
    	catch(TTKException expTTK)
    	{
    		return this.processOnlineExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processOnlineExceptions(request,mapping,new TTKException(exp,strOnlinePolicies));
    	}//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * Returns the ArrayList which contains the populated search criteria elements
     * @param request HttpServletRequest object which contains the search parameter that is built
     * @return alSearchParams ArrayList
     */
    private ArrayList<Object> populateSearchCriteria(HttpServletRequest request) throws TTKException
    {
    	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)
    												request.getSession().getAttribute("UserSecurityProfile");
    	//build the column names along with their values to be searched
    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
    	if(userSecurityProfile.getLoginType().equals("E"))  //for corporate login
    	{
    		alSearchParams.add(userSecurityProfile.getUSER_ID());
    		alSearchParams.add(userSecurityProfile.getPolicyNo());
    		alSearchParams.add(userSecurityProfile.getGroupID());
    		alSearchParams.add(null);
    		alSearchParams.add(userSecurityProfile.getLoginType());
    		alSearchParams.add(TTKCommon.getUserSeqId(request));
    	}//end of if(userSecurityProfile.getLoginType().equals("E"))
    	else if(userSecurityProfile.getLoginType().equals("B"))  //for Broker login
    	{
    		alSearchParams.add(userSecurityProfile.getUSER_ID());
    		alSearchParams.add(null);
    		alSearchParams.add(userSecurityProfile.getGroupID());
    		alSearchParams.add(null);
    		alSearchParams.add(userSecurityProfile.getLoginType());
    		alSearchParams.add(TTKCommon.getUserSeqId(request));
    	}//end of if(userSecurityProfile.getLoginType().equals("B"))
    	else if(userSecurityProfile.getLoginType().equals("I"))     //for Individual Login
    	{
    		alSearchParams.add(null);
    		alSearchParams.add(userSecurityProfile.getPolicyNo());
    		alSearchParams.add(null);
    		alSearchParams.add(userSecurityProfile.getEnrollmentID());
    		alSearchParams.add(userSecurityProfile.getLoginType());
    		alSearchParams.add(TTKCommon.getUserSeqId(request));
    	}//end of else if(userSecurityProfile.getLoginType().equals("I"))
    	else if(userSecurityProfile.getLoginType().equals("H"))     //for HR Login
    	{
    		alSearchParams.add(userSecurityProfile.getUSER_ID());
    		alSearchParams.add(null);
    		alSearchParams.add(userSecurityProfile.getGroupID());
    		alSearchParams.add(null);
    		alSearchParams.add(userSecurityProfile.getLoginType());
    		alSearchParams.add(TTKCommon.getUserSeqId(request));
    	}//end of else if(userSecurityProfile.getLoginType().equals("H"))
    	return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchBankAccount)

    DecimalFormat  formatter = new DecimalFormat("0.000"); 
    
    final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
    
   private String getCellValue(HSSFCell cell){
	   
	   String cellValue="";
   	
   	if(cell==null){
   		cellValue="";
   	}
   	else
   		{ 
   		
   			switch (cell.getCellType ())
            {
                case HSSFCell.CELL_TYPE_NUMERIC :
                {
                    // Date CELL TYPE
                    if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)){
                    	
                    	cellValue=new SimpleDateFormat("DD/MM/YYYY").format(cell.getDateCellValue());
                    	
                    }
                    else{
                    	
                    	String strValue=formatter.format(cell.getNumericCellValue ());

                    	String values[]=strValue.split("[.]");
                    	if(new Integer(values[1])>0) cellValue=strValue; 
                    	else cellValue=values[0];
                    	
                    }
                    break;
                }
                case HSSFCell.CELL_TYPE_STRING :
                {
                    // STRING CELL TYPE
                    //String richTextString = cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
                	//String richTextString = cell.getStringCellValue().trim();
                	//richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
                   // rowData.add(richTextString);
                	cellValue=cell.getStringCellValue();
                    break;
                }
                case HSSFCell.CELL_TYPE_BLANK :
                {	
                	//HSSFRichTextString blankCell	=	cell.get.getRichStringCellValue();
                
                	//	String blankCell	=	cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
                	cellValue="";
                	break;
                }
                default:
                {
                	cellValue="";
                    break;
                }
            } // end switch
		}//else
   	
   return cellValue;
   }
       
    /**
    * Returns the onlineAccessManager session object for invoking methods on it.
    * @return onlineAccessManager session object which can be used for method invocation
    * @exception throws TTKException
    */
   private OnlineAccessManager getOnlineAccessManagerObject() throws TTKException
   {
   	OnlineAccessManager onlineAccessManager = null;
   	try
   	{
   		if(onlineAccessManager == null)
   		{
   			InitialContext ctx = new InitialContext();
   			onlineAccessManager = (OnlineAccessManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineAccessManagerBean!com.ttk.business.onlineforms.OnlineAccessManager");
   			log.debug("Inside getOnlineAccessManagerObject: onlineAccessManager: " + onlineAccessManager);
   		}//end if
   	}//end of try
   	catch(Exception exp)
   	{
   		throw new TTKException(exp, strFailure);
   	}//end of catch
   	return onlineAccessManager;
   }//end of getOnlineAccessManagerObject()
}//end of class OnlinePoliciesAction
