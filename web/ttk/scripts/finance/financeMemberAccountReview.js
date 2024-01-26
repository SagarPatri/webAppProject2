function onUserSubmit() {
	if (!JS_SecondSubmit) {	
	 	if(document.forms[1].switchHospOrPtr.value === "MEM"){
	  		trimForm(document.forms[1]);
			document.forms[1].mode.value = "doMemberReviewSave";
			document.forms[1].action = "/CustomerBankDetailsAccountList.do";
			JS_SecondSubmit = true;
			document.forms[1].submit();
	  	}		
	}
}


function onCloseReview()
{  
    	document.forms[1].mode.value="doViewMemberReviewList";
    	document.forms[1].action="/CustomerBankDetailsAccountList.do";
    	document.forms[1].submit();
}//end of onChangeState()






function onCloseRevisedReview()
{  
	
	    var reforward = "cancel";
    	document.forms[1].mode.value="doViewMemberReviewList";
    	document.forms[1].action="/CustomerBankDetailsAccountList.do?reforward="+reforward;
    	document.forms[1].submit();
}//end of onChangeState()





