

//Bajaj Change request 1274A

/**
* @ (#) InsureApproveConfiguration.java Jun 23, 2011
* Project       : TTK HealthCare Services
* File          : InsureApproveConfiguration.java
* Author        : Satya Moganti
* Company       : RCS 
* Date Created  : Jun 23, 2011
*
* @author       :  Satya 
* Modified date :  
* Reason        :  Bajaj Insurance Approve Configuartion
*/


/**
* 
*/

package com.ttk.action.insurancepricing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.ttk.action.TTKAction;
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.business.empanelment.InsuranceManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.insurancepricing.InsPricingVO;
import com.ttk.dto.insurancepricing.PolicyConfigVO;
import com.ttk.dto.insurancepricing.SwPolicyConfigVO;
import com.ttk.dto.insurancepricing.SwPricingSummaryVO;

import formdef.plugin.util.FormUtils;


public class SwPlanDesignConfiguration extends TTKAction  {
	
	//private static final Logger log = Logger.getLogger( PlanDesignConfiguration.class );
	
	 
	private static final String strInsuranceConfig="insuranceconfig";
	private static final String strGenerateQuote="generatequote";
	private static final String strProductClose="productclose";
	private static final String strPolicyClose="policyclose";
	private static final String strGenerateConfig="generateConfig";
	private static final String strInsError="insurance";
	private static final String strPremiumConfig="premiumconfig";
	private static final String strGrossPremium="grosspremium";
	
	 private static final String strReportdisplay="reportdisplay";
 	 private static final String strReportExp="report";
	
	/**
	 * This method is used to forward to configuration screen.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward  doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
			{
				try
				{
					setLinks(request);
					InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
					DynaActionForm frmSwPolicyConfig = (DynaActionForm)form;
					ArrayList  alplanDesignList = new ArrayList();
					ArrayList  alLoadDesignList = new ArrayList();
					ArrayList aldemographicDataList = new ArrayList();
					InsPricingVO insPricingVO = new InsPricingVO();
					boolean durationcheck=true;
					String addedDate=null;
					SwPolicyConfigVO swPolicyConfigVO =  new SwPolicyConfigVO();
					String pricingNumberAlert = "";
					 
					long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
					swPolicyConfigVO.setLngGroupProfileSeqID(GroupProfileSeqID);
					String clientCode = (String)request.getSession().getAttribute("ClientCode");
					String completeSaveYN = (String)request.getSession().getAttribute("completeSaveYN");

					if("Y".equalsIgnoreCase(request.getParameter("proceedbutton"))){
					 pricingNumberAlert=  (String)request.getSession().getAttribute("pricingNumberAlert");
					}

					
					  insPricingVO  =  insuranceObject.getfalgPricingvalue(GroupProfileSeqID);
					  addedDate=insPricingVO.getRiskPremiumDate();
					  String calCPM_FlagYN = insPricingVO.getCalCPMFlagYN();
					  String loadingFlagYN = insPricingVO.getLoadingFlagYN();
					  String demographicDataFlag = insPricingVO.getDemographicflagYN();
					  
					 
					  insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
					  insPricingVO.setClientCode(clientCode);
					  insPricingVO.setGroupProfileSeqID(GroupProfileSeqID);
					  swPolicyConfigVO.setLngGroupProfileSeqID(GroupProfileSeqID);
					  /*demographic code*/
					  if(insPricingVO.getCompleteSaveYNInSc2().equalsIgnoreCase("Y"))
					  aldemographicDataList= insuranceObject.getdemographicData(insPricingVO,demographicDataFlag);
					  
					  
				if(GroupProfileSeqID > 0  && completeSaveYN.equalsIgnoreCase("Y") && insPricingVO.getCompleteSaveYNInSc2().equalsIgnoreCase("Y"))
						{
					if(calCPM_FlagYN.equalsIgnoreCase("Y")){
					alplanDesignList= insuranceObject.getcpmAfterCalcultion(insPricingVO);
					frmSwPolicyConfig.set("calCPM_FlagYN", calCPM_FlagYN);
				

					}else{
					alplanDesignList= insuranceObject.getcpmBeforeCalcultion(insPricingVO);//to get CPM Data
					
					}
					if(loadingFlagYN.equalsIgnoreCase("Y"))
					{
						alLoadDesignList= insuranceObject.getAfterLoadingData(insPricingVO);	
					}else{
						alLoadDesignList= insuranceObject.getBeforeLoadingData(insPricingVO);//to get Loading data	
					}
						frmSwPolicyConfig.set("Message","N"); 
						}
						else
						{
							
							frmSwPolicyConfig.set("Message",request.getParameter("Message")); 
							frmSwPolicyConfig.set("Message","Y"); 
							if(completeSaveYN == null){
						    	TTKException expTTK = new TTKException();
								expTTK.setMessage("error.pricing.required");
								throw expTTK;
								}else if(completeSaveYN.equalsIgnoreCase("N")){
									TTKException expTTK = new TTKException();
									expTTK.setMessage("error.pricing.complete.screen");
									throw expTTK;
								}
							if(insPricingVO.getCompleteSaveYNInSc2().equalsIgnoreCase("N") || insPricingVO.getCompleteSaveYNInSc2().equalsIgnoreCase("")){
								TTKException expTTK = new TTKException();
								expTTK.setMessage("error.pricing.complete.screen-2");
								throw expTTK;
							}
							
						}
						
						if(GroupProfileSeqID > 0)
						{
							frmSwPolicyConfig.set("Message","N"); 
							
						}
						
						SwPolicyConfigVO  swPolicyConfigVOfinalDesign= insuranceObject.getcpmAfterLoading(insPricingVO);// final Data so no Flag condition required
						if(swPolicyConfigVOfinalDesign != null){
						frmSwPolicyConfig= (DynaActionForm)FormUtils.setFormValues("frmSwPolicyConfig",	swPolicyConfigVOfinalDesign, this, mapping, request);
						}
				//	frmSwPolicyConfig.set("loadComments", (((SwPolicyConfigVO)alLoadDesignList.get(0)).getLoadComments()));
					
						for(Object swPolicyConfig:alplanDesignList){
							SwPolicyConfigVO swPolicyConfigVO2=(SwPolicyConfigVO) swPolicyConfig;
							//System.out.println("maternityCPM::"+swPolicyConfigVO2.getMaternityCPM());
							Date coverageStartDate=swPolicyConfigVO2.getPolicyEffDate();
							Date coverageEndDate=swPolicyConfigVO2.getPolicyExpDate();
							if(coverageEndDate!=null && coverageStartDate!=null){
							durationcheck=getDiffYears(coverageStartDate,coverageEndDate);
							//System.out.println("coverageStartDate::"+coverageStartDate+"coverageEndDate::"+coverageEndDate+"---durationcheck::"+durationcheck);
							if(durationcheck==false){	
								break;
							}
							}
						}
					frmSwPolicyConfig.set("alDemographicData", aldemographicDataList);
					frmSwPolicyConfig.set("alResultpastYear", alplanDesignList);
					frmSwPolicyConfig.set("addedDateForRiskPremium", addedDate);
					//frmSwPolicyConfig.set("alLoadingData", alLoadDesignList);
					frmSwPolicyConfig.set("alertMsg", (((SwPolicyConfigVO)alplanDesignList.get(0)).getAlertMsg()));
					frmSwPolicyConfig.set("pricingNumberAlert", pricingNumberAlert);
					request.getSession().setAttribute("GroupProfileSeqID", GroupProfileSeqID); 
					request.getSession().setAttribute("durationcheck", durationcheck); 
					request.getSession().setAttribute("frmSwPolicyConfig",frmSwPolicyConfig); 
					return this.getForward(strInsuranceConfig, mapping, request);
				}
				catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,"Configuration"));
				}//end of catch(Exception exp)
		
			}//end of  doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           // HttpServletResponse response)
	
	/**
	 * This method is used to forward to configuration screen.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward  doDefaultQuotation(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
			{
				try
				{
					setLinks(request);
					DynaActionForm frmSwPolicyConfigQuote = (DynaActionForm)form;
					long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
					String completeSaveYN = (String)request.getSession().getAttribute("completeSaveYN");
						
					
					InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
					InsPricingVO insPricingVO = new InsPricingVO();
					insPricingVO.setGroupProfileSeqID(GroupProfileSeqID);
					insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
					
					
					InsPricingVO insPricingVO2  =  insuranceObject.getfalgPricingvalue(GroupProfileSeqID);
					String pricingModifiedYN = insPricingVO2.getPricingmodifyYN();
					
					if(GroupProfileSeqID > 0  && completeSaveYN.equalsIgnoreCase("Y") && pricingModifiedYN.equals("N") )
					{
						SwPricingSummaryVO  SwPricingSummaryVO= insuranceObject.getcpmAfterLoadingPricing(insPricingVO);// FINAL CPM  Data so no Flag condition required
						if(SwPricingSummaryVO != null){
							frmSwPolicyConfigQuote= (DynaActionForm)FormUtils.setFormValues("frmSwPolicyConfigQuote",SwPricingSummaryVO, this, mapping, request);
						}
					frmSwPolicyConfigQuote.set("Message","N"); 
					}
					else
					{
						((DynaActionForm)form).initialize(mapping);
						frmSwPolicyConfigQuote.set("Message","Y"); 
						
						if(completeSaveYN == null){
					    	TTKException expTTK = new TTKException();
							expTTK.setMessage("error.pricing.required");
							throw expTTK;
							}else if(completeSaveYN.equalsIgnoreCase("N")){
								TTKException expTTK = new TTKException();
								expTTK.setMessage("error.pricing.complete.screen");
								throw expTTK;
							}
							else if(pricingModifiedYN.equals("Y")){
							TTKException expTTK = new TTKException();
							expTTK.setMessage("error.pricing.complete.screen.riskpremium");
							throw expTTK;
							}
						
						}
						
		            	
				
					//Long  lngProdPolicySeqId = TTKCommon.getWebBoardId(request);
					/*insuranceApproveVO = productpolicyObject.getConfigInsuranceApproveData(lngProdPolicySeqId);
					 
					frmConfigInsuranceApprove = (DynaActionForm)FormUtils.setFormValues("frmConfigInsuranceApprove",insuranceApproveVO,this,mapping,request);
					frmConfigInsuranceApprove.set("caption",buildCaption(request));*/ 
				
					request.getSession().setAttribute("frmSwPolicyConfigQuote",frmSwPolicyConfigQuote); 
				//	return this.getForward(strGenerateQuote, mapping, request);
					return mapping.findForward(strGenerateQuote);
				}
				catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,"Configuration"));
				}//end of catch(Exception exp)
		
			}//end of  doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           // HttpServletResponse response)
	
	
	
	public ActionForward  doDefaultGrossPremium(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
			{
				try
				{
					setLinks(request);
					DynaActionForm frmSwGrosspremium = (DynaActionForm)form;
					long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
					String completeSaveYN = (String)request.getSession().getAttribute("completeSaveYN");
					
					InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
					InsPricingVO insPricingVO = new InsPricingVO();
					ArrayList  alLoadingGrosspremiumList = new ArrayList();
					
					insPricingVO.setGroupProfileSeqID(GroupProfileSeqID);
					insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
					
					InsPricingVO insPricingVO2  =  insuranceObject.getfalgPricingvalue(GroupProfileSeqID);
					String loadingFlagYN = insPricingVO2.getLoadingFlagYN();
					 String calCPM_FlagYN = insPricingVO2.getCalCPMFlagYN();
					
					if(loadingFlagYN.equalsIgnoreCase("Y"))
					{
						alLoadingGrosspremiumList= insuranceObject.getAfterLoadingData(insPricingVO);	
					}else{
						alLoadingGrosspremiumList= insuranceObject.getBeforeLoadingData(insPricingVO);//to get Loading data	
					}
					
					if(GroupProfileSeqID > 0  && completeSaveYN.equalsIgnoreCase("Y") && calCPM_FlagYN.equalsIgnoreCase("Y"))
					{
						SwPricingSummaryVO  SwPricingSummaryVO= insuranceObject.getcpmAfterLoadingPricing(insPricingVO);// FINAL CPM  Data so no Flag condition required
						//  SwPricingSummaryVO.setDummyStringflag("Y");
						 // if(SwPricingSummaryVO != null){
							frmSwGrosspremium= (DynaActionForm)FormUtils.setFormValues("frmSwGrosspremium",SwPricingSummaryVO, this, mapping, request);
						//}
						frmSwGrosspremium.set("Message","N"); 
					}
					else
					{
						((DynaActionForm)form).initialize(mapping);
						frmSwGrosspremium.set("Message","Y"); 
						
						if(completeSaveYN == null){
					    	TTKException expTTK = new TTKException();
							expTTK.setMessage("error.pricing.required");
							throw expTTK;
							}else if(completeSaveYN.equalsIgnoreCase("N")){
								TTKException expTTK = new TTKException();
								expTTK.setMessage("error.pricing.complete.screen");
								throw expTTK;
							}
						if(calCPM_FlagYN.equals("N") || calCPM_FlagYN.equals("")){
							TTKException expTTK = new TTKException();
							expTTK.setMessage("error.pricing.complete.screen.riskpremium");
							throw expTTK;
						}
						
					}
					frmSwGrosspremium.set("loadComments", (((SwPricingSummaryVO)alLoadingGrosspremiumList.get(0)).getLoadComments()));
					frmSwGrosspremium.set("alLoadingGrosspremium", alLoadingGrosspremiumList);
					request.getSession().setAttribute("frmSwGrosspremium",frmSwGrosspremium); 
					return mapping.findForward(strGrossPremium);
				}
				catch(TTKException expTTK)
				{
					return this.processExceptions(request, mapping, expTTK);
				}//end of catch(TTKException expTTK)
				catch(Exception exp)
				{
					return this.processExceptions(request, mapping, new TTKException(exp,"Configuration"));
				}//end of catch(Exception exp)
		
			}//end of  doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	           // HttpServletResponse response)
	
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
	
	
	public ActionForward doSaveCalculate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException
	{
		try
		{
			log.debug("in doSaveCalculate");
			
			setLinks(request); 
			InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			String strForwardPath=null;
			DynaActionForm frmSwPolicyConfig = (DynaActionForm)form;
			SwPolicyConfigVO swpolicyConfigVO=null;
			InsPricingVO insPricingVO  = new InsPricingVO();
			ArrayList alLoadDesignList = new ArrayList();
			ArrayList<Object> alpastData =  (ArrayList<Object>) frmSwPolicyConfig.get("alResultpastYear");
			String pricingNumberAlert=  (String)request.getSession().getAttribute("pricingNumberAlert");

			////////////////////////// check below code
			
		//	String validation = calculateValidation(alpastData);//Validation Added for income profile Use for Inpatient and Out patient
			
		    ////////////////////////
		    
		    
			long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
			swpolicyConfigVO =(SwPolicyConfigVO)FormUtils.getFormValues(frmSwPolicyConfig, "frmSwPolicyConfig",this, mapping, request); 
		
			swpolicyConfigVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			swpolicyConfigVO.setLngGroupProfileSeqID(GroupProfileSeqID);
			insPricingVO.setGroupProfileSeqID(GroupProfileSeqID);
			insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			swpolicyConfigVO.setAlPastData(alpastData);
			
			ArrayList  iResultarray = insuranceObject.calculatePlanDesignConfig(swpolicyConfigVO);
			
			InsPricingVO insPricingVO2  =  insuranceObject.getfalgPricingvalue(GroupProfileSeqID);// address changing insPricingVO2
			 String loadingFlagYN = insPricingVO2.getLoadingFlagYN();
			 String demographicDataFlag = insPricingVO2.getDemographicflagYN();
			 ArrayList  aldemographicDataList= insuranceObject.getdemographicData(insPricingVO,demographicDataFlag);

			ArrayList  alplanDesignList= insuranceObject.getcpmAfterCalcultion(insPricingVO);
			
			
			if(loadingFlagYN.equalsIgnoreCase("Y"))
			{
				alLoadDesignList= insuranceObject.getAfterLoadingData(insPricingVO);	
			}else{
				alLoadDesignList= insuranceObject.getBeforeLoadingData(insPricingVO);//to get Loading data	
			}
			
			int iResult = (int) iResultarray.get(0);
			if (iResult > 0){
				request.setAttribute("updated","message.saved");
				frmSwPolicyConfig.set("Message","N"); 
			}
			SwPolicyConfigVO  swPolicyConfigVOfinalDesign= insuranceObject.getcpmAfterLoading(insPricingVO);// final Data so no Flag condition required
			if(swPolicyConfigVOfinalDesign != null){
			frmSwPolicyConfig= (DynaActionForm)FormUtils.setFormValues("frmSwPolicyConfig",	swPolicyConfigVOfinalDesign, this, mapping, request);
			}
			frmSwPolicyConfig.set("pricingNumberAlert", pricingNumberAlert);
		//	frmSwPolicyConfig.set("loadComments", (((SwPolicyConfigVO)alLoadDesignList.get(0)).getLoadComments()));
			//frmSwPolicyConfig.set("alLoadingData", alLoadDesignList);
			frmSwPolicyConfig.set("alResultpastYear", alplanDesignList);
			frmSwPolicyConfig.set("alDemographicData", aldemographicDataList);
			frmSwPolicyConfig.set("addedDateForRiskPremium", insPricingVO2.getRiskPremiumDate());
			
			request.getSession().setAttribute("frmSwPolicyConfig",frmSwPolicyConfig);
			return this.getForward(strInsuranceConfig, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"Configuration"));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	  //HttpServletResponse response)
	
	
	public ActionForward doSaveDemography(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException
	{
		try
		{
			log.debug("in doSaveDemography");
			
			setLinks(request); 
			InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			String strForwardPath=null;
			DynaActionForm frmSwPolicyConfig = (DynaActionForm)form;
			SwPolicyConfigVO swpolicyConfigVO=null;
			InsPricingVO insPricingVO  = new InsPricingVO();
			ArrayList alLoadDesignList = new ArrayList();
			ArrayList  alplanDesignList =  new ArrayList();
			ArrayList aldemographicDataList = new ArrayList();
			ArrayList<Object> alDemopastData =  (ArrayList<Object>) frmSwPolicyConfig.get("alDemographicData");

		    long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
			swpolicyConfigVO =(SwPolicyConfigVO)FormUtils.getFormValues(frmSwPolicyConfig, "frmSwPolicyConfig",this, mapping, request); 
		
			swpolicyConfigVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			swpolicyConfigVO.setLngGroupProfileSeqID(GroupProfileSeqID);
			insPricingVO.setGroupProfileSeqID(GroupProfileSeqID);
			insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			swpolicyConfigVO.setAlDemoPastData(alDemopastData);

			ArrayList  iResultarray = insuranceObject.saveDemographicData(swpolicyConfigVO);//only save has writtern
			
			InsPricingVO insPricingVO2  =  insuranceObject.getfalgPricingvalue(GroupProfileSeqID);// itshoud be after save data,Once data saved flag will be correct
			String loadingFlagYN = insPricingVO2.getLoadingFlagYN();
			String calCPM_FlagYN = insPricingVO2.getCalCPMFlagYN();
			String demographicDataFlag = insPricingVO2.getDemographicflagYN();
			 
			aldemographicDataList= insuranceObject.getdemographicData(insPricingVO,demographicDataFlag);
			
			
			  if(calCPM_FlagYN.equalsIgnoreCase("Y")){
				  alplanDesignList= insuranceObject.getcpmAfterCalcultion(insPricingVO);
					frmSwPolicyConfig.set("calCPM_FlagYN", calCPM_FlagYN);

					}else{
					alplanDesignList= insuranceObject.getcpmBeforeCalcultion(insPricingVO);//to get CPM Data
					
					}
			
			if(loadingFlagYN.equalsIgnoreCase("Y"))
			{
				alLoadDesignList= insuranceObject.getAfterLoadingData(insPricingVO);	
			}else{
				alLoadDesignList= insuranceObject.getBeforeLoadingData(insPricingVO);//to get Loading data	
			}
			
			int iResult = (int) iResultarray.get(0);
			if (iResult > 0){
				request.setAttribute("updated","message.saved");
				frmSwPolicyConfig.set("Message","N"); 
			}
			SwPolicyConfigVO  swPolicyConfigVOfinalDesign= insuranceObject.getcpmAfterLoading(insPricingVO);// final Data so no Flag condition required
			if(swPolicyConfigVOfinalDesign != null){
			frmSwPolicyConfig= (DynaActionForm)FormUtils.setFormValues("frmSwPolicyConfig",	swPolicyConfigVOfinalDesign, this, mapping, request);
			}
			
		//	frmSwPolicyConfig.set("loadComments", (((SwPolicyConfigVO)alLoadDesignList.get(0)).getLoadComments()));
		//	frmSwPolicyConfig.set("alLoadingData", alLoadDesignList);
			frmSwPolicyConfig.set("alResultpastYear", alplanDesignList);
			frmSwPolicyConfig.set("alDemographicData", aldemographicDataList);
			request.getSession().setAttribute("frmSwPolicyConfig",frmSwPolicyConfig);
			return this.getForward(strInsuranceConfig, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"Configuration"));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	  //HttpServletResponse response)
	
	
	// below method form changed because "Loadings & Management Discount" shifted from premium working screen to gross premium screen
	
	public ActionForward doLoadingCalculate(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException
	{
		try
		{
		
			setLinks(request); 
			InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);
			String strForwardPath=null;
			DynaActionForm frmSwGrosspremium = (DynaActionForm)form;
			SwPricingSummaryVO swPricingSummaryVO=null;
		//	ArrayList  alplanDesignList = new ArrayList();
			String completeSaveYN = (String)request.getSession().getAttribute("completeSaveYN");
			InsPricingVO insPricingVO  = new InsPricingVO();
			ArrayList alLoadDesignList = new ArrayList();
			ArrayList<Object> alLoadingGrosspremium =  (ArrayList<Object>) frmSwGrosspremium.get("alLoadingGrosspremium");
			 
			long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
			swPricingSummaryVO =(SwPricingSummaryVO)FormUtils.getFormValues(frmSwGrosspremium, "frmSwGrosspremium",this, mapping, request); 
		
			swPricingSummaryVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			swPricingSummaryVO.setLngGroupProfileSeqID(GroupProfileSeqID);
		
			insPricingVO.setGroupProfileSeqID(GroupProfileSeqID);
			insPricingVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			
			swPricingSummaryVO.setAlLoadingGrosspremium(alLoadingGrosspremium);
			int  calculateload = 0;
			
			int  saveload = insuranceObject.saveLoading(swPricingSummaryVO);
			if(saveload >0){
				
			  calculateload = insuranceObject.calculateLoading(swPricingSummaryVO);
			}
			
			alLoadDesignList= insuranceObject.getAfterLoadingData(insPricingVO);	//Before loading is not required in save
			
			if(GroupProfileSeqID > 0  && completeSaveYN.equalsIgnoreCase("Y") && calculateload > 0)
			{
				SwPricingSummaryVO  SwPricingSummaryVO= insuranceObject.getcpmAfterLoadingPricing(insPricingVO);// FINAL CPM  Data so no Flag condition required
				if(SwPricingSummaryVO != null){
					frmSwGrosspremium= (DynaActionForm)FormUtils.setFormValues("frmSwGrosspremium",SwPricingSummaryVO, this, mapping, request);
				}
				request.setAttribute("updated","message.saved");
				frmSwGrosspremium.set("Message","N"); 
			}
			else
			{
				((DynaActionForm)form).initialize(mapping);
				frmSwGrosspremium.set("Message","Y"); 
				
				if(completeSaveYN == null){
			    	TTKException expTTK = new TTKException();
					expTTK.setMessage("error.pricing.required");
					throw expTTK;
					}else if(completeSaveYN.equalsIgnoreCase("N")){
						TTKException expTTK = new TTKException();
						expTTK.setMessage("error.pricing.complete.screen");
						throw expTTK;
					}
				
			}

			/*	InsPricingVO insPricingVO2  =  insuranceObject.getfalgPricingvalue(GroupProfileSeqID);
			
			  String calCPM_FlagYN = insPricingVO2.getCalCPMFlagYN();
			  String loadingFlagYN = insPricingVO2.getLoadingFlagYN();
			
			below code commented -->"Loadings & Management Discount" shifted from premium working screen to gross premium screen 
		    if(calCPM_FlagYN.equalsIgnoreCase("Y")){
					alplanDesignList= insuranceObject.getcpmAfterCalcultion(insPricingVO);
					frmSwGrosspremium.set("calCPM_FlagYN", calCPM_FlagYN);

					}else{
					alplanDesignList= insuranceObject.getcpmBeforeCalcultion(insPricingVO);//to get CPM Data
					
					}
			  
		//	ArrayList  alplanDesignList= insuranceObject.getcpmAfterCalcultion(insPricingVO);// cpm  Data 1st table so no Flag condition required
		
			SwPolicyConfigVO  swPolicyConfigVOfinalDesign= insuranceObject.getcpmAfterLoading(insPricingVO);// FINAL CPM  Data so no Flag condition required
			if(swPolicyConfigVOfinalDesign != null){
				frmSwGrosspremium= (DynaActionForm)FormUtils.setFormValues("frmSwGrosspremium",	swPolicyConfigVOfinalDesign, this, mapping, request);
			}*/
			frmSwGrosspremium.set("loadComments", (((SwPricingSummaryVO)alLoadDesignList.get(0)).getLoadComments()));
			frmSwGrosspremium.set("alLoadingGrosspremium", alLoadDesignList);
		//	frmSwGrosspremium.set("alResultpastYear", alplanDesignList);
			request.getSession().setAttribute("frmSwGrosspremium",frmSwGrosspremium);
		
		//	return this.getForward(strInsuranceConfig, mapping, request);
			return this.getForward(strGrossPremium, mapping, request);
			//return mapping.findForward(strPremiumConfig);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"Configuration"));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	  //HttpServletResponse response)
	
	
	public ActionForward doSaveCalculateQuote(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException
	{
		try
		{
			setLinks(request); 
			InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			String strActiveSubLink=TTKCommon.getActiveSubLink(request);

			DynaActionForm frmSwPolicyConfigQuote = (DynaActionForm)form;
			PolicyConfigVO policyConfigVO=null;
			 
			long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
			
			policyConfigVO =(PolicyConfigVO)FormUtils.getFormValues(frmSwPolicyConfigQuote, "frmSwPolicyConfigQuote",this, mapping, request); 
			
			
			//as per 1274 A latest changes
			policyConfigVO.setGroupProfileSeqID(GroupProfileSeqID);
			 
			policyConfigVO = insuranceObject.saveGenerateQuote(policyConfigVO);
			
			//Object[]	premiumProposal=null;
			ArrayList<PolicyConfigVO> PremiumFirst=null;
			ArrayList<PolicyConfigVO> PremiumSecond=null;
			if(policyConfigVO.getGenerateflag().equals("Y"))
			{
					request.setAttribute("updated","message.saved");
					Object[]	premiumProposal = insuranceObject.selectGenerateQuote(GroupProfileSeqID);
					PolicyConfigVO policyConfigVO1 = (PolicyConfigVO) premiumProposal[0];
					 PremiumFirst = (ArrayList<PolicyConfigVO>) premiumProposal[1];
					 PremiumSecond = (ArrayList<PolicyConfigVO>) premiumProposal[2];
					frmSwPolicyConfigQuote = (DynaActionForm)FormUtils.setFormValues("frmSwPolicyConfigQuote",policyConfigVO1, this, mapping, request);
					frmSwPolicyConfigQuote.set("Message","N"); 
					
			}//end of if(iResult>0)
		 
	
			
			//frmConfigSumInsured.set("preAuthClaimsExistAlert",preAuthClaimsExistAlert);
			request.getSession().setAttribute("PremiumFirst",PremiumFirst);
			frmSwPolicyConfigQuote.set("PremiumFirst",PremiumFirst);
			frmSwPolicyConfigQuote.set("PremiumSecond",PremiumSecond);
			request.getSession().setAttribute("frmSwPolicyConfigQuote",frmSwPolicyConfigQuote);

			 
			return this.getForward(strGenerateConfig, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,"Configuration"));
		}//end of catch(Exception exp)
	}
	
	
	
	
	/**
	 * This method is used to reload the screen when the reset button is pressed.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws TTKException if any error occurs
	 */
		public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws TTKException
	    {
				setLinks(request);
				return doDefault(mapping, form, request, response);
	    }//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		//HttpServletResponse response)
		
		/**
		 * This method is used to navigate to previous screen when close  button is clicked.
		 * Finally it forwards to the appropriate view based on the specified forward mappings
		 *
		 * @param mapping The ActionMapping used to select this instance
		 * @param form The optional ActionForm bean for this request (if any)
		 * @param request The HTTP request we are processing
		 * @param response The HTTP response we are creating
		 * @return ActionForward Where the control will be forwarded, after this request is processed
		 * @throws Exception if any error occurs
		 */
		public ActionForward doClose(ActionMapping mapping,ActionForm form,
				HttpServletRequest request,HttpServletResponse response) throws TTKException
		{
			try
			{
				setLinks(request);
				String strActiveSubLink=TTKCommon.getActiveSubLink(request);
				String strForward="";
				if("Products".equals(strActiveSubLink)){
					strForward=strProductClose;
				}//end of if("Products".equals(strActiveSubLink))
				else {
					strForward=strPolicyClose;
				}//end of else
				if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				{
					((DynaActionForm)form).initialize(mapping);//reset the form data
				}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
				request.getSession().removeAttribute("frmConfigInsuranceApprove");
				return this.getForward(strForward, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp, "Configuration"));
			}//end of catch(Exception exp)
		}// end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
		
		
		
	/**
	 * This method is to build the Caption 
	 * @param request current HttpRequest
	 * @param form 
	 * @return String caption built
	 * @throws TTKException
	 */
	private String buildCaption(HttpServletRequest request)throws Exception
	{
		String strCaption=null;
		StringBuffer sbfCaption= new StringBuffer();
		String strActiveSubLink=TTKCommon.getActiveSubLink(request);
		if(strActiveSubLink.equals("Policies"))
		{
			DynaActionForm frmPolicies = (DynaActionForm)request.getSession().getAttribute("frmPoliciesEdit");
			sbfCaption.append("Configuration - [ ").append(frmPolicies.get("companyName")).append(" ] [ ").append(frmPolicies.get("policyNbr")).append(" ] ");
		}//end of else if(strActiveSubLink.equals("Policies"))
		if(strActiveSubLink.equals("Products"))
		{
			DynaActionForm frmProductList = (DynaActionForm)request.getSession().getAttribute("frmProductGeneralInfo");
			sbfCaption.append("Configuration - [").append(frmProductList.get("companyName")).append("]");
			sbfCaption.append(" [").append(frmProductList.get("productName")).append("]");			
		}//end of if(strActiveSubLink.equals("Products"))
		strCaption=String.valueOf(sbfCaption);
		return strCaption;
	}//end of buildCaption(HttpServletRequest request)
	
	
	  private String calculateValidation (ArrayList<Object> alpastData) throws TTKException{
    	

	
			BigDecimal totalFinalWeighatage = new BigDecimal(0.00); 
			  BigDecimal totalsumWeightage = new BigDecimal(0.00); 
			  String dataTypeyear = "";
			  int idata = 0;int i=0;
			
		    if(!(alpastData.isEmpty())){
              for(Object object:alpastData){
          SwPolicyConfigVO   swpolicyConfigVOvalid = (SwPolicyConfigVO)object;
      
          dataTypeyear=swpolicyConfigVOvalid.getDataType();
          if(!dataTypeyear.equalsIgnoreCase("FNL_PROJ_DTA"))
       	{ 
        	  idata++;
        	  if(swpolicyConfigVOvalid.getFinalweightage() != null &&  swpolicyConfigVOvalid.getFinalweightage()!= ""){
          			totalFinalWeighatage= new BigDecimal(swpolicyConfigVOvalid.getFinalweightage());
		            }else{
		            	totalFinalWeighatage = new BigDecimal(0.00);
		            }
                 totalsumWeightage=totalsumWeightage.add(totalFinalWeighatage);

              }//end if
			       
              }//end for
              
              if (idata>0 && totalsumWeightage.doubleValue() != 100){
		      		TTKException expTTK = new TTKException();
						expTTK.setMessage("error.pricing.weightage");
						throw expTTK;
		        	}
          	 
		    }//end isempty if
			
		  
		return null;   
		
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
    

	
	
	
    public ActionForward doViewInputSummary(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doViewInputSummary method of ReportsAction");
			 setLinks(request);
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			
			 String jrxmlfile=request.getParameter("fileName");
		
			 
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));			 
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport(jrxmlfile);
				
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 ByteArrayOutputStream boas=new ByteArrayOutputStream();
			
				if(ttkReportDataSource.getResultData().next())
				 {
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of if(ttkReportDataSource.getResultData().next())
				 
			
				JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boas",boas);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
			 return (mapping.findForward(strReportdisplay));
		 }//end of try
		 catch(TTKException expTTK)
		 {
			 return this.processExceptions(request, mapping, expTTK);
		 }//end of catch(TTKException expTTK)
		 catch(Exception exp)
		 {
			 return this.processExceptions(request, mapping, new TTKException(exp,strInsError));
		 }//end of catch(Exception exp)
	 }//end of doGeneratePolicyHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		
    
    
    public ActionForward doPrintpdf(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		setLinks(request);
    		return (mapping.findForward(strReportdisplay));
    	}
    	catch(TTKException expTTK)
    	{
    		return this.processExceptions(request,mapping,expTTK);
    	}//end of catch(ETTKException expTTK)
    	catch(Exception exp)
    	{
    		return this.processExceptions(request,mapping,new TTKException(exp,strReportExp));
    	}//end of catch(Exception exp)
    }//end of doViewPaymentXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
		
    public static void doMergePDF(List<InputStream> list, OutputStream outputStream)
			throws DocumentException, IOException {
				Document document = new Document();
				PdfWriter writer = PdfWriter.getInstance(document, outputStream);
				writer.open();
				document.open();

				PdfContentByte cb = writer.getDirectContent();
			
				for (InputStream in : list) {
				
					PdfReader reader = new PdfReader(in);
					
					for (int i = 1; i <=reader.getNumberOfPages(); i++) {
						
						document.newPage();
						//import the page from source pdf
						PdfImportedPage page = writer.getImportedPage(reader, i);
						//add the page to the destination pdf
						cb.addTemplate(page, 0, 0);
					}
				}
				outputStream.flush();
				document.close();
				outputStream.close();
			}
  
    
	/**
	 * Returns the ProductPolicyManager session object for invoking methods on it.
	 * @return ProductPolicyManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private ProductPolicyManager getProductPolicyManager() throws TTKException
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
			throw new TTKException(exp, "Configuration");
		}//end of catch
		return productPolicyManager;
	}//end getProductPolicyManager()
	
	public static boolean getDiffYears(Date first, Date last) {
		Calendar a = getCalendar(first);
		Calendar b = getCalendar(last);
		boolean checkPolicy=true;
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (diff <= 1) {
			long days = getDifferenceDays(last, first);
			boolean isLeafYear=checkLeafYear(b,a);
			if (isLeafYear) 
				if (days>=365) 
					checkPolicy=true;
				 else 
					checkPolicy=false;
			else 
				if (days>=364)
					checkPolicy=true;
				 else 
					checkPolicy=false;
		}
		return checkPolicy;
	}

	private static boolean checkLeafYear(Calendar endDate, Calendar startDate) {
		boolean isLeaf=false;
		if (endDate.get(Calendar.YEAR) % 4 == 0) {
			int month = endDate.get(Calendar.MONTH);
			int day = endDate.get(Calendar.DAY_OF_MONTH);
			if (month >= 1) {
				if (month == 1 && (day > 28))
					isLeaf = true;
				else if (month >1)
					isLeaf = true;
			}
		}
		if(startDate.get(Calendar.YEAR) % 4 == 0){
			int month=startDate.get(Calendar.MONTH);
			if(month<=1)
				isLeaf=true;
		}
		return isLeaf;
	}
	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		return cal;
	}

	public static long getDifferenceDays(Date d1, Date d2) {
		 return (int)( (d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
	}
}//end of InsureApproveConfiguration
	
