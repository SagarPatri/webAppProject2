function edit(rownum)
{
	document.forms[1].reforward.value="pdf";
	document.forms[1].mode.value="downloadInvoice";
	document.forms[1].rownum.value=rownum;
	document.forms[1].tab.value="Premium Distribution";
	document.forms[1].action="/CollectionsSearchAction.do";
	document.forms[1].submit();
}

function edit2(rownum)
{
	document.forms[1].reforward.value="xls";
	document.forms[1].mode.value="downloadInvoice";
	document.forms[1].rownum.value=rownum;
	document.forms[1].tab.value="Premium Distribution";
	document.forms[1].action="/CollectionsSearchAction.do";
	document.forms[1].submit();
}

function edit3(rownum)
{
	document.forms[1].mode.value="addCollection";
	document.forms[1].rownum.value=rownum;
	document.forms[1].tab.value="Collections";
	document.forms[1].action="/CollectionsAction.do";
	document.forms[1].submit();
}

function edit4(rownum)
{
	document.forms[1].reforward.value="reverse";
	document.forms[1].mode.value="reverseCollection";
	document.forms[1].rownum.value=rownum;
	document.forms[1].tab.value="Collections";
	document.forms[1].action="/CollectionsAction.do";
	document.forms[1].submit();
}



function onDownloadCollection()
{
	
	 var partmeter = "?mode=doDownloadCollections&reportType=EXCEL";
	   // var partmeter = "?mode=doExportReport";
	    var openPage = "/CollectionsSearchAction.do"+partmeter;
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 99;
		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open(openPage,'',features);
	
}

function closePremiumDistribution()
{
	
	document.forms[1].mode.value="doDefault";
	//document.forms[1].rownum.value=rownum;
	document.forms[1].tab.value="Search";
	document.forms[1].action="/CollectionsSearchAction.do";
	document.forms[1].submit();
	
}






