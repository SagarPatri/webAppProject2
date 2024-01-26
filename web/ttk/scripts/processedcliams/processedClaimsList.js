function onSearch(element)
{
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/ProcessedClaimsSearchAction.do";
	JS_SecondSubmit=true;
	element.innerHTML	=	"<b>"+ "Please Wait...!!"+"</b>";
	document.forms[1].submit();
	
}

function edit(rownum){
    document.forms[1].mode.value="doView";
    document.forms[1].rownum.value=rownum;
    if(!JS_SecondSubmit){
    document.forms[1].tab.value ="General";
    document.forms[1].action = "/ProcessedClaimsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}//end of edit(rownum)




function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    if(!JS_SecondSubmit){
    document.forms[1].action = "/ProcessedClaimsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}//end of toggle(sortid)

function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ProcessedClaimsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}


function PressBackWard()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ProcessedClaimsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}

function PressForward()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ProcessedClaimsSearchAction.do";
	 JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}



function onClose()
{

	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doDefault";
    document.forms[1].action = "/ProcessedClaimsSearchAction.do";
	 JS_SecondSubmit=true;
    document.forms[1].submit();
	}
	
}

function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/ProcessedClaimsSearchAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}
