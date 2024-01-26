<%
/**
 * @ (#) summarydetails.jsp July 19, 2007
 * Project      : TTK HealthCare Services
 * File         : summarydetails.jsp
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : July 19, 2007
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/childwindow.js"></script>
<script language="javascript" src="/ttk/scripts/onlineforms/summarydetails.js"></script>

<style type="text/css">
.button_class {
	float: right;
	margin-right: 218px;
	padding-top: 15px;
}

.tob_class,.benefit_class,.ecard_class {
	background-color: rgb(0, 26, 102);
    font-weight: bold;
    margin-left: auto;
    margin-right: auto;
    margin: auto;
    color: #fff;
}


.diagnosis_fieldset_class{
    width: 80%;
}


.activity_fieldset_class{
    width: 97%;
    height: auto;
}


.tob_class {    
    width: 8%;
    height: 31px;
    text-align: center;
    padding-right: 13px;
}

.benefit_class {  
    width: 13%;
    height: 31px;
    padding-right: 15px;
}
.ecard_class {
    width: 18%;
    height: 31px;
    padding-right: 15px;
}
.shortfall_button_class
{
    width: 50%;
    height: 29px;
    background-color: rgb(0, 26, 102);
    color: #fff;
    font-weight: bold;
}
/* .shortfall_button_class {
    height: 29px;
    background-color: rgb(0, 26, 102);
    color: #fff;
    font-weight: bold;
    cursor: pointer;
}

 */
.generate_letter_button_class {
    height: 29px;
    background-color: rgb(0, 26, 102);
    color: #fff;
    font-weight: bold;
    width: 55%;
    cursor: pointer;
}

.amount_class {
    font-weight: bold;
    float: left;
    width: 90%;
    margin: 10px;
    font-size: 12px;
    font-weight: normal;
}
/* .info_text_class {
    color: #0c48a2;
    font-weight: bold;
    margin-left: 3px;
    padding-top: 0%;
    margin-top: -12px;
    font-size: 14px;
    font-family: Arial, Helvetica, sans-serif;
} */
.main_info_text_class {
    padding-left: 0px;
    margin-top: 40px;
    font-size: 12px;
}

.shortfall_class{
font-size: 11px;
}

.button_design_class:nth-child(2) {
  width: 11%;
}
.button_design_class:nth-child(3) {
  width: 11%;
}

</style>
<html:form action="/OnlineHistoryAction.do" >
	<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	String typeStatus=null;
	if(session.getAttribute("typeStatus")!=null)
		typeStatus=(String) session.getAttribute("typeStatus");
	
		if(TTKCommon.getActiveSubLink(request).equals("Pre-Auth")||TTKCommon.getActiveSubLink(request).equals("Pre-Approval"))
			 {
	%>
		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="57%">Pre-Approval  Details - <bean:write name="frmHistoryDetail" property="caption" scope="session"/></td>
			<td width="43%" align="right" class="webBoard">&nbsp;&nbsp;</td>
		  </tr>
		</table>
	<%
			 }
	%>
	<%
		if(TTKCommon.getActiveSubLink(request).equals("Claims")||TTKCommon.getActiveSubLink(request).equals("Claims History"))
			 {
	%>
		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="57%">Claims Details - <bean:write name="frmHistoryDetail" property="caption" scope="session"/></td>
			<td width="43%" align="right" class="webBoard">&nbsp;&nbsp;</td>
		  </tr>
		</table>
	<%
			 }
	%>
<div class="contentArea" id="contentArea">

	<%
		if(TTKCommon.getActiveSubLink(request).equals("Pre-Auth")||TTKCommon.getActiveSubLink(request).equals("Pre-Approval"))
			 {
	%>
		<ttk:OnlinePreAuthHistory/>
	<%
			 }
	%>
	<%
		if(TTKCommon.getActiveSubLink(request).equals("Claims")||TTKCommon.getActiveSubLink(request).equals("Claims History"))
			 {
	%>
	<ttk:OnlineClaimHistory/>
	<%
			 }
	%>
	
	<% if(userSecurityProfile.getLoginType().equals("EMPL")){  %>
	<div>
	<fieldset class="diagnosis_fieldset_class">
	<legend>Diagnosis Details</legend>
	<ttk:OnlineDiagnosisDetails flow="PAT" flag="MEMPATCreate"/>
	</fieldset>
	</div>
		
		<div>
		<fieldset class="activity_fieldset_class">
	<legend>Activity/Service Details</legend>
		<ttk:OnlineActivityDetails flow="PAT" flag="MEMPATCreate"/>
		<div class="amount_class">
		<table>
		  <tr>
		    <td align="left" class="webBoard_class" ><b>Total Requested Amount. (QAR.): </b> </td><td width="50%" align="left" ><c:out value="${sessionScope.requestedAmount}" /></td>
			  </tr><tr>
			<td align="left" class="webBoard_class" ><b>Total Approved Amount. (QAR.): </b> </td><td width="50%" align="left"><c:out value="${sessionScope.approvedAmount}" /></td>
		  </tr>
		  
		  <%if(("Approved".equals(typeStatus)||"Rejected".equals(typeStatus))&&(TTKCommon.getActiveSubLink(request).equals("Pre-Approval"))){ %>
		  <tr>
			<td><input type="button" onClick="generatePreAuthLetter();" class="generate_letter_button_class" value="Generate Letter" name="GenerateLetter" style="width: 100%;"/></td>
		  </tr>
		 <%}else if(("Approved".equals(typeStatus)||"Rejected".equals(typeStatus))&&(TTKCommon.getActiveSubLink(request).equals("Claims History"))){ %>
		 <tr>
			<td><input type="button" onClick="javascript:generateClaimLetter()" class="generate_letter_button_class" value="Generate Letter" name="GenerateLetter" style="width: 100%;"/></td>
		  </tr>
		 <%} %>
		</table>
		</div>
		</fieldset>
		</div>
		<%if(TTKCommon.getActiveSubLink(request).equals("Pre-Approval")||TTKCommon.getActiveSubLink(request).equals("Claims History")){ %>
		<div class="main_info_text_class">
		<B>Note:</B>
		<div class="info_text_class">
		<ul style="list-style-type:square">
		  <li>Please click on Report to Al Koot and submit your message in case of any concerns.</li>
		</ul>
		</div>
		<br/>
		</div>
		<%}%>
		<%-- <div class="button_class">
		<%if(TTKCommon.getActiveSubLink(request).equals("Pre-Approval")){ %>
		<input type="button" class="tob_class" value="Back" onClick="javascript:goBack();"/> 
		<%} else{%>
		<input type="button" class="tob_class" value="Back" onClick="javascript:goClaimBack();"/> 
		<%}%>
		&nbsp;&nbsp;<input type="button" class="benefit_class" value="Go To Home" onClick="javascript:goToHome();"/> 
		&nbsp;&nbsp;<input type="button" class="ecard_class" value="Report To Alkoot" onClick="javascript:onReportToAlkoot();"/>
	</div> --%>
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    <%if(TTKCommon.getActiveSubLink(request).equals("Pre-Approval")){ %>
	     <button type="button" name="Button" accesskey="b" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:goBack();"><u>B</u>ack</button>&nbsp;&nbsp;&nbsp;&nbsp;
		<%} else{%>
		 <button type="button" name="Button" accesskey="b" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:goClaimBack();"><u>B</u>ack</button>&nbsp;&nbsp;&nbsp;&nbsp;
		<%}%>
	    <button type="button" name="Button" accesskey="g" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:goToHome();"><u>G</u>o To Home</button>&nbsp;&nbsp;&nbsp;&nbsp;
	     <button type="button" name="Button" accesskey="c" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onReportToAlkoot();"><u>R</u>eport To Alkoot</button>&nbsp;
	    
	    </td>
	  </tr>
	</table>
	
	
	
	<%}%>
    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    <%//kocb 
	    if(userSecurityProfile.getLoginType().equals("B")){  %>
	    <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>B</u>ack</button>
	    <% }else{
	    	if(!userSecurityProfile.getLoginType().equals("EMPL")){%>
	    	<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	    	<% }}%>
	    </td>
	  </tr>
	</table>
	<!-- E N D : Buttons -->
</div>

<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="leftlink" value="">
<input type="hidden" name="sublink" value="">
<input type="hidden" name="tab" value="">
<% if(!userSecurityProfile.getLoginType().equals("EMPL")){%>
<input type="hidden" name="child" value="Details">
<% }%>
</html:form>
