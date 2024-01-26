<%@page import="com.ttk.dto.preauth.ActivityDetailsVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.dom4j.Document"%>
<%@page import="org.dom4j.Node"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Activity Details</title>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
  <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript">
var JS_Focus_Disabled =true;

function closeBenefitDetails(){
	document.forms[1].mode.value="doClose";
    document.forms[1].action = "/OnlineMemberDetailsAction.do";
    document.forms[1].submit();
}

function displayBenefitsBasedOnType(){
	var benefitType = document.getElementById("benefitType").value;
	document.forms[1].mode.value="onChangeBenefitDetails";
	document.forms[1].action="/OnlineMemberDetailsAction.do?benefitType4="+benefitType;	 
	document.forms[1].submit();
}

</script>
</head>
<%
UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
request.setAttribute("loginType", userSecurityProfile.getLoginType());
%>
<body>

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				    <td width="57%">Benefits Details</td>
					<td  align="right" class="webBoard"></td>
			  </tr>
		</table>
<html:form action="OnlineCashlessPartBackAction.do">
<div class="contentArea" id="contentArea">
<html:errors/>

	
	
		 	

<fieldset>
		<legend> Details of Benefits Utilized</legend>
		<fieldset>
		     <legend>Member/Policy Details</legend>
		     
				 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		        <td class="formLabel">Alkoot ID:</td> 
					  <td class="textLabel">
					 <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${enrollmentId}">
			         
					  </td>
					  <td class="formLabel">Member Inception Date:</td>
					  <td class="textLabel">
					  	  <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${policyIssueDate}">
					  </td>
					  </tr>
					  <tr>
					  <td class="formLabel">Policy No:</td>
					  <td class="textLabel">
				 <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${policyNumber}">
					  </td>
					   <td class="formLabel">Provider Networks:</td>
					  <td class="textLabel">
				 <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${productCatTypeID}">
					  </td>
					  </tr>
					  <tr>
					  <td class="formLabel">Policy start Date: </td>
					  <td class="textLabel">
			          <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${effectiveFromDate}">
					  </td>
					  <td class="formLabel">Policy End Date:</td>
					  <td class="textLabel">
					  	 <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${effectiveToDate}">
					  </td>
					  
		 </tr>
	   </table>	
					  </fieldset>
		  <fieldset>
		   <legend>Benefit Details</legend>
		  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		  <c:if test="${requestScope.loginType ne 'EMPL'}">
			  <tr>
			        <td class="formLabel">Sum Insured:</td>
						  <td class="textLabel">
						 <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${sumInsured}">
				         
						  </td>
						  <td class="formLabel">Available Sum Insured:</td>
						  <td class="textLabel">
						  	   <input type="text" class="textBox textBoxLarge textBoxDisabled" value="${availableSumInsured}">
							</td>
			 </tr>
		  </c:if>
		  
		 <tr>
		 	<c:if test="${requestScope.loginType eq 'EMPL'}">
		 	<td class="formLabel" style="width: 14%;">Benefit Type:</td>
		 	</c:if>
		 	<c:if test="${requestScope.loginType ne 'EMPL'}">
		 	<td class="formLabel">Benefit Type:</td>
		 	</c:if>
		 	<td class="textLabel">
		 	<SELECT onchange=displayBenefitsBasedOnType(); id=benefitType class="selectBox selectBoxMedium" >
		<%--   <OPTION value=""   ${benefitType1 eq '' ? 'selected' : ''} >ALL</OPTION>      --%>
		 	<%-- 	<OPTION value="ALTERNATIVE/COMPLEMENTARY MEDICINES" ${benefitType1 eq 'ALTERNATIVE/COMPLEMENTARY MEDICINES' ? 'selected' : ''}> ALTERNATIVE/COMPLEMENTARY MEDICINES</OPTION>
		 		<OPTION value="AREA OF COVERAGE" ${benefitType1 eq 'AREA OF COVERAGE' ? 'selected' : ''}>AREA OF COVERAGE</OPTION>
		 		<OPTION value="CHRONIC CONDITION" ${benefitType1 eq 'CHRONIC CONDITION' ? 'selected' : ''}>CHRONIC CONDITION</OPTION>
		 		<OPTION value="DAY CARE" ${benefitType1 eq 'DAY CARE' ? 'selected' : ''}>DAY CARE</OPTION>
		 		<OPTION value="GENERAL EXCLUSIONS" ${benefitType1 eq 'GENERAL EXCLUSIONS' ? 'selected' : ''}>GENERAL EXCLUSIONS</OPTION> --%>
		 		<OPTION value="OPT" ${benefitType1 eq 'OPT' ? 'selected' : ''}>OUT-PATIENT</OPTION>
		 		<OPTION value="IPT"  ${benefitType1 eq 'IPT' ? 'selected' : ''}>IN-PATIENT</OPTION>
		 		<OPTION value="OPL" ${benefitType1 eq 'OPL' ? 'selected' : ''}>OPTICAL</OPTION>
		 		<OPTION value="DNT" ${benefitType1 eq 'DNT' ? 'selected' : ''}>DENTAL</OPTION>
		 		<OPTION value="MAT" ${benefitType1 eq 'MAT' ? 'selected' : ''}>MATERNITY</OPTION>
		 		<OPTION value="OTH" ${benefitType1 eq 'OTH' ? 'selected' : ''}>OTHERS</OPTION>
		<%--  		<OPTION value="GLOBAL" ${benefitType1 eq 'GLOBAL' ? 'selected' : ''}>GLOBAL</OPTION>
		 		<OPTION value="PRE-EXISTING CONDITIONS" ${benefitType1 eq 'PRE-EXISTING CONDITIONS' ? 'selected' : ''}>PRE-EXISTING CONDITIONS</OPTION>
		 		<OPTION value="RI-COPAR" ${benefitType1 eq 'RI-COPAR' ? 'selected' : ''}>RI-COPAR</OPTION>
		 		<OPTION value="VACCINATIONS" ${benefitType1 eq 'VACCINATIONS' ? 'selected' : ''}>VACCINATIONS</OPTION>
		 		<OPTION value="HEALTH CHECK UP" ${benefitType1 eq 'HEALTH CHECK UP' ? 'selected' : ''}>HEALTH CHECK UP</OPTION> --%>
		 	</SELECT>
		 	
			</td>	
			<c:if test="${requestScope.loginType ne 'EMPL'}">
			 <td class="formLabel">Utilized Sum Insured:</td>
			<td class="textLabel">
				<input type="text" class="textBox textBoxLarge textBoxDisabled"  value="${utilizedsumInsured}">
			</td>
			</c:if>
			<c:if test="${requestScope.loginType eq 'EMPL'}">
				<td class="formLabel"></td>
				<td class="textLabel"></td>
			</c:if>
		 </tr>
	   </table>	
			</fieldset>
	<fieldset>
	<ttk:HtmlGrid name="tableData"/>
		</fieldset> 
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">	
		<%--  <tr><td class="formLabelBold">Final Remarks:</td></tr>
		<tr>
		<TD class=textLabel colSpan=5>
				<TEXTAREA cols=135 class="textAreaMediumhtDisabled" name=otherRemarks >${otherRemarks}</TEXTAREA></TD>
     	</tr> --%>
     	
     	
		 <tr>	
		 <td align="center">
		  <button type="button" onclick="closeBenefitDetails();" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>&nbsp;			  
		  </td>		  
		 </tr>
		 </table>
		</fieldset>
</div>	 		
<input type="hidden" id="mode" name="mode"/>
<INPUT TYPE="hidden" NAME="reforward" value="">
<input type="hidden" name="memberSeqID" id="memberSeqID" value="${memberSeqID}"/>
<input type="hidden" name="policySeqID" id="policySeqID" value="${policySeqID}"/>
</html:form>
</body>


</html>