//functions for errorLoglist screen 
//function to display the selected page

function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ProviderClaimViewLogListAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ProviderClaimViewLogListAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ProviderClaimViewLogListAction.do";
    document.forms[1].submit();
}//end of PressForward()

function onSearch()
{
   if(!JS_SecondSubmit)
 {
	trimForm(document.forms[1]);
	 var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	 var formDate=document.forms[1].formDate.value;
	 var toDate = document.forms[1].toDate.value;
	if (formDate.length>1 && !pattern.test(formDate)){
  alert("Date format should be (dd/mm/yyyy)");
  return;
 }
	if (toDate.length>1 && !pattern.test(toDate)){
		alert("Date format should be (dd/mm/yyyy)");
		return;
	}
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/ProviderClaimViewLogListAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
  //}//end of if(!JS_SecondSubmit)
 }//end of onSearch()
}
function edit(rownum)
{
    var openPage = "/ProviderClaimViewLogListAction.do?mode=doViewErrorLog&reportType=EXCEL&rownum="+rownum;
    var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}//end of edit(rownum)