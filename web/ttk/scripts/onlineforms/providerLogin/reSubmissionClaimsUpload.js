
function startClaimUpload(){
		var batchRefNo =  document.forms[1].hiddenBatchRefNo.value;
		if(batchRefNo != ""){
			alert("Batch Reference Number is "+batchRefNo);
			if(!JS_SecondSubmit){
				document.forms[1].mode.value="doCreateClaimsFromUploadedFile";
				document.forms[1].action="/OnlineProviderReSubmissionClaimsAction.do";	
				// document.forms[1].Button.innerHTML= 'Please wait..';
				document.getElementById("Button").innerHTML	= 'Please wait..';
				document.getElementById("Button").disabled = true;
				JS_SecondSubmit=true;
				document.forms[1].submit();
		}
	}
}


function onUpload()
{
	if(!JS_SecondSubmit)
	{
		 if(document.forms[1].file.value==""){
			 alert("Please select a file to Upload");
			 document.forms[1].file.focus();
			 return false;
		 }
		document.forms[1].mode.value="doUploadClaims";
	   	document.forms[1].action="/OnlineProviderReSubmissionClaimsAction.do";
	   	JS_SecondSubmit=true;
	   	
	 	document.forms[1].Button.value = 'Please wait..';
	   	document.getElementById("Button").style.background='red';
	   	document.getElementById("Button").disabled = true;
	   	document.forms[1].submit();
	}
}

function onDownloadClmLog(){
	  
  	document.forms[0].leftlink.value="Provider Claims";
    document.forms[0].sublink.value="Claims Error Log Search";
    document.forms[0].tab.value ="Error Log";
    document.forms[0].mode.value = "doSearch";
    document.forms[0].action = "/ProviderReSubmissionClaimViewLogListAction.do";
    document.forms[0].submit();
}

/*function onDownloadClmLog(){
	var parameter = "?mode=doDownloadClmLog&batchNo="+document.forms[1].batchNo.value;
    var openPage = "/ViewOnlineClaimDetails.do"+parameter;
    var w = screen.availWidth - 10;
    var h = screen.availHeight - 49;
    var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
    window.open(openPage,'',features);
}*/
function onDownloadClmTemplate(){
	var flagYN=document.forms[1].flagYN.value;
	var parameter = "?mode=doDownloadClmTemplate&flagYN="+flagYN;
    var openPage = "/ViewOnlineClaimDetails.do"+parameter;
    var w = screen.availWidth - 10;
    var h = screen.availHeight - 49;
    var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
    window.open(openPage,'',features);
}

