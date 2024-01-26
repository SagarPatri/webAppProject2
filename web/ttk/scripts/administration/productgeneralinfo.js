function onReset()
{

if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	{
		document.forms[1].mode.value="doReset";
		document.forms[1].action="/EditProductAction.do";
		document.forms[1].submit();
	}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	else
	{
		document.forms[1].reset();
	}//end of else

}//end of onReset()

function onSave()
{
    trimForm(document.forms[1]);
    if(!JS_SecondSubmit)
    {
    	/*if(document.forms[1].stopPreAuthsYN.checked)
    		document.forms[1].stopPreAuth.value="Y";
    	else
    		document.forms[1].stopPreAuth.value="N";
    	if(document.forms[1].stopClaimsYN.checked)
    		document.forms[1].stopClaim.value="Y";
    	else
    		document.forms[1].stopClaim.value="N";*/
			//KOC 1286 for OPD Benefit
    	if(document.forms[1].opdClaimsYN.checked)
    		document.forms[1].opdClaim.value="Y";
    	else
    		document.forms[1].opdClaim.value="N";
    	//KOC 1286 for OPD Benefit
			
			//KOC 1270 for hospital cash benefit
    	
    	if(document.forms[1].cashBenefitsYN.checked)
    	{
    		document.forms[1].cashBenefit.value="Y";
    	}
    	else
    	{
    		document.forms[1].cashBenefit.value="N";
    	}
    	
    	if(document.forms[1].convCashBenefitsYN.checked) 
    	{
    		document.forms[1].convCashBenefit.value="Y";
    	}
    	else
    	{
    		document.forms[1].convCashBenefit.value="N";
    	}
    	
    	//KOC 1270 for hospital cash benefit
    	
    	//added for KOC-1273
    	if(document.forms[1].criticalBenefitYN.checked)
    		document.forms[1].criticalBenefit.value="Y";
    	else
    		document.forms[1].criticalBenefit.value="N";
    	
    	if(document.forms[1].survivalPeriodYN.checked)
    		document.forms[1].survivalPeriod.value="Y";
    	else
    		document.forms[1].survivalPeriod.value="N";
    	
    	document.forms[1].mode.value="doSave";
		document.forms[1].action="/UpdateProductAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onSave()

function onCopyRules()
{
    trimForm(document.forms[1]);
    if(document.forms[1].insuranceSeqId.value=="")
    {
    	alert('Please select Healthcare Company');
    	document.forms[1].insuranceSeqId.focus();
	    return false;
    }
    if(document.forms[1].productSeqID.value=="")
    {
        alert('Please select Product Name');
    	document.forms[1].productSeqID.focus();
	    return false;
    }
    var msg =  confirm("Are you sure want to copy Product Rules from the selected Product?");
	if (msg == false)
		return false;	
    if(!JS_SecondSubmit)
    {
		document.forms[1].mode.value="doCopyRules";
		document.forms[1].action="/EditProductAction.do";
		JS_SecondSubmit=true
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onCopyRules()

function onChangeInsurance()
{
    trimForm(document.forms[1]);
    if(!JS_SecondSubmit)
    {
		document.forms[1].mode.value="doChangeInsurance";
		document.forms[1].action="/EditProductAction.do";
		JS_SecondSubmit=true
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onCopyRules()

function onConfiguration()
{
	document.forms[1].mode.value="doConfiguration";
	document.forms[1].action="/EditProductAction.do";
	document.forms[1].submit();
}//end of onConfiguration()

function stopPreAuthClaim()
{
	if(document.forms[1].stopPreAuth.value=="Y")
	    document.forms[1].stopPreAuthsYN.checked=true;
	else
		document.forms[1].stopPreAuthsYN.checked=false;
	if(document.forms[1].stopClaim.value=="Y")
		document.forms[1].stopClaimsYN.checked=true;
	else
		document.forms[1].stopClaimsYN.checked=false;
		//KOC 1286 for OPD Benefit
	if(document.forms[1].opdClaim.value=="Y")
		document.forms[1].opdClaimsYN.checked=true;
	else
		document.forms[1].opdClaimsYN.checked=false;
	//KOC 1286 for OPD Benefit
		
		//KOC 1270 for hospital cash benefit
	
	if(document.forms[1].cashBenefit.value=="Y") 
	{
		document.forms[1].cashBenefitsYN.checked=true;
	}
	else
	{
		document.forms[1].cashBenefitsYN.checked=false;
	}
	
	if(document.forms[1].convCashBenefit.value=="Y")  
	{
		document.forms[1].convCashBenefitsYN.checked=true;
	}
	else
	{
		document.forms[1].convCashBenefitsYN.checked=false;
	}
	//KOC 1270 for hospital cash benefit
}//end of stopPreAuthClaim()
function criticalBenefit()
{
	if(document.forms[1].criticalBenefit.value=="Y")
	    document.forms[1].criticalBenefitYN.checked=true;
	else
		document.forms[1].criticalBenefitYN.checked=false;
}

function survivalPeriod()
{
	if(document.forms[1].survivalPeriod.value=="Y")
		document.forms[1].survivalPeriodYN.checked=true;
	else
		document.forms[1].survivalPeriodYN.checked=false;
	
}
function getProductCode(obj)
{
	var prodname	=	obj.value;
	if (window.ActiveXObject){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		if (xmlhttp){
			xmlhttp.open("GET",'/EditProductAction.do?mode=getProductCode&prodname=' +prodname ,true);
			xmlhttp.onreadystatechange=state_GetProdCode;
			xmlhttp.send();
		}
	}
	
}

function state_GetProdCode(){
	var result,result_arr;
	if (xmlhttp.readyState==4){
		//alert(xmlhttp.status);
	if (xmlhttp.status==200){
				result = xmlhttp.responseText;
			result_arr = result.split(","); 
			document.forms[1].insProductCode.value=result_arr[0];
			
		}
	}
}
function setAuthorityLabel(obj) {
	if(obj.value=="HAAD"){
		document.getElementById("authorityID").innerHTML="Authority Product Reference No:";
	}else{
		document.getElementById("authorityID").innerHTML="Authority Product ID:";
	}
}

function validateCopay(selObj,cId) {
	if(cId==="MT"){
		if(selObj.value==="Y"){
			document.forms[1].maternityCopay.readOnly=false;
			document.forms[1].maternityCopay.style.backgroundColor="white";
			document.forms[1].maternityCopay.style.color="black"; 
		}else{
			document.forms[1].maternityCopay.readOnly=true;
			document.forms[1].maternityCopay.style.backgroundColor="#EEEEEE";
			document.forms[1].maternityCopay.style.color="#666666";
		    document.forms[1].maternityCopay.value=""; 
		}
	}else if(cId==="OT"){
		if(selObj.value==="Y"){
			document.forms[1].opticalCopay.readOnly=false;
			document.forms[1].opticalCopay.style.backgroundColor="white";
			document.forms[1].opticalCopay.style.color="black"; 
		}else{
			document.forms[1].opticalCopay.readOnly=true;
			document.forms[1].opticalCopay.style.backgroundColor="#EEEEEE";
			document.forms[1].opticalCopay.style.color="#666666"; 
			document.forms[1].opticalCopay.value=""; 
		}
	}else if(cId==="DT"){
		if(selObj.value==="Y"){
			document.forms[1].dentalCopay.readOnly=false;
			document.forms[1].dentalCopay.style.backgroundColor="white";
			document.forms[1].dentalCopay.style.color="black"; 
		}else{
			document.forms[1].dentalCopay.readOnly=true;
			document.forms[1].dentalCopay.style.backgroundColor="#EEEEEE";
			document.forms[1].dentalCopay.style.color="#666666"; 
			document.forms[1].dentalCopay.value=""; 
		}
	}
}
