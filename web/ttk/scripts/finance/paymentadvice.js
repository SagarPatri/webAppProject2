//java script for the payment advice screen

//function to sort based on the link selected
function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/PaymentAdviceAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

// JavaScript function for Page Indexing
function pageIndex(pagenumber)
{
	document.forms[1].reset();
	document.forms[1].mode.value="doSearch";
	document.forms[1].pageId.value=pagenumber;
	document.forms[1].action="/PaymentAdviceAction.do";
	document.forms[1].submit();
}// End of pageIndex()

//function to display next set of pages
function PressForward()
{   document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/PaymentAdviceAction.do";
    document.forms[1].submit();
}//end of PressForward()

//function to display previous set of pages
function PressBackWard()
{   document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/PaymentAdviceAction.do";
    document.forms[1].submit();
}//end of PressBackWard()


//functin to search
	function onSearch(element)
	{
	  if(!JS_SecondSubmit)
      {

			trimForm(document.forms[1]);
			document.forms[1].mode.value = "doSearch";
	    	document.forms[1].action = "/PaymentAdviceAction.do";
	    	JS_SecondSubmit=true;
	    	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	    	document.forms[1].submit();
	   }//end of if(!JS_SecondSubmit)
	}//end of if(isValidated()==true)
// JavaScript edit() function

//functin to GenerateXL
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

	function onGenerateXL()
	{
		if(getCheckedCount('chkopt',document.forms[1])> 0)
	    {
			var msg = confirm("Are you sure you want to Generate Excel for the selected Records");
			if(msg)
				{
				if(document.forms[1].sreportFormat.value == 'OIC Report')
				{
					document.forms[1].mode.value="doGenerateUTIXL";
					}
					else
					{
					document.forms[1].mode.value="doGenerateXLAlKoot";
					}
					document.forms[1].action="/PaymentAdviceAction.do";
					document.forms[1].submit();
				}//end of if(msg)
	    }//end of if(!mChkboxValidation(document.forms[1]))
	    else
	    {
	    	alert('Please select atleast one record');
			return true;
    	}
    	
	}//end of functin onGenerateXL()
	function onGenerateUTIXL()
	{
		if(getCheckedCount('chkopt',document.forms[1])> 0)
	    {
			var msg = confirm("Are you sure you want to Generate Excel for the selected Records");
			if(msg)
				{
					document.forms[1].mode.value="doGenerateUTIXL";
					document.forms[1].action="/PaymentAdviceAction.do";
					document.forms[1].submit();
				}//end of if(msg)
	    }//end of if(!mChkboxValidation(document.forms[1]))
	    else
	    {
		alert('Please select atleast one record');
			return true;
	}
	}//end of functin onGenerateXL()
	
	function onUploadBatchdetail()
	{
		
		if(!JS_SecondSubmit)
		{
			
			var incuredCurencyFormat=document.getElementById("incuredCurencyFormat").value;
			
			if(incuredCurencyFormat!=""&&incuredCurencyFormat!=null)
			{
				trimForm(document.forms[1]);
				document.forms[1].mode.value = "doPaymentAdviceUploadBatchdetail";
				document.forms[1].action = "/PaymentAdviceAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}else
			{
				alert("Please Select Currency Format");
			}
		}//end of if(!JS_SecondSubmit)
			
	}
	
	function onPaymentAdviceLogSearch()
	{
		 trimForm(document.forms[1]);
			
		 if(document.forms[1].startDate.value=="")
			{
				alert("Please enter the Start Date ");
				document.forms[1].elements[i].focus();
				return false;
			}
			else if(document.forms[1].endDate.value=="")
			{
				alert("Please enter the End Date ");
				document.forms[1].elements[i].focus();
				return false;
			}
			else if(compareDates("startDate","Start Date","endDate","End Date","greater than")==false)
			{
				document.forms[1].endDate.value="";
			    return false;
			}
			
	       if(!isDate(document.forms[1].startDate,"Start Date"))
		   	{
		   		document.forms[1].startDate.focus();
		   		return false;
		   	}
		   	if(!isDate(document.forms[1].endDate,"End Date"))
		   	{
		   		document.forms[1].endDate.focus();
		   		return false;
		   	}
		    if(!compareDates('startDate','Start Date','endDate','End Date','greater than'))
		    {
		    	return false;
		    }//end of if(!compareDates('sReportFrom','Report From','sReportTo','Report To','greater than'))

		//    document.forms[1].mode.value="doLogDetailsExcelUploads";
		    document.forms[1].action = "/PaymentAdviceAction.do?mode=doLogDetailsExcelUploads&Flag=A";
		    document.forms[1].submit();

	}
	
	function onDownloadPaymentUploadLog(){
		   document.forms[1].mode.value="doDownloadPaymentUploadLog";
		    document.forms[1].action = "/ClaimsSearchAction.do";
		    document.forms[1].submit();
	}
	
	function toCheckBox(obj, bChkd, objform )
	{
		var count=0;
			
	        var fundslimit = 0;
	        objform.totalAmt.value = eval(0);
	        objform.availBalance.value=eval(0);
	        //Added For CR-0145
	        objform.totalPaymentDiscount.value = eval(0);
	        objform.totalPayableDiscount.value = eval(0);
	        
	        document.getElementById("totlAmountinConvert").value=eval(0);
	        document.getElementById("convertedTotalAmt").value=eval(0);
	        document.getElementById("convertedTotalUSDAmt").value=eval(0);
	        var tempCurr	=	"";
	        var tempCurrStr	=	"";
	        var tempCurrStrArr	=	"";
	        var bFlag = false;
	        var oFlag = false;
	        var sFlag = false;
	        var countSameCurr=0;
            var countMultCurr=0;
	        var incuredCurencyFormat=document.getElementById("incuredCurencyFormat").value;

	        if(bChkd == false)
	        {
	          objform.chkAll.checked =false;
	        }//end of if(bChkd == false)
            var chkoptCount=0;
            var chkedCount=0;
	            var k=0;
	   if(objform.chkopt.length){
	    chkoptCount=objform.chkopt.length;
	    for(j=0;j<chkoptCount;j++){
	     if(objform.chkopt[j].checked == true){
	      chkedCount=eval(chkedCount)+1;
	      if(objform.totalAmt.value=="null")
	      objform.totalAmt.value=eval(0);
	      objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[j]);
	      
	      //Added For Cr-0145
	      if(objform.totalPaymentDiscount.value=="null")
		      objform.totalPaymentDiscount.value=eval(0);
		      objform.totalPaymentDiscount.value = eval(objform.totalPaymentDiscount.value) + eval(discountedAmount[j]);
	      if(objform.totalPayableDiscount.value=="null")
		      objform.totalPayableDiscount.value=eval(0);
		      objform.totalPayableDiscount.value = eval(objform.totalPayableDiscount.value) + eval(payableAmount[j]);
	   //   alert("claimTypeDesc::::"+claimTypeDesc[0]);
	      
	      if(document.getElementById("convertedTotalAmt").value=="")
	      document.getElementById("").value=eval(0);
	      if(document.getElementById("convertedTotalUSDAmt").value=="")
		      document.getElementById("").value=eval(0);
	      if(claimTypeDesc == "Member"){
          		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	    		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
	      }
	          else if(claimTypeDesc == "Network"){
              		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	        		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
	          }


	     
	      if(document.getElementById("totlAmountinConvert").value=="")
	      document.getElementById("totlAmountinConvert").value=eval(0);
	      document.getElementById("totlAmountinConvert").value=eval(document.getElementById("totlAmountinConvert").value) + eval(claimsAmt[j]);
	   //   alert("1:::::"+ document.getElementById("totlAmountinConvert").value);

	      //TO Display Converted Total Amount Currency Format Based on selection S T A R T S
          if(incurredCurr[j]!="-" && incurredCurr[j]!=null && incurredCurr[j]!="null")
        	  tempCurr	=	incurredCurr[j];
    	if(tempCurrStr!="")
    	{

    		tempCurrStr	=	tempCurrStr.substring(0,tempCurrStr.length-1);
    		tempCurrStrArr	=	tempCurrStr.split(",");
    		for(var k=0;k<tempCurrStrArr.length;k++)
    		{
    			if(tempCurr==tempCurrStrArr[k])
    				countSameCurr	=	countSameCurr+1;
    			else
    				countMultCurr	=	countMultCurr+1;
    		}
    		if(countSameCurr>0 && countMultCurr>0)
    			sFlag	=	false;
    		else if(countSameCurr>0 && countMultCurr==0)
    			sFlag	=	true;
    		else if(countSameCurr==0 && countMultCurr>0)
    			sFlag	=	false;
    		else if(countSameCurr==0 && countMultCurr==0)
    			sFlag	=	false;
    	}
        	  tempCurrStr	=	tempCurrStr.concat(tempCurr+",");
              if(tempCurr=="QAR")
            	  bFlag	=	true;
              else
            	  oFlag	=	true;
                //TO Display Converted Total Amount Currency Format Based on selection E N D S 
	     }// end of if(objform.chkopt[j].checked == true)
                 
	    }// end of for
	   }else{
		   tempCurr	=	incurredCurr[0];
	     chkoptCount=1;
	     if(objform.chkopt.checked == true){
	      chkedCount=eval(chkedCount)+1;
	      if(objform.totalAmt.value=="null")
	        objform.totalAmt.value=eval(0);
	      objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[0]);
	      //Added For CR-0145
	      if(objform.totalPaymentDiscount.value=="null")
		        objform.totalPaymentDiscount.value=eval(0);
		      objform.totalPaymentDiscount.value = eval(objform.totalPaymentDiscount.value) + eval(discountedAmount[0]);
	      if(objform.totalPayableDiscount.value=="null")
		        objform.totalPayableDiscount.value=eval(0);
		      objform.totalPayableDiscount.value = eval(objform.totalPayableDiscount.value) + eval(payableAmount[0]);
	     
	      if(document.getElementById("convertedTotalAmt").value=="")
	    	  document.getElementById("convertedTotalAmt").value=eval(0);
	      if(claimTypeDesc == "Member"){
          		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
	    		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[0]);
	      }
	          else if(claimTypeDesc == "Network"){
              		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
	        		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[0]);
	          }


	      if(document.getElementById("totlAmountinConvert").value=="")
	    	  document.getElementById("totlAmountinConvert").value=eval(0);
	      document.getElementById("totlAmountinConvert").value=eval(document.getElementById("totlAmountinConvert").value) + eval(claimsAmt[0]);
	    //  alert("2:::::"+ document.getElementById("totlAmountinConvert").value);

	      
	     }// end of if(objform.chkopt.checked == true)
	   }

	   if(eval(objform.avblFloatBalance.value) >= eval(objform.totalAmt.value)){
	      objform.availBalance.value = eval(objform.avblFloatBalance.value - objform.totalAmt.value);
	      fundslimit = 1;
	   }else{
	     objform.availBalance.value = eval(objform.avblFloatBalance.value - objform.totalAmt.value);
	     fundslimit=-1
	   }
	   if(fundslimit == -1){
	    alert("Insufficient funds");
	    obj.checked = false;
	    objform.chkAll.checked = false;
	    objform.totalAmt.value = 0;
	    objform.totalPaymentDiscount.value = 0;
	    objform.totalPayableDiscount.value = 0;
	    document.getElementById("convertedTotalAmt").value = eval(0);
	    document.getElementById("totlAmountinConvert").value = eval(0);

	    
	if(chkoptCount>1) {
	     for(j=0;j<chkoptCount;j++){
	     if(objform.chkopt[j].checked == true){
	      if(objform.totalAmt.value=="null")
	       objform.totalAmt.value=eval(0);
	      objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[j]);
	      //Added CR-0145
	      if(objform.totalPaymentDiscount.value=="null")
		       objform.totalPaymentDiscount.value=eval(0);
		      objform.totalPaymentDiscount.value = eval(objform.totalPaymentDiscount.value) + eval(discountedAmount[j]);
	      if(objform.totalPayableDiscount.value=="null")
		       objform.totalPayableDiscount.value=eval(0);
		      objform.totalPayableDiscount.value = eval(objform.totalPayableDiscount.value) + eval(payableAmount[j]);
	      if(document.getElementById("convertedTotalAmt").value=="")
	    	  document.getElementById("convertedTotalAmt").value=eval(0);
	     
	      if(claimTypeDesc == "Member"){
          		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	    		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
	      }
	          else if(claimTypeDesc == "Network"){
              		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	        		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
	          }
	 
	      
	      if(document.getElementById("totlAmountinConvert").value=="")
	    	  document.getElementById("totlAmountinConvert").value=eval(0);
	      document.getElementById("totlAmountinConvert").value=eval(document.getElementById("totlAmountinConvert").value) + eval(claimsAmt[j]);
	     // alert("3:::::"+ document.getElementById("totlAmountinConvert").value);

	     }
	    }
	    chkoptCount = chkoptCount-1;
	}else{
		 chkoptCount = chkoptCount-1;
	     for(j=0;j<chkoptCount;j++){
	     if(objform.chkopt.checked == true){
	      if(objform.totalAmt.value=="null")
	       objform.totalAmt.value=eval(0);
	      objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[j]);
	      //Added For CR-0145
	      if(objform.totalPaymentDiscount.value=="null")
		       objform.totalPaymentDiscount.value=eval(0);
		      objform.totalPaymentDiscount.value = eval(objform.totalPaymentDiscount.value) + eval(discountedAmount[j]);
	      if(objform.totalPayableDiscount.value=="null")
		       objform.totalPayableDiscount.value=eval(0);
		      objform.totalPayableDiscount.value = eval(objform.totalPayableDiscount.value) + eval(payableAmount[j]);
	      
	      if(document.getElementById("convertedTotalAmt").value=="")
	    	  document.getElementById("convertedTotalAmt").value=eval(0);
	      if(claimTypeDesc == "Member"){
          		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	    		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
	      }
	          else if(claimTypeDesc == "Network"){
              		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	        		  document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
	          }

	      
	      if(document.getElementById("totlAmountinConvert").value=="")
	    	  document.getElementById("totlAmountinConvert").value=eval(0);
	      document.getElementById("totlAmountinConvert").value=eval(document.getElementById("totlAmountinConvert").value) + eval(claimsAmt[j]);
	  //    alert("4:::::"+ document.getElementById("totlAmountinConvert").value);

	     }
	    }
	}
	    objform.availBalance.value = eval(objform.avblFloatBalance.value - objform.totalAmt.value)
	   }

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
	            
	          //TO Display Converted Total Amount Currency Format Based on selection S T A R T S
	            if(chkedCount>1){
	            	if(sFlag && !bFlag && !oFlag)
	            		document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
	            	else{
	        		  if(bFlag && oFlag)//QAR and OTHERS
	        			  document.getElementById("incuredCurencyFormatNew").value	=	"ANY";
	        		  else if(bFlag && !oFlag)//ONLY QAR
	        			  document.getElementById("incuredCurencyFormatNew").value	=	"QAR";
	        		  else if(!bFlag && oFlag && !sFlag)
	        			  document.getElementById("incuredCurencyFormatNew").value	=	"OTQ";
	        		  else if(!bFlag && oFlag && sFlag)
	        			  document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
	        		  else
	        			  document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
	        	  }
	            }else if(chkedCount==1)//count >1
	        		  document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
        		  else
	        		  document.getElementById("incuredCurencyFormatNew").value	=	"";
	          //TO Display Converted Total Amount Currency Format Based on selection E N D S
	document.getElementById("noofclaimssettlementnum").value	=	chkedCount;
	var checkedObj=document.getElementsByName("chkopt");
	for(var i=0;i<checkedObj.length;i++){
		if(checkedObj[i].checked == true){
			count=count+1;
		}	
	}
	
	if(count>900){
		obj.checked=false;
		alert("Sorry you can't select more than 900");
	}
}

	
	function onLogSearch()
	{
		 trimForm(document.forms[1]);
			
		 if(document.forms[1].startDate.value=="")
			{
				alert("Please enter the Start Date ");
				document.forms[1].elements[i].focus();
				return false;
			}
			else if(document.forms[1].endDate.value=="")
			{
				alert("Please enter the End Date ");
				document.forms[1].elements[i].focus();
				return false;
			}
			else if(compareDates("startDate","Start Date","endDate","End Date","greater than")==false)
			{
				document.forms[1].endDate.value="";
			    return false;
			}
			
	       if(!isDate(document.forms[1].startDate,"Start Date"))
		   	{
		   		document.forms[1].startDate.focus();
		   		return false;
		   	}
		   	if(!isDate(document.forms[1].endDate,"End Date"))
		   	{
		   		document.forms[1].endDate.focus();
		   		return false;
		   	}
		    if(!compareDates('startDate','Start Date','endDate','End Date','greater than'))
		    {
		    	return false;
		    }//end of if(!compareDates('sReportFrom','Report From','sReportTo','Report To','greater than'))

		 //   document.forms[1].mode.value="doLogDetailsExcelUploads";
		    document.forms[1].action = "/ClaimsSearchAction.do?mode=doLogDetailsExcelUploads&Flag=PA";
		    document.forms[1].submit();
	}
	
	function selectAll( bChkd, objform )
	{
	    var chkedCount=0;
	    var k=0;
	    var fundslimit = 0;
	    objform.totalAmt.value = eval(0);
	    objform.availBalance.value=eval(0);
	    document.getElementById("totlAmountinConvert").value=eval(0);
	    document.getElementById("convertedTotalAmt").value=eval(0);
	    document.getElementById("convertedTotalUSDAmt").value=eval(0);
	    document.getElementById("totalPaymentDiscount").value=eval(0);
	    document.getElementById("totalPayableDiscount").value=eval(0);
	    var tempCurr	=	"";
	    var tempCurrStr	=	"";
	    var tempCurrStrArr	=	"";
	    var bFlag = false;
	    var oFlag = false;
	    var sFlag = false;
	    var countSameCurr=0;
        var countMultCurr=0;
	    var incuredCurencyFormat=document.getElementById("incuredCurencyFormat").value;
	        for(i=0;i<objform.length;i++)
	        {	
	                if(objform.elements[i].name == "chkopt" && objform.elements[i].disabled==false)
	                {
//	                        objform.elements[i].checked = bChkd;
	                        chkedCount=eval(chkedCount)+1;
	                }//end of if(objform.elements[i].name == "chkopt")
	        }//end of for
	        if(chkedCount<900){
	        	for(i=0;i<objform.length;i++)
		        {	
		                if(objform.elements[i].name == "chkopt" && objform.elements[i].disabled==false)
		                {
		                        objform.elements[i].checked = bChkd;
//		                        chkedCount=eval(chkedCount)+1;
		                }//end of if(objform.elements[i].name == "chkopt")
		        }//end of for
	        }else{
	        	alert("Sorry, You Can't select more than 900");
	        	objform.chkAll.checked=false;
	        	objform.chkopt[j].checked == false;
	        	for(i=0;i<objform.length;i++)
		        {	
		                        objform.chkopt[i].checked = false;
		               //end of if(objform.elements[i].name == "chkopt")
		        }//end of for
	        }
	        
	        
	        if(objform.chkopt != null)
	        {
	                if(objform.chkopt.length)
	                {
	                        for(j=0;j<objform.chkopt.length;j++)
	                        {
	                                if(objform.chkopt[j].checked == true)
	                                {
	                                        if(objform.totalAmt.value=="null")
	                                                objform.totalAmt.value=eval(0);
	                                        objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[j]);
	                                        objform.availBalance.value = eval(objform.avblFloatBalance.value - objform.totalAmt.value);
	                                        
	                                        if(document.getElementById("convertedTotalAmt").value=="")
	                                      	  document.getElementById("convertedTotalAmt").value=eval(0);
	                                        if(document.getElementById("convertedTotalUSDAmt").value=="")
		                                      	  document.getElementById("convertedTotalUSDAmt").value=eval(0);
	                                        if(claimTypeDesc == "Member"){
	                                        		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	                                        		document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
	                                            
	                                        }else if(claimTypeDesc == "Network"){
	                                        		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
	                                        		document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
	                                                
	                                        }

	                                        if(document.getElementById("totlAmountinConvert").value=="")
	                                      	  document.getElementById("totlAmountinConvert").value=eval(0);
	                                        document.getElementById("totlAmountinConvert").value=eval(document.getElementById("totlAmountinConvert").value) + eval(convertedClaimsAmt[j]);

	                                        if(document.getElementById("totalPaymentDiscount").value=="")
	                                        	  document.getElementById("totalPaymentDiscount").value=eval(0);
	                                          document.getElementById("totalPaymentDiscount").value=eval(document.getElementById("totalPaymentDiscount").value) + eval(discountedAmount[j]);
	                                          
	                                          if(document.getElementById("totalPayableDiscount").value=="")
	                                          	  document.getElementById("totalPayableDiscount").value=eval(0);
	                                            document.getElementById("totalPayableDiscount").value=eval(document.getElementById("totalPayableDiscount").value) + eval(payableAmount[j]);

	                                        
	                                      //TO Display Converted Total Amount Currency Format Based on selection S T A R T S
	                                        if(incurredCurr[j]!="-" && incurredCurr[j]!=null && incurredCurr[j]!="null")
	                                      	  tempCurr	=	incurredCurr[j];
	                                  	if(tempCurrStr!="")
	                                  	{

	                                  		tempCurrStr	=	tempCurrStr.substring(0,tempCurrStr.length-1);
	                                  		tempCurrStrArr	=	tempCurrStr.split(",");
	                                  		for(var k=0;k<tempCurrStrArr.length;k++)
	                                  		{
	                                  			if(tempCurr==tempCurrStrArr[k])
	                                  				countSameCurr	=	countSameCurr+1;
	                                  			else
	                                  				countMultCurr	=	countMultCurr+1;
	                                  		}
	                                  		if(countSameCurr>0 && countMultCurr>0)
	                                  			sFlag	=	false;
	                                  		else if(countSameCurr>0 && countMultCurr==0)
	                                  			sFlag	=	true;
	                                  		else if(countSameCurr==0 && countMultCurr>0)
	                                  			sFlag	=	false;
	                                  		else if(countSameCurr==0 && countMultCurr==0)
	                                  			sFlag	=	false;
	                                  	}
	                                      	  
	                                      	  tempCurrStr	=	tempCurrStr.concat(tempCurr+",");
	                                            if(tempCurr=="QAR")
	                                          	  bFlag	=	true;
	                                            else
	                                          	  oFlag	=	true;
	                                              //TO Display Converted Total Amount Currency Format Based on selection E N D S 
	                                                
	                                }//end of if
	                                else{
	                                	chkedCount=eval(chkedCount)-1;
	                                }
	                        }// end of for
	                        
	                        if(eval(objform.avblFloatBalance.value) < eval(objform.totalAmt.value))
	                        {
	                                alert("Insufficient funds");
	                                objform.totalAmt.value = 0;
	                                objform.availBalance.value = 0;
	                                document.getElementById("convertedTotalAmt").value = eval(0);
	                                document.getElementById("convertedTotalUSDAmt").value = eval(0);
	                                document.getElementById("totlAmountinConvert").value = eval(0);
	                                document.getElementById("totalPaymentDiscount").value = eval(0);
	                                document.getElementById("totalPayableDiscount").value = eval(0);
	                                for(j=0;j<objform.chkopt.length;j++)
	                                {
	                                        if(objform.chkopt[j].checked == true)
	                                        {
	                                                objform.chkopt[j].checked = false;
	                                                chkedCount=eval(chkedCount)-1;
	                                        }//end of if(objform.chkopt[j].checked == true)
	                                }//end of for
	                                objform.chkAll.checked = false;
	                        }//end of if(eval(objform.avblFloatBalance.value) < eval(objform.totalAmt.value))
	                }//end of if(objform.chkopt.length)
	                else
	                {
	                        if(objform.chkopt.checked == true)
	                        {tempCurr	=	incurredCurr[0];
	                                if(objform.totalAmt.value=="null")
	                                        objform.totalAmt.value=eval(0);
	                                        objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[0]);
	                                        
	                                        if(document.getElementById("convertedTotalAmt").value=="")
	                                        	  document.getElementById("convertedTotalAmt").value=eval(0);
	                                        if(document.getElementById("convertedTotalUSDAmt").value=="")
	                                        	  document.getElementById("convertedTotalUSDAmt").value=eval(0);
	                                        if(claimTypeDesc == "Member"){
	                                        		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
	                                        		document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[0]);
	                                            
	                                        }else if(claimTypeDesc == "Network"){
	                                        		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
	                                        		document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[0]);
	                                                
	                                        }

	                                        if(document.getElementById("totlAmountinConvert").value=="")
	                                          	  document.getElementById("totlAmountinConvert").value=eval(0);
	                                        document.getElementById("totlAmountinConvert").value=eval(document.getElementById("totlAmountinConvert").value) + eval(convertedClaimsAmt[0]);

	                                        if(document.getElementById("totalPaymentDiscount").value=="")
	                                        	  document.getElementById("totalPaymentDiscount").value=eval(0);
	                                      document.getElementById("totalPaymentDiscount").value=eval(document.getElementById("totalPaymentDiscount").value) + eval(discountedAmount[0]);
	                                      
	                                      if(document.getElementById("totalPayableDiscount").value=="")
	                                      	  document.getElementById("totalPayableDiscount").value=eval(0);
	                                    document.getElementById("totalPayableDiscount").value=eval(document.getElementById("totalPayableDiscount").value) + eval(payableAmount[0]);

	                                        
	                                        if(eval(objform.avblFloatBalance.value) >= eval(objform.totalAmt.value))
	                             {
	                                        objform.availBalance.value = eval(objform.avblFloatBalance.value - objform.totalAmt.value);

	                                 }
	                                else
	                                {
	                                        objform.availBalance.value = eval(objform.avblFloatBalance.value - objform.totalAmt.value);

	                                        fundslimit = -1;
	                                }//end of else
	                                if(fundslimit == -1)
	                                {
	                                        alert("Insufficient funds");
	                                        objform.totalAmt.value = 0;
	                                        objform.availBalance.value = 0;
	                                        objform.chkopt.checked = false;
	                                        objform.chkAll.checked = false;
	                                        document.getElementById("convertedTotalAmt").value = eval(0);
	                                        document.getElementById("convertedTotalUSDAmt").value = eval(0);
	                                        document.getElementById("totlAmountinConvert").value = eval(0);
	                                        document.getElementById("totalPaymentDiscount").value = eval(0);
	                                        document.getElementById("totalPayableDiscount").value = eval(0);
	                                }//end of if(fundslimit == -1)
	                        }//end of if
	                }// end of else
	        }//end of if(objform.chkopt != null)
	      //TO Display Converted Total Amount Currency Format Based on selection S T A R T S
	        if(chkedCount>1){
	        	if(sFlag && !bFlag && !oFlag)
	        		document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
	        	else{
	    		  if(bFlag && oFlag)//QAR and OTHERS
	    			  document.getElementById("incuredCurencyFormatNew").value	=	"ANY";
	    		  else if(bFlag && !oFlag)//ONLY QAR
	    			  document.getElementById("incuredCurencyFormatNew").value	=	"QAR";
	    		  else if(!bFlag && oFlag && !sFlag)
	    			  document.getElementById("incuredCurencyFormatNew").value	=	"OTQ";
	    		  else if(!bFlag && oFlag && sFlag)
	    			  document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
	    		  else
	    			  document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
	    	  }
	        }else if(chkedCount==1)//count >1
      		  document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
  		  else
      		  document.getElementById("incuredCurencyFormatNew").value	=	"";
	      //TO Display Converted Total Amount Currency Format Based on selection E N D S
	        
	        document.getElementById("noofclaimssettlementnum").value	=	chkedCount;

	}//end of function selectAll( bChkd, objform )
	
	function onBankChanged(){
		var spaymentTransIn=document.forms[1].spaymentTransIn.value;
		var banksName=document.forms[1].banksName.value;
		 var select = document.getElementById('reportFormatId');
         select.innerHTML = "";        
	             if( banksName=='AL KHALIJ COMMERCIAL BANK'&&spaymentTransIn=='QAR'){
	                 var option = document.createElement("option");
	                 option.text = "Al khaliji Local Format";
	                 option.value = "Al khaliji Local Format";
	                 select.add(option);
	             }else if(banksName=='AL KHALIJ COMMERCIAL BANK'&&spaymentTransIn!='QAR'){
	                 var option = document.createElement("option");
	                 option.text = "Al khaliji Foreign Format";
	                 option.value = "Al khaliji Foreign Format";
	                 select.add(option);
	             }
	             if(banksName=='QATAR NATIONAL BANK'){
	            	 var option1 = document.createElement("option");
	                 option1.text = "QNB Format";
	                 option1.value = "QNB Format";
	                 select.add(option1);
	             }
	             if(banksName=='THE COMMERCIAL BANK'){
	        	 var option2 = document.createElement("option");
                 option2.text = "CBQ Format";
                 option2.value = "Default Format";
                 select.add(option2);
	         }
	             if(!(banksName=='AL KHALIJ COMMERCIAL BANK'||banksName=='QATAR NATIONAL BANK'||banksName=='THE COMMERCIAL BANK')){
		        	 alert("Selected bank is not preferable to generate");
		         }
	}//end of onPaymentTypeChanged()
	
	function onPaymentTypeChanged(){
		var spaymentTransIn=document.forms[1].spaymentTransIn.value;
		var banksName=document.forms[1].banksName.value;
		 var select = document.getElementById('reportFormatId');
         select.innerHTML = "";      
	             if( banksName=='AL KHALIJ COMMERCIAL BANK'&&spaymentTransIn=='QAR'){
	                 var option = document.createElement("option");
	                 option.text = "Al khaliji Local Format";
	                 option.value = "Al khaliji Local Format";
	                 select.add(option);
	             }else if(banksName=='AL KHALIJ COMMERCIAL BANK'&&spaymentTransIn!='QAR'){
	                 var option = document.createElement("option");
	                 option.text = "Al khaliji Foreign Format";
	                 option.value = "Al khaliji Foreign Format";
	                 select.add(option);
	             }
	             if(banksName=='QATAR NATIONAL BANK'&&(spaymentTransIn=='QAR'||spaymentTransIn=='USD'||spaymentTransIn=='GBP'||spaymentTransIn=='EUR')){
	            	 var option1 = document.createElement("option");
	                 option1.text = "QNB Format";
	                 option1.value = "QNB Format";
	                 select.add(option1);
	             }
	             if(banksName=='THE COMMERCIAL BANK'&&(spaymentTransIn=='QAR'||spaymentTransIn=='USD'||spaymentTransIn=='GBP'||spaymentTransIn=='EUR')){
	        	 var option2 = document.createElement("option");
                 option2.text = "CBQ Format";
                 option2.value = "Default Format";
                 select.add(option2);
	         }
	             if(!(banksName=='AL KHALIJ COMMERCIAL BANK'||banksName=='QATAR NATIONAL BANK'||banksName=='THE COMMERCIAL BANK')){
		        	 alert("Selected bank is not preferable to generate");
		         }
	}//end of onPaymentTypeChanged()