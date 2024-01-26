//bajaj


function onClose()
 {
	
 	if(!TrackChanges()) return false;
 	document.forms[1].mode.value = "doClose";
 	document.forms[1].action = "/SwInsuranceApproveConfiguration.do";
 	document.forms[1].submit();	
 }//end of onClose() 


function onSave()
{
		trimForm(document.forms[1]); 
		if(!JS_SecondSubmit)
		{
		    document.forms[1].mode.value="doSave"; 
		    document.forms[1].action="/UpdatePolicyDesignConfiguration.do"; 
		    JS_SecondSubmit=true;
		   document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
}//end of onSave()


function onSaveDemography()
{
	var flag ="N";
	/*for(var i=1;i<19;i++)
	{
		var currrentPolicy = document.getElementById("democurrentPolicy["+i+"]").value;
			if (currrentPolicy == "" ) {
			flag = "Y";
		}
	}
	
	if (flag == "Y" ) {
		 if (confirm("Are you sure you want to proceed without entering the current policy details?")){
			 
		 }
		 else{
			 document.getElementById("democurrentPolicy["+i+"]").focus();
			 return false;
		 }
		
	}*/
	
	
	trimForm(document.forms[1]); 
	if(!JS_SecondSubmit)
	{
	    document.forms[1].mode.value="doSaveDemography"; 
	    document.forms[1].action="/SwUpdatePolicyDesignConfiguration.do"; 
	    JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}

function onSaveCalculate()
{
	
	var totaliterate = document.getElementById("totaliterate").value;
	var prpINPCPM = document.getElementById("prpINPCPM").value;
	var prpOUTCPM = document.getElementById("prpOUTCPM").value;
	var prpMATCPM = document.getElementById("prpMATCPM").value;
	var prpOPTCPM = document.getElementById("prpOPTCPM").value;
	var prpDENTCPM = document.getElementById("prpDENTCPM").value;
	var isDurationComp = document.getElementById("durationcheckId").value;
	if ( isNaN( prpINPCPM ) || prpINPCPM == "" ) {
		prpINPCPM = 0;
	}
	if ( isNaN( prpOUTCPM ) || prpOUTCPM == "" ) {
		prpOUTCPM = 0;
	}
	if ( isNaN( prpMATCPM ) || prpMATCPM == "" ) {
		prpMATCPM = 0;
	}
	if ( isNaN( prpOPTCPM ) || prpOPTCPM == "" ) {
		prpOPTCPM = 0;
	}
	if ( isNaN( prpDENTCPM ) || prpDENTCPM == "" ) {
		prpDENTCPM = 0;
	}

	
	var increditfirstvalue = 0;
	var outpatientfirstvalue = 0;
	var maternityfirstvalue = 0;
	var opticalfirstvalue = 0;
	var dentalfirstvalue = 0;
			for(var i=0;i<totaliterate;i++)
			{
				var inpatientcrediblty = document.getElementById("inpatientcrediblty["+i+"]").value;
				var outpatientcrediblty = document.getElementById("outpatientcrediblty["+i+"]").value;
				var maternitycrediblty = document.getElementById("maternitycrediblty["+i+"]").value;
				var opticalcrediblty = document.getElementById("opticalcrediblty["+i+"]").value;
				var dentalcrediblty = document.getElementById("dentalcrediblty["+i+"]").value;
				
				if ( isNaN( inpatientcrediblty ) || inpatientcrediblty == "" ) {
					inpatientcrediblty = 0;
				}
				if ( isNaN( outpatientcrediblty ) || outpatientcrediblty == "" ) {
					outpatientcrediblty = 0;
				}
				if ( isNaN( maternitycrediblty ) || maternitycrediblty == "" ) {
					maternitycrediblty = 0;
				}
				if ( isNaN( opticalcrediblty ) || opticalcrediblty == "" ) {
					opticalcrediblty = 0;
				}
				if ( isNaN( dentalcrediblty ) || dentalcrediblty == "" ) {
					dentalcrediblty = 0;
				}
				increditfirstvalue =	 parseFloat(inpatientcrediblty)+parseFloat(increditfirstvalue);
				outpatientfirstvalue =	 parseFloat(outpatientcrediblty)+parseFloat(outpatientfirstvalue);
				maternityfirstvalue =	 parseFloat(maternitycrediblty)+parseFloat(maternityfirstvalue);
				opticalfirstvalue =	 parseFloat(opticalcrediblty)+parseFloat(opticalfirstvalue);
				dentalfirstvalue =	 parseFloat(dentalcrediblty)+parseFloat(dentalfirstvalue);

			}
			if(prpINPCPM > 0 && increditfirstvalue != 100)
			{
				alert("Inpatient credibility numbers should add up to 100%");
				return false;
			}
			
			if(prpOUTCPM > 0 && outpatientfirstvalue != 100)
			{
				alert("Outpatient credibility numbers should add up to 100%");
				return false;
			}
			
			if(prpMATCPM > 0 && maternityfirstvalue != 100)
			{
				alert("Maternity credibility numbers should add up to 100%");
				return false;
			}
			
			if(prpOPTCPM > 0 && opticalfirstvalue != 100)
			{
				alert("Optical credibility numbers should add up to 100%");
				return false;
			}
			
			if(prpDENTCPM > 0 && dentalfirstvalue != 100)
			{
				alert("Dental credibility numbers should add up to 100%");
				return false;
			}
	
		trimForm(document.forms[1]); 
		if(!JS_SecondSubmit)
		{
			if(isDurationComp=='false'){
				alert("Policy duration for one or more past policies here is not exactly 12 months. Please note this while finalizing credibility(%), as the cost shown is for the respective policy period and is not annualized.");
			}
				document.forms[1].mode.value="doSaveCalculate"; 
			    document.forms[1].action="/SwUpdatePolicyDesignConfiguration.do"; 
			    JS_SecondSubmit=true;
			    document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
	
}//end of onSave()





function onSaveLoading()
{
		trimForm(document.forms[1]); 
		var fieldvalue =parseFloat(document.getElementById("load_DeductTypePercentage[0]").value);
		var ContingencyLd =parseFloat(document.getElementById("load_DeductTypePercentage[1]").value);
		var ManagementDisc =parseFloat(document.getElementById("load_DeductTypePercentage[3]").value);
		
		var field =document.getElementById("load_DeductTypePercentage[0]").value;
		if(fieldvalue > 20){
			alert("Broking commission should not exceed 20%");
			field.focus();
			return false;
		}
		
		var comments = document.getElementById("loadComments").value;
		if(comments == "" && (ContingencyLd > 0 || ManagementDisc >0))
			{
			alert("Please specify comments");
			document.getElementById("loadComments").focus();
			return false;
			}
	
			
		if(!JS_SecondSubmit)
		{
			  document.forms[1].child.value="Edit";
		    document.forms[1].mode.value="doLoadingCalculate"; 
		    document.forms[1].action="/SwGrossPremiumCalculate.do"; 
		    JS_SecondSubmit=true;
		   document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
}//end of onSave()

function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset)	 {
	  document.forms[1].mode.value="doReset";
	  document.forms[1].action="/InsuranceApproveConfiguration.do";
	  document.forms[1].submit();
	 }//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	 else {
	  document.forms[1].reset();
	 }//end of else   
}//end of onReset()




 
function onGenerateFile()
{  
  	    var parameterValue=document.forms[1].groupseqid.value;
		document.forms[1].mode.value="doGenerateQuoteReport";
		document.forms[1].parameter.value=parameterValue; 
		document.forms[1].reportType.value="PDF";
		document.forms[1].action = "/ReportsAction.do";
		document.forms[1].submit();
}



function onClosePlan()
{
	document.forms[1].reset();
	var proceedbutton = "N";
    document.forms[1].mode.value = "doIncomeProfile";
    document.forms[1].tab.value ="Income Profile";
    document.forms[1].action = "/SwInsPricingActionIncome.do?proceedbutton="+proceedbutton;
    document.forms[1].submit();
}



function onGenerate()
 {
	document.forms[1].reset();
 	document.forms[1].mode.value = "doDefaultQuotation";
 	 document.forms[1].tab.value ="Generate Quote";
 	document.forms[1].action = "/GenerateQuotationAction.do";
 	document.forms[1].submit();	
 }



function onCalculateQuote()
{
		trimForm(document.forms[1]); 
		if(!JS_SecondSubmit)
		{
		    document.forms[1].mode.value= "doSaveCalculateQuote"; 
		    document.forms[1].tab.value = "Generate Quote";
		    document.forms[1].action="/UpdateGenerateQuotationAction.do"; 
		    JS_SecondSubmit=true;
		   document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
}

function loadingValidation(field)
{
	//alert(load_DeductType);
	//alert(load_DeductType.value);
	if(field.value > 20){
		alert("Loadings Discount Should Not Exceed 20%");
		field.focus();
		field.value="10";
		return false;
	}
		
}

function onViewInputSummary(){
	   var partmeter = "?mode=doViewInputSummary&parameter=|"+document.forms[1].groupseqid.value+"|"+document.forms[1].addedBy.value+"|&fileName=generalreports/viewInputPricing.jrxml&reportID=ViewInputPricing&reportType=PDF";
	   var openPage = "/SwFinalOutputAction.do"+partmeter;
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
	
}

function onViewPricingSummary(){
	   // document.forms[1].reset();
	   
	    document.forms[2].mode.value= "doDefaultQuotation"; 
	    document.forms[2].tab.value ="Generate Quote";
	    document.forms[2].action="/SwGenerateQuotationAction.do"; 
	    document.forms[2].submit();
	
}


function onViewGrosspremium(){
   // document.forms[1].reset();
    document.forms[1].mode.value= "doDefaultGrossPremium"; 
    document.forms[1].tab.value ="Gross Premium";
    document.forms[1].action="/SwGenerateQuotationAction.do"; 
    document.forms[1].submit();

}


function gettotallivesType(obj){
    var flag="SCRN3";
    var policy_no="";
    var tot_lives=obj.value;
    var grp_prof_seq_id=  document.getElementById("groupProfileSeqID").value;
  
    
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

function onCloseGrossPremium(){
	
	var proceedbutton = "N";
	 //   document.forms[1].reset();
	    document.forms[2].mode.value="doDefault"; 
	    document.forms[2].tab.value ="Plan design";
	    document.forms[2].action="/SwPlanDesignConfigurationAction.do?proceedbutton="+proceedbutton; 
	    document.forms[2].submit();
	
}



function onCloseOutput(){
	var proceedbutton = "N";
	if(!JS_SecondSubmit)
	{
	    document.forms[1].mode.value="doDefaultGrossPremium"; 
	    document.forms[1].tab.value ="Gross Premium";
	    document.forms[1].action="/SwFinalOutputAction.do?proceedbutton="+proceedbutton; 
	    JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}



function dateValidate(objDate) {
	var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
    var dateInput1 = objDate.value;
    if (!pattern.test(dateInput1)){
        alert("Date format should be (dd/mm/yyyy)");
        objDate.value="";
        objDate.focus();
        return false;
    }

}

function onbenefitvalueCheck(objvalue1,credibility){
	
	if(credibility == "inpatient")
		{
			if(document.getElementById("inPatientCPMvalue").value == ""){
				alert("Please enter inpatient current policy experience");
				objvalue1.value = "";
				return false;
			}
		}

	if(credibility == "outpatient")
		{
			if(document.getElementById("outPatientCPMvalue").value == ""){
				alert("Please enter outpatient current policy experience");
				objvalue1.value = "";
				return false;
			}
		}
		
	if(credibility == "maternity")
		{
			if(document.getElementById("maternityCPMvalue").value == ""){
				alert("Please enter maternity current policy experience");
				objvalue1.value = "";
				return false;
			}
		}
	if(credibility == "optical")
		{
			if(document.getElementById("opticalCPMvalue").value == ""){
				alert("Please enter optical current policy experience");
				objvalue1.value = "";
				return false;
			}
		}	
	if(credibility == "dental")
		{
			if(document.getElementById("dentalCPMvalue").value == ""){
				alert("Please enter dental current policy experience");
				objvalue1.value = "";
				return false;
			}
		}
	
	  
	
}

function onProceedProposal(){ 

	if(!JS_SecondSubmit)
	{
	    document.forms[1].mode.value="doDefault"; 
	    document.forms[1].tab.value ="Final Generate Quote";
	    document.forms[1].action="/SwFinalGenerateQuotationAction.do"; 
	    JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}
