//Cashbenefit.js is called from the Cashbenefit.js
//KOC 1270 for hospital cash benefit 
function toggle(sortid)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doDefault";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/CashBenefitConfiguration.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to call edit screen
function edit(rownum)
{
    document.forms[1].mode.value="doViewPlanDetails";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value="General";
    document.forms[1].action = "/EditCashBenefitAction.do";
    document.forms[1].submit();
}//end of edit(rownum)

//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doDefault";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/CashBenefitConfiguration.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages

//function to display next set of pages
function PressForward()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/CashBenefitConfiguration.do";
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/CashBenefitConfiguration.do";
    document.forms[1].submit();
}//end of PressBackWard()

function onCashBenefitAdd()
{
	document.forms[1].mode.value="doCashBenefitAdd";
	document.forms[1].action="/EditCashBenefitAction.do";
	document.forms[1].submit();
}//end of onCashBenefitAdd()

function onClose()
{   
    document.forms[1].mode.value="doClose";
    document.forms[1].action = "/CashBenefitConfiguration.do";
    document.forms[1].submit();
}//end of onClose()