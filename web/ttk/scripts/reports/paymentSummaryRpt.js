function onGenerateReport()
{
	
	var parameterValue	=	"";
	
	if(document.forms[1].floatAccNo.value == "")
	{
		alert("Please enter the Float Account No.");
		document.forms[1].floatAccNo.focus();
		return false;
	}//end of else if(document.forms[1].floatAccNo.value == "" )
	if(document.forms[1].searchBasedOn.value !="any"){
		if(document.forms[1].apprStartDate.value == "")
		{
			alert("Please select start date");
			document.forms[1].apprStartDate.focus();
			return false;
		}//end of else if(document.forms[1].floatAccNo.value == "" )

		
		
	}
	
	var corporatename = "";
	var providername = "";
	var partnerName = "";
	var apprStartDate = "";
	var apprEndDate = "";
	 var providerText ="";
	var floatAccNo = document.forms[1].floatAccNo.value;
	var searchBasedOn = document.forms[1].searchBasedOn.value;
	var claimTypeID = document.forms[1].claimTypeID.value;
	
	
	if(searchBasedOn !="any"){
	 apprStartDate = document.forms[1].apprStartDate.value;
	 apprEndDate = document.forms[1].apprEndDate.value;
	 
	 
	 if(apprStartDate!="" && apprEndDate !=""){
			
			if(compareDates("apprStartDate","Start Date","apprEndDate","End Date","greater than")==false)
			{
				document.forms[1].apprEndDate.value="";
				document.forms[1].apprEndDate.focus();
			    return false;
			}
			
		}
	 
	 
	 
	 
	}
	
	
	if(claimTypeID =="CTM"){
		 corporatename = document.forms[1].corporatename.value;
	}
	
	if(claimTypeID =="CNH"){
	 providername = document.forms[1].providername.value;
	 partnerName = document.forms[1].partnerName.value;
	 
	  var selecttype1=document.forms[1].providername;
	  providerText=selecttype1.options[selecttype1.options.selectedIndex].text;
	 
	 
	}
	
	
	
	
	if(claimTypeID =="CTM"){
		
		parameterValue = "?mode=pendingMemberRpt&parameterValue="+"|"+floatAccNo+"|"+searchBasedOn+"|"+apprStartDate+"|"+apprEndDate+"|"+claimTypeID+"|"+corporatename+"|"+providername+"|"+partnerName+"|";
	}
	
	else if(claimTypeID =="CNH"){
		
		
		if(partnerName !=""){

			parameterValue = "?mode=pendingNetworkPartnerRpt&parameterValue="+"|"+floatAccNo+"|"+searchBasedOn+"|"+apprStartDate+"|"+apprEndDate+"|"+claimTypeID+"|"+corporatename+"|"+providername+"|"+partnerName+"|";
		}
		else if(providername !=""){

			parameterValue = "?mode=pendingNetworkProviderRpt&parameterValue="+"|"+floatAccNo+"|"+searchBasedOn+"|"+apprStartDate+"|"+apprEndDate+"|"+claimTypeID+"|"+corporatename+"|"+providername+"|"+partnerName+"|";
		}
		else{
			parameterValue = "?mode=pendingNetworkRpt&parameterValue="+"|"+floatAccNo+"|"+searchBasedOn+"|"+apprStartDate+"|"+apprEndDate+"|"+claimTypeID+"|"+corporatename+"|"+providername+"|"+partnerName+"|";
		}
		
		
	}
	else{
		
		parameterValue = "?mode=doGeneratePaymentSummaryRpt&parameterValue="+"|"+floatAccNo+"|"+searchBasedOn+"|"+apprStartDate+"|"+apprEndDate+"|"+claimTypeID+"|"+corporatename+"|"+providername+"|"+partnerName+"|";
	}
	
	
	var openPage = "/ClaimsReportsAction.do"+parameterValue+"&reportType=EXCEL"+"&reportID="+document.forms[1].reportID.value+"&claimTypeID="+claimTypeID+"&providername="+providername+"&partnerName="+partnerName+"&providerText="+providerText;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
	
	
	
	
}






function onClose()
{
	
	document.forms[1].mode.value="doClose";
	document.forms[1].action="/TPAComReportsAction.do";
	document.forms[1].submit();
	
	
}


function onChangeSearchBasedOn()
{
	
	document.forms[1].mode.value="doChangeSearchBasedOn";
	document.forms[1].action="/ClaimsReportsAction.do";
	document.forms[1].submit();
	
	
}




function onChangeClaimType()
{
	
	document.forms[1].mode.value="doChangeClaimType";
	document.forms[1].action="/ClaimsReportsAction.do";
	document.forms[1].submit();
	
	
}




function onChangeProviderOrPartner()
{
	
	document.forms[1].mode.value="doChangeProviderOrPartner";
	document.forms[1].action="/ClaimsReportsAction.do";
	document.forms[1].submit();
	
	
}








