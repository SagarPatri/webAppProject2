//java script for the edit productsync list screen in the administration of products flow.

//function to sort based on the link selected
function toggle(sortid)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/ProductListSyncAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/ProductListSyncAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages

//function to display next set of pages
function PressForward()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/ProductListSyncAction.do";
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/ProductListSyncAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

function onSynchronize()
{
    if(!mChkboxValidation(document.forms[1]))
    {
    	var msg = confirm("Are you sure want to Synchronize the rules for the selected Policies?");
    	if(msg)
	    {
	    	document.forms[1].mode.value = "doAddProductSync";
   			document.forms[1].action = "/ProductListSyncAction.do";
	    	document.forms[1].submit();
	    }//end of if(msg)	
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of onSynchronize()

//denial process
function onInsurerSynchronize()
{
    if(!mChkboxValidation(document.forms[1]))
    {
    	var msg = confirm("Are you sure want to Synchronize the insurer denial process for the selected Policies?");
    	if(msg)
	    {
	    	document.forms[1].mode.value = "doAddInsProductSync";
   			document.forms[1].action = "/ProductListSyncAction.do";
	    	document.forms[1].submit();
	    }//end of if(msg)	
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of onInsurerSynchronize()
//denial process
function onSearch()
{
if(!JS_SecondSubmit)
 {
    trimForm(document.forms[1]);
	    if(isValidated())
	    {
	    	document.forms[1].sPolicyNbr.value=trim(document.forms[1].sPolicyNbr.value);
	    	document.forms[1].sCorporateName.value=trim(document.forms[1].sCorporateName.value);
	    	document.forms[1].sValidFrom.value=trim(document.forms[1].sValidFrom.value);
	    	document.forms[1].sValidDate.value=trim(document.forms[1].sValidDate.value);
    		document.forms[1].mode.value = "doSearch";
    		document.forms[1].action = "/ProductListSyncAction.do";
			JS_SecondSubmit=true
    		document.forms[1].submit();
    	}	
 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function onClose()
{
    document.forms[1].mode.value = "doClose";
    document.forms[1].action = "/ProductListAction.do";
    document.forms[1].submit();
}//end of ()

function isValidated()
{
	var strValidFrom = document.forms[1].sValidFrom;	
	var strValidDate = document.forms[1].sValidDate;	
		//checks if Valid From is entered
		if(strValidFrom.value.length!=0)
		{
			if(isDate(strValidFrom,"Valid From")==false)
				return false;
				strValidFrom.focus();
		}// end of if(strValidFrom.value.length!=0)
		//checks if Valid Date is entered
		if(strValidDate.value.length!=0)
		{
			if(isDate(strValidDate,"Valid To")==false)
				return false;
				strValidDate.focus();
		}// end of if(strValidDate.value.length!=0)
		//checks if both dates entered
		if(strValidFrom.value.length!=0 && strValidDate.value.length!=0)
		{
			if(compareDates("sValidFrom","Valid From","sValidDate","Valid To","greater than")==false)
				return false;
		}// end of if(strValidFrom.value.length!=0 && strValidDate.value.length!=0)
		return true;
}//end of isValidated()