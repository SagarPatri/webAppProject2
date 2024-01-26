

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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.administration.ProductPolicyManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.common.security.Cache;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.administration.InsuranceApproveVO;
import com.ttk.business.empanelment.InsuranceManager;


import com.ttk.business.enrollment.PolicyManager;
import com.ttk.business.preauth.PreAuthManager;
import com.ttk.dto.insurancepricing.InsPricingVO;
import com.ttk.dto.insurancepricing.PolicyConfigVO;
import com.ttk.dto.insurancepricing.SwFinalQuoteVO;
import com.ttk.dto.insurancepricing.SwPolicyConfigVO;
import com.ttk.dto.insurancepricing.SwPricingSummaryVO;
import com.ttk.dto.preauth.DiagnosisDetailsVO;
import com.ttk.dto.preauth.PreAuthDetailVO;
import com.ttk.dto.preauth.PreAuthVO;

import formdef.plugin.util.FormUtils;


public class SwFinalGenerateQuotationAction extends TTKAction  {
	
	//private static final Logger log = Logger.getLogger( PlanDesignConfiguration.class );
	
	 
	private static final String strFinalQuote="finalQuote";
	private static final String strReportdisplay="reportdisplay";
	private static final String strInsError="insurance";
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	private static final String strGenerateQuote="finalQuote";
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
					DynaActionForm frmSwFinalQuote = (DynaActionForm)form;
					SwFinalQuoteVO swFinalQuoteVO =  new SwFinalQuoteVO();
				    long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
					swFinalQuoteVO.setLngGroupProfileSeqID(GroupProfileSeqID);
					swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
					InsPricingVO  insPricingVO  =  insuranceObject.getfalgPricingvalue(GroupProfileSeqID);
					String completepricing = insPricingVO.getLoadingFlagYN();
					String trendFactor = insPricingVO.getTrendFactor();
					String pricingModifiedYN = insPricingVO.getPricingmodifyYN();
				//	ArrayList alInsProducts = new ArrayList();
					
					//alInsProducts = insuranceObject.getAlkootProducts();
            		swFinalQuoteVO= insuranceObject.swSelectQuotationdetails(swFinalQuoteVO);
            		//System.out.println("array size--->"+alInsProducts.size());
            		//frmSwFinalQuote.set("alInsProducts",alInsProducts);
            		
					if((GroupProfileSeqID > 0) && completepricing.equalsIgnoreCase("Y") && pricingModifiedYN.equalsIgnoreCase("N"))  
					//	if(GroupProfileSeqID > 0)
					{
						frmSwFinalQuote.set("Message","N"); 
						frmSwFinalQuote = (DynaActionForm)FormUtils.setFormValues("frmSwFinalQuote",swFinalQuoteVO,
								this,mapping,request);
					}
					else
					{
						frmSwFinalQuote.set("Message",request.getParameter("Message")); 
						frmSwFinalQuote.set("Message","Y");
						if(pricingModifiedYN.equalsIgnoreCase("Y")){
							TTKException expTTK = new TTKException();
							expTTK.setMessage("error.pricing.complete.screen.riskpremium");
							throw expTTK;
						}else{
		            	TTKException expTTK = new TTKException();
						expTTK.setMessage("error.pricing.required");
						throw expTTK;
						}
						
					}
					
	///////////////////table data/////////////////////
					String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		            String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		            TableData tableDataquo = TTKCommon.getTableData(request);
		            //if the page number or sort id is clicked
		            if(!strPageID.equals("") || !strSortID.equals(""))
		            {
		            	if(!strPageID.equals(""))
		                {
		            		tableDataquo.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
		                    return mapping.findForward(strFinalQuote);
		                }///end of if(!strPageID.equals(""))
		                else
		                {
		                	tableDataquo.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
		                	tableDataquo.modifySearchData("sort");//modify the search data
		                }//end of else
		            }//end of if(!strPageID.equals("") || !strSortID.equals(""))
		            else
		            {
		                //create the required grid table
		            	tableDataquo.createTableInfo("Quotationtable",null);
		            	tableDataquo.modifySearchData("search");
		            }//end of else
		            ArrayList alpolicyCopiesList= insuranceObject.getPolicyCopiesList(swFinalQuoteVO);

		            tableDataquo.setData(alpolicyCopiesList, "search");
					//set the table data object to session
					request.getSession().setAttribute("tableDataquo",tableDataquo);
					
					if(alpolicyCopiesList.size() > 0){
					SwFinalQuoteVO	swFinalQuoteVOtable=(SwFinalQuoteVO)alpolicyCopiesList.get(alpolicyCopiesList.size()-(alpolicyCopiesList.size()));
					swFinalQuoteVO.setFinalQuotationNo(swFinalQuoteVOtable.getFinalQuotationNo());
					swFinalQuoteVO.setFinalPolcopyseqid(swFinalQuoteVOtable.getFinalPolcopyseqid());
				
					
					}
					///////////////////table data/////////////////////
					frmSwFinalQuote= (DynaActionForm)FormUtils.setFormValues("frmSwFinalQuote",	swFinalQuoteVO, this, mapping, request);
					request.getSession().setAttribute("GroupProfileSeqID", GroupProfileSeqID); 
					request.getSession().setAttribute("trendFactor", trendFactor);
					request.getSession().setAttribute("frmSwFinalQuote",frmSwFinalQuote); 
					Cache.refresh("alkootProducts");
					return this.getForward(strFinalQuote, mapping, request);
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
	
	
	
	public ActionForward  doProductList(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException 
			{
				try
				{
					setLinks(request);
					
					InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
					PolicyManager policyManagerObject=this.getPolicyManagerObject();
					DynaActionForm frmSwFinalQuote = (DynaActionForm)form;
					SwFinalQuoteVO swFinalQuoteVO =  new SwFinalQuoteVO();
					ArrayList alInsProducts = new ArrayList();
					
					 
					long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
					swFinalQuoteVO.setLngGroupProfileSeqID(GroupProfileSeqID);
					String insSeqid = request.getParameter("insSeqid");
					if(insSeqid != "")
					{
						frmSwFinalQuote.set("Message","N"); 
						HashMap hmInsProducts = policyManagerObject.getProductInfo(TTKCommon.getLong(request.getParameter("insSeqid")));
						alInsProducts = null;			
						alInsProducts=(ArrayList)hmInsProducts.get("COR");
						if(alInsProducts == null)
						{
							alInsProducts = new ArrayList();
						}//end of if(alInsProducts == null)
						
					}
				/*	else
					{
						frmSwFinalQuote.set("Message",request.getParameter("Message")); 
						frmSwFinalQuote.set("Message","Y"); 
		            	TTKException expTTK = new TTKException();
						expTTK.setMessage("error.pricing.required");
						throw expTTK;
					}*/
					//System.out.println("change qrray---->"+alInsProducts.size());
					frmSwFinalQuote.set("alInsProducts",alInsProducts);		
					frmSwFinalQuote.set("companyName", request.getParameter("insSeqid"));
					request.getSession().setAttribute("GroupProfileSeqID", GroupProfileSeqID); 
					request.getSession().setAttribute("frmSwFinalQuote",frmSwFinalQuote); 
					return this.getForward(strFinalQuote, mapping, request);
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
    
	  
    public ActionForward doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    try{
    setLinks(request);
    DynaActionForm frmSwFinalQuote= (DynaActionForm)form;
    SwFinalQuoteVO swFinalQuoteVO=null;  String successMsg="";

    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
    swFinalQuoteVO = (SwFinalQuoteVO)FormUtils.getFormValues(frmSwFinalQuote, this, mapping, request);
    swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
	long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
	//swFinalQuoteVO.setLngGroupProfileSeqID(group_seq_id);
	
	
	 int result = insuranceObject.swSaveQuotationdetails(swFinalQuoteVO);
	 if(result > 0)successMsg="Details Added Successfully";
	
	  swFinalQuoteVO= insuranceObject.swSelectQuotationdetails(swFinalQuoteVO);
	 frmSwFinalQuote= (DynaActionForm)FormUtils.setFormValues("frmSwFinalQuote",	swFinalQuoteVO, this, mapping, request);
	 request.setAttribute("successMsg",successMsg);
		request.getSession().setAttribute("frmSwFinalQuote", frmSwFinalQuote);	
 
	 return mapping.findForward(strFinalQuote);
	    
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
    
  
	public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{

    		setLinks(request);
    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
            String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
            TableData tableDataquo = TTKCommon.getTableData(request);
             SwFinalQuoteVO swFinalSearchQuoteVO=new SwFinalQuoteVO(); 
             SwFinalQuoteVO swfinalquote =new SwFinalQuoteVO(); 
             DynaActionForm swfinalquotefrm= (DynaActionForm)form;
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			 
			 swFinalSearchQuoteVO.setLngGroupProfileSeqID(group_seq_id);
			 swFinalSearchQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
            //if the page number or sort id is clicked
            if(!strPageID.equals("") || !strSortID.equals(""))
            {
            	if(!strPageID.equals(""))
                {
            		tableDataquo.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
                    return mapping.findForward(strFinalQuote);
                }///end of if(!strPageID.equals(""))
                else
                {
                	tableDataquo.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
                	tableDataquo.modifySearchData("sort");//modify the search data
                }//end of else
            }//end of if(!strPageID.equals("") || !strSortID.equals(""))
            else
            {
                //create the required grid table
            	tableDataquo.createTableInfo("Quotationtable",null);
            	//tableDataquo.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
            	tableDataquo.modifySearchData("search");
            }//end of else
            ArrayList alpolicyCopiesList= insuranceObject.getPolicyCopiesList(swFinalSearchQuoteVO);
            tableDataquo.setData(alpolicyCopiesList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableDataquo",tableDataquo);
			if(alpolicyCopiesList.size() > 0){
			swfinalquote=(SwFinalQuoteVO)alpolicyCopiesList.get(alpolicyCopiesList.size()-(alpolicyCopiesList.size()));
			swfinalquotefrm.set("finalQuotationNo",swfinalquote.getFinalQuotationNo());
			swfinalquotefrm.set("finalPolcopyseqid",swfinalquote.getFinalPolcopyseqid().toString());
		
    	     }
			
			request.getSession().setAttribute("frmSwFinalQuote", swfinalquotefrm);	
			//finally return to the grid screen
			return this.getForward(strFinalQuote, mapping, request);
			//return mapping.findForward("reFwdToSearch");
			
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
    
	public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
			try{
			log.debug("Inside PreAuthAction doForward");
			setLinks(request);
			TableData tableData = TTKCommon.getTableData(request);
			 SwFinalQuoteVO swFinalSearchQuoteVO=new SwFinalQuoteVO();  
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			 swFinalSearchQuoteVO.setLngGroupProfileSeqID(group_seq_id);
			 swFinalSearchQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			 
			tableData.modifySearchData(strForward);//modify the search data
            ArrayList alpolicyCopiesList= insuranceObject.getPolicyCopiesList(swFinalSearchQuoteVO);
			tableData.setData(alpolicyCopiesList, strForward);//set the table data
			request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
			return this.getForward(strFinalQuote, mapping, request);   //finally return to the grid screen
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

	
	public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
				try{
				log.debug("Inside PreAuthAction doBackward");
				setLinks(request);
				TableData tableData = TTKCommon.getTableData(request);
				 SwFinalQuoteVO swFinalSearchQuoteVO=new SwFinalQuoteVO();  
				 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
				 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
				 swFinalSearchQuoteVO.setLngGroupProfileSeqID(group_seq_id);
				 swFinalSearchQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
				 
				tableData.modifySearchData(strBackward);//modify the search data
	            ArrayList alpolicyCopiesList= insuranceObject.getPolicyCopiesList(swFinalSearchQuoteVO);
				tableData.setData(alpolicyCopiesList, strBackward);//set the table data
				request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
				return this.getForward(strFinalQuote, mapping, request);   //finally return to the grid screen
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
    
    public ActionForward doViewQuotaion(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doView Quotation method of ReportsAction");
			 setLinks(request);
			 
			 DynaActionForm frmSwFinalQuote= (DynaActionForm)form;
			 SwFinalQuoteVO swFinalQuoteVO=null;  
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			 swFinalQuoteVO = (SwFinalQuoteVO)FormUtils.getFormValues(frmSwFinalQuote, this, mapping, request);
			 swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			 ByteArrayOutputStream boasout=new ByteArrayOutputStream();
			 String QuotationNo = "";

			 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			 
			 JasperReport jasperReport,emptyReport;
			 JasperPrint jasperPrint;
			 TTKReportDataSource ttkReportDataSource = null;
			
			 String jrxmlfile=request.getParameter("fileName");
		
			// String partmeter = "212";
			 
			 ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));			 
			// ttkReportDataSource = new TTKReportDataSource("GenerateQauotation",partmeter);			 
			 try
			 {
				 jasperReport = JasperCompileManager.compileReport("generalreports/GenerateQauotation.jrxml");
				
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
				if(ttkReportDataSource.getResultData().next())
				 {
					 QuotationNo = ttkReportDataSource.getResultData().getString("QUOT_NO");
					

					 ttkReportDataSource.getResultData().beforeFirst();
				
					 jasperPrint = JasperFillManager.fillReport( jasperReport, hashMap,ttkReportDataSource);					 
				 }//end of if(ttkReportDataSource.getResultData().next()))
				 else
				 {
					 jasperPrint = JasperFillManager.fillReport( emptyReport, hashMap,new JREmptyDataSource());
				 }//end of if(ttkReportDataSource.getResultData().next())
				 
				
				request.setAttribute("quatationFIle", jasperPrint);
				request.getSession().setAttribute("quatationFIleSession", jasperPrint);
				
				 JasperExportManager.exportReportToPdfStream(jasperPrint,boasout);
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 request.setAttribute("boasout",boasout);
				 //return this.getForward(strFinalQuote, mapping, request);
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
				 byte[] data=null;
				 data=boasout.toByteArray();
				 if(QuotationNo !=null && ""!=QuotationNo)
				 {
				 swFinalQuoteVO.setFinalpolicydocYN("N");
				 swFinalQuoteVO.setPolcopyseqid(null);
				 swFinalQuoteVO.setQuotationNo(QuotationNo);
				
				 Long lpolcopySeqId= insuranceObject.swSave_pol_copies(swFinalQuoteVO,data);//save the generated Quotation in DB
				 }
			 //return this.getForward(strFinalQuote, mapping, request);
				 
					//return (mapping.findForward(strReportdisplay));
				 return mapping.findForward("reFwdToSearch");

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
    
    //below method doGenerateQuotaion is for reference dynamic method added below in doGenerateQuotaionDynamic
    public ActionForward doGenerateQuotaion(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doGenerateQuotaion method");
			 setLinks(request);
			 
			 DynaActionForm frmSwFinalQuote= (DynaActionForm)form;
			 SwFinalQuoteVO swFinalQuoteVO=null;  
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			 swFinalQuoteVO = (SwFinalQuoteVO)FormUtils.getFormValues(frmSwFinalQuote, this, mapping, request);
			 int iNoOfCursor = 6;
			 swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			 ByteArrayOutputStream boas1=new ByteArrayOutputStream();
			 ByteArrayOutputStream boas2=new ByteArrayOutputStream();
			 String QuotationNo= "";
			 List<InputStream> fileData = new ArrayList<InputStream>();
			
			 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			 String strParameter = request.getParameter("parameter");
			 
			 JasperReport jasperReport1,jasperReport2,emptyReport;
			 JasperPrint jasperPrint1 = null;
			 JasperPrint jasperPrint2 = null;
			
			 try
			 {
				 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 				 
				 TTKReportDataSource ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParameter);			
				 jasperReport1 = JasperCompileManager.compileReport("reports/quotation/GenerateQauotationfrontpage1.jrxml"); 
				 if(ttkReportDataSource.getResultData().next())
				 { 
					 QuotationNo = ttkReportDataSource.getResultData().getString("QUOT_NO");
					 ttkReportDataSource.getResultData().beforeFirst();
					  jasperPrint1 = JasperFillManager.fillReport(jasperReport1, hashMap, ttkReportDataSource);
				 }
				 String strPdfFile1 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_page1.pdf";
				 JasperExportManager.exportReportToPdfFile(jasperPrint1,strPdfFile1); 
				 
				 JasperExportManager.exportReportToPdfStream(jasperPrint1,boas1);
				 ByteArrayInputStream is1 = new ByteArrayInputStream(boas1.toByteArray());
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				
				 				 
/*Notes for Letter Section*/
								
				 TTKReportDataSource ttkReportDataSource2 = new TTKReportDataSource(request.getParameter("reportID"),request.getParameter("parameter"));			
				 jasperReport2 = JasperCompileManager.compileReport("reports/quotation/GenerateQauotationpage2.jrxml"); 
				 if(ttkReportDataSource2.getResultData().next())
				 { 
					 ttkReportDataSource2.getResultData().beforeFirst();
					 jasperPrint2 = JasperFillManager.fillReport(jasperReport2, hashMap, ttkReportDataSource2);
				 }
				 String strPdfFile2 = TTKPropertiesReader.getPropertyValue("generatequoterptdir")+strParameter+"_page2.pdf";
				 JasperExportManager.exportReportToPdfFile(jasperPrint2,strPdfFile2); 
				 JasperExportManager.exportReportToPdfStream(jasperPrint2,boas2);
				 ByteArrayInputStream is2 = new ByteArrayInputStream(boas2.toByteArray());
				 
				 
				 String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("quotationdir"))+"/";
				 path = path+"QUO_"+QuotationNo+".pdf";
				 OutputStream outputStream = new FileOutputStream(path);
				 fileData.add(is1);
				 fileData.add(is2);
				 doPdfMergeBytes(request,fileData,outputStream);
				 
				 /// file end ///	 
				 
				 String fileName2="";
				 File file = null;
				 String path1=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("quotationdir"))+"/";
				 fileName2="QUO_"+QuotationNo+".pdf";
				 String finalPath=(path1+fileName2);
				   
			     file = new File(finalPath);
				 if(file.exists())
	    			{
					 	finalPath = (path1+fileName2);
	    				request.setAttribute("fileName",finalPath);
	    			}//end of if(file.exists())
				// JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 
				 File filesave = new File(finalPath);  
				 if(QuotationNo !=null && ""!=QuotationNo)
				 {			     
			     byte[] data = new byte[(int) filesave.length()];
			     FileInputStream fis = new FileInputStream(filesave);
			     fis.read(data); //read file into bytes[]
			     fis.close();
				 swFinalQuoteVO.setFinalpolicydocYN("N");
				 swFinalQuoteVO.setPolcopyseqid(null);
				 swFinalQuoteVO.setQuotationNo(QuotationNo);
				
				 Long lpolcopySeqId= insuranceObject.swSave_pol_copies(swFinalQuoteVO,data);//save the generated Quotation in DB*/
				 }
			
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
				
		
			 //return this.getForward(strFinalQuote, mapping, request);
				// return this.getForward(strGenerateQuote, mapping, request);
				 return mapping.findForward("reFwdToSearch");
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
    
    
    public ActionForward doGenerateQuotaionDynamic(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.info("Inside the doGenerateQuotaion method");
			 setLinks(request);
			 
			 DynaActionForm frmSwFinalQuote= (DynaActionForm)form;
			 SwFinalQuoteVO swFinalQuoteVO=null;  
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			 swFinalQuoteVO = (SwFinalQuoteVO)FormUtils.getFormValues(frmSwFinalQuote, this, mapping, request);
			 int iNoOfCursor = 8;
			 
			 JasperReport jasperReport[] =new JasperReport[iNoOfCursor];
			 JasperReport emptyReport;
			 JasperPrint jasperPrint[] = new JasperPrint[iNoOfCursor];
				
			 swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			
			 String QuotationNo= "";
			 List<InputStream> fileData = new ArrayList<InputStream>();
			 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			 String strParameter = request.getParameter("parameter");
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();

			 String jrxmlFiles[] = new String[]
                     {
					"reports/quotation/GenerateQauotationfrontpage1.jrxml",
					"reports/quotation/GenerateQauotationpage2.jrxml",
					"reports/quotation/GenerateQauotationpage3.jrxml",
					"reports/quotation/GenerateQauotationpage4.jrxml",
					"reports/quotation/GenerateQauotationpage5.jrxml",
					"reports/quotation/GenerateQauotationpage6.jrxml",
					"reports/quotation/GenerateQauotationpage7.jrxml",
					"reports/quotation/GenerateQauotationpage8.jrxml",
                     };
			
			 try
			 {
				 
				 for(int i=0;i<iNoOfCursor;i++)
					{
				
				 ByteArrayOutputStream boasi=new ByteArrayOutputStream();// i is the number of cursors in boasi
				 				 
				 TTKReportDataSource ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParameter);			
				 jasperReport[i] = JasperCompileManager.compileReport(jrxmlFiles[i]); 
				 if(ttkReportDataSource.getResultData().next())
				 { 
					 QuotationNo = ttkReportDataSource.getResultData().getString("QUOT_NO");
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint[i] = JasperFillManager.fillReport(jasperReport[i], hashMap, ttkReportDataSource);
				 }
				 
				 JasperExportManager.exportReportToPdfStream(jasperPrint[i],boasi);
				 ByteArrayInputStream isi = new ByteArrayInputStream(boasi.toByteArray());
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 fileData.add(isi);// i is the number of cursors in isi
			 }
			 
			 String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("quotationdir"))+"/";
			 path = path+"QUO_"+QuotationNo+".pdf";
			 OutputStream outputStream = new FileOutputStream(path);
				 
				 doPdfMergeBytes(request,fileData,outputStream);
				 
				 /// file end ///	 
				 
				 String fileName2="";
				 File file = null;
				 String path1=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("quotationdir"))+"/";
				 fileName2="QUO_"+QuotationNo+".pdf";
				 String finalPath=(path1+fileName2);
				   
			     file = new File(finalPath);
				 if(file.exists())
	    			{
					 	finalPath = (path1+fileName2);
	    				request.setAttribute("fileName",finalPath);
	    			}//end of if(file.exists())
				// JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 
				 File filesave = new File(finalPath);  
				 if(QuotationNo !=null && ""!=QuotationNo)
				 {			     
			     byte[] data = new byte[(int) filesave.length()];
			     FileInputStream fis = new FileInputStream(filesave);
			     fis.read(data); //read file into bytes[]
			     fis.close();
				 swFinalQuoteVO.setFinalpolicydocYN("N");
				 swFinalQuoteVO.setPolcopyseqid(null);
				 swFinalQuoteVO.setQuotationNo(QuotationNo);
			
				 Long lpolcopySeqId= insuranceObject.swSave_pol_copies(swFinalQuoteVO,data);//save the generated Quotation in DB*/
				 }
			
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
				
		
			 //return this.getForward(strFinalQuote, mapping, request);
				// return this.getForward(strGenerateQuote, mapping, request);
				 return mapping.findForward("reFwdToSearch");
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
    
    public static void doPdfMergeBytes(HttpServletRequest request,List<InputStream> list, OutputStream outputStream){

        Document document = new Document();
       try{

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.open();
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        for (InputStream inputStream : list) {
            PdfReader reader = new PdfReader(inputStream);
          
            for (int i = 1; i <=reader.getNumberOfPages(); i++){
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }//end of for (int i = 1; i <=reader.getNumberOfPages(); i++)
        }//end of for (InputStream inputStream : list)
        outputStream.flush();
       }//end of try
       catch(Exception exception){
           log.error("PDF merging some Problem Occured "+exception);;
       }//end of catch(Exception exception){
       finally{
        if(document != null)document.close();
           try {
               if(outputStream != null)outputStream.close();
           }catch (IOException e) {
               e.printStackTrace();
           }
       }//end of finally

   }//end of public static void doPdfMerge(HttpServletRequest request,List<InputStream> list, OutputStream outputStream)

    
    
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
		
	 public ActionForward doPrintAcknowledgement(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		setLinks(request);
	    		
	    		JasperPrint jasperPrint	=	(JasperPrint) request.getSession().getAttribute("quatationFIleSession"); 
	    		 ByteArrayOutputStream boas=new ByteArrayOutputStream();

	    		//ByteArrayOutputStream boas=new ByteArrayOutputStream();
	    		JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
	    		request.setAttribute("boas",boas);
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
	 
	 
	 public ActionForward doPrintAcknowledgementfile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
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
    
    public ActionForward doPolicyCopy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			 log.debug("Inside the doViewInputSummary method of ReportsAction");
			 setLinks(request);
			 
			 DynaActionForm frmSwFinalQuote= (DynaActionForm)form;
			 SwFinalQuoteVO swFinalQuoteVO=null;  
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			 swFinalQuoteVO = (SwFinalQuoteVO)FormUtils.getFormValues(frmSwFinalQuote, this, mapping, request);
			 swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			 ByteArrayOutputStream boas=new ByteArrayOutputStream();

			 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			 
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
		
			 //return this.getForward(strFinalQuote, mapping, request);
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
    

    public ActionForward doPolicyCopyDynamic(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 try{
			
			 setLinks(request);
			 DynaActionForm frmSwFinalQuote= (DynaActionForm)form;
			 SwFinalQuoteVO swFinalQuoteVO=null;  
			 InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
			 swFinalQuoteVO = (SwFinalQuoteVO)FormUtils.getFormValues(frmSwFinalQuote, this, mapping, request);
			 int iNoOfCursor = 6;
			 
			 JasperReport jasperReport[] =new JasperReport[iNoOfCursor];
			 JasperReport emptyReport;
			 JasperPrint jasperPrint[] = new JasperPrint[iNoOfCursor];
				
			 swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
			
			 String QuotationNo= "";
			 List<InputStream> fileData = new ArrayList<InputStream>();
			 long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			 String strParameter = request.getParameter("parameter");
			 emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();

			 String jrxmlFiles[] = new String[]
                    {
					"reports/quotation/PolicyCopypage1.jrxml",
					"reports/quotation/PolicyCopypage2.jrxml",
					"reports/quotation/PolicyCopypage3.jrxml",
					"reports/quotation/PolicyCopypage4.jrxml",
					"reports/quotation/PolicyCopypage5.jrxml",
					"reports/quotation/PolicyCopypage6.jrxml",
                    };
			
			 try
			 {
				 
				 for(int i=0;i<iNoOfCursor;i++)
					{
					
				 ByteArrayOutputStream boasi=new ByteArrayOutputStream();// i is the number of cursors in boasi
				 				 
				 TTKReportDataSource ttkReportDataSource = new TTKReportDataSource(request.getParameter("reportID"),strParameter);			
				 jasperReport[i] = JasperCompileManager.compileReport(jrxmlFiles[i]); 
				 if(ttkReportDataSource.getResultData().next())
				 { 
					 QuotationNo = ttkReportDataSource.getResultData().getString("QUOT_NO");
					 ttkReportDataSource.getResultData().beforeFirst();
					 jasperPrint[i] = JasperFillManager.fillReport(jasperReport[i], hashMap, ttkReportDataSource);
				 }
				 
				 JasperExportManager.exportReportToPdfStream(jasperPrint[i],boasi);
				 ByteArrayInputStream isi = new ByteArrayInputStream(boasi.toByteArray());
				 //keep the byte array out stream in request scope to write in the BinaryStreamServlet
				 fileData.add(isi);// i is the number of cursors in isi
			 }
			 
			 String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("quotationdir"))+"/";
			 path = path+"PCO_"+QuotationNo+".pdf";
			 OutputStream outputStream = new FileOutputStream(path);
				 
				 doPdfMergeBytes(request,fileData,outputStream);
				 
				 /// file end ///	 
				 
				 String fileName2="";
				 File file = null;
				 String path1=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("quotationdir"))+"/";
				 fileName2="PCO_"+QuotationNo+".pdf";
				 String finalPath=(path1+fileName2);
				   
			     file = new File(finalPath);
				 if(file.exists())
	    			{
					 	finalPath = (path1+fileName2);
	    				request.setAttribute("fileName",finalPath);
	    			}//end of if(file.exists())
				// JasperExportManager.exportReportToPdfStream(jasperPrint,boas);
				 
				 File filesave = new File(finalPath);  
				 if(QuotationNo !=null && ""!=QuotationNo)
				 {			     
			     byte[] data = new byte[(int) filesave.length()];
			     FileInputStream fis = new FileInputStream(filesave);
			     fis.read(data); //read file into bytes[]
			     fis.close();
				 swFinalQuoteVO.setFinalpolicydocYN("N");
				 swFinalQuoteVO.setPolcopyseqid(null);
				 swFinalQuoteVO.setQuotationNo(QuotationNo);
				
		
				 }
			
			 }//end of try
			 catch (JRException e)
			 {
				 e.printStackTrace();
			 }//end of catch(JRException e)
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }//end of catch (Exception e)
				
		
			 //return this.getForward(strFinalQuote, mapping, request);
				// return this.getForward(strGenerateQuote, mapping, request);
				 return mapping.findForward("reFwdToSearch");
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
    
    
    
	public ActionForward  doViewUploadDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws TTKException {
	    
		 ByteArrayOutputStream baos=null;
	    OutputStream sos = null;
	    FileInputStream fis = null; 
	    BufferedInputStream bis =null;
	    long policycopy_seq_id = 0;
	   
	  try{   
			
			String strFile	=	request.getParameter("filePath");
			String strFileName	=	"fileName.jpeg";
	    	String strFileNoRecords = TTKPropertiesReader.getPropertyValue("GlobalDownload")+"/noRecordsFound.pdf";

			  InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();

			  String rownum = request.getParameter("rownum");	
			  TableData tableData =TTKCommon.getTableData(request);
	    	
	    		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
				{
	    			SwFinalQuoteVO swFinalQuotetableVO=(SwFinalQuoteVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
	    		
	    			  policycopy_seq_id = swFinalQuotetableVO.getPolcopyseqid();
				}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			  
			  
			  ArrayList arraylist = new ArrayList();
			//  long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
			  arraylist	=	insuranceObject.getViewUploadDocs(policycopy_seq_id);
			  InputStream  iStream= (InputStream) arraylist.get(0);
			  
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
	
	 public ActionForward doAcceptQuotation(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    try{
	    	
	    setLinks(request);
	    DynaActionForm frmSwFinalQuote= (DynaActionForm)form;
	    SwFinalQuoteVO swFinalQuoteVO=null;  String successMsg="";String errorMsg ="";
	    String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
        String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
        TableData tableDataquo = TTKCommon.getTableData(request);
	    InsuranceManager insuranceObject=(InsuranceManager)this.getInsuranceManagerObject();
	    swFinalQuoteVO = (SwFinalQuoteVO)FormUtils.getFormValues(frmSwFinalQuote, this, mapping, request);
	    swFinalQuoteVO.setAddedBy((TTKCommon.getUserSeqId(request)));
	    String lowercaseFileExtn1 = "";
	    
		long group_seq_id=(long)request.getSession().getAttribute("GroupProfileSeqID");
		
		// UPLOAD FILE STARTS
		 FormFile formFile1 = (FormFile)frmSwFinalQuote.get("file1");
		 int fileSize = 3*1024*1024;
		    
		  if(formFile1==null||formFile1.getFileSize()<1){
	    	  TTKException ttkExc =new TTKException();
	    	  ttkExc.setMessage("error.file.data.empty");
	    	  throw ttkExc;
	      }
		    if(!("").equals(formFile1.getFileName()))
		   	{
		    	swFinalQuoteVO.setAttachmentname1(formFile1.getFileName());
		    	
		         lowercaseFileExtn1 = formFile1.getFileName().toLowerCase();checkfileExtention(lowercaseFileExtn1);
		        if(fileSize<=formFile1.getFileSize())
				{
		        	TTKException expTTK = new TTKException();
					expTTK.setMessage("error.Upload.common.size.2mb");
					throw expTTK;
				}
		   	}

		 swFinalQuoteVO.setSourceAttchments1(formFile1);	
		 swFinalQuoteVO.setFinalpolicydocYN("Y");
		 swFinalQuoteVO.setPolcopyseqid(swFinalQuoteVO.getFinalPolcopyseqid());
		
		 byte[] data=swFinalQuoteVO.getFinalQuotationdocs();
		 
		 String alertResult = insuranceObject.swIssuePolicy(swFinalQuoteVO);
		 if(alertResult == "" || alertResult == null)
		 {	 
		 successMsg="Quotation details Added Successfully";
		 Long lpolcopySeqId= insuranceObject.swSave_pol_copies(swFinalQuoteVO,data);		
		 }else{
			 errorMsg = alertResult;
		 }
		
		 swFinalQuoteVO= insuranceObject.swSelectQuotationdetails(swFinalQuoteVO);
		 frmSwFinalQuote= (DynaActionForm)FormUtils.setFormValues("frmSwFinalQuote",	swFinalQuoteVO, this, mapping, request);
		 request.setAttribute("successMsg",successMsg);
		 request.setAttribute("errorMsg",errorMsg);
		 
		  //if the page number or sort id is clicked
         if(!strPageID.equals("") || !strSortID.equals(""))
         {
         	if(!strPageID.equals(""))
             {
         		tableDataquo.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
                 return mapping.findForward(strFinalQuote);
             }///end of if(!strPageID.equals(""))
             else
             {
             	tableDataquo.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
             	tableDataquo.modifySearchData("sort");//modify the search data
             }//end of else
         }//end of if(!strPageID.equals("") || !strSortID.equals(""))
         else
         {
             //create the required grid table
         	tableDataquo.createTableInfo("Quotationtable",null);
         	//tableDataquo.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
         	tableDataquo.modifySearchData("search");
         }//end of else
         ArrayList alpolicyCopiesList= insuranceObject.getPolicyCopiesList(swFinalQuoteVO);
         tableDataquo.setData(alpolicyCopiesList, "search");
			//set the table data object to session
			request.getSession().setAttribute("tableDataquo",tableDataquo);
			if(alpolicyCopiesList.size() > 0){
			SwFinalQuoteVO	swfinalquote=(SwFinalQuoteVO)alpolicyCopiesList.get(alpolicyCopiesList.size()-(alpolicyCopiesList.size()));
			frmSwFinalQuote.set("finalQuotationNo",swfinalquote.getFinalQuotationNo());
			frmSwFinalQuote.set("finalPolcopyseqid",swfinalquote.getFinalPolcopyseqid().toString());
		
 	     }
		 
		 request.getSession().setAttribute("frmSwFinalQuote", frmSwFinalQuote);	
	 
		 return mapping.findForward(strFinalQuote);
		    
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
    
	 

	    
	    public ActionForward  doViewEmailUploadDocs(ActionMapping mapping,ActionForm form,HttpServletRequest request,
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
		    	  
		    	  SwFinalQuoteVO  swFinalQuoteVO = new SwFinalQuoteVO();
		    		long GroupProfileSeqID = (long)request.getSession().getAttribute("GroupProfileSeqID");
					swFinalQuoteVO.setLngGroupProfileSeqID(GroupProfileSeqID);
		    	  
		 		 ArrayList alpolicyCopiesList= insuranceObject.getPolicyCopiesList(swFinalQuoteVO);
		 			 
		 				if(alpolicyCopiesList.size() > 0){
		 				SwFinalQuoteVO	swFinalQuoteVOtable=(SwFinalQuoteVO)alpolicyCopiesList.get(alpolicyCopiesList.size()-(alpolicyCopiesList.size()));
		 				//System.out.println("outsideif =="+filename);
		 				if(filename.equalsIgnoreCase("file1")){
		 					 // System.out.println("inside if--"+swFinalQuoteVOtable.getAttachmentname1());
						  iStream= swFinalQuoteVOtable.getInputstreamdoc1();
						  fileExtn = swFinalQuoteVOtable.getAttachmentname1();
				    	  }
			    	  
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
			    			//System.out.println("inside xls file---"+lowercaseFileExtn);
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


private PolicyManager getPolicyManagerObject() throws TTKException
{
	PolicyManager policyManager = null;
	try
	{
		if(policyManager == null)
		{
			InitialContext ctx = new InitialContext();
			policyManager = (PolicyManager) ctx.lookup("java:global/TTKServices/business.ejb3/PolicyManagerBean!com.ttk.business.enrollment.PolicyManager");
		}//end of if(policyManager == null)
	}//end of try
	catch(Exception exp)
	{
		throw new TTKException(exp, "enrollment");
	}//end of catch
	return policyManager;
}//end getPolicyManagerObject()


}//end of InsureApproveConfiguration
	
