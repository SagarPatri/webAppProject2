/*
		 * This java script is added for cr koc 1103
		 * added eft
		 */
//declare the Ids of the form fields seperated by comma which should not be focused when document is loaded
var JS_donotFocusIDs=["switchType"];
function Close()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doClose";
	document.forms[1].tab.value="Search";
	document.forms[1].action="/CustomerBankDetails.do";
	document.forms[1].submit();
}//end of Close()

function onSearch(element)
{
	 //alert("called");
  if(!JS_SecondSubmit)
 { 
	  //alert("called1");
	trimForm(document.forms[1]);
	//alert("called2");
    var searchFlag=false;
   var switchty=document.forms[1].switchType.value;
  // alert(switchty);
  
  
   
   // alert("after reading all var");
    if(switchty == "POLC")
    {
    	//alert("called end");
    	  var policynum=document.forms[1].sPolicyNumber.value;
    	    var enrollnum=document.forms[1].sEnrollNumber.value;
    	    var insurename=document.forms[1].sInsuredName.value;
    	    var ttkbranch=document.forms[1].sTtkBranch.value;
    	    var sQatarId=document.forms[1].sQatarId.value;
    	    
      //  alert("in switch end");
    if((policynum == "")&&(enrollnum == "") && (sQatarId == "") )
    {
       alert("Please enter Policy No. / AlKoot No. / Qatar ID");
       document.forms[1].sPolicyNumber.focus();
        return false;
    }
    if((policynum != "")&&(policynum.length < 3)) 
	{
    	//|| (policynum == "")) && (enrollnum != "") && (insurename != "")
		alert("Please enter atleast 3 characters of Policy No.");
		document.forms[1].sPolicyNumber.focus();
		return false;
	}
    if(insurename != "")
    {
    if((policynum == "") && (enrollnum == ""))
    {
    	alert("Please enter  Policy No. or AlKoot No.");
    	document.forms[1].sPolicyNumber.focus();
    	return false;
    }
    }
    }
    
 

    if(switchty == "CLAM")
       {
    	//alert("called enm");
       	var clmnmr=document.forms[1].sClaimNumber.value;
         //  alert("claim number"+clmnmr);
          var claimsettlmentnumber=document.forms[1].sClaimSettmentNumber.value;
         // alert("claim sett nmr"+claimsettlmentnumber);
          var claimentname=document.forms[1].sClaimentName.value;
          // alert("claiment name"+claimentname);
       	
       	if((clmnmr == "") && (claimsettlmentnumber == "")) 
           {
               alert("Please enter Claim No. / Claim Settlement No.");
               document.forms[1].sClaimNumber.focus();
               return false;
           } 
       	if(claimentname != "")
        {
        if((clmnmr == "") && (claimsettlmentnumber == ""))
        {
        	alert("Please enter  Cliam No. or claim Settelment No");
        	document.forms[1].sClaimNumber.focus();
        	return false;
        }
        }
       }//end clm
   
    if(switchty == "HOSP")
    {
 	   
    	var empnalNO=document.forms[1].sEmpanelmentNo.value;
     
       var hospitalname=document.forms[1].sHospitalName.value;
      
    	
    	if((empnalNO == "") && (hospitalname == "")) 
        {
            alert("Please enter atleast one search criteria");
            document.forms[1].sEmpanelmentNo.focus();
            return false;
        } 
    	
    }//end hosp
    
	//alert("alert5");
	document.forms[1].mode.value = "doSearch";
    document.forms[1].action = "/CustomerBankDetailsSearch.do";
    JS_SecondSubmit=true;
    element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
    document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()
  function edit(rownum)
  {
	  
	  var switchty=document.forms[1].switchType.value;
	  var from	=	document.forms[1].from.value;
  	if(document.forms[0].sublink.value=="Cust. Bank Details")
  	{
  		document.forms[1].tab.value="Policy Bank Details";
  	}
  	if(switchty =='POLC' || switchty == "CLAM" )
  	{
  	document.forms[1].rownum.value=rownum;
  	document.forms[1].mode.value="doViewBankAccount";
  	document.forms[1].action="/CustomerBankDetailsSearch.do";
  	document.forms[1].submit();
  	}
  	else if(switchty =='HOSP' )
  	{
  	document.forms[1].rownum.value=rownum;
  	if(from=="from")
		document.forms[1].tab.value="Account Validation";
	else
		document.forms[1].tab.value="Hospital Bank Details";
  	document.forms[1].mode.value="doViewHospBankAccount";
  	document.forms[1].action="/CustomerBankDetailsSearch.do?from="+from;
  	document.forms[1].submit();
  	}
  	else if(switchty =='EMBS' )
  	{
	document.forms[1].tab.value="Embassy Details";
  	document.forms[1].rownum.value=rownum;
  	document.forms[1].mode.value="doViewEmbassyDtls";
  	document.forms[1].action="/CustomerBankDetailsSearch.do";
  	document.forms[1].submit();
  	}
  	else if(switchty =='PTNR' )
  	{
	document.forms[1].tab.value="Partner Bank Details";
  	document.forms[1].rownum.value=rownum;
  	document.forms[1].mode.value="doViewPartnerBankDetails";
  	document.forms[1].action="/PartnerBankDetailsSearch.do?from="+from;
  	document.forms[1].submit();
  	}
  	
  }//end of edit
//function to be called onClick of third link in Html Grid
  function edit2(rownum)
  {
	  
	  	if(document.forms[0].sublink.value=="Cust. Bank Details")
	  	{
	  		document.forms[1].tab.value="Member Bank Details";
	  	}
	  	document.forms[1].rownum.value=rownum;
	  	document.forms[1].mode.value="doMemberAccount";
	  	document.forms[1].action="/CustomerBankDetailsSearch.do";
	  	document.forms[1].submit();
  }//end of edit(rownum)
  
  function edit3(rownum)
  {
	  	document.forms[1].tab.value="Embassy Details";
	  	document.forms[1].rownum.value=rownum;
	  	document.forms[1].mode.value="doViewEmbassyDtls";
	  	document.forms[1].action="/CustomerBankDetailsSearch.do";
	  	document.forms[1].submit();
  }//end of edit(rownum
  
  function onAddBankAccount()
  {
      document.forms[1].reset();
      document.forms[1].rownum.value = "";
      document.forms[1].mode.value = "doAdd";
      document.forms[1].tab.value="Bank Details";
      document.forms[1].action = "/BankAcctGeneralActionTest.do";
      document.forms[1].submit();
  }//end of ()
  function pageIndex(pagenumber)
  {
  	document.forms[1].reset();
      document.forms[1].mode.value="doSearch";
      document.forms[1].pageId.value=pagenumber;
      document.forms[1].action = "/CustomerBankDetailsSearch.do";
      document.forms[1].submit();
  }//end of pageIndex(pagenumber)
  function PressForward()
  {
  	document.forms[1].reset();
      document.forms[1].mode.value ="doForward";
      document.forms[1].action = "/CustomerBankDetailsSearch.do";
      document.forms[1].submit();
  }//end of PressForward()

  //function to display previous set of pages
  function PressBackWard()
  {
  	document.forms[1].reset();
      document.forms[1].mode.value ="doBackward";
      document.forms[1].action = "/CustomerBankDetailsSearch.do";
      document.forms[1].submit();
  }//end of PressBackWard()
  function toggle(sortid)
  {
  	document.forms[1].reset();
      document.forms[1].mode.value="doSearch";
      document.forms[1].sortId.value=sortid;
      document.forms[1].action = "/CustomerBankDetailsSearch.do";
      document.forms[1].submit();
  }//end of toggle(sortid)
  function onSwitch()
  {
  	document.forms[1].mode.value="doSwitchTo";
  	document.forms[1].action="/CustomerBankDetailsSearch.do";
  	document.forms[1].submit();
  }//end of onSwitch()
  
  function changePolicyType()
  {
  	document.forms[1].mode.value="doChangePolicyType";
  	document.forms[1].action="/CustomerBankDetailsSearch.do";
  	document.forms[1].submit();
  }//end of changeCallerFields()

//function to enable or disable the fields
	function enableField(obj)
	{
		if(obj.value == "COR")
		{
		document.forms[1].sGroupId.disabled=false;
		}
		else
		{
			document.forms[1].sGroupId.disabled=true;
			
		}
		
			    
			
	}//end of function enableField(obj)
	
	
	function copyToWebBoard()
	{
	    if(!mChkboxValidation(document.forms[1]))
	    {
		    document.forms[1].mode.value = "doCopyToWebBoard";
	   		document.forms[1].action = "/CustomerBankDetailsSearch.do";
		    document.forms[1].submit();
	    }//end of if(!mChkboxValidation(document.forms[1]))
	}//end of copyToWebBoard()

	function softcopyUpload()
	{
		
		var softCopyBulkAccountUpload	=	document.forms[1].softCopyBulkAccountUpload.value;
		var userSeqId	=	document.forms[1].userSeqId.value;
	 var param = { 'loginType' : 'Finance', 'userId': userSeqId ,'password' :'m'};
	 OpenWindowWithPost(softCopyBulkAccountUpload, "width=1000, height=600, left=100, top=100, resizable=yes, scrollbars=yes", "NewFile", param);
	}
	function OpenWindowWithPost(url, windowoption, name, params)
	{
	 var form = document.createElement("form");
	 form.setAttribute("method", "post");
	 form.setAttribute("action", url);
	 form.setAttribute("target", name);
	 for (var i in params)
	 {
	   if (params.hasOwnProperty(i))
	   {
	     var input = document.createElement('input');
	     input.type = 'hidden';
	     input.name = i;
	     input.value = params[i];
	     form.appendChild(input);
	   }
	 }
	 document.body.appendChild(form);
	// window.open("", name, windowoption);
	 form.submit();
	 document.body.removeChild(form);
	}