
	//policydetail.js is called from policydetail.jsp
	//funtion onSave
	function onSave()
	{
		trimForm(document.forms[1]);
		if(!JS_SecondSubmit)
		{
			if(document.getElementById("stopCashlessDateId").style.display ==""){
				var stoppreauth;
				  var stopcshless = 	document.getElementById("stopcshlessid").value;
				  if(stopcshless==""){
					  alert("Stop Cashless Date is Required");
					  return false;
				  }
				 if(document.getElementById("stoppreauthid").value != null){
					 stoppreauth = document.getElementById("stoppreauthid").value; 
				 } 
				var  currentDate =  new Date();
				var curmonth = currentDate.getMonth() + 1;
			    var curday = currentDate.getDate();
			    var curyear = currentDate.getFullYear();
			    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
				  
			      curnewdate = curnewdate.split("/");
				  stopcshless = stopcshless.split("/");
				  stoppreauth = stoppreauth.split("-");
				  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
				  stopcshless = new Date(stopcshless[2], stopcshless[1] - 1, stopcshless[0]).getTime();
				  stoppreauth = new Date(stoppreauth[0], stoppreauth[1] - 1, stoppreauth[2]).getTime();
				  
				  if(stoppreauth == stopcshless){
					   
				  }else if(!(stopcshless > curnewdate)){
					  alert("Stop Cashless Date must be future date");
					  return false;
				  }
				
					}
					
					if(document.getElementById("stopClaimsDateId").style.display ==""){
						var stopclaim;
						var stopclms = document.getElementById("stopclmsid").value;
						if(stopclms==""){
							  alert("Stop Claims Date is Required");
							  return false;
						  }
						 if(document.getElementById("stoppreauthid").value != null){
							 stopclaim	= document.getElementById("stopclaimid").value; 
							 }
						var  currentDate =  new Date();
						var curmonth = currentDate.getMonth() + 1;
					    var curday = currentDate.getDate();
					    var curyear = currentDate.getFullYear();
					    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
					      
					      curnewdate = curnewdate.split("/");
					      stopclms = stopclms.split("/");
					      stopclaim = stopclaim.split("-");
						  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
						  stopclms = new Date(stopclms[2], stopclms[1] - 1, stopclms[0]).getTime();
						  stopclaim = new Date(stopclaim[0], stopclaim[1] - 1, stopclaim[2]).getTime();
						  if(stopclaim == stopclms){
							   
						  }else if(!(stopclms > curnewdate)){
							  alert("Stop Claims Date must be future date");
							  return false;
						  }
					}
			
			if(document.forms[1].stopPreAuthsYN.checked)
	    		document.forms[1].stopPreAuth.value="Y";
	    	else
	    		document.forms[1].stopPreAuth.value="N";
	    	if(document.forms[1].stopClaimsYN.checked)
	    		document.forms[1].stopClaim.value="Y";
	    	else
	    		document.forms[1].stopClaim.value="N";
				//KOC 1286 for OPD Benefit
	    	
	    	if(document.forms[1].opdClaimsYN.checked)
	    		document.forms[1].opdClaim.value="Y";
	    	else
	    		document.forms[1].opdClaim.value="N";
	    	
	    	//KOC 1286 for OPD Benefit
				
				// KOC 1270 for hospital cash benefit
	    	
	    	if(document.forms[1].cashBenefitYN.checked)
	    	{
	    		document.forms[1].cashBenefit.value="Y";
	    	}
	    	else
	    	{
	    		document.forms[1].cashBenefit.value="N";
	    	}
	   
	    	if(document.forms[1].convCashBenefitYN.checked)
	    	{
	    		document.forms[1].convCashBenefit.value="Y";
	    	}
	    	else
	    	{
	    		document.forms[1].convCashBenefit.value="N";
	    	}
	    	// KOC 1270 for hospital cash benefit
		//added as per KOC 1216B Change request
	    	if(document.forms[1].memberBufferYN.checked==true)
	    		document.forms[1].memberBuffer.value="Y";
	    	else
	    		document.forms[1].memberBuffer.value="N";
	    	
	    	//added for KOC-1273
	    	if(document.forms[1].criticalBenefitYN.checked)
	    		document.forms[1].criticalBenefit.value="Y";
	    	else
	    		document.forms[1].criticalBenefit.value="N";
	    	
	    	if(document.forms[1].survivalPeriodYN.checked)
	    		document.forms[1].survivalPeriod.value="Y";
	    	else
	    		document.forms[1].survivalPeriod.value="N";
	    	
	    	if(document.forms[1].mailNotificationYN.checked)
	    		document.forms[1].mailNotificationhiddenYN.value="Y";
	    	else
	    		document.forms[1].mailNotificationhiddenYN.value="N";
	    	//added as per KOC 1216B Change request
			document.forms[1].mode.value="doSave";
		    document.forms[1].action="/SavePoliciesAction.do";
		    JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
	}//end of onSave()

	function onReset()
	{
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		 {
		  document.forms[1].mode.value="doReset";
		  document.forms[1].action="/EditPoliciesSearchAction.do";
		  document.forms[1].submit();
		 }
		 else
		 {
		  	document.forms[1].reset();
		  	isBufferAllowedYN();
		  	//isBrokerAllowedYN();
		  	stopPreAuthClaim();
		 }
	}//end of onReset

	function isBufferAllowedYN()
	{
		bufferAllowedYN=document.getElementById("bufferAllowedYN");
		if(bufferAllowedYN.checked)
		{
			document.forms[1].admnAuthorityTypeID.disabled=false;
			document.forms[1].allocatedTypeID.disabled=false;
//added as per KOC 1216B changeRequest
			//memberBufferYN
			
			if(document.forms[1].memberBuffer.value=="Y")
			{
				document.forms[1].memberBufferYN.checked=true;
			}
			else{
				document.forms[1].memberBufferYN.checked=false;
			}
			document.forms[1].memberBufferYN.disabled=false;
		}
		else
		{
			document.forms[1].admnAuthorityTypeID.disabled=true;
			document.forms[1].allocatedTypeID.disabled=true;
			document.forms[1].admnAuthorityTypeID.value="";
			document.forms[1].allocatedTypeID.value="";

			//Added as per KOC 1216B ChangeRequest
			document.forms[1].memberBufferYN.disabled=true;
			document.forms[1].memberBufferYN.checked=false;
			document.forms[1].memberBuffer.value="N";
			//Added as per KOC 1216B ChangeRequest
			
		}
	}//end of isBufferAllowedYN()
	
	function isBrokerAllowedYN()
	{
		brokerYN=document.getElementById("brokerYN");
		if(brokerYN.checked)
		{
			document.forms[1].brokerID.disabled=false;
			//document.forms[1].groupBranchSeqID.disabled=true;
			//document.forms[1].groupBranchSeqID.value="";
//added as per Broker changeRequest
			
		}
		else
		{
			document.forms[1].brokerID.disabled=true;
			document.forms[1].brokerID.value="";
			//document.forms[1].groupBranchSeqID.disabled=false;
			

		}
	}//end of isBrokerAllowedYN()	
	
//Added as per Broker Change Request 
	
	 function showHideType2()
	   {
	       var a=document.getElementById('brokerYN');
	       if (a.checked)
	       {
	    	 document.forms[1].brokerID.style.display="";  
	   	 	}
	       else
	       {
	    	   document.forms[1].brokerID.style.display="none";
	   	 	}
	   	}
	
	
	
	function isCheck()
	{
		if(document.forms[1].memberBufferYN.checked)
		{
			

			document.forms[1].memberBufferYN.value="Y";
		}
		else{
			document.forms[1].memberBufferYN.checked=false;
			document.forms[1].memberBufferYN.value="N";
		}
		
	}
	
	//Added as per KOC 1216B Change Request 
	
	function onGenerateXL()
	{
		/*//document.forms[1].mode.value="doGenerateAdminXL";
		//document.forms[1].action="/ReportsAction.do";
		var policySeqID = document.forms[1].policySeqID.value;
		parameterValue =policySeqID;
		var partmeter = "?mode=doGenerateAdminXL&parameter="+parameterValue+"&fileName=generalreports/RenewalMembersXL.jrxml&reportID=GenRenMemXLs&reportType=EXL";
		var openPage = "/ReportsAction.do"+partmeter;
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 99;
		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		document.forms[1].mode.value="doGenerateAdminXL";
		window.open(openPage,'',features);*/
		var policySeqID = document.forms[1].policySeqID.value;
		document.forms[1].mode.value="doGenerateXLReport";
		document.forms[1].action="/EditPoliciesSearchAction.do?policySeqID="+policySeqID;
		document.forms[1].submit();
	}//end of onGenerateXL()
	
	function onConfiguration()
	{
		document.forms[1].mode.value="doConfiguration";
		document.forms[1].action="/EditPoliciesSearchAction.do";
		document.forms[1].submit();
	}//end of onConfiguration()
	
	function stopPreAuthClaim()
	{
		if(document.forms[1].stopPreAuth.value=="Y")
		    document.forms[1].stopPreAuthsYN.checked=true;
		else
			document.forms[1].stopPreAuthsYN.checked=false;
		if(document.forms[1].stopClaim.value=="Y")
			document.forms[1].stopClaimsYN.checked=true;
		else
			document.forms[1].stopClaimsYN.checked=false;
			//KOC 1286 for OPD Benefit
		
		if(document.forms[1].opdClaim.value=="Y")
			document.forms[1].opdClaimsYN.checked=true;
		else
			document.forms[1].opdClaimsYN.checked=false;
		
		//KOC 1286 for OPD Benefit
			// KOC 1270 for hospital cash benefit
		
		if(document.forms[1].cashBenefit.value=="Y")
		{
			document.forms[1].cashBenefitYN.checked=true;
		}
		else
		{
			document.forms[1].cashBenefitYN.checked=false;
		}
		if(document.forms[1].convCashBenefit.value=="Y") 
			document.forms[1].convCashBenefitYN.checked=true;
		else
			document.forms[1].convCashBenefitYN.checked=false;
		
		// KOC 1270 for hospital cash benefit
	}//end of stopPreAuthClaim()

       function memberBuffer()
	{
		if(document.forms[1].memberBuffer.value=="Y")
		    document.forms[1].memberBufferYN.checked=true;
		else
			document.forms[1].memberBufferYN.checked=false;
	}
	   //added for KOC-1273
       function criticalBenefit()
       {
    	   if(document.forms[1].criticalBenefit.value=="Y")
   		    document.forms[1].criticalBenefitYN.checked=true;
   		else
   			document.forms[1].criticalBenefitYN.checked=false;
       }
       function survivalPeriod()
       {
       	if(document.forms[1].survivalPeriod.value=="Y")
       		document.forms[1].survivalPeriodYN.checked=true;
       	else
       		document.forms[1].survivalPeriodYN.checked=false;
       	
       }
       
       //OPD_4_hosptial 
       function showhideheealthType(selObj)
       { 
    	   var selVal=document.getElementById("healthCheckType").value;
    	   if(selVal == 'TPA')
    	    {
    	        
    	          document.getElementById("paymentto").style.display="";
    	          
    	    }
    	    else
    	    {
    	
    	          document.getElementById("paymentto").style.display="none";
    	    }
       }
       
       
     //added for hyundaiBuffer on 19.05.2014 by Rekha  
       function stopHRApp()
       {
       
       		if(document.forms[1].hrApp.value=="Y")  
       		{
       			document.forms[1].hrAppYN.checked=true;
       		}
       		else
       		{
       			document.forms[1].hrAppYN.checked=false;
       		}
       
       }//end of stopHRApp()
       
      
       //end  for hyundaiBuffer on 19.05.2014 by Rekha  
       //OPD_4_hosptial
       
       function validateCopay(selObj,cId) {
    		if(cId==="MT"){
    			if(selObj.value==="Y"){
    				document.forms[1].maternityCopay.readOnly=false;
    				document.forms[1].maternityCopay.style.backgroundColor="white";
    				document.forms[1].maternityCopay.style.color="black"; 
    			}else{
    				document.forms[1].maternityCopay.readOnly=true;
    				document.forms[1].maternityCopay.style.backgroundColor="#EEEEEE";
    				document.forms[1].maternityCopay.style.color="#666666";
    			    document.forms[1].maternityCopay.value=""; 
    			}
    		}else if(cId==="OT"){
    			if(selObj.value==="Y"){
    				document.forms[1].opticalCopay.readOnly=false;
    				document.forms[1].opticalCopay.style.backgroundColor="white";
    				document.forms[1].opticalCopay.style.color="black"; 
    			}else{
    				document.forms[1].opticalCopay.readOnly=true;
    				document.forms[1].opticalCopay.style.backgroundColor="#EEEEEE";
    				document.forms[1].opticalCopay.style.color="#666666"; 
    				document.forms[1].opticalCopay.value=""; 
    			}
    		}else if(cId==="DT"){
    			if(selObj.value==="Y"){
    				document.forms[1].dentalCopay.readOnly=false;
    				document.forms[1].dentalCopay.style.backgroundColor="white";
    				document.forms[1].dentalCopay.style.color="black"; 
    			}else{
    				document.forms[1].dentalCopay.readOnly=true;
    				document.forms[1].dentalCopay.style.backgroundColor="#EEEEEE";
    				document.forms[1].dentalCopay.style.color="#666666"; 
    				document.forms[1].dentalCopay.value=""; 
    			}
    		}
    	}  
       
    function onGeneratemMemReport()
   	{
    	var memberStatus = document.forms[1].memberStatus.value;
    	var policySeqID = document.forms[1].polSeqID.value;
   		parameterValue =policySeqID;
   		var partmeter = "?mode=doGenerateAdminPolMemList&parameter="+parameterValue+"&fileName=generalreports/RenewalMembersXL.jrxml&reportID=GenPolicyRenMemXLs&reportType=EXL"+"&memberStatus="+memberStatus;
   		var openPage = "/ReportsAction.do"+partmeter;
   		var w = screen.availWidth - 10;
   		var h = screen.availHeight - 70;
   		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   	//	document.forms[1].mode.value="doGenerateAdminPolMemList";
   		window.open(openPage,'',features);
   	}
    
    function onClose()
    {
    	  document.forms[1].mode.value="doClose";
		  document.forms[1].action="/EditPoliciesSearchAction.do";
		  document.forms[1].submit();
    }
    
    function showAndHideDateCashless(){
    	var elementcheckedornot = document.getElementById('stopPreAuthsYN');
    	if(elementcheckedornot.checked){
    		document.getElementById("stopCashlessDateId").style.display="";
    	}else{
    		document.getElementById("stopCashlessDateId").style.display="none";
    		document.getElementById("stopcshlessid").value="";
    	}
     }
     
     function showAndHideDateClaims(){
    	 
    		var elementcheckedornot = document.getElementById('stopClaimsYN');
    		if(elementcheckedornot.checked){
    			document.getElementById("stopClaimsDateId").style.display="";
    		}else{
    			document.getElementById("stopClaimsDateId").style.display="none";
    			document.getElementById("stopclmsid").value="";
    		}
    	 }

	function mailNotification()
    	{
 	  	 if(document.forms[1].mailNotificationhiddenYN.value=="Y")
		    document.forms[1].mailNotificationYN.checked=true;
		else
			document.forms[1].mailNotificationYN.checked=false;
    	}

    function onChangereinsurerName()
	{
		if(!JS_SecondSubmit){
		        document.forms[1].mode.value="doChangeTrity";
			    document.forms[1].action="/SavePoliciesAction.do";
		 JS_SecondSubmit=true;
		document.forms[1].submit();
		}
	}