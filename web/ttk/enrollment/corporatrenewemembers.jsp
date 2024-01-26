<%@page import="com.ttk.common.TTKPropertiesReader"%>
<%
/**
 * @ (#) corporatemembers.jsp Feb 3rd 2006
 * Project      : TTK HealthCare Services
 * File         : corporatemembers.jsp
 * Author       : Krishna K H
 * Company      : Span Systems Corporation
 * Date Created : Feb 3rd 2006
 *
 * @author       :  Rishi Sharma
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.WebBoardHelper,com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.common.TTKPropertiesReader"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/enrollment/corporatemember.js"></SCRIPT>
<script> TC_Disabled = true;</script>
<%
String softCopyUrl=TTKPropertiesReader.getPropertyValue("EnrolmentSoftCopyUrl");
%>

<%! String policy_status = null, strActiveSubLink = null;
	long policy_num,productTyp_num,insComp_num,grpId_num;
%>


<%  policy_status =(String)request.getSession().getAttribute("policy_status");
//System.out.println(" policy_status  in corporatrenewemembers.jsp  ====> "+policy_status);
    pageContext.getSession().setAttribute("policy_status","RTS");
    pageContext.getSession().setAttribute("Corporate Policy","Corporate Policy");
    
    policy_num =((Long)request.getSession().getAttribute("policy_num")).longValue();
	productTyp_num=((Long)request.getSession().getAttribute("productTyp_num")).longValue();
	insComp_num=((Long)request.getSession().getAttribute("insComp_num")).longValue();
	grpId_num=((Long)request.getSession().getAttribute("grpId_num")).longValue();
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CorporateMemberAction.do" >
<%UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");%>
<logic:empty name="frmMember" property="display">
	<!-- S T A R T : Page Title -->
	<logic:match name="frmPolicyList" property="switchType" value="ENM">
		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	</logic:match>
	<logic:match name="frmPolicyList" property="switchType" value="END">
		<table align="center" class="pageTitleHilite" border="0" cellspacing="0" cellpadding="0">
	</logic:match>
	<tr>
    <td width="50%">List of Members - <bean:write name="frmMember" property="caption"/></td>
    <td width="50%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
    </tr>
</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">

	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:message name="updated" scope="request"/>
			  </td>
			</tr>
		</table>
	</logic:notEmpty>
	<!-- E N D : Success Box -->
	<html:errors/>
    <table align="center" class="searchContainer rcContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td nowrap>Al Koot No.:<br>
             <html:text property="sEnrollmentNbr" styleClass="textBox textBoxMedium" maxlength="60" />
        </td>
        
        
             <td nowrap>Qatar ID:<br>
             <html:text property="sQatarId" styleClass="textBox textBoxMedium" maxlength="60" />
        </td>
        
        
        
        <td nowrap>
         <% String strSublink = TTKCommon.getActiveSubLink(request);
        	pageContext.setAttribute("strSublink",strSublink);
         %>
       <logic:equal name="strSublink" value="Corporate Policy" >
        	Employee Name:
        </logic:equal>
        <logic:equal name="strSublink" value="Non-Corporate Policy" >
        	Member Name:
        </logic:equal>
        	<br><html:text property="sMemberName" styleClass="textBox textBoxMedium" maxlength="60" />
        </td>
        <logic:equal name="strSublink" value="Corporate Policy" >
        <td nowrap>Employee No.:<br>
             <html:text property="sEmployeeNum" styleClass="textBox textBoxMedium" maxlength="60" />
        </td>
        </logic:equal>
        <logic:equal name="strSublink" value="Corporate Policy" >
        <td nowrap>Member Name:
        	<br><html:text property="sCorpMemberName" styleClass="textBox textBoxMedium" maxlength="60" />
        </td>
        </logic:equal>
         <logic:equal name="strSublink" value="Non-Corporate Policy" >
        <td nowrap>Certificate No.:
        	<br><html:text property="sCertificateNumber" styleClass="textBox textBoxMedium" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement);"/>
        </td>
        <td nowrap>Order No.:
        	<br><html:text property="sOrderNumber" styleClass="textBox textBoxMedium" maxlength="60" />
        </td>
        </logic:equal>
        <td align="left" width="100%" valign="bottom" nowrap>
        <table>
        <tr>
        
        <td><a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>
        
<%--         <td><a href="#" onClick="javascript:window.open('<%=softCopyUrl%>', '', '', '')" >Soft Copy Enrolment Upload</a></td>
 --%>         <td><a href="#" onClick="javascript:softcopyUpload()" >Soft Copy Enrolment Upload</a></td>
       
        </tr>
        </table>
        </td>
      </tr>   
	      
    </table>
    <!-- S T A R T : Form Fields -->
	<table align="center" class="formContainer rcContainer" border="0" cellspacing="1" cellpadding="0">
	  <tr>
    	<td height="10" colspan="2"></td>
	  </tr>
	  <tr>
	    <td colspan="2">
    		<ttk:TreeComponent name="treeData"/> <br>
	    </td>
	  </tr>
	  <tr>
		<td align="right" colspan="2" class="buttonsContainerGrid">
			<%
	    		if(TTKCommon.isTreeDataFound(request,"treeData") && WebBoardHelper.getPolicySeqId(request)!=null && TTKCommon.isAuthorized(request,"Add") && WebBoardHelper.getCompletedYN(request).equals("Y"))
				{
		     %>
				     <button type="button" name="Button" accesskey="p" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:approveCard();">A<u>p</u>prove Card Printing for All</button>&nbsp;
			<%
				}//end of if(TTKCommon.isTreeDataFound(request,"treeData") && WebBoardHelper.getPolicySeqId(request)!=null&& TTKCommon.isAuthorized(request,"Add"))
			%>
				<%
				
				if(TTKCommon.isAuthorized(request,"DefineRule"))
				{
			%>
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClearRules();"><u>C</u>lear Rules</button>&nbsp;
			<%
				}//end of if(TTKCommon.isAuthorized(request,"DefineRule"))
			%>
			<button type="button" name="Button" accesskey="v" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onShowError();">Show <u>V</u>alidation Error</button>
		</td>
	  </tr>
	  <ttk:TreePageLinks name="treeData"/>
	</table>
	<!-- E N D : Form Fields -->
<p>&nbsp;</p>
</div>


<!-- E N D : Buttons --></div>

	<!-- E N D : Content/Form Area -->
<!-- E N D : Main Container Table -->
 	<html:hidden property="rownum"/>
    <html:hidden property="mode" />
    <input type="hidden" name="child" value="">
    <input type="hidden" name="selectedroot" value="">
    <input type="hidden" name="selectednode" value="">
     <%  if(request.getSession().getAttribute("policy_status")!=null)%>
       <input type="hidden" name="policystatus" id="policystatus" value="<%=policy_status %>">
    <input type="hidden" name="pageId" value="">
    <input type="hidden" name="sPolicySeqID" value="7">
    <input type="hidden" name="userSeqId" value="<%= TTKCommon.getUserID(request)%>">
    <input type="hidden" name="EnrollmentUploadURL" value="<%=TTKPropertiesReader.getPropertyValue("EnrollmentUploadURL") %>"/>
     <input type="hidden" name="EnrollmentSoftUploadURL" value="<%=TTKPropertiesReader.getPropertyValue("EnrolmentSoftCopyUrl") %>"/>
     
       <!-- Required data for softcopy upload.-->
    <input type="hidden" name="policy_num" id="policy_num" value="<%=policy_num %>">
    <input type="hidden" name="productTyp_num" id="productTyp_num" value="<%=productTyp_num %>">
    <input type="hidden" name="insComp_num" id="insComp_num" value="<%=insComp_num %>">
    <input type="hidden" name="grpId_num" id="grpId_num" value="<%=grpId_num %>">
    
    
</logic:empty>

<logic:notEmpty name="frmMember" property="display">
	<html:errors/>
</logic:notEmpty>

</html:form>