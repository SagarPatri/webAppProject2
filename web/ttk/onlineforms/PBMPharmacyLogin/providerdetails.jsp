<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%
//TTKCommon.setActiveLinks("Broker","Home","DashBoard",request);
%>
<head>
<style type="text/css">
.dashboardTable th{
padding: 10px;
text-align: left;
font-size: 14px;
}

.dashboardTable td{
padding: 10px;
text-align: left;
font-size: 14px;
}
</style>
<script type="text/javascript" src="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/onlineforms/PBMPharmacyLogin/providerdetails.js"></script>
<script type="text/javascript">


function onPreauthProccess(){		
	/* document.forms[1].mode.value ="doPreauthProccess";
	document.forms[1].child.value ="PreauthProccess";
	document.forms[1].action="/PbmPharmacyGeneralAction.do";
	document.forms[1].submit(); */
	
	
	
	document.forms[1].tab.value="PBM Check Eligibility";	
	document.forms[1].sublink.value="PBM Check Eligibility";
	document.forms[1].leftlink.value="PBMPreauth";
	document.forms[1].mode.value="doPreauthProccess";
	document.forms[1].action="/PbmPharmacyGeneralAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
}
</script>
</head>
<form action="/PbmPharmacyAction.do" >
<html:errors/>
<br>

<h4 class="sub_heading">Provider Details</h4>
<br>
<fieldset>
				<legend>Provider Details</legend>
<table class="formContainerWeblogin" style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">

   <tr>
 <th></th><td>&nbsp;</td>
 </tr>
    <tr style="margin-left:5PX;margin-top:10PX;">
  <th align="left" >Provider Name</th><th>:</th><td><bean:write name="hospName" scope="session"/></td>
  <th align="left">Empanelment ID</th><th>:</th><td><bean:write name="hospEmpnNO" scope="session"/></td>  
 </tr>
 <tr>
 <td>&nbsp;</td>
 </tr>
 <tr style="margin-left:5PX;">
 <th align="left">Country</th><th>:</th><td><bean:write name="hospCountry" scope="session"/></td>
 <th align="left">City</th><th>:</th><td><bean:write name="hospEmirate" scope="session"/></td>
 </tr>
 <tr>
 <td>&nbsp;</td>
 </tr>
 <tr style="margin-left:5PX;">
 <th align="left">Area</th><th>:</th><td><bean:write name="hospArea" scope="session"/></td>
 <th align="left">Address</th><th>:</th><td><bean:write name="hospAddress" scope="session"/></td>
 </tr>
 <tr>
 <th></th><td>&nbsp;</td>
 </tr>
 <tr style="margin-left:5PX;">
 <th align="left">Office Ph.NO</th><th>:</th><td><bean:write name="hospPhoneNO" scope="session"/></td>
 <th></th><th></th><td></td>
 </tr>
 <tr>
 <th></th><td>&nbsp;</td>
 </tr>
 <!-- <tr align="center">
 <td align="center" colspan="6">
 <div style="text-align: center;">
 <button type="button" name="mybutton" accesskey="p"  class="olbtn" onClick="onPreauthProccess();"><u>P</u>roceed</button>
 </div> 
 </td>
 </tr>-->
</table>
 
</fieldset>
<div style="text-align: center;margin-left:15PX;">
 <button type="button" name="mybutton" accesskey="p"  class="olbtn" onClick="onPreauthProccess();"><u>P</u>roceed</button>
 </div>

<input type="hidden" name="mode" value="">
<input type="hidden" name="tab" value="">
<input type="hidden" name="leftlink" value="">
<input type="hidden" name="sublink" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="loginType" value="PBM">
</form>
