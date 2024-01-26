//java script for the log screen .

//function to sort based on the link selected
function toggle(sortid)
{
	document.forms[1].mode.value="doSearchChequeInfoLog";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/ChequeDetailsAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

function onChequeInfoLogSearch()
{
	if(!JS_SecondSubmit)
	 {
	    //	var selectedData=document.getElementById("logTypeId").value;
		    document.forms[1].mode.value = "doSearchChequeInfoLog";
		    document.forms[1].action = "/ChequeDetailsAction.do";
			JS_SecondSubmit=true;
		    document.forms[1].submit();
		
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function onSave()
{
	
	var flag = document.forms[1].logFlag.value;
	document.forms[1].logDesc.value=trim(document.forms[1].logDesc.value);
	if(document.forms[1].logDesc.value.length==0)
	{
		alert("Please Enter the Log Information");
		document.forms[1].logDesc.focus();
		return false;
	}//end of if(document.forms[1].logDesc.value.length==0)
	if(document.forms[1].logDesc.value.length >750)
	{
		alert("Entered data should not be more than 750 characters");
		document.forms[1].logDesc.focus();
		return false;
	}//end of if(document.forms[1].logDesc.value.length >250)
	if(flag=="N")
	{
		alert("Please Search logs once before Save to list");
		document.forms[1].logDesc.focus();
		return false;
	}
	
	
	if(!JS_SecondSubmit)
	{
		document.forms[1].mode.value = "doSaveChequeInfoLog";
	    document.forms[1].action = "/ChequeDetailsAction.do";
	    JS_SecondSubmit=true;
	    document.forms[1].submit();
    }//end of if(!JS_SecondSubmit)
}//end of onSave()