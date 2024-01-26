//java script called by bonuslist.jsp in enrollment flow

function onDelete()
{
    if(!mChkboxValidation(document.forms[1]))
    {
		var msg = confirm("Are you sure you want to delete the selected records ?");
		if(msg)
		{
			if(!JS_SecondSubmit)
			 {
				document.forms[1].mode.value = "doDeleteList";
				document.forms[1].action = "/BonusAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			 }
		}// end of if(msg)
	}//end of if(!mChkboxValidation(document.forms[1]))
}//end of onDelete()

function onClose()
{
	if(!TrackChanges()) return false;
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doClose";
		document.forms[1].action="/BonusAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onClose()

function onAdd()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].rownum.value="";
		document.forms[1].mode.value="doAdd";
		document.forms[1].child.value="Add Bonus";
		document.forms[1].action="/AddBonusAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onAdd()

function edit(rownum)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doViewBonus";
		document.forms[1].rownum.value=rownum;
		document.forms[1].action = "/AddBonusAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of edit(rownum)