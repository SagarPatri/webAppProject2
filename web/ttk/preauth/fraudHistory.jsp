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

<!DOCTYPE html> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fraud History</title>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
  <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript">
var JS_Focus_Disabled =true;

function closeBenefitDetails() {   
	//document.getElementById("mode").value = "doGeneral";
	document.forms[1].mode.value="doView";
    document.forms[1].action ="/PreAuthGeneralAction.do";
    document.forms[1].submit();
}
</script>
</head>

<body>
		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
				<tr>
						<td width="57%">Suspected Counter Fraud History</td>
						<td  align="right" class="webBoard"></td>
				</tr>
		</table>
		<html:form action="PreAuthGeneralAction.do">
		<div class="contentArea" id="contentArea">
		<html:errors/>

<fieldset>
		<legend>Fraud History</legend>
		<ttk:HtmlGrid name="tableData"/>
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">	
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
</html:form> 
</body>
</html>