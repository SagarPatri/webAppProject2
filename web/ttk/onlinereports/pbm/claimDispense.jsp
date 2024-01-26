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
<script type="text/javascript" src="/ttk/scripts/onlinereports/pbm/pbmReporstList.js"></script>
</head>
<%
pageContext.setAttribute("sStatus", Cache.getCacheObject("preauthStatus"));
%>
<div class="contentArea" id="contentArea">
<html:form action="/PbmReportsAction.do" >
<html:errors/>
<div id="sideHeadingMedium">Summary Reports</div>

<fieldset>
	<legend><bean:write name="frmPBMReportsList" property="reportName" /></legend>
<div align="center" style="border: 1px solid gray;border-radius: 20px;padding: 10px 20px 10px 20px;background-color: #F8F8F8;width: 87%;">
<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="1" id="onlineSearchEnterTable">
  <tr style="margin-bottom: 20PX;"><td nowrap>Prescription Date</td>
  <td></td>
  <td nowrap>Dispensed Date</td>
  <td></td>
  <td></td>
  </tr>
	 <tr style="margin-bottom: 20PX;">
	 <td nowrap>From date:<br>
	
  <html:text name="frmPBMReportsList" property="trtmtFromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].trtmtFromDate',document.forms[1].trtmtFromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="trtmtFromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>To date:<br>
  <html:text name="frmPBMReportsList" property="trtmtToDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].trtmtToDate',document.forms[1].trtmtToDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="trtmtToDate" width="24" height="17" border="0"
												align="absmiddle"></a></td>
	 
	 <td nowrap>From date:<br>
	
  <html:text name="frmPBMReportsList" property="clmFromDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmFromDate',document.forms[1].clmFromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="clmFromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>To date:<br>
  <html:text name="frmPBMReportsList" property="clmToDate" maxlength="10" styleClass="textBoxDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmToDate',document.forms[1].clmToDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="clmToDate" width="24" height="17" border="0"
												align="absmiddle"></a></td>
		</tr>
		<tr>										
	  <td nowrap>Patient name:<br>
	  <html:text property="patientName" name="frmPBMReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	   
	
	 
	 <td nowrap>Authorization No.:<br>
	  <html:text property="authNo" name="frmPBMReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	   <td nowrap>Pre-Approval No.:<br>
	  <html:text property="preApprovalNo" name="frmPBMReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  
	  <td nowrap>Al Koot Id:<br>
	  <html:text property="alKootId" name="frmPBMReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  </tr>
	  <tr>
	  <td nowrap>Event Reference No.:<br>
	  <html:text property="eventRefNo" name="frmPBMReportsList" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  <td nowrap>Claim Number<br>
	  	 <html:text property="claimNumber" name="frmPBMReportsList" styleClass="textBox textBoxMedLarge"/>  
	  </td>
	  
	 
	  
	 
	  
	  
	  <td nowrap>Status:<br>
	   <html:select property="claimStatus" name="frmPBMReportsList" styleClass="selectBox selectBoxMedium">
		  	 		<html:option value="">Any</html:option>
		  	 		<html:option value="INP">In-Progress</html:option>
		        	<html:option value="APR">Approved</html:option>
                    <html:option value="REJ">Rejected</html:option>
                     
            	</html:select></td>
            	
            	<td nowrap>Dispensed Status:<br>
	   <html:select property="clmDispStatus" name="frmPBMReportsList" styleClass="selectBox selectBoxMedium">
		  	 		<%-- <html:option value="">select from list</html:option> --%>
		        	<html:option value="PD">PreApprvd and Dispensed</html:option>
                    <html:option value="PND">PreApprvd not Dispensed</html:option>
            	</html:select>
           
       <img title="Search" src="/ttk/images/SearchIcon.gif" height="18" width="16">
       <a href="#">
	   <button class="searchSbtn" accesskey="s"  onclick="return onSearch();"><u>S</u>earch</button>	 
	   </a>  
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
	
<input type="hidden" name="mode" value="">
<input type="hidden" name="rownum" value="">
<input type="hidden" name="sortId" value="">
<input type="hidden" name="pageId" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="backID" value="">
<input type="hidden" name="tab" value="">
<INPUT TYPE="hidden" NAME="loginType" VALUE="PBM">
<INPUT TYPE="hidden" NAME="reportId" VALUE="<%=request.getParameter("reportId")%>">
</html:form>
</div>