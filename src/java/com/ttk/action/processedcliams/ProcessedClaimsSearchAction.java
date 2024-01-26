package com.ttk.action.processedcliams;

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
import com.ttk.business.claims.ClaimManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.preauth.PreAuthVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

public class ProcessedClaimsSearchAction extends TTKAction{
	
	private static Logger log = Logger.getLogger(ProcessedClaimsSearchAction.class);
	
	private static final String strProcessedClaims="ProcessedClaims";
	private static final String strProcessedClaimsSearch="ProcessedClaimsSearch";
	private static final String strBackward="Backward";
    private static final String strForward="Forward";
    private static final  String strProcessedClaimDetails="processedClaimdetail";

	  public ActionForward doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		  
		  try{
	    		setLinks(request);
	    		log.debug("Inside ProcessedClaimsSearchAction doDefault");
	    		String strDefaultBranchID  = String.valueOf(((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile")).getBranchID());
	    			TableData tableData =TTKCommon.getTableData(request);
	    			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y")){
	    				((DynaActionForm)form).initialize(mapping);//reset the form data
	    			}
	    		DynaActionForm frmCallCenterList= (DynaActionForm) form;
	    		tableData = new TableData();
	    		tableData.createTableInfo("ProcessedClaimsTable",new ArrayList());
				request.getSession().setAttribute("tableData",tableData);
	    		
	    		return this.getForward(strProcessedClaimsSearch, mapping, request);	
		  }
			catch(TTKException expTTK)
	    	{
	    		return this.processExceptions(request,mapping,expTTK);
	    	}//end of catch(TTKException expTTK)
	    	catch(Exception exp)
	    	{
	    		return this.processExceptions(request,mapping,new TTKException(exp,strProcessedClaims));
	    	}//end of catch(Exception exp)
		  
		  
	  }
	  
	  public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
		  
		  
		  try{
	    		log.debug("Inside the doSearch method of ProcessedClaimsSearchAction");
	    		setLinks(request);
		  
	    		ClaimManager claimManagerObject=this.getClaimManagerObject();
	    		/*ProcessedclaimManager claimManagerObject=this.getClaimManagerObject();*/
	    		TableData tableData =TTKCommon.getTableData(request);
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
						return mapping.findForward(strProcessedClaimsSearch);
					}///end of if(!strPageID.equals(""))
					else
					{
						tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
						tableData.modifySearchData("sort");//modify the search data
					}//end of else
				}
				else
				{
					tableData.createTableInfo("ProcessedClaimsTable",null);
					tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));
	               // this.setColumnVisiblity(tableData,(DynaActionForm)form,request);
					tableData.modifySearchData("search");	
					
				}
				
				ArrayList alClaimsList= claimManagerObject.getProcessedClaimList(tableData.getSearchData());
				tableData.setData(alClaimsList, "search");
				//set the table data object to session
				request.getSession().setAttribute("tableData",tableData);
				
	    		
	    		
	    		return this.getForward(strProcessedClaimsSearch, mapping, request);	
		  }
		  catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strProcessedClaims));
			}
	  }


	  private ClaimManager getClaimManagerObject() throws TTKException
	    {
	    	ClaimManager claimManager = null;
	    	try
	    	{
	    		if(claimManager == null)
	    		{
	    			InitialContext ctx = new InitialContext();
	    			claimManager = (ClaimManager) ctx.lookup("java:global/TTKServices/business.ejb3/ClaimManagerBean!com.ttk.business.claims.ClaimManager");
	    		}//end if
	    	}//end of try
	    	catch(Exception exp)
	    	{
	    		throw new TTKException(exp, strProcessedClaims);
	    	}//end of catch
	    	return claimManager;
	    }//end getClaimManagerObject()

		  
	 private ArrayList<Object> populateSearchCriteria(DynaActionForm frmClaimsList,HttpServletRequest request)
	    {
		 
		 
		 ArrayList<Object> alSearchParams = new ArrayList<Object>();
		 
		 alSearchParams.add(frmClaimsList.getString("sClaimNO").trim());
		 alSearchParams.add(frmClaimsList.getString("sBatchNO").trim());
		 alSearchParams.add(frmClaimsList.getString("sProviderName").trim());
		 alSearchParams.add(frmClaimsList.getString("sModeOfClaim").trim());
		 alSearchParams.add(frmClaimsList.getString("internalRemarkStatus").trim());
		 alSearchParams.add(frmClaimsList.getString("sSettlementNO").trim());
		 alSearchParams.add(frmClaimsList.getString("sPolicyNumber").trim());
		 alSearchParams.add(frmClaimsList.getString("sPartnerName").trim());
		 alSearchParams.add(frmClaimsList.getString("sProcessType").trim());
		 alSearchParams.add(frmClaimsList.getString("sRecievedDate").trim());
		 alSearchParams.add(frmClaimsList.getString("sInvoiceNO").trim());
		 alSearchParams.add(frmClaimsList.getString("sEnrollmentId").trim());
		 alSearchParams.add(frmClaimsList.getString("sClaimantName").trim());
		 alSearchParams.add(frmClaimsList.getString("sClaimType").trim());
		 alSearchParams.add(frmClaimsList.getString("sStatus").trim());
		 alSearchParams.add(frmClaimsList.getString("sQatarId").trim());
		 alSearchParams.add(frmClaimsList.getString("sTtkBranch").trim());
		 alSearchParams.add(frmClaimsList.getString("sBenefitType").trim());
		// alSearchParams.add(TTKCommon.getUserSeqId(request));
		 return alSearchParams;
		 
	    }
	  
	  
	  
	 public ActionForward doView(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	    	try{
	    		log.debug("Inside the doView method of ProcessedClaimsSearchAction");
	    		setLinks(request);
	    		//get the tbale data from session if exists
				TableData tableData =TTKCommon.getTableData(request);
	    		DynaActionForm frmClaimsList=(DynaActionForm)form;
	    		if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
				{
					PreAuthVO preAuthVO=(PreAuthVO)tableData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
					
				Long claimseqid =	preAuthVO.getClaimSeqID();
				frmClaimsList.set("lngClaimSeqID",preAuthVO.getClaimSeqID());
				 request.getSession().setAttribute("claimSeqID",claimseqid);	
				 request.getSession().setAttribute("frmProcessedClaimGeneral",frmClaimsList);	
				
					//this.addToWebBoard(preAuthVO, request);
				}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
				return mapping.findForward(strProcessedClaimDetails);
	    	}//end of try
	    	catch(TTKException expTTK)
			{
				return this.processExceptions(request, mapping, expTTK);
			}//end of catch(TTKException expTTK)
			catch(Exception exp)
			{
				return this.processExceptions(request, mapping, new TTKException(exp,strProcessedClaims));
			}//end of catch(Exception exp)
	    }
	 
	 
	 public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doForward method of ProcessedClaimsSearchAction");
setLinks(request);
ClaimManager claimManagerObject=this.getClaimManagerObject();
//get the tbale data from session if exists
TableData tableData =TTKCommon.getTableData(request);
tableData.modifySearchData(strForward);//modify the search data
ArrayList alClaimsList = claimManagerObject.getProcessedClaimList(tableData.getSearchData());
tableData.setData(alClaimsList, strForward);//set the table data
request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
return this.getForward(strProcessedClaimsSearch, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strProcessedClaims));
}//end of catch(Exception exp)
}	 
	 
	 
	 public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception{
try{
log.debug("Inside the doBackward method of ProcessedClaimsSearchAction");
setLinks(request);
ClaimManager claimManagerObject=this.getClaimManagerObject();
//get the tbale data from session if exists
TableData tableData =TTKCommon.getTableData(request);
tableData.modifySearchData(strBackward);//modify the search data
ArrayList alClaimsList = claimManagerObject.getProcessedClaimList(tableData.getSearchData());
tableData.setData(alClaimsList, strBackward);//set the table data
request.getSession().setAttribute("tableData",tableData);   //set the table data object to session
return this.getForward(strProcessedClaimsSearch, mapping, request);   //finally return to the grid screen
}//end of try
catch(TTKException expTTK)
{
return this.processExceptions(request, mapping, expTTK);
}//end of catch(TTKException expTTK)
catch(Exception exp)
{
return this.processExceptions(request, mapping, new TTKException(exp,strProcessedClaims));
}//end of catch(Exception exp)
}	 
	 
	 
	 
	  
	  
}
