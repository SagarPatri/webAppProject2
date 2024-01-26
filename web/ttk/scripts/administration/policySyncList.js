
	function toggle(sortid)
	{
	    document.forms[1].reset();
	    document.forms[1].sortId.value=sortid;
	    document.forms[1].mode.value="doSearchPolicySynk";
	    document.forms[1].action = "/policyListSyncAction.do";
	    document.forms[1].submit();
	}//end of toggle(sortid)

	//function to display the selected page
	function pageIndex(pagenumber)
	{
		document.forms[1].reset();
	    document.forms[1].mode.value="doSearchPolicySynk";
	    document.forms[1].pageId.value=pagenumber;
	    document.forms[1].action = "/policyListSyncAction.do";
	    document.forms[1].submit();
	}//end of pageIndex(pagenumber)

	//function to display previous set of pages
	function PressBackWard()
	{
	    document.forms[1].reset();
	    document.forms[1].mode.value ="doBackwardPolSynkList";
	    document.forms[1].action = "/policyListSyncAction.do";
	    document.forms[1].submit();
	}//end of PressBackWard()

	//function to display next set of pages
	function PressForward()
	{
	    document.forms[1].reset();
	    document.forms[1].mode.value ="doForwardPolSynkList";
	    document.forms[1].action = "/policyListSyncAction.do";
	    document.forms[1].submit();
	}//end of PressForward()

	
	



function onClose()
{
    document.forms[1].mode.value = "doClosePolicySynk";
    document.forms[1].action = "/policyListSyncAction.do";
    document.forms[1].submit();
}

function onSearchPolicySynk()
{
	if(!JS_SecondSubmit)
	 {
	    trimForm(document.forms[1]);
	   	document.forms[1].enrollmentId.value=trim(document.forms[1].enrollmentId.value);
	 	document.forms[1].mode.value = "doSearchPolicySynk";
	    document.forms[1].action = "/policyListSyncAction.do";
		JS_SecondSubmit=true
	    document.forms[1].submit();
	    	
	 }
}

function onSynchronize()
{
    if(!mChkboxValidation(document.forms[1]))
    {
    	var msg = confirm("Are you sure want to Synchronize the rules for the selected Policies?");
    	if(msg)
	    {
	    	document.forms[1].mode.value = "doAddPolicySync";
   			document.forms[1].action = "/policyListSyncAction.do";
	    	document.forms[1].submit();
	    }//end of if(msg)	
    }//end of if(!mChkboxValidation(document.forms[1]))
}
