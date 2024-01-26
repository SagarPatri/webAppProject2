function selectDiagnosisCode(){
	if(!JS_SecondSubmit){
	  document.forms[1].mode.value="doGeneral";
	   document.forms[1].reforward.value="diagnosisSearch";
	   document.forms[1].action="/PartnerClaimDiagnosisSearchAction.do?focusObj=icdCode";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
	}
}

function onSubmitForm(){
	/*if(document.getElementById("presentingComplaintsId").value =="" && document.getElementById("icdCode").value == ""){
		alert("Any one of Presenting Complaints OR Diagnosis / ICD Code are mandatory");
	}*/
	if(!JS_SecondSubmit){
		  document.forms[1].mode.value="doSubmitPartnerClaims";
		   document.forms[1].action="/OnlinePartnerClaimsSubmit.do?focusObj=icdCode";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();
		}
}

function getcurrencyCode(){
	 var countryId= document.getElementById("country").value;
	 var  path="/asynchronAction.do?mode=getCurencyType&countryId="+countryId;
	 	$.ajax({
		     url :path,
		     dataType:"text",
		     success : function(data) {
		     var res1 = data;
		    document.getElementById("currency").value=res1;
		     }//function(data)
		 });
}

function onClose(){
	if(!JS_SecondSubmit){
		  document.forms[1].mode.value="doClose";
		   document.forms[1].action="/OnlinePartnerClaimsSubmission.do?focusObj=icdCode";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();
		}
}

function onCloseCLaim(){
	
	if(!JS_SecondSubmit){
		
		document.forms[1].leftlink.value="Partner Information";
		document.forms[1].sublink.value="Home";
		document.forms[1].tab.value="Home";
		  document.forms[1].mode.value="doClose";
		   document.forms[1].action="/OnlinePartnerClaimsSubmission.do?closeObj=claimClose";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();
		}
}

function reqAmtIsNumber(obj,displayName)
{
	var str=obj.value;

	str=trim(str);
	if(isNaN(str))
	{
		alert("Please enter a valid "+displayName+".");
		obj.value="";
		obj.focus();
		return false;
	}
	
}

function isNumberForRequestAmount(obj,displayName)
{
	var str=obj.value;

	str=trim(str);
	obj.value=str;
	var vflag=true;
	if(isNaN(str))
	{
		alert("Please enter a valid "+displayName);
		obj.select();
		obj.focus();
		document.getElementById("requestedAmt").value="";
		vflag=false;
	}
	return vflag
}