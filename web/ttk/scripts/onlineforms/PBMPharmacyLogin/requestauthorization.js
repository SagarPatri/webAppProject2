/** @ (#) addenrollment.js 07th April 2017
 * Project     : Project X
 * File        : pbmPreauth.js
 * Author      : Nagababu K
 * Company     : RCS Technologies
 * Date Created: 07th April 2017
 *
 * @author 		 : Nagababu K
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */

function onBack() {
	var msg=confirm('Do you want to close?');
    if(msg){
    	
    	document.forms[1].tab.value="PBM Check Eligibility";	
    	document.forms[1].sublink.value="PBM Check Eligibility";
    	  document.forms[1].mode.value="doPreauthProccess";
    	  document.forms[1].action="/PbmPharmacyGeneralAction.do";
    	  document.forms[1].submit();
    	
   	 
	 /* document.forms[1].mode.value="doBack";
	  document.forms[1].action=  "/PbmPharmacyPreauthViewlAction.do";
	  document.forms[1].submit();*/
	  
    }
 }

/*function onDispensed() { 
	
	
	
	  if(("REJ"==document.forms[1].preAuthStatus.value)||("INP"==document.forms[1].preAuthStatus.value))
		{
		        alert("There is no approved drug to submit the claim");
		      return;
		 }
  	document.forms[1].leftlink.value="PBMPL";
      document.forms[1].sublink.value="Home";
      document.forms[1].tab.value ="PBM";
	  document.forms[1].mode.value="doDispensed";
	  document.forms[1].action="/PbmClaimSubmissionAction.do";
	  document.forms[1].submit();
	  
  
}*/

function onDispensed() { 	
	  if(("APR"==document.forms[1].preAuthStatus.value)&&("Y"==document.forms[1].completedYN.value))
		{
  	/*document.forms[1].leftlink.value="PBMPL";
      document.forms[1].sublink.value="Home";
      document.forms[1].tab.value ="PBM";*/
	  document.forms[1].mode.value="doDispensed";
	  document.forms[1].action="/PbmClaimSubmissionAction.do";
	  document.forms[1].submit();
	}else if("REQ"==document.forms[1].preAuthStatus.value){
		   alert("Pre-approval is under shortfall, can’t submit Claim");
		   return;
	}else if("PCN"==document.forms[1].preAuthStatus.value){
		   alert("Cancelled Pre-approval can’t submit Claim");
		   return;
	}
	  else if ("REJ"==document.forms[1].preAuthStatus.value){
	        alert("Rejected Pre-approval can’t submit Claim");
	      return;
	 }  else if ("INP"==document.forms[1].preAuthStatus.value){
	        alert("Inprogress Pre-approval can’t submit Claim");
		      return;
		 }else{
			 alert("There is no approved drug to submit the claim");
		 }
}

function onPBMSubmitClaim() {
	if(checkAtleastOne()){
		alert("Please select at least one Drug");
		return;
	}
	var msg=confirm('Are you sure?');
    if(msg){ 
    	
    	document.forms[1].leftlink.value="PBMPL";
        document.forms[1].sublink.value="Home";
        document.forms[1].tab.value ="PBM";
       document.forms[1].enctype="multipart/form-data";
	  document.forms[1].mode.value="doPBMSubmitClaim";
	  document.forms[1].action="/PbmPharmacyGeneralAction.do";
	  document.forms[1].submit();
	  
    }
 }
function drucCheckAll(chObj){
	var varChbx=document.forms[1].drucCheck;			
	var varChbxArr = varChbx.length ? varChbx : [varChbx];  
	var i=0;
	var contrLength=varChbxArr.length;
	
	var flag;
	if(chObj.checked)flag=true;
	else flag=false;
		
		for(i;i<contrLength;i++){
			varChbxArr[i].checked=flag;				           		
		}
}

function checkAtleastOne(){
	var varChbx=document.forms[1].drucCheck;			
	var varChbxArr = varChbx.length ? varChbx : [varChbx];  
	var i=0;
	var contrLength=varChbxArr.length;
	var status=true;
	for(i;i<contrLength;i++){
	if( varChbxArr[i].checked){
		status=false;
			
		break;
	}
	}
	return status;
}

function onSaveAppealComments()
{
	var appealDocsYN= document.getElementById("appealDocsYN").value;
	var appealComments= document.getElementById("appealComments").value;
	if(appealComments == "" || appealComments == null)
		{
			alert("please enter Appeal Comments.");
			document.getElementById("appealComments").focus();
			return;
		}
	
	var appealComments= document.getElementById("appealComments").value;
    var space = appealComments.charCodeAt(0);
      if(space==32)
      {
             alert("Appeal Comments  should not start with space.");
             document.getElementById("appealComments").value="";
             document.getElementById("appealComments").focus();
             return;
      } 
	
	if(appealDocsYN == "N")
	{
		res = confirm("Appeal Documents are not uploaded! Do you still want to save ?");
		if(res == true)
		{
			if(!JS_SecondSubmit){
				document.forms[1].mode.value="doSaveAppealCommentPBM";
			    document.forms[1].action = "/PbmPharmacyGeneralAction.do";    
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}
		}
		else
		{
			return false;
		}
	}	
	
	if(appealDocsYN == "Y")
	{	
		if(!JS_SecondSubmit)
		{
			document.forms[1].mode.value="doSaveAppealCommentPBM";
		    document.forms[1].action = "/PbmPharmacyGeneralAction.do";    
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}	

	}
	
}

function onCommonUploadDocuments2(strTypeId,strAppeal)
{
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefaultUploadView";
	var seqID	=	document.forms[1].seqId.value;
	if(strTypeId==="PAT"&&document.getElementById("patSeqId")){
		seqID=document.getElementById("patSeqId").value;
	}
	document.forms[1].action="/DocsUpload.do?typeId="+strTypeId+"&seqId="+seqID+"&strAppeal="+strAppeal;
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

