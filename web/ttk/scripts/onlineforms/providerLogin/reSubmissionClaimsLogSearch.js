//on proceed corporate
function onSearch()
{
	var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	//var trtmtFromDate=document.forms[1].trtmtFromDate.value;
	// var trtmtToDate=document.forms[1].trtmtToDate.value;
		var clmFromDate=document.forms[1].clmFromDate.value;
		 var clmToDate=document.forms[1].clmToDate.value;
	if (clmFromDate.length>1 && !pattern.test(clmFromDate) || clmToDate.length>1 && !pattern.test(clmToDate)){
    alert("Date format should be (dd/mm/yyyy)");
    return;
   }
	
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doSearchClaim";
   	document.forms[1].action="/OnlineProviderResubmissionClaimsSearchAction.do";
   	JS_SecondSubmit=true;
   	document.forms[1].submit();
	}
}


function edit(rownum)
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value="doViewClaimDetails";
    document.forms[1].rownum.value=rownum;
    document.forms[1].action = "/ViewOnlineReSubmissionClaimDetails.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of edit(rownum)

//function to sort based on the link selected
function toggle(sortid)
{
	if(!JS_SecondSubmit)
	{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearchClaim";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/OnlineProviderResubmissionClaimsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearchClaim";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/OnlineProviderResubmissionClaimsSearchAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber).

//function to display previous set of pages
function PressBackWard()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/OnlineProviderResubmissionClaimsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/OnlineProviderResubmissionClaimsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()

function onStatusChanged()
{
	document.forms[1].mode.value="doStatusChange";
	document.forms[1].action="/OnlineProviderResubmissionClaimsSearchAction.do";
	document.forms[1].submit();
}//end of onStatusChanged()