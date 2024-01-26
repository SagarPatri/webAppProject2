package com.ttk.common.tags;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.action.DynaActionForm;

import com.ttk.common.TTKCommon;
import com.ttk.common.security.Cache;
import com.ttk.dto.common.CacheObject;

public class SMSEmailTriggerInformation extends TagSupport{
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger( AdditionalInformation.class );
	public int doStartTag() throws JspException {
		try
		{
			log.debug("Inside AdditionalInformation TAG :");
			JspWriter out = pageContext.getOut();
			String strActiveSubLink=TTKCommon.getActiveSubLink((HttpServletRequest)pageContext.getRequest());
			// get the reference of the frmUserContact
			DynaActionForm generalForm=(DynaActionForm)((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute("frmUserContact");
			DynaActionForm smsEmailTriggerInfoForm=(DynaActionForm)generalForm.get("smsEmailTriggerInfoVO");
			ArrayList alOfficeInfo=null;
			ArrayList alDepartment=new ArrayList();
			ArrayList alFrequency = null;
			CacheObject cacheObject = null;
			alOfficeInfo=Cache.getCacheObject("officeInfo");
			alDepartment=Cache.getCacheObject("departmentID");
			alFrequency = Cache.getCacheObject("frequencyInfo");
//			String strContactTypeID="",residentYN="";
			String strvipMemberPreauthSMSTriggerYN="",strvipMemberPreauthEmailTriggerYN="",strvipMemberPreauthPrimaryContactYN="",strvipMemberPreauthAlternativeContactYN="";
			String strvipMemberPreauthMemberRequestYN="", strvipMemberPreauthNetworkRequestYN="", strFrequencyIDForVipPreauth="", strvipMemberClaimSMSTriggerYN="", strvipMemberClaimEmailTriggerYN="";
			String strvipMemberClaimPrimaryContactYN = "",strvipMemberClaimAlternativeContactYN = "", strvipMemberClaimMemberRequestYN ="", strvipMemberClaimNetworkRequestYN ="" ;
			String strFrequencyIDForVipClaim = "", strnonVIPMemberPreauthSMSTriggerYN ="", strnonVIPMemberPreauthEmailTriggerYN ="",strnonVIPMemberPreauthPrimaryContactYN ="" ;
			String strnonVIPMemberPreauthAlternativeContactYN ="", strnonVIPMemberPreauthMemberRequestYN ="",strnonVIPMemberPreauthNetworkRequestYN ="",strFrequencyIDForNonVipPreauth ="";
			String strnonVIPMemberClaimSMSTriggerYN = "", strnonVIPMemberClaimEmailTriggerYN ="", strnonVIPMemberClaimPrimaryContactYN="", strnonVIPMemberClaimAlternativeContactYN ="";
			String strnonVIPMemberClaimMemberRequestYN = "", strnonVIPMemberClaimNetworkRequestYN ="", strFrequencyIDForNonVipClaim="", struserPermissiontoAuthorizedUserPreapprovalRejection="", struserPermissiontoAuthorizedUserClaimRejection="";
			String strUserManagement="User Management";
			String strHospital="Hospital";
			String strPartner="Partner";
			String strViewmode=" Disabled ";
			if(TTKCommon.isAuthorized((HttpServletRequest)pageContext.getRequest(),"Edit") || TTKCommon.isAuthorized((HttpServletRequest)pageContext.getRequest(),"Add"))
			{
				strViewmode="";
			}//end of if(TTKCommon.isAuthorized((HttpServletRequest)pageContext.getRequest(),"Edit") || TTKCommon.isAuthorized((HttpServletRequest)pageContext.getRequest(),"Add"))
			
			if(strActiveSubLink.equals(strUserManagement))
			{
				strvipMemberPreauthSMSTriggerYN=(String)smsEmailTriggerInfoForm.get("vipMemberPreauthSMSTriggerYN");
				strvipMemberPreauthEmailTriggerYN=(String)smsEmailTriggerInfoForm.get("vipMemberPreauthEmailTriggerYN");

			    strvipMemberPreauthPrimaryContactYN=(String)smsEmailTriggerInfoForm.get("vipMemberPreauthPrimaryContactYN");
			    strvipMemberPreauthAlternativeContactYN = (String) smsEmailTriggerInfoForm.get("vipMemberPreauthAlternativeContactYN");
			    strvipMemberPreauthMemberRequestYN = (String) smsEmailTriggerInfoForm.get("vipMemberPreauthMemberRequestYN");
			    strvipMemberPreauthNetworkRequestYN = (String) smsEmailTriggerInfoForm.get("vipMemberPreauthNetworkRequestYN");
			    strFrequencyIDForVipPreauth = (String) smsEmailTriggerInfoForm.get("strFrequencyIDForVipPreauth");
			    strvipMemberClaimSMSTriggerYN = (String) smsEmailTriggerInfoForm.get("vipMemberClaimSMSTriggerYN");
			    strvipMemberClaimEmailTriggerYN = (String) smsEmailTriggerInfoForm.get("vipMemberClaimEmailTriggerYN");
			    strvipMemberClaimPrimaryContactYN = (String) smsEmailTriggerInfoForm.get("vipMemberClaimPrimaryContactYN");
			    strvipMemberClaimAlternativeContactYN = (String) smsEmailTriggerInfoForm.get("vipMemberClaimAlternativeContactYN");
			    strvipMemberClaimMemberRequestYN = (String) smsEmailTriggerInfoForm.get("vipMemberClaimMemberRequestYN");
			    strvipMemberClaimNetworkRequestYN = (String) smsEmailTriggerInfoForm.get("vipMemberClaimNetworkRequestYN");
			    strFrequencyIDForVipClaim = (String) smsEmailTriggerInfoForm.get("strFrequencyIDForVipClaim");
			    strnonVIPMemberPreauthSMSTriggerYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberPreauthSMSTriggerYN");
			    strnonVIPMemberPreauthEmailTriggerYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberPreauthEmailTriggerYN");
			    strnonVIPMemberPreauthPrimaryContactYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberPreauthPrimaryContactYN");
			    strnonVIPMemberPreauthAlternativeContactYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberPreauthAlternativeContactYN");
			    strnonVIPMemberPreauthMemberRequestYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberPreauthMemberRequestYN");
			    strnonVIPMemberPreauthNetworkRequestYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberPreauthNetworkRequestYN");
			    strFrequencyIDForNonVipPreauth = (String) smsEmailTriggerInfoForm.get("strFrequencyIDForNonVipPreauth");
			    strnonVIPMemberClaimSMSTriggerYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberClaimSMSTriggerYN");
			    strnonVIPMemberClaimEmailTriggerYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberClaimEmailTriggerYN");
			    strnonVIPMemberClaimPrimaryContactYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberClaimPrimaryContactYN");
			    strnonVIPMemberClaimAlternativeContactYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberClaimAlternativeContactYN");
			    strnonVIPMemberClaimMemberRequestYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberClaimMemberRequestYN");
			    strnonVIPMemberClaimNetworkRequestYN = (String) smsEmailTriggerInfoForm.get("nonVIPMemberClaimNetworkRequestYN");
			    strFrequencyIDForNonVipClaim = (String) smsEmailTriggerInfoForm.get("strFrequencyIDForNonVipClaim");
			    struserPermissiontoAuthorizedUserPreapprovalRejection = (String) smsEmailTriggerInfoForm.get("userPermissiontoAuthorizedUserPreapprovalRejection");
			    struserPermissiontoAuthorizedUserClaimRejection = (String) smsEmailTriggerInfoForm.get("userPermissiontoAuthorizedUserClaimRejection");
				DynaActionForm userAccessInfoForm=(DynaActionForm)generalForm.get("userAccessVO");
				String strUserType=(String)userAccessInfoForm.getString("userType");
				
			  
				// Added new block if it is ttk user then only it will apply
				
				if(strUserType.equals("TTK")){

                    out.print("<fieldset><legend>SMS/Email Trigger Configuration and Authorization Information</legend>");
                    out.print("<table border=\"1\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"formContainer\">");
                    if(strUserType.equals("TTK")){
                    out.print("<tr>");
					out.print("<td width=\"25%\" CLASS=\"formLabel\">");
					out.print("<b>");
					out.print("SMS And Email Trigger ");
					out.print("</b>");
					out.print("</td>");
					
					out.print("<td width=\"8%\" CLASS=\"formLabel\">");
					out.print("Email Trigger ");
					out.print("</td>");
					
					out.print("<td width=\"8%\" CLASS=\"formLabel\">");
					out.print("SMS Trigger ");
					out.print("</td>");
					
					out.print("<td width=\"10%\" CLASS=\"formLabel\">");
					out.print("Primary Contact ");
					out.print("</td>");
					
					out.print("<td width=\"15%\" CLASS=\"formLabel\">");
					out.print("Alternative Contact ");
					out.print("</td>");
					
					out.print("<td width=\"10%\" CLASS=\"formLabel\">");
					out.print("Member Request ");
					out.print("</td>");
					
					
					out.print("<td width=\"10%\" CLASS=\"formLabel\">");
					out.print("Network Request ");
					out.print("</td>");
					
					out.print("<td width=\"17%\" CLASS=\"formLabel\">");
					out.print("Frequency ");
					out.print(" <span class=\"mandatorySymbol\">*</span>");
					out.print("</td>");
					out.print("</tr>");
					
					
				/*add roes*/
					out.print("<tr>");
					out.print("<td width=\"25%\" CLASS=\"formLabel\">");
					out.print("VIP Member Pre-Approval Assign To User");
					out.print("</td>");
					
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberPreauthEmailTriggerYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberPreauthEmailTriggerYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberPreauthEmailTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberPreauthSMSTriggerYNFlagID\" name=\"smsEmailTriggerInfoVO.vipMemberPreauthSMSTriggerYN\"  value=\"Y\" "+TTKCommon.isChecked(strvipMemberPreauthSMSTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberPreauthPrimaryContactYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberPreauthPrimaryContactYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberPreauthPrimaryContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberPreauthAlternativeContactYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberPreauthAlternativeContactYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberPreauthAlternativeContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<b>");
					out.print("-");
					out.print("</b>");
					out.print("</td>");
					
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberPreauthNetworkRequestYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberPreauthNetworkRequestYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberPreauthNetworkRequestYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"17%\" CLASS=\"formLabel\">");
					if(alFrequency != null && alFrequency.size() > 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForVipPreauth\" class=\"selectBox selectBoxMedium\""+strViewmode+" >");
						for(int i=0; i < alFrequency.size(); i++)
						{
							cacheObject = (CacheObject)alFrequency.get(i);
							out.print("<option value='"+cacheObject.getCacheId()+"' "+TTKCommon.isSelected(strFrequencyIDForVipPreauth, cacheObject.getCacheId())+">"+cacheObject.getCacheDesc()+"</option>");
						}//end of for(int i=0; i < alOfficeInfo.size(); i++)
						out.print("</select>");
					}//end of if(alOfficeInfo != null && alOfficeInfo.size() > 0)
					else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForVipPreauth\" class=\"selectBox selectBoxMedium\""+strViewmode+">");
						out.print("</select>");
					}//end of else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					out.print("</td>");
					out.print("</tr>");
					
			          	/*'''@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'''*/
					out.print("<tr>");
					out.print("<td width=\"25%\" CLASS=\"formLabel\">");
					out.print("VIP Member Claim Assign To User");
					out.print("</td>");
					
					
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberClaimEmailTriggerYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberClaimEmailTriggerYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberClaimEmailTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberClaimSMSTriggerYNFlagID\" name=\"smsEmailTriggerInfoVO.vipMemberClaimSMSTriggerYN\"  value=\"Y\" "+TTKCommon.isChecked(strvipMemberClaimSMSTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberClaimPrimaryContactYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberClaimPrimaryContactYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberClaimPrimaryContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberClaimAlternativeContactYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberClaimAlternativeContactYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberClaimAlternativeContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberClaimMemberRequestYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberClaimMemberRequestYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberClaimMemberRequestYN) +strViewmode+">");
					out.print("</td>");
					
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"vipMemberClaimNetworkRequestYNFlagID \" name=\"smsEmailTriggerInfoVO.vipMemberClaimNetworkRequestYN\" value=\"Y\" "+TTKCommon.isChecked(strvipMemberClaimNetworkRequestYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"17%\" CLASS=\"formLabel\">");
					if(alFrequency != null && alFrequency.size() > 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForVipClaim\" class=\"selectBox selectBoxMedium\""+strViewmode+">");
						for(int i=0; i < alFrequency.size(); i++)
						{
							cacheObject = (CacheObject)alFrequency.get(i);
							out.print("<option value='"+cacheObject.getCacheId()+"' "+TTKCommon.isSelected(strFrequencyIDForVipClaim, cacheObject.getCacheId())+">"+cacheObject.getCacheDesc()+"</option>");
						}//end of for(int i=0; i < alOfficeInfo.size(); i++)
						out.print("</select>");
					}//end of if(alOfficeInfo != null && alOfficeInfo.size() > 0)
					else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForVipClaim\" class=\"selectBox selectBoxMedium\""+strViewmode+">");
						out.print("</select>");
					}//end of else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					out.print("</td>");
					out.print("</tr>");
					
					
					
					/*'''@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'''*/
					out.print("<tr>");
					out.print("<td width=\"25%\" CLASS=\"formLabel\">");
					out.print(" Non VIP Member Pre-Approval Assign To User");
					out.print("</td>");
					
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberPreauthEmailTriggerYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberPreauthEmailTriggerYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberPreauthEmailTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberPreauthSMSTriggerYNFlagID\" name=\"smsEmailTriggerInfoVO.nonVIPMemberPreauthSMSTriggerYN\"  value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberPreauthSMSTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberPreauthPrimaryContactYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberPreauthPrimaryContactYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberPreauthPrimaryContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberPreauthAlternativeContactYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberPreauthAlternativeContactYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberPreauthAlternativeContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<b>");
					out.print("-");
					out.print("</b>");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberPreauthNetworkRequestYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberPreauthNetworkRequestYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberPreauthNetworkRequestYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"17%\" CLASS=\"formLabel\">");
					if(alFrequency != null && alFrequency.size() > 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForNonVipPreauth\" class=\"selectBox selectBoxMedium\""+strViewmode+">");
						for(int i=0; i < alFrequency.size(); i++)
						{
							cacheObject = (CacheObject)alFrequency.get(i);
							out.print("<option value='"+cacheObject.getCacheId()+"' "+TTKCommon.isSelected(strFrequencyIDForNonVipPreauth, cacheObject.getCacheId())+">"+cacheObject.getCacheDesc()+"</option>");
						}//end of for(int i=0; i < alOfficeInfo.size(); i++)
						out.print("</select>");
					}//end of if(alOfficeInfo != null && alOfficeInfo.size() > 0)
					else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForNonVipPreauth\" class=\"selectBox selectBoxMedium\""+strViewmode+">");
						out.print("</select>");
					}//end of else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					out.print("</td>");
					out.print("</tr>");
					
					/*'''@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'''*/
					out.print("<tr>");
					out.print("<td width=\"25%\" CLASS=\"formLabel\">");
					out.print("Non VIP Member Claim Assign To User");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberClaimEmailTriggerYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberClaimEmailTriggerYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberClaimEmailTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberClaimSMSTriggerYNFlagID\" name=\"smsEmailTriggerInfoVO.nonVIPMemberClaimSMSTriggerYN\"  value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberClaimSMSTriggerYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberClaimPrimaryContactYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberClaimPrimaryContactYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberClaimPrimaryContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberClaimAlternativeContactYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberClaimAlternativeContactYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberClaimAlternativeContactYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberClaimMemberRequestYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberClaimMemberRequestYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberClaimMemberRequestYN) +strViewmode+">");
					out.print("</td>");
					
					
					out.print("<td width=\"8%\" align=\"center\">");
					out.print("<input type=\"checkbox\" id=\"nonVIPMemberClaimNetworkRequestYNFlagID \" name=\"smsEmailTriggerInfoVO.nonVIPMemberClaimNetworkRequestYN\" value=\"Y\" "+TTKCommon.isChecked(strnonVIPMemberClaimNetworkRequestYN) +strViewmode+">");
					out.print("</td>");
					
					out.print("<td width=\"17%\" CLASS=\"formLabel\">");
					if(alFrequency != null && alFrequency.size() > 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForNonVipClaim\" class=\"selectBox selectBoxMedium\""+strViewmode+">");
						for(int i=0; i < alFrequency.size(); i++)
						{
							cacheObject = (CacheObject)alFrequency.get(i);
							out.print("<option value='"+cacheObject.getCacheId()+"' "+TTKCommon.isSelected(strFrequencyIDForNonVipClaim, cacheObject.getCacheId())+">"+cacheObject.getCacheDesc()+"</option>");
						}//end of for(int i=0; i < alOfficeInfo.size(); i++)
						out.print("</select>");
					}//end of if(alOfficeInfo != null && alOfficeInfo.size() > 0)
					else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					{
						out.print("<select name=\"smsEmailTriggerInfoVO.strFrequencyIDForNonVipClaim\" class=\"selectBox selectBoxMedium\""+strViewmode+">");
						out.print("</select>");
					}//end of else if(alOfficeInfo != null && alOfficeInfo.size() == 0)
					out.print("</td>");
					out.print("</tr>");
					
				}
                    out.print("</table>");
                    out.print("<table border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"formContainer\">");
                    out.print("<tr>");
					out.print("<td>");
					out.print("<b>");
					out.print("User permission for VIP Pre-approval Rejection :");
					out.print("</b>");
					out.print("</td>");
					
					out.print("<td>");
					out.print("<input type=\"checkbox\" id=\"userPermissiontoAuthorizedUserPreapprovalRejectionFlagID\" class=\"smsEmailTriggerChkbox\" name=\"smsEmailTriggerInfoVO.userPermissiontoAuthorizedUserPreapprovalRejection\" value=\"Y\" "+TTKCommon.isChecked(struserPermissiontoAuthorizedUserPreapprovalRejection) +strViewmode+">");
					out.print("</td>");
					
					out.print("<tr>");
					out.print("<td>");
					out.print("<b>");
					out.print("User permission for VIP Claim Rejection :");
					out.print("</b>");
					out.print("</td>");
					
					out.print("<td>");
					out.print("<input type=\"checkbox\" id=\"userPermissiontoAuthorizedUserClaimRejectionFlagID\" class=\"smsEmailTriggerChkbox\" name=\"smsEmailTriggerInfoVO.userPermissiontoAuthorizedUserClaimRejection\" value=\"Y\" "+TTKCommon.isChecked(struserPermissiontoAuthorizedUserClaimRejection) +strViewmode+">");
					out.print("</td>");
					out.print("</table>");
					out.print("</fieldset>");
				}
			}//end of if(strActiveSubLink.equals(strUserManagement))
			
		}//end of try block
		catch(Exception exp)
		{
			exp.printStackTrace();
		}//end of catch block
		return SKIP_BODY;
	}//end of doStartTag()

	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;//to process the rest of the page
	}//end doEndTag()
}
