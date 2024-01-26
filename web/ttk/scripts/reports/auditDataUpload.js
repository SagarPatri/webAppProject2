function onUpLoadFiles(){
	
	
	 document.forms[1].mode.value="doAuditUploadFiles";
	  document.forms[1].action="/AuditDataUploadAction.do";	
	  // document.forms[1].Button.innerHTML= 'Please wait..';
	  document.getElementById("Button").innerHTML	=	'Please wait..';
	  document.forms[1].Button.disabled = true;
	  JS_SecondSubmit=true;
	  document.forms[1].submit();
	
}

function onDownloadFile(){
		  document.forms[1].mode.value="doDownloadAuditSampleFile";
		  document.forms[1].action="/AuditDataUploadAction.do";	
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
}


function getClaimUploadCount(){
	var batchRefNo =  document.forms[1].hiddenBatchRefNo.value;
	if(batchRefNo != ""){
		alert("Batch Reference Number is "+batchRefNo);
		if(!JS_SecondSubmit){
		document.forms[1].mode.value="doCreateClaimsFromUploadedFile";
		document.forms[1].action="/ClaimUploadActionSave.do";	
		// document.forms[1].Button.innerHTML= 'Please wait..';
		document.getElementById("Button").innerHTML	=	'Please wait..';
		document.forms[1].Button.disabled = true;
		JS_SecondSubmit=true;
		document.forms[1].submit();
		}
	}
}









