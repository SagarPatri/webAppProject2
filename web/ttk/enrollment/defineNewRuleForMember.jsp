
<%@ page import="com.ttk.common.TTKCommon" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
function onDefineRule()
{
	document.forms[1].mode.value="doAdd";
	document.forms[1].action="/defineNewMemRuleAction.do";
	document.forms[1].submit();
}
</script>

<!-- S T A R T : Content/Form Area -->
<html:form action="/ViewMemberPolicyAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">
<html:errors/>

<!-- S T A R T : Buttons and Page Counter -->
<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center" nowrap>
			<button type="button" name="Button" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onDefineRule();"><u>D</u>efine Rule</button>
		</td>
	</tr>
</table>
</div>
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<html:hidden property="selectedroot"/>
	<html:hidden property="selectednode"/>
	<html:hidden property="memberSeqID"/>
</html:form>
