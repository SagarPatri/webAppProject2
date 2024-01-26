package com.ttk.action.finance;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.finance.FloatAccountManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.finance.ChequeVO;

public class ViewManualChequePrintingsAction extends TTKAction{
	
	 private static Logger log = Logger.getLogger(ViewManualChequePrintingsAction.class );
	  private static final String strchequeslistdetails="manualChequeList";
	  private static final String strBank="bank";
	  private static final String strBackward="Backward";
	  private static final String strForward="Forward";
	  private static final String strReportdisplay="reportdisplay";
	  private static final String strReportExp="report";
	
	  public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		setLinks(request);
	    		log.debug("Inside ViewManualChequePrintingsAction doDefault");
	    		if(TTKCommon.getWebBoardId(request) == null)
	            {
	                TTKException expTTK = new TTKException();
	                expTTK.setMessage("error.floatno.required");
	                throw expTTK;
	            }//end of if(TTKCommon.getWebBoardId(request) == null)
	    		TableData  tableData =TTKCommon.getTableData(request);
	    		//create new table data object
	    		tableData = new TableData();
	    		//create the required grid table
	    		tableData.createTableInfo("PaymentBatchTable",new ArrayList());
	    		request.getSession().setAttribute("tableData",tableData);
	    		((DynaActionForm)form).initialize(mapping);//reset the form data
	    		request.getSession().setAttribute("searchparam", null);
	    		return this.getForward(strchequeslistdetails, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(ETTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
	    	}//end of catch(Exception exp)
	    }//end of Default(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	
	
	
	  
	  
	  
	  public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		setLinks(request);
	    		log.info("Inside ViewManualChequePrintingsAction doSearch");
	    		TableData  tableData =TTKCommon.getTableData(request);
	    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
	    		String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	    		String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	    		//if the page number or sort id is clicked
	    		if(!strPageID.equals("") || !strSortID.equals(""))
	    		{
	    			if(!strPageID.equals(""))
	    			{
	    				log.debug("PageId   :"+TTKCommon.checkNull(request.getParameter("pageId")));
	    				tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
	    				return (mapping.findForward(strchequeslistdetails));
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
	    			tableData.createTableInfo("PaymentBatchTable",null);
	    			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
	    			tableData.setSortColumnName("BATCH_DATE");
	    			tableData.setSortOrder("DESC");
	    			tableData.setSortData("0101");
	    			tableData.modifySearchData("search");
	    		}//end of else

	    		ArrayList alTransaction= floatAccountManagerObject.getViewManualChequesList(tableData.getSearchData());
	    		tableData.setData(alTransaction, "search");
	    		//set the table data object to session
	    		request.getSession().setAttribute("tableData",tableData);
	    		//finally return to the grid screen
			return this.getForward(strchequeslistdetails, mapping, request);
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(ETTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
	    	}//end of catch(Exception exp)
	    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	  
	  
	  
	    private FloatAccountManager getfloatAccountManagerObject() throws TTKException
	    {
	        FloatAccountManager floatAccountManager = null;
	        try
	        {
	            if(floatAccountManager == null)
	            {
	                InitialContext ctx = new InitialContext();
	                floatAccountManager = (FloatAccountManager) ctx.lookup("java:global/TTKServices/business.ejb3/FloatAccountManagerBean!com.ttk.business.finance.FloatAccountManager");
	            }//end of if(contactManager == null)
	        }//end of try
	        catch(Exception exp)
	        {
	            throw new TTKException(exp, "floataccount");
	        }//end of catch
	        return floatAccountManager;
	    }//end getfloatAccountManagerObject()
	    
	    
	    private ArrayList<Object> populateSearchCriteria(DynaActionForm frmViewPayments,HttpServletRequest request)
	    	    throws TTKException
	    	    {
	    	        //build the column names along with their values to be searched
	    	        ArrayList<Object> alSearchParams = new ArrayList<Object>();
	    	        alSearchParams.add(TTKCommon.getWebBoardId(request));
	    	        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sFileName")));
	    	        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sBatchNum")));
	    	        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sClaimSettelmentNumber")));
	    	        alSearchParams.add(TTKCommon.replaceSingleQots((String)frmViewPayments.get("sBatchDate")));
	    	        alSearchParams.add(TTKCommon.getUserSeqId(request));
	    	        return alSearchParams;
	    	    }//end of populateSearchCriteria(DynaActionForm frmFloatAccounts,HttpServletRequest request)   
	    
	    
	    
	    
	    public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		setLinks(request);
	    		log.debug("Inside ViewManualChequePrintingsAction doForward");
	    		TableData  tableData =TTKCommon.getTableData(request);
	    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
	    		tableData.modifySearchData(strForward);//modify the search data
	    		ArrayList alBankList = floatAccountManagerObject.getViewManualChequesList(tableData.getSearchData());
	    		tableData.setData(alBankList, strForward);//set the table data
	    		request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
	    		return this.getForward(strchequeslistdetails, mapping, request);//finally return to the grid screen
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(ETTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
	    	}//end of catch(Exception exp)
	    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)

	  
	    public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	try{
	    		setLinks(request);
	    		log.debug("Inside ViewManualChequePrintingsAction doBackward");
	    		TableData  tableData =TTKCommon.getTableData(request);
	    		FloatAccountManager floatAccountManagerObject=this.getfloatAccountManagerObject();
	    		tableData.modifySearchData(strBackward);//modify the search data
	    		ArrayList alBankList = floatAccountManagerObject.getViewManualChequesList(tableData.getSearchData());
	    		tableData.setData(alBankList, strBackward);//set the table data
	    		request.getSession().setAttribute("tableData",tableData);//set the tableData object to session
	    		return this.getForward(strchequeslistdetails, mapping, request);//finally return to the grid screen
	    	}//end of try
	    	catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(ETTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strBank));
	    	}//end of catch(Exception exp)
	    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)	    
	    
	    
	    public ActionForward doViewFile(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
					
	    	
	    	ByteArrayOutputStream baos=null;
			OutputStream sos = null;
			FileInputStream fis= null; 
			File file = null;
			BufferedInputStream bis =null;
	    	try {
				
	    		log.debug("inside ViewManualChequePrintingsAction Action doViewFile method");
	    		String strBatchFileName = "";
	    		response.setContentType("application/octet-stream");
	    		
	    		TableData  tableData =TTKCommon.getTableData(request);
	    		
	    		String rowFileName = request.getParameter("rownum");
	    		
	    		
	    		strBatchFileName = ((ChequeVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")))).getFileName();

				setLinks(request);
				//response.setContentType("application/txt");
				response.setHeader("Content-Disposition", "attachment;filename="+strBatchFileName);

				String fileName =	TTKPropertiesReader.getPropertyValue("PaymentManualChequePrint")+strBatchFileName;
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
	            return (mapping.findForward(strchequeslistdetails));
			}
	    	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request,mapping,new TTKException(exp,strBank));
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
					return this.processExceptions(request,mapping,new TTKException(exp,strBank));
				}//                 
			}
	    	
	    	
	    }
	    
	    
	    
	    public ActionForward doChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,
	    		HttpServletResponse response) throws Exception{
	    	log.debug("Inside ViewManualChequePrintingsAction doChangeWebBoard");
	    	return doDefault(mapping,form,request,response);
	    }//end of ChangeWebBoard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	    
	    
	    
	    

}
