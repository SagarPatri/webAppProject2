

package com.ttk.action.insurancepricing;



import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

import com.ttk.action.TTKAction;
import com.ttk.action.finance.Batch;
import com.ttk.action.finance.MemberData;
import com.ttk.action.finance.MemberDetails;
import com.ttk.action.table.TableData;
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.business.empanelment.InsuranceManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.dto.insurancepricing.InsPricingVO;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.CacheObject;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.Date;

import formdef.plugin.util.FormUtils;
/**
* This class is used to search the InsPricing company,add
* that to webboard. and also has the mode add/edit and delete the
* insurance companies at various level.
*/

public class SwInsPricingAction extends TTKAction {

	private static Logger log = Logger.getLogger( InsPricingAction.class );
  
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	private static final String strPricingHome="pricinghome";
    private static final String strGroupprofile="groupprofile";
    private static final String strSaveproceed="saveproceed";
    private static final String strIncomeprofile="incomeprofile";
    private static final String strInputscreen3="inputscreen3";
    
    
    private static final String strInsError="insurance";
	private static final String strChngCorporate="changecorporate";
	private static final String strRownum="rownum";
    
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
    	//	log.info("Inside the doDefault method of InsPricingAction");
    		setLinks(request);
    		DynaActionForm frmSwPricingHome = (DynaActionForm)form;
            //((DynaActionForm)form).initialize(mapping);//reset the form data
    		TableData tableData = TTKCommon.getTableData(request);
    		//remove the selected office from the session
            request.getSession().removeAttribute("SelectedOffice");
            tableData = new TableData();//create new table data object
            //create the required grid table
            tableData.createTableInfo("SwInsurancePricingTable",new ArrayList());
            request.getSession().setAttribute("tableData",tableData);
            ((DynaActionForm)form).initialize(mapping);//reset the form data
    		if(TTKCommon.checkNull(request.getParameter("Message")).equals("Y"))
			{
    			frmSwPricingHome.set("Message",request.getParameter("Message")); 
            	TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.required");
				throw expTTK;
				
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
    		else
    		{
    			frmSwPricingHome.set("Message","N");  	
    		}
    		
    		 long seqid=0;
    		 request.getSession().setAttribute("GroupProfileSeqID",seqid);
    		
    		 request.getSession().setAttribute("frmSwPricingHome",frmSwPricingHome);
            return this.getForward(strPricingHome, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
         //  log.info("doSearch...........");
    		setLinks(request);
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
            String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
            TableData tableData = TTKCommon.getTableData(request);
            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
            //if the page number or sort id is clicked
            if(!strPageID.equals("") || !strSortID.equals(""))
            {
            	if(!strPageID.equals(""))
                {
            		tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
                    return mapping.findForward(strPricingHome);
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
                tableData.createTableInfo("SwInsurancePricingTable",null);
                tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
                tableData.modifySearchData("search");
            }//end of else
            ArrayList alInsuranceProfileList= insuranceObject.getSwInsuranceProfileList(tableData.getSearchData());
            tableData.setData(alInsuranceProfileList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableData",tableData);
			//finally return to the grid screen
			return this.getForward(strPricingHome, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmSwPricingHome,
			HttpServletRequest request)throws TTKException
	{
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		//alSearchParams.add(new SearchCriteria("EMPANELED_DATE", (String)frmSearchInsurance.get("sEmpanelDate")));
		alSearchParams.add(TTKCommon.checkNull(frmSwPricingHome.get("pricingRefno")));
		alSearchParams.add(TTKCommon.checkNull(frmSwPricingHome.get("previousPolicyNo")));
		alSearchParams.add(TTKCommon.checkNull(frmSwPricingHome.get("clientCode")));
		alSearchParams.add(TTKCommon.checkNull(frmSwPricingHome.get("groupName")));

    	return alSearchParams;
	}
    
    private String calculateValidation (InsPricingVO insPricingVO, String totalNumLivesclient, String totalMatNumLivesclient,String dentalLivesYN,String opticalLivesYN,HttpServletRequest request) throws TTKException{
    	
    	HttpSession session=request.getSession();

    	UserSecurityProfile userSecurityProfile = (UserSecurityProfile)session.getAttribute("UserSecurityProfile");

    	
    	BigDecimal totalinpatientLives = new BigDecimal(0.00); 
		  BigDecimal totalsumInpatient = new BigDecimal(0.00);
		  BigDecimal totalFemaleinpatientLives = new BigDecimal(0.00); 
		  BigDecimal totalsumFemaleInpatient = new BigDecimal(0.00);
		  
		  BigDecimal totalNumLivesclientBd = new BigDecimal(totalNumLivesclient);
		  BigDecimal totaldentalLives = new BigDecimal(0.00); 
		  BigDecimal totalsumdental = new BigDecimal(0.00); 
		  BigDecimal totalopticalLives = new BigDecimal(0.00); 
		  BigDecimal totalsumoptical = new BigDecimal(0.00);
		  BigDecimal totalMatNumLivesclientBd = new BigDecimal(totalMatNumLivesclient);
		  BigDecimal totalmaternityLives = new BigDecimal(0.00); 
		  BigDecimal totalsummaternity = new BigDecimal(0.00);
		  BigDecimal totalnationalityLives = new BigDecimal(0.00); 
		  BigDecimal totalsumnationality = new BigDecimal(0.00);

		  String benefitType = "";
		  Long benf_typeseqid= null;   
		  Long genderType=null;
		
    	 for(int i=0;i<insPricingVO.getGndrtypeseqid().length;i++){
    		 
             if(!insPricingVO.getBenf_typeseqid()[i].equals(""))//to
             {
             	benf_typeseqid =insPricingVO.getBenf_typeseqid()[i];
             	benefitType = insPricingVO.getBenfdesc()[i];
             	
             	if((benf_typeseqid == 6) || (benefitType.equalsIgnoreCase("In-Patient/Out-Patient")))
             	{ 	
             		
             	  if((insPricingVO.getTotalCoverdLives()[i].equals(null)) || (insPricingVO.getTotalCoverdLives()[i].equals("")))
             	  {
           			//System.out.println("getTotalCoverdLives if---"+insPricingVO.getTotalCoverdLives()[i]+"HIII");

	             		totalinpatientLives = new BigDecimal(0.00); 
	             		}else{
	             			//System.out.println("getTotalCoverdLives else---"+insPricingVO.getTotalCoverdLives()[i]+"hello");
	             		totalinpatientLives = new BigDecimal(insPricingVO.getTotalCoverdLives()[i]);
	             		}
	             	
	                totalsumInpatient=totalsumInpatient.add(totalinpatientLives);
	             //	  
	             }
             	
             	/*if((benf_typeseqid == 2) || (benefitType.equalsIgnoreCase("Dental")))
             	{ 	
             	  if(insPricingVO.getTotalCoverdLives()[i] == null || insPricingVO.getTotalCoverdLives()[i] == ""){
             		    totaldentalLives = new BigDecimal(0.00); 
	             		}else{
	             			totaldentalLives = new BigDecimal(insPricingVO.getTotalCoverdLives()[i]);
	             		}
	             	
             	     totalsumdental=totalsumdental.add(totaldentalLives);
	             //	  
	             }*/
             	
             /*	if((benf_typeseqid == 3) || (benefitType.equalsIgnoreCase("Optical")))
             	{ 	
             	  if(insPricingVO.getTotalCoverdLives()[i] == null || insPricingVO.getTotalCoverdLives()[i] == ""){
             		      totalopticalLives = new BigDecimal(0.00); 
	             		}else{
	             			totalopticalLives = new BigDecimal(insPricingVO.getTotalCoverdLives()[i]);
	             		}
	             	
             	     totalsumoptical=totalsumoptical.add(totalopticalLives);
	             	//  
	             }*/
             	
             	if((benf_typeseqid == 1) || (benefitType.equalsIgnoreCase("Maternity")))
             	{ 	
             	  if(insPricingVO.getTotalCoverdLives()[i].equals(null) || insPricingVO.getTotalCoverdLives()[i].equals("")){
             		        totalmaternityLives = new BigDecimal(0.00); 
	             		}else{
	             			totalmaternityLives = new BigDecimal(insPricingVO.getTotalCoverdLives()[i]);
	             		}
	             	
             	      totalsummaternity=totalsummaternity.add(totalmaternityLives);
	             	//  
	             }
             	
           //validation for "Maternity lives distn by age - should not be higher than the female lives in the respective age band for inpatient and outpatient lives"

             	if((benf_typeseqid == 6) || (benefitType.equalsIgnoreCase("In-Patient/Out-Patient")))
             	{ 	
             	  genderType=insPricingVO.getGndrtypeseqid()[i];
             	  if(genderType == 2){
             		  		if(insPricingVO.getTotalCoverdLives()[i].equals(null) || insPricingVO.getTotalCoverdLives()[i].equals("")){
		             		totalFemaleinpatientLives = new BigDecimal(0.00); 
		             		}else{
		             			totalFemaleinpatientLives = new BigDecimal(insPricingVO.getTotalCoverdLives()[i]);
		             		}
		             	
	                totalsumFemaleInpatient=totalsumFemaleInpatient.add(totalFemaleinpatientLives);
	             	//  
             	   }//end if gendertype 
             	}
             	
             	
             
             }//end if
            
    	 } //end for loop benefit
    	 
    	    
         for(int j=0;j<insPricingVO.getNatl_name().length;j++){
    		 
             if(!insPricingVO.getNatl_typeseqid()[j].equals(""))//to
             {
            	 
            	 if(insPricingVO.getNatCoverdLives()[j].equals(null) || insPricingVO.getNatCoverdLives()[j].equals("")){
            		 totalnationalityLives = new BigDecimal(0.00); 
             		}else{
             			totalnationalityLives = new BigDecimal(insPricingVO.getNatCoverdLives()[j]);
             		}
            	 totalsumnationality=totalsumnationality.add(totalnationalityLives);
	            // 	  
	            
            	 
             }
         }//end nationality for loop
    

       
         /*int retvalInp = Double.compare(totalsumInpatient.doubleValue(),totalNumLivesclientBd.doubleValue());
        
    	 if (retvalInp > 0 || retvalInp < 0){
    		
    		 userSecurityProfile.getSecurityProfile().setActiveTab("Income Profile");
      		TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.inpatientValidation");
				throw expTTK;
        	}*/
    	 
    	/* if(dentalLivesYN.equalsIgnoreCase("Y")){
    	 if (totalsumdental.doubleValue() < totalNumLivesclientBd.doubleValue()){
    		 userSecurityProfile.getSecurityProfile().setActiveTab("Income Profile");
      		TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.dentalValidation");
				throw expTTK;
        	}
    	 }
    	 
    	 if(opticalLivesYN.equalsIgnoreCase("Y")){
    	 if (totalsumoptical.doubleValue() < totalNumLivesclientBd.doubleValue()){
    		 userSecurityProfile.getSecurityProfile().setActiveTab("Income Profile");
      		TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.opticalValidation");
				throw expTTK;
        	}
    	 }*/
    	 
    	
    	 
    	 int retvalMaternity = Double.compare(totalsummaternity.doubleValue(),totalMatNumLivesclientBd.doubleValue());
    	 
    	 if (retvalMaternity > 0 || retvalMaternity < 0){
    		 userSecurityProfile.getSecurityProfile().setActiveTab("Income Profile");
      		TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.maternityValidation");
				throw expTTK;
        	}
    	 
    	

    	
//double.compare not works for totalsumFemaleInpatient and totalsummaternity condition
    	 /*if (totalsumFemaleInpatient.doubleValue() < totalsummaternity.doubleValue()){
    		 System.out.println("inside loop "+totalsumFemaleInpatient);
    		 userSecurityProfile.getSecurityProfile().setActiveTab("Income Profile");
      		TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.female.maternityValidation");
				throw expTTK;
        	}*/
    	 
    	 
    	 int retvalNationality = Double.compare(totalsumnationality.doubleValue(),totalNumLivesclientBd.doubleValue());
    	 if (retvalNationality > 0 || retvalNationality < 0){
       		TTKException expTTK = new TTKException();
       		userSecurityProfile.getSecurityProfile().setActiveTab("Income Profile");
 				expTTK.setMessage("error.pricing.nationality");
 				throw expTTK;
         	}
    	 
    	 
    	return null;
    	
    }
    /**
     * This method is called from the struts framework.
     * This method is used to navigate to detail screen to add a record.
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    
    public ActionForward doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doAdd method of InsuranceCompanyAction");
    		setLinks(request);
    		DynaActionForm frmSwPricing = (DynaActionForm)form;
            UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().
            										 getAttribute("UserSecurityProfile");
            InsPricingVO insPricingVO=new InsPricingVO();
            //code to get the Next Abbreviation for Insurance
            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
            frmSwPricing.initialize(mapping);
            
            
            insPricingVO.setTrendFactor("6");
          
           
        	   
            frmSwPricing= (DynaActionForm)FormUtils.setFormValues("frmSwPricing", insPricingVO,
					this, mapping, request);
            request.getSession().setAttribute("completeSaveYN","N");
            request.getSession().setAttribute("frmSwPricing",frmSwPricing);
            return this.getForward(strGroupprofile, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    try{
    setLinks(request);
    DynaActionForm frmSwPricing= (DynaActionForm)form;
    InsPricingVO insPricingVO=null;
    String successMsg;String newdata = "N";String pricingNumberAlert = "";
    String singlebutton = request.getParameter("singlebutton");
   

    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    insPricingVO = (InsPricingVO)FormUtils.getFormValues(frmSwPricing, this, mapping, request);
    insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
 // UPLOAD FILE STARTS
    FormFile formFile1 = (FormFile)frmSwPricing.get("file1");
    FormFile formFile2 = (FormFile)frmSwPricing.get("file2");
    FormFile formFile3 = (FormFile)frmSwPricing.get("file3");
    FormFile formFile4 = (FormFile)frmSwPricing.get("file4");
    FormFile formFile5 = (FormFile)frmSwPricing.get("file5");
    int fileSize = 3*1024*1024;
    
    if(!("").equals(formFile1.getFileName()))
   	{
    	
       	insPricingVO.setAttachmentname1(formFile1.getFileName());
        String lowercaseFileExtn1 = formFile1.getFileName().toLowerCase();checkfileExtention(lowercaseFileExtn1);
        if(fileSize<=formFile1.getFileSize())
		{
        	TTKException expTTK = new TTKException();
			expTTK.setMessage("error.Upload.size1.mb");
			throw expTTK;
		}
   	}
    if(!("").equals(formFile2.getFileName()))
   	{
       	insPricingVO.setAttachmentname2(formFile2.getFileName());
        String lowercaseFileExtn2 = formFile2.getFileName().toLowerCase();checkfileExtention(lowercaseFileExtn2);
        if(fileSize<=formFile2.getFileSize())
		{
        	TTKException expTTK = new TTKException();
			expTTK.setMessage("error.Upload.size2.mb");
			throw expTTK;
		}
   	}
    if(!("").equals(formFile3.getFileName()))
   	{
       	insPricingVO.setAttachmentname3(formFile3.getFileName());
        String lowercaseFileExtn3 = formFile3.getFileName().toLowerCase();checkfileExtention(lowercaseFileExtn3);
        if(fileSize<=formFile3.getFileSize())
		{
        	TTKException expTTK = new TTKException();
			expTTK.setMessage("error.Upload.size3.mb");
			throw expTTK;
		}
   	}
    if(!("").equals(formFile4.getFileName()))
   	{
       	insPricingVO.setAttachmentname4(formFile4.getFileName());
        String lowercaseFileExtn4 = formFile4.getFileName().toLowerCase();checkfileExtention(lowercaseFileExtn4);
        if(fileSize<=formFile4.getFileSize())
		{
        	TTKException expTTK = new TTKException();
			expTTK.setMessage("error.Upload.size4.mb");
			throw expTTK;
		}
   	}
    if(!("").equals(formFile5.getFileName()))
   	{
       	insPricingVO.setAttachmentname5(formFile5.getFileName());
        String lowercaseFileExtn5 = formFile5.getFileName().toLowerCase();checkfileExtention(lowercaseFileExtn5);
        if(fileSize<=formFile5.getFileSize())
		{
        	TTKException expTTK = new TTKException();
			expTTK.setMessage("error.Upload.size5.mb");
			throw expTTK;
		}
   	}
				
		/*	select drop down validation*/
			setSelectModeform(insPricingVO);
		
		if(insPricingVO.getGroupProfileSeqID() == null)
		{
			newdata = "Y";
		}
		
		//System.out.println("formFile1---"+formFile1);
			 insPricingVO.setSourceAttchments1(formFile1);	
			 insPricingVO.setSourceAttchments2(formFile2);
			 insPricingVO.setSourceAttchments3(formFile3);
			 insPricingVO.setSourceAttchments4(formFile4);
			 insPricingVO.setSourceAttchments5(formFile5);
			 Long lpricingSeqId= insuranceObject.swSavePricingList(insPricingVO);
    		 if(lpricingSeqId > 0)successMsg="Details Added Successfully";
    		 else successMsg="Details Updated Successfully";
			 insPricingVO= insuranceObject.swSelectPricingList(lpricingSeqId);
			
			// if(newdata.equalsIgnoreCase("Y")){
					pricingNumberAlert = " Please note the pricing reference number "+insPricingVO.getPricingRefno()+" for future";
			//	}
				
			ArrayList alPricingPolicyList= null;
        	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
        	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(insPricingVO.getClientName());
        	 if(alPricingPolicyList==null){
				  alPricingPolicyList=new ArrayList();
	            }//end of if(alCityList==null) 
        	
			 frmSwPricing= (DynaActionForm)FormUtils.setFormValues("frmSwPricing",	insPricingVO, this, mapping, request);
			 frmSwPricing.set("newdataentry", newdata);
			 frmSwPricing.set("pricingNumberAlert", pricingNumberAlert);	
			 frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
			 
			 
			 request.getSession().setAttribute("frmSwPricing",frmSwPricing);
			 request.getSession().setAttribute("ClientCode", insPricingVO.getClientCode());	
			 request.getSession().setAttribute("policyNumber", insPricingVO.getPreviousPolicyNo());	
			 request.getSession().setAttribute("clientName", insPricingVO.getClientName());	
			 request.getSession().setAttribute("pricingRefNo", insPricingVO.getPricingRefno());	
			 request.getSession().setAttribute("completeSaveYN",insPricingVO.getCompleteSaveYN());
			 request.getSession().setAttribute("renewalYN",insPricingVO.getRenewalYN());
			 request.getSession().setAttribute("newdataentry", newdata);
			 request.getSession().setAttribute("pricingNumberAlert", pricingNumberAlert);
			 request.getSession().setAttribute("totalNoOfLives",insPricingVO.getTotalCovedLives());
			 request.getSession().setAttribute("TotalMaternityLives",insPricingVO.getTotalLivesMaternity());
			 request.getSession().setAttribute("GroupProfileSeqID",insPricingVO.getGroupProfileSeqID());
			 request.getSession().setAttribute("DentalLivesYN",insPricingVO.getDentalLivesYN());
			 request.getSession().setAttribute("OpticalLivesYN",insPricingVO.getOpticalLivesYN());
    		 request.setAttribute("successMsg",successMsg);
    		 
    		 this.addToWebBoard(insPricingVO, request);
    		
    		

	if(singlebutton.equalsIgnoreCase("save")){
		 return this.getForward(strGroupprofile, mapping, request);
	}
	else{
		
		 return mapping.findForward(strSaveproceed);
	}
    
    }//end of try
    catch(TTKException expTTK)
    {
    return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
    }//end of catch(Exception exp)
    }
	//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

   

    
    private void checkfileExtention(String lowercaseFileExtn) throws TTKException {
		// TODO Auto-generated method stub
    	String flag= "";
        if ((lowercaseFileExtn.endsWith("jpeg")|| (lowercaseFileExtn.endsWith("jpg"))|| (lowercaseFileExtn.endsWith("gif")) ||(lowercaseFileExtn.endsWith("png"))
        	|| (lowercaseFileExtn.endsWith("zip")) || (lowercaseFileExtn.endsWith("pdf")) || (lowercaseFileExtn.endsWith("xls")) || (lowercaseFileExtn.endsWith("xlsx"))
        	|| (lowercaseFileExtn.endsWith("doc")) || (lowercaseFileExtn.endsWith("docx")) || (lowercaseFileExtn.endsWith("txt")) ))
        {
        	flag="success";
        }else{
        	TTKException expTTK = new TTKException();
			expTTK.setMessage("error.Upload.required");
			throw expTTK;
        }
	}

    
   
   

	public ActionForward doIncomeProfile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		try
		{
			log.debug("Inside the doSearch method of CriticalICDListAction");
			setLinks(request);
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();


			  DynaActionForm frmSwIncomeProfile= (DynaActionForm)form;
			  ArrayList alprofileIncomeList=null;
			  InsPricingVO  insPricingVO =null;
			 
			
			  long group_seq_id=0;
			  if(request.getSession().getAttribute("GroupProfileSeqID")!=null)
			  group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			  String pricingNumberAlert=  "";
			  if("Y".equalsIgnoreCase(request.getParameter("proceedbutton"))){	 
				 pricingNumberAlert=  (String)request.getSession().getAttribute("pricingNumberAlert");
			   }
					
		
			  insPricingVO  =  insuranceObject.getfalgPricingvalue(group_seq_id);
			  String bene_covered_FlagYN = insPricingVO.getBenecoverFlagYN();
			  String calCPM_FlagYN = insPricingVO.getBenecoverFlagYN();
			  
			String pricingRefNo=  (String)request.getSession().getAttribute("pricingRefNo");
			String newdataentry=  (String)request.getSession().getAttribute("newdataentry");
			String completeSaveYN = (String)request.getSession().getAttribute("completeSaveYN");
			String clientCode=null;
			String policyNumber= null;
			String clientName=null;
			if(request.getSession().getAttribute("ClientCode")!=null)
				clientCode=(String) request.getSession().getAttribute("ClientCode");	
			if(request.getSession().getAttribute("policyNumber")!=null)
			policyNumber=(String) request.getSession().getAttribute("policyNumber");	
			if(request.getSession().getAttribute("clientName")!=null)
			clientName=(String) request.getSession().getAttribute("clientName");	
			ArrayList dataList=new ArrayList<>();
			InsPricingVO insPricingVOPrev  =null;
			 dataList.add(policyNumber);
	            dataList.add(clientName);
	            dataList.add(clientCode);
	            if(policyNumber!=null&&clientName!=null&&clientCode!=null)
	            	insPricingVOPrev= insuranceObject.getPolicyStatusInfo(dataList); 
		//	if(newdataentry.equalsIgnoreCase("Y")){
			
		//	}
			
			
			if(group_seq_id > 0 && completeSaveYN.equalsIgnoreCase("Y"))
			{
			
			 if(bene_covered_FlagYN.equalsIgnoreCase("Y")){
				 alprofileIncomeList= insuranceObject.getBenefitvalueAfter(group_seq_id);

			 }else{
				 alprofileIncomeList= insuranceObject.getBenefitvalueBefore(group_seq_id);

			 }

		
			 frmSwIncomeProfile.set("Message","N");
			}
			else
			{
				
				frmSwIncomeProfile.initialize(mapping);
				frmSwIncomeProfile.set("Message",request.getParameter("Message")); 
				frmSwIncomeProfile.set("Message","Y"); 
				if(completeSaveYN == null){
		    	TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.required");
				throw expTTK;
				}else if(completeSaveYN.equalsIgnoreCase("N")){
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.pricing.complete.screen");
					throw expTTK;
				}else if(group_seq_id == 0){
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.pricing.complete.screen");
					throw expTTK;
				}
				
			}
			frmSwIncomeProfile.set("profileBenefitList",alprofileIncomeList.get(0)); 
			frmSwIncomeProfile.set("profileNationalityList",alprofileIncomeList.get(1)); 
					
			frmSwIncomeProfile.set("sumTotalLives",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLives());  
			frmSwIncomeProfile.set("sumTotalLivesMaternity",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesMaternity());  
			frmSwIncomeProfile.set("sumTotalLivesOptical",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesOptical());  
			frmSwIncomeProfile.set("sumTotalLivesDental",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesDental());  
			frmSwIncomeProfile.set("sumNationalityLives",((InsPricingVO)(alprofileIncomeList.get(2))).getSumNationalityLives());  
			
			
			frmSwIncomeProfile.set("pricingNumberAlert", pricingNumberAlert);
			frmSwIncomeProfile.set("dentalLivesYN", (String)request.getSession().getAttribute("DentalLivesYN"));
			frmSwIncomeProfile.set("opticalLivesYN", (String)request.getSession().getAttribute("OpticalLivesYN"));
			if(bene_covered_FlagYN.equalsIgnoreCase("Y")){
			frmSwIncomeProfile.set("totalNoOfLives",((InsPricingVO)alprofileIncomeList.get(3)).getTotalCovedLives());
			frmSwIncomeProfile.set("TotalMaternityLives",((InsPricingVO)alprofileIncomeList.get(3)).getTotalLivesMaternity());
			}else{
				frmSwIncomeProfile.set("totalNoOfLives",(String)request.getSession().getAttribute("totalNoOfLives"));
				frmSwIncomeProfile.set("TotalMaternityLives",(String)request.getSession().getAttribute("TotalMaternityLives"));
			}
			
			request.getSession().setAttribute("frmSwIncomeProfile", frmSwIncomeProfile);
			if(insPricingVOPrev!=null)
			request.getSession().setAttribute("numberOfLives", insPricingVOPrev.getNumberOfLives());
			request.getSession().setAttribute("showflag", "N");	

			//finally return to the grid screen
			return this.getForward(strIncomeprofile, mapping, request);
	    }//end of try
	    catch(TTKException expTTK)
	    {
	    return this.processExceptions(request, mapping, expTTK);
	    }//end of catch(TTKException expTTK)
	    catch(Exception exp)
	    {
	    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
	    }//end of catch(Exception exp)
	    }
    
    public ActionForward doSaveIncome(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
			log.debug("Inside CriticalICDListAction SW doSave");
			InsPricingVO  insPricingVO =new InsPricingVO();
			DynaActionForm frmSwIncomeProfile=null;
			try
			{
			setLinks(request);
			frmSwIncomeProfile=(DynaActionForm)form;
			String singlebutton = request.getParameter("singlebutton");
			//DynaActionForm frmSwIncomeProfile = (DynaActionForm)request.getSession().getAttribute("frmSwPricing");
			insPricingVO =(InsPricingVO)FormUtils.getFormValues(frmSwIncomeProfile, "frmSwIncomeProfile",this, mapping, request);
			InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject(); 
			long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			//group_seq_id=192;
			insPricingVO.setGndrdesc((String[])frmSwIncomeProfile.get("gndrdesc"));
			insPricingVO.setAge_range((String[])frmSwIncomeProfile.get("age_range"));
			insPricingVO.setTotalCoverdLives((String[])frmSwIncomeProfile.get("totalCoverdLives"));
			insPricingVO.setOvrprtflio_dstr((String[])frmSwIncomeProfile.get("ovrprtflio_dstr"));
			insPricingVO.setBenf_typeseqid((Long[])frmSwIncomeProfile.get("benf_typeseqid"));
			insPricingVO.setGndrtypeseqid((Long[])frmSwIncomeProfile.get("gndrtypeseqid"));
			insPricingVO.setAge_rngseqid((Long[])frmSwIncomeProfile.get("age_rngseqid"));
			insPricingVO.setBenf_lives_seq_id((Long[])frmSwIncomeProfile.get("benf_lives_seq_id"));
			insPricingVO.setBenfdesc((String[])frmSwIncomeProfile.get("benfdesc"));
			insPricingVO.setNatl_typeseqid((Long[])frmSwIncomeProfile.get("natl_typeseqid"));
			insPricingVO.setNatl_seqid((Long[])frmSwIncomeProfile.get("natl_seqid"));
			insPricingVO.setNatl_name((String[])frmSwIncomeProfile.get("natl_name"));
			insPricingVO.setNatCoverdLives((String[])frmSwIncomeProfile.get("natCoverdLives"));
			insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			insPricingVO.setGroupProfileSeqID(group_seq_id);
			String totalNumLivesclient = (String) frmSwIncomeProfile.get("totalNoOfLives");
			String totalMatNumLivesclient =(String) frmSwIncomeProfile.get("TotalMaternityLives");
			String dentalLivesYN =(String) frmSwIncomeProfile.get("dentalLivesYN");
			String opticalLivesYN =(String) frmSwIncomeProfile.get("opticalLivesYN");
			 String pricingNumberAlert=  (String)request.getSession().getAttribute("pricingNumberAlert");

			
			if(totalMatNumLivesclient.equals("")){
				totalMatNumLivesclient = "0";
			}
			
			if(!singlebutton.equalsIgnoreCase("partialsave")){
			String validation = calculateValidation(insPricingVO,totalNumLivesclient,totalMatNumLivesclient,dentalLivesYN,opticalLivesYN,request);//Validation Added for income profile
			}
			
		
			
			int strTransactionseqId= insuranceObject.swSaveIncomeProfile(insPricingVO);
		
			int nationalitysave = insuranceObject.swSaveIncomeNatProfile(insPricingVO);
			
			if(strTransactionseqId>0 && nationalitysave >0)
			{
					request.setAttribute("updated","message.savedSuccessfully");
					request.getSession().setAttribute("showflag", "N");
					 
			} 
			
			
			ArrayList alprofileIncomeList= insuranceObject.getBenefitvalueAfter(group_seq_id);

			/*benefit code screen2*/
			frmSwIncomeProfile.set("pricingNumberAlert", pricingNumberAlert);
			frmSwIncomeProfile.set("profileBenefitList",alprofileIncomeList.get(0)); 
			frmSwIncomeProfile.set("profileNationalityList",alprofileIncomeList.get(1)); 
			frmSwIncomeProfile.set("sumTotalLives",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLives());  
			frmSwIncomeProfile.set("sumTotalLivesMaternity",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesMaternity());  
			frmSwIncomeProfile.set("sumTotalLivesOptical",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesOptical());  
			frmSwIncomeProfile.set("sumTotalLivesDental",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesDental());  
			frmSwIncomeProfile.set("sumNationalityLives",((InsPricingVO)(alprofileIncomeList.get(2))).getSumNationalityLives());  
			
			
			request.getSession().setAttribute("GroupProfileSeqID",group_seq_id);
		/*	System.out.println("total lives-----"+(String)request.getSession().getAttribute("totalNoOfLives"));
			System.out.println("total lives-----"+(String)request.getSession().getAttribute("TotalMaternityLives"));*/

			frmSwIncomeProfile.set("totalNoOfLives",((InsPricingVO)alprofileIncomeList.get(3)).getTotalCovedLives());
			frmSwIncomeProfile.set("TotalMaternityLives",((InsPricingVO)alprofileIncomeList.get(3)).getTotalLivesMaternity());
			request.getSession().setAttribute("frmSwIncomeProfile",frmSwIncomeProfile); 

			//return doSearch(mapping ,form, request, response);
			if(singlebutton.equalsIgnoreCase("save") || singlebutton.equalsIgnoreCase("partialsave")  ){
				return this.getForward(strIncomeprofile, mapping, request);
			}
			else {
				return mapping.findForward("incomeprofileProceed");
			}
		 }//end of try
	    catch(TTKException expTTK)
	    {
	    	request.setAttribute("totalCoverdLives", insPricingVO.getTotalCoverdLives());
	    	request.setAttribute("natCoverdLives", insPricingVO.getNatCoverdLives());
	    	return this.processExceptions(request, mapping, expTTK);
	    }//end of catch(TTKException expTTK)
	    catch(Exception exp)
	    {
	    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
	    }//end of catch(Exception exp)
	    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  
    
	

	public ActionForward doEditIncome(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    try{
    setLinks(request);
    DynaActionForm frmSwPricing= (DynaActionForm)form;
   InsPricingVO insPricingVO=null;


    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
//long Seq_Id =(long)request.getSession().getAttribute("GroupProfileSeqID");
  
   // System.out.println("seq id ----"+TTKCommon.getWebBoardId(request));
    
    insPricingVO=new InsPricingVO();
	TableData tableData = TTKCommon.getTableData(request);

	//Edit flow
	if(!((String)(frmSwPricing).get("rownum")).equals("")) 
	{
		insPricingVO =(InsPricingVO)tableData.getRowInfo(Integer.parseInt((String)(frmSwPricing).
																					get("rownum")));
  
		 insPricingVO= insuranceObject.swSelectPricingList(insPricingVO.getGroupProfileSeqID());
		 ArrayList alPricingPolicyList= null;
     	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
     	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(insPricingVO.getClientName());
     	 if(alPricingPolicyList==null){
			  alPricingPolicyList=new ArrayList();
           }//end of if(alCityList==null)
     	
		 frmSwPricing = (DynaActionForm)FormUtils.setFormValues("frmSwPricing",insPricingVO,
																		this,mapping,request);
		//add the selected item to the web board and make it as default selected
			this.addToWebBoard(insPricingVO, request);
			frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
		 request.getSession().setAttribute("showflag", "Y");

	}//end of if(!((String)(formTariffItem).get("rownum")).equals(""))
	else if(insPricingVO.getGroupProfileSeqID() > 0)
	{
		insPricingVO.setGroupProfileSeqID((long)request.getSession().getAttribute("GroupProfileSeqID"));
		insPricingVO= insuranceObject.swSelectPricingList(insPricingVO.getGroupProfileSeqID());
		
		ArrayList alPricingPolicyList= null;
     	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
     	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(insPricingVO.getClientName());
     	 if(alPricingPolicyList==null){
			  alPricingPolicyList=new ArrayList();
           }//end of if(alCityList==null)
     	
		frmSwPricing = (DynaActionForm)FormUtils.setFormValues("frmSwPricing",insPricingVO,
				this,mapping,request);
		frmSwPricing.set("Message","N"); 
		frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
	}
	else
	{
		
		frmSwPricing.set("Message",request.getParameter("Message")); 
		frmSwPricing.set("Message","Y"); 
    	TTKException expTTK = new TTKException();
		expTTK.setMessage("error.pricing.required");
		throw expTTK;
	}
	String pricingNumberAlert  = "Please note the pricing reference number "+insPricingVO.getPricingRefno()+" for future";
	request.getSession().setAttribute("pricingNumberAlert", pricingNumberAlert);
	 request.getSession().setAttribute("GroupProfileSeqID",insPricingVO.getGroupProfileSeqID());
	 request.getSession().setAttribute("completeSaveYN",insPricingVO.getCompleteSaveYN());
	 
	 request.getSession().setAttribute("ClientCode",insPricingVO.getClientCode());
	 request.getSession().setAttribute("policyNumber", insPricingVO.getPreviousPolicyNo());	
	 request.getSession().setAttribute("clientName", insPricingVO.getClientName());
	 
	 request.getSession().setAttribute("totalNoOfLives",insPricingVO.getTotalCovedLives());
	 request.getSession().setAttribute("TotalMaternityLives",insPricingVO.getTotalLivesMaternity());
	
	 request.getSession().setAttribute("DentalLivesYN",insPricingVO.getDentalLivesYN());
	 request.getSession().setAttribute("OpticalLivesYN",insPricingVO.getOpticalLivesYN());
	 request.getSession().setAttribute("newdataentry", "N");	
	 request.getSession().setAttribute("networkType", frmSwPricing.get("networkList"));
	 request.getSession().setAttribute("numberOfLives", null);
	 request.getSession().setAttribute("renewalYN",insPricingVO.getRenewalYN());
	 request.getSession().setAttribute("frmSwPricing",frmSwPricing);
	return this.getForward(strGroupprofile, mapping, request);
    
    
    }//end of try
    catch(TTKException expTTK)
    {
    return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
    }//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	public ActionForward doViewGroupProfile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
		//log.info("doViewGroupProfile");
    try{
    setLinks(request);
 //   DynaActionForm frmSwPricing= (DynaActionForm)form;
    DynaValidatorForm frmSwPricing = (DynaValidatorForm)form;
   InsPricingVO insPricingVO=null;
	ArrayList alPricingPolicyList= null;


    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();

    long Seq_Id=(long)request.getSession().getAttribute("GroupProfileSeqID");
   
    
    insPricingVO=new InsPricingVO();
	TableData tableData = TTKCommon.getTableData(request);
	
	
	if(!(TTKCommon.checkNull(request.getParameter(strRownum)).equals("")))
	{
		insPricingVO = (InsPricingVO)tableData.getRowInfo(Integer.parseInt((String)(frmSwPricing).
																				get(strRownum)));
		insPricingVO= insuranceObject.swSelectPricingList(insPricingVO.getGroupProfileSeqID());
    	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
    	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(insPricingVO.getClientName());
    	 if(alPricingPolicyList==null){
			  alPricingPolicyList=new ArrayList();
           }//end of if(alCityList==null)
		//add the selected item to the web board and make it as default selected
		this.addToWebBoard(insPricingVO, request);
		frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
		frmSwPricing.set("Message","N"); 
		//this.valueObjectToForm(productVO, frmProductDetail);				
	}//end if(!(TTKCommon.checkNull(request.getParameter(strRownum)).equals("")))
	else if(TTKCommon.getWebBoardId(request) != null)
	{
		//if web board id is found, set it as current web board id
		Long lProdPolicySeqId=TTKCommon.getWebBoardId(request);
		//get the product details from the Dao object
		insPricingVO= insuranceObject.swSelectPricingList(lProdPolicySeqId);
    	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
    	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(insPricingVO.getClientName());
    	 if(alPricingPolicyList==null){
			  alPricingPolicyList=new ArrayList();
           }//end of if(alCityList==null)
		frmSwPricing.set("Message","N"); 
		
		
	}//end of else if(TTKCommon.getWebBoardId(request) != null)
	
/*
 if(Seq_Id > 0)
	{
		insPricingVO.setGroupProfileSeqID((long)request.getSession().getAttribute("GroupProfileSeqID"));
		insPricingVO= insuranceObject.swSelectPricingList(insPricingVO.getGroupProfileSeqID());
		frmSwPricing = (DynaActionForm)FormUtils.setFormValues("frmSwPricing",insPricingVO,
				this,mapping,request);
		frmSwPricing.set("Message","N"); 
	}*/
	else
	{
		
		frmSwPricing.set("Message",request.getParameter("Message")); 
		frmSwPricing.set("Message","Y"); 
		 if(alPricingPolicyList==null){
			  alPricingPolicyList=new ArrayList();
          }//end of if(alCityList==null)
    	TTKException expTTK = new TTKException();
		expTTK.setMessage("error.pricing.required");
		throw expTTK;
	}
	String pricingNumberAlert  = "Please note the pricing reference number "+insPricingVO.getPricingRefno()+" for future";
	request.getSession().setAttribute("pricingNumberAlert", pricingNumberAlert);
	 request.getSession().setAttribute("GroupProfileSeqID",insPricingVO.getGroupProfileSeqID());
	 request.getSession().setAttribute("completeSaveYN",insPricingVO.getCompleteSaveYN());
	 
	 request.getSession().setAttribute("ClientCode",insPricingVO.getClientCode());
	 request.getSession().setAttribute("totalNoOfLives",insPricingVO.getTotalCovedLives());
	 request.getSession().setAttribute("TotalMaternityLives",insPricingVO.getTotalLivesMaternity());
	
	 request.getSession().setAttribute("DentalLivesYN",insPricingVO.getDentalLivesYN());
	 request.getSession().setAttribute("OpticalLivesYN",insPricingVO.getOpticalLivesYN());
	 request.getSession().setAttribute("newdataentry", "N");	
		
	 frmSwPricing = (DynaValidatorForm)FormUtils.setFormValues("frmSwPricing",insPricingVO,
				this,mapping,request);

	 frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
	 request.getSession().setAttribute("frmSwPricing",frmSwPricing);
	return this.getForward(strGroupprofile, mapping, request);
    
    
    }//end of try
    catch(TTKException expTTK)
    {
    return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
    }//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doCloseIncome(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    try{
    setLinks(request);
    DynaActionForm frmSwPricing= (DynaActionForm)form;
    InsPricingVO insPricingVO=null;
    String successMsg;

    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    insPricingVO = (InsPricingVO)FormUtils.getFormValues(frmSwPricing, this, mapping, request);
    insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
   long lpricingSeqId=  (long)request.getSession().getAttribute("GroupProfileSeqID");    

    		
    		 insPricingVO= insuranceObject.swSelectPricingList(lpricingSeqId);
    		 ArrayList alPricingPolicyList= null;
         	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
         	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(insPricingVO.getClientName());
         	 if(alPricingPolicyList==null){
				  alPricingPolicyList=new ArrayList();
	            }//end of if(alCityList==null)
    		 frmSwPricing= (DynaActionForm)FormUtils.setFormValues("frmSwPricing",	insPricingVO, this, mapping, request);
			 frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
    		 request.getSession().setAttribute("frmSwPricing",frmSwPricing);
    		 request.getSession().setAttribute("GroupProfileSeqID",lpricingSeqId);
    			//request.setAttribute("successMsg",successMsg);
    return this.getForward(strGroupprofile, mapping, request);
    }//end of try
    catch(TTKException expTTK)
    {
    return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
    }//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
  
    
    
   public ActionForward  doViewUploadDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException {
	    
	    ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
	    InputStream  iStream = null;
	    String fileExtn = "";
	   
	  try{   
			
			String strFile	=	request.getParameter("filePath");
			String strFileName	=	"fileName.jpeg";
	    	String strFileNoRecords = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/noRecordsFound.pdf";
	    	String strFileerror = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/fileImproper.pdf";
	       
	    	InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
	    	long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
	    	String filename = request.getParameter("filename");
	    	    
	    	  
	    	  InsPricingVO insPricingVOdoc= insuranceObject.swSelectPricingList(group_seq_id);
		    	  if(filename.equalsIgnoreCase("file1")){
					  iStream= insPricingVOdoc.getInputstreamdoc1();
					  fileExtn = insPricingVOdoc.getAttachmentname1();
			    	  }
		    	  if(filename.equalsIgnoreCase("file2")){
					  iStream= insPricingVOdoc.getInputstreamdoc2();
					  fileExtn = insPricingVOdoc.getAttachmentname2();
			    	  }
		    	  if(filename.equalsIgnoreCase("file3")){
					  iStream= insPricingVOdoc.getInputstreamdoc3();
					  fileExtn = insPricingVOdoc.getAttachmentname3();

			    	  }
		    	  if(filename.equalsIgnoreCase("file4")){
					  iStream= insPricingVOdoc.getInputstreamdoc4();
					  fileExtn = insPricingVOdoc.getAttachmentname4();

			    	  }
		    	  if(filename.equalsIgnoreCase("file5")){
					  iStream= insPricingVOdoc.getInputstreamdoc5();
					  fileExtn = insPricingVOdoc.getAttachmentname5();
			    	  }
		    	 
		    	  
			  if((iStream!=null) && (!iStream.equals("")))
			  {
	           
	        	 bis = new BufferedInputStream(iStream);
		         baos=new ByteArrayOutputStream();
		         String lowercaseFileExtn = fileExtn.toLowerCase();
		         
		         if ((lowercaseFileExtn.endsWith("jpeg")|| (lowercaseFileExtn.endsWith("jpg"))|| (lowercaseFileExtn.endsWith("gif")) ||(lowercaseFileExtn.endsWith("png")))){
		    	          
		        	    InputStream in=iStream;
		        	    ServletOutputStream out = response.getOutputStream();
		        	    byte[] buf = new byte[10*1024];
		        	    int len;
		        	    while ((len = in.read(buf)) > 0) {
		        	    out.write(buf, 0, len);
		        	    }
		        	    in.close();
		        	    out.flush();
		        	    out.close();
		        	    }//end image format
		         else{
		           
		         
		         if(lowercaseFileExtn.endsWith("doc") || lowercaseFileExtn.endsWith("docx"))
		    		{
		    			response.setContentType("application/msword");
		    			response.addHeader("Content-Disposition","attachment; filename="+fileExtn);
		    		}//end of if(fileExtn.endsWith("doc"))
		    		else if(lowercaseFileExtn.endsWith("pdf"))
		    		{
		    			response.setContentType("application/pdf");
		    			response.addHeader("Content-Disposition","attachment; filename="+fileExtn);
		    		}//end of else if(fileExtn.endsWith("pdf"))
		    		else if(lowercaseFileExtn.endsWith("xls") || lowercaseFileExtn.endsWith("xlsx"))
		    		{
		    			response.setContentType("application/vnd.ms-excel");
		    			response.addHeader("Content-Disposition","attachment; filename="+fileExtn);
		    		}//end of else if(fileExtn.endsWith("xls"))
		    		else if(lowercaseFileExtn.endsWith("txt")){
				    		response.setContentType("text/plain");
				    		response.setHeader("Content-Disposition","attachment;filename"+fileExtn);
		    		}
		         
	         
	           int ch;
               while ((ch = bis.read()) != -1) baos.write(ch);
               sos = response.getOutputStream();
               baos.writeTo(sos);  
               baos.flush();      
               sos.flush(); 
		         }//end document format
			  }//end   istream null
			  else{
  				File f = new File(strFileNoRecords);
  	    		if(f.isFile() && f.exists()){
  	    			fis = new FileInputStream(f);
  	    		}//end of if(strFile !="")
  	    		BufferedInputStream bist = new BufferedInputStream(fis);
  	    		baos=new ByteArrayOutputStream();
  	    		int ch;
  	    		while ((ch = bist.read()) != -1)
  	    		{
  	    			baos.write(ch);
  	    		}//end of while ((ch = bis.read()) != -1)
  	    		sos = response.getOutputStream();
  	    		baos.writeTo(sos);
			  }
	           
		            }catch(Exception exp)
		            	{
		            		return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
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
    
    
    /**
	 * This method is used to set the batch cmplete policy.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doChangeCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doChangeCorporate method of BatchPolicyAction");
			setLinks(request);
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			//Building the caption
			StringBuffer strAddCaption= new StringBuffer();
			DynaActionForm frmSwPricing=(DynaActionForm)form;
			/*if(!(TTKCommon.checkNull((String)frmSwPricing.get("rownum")).equals("")))
			{
				strAddCaption.append("Policy Details - ").append("Edit").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of if(!(TTKCommon.checkNull((String)frmPolicyDetail.get("rownum")).equals("")))
			else
			{
				strAddCaption.append("Policy Details - ").append("Add").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of else
*/			frmSwPricing.set("caption",strAddCaption.toString());
			request.getSession().setAttribute("frmSwPricing",frmSwPricing);
			return mapping.findForward(strChngCorporate);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
	}//end of doChangeCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	public ActionForward doCheckPolicyNumber(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		InsPricingVO insPricingVO = null;
    		 DynaActionForm frmSwPricing= (DynaActionForm)form;
            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
            String policyNumber = request.getParameter("policyNumber");
            
            insPricingVO   = insuranceObject.getPolicyNumberStatus(policyNumber); 
            if(insPricingVO.getPolicyNumberFlag().equals("N")) 
            {
            	frmSwPricing.set("previousPolicyNo", "");
            	frmSwPricing.set("clientCode", "");
            	TTKException expTTK = new TTKException();
				expTTK.setMessage("error.pricing.Policy.required");
				throw expTTK;
            }
            else
            {  
            	frmSwPricing.set("clientCode", insPricingVO.getClientCode());
            }
			 
			return this.getForward(strGroupprofile, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
   
	 public InsPricingVO setSelectModeform(InsPricingVO insPricingVO) throws Exception{
	   
	  
	    String outpatientBenefit = insPricingVO.getOutpatientBenefit(); 
	    String opdeductableserviceYN = insPricingVO.getOpdeductableserviceYN();
	    String alAhlihospital = insPricingVO.getAlAhlihospital();
	    String alAhlihospOPservices = insPricingVO.getAlAhlihospOPservices();
	    String alOpCopayList = insPricingVO.getOpCopayList();
	   
	    
		
		if(outpatientBenefit.equalsIgnoreCase("N") ||outpatientBenefit.equalsIgnoreCase(""))
		{
			
			 insPricingVO.setOpCopayList("");
			 insPricingVO.setOpDeductableList("");
			 insPricingVO.setOpdeductableserviceYN("");
			 insPricingVO.setOpCopaypharmacy("");
			 insPricingVO.setOpInvestigation("");
			 insPricingVO.setOpCopyconsultn("");
			 insPricingVO.setOpCopyothers("");
			 
			 insPricingVO.setAlAhlihospOPservices("");
			 insPricingVO.setOpCopyalahlihosp("");	 
			 insPricingVO.setOpPharmacyAlAhli("");
			 insPricingVO.setOpConsultAlAhli("");
			 insPricingVO.setOpInvestnAlAhli("");
			 insPricingVO.setOpothersAlAhli("");
		}
		else if("Y".equals(outpatientBenefit)){
			
			if("".equals(opdeductableserviceYN)){
				 insPricingVO.setOpCopayList("");
				 insPricingVO.setOpDeductableList("");
				 insPricingVO.setOpCopaypharmacy("");
				 insPricingVO.setOpInvestigation("");
				 insPricingVO.setOpCopyconsultn("");
				 insPricingVO.setOpCopyothers("");
			
			}else if("Y".equals(opdeductableserviceYN))
				{
				
				if(!"6".equals(alOpCopayList)){
					
				 insPricingVO.setOpDeductableList("");
				}
								
				 insPricingVO.setOpCopaypharmacy("");
				 insPricingVO.setOpInvestigation("");
				 insPricingVO.setOpCopyconsultn("");
				 insPricingVO.setOpCopyothers("");
				}
			else if("N".equals(opdeductableserviceYN)){
				 insPricingVO.setOpCopayList("");
				 insPricingVO.setOpDeductableList("");
				}
			
			//alahli condition
			  if(("N".equals(alAhlihospital)) || ("".equals(alAhlihospital)))
				{
				     insPricingVO.setAlAhlihospOPservices("");
					 insPricingVO.setOpCopyalahlihosp("");	 
					 insPricingVO.setOpPharmacyAlAhli("");
					 insPricingVO.setOpConsultAlAhli("");
					 insPricingVO.setOpInvestnAlAhli("");
					 insPricingVO.setOpothersAlAhli("");
					
				}	
				if("".equals(alAhlihospOPservices)){
					 insPricingVO.setOpCopyalahlihosp("");	 
					 insPricingVO.setOpPharmacyAlAhli("");
					 insPricingVO.setOpConsultAlAhli("");
					 insPricingVO.setOpInvestnAlAhli("");
					 insPricingVO.setOpothersAlAhli("");
				}else if("Y".equals(alAhlihospOPservices))
					{
					 insPricingVO.setOpPharmacyAlAhli("");
					 insPricingVO.setOpConsultAlAhli("");
					 insPricingVO.setOpInvestnAlAhli("");
					 insPricingVO.setOpothersAlAhli("");
					}
				else if("N".equals(alAhlihospOPservices)){
					 insPricingVO.setOpCopyalahlihosp("");	 
					 }
		
		}
		
		//optial dental maternity
		 if(insPricingVO.getOpticalYN().equalsIgnoreCase("N")){
				insPricingVO.setOpticalLivesYN("N"); 
				insPricingVO.setOpticalLimitList(""); 
				insPricingVO.setOpticalCopayList(""); 
			}else if(insPricingVO.getOpticalYN().equalsIgnoreCase("Y")){
				insPricingVO.setOpticalLivesYN("Y"); 
			}
			if(insPricingVO.getDentalYN().equalsIgnoreCase("N")){
				insPricingVO.setDentalLivesYN("N"); 
				insPricingVO.setDentalLimitList(""); 
				insPricingVO.setDentalcopayList(""); 
				insPricingVO.setOrthodonticsCopay(""); 
			}else if(insPricingVO.getDentalYN().equalsIgnoreCase("Y")){
				insPricingVO.setDentalLivesYN("Y"); 
			}
			if(insPricingVO.getMaternityYN().equalsIgnoreCase("N")){
				insPricingVO.setMaternityLimitList(""); 
			}
		
		return insPricingVO;
	    }
	    
	  
	    
	
	  
    public ActionForward setSelectModeType(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    try{
    setLinks(request);
  
    DynaActionForm frmSwPricing= (DynaActionForm)form;
    InsPricingVO insPricingVO=null;
	insPricingVO = (InsPricingVO)FormUtils.getFormValues(frmSwPricing, this, mapping, request);
	frmSwPricing= (DynaActionForm)FormUtils.setFormValues("frmSwPricing",	insPricingVO, this, mapping, request);


    String OpticalType=frmSwPricing.getString("opticalYN");
    String MaternityType=frmSwPricing.getString("maternityYN");
    String DentalType=frmSwPricing.getString("dentalYN");
    String totalLivesMaternity = frmSwPricing.getString("totalLivesMaternity");
    String Obesitytreatment = frmSwPricing.getString("obesityTreatment");
    
    String outpatientBenefit = frmSwPricing.getString("outpatientBenefit");
    String opdeductableserviceYN = frmSwPricing.getString("opdeductableserviceYN");
    String alAhlihospital = frmSwPricing.getString("alAhlihospital");
    String alAhlihospOPservices = frmSwPricing.getString("alAhlihospOPservices");
    
	request.setAttribute("focusId", request.getParameter("focusId"));
	
	if(outpatientBenefit == "N"||outpatientBenefit =="")
	{
	 frmSwPricing.set("opCopayList", "");
	 frmSwPricing.set("opDeductableList", "");
	 frmSwPricing.set("opdeductableserviceYN", "");
	 frmSwPricing.set("opCopaypharmacy", "");
	 frmSwPricing.set("opInvestigation", "");
	 frmSwPricing.set("opCopyconsultn", "");
	 frmSwPricing.set("opCopyothers", "");
	 
	 frmSwPricing.set("alAhlihospOPservices", "");
	 frmSwPricing.set("opCopyalahlihosp", "");	 
	 frmSwPricing.set("opPharmacyAlAhli", "");
	 frmSwPricing.set("opConsultAlAhli", "");
	 frmSwPricing.set("opInvestnAlAhli", "");
	 frmSwPricing.set("opothersAlAhli", "");
	}
	else if("Y".equals(outpatientBenefit)){
		
		if("".equals(opdeductableserviceYN)){
			 frmSwPricing.set("opCopaypharmacy", "");
			 frmSwPricing.set("opInvestigation", "");
			 frmSwPricing.set("opCopyconsultn", "");
			 frmSwPricing.set("opCopyothers", "");
			 frmSwPricing.set("opCopayList", "");
			 frmSwPricing.set("opDeductableList", "");
		
		}else if("Y".equals(opdeductableserviceYN))
			{
		     frmSwPricing.set("opCopaypharmacy", "");
			 frmSwPricing.set("opInvestigation", "");
			 frmSwPricing.set("opCopyconsultn", "");
			 frmSwPricing.set("opCopyothers", "");
			}
		else if("N".equals(opdeductableserviceYN)){
			 frmSwPricing.set("opCopayList", "");
			 frmSwPricing.set("opDeductableList", "");
			}
		
		//alahli condition
		  if(("N".equals(alAhlihospital)) || ("".equals(alAhlihospital)))
			{
			     frmSwPricing.set("alAhlihospOPservices", "");
			     frmSwPricing.set("opCopyalahlihosp", "");
				 frmSwPricing.set("opPharmacyAlAhli", "");
				 frmSwPricing.set("opConsultAlAhli", "");
				 frmSwPricing.set("opInvestnAlAhli", "");
				 frmSwPricing.set("opothersAlAhli", "");
				
			}	
			if("".equals(alAhlihospOPservices)){
				 frmSwPricing.set("opCopyalahlihosp", "");
				 frmSwPricing.set("opPharmacyAlAhli", "");
				 frmSwPricing.set("opConsultAlAhli", "");
				 frmSwPricing.set("opInvestnAlAhli", "");
				 frmSwPricing.set("opothersAlAhli", "");
			}else if("Y".equals(alAhlihospOPservices))
				{
				 frmSwPricing.set("opPharmacyAlAhli", "");
				 frmSwPricing.set("opConsultAlAhli", "");
				 frmSwPricing.set("opInvestnAlAhli", "");
				 frmSwPricing.set("opothersAlAhli", "");
				}
			else if("N".equals(alAhlihospOPservices)){
				 frmSwPricing.set("opCopyalahlihosp", "");
				 }
	
	}
	
	
   /* if("Y".equals(OpticalType))
    	{
    	frmSwPricing.set("opticalLivesYN", "Y");
    	}
    else{
    	frmSwPricing.set("opticalLivesYN", "N");
        }
    
	if("Y".equals(DentalType))
		{
		frmSwPricing.set("dentalLivesYN", "Y");
		}
    else{
		frmSwPricing.set("dentalLivesYN", "N");
	    }*/

    request.getSession().setAttribute("frmSwPricing",frmSwPricing);
    return this.getForward(strGroupprofile, mapping, request);
    }//end of try
    catch(TTKException expTTK)
    {
    return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
    }//end of catch(Exception exp)
    }//end of setNetWorkType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    public ActionForward doViewInputScreen3(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    try{
    setLinks(request);
    DynaActionForm frmSwPricing= (DynaActionForm)form;
   InsPricingVO insPricingVO=null;

    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    long Seq_Id=(long)request.getSession().getAttribute("GroupProfileSeqID");
    insPricingVO=new InsPricingVO();

	if(Seq_Id > 0)
	{
		insPricingVO.setGroupProfileSeqID((long)request.getSession().getAttribute("GroupProfileSeqID"));
		insPricingVO= insuranceObject.swSelectPricingList(insPricingVO.getGroupProfileSeqID());
		frmSwPricing = (DynaActionForm)FormUtils.setFormValues("frmSwPricing",insPricingVO,
				this,mapping,request);
		frmSwPricing.set("Message","N"); 
	}
	else
	{
		frmSwPricing.set("Message",request.getParameter("Message")); 
		frmSwPricing.set("Message","Y"); 
    	TTKException expTTK = new TTKException();
		expTTK.setMessage("error.pricing.required");
		throw expTTK;
	}
	 String pricingNumberAlert  = "Please note the pricing reference number "+insPricingVO.getPricingRefno()+" for future";
	 request.getSession().setAttribute("pricingNumberAlert", pricingNumberAlert);
	 request.getSession().setAttribute("GroupProfileSeqID",insPricingVO.getGroupProfileSeqID());
	 request.getSession().setAttribute("completeSaveYN",insPricingVO.getCompleteSaveYN());
	 request.getSession().setAttribute("newdataentry", "N");	
	 request.getSession().setAttribute("frmSwPricing",frmSwPricing);
     return this.getForward(strInputscreen3, mapping, request);
    
    }//end of try
    catch(TTKException expTTK)
    {
    return this.processExceptions(request, mapping, expTTK);
    }//end of catch(TTKException expTTK)
    catch(Exception exp)
    {
    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
    }//end of catch(Exception exp)
    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
				try{
				log.debug("Inside PreAuthAction doForward");
				setLinks(request);
				TableData tableData = TTKCommon.getTableData(request);
	            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
				tableData.modifySearchData(strForward);//modify the search data
			    ArrayList alInsuranceProfileList= insuranceObject.getSwInsuranceProfileList(tableData.getSearchData());
			    tableData.setData(alInsuranceProfileList, strForward);//set the table data
				request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
				return this.getForward(strPricingHome, mapping, request);   //finally return to the grid screen
				}//end of try
				catch(TTKException expTTK)
				{
				return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
				return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
				}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
    
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
			log.debug("Inside PreAuthAction doBackward");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
            InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			tableData.modifySearchData(strBackward);//modify the search data
		    ArrayList alInsuranceProfileList= insuranceObject.getSwInsuranceProfileList(tableData.getSearchData());
			tableData.setData(alInsuranceProfileList, strBackward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return this.getForward(strPricingHome, mapping, request);   //finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
	}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	  public ActionForward doCopyToWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("Inside the doCopyToWebBoard method of InsuranceCompanyAction");
	    		setLinks(request);
	    		this.populateWebBoard(request);
				return this.getForward(strPricingHome, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
			}//end of catch(Exception exp)
	    }//end of doCopyToWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	  /**
		 * Populates the web board in the session with the selected items in the grid
		 * @param request HttpServletRequest object which contains information about the selected check boxes
		 * @throws TTKException If any run time Excepton occures
	     */
		private void populateWebBoard(HttpServletRequest request)throws TTKException
		{
			String[] strChk = request.getParameterValues("chkopt");
			TableData tableData = (TableData)request.getSession().getAttribute("tableData");
			Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
			ArrayList<Object> alCacheObject = new ArrayList<Object>();
			CacheObject cacheObject = null;
			if(strChk!=null&&strChk.length!=0)
			{
				for(int i=0; i<strChk.length;i++)
				{
					cacheObject = new CacheObject();
					cacheObject.setCacheId(String.valueOf(((InsPricingVO)tableData.getData().
							get(Integer.parseInt(strChk[i]))).getGroupProfileSeqID()));
					cacheObject.setCacheDesc(((InsPricingVO)tableData.getData().get(Integer.parseInt(strChk[i]))).
							getPricingRefno());
					alCacheObject.add(cacheObject);
				}//end of for(int i=0; i<strChk.length;i++)
			}//end of if(strChk!=null&&strChk.length!=0)
			if(toolbar != null)
				toolbar.getWebBoard().addToWebBoardList(alCacheObject);
		}//end of populateWebBoard(HttpServletRequest request)
	
		
		private void addToWebBoard(InsPricingVO insPricingVO, HttpServletRequest request)
		{
			Toolbar toolbar = (Toolbar) request.getSession().getAttribute("toolbar");
			CacheObject cacheObject = new CacheObject();
			String strPricingRefno=insPricingVO.getPricingRefno();
			/*System.out.println("addToWebBoard getGroupProfileSeqID---"+insPricingVO.getGroupProfileSeqID());
			System.out.println("addToWebBoard getPricingRefno---"+insPricingVO.getPricingRefno());*/

			cacheObject.setCacheId(insPricingVO.getGroupProfileSeqID().toString());
			cacheObject.setCacheDesc(insPricingVO.getPricingRefno());
			ArrayList<Object> alCacheObject = new ArrayList<Object>();
			alCacheObject.add(cacheObject);
			//if the object(s) are added to the web board, set the current web board id
			//if(toolbar.getWebBoard().addToWebBoardList(alCacheObject))
			toolbar.getWebBoard().addToWebBoardList(alCacheObject);
			toolbar.getWebBoard().setCurrentId(cacheObject.getCacheId());
		}//end of addToWebBoard(HospitalVO hospitalVO, HttpServletRequest request)
		
		/**
		 * This method is used to get the details of the selected record from web-board.
		 * Finally it forwards to the appropriate view based on the specified forward mappings
		 *
		 * @param mapping The ActionMapping used to select this instance
		 * @param form The optional ActionForm bean for this request (if any)
		 * @param request The HTTP request we are processing
		 * @param response The HTTP response we are creating
		 * @return ActionForward Where the control will be forwarded, after this request is processed
		 * @throws Exception if any error occurs
		 */
		public ActionForward doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
																HttpServletResponse response) throws Exception{
			try{
				log.debug("Inside the doChangeWebBoard method of ProductSearchAction");
				setLinks(request);
				//if web board id is found, set it as current web board id
				//TTKCommon.setWebBoardId(request);
				//get the session bean from the bean pool for each excecuting thread
				 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
				Long lGroupProfileSeqId=TTKCommon.getWebBoardId(request);
				//System.out.println("=================================================");
				//create a new Product object
				InsPricingVO insPricingVO = new InsPricingVO();
				  DynaActionForm frmSwPricing= (DynaActionForm)form;
				  frmSwPricing.initialize(mapping);
				//get the product details from the Dao object
				
				 insPricingVO= insuranceObject.swSelectPricingList(lGroupProfileSeqId);
				 ArrayList alPricingPolicyList= null;
		        	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
		        	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(insPricingVO.getClientName());
		        	 if(alPricingPolicyList==null){
						  alPricingPolicyList=new ArrayList();
			            }//end of if(alCityList==null)
				 
				 frmSwPricing = (DynaActionForm)FormUtils.setFormValues("frmSwPricing",insPricingVO, this, mapping, request);
				
				 frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
				 String pricingNumberAlert  = "Please note the pricing reference number "+insPricingVO.getPricingRefno()+" for future";
				 request.getSession().setAttribute("pricingNumberAlert", pricingNumberAlert);
				 request.getSession().setAttribute("GroupProfileSeqID",insPricingVO.getGroupProfileSeqID());
				 request.getSession().setAttribute("completeSaveYN",insPricingVO.getCompleteSaveYN());
				 request.getSession().setAttribute("ClientCode",insPricingVO.getClientCode());
				 request.getSession().setAttribute("policyNumber", insPricingVO.getPreviousPolicyNo());	
				 request.getSession().setAttribute("clientName", insPricingVO.getClientName());
				 request.getSession().setAttribute("totalNoOfLives",insPricingVO.getTotalCovedLives());
				 request.getSession().setAttribute("TotalMaternityLives",insPricingVO.getTotalLivesMaternity());
				 request.getSession().setAttribute("DentalLivesYN",insPricingVO.getDentalLivesYN());
				 request.getSession().setAttribute("OpticalLivesYN",insPricingVO.getOpticalLivesYN());
				 request.getSession().setAttribute("newdataentry", "N");
				 request.getSession().setAttribute("renewalYN",insPricingVO.getRenewalYN());
				request.getSession().setAttribute("frmSwPricing",frmSwPricing);
				//add the product to session
				request.getSession().setAttribute("insPricingVO",insPricingVO);
				
				return this.getForward(strGroupprofile, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
			}//end of catch(Exception exp)
		}//end of ChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				//HttpServletResponse response)
		
		
		 public ActionForward doshowTemplate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
					HttpServletResponse response) throws TTKException {
			 	log.debug("Inside ChequeSearchAction class  doshowChequeTemplate");
				ByteArrayOutputStream baos=null;
				OutputStream sos = null;
				FileInputStream fis= null; 
				File file = null;
				BufferedInputStream bis =null;
				try
				{
					setLinks(request);
						response.setContentType("application/txt");
						response.setHeader("Content-Disposition", "attachment;filename=PricingMemTemplate.xls");
		               
					String fileName =	TTKPropertiesReader.getPropertyValue("QatarXmlPricingDir")+"PricingMemTemplate.xls";
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
		            return (mapping.findForward(strIncomeprofile));
				}//end of try
				catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request,mapping,new TTKException(exp,strInsError));
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
						return this.processExceptions(request,mapping,new TTKException(exp,strInsError));
					}//                 
				}
			}
		 
		 
			public ActionForward doUploadMemDetails (ActionMapping mapping,ActionForm form,HttpServletRequest request,
					 HttpServletResponse response) throws Exception{
				 try{
					// log.info("Inside ChequeSearchAction class doUploadMemDetails method");
					 setLinks(request);
					 String strNotify = null;
					 FileOutputStream outputStream = null;
						DynaActionForm frmSwPricing=(DynaActionForm)request.getSession().getAttribute("frmSwPricing");
						long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
						String pricingNumberAlert=  (String)request.getSession().getAttribute("pricingNumberAlert");

					 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject(); 

					 Object[] excelData=null;
					 DynaActionForm frmSwIncomeProfile=(DynaActionForm)form;
					 FormFile formFile = null;
					
					 formFile = (FormFile)frmSwIncomeProfile.get("stmFile");
					
					 if(formFile==null||formFile.getFileSize()==0 || formFile.equals(""))
					 {
						 // strNotify="Please select the xls or xlsx file";
						 strNotify="Please select the .XL or .XLS file.";
						// request.getSession().setAttribute("notifyerror", strNotify);
						 request.setAttribute("notifyerror", strNotify);
					 }
					 else
					 {
						 String[] arr=formFile.getFileName().split("[.]");
						 String fileType=arr[arr.length-1];
						 //if(!("xls".equalsIgnoreCase(fileType)||"xlsx".equalsIgnoreCase(fileType)))
						 if(!("xl".equalsIgnoreCase(fileType)||"xls".equalsIgnoreCase(fileType)))
							 
						 {
							// strNotify="File Type should be xls or xlsx";
//							 strNotify="FILE TYPE SHOULD BE .XL OR .XLS .";
							 strNotify="Please note the upload option here only supports .xls files";
							 request.setAttribute("notifyerror", strNotify);
							// request.getSession().setAttribute("notifyerror", strNotify);  
						 }
					 }
					 if(formFile.getFileSize()>(1024*1024*1024)) 
					 {
						 strNotify="File Length Lessthan 3GB";	        	  
						// request.getSession().setAttribute("notifyerror", strNotify);
						 request.setAttribute("notifyerror", strNotify);
					 }
					 
					 log.info("strNotify:"+strNotify);
					 
					
					 
					 if(strNotify!=null && strNotify.length()!=0)
					 {
						// return (mapping.findForward(strIncomeprofile));
						 return this.getForward(strIncomeprofile, mapping, request);
					 }
					 else
						{
//						 String[] sheet1Data=getSheet1Data(formFile);
							String[] arr=formFile.getFileName().split("[.]");
							String fileType=arr[arr.length-1];
							
							excelData=this.getExcelData(request,formFile,fileType,8);
							String settlementNo=(String) excelData[0];
							String errorMessage=(String) excelData[2];
							String sheet1DateData=null;
							if(excelData[3]!=null)
								sheet1DateData=(String) excelData[3];
							log.info("sheet1DateData::"+sheet1DateData);
							if("0".equals(settlementNo) && ("".equals(errorMessage)&&errorMessage==null)){
								strNotify="Please check the file, we are reading data from sheet-2";	        	  
								// request.getSession().setAttribute("notifyerror", strNotify);
								 request.setAttribute("notifyerror", strNotify);
							}
							else if(!"".equals(errorMessage)&&errorMessage!=null){
								 request.setAttribute("notifyerror", errorMessage);
							}
							else{
								String batchNo=convertExcelDataIntoXml(request,excelData,formFile,fileType,frmSwPricing.get("groupProfileSeqID").toString());
								 // System.out.println("batchNo-------"+batchNo);
								  /*Backup for audit purposes */
								String fileName=batchNo+"."+fileType;
								  String backupPath=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("QatarXmlPricingDir"))+fileName;
								  outputStream = new FileOutputStream(new File(backupPath));
								  outputStream.write(formFile.getFileData());//Excel file Upload backUp
								  outputStream.close();
								  String xmlFilePath=TTKPropertiesReader.getPropertyValue("QatarXmlPricingDir")+batchNo+".xml";
								  
			          	    	  File file2=new File(xmlFilePath);
			          	    	  FileReader fileReader=new FileReader(file2);
			          	    	 // System.out.println("xmlFilePath--"+xmlFilePath);

			          	    	ArrayList inputData=new ArrayList();
			          	    	inputData.add(batchNo);
			          	    	inputData.add(fileReader);
			          	    	inputData.add((int)file2.length());
			          	    	inputData.add(new Long(frmSwPricing.get("groupProfileSeqID").toString()));
			          	    	inputData.add(TTKCommon.getUserSeqId(request));
			          	    	inputData.add(fileName);
			          	    	String pricingmemUpload= insuranceObject.PricingUploadExcel(inputData);
			          	    	
			          	   if(pricingmemUpload == null){
			          	    request.setAttribute("successMsg","File uploaded successfully");  
			          	    }else{
			          	    	 request.setAttribute("errorMsg",pricingmemUpload);  
			          	    }
			     		  
							  // for fetch using esisting qatar Code // not copied from Dubai
				          	  // System.out.println("group_seq_id-In upload-"+group_seq_id);
				          	 ArrayList alprofileIncomeList= insuranceObject.getBenefitvalueAfter(group_seq_id);

				 			/*benefit code screen2*/
				 		
				 			frmSwIncomeProfile.set("profileBenefitList",alprofileIncomeList.get(0)); 
				 			frmSwIncomeProfile.set("profileNationalityList",alprofileIncomeList.get(1)); 
				 			frmSwIncomeProfile.set("sumTotalLives",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLives());  
				 			frmSwIncomeProfile.set("sumTotalLivesMaternity",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesMaternity());  
				 			frmSwIncomeProfile.set("sumTotalLivesOptical",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesOptical());  
				 			frmSwIncomeProfile.set("sumTotalLivesDental",((InsPricingVO)(alprofileIncomeList.get(2))).getSumTotalLivesDental());  
				 			frmSwIncomeProfile.set("sumNationalityLives",((InsPricingVO)(alprofileIncomeList.get(2))).getSumNationalityLives());  
				 			
				 			
				 			request.getSession().setAttribute("GroupProfileSeqID",group_seq_id);
							frmSwIncomeProfile.set("pricingNumberAlert", pricingNumberAlert);

				 		/*	System.out.println("total lives-----"+(String)request.getSession().getAttribute("totalNoOfLives"));
				 			System.out.println("total lives-----"+(String)request.getSession().getAttribute("TotalMaternityLives"));*/

//				 			frmSwIncomeProfile.set("totalNoOfLives",(String)request.getSession().getAttribute("totalNoOfLives"));
//				 			frmSwIncomeProfile.set("TotalMaternityLives",(String)request.getSession().getAttribute("TotalMaternityLives"));
							frmSwIncomeProfile.set("totalNoOfLives",((InsPricingVO)alprofileIncomeList.get(3)).getTotalCovedLives());
							frmSwIncomeProfile.set("TotalMaternityLives",((InsPricingVO)alprofileIncomeList.get(3)).getTotalLivesMaternity());
							request.getSession().setAttribute("frmSwIncomeProfile",frmSwIncomeProfile); 
							}
										
		 			

		 			//return doSearch(mapping ,form, request, response);
		 				return this.getForward(strIncomeprofile, mapping, request);
		 		
						}//notify else close
				 }//end of try
				 catch(TTKException expTTK)
				 {
					 return this.processExceptions(request, mapping, expTTK);
				 }//end of catch(TTKException expTTK)
				 catch(Exception exp)
				 {
					 return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
				 }//end of catch(Exception exp)
			 }//
  
			

			private Object[] getExcelData(HttpServletRequest request,FormFile formFile, String fileType, int column) throws FileNotFoundException, IOException {
				// TODO Auto-generated method stub
				InputStream fis=null;
		    	HSSFSheet sheet = null;// i; // sheet can be used as common for XSSF and HSSF WorkBook
		    	Reader reader		=	null;
		    	Object object[]=new Object[4];
		    	String sheet1DateData=null;
		    	int numclaimsettlementnumber=0;
		 	    ArrayList<ArrayList<String>> excelDatar=new ArrayList<>();
				FileWriter fileWriter=	null;
				HSSFWorkbook workbook = null;
				String errorMessage=null;
				
				fis = formFile.getInputStream(); 
				workbook =  (HSSFWorkbook) new HSSFWorkbook(fis);
				int shettCount=workbook.getNumberOfSheets();
				
		    	  //log("xls="+wb_hssf.getSheetName(0));
				if(shettCount>=2){
					sheet = workbook.getSheetAt(1);
					if(sheet==null){
		 	        	errorMessage="Please upload proper File";
		 	        	request.getSession().setAttribute("notify", "Please upload proper File");
		 	        }else{
						//Initializing the XML document
//		 	        	HSSFSheet sheet1 = workbook.getSheetAt(1);
		 	        	Iterator<?> rows1     = sheet.rowIterator ();
		 	        	int rowNumber=0;
		      	        while (rows1.hasNext ()) 
		      	        {
		      	        HSSFRow row1 =  (HSSFRow) rows1.next(); 
//				      	    Iterator<?> cells = row1.cellIterator (); 
				      	    for(short i=0;i<2;i++)
		      	            {
		      	            	HSSFCell cell1	=	row1.getCell(i);
		      	            	if(cell1!=null && cell1.getCellType ()==HSSFCell.CELL_TYPE_NUMERIC){
		      	            		if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell1))
 		     	                     {
		      	            			sheet1DateData=new SimpleDateFormat("dd/MM/YYYY").format(cell1.getDateCellValue());
		      	            			break;
 		     	                     }
		      	            	}	
		      	            }
				      	  rowNumber++;
				      	  if(rowNumber>2)
				      		  break;
		      	        }
		 	        	
		 	        	
		 	        	
		 	        	if(sheet1DateData!=null){
		 	        		final Pattern REGEX_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
			 	        	Iterator<?> rows     = sheet.rowIterator ();
			      	        while (rows.hasNext ()) 
			      	        {
			      	        HSSFRow row =  (HSSFRow) rows.next(); 
			      	        int columnCount=sheet.getRow(3).getLastCellNum();
				 	    	if(columnCount!=9){
				 	    		errorMessage="Please upload proper File";
				 	        	request.getSession().setAttribute("notify", "Please upload proper File");
							}else{
								if(row.getRowNum()<4)
			      	            	continue;
			      	          
//			      	            Iterator<?> cells = row.cellIterator (); 
			      	            ArrayList<String> rowData = new ArrayList<String>();
			      	            for(short i=0;i<10;i++)
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
			  		     	                    if(HSSFDateUtil.isCellDateFormatted((HSSFCell) cell) && i==3)
			  		     	                     {
			  		     	               
			  		     	                    	rowData.add(new SimpleDateFormat("dd/MM/YYYY").format(cell.getDateCellValue()));
			  		     	           
			  		     	                    }
			  		     	                    else 
			  		     	                    {
			  		     	                    	double numData=cell.getNumericCellValue();
			  		     	                    	if(i==3){
			  		     	                    		String dateDate="";
			  		     	                    		try{
			  		     	                    			dateDate=getFormattedDateData(numData);	
			  		     	                    		}catch(Exception exception){
			  		     	                    			dateDate="";
			  		     	                    		}
			  		     	                    		rowData.add(dateDate);
			  		     	                    	}
			  		     	                    	else {
			  		     	                    		rowData.add(numData+"");	
			  		     	                    	}
			  		     	                    }
			  		     	                    break;
			  		     	                }
			  		     	                case HSSFCell.CELL_TYPE_STRING :
			  		     	                {
			  		     	                	String richTextString = cell.getStringCellValue().trim();
			  		     	                    /*if(i==2){
			  		     	                    	if(!"female".equalsIgnoreCase(richTextString)&&!"male".equalsIgnoreCase(richTextString)){
			  		     	                    		errorMessage="Gender should be either Male or Female.";
			  		     	                    		break;
			  		     	                    	}
			  		     	                    }*/
			  		     	                 /*if(i==4)
			  		     	                    {
			  		     	                	boolean validate=richTextString.matches("^\\d+$");
			  		     	                	if(!validate){
			  		     	                		errorMessage="Age should be number";
		  		     	                    		break;
			  		     	                	}
			  		     	                    }*/
			  		     	                 
			  		     	                    /*if(i==5)
			  		     	                    {
			  		     	                    	if(!"Unmarried".equalsIgnoreCase(richTextString)&&!"Married".equalsIgnoreCase(richTextString)){
			  		     	                    		errorMessage="Marital Status should be either Unmarried or Married.";
			  		     	                    		break;
			  		     	                    	}
			  		     	                    }*/
			  		     	                    /*else if(i==7){
			  		     	                    	if(!"No".equalsIgnoreCase(richTextString)&&!"Yes".equalsIgnoreCase(richTextString)){
			  		     	                    		errorMessage="Maternity Eligible should be either No or Yes.";
			  		     	                    		break;
			  		     	                    	}
			  		     	                    }*/
			  		     	                	richTextString	=	REGEX_PATTERN.matcher(richTextString).replaceAll("").trim();
			  		     	                    rowData.add(richTextString);
			  		     	                    break;
			  		     	                }
			  		     	             case HSSFCell.CELL_TYPE_FORMULA :
			  		     	             {
			  		     	            	
			  		     	            	 if(i==4){
			  		     	            		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			  		     	            		rowData.add(cell.getNumericCellValue()+"");
			  		     	            	 }else{
			  		     	            		rowData.add(cell.getCellFormula());
			  		     	            	 }
			  		     	            	break;
			  		     	             }
			  		     	                case HSSFCell.CELL_TYPE_BLANK :
			  		     	                {	//HSSFRichTextString blankCell	=	cell.get.getRichStringCellValue();
			  		     	                	String blankCell	=	cell.getStringCellValue().trim().replaceAll("[^\\x00-\\x7F]", "");
			  		     	                	rowData.add(blankCell);
			  		     	                	break;
			  		     	                }
			  		     	                default:
			  		     	                {
			  		     	                    // types other than String and Numeric.
			  		     	                    System.out.println ("Type not supported.");
			  		     	                    rowData.add("");
			  		     	                    break;
			  		     	                }
			  	     	                } // end switch
			  	            		}//else
			      	            	
			      	            }//for
			      	          
			      	          excelDatar.add(rowData);//adding Excel data to ArrayList
			      	        numclaimsettlementnumber++; 
			      	        } //end while
			      	     
			 	        }
		 	        	}else{
		 	        		errorMessage="\"As on date\" is mandatory, Please enter valid date.";
		 	        	}
		 	        	}
				}else{
					errorMessage="Please check the file, we are reading data from sheet-2";
					request.getSession().setAttribute("notify", "Please check the file, we are reading data from sheet-2");
				}
		 	        object[0]=numclaimsettlementnumber+"";//adding no. of policies
					object[1]=excelDatar;//adding all rows dataO
					object[2]=errorMessage;
					object[3]=sheet1DateData;
        		    return object;
			}
			
			
			
			private String convertExcelDataIntoXml(HttpServletRequest request,Object[] objects,FormFile formFile,String fileType,String pricingSeqid) throws JAXBException {
				
					 
				    	String noOfPolicies=(String)objects[0];
						ArrayList<ArrayList<String>> excelDataRows=(ArrayList<ArrayList<String>>)objects[1];
				    	String uploadType="PRI";
	                    Long uploadedBy1=(Long) TTKCommon.getUserSeqId(request);
	                    String uploadedBy = uploadedBy1.toString();
				    	
				        String batchNo=uploadType+"-"+pricingSeqid+"-"+ new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date());//policyType+"-"+ new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+"-"+vidalOfficeName+"-"+abrCode+"-"+uploadType+"-"+groupId;
				       
				       String policyFileName =batchNo+"."+fileType;  
				        
				       
				        //prepare the marshaling
				    
				        Batch batch=new Batch();
				      JAXBContext contextObj = JAXBContext.newInstance(Batch.class); 
						Marshaller marshallerObj = contextObj.createMarshaller(); 
						marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					  //  FileDetail fileDetail=new FileDetail(member_Name,gender,dob,age,matCovered,salary,country_code,policy_number,member_id,relationship) 
					 //  batch.setFileDetail(fileDetail);
				       
					 //write the data into xml file				    
			    		Iterator<ArrayList<String>>rit=excelDataRows.iterator();
			    	
			    		
			    		ArrayList<MemberDetails> memberAl=new ArrayList<>();
			    		
				    		while(rit.hasNext()){
				    			 ArrayList<String> rlist=rit.next();
				    			 MemberDetails memberDetails=new MemberDetails();
				    			 memberDetails.setUploadstatus("Y");
				    			 MemberData memberData=new MemberData();		    			 
		/*System.out.println("memname 0--------"+rlist.get(0));
		System.out.println("memname 1--------"+rlist.get(1));
		System.out.println("memname 2--------"+rlist.get(2));
		System.out.println("memname 3--------"+rlist.get(3));
		System.out.println("memname 4--------"+rlist.get(4));
		System.out.println("memname 5--------"+rlist.get(5));
		System.out.println("memname 6--------"+rlist.get(6));
		System.out.println("memname 7--------"+rlist.get(7));*/


				    			 memberData.setAlkootId(rlist.get(0));
				    			 memberData.setMember_Name(rlist.get(1)); 
				    			 memberData.setGender(rlist.get(2)); 
				    			 memberData.setDob(rlist.get(3)); 
				    			 memberData.setAge(rlist.get(4)); 
				    			 memberData.setMatCovered(rlist.get(5)); 
				    			 memberData.setRelationship(rlist.get(6));
				    			 memberData.setIsMaternityEligible(rlist.get(7)); 
				    			 memberData.setCountry_code(rlist.get(8));
//				    			 memberData.setSalary("");  
				    			// memberData.setPolicy_number(rlist.get(7));  
				    			// memberData.setMember_id(rlist.get(8));
				    			
				    			 
				    			 memberDetails.setMemberData(memberData);
				    			 
				    			 memberAl.add(memberDetails);		    		   		   
				    			}//while		    		
				    	
				    		 batch.setMemberDetails(memberAl); 		    			  
			    			  
			    			  File xmlPath=new File(TTKPropertiesReader.getPropertyValue("QatarXmlPricingDir"));
			    			  if(!xmlPath.exists())xmlPath.mkdirs();
			    			  String xmlFilePath=TTKPropertiesReader.getPropertyValue("QatarXmlPricingDir")+batchNo+".xml";  
			    			  marshallerObj.marshal(batch,new File(xmlFilePath));		    			  
			    		
	                                return batchNo; 
	                                
			}    
			
			
			 public ActionForward fetchScreen1(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			    		HttpServletResponse response) throws Exception{
			    try{
			    	
			    setLinks(request);

			    String renewalYN =request.getParameter("renewalYN");
			    String PricingRefno=request.getParameter("pricingRefno");
			    String clientcodeId =request.getParameter("clientcodeId");
			    String clientName ="";
			 //   StringBuffer clientNameb = new StringBuffer();
			    String clientNameA =request.getParameter("clientNameArray");
			 //   log.info("clientNameA:"+clientNameA);
			    if(clientNameA.equals("NA")){
			    	  
			    	  clientName =request.getParameter("clientName");
			    }else{
			    	
			    	clientName =clientNameA.replace(",","&");
			    }
			  
			 //   log.info("clientName:"+clientName);
			    String pastPolicyNumber =request.getParameter("previousPolicyNo");
			   
			    String coverStartDate =request.getParameter("coverStartDate");
			    String coverEndDate =request.getParameter("coverEndDate");
			    String totalCovedLives =request.getParameter("totalCovedLives");
		/*	  log.info("clientName:"+clientName);
			  log.info("pastPolicyNumber:"+pastPolicyNumber);
			  log.info("clientName:"+clientName);*/
			    
			    DynaActionForm frmSwPricing= (DynaActionForm)form;
			    
			
			  
			     
			   InsPricingVO insPricingVO=new InsPricingVO();
			   insPricingVO.setPreviousPolicyNo(pastPolicyNumber);
			   insPricingVO.setGroupProfileSeqID( (long)request.getSession().getAttribute("GroupProfileSeqID"));//bk
			   insPricingVO.setRenewalYN(renewalYN);
			    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			    InsPricingVO dbinsPricingVO=null;
			    dbinsPricingVO= insuranceObject.swFetchScreen1(insPricingVO);
			    String policyNumber = frmSwPricing.getString("previousPolicyNo");
			    ArrayList dataList=new ArrayList();
//	            String clientName = frmSwPricing.getString("clientName");
	            String clientCode= frmSwPricing.getString("clientCode");
	            dataList.add(policyNumber);
	            dataList.add(clientName);
	            dataList.add(clientCode);
	           InsPricingVO insPricingVOPrev   = insuranceObject.getPolicyStatusInfo(dataList);
	            
			    ArrayList alPricingPolicyList= null;
	        	ProductPolicyManager prodPolicyManager = this.getProductPolicyManagerObject();
	        	alPricingPolicyList = prodPolicyManager.getPreviousPolicy(clientName);
	        	if(alPricingPolicyList==null){
					  alPricingPolicyList=new ArrayList();
		        }//end of if(alCityList==null) 
	        	String polCategory=frmSwPricing.getString("policycategory");
			    if(dbinsPricingVO.getNotifyerror()!=null&&!dbinsPricingVO.getNotifyerror().equals("")){
			    	if("N".equals(renewalYN)){
			    		 insPricingVO=new InsPricingVO();
			    		 insPricingVO.setPricingRefno(PricingRefno);
			    		 insPricingVO.setClientCode(clientcodeId);
			    		 insPricingVO.setClientName(clientName);
			    		 insPricingVO.setPreviousPolicyNo(frmSwPricing.getString("previousPolicyNo"));
			    		 insPricingVO.setPolicycategory(polCategory);
//			    		 insPricingVO.setCoverStartDate(insPricingVOPrev.getCoverStartDate());
//			    		 insPricingVO.setCoverEndDate(insPricingVOPrev.getCoverEndDate());
			    		 insPricingVO.setTotalCovedLives(totalCovedLives);
			             frmSwPricing.initialize(mapping);
			             insPricingVO.setTrendFactor("6");
			             frmSwPricing= (DynaActionForm)FormUtils.setFormValues("frmSwPricing", insPricingVO,this, mapping, request);
			             frmSwPricing.set("renewalYN","N");
			             frmSwPricing.set("policycategory",polCategory);
			             frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
			             request.getSession().setAttribute("frmSwPricing",frmSwPricing);
			    	}else{
//			    	request.setAttribute("fetchErrorData", dbinsPricingVO.getNotifyerror());			    	
			    	request.setAttribute("errorMsg",dbinsPricingVO.getNotifyerror()); 
			    	}
			    }else{
			    	dbinsPricingVO.setRenewalYN(renewalYN);
				    dbinsPricingVO.setPricingRefno(PricingRefno);//BK
				    dbinsPricingVO.setClientCode(clientcodeId);
				    dbinsPricingVO.setClientName(clientName);
				    dbinsPricingVO.setPreviousPolicyNo(pastPolicyNumber);
				    dbinsPricingVO.setCoverStartDate(insPricingVOPrev.getCoverStartDate());
				    dbinsPricingVO.setCoverEndDate(insPricingVOPrev.getCoverEndDate());
				    dbinsPricingVO.setTotalCovedLives(totalCovedLives);
				    dbinsPricingVO.setTrendFactor("6");
			//	System.out.println("action -----"+dbinsPricingVO.getMaternityYN());
				
				
				   
				  //  frmSwPricing.set("pricingRefno", dbinsPricingVO.getPricingRefno());
				    
					frmSwPricing = (DynaActionForm)FormUtils.setFormValues("frmSwPricing",dbinsPricingVO,this,mapping,request);
					
						
					 frmSwPricing.set("alpreviousPolicyNo",alPricingPolicyList);
					 frmSwPricing.set("policycategory",polCategory);
					
					 request.setAttribute("fetchData","Y");

					 request.getSession().setAttribute("networkType", frmSwPricing.get("networkList"));
					request.getSession().setAttribute("frmSwPricing",frmSwPricing);
			    }
			    
				return this.getForward(strGroupprofile, mapping, request);
			    
			    
			    }//end of try
			    catch(TTKException expTTK)
			    {
			    return this.processExceptions(request, mapping, expTTK);
			    }//end of catch(TTKException expTTK)
			    catch(Exception exp)
			    {
			    return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
			    }//end of catch(Exception exp)
			    }//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			
			 
			 public ActionForward getPolicyInfo(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			    		HttpServletResponse response) throws Exception{
			    	try{
			    		setLinks(request);
			    		DynaActionForm frmSwPricing= (DynaActionForm)form;
			    		InsPricingVO insPricingVO=null;
			    		InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			    		ArrayList dataList=new ArrayList();
			    		
			            String policyNumber = frmSwPricing.getString("previousPolicyNo");
			            String clientName = frmSwPricing.getString("clientName");
			            String clientCode= frmSwPricing.getString("clientCode");
			            String renewalYN= frmSwPricing.getString("renewalYN");
			            String pricingRefno= frmSwPricing.getString("pricingRefno");
			            String policycategory= frmSwPricing.getString("policycategory");
			            String groupProfileSeqID= frmSwPricing.getString("groupProfileSeqID");
			            String trendFactor= frmSwPricing.getString("trendFactor");
			            ArrayList alPricingPolicyList= (ArrayList)frmSwPricing.get("alpreviousPolicyNo");
			            dataList.add(policyNumber);
			            dataList.add(clientName);
			            dataList.add(clientCode);
			            frmSwPricing.initialize(mapping);
			            insPricingVO   = insuranceObject.getPolicyStatusInfo(dataList); 
			            /*if(insPricingVO.getAlertmsgscreen1()!=null&&!insPricingVO.getAlertmsgscreen1().equals("")){
			            	request.setAttribute("errorMsg", insPricingVO.getAlertmsgscreen1()); 	
			            }*/
			            /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
			            if(insPricingVO.getCoverStartDate()!=null){
			            String coverStartDate = formatter.format(insPricingVO.getCoverStartDate());  
			            frmSwPricing.set("coverStartDate", coverStartDate);
			            }if(insPricingVO.getCoverEndDate()!=null){
			            	String coverEndDate = formatter.format(insPricingVO.getCoverEndDate());  			           
				            frmSwPricing.set("coverEndDate", coverEndDate);
			            }*/
			            frmSwPricing.set("previousPolicyNo", policyNumber);
			            frmSwPricing.set("clientName", clientName);
			            frmSwPricing.set("clientCode", clientCode);
			            frmSwPricing.set("renewalYN", renewalYN);
			            frmSwPricing.set("pricingRefno", pricingRefno);
			            frmSwPricing.set("policycategory", policycategory);
			            frmSwPricing.set("groupProfileSeqID", groupProfileSeqID);
			            frmSwPricing.set("alpreviousPolicyNo", alPricingPolicyList);
			            frmSwPricing.set("trendFactor", trendFactor);
			            frmSwPricing.set("coverStartDate", "");
			            frmSwPricing.set("coverEndDate", "");
			            frmSwPricing.set("alertmsgscreen1", insPricingVO.getAlertmsgscreen1());
			            request.getSession().setAttribute("numberOfLives", insPricingVO.getNumberOfLives());
			            frmSwPricing.set("policycategory", insPricingVO.getPolicycategory());
			    		request.setAttribute("policyFetchFlag",insPricingVO.getPolicyNumberFlag());
			    		request.getSession().setAttribute("frmSwPricing",frmSwPricing);
						return mapping.findForward(strGroupprofile);
			    	}//end of try
			    	catch(TTKException expTTK)
					{
						return this.processExceptions(request, mapping, expTTK);
					}//end of catch(TTKException expTTK)
					catch(Exception exp)
					{
						return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
					}//end of catch(Exception exp)
			    }
			 
		 /**
     * Returns the InsuranceManager session object for invoking methods on it.
     * @return InsuranceManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private InsuranceManager getInsuranceManagerObject() throws TTKException
	{
		InsuranceManager insuremanager = null;
		try
		{
			if(insuremanager == null)
			{
				InitialContext ctx = new InitialContext();
				insuremanager = (InsuranceManager) ctx.lookup("java:global/TTKServices/business.ejb3/InsuranceManagerBean!com.ttk.business.empanelment.InsuranceManager");
			}//end if
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strInsError);
		}//end of catch
		return insuremanager;
	} // end of private InsuranceManager getInsManagerObject() throws TTKException
    
    private ProductPolicyManager getProductPolicyManagerObject() throws TTKException
	{
		ProductPolicyManager productPolicyManager = null;
		try
		{
			if(productPolicyManager == null)
			{
				InitialContext ctx = new InitialContext();
				productPolicyManager = (ProductPolicyManager) ctx.lookup("java:global/TTKServices/business.ejb3/ProductPolicyManagerBean!com.ttk.business.administration.ProductPolicyManager");
			}//end of if(productPolicyManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, strInsError);
		}//end of catch
		return productPolicyManager;
	}//end getProductPolicyManagerObject()
    
    private String getFormattedDateData(double numericCellValue) {
		int myDouble = (int) numericCellValue;
		GregorianCalendar gc = new GregorianCalendar(1900, Calendar.JANUARY, 1);
        gc.add(Calendar.DATE, myDouble-2);
        Date result = gc.getTime();
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		String outputString = outputFormat.format(result);
		return outputString;
	}
    
    
    public ActionForward checkPolicyStatus(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		DynaActionForm frmSwPricing= (DynaActionForm)form;
    		frmSwPricing.set("renewalYN", request.getParameter("RevewalYN"));
    		request.getSession().setAttribute("frmSwPricing",frmSwPricing);
			return mapping.findForward(strGroupprofile);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    
    public ActionForward doViewFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		DynaActionForm frmSessionSwPricing=(DynaActionForm)request.getSession().getAttribute("frmSwPricing");
			InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			ArrayList inputData=new ArrayList();
			inputData.add(frmSessionSwPricing.get("groupProfileSeqID").toString());
			ArrayList outputData=insuranceObject.getViewFiles(inputData);
			String fileName =	TTKPropertiesReader.getPropertyValue("QatarXmlPricingDir");
			if(outputData.size()!=0)
			fileName=fileName+(String)outputData.get(0);
			File file = new File(fileName);
			if(file.isFile() && file.exists()){
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename="+(String)outputData.get(0));
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				int ch;
				while ((ch = bis.read()) != -1) baos.write(ch);
				ServletOutputStream sos = response.getOutputStream();
				baos.writeTo(sos);  
				baos.flush();   
				sos.flush(); 
				bis.close();	
			}else{
				request.setAttribute("notifyerror", "File is not available, please upload.");
			}
			return mapping.findForward(strIncomeprofile);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		}//end of catch(Exception exp)
    }
    
    
} //end of InsPricingAction