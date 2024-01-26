
<%
	/** @ (#) inscompanyinfo.jsp 25th Feb 2008
	 * Project     : TTK Healthcare Services
	 * File        : cashlessAdd.jsp
	 * Author      : kishor kumar 
	 * Company     : RCS Technologies
	 * Date Created: 28/11/2014
	 *
	 * @author 		 : kishor kumar
	 * Modified by   :
	 * Modified date :
	 * Reason        :
	 * welcome to Qatar
	 */
%>
<%@ page import="java.util.ArrayList,com.ttk.common.TTKCommon"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ page import="java.util.Arrays"%>
<%@ page
	import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,org.apache.struts.action.DynaActionForm"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript"
	src="/ttk/scripts/partner/cashlessAddNew.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
</br></br>
<html:form action="/OnlinePreAuthAction.do" method="post" enctype="multipart/form-data" styleId="testId">
<table align="center" class="formContainer" border="0" cellspacing="0"
	cellpadding="0" style="width: 70%">

	<tr>
		<td width="100%" class="formLabel" align="left" colspan="2"><strong>
				Your PreApproval Request is <font color="blue"> <bean:write
						name="frmOnlinePreAuth" property="status" /></font>
		</strong> <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
			<br> <strong> "Received the preapproval request, case
				is forwarded for review". </strong></td>
	</tr>
	<tr>
		<td>Pre Approval Reference No. : <bean:write
				name="frmOnlinePreAuth" property="preAuthRefNo" /></td>
	</tr>

</table>
<br>
<table align="center" class="formContainer" border="0" cellspacing="0"
	cellpadding="0" style="width: 70%">
	<tr>
		<td class="formLabel metaDesign"><strong> Pre-Approval
				No. </td>
		<td>: <bean:write name="frmOnlinePreAuth"
				property="preAuthNO" /></td>
		</strong>

		<td width="15%" class="formLabel">Authorization No.</td>
		<td>: <bean:write name="frmOnlinePreAuth" property="approvalNo" /></td>
	</tr>

	<tr>
		<td class="formLabel metaDesign"><strong> Alkoot ID </td>
		<td>: <bean:write name="frmOnlinePreAuth"
				property="enrollId" /> </strong></td>

		<td width="15%" class="formLabel">Card Holder Name</td>
		<td>: <bean:write name="frmOnlinePreAuth" property="patientName" /></td>
	</tr>

	<tr>
		<td class="formLabel metaDesign"><strong> Provider Name
		</td>
		<td>: <bean:write name="frmOnlinePreAuth"
				property="providerName" /> </strong></td>

		<td width="15%" class="formLabel">Provider Country</td>
		<td>: <bean:write name="frmOnlinePreAuth" property="countryName" /></td>
	</tr>

	<tr>

	</tr>
	<tr>
		<td class="formLabel metaDesign">Policy No.</td>
		<td>: <bean:write name="frmOnlinePreAuth" property="policyNo" /></td>
		<td width="15%" class="formLabel">Status</td>
		<td>: <bean:write name="frmOnlinePreAuth" property="status" /></td>
	</tr>
	<tr>
		<td class="formLabel metaDesign">Pre-Approval Amount</td>
		<td>: <bean:write name="frmOnlinePreAuth"
				property="totalGrossAmt" /></td>
	</tr>

</table>



<table align="center" class="formContainer" border="0" cellspacing="0"
	cellpadding="0" style="width: 70%">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><strong>NOTE:</strong> An Authorization letter will be sent
			to the registered email id of Partner & Patient within 30 minutes.</td>
	</tr>
</table>

<br>
<!-- S T A R T : Buttons -->
<table align="center" class="buttonsContainer" border="0"
	cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" align="center">
			<button type="button" name="Button" accesskey="x" class="olbtnSmall"
				onClick="javascript:onClose(${isDoViewPolicyTOB});">
				E<u>x</u>it
			</button>&nbsp;
		</td>
		<td width="100%" align="center">
	</tr>
</table>
<INPUT TYPE="hidden" NAME="sublink">
<INPUT TYPE="hidden" NAME="tab">
<input type="hidden" name="mode" value="">
</html:form>