// Java Script function for copaymentcharges.jsp
function onSave()
{	
	if(!JS_SecondSubmit)
	{
		document.forms[1].mode.value="doSave";
		document.forms[1].action="/CopaymentSaveAction.do";
		document.forms[1].submit();
	}
}//end of onSave()

function onClose()
{
  document.forms[1].mode.value="doClose";  
  document.forms[1].action = "/AdminHospitalsAction.do";
  document.forms[1].submit();
}//end of onClose()

function onReset()
{
    document.forms[1].reset(); 
}//end of onReset()
function onDeleteIcon(rownum)
{
	var msg = confirm("Are you sure you want to delete this provier copay ?");
    if(msg)
    {
    	document.forms[1].rownum.value=rownum;
    	document.forms[1].mode.value="doDeleteProviderCopay";  
    	document.forms[1].action = "/DeleteCopaymentSaveAction.do";
    	document.forms[1].submit();
    }
}//end of onClose()


//function to sort based on the link selected
function toggle(sortid)
{   document.forms[1].reset();
  document.forms[1].mode.value="doSearch";
  document.forms[1].sortId.value=sortid;
  document.forms[1].action = "/CopaymentAction.do";
  document.forms[1].submit();
}//end of toggle(sortid)

//JavaScript function for Page Indexing
function pageIndex(pagenumber)
{
	document.forms[1].reset();
	document.forms[1].mode.value="doSearch";
	document.forms[1].pageId.value=pagenumber;
	document.forms[1].action="/CopaymentAction.do";
	document.forms[1].submit();
}// End of pageIndex()

//function to display next set of pages
function PressForward()
{
  document.forms[1].reset();
  document.forms[1].mode.value ="doForward";
  document.forms[1].action = "/CopaymentAction.do";
  document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{  
  document.forms[1].reset();
  document.forms[1].mode.value ="doBackward";
  document.forms[1].action = "/CopaymentAction.do";
  document.forms[1].submit();
}//end of PressBackWard()