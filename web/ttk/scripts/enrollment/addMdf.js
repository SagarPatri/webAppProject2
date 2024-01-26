//this function is called onclick of Upload button
function onUpload()
{ 
	if(document.forms[1].enrollmentId.value==""){
		alert("Please Select the Member");
		document.forms[1].enrollmentId.focus();
		return false;
	}if(document.forms[1].files.value==""){
		alert("Please Select the file");
		document.forms[1].files.focus();
		return false;
	}
	
	if(!JS_SecondSubmit)
	{
		trimForm(document.forms[1]);
		document.forms[1].mode.value="doUploadFiles";
		document.forms[1].action = "/AddMdfMemberAction.do";
		JS_SecondSubmit=true
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onUpload()


//this function is called onclick of delete button to delete uploaded files
function onDeleteMdfFile(obj,obj2)
{
	if(!JS_SecondSubmit)
	{
		document.forms[1].mode.value="doDeleteMdfFile";
		document.forms[1].action = "/DeleteMdfMemberAction.do?memSeqId="+obj+"&mdfSeqId="+obj2;
		JS_SecondSubmit=true
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onUpload()

function viewMdfFile(obj)
{
	/*document.forms[1].mode.value="doViewMdfFile";
	document.forms[1].action = "/AddMdfMemberAction.do?fileName="+obj;
	JS_SecondSubmit=true;
	document.forms[1].submit();*/
	
	var openPage = "/AddMdfMemberAction.do?mode=doViewMdfFile&fileName="+obj;
	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   	window.open(openPage,'',features);
}

function onClose()
{
	if(!JS_SecondSubmit)
	 {
		if(!TrackChanges()) return false;
		document.forms[1].mode.value="doCloseMember";
		document.forms[1].action="/MemberAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}