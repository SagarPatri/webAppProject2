package com.ttk.action.claims;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.TableData;
import com.ttk.business.claims.ClaimBatchManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;

public class SelectAutoRejectionAlkootIdAction  extends TTKAction{
	
	private static Logger log = Logger.getLogger(SelectAutoRejectionAlkootIdAction.class);
	
	
	private static final String strForward="Forward";
	private static final String strBackward="Backward";
	private static final String strRownum="rownum";
	   private static final String strClaimBatchDetails="ClaimsBatchDetails";
	    private static final String strClaimAutoRejectionDetail="claimAutoRejectionDetail"; 
	    private static final String strSelectEnrollID="selectEnrollmentId";
	    private static final String strAutoRejectDetails="ClaimAutoRejectionDetails";
	
	 public ActionForward doMemberSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
	
	log.debug("Inside the doSearch method of SelectAutoRejectionAlkootIdAction");
	setLinks(request);
	HttpSession session=request.getSession();
	ClaimBatchManager claimBatchManagerObject=this.getClaimBatchManagerObject();
	//get the tbale data from session if exists
	TableData tableData =session.getAttribute("tableData")==null?new TableData():(TableData)session.getAttribute("tableData");
	
	if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
	{
		((DynaActionForm)form).initialize(mapping);//reset the form data
	}
	
	String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
	String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
	
	if(!strPageID.equals("") || !strSortID.equals(""))
	{
		if(!strPageID.equals(""))
		{
			tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
			return mapping.findForward(strSelectEnrollID);
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
		tableData.createTableInfo("AutiRejectionEnrollmentIdTable",null);
		tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
     tableData.modifySearchData("search");
     tableData.getSearchData().set(tableData.getSearchData().size()-3, "DESC");
	}//end of else
	//ArrayList alClaimsBatchList= claimBatchManagerObject.getClaimUploadErrorLogList(tableData.getSearchData());
	ArrayList alClaimsBatchList= claimBatchManagerObject.getClaimAutoRejectionList(tableData.getSearchData());
	
	tableData.setData(alClaimsBatchList, "search");
	//set the table data object to session
	session.setAttribute("tableData",tableData);
	
	return this.getForward(strSelectEnrollID, mapping, request);
}//end of try
catch(TTKException expTTK)
{
	return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
	return this.processExceptions(request, mapping, new TTKException(exp,strClaimAutoRejectionDetail));
}//end of catch(Exception exp)
}	
	
	 private ClaimBatchManager getClaimBatchManagerObject() throws TTKException
		{
			ClaimBatchManager claimBatchManager = null;
			try
			{
				if(claimBatchManager == null)
				{
					InitialContext ctx = new InitialContext();
					claimBatchManager = (ClaimBatchManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimBatchManagerBean!com.ttk.business.claims.ClaimBatchManager");
				}//end if
			}//end of try
			catch(Exception exp)
			{
				throw new TTKException(exp, strClaimAutoRejectionDetail);
			}//end of catch
			return claimBatchManager;
		}	
	 
	 

public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
log.debug("Inside SelectAutoRejectionAlkootIdAction doClose");


return this.getForward(strAutoRejectDetails, mapping, request);
}
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strClaimAutoRejectionDetail));
}//end of catch(Exception exp)
}
	 
	 
	 
	 
	 
	 private ArrayList<Object> populateSearchCriteria(DynaActionForm frmAutiRejectionLogList,HttpServletRequest request)
	    {
	    	//build the column names along with their values to be searched
	    	ArrayList<Object> alSearchParams = new ArrayList<Object>();
	    	alSearchParams.add(frmAutiRejectionLogList.getString("iBatchRefNO"));
	    	alSearchParams.add(frmAutiRejectionLogList.getString("batchNO"));
	    	alSearchParams.add(frmAutiRejectionLogList.getString("invoiceNo"));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("formDate")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("toDate")));
	    	alSearchParams.add(TTKCommon.checkNull(frmAutiRejectionLogList.getString("providerName")));
	    	alSearchParams.add(frmAutiRejectionLogList.getString("providerId"));
	    	return alSearchParams;
	    }
	 
	 

}
