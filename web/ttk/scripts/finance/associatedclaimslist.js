//javascript used in associatedclaimslist.jsp of Finance flow

//function to sort based on the link selected
	function toggle(sortid)
	{
		document.forms[1].reset();
	    document.forms[1].mode.value="doSearch";
	    document.forms[1].sortId.value=sortid;
	    document.forms[1].action = "/AssociatedClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of toggle(sortid)

	//function to display the selected page
	function pageIndex(pagenumber)
	{
	    document.forms[1].reset();
	    document.forms[1].mode.value="doSearch";
	    document.forms[1].pageId.value=pagenumber;
	    document.forms[1].action = "/AssociatedClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of pageIndex(pagenumber)

	//function to display previous set of pages
	function PressBackWard()
	{
		document.forms[1].reset();
		document.forms[1].mode.value ="doBackward";
	    document.forms[1].action = "/AssociatedClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of PressBackWard()

	//function to display next set of pages
	function PressForward()
	{
		document.forms[1].reset();
		document.forms[1].mode.value ="doForward";
	    document.forms[1].action = "/AssociatedClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of PressForward()

	//functin to search
	function onSearch()
	{
	  if(!JS_SecondSubmit)
      {
     	trimForm(document.forms[1]);
		document.forms[1].mode.value = "doSearch";
    	document.forms[1].action = "/AssociatedClaimsSearchAction.do";
    	JS_SecondSubmit=true
    	document.forms[1].submit();
	  }//end of if(!JS_SecondSubmit)
	}//end of onSearch()

//function to associate/exclude
	function onAssociateExclude(task)
	{
	  if(!mChkboxValidation(document.forms[1]))
    	{			
		var msg = confirm('Are you sure you want to '+task);
		if(msg){
			if(!JS_SecondSubmit)
	      	{
				document.forms[1].mode.value = "doAssociateExclude";
				document.forms[1].action = "/AssociatedClaimsSearchAction.do";
				JS_SecondSubmit=true
				document.forms[1].submit();	
			}//end of if(!JS_SecondSubmit)
		}//end of if(msg)
	  	}//end of if(!mChkboxValidation(document.forms[1]))
	}//end of asociate/exclude
	
	function onClose()
	{
		if(!TrackChanges()) return false;
		document.forms[1].mode.value = "doClose";
		document.forms[1].action = "/AssociatedClaimsSearchAction.do";
		document.forms[1].submit();	
	}//end of onClose() 
	//function to check the total amount Associated 
	
	
	//to check the selected check box in the row. Same side client side validation with total approved amount and 
	//debit note amount
	/* function toCheckBox(obj, bChkd, objform )
	{
	    var bFlag = false;
        var chkoptCount=0;
        var chkedCount=0;
        var k=0;
        var sumApprovedAmt=0;
       if(bChkd == false)
        {
           //alert("1");
           objform.chkAll.checked =false;
        }//end of if(bChkd == false)
        //alert("2");
	   if(objform.chkopt.length)
	   {
    		chkoptCount=objform.chkopt.length;
	    	for(j=0;j<chkoptCount;j++)
	   		 {
    			 if(objform.chkopt[j].checked == true){
					chkedCount=eval(chkedCount)+1;    	  	
					sumApprovedAmt = sumApprovedAmt+ eval(claimsAmt[j]);
		    	 }// end of if(objform.chkopt[j].checked == true)
	   		 }// end of for
  		 }//end of if(objform.chkopt.length)
  		else
  		{
     		chkoptCount=1;
    	 	if(objform.chkopt.checked == true)
    	 	{
     		 	chkedCount=eval(chkedCount)+1;
     		 	sumApprovedAmt = sumApprovedAmt+ eval(claimsAmt[0]);
    		 }// end of if(objform.chkopt.checked == true)
    	    if(chkedCount!=chkoptCount)
            {
                //alert("1");
                objform.chkAll.checked =false;
            }
            else
            {
                //alert("2");
                objform.chkAll.checked =true;
            }
   		}   
  		if(sumApprovedAmt > debitNoteAmt)
  		{
    	alert("Total Approved Amount should not be greater than Debit Note Amount");
    	obj.checked = false;
    	objform.chkAll.checked = false;
    	}
	}*/

//to select/deselect all the records.Validation implemented to check total approved amount should not exceed the debit note amount
/*function selectAll( bChkd, objform )
{
	var sumApprovedAmt=0;
	var chkedCount=0;
	//alert('objform length :'+objform.length);
	//alert('bChkd ' + bChkd);
	if(bChkd)
	{//alert('break1');
		for(j=0;j<claimsAmt.length;j++)
		{    
			sumApprovedAmt = sumApprovedAmt+ eval(claimsAmt[j]);
		}//end of for(j=0;j<objform.chkopt.length;j++) 
		if(sumApprovedAmt > debitNoteAmt )
		{
			alert("Total Approved Amount should not be greater than Debit Note Amount");
			for(i=0;i<objform.length;i++)
			{
				if(objform.elements[i].name == "chkopt")
				{
				objform.elements[i].checked = false;
				}
			}
			objform.chkAll.checked =false;
			
		}//end of if(sumApprovedAmt > debitNoteAmt )
		else
		{//alert('break2');
			for(i=0;i<objform.length;i++)
			{
				if(objform.elements[i].name == "chkopt")
				{
				objform.elements[i].checked = bChkd;
				}
			}
		}  
		
	}//end of if(bChkd)
	else
	{//alert('break3');
		for(i=0;i<objform.length;i++)
			{
				if(objform.elements[i].name == "chkopt")
				{			
					objform.elements[i].checked = bChkd;
				}//end of if(objform.elements[i].name == "chkopt")
			}//end of for(i=0;i<objform.length;i++)
	}	
	//alert(sumApprovedAmt);
	//alert(debitNoteAmt);
}//end of  selectAll( bChkd, objform )*/


	

		
	
	
	



