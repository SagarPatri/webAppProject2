
function onCloseReportWindow()
{
	document.forms[1].mode.value="doClose";
	document.forms[1].action.value="/AuditReportAction.do";
	document.forms[1].submit();
}//end of onClose()


function onDefaultGenerateReport(){
	document.forms[1].mode.value="doDefaultGenerateReport";
	document.forms[1].action.value="/AuditReportAction.do";
	document.forms[1].submit();
}
function onAuditResultUpload(){
	document.forms[1].mode.value="doAuditResultUpload";
	document.forms[1].action.value="/AuditReportAction.do";
	document.forms[1].submit();
}
/*function onAuditResultUpload(){
	document.forms[1].mode.value="doAuditResultUpload";
	document.forms[1].action.value="/AuditReportAction.do";
	document.forms[1].submit();
}*/
function onGenerateAuditReport()
{
		trimForm(document.forms[1]);
		var validateElem=checkFormData(document.forms[1]);
		if(validateElem){
			var switchType=document.forms[1].switchType.value;
			var inputParam="";
			if('CLM'==switchType){
					inputParam="&auditStatus="+document.forms[1].auditStatus.value+"&sProviderName="+document.forms[1].sProviderName.value+"&sBatchNO="+document.forms[1].sBatchNO.value+"&sClaimNO="+document.forms[1].sClaimNO.value+"&sSettlementNO="+document.forms[1].sSettlementNO.value
					+"&sClaimantName="+document.forms[1].sClaimantName.value+"&sEnrollmentId="+document.forms[1].sEnrollmentId.value+"&sStatus="+document.forms[1].sStatus.value+"&sPolicyNumber="+document.forms[1].sPolicyNumber.value+"&claimRecvdFrmDate="+document.forms[1].claimRecvdFrmDate.value
					+"&claimRecvdToDate="+document.forms[1].claimRecvdToDate.value+"&claimProcessedFrmDate="+document.forms[1].claimProcessedFrmDate.value+"&claimProcessedToDate="+document.forms[1].claimProcessedToDate.value+"&sClaimType="+document.forms[1].sClaimType.value
					+"&claimTreatmentFrmDate="+document.forms[1].claimTreatmentFrmDate.value+"&claimTreatmentToDate="+document.forms[1].claimTreatmentToDate.value;
				}else{
				inputParam="&preauthNumber="+document.forms[1].preauthNumber.value+"&sProviderName="+document.forms[1].sProviderName.value+"&authorizationNo="+document.forms[1].authorizationNo.value
				+"&sClaimantName="+document.forms[1].sClaimantName.value+"&sEnrollmentId="+document.forms[1].sEnrollmentId.value+"&sStatus="+document.forms[1].sStatus.value+"&sPolicyNumber="+document.forms[1].sPolicyNumber.value+"&preauthRecvdFrmDate="+document.forms[1].preauthRecvdFrmDate.value+"&preauthRecvdToDate="+document.forms[1].preauthRecvdToDate.value
				+"&preauthProcessedFrmDate="+document.forms[1].preauthProcessedFrmDate.value+"&preauthProcessedToDate="+document.forms[1].preauthProcessedToDate.value+"&preauthTreatmentFrmDate="+document.forms[1].preauthTreatmentFrmDate.value+"&preauthTreatmentToDate="+document.forms[1].preauthTreatmentToDate.value;
			}
				
			var openPage = "/GenAuditReportAction.do?mode=doGenerateAuditReport"+inputParam;
			var w = screen.availWidth - 10;
			var h = screen.availHeight - 99;
			var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
			window.open(openPage,'',features);
		}
	}


function showTemplate()
{
	var parameter = "?mode=doshowTemplate";
	var openPage = "/GenAuditReportAction.do"+parameter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 49;
	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}

function onUploadClaimAuditReport()
{
	var fileData=document.forms[1].resultUploadFile.value;
	if(fileData!=''&&fileData!=null){
		document.forms[1].logStartDate.value='';
		document.forms[1].logEndDate.value='';
		document.forms[1].mode.value="doUploadClaimAuditReport";
		document.forms[1].action = "/UploadAuditReportAction.do";
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
        	var openPage = "/GenAuditReportAction.do"+parameter;
        	var w = screen.availWidth - 10;
        	var h = screen.availHeight - 49;
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

