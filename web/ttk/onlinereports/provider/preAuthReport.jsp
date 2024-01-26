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
<script type="text/javascript" src="/ttk/scripts/onlinereports/provider/providerReportList.js"></script>
</head>
<%
pageContext.setAttribute("sStatus", Cache.getCacheObject("preauthStatus"));
pageContext.setAttribute("benefitType",Cache.getCacheObject("main4benifitTypes"));
%>
<div class="contentArea" id="contentArea">
<html:form action="/ProviderReportsAction.do" >
<html:errors/>
<div id="sideHeadingMedium">Summary Reports</div>



<fieldset>
	<legend><bean:write name="frmProviderReportsList" property="reportName" /></legend>
<div align="center" style="border: 1px solid gray;border-radius: 20px;padding: 10px 20px 10px 20px;background-color: #F8F8F8;width: 97%;">
<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="1" id="onlineSearchEnterTable">
 <tr>
 <td nowrap>Pre-Approval No.:<br>
	  <html:text property="preApprovalNo" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
</td>

<td nowrap>From date:<span class="mandatorySymbol">*</span><br>
	
  <html:text name="frmProviderReportsList" property="fromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].fromDate',document.forms[1].fromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="fromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>To date:<span class="mandatorySymbol">*</span><br>
  <html:text name="frmProviderReportsList" property="toDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].toDate',document.forms[1].toDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="toDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
   <td nowrap>Patient Name:<br>
	  <html:text property="patientName" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
 </td>
 </tr>
 <tr>
	   <td nowrap>Authorization Number:<br>
	  <html:text property="authNo" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	
		<td nowrap>Doctor Name:<br>
	  <html:text property="doctorName" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  
	  <td nowrap>Al Koot Id:<br>
	  <html:text property="alKootId" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  
	  <td nowrap>Benefit Type:<br>
	  <html:select property ="benefitType" styleClass="selectBox selectBoxMedium">
           			<html:option value="">Select from list</html:option>
           			<html:options collection="benefitType" property="cacheId" labelProperty="cacheDesc"/>
      </html:select>
	  </td>
	  </tr>
	  <tr>
	  <td nowrap>Status:<br>
           <html:select property="claimStatus" name="frmProviderReportsList" styleClass="selectBox selectBoxMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
           </html:select>
      </td>
      
      <td nowrap>Event Reference Number:<br>
	  <html:text property="eventRefNo" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  
	   <td nowrap>Qatar Id:<br>
	  <html:text property="qatarId" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  
	  <td nowrap>Reference Number:<br>
	  <html:text property="RefNo" name="frmProviderReportsList" styleClass="textBox textBoxMedLarge"/>
       <a href="#"><button name="mybutton" class="olbtn" accesskey="s" onclick="return onSearch();" type="button"><u>S</u>earch</button></a>  
	  </td>
 </tr>
</table>	
</div>

</fieldset>


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
	<div align="center">
	<table>
	<tr>
	<td colspan="4">&nbsp;</td>
     	<td colspan="4">&nbsp;</td>
     	<td colspan="4">&nbsp;</td>
	<td>
 <button name="mybutton" class="olbtn" accesskey="c" onclick="onBack();" type="button"><u>C</u>lose</button>
 </td>
 <td colspan="4">&nbsp;</td>
  <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onExportToExcel();" type="button"><u>E</u>xport to Excel</button>
  </td>

	</tr></table>
	</div>

<%if(("preAuthDetailed".equals(request.getSession().getAttribute("reportId")))){%>
<div align="left">
<table>
<tr><td><b>NOTE:-</b></td></tr>
<tr><td><b><small>Wherever the amount approved or paid is as per the requested or net amount, please IGNORE the denial reasons, if any</small></b></td></tr>
</table>
</div>
<%}%>
	
<input type="hidden" name="mode" value="">
<input type="hidden" name="rownum" value="">
<input type="hidden" name="sortId" value="">
<input type="hidden" name="pageId" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="backID" value="">
<input type="hidden" name="tab" value="">
<INPUT TYPE="hidden" NAME="loginType" VALUE="HOS">
<INPUT TYPE="hidden" NAME="reportId" VALUE="<%=request.getParameter("reportId")%>">
</html:form>
</div>