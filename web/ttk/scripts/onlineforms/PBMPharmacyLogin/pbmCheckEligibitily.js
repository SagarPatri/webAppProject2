function onCheckEligibility(){	
	
	if(document.forms[1].enrollId.value=='Al Koot Id / Qatar Id')
		document.forms[1].enrollId.value='';
	
	var selIndex = document.forms[1].benifitTypeID.selectedIndex;
	var selText = document.forms[1].benifitTypeID.options[parseInt(selIndex)].text;
	var benifitTypeID	=	document.getElementById("benifitTypeID").value;
	
	document.forms[1].mode.value="doPbmCheckEligibility";
	document.forms[1].action = "/PbmPharmacyGeneralAction.do?&benifit="+selText+"&benifitTypeID="+benifitTypeID;
	document.forms[1].submit();
	
}




function onClose()
{
	document.forms[1].tab.value="PBM Check Eligibility";	
	document.forms[1].sublink.value="PBM Check Eligibility";
	document.forms[1].mode.value="doPreauthProccess";
	document.forms[1].action="/PbmPharmacyGeneralAction.do";
    document.forms[1].submit();
}



function onProceed(){		
	/*document.forms[1].mode.value ="doProceed";
	document.forms[1].child.value ="PreauthProccess";
	document.forms[1].action="/PbmPharmacyGeneralAction.do";
	document.forms[1].submit();*/
	
	
	
	
	/*document.forms[1].tab.value="PBM Check Eligibility";	
	document.forms[1].sublink.value="PBM Check Eligibility";*/
	
	
	
	//var enrolId = document.forms[1].enrollId.value();
	//var enrolId	=	document.getElementById("enrollId").value;
	
	document.forms[1].mode.value="doProceed";
	document.forms[1].action="/PbmPharmacyGeneralAction.do";
	//document.forms[1].action = "/PbmPharmacyGeneralAction.do?&enrollId="+enrolId;
	document.forms[1].submit();
	
	
	
	
	
	
}


function onPrintForms()
{
	/*document.forms[0].tab.value ="Check Eligibility";
	document.forms[0].sublink.value ="Check Eligibility";
	document.forms[0].mode.value="doPrintForms";
	document.forms[0].action="/OnlineCashlessHospAction.do";
	document.forms[0].submit();*/
	var partmeter = "";
	if('DNTL'==document.forms[1].benifitTypeVal.value)
		partmeter = "?mode=doPrintDentalForms";
	else
		partmeter = "?mode=doPrintForms";
	var openPage = "/OnlineCashlessHospAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}










