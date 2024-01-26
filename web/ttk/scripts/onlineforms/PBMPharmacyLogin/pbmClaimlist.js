

function edit(rownum){
	
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value = "doViewclaimDetails";
    document.forms[1].rownum.value=rownum;
    
    document.forms[1].action = "/PbmClaimAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of edit(rownum)
function edit2(rownum){
	
	
	var openPage = "/OnlineReportsAction.do?mode=doViewCommonForAll&module=claimSettlementFile&rownum="+rownum;
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}//end of edit(rownum)


function toggle(sortid)
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/PbmClaimAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/PbmClaimAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/PbmClaimAction.do";
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
    document.forms[1].action = "/PbmClaimAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()

function isTrtmntValidated()
{
		//checks if from Date is entered
		if(document.forms[1].trtmtFromDate.value.length!=0)
		{
			if(isDate(document.forms[1].trtmtFromDate,"from Date")==false)
				return true;
			//document.forms[1].fromDate.focus();
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)
		if(document.forms[1].trtmtFromDate.value.length!=0){
			if(isFutureDate(document.forms[1].trtmtFromDate.value)){
				alert("From date should not be future date ");
				return true;
			}
		}
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

		return true;
}//end of isValidated()

//claim From To Dates validations
function validateDates()
{
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
	
	
		return false;
}//end of validateDates()

    	


function onSearch()
{
if(validateDates()){
    	
    	return false;
    }
	if(!JS_SecondSubmit)
	 {
	   
			trimForm(document.forms[1]);
	    	document.forms[1].mode.value = "doSearch";
	    	document.forms[1].action = "/PbmClaimAction.do";
			JS_SecondSubmit=true;
	    	document.forms[1].submit();
	    
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

function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/PbmClaimAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()

function onUserIcon(rownum)
{
	document.forms[1].mode.value="doAssign";
	document.forms[1].child.value="Assign";
	document.forms[1].rownum.value=rownum;
	document.forms[1].action="/PbmClaimAction.do";
	document.forms[1].submit();
}//end of onUserIcon(rownum)
function onUserIcon1(rownum)
{
	document.forms[1].mode.value="doSearch";
	document.forms[1].child.value="Investigation";
	document.forms[1].rownum.value=rownum;
	document.forms[1].action="/SupportDocAction.do";
	document.forms[1].submit();
}//end of onUserIcon(rownum)

function onStatusChanged()
{
	document.forms[1].mode.value="doStatusChange";
	document.forms[1].action="/PbmClaimAction.do";
	document.forms[1].submit();
}//end of onStatusChanged()