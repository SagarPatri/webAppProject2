//javascript used in preauthgeneral.jsp of Preauth flow
 var xhttp = new XMLHttpRequest();
 function ConvertToUpperCase(charObj)
 {
     charObj.value=charObj.value.toUpperCase();
 } 
 function setProviderID(providerID,providerName){	   
	    document.getElementById("providerId").value=providerID;
	    document.getElementById("providerName").value=providerName;
	    var spDiv=document.getElementById("spDivID");
	    
	    spDiv.innerHTML="";
	    spDiv.style.display="none";
	} 
 
 function onchangeProcessType()
 {
	 var processType = document.getElementById("processType").value ; 

	 if(processType == "DBL")
		 {
		 	document.getElementById("requestedAmountcurrencyType").value="";
		 	document.getElementById("partnerSelectLabel").style.display="";
		 	document.getElementById("partnerSelectBox").style.display="";
		 }else{
			 document.getElementById("requestedAmountcurrencyType").value="QAR";
			 document.getElementById("partnerName").value="";
			 document.getElementById("partnerSelectLabel").style.display="none";
			 document.getElementById("partnerSelectBox").style.display="none";
		 }
 }
 
 function QARValidation() 
 {
 if(document.getElementById("requestedAmountcurrencyType").value=="QAR")
 	{
 	document.getElementById("conversionRate").value="";
 	document.getElementById("convertedAmount").value=document.getElementById("requestedAmount").value;
 	document.getElementById("conversionRate").className="textBox textBoxSmall textBoxDisabled";
 	document.getElementById("conversionRate").readOnly="true";
 	}
 else
 	{
 	document.getElementById("conversionRate").className="textBox textBoxSmall";
 	document.getElementById("conversionRate").readOnly="";
 	}

 }
 function clearField()
 {
 	var crate = document.forms[1].conversionRate.value;
 	if(crate == "")
 	{
 		document.forms[1].convertedAmount.value="";
 		
 	}else if(crate == "0")
		{
		alert("Conversion Rate should be greater than Zero");
		document.forms[1].conversionRate.value = "";
		document.forms[1].convertedAmount.value="";
		document.forms[1].focusID.value ="conversionRate";
		return false;
		}
 }
  
function spDivIDspDivIDspDivID(sObj) {
	document.getElementById("providerId").value="";
    var spDiv;
    spDiv=document.getElementById("spDivID");
    
    spDiv.innerHTML="";
    spDiv.style.display="none";
   
    if(sObj.value!==null&&sObj.value!==""){

        //document.forms[1].insuSeqID.value="";
if(xhttp!=null)xhttp = new XMLHttpRequest();

    var path="/PreauthOralAction.do?mode=doProviderSearch&providerName="+sObj.value;


  xhttp.open("POST", path, false);
  xhttp.send();
  var sData=xhttp.responseText;
  //sData=sData.trim();
  if(sData!==null&&sData!==""&&sData.length>1){
      spDiv.style.display="";

      var arrData=sData.split("|");

      if(arrData.length>0){
          var scDiv;

          for(var i=0;i<arrData.length-1;i++){

              scDiv = document.createElement('div'),

          scDiv.className = 'scDivClass';
          var idData=arrData[i].split("#");
        // var funName="setProviderID('"+idData[0]+"','"+idData[1]+"');";   
         
            scDiv.innerText=idData[1]; 
          // scDiv.setAttribute("onclick",funName);
            scDiv.onclick=function(){setProviderID(idData[0],idData[1]);};

           spDiv.appendChild(scDiv);

          }//for(var i=0;i<arrData.length-1;i++){
        }// if(arrData.length>0){
      else{
          document.getElementById("spDivID").style.display="none";
        }
}//if(sData!==null&&sData!==""&&sData.length>1){
     else {
      document.getElementById("spDivID").style.display="none";
      }
  }//if(sObj.value!==null&&sObj.value!==""){
  }//function onInsSearch(sObj) {



function clearAuthorization()
{
	if(!document.forms[1].authNbr.value=='')
	{
		document.forms[1].mode.value="doClearAuthorization";
		document.forms[1].action="/PreAuthGeneralAction.do";
		document.forms[1].submit();
	}//end of if(!document.forms[1].authNbr.value=='')
}//end of clearAuthorization()
function onEdit() 
{
	document.getElementById("diag").style.display="";
	document.getElementById("ser").style.display="";
	document.getElementById("approved").style.display="";
	document.getElementById("link").style.display="none";
	document.getElementById("services").readOnly = true;
	document.getElementById("diagnosis").readOnly = true;
	document.getElementById("apprAmount").readOnly = true;
	//document.forms[1].denialRemarks.readOnly=true;
	//document.forms[1].approvedAmount.readOnly=true;
}
function addActivityDetails()
{
    document.forms[1].rownum.value = "";
    document.forms[1].tab.value ="System Preauth Approval";
    document.forms[1].mode.value = "addActivityDetails";
    document.forms[1].action = "/PreAuthGeneralAction.do";  
    document.forms[1].submit();
}//end of function addPreAuth()

function onPrevHospitalization()
{
	if(document.forms[1].authNbr.value=='')
	{
		document.forms[1].mode.value="doPrevHospitalization";
		document.forms[1].focusID.value="PrevHospId";
		document.forms[1].action="/PreAuthGeneralAction.do";
		document.forms[1].submit();
	}//end of if(document.forms[1].authNbr.value=='')
}//end of onPrevHospitalization()

function selectEnrollmentID()
{
	 if(trim(document.forms[1].elements['claimantDetailsVO.enrollmentID'].value).length>0)
	 {
		var message=confirm('You are fetching new data. Do you want to continue?');
		if(message)
		{
			document.forms[1].mode.value="doSelectEnrollmentID";
			document.forms[1].child.value="EnrollmentList";
		 	document.forms[1].action="/PreAuthGeneralAction.do";
		 	document.forms[1].submit();
		}//end of if(message)
	 }else
	 {
	 	 document.forms[1].mode.value="doSelectEnrollmentID";
	 	 document.forms[1].child.value="EnrollmentList";
		 document.forms[1].action="/PreAuthGeneralAction.do";
		 document.forms[1].submit();
	 }//end of else
}//end of selectEnrollmentID()

function clearEnrollmentID()
{
	 document.forms[1].mode.value="doClearEnrollmentID";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of clearEnrollmentID()

function selectCorporate()
{
	 document.forms[1].mode.value="doSelectCorporate";
	 document.forms[1].child.value="GroupList";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of selectCorporate()

function selectPolicy()
{
	 if(trim(document.forms[1].elements['claimantDetailsVO.policyNbr'].value).length>0)
	 {
		var message=confirm('You are fetching new data. Do you want to continue?');
		if(message)
		{
			document.forms[1].mode.value="doSelectPolicy";
			document.forms[1].child.value="PolicyList";
			document.forms[1].action="/PreAuthGeneralAction.do";
			document.forms[1].submit();
		}//end of if(message)
	 }else
	 {
	 	 document.forms[1].mode.value="doSelectPolicy";
	 	 document.forms[1].child.value="PolicyList";
	 	 document.forms[1].action="/PreAuthGeneralAction.do";
	 	 document.forms[1].submit();
	 }//end of else
}//end of selectPolicy()

function clearPolicy()
{
	 document.forms[1].mode.value="doClearInsurance";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of clearPolicy()

function selectInsurance()
{
	 document.forms[1].mode.value="doSelectInsurance";
	 document.forms[1].child.value="Insurance Company";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of selectInsurance()

function selectHospital()
{
	//OPD_4_hosptial
	if(document.forms[0].leftlink.value=="Claims")
	 {
	if( document.forms[1].elements['claimDetailVO.paymentType'].value =='HSL')
	{
		
		document.forms[1].paymentto.value =document.forms[1].elements['claimDetailVO.paymentType'].value;
	}
	else
	{
		
		document.forms[1].paymentto.value =" ";
	}
	//OPD_4_hosptial
	 }
	 document.forms[1].mode.value="doSelectHospital";
	 document.forms[1].child.value="HospitalList";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of selectHospital()

function onClearHospital()
{
	document.forms[1].mode.value="doClearHospital";
    document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
}//end of onClearHospital()

function selectAuthorization()
{
	 document.forms[1].mode.value="doSelectAuthorization";
	 document.forms[1].child.value="SelectAuthorization";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of selectHospital()

function onEnhancementAmount()
{
	 document.forms[1].mode.value="doEnhancementAmount";
	 document.forms[1].child.value="SumInsuredList";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of onEnhancementAmount()

function showhideadditionalinfo(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	var dagreeObj = document.getElementById("additionalinfo");
	dagreeObj.style.display="none";
	if(selVal == 'MAN')
	{
		dagreeObj.style.display="";
	}//end of if(selVal == 'MAN')
}//end of showhideadditionalinfo(selObj)

function onUserSubmit(){	
	 trimForm(document.forms[1]);	
	//Dental Validations
	 if(document.forms[1].benefitType.value=="DNTL")
		{
			if(document.getElementById("overJet").value!='' && 
					(document.getElementById("openbiteAntrio").value=='' && document.getElementById("openbitePosterior").value=='' 
						&& document.getElementById("openbiteLateral").value=='')){
				alert("Overjet and Open Bite should be billed together");
				return false;
			}
			if(document.getElementById("overJet").value=='' && 
					(document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!='' 
						|| document.getElementById("openbiteLateral").value!='')){
				alert("Open Bite and Overjet should be billed together");
				return false;
			}
		}
	 
	 var strPrentPreAuthSeqID = document.forms[1].parentPreAuthSeqID.value;
	 var strPreAuthNo = document.forms[1].preAuthNo.value;	 
	if(strPreAuthNo=="" ){
	 if(isFutureDate(document.forms[1].receiveDate.value)){
			alert("PreApproval Request Received Date Should Not Be Future Date!!!");
			document.forms[1].receiveDate.value='';
			document.forms[1].receiveDate.focus();
			return true;
		} if(daysRangeValidation(document.forms[1].receiveDate.value)){
			document.forms[1].receiveDate.value='';
			document.forms[1].receiveDate.focus();
			alert("Back Date allowed upto 3 Days only for PreApproval Request Received Date !!! ");
			return true;
		}/* if(isBackDate(document.forms[1].prvReceiveDate.value,document.forms[1].receiveDate.value)){
			document.forms[1].receiveDate.focus();
			alert("Enhancement received date/time should not be less than original preauth received date/time!!!");
			return true;
		}*/
	}	 
	if((strPrentPreAuthSeqID!= "")){
	 if(isFutureDate(document.forms[1].receiveDate.value)){
			alert("PreApproval Request Received Date Should Not Be Future Date!!!");
			document.forms[1].receiveDate.value='';
			document.forms[1].receiveDate.focus();
			return true;
		} if(daysRangeValidation(document.forms[1].receiveDate.value)){
			document.forms[1].receiveDate.value='';
			document.forms[1].receiveDate.focus();
			alert("Back Date allowed upto 3 Days only for PreApproval Request Received Date !!! ");
			return true;
		} /*if(isBackDate(document.forms[1].prvReceiveDate.value,document.forms[1].receiveDate.value)){
			document.forms[1].receiveDate.focus();
			alert("Enhancement received date/time should not be less than original preauth received date/time!!!");
			return true;
		}*/
	}
	
	if(document.getElementById("memberSpecificRemarksid").value !=""){

		 var message=confirm(document.getElementById("memberSpecificRemarksid").value);
		 if(message){

			   document.forms[1].validateIcdCodeYN.value="N";
			   document.forms[1].mode.value="doSave";
			   document.forms[1].action="/UpdatePreAuthGeneral.do";
			   JS_SecondSubmit=true;
			   document.forms[1].submit();
			   return;
		 }else{
			 return;
		 }
	}
	 if(!JS_SecondSubmit)
     {
		 document.forms[1].validateIcdCodeYN.value="N";
	   document.forms[1].mode.value="doSave";
	   document.forms[1].action="/UpdatePreAuthGeneral.do";
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
	 
}//end of onUserSubmit()

function onOralUserSubmit(){	
	 trimForm(document.forms[1]);
	
	 if(!JS_SecondSubmit)
    {
		 document.forms[1].validateIcdCodeYN.value="N";
	   document.forms[1].mode.value="doSave";
	   document.forms[1].action="/UpdatePreAuthOral.do";
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	} 
	
}
function addDiagnosisDetails(){
	
	if(!JS_SecondSubmit)
	 {
		var benefitType = document.forms[1].benefitType.value;
		var primary = document.forms[1].primary.value;
		var mat_icd_yn = parseInt(document.forms[1].mat_icd_yn.value);
		
		if(eventFire==true){
	if(document.forms[1].primaryAilment.checked){
		document.forms[1].primaryAilment.value='Y';
		}else{
			document.forms[1].primaryAilment.value='N';
		}
	 document.forms[1].validateIcdCodeYN.value="Y";
	 
	 if((benefitType=="OMTI" || benefitType=="IMTI")){

		   document.forms[1].mode.value="addDiagnosisDetails";
		   document.forms[1].action="/UpdatePreAuthGeneral.do?focusObj=ailmentDescription";	
		   JS_SecondSubmit=true;	
		   document.forms[1].submit();
	}else{
	 
	 if((benefitType!="OMTI" || benefitType!="IMTI") && primary == "YES" && mat_icd_yn >0){
		 var message=confirm('Please check if the benefit type is maternity as secondary has a maternity icd code');
			if(message)
			{
				   document.forms[1].mode.value="addDiagnosisDetails";
				   document.forms[1].action="/UpdatePreAuthGeneral.do?focusObj=ailmentDescription";	
				   JS_SecondSubmit=true;	  
				   document.forms[1].submit();
			}
	 }else{
	   document.forms[1].mode.value="addDiagnosisDetails";
	   document.forms[1].action="/UpdatePreAuthGeneral.do?focusObj=ailmentDescription";	
	   JS_SecondSubmit=true;	  
	   document.forms[1].submit();	
	   }
	 }
   }else{
			eventFire=true;
		}
	 }	
	}

function onReset()
{
	if(!JS_SecondSubmit)
	 {  
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{
			document.forms[1].mode.value="doReset";
			document.forms[1].action="/PreAuthGeneralAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
		//showhideCorporateDet(document.forms[1].elements['claimantDetailsVO.policyTypeID']);
		}//end of else
	 }
}//end of onReset()

function showhideCorporateDet(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	
	if(document.forms[1].flowType.value=="PRE")
	{
		var addInfo = document.getElementById("additionalinfo");
		var empNoLabel =document.getElementById("empNoLabel");
		var empNoField =document.getElementById("empNoField");
		var empName =document.getElementById("empName");
		var schemeName = document.getElementById("schemeName");
		
		if(selVal == 'COR')
		{
		   document.getElementById("corporate").style.display="";
		   //document.forms[1].elements['claimantDetailsVO.policyHolderName'].value="";
		   //document.forms[1].elements['claimantDetailsVO.policyHolderName'].disabled = true;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = true;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge textBoxDisabled";
		   if(document.forms[1].preAuthTypeID.value=="MAN")
		   {

			   addInfo.style.display="";
			   empNoLabel.style.display="";
			   empNoField.style.display="";
		       empName.style.display="";
		      //document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = false;
		   }//end of if(document.forms[1].preAuthTypeID.value=="MAN")
		    //document.forms[1].elements['additionalDetailVO.insScheme'].value="";
		   // document.forms[1].elements['additionalDetailVO.certificateNo'].value="";
		    schemeName.style.display="none";
		}//end of if(selVal == 'COR')
		
		else if(selVal == 'NCR'&& document.forms[1].preAuthTypeID.value=="MAN")
		{
			document.getElementById("corporate").style.display="";
			addInfo.style.display="";
			schemeName.style.display="";
			//document.forms[1].elements['additionalDetailVO.employeeName'].value="";
		    //document.forms[1].elements['additionalDetailVO.joiningDate'].value="";
		   	//document.forms[1].elements['additionalDetailVO.employeeNbr'].value="";
			empNoLabel.style.display="none";
			empNoField.style.display="none";
		    empName.style.display="none";
		}//end of
		
		else
		{
		   document.getElementById("corporate").style.display="none";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly =false;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge";
		   //document.forms[1].elements['additionalDetailVO.insScheme'].value="";
		   //document.forms[1].elements['additionalDetailVO.certificateNo'].value="";
		   addInfo.style.display="none";
		   empNoLabel.style.display="none";
		   empNoField.style.display="none";
		   empName.style.display="none";
		   schemeName.style.display="none";
		}//end of else
	}//end of if(document.forms[1].flowType.value=="PRE")
	else if(document.forms[1].flowType.value=="CLM")
	{
		if(selVal == 'COR')
		{
			document.getElementById("corporate").style.display="";
			//document.forms[1].elements['claimantDetailsVO.policyHolderName'].value="";
	   	    document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = true;
	   	    document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge textBoxDisabled";
		}else
		{
		   //document.getElementById("corporate").style.display="none";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly =false;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge";
		}//end of else if(document.forms[1].flowType.value=="CLM")
	}//end of else
	
	document.forms[1].mode.value="doChangePolicyType";
	document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
}//end of showhideCorporateDet(selObj)

//on Click of review button
function onReview()
{
    //trimForm(document.forms[1]);
    //if(!TrackChanges()) return false;
    if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Review");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	if(!JS_SecondSubmit)
     {
		document.forms[1].mode.value="doReviewInfo";
		document.forms[1].action="/UpdatePreAuthGeneral.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)

}//end of onReview()

function onRevert()
{
	if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Promote");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
    var message;
	if(document.forms[0].leftlink.value=='Pre-Authorization')
	{
		message=confirm('Cashless will be moved to the next level and if you do not have privileges, you may not see this Cashless.');
	}//end of if(document.forms[0].leftlink.value=='Pre-Authorization')
	else
	{
		message=confirm('Claim will be moved to the next level and if you do not have privileges, you may not see this Claim.');
	}//end of else if(document.forms[0].leftlink.value=='Pre-Authorization')
	if(message)
	{
		if(!JS_SecondSubmit)
        {
			document.forms[1].mode.value="doRevert";
			document.forms[1].action="/UpdatePreAuthGeneral.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}
	}//end of if(!JS_SecondSubmit)

}//end of onRevert()

//on Click of promote button
function onPromote()
{
    //trimForm(document.forms[1]);
    //if(!TrackChanges()) return false;
    if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Promote");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	var message;
	if(document.forms[0].leftlink.value=='Pre-Authorization')
	{
		message=confirm('Cashless will be moved to the next level and if you do not have privileges, you may not see this Cashless.');
	}//end of if(document.forms[0].leftlink.value=='Pre-Authorization')
	else
	{
		message=confirm('Claim will be moved to the next level and if you do not have privileges, you may not see this Claim.');
	}//end of else if(document.forms[0].leftlink.value=='Pre-Authorization')
	if(message)
	{
		if(!JS_SecondSubmit)
        {
			document.forms[1].mode.value="doReviewInfo";
			document.forms[1].action="/UpdatePreAuthGeneral.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
	}//end of if(message)
}//end of onPromote()

function onDiscrepancySubmit()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDiscrepancies";
	document.forms[1].child.value="Discrepancy";
	document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
}//end of function onDiscrepancySubmit()

function getPreauthReferenceNo(refNo)
{
	document.forms[1].DMSRefID.value=refNo;
}//end of getPreauthReferenceNo(refNo)

function showhideClaimSubType(selObj)
{

	var selVal = selObj.options[selObj.selectedIndex].value;
	var dagreeObj = document.getElementById("hospitalinfo");
	// Changes as per KOC1216C Change Request
	
	
	if(selVal != 'CSD')
	{
		document.getElementById('labelchange').innerHTML="Admission Date/Time:<span class=\"mandatorySymbol\">*</span>";
		document.getElementById('labelchange1').innerHTML="Discharge Date/Time:<span class=\"mandatorySymbol\">*</span>";
	    document.getElementById("domicilaryinfo").style.display="none";
	    document.getElementById("domicilaryinfocheckBox").style.display="none";
	}
	else{
		document.getElementById('labelchange').innerHTML="Treatment Commencement Date/Time:<span class=\"mandatorySymbol\">*</span>";
		document.getElementById('labelchange1').innerHTML="Treatment Completion Date/Time:<span class=\"mandatorySymbol\">*</span>";
	    document.getElementById("domicilaryinfo").style.display="";
	    document.getElementById("domicilaryinfocheckBox").style.display="";
	}
 // Changes as per KOC1216C Change Request
    if(selVal == 'OPD')
    {
          document.forms[1].elements['prevHospClaimSeqID'].disabled = true;
          document.getElementById("paymentto").style.display="";
          
    }
    else
    {
          document.forms[1].elements['prevHospClaimSeqID'].disabled = false;
          document.getElementById("paymentto").style.display="none";
    }
    if(selVal == 'CTL')
    {
		document.forms[1].elements['claimRequestAmount'].value=1;
	}
   // Changes as per KOC1216C Change Request
	if(selVal == 'CSD')
	{
	   document.getElementById("hospitalinfo").style.display="none";
	}//end of if(selVal == 'CSD')
	else
	{
	 	document.getElementById("hospitalinfo").style.display="";
	}//end of else if(selVal == 'CSD')
	//added for KOC-1273
	if((selVal == 'HCU')||(selVal == 'CTL'))
	{
	   document.getElementById("hospitalizationdetail").style.display="none";
	}//end of if(selVal == 'CSD')
	//ended
	else
	{
	 	document.forms[1].elements['prevHospClaimSeqID'].value = "";
	 	document.forms[1].elements['clmAdmissionDate'].value = "";
	 	document.forms[1].elements['clmAdmissionTime'].value = "";
	 	document.forms[1].elements['dischargeDate'].value = "";
	 	document.forms[1].elements['dischargeTime'].value = "";

	 	document.getElementById("hospitalizationdetail").style.display="";
	}//end of else if(selVal == 'CSD')

}//end of showhideClaimSubType(selObj)

function onOverride()
{
	if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Overriding");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	document.forms[1].mode.value="doOverride";
	document.forms[1].action="/OverridePreAuthGeneral.do";
	document.forms[1].submit();
}//end of onOverride()

function reassignEnrID()
{
	if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Re-associating");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	document.forms[1].mode.value="doReassociateEnrollID";
	document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
}//end of reassignEnrID()

function onDocumentLoad()
{
		selObj = document.forms[1].elements['claimantDetailsVO.policyTypeID'];
		selVal =selObj.options[selObj.selectedIndex].value;
		var addInfo = document.getElementById("additionalinfo");
		var empNoLabel =document.getElementById("empNoLabel");
		var empNoField =document.getElementById("empNoField");
		var empName =document.getElementById("empName");
		var schemeName = document.getElementById("schemeName");
		
		if(selVal == 'COR')
		{
		   document.getElementById("corporate").style.display="";
	   	   document.forms[1].elements['claimantDetailsVO.policyHolderName'].value="";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = true;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge textBoxDisabled";
		   if(document.forms[1].preAuthTypeID.value=="MAN")
		   {
			   addInfo.style.display="";
			   empNoLabel.style.display="";
			   empNoField.style.display="";
		   	   empName.style.display="";
		   }//end of if(document.forms[1].preAuthTypeID.value=="MAN")
		   schemeName.style.display="none";
		}//end of if(selVal == 'COR')
		else if(selVal == 'NCR'&& document.forms[1].preAuthTypeID.value=="MAN")
		{
			document.getElementById("corporate").style.display="";
			addInfo.style.display="";
			schemeName.style.display="";
			empNoLabel.style.display="none";
			empNoField.style.display="none";
		  	empName.style.display="none";
		}//end of
		else
		{
		   document.getElementById("corporate").style.display="none";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly =false;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge";
		   addInfo.style.display="none";
		   schemeName.style.display="none";
		   empNoLabel.style.display="none";
		   empNoField.style.display="none";
		   empName.style.display="none";
		}//end of else
}//end of onDocumentLoad()
//KOC 1285 Change Request  
function onDoctorCertificate(){
	var doctorCertificateYN=document.forms[1].elements['claimDetailVO.doctorCertificateYN'];
	 if(doctorCertificateYN.checked!=true)	 { 
		 document.forms[1].doctorCertificateYN.value="N";
	     }
	 else{
		 document.forms[1].doctorCertificateYN.value="Y";
		 }
		 
}
function onUnfreeze(){
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDefault";
	//document.forms[1].child.value="Discrepancy";
	document.forms[1].action="/FileUploadUnfreeze.do";
	document.forms[1].submit();
}
//KOC FOR Grievance
/*function call()
{
    popup = window.open('http://www.google.co.in');         
    setInterval(function() {wait();},4000);
} */  
/*function selectageID()
{
    setInterval(function() {call();},5000);
}*/
/*function wait()
{
    popup.close();
}*/
function selectageID()
{

	var gender=document.forms[1].elements['claimantDetailsVO.genderTypeID'].value;
	var age=document.forms[1].elements['claimantDetailsVO.age'].value;
	
	
	if(((gender == "MAL") && (age >= 60)) || ((gender == "FEM") && (age >= 60)))
	{
		alert("Senior Citizen � Prioritize");
	}
	
	
}//end of selectEnrollmentID()

function onInsOverrideConf(){
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDefault";
	//document.forms[1].child.value="Discrepancy";
	document.forms[1].action="/FileInsOverrideConf.do";
	document.forms[1].submit();
}
function setValidateIconTitle(){
	document.getElementById('memberIdResult2').innerHTML='';
	document.getElementById('memberSeqID').value='';
	document.getElementById('patientName').value='';
	document.getElementById('memberAge').value='';                                	
	document.getElementById('emirateId').value='';
	document.getElementById('payerId').value='';
	document.getElementById('insSeqId').value='';
	document.getElementById('payerName').value='';
	document.getElementById('policySeqId').value='';
	document.getElementById('patientGender').value='';
	document.getElementById('policyNumber').value='';
	document.getElementById('corporateName').value='';
	document.getElementById('policyStartDate').value='';
  	document.getElementById('policyEndDate').value='';
  	document.getElementById('nationality').value='';
  	document.getElementById('sumInsured').value='';
  	document.getElementById('availableSumInsured').value='';
	
   var preauthMode=document.getElementById("preauthMode").value;
   if(preauthMode==="DHP"){
	   document.getElementById("validateIcon").title="Validate Member Id";
       }else{
    	   document.getElementById("validateIcon").title="Validate OTP";
           }
	}

function selectDiagnosisCode(){
	  document.forms[1].mode.value="doGeneral";
	   document.forms[1].reforward.value="diagnosisSearch";
	   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=icdCode";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
}
function editDiagnosisDetails(rownum){
	if(!JS_SecondSubmit)
	 {	
		document.forms[1].rownum.value=rownum;
	   document.forms[1].mode.value="editDiagnosisDetails";
	   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=ailmentDescription";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
	 }		
	}

function deleteDiagnosisDetails(rownum){
 if(confirm("Are You Sure You Want To Delete Diagnosis Details!")){
	if(!JS_SecondSubmit){	
		document.forms[1].rownum.value=rownum;
		document.forms[1].child.value="DeleteDiagnosisDetails";
	   document.forms[1].mode.value="deleteDiagnosisDetails";
	   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=ailmentDescription";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
	 }	
	 }	
	}	

function addActivityDetails(){	
	if(!JS_SecondSubmit){     
	document.forms[1].mode.value="doGeneral";
    document.forms[1].reforward.value="addActivityDetails";		        	       
    document.forms[1].action = "/PreAuthGeneralAction.do?focusObj=medicalOpinionRemarks";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}
function editActivityDetails(activityDtlSeqId1){
	   if(!JS_SecondSubmit){ 
		    document.forms[1].reforward.value="viewActivityDetails";	
			document.forms[1].activityDtlSeqId.value=activityDtlSeqId1;
			 //document.forms[1].child.value="EditActivityDetails";
			document.forms[1].mode.value="doGeneral";
			document.forms[1].action="/PreAuthGeneralAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		 }
			}
function deleteActivityDetails(activityDtlSeqId1){
	   if(confirm("Are You Sure You Want To Delete Activity Details!")){
		if(!JS_SecondSubmit){	
		   document.forms[1].activityDtlSeqId.value=activityDtlSeqId1;
		   document.forms[1].child.value="DeleteActivityDetails";
		   document.forms[1].mode.value="deleteActivityDetails";
		   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=medicalOpinionRemarks";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();
		 }		
	    }
		}	

function calculatePreauthAmount(){	
	
	
	var alhalliPopUpFlg =document.forms[1].benefitPopUpAlhalli.value;  
    if(alhalliPopUpFlg=="Y"){
    	alert("!Benefit type selected does not match with principal ICD, Please select correct benefit type");
    	return false;
    }
    else{
	
	if(document.getElementById("memberSpecificRemarksid").value !=""){

		 var message=confirm(document.getElementById("memberSpecificRemarksid").value);
		 if(message){
			    document.forms[1].mode.value="calculatePreauthAmount";
				document.forms[1].action="/PreAuthGeneralAction.do?focusObj=medicalOpinionRemarks";
				JS_SecondSubmit=true;
				document.forms[1].submit();
				document.forms[1].calculate.value	=	"Please Wait...!!";
				document.forms[1].calculate.disabled	=	true;
			   return;
		 }else{
			 return;
		 }
	}
	}
	 if(!JS_SecondSubmit){ 
				document.forms[1].mode.value="calculatePreauthAmount";
				document.forms[1].action="/PreAuthGeneralAction.do?focusObj=medicalOpinionRemarks";
				JS_SecondSubmit=true;
				document.forms[1].submit();
				
	   }
	
}
function saveAndCompletePreauth()
{
	
	var v = document.getElementById("vipmemberuseraccesspermissionflagid").value;
	var userSeqID = document.getElementById("userSeqID").value;
	var assigntouserseqid = document.getElementById("assigntouserseqid").value;
	var vipYorN = document.getElementById("vipYorN").value;
	var status =   document.getElementById("preauthStatus").value;
	var preReason =   document.getElementById("reason").value;
    var externalPatYN =  document.forms[1].external_pat_yn.value;;
	
	if(status === 'WIP'){
		if(preReason =="" || preReason == null){
			alert("Reason is require");
			return false;
		}
		
	}
  if((document.getElementById("vipmemberuseraccesspermissionflagid").value !="Y") && (document.forms[1].preauthStatus.value=="REJ") && (document.getElementById("userSeqID").value == document.getElementById("assigntouserseqid").value) && (document.getElementById("vipYorN").value == "Yes")){
		
		if(!confirm("VIP Member Pre-Approval can be rejected only through\n Authorized user.  Please assign Pre-approval to authorized user.")) return ;
		document.forms[1].mode.value="doAssign";
	  	document.forms[1].child.value="Assign";
	  	document.forms[1].action="/AssignToAction.do";
	  	document.forms[1].submit();
	  	
		
  }else if((document.getElementById("vipmemberuseraccesspermissionflagid").value =="Y") && (document.forms[1].preauthStatus.value=="REJ") && (document.getElementById("userSeqID").value == document.getElementById("assigntouserseqid").value) && (document.getElementById("vipYorN").value == "Yes")){
	 
	  if(!confirm("VIP Member Pre Approval rejection. \nPlease confirm before rejecting the request."))return ;
	  

		if(document.forms[1].approvedAmount.value>0)
		{
			Alert.render('<table><tr><td align=center>Approved Amount is not equal to Zero. </td></tr><tr><td align=center>Pre-approval Cannot be Rejected.</td></tr><tr><td align=center>However, To Proceed With Entire Pre-approval  Rejection.<br/> Please &nbsp;<a  onclick=Alert.ok()></u><b><u>Click here</b></a></td><tr>');
		}
		else
		{
			if(!JS_SecondSubmit)
			 {
				   //    if(!confirm("Do you want to complete the VIP pre-Approval?"))return ;
						document.forms[1].mode.value="saveAndCompletePreauth";
						document.forms[1].child.value="PreauthFinalSavePermission"; 
						document.forms[1].action="/PreAuthGeneralAction.do";
						JS_SecondSubmit=true;
						document.forms[1].submit();
			 }
		}
	
  }
  
  else if(document.forms[1].preauthStatus.value=="REJ")
	{
	  	 
	  if(document.forms[1].approvedAmount.value>0)
		{
			Alert.render('<table><tr><td align=center>Approved Amount is not equal to Zero. </td></tr><tr><td align=center>Pre-approval Cannot be Rejected.</td></tr><tr><td align=center>However, To Proceed With Entire Pre-approval  Rejection.<br/>\n &nbsp;  Please &nbsp;<a  onclick=Alert.ok()></u><b><u>Click here</b></a></td><tr>');
		}
		else
		{
			if(!JS_SecondSubmit)
			 {
				       if(!confirm("Do you want to complete the pre-Approval?"))return ;
						document.forms[1].mode.value="saveAndCompletePreauth";
						document.forms[1].child.value="PreauthFinalSavePermission"; 
						document.forms[1].action="/PreAuthGeneralAction.do";
						JS_SecondSubmit=true;
						document.forms[1].submit();
			 }
		}
	}
  
  else if(document.forms[1].preauthStatus.value=="PCN")
	{
	  	 
	  if(externalPatYN=="Y")
		{
			alert("Kindly do not cancel the Preauth as it is a mandatory preapproval");
			return false;
		}
		else
		{
			if(!JS_SecondSubmit)
			 {
				       if(!confirm("Do you want to complete the pre-Approval?"))return ;
						document.forms[1].mode.value="saveAndCompletePreauth";
						document.forms[1].child.value="PreauthFinalSavePermission"; 
						document.forms[1].action="/PreAuthGeneralAction.do";
						JS_SecondSubmit=true;
						document.forms[1].submit();
			 }
		}
	}
	else
	{
		if(!JS_SecondSubmit)
		 {
			       if(!confirm("Do you want to complete the pre-Approval?"))return ;
					document.forms[1].mode.value="saveAndCompletePreauth";
					document.forms[1].child.value="PreauthFinalSavePermission";  
					document.forms[1].action="/PreAuthGeneralAction.do";
					JS_SecondSubmit=true;
					document.forms[1].submit();
		 }
	}
} // end of saveAndCompletePreauth()

function CustomAlert()
{
    this.render = function(dialog)
    {
        var winW = window.innerWidth;
        var winH = window.innerHeight;
        var dialogoverlay = document.getElementById('dialogoverlay');
        var dialogbox = document.getElementById('dialogbox');
        dialogoverlay.style.display = "block";
        dialogbox.style.display = "block";
//        dialogbox.style = "width: 400px !important";
        document.getElementById('dialogboxbody').innerHTML =dialog;
        
       document.getElementById('dialogboxfoot').innerHTML = '<button onclick="Alert.cancel()">Cancel</button>'; 
    };
    
	this.ok = function()
	{
		document.getElementById('dialogbox').style.display = "none";
		document.getElementById('dialogoverlay').style.display = "none";
		 popupWindow=window.open("/PreauthDiagnosisDetails.do?preAuthSeqID="+document.getElementById("preAuthSeqID").value+"&medicalOpinionRemarks="+document.forms[1].medicalOpinionRemarks.value+"&overrideRemarks="+document.forms[1].overrideRemarks.value,"PREAUTH","toolbar=no,scrollbars=yes,status=no,menubar=0");
		  document.onmousedown=focusPopup; 
		  document.onkeyup=focusPopup; 
		  document.onmousemove=focusPopup;
	};
	
	this.cancel = function()
	{
		
		document.getElementById('dialogbox').style.display = "none";
		document.getElementById('dialogoverlay').style.display = "none";
	};
}
var Alert = new CustomAlert();

function generatePreAuthLetter(){
	 var preauthCompleteStatus=document.forms[1].preauthCompleteStatus.value;
	 if(preauthCompleteStatus!="Y"){
	      alert("Please save and complete the pre-Approval, before Generating Letter");
	      return false;
	    }//end of if(TC_GetChangedElements().length>0)								   
	if(TC_GetChangedElements().length>0)
   {
     alert("Please save the modified data, before Generating Letter");
     return false;
   }//end of if(TC_GetChangedElements().length>0)
   
   var statusID=document.forms[1].preauthStatus.value;
     var parameterValue="|"+document.forms[1].preAuthSeqID.value+"|"+statusID+"|PRE|";
     var parameter = "";
     var authno = document.forms[1].authNum.value;
     if(statusID === 'APR')
     {  
	      	parameter = "?mode=generatePreauthLetter&reportType=PDF&parameter="+parameterValue+"&fileName=generalreports/AuthAprLetter.jrxml&reportID=AuthLetter&authorizationNo="+authno;
         
     }//end of if(statusID == 'APR')
     else if(statusID === 'REJ')
     {
           parameter = "?mode=generatePreauthLetter&reportType=PDF&parameter="+parameterValue+"&fileName=generalreports/AuthRejLetter.jrxml&reportID=AuthLetter&authorizationNo="+authno;
     }//end of else	
     else if(statusID === 'PCN')
     {
          alert("You can not generate letter for Cancel Preauth");
          return false;
     }
  var openPage = "/PreAuthGeneralAction.do"+parameter;
  var w = screen.availWidth - 10;
  var h = screen.availHeight - 49;
  var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  window.open(openPage,'',features);
 }	

function sendPreAuthLetter(){
	  if(!JS_SecondSubmit){			 				   
		   document.forms[1].mode.value="sendPreAuthLetter";
		   document.forms[1].action="/PreAuthGeneralAction.do";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();		 				   
	  }
}
function addPreauthShortFalls(){
    document.forms[1].mode.value="doGeneral";
    document.forms[1].reforward.value="preauthshortfalls";		        	       
    document.forms[1].action = "/PreAuthGeneralAction.do";    
    document.forms[1].submit();
}//end of addPreauthShortFalls()

function doViewShortFalls(preauthSeqId1,shortFallSeqId1){
	if(!JS_SecondSubmit)
	 {	
		document.forms[1].reforward.value="viewShortfalls";        	       
		document.forms[1].mode.value="doGeneral";
		document.forms[1].shortFallSeqId.value=shortFallSeqId1;
		document.forms[1].preAuthSeqID.value=preauthSeqId1;
		document.forms[1].action = "/PreAuthGeneralAction.do";  
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of doViewShortFall()


function deleteShortfallsDetails(preauthSeqId1,shortFallSeqId1){
	   if(confirm("Are You Sure You Want To Delete Shortfalls Details!")){						   
		if(!JS_SecondSubmit){								
		   document.forms[1].preAuthSeqID.value=preauthSeqId1;
		   document.forms[1].shortFallSeqId.value=shortFallSeqId1;
		   document.forms[1].mode.value="deleteShortfallsDetails";
		   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=medicalOpinionRemarks";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();						   
		     }		
	      }
		}

function setMaternityMode(){
		   document.forms[1].mode.value="setMaternityMode";
		   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=benefitType";			  	   
		   document.forms[1].submit();	 				   
}


function selectClinician(){
	   var networkProviderType=document.forms[1].networkProviderType.value;
		  if(networkProviderType==="N"){
            alert("Sorry! You Selected Network Type No");
            return ;
			  }
				  document.forms[1].mode.value="doGeneral";
				  document.forms[1].reforward.value="clinicianSearch";
				  document.forms[1].action="/PreAuthGeneralAction.do";	
				   document.forms[1].submit();	
	}

function selectProvider(){
	   
	  var networkProviderType=document.forms[1].networkProviderType.value;
	  if(networkProviderType==="N"){
         alert("Sorry! You Selected Network Type No");
         return ;
		  }
		  document.forms[1].mode.value="doGeneral";
		  document.forms[1].reforward.value="providerSearch";
		  document.forms[1].action="/PreAuthGeneralAction.do";	
		  document.forms[1].submit();
}
function selectMember(element){
	if(!JS_SecondSubmit){ 
	  
		  document.forms[1].mode.value="doGeneral";
		  document.forms[1].reforward.value="memberSearch";
		  document.forms[1].action="/PreAuthGeneralAction.do";	
		  JS_SecondSubmit=true;
		  element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
		  document.forms[1].submit();
	}
}

function deleteMember(){
	if(!JS_SecondSubmit){ 
	  
		  document.forms[1].mode.value="deleteMember";	
		  document.forms[1].action="/PreAuthGeneralAction.do";	
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
	}
}

function viewHistory(){
	document.forms[1].mode.value = "doViewHistory";		
	document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
	}
	
	function setEndDate(){
		//document.forms[1].dischargeDate.readOnly=false;
		var encounterType=document.forms[1].encounterTypeId.value;
		if(encounterType==1||encounterType==2||encounterType==="1"||encounterType==="2"){
			document.forms[1].dischargeDate.value=document.forms[1].admissionDate.value;
			document.forms[1].dischargeTime.value=document.forms[1].admissionTime.value;
			document.forms[1].dischargeDay.selectedIndex=document.forms[1].admissionDay.selectedIndex;
			//document.forms[1].dischargeDate.readOnly=true;
			//document.forms[1].dischargeTime.readOnly=true;
			//document.forms[1].dischargeDay.readOnly=true;						
			}
		}
	function setOralMaternityMode(){
		   document.forms[1].mode.value="setMaternityMode";
		   document.forms[1].action="/PreauthOralAction.do";			  	   
		   document.forms[1].submit();	 				   
	}
	function setDateMode(){			
		var encounterType=document.forms[1].encounterTypeId.value;
		if(encounterType==1||encounterType==2||encounterType==="1"||encounterType==="2"){
			document.forms[1].dischargeDate.value=document.forms[1].admissionDate.value;
			document.forms[1].dischargeTime.value=document.forms[1].admissionTime.value;
			document.forms[1].dischargeDay.selectedIndex=document.forms[1].admissionDay.selectedIndex;
			//document.forms[1].dischargeDate.readOnly=true;
			//document.forms[1].dischargeTime.readOnly=true;
			//document.forms[1].dischargeDay.readOnly=true;					
		}else{
			document.forms[1].dischargeDate.value="";
			document.forms[1].dischargeTime.value="";
			document.forms[1].dischargeDay.value="AM";
			//document.forms[1].dischargeDate.readOnly=false;	
			//document.forms[1].dischargeTime.readOnly=false;
			//document.forms[1].dischargeDay.readOnly=false;					
			}
		}

	function checkCalender(){			
		/*var encounterType=document.forms[1].encounterTypeId.value;
		if(encounterType==1||encounterType==2||encounterType==="1"||encounterType==="2"){            
			//show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.dischargeDate',document.frmPreAuthGeneral.dischargeDate.value,'',event,148,178);return false;
		}else{				
			show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.dischargeDate',document.frmPreAuthGeneral.dischargeDate.value,'',event,148,178);return false;
			}*/
		show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.dischargeDate',document.frmPreAuthGeneral.dischargeDate.value,'',event,148,178);return false;

		}
	
	function setProviderMode(){	
		   document.forms[1].mode.value="setProviderMode";
		   document.forms[1].action="/PreAuthGeneralAction.do";			    
		   document.forms[1].submit();
		}
	function overrideActivityDetails(activityDtlSeqId1){
		var complYN=document.forms[1].preauthCompleteStatus.value;
		var revertFalg = document.forms[1].revertFlag.value;		
		if(revertFalg === 'N'){
			alert("Please Calculate Before Override");
			return false;	
		}
		if(complYN==="Y"){
			alert("Completed  Pre-Approval Can Not Modify");
			return;
		}
		
		if(!JS_SecondSubmit){ 
			 if(!confirm("You Want Override This Activity Code Details?")) return;
			    document.forms[1].reforward.value="overrideActivityDetails";	
				document.forms[1].activityDtlSeqId.value=activityDtlSeqId1;
				document.forms[1].override.value="Y";
				var rsData=false;
				var restMsg="";
				//document.forms[1].child.value="Override";
				$(document).ready(function () {
					$.ajax({
			            url :"/asynchronAction.do?mode=checkAccessForOverride&activityDtlSeqId="+activityDtlSeqId1,
			            dataType:"text",
						type:"POST",
						async:false,
					    context:document.body,
			            success : function(responseData) {
			            	var respData=[];
			            	respData=responseData.split("@");
			            	rsData=respData[0];
			            	restMsg=respData[1];
			              }//success data
					 });//$.ajax(	
					});
				if(rsData=='false'){
					confirm("User Override Option is restricted for this denial code");
				}else{
					document.forms[1].mode.value="doGeneral";
					document.forms[1].action="/PreAuthGeneralAction.do";
					JS_SecondSubmit=true;
					document.forms[1].submit();
				}
				
			 }
		}
	
	function overridPreAuthDetails(){	
		
		
		if(!confirm("You Want Override PreAuth Details?")) return;	
		else{
			/*var overrRemarks=prompt("Enter Override Remarks","");
			overrRemarks =jQuery.trim(overrRemarks);
			if(overrRemarks==null ||overrRemarks==="" || overrRemarks.length ==0){
			 alert("Override Remarks Are Required");
			 return;
		 }else if(overrRemarks.length<20){
			 alert("Override Remarks Should Not Less Than 20 Characters");
			 return;
		 }
		if(!JS_SecondSubmit)
			{  
				document.forms[1].overrideRemarks.value=overrRemarks;
				document.forms[1].mode.value="overridPreAuthDetails";
				document.forms[1].action="/PreAuthGeneralAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}*/
			
			  popupWindow=window.open("/PreAuthGeneralOverrideAction.do?mode=doOverridePreauth","PREAUTH","toolbar=no,scrollbars=yes,status=no,menubar=0,width=470,height=270");
			  document.onmousedown=focusPopup; 
			  document.onkeyup=focusPopup; 
			  document.onmousemove=focusPopup;
		}
	}  
	
	function doViewProviderDocs(){
		
		document.forms[1].mode.value="doViewProviderDocs";
		document.forms[1].action="/PreAuthGeneralAction.do";			
		document.forms[1].submit();
	
}
  function isZero(obj){
	  var re = /^[0-9]*\.*[0-9]*$/;
	  var val=obj.value;
	  val=(val==null||val===""||!re.test(val)||val==="0")?0:val;
	  
	  if(parseInt(val)<1){
         alert("Enter Greater Than Zero");
         obj.value="";
         obj.focus();
		  }
	  }
  
  
  function uploadToDhpo(){
				
		if(!JS_SecondSubmit){ 					
				document.forms[1].child.value="UploadToDhpo";
				document.forms[1].mode.value="uploadToDhpo";
				document.forms[1].action="/PreAuthGeneralAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
		}
	}
  function viewDhpoLogs(){				
				document.forms[1].mode.value="viewDhpoLogs";
				document.forms[1].action="/PreAuthGeneralAction.do";				
				document.forms[1].submit();
		
	}
  
  
//end date validation
  function endDateValidation()
  {
  	var startDate =document.getElementById("admissionDate").value;    	
      var endDate=document.getElementById("dischargeDate").value;				
      
      if( !((document.getElementById("admissionDate").value)==="") && !((document.getElementById("dischargeDate").value)===""))
     	{
          var sdate = startDate.split("/");
        	var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
          altsdate=new Date(altsdate);
          
          var edate =endDate.split("/");
          var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
          altedate=new Date(altedate);
          
          var Startdate = new Date(altsdate);
          var EndDate =  new Date(altedate);
          
          if(EndDate < Startdate)
         	 {
         	 	alert("End Date should be greater than or equal to Start Date");
         		document.getElementById("dischargeDate").value="";
         		return ;
         	 }
     	}
      
      if(!((document.getElementById("dischargeDate").value)===""))
	     {
	          var edate =endDate.split("/");
	          var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
	          altedate=new Date(altedate);
	        
	          var EndDate =  new Date(altedate);
	          var currentDate = new Date();
	          
	          if(EndDate>currentDate)
	          {
	        	  	document.getElementById("conversionRate").disabled = false;
		  		 	document.getElementById("conversionRate").className="textBox textBoxSmall";
	          }
	          else
	          {
	        	document.getElementById("conversionRate").disabled = true;
	          }
	     } 
  }	
  
  function enableConversionRate() 
  {
		  var converionRateYN=document.getElementById("converionRateYN").checked;			
		  if(converionRateYN)
		  {
			  	document.getElementById("conversionRatediv").style.display="";
			  	var endDate=document.getElementById("dischargeDate").value;
			  	
			  	 if(!((document.getElementById("dischargeDate").value)===""))
			     {
			          var edate =endDate.split("/");
			          var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
			          altedate=new Date(altedate);
			        
			          var EndDate =  new Date(altedate);
			          var currentDate = new Date();
			          
			          if(EndDate>currentDate)
			          {
			        	if(document.getElementById("requestedAmountcurrencyType").value != "QAR")
			        	{
			        	   	document.getElementById("conversionRate").readOnly="";
				  		 	document.getElementById("conversionRate").className="textBox textBoxSmall";
			        	}
			        	else
			        	{
			        		document.getElementById("conversionRate").readOnly="true";
			        		document.getElementById("conversionRate").value="1";
			  		 		document.getElementById("conversionRate").className="textBox textBoxSmall textBoxDisabled";
			        	}	
			          }
			          else
			          {
			        	  		document.getElementById("conversionRate").readOnly="true";
				  		 		document.getElementById("conversionRate").className="textBox textBoxSmall textBoxDisabled";
			          }
			     }
			  	 else
			  	 {
			  		document.getElementById("conversionRate").readOnly="true";
		  		 	document.getElementById("conversionRate").className="textBox textBoxSmall textBoxDisabled";
			  	 }	 
		  } 	 
		  else 
			  document.getElementById("conversionRatediv").style.display="none";
  }

  function clearConversionRate() {
  	document.getElementById("conversionRate").value="";
  	document.getElementById("convertedAmount").value="";
  }

    
  function changeTreatment(obj){
	  
	  if(obj.value==='1' || obj.value==='3')
		  document.getElementById("orthoDiv").style.display="inline";
	  else
		  document.getElementById("orthoDiv").style.display="none";
  }
  function changeTreatmentType()
  {
  	 document.forms[1].mode.value="doChangeTreatmentType";
  	 document.forms[1].action="/PreAuthGeneralAction.do";
  	 document.forms[1].submit();
  }//end of clearEnrollmentID()
  
  function onPolicyTob()
  {
  	var prodPolicySeqId=document.getElementById("prodPolicySeqId").value;

  	   //	alert("hello::::"+claimSeqID);
  	   	var openPage = "/ViewTOBPreAuthGeneralAction.do?mode=doViewPolicyTOB&policySeqId="+prodPolicySeqId;	 
  	   	var w = screen.availWidth - 10;
  	  	var h = screen.availHeight - 49;
  	  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  	  	window.open(openPage,'',features);
  }
  function onUserIcon()
  {
	  if(!JS_SecondSubmit)
		 {  
		  document.forms[1].mode.value="doAssign";
		  document.forms[1].child.value="Assign";
		  document.forms[1].action="/AssignToAction.do";
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
		 }
  }//end of onUserIcon()
  
  
  function viewPreAuthDetails(preAuthSeqID) {
		document.forms[1].mode.value="doView";
		document.forms[1].action="/PreAuthGeneralAction.do?preAuthSeqID="+preAuthSeqID;
		document.forms[1].submit();
	}
  
 function displayBenefitsDetails(){
	 var memberId =document.forms[1].memberId.value;
	 
	 if(!(memberId===null||memberId==='')){
	   document.forms[1].mode.value="viewBenefitDetails";
	   document.forms[1].action="/PreAuthGeneralAction.do";	 
	   document.forms[1].submit();
	 }else
		 alert("Al Koot ID is required!");
 }
 
function fillFraudDeatils(){
		
		var preAuthSeqID = document.forms[1].preAuthSeqID.value;	
	//	var completedYN = document.forms[1].completedYN.value;
		document.forms[1].mode.value="markPreauthAsSuspect";
		document.forms[1].action="/PreauthInternalRemarksAction.do?preAuthSeqID="+preAuthSeqID;
		/*var openPage = "/ttk/claims/internalRemark.jsp?clmSeqId="+clmSeqId+"&completedYN="+completedYN;
		var features = "scrollbars=0,status=0,toolbar=0,top=100,left=200,resizable=0,menubar=0,width=800,height=420";*/
		document.forms[1].submit();
	  //  window.open(openPage,'',features);
		
	}

function showFraudDeatils(){
	
	/*var clmSeqId = document.forms[1].clmSeqId.value;
	var completedYN = document.forms[1].completedYN.value;
	var openPage = "/ClaimGeneralAction.do?mode=viewFraudData&clmSeqId="+clmSeqId+"&completedYN="+completedYN;
	var features = "scrollbars=0,status=0,toolbar=0,top=250,left=250,resizable=0,menubar=0,width=800,height=420";
    window.open(openPage,'',features);*/
	
	var preAuthSeqID = document.forms[1].preAuthSeqID.value;	
//	var completedYN = document.forms[1].completedYN.value;
	var activelink= document.forms[1].leftlink.value;
	if(activelink=="CounterFraudDept"){
		document.forms[1].mode.value="viewCFDPreauthAndClaimAsSuspect"; 
		document.forms[1].action="/CounterFraudSearchAction.do?preAuthSeqID="+preAuthSeqID;
	}else{
	document.forms[1].mode.value="dogetFraud";
	document.forms[1].action="/PreauthInternalRemarksAction.do?preAuthSeqID="+preAuthSeqID;
	}
	
	/*var openPage = "/ttk/claims/internalRemark.jsp?clmSeqId="+clmSeqId+"&completedYN="+completedYN;
	var features = "scrollbars=0,status=0,toolbar=0,top=100,left=200,resizable=0,menubar=0,width=800,height=420";*/
	document.forms[1].submit();
	
}

 // added by govind
 function qtyAndDaysAlert(){
		var qtyAndDaysAlert =  document.forms[1].qtyAndDaysAlert.value;
		if(qtyAndDaysAlert != ""){
			
			alert(qtyAndDaysAlert);
			
			
		}
	}
 
 function onPreauthFroudHistory(){
		if(!JS_SecondSubmit){
		   document.forms[1].mode.value="viewPreauthFraudHistroy";
		   document.forms[1].action="/PreAuthGeneralAction.do";	
		   JS_SecondSubmit=true;
		   document.forms[1].submit();
		}
	}
 
 function onClaimFroudHistory(){
		if(!JS_SecondSubmit){
		   document.forms[1].mode.value="viewClaimFraudHistroy";
		   document.forms[1].action="/PreAuthGeneralAction.do";	
		   JS_SecondSubmit=true;
		   document.forms[1].submit();
		}
	}
 
 function isFutureDate(argDate){
		var dateArr=argDate.split("/");	
		var givenDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
		var currentDate = new Date();
		if(givenDate>currentDate){
		return true;
		}
		return false;
	}
 function daysRangeValidation(argDate){
	 	var dateArr=argDate.split("/");	
		var inputDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
		var currentDate = new Date();
		var oneDay = 24*60*60*1000; 
		var diffDays = Math.round(Math.abs((currentDate.getTime() - inputDate.getTime())/(oneDay)));	
		var stYear=inputDate.getYear();
		var endYear=currentDate.getYear();
		var stLeapYear=stYear % 4 == 0;
		var endLeapYear=endYear % 4 == 0;
		if(stLeapYear==true||endLeapYear==true){
			if(diffDays >3){
				return true;
			}else{
				return false;
			}
		}else{
			if(diffDays >3){
				return true;
			}else{
				return false;
			}
		}				
}	 		


 function isBackDate(argDate,argDate1){
		var dateArr=argDate.split("/");	
		var prvRecvDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
		var dateArr=argDate1.split("/");	
		var currentRecvDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
		alert("prvRecvDate:::"+prvRecvDate+"currentRecvDate           "+currentRecvDate);
		if(prvRecvDate>currentRecvDate){
		return true;
		}
		return false;
	}
 
 function closePreauth()
 {
  	document.forms[1].mode.value="doClose";
  	document.forms[1].leftlink.value ="Pre-Authorization";
    document.forms[1].sublink.value ="Dashbord";
    document.forms[1].tab.value ="Dashboard";
    document.forms[1].action="/AssignToDashBordPreauthAction.do";      
 	document.forms[1].submit();
 }
 function statusChange(){
	 
	  var statusval = document.getElementById("preauthStatus").value;
	 
	  if(statusval !=='INP'){
		  document.getElementById("reason").value='';
	  }
	   document.getElementById("focusID").value="preauthStatus";
	/*   document.forms[1].focusID.value ="preauthStatus";*/	   
	   document.forms[1].mode.value="setReasonMode";
	   document.forms[1].action="/PreAuthGeneralAction.do";			    
	   document.forms[1].submit();
		 
}
 
 function statusReasonChange(){
	 
	  
	   document.getElementById("focusID").value="reason";	   
	   document.forms[1].mode.value="setTimeMode";
	   document.forms[1].action="/PreAuthGeneralAction.do";			    
	   document.forms[1].submit();
		 
}
 
function onViewMdf(){

	var	memberSeqID	=	document.getElementById('memberSeqID').value;
	if(memberSeqID==''){
		alert("Please select a Member");
		document.getElementById('memberId').focus();
		return false;
	}else{
		var openPage = "/AddMdfMemberAction.do?mode=doViewMemberMdfFile&patOrClmFlag=YES&memberSeqID="+memberSeqID;
		var w = screen.availWidth - 10;
	  	var h = screen.availHeight - 49;
	  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   	window.open(openPage,'',features);
	}
}