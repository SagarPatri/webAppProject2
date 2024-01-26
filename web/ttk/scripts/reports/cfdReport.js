function onDefaultGenerateReport(){
	document.forms[1].mode.value="doDefaultGenerateReport";
	document.forms[1].action.value="/CFDReportAction.do";
	document.forms[1].submit();
}

function onCFDResultUpload(){
	document.forms[1].mode.value="doCFDResultUpload";
	document.forms[1].action.value="/CFDReportAction.do";
	document.forms[1].submit();
}

function onCloseReportWindow()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].action.value="/CFDReportAction.do";
	document.forms[1].submit();
}//end of onClose()

function showTemplate()
{
	var parameter = "?mode=doshowTemplate";
	var openPage = "/GenCFDReportAction.do"+parameter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}

function onUploadCFDInvReport()
{
	var fileData=document.forms[1].resultUploadFile.value;
	if(fileData!=''&&fileData!=null){
		document.forms[1].logStartDate.value='';
		document.forms[1].logEndDate.value='';
		document.forms[1].mode.value="doUploadCFDInvestigationReport";
		document.forms[1].action = "/UploadCFDReportAction.do";
		document.forms[1].submit();	
	}else{
		alert('Please select the file to upload.');
	}	
}

function onSearchUploadLog()
{
	var startDate=document.forms[1].logStartDate.value;
	var endDate=document.forms[1].logEndDate.value;

    if(startDate=="")
	{
	alert("Please Enter Start Date");
	document.forms[1].logStartDate.focus();
	return false;
	}
	else if(document.forms[1].logEndDate.value=="")
	{
	alert("Please Enter End Date");
	document.forms[1].logEndDate.focus();
	return false;
	}else if(!formatValidation(document.forms[1].logStartDate,'Start Date')){
		document.forms[1].logStartDate.focus();
		return false;
	}else if(!formatValidation(document.forms[1].logEndDate,'End Date')){
		document.forms[1].logEndDate.focus();
		return false;
	}
	else if(compareDates("logStartDate","From Date","logEndDate","To Date","greater than")==false)
	{
		document.forms[1].logEndDate.value="";
	    return false;
	}
    
    var validateStart=validateStartDate(document.forms[1].logStartDate,'Start Date');
    if(!validateStart){
    	return false;
    }
    var validateEnd=validateStartDate(document.forms[1].logEndDate,'End Date');
    if(!validateEnd){
    	return false;
    }
    
	if(!isDate(document.forms[1].logStartDate,"From Date"))
   	{
   		document.forms[1].logStartDate.focus();
   		return false;
   	}
   	if(!isDate(document.forms[1].logEndDate,"To Date"))
   	{
   		document.forms[1].logEndDate.focus();
   		return false;
   	}
    if(!compareDates('logStartDate','From Date','logEndDate','To Date','greater than'))
    {
    	return false;
    }//end of if(!compareDates('sReportFrom','Report From','sReportTo','Report To','greater than'))

    var day1 = startDate.substring (0, startDate.indexOf ("/"));
    var month1 = startDate.substring (startDate.indexOf ("/")+1, startDate.lastIndexOf ("/"));
    var year1 = startDate.substring (startDate.lastIndexOf ("/")+1, startDate.length);

    var day2 = endDate.substring (0, endDate.indexOf ("/"));
    var month2 = endDate.substring (endDate.indexOf ("/")+1, endDate.lastIndexOf ("/"));
    var year2 = endDate.substring (endDate.lastIndexOf ("/")+1, endDate.length);

var    date1 = year1+"/"+month1+"/"+day1;
var    date2 = year2+"/"+month2+"/"+day2;
     firstDate = Date.parse(date1);
    secondDate= Date.parse(date2);
     msPerDay =  1000 * 3600 * 24;
  dbd = Math.ceil((secondDate.valueOf()-firstDate.valueOf())/ msPerDay) + 1;
    if(dbd > 90)
        {
        alert("Maximum No of Days allowed is 90");
        document.forms[1].logStartDate.focus();
        document.forms[1].logEndDate.focus();
        document.forms[1].logStartDate.value="";
        document.forms[1].logEndDate.value ="";
        return false;
        //document.forms[1].mode.value="";
        }else{
        	var parameter = "?mode=doSearchUploadLog"+"&logStartDate="+startDate+"&logEndDate="+endDate;
        	var openPage = "/GenCFDReportAction.do"+parameter;
        	var w = screen.availWidth - 10;
        	var h = screen.availHeight - 49;
        	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
        	window.open(openPage,'',features);
        }
    
	
}

function onGenerateCFDReport()
{
		trimForm(document.forms[1]);
		/*var validateElem=checkFormData(document.forms[1]);
		if(validateElem){*/	
		if((document.forms[1].sClaimPreauthNO.value!=""  || document.forms[1].sAuthorizationNo.value!="" ||
				document.forms[1].sBatchNO.value!="" || document.forms[1].sClaimType.value!="" || 
				document.forms[1].sSettlementNO.value!="" || document.forms[1].sSubmissionType.value!="" || 
				document.forms[1].sInvoiceNo.value!="")&&document.forms[1].switchType.value=="")
			{
				alert("Please Select the Flag !!!");
				document.forms[1].switchType.focus();
				return false;
			}else if(document.forms[1].sAuthorizationNo.value!=""&&document.forms[1].switchType.value=="CLM"){
				alert("Please enter proper inputs to retrieve the requested information !!!");
				document.forms[1].switchType.focus();
				return false;
			}else if((document.forms[1].sBatchNO.value!="" || document.forms[1].sClaimType.value!="" || 
					document.forms[1].sSettlementNO.value!="" || document.forms[1].sSubmissionType.value!="" ||
					document.forms[1].sInvoiceNo.value!="")&&document.forms[1].switchType.value=="PAT"){
						alert("Please enter proper inputs to retrieve the requested information !!!");
						document.forms[1].switchType.focus();
						return false;
			}else if(!(document.forms[1].sClaimPreauthNO.value!="" ||	document.forms[1].switchType.value!=""||
					document.forms[1].sAuthorizationNo.value!="" || document.forms[1].sBatchNO.value!="" || 
					document.forms[1].sProviderName.value!="" || document.forms[1].sClaimType.value!="" || 
					document.forms[1].sInternalRemarkStatus.value!="" || document.forms[1].sSettlementNO.value!=""||
					document.forms[1].sPolicyNumber.value!="" || document.forms[1].sPartnerName.value!="" || 
					document.forms[1].sSubmissionType.value!="" || document.forms[1].sRiskLevel.value!="" || 
					document.forms[1].sEnrollmentId.value!="" ||  document.forms[1].sMemberName.value!="" ||
					document.forms[1].sBenefitType.value!="" || document.forms[1].sInvoiceNo.value!="" || 
					document.forms[1].sPolicyType.value!="" || document.forms[1].sClaimPreauthStatus.value!="" || 
					document.forms[1].sCFDInvestigationStatus.value!="" || document.forms[1].sCFDReceivedFromDate.value!="" || 
					document.forms[1].sCFDReceivedtoDate.value!="" )){
						alert("To generate the report please provide  at least one input   !!!");
						return false;
			}else if(document.forms[1].sMemberName.value!=""&&document.forms[1].sEnrollmentId.value=="")
				{
					alert("Please provide Alkoot ID to retrieve the requested information!");
					document.forms[1].switchType.focus();
					return false;
				}else{
						var	inputParam="&sClaimPreauthNO="+document.forms[1].sClaimPreauthNO.value
						+"&switchType="+document.forms[1].switchType.value
						+"&sAuthorizationNo="+document.forms[1].sAuthorizationNo.value
						+"&sBatchNO="+document.forms[1].sBatchNO.value
						+"&sProviderName="+document.forms[1].sProviderName.value
						+"&sClaimType="+document.forms[1].sClaimType.value
						+"&sInternalRemarkStatus="+document.forms[1].sInternalRemarkStatus.value
						+"&sSettlementNO="+document.forms[1].sSettlementNO.value
						+"&sPolicyNumber="+document.forms[1].sPolicyNumber.value
						+"&sPartnerName="+document.forms[1].sPartnerName.value
						+"&sSubmissionType="+document.forms[1].sSubmissionType.value
						+"&sRiskLevel="+document.forms[1].sRiskLevel.value
						+"&sEnrollmentId="+document.forms[1].sEnrollmentId.value
						+"&sMemberName="+document.forms[1].sMemberName.value
						+"&sBenefitType="+document.forms[1].sBenefitType.value
						+"&sPolicyType="+document.forms[1].sPolicyType.value
						+"&sClaimPreauthStatus="+document.forms[1].sClaimPreauthStatus.value
						+"&sCFDInvestigationStatus="+document.forms[1].sCFDInvestigationStatus.value
						+"&sCFDReceivedFromDate="+document.forms[1].sCFDReceivedFromDate.value
						+"&sCFDReceivedtoDate="+document.forms[1].sCFDReceivedtoDate.value;
						var openPage = "/GenCFDReportAction.do?mode=doGenerateCFDReport"+inputParam;
						var w = screen.availWidth - 10;
						var h = screen.availHeight - 99;
						var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
						window.open(openPage,'',features);
				}
		}











function onSwitch()
{
	document.forms[1].mode.value="doSwitchTo";
	document.forms[1].action = "/AuditReportAction.do";
	document.forms[1].submit();
}


function checkFormData(objForm)
{
	var count=0;
	var switchType=objForm.switchType.value;
	/*if(switchType=='CLM' &&  objForm.auditStatus.value==''){
		alert('Please select the Audit status');
		return false;
	}*/
	for(var i=0;i<objForm.elements.length;i++)
	{
		field=objForm.elements[i];
		if(field.type == 'text' || field.type == 'select' ||field.type == 'select-one'){
			if((field.name!='sClaimantName' && field.name!='switchType')&&(field.value == '')){
				count++;
			}
		}	
	}
	if(switchType=='CLM'&&count==15){
		alert("Please select minimum any one search category except 'Member Name'");
		return false;
	}else if(switchType=='CLM'){
		var auditStatus=objForm.auditStatus.value;
		var sStatus=objForm.sStatus.value;
		var sClaimType=objForm.sClaimType.value;
		var sClaimantName=objForm.sClaimantName.value;
		var sPolicyNumber=objForm.sPolicyNumber.value;
		if((auditStatus!=''&&sStatus!=''&&sClaimType!='') && count>=12){
			alert('Please select one more category');
			return false;
		}
		if(((auditStatus!=''&&sStatus!='')||(auditStatus!=''&&sClaimType!='')) && count>=13){
			alert('Please select one more category');
			return false;
		}
		if(auditStatus!='' &&sClaimantName!='' && count>=14){
			alert('Please select one more category');
			return false;
		}
		if(sPolicyNumber!=''&& count>=14){
			alert('Please select one more category');
			return false;
		}
		if(auditStatus!=''&& count>=13){
			if(sPolicyNumber!='' && count>=13){
				alert('Please select one more category');
				return false;
			}else if(sPolicyNumber=='' && count>13){
				alert('Please select one more category');
				return false;
		}else{
			return true;
		}
		}
		if(validateStartDate(objForm.claimRecvdFrmDate,'Claim received from Date')){
			if(formatValidation(objForm.claimRecvdFrmDate,'Claim received from Date')){
				if(validateEndDate(objForm.claimRecvdToDate,objForm.claimRecvdFrmDate,'Claim received to Date','Claim received from Date')){
					if(formatValidation(objForm.claimRecvdToDate,'Claim received to Date')){
						if(validateStartDate(objForm.claimProcessedFrmDate,'Claim processed from Date')){
							if(formatValidation(objForm.claimProcessedFrmDate,'Claim processed from Date')){
								if(validateEndDate(objForm.claimProcessedToDate,objForm.claimProcessedFrmDate,'Claim processed to Date','Claim processed from Date')){
									if(formatValidation(objForm.claimProcessedToDate,'Claim processed to Date')){
										if(validateStartDate(objForm.claimTreatmentFrmDate,'Date of Treatment from Date')){
											if(formatValidation(objForm.claimTreatmentFrmDate,'Date of Treatment from Date')){
												if(validateEndDate(objForm.claimTreatmentToDate,objForm.claimTreatmentFrmDate,'Date of Treatment to Date','Date of Treatment from Date')){
													if(formatValidation(objForm.claimTreatmentToDate,'Date of Treatment to Date')){
														return true;
													}else
														return false;
												}else
													return false;
											}else
												return false;
												
										}else
											return false;
									}else
										return false;
									
								}else
									return false;
							}
							else
								return false;
						}else
							return false;
					}
					else
						return false;	
				}else
					return false;
			}
			else
				return false;	
		}
			else
			return false;
	}
	if(switchType=='PAT'&&count>=12){
		alert("Please select minimum any one search category except 'Member Name'");
		return false;
	}else if(switchType=='PAT'){
		var sStatus=objForm.sStatus.value;
		var sPolicyNumber=objForm.sPolicyNumber.value;
		if((sStatus!='') && count>10){
			alert('Please select one more category');
			return false;
		}
		if(sPolicyNumber!=''&& count>10){
			alert('Please select one more category');
			return false;
		}
		if(validateStartDate(objForm.preauthRecvdFrmDate,'Pre-approval received from Date')){
			if(formatValidation(objForm.preauthRecvdFrmDate,'Pre-approval received from Date')){
				if(validateEndDate(objForm.preauthRecvdToDate,objForm.preauthRecvdFrmDate,'Pre-approval received to Date','Pre-approval received from Date')){
					if(formatValidation(objForm.preauthRecvdToDate,'Pre-approval received to Date')){
						if(validateStartDate(objForm.preauthProcessedFrmDate,'Pre-approval processed from Date')){
							if(formatValidation(objForm.preauthProcessedFrmDate,'Pre-approval processed from Date')){
								if(validateEndDate(objForm.preauthProcessedToDate,objForm.preauthProcessedFrmDate,'Pre-approval processed to Date','Pre-approval processed from Date')){
									if(formatValidation(objForm.preauthProcessedToDate,'Pre-approval processed to Date')){
										if(validateStartDate(objForm.preauthTreatmentFrmDate,'Date of Treatment from Date')){
											if(formatValidation(objForm.preauthTreatmentFrmDate,'Date of Treatment from Date')){
												if(validateEndDate(objForm.preauthTreatmentToDate,objForm.preauthTreatmentFrmDate,'Date of Treatment to Date','Date of Treatment from Date')){
													if(formatValidation(objForm.preauthTreatmentToDate,'Date of Treatment to Date')){
														return true;
													}else
														return false;	
												}else
													return false;
											}else
												return false;
											
										}else
											return false;
									}else
										return false;
											
								}else
									return false;
							}else
								return false;
									
						}else
							return false;
					}else
						return false;
					
				}else
					return false;
			}else
				return false;	
		}
		else
		return false;
	}
}


function isFutureDate(argDate){
	var dateArr=argDate.split("/");	
	var givenDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
	var currentDate = new Date();
	if(givenDate>currentDate){
	return true;
	}
	return false;
}


function validateStartDate(element,elementMsg)
{
	var startDate='';
	if(element){
		startDate=element.value;
		var validate=isFutureDate(startDate);
		if(validate){
			alert("\'"+elementMsg+"\'"+ " cannot be future date.");
			element.value='';
			element.focus();
			return false;
		}else
			return true;
	}else{
		return true;
	}
		
}

function validateEndDate(currentElement,prevElement,toElementMsg,frmElementMsg)
{
	var endDate='';
	if(currentElement){
		endDate=currentElement.value;
		var validate=isFutureDate(endDate);
		if(validate){
			alert("\'"+frmElementMsg+"\'"+ " cannot be future date.");
			currentElement.value='';
			currentElement.focus();
			return false;
		}else{
			var endDateParts=endDate.split('/');
			var stDateValue='';
			if(prevElement){
				stDateValue=prevElement.value;
			}
			if(stDateValue!='' && endDate!=''){
				var startDateParts=stDateValue.split('/');
				var valid=checkDate(startDateParts,endDateParts,frmElementMsg,toElementMsg);
				if(valid){
					return true;
				}else{
					currentElement.value='';
					currentElement.focus();
					return false;
				}
				//return false;
			}
			else{
				/*if(endDate!=''){
					alert('Please select '+"\'"+toElementMsg+"\'");
					prevElement.value='';
					prevElement.focus();
					currentElement.value='';
					currentElement.focus();
					return false;
				}else*/
					return true;
			}
		}
	}else{
		return true;
	}
}

function checkDate(stParts,endParts,startElem,endElem){
	var startDate = new Date(stParts[2], stParts[1] - 1, stParts[0]);
	var endDate = new Date(endParts[2], endParts[1] - 1, endParts[0]);
if(startDate>endDate){
	alert("Please Provide "+"\'"+endElem+"\'"+" Greater Than "+"\'"+startElem+"\'"+"!!!");
	return false;
}else{
	var oneDay = 24*60*60*1000; 
	var diffDays = Math.round(Math.abs((startDate.getTime() - endDate.getTime())/(oneDay)));
		if(diffDays > 90){
			alert("Allowed duration for "+"\'"+startElem+"\'"+" and "+"\'"+endElem+"\'"+" upto 90 days only!!!");
			return false;
		}else{
			return true;
		}
		return false;
	}
}
function formatValidation(element,elementMsg){
	var dateValue='';
	if(element){
		dateValue=element.value;
		if(dateValue!=''){ 
			if(!isDate(element,"\'"+elementMsg+"\'")){
				element.value='';
				element.focus();
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
}

function onChangeFlag(){
	document.forms[1].mode.value="doChangeFlag";
	document.forms[1].action.value="/CFDReportAction.do";
	document.forms[1].submit();
}

function onGenerateCampaginReport(){
	if(document.getElementById("campReceivedFromDate").value != "")
	{
		if(isDate(document.getElementById("campReceivedFromDate")," Campaign Received from Date ")==false)
		{
			document.getElementById("campReceivedFromDate").focus();
			return false;
		}
	}
	
	if(document.getElementById("campReceivedToDate").value != "")
	{
		if(isDate(document.getElementById("campReceivedToDate")," Campaign Received to Date ")==false)
		{
			document.getElementById("campReceivedToDate").focus();
			return false;
		}
	}
	
	if(document.getElementById("campStartDate").value != "")
	{
		if(isDate(document.getElementById("campStartDate")," Campaign Start Date ")==false)
		{
			document.getElementById("campStartDate").focus();
			return false;
		}
	}
	
	if(document.getElementById("campEndDate").value != "")
	{
		if(isDate(document.getElementById("campEndDate")," Campaign End Date ")==false)
		{
			document.getElementById("campEndDate").focus();
			return false;
		}
	}
	
	// 1
	if( !((document.getElementById("campReceivedFromDate").value)==="") && !((document.getElementById("campReceivedToDate").value)===""))
	{
		var startDate =document.getElementById("campReceivedFromDate").value;    	
		var endDate=document.getElementById("campReceivedToDate").value;	
		
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
			alert("Campaign Received to Date should be greater than or equal to Campaign Received from Date.");
			document.getElementById("campReceivedToDate").value="";
			document.getElementById("campReceivedToDate").focus();
			return ;
		 }
	}
	
	// 2
	if( !((document.getElementById("campStartDate").value)==="") && !((document.getElementById("campEndDate").value)===""))
	{
		var startDate =document.getElementById("campStartDate").value;    	
		var endDate=document.getElementById("campEndDate").value;	
		
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
			alert("Campaign End Date should be greater than or equal to Campaign Start Date.");
			document.getElementById("campEndDate").value="";
			document.getElementById("campEndDate").focus();
			return ;
		 }
	}
	var campName = document.getElementById("campName").value;
	var flag = document.getElementById("flag").value;
	var sProviderName = document.getElementById("sProviderName").value;
	var campStatus = document.getElementById("campStatus").value;
	var campReceivedFromDate = document.getElementById("campReceivedFromDate").value;
	var campReceivedToDate = document.getElementById("campReceivedToDate").value;
	var campStartDate = document.getElementById("campStartDate").value;
	var campEndDate = document.getElementById("campEndDate").value;
	
	var partmeter ="?mode=doGenerateCampaginReport&campName="+campName+"&flag="+flag+"&sProviderName="+sProviderName+"&campStatus="+campStatus+"&campReceivedFromDate="+campReceivedFromDate+"&campReceivedToDate="+campReceivedToDate+"&campStartDate="+campStartDate+"&campEndDate="+campEndDate;
	var openPage = "/GenCFDReportAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}