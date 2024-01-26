function onSearch()
{
   if(!JS_SecondSubmit)
 {
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/CounterFraudSearchAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/CounterFraudSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}

function changeclaimtopreauth(){
	document.forms[1].mode.value = "doStatusChange";
	document.forms[1].action = "/CounterFraudSearchAction.do";
	document.forms[1].submit();
}

function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/CounterFraudSearchAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()

function edit(rownum){
    document.forms[1].mode.value="doView";
    document.forms[1].rownum.value=rownum;
    if(!JS_SecondSubmit){
    document.forms[1].tab.value ="General";
    document.forms[1].action = "/CounterFraudSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
 }//end of edit(rownum)

function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/CounterFraudSearchAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/CounterFraudSearchAction.do";
    document.forms[1].submit();
}//end of PressForward()