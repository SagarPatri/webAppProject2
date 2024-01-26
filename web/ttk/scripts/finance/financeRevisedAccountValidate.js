
  function onChangeState(focusid)
  {
      	document.forms[1].mode.value="doChangeState";
      	document.forms[1].action="/CustomerBankDetailsReviewAccountList.do";
      	document.forms[1].submit();
  }//end of onChangeState()
  
  function onUserSubmit() {
		if (!JS_SecondSubmit) {		
		  
			var reviewedYN=document.forms[1].reviewedYN.checked;
			if(reviewedYN=="" || reviewedYN=="false"){
				alert("Finance Reviewed Yes/No field should be checked");
				return;
			}
			
			
				  trimForm(document.forms[1]);
					document.forms[1].mode.value = "doRevisedReviewSave";
					document.forms[1].action = "/CustomerBankDetailsRevisedAccountSave.do";
				  /*document.forms[1].action = "/CustomerBankDetailsRevisedAccountSave.do?mode=doRevisedReviewSave&reviewedYN"+reviewedYN;*/
					
					JS_SecondSubmit = true;
					document.forms[1].submit();
				
					
			
		}// end of if(!JS_SecondSubmit)
	}// end of onUserSubmit()
	  
  function onCloseRevisedReview()
  {

      	document.forms[1].mode.value="doViewHospReviewList";
      	document.forms[1].action="/CustomerBankDetailsReviewAccountList.do";
      	document.forms[1].submit();
  }//end of onChangeState()
  

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
      document.forms[1].action = "/CustomerBankDetailsReviewAccountList.do";
      document.forms[1].submit();
  }//end of toggle(sortid)
  //function to display the selected page
  function pageIndex(pagenumber)
  {
      document.forms[1].reset();
      document.forms[1].mode.value="doViewHospReviewList";
      document.forms[1].pageId.value=pagenumber;
      document.forms[1].action = "/CustomerBankDetailsReviewAccountList.do";
      document.forms[1].submit();
  }//end of pageIndex(pagenumber)
  //function to display previous set of pages
  function PressBackWard()
  {
  	document.forms[1].reset();
      document.forms[1].mode.value ="doBackwardAccValidation";
      document.forms[1].action = "/CustomerBankDetailsReviewAccountList.do";
      document.forms[1].submit();
  }//end of PressBackWard()
  //function to display next set of pages
  function PressForward()
  {
  	document.forms[1].reset();
      document.forms[1].mode.value ="doForwardAccValidation";
      document.forms[1].action = "/CustomerBankDetailsReviewAccountList.do";
      document.forms[1].submit();
  }//end of PressForward()
  //function to display pages based on search criteria
  
  
  function edit(rownum)
  { 
	  var hospOrPtr =   document.forms[1].switchHospOrPtr.value;
	  
	  if(document.forms[1].switchHospOrPtr.value === "HOS"){
		  
		  document.forms[1].rownum.value=rownum;
		  	document.forms[1].mode.value="doViewHospBankReviewAccountReviewDetails";
		  	document.forms[1].action="/CustomerBankDetailsReviewAccountList.do";
		  	document.forms[1].submit();
			  }
	/*if(document.forms[1].switchHospOrPtr.value === "PTR"){
	  		
	  		document.forms[1].rownum.value=rownum;
	  	  	document.forms[1].mode.value="doViewPartnerBankReviewAccountReviewDetails";
	  	  	document.forms[1].action="/CustomerBankDetailsReviewAccountList.do?hospOrPtr="+hospOrPtr;
	  	  	document.forms[1].submit(); 		
	  	}*/
	
	  	if(document.forms[1].switchHospOrPtr.value === "MEM"){
	  		
	  		document.forms[1].rownum.value=rownum;
	  	  	document.forms[1].mode.value="doViewMemberBankReviewAccountReviewDetails";
	  	  	document.forms[1].action="/CustomerBankDetailsReviewAccountList.do";
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
  
  
  
  
  
  