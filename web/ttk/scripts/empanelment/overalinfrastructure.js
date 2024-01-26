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
}//end gotoGrade()

function onReset()
{
	if(!JS_SecondSubmit)
	 {
		if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
		{
			document.forms[1].mode.value="doViewOverallDetails";
			document.forms[1].action="/EditGradingInfraStrAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
		else
		{
			document.forms[1].reset();
		}//end of else
	}
}//end of onReset()
function trim(s)
{
	return s.replace(/^\s+/g,'').replace(/\s+$/g,'');
}
function onSave()
{
  trimForm(document.forms[1]);
  if(!JS_SecondSubmit)
  {	
	document.forms[1].mode.value="doSaveOverallDetails";
	document.forms[1].action="/SaveGradingInfraStrAction.do";
	JS_SecondSubmit=true;
 	document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}
