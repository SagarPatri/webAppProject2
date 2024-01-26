function onBatchReports()
{
    document.forms[1].mode.value="doBatchReports";
	document.forms[1].action="/AutoRejectionClaimConfiguration.do";
	document.forms[1].submit();
}//end of onPlan()

function onClose()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].action="/AutoRejectionClaimConfiguration.do";
	document.forms[1].submit();
}//end of onClose()


function onGenerateXL()
{
	var batchNO=document.forms[1].batchNO.value;
	var iBatchRefNO=document.forms[1].iBatchRefNO.value;
	
	
	if(batchNO=="" && iBatchRefNO==""){
		alert("Please enter atleast one value");
		 document.forms[1].batchNO.focus();
		return false;
	}
	
	
	
	
	parameterValue=batchNO+"|"+iBatchRefNO+"|"+"EXL";
	
	
	var partmeter = "?mode=doGenerateXL&parameter="+parameterValue+"&reportType="+"EXL";
   	var openPage = "/AutoRejectionClaimConfiguration.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features); 
	
	
	
	
	
	
}//end of onPlan()

function onClosebatchReport()
{
	document.forms[1].mode.value="doClosebatchReport";
	document.forms[1].action="/AutoRejectionClaimConfiguration.do";
	document.forms[1].submit();
}//