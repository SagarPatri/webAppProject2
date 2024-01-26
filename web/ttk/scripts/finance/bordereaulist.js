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
    document.forms[1].sortId.value=sortid;
    var partmeter = "?mode=doGetInvoiceList&from="+"Search";
    document.forms[1].action = "/GenerateBordereauAction.do"+partmeter;
    document.forms[1].submit();
}//end of toggle(sortid)

// JavaScript function for Page Indexing
function pageIndex(pagenumber)
{
	document.forms[1].reset();
	document.forms[1].pageId.value=pagenumber;
	var partmeter = "?mode=doGetInvoiceList&from="+"Search";
    document.forms[1].action = "/GenerateBordereauAction.do"+partmeter;
	document.forms[1].submit();
}// End of pageIndex()

//function to display next set of pages
function PressForward()
{   document.forms[1].reset();
    var partmeter = "?mode=doForward&from="+"Search";
    document.forms[1].action = "/GenerateBordereauAction.do"+partmeter;
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{   document.forms[1].reset();
    var partmeter = "?mode=doBackward&from="+"Search";
    document.forms[1].action = "/GenerateBordereauAction.do"+partmeter;
    document.forms[1].submit();
}//end of PressBackWard()

function onGenerateInvoice()
{
	document.forms[1].mode.value = "doGenerateLetter";
    document.forms[1].action = "/GenerateBordereauAction.do";
    document.forms[1].submit();
}//end of onAddInvoices()
function onGetInvoiceList(element)
{
	if(!JS_SecondSubmit)
    {
   var reinsurerval =	document.getElementById("reinsurerId").value;
   if(reinsurerval ==""){
	   alert("Reinsurer required to generate Report");
	   return;
   }
	document.forms[1].mode.value = "doGetInvoiceList";
	document.forms[1].action = "/GenerateBordereauAction.do";
	JS_SecondSubmit=true;
	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	document.forms[1].submit();
    }
}//end of onAddInvoices()
function onSearchReportList(element)
{
//	if(!JS_SecondSubmit)
//	{
		document.forms[1].mode.value = "doSearchReportList";
		document.forms[1].action = "/GenerateBordereauAction.do";
		JS_SecondSubmit=true;
		element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
		document.forms[1].submit();
//	}
}//end of onAddInvoices()

// JavaScript edit() function
function edit(rownum)
{
	var childDivs = document.getElementById('bordereauxReport');
	var children =childDivs.getElementsByTagName("a")[rownum];
	var partmeter = "?mode=doViewGenerateReport&rownum="+rownum+"&fileName="+children.innerHTML;
	var openPage = "/GenerateBordereauAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=0,status=1,toolbar=no,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}

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