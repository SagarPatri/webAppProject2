<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript">

function onClose()
{
	document.forms[1].mode.value="doCloseActivityLog";
	document.forms[1].action="/ReportsAction.do";
	document.forms[1].submit();
}
</script>
<!-- S T A R T : Content/Form Area -->
	<html:form action="/ReportsAction.do" method="post">

<div class="contentArea" id="contentArea">

<!-- S T A R T : Search Box -->
<html:errors/>

<!-- S T A R T : Grid -->
<fieldset>
<legend>Activity Log</legend>
<ttk:HtmlGrid name="tableData" />
<!-- E N D : Grid -->
</fieldset>
<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	   	 	<td align="center" nowrap>
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	    	</td>
	  </tr>
	</table>
	</div>
<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	</html:form>
<!-- E N D : Content/Form Area -->