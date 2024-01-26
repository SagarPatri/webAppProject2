<%
/** @ (#) dashboard.jsp 4/16/2020
 * Project     : ProjectX
 * File        : dashboard.jsp
 * Author      : Gyanendra 
 * Company     : Vidal
 * Date Created: 4/16/2020
 *
 * @author 		 : Gyanendra
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>

<script type="text/javascript" src="/ttk/scripts/preauth/dashboard.js"></script>

<html:form action="/PreAuthDashboardAction.do" >

<% boolean flag = TTKCommon.isAuthorized(request, "DashBordView") ;%>
<% boolean docFlag = TTKCommon.isAuthorized(request, "ViewDoc") ;%>
<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td>Dashboard</td>
	</tr>
	</table>
<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">
<!-- S T A R T : AllReportsList -->
    <fieldset>
    	<legend>Dashboard</legend>
	    <table class="formContainer" align="left"  border="0" cellspacing="0" cellpadding="0">
    	<tr>
	    <td class="formLabel">
		 <ul class="liBotMargin">    
	    <li class="liPad"><a href="#" onClick="javascript:displayManaDashboard();">Processor Dashboard</a></li>
	    <li class="liPad"><a href="#" onClick="javascript:displayDoctorDashboard()">Team Dashboard</a></li>
	  
	    </ul>
	    </td>
    	</tr>
	    </table>
      <INPUT TYPE="hidden" NAME="mode" VALUE="">
      <input type="hidden" name="securityflag" id="securityflagid" value="<%= flag %>">
      <input type="hidden"  id="docSeqirityFlag" value="<%= docFlag %>">
      
</html:form>