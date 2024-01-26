//java script for the invoice details screen in the finance flow.

function Reset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
	{
  		document.forms[1].mode.value="doReset";
		document.forms[1].action="/InvoiceGeneralAction.do";
		document.forms[1].submit();	
 	}
 	else
 	{
 		document.forms[1].reset(); 			
 	}
}//end of Reset()

function onSave()
{
	var paymentType= document.forms[1].paymentTypeFlag.value;
	trimForm(document.forms[1]);
	 if(document.forms[1].groupRegnSeqID.value.length == 0){
			alert("Please select Corporate Name");
			 return false;
		 }
	 if(document.forms[1].policySeqID.value.length == 0){
			alert("Please select Policy Number");
			 return false;
		 }
	 
		

		if(!JS_SecondSubmit)
		{
			document.forms[1].mode.value="doSave";
			document.forms[1].action="/UpdateCreditAction.do?paymentType="+paymentType;
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)

	
}//end of onSave()

// function to close the invoice details screen.
function Close()
{
	if(!TrackChanges()) return false;	
	document.forms[1].mode.value="doClose";
	document.forms[1].tab.value ="Search";
	document.forms[1].action="/InvoiceGeneralAction.do";
	document.forms[1].submit();
}//end of Close()

function onListInvoices()
{
	if(document.forms[1].groupRegnSeqID.value.length == 0){
		alert("Please select Corporate Name");
		 return false;
	 }
	
 document.forms[1].mode.value="doSetFormValues";
 document.forms[1].action="/InvoiceGeneralAction.do";
 document.forms[1].submit();
}//end of onListInvoices()

//to generate TPACommission Report
function onGenerateTPARpt(startDate,endDate,seqID,policyseqID)
{
	
	if(!JS_SecondSubmit)
	{
		
		var paymentType= document.forms[1].paymentTypeFlag.value;
	var partmeter = "?mode=doGenerateTPACommissionRpt"+"&reportID=TPACommissionRpt&reportType=EXL"
	+"&startDate="+startDate+"&endDate="+endDate+"&seqID="+seqID+"&policySeqID="+policyseqID+"&paymentType="+paymentType;
	document.forms[1].action = "/ReportsAction.do"+partmeter;
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

function onGroupComp()
{
	document.forms[1].policyNbr.value = "";
	document.forms[1].policySeqID.value = "";
	document.forms[1].mode.value="doSelectGroup";
	document.forms[1].action="/InvoiceGeneralAction.do";
	document.forms[1].submit();
}

function paymentmodechange(obj)
{
	
	if(obj.value == "IMD" ||  obj.value == "ANL"){
		document.forms[1].dtdueDate2.value = "";
		 document.forms[1].dtdueDate3.value = "";
		 document.forms[1].dtdueDate4.value = "";
		 document.getElementById("dueDate1").style.display="";
		 document.getElementById("dueDate2").style.display="none";
		 document.getElementById("dueDate3").style.display="none";
		 document.getElementById("dueDate4").style.display="none";
		
		 $(".spanImmediate").removeAttr("style");
		 $(".spanHalfyear").attr("style","display:none");
		 $(".spanQuaterly").attr("style","display:none");
		 

	}
	if(obj.value == "HLF"){
		 document.forms[1].dtdueDate3.value = "";
		 document.forms[1].dtdueDate4.value = "";
		 document.getElementById("dueDate1").style.display="";
		 document.getElementById("dueDate2").style.display="";
		 document.getElementById("dueDate3").style.display="none";
		 document.getElementById("dueDate4").style.display="none";
	
		 $(".spanImmediate").attr("style","display:none");
		 $(".spanHalfyear").removeAttr("style");
		 $(".spanQuaterly").attr("style","display:none");
	}
	if(obj.value == "QTR"){
		 document.getElementById("dueDate1").style.display="";
		 document.getElementById("dueDate2").style.display="";
		 document.getElementById("dueDate3").style.display="";
		 document.getElementById("dueDate4").style.display="";
		 $(".spanImmediate").attr("style","display:none");
		 $(".spanHalfyear").attr("style","display:none");
		 $(".spanQuaterly").removeAttr("style");
	}
	
}

function isDateBetween()
{
	
	var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	var dtdueDate1 = document.forms[1].dtdueDate1.value;
	var dtdueDate2 = document.forms[1].dtdueDate2.value;
	var dtdueDate3 =document.forms[1].dtdueDate3.value;
	var dtdueDate4 =document.forms[1].dtdueDate4.value;
	
	if(document.forms[1].paymentMode.value == "IMD" || document.forms[1].paymentMode.value == "ANL" || document.forms[1].paymentMode.value == "HLF" || document.forms[1].paymentMode.value == "QTR"){
			if (dtdueDate1 == null || dtdueDate1 === "" || !pattern.test(dtdueDate1)){
		        alert("Date format should be (dd/mm/yyyy)");
		        return false;
		    }
	}

	if( document.forms[1].paymentMode.value == "QTR" || document.forms[1].paymentMode.value == "HLF" ){
		if (dtdueDate2 == null || dtdueDate2 === "" || !pattern.test(dtdueDate2))
			{
			 alert("Date format should be (dd/mm/yyyy)");
			return false;
			}
	}
	
   if( document.forms[1].paymentMode.value == "QTR"){
		if (dtdueDate3 == null || dtdueDate3 === "" || !pattern.test(dtdueDate3))
		{
			 alert("Date format should be (dd/mm/yyyy)");
			return false;
		}
	}

	if( document.forms[1].paymentMode.value == "QTR"){
		if (dtdueDate4 == null || dtdueDate4 === "" || !pattern.test(dtdueDate4))
			{
			 alert("Date format should be (dd/mm/yyyy)");
			return false;
			}

	}
	
	date1 = Date.parse(document.forms[1].dtdueDate1.value);
	date2 = Date.parse(document.forms[1].dtdueDate2.value);
	date3 = Date.parse(document.forms[1].dtdueDate3.value);
	date4 = Date.parse(document.forms[1].dtdueDate4.value);
	if((date4 <= date1 || date4 <= date2 || date4 <= date3 )) {
		alert("Mismatch in Due Date. ");
		return false;
	}//end of if((c <= e && c >= b))
	
	if((date3 <= date1 || date3 <= date2)) {
		alert("Mismatch in Due Date. ");
		return false;
	}//end of if((c <= e && c >= b))
	
	if((date2 <= date1 )) {
		alert("Mismatch in Due Date. ");
		return false;
	}//end of if((c <= e && c >= b))
	
	return true;
}//end of isDateBetween(beginDate,endDate,checkDate)

function refreshPolicyNo(){
	 document.forms[1].policyNbr.value = "";
	 document.forms[1].policySeqID.value = "";
}




