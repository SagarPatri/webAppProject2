
	function onSwitch()
	{
		document.forms[1].action="/ClaimsDiscountActivityReport.do";
		document.forms[1].mode.value="doSwitchTypeChange";
		document.forms[1].submit();
	}// end of onSwitch()
	

	function onClose()
	{
		document.forms[1].action="/ClaimsDiscountActivityReport.do";
		document.forms[1].mode.value="doClose";
		document.forms[1].submit();
	}



	function onClmDetailSearch(element)
	{
	   if(!JS_SecondSubmit)
	 {
		trimForm(document.forms[1]);
		var providerName=document.forms[1].sProviderName.value;
		if((document.forms[1].patientName.value!=""  || document.forms[1].invoiceNo.value!="" ||
				document.forms[1].batchNo.value!="" || document.forms[1].alKootId.value!="" || 
				document.forms[1].claimNo.value!="" || document.forms[1].eventRefNo.value!="" || 
				document.forms[1].qatarId.value!="" || document.forms[1].payRefNo.value!=""))
			{
				trimForm(document.forms[1]);
				document.forms[1].mode.value = "doClmDiscountActReportSearch";
				document.forms[1].action = "/ClaimsDiscountActivityReport.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();	 
			}else{
				if((document.forms[1].tmtfromDate.value=="" || document.forms[1].tmttoDate.value=="") && (document.forms[1].clmFromDate.value=="" || document.forms[1].clmToDate.value=="")){
					alert("Please Select  Date Range!!!");
					document.forms[1].tmtfromDate.focus();
					return false;
				}
				if(document.forms[1].clmFromDate.value.length!=0)
				{
					if(!isDate(document.forms[1].clmFromDate,"\'Claim Submission From Date\'"))
						return false;

					if(isFutureDate(document.forms[1].clmFromDate.value)){
						alert("\'Claim Submission From Date\' should not be future date ");
						document.forms[1].clmFromDate.value='';
						document.forms[1].clmFromDate.focus();
						return false;
					}
				}
				if(document.forms[1].tmtfromDate.value.length!=0)
				{
					if(!isDate(document.forms[1].tmtfromDate,"\'Treatment Start date\'"))
						return false;

					if(isFutureDate(document.forms[1].tmtfromDate.value)){
						alert("\'Treatment Start date\' should not be future date ");
						document.forms[1].tmtfromDate.value='';
						document.forms[1].tmtfromDate.focus();
						return false;
					}
				}
				if(document.forms[1].clmToDate.value.length!=0)
				{
					if(!isDate(document.forms[1].clmToDate,"\'Claim Submission To Date\'"))
						return false;

					if(isFutureDate(document.forms[1].clmToDate.value)){
						alert("\'Claim Submission To Date\' should not be future date ");
						document.forms[1].clmToDate.value='';
						document.forms[1].clmToDate.focus();
						return false;
					}
				}
				if(document.forms[1].tmttoDate.value.length!=0)
				{
					if(!isDate(document.forms[1].tmttoDate,"\'Treatment End date\'"))
						return false;

					if(isFutureDate(document.forms[1].tmttoDate.value)){
						alert("\'Treatment End date\' should not be future date ");
						document.forms[1].tmttoDate.value='';
						document.forms[1].tmttoDate.focus();
						return false;
					}
				}
			}
		
		
		var stParts =document.forms[1].tmtfromDate.value.split('/');
		var endParts=document.forms[1].tmttoDate.value.split('/');
		var validateFlag=dateValidation(stParts,endParts,'Treatment Start date','Treatment End date');
			if(validateFlag==true){
				var stParts =document.forms[1].clmFromDate.value.split('/');
				var endParts=document.forms[1].clmToDate.value.split('/');
				var validateFlag=dateValidation(stParts,endParts,'Claim Submission From Date','Claim Submission To Date');
				if(validateFlag==true){
					document.forms[1].mode.value = "doClmDiscountActReportSearch";
					document.forms[1].action = "/ClaimsDiscountActivityReport.do";
					JS_SecondSubmit=true;
					element.innerHTML	=	"<b>"+ "Please Wait...!!"+"</b>";
					document.forms[1].submit();   
				}else{
					document.forms[1].clmToDate.value='';
					document.forms[1].clmToDate.focus();	
					return false;
				}	
			}else{
				document.forms[1].tmttoDate.value='';
				document.forms[1].tmttoDate.focus();
				return false;
			}		
	  }
	}

	function isFutureDate(argDate){
		var dateArr=argDate.split("/");	
		var givenDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
		var currentDate = new Date();
		if(givenDate>currentDate){
		return true;
		}
		return false;
	}


	function dateValidation(stParts,endParts,fromDateVar,toDateVar){
		var startDate = new Date(stParts[2], stParts[1] - 1, stParts[0]);
		var endDate = new Date(endParts[2], endParts[1] - 1, endParts[0]);
			if(startDate>endDate){
				alert("Please Provide \'"+toDateVar+"\' Greater Than \'"+fromDateVar+"\'!!!");
				return false;
			}else{
				var oneDay = 24*60*60*1000; 
				var diffDays = Math.round(Math.abs((startDate.getTime() - endDate.getTime())/(oneDay)));
				var stYear=startDate.getYear();
				var endYear=endDate.getYear();
				var stLeapYear=stYear % 4 == 0;
				var endLeapYear=endYear % 4 == 0;
				var validateFlag=false;
					if(stLeapYear==true||endLeapYear==true){
						if(diffDays > 90){
							validateFlag=true;
							alert("Allowed Duration For \'"+fromDateVar+"\' And \'"+toDateVar+"\' Upto 90 Day's Only!!! ");		
						}
					}else{
						if(diffDays > 89){
							validateFlag=true;
							alert("Allowed Duration For \'"+fromDateVar+"\ And \'"+toDateVar+"\' Upto 90 Day's Only!!! ");	
						}
					}				
				if(validateFlag==false){
						return true;
					}else{
						return false;
					}
			}	 		
	}

	
	
function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value = "doClmDiscountActReportSearch";
    document.forms[1].sortId.value=sortid;
    if(!JS_SecondSubmit){
    document.forms[1].action = "/ClaimsDiscountActivityReport.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value = "doClmDiscountActReportSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ClaimsDiscountActivityReport.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doClmDisActRptBackward";
    document.forms[1].action = "/ClaimsDiscountActivityReport.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doClmDisActRptForward";
    document.forms[1].action = "/ClaimsDiscountActivityReport.do";
	 JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}



	function onExportToExcel()
	{ 
		if((document.forms[1].patientName.value!=""  || document.forms[1].invoiceNo.value!="" ||
				document.forms[1].batchNo.value!="" || document.forms[1].alKootId.value!="" || 
				document.forms[1].claimNo.value!="" || document.forms[1].eventRefNo.value!="" || 
				document.forms[1].qatarId.value!="" || document.forms[1].payRefNo.value!=""))
			{
				trimForm(document.forms[1]);
				 document.forms[1].mode.value = "doClmDiscountActReportSearch";
				 document.forms[1].action = "/ClaimsDiscountActivityReport.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();	 
			}else{
				if((document.forms[1].tmtfromDate.value=="" || document.forms[1].tmttoDate.value=="") && (document.forms[1].clmFromDate.value=="" || document.forms[1].clmToDate.value=="")){
					alert("Please Select  Date Range!!!");
					document.forms[1].tmtfromDate.focus();
					return false;
				}
				if(document.forms[1].clmFromDate.value.length!=0)
				{
					if(!isDate(document.forms[1].clmFromDate,"\'Claim Submission From Date\'"))
						return false;

					if(isFutureDate(document.forms[1].clmFromDate.value)){
						alert("\'Claim Submission From Date\' should not be future date ");
						document.forms[1].clmFromDate.value='';
						document.forms[1].clmFromDate.focus();
						return false;
					}
				}
				if(document.forms[1].tmtfromDate.value.length!=0)
				{
					if(!isDate(document.forms[1].tmtfromDate,"\'Treatment Start date\'"))
						return false;

					if(isFutureDate(document.forms[1].tmtfromDate.value)){
						alert("\'Treatment Start date\' should not be future date ");
						document.forms[1].tmtfromDate.value='';
						document.forms[1].tmtfromDate.focus();
						return false;
					}
				}
				
			}
		
		
		var stParts =document.forms[1].tmtfromDate.value.split('/');
		var endParts=document.forms[1].tmttoDate.value.split('/');
		var validateFlag=dateValidation(stParts,endParts,'Treatment Start date','Treatment End date');
			if(validateFlag==true){
				var stParts =document.forms[1].clmFromDate.value.split('/');
				var endParts=document.forms[1].clmToDate.value.split('/');
				var validateFlag=dateValidation(stParts,endParts,'Claim Submission From Date','Claim Submission To Date');
				if(validateFlag==true){
					var trtmtStartDate=document.forms[1].tmtfromDate.value;
					var trtmtEndDate=document.forms[1].tmttoDate.value;
					var clmFromDate=document.forms[1].clmFromDate.value;
					var clmToDate=document.forms[1].clmToDate.value;
					var patientName=document.forms[1].patientName.value;
					var status=document.forms[1].claimStatus.value;
					var invoiceNumber=document.forms[1].invoiceNo.value;
					var batchNumber=document.forms[1].batchNo.value;
					var alKootId=document.forms[1].alKootId.value;
					var claimNumber=document.forms[1].claimNo.value;
					var benefitType=document.forms[1].benefitType.value;
					var eventRefNo=document.forms[1].eventRefNo.value;
					var qatarId=document.forms[1].qatarId.value;
					var paymentRefNumber=document.forms[1].payRefNo.value;
					var sProviderName=document.forms[1].sProviderName.value;
					var switchType=document.forms[1].switchType.value;
					if(switchType == 'QPR')
					{
						var finBatchNo =document.forms[1].finBatchNo.value;
						var financeStatus=document.forms[1].financeStatus.value;
						parameterValue =  "|"+"CDR"+"|"+sProviderName+"|"+trtmtStartDate+"|"+trtmtEndDate+"|"+clmFromDate+"|"+clmToDate+"|"+patientName+"|"+status+"|"+invoiceNumber+"|"+batchNumber+"|"+alKootId+"|"+claimNumber+"|"+benefitType+"|"+eventRefNo+"|"+qatarId+"|"+paymentRefNumber+"|"+finBatchNo+"|"+financeStatus+"|";	
					}
					else
					{
						var partnerNm=document.forms[1].partnerNm.value;
						parameterValue =  "|"+"CDR"+"|"+sProviderName+"|"+trtmtStartDate+"|"+trtmtEndDate+"|"+clmFromDate+"|"+clmToDate+"|"+patientName+"|"+status+"|"+invoiceNumber+"|"+batchNumber+"|"+alKootId+"|"+claimNumber+"|"+benefitType+"|"+eventRefNo+"|"+qatarId+"|"+paymentRefNumber+"|"+partnerNm+"|"+"PTR"+"|";
					}
					var partmeter = "?mode=doGenerateClmDisActReport&parameter="+parameterValue;
				   	var openPage = "/ClaimsDiscountActivityReport.do"+partmeter;
					var w = screen.availWidth - 10;
					var h = screen.availHeight - 99;
					var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
					window.open(openPage,'',features);   
				}else{
					document.forms[1].clmToDate.value='';
					document.forms[1].clmToDate.focus();	
					return false;
				}	
			}else{
				document.forms[1].tmttoDate.value='';
				document.forms[1].tmttoDate.focus();
				return false;
			}	   			
	}

	