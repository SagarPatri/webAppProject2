
function doViewHistory(authSeqID1){
	document.forms[1].authSeqID.value=authSeqID1;
	document.forms[1].mode.value = "doViewHistory";		
	document.forms[1].action="/PreAuthHistoryAction.do";
	document.forms[1].submit();
	}

function closeHistory(){
	document.forms[1].mode.value = "doGeneral";	
	document.forms[1].reforward.value = "close";		
	document.forms[1].action="/PreAuthHistoryAction.do";
	document.forms[1].submit();
	}
function changeHistoryMode(){
	document.forms[1].mode.value = "historyList";	
	document.forms[1].action="/PreAuthHistoryAction.do";
	document.forms[1].submit();
	}
function changeBenefitTypeMode(){
	document.forms[1].mode.value = "historyList";	
	document.forms[1].action="/PreAuthHistoryAction.do";
	document.forms[1].submit();
	}
function generateClaimHistoryReport(){
	var bType=document.getElementById("bType").value; //benefitType
	var alkootId=document.getElementById("memberId").value; 

	var policySequenceId;
	var policyDet=document.getElementById("policyDet").value; 
	if(policyDet == "" || policyDet == null)
	{
		policySequenceId = document.getElementById("PolicySeqId").value;
	}
	else
	{	
		policySequenceId = document.getElementById("policyDet").value; 
	}
	document.forms[0].mode.value="GenerateReport";
	var partmeter = "?mode=doGenerateReport&parameter=||"+document.getElementById("memberSeqID").value+"|"+bType+"|"+alkootId+"|"+policySequenceId+"|&fileName=reports/customercare/ClaimHistory.jrxml&reportID=ClaimViewHis&reportType=PDF";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=0,status=1,toolbar=no,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}

function generatePreAuthHistoryReport(){
	var bType=document.getElementById("bType").value; //benefitType
	var alkootId=document.getElementById("memberId").value; 
	var policySequenceId;
	var policyDet=document.getElementById("policyDet").value; 
	if(policyDet == "" || policyDet == null)
	{
		policySequenceId = document.getElementById("PolicySeqId").value;
	}
	else
	{	
		policySequenceId = policyDet;
	}
	document.forms[0].mode.value="GenerateReport";
	var partmeter = "?mode=doGenerateReport&parameter=||"+document.getElementById("memberSeqId").value+"|"+bType+"|"+alkootId+"|"+policySequenceId+"|&fileName=reports/customercare/PreauthHistory.jrxml&reportID=PreautViewHis&reportType=PDF";
	var openPage = "/ReportsAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=0,status=1,toolbar=no,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}

function generateDownloadPATCLM()
{
	 var historyMode=document.getElementById("historyMode").value;
	 var memberSeqID=document.getElementById("memberSeqID").value;
	 if(historyMode=="PAT")
    {
		    document.forms[1].mode.value="doGenerateDownloadHistoryPATCLM";
		    var parameter = "?mode=doGenerateDownloadHistoryPATCLM&memberSeqID="+memberSeqID+"&historyMode=PAT&reportType=EXL";
			document.forms[1].action="/PreAuthHistoryAction.do"+parameter;
			document.forms[1].submit();
    }
	 else
		 {
	    	document.forms[1].mode.value="doGenerateDownloadHistoryPATCLM";
	    	var parameter = "?mode=doGenerateDownloadHistoryPATCLM&memberSeqID="+memberSeqID+"&historyMode=CLM&reportType=EXL";
			document.forms[1].action="/PreAuthHistoryAction.do"+parameter;
			document.forms[1].submit();
		 }
	  
}
function changePolicyDetails()
{	
	document.forms[1].mode.value = "historyList";	
	document.forms[1].action="/PreAuthHistoryAction.do";
	document.forms[1].submit();
} 

function getPolicyNo()
{
	 var policyDetails = document.getElementById("policyDet").value;
	 var polLable;
	 if(policyDetails != "" || policyDetails != null)
		polLable = document.getElementById('policyDet')[document.getElementById('policyDet').selectedIndex].innerHTML;
     	document.forms[1].policyNoLabel.value = polLable;
}