function onTatReport()
{  
	document.forms[1].mode.value="doTatReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
}
function onProductivityReport()
{  
	document.forms[1].mode.value="doProductivityReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
}
function onPreapprovalDetailedReport()
{  
	document.forms[1].mode.value="doPreapprovalDetailedReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
}
function onPreapprovalActivityReport()
{  
	document.forms[1].mode.value="doPreapprovalActivityReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
}

function onClose()
{  
	document.forms[1].mode.value="doCloseCashlessRpt";
	document.forms[1].action="/ReportsAction.do";
	document.forms[1].submit();
}

function onGenerateTATReport()
{  
	var fromDate=document.getElementById("startDate").value;
	var toDate=document.getElementById("endDate").value;
	
	if(fromDate==""){
		alert("Please select From Date");
		document.forms[1].startDate.value="";
		document.forms[1].startDate.focus();
	    return false;
	}
	if(toDate==""){
		alert("Please select To Date");
		document.forms[1].endDate.value="";
		document.forms[1].endDate.focus();
	    return false;
	}
		  if(fromDate!="" && toDate !=""){
				if(compareDates("startDate","From Date","endDate","To Date","greater than")==false)
				{
					document.forms[1].endDate.value="";
				    return false;
				}
			}
		  
		    var date1 = fromDate.split("/");
	 		var altDate1=date1[1]+"/"+date1[0]+"/"+date1[2];
	 		altDate1=new Date(altDate1);
	 		var date2 =toDate.split("/");
	 		var altDate2=date2[1]+"/"+date2[0]+"/"+date2[2];
	 		altDate2=new Date(altDate2);
		  
	 		var timeDiff = Math.abs(altDate2.getTime() - altDate1.getTime());
 			var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
 			diffDays++;
	 		if(date1[1]=="01"||date1[1]=="03"||date1[1]=="05"||date1[1]=="07"||date1[1]=="08"||date1[1]=="10"||date1[1]=="12"){
	 			if(diffDays>31){
	 			alert("You can download the data of only one month");	
	 			return false;
	 			}
	 		}
 			
	 		if(date1[1]=="04"||date1[1]=="06"||date1[1]=="09"||date1[1]=="11"){
	 			if(diffDays>30){
		 			alert("You can download the data of only one month");	
		 			return false;
	 			}	 			
	 		}
	 		if(date1[1]=="02"){
	 			if ( (date1[2] % 4 === 0) && (date1[2] % 100 !== 0) || (date1[2] % 400 === 0) ) {
	 				if(diffDays>29){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 			else {
	 				if(diffDays>28){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 		}
	document.forms[1].mode.value="doGenerateTATReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
	 // }
}

function onGenerateProductivityReport()
{  
	var fromDate=document.getElementById("startDate").value;
	var toDate=document.getElementById("endDate").value;
	
	if(fromDate==""){
		alert("Please select From Date");
		document.forms[1].startDate.value="";
		document.forms[1].startDate.focus();
	    return false;
	}
	if(toDate==""){
		alert("Please select To Date");
		document.forms[1].endDate.value="";
		document.forms[1].endDate.focus();
	    return false;
	}
		  if(fromDate!="" && toDate !=""){
				if(compareDates("startDate","From Date","endDate","To Date","greater than")==false)
				{
					document.forms[1].endDate.value="";
				    return false;
				}
			}
		  
		    var date1 = fromDate.split("/");
	 		var altDate1=date1[1]+"/"+date1[0]+"/"+date1[2];
	 		altDate1=new Date(altDate1);
	 		var date2 =toDate.split("/");
	 		var altDate2=date2[1]+"/"+date2[0]+"/"+date2[2];
	 		altDate2=new Date(altDate2);
		  
	 		var timeDiff = Math.abs(altDate2.getTime() - altDate1.getTime());
 			var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
 			diffDays++;
	 		if(date1[1]=="01"||date1[1]=="03"||date1[1]=="05"||date1[1]=="07"||date1[1]=="08"||date1[1]=="10"||date1[1]=="12"){
	 			if(diffDays>31){
	 			alert("You can download the data of only one month");	
	 			return false;
	 			}
	 		}
 			
	 		if(date1[1]=="04"||date1[1]=="06"||date1[1]=="09"||date1[1]=="11"){
	 			if(diffDays>30){
		 			alert("You can download the data of only one month");	
		 			return false;
	 			}	 			
	 		}
	 		if(date1[1]=="02"){
	 			if ( (date1[2] % 4 === 0) && (date1[2] % 100 !== 0) || (date1[2] % 400 === 0) ) {
	 				if(diffDays>29){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 			else {
	 				if(diffDays>28){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 		}
	
	document.forms[1].mode.value="doGenerateProductivityReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
}

function onGenerateDetailedReport()
{  
	var fromDate=document.getElementById("startDate").value;
	var toDate=document.getElementById("endDate").value;
	
	if(fromDate==""){
		alert("Please select From Date");
		document.forms[1].startDate.value="";
		document.forms[1].startDate.focus();
	    return false;
	}
	if(toDate==""){
		alert("Please select To Date");
		document.forms[1].endDate.value="";
		document.forms[1].endDate.focus();
	    return false;
	}
		  if(fromDate!="" && toDate !=""){
				if(compareDates("startDate","From Date","endDate","To Date","greater than")==false)
				{
					document.forms[1].endDate.value="";
				    return false;
				}
			}
		  
		    var date1 = fromDate.split("/");
	 		var altDate1=date1[1]+"/"+date1[0]+"/"+date1[2];
	 		altDate1=new Date(altDate1);
	 		var date2 =toDate.split("/");
	 		var altDate2=date2[1]+"/"+date2[0]+"/"+date2[2];
	 		altDate2=new Date(altDate2);
		  
	 		var timeDiff = Math.abs(altDate2.getTime() - altDate1.getTime());
 			var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
 			diffDays++;
	 		if(date1[1]=="01"||date1[1]=="03"||date1[1]=="05"||date1[1]=="07"||date1[1]=="08"||date1[1]=="10"||date1[1]=="12"){
	 			if(diffDays>31){
	 			alert("You can download the data of only one month");	
	 			return false;
	 			}
	 		}
 			
	 		if(date1[1]=="04"||date1[1]=="06"||date1[1]=="09"||date1[1]=="11"){
	 			if(diffDays>30){
		 			alert("You can download the data of only one month");	
		 			return false;
	 			}	 			
	 		}
	 		if(date1[1]=="02"){
	 			if ( (date1[2] % 4 === 0) && (date1[2] % 100 !== 0) || (date1[2] % 400 === 0) ) {
	 				if(diffDays>29){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 			else {
	 				if(diffDays>28){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 		}
	document.forms[1].mode.value="doGenerateDetailedReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
}

function onGenerateActivityReport()
{  
	var fromDate=document.getElementById("startDate").value;
	var toDate=document.getElementById("endDate").value;
	
	if(fromDate==""){
		alert("Please select From Date");
		document.forms[1].startDate.value="";
		document.forms[1].startDate.focus();
	    return false;
	}
	if(toDate==""){
		alert("Please select To Date");
		document.forms[1].endDate.value="";
		document.forms[1].endDate.focus();
	    return false;
	}
		  if(fromDate!="" && toDate !=""){
				if(compareDates("startDate","From Date","endDate","To Date","greater than")==false)
				{
					document.forms[1].endDate.value="";
				    return false;
				}
			}
		  
		    var date1 = fromDate.split("/");
	 		var altDate1=date1[1]+"/"+date1[0]+"/"+date1[2];
	 		altDate1=new Date(altDate1);
	 		var date2 =toDate.split("/");
	 		var altDate2=date2[1]+"/"+date2[0]+"/"+date2[2];
	 		altDate2=new Date(altDate2);
		  
	 		var timeDiff = Math.abs(altDate2.getTime() - altDate1.getTime());
 			var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
 			diffDays++;
	 		if(date1[1]=="01"||date1[1]=="03"||date1[1]=="05"||date1[1]=="07"||date1[1]=="08"||date1[1]=="10"||date1[1]=="12"){
	 			if(diffDays>31){
	 			alert("You can download the data of only one month");	
	 			return false;
	 			}
	 		}
 			
	 		if(date1[1]=="04"||date1[1]=="06"||date1[1]=="09"||date1[1]=="11"){
	 			if(diffDays>30){
		 			alert("You can download the data of only one month");	
		 			return false;
	 			}	 			
	 		}
	 		if(date1[1]=="02"){
	 			if ( (date1[2] % 4 === 0) && (date1[2] % 100 !== 0) || (date1[2] % 400 === 0) ) {
	 				if(diffDays>29){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 			else {
	 				if(diffDays>28){
			 			alert("You can download the data of only one month");	
			 			return false;
		 			}	
	 				}
	 		}
	
	document.forms[1].mode.value="doGenerateActivityReport";
	document.forms[1].action="/CashlessReportsAction.do";
	document.forms[1].submit();
}












