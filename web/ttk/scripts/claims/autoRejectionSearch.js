function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ClaimAutoRejectionAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}// end of pageIndex(pagenumber)

function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    if(!JS_SecondSubmit){
    document.forms[1].action = "/ClaimAutoRejectionAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}//end of toggle(sortid)

// function to display previous set of pages
function PressBackWard()
{	
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ClaimAutoRejectionAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
    
}// end of PressBackWard()

// function to display next set of pages
function PressForward()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ClaimAutoRejectionAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}// end of PressForward()

function onSearch(element)
{
   if(!JS_SecondSubmit)
 {
	trimForm(document.forms[1]);
	 var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	 var fromDate=document.forms[1].formDate.value;
	 var toDate=document.forms[1].toDate.value;
	if (fromDate.length>1 && !pattern.test(fromDate)){
		alert("'From Date' format should be (dd/mm/yyyy)");
		return;
 }
	if (toDate.length>1 && !pattern.test(toDate)){
		alert("'To Date' format should be (dd/mm/yyyy)");
		return;
	}
	
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/ClaimAutoRejectionAction.do";
	JS_SecondSubmit=true;
	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	document.forms[1].submit();
  // }//end of if(!JS_SecondSubmit)
 }// end of onSearch()
}


function edit(rownum){
	if(!JS_SecondSubmit)
	 {  
	    	document.forms[1].mode.value="doView";
	    	document.forms[1].rownum.value=rownum;
	    	//document.forms[1].tab.value ="System Preauth Approval";
	    	document.forms[1].action = "/ClaimAutoRejectionAction.do";
	    	JS_SecondSubmit=true;
	    	document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}//end of edit(rownum)






function onConfiguration()
{
	document.forms[1].mode.value="doConfiguration";
	document.forms[1].action="/ClaimAutoRejectionAction.do";
	document.forms[1].submit();
}//end of onConfiguration()


function setProviderID()
{
	document.forms[1].mode.value="setProviderID";
	document.forms[1].action="/ClaimAutoRejectionAction.do";
	document.forms[1].submit();
}