/** @ (#) claimsUpload.js 20 Nov 2015 
 * Project     : TTK Healthcare Services
 * File        : claimsUpload.js
 * Author      : Kishor kumar S H
 * Company     : RCS Technologies
 * Date Created: 17 May 2017
 *
 * @author 		 : Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
function startClaimUpload(){
	var batchRefNo =  document.forms[1].hiddenBatchRefNo.value;
	if(batchRefNo != ""){
		alert("Batch Reference Number is "+batchRefNo);
		if(!JS_SecondSubmit){
			document.forms[1].mode.value="doCreateClaimsFromUploadedFile";
			document.forms[1].action="/OnlinePBMUploadsClaimsAction.do";	
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
		document.forms[1].mode.value="doPBMBulkUploadClaims";
	   	document.forms[1].action="/OnlinePBMBulkUploadsClaimsAction.do";
	   	JS_SecondSubmit=true;
	   	
	 	document.forms[1].Button.value = 'Please wait..';
	   	document.getElementById("Button").style.background='red';
	   	document.getElementById("Button").disabled = true;
	   	document.forms[1].submit();
	}
}

function onDownloadClmLog(){
	  
  	document.forms[0].leftlink.value="PBMClaim";
    document.forms[0].sublink.value="Claims Error Log Search";
    document.forms[0].tab.value ="Error Log";
    document.forms[0].mode.value = "doSearch";
    document.forms[0].action = "/PBMClaimViewLogListAction.do";
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
	var parameter = "?mode=doDownloadPBMClmTemplate";
    var openPage = "/downLoadPBMClmTemplate.do"+parameter;
    var w = screen.availWidth - 10;
    var h = screen.availHeight - 49;
    var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
    window.open(openPage,'',features);
}

