<%@page import="com.ttk.common.TTKCommon"%>
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
/* .tob_class {
	background-color: rgb(0, 26, 102);
	font-weight: bold;
	width: 87px;
	height: 34px;
	margin-left: auto;
	margin-right: auto;
	margin: auto;
	position: relative;
	left: 50%;
	color: #fff;
} */

.formContainer {
	height: 120px;
	width: 80%;
	border: 1px solid black;
}

.gridWithCheckBox {
	width: 70%;
}

.textData {
	padding-left: 20px;
}
.main_info_text_class {
    padding-left: 205px;
    margin-top: 40px;
    font-size: 14px;
    font-family: Arial, Helvetica, sans-serif;
}

/* .info_text_class {
    color: #0c48a2;
    font-weight: bold;
    margin-left: 3px;
    padding-top: 0%;
    margin-top: -12px;
    font-size: 12px;
} */
</style>
<%
	pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
%>
<div class="contentArea" id="contentArea">
	<div>
		<table align="center" class="pageTitle" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td width="57%">
				<c:if test="${requestScope.type eq 'PAT'}">
				Pre-Approval Shortfall List - 
				</c:if>
				<c:if test="${requestScope.type eq 'CLM'}">
					Claim Shortfall List - 
				</c:if>
				<bean:write name="employeeHomeForm" property="caption" /> 
				</td>
			</tr>
		</table>
	</div>
	<h4 class="sub_heading">
<c:if test="${requestScope.type eq 'PAT'}">
Pre-Approval Shortfall List
</c:if>

<c:if test="${requestScope.type eq 'CLM'}">
Claim Shortfall List
</c:if>
	</h4>
	<br /> <br />
	<div>
		<html:errors />
		<html:form action="/EmployeeHomeAction.do">
			<div class="tableDataList">
				<ttk:HtmlGrid name="empShortfallDetails" />
			</div>
			<br/><br/>
			<!-- <input type="button" class="tob_class" value="Back" onClick="javascript:onEmplShortfallClose()" /> -->
			
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	     <button type="button" name="Button" accesskey="b" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onEmplShortfallClose();"><u>B</u>ack</button>&nbsp;
	    </td>
	  </tr>
	</table>
	
			<INPUT TYPE="hidden" NAME="startNum" VALUE="">
			<INPUT TYPE="hidden" NAME="endNum" VALUE="">
			<INPUT TYPE="hidden" NAME="mode" VALUE="">
			<INPUT TYPE="hidden" NAME="sortId" VALUE="">
			<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
			<INPUT TYPE="hidden" NAME="pageId" VALUE="">
			<div class="main_info_text_class">
			<B>Note:</B>
			<div class="info_text_class">
			<ul style="list-style-type:square">
			<c:if test="${requestScope.type eq 'PAT'}">
			Please Click on Shortfall No. to view the information required by Al Koot for processing your pre-approval request further
			</c:if>
			<c:if test="${requestScope.type eq 'CLM'}">
			Please Click on Shortfall No. to view the information required by Al Koot for processing your claim further
			</c:if>
			</ul>
			</div>
			<br/>
			</div>
			
			
		</html:form>
	</div>
</div>


