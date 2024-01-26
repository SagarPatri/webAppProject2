//java script for the feedback screen in the empanelment of hospital flow.

//function to call edit screen
function edit(rownum)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doView";
		document.forms[1].child.value="Feedback Details";
		document.forms[1].rownum.value=rownum;
		document.forms[1].action = "/EditHospFeedbackAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of edit(rownum)

function toggle(sortid)
{
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/HospitalFeedbackAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/HospitalFeedbackAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/HospitalFeedbackAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/HospitalFeedbackAction.do";
    document.forms[1].submit();
}//end of PressForward()

function isValidated()
{
		//checks if start date is entered
		if(document.forms[1].startdate.value.length!=0)
		{
			if(isDate(document.forms[1].startdate,"Start Date")==false)
				return false;
		}// end of if(document.forms[1].startdate.value.length!=0)
		
		if(document.forms[1].fromDate.value.length!=0)
		{
			if(isDate(document.forms[1].fromDate,"Start Date")==false)
				return false;
		}// end of if(document.forms[1].startdate.value.length!=0)
		
		
		//checks if end Date is entered
		if(document.forms[1].enddate.value.length!=0)
		{
			if(isDate(document.forms[1].enddate,"End Date")==false)
				return false;
		}// end of if(document.forms[1].enddate.value.length!=0)
		//checks if both dates entered
		if(document.forms[1].startdate.value.length!=0 && document.forms[1].enddate.value.length!=0)
		{
			if(compareDates("startdate","Start Date","enddate","End Date","greater than")==false)
				return false;
		}// end of if(document.forms[1].startdate.value.length!=0 && document.forms[1].enddate.value.length!=0)
		return true;
}// end of isValidated()

function onSearch()
{
	if(!JS_SecondSubmit)
	 {
		if(isValidated())
		{
	    	document.forms[1].mode.value = "doSearch";
	    	document.forms[1].action = "/HospitalFeedbackAction.do";
			JS_SecondSubmit=true;
	    	document.forms[1].submit();
	    }// end of if(isValidated())
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function onAddEditFeedback()
{
    document.forms[1].mode.value = "doAdd";
    document.forms[1].child.value="Feedback Details";
    document.forms[1].action = "/EditHospFeedbackAction.do";
    document.forms[1].submit();
}//end of onAddEditUser()

function onDelete()
{
	if(!JS_SecondSubmit)
	 {
		if(!mChkboxValidation(document.forms[1]))
		{
			var msg = confirm("Are you sure you want to delete the selected record ?");
			if(msg)
			{
				document.forms[1].mode.value = "doDeleteList";
				document.forms[1].action = "/HospitalFeedbackAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}// end of if(msg)
		}//end of if(!mChkboxValidation(document.forms[1]))
	 }
}//end of onDelete()


//Adding status related scripts for intX
function onStatusChanged()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doStatusChange";
		document.forms[1].action="/HospitalFeedbackAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onStatusChanged()

function onSaveStatus()
{
  if(!JS_SecondSubmit)
  {	
	if(isStatusValidated())
	{
		document.forms[1].mode.value="doSaveStatus";
		document.forms[1].action="/HospitalFeedbackStatusSaveAction.do";
		JS_SecondSubmit=true;
 		document.forms[1].submit();
	}//end of if(isValidated())
  }//end of if(!JS_SecondSubmit)	
}//end of onSaveStatus()
function onReset()
{
	if(!JS_SecondSubmit)
	 {
		if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
		{
			document.forms[1].mode.value="doDefault";
			document.forms[1].action="/HospitalFeedbackAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
		else
		{
			document.forms[1].reset();
		}//end of else
	 }
}//end of onReset()
//Function to change the date.
function changeDate(){
    if(document.forms[1].fromDate.value != '' &&  isDate(document.forms[1].fromDate,"Start Date")== true && document.forms[1].drpYears.value != '')
    {
	var selVal = document.forms[1].drpYears.value;
	var FromDate = document.forms[1].fromDate.value;
	var dateArray = FromDate.split("/");
	
	var passedDay = dateArray[0];
	var passedMonth = dateArray[1];
	var passedYear = dateArray[2];

	var  dateStr = passedMonth+"/"+passedDay +"/"+passedYear;
	var myDate=new Date(dateStr);
	//subtracting one day from the original date
	myDate.setDate(myDate.getDate()-1);
	// add year
	var yrStr =eval(myDate.getFullYear())+eval(selVal);
	var newMonth = getActualMonth(myDate);
	var newDate = getDate(myDate.getDate())+"/"+newMonth+"/"+yrStr;
	document.forms[1].toDate.value=newDate;
	}// end of  if(document.forms[1].fromDate.value != '')
}//Edn of Function to change the date.

    function getDate(theDate)
    {
    var dateCtr="";
    var thedate="";
    thedate=theDate+"";
    if(thedate.length >= 2)
    {
     dateCtr = theDate ;
    }
    else
    {
    dateCtr = "0"+theDate;
	}
    return dateCtr;
    }
/*  Added by Unni */
	function getActualMonth(theDate)
	{
		var monthCtr="";
	if(eval(theDate.getMonth())==0) 
			monthCtr="01";
	else if(eval(theDate.getMonth())==1) 
			monthCtr="02";
	else if(eval(theDate.getMonth())==2) 
			monthCtr="03";
	else if(eval(theDate.getMonth())==3) 
			monthCtr="04";
	else if(eval(theDate.getMonth())==4) //feb
			monthCtr="05";
	else if(eval(theDate.getMonth())==5) //feb
			monthCtr="06";
	else if(eval(theDate.getMonth())==6) //feb
			monthCtr="07";
	else if(eval(theDate.getMonth())==7) //feb
			monthCtr="08";
	else if(eval(theDate.getMonth())==8) //feb
			monthCtr="09";
	else if(eval(theDate.getMonth())==9) //feb
			monthCtr="10";
	else if(eval(theDate.getMonth())==10) //feb
			monthCtr="11";
	else if(eval(theDate.getMonth())==11) //feb
			monthCtr="12";
		return monthCtr;
		}
	
function isStatusValidated()
{
	trimForm(document.forms[1]);
	if(document.getElementById("HidEmplStatusTypeId").value=='EMP')
	{
		 document.forms[1].empanelDate.value = document.forms[1].fromDate.value ;
	}//end of if(document.forms[1].status.value=='EMP')
/*	if(document.getElementById("HidEmplStatusTypeId").value!='ONH' && 
			document.getElementById("HidEmplStatusTypeId").value!='EMP' &&
			document.getElementById("HidEmplStatusTypeId").value!='INP' )//&& document.getElementById("HidEmplStatusTypeId").value!=''
*/		if(document.getElementById("HidEmplStatusTypeId").value=='DFC')
	
	{
		 document.forms[1].disEmpanelDate.value = document.forms[1].fromDate.value ;
	}//end of if(document.forms[1].status.value=='DIS')

	if(document.getElementById("HidEmplStatusTypeId").value=='CLS')
	{
		document.forms[1].closeDate.value = document.forms[1].fromDate.value ;
	}//end of if(document.forms[1].status.value=='CLS')
	return true;
}//end of isValidated()