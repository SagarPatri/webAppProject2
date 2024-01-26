<%
/** @ (#) policylist.jsp Jun 22, 2006
 * Project     : TTK Healthcare Services
 * File        : reportlist.jsp
 * Author      : Arun K N
 * Company     : Span Systems Corporation
 * Date Created: Feb 2, 2006
 *
 * @author 		 : Arun K N
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import=" com.ttk.common.TTKCommon" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<SCRIPT LANGUAGE="JavaScript">
bAction = false; //to avoid change in web board in product list screen
var TC_Disabled = true;
function onSelectLink(filename,reportID,reportName)
{
	document.forms[1].fileName.value=filename;
	document.forms[1].reportID.value=reportID;
	document.forms[1].reportName.value=reportName;
	if(document.forms[1].reportID.value == 'FinDaiBat')
	{
		document.forms[1].mode.value="doBatchReportsDetail";
    }//end of if(document.forms[1].reportID.value == 'FinDaiBat')
    else if(document.forms[1].reportID.value == 'FinTPAComm')
	{
		document.forms[1].mode.value="doTPAComReportsDetail";
    }//end of else if(document.forms[1].reportID.value == 'FinTPAComm')
    else if(document.forms[1].reportID.value =='FaxStsRpt')
    {
    	document.forms[1].mode.value="doFaxStatusReportsDetail";
    }//end of else if(document.forms[1].reportID.value =='FaxStsRpt')
    else if(document.forms[1].reportID.value == 'FinPenRpt')
    {
    	document.forms[1].mode.value="doClaimPendRptDetail";
    }//end of else if(document.forms[1].reportID.value == 'FinPenRpt')
    else if(document.forms[1].reportID.value == 'IOBBatRpt')
    {
    	document.forms[1].mode.value="doIOBReportDetail";
    }//end of else if(document.forms[1].reportID.value == 'IOBBatRpt')
    else if(document.forms[1].reportID.value == 'FinDetRpt')
    {
    	document.forms[1].mode.value="doClaimsReportDetail";
    }//end of else if(document.forms[1].reportID.value == 'FinDetRpt')
    else if(document.forms[1].reportID.value == 'AccentureRpt')
    {
    	document.forms[1].mode.value="doAccentureReportDetail";
    }//end of else if(document.forms[1].reportID.value == 'AccentureRpt')
    //Added for ibm
    else if(document.forms[1].reportID.value == 'IBMDeletion')
    {
    	document.forms[1].mode.value="doIBMReportDetail";
    }//else if(document.forms[1].reportID.value == 'IBMDeletion')
    else if(document.forms[1].reportID.value =='IBMAdditionMaxRec')
	{
		document.forms[1].mode.value='doIBMAdditioncutoffMaxRecReportDetail';
	}
	else if(document.forms[1].reportID.value == "IBMGrpPreAuthRpt")
	{
		document.forms[1].mode.value="doGenerateGrpPreAuthRpt";
	}//end of else if(document.forms[1].reportID.value == "GrpPreAuthRpt")
	else if(document.forms[1].reportID.value == "Reoptin")
	{
		document.forms[1].mode.value="doIBMReoptinRpt";
	}//end of else if(document.forms[1].reportID.value == "GrpPreAuthRpt")
	else if(document.forms[1].reportID.value == "MontlyRecon")
	{
		document.forms[1].mode.value="doIBMMontlyReconRpt";
	}//end of else if(document.forms[1].reportID.value == "GrpPreAuthRpt")
	else if(document.forms[1].reportID.value == "ChildBorn")
	{
		document.forms[1].mode.value="doIBMChildBornRpt";
	}//end of else if(document.forms[1].reportID.value == "GrpPreAuthRpt")
	else if(document.forms[1].reportID.value == "DailyReport")
	{
		document.forms[1].mode.value="doIBMDailyRpt";
	}//end of else if(document.forms[1].reportID.value == "GrpPreAuthRpt")
	else if(document.forms[1].reportID.value == "NewIBMDeletion")
	{
		document.forms[1].mode.value="doNewIBMDeletion";
	}
	//ended
    else if(document.forms[1].reportID.value == "PreAuthClmDetail")
    {
    	document.forms[1].mode.value="doPreAuthClmDetail";
    }//end of else if(document.forms[1].reportID.value == "PreAuthClmDetail")
    else if(document.forms[1].reportID.value == 'DailyTansferRpt' || 
    	    	document.forms[1].reportID.value == 'Annexure126qRpt'|| 
    	    		document.forms[1].reportID.value == 'ChallanDetailsRpt')
    {
        document.forms[1].mode.value="doTDSReports";
    }//end of else if(document.forms[1].reportID.value == 'DailyTansferRpt')
    	
    else if(document.forms[1].reportID.value == "FinClmSetldRpt" || document.forms[1].reportID.value == "FinClmInpRpt" || document.forms[1].reportID.value == "FinClmOutRpt")
    {
    	document.forms[1].mode.value="doClaimPendRptDetail";
    }//end of else if(document.forms[1].reportID.value == 'FinPenRpt')
    else if(document.forms[1].reportID.value == 'RoutineRpt')
    {
    	document.forms[1].mode.value="doRoutineReport";
    }//end of else if(document.forms[1].reportID.value == 'RoutineRpt')
   
    else if(document.forms[1].reportID.value == 'pendSumRpt')
    {
    	document.forms[1].mode.value="doPendingSummaryRpt";
    }//end of else if(document.forms[1].reportID.value == 'RoutineRpt')	
    else if(document.forms[1].reportID.value == 'FinPreAuthRpt')
    {
    	document.forms[1].mode.value="doPreApprovalReport";
    }	

    else if(document.forms[1].reportID.value == 'UnderwritingRpt')
    {
    	document.forms[1].mode.value="doUnderwritingRpt";
    }	
    	
    	else
    {
    	document.forms[1].mode.value="doReportsDetail";
    }//end of else
	document.forms[1].action.value="/ReportsAction.do";
	document.forms[1].submit();
}//end of onSelectLink(filename)

function onSelectEmpDependent(){
	document.forms[1].mode.value="doEmpDependent";
	document.forms[1].action.value="/ReportsAction.do";
	document.forms[1].submit();
}

function onSelectEmpDependentNoAccess()
{
	alert("Access is denied for the requested screen.\n"+"Please contact the administrator.");
	
	return false;
}
</SCRIPT>
<html:form action="/ReportsAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><%=TTKCommon.getActiveSubLink(request)%> Reports</td>
  </tr>
</table>
<!-- End of: Page Title -->
<div class="contentArea" id="contentArea">
<%
String subLink = TTKCommon.getActiveSubLink(request);
System.out.println("subLink = "+subLink);
%>
<% if(!"Enrollment".equals(subLink)) {%>
<!-- Start of Reports List -->
	<ttk:ReportsList/>
<!-- End of Reports List -->
<%} %>
<% if("Enrollment".equals(subLink)) {%>
  <fieldset>
    	<legend>Daily Reports</legend>
	    <table class="formContainer" align="left"  border="0" cellspacing="0" cellpadding="0">
    	<tr>
	    	<td class="formLabel">
		 		<ul class="liBotMargin">
		 		<% if (TTKCommon.isAuthorized(request, "EmpDependentList")) {%>
				<li class="liPad"><a href="#" onClick="javascript:onSelectEmpDependent()">Employee Dependent Report</a></li>
				<% }%>
				<% if (!TTKCommon.isAuthorized(request, "EmpDependentList")) {%>
				<li class="liPad"><a href="#" onClick="javascript:onSelectEmpDependentNoAccess()">Employee Dependent Report</a></li>
				<% }%>
	    		</ul>
	   		 </td>
    	</tr>
	    </table>
  </fieldset>
<%} %>    
</div>
<html:hidden property="fileName"/>
<html:hidden property="reportID"/>
<html:hidden property="reportName"/>
<html:hidden property="mode"/>
</html:form>