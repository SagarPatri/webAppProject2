
/* function onGeneratReport()
{
	document.forms[1].mode.value="doGenerateExcelReportIntX";
	var partmeter = "?mode=doGenerateExcelReportIntX&parameter=PRE&reportID=PreAuthUtilization&fileName=PreAuthUtilization"+"&reportType=EXL&startDate="+document.getElementById("sStartDate").value+"&endDate="+document.getElementById("sEndDate").value+
			"&payerCodes="+document.getElementById("payerCodes").value+"&providerCodes="+document.getElementById("providerCodes").value+"&corporateCodes="+document.getElementById("corporateCodes").value;
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}
function onCLMGeneratReport()
{
	document.forms[1].mode.value="doGenerateExcelReportIntX";
	var partmeter = "?mode=doGenerateExcelReportIntX&parameter=CLM&reportID=PreAuthUtilization&fileName=PreAuthUtilization"+"&reportType=EXL";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
} */
function onERPGeneratReport()
{
	if(compareDates("sStartDate","From Date","sEndDate","To Date","greater than")==false)
	{
		document.forms[1].sEndDate.value="";
	    return false;
	}
	
	if(document.forms[1].payerCodes.value=="")
		{
		alert("Please Select Authority");
		document.forms[1].payerCodes.focus();
		return false;
		}
 	else if(document.forms[1].eType.value=="")
	{
	alert("Please Select Enrollment Type");
	document.forms[1].eType.focus();
	return false;
	} 
	else if(document.forms[1].insCompanyCode.value=="")
	{
	alert("Please Select Insurance Company");
	document.forms[1].insCompanyCode.focus();
	return false;
	}
	else if(document.forms[1].sStartDate.value=="")
	{
	alert("Please Enter Start Date");
	document.forms[1].sStartDate.focus();
	return false;
	}
	else if(document.forms[1].sEndDate.value=="")
	{
	alert("Please Enter End Date");
	document.forms[1].sEndDate.focus();
	return false;
	}
	if(!isDate(document.forms[1].sStartDate1,"From Date"))
   	{
   		document.forms[1].sStartDate1.focus();
   		return false;
   	}
   	if(!isDate(document.forms[1].sEndDate1,"To Date"))
   	{
   		document.forms[1].sEndDate.focus();
   		return false;
   	}
    if(!compareDates('sStartDate1','From Date','sEndDate1','To Date','greater than'))
    {
    	return false;
    }//end of if(!compareDates('sReportFrom','Report From','sReportTo','Report To','greater than'))

	document.forms[1].mode.value="doGenerateExcelReportIntX";
	var partmeter = "?mode=doGenerateExcelReportIntX&parameter=ERP&reportID=PreAuthUtilization&fileName=PreAuthUtilization"+"&reportType=EXL&sStartDate="+document.getElementById("sStartDate1").value+"&sEndDate="+document.getElementById("sEndDate1").value+"&insCompanyCode="+document.getElementById("insCompanyCode").value+"&sAgentCode="+document.getElementById("sAgentCode").value+"&eType="+document.getElementById("eType").value+"&sGroupPolicyNo="+document.getElementById("sGroupPolicyNo").value+"&payerCodes="+document.getElementById("payerCodes").value+"&corporateCodes="+document.getElementById("corporateCodes").value+"&sType="+document.getElementById("sType").value;
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}
function onPRPGeneratReport()
{
	if(compareDates("sStartDate","From Date","sEndDate","To Date","greater than")==false)
	{
		document.forms[1].sEndDate.value="";
	    return false;
	}
	if(document.forms[1].payerCodes.value=="")
	{
	alert("Please Select Authority");
	document.forms[1].payerCodes.focus();
	return false;
	}
	else if(document.forms[1].sStatus.value=="")
	{
	alert("Please Select Status");
	document.forms[1].sStatus.focus();
	return false;
	}
	else if(document.forms[1].insCompanyCode.value=="")
	{
	alert("Please Select Insurance Company");
	document.forms[1].insCompanyCode.focus();
	return false;
	}
	else if(document.forms[1].sStartDate.value=="")
	{
	alert("Please Enter Start Date");
	document.forms[1].sStartDate.focus();
	return false;
	}
	else if(document.forms[1].sEndDate.value=="")
	{
	alert("Please Enter End Date");
	document.forms[1].sEndDate.focus();
	return false;
	}
	if(!isDate(document.forms[1].sStartDate1,"From Date"))
   	{
   		document.forms[1].sStartDate.focus();
   		return false;
   	}
   	if(!isDate(document.forms[1].sEndDate1,"To Date"))
   	{
   		document.forms[1].sEndDate.focus();
   		return false;
   	}
    if(!compareDates('sStartDate1','From Date','sEndDate1','To Date','greater than'))
    {
    	return false;
    }//end of if(!compareDates('sReportFrom','Report From','sReportTo','Report To','greater than'))

	document.forms[1].mode.value="doGenerateExcelReportIntX";	
	var partmeter = "?mode=doGenerateExcelReportIntX&parameter=PRP&reportID=PreAuthUtilization&fileName=PreAuthUtilization"+"&reportType=EXL&startDate="+document.getElementById("sStartDate1").value+"&endDate="+document.getElementById("sEndDate1").value+"&insCompanyCode="+document.getElementById("insCompanyCode").value+"&sGroupPolicyNo="+document.getElementById("sGroupPolicyNo").value+"&payerCodes="+document.getElementById("payerCodes").value+"&sStatus="+document.getElementById("sStatus").value+"&providerCodes="+document.getElementById("providerCodes").value+"&corporateCodes="+document.getElementById("corporateCodes").value;
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}
function onCLRGeneratReport()
{
	if(compareDates("sStartDate","From Date","sEndDate","To Date","greater than")==false)
	{
		document.forms[1].sEndDate.value="";
	    return false;
	}

	if(document.forms[1].sStatus.value=="")
	{
	alert("Please Select Status");
	document.forms[1].sStatus.focus();
	return false;
	}
	else if(document.forms[1].sType.value=="")
	{
	alert("Please Select Claim Type");
	document.forms[1].sType.focus();
	return false;
	}
	else if(document.forms[1].insCompanyCode.value=="")
	{
	alert("Please Select Insurance Company");
	document.forms[1].insCompanyCode.focus();
	return false;
	}
	else if(document.forms[1].sStartDate.value=="")
	{
	alert("Please Select Claim Start Date");
	document.forms[1].sStartDate.focus();
	return false;
	}
	else if(document.forms[1].sEndDate.value=="")
	{
	alert("Please Select Claim End Date");
	document.forms[1].sEndDate.focus();
	return false;
	}
	if(!isDate(document.forms[1].sStartDate,"Claim StartDate"))
   	{
   		document.forms[1].sStartDate.focus();
   		return false;
   	}

	if(!isDate(document.forms[1].sEndDate,"Claim EndDate"))
   	{
   		document.forms[1].sEndDate.focus();
   		return false;
   	}

	document.forms[1].mode.value="doGenerateExcelReportIntX";
	var partmeter = "?mode=doGenerateExcelReportIntX&parameter=CLR&reportID=PreAuthUtilization&fileName=PreAuthUtilization"+"&reportType=EXL&insCompanyCode="+document.getElementById("insCompanyCode").value+"&sGroupPolicyNo="+document.getElementById("sGroupPolicyNo").value+"&sType="+document.getElementById("sType").value+"&sStatus="+document.getElementById("sStatus").value+"&sAgentCode="+document.getElementById("sAgentCode").value+"&providerCodes="+document.getElementById("providerCodes").value+"&corporateCodes="+document.getElementById("corporateCodes").value+"&sStartDate="+document.getElementById("sStartDate").value+"&sEndDate="+document.getElementById("sEndDate").value;
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}

/* function onPRMGeneratReport()
{
	document.forms[1].mode.value="doGenerateExcelReportIntX";
	var partmeter = "?mode=doGenerateExcelReportIntX&parameter=PRM&reportID=PreAuthUtilization&fileName=PreAuthUtilization"+"&reportType=EXL";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}
function onCSMGeneratReport()
{
	document.forms[1].mode.value="doGenerateExcelReportIntX";
	var partmeter = "?mode=doGenerateExcelReportIntX&parameter=CSM&reportID=PreAuthUtilization&fileName=PreAuthUtilization"+"&reportType=EXL";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}
 */


