//functions for claimslist screen of claims flow.

function addClaim(){
	
	document.forms[1].reset();
    document.forms[1].rownum.value = "";
    if(!JS_SecondSubmit){
    document.forms[1].tab.value ="General";
    document.forms[1].mode.value = "AddClaim";
    document.forms[1].action = "/ClaimGeneralAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of function addClaim()

function edit(rownum){
    document.forms[1].mode.value="doView";
    document.forms[1].rownum.value=rownum;
    if(!JS_SecondSubmit){
    document.forms[1].tab.value ="General";
    document.forms[1].action = "/ClaimsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}//end of edit(rownum)

function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    if(!JS_SecondSubmit){
    document.forms[1].action = "/ClaimsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
    }
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ClaimsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ClaimsAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	if(!JS_SecondSubmit){
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ClaimsAction.do";
	 JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()

function onSearch(element)
{
   if(!JS_SecondSubmit)
 {
	   var claimStatus=document.forms[1].sStatus.value;
	   var searchFlag=false;
	trimForm(document.forms[1]);
	   var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	   
	 var recievedDate=document.forms[1].sRecievedDate.value;
	if (recievedDate.length>1 && !pattern.test(recievedDate)){
  alert("Date format should be (dd/mm/yyyy)");
  return;
 }
	if(claimStatus=='' ||claimStatus=="APR"||claimStatus=="REJ"){
			if(document.forms[1].sClaimNO.value!="" || document.forms[1].sBatchNO.value!="" || document.forms[1].sSettlementNO.value!="" || document.forms[1].sEnrollmentId.value!="" || 
					document.forms[1].sPolicyNumber.value!="" || document.forms[1].sInvoiceNO.value!="" || document.forms[1].sRecievedDate.value!="" || (document.forms[1].sProviderName.value!="" && document.forms[1].sProviderName.value!="ANY")
					|| (document.forms[1].cfdInvestigationStatus.value!="" && document.forms[1].cfdInvestigationStatus.value!="ANY"))
			{
				searchFlag=true;
			}
	if(searchFlag==false)
		{
			alert("Please enter atleast one search criteria like Claim No. , Batch No. , Settlement No., Policy No., Invoice No., Received Date, Provider Name or Al koot ID");
			document.forms[1].name.focus();
			return false;
		}
	}
	
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/ClaimsAction.do";
	JS_SecondSubmit=true;
	element.innerHTML	=	"<b>"+ "Please Wait...!!"+"</b>";
	document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/ClaimsAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()

function onDelete()
{
    if(!mChkboxValidation(document.forms[1]))
    {
		var msg = confirm("Are you sure you want to delete the selected records ?");
		if(msg)
		{
			if(!JS_SecondSubmit){
			document.forms[1].mode.value = "doDeleteList";
			document.forms[1].action = "/DeleteClaimsAction.do";
			 JS_SecondSubmit=true;
			document.forms[1].submit();
			}
		}// end of if(msg)
	}//end of if(!mChkboxValidation(document.forms[1]))
}//end of onDelete()

function onUserIcon(rownum)
{	
	if(!JS_SecondSubmit)
	{
		if(document.forms[1].AssignFlagYN.value == "N")
		{
			document.forms[1].mode.value="doAssign";
			document.forms[1].rownum.value=rownum;
			document.forms[1].child.value="Assign";
			document.forms[1].action="/AssignToActionClaim.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}
		else
		{
			document.forms[1].mode.value="doAssign";
			document.forms[1].rownum.value=rownum;
			document.forms[1].child.value="Assign";
			document.forms[1].action="/AssignToAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}
	}
}//end of onUserIcon(rownum)
function onStatusChanged()
{
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doStatusChange";
	document.forms[1].action="/ClaimsAction.do";
	 JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onStatusChanged()

function onRangeChanged()
{
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doRangeChange";
	document.forms[1].action="/ClaimsAction.do";
	 JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onStatusChanged()

function onAssignUser(rownum)
{
	if(!JS_SecondSubmit)
	{
		if(!mChkboxValidation(document.forms[1]))
	    {
			if(document.forms[1].AssignAllFlagYN.value == "N")
			{
				document.forms[1].mode.value="doMultipleAssign";
				document.forms[1].child.value="AssignAll"; 
				document.forms[1].action="/AssignToActionClaim.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}
			else
			{
				document.forms[1].mode.value="doMultipleAssign";
				document.forms[1].child.value="AssignAll"; 
				document.forms[1].action="/AssignToAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}
	    }
	}
}

function onPriorityChanged()
{
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doPriorityChanged";
	document.forms[1].action="/ClaimsAction.do";
	 JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onStatusChanged()