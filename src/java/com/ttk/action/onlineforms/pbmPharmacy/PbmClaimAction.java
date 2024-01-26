/**
 * @ (#) PbmPreauthAction.java 2nd Feb 2006
 * Project      : TTK HealthCare Services
 * File         : PbmPreauthAction.java
 * Author       : Raghavendra T M
 * Company      : Span Systems Corporation
 * Date Created : 2nd Feb 2006
 *
 * @author       : Raghavendra T M
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.onlineforms.pbmPharmacy;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import oracle.sql.DATE;

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
import com.ttk.action.claims.claimUploadXml.ClaimsUpload;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.business.onlineforms.providerLogin.OnlinePbmProviderManager;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching the List of Batch .
 * This class also provides option for Deletion, Addition and Updation of Batch.
 */
public class PbmClaimAction extends TTKAction {
	
	private static Logger log = Logger.getLogger( PbmClaimAction.class );
	
	//Modes
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	
	//Exception Message Identifier
	private static final String strPbmpreauthsearch="pbmpreauthsearch";
	private static final String strPbmClaimList="pbmClaimList";
	private static final String strPreAuthDetails="pbmPreauthDetails";
	private static final String strRedirectClm="pbmclmDetails";
	private static final String strBackToProcess="backToProcess";
	
	private static final String strClaimsUpload = "claimsUpload";
	
	private static final String strClaimsUploadError="claimsUpload";
	
	
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
	public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doDefault method of PbmPreauthAction");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			//clear the dynaform if visting from left links for the first time
			//else get the dynaform data from session
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			
			if(request.getAttribute("KeepTableData")==null){
				
			tableData = new TableData();//create new table data object
			tableData.createTableInfo("PbmClaimListTable",new ArrayList());
			((Column)((ArrayList)tableData.getTitle()).get(4)).setVisibility(false);//patient name
			((Column)((ArrayList)tableData.getTitle()).get(9)).setVisibility(false);//Claim Number
			((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);//Drug Description
			((Column)((ArrayList)tableData.getTitle()).get(11)).setVisibility(false);
			((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);
			((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
			}
			//create the required grid table
			
			request.getSession().setAttribute("tableData",tableData);
			((DynaActionForm)form).initialize(mapping);//reset the form data
			DynaActionForm frmPBMClaimLog= (DynaActionForm) form;
			request.getSession().setAttribute("frmPBMClaimLog",frmPBMClaimLog);//reset the form data
			
			return this.getForward(strPbmClaimList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmClaimList));
		}//end of catch(Exception exp)
	}//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
	public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doSearch method of PbmClaimAction");
			setLinks(request);
			
			HttpSession session=request.getSession();
			
			
			OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
			
			 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			 Long hospSeqID=userSecurityProfile.getHospSeqId();
			TableData tableData =TTKCommon.getTableData(request);
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
					return mapping.findForward(strPbmClaimList);
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else{
				//create the required grid table
				tableData.createTableInfo("PbmClaimListTable",null);
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
				//tableData.setSortColumnName("PRE_AUTH_NUMBER");
				
				((Column)((ArrayList)tableData.getTitle()).get(4)).setVisibility(false);//patient name
				((Column)((ArrayList)tableData.getTitle()).get(9)).setVisibility(false);//Claim Number
				((Column)((ArrayList)tableData.getTitle()).get(10)).setVisibility(false);//Drug Description
				((Column)((ArrayList)tableData.getTitle()).get(11)).setVisibility(false);
				((Column)((ArrayList)tableData.getTitle()).get(12)).setVisibility(false);
				((Column)((ArrayList)tableData.getTitle()).get(17)).setVisibility(false);
				tableData.modifySearchData("search");
			}//end of else
			
			ArrayList alClaimList=providerManagerObj.getPbmClaimList(tableData.getSearchData(),hospSeqID);
			
			tableData.setData(alClaimList, "search");
			//set the table data object to session
									
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strPbmClaimList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmClaimList));
		}//end of catch(Exception exp)
	}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	 public ActionForward doExit(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doBack method of PbmPharmacyGeneralAction");
			setOnlineLinks(request);
			HttpSession session=request.getSession();
			UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			String activeTab=userSecurityProfile.getSecurityProfile().getActiveTab();
			if("PBM".equals(activeTab))
			{
				return mapping.findForward(strBackToProcess);
			}
			return mapping.findForward(strPbmClaimList);
			
				
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPbmClaimList));
		}//end of catch(Exception exp)
	}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	//HttpServletResponse response)

	
	//do view claim details
	public ActionForward doViewclaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
              try{
                    log.debug("Inside PbmPreauthAction doViewPreauth");
                 setLinks(request);
                 HttpSession session=request.getSession();
                 PbmPreAuthVO pbmPreAuthVO=null;
                Long claimSeqNO=null;
                Long preAuthSeqId=null;
                 OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
                TableData tableData = TTKCommon.getTableData(request);
              DynaActionForm frmPreAuthList=(DynaActionForm)form;
              DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
            if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
           {
             pbmPreAuthVO=(PbmPreAuthVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
             request.getSession().setAttribute("decisionDateOfClaim", pbmPreAuthVO.getDecisionDtOfClaim());
             request.getSession().setAttribute("transactionDt", pbmPreAuthVO.getClaimSubmittedDate());
             claimSeqNO= pbmPreAuthVO.getClmSeqId();               
           }//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            if(pbmPreAuthVO.getPreAuthSeqID() != null){
            	preAuthSeqId=pbmPreAuthVO.getPreAuthSeqID();	
            }else{
            	preAuthSeqId = null;
            }
            
         //   pbmPreAuthVO=providerManagerObj.getAllPreAuthDetails(pbmPreAuthVO.getPreAuthSeqID());
              pbmPreAuthVO=  providerManagerObj.getClaimDetails(claimSeqNO,preAuthSeqId);
    		
    		  frmPbmPreauthGeneral.initialize(mapping);
    		
    		frmPbmPreauthGeneral = (DynaActionForm) FormUtils.setFormValues("frmPbmPreauthGeneral", pbmPreAuthVO, this, mapping,request);
    		
    		    frmPbmPreauthGeneral.set("allIcdDetails", pbmPreAuthVO.getIcdDetails());
    		    frmPbmPreauthGeneral.set("allDrugDetails", pbmPreAuthVO.getDrugDetails());
    						   		   		
    			request.getSession().setAttribute("closeMode", "ClaimSubmissionScreen");
    						
    	      request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
    
            
            if("INP".equals(pbmPreAuthVO.getClaimStatus())){
    			request.getSession().setAttribute("claimStatus", "In Progress");
    		}else if("APR".equals(pbmPreAuthVO.getClaimStatus())){
    			request.getSession().setAttribute("claimStatus", "Approved");
    		}else if("REJ".equals(pbmPreAuthVO.getClaimStatus())){
    			request.getSession().setAttribute("claimStatus", "Rejected");
    		}else if("PCN".equals(pbmPreAuthVO.getClaimStatus())){
    			request.getSession().setAttribute("claimStatus", "Cancel");
    		}else if("REQ".equals(pbmPreAuthVO.getClaimStatus())){
    			request.getSession().setAttribute("claimStatus", "Required Information");
    		}else{
    			request.getSession().setAttribute("claimStatus", pbmPreAuthVO.getClaimStatus());

    		}
            request.getSession().setAttribute("preAuthStatus", null);
            request.getSession().setAttribute("preAuthSeqId",preAuthSeqId);
            request.getSession().setAttribute("claimSeqId", pbmPreAuthVO.getClmSeqId());           
            session.setAttribute("pbmPreAuthVO", pbmPreAuthVO);
            
            return mapping.findForward(strRedirectClm);
              }//end of try
           catch(TTKException expTTK)
       {
             return this.processExceptions(request, mapping, expTTK);
          }//end of catch(TTKException expTTK)
     catch(Exception exp)
    {
     return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
   }//end of catch(Exception exp)
     }//end of doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request
	// HttpServletResponse response)
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doBackward method of PbmPreauthAction");
			setLinks(request);
		//	OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
			OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
			HttpSession session=request.getSession();
			
			 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			 Long hospSeqID=userSecurityProfile.getHospSeqId();
			
			/*ArrayList alBatchList = null;*/
			TableData tableData = TTKCommon.getTableData(request);
			tableData.modifySearchData(strBackward);//modify the search data
			//fetch the data from the data access layer and set the data to table object
		//	ArrayList alClaimList= onlineAccessManagerObject.getPbmReportData(tableData.getSearchData(),hospSeqID,null);// Populating Data Which matches the search criteria
			ArrayList alClaimList=providerManagerObj.getPbmClaimList(tableData.getSearchData(),hospSeqID);
			tableData.setData(alClaimList,strBackward);
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strPbmClaimList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
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
	public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doForward method of PbmPreauthAction");
			setLinks(request);
			HttpSession session=request.getSession();
			
			 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			 Long hospSeqID=userSecurityProfile.getHospSeqId();
		//	OnlineAccessManager onlineAccessManagerObject= this.getOnlineAccessManagerObject();
			 OnlinePbmProviderManager providerManagerObj=getPbmManagerObject(); 
			TableData tableData = TTKCommon.getTableData(request);
			tableData.modifySearchData(strForward);//modify the search data
			//fetch the data from the data access layer and set the data to table object
		//	ArrayList alClaimList= onlineAccessManagerObject.getPbmReportData(tableData.getSearchData(),hospSeqID,null); // Populating Data Which matches the search criteria
			ArrayList alClaimList=providerManagerObj.getPbmClaimList(tableData.getSearchData(),hospSeqID);
			tableData.setData(alClaimList,strForward);
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strPbmClaimList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
		}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
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
	public ActionForward doGenerateClaimLetter(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doForward method of PbmPreauthAction");
			setLinks(request);


			return this.getForward(strPbmClaimList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strPbmpreauthsearch));
		}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * Return the ArrayList populated with Search criteria elements
	 * @param DynaActionForm
	 * @param HttpServletRequest
	 * @return ArrayList
	 */
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmPBMClaimLog,HttpServletRequest request)
	{
		ArrayList<Object> alSearchParams =new ArrayList<Object>();
		
		alSearchParams.add(frmPBMClaimLog.getString("trtmtFromDate"));//1
		alSearchParams.add(frmPBMClaimLog.getString("trtmtToDate"));//2
		alSearchParams.add(frmPBMClaimLog.getString("clmFromDate"));//3
		alSearchParams.add(frmPBMClaimLog.getString("clmToDate"));//4
		alSearchParams.add(frmPBMClaimLog.getString("patientName"));//5
		alSearchParams.add(frmPBMClaimLog.getString("authNo"));	//6
		alSearchParams.add(frmPBMClaimLog.getString("invoiceNumber"));//7
		alSearchParams.add(frmPBMClaimLog.getString("alKootId"));//8
		alSearchParams.add(frmPBMClaimLog.getString("claimNumber"));//9
		alSearchParams.add(frmPBMClaimLog.getString("clmPayStatus"));//10
		alSearchParams.add(frmPBMClaimLog.getString("status"));//11
		alSearchParams.add(frmPBMClaimLog.getString("eventRefNo"));//12
		alSearchParams.add(frmPBMClaimLog.getString("batchNo"));//13
		alSearchParams.add(frmPBMClaimLog.getString("qatarId"));//14
		alSearchParams.add(frmPBMClaimLog.getString("inProgressStatus"));//15 inProgressStatus
		
		return alSearchParams;
	}//end of populateSearchCriteria(DynaActionForm frmBatchList,HttpServletRequest request)
	
	
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
	   		throw new TTKException(exp, "failure");
	   	}//end of catch
	   	return onlineAccessManager;
	   }//end of getOnlineAccessManagerObject()

	   OnlinePbmProviderManager  onlinePbmProviderManager= null;
	    private OnlinePbmProviderManager getPbmManagerObject() throws TTKException
	    {
	    	
	        try
	        {
	            if(onlinePbmProviderManager == null)
	            {
	                InitialContext ctx = new InitialContext();   
	                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
	                onlinePbmProviderManager = (OnlinePbmProviderManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlinePbmProviderManagerBean!com.ttk.business.onlineforms.providerLogin.OnlinePbmProviderManager");
	            }//end if
	        }//end of try
	        catch(Exception exp)
	        {
	            throw new TTKException(exp, "onlinepbmproviderinfo");
	        }//end of catch
	        return onlinePbmProviderManager;
	    }//end of getHospitalManagerObject()

	    public ActionForward doDefaultClaimsubmission(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		           HttpServletResponse response) throws Exception{
		try{
				log.debug("Inside the doDefault method of PBMClaimsAction");
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
	    
	    public ActionForward  doDownloadPBMClmTemplate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws TTKException {
		    
			 ByteArrayOutputStream baos=null;
		    OutputStream sos = null;
		    FileInputStream fis = null; 
		    BufferedInputStream bis =null;
		  try{   
				
				String strFile	=	TTKPropertiesReader.getPropertyValue("providerClmTemplate")+"PBMClaimUploadTemplate.xls";
    		    
				response.setContentType("application/vnd.ms-excel");
				response.addHeader("Content-Disposition","attachment; filename=claimUploadTemplate.xls");

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
	    
	    public ActionForward doPBMBulkUploadClaims(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	       		HttpServletResponse response) throws Exception{
	           	try{		
	           		log.info("Inside the doPBMBulkUploadClaims method of PBMClaimsAction");
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
	    		    fileDir+="PBMOriginalFile/";
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
	    	        OnlinePbmProviderManager onlinePbmProviderManager	=	this.getPbmManagerObject();
	            	  
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
	           	     
	           	      //Extract Claims Data
	           	    	ClaimUploadAction claimUploadAction	=	new ClaimUploadAction();
	           	    	Object claimsData[]		=	claimUploadAction.extractExcelDataForPBM(sheet);//extractExcelData(sheet,batchNos,hospSeqId,addedBy);
	           	    	ClaimsUpload claimsUpload	=	new ClaimsUpload();
	           	    	claimsUpload.setHospSeqId(hospSeqId);
	           	    	claimsUpload.setAddedBy(addedBy);
	           	    	claimsUpload.setSourceType("PBCL");
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
	           	    	String batchRefNo 	=	onlinePbmProviderManager.saveClaimXML(inputStream2,formFile.toString(),TTKCommon.getUserSeqId(request));
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

	    private static String GetFileExtension(String fname2) {
	        String fileName = fname2;
	 		String fname = "";
	 		String ext = "";
	 		int mid = fileName.lastIndexOf(".");
	 		fname = fileName.substring(0, mid);
	 		ext = fileName.substring(mid + 1, fileName.length());
	        return ext;
	    }
	    
	    public ActionForward doCreateClaimsFromUploadedFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	 	           HttpServletResponse response) throws Exception{
	           try{
	            setLinks(request);
	            log.debug("Inside doCreateClaimsFromUploadedFile  ");
	            HttpSession session = request.getSession();
	    		   DynaActionForm frmOnlineClaimUpload = (DynaActionForm) form;
	 	        
	 	        String BatchRefNo=(String)frmOnlineClaimUpload.get("hiddenBatchRefNo");
	 	        Long userSeqId = TTKCommon.getUserSeqId(request);
	 	       
	 	       OnlinePbmProviderManager onlinePbmProviderManager	=	this.getPbmManagerObject();
	        	   
	   			String batchNo[] 	=	onlinePbmProviderManager.uploadingClaims(BatchRefNo, userSeqId);
	   		    
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
	     }
	    
	    
		  public ActionForward doStatusChange(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		        try{
		            log.debug("Inside the method doStatusChange");
		            return this.getForward(strPbmClaimList, mapping, request);  
		           }//end of try
		        catch(TTKException expTTK)
		           {
		        	return this.processExceptions(request, mapping, expTTK);
		           }//end of catch(TTKException expTTK)
		      catch(Exception exp)
		      	{
		    	  return this.processExceptions(request, mapping, new TTKException(exp,strClaimsUpload));
		      	}//end of catch(Exception exp)
		        }
	    
}//end of PbmPreauthAction


