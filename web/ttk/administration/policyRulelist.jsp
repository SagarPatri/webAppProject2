<%
/** @ (#) policyRulelist.jsp 30th DEC 2020
 * Project     : TTK Healthcare Services
 * File        : policyRulelist.jsp
 * Author      : Deepthi Meesala
 * Company     : RCS
 * Date Created: 30th DEC 2020
 *
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ page import="com.ttk.common.TTKCommon" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
var TC_Disabled = true;
//function to call edit screen
function edit(rownum)
{
    document.forms[1].mode.value="viewPolicyRuleHistory";
    document.forms[1].rownum.value=rownum;
    document.forms[1].action = "/UpdateRuleAction.do";
    document.forms[1].submit();
}//end of edit(rownum)

function onDefineRule()
{
	document.forms[1].mode.value="doAdd";
	document.forms[1].child.value="Clause List";
	document.forms[1].action="/EditRuleAction.do";
	document.forms[1].submit();
}



function onHistoryClose()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].child.value="";
	document.forms[1].action="/RuleAction.do";
	document.forms[1].submit();
}





</script>
<!-- S T A R T : Content/Form Area -->
<html:form action="/RuleAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
     <td>Policy Rules - <bean:write name="frmRules" property="caption"/></td>
    
    <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
  </tr>
</table>
<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">
<html:errors/>
<!-- S T A R T : Grid -->
<ttk:HtmlGrid name="tableData"/>
<!-- E N D : Grid -->
<!-- S T A R T : Buttons and Page Counter -->
<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="27%">&nbsp;</td>
<td align="right" nowrap>

	<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onHistoryClose();"><u>C</u>lose</button>

</td>
</tr>
</table> 







</div>
<!-- E N D : Buttons and Page Counter -->
<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
<input type="hidden" name="child" value="">
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<INPUT TYPE="hidden" NAME="sortId" VALUE="">
<INPUT TYPE="hidden" NAME="pageId" VALUE="">
</html:form>
<!-- E N D : Content/Form Area -->