<%@page import="com.ttk.common.security.SecurityProfile"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<head>
<style type="text/css">

table#textTable{
margin-left: 20px;
width: 500px;
display: inline;
}
table#textTable,table#textTable tr td{
border: 1px solid white;
background-color: #F0F0F0;
border-collapse: collapse;
}

table#textTable tr td{
text-align:left;
font-size: 18px;
padding: 20px;
}
#hexagonLinks{
display: inline;
float: right;
list-style: none;
}
#onlineTableTD{
text-align: center;
font-size: 14px;
}
#onlineSearchEnterTable{
border: none;
padding: 0;
}
</style>
<script type="text/javascript" src="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/onlineforms/PBMPharmacyLogin/pbmpreauthlist.js"></script>
</head>
<%
pageContext.setAttribute("sStatus", Cache.getCacheObject("preauthStatus"));
%>
<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)session.getAttribute("UserSecurityProfile");
	SecurityProfile spx = null;
	if(userSecurityProfile!=null && userSecurityProfile.getSecurityProfile()!=null)
	{
		spx = userSecurityProfile.getSecurityProfile();
	}
%>
<div class="contentArea" id="contentArea">
<html:form action="/PbmPreauthAction.do" >
<html:errors/>
<div id="sideHeadingMedium">Pre-Auth Log Search</div>
<br><br>
<div align="center" style="border: 1px solid gray;border-radius: 20px;padding: 10px 20px 10px 20px;background-color: #F8F8F8;width: 75%;">
<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0" id="onlineSearchEnterTable">

	 <tr>
	 <td nowrap>Authorization No.:<br>
	  <html:text property="authorizationId" name="frmOnlinePreAuthLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	 <td nowrap>From date:<br>
	
  <html:text name="frmOnlinePreAuthLog" property="fromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].fromDate',document.forms[1].fromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="fromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>To date:<br>
  <html:text name="frmOnlinePreAuthLog" property="toDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].toDate',document.forms[1].toDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="toDate" width="24" height="17" border="0"
												align="absmiddle"></a></td>
	   <td nowrap>Patient name:<br>
	  <html:text property="patientName" name="frmOnlinePreAuthLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	 </tr>
	 <tr>
	 <td nowrap>Pre-Approval No.:<br>
	  <html:text property="preApprovalNo" name="frmOnlinePreAuthLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  <td nowrap>Event Reference No.:<br>
	  <html:text property="eventRefNo" name="frmOnlinePreAuthLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	 
	<td nowrap>Clinician name:<br>
	  <html:text property="clinicianName" name="frmOnlinePreAuthLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  <td nowrap>Al Koot Id:<br>
	  <html:text property="memberId" name="frmOnlinePreAuthLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  </tr>
	  <tr>
	  <td nowrap>Qatar ID:<br>
	  	 <html:text property="qatarId" name="frmOnlinePreAuthLog" styleClass="textBox textBoxMedLarge"/>  
	  </td>
	  <td nowrap>Status:<br>
	   <html:select property="status" name="frmOnlinePreAuthLog" styleClass="selectBox selectBoxMedium" onchange="javascript:onStatusChanged()">
		  	 		<html:option value="">Any</html:option>
		        	<html:option value="APR">Approved</html:option>
                    <html:option value="REJ">Rejected</html:option>
                    <html:option value="INP">In-Progress</html:option>
                    <html:option value="REQ">Required Information</html:option>
            	</html:select>
            	</td>
      	<logic:equal value="INP" name="frmOnlinePreAuthLog" property="status">
   	       		<td width="25%" nowrap>In-Progress Status:<br>
         			<html:select property ="inProgressStatus" styleClass="selectBox selectBoxMedium">
	           			<html:option value="">Any</html:option>
	           			<html:option  value="FRH">Fresh</html:option>
						<html:option  value="APL">Appeal</html:option>
						<html:option  value="ENH">Enhanced</html:option>
						<html:option  value="RES">Shortfall Responded</html:option>
       				</html:select>
       	   		</td>
       	</logic:equal>
            	
            	<td>
       <img title="Search" src="/ttk/images/SearchIcon.gif" height="18" width="16">
       <a href="#">
	   <button class="searchSbtn" accesskey="s"  onclick="return onSearch();"><u>S</u>earch</button>	 
	   </a>  
          	</td> 
	 <!--  <td><img title="Search" src="/ttk/images/SearchIcon.gif" height="18" width="16"></td>
	  <td id="onlineTableTD"><a title="Proceed" id="imgAncSmall" href="#" onclick="javascript:onSearch();"><u>S</u>earch</a></td>
	   -->
	 </tr>	
	</table>	
</div>
<!-- <br>
	<table align="center">
	<tr>
	<td><img title="Back" src="/ttk/images/broker/small_hexagon.png" height="48" width="46"></td>
	<td><a id="imgAncMedium" onclick="onBack('Home');" href="#" title="Back">Back</a></td>
	</tr>
	</table>
<br> -->
<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table  align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	<tr>
     	<td><ttk:PageLinks name="tableData"/></td>
     	</tr>     	
	</table>
<input type="hidden" name="mode" value="">
<input type="hidden" name="rownum" value="">
<input type="hidden" name="sortId" value="">
<input type="hidden" name="pageId" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="backID" value="">
<INPUT TYPE="hidden" NAME="leftlink" VALUE="<%= spx.getActiveLink()%>">
<INPUT TYPE="hidden" NAME="sublink" VALUE="<%= spx.getActiveSubLink()%>">
<INPUT TYPE="hidden" NAME="tab" VALUE="<%= spx.getActiveTab()%>">
<INPUT TYPE="hidden" NAME="loginType" VALUE="PBM">
</html:form>
</div>