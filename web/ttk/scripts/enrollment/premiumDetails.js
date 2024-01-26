function addPremiumDtls()
{
	
	if(!JS_SecondSubmit){
		
		var minimumAge=document.getElementById("minimumAge").value;
		var maximumAge=document.getElementById("maximumAge").value;
		var gender=document.getElementById("gender").value;
		var premium=document.getElementById("premium").value;
		var deleteYN="N";
		var switType=document.getElementById("switType").value;
		var memcountYN=document.getElementById("memcountYN").value;
		if(switType=="END"){
			alert("You cann't add the premium details through endorsement ");
			return;
		}
		if(memcountYN=="Y"){
			alert("You cann't add the premium details because member exists for this policy");
			return;
		}
		if(minimumAge==""){
			alert("Please enter the Minimun Age");
			document.getElementById("minimumAge").focus();
			return;
		}
		if(maximumAge==""){
			alert("Please enter the Maximum Age");
			document.getElementById("maximumAge").focus();
			return;
		}
		if(premium==""){
			alert("Please enter the Premium");
			document.getElementById("premium").focus();
			return;
		}


		if (!((document.getElementById("minimumAge").value) === "")
				&& !((document.getElementById("maximumAge").value) === "")) {

			if (Number(document.getElementById("minimumAge").value) > Number(document
					.getElementById("maximumAge").value)) {
				alert("Maximum age always Greater Then Minimum age");
				document.getElementById("maximumAge").value = "";
				document.getElementById("maximumAge").focus();
				return;
			}

		}
		
		document.forms[0].mode.value="addPremiumDtls";
		document.forms[0].action="/PremiumDetailsAction.do?&minimumAge="+minimumAge+"&maximumAge="+maximumAge+"&gender="+gender+"&premium="+premium+"&deleteFlag="+deleteYN;             
		JS_SecondSubmit=true;
		document.forms[0].submit();
    }
	
	
}


function edit(id)
{
	var switType=document.getElementById("switType").value;
	var memcountYN=document.getElementById("memcountYN").value;
	if(switType=="END"){
		alert("You cann't edit the premium details through endorsement ");
		return;
	}
	if(memcountYN=="Y"){
		alert("You cann't edit the premium details because member exists for this policy");
		return;
	}
	document.forms[0].mode.value="editPremiumDtls";
	document.forms[0].action="/PremiumDetailsAction.do?&premiumSeqId="+id;
	document.forms[0].submit();
	
	
}


function deletePremium(id)
{
	var deleteYN="Y";
	var switType=document.getElementById("switType").value;
	if(switType=="END"){
		alert("You cann't delete the premium details through endorsement ");
		return;
	}
	document.forms[0].mode.value="deletePremiumDtls";
	document.forms[0].action="/PremiumDetailsAction.do?&premiumSeqId="+id+"&deleteFlag="+deleteYN;
	document.forms[0].submit();
	
}




function deletePremium(id)
{
var message;
message=confirm('Are you sure you want to delete the premium details?');
if(message)
{
	if(!JS_SecondSubmit)
    {
	 	var deleteYN="Y";
		var switType=document.getElementById("switType").value;
		var memcountYN=document.getElementById("memcountYN").value;
		if(switType=="END"){
			alert("You cann't delete the premium details through endorsement ");
			return;
		}
		if(memcountYN=="Y"){
			alert("You cann't delete the premium details because member exists for this policy");
			return;
		}
		document.forms[0].mode.value="deletePremiumDtls";
		document.forms[0].action="/PremiumDetailsAction.do?&premiumSeqId="+id+"&deleteFlag="+deleteYN;
		JS_SecondSubmit=true
		document.forms[0].submit();
		
	}//end of if(!JS_SecondSubmit)
}//end of if(message)

}



function isPositiveIntegerPre(obj,field_name)
{
   for (var i=0;i<obj.value.length;i++)
    {
	     if(isNaN(obj.value.charAt(i)))
	      {
	         alert("Please enter a valid "+ field_name);
	         obj.focus();
	         obj.value = "";
	         return false;
	       }
    }
    return true;
}


function ageCompare() {

	if (!((document.getElementById("minimumAge").value) === "")
			&& !((document.getElementById("maximumAge").value) === "")) {

		if (Number(document.getElementById("minimumAge").value) > Number(document
				.getElementById("maximumAge").value)) {
			alert("Maximum age always Greater Then Minimum age");
			document.getElementById("maximumAge").value = "";
			document.getElementById("maximumAge").focus();
		}

	}

}



function onClose() {
	
	document.forms[0].mode.value="closePremium";
	document.forms[0].action="/PremiumDetailsAction.do";
	document.forms[0].submit();
	
}


function onReset() {
	
	document.forms[0].mode.value="selectPremiumDetails";
	document.forms[0].action="/PolicyDetailsAction.do";
	document.forms[0].submit();
	
}


