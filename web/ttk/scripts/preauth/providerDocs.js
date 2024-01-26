

// JavaScript function for Page Indexing
function doViewProviderDoc(filePath,fileName,mouDocSeqId){
	/*document.forms[1].mode.value="doViewUploadDocs";
	document.forms[1].fileName.value=fileName;	
	document.forms[1].filePath.value=mouDocSeqId;	
	document.forms[1].submit();*/
	var openPage = "/PreAuthGeneralAction.do?mode=doViewUploadDocs&fileName="+fileName+"&filePath="+mouDocSeqId;
  	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  	window.open(openPage,'',features);
  	
}// End of pageIndex()


function onClose()
{
	document.forms[1].mode.value="doClose";	
	document.forms[1].submit();
}//end of onClose()