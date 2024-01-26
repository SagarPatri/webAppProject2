//functin to search
	function onSearch(element)
	{
	
	trimForm(document.forms[1]);
		 
	  if(!JS_SecondSubmit)
      {     		
    	    if(isValidated())
    	    {
			document.forms[1].mode.value = "doSearch";
	    	document.forms[1].action = "/InvoicesAction.do";
	    	JS_SecondSubmit=true;
	    	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	    	document.forms[1].submit();
	    }//end of if(isValidated())
	   }//end of if(!JS_SecondSubmit)
	}

//function to sort based on the link selected
function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/InvoicesAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

// JavaScript function for Page Indexing
function pageIndex(pagenumber)
{
	document.forms[1].reset();
	document.forms[1].mode.value="doSearch";
	document.forms[1].pageId.value=pagenumber;
	document.forms[1].action="/InvoicesAction.do";
	document.forms[1].submit();
}// End of pageIndex()

//function to display next set of pages
function PressForward()
{   document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/InvoicesAction.do";
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{   document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/InvoicesAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

function onAddInvoices()
{
	var paymentType= document.getElementById("paymentid").value;
	
	if(paymentType == ""){
		alert("Please select payment");
		return false;
	} 
    document.forms[1].reset();
    document.forms[1].rownum.value = "";
    document.forms[1].mode.value = "doAdd";
    if(paymentType == "ADD"){
        document.forms[1].tab.value="General";

    }else if(paymentType == "DEL"){
        document.forms[1].tab.value="Credit Note";

    }
    document.forms[1].action = "/InvoiceGeneralAction.do?paymentType="+paymentType;
    document.forms[1].submit();
}//end of onAddInvoices()

// JavaScript edit() function
function edit(rownum)
{
	document.forms[1].rownum.value=rownum;
    document.forms[1].mode.value="doView";
   // document.forms[1].tab.value="General";
    document.forms[1].action = "/InvoiceGeneralAction.do";
    document.forms[1].submit();
}// End of edit()

function isValidated()
{
		
		//checks if From Date is entered
		if(document.forms[1].sFromDate.value.length!=0)
		{
			if(isDate(document.forms[1].sFromDate,"From Date")==false)
				return false;
				document.forms[1].sFromDate.focus();
		}// end of if(document.forms[1].sFromDate.value.length!=0)

		//checks if To Date is entered
		if(document.forms[1].sToDate.value.length!=0)
		{
			if(isDate(document.forms[1].sToDate,"To Date")==false)
				return false;
				document.forms[1].sToDate.focus();
		}// end of if(document.forms[1].sToDate.value.length!=0)

		//checks if both dates entered
		if(document.forms[1].sFromDate.value.length!=0 && document.forms[1].sToDate.value.length!=0)
		{
			if(compareDates("sFromDate","From Date","sToDate","To Date","greater than")==false)
				return false;
		}// end of if(document.forms[1].sFromDate.value.length!=0 && document.forms[1].sToDate.value.length!=0)
		return true;
}//end of isValidated()