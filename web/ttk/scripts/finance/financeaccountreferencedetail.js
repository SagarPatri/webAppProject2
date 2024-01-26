/*
		 * This java script is added for cr koc 1103
		 * added eft
		 * java script for the account reference details screen in the CustomerBankDetails 
		 */
//function to submit the screen
function onUserSubmit()
{	
	if(!JS_SecondSubmit)
	{		
			trimForm(document.forms[1]);
			document.forms[1].mode.value="doReferenceDetail";
			if(document.forms[1].switchType.value === "PTNR")
			document.forms[1].action="/PartnerBankAcctSaveGeneralActionTestRef.do";
			else
			document.forms[1].action="/SaveBankAcctRefGeneralActionTest.do";
			JS_SecondSubmit=true;
		 	document.forms[1].submit();
 	}//end of if(!JS_SecondSubmit)
}//end of onUserSubmit()

function onClose()
{
	
	if(!TrackChanges()) return false;
   	onReset();
    document.forms[1].mode.value="doClose";
    if(document.forms[1].switchType.value === "PTNR")
    	document.forms[1].action = "/PartnerBankAcctSaveGeneralActionClose.do";
    else
    document.forms[1].action = "/ReferenceBankAcctGeneralActionTest.do";
    document.forms[1].submit();
}//end of onClose

function onClosePatReview(){
  	document.forms[1].mode.value="doClosePartner";
    document.forms[1].action = "/ChangeIfscGeneralActionTest.do";
	document.forms[1].submit();
}

function onCloseReview()
{
	
    	document.forms[1].mode.value="doViewHospReviewList";
    	document.forms[1].action="/CustomerBankDetailsAccountList.do";
    	document.forms[1].submit();
}//end of onChangeState()

function onUserReviewSubmit()
{
    if(!JS_SecondSubmit)
    {
     if(document.forms[1].switchHospOrPtr.value === "HOSP"){
        trimForm(document.forms[1]);
        document.forms[1].mode.value="doReferenceReviewDetail";
document.forms[1].action="/SaveCustomerBankDetailsAccount.do";
        JS_SecondSubmit=true;
           document.forms[1].submit();
         }
         if(document.forms[1].switchHospOrPtr.value === "PTNR"){
                trimForm(document.forms[1]);
document.forms[1].mode.value="doPartnerReferenceReviewDetail";
document.forms[1].action="/SaveCustomerBankDetailsAccount.do";
                JS_SecondSubmit=true;
                   document.forms[1].submit();
                }
     }//end of if(!JS_SecondSubmit)
}//end of onUserSubmit()


function onReset()
{
    document.forms[1].reset();
    document.frmCustomerBankAcctGeneral.elements['refDate'].value="";
    document.frmCustomerBankAcctGeneral.elements['modReson'].selectedIndex=0;
    document.frmCustomerBankAcctGeneral.elements['refNmbr'].value="";
    document.frmCustomerBankAcctGeneral.elements['remarks'].value="";
}//end of onReset()