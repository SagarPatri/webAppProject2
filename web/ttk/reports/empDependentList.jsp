<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript">

function onGenerateReport(){
var groupid=document.getElementById("grpList").value;	
var policyNo=document.getElementById("policyNo").value;
var fromDate=document.getElementById("fromDate").value;
var toDate=document.getElementById("toDate").value;
var status=document.getElementById("status").value;

var reportType=document.getElementById("reportType").value;
var reportID=document.getElementById("reportID").value;

if(groupid==""){
	alert("Please select Group Name.");
	document.forms[1].grpList.value="";
	document.forms[1].grpList.focus();
    return false;
}

if(policyNo==""){
	alert("Please select Policy No.");
	document.forms[1].policyNo.value="";
	document.forms[1].policyNo.focus();
    return false;
}

if(fromDate==""){
	alert("Please select From Date.");
	document.forms[1].fromDate.value="";
	document.forms[1].fromDate.focus();
    return false;
}
if(isDate(document.forms[1].fromDate,"From Date")==false)
{
	document.forms[1].fromDate.focus();
	return false;
}

if(toDate!=""){
	if(isDate(document.forms[1].toDate,"To Date")==false)
	{
		document.forms[1].toDate.focus();
		return false;
	}
}

var startDate =document.getElementById("fromDate").value;    	
var endDate=document.getElementById("toDate").value;

if( !((document.getElementById("fromDate").value)==="") && !((document.getElementById("toDate").value)===""))
{
	var sdate = startDate.split("/");
	var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
	altsdate=new Date(altsdate);
	
	var edate =endDate.split("/");
	var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
	altedate=new Date(altedate);
	
	var Startdate = new Date(altsdate);
	var EndDate =  new Date(altedate);
	
	if(EndDate < Startdate)
	 {
		alert("To Date should be greater than or equal to From Date");
		document.getElementById("dischargeDate").value="";
		return ;
	 }
} 

var strParam = "";
strParam = "|"+policyNo+"|"+fromDate+"|"+toDate+"|"+status+"|"+groupid+"|";

var reportType=document.getElementById("reportType").value;
var reportID=document.getElementById("reportID").value;

var openPage = "/ReportsAction.do?mode=doGenerateEmpDependentReport"+"&strParam="+strParam+"&reportType="+reportType+"&reportID="+reportID+"&fromDate="+fromDate+"&toDate="+toDate;
var w = screen.availWidth - 10;
var h = screen.availHeight - 99;
var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
window.open(openPage,'',features);
}

function displayPolicyNoList()
{
	document.forms[1].mode.value="doDisplayPolicyNoList";
	document.forms[1].action="/ReportsAction.do";
	document.forms[1].submit();
}

function onClose()
{
	document.forms[1].mode.value="doCloseEmpDependent";
	document.forms[1].action="/ReportsAction.do";
	document.forms[1].submit();
}
</script>
<%
	pageContext.setAttribute("groupList",Cache.getCacheObject("groupList"));
%>
<html:form action="/ReportsAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>List of Employees and Dependents (Active/Cancelled) for a Period</td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Form Fields -->
   
	<fieldset>
		<legend>Report Parameters </legend>
			<table border="0" align="center" cellpadding="0" cellspacing="0" class="formContainer">
					<tr>
							<td>Group Name:<span class="mandatorySymbol">*</span> </td>
   							<td>	<html:select property ="grpList" styleId="grpList" styleClass="selectBox selectBoxMedium" onchange="javascript:displayPolicyNoList();">
           						<html:option value="">Select from list</html:option>
           						<html:options collection="groupList" property="cacheId" labelProperty="cacheDesc"/>
   								</html:select>
   							
							</td>
							<td class="formLabel">Policy No <span class="mandatorySymbol">*</span> </td>
							   <td>
	         					<html:select property="policyNo" styleId="policyNo" styleClass="selectBox selectBoxMedium">
										<html:option value="">Select from list</html:option>	            				
			            				<%-- <html:options collection="" property="cacheId" labelProperty="cacheDesc"/> --%>
	         				   			<html:optionsCollection  property="policyNoList" label="cacheDesc" value="cacheId" />
	         				   </html:select>
	       				   </td>
					</tr>
				<tr>
					 <td nowrap>From Date <span class="mandatorySymbol">*</span> </td>
							<td>
								<html:text property="fromDate" styleId="fromDate"	styleClass="textBox textDate" maxlength="10" /> 
								<a NAME="CalendarObjectstartDate" ID="CalendarObjectstartDate" HREF="#"	onClick="javascript:show_calendar('CalendarObjectstartDate','forms[1].fromDate',document.forms[1].fromDate.value,'',event,148,178);return false;"	onMouseOver="window.status='Calendar';return true;"	onMouseOut="window.status='';return true;">
								<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="startDate" width="24" height="17" border="0" align="absmiddle"></a>
							</td>
					<td nowrap>To Date </td>
					<td>
						<html:text property="toDate" styleId="toDate" styleClass="textBox textDate" maxlength="10" />
						<a NAME="CalendarObjectendDate" ID="CalendarObjectendDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectendDate','forms[1].toDate',document.forms[1].toDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
						<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="endDate" width="24" height="17" border="0" align="absmiddle"></a>
					</td>
					<td class="formLabel">Status</td>
							   <td>
	         					<html:select property="status" styleId="status" styleClass="selectBox">
										<html:option value="ALL">Any</html:option>	            				
			            				<html:option value="POA">Active</html:option>
			            				<html:option value="POC">Cancelled</html:option>
	         				   </html:select>
	       				   </td>
				</tr>
			</table>
	</fieldset>
	<!-- E N D : Form Fields -->
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">Report Type 
			
					<html:select property="reportType" styleId="reportType" styleClass="selectBox">
						<html:option value="PDF">PDF</html:option>	            				
			            <html:option value="EXL">EXCEL</html:option>
			        </html:select> 	&nbsp;
				
				<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateReport();"><u>G</u>enerateReport</button>
				&nbsp;
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
			</td>
		</tr>
	</table>
	<!-- End of Reports List -->
	</div>	
	<html:hidden property="mode"/>
	<html:hidden property="reportID" styleId="reportID" value="ListEmpDepPeriod"/>
</html:form>
