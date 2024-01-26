function onTOBUpload(){
	
		if(document.forms[1].file.value==""){
			alert("Please select a file to Upload");
			document.forms[1].file.focus();
			return false;
		}
		document.forms[1].mode.value="doUploadSave";
	   	document.forms[1].action="/TOBUploadSave.do";
	   	document.forms[1].submit();
	
}

function onClose()
{   
    document.forms[1].mode.value="doClose";
    document.forms[1].action = "/TOBUpload.do";
    document.forms[1].submit();
}//end of onClose()


function onViewDocument()
{
	var openPage = "/TOBUploadSave.do?mode=doViewUploadDocs";
	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	/*document.forms[1].mode.value="getStreamInfo";
   	document.forms[1].action="/PdfFileDownloadAction.do";
   	document.forms[1].submit();
   	alert("End");*/
   	window.open(openPage,'',features);
}

function onDelDoc()
{
	document.forms[1].mode.value="doRemoveDoc";
	document.forms[1].action="/TOBUploadSave.do";
    document.forms[1].submit();
}