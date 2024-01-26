//This is javascript file for the definerule.jsp

function onSave()
{
	trimForm(document.forms[1]);
	if(isValidated())
	{
		if(!JS_SecondSubmit)
		{
			
			if(document.getElementById("dsp.11.1.2.2") != null && document.getElementById("dsp.11.1.3.3") != null && document.getElementById("dsp.11.1.4.3") != null && document.getElementById("dsp.11.1.5.3") != null ){
				var LimitAmount = document.getElementById("dsp.11.1.2.2").value;
				var optcLimitAmount=parseFloat(LimitAmount);
			    var consultationLimitAmount = document.getElementById("dsp.11.1.3.3").value;
			    if(consultationLimitAmount == ""){
			    	consultationLimitAmount = 0;
			    }else if(document.getElementById("dsp.11.1.3.1").value=="EXC"){
			    	consultationLimitAmount = 0;
			    }
				var opticalInvestigationLimitAmount = document.getElementById("dsp.11.1.4.3").value;
				if(opticalInvestigationLimitAmount == ""){
					opticalInvestigationLimitAmount = 0;
				}else if(document.getElementById("dsp.11.1.4.1").value=="EXC"){
					opticalInvestigationLimitAmount = 0;
			    }
				var framesAndLensLimitAmount = document.getElementById("dsp.11.1.5.3").value;
				if(framesAndLensLimitAmount == ""){
					framesAndLensLimitAmount = 0;
				}else if(document.getElementById("dsp.11.1.5.1").value=="EXC"){
					framesAndLensLimitAmount = 0;
			    }
				var totalsum = parseFloat(consultationLimitAmount)+parseFloat(opticalInvestigationLimitAmount)+parseFloat(framesAndLensLimitAmount);
				if(optcLimitAmount < totalsum){
					alert("Optical limit amount should be greater than or equal  to the sum of Consultation, Investigation and Frame& Lens.");
					return;
				  }
			  }
			document.forms[1].mode.value="doSave";
			document.forms[1].action="/UpdateRuleAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
	}
}

function onReconfigure()
{
	trimForm(document.forms[1]);
	document.forms[1].mode.value="doReconfigure";
	document.forms[1].child.value="Clause List";
	document.forms[1].action="/EditRuleAction.do";
	document.forms[1].submit();
}

function onClose()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doClose";
	document.forms[1].child.value="";
	document.forms[1].action="/RuleAction.do";
	document.forms[1].submit();
}
function isValidated()
{
	if(document.forms[0].sublink.value=="Products")
	{
		if(isDate(document.forms[1].fromDate,"Rule Valid From ")==false)
				return false;
	}//end of if(document.forms[0].sublink.value=="Products")
	return true;
}//end of isValidated()

//This function is called when Revise button is pressed
function onReviseRule()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doReviseRule";
	document.forms[1].action="/RuleAction.do";
	document.forms[1].submit();
}//end of onReviseRule()

//This function is called when Verify button is pressed
function onVerifyRule()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doVerifyRule";
	document.forms[1].action="/RuleVerification.do";
	document.forms[1].submit();
}//end of onVerifyRule()

function onClauseSelection(field)
{
	var name=field.name;
	var regexp=new RegExp("^("+name+").*$");
	if(field.checked==false)
	{
		for(i=0;i<document.forms[1].length;i++)
		{
			if(document.forms[1].elements[i].type =="checkbox"  && regexp.test(document.forms[1].elements[i].name))
			{
				document.forms[1].elements[i].checked=false;
				onCoverageSelection(document.forms[1].elements[i]);
			}
		}
	}
}

function onCoverageSelection(element)
{
	var name=element.name;
	if(element.checked==false)
	{
		var regexp1=new RegExp("^("+name+").*}$");
		for(i=0;i<document.forms[1].length;i++)
		{
			if(regexp1.test(document.forms[1].elements[i].name))
			{
				if(document.forms[1].elements[i].type =="text" ||document.forms[1].elements[i].type =="textarea")
					document.forms[1].elements[i].value="";
			}
		}
	}
}

//This function is used to show/hide the conditions based on the
//status of the coverage
function showHideCondition(element)
{
	var id="coverage"+element.name;
	if(document.getElementById(id))
	{
		if(element.value == 3)
		{
		  document.getElementById(id).style.display="";
		}
		else
		{
			document.getElementById(id).style.display="none";
		}
	}
}//end of showHideCondition(element)


function validateMaternity() {
	 var selBox=document.getElementById("dsp.18.1.2.1");
	
	 if(selBox!=null){
	
		 if(selBox.value==="Y"){
		 document.getElementById("mnt18").style.display=""; 
	 }else{
		 document.getElementById("mnt18").style.display="none";
	 }
	 }	
	 return;
}

function onConfiguration()
{
	document.forms[1].mode.value="doConfiguration";
	document.forms[1].action="/EditPoliciesSearchAction.do";
	document.forms[1].submit();
}//end of onConfiguration()


function onConfiguration2()
{
	document.forms[1].mode.value="getPolicyBenefits";
	document.forms[1].action="/getPolicyBenefits.do";
	document.forms[1].submit();
}//end of onConfiguration()

function hideGeneralExclusionFields() 
{
		var ids = ["dsp.23.70.1.1","dsp.23.70.1.2","dsp.23.70.1.3","dsp.23.70.1.4","dsp.23.70.1.5",
		           "dsp.23.71.1.1","dsp.23.71.1.2","dsp.23.71.1.3","dsp.23.71.1.4","dsp.23.71.1.5",
		           "dsp.23.72.1.1","dsp.23.72.1.2","dsp.23.72.1.3","dsp.23.72.1.4","dsp.23.72.1.5",
		           "dsp.23.73.1.1","dsp.23.73.1.2","dsp.23.73.1.3","dsp.23.73.1.4","dsp.23.73.1.5",
		           "dsp.23.74.1.1","dsp.23.74.1.2","dsp.23.74.1.3","dsp.23.74.1.4","dsp.23.74.1.5"];
		
		for (var i = 0; i < ids.length; i++) 
		{
			var  obj=document.getElementById(ids[i]);
			if(obj!=null)
			obj.style.display="none";
		}
}

function validateChronic() {
	var selBox = document.getElementById("dsp.15.1.2.1").value;
	if (selBox != null) {
		if (selBox === "OCL") {
			document.getElementById("chr15").style.display = "";
			document.getElementById("chr16").style.display = "";
			document.getElementById("chr17").style.display = "";	
		} else {
			document.getElementById("chr15").style.display = "none";
			document.getElementById("chr16").style.display = "none";
			document.getElementById("chr17").style.display = "none";
		}
	}
	return;
}

function validateOncology() {
	var selBox = document.getElementById("dsp.21.13.3.1");
	if (selBox != null) {
		if (selBox.value === "WOL") {
			document.getElementById("onc21").style.display = "";
			document.getElementById("onc22").style.display = "";
			document.getElementById("onc23").style.display = "";
		} else {
			document.getElementById("onc21").style.display = "none";
			document.getElementById("onc22").style.display = "none";
			document.getElementById("onc23").style.display = "none";
		}
	}
	return;
}


function validateBirthDefects() {
	var selBox = document.getElementById("dsp.21.5.4.1");
	if (selBox != null) {
		if (selBox.value === "WCL") {
			document.getElementById("brd21").style.display = "";
			document.getElementById("brd22").style.display = "";
			document.getElementById("brd23").style.display = "";
		} else {
			document.getElementById("brd21").style.display = "none";
			document.getElementById("brd22").style.display = "none";
			document.getElementById("brd23").style.display = "none";
		}
	}
	return;
}


function validatePsychiatricTreatment() {
	var selBox = document.getElementById("dsp.23.19.3.1");
	if (selBox != null) {
		if (selBox.value === "OPL") {
			document.getElementById("psy23").style.display = "";
			document.getElementById("psy24").style.display = "";
			document.getElementById("psy25").style.display = "";
		} else {
			document.getElementById("psy23").style.display = "none";
			document.getElementById("psy24").style.display = "none";
			document.getElementById("psy25").style.display = "none";
		}
	}
	return;
}




function viewPolicyRuleHistory(element)
{
	//document.forms[1].mode.value="viewPolicyRuleHistory";
	document.forms[1].mode.value="getPolicyRuleHistoryList";
	document.forms[1].action="/UpdateRuleAction.do";
	element.innerHTML	=	"<b>"+ "Please Wait...!!"+"</b>";
	document.forms[1].submit();
}

function onHistoryClose()
{
	document.forms[1].mode.value="onHistoryClose";
	document.forms[1].child.value="";
	document.forms[1].action="/UpdateRuleAction.do";
	document.forms[1].submit();
}



