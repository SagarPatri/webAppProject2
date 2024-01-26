// Java Script function for copaymentsyncSearch.jsp

function onSearch()
{
  document.forms[1].mode.value ="doSearchSyncList";
  document.forms[1].action = "/CopaymentAction.do";
  document.forms[1].submit();
}//end of PressForward()

function onSynchronizeAll()
{	
	var msg = confirm("Provider wise copay will Synchronize to all other Providers ?");
	if(msg)
	{
		if(!JS_SecondSubmit)
		{
			document.forms[1].mode.value="doSynchronizeAll";
			document.forms[1].action="/CopaymentAction.do";
			document.forms[1].submit();
		}
	}
}//end of onSynchronizeAll()

function onSynchronize()
{
	if(!mChkboxValidation(document.forms[1]))
    {
		if(!JS_SecondSubmit)
		{
			document.forms[1].mode.value="doSynchronizeAll";
			document.forms[1].action="/CopaymentAction.do";
			document.forms[1].submit();
		}
    }
}//end of onSynchronizeAll()

function onClose()
{
  document.forms[1].mode.value="doClose";  
  document.forms[1].action = "/AdminHospitalsAction.do";
  document.forms[1].submit();
}//end of onClose()


//function to sort based on the link selected
function toggle(sortid)
{ 
  document.forms[1].mode.value="doSearchSyncList";
  document.forms[1].sortId.value=sortid;
  document.forms[1].action = "/CopaymentAction.do";
  document.forms[1].submit();
}//end of toggle(sortid)

//JavaScript function for Page Indexing
function pageIndex(pagenumber)
{
	document.forms[1].mode.value="doSearchSyncList";
	document.forms[1].pageId.value=pagenumber;
	document.forms[1].action="/CopaymentAction.do";
	document.forms[1].submit();
}// End of pageIndex()

//function to display next set of pages
function PressForward()
{
  document.forms[1].mode.value ="doForward";
  document.forms[1].action = "/CopaymentAction.do";
  document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{  
  document.forms[1].mode.value ="doBackward";
  document.forms[1].action = "/CopaymentAction.do";
  document.forms[1].submit();
}//end of PressBackWard()