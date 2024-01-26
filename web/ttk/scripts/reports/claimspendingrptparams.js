//javascript function for claimspendingrptparams jsp
var sdXhttp;
function onBatchNumber()
{
    document.forms[1].mode.value="doBatchNumber";
	document.forms[1].action.value="/ClaimsPendingReportsAction.do";
	document.forms[1].submit();
}//end of onBatchNumber()
function onClose()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].action="/TPAComReportsAction.do";
	document.forms[1].submit();
}//end of onClose()
function ClearCorporate()
{
	document.forms[1].mode.value="doClearCorporate";
	document.forms[1].action="/ClaimsPendingReportsAction.do";
	document.forms[1].submit();
}//end of ClearCorporate()
function onGenerateReport()
{
	var parameterValue	=	"";
	trimForm(document.forms[1]);
	var numericValue=/^[0-9]*$/;
	/*if(document.forms[1].startDate.value == "")
	{
		alert("Please enter the Start Date");
		document.forms[1].startDate.focus();
		return false;
	}//end of else if(document.forms[1].startDate.value == "" )
	else if(document.forms[1].endDate.value == "")
	{
		alert("Please enter the End Date");
		document.forms[1].endDate.focus();
		return false;
	}//end of else if(document.forms[1].endDate.value == "" )
	else */
	if("FinClmInpRpt"!= document.forms[1].reportID.value)
	{
		if(document.forms[1].floatAccNo!=null){
		if(document.forms[1].floatAccNo.value == "")
		{
			alert("Please enter the Float Account No.");
			document.forms[1].floatAccNo.focus();
			return false;
		}//end of else if(document.forms[1].floatAccNo.value == "" )
	  }
	}
	
	//Added for new Finance report (Routine Report)
	if("RoutineRpt"== document.forms[1].reportID.value)
	{
		if(document.forms[1].floatAccNo.value == "")
		{
			alert("Please select the Float Account No.");
			document.forms[1].floatAccNo.focus();
			return false;
		}//end of else if(document.forms[1].floatAccNo.value == "" )
		
		if(document.forms[1].searchBasedOn.value == "")
		{
			alert("Please select  Search Based on.");
			document.forms[1].searchBasedOn.focus();
			return false;
		}//end of else if(document.forms[1].searchBasedOn.value == "" )
		
		if(document.forms[1].startDate.value == "")
		{
			alert("Please enter Start Date.");
			document.forms[1].startDate.focus();
			return false;
		}//end of else if(document.forms[1].startDate.value == "" )
		
		if(document.forms[1].financeStatus.value == "")
		{
			alert("Please select Finance Status.");
			document.forms[1].financeStatus.focus();
			return false;
		}//end of else if(document.forms[1].financeStatus.value == "" )
		if(document.forms[1].financeStatus.value == "ALL")
		{
			var startDate =document.getElementById("startDate").value;    	
			var endDate=document.getElementById("endDate").value;
			if( document.forms[1]. endDate.value == "")
			{
				alert("Please enter End Date.");
				document.forms[1].endDate.focus();
				return false;
			}	
			else 
			{	
					var sdate = startDate.split("/");
					var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
					altsdate=new Date(altsdate);
					var edate =endDate.split("/");
					var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
					altedate=new Date(altedate);
					var Startdate = new Date(altsdate);
					var EndDate =  new Date(altedate);
					if(EndDate < Startdate)
					 {
						alert("End Date should be greater than or equal to Start Date.");
						document.getElementById("endDate").focus();
						return ;
					 }
					  var miliseconds=EndDate - Startdate;
					  total_seconds = parseInt(Math.floor(miliseconds / 1000));
					  total_minutes = parseInt(Math.floor(total_seconds / 60));
					  total_hours = parseInt(Math.floor(total_minutes / 60));
					  days = parseInt(Math.floor(total_hours / 24));
					if(days>120)
					 {
						alert("Start Date and End Date has a gap of more than 4 months(120 Days).");
						document.getElementById("endDate").focus();
						return ;
					 }
			} // else close.. 
		} // if(document.forms[1].financeStatus.value == "ALL")
	} 
	
		var NumElements=document.forms[1].elements.length;
		for(n=0; n<NumElements;n++)
		{
			if(document.forms[1].elements[n].type=="text")
			{
				 if(document.forms[1].elements[n].className=="textBox textDate")
				 {
				 	if(trim(document.forms[1].elements[n].value).length>0)
					{
						if(isDate(document.forms[1].elements[n],"Date")==false)
						{
							document.forms[1].elements[n].focus();
							return false;
						}//end of if(isDate(document.forms[1].elements[n],"Date")==false)
					}//end of if(trim(document.forms[1].elements[n].value).length>0)
				 }//end of if(document.forms[1].elements[n].className=="textBox textDate")
			}//end of if(document.forms[1].elements[n].type=="text")
		}//end of for(n=0; n<NumElements;n++)
		if(document.forms[1].selectRptType.value == 'CAC')
		{
			if(document.forms[1].batchNo.value == "")
			{
				alert("Please enter the Batch No.");
				document.forms[1].batchNo.focus();
				return false;
			}//end of else if(document.forms[1].batchNo.value == "")
			else if(numericValue.test(document.forms[1].batchNo.value)==false)
			{
				alert("Batch No. should be a numeric value");
				document.forms[1].batchNo.focus();
				return false;
			}//end of else if(numericValue.test(document.forms[1].batchNo.value)==false)
			parameterValue = "?mode=doGenerateClaimsPendingReport&parameterValue="+"|S|"+document.forms[1].floatAccNo.value+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|"+document.forms[1].claimTypeID.value+"|"+document.forms[1].batchNo.value+"|"+"&reportID="+document.forms[1].reportID.value;
		}//end of if(document.forms[1].selectRptType.value == 'CAC')
		else
		{
			if("FinClmSetldRpt"== document.forms[1].reportID.value || "FinClmOutRpt"== document.forms[1].reportID.value){
				parameterValue = "?mode=doGenerateClaimsPendingReport&parameterValue="+"|S|"+document.forms[1].floatAccNo.value+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|F|"+document.forms[1].corporatename.value+"|"+document.forms[1].providername.value+"|"+document.forms[1].apprStartDate.value+"|"+document.forms[1].apprEndDate.value+"|"+"&reportID="+document.forms[1].reportID.value;
			}else if("FinClmInpRpt"== document.forms[1].reportID.value ){
				parameterValue = "?mode=doGenerateClaimsPendingReport&parameterValue="+"|S|"+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|F|"+document.forms[1].corporatename.value+"|"+document.forms[1].providername.value+"|"+"|"+"|"+"&reportID="+document.forms[1].reportID.value;
			}else if("RoutineRpt"== document.forms[1].reportID.value ){
				if(document.forms[1].claimTypeID.value =="CNH"){
					parameterValue = "?mode=doGenerateClaimsPendingReport&parameterValue="+"|S|"+document.forms[1].floatAccNo.value+"|"+document.forms[1].searchBasedOn.value+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|F|"+document.forms[1].corporatename.value+"|"+document.forms[1].providername.value+"|"+document.forms[1].claimTypeID.value+"|"+document.forms[1].financeStatus.value+"|"+document.forms[1].ageBand.value+"|"+document.forms[1].partnerName.value+"|"+document.forms[1].paymentTermAgr.value+"|"+"&reportID="+document.forms[1].reportID.value;
				}
				else{
					parameterValue = "?mode=doGenerateClaimsPendingReport&parameterValue="+"|S|"+document.forms[1].floatAccNo.value+"|"+document.forms[1].searchBasedOn.value+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|F|"+document.forms[1].corporatename.value+"|"+document.forms[1].providername.value+"|"+document.forms[1].claimTypeID.value+"|"+document.forms[1].financeStatus.value+"|"+document.forms[1].ageBand.value+"||"+document.forms[1].paymentTermAgr.value+"|"+"&reportID="+document.forms[1].reportID.value;
					}
			}else{
				parameterValue = "?mode=doGenerateClaimsPendingReport&parameterValue="+"|S|"+document.forms[1].floatAccNo.value+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|"+document.forms[1].claimTypeID.value+"|"+document.forms[1].financeStatus.value+"|F|"+"|"+"|"+"|"+"|"+"&reportID="+document.forms[1].reportID.value;
			}
		}//end of else
		var openPage = "/ClaimsReportsAction.do"+parameterValue+"&selectRptType="+document.forms[1].selectRptType.value+"&reportType="+document.forms[1].reportType.value;
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 99;
		var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open(openPage,'',features);
}//end of onGenerateReport()
function onReportType()
{
	document.forms[1].mode.value="doReportType";
	document.forms[1].action="/ClaimsPendingReportsAction.do";
	document.forms[1].submit();	
}//end of onReportType()

function onChangeClaimType()
{
	document.forms[1].mode.value="doChangeClmType";
	document.forms[1].action="/ReportsAction.do";
	document.forms[1].submit();
}

function onGenerateFinPreAuthReport(){
	var parameterValue	=	"";
	trimForm(document.forms[1]);
	
	if("FinPreAuthRpt"== document.forms[1].reportID.value)
	{
	
		
		if(document.forms[1].startDate.value == "")
		{
			alert("Please enter Start Date.");
			document.forms[1].startDate.focus();
			return false;
		}//end of else if(document.forms[1].startDate.value == "" )
		
		if(document.forms[1].preauthSearchBased.value == "")
		{
			alert("Please Select Search Based On.");
			document.forms[1].preauthSearchBased.focus();
			return false;
		}
		if(document.forms[1].preauthStatus.value == "" || document.forms[1].preauthStatus.value == "INP" || document.forms[1].preauthStatus.value == "REQ" || document.forms[1].preauthStatus.value == "APR" || document.forms[1].preauthStatus.value == "REJ" || document.forms[1].preauthStatus.value == "PCN")
		{
			var startDate =document.getElementById("startDate").value;    	
			var endDate=document.getElementById("endDate").value;
			if( document.forms[1]. endDate.value == "")
			{
				alert("Please enter End Date.");
				document.forms[1].endDate.focus();
				return false;
			}	
			else 
			{	
					var sdate = startDate.split("/");
					var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
					altsdate=new Date(altsdate);
					var edate =endDate.split("/");
					var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
					altedate=new Date(altedate);
					var Startdate = new Date(altsdate);
					var EndDate =  new Date(altedate);
					if(EndDate < Startdate)
					 {
						alert("End Date should be greater than or equal to Start Date.");
						document.getElementById("endDate").focus();
						return ;
					 }
					  var miliseconds=EndDate - Startdate;
					  total_seconds = parseInt(Math.floor(miliseconds / 1000));
					  total_minutes = parseInt(Math.floor(total_seconds / 60));
					  total_hours = parseInt(Math.floor(total_minutes / 60));
					  days = parseInt(Math.floor(total_hours / 24));
					if(document.forms[1].preauthStatus.value == ""){
					if(days>120)
					 {
						alert("Start Date and End Date has a gap of more than 4 months(120 Days).");
						document.getElementById("endDate").focus();
						return ;
					 }
					 }else{
					 if(days>365)
					 {
						alert("Start Date and End Date has a gap of more than 1 Year(365 Days).");
						document.getElementById("endDate").focus();
						return ;
					 }
					 }
			} // else close.. 
		} // if(document.forms[1].financeStatus.value == "ALL")
	} 
	parameterValue = "?mode=doGenerateFinancePreauthReport";
	var openPage = "/ClaimsReportsAction.do"+parameterValue+"&reportType="+document.forms[1].reportType.value+"&reportID="+document.forms[1].reportID.value+"&startDate="+document.forms[1].startDate.value+"&endDate="+document.forms[1].endDate.value+"&corporatename="+document.forms[1].corporatename.value+"&providername="+document.forms[1].providername.value+"&preauthStatus="+document.forms[1].preauthStatus.value+"&preauthSearchBased="+document.forms[1].preauthSearchBased.value;
	var w = screen.availWidth - 10;
		var h = screen.availHeight - 99;
		var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open(openPage,'',features);
}

function getStartDateEndDate(){  
	var underWritingYear = document.getElementById("underWritingYearid").value;
    var corporatename = document.getElementById("corporatenameid").value;	
 var path="/asynchronAction.do?mode=getStartDateEndDate&underWritingYear="+underWritingYear+"&corporatename="+corporatename;
 var varSearchData=getSearchData(path);
 	if(varSearchData=="NoValue"){
 		document.getElementById("startDate").value="";
 		document.getElementById("startDateAutoid").value="";
 		document.getElementById("endDate").value="";
 		document.getElementById("endDateAutoid").value="";
 	}else{
 	var dateArray =	varSearchData.split("|");
 	    document.getElementById("startDate").value=dateArray[0];
 	    document.getElementById("startDateAutoid").value=dateArray[0];
		document.getElementById("endDate").value=dateArray[1];
		document.getElementById("endDateAutoid").value=dateArray[1];
 	}
  }

function getSearchData(path){
	  if(sdXhttp==null){
	  if (window.XMLHttpRequest) {
		    sdXhttp = new XMLHttpRequest();
		    } else {
		    // code for IE6, IE5
		    sdXhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	  }
	     
	  sdXhttp.open("POST", path, false);
	  sdXhttp.send();
	  var varSearchData=sdXhttp.responseText;
	  return varSearchData;
	  }

	function onGenerateFinUnderWritingReport(){
		var parameterValue	=	"";
		trimForm(document.forms[1]);
		
		if("UnderwritingRpt"== document.forms[1].reportID.value)
		{
		
			if(document.forms[1].floatAccNo.value == "")
			{
				alert("Please select the Float Account No.");
				document.forms[1].floatAccNo.focus();
				return false;
			}	
			
			if(document.forms[1].startDate.value == "")
			{
				alert("Please enter Start Date.");
				document.forms[1].startDate.focus();
				return false;
			}//end of else if(document.forms[1].startDate.value == "" )
			
			if(document.forms[1].searchBasedOn.value == "")
			{
				alert("Please Select Search Based On.");
				document.forms[1].searchBasedOn.focus();
				return false;
			}
			
			if(document.forms[1].financeStatus.value == "")
			{
				alert("Please select Finance Status.");
				document.forms[1].financeStatus.focus();
				return false;
			}
			
			if(document.forms[1].corporatename.value == "")
			{
				alert("Please select Corporate Name.");
				document.forms[1].corporatename.focus();
				return false;
			}
			
			if(document.forms[1].financeStatus.value == "ALL" || document.forms[1].financeStatus.value == "PENDING" || document.forms[1].financeStatus.value == "READY_TO_BANK" || document.forms[1].financeStatus.value == "SENT_TO_BANK" || document.forms[1].financeStatus.value == "PAID")
			{
				var startDate =document.getElementById("startDate").value;    	
				var endDate=document.getElementById("endDate").value;
				if( document.forms[1]. endDate.value == "")
				{
					alert("Please enter End Date.");
					document.forms[1].endDate.focus();
					return false;
				}	
				else 
				{	
						var sdate = startDate.split("/");
						var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
						altsdate=new Date(altsdate);
						var edate =endDate.split("/");
						var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
						altedate=new Date(altedate);
						var Startdate = new Date(altsdate);
						var EndDate =  new Date(altedate);
						var date2 = document.getElementById("startDateAutoid").value;
						var date2Arr = date2.split("/");
						date2 = date2Arr[1]+"/"+date2Arr[0]+"/"+date2Arr[2];
						var startDateAuto = new Date(date2);
						var endDateAutoid = document.getElementById("endDateAutoid").value;
						var endDateAutoArr = endDateAutoid.split("/");
						endDateAutoid = endDateAutoArr[1]+"/"+endDateAutoArr[0]+"/"+endDateAutoArr[2];
						var endDateAuto = new Date(endDateAutoid);
						if(EndDate < Startdate)
						 {
							alert("End Date should be greater than or equal to Start Date.");
							document.getElementById("endDate").focus();
							return ;
						 }
						  var miliseconds=EndDate - Startdate;
						  total_seconds = parseInt(Math.floor(miliseconds / 1000));
						  total_minutes = parseInt(Math.floor(total_seconds / 60));
						  total_hours = parseInt(Math.floor(total_minutes / 60));
						  days = parseInt(Math.floor(total_hours / 24));
						if(document.forms[1].financeStatus.value == "ALL"){
						if(days>120)
						 {
							alert("Start Date and End Date has a gap of more than 4 months(120 Days).");
							document.getElementById("endDate").focus();
							return ;
						 }
					}else{
						
						if(startDateAuto!=""){
						if(Startdate<startDateAuto){
							alert("Start Date and End Date Should be Within Auto-filled Date Range");
							document.getElementById("startDate").value=document.getElementById("startDateAutoid").value;
							return ;
						}
						
						if(EndDate>endDateAuto){
							alert("Start Date and End Date Should be Within Auto-filled Date Range");
							document.getElementById("endDate").value=document.getElementById("endDateAutoid").value;
							return ;
						}
					  }
					}
				} // else close.. 
			} // if(document.forms[1].financeStatus.value == "ALL")
			
			if(document.forms[1].claimTypeID.value=="CNH"){
				parameterValue = "?mode=doGenerateUnderWritingReport&parameterValue="+"|"+document.forms[1].floatAccNo.value+"|"+document.forms[1].corporatename.value+"|"+document.forms[1].underWritingYear.value+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|"+document.forms[1].searchBasedOn.value+"|"+document.forms[1].financeStatus.value+"|"+document.forms[1].claimTypeID.value+"|"+document.forms[1].providername.value+"|"+document.forms[1].partnerName.value+"|"+"";
				}else{
				parameterValue = "?mode=doGenerateUnderWritingReport&parameterValue="+"|"+document.forms[1].floatAccNo.value+"|"+document.forms[1].corporatename.value+"|"+document.forms[1].underWritingYear.value+"|"+document.forms[1].startDate.value+"|"+document.forms[1].endDate.value+"|"+document.forms[1].searchBasedOn.value+"|"+document.forms[1].financeStatus.value+"|"+document.forms[1].claimTypeID.value+"|"+document.forms[1].providername.value+"|"+"||"+"";
				}
		} 
		var openPage = "/ClaimsReportsAction.do"+parameterValue+"&reportType="+document.forms[1].reportType.value+"&reportID="+document.forms[1].reportID.value+"&selectRptType="+document.forms[1].selectRptType.value;
		var w = screen.availWidth - 10;
			var h = screen.availHeight - 99;
			var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
			window.open(openPage,'',features);
	}
	function onChangeClaimTypeForUnderWriting()
	{
		document.forms[1].mode.value="doUnderwritingRpt";
		document.forms[1].action="/ReportsAction.do";
		document.forms[1].submit();
	}
	
	function onChangeProvider()
	{
		document.forms[1].mode.value="doChangeProvider";
		document.forms[1].action="/ReportsAction.do";
		document.forms[1].submit();
	}	
	
	function onActivityLog()
	{	
		var providername = document.forms[1].providername.value;
		if(providername == '')
		{
			alert("Please select Provider Name.");
			return;
		}
		
		document.forms[1].mode.value="doDisplayActivityLog";
		document.forms[1].action="/ReportsAction.do";
	   	document.forms[1].submit();
	}
