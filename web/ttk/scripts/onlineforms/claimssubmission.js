function onBack()
{
	document.forms[1].mode.value="doClaimsLog";
   	document.forms[1].action="/OnlineProviderClaimsSearchAction.do";
   	document.forms[1].submit();
}

function onViewShortfall(obj)
{
	document.forms[1].mode.value="doViewShortfall";
   	document.forms[1].action="/OnlineProviderClaimsShortfallAction.do?shortfallSeqId="+obj;
   	document.forms[1].submit();
}

function onUserSubmit()
{
	var fileName=document.forms[1].file.value;
	var extension=fileName.substring((fileName.lastIndexOf(".")+1),fileName.length).toLowerCase();
	if(extension==''||(extension!="pdf"&&extension!="png"&&extension!="jpeg"&&extension!="jpg")){
		if(extension=='')
		alert("Please select a file to Upload");
		else
			alert("Please select a proper file to Upload");
		document.forms[1].file.focus();
		return false;
	}
	else if (document.forms[1].requestedAmount.value=="") {
		alert("Requested Amount is required");
		document.forms[1].requestedAmount.focus();
		return false;
	}
	else if (document.forms[1].invoiceno.value=="") {
		alert("Invoice Number is required.");
		document.forms[1].invoiceno.focus();
		return false;
	}
	if(document.forms[1].loginType.value=="EMPL"){
		document.forms[1].mode.value="doSubmitFile";
	   	document.forms[1].action="/EmplClaimSubmissionAction.do";
	}else{
		document.forms[1].mode.value="doSubmitFile";
	   	document.forms[1].action="/EmpClaimSubmissionAction.do";	
	}
	
   	document.forms[1].submit();
}
function onClose()
{
	document.forms[1].mode.value="doClose";
    document.forms[1].action = "/EmpClaimSubmissionAction.do";
    document.forms[1].submit();
}//end of onClose()

function onGetDownloadForm(){
	document.forms[1].mode.value="doDefaultHelpDocs";
    document.forms[1].action = "/EmployeeClaimSubmissionAction.do";
    document.forms[1].submit();
}

function onEditContactInfo(){
	document.forms[1].mode.value="doEditable";
    document.forms[1].action = "/EmployeeClaimSubmissionAction.do?editable=ContactInfo";
    document.forms[1].submit();
}

function onEditAcctInfo(){
	document.forms[1].mode.value="doEditable";
    document.forms[1].action = "/EmployeeClaimSubmissionAction.do?editable=AccountInfo";
    document.forms[1].submit();
}

function onSaveAccountInfo(){
	
	
	
	 var ibanNum = document.forms[1].bankAccNbr.value;
		
		if(ibanNum.indexOf(' ') >= 0){
			alert("Plase remove the space");
			document.getElementById("bankAccNbr").focus();
			 return false;
		}
		
  	var bankLocation = document.forms[1].bankAccountQatarYN.value;
  	if(ibanNum !=""){
 	 if(bankLocation=="Y"&& ibanNum.length != 29){
		alert("IBAN Number should be 29 characters"); 
		document.getElementById("bankAccNbr").focus();
		 return false;
	 }
  	} 
	
	
	
	document.forms[1].mode.value="doSaveAccountInfo";
    document.forms[1].action = "/EmployeeSaveAccountInfo.do?editable="+document.forms[1].editable.value;
    document.forms[1].submit();
	
}

function onNewClose(){
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineMemberAction.do";
	document.forms[1].submit();
}

function onChangeBank(focusid)
{
    	document.forms[1].mode.value="doChangeBank";
    //	document.forms[1].focusID.value=bankState;   
    	document.forms[1].bankState.focus();
    	//document.forms[1].bankname.disabled = true;
    	document.forms[1].bankState.value="";  
    	document.forms[1].bankcity.value="";  
    	document.forms[1].bankBranch.value="";  
    	document.forms[1].action="/EmployeeClaimSubmissionAction.do?focusID="+focusid;
    	document.forms[1].submit();
}//end of onChangeBank()

function onChangeState(focusid)
{
	    document.forms[1].mode.value="doChangeState";
	    document.forms[1].focusID.value="bankcity";
	    document.forms[1].bankcity.focus();
	    document.forms[1].bankcity.value="";
	    document.forms[1].bankBranch.value="";  
    	document.forms[1].action="/EmployeeClaimSubmissionAction.do?focusID="+focusid;
    	document.forms[1].submit();
}//end of onChangeState()

function onChangeCity(focusid)
{

	    document.forms[1].mode.value="doChangeCity";
	    document.forms[1].focusID.value="bankBranch";
	    document.forms[1].bankBranch.focus();
	    document.forms[1].bankBranch.value="";
    	document.forms[1].action="/EmployeeClaimSubmissionAction.do?focusID="+focusid;
    	document.forms[1].submit();
}//end of onChangeCity()


/*function getFileInfo(object){
	alert("Id is::"+object.id);
	var idVar=object.id;
	var id=idVar.substr(idVar.length -2);
	var file=object.value;
	var a =0;
	if(id=='08')
		a=9;
	else if(id<10)
		a=parseInt(id)+1;
	var obj=document.getElementById("file0"+a);
	var extension=file.split('.').pop().toLowerCase();
	if(extension=="pdf"||extension=="png"||extension=="jpeg"){
		obj.style.display="block";
	}
	else{
		alert("Please select Proper file.");
	}
}*/

function getFileInfo(object){
	var idVar=object.id;
	var id=idVar.substr(idVar.length -2);
	var file=object.value;
	var a =0;
//	if(id=='08')
//		a=9;
	if(id<5)
		a=parseInt(id)+1;
	var obj=document.getElementById("file0"+a);
	var extension=file.substring((file.lastIndexOf(".")+1),file.length).toLowerCase();
	if(extension=="pdf"||extension=="png"||extension=="jpeg"||extension=="jpg"){
		obj.style.display="block";
	}
	else{
		object.focus();
		alert("Please select Proper file.");
		return false;
		
	}
}

function onChangeQatarYN()
{
    document.forms[1].mode.value="doChangeQatarYN";
	document.forms[1].action="/EmployeeClaimSubmissionAction.do";
	document.forms[1].submit();
	
}

function zeroVal(Obj)
{	
	var reqAmt = Obj.value;
	var reqAmtVal = reqAmt.charAt(0);		
	if(reqAmtVal === '0')
	{ 		
		alert("Requested Amount must be greater then Zero.");
		Obj.value="";
		Obj.focus();
		return;
	}
}