function onSave()
{
	
	document.forms[1].mode.value="doSave";
	document.forms[1].action="/BenefitBufferLimit.do";
	document.forms[1].submit();
	
	
}

function isNumaricOnly(upObj){
	
	var re = /^[0-9]*\.*[0-9]*$/;	
	var objValue=upObj.value;
	if(objValue.length>=1){
		if(!re.test(objValue)){
			alert("Please Enter Numbers only");
			upObj.value="";
			upObj.focus();
		}
	}  
	
}



function onClose()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].action="/BenefitBufferLimit.do";
	document.forms[1].submit();
}//end of onClose()


function onReset()
{
	document.forms[1].mode.value="doReset";
	document.forms[1].action="/BenefitBufferLimit.do";
	document.forms[1].submit();
	
	
}







