// Java script function for notificationdetails.jsp

//function is called onclick of save button
function onSubmit()
{
	trimForm(document.forms[1]);
	if(!JS_SecondSubmit) 
	{
    	document.forms[1].mode.value="doSave";
    	document.forms[1].action="/SaveNotificationAction.do";
    	JS_SecondSubmit=true
    	document.forms[1].submit();  
    }//end of if(!JS_SecondSubmit)	  
}//end of onSubmit()

//function is called onclick of reset button
function onReset()
{	
    if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
	{
	    document.forms[1].mode.value="doReset";
	    document.forms[1].action = "/EditNotificationAction.do";
	    document.forms[1].submit();	
	}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
	else
	{
		document.forms[1].reset();
	}//end of else
}//end of onReset()

//function is called onclick of close button
function onClose()
{
	if(!TrackChanges()) return false;

    document.forms[1].mode.value="doClose";
    document.forms[1].action = "/EditNotificationAction.do";
    document.forms[1].submit();
}//end of onClose()

function onConfigurationInfo()
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].Entry.value="Y";
	document.forms[1].action="/ConfigurationDetailsAction.do";
	document.forms[1].submit();
}//end of onConfigurationInfo()

function onPreview(){
	
	var widow = window.open("","wildebeast","width=700,height=600,top=100,left=300,location=1,status=1,scrollbars=1,resizable=1");
//	var emailDesc	=	document.forms[1].emailDesc.value;
	  var emailDesc = document.getElementById("emailDesc").value;
	
	widow.document.open();
	widow.document.write(emailDesc);
	widow.document.close();
}