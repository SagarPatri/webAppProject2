
function setInitialStates(){
	/*if(document.getElementById("inpatientBenefit").value == "Y"||document.getElementById("inpatientBenefit").value == "N"){
		
		document.getElementById("opticalYNid").disabled="";
		if(document.getElementById("opticalYNid").value=="Y"){
			document.getElementById("opticalLimitList").disabled="";
			document.getElementById("opticalCopayList").disabled="";
			$(".spanOptical2").removeAttr("style");
		}else{
			document.getElementById("opticalLimitList").disabled="disabled";
			document.getElementById("opticalCopayList").disabled="disabled";
			$(".spanOptical2").attr("style","display:none");
		}
		
		document.getElementById("dentalYNid").disabled="";
		if(document.getElementById("dentalYNid").value=="Y"){
			document.getElementById("dentalLimitList").disabled="";
			document.getElementById("dentalcopayList").disabled="";
			$(".spanDental2").removeAttr("style");
		}else{
			document.getElementById("dentalLimitList").disabled="disabled";
			document.getElementById("dentalcopayList").disabled="disabled";
			$(".spanDental2").removeAttr("style","display:none");
		}
		
		document.getElementById("maternityYNId").disabled="";
		if(document.getElementById("maternityYNId").value=="Y"){
				$(".spanMeternity2").removeAttr("style");
				document.getElementById("maternityLimitList").disabled="";
				document.getElementById("totalLivesMaternity").readOnly="";
				document.getElementById("totalLivesMaternity").className="textBox textBoxLarge";
		}else{
			$(".spanMeternity2").attr("style","display:none");
			document.getElementById("maternityLimitList").disabled="disabled";
			document.getElementById("totalLivesMaternity").readOnly="true";
			document.getElementById("totalLivesMaternity").className="textBox textBoxLarge textBoxDisabled";
		}
		
		document.getElementById("alAhlihospital").disabled="";
		if(document.getElementById("alAhlihospital").value=="Y"||document.getElementById("outpatientBenefit").value=="Y"){
			$(".spanhospital3").removeAttr("style");
			$(".spaniscopayhosp").removeAttr("style");
			document.getElementById("opConsultAlAhli").disabled="";
			document.getElementById("alAhlihospOPservices").disabled="";
			}
		 else if(obj.value == "N")
		 {
			 $(".spanhospital3").attr("style","display:none");
			 $(".spaniscopayhosp").attr("style","display:none");
			 $(".spancopayhosp").attr("style","display:none");
			 
			 document.getElementById("opCopyalahlihosp").disabled="disabled";
			 document.getElementById("opPharmacyAlAhli").disabled="disabled";
			 document.getElementById("opConsultAlAhli").disabled="disabled";
			 document.getElementById("opInvestnAlAhli").disabled="disabled";
			 document.getElementById("opothersAlAhli").disabled="disabled";
			 document.getElementById("alAhlihospOPservices").disabled="disabled";		 
		}
		
		document.getElementById("maxBenifitList").disabled="";
		document.getElementById("areaOfCoverList").disabled="";
		document.getElementById("networkList").disabled="";
		document.getElementById("physiocoverage").disabled="";
		document.getElementById("vitDcoverage").disabled="";
		document.getElementById("vaccImmuCoverage").disabled="";
		document.getElementById("matComplicationCoverage").disabled="";
		document.getElementById("psychiatrycoverage").disabled="";
		document.getElementById("deviatedNasalSeptum").disabled="";
		document.getElementById("obesityid").disabled="";
		document.getElementById("gastricBinding").disabled="disabled";
		document.getElementById("healthScreen").disabled="";
		document.getElementById("orthodonticsCopay").disabled="";
		
		$(".spanOptical1").removeAttr("style");
		$(".spanMeternity1").removeAttr("style");
		$(".spanDental1").removeAttr("style");
		$(".otherspan").removeAttr("style");
		$(".spanMeternity2").attr("style","display:none");
		$(".spanhospital3").attr("style","display:none");
		$(".spanObesity").attr("style","display:none");
		
	}else{
		document.getElementById("maxBenifitList").disabled="disabled";
		document.getElementById("areaOfCoverList").disabled="disabled";
		document.getElementById("networkList").disabled="disabled";
		document.getElementById("alAhlihospital").disabled="disabled";
		document.getElementById("opticalYNid").disabled="disabled";
		document.getElementById("opticalLimitList").disabled="disabled";
		document.getElementById("opticalCopayList").disabled="disabled";
		document.getElementById("dentalYNid").disabled="disabled";
		document.getElementById("dentalLimitList").disabled="disabled";
		document.getElementById("dentalcopayList").disabled="disabled";
		document.getElementById("maternityYNId").disabled="disabled";
		document.getElementById("maternityLimitList").disabled="disabled";
		document.getElementById("physiocoverage").disabled="disabled";
		document.getElementById("vitDcoverage").disabled="disabled";
		document.getElementById("vaccImmuCoverage").disabled="disabled";
		document.getElementById("matComplicationCoverage").disabled="disabled";
		document.getElementById("psychiatrycoverage").disabled="disabled";
		document.getElementById("deviatedNasalSeptum").disabled="disabled";
		document.getElementById("obesityid").disabled="disabled";
		document.getElementById("gastricBinding").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("healthScreen").disabled="disabled";
		document.getElementById("orthodonticsCopay").disabled="disabled";
		document.getElementById("totalLivesMaternity").disabled="disabled";
		document.getElementById("alAhlihospital").disabled="disabled";
		$(".spanhospital1").attr("style","display:none");
		$(".spanOptical1").attr("style","display:none");
		$(".spanMeternity1").attr("style","display:none");
		$(".spanDental1").attr("style","display:none");
		$(".spanhospital2").attr("style","display:none");
		$(".spanOptical2").attr("style","display:none");
		$(".spanMeternity2").attr("style","display:none");
		$(".spanDental2").attr("style","display:none");
		$(".spanhospital3").attr("style","display:none");
		$(".spanObesity").attr("style","display:none");
		$(".otherspan").attr("style","display:none");
	}*/
	
	if(document.getElementById("outpatientBenefit").value == "Y"||document.getElementById("outpatientBenefit").value == "N"){
		
		if(document.getElementById("outpatientBenefit").value == "Y"){
			document.getElementById("opdeductableserviceYN").disabled="";
		}
		else if(document.getElementById("outpatientBenefit").value == "N"){
			document.getElementById("opdeductableserviceYN").disabled="disabled";
			document.getElementById("alAhlihospOPservices").disabled="disabled";
		}
		if(document.getElementById("opdeductableserviceYN").value == ""){
				$(".spanOpservices").attr("style","display:none");
				$(".spaniscopayOP1").attr("style","display:none");
				$(".spaniscopayOP2").attr("style","display:none");
				 document.getElementById("opCopaypharmacy").disabled="disabled";
				 document.getElementById("opInvestigation").disabled="disabled";
				 document.getElementById("opCopyconsultn").disabled="disabled";
				 document.getElementById("opCopyothers").disabled="disabled";
				 document.getElementById("opCopayList").disabled="disabled";
				 document.getElementById("opDeductableList").disabled="disabled";
			}else if(document.getElementById("opdeductableserviceYN").value == "Y"){
				 $(".spanOpservices1").attr("style","display:none");
				 $(".spaniscopayOP1").removeAttr("style");
				 $(".spaniscopayOP2").removeAttr("style");
				 document.getElementById("opCopaypharmacy").disabled="disabled";
				 document.getElementById("opInvestigation").disabled="disabled";
				 document.getElementById("opCopyconsultn").disabled="disabled";
				 document.getElementById("opCopyothers").disabled="disabled";
				 document.getElementById("opCopayList").disabled="";
				 if(document.getElementById("opCopayList").value =="" || document.getElementById("opCopayList").value =="6"){
					 document.getElementById("opDeductableList").disabled="";
				}else{
					 document.getElementById("opDeductableList").disabled="disabled";
					 document.getElementById("opDeductableList").value = "";
				}				 
				
			}else if(document.getElementById("opdeductableserviceYN").value == "N"){
				 $(".spanOpservices").removeAttr("style");
				 $(".spaniscopayOP1").removeAttr("style");
				 $(".spaniscopayOP2").attr("style","display:none");
				 document.getElementById("opCopaypharmacy").disabled="";
				 document.getElementById("opInvestigation").disabled="";
				 document.getElementById("opCopyconsultn").disabled="";
				 document.getElementById("opCopyothers").disabled="";
				 document.getElementById("opCopayList").disabled="disabled";
				 document.getElementById("opDeductableList").disabled="disabled";
				}
		
		
		
			if(document.getElementById("alAhlihospOPservices").value==""){
				 document.getElementById("opPharmacyAlAhli").disabled="disabled";
				 document.getElementById("opConsultAlAhli").disabled="disabled";
				 document.getElementById("opInvestnAlAhli").disabled="disabled";
				 document.getElementById("opothersAlAhli").disabled="disabled";
				 document.getElementById("opCopyalahlihosp").disabled="disabled";
			}else if(document.getElementById("alAhlihospOPservices").value=="Y"){
				 $(".spanhospital").attr("style","display:none"); // al alali copay only madatory
				 $(".spaniscopayhosp").removeAttr("style");
				 $(".spancopayhosp").removeAttr("style");
				 document.getElementById("opCopyalahlihosp").disabled="";
				 document.getElementById("opPharmacyAlAhli").disabled="disabled";
				 document.getElementById("opConsultAlAhli").disabled="disabled";
				 document.getElementById("opInvestnAlAhli").disabled="disabled";
				 document.getElementById("opothersAlAhli").disabled="disabled";
				}
			else if(document.getElementById("alAhlihospOPservices").value=="N"){
				 $(".spaniscopayhosp").attr("style","display:none"); // al alali copay only madatory
				 $(".spancopayhosp").attr("style","display:none"); // al alali copay only madatory
				 $(".spanhospital").removeAttr("style");
				 document.getElementById("opCopyalahlihosp").disabled="disabled";
				 document.getElementById("opPharmacyAlAhli").disabled="";
				 document.getElementById("opConsultAlAhli").disabled="";
				 document.getElementById("opInvestnAlAhli").disabled="";
				 document.getElementById("opothersAlAhli").disabled="";
			}	
	}
	
	else{
			document.getElementById("opdeductableserviceYN").disabled="disabled";
			document.getElementById("opCopaypharmacy").disabled="disabled";
			document.getElementById("opInvestigation").disabled="disabled";
			document.getElementById("opCopyconsultn").disabled="disabled";
			document.getElementById("opCopyothers").disabled="disabled";
			document.getElementById("opDeductableList").disabled="disabled";
			document.getElementById("opCopayList").disabled="disabled";
			document.getElementById("alAhlihospOPservices").disabled="disabled";
			document.getElementById("opPharmacyAlAhli").disabled="disabled";
			document.getElementById("opConsultAlAhli").disabled="disabled";
			document.getElementById("opInvestnAlAhli").disabled="disabled";
			document.getElementById("opothersAlAhli").disabled="disabled";
			document.getElementById("opCopyalahlihosp").disabled="disabled";
			$(".spanhospital").removeAttr("style"); 
			$(".spanOpservices1").removeAttr("style");
			$(".spaniscopayOP1").attr("style","display:none");
			$(".spanhospital3").attr("style","display:none");
			$(".spaniscopayhosp").attr("style","display:none");
			$(".spancopayhosp").attr("style","display:none");
			$(".spaniscopayOP2").attr("style","display:none");
	}
};

	function copyToWebBoard()
	{
	    if(!mChkboxValidation(document.forms[1]))
	    {
		    document.forms[1].mode.value = "doCopyToWebBoard";
	   		document.forms[1].action = "/SwInsPricingAction.do";
		    document.forms[1].submit();
	    }//end of if(!mChkboxValidation(document.forms[1]))
	}//end of copyToWebBoard()

function addPricing()
{
	document.forms[1].reset();
    document.forms[1].rownum.value = "";
    document.forms[1].tab.value ="Group Profile";
    document.forms[1].mode.value = "doAdd";
    document.forms[1].action = "/SwInsPricingAction.do";
    document.forms[1].submit();
}

function isTotalNatLives(obj1,copyobj2)
{
	var copyobj	=	copyobj2;
	var totalNoOfLives = document.forms[1].totalNoOfLives.value;
	var first =	 0;
	for(var i=0;i<6;i++)
	{
		var natValue =  parseInt(document.getElementById("natl_typeseqid["+i+"]").value);
		
		if ( isNaN( natValue ) || natValue == "" ) {
			natValue = 0;
		}
		first =	 parseInt(natValue)+parseInt(first);
		document.getElementById("sumNationalityLives").value = first;
		/*if(first>totalNoOfLives)
		{
			alert("Lives split by nationality is more than "+totalNoOfLives);
			document.getElementById("natl_typeseqid["+copyobj+"]").value	=	"";
			return false;
		}*/
	}
}

function isTotalLives(obj1,copyobj2,benvalobj3)
{
	var copyobj	=	copyobj2;
	var benvalobj = benvalobj3;
	var totalNoOfLives = document.forms[1].totalNoOfLives.value;
	var MaternitytotLives = document.forms[1].TotalMaternityLives.value;
	var first =	 0;
	for(var i=0;i<18;i++)
	{
		if(benvalobj == "6"){
			var inpatientValue =  parseInt(document.getElementById("benf_typeseqid6["+i+"]").value);
		
			if ( isNaN( inpatientValue ) || inpatientValue == "" ) {
				inpatientValue = 0;
			}
			first =	 parseInt(inpatientValue)+parseInt(first);
			
			document.getElementById("sumTotalLives").value = first;
			document.getElementById("totalNoOfLives").value = first;
//			document.getElementById("totalNoOfLives_id").value = first;
			
		//	document.getElementById("sumTotalLivesDental").value = first;
		//	document.getElementById("sumTotalLivesOptical").value = first;
			
			/*if(first>totalNoOfLives)
			{
				alert("Total covered lives-Inpatient/Outpatient is more than "+totalNoOfLives);
				document.getElementById("benf_typeseqid6["+copyobj+"]").value	=	"";
				return false;
			}*/
		}
		
		if(benvalobj == "2"){
			var dentalValue =  parseInt(document.getElementById("benf_typeseqid2["+i+"]").value);
		
			if ( isNaN( dentalValue ) || dentalValue == "" ) {
				dentalValue = 0;
			}
			first =	 parseInt(dentalValue)+parseInt(first);
			
			if(first>totalNoOfLives)
			{
				alert("Total covered lives-Dental is more than "+totalNoOfLives);
				document.getElementById("benf_typeseqid2["+copyobj+"]").value	=	"";
				return false;
			}
		}
		
		if(benvalobj == "3"){
			var opticalValue =  parseInt(document.getElementById("benf_typeseqid3["+i+"]").value);
		
			if ( isNaN( opticalValue ) || opticalValue == "" ) {
				opticalValue = 0;
			}
			first =	 parseInt(opticalValue)+parseInt(first);
			if(first>totalNoOfLives)
			{
				alert("Total covered lives-Optical is more than "+totalNoOfLives);
				document.getElementById("benf_typeseqid3["+copyobj+"]").value	=	"";
				return false;
			}
		}
		
		if(benvalobj == "1"){
			var maternityValue =  parseInt(document.getElementById("benf_typeseqid1["+i+"]").value);
		
			if ( isNaN( maternityValue ) || maternityValue == "" ) {
				maternityValue = 0;
			}
			first =	 parseInt(maternityValue)+parseInt(first);
			document.getElementById("sumTotalLivesMaternity").value = first;
//			document.getElementById("totalMaternityLives_id").value = first;
			document.getElementById("TotalMaternityLives").value = first;
			/*if(first>MaternitytotLives)
			{
				alert("Total covered lives-Maternity more than "+MaternitytotLives);
				document.getElementById("benf_typeseqid1["+copyobj+"]").value	=	"";
				return false;
			}*/
		}
		
		
	}
	
}

function isCopy(obj)
{

var dentalYN = document.forms[1].dentalLivesYN.value;
var opticalYN = document.forms[1].opticalLivesYN.value;
//alert(dentalYN+"-------------"+opticalYN+"----"+document.getElementById("benf_typeseqid6[0]").value);
		for(var i=0;i<18;i++)
		{
			if(dentalYN == "Y"){
		  	document.getElementById("benf_typeseqid2["+i+"]").value = document.getElementById("benf_typeseqid6["+i+"]").value;
			}
			if (opticalYN == "Y"){
			document.getElementById("benf_typeseqid3["+i+"]").value = document.getElementById("benf_typeseqid6["+i+"]").value;
			}
		}

}

function onSave(obj){	
	var singlebutton = obj;
	 trimForm(document.forms[1]);
	 var maxBenefitElement=document.getElementById('maxBenifitList').value;
	 var opticalLimitElement=document.getElementById('opticalLimitList').value;
	 var dentalLimitElement=document.getElementById('dentalLimitList').value;
	 var maternityLimitElement=document.getElementById('maternityLimitList').value;
	 setDescValues();
	
	 if( document.forms[1].renewalYN.value == "Y" && document.getElementById("previousPolicyNo").value == ""){
		 	alert('\'Past policy number\' is required.');
		    document.getElementById("previousPolicyNo").value = "";
			document.getElementById("previousPolicyNo").focus();
			return false;
	 }
	 
	 
	 var trendFactor = parseFloat(document.forms[1].trendFactor.value);
	if(trendFactor > 14){
		alert("Trend should not exceed 14%");
		document.forms[1].trendFactor.focus();
		return false;
	}
	/*if(document.forms[1].groupProfileSeqID.value == 0)
	{
	alert("Please note the pricing reference number");
	}*/
	var fetchData = document.getElementById('fetchData').value;

if(fetchData == "Y"){
		
		var confYN=confirm("''Key Coverages' section have been auto popuated, please confirm that you have reviewed the inputs and click 'OK' to proceed.");
		
		if(!confYN){
		  return false;
		}
		
		
	}
	 if(maxBenefitElement=='45'){
		 var maxBenefitOthElement=document.forms[1].maxBeneLimitOth;
		 var check=checkValue(maxBenefitOthElement);
		 if(check==false){
			 maxBenefitOthElement.focus();
				return false; 
			 }
	 }
	 if(opticalLimitElement=='45'){
		 var opticalLimitOth=document.forms[1].opticalLimitOth;
		 var check=checkValue(opticalLimitOth);
		 if(check==false){
			 opticalLimitOth.focus();
				return false; 
			 }
	 }
	 if(dentalLimitElement=='45'){
		 var dentalLimitOth=document.forms[1].dentalLimitOth;
		 var check=checkValue(dentalLimitOth);
		 if(check==false){
			 dentalLimitOth.focus();
				return false; 
			 }
	 }
	 if(maternityLimitElement=='45'){
		 var maternityLimitOth=document.forms[1].maternityLimitOth;
		 var check=checkValue(maternityLimitOth);
		 if(check==false){
			 maternityLimitOth.focus();
			return false; 
		 }
	 }
	 if(!JS_SecondSubmit)
    {
		 /*if (confirm('Are you sure you want to proceed pricing of the policy category mentioned on the top?')) {*/
			if(singlebutton =="saveProceed"){
			document.forms[1].tab.value ="Income Profile";
		    }
		   document.forms[1].mode.value="doSave";
		   document.forms[1].action="/SwInsPricingActionAdd.do?singlebutton="+singlebutton;
		   JS_SecondSubmit=true;
		   document.forms[1].submit();
		 /*}else{
				document.getElementById("previousPolicyNo").focus();
				return false;
			} */
	}//end of if(!JS_SecondSubmit)
	
}//end of onSave()


function setDescValues(){
	
	 var selopCopayListIndex = document.forms[1].opCopayList.selectedIndex;
	 var selopCopayListText = document.forms[1].opCopayList.options[parseInt(selopCopayListIndex)].text;
	 document.forms[1].opCopayListDesc.value=selopCopayListText;
	 
	 var selopDentalcopayListIndex = document.forms[1].dentalcopayList.selectedIndex;
	 var selopDentalcopayListText = document.forms[1].dentalcopayList.options[parseInt(selopDentalcopayListIndex)].text;
	 document.forms[1].dentalcopayListDesc.value=selopDentalcopayListText;
	 
	 var selopOpticalCopayListIndex = document.forms[1].opticalCopayList.selectedIndex;
	 var selopOpticalCopayText = document.forms[1].opticalCopayList.options[parseInt(selopOpticalCopayListIndex)].text;
	 document.forms[1].opticalCopayListDesc.value=selopOpticalCopayText;
	 
	 var selopDeductableListIndex = document.forms[1].opDeductableList.selectedIndex;
	 var selopDeductableListText = document.forms[1].opDeductableList.options[parseInt(selopDeductableListIndex)].text;
	 document.forms[1].opDeductableListDesc.value=selopDeductableListText;
	 
	 var selorthodonticsCopayIndex = document.forms[1].orthodonticsCopay.selectedIndex;
	 var selorthodonticsCopayText = document.forms[1].orthodonticsCopay.options[parseInt(selorthodonticsCopayIndex)].text;
	 document.forms[1].orthodonticsCopayDesc.value=selorthodonticsCopayText;
	 
	 var selopCopaypharmacyIndex = document.forms[1].opCopaypharmacy.selectedIndex;
	 var selopCopaypharmacyText = document.forms[1].opCopaypharmacy.options[parseInt(selopCopaypharmacyIndex)].text;
	 document.forms[1].opCopaypharmacyDesc.value=selopCopaypharmacyText;
	 
	 var selopInvestigationIndex = document.forms[1].opInvestigation.selectedIndex;
	 var selopInvestigationText = document.forms[1].opInvestigation.options[parseInt(selopInvestigationIndex)].text;
	 document.forms[1].opInvestigationDesc.value=selopInvestigationText;
	 
	 var selopCopyconsultnIndex = document.forms[1].opCopyconsultn.selectedIndex;
	 var selopCopyconsultnText = document.forms[1].opCopyconsultn.options[parseInt(selopCopyconsultnIndex)].text;
	 document.forms[1].opCopyconsultnDesc.value=selopCopyconsultnText;
	 
	 var selopCopyothersIndex = document.forms[1].opCopyothers.selectedIndex;
	 var selopCopyothersText = document.forms[1].opCopyothers.options[parseInt(selopCopyothersIndex)].text;
	 document.forms[1].opCopyothersDesc.value=selopCopyothersText;
	 
	 var selopCopyalahlihospIndex = document.forms[1].opCopyalahlihosp.selectedIndex;
	 var selopCopyalahlihospText = document.forms[1].opCopyalahlihosp.options[parseInt(selopCopyalahlihospIndex)].text;
	 document.forms[1].opCopyalahlihospDesc.value=selopCopyalahlihospText;
	 
	 var selopPharmacyAlAhliIndex = document.forms[1].opPharmacyAlAhli.selectedIndex;
	 var selopPharmacyAlAhliText = document.forms[1].opPharmacyAlAhli.options[parseInt(selopPharmacyAlAhliIndex)].text;
	 document.forms[1].opPharmacyAlAhliDesc.value=selopPharmacyAlAhliText;
	 
	 var selopConsultAlAhliIndex = document.forms[1].opConsultAlAhli.selectedIndex;
	 var selopConsultAlAhliText = document.forms[1].opConsultAlAhli.options[parseInt(selopConsultAlAhliIndex)].text;
	 document.forms[1].opConsultAlAhliDesc.value=selopConsultAlAhliText;
	 
	 var selopInvestnAlAhliIndex = document.forms[1].opInvestnAlAhli.selectedIndex;
	 var selopInvestnAlAhliText = document.forms[1].opInvestnAlAhli.options[parseInt(selopInvestnAlAhliIndex)].text;
	 document.forms[1].opInvestnAlAhliDesc.value=selopInvestnAlAhliText;
	 
	 var selopothersAlAhliIndex = document.forms[1].opothersAlAhli.selectedIndex;
	 var selopothersAlAhliText = document.forms[1].opothersAlAhli.options[parseInt(selopothersAlAhliIndex)].text;
	 document.forms[1].opothersAlAhliDesc.value=selopothersAlAhliText;
}


function onPartialSave(obj){	
	var singlebutton = obj;  
	 trimForm(document.forms[1]);	
	
	 setDescValues();
	 if( document.forms[1].renewalYN.value == "Y" && document.getElementById("previousPolicyNo").value == ""){
	 if (confirm('Previous policy number has not been entered. Do you want to proceed?')) {
		   document.getElementById("previousPolicyNo").value = "";
		} else {
			document.getElementById("previousPolicyNo").focus();
		} 
	 }
	 
	 
	 var trendFactor = parseFloat(document.forms[1].trendFactor.value);
	if(trendFactor > 14){
		alert("Trend should not exceed 14%");
		document.forms[1].trendFactor.focus();
		return false;
	}
	/*if(document.forms[1].groupProfileSeqID.value == 0)
	{
	alert("Please note the pricing reference number");
	}*/
	 
	 if(!JS_SecondSubmit)
    {
		 
	   document.forms[1].mode.value="doSave";
	   document.forms[1].action="/SwInsPricingActionPartialAdd.do?singlebutton="+singlebutton;
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
	
}//end of onSave()

function onIncomeprofile()
{
	if(!JS_SecondSubmit)
	 {
		//document.forms[1].reset();
		
		if(document.forms[1].groupProfileSeqID.value == 0)
			{
				alert('Please save before proceed');											
			return false;
			}
		if(!TrackChanges()) return false; 
	    document.forms[1].rownum.value = "";
	    document.forms[1].tab.value ="Income Profile";
	    document.forms[1].mode.value = "doIncomeProfile";
	    document.forms[1].action = "/SwInsPricingActionIncome.do";
	    document.forms[1].submit();
	    
	 }
}


function onSaveIncome(obj){	
var singlebutton = obj;
var policyNumber=document.forms[1].policyNumber.value;
var sumTotalLivesMaternity=document.forms[1].sumTotalLivesMaternity.value;
var totalNoOfLives=document.forms[1].totalNoOfLives.value;
var totalNumberOfLive=document.forms[1].numberOfLives.value;
var sumNationalityLives=document.forms[1].sumNationalityLives.value;
var sumTotalLives=document.forms[1].sumTotalLives.value;
var TotalLives =	 parseInt(sumTotalLives);
	TotalLives=TotalLives ? TotalLives : 0;
var TotalLivesMaternity =	 parseInt(sumTotalLivesMaternity);
	TotalLivesMaternity= TotalLivesMaternity ? TotalLivesMaternity : 0;
var NationalityLives =	 parseInt(sumNationalityLives);
	NationalityLives=NationalityLives ? NationalityLives : 0;
var totLives = parseFloat(sumTotalLives);
totLives=totLives ? totLives : 0.0;
var actTotLives=parseFloat(totalNumberOfLive);
actTotLives=actTotLives ? actTotLives : 0.0;
if(TotalLives==NationalityLives){
	if(TotalLives>=TotalLivesMaternity){
		document.getElementById('totalMaternityLives_id').value=sumTotalLivesMaternity;
		document.getElementById('totalNoOfLives_id').value=totalNoOfLives;
		document.forms[1].TotalMaternityLives.value=sumTotalLivesMaternity;
			 trimForm(document.forms[1]);
			 if((totLives!=0&&totLives>=actTotLives) && singlebutton =="save" && (policyNumber!="" && policyNumber!=null)){
					alert('Group size entered is 25% higher than last available data for the given policy.');
			 }
			 if(totLives!=0){
				 if(!JS_SecondSubmit)
				   {
					  if(singlebutton =="saveProceed"){
						document.forms[1].tab.value="Plan design";
					   }
					   document.forms[1].mode.value="doSaveIncome";
					   document.forms[1].action="/SwInsPricingActionIncome.do?singlebutton="+singlebutton;
					   JS_SecondSubmit=true;
					   document.forms[1].submit();
					}//end of if(!JS_SecondSubmit)
			 }else{
				 alert('Total covered lives entered should be more than 0');
			 }
			
	}else{
		alert('Total covered lives-Maternity should be less than or equal to Total covered lives');
	}	
}else{
//	document.getElementById("totalNoOfLives_id").value = totalNoOfLives;
	alert('Total Nationality covered lives should equal to Total covered lives');
}
}

function onSaveIncomePartial(obj){	
	var singlebutton = obj;

		 trimForm(document.forms[1]);	
		 if(!JS_SecondSubmit)
	   {
		  if(singlebutton =="saveProceed"){
			document.forms[1].tab.value="Plan design";
		   }
		   document.forms[1].mode.value="doSaveIncome";
		   document.forms[1].action="/SwInsPricingActionIncomePartial.do?singlebutton="+singlebutton;
		   JS_SecondSubmit=true;
		   document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
		
	}


function onCloseIncome()
{
	document.forms[1].reset();
    document.forms[1].rownum.value = "";
    document.forms[1].tab.value ="Group Profile";
    document.forms[1].mode.value = "doCloseIncome";
    document.forms[1].action = "/SwInsPricingActionClose.do";
    document.forms[1].submit();
}

function toggle(sortid)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/SwInsPricingAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

function edit(rownum)
{
    document.forms[1].mode.value="doEditIncome";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value="Group Profile";
    document.forms[1].action = "/SwInsPricingAction.do";
    document.forms[1].submit();
}//end of edit(rownum)

//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/SwInsPricingAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
	document.forms[1].reset();
	document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/SwInsPricingAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	document.forms[1].reset();
	document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/SwInsPricingAction.do";
    document.forms[1].submit();
}//end of PressForward()

function onSearch()
{
	
	if(!JS_SecondSubmit)
	 {
			document.forms[1].mode.value = "doSearch";
			document.forms[1].action = "/SwInsPricingAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
	 }
	
}//end of onSearch()
 

function onViewPlanDesign()
{
	if(!TrackChanges()) return false; 
	if(!JS_SecondSubmit)
	 {
	document.forms[1].reset();
	document.forms[1].mode.value ="doDefault";
	document.forms[1].tab.value="Plan design";
    document.forms[1].action = "/SwPlanDesignConfigurationAction.do";
    document.forms[1].submit();
	 }
}//end of PressBackWard()

function onchangeMaxBenefitType(obj)
{

	 if(obj.value == "45"){
		
		document.getElementById("maxBeneLimitOth").className="textBox textBoxLarge";
	 	document.getElementById("maxBeneLimitOth").readOnly="";
	 }
	 else{
		 document.getElementById("maxBeneLimitOth").className="textBox textBoxLarge textBoxDisabled";
		 document.getElementById("maxBeneLimitOth").readOnly="true"; 
		 document.getElementById("maxBeneLimitOth").value = "";
	 }
}


function onchangeDentalType(obj)
{
	
	 if(obj.value == "56"){
		 alert('There is little underlying data for plans with dental limit greater than 15,000. The user is requested to finalize the pricing keeping this in mind.');
	 }
	 if(obj.value == "45"){
	
		document.getElementById("dentalLimitOth").className="textBox textBoxLarge ";
	 	document.getElementById("dentalLimitOth").readOnly="";
	 }else{
		document.getElementById("dentalLimitOth").className="textBox textBoxLarge textBoxDisabled";
		document.getElementById("dentalLimitOth").readOnly="true";
		document.getElementById("dentalLimitOth").value = "";
	 }
}

function onchangeOpticalType(obj)
{
	if(obj.value == "55"){
		alert('There is little underlying data for plans with optical limit greater than 5,000. The user is requested to finalize the pricing keeping this in mind.');
	 }
	 if(obj.value == "45"){
	
		document.getElementById("opticalLimitOth").className="textBox textBoxLarge";
	 	document.getElementById("opticalLimitOth").readOnly="";
	 }else{
		 document.getElementById("opticalLimitOth").className="textBox textBoxLarge textBoxDisabled";
		 document.getElementById("opticalLimitOth").readOnly="true";
		 document.getElementById("opticalLimitOth").value  = "";
	 }
}


function onchangeChronicType(obj)
{
	 if(obj.value == "45"){
	
		document.getElementById("chronicLimitOth").className="textBox textBoxLarge";
	 	document.getElementById("chronicLimitOth").readOnly="";
	 }else{
		 document.getElementById("chronicLimitOth").className="textBox textBoxLarge textBoxDisabled";
		 document.getElementById("chronicLimitOth").readOnly="true";
		 document.getElementById("chronicLimitOth").value  = "";
	 }
}

function onchangeMaternityType(obj)
{
	 if(obj.value == "57"){
		 alert('There is little underlying data for plans with maternity limit greater than 35,000. The user is requested to finalize the pricing keeping this in mind.');
	 }
	 if(obj.value == "45"){
	
		document.getElementById("maternityLimitOth").className="textBox textBoxLarge";
	 	document.getElementById("maternityLimitOth").readOnly="";
	 }else{
		 document.getElementById("maternityLimitOth").className="textBox textBoxLarge textBoxDisabled";
		 document.getElementById("maternityLimitOth").readOnly="true";
		 document.getElementById("maternityLimitOth").value = "";
	 }
}



/*function onchangePolicyType(obj) // on hold
{
	 if(obj.value == "Y"){
	
		document.getElementById("clientcodeId").className="textBox textBoxLarge";
	 	document.getElementById("clientcodeId").readOnly="";
	 }else{
		 document.getElementById("clientcodeId").className="textBox textBoxLarge textBoxDisabled";
		 document.getElementById("clientcodeId").readOnly="true";
	 }
}*/



function onViewDocument(filename)
{

	
   	var openPage = "/SwInsPricingAction.do?mode=doViewUploadDocs&filename="+filename;	 
   	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  	window.open(openPage,'',features);
}

//function to Change the corporate.
function changeCorporate()
{
/*	if( document.forms[1].renewalYN.value == "N"){
	alert("Client code applicable only for renewal policy ");
	}else{*/
	
	document.forms[1].mode.value="doChangeCorporate";
	document.forms[1].child.value="GroupList";
	document.forms[1].action="/clientDetailAction.do";
	document.forms[1].submit();
	//}
}//end of changeCorporate()



function onDentalChange(obj)
{
	if(obj.value == ""){
		$(".spanDental2").attr("style","display:none"); 
		document.getElementById("dentalLivesYN").disabled="disabled";
		document.forms[1].dentalLivesYN.value = "N";
		document.getElementById("dentalLivesYN").value = "N";
		document.getElementById("dentalLimitList").disabled="disabled";
		document.getElementById("dentalLimitList").value = "";
		document.getElementById("dentalcopayList").disabled="disabled";
		document.getElementById("dentalcopayList").value = "";
		document.getElementById("orthodonticsCopay").disabled="disabled";
		document.getElementById("orthodonticsCopay").value = "";
	}else if(obj.value == "N")
		{
		$(".spanDental2").attr("style","display:none"); 
		document.getElementById("dentalLivesYN").disabled="disabled";
		document.forms[1].dentalLivesYN.value = "N";
		document.getElementById("dentalLivesYN").value = "N";
		document.getElementById("dentalLimitList").disabled="disabled";
		document.getElementById("dentalLimitList").value = "";
		document.getElementById("dentalcopayList").disabled="disabled";
		document.getElementById("dentalcopayList").value = "";
		document.getElementById("orthodonticsCopay").disabled="disabled";
		document.getElementById("orthodonticsCopay").value = "";
		}
	else if(obj.value == "Y"){
		$(".spanDental2").removeAttr("style");
		document.getElementById("dentalLivesYN").disabled="disabled";
		document.getElementById("dentalLivesYN").value = "Y";
	    document.getElementById("dentalLimitList").disabled="";
		document.getElementById("dentalcopayList").disabled="";
		document.getElementById("orthodonticsCopay").disabled="";
		 
	    }
}

function onOpticalChange(obj)
{
	if(obj.value == ""){
		 $(".spanOptical2").attr("style","display:none");
		 document.getElementById("opticalLivesYN").disabled="disabled";
		 document.forms[1].opticalLivesYN.value= "N";
		 document.getElementById("opticalLivesYN").value = "N";
		 document.getElementById("opticalLimitList").disabled="disabled";
		 document.getElementById("opticalLimitList").value = "";
		 document.getElementById("opticalCopayList").disabled="disabled";
		 document.getElementById("opticalCopayList").value = "";
		 document.getElementById("opticalFrameLimitList").disabled="disabled";
		 document.getElementById("opticalFrameLimitList").value = "";
		 document.getElementById("opticalLivesYN").disabled="disabled"; 
	}else if(obj.value == "N"){
		 $(".spanOptical2").attr("style","display:none");
		 document.getElementById("opticalLivesYN").disabled="disabled";
		 document.forms[1].opticalLivesYN.value= "N";
		 document.getElementById("opticalLivesYN").value = "N";
		 document.getElementById("opticalLimitList").disabled="disabled";
		 document.getElementById("opticalLimitList").value = "";
		 document.getElementById("opticalCopayList").disabled="disabled";
		 document.getElementById("opticalCopayList").value = "";
		 document.getElementById("opticalFrameLimitList").disabled="disabled";
		 document.getElementById("opticalFrameLimitList").value = "";
	}else if(obj.value == "Y") {
		 $(".spanOptical2").removeAttr("style");
	     document.getElementById("opticalLivesYN").disabled="disabled";
		 document.getElementById("opticalLivesYN").value = "Y";
		 document.getElementById("opticalLimitList").disabled="";
		 document.getElementById("opticalCopayList").disabled="";
		 document.getElementById("opticalFrameLimitList").disabled="";
	}
}

function setSelectModeType(focusId)
{
	 
	   document.forms[1].mode.value="setSelectModeType";
	   document.forms[1].action="/SwInsPricingActionvalidate.do?focusId="+focusId;
	   document.forms[1].submit();
}

function onMaternityChange(obj)
{
	if(obj.value == "N"||obj.value == "")
		{
		 $(".spanMeternity2").attr("style","display:none");
		 document.getElementById("maternityLimitList").disabled="disabled";
		 document.getElementById("maternityLimitList").value = "";
		 document.getElementById("totalLivesMaternity").readOnly="true";
		 document.getElementById("totalLivesMaternity").value="0";
		 document.getElementById("totalLivesMaternity").className="textBox textBoxLarge textBoxDisabled";
		 
		 $(".spanMaternityCopay").attr("style","display:none");
		 document.getElementById("maternityCopay").disabled="disabled";
		 document.getElementById("maternityCopay").value = "";
		 document.getElementById("maternityCopayPerc").readOnly="true";
		 document.getElementById("maternityCopayPerc").value="0";
		}
	else{
		 $(".spanMeternity2").removeAttr("style");
		 document.getElementById("maternityLimitList").disabled="";
		 document.getElementById("totalLivesMaternity").readOnly="";
		 document.getElementById("totalLivesMaternity").className="textBox textBoxLarge";
		 
		 $(".spanMaternityCopay").removeAttr("style");
		 document.getElementById("maternityCopay").disabled="";
		 document.getElementById("maternityCopayPerc").readOnly="";
		// document.getElementById("totalLivesMaternity").value="";
	    }
}


function checkPolicyNumber(obj)
{

	if(document.forms[1].renewalYN.value == "Y"){
	var policyNumber = obj.value;
	document.forms[1].mode.value="doCheckPolicyNumber";
	document.forms[1].child.value="";
	document.forms[1].policyNumber.value=policyNumber;
	document.forms[1].action="/clientDetailAction.do";
	document.forms[1].submit();
	}
	
}
	
function validateLives(field)
{
	var totalLives = parseInt(document.forms[1].totalCovedLives.value);
	var maternityLives = parseInt(document.forms[1].totalLivesMaternity.value);
	
	if(maternityLives > totalLives)	{
		alert("Total covered lives eligible for maternity cannot be higher than total covered lives");
		field.value = "";
		return false;
	}
	
/*	if(totalLives > 0)
	{
		document.getElementById("maternityYN").disabled="disabled";
		document.getElementById("maternityYN").value = "Y";
	}
	else{
		document.getElementById("maternityYN").disabled="";
		document.getElementById("maternityYN").value = "";
	}*/
}



function renewalChange(){
	
	if( document.forms[1].renewalYN.value == "N"){
		 document.getElementById("previousPolicyNo").className="textBox textBoxLarge textBoxDisabled";
		 document.getElementById("previousPolicyNo").readOnly="true"; 
		 document.getElementById("previousPolicyNo").value = "";
	}
	else if( document.forms[1].renewalYN.value == "Y"){
		 document.getElementById("previousPolicyNo").className="textBox textBoxLarge";
		document.getElementById("previousPolicyNo").readOnly="";
	}
	
}

function formalMessage(){
	
	var pricingRefno = document.forms[1].pricingRefno.value;
	if( document.forms[1].newdataentry.value == "Y"){
	alert("Inputs have been saved . Please note the pricing reference number "+pricingRefno+" for future");
	}
}

function warnFutureDate(){
	var dateString = document.getElementById('coverStartDate').value;
	
	var sDate  = dateString.split("/");
	var myDate = new Date(sDate[2] ,sDate[1]-1, sDate[0]);
	var today = new Date();
	if( myDate < today ){
		if (confirm('Coverage start date entered is in the past, do you want to proceed?')) {
		 document.getElementById('coverStartDate').value = dateString;
		} else {
		  document.getElementById('coverEndDate').value="";
		} 
	 }
	var startDate=document.getElementById('coverStartDate').value;
	if(startDate.length==0){
	document.getElementById('coverEndDate').value="";
	}
	var coverStartDate_check=document.getElementById('coverStartDate_check').value;
	if(coverStartDate_check!=startDate){
		document.getElementById('coverEndDate').value="";
		document.getElementById('coverStartDate_check').value=startDate;
	}
	/*if(document.lastModified){
		alert('Modified...');
	}else{
		alert('Not modified...');
	}*/
}




function gettotallivesType(){
	     var flag="SCRN1";
	     var policy_no=document.getElementById("previousPolicyNo").value;
	     var tot_lives=parseInt(document.forms[1].totalCovedLives.value);
	     var grp_prof_seq_id="";
	    
	     
	     var  path="/asynchronAction.do?mode=getTotalLivesPricing&flag="+flag+"&policy_no="+policy_no+"&tot_lives="+tot_lives+"&grp_prof_seq_id="+grp_prof_seq_id;
	$.ajax({
	     url :path,
	     dataType:"text",
	     success : function(data) {
	     var res1 = data;
	     if(res1!=""){
	         alert(data);
	        }
	 	     }//function(data)
	 });

}

/*
 * //in DAO level value need to pass as N reminder
 * if("Y".equals(Obesitytreatment))
	{
	frmSwPricing.set("gastricBinding", "Y");
	}
else{
	frmSwPricing.set("gastricBinding", "N");
    }*/

function onObesityChange(obj)
{
	if(obj.value == "N"||obj.value == "")
		{
		 $(".spanObesity").attr("style","display:none");
		 document.getElementById("gastricBinding").disabled="disabled";
		 document.getElementById("gastricBinding").value = "N";
		}
	else{
		 $(".spanObesity").removeAttr("style");
		 document.getElementById("gastricBinding").disabled="";
		 document.getElementById("gastricBinding").value = "";
	    }
}


function onAhliHospChange(obj)
{
	var outpatient = document.getElementById("outpatientBenefit").value;
	
	
	 if(obj.value == "Y" && outpatient == "Y") {
		
		 $(".spaniscopayhosp").removeAttr("style");//is copyay only manadatory
		 $(".ipCopayAtAlAhli").removeAttr("style");
		 //document.getElementById("opConsultAlAhli").disabled="";
		 document.getElementById("alAhlihospOPservices").disabled="";
		 document.getElementById("ipCopayAtAlAhliId").disabled="";
		 }
	 else if(obj.value == "N" || obj.value == "")
		{
		
		 $(".spanhospital3").attr("style","display:none");
		 $(".spaniscopayhosp").attr("style","display:none");
		 $(".spancopayhosp").attr("style","display:none");
		 document.getElementById("opCopyalahlihosp").disabled="disabled";
		 document.getElementById("opCopyalahlihosp").value = "";
		 document.getElementById("opPharmacyAlAhli").disabled="disabled";
		 document.getElementById("opPharmacyAlAhli").value = "";
		 document.getElementById("opConsultAlAhli").disabled="disabled";
		 document.getElementById("opConsultAlAhli").value = "";
		 document.getElementById("opInvestnAlAhli").disabled="disabled";
		 document.getElementById("opInvestnAlAhli").value = "";
		 document.getElementById("opothersAlAhli").disabled="disabled";
		 document.getElementById("opothersAlAhli").value = "";
		 document.getElementById("alAhlihospOPservices").disabled="disabled";
		 document.getElementById("alAhlihospOPservices").value = "";		 
		 document.getElementById("ipCopayAtAlAhliId").disabled="disabled";
		 $(".ipCopayAtAlAhli").attr("style","display:none");
		}	
	 
}

function onOPCopayAhliHosp(obj)
{
	if(obj.value == ""){
		 document.getElementById("opPharmacyAlAhli").disabled="disabled";
		 document.getElementById("opPharmacyAlAhli").value = "";
		 document.getElementById("opConsultAlAhli").disabled="disabled";
		 document.getElementById("opConsultAlAhli").value = "";
		 document.getElementById("opInvestnAlAhli").disabled="disabled";
		 document.getElementById("opInvestnAlAhli").value = "";
		 document.getElementById("opothersAlAhli").disabled="disabled";
		 document.getElementById("opothersAlAhli").value = "";
		 document.getElementById("opCopyalahlihosp").disabled="disabled";
		 document.getElementById("opCopyalahlihosp").value = "";
	}else if(obj.value == "Y")
		{
		 $(".spanhospital").attr("style","display:none"); // al alali copay only madatory
		 $(".spanhospital3").attr("style","display:none");
		 $(".spaniscopayhosp").removeAttr("style");
		 $(".spancopayhosp").removeAttr("style");
		 document.getElementById("opCopyalahlihosp").disabled="";
		 document.getElementById("opPharmacyAlAhli").disabled="disabled";
		 document.getElementById("opPharmacyAlAhli").value = "";
		 document.getElementById("opConsultAlAhli").disabled="disabled";
		 document.getElementById("opConsultAlAhli").value = "";
		 document.getElementById("opInvestnAlAhli").disabled="disabled";
		 document.getElementById("opInvestnAlAhli").value = "";
		 document.getElementById("opothersAlAhli").disabled="disabled";
		 document.getElementById("opothersAlAhli").value = "";
		 
		}
	else if(obj.value == "N"){
		 $(".spaniscopayhosp").attr("style","display:none"); // al alali copay only madatory
		 $(".spancopayhosp").attr("style","display:none"); // al alali copay only madatory
		 $(".spanhospital").removeAttr("style");
		 $(".spanhospital3").removeAttr("style");
		 document.getElementById("opCopyalahlihosp").disabled="disabled";
		 document.getElementById("opCopyalahlihosp").value = "";
		 document.getElementById("opPharmacyAlAhli").disabled="";
		 document.getElementById("opConsultAlAhli").disabled="";
		 document.getElementById("opInvestnAlAhli").disabled="";
		 document.getElementById("opothersAlAhli").disabled="";
		 }
	
}
function onOPCopay(obj)
{
	if(obj.value =="" || obj.value =="6"){
		 document.getElementById("opDeductableList").disabled="";

	}else{
		 document.getElementById("opDeductableList").value = "";
		 document.getElementById("opDeductableList").disabled="disabled";
	}
}

function onOPservices(obj)
{
	if(obj.value ==""){
		$(".spanOpservices").attr("style","display:none");
		$(".spaniscopayOP1").attr("style","display:none");
		$(".spaniscopayOP2").attr("style","display:none");
		 document.getElementById("opCopaypharmacy").disabled="disabled";
		 document.getElementById("opCopaypharmacy").value = "";
		 document.getElementById("opInvestigation").disabled="disabled";
		 document.getElementById("opInvestigation").value = "";
		 document.getElementById("opCopyconsultn").disabled="disabled";
		 document.getElementById("opCopyconsultn").value = "";
		 document.getElementById("opCopyothers").disabled="disabled";
		 document.getElementById("opCopyothers").value = "";
		 document.getElementById("opCopayList").disabled="disabled";
		 document.getElementById("opCopayList").value="";
		 document.getElementById("opDeductableList").value="";
		 document.getElementById("opDeductableList").disabled="disabled";
	}else if(obj.value == "Y")
		{
		 $(".spanOpservices1").attr("style","display:none");
		 $(".spaniscopayOP1").removeAttr("style");
		 $(".spaniscopayOP2").removeAttr("style");
		 document.getElementById("opCopaypharmacy").disabled="disabled";
		 document.getElementById("opCopaypharmacy").value = "";
		 document.getElementById("opInvestigation").disabled="disabled";
		 document.getElementById("opInvestigation").value = "";
		 document.getElementById("opCopyconsultn").disabled="disabled";
		 document.getElementById("opCopyconsultn").value = "";
		 document.getElementById("opCopyothers").disabled="disabled";
		 document.getElementById("opCopyothers").value = "";
		 document.getElementById("opCopayList").disabled="";
			if(document.getElementById("opCopayList").value =="" || document.getElementById("opCopayList").value =="6"){
				 document.getElementById("opDeductableList").disabled="";
			}else{
				 document.getElementById("opDeductableList").value = "";
				 document.getElementById("opDeductableList").disabled="disabled";
			}
		}
	else if(obj.value == "N"){
		 $(".spanOpservices").removeAttr("style");
		 $(".spaniscopayOP1").removeAttr("style");
		 $(".spaniscopayOP2").attr("style","display:none");
		 document.getElementById("opCopaypharmacy").disabled="";
		 document.getElementById("opInvestigation").disabled="";
		 document.getElementById("opCopyconsultn").disabled="";
		 document.getElementById("opCopyothers").disabled="";
		 document.getElementById("opCopayList").value = "";
		 document.getElementById("opCopayList").disabled="disabled";
		 document.getElementById("opDeductableList").disabled="disabled";
		 document.getElementById("opDeductableList").value = "";
		 }
	
}

//function onchangeInpatient(obj,pageloadYN){
//	if(obj.value==""){
//		$(".spanOptical1").attr("style","display:none");
//		$(".spanDental1").attr("style","display:none");
//		$(".spanMeternity1").attr("style","display:none");
//		$(".otherspan").attr("style","display:none");
//		
//		document.getElementById("maxBenifitList").disabled="disabled";
//		document.getElementById("maxBenifitList").value = "";
//		document.getElementById("areaOfCoverList").disabled="disabled";
//		document.getElementById("areaOfCoverList").value = "";
//		document.getElementById("networkList").disabled="disabled";
//		document.getElementById("networkList").value = "";
//		document.getElementById("alAhlihospital").disabled="disabled";
//		document.getElementById("alAhlihospital").value = "";
//		document.getElementById("opticalYNid").disabled="disabled";
//		document.getElementById("opticalYNid").value = "";
//		document.getElementById("opticalLimitList").disabled="disabled";
//		document.getElementById("opticalLimitList").value = "";
//		document.getElementById("opticalCopayList").disabled="disabled";
//		document.getElementById("opticalCopayList").value = "";
//		document.getElementById("dentalYNid").disabled="disabled";
//		document.getElementById("dentalYNid").value = "";
//		document.getElementById("dentalLimitList").disabled="disabled";
//		document.getElementById("dentalLimitList").value = "";
//		document.getElementById("dentalcopayList").disabled="disabled";
//		document.getElementById("dentalcopayList").value = "";
//		document.getElementById("maternityYNId").disabled="disabled";
//		document.getElementById("maternityYNId").value = "";
//		document.getElementById("maternityLimitList").disabled="disabled";
//		document.getElementById("maternityLimitList").value = "";
//		document.getElementById("physiocoverage").disabled="disabled";
//		document.getElementById("physiocoverage").value = "";
//		document.getElementById("vitDcoverage").disabled="disabled";
//		document.getElementById("vitDcoverage").value = "";
//		document.getElementById("vaccImmuCoverage").disabled="disabled";
//		document.getElementById("vaccImmuCoverage").value = "";
//		document.getElementById("matComplicationCoverage").disabled="disabled";
//		document.getElementById("matComplicationCoverage").value = "";
//		document.getElementById("psychiatrycoverage").disabled="disabled";
//		document.getElementById("psychiatrycoverage").value = "";
//		document.getElementById("deviatedNasalSeptum").disabled="disabled";
//		document.getElementById("deviatedNasalSeptum").value = "";
//		document.getElementById("obesityid").disabled="disabled";
//		document.getElementById("obesityid").value = "";
//		document.getElementById("gastricBinding").disabled="disabled";
//		document.getElementById("gastricBinding").value = "";
//		document.getElementById("healthScreen").disabled="disabled";
//		document.getElementById("healthScreen").value = "";
//		document.getElementById("orthodonticsCopay").disabled="disabled";
//		document.getElementById("orthodonticsCopay").value = "";
//		document.getElementById("totalLivesMaternity").disabled="disabled";
//		document.getElementById("totalLivesMaternity").value = "";
//	}if(obj.value == "Y"||obj.value == "N"){
//		$(".spanOptical1").removeAttr("style");
//		$(".spanDental1").removeAttr("style");
//		$(".spanMeternity1").removeAttr("style");
//		$(".otherspan").removeAttr("style");
//		document.getElementById("maxBenifitList").disabled="";
//		document.getElementById("areaOfCoverList").disabled="";
//		document.getElementById("networkList").disabled="";
//		document.getElementById("alAhlihospital").disabled="";
//		document.getElementById("opticalYNid").disabled="";
//		document.getElementById("opticalLimitList").disabled="disabled";
//		document.getElementById("opticalCopayList").disabled="disabled";
//		document.getElementById("dentalYNid").disabled="";
//		document.getElementById("dentalLimitList").disabled="disabled";
//		document.getElementById("dentalcopayList").disabled="disabled";
//		document.getElementById("maternityYNId").disabled="";
//		document.getElementById("maternityLimitList").disabled="";
//		document.getElementById("physiocoverage").disabled="";
//		document.getElementById("vitDcoverage").disabled="";
//		document.getElementById("vaccImmuCoverage").disabled="";
//		document.getElementById("matComplicationCoverage").disabled="";
//		document.getElementById("psychiatrycoverage").disabled="";
//		document.getElementById("deviatedNasalSeptum").disabled="";
//		document.getElementById("obesityid").disabled="";
//		document.getElementById("gastricBinding").disabled="disabled";
//		document.getElementById("healthScreen").disabled="";
//		document.getElementById("orthodonticsCopay").disabled="";
//		document.getElementById("totalLivesMaternity").disabled="";
//	}
//}


function onchangeInpatient(obj,pageloadYN){
	var isIpCovered=document.getElementById("inpatientBenefit").value;
	if(isIpCovered=='N'){
		document.getElementById("ipCopayId").disabled="disabled";
		$(".spaniscopayIP1").attr("style","display:none");
	}else{
		$(".spaniscopayIP1").removeAttr("style");
		document.getElementById("ipCopayId").disabled="";
	}
}



function onIpCopay(element){
	var ipCopayIndex = document.forms[1].ipCopay.selectedIndex;
	var ipCopayText = document.forms[1].ipCopay.options[parseInt(ipCopayIndex)].text;
	document.forms[1].ipCopayPerc.value=ipCopayText;
}

function onMatCopay(element){
	var maternityCopayIndex = document.forms[1].maternityCopay.selectedIndex;
	var maternityCopayText = document.forms[1].maternityCopay.options[parseInt(maternityCopayIndex)].text;
	document.forms[1].maternityCopayPerc.value=maternityCopayText;
}


function onIpCopayAtAlAhli(element){
	var ipCopayAtAlAhliIndex = document.forms[1].ipCopayAtAlAhli.selectedIndex;
	var ipCopayAtAlAhliText = document.forms[1].ipCopayAtAlAhli.options[parseInt(ipCopayAtAlAhliIndex)].text;
	document.forms[1].ipCopayAtAlAhliPerc.value=ipCopayAtAlAhliText;
}


function onchangeOutpatient(obj,pageloadYN)
{ 
	if (pageloadYN == "N"){
		
		document.getElementById("alAhlihospital").value = "";//its mandatory
	}
	
	if(obj.value == "N"||obj.value=="")
	{
		
	 $(".spaniscopayOP1").attr("style","display:none");
	 $(".spaniscopayOP2").attr("style","display:none");
	 $(".spanOpservices").attr("style","display:none");
	 $(".spanOpservices1").attr("style","display:none");
	 $(".spaniscopayOP").attr("style","display:none");
	 
	 document.getElementById("opCopayList").disabled="disabled";
	 document.getElementById("opCopayList").value = "";
	 document.getElementById("opDeductableList").value = "";
	 document.getElementById("opDeductableList").disabled="disabled";
	 document.getElementById("opdeductableserviceYN").disabled="disabled";
	 document.getElementById("opdeductableserviceYN").value = "";
	 document.getElementById("opCopaypharmacy").disabled="disabled";
	 document.getElementById("opCopaypharmacy").value = "";
	 document.getElementById("opInvestigation").disabled="disabled";
	 document.getElementById("opInvestigation").value = "";
	 document.getElementById("opCopyconsultn").disabled="disabled";
	 document.getElementById("opCopyconsultn").value = "";
	 document.getElementById("opCopyothers").disabled="disabled";
	 document.getElementById("opCopyothers").value = "";
		
	 $(".spanhospital1").attr("style","display:none");
	 $(".spaniscopayhosp").attr("style","display:none");
	 $(".spancopayhosp").attr("style","display:none");
	 document.getElementById("opCopyalahlihosp").disabled="disabled";
	 document.getElementById("opCopyalahlihosp").value = "";
	 document.getElementById("opPharmacyAlAhli").disabled="disabled";
	 document.getElementById("opPharmacyAlAhli").value = "";
	 document.getElementById("opConsultAlAhli").disabled="disabled";
	 document.getElementById("opConsultAlAhli").value = "";
	 document.getElementById("opInvestnAlAhli").disabled="disabled";
	 document.getElementById("opInvestnAlAhli").value = "";
	 document.getElementById("opothersAlAhli").disabled="disabled";
	 document.getElementById("opothersAlAhli").value = "";
	 document.getElementById("alAhlihospOPservices").disabled="disabled";
	 document.getElementById("alAhlihospOPservices").value = "";
	 
	}
else if(obj.value == "Y"){

	$(".spaniscopayOP1").removeAttr("style");
	$(".spanhospital1").removeAttr("style");
	$(".spanOpservices1").removeAttr("style");
	$(".spaniscopayOP").removeAttr("style");
	//$(".spaniscopayhosp").removeAttr("style");
	document.getElementById("opdeductableserviceYN").disabled="";
	//document.getElementById("alAhlihospOPservices").disabled=""; 
}	
}//function close

function showTemplate()
{
	
	   document.forms[1].mode.value="doshowTemplate";
	    document.forms[1].action = "/SwInsPricingActionIncome.do";
	    document.forms[1].submit();
}

function onUploadMemDetails()
{
	
/* var inp = document.getElementById('stmFile').value;
	    if(inp == ""){
	        alert("File Required");
	        document.getElementById('stmFile').focus();

	        return false;
	    }*/
	    if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doUploadMemDetails";
	    document.forms[1].action = "/SwInsPricingActionIncome.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}

function fetchScreen1()
{
	
	var renewalYN = document.getElementById("renewalYN").value;
	var clientcodeId = document.getElementById("clientcodeId").value;
	var clientName = document.getElementById("clientName").value;
	var coverStartDate = document.getElementById("coverStartDate").value;
	var coverEndDate = document.getElementById("coverEndDate").value;
	var totalCovedLives = document.getElementById("totalCovedLives").value;
	var previousPolicyNo = document.getElementById("previousPolicyNo").value;
	var policyFetchFlagElem = document.getElementById("vidalPolicyId").value;
	var	clientNameArray = "NA";
	if(clientName.search("&")!= -1){
		
		clientNameArray = clientName.split("&");
	}
	
    document.forms[1].tab.value="Group Profile";
    if(previousPolicyNo == null || previousPolicyNo == ""){
    	alert("Please enter Previous policy no to fetch the details");
    	return false;
    }

	if(!JS_SecondSubmit)
    {
		if(renewalYN!='Y'){
		alert('Fetch Key Coverages provision is only available for renewal pricing.');
	    document.forms[1].mode.value="fetchScreen1";
	    document.forms[1].action ="/SwInsPricingAction.do?previousPolicyNo="+previousPolicyNo+"&renewalYN="+renewalYN+"&clientcodeId="+clientcodeId+"" +
		"&coverStartDate="+coverStartDate+"&coverEndDate="+coverEndDate+"&totalCovedLives="+totalCovedLives+"&clientName="+clientName+"&clientNameArray="+clientNameArray; 
	    document.forms[1].submit();
	    
		}else{
			if(policyFetchFlagElem=='Y'){
				alert('Fetch Key Coverages provision is only available with past policy numbers that exists in the Vidal Health system.');
			}else{
//				if (confirm('The benefits will now be populated for Policy Number '+previousPolicyNo+'. Please press ok to proceed.')) {
					if (confirm("The benefits will be populated for Policy Number "+previousPolicyNo+". Coverage start date, end date will be populated assuming renewal of the most recent policy available. Please press 'OK' to proceed.")) {
				    document.forms[1].mode.value="fetchScreen1";
				    document.forms[1].action ="/SwInsPricingAction.do?previousPolicyNo="+previousPolicyNo+"&renewalYN="+renewalYN+"&clientcodeId="+clientcodeId+"" +
					"&coverStartDate="+coverStartDate+"&coverEndDate="+coverEndDate+"&totalCovedLives="+totalCovedLives+"&clientName="+clientName+"&clientNameArray="+clientNameArray; 
				    document.forms[1].submit();
				    }else{
						document.getElementById("previousPolicyNo").focus();
						return false;
					}
			}
		}
		
	   	 
}
    
    
  
    
}
// added by govind
function alphanumeric(inputtxt)
{ 
var letters = /^[0-9a-zA-Z]+$/;
if(!(inputtxt.value.match(letters)))
{
	alert('Please input alphanumeric characters only');
	   document.getElementById("opticalLimitOth").focus();
	   return false;
}

}

function checkCopay(element){
	var networkType=document.getElementById("networkList").value;
	var elemName=element.name;
	var elemValue=element.value;
	var x = element.children[0];
	
	if(networkType!=1){
		if((elemName=='opCopaypharmacy'||elemName=='opInvestigation'||elemName=='opCopyothers')&&elemValue==16){
			element.value='';
			x.setAttribute("selected", "selected");
			alert('Copay selection is not applicable for the selected network');
		}if(elemName=='opCopyconsultn' && elemValue==13){
			element.value='';
			x.setAttribute("selected", "selected");
			alert('Copay selection is not applicable for the selected network');
		}		
	}if(networkType==3||networkType==4){
		if((elemName=='opCopaypharmacy'||elemName=='opInvestigation'||elemName=='opCopyothers')&&elemValue==17){
			element.value='';
			x.setAttribute("selected", "selected");
			alert('Al Emadi and Aster Hospital are not part of the selected network. Please review selection of network.');
		}if(elemName=='opCopyconsultn' && elemValue==12){
			element.value='';
			x.setAttribute("selected", "selected");
			alert('Al Emadi and Aster Hospital are not part of the selected network. Please review selection of network.');
		}
	}
	}

function checkNetwork(element){
	var networkType=element.value;
	var existingNetworkType=document.getElementById("networkTypeId").value;
	var opCopaypharmacyVal=document.getElementById("opCopaypharmacy").value;
	var opInvestigationVal=document.getElementById("opInvestigation").value;
	var opCopyothersVal=document.getElementById("opCopyothers").value;
	var opCopyconsultnVal=document.getElementById("opCopyconsultn").value;
	if(networkType!=1){
		var option = element.children[existingNetworkType];
		if(opCopaypharmacyVal==16||opInvestigationVal==16||opCopyothersVal==16){
			element.value=existingNetworkType;
			option.setAttribute("selected", "selected");
			alert('Copay selection is not applicable for the selected network');
		}if(opCopyconsultnVal==13){
			element.value=existingNetworkType;
			option.setAttribute("selected", "selected");
			alert('Copay selection is not applicable for the selected network');
		}		
	}
	if(networkType==3||networkType==4){
		var option = element.children[existingNetworkType];
		if(opCopaypharmacyVal==17||opInvestigationVal==17||opCopyothersVal==17){
			element.value=existingNetworkType;
			option.setAttribute("selected", "selected");
			alert('Al Emadi and Aster Hospital are not part of the selected network. Please review selection of network.');
		}if(opCopyconsultnVal==12){
			element.value=existingNetworkType;
			option.setAttribute("selected", "selected");
			alert('Al Emadi and Aster Hospital are not part of the selected network. Please review selection of network.');
		}
	}
}

function checkPolicyNo(element){
		    document.forms[1].mode.value = "getPolicyInfo";
		    document.forms[1].action = "/clientDetailAction.do";
		    document.forms[1].submit();
/*	var value=element.value;
	var  path="/asynchronAction.do?mode=getPolicyInfo&policyNumber="+value;
	$.ajax({
	     url :path,
	     dataType:"text",
	     success : function(data) {
	     var res1 = data;
	     if(res1!=""){
	         var policyFetchFlagElem=document.getElementById("vidalPolicyId");
	         policyFetchFlagElem.value=data;
	      }
	 	 }
	 });*/
}

function checkValue(element){
	var elemName=element.name;
	var elemValue=0;
	if(element.value!='')
		elemValue=parseInt(element.value);
	else{
		if('maxBeneLimitOth'==elemName){
			alert('\'Maximum benefit limit (Others)\’ cannot be empty');
			return false;
		}
		if('opticalLimitOth'==elemName){
			alert('\'Optical limit (Others)\' cannot be empty');
			return false;
		}
		if('dentalLimitOth'==elemName){
			alert('\'Dental limit (Others)\' cannot be empty');
			return false;
		}
		if('maternityLimitOth'==elemName){
			alert('\'Maternity limit (Others)\' cannot be empty');
			return false;
		}
		element.focus();
	}
	if(element.value!=''){
		if('maxBeneLimitOth'==elemName){
			if(elemValue>10000000){
				element.value = '';
				element.focus();
				alert('\'Maximum benefit limit (Others)\’ cannot be higher than the maximum available option under \‘Maximum benefit limit\’');
				return false;
			}if(elemValue==0){
				element.value = '';
				element.focus();
				alert('\'Maximum benefit limit (Others)\' cannot be zero.');
				return false;
			}
		}
		if('opticalLimitOth'==elemName){
			if(elemValue>18200){
				element.value = '';
				element.focus();
				alert('\'Optical limit (Others)\' cannot be higher than the maximum available option under \'Optical limit\'.');
				return false;
			}if(elemValue==0){
				element.value = '';
				element.focus();
				alert('\'Optical limit (Others)\' cannot be zero.');
				return false;
			}
		}
		if('dentalLimitOth'==elemName){
			if(elemValue>72800){
				element.value = '';
				element.focus();
				alert('\'Dental limit (Others)\' cannot be higher than the maximum available option under \'Dental limit\'.');
				return false;
			}if(elemValue==0){
				element.value = '';
				element.focus();
				alert('\'Dental limit (Others)\' cannot be zero.');
				return false;
			}
		}
		if('maternityLimitOth'==elemName){
			if(elemValue>80080){
				element.value = '';
				element.focus();
				alert('\'Maternity limit (Others)\' cannot be higher than the maximum available option under \'Maternity limit\'.');
				return false;
			}if(elemValue==0){
				element.value = '';
				element.focus();
				alert('\'Maternity limit (Others)\' cannot be zero.');
				return false;
			}
		}
	}
	
	
}

function checkPolicyStatus(element){
	document.forms[1].mode.value = "checkPolicyStatus";
    document.forms[1].action = "/clientDetailAction.do?RevewalYN="+element.value;
    document.forms[1].submit();
}


function viewFiles(){
	document.forms[1].mode.value="doViewFiles";
    document.forms[1].action = "/SwInsPricingActionIncome.do";
    document.forms[1].submit();
}