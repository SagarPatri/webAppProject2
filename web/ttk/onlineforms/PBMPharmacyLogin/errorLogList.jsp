<%
/** @ (#) errorLogList.jsp
 *
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/onlineforms/PBMPharmacyLogin/errorLoglist.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<script>
bAction=false;
var TC_Disabled = true;
</script>
<style>
.bodystyling
{
  color:#000000;
  border: 1px solid;
  background-color:#EBEDF2;
  margin-top:10px;
  padding:5px;
  width:98%;
}
</style>
<!-- S T A R T : Content/Form Area -->
<html:form action="/ClaimViewLogListAction.do">
	<!-- S T A R T : Page Title -->
	<!-- <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0"> -->
		<%-- <tr>
			<td width="57%">Error Log Search</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr> --%>
		<div id="sideHeadingMedium">Claims Upload Error Log Search</div>
	<br>
	<!-- </table> -->
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<table align="center" class="bodystyling" border="0" cellspacing="0" cellpadding="0" style="border-collapse: separate;">
	<tr>
	
			<td nowrap>Batch Reference Number:<br>
            	<html:text property="iBatchRefNO"  styleClass="textBox textBoxLarge" onkeyup="isNumeric(this)" maxlength="60"/>
     		</td>
        
        	<td nowrap>File Name: <br>
            	<html:text property="sFileName"  styleClass="textBox textBoxLarge" maxlength="60"/>
     		</td>
     </tr>
        <tr> 
			<td nowrap>From Date:<br>
	            <html:text property="formDate"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].formDate',document.forms[1].formDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
            
            <td nowrap>To Date:<br>
	            <html:text property="toDate"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].toDate',document.forms[1].toDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    	       
        <td>
        <a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>   
        	</td>
        	</tr>  	
	</table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td></td>
	<td></td>	
	<td><!--button type="button" name="Button2" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="addClaim()"><u>A</u>dd</button-->&nbsp;</td>
	</tr>
    	
      	<ttk:PageLinks name="tableData"/>
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