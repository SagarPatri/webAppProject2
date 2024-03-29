<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.security.Cache"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="/ttk/scripts/onlineforms/EmployeeLogin/employeeClaim.js"></SCRIPT>
<style type="text/css">
.tob_class {
	background-color: rgb(0, 26, 102);
	font-weight: bold;
    width: 87px;
    height: 34px;
	margin-left: 608px;
    /* left: 50%; */
	color: #fff;
}

.formContainer {
	height: 120px;
	width: 80%;
	border: 1px solid black;
}

.gridWithCheckBox {
	width: 70%;
}
.textData
{
    padding-left: 20px;
}
.bottom_close_button{
}
.main_info_text_class {
    padding-left: 15px;
    margin-top: 40px;
    font-size: 14px;
    font-family: Arial, Helvetica, sans-serif;
}



</style>
<%
	pageContext.setAttribute("claimType",Cache.getCacheObject("claimType"));
	pageContext.setAttribute("sStatus", Cache.getCacheObject("claimStatusList"));
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
%>
<div class="contentArea" id="contentArea">
<div>
	<table align="center" class="pageTitle" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td width="57%">Claims History Log Search - <bean:write name="employeeHomeForm"
					property="caption" /></td>
		</tr>
	</table>
</div>

<h4 class="sub_heading">Claim Log Search</h4>
<br />
<br />
<div>
<html:errors/>
	<html:form action="/EmployeeHomeAction.do">
		<table align="center" class="formContainer" border="0" cellspacing="0"
			cellpadding="0">

			<tr>
				<td nowrap class="textData">Claim Submission <br/>From Date:<br> <html:text
						property="treatementStartDate" name="employeeHomeForm"
						styleClass="textBox textDate" maxlength="60" /><A
					NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
					onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].treatementStartDate',document.forms[1].treatementStartDate.value,'',event,148,178);return false;"
					onMouseOver="window.status='Calendar';return true;"
					onMouseOut="window.status='';return true;"><img
						src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate"
						width="24" height="17" border="0" align="absmiddle"></a>
				</td>
				<td nowrap>To Date:<br> <html:text
						property="treatementEndDate" name="employeeHomeForm"
						styleClass="textBox textDate" maxlength="60" /><A
					NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
					onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].treatementEndDate',document.forms[1].treatementEndDate.value,'',event,148,178);return false;"
					onMouseOver="window.status='Calendar';return true;"
					onMouseOut="window.status='';return true;"><img
						src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate"
						width="24" height="17" border="0" align="absmiddle"></a>
				</td>

				<td nowrap>Claim No.:<br> <html:text property="claimNo"
						name="employeeHomeForm" styleClass="textBox textBoxLarge"
						maxlength="60" />
				</td>

				<td nowrap>Provider Name:<br> <html:select
						property="providerName" styleId="state3"
						styleClass="selectBox selectBoxMedium">
						<html:option value="">Select from list</html:option>
						<html:optionsCollection name="providerNameList" label="cacheDesc"
							value="cacheId" />
					</html:select>
				</td>


			</tr>
			<tr>
				<td nowrap class="textData">Claim Type:<br> <html:select
						property="sClaimType" styleClass="selectBox selectBoxMedium"
						name="employeeHomeForm">
						<html:option value="">Any</html:option>
						<html:optionsCollection name="claimType" label="cacheDesc"
							value="cacheId" />
					</html:select>
				</td>
				<td nowrap>Invoice No.:<br> <html:text
						property="invoiceNo" name="employeeHomeForm"
						styleClass="textBox textBoxLarge" maxlength="60" />
				</td>
				<td nowrap>Batch No.:<br> <html:text
						property="claimBatchNumber" name="employeeHomeForm"
						styleClass="textBox textBoxLarge" maxlength="60" />
				</td>
				<td nowrap>Status:<br>
	            <html:select property="sStatus" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:onStatusChanged()">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
            	</html:select>
          		</td>
				<td><a href="#" accesskey="s" onClick="javascript:onClaimHistorySearch()"
					class="search"><img src="/ttk/images/SearchIcon.gif"
						title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
				</td>
			</tr>

		</table>

		<div class="tableDataList">
			<ttk:HtmlGrid name="EmpClaimHistoryTable" />
		</div>
		
		<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	
     	<ttk:PageLinks name="EmpClaimHistoryTable"/>
      	 <tr>
        	<td height="4" colspan="2"></td>
      	</tr> 
		</table>
<br/>
<div class="main_info_text_class">
<B>Note:</B>
<div class="info_text_class">
<ul style="list-style-type:square">
  <li>Please Click on Claim No. to view the details.</li>
</ul>
</div>
<br/>
</div>

	<!-- 	<input type="button" class="tob_class" value="Back"
			onClick="javascript:onEmplclaimHistoryClose()" /> -->
			
			<div class="bottom_close_button">		
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	     <button type="button" name="Button" accesskey="b" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onEmplclaimHistoryClose();"><u>B</u>ack</button>&nbsp;
	    </td>
	  </tr>
	</table>
		</div>
		<br/>
		<br/>
		<INPUT TYPE="hidden" NAME="startNum" VALUE="">
		<INPUT TYPE="hidden" NAME="endNum" VALUE="">
		<INPUT TYPE="hidden" NAME="mode" VALUE="">
		<INPUT TYPE="hidden" NAME="sortId" VALUE="">
		<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
		<INPUT TYPE="hidden" NAME="pageId" VALUE="">
		<input type="hidden" name="activeSubLink" value="<%=userSecurityProfile.getSecurityProfile().getActiveSubLink()%>" />
	</html:form>
</div>
</div>


