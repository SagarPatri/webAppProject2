/*
		 * This java script is added for cr koc 1103
		 * added eft
		 */
//declare the Ids of the form fields seperated by comma which should not be focused when document is loaded
var JS_donotFocusIDs=["switchType"];
function onUserPolicySubmit()
{
	if(!JS_SecondSubmit)
     {
		// 1:  space validation
		var ibanNum = document.forms[1].micr.value;
		if(ibanNum.indexOf(' ') >= 0){
			alert("Plase remove the space.");
			document.getElementById("micr").focus();
			 return false;
		}
		
		// 2:  length validation
		var bankLocation = document.forms[1].bankAccountQatarYN.value;
    	 if(document.getElementById("stopPreauthDateId").style.display ==""){
    		 var stoppreauthdateflag;
 			var stoppreauth = document.getElementById("stopPreauthId").value;
 			if(stoppreauth ==""){
 				 alert("Stop Cashless Date is Required");
 				  return false;
 			}
 			
 			 if(document.getElementById("stoppreauthdateflagid") != null){
 				stoppreauthdateflag =	document.getElementById("stoppreauthdateflagid").value;
 			 }	
 					var  currentDate =  new Date();
 					var curmonth = currentDate.getMonth() + 1;
 				    var curday = currentDate.getDate();
 				    var curyear = currentDate.getFullYear();
 				    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
 					  
 				      curnewdate = curnewdate.split("/");
 				      stoppreauth = stoppreauth.split("/");
 				      stoppreauthdateflag = stoppreauthdateflag.split("-");
 					  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
 					  stoppreauth = new Date(stoppreauth[2], stoppreauth[1] - 1, stoppreauth[0]).getTime();
 					  stoppreauthdateflag = new Date(stoppreauthdateflag[0], stoppreauthdateflag[1] - 1, stoppreauthdateflag[2]).getTime();
 					  if(stoppreauthdateflag == stoppreauth){
 						  
 					  }else if(!(stoppreauth > curnewdate)){
 						  alert("Stop Cashless Date must be future date");
 						  return false;
 					  }
 				}
 	    	
 	    	if(document.getElementById("stopClaimsDateId").style.display ==""){
 	    		var stopclaimsflag;	
 			var stopclms = document.getElementById("stopclmsid").value;
 			if(stopclms==""){
 				  alert("Stop Claims Date is Required");
 				  return false;
 			  }
 			
 			if(document.getElementById("stopclaimdateflagid") != null){
 				stopclaimsflag =	document.getElementById("stopclaimdateflagid").value;
 			}
 			var  currentDate =  new Date();
 			var curmonth = currentDate.getMonth() + 1;
 		    var curday = currentDate.getDate();
 		    var curyear = currentDate.getFullYear();
 		    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
 			  
 		      curnewdate = curnewdate.split("/");
 		      stopclms = stopclms.split("/");
 		      stopclaimsflag = stopclaimsflag.split("-");
 			  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
 			  stopclms = new Date(stopclms[2], stopclms[1] - 1, stopclms[0]).getTime();
 			  stopclaimsflag = new Date(stopclaimsflag[0], stopclaimsflag[1] - 1, stopclaimsflag[2]).getTime();
 			  if(stopclaimsflag == stopclms){
 				  
 			  }else  if(!(stopclms > curnewdate)){
 				  alert("Stop Claims Date must be future date");
 				  return false;
 			  }
 			
 			}  
 			
 		
    	 var ibanNum = document.forms[1].micr.value;
    		if(ibanNum.indexOf(' ') >= 0){
    			alert("Plase remove the space");
    			document.getElementById("micr").focus();
    			 return false;
    		}
    	 var bankLocation = document.forms[1].bankAccountQatarYN.value;
     	 if(bankLocation=="Y"&& ibanNum.length != 29){
    		alert("IBAN Number should be 29 characters."); 
    		document.getElementById("micr").focus();
   		 	return false;
    	 }
     	 
     	// 3:  QA validation
     	 var IbanNoFirstTwodigi 	= ibanNum.charAt(0)+ibanNum.charAt(1);
      	if(bankLocation=="Y" && ibanNum.length == 29 && IbanNoFirstTwodigi != 'QA')
      	{
     		alert("IBAN Number should be Start with characters QA"); 
     		document.getElementById("micr").focus();
     		return false;
     	 }
		
     // 4:  swiftcode & IbanNo validation
    	if(bankLocation=="Y"){
      	var swiftcode 	= document.forms[1].ifsc.value;
    	 var IbanNo	= document.getElementById("micr").value;
    	 
    	 var IbanNo5to8Digit 	= IbanNo.charAt(4)+IbanNo.charAt(5)+IbanNo.charAt(6)+IbanNo.charAt(7);
    	 var swiftcode1to4Digit = swiftcode.charAt(0)+swiftcode.charAt(1)+swiftcode.charAt(2)+swiftcode.charAt(3);
    	 if(swiftcode != ""  && IbanNo != ""){
  			if(IbanNo5to8Digit != swiftcode1to4Digit){
  					alert("The 5th to 8th characters of IBAN Number and 1st to 4th characters of Swift code are not matching! Please enter valid IBAN Number & swift Code.");
  					return false;
  			}
  		}}
		
    // 5:  Swift Code & Country Code validation
    	 var swiftcode = document.forms[1].ifsc.value;
		var FifthSixthDigitOfswiftcode = swiftcode.charAt(4)+swiftcode.charAt(5);
		var towDigitCountryCode = document.forms[1].towDigitCountryCode.value;
		if(swiftcode != ""  && towDigitCountryCode != ""){
			if(towDigitCountryCode != FifthSixthDigitOfswiftcode)
			{
					alert("Swift Code 5th & 6th letter not matching with Country Code! Please enter valid Swift Code.");
					document.forms[1].ifsc.focus();
					return false;
			}
		}
		
	// 6 : Bank Location & Country validation
		var bankAccountQatarYN 	= document.forms[1].bankAccountQatarYN.value;
		var countryCode			= document.forms[1].countryCode.value;
		if(bankAccountQatarYN == 'Y' && countryCode != "")
			{
				if(countryCode !='134')
					{
						alert("Mismatch between Bank Location & Country! Please select valid Country.");
						return;
					}
			}
     	 
     	trimForm(document.forms[1]);
     	document.forms[1].mode.value="doSave";
     	document.forms[1].action="/SaveBankAcctGeneralActionTest.do";
     	JS_SecondSubmit=true
     	document.forms[1].submit();
     }//end of if(!JS_SecondSubmit)	
}//end of onUserSubmit()
function onUserMemberSubmit()
{	
		
     if(!JS_SecondSubmit)
     {
    	// 1:  space validation
    	 	var ibanNum = document.forms[1].micr.value;
    	 	if(ibanNum.indexOf(' ') >= 0){
    	 		alert("Plase remove the space.");
    	 		document.getElementById("micr").focus();
    	 		return false;
    	 	}
    	 	
    	 // 2:  length validation 	
		 	 var bankLocation = document.forms[1].bankAccountQatarYN.value;
		  	 if(bankLocation=="Y"&& ibanNum.length != 29){
		 		alert("IBAN Number should be 29 characters."); 
		 		document.getElementById("micr").focus();
				 return false;
		 	 }
		  	 
		  // 3:  QA validation 	 
			  	var IbanNoFirstTwodigi 	= ibanNum.charAt(0)+ibanNum.charAt(1);
			  	if(bankLocation=="Y" && ibanNum.length == 29 && IbanNoFirstTwodigi != 'QA')
			  	{
			 		alert("IBAN Number should be Start with characters QA"); 
			 		document.getElementById("micr").focus();
			 		return false;
			  	}
			  	
		  // 4:  swift code & IbanNo validation
			  	if(bankLocation=="Y"){
			    	 var swiftcode 	= document.forms[1].ifsc.value;
			    	 var IbanNo	= document.getElementById("micr").value;
			    	 var IbanNo5to8Digit 	= IbanNo.charAt(4)+IbanNo.charAt(5)+IbanNo.charAt(6)+IbanNo.charAt(7);
			    	 var swiftcode1to4Digit = swiftcode.charAt(0)+swiftcode.charAt(1)+swiftcode.charAt(2)+swiftcode.charAt(3);
			    	 if(swiftcode != ""  && IbanNo != "")
			    	 {
			  			if(IbanNo5to8Digit != swiftcode1to4Digit)
			  			{
			  					alert("The 5th to 8th characters of IBAN Number and 1st to 4th characters of Swift code are not matching! Please enter valid IBAN Number & swift Code.");
			  					return false;
			  			}
			  		} }
			    	 
			// 5:  Swift Code & Country Code validation    	 
		    	var swiftcode = document.forms[1].ifsc.value;
		 		var FifthSixthDigitOfswiftcode = swiftcode.charAt(4)+swiftcode.charAt(5);
		 		var towDigitCountryCode = document.forms[1].towDigitCountryCode.value;
		 		if(swiftcode != ""  && towDigitCountryCode != ""){
		 			if(towDigitCountryCode != FifthSixthDigitOfswiftcode)
		 			{
		 					alert("Swift Code 5th & 6th letter not matching with Country Code! Please enter valid Swift Code.");
		 					document.forms[1].ifsc.focus();
		 					return false;
		 			}
		 		}
				// 6 : Bank Location & Country validation
		 		var bankAccountQatarYN 	= document.forms[1].bankAccountQatarYN.value;
		 		var countryCode			= document.forms[1].countryCode.value;
		 		if(bankAccountQatarYN == 'Y' && countryCode != "")
		 		{
		 				if(countryCode !='134')
		 					{
		 						alert("Mismatch between Bank Location & Country! Please select valid Country.");
		 						return;
		 					}
		 		}	
    	 if(document.getElementById("stopPreauthDateId").style.display ==""){
    		 var stoppreauthdateflag ;
 			var stoppreauth = document.getElementById("stopPreauthId").value;
 			if(stoppreauth ==""){
 				 alert("Stop Cashless Date is Required");
 				  return false;
 			}
 			
 			 if(document.getElementById("stoppreauthdateflagid")!= null ){
 				stoppreauthdateflag	=	document.getElementById("stoppreauthdateflagid").value;
 			 }
 					var  currentDate =  new Date();
 					var curmonth = currentDate.getMonth() + 1;
 				    var curday = currentDate.getDate();
 				    var curyear = currentDate.getFullYear();
 				    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
 					  
 				      curnewdate = curnewdate.split("/");
 				      stoppreauth = stoppreauth.split("/");
 				      stoppreauthdateflag = stoppreauthdateflag.split("-");
 					  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
 					  stoppreauth = new Date(stoppreauth[2], stoppreauth[1] - 1, stoppreauth[0]).getTime();
 					  stoppreauthdateflag = new Date(stoppreauthdateflag[0], stoppreauthdateflag[1] - 1, stoppreauthdateflag[2]).getTime();
 					  if(stoppreauthdateflag == stoppreauth){
 						  
 					  }else if(!(stoppreauth > curnewdate)){
 						  alert("Stop Cashless Date must be future date");
 						  return false;
 					  }
 				}
 		
 	    	
 	    	if(document.getElementById("stopClaimsDateId").style.display ==""){
 	    	var stopclaimsflag;
 			var stopclms = document.getElementById("stopclmsid").value;
 			if(stopclms==""){
 				  alert("Stop Claims Date is Required");
 				  return false;
 			  }
 			
 			if(document.getElementById("stopclaimdateflagid") != null){
 				stopclaimsflag =	document.getElementById("stopclaimdateflagid").value;
 			}
 			var  currentDate =  new Date();
 			var curmonth = currentDate.getMonth() + 1;
 		    var curday = currentDate.getDate();
 		    var curyear = currentDate.getFullYear();
 		    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
 			  
 		      curnewdate = curnewdate.split("/");
 		      stopclms = stopclms.split("/");
 		      stopclaimsflag = stopclaimsflag.split("-");
 			  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
 			  stopclms = new Date(stopclms[2], stopclms[1] - 1, stopclms[0]).getTime();
 			  stopclaimsflag = new Date(stopclaimsflag[0], stopclaimsflag[1] - 1, stopclaimsflag[2]).getTime();
 			  if(stopclaimsflag == stopclms){
 				  
 			  }else  if(!(stopclms > curnewdate)){
 				  alert("Stop Claims Date must be future date");
 				  return false;
 			  }
 			
 			}  
 	    	
    	 var ibanNum = document.forms[1].micr.value;
    		if(ibanNum.indexOf(' ') >= 0){
    			alert("Plase remove the space");
    			document.getElementById("micr").focus();
    			 return false;
    		}
    	 var bankLocation = document.forms[1].bankAccountQatarYN.value;
     	 if(bankLocation=="Y"&& ibanNum.length != 29){
    		alert("IBAN Number should be 29 characters"); 
    		document.getElementById("micr").focus();
   		 return false;
    	 }
     	 
     	trimForm(document.forms[1]);
     	document.forms[1].mode.value="doSaveMember";
     	document.forms[1].action="/SaveBankAcctGeneralActionTest.do?towDigitCountryCode="+towDigitCountryCode;
     	JS_SecondSubmit=true
     	document.forms[1].submit();
     }//end of if(!JS_SecondSubmit)	
}//end of onUserSubmit()
function onUserHospitalSubmit()
{
	if(!JS_SecondSubmit)
     {
		// 1:  space validation
			var ibanNum = document.forms[1].micr.value;
			if(ibanNum.indexOf(' ') >= 0){
				alert("Plase remove the space.");
				document.getElementById("micr").focus();
				 return false;
			}
		
		// 2:  length validation	
			 var bankLocation = document.forms[1].bankAccountQatarYN.value;
		 	 if(bankLocation=="Y"&& ibanNum.length != 29){
				alert("IBAN Number should be 29 characters."); 
				document.getElementById("micr").focus();
				 return false;
			 } 
			
		// 3:  QA validation 	 
		 	var IbanNoFirstTwodigi 	= ibanNum.charAt(0)+ibanNum.charAt(1);
		  	if(bankLocation=="Y" && ibanNum.length == 29 && IbanNoFirstTwodigi != 'QA')
		  	{
		 		alert("IBAN Number should be Start with characters QA"); 
		 		document.getElementById("micr").focus();
		 		return false;
		 	 }
		  	
		 // 4:  swift code & IbanNo validation  	
		  	if(bankLocation=="Y"){ 
		  	var swiftcode 	= document.forms[1].ifsc.value;
		  	var IbanNo	= document.getElementById("micr").value;
	    	 var IbanNo5to8Digit 	= IbanNo.charAt(4)+IbanNo.charAt(5)+IbanNo.charAt(6)+IbanNo.charAt(7);
	    	 var swiftcode1to4Digit = swiftcode.charAt(0)+swiftcode.charAt(1)+swiftcode.charAt(2)+swiftcode.charAt(3);
	    	 if(swiftcode != ""  && IbanNo != "")
	    	 {
	  			if(IbanNo5to8Digit != swiftcode1to4Digit)
	  			{
	  					alert("The 5th to 8th characters of IBAN Number and 1st to 4th characters of Swift code are not matching! Please enter valid IBAN Number & swift Code.");
	  					return false;
	  			}
	  		} }
	    	 
	    	// 5:  Swift Code & Country Code validation
				var swiftcode = document.forms[1].ifsc.value;
		 		var FifthSixthDigitOfswiftcode = swiftcode.charAt(4)+swiftcode.charAt(5);
		 		var towDigitCountryCode = document.forms[1].towDigitCountryCode.value;
		 		if(swiftcode != ""  && towDigitCountryCode != ""){
		 			if(towDigitCountryCode != FifthSixthDigitOfswiftcode)
		 			{
		 					alert("Swift Code 5th & 6th letter not matching with Country Code! Please enter valid Swift Code.");
		 					document.forms[1].ifsc.focus();
		 					return false;
		 			}
		 		} 
    	 
    	 if(document.getElementById("stopPreauthDateId").style.display ==""){
    		 var stoppreauthdateflag;
 			var stoppreauth = document.getElementById("stopPreauthId").value;
 			if(stoppreauth ==""){
 				 alert("Stop Cashless Date is Required");
 				  return false;
 			}
 			
 			 if(document.getElementById("stoppreauthdateflagid") != null){
 				stoppreauthdateflag =	document.getElementById("stoppreauthdateflagid").value;
 			 }	
 					var  currentDate =  new Date();
 					var curmonth = currentDate.getMonth() + 1;
 				    var curday = currentDate.getDate();
 				    var curyear = currentDate.getFullYear();
 				    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
 					  
 				      curnewdate = curnewdate.split("/");
 				      stoppreauth = stoppreauth.split("/");
 				      stoppreauthdateflag = stoppreauthdateflag.split("-");
 					  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
 					  stoppreauth = new Date(stoppreauth[2], stoppreauth[1] - 1, stoppreauth[0]).getTime();
 					  stoppreauthdateflag = new Date(stoppreauthdateflag[0], stoppreauthdateflag[1] - 1, stoppreauthdateflag[2]).getTime();
 					  if(stoppreauthdateflag == stoppreauth){
 						  
 					  }else if(!(stoppreauth > curnewdate)){
 						  alert("Stop Cashless Date must be future date");
 						  return false;
 					  }
 				}
 	    	
 	    	if(document.getElementById("stopClaimsDateId").style.display ==""){
 	    	var stopclaimsflag;
 			var stopclms = document.getElementById("stopclmsid").value;
 			if(stopclms==""){
 				  alert("Stop Claims Date is Required");
 				  return false;
 			  }
 			
 			if(document.getElementById("stopclaimdateflagid") != null){
 				stopclaimsflag =	document.getElementById("stopclaimdateflagid").value;
 			}
 			
 			var  currentDate =  new Date();
 			var curmonth = currentDate.getMonth() + 1;
 		    var curday = currentDate.getDate();
 		    var curyear = currentDate.getFullYear();
 		    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
 			  
 		      curnewdate = curnewdate.split("/");
 		      stopclms = stopclms.split("/");
 		      stopclaimsflag = stopclaimsflag.split("-");
 			  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
 			  stopclms = new Date(stopclms[2], stopclms[1] - 1, stopclms[0]).getTime();
 			  stopclaimsflag = new Date(stopclaimsflag[0], stopclaimsflag[1] - 1, stopclaimsflag[2]).getTime();
 			  if(stopclaimsflag == stopclms){
 				  
 			  }else  if(!(stopclms > curnewdate)){
 				  alert("Stop Claims Date must be future date");
 				  return false;
 			  }
 			
 			}  
 			
    	 var ibanNum = document.forms[1].micr.value;
    		if(ibanNum.indexOf(' ') >= 0){
    			alert("Plase remove the space");
    			document.getElementById("micr").focus();
    			 return false;
    		}
    	 var bankLocation = document.forms[1].bankAccountQatarYN.value;
     	 if(bankLocation=="Y"&& ibanNum.length != 29){
    		alert("IBAN Number should be 29 characters"); 
    		document.getElementById("micr").focus();
   		 return false;
    	 }
     	 
     	
     	 var startDate = document.forms[1].startDate.value;
     	 if(startDate==null||startDate==""){
     		alert("Please select Start Date"); 
    		document.getElementById("startDate").focus();
   		 return false;
     	 }
     	 
     	// 6 : Bank Location & Country validation
 		var bankAccountQatarYN 	= document.forms[1].bankAccountQatarYN.value;
 		var countryCode			= document.forms[1].countryCode.value;
 		if(bankAccountQatarYN == 'Y' && countryCode != "")
 			{
 				if(countryCode !='134')
 					{
 						alert("Mismatch between Bank Location & Country! Please select valid Country.");
 						return;
 					}
 			} 
     	 
     	trimForm(document.forms[1]);
     	document.forms[1].mode.value="doSaveHosp";
     	document.forms[1].action="/SaveBankAcctGeneralActionTest.do";
     	JS_SecondSubmit=true;
     	document.forms[1].submit();
     	
     }//end of if(!JS_SecondSubmit)	
     
}//end of onUserSubmit()
function onChangeBank(focusid)
{
	
    	document.forms[1].mode.value="doChangeBank";
    	document.forms[1].focusID.value=focusid;
    	//document.forms[1].bankname.focus();
    	//document.forms[1].bankname.disabled = true;
    	document.forms[1].bankState.value="";
    	document.forms[1].action="/ChangeIfscGeneralActionTest.do";
    	
    	document.forms[1].submit();
}//end of onChangeState()

function onChangeState(focusid)
{
	    document.forms[1].mode.value="doChangeState";
	    document.forms[1].focusID.value=focusid;
	    document.forms[1].bankcity.value="";
    	document.forms[1].action="/ChangeIfscGeneralActionTest.do";
    	document.forms[1].submit();
}//end of onChangeState()
function onChangeCity(focusid)
{
	    document.forms[1].mode.value="doChangeCity";
	    document.forms[1].focusID.value=focusid;
	    document.forms[1].bankBranch.value="";
    	document.forms[1].action="/ChangeIfscGeneralActionTest.do";
    	document.forms[1].submit();
}//end of onChangeState()

function onChangeBranch(focusid)
{
	 	document.forms[1].mode.value="doChangeBranch"
	 		document.forms[1].focusID.value=focusid;
        document.forms[1].action="/ChangeIfscGeneralActionTest.do";
    	document.forms[1].submit();
}//end of onChangeState()
function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	{
		document.forms[1].mode.value="doReset";
		document.forms[1].action="/ChangeIfscGeneralActionTest.do";
		document.forms[1].submit();
	}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	else
	{
		document.forms[1].reset();
	}//end of else
}//end of onReset()
function onClose()
{
	
	if(!TrackChanges()) return false;
   	onReset();
   	document.forms[1].tab.value="Search";
    document.forms[1].mode.value="doCloseFinance";
    document.forms[1].action = "/ChangeIfscGeneralActionTest.do";
    document.forms[1].submit();
}//end of onClose

function onCloseHospReview()
{
	
	if(!TrackChanges()) return false;
   	onReset();
   	document.forms[1].leftlink.value="Finance";
   	document.forms[1].sublink.value="Cust. Bank Details";
   	document.forms[1].tab.value="Account Validation";
    document.forms[1].mode.value="doCloseHospReview";
    document.forms[1].action = "/ChangeIfscGeneralActionTest.do";
    document.forms[1].submit();
}//end of onClose


function onChangeQatarYN()
{
	document.forms[1].reforward.value="POL";
    document.forms[1].mode.value="doChangeQatarYN";
	document.forms[1].action="/ChangeIfscGeneralActionTest.do";
	document.forms[1].submit();
	
}

function swiftCodeValidation()
{
	var ifsc = document.forms[1].ifsc.value;
	if(ifsc.indexOf(' ') >= 0)
	{
		alert("Space not allowed in the Swift Code.");
		document.forms[1].ifsc.value="";
		document.forms[1].ifsc.focus();
		return false;
	}
	
	if(ifsc.length < 6)
	{
		alert("Swift Code should be minimum 6 digits.");
		document.forms[1].ifsc.value="";
		return;
	}
}

function onCountryCode(Obj)
{
	var countryCode = Obj.value;
	document.forms[1].mode.value="doGetCountryCode";
	document.forms[1].action="/getTwodigitCountryCode.do?countryCode="+countryCode;
	document.forms[1].submit();
}
function showAndHideDateClaims(){
	var elementcheckedornot = document.getElementById('stopClaimsYN');
	if(elementcheckedornot.checked){
		document.getElementById("stopClaimsDateId").style.display="";
		document.getElementById("stopClaimsYN").value="Y";
	}else{
		document.getElementById("stopClaimsDateId").style.display="none";
		document.getElementById("stopClaimsYN").value="N";
		document.getElementById("stopclmsid").value="";
	}
 }

function showAndHideDatePreauth(){
	var elementcheckedornot = document.getElementById('stopPreAuthsYN');
	if(elementcheckedornot.checked){
		document.getElementById("stopPreauthDateId").style.display="";
		document.getElementById("stopPreAuthsYN").value="Y";
	}else{
		document.getElementById("stopPreauthDateId").style.display="none";
		document.getElementById("stopPreAuthsYN").value="N";
		document.getElementById("stopPreauthId").value="";
	}
 }