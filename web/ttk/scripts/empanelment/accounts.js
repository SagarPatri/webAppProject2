//java script for the accounts screen in the empanelment of hospital flow.
function isEmpanelCharged(strOnSubmit)
{
	 if(document.getElementById("emplFeeChrgYn").checked || strOnSubmit=="true")
	 {
	 	document.forms[1].payOrdType.disabled=false;
	 	document.forms[1].payOrdNmbr.disabled=false;
	 	document.forms[1].payOrdAmountWord.disabled=false;
	 	document.forms[1].payOrdRcvdDate.disabled=false;
	 	document.forms[1].payOrdBankName.disabled=false;
	 	document.forms[1].chkIssuedDate.disabled=false;
	 	document.forms[1].elements['payOrdBankAddressVO.address1'].disabled=false;
	 	document.forms[1].elements['payOrdBankAddressVO.address2'].disabled=false;
	 	document.forms[1].elements['payOrdBankAddressVO.address3'].disabled=false;
	 	document.forms[1].elements['payOrdBankAddressVO.cityCode'].disabled=false;
	 	document.forms[1].elements['payOrdBankAddressVO.stateCode'].disabled=false;
	 	document.forms[1].elements['payOrdBankAddressVO.pinCode'].disabled=false;
	 	document.forms[1].elements['payOrdBankAddressVO.countryCode'].disabled=false;
	 }//end of if(document.getElementById("emplFeeChrgYn").checked || strOnSubmit=="true")
	 else
	 {
	 	document.forms[1].payOrdType.value="";
	 	document.forms[1].payOrdNmbr.value="";
	 	document.forms[1].payOrdAmountWord.value="";
	 	document.forms[1].payOrdRcvdDate.value="";
	 	document.forms[1].payOrdBankName.value="";
	 	document.forms[1].chkIssuedDate.value="";
	 	document.forms[1].elements['payOrdBankAddressVO.address1'].value="";
	 	document.forms[1].elements['payOrdBankAddressVO.address2'].value="";
	 	document.forms[1].elements['payOrdBankAddressVO.address3'].value="";
	 	document.forms[1].elements['payOrdBankAddressVO.cityCode'].value="";
	 	document.forms[1].elements['payOrdBankAddressVO.stateCode'].value="";
	 	document.forms[1].elements['payOrdBankAddressVO.pinCode'].value="";
	 	document.forms[1].elements['payOrdBankAddressVO.countryCode'].value="1";

	 	document.forms[1].payOrdType.disabled=true;
	 	document.forms[1].payOrdNmbr.disabled=true;
	 	document.forms[1].payOrdAmountWord.disabled=true;
	 	document.forms[1].payOrdRcvdDate.disabled=true;
	 	document.forms[1].payOrdBankName.disabled=true;
	 	document.forms[1].chkIssuedDate.disabled=true;
	 	document.forms[1].elements['payOrdBankAddressVO.address1'].disabled=true;
	 	document.forms[1].elements['payOrdBankAddressVO.address2'].disabled=true;
	 	document.forms[1].elements['payOrdBankAddressVO.address3'].disabled=true;
	 	document.forms[1].elements['payOrdBankAddressVO.cityCode'].disabled=true;
	 	document.forms[1].elements['payOrdBankAddressVO.stateCode'].disabled=true;
	 	document.forms[1].elements['payOrdBankAddressVO.pinCode'].disabled=true;
	 	document.forms[1].elements['payOrdBankAddressVO.countryCode'].disabled=true;
	 }//end of else
}//end of isEmpanelCharged(strOnSubmit)

function showCalendar()
{
	if(document.getElementById("emplFeeChrgYn").checked )
		show_calendar('CalendarObjectempDate1','frmAccounts.payOrdRcvdDate',document.frmAccounts.payOrdRcvdDate.value,'',event,148,178);
}//end of showCalendar

function showCalendar1()
{
	if(document.getElementById("emplFeeChrgYn").checked )
		show_calendar('CalendarObjectempDate2','frmAccounts.chkIssuedDate',document.frmAccounts.chkIssuedDate.value,'',event,148,178);
}//end of showCalendar1

//function to submit the screen
function onUserSubmit()
{
 if(!JS_SecondSubmit)
 {	
	if(document.getElementById("emplFeeChrgYn").checked )
	 {
	 	document.getElementById("emplFeeChrgYn").value="Y";
	 	document.forms[1].hidpoEmpFeeCharged.value="Y";
	 }//end of if(document.getElementById("emplFeeChrgYn").checked )
	 else
	 {
	 	document.getElementById("emplFeeChrgYn").value="N";
	 	document.forms[1].hidpoEmpFeeCharged.value="N";
	 }//end of else
	 trimForm(document.forms[1]);
	 //isEmpanelCharged("true"); commented for QATAR project as we are not using
	 
	 if(document.forms[1].partnerOrProvider.value==="Partner"){
		 
	 document.forms[1].mode.value="doSavePartnerAccount";
	 
	 }else{
		 
		 
			var ibanNum = document.forms[1].swiftCode.value;
			
			if(ibanNum.indexOf(' ') >= 0){
				alert("Plase remove the space");
				document.getElementById("swiftCode").focus();
				 return false;
			}
			
	     	var bankLocation = document.forms[1].bankAccountQatarYN.value;
	     	
	    	 if(bankLocation=="Y"&& ibanNum.length != 29){
	   		alert("IBAN Number should be 29 characters"); 
	   		document.getElementById("swiftCode").focus();
	  		 return false;
	   	 }
		 
		 document.forms[1].mode.value="doSave";
	 }
	 document.forms[1].action="/AddAccountsAction.do?partnerOrProvider="+document.forms[1].partnerOrProvider.value;
	 JS_SecondSubmit=true;
	 document.forms[1].submit();
 }//end of if(!JS_SecondSubmit)
}//end of onUserSubmit()

function onReset()
{
	if(!JS_SecondSubmit)
	 {
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{
			document.forms[1].mode.value="doView";
			document.forms[1].action="/AccountsAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
			//isEmpanelCharged(""); commented for QATAR project as we are not using
		}//end of else
	 }
}//end of onReset()

function onAccountHistory()
{
	if(!TrackChanges()) return false;

    document.forms[1].mode.value="doDefault";
    document.forms[1].child.value="AccountsHistory";
    document.forms[1].action="/AccountsHistoryAction.do";
    document.forms[1].submit();
}//end of onAccountHistory()

function onChangeState(focusid)
{
    	var partnerOrProvider = document.forms[1].partnerOrProvider.value;
    	document.forms[1].mode.value="doChangeState";
    	document.forms[1].focusID.value=focusid;
    	document.forms[1].action="/AccountsAction.do?partnerOrProvider="+partnerOrProvider;
    	document.forms[1].submit();
}//end of onChangeState()



function onClose()
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/AccountsAction.do";
	document.forms[1].submit();
}
function onChangeBank(focusid)
{
    	document.forms[1].mode.value="doChangeBank";
    	document.forms[1].focusID.value=focusid;
    	document.getElementById("state3").value="";
    	document.forms[1].action="/AccountsAction.do";
    	document.forms[1].submit();
}//end of onChangeState()

function onChangeCity(focusid)
{
	    document.forms[1].mode.value="doChangeCity";
	    document.forms[1].focusID.value=focusid;
	    document.getElementById("state4").value="";
    	document.forms[1].action="/AccountsAction.do";
    	document.forms[1].submit();
}//end of onChangeState()

function onChangeQatarYN()
{
    document.forms[1].mode.value="doChangeQatarYN";
	document.forms[1].action="/AccountsAction.do";
	document.forms[1].submit();
	
}

function swiftCodeValidation()
{
	var ifsc = document.forms[1].ibanNumber.value;
	if(ifsc.indexOf(' ') >= 0)
	{
		alert("Space not allowed in the Swift Code.");
		document.forms[1].ibanNumber.value="";
		document.forms[1].ibanNumber.focus();
		return false;
	}
	
	if(ifsc.length < 6)
	{
		alert("Swift Code should be minimum 6 digits.");
		document.forms[1].ibanNumber.value="";
		return;
	}
}