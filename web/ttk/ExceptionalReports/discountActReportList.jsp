<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import=" com.ttk.common.TTKCommon" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script>
function onSelectClaimsDisActReport()
{
	document.forms[1].action="/ClaimsDiscountActivityReport.do";
	document.forms[1].mode.value = "doViewClmDiscountActivityReport";
	document.forms[1].submit();	
}
</script>

<html:form action="/ClaimsDiscountActivityReport.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>Claims Reports</td>
  </tr>
</table>
<!-- End of: Page Title -->
<div class="contentArea" id="contentArea">

  <fieldset>
    	<legend>Reports</legend>
	    <table class="formContainer" align="left"  border="0" cellspacing="0" cellpadding="0">
    	<tr>
	    	<td class="formLabel">
		 		<ul class="liBotMargin">
				<li class="liPad"><a href="#" onClick="javascript:onSelectClaimsDisActReport()">Claims-Discount Activity Report</a></li>
	    		</ul>
	   		 </td>
    	</tr>
	    </table>
  </fieldset>

</div>
<html:hidden property="mode"/>
</html:form>