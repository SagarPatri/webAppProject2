//java script for the log screen .

//function to sort based on the link selected
function toggle(sortid)
{
	document.forms[1].mode.value="doSearchLogDetails";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/CustomerBankLogDetails.do";
    document.forms[1].submit();
}//end of toggle(sortid)

function onSearch(element)
{
	if(!JS_SecondSubmit)
	 {
	    if(isValidated())
	    {
//	    	var selectedData=document.getElementById("logTypeId").value;
		    document.forms[1].mode.value = "doSearchLogDetails";
		    document.forms[1].action = "/CustomerBankLogDetails.do";
			JS_SecondSubmit=true;
			element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
		    document.forms[1].submit();
		}//end of if(isValidated())
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearchLogDetails";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/CustomerBankLogDetails.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackwardAllertLog";
    document.forms[1].action = "/CustomerBankLogDetails.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForwardAllertLog";
    document.forms[1].action = "/CustomerBankLogDetails.do";
    document.forms[1].submit();
}//end of PressForward()

function isValidated()
{

	document.forms[1].sStartDate.value=trim(document.forms[1].sStartDate.value);
	document.forms[1].sEndDate.value=trim(document.forms[1].sEndDate.value);
	if(document.forms[1].sStartDate.value.length!=0)
	{
		//checks for the validation of Start Date
		if(isDate(document.forms[1].sStartDate,"Start Date")==false)
			return false;
	}//end of if(document.forms[1].sStartDate.value.length!=0)

	if(document.forms[1].sEndDate.value.length!=0)
	{
		//checks for the validation of end date
		if(isDate(document.forms[1].sEndDate,"End Date")==false)
			return false;
	}//end of if(document.forms[1].sEndDate.value.length!=0)

	if(document.forms[1].sStartDate.value.length!=0 && document.forms[1].sEndDate.value.length!=0)
	{
		//checks if both the dates are entered
		if(compareDates("sStartDate","Start Date","sEndDate","End Date",'greater than')==false)
			return false;
	}//end of if()
	return true;
}//end of isValidated()