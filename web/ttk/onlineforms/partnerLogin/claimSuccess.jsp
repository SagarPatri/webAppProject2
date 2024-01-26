<!-- mm aa -->
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page import="java.util.ArrayList,com.ttk.common.TTKCommon"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon" %>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,org.apache.struts.action.DynaActionForm"%>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript"  src="/ttk/scripts/partner/partnerClaimSubmission.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
<style>
.formContainer123
{
  
  margin-top:0px;
  padding:3px;
  width:98%;
  text-align:left;
}
</style>

<%
	pageContext.setAttribute("alCountryCode", Cache.getCacheObject("countryCode"));
	pageContext.setAttribute("benifitType",Cache.getCacheObject("main4benifitTypes"));
	System.out.println("alCountryCode ="+ Cache.getCacheObject("countryCode"));
	System.out.println("benifitTypeee ="+ Cache.getCacheObject("benifitTypeee"));
%>

<html:form action="/OnlinePartnerClaimsSubmission.do" method="post">
<html:errors/>
<div class="contentArea" id="contentArea">
<table align="center" class="formContainer" border="0" cellspacing="0"
	cellpadding="0" style="width: 50%; margin-top: 65px;">
<tr><td><br><b style="font-size: 14px;">The Claim Batch Submitted Successfully.</b></td></tr>

<tr> <td><br> <b style="font-size: 12px;">The batch number from this submission is : </b> <font style="font-size: 15px;" color="red"><bean:write
				name="frmOnlineClaimSubmission" property="claimBatchNo" /></font></td></tr>
<tr>
<td></td><td></td>
</tr>

<tr> <td><br> <b style="font-size: 12px;">The Claim Number from this submission is : </b> <font style="font-size: 15px;" color="red"><bean:write
				name="frmOnlineClaimSubmission" property="claimNumber" /></font></td></tr>
<tr>
<td></td><td></td>
</tr>

<tr> <td><br> <b style="font-size: 12px;">The Invoice Number from this submission is : </b> <font style="font-size: 15px;" color="red"><bean:write
				name="frmOnlineClaimSubmission" property="invoiceNo" /></font></td></tr>
<tr>

</table>	
<table align="center" class="formContainer" border="0" cellspacing="0"
	cellpadding="0" style="width: 50%">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><font color="red"> <strong>NOTE:</strong> Find Claim details at claims log search.</font></td>
	</tr>
</table>

<br>
<!-- S T A R T : Buttons -->

<table align="center" class="buttonsContainer" border="0"
	cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" align="center">
			<button type="button" name="Button" accesskey="x" class="olbtnSmall"
				onClick="onClose();">
				E<u>x</u>it
			</button>&nbsp;
		</td>
		<td width="100%" align="center">
	</tr>
</table>
    <INPUT TYPE="hidden" NAME="sublink">
    <INPUT TYPE="hidden" NAME="tab">
	<html:hidden name="frmOnlineClaimSubmission" property="icdCodeSeqId"
											styleId="icdCodeSeqId" /> 
    <html:hidden name="frmOnlineClaimSubmission" property="diagSeqId"
											styleId="diagSeqId" />
	<INPUT TYPE="hidden" NAME="mode" id="mode" VALUE=""> 	
	<INPUT TYPE="hidden" NAME="reforward" id="reforward" VALUE="">									
</div>
</html:form> 