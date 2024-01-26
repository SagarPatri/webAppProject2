<%@page import="com.ttk.common.TTKCommon"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="/ttk/scripts/onlineforms/EmployeeLogin/employeehome.js"></SCRIPT>
<script>
	TC_Disabled = true;
</script>
<head>
<script type="text/javascript">
	function edit(rownum) {
		document.forms[1].mode.value = "doViewEmpDetails";
		document.forms[1].rownum.value = rownum;
		document.forms[1].action = "/EmpAddEnrollmentAction.do";
		document.forms[1].submit();
	}//end of editRoot(strRootIndex)
</script>
<style type="text/css">
.fieldset_class {
	padding-left: 9px;
	width: 79%;
	margin-left: 130px;
	font-size: 13px;
}

.textData {
	font-weight: bold;
	padding-bottom: 10px;
}

.tableDataList {
	width: 80%;
	margin-left: 110px;
	font-size: 12px;
}

/* .tob_class,.benefit_class,.ecard_class {
	background-color: rgb(0, 26, 102);
	font-weight: bold;
	width: 8%;
	height: 31px;
	margin-left: auto;
	margin-right: auto;
	margin: auto;
	color: #fff;
	left: 42%;
} */

.tob_class,.benefit_class,.ecard_class {
    background-color: rgb(0, 26, 102);
    font-weight: bold;
    width: 8%;
    height: 31px;
    color: #fff;
    text-align: center;
    padding: 8px;
    padding-right: 12px;
    padding-left: 0px;
}

.info_text_class {
	color: #0c48a2;
	font-weight: bold;
}

.main_info_text_class {
	padding-left: 205px;
	margin-top: 115px;
}

.info_text_close_class {
	MARGIN-LEFT: 500px;
}

textarea {
	overflow-y: scroll;
}

.button_class {
	float: right;
	margin-right: 305px;
	padding-top: 15px;
}

.diagnosis_fieldset_class {
	margin-left: 25px;
	width: 90%;
	border: 1px solid #CCCCCC;
	padding-top: 0px;
	padding-bottom: 10px;
}

.textAreaBox {
	height: 63px;
	width: 35%;
}

input {
	border: 0;
}


#errorMessage {
	color: #a83108;
	width: 50%;
	height: 20px;
	font-style: bold;
	margin-left: 25px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	padding: 0px;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 15px;
	color: #ff0000;
}
.save_button_class {
    margin-right: 50px;
    background-color: rgb(0, 26, 102);
    color: #fff;
    font-weight: bold;
    height: 5%;
}
</style>
</head>
<%
String activeSubLink=TTKCommon.getActiveSubLink(request);
request.setAttribute("activeSubLink", activeSubLink);
boolean conViewMode=true;
String editable=null;
if(session.getAttribute("editable")!=null)
	editable=(String) session.getAttribute("editable");
if("ContactInfo".equals(editable))
	conViewMode=false;
%>
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="57%">
<c:choose>
	<c:when test="${requestScope.activeSubLink eq 'Pre-Approval'}">
		Pre-Approval Details -
	</c:when>
	<c:otherwise>
		Claims Details -
	</c:otherwise>
</c:choose>
			
 <bean:write name="employeeHomeForm" property="caption"/></td>
			<td width="43%" align="right" class="webBoard">&nbsp;&nbsp;</td>
		  </tr>
</table>
		
<h4 class="sub_heading">Report To Alkoot</h4>
<div style="margin-left: 4px;width: 94%;">
<html:errors />

<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			  		<td>
			  			<img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="successMsg" scope="request"/>
			  		</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>
<div>
<input type="text" value="" id="errorMessage" readonly="readonly" />
</div>

<html:form action="/EmployeeHomeAction.do" styleId="contactForm">

	<div>
		<fieldset class="diagnosis_fieldset_class">
			<legend>
			<c:choose>
			<c:when test="${requestScope.activeSubLink eq 'Pre-Approval'}">
			Pre-Approval Details
			</c:when>
			<c:otherwise>
			Claim Details
			</c:otherwise>
			</c:choose>
			</legend>
			<table align="center" class="formContainer" border="0"
				cellspacing="0" cellpadding="0" style="width: 98%">
				<tr>
				<c:choose>
			<c:when test="${requestScope.activeSubLink eq 'Pre-Approval'}">
			<td width="15%" class="formLabel">Pre-Approval No.:</td>
					<td class="textLabel"> <bean:write name="employeeHomeForm"
							property="preAuthNumber" /></td>
			</c:when>
			<c:otherwise>
			<td width="15%" class="formLabel">Claim Number.:</td>
					<td class="textLabel"> <bean:write name="employeeHomeForm"
							property="claimNumber" /></td>
			</c:otherwise>
			</c:choose>
					

					<td width="15%" class="formLabel">Alkoot Id :</td>
					<td class="textLabel"> <bean:write name="employeeHomeForm"
							property="enrollmentID" /></td>
				</tr>

				<tr>
					<td width="15%" class="formLabel">Provider Name :</td>
					<td class="textLabel"> <bean:write name="employeeHomeForm"
							property="providerName" /></td>

					<td width="15%" class="formLabel">Member Name :</td>
					<td class="textLabel"> <bean:write name="employeeHomeForm" property="memName" /></td>
				</tr>
				<tr>
					<td width="15%" class="formLabel">Contact Number :</td>
					<td class="textLabel"> <bean:write name="employeeHomeForm"
							property="contactNumber" /></td>

					<td width="15%" class="formLabel">Email Id :</td>
					<td class="textLabel"> <bean:write name="employeeHomeForm" property="emailID3" /></td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset class="diagnosis_fieldset_class">
		<legend>Contact Information </legend>
			<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="formLabel">Contact Number: </td>
					<td class="textLabel">
					<html:text property="customerNbr" name="employeeHomeForm" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" readonly="<%=conViewMode%>" disabled="<%=conViewMode%>" />
					</td>
					<td class="formLabel">Email Id: </td><TD class="textLabel">
					<html:text property="emailID2" name="employeeHomeForm" styleClass="textBoxWeblogin textBoxMediumWeblogin"  readonly="true" disabled="true" style="background-color: #EEEEEE;" /></TD>
					</tr>
					<%if("ContactInfo".equals(editable)){ %>
					<tr><td class="formLabel"><input type="button" name="saveForm" value="Update" id="save_con_id" class="save_button_class" onclick="javascript:onSaveAccountInfo();"></td></tr>
					<%} %>
			</table>
			<span class="mandatorySymbol"><font size=2 color= "red" style="font-family: Arial, Helvetica, sans-serif;">The above contact details are registered with us. Please <a href="#" onclick="javascript:onEditContactInfo();" style="color:#ff0000;"> <b>Click Here</b></a> if you wish to change the Contact information</font></span>
		</fieldset>

		<fieldset class="diagnosis_fieldset_class">
		
				
			<legend>Message</legend>
			<table align="center" class="formContainer" border="0"cellspacing="0" cellpadding="0" style="width: 98%">
			<tr>
			<td style="padding-left: 15px; width: 5%;">Message<span class="mandatorySymbol">*</span></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<html:textarea name="employeeHomeForm"  property="finalRemarks"
			styleClass="textAreaBox" cols="50" rows="4" styleId="finalRemarksId" /></td>
			</tr>
			</table>
		</fieldset>
		</div>

<div id="id01" class="w3-modal">
 <div class="w3-modal-content w3-card-4 w3-animate-zoom">
  <!-- <div class="header_popup_class">
  <header class="w3-container"> 
   <div>
    <span onclick="document.getElementById('id01').style.display='none'" 
   class="w3-button w3-blue w3-xlarge w3-display-topright" onmouseover="this.className='w3-button w3-blue w3-xlarge w3-display-topright close_btn_class'" onmouseout="this.className='w3-button w3-blue w3-xlarge w3-display-topright'">
   <span class="close_button_class">&times;</span>
   <span class="close_button_class" style="font-size: 16px;">X</span>
   </span> 
   
   </div>
   <h2></h2>
  </header></div> -->
<div class="body_popup_class">
<p id="responseMessage"></p>
<!-- Your Query Will be attended <br/>by our representative shortly -->
<span id="show_popup"></span>
  </div>

  <div class="w3-container w3-padding footer_popup_class">
   <span class="w3-button poup_ok_class" 
   onclick="document.getElementById('id01').style.display='none'">OK</span>
  </div>
 </div>
</div>

	<!-- <div class="button_class">
		<input type="button" class="tob_class" value="Back" onClick="javascript:goBackToPreAuth();" /> 
		&nbsp;&nbsp;<input type="button" class="benefit_class" value="Submit" onClick="javascript:onSubmit();"/> 
	</div> -->
	
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    
	    <!-- <button type="button" name="Button" accesskey="s" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:goBackToPreAuth();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;&nbsp; -->
	     <button type="button" name="Button" accesskey="b" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:goBackToPreAuth();"><u>B</u>ack</button>&nbsp;
	   <!-- 	<span class="button_design_class" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onSubmit();">Submit</span> -->
	   	<button type="button" name="Button" accesskey="s" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" id="btnSubmit"><u>S</u>ubmit</button>&nbsp;
	   	<!-- onClick="javascript:onSubmit();" -->
	    </td>
	  </tr>
	</table>
	<input type="hidden" name="mode" value="">
	<input type="hidden" name="editable" value="<%=editable%>">
	<input type="hidden" name="reportFlag" value="${requestScope.reportFlag}" />
</html:form>