//java script for the changedobo.jsp

function onSearch()
{
	document.forms[1].mode.value="doReviewSearch";
	document.forms[1].action="/MaintainPharmacyReviewSearchAction.do";
	document.forms[1].submit();
}//end of onSave()

function edit(rownum)
{
	document.forms[1].mode.value="doView";
	document.forms[1].rownum.value=rownum;
	document.forms[1].action="/MaintainPharmacyReviewViewAction.do?phrmRev=Y";
	document.forms[1].submit();
}//end of onClose()

//function to display next set of pages
function PressForward()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doReviewForward";
    document.forms[1].action = "/MaintainPharmacyReviewSearchAction.do";
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doReviewBackward";
    document.forms[1].action = "/MaintainPharmacyReviewSearchAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doReviewSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/MaintainPharmacyReviewSearchAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

