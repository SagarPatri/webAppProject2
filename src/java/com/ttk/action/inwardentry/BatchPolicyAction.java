/**
 * @ (#) BatchPolicyAction.java Feb 01, 2006
 * Project      : TTK HealthCare Services
 * File         : BatchPolicyAction.java
 * Author       : Lancy A
 * Company      : Span Systems Corporation
 * Date Created : Feb 01, 2006
 *
 * @author       : Lancy A
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.inwardentry;

import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.business.enrollment.PolicyManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.enrollment.PolicyDetailVO;
import com.ttk.dto.enrollment.PolicyVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for displaying the List of Batch Policies.
 * This class also provides option for Deletion, Addition and Updation of Batch Policies.
 */
public class BatchPolicyAction extends TTKAction
{
	
	private static Logger log = Logger.getLogger(BatchPolicyAction.class);
	
	//Declaration
	private String strAddFlag="";
	
	//Modes.
	private static final String strBackward="Backward";
	private static final String strForward="Forward";
	
	// Action mapping forwards.
	private static final String strBatchPolicyList="batchpolicylist";
	private static final String strPolicyDetail="policydetail";
	private static final String strChngCorporate="changecorporate";
	
	//Exception Message Identifier
	private static final String strEnrollmentExp="enrollment";
	
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
	public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doSearch method of BatchPomlicyAction");
			setLinks(request);
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			DynaActionForm formBatchPolicy= (DynaActionForm) form;
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			//Building the caption
			StringBuffer strCaption= new StringBuffer();
			if(frmBatch!=null)
			{
				if((TTKCommon.checkNull((String)frmBatch.get("batchSeqID")).equals("")))
				{
					TTKException expTTK = new TTKException();
					expTTK.setMessage("error.batch.required");
					throw expTTK;
				}// end of if((TTKCommon.checkNull((String)frmGeneral.get("batchSeqID")).equals("")))
			}//end of if(frmBatch!=null)
			else
			{
				TTKException expTTK = new TTKException();
				expTTK.setMessage("error.batch.required");
				throw expTTK;
			}//end of else if(frmBatch!=null)
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)
			//clear the dynaform if visting from left links for the first time
			//else get the dynaform data from session
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			Boolean blPemession=TTKCommon.isAuthorized(request,"Edit");//check for the edit permession.
			String strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			String strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					batchPolicyData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return (mapping.findForward(strBatchPolicyList));
				}///end of if(!strPageID.equals(""))
				else
				{
					batchPolicyData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					batchPolicyData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			else
			{
				batchPolicyData.createTableInfo("BatchPolicyTable",null);
				if(blPemession&&(TTKCommon.checkNull((String)frmBatch.get("inwardCompleted")).equals("")))//check of edit permession and inward entry completion.
				{
					((Column)((ArrayList)batchPolicyData.getTitle()).get(0)).setIsLink(true); // set the hyper link
				}// end of if((TTKCommon.checkNull((String)frmGeneral.get("inwardCompleted")).equals("")))
				else
				{
					((Column)((ArrayList)batchPolicyData.getTitle()).get(5)).setVisibility(false);
					((Column)((ArrayList)batchPolicyData.getTitle()).get(0)).setIsLink(false);//disable the hyper link
				}// end of else
				batchPolicyData.setSearchData(this.populateSearchCriteria(request));
				batchPolicyData.setSortData("0001");
				batchPolicyData.setSortColumnName("POLICY_SEQ_ID");
				batchPolicyData.setSortOrder("ASC");
				batchPolicyData.modifySearchData("search");
			}//end of else
			ArrayList alPolicy= policyManagerObject.getBatchPolicyList(batchPolicyData.getSearchData());
			setFlagValues(alPolicy,formBatchPolicy);		//To set the different flag values for add policy and batch complete
			batchPolicyData.setData(alPolicy, "search");
			strCaption.append("List of Policies - [").append(frmBatch.get("batchNbr")).append("]");
			formBatchPolicy.set("caption",strCaption.toString());
			//set the batch policy data object to session
			request.getSession().setAttribute("batchPolicyData",batchPolicyData);
			request.getSession().removeAttribute("alInsProducts");	//removing the Insurance Products arraylist from the session
			//finally return to the grid screen
			return this.getForward(strBatchPolicyList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
	public ActionForward doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doBackward method of BatchPolicyAction");
			setLinks(request);
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)
			batchPolicyData.modifySearchData(strBackward);//modify the search data
			ArrayList alPolicy = policyManagerObject.getBatchPolicyList(batchPolicyData.getSearchData());
			batchPolicyData.setData(alPolicy, strBackward);//set the batch policy data
			request.getSession().setAttribute("batchPolicyData",batchPolicyData);//set the batch policy data object to session
			return this.getForward(strBatchPolicyList, mapping, request);//finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to get the next set of records with the given search criteria.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doForward method of BatchPolicyAction");
			setLinks(request);
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)
			batchPolicyData.modifySearchData(strForward);//modify the search data
			ArrayList alPolicy = policyManagerObject.getBatchPolicyList(batchPolicyData.getSearchData());
			batchPolicyData.setData(alPolicy, strForward);//set the batch policy data
			request.getSession().setAttribute("batchPolicyData",batchPolicyData);//set the batch policy data object to session
			return this.getForward(strBatchPolicyList, mapping, request);//finally return to the grid screen
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to navigate to  detail screen to edit selected record.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doViewBatchPolicy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doViewBatchPolicy method of BatchPolicyAction");
			setLinks(request);
			PolicyDetailVO policyDetailVO=null;
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			//Building the caption
			StringBuffer strAddCaption= new StringBuffer();
			ArrayList alInsProducts = new ArrayList();
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)
			PolicyVO policyVO=null;
			DynaActionForm frmPolicyDetail= (DynaActionForm)form;
			if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			{
				policyVO = (PolicyVO)batchPolicyData.getRowInfo(Integer.parseInt(request.getParameter("rownum")));
				if(policyVO.getEndorsementSeqID()!=null)
				{
					policyDetailVO=policyManagerObject.getBatchPolicy(null,policyVO.getEndorsementSeqID(),TTKCommon.getLong(frmBatch.get("insuranceSeqID").toString()));
				}//end of if(policyVO.getEndorsementSeqID()!=null)
				else
				{
					policyDetailVO=policyManagerObject.getBatchPolicy(policyVO.getPolicySeqID(),null,TTKCommon.getLong(frmBatch.get("insuranceSeqID").toString()));
				}//end of else
				HashMap hmInsProducts = policyManagerObject.getProductInfo(TTKCommon.getLong(frmBatch.getString("insuranceSeqID")));
				//HashMap<Object,Object> hmInsProducts = (HashMap)request.getSession().getAttribute("hmInsProducts");
				alInsProducts =(ArrayList)hmInsProducts.get(policyDetailVO.getPolicyTypeID());
				frmPolicyDetail = (DynaActionForm)FormUtils.setFormValues("frmPolicyDetail",policyDetailVO,this,mapping,request);
				if(policyDetailVO.getEndorseGenTypeID().equals("ENM"))
				{
					frmPolicyDetail.set("hidEndorsementNbrINS",policyDetailVO.getEndorsementNbr());
					
				}//end of if(policyDetailVO.getEndorseGenTypeID().equals("EIN"))
				else if(policyDetailVO.getEndorseGenTypeID().equals("END"))
				{
					
					frmPolicyDetail.set("hidEndorsementNbrTTK",policyDetailVO.getEndorsementNbr());
					
			   }//end of else if(policyDetailVO.getEndorseGenTypeID().equals("ETT"))
				frmPolicyDetail.set("rownum",request.getParameter("rownum"));
				frmPolicyDetail.set("addEdit","edit");
				strAddCaption.append("Policy Details - ").append("Edit").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
			frmPolicyDetail.set("caption",strAddCaption.toString());
			frmPolicyDetail.set("alInsProducts",alInsProducts);
			request.getSession().setAttribute("frmPolicyDetail",frmPolicyDetail);
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doViewBatchPolicy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
	public ActionForward doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doAdd method of BatchPolicyAction");
			setLinks(request);
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			DynaActionForm frmPolicyDetail= (DynaActionForm)form;
			//Building the caption
			StringBuffer strAddCaption= new StringBuffer();
			ArrayList alInsProducts = new ArrayList();
			frmPolicyDetail=(DynaActionForm)form;
			frmPolicyDetail.initialize(mapping);
			
			if(userSecurityProfile.getBranchID()!=null)
			{
				frmPolicyDetail.set("officeSeqID", userSecurityProfile.getBranchID().toString());
			}//end of if(userSecurityProfile.getBranchID()!=null)
			else
			{
				frmPolicyDetail.set("officeSeqID", "");
			}//end of else
			
			
			frmPolicyDetail.set("policyTypeID", "COR");
			
			HashMap hmInsProducts = policyManagerObject.getProductInfo(TTKCommon.getLong(frmBatch.getString("insuranceSeqID")));
			//HashMap<Object,Object> hmInsProducts = (HashMap)request.getSession().getAttribute("hmInsProducts");
			alInsProducts = null;			
			alInsProducts=(ArrayList)hmInsProducts.get("COR");
			if(alInsProducts == null)
			{
				alInsProducts = new ArrayList();
			}//end of if(alInsProducts == null)
			
			frmPolicyDetail.set("endorseGenTypeID", "ENM");
			frmPolicyDetail.set("insuranceSeqID",frmBatch.get("hidInsuranceSeqID"));
			frmPolicyDetail.set("companyName",frmBatch.get("hidCompanyName"));
			frmPolicyDetail.set("officeCode",frmBatch.get("hidOfficeCode"));
			frmPolicyDetail.set("addEdit","add");
			
			strAddCaption.append("Policy Details - ").append("Add").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			frmPolicyDetail.set("caption",strAddCaption.toString());
			frmPolicyDetail.set("alInsProducts",alInsProducts);
			
			
			
			request.getSession().setAttribute("frmPolicyDetail",frmPolicyDetail);
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
	public ActionForward doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doSave method of BatchPolicyAction");
			setLinks(request);
			PolicyDetailVO policyDetailVO=null;
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			
			//Building the caption
			StringBuffer strAddCaption= new StringBuffer();
			ArrayList alInsProducts = new ArrayList();
			DynaActionForm frmPolicyDetail=(DynaActionForm)form;
			alInsProducts = (ArrayList)frmPolicyDetail.get("alInsProducts");
			Long lngPolicySeqID=null;
			Long lngEndorsementSeqID=null;
			String strAddEdit="";
			if(request.getParameter("photoPresentYN") == null)// if photoPresentYN checkbox in not selected
			{
				frmPolicyDetail.set("photoPresentYN","N");
			}//end of if(request.getParameter("photoPresentYN") == null)
			if(request.getParameter("policyNotLegibleYN") == null)// if policyNotLegibleYN checkbox in not selected
			{
				frmPolicyDetail.set("policyNotLegibleYN","N");
			}//end of if(request.getParameter("policyNotLegibleYN") == null)
			
			
			policyDetailVO=(PolicyDetailVO)FormUtils.getFormValues(frmPolicyDetail,"frmPolicyDetail",this, mapping, request);
			policyDetailVO.setUpdatedBy(TTKCommon.getUserSeqId(request));
			policyDetailVO.setBatchSeqID(new Long((String)frmBatch.get("batchSeqID")));
			
			/*if(frmPolicyDetail.get("endorseGenTypeID").equals(""))
			{
				frmPolicyDetail.set("endorsementNbr","");
			}//end of if(frmPolicyDetail.get("endorseGenTypeID").equals(""))*/	
			
			
			
		int iResult=policyManagerObject.saveBatchPolicy(policyDetailVO);
		
			if(iResult>0)
			{
				request.setAttribute("updated",((String)frmPolicyDetail.get("addEdit")).equals("add")? "message.addedSuccessfully" :"message.savedSuccessfully");
			}//end of if(iResult>0)
			
			if(!TTKCommon.checkNull((String)frmPolicyDetail.get("policySeqID")).equals(""))
			{
				lngPolicySeqID=new Long((String)frmPolicyDetail.get("policySeqID"));
				
			}//end of if(!TTKCommon.checkNull((String)frmPolicyDetail.get("policySeqID")).equals(""))
			
			if(!TTKCommon.checkNull((String)frmPolicyDetail.get("endorsementSeqID")).equals(""))
			{
				lngEndorsementSeqID=new Long((String)frmPolicyDetail.get("endorsementSeqID"));
				
			
			}//end of if(!TTKCommon.checkNull((String)frmPolicyDetail.get("endorsementSeqID")).equals(""))
			
			if(lngEndorsementSeqID!=null)
			{
				policyDetailVO=policyManagerObject.getBatchPolicy(null,lngEndorsementSeqID,TTKCommon.getLong(frmBatch.get("insuranceSeqID").toString()));
			
				
			}//end of if(lngEndorsementSeqID!=null)
			else
			{
				policyDetailVO=policyManagerObject.getBatchPolicy(lngPolicySeqID,null,TTKCommon.getLong(frmBatch.get("insuranceSeqID").toString()));
			
				
				
			}//end of else
			frmPolicyDetail = (DynaActionForm)FormUtils.setFormValues("frmPolicyDetail",policyDetailVO,this,mapping,request);
			
			if(policyDetailVO.getEndorseGenTypeID().equals("ENM"))
			{
				frmPolicyDetail.set("hidEndorsementNbrINS",policyDetailVO.getEndorsementNbr());
				
			}//end of if(policyDetailVO.getEndorseGenTypeID().equals("EIN"))
			else if(policyDetailVO.getEndorseGenTypeID().equals("END"))
			{
				frmPolicyDetail.set("hidEndorsementNbrTTK",policyDetailVO.getEndorsementNbr());
				
			}//end of else if(policyDetailVO.getEndorseGenTypeID().equals("ETT"))
			frmPolicyDetail.set("insuranceSeqID",frmBatch.get("hidInsuranceSeqID"));
			frmPolicyDetail.set("rownum",request.getParameter("rownum"));
			frmPolicyDetail.set("companyName",frmBatch.get("hidCompanyName"));
			frmPolicyDetail.set("officeCode",frmBatch.get("hidOfficeCode"));
			if(!(TTKCommon.checkNull((String)frmPolicyDetail.get("rownum")).equals("")))
			{
				strAddCaption.append("Policy Details - ").append("Edit").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
				strAddEdit="edit";
			}//end of if(!(TTKCommon.checkNull((String)frmPolicyDetail.get("rownum")).equals("")))
			else
			{
				strAddCaption.append("Policy Details - ").append("Add").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
				alInsProducts = new ArrayList();
				strAddEdit="add";
				if(userSecurityProfile.getBranchID()!=null)
				{
					frmPolicyDetail.set("officeSeqID", userSecurityProfile.getBranchID().toString());
				}//end of if(userSecurityProfile.getBranchID()!=null)
				else
				{
					frmPolicyDetail.set("officeSeqID", "");
				}//end of else
			}//end of else
			frmPolicyDetail.set("addEdit",strAddEdit);
			frmPolicyDetail.set("alInsProducts",alInsProducts);
			frmPolicyDetail.set("caption",strAddCaption.toString());
			request.getSession().setAttribute("frmPolicyDetail",frmPolicyDetail);
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doSave(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to delete the record from the list.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doDeleteList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doDeleteList method of BatchPolicyAction");
			setLinks(request);
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			DynaActionForm formBatchPolicy= (DynaActionForm) form;
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)
			StringBuffer sbfDeleteId = new StringBuffer("|");
			//populate the delete string which contains the sequence id's to be deleted
			sbfDeleteId.append(populateDeleteId(request, (TableData)request.getSession().getAttribute("batchPolicyData")));
			//delete the Batch Policy Details
			int iCount = policyManagerObject.deleteBatchPolicy(sbfDeleteId.append("|").toString(),TTKCommon.getUserSeqId(request));
			//refresh the grid with search data in session
			ArrayList alPolicy = null;
			//fetch the data from previous set of rowcounts, if all the records are deleted for the current set of search criteria
			if(iCount == batchPolicyData.getData().size())
			{
				batchPolicyData.modifySearchData("Delete");//modify the search data
				int iStartRowCount = Integer.parseInt((String)batchPolicyData.getSearchData().get(batchPolicyData.getSearchData().size()-2));
				if(iStartRowCount > 0)
				{
					alPolicy = policyManagerObject.getBatchPolicyList(batchPolicyData.getSearchData());
				}//end of if(iStartRowCount > 0)
			}//end if(iCount == batchPolicyData.getData().size())
			else
			{
				alPolicy = policyManagerObject.getBatchPolicyList(batchPolicyData.getSearchData());
			}//end of else
			setFlagValues(alPolicy,formBatchPolicy);		//To set the different flag values for add policy and batch complete
			batchPolicyData.setData(alPolicy, "Delete");
			//set the batch policy data object to session
			request.getSession().setAttribute("batchPolicyData",batchPolicyData);
			return this.getForward(strBatchPolicyList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doDeleteList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to reload the screen when the reset button is pressed.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doReset method of BatchPolicyAction");
			setLinks(request);
			PolicyDetailVO policyDetailVO=null;
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			//Building the caption
			StringBuffer strAddCaption= new StringBuffer();
			ArrayList alInsProducts = new ArrayList();
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)
			DynaActionForm frmPolicyDetail= (DynaActionForm)form;
			if(((frmPolicyDetail.get("policySeqID")!=null && !frmPolicyDetail.get("policySeqID").equals(""))||((frmPolicyDetail.get("endorsementSeqID"))!=null)&&!frmPolicyDetail.get("endorsementSeqID").equals("")))
			{
				if(frmPolicyDetail.get("endorsementSeqID")!=null&&!frmPolicyDetail.get("endorsementSeqID").equals(""))
				{
					policyDetailVO=policyManagerObject.getBatchPolicy(null,TTKCommon.getLong(frmPolicyDetail.get("endorsementSeqID").toString()),TTKCommon.getLong(frmBatch.get("insuranceSeqID").toString()));
				}//end of if(frmPolicyDetail.get("endorsementSeqID")!=null&&!frmPolicyDetail.get("endorsementSeqID").equals(""))
				else
				{
					policyDetailVO=policyManagerObject.getBatchPolicy(TTKCommon.getLong(frmPolicyDetail.get("policySeqID").toString()),null,TTKCommon.getLong(frmBatch.get("insuranceSeqID").toString()));
				}//end of else
				HashMap hmInsProducts = policyManagerObject.getProductInfo(TTKCommon.getLong(frmBatch.getString("insuranceSeqID")));
				//HashMap<Object,Object> hmInsProducts = (HashMap)request.getSession().getAttribute("hmInsProducts");
				alInsProducts =(ArrayList)hmInsProducts.get(policyDetailVO.getPolicyTypeID());
				frmPolicyDetail = (DynaActionForm)FormUtils.setFormValues("frmPolicyDetail",policyDetailVO,this,mapping,request);
				if(policyDetailVO.getEndorseGenTypeID().equals("ENM"))
				{
					frmPolicyDetail.set("hidEndorsementNbrINS",policyDetailVO.getEndorsementNbr());
				}//end of if(policyDetailVO.getEndorseGenTypeID().equals("EIN"))
				else if(policyDetailVO.getEndorseGenTypeID().equals("END"))
				{
					frmPolicyDetail.set("hidEndorsementNbrTTK",policyDetailVO.getEndorsementNbr());
				}//end of else if(policyDetailVO.getEndorseGenTypeID().equals("ETT"))
				frmPolicyDetail.set("rownum",request.getParameter("rownum"));
				frmPolicyDetail.set("addEdit","edit");
				strAddCaption.append("Policy Details - ").append("Edit").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of if(((frmPolicyDetail.get("policySeqID")!=null && !frmPolicyDetail.get("policySeqID").equals(""))||((frmPolicyDetail.get("endorsementSeqID"))!=null)&&!frmPolicyDetail.get("endorsementSeqID").equals("")))
			else
			{
				frmPolicyDetail=(DynaActionForm)form;
				frmPolicyDetail.initialize(mapping);
				frmPolicyDetail.set("policyTypeID","");
				if(userSecurityProfile.getBranchID()!=null)
				{
					frmPolicyDetail.set("officeSeqID", userSecurityProfile.getBranchID().toString());
				}//end of if(userSecurityProfile.getBranchID()!=null)
				else
				{
					frmPolicyDetail.set("officeSeqID", "");
				}//end of else
				frmPolicyDetail.set("insuranceSeqID",frmBatch.get("hidInsuranceSeqID"));
				frmPolicyDetail.set("companyName",frmBatch.get("hidCompanyName"));
				frmPolicyDetail.set("officeCode",frmBatch.get("hidOfficeCode"));
				frmPolicyDetail.set("addEdit","add");
				strAddCaption.append("Policy Details - ").append("Add").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of else
			frmPolicyDetail.set("caption",strAddCaption.toString());
			frmPolicyDetail.set("alInsProducts",alInsProducts);
			request.getSession().setAttribute("frmPolicyDetail",frmPolicyDetail);
			return this.getForward(strPolicyDetail,mapping,request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doReset(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
	public ActionForward doBatchCompletePolicy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doBatchCompletePolicy method of BatchPolicyAction");
			setLinks(request);
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			DynaActionForm formBatchPolicy= (DynaActionForm) form;
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)s
//			pass 'INW' if it is Inward Entry complete and pass 'BAT' if it is Batch complete
			int iResult = policyManagerObject.saveBatchComplete(new Long((String)frmBatch.get("batchSeqID")), TTKCommon.getUserSeqId(request),formBatchPolicy.get("batchType").toString());
			((Column)((ArrayList)batchPolicyData.getTitle()).get(5)).setVisibility(false);
			((Column)((ArrayList)batchPolicyData.getTitle()).get(0)).setIsLink(false);
			if(iResult > 0)
			{
				ArrayList alPolicy= policyManagerObject.getBatchPolicyList(batchPolicyData.getSearchData());
				setFlagValues(alPolicy,formBatchPolicy);		//To set the different flag values for add policy and batch complete
				batchPolicyData.setData(alPolicy, "search");
				request.getSession().setAttribute("batchPolicyData",batchPolicyData);
				request.setAttribute("updated","message.processExecutedSuccessfully");
			}//end of if(iResult > 0)
			return mapping.findForward(strBatchPolicyList);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doBatchCompletePolicy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
	public ActionForward doChangePolicyType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doChangePolicyType method of BatchPolicyAction");
			setLinks(request);
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			DynaActionForm frmBatch = (DynaActionForm)request.getSession().getAttribute("frmBatch");
			//Building the caption
			StringBuffer strAddCaption= new StringBuffer();
			ArrayList alInsProducts = new ArrayList();
			DynaActionForm frmPolicyDetail=(DynaActionForm)form;
			HashMap hmInsProducts = policyManagerObject.getProductInfo(TTKCommon.getLong(frmBatch.getString("insuranceSeqID")));
			//HashMap<Object,Object> hmInsProducts = (HashMap)request.getSession().getAttribute("hmInsProducts");
			alInsProducts = null;
			String StrPolicyTypeID=(String)frmPolicyDetail.get("policyTypeID");
			if(!StrPolicyTypeID.equals(""))
			{
				alInsProducts=(ArrayList)hmInsProducts.get(StrPolicyTypeID);
			}//end of if(!StrPolicyTypeID.equals(""))
			else
			{
				alInsProducts=new ArrayList();
			}//end of else
			if(alInsProducts == null)
			{
				alInsProducts = new ArrayList();
			}//end of if(alInsProducts == null)
			frmPolicyDetail.set("alInsProducts",alInsProducts);
			frmPolicyDetail.set("groupID","");
			frmPolicyDetail.set("groupName","");
			frmPolicyDetail.set("groupRegnSeqID","");
			frmPolicyDetail.set("frmChanged","changed");
			if(!(TTKCommon.checkNull((String)frmPolicyDetail.get("rownum")).equals("")))
			{
				strAddCaption.append("Policy Details - ").append("Edit").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of if(!(TTKCommon.checkNull((String)frmPolicyDetail.get("rownum")).equals("")))
			else
			{
				strAddCaption.append("Policy Details - ").append("Add").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of else
			frmPolicyDetail.set("caption",strAddCaption.toString());
			request.getSession().setAttribute("frmPolicyDetail",frmPolicyDetail);
			return this.getForward(strPolicyDetail, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doChangePolicyType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
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
			DynaActionForm frmPolicyDetail=(DynaActionForm)form;
			if(!(TTKCommon.checkNull((String)frmPolicyDetail.get("rownum")).equals("")))
			{
				strAddCaption.append("Policy Details - ").append("Edit").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of if(!(TTKCommon.checkNull((String)frmPolicyDetail.get("rownum")).equals("")))
			else
			{
				strAddCaption.append("Policy Details - ").append("Add").append(" [ ").append(frmBatch.get("batchNbr")).append(" ]");
			}//end of else
			frmPolicyDetail.set("caption",strAddCaption.toString());
			request.getSession().setAttribute("frmPolicyDetail",frmPolicyDetail);
			return mapping.findForward(strChngCorporate);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of doChangeCorporate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * This method is used to navigate to previous screen when closed button is clicked.
	 * Finally it forwards to the appropriate view based on the specified forward mappings
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * @return ActionForward Where the control will be forwarded, after this request is processed
	 * @throws Exception if any error occurs
	 */
	public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			log.debug("Inside the doClose method of BatchPolicyAction");
			setLinks(request);
			PolicyManager policyManagerObject=this.getPolicyManagerObject();
			DynaActionForm formBatchPolicy= (DynaActionForm) form;
			TableData  batchPolicyData = null;
			batchPolicyData = (TableData)request.getSession().getAttribute("batchPolicyData");
			if(batchPolicyData == null)
			{
				batchPolicyData = new TableData();
			}// end of if(batchPolicyData == null)
			request.getSession().removeAttribute("frmPolicyDetail");
			request.getSession().removeAttribute("alInsProducts");	//removing the Insurance Products arraylist from the session
			if(batchPolicyData.getSearchData().size()>1)
			{
				ArrayList alPolicy= policyManagerObject.getBatchPolicyList(batchPolicyData.getSearchData());
				setFlagValues(alPolicy,formBatchPolicy);	//To set the different flag values for add policy and batch complete
				batchPolicyData.setData(alPolicy, "search");
				//set the batch policy data object to session
				request.getSession().setAttribute("batchPolicyData",batchPolicyData);
			}//end of if(batchPolicyData.getSearchData().size()>1)
			return this.getForward(strBatchPolicyList, mapping, request);
		}//end of try
		catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEnrollmentExp));
		}//end of catch(Exception exp)
	}//end of Close(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	
	/**
	 * Returns a string which contains the comma separated sequence id's to be deleted
	 * @param request HttpServletRequest object which contains information about the selected check boxes
	 * @param batchPolicyData TableData object which contains the value objects
	 * @return String which contains the comma separated sequence id's to be deleted
	 */
	
	private String populateDeleteId(HttpServletRequest request, TableData batchPolicyData)
	{
		String[] strChk = request.getParameterValues("chkopt");
		StringBuffer sbfDeleteId = new StringBuffer();
		Long lngEndorsementSeqID;
		Long lngPolicySeqID ;
		if(strChk!=null&&strChk.length!=0)
		{
			//loop through to populate delete sequence id's and get the value from session for the matching check box value
			for(int i=0; i<strChk.length;i++)
			{
				if(strChk[i]!=null)
				{
					//extract the sequence id to be deleted from the value object
					lngEndorsementSeqID = ((PolicyVO)batchPolicyData.getData().get(Integer.parseInt(strChk[i]))).getEndorsementSeqID();
					lngPolicySeqID = ((PolicyVO)batchPolicyData.getData().get(Integer.parseInt(strChk[i]))).getPolicySeqID();
					if(i == 0)
					{
						if(lngEndorsementSeqID == null && lngPolicySeqID != null)
						{
							sbfDeleteId.append("").append("|").append(lngPolicySeqID.intValue());
						}//end of if(lngEndorsementSeqID == null && lngPolicySeqID != null)
						else if((lngPolicySeqID == null && lngEndorsementSeqID != null)  || (lngEndorsementSeqID != null && lngPolicySeqID != null))
						{
							sbfDeleteId.append(lngEndorsementSeqID.intValue()).append("|").append("");
						}//end of else if((lngPolicySeqID == null && lngEndorsementSeqID != null)  || (lngEndorsementSeqID != null && lngPolicySeqID != null))
					}//end of if(i == 0)
					else
					{
						if(lngEndorsementSeqID == null && lngPolicySeqID != null)
						{
							sbfDeleteId.append("|").append("").append("|").append(lngPolicySeqID.intValue());
						}//end of if(lngEndorsementSeqID == null && lngPolicySeqID != null)
						else if((lngPolicySeqID == null && lngEndorsementSeqID != null)  || (lngEndorsementSeqID != null && lngPolicySeqID != null))
						{
							sbfDeleteId.append("|").append(lngEndorsementSeqID.intValue()).append("|").append("");
						}// end of else if((lngPolicySeqID == null && lngEndorsementSeqID != null)  || (lngEndorsementSeqID != null && lngPolicySeqID != null))
					}//end of else
				}//end of if(strChk[i]!=null)
			}//end of for(int i=0; i<strChk.length;i++)
		}//end of if(strChk!=null&&strChk.length!=0)
		return sbfDeleteId.toString();
	}//end of populateDeleteId(HttpServletRequest request, TableData batchPolicyData)
	
	/**
	 * Returns the ArrayList which contains the populated search criteria elements.
	 * @param request HttpServletRequest object which contains the search parameter that is built.
	 * @return alSearchParams ArrayList.
	 */
	private ArrayList<Object> populateSearchCriteria(HttpServletRequest request)
	{
		DynaActionForm frmGeneral = (DynaActionForm)request.getSession().getAttribute("frmBatch");
		//build the column names along with their values to be searched
		ArrayList<Object> alSearchParams = new ArrayList<Object>();
		alSearchParams.add(frmGeneral.get("batchSeqID"));
		alSearchParams.add(TTKCommon.getUserSeqId(request));
		return alSearchParams;
	}//end of populateSearchCriteria(HttpServletRequest request)
	
	/**
	 * Returns the PolicyManager session object for invoking methods on it.
	 * @return PolicyManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
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
	
	/**
	 * This method is used to set the default flag values.
	 * @param alPolicy ArrayList which contains the information of all the policies.
	 * @param formBatchPolicy DynaActionForm.
	 */
	private void setFlagValues(ArrayList alPolicy, DynaActionForm formBatchPolicy)
	{
		PolicyVO policyVO=null;
		if(alPolicy!=null && alPolicy.size()>0)
		{
			//If arraylist cotains only string value then is used as flag to dispaly the Add Policy Button.
			//If this is the flow then, It will not contain the table data information, we will be removing string value.
			Object obj=(Object)alPolicy.get(0);
			if(obj.getClass()== String.class)
			{
				strAddFlag=alPolicy.get(0).toString();
				alPolicy.remove(0);
			}//end of if(obj.getClass()== String.class)
		}//end of if(alPolicy!=null && alPolicy.size()>0)
		if(alPolicy!=null && alPolicy.size()>0)
		{
			policyVO = (PolicyVO)alPolicy.get(0);
			strAddFlag=policyVO.getFlag();
			//Set BatchCompleteYN as Y when both BatchEntryCompleteYN and BatchCompleteYN are "N" to display one button at a time
			if(policyVO.getBatchEntryCompleteYN().equals("N")&&policyVO.getBatchCompleteYN().equals("N"))
			{
				policyVO.setBatchCompleteYN("Y");
			}//end of if(policyVO.getBatchEntryCompleteYN().equals("N")&&policyVO.getBatchCompleteYN().equals("N"))
			formBatchPolicy.set("inwardCompletedYN",policyVO.getBatchEntryCompleteYN());//flag value to say inward entry completed or not
			formBatchPolicy.set("batchCompletedYN",policyVO.getBatchCompleteYN());//flag value to say batch completed or not
		}//end of if(alPolicy!=null && alPolicy.size()>0)
		else
		{
			formBatchPolicy.set("inwardCompletedYN","N");
			formBatchPolicy.set("batchCompletedYN","Y");
		}//end of else if(alPolicy!=null && alPolicy.size()>0)
		formBatchPolicy.set("addFlag",strAddFlag);
	}//end of setFlagValues(ArrayList alPolicy, DynaActionForm formBatchPolicy)
	
}// end of BatchPolicyAction.java