<%
/**
 * @ (#) accounthistorylist.jsp 26th Nov 2005
 * Project      : TTK HealthCare Services
 * File         : accounthistorylist.jsp
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : 26th Nov 2005
 *
 * @author       :Chandrasekaran J
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/empanelment/accounthistorylist.js"></script>
<SCRIPT LANGUAGE="JavaScript">
var TC_Disabled = true; //to avoid the alert message on change of form elements
</SCRIPT>
<%
	pageContext.setAttribute("empHistory", Cache.getCacheObject("empHistory"));
%>
	<!-- S T A R T : Content/Form Area -->
	<html:form action="/AccountsHistoryAction.do"  method="post">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">History of <bean:write name="frmAccountsHistory" property="caption" /></td>
			 <td width="43%" align="right" class="webBoard">&nbsp;</td>
		</tr>
	</table>
	<!-- E N D : Page Title -->

	<!-- S T A R T : Search Box -->
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>Type:<br>
				<html:select property="empHistory" name="frmAccountsHistory" styleClass="selectBox" >
        		<html:options collection="empHistory" property="cacheId" labelProperty="cacheDesc"/>
			</html:select>

			</td>
			<td>Start Date:<br>
				<html:text property="startdate" maxlength="10" styleClass="textBox textDate" /><A NAME="CalendarObjectStartDate" ID="CalendarObjectStartDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectStartDate','forms[1].startdate',document.forms[1].startdate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
			</td>
			<td>End Date:<br>
				<html:text property="enddate"  maxlength="10" styleClass="textBox textDate" /><A NAME="CalendarObjectEndDate" ID="CalendarObjectEndDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectEndDate','forms[1].enddate',document.forms[1].enddate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
			</td>
			<td valign="bottom" nowrap>
				<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
			</td>
			<td width="100%">&nbsp;</td>
		</tr>
	</table>
	<!-- E N D : Search Box -->

	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->

	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="27%"></td>
			<td width="73%" align="right">
			<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
		</tr>
		<ttk:PageLinks name="tableData"/>
	</table>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<input type="hidden" name="child" value="AccountsHistory">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	</html:form>
	<!-- E N D : Content/Form Area -->
