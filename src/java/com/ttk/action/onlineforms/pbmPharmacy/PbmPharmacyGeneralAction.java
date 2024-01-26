package com.ttk.action.onlineforms.pbmPharmacy; 
/**
 * @ (#) PbmPharmacyGeneralAction.java 27 MAR 2017
 * Project      : TTK HealthCare Services
 * File         : PbmPharmacyGeneralAction.java
 * Author       : Nagababu K
 * Company      : RCS Technologies
 * Date Created : 27 MAR 2017
 *
 * @author       :  Nagababu K
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import javax.jms.Session;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.ttk.action.TTKAction;
import com.ttk.business.common.CeedSoapSaajClient;
import com.ttk.business.maintenance.MaintenanceManager;
import com.ttk.business.onlineforms.OnlineAccessManager;
import com.ttk.business.onlineforms.OnlinePreAuthManager;
import com.ttk.business.onlineforms.providerLogin.OnlinePbmProviderManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.CashlessVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for Searching the List Policies to see the Online Account Info.
 * This also provides deletion and updation of products.
 */
public class PbmPharmacyGeneralAction extends TTKAction {

    private static final Logger log = Logger.getLogger( PbmPharmacyGeneralAction.class );


    // Action mapping forwards.
    private static final String strDashBoard="dashBoardDetails";
    private static final String strPreauthProccess="preauthProccess";
    
    private static final String strRequestAuthorization="requestAuthorization";
    private static final String strPBMClaimSubmission="pbmClaimSubmission";
    private static final String strPBMClaimSuccess="PbmClaimSuccess";
    private static final String strPBMClaimSubmitted="pbmClaimSubmitted";
    private static final String claimRedirect="PbmClaimRedirect";
    private static final String strUpload="upload";
    private static final String strPbmOutPutScreen=	"PbmApealOutPutScreen";
    private static final String strCheckEligibility="checkEligibilityPBM";
    private static final String strVlaidateCheckEligibiliy="vlaidateEligibility";
    
   public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doDefault method of PbmPharmacyGeneralAction");
			setOnlineLinks(request);
			
		//request.getSession().setAttribute("pbmRightClick", TTKPropertiesReader.getPropertyValue("right.click.disable"));
			
			return this.getForward(strDashBoard, mapping, request);
			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
		}//end of catch(Exception exp)
   }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   

   public ActionForward doPreauthProccess(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doPreauthProccess method of PbmPharmacyGeneralAction");
			setOnlineLinks(request);		
			
            DynaActionForm frmPbmPreauthGeneral =(DynaActionForm)form;
            frmPbmPreauthGeneral.initialize(mapping);     //reset the form data
            request.setAttribute("enrollId", null);
			request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
			request.getSession().removeAttribute("benifitTypeID");
		
		return this.getForward(strCheckEligibility, mapping, request);
		
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
		}//end of catch(Exception exp)
   }//end of doPreauthProccess(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   
   
   public ActionForward doValidateGeneralDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doValidateGeneralDetails method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
		
		HttpSession session=request.getSession();
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
		OnlinePbmProviderManager providerManagerObj=this.getPbmManagerObject();
		Long hospSeqID=(Long)session.getAttribute("hospSeqID");
		Long hospUserSeqID=(Long)session.getAttribute("hospUserSeqID");
		PbmPreAuthVO pbmPreAuthVO = (PbmPreAuthVO) FormUtils.getFormValues(frmPbmPreauthGeneral, this, mapping, request);
		if(pbmPreAuthVO.getEnrolmentID()==null||pbmPreAuthVO.getEnrolmentID().length()<1){
			pbmPreAuthVO.setEnrolmentID(pbmPreAuthVO.getQatarID());
		}
		Long memSeqID=pbmPreAuthVO.getMemSeqID();
		pbmPreAuthVO.setHospitalSeqID(new Long(hospSeqID));
		pbmPreAuthVO.setAddedBy(new Long(hospUserSeqID));
		pbmPreAuthVO.setEventRefNo(frmPbmPreauthGeneral.getString("eventRefNo"));		
		//  
		//Long preAuthSeqID=providerManagerObj.addPreAuthGeneralDetails(pbmPreAuthVO);
		 pbmPreAuthVO=providerManagerObj.validateMemberID(pbmPreAuthVO);
		
		 if(memSeqID==null){
			request.setAttribute("success","Eligibility details validated successfully");
		}else{
			request.setAttribute("success","Eligibility details updated successfully");	
			}	
		
			/* Capturing log start*/
		 	OnlineAccessManager onlineAccessManager = null;
			onlineAccessManager = this.getOnlineAccessManagerObject();
			ArrayList<String> dataList=new ArrayList<>();
			dataList.add("PBM");
			dataList.add(pbmPreAuthVO.getEnrolmentID());
			dataList.add(null);
			dataList.add(pbmPreAuthVO.getDateOfTreatment());
			dataList.add("YES");
			dataList.add(hospSeqID+"");
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(userSecurityProfile.getUSER_SEQ_ID()+"");
			dataList.add(userSecurityProfile.getHospitalName()+"");
			dataList.add(userSecurityProfile.getEmpanelNumber()+"");
			dataList.add(pbmPreAuthVO.getEventRefNo());
			dataList.add(null);
			onlineAccessManager.updateLogTable(dataList);
			/* Capturing log end*/
		frmPbmPreauthGeneral = (DynaActionForm) FormUtils.setFormValues("frmPbmPreauthGeneral", pbmPreAuthVO, this, mapping,request);
		 ArrayList<DiagnosisDetailsVO> allIcdDetails=(ArrayList<DiagnosisDetailsVO>)session.getAttribute("allPbmIcdDetails");
		 ArrayList<ActivityDetailsVO> allDrugDetails=(ArrayList<ActivityDetailsVO>)session.getAttribute("allPbmDrugDetails");
		
		 frmPbmPreauthGeneral.set("allIcdDetails", allIcdDetails);
		frmPbmPreauthGeneral.set("allDrugDetails", allDrugDetails);
		request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		PbmPreAuthVO pbmPreAuthVO1 = (PbmPreAuthVO) FormUtils.getFormValues(frmPbmPreauthGeneral, this, mapping, request);
		return this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doValidateGeneralDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   public ActionForward addIcdDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the addIcdDetails method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);
		
		HttpSession session=request.getSession();
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		
		DiagnosisDetailsVO diagnosisDetailsVO=new DiagnosisDetailsVO();
		
		diagnosisDetailsVO.setIcdCode(frmPbmPreauthGeneral.getString("icdCode"));
		diagnosisDetailsVO.setIcdCodeSeqId(new Long(frmPbmPreauthGeneral.getString("icdCodeSeqID")));
		diagnosisDetailsVO.setAilmentDescription(frmPbmPreauthGeneral.getString("icdCodeDesc"));
		
		DynaActionForm sessionFrmPbmPreauthGeneral=(DynaActionForm)session.getAttribute("frmPbmPreauthGeneral");
		
		//ArrayList<DiagnosisDetailsVO> allIcdDetails=(ArrayList<DiagnosisDetailsVO>)session.getAttribute("allPbmIcdDetails");
		ArrayList<DiagnosisDetailsVO> allIcdDetails=(ArrayList<DiagnosisDetailsVO>)sessionFrmPbmPreauthGeneral.get("allIcdDetails");
		 if(allIcdDetails==null)allIcdDetails=new ArrayList<>();
		
		 if(allIcdDetails.size()==0)	diagnosisDetailsVO.setPrimaryAilment("Principal");
		 else diagnosisDetailsVO.setPrimaryAilment("Secondary");
		
		 allIcdDetails.add(diagnosisDetailsVO);
		
		  request.setAttribute("success","Diagnosis details added successfully");
		
		  frmPbmPreauthGeneral.set("icdCodeDesc","");
		  frmPbmPreauthGeneral.set("icdCode","");
		  frmPbmPreauthGeneral.set("icdCodeSeqID","");
		  frmPbmPreauthGeneral.set("allIcdDetails", allIcdDetails);
		//session.setAttribute("allPbmIcdDetails", allIcdDetails);
		session.setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		
		return this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of addIcdDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  
   public ActionForward addDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the addDrugDetails method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
		OnlinePbmProviderManager providerManagerObj=this.getPbmManagerObject();

		HttpSession session=request.getSession();
		
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		
		ActivityDetailsVO activityDetailsVO=null;
		PbmPreAuthVO pbmPreAuthVO=new PbmPreAuthVO();
	
		Long hospSeqID=(Long)session.getAttribute("hospSeqID");
		
		pbmPreAuthVO.setHospitalSeqID(hospSeqID);
		pbmPreAuthVO.setEnrolmentID(frmPbmPreauthGeneral.getString("enrolmentID"));
	//	pbmPreAuthVO.setDrugCodeSeqID(new Long(frmPbmPreauthGeneral.getString("drugCodeSeqID")));		// drug code
		pbmPreAuthVO.setDrugCode(frmPbmPreauthGeneral.getString("drugCodeSeqID"));						// drug code
		pbmPreAuthVO.setDrugCodeQuantity(frmPbmPreauthGeneral.getString("drugCodeQuantity"));

		pbmPreAuthVO.setDateOfTreatment(frmPbmPreauthGeneral.getString("dateOfTreatment"));
		pbmPreAuthVO.setUnitType(frmPbmPreauthGeneral.getString("unitType"));
		activityDetailsVO=providerManagerObj.getTariffDetails(pbmPreAuthVO);
		if(activityDetailsVO==null){
			activityDetailsVO=new ActivityDetailsVO();
		}
		String quantity=frmPbmPreauthGeneral.getString("quantity");
		String unitType=frmPbmPreauthGeneral.getString("unitType");
		String drugCodeQuantity = frmPbmPreauthGeneral.getString("drugCodeQuantity");
		  BigDecimal bdDiscAmt=activityDetailsVO.getDiscount();
		  BigDecimal bdGrossAmt=activityDetailsVO.getGrossAmount();
		  BigDecimal granularQty = null;
		  
		  if(bdDiscAmt!=null){
			  bdDiscAmt=bdDiscAmt.multiply(new BigDecimal(quantity));
		}else{
			  bdDiscAmt=new BigDecimal(0);
		}
		
		if(bdGrossAmt!=null){
			bdGrossAmt=bdGrossAmt.multiply(new BigDecimal(quantity));
		  }else{
			  bdGrossAmt=new BigDecimal(0);
		  }
		
		if(unitType.equalsIgnoreCase("LOSE")){
			granularQty = new BigDecimal(quantity);
			
			
		}else if(unitType.equalsIgnoreCase("PCKG")){
			drugCodeQuantity=("".equals(TTKCommon.checkNull(drugCodeQuantity))?"1":drugCodeQuantity);
			granularQty = new BigDecimal(drugCodeQuantity).multiply(new BigDecimal(quantity));
			
		}
		
		activityDetailsVO.setDiscount(bdDiscAmt);
		activityDetailsVO.setGrossAmount(bdGrossAmt);
		activityDetailsVO.setActivityCodeDesc(frmPbmPreauthGeneral.getString("drugDesc"));
		activityDetailsVO.setNetAmount(bdGrossAmt.subtract(bdDiscAmt));
		activityDetailsVO.setQuantity(new Float(quantity));
	//	activityDetailsVO.setActivityCodeSeqId(new Long(frmPbmPreauthGeneral.getString("drugCodeSeqID")));		
		activityDetailsVO.setActivityCodeSeqId(activityDetailsVO.getActivityCodeSeqId());
		activityDetailsVO.setUnitType(frmPbmPreauthGeneral.getString("unitType"));
		activityDetailsVO.setMedicationDays(new Integer(frmPbmPreauthGeneral.getString("days")));
		activityDetailsVO.setDrugCodeQuantity(new Float(granularQty.toString()));
		//frmPbmPreauthGeneral.set("drugCodeQuantity",drugCodeQuantity);
		
		DynaActionForm sessionFrmPbmPreauthGeneral=(DynaActionForm)session.getAttribute("frmPbmPreauthGeneral");
		
		ArrayList<ActivityDetailsVO> allPbmDrugDetails=(ArrayList<ActivityDetailsVO>)sessionFrmPbmPreauthGeneral.get("allDrugDetails");
		
		if(allPbmDrugDetails==null)allPbmDrugDetails=new ArrayList<>();
		
		 allPbmDrugDetails.add(activityDetailsVO);		 
		 
		 request.setAttribute("success","Drug details added successfully");   
		
		 frmPbmPreauthGeneral.set("days","");
		 frmPbmPreauthGeneral.set("unitType","");
		 frmPbmPreauthGeneral.set("drugDesc","");
		 frmPbmPreauthGeneral.set("quantity","");
		 frmPbmPreauthGeneral.set("drugCodeSeqID","");
		 frmPbmPreauthGeneral.set("drugCodeQuantity","");
		 
		 
		 frmPbmPreauthGeneral.set("allDrugDetails", allPbmDrugDetails);		 
		session.setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		
		return this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of addDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   
   
   //pbmClaimSubmission
   public ActionForward doPBMSubmitClaim(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doPBMSubmitClaim method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		HttpSession session=request.getSession();
		String strNotify="";
		Pattern pattern =null;
		Matcher matcher =null;
		int iUpdate	=	0;
		PbmPreAuthVO pbmPreAuthVO = (PbmPreAuthVO) FormUtils.getFormValues(frmPbmPreauthGeneral, this, mapping, request);
		OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
		String[]drucCheckValues=request.getParameterValues("drucCheck");
		
		StringBuilder builder=new StringBuilder();
		if(drucCheckValues!=null){
			//builder.append("|");
			for(String strCheckValue:drucCheckValues){
				builder.append(strCheckValue);
				builder.append("|");
			}
		}
		ArrayList<Object> claimData=new ArrayList<Object>();
		
		claimData.add(pbmPreAuthVO.getPreAuthSeqID());
		claimData.add(builder.toString());
		claimData.add(frmPbmPreauthGeneral.get("invoiceNo"));
	
		 			
		 			 //Store file in database
		 			FormFile formFile = null;
		 			formFile = (FormFile)frmPbmPreauthGeneral.get("pdfUploadFile");
		 			if(formFile.getFileSize()!=0){
		 				String fileDesc	=	(String)frmPbmPreauthGeneral.get("fileTypeUpload");
			 			MaintenanceManager managerObject=this.getMaintenanceManagerObject();
						int fileSize=4*1024*1024;
						TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
						DateFormat df =new SimpleDateFormat("ddMMyyyy HH:mm:ss:S");
						df.setTimeZone(tz);  
						UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
						Long referenceSeqId = null;
						referenceSeqId = userSecurityProfile.getHospSeqId();
						ArrayList<Object> alFileAUploadList = new ArrayList<Object>();
						alFileAUploadList.add(TTKCommon.getUserSeqId(request));//0
						Long userSeqId	=	(Long) TTKCommon.getUserSeqId(request);
						
//						String sourceid	=	(String)frmPbmPreauthGeneral.get("source_id");
						pattern = Pattern.compile( "[^a-zA-Z0-9_\\.\\-\\s*]" );
						matcher = pattern.matcher( formFile.getFileName() );
						String 	strTimeStamp=((df.format(Calendar.getInstance(tz).getTime())).replaceAll(" ", "_")).replaceAll(":", ""); 
						String fileName=strTimeStamp+"_"+formFile.getFileName();
						String origFileName	=	formFile.getFileName();
						InputStream inputStream	=	 formFile.getInputStream();//INPUT STREAM  FROM FILE UPLOADED
						int formFileSize	=	formFile.getFileSize();

							alFileAUploadList.add(fileName);//1
							alFileAUploadList.add(fileDesc);//2
							alFileAUploadList.add(pbmPreAuthVO.getClmSeqId());//3
							alFileAUploadList.add("PBM|CLM");//4
						
							String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("preauthDocsUploads"));
							String finalPath=(path+fileName);
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
										pbmPreAuthVO=providerManagerObj.getPBMSubmitClaim(claimData);
										
										if(pbmPreAuthVO.getClmSeqId()!=0L){
											String seqId=String.valueOf(pbmPreAuthVO.getClmSeqId());
											iUpdate=managerObject.saveDocUploads(alFileAUploadList,userSeqId,seqId,origFileName,inputStream,formFileSize,referenceSeqId);	
										}else{
											strNotify="Sorry, Your file has not uploaded. Please Contact Admin";
											request.setAttribute("errorMsg",strNotify);
										}
								}//end of if(strFileExt.equalsIgnoreCase("html") || strFileExt.equalsIgnoreCase("mhtml")||strFileExt.equalsIgnoreCase("msg") || strFileExt.equalsIgnoreCase("rar")|| strFileExt.equalsIgnoreCase("zip") || strFileExt.equalsIgnoreCase("pdf") || strFileExt.equalsIgnoreCase("doc") || strFileExt.equalsIgnoreCase("docx") || strFileExt.equalsIgnoreCase("txt") || strFileExt.equalsIgnoreCase("odt") || strFileExt.equalsIgnoreCase("jrxml") || strFileExt.equalsIgnoreCase("xls") || strFileExt.equalsIgnoreCase("xlsx") )
								else{
									strNotify="selected file should be any of these extensions (.pdf,.doc,.docx,.xls,.xlsx)";
									request.setAttribute("errorMsg",strNotify);
									return mapping.findForward(strPBMClaimSubmission);
								}//end ofelse(strFileExt.equalsIgnoreCase("pdf"))
							}//end of if(formFile.getFileSize()<=fileSize)
								
							else{
								strNotify="selected file size should be less than or equal to 4 MB";
								request.setAttribute("errorMsg",strNotify);
								return mapping.findForward(strPBMClaimSubmission);
							}
							}else{
								strNotify="Upload document type is required.";
								request.setAttribute("errorMsg",strNotify);
								return mapping.findForward(strPBMClaimSubmission);
							}
		 			}else{
		 				pbmPreAuthVO=providerManagerObj.getPBMSubmitClaim(claimData);
		 			}
					
		frmPbmPreauthGeneral.set("invoiceNo", "");
		frmPbmPreauthGeneral.set("clmNumber",pbmPreAuthVO.getClaimNo());
		request.setAttribute("successMsg", "claim submitted Successfully");
		request.setAttribute("isClmSubmitted", "true");
		session.setAttribute("pbmPreAuthVO", pbmPreAuthVO);
		return mapping.findForward(claimRedirect);
		//return this.getForward(claimRedirect, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPBMClaimSubmission));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   //redirect to claim details
   
   
   //pbmClaimSubmission
   public ActionForward doPBMClaimDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doPBMClaimDetails method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);
		
		DynaActionForm frmPbmClaimSuccess=(DynaActionForm)form;
		
		//PbmPreAuthVO pbmPreAuthVO = (PbmPreAuthVO) FormUtils.get
		//PbmPreAuthVO pbmPreAuthVO=new PbmPreAuthVO();
		HttpSession session=request.getSession();
		
		PbmPreAuthVO pbmPreAuthVO=(PbmPreAuthVO) session.getAttribute("pbmPreAuthVO");
		String decisionDateOfClaim=(String) request.getSession().getAttribute("decisionDateOfClaim");
		String transactionDt= (String) request.getSession().getAttribute("transactionDt");
		frmPbmClaimSuccess.set("invoiceNumber",pbmPreAuthVO.getInvoiceNo());
		frmPbmClaimSuccess.set("claimNumber",pbmPreAuthVO.getClaimNo());
		frmPbmClaimSuccess.set("claimedAmt",pbmPreAuthVO.getClaimedAmount());
		frmPbmClaimSuccess.set("transactionDt",transactionDt);
		frmPbmClaimSuccess.set("decisionDt",decisionDateOfClaim);
		frmPbmClaimSuccess.set("clinicianId",pbmPreAuthVO.getClinicianID());
		frmPbmClaimSuccess.set("clinicianName",pbmPreAuthVO.getClinicianName());
		
		if("INP".equals(pbmPreAuthVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "In Progress");
			frmPbmClaimSuccess.set("clmStatus","In Progress");		
		}else if("APR".equals(pbmPreAuthVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Approved");
			frmPbmClaimSuccess.set("clmStatus","Approved");
		}else if("REJ".equals(pbmPreAuthVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Rejected");
			frmPbmClaimSuccess.set("clmStatus","Rejected");
		}else if("PCN".equals(pbmPreAuthVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Cancel");
			frmPbmClaimSuccess.set("clmStatus","Cancel");
		}else if("REQ".equals(pbmPreAuthVO.getClaimStatus())){
			request.getSession().setAttribute("claimStatus", "Required Information");
			frmPbmClaimSuccess.set("clmStatus","Required Information");
		}else{
			request.getSession().setAttribute("claimStatus", pbmPreAuthVO.getClaimStatus());
			frmPbmClaimSuccess.set("clmStatus",pbmPreAuthVO.getClaimStatus());
		}
		request.getSession().setAttribute("preAuthStatus", null);
		frmPbmClaimSuccess.set("memberName",pbmPreAuthVO.getMemberName());
		frmPbmClaimSuccess.set("policyNo",pbmPreAuthVO.getPolicyNo());
		
		request.getSession().setAttribute("frmOnlineClaimsDetails", frmPbmClaimSuccess);
		String isClmSubmitted;
		isClmSubmitted=(String) request.getAttribute("isClmSubmitted") ;
		if(isClmSubmitted!=null&&isClmSubmitted.equals("true")){
			return this.getForward(strPBMClaimSubmitted, mapping, request);
		}else
		return mapping.findForward(strPBMClaimSuccess);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPBMClaimSubmission));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   
   
   
   
   
   //RedirectPbmPreToClaim
   
   public ActionForward doDispensed(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doDispensed method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
		HttpSession session=request.getSession();
		UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
		OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
		//String ceedResponse	=	"C:/Users/shruthi/Desktop/PBM/Soap.xml";		
		
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		Long hospUserSeqID=(Long)session.getAttribute("hospUserSeqID");
		
		PbmPreAuthVO pbmPreAuthVO = (PbmPreAuthVO) FormUtils.getFormValues(frmPbmPreauthGeneral, this, mapping, request);
		
		
		pbmPreAuthVO.setAddedBy(hospUserSeqID);
		
		DynaActionForm sessionFrmPbmPreauthGeneral=(DynaActionForm)session.getAttribute("frmPbmPreauthGeneral");
		ArrayList<DiagnosisDetailsVO> allIcdDetails=(ArrayList<DiagnosisDetailsVO>)sessionFrmPbmPreauthGeneral.get("allIcdDetails");
		ArrayList<ActivityDetailsVO> allPbmDrugDetails=(ArrayList<ActivityDetailsVO>)sessionFrmPbmPreauthGeneral.get("allDrugDetails");
		pbmPreAuthVO.setIcdDetails(allIcdDetails);
		pbmPreAuthVO.setDrugDetails(allPbmDrugDetails);
		
		
		Long preAuthSeqID=pbmPreAuthVO.getPreAuthSeqID();
		pbmPreAuthVO.setPreAuthSeqID(preAuthSeqID);
	
		
		String ClaimFlag=providerManagerObj.checkClaimElegibility(preAuthSeqID);
		pbmPreAuthVO=providerManagerObj.getAllPreAuthDetails(preAuthSeqID);
		
		frmPbmPreauthGeneral.initialize(mapping);
		
		frmPbmPreauthGeneral = (DynaActionForm) FormUtils.setFormValues("frmPbmPreauthGeneral", pbmPreAuthVO, this, mapping,request);
		
		
		frmPbmPreauthGeneral.set("caption", "["+pbmPreAuthVO.getPreAuthNO()+"]");
		    frmPbmPreauthGeneral.set("allIcdDetails", pbmPreAuthVO.getIcdDetails());
		    frmPbmPreauthGeneral.set("allDrugDetails", pbmPreAuthVO.getDrugDetails());
						   		   		
			request.getSession().setAttribute("closeMode", "dispensed");
			request.getSession().setAttribute("preAuthSeqId", preAuthSeqID);
	      request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		   request.getSession().setAttribute("preAuthStatus", pbmPreAuthVO.getPreAuthstatusDesc());
		  request.getSession().setAttribute("claimStatus", null);
		  
		if("Y".equals(ClaimFlag)){
			 ActionMessages actionMessages = new ActionMessages();
	            
	            ActionMessage actionMessage = new ActionMessage("error.preauth.preauthdetails.review");
	            actionMessages.add("global.error",actionMessage);
	            saveErrors(request,actionMessages);
	            request.getSession().setAttribute("closeMode", "View");
	            request.getSession().setAttribute("isDispenced", "false");
	            return this.getForward(strRequestAuthorization, mapping, request);
		}
		else
		{
			request.getSession().setAttribute("isDispenced", "true");
			return this.getForward(strPBMClaimSubmission, mapping, request);
		}
	}//end of try
	catch(TTKException expTTK)
	{
		request.getSession().setAttribute("isDispenced", "false");
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			request.getSession().setAttribute("isDispenced", "false");
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPBMClaimSubmission));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

  
   public ActionForward doClear(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doClear method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		frmPbmPreauthGeneral.initialize(mapping);
		 HttpSession session=request.getSession();  
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
	     String toDayDate=dateFormat.format(new Date());
	     String  enrollId  =  (String) session.getAttribute("enrollId");  
	     String  qatarID  =  (String) session.getAttribute("qatarID");  
	    frmPbmPreauthGeneral.set("enrolmentID", enrollId);
	    frmPbmPreauthGeneral.set("qatarID", qatarID);
	    frmPbmPreauthGeneral.set("dateOfTreatment", toDayDate);
		request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		return this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   public ActionForward doRequstAuthorization(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
	    String closeFlag="PBM";
		log.debug("Inside the doPreauthProccess method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
		HttpSession session=request.getSession();
		OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
		//String ceedResponse	=	"C:/Users/shruthi/Desktop/PBM/Soap.xml";
		
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		Long hospUserSeqID=(Long)session.getAttribute("hospUserSeqID");
		PbmPreAuthVO pbmPreAuthVO = (PbmPreAuthVO) FormUtils.getFormValues(frmPbmPreauthGeneral, this, mapping, request);
		
		pbmPreAuthVO.setAddedBy(hospUserSeqID);
		
		DynaActionForm sessionFrmPbmPreauthGeneral=(DynaActionForm)session.getAttribute("frmPbmPreauthGeneral");
		Long referenceSeqId = null;
		String fileName=null;
		String fileDesc=null;
		int fileSize=4*1024*1024;
		FormFile formFile = (FormFile)sessionFrmPbmPreauthGeneral.get("file");
		InputStream inputStream	=null;
		inputStream=formFile.getInputStream();//INPUT STREAM  FROM FILE UPLOADED
		ArrayList<DiagnosisDetailsVO> allIcdDetails=(ArrayList<DiagnosisDetailsVO>)sessionFrmPbmPreauthGeneral.get("allIcdDetails");
		ArrayList<ActivityDetailsVO> allPbmDrugDetails=(ArrayList<ActivityDetailsVO>)sessionFrmPbmPreauthGeneral.get("allDrugDetails");
		pbmPreAuthVO.setIcdDetails(allIcdDetails);
		pbmPreAuthVO.setDrugDetails(allPbmDrugDetails);

		Long preAuthSeqID=0L;
		String strNotify="";
					if(inputStream.available()!=0){
							/* storing file into batabase*/
							String strFileExt=formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1,formFile.getFileName().length());
							fileDesc=(String) sessionFrmPbmPreauthGeneral.get("description");
							if(!(fileDesc==null||fileDesc.trim().equals(""))){
								if(formFile.getFileSize()<=fileSize)
								{
									if((strFileExt.equalsIgnoreCase("pdf"))   || (strFileExt.equalsIgnoreCase("doc")) 
				                            || (strFileExt.equalsIgnoreCase("docx")) || (strFileExt.equalsIgnoreCase("xls"))   
				                            || (strFileExt.equalsIgnoreCase("xlsx")))
										{
										preAuthSeqID=providerManagerObj.requstAuthorization(pbmPreAuthVO);
										MaintenanceManager managerObject=this.getMaintenanceManagerObject();
										ArrayList<Object> alFileAUploadList = new ArrayList<Object>();
										fileName	=	formFile.getFileName();
										UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
										Long userSeqId	=	(Long) TTKCommon.getUserSeqId(request);
										int iUpdate=0;
										int formFileSize	=	formFile.getFileSize();
										alFileAUploadList.add(TTKCommon.getUserSeqId(request));
										alFileAUploadList.add(fileName);//1
										alFileAUploadList.add(fileDesc);//2
										alFileAUploadList.add(preAuthSeqID);//3
										alFileAUploadList.add("PBM|PAT");//4
										iUpdate=managerObject.saveDocUploads(alFileAUploadList,userSeqId,String.valueOf(preAuthSeqID),fileName,inputStream,formFileSize,referenceSeqId);
										}else{
											strNotify="selected file should be any of these extensions (.pdf,.doc,.docx,.xls,.xlsx)";
										}//end ofelse
								
								}else{
									strNotify="selected file size should be less than or equal to 4 MB";
								}
								}else{
								strNotify="Description is required.";
							}
							request.setAttribute("notify",strNotify);
							/*storing file into databse end*/
					}else{
						preAuthSeqID=providerManagerObj.requstAuthorization(pbmPreAuthVO);
					}
					pbmPreAuthVO.setPreAuthSeqID(preAuthSeqID);
					
			/*		webservices start	
		String responseData=CeedSoapSaajClient.execute("1",pbmPreAuthVO);
		SOAPMessage soapMessageinput=CeedSoapSaajClient.createSOAPRequest(pbmPreAuthVO);
	
     //   CeedSoapSaajClient.writeFileToDisk(responseData,ceedResponse);//RESPONSE XML WRITE TO FILE
        
          //READING XML RESPONSE FROM DESKTOP FILE
            SAXReader reader	=	new SAXReader();
            Document doc		=	(Document) reader.read(new StringReader(responseData));
           
            pbmPreAuthVO.setResponsePBMXML(responseData);            
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapMessageinput.writeTo(out);
            String input= out.toString();           
            String soapMessagein = input;
            pbmPreAuthVO.setInputPBMXML(soapMessagein);          
            
            //  
            //  
    		long Webservice=providerManagerObj.requstAuthWebservice(preAuthSeqID,pbmPreAuthVO);
		
    		webservices End		*/

		long PrauthWebservice=providerManagerObj.requstComp_preauth(preAuthSeqID,pbmPreAuthVO);
		if(strNotify.equals("")){	
		pbmPreAuthVO=providerManagerObj.getAllPreAuthDetails(preAuthSeqID);
		}
		frmPbmPreauthGeneral.initialize(mapping);
		frmPbmPreauthGeneral = (DynaActionForm) FormUtils.setFormValues("frmPbmPreauthGeneral", pbmPreAuthVO, this, mapping,request);
		
		frmPbmPreauthGeneral.set("allIcdDetails", pbmPreAuthVO.getIcdDetails());
		frmPbmPreauthGeneral.set("allDrugDetails", pbmPreAuthVO.getDrugDetails());
		request.getSession().setAttribute("preAuthSeqID", preAuthSeqID);
		request.getSession().setAttribute("closeMode", "Process");
		request.getSession().setAttribute("closeFlag",closeFlag);
		request.getSession().setAttribute("preAuthStatus", pbmPreAuthVO.getPreAuthstatusDesc()); 
		request.getSession().setAttribute("claimStatus", null); 
		request.getSession().setAttribute("isDispenced", "false");
		request.getSession().setAttribute("isLogSearch", "false");
		request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		if(!strNotify.equals("")){
			return mapping.findForward("preauthProccess");
		}
		return this.getForward(strRequestAuthorization, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

   public ActionForward getPreAuthData(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doPreauthProccess method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
		OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
		
		DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
		
		String testPreAuthSeqID=request.getParameter("testPreAuthSeqID");
		if(testPreAuthSeqID!=null&&testPreAuthSeqID.trim().length()>1){
		PbmPreAuthVO pbmPreAuthVO=providerManagerObj.getAllPreAuthDetails(new Long(testPreAuthSeqID));
		
		frmPbmPreauthGeneral.initialize(mapping);
		
		frmPbmPreauthGeneral = (DynaActionForm) FormUtils.setFormValues("frmPbmPreauthGeneral", pbmPreAuthVO, this, mapping,request);
		frmPbmPreauthGeneral.set("allIcdDetails", pbmPreAuthVO.getIcdDetails());
		frmPbmPreauthGeneral.set("allDrugDetails", pbmPreAuthVO.getDrugDetails());
		request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		
		
		}
		return this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   public ActionForward deleteDiagnosisDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the deleteDiagnosisDetails method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
				
		HttpSession session=request.getSession();
		
		String strIndexID=request.getParameter("indexID");
		DynaActionForm sessionFrmPbmPreauthGeneral=(DynaActionForm)session.getAttribute("frmPbmPreauthGeneral");
		 ArrayList<DiagnosisDetailsVO> allIcdDetails=(ArrayList<DiagnosisDetailsVO>)sessionFrmPbmPreauthGeneral.get("allIcdDetails");
		 if(allIcdDetails!=null){
		 allIcdDetails.remove(new Integer(strIndexID).intValue());
		
		 }
		
		 if(allIcdDetails!=null&&allIcdDetails.size()>0){		 
			 DiagnosisDetailsVO diagnosisDetailsVO= allIcdDetails.get(0);
			 diagnosisDetailsVO.setPrimaryAilment("Principal");
			 allIcdDetails.set(0, diagnosisDetailsVO);
		 }
		request.setAttribute("success","Dianosis details deleted successfully");
		
		session.setAttribute("frmPbmPreauthGeneral", sessionFrmPbmPreauthGeneral);
		return this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of deleteDiagnosisDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   
   public ActionForward deleteDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the deleteDrugDetails method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);		
		HttpSession session=request.getSession();
		String strIndexID=request.getParameter("indexID");

		DynaActionForm sessionFrmPbmPreauthGeneral=(DynaActionForm)session.getAttribute("frmPbmPreauthGeneral");
		
		ArrayList<ActivityDetailsVO> allDrugDetails=(ArrayList<ActivityDetailsVO>)sessionFrmPbmPreauthGeneral.get("allDrugDetails");
		
		 if(allDrugDetails!=null){
			 allDrugDetails.remove(new Integer(strIndexID).intValue());
		
		 }//deleting drug details		
		 
		request.setAttribute("success","Drug details deleted successfully");
		
		sessionFrmPbmPreauthGeneral.set("allDrugDetails", allDrugDetails);
		session.setAttribute("frmPbmPreauthGeneral", sessionFrmPbmPreauthGeneral);
		
		return this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of deleteDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  
   public ActionForward doBack(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
    try{
		log.debug("Inside the doBack method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);
		HttpSession session=request.getSession();
		UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
		//userSecurityProfile.getSecurityProfile().get
		if(request.getSession().getAttribute("claimStatus")!=null){
			return mapping.findForward("pbmClaimList");
		}
		if("View".equals(request.getSession().getAttribute("closeMode"))){			
			request.setAttribute("KeepTableData", "KeepTableData");
		//	return mapping.findForward("PBMPL.Home.PreAuthStatus");
			
			return mapping.findForward("PBMPreauth.PreAuthStatus.PreAuthStatus");
		}
		else if("dispensed".equals(request.getSession().getAttribute("closeMode"))){
			
			request.setAttribute("KeepTableData", "KeepTableData");
			
			
			if("PreAuthStatus".equals(session.getAttribute("closeFlag")))
		    { 
				
				request.getSession().setAttribute("closeMode", "View");
			}else{
			 request.getSession().setAttribute("closeMode", "Process");
			 }
			return mapping.findForward(strRequestAuthorization);
		}
		else{
			
		return this.doPreauthProccess(mapping,form,request,response);
	}
		
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

   public ActionForward doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
       try{
          log.debug("Inside PbmPreauthAction doViewPreauth");
          setLinks(request);

         String closeFlag="PreAuthStatus";
         String completedYN="completedYN";
         OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
          Long pbmPreauthSeqID=(Long)request.getAttribute("pbmPreauthSeqID");
          String appealBtn=(String)request.getAttribute("appealBtn");
          String refview=(String)request.getAttribute("refview");
	    DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
        PbmPreAuthVO pbmPreAuthVO=providerManagerObj.getAllPreAuthDetails(pbmPreauthSeqID);	
  
	frmPbmPreauthGeneral.initialize(mapping);
	frmPbmPreauthGeneral = (DynaActionForm) FormUtils.setFormValues("frmPbmPreauthGeneral", pbmPreAuthVO, this, mapping,request);
	frmPbmPreauthGeneral.set("allIcdDetails", pbmPreAuthVO.getIcdDetails());
	frmPbmPreauthGeneral.set("allDrugDetails", pbmPreAuthVO.getDrugDetails());
	frmPbmPreauthGeneral.set("decisionDt", pbmPreAuthVO.getDecisionDt());
	frmPbmPreauthGeneral.set("appealBtn", appealBtn);
	frmPbmPreauthGeneral.set("refview", refview);
	request.getSession().setAttribute("closeMode", "View");
	request.getSession().setAttribute("closeFlag", closeFlag);
	request.getSession().setAttribute("completedYN", completedYN);
	request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
	
     return this.getForward(strRequestAuthorization, mapping, request);
    }//end of try
      catch(TTKException expTTK)
    {
         return this.processExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
      catch(Exception exp)
       {
         return this.processExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
         }//end of catch(Exception exp)
    }//end of doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)

   public ActionForward providerLoginRedirecting(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doPreauthProccess method of PbmPharmacyGeneralAction");
			setOnlineLinks(request);	
			
			HttpSession session=request.getSession();
			
			 UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");
			 String strForward="";
			if("HOS".equals(request.getAttribute("currentLoginMode"))){

			userSecurityProfile.getSecurityProfile().setActiveLink("PBMPL");
			userSecurityProfile.getSecurityProfile().setActiveSubLink("Home");
			userSecurityProfile.getSecurityProfile().setActiveTab("PBM");
			session.setAttribute("UserSecurityProfile",userSecurityProfile);//set the User security profile object to session
			
			session.setAttribute("providerLoginRedirectingFlag", "PHR");
			strForward="PBMPL.Home.PBM";
			}
			
           if("PHR".equals(request.getAttribute("currentLoginMode"))){
        	   session.setAttribute("providerLoginRedirectingFlag", "HOS");
        	   
        	   strForward="Hospital Information.Home.Home";
			}
          request.setAttribute("providerRedirectPage", "providerRedirectPage");
           return mapping.findForward(strForward);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
		}//end of catch(Exception exp)
   }//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   public ActionForward doPbmLogin(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doPbmLogin method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);
		
		request.setAttribute("currentLoginMode", "HOS");
		
	return this.providerLoginRedirecting( mapping, form, request,response);



	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   public ActionForward doProviderLogin(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doPbmLogin method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);
		
		request.setAttribute("currentLoginMode", "PHR");
		
	return this.providerLoginRedirecting( mapping, form, request,response);


	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   
   public ActionForward doPartnerLogin(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
		log.debug("Inside the doPartnerLogin method of PbmPharmacyGeneralAction");
		setOnlineLinks(request);			
		
		return mapping.findForward("Partner Information.Home.Home");//this.getForward(strPreauthProccess, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
   
   //Document Upload PDF
   public ActionForward doDefaultUploadDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
           HttpServletResponse response) throws Exception{
try{
	
	log.debug("Inside the doDefaultUploadDocs method of PbmPharmacyGeneralAction");
	setOnlineLinks(request);			
	DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
	HttpSession session	=	request.getSession();
	 OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
//	String fromFlag	=	(String) session.getAttribute("fromFlag");
	
	// UPLOAD FILE STARTS
	FormFile formFile = null;
	formFile = (FormFile)frmPbmPreauthGeneral.get("pdfUploadFile");
	
	String fileExtn	=	"";
	if(formFile==null||formFile.getFileSize()<1){
	    	  TTKException ttkExc =new TTKException();
	    	  ttkExc.setMessage("error.file.data.empty");
	    	  
	    	  throw ttkExc;
	      }
	if(formFile.getFileSize()!=0)
	{
		fileExtn = GetFileExtension(formFile.toString());
		if(!"pdf".equals(fileExtn)){
			TTKException expTTK = new TTKException();
			expTTK.setMessage("error.pdf.only.required");
			throw expTTK;
		}
	}
	
	
	
	String preAuthSeqId=(String)frmPbmPreauthGeneral.get("preAuthSeqID");
		
	
	String generalType=(String)frmPbmPreauthGeneral.get("fileTypeUpload");
	
	Long count=providerManagerObj.SavePBMUploadFile(preAuthSeqId,generalType,formFile);
			//public String[] savePartialPreAuthDetails(CashlessDetailVO cashlessDetailVO, HashMap prescriptions,UserSecurityProfile userSecurityProfile,String benifitTypeVal,String enhanceYN,FormFile formFile) throws TTKException
	request.setAttribute("successMsg", "File Uploaded Successfully");
	
	
		
		return this.getForward(strPBMClaimSubmission, mapping, request);
		
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPBMClaimSubmission));
	}//end of catch(Exception exp)
}//end of doDefaultPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
  
   
   public static String GetFileExtension(String fname2)
   {
       String fileName = fname2;
       String fname="";
       String ext="";
       int mid= fileName.lastIndexOf(".");
       fname=fileName.substring(0,mid);
       ext=fileName.substring(mid+1,fileName.length());
       return ext;
   }
   
   
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
    
    private OnlineAccessManager getOnlineAccessManagerObject() throws TTKException
    {
    	OnlineAccessManager onlineAccessManager = null;
        try
        {
            if(onlineAccessManager == null)
            {
                InitialContext ctx = new InitialContext();
                //onlineAccessManager = (OnlineAccessManager) ctx.lookup(OnlineAccessManager.class.getName());
                onlineAccessManager = (OnlineAccessManager) ctx.lookup("java:global/TTKServices/business.ejb3/OnlineAccessManagerBean!com.ttk.business.onlineforms.OnlineAccessManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "onlinepbmproviderinfo");
        }//end of catch
        return onlineAccessManager;
    }//end of getOnlineAccessManagerObject()

    
  //CEED Validation
	
	/*public ActionForward doCeedValidation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			
			setLinks(request);
			log.info("Inside PBM Webservices ");
			String ceedResponse	=	"C:/Users/shruthi/Desktop/PBM/Soap.xml";
						
			//GET CLAIM XML FROM DB  S T A R T S
			OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
			//Document xmlData	=	providerManagerObj.getPATXmlTOSendCEED(new Long(preAuthSeqID));
			
			String responseData=CeedSoapSaajClient.execute("1");

              
            CeedSoapSaajClient.writeFileToDisk(responseData,ceedResponse);//RESPONSE XML WRITE TO FILE
            
	          //READING XML RESPONSE FROM DESKTOP FILE
	            SAXReader reader	=	new SAXReader();
	            Document doc		=	(Document) reader.read(new StringReader(responseData));
	              
			
			return this.getForward(strRequestAuthorization, mapping, request);
		}// end of try

		catch (TTKException expTTK) {
			return this.processExceptions(request, mapping, expTTK);
		}// end of catch(TTKException expTTK)
		catch (Exception exp) {
			return this.processExceptions(request, mapping, new TTKException(
					exp, strPreauthProccess));
		}// end of catch(Exception exp)
	}// end of doCeedValidation(ActionMapping mapping,ActionForm
		// form,HttpServletRequest request,HttpServletResponse response)
*/    

    public ActionForward doSaveAppealCommentPBM(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    		try{
    		setOnlineLinks(request);
    		log.debug("Inside PbmPharmacyGeneralAction doSaveAppealComments");
    		HttpSession session	=	request.getSession();
    		DynaActionForm frmPbmPreauthGeneral =(DynaActionForm)form;
    		
    		String appealComments = frmPbmPreauthGeneral.getString("appealComments");
    		appealComments = (appealComments==null) || (appealComments.length()<1)? "" :appealComments;
    		
    		String lPreAuthIntSeqId = frmPbmPreauthGeneral.getString("preAuthSeqID");
    		lPreAuthIntSeqId = (lPreAuthIntSeqId==null) || (lPreAuthIntSeqId.length()<1)? "0" :lPreAuthIntSeqId;
    		Long preAuthSeqId = Long.parseLong(lPreAuthIntSeqId);
    		OnlinePbmProviderManager providerManagerObj=getPbmManagerObject();
    		String alAppealComment="";
    		alAppealComment = providerManagerObj.saveAppealComments(preAuthSeqId,appealComments);
    		
    		PbmPreAuthVO pbmPreAuthVO = null;
    		pbmPreAuthVO=providerManagerObj.getAllPreAuthDetails(preAuthSeqId);
    		    	    		
    		frmPbmPreauthGeneral = (DynaActionForm) FormUtils.setFormValues("frmPbmPreauthGeneral", pbmPreAuthVO, this, mapping,request);
    		
    		session.setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
    		
    		return mapping.findForward(strPbmOutPutScreen);
    		
    		}//end of try
    	catch(TTKException expTTK)
    		{
    			return this.processOnlineExceptions(request, mapping, expTTK);
    	}//end of catch(TTKException expTTK)
    	catch(Exception exp)
    		{
    			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
    	}//end of catch(Exception exp)
    }
    
   
         
    
     
    
    public ActionForward doDefaultCheckEligibility(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doDefaultCheckEligibility method of PbmPharmacyGeneralAction");
			//setOnlineLinks(request);
			
	//	request.getSession().setAttribute("pbmRightClick", TTKPropertiesReader.getPropertyValue("right.click.disable"));
			
			return this.getForward(strCheckEligibility, mapping, request);
			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
		}//end of catch(Exception exp)
}//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) 
    
    
    public ActionForward doPbmCheckEligibility(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doPbmCheckEligibility method of PbmPharmacyGeneralAction");
			setOnlineLinks(request);
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
			  DynaActionForm frmPbmPreauthGeneral =(DynaActionForm)form;
			  HttpSession session=request.getSession();  
			  OnlinePbmProviderManager onlineAccessManager=this.getPbmManagerObject();
			  PbmPreAuthVO pbmVo = null;
			String vip=null;
			  String enrollId		=	(String) (request.getParameter("enrollId").trim());
			String benifitTypeVal	=	(String) (request.getParameter("benifitTypeCode"));
			String treatmentDate	=	(String) (request.getParameter("treatmentDate"));
			String benefitText	=	(String) (request.getParameter("benifit"));
			String eligibility=null;
			ArrayList alOnlineAccList = null;
			String flag	=	"";
			PbmPreAuthVO pbmPreAuthVO = (PbmPreAuthVO) FormUtils.getFormValues(frmPbmPreauthGeneral, this, mapping, request);
			
			if(enrollId ==null || enrollId ==""){
    			
    			TTKException expTTK = new TTKException();
 				expTTK.setMessage("error.pbm.alkoot.id.required"); 
 				throw expTTK;
    		}
			else{
				
			
				Long hospSeqID=(Long)session.getAttribute("hospSeqID");
				Long hospUserSeqID=(Long)session.getAttribute("hospUserSeqID");
				Long memSeqID=pbmPreAuthVO.getMemSeqID();
				pbmPreAuthVO.setEnrolmentID(enrollId);
				pbmPreAuthVO.setDateOfTreatment(treatmentDate);
				pbmPreAuthVO.setHospitalSeqID(new Long(hospSeqID));
				pbmPreAuthVO.setAddedBy(new Long(hospUserSeqID));
				pbmPreAuthVO.setEventRefNo(frmPbmPreauthGeneral.getString("eventRefNo"));	
			
				 pbmPreAuthVO=onlineAccessManager.validateMemberID(pbmPreAuthVO);
			
			pbmVo	= onlineAccessManager.geMemberDetailsOnEnrollId(enrollId,benifitTypeVal,userSecurityProfile.getHospSeqId());
			pbmVo.setBenifitType(benefitText);
			pbmVo.setBenifitTypeCode(benifitTypeVal);
			
			String[] tobBenefits	=	onlineAccessManager.getTobForBenefits(benifitTypeVal,enrollId);
			pbmVo.setCoPay(tobBenefits[0]);
			pbmVo.setEligibility1(tobBenefits[1]);
			pbmVo.setDeductible(tobBenefits[2]);
		//	pbmVo.setVipDetails("vip: yes");
			frmPbmPreauthGeneral = (DynaActionForm)FormUtils.setFormValues("frmPbmPreauthGeneral",pbmVo,this,mapping,request);
			
			/* Capturing log start*/
			ArrayList<String> dataList=new ArrayList<>();
			dataList.add("PROVIDER");
			dataList.add(enrollId);
			dataList.add(benifitTypeVal);
			dataList.add(treatmentDate);
			dataList.add(eligibility);
			dataList.add(userSecurityProfile.getHospSeqId()+"");
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(null);
			dataList.add(userSecurityProfile.getUSER_SEQ_ID()+"");
			dataList.add(userSecurityProfile.getHospitalName()+"");
			dataList.add(userSecurityProfile.getEmpanelNumber()+"");
			dataList.add(null);
			dataList.add(frmPbmPreauthGeneral.getString("reasonForRejection"));
			onlineAccessManager.updateLogTable(dataList);
			/* Capturing log end*/
			
			request.getSession().setAttribute("enrollId", pbmVo.getEnrollId());
			request.getSession().setAttribute("benifitTypeVal", benifitTypeVal);
			request.getSession().setAttribute("benefitText", benefitText);
			request.getSession().setAttribute("provMemName", pbmVo.getMemberName());
    		request.getSession().setAttribute("fromFlag", request.getParameter("fromFlag"));
			if("true".equals((String)request.getParameter("memberAuthenticated")))
			request.getSession().setAttribute("memberAuthenticated", "true");
			request.setAttribute("flag",flag);
			request.setAttribute("logicVar","true");
			request.setAttribute("eligibility", pbmVo.getEligibility());
			frmPbmPreauthGeneral.set("eligibility", pbmVo.getEligibility());
			frmPbmPreauthGeneral.set("reasonForRejection",pbmVo.getReasonForRejection());
			frmPbmPreauthGeneral.set("benifitType", benifitTypeVal);
			frmPbmPreauthGeneral.set("enrollId", pbmVo.getEnrollId()); 
			frmPbmPreauthGeneral.set("qatarID", pbmVo.getEmirateID()); 
			frmPbmPreauthGeneral.set("treatmentDate", frmPbmPreauthGeneral.get("treatmentDate"));
			frmPbmPreauthGeneral.set("bufferWarning",pbmVo.getBufferFlag());
			//frmPbmPreauthGeneral.set("vipdetails",pbmVo.getVipDetails());
			request.getSession().setAttribute("tobBenefitsForMemElig", tobBenefits);
			request.getSession().setAttribute("benifitTypeVal", benifitTypeVal);
			request.getSession().setAttribute("benefitText", benefitText);
			
			if(pbmVo.getVipDetails().compareTo("Y")==0) vip="YES";
			else vip="NO";
			request.getSession().setAttribute("vip", vip);
			request.getSession().setAttribute("frmPbmPreauthGeneral",frmPbmPreauthGeneral);
	        session.setAttribute("benifitTypeVal",benifitTypeVal);  
			
			}
			
			return this.getForward(strVlaidateCheckEligibiliy, mapping, request);
			
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"checkEligibility"));
		}//end of catch(Exception exp)
}
    
    public ActionForward doProceed(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           HttpServletResponse response) throws Exception{
	try{
			log.debug("Inside the doProceed method of PbmPharmacyGeneralAction");
			setOnlineLinks(request);		
			DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)form;
			//frmPbmPreauthGeneral.initialize(mapping);
			 HttpSession session=request.getSession();  
		 SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
	     String toDayDate=dateFormat.format(new Date());
	     
	     String enrollId= frmPbmPreauthGeneral.getString("enrollId");
	     String qatarID= frmPbmPreauthGeneral.getString("qatarID");
	     String flag= frmPbmPreauthGeneral.getString("flag");
	     session.setAttribute("enrollId",enrollId);  
	     session.setAttribute("qatarID",qatarID); 
	     
	     if("YES".equals(flag)){
	    	 frmPbmPreauthGeneral.set("enrolmentID", "");
		     frmPbmPreauthGeneral.set("qatarID", ""); 
	     }
	     else{
	    	 frmPbmPreauthGeneral.set("enrolmentID", enrollId);
		     frmPbmPreauthGeneral.set("qatarID", qatarID);
	     }
	    frmPbmPreauthGeneral.set("dateOfTreatment", toDayDate);
	    frmPbmPreauthGeneral.set("flag", flag);
	    request.getSession().setAttribute("flag", flag);
		request.getSession().setAttribute("frmPbmPreauthGeneral", frmPbmPreauthGeneral);
		
			return this.getForward(strPreauthProccess, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
			catch(Exception exp)
		{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,strPreauthProccess));
		}//end of catch(Exception exp)
   }
    
    
    
    
}//end of PbmPharmacyGeneralAction
