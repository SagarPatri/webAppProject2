//java script for the policies list screen in the online forms module of enrollment flow.

//function to call edit screen

//function to display the selected page



//kocb
function onSelectLink(reportId)
{
	
	
		document.forms[1].reportId.value=reportId;
		
		
		
		document.forms[1].mode.value = "doSelectLink";
		document.forms[1].action = "/PbmReportsAction.do";
		
	    document.forms[1].submit();
	
}


function onBack()
{
		document.forms[1].mode.value = "doBack";
		document.forms[1].action = "/PbmReportsAction.do";
		
	    document.forms[1].submit();
	
}


function edit(rownum){
	document.forms[1].mode.value = "doViewPreauth";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value ="PBM";
    document.forms[1].action = "/PbmReportsAction.do";
    document.forms[1].submit();
}//end of edit(rownum)


function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/PbmReportsAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/PbmReportsAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/PbmReportsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/PbmReportsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()

//

function validateDates(reportId)
{      if("clmSubmissionId"==reportId||"drugSubmissionId"==reportId||"clmPaymentId"==reportId||"drugPaymentId"==reportId)
	{	//checks if from Date is entered
	if(document.forms[1].clmFromDate.value.length!=0 && document.forms[1].clmToDate.value.length!=0)
	{
		if(!isDate(document.forms[1].clmFromDate,"from Date"))
			return true;
		
		if(isFutureDate(document.forms[1].clmFromDate.value)){
			alert("From date should not be future date ");
			return true;
		}
		//document.forms[1].fromDate.focus();
	}// end of if(document.forms[1].sRecievedDate.value.length!=0)
	 else if (document.forms[1].invoiceNumber.value ==""  && document.forms[1].alKootId.value =="" &&
		   document.forms[1].claimNumber.value ==""  && document.forms[1].patientName.value =="" &&
		   document.forms[1].eventRefNo.value ==""  && document.forms[1].batchNo.value =="" ){
				alert("Please Provide Claim Submission From Date And To Date Range!!!");
				//document.forms[1].clmFromDate.value='';
				document.forms[1].clmFromDate.focus();
				return true;
	}
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


/*function onSearch()
{      var reportId=document.forms[1].reportId.value;

     if(validateDates(reportId)){
	
	    return false;
     }
	
               
	if(!JS_SecondSubmit)
	 {  
		var stParts =document.forms[1].clmFromDate.value.split('/');
		var endParts=document.forms[1].clmToDate.value.split('/');
		var validateFlag=dateValidation(stParts,endParts);
		if(validateFlag==true){
		   trimForm(document.forms[1]);
	    	document.forms[1].mode.value = "doSearch";
	    	document.forms[1].action = "/PbmReportsAction.do";
			JS_SecondSubmit=true;
	    	document.forms[1].submit();
		}else{
			document.forms[1].clmToDate.value='';
			document.forms[1].clmToDate.focus();
			return false;
		}	
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()
*/

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
   		document.forms[1].action = "/PbmReportsAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()


//doGenerateReport
function onExportToExcel()
{ 
	
	var reportId=document.forms[1].reportId.value;
	
	
	var	invoiceNumber=null;
	var	alKootId=null;
	var	claimNumber=null;
	var	status=null;
	var	clmFromDate=null;
	var	clmToDate=null;
	var	patientName=null;
	var	eventRefNo=null;
	
//	var	trtmtFromDate=null;
//	var	trtmtToDate=null;
	var	authNo=null;
	var	preApprovalNo=null;
	var	clmDispStatus=null;
	var parameterValue=null;
	
	var batchNo = "";
	var paymentRefNo = "";
	var clmPayStatus = "";
	invoiceNumber="";
	var trtmtStartDate = null;
	var trtmtEndDate = null;
	var invoiceNumber = null;
	var batchNumber = null;
	var benefitType = null;
	var qatarId = null;
	var paymentRefNumber = null;
	//data
	if("clmSubmissionId"==reportId||"drugSubmissionId"==reportId||"clmPaymentId"==reportId||"drugPaymentId"==reportId){
		invoiceNumber=document.forms[1].invoiceNumber.value;
	    alKootId=document.forms[1].alKootId.value;
		claimNumber=document.forms[1].claimNumber.value;
	    status=document.forms[1].claimStatus.value;
		clmFromDate=document.forms[1].clmFromDate.value;
		clmToDate=document.forms[1].clmToDate.value;
		patientName=document.forms[1].patientName.value;
		eventRefNo=document.forms[1].eventRefNo.value;
		batchNo = document.forms[1].batchNo.value;
		if(document.getElementById("ClaimpaymentRefNoId") !=null ){
			paymentRefNo = document.getElementById("ClaimpaymentRefNoId").value ;
		}else if(document.getElementById("DrugpaymentRefNoId") !=null){
			paymentRefNo = document.getElementById("DrugpaymentRefNoId").value;
		}
		
		if(document.forms[1].clmPayStatus != null){
		clmPayStatus = document.forms[1].clmPayStatus.value;
		 }
		parameterValue = "|"+invoiceNumber+"|"+alKootId+"|"+claimNumber+"|"+status+"|"+clmFromDate+"|"+clmToDate+"|"+patientName+"|"+eventRefNo+"|"+"|"+"|"+"|"+"|"+"|"+batchNo+"|"+paymentRefNo+"|"+clmPayStatus+"|"; 
	}else{
		
		
		/*trtmtFromDate=document.forms[1].trtmtFromDate.value;
		trtmtToDate=document.forms[1].trtmtToDate.value;
		clmFromDate=document.forms[1].clmFromDate.value;
		clmToDate=document.forms[1].clmToDate.value;
		patientName=document.forms[1].patientName.value;
		alKootId=document.forms[1].alKootId.value;
		authNo=document.forms[1].authNo.value;
		preApprovalNo=document.forms[1].preApprovalNo.value;
		eventRefNo=document.forms[1].eventRefNo.value;
		claimNumber=document.forms[1].claimNumber.value;
		status=document.forms[1].claimStatus.value;
		clmDispStatus=document.forms[1].clmDispStatus.value;*/
		
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
		
		
		// parameterValue =  "|"+trtmtFromDate+"|"+trtmtToDate+"|"+clmFromDate+"|"+clmToDate+"|"+patientName+"|"+authNo+"|"+preApprovalNo+"|"+alKootId+"|"+eventRefNo+"|"+claimNumber+"|"+status+"|"+clmPayStatus+"|"; 
	//	parameterValue =  "|"+invoiceNumber+"|"+alKootId+"|"+claimNumber+"|"+status+"|"+clmFromDate+"|"+clmToDate+"|"+patientName+"|"+eventRefNo+"|"+trtmtFromDate+"|"+trtmtToDate+"|"+authNo+"|"+preApprovalNo+"|"+clmDispStatus+"|";	
		parameterValue =  "|"+trtmtStartDate+"|"+trtmtEndDate+"|"+clmFromDate+"|"+clmToDate+"|"+patientName+"|"+status+"|"+invoiceNumber+"|"+batchNumber+"|"+alKootId+"|"+claimNumber+"|"+benefitType+"|"+eventRefNo+"|"+qatarId+"|"+paymentRefNumber+"|";
	}
	
	
	
	
	//end data
	
	 var fileName;
	 
	 if("clmSubmissionId"==reportId)
	   {
	      fileName = "onlinereports/pbm/claimSubmissionReport.jrxml";
	      
        }
	 else if("drugSubmissionId"==reportId)
	   {
	     fileName = "onlinereports/pbm/drugSubmissionReport.jrxml";
	   }
	 else if("clmPaymentId"==reportId)
	   {
	    fileName = "onlinereports/pbm/claimPaymentReport.jrxml";
	   }

     else if("drugPaymentId"==reportId)
	   {
	    fileName = "onlinereports/pbm/drugPaymentReport.jrxml";
	   }

     else if("clmDispenseId"==reportId)
	   {
	    fileName = "onlinereports/pbm/claimDispenseReport.jrxml";
	   }
     else if("claimSummary"==reportId)
	   {
	    fileName = "onlinereports/pbm/claimSummaryReport.jrxml";
	   }

     else if("claimDetailed"==reportId)
	   {
	    fileName = "onlinereports/pbm/PBMclaimDetailedReport.jrxml";
	   }
	   else 
	   {
	    fileName = "onlinereports/pbm/drugDispenseReport.jrxml";
	   }

	  
	   
	   var partmeter = "?mode=doGenerateReport&parameter="+parameterValue+"&fileName="+fileName+"&reportType=EXCEL"+"&reportId="+reportId;
  
	   var openPage = "/PbmReportsAction.do"+partmeter;
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 99;
		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open(openPage,'',features);		   
				
				 
				 
			
		
		
		
}
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

function onSearch(){
	if(!JS_SecondSubmit)
	 {
		var reportId=document.forms[1].reportId.value;
		//Claim Report Validation
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
					document.forms[1].action = "/PbmReportsAction.do";
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
	
		
	 }
}