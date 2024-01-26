//javascript used in batchgeneral.jsp of claim flow

function changenetwork()
{
	
	var networkType=document.forms[1].netWorkType.value;
	var paymentTo=document.forms[1].paymentTo.value;
	var processType = document.forms[1].processType.value;
	
	if(processType == "DBL"){
	if(networkType == "Y" && paymentTo == "PRV" ){
		
	 document.getElementById("providerId").style.display="";
	 document.getElementById("partnerId").style.display="none";
	 document.getElementById("partnerIdLabel").style.display="none";
	}else{
		document.getElementById("providerId").style.display="none";
		 document.getElementById("partnerId").style.display="";
		 document.getElementById("partnerIdLabel").style.display="inline";

	}
	}else if(processType == "RGL"){
		if(networkType == "Y")
			{
			 document.getElementById("providerId").style.display="inline";
		
			}else
		{
			document.getElementById("providerId").style.display="none";
			

		}
	}
	

}


/*function paymentChange(){
	 var paymentTo=	document.getElementById("paymentToId").value;
	 alert("paymentChange");
	 if(paymentTo == "PRV"){
		 document.getElementById("partnerId").style.display="none";
		 document.getElementById("partnerIdLabel").style.display="none";

		 
	 }else{
		 document.getElementById("partnerId").style.display="inline";
		 document.getElementById("partnerIdLabel").style.display="inline";

	 }
	}*/

function onSave(){	
	 trimForm(document.forms[1]);	
	 if(!JS_SecondSubmit)
     {
			var networkType=document.forms[1].netWorkType.value;
			var processType = document.forms[1].processType.value;
   
			if(processType == "DBL"){
				if(networkType == "Y" && document.forms[1].claimType.value == "CNH" && document.forms[1].paymentTo.value == "PTN"){
					if(document.forms[1].partnerName.value == ""){
						alert("Please select Partner Name");
						return false;
						}
					}else if(networkType == "Y" && document.forms[1].paymentTo.value == "PRV" && document.forms[1].claimType.value == "CNH"){
						if(document.forms[1].providerID.value == ""){
							alert("Please select Provider Name");
							return false;

						}
					}
				
				}else if(processType == "RGL"){
					if(document.forms[1].claimType.value == "CNH" && document.forms[1].providerID.value == ""){
						alert("Please select Provider Name");
						return false;
					}
				}
					
				
		 
		var status=document.forms[1].batchStatus.value;
		var completedYN=document.forms[1].completedYN.value;
		if(completedYN==="Y"){
			alert("Sorry!!! This Claim Batch Is Completed.If You Want Changes Click Override Button");return;
		}
		if((status==="COMP")&&(completedYN!="Y")){
			if(!confirm("Do You Want to Complete The Batch?"))return;
		}
	   document.forms[1].mode.value="doSave";
	   document.forms[1].validateYN.value="N";
	   document.forms[1].action="/SaveClaimBatchGeneralAction.do";
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
	
}//end of onSave()
function deleteClaimDetails(claimSeqID1){	
	 if(!JS_SecondSubmit)
	    {
		 var completedYN=document.forms[1].completedYN.value;
			if(completedYN==="Y"){
				alert("Sorry!!! This Claim Batch Is Completed.If You Want Changes Click Override Button");return;
			}
			if(!confirm("Do You Want Delete Claim Details?"))return;
		   document.forms[2].mode.value="deleteClaimDetails";
		   document.forms[2].claimSeqID.value=claimSeqID1;
		   document.forms[2].child.value="DeleteInvoiceNO";
		   document.forms[2].action="/AddClaimDetailsAction.do";
		   JS_SecondSubmit=true;
		   document.forms[2].submit();
		}//end of if(!JS_SecondSubmit)
}//end of deleteClaimDetails()

function override(){
	if(!confirm("Do You Want Override Batch Details?"))return;
	 if(!JS_SecondSubmit)
    {
	   document.forms[1].mode.value="overrideBatchDetails";
	   document.forms[1].overrideYN.value="Y";
	   document.forms[1].child.value="OverrideBatchDetails";
	   document.forms[1].action="/ClaimBatchGeneralAction.do";
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
	
}//end of override()


function setProviderType(){
	 var completedYN=document.forms[1].completedYN.value;
		if(completedYN==="Y"){
			alert("Sorry!!! This Claim Batch Is Completed.If You Want Changes Click Override Button");return;
		}
	   document.forms[1].mode.value="addBatch";
	   document.forms[1].action="/ClaimBatchGeneralAction.do?initialize=NO";
	   document.forms[1].submit();
}//end of setProviderType()
function setNetWorkType(){
	 var completedYN=document.forms[1].completedYN.value;
		if(completedYN==="Y"){
			alert("Sorry!!! This Claim Batch Is Completed.If You Want Changes Click Override Button");return;
		}
	   document.forms[1].mode.value="setNetWorkType";
	   document.forms[1].action="/ClaimBatchGeneralAction.do";
	   document.forms[1].submit();
}//end of setNetWorkType()
function setProviderID(){
	   document.forms[1].providerLisenceNO.value= document.forms[1].providerID.value;
}//end of setProviderID()
function addClaimDetails(){	
	 trimForm(document.forms[2]);
	 if(!JS_SecondSubmit){
		 var completedYN=document.forms[1].completedYN.value;
		 var submissionCat=document.forms[1].processType.value;
		 var submissionType=document.forms[1].submissionType.value;
		 if(submissionType==="DTC"){
			 var descriptionVal=document.forms[2].description.value;
			 if(submissionCat==="DBL" && descriptionVal==""){
				 alert("Please select the Description !!");
				 document.forms[2].description.focus();
				 return false;
			 }
			 if(submissionCat==="DBL" && document.forms[2].file.value==""){
				 alert("Please select a file to Upload");
				 document.forms[2].file.focus();
				 return false;
			 }
		 }
			if(completedYN==="Y"){
				alert("Sorry!!! This Claim Batch Is Completed.If You Want Changes Click Override Button");return;
			}
	   document.forms[2].batchSeqID.value=document.forms[1].batchSeqID.value;
	   document.forms[2].batchReceiveDate.value=document.forms[1].batchReceiveDate.value;
	   document.forms[2].receivedTime.value=document.forms[1].receivedTime.value;
	   document.forms[2].receiveDay.value=document.forms[1].receiveDay.value;
	   document.forms[2].claimType.value=document.forms[1].claimType.value;	  
	   document.forms[2].providerName.value=document.forms[1].providerName.value;
	   document.forms[2].providerID.value=document.forms[1].providerID.value;
	   document.forms[2].providerLisenceNO.value=document.forms[1].providerLisenceNO.value;
	   document.forms[2].mode.value="addClaimDetails";
	   document.forms[2].action="/SaveAddClaimDetailsAction.do";
	   JS_SecondSubmit=true;
	   document.forms[2].submit();
	}//end of if(!JS_SecondSubmit)
	
}//end of addClaimDetails()
function selectEnrollmentID()
{
	if(!JS_SecondSubmit){
	document.forms[2].mode.value="doSelectEnrollmentID";
	document.forms[2].child.value="EnrollmentList";
	document.forms[2].action="/ClaimBatchGeneralAction.do";
	JS_SecondSubmit=true;
	document.forms[2].submit();
	}
}//selectEnrollmentID()
function editClaimSubmissionDetails(rownum){	
	 if(!JS_SecondSubmit){
		 var completedYN=document.forms[1].completedYN.value;
			if(completedYN==="Y"){
				alert("Sorry!!! This Claim Batch Is Completed.If You Want Changes Click Override Button");return;
			}
	   document.forms[2].rownum.value=rownum;
	   document.forms[2].mode.value="editClaimSubmissionDetails";
	   document.forms[2].action="/AddClaimDetailsAction.do";
	   JS_SecondSubmit=true;
	   document.forms[2].submit();
	}//end of if(!JS_SecondSubmit)
	
}//end of editClaimSubmissionDetails()
function editClaimReSubmissionDetails(rownum){	
	 if(!JS_SecondSubmit){
		 var completedYN=document.forms[1].completedYN.value;
			if(completedYN==="Y"){
				alert("Sorry!!! This Claim Batch Is Completed.If You Want Changes Click Override Button");return;
			}
	   document.forms[2].rownum.value=rownum;
	   document.forms[2].mode.value="editClaimReSubmissionDetails";
	   document.forms[2].action="/AddClaimDetailsAction.do";
	   JS_SecondSubmit=true;
	   document.forms[2].submit();
	}//end of if(!JS_SecondSubmit)
	
}//end of editClaimReSubmissionDetails()
function onOverride()
{
	if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Overriding");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doOverride";
	document.forms[1].action="/OverridePreAuthGeneral.do";
	document.forms[1].submit();
	}
}//end of onOverride()


function onAssignUser(rownum)
{
	if(!JS_SecondSubmit)
	{
				document.forms[1].mode.value="doBatchAssign";
				document.forms[1].rownum.value=rownum;
				document.forms[1].action="/ClaimBatchAssignAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
	}
}


function onBatchAssignSave()
{
	trimForm(document.forms[1]);
	if(!JS_SecondSubmit){
		document.forms[1].mode.value="doBatchAssignSave";
		document.forms[1].action="/ClaimBatchAssignAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)	
}//end of onSave(

	