<%
/**
 * @ (#) groupdetail.jsp 16th Jan 2006
 * Project      : TTK HealthCare Services
 * File         : groupdetail.jsp
 * Author       : Krishna K H
 * Company      : Span Systems Corporation
 * Date Created : 12th Jan 2006
 *
 * @author       : Krishna K H
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%
	pageContext.setAttribute("officeinfo", Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("groupType", Cache.getCacheObject("groupType"));
%>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/empanelment/empanelmentfilesview.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var TC_Disabled = true; //to avoid the alert message on change of form elements
</SCRIPT>
<!-- S T A R T : Content/Form Area -->

<html:form action="/HRFileSearchAction.do" >
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  	<tr>
    	<td>Files List [<bean:write name="frmFilesList" property="caption"/>]</td>
    </tr>
	</table>
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
		<tr class="searchContainerWithTab">
		<td nowrap>File Upload Date:<br>
	            <html:text property="fileUploadDate" name="frmFilesList"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].fileUploadDate',document.forms[1].fileUploadDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>       
	     </td>
	     
	     <td nowrap>Status:<br>
	     <html:select name="frmFilesList" property="listofStatus">
	     <html:option value="ALL">Any</html:option>
	     <html:option value="PENDING">Pending</html:option>
	     <html:option value="COMPLETED">Complete</html:option>
	     </html:select>       
		 <a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
            	</td>
            	
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
		<html:errors />
		<ttk:HtmlGrid name="tableData" className="gridWithCheckBox zeroMargin" />
		
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
    		<tr><td><table>
    		<ttk:PageLinks name="tableData"/>
    		</table></td>
    		</tr>
    		<tr>
      			<td width="100%" align="center">
      			    <button type="button" name="Button1" accesskey="v" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="completeDocument()"><u>C</u>omplete</button>&nbsp;
      				<button type="button" name="Button2" accesskey="v" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onCloseViewUpload()"><u>C</u>lose</button>&nbsp;
      			</td>
    		</tr>
  		</table>
	</div>
	<input type="hidden" name="child" value="">
 	<%-- <html:hidden property="rownum"/> --%>
    <%-- <html:hidden property="mode" /> --%>
    <input type="hidden" name="selectedroot" value="">
    <input type="hidden" name="selectednode" value="">
    <input type="hidden" name="pageId" value="">
    <INPUT TYPE="hidden" NAME="sortId" VALUE="">
	
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<input type="hidden" name="ActiveLink" value="<%=TTKCommon.getActiveLink(request)%>"> 
</html:form>
