//this function is called onclick of the save button
function onSave()
{	
  if(!JS_SecondSubmit)
  {
	trimForm(document.forms[1]);
	
	document.forms[1].mode.value="doSavePolicyDocs";
	document.forms[1].action = "/UploadPolicyDocsSave.do";
	JS_SecondSubmit=true
	document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}//end of onSave()


//this function is called onclick of the close button
function onClose()
{
    document.forms[1].mode.value="doClosePolicyDocs";
    document.forms[1].action="/UploadPolicyDocs.do";
    document.forms[1].submit();
}//end of onClose()


//this function is called on click of the link in grid to display the Files Upload
function edit(rownum)
{
    document.forms[1].action="/ReportsAction.do?rownum="+rownum;
    document.forms[1].mode.value="doPolicyFileDownload";
    document.forms[1].submit();     
}//end of edit(rownum)
function editTestNag(rownum)
{
	//document.forms[1].fileName.value = strFileName;
	var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=policyDocs&rownum="+rownum;
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}//end of edit(rownum)


