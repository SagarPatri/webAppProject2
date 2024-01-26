//this function is called onclick of the close button
function onClose()
{
	if(!TrackChanges()) return false;//end of if(!TrackChanges())
    document.forms[1].mode.value="doUploadClose";
    document.forms[1].action="/DocsUpload.do?typeId="+"PAT";
    document.forms[1].submit();
}//end of onClose()

//this function is called onclick of the save button
function doUpload()
{	
	  if(!JS_SecondSubmit)
	  {
		trimForm(document.forms[1]);	
		document.forms[1].mode.value="preauthDocUploads";
		document.forms[1].action = "/DocsUploadList.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
		
		
	}//end of if(!JS_SecondSubmit)
}//end of onSave()

//this function is called onclick of the delete button
function onDelete()
{
	if(!mChkboxValidation(document.forms[1]))
    {
		var msg = confirm("Are you sure you want to delete the Information ?");
		if(msg)
		{
		    document.forms[1].mode.value = "doDelete";
		    document.forms[1].action = "/DocsUpload.do";
		    document.forms[1].submit();
		}//end of if(msg)
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of onDelete()

//this function is called on click of the link in grid to display the Files Upload
function edit(rownum)
{
	//document.forms[1].fileName.value = strFileName;
	var openPage = "/ReportsAction.do?mode=doViewUploadFiles&module=mou&rownum="+rownum;
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}//end of edit(rownum)
