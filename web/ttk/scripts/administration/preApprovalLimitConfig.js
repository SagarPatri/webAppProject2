

function preApprovalLimitVIP(checkObj,styleID) {	
	//var =document.getElementById("preApprovalLimitYN");
	if(checkObj.checked){
       document.getElementById(styleID).style.display="";
       document.forms[1].preApprovalAmountVIP.value="";
		}else{
			document.getElementById(styleID).style.display="none";
			document.forms[1].preApprovalAmountVIP.value="";
		}
}
function preApprovalLimitNONVIP(checkObj,styleID) {	
	//var =document.getElementById("preApprovalLimitYN");
	if(checkObj.checked){
       document.getElementById(styleID).style.display="";
       document.forms[1].preApprovalAmountNONVIP.value="";
		}else{
			document.getElementById(styleID).style.display="none";
			document.forms[1].preApprovalAmountNONVIP.value="";
		}
}

function onSave(){
	
	var checkObjVIP=document.getElementById("VIPpreApprovalLimitYN");
	var checkObjNONVIP=document.getElementById("NONVIPpreApprovalLimitYN");
	var checkObjManVip=document.getElementById("MandatoryServiceVIP");
	var checkObjManNonVip=document.getElementById("MandatoryServiceNONVIP");
	
	if(checkObjVIP.checked){
		document.forms[1].VIPpreApprovalLimitYN.value="Y";
		var paaVIPAmt=document.forms[1].preApprovalAmountVIP.value;
		if(paaVIPAmt==null||paaVIPAmt==""||paaVIPAmt===""||paaVIPAmt.length<1){
			alert("Please Enter VIP Amount");
			return;
		}	
	}
	else{
		document.forms[1].VIPpreApprovalLimitYN.value="N";
		document.forms[1].preApprovalAmountVIP.value="";		
	}
	
	
	if(checkObjNONVIP.checked){
		document.forms[1].NONVIPpreApprovalLimitYN.value="Y";
		var paaNONVIPAmt=document.forms[1].preApprovalAmountNONVIP.value;
		if(paaNONVIPAmt==null||paaNONVIPAmt==""||paaNONVIPAmt===""||paaNONVIPAmt.length<1){
			alert("Please Enter NON-VIP Amount");
			return;
		}	
	}
	else{
		document.forms[1].NONVIPpreApprovalLimitYN.value="N";
		document.forms[1].preApprovalAmountNONVIP.value="";
	}
	
	
	if(checkObjManVip.checked) document.forms[1].MandatoryServiceVIP.value="Y";
	else{
		document.forms[1].MandatoryServiceVIP.value="N";		
	}
	
	if(checkObjManNonVip.checked) document.forms[1].MandatoryServiceNONVIP.value="Y";
	else{
		document.forms[1].MandatoryServiceNONVIP.value="N";		
	}
	

	document.forms[1].mode.value="doSavePreApprovalLimit";
	document.forms[1].action="/PreApprovalLimitConfiguration.do";
	document.forms[1].submit();
}//end of onSave()


function onClose()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value = "doClose";
	document.forms[1].action = "/InsuranceApproveConfiguration.do";
	document.forms[1].submit();	
}//end of onClose() 