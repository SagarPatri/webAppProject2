package com.ttk.action.finance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.finance.CollectionsManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dao.impl.reports.TTKReportDataSource;
import com.ttk.dto.finance.CollectionDetailsVO;
import com.ttk.dto.finance.CollectionsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

public class CollectionsSearchAction extends TTKAction{
	private static Logger log = Logger.getLogger(CollectionsSearchAction.class);
	
	  private static final String strCollectionSearch="collectionsSearch";
	  private static final String strCollections="collections";
	  private static final String strCollectionPremiumDistribution="collectionPremiumDistribution";
	  private static final String strGoToCollection="goToCollection";
	  private static final String strCreditNote="CreditNote";
	    private static final String strBackward="Backward";
	    private static final String strForward="Forward";
	    private static final String strReportdisplay="reportdisplay";
	    private static final String strReportExp="report";
	  
	  public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
					
		 try {
			 setLinks(request);
	    		log.debug("Inside CollectionsSearchAction doDefault");
	    		DynaActionForm frmCollections =(DynaActionForm)form;
	    		String switchto = frmCollections.getString("switchTo");
	    		frmCollections.initialize(mapping);
	    		TableData tableData =TTKCommon.getTableData(request);//get the table data from session if exists
				tableData = new TableData();//create new table data object
				//create the required grid table
				tableData.createTableInfo("CollectionsSearchTable",new ArrayList());
				request.getSession().setAttribute("tableData",tableData);
			 
				frmCollections.set("totalPremiumSum", "");
				frmCollections.set("totalCollectionsSum", "");
				frmCollections.set("totalOutstandingSum", "");
				frmCollections.set("switchTo", switchto);
		    	
				request.getSession().setAttribute("frmCollectionsList",frmCollections);
				request.getSession().removeAttribute("policySeqId");
				return this.getForward(strCollectionSearch, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
	    	}//end of catch(Exception exp)
	 }
	
	  
	  public ActionForward doDefaultPremium(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
					
		 try {
			 setLinks(request);
	    		log.debug("Inside CollectionsSearchAction doDefaultPremium");
	    		DynaActionForm frmCollections =(DynaActionForm)form;
	    		 CollectionsVO collectionsVo = null;
	    		 CollectionsVO invoiceVo = new CollectionsVO();
	    		 CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
	    		TableData tableData =TTKCommon.getTableData(request);//get the table data from session if exists
	    		
	    		String policySeqId  = (String) request.getSession().getAttribute("policySeqId");
	    		if(policySeqId ==null || policySeqId ==""){
	    			
	    			TTKException expTTK = new TTKException();
	 				expTTK.setMessage("error.premium.distribution.policyNo.required"); 
	 				throw expTTK;
	    		}
	    		else{
	    			
	    			 invoiceVo = collectionsManagerObject.getPolicyDetails(policySeqId);
	    			 ArrayList<CollectionsVO> alCollectionsSearch =null;
	    			 frmCollections = setFormValues(invoiceVo,mapping,request);
	    			 frmCollections.set("reforward" , "edit");
	    			 String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	 	    		 String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	    			 if(!strPageID.equals("") || !strSortID.equals(""))
	 	    		{
	 	    			if(!strPageID.equals(""))
	 	    			{
	 	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
	 	    				return (mapping.findForward(strCollectionPremiumDistribution));
	 	    			}///end of if(!strPageID.equals(""))
	 	    			else
	 	    			{
	 	    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
	 	    				tableData.modifySearchData("sort");//modify the search data
	 	    			}//end of else
	 	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
	    			 else{
	 	    			tableData.createTableInfo("CollectionsPrimiumDistributionTable",new ArrayList());
	 	    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
	 	    			tableData.modifySearchData("search");
	 	    		}
	    			 
	    			 ArrayList<Object> alSearchParams = new ArrayList<Object>();
	    			 alSearchParams.add(policySeqId);
	    			    alCollectionsSearch= collectionsManagerObject.getInvoiceDetails(alSearchParams);
						
						if(alCollectionsSearch!= null && alCollectionsSearch.size()>0)
		    	            {
							tableData.setData(alCollectionsSearch, "search");
			    				 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsSearch.get(0);
			    				String invoiceSeqId =  collectionInfoVO.getInvoiceSeqId();
			    				invoiceVo.setInvoiceSeqId(invoiceSeqId);
			    				frmCollections.set("invoiceSeqId", invoiceVo.getInvoiceSeqId());
		    	            }
	    			    else{
	    			    	 alCollectionsSearch = new ArrayList<>();
	    			    	 tableData.setData(alCollectionsSearch, "search");
	    			    }
			    		
			    		    frmCollections.set("groupName", invoiceVo.getGroupName());
				    		frmCollections.set("groupId", invoiceVo.getGroupId());
				    		frmCollections.set("policyNum", invoiceVo.getPolicyNum());
				    		frmCollections.set("lineOfBussiness", invoiceVo.getLineOfBussiness());
				    		frmCollections.set("startDate", invoiceVo.getStartDate());
				    		frmCollections.set("endDate", invoiceVo.getEndDate());
			    		    frmCollections.set("totalPremiumSum", invoiceVo.getTotalPremiumSum());
			    		    frmCollections.set("totalCollectionsSum", invoiceVo.getTotalCollectionsSum());
			    		    frmCollections.set("totalOutstandingSum", invoiceVo.getTotalOutstandingSum());
			    		    frmCollections.set("policySeqId", policySeqId);
						request.getSession().setAttribute("frmCollectionsList",frmCollections);
						request.getSession().setAttribute("tableData",tableData);
						request.getSession().setAttribute("policySeqId",policySeqId);
						request.setAttribute("rownum", "0");
	    		}
	    		 
	    		
				/*tableData.createTableInfo("CollectionsPrimiumDistributionTable",new ArrayList());
				request.getSession().setAttribute("tableData",tableData);
				frmCollections.set("totalPremiumSum", "");
				frmCollections.set("totalCollectionsSum", "");
				frmCollections.set("totalOutstandingSum", "");*/
		    	
				//request.getSession().setAttribute("frmCollectionsList",frmCollections);
			 
				return this.getForward(strCollectionPremiumDistribution, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
	    	}//end of catch(Exception exp)
	 }	
	  
	  
	  public ActionForward doDefaultCollections(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		 try {
			 setLinks(request);
	    		log.debug("Inside CollectionsSearchAction goToCollections");
	    		DynaActionForm frmCollections =(DynaActionForm)form;
	    		frmCollections.initialize(mapping);
	    		
				 request.getSession().removeAttribute("tableData");
				 request.getSession().removeAttribute("frmCollectionsList");
	    		
	    		TTKException expTTK = new TTKException();
 				expTTK.setMessage("error.collection.policyNo.required"); 
 				throw expTTK;
	    		
	    		
				//return this.getForward(strGoToCollection, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
	    	}//end of catch(Exception exp)
	 }	 
	 public ActionForward doDefaultCreditNote(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		 try {
			 setLinks(request);
	    		log.debug("Inside CollectionsSearchAction doDefaultCreditNote");
	    		DynaActionForm frmCollections =(DynaActionForm)form;
	    		
	    		TTKException expTTK = new TTKException();
 				expTTK.setMessage("error.collection.credit.note"); 
 				throw expTTK;
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
	    	}//end of catch(Exception exp)
	 }	
	  
	  
	 public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		 try {
			 setLinks(request);
	    		log.debug("Inside CollectionsSearchAction doDefault");
	    		DynaActionForm frmCollectionsList =(DynaActionForm)form;
	    		CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
	    		TableData tableData =TTKCommon.getTableData(request);//get the table data from session if exists
				//create the required grid table
				String switchTo = frmCollectionsList.getString("switchTo");
				String groupName = frmCollectionsList.getString("groupName");
				String groupId = frmCollectionsList.getString("groupId");
				String policyNum = frmCollectionsList.getString("policyNum");
				String policyStatus = frmCollectionsList.getString("policyStatus");
				
				if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
	    		{
	    			((DynaActionForm)form).initialize(mapping);//reset the form data
	    		}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
	    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	    		//if the page number or sort id is clicked
	    		if(!strPageID.equals("") || !strSortID.equals(""))
	    		{
	    			if(!strPageID.equals(""))
	    			{
	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
	    				return (mapping.findForward(strCollectionSearch));
	    			}///end of if(!strPageID.equals(""))
	    			else
	    			{
	    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
	    				tableData.modifySearchData("sort");//modify the search data
	    			}//end of else
	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
	    		else{
	    			
	    			
	    			tableData.createTableInfo("CollectionsSearchTable",new ArrayList());
	    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
	    			tableData.modifySearchData("search");
	    		}
	    		CollectionsVO totalSumVo = null;
	    		ArrayList<CollectionsVO> alCollectionsSearch =null;
	    		if("COR".equals(switchTo)){
	    			 alCollectionsSearch= collectionsManagerObject.getCollectionsList(tableData.getSearchData());
	    			 totalSumVo = collectionsManagerObject.getTotalPremiumDetails(tableData.getSearchData());
	    			 frmCollectionsList = setFormValues(totalSumVo,mapping,request);
	    			 
	    			 if(alCollectionsSearch!= null && alCollectionsSearch.size()>0)
	    	            {
	    				 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsSearch.get(0);
	    				String policyseqid =  collectionInfoVO.getPolicySeqId();
	    				totalSumVo.setPolicySeqId(policyseqid);
	    				frmCollectionsList.set("policySeqId", totalSumVo.getPolicySeqId());
	    	            }
	    		}
	    		else{
	    			alCollectionsSearch = new ArrayList<>();
	    		}
	    			
	    		tableData.setData(alCollectionsSearch, "search");
	    		request.getSession().setAttribute("tableData",tableData);
	    		
	    		frmCollectionsList.set("totalPremiumSum", totalSumVo.getTotalPremiumSum());
		    	frmCollectionsList.set("totalCollectionsSum", totalSumVo.getTotalCollectionsSum());
		    	frmCollectionsList.set("totalOutstandingSum", totalSumVo.getTotalOutstandingSum());
		    	frmCollectionsList.set("groupName", groupName);
	    		frmCollectionsList.set("groupId", groupId);
		    	frmCollectionsList.set("policyNum", policyNum);
		    	frmCollectionsList.set("policyStatus", policyStatus);
		    	frmCollectionsList.set("switchTo", switchTo);
		    	request.getSession().setAttribute("policySeqId",totalSumVo.getPolicySeqId());
				request.getSession().setAttribute("frmCollectionsList",frmCollectionsList);
				return this.getForward(strCollectionSearch, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
	    	}//end of catch(Exception exp)
	 }	 
	 
	  private ArrayList<Object> populateSearchCriteria(DynaActionForm frmCollectionsList,HttpServletRequest request) throws TTKException
{
		    ArrayList<Object> alSearchParams = new ArrayList<Object>();
		    
		    String reforward = (String) frmCollectionsList.get("reforward");
		    String policyseqId = (String) frmCollectionsList.get("policySeqId");
		    
		    if("edit".equals(reforward)){
		    	 alSearchParams.add(frmCollectionsList.get("policySeqId").toString().trim());
		    }
		    else{
		    
		    alSearchParams.add(frmCollectionsList.get("switchTo").toString());
		    alSearchParams.add(frmCollectionsList.get("groupName").toString().trim());
		    alSearchParams.add(frmCollectionsList.get("groupId").toString().trim());
		    alSearchParams.add(frmCollectionsList.get("policyNum").toString().trim());
		    alSearchParams.add(frmCollectionsList.get("policyStatus"));
		   }
		  
		  
	return alSearchParams;
}
	  
	  
	  private CollectionsManager getCollectionManagerObject() throws TTKException
	    {
		  CollectionsManager collectionsManager = null;
	        try
	        {
	            if(collectionsManager == null)
	            {
	                InitialContext ctx = new InitialContext();
	                collectionsManager = (CollectionsManager)ctx.lookup("java:global/TTKServices/business.ejb3/CollectionsManagerBean!com.ttk.business.finance.CollectionsManager");
	            }//end of if(callCenterManager == null)
	        }//end of try
	        catch(Exception exp)
	        {
	            throw new TTKException(exp, "collections");
	        }//end of catch
	        return collectionsManager;
	    }//end getCallCenterManagerObject()
	  
	  
	  
	  public ActionForward doViewPolicy(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		  
		  try {
		    		log.debug("Inside CollectionsSearchAction doViewPolicy");
		    		setLinks(request);
		    		CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
		    		TableData tableData =TTKCommon.getTableData(request);
		    		 if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
		             	((DynaActionForm)form).initialize(mapping);//reset the form data
		             }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		    		 DynaActionForm frmCollections =(DynaActionForm)form;
		    		 CollectionsVO collectionsVo = null;
		    		 CollectionsVO invoiceVo = new CollectionsVO();
		    		 
		    		 CollectionDetailsVO collectiondetailsVo = new CollectionDetailsVO();
		    		 
		    		 
		    		 
		    		 if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals(""))  && request.getParameter("rownum") !=null )
		             {
		    			 String str = request.getParameter("rownum");
		    			 int rowcnt = Integer.parseInt(str);
		    			 
		    			 if(tableData.getData().size() > 0){
		    				 if(rowcnt >= tableData.getData().size()){
			    					TTKException expTTK = new TTKException();
			    	 				expTTK.setMessage("error.no.premium.distribution.list"); 
			    	 				throw expTTK;
		   		                   	}
		    				 collectionsVo = (CollectionsVO)tableData.getRowInfo(rowcnt); 
		    			 }
		    			 else{
		    					TTKException expTTK = new TTKException();
		    	 				expTTK.setMessage("error.no.premium.distribution.list"); 
		    	 				throw expTTK;
		    			 }
		    			 String PolicySeqId1="";
		    			 String PolicySeqId = collectionsVo.getPolicySeqId();
		    			 if(PolicySeqId ==null){
		    				 
		    				String plcySqId = (String) request.getSession().getAttribute("policySeqId");
		    				 
		    				 if(plcySqId==null){
		    					 PolicySeqId1="0";
		    				 }
		    				 else{
		    					 PolicySeqId1=plcySqId;
		    				 }
		    			 }
		    			 else{
		    				 PolicySeqId1=PolicySeqId;
		    			 }
		    			 
		    			 invoiceVo = collectionsManagerObject.getPolicyDetails(PolicySeqId1);
		    			 ArrayList<CollectionsVO> alCollectionsSearch =null;
		    			 frmCollections = setFormValues(invoiceVo,mapping,request);
		    			 String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		 	    		 String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		    			 if(!strPageID.equals("") || !strSortID.equals(""))
		 	    		{
		 	    			if(!strPageID.equals(""))
		 	    			{
		 	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
		 	    				return (mapping.findForward(strCollectionPremiumDistribution));
		 	    			}///end of if(!strPageID.equals(""))
		 	    			else
		 	    			{
		 	    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
		 	    				tableData.modifySearchData("sort");//modify the search data
		 	    			}//end of else
		 	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		    			 else{
		 	    			tableData.createTableInfo("CollectionsPrimiumDistributionTable",new ArrayList());
		 	    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
		 	    			tableData.modifySearchData("search");
		 	    		}
		    			 ArrayList<Object> alSearchParams = new ArrayList<Object>();
		    			 alSearchParams.add(PolicySeqId1);
		    			    alCollectionsSearch= collectionsManagerObject.getInvoiceDetails(alSearchParams);
							
							if(alCollectionsSearch!= null && alCollectionsSearch.size()>0)
			    	            {
								tableData.setData(alCollectionsSearch, "search");
				    				 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsSearch.get(0);
				    				String invoiceSeqId =  collectionInfoVO.getInvoiceSeqId();
				    				invoiceVo.setInvoiceSeqId(invoiceSeqId);
				    				frmCollections.set("invoiceSeqId", invoiceVo.getInvoiceSeqId());
				    				request.getSession().setAttribute("invoiceSeqId",invoiceVo.getInvoiceSeqId());
			    	            }
		    			    else{
		    			    	 alCollectionsSearch = new ArrayList<>();
		    			    	 tableData.setData(alCollectionsSearch, "search");
		    			    }
				    		
				    		    frmCollections.set("groupName", invoiceVo.getGroupName());
					    		frmCollections.set("groupId", invoiceVo.getGroupId());
					    		frmCollections.set("policyNum", invoiceVo.getPolicyNum());
					    		frmCollections.set("lineOfBussiness", invoiceVo.getLineOfBussiness());
					    		frmCollections.set("startDate", invoiceVo.getStartDate());
					    		frmCollections.set("endDate", invoiceVo.getEndDate());
				    		    frmCollections.set("totalPremiumSum", invoiceVo.getTotalPremiumSum());
				    		    frmCollections.set("totalCollectionsSum", invoiceVo.getTotalCollectionsSum());
				    		    frmCollections.set("totalOutstandingSum", invoiceVo.getTotalOutstandingSum());
				    		    frmCollections.set("policySeqId", PolicySeqId1);
				    		    frmCollections.set("invoiceSeqId", invoiceVo.getInvoiceSeqId());
							request.getSession().setAttribute("tableData",tableData);
							request.getSession().setAttribute("policySeqId",PolicySeqId1);
							request.getSession().setAttribute("frmCollectionsList",frmCollections);
							request.setAttribute("rownum", "0");
		             }
		    		 else{
		    			 return this.getForward(strCollectionPremiumDistribution, mapping, request);
		    		 }
					
		    	     	 frmCollections.set("switchTo", "COR");
						request.getSession().setAttribute("tableData",tableData);
				 
				 
					return this.getForward(strCollectionPremiumDistribution, mapping, request);
		    	}//end of try
		    	catch(TTKException expTTK)
		    	{
		    		return this.processExceptions(request,mapping,expTTK);
		    	}//end of catch(TTKException expTTK)
		    	catch(Exception exp)
		    	{
		    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
		    	}//end of catch(Exception exp)
	  }
	  
  public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			              HttpServletResponse response) throws Exception{
					try{
					log.debug("Inside the doForward method of CollectionsSearchAction");
					setLinks(request);
					//get the session bean from the bean pool for each excecuting thread
					CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
					TableData tableData = TTKCommon.getTableData(request);
					tableData.modifySearchData(strForward);
					ArrayList alCollectionsSearch =null;
						 alCollectionsSearch= collectionsManagerObject.getCollectionsList(tableData.getSearchData());
						 
					tableData.setData(alCollectionsSearch, strForward);
					request.getSession().setAttribute("tableData",tableData);
					return this.getForward(strCollectionSearch, mapping, request);
			}//end of try
			catch(TTKException expTTK)
			{
			return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
			return this.processExceptions(request, mapping, new TTKException(exp,strCollections));
			}//end of catch(Exception exp)
}  
	  
	  public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
              HttpServletResponse response) throws Exception{
			try{
			log.debug("Inside the doForward method of CollectionsSearchAction");
			setLinks(request);
			//get the session bean from the bean pool for each excecuting thread
			CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
			TableData tableData = TTKCommon.getTableData(request);
			tableData.modifySearchData(strBackward);
			ArrayList alCollectionsSearch =null;
			alCollectionsSearch= collectionsManagerObject.getCollectionsList(tableData.getSearchData());
				 
			tableData.setData(alCollectionsSearch, strBackward);
			request.getSession().setAttribute("tableData",tableData);
			return this.getForward(strCollectionSearch, mapping, request);
	}//end of try
	catch(TTKException expTTK)
	{
	return this.processExceptions(request, mapping, expTTK);
	}//end of catch(TTKException expTTK)
	catch(Exception exp)
	{
	return this.processExceptions(request, mapping, new TTKException(exp,strCollections));
	}//end of catch(Exception exp)
}  

	  private DynaActionForm setFormValues(CollectionsVO collectionsVo,ActionMapping mapping,
	    		HttpServletRequest request) throws TTKException
	    {
	        try
	        {
	            DynaActionForm frmCollectionsList = (DynaActionForm)FormUtils.setFormValues("frmCollectionsList",
	            		collectionsVo,this,mapping,request);
	           
	            return frmCollectionsList;
	        }
	        catch(Exception exp)
	        {
	            throw new TTKException(exp,strCollections);
	        }//end of catch
	    }//end of setFormValues
	  
	  public ActionForward doExportReport(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
				
		  try {
			  log.info("Inside the doExportReport method of CollectionsSearchAction");
	    		setLinks(request);
	    		 DynaActionForm frmCollections =(DynaActionForm)form;
	    			JasperReport mainJasperReport,emptyReport;
		    		TTKReportDataSource mainTtkReportDataSource = null;
		    		JasperPrint mainJasperPrint = null;
		    		// UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
		    		 CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
		    		 HashMap<String,String> hashMap = new HashMap<String,String>();
		    		 
		    		 String switchTo = frmCollections.getString("switchTo");
		    		 String groupName = frmCollections.getString("groupName");
		    		 String groupId = frmCollections.getString("groupId");
		    		 String policyNum = frmCollections.getString("policyNum");
		    		 String policyStatus = frmCollections.getString("policyStatus");
		    		 String reportType = request.getParameter("reportType");
		    		 
		    		 ArrayList<Object> alGenerateXL= new ArrayList<Object>();
		    		 alGenerateXL.add(switchTo);
		    		 alGenerateXL.add(groupName);
		    		 alGenerateXL.add(groupId);
		    		 alGenerateXL.add(policyNum);
		    		 alGenerateXL.add(policyStatus);
		    		 
		    		 String fileName = "reports/finance/CollectionsSearchList.jrxml";
		    		 String jrxmlfile=null;
			    		jrxmlfile=fileName;
		    		 
			    		emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
		    		 
			    		ByteArrayOutputStream boas=new ByteArrayOutputStream();
		    		 
			    		mainTtkReportDataSource = collectionsManagerObject.getExportReport(alGenerateXL);
			    		ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
			    		
			    		
			    		mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
			    		
			    		if (main_report_RS != null && main_report_RS.next()) {
		    				main_report_RS.beforeFirst();
		    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);
		    				  				
		    			} else {
		    				
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    			}
			    		
			    		if("EXCEL".equals(reportType)){
			    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		 				jExcelApiExporter.exportReport();	
			    		}
			    		
		 				request.setAttribute("boas",boas);
		 				request.setAttribute("reportType", reportType);
		 				response.setHeader("Content-Disposition", "attachement; filename = CollectionsReport.xls");
		 				//frmCollections.set("letterPath", reportLink);
		 				 return (mapping.findForward(strReportdisplay));
		 				//return this.getForward(strCollectionSearch, mapping, request);
		} 
		  
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}
	  }
	  
	  public ActionForward addCollection(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		  try {
			  log.debug("Inside CollectionsSearchAction addCollection");
			  
				setLinks(request);
	    		CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
	    		TableData tableData =TTKCommon.getTableData(request);
	    		 if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
	             	((DynaActionForm)form).initialize(mapping);//reset the form data
	             }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
	    		 DynaActionForm frmCollections =(DynaActionForm)form;;
	    		 CollectionsVO collectionsVo = null;
	    		 CollectionsVO invoiceVo = new CollectionsVO();
	    		//Object form =  request.getSession().getAttribute("frmCollectionsList");
	    		 DynaActionForm frmCollectionsList=(DynaActionForm)request.getSession().getAttribute("frmCollectionsList");
	    		 if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals(""))  && request.getParameter("rownum") !=null )
	             {
	    			 String str = request.getParameter("rownum");
	    			 int rowcnt = Integer.parseInt(str);
	    			 
	    			 if(tableData.getData().size() > 0){
	    				 
	    				if(rowcnt >= tableData.getData().size()){
	    					TTKException expTTK = new TTKException();
	    	 				expTTK.setMessage("error.no.collection.list"); 
	    	 				throw expTTK;
   		                   	}
	    				 collectionsVo = (CollectionsVO)tableData.getRowInfo(rowcnt); 
	    			 }
	    			 else{
	    				// collectionsVo = (CollectionsVO)tableData.getRowInfo(0); 
	    			
	    					TTKException expTTK = new TTKException();
	    	 				expTTK.setMessage("error.no.collection.list"); 
	    	 				throw expTTK;
	    				 
	    			 }
	    			 
	    			 String invoiceseqid ;
	    			 String invoiceNum;
	    			 String invoiceseqid1 = collectionsVo.getInvoiceSeqId(); 
		    		 String invoiceNum1 = collectionsVo.getInvoiceNo();
		    		 
		    		if(invoiceseqid1 ==null)
		    			invoiceseqid = frmCollectionsList.getString("invoiceSeqId");
		    		else
		    			invoiceseqid = invoiceseqid1;
		    		 
		    		if(invoiceNum1 ==null)
		    			invoiceNum = frmCollectionsList.getString("invoiceNo");
		    		else
		    			invoiceNum = invoiceNum1;
		    		 
		    		String invoiceDate;
		    		String invoiceDate1 = (String) frmCollectionsList.get("dueDate");
		    		String invoiceDate2 = collectionsVo.getDueDate();
		    		if(invoiceDate2==null||invoiceDate2=="")
		    			invoiceDate = invoiceDate1;
		    		else
		    			invoiceDate = invoiceDate2;
		    		
		    		
		    		
		    		String dueDate;
		    		String dueDate1 = (String) frmCollectionsList.get("invoiceDate");
		    		String dueDate2 = collectionsVo.getInvoiceDate();
		    		if(dueDate2==null||dueDate2=="")
		    			dueDate = dueDate1;
		    		else
		    			dueDate = dueDate2;
		    		
		    		
		    		
		    		
		    		String invoiceAmnt;
		    		String invoiceAmnt1 = (String) frmCollectionsList.get("invoiceAmount");
		    		String invoiceAmnt2 = collectionsVo.getInvoiceAmount();
		    		if(invoiceAmnt2==null||invoiceAmnt2=="")
		    			invoiceAmnt = invoiceAmnt1;
		    		else
		    			invoiceAmnt = invoiceAmnt2;
		    		
		    		String totcolQar;
		    		String totcolQar1 = (String) frmCollectionsList.get("totalCollectionsQAR");
		    		String totcolQar2 = collectionsVo.getTotalCollectionsQAR();
		    		if(totcolQar2==null||totcolQar2=="")
		    			totcolQar = totcolQar1;
		    		else
		    			totcolQar = totcolQar2;
		    		
		    		String totOutQar;
		    		String totOutQar1 = (String) frmCollectionsList.get("totalOutstandingQAR");
		    		String totOutQar2 = collectionsVo.getTotalOutstandingQAR();
		    		if(totOutQar2==null||totOutQar2=="")
		    			totOutQar = totOutQar1;
		    		else
		    			totOutQar = totOutQar2;
	    			 
	    			 frmCollections = setFormValues(collectionsVo,mapping,request);
	    			 frmCollections.set("groupName", frmCollectionsList.get("groupName"));
	    			 frmCollections.set("groupId", frmCollectionsList.get("groupId"));
	    			 frmCollections.set("policyNum", frmCollectionsList.get("policyNum"));
	    			 frmCollections.set("lineOfBussiness", frmCollectionsList.get("lineOfBussiness"));
	    			 frmCollections.set("startDate", frmCollectionsList.get("startDate"));
	    			 frmCollections.set("endDate", frmCollectionsList.get("endDate"));
	    			 frmCollections.set("invoiceSeqId", invoiceseqid);
	    			 frmCollections.set("invoiceNo", invoiceNum);
	    			 frmCollections.set("invoiceDate", invoiceDate);
	    			 frmCollections.set("dueDate", dueDate);
	    			 frmCollections.set("invoiceAmount", invoiceAmnt);
	    			 frmCollections.set("totalCollectionsQAR", totcolQar);
	    			 frmCollections.set("totalOutstandingQAR", totOutQar);
	    			 frmCollections.set("totalOutstandingAmnt", totOutQar);
	    			 request.getSession().setAttribute("totalOutstandingQAR",totOutQar);
		    		 
		    			// invoiceVo = collectionsManagerObject.getPolicyDetails(policyseqid);
		    			 ArrayList<CollectionsVO> alCollectionsSearch =null;
		    			// frmCollections = setFormValues(invoiceVo,mapping,request);
		    			 String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		 	    		 String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		    			 if(!strPageID.equals("") || !strSortID.equals(""))
		 	    		{
		 	    			if(!strPageID.equals(""))
		 	    			{
		 	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
		 	    				return (mapping.findForward(strGoToCollection));
		 	    			}///end of if(!strPageID.equals(""))
		 	    			else
		 	    			{
		 	    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
		 	    				tableData.modifySearchData("sort");//modify the search data
		 	    			}//end of else
		 	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		    			 else{
		 	    			tableData.createTableInfo("CollectionDetailsTable",new ArrayList());
		 	    			//tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
		 	    			tableData.modifySearchData("search");
		 	    		}
		    			 
		    			 
		    			 ArrayList<Object> alSearchParams = new ArrayList<Object>();
		    			// alSearchParams.add(policyseqid);
		    			 alSearchParams.add(invoiceseqid);
		    			 alSearchParams.add(invoiceNum);
		    			    alCollectionsSearch= collectionsManagerObject.getCollectionDetailsList(alSearchParams);
							
							if(alCollectionsSearch!= null && alCollectionsSearch.size()>0)
			    	            {
								    tableData.setData(alCollectionsSearch, "search");
				    				 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsSearch.get(0);
				    				invoiceVo.setInvoiceSeqId(invoiceseqid);
				    				frmCollections.set("invoiceSeqId", invoiceVo.getInvoiceSeqId());
				    				frmCollections.set("collectionsSeqId", invoiceVo.getCollectionsSeqId());
				    				request.getSession().setAttribute("collectionsSeqId",invoiceVo.getCollectionsSeqId());
				    				
				    				
				    				request.getSession().setAttribute("invoiceSeqId",invoiceseqid);
									request.getSession().setAttribute("hiddenInvoiceNo",invoiceNum);
				    				
			    	            }
		    			    else{
		    			    	 alCollectionsSearch = new ArrayList<>();
		    			    	 tableData.setData(alCollectionsSearch, "search");
		    			    }
							
							frmCollections.set("amountReceivedQAR", "");
							frmCollections.set("receivedDate", "");
							frmCollections.set("transactionRef", "");
							frmCollections.set("amountDueQAR", "");
							
							request.getSession().setAttribute("tableData",tableData);
	    			 
							 request.getSession().setAttribute("frmCollectionsList",frmCollections);
	    			 
	             }
	    		 return this.getForward(strGoToCollection, mapping, request);
		}
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
	    	}//end of catch(Exception exp)
	  }
	  
	  public ActionForward saveCollection(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		  
		  try {
			  log.info("Inside CollectionsSearchAction saveCollection");
			  
			    setLinks(request);
	    		CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
	    		
	    		DynaActionForm frmCollections =(DynaActionForm)form;
	    		CollectionsVO collectionsVO = new CollectionsVO();
	    		 CollectionsVO invoiceVo = new CollectionsVO();
	    		 collectionsVO= (CollectionsVO)FormUtils.getFormValues(frmCollections,this,mapping,request);
	    		 
	    		 DynaActionForm frmCollectionsDetails = (DynaActionForm) request.getSession().getAttribute("frmCollectionsList");
	    		 String invoiceSeqId="";;
	    		 String invoiceNumber="";;
	    		 String invoiceSeqId2= frmCollectionsDetails.getString("invoiceSeqId");
	    		 String  invoiceNumber2= frmCollectionsDetails.getString("invoiceNo");
	    		  
	    		  
	    		  String  invoiceSeqId1= (String) request.getSession().getAttribute("invoiceSeqId");
	    		  String  invoiceNumber1= (String) request.getSession().getAttribute("hiddenInvoiceNo");
	    		 
	    		 if(invoiceSeqId2==null||invoiceSeqId2=="")
	    			 invoiceSeqId = invoiceSeqId1;
	    		 else
	    			 invoiceSeqId=invoiceSeqId2;
	    		 
	    		  
	    		 if(invoiceNumber2==null||invoiceNumber2=="")
	    			 invoiceNumber = invoiceNumber1;
	    		 else
	    			 invoiceNumber=invoiceNumber2;
	    		 
	    		 
	    	 	String outStandingAmt= (String) request.getSession().getAttribute("totalOutstandingQAR");
	    	
	    		 collectionsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
	    		 
	    		//FETCH FILE FROM LOCAL SYSTEM
	 			FormFile formFile = null;
	 			formFile = (FormFile)frmCollections.get("uploadFile");
	 			InputStream inputStream = null;
	 			int formFileSize		=	0;
	 			String finalPath		=	"";
	 			FileOutputStream outputStream = null;
	 			if(formFile!=null && formFile.getFileSize()>0) {
	 				inputStream = formFile.getInputStream();
	 				formFileSize	=	formFile.getFileSize();

	 				//COPYNG FILE TO SERVER FOR BACKUP
	 	    		String path=TTKCommon.checkNull(TTKPropertiesReader.getPropertyValue("collectionDocsUpload"));
	 		        File folder = new File(path);
	 				if(!folder.exists()){
	 					folder.mkdir();
	 				}
	 				finalPath=(formFile.toString().substring(0, formFile.toString().indexOf("."))+
	 						new SimpleDateFormat("dd-MM-yyyy-hh-mm-SSS").format(new Date())+
	 						formFile.toString().substring(formFile.toString().indexOf(".")));

	 				//above substring to get the extension of the file  at the end
	 				outputStream = new FileOutputStream(new File(path+finalPath));
	 				outputStream.write(formFile.getFileData());//Uploaded file backUp

	 			}//if form file is not null
	    		 
	 			   Long collectionSeqId=collectionsManagerObject.saveCollection(collectionsVO,inputStream,formFileSize,finalPath);
	    			if(collectionSeqId > 0){
	    				 frmCollections.set("reforward", "SAVEYN");
	    			}
	 			   
	 			   
	 			   
	    			 request.setAttribute("updated","message.savedSuccessfully"); 
	    			
	    			 frmCollections.set("groupName", "");
	    			 frmCollections.set("groupId", "");
	    			 frmCollections.set("policyNum", "");
	    			 frmCollections.set("lineOfBussiness", "");
	    			 frmCollections.set("startDate", "");
	    			 frmCollections.set("endDate", "");
	    			 frmCollections.set("invoiceNo", "");
	    			 frmCollections.set("invoiceDate", "");
	    			 frmCollections.set("dueDate", "");
	    			 frmCollections.set("invoiceAmount", "");
	    			 frmCollections.set("totalCollectionsQAR", "");
	    			 frmCollections.set("totalOutstandingQAR", "");
	    			 frmCollections.set("amountReceivedQAR", "");
	    			 frmCollections.set("receivedDate", "");
	    			 frmCollections.set("transactionRef", "");
	    			 frmCollections.set("amountDueQAR", "");
	    			 frmCollections.set("fileName", finalPath);
	    			 frmCollections.set("collectionsSeqId", collectionSeqId.toString());
	    			 TableData tableData =TTKCommon.getTableData(request);
		    		 String policyseqid = (String) request.getSession().getAttribute("policySeqId");
		    		 String invoiceseqid = collectionsVO.getInvoiceSeqId();
		    		 String invoiceNo = frmCollections.getString("invoiceNo");
		    		 
		    			 ArrayList<CollectionsVO> alCollectionsSearch =null;
		    			// frmCollections = setFormValues(invoiceVo,mapping,request);
		    			 String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		 	    		 String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		    			 if(!strPageID.equals("") || !strSortID.equals(""))
		 	    		{
		 	    			if(!strPageID.equals(""))
		 	    			{
		 	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
		 	    				return (mapping.findForward(strGoToCollection));
		 	    			}///end of if(!strPageID.equals(""))
		 	    			else
		 	    			{
		 	    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
		 	    				tableData.modifySearchData("sort");//modify the search data
		 	    			}//end of else
		 	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		    			 else{
		 	    			tableData.createTableInfo("CollectionDetailsTable",new ArrayList());
		 	    			tableData.modifySearchData("search");
		 	    		}
		    			 
		    			 ArrayList<Object> alSearchParams = new ArrayList<Object>();
		    			 alSearchParams.add(invoiceSeqId);
		    			 alSearchParams.add(invoiceNumber);
		    			    alCollectionsSearch= collectionsManagerObject.getCollectionDetailsList(alSearchParams);
							
							if(alCollectionsSearch!= null && alCollectionsSearch.size()>0)
			    	            {
								tableData.setData(alCollectionsSearch, "search");
								 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsSearch.get(0);
				    				invoiceVo.setInvoiceSeqId(invoiceseqid);
				    				frmCollections.set("invoiceSeqId", invoiceVo.getInvoiceSeqId());
				    				frmCollections.set("collectionsSeqId", collectionInfoVO.getCollectionsSeqId().toString());
								
								request.getSession().setAttribute("invoiceSeqId",invoiceSeqId);
								request.getSession().setAttribute("hiddenInvoiceNo",invoiceNumber);
								
			    	            }
		    			    else{
		    			    	 alCollectionsSearch = new ArrayList<>();
		    			    }
	    			 
							 tableData.setData(alCollectionsSearch, "search");
						  	request.getSession().setAttribute("tableData",tableData);
	    			 
	    			 request.getSession().setAttribute("frmCollectionsList",frmCollections);
			  
			  return this.getForward(strGoToCollection, mapping, request);
			}
			  catch(TTKException expTTK)
		    	{
		    		return this.processExceptions(request,mapping,expTTK);
		    	}//end of catch(TTKException expTTK)
		    	catch(Exception exp)
		    	{
		    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
		    	}//end of catch(Exception exp)
	  }
	  
	  public ActionForward doReverse(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		  
		  try {
			  log.info("Inside CollectionsSearchAction doReverse");
			  
			    setLinks(request);
	    		CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
	    		
	    		DynaActionForm frmCollections =(DynaActionForm)form;
	    		CollectionsVO collectionsVO = new CollectionsVO();
	    		 collectionsVO= (CollectionsVO)FormUtils.getFormValues(frmCollections,this,mapping,request);
	    		 String collectionsSeqId="";
	    		 String strCollectionsSeqId = request.getParameter("collectionsSeqId");
	    		 String deletionRemarks = request.getParameter("deletionRemarks");
	    		 
	    		 
	    		 if(strCollectionsSeqId==""){
	    			 collectionsSeqId="0"; 
	    		 }
	    		 else
	    			 collectionsSeqId=strCollectionsSeqId; 
	    		 
	    		 collectionsVO.setCollectionsSeqId(Long.parseLong(collectionsSeqId));
	    		 collectionsVO.setDeletionRemarks(deletionRemarks);
	    		 collectionsVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
	    		 
	    		 if(collectionsSeqId!=null){
	    			 int reverseTransaction=collectionsManagerObject.doReverseTrasaction(collectionsVO);
	    			 if(reverseTransaction >0 ){
	    				 request.setAttribute("updated1","message.reverse"); 
	    			 }
	    		 }
	    		 
	    		 String invoiceSeqId;
	    		 String invoiceNumber;
	    			 invoiceSeqId= (String) request.getSession().getAttribute("invoiceSeqId");
		    		  invoiceNumber= (String) request.getSession().getAttribute("hiddenInvoiceNo");
	    		 
	    		 TableData tableData =TTKCommon.getTableData(request);
	    		 CollectionsVO invoiceVo = new CollectionsVO();
	    		 
	    		 String policyseqid = (String) request.getSession().getAttribute("policySeqId");
	    		 String invoiceseqid = collectionsVO.getInvoiceSeqId();
	    		 String invoiceNo = frmCollections.getString("invoiceNo");
	    		 
	    			 ArrayList<CollectionsVO> alCollectionsSearch =null;
	    			 ArrayList<CollectionsVO> alCollectionsTotalList =null;
	    			 String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	 	    		 String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	    			 if(!strPageID.equals("") || !strSortID.equals(""))
	 	    		{
	 	    			if(!strPageID.equals(""))
	 	    			{
	 	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
	 	    				return (mapping.findForward(strGoToCollection));
	 	    			}///end of if(!strPageID.equals(""))
	 	    			else
	 	    			{
	 	    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
	 	    				tableData.modifySearchData("sort");//modify the search data
	 	    			}//end of else
	 	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
	    			 else{
	 	    			tableData.createTableInfo("CollectionDetailsTable",new ArrayList());
	 	    			//tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
	 	    			tableData.modifySearchData("search");
	 	    		}
	    			 
	    			 ArrayList<Object> alSearchParams = new ArrayList<Object>();
	    			 alSearchParams.add(invoiceSeqId);
	    			 alSearchParams.add(invoiceNumber);
	    			    alCollectionsSearch= collectionsManagerObject.getCollectionDetailsList(alSearchParams);
	    			    
	    			    
	    			    alCollectionsTotalList= collectionsManagerObject.getCollectionTotalDetailsList(alSearchParams);
	    			    
	    			    if(alCollectionsTotalList!= null && alCollectionsTotalList.size()>0)
	    	            {
	    			    	 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsTotalList.get(0);
	    			    	 frmCollections.set("invoiceAmount", collectionInfoVO.getInvoiceAmount());
	    			    	 frmCollections.set("totalCollectionsQAR", collectionInfoVO.getTotalCollectionsQAR());
	    			    	 frmCollections.set("totalOutstandingQAR", collectionInfoVO.getTotalOutstandingAmnt());
	    			    	 frmCollections.set("totalOutstandingAmnt", collectionInfoVO.getTotalOutstandingAmnt());
	    			    	 
	    			    	 
	    			    	 frmCollections.set("invoiceNo", invoiceNumber);
	    			    	 frmCollections.set("groupName", collectionInfoVO.getGroupName());
	    			    	 frmCollections.set("groupId", collectionInfoVO.getGroupId());
	    			    	 frmCollections.set("policyNum", collectionInfoVO.getPolicyNum());
	    			    	 frmCollections.set("lineOfBussiness", collectionInfoVO.getLineOfBussiness());
	    			    	 frmCollections.set("startDate", collectionInfoVO.getStartDate());
	    			    	 frmCollections.set("endDate", collectionInfoVO.getEndDate());
	    			    	 frmCollections.set("invoiceDate", collectionInfoVO.getDueDate());
	    			    	 frmCollections.set("dueDate", collectionInfoVO.getInvoiceDate());
	    			    	 
	    	            }
						
						if(alCollectionsSearch!= null && alCollectionsSearch.size()>0)
		    	            {
							tableData.setData(alCollectionsSearch, "search");
							 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsSearch.get(0);
			    				invoiceVo.setInvoiceSeqId(invoiceseqid);
			    				frmCollections.set("invoiceSeqId", invoiceVo.getInvoiceSeqId());
			    				frmCollections.set("collectionsSeqId", collectionInfoVO.getCollectionsSeqId().toString());
							request.getSession().setAttribute("collectionsSeqId",collectionInfoVO.getCollectionsSeqId().toString());
		    	            }
	    			    else{
	    			    	 alCollectionsSearch = new ArrayList<>();
	    			    }
						 tableData.setData(alCollectionsSearch, "search");
					  	request.getSession().setAttribute("tableData",tableData);
    			 
					  	request.getSession().setAttribute("frmCollectionsList",frmCollections);
		  
		  return this.getForward(strGoToCollection, mapping, request);
		}
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
	    	}//end of catch(Exception exp)
	  }
	  
	  public ActionForward doBackCollection(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception {
		  try {
		    		log.debug("Inside CollectionsSearchAction doBackCollection");
		    		setLinks(request);
		    		CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
		    		TableData tableData =TTKCommon.getTableData(request);
		    		 if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
		             	((DynaActionForm)form).initialize(mapping);//reset the form data
		             }//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
		    		 DynaActionForm frmCollections =(DynaActionForm)form;
		    		 CollectionsVO collectionsVo = null;
		    		 CollectionsVO invoiceVo = new CollectionsVO();
		    		 
		    		 CollectionDetailsVO collectiondetailsVo = new CollectionDetailsVO();
		    		 
		    		 String policyseqid = (String) request.getSession().getAttribute("policySeqId");
		    			 
		    			 invoiceVo = collectionsManagerObject.getPolicyDetails(policyseqid);
		    			 ArrayList<CollectionsVO> alCollectionsSearch =null;
		    			 frmCollections = setFormValues(invoiceVo,mapping,request);
		    			 String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
		 	    		 String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
		    			 if(!strPageID.equals("") || !strSortID.equals(""))
		 	    		{
		 	    			if(!strPageID.equals(""))
		 	    			{
		 	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
		 	    				return (mapping.findForward(strCollectionPremiumDistribution));
		 	    			}///end of if(!strPageID.equals(""))
		 	    			else
		 	    			{
		 	    				tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
		 	    				tableData.modifySearchData("sort");//modify the search data
		 	    			}//end of else
		 	    		}//end of if(!strPageID.equals("") || !strSortID.equals(""))
		    			 else{
		 	    			tableData.createTableInfo("CollectionsPrimiumDistributionTable",new ArrayList());
		 	    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
		 	    			tableData.modifySearchData("search");
		 	    		}
		    			 ArrayList<Object> alSearchParams = new ArrayList<Object>();
		    			 alSearchParams.add(policyseqid);
		    			 
		    			    alCollectionsSearch= collectionsManagerObject.getInvoiceDetails(alSearchParams);
							
							if(alCollectionsSearch!= null && alCollectionsSearch.size()>0)
			    	            {
								tableData.setData(alCollectionsSearch, "search");
				    				 CollectionsVO collectionInfoVO=(CollectionsVO)alCollectionsSearch.get(0);
				    				String invoiceSeqId =  collectionInfoVO.getInvoiceSeqId();
				    				invoiceVo.setInvoiceSeqId(invoiceSeqId);
				    				frmCollections.set("invoiceSeqId", invoiceVo.getInvoiceSeqId());
			    	            }
		    			    else{
		    			    	 alCollectionsSearch = new ArrayList<>();
		    			    	 tableData.setData(alCollectionsSearch, "search");
		    			    }
				    		
				    		    frmCollections.set("groupName", invoiceVo.getGroupName());
					    		frmCollections.set("groupId", invoiceVo.getGroupId());
					    		frmCollections.set("policyNum", invoiceVo.getPolicyNum());
					    		frmCollections.set("lineOfBussiness", invoiceVo.getLineOfBussiness());
					    		frmCollections.set("startDate", invoiceVo.getStartDate());
					    		frmCollections.set("endDate", invoiceVo.getEndDate());
				    		    frmCollections.set("totalPremiumSum", invoiceVo.getTotalPremiumSum());
				    		    frmCollections.set("totalCollectionsSum", invoiceVo.getTotalCollectionsSum());
				    		    frmCollections.set("totalOutstandingSum", invoiceVo.getTotalOutstandingSum());
				    		    frmCollections.set("policySeqId", policyseqid);
							request.getSession().setAttribute("frmCollectionsList",frmCollections);
							request.getSession().setAttribute("tableData",tableData);
							request.getSession().setAttribute("policySeqId",policyseqid);
							request.setAttribute("rownum", "0");
					
		    	     	 frmCollections.set("switchTo", "COR");
						request.getSession().setAttribute("tableData",tableData);
				 
					return this.getForward(strCollectionPremiumDistribution, mapping, request);
		    	}//end of try
		    	catch(TTKException expTTK)
		    	{
		    		return this.processExceptions(request,mapping,expTTK);
		    	}//end of catch(TTKException expTTK)
		    	catch(Exception exp)
		    	{
		    		return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
		    	}//end of catch(Exception exp)
	  
}

	  public ActionForward doDownloadCollections(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
		  try {
			  log.info("Inside the doExportReport method of CollectionsSearchAction");
	    		setLinks(request);
	    		 DynaActionForm frmCollections =(DynaActionForm)form;
	    			JasperReport mainJasperReport,emptyReport;
		    		TTKReportDataSource mainTtkReportDataSource = null;
		    		JasperPrint mainJasperPrint = null;
		    		 CollectionsManager collectionsManagerObject=this.getCollectionManagerObject();
		    		 HashMap<String,String> hashMap = new HashMap<String,String>();
		    		 
		    		 String policySeqId = frmCollections.getString("policySeqId");
		    	
		    		 String reportType = request.getParameter("reportType");
		    		 
		    		 ArrayList<Object> alGenerateXL= new ArrayList<Object>();
		    		 alGenerateXL.add(policySeqId);
		    		 
		    		 String fileName = "reports/finance/CollectionDetailsReport.jrxml";
		    		 String jrxmlfile=null;
			    		jrxmlfile=fileName;
		    		 
			    		emptyReport = JasperCompileManager.compileReport("generalreports/EmptyReprot.jrxml");
			    		ByteArrayOutputStream boas=new ByteArrayOutputStream();
			    		mainTtkReportDataSource = collectionsManagerObject.downLoadCollectionDtls(alGenerateXL);
			    		ResultSet main_report_RS = mainTtkReportDataSource.getResultData();
			    		mainJasperReport = JasperCompileManager.compileReport(jrxmlfile);
			    		
			    		if (main_report_RS != null && main_report_RS.next()) {
		    				main_report_RS.beforeFirst();
		    				mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, hashMap, mainTtkReportDataSource);
		    			} else {
		    				
		    				mainJasperPrint = JasperFillManager.fillReport(emptyReport,hashMap, new JREmptyDataSource());
		    			}
			    		
			    		if("EXCEL".equals(reportType)){
			    		JRXlsExporter jExcelApiExporter = new JRXlsExporter();
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,mainJasperPrint);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, boas);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		 				jExcelApiExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		 				jExcelApiExporter.exportReport();	
			    		}
			    		
		 				request.setAttribute("boas",boas);
		 				request.setAttribute("reportType", reportType);
		 				response.setHeader("Content-Disposition", "attachement; filename = CollectionDetailsReport.xls");
		 				 return (mapping.findForward(strReportdisplay));
		} 
		  catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request, mapping, expTTK);
	    	}
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request, mapping, new TTKException(exp, strReportExp));
	    	}
	  }
	  
	  public ActionForward downloadInvoice(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			  HttpServletResponse response) throws TTKException{
			try{
				setLinks(request);
				log.debug("Inside CollectionsSearchAction : doViewFile");
				String strBankType = "";
				TableData  tableData =TTKCommon.getTableData(request);
				strBankType = ((CollectionsVO)tableData.getRowInfo(Integer.parseInt(
																		request.getParameter("rownum")))).getBatchName();
				File file = null;
				
				String refwd = request.getParameter("reforward");
					
				if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
				{
					if("pdf".equals(refwd)){
						String strAuthpdf = TTKPropertiesReader.getPropertyValue("Invoicerptdir")+strBankType+".pdf";
						file = new File(strAuthpdf);
						if(file.exists())
						{
							strAuthpdf = TTKPropertiesReader.getPropertyValue("Invoicerptdir")+strBankType+".pdf";
							request.setAttribute("fileName",strAuthpdf);
							request.setAttribute("reforward",refwd);
						}//end of if(file.exists())
					}
					else if("xls".equals(refwd)){
						String strAuthpdf = TTKPropertiesReader.getPropertyValue("Invoicerptdir")+strBankType+".xls";
						file = new File(strAuthpdf);
						if(file.exists())
						{
							strAuthpdf = TTKPropertiesReader.getPropertyValue("Invoicerptdir")+strBankType+".xls";
							request.setAttribute("fileName",strAuthpdf);
							request.setAttribute("reforward",refwd);
						}//end of if(file.exists())
					}//end else
				
				}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
				
				//return (mapping.findForward(strReportdisplay));
				
				return this.getForward(strCollectionPremiumDistribution, mapping, request);//finally return to the grid screen
			}//end of try
			catch(TTKException expTTK)
			{
				return this.processExceptions(request,mapping,expTTK);
			}//end of catch(ETTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
			}//end of catch(Exception exp)
		}
	  
	  public ActionForward doViewInvoiceFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			try{
				setLinks(request);
				log.debug("Inside CollectionsSearchAction : doViewInvoiceFile");
				return (mapping.findForward(strReportdisplay));
			}
			catch(TTKException expTTK)
			{
				return this.processExceptions(request,mapping,expTTK);
			}//end of catch(ETTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request,mapping,new TTKException(exp,strCollections));
			}//end of catch(Exception exp)
		}//end of doViewInvoiceXL(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse

}
