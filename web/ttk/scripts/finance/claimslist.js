//javascript used in claimslist.jsp of Finance flow

//function to sort based on the link selected
	function toggle(sortid)
	{
		document.forms[1].reset();
	    document.forms[1].mode.value="doSearch";
	    document.forms[1].sortId.value=sortid;
	    document.forms[1].action = "/ClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of toggle(sortid)

	//function to display the selected page
	function pageIndex(pagenumber)
	{
	    document.forms[1].reset();
	    document.forms[1].mode.value="doSearch";
	    document.forms[1].pageId.value=pagenumber;
	    document.forms[1].action = "/ClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of pageIndex(pagenumber)

	//function to display previous set of pages
	function PressBackWard()
	{
		document.forms[1].reset();
		document.forms[1].mode.value ="doBackward";
	    document.forms[1].action = "/ClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of PressBackWard()

	//function to display next set of pages
	function PressForward()
	{
		document.forms[1].reset();
		document.forms[1].mode.value ="doForward";
	    document.forms[1].action = "/ClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of PressForward()

	//functin to search
	function onSearch(element)
	{
	  if(!JS_SecondSubmit)
      {
		trimForm(document.forms[1]);
		var incuredCurencyFormat=document.getElementById("incuredCurencyFormat").value;
		if(isValidated()==true)
		{
			if(incuredCurencyFormat!=""&&incuredCurencyFormat!=null)
				{
			document.forms[1].mode.value = "doSearch";
	    	document.forms[1].action = "/ClaimsSearchAction.do";
	    	JS_SecondSubmit=true;
	    	element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
	    	document.forms[1].remarks.value="";
	    	document.forms[1].startNo.value="";
	    	document.forms[1].submit();
				}
			/*else
			{
			alert("Please Select Currency Format");
			}*/
	    }//end of if(isValidated()==true)
		
	  }//end of if(!JS_SecondSubmit)
	}//end of onSearch()

	//function to call edit screen
	function edit(rownum)
	{
	    document.forms[1].mode.value="doViewCheque";
	    document.forms[1].rownum.value=rownum;
	    document.forms[1].child.value="Cheque Printing Details";
	    document.forms[1].action = "/ClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of edit(rownum)
	
	//function to enable or disable the fields
	function enableField(obj)
	{
			if(obj.value == "PMM"){
			document.forms[1].startNo.disabled=false;
			document.forms[1].startNo.className="textBox textBoxMedium";
			document.forms[1].remarks.disabled=true;
			document.forms[1].remarks.className="textBox textAreaMediumht textAreaMediumhtDisabled";
			document.forms[1].remarks.value="";
		}//end of if(obj.value == "PMM")
		else{
			if(obj.value == "EFT"){
				document.forms[1].remarks.disabled=false;
			    document.forms[1].remarks.className="textBox textAreaMediumht";
			}//end of if(obj.value == "EFT")
			else{
				document.forms[1].remarks.disabled=true;
			    document.forms[1].remarks.className="textBox textAreaMediumht textAreaMediumhtDisabled";
			    document.forms[1].remarks.value="";
			}//end of else
			document.forms[1].startNo.disabled=true;
			document.forms[1].startNo.className="textBox textBoxMedium textBoxDisabled";
			document.forms[1].startNo.value="";
		}//end of else
	}//end of function enableField(obj)
	
	//function to validate fields for search
	function isValidated()
	{
		//checks if DV Received Date is entered
		if(document.forms[1].dvReceivedDate.value.length!=0)
		{
			if(isDate(document.forms[1].dvReceivedDate,"Approved Date")==false)
			{
				return false;
				document.forms[1].dvReceivedDate.focus();
			}//end of if(isDate(document.forms[1].dvReceivedDate,"DV Received Date")==false)
		}// end of if(document.forms[1].dvReceivedDate.value.length!=0)
		if(document.forms[1].receivedDate.value.length!=0)
		{
			if(isDate(document.forms[1].receivedDate,"Claim Received Date")==false)
			{
				return false;
				document.forms[1].receivedDate.focus();
			}//end of if(isDate(document.forms[1].receivedDate,"Claim Received")==false)
		}// end of if(document.forms[1].receivedDate.value.length!=0)
		if(document.forms[1].floatType.value=='FTD' && document.forms[1].debitSeqID.value=='')
		{
			alert('Please select Debit Note');
			return false;
		}//end of if(document.forms[1].floatType.value=='FTD' && document.forms[1].debitSeqID.value=='')
		return true;
	}//end of function isValidated()

	//function to check the Cheque Nbr is entered or not
	function ChkStartNum()
	{
		trimForm(document.forms[1]);
		if(document.forms[1].paymethod.value=='PMM')
		{
			if(document.forms[1].startNo.value.length >= 1)
			{
				return false;
			}
			else
			{
				alert('Please enter the cheque number');
				return true;
			}
		}
		return false;
	}
	
	//function to check the Remarks is entered or not
	function EFTRemarks()
	{
		trimForm(document.forms[1]);
		if(document.forms[1].remarks.value =="")
		{
			alert('Please enter Remarks');
			return true;
		}//end of if(document.forms[1].remarks.value =="")
		else
		{
			return false;
		}//end of else
	}//end of function EFTRemarks()
	
	//function to Print the Cheque
	function onPrint()
	{
		   if(!mChkboxValidation(document.forms[1]))
    	{
			if((!ChkStartNum()))
			{
				if(isNumber(document.forms[1].startNo,"Starting Cheque No."))
				{
					if(document.forms[1].paymethod.value=='EFT'){
						if((!EFTRemarks())){
							var msg = confirm("Are you sure you want to Print the Cheque for the selected Records");
							if(msg)
							{
								
								trimForm(document.forms[1]);
								
								if(!JS_SecondSubmit)
								{
									document.forms[1].mode.value="doPrintCheque";
									document.forms[1].action = "/ClaimsSearchAction.do";
									JS_SecondSubmit=true;
									document.forms[1].submit();
								}//end of if(!JS_SecondSubmit)
							}//end of if(msg)
						}//end of if((!EFTRemarks()))
					}//end of if(document.forms[1].paymethod.value=='EFT')
					else{
						var msg = confirm("Are you sure you want to Print the Cheque for the selected Records");
						if(msg)
						{
							
								trimForm(document.forms[1]);
							if(!JS_SecondSubmit)
								{
									document.forms[1].mode.value="doPrintCheque";
									document.forms[1].action = "/ClaimsSearchAction.do";
									JS_SecondSubmit=true;
									document.forms[1].submit();
								}//end of if(!JS_SecondSubmit)
						}//end of if(msg)
					 }//end of else
				}//end of if(isNumber(document.forms[1].startNo,"Starting Cheque No."))
			}//end of if((!ChkStartNum()))
		}//end of if(!mChkboxValidation(document.forms[1]))
	}//end of onPrint()

//this function will select or deselect all the check boxex available in the form based on the boolean value passed to the method

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
        for(i=0;i<objform.length;i++)
        {	
                if(objform.elements[i].name == "chkopt" && objform.elements[i].disabled==false)
                {
                        objform.elements[i].checked = bChkd;
//                        chkedCount=eval(chkedCount)+1;   // commentted by govind
                       
                }//end of if(objform.elements[i].name == "chkopt")
        }//end of for
        if(objform.chkopt != null)
        {
                if(objform.chkopt.length)
                {
                        for(j=0;j<objform.chkopt.length;j++)
                        {
                                if(objform.chkopt[j].checked == true)
                                {
                                	chkedCount=eval(chkedCount)+1;  // added by govind
                                        if(objform.totalAmt.value=="null")
                                                objform.totalAmt.value=eval(0);
                                        objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[j]);
                                        objform.availBalance.value = eval(objform.avblFloatBalance.value - objform.totalAmt.value);
                                        
                                        if(document.getElementById("convertedTotalAmt").value=="")
                                      	  document.getElementById("convertedTotalAmt").value=eval(0);
                                        if(claimTypeDesc == "Member"){
                                            document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
                                    		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
                                        }
                                            else if(claimTypeDesc == "Network"){
                                                document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
                                        		document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
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
                                /*else{
                                	chkedCount=eval(chkedCount)-1;    // else part removed by Govind
                                }*/
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
                        {
                        	 chkedCount=eval(chkedCount)+1;   // commentted by 21-11-2018
                        	tempCurr	=	incurredCurr[0];
                                if(objform.totalAmt.value=="null")
                                        objform.totalAmt.value=eval(0);
                                        objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[0]);
                                        
                                        if(document.getElementById("convertedTotalAmt").value=="")
                                        	  document.getElementById("convertedTotalAmt").value=eval(0);
                                        if(claimTypeDesc == "Member"){
                                            document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[0]);
                                            document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
                                        }
                                            else if(claimTypeDesc == "Network"){
                                                document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[0]);
                                                document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
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

//this function will select or deselect all the check boxex available in the form based on the boolean value passed to the method

function toCheckBox(obj, bChkd, objform )
{
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
        if(bChkd == false)
        {
          objform.chkAll.checked =false;
        }//end of if(bChkd == false)
            var bFlag = false;
            var oFlag = false;
            var sFlag = false;
            var countSameCurr=0;
            var countMultCurr=0;
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
      if(claimTypeDesc == "Member"){
          document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
          document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
      }
          else if(claimTypeDesc == "Network"){
              document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
              document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
          }

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
          
      if(document.getElementById("totlAmountinConvert").value=="")
      document.getElementById("totlAmountinConvert").value=eval(0);
      document.getElementById("totlAmountinConvert").value=eval(document.getElementById("totlAmountinConvert").value) + eval(claimsAmt[j]);
   //   alert("1:::::"+ document.getElementById("totlAmountinConvert").value);

     }// end of if(objform.chkopt[j].checked == true)
    }// end of for

   }else{
     chkoptCount=1;
     if(objform.chkopt.checked == true){
    	 tempCurr	=	incurredCurr[0];
      chkedCount=eval(chkedCount)+1;
      if(objform.totalAmt.value=="null")
        objform.totalAmt.value=eval(0);
      objform.totalAmt.value = eval(objform.totalAmt.value) + eval(claimsAmt[0]);
     
      if(document.getElementById("convertedTotalAmt").value=="")
    	  document.getElementById("convertedTotalAmt").value=eval(0);
      if(claimTypeDesc == "Member"){
          document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[0]);
          document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
      }
          else if(claimTypeDesc == "Network"){
              document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[0]);
              document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[0]);
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
    document.getElementById("convertedTotalUSDAmt").value = eval(0);
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
      if(document.getElementById("convertedTotalUSDAmt").value=="")
    	  document.getElementById("convertedTotalUSDAmt").value=eval(0);
      
      if(claimTypeDesc == "Member"){
          document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
          document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
      }
          else if(claimTypeDesc == "Network"){
              document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
          document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
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
          document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(claimsAmt[j]);
          document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
      }
          else if(claimTypeDesc == "Network"){
              document.getElementById("convertedTotalAmt").value=eval(document.getElementById("convertedTotalAmt").value) + eval(convertedClaimsAmt[j]);
          document.getElementById("convertedTotalUSDAmt").value=eval(document.getElementById("convertedTotalUSDAmt").value) + eval(convertedUSDAmt[j]);
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
            }else//count >1
        		  document.getElementById("incuredCurencyFormatNew").value	=	tempCurr;
          //TO Display Converted Total Amount Currency Format Based on selection E N D S
            
            document.getElementById("noofclaimssettlementnum").value	=	chkedCount;
            
}

//function to call Debit screen
	function onSelectDebitNote()
	{
	    document.forms[1].mode.value="doSelectDebitNote";
	    document.forms[1].action = "/ClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of onSelectDebitNote()
	
	//function to clear debitnote
	function onClearDebit()
	{
	    document.forms[1].mode.value="doClearDebitNote";
	    document.forms[1].action = "/ClaimsSearchAction.do";
	    document.forms[1].submit();
	}//end of edit(rownum)
	

	function onUploadClaimSettlementNumber()
	{
	if(oncheckDebit())
		{
		if(!JS_SecondSubmit)
		 {
		    trimForm(document.forms[1]);
		   
		    var incuredCurencyFormat=document.getElementById("incuredCurencyFormat").value;
		    if(incuredCurencyFormat!=""&&incuredCurencyFormat!=null)
			{
		    document.forms[1].mode.value = "doPaymentUploadBatchDetail";
		    document.forms[1].action = "/ClaimsSearchAction.do";
			JS_SecondSubmit=true;
		    document.forms[1].submit();
			}
			/*else
			{
			alert("Please Select Currency Format");
			}*/
		 }//end of if(!JS_SecondSubmit)
		}
	}
	
	function oncheckDebit()
	{
		if(document.forms[1].floatType.value=='FTD' && document.forms[1].debitSeqID.value=='')
		{
			alert('Please select Debit Note');
			return false;
		}//end of if(document.forms[1].floatType.value=='FTD' && document.forms[1].debitSeqID.value=='')
	return true;
	}
	
	function showFinanceTemplate()
	{
		   document.forms[1].mode.value="doShowClaimFinanceStatusTemplate";
		    document.forms[1].action = "/ClaimsSearchAction.do";
		    document.forms[1].submit();
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
		    document.forms[1].action = "/ClaimsSearchAction.do?mode=doLogDetailsExcelUploads&Flag=P";
		    document.forms[1].submit();
	}
	
	function onDownloadPaymentUploadLog(){
		   document.forms[1].mode.value="doDownloadPaymentUploadLog";
		    document.forms[1].action = "/ClaimsSearchAction.do";
		    document.forms[1].submit();
	}
	
	function onClaimChanged()
	{
		if(!JS_SecondSubmit){
		document.forms[1].mode.value="doClaimChanged";
		document.forms[1].action="/ClaimsSearchAction.do";
		 JS_SecondSubmit=true;
		document.forms[1].submit();
		}
	}//end of onStatusChanged()
	
	function onPriorityChanged()
	{
		if(!JS_SecondSubmit){
		document.forms[1].mode.value="doPriorityChanged";
		document.forms[1].action="/ClaimsSearchAction.do";
		 JS_SecondSubmit=true;
		document.forms[1].submit();
		}
	}//end of onStatusChanged()
	
	