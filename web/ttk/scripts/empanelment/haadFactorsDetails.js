function onClose()
{
	if(!TrackChanges()) return false;//end of if(!TrackChanges())
    document.forms[1].mode.value="doClose";
    document.forms[1].action="/UploadMOUCertificatesList.do";
    document.forms[1].submit();
}//end of onClose()


function onSaveHaadFactors()
{
	networkType	=	document.forms[1].networkType.value;
	if(document.forms[1].haadTarrifStartDt.value=="")
	{
		alert("Please Select Tariff Start Date");
		document.forms[1].haadTarrifStartDt.focus();
		return false;
	}
	if(!JS_SecondSubmit){ 
		document.forms[1].mode.value="doModifyMultipleHaadFactors";
		document.forms[1].action="/ModifyHaadFactorsHospitalAction.do?networkType="+networkType;
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}//end of onSaveHaadFactors()


function onUpdateHaadFactors()
{
	if(!JS_SecondSubmit){ 
		document.forms[1].mode.value="doUpdateHaadFactors";
		document.forms[1].action="/UpdateHaadFactorsHospitalAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}//end of onUpdateHaadFactors()
function onEditValues()
{
	if(!JS_SecondSubmit){ 
		document.forms[1].mode.value="doEditValues";
		document.forms[1].action="/EditHaadFactorsHospitalAction.do?editFlag=Edit";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}//end of onEditValues()

function changeNetworkType()
{
	networkType	=	document.forms[1].networkType.value;
	if(!JS_SecondSubmit){ 
		document.forms[1].mode.value="doChangeNetworkType";
		document.forms[1].action="/EditHaadFactorsHospitalAction.do?networkType="+networkType;
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}//end of changeNetworkType()



