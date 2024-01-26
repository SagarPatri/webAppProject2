/** @ (#) addenrollment.js 09th Jan 2007
 * Project     : TTK Healthcare Services
 * File        : addenrollment.js
 * Author      : Chandrasekaran J
 * Company     : Span Systems Corporation
 * Date Created: 09th Jan 2007
 *
 * @author 		 : Chandrasekaran J
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */

//this function is called onclick of save button
/*function onSave()
{
	if(!JS_SecondSubmit)
    {
		trimForm(document.forms[1]);
		document.getElementById("memname").value=document.forms[1].name.value;
		document.forms[1].mode.value="doSave";
		document.forms[1].action="/UpdateAddEnrollmentAction.do";
		JS_SecondSubmit=true
		document.forms[1].submit();
	}//end of	if(!JS_SecondSubmit)
}//end of onSave()*/


function onSave()
{
	if(!JS_SecondSubmit)
    {
		var validateFlag=false;
	   if(((document.forms[1].validEmailPhYN!= 'undefined') && (document.forms[1].validEmailPhYN != null)  && (document.forms[1].validEmailPhYN != ''))
	   || ((document.forms[1].stopOPtInYN!= 'undefined') && (document.forms[1].stopOPtInYN != null)  && (document.forms[1].stopOPtInYN != '')))
	   {
			//alert("top");
		  
		if(document.forms[1].validEmailPhYN.checked)
		{
			if(document.forms[1].EmailId2.value == "")
	  		{
			alert('Please Enter Personal EmailID');
			document.forms[1].EmailId2.focus();
			return false;
		   }
	 		if(document.forms[1].MobileNo.value == "")
			{
			alert('Please Enter Mobile No');
			document.forms[1].MobileNo.focus();
			return false;
			} 		
			
		   document.forms[1].EmailPh.value="Y";		  
			
		}
		else
		{
			document.forms[1].EmailPh.value="N";		
		}
////////////////////
		if(document.forms[1].stopOPtInYN.checked)
		{			
				document.forms[1].OPT.value="N";			
		
		}		
		else 
		{				
		document.forms[1].OPT.value="Y";
		trimForm(document.forms[1]);	
		}
	
		}
		  //alert(document.forms[1].EmailPh.value);		   
		 //alert(document.forms[1].OPT.value);				
		//no else final conditions
	   if(document.forms[1].loginType.value=="EMPL"){
		   
		   
		   
		   var ibanNum = document.forms[1].micr.value;
			
			if(ibanNum.indexOf(' ') >= 0){
				alert("Plase remove the space");
				document.getElementById("micr").focus();
				 return false;
			}
			
	     	var bankLocation = document.forms[1].bankAccountQatarYN.value;
	     	if(ibanNum !=""){
	    	 if(bankLocation=="Y"&& ibanNum.length != 29){
	   		alert("IBAN Number should be 29 characters"); 
	   		document.getElementById("micr").focus();
	  		 return false;
	   	 }
	     	} 
		   
		   document.forms[1].mode.value="doSave";
			document.forms[1].action="/UpdateAddEmplEnrollmentAction.do";
	   }else{
		   var ibanNum = document.forms[1].micr.value;
			
			if(ibanNum.indexOf(' ') >= 0){
				alert("Plase remove the space");
				document.getElementById("micr").focus();
				 return false;
			}
			
	     	var bankLocation = document.forms[1].bankAccountQatarYN.value;
	     	if(ibanNum !=""){
	    	 if(bankLocation=="Y"&& ibanNum.length != 29){
	   		alert("IBAN Number should be 29 characters"); 
	   		document.getElementById("micr").focus();
	  		 return false;
	   	 }
	     	}
	     	
	     	var bankAccountQatarYN 	= document.forms[1].bankAccountQatarYN.value;
			var bankCountry			= document.forms[1].bankCountry.value;
			if(bankAccountQatarYN == 'Y' && bankCountry != "")
				{
					if(bankCountry !='134')
						{
							alert("Mismatch between Bank Location & Country! Please select valid Country.");
							return;
						}
				}
	     	
	     	var actualPrincipalName=$.trim(document.getElementById('actualPrincipalName').value);
	     	var insuredName=$.trim(document.getElementById("insuredName").value);
	     	if(actualPrincipalName!=''&&insuredName!=actualPrincipalName){
	     		validateFlag=true;
	     	}
	     	document.forms[1].relationship.removeAttribute("disabled");
			document.forms[1].genderTypeID.removeAttribute("disabled");
			document.forms[1].maritalStatus.removeAttribute("disabled");
	     	 if(document.forms[1].loginType.value=="H" && validateFlag==true){
	     		if(confirm('Do you want to re-print the card?')) {
	     			document.forms[1].mode.value="doSave";
					document.forms[1].action="/UpdateAddEnrollmentAction.do?validateFlag="+validateFlag;
					JS_SecondSubmit=true;
					document.forms[1].submit();
	     		}
	     	 }else{
	     		document.forms[1].mode.value="doSave";
				document.forms[1].action="/UpdateAddEnrollmentAction.do?validateFlag="+validateFlag;
	     	 }
		   
	   }
		   	if(validateFlag==false){
		   		JS_SecondSubmit=true;
				document.forms[1].submit();
		   	}
			
	}//end of if(!JS_SecondSubmit)
}//end of onSave()

//<!--Ended---->
//this function is called onclick of close button
function onClose()
{
	if(!TrackChanges()) return false;
    //document.forms[1].mode.value="doClose";
	document.forms[1].mode.value="doDefault";
    document.forms[1].child.value="";
    //document.forms[1].action="/AddEnrollmentAction.do";
    document.forms[1].action="/OnlineMemberAction.do";
    document.forms[1].submit();
}//end of onClose()
function onCloseMember()
{
	if(!TrackChanges()) return false;
    //document.forms[1].mode.value="doClose";
	document.forms[1].mode.value="doViewMemberInfo";
    document.forms[1].child.value="";
    //document.forms[1].action="/AddEnrollmentAction.do";
    document.forms[1].action="/OnlineMemberAction.do";
    document.forms[1].submit();
}//end of onClose()

function onHRClose()
{
	if(!TrackChanges()) return false;
    document.forms[1].mode.value="doClose";
	document.forms[1].child.value="";
    document.forms[1].action="/AddEnrollmentAction.do";
    document.forms[1].submit();
}//end of onClose()

//this function is called onclick of reset button
function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset)
  	{
    	document.forms[1].mode.value="doReset";
    	document.forms[1].action="/AddEnrollmentAction.do";
    	document.forms[1].submit();
  	}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
  else
  {
    document.forms[1].reset();
    if(typeof(validEmailPhYN)!='undefined')
    {	
        if(document.forms[1].EmailPh.value=="Y")//overwritten by Praveen Nov8.2012
        document.forms[1].validEmailPhYN.checked=true;
        else
        document.forms[1].validEmailPhYN.checked=false;
    }    
	
  }//end of else
}//end of onReset()
function onSend()
{
	document.forms[1].mode.value="doSend";
	document.forms[1].action="/UpdateAddEnrollmentAction.do";
	document.forms[1].submit();		
}//end of onSend()


//koc1216 added by Rekha 13.07.2012
//------------------------

function stopOPTYN()
{
	//if(document.forms[1].OPT.value=="Y")
	if(document.forms[1].OPT.value=="N")//overwritten by Praveen Nov8.2012
	    document.forms[1].stopOPtInYN.checked=true;
	else
		
	    document.forms[1].stopOPtInYN.checked=false;
	
}//end of stopOPTYN()

function EmailMobPhVal()
{
	//if(document.forms[1].OPT.value=="Y")
	if(document.forms[1].EmailPh.value=="Y")//overwritten by Praveen Nov8.2012
	    document.forms[1].validEmailPhYN.checked=true;
	else
		
	    document.forms[1].validEmailPhYN.checked=false;
	
}//end of EmailMobPhVal()

//koc1216 end added by Rekha 13.07.2012
function onCloseWindow()
{
	if(!TrackChanges()) return false;
	document.forms[1].child.value="";
	if(document.forms[1].loginType.value==="EMPL"){
		 //document.forms[1].mode.value="doClose";
		document.forms[1].mode.value="doDefault"; 
	    //document.forms[1].action="/AddEnrollmentAction.do";
	    document.forms[1].action="/EmployeeHomeAction.do";
	}else{
		 //document.forms[1].mode.value="doClose";
		document.forms[1].mode.value="doClose"; 
	    //document.forms[1].action="/AddEnrollmentAction.do";
	    document.forms[1].action="/OnlinePolicyDetailsAction.do";		
	}
    document.forms[1].submit();
}//end of onCloseWindow()















function convertToHDate(hijriId){
	var dd,mm,yyyy;
	var Gdate = toDate(document.getElementById("Dob").value,false);
	if(Gdate){
		var Hdate = new HijriDate();
		Hdate.setTime(Gdate.getTime());
		dd = Hdate.getDate();
		mm = Hdate.getHMonth()+1;
		yyyy = Hdate.getHFullYear();
		if(dd.toString().length == 1)
			dd = "0"+dd;
		if(mm.toString().length == 1)
			mm = "0"+mm;
		var convertedDate = dd+"/"+mm+"/"+yyyy;
		document.getElementById(hijriId).value = convertedDate;
	}
	else
		document.getElementById(hijriId).value="";
}

function onDateofBirth()
{
	var regexp = /^(\d{2}\/\d{2}\/\d{4})$/;
	document.forms[1].dateOfBirth.value = trim(document.forms[1].dateOfBirth.value);
	var ageObj = document.getElementById("ageid");
	if (regexp.test(document.forms[1].dateOfBirth.value)==true)
	{
		var age = calculatedAge(document.forms[1].dateOfBirth.value,document.forms[1].policyStartDate.value);
		if(!isNaN(age) && age>=0)
			ageObj.value = age;
		else
			ageObj.value = "0";
	}//end of if (regexp.test(document.forms[1].dateOfBirth.value)==true)
	if(document.forms[1].dateOfBirth.value.length>0)
		ageObj.readOnly=true;
	else
		ageObj.readOnly=false;
}//end of onDateofBirth()

function convertToGDate(GId){
	var dd,mm,yyyy;
	var Hdate = toDate(document.getElementById("HDob").value,true);
	
	if(Hdate){
		var Gdate = new Date();
		Gdate.setTime(Hdate.getTime());
		//Gdate.setDate(Gdate.getDate()+1);
		dd = Gdate.getDate();
		mm = Gdate.getMonth()+1;
		yyyy = Gdate.getFullYear();
		if(dd.toString().length == 1)
			dd = "0"+dd;
		if(mm.toString().length == 1)
			mm = "0"+mm;
		var convertedDate = dd+"/"+mm+"/"+yyyy;
		document.getElementById(GId).value = convertedDate;
	}
	else
		document.getElementById(GId).value="";
}

function toDate(dateStr,isHijri) {
    var sDate  = dateStr.split("/");
    if(dateStr){
    	if(isHijri){
    		var hijriDate = new HijriDate();
    		hijriDate.setDate(sDate[0]);
    		hijriDate.setMonth(sDate[1]-1);
    		hijriDate.setFullYear(sDate[2]);
    		
    		if(sDate[0]==="08"||sDate[0]==="09"){
    			if(sDate[0]==="08")
    				hijriDate.setDate(hijriDate.getDate() - 21);
    			else
    				hijriDate.setDate(hijriDate.getDate() - 20);
    		}
    	return hijriDate;
      }
    	else
    	if(!(dateStr === ""))
    		return new Date(sDate[2] ,sDate[1]-1, sDate[0]);
    }
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
    	document.forms[1].action="/UpdateEnrollmentAction.do?focusID=bankstate";
    	document.forms[1].submit();
}//end of onChangeBank()

function onChangeState(focusid)
{
	    document.forms[1].mode.value="doChangeState";
	    document.forms[1].focusID.value="bankcity";
	    document.forms[1].bankcity.focus();
	    document.forms[1].bankcity.value="";
	    document.forms[1].bankBranch.value=""; 
    	document.forms[1].action="/UpdateEnrollmentAction.do?focusID=bankcity";
    	document.forms[1].submit();
}//end of onChangeState()

function onChangeCity(focusid)
{

	    document.forms[1].mode.value="doChangeCity";
	    document.forms[1].focusID.value="bankBranch";
	    document.forms[1].bankBranch.focus();
	    document.forms[1].bankBranch.value="";
    	document.forms[1].action="/UpdateEnrollmentAction.do?focusID=bankBranch";
    	document.forms[1].submit();
}//end of onChangeCity()





function onChangeQatarYN()
{
    document.forms[1].mode.value="doChangeQatarYN";
	document.forms[1].action="/UpdateEnrollmentAction.do";
	document.forms[1].submit();
	
}











