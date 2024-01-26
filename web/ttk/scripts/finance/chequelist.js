//java script for the cheques list screen in the finance module of cheque information flow.

//function to call edit screen
function edit(rownum)
{
    document.forms[1].mode.value="doViewCheque";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value="General";
    document.forms[1].action = "/ChequeSearchAction.do";
    document.forms[1].submit();
}//end of edit(rownum)

//function to sort based on the link selected
function toggle(sortid)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/ChequeSearchAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ChequeSearchAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages

//function to display next set of pages
function PressForward()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ChequeSearchAction.do";
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ChequeSearchAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

function isValidated()
{
		/*if(trim(document.forms[1].sChequeNumber.value).length>0)
		{
			regexp1=/^\d*$/;
			if(regexp1.test(trim(document.forms[1].sChequeNumber.value))==false)
			{
				alert("Cheque No. should be a numeric value");
				document.forms[1].sChequeNumber.focus();
				document.forms[1].sChequeNumber.select();
				return false;
			}//end of if(regexp1.test(trim(document.forms[1].sChequeNumber.value))==false)
		}//end of if(trim(document.forms[1].sChequeNumber.value).length>0)
		*/
		if(trim(document.forms[1].sBatchNumber.value).length>0)
		{
			regexp1=/^\d*$/;
			if(regexp1.test(trim(document.forms[1].sBatchNumber.value))==false)
			{
				alert("Batch No. should be a numeric value");
				document.forms[1].sBatchNumber.focus();
				document.forms[1].sBatchNumber.select();
				return false;
			}//end of if(regexp1.test(trim(document.forms[1].sBatchNumber.value))==false)
		}//end of if(trim(document.forms[1].sBatchNumber.value).length>0)
		//checks if start date is entered
		if(document.forms[1].sStartDate.value.length!=0)
		{
			if(isDate(document.forms[1].sStartDate,"Start Date")==false)
				return false;
				document.forms[1].sStartDate.focus();
		}// end of if(document.forms[1].sStartDate.value.length!=0)
		//checks if end Date is entered
		if(document.forms[1].sEndDate.value.length!=0)
		{
			if(isDate(document.forms[1].sEndDate,"End Date")==false)
				return false;
				document.forms[1].sEndDate.focus();
		}// end of if(document.forms[1].sEndDate.value.length!=0)
		//checks if both dates entered
		if(document.forms[1].sStartDate.value.length!=0 && document.forms[1].sEndDate.value.length!=0)
		{
			if(compareDates("sStartDate","Start Date","sEndDate","End Date","greater than")==false)
				return false;
		}// end of if(document.forms[1].sStartDate.value.length!=0 && document.forms[1].sEndDate.value.length!=0)
		return true;
}//end of isValidated()

function onSearch(element)
{
   if(!JS_SecondSubmit)
 {
    trimForm(document.forms[1]);
    if(isValidated())
	{
    	if((document.forms[1].sChequeNumber.value!=""  || document.forms[1].sClaimSettleNumber.value!="" ||
				document.forms[1].sEnrollmentId.value!="" || document.forms[1].sBatchNumber.value!="" || 
					document.forms[1].sQatarId.value!="" || (document.forms[1].sStartDate.value!=""
						&& document.forms[1].sEndDate.value!="")))
			{
    			var stParts =document.forms[1].sStartDate.value.split('/');
    			var endParts=document.forms[1].sEndDate.value.split('/');
    			var validateFlag=dateValidation(stParts,endParts);
    			if(validateFlag==true){
    				document.forms[1].mode.value = "doSearch";
    				document.forms[1].action = "/ChequeSearchAction.do";
    				JS_SecondSubmit=true;
    				element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
    				document.forms[1].submit();
    			}else{
    				alert("Please Select  Date Range!!!");
    				document.forms[1].sStartDate.value='';
    				document.forms[1].sStartDate.focus();
    				return false;
			}

			}else if ((document.forms[1].sFloatAccountNumber.value!=""  || document.forms[1].sStatus.value!="" ||
				document.forms[1].sClaimType.value!="" || document.forms[1].sInsuranceCompany.value!="" || 
					document.forms[1].sPaymentMethod.value!="") && (document.forms[1].sStartDate.value=="" ||
						document.forms[1].sEndDate.value=="")){
				alert("Please Select  Date Range!!!");
				document.forms[1].sStartDate.focus();
				return false;
			
			}
	}
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function isFutureDate(argDate){

	var dateArr=argDate.split("/");	
	var givenDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
	var currentDate = new Date();
	if(givenDate>currentDate){
	return true;
	}
	return false;
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


function onExportToExcel()
{ 
	parameterValue =  "|"+document.forms[1].sChequeNumber.value+"|"+document.forms[1].sFloatAccountNumber.value
	+"|"+document.forms[1].sStatus.value+"|"+document.forms[1].sStartDate.value+"|"+document.forms[1].sEndDate.value
	+"|"+document.forms[1].sClaimSettleNumber.value+"|"+document.forms[1].sClaimType.value+"|"+document.forms[1].sEnrollmentId.value
	+"|"+document.forms[1].sInsuranceCompany.value+"|"+document.forms[1].sCompanyCode.value+"|"+document.forms[1].sBatchNumber.value
	+"|"+document.forms[1].sPolicyNumber.value+"|"+document.forms[1].sQatarId.value+"|"+document.forms[1].sPaymentMethod.value+"|";
	var partmeter = "?mode=doGenerateReport&parameter="+parameterValue;
   	var openPage = "/ChequeSearchAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);   

}