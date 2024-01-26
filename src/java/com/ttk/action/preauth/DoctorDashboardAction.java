package com.ttk.action.preauth;

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
import com.ttk.business.preauth.PreAuthDashboardManager;
import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.common.Toolbar;
import com.ttk.dto.preauth.DoctorDashboardUserVO;
import com.ttk.dto.preauth.PreDashboardVO;
import com.ttk.dto.usermanagement.UserSecurityProfile;

public class DoctorDashboardAction extends TTKAction{
	
	private static Logger log = Logger.getLogger(DoctorDashboardAction.class);
	private static final String strPreauthDoctorDashboard="DoctorDashboard";
	private static final String strPreauthDoctorDashboardPreDetails="DoctorDashboardPreDetails";
	 private static final String strPreDashboardError="DoctorDashboard";
		
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
	 
		public ActionForward doDisplayDoctorDashboardDefault(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			try {
				/*setLinks(request);*/
				log.debug("Inside PreAuthDashboardAction doDefault");
				PreAuthDashboardManager preAuthDashboardManagerobject= this.getPreAuthDashboardManagerObject();
				Long  userSeqId = TTKCommon.getUserSeqId(request);
				DynaActionForm frmDashboardList= (DynaActionForm)form;  
				frmDashboardList.initialize(mapping);
				PreDashboardVO  preDashboardVO = preAuthDashboardManagerobject.getDoctorDashboardDetails(userSeqId);
				
				if(preDashboardVO !=null){
				frmDashboardList.set("coding_cases",preDashboardVO.getCoding_cases());
				frmDashboardList.set("highValuePA",preDashboardVO.getHighValuePA());
				frmDashboardList.set("shortfall_cases", preDashboardVO.getShortfall_cases());
				frmDashboardList.set("shortfall_respond_ases", preDashboardVO.getShortfall_respond_ases());
				frmDashboardList.set("enhanced_shortfall_cases", preDashboardVO.getEnhanced_shortfall_cases());
				frmDashboardList.set("enhanced_shrtfal_res_cases", preDashboardVO.getEnhanced_shrtfal_res_cases());
				
				frmDashboardList.set("approved_cases", preDashboardVO.getApproved_cases());
				frmDashboardList.set("rejected_cases", preDashboardVO.getRejected_cases());
				frmDashboardList.set("forResolution", preDashboardVO.getForResolution());
				frmDashboardList.set("enhance_inp_cases",preDashboardVO.getEnhance_case());
				frmDashboardList.set("appeal_inp_cases",preDashboardVO.getAppeal_case());
				frmDashboardList.set("doctor_logedin_case",preDashboardVO.getDoctorLogedin());
				frmDashboardList.set("fress_case",preDashboardVO.getFresh_case());
				
			  }else{
					frmDashboardList.set("coding_cases","0");
					frmDashboardList.set("highValuePA","0");
					frmDashboardList.set("shortfall_cases","0");
					frmDashboardList.set("shortfall_respond_ases","0");
					frmDashboardList.set("enhanced_shortfall_cases","0");
					frmDashboardList.set("enhanced_shrtfal_res_cases","0");
					
					frmDashboardList.set("approved_cases", "0");
					frmDashboardList.set("rejected_cases","0");
					frmDashboardList.set("forResolution","0");
					frmDashboardList.set("enhance_inp_cases","0");
					frmDashboardList.set("appeal_inp_cases","0");
					frmDashboardList.set("doctor_logedin_case","0");
					frmDashboardList.set("fress_case","0");
			  }
				TableData tDtvUserInfo = new TableData();	
				tDtvUserInfo.createTableInfo("PreauthUserInfoTable",new ArrayList());
				ArrayList alPreauthUserDashBoadinfo = preAuthDashboardManagerobject.getPreauthUserDashBoadinfo(tDtvUserInfo.getSearchData());
				
				tDtvUserInfo.setData((ArrayList)alPreauthUserDashBoadinfo.get(0), "search");
				request.getSession().removeAttribute("tDtvUserInfo");
				request.getSession().setAttribute("tDtvUserInfo",tDtvUserInfo);
				
				TableData tDtvUserInfo2 = new TableData();
				tDtvUserInfo2.createTableInfo("PreauthUserInfoTable",new ArrayList());
				
				tDtvUserInfo2.setData((ArrayList)alPreauthUserDashBoadinfo.get(1) != null ? (ArrayList)alPreauthUserDashBoadinfo.get(1) :new  ArrayList(), "search");
				request.getSession().removeAttribute("tDtvUserInfo2");
				request.getSession().setAttribute("tDtvUserInfo2",tDtvUserInfo2);
				
				TableData tDtvUserInfo3 = new TableData();
				tDtvUserInfo3.createTableInfo("PreauthUserInfoTable",new ArrayList());
				
				tDtvUserInfo3.setData((ArrayList)alPreauthUserDashBoadinfo.get(2) != null ? (ArrayList)alPreauthUserDashBoadinfo.get(2) :new  ArrayList(), "search");
				request.getSession().removeAttribute("tDtvUserInfo3");
				request.getSession().setAttribute("tDtvUserInfo3",tDtvUserInfo3);
				
				TableData tDtvUserInfo4 = new TableData();
				tDtvUserInfo4.createTableInfo("PreauthUserInfoTable",new ArrayList());
				
				tDtvUserInfo4.setData((ArrayList)alPreauthUserDashBoadinfo.get(3) != null ? (ArrayList)alPreauthUserDashBoadinfo.get(3) :new  ArrayList(), "search");
				request.getSession().removeAttribute("tDtvUserInfo4");
				request.getSession().setAttribute("tDtvUserInfo4",tDtvUserInfo4);
				
			
				
				return this.getForward(strPreauthDoctorDashboard, mapping, request);
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strPreauthDoctorDashboard));
			}// end of catch(Exception exp)
		}// end of doDefault(ActionMapping mapping,ActionForm
			// form,HttpServletRequest request,HttpServletResponse response)
		
		
		public ActionForward doDisplaydoctordashboardPreDetails(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			try {
				/*setLinks(request);*/
				log.debug("Inside PreAuthDashboardAction doDefault");
				HttpSession session = request.getSession();
				PreAuthDashboardManager preAuthDashboardManagerobject= this.getPreAuthDashboardManagerObject();
				UserSecurityProfile userSecurityProfile = (UserSecurityProfile) request.getSession().getAttribute("UserSecurityProfile");
				
				Toolbar toolBar = (Toolbar) session.getAttribute("toolbar");
				Long  userSeqId = TTKCommon.getUserSeqId(request);
				DynaActionForm frmDashboardList= (DynaActionForm)form;  
				frmDashboardList.initialize(mapping);
				PreDashboardVO  preDashboardVO = preAuthDashboardManagerobject.getDoctorDashboardDetails(userSeqId);
				if(preDashboardVO !=null){
				
				frmDashboardList.set("coding_cases",preDashboardVO.getCoding_cases());
				frmDashboardList.set("highValuePA",preDashboardVO.getHighValuePA());
				frmDashboardList.set("shortfall_cases", preDashboardVO.getShortfall_cases());
				frmDashboardList.set("shortfall_respond_ases", preDashboardVO.getShortfall_respond_ases());
				frmDashboardList.set("enhanced_shortfall_cases", preDashboardVO.getEnhanced_shortfall_cases());
				frmDashboardList.set("enhanced_shrtfal_res_cases", preDashboardVO.getEnhanced_shrtfal_res_cases());
				
				frmDashboardList.set("approved_cases", preDashboardVO.getApproved_cases());
				frmDashboardList.set("rejected_cases", preDashboardVO.getRejected_cases());
				frmDashboardList.set("forResolution", preDashboardVO.getForResolution());
				
				frmDashboardList.set("enhance_inp_cases",preDashboardVO.getEnhance_case());
				frmDashboardList.set("appeal_inp_cases",preDashboardVO.getAppeal_case());
				frmDashboardList.set("doctor_logedin_case",preDashboardVO.getDoctorLogedin());
				frmDashboardList.set("fress_case",preDashboardVO.getFresh_case());
				
				}else{
					frmDashboardList.set("coding_cases","0");
					frmDashboardList.set("highValuePA","0");
					frmDashboardList.set("shortfall_cases","0");
					frmDashboardList.set("shortfall_respond_ases","0");
					frmDashboardList.set("enhanced_shortfall_cases","0");
					frmDashboardList.set("enhanced_shrtfal_res_cases","0");
					
					frmDashboardList.set("approved_cases", "0");
					frmDashboardList.set("rejected_cases","0");
					frmDashboardList.set("forResolution","0");
					
					frmDashboardList.set("enhance_inp_cases","0");
					frmDashboardList.set("appeal_inp_cases","0");
					frmDashboardList.set("doctor_logedin_case","0");
					frmDashboardList.set("fress_case","0");
				}
				TableData tableData = new TableData();
				tableData.createTableInfo("PreauthCaseInfoTable",new ArrayList());
				ArrayList alPreauthBoardinfoDetails = preAuthDashboardManagerobject.getPreauthBoardinfo(tableData.getSearchData());
				tableData.setData(alPreauthBoardinfoDetails, "search");
				request.getSession().setAttribute("tableDataTeamDashbord",tableData);
				
				
				return this.getForward(strPreauthDoctorDashboardPreDetails, mapping, request);
			}// end of try
			catch (TTKException expTTK) {
				return this.processExceptions(request, mapping, expTTK);
			}// end of catch(TTKException expTTK)
			catch (Exception exp) {
				return this.processExceptions(request, mapping, new TTKException(
						exp, strPreauthDoctorDashboardPreDetails));
			}// end of catch(Exception exp)
		}// end of doDefault(ActionMapping mapping,ActionForm
			// form,HttpServletRequest request,HttpServletResponse response)
		
	
		private PreAuthDashboardManager getPreAuthDashboardManagerObject() throws TTKException
		{
			PreAuthDashboardManager preAuthDashboardManager = null;
			try
			{
				if(preAuthDashboardManager == null)
				{
					InitialContext ctx = new InitialContext();
					preAuthDashboardManager = (PreAuthDashboardManager) ctx.lookup("java:global/TTKServices/business.ejb3/PreAuthDashboardManagerBean!com.ttk.business.preauth.PreAuthDashboardManager");

				}//end of if(PreAuthDashboardManager == null)
			}//end of try
			catch(Exception exp)
			{
				throw new TTKException(exp,strPreDashboardError );
			}//end of catch
			return preAuthDashboardManager;
		}//end getPreAuthSupportManagerObject()

}
