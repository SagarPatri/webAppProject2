//as per intX 
var xhttp	=null;
function edit(rownum)
{
	 //document.forms[1].leftlink.value ="Claims";
	document.forms[1].tab.value ="Details";
    document.forms[1].mode.value="doView";
    document.forms[1].rownum.value=rownum;   
    document.forms[1].action = "/OnlineClmSearchHospAction.do";
    document.forms[1].submit();
}//end of edit(rownum)



function toggle(sortid)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/OnlineClmSearchHospAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)

//function to display the selected page
function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/OnlineClmSearchHospAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/OnlineClmSearchHospAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/OnlineClmSearchHospAction.do";
    document.forms[1].submit();
}//end of PressForward()

function onSearch()
{
   if(!JS_SecondSubmit)
 {
	trimForm(document.forms[1]);
	document.forms[1].mode.value = "doSearch";
	document.forms[1].action = "/OnlineClmSearchHospAction.do";
	JS_SecondSubmit=true
	document.forms[1].submit();
  }//end of if(!JS_SecondSubmit)
}//end of onSearch()


function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/OnlineClmSearchHospAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()

function onValidateVidalId()
{
	document.forms[1].mode.value = "doValidateEnrollId";
	document.forms[1].action = "/OnlinePreAuthEnrollValidate.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
}//end of onValidateVidalId()

function onUserSubmit()
{
	document.forms[1].tab.value ="Pre-Authorization";
	document.forms[1].sublink.value ="Pre-Authorization";
	document.forms[1].leftlink.value ="Approval Claims";
	document.forms[1].mode.value = "doSaveGeneral";
	document.forms[1].action = "/OnlinePreAuthAction.do";
	document.forms[1].submit();
}

function onAddDiags(obj)
{
	if(document.forms[1].icdCode.value=="")
	{
		alert("Please Enter Diagnosys Code First");
		document.forms[1].icdCode.focus();
		return false;
	}else if(document.forms[1].icdDesc.value=="")
	{
		alert("Please Enter Diagnosys Description");
		document.forms[1].icdDesc.focus();
		return false;
	}else if(daysRangeValidation(document.forms[1].treatmentDate.value)){
		alert("Back Date allowed upto 3 Days only for Date of Treatment/Admission !!! ");
		document.forms[1].treatmentDate.value='';
		document.forms[1].treatmentDate.focus();
		return false;
	}
	
	if(document.getElementById("icdCountid") !=  null){
		var icdCountSize = document.getElementById("icdCountid").value;
	if(document.forms[1].icdCode.value!="")
	{
		var icdformValue =  document.forms[1].icdCode.value;
		for(i=0; i<icdCountSize;i++){
		var savedicdValue =	document.getElementById("icdcodeid"+i).textContent;
		if(icdformValue==savedicdValue){
			alert("Diagnosis code already Exists!");
			return false;
		}
		}
	  }
	}
	document.forms[1].mode.value = "doSaveDiags";
	document.forms[1].action = "/OnlinePreAuthAction.do?focusId="+obj;
	document.forms[1].submit();
}


function deleteDiagnosisDetails(rownum){
 if(confirm("Are You Sure You Want To Delete Diagnosis Details!")){
if(!JS_SecondSubmit){	
	document.forms[1].rownum.value=rownum;
   document.forms[1].mode.value="deleteDiagnosisDetails";
   document.forms[1].action="/OnlinePreAuthAction.do";	
   JS_SecondSubmit=true;	   
   document.forms[1].submit();
 }	
 }	
}

function deleteDrugDetails(rownum){
 if(confirm("Are You Sure You Want To Delete Drug Details!")){
if(!JS_SecondSubmit){	
	document.forms[1].rownum.value=rownum;
   document.forms[1].mode.value="deleteDrugDetails";
   document.forms[1].action="/OnlinePreAuthAction.do";	
   JS_SecondSubmit=true;	   
   document.forms[1].submit();
 }	
 }	
}

function addDiagnosisDetails(){
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doAddDiagnosisDetails";
    document.forms[1].action = "/OnlinePreAuthAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}

function addActivityDetails(){
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doAddActivityDetails";
    document.forms[1].action = "/OnlinePreAuthAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}

function addDrugDetails(){
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doAddDrugDetails";
    document.forms[1].action = "/OnlinePreAuthAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}

function onSaveActivities(obj){
	var serviceType	=	"";
	  if((document.getElementById("benefitTemp").value=="IMTI" || document.getElementById("benefitTemp").value=="In-Patient Maternity")
			  || (document.getElementById("benefitTemp").value=="IPT" || document.getElementById("benefitTemp").value=="In-Patient")){
		  serviceType	=	document.forms[1].activityServiceType.value;
		  if(serviceType=="")
			{
				alert("Please Select Service Type");
				document.forms[1].activityServiceType.focus();
				return false;
			}
	  }

	
	if("SCD"==serviceType){
		if(document.forms[1].activityServiceCode.value=="")
		{
			alert("Please Enter Service Code First");
			document.forms[1].activityServiceCode.focus();
			return false;
		}else if(document.forms[1].activityServiceCodeDesc.value=="")
		{
			alert("Please Enter Service Description");
			document.forms[1].activityServiceCodeDesc.focus();
			return false;
		}else if(document.forms[1].activityQuantity.value=="")
		{
			alert("Please Enter Quantity");
			document.forms[1].activityQuantity.focus();
			return false;
		}
		
	}else{
		if(document.forms[1].activityCode.value=="")
		{
			alert("Please Enter Activity Code First");
			document.forms[1].activityCode.focus();
			return false;
		}else if(document.forms[1].activityCodeDesc.value=="")
		{
			alert("Please Enter Activity Description");
			document.forms[1].activityCodeDesc.focus();
			return false;
		}else if(document.forms[1].activityQuantity.value=="")
		{
			alert("Please Enter Quantity");
			document.forms[1].activityQuantity.focus();
			return false;
		}
	}
	
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doSaveActivities";
    document.forms[1].action = "/OnlinePreAuthAction.do?focusId="+obj+"&tariffSeqId="+document.forms[1].tariffSeqId.value;
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}
function onSaveDrugs(){
	if(!JS_SecondSubmit){
		if(document.forms[1].drugCode.value=="")
		{
			alert("Please Enter Drug Code First");
			document.forms[1].drugCode.focus();
			return false;
		}else if(document.forms[1].drugDesc.value=="")
		{
			alert("Please Enter Drug Description");
			document.forms[1].drugDesc.focus();
			return false;
		}else if(document.forms[1].drugdays.value=="")
		{
			alert("Please Enter Days");
			document.forms[1].drugdays.focus();
			return false;
		}else if(document.forms[1].drugUnit.value=="")
		{
			alert("Please Enter Drug Unit");
			document.forms[1].drugUnit.focus();
			return false;
		}else if(document.forms[1].drugquantity.value=="")
		{
			alert("Please Enter Drug Quantity");
			document.forms[1].drugquantity.focus();
			return false;
		}
		
 		var noOfUnits=document.forms[1].noOfUnits.value;
 		var granularUnit=document.forms[1].granularUnit.value;
 		var quantity = document.getElementById("drugquantity").value;
 		if(noOfUnits != "" && quantity != "")
 		{	
 					if(granularUnit == "")
 					{
 						if(parseFloat(quantity) > parseFloat(noOfUnits))
 						{
 							var msg = confirm("Requested quantity is more than the usual prescribed.\n"+ 
 									          "Do you want to continue submission" );
 								if(msg != true)
 								{
 									document.getElementById("drugquantity").focus();
 									return false;
 								}
 						}
 					}
 					if(granularUnit != "")
 					{
 						var unitType = document.getElementById("drugUnit").value;
 						if(unitType == "PCKG")
 						{
 								res = parseFloat(quantity) * parseFloat(granularUnit);
 								if(parseFloat(res) > parseFloat(noOfUnits))
 								{
 									var msg = confirm("Requested quantity is more than the usual prescribed.\n"+ 
 													  "Do you want to continue submission" );
 										if(msg != true)
 										{
 											document.getElementById("drugquantity").focus();
 											return false;
 										}
 								}
 						}
 						else {
 								if(parseFloat(quantity) > parseFloat(noOfUnits))
 								{
 									var msg = confirm("Requested quantity is more than the usual prescribed.\n"+ 
								                          "Do you want to continue submission" );
 									if(msg != true)
 									{
 										document.getElementById("drugquantity").focus();
 										return false;
 									}
 								}	
 							}
 					}	
 	}
	document.forms[1].mode.value="doSaveDrugs";
    document.forms[1].action = "/OnlinePreAuthAction.do?focusId=drugDesc";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}
function deleteActivityDetails(rownum){
	 if(confirm("Are You Sure You Want To Delete Activity Details!")){
	if(!JS_SecondSubmit){	
		document.forms[1].rownum.value=rownum;
	   document.forms[1].mode.value="deleteActivityDetails";
	   document.forms[1].action="/OnlinePreAuthAction.do";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
	 }	
	 }	
	}
function onSaveOnlinePreAuth(){
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doSaveOnlinePreAuth";
    document.forms[1].action = "/OnlinePreAuthAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}
function onSavePartialPreAuth(){
	if(!JS_SecondSubmit){
		
	if((document.getElementById("benefitTemp").value=="DNTL" || document.getElementById("benefitTemp").value=="Dental"))
	{
		if(document.getElementById("overJet").value!='' && 
				(document.getElementById("openbiteAntrio").value=='' && document.getElementById("openbitePosterior").value=='' 
					&& document.getElementById("openbiteLateral").value=='')){
			alert("Overjet and Open Bite should be billed together");
			return false;
		}
		if(document.getElementById("overJet").value=='' && 
				(document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!='' 
					|| document.getElementById("openbiteLateral").value!='')){
			alert("Open Bite and Overjet should be billed together");
			return false;
		}
	}
	document.forms[1].mode.value="doSavePartialPreAuth";
    document.forms[1].action = "/OnlinePreAuthAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}
function onSavePartialPreAuthEnhance(){
	if(!JS_SecondSubmit){
		if((document.getElementById("benefitTemp").value=="DNTL" || document.getElementById("benefitTemp").value=="Dental"))
		{
			if(document.getElementById("overJet").value!='' && 
					(document.getElementById("openbiteAntrio").value=='' && document.getElementById("openbitePosterior").value=='' 
						&& document.getElementById("openbiteLateral").value=='')){
				alert("Overjet and Open Bite should be billed together");
				return false;
			}
			if(document.getElementById("overJet").value=='' && 
					(document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!='' 
						|| document.getElementById("openbiteLateral").value!='')){
				alert("Open Bite and Overjet should be billed together");
				return false;
			}
		}
	document.forms[1].mode.value="doSavePartialPreAuth";
    document.forms[1].action = "/OnlinePreAuthEnhanceAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}


function onSubmitOnlinePreAuth(){
	var document1= document.getElementById("fileid1").value;
	var document2= document.getElementById("fileid2").value;
	var document3= document.getElementById("fileid3").value;
	var document4= document.getElementById("fileid4").value;
	var description= document.getElementById("descriptionid").value;
	var description2= document.getElementById("descriptionid2").value;
	var description3= document.getElementById("descriptionid3").value;
	var description4= document.getElementById("descriptionid4").value;
	var sumofallfilesize = 0;
	var maxfilesize = 8*1024*1024;
	if(!JS_SecondSubmit){
		if(document1 != ""){
			var file1Size = document.getElementById('fileid1').files[0].size;
		    var file1Name = document.getElementById('fileid1').files[0].name;
		    sumofallfilesize = sumofallfilesize+file1Size;
		    if(description==""){
				alert("upload document description1 is required");
				if(document1 !=""){
					document.getElementById("fileid1").value="";
				}
				return;
			}
		   
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		 
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File1 Format should be xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid1").value="";
		    	return;
		    }
		    
		  }

		if(document2 != ""){
			var file1Size = document.getElementById('fileid2').files[0].size;
		    var file1Name = document.getElementById('fileid2').files[0].name;
		    sumofallfilesize = sumofallfilesize+file1Size;
		    if(description2==""){
				alert("upload document description2 is required");
					document.getElementById("fileid2").value="";
				return;
			}
		    
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		  
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File2 Format should be xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid2").value="";
		    	return;
		    }
		    
		  }

		if(document3 != ""){
			
			var file1Size = document.getElementById('fileid3').files[0].size;
		    var file1Name = document.getElementById('fileid3').files[0].name;
		    sumofallfilesize = sumofallfilesize+file1Size;
		    if(description3==""){
				alert("upload document description3 is required");
					document.getElementById("fileid3").value="";
				return;
			}
		   
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File3 Format should me xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid3").value="";
		    	return;
		    }
		    
		  }

		if(document4 != ""){
			var file1Size = document.getElementById('fileid4').files[0].size;
		    var file1Name = document.getElementById('fileid4').files[0].name;
		    sumofallfilesize = sumofallfilesize + file1Size;
		    if(description4==""){
				alert("upload document description4 is required");
				document.getElementById("fileid4").value="";
				return;
			}
		    
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		  
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File4 Format should be xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid4").value="";
		    	return;
		    }
		    
		  }
		
		if(sumofallfilesize > maxfilesize){
			alert("files size should not be more than 8MB.");
			document.getElementById("fileid1").value="";
			document.getElementById("fileid2").value="";
			document.getElementById("fileid3").value="";
			document.getElementById("fileid4").value="";
			return;
		}
		if((document.getElementById("benefitTemp").value=="DNTL" || document.getElementById("benefitTemp").value=="Dental"))
		{
			if(document.getElementById("overJet").value!='' && 
					(document.getElementById("openbiteAntrio").value=='' && document.getElementById("openbitePosterior").value=='' 
						&& document.getElementById("openbiteLateral").value=='')){
				alert("Overjet and Open Bite should be billed together");
				return false;
			}
			if(document.getElementById("overJet").value=='' && 
					(document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!='' 
						|| document.getElementById("openbiteLateral").value!='')){
				alert("Open Bite and Overjet should be billed together");
				return false;
			}
		}
		
		if(document.getElementById("benefitTemp").value=="OPTS" || document.getElementById("benefitTemp").value=="Out-Patient"){
			var providernetPreauthAmount;
			var providernetPreauthAmountFloat;
			var totalcountfordiagoractivity=0;
			/*if(document.getElementById("diagnosisCountSumid")!=null || document.getElementById("activitytotalsumcountid") !=null){*/
				var diagnosiscountsum =0;
				var activitycountsum =0;
				var diagnosiscountsumint=0;
				var activitycountsumint = 0;
				var isTest=false;
				if(document.getElementById("diagnosisCountSumid")){
					diagnosiscountsum=document.getElementById("diagnosisCountSumid").value;
					isTest=true;
					diagnosiscountsumint = parseInt(diagnosiscountsum);
					totalcountfordiagoractivity = diagnosiscountsumint;
				}
				
				if(document.getElementById("activitytotalsumcountid")){
					activitycountsum=document.getElementById("activitytotalsumcountid").value;
					isTest=true;
					activitycountsumint = parseInt(activitycountsum);
					totalcountfordiagoractivity = activitycountsumint;
				}
				
				if(document.getElementById("diagnosisCountSumid") != null && document.getElementById("activitytotalsumcountid") !=null ){
					totalcountfordiagoractivity = diagnosiscountsumint+activitycountsumint;
				}
				
			/*}*/
			
			
			
			if(totalcountfordiagoractivity == 0 && isTest==true){
			if(document.getElementById("optspreauthavaillimitsid").value !="" && document.getElementById("optspreauthavaillimitsid").value !=null){
				var optspreauthavaillimits = document.getElementById("optspreauthavaillimitsid").value;
				var optspreauthavaillimitsfloat = parseFloat(optspreauthavaillimits);
				if(document.getElementById("netamountidactivity") != null && document.getElementById("netamountiddrug")!=null){
					providernetPreauthAmountForActivity = document.getElementById("netamountidactivity").value;
					providernetPreauthAmountForDrug = document.getElementById("netamountiddrug").value;
					var activitynetAmount = parseFloat(providernetPreauthAmountForActivity);
					var drugnetAmount = parseFloat(providernetPreauthAmountForDrug);
					providernetPreauthAmountFloat = activitynetAmount+drugnetAmount;
					if(optspreauthavaillimitsfloat>providernetPreauthAmountFloat){
						var isValid = optspreauthlimits();
						if(daysRangeValidation(document.forms[1].treatmentDate.value)){
							alert("Back Date allowed upto 3 Days only for Date of Treatment/Admission !!! ");
							document.forms[1].treatmentDate.value='';
							document.forms[1].treatmentDate.focus();
							return true;
						}
						if(!isValid){
							document.forms[1].mode.value="doSubmitOnlinePreAuth";
						    document.forms[1].action = "/OnlinePreAuthAction.do";    
							JS_SecondSubmit=true;
							document.forms[1].submit();
							return;
						}else{
							return false;
						  }
					}
					
				}else{
					if(document.getElementById("netamountidactivity") != null){
						providernetPreauthAmount = document.getElementById("netamountidactivity").value;
						providernetPreauthAmountFloat = parseFloat(providernetPreauthAmount);
						if(optspreauthavaillimitsfloat>providernetPreauthAmountFloat){
                         var isValid = optspreauthlimits();
                         if(daysRangeValidation(document.forms[1].treatmentDate.value)){
                        	 alert("Back Date allowed upto 3 Days only for Date of Treatment/Admission !!! ");
                        	 document.forms[1].treatmentDate.value='';
                     		document.forms[1].treatmentDate.focus();
                 			return true;
                 		}
							if(!isValid){
								document.forms[1].mode.value="doSubmitOnlinePreAuth";
							    document.forms[1].action = "/OnlinePreAuthAction.do";    
								JS_SecondSubmit=true;
								document.forms[1].submit();
								return;
							}else{
								return false;
							  }
						}
					}
					if(document.getElementById("netamountiddrug")!=null){
						providernetPreauthAmount = document.getElementById("netamountiddrug").value;
						providernetPreauthAmountFloat = parseFloat(providernetPreauthAmount);
						if(optspreauthavaillimitsfloat>providernetPreauthAmountFloat){
                        var isValid = optspreauthlimits();
                        if(daysRangeValidation(document.forms[1].treatmentDate.value)){
                        	alert("Back Date allowed upto 3 Days only for Date of Treatment/Admission !!! ");
                        	document.forms[1].treatmentDate.value='';
                    		document.forms[1].treatmentDate.focus();
                			return true;
                		}
							if(!isValid){
								document.forms[1].mode.value="doSubmitOnlinePreAuth";
							    document.forms[1].action = "/OnlinePreAuthAction.do";    
								JS_SecondSubmit=true;
								document.forms[1].submit();
								return;
							}else{
								return false;
							  }
						}
					}
				   }
			}
		}
			/*if(document.getElementById("optspreauthlimitsid").value !="" ){
		
			var optspreauthlimits = document.getElementById("optspreauthlimitsid").value;
			var optspreauthlimitsfloat = parseFloat(optspreauthlimits);
			
			if(document.getElementById("netamountidactivity") != null && document.getElementById("netamountiddrug")!=null){
				providernetPreauthAmountForActivity = document.getElementById("netamountidactivity").value;
				providernetPreauthAmountForDrug = document.getElementById("netamountiddrug").value;
				var activitynetAmount = parseFloat(providernetPreauthAmountForActivity);
				var drugnetAmount = parseFloat(providernetPreauthAmountForDrug);
				providernetPreauthAmountFloat = activitynetAmount+drugnetAmount;
				if(optspreauthlimitsfloat>providernetPreauthAmountFloat){
					alert("Preapproval is not required as the total net amount is less than "+optspreauthlimitsfloat);
					return false;
				}
			}else{
		if(document.getElementById("netamountidactivity") != null){
			providernetPreauthAmount = document.getElementById("netamountidactivity").value;
			providernetPreauthAmountFloat = parseFloat(providernetPreauthAmount);
			if(optspreauthlimitsfloat>providernetPreauthAmountFloat){
				alert("Preapproval is not required as the total net amount is less than "+optspreauthlimitsfloat);
				return false;
			}
		}
		if(document.getElementById("netamountiddrug")!=null){
			providernetPreauthAmount = document.getElementById("netamountiddrug").value;
			providernetPreauthAmountFloat = parseFloat(providernetPreauthAmount);
			if(optspreauthlimitsfloat>providernetPreauthAmountFloat){
				alert("Preapproval is not required as the total net amount is less than "+optspreauthlimitsfloat);
				return false;
			}
		}
	   }
	  }*/
	}if(daysRangeValidation(document.forms[1].treatmentDate.value)){
		alert("Back Date allowed upto 3 Days only for Date of Treatment/Admission !!! ");
		document.forms[1].treatmentDate.value='';
		document.forms[1].treatmentDate.focus();
		return true;
	}
		document.forms[1].mode.value="doSubmitOnlinePreAuth";
	    document.forms[1].action = "/OnlinePreAuthAction.do";    
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}

function calcQtyOnPosology()
{
	//var	posology	=	document.forms[1].posology.value;
	
	var drugDesc	=	document.forms[1].drugDesc.value;
	var	days		=	document.forms[1].drugdays.value;
	var	drugUnit	=	document.forms[1].drugUnit.value;
	var	qty			=	"";
	if(days!='' && !isNaN(days))
		qty	=	parseInt(days);//parseInt(posology)*parseInt(days);//Removed Posology calculation as per QATAR
	var	gran		=	document.forms[1].gran.value;//drugDesc.substring(drugDesc.lastIndexOf(",")+1,drugDesc.length);//document.forms[1].gran.value;
	if("PCKG"==drugUnit)
		result	=	Math.ceil(qty/gran);
	else
		result	=	qty;
	document.forms[1].drugquantity.value	=	result;
}

function onEditOnlinePreAuth(){
	if(!JS_SecondSubmit){
		document.forms[1].mode.value="doEditOnlinePreAuth";
	    document.forms[1].action = "/OnlinePreAuthAction.do";    
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}

function onExit()
{
	document.forms[1].tab.value ="Check Eligibility";
	document.forms[1].sublink.value ="Check Eligibility";
	document.forms[1].leftlink.value ="Approval Claims";
	
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineCashlessHospAction.do";
	document.forms[1].submit();
}

function selectClinician(){
  document.forms[1].mode.value="doDefaultClinician";
  document.forms[1].action="/OnlinePreAuthClinicianAction.do";	
  document.forms[1].submit();	
}

var req = "";
function validateClinician(obj){
	if(document.getElementById("clinicianId").value!=""){
		if (window.XMLHttpRequest)
		{ // Non-IE browsers
			req = new XMLHttpRequest();
			req.onreadystatechange = state_ClinicianDetails;
		    try {
		    	req.open("POST","/OnlinePreAuthClinicianAction.do?mode=doValidateClinician&clinicianId="+document.getElementById("clinicianId").value,true);
			} catch (e)
			{
				alert(e);
			}
			req.send(null);
		} 
		else if (window.ActiveXObject)
		{ // IE
			req = new ActiveXObject("Microsoft.XMLHTTP");
		    if (req)
		    {
		    	req.onreadystatechange = state_ClinicianDetails;
		        req.open("GET", "/OnlinePreAuthClinicianAction.do?mode=doValidateClinician&clinicianId="+document.getElementById("clinicianId").value,true);
		        req.send(null);
			}
		}
	/*if (window.ActiveXObject){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		if (xmlhttp){
			xmlhttp.open("GET",'/OnlinePreAuthClinicianAction.do?mode=doValidateClinician&clinicianId='+obj.value,true);
			xmlhttp.onreadystatechange=state_ClinicianDetails;
			xmlhttp.send();
		}
	}*/
	 /* document.forms[1].mode.value="doValidateClinician";
	  document.forms[1].action="/OnlinePreAuthClinicianAction.do";	
	  document.forms[1].submit();	*/
	}
}
function state_ClinicianDetails(){
	var result,result_arr;
	if (req.readyState == 4){
		//alert(xmlhttp.status);
	if (req.status == 200){
				result = req.responseText;
			result_arr = result.split(","); 
			if(result_arr[0]!=""){
				document.forms[1].clinicianName.value=result_arr[1];
				document.forms[1].speciality.value=result_arr[2];
				document.forms[1].consultation.value=result_arr[3];
			}
			else{
				alert("Treating Doctor is not associated with the Provider and we will not process the Preapproval " +
						"request until the doctor is associated. Please send the details of Treating doctor to " +
						document.forms[1].hospMailId.value+".");
				document.forms[1].clinicianName.value="";
				document.forms[1].speciality.value="";
				document.forms[1].consultation.value="";
			}
			
		}
	}
}

function callFocusObj(){
	//var focusObj	=	document.forms[1].focusObj.value;
	//if(focusObj.value!=""){
		//document.getElementById("drugUnit").scrollIntoView(true);
		//document.getElementById("drugUnit").focus();
		//document.forms[1].focusObj.scrollIntoView(true);
		//document.getElementById(focusObj).focus();
		//window.scrollTo(0,1000);
	//}
}

function showUplodedFile(rownum)
{
	
	var openPage = "/OnlineReportsAction.do?mode=doViewCommonForAll&module=preAuthorizationUploadedFile&rownum="+rownum;
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

function onUploadDocs()
{
	var fromFlag	=	document.forms[1].fromFlag.value;
	document.forms[1].mode.value="doDefaultUploadDocs";
	var lPreAuthIntSeqId	=	document.forms[1].lPreAuthIntSeqId.value;
	document.forms[1].action="/UploadMOUCertificatesList.do?lPreAuthIntSeqId="+lPreAuthIntSeqId+"&preAuthNoYN="+document.forms[1].preAuthNoYN.value+"&fromFlag="+fromFlag;
	document.forms[1].submit();
}

function onBack(){
	if(!JS_SecondSubmit){
		var fromFlag	=	document.forms[1].fromFlag.value;
		//alert("fromFlag::"+fromFlag);
		if("preAuth"==fromFlag){
			document.forms[1].mode.value="doDefaultPreAuth";
			document.forms[1].action = "/OnlineProviderPreAuthAction.do";   
		}
		else if("preAuthEnhance"==fromFlag){
			document.forms[1].mode.value="doSearchPreAuth";
			document.forms[1].action = "/SearchPreAuthLogsAction.do";   
		}
		else{
		document.forms[1].mode.value="doValidate";
	    document.forms[1].action = "/OnlineCashlessHospBackAction.do";   
		} 
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}

////Diag Code
function onDiagCodeSearch(searchMode) {
	  document.getElementById("icdDesc").value="";
	  onSearchData(searchMode,'onDiagCodeSearch','icdCode','icdDivID','/ProviderAsynchronousSearch.do?mode=doDiagDetailsSearch&icdMode=CODE','onSelectIcdCode');
}

function onSelectIcdCode(param1){
	  var path="/ProviderAsynchronousSearch.do?mode=getIcdDetails&icdMode=DESC&icdSeqID="+param1;
		 var varIcdCodeDec= getSearchData(path);
		 document.getElementById("diagCodeSeqID").value=param1;
		 document.getElementById("icdDesc").value=varIcdCodeDec; 
	  }


//Diag Desc
function onDiagDescSearch(searchMode) {
	  document.getElementById("icdCode").value="";
	  onSearchData(searchMode,'onDiagDescSearch','icdDesc','icdDescDivID','/ProviderAsynchronousSearch.do?mode=doDiagDetailsSearch&icdMode=DESC','onSelectIcdDesc');
}

function onSelectIcdDesc(param1){
	  var path="/ProviderAsynchronousSearch.do?mode=getIcdDetails&icdMode=CODE&icdSeqID="+param1;
		 var varIcdCodeDec= getSearchData(path);
		 document.getElementById("diagCodeSeqID").value=param1;
		 document.getElementById("icdCode").value=varIcdCodeDec; 
	  }


//Activity COde
function onActivityCodeSearch(searchMode) {
	  document.getElementById("activityCodeDesc").value="";
	  onSearchData(searchMode,'onActivityCodeSearch','activityCode','activityCodeDivID','/ProviderAsynchronousSearch.do?mode=doActivityCodeSearch&icdMode=CODE','onSelectActivityCode');
}

function onSelectActivityCode(param1){
	  var path="/ProviderAsynchronousSearch.do?mode=getIcdDetails&searchFlag=getActDesc&icdMode=DESC&icdSeqID="+param1;
		// var varIcdCodeDec= getSearchData(path);
		 var strArr	=	param1.split("--");
		 document.getElementById("activityCodeDesc").value=strArr[0];
		 document.getElementById("internalCode").value=strArr[1]; 
	  }
//Activity Desc
function onActivityDescSearch(searchMode) {
	  document.getElementById("activityCode").value="";
	  document.getElementById("internalCode").value="";
	  document.getElementById("providerRequestedAmt").value="";
	  var hospSeqID	=	document.getElementById("hospSeqID").value;
	  var alkootId	=	document.forms[1].enrollId.value;
	  onSearchData(searchMode,'onActivityDescSearch','activityCodeDesc','activityDescDivID','/ProviderAsynchronousSearch.do?mode=doActivityCodeSearch&icdMode=DESC&hospSeqId='+hospSeqID+'&alkootId='+alkootId,'onSelectActivityDesc');
}

function onSelectActivityDesc(param1){
	  var path="/ProviderAsynchronousSearch.do?mode=getIcdDetails&searchFlag=getActCode&icdMode=CODE&icdSeqID="+param1;
		 //var varIcdCodeDec= getSearchData(path);
		 var strArr	=	param1.split("--");
		 //document.getElementById("diagCodeSeqID").value=param1;
		 document.getElementById("activityCode").value=strArr[0];
		 document.getElementById("internalCode").value=strArr[1]; 
		 document.getElementById("tariffSeqId").value=strArr[3];	
		 document.getElementById("providerRequestedAmt").value=strArr[4]; 
		 if(strArr[4]!='' && strArr[4]!='undefined')
			 document.getElementById("providerRequestedAmt").readOnly	=	true;
		}
//Internal COde
function onInternalCodeSearch(searchMode) {
	  document.getElementById("activityCode").value="";
	  document.getElementById("activityCodeDesc").value="";
	  document.getElementById("providerRequestedAmt").value="";
	  var hospSeqID	=	document.getElementById("hospSeqID").value;
	  var alkootId	=	document.forms[1].enrollId.value;
	  onSearchData(searchMode,'onInternalCodeSearch','internalCode','internalCodeDivID','/ProviderAsynchronousSearch.do?mode=doActivityCodeSearch&icdMode=INTERNALCODE&hospSeqId='+hospSeqID+'&alkootId='+alkootId,'onSelectInternalCode');
}

function onSelectInternalCode(param1){
	  //var path="/ProviderAsynchronousSearch.do?mode=getIcdDetails&searchFlag=getInternalDesc&icdMode=DESC&icdSeqID="+param1;
		 var strArr	=	param1.split("--");
		 document.getElementById("activityCode").value=strArr[0];
		 document.getElementById("activityCodeDesc").value=strArr[1]; 
		 document.getElementById("tariffSeqId").value=strArr[3]; 
		 document.getElementById("providerRequestedAmt").value=strArr[4]; 
		 if(strArr[4]!='' && strArr[4]!='undefined')
			 document.getElementById("providerRequestedAmt").readOnly	=	true;
		 
	  }
//-----------------------------

//Drug COde
function onDrugCodeSearch(searchMode) {
	  //document.getElementById("drugDesc").value="";
	  var alkootId	=	document.forms[1].enrollId.value;
	  onSearchData(searchMode,'onDrugCodeSearch','drugCode','drugCodeDivID','/ProviderAsynchronousSearch.do?mode=doDrugCodeSearch&icdMode=CODE&alkootId='+alkootId,'onSelectDrugCode');
}

function onSelectDrugCode(param1){
	  var path="/ProviderAsynchronousSearch.do?mode=getIcdDetails&searchFlag=getDrugDesc&icdMode=DESC&icdSeqID="+param1;
		 var varIcdCodeDec= getSearchData(path);
		 var strArr	=	varIcdCodeDec.split("--");
		 document.getElementById("drugDesc").value=strArr[0]; 
		 document.getElementById("gran").value=strArr[1]; 
		 document.getElementById("tariffSeqId").value=param1;
  }
//Drug Desc
function onDrugDescSearch(searchMode) {
	  //document.getElementById("drugCode").value="";
	  var alkootId	=	document.forms[1].enrollId.value;
	  onSearchData(searchMode,'onDrugDescSearch','drugDesc','drugDescDivID','/ProviderAsynchronousSearch.do?mode=doDrugCodeSearch&icdMode=DESC&alkootId='+alkootId,'onSelectDrugDesc');
}

function onSelectDrugDesc(param1,param2,param3,param4){
		document.getElementById("granularUnit").value=param3;
		document.getElementById("noOfUnits").value=param4;
	  var path="/ProviderAsynchronousSearch.do?mode=getIcdDetails&searchFlag=getDrugCode&icdMode=CODE&icdSeqID="+param1;
		 var varIcdCodeDec= getSearchData(path);
		 //document.getElementById("diagCodeSeqID").value=param1;
		 var strArr	=	varIcdCodeDec.split("--");
		 document.getElementById("drugCode").value=strArr[0];;
		 document.getElementById("gran").value=strArr[1];  
		 document.getElementById("tariffSeqId").value=param1;
  }
//----------------------------
//Clicnician Search
function onClinicianSearch(searchMode) {
	//document.getElementById("clinicianName").value="";
	  onSearchData(searchMode,'onClinicianSearch','clinicianId','clinicianDivID','/ProviderAsynchronousSearch.do?mode=doClinicianSearch','onSelectClinicianID');
}
function onSelectClinicianID(param1) {
	var strArr	=	param1.split("--");
	  document.getElementById("clinicianName").value=strArr[0];
	  document.forms[1].speciality.value=strArr[1];
}

function onClinicianNameSearch(searchMode) {
	  //document.getElementById("clinicianId").value="";
	  onSearchData(searchMode,'onClinicianNameSearch','clinicianName','clinicianNameDivID','/ProviderAsynchronousSearch.do?mode=doClinicianNameSearch','onSelectClinicianName');
}
function onSelectClinicianName(param1) {
	var strArr	=	param1.split("--");
	  document.getElementById("clinicianId").value=strArr[0];
	  document.forms[1].speciality.value=strArr[1];
}

function onViewShortfall(obj)
{
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doViewShortfall";
   	document.forms[1].action="/ViewAuthorizationShortfallDetails.do?shortfallSeqId="+obj;
   	JS_SecondSubmit=true;
   	document.forms[1].submit();
	}
}
function changeTreatment(obj){
	  if((obj.value==='1' || obj.value==='3') && (document.getElementById("benefitTemp").value=="DNTL" || document.getElementById("benefitTemp").value=="Dental"))
		  document.getElementById("orthoDiv").style.display="inline";
	  else
		  document.getElementById("orthoDiv").style.display="none";
}

function onChangeServiceType(obj) {
	var	serviceType	=	obj.value;
	if("ACD"==serviceType || "PKD"==serviceType)
	{
		 document.getElementById("activity").style.display		=	"inline";
		 document.getElementById("activityCodeId").style.display=	"inline";
		 document.getElementById("activityCodeVal").style.display=	"inline";
		 document.getElementById("activityDescVal").style.display=	"inline";
		 document.getElementById("activityInternalCode").style.display=	"inline";
		 document.getElementById("service").style.display		=	"none";
		 document.getElementById("serviceCodeId").style.display	=	"none";
		 document.getElementById("serviceCodeVal").style.display=	"none";
		 document.getElementById("serviceDescVal").style.display=	"none";
		 document.getElementById("serviceInternalCode").style.display=	"none";
		 
		 document.getElementById("activityCodeDesc").value	 =	"";
		 document.getElementById("activityCode").value	 	 =	"";
		 document.getElementById("internalCode").value		 =	"";
		 document.getElementById("providerRequestedAmt").value		 =	"";
	}else{
		 document.getElementById("activity").style.display		=	"none";
		 document.getElementById("activityCodeId").style.display=	"none";
		 document.getElementById("activityCodeVal").style.display=	"none";
		 document.getElementById("activityDescVal").style.display=	"none";
		 document.getElementById("activityInternalCode").style.display=	"none";
		 document.getElementById("service").style.display		=	"inline";
		 document.getElementById("serviceCodeId").style.display	=	"inline";
		 document.getElementById("serviceCodeVal").style.display=	"inline";
		 document.getElementById("serviceDescVal").style.display=	"inline";
		 document.getElementById("serviceInternalCode").style.display=	"inline";
		 
		 document.getElementById("activityServiceCode").selectedIndex	 =	0;
		 document.getElementById("activityServiceCodeDesc").value	 	 =	"";
		 document.getElementById("serviceInternalCodeVal").value		 =	"";
		 document.getElementById("providerRequestedAmt").value		 =	"";
	}
}
function onServiceCode(obj){
	var selIndex 	= obj.selectedIndex;
	var selText 	= obj.options[parseInt(selIndex)].text;
	if(selIndex!=0){
		document.getElementById("activityServiceCodeDesc").value		=	selText.substring(0,selText.lastIndexOf("-"));
		document.getElementById("serviceInternalCodeVal").value			=	selText.substring(selText.lastIndexOf("-")+2,selText.length);
		document.getElementById("serviceInternalCodeVal").readOnly		=	true;
		document.getElementById("providerRequestedAmt").value			=	"";
	}

}

function onViewShortfallDoc(shortfallSeqID){
   	var openPage = "/PreAuthGeneralAction.do?mode=viewShortfalldoc&shortfallSeqID="+shortfallSeqID;
  	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  	window.open(openPage,'',features);
}

function onSubmitOnlinePreAuthEnhance(){
	var document1= document.getElementById("fileid1").value;
	var document2= document.getElementById("fileid2").value;
	var document3= document.getElementById("fileid3").value;
	var document4= document.getElementById("fileid4").value;
	var description= document.getElementById("descriptionid").value;
	var description2= document.getElementById("descriptionid2").value;
	var description3= document.getElementById("descriptionid3").value;
	var description4= document.getElementById("descriptionid4").value;
	var sumofallfilesize = 0;
	var maxfilesize = 8*1024*1024;
	if(!JS_SecondSubmit){
		if(document1 != ""){
			var file1Size = document.getElementById('fileid1').files[0].size;
		    var file1Name = document.getElementById('fileid1').files[0].name;
		    sumofallfilesize = sumofallfilesize+file1Size;
		    if(description==""){
				alert("upload document description1 is required");
				if(document1 !=""){
					document.getElementById("fileid1").value="";
				}
				return;
			}
		   
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		 
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File1 Format should be xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid1").value="";
		    	return;
		    }
		    
		  }

		if(document2 != ""){
			var file1Size = document.getElementById('fileid2').files[0].size;
		    var file1Name = document.getElementById('fileid2').files[0].name;
		    sumofallfilesize = sumofallfilesize+file1Size;
		    if(description2==""){
				alert("upload document description2 is required");
					document.getElementById("fileid2").value="";
				return;
			}
		    
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		  
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File2 Format should be xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid2").value="";
		    	return;
		    }
		    
		  }

		if(document3 != ""){
			
			var file1Size = document.getElementById('fileid3').files[0].size;
		    var file1Name = document.getElementById('fileid3').files[0].name;
		    sumofallfilesize = sumofallfilesize+file1Size;
		    if(description3==""){
				alert("upload document description3 is required");
					document.getElementById("fileid3").value="";
				return;
			}
		   
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File3 Format should me xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid3").value="";
		    	return;
		    }
		    
		  }

		if(document4 != ""){
			var file1Size = document.getElementById('fileid4').files[0].size;
		    var file1Name = document.getElementById('fileid4').files[0].name;
		    sumofallfilesize = sumofallfilesize + file1Size;
		    if(description4==""){
				alert("upload document description4 is required");
				document.getElementById("fileid4").value="";
				return;
			}
		    
		  var  file1Extension = file1Name.replace(/^.*\./, '');
		  
		    if(file1Extension!="xls" && file1Extension !="xlsx" && file1Extension !="doc" && file1Extension !="docx" && file1Extension !="pdf"){
		    	alert("File4 Format should be xls, xlsx, doc, docx or pdf");
		    	document.getElementById("fileid4").value="";
		    	return;
		    }
		    
		  }
		
		if(sumofallfilesize > maxfilesize){
			alert("files size should not be more than 8MB.");
			document.getElementById("fileid1").value="";
			document.getElementById("fileid2").value="";
			document.getElementById("fileid3").value="";
			document.getElementById("fileid4").value="";
			return;
		}
		
		if((document.getElementById("benefitTemp").value=="DNTL" || document.getElementById("benefitTemp").value=="Dental"))
		{
			if(document.getElementById("overJet").value!='' && 
					(document.getElementById("openbiteAntrio").value=='' && document.getElementById("openbitePosterior").value=='' 
						&& document.getElementById("openbiteLateral").value=='')){
				alert("Overjet and Open Bite should be billed together");
				return false;
			}
			if(document.getElementById("overJet").value=='' && 
					(document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!='' 
						|| document.getElementById("openbiteLateral").value!='')){
				alert("Open Bite and Overjet should be billed together");
				return false;
			}
		}
		document.forms[1].mode.value="doSubmitOnlinePreAuth";
	    document.forms[1].action ="/OnlinePreAuthEnhanceAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}

function addDiagnosisDetailsEnhance(){
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doAddDiagnosisDetails";
    document.forms[1].action = "/OnlinePreAuthEnhanceAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}


function onAddDiagsEnhance(obj)
{
	var benefitType=document.getElementById("benefitypeCheck").value;
	var icdCode=document.getElementById("icdCode").value;
	if(document.forms[1].icdCode.value=="")
	{
		alert("Please Enter Diagnosys Code First");
		document.forms[1].icdCode.focus();
		return false;
	}else if(document.forms[1].icdDesc.value=="")
	{
		alert("Please Enter Diagnosys Description");
		document.forms[1].icdDesc.focus();
		return false;
	}
	
	if(document.getElementById("icdCountid") !=  null){
		var icdCountSize = document.getElementById("icdCountid").value;
	if(document.forms[1].icdCode.value!="")
	{
		var icdformValue =  document.forms[1].icdCode.value;
		for(i=0; i<icdCountSize;i++){
		var savedicdValue =	document.getElementById("icdcodeid"+i).textContent;
		if(icdformValue==savedicdValue){
			alert("Diagnosis code already Exists!");
			return false;
		}
		}
	  }
	}
	document.forms[1].mode.value = "doSaveDiags";
	document.forms[1].action = "/OnlinePreAuthEnhanceAction.do?focusId="+obj+"&benefitType="+benefitType+"&icdCode="+icdCode;
	document.forms[1].submit();
}

function addActivityDetailsEnhance(){
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doAddActivityDetails";
    document.forms[1].action = "/OnlinePreAuthEnhanceAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}

function onSaveActivitiesEnhance(obj){
	var serviceType	=	"";
	  if((document.getElementById("benefitTemp").value=="IMTI" || document.getElementById("benefitTemp").value=="In-Patient Maternity")
			  || (document.getElementById("benefitTemp").value=="IPT" || document.getElementById("benefitTemp").value=="In-Patient")){
		  serviceType	=	document.forms[1].activityServiceType.value;
		  if(serviceType=="")
	{
		alert("Please Select Service Type");
				document.forms[1].activityServiceType.focus();
		return false;
	}
	  }

	
	if("SCD"==serviceType){
		if(document.forms[1].activityServiceCode.value=="")
		{
			alert("Please Enter Service Code First");
			document.forms[1].activityServiceCode.focus();
			return false;
		}else if(document.forms[1].activityServiceCodeDesc.value=="")
		{
			alert("Please Enter Service Description");
			document.forms[1].activityServiceCodeDesc.focus();
			return false;
		}else if(document.forms[1].activityQuantity.value=="")
		{
			alert("Please Enter Quantity");
			document.forms[1].activityQuantity.focus();
			return false;
		}
		
	}else{
	if(document.forms[1].activityCode.value=="")
	{
		alert("Please Enter Activity Code First");
		document.forms[1].activityCode.focus();
		return false;
	}else if(document.forms[1].activityCodeDesc.value=="")
	{
		alert("Please Enter Activity Description");
		document.forms[1].activityCodeDesc.focus();
		return false;
	}else if(document.forms[1].activityQuantity.value=="")
	{
		alert("Please Enter Quantity");
		document.forms[1].activityQuantity.focus();
		return false;
	}
	}
	
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doSaveActivities";
    document.forms[1].action = "/OnlinePreAuthEnhanceAction.do?focusId="+obj+"&tariffSeqId="+document.forms[1].tariffSeqId.value;
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}

function addDrugDetailsEnhance(){
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doAddDrugDetails";
    document.forms[1].action = "/OnlinePreAuthEnhanceAction.do";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}

function onSaveDrugsEnhance(){
	if(!JS_SecondSubmit){
		if(document.forms[1].drugCode.value=="")
		{
			alert("Please Enter Drug Code First");
			document.forms[1].drugCode.focus();
			return false;
		}else if(document.forms[1].drugDesc.value=="")
		{
			alert("Please Enter Drug Description");
			document.forms[1].drugDesc.focus();
			return false;
		}else if(document.forms[1].drugdays.value=="")
		{
			alert("Please Enter Days");
			document.forms[1].drugdays.focus();
			return false;
		}else if(document.forms[1].drugUnit.value=="")
		{
			alert("Please Enter Drug Unit");
			document.forms[1].drugUnit.focus();
			return false;
		}else if(document.forms[1].drugquantity.value=="")
		{
			alert("Please Enter Drug Quantity");
			document.forms[1].drugquantity.focus();
			return false;
		}
		
	document.forms[1].mode.value="doSaveDrugs";
    document.forms[1].action = "/OnlinePreAuthEnhanceAction.do?focusId=drugDesc";    
	JS_SecondSubmit=true;
	document.forms[1].submit();
	 }
	}

function onSaveAppealComments()
{
	var res;
	var appealDocsYN= document.getElementById("appealDocsYN").value;
	var appealComments= document.getElementById("appealComments").value;
	if(appealComments == "" || appealComments == null)
		{
			alert("please enter Appeal Comments.");
			document.getElementById("appealComments").focus();
			return;
		}
	
	var appealComments= document.getElementById("appealComments").value;
    var space = appealComments.charCodeAt(0);
      if(space==32)
      {
             alert("Appeal Comments  should not start with space.");
             document.getElementById("appealComments").value="";
             document.getElementById("appealComments").focus();
             return;
      } 
	
	if(appealDocsYN == "N")
	{
		res = confirm("Appeal Documents are not uploaded! Do you still want to save ?");
		if(res == true)
		{
			if(!JS_SecondSubmit){
				document.forms[1].mode.value="doSaveAppealComment";
			    document.forms[1].action = "/OnlinePreAuthAction.do";    
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}
		}
		else
		{
			return false;
		}
	}	
	
	if(appealDocsYN == "Y")
	{	
		if(!JS_SecondSubmit)
		{
			document.forms[1].mode.value="doSaveAppealComment";
		    document.forms[1].action = "/OnlinePreAuthAction.do";    
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}	

	}
}

function onCommonUploadDocuments2(strTypeId,strAppeal)
{
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefaultUploadView";
	var seqID	=	document.forms[1].seqId.value;
	if(strTypeId==="PAT"&&document.getElementById("patSeqId")){
		seqID=document.getElementById("patSeqId").value;
	}
	document.forms[1].action="/DocsUpload.do?typeId="+strTypeId+"&seqId="+seqID+"&strAppeal="+strAppeal;
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}


function optspreauthlimits(){
	if(document.getElementById("optspreauthlimitsid").value !="" ){
		
		var optspreauthlimits = document.getElementById("optspreauthlimitsid").value;
		var optspreauthlimitsfloat = parseFloat(optspreauthlimits);
		
		if(document.getElementById("netamountidactivity") != null && document.getElementById("netamountiddrug")!=null){
			providernetPreauthAmountForActivity = document.getElementById("netamountidactivity").value;
			providernetPreauthAmountForDrug = document.getElementById("netamountiddrug").value;
			var activitynetAmount = parseFloat(providernetPreauthAmountForActivity);
			var drugnetAmount = parseFloat(providernetPreauthAmountForDrug);
			providernetPreauthAmountFloat = activitynetAmount+drugnetAmount;
			if(optspreauthlimitsfloat>providernetPreauthAmountFloat){
				alert("Preapproval is not required as the total net amount is less than "+optspreauthlimitsfloat);
				return true;
			}else{
				return false;
			}
		}else{
	/*if(document.getElementById("netamountidactivity") != null){
		providernetPreauthAmount = document.getElementById("netamountidactivity").value;
		providernetPreauthAmountFloat = parseFloat(providernetPreauthAmount);
		if(optspreauthlimitsfloat>providernetPreauthAmountFloat){
			alert("Preapproval is not required as the total net amount is less than "+optspreauthlimitsfloat);
			return true;
		}else{
			return false;
		}
	}*/
	
	if(document.getElementById("netamountiddrug")!=null){
		providernetPreauthAmount = document.getElementById("netamountiddrug").value;
		providernetPreauthAmountFloat = parseFloat(providernetPreauthAmount);
		if(optspreauthlimitsfloat>providernetPreauthAmountFloat){
			alert("Preapproval is not required as the total net amount is less than "+optspreauthlimitsfloat);
			return true;
		}else{
			return false;
		}
	}
	
   }
  }
}

function daysRangeValidation(argDate){
 	var dateArr=argDate.split("/");	
	var inputDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
	var currentDate = new Date();
	var oneDay = 24*60*60*1000; 
	var diffDays = Math.round(((currentDate.getTime() - inputDate.getTime())/(oneDay)));
	var stYear=inputDate.getYear();
	var endYear=currentDate.getYear();
	var stLeapYear=stYear % 4 == 0;
	var endLeapYear=endYear % 4 == 0;
	if(stLeapYear==true||endLeapYear==true){
		if(diffDays >3){
			return true;
		}else{
			return false;
		}
	}else{
		if(diffDays >3){
			return true;
		}else{
			return false;
		}
	}				
}	 		