
  function edit(rownum)
  {  		 
	  var hospOrPtr =   document.forms[1].switchHospOrPtr.value;
	  if(document.forms[1].switchHospOrPtr.value === "HOS"){
		  
	document.forms[1].rownum.value=rownum;
  	document.forms[1].mode.value="doViewHospBankAccountReviewDetails";
  	document.forms[1].action="/CustomerBankDetailsAccountList.do";
  	document.forms[1].submit();
	  }
	  	if(document.forms[1].switchHospOrPtr.value === "PTR"){
	  		
	  		document.forms[1].rownum.value=rownum;
	  	  	document.forms[1].mode.value="doViewPtnrBankAccountReviewDetails";
	  	  	document.forms[1].action="/CustomerBankDetailsAccountList.do?hospOrPtr="+hospOrPtr;
	  	  	document.forms[1].submit(); 		
	  	}
	
	  	if(document.forms[1].switchHospOrPtr.value === "MEM"){
	  		
	  		document.forms[1].rownum.value=rownum;
	  	  	document.forms[1].mode.value="doViewMemberBankAccountReviewDetails";
	  	  	document.forms[1].action="/CustomerBankDetailsAccountList.do";
	  	  	document.forms[1].submit();
	  		  }
	  	
	  	
	  	
  }//end of edit
  

  
  function onSwitch(){
	  
	 
	  if(document.forms[1].switchHospOrPtr.value === "HOS"){
			document.forms[1].mode.value="doViewHospReviewList";
		  	document.forms[1].action="/CustomerBankDetailsAccountList.do";
		  	document.forms[1].submit();
		  
	  }
	  	if(document.forms[1].switchHospOrPtr.value === "PTR"){
		  
	  		document.forms[1].mode.value="doViewPtnrReviewList";
	  	  	document.forms[1].action="/CustomerBankDetailsAccountList.do";
	  	  	document.forms[1].submit();	  
	  }
		if(document.forms[1].switchHospOrPtr.value === "MEM"){
			  
	  		document.forms[1].mode.value="doViewMemberReviewList";
	  	  	document.forms[1].action="/CustomerBankDetailsAccountList.do";
	  	  	document.forms[1].submit();	  
	  }
  }
 
  function onChangeState(focusid)
  {
	  var hospOrPtr =   document.forms[1].switchHospOrPtr.value;	
	  document.forms[1].mode.value="doChangeState";
      document.forms[1].action="/CustomerBankDetailsAccountList.do?hospOrPtr="+hospOrPtr;
      document.forms[1].submit();
  }//end of onChangeState()
  

  function onUserSubmit() {
	if (!JS_SecondSubmit) {		
	  if(document.forms[1].switchHospOrPtr.value === "HOSP"){	 
			  trimForm(document.forms[1]);
				document.forms[1].mode.value = "doReviewSave";
				document.forms[1].action = "/CustomerBankDetailsAccountSave.do";
				JS_SecondSubmit = true;
				document.forms[1].submit();
			
				  }
				  	if(document.forms[1].switchHospOrPtr.value === "PTNR"){
				  		
				  		trimForm(document.forms[1]);
						document.forms[1].mode.value = "doPartnerReviewSave";
						document.forms[1].action = "/PartnerCustomerBankDetailsAccountSave.do";
						JS_SecondSubmit = true;
						document.forms[1].submit();
				  	
				  	}
				  	
		
	}// end of if(!JS_SecondSubmit)
}// end of onUserSubmit()
  
  
 /* function onUserSave() {
		if (!JS_SecondSubmit) {
			trimForm(document.forms[1]);
			document.forms[1].mode.value = "doPartnerReviewSave";
			document.forms[1].action = "/CustomerBankDetailsAccountSave.do";
			JS_SecondSubmit = true;
			document.forms[1].submit();
		}// end of if(!JS_SecondSubmit)
	}// end of onUserSubmit()
  */
  
  function onCloseReview()
  {
      	document.forms[1].mode.value="doViewHospReviewList";
      	document.forms[1].action="/CustomerBankDetailsAccountList.do";
      	document.forms[1].submit();
  }//end of onChangeState()
  
  function onClosePatReview(){
	  	document.forms[1].mode.value="doClosePartner";
	    document.forms[1].action = "/ChangeIfscGeneralActionTest.do";
		document.forms[1].submit();
	}

//this function is called on click of the link in grid to display the Files Upload
  function showFiles(obj)
  {
	  var openPage = "/ReportsAction.do?mode=doViewCommonForAll&module=mouUploads&fileName="+obj;
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
  }//end of edit(rownum)
  
  
  
  
  
  function toggle(sortid)
  {
  	document.forms[1].reset();
      document.forms[1].mode.value="doViewHospReviewList";
      document.forms[1].sortId.value=sortid;
      document.forms[1].action = "/CustomerBankDetailsAccountList.do";
      document.forms[1].submit();
  }//end of toggle(sortid)
  //function to display the selected page
  function pageIndex(pagenumber)
  {
  	document.forms[1].reset();
      document.forms[1].mode.value="doViewHospReviewList";
      document.forms[1].pageId.value=pagenumber;
      document.forms[1].action = "/CustomerBankDetailsAccountList.do";
      document.forms[1].submit();
  }//end of pageIndex(pagenumber)
  //function to display previous set of pages
  function PressBackWard()
  {
  	document.forms[1].reset();
      document.forms[1].mode.value ="doBackwardAccValidation";
      document.forms[1].action = "/CustomerBankDetailsAccountList.do";
      document.forms[1].submit();
  }//end of PressBackWard()
  //function to display next set of pages
  function PressForward()
  {
  	document.forms[1].reset();
      document.forms[1].mode.value ="doForwardAccValidation";
      document.forms[1].action = "/CustomerBankDetailsAccountList.do";
      document.forms[1].submit();
  }//end of PressForward()
  //function to display pages based on search criteria