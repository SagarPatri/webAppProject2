package com.ttk.action.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.maintenance.MaintenanceManager;
import com.ttk.business.onlineforms.OnlinePreAuthManager;
import com.ttk.business.onlineforms.providerLogin.OnlinePbmProviderManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.empanelment.TdsCertificateVO;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

public class CommonUploadDownload extends TTKAction {

	private static final String strDocsUpload="DocsUpload";
	private static final String strClmDocsUpload="ClmDocsUpload";
	private static final String strUpload="upload";
	private static  String strOnlineDocsUpload="OnlineDocsUpload";
	private static final String strOnlinePtrDocsUpload="OnlinePtrDocsUpload";
	private static final String strOnlinePbmDocsUpload="OnlinePbmDocsUpload";
	
	private static final String strPreauthUploadClose="preauthUploadClose";
	private static final String strClaimsUploadClose="claimUploadClose";
	private static final String strUploadsHOSClosePreAuth=	"uploadsclosePreAuth";
	private static final String strUploadsHOSCloseClaims = "uploadscloseClaims";
	private static final String strUploadsPTRClosePreAuth=	"uploadsptrclosePreAuth";
	private static final String strUploadsPTRCloseClaims = "uploadsptrcloseClaims";
	private static final String strUploadsPBMClosePreAuth=	"uploadspbmclosePreAuth";
	private static final String strUploadsPBMCloseClaims = "uploadspbmcloseClaims";
	private static final String strReportExp="report";
	private static final String strUploadsHOSClosePreAuthfromLogSearch=	"uploadsclosePreAuthfromLogSearch";
	private static final String strUploadsHOSClosePreAuthEnhancefromLogSearch = "onlinePreAuthEnhance";
	private static final String strCloseUploadsHOSPreAuth = "closeUploadsHOSPreAuth";
	
	public ActionForward  doDefaultUploadView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
	{
		try
		{   
			log.debug("Inside the doDefaultUploadView method of CommonUploadDownload");
			//setLinks(request);
			String strTable = "";
			StringBuffer sbfCaption= new StringBuffer();	
			DynaActionForm frmDocsUpload= null;
			long preAuthSeqId=0;
			String typeId = request.getParameter("typeId");
			Long seqId = Long.parseLong(request.getParameter("seqId"));
			String strAppeal = TTKCommon.checkNull(request.getParameter("strAppeal"));
			String preauthOrClaimType = request.getParameter("preauthOrClaimType");
			boolean statusFlag=false;
			if( "Y".equals(request.getParameter("Entry"))) {
				frmDocsUpload = (DynaActionForm)form;
				frmDocsUpload.initialize(mapping);
			}//end of if(request.getParameter("Entry").equals("Y"))
			else {	
				if("PAT".equals(typeId)){
				frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmPreAuthGeneral");
				request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
//				sbfCaption.append("[").append(frmDocsUpload.get("preAuthNo")).append("]");	
				boolean isPtrPat=false;
				if(request.getSession().getAttribute("isPtrPat")!=null)
				isPtrPat=(boolean) request.getSession().getAttribute("isPtrPat") ;
				if(isPtrPat==true){
					String preAuthstaus=(String) request.getSession().getAttribute("preAuthStatus");					
					if(preAuthstaus!=null&&preAuthstaus.equals("In-Progress")){
						request.getSession().setAttribute("preAuthStatus","In Progress");
					}else{
						request.getSession().setAttribute("requestStatus", null);
					}
					
				}
				sbfCaption.delete(0, sbfCaption.length());
				sbfCaption.append(request.getSession().getAttribute("caption"));
				request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("CLM".equals(typeId)){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmClaimGeneral");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					sbfCaption.append("[").append(frmDocsUpload.get("claimNo")).append("]");	
					request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("HOS|PAT".equals(typeId)){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmOnlinePreAuth");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					String preAuthNo=(String) frmDocsUpload.get("preAuthNo");
					sbfCaption.append("[").append(preAuthNo).append("]");
					if(TTKCommon.getActiveTab(request).equals("Pre-Auth Log")){
					if(preAuthNo!=null&&!preAuthNo.equals("")){
						statusFlag=true;
					request.getSession().setAttribute("requestStatus", null);
					  }
					}else{
						if(preAuthNo!=null&&!preAuthNo.equals("")){
							
							statusFlag=false;
						request.getSession().setAttribute("requestStatus", null);
						}
					}
					
					if("Y".equals(request.getSession().getAttribute("enhanceYN"))){
						if(request.getSession().getAttribute("isSubmittedforEnhance")!=null&&request.getSession().getAttribute("isSubmittedforEnhance").equals(false))
							statusFlag=false;
						else
							
							statusFlag=true;
						
					}
					request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("HOS|CLM".equals(typeId)){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmOnlineClaimsDetails");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					sbfCaption.append("[").append(frmDocsUpload.get("claimNO")).append("]");	
					statusFlag=true;
					request.getSession().setAttribute("requestStatus", null);
					request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("PTR|PAT".equals(typeId)){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmOnlinePreAuth");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					sbfCaption.append("[").append(frmDocsUpload.get("preAuthNo")).append("]");
					if(frmDocsUpload.get("preAuthNo")!=null&&!frmDocsUpload.get("preAuthNo").equals("")){
						statusFlag=true;
						request.getSession().setAttribute("requestStatus", null);	
					}else{
						statusFlag=false;
						request.getSession().setAttribute("preAuthStatus","In Progress");
					}
					
					request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("PTR|CLM".equals(typeId)){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmOnlineClaimsDetails");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					sbfCaption.append("[").append(frmDocsUpload.get("claimNO")).append("]");	
					statusFlag=true;
					request.getSession().setAttribute("requestStatus", null);
					request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("PBM|PAT".equals(typeId)){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmPbmPreauthGeneral");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					sbfCaption.append("[").append(frmDocsUpload.get("preAuthNO")).append("]");	
					statusFlag=true;
					request.getSession().setAttribute("requestStatus", null);
					request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("PBM|CLM".equals(typeId)){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmOnlineClaimsDetails");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					statusFlag=true;
					request.getSession().setAttribute("requestStatus", null);
					sbfCaption.append("[").append(frmDocsUpload.get("claimNumber")).append("]");
					request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
				}
				
				if("CFD|CLM".equals(typeId) || "CFD|PAT".equals(typeId)){
					if(preauthOrClaimType.equals("PAT")){
					frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmPreAuthGeneral");
					request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
					
					}else if(preauthOrClaimType.equals("CLM")){
						
						frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmClaimGeneral");
						request.getSession().setAttribute("frmDocsUpload", frmDocsUpload);
						sbfCaption.append("[").append(frmDocsUpload.get("claimNo")).append("]");	
						request.getSession().setAttribute("ConfigurationTitle", sbfCaption.toString());
					}
				}
			//	request.getSession().setAttribute("EmpanelNumber",strEmpanelNumber);
			}//end of else			
			ArrayList<Object> alDocsUploadList = new ArrayList<Object>();	
			MaintenanceManager maintenanceObject=null;
		//	TableData tableDataDocsUpload =TTKCommon.getTableData(request);
			TableData tableDataDocsUpload =(TableData) request.getSession().getAttribute("tableDataDocsUpload");
			frmDocsUpload.set("caption",sbfCaption.toString());
			//get the table data from session if exists			
			if(tableDataDocsUpload==null){
				//create new table data object
				tableDataDocsUpload = new TableData();
			}//end of if(tableData==null) 
			//create the required grid table
			strTable = "DocsUploadFilesTable";
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			if(strSortID.equals(""))
			{
				tableDataDocsUpload.createTableInfo(strTable,null);
			//	tableDataDocsUpload.setSearchData(this.populateSearchCriteria(request));
				tableDataDocsUpload.modifySearchData("search");
			}//end of if(strSortID.equals(""))
			else
			{
				tableDataDocsUpload.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
				tableDataDocsUpload.modifySearchData("sort");//modify the search data	
			}// end of else	
			maintenanceObject=this.getMaintenanceManagerObject();
			if("PBM|CLM".equals(typeId)){
				if(request.getSession().getAttribute("preAuthSeqId")!=null)
					alDocsUploadList = maintenanceObject.getDocsUploadsList(alDocsUploadList,(long) request.getSession().getAttribute("preAuthSeqId"),"PBM|PAT");	
				else
				alDocsUploadList = maintenanceObject.getDocsUploadsList(alDocsUploadList,(Long) request.getSession().getAttribute("claimSeqId"),"PBM|PAT");				
			}
			
			alDocsUploadList = maintenanceObject.getDocsUploadsList(alDocsUploadList,seqId,typeId);
			tableDataDocsUpload.setData(alDocsUploadList,"search");
			request.setAttribute("typeId",typeId );
			request.setAttribute("seqId",seqId );
			if("APL".equalsIgnoreCase(strAppeal))
				request.setAttribute("APL", strAppeal);
			else
				request.setAttribute("APL", null);
			request.getSession().setAttribute("caption", sbfCaption.toString());
			request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
			request.getSession().setAttribute("frmDocsUpload",frmDocsUpload);
			if(statusFlag==false){
			if(request.getSession().getAttribute("preAuthStatus")!=null&&request.getSession().getAttribute("claimStatus")==null)
			request.getSession().setAttribute("requestStatus", request.getSession().getAttribute("preAuthStatus"));
			else if(request.getSession().getAttribute("claimStatus")!=null&&request.getSession().getAttribute("preAuthStatus")==null)
				request.getSession().setAttribute("requestStatus", request.getSession().getAttribute("claimStatus"));
			else{
				request.getSession().setAttribute("requestStatus", null);
			}
		} 
			if(("HOS|PAT".equals(typeId))||(("HOS|CLM".equals(typeId)))){
//			return this.getForward(strOnlineDocsUpload,mapping,request);
			return mapping.findForward(strOnlineDocsUpload);
			}
			if(("PTR|PAT".equals(typeId))||("PTR|CLM".equals(typeId)))
				return mapping.findForward(strOnlinePtrDocsUpload);
//			return this.getForward(strOnlinePtrDocsUpload, mapping, request);
			if(("PBM|PAT".equals(typeId))||("PBM|CLM".equals(typeId))){
				return mapping.findForward("OnlinePbmDocsUpload");
			}
//			return this.getForward(strOnlinePbmDocsUpload, mapping, request);
			else
//			return this.getForward(strDocsUpload, mapping, request);
			return mapping.findForward(strDocsUpload);
		} // end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strUpload));
		}//end of catch(Exception exp)
	} // end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			//HttpServletResponse response) throws TTKException 
	public ActionForward doCommonDocUploads(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		log.debug("Inside the download method of doCommonDocUploads");
		try{
			DynaActionForm frmDocsUpload=(DynaActionForm)form;
			ArrayList alLinkDetailsList=new ArrayList();
			TableData tableDataDocsUpload = null;
			Long seqId = Long.parseLong(request.getParameter("seqId"));
			String typeId = request.getParameter("typeId");
			String authData=null;
			if(("HOS|PAT".equals(typeId))||(("HOS|CLM".equals(typeId))))
			authData=strOnlineDocsUpload;
			else if(("PTR|PAT".equals(typeId))||("PTR|CLM".equals(typeId)))
			authData=strOnlinePtrDocsUpload;
			else if(("PBM|PAT".equals(typeId))||("PBM|CLM".equals(typeId)))
			authData="OnlinePbmDocsUpload";
			else if("CLM".equals(typeId))
				authData=strClmDocsUpload;
				else
					authData=strDocsUpload;
			boolean isAuthorized=checkAuthorization(authData,mapping,request);
			long preAuthSeqId=0;
			if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
			{
				tableDataDocsUpload=(TableData)(request.getSession()).getAttribute("tableDataDocsUpload");
			}//end of if(request.getSession().getAttribute("tableDataMouCertificates")!=null)
			else
			{
				tableDataDocsUpload=new TableData();
			}//end of else
			StringBuffer sbfCaption= new StringBuffer((String) request.getSession().getAttribute("caption"));			
			int iUpdate	=	0;
			String strNotify="";
			MaintenanceManager maintenanceObject=null;
			MaintenanceManager managerObject=this.getMaintenanceManagerObject();
			if(isAuthorized==true){
			String fileName="";
			String origFileName	=	"";
			Pattern pattern =null;
			Matcher matcher =null;
			FormFile formFile = null;
			int fileSize=4*1024*1024;
			TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
			DateFormat df =new SimpleDateFormat("ddMMyyyy HH:mm:ss:S");
			df.setTimeZone(tz);  
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			Long referenceSeqId = null;
			if(typeId != null && (typeId.contains("HOS")))
			referenceSeqId = userSecurityProfile.getHospSeqId();
			if(typeId != null && (typeId.contains("PTR")))
			referenceSeqId = userSecurityProfile.getPtnrSeqId();
			ArrayList<Object> alFileAUploadList = new ArrayList<Object>();
			alFileAUploadList.add(TTKCommon.getUserSeqId(request));//0
			Long userSeqId	=	(Long) TTKCommon.getUserSeqId(request);
			formFile = (FormFile)frmDocsUpload.get("file");
			String fileDesc	=	(String)frmDocsUpload.get("description");
			String sourceid	=	(String)frmDocsUpload.get("source_id");
			pattern = Pattern.compile( "[^a-zA-Z0-9_\\.\\-\\s*]" );
			matcher = pattern.matcher( formFile.getFileName() );
			String 	strTimeStamp=((df.format(Calendar.getInstance(tz).getTime())).replaceAll(" ", "_")).replaceAll(":", ""); 
			fileName=strTimeStamp+"_"+formFile.getFileName();
			origFileName	=	formFile.getFileName();
			InputStream inputStream	=	 formFile.getInputStream();//INPUT STREAM  FROM FILE UPLOADED
			int formFileSize	=	formFile.getFileSize();
			/*if(!matcher.find())
			{*/
				alFileAUploadList.add(fileName);//1
				alFileAUploadList.add(fileDesc);//2
				alFileAUploadList.add(seqId);//3
				alFileAUploadList.add(sourceid);//4
				//	alFileDetails=this.populateSearchCriteria(webconfigInsCompInfoVO);
					String finalPath=fileName;
				String strFileExt=formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1,formFile.getFileName().length());
				if(!(fileDesc==null||fileDesc.trim().equals("")))
				{
					if(formFile.getFileSize()<=fileSize)
					{				
						if((strFileExt.equalsIgnoreCase("pdf"))   || (strFileExt.equalsIgnoreCase("doc")) 
                            || (strFileExt.equalsIgnoreCase("docx")) || (strFileExt.equalsIgnoreCase("xls"))   
                            || (strFileExt.equalsIgnoreCase("xlsx")))
						{ 
							alFileAUploadList.add(finalPath);//3 This code adds Total path of the file
							iUpdate=managerObject.saveDocUploads(alFileAUploadList,userSeqId,seqId.toString(),origFileName,inputStream,formFileSize,referenceSeqId);
							if(iUpdate>0)
							{
								if(frmDocsUpload.get("mouDocSeqID")!=null)
								{
									//set the appropriate message
									request.setAttribute("updated","message.savedSuccessfully");
								}//end of if(frmDocsUpload.get("configLinkSeqID")!=null)
								else
								{
									//set the appropriate message
									request.setAttribute("updated","message.addedSuccessfully");
								}//end else
							}// end of if(iUpdate>0)
					}//end of if(strFileExt.equalsIgnoreCase("html") || strFileExt.equalsIgnoreCase("mhtml")||strFileExt.equalsIgnoreCase("msg") || strFileExt.equalsIgnoreCase("rar")|| strFileExt.equalsIgnoreCase("zip") || strFileExt.equalsIgnoreCase("pdf") || strFileExt.equalsIgnoreCase("doc") || strFileExt.equalsIgnoreCase("docx") || strFileExt.equalsIgnoreCase("txt") || strFileExt.equalsIgnoreCase("odt") || strFileExt.equalsIgnoreCase("jrxml") || strFileExt.equalsIgnoreCase("xls") || strFileExt.equalsIgnoreCase("xlsx") )
					else{
						strNotify="selected file should be any of these extensions (.pdf,.doc,.docx,.xls,.xlsx)";
					}//end ofelse(strFileExt.equalsIgnoreCase("pdf"))
				}//end of if(formFile.getFileSize()<=fileSize)
				else{
					strNotify="selected file size should be less than or equal to 4 MB";
				}
				}else{
					strNotify="Description is required.";
				}
			}
			request.setAttribute("seqId", seqId);
			request.setAttribute("typeId", typeId);
			frmDocsUpload.initialize(mapping);
			maintenanceObject=this.getMaintenanceManagerObject();
			if("PBM|CLM".equals(typeId)){
				preAuthSeqId=(long) request.getSession().getAttribute("preAuthSeqId");
				alLinkDetailsList = maintenanceObject.getDocsUploadsList(alLinkDetailsList,preAuthSeqId,"PBM|PAT");
			}
			alLinkDetailsList=managerObject.getDocsUploadsList(alLinkDetailsList,seqId,typeId);
			tableDataDocsUpload.setData(alLinkDetailsList);
			frmDocsUpload.set("caption",sbfCaption.toString());
			request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
			request.setAttribute("frmDocsUpload",frmDocsUpload);
			request.getSession().setAttribute("frmUpload", frmDocsUpload);
			request.setAttribute("notify",strNotify);
			return this.getForward(authData, mapping, request);
		}//end of try
		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request,mapping,new TTKException(exp,strUpload));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	private boolean checkAuthorization(String authData, ActionMapping mapping,
			HttpServletRequest request) throws TTKException {
		return isAuthorized(mapping.findForward(authData).getPath(), request);
	}
				//HttpServletResponse response)
	
	public ActionForward doUploadClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException
	{
		try
		{
			log.debug("Inside the doClose method of UploadMOUDocsAction");
			setLinks(request);
			String typeId = request.getParameter("typeId");
			DynaActionForm frmDocsUpload = (DynaActionForm) request.getSession().getAttribute("frmOnlinePreAuth");
			String activeLink = TTKCommon.getActiveLink(request);
			String activeSubLink = TTKCommon.getActiveSubLink(request);
			String activeTab =TTKCommon.getActiveTab(request);
			HttpSession session = request.getSession();
			System.out.println("activeLink close::"+activeLink);
			System.out.println("activeSubLink close::"+activeSubLink);
			System.out.println("activeTab close::"+activeTab);
			
			if("PAT".equals(typeId) || "CFD|PAT".equals(typeId)){
				boolean isPtrPat=false;
				if(request.getSession().getAttribute("isPtrPat")!=null)
				isPtrPat=(boolean) request.getSession().getAttribute("isPtrPat");
				if(isPtrPat==true){
					return mapping.findForward("preauthdetail");
				}else
				return mapping.findForward(strPreauthUploadClose);
			}
             if("CLM".equals(typeId) || "CFD|CLM".equals(typeId))
             return mapping.findForward(strClaimsUploadClose);
             if("HOS|CLM".equals(typeId))
             return mapping.findForward(strUploadsHOSCloseClaims);
             if("PTR|PAT".equals(typeId))
			if("CLM".equals(typeId))
			return mapping.findForward(strClaimsUploadClose);
			
			
			DynaActionForm frmOnlinePreAuth = (DynaActionForm)session.getAttribute("frmOnlinePreAuth");	
			
			if(TTKCommon.getActiveTab(request).equals("Pre-Auth Log"))
			{
				if(!"appealBtn".equals(frmOnlinePreAuth.get("appealBtn")))
				{	
					if("HOS|PAT".equals(typeId) && (! request.getSession().getAttribute("enhanceYN").equals("Y")) && (request.getSession().getAttribute("viewDocFlag").equals("view")))
					return mapping.findForward(strUploadsHOSClosePreAuthfromLogSearch);
					else if("HOS|PAT".equals(typeId) && (request.getSession().getAttribute("enhanceYN").equals("Y")) && (request.getSession().getAttribute("viewDocFlag").equals("view")))
						return mapping.findForward(strUploadsHOSClosePreAuthEnhancefromLogSearch);
					else if("HOS|PAT".equals(typeId) && (request.getSession().getAttribute("enhanceYN").equals("Y")) && (request.getSession().getAttribute("viewDocFlag").equals("upload")))
						return mapping.findForward(strUploadsHOSClosePreAuth);
				}
				else
				{
					if("HOS|PAT".equals(typeId))
					{
					 frmOnlinePreAuth= (DynaActionForm)request.getSession().getAttribute("frmOnlinePreAuth");
					String seqId = request.getParameter("seqId");
					seqId = (seqId==null) || (seqId.length()<1)? "0" :seqId;
					Long preAuthSeqId = Long.parseLong(seqId);
					
					OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
					CashlessDetailVO cashlessDetailVO = null;
					
					Object[] preauthAllresult	=	onlinePreAuthManager.getOnlinePartialPreAuthDetails(preAuthSeqId);
					cashlessDetailVO = (CashlessDetailVO) preauthAllresult[0];
					frmOnlinePreAuth.set("appealDocsYN",cashlessDetailVO.getAppealDocsYN());
					request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
					return mapping.findForward(strCloseUploadsHOSPreAuth);
					}
				}
			}else{
				if("HOS|PAT".equals(typeId))
					return mapping.findForward(strUploadsHOSClosePreAuth);
			}	
			
			if("HOS|CLM".equals(typeId))
			return mapping.findForward(strUploadsHOSCloseClaims);
			if("PTR|PAT".equals(typeId))
			{
				boolean isSubmitted=false;
				isSubmitted=(boolean) request.getSession().getAttribute("isSubmitted");
				if(isSubmitted==false){
					request.setAttribute("savePartialPreAuthFlag", "savePartialPreAuth");
					return mapping.findForward("onlinePreAuth");
				}else
             return mapping.findForward(strUploadsPTRClosePreAuth);
			}
			if("PTR|CLM".equals(typeId))
			return mapping.findForward(strUploadsPTRCloseClaims);
			if("PBM|PAT".equals(typeId)){
				String isDispenced=(String) request.getSession().getAttribute("isDispenced");
				if(isDispenced!=null&&isDispenced.equals("true")){
					return mapping.findForward(strUploadsPBMCloseClaims);
				}else{
					
					DynaActionForm frmPbmPreauthGeneral= (DynaActionForm)request.getSession().getAttribute("frmPbmPreauthGeneral"); 
					String seqId = request.getParameter("seqId");
					seqId = (seqId==null) || (seqId.length()<1)? "0" :seqId;
					Long preAuthSeqId = Long.parseLong(seqId);
					OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
					PbmPreAuthVO pbmPreAuthVO=providerManagerObj.getAllPreAuthDetails(preAuthSeqId);
					
					
					frmPbmPreauthGeneral.set("appealDocsYN",pbmPreAuthVO.getAppealDocsYN());
					request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
					return mapping.findForward(strUploadsPBMClosePreAuth);
				}
			}
			
			if("PBM|CLM".equals(typeId))
			return mapping.findForward(strUploadsPBMCloseClaims);
			
			return null;
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strUpload));
		}//end of catch(Exception exp)
	}//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				//HttpServletResponse response)
	
	public ActionForward doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{
			String typeId = request.getParameter("typeId");
			Long seqId = Long.parseLong(request.getParameter("seqId"));
			request.setAttribute("seqId", seqId);
			request.setAttribute("typeId", typeId);
			long preAuthSeqId=0;
			if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
			{
			setLinks(request);
			log.debug("Inside PreauthAction doDelete");
			StringBuffer sbfDeleteId = new StringBuffer();
			ArrayList <Object>alCertificateDelete=new ArrayList<Object>();
			ArrayList <Object>alAssociatedCertificatesList=new ArrayList<>();
			DynaActionForm frmDocsUpload=(DynaActionForm)form;
			StringBuffer sbfCaption= new StringBuffer((String) request.getSession().getAttribute("caption"));
			MaintenanceManager managerObject=this.getMaintenanceManagerObject();
			TableData tableDataDocsUpload = null;
			
			//get the Table Data From the session 
			if(request.getSession().getAttribute("tableDataDocsUpload")!=null)
			{
				tableDataDocsUpload=(TableData)(request.getSession()).getAttribute("tableDataDocsUpload");
			}//end of if(request.getSession().getAttribute("tableDataLinkDetails")!=null)
			else
			{
				tableDataDocsUpload=new TableData();
			}//end of else
			sbfDeleteId.append(populateDeleteIds(request, (TableData)request.getSession().getAttribute("tableDataDocsUpload")));
			alCertificateDelete.add(String.valueOf(sbfDeleteId));
			frmDocsUpload.initialize(mapping);
			int iCount=managerObject.deleteDocsUpload(alCertificateDelete,"tableDataDocsUpload");
			log.info("iCount :"+iCount);
			//refresh the data in order to get the new records if any.
			
			managerObject=this.getMaintenanceManagerObject();
			//DynaActionForm frmClaimGeneral=null;
			String preAuthSeqID	=	(String)request.getSession().getAttribute("preAuth_seq_id_docs_uploads");
			frmDocsUpload=(DynaActionForm) request.getSession().getAttribute("frmDocsUpload");
			MaintenanceManager maintenanceObject=null;
			maintenanceObject=this.getMaintenanceManagerObject();
						if("PBM|CLM".equals(typeId)){
							preAuthSeqId=(long) request.getSession().getAttribute("preAuthSeqId");
							alAssociatedCertificatesList = maintenanceObject.getDocsUploadsList(alAssociatedCertificatesList,preAuthSeqId,"PBM|PAT");
						}
			alAssociatedCertificatesList = managerObject.getDocsUploadsList(alAssociatedCertificatesList,seqId,typeId);
			tableDataDocsUpload.setData(alAssociatedCertificatesList,"search");
			request.getSession().setAttribute("tableDataDocsUpload",tableDataDocsUpload);
			request.setAttribute("frmDocsUpload",frmDocsUpload);
			frmDocsUpload.set("caption",sbfCaption.toString());
			
			}//End of if(request.getSession().getAttribute("tableDataDocsUpload")!=null)
			if(("HOS|PAT".equals(typeId))||(("HOS|CLM".equals(typeId))))
			return this.getForward(strOnlineDocsUpload,mapping,request);
			if(("PTR|PAT".equals(typeId))||("PTR|CLM".equals(typeId)))
			return this.getForward(strOnlinePtrDocsUpload, mapping, request);
			else if(("PBM|PAT".equals(typeId))||("PBM|CLM".equals(typeId)))
				return this.getForward("OnlinePbmDocsUpload", mapping, request);
			else
			return this.getForward(strDocsUpload, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strUpload));
			}//end of catch(Exception exp)
		}//end of doDelete(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	
	
	public ActionForward doViewUploadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException{
		try{	    		
    		log.info("inside Reports Action doViewFiles method");
    		setOnlineLinks(request);
    		MaintenanceManager managerObject=this.getMaintenanceManagerObject();
    		response.setContentType("application/octet-stream");
    		String strFile = "";
    		String rownum =request.getParameter("rownum");	    		
    		PreAuthDetailVO preAuthDetailVO	=null;
    		TableData tableData	=	null;
   			tableData = (TableData) request.getSession().getAttribute("tableDataDocsUpload");
    		preAuthDetailVO = (PreAuthDetailVO)tableData.getRowInfo(Integer.parseInt(rownum));     
    		strFile	=preAuthDetailVO.getMouDocPath();
    		strFile = strFile.toLowerCase();
    		if(strFile.endsWith("doc") || strFile.endsWith("docx"))
    		{
    			response.setContentType("application/msword");
    		}//end of if(strFile.endsWith("doc"))
    		else if(strFile.endsWith("pdf"))
    		{
    			response.setContentType("application/pdf");
    		}//end of else if(strFile.endsWith("pdf"))
    		else if(strFile.endsWith("xls") || strFile.endsWith("xlsx"))
    		{
    			response.setContentType("application/vnd.ms-excel");	
    		}//end of else if(strFile.endsWith("xls"))
    		else if(strFile.endsWith("png"))
    		{
    			response.setContentType("image/png");	
    		}
    		else if(strFile.endsWith("jpg") || strFile.endsWith("jpeg"))
    		{
    			response.setContentType("image/jpeg");	
    		}
    		response.addHeader("Content-Disposition","attachment; filename="+strFile);
    		if(strFile.endsWith("xls") || strFile.endsWith("xlsx")||strFile.endsWith("pdf")||strFile.endsWith("doc") || strFile.endsWith("docx") || strFile.endsWith("png") || strFile.endsWith("jpg") || strFile.endsWith("jpeg")){
    			Blob blobData=managerObject.getFile(String.valueOf(preAuthDetailVO.getMouDocSeqID()));
//    			if(resultSet != null){
//    				while(resultSet.next()){	
//    	            Blob blobData=resultSet.getBlob("IMAGE_FILE");
    			if(blobData!=null){
    				InputStream in = blobData.getBinaryStream();
    	            OutputStream outputStream=response.getOutputStream();
    	            try {
    	                int c;
    	                while ((c = in.read()) != -1) {
    	                	outputStream.write(c);
    	                }
    	             }finally {
    	                if (in != null) {
    	                   in.close();
    	                }
    	                if (outputStream != null) {
    	                	outputStream.close();
    	                }
    	             }
    			}
    	            
//    					}
//    				}
    		}
    	}//end of try
    	catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
    	}//end of catch(Exception exp)
    	return null;
	}
	/**
	 * Returns a string which contains the | separated sequence id's to be deleted
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 * @param tableData TableData object which contains the value objects
	 * @return String which contains the | separated sequence id's to be delete
	 * @throws TTKException If any run time Excepton occures
	 */
	private String populateDeleteIds(HttpServletRequest request, TableData tableDataDocsUpload)throws TTKException
	{
		if(request.getSession().getAttribute("tableDataDocsUpload")!=null) 
		{			
			String[] strChk = request.getParameterValues("chkopt");
			StringBuffer sbfDeleteId = new StringBuffer();
			if(strChk!=null&&strChk.length!=0)
			{
				for(int i=0; i<strChk.length;i++)
				{
					if(strChk[i]!=null)
					{
						//extract the sequence id to be deleted from the value object
						if(i == 0)
						{
							sbfDeleteId.append("|").append(String.valueOf(((PreAuthDetailVO)tableDataDocsUpload.getData().get(Integer.parseInt(strChk[i]))).getMouDocSeqID()));
						}//end of if(i == 0)
						else
						{
							sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((PreAuthDetailVO)tableDataDocsUpload.getData().get(Integer.parseInt(strChk[i]))).getMouDocSeqID().intValue()));
						}//end of else
					}//end of if(strChk[i]!=null)
				}//end of for(int i=0; i<strChk.length;i++)
				sbfDeleteId=sbfDeleteId.append("|");
			}//end of if(strChk!=null&&strChk.length!=0)
			return String.valueOf(sbfDeleteId);
		}//end of populateDeleteId(HttpServletRequest request, TableData tableData)
						
	else{
		
		String[] strChk = request.getParameterValues("chkopt");
		StringBuffer sbfDeleteId = new StringBuffer();
		if(strChk!=null&&strChk.length!=0)
		{
			for(int i=0; i<strChk.length;i++)
			{
				if(strChk[i]!=null)
				{
					//extract the sequence id to be deleted from the value object
					if(i == 0)
					{
						sbfDeleteId.append("|").append(String.valueOf(((TdsCertificateVO)tableDataDocsUpload.getData().
								get(Integer.parseInt(strChk[i]))).getCertSeqId()));
					}//end of if(i == 0)
					else
					{
						sbfDeleteId = sbfDeleteId.append("|").append(String.valueOf(((TdsCertificateVO)tableDataDocsUpload.getData().
								get(Integer.parseInt(strChk[i]))).getCertSeqId().intValue()));
					}//end of else
				}//end of if(strChk[i]!=null)
			}//end of for(int i=0; i<strChk.length;i++)
			sbfDeleteId=sbfDeleteId.append("|");
		}//end of if(strChk!=null&&strChk.length!=0)
		return String.valueOf(sbfDeleteId);
	}//end of populateDeleteId(HttpServletRequest request, TableData tableData)

}//else
	
	
	
	
	 /**
		 * Returns the MaintenanceManager session object for invoking methods on it.
		 * @return MaintenanceManager session object which can be used for method invokation
		 * @exception throws TTKException
		 */
		private MaintenanceManager getMaintenanceManagerObject() throws TTKException
		{
			MaintenanceManager maintenanceManager = null;
			try
			{
				if(maintenanceManager == null)
				{
					InitialContext ctx = new InitialContext();
					maintenanceManager = (MaintenanceManager) ctx.lookup("java:global/TTKServices/business.ejb3/MaintenanceManagerBean!com.ttk.business.maintenance.MaintenanceManager");
				}//end if(maintenanceManager == null)
			}//end of try
			catch(Exception exp)
			{
				throw new TTKException(exp, strUpload);
			}//end of catch
			return maintenanceManager;
		}//end getMaintenanceManagerObject()
		
		 private OnlinePreAuthManager getOnlineAccessManagerObject() throws TTKException
		    {
		    	OnlinePreAuthManager onlinePreAuthManager= null;
		        try
		        {
		            if(onlinePreAuthManager == null)
		            {
		                InitialContext ctx = new InitialContext();
		                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
		                onlinePreAuthManager = (OnlinePreAuthManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlinePreAuthManagerBean!com.ttk.business.onlineforms.OnlinePreAuthManager");
		            }//end if
		        }//end of try
		        catch(Exception exp)
		        {
		            throw new TTKException(exp, strUpload);
		        }//end of catch
		        return onlinePreAuthManager;
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
	
}
