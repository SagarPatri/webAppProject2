//java script for the changedobo.jsp

function onUserSubmit()
{
	if(!JS_SecondSubmit)
    {
		document.forms[1].mode.value="doSavePharmacy";
		document.forms[1].action="/MaintainPharmacySaveAction.do";
		document.forms[1].submit();
    }
}//end of changeOffice()

function onReset()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doAddPharmacy";
	document.forms[1].action="/MaintainPharmacyAddAction.do";
	document.forms[1].submit();
}//end of onSave()

function onClose()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/MaintainPharmacySearchAction.do";
	document.forms[1].submit();
}//end of onClose()

function onCloseReview()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/MaintainPharmacyReviewSearchAction.do?PharmReview=Y";
	document.forms[1].submit();
}//end of onClose()

function onReviewSubmit()
{
	if(!JS_SecondSubmit)
    {
		document.forms[1].mode.value="doSavePharmacy";
		document.forms[1].action="/MaintainPharmacySaveAction.do?phrmRev="+document.forms[1].phrmRev.value;
		document.forms[1].submit();
    }
}//end of changeOffice()