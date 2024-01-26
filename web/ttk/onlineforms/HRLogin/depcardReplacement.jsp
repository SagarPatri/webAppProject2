<%
/**
 * @ (#) policygeneral.jsp July 24, 2007
 * Project      : TTK HealthCare Services
 * File         : policygeneral.jsp
 * Author       : Krupa J	
 * Company      : Span Systems Corporation
 * Date Created : July 24, 2007
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
<%@ page import="com.ttk.common.WebBoardHelper,com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/corporatemember.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>

<script>
</script>
<style>
.sub_heading {
    background-color: rgb(0, 26, 102);
    margin-left: 25px;
    margin-top: 40px;
    width: 20%;
    text-align: center;
    padding: 5px 16px;
    text-decoration: none;
    margin-bottom: 3px;
    border: 1px solid rgb(0, 26, 102);
    white-space: nowrap;
    box-shadow: 0px 1px 3px rgba(0,0,0,0.12), 0px 1px 2px rgba(0,0,0,0.24);
    border-radius: 10px 10px 10px 10px;
    font-size: 15px;
}
FIELDSET {
    width: 90%;
    border: 1px solid #CCCCCC;
    padding-top: 0px;
    padding-bottom: 10px;
    margin-left: 51px;
}
.errorContainer {
    color: #ff0000;
    border: 1px solid #CCCCCC;
    background-color: #FFFFFF;
    margin-top: 10px;
    padding: 5px 5px 5px 5px;
    width: 90%;
    text-align: left;
}
.successContainer {
    color: #0000cc;
    border: 1px solid #CCCCCC;
    background-color: #FFFFFF;
    margin-top: 10px;
    padding: 5px 5px 5px 5px;
    width: 90%;
    text-align: left;
}


</style>
<html:form action="/OnlinePolicyDetailsAction.do" method="post" enctype="multipart/form-data">

<%-- table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="57%">Enrollment Details - <bean:write name="frmOnlineDetails" property="caption"/></td>
	<td width="43%" align="right" class="webBoard">&nbsp;&nbsp;</td>
  </tr>
</table> --%>

<div class="contentArea" id="contentArea">

<h4 class="sub_heading">Card Replacement Request</h4>
<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:"
				border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success"
						width="16" height="16" align="absmiddle">&nbsp; <bean:message
							name="updated" scope="request" /></td>
				</tr>
			</table>
		</logic:notEmpty>
		<!-- E N D : Success Box -->
		<logic:notEmpty name="statusinfo" scope="request">
			<table align="center" class="errorContainer" style="display:"
				border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><strong> <img src="/ttk/images/ErrorIcon.gif"
							title="Error" alt="Error" width="16" height="16" align="absmiddle">&nbsp;
					</strong> <bean:message name="statusinfo" scope="request" />
						<ol
							style="padding: 0px; margin-top: 3px; margin-bottom: 0px; margin-left: 25px;">
						</ol></td>
				</tr>
			</table>
		</logic:notEmpty>
		<logic:notEmpty name="notify" scope="request">
			<table align="center" class="errorContainer" style="display:"
				border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><img src="/ttk/images/ErrorIcon.gif" alt="Alert"
						title="Alert" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="notify" scope="request" /></td>
				</tr>
			</table>
		</logic:notEmpty>
		<html:errors />
	<ttk:OnlinePolicyHistory/>
	<!-- S T A R T : Buttons -->
	<fieldset class="diagnosis_fieldset_class">
		
				
			<legend>Reason for Reprint</legend>
			<table align="center" class="" border="0"cellspacing="0" cellpadding="0" style="width: 98%">
			<tr>
			<td style="padding-left: 15px; width: 5%;">Reason<span class="mandatorySymbol">*</span></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<html:textarea name="frmAddEnrollment"  property="finalRemarks"
			styleClass="textAreaBox" cols="50" rows="4" styleId="finalRemarksId" /></td>
			</tr>
			</table>
		</fieldset>
		

		<table align="center" class="buttonsContainer" border="0"
			cellspacing="0" cellpadding="0">
			<tr>
			<!-- onMouseout="this.className='buttons'"
						onMouseover="this.className='buttons buttonsHover'" -->
				<td width="100%" align="center">
					<button type="button" name="Button" accesskey="s" class="buttons"						
						onClick="javascript:onSubmitMemberRequest()">
						<u>S</u>ubmit
					</button>&nbsp;
					<button type="button" name="Button" accesskey="r" class="buttons"						
						onClick="javascript:onReset()">
						<u>R</u>eset
					</button>&nbsp;
					<button type="button" name="Button" accesskey="c" class="buttons"
						onClick="javascript:onCloseWindow()">
						<u>C</u>lose
					</button>
				</td>
			</tr>
		</table>
	</div>
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
<html:hidden property="logintype" />
<html:hidden property="isCardRepRequest" name="frmAddEnrollment" value="YES"/>
<!-- E N D : Buttons -->

</html:form>