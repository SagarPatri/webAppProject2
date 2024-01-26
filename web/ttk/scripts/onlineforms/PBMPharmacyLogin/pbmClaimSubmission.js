/** @ (#) pbmClaimSubmission.js 07th April 2017
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

function onBack(){
	var msg=confirm('Do you want to close?');
    if(msg){
    	if(document.getElementById("preAuthDetailId")){
    		document.forms[1].mode.value="doBack";
    		  document.forms[1].action="/PbmPharmacyGeneralUpdationAction.do";
    		  document.forms[1].submit();
    	}else{
    		document.forms[1].mode.value="doBack";
    		  document.forms[1].action="/PbmPharmacyClaimAction.do";
    		  document.forms[1].submit();
    	}
	  
    }else
    	return false;
 }


function onPBMSubmitClaim() {
	
	var invoiceNo=document.forms[1].invoiceNo.value;
	
	if(checkAtleastOne()){
		alert("Please select at least one Drug");
		return;
	}
	if("null"==invoiceNo||0==invoiceNo){
		alert("Please enter Invoice Number");
		return;
	}
	
	
	
	var msg=confirm('Confirm claim submission');
    if(msg){     	
    	document.forms[1].leftlink.value="PBMPL";
        document.forms[1].sublink.value="Home";
        document.forms[1].tab.value ="PBM";
	  document.forms[1].mode.value="doPBMSubmitClaim";
	  document.forms[1].action="/PbmClaimSubmissionAction.do";
	  
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



function onUploadDocs()
{
	//var fromFlag	=	document.forms[1].fromFlag.value;
	document.forms[1].mode.value="doDefaultUploadDocs";
	//var lPreAuthIntSeqId	=	document.forms[1].lPreAuthIntSeqId.value;
	document.forms[1].action="/PbmPharmacyGeneralUpdationAction.do?";//lPreAuthIntSeqId="+lPreAuthIntSeqId+"&preAuthNoYN="+document.forms[1].preAuthNoYN.value+"&fromFlag="+fromFlag;
	document.forms[1].submit();
}

function onBackWard() {
	var msg=confirm('Do you want to close?');
    if(msg){
   	 
	  document.forms[1].mode.value="doBackward";
	  document.forms[1].action="/PbmClaimAction.do";
	  document.forms[1].submit();
	  
    }
 }








