//java script for the invoice details screen in the finance flow.

function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
	{
		document.forms[1].tab.value="Embassy Details";
  		document.forms[1].mode.value="doReset";
		document.forms[1].action="/ChangeIfscGeneralActionTest.do";
		document.forms[1].submit();	
 	}
 	else
 	{
 		document.forms[1].reset(); 			
 	}
}//end of Reset()

function onSave()
{
	trimForm(document.forms[1]);
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
			if(swiftcode != ""  && IbanNo != ""){
				if(IbanNo5to8Digit != swiftcode1to4Digit){
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
		
 	document.forms[1].tab.value="Embassy Details";
	document.forms[1].mode.value="doSaveEmbassy";
	document.forms[1].action="/SaveEmbassyBankTest.do";
	JS_SecondSubmit=true;
	document.forms[1].submit(); 
		
	}//end of if(!JS_SecondSubmit)
}//end of onSave()

// function to close the invoice details screen.
function onCloseEmbassy()
{
	if(!TrackChanges()) return false;
   	//onReset();
   	document.forms[1].leftlink.value="Finance";
   	document.forms[1].sublink.value="Cust. Bank Details";
   	document.forms[1].tab.value="Search";
    document.forms[1].mode.value="doCloseEmbassy";
    document.forms[1].action = "/ChangeIfscGeneralActionTest.do";
    document.forms[1].submit();
}//end of Close()


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


function onChangeQatarYN()
{
	
	document.forms[1].reforward.value="EMBSY";
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
	document.forms[1].action="/getCountryCodeForEmbassy.do?countryCode="+countryCode;
	document.forms[1].submit();
}

