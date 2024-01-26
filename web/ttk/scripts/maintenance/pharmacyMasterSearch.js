//java script for the changedobo.jsp

function addPharmacy()
{
	document.forms[1].mode.value="doAddPharmacy";
	document.forms[1].action="/MaintainPharmacyAddAction.do";
	document.forms[1].submit();
}//end of changeOffice()

function onSearch()
{
	document.forms[1].mode.value="doSearch";
	document.forms[1].action="/MaintainPharmacySearchAction.do";
	document.forms[1].submit();
}//end of onSave()

function edit(rownum)
{
	document.forms[1].mode.value="doView";
	document.forms[1].rownum.value=rownum;
	document.forms[1].action="/MaintainPharmacyAddAction.do";
	document.forms[1].submit();
}//end of onClose()

//function to display next set of pages
function PressForward()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/MaintainPharmacySearchAction.do";
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/MaintainPharmacySearchAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/MaintainPharmacySearchAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)
