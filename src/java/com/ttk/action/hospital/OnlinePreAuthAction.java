package com.ttk.action.hospital;

/**
 * as per Hospital Login
 * @ (#) OnlinePreAuthAction Mar 24, 2014
 *  Author       :Kishor kumar S h
 * Company      : Rcs Technologies
 * Date Created : April 29 ,2015
 *
 * @author       :Kishor kumar S h
 * Modified by   :
 * Modified date :
 * Reason        :
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.maintenance.MaintenanceManager;
import com.ttk.business.onlineforms.OnlinePreAuthManager;
import com.ttk.business.onlineforms.providerLogin.OnlineProviderManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.MOUDocumentVO;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.empanelment.LabServiceVO;
import com.ttk.dto.onlineforms.providerLogin.PbmPreAuthVO;
import com.ttk.dto.onlineforms.providerLogin.PreAuthSearchVO;
import com.ttk.dto.onlineforms.providerLogin.ServiceDetailsVO;
import com.ttk.dto.preauth.ActivityDetailsVO;
import com.ttk.dto.preauth.CashlessDetailVO;
import com.ttk.dto.preauth.ClinicianDetailsVO;
import com.ttk.dto.preauth.DentalOrthoVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.DrugDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for Searching the List Policies to see the Online Account Info.
 * This also provides deletion and updation of products.
 */
public class OnlinePreAuthAction extends TTKAction {


    private static final Logger log = Logger.getLogger( OnlinePreAuthAction.class );

    //Modes.
    private static final String strBackward="Backward";
    private static final String strForward="Forward";

    // Action mapping forwards.
    private static final String strOnlinePreAuth	=	"onlinePreAuth";
    private static String strForwardValidate		=	"onlinePreAuth";
    private static String strPreauthInvoice			=	"preauthInvoice";
    private static final String strAddConsumables	=	"addConsumables";
    private static final String strAddPharmacy		=	"addPharmacy";

	 private static final String strONLINEPREAUTH	=	"onlinePreAuth";
	 private static final String strAddActivityDetails=	"addActivityDetails";
	 private static final String strONLINEPREAUTHSUCCESS=	"preAuthSuccess";
	 private static final String strCliniciansList=	"onlineClinicianList";
	 private static final String strONLINEPREAUTHENHANCE	=	"onlinePreAuthEnhance";
	 
    
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
            log.debug("Inside the doDefault method of OnlinePreAuthAction");
            setOnlineLinks(request);
            
            return this.getForward(strOnlinePreAuth, mapping, request);
        }//end of try
        catch(TTKException expTTK)
        {
            return this.processOnlineExceptions(request, mapping, expTTK);
        }//end of catch(TTKException expTTK)
        catch(Exception exp)
        {
            return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
        }//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
            //HttpServletResponse response)

    
    
         
   
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
            throw new TTKException(exp, strOnlinePreAuth);
        }//end of catch
        return onlinePreAuthManager;
    }//end of getOnlineAccessManagerObject()

    
    
    public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		
    		setOnlineLinks(request);
    		log.debug("Inside the doView method of OnlineCashlessHospAction");
    		DynaActionForm frmOnlinePreAuth =(DynaActionForm)request.getSession().getAttribute("frmCashlessAdd");
    		//frmCashlessAdd.initialize(mapping);     //reset the form data
    		HttpSession session	=	request.getSession();
    		CashlessDetailVO cashlessDetailVO	=	new CashlessDetailVO();
    		
    		OnlinePreAuthManager onlinePreAuthManager = this.getOnlineAccessManagerObject();
    		String rownum	=	(String)request.getParameter("rownum");
			TableData tableData = (TableData) request.getSession().getAttribute("tableData");
			PreAuthSearchVO preAuthSearchVO	=	null;
			preAuthSearchVO = (PreAuthSearchVO)tableData.getRowInfo(Integer.parseInt(rownum));
			request.getSession().setAttribute("lPreAuthIntSeqId", preAuthSearchVO.getPatAuthSeqId());
			Object[] preauthAllresult	=	onlinePreAuthManager.getOnlinePartialPreAuthDetails(preAuthSearchVO.getPatAuthSeqId());	
			cashlessDetailVO = (CashlessDetailVO) preauthAllresult[0];
			System.out.println("enrollllllll::::::::::"+cashlessDetailVO.getEnrollId());
			String res=onlinePreAuthManager.getVipYN(cashlessDetailVO.getEnrollId());
			String vip=null;
			if(res.compareTo("Y")==0) vip="YES";
			else vip="NO";
			ArrayList<DiagnosisDetailsVO> diagnosis = (ArrayList<DiagnosisDetailsVO>) preauthAllresult[1];
			ArrayList<ActivityDetailsVO> activities = (ArrayList<ActivityDetailsVO>) preauthAllresult[2];
			ArrayList<DrugDetailsVO> aldrugs = (ArrayList<DrugDetailsVO>) preauthAllresult[3];
			session.setAttribute("preauthDiagnosis", diagnosis);
			session.setAttribute("preauthActivities", activities);
			session.setAttribute("preauthDrugs", aldrugs);
			//System.out.println("Source Type Id ::"+cashlessDetailVO.getSourceTypeId());
    		DiagnosisDetailsVO diagnosisDetailsVO	=	new DiagnosisDetailsVO();//diagnosis details
    		ActivityDetailsVO activityDetailsVO		=	new ActivityDetailsVO();//activity Details
    		/*cashlessDetailVO = (CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmCashlessAdd",
    				this,mapping,request);*/

    		cashlessDetailVO.setDiagnosisDetailsVO(diagnosisDetailsVO);
    		cashlessDetailVO.setActivityDetailsVO(activityDetailsVO);
    		cashlessDetailVO.setDrugDetailsVO(new DrugDetailsVO());
    		
    		ArrayList<CacheObject> alEncountertypes	=	new ArrayList<CacheObject>();
    		alEncountertypes			=	onlinePreAuthManager.getEnounterTypes(cashlessDetailVO.getBenifitType());
    		cashlessDetailVO.setEncounterTypes(alEncountertypes);
    		request.getSession().setAttribute("encounterTypes", alEncountertypes);
    		if("EnhancementNew".equals(preAuthSearchVO.getEnhanceImageName()) || ("AppealButton".equals(preAuthSearchVO.getAppealImageName())))
    			cashlessDetailVO.setValidateDocNameYN("Y");
    		else
    			cashlessDetailVO.setValidateDocNameYN("N");
    		DynaActionForm onlinePreAuthForm = setFormValues(cashlessDetailVO,mapping,request);
    		onlinePreAuthForm.set("encounterTypes", alEncountertypes);
    		onlinePreAuthForm.set("appealBtn", request.getParameter("appealBtn"));
    		onlinePreAuthForm.set("refview", request.getParameter("refview"));
    		
    		OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject1();
			ArrayList<String[]> alShortFallList	=	onlineProviderManager.getShortfallList(preAuthSearchVO.getPatAuthSeqId());
			request.getSession().setAttribute("alShortFallList",alShortFallList);
			onlinePreAuthForm.set("enhanceYN", cashlessDetailVO.getEnhancedYN());
			if(request.getParameter("appealBtn")!= null && request.getParameter("appealBtn").equals("appealBtn")){
				request.getSession().setAttribute("sourceType", "Y");
				onlinePreAuthForm.set("sourceType","Y");
				}else{
					request.getSession().setAttribute("sourceType", "N");
					onlinePreAuthForm.set("sourceType","N");	
				}
			
    		request.getSession().setAttribute("frmOnlinePreAuth", onlinePreAuthForm);
    		request.getSession().setAttribute("preauthDiagnosis", diagnosis);
    		request.getSession().setAttribute("preauthActivities", activities);
    		request.getSession().setAttribute("preauthDrugs", aldrugs);
    		request.getSession().setAttribute("preAuthNo", preAuthSearchVO.getPreAuthNo());
    		request.getSession().setAttribute("preAuthStatus", preAuthSearchVO.getStatus());
    		request.getSession().setAttribute("claimStatus", null);
    		request.getSession().setAttribute("fromFlag", request.getParameter("fromFlag"));
    		request.getSession().setAttribute("enhanceYN", cashlessDetailVO.getEnhancedYN());
    		request.getSession().setAttribute("viewDocFlag", "view");
    		request.getSession().setAttribute("loginType", "HOS");
    		request.getSession().setAttribute("vip", vip);
    		
    		return this.getForward(strONLINEPREAUTH, mapping, request);
    		}//end of try
    	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
		}//end of catch(Exception exp)
    }//end of doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
		/**
		 * @param mapping ActionMapping
		 * @param form 
		 * @param request
		 * @param response
		 * @return true if vidal ID is already got OTP
		 * @throws Exception
		 */
		public ActionForward doValidateEnrollId(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
			try{
				setOnlineLinks(request);
				log.debug("Inside the doValidateEnrollId method of OnlinePreAuthAction");
				DynaActionForm frmOnlinePreAuth =(DynaActionForm)form;
				String vidalId	=	frmOnlinePreAuth.getString("vidalId");
				
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
				OnlinePreAuthManager onlinePreAuthManager= null;
				CashlessDetailVO cashlessDetailVO	=	null;
				
				onlinePreAuthManager = this.getOnlineAccessManagerObject();
				String flag	=	"";
				//below function to check the OTP has generated for the vidal ID  on that day
				flag	= onlinePreAuthManager.getValidateVidalId(vidalId);
			//	flag	=	"Y";
				if("Y".equals(flag))
				{
					cashlessDetailVO	= onlinePreAuthManager.geMemberDetailsOnEnrollId(vidalId,(String)request.getSession().getAttribute("benifitTypeID"));
					frmOnlinePreAuth = (DynaActionForm)FormUtils.setFormValues("frmOnlinePreAuth",
							cashlessDetailVO,this,mapping,request);
					request.setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
					frmOnlinePreAuth.set("enrollId", vidalId);
					frmOnlinePreAuth.set("providerName", userSecurityProfile.getHospitalName());
					
					strForwardValidate	=	"showpreauth";
				}
				else
				{
					strForwardValidate	=	"doEligibilityCheck";
				}
				//	frmOnlinePreAuth.set("enrollId", request.getParameter("enrollId"));
				//finally return to the grid screen
				request.setAttribute("vidalId", vidalId);
				request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
				return this.getForward(strForwardValidate, mapping, request);
			}//end of try
		catch(TTKException expTTK)
		{
		return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
		}//end of catch(Exception exp)
		}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
		
		
		
		/**
	     * 
	     * @param mapping doaddConsumablesList
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    public ActionForward doaddConsumablesList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("Inside the doaddConsumablesList method of ProviderPrescriptionAction");
	    		setOnlineLinks(request);
	    		/*UserSecurityProfile userSecurityProfile = (UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	    		//get the session bean from the bean pool for each excecuting thread
	    		ArrayList alMinorServices	=	null;
	    		OnlineAccessManager onlineAccessManager=null;
	    		onlineAccessManager = this.getOnlineAccessManagerObject();
	    		LaboratoryServicesVO laboratoryServicesVO =new LaboratoryServicesVO();
				DynaActionForm frmCashlessPrescription = (DynaActionForm)FormUtils.setFormValues("frmCashlessPrescription",
						laboratoryServicesVO, this, mapping, request);
				
				alMinorServices=onlineAccessManager.getLabServices("LABORATORY",userSecurityProfile.getHospSeqId());
				if(alMinorServices==null){
					alMinorServices=new ArrayList();
				}//end of if(alGrading==null)
				frmCashlessPrescription.set("hospLabs",alMinorServices);
				request.setAttribute("hospLabs",alMinorServices);
				frmCashlessPrescription.set("caption","");
				request.setAttribute("frmCashlessPrescription",frmCashlessPrescription);
				request.setAttribute("Prescription_Type", "Consumables");*/
				return this.getForward(strAddConsumables, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processOnlineExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
			}//end of catch(Exception exp)
	    }//end of doaddConsumablesList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    
	    
	    /**
	     * 
	     * @param mapping doaddConsumablesList
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    public ActionForward doaddPharmacyList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("Inside the doAddPharmacyList method of ProviderPrescriptionAction");
	    		setOnlineLinks(request);
				return this.getForward(strAddPharmacy, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processOnlineExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
			}//end of catch(Exception exp)
	    }//end of doAddPharmacyList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    
	    
	    /**
		 * Saving the Online PreAuth Details General basic Details
		 */
	    
	    public ActionForward doSaveGeneral(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
			try{
				setOnlineLinks(request);
				log.debug("Inside the doSaveGeneral method of OnlinePreAuthAction");
				DynaActionForm frmOnlinePreAuth =(DynaActionForm)form;
				CashlessDetailVO cashlessDetailVO = null;
				cashlessDetailVO	=	(CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmOnlinePreAuth",
         				this,mapping,request);
					
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));

				OnlinePreAuthManager onlinePreAuthManager	=	null;
				onlinePreAuthManager = this.getOnlineAccessManagerObject();
				Long	lPreAuthIntSeqId	=	null;
				
				//lPreAuthIntSeqId	= 	onlinePreAuthManager.savePreAuthDetails(cashlessDetailVO,null,userSecurityProfile,null);
				
				cashlessDetailVO	=	null;
				if(lPreAuthIntSeqId>0)
				{
					cashlessDetailVO=	onlinePreAuthManager.getPreAuthDetails(lPreAuthIntSeqId);
				}
				DiagnosisDetailsVO diagnosisDetailsVO	=	new DiagnosisDetailsVO();
				cashlessDetailVO.setDiagnosisDetailsVO(diagnosisDetailsVO);
				
				frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
				
				request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
				//request.getSession().setAttribute("cashlessDetailVO", cashlessDetailVO);
				request.getSession().setAttribute("lPreAuthIntSeqId", lPreAuthIntSeqId);
				
				request.setAttribute("updated", "message.savedSuccessfully");
				request.setAttribute("MemberSave", "MemberSave"); 
				return this.getForward(strONLINEPREAUTH, mapping, request);
			//	return this.getForward(strPreauthInvoice, mapping, request);
			}//end of try
		catch(TTKException expTTK)
		{
		return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
		}//end of catch(Exception exp)
		}//end of doSaveGeneral(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
	    
		/**
		 * Saving the Online PreAuth Details and File Upload
		 */
		
		
		public ActionForward doSavePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
			try{
				setOnlineLinks(request);
				log.debug("Inside the doSavePreAuth method of OnlinePreAuthAction");
				DynaActionForm frmOnlinePreAuth =(DynaActionForm)form;
				String vidalId	=	frmOnlinePreAuth.getString("vidalId");
				
			
				
				 CashlessDetailVO cashlessDetailVO = null;
				 cashlessDetailVO	=	(CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmOnlinePreAuth",
         				this,mapping,request);
					
					
				HashMap prescriptions	=	(HashMap) request.getSession().getAttribute("prescriptions");
				ArrayList allabs		= null;
				ArrayList alRadios		= null;
				ArrayList alSurgeries	= null;
				ArrayList alConsumables	= null;
				ArrayList alMinors		= null;
				if(prescriptions!=null){
					allabs			= (ArrayList)prescriptions.get("LABORATORY");
					alRadios		= (ArrayList)prescriptions.get("RADIOLOGY");
					alSurgeries		= (ArrayList)prescriptions.get("SURGERIES");
					alConsumables	= (ArrayList)prescriptions.get("CONSUMABLES");
					alMinors		= (ArrayList)prescriptions.get("MINORSURGERIES");
				}
				
				Set KeySet	=	 prescriptions.keySet();
				Iterator itr = (Iterator) prescriptions.keySet().iterator();
				LabServiceVO labServiceVO	=	null;
				StringBuffer medicalIds		=	new StringBuffer();
				
			    while (itr.hasNext()){
			      allabs	=	(ArrayList)prescriptions.get(itr.next());
			      for(int k=0;k<allabs.size();k++){
			    	  labServiceVO	=	(LabServiceVO) allabs.get(k);
			    	  medicalIds		=	medicalIds.append("|").append(labServiceVO.getMedicalTypeId());
			      }
			    }
			    medicalIds.append("|");
				UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
				
				OnlinePreAuthManager onlinePreAuthManager= null;
				onlinePreAuthManager = this.getOnlineAccessManagerObject();
				int iResult	=	0;
				FormFile formFile = null;
				formFile = (FormFile)frmOnlinePreAuth.get("uploadFile");
				
				//F I L E UPLOAD S T A R T S
				FileOutputStream outputStream = null;
        		String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("mouUploads"));
    	        File folder = new File(path);
				if(!folder.exists()){
					folder.mkdir();
				}
				String finalPath=(path+formFile+"_"+new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date()));
				outputStream = new FileOutputStream(new File(finalPath));
				outputStream.write(formFile.getFileData());
				outputStream.close();
				//F I L E UPLOAD E N D S
				
				
				cashlessDetailVO.setFileName(formFile.toString());
				
				String fileExtn = GetFileExtension(formFile.toString());
				if((fileExtn.equalsIgnoreCase("pdf"))   || (fileExtn.equalsIgnoreCase("doc")) 
                        || (fileExtn.equalsIgnoreCase("docx")) || (fileExtn.equalsIgnoreCase("xls"))   
                        || (fileExtn.equalsIgnoreCase("xlsx")) || fileExtn.equalsIgnoreCase("jpeg") 
                        || fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("png"))
				{
					
				}
			//	iResult	= onlinePreAuthManager.savePreAuthDetails(cashlessDetailVO,prescriptions,userSecurityProfile,medicalIds.toString());
				
				
					
				//	frmOnlinePreAuth.set("enrollId", request.getParameter("enrollId"));
				//finally return to the grid screen
				request.setAttribute("vidalId", vidalId);
				request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
				return this.getForward(strPreauthInvoice, mapping, request);
			}//end of try
		catch(TTKException expTTK)
		{
		return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
		}//end of catch(Exception exp)
		}//end of doSavePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)
		
		
		
		
		/**
	     * 
	     * @param mapping doAddConsumables
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    public ActionForward doConsumablesAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("Inside the doConsumablesAdd method of OnlinePreAuthAction");
	    		setOnlineLinks(request);
	    		OnlinePreAuthManager onlinePreAuthManager= null;
				onlinePreAuthManager = this.getOnlineAccessManagerObject();
	    		
				DynaActionForm frmOnlineConsumables 	=	(DynaActionForm)form;
				String consDesc	=	frmOnlineConsumables.getString("consumableDesc");
				LabServiceVO	labServiceVO	=	new LabServiceVO();
				
				ArrayList alConsumables	=	null;
				
				/*
				 * Activity Seq Id and Activity Codes
				 */
				labServiceVO	=	onlinePreAuthManager.getConsumableDetails(consDesc);
				labServiceVO.setUnitPrice(new BigDecimal(TTKCommon.checkNull(frmOnlineConsumables.get("addUnitPrice").toString())));
				labServiceVO.setQuantity(new Integer( TTKCommon.checkNull(frmOnlineConsumables.get("addQuantity").toString())));
				labServiceVO.setGross(new BigDecimal(TTKCommon.checkNull(frmOnlineConsumables.get("addGross").toString())));
				labServiceVO.setDiscount(new BigDecimal(TTKCommon.checkNull(frmOnlineConsumables.get("addDiscount").toString())));
				labServiceVO.setDiscGross(new BigDecimal(TTKCommon.checkNull(frmOnlineConsumables.get("addDiscountedGross").toString())));
				labServiceVO.setPatientShare(new BigDecimal(TTKCommon.checkNull(frmOnlineConsumables.get("addPatientShare").toString())));
				labServiceVO.setNetAmount(new BigDecimal(TTKCommon.checkNull(frmOnlineConsumables.get("addNetAmount").toString())));
				labServiceVO.setAddedBy(TTKCommon.getUserSeqId(request));
				Long intimationSeqID	=	(Long) request.getSession().getAttribute("PAT_INTIMATION_SEQ_ID");
				
				frmOnlineConsumables.initialize(mapping);
				/*
				 * Save the PreAuth Consumables line item wise
				 */
				int iSaveConsumables	=	onlinePreAuthManager.savePreAuthConsumables(labServiceVO,intimationSeqID);
				
				/*
				 * Getting the Inserted Data and Showing in the Grid
				 */
				if(iSaveConsumables>0)
				{
					alConsumables	=	onlinePreAuthManager.getPreAuthConsumables(intimationSeqID);
				}
				/*alConsumables	=	(ArrayList) request.getSession().getAttribute("alConsumables");
				if(alConsumables==null){
					alConsumables	=	new ArrayList();
					alConsumables.add(labServiceVO);
				}
				else{
					alConsumables.add(labServiceVO);
				}*/
				request.getSession().setAttribute("alConsumables", alConsumables);
				return this.getForward(strAddConsumables, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processOnlineExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
			}//end of catch(Exception exp)
	    }//end of doConsumablesAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    
		
	    
	    
	  
	    /**
	     * 
	     * @param mapping Pharmacy Details
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    public ActionForward doGetConsumablesDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("Inside the doGetConsumablesDetails method of OnlinePreAuthAction");
	    		setOnlineLinks(request);
	    		OnlinePreAuthManager onlinePreAuthManager= null;
				onlinePreAuthManager = this.getOnlineAccessManagerObject();
	    		
				DynaActionForm frmOnlinePharmacy 	=	(DynaActionForm)form;
				String pharmacyDesc	=	frmOnlinePharmacy.getString("pharmacyDescSearch");
				LabServiceVO	labServiceVO	=	new LabServiceVO();
				
				labServiceVO	=	onlinePreAuthManager.getPreAuthPharamcyDetails(pharmacyDesc);

				frmOnlinePharmacy = (DynaActionForm)FormUtils.setFormValues("frmOnlinePharmacy",
						labServiceVO,this,mapping,request);
				
				return this.getForward(strAddPharmacy, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processOnlineExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
			}//end of catch(Exception exp)
	    }//end of doGetConsumablesDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    
	    
		//This method is used to get the extension of the file attached
	    //DonE for INTX - KISHOR KUMAR S H
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
	    
	    
	    /*
		 * Online PREAUTH SUBMISSION ENTRY SCREEN
		 */
	    public ActionForward doProceedPreAuthSubmission(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside the doProceedPreAuthSubmission method of OnlineCashlessHospAction");
		DynaActionForm frmOnlinePreAuth =(DynaActionForm)request.getSession().getAttribute("frmCashlessAdd");
		//frmCashlessAdd.initialize(mapping);     //reset the form data
		CashlessDetailVO cashlessDetailVO	=	new CashlessDetailVO();
		DiagnosisDetailsVO diagnosisDetailsVO	=	new DiagnosisDetailsVO();//diagnosis details
		ActivityDetailsVO activityDetailsVO		=	new ActivityDetailsVO();//activity Details
		cashlessDetailVO = (CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmCashlessAdd",
				this,mapping,request);

		OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
		ArrayList<CacheObject> alEncountertypes	=	new ArrayList<CacheObject>();
		alEncountertypes			=	onlinePreAuthManager.getEnounterTypes(cashlessDetailVO.getBenifitTypeCode());
		cashlessDetailVO.setDiagnosisDetailsVO(diagnosisDetailsVO);
		cashlessDetailVO.setActivityDetailsVO(activityDetailsVO);
		cashlessDetailVO.setDrugDetailsVO(new DrugDetailsVO());
		DynaActionForm onlinePreAuthForm = setFormValues(cashlessDetailVO,mapping,request);
		onlinePreAuthForm.set("encounterTypes", alEncountertypes);
		
		request.getSession().removeAttribute("alShortFallList");
		request.getSession().setAttribute("frmOnlinePreAuth", onlinePreAuthForm);
		request.getSession().setAttribute("encounterTypes", alEncountertypes);
		request.getSession().setAttribute("preauthDiagnosis", null);
		request.getSession().setAttribute("preauthActivities", null);
		request.getSession().setAttribute("preauthDrugs", null);
		request.getSession().setAttribute("preAuthStatus", null);
		request.getSession().setAttribute("preAuthNo", null);
		request.getSession().removeAttribute("sourceType");
		return this.getForward(strONLINEPREAUTH, mapping, request);
		}//end of try
		catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
	}//end of doProceedPreAuthSubmission(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		 //HttpServletResponse response)
		 
		 
	    
	    /*
	     * TO Save Diagnosis details
	     */

	    public ActionForward doSaveDiags(ActionMapping mapping,ActionForm form,HttpServletRequest request,
                HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside the doSaveDiags method of OnlineCashlessHospAction");
		HttpSession session				=	request.getSession();
		DynaActionForm frmOnlinePreAuth =	(DynaActionForm)form;
		ArrayList<CacheObject> alEncountertypes	=	(ArrayList<CacheObject>) session.getAttribute("encounterTypes");
		ArrayList<DiagnosisDetailsVO> preauthDiagnosis	=	(ArrayList<DiagnosisDetailsVO>) (session.getAttribute("preauthDiagnosis")==null?new ArrayList<DiagnosisDetailsVO>():session.getAttribute("preauthDiagnosis"));
		String fromFlag	=	(String) session.getAttribute("fromFlag");
		CashlessDetailVO cashlessDetailVO	 	=	new CashlessDetailVO();
		DiagnosisDetailsVO diagnosisDetailsVO	=	new DiagnosisDetailsVO();//diagnosis details
		DiagnosisDetailsVO diagnosisDetailsVO1	=	new DiagnosisDetailsVO();//diagnosis details
		cashlessDetailVO 	= 	getFormValues(frmOnlinePreAuth,mapping,request);
		diagnosisDetailsVO	=	cashlessDetailVO.getDiagnosisDetailsVO();
		diagnosisDetailsVO1.setIcdCode("");
		diagnosisDetailsVO1.setAilmentDescription("");
		cashlessDetailVO.setDiagnosisDetailsVO(diagnosisDetailsVO1);
		
		if("preAuthEnhance".equals(fromFlag)){
		
		cashlessDetailVO.setEnhancedYN("Y");
		}
		frmOnlinePreAuth	=	setFormValues(cashlessDetailVO,mapping,request);
		
		if(preauthDiagnosis.size()==0)
			diagnosisDetailsVO.setPrimaryAilment("Y");
		else
			diagnosisDetailsVO.setPrimaryAilment("N");
		if(preauthDiagnosis.size()==1)
			diagnosisDetailsVO.setAuthType("Y");
		else
			diagnosisDetailsVO.setAuthType("N");
		
		
		diagnosisDetailsVO.setEnhanceYN("Y");
		preauthDiagnosis.add(diagnosisDetailsVO);
			
		// focus object Code S T A R T S
		request.setAttribute("focusId", request.getParameter("focusId"));
		// focus object Code E N D S 
		frmOnlinePreAuth.set("encounterTypes", alEncountertypes);
		request.getSession().setAttribute("preauthDiagnosis", preauthDiagnosis);
		request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);

		if("preAuthEnhance".equals(fromFlag))
			return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
		else
			return this.getForward(strONLINEPREAUTH, mapping, request);
		}//end of try
		catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
		catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
	}//end of doSaveDiags(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		 //HttpServletResponse response)


	public ActionForward deleteDiagnosisDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside PreAuthGenealAction deleteDiagnosis ");
			HttpSession session=request.getSession();
			DynaActionForm frmOnlinePreAuth=(DynaActionForm)form;
			ArrayList<DiagnosisDetailsVO> preauthDiagnosis	=	(ArrayList<DiagnosisDetailsVO>) (session.getAttribute("preauthDiagnosis")==null?new ArrayList<DiagnosisDetailsVO>():session.getAttribute("preauthDiagnosis"));

			int rownum	=	Integer.parseInt(request.getParameter("rownum"));
			DiagnosisDetailsVO diagnosisDetailsVO	=	preauthDiagnosis.get(rownum);
			
			preauthDiagnosis.remove(diagnosisDetailsVO);
			/*for(int k=0;k<preauthDiagnosis.size();k++){
				diagnosisDetailsVO	=	preauthDiagnosis.get(k);
				if(diagnosisDetailsVO.getIcdCode().equals(rownum))
					preauthDiagnosis.remove(diagnosisDetailsVO);
			}*/
			/*diagnosisDetailsVO.setAuthType("PAT"); 
			diagnosisDetailsVO.setPreAuthSeqID(new Long(preAuthSeqID));
			diagnosisDetailsVO.setAddedBy(TTKCommon.getUserSeqId(request));		  
			onlinePreAuthManager.deleteDiagnosisDetails(diagnosisDetailsVO);*/
			request.setAttribute("successMsg","Diagnosis Details Deleted Successfully");	
			
			/*Object[]	preauthAllresult=onlinePreAuthManager.getOnlinePreAuthDetails(diagnosisDetailsVO.getPreAuthSeqID());
			cashLessDetailVO=(CashlessDetailVO)preauthAllresult[0];*/
			//ArrayList<DiagnosisDetailsVO> diagnosis=(ArrayList<DiagnosisDetailsVO>)preauthDiagnosis;
			//frmOnlinePreAuth = setFormValues(cashLessDetailVO,mapping,request);
			
			request.getSession().setAttribute("preauthDiagnosis", preauthDiagnosis);
			request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
			String fromFlag	=	(String) session.getAttribute("fromFlag");
			if("preAuthEnhance".equals(fromFlag))
				return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
			else
				return this.getForward(strONLINEPREAUTH, mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of deleteDiagnosisDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


	public ActionForward deleteActivityDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside PreAuthGenealAction deleteActivityDetails ");
			HttpSession session=request.getSession();
			DynaActionForm frmOnlinePreAuth=(DynaActionForm)form;
			ArrayList<ActivityDetailsVO> preauthActivities	=	(ArrayList<ActivityDetailsVO>) (session.getAttribute("preauthActivities")==null?new ArrayList<ActivityDetailsVO>():session.getAttribute("preauthActivities"));

			int rownum	=	Integer.parseInt(request.getParameter("rownum"));
			ActivityDetailsVO activityDetailsVO	=	preauthActivities.get(rownum);
			
			preauthActivities.remove(activityDetailsVO);
			request.setAttribute("successMsg","Diagnosis Details Deleted Successfully");	
			
			request.getSession().setAttribute("preauthActivities", preauthActivities);
			request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
			String fromFlag	=	(String) session.getAttribute("fromFlag");
			if("preAuthEnhance".equals(fromFlag))
				return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
			else
				return this.getForward(strONLINEPREAUTH, mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of deleteActivityDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	
	public ActionForward deleteDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside PreAuthGenealAction deleteDrugDetails ");
			HttpSession session=request.getSession();
			DynaActionForm frmOnlinePreAuth=(DynaActionForm)form;
			ArrayList<DrugDetailsVO> preauthDrugs	=	(ArrayList<DrugDetailsVO>) (session.getAttribute("preauthDrugs")==null?new ArrayList<ActivityDetailsVO>():session.getAttribute("preauthDrugs"));

			int rownum	=	Integer.parseInt(request.getParameter("rownum"));
			DrugDetailsVO drugDetailsVO	=	preauthDrugs.get(rownum);
			
			preauthDrugs.remove(drugDetailsVO);
			request.setAttribute("successMsg","Diagnosis Details Deleted Successfully");	
			
			request.getSession().setAttribute("preauthDrugs", preauthDrugs);
			request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
			String fromFlag	=	(String) session.getAttribute("fromFlag");
			if("preAuthEnhance".equals(fromFlag))
				return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
			else
				return this.getForward(strONLINEPREAUTH, mapping, request);
		}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of deleteDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	
	public ActionForward doAddDiagnosisDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doAddDiagnosisDetails method of OnlinePreAuthAction");
			setOnlineLinks(request);
			
			HttpSession session=request.getSession();
			TableData diagnosisCodeListData = new TableData();  //create new table data object
			diagnosisCodeListData.createTableInfo("DiagnosisCodeListTable",new ArrayList()); 
			session.setAttribute("diagnosisCodeListData",diagnosisCodeListData);//create the required grid table
			request.setAttribute("typeOfCodes", "Diagnosis");
			request.setAttribute("drugSearch", "diagSearch");
			return this.getForward(strAddActivityDetails, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
		}//end of catch(Exception exp)
	}//end of doAddDiagnosisDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	
	
public ActionForward doAddActivityDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
		log.debug("Inside the doAddActivityDetails method of OnlinePreAuthAction");
		setOnlineLinks(request);
		
		HttpSession session=request.getSession();
		TableData activityCodeListData = new TableData();  //create new table data object
		activityCodeListData.createTableInfo("ActivityCodeListTable",new ArrayList()); 
		session.setAttribute("activityCodeListData",activityCodeListData);//create the required grid table
		request.setAttribute("typeOfCodes", "Activity");
		request.setAttribute("drugSearch", "activitySearch");
		return this.getForward(strAddActivityDetails, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
	}//end of catch(Exception exp)
}//end of doAddActivityDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)




public ActionForward doAddDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	HttpServletResponse response) throws Exception{
try{
	log.debug("Inside the doAddDrugDetails method of OnlinePreAuthAction");
	setOnlineLinks(request);
	
	HttpSession session=request.getSession();
	TableData drugCodeListData = new TableData();  //create new table data object
	drugCodeListData.createTableInfo("DrugListTable",new ArrayList()); 
	session.setAttribute("drugCodeListData",drugCodeListData);//create the required grid table
	request.setAttribute("drugSearch", "drugSearch");
	request.setAttribute("typeOfCodes", "Drug");
	return this.getForward(strAddActivityDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
	return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
}//end of catch(Exception exp)
}//end of doAddDrugDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



public ActionForward diagCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
	log.debug("Inside PreAuthAction diagCodeSearch");
	setOnlineLinks(request);
	OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
	PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	HttpSession session=request.getSession();
	String drugSearch	=	(String) request.getParameter("drugSearch");
	TableData diagCodeListData = null;
	if(session.getAttribute("diagCodeListData") != null)
	{
		diagCodeListData = (TableData)session.getAttribute("diagCodeListData");
	}//end of if((request.getSession()).getAttribute("icdListData") != null)
	else
	{
		diagCodeListData = new TableData();
	}//end of else
	
	String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	//if the page number or sort id is clicked
	if(!strPageID.equals("") || !strSortID.equals(""))
	{
	if(strPageID.equals(""))
	{
		diagCodeListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
		diagCodeListData.modifySearchData("sort");//modify the search data                    
	}///end of if(!strPageID.equals(""))
	else
	{
	log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
	diagCodeListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
	request.setAttribute("drugSearch", drugSearch);
	request.setAttribute("typeOfCodes", "Diagnosis");
	return this.getForward(strAddActivityDetails, mapping, request);
	}//end of else
	}//end of if(!strPageID.equals("") || !strSortID.equals(""))
	else{
		diagCodeListData.createTableInfo("DiagnosisCodeListTable",null);
		diagCodeListData.setSearchData(this.populateDiagCodeSearchCriteria((DynaActionForm)form,request));
		diagCodeListData.modifySearchData("search");				
	}//end of else
	
	ArrayList alActivityCodeList=null;
	alActivityCodeList= preAuthObject.getDiagnosisCodeList(diagCodeListData.getSearchData());
	diagCodeListData.setData(alActivityCodeList, "search");
	//set the table data object to session
	request.getSession().setAttribute("diagCodeListData",diagCodeListData);
	request.setAttribute("drugSearch", drugSearch);
	request.setAttribute("typeOfCodes", "Diagnosis");
	return this.getForward(strAddActivityDetails, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
}//end of catch(Exception exp)
}//end of diagCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
public ActionForward drugCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
	try{
		log.debug("Inside PreAuthAction drugCodeSearch");
		setOnlineLinks(request);
		OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
		HttpSession session=request.getSession();
		DynaActionForm frmOnlinePreAuth=(DynaActionForm)session.getAttribute("frmOnlinePreAuth");
		String drugSearch	=	(String) request.getParameter("drugSearch");
		TableData drugCodeListData = null;
		if(session.getAttribute("drugCodeListData") != null)
		{
			drugCodeListData = (TableData)session.getAttribute("drugCodeListData");
		}//end of if((request.getSession()).getAttribute("icdListData") != null)
		else
		{
			drugCodeListData = new TableData();
		}//end of else
		
		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(strPageID.equals(""))
			{
				drugCodeListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
				drugCodeListData.modifySearchData("sort");//modify the search data                    
			}///end of if(!strPageID.equals(""))
			else
			{
			log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
			drugCodeListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			request.setAttribute("drugSearch", drugSearch);
			request.setAttribute("typeOfCodes", "Drug");
			return this.getForward(strAddActivityDetails, mapping, request);
			}//end of else
          }//end of if(!strPageID.equals("") || !strSortID.equals(""))
		else{
			drugCodeListData.createTableInfo("DrugListTable",null);
			drugCodeListData.setSearchData(this.populateActivityCodeSearchCriteria((DynaActionForm)form,request,"drug",frmOnlinePreAuth.getString("enrollId")));
			drugCodeListData.modifySearchData("search");				
		}//end of else
		
		ArrayList alActivityCodeList=null;
		alActivityCodeList= onlinePreAuthManager.getDrugCodeList(drugCodeListData.getSearchData());
		drugCodeListData.setData(alActivityCodeList, "search");
		//set the table data object to session
		request.getSession().setAttribute("drugCodeListData",drugCodeListData);
		request.setAttribute("drugSearch", drugSearch);
		request.setAttribute("typeOfCodes", "Drug");
		return this.getForward(strAddActivityDetails, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of drugCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



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
public ActionForward activityCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
	try{
		log.debug("Inside PreAuthAction activityCodeSearch");
		setOnlineLinks(request);
		OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
		HttpSession session=request.getSession();
		DynaActionForm frmOnlinePreAuth=(DynaActionForm)session.getAttribute("frmOnlinePreAuth");
		String searchType=((DynaActionForm) form).getString("sSearchType");
		TableData activityCodeListData = null;
		if(session.getAttribute("activityCodeListData") != null)
		{
			activityCodeListData = (TableData)session.getAttribute("activityCodeListData");
		}//end of if((request.getSession()).getAttribute("icdListData") != null)
		else
		{
			activityCodeListData = new TableData();
		}//end of else
		
		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(strPageID.equals(""))
			{
				activityCodeListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
				activityCodeListData.modifySearchData("sort");//modify the search data                    
			}///end of if(!strPageID.equals(""))
			else
			{
			log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
			activityCodeListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			request.setAttribute("typeOfCodes", "Activity");
			request.setAttribute("drugSearch", request.getParameter("drugSearch"));
			return this.getForward(strAddActivityDetails, mapping, request);
			}//end of else
          }//end of if(!strPageID.equals("") || !strSortID.equals(""))
		else{
			activityCodeListData.createTableInfo("ActivityCodeListTable",null);
			activityCodeListData.setSearchData(this.populateActivityCodeSearchCriteria((DynaActionForm)form,request,"activity",frmOnlinePreAuth.getString("enrollId")));
			activityCodeListData.modifySearchData("search");	
			if("ACT".equals(searchType)){
				((Column)((ArrayList)activityCodeListData.getTitle()).get(3)).setVisibility(false);
			}
			
			
		}//end of else
		
		ArrayList alActivityCodeList=null;
		alActivityCodeList= onlinePreAuthManager.getActivityCodeList(activityCodeListData.getSearchData(),"CPT");
		activityCodeListData.setData(alActivityCodeList, "search");
		//set the table data object to session
		request.getSession().setAttribute("activityCodeListData",activityCodeListData);
		request.setAttribute("typeOfCodes", "Activity");
		request.setAttribute("drugSearch", request.getParameter("drugSearch"));
		return this.getForward(strAddActivityDetails, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of activityCodeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



public ActionForward doSelectDiagCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
	setOnlineLinks(request);
	log.debug("Inside OnlinePreAuthAction doSelectDiagCode ");
	HttpSession session=request.getSession();
	
	TableData diagCodeListData = (TableData)session.getAttribute("diagCodeListData");
	DiagnosisDetailsVO diagnosisDetailsVO=(DiagnosisDetailsVO)diagCodeListData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
	DynaActionForm frmActivitySearch	=	(DynaActionForm)form;
	
	 DynaActionForm frmOnlinePreAuth	=	(DynaActionForm)session.getAttribute("frmOnlinePreAuth");
	//activityDetailsVO	=	onlinePreAuthManager.getActivityCodeDeails(frmActivitySearch.get("sSearchType"),"");
	CashlessDetailVO cashlessDetailVO 	= 	getFormValues(frmOnlinePreAuth,mapping,request);

	/*cashlessDetailVO	=	(CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmOnlinePreAuth",
			this,mapping,request);*/
	cashlessDetailVO.setDiagnosisDetailsVO(diagnosisDetailsVO);
	frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
	ArrayList<CacheObject> alEncountertypes	=	(ArrayList<CacheObject>) session.getAttribute("encounterTypes");
	frmOnlinePreAuth.set("encounterTypes", alEncountertypes);
	frmOnlinePreAuth.set("sSearchType", frmActivitySearch.get("sSearchType")); 
	request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
	
	String fromFlag	=	(String) session.getAttribute("fromFlag");
	if("preAuthEnhance".equals(fromFlag))
		return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
	else
		return mapping.findForward(strONLINEPREAUTH);
}//end of try
catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
}//end of catch(Exception exp)
}//end of doSelectDiagCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


public ActionForward doSelectActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
	setOnlineLinks(request);
	log.debug("Inside OnlinePreAuthAction doSelectActivityCode ");
	HttpSession session=request.getSession();
	TableData activityCodeListData = (TableData)session.getAttribute("activityCodeListData");
	ActivityDetailsVO activityDetailsVO=(ActivityDetailsVO)activityCodeListData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
	DynaActionForm frmActivitySearch	=	(DynaActionForm)form;
	 DynaActionForm frmOnlinePreAuth	=	(DynaActionForm)session.getAttribute("frmOnlinePreAuth");
	//activityDetailsVO	=	onlinePreAuthManager.getActivityCodeDeails(frmActivitySearch.get("sSearchType"),"");
	CashlessDetailVO cashlessDetailVO 	= 	getFormValues(frmOnlinePreAuth,mapping,request);

	activityDetailsVO.setSearchType(frmActivitySearch.get("sSearchType").toString());
	activityDetailsVO.setActivityServiceType("ACD");//Setting By Default Tariff to Service Type
	cashlessDetailVO.setActivityDetailsVO(activityDetailsVO);
	frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
	ArrayList<CacheObject> alEncountertypes	=	(ArrayList<CacheObject>) session.getAttribute("encounterTypes");
	frmOnlinePreAuth.set("encounterTypes", alEncountertypes);
	frmOnlinePreAuth.set("sSearchType", frmActivitySearch.get("sSearchType")); 
	request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
	
	String fromFlag	=	(String) session.getAttribute("fromFlag");
	if("preAuthEnhance".equals(fromFlag))
		return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
	else
		return mapping.findForward(strONLINEPREAUTH);
}//end of try
catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
}//end of catch(Exception exp)
}//end of doSelectActivityCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


public ActionForward doSelectDrugCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
	setOnlineLinks(request);
	log.debug("Inside OnlinePreAuthAction doSelectDrugCode ");
	HttpSession session=request.getSession();
	
	TableData drugCodeListData = (TableData)session.getAttribute("drugCodeListData");
	DrugDetailsVO drugDetailsVO=(DrugDetailsVO)drugCodeListData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
	DynaActionForm frmActivitySearch	=	(DynaActionForm)form;
	DynaActionForm frmOnlinePreAuth	=	(DynaActionForm)session.getAttribute("frmOnlinePreAuth");
	//activityDetailsVO	=	onlinePreAuthManager.getActivityCodeDeails(frmActivitySearch.get("sSearchType"),"");
	CashlessDetailVO cashlessDetailVO 	= 	getFormValues(frmOnlinePreAuth,mapping,request);
	drugDetailsVO.setSearchType(frmActivitySearch.get("sSearchType").toString());
	cashlessDetailVO.setDrugDetailsVO(drugDetailsVO);
	frmOnlinePreAuth	=	setFormValues(cashlessDetailVO,mapping,request);
	ArrayList<CacheObject> alEncountertypes	=	(ArrayList<CacheObject>) session.getAttribute("encounterTypes");
	frmOnlinePreAuth.set("encounterTypes", alEncountertypes);
	frmOnlinePreAuth.set("sSearchType", frmActivitySearch.get("sSearchType")); 
	if(drugDetailsVO.getNoOfUnits() != null) {
		frmOnlinePreAuth.set("noOfUnits",drugDetailsVO.getNoOfUnits().toString());
	}else {
		frmOnlinePreAuth.set("noOfUnits","");
	}
	
	if(drugDetailsVO.getGranularUnit() != null) {
		frmOnlinePreAuth.set("granularUnit",drugDetailsVO.getGranularUnit().toString());
	}else {
			frmOnlinePreAuth.set("granularUnit","");
	}
	request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
	request.setAttribute("gran", drugDetailsVO.getGranularUnit());
	String fromFlag	=	(String) session.getAttribute("fromFlag");
	request.setAttribute("focusId", request.getParameter("focusId"));
	if("preAuthEnhance".equals(fromFlag))
		return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
	else
		return mapping.findForward(strONLINEPREAUTH);
}//end of try
catch(TTKException expTTK)
	{
	return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
	{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
}//end of catch(Exception exp)
}//end of doSelectDrugCode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


public ActionForward doSaveActivities(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlinePreAuthAction doSaveActivities");
		HttpSession session=request.getSession();
		OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
		ArrayList<ActivityDetailsVO> preauthActivities	=	(ArrayList<ActivityDetailsVO>) (session.getAttribute("preauthActivities")==null?new ArrayList<ActivityDetailsVO>():session.getAttribute("preauthActivities"));
		ArrayList<CacheObject> alEncountertypes	=	(ArrayList<CacheObject>) session.getAttribute("encounterTypes");
		DynaActionForm frmOnlinePreAuth		=	(DynaActionForm)form;
		CashlessDetailVO cashlessDetailVO 	= 	getFormValues(frmOnlinePreAuth,mapping,request);
		String actOrDrug	=	request.getParameter("focusId");
		Long tariffSeqId	=	new Long(request.getParameter("tariffSeqId")==""?"0":request.getParameter("tariffSeqId"));//null;//
		if(tariffSeqId==null || "0".equals(tariffSeqId.toString())){
			tariffSeqId	=	(Long) session.getAttribute("tariffSeqId");
		}
		String benefitTemp	=	request.getParameter("benefitTemp");
		session.setAttribute("tariffSeqId", tariffSeqId);
		ActivityDetailsVO activityDetailsVO	=	new ActivityDetailsVO();
		activityDetailsVO	=	cashlessDetailVO.getActivityDetailsVO();
	    long activityicdcount =	activityDetailsVO.getActivityicdcountflag();
		String toothNO	=	activityDetailsVO.getToothNo();
		
		String serviceType	=	activityDetailsVO.getActivityServiceType();
		if(tariffSeqId!=null || tariffSeqId!=new Long(0))
		{
			if("TAR".equals(activityDetailsVO.getSearchType()))
				activityDetailsVO.setActivitySeqId(activityDetailsVO.getTariffSeqId());
			else if("ACT".equals(activityDetailsVO.getSearchType()))
				activityDetailsVO.setActivitySeqId(activityDetailsVO.getActivityDtlSeqId());
			else activityDetailsVO.setActivitySeqId(tariffSeqId);
		}
		
		Float qty	=	activityDetailsVO.getQuantity();
		BigDecimal reqAmt=	activityDetailsVO.getProviderRequestedAmt();
		BigDecimal netAmt	=	null;
		cashlessDetailVO	=	(CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmOnlinePreAuth",
				this,mapping,request);
	    UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
		    if("ACD".equals(serviceType) && "TAR".equals(activityDetailsVO.getSearchType()))//For Tariff and Service type is Activity
		    {
		    	activityDetailsVO	=	 onlinePreAuthManager.getActivityCodeDetails(userSecurityProfile.getHospSeqId(),cashlessDetailVO.getEnrollId(),activityDetailsVO.getActivitySeqId(),cashlessDetailVO.getTreatmentDate(),activityDetailsVO.getSearchType(),actOrDrug);
				netAmt	=	activityDetailsVO.getNetAmount();
		    	netAmt	=	netAmt.multiply(new BigDecimal(qty));
	    		activityDetailsVO.setGrossAmount(netAmt.add(activityDetailsVO.getDiscount()));
		    }else if("ACD".equals(serviceType) && "ACT".equals(activityDetailsVO.getSearchType()))//For Master and Service type is Activity
		    {
		    	activityDetailsVO.setActivityCode(activityDetailsVO.getActivityCode());
		    	activityDetailsVO.setActivityCodeDesc(activityDetailsVO.getActivityCodeDesc());
		    	activityDetailsVO.setInternalCode(activityDetailsVO.getInternalCode());
		    	netAmt	=	reqAmt;//  from activity Table
		    	activityDetailsVO.setNetAmount(netAmt);
		    	netAmt	=	netAmt.multiply(new BigDecimal(qty));
		    	activityDetailsVO.setGrossAmount(netAmt);
		    }
		    else if("SCD".equals(serviceType)){//Service type is Service
		    	activityDetailsVO.setActivityCode(activityDetailsVO.getActivityServiceCode());
		    	activityDetailsVO.setActivityCodeDesc(activityDetailsVO.getActivityServiceCodeDesc());
		    	activityDetailsVO.setInternalCode(activityDetailsVO.getServiceInternalCode());
		    	netAmt	=	reqAmt;//  from activity Table
		    	activityDetailsVO.setNetAmount(netAmt);
		    	netAmt	=	netAmt.multiply(new BigDecimal(qty));
		    	activityDetailsVO.setGrossAmount(netAmt);
		   
		    }else{
			    activityDetailsVO	=	 onlinePreAuthManager.getActivityCodeDetails(userSecurityProfile.getHospSeqId(),cashlessDetailVO.getEnrollId(),activityDetailsVO.getActivitySeqId(),cashlessDetailVO.getTreatmentDate(),"TAR","");
			    if(null==activityDetailsVO)
				    {
				    	TTKException expTTK = new TTKException();
						expTTK.setMessage("error.netAmountNull");
						throw new TTKException(expTTK , "netAmountNull");
				    }
				netAmt	=	activityDetailsVO.getNetAmount();
		    	netAmt	=	netAmt.multiply(new BigDecimal(qty));
		    }
	    	activityDetailsVO.setDiscount(activityDetailsVO.getDiscount().multiply(new BigDecimal(qty)));
	    	if(serviceType==null&& activityDetailsVO.getSearchType().equals("")){
		    		activityDetailsVO.setGrossAmount(reqAmt.multiply(new BigDecimal(qty)));
	    		} else if(serviceType!=null&& activityDetailsVO.getSearchType().equals("")){
	    			activityDetailsVO.setGrossAmount(reqAmt.multiply(new BigDecimal(qty)));
	    		}
		    	else{
		    		activityDetailsVO.setGrossAmount(netAmt);
	    		}
		activityDetailsVO.setQuantity(qty);
		activityDetailsVO.setToothNo(toothNO);
		netAmt	=	activityDetailsVO.getNetAmount();
double ttlAmt	=	0.0d;
		if(netAmt==null){
			/*TTKException expTTK = new TTKException();
			expTTK.setMessage("error.netAmountNull");
			throw new TTKException(expTTK , "netAmountNull");*/
			netAmt	=	new BigDecimal(0);
		}else{
			ttlAmt	=	qty.doubleValue()*netAmt.doubleValue();
		}
		activityDetailsVO.setUnitPrice(reqAmt.setScale(2, RoundingMode.HALF_UP));
		activityDetailsVO.setApprovedAmount(new BigDecimal(ttlAmt).setScale(2, RoundingMode.HALF_UP));
		//for net amnt calculation

		ttlAmt	=	activityDetailsVO.getApprovedAmount().doubleValue();//-activityDetailsVO.getDiscount().doubleValue();
		activityDetailsVO.setNetAmount(new BigDecimal(ttlAmt).setScale(2, RoundingMode.HALF_UP));
		activityDetailsVO.setApprovedAmount(new BigDecimal(ttlAmt).setScale(2, RoundingMode.HALF_UP));
		activityDetailsVO.setActivityicdcountflag(activityicdcount);
		String fromFlag	=	(String) session.getAttribute("fromFlag");
		
		if("preAuthEnhance".equals(fromFlag)){
		activityDetailsVO.setEnhancedFlag("Y");
		}
		//System.out.println("enhance act::"+activityDetailsVO.getEnhancedFlag());
		preauthActivities.add(activityDetailsVO);
		//activityDetailsVO1.setActivityCode("");activityDetailsVO1.setActivityCodeDesc("");activityDetailsVO1.setQuantity(new Float(0));
		//cashlessDetailVO.setActivityDetailsVO(new ActivityDetailsVO());
		frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
		frmOnlinePreAuth.set("encounterTypes", alEncountertypes);
		request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
		session.setAttribute("preauthActivities", preauthActivities);
		
		// focus object Code S T A R T S
		request.setAttribute("focusId", request.getParameter("focusId"));

		// focus object Code E N D S 
	
		if("preAuthEnhance".equals(fromFlag))
			return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);	
		else
			return mapping.findForward(strONLINEPREAUTH);
		}//end of try
	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of doSaveActivities(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



public ActionForward doSaveDrugs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlinePreAuthAction doSaveDrugs");
		HttpSession session=request.getSession();
		OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
		ArrayList<DrugDetailsVO> preauthDrugs	=	(ArrayList<DrugDetailsVO>) (session.getAttribute("preauthDrugs")==null?new ArrayList<DrugDetailsVO>():session.getAttribute("preauthDrugs"));
		ArrayList<CacheObject> alEncountertypes	=	(ArrayList<CacheObject>) session.getAttribute("encounterTypes");
		DynaActionForm frmOnlinePreAuth		=	(DynaActionForm)form;
		CashlessDetailVO cashlessDetailVO 	= 	getFormValues(frmOnlinePreAuth,mapping,request);
		
		
		
		DrugDetailsVO drugDetailsVO	=	new DrugDetailsVO();
		drugDetailsVO	=	cashlessDetailVO.getDrugDetailsVO();
		Long tariffSeqId	=	new Long(request.getParameter("tariffSeqId").equals("")?"0":request.getParameter("tariffSeqId"));//null;//
		if(tariffSeqId!=null)
		{
			if("TAR".equals(drugDetailsVO.getSearchType()) || "ACT".equals(drugDetailsVO.getSearchType()))
				drugDetailsVO.setDrugSeqId(drugDetailsVO.getDrugSeqId());
			else 
				drugDetailsVO.setDrugSeqId(tariffSeqId);
		}
		Float qty	=	drugDetailsVO.getDrugqty();
		String unit	=	drugDetailsVO.getDrugunit();
		String posology	=	drugDetailsVO.getPosology();
		String days	=	drugDetailsVO.getDays();
		//cashlessDetailVO	=	(CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmOnlinePreAuth",
		//		this,mapping,request);
	    UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	    String actOrDrug	=	request.getParameter("focusId");
	    drugDetailsVO	=	 onlinePreAuthManager.getDrugCodeDetails(userSecurityProfile.getHospSeqId(),cashlessDetailVO.getEnrollId(),drugDetailsVO.getDrugSeqId(),cashlessDetailVO.getTreatmentDate(),actOrDrug);
	    
	    Float unitPrice	=	(float) (drugDetailsVO.getUnitPrice()==null?0.0:drugDetailsVO.getUnitPrice().floatValue());
	    Float packagePrice=	(float) (drugDetailsVO.getPackagePrice()==null?0.0:drugDetailsVO.getPackagePrice().floatValue());
	    BigDecimal granularUnit	=	drugDetailsVO.getGranularUnit();
	    
	    float ttlVal=	0;
	    if("PCKG".equals(unit)){
	    	ttlVal	=	qty*packagePrice;
	    	drugDetailsVO.setUnitPrice(new BigDecimal(packagePrice));
	    }
	    else{
	    	ttlVal	=	qty*unitPrice;
	    }
	    
	    drugDetailsVO.setDrugqty(qty);
	    drugDetailsVO.setPosology(posology);
	    drugDetailsVO.setDays(days);
	    drugDetailsVO.setDrugunit(unit);
	    drugDetailsVO.setNetPriceAftDisc(new BigDecimal(ttlVal).setScale(2,RoundingMode.HALF_UP));
	    drugDetailsVO.setGrossAmount(new BigDecimal(ttlVal).setScale(2,RoundingMode.HALF_UP));
String fromFlag	=	(String) session.getAttribute("fromFlag");
		
		if("preAuthEnhance".equals(fromFlag)){
		
	    drugDetailsVO.setEnhancedYN("Y");
		}
		preauthDrugs.add(drugDetailsVO);
		//drugDetailsVO1.setDrugCode("");drugDetailsVO1.setDrugDesc("");drugDetailsVO1.setDays("");drugDetailsVO1.setDrugqty(Float.parseFloat("0"));
		cashlessDetailVO.setDrugDetailsVO(new DrugDetailsVO());
		frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
		frmOnlinePreAuth.set("encounterTypes", alEncountertypes);
		request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
		session.setAttribute("preauthDrugs", preauthDrugs);
		request.setAttribute("focusId", request.getParameter("focusId"));
		if("preAuthEnhance".equals(fromFlag))
			return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
		else
			return mapping.findForward(strONLINEPREAUTH);
		}//end of try
	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of doSaveDrugs(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)







public ActionForward doDiagCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside OnlinePreAuthAction doDiagCodeForward");
setOnlineLinks(request);
TableData diagCodeListData = (TableData)request.getSession().getAttribute("diagCodeListData");
PreAuthManager preAuthObject=this.getPreAuthManagerObject();diagCodeListData.modifySearchData(strForward);//modify the search data
ArrayList alPreauthList = preAuthObject.getDiagnosisCodeList(diagCodeListData.getSearchData());
diagCodeListData.setData(alPreauthList, strForward);//set the table data
request.getSession().setAttribute("diagCodeListData",diagCodeListData);   //set the table data object to session
request.setAttribute("typeOfCodes", "Diagnosis");
request.setAttribute("drugSearch", request.getParameter("drugSearch"));
return this.getForward(strAddActivityDetails, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processOnlineExceptions(request, mapping, new TTKException(exp,strONLINEPREAUTH));
}//end of catch(Exception exp)
}//end of doDiagCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
public ActionForward doDiagCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside OnlinePreAuthAction doDiagCodeBackward");
setOnlineLinks(request);
TableData diagCodeListData = (TableData)request.getSession().getAttribute("diagCodeListData");
PreAuthManager preAuthObject=this.getPreAuthManagerObject();diagCodeListData.modifySearchData(strForward);//modify the search data
diagCodeListData.modifySearchData(strBackward);//modify the search data
ArrayList alPreauthList = preAuthObject.getDiagnosisCodeList(diagCodeListData.getSearchData());
diagCodeListData.setData(alPreauthList, strBackward);//set the table data
request.getSession().setAttribute("diagCodeListData",diagCodeListData);   //set the table data object to session
request.setAttribute("typeOfCodes", "Diagnosis");
request.setAttribute("drugSearch", request.getParameter("drugSearch"));
return this.getForward(strAddActivityDetails, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strONLINEPREAUTH));
}//end of catch(Exception exp)
}//end of doDiagCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)






public ActionForward doActivityCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside OnlinePreAuthAction doActivityCodeForward");
setOnlineLinks(request);
TableData activityCodeListData = (TableData)request.getSession().getAttribute("activityCodeListData");
OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
activityCodeListData.modifySearchData(strForward);//modify the search data
ArrayList alPreauthList = onlinePreAuthManager.getActivityCodeList(activityCodeListData.getSearchData(),"ACT");
activityCodeListData.setData(alPreauthList, strForward);//set the table data
request.getSession().setAttribute("activityCodeListData",activityCodeListData);   //set the table data object to session
request.setAttribute("typeOfCodes", "Activity");
request.setAttribute("drugSearch", request.getParameter("drugSearch"));
return this.getForward(strAddActivityDetails, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processOnlineExceptions(request, mapping, new TTKException(exp,strONLINEPREAUTH));
}//end of catch(Exception exp)
}//end of doActivityCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
public ActionForward doActivityCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside OnlinePreAuthAction doActivityCodeBackward");
setOnlineLinks(request);
TableData activityCodeListData = (TableData)request.getSession().getAttribute("activityCodeListData");
OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
activityCodeListData.modifySearchData(strBackward);//modify the search data
ArrayList alPreauthList = onlinePreAuthManager.getActivityCodeList(activityCodeListData.getSearchData(),"ACT");
activityCodeListData.setData(alPreauthList, strBackward);//set the table data
request.getSession().setAttribute("activityCodeListData",activityCodeListData);   //set the table data object to session
request.setAttribute("typeOfCodes", "Activity");
request.setAttribute("drugSearch", request.getParameter("drugSearch"));
return this.getForward(strAddActivityDetails, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strONLINEPREAUTH));
}//end of catch(Exception exp)
}//end of doActivityCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)







public ActionForward doDrugCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside OnlinePreAuthAction doDrugCodeForward");
String drugSearch	=	(String) request.getParameter("drugSearch");
setOnlineLinks(request);
TableData drugCodeListData = (TableData)request.getSession().getAttribute("drugCodeListData");
OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
drugCodeListData.modifySearchData(strForward);//modify the search data
ArrayList alPreauthList = onlinePreAuthManager.getDrugCodeList(drugCodeListData.getSearchData());
drugCodeListData.setData(alPreauthList, strForward);//set the table data
request.getSession().setAttribute("drugCodeListData",drugCodeListData);   //set the table data object to session
request.setAttribute("typeOfCodes", "Drug");
request.setAttribute("drugSearch", drugSearch);
return this.getForward(strAddActivityDetails, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processOnlineExceptions(request, mapping, new TTKException(exp,strONLINEPREAUTH));
}//end of catch(Exception exp)
}//end of doDrugCodeForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


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
public ActionForward doDrugCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
log.debug("Inside OnlinePreAuthAction doDrugCodeBackward");
setOnlineLinks(request);
String drugSearch	=	(String) request.getParameter("drugSearch");
TableData drugCodeListData = (TableData)request.getSession().getAttribute("drugCodeListData");
OnlinePreAuthManager onlinePreAuthManager=this.getOnlineAccessManagerObject();
drugCodeListData.modifySearchData(strBackward);//modify the search data
ArrayList alPreauthList = onlinePreAuthManager.getDrugCodeList(drugCodeListData.getSearchData());
drugCodeListData.setData(alPreauthList, strBackward);//set the table data
request.getSession().setAttribute("drugCodeListData",drugCodeListData);   //set the table data object to session
request.setAttribute("typeOfCodes", "Drug");
request.setAttribute("drugSearch", drugSearch);
return this.getForward(strAddActivityDetails, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
	return this.processOnlineExceptions(request, mapping, new TTKException(exp,strONLINEPREAUTH));
}//end of catch(Exception exp)
}//end of doDrugCodeBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

public ActionForward closeActivityCodes(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
setOnlineLinks(request);
log.debug("Inside OnlinePreAuthAction closeActivityCodes");
//	return mapping.findForward(strONLINEPREAUTH);
HttpSession session=request.getSession();
String fromFlag	=	(String) session.getAttribute("fromFlag");
if("preAuthEnhance".equals(fromFlag))
return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
else
	return mapping.findForward(strONLINEPREAUTH);
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processOnlineExceptions(request, mapping, new TTKException(exp,strONLINEPREAUTH));
}//end of catch(Exception exp)

}//end of closeActivityCodes(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



public ActionForward doSaveOnlinePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlinePreAuthAction doSaveOnlinePreAuth");
		HttpSession session=request.getSession();
		DynaActionForm frmOnlinePreAuth	=	(DynaActionForm)session.getAttribute("frmOnlinePreAuth");
		
		
		request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
		request.setAttribute("readonly", "true");
		
		return mapping.findForward(strONLINEPREAUTH);
		}//end of try
	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of doSaveOnlinePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

public ActionForward doSavePartialPreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside the doSavePartialPreAuth method of OnlinePreAuthAction");
			DynaActionForm frmOnlinePreAuth =(DynaActionForm)form;
			HttpSession session	=	request.getSession();
			String fromFlag	=	(String) session.getAttribute("fromFlag");
			// UPLOAD FILE STARTS

			
			CashlessDetailVO cashlessDetailVO 	= 	getFormValues(frmOnlinePreAuth,mapping,request);
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
			OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
			Long	lPreAuthIntSeqId	=	null;
			String[] arrResult	=	new String[2];
			//cashlessDetailVO.setBenifitTypeCode(request.getSession().getAttribute("benefitText").toString());
			
			//String benifitTypeVal	=	(String) request.getSession().getAttribute("benifitTypeVal");
			/*arrResult	= 	onlinePreAuthManager.savePartialPreAuthDetails(cashlessDetailVO,null,userSecurityProfile,"","Y",formFile);*/
			if("preAuthEnhance".equals(fromFlag))
				arrResult	= 	onlinePreAuthManager.savePartialPreAuthDetails(cashlessDetailVO,null,userSecurityProfile,"","Y");
			else
				arrResult	= 	onlinePreAuthManager.savePartialPreAuthDetails(cashlessDetailVO,null,userSecurityProfile,"","N");
			/*arrResult	= 	onlinePreAuthManager.savePartialPreAuthDetails(cashlessDetailVO,null,userSecurityProfile,"","N",formFile);*/
			lPreAuthIntSeqId	=	new Long(arrResult[0]);
			
			String treatmentDate	=	frmOnlinePreAuth.getString("treatmentDate");
			String clinicianId		=	frmOnlinePreAuth.getString("clinicianId");
			DiagnosisDetailsVO diagnosisDetailsVO	=	cashlessDetailVO.getDiagnosisDetailsVO();
			
			ArrayList<DiagnosisDetailsVO> diagnosis	=	(ArrayList<DiagnosisDetailsVO>)(request.getSession().getAttribute("preauthDiagnosis")==null?new ArrayList<>():request.getSession().getAttribute("preauthDiagnosis"));
			//int iResultDiag	=	onlinePreAuthManager.saveDiagnosisDetails(diagnosis,lPreAuthIntSeqId,userSecurityProfile.getUSER_SEQ_ID());
			
			ArrayList<ActivityDetailsVO> alActivityDetails= (ArrayList<ActivityDetailsVO>)(request.getSession().getAttribute("preauthActivities")==null?new ArrayList<>():request.getSession().getAttribute("preauthActivities"));
			
			
			ArrayList<DrugDetailsVO> alDrugDetails= (ArrayList<DrugDetailsVO>)(request.getSession().getAttribute("preauthDrugs")==null?new ArrayList<>():request.getSession().getAttribute("preauthDrugs"));

			StringBuffer sb	=	new StringBuffer();
			StringBuffer sbQty	=	new StringBuffer();
			String seqIds	=	"";
			String seqQty	=	"";
			//TO DELETE THE CHILD TABLE DATA WHICH ARE NOT IN FRONT END - ICD CODES - DIAGNOSIS
			Long lSuccess	=	null;
			if(diagnosis.size()>0){
				for(DiagnosisDetailsVO vo :diagnosis)
				{	
					sb.append(0+"|"+vo.getIcdCode()+"|"+vo.getPrimaryAilment()+"|"+userSecurityProfile.getUSER_SEQ_ID()+",");
				}
			}
			if(sb.length()>0)
				seqIds		=	sb.substring(0, sb.length()-1);
			//if(seqIds!=null && !"".equals(seqIds))
				lSuccess	=	onlinePreAuthManager.deleteExistngDataForPreAuth(seqIds,lPreAuthIntSeqId,"ICD",seqQty,userSecurityProfile.getHospSeqId(),userSecurityProfile.getUSER_SEQ_ID(),"",request);
			
			
			//TO DELETE THE CHILD TABLE DATA WHICH ARE NOT IN FRONT END - ACTIVITY CODES - ACTIVITIES AND DRUGS AT SAME TIME AS DATA IS IN SAME TABLE
			sb	=	new StringBuffer();
			seqIds	=	"";
			if(alActivityDetails.size()>0){
				for(ActivityDetailsVO actVo :alActivityDetails)
				{
					sb.append(actVo.getActivityCode()+"|");
					sb.append(actVo.getQuantity()+",");
					
				}
			}if(alDrugDetails.size()>0){
				for(DrugDetailsVO drugVo :alDrugDetails)
				{
					sb.append(drugVo.getDrugCode()+"|");
					sb.append(drugVo.getDrugqty()+",");
				}
			}
			if(sb.length()>0){
				seqIds		=	sb.substring(0, sb.length()-1);
				//seqQty		=	sbQty.substring(0, sbQty.length()-1);
			}
			//if(seqIds!=null && !"".equals(seqIds))
			int iResultActi	=	0;
			int iResultDrug	=	0;
				lSuccess	=	onlinePreAuthManager.deleteExistngDataForPreAuth(seqIds,lPreAuthIntSeqId,"ACT",seqQty,userSecurityProfile.getHospSeqId(),userSecurityProfile.getUSER_SEQ_ID(),"",request);
				if(lSuccess>0){
					iResultActi	=	onlinePreAuthManager.saveActivitiesDetails(alActivityDetails,lPreAuthIntSeqId,userSecurityProfile.getUSER_SEQ_ID(),treatmentDate,clinicianId,"",request);
					iResultDrug	=	onlinePreAuthManager.saveDrugDetails(alDrugDetails,lPreAuthIntSeqId,userSecurityProfile.getUSER_SEQ_ID(),treatmentDate,clinicianId,"",request);
				
				}
				
				//-------------------Save the Dental related data--------------------
				ActionForm dentalForm=(ActionForm)frmOnlinePreAuth.get("dentalOrthoVO");
				DentalOrthoVO dentalOrthoVO=(DentalOrthoVO)FormUtils.getFormValues(dentalForm,"frmPreAuthDental",this,mapping,request);
				dentalOrthoVO.setPreAuthSeqid(lPreAuthIntSeqId);
				String lVal	=	onlinePreAuthManager.saveDentalDetails(dentalOrthoVO,"PAT",lPreAuthIntSeqId,userSecurityProfile.getUSER_SEQ_ID(),"",request);
	            //---------------------------------------------------------
				
			//retreiving Diagnosis codes
			/*diagnosis		=	onlinePreAuthManager.getDiagDetails(lPreAuthIntSeqId);
			request.getSession().setAttribute("preauthDiagnosis", diagnosis);*/
			
			//retreiving Activity codes
			/*diagnosis		=	onlinePreAuthManager.getActivityCodeDetails(hospSeqId, mode, activityCode, treatmentDate)(lPreAuthIntSeqId);
			request.getSession().setAttribute("preauthDiagnosis", diagnosis);*/
			
			Object[] preauthAllresult	=	onlinePreAuthManager.getOnlinePartialPreAuthDetails(lPreAuthIntSeqId);
    		
			cashlessDetailVO = (CashlessDetailVO) preauthAllresult[0];
			diagnosis = (ArrayList<DiagnosisDetailsVO>) preauthAllresult[1];
			alActivityDetails = (ArrayList<ActivityDetailsVO>) preauthAllresult[2];
			alDrugDetails = (ArrayList<DrugDetailsVO>) preauthAllresult[3];
			request.getSession().setAttribute("preauthDiagnosis", diagnosis);
			request.getSession().setAttribute("preauthActivities", alActivityDetails);
			request.getSession().setAttribute("preauthDrugs", alDrugDetails);
			
			ArrayList<CacheObject> alEncountertypes	=	new ArrayList<CacheObject>();
			alEncountertypes			=	onlinePreAuthManager.getEnounterTypes(cashlessDetailVO.getBenifitType());
			//GET ALL DATA FROM 
			request.getSession().setAttribute("lPreAuthIntSeqId", lPreAuthIntSeqId);
			request.getSession().setAttribute("preAuthStatus", "In-Progress");
			request.setAttribute("onlinePreAuthRefNO", arrResult[1]);
//			cashlessDetailVO.setPreAuthRefNo(arrResult[1]);
			cashlessDetailVO.setPreAuthSeqID(lPreAuthIntSeqId);
			frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
			frmOnlinePreAuth.set("encounterTypes", alEncountertypes);
			frmOnlinePreAuth.set("enhanceYN", request.getSession().getAttribute("enhanceYN"));
			request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
			request.setAttribute("updated", "message.savedSuccessfully");
			request.getSession().setAttribute("preAuthStatus", cashlessDetailVO.getStatus());
			request.getSession().setAttribute("claimStatus", null);
			request.getSession().setAttribute("isSubmitted", false);
			//request.setAttribute("MemberSave", "MemberSave"); 
			String strActiveLink	=	TTKCommon.getActiveSubLink(request);
			if("preAuthEnhance".equals(fromFlag))
				return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
			else
				return this.getForward(strONLINEPREAUTH, mapping, request);
			
		}//end of try
	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of Partial(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)



public ActionForward doSubmitOnlinePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.info("Inside the doSaveGeneral method of OnlinePreAuthAction");
			DynaActionForm frmOnlinePreAuth =(DynaActionForm)form;
			FormFile formFile1 = (FormFile)frmOnlinePreAuth.get("file1");
			FormFile formFile2 = (FormFile)frmOnlinePreAuth.get("file2");
			FormFile formFile3 = (FormFile)frmOnlinePreAuth.get("file3");
			FormFile formFile4 = (FormFile)frmOnlinePreAuth.get("file4");
			List<String> fileDesclist	=	new ArrayList<>();
			
			MaintenanceManager managerObject=this.getMaintenanceManagerObject();
			TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");   
			DateFormat df =new SimpleDateFormat("ddMMyyyy HH:mm:ss:S");
			df.setTimeZone(tz);
			ArrayList<FormFile> listOfFiles = new ArrayList<>();
			if(formFile1.getFileName()!=""){
				listOfFiles.add(formFile1);
				fileDesclist.add((String)frmOnlinePreAuth.get("description"));
			}
			if(formFile2.getFileName()!=""){
				listOfFiles.add(formFile2);
				fileDesclist.add((String)frmOnlinePreAuth.get("description2"));
			}
			if(formFile3.getFileName()!=""){
				listOfFiles.add(formFile3);
				fileDesclist.add((String)frmOnlinePreAuth.get("description3"));
			}
			if(formFile4.getFileName()!=""){
				listOfFiles.add(formFile4);
				fileDesclist.add((String)frmOnlinePreAuth.get("description4"));
			}
			HttpSession session	=	request.getSession();
			String fromFlag	=	(String) session.getAttribute("fromFlag");
			CashlessDetailVO cashlessDetailVO = null;
			cashlessDetailVO	=	getFormValues(frmOnlinePreAuth,mapping,request);
			UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
			OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
			/*Long	lPreAuthIntSeqId	=	(Long) request.getSession().getAttribute("lPreAuthIntSeqId"); */
			Long	lPreAuthIntSeqId = null;
			String[] arrResult	=	new String[2];
			if(request.getSession().getAttribute("benifitTypeVal") != null){
				cashlessDetailVO.setBenifitTypeCode((String)request.getSession().getAttribute("benifitTypeVal"));	
			}
			
			ArrayList<ActivityDetailsVO> alActivityDetails= (ArrayList<ActivityDetailsVO>)(request.getSession().getAttribute("preauthActivities")==null?new ArrayList<>():request.getSession().getAttribute("preauthActivities"));
			ArrayList<DiagnosisDetailsVO> diagnosis	=	(ArrayList<DiagnosisDetailsVO>)(request.getSession().getAttribute("preauthDiagnosis")==null?new ArrayList<>():request.getSession().getAttribute("preauthDiagnosis"));
			ArrayList<DrugDetailsVO> alDrugDetails	= (ArrayList<DrugDetailsVO>)(request.getSession().getAttribute("preauthDrugs")==null?new ArrayList<>():request.getSession().getAttribute("preauthDrugs"));
			
			if(alActivityDetails == null || alActivityDetails.size()<1 )
			{	
				if(alDrugDetails == null || alDrugDetails.size()<1 )
				{
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.Activity.or.Drug.not.added");     
					throw expTTK;
				}	
			}
			if(diagnosis == null || diagnosis.size()<1 )
			{	
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.diagnosis.not.added");     
					throw expTTK;
			}	
			
			if(session.getAttribute("lpreAuthIntSeqId")== null){
			if("preAuthEnhance".equals(fromFlag))
				arrResult	= 	onlinePreAuthManager.savePartialPreAuthDetails(cashlessDetailVO,null,userSecurityProfile,"","Y");
			else
				arrResult	= 	onlinePreAuthManager.savePartialPreAuthDetails(cashlessDetailVO,null,userSecurityProfile,"","N");

			/*arrResult	= 	onlinePreAuthManager.savePartialPreAuthDetails(cashlessDetailVO,null,userSecurityProfile,"","N",formFile);*/
			lPreAuthIntSeqId	=	new Long(arrResult[0]);
			session.setAttribute("lpreAuthIntSeqId", lPreAuthIntSeqId);
			}
			String treatmentDate	=	frmOnlinePreAuth.getString("treatmentDate");
			String clinicianId		=	frmOnlinePreAuth.getString("clinicianId");
			DiagnosisDetailsVO diagnosisDetailsVO	=	cashlessDetailVO.getDiagnosisDetailsVO();
			
	//		ArrayList<DiagnosisDetailsVO> diagnosis	=	(ArrayList<DiagnosisDetailsVO>)(request.getSession().getAttribute("preauthDiagnosis")==null?new ArrayList<>():request.getSession().getAttribute("preauthDiagnosis"));
			//int iResultDiag	=	onlinePreAuthManager.saveDiagnosisDetails(diagnosis,cashlessDetailVO.getPreAuthSeqID(),userSecurityProfile.getUSER_SEQ_ID());
			
	//		ArrayList<ActivityDetailsVO> alActivityDetails= (ArrayList<ActivityDetailsVO>)(request.getSession().getAttribute("preauthActivities")==null?new ArrayList<>():request.getSession().getAttribute("preauthActivities"));
			//int iResultActi	=	onlinePreAuthManager.saveActivitiesDetails(alActivityDetails,cashlessDetailVO.getPreAuthSeqID(),userSecurityProfile.getUSER_SEQ_ID(),treatmentDate,clinicianId);
			
	//		ArrayList<DrugDetailsVO> alDrugDetails	= (ArrayList<DrugDetailsVO>)(request.getSession().getAttribute("preauthDrugs")==null?new ArrayList<>():request.getSession().getAttribute("preauthDrugs"));
			//int iResultDrug	=	onlinePreAuthManager.saveDrugDetails(alDrugDetails,cashlessDetailVO.getPreAuthSeqID(),userSecurityProfile.getUSER_SEQ_ID(),treatmentDate,clinicianId);
			
			StringBuffer sb	=	new StringBuffer();
			StringBuffer sbQty	=	new StringBuffer();
			String seqIds	=	"";
			String seqQty	=	"";
			//TO DELETE THE CHILD TABLE DATA WHICH ARE NOT IN FRONT END - ICD CODES - DIAGNOSIS
			Long lSuccess	=	null;
			if(diagnosis.size()>0){
				for(DiagnosisDetailsVO vo :diagnosis)
				{	
					sb.append(0+"|"+vo.getIcdCode()+"|"+vo.getPrimaryAilment()+"|"+userSecurityProfile.getUSER_SEQ_ID()+",");
				}
			}
			if(sb.length()>0)
				seqIds		=	sb.substring(0, sb.length()-1);
			
			//if(seqIds!=null && !"".equals(seqIds))
		/*m1*/		lSuccess	=	onlinePreAuthManager.deleteExistngDataForPreAuth(seqIds,(Long)session.getAttribute("lpreAuthIntSeqId"),"ICD",seqQty,userSecurityProfile.getHospSeqId(),userSecurityProfile.getUSER_SEQ_ID(),"providerLogin",request);
			
			
			//TO DELETE THE CHILD TABLE DATA WHICH ARE NOT IN FRONT END - ACTIVITY CODES - ACTIVITIES AND DRUGS AT SAME TIME AS DATA IS IN SAME TABLE
			sb	=	new StringBuffer();
			seqIds	=	"";
			if(alActivityDetails.size()>0){
				for(ActivityDetailsVO actVo :alActivityDetails)
				{  
					sb.append(actVo.getActivityCode()+"|");
					sb.append(actVo.getQuantity()+",");
				}
			}if(alDrugDetails.size()>0){
				for(DrugDetailsVO drugVo :alDrugDetails)
				{
					sb.append(drugVo.getDrugCode()+"|");
					sb.append(drugVo.getDrugqty()+",");
				}
			}
			if(sb.length()>0){
				seqIds		=	sb.substring(0, sb.length()-1);
				//seqQty		=	sbQty.substring(0, sbQty.length()-1);
			}

		/*m2*/	lSuccess	=	onlinePreAuthManager.deleteExistngDataForPreAuth(seqIds,(Long)session.getAttribute("lpreAuthIntSeqId"),"ACT",seqQty,userSecurityProfile.getHospSeqId(),userSecurityProfile.getUSER_SEQ_ID(),"providerLogin",request);
			
			if(lSuccess>0){
		/*m3*/		onlinePreAuthManager.saveActivitiesDetails(alActivityDetails,(Long)session.getAttribute("lpreAuthIntSeqId"),userSecurityProfile.getUSER_SEQ_ID(),treatmentDate,clinicianId,"providerLogin",request);
		/*m4*/		onlinePreAuthManager.saveDrugDetails(alDrugDetails,(Long)session.getAttribute("lpreAuthIntSeqId"),userSecurityProfile.getUSER_SEQ_ID(),treatmentDate,clinicianId,"providerLogin",request);
			
			}
			//-------------------Save the Dental related data--------------------
			ActionForm dentalForm=(ActionForm)frmOnlinePreAuth.get("dentalOrthoVO");
			DentalOrthoVO dentalOrthoVO=(DentalOrthoVO)FormUtils.getFormValues(dentalForm,"frmPreAuthDental",this,mapping,request);
			dentalOrthoVO.setPreAuthSeqid((Long)session.getAttribute("lpreAuthIntSeqId"));
		/*m5*/	String lVal	=	onlinePreAuthManager.saveDentalDetails(dentalOrthoVO,"PAT",(Long)session.getAttribute("lpreAuthIntSeqId"),userSecurityProfile.getUSER_SEQ_ID(),"providerLogin",request);
            //---------------------------------------------------------
			cashlessDetailVO.setPreAuthSeqID((Long)session.getAttribute("lpreAuthIntSeqId"));
			
					if("preAuthEnhance".equals(fromFlag)){
		/*m6*/				arrResult	= 	onlinePreAuthManager.savePreAuthDetails(cashlessDetailVO,null,userSecurityProfile,cashlessDetailVO.getBenifitType(),"Y","providerLogin",request);
						request.getSession().setAttribute("enhanceYN", "Y");
						}
						else{
			/*m7*/					arrResult	= 	onlinePreAuthManager.savePreAuthDetails(cashlessDetailVO,null,userSecurityProfile,cashlessDetailVO.getBenifitTypeCode(),"N","providerLogin",request);
							request.getSession().setAttribute("enhanceYN", "N");
						}
				
			lPreAuthIntSeqId	=	new Long(arrResult[0]);
			
			cashlessDetailVO.setPreAuthSeqID(lPreAuthIntSeqId);
			
			if(listOfFiles.size()>0){
				ArrayList fileToUpload = new ArrayList<>();
				Long	referenceSeqId = userSecurityProfile.getHospSeqId();
				
				for(int i=0; i< listOfFiles.size(); i++){
					Long userSeqId	=	(Long) TTKCommon.getUserSeqId(request);
					String preauthseqid = String.valueOf(cashlessDetailVO.getPreAuthSeqID());
					FormFile fileInfo = 	listOfFiles.get(i);
					String 	strTimeStamp=((df.format(Calendar.getInstance(tz).getTime())).replaceAll(" ", "_")).replaceAll(":", ""); 
					String fileName=strTimeStamp+"_"+fileInfo.getFileName();
					String origFileName	=	fileInfo.getFileName();
					InputStream inputStream = fileInfo.getInputStream();
					int fileSize = fileInfo.getFileSize();
					String fileDesc = fileDesclist.get(i);
					fileToUpload.add(TTKCommon.getUserSeqId(request)); //0
					fileToUpload.add(fileName);//1
					fileToUpload.add(fileDesc);//2
					fileToUpload.add("Y");//3    Flag required to upload document in all case.
					fileToUpload.add("HOS|PAT");//4
				int	iUpdate=managerObject.saveDocUploads(fileToUpload,userSeqId,preauthseqid,origFileName,inputStream,fileSize,referenceSeqId);
				   fileToUpload.clear();  // clear list after save to save 2nd description at same index.
				System.out.println("File uploaded :"+iUpdate);
				}
			}
			
			Object[] preauthAllresult	=	onlinePreAuthManager.getOnlinePartialPreAuthDetails(cashlessDetailVO.getPreAuthSeqID());
			
			cashlessDetailVO = (CashlessDetailVO) preauthAllresult[0];
			diagnosis = (ArrayList<DiagnosisDetailsVO>) preauthAllresult[1];
			alActivityDetails = (ArrayList<ActivityDetailsVO>) preauthAllresult[2];
			alDrugDetails = (ArrayList<DrugDetailsVO>) preauthAllresult[3];
		
			request.getSession().setAttribute("preauthDiagnosis", diagnosis);
			request.getSession().setAttribute("preauthActivities", alActivityDetails);
			request.getSession().setAttribute("preauthDrugs", alDrugDetails);
			frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
		
			request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
			request.getSession().setAttribute("lPreAuthIntSeqId", cashlessDetailVO.getPreAuthSeqID());
			request.getSession().setAttribute("preAuthStatus", "In-Progress");
			request.getSession().setAttribute("claimStatus", null);
			request.getSession().setAttribute("onlinePreAuthNO", arrResult[1]);
			request.setAttribute("isSubmitted", true);
			request.setAttribute("updated", "message.savedSuccessfully");
			
			return this.getForward(strONLINEPREAUTHSUCCESS, mapping, request);
		}//end of try
	catch(TTKException expTTK)
		{
			System.out.println("**************  Outer catch(TTKException expTTK) **************** ");
			return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
		{
			System.out.println("**************  Outer catch(Exception exp) **************** ");
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of doSubmitOnlinePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)




public ActionForward doDefaultClinician(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
		log.debug("Inside the doDefaultClinician method of OnlinePreAuthAction");
		setOnlineLinks(request);
		HttpSession session=request.getSession();
		DynaActionForm frmOnlinePreAuth=(DynaActionForm) (session.getAttribute("frmOnlinePreAuth")==null?form:session.getAttribute("frmOnlinePreAuth"));
		
		TableData clinicianListData = new TableData();  //create new table data object
		clinicianListData.createTableInfo("ClinicianListTable",new ArrayList()); 
		session.setAttribute("clinicianListData",clinicianListData);//create the required grid table
		
		return this.getForward(strCliniciansList, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
		return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
		return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineEnrollment"));
	}//end of catch(Exception exp)
}//end of doDefaultClinician(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


public ActionForward clinicianSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside OnlinePreAuthAction clinicianSearch");
			setOnlineLinks(request);
			HttpSession session=request.getSession();
			PreAuthManager preAuthObject=this.getPreAuthManagerObject();
			TableData clinicianListData = null;
			DynaActionForm frmPreAuthList=(DynaActionForm)form;
		if((request.getSession()).getAttribute("clinicianListData") != null)
		{
			clinicianListData = (TableData)session.getAttribute("clinicianListData");
		}//end of if((request.getSession()).getAttribute("icdListData") != null)
		else
		{
			clinicianListData = new TableData();
		}//end of else
		
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));		
		//if the page number or sort id is clicked
		if(!strPageID.equals("") || !strSortID.equals(""))
		{
			if(strPageID.equals(""))
		{
			clinicianListData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
			clinicianListData.modifySearchData("sort");//modify the search data                    
		}///end of if(!strPageID.equals(""))
		else
		{
			log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
			clinicianListData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			return this.getForward(strCliniciansList, mapping, request);
		}//end of else
		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		else{
			clinicianListData.createTableInfo("ClinicianListTable",null);
			clinicianListData.setSearchData(this.populateClinicianSearchCriteria(frmPreAuthList,request));
			clinicianListData.modifySearchData("search");				
		}//end of else
		
			ArrayList alClinicianList=null;
			alClinicianList= preAuthObject.getProviderClinicianList(clinicianListData.getSearchData());
			clinicianListData.setData(alClinicianList, "search");
			//set the table data object to session
			session.setAttribute("clinicianListData",clinicianListData);
			return this.getForward(strCliniciansList, mapping, request);
}//end of try
catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strCliniciansList));
}//end of catch(Exception exp)
}//end of clinicianSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


/*
 * Clinician Forward
 */

public ActionForward doClinicianForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
	log.debug("Inside OnlinePreAuthAction doClinicianForward");
	setOnlineLinks(request);
	TableData clinicianListData = (TableData)request.getSession().getAttribute("clinicianListData");
	PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	clinicianListData.modifySearchData(strForward);//modify the search data
	ArrayList alClinicianList = preAuthObject.getProviderClinicianList(clinicianListData.getSearchData());
	clinicianListData.setData(alClinicianList, strForward);//set the table data
	request.getSession().setAttribute("clinicianListData",clinicianListData);   //set the table data object to session
	return this.getForward(strCliniciansList, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strCliniciansList));
}//end of catch(Exception exp)
}//end of doClinicianForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


/**
Clinician Backward
*/
public ActionForward doClinicianBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
	try{
	log.debug("Inside OnlinePreAuthAction doClinicianBackward");
	setOnlineLinks(request);
	TableData clinicianListData = (TableData)request.getSession().getAttribute("clinicianListData");
	PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	clinicianListData.modifySearchData(strBackward);//modify the search data
	ArrayList alClinicianList = preAuthObject.getProviderClinicianList(clinicianListData.getSearchData());
	clinicianListData.setData(alClinicianList, strBackward);//set the table data
	request.getSession().setAttribute("clinicianListData",clinicianListData);   //set the table data object to session
	return this.getForward(strCliniciansList, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strCliniciansList));
}//end of catch(Exception exp)
}//end of doClinicianBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


public ActionForward doSelectClinicianId(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
setOnlineLinks(request);
log.debug("Inside PreAuthGenealAction doSelectclinicianId ");
HttpSession session=request.getSession();
TableData clinicianListData = (TableData)session.getAttribute("clinicianListData");
if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
{
	ClinicianDetailsVO clinicianDetailsVO=(ClinicianDetailsVO)clinicianListData.getRowInfo(Integer.parseInt((String)request.getParameter("rownum")));
	
	DynaActionForm frmOnlinePreAuth=(DynaActionForm)session.getAttribute("frmOnlinePreAuth");
	
	CashlessDetailVO cashlessDetailVO	=	getFormValues(frmOnlinePreAuth, mapping, request);
	
	/*frmOnlinePreAuth.set("clinicianId", clinicianDetailsVO.getClinicianId());
	frmOnlinePreAuth.set("clinicianName", clinicianDetailsVO.getClinicianName());*/

	cashlessDetailVO.setClinicianId(clinicianDetailsVO.getClinicianId());
	cashlessDetailVO.setClinicianName(clinicianDetailsVO.getClinicianName());
	cashlessDetailVO.setSpeciality(clinicianDetailsVO.getClinicianSpeciality());
	cashlessDetailVO.setConsultation(clinicianDetailsVO.getClinicianConsultation());
	frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
	session.setAttribute("frmOnlinePreAuth",frmOnlinePreAuth);
		//forward=strPreAuthDetail;
}
return this.getForward(strONLINEPREAUTH, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strCliniciansList));
}//end of catch(Exception exp)
}//end of doSelectclinicianId(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


/*
 * Validate Clinician ID
 */

public ActionForward doValidateClinician(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
try{
setOnlineLinks(request);
log.debug("Inside PreAuthGenealAction doValidateClinician ");
	HttpSession session=request.getSession();
	UserSecurityProfile userSecurityProfile	=	(UserSecurityProfile) session.getAttribute("UserSecurityProfile");
	String clinicianId	=	request.getParameter("clinicianId");
	PreAuthManager preAuthObject=this.getPreAuthManagerObject();
	ClinicianDetailsVO clinicianDetailsVO	=	preAuthObject.getProviderClinicianDetails(clinicianId,userSecurityProfile.getHospSeqId());
	
	PrintWriter out = response.getWriter();  
    response.setContentType("text/xml");  
    response.setHeader("Cache-Control", "no-cache");  
    response.setStatus(HttpServletResponse.SC_OK);  
    if(clinicianDetailsVO!=null)
    	out.write(clinicianDetailsVO.getClinicianId()+","+clinicianDetailsVO.getClinicianName()+","+clinicianDetailsVO.getClinicianSpeciality()+","+clinicianDetailsVO.getClinicianConsultation()+",");
    out.flush();  
    
    
	/*cashlessDetailVO.setClinicianId(clinicianDetailsVO.getClinicianId());
	cashlessDetailVO.setClinicianName(clinicianDetailsVO.getClinicianName());
	cashlessDetailVO.setSpeciality(clinicianDetailsVO.getClinicianSpeciality());
	cashlessDetailVO.setConsultation(clinicianDetailsVO.getClinicianConsultation());
	frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
			
	session.setAttribute("frmOnlinePreAuth",frmOnlinePreAuth);
		//forward=strPreAuthDetail;
return this.getForward(strONLINEPREAUTH, mapping, request);*/
    return null;
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strCliniciansList));
}//end of catch(Exception exp)
}//end of doValidateClinician(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)


/*
 * EditOnlinePreAuth
 */
public ActionForward doEditOnlinePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
			setOnlineLinks(request);
			log.debug("Inside the doEditOnlinePreAuth method of OnlinePreAuthAction");
			DynaActionForm frmOnlinePreAuth =(DynaActionForm)request.getSession().getAttribute("frmOnlinePreAuth");
			CashlessDetailVO cashlessDetailVO = null;
			cashlessDetailVO	=	(CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmOnlinePreAuth",
     				this,mapping,request);

			request.setAttribute("readonly", "false");
			request.getSession().setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
			return this.getForward(strONLINEPREAUTH, mapping, request);
		}//end of try
	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
}//end of doEditOnlinePreAuth(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)





/*
 * EnhanceMEnt of Pr3Auth			
 */

public ActionForward doViewEnhance(ActionMapping mapping,ActionForm form,HttpServletRequest request,
        HttpServletResponse response) throws Exception{
	try{
	setOnlineLinks(request);
	request.getSession().removeAttribute("lpreAuthIntSeqId");
	log.debug("Inside the doViewEnhance method of OnlineCashlessHospAction");
	DynaActionForm frmOnlinePreAuth =(DynaActionForm)form;
	//frmCashlessAdd.initialize(mapping);     //reset the form data
	HttpSession session	=	request.getSession();
	CashlessDetailVO cashlessDetailVO	=	new CashlessDetailVO();
	
	OnlinePreAuthManager onlinePreAuthManager = this.getOnlineAccessManagerObject();
	String rownum	=	(String)request.getParameter("rownum");
	TableData tableData = (TableData) request.getSession().getAttribute("tableData");
	PreAuthSearchVO preAuthSearchVO	=	null;
	preAuthSearchVO = (PreAuthSearchVO)tableData.getRowInfo(Integer.parseInt(rownum));
	request.getSession().setAttribute("lPreAuthIntSeqId", preAuthSearchVO.getPatAuthSeqId());
	Object[] preauthAllresult	=	onlinePreAuthManager.getOnlinePartialPreAuthDetails(preAuthSearchVO.getPatAuthSeqId());
	
	cashlessDetailVO = (CashlessDetailVO) preauthAllresult[0];
	ArrayList<DiagnosisDetailsVO> diagnosis = (ArrayList<DiagnosisDetailsVO>) preauthAllresult[1];
	ArrayList<ActivityDetailsVO> activities = (ArrayList<ActivityDetailsVO>) preauthAllresult[2];
	ArrayList<DrugDetailsVO> aldrugs = (ArrayList<DrugDetailsVO>) preauthAllresult[3];
	session.setAttribute("preauthDiagnosis", diagnosis);
	session.setAttribute("preauthActivities", activities);
	session.setAttribute("preauthDrugs", aldrugs);
	
	DiagnosisDetailsVO diagnosisDetailsVO	=	new DiagnosisDetailsVO();//diagnosis details
	ActivityDetailsVO activityDetailsVO		=	new ActivityDetailsVO();//activity Details
	
	cashlessDetailVO.setDiagnosisDetailsVO(diagnosisDetailsVO);
	cashlessDetailVO.setActivityDetailsVO(activityDetailsVO);
	cashlessDetailVO.setDrugDetailsVO(new DrugDetailsVO());
	ArrayList<CacheObject> alEncountertypes	=	new ArrayList<CacheObject>();
	alEncountertypes			=	onlinePreAuthManager.getEnounterTypes(cashlessDetailVO.getBenifitType());
	
	OnlineProviderManager onlineProviderManager	=	this.getOnlineAccessManagerObject1();
	ArrayList<String[]> alShortFallList	=	onlineProviderManager.getShortfallList(preAuthSearchVO.getPatAuthSeqId());
	request.getSession().setAttribute("alShortFallList",alShortFallList);
	
	if("EnhancementNew".equals(preAuthSearchVO.getEnhanceImageName()) || ("AppealButton".equals(preAuthSearchVO.getAppealImageName())))
		cashlessDetailVO.setValidateDocNameYN("Y");
	else
		cashlessDetailVO.setValidateDocNameYN("N");
	
	DynaActionForm onlinePreAuthForm = setFormValues(cashlessDetailVO,mapping,request);
	onlinePreAuthForm.set("encounterTypes", alEncountertypes);
	onlinePreAuthForm.set("enhanceYN",preAuthSearchVO.getEnhanceYN());
	request.getSession().setAttribute("encounterTypes", alEncountertypes);
	request.getSession().setAttribute("frmOnlinePreAuth", onlinePreAuthForm);
	request.getSession().setAttribute("preauthDiagnosis", diagnosis);
	request.getSession().setAttribute("preauthActivities", activities);
	request.getSession().setAttribute("preauthDrugs", aldrugs);
	request.getSession().setAttribute("enhanceYN", preAuthSearchVO.getEnhanceYN());
	request.getSession().setAttribute("preAuthStatus", cashlessDetailVO.getStatus());
	request.getSession().setAttribute("isSubmittedforEnhance", false);
	request.getSession().setAttribute("fromFlag", request.getParameter("fromFlag"));
	String  submitEN = cashlessDetailVO.getSubmitEN();
	request.getSession().setAttribute("submitEN", submitEN);
	frmOnlinePreAuth.set("submitEN", submitEN);
	request.getSession().setAttribute("viewDocFlag", "upload");
	request.getSession().removeAttribute("benifitTypeVal");
	request.getSession().setAttribute("loginTypeProvider", "providerLogin"); 
	return this.getForward(strONLINEPREAUTHENHANCE, mapping, request);
}//end of try
catch(TTKException expTTK)
{
return this.processOnlineExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processOnlineExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
}//end of catch(Exception exp)
}//end of doDefaultRecentReports(ActionMapping mapping,ActionForm form,HttpServletRequest request,
//HttpServletResponse response)



/*
 * Generate PReauth Letter from provider Login - On Submit of Online preauth
 */

public ActionForward generatePreauthLetter(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
	try {
		setLinks(request);
		DynaActionForm frmPreAuthGeneral = (DynaActionForm) form;
		/*String preAuthSeqID = frmPreAuthGeneral.getString("preAuthSeqID");
		String authNum = frmPreAuthGeneral.getString("authNum");
		String preauthStatus = frmPreAuthGeneral.getString("preauthStatus");*/

		JasperReport mainJasperReport = null;
		JasperReport diagnosisJasperReport = null;
		JasperReport activityJasperReport = null;
		JasperPrint mainJasperPrint = null;
		String parameter = request.getParameter("parameter");
		String mainJrxmlfile = "generalreports/PreAuthApprovalOrDenialLetter.jrxml";
		String diagnosisJrxmlfile = "generalreports/DiagnosisDoc.jrxml";
		String activityJrxmlfile = "generalreports/ActivitiesDoc.jrxml";
		TTKReportDataSource mainTtkReportDataSource = null;
		TTKReportDataSource diagnosisTtkReportDataSource = null;
		TTKReportDataSource activityTtkReportDataSource = null;

		ByteArrayOutputStream boas = new ByteArrayOutputStream();

		String authNum	=	request.getParameter("authorizationNo");
		String strPdfFile = TTKPropertiesReader
				.getPropertyValue("authorizationrptdir") + authNum + ".pdf";
		JasperReport emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		  // mainJasperReport = JasperCompileManager.compileReport(mainJrxmlfile);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		//parameter = "|" + preAuthSeqID + "|" + preauthStatus + "|PAT|";
		mainTtkReportDataSource = new TTKReportDataSource("PreauthLetterFormat", parameter);
		diagnosisTtkReportDataSource = new TTKReportDataSource("DiagnosisDetails", parameter);
		activityTtkReportDataSource = new TTKReportDataSource("ActivityDetails", parameter);


		ResultSet main_report_RS = mainTtkReportDataSource.getResultData();

		mainJasperReport = JasperCompileManager	.compileReport(mainJrxmlfile);
		diagnosisJasperReport = JasperCompileManager.compileReport(diagnosisJrxmlfile);
		activityJasperReport = JasperCompileManager.compileReport(activityJrxmlfile);

		hashMap.put("diagnosisDataSource", diagnosisTtkReportDataSource);
		hashMap.put("diagnosis", diagnosisJasperReport);
		hashMap.put("activityDataSource", activityTtkReportDataSource);
		hashMap.put("activity", activityJasperReport);
		// JasperFillManager.fillReport(activityJasperReport, hashMap,
		// activityTtkReportDataSource);

		if (main_report_RS == null & !main_report_RS.next()) {
			mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		} else {
			main_report_RS.beforeFirst();
			mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);
		}// end of else
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
				exp, strCliniciansList));
	}// end of catch(Exception exp)
}// end of doSaveShortFallDetails(ActionMapping mapping,ActionForm
	// form,HttpServletRequest request,
	// HttpServletResponse response)




/*
 * Reading and Displaying File Stored in DB
 */

 public ActionForward viewUploadDocumentsProviders(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
	    
		 ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
	  try{
			
		  
		  OnlinePreAuthManager onlinePreAuthManager = this.getOnlineAccessManagerObject();
		  	TableData tableData = (TableData) request.getSession().getAttribute("tableDataMouCertificates");
		  	String rownum	=	(String)request.getParameter("rownum");
		  	MOUDocumentVO mouDocumentVO = (MOUDocumentVO)tableData.getRowInfo(Integer.parseInt(rownum));
			String fileSeqId = mouDocumentVO.getMouDocSeqID()+"";
			
			InputStream iStream	=	onlinePreAuthManager.getProviderUploadedDocsDBFile(fileSeqId);
			
           bis = new BufferedInputStream(iStream);
           baos=new ByteArrayOutputStream();
           response.setContentType("application/pdf");
           int ch;
                 while ((ch = bis.read()) != -1) baos.write(ch);
                 sos = response.getOutputStream();
                 baos.writeTo(sos);  
                 baos.flush();      
                 sos.flush(); 
			      
	            }catch(Exception exp)
	            	{
	            		return this.processExceptions(request, mapping, new TTKException(exp,"onlineaccountinfo"));
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
 
 
 

 private DynaActionForm setFormValues(CashlessDetailVO cashlessDetailVO,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
        try
        {
            DynaActionForm hospitalForm = (DynaActionForm)FormUtils.setFormValues("frmOnlinePreAuth",
        							   cashlessDetailVO,this,mapping,request);
        if(cashlessDetailVO.getDiagnosisDetailsVO()!=null)
        {
            hospitalForm.set("diagnosisDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmDiagnosisDetails",
            		cashlessDetailVO.getDiagnosisDetailsVO(),this,mapping,request));
        }//end of if(cashlessDetailVO.getAddressVO()!=null)
        else
        {
            hospitalForm.set("diagnosisDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmDiagnosisDetails",
            		new DiagnosisDetailsVO(),this,mapping,request));
        }//end of else
        
        if(cashlessDetailVO.getActivityDetailsVO()!=null)
        {
            hospitalForm.set("activityDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmActivityDetails",
            		cashlessDetailVO.getActivityDetailsVO(),this,mapping,request));
        }//end of if(cashlessDetailVO.getAddressVO()!=null)
        else
        {
            hospitalForm.set("activityDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmActivityDetails",
            		new ActivityDetailsVO(),this,mapping,request));
        }//end of else
        
        
        if(cashlessDetailVO.getDrugDetailsVO()!=null)
        {
            hospitalForm.set("drugDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmDrugsDetails",
            		cashlessDetailVO.getDrugDetailsVO(),this,mapping,request));
        }//end of if(cashlessDetailVO.getAddressVO()!=null)
        else
        {
            hospitalForm.set("drugDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmDrugsDetails",
            		new DrugDetailsVO(),this,mapping,request));
        }//end of else
        
        if(cashlessDetailVO.getDentalOrthoVO()!=null)
        {
        	hospitalForm.set("dentalOrthoVO", (DynaActionForm)FormUtils.setFormValues("frmPreAuthDental",
        			cashlessDetailVO.getDentalOrthoVO(),this,mapping,request));
        }//end of if(preAuthDetailVO.getDentalOrthoVO()!=null)
        else
        {
        	hospitalForm.set("dentalOrthoVO", (DynaActionForm)FormUtils.setFormValues("frmPreAuthDental",
            		new DentalOrthoVO(),this,mapping,request));
        }//end of else
        
        if(cashlessDetailVO.getServiceDetailsVO() !=null)
        {
        	hospitalForm.set("serviceDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmServiceDetails",
        			cashlessDetailVO.getServiceDetailsVO(),this,mapping,request));
        }//end of if(preAuthDetailVO.getDentalOrthoVO()!=null)
        else
        {
        	hospitalForm.set("serviceDetailsVO", (DynaActionForm)FormUtils.setFormValues("frmServiceDetails",
            		new ServiceDetailsVO(),this,mapping,request));
        }//end of else
        
        
        return hospitalForm;
    }
    catch(Exception exp)
    {
        throw new TTKException(exp,strOnlinePreAuth);
    }//end of catch
}//end of setFormValues(cashlessDetailVO cashlessDetailVO,ActionMapping mapping,HttpServletRequest request)

		 
		 
		 
 private CashlessDetailVO getFormValues(DynaActionForm frmOnlinePreAuth,ActionMapping mapping,
    		HttpServletRequest request) throws TTKException
    {
     try
     {
     	CashlessDetailVO cashlessDetailVO 		=	null;
         DiagnosisDetailsVO diagnosisDetailsVO	=	null;
         ActivityDetailsVO activityDetailsVO		=	null;
         DrugDetailsVO drugDetailsVO				=	null;
         DentalOrthoVO dentalOrthoVO				=	null;
		ServiceDetailsVO serviceDetailsVO				=	null;
         cashlessDetailVO = (CashlessDetailVO)FormUtils.getFormValues(frmOnlinePreAuth,"frmOnlinePreAuth",
     				this,mapping,request);
            

     ActionForm DiagForm=(ActionForm)frmOnlinePreAuth.get("diagnosisDetailsVO");
     diagnosisDetailsVO=(DiagnosisDetailsVO)FormUtils.getFormValues(DiagForm,"frmDiagnosisDetails",this,mapping,request);
     //To bring country based on state selection along with cities
     
     ActionForm activityForm=(ActionForm)frmOnlinePreAuth.get("activityDetailsVO");
     activityDetailsVO=(ActivityDetailsVO)FormUtils.getFormValues(activityForm,"frmActivityDetails",this,mapping,request);
     
     ActionForm drugForm=(ActionForm)frmOnlinePreAuth.get("drugDetailsVO");
     drugDetailsVO=(DrugDetailsVO)FormUtils.getFormValues(drugForm,"frmDrugsDetails",this,mapping,request);
     
     ActionForm dentalForm=(ActionForm)frmOnlinePreAuth.get("dentalOrthoVO");
     dentalOrthoVO=(DentalOrthoVO)FormUtils.getFormValues(dentalForm,"frmPreAuthDental",this,mapping,request);

		ActionForm serviceFrm=(ActionForm)frmOnlinePreAuth.get("serviceDetailsVO");
        serviceDetailsVO=(ServiceDetailsVO)FormUtils.getFormValues(serviceFrm,"frmServiceDetails",this,mapping,request);
     
     cashlessDetailVO.setDiagnosisDetailsVO(diagnosisDetailsVO);
     cashlessDetailVO.setActivityDetailsVO(activityDetailsVO);
     cashlessDetailVO.setDrugDetailsVO(drugDetailsVO);
     cashlessDetailVO.setDentalOrthoVO(dentalOrthoVO);
     cashlessDetailVO.setServiceDetailsVO(serviceDetailsVO);
     return cashlessDetailVO;
 }//end of try
 catch(Exception exp)
 {
     throw new TTKException(exp,strOnlinePreAuth);
 }//end of catch
}//end of getFormValues(DynaActionForm frmOnlinePreAuth,ActionMapping mapping,HttpServletRequest request)
 
		 
		 
		 
		 
		 
/**
 * this method will add search criteria fields and values to the arraylist and will return it
 * @param frmClmHospSearch formbean which contains the search fields
 * @return ArrayList contains search parameters
 */
private ArrayList<Object> populateSearchCriteria(DynaActionForm frmClmHospSearch,HttpServletRequest request) throws TTKException
{
//      build the column names along with their values to be searched
    ArrayList<Object> alSearchBoxParams=new ArrayList<Object>();
    UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
    //prepare the search BOX parameters
  //  Long lngHospSeqId =userSecurityProfile.getHospSeqId();
 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sClaimNumber")));
 	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sAuthNumber")));
    alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sEnrollmentNumber")));
	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sStatus")));
    alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sPolicyNumber")));
    alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sDOA")));
    alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sDateOfClm")));
    alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sClmStartDate")));
	alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sClmEndDate")));   //sPatStartDate,sPatEndDate,sClmStartDate,sClmEndDate
    alSearchBoxParams.add(userSecurityProfile.getHospSeqId());
    //new Req
    alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sPatientName")));
    alSearchBoxParams.add(TTKCommon.replaceSingleQots((String)frmClmHospSearch.getString("sDischargeDate")));
	return alSearchBoxParams;
}//end of populateSearchCriteria(DynaActionForm frmClmHospSearch,HttpServletRequest request) 

private ArrayList<Object> populateActivityCodeSearchCriteria(DynaActionForm frmActivityList,HttpServletRequest request,String type,String enrollId)
{
    UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");

	//build the column names along with their values to be searched
	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sActivityCode")));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sActivityCodeDesc")));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sSearchType")));
	alSearchParams.add((Long)userSecurityProfile.getHospSeqId());
	alSearchParams.add(enrollId);
	if("activity".equals(type))
		alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sInternalCode")));
	else
		alSearchParams.add("");
	//alSearchParams.add(TTKCommon.getUserSeqId(request));
	return alSearchParams;
}//end of populateActivityCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

		
/**
 * Returns the PreAuthManager session object for invoking methods on it.
 * @return PreAuthManager session object which can be used for method invokation
 * @exception throws TTKException
 */
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
		throw new TTKException(exp, strOnlinePreAuth);
	}//end of catch
	return preAuthManager;
}//end getPreAuthManagerObject()

private OnlineProviderManager getOnlineAccessManagerObject1() throws TTKException
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
/**
 * this method will add search criteria fields and values to the arraylist and will return it
 * @param frmPreAuthList formbean which contains the search fields
 * @param request HttpServletRequest
 * @return ArrayList contains search parameters
 */
private ArrayList<Object> populateClinicianSearchCriteria(DynaActionForm frmPreAuthList,HttpServletRequest request)
{
	//build the column names along with their values to be searched
    UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");

	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sClinicianId")));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sClinicianName")));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProviderId")));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmPreAuthList.getString("sProviderName")));
	alSearchParams.add(TTKCommon.getUserSeqId(request));
	alSearchParams.add(userSecurityProfile.getHospSeqId());
	return alSearchParams;
}//end of populateClinicianSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)


private ArrayList<Object> populateDiagCodeSearchCriteria(DynaActionForm frmActivityList,HttpServletRequest request)
{
    UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");

	//build the column names along with their values to be searched
	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sActivityCode")));
	alSearchParams.add(TTKCommon.replaceSingleQots((String)frmActivityList.getString("sActivityCodeDesc")));
	alSearchParams.add(TTKCommon.getUserSeqId(request));
	return alSearchParams;
}//end of populateDiagCodeSearchCriteria(DynaActionForm frmProductList,HttpServletRequest request)

public ActionForward doSaveAppealComment(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		try{
		setOnlineLinks(request);
		log.debug("Inside OnlinePreAuthAction doSaveAppealComments");
		HttpSession session	=	request.getSession();
		DynaActionForm frmOnlinePreAuth =(DynaActionForm)form;
		
		String appealComments = frmOnlinePreAuth.getString("appealComments");
		appealComments = (appealComments==null) || (appealComments.length()<1)? "" :appealComments;
		
		String lPreAuthIntSeqId = frmOnlinePreAuth.getString("lPreAuthIntSeqId");
		lPreAuthIntSeqId = (lPreAuthIntSeqId==null) || (lPreAuthIntSeqId.length()<1)? "0" :lPreAuthIntSeqId;
		Long preAuthSeqId = Long.parseLong(lPreAuthIntSeqId);
		
		String preAuthNo = frmOnlinePreAuth.getString("preAuthNo");
		OnlinePreAuthManager onlinePreAuthManager	=	this.getOnlineAccessManagerObject();
		String alAppealComment="";
		alAppealComment = onlinePreAuthManager.saveAppealComments(preAuthSeqId,appealComments);
		
		CashlessDetailVO cashlessDetailVO = null;
		cashlessDetailVO	=	getFormValues(frmOnlinePreAuth,mapping,request);
		
		Object[] preauthAllresult	=	onlinePreAuthManager.getOnlinePartialPreAuthDetails(preAuthSeqId);
		cashlessDetailVO = (CashlessDetailVO) preauthAllresult[0];

		frmOnlinePreAuth = setFormValues(cashlessDetailVO,mapping,request);
		
		request.getSession().setAttribute("onlinePreAuthNO", preAuthNo);
		frmOnlinePreAuth.set("appealFlag", "Y");
		
		session.setAttribute("frmOnlinePreAuth", frmOnlinePreAuth);
		return this.getForward(strONLINEPREAUTHSUCCESS, mapping, request);	
		}//end of try
	catch(TTKException expTTK)
		{
			return this.processOnlineExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
		{
			return this.processOnlineExceptions(request, mapping, new TTKException(exp,strOnlinePreAuth));
	}//end of catch(Exception exp)
} // end of doSaveAppealComment()

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
		throw new TTKException(exp, strOnlinePreAuth);
	}//end of catch
	return maintenanceManager;
 }//end getMaintenanceManagerObject()

}//end of OnlineClmSearchHospAction
