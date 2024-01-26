//functions for claimslist screen of claims flow.
function onAdd(){
	document.forms[1].reset();
    document.forms[1].rownum.value = "";
    document.forms[1].tab.value ="General";
    document.forms[1].mode.value = "addBatch";
    document.forms[1].action = "/ClaimBatchGeneralAction.do";
    document.forms[1].submit();
}//end of function addBatch()

function onClaimUpload(){
	if(!JS_SecondSubmit)
	{
	    document.forms[1].tab.value ="General";
	    document.forms[1].mode.value = "doClaimUpload";
	    document.forms[1].action = "/ClaimBatchGeneralAction.do";
	    JS_SecondSubmit=true;
	    document.forms[1].submit();
   	}
}//end of function addBatch()



function edit(rownum){
    document.forms[1].mode.value="doView";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value ="General";
    document.forms[1].action = "/ClaimBatchListAction.do";
    document.forms[1].submit();
}//end of edit(rownum)

function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/ClaimBatchListAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ClaimBatchListAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ClaimBatchListAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ClaimBatchListAction.do";
    document.forms[1].submit();
}//end of PressForward()


function onSearch(element)
{
   if(!JS_SecondSubmit)
 {
	   var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	trimForm(document.forms[1]);
	 var fromDate=document.forms[1].sBatchReceivedFrom.value;
	 var toDate=document.forms[1].sBatchReceivedTo.value;
	if (fromDate.length>1 && !pattern.test(fromDate) || toDate.length>1 && !pattern.test(toDate)){
     alert("Date format should be (dd/mm/yyyy)");
     return;
    }
	
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/ClaimBatchListAction.do";
	JS_SecondSubmit=true;
	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/ClaimBatchListAction.do";
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
		    }//End JS
		}// end of if(msg)
	}//end of if(!mChkboxValidation(document.forms[1]))
}//end of onDelete()

function onUserIcon(rownum)
{
	document.forms[1].mode.value="doAssign";
	document.forms[1].rownum.value=rownum;
	document.forms[1].action="/AssignToAction.do";
	document.forms[1].submit();
}//end of onUserIcon(rownum)


function onAssignUser(rownum)
{
	if(!JS_SecondSubmit)
	{
		if(!mChkboxValidation(document.forms[1]))
	    {
				document.forms[1].mode.value="doBatchAssign";
				document.forms[1].rownum.value=rownum;
				document.forms[1].action="/ClaimBatchAssignAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
	    }
	}
}


function onBatchAssignSave()
{
	trimForm(document.forms[1]);
	if(!JS_SecondSubmit){
		
    	if(document.forms[1].officeSeqID.value==null || document.forms[1].officeSeqID.value =='')
		{	
			alert("Please Select Al Koot Branch !!");
			document.forms[1].officeSeqID.focus();
			return false;
		}
    	
    	if(document.forms[1].doctor.value==null || document.forms[1].doctor.value =='')
		{	
			alert("Please Select Assigned To !!");
			document.forms[1].doctor.focus();
			return false;
		}
    	
		/*var validateFlag=myCheckValidation();
		if(validateFlag==false){
			 alert('No item selected for Assign Claim Status');
			 return false;
		}*/
    	
    	if(document.forms[1].assignRemarks.value==null || document.forms[1].assignRemarks.value =='')
		{	
			alert("Please Select Assign Remarks !!");
			document.forms[1].assignRemarks.focus();
			return false;
		}
    	
	    	if((document.forms[1].assignRemarks.value=='Others') && document.forms[1].otherRemarks.value =='')
			{	
				alert("Please provide Other Remarks !!");
				document.forms[1].otherRemarks.focus();
				return false;
			}
		document.forms[1].mode.value="doBatchAssignSave";
		document.forms[1].action="/ClaimBatchAssignAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)	
}//end of onSave(

function onBatchAssignHis()
{
	trimForm(document.forms[1]);
	if(!JS_SecondSubmit){
		document.forms[1].mode.value="doViewBatchAssignHis";
		document.forms[1].action="/ClaimBatchAssignHistoryAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)	
}//end of onSave(

function onHistorySearch(element)
{
   if(!JS_SecondSubmit)
 {
	  /* var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	trimForm(document.forms[1]);
	 var fromDate=document.forms[1].sBatchReceivedFrom.value;
	 var toDate=document.forms[1].sBatchReceivedTo.value;
	if (fromDate.length>1 && !pattern.test(fromDate) || toDate.length>1 && !pattern.test(toDate)){
     alert("Date format should be (dd/mm/yyyy)");
     return;
    }*/
	
	document.forms[1].mode.value = "doHistorySearch";
	document.forms[1].action = "/ClaimBatchAssignHistoryAction.do";
	JS_SecondSubmit=true;
	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function onBack(){
	if(!JS_SecondSubmit){
	document.forms[1].tab.value ="Search";
		  document.forms[1].mode.value="doBack";
		  document.forms[1].action="/ClaimUploadAction.do";	
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
	}
}


function myCheckValidation(){ 
	var i,
	  chks = document.getElementsByName("assignClaimStatus");
	  for (i = 0; i < chks.length; i++){
	    if (chks[i].checked){	        
	        return true;
	    }else{
	       // alert('No item selected for Assign Claim Status');
	        return false;
	    }
	  }
}
