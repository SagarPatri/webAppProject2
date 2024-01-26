//contains the javascript functions of assignto.jsp
function onSave()
{
	trimForm(document.forms[1]);
	if(!JS_SecondSubmit){
		document.forms[1].mode.value="doSave";
		document.forms[1].action="/UpdateAssignToDashBordPreauthAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)	
}//end of onSave()

/*function onCancel()
{
//	if(document.getElementById("strLinkIDForPreAuth").value == "Pre-Authorization"){
	if(document.getElementById("leftlink").value == "Pre-Authorization"){
	    
		var preAuthSeqID = document.getElementById("preAuthSeqID").value;
		viewPreAuthDetails(preAuthSeqID);
		
	}else{
	
	
	if(!TrackChanges()) return false;
 	document.forms[1].mode.value="doClose";
    document.forms[1].action="/AssignToAction.do";
	document.forms[1].submit();
	}
}//end of onCancel
*/

function onReset(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{
			document.forms[1].mode.value="doReset";
			document.forms[1].action="/AssignToDashBordPreauthAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
		}//end of else
	 }
}//end of onReset

//function to Change the Policy Type.
function doSelectUsers()
{
	document.forms[1].mode.value="doChangeUsers";
	document.forms[1].action="/AssignToDashBordPreauthAction.do";
	document.forms[1].submit();
}//end of doSelectUsers()

function onCancel()
{
 	document.forms[1].mode.value="doClose";
    document.forms[1].action="/AssignToDashBordPreauthAction.do";
	document.forms[1].submit();
}