
function onSelectDoBoClaimsDetail()
{  
	document.forms[1].mode.value="doViewClaimsDetail";
	document.forms[1].action="/MISFinanceReportsListAction.do";
	document.forms[1].submit();
}//end of function onSelectDoBoClaimsDetail()

function onSelectFGPendingReport()
{  
	document.forms[1].mode.value="doViewFGPendingReport";
	document.forms[1].action="/MISFinanceReportsListAction.do";
	document.forms[1].submit();
}//end of function onSelectFGPendingReport()

function onSelectOrientalPaymentAdvice()
{  
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/MISPaymentAdviceAction.do";
	document.forms[1].submit();
}//end of function onSelectOrientalPaymentAdvice()

function onSelectCitibankClaimsDtsRpt()
{  
	document.forms[1].mode.value="doCitibankClaimsDetailRpt";
	document.forms[1].action="/MISFinanceReportsListAction.do";
	document.forms[1].submit();
}//end of function onSelectCitibankClaimsDtsRpt()

function onUniversalSampo()
{
	document.forms[1].mode.value="doUniversalSampo";
	document.forms[1].action="/MISFinanceReportsListAction.do";
	document.forms[1].submit();
}//end of function onUniversalSampo() 

function onSelectDtlRpt()
{
	document.forms[1].mode.value="doDetailReport";
	document.forms[1].action="/MISFinanceReportsListAction.do?repType=Paid Report&logicType=detailed";
	document.forms[1].submit();
}//end of function onUniversalSampo() 

function onPendingReport()
{
	document.forms[1].mode.value="doDetailReport";
	document.forms[1].action="/MISFinanceReportsListAction.do?repType=Pending Report&logicType=pending";
	document.forms[1].submit();
}//end of function onUniversalSampo() 
function onBankAcc()
{
	document.forms[1].mode.value="doBankAccountReport";
	document.forms[1].action="/MISFinanceReportsListAction.do?repType=Policy Holders Bank Account Report&logicType=policy";
	document.forms[1].submit();
}//end of function onUniversalSampo() 
/*function onHospitals()
{
	document.forms[1].mode.value="doBankAccountReport";
	document.forms[1].action="/MISFinanceReportsListAction.do?repType=Hospitals Bank Account Report&logicType=hospital";
	document.forms[1].submit();
}//end of function onUniversalSampo() 
*/

function onExchangeRates()
{
	document.forms[1].mode.value="doDetailReport";
	document.forms[1].action="/MISFinanceReportsListAction.do?repType=Exchange Rate&logicType=exchangeRate";
	document.forms[1].submit();
}//end of function onUniversalSampo() 
