//java script for the hospital general screen in the empanelment of hospital flow
//function to submit the screen
function onUserSubmit()
{
  		if(!JS_SecondSubmit)
        {
  			
  			if(document.forms[1].indORgrp.value=='GRP' && document.forms[1].createNewGrp.value=='NewGrp')
  			{
  				if(document.forms[1].groupName.value=='')
  				{	
  					alert("Please Enter Group Name");
	  				document.forms[1].groupName.focus();
	  				return false;
  				}
  			}
  			else if(document.forms[1].indORgrp.value=='GRP' && document.forms[1].createNewGrp.value=='')
  			{
  				if(document.getElementById("provGrpListId").selectedIndex==0)
	  			{
  				alert("Please Select Group Provider");
  				document.forms[1].provGrpListId.focus();
  				return false;
	  			}
  			} 
  			
  			trimForm(document.forms[1]);
  			if(document.forms[1].stopPreAuthsYN.checked)
  	    		document.forms[1].stopPreAuthsYN.value="Y";
  	    	else
  	    		document.forms[1].stopPreAuthsYN.value="N";
  	    	if(document.forms[1].stopClaimsYN.checked)
  	    		document.forms[1].stopClaimsYN.value="Y";
  	    	else
  	    		document.forms[1].stopClaimsYN.value="N";
  	    	

  	    	/*
  	    	 * coomented after makign masters for Networks
  	    	 * if(document.forms[1].CNYN.checked)
  	    		document.forms[1].cnynNet.value="Y";
  	    	else
  	    		document.forms[1].cnynNet.value="N";

  	    	if(document.forms[1].GNYN.checked)
  	    		document.forms[1].gnynNet.value="Y";
  	    	else
  	    		document.forms[1].gnynNet.value="N";

  	    	if(document.forms[1].SRNYN.checked)
  	    		document.forms[1].srnynNet.value="Y";
  	    	else
  	    		document.forms[1].srnynNet.value="N";

  	    	if(document.forms[1].RNYN.checked)
  	    		document.forms[1].rnynNet.value="Y";
  	    	else
  	    		document.forms[1].rnynNet.value="N";
  	    	
  	    	if(document.forms[1].WNYN.checked)
  	    		document.forms[1].wnynNet.value="Y";
  	    	else
  	    		document.forms[1].wnynNet.value="N";*/
  	    	
  	    	
  	    	/*if(document.forms[1].providerYN.checked)
  	    		document.forms[1].provgrpHidden.value="Y";
  	    	else
  	    		document.forms[1].provgrpHidden.value="N";*/
  	    	
  	    	if(document.forms[1].providerReview.checked)
  	    		document.forms[1].providerReviewYN.value="Y";
  	    	else
  	    		document.forms[1].providerReviewYN.value="N";
  	    	
  	    	if(document.getElementById("stopPreauthDateId").style.display ==""){
				var stoppreauth = document.getElementById("stopPreauthId").value;
				if(stoppreauth ==""){
					 alert("Stop Cashless Date is Required");
					  return false;
				}
				
				 if(document.getElementById("stopcashlessproviderid").value != null){
						var stopcashlessprovider =	document.getElementById("stopcashlessproviderid").value;
						
						var  currentDate =  new Date();
						var curmonth = currentDate.getMonth() + 1;
					    var curday = currentDate.getDate();
					    var curyear = currentDate.getFullYear();
					    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
						  
					      curnewdate = curnewdate.split("/");
					      stoppreauth = stoppreauth.split("/");
					      stopcashlessprovider = stopcashlessprovider.split("-");
						  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
						  stoppreauth = new Date(stoppreauth[2], stoppreauth[1] - 1, stoppreauth[0]).getTime();
						  stopcashlessprovider = new Date(stopcashlessprovider[0], stopcashlessprovider[1] - 1, stopcashlessprovider[2]).getTime();
						  if(stopcashlessprovider == stoppreauth){
							  
						  }else if(!(stoppreauth > curnewdate)){
							  alert("Stop Cashless Date must be future date");
							  return false;
						  }
					}
			}
  	    	
  	    	if(document.getElementById("stopClaimsDateId").style.display ==""){
				var stopclms = document.getElementById("stopclmsid").value;
				if(stopclms==""){
					  alert("Stop Claims Date is Required");
					  return false;
				  }
				
				if(document.getElementById("stopclaimsproviderid").value != null){
				var stopclaimsprovider =	document.getElementById("stopclaimsproviderid").value;
				
				var  currentDate =  new Date();
				var curmonth = currentDate.getMonth() + 1;
			    var curday = currentDate.getDate();
			    var curyear = currentDate.getFullYear();
			    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
				  
			      curnewdate = curnewdate.split("/");
			      stopclms = stopclms.split("/");
			      stopclaimsprovider = stopclaimsprovider.split("-");
				  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
				  stopclms = new Date(stopclms[2], stopclms[1] - 1, stopclms[0]).getTime();
				  stopclaimsprovider = new Date(stopclaimsprovider[0], stopclaimsprovider[1] - 1, stopclaimsprovider[2]).getTime();
				  if(stopclaimsprovider == stopclms){
					  
				  }else if(!(stopclms > curnewdate)){
					  alert("Stop Claims Date must be future date");
					  return false;
				  }
				
				}  
				
			}
  	   //<!-- added for RoomRenttariff -->
  			
  	    	/*if(document.forms[1].gipsaPpn.value=="Y")
  			{
  				document.forms[1].gipsaPpnYN.checked=true;
  			}
  			else
  			{
  				document.forms[1].gipsaPpnYN.checked=false;
  			}*/
  			//end  <!-- added for RoomRenttariff -->
  	   // 	document.forms[1].categoryID.disabled=false;
  	    	if(document.forms[1].dayOfMonth.value!="" && document.forms[1].claimSubmissionFlag.value=="")
				{	
					alert("Please Select Claim Submission Period Dropdown");
  				document.forms[1].claimSubmissionFlag.focus();
  				return false;
				}
  	    	if(document.forms[1].freeFollowupPeriod.value==null || document.forms[1].freeFollowupPeriod.value =='')
			{	
				alert("Provider Wise Consultation Free Followup for Period is Required");
				document.forms[1].freeFollowupPeriod.focus();
				return false;
			}
  	    	var count = document.forms[1].dayOfMonth.value;
  	    	if((document.forms[1].claimSubmissionFlag.value=='MON') && count>31)
			{	
				alert("Maximum 31 days are allowed For Day of The Next Month selection !!");
				document.forms[1].dayOfMonth.focus();
				return false;
			}
  	    	
  	  // Payment Term Agreed (In Days) mandatory validation
			/*var paymentDuration = document.getElementById("paymentDuration").value
			if(paymentDuration == null || paymentDuration =="")
			{
				alert("Please select Payment Term Agreed (In Days).");
				document.getElementById("paymentDuration").focus();
				return ;
			}*/
	// start date  mandatory validation
		/*	var paymentTermAgrSDate = document.getElementById("paymentTermAgrSDate").value
			if(paymentTermAgrSDate == null || paymentTermAgrSDate =="")
			{
				alert("Please enter Payment Term Agreed (In Days) Start Date.");
				document.getElementById("paymentTermAgrSDate").focus();
				return ;
			}*/
  	    	
  	   // start date format validation
  	    	if(document.getElementById("paymentTermAgrSDate").value != "")
			{
				if(isDate(document.getElementById("paymentTermAgrSDate"),"Date")==false)
				{
					document.getElementById("paymentTermAgrSDate").value="";
					document.getElementById("paymentTermAgrSDate").focus();
					return false;
				}
			}
  	    	
  	   // end date format validation
			if(document.getElementById("paymentTermAgrEDate").value != "")
			{
				if(isDate(document.getElementById("paymentTermAgrEDate"),"Date")==false)
				{
					document.getElementById("paymentTermAgrEDate").value="";
					document.getElementById("paymentTermAgrEDate").focus();
					return false;
				}
			}
			
		// 1) for first time : start date  10+ and 10- validation
			var changeStDate = document.getElementById("paymentTermAgrSDate").value;	
			var landDate=document.getElementById("oldPaymentTermAgrSDate").value;	
			
  	    	if((changeStDate != null && changeStDate !="") &&  (landDate == null || landDate ==""))
  	    	{
  	  		
  	  			var sdate =	changeStDate;
  	  		//	var pattern = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
  	  		//	pyamentStartDate = document.getElementById("paymentTermAgrSDate").value;
  	  		//  if (pattern.test(pyamentStartDate))
  	  			sdate = sdate.split("/");
  	  			sdate = new Date(sdate[2], sdate[1] - 1, sdate[0]).getTime();
  	  		
  	  		// current date
  	  			var currentDate =  new Date();
  	  			var curmonth = currentDate.getMonth() + 1;
  	  		    var curday = currentDate.getDate();
  	  		    var curyear = currentDate.getFullYear();
  	  		    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
  	  			  
  	  		    curnewdate = curnewdate.split("/");
  	  		    curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
  	  			
  	  			  	msPerDay =  1000 * 3600 * 24;
  	  				days = Math.ceil((sdate.valueOf()-curnewdate.valueOf())/ msPerDay) + 1;
  	  			//	alert("total days="+days);
  	  				if(days<1 || days > 30)
  	  				{
  	  						alert("Only current date & Future date allowed upto 30 days and maximum 1 future payment term can be added.");
  	  						document.getElementById("paymentTermAgrSDate").value='';
  	  						document.getElementById("paymentTermAgrSDate").focus();
  	  						return false;
  	  				}  
  	    	}
  	    	
  	    // case 2: if user not changed any start date 	
  	    	
  	    		var changStDate =	changeStDate;
  	    		changStDate = changStDate.split("/");
  	    		changStDate = new Date(changStDate[2], changStDate[1] - 1, changStDate[0]).getTime();
  	    	
  	    		var lndDate =	landDate;
  	    		lndDate = lndDate.split("/");
  	    		lndDate = new Date(lndDate[2], lndDate[1] - 1, lndDate[0]).getTime();
  	    		if(changStDate ==lndDate )
  	    			{
  	    			
  	    			}
  	    		else
  	    			{
  	    // 	case 3 
  	    			if(changStDate < lndDate)	
  	    				{
  	    				// db logic  
  	    				}
  	    			else if(changStDate > lndDate)	
  	    				{
  	    				
  	    	  	  		// current date
  	    	  	  			var currentDate =  new Date();
  	    	  	  			var curmonth = currentDate.getMonth() + 1;
  	    	  	  		    var curday = currentDate.getDate();
  	    	  	  		    var curyear = currentDate.getFullYear();
  	    	  	  		    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
  	    	  	  			  
  	    	  	  		    curnewdate = curnewdate.split("/");
  	    	  	  		    curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
  	    	  	  			
  	    	  	  			  	msPerDay =  1000 * 3600 * 24;
  	    	  	  				days = Math.ceil((changStDate.valueOf()-curnewdate.valueOf())/ msPerDay) + 1;
  	    	  	  			//	alert("total days="+days);
  	    	  	  				if(days<1 || days > 30)
  	    	  	  				{
  	    	  	  						alert("Only current date & Future date allowed upto 30 days and maximum 1 future payment term can be added.");
  	    	  	  						document.getElementById("paymentTermAgrSDate").value='';
  	    	  	  						document.getElementById("paymentTermAgrSDate").focus();
  	    	  	  						return false;
  	    	  	  				}  
  	    				}
  	    			}
  	    	
		// end date compare validation
  	    	var startDate =document.getElementById("paymentTermAgrSDate").value;    	
			var endDate=document.getElementById("paymentTermAgrEDate").value;				
			
			if( !((document.getElementById("paymentTermAgrSDate").value)==="") && !((document.getElementById("paymentTermAgrEDate").value)===""))
			{
				var sdate = startDate.split("/");
				var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
				altsdate=new Date(altsdate);
				
				var edate =endDate.split("/");
				var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
				altedate=new Date(altedate);
				
				var Startdate = new Date(altsdate);
				var EndDate =  new Date(altedate);
				
				if(EndDate < Startdate)
				 {
					alert("Payment Term Agreed (In Days) End Date should be greater than or equal to Start Date.");
					document.getElementById("paymentTermAgrEDate").value="";
					document.getElementById("paymentTermAgrEDate").focus();
					return ;
				 }
			} 
  	 
    		document.forms[1].mode.value="doSave";
    		document.forms[1].action="/AddHospitalSearchAction.do";
    		JS_SecondSubmit=true;
    		document.forms[1].submit();
    	}//end of if(!JS_SecondSubmit)
}//end of onUserSubmit()

function onDiscrepancySubmit()
{
	document.forms[1].mode.value="doDiscrepancy";
   	document.forms[1].action="/GeneralDiscrepancyAction.do";
   	document.forms[1].submit();
}//end of onDiscrepancySubmit()

function onReset()
{
	if(!JS_SecondSubmit)
	{
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{	
    		document.forms[1].mode.value="doReset";
    		document.forms[1].action="/EditHospitalSearchAction.do";
    		JS_SecondSubmit=true;
    		document.forms[1].submit();
    	
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
			stopPreAuthClaim();
			//setnetworkTypes();
		}//end of else
	}
}//end of onReset()

function onChangeState(obj)
{
	if(obj.value!=""){
    	document.forms[1].mode.value="doChangeState";
    	document.forms[1].focusID.value="state";
    	document.forms[1].action="/EditHospitalSearchAction.do";
    	document.forms[1].submit();
	}
}//end of onChangeCity()

function onConfiguration()
{
	document.forms[1].mode.value="doConfiguration";
	document.forms[1].action="/EditHospitalSearchAction.do";
	document.forms[1].submit();
}//end of onConfiguration()

function stopPreAuthClaim()
{
	if(document.forms[1].stopPreAuth.value=="Y")
	    document.forms[1].stopPreAuthsYN.checked=true;
	if(document.forms[1].stopClaim.value=="Y")
		document.forms[1].stopClaimsYN.checked=true;
}//end of stopPreAuthClaim()

/*
 * coomented after makign masters for Networks
 * function setnetworkTypes()
{
	if(document.forms[1].cnynNet.value=="Y")
	    document.forms[1].CNYN.checked=true;
	if(document.forms[1].gnynNet.value=="Y")
		document.forms[1].GNYN.checked=true;
	if(document.forms[1].srnynNet.value=="Y")
		document.forms[1].SRNYN.checked=true;
	if(document.forms[1].rnynNet.value=="Y")
		document.forms[1].RNYN.checked=true;
	if(document.forms[1].wnynNet.value=="Y")
		document.forms[1].WNYN.checked=true;
	
	if(document.forms[1].primaryNetworkID.value=="GN")
	{
		document.forms[1].CNYN.disabled=true;
	}
	if(document.forms[1].primaryNetworkID.value=="SN")
	{
		document.forms[1].CNYN.disabled=true;
		document.forms[1].GNYN.disabled=true;
	}
	if(document.forms[1].primaryNetworkID.value=="BN")
	{
		document.forms[1].CNYN.disabled=true;
		document.forms[1].GNYN.disabled=true;
		document.forms[1].SRNYN.disabled=true;
	}
	if(document.forms[1].primaryNetworkID.value=="WN")
	{
		document.forms[1].CNYN.disabled=true;
		document.forms[1].GNYN.disabled=true;
		document.forms[1].SRNYN.disabled=true;
		document.forms[1].RNYN.disabled=true;
	}
	
	
	if(document.forms[1].providerReviewYN.value=="Y")
		document.forms[1].providerReview.checked=true;
	
	if(document.forms[1].provgrpHidden.value=="Y")
	{
		document.forms[1].providerYN.checked=true;
		document.getElementById("idprovGrpListId").style.display="none";
	}
	
	if(document.forms[1].sectorType.value=="SPR")
	{
		document.getElementById("idprovSector").style.display="none";
		document.forms[1].sectorNumb.value="";
	}else{
		document.getElementById("idprovSector").style.display="inline";
	}
	
	if(document.forms[1].provGrpId.value=="PGR" || document.forms[1].provGrpId.value=="")
	{
		document.getElementById("idprovGrpListId").style.display="none";
	}else
	{
		document.getElementById("idprovGrpListId").style.display="inline";
	}
	
//	changeNetworks(document.forms[1].primaryNetworkID);
}//end of setnetworkTypes()
*/

function checkHideGroupProvider()
{
	if(document.getElementById("providerYN").checked==true)
	{
		document.getElementById("idprovGrpListId").style.display="none";
		document.forms[1].groupName.style.display	=	"inline";
		document.forms[1].provGrpListId.value="";	
	}else{
		document.getElementById("idprovGrpListId").style.display="inline";
		document.forms[1].groupName.style.display	=	"none";
		document.forms[1].groupName.value	=	'';
	}
}

function onCheckIndGrp(obj)
{
	var indORgrp	=	obj.value;
	if(indORgrp=='GRP')
	{
		document.getElementById("createNew").style.display	=	"inline";
		document.forms[1].createNewGrp.value		=	"";
		document.getElementById("groupProvider").style.display	=	"inline";
		
	}
	else{
		document.getElementById("createNew").style.display	=	"none";
		document.getElementById("grpContacts1").style.display	="none";
		document.getElementById("grpContacts2").style.display	="none";
		document.getElementById("grpContacts3").style.display	="none";

		document.forms[1].groupName.value			=	"";
		document.forms[1].grpContactPerson.value	=	"";
		document.forms[1].grpContactNo.value		=	"";
		document.forms[1].grpContactEmail.value		=	"";
		document.forms[1].grpContactAddress.value	=	"";
		document.getElementById("provGrpListId").selectedIndex=0;
		document.forms[1].createNewGrp.value		=	"";
	}
}

//to create groups
function onCreateGroup()
{
	document.getElementById("grpContacts1").style.display="inline";
	document.getElementById("grpContacts2").style.display="inline";
	document.getElementById("grpContacts3").style.display="inline";
	document.getElementById("groupProvider").style.display="none";
	document.forms[1].createNewGrp.value		=	"NewGrp";
	
}
//Ajax code to get Provider details dynamically.
function getProviderDetails(obj)
{
	//alert("value::"+obj.value);
	var val	=	obj.value;
	val	=	val.split('[');
	
	var valNew	=	val[1].substring(0,val[1].length-1);
	var provName	=	obj.value.split('[');
	if (window.ActiveXObject){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		if (xmlhttp){
			xmlhttp.open("GET",'/HospitalSearchAction.do?mode=getLicenceNumbers&ProviderId=' +valNew+'&provName=' +provName[0] ,true);
			xmlhttp.onreadystatechange=state_ChangeProvider;
			xmlhttp.send();
		}
	}
	
	//Ajax code removing as we are getting the code in the auto complete box itself
	/*var val	=	obj.value;
	val	=	val.split('[');
	
	var valNew	=	val[1].substring(0,val[1].length-1);
	document.getElementById("irdaNumber").value	=	valNew;*/
}

function state_ChangeProvider(){
	document.getElementById("validHosp").innerHTML	=	'';
	var result,result_arr;
	if (xmlhttp.readyState==4){
		//alert(xmlhttp.status);
	if (xmlhttp.status==200){
				result = xmlhttp.responseText;
			result_arr = result.split(","); 
			if(result_arr[0]!=""){
				document.getElementById("irdaNumber").value=result_arr[0];
				document.getElementById("validHosp").innerHTML	=	'Valid Provider';
				document.getElementById("validHosp").style.color = 'green';
			}
			else{
				document.getElementById("validHosp").innerHTML	=	'Invalid Provider';
				document.getElementById("validHosp").style.color = 'red';
				document.getElementById("irdaNumber").value="";
				document.getElementById("hospitalName").value="";
			}
			
		}
	}
}

function getProviderDetailsOnLicence(obj)
{
	var val	=	obj.value;
	if (window.ActiveXObject){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		if (xmlhttp){
			xmlhttp.open("GET",'/HospitalSearchAction.do?mode=getLicenceNumbers&ProviderId=' +val+'&strIdentifier=GetNameByLicence' ,true);
			xmlhttp.onreadystatechange=state_ChangeLicence;
			xmlhttp.send();
		}
	}
}


function state_ChangeLicence(){
	document.getElementById("validHosp").innerHTML	=	'';
	var result,result_arr;
	if (xmlhttp.readyState==4){
		//alert(xmlhttp.status);
	if (xmlhttp.status==200){
				result = xmlhttp.responseText;
			result_arr = result.split(","); 
			if(result_arr[0]!=""){
				document.getElementById("hospitalName").value=result_arr[0];
				document.getElementById("validHosp").innerHTML	=	'Valid Provider';
				document.getElementById("validHosp").style.color = 'green';
			}
			else{
				document.getElementById("validHosp").innerHTML	=	'Invalid Provider';
				document.getElementById("validHosp").style.color = 'red';
				document.getElementById("irdaNumber").value="";
				document.getElementById("hospitalName").value="";
			}
			
		}
	}
}
/*function changeNetworks(obj)
{	
	var networkType	=	obj.value;
	if(networkType=='CN')
	{
		document.getElementById("CNYN").disabled = false;
		document.getElementById("GNYN").disabled = false;
		document.getElementById("SRNYN").disabled = false;
		document.getElementById("RNYN").disabled = false;
		document.getElementById("WNYN").disabled = false;
		
		
		document.getElementById("CNYN").checked=true;
		document.getElementById("RNYN").checked=false;
		document.getElementById("SRNYN").checked=false;
		document.getElementById("GNYN").checked=false;
		document.getElementById("WNYN").checked = false;
	}else if(networkType=='GN')
	{
		document.getElementById("CNYN").disabled = true;
		document.getElementById("GNYN").disabled = false;
		document.getElementById("SRNYN").disabled = false;
		document.getElementById("RNYN").disabled = false;
		document.getElementById("WNYN").disabled = false;

		document.getElementById("CNYN").checked = false;
		document.getElementById("GNYN").checked=true;
		document.getElementById("RNYN").checked=false;
		document.getElementById("SRNYN").checked=false;
		document.getElementById("WNYN").checked = false;

	}else if(networkType=='SN')
	{
		document.getElementById("CNYN").disabled = true;
		document.getElementById("GNYN").disabled = true;
		document.getElementById("SRNYN").disabled = false;
		document.getElementById("RNYN").disabled = false;
		document.getElementById("WNYN").disabled = false;
		
		document.getElementById("CNYN").checked = false;
		document.getElementById("GNYN").checked = false;
		document.getElementById("SRNYN").checked = true;
		document.getElementById("RNYN").checked=false;
		document.getElementById("WNYN").checked = false;
	}else if(networkType=='BN')
	{
		document.getElementById("CNYN").disabled = true;
		document.getElementById("GNYN").disabled = true;
		document.getElementById("SRNYN").disabled = true;
		document.getElementById("RNYN").disabled = false;
		document.getElementById("WNYN").disabled = false;
		
		document.getElementById("CNYN").checked = false;
		document.getElementById("GNYN").checked = false;
		document.getElementById("SRNYN").checked=false;
		document.getElementById("RNYN").checked=true;
		document.getElementById("WNYN").checked = false;

	}else if(networkType=='VN')
	{
		document.getElementById("CNYN").disabled = true;
		document.getElementById("GNYN").disabled = true;
		document.getElementById("SRNYN").disabled = true;
		document.getElementById("RNYN").disabled = true;
		document.getElementById("WNYN").disabled = false;

		document.getElementById("CNYN").checked = false;
		document.getElementById("GNYN").checked = false;
		document.getElementById("RNYN").checked = false;
		document.getElementById("SRNYN").checked = false;
		document.getElementById("WNYN").checked = true;
		
	}

}*/

function onUploadDocs()
{
	document.forms[1].mode.value="doDefault";
	var hosp_seq_id	=	document.forms[1].hospSeqId.value;
	document.forms[1].action="/UploadMOUCertificatesList.do?hosp_seq_id="+hosp_seq_id;
	document.forms[1].submit();
}

function changeMe(obj)
{
	var val	=	obj.value;
	//alert(val);
	if(val=='Phone No')
	{
		document.forms[1].officePhone1.value="";
	}
	if(val=='ISD')
	{
		document.forms[1].isdCode.value="";
	}
	if(val=='STD')
	{
		document.forms[1].stdCode.value="";
	}
}
function getMe(obj)
{
	//alert(obj);
	
	if(obj=='ISD')
	{
		if(document.forms[1].isdCode.value=="")
			document.forms[1].isdCode.value=obj;
	}
	if(obj=='STD')
	{
		if(document.forms[1].stdCode.value=="")
			document.forms[1].stdCode.value=obj;
	}if(obj=='Phone No')
	{
		if(document.forms[1].officePhone1.value=="")
			document.forms[1].officePhone1.value=obj;
	}
	
	
}

function checkProviderGroup(obj)
{
	if(document.getElementById("providerYN").checked==true)
	{
		document.getElementById("tdprovGrpId").style.display="inline";
		//document.getElementById("idprovGrpListId").style.display="inline";
	}
	if(document.getElementById("providerYN").checked==false)
	{
		document.getElementById("tdprovGrpId").style.display="none";
		document.getElementById("idprovGrpListId").style.display="none";
		document.forms[1].provGrpId.value="";
		document.forms[1].provGrpListId.value="";	
	}
}

function changeGroupType(obj)
{
	if(obj.value=="PGR" || obj.value=="")
	{
		document.getElementById("idprovGrpListId").style.display="none";
	}else
	{
		document.getElementById("idprovGrpListId").style.display="inline";
	}
}

function onChangeSector(obj)
{
	var val	=	obj.value;
	if(val=="SGO")
	{
		document.getElementById("idprovSector").style.display="inline";
	}else{
		document.getElementById("idprovSector").style.display="none";
	}
}

function getToolTip(){
    document.forms[1].providerReview.title="Please Review the Hospital Details before selecting";
}

function onChangeRegAuth(){
	document.forms[1].hospitalName.value="";
	document.forms[1].irdaNumber.value="";
	document.forms[1].action="/ChangeRegAuth.do";
	document.forms[1].submit();
}

function onLoadGroup(indOrGrp)
{
	if(indOrGrp=='GRP' && document.forms[1].createNewGrp.value=='NewGrp')
	{
		document.getElementById("grpContacts1").style.display="inline";
		document.getElementById("grpContacts2").style.display="inline";
		document.getElementById("grpContacts3").style.display="inline";
		
	}
	else if(indOrGrp=='GRP' && document.forms[1].createNewGrp.value=='')
	{
		document.getElementById("createNew").style.display="inline";
		
	} 
}

function changeNetworks(obj)
{
	var networkType	=	obj.value;
	var temp	=	0;
	var inputs	=	document.getElementsByName("serviceType");
	for(var i=0;i<inputs.length;i++)
	{
		if(inputs[i].type=="checkbox"){
			inputs[i].disabled	=	false;
			if(networkType==inputs[i].value){
				inputs[i].checked	=	true;
				document.getElementById("hidServiceType"+i).value='Y';
				temp	=	i;
			}
			else{
				inputs[i].checked	=	false;
				document.getElementById("hidServiceType"+i).value='N';
			}
		}
	}
	
	for(var k=0;k<temp;k++)
		document.getElementById("serviceType"+k).disabled=true;
}

function checkNetworkType(obj){
	if(document.getElementById("serviceType"+obj).checked)
		document.getElementById("hidServiceType"+obj).value='Y';
	else
		document.getElementById("hidServiceType"+obj).value='N';
}

function disableHighlevelNet(obj)
{
	var res	=	obj.split(",");
if(document.forms[1].hospSeqId.value!=""){
	for(var a=0;a<obj.length;a++){
		document.getElementById("serviceType"+res[a]).checked	=	true;
		document.getElementById("hidServiceType"+res[a]).value	=	"Y";
	}
}
}

function onHaadFactorDefault()
{
	if(!JS_SecondSubmit)
    {
		var hosp_seq_id	=	document.forms[1].hospSeqId.value;
		if(hosp_seq_id==null || hosp_seq_id=='')
		{
			alert("Please Empanel the Provider First..");
			return false;
		}
		document.forms[1].mode.value="doHaadFactorDefault";
    	document.forms[1].action="/EditHaadFactorsHospitalAction.do?hosp_seq_id="+hosp_seq_id;
    	document.forms[1].submit();
    
    }
}

function showNotification(){
	var val1 = document.getElementById("bioMetricAccess").value;
	if( val1 == "BIOE")
		alert("Please Make Sure Bio-Metric Device is Installed and Configured in Client's System!");
}



function onProviderDiscountConfiguration()
{
	if(!JS_SecondSubmit)
    {
		document.forms[1].mode.value="doProviderDiscountConfiguration";
		var hosp_seq_id	=	document.forms[1].hospSeqId.value;
		document.forms[1].action="/AddHospitalSearchAction.do?hosp_seq_id="+hosp_seq_id;
		JS_SecondSubmit=true;
		document.forms[1].submit();
    }
}//end of onAssociateCorp()

function showAndHideDateClaims(){
	var elementcheckedornot = document.getElementById('stopClaimsYN');
	if(elementcheckedornot.checked){
		document.getElementById("stopClaimsDateId").style.display="";
		document.getElementById("stopClaimsYN").value="Y";
	}else{
		document.getElementById("stopClaimsDateId").style.display="none";
		document.getElementById("stopClaimsYN").value="N";
		document.getElementById("stopclmsid").value="";
	}
 }

function showAndHideDatePreauth(){
	var elementcheckedornot = document.getElementById('stopPreAuthsYN');
	if(elementcheckedornot.checked){
		document.getElementById("stopPreauthDateId").style.display="";
		document.getElementById("stopPreAuthsYN").value="Y";
	}else{
		document.getElementById("stopPreauthDateId").style.display="none";
		document.getElementById("stopPreAuthsYN").value="N";
		document.getElementById("stopPreauthId").value="";
	}
 }

function onActivityLog()
{	
	var oldPaymentDuration = document.getElementById("oldPaymentDuration").value;
	var oldPaymentTermAgrSDate = document.getElementById("oldPaymentTermAgrSDate").value;
	var oldPaymentTermAgrEDate = document.getElementById("oldPaymentTermAgrEDate").value;
	 
	document.forms[1].mode.value="doDisplayActivityLog";
   	document.forms[1].action="/HospitalActivityLogAction.do?oldPaymentDuration="+oldPaymentDuration+"&oldPaymentTermAgrSDate="+oldPaymentTermAgrSDate+"&oldPaymentTermAgrEDate="+oldPaymentTermAgrEDate;
   	document.forms[1].submit();
}//end of onDiscrepancySubmit()

function OnChangePaymentDuration()
{
	document.getElementById("paymentTermAgrSDate").value ="";
	document.getElementById("paymentTermAgrEDate").value ="";
	document.getElementById("paymentTermAgrSDate").focus();
}	
