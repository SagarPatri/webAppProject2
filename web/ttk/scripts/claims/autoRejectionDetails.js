
function selectEnrollmentID()
{

document.forms[1].mode.value="doSelectEnrollmentID";
document.forms[1].action="/ClaimAutoRejectionGeneralAction.do";
document.forms[1].submit();

}





function onClaimEdit()
{
document.forms[1].mode.value="doEdit";
document.forms[1].action="/ClaimAutoRejectionGeneralAction.do";
document.forms[1].submit();

}
function onSave()
{
	
	var alkootId=document.forms[1].alkootId.value;
	var previousClaimNo=document.forms[1].previousClaimNo.value;
	var memberName=document.forms[1].memberName.value;
	var claimAction=document.forms[1].claimAction.value;
	
	if(alkootId==""||alkootId==null){
		alert("Please Enter Alkoot ID");
		 document.forms[1].alkootId.focus();
		return false;
	}
	if(previousClaimNo==""||previousClaimNo==null){
		alert("Please Enter Parent Claim No.");
		 document.forms[1].previousClaimNo.focus();
		return false;
	}
	if(memberName==""||memberName==null){
		alert("Please Enter Member Name");
		 document.forms[1].memberName.focus();
		return false;
	}
	if(claimAction=="REJA"){
		var rejectionReason=document.forms[1].rejectionReason.value;
	if(rejectionReason==""||rejectionReason==null){
		alert("Please Enter Rejection Reason");
		 document.forms[1].rejectionReason.focus();
		return false;
	}	
	}
	
document.forms[1].mode.value="doSave";
document.forms[1].action="/ClaimAutoRejectionGeneralAction.do";
document.forms[1].submit();

}



function onGeneralClose()
{

document.forms[1].mode.value="doClose";
document.forms[1].action="/ClaimAutoRejectionAction.do";
document.forms[1].submit();

}








function onActivitySave()
{

	
	var internalServiceCode=document.forms[1].internalServiceCode.value;
	var serviceDate=document.forms[1].serviceDate.value;
	var parentClaimSetlmntNo=document.forms[1].parentClaimSetlmntNo.value;
	var serviceDescription=document.forms[1].serviceDescription.value;
	var activityType=document.forms[1].activityType.value;
	var alkootRemarks=document.forms[1].alkootRemarks.value;
	
	
	if(internalServiceCode==""||internalServiceCode==null){
		alert("Please Enter Internal Service Code");
		 document.forms[1].internalServiceCode.focus();
		return false;
	}
	if(serviceDate==""||serviceDate==null){
		alert("Please Enter Service Date");
		 document.forms[1].serviceDate.focus();
		return false;
	}
	if(parentClaimSetlmntNo==""||parentClaimSetlmntNo==null){
		alert("Please Enter Parent Claim Settlement NO.");
		 document.forms[1].parentClaimSetlmntNo.focus();
		return false;
	}
	if(serviceDescription==""||serviceDescription==null){
		alert("Please Enter Service Description");
		 document.forms[1].serviceDescription.focus();
		return false;
	}
	if(activityType==""||activityType==null){
		alert("Please Enter Activity Type");
		 document.forms[1].activityType.focus();
		return false;
	}
	if(alkootRemarks==""||alkootRemarks==null){
		alert("Please Enter AlKoot Remarks");
		 document.forms[1].alkootRemarks.focus();
		return false;
	}
	
document.forms[1].mode.value="doSave";
document.forms[1].action="/ClaimAutoRejectionActivityDetails.do";
document.forms[1].submit();

}


function onActivityClose()
{

document.forms[1].mode.value="doActivityClose";
document.forms[1].action="/ClaimAutoRejectionActivityDetails.do";
document.forms[1].submit();

}







function onReEvaluate()
{

document.forms[1].mode.value="doReEvaluate";
document.forms[1].action="/ClaimAutoRejectionGeneralAction.do";
document.forms[1].submit();

}

function edit(internalcode,parentSetlmntNo,serviceDate,ActivityType,serviceDesc,cptCode,reSubReqAmnt,quantity,toothNo,alkootRemarks,reSubJustification,errorLog,xmlseqid,cmlautoRejectSeqId,resubinterseqid,hosp_seq_id){	
	
	
	
	var parameterValue=internalcode+"|"+parentSetlmntNo+"|"+serviceDate+"|"+ActivityType+"|"+serviceDesc+"|"+cptCode+"|"+reSubReqAmnt+"|"+quantity+"|"+toothNo+"|"+alkootRemarks+"|"+reSubJustification+"|"+errorLog+"|"+xmlseqid+"|"+cmlautoRejectSeqId+"|"+resubinterseqid+"|"+hosp_seq_id+"|";                                                          
	//document.forms[1].mode.value="doEditActivity";
	var partmeter = "?mode=doEditActivity&parameter="+parameterValue;
	document.forms[1].action="/ClaimAutoRejectionActivityDetails.do"+partmeter;
	document.forms[1].submit();
	
	//var partmeter = "?mode=doEditActivity&parameter="+parameterValue;
   	//var openPage = "/ClaimDetailedReports.do"+partmeter;
	
	
}


function onActionChange(){
	
	document.forms[1].mode.value="doChangeAction";
	document.forms[1].action="/ClaimAutoRejectionGeneralAction.do";
	document.forms[1].submit();
	
}
