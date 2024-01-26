function gotoGrade()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value = "doViewGrading";
		document.forms[1].child.value="";
		document.forms[1].action = "/HospitalGradingAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of edit(rownum)

function onSave()
{
 if(!JS_SecondSubmit)
 {	
	for(var i=0;i<document.forms[1].answer1List1.length;i++)
	{
		if(document.forms[1].answer1List1[i].checked)
			document.forms[1].selectedAnswer1List[i].value="Y";
		else
			document.forms[1].selectedAnswer1List[i].value="N";
	}
	document.forms[1].mode.value="doSaveClinicalServices";
	document.forms[1].action="/UpdateGradingClinicAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
 }//end of if(!JS_SecondSubmit)	
}//end of onSave()

function onReset()
{
	if(!JS_SecondSubmit)
	 {
		if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
		{
			document.forms[1].mode.value="doViewClinicalServices";
			document.forms[1].action="/UpdateGradingClinicAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
		else
		{
			document.forms[1].reset();
		}//end of else
	 }
}//end of onReset()

function onAddClinical(obj)
{
	document.forms[1].mode.value="doServicesAdd";
	document.forms[1].action = "/GradingAddServiceAction.do";
	JS_SecondSubmit=true;
	document.forms[1].serviceType.value=obj;
	document.forms[1].submit();
}
