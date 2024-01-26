//java script for the policies list screen in the online forms module of enrollment flow.

//function to call edit screen

//function to display the selected page



//kocb
function onSelectLink(reportId)
{
		document.forms[1].reportId.value=reportId;
		document.forms[1].mode.value = "doSelectLink";
		document.forms[1].action = "/ProviderReportsAction.do";
	    document.forms[1].submit();	
}


function onBack()
{
		document.forms[1].mode.value = "doBack";
		document.forms[1].action = "/ProviderReportsAction.do";
		
	    document.forms[1].submit();
	
}


function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/ProviderReportsAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ProviderReportsAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ProviderReportsAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ProviderReportsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()

//

function validateDates(reportId)
{      if("clmSubmissionId"==reportId||"drugSubmissionId"==reportId||"clmPaymentId"==reportId||"drugPaymentId"==reportId)
	{	//checks if from Date is entered
	if(document.forms[1].clmFromDate.value.length!=0)
	{
		if(!isDate(document.forms[1].clmFromDate,"from Date"))
			return true;
		
		if(isFutureDate(document.forms[1].clmFromDate.value)){
			alert("From date should not be future date ");
			return true;
		}
		//document.forms[1].fromDate.focus();
	}// end of if(document.forms[1].sRecievedDate.value.length!=0)

//checks if to Date is entered
	if(document.forms[1].clmToDate.value.length!=0)
	{
		if(isDate(document.forms[1].clmToDate,"to Date")==false)
			return true;
	}// end of if(document.forms[1].sRecievedDate.value.length!=0)
	if(document.forms[1].clmFromDate.value.length!=0 && document.forms[1].clmToDate.value.length!=0)
	{
		if(compareDates("clmFromDate","from Date","clmToDate","to Date","greater than")==false)
			return true;
	}// end of if(document.forms[1].startDate.value.length!=0 && document.forms[1].endDate.value.length!=0)
	
	}else{
		//checks if from Date is entered
		if(document.forms[1].trtmtFromDate.value.length!=0)
		{
			if(!isDate(document.forms[1].trtmtFromDate,"from Date"))
				return true;
			
			if(isFutureDate(document.forms[1].trtmtFromDate.value)){
				alert("From date should not be future date ");
				return true;
			}
			//document.forms[1].fromDate.focus();
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)
	
    //checks if to Date is entered
		if(document.forms[1].trtmtToDate.value.length!=0)
		{
			if(isDate(document.forms[1].trtmtToDate,"to Date")==false)
				return true;
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)
		if(document.forms[1].trtmtFromDate.value.length!=0 && document.forms[1].trtmtToDate.value.length!=0)
		{
			if(compareDates("trtmtFromDate","from Date","trtmtToDate","to Date","greater than")==false)
				return true;
		}// end of if(document.forms[1].startDate.value.length!=0 && document.forms[1].endDate.value.length!=0)
		//checks if from Date is entered
		if(document.forms[1].clmFromDate.value.length!=0)
		{
			if(!isDate(document.forms[1].clmFromDate,"from Date"))
				return true;
			
			if(isFutureDate(document.forms[1].clmFromDate.value)){
				alert("From date should not be future date ");
				return true;
			}
			//document.forms[1].fromDate.focus();
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)

	//checks if to Date is entered
		if(document.forms[1].clmToDate.value.length!=0)
		{
			if(isDate(document.forms[1].clmToDate,"to Date")==false)
				return true;
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)
		if(document.forms[1].clmFromDate.value.length!=0 && document.forms[1].clmToDate.value.length!=0)
		{
			if(compareDates("clmFromDate","from Date","clmToDate","to Date","greater than")==false)
				return true;
		}// end of if(document.forms[1].startDate.value.length!=0 && document.forms[1].endDate.value.length!=0)
		
		
	}
		
		return false;
}//end of validateDates()


function onSearch()
{   
	if(!JS_SecondSubmit)
	 {  
		var reportId=document.forms[1].reportId.value;
		if("preAuthSummary"==reportId||"preAuthDetailed"==reportId){
			if((document.forms[1].preApprovalNo.value!=""  || document.forms[1].patientName.value!="" ||
					document.forms[1].authNo.value!="" || document.forms[1].doctorName.value!="" || 
					document.forms[1].alKootId.value!="" || document.forms[1].eventRefNo.value!="" || 
					document.forms[1].qatarId.value!="" || document.forms[1].RefNo.value!=""))
				{
						trimForm(document.forms[1]);
						document.forms[1].mode.value = "doSearch";
						document.forms[1].action = "/ProviderReportsAction.do";
						JS_SecondSubmit=true;
						document.forms[1].submit();	 
				}else{
						if(document.forms[1].fromDate.value==""){
							alert("Please Provide From Date !!!");
							document.forms[1].fromDate.value='';
							document.forms[1].fromDate.focus();
							return false;
						}else if(document.forms[1].toDate.value==""){
							alert("Please Provide To Date !!!");
							document.forms[1].toDate.value='';
							document.forms[1].toDate.focus();
							return false;
						}
						if(document.forms[1].fromDate.value.length!=0)
						{
							if(!isDate(document.forms[1].fromDate,"from Date"))
								return false;
				
							if(isFutureDate(document.forms[1].fromDate.value)){
								alert("From date should not be future date ");
								document.forms[1].fromDate.value='';
								document.forms[1].fromDate.focus();
								return false;
							}//end of if(isFutureDate(document.forms[1].fromDate.value)){
						}// end of if(document.forms[1].fromDate.value.length!=0)
				}// end of else
		}else{
				if((document.forms[1].patientName.value!=""  || document.forms[1].invoiceNo.value!="" ||
						document.forms[1].batchNo.value!="" || document.forms[1].alKootId.value!="" || 
						document.forms[1].claimNo.value!="" || document.forms[1].eventRefNo.value!="" || 
						document.forms[1].qatarId.value!="" || document.forms[1].payRefNo.value!=""))
					{
						trimForm(document.forms[1]);
						document.forms[1].mode.value = "doSearch";
						document.forms[1].action = "/ProviderReportsAction.do";
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
							if(!isDate(document.forms[1].clmFromDate,"from Date"))
								return false;
				
							if(isFutureDate(document.forms[1].clmFromDate.value)){
								alert("From date should not be future date ");
								document.forms[1].clmFromDate.value='';
								document.forms[1].clmFromDate.focus();
								return false;
							}// end of 	if(isFutureDate(document.forms[1].clmFromDate.value))
						}// end of if(document.forms[1].fromDate.value.length!=0)	
						if(document.forms[1].tmtfromDate.value.length!=0)
						{
							if(!isDate(document.forms[1].tmtfromDate,"from Date"))
								return false;
					
							if(isFutureDate(document.forms[1].tmtfromDate.value)){
								alert("From date should not be future date ");
								document.forms[1].tmtfromDate.value='';
								document.forms[1].tmtfromDate.focus();
								return false;
							}// end of if(isFutureDate(document.forms[1].tmtfromDate.value))
						}// end of if(document.forms[1].fromDate.value.length!=0)	 
					}// end of else
		}// end of else
		// For 90 Days Validation
		if("preAuthSummary"==reportId||"preAuthDetailed"==reportId){
					var stParts =document.forms[1].fromDate.value.split('/');
					var endParts=document.forms[1].toDate.value.split('/');
					var validateFlag=dateValidation(stParts,endParts);
					if(validateFlag==true){
					  	trimForm(document.forms[1]);
				    	document.forms[1].mode.value = "doSearch";
				    	document.forms[1].action = "/ProviderReportsAction.do";
						JS_SecondSubmit=true;
				    	document.forms[1].submit();	   
					}else{
						document.forms[1].toDate.value='';
						document.forms[1].toDate.focus();
						return false;
					}		
		}else{//Claim Report Validation
				var stParts =document.forms[1].tmtfromDate.value.split('/');
				var endParts=document.forms[1].tmttoDate.value.split('/');
				var validateFlag=dateValidation(stParts,endParts);
					if(validateFlag==true){
						var stParts =document.forms[1].clmFromDate.value.split('/');
						var endParts=document.forms[1].clmToDate.value.split('/');
						var validateFlag=dateValidation(stParts,endParts);
						if(validateFlag==true){
							trimForm(document.forms[1]);
							document.forms[1].mode.value = "doSearch";
							document.forms[1].action = "/ProviderReportsAction.do";
							JS_SecondSubmit=true;
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
			}//End of else{//Claim Report Validation
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function dateValidation(stParts,endParts){
	var startDate = new Date(stParts[2], stParts[1] - 1, stParts[0]);
	var endDate = new Date(endParts[2], endParts[1] - 1, endParts[0]);
		if(startDate>endDate){
			alert("Please Provide To Date Greater Than From Date!!!");
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
						alert("Allowed Duration For From Date And To Date Upto 90 Day's Only!!! ");		
					}
				}else{
					if(diffDays > 89){
						validateFlag=true;
						alert("Allowed Duration For From Date And To Date Upto 90 Day's Only!!! ");	
					}
				}				
			if(validateFlag==false){
					return true;
				}else{
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

function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/ProviderReportsAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()


//doGenerateReport
function onExportToExcel()
{ 

var reportId=document.forms[1].reportId.value;
var patientName = null;
var status = null;
var	alKootId=null;
var benefitType = null;
var qatarId = null;
var eventRefNum = null;

//for preAuth reports.
var preAuthNumber = null;
var	fromDate=null;
var	toDate=null;
var authNum = null;
var doctorName = null;
var refId = null;

//for claim reports.
var trtmtStartDate = null;
var trtmtEndDate = null;
var clmFromDate = null;
var clmToDate = null;
var batchNumber = null;
var invoiceNumber = null;
var claimNumber = null;
var paymentRefNumber = null;


	//data
	if("preAuthSummary"==reportId||"preAuthDetailed"==reportId){
		preAuthNumber=document.forms[1].preApprovalNo.value;
		fromDate=document.forms[1].fromDate.value;
		toDate=document.forms[1].toDate.value;
		patientName=document.forms[1].patientName.value;
		authNum=document.forms[1].authNo.value;
		doctorName=document.forms[1].doctorName.value;
		alKootId=document.forms[1].alKootId.value;
		benefitType=document.forms[1].benefitType.value;
		status=document.forms[1].claimStatus.value;
		eventRefNum=document.forms[1].eventRefNo.value;
		qatarId=document.forms[1].qatarId.value;
		refId=document.forms[1].RefNo.value;
	
		parameterValue = "|"+preAuthNumber+"|"+fromDate+"|"+toDate+"|"+patientName+"|"+authNum+"|"+doctorName+"|"+alKootId+"|"+benefitType+"|"+status+"|"+eventRefNum+"|"+qatarId+"|"+refId+"|"; 
	}else{
		trtmtStartDate=document.forms[1].tmtfromDate.value;
		trtmtEndDate=document.forms[1].tmttoDate.value;
		clmFromDate=document.forms[1].clmFromDate.value;
		clmToDate=document.forms[1].clmToDate.value;
		patientName=document.forms[1].patientName.value;
		status=document.forms[1].claimStatus.value;
		invoiceNumber=document.forms[1].invoiceNo.value;
		batchNumber=document.forms[1].batchNo.value;
		alKootId=document.forms[1].alKootId.value;
		claimNumber=document.forms[1].claimNo.value;
		benefitType=document.forms[1].benefitType.value;
		eventRefNo=document.forms[1].eventRefNo.value;
		qatarId=document.forms[1].qatarId.value;
		paymentRefNumber=document.forms[1].payRefNo.value;
		
		parameterValue =  "|"+trtmtStartDate+"|"+trtmtEndDate+"|"+clmFromDate+"|"+clmToDate+"|"+patientName+"|"+status+"|"+invoiceNumber+"|"+batchNumber+"|"+alKootId+"|"+claimNumber+"|"+benefitType+"|"+eventRefNo+"|"+qatarId+"|"+paymentRefNumber+"|";	
	  
	}
	
	
	
	
	//end data
	
	 var fileName;
	 
	 if("preAuthSummary"==reportId)
	   {
	      fileName = "onlinereports/provider/preAuthSummaryReport.jrxml";
	      
        }
	 else if("preAuthDetailed"==reportId)
	   {
	     fileName = "onlinereports/provider/preAuthDetailedReport.jrxml";
	   }
	 else if("claimSummary"==reportId)
	   {
	    fileName = "onlinereports/provider/claimSummaryReport.jrxml";
	   }

     else if("claimDetailed"==reportId)
	   {
	    fileName = "onlinereports/provider/ProviderclaimDetailedReport.jrxml";
	   }
	   
	   var partmeter = "?mode=doGenerateReport&parameter="+parameterValue+"&fileName="+fileName+"&reportType=EXCEL"+"&reportId="+reportId;
  
	   var openPage = "/ProviderReportsAction.do"+partmeter;
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 99;
		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open(openPage,'',features);		   
				
}

