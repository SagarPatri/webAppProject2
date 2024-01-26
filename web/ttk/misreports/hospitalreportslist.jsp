<%
/** @ (#) hospitalreportslist.jsp July 13, 2007
 * Project     : TTK Healthcare Services
 * File        : hospitalreportslist.jsp
 * Author      : Ajay Kumar
 * Company     : WebEdge Technologies Pvt.Ltd.
 * Date Created: 
 *
 * @author 		 : Ajay Kumar
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<script language="javascript" src="/ttk/scripts/misreports/hospitalreportslist.js"></script>
<!-- S T A R T : Content/Form Area -->
<html:form action="/HospitalReportsListAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td>All Hospital Reports List</td>
	    </tr>
</table>
<!-- E N D : Page Title -->
<html:errors/>
<div class="contentArea" id="contentArea">
<!-- S T A R T : AllReportsList -->
    <fieldset>
    <legend>Report Links</legend>
    <table class="formContainer" align="left"  border="0" cellspacing="0" cellpadding="0">
    <tr>
    <td class="formLabel">
	<ul class="liBotMargin">
    <li class="liPad"><a href="#" onClick="javascript:onSelectHM()">Hospital Monitor</a></li>
    </ul>
    </td>
    </tr>
    </table>
    </fieldset>
    
<!-- E N D : AllReportsList -->
</div>
<INPUT TYPE="hidden" NAME="mode" VALUE="">
</html:form>
<!-- E N D : Content/Form Area -->