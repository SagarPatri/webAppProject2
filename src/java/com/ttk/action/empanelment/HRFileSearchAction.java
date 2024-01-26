/**
* @ (#) GroupListAction.java Jan 16th, 2006
* Project 		: TTK HealthCare Services
* File 			: GroupListAction.java
* Author 		: Krishna K H
* Company 		: Span Systems Corporation
* Date Created 	: Jan 16, 2006
*
* @author 		: Krishna K H
* Modified by 	:
* Modified date :
* Reason 		:
*/

package com.ttk.action.empanelment;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axis.utils.ArrayUtil;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.ttk.action.TTKAction;
import com.ttk.action.table.Column;
import com.ttk.action.table.TableData;
import com.ttk.action.tree.TreeData;
import com.ttk.business.empanelment.GroupRegistrationManager;
import com.ttk.business.empanelment.HospitalManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.SearchCriteria;
import com.ttk.dto.empanelment.AddressVO;
import com.ttk.dto.empanelment.BrokerVO;
import com.ttk.dto.empanelment.GroupRegistrationVO;
import com.ttk.dto.empanelment.InsuranceVO;
import com.ttk.dto.enrollment.HRFilesDetailsVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

import formdef.plugin.util.FormUtils;

/**
 * This class is used for searching the list of groups,add/edit,delete group information.
 * This class is also used to search,add/edit contact information.
 */

public class HRFileSearchAction extends TTKAction
{
	private static final String strBackward="Backward";
    private static final String strForward="Forward";
   
    private static final String strEmpnelmentFiles="empalmentfiles";
    //Action mapping forwards.
    private static final String strGroupDetail="groupdetail";
    //Exception Message Identifier
    private static final String strGroupDetailError="groupdetail";
    
	private static Logger log = Logger.getLogger( HRFileSearchAction.class );
	
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
    		log.debug("Inside the doDefault method of HRFileSearchAction");
    		setLinks(request);
			log.debug("Inside HRFileSearchAction doDefault");
			TreeData treeData = TTKCommon.getTreeData(request);
			DynaActionForm frmFilesList = (DynaActionForm)form;
			GroupRegistrationManager groupRegisterManagerObject=this.getGroupRegnManagerObject();
			GroupRegistrationVO groupRegistrationVO= new GroupRegistrationVO();
	    	GroupRegistrationVO groupRegistrationParentVO= new GroupRegistrationVO();
	    	String strOfficemode="Group";
	    	String strParentGroupName="";
	    	int iSelectedRoot = Integer.parseInt((String)frmFilesList.get("selectedroot"));
	    	//int iSelectedNode=-1;  // by default no node is selected
	    	String strSelectedRoot = (String)frmFilesList.get("selectedroot");
	    	String strSelectedNode = (String)frmFilesList.get("selectednode");
	    	//treeData.setSelectedRoot(-1);	//sets the selected rows
	    	if(strSelectedNode==null)
	    	{
	    		strSelectedNode="";
	    	}//end of if(strSelectedNode==null)
	    	if(!strSelectedNode.equals(""))
	    	{
	    		//Gets the office information
	    		//iSelectedNode=Integer.parseInt(strSelectedNode);
	    		//from tree with with min data
	    		groupRegistrationVO=(GroupRegistrationVO)treeData.getSelectedObject(strSelectedRoot,strSelectedNode);
	    		//Gettign the parent information.
	    		groupRegistrationParentVO=(GroupRegistrationVO)treeData.getSelectedObject(strSelectedRoot,null); 
	    		String  temp=groupRegistrationParentVO.getGroupName();
				String [] caption=temp.split("-");
	    		strOfficemode="Office";
	    		strParentGroupName=groupRegistrationVO.getGroupName();
	    		strParentGroupName=strParentGroupName+" - "+caption[1];
	    	}//end of if(!strSelectedNode.equals(""))
	    	else
	    	{
	    		strOfficemode="Group";
	    		groupRegistrationVO=(GroupRegistrationVO)treeData.getRootData().get(iSelectedRoot);
	    		//Gettign the parent information.
	    		groupRegistrationParentVO=(GroupRegistrationVO)treeData.getSelectedObject(strSelectedRoot,null); 
	    		strParentGroupName=groupRegistrationParentVO.getGroupName();
                if(!strSelectedRoot.equals(String.valueOf(treeData.getSelectedRoot()))){
                	treeData.setSelectedRoot(-1);   //sets the selected rows
                }//end of if(!strSelectedRoot.equals(String.valueOf(treeData.getSelectedRoot())))
            }//end of else
	    	Long lGroupRegSeqID=groupRegistrationVO.getGroupRegSeqID();
	    	//gets all the required information based on selected GroupRegSeqID
	    	groupRegistrationVO = groupRegisterManagerObject.getGroup(lGroupRegSeqID);
	    	String groupID=groupRegistrationVO.getGroupID();
			//  
			
			TableData tableData =TTKCommon.getTableData(request);
			
			//create new table data object
			tableData = new TableData();
			request.getSession().setAttribute("groupID",groupID);
			//create the required grid table
			tableData.createTableInfo("EmpanelmentFilesTable",new ArrayList());
			
			tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));		
			
			tableData.setSortColumnName("UPLOAD_DATE");
			tableData.setSortOrder("DESC");
			tableData.modifySearchData("search");
			request.getSession().setAttribute("tableData",tableData);
			((DynaActionForm)form).initialize(mapping);//reset the form data
			
			ArrayList alUploadFiles=null;
			
			alUploadFiles= groupRegisterManagerObject.getHRUploadedFiles(tableData.getSearchData());
            tableData.setData(alUploadFiles, "search");
			request.getSession().setAttribute("tableData",tableData);
			frmFilesList.set("caption",strParentGroupName);
			
			request.getSession().setAttribute("frmFilesList",frmFilesList);
			return this.getForward("empalmentfiles", mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
		}//end of catch(Exception exp)
    }//end of doDefault(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
   
   
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
    		log.debug("Inside the doBackward method of HRFileSearchAction");
    		setLinks(request);
    		TableData tableData =TTKCommon.getTableData(request);
    		GroupRegistrationManager groupRegisterManagerObject=this.getGroupRegnManagerObject();
    		tableData.modifySearchData(strBackward);//modify the search data
            ArrayList alUploadFiles= groupRegisterManagerObject.getHRUploadedFiles(tableData.getSearchData());
            tableData.setData(alUploadFiles, strBackward);//set the table data
            request.getSession().setAttribute("tableData",tableData);//set the table data object to session
            return this.getForward("empalmentfiles", mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
		}//end of catch(Exception exp)
    }//end of doBackward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
  //ramana
    public ActionForward doViewUploadFiles(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		          HttpServletResponse response) throws Exception{
     try{
            log.debug("Inside HRFileSearchAction doViewPreauth");
            setLinks(request);
            TableData tableData = TTKCommon.getTableData(request);
            DynaActionForm frmFilesList=(DynaActionForm)form;
           if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
            {
          	 HRFilesDetailsVO hrFilesDetailsVO=(HRFilesDetailsVO)tableData.getRowInfo(Integer.parseInt((String)(frmFilesList).get("rownum")));
          	 
          	 
          	 //  
          	//  
          	 String filePath= hrFilesDetailsVO.getStrFilePath();
          	 String fileName= hrFilesDetailsVO.getStrFileName();
               ServletOutputStream sos=response.getOutputStream();
        		
               
        		response.setContentType("application/vnd.ms-excel");

        		response.addHeader("Content-Disposition","attachment; filename="+fileName);
             //  
        		FileInputStream fis=new FileInputStream(filePath);

        		                byte[] excelData=new byte[fis.available()];
        		                fis.read(excelData);

        		                sos.write(excelData);
        		                sos.flush();
                   if(fis!=null)fis.close();
        		                return null;

             }//end of if(!(TTKCommon.checkNull(request.getParameter("rownum")).equals("")))
           
   		
   		
           
           
           
          return mapping.findForward("empalmentfiles");
          }//end of try
          catch(TTKException expTTK)
             {
                 return this.processExceptions(request, mapping, expTTK);
               }//end of catch(TTKException expTTK)
        catch(Exception exp)
         {
            return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
          }//end of catch(Exception exp)
      }//end of doViewPreauth(ActionMapping mapping,ActionForm form,HttpServletRequest request,
              //HttpServletResponse response)


    
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
    public ActionForward doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doForward method of HRFileSearchAction");
    		setLinks(request);
    		TableData tableData =TTKCommon.getTableData(request);
    		GroupRegistrationManager groupRegisterManagerObject=this.getGroupRegnManagerObject();
    		tableData.modifySearchData(strForward);//modify the search data
    		ArrayList alUploadFiles= groupRegisterManagerObject.getHRUploadedFiles(tableData.getSearchData());
    		tableData.setData(alUploadFiles, strForward);//set the table data
            request.getSession().setAttribute("tableData",tableData);//set the table data object to session
            return this.getForward("empalmentfiles", mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
		}//end of catch(Exception exp)
    }//end of doForward(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
   
   
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
    public ActionForward doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doClose method of HRFileSearchAction");
    		setLinks(request);
    		TreeData treeData = TTKCommon.getTreeData(request);
    		GroupRegistrationManager groupRegisterManagerObject=this.getGroupRegnManagerObject();
    		treeData = TTKCommon.getTreeData(request);
		    ArrayList alGroups =null;
		    if(treeData.getSearchData() != null && treeData.getSearchData().size() > 0)
            {
		        alGroups = groupRegisterManagerObject.getGroupList(treeData.getSearchData());
		        treeData.setRootData(alGroups);
		        //set the tree data object to session
			    request.getSession().setAttribute("treeData",treeData);
			    treeData.setForwardNextLink();
                if(treeData.getSelectedRoot()>=0 && alGroups!=null && alGroups.size()>0)
                {
                    GroupRegistrationVO groupRegistrationVO = (GroupRegistrationVO)alGroups.
                    										   get(treeData.getSelectedRoot());
                    groupRegistrationVO.setBranch(groupRegisterManagerObject.getBranchList(groupRegistrationVO.
                    		getGroupRegSeqID()));
                }//end of if(treeData.getSelectedRoot()>=0 && alGroups!=null && alGroups.size()>0)
			}// end of if(treeData.getSearchData() != null && treeData.getSearchData().size() > 0)
	        return this.getForward(strGroupDetail, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
		}//end of catch(Exception exp)
    }//end of doClose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    /**
     * This method is used to Hide the Nodes/Root .
     * Finally it forwards to the appropriate view based on the specified forward mappings
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return ActionForward Where the control will be forwarded, after this request is processed
     * @throws Exception if any error occurs
     */
    public ActionForward doShowHide(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doShowHide method of HRFileSearchAction");
    		setLinks(request);
    		TreeData treeData = TTKCommon.getTreeData(request);
    		GroupRegistrationManager groupRegisterManagerObject=this.getGroupRegnManagerObject();
    		DynaActionForm generalForm = (DynaActionForm)form;
	    	GroupRegistrationVO groupRegistrationVO = null;
	    	int iSelectedRoot = Integer.parseInt((String)generalForm.get("selectedroot"));
			//create the required tree
		   	ArrayList<Object> alNodeGroups = new ArrayList<Object>();
			groupRegistrationVO = ((GroupRegistrationVO)treeData.getRootData().get(iSelectedRoot));
			alNodeGroups=groupRegisterManagerObject.getBranchList(groupRegistrationVO.getGroupRegSeqID());
			// make the selected root
			treeData.setSelectedRoot(iSelectedRoot);
			((GroupRegistrationVO)treeData.getRootData().get(iSelectedRoot)).setBranch(alNodeGroups);
			request.getSession().setAttribute("treeData",treeData);
		    return this.getForward(strGroupDetail, mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
		}//end of catch(Exception exp)
    }//end of doShowHide(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
   
 
    //ramana
    
    public ActionForward doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the doSearch method of HRFileSearchAction");
    		setLinks(request);
    		setOnlineLinks(request);
    		TableData tableData=null;
    		String strPageID =""; 
			String strSortID ="";
			//DynaActionForm frmGroupList = (DynaActionForm)form;
			
			 HttpSession session = request.getSession();
			
			 UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)session.getAttribute("UserSecurityProfile"));
    		GroupRegistrationManager groupRegisterManagerObject=this.getGroupRegnManagerObject();
    		String groupID=userSecurityProfile.getGroupID();
    		DynaActionForm dynaFrom = (DynaActionForm)form;
    		String status = dynaFrom.getString("listofStatus");
    		tableData=(TableData)request.getSession().getAttribute("tableData");
    		Column column = ((Column)((ArrayList)tableData.getTitle()).get(4));
			if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			{
				((DynaActionForm)form).initialize(mapping);//reset the form data
			}//end of if(TTKCommon.checkNull(request.getParameter("Entry")).equals("Y"))
			strPageID = TTKCommon.checkNull(request.getParameter("pageId"));
			strSortID = TTKCommon.checkNull(request.getParameter("sortId"));
			//if the page number or sort id is clicked
			if(!strPageID.equals("") || !strSortID.equals(""))
			{
				if(!strPageID.equals(""))
				{
					tableData.setCurrentPage(Integer.parseInt(TTKCommon.checkNull(request.getParameter("pageId"))));
					return (mapping.findForward("empalmentfiles"));
				}///end of if(!strPageID.equals(""))
				else
				{
					tableData.setSortData(TTKCommon.checkNull(request.getParameter("sortId")));
					tableData.modifySearchData("sort");//modify the search data
				}//end of else
			}//end of if(!strPageID.equals("") || !strSortID.equals(""))
			
			
			else
    		{
				if(status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase( "All")){
    			//create the required grid table
				tableData.createTableInfo("EmpanelmentFilesTable",null);
			    
				tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));						
				tableData.setSortColumnName("UPLOAD_DATE");
				tableData.setSortOrder("DESC");
				tableData.modifySearchData("search");
				}else{
					tableData.createTableInfo("EmpanelmentFilesTable",null);
				    
					tableData.setSearchData(this.populateSearchCriteria((DynaActionForm)form,request));						
					tableData.setSortColumnName("STATUS_CHANGED_DATE");
					tableData.setSortOrder("DESC");
					tableData.modifySearchData("search");
				}
				
    		}
				ArrayList<Object> alUploadFiles=groupRegisterManagerObject.getHRUploadedFiles(tableData.getSearchData());
				
				tableData.setData(alUploadFiles, "search");
				
				
			
			request.getSession().setAttribute("tableData",tableData);
			
			//finally return to the grid screen
			return this.getForward("empalmentfiles", mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
		}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    
    
	private ArrayList<Object> populateSearchCriteria(DynaActionForm frmFilesList,HttpServletRequest request)
    {
		TableData tableData = (TableData)request.getSession().getAttribute("tableData"); 
		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
         String userId = userSecurityProfile.getUSER_ID();
		String[] strChk = request.getParameterValues("chkopt");
		String status = request.getParameter("listofStatus");
		
        ArrayList<Object> alSearchParams = new ArrayList<Object>();
        request.getSession().getAttribute("groupID");
        //  
        alSearchParams.add((String)request.getSession().getAttribute("groupID"));
        alSearchParams.add(request.getParameter("fileUploadDate"));
        alSearchParams.add(userId);
        alSearchParams.add((String)frmFilesList.getString("completed"));
        alSearchParams.add(status);
        alSearchParams.add(strChk);
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchUser,HttpServletRequest request)

	/**
     * Returns the UserManager session object for invoking methods on it.
     * @return UserManager session object which can be used for method invokation
     * @exception throws TTKException
     */
    private GroupRegistrationManager getGroupRegnManagerObject() throws TTKException
    {
    	GroupRegistrationManager groupRegnManager = null;
    	try
        {
            if(groupRegnManager == null)
            {
                InitialContext ctx = new InitialContext();
                groupRegnManager = (GroupRegistrationManager) ctx.lookup("java:global/TTKServices/business.ejb3/GroupRegistrationManagerBean!com.ttk.business.empanelment.GroupRegistrationManager");
            }//end if
        }//end of try
        catch(Exception exp)
        {
            throw new TTKException(exp, "groupdetail");
        }//end of catch
        return groupRegnManager;
    }//end getGroupRegistrationManagerObject()

    /**
	 * Returns the HospitalManager session object for invoking methods on it.
	 * @return HospitalManager session object which can be used for method invokation
	 * @exception throws TTKException
	 */
	private HospitalManager getHospitalManagerObject() throws TTKException
	{
		HospitalManager hospManager = null;
		try
		{
			if(hospManager == null)
			{
				InitialContext ctx = new InitialContext();
				hospManager = (HospitalManager) ctx.lookup("java:global/TTKServices/business.ejb3/HospitalManagerBean!com.ttk.business.empanelment.HospitalManager");
			}//end if(hospManager == null)
		}//end of try
		catch(Exception exp)
		{
			throw new TTKException(exp, "groupdetail");
		}//end of catch
		return hospManager;
	}//end getHospitalManagerObject()
//ramana
	public String buildCaption(HttpServletRequest request) throws TTKException{
    	String strSubLink=TTKCommon.getActiveSubLink(request);
    	String strCaption= "";
    	InsuranceVO  insuranceVO=null;
    	BrokerVO  brokerVO=null;
    	String strCompanyName = "";
    	if(strSubLink.equals("Insurance"))
        {
    		insuranceVO = (InsuranceVO)request.getSession().getAttribute("SelectedOffice");
            if(insuranceVO != null){
            	strCompanyName="[";
                strCompanyName+=insuranceVO.getBranchName()+"]";
            }//end of if(insuranceVO != null)
            strCaption = strCompanyName;
    	}//end of if(strSubLink.equals("Insurance")) 
    	else if(strSubLink.equals("Broker"))// CAption Display for Broker in Managing contacts, Added by Kishor 
        {
    		brokerVO = (BrokerVO)request.getSession().getAttribute("SelectedOffice");
            if(brokerVO != null){
            	strCompanyName="[";
                strCompanyName+=brokerVO.getBranchName()+"]";
            }//end of if(insuranceVO != null)
            strCaption = strCompanyName;
    	}//end of if(strSubLink.equals("Broker")) 
    	else{
    		strCaption=(String)request.getAttribute("caption"); //Getting the caption value from grouplist action
    	}//end of else
    	return strCaption;
    }//end of buildCaption(HttpServletRequest request)
    /**
     * Check the ACL permission and set the display property of icons.
     * @param request The HTTP request we are processing
     * @param treeData TreeData for which permission has to check
     */
    private void checkTreePermission(HttpServletRequest request,TreeData treeData) throws TTKException
    {
    	DynaActionForm frmGroupList=(DynaActionForm)request.getSession().getAttribute("frmGroupList");
    	String strGroupType=TTKCommon.checkNull((String)frmGroupList.get("groupType"));
		int NOTIFICATION_ICON=4;
		if(strGroupType.equals("NCR") || strGroupType.equals("IAG"))
		{
			treeData.getTreeSetting().getRootNodeSetting().setIconVisible(NOTIFICATION_ICON,false);
			treeData.getTreeSetting().getChildNodeSetting().setIconVisible(3,false);
		}//end of if(strGroupType.equals("NCR")||strGroupType.equals("IAG"))
		
        // check the permision and set the tree for not to display respective icon
        if(TTKCommon.isAuthorized(request,"Add")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(0,false);
        }//end of if(TTKCommon.isAuthorized(request,"Add")==false)
        /*if(TTKCommon.isAuthorized(request,"Edit")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(1,false);
            treeData.getTreeSetting().getChildNodeSetting().setIconVisible(0,false);
        }//end of if(TTKCommon.isAuthorized(request,"Edit")==false)
*/        if(TTKCommon.isAuthorized(request,"Delete")==false)
        {
            treeData.getTreeSetting().getRootNodeSetting().setIconVisible(3,false);
            treeData.getTreeSetting().getChildNodeSetting().setIconVisible(2,false);
        }// end of if(TTKCommon.isAuthorized(request,"Delete")==false)
    }//end of checkTreePermission(HttpServletRequest request,TreeData treeData)
    
    public ActionForward docompleteDocument(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws Exception{
    	try{
    		log.debug("Inside the docompleteDocument method of HRFileSearchAction");
    		setLinks(request);
    		DynaActionForm frmFilesList = (DynaActionForm)form;
			GroupRegistrationManager groupRegisterManagerObject=this.getGroupRegnManagerObject();
			TableData tableData = (TableData)request.getSession().getAttribute("tableData");

			tableData.setSearchData(this.searchCriteriaForComplete(frmFilesList, request));
			tableData.setSortOrder("DESC");
			tableData.modifySearchData("search");
			
			request.getSession().setAttribute("tableData",tableData);
	//		((DynaActionForm)form).initialize(mapping);//reset the form data
    //	ArrayList searchList =	this.searchCriteriaForComplete(frmFilesList, request);
    	ArrayList<Object> alUploadFiles=groupRegisterManagerObject.getHRUploadedFiles(tableData.getSearchData());
		
		tableData.setData(alUploadFiles, "search");
			//finally return to the grid screen
			return this.getForward("empalmentfiles", mapping, request);
    	}//end of try
    	catch(TTKException expTTK)
		{
			return this.processExceptions(request, mapping, expTTK);
		}//end of catch(TTKException expTTK)
		catch(Exception exp)
		{
			return this.processExceptions(request, mapping, new TTKException(exp,strEmpnelmentFiles));
		}//end of catch(Exception exp)
    }//end of doSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
     
    private ArrayList<Object> searchCriteriaForComplete(DynaActionForm frmFilesList,HttpServletRequest request)
    {
    	String[] strChk = request.getParameterValues("chkopt");
		TableData tableData = (TableData)request.getSession().getAttribute("tableData");
		ArrayList<Object> alCacheObject = new ArrayList<Object>();
    //	TableData tableData = (TableData)request.getSession().getAttribute("tableData");
		UserSecurityProfile userSecurityProfile = ((UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile"));
        String userId = userSecurityProfile.getUSER_ID();
	//	String[] strChk = request.getParameterValues("chkopt");
		String fileSequenceIdList="";
		
	//	 StringBuffer sbfCompletedId = new StringBuffer();
		 fileSequenceIdList = fileSequenceIdList.concat("|");
	//	 sbfCompletedId.append("|");
		    if(strChk!=null&&strChk.length!=0)
		    {
		        //loop through to populate delete sequence id's and get the value from session for the matching check box value
		        for(int i=0; i<strChk.length;i++)
		        {
		           if(strChk[i]!=null)
		            {
		        	        
		                
		                	HRFilesDetailsVO hrFilesDetailsVO = new HRFilesDetailsVO();
		                	hrFilesDetailsVO.getFileSequenceId();
		                	fileSequenceIdList  = 	fileSequenceIdList.concat(String.valueOf(((HRFilesDetailsVO)tableData.getData().
		    						get(Integer.parseInt(strChk[i]))).getFileSequenceId()));
		                	
		                
		               
		            }//end of if(strChk[i]!=null)
		           fileSequenceIdList =  fileSequenceIdList.concat("|");
		        }//end of for(int i=0; i<strChk.length;i++)
		        
		    }//end of if(strChk!=null&&strChk.length!=0)
		    
		
		
		
		
		String status = frmFilesList.getString("listofStatus");
		String completedstatus = "Y";
		
        ArrayList<Object> alSearchParams = new ArrayList<Object>();
        alSearchParams.add((String)request.getSession().getAttribute("groupID"));
        alSearchParams.add((String)frmFilesList.getString("fileUploadDate"));
       // alSearchParams.add((String)frmFilesList.getString("fileUploadDate"));
        frmFilesList.set("completed", completedstatus);
        alSearchParams.add(userId);
        alSearchParams.add(completedstatus);
        alSearchParams.add(status);
        alSearchParams.add(fileSequenceIdList);
       
        return alSearchParams;
    }//end of populateSearchCriteria(DynaActionForm frmSearchUser,HttpServletRequest request)

}//end of class GroupListAction
