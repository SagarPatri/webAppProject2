//functions for claimslist screen of claims flow.

function startClaimUpload(){
	var batchRefNo =  document.forms[1].hiddenBatchRefNo.value;
	if(batchRefNo != ""){
		alert("Batch Reference Number is "+batchRefNo);
		if(!JS_SecondSubmit){
			if(document.forms[1].claimsSubmissionTypes.value=='DTC'){
	  			document.forms[1].mode.value="doCreateClaimsFromUploadedFile";
	  		}else if(document.forms[1].claimsSubmissionTypes.value=='DTR'){
	  			document.forms[1].mode.value="doCreateClaimsReSubFile";
	  		}
		document.forms[1].action="/ClaimUploadActionSave.do";	
		// document.forms[1].Button.innerHTML= 'Please wait..';
		document.getElementById("Button").innerHTML	=	'Please wait..';
		document.forms[1].Button.disabled = true;
		JS_SecondSubmit=true;
		document.forms[1].submit();
		}
	}
}


function onClaimUpload(){
	if(!JS_SecondSubmit){
	document.forms[1].leftlink.value="Claims";
    document.forms[1].sublink.value="BatchEntry";
    document.forms[1].tab.value ="Claim Upload";
    document.forms[1].mode.value = "doClaimUpload";
    document.forms[1].action = "/ClaimUploadAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of function addBatch()

function selectProvider(){
	   
	      
	    // document.forms[1].sublink.value="BatchEntry";
	    
	if(!JS_SecondSubmit){
		  document.forms[1].mode.value="doGeneral";
		  document.forms[1].action="/ClaimUploadAction.do";	
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
	}
}


function onBack(){
	   
	if(!JS_SecondSubmit){
	document.forms[1].tab.value ="Search";
		  document.forms[1].mode.value="doBack";
		  document.forms[1].action="/ClaimUploadAction.do";	
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
	}
}


function onUpLoadFiles(){
	var regexp=/^(([0-9]|[0][0-9]|[0-1][0-2])\:([0-9]|[0-5][0-9]))$/;
	 if(document.forms[1].claimsSubmissionTypes.value.length==0||document.forms[1].claimsSubmissionTypes.value==null||document.forms[1].claimsSubmissionTypes.value=='')
	   {  alert("Please Select Claims Submission Type");
	   		document.forms[1].claimsSubmissionTypes.focus();
	      return;
	   }
	   if(document.forms[1].providerId.value.length==0||document.forms[1].providerId.value==null||document.forms[1].providerId.value=='')
	   {  alert("Please Select Provider ID");
	   		document.forms[1].providerId.focus();
	      return;
	   }
	   if(isDate(document.forms[1].receiveDate,"Received Date")==false)
			return false;
	   if(document.forms[1].receivedTime.value.length==0||document.forms[1].receivedTime.value==null||document.forms[1].receivedTime.value=='')
		   {
		   alert("Please Select Received Time");
	   		document.forms[1].receivedTime.focus();
	      return;
		   }
	  	if(document.forms[1].receivedTime.value!="" && regexp.test(document.forms[1].receivedTime.value)==false)
	   {  alert("Please Enter Valid Received Time");
	   		document.forms[1].receivedTime.focus();
	      return;
	   }
	  	if(!JS_SecondSubmit){
	  		if(document.forms[1].claimsSubmissionTypes.value=='DTC'){
	  			 document.forms[1].mode.value="doUploadFiles";
	  		}else if(document.forms[1].claimsSubmissionTypes.value=='DTR'){
	  			 document.forms[1].mode.value="doUploadReSubmissionFiles";
	  		}
		  document.forms[1].action="/ClaimUploadActionSave.do";	
		  // document.forms[1].Button.innerHTML= 'Please wait..';
		  document.getElementById("Button").innerHTML	=	'Please wait..';
		  document.forms[1].Button.disabled = true;
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
	  	}
}


function onDownloadFile(){
	//if(!JS_SecondSubmit){
		  document.forms[1].mode.value="doDownLoadFiles";
		  document.forms[1].action="/ClaimUploadAction.do";	
		  //JS_SecondSubmit=true;
		  document.forms[1].submit();
//	}
}

function onDownloadClmLog(){
	if(!JS_SecondSubmit){
		  	document.forms[1].leftlink.value="Claims";
		    document.forms[1].sublink.value="BatchEntry";
		    document.forms[1].tab.value ="Error Log";
		    document.forms[1].mode.value = "doSearch";
		    document.forms[1].action = "/ClaimViewLogListAction.do";
			 JS_SecondSubmit=true;
		    document.forms[1].submit();
	}
}
/*function onDownloadClmLog(){
	var parameter = "?mode=doDownloadClmLog&batchNo="+document.forms[1].batchNo.value;
    var openPage = "/ClaimUploadAction.do"+parameter;
    var w = screen.availWidth - 10;
    var h = screen.availHeight - 49;
    var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
    window.open(openPage,'',features);
}*/

function onDownloadReSubFile(){
//	if(!JS_SecondSubmit){
		  document.forms[1].mode.value="doDownLoadReSubFiles";
		  document.forms[1].action="/ClaimUploadAction.do";	
		 // JS_SecondSubmit=true;
		  document.forms[1].submit();
	//}
}