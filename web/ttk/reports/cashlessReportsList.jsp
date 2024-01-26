

<%
/** @ (#) cashlessReportsList.jsp March 17, 2020
 * Project     : TTK Healthcare Services
 * File        : cashlessReportsList.jsp
 * Author      : Deepthi Meesala
 * Company     : RCS Technologies 
 * Date Created: March 17, 2020
 *
 * @author 		 :  
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<script language="javascript" src="/ttk/scripts/reports/cashlessReportsList.js"></script>

<!-- S T A R T : Content/Form Area -->
<html:form action="/ReportsAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td>Cashless Reports List</td>
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

    <li class="liPad"><a href="#" onClick="javascript:onTatReport()">TAT Report</a></li>
    <li class="liPad"><a href="#" onClick="javascript:onProductivityReport()">Productivity Report</a></li>
    <li class="liPad"><a href="#" onClick="javascript:onPreapprovalDetailedReport()">Pre-approval Detailed Report</a></li>
    <li class="liPad"><a href="#" onClick="javascript:onPreapprovalActivityReport()">Pre-approval Activity  Report</a></li>	
    </ul>
    </td>
    </tr>
    </table>
    </fieldset>
    
<!-- E N D : AllReportsList -->
</div>

    <INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
	
	</html:form>
<!-- E N D : Content/Form Area -->