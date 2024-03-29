//java script for the partner search screen in the empanelment of partner flow.

//function to get the substatus change
function onStatusChanged()
{
	document.forms[1].mode.value="statusChanged";
	document.forms[1].action="/PartnerAction.do";
	document.forms[1].submit();
}//end of onStatusChanged()
//function to call edit screen
function edit(rownum)
{
    document.forms[1].mode.value="EditPartner";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value="General";
    document.forms[1].action = "/EditPartnerSearchAction.do";
    document.forms[1].submit();
}//end of edit(rownum)
//function to sort based on the link selected
function toggle(sortid)
{
    document.forms[1].mode.value="PartnerSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/PartnerAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)
//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].mode.value="PartnerSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/PartnerAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)
//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].mode.value ="Backward";
    document.forms[1].action = "/PartnerAction.do";
    document.forms[1].submit();
}//end of PressBackWard()
//function to display next set of pages
function PressForward()
{
    document.forms[1].mode.value ="Forward";
    document.forms[1].action = "/PartnerAction.do";
    document.forms[1].submit();
}//end of PressForward()
//function to display pages based on search criteria
function onSearch()
{
	alert("Inside onseach Script");
if(!JS_SecondSubmit)
 {
	alert("Inside onseach Script");
    document.forms[1].sEmpanelmentNo.value=trim(document.forms[1].sEmpanelmentNo.value);
    document.forms[1].sPartnerName.value=trim(document.forms[1].sPartnerName.value);
    document.forms[1].mode.value = "PartnerSearch";
    document.forms[1].action = "/PartnerSearchAction.do";
    document.forms[1].submit();
	JS_SecondSubmit=true
 }//end of if(!JS_SecondSubmit)
}//end of onSearch()
//function to add partner
function onAddPartner()
{
    document.forms[1].rownum.value = "";
    document.forms[1].mode.value = "AddPartner";
    document.forms[1].tab.value="General";
    document.forms[1].action = "/EditPartnerSearchAction.do";
    document.forms[1].submit();
}//end of onAddPartner()
//copy the select partner to webboard
function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "CopyToWebBoard";
   		document.forms[1].action = "/PartnerAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()
