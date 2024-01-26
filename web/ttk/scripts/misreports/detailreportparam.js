function onGenerateDetailReport()
{
	var argList	=	"|"+document.forms[1].insuranceCompany.value+"|"+document.forms[1].sFloatAccNo.value+"|"+document.forms[1].fDebitNoteNo.value+"|"
	+document.forms[1].sProviderName.value+"|"+document.forms[1].fCorpName.value+"|"+document.forms[1].sChequeFromDate.value+"|"+document.forms[1].sChequeToDate.value+"|"+document.forms[1].fGroupId.value+"|";
	var partmeter = "?mode=doGenerateDetailReport&argList="+argList+"&repType=detail";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}//end of onGenerateDetailReport()

function onGeneratePendingReport()
{
	var argList	=	"|"+document.forms[1].insuranceCompany.value+"|"+document.forms[1].sFloatAccNo.value+"|"+document.forms[1].fDebitNoteNo.value+"|"+document.forms[1].sProviderName.value+"|"
	+document.forms[1].fCorpName.value+"|"+document.forms[1].sChequeToDate.value+"|"+document.forms[1].fGroupId.value+"|";//+"|"+document.forms[1].sChequeFromDate.value+
	var partmeter = "?mode=doGenerateDetailReport&argList="+argList+"&repType=pending";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}//end of onGeneratePendingReport()

function onGenerateHospitalReport()
{
	var argList	=	"|"+document.forms[1].sProviderName.value+"|"+document.forms[1].sEmpanelId.value+"|"
	+document.forms[1].sFloatAccNo.value+"|"+document.forms[1].sChequeFromDate.value+"|"+document.forms[1].sChequeToDate.value+"|"+document.forms[1].fGroupId.value+"|";;
	var partmeter = "?mode=doGenerateDetailReport&argList="+argList+"&repType=hospital";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}//end of onGenerateHospitalReport()


function onGeneratePolicyReport()
{
	var argList	=	"|"+document.forms[1].sPolicyNo.value+"|"+document.forms[1].sEnrolId.value+"|"
	+document.forms[1].sChequeFromDate.value+"|"+document.forms[1].sChequeToDate.value+"|"+document.forms[1].sFloatAccNo.value+"|"+document.forms[1].fGroupId.value+"|";;
	var partmeter = "?mode=doGenerateDetailReport&argList="+argList+"&repType=policy";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}//end of onGenerateHospitalReport()

function onClose(){
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/MISFinanceReportsListAction.do";
	document.forms[1].submit();
}

function onSwitch(){
	document.forms[1].mode.value="doSwitchTo";
	document.forms[1].action="/MISFinanceReportsListAction.do";
	document.forms[1].submit();
}

function onGenerateExchangeReport()
{
	if(document.forms[1].sExchangeToDate.value == ""){
		alert("Please Enter Date");
		return false;
	}
	var argList	=	"|"+document.forms[1].sExchangeToDate.value+"|"+document.forms[1].sCountry.value+"|";
	var partmeter = "?mode=doGenerateDetailReport&argList="+argList+"&repType=exchangeRate";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}//end of onGeneratePendingReport()

function doSearchExchangeRate()
{
	if(!JS_SecondSubmit)
	 {
		trimForm(document.forms[1]);
		document.forms[1].mode.value = "doSearchExchangeRate";
	    document.forms[1].action = "/MISFinanceReportsListAction.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
	 
}//end of onSearch()

//function to sort based on the link selected
function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearchExchangeRate";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/MISFinanceReportsListAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
	alert("ssdsfs");
	document.forms[1].reset();
    document.forms[1].mode.value="doSearchExchangeRate";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/MISFinanceReportsListAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/MISFinanceReportsListAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/MISFinanceReportsListAction.do";
    document.forms[1].submit();
}//end of PressForward()
