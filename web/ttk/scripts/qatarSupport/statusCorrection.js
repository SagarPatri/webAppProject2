function onStatusCorrection(){
	
	if(getCheckedCount('chkopt',document.forms[1])> 0)
    {
		var remarks= document.forms[1].remarks.value;
		if(document.forms[1].remarks.value =="")
		{
			alert('Please enter TicketNo./Remarks');
			return true;
		}
		 var space = remarks.charCodeAt(0);
		  if(space==32)
		  {
				 alert("TicketNo./Remarks should not start with space.");
				 document.getElementById("remarks").value="";
		       		document.getElementById("remarks").focus();
				 return;
		  }
		
		var msg = confirm("Are you sure you want to Revert for the selected Records");
		if(msg)
			{
				document.forms[1].mode.value="doStatusCorrection";
				document.forms[1].action="/StatusCorrectionAction.do";
				document.forms[1].submit();
			}//end of if(msg)
    }//end of if(!mChkboxValidation(document.forms[1]))
    else
    {
    	alert('Please select atleast one record');
		return false;
	}
		    
	}



function getCheckedCount(chkName,objform)
{
    var varCheckedCount = 0;
    for(i=0;i<objform.length;i++)
    {
	    if(objform.elements[i].name == chkName)
	    {
		    if(objform.elements[i].checked)
		        varCheckedCount++;
	    }//End of if(objform.elements[i].name == chkName)
    }//End of for(i=0;i>objform.length;i++)
    return varCheckedCount;
}//End of function getCheckedCount(chkName)


function onSearch(){
	
	var batchDate = document.getElementById("batchDate").value;
	if(batchDate!=""){
	var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;	
	if(!pattern.test(batchDate)){
		alert("Date formate should be in DD/MM/YYYY");
		document.getElementById("batchDate").value="";
		document.getElementById("batchDate").focus();
		return false;
	}
	}
	  document.forms[1].mode.value="doSearch";
	    document.forms[1].action = "/StatusCorrectionAction.do";
	    document.forms[1].submit();
}


function toggle(sortid)
{
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/StatusCorrectionAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)


function pageIndex(pagenumber)
{
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/StatusCorrectionAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)


function PressBackWard()
{
	document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/StatusCorrectionAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
	document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/StatusCorrectionAction.do";
    document.forms[1].submit();
}//end of PressForward()

function isNumaricOnly(upObj){
	
	var re = /^[0-9]*\.*[0-9]*$/;	
	var objValue=upObj.value;
	if(objValue.length>=1){
		if(!re.test(objValue)){
			alert("Please Enter Numbers only");
			upObj.value="";
			upObj.focus();
		}
	}  
	
}

function showFinanceTemplate()
{
	   document.forms[1].mode.value="doShowClaimFinanceStatusTemplate";
	   // document.forms[1].action = "/ClaimsSearchAction.do";
	   document.forms[1].action = "/StatusCorrectionAction.do";
	    document.forms[1].submit();
}

function onUploadClaimSettlementNumber()
{

	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	    document.forms[1].mode.value = "doPaymentUploadBatchDetail";
	    document.forms[1].action = "/StatusCorrectionAction.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	
	 }//end of if(!JS_SecondSubmit)
}

function onDownloadPaymentUploadLog(){
		   document.forms[1].mode.value="doDownloadUploadLog";
		    document.forms[1].action = "/StatusCorrectionAction.do";
		    document.forms[1].submit();
	}

