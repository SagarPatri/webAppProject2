function onSwitch()
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/CollectionsSearchAction.do";
	document.forms[1].submit(); 
}

function goToCollections()
{
	document.forms[1].tab.value="Collections";
	document.forms[1].mode.value="goToCollections";
	document.forms[1].action="/CollectionsSearchAction.do";
	document.forms[1].submit(); 
}

function onSearch()
{
	//document.forms[1].tab.value="Collections";
	var swithto  = document.forms[1].switchTo.value;
	if(swithto=="COR"){
		document.forms[1].reforward.value="search";
		document.forms[1].mode.value="doSearch";
		document.forms[1].action="/CollectionsSearchAction.do";
		document.forms[1].submit(); 
	}
	
}

function edit(rownum)
{
	document.forms[1].reforward.value="edit";
	document.forms[1].mode.value="doViewPolicy";
	document.forms[1].rownum.value=rownum;
	document.forms[1].tab.value="Premium Distribution";
	document.forms[1].action="/CollectionsSearchAction.do";
	document.forms[1].submit();
}


function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    if(!JS_SecondSubmit){
    document.forms[1].action = "/CollectionsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}

function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/CollectionsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}

function PressBackWard()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/CollectionsSearchAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}

function PressForward()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/CollectionsSearchAction.do";
	 JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}



function onExport()
{
	
	
	 var partmeter = "?mode=doExportReport&reportType=EXCEL";
   // var partmeter = "?mode=doExportReport";
    var openPage = "/CollectionsSearchAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}




