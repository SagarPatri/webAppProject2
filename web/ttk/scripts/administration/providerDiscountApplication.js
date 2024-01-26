function onChangeProviderDiscount()
{
	if(document.getElementById("FIN").checked == true)
	{	
		document.getElementById('showHideFin').style.display = 'block';
	}	
	if(document.getElementById("PATCLM").checked == true)
	{	
		document.getElementById("all").checked = false;
		document.getElementById("dental").checked = false;
		document.getElementById("optical").checked = false;
		document.getElementById("opMaternity").checked = false;
		document.getElementById("ipMaternity").checked = false;
		document.getElementById("opBenefit").checked = false;
		document.getElementById("ipBenefit").checked = false;
		document.getElementById('showHideFin').style.display = 'none';
	}	
}

function onCheckAll()
{
	if(document.getElementById("all").checked == true)
	{
		document.getElementById("all").checked = true;
		document.getElementById("dental").checked = true;
		document.getElementById("optical").checked = true;
		document.getElementById("opMaternity").checked = true;
		document.getElementById("ipMaternity").checked = true;
		document.getElementById("opBenefit").checked = true;
		document.getElementById("ipBenefit").checked = true;
	}
	if(document.getElementById("all").checked == false)
	{
	document.getElementById("all").checked = false;
	document.getElementById("dental").checked = false;
	document.getElementById("optical").checked = false;
	document.getElementById("opMaternity").checked = false;
	document.getElementById("ipMaternity").checked = false;
	document.getElementById("opBenefit").checked = false;
	document.getElementById("ipBenefit").checked = false;
	}
}

function onClose()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].action="/ProviderDiscountApplication.do";
	document.forms[1].submit();
}

function onSave()
{
	var checkObjALL=document.getElementById("all");
	if(checkObjALL.checked)
		 document.forms[1].all.value="Y";
	else
		 document.forms[1].all.value="N";
	
	var checkObjDental=document.getElementById("dental");
	
	if(checkObjDental.checked)
		 document.forms[1].dental.value="Y";
	else
		 document.forms[1].dental.value="N";
	
	var checkObjOptical=document.getElementById("optical");
	if(checkObjOptical.checked)
		 document.forms[1].optical.value="Y";
	else
		 document.forms[1].optical.value="N";
	
	var checkObjOPMaternity=document.getElementById("opMaternity");
	if(checkObjOPMaternity.checked)
		 document.forms[1].opMaternity.value="Y";
	else
		 document.forms[1].opMaternity.value="N";
	
	var checkObjIPMaternity=document.getElementById("ipMaternity");
	if(checkObjIPMaternity.checked)
		 document.forms[1].ipMaternity.value="Y";
	else
		 document.forms[1].ipMaternity.value="N";
	
	var checkObjOPBenefit=document.getElementById("opBenefit");
	if(checkObjOPBenefit.checked)
		 document.forms[1].opBenefit.value="Y";
	else
		 document.forms[1].opBenefit.value="N";
	
	var checkObjIPBenefit=document.getElementById("ipBenefit");
	if(checkObjIPBenefit.checked)
		 document.forms[1].ipBenefit.value="Y";
	else
		 document.forms[1].ipBenefit.value="N";
	
	document.forms[1].mode.value="doSaveProviderdisApp";
	document.forms[1].action="/ProviderDiscountApplication.do";
	document.forms[1].submit();
}