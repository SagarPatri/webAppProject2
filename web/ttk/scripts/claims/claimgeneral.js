//javascript used in preauthgeneral.jsp of claim flow
var  popupWindow=null;	
    function openPopUpWindow()
    {
    	if(TC_GetChangedElements().length>0)
	    {
	      alert("Please save the modified data, before Generating Letter");
	      return false;
	    }//end of if(TC_GetChangedElements().length>0)
	       
	  
    	var claimSeqID=document.getElementById("claimSeqID").value;
    	var claimStatus=document.getElementById("claimStatus").value;
    	var claimSettelmentNo=document.getElementById("claimSettelmentNo").value;
    	var url="/LetterViewPage.do?claimSeqID="+claimSeqID+"&claimStatus="+claimStatus+"&claimSettelmentNo="+claimSettelmentNo;
    	 //popupWindow= window.open("/LettetViewPage.do?claimSeqID="+claimSeqID+"&claimStatus="+claimStatus+"&claimSettelmentNo="+claimSettelmentNo,"Select Letter","width=500,height=200,left=300,top=200,toolbar=no,scrollbars=no,status=no");
    	 popupWindow = window.open(url, "LetterFormat", "width=400,height=250,left=400,top=300,toolbar=no,scrollbars=no,status=no");
  		  popupWindow.focus(); 
  		  document.onmousedown=focusPopup; 
  		  document.onkeyup=focusPopup; 
  		  document.onmousemove=focusPopup;   
	}
	function viewHistory(){
		if(!JS_SecondSubmit)
		 {		
		document.forms[1].mode.value = "doViewHistory";		
		document.forms[1].action="/PreAuthGeneralAction.do";
		 JS_SecondSubmit=true;
		document.forms[1].submit();
		 }
		}//viewHistory()
	function selectMember(){
		if(!JS_SecondSubmit){ 
		  
			  document.forms[1].mode.value="doGeneral";
			  document.forms[1].reforward.value="memberSearch";
			  document.forms[1].action="/ClaimGeneralAction.do";	
			  JS_SecondSubmit=true;
			  document.forms[1].submit();
		}
	}

	function deleteMember(){
		if(!JS_SecondSubmit){ 
			  document.forms[1].mode.value="deleteMember";	
			  document.forms[1].action="/ClaimGeneralAction.do";	
			  JS_SecondSubmit=true;
			  document.forms[1].submit();
		}
	}
	function addDiagnosisDetails(){	
		if(!JS_SecondSubmit)
		 {
			var benefitType = document.forms[1].benefitType.value;
			var primary = document.forms[1].primary.value;
			var mat_icd_yn = parseInt(document.forms[1].mat_icd_yn.value);
			
			if(eventFire==true){
				if(document.forms[1].primaryAilment.checked){
					document.forms[1].primaryAilment.value='Y';
					}else{
						document.forms[1].primaryAilment.value='N';
					}
				
				document.forms[1].validateIcdCodeYN.value="Y";
				if((benefitType=="OMTI" || benefitType=="IMTI")){
					   document.forms[1].mode.value="addDiagnosisDetails";
					   document.forms[1].action="/UpdateClaimGeneral.do?focusObj=ailmentDescription";	
					   JS_SecondSubmit=true;	
					   document.forms[1].submit();
				}else{
				
				if((benefitType!="OMTI" || benefitType!="IMTI") && primary == "YES" && mat_icd_yn >0){
					 var message=confirm('Please check if the benefit type is maternity as secondary has a maternity icd code');
						if(message)
						{
							   document.forms[1].mode.value="addDiagnosisDetails";
							   document.forms[1].action="/UpdateClaimGeneral.do?focusObj=ailmentDescription";	
							   JS_SecondSubmit=true;	  
							   document.forms[1].submit();
						}
				 }else{
				   
				   document.forms[1].mode.value="addDiagnosisDetails";
				   document.forms[1].action="/UpdateClaimGeneral.do?focusObj=ailmentDescription";	
				   JS_SecondSubmit=true;	
				   document.forms[1].submit();
				 } 
			}
				}else{
				eventFire=true;
			}
		 }	
		}//addDiagnosisDetails()
	function editDiagnosisDetails(diagSeqId1,icdCodeSeqId1){
		if(!JS_SecondSubmit)
		 {	
		   document.forms[1].diagSeqId.value=diagSeqId1;
		   document.forms[1].icdCodeSeqId.value=icdCodeSeqId1;
		   document.forms[1].mode.value="editDiagnosisDetails";
		   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=ailmentDescription";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();
		 }		
		}//editDiagnosisDetails()
	function deleteDiagnosisDetails(diagSeqId1,icdCodeSeqId1){
	 if(confirm("Are You Sure You Want To Delete Diagnosis Details!")){
		if(!JS_SecondSubmit){	
		   document.forms[1].diagSeqId.value=diagSeqId1;
		   document.forms[1].icdCodeSeqId.value=icdCodeSeqId1;
		   document.forms[1].mode.value="deleteDiagnosisDetails";
		   document.forms[1].action="/PreAuthGeneralAction.do?focusObj=ailmentDescription";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();
		 }	
		 }	
		}//deleteDiagnosisDetails	
	
	function addActivityDetails(){	
		if(!JS_SecondSubmit){   
		
		document.forms[1].mode.value="doGeneral";
	    document.forms[1].reforward.value="addActivityDetails";		        	       
	    document.forms[1].action = "/ClaimGeneralAction.do";    
		JS_SecondSubmit=true;
		document.forms[1].submit();
		 }
		}//addActivityDetails()
   function editActivityDetails(activityDtlSeqId1){	
	   if(!JS_SecondSubmit){ 
		   document.forms[1].reforward.value="viewActivityDetails";	
			document.forms[1].activityDtlSeqId.value=activityDtlSeqId1;
			document.forms[1].child.value="EditActivityDetails";
			document.forms[1].mode.value="doGeneral";
			document.forms[1].action="/ClaimGeneralAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		 }
			}   
   function deleteActivityDetails(activityDtlSeqId1){
	   if(confirm("Are You Sure You Want To Delete Activity Details!")){
		if(!JS_SecondSubmit){	
		   document.forms[1].activityDtlSeqId.value=activityDtlSeqId1;
		   document.forms[1].child.value="DeleteActivityDetails";
		   document.forms[1].mode.value="deleteActivityDetails";
		   document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();
		 }		
	    }
		}	//deleteActivityDetails()
function calculateClaimAmount(){
	
	if(document.getElementById("memberSpecificRemarksid").value !=""){

		 var message=confirm(document.getElementById("memberSpecificRemarksid").value);
		 if(message){
			    document.forms[1].mode.value="calculateClaimAmount";
				document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
				JS_SecondSubmit=true;
				document.forms[1].submit();
				document.forms[1].calculate.value	=	"Please Wait...!!";
				document.forms[1].calculate.disabled	=	true;
			   return;
		 }else{
			 return;
		 }
	}
	
	 if(!JS_SecondSubmit){ 
				document.forms[1].mode.value="calculateClaimAmount";
				document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
				JS_SecondSubmit=true;
				document.forms[1].submit();
				document.forms[1].calculate.value	=	"Please Wait...!!";
				document.forms[1].calculate.disabled	=	true;
	   }
}
function preAuthAmountApproved(){
	 if(!JS_SecondSubmit){
				document.forms[1].mode.value="preAuthAmountApproved";
				document.forms[1].action="/PreAuthGeneralAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
	    }
}//preAuthAmountApproved()
		function getPreAuthDetails(){	
			
			document.forms[1].mode.value="testMethod";			
			document.forms[1].action="/PreAuthGeneralAction.do";
			document.forms[1].submit();
	}		
		var sss='<%=request.getSession().getId()%>';
		function addObservs(activityDtlSeqId1){
			
			var pstatussi=document.forms[1].preAuthSeqID.value;
			var obrurl="/ObservationAction.do?obru="+sss+sss+"&mode=observWindow&activityDtlSeqId="+activityDtlSeqId1+"&pstatussi="+pstatussi;				
			 popupWindow= window.open(obrurl,"OBSERVS","width=950,height=450,left=200,top=100,toolbar=no,scrollbars=no,status=no"); 
			  popupWindow.focus(); 
			  document.onmousedown=focusPopup; 
			  document.onkeyup=focusPopup; 
			  document.onmousemove=focusPopup; 		
			}
		function setEndDate(){
			//document.forms[1].dischargeDate.readOnly=false;
			var encounterType=document.forms[1].encounterTypeId.value;
			if(encounterType==1||encounterType==2||encounterType==="1"||encounterType==="2"){
				document.forms[1].dischargeDate.value=document.forms[1].admissionDate.value;
				document.forms[1].dischargeTime.value=document.forms[1].admissionTime.value;
				document.forms[1].dischargeDay.selectedIndex=document.forms[1].admissionDay.selectedIndex;
				//document.forms[1].dischargeDate.readOnly=true;
				//document.forms[1].dischargeTime.readOnly=true;
				//document.forms[1].dischargeDay.readOnly=true;						
				}
			}

		function setDateMode(){			
			var encounterType=document.forms[1].encounterTypeId.value;
			if(encounterType==1||encounterType==2||encounterType==="1"||encounterType==="2"){
				document.forms[1].dischargeDate.value=document.forms[1].admissionDate.value;
				document.forms[1].dischargeTime.value=document.forms[1].admissionTime.value;
				document.forms[1].dischargeDay.selectedIndex=document.forms[1].admissionDay.selectedIndex;
				//document.forms[1].dischargeDate.readOnly=true;
				//document.forms[1].dischargeTime.readOnly=true;
				//document.forms[1].dischargeDay.readOnly=true;					
			}else{
				document.forms[1].dischargeDate.value="";
				document.forms[1].dischargeTime.value="";
				document.forms[1].dischargeDay.value="AM";
				//document.forms[1].dischargeDate.readOnly=false;	
				//document.forms[1].dischargeTime.readOnly=false;
				//document.forms[1].dischargeDay.readOnly=false;					
				}
			}

		function checkCalender(){			
			var encounterType=document.forms[1].encounterTypeId.value;
			if(encounterType==1||encounterType==2||encounterType==="1"||encounterType==="2"){            
				//show_calendar('CalendarObjectPARDate','frmClaimGeneral.dischargeDate',document.frmClaimGeneral.dischargeDate.value,'',event,148,178);return false;
			}else{				
				show_calendar('CalendarObjectPARDate','frmClaimGeneral.dischargeDate',document.frmClaimGeneral.dischargeDate.value,'',event,148,178);return false;
				}
			}
		
		function setProviderMode(){	
			if(!JS_SecondSubmit){
			   document.forms[1].mode.value="setProviderMode";
			   document.forms[1].action="/ClaimGeneralAction.do";	
			   JS_SecondSubmit=true;
			   document.forms[1].submit();
			}
			}
		
  function addShortFalls(){
	  ///SupportDocAction.do?mode=doSearch&amp;Entry=Y	
	  if(!JS_SecondSubmit){
		        document.forms[1].mode.value="addShortFalls";
		        document.forms[1].tab.value="General";		        	       
		        document.forms[1].action = "/PreAuthGeneralAction.do";    
				 JS_SecondSubmit=true;
		        document.forms[1].submit();
	  }
		}//end of onAdd()
		 
		function doViewPreauthShortFall(preauthSeqId1,shortFallSeqId1){
			if(!JS_SecondSubmit){
				        document.forms[1].tab.value="General";		        	       
				        document.forms[1].mode.value="doViewPreauthShortFall";
				        document.forms[1].shortFallSeqId.value=shortFallSeqId1;
				        document.forms[1].preAuthSeqID.value=preauthSeqId1;
				        document.forms[1].action = "/PreAuthGeneralShortfallsAction.do";
						 JS_SecondSubmit=true;
				        document.forms[1].submit();
			}
				}//end of doViewPreauthShortFall()
				function deleteShortfallsDetails(preauthSeqId1,shortFallSeqId1){
					   if(confirm("Are You Sure You Want To Delete Shortfalls Details!")){						   
						if(!JS_SecondSubmit){								
						   document.forms[1].preAuthSeqID.value=preauthSeqId1;
						   document.forms[1].shortFallSeqId.value=shortFallSeqId1;
						   document.forms[1].mode.value="deleteShortfallsDetails";
						   document.forms[1].action="/PreAuthGeneralAction.do";	
						   JS_SecondSubmit=true;	   
						   document.forms[1].submit();						   
						     }		
					      }
						}
	function generateClaimLetter(){
		 							   
		if(TC_GetChangedElements().length>0)
	    {
	      alert("Please save the modified data, before Generating Letter");
	      return false;
	    }//end of if(TC_GetChangedElements().length>0)
	       
	   var openPage = "/ClaimGeneralAction.do?mode=generateClaimLetter";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
	  }	

	  function sendClaimLetter(){
		  if(!JS_SecondSubmit){			 				   
			   document.forms[1].mode.value="sendClaimLetter";
			   document.forms[1].action="/ClaimGeneralAction.do";	
			   JS_SecondSubmit=true;	   
			   document.forms[1].submit();		 				   
		  }
	}
	  
	  function setMaternityMode(){
		  if(!JS_SecondSubmit){	
					
			   document.forms[1].mode.value="setMaternityMode";
			   document.forms[1].action="/ClaimGeneralAction.do?focusObj=benefitType";	
			   JS_SecondSubmit=true;	   
			   document.forms[1].submit();		 				   
		  }
	}
	  function isZero(obj){
		  var re = /^[0-9]*\.*[0-9]*$/;
		  var val=obj.value;
		  val=(val==null||val===""||!re.test(val)||val==="0")?0:val;
		  
		  if(parseInt(val)<1){
             alert("Enter Greater Than Zero");
             obj.value="";
             obj.focus();
			  }
		  }
	  
	   function selectProvider(){
		 		 				   
		  var networkProviderType=document.forms[1].networkProviderType.value;
		  if(networkProviderType==="N"){
               alert("Sorry! You Selected Network Type No");
               return ;
			  }
		  if(!JS_SecondSubmit){
			  document.forms[1].mode.value="doGeneral";
			  document.forms[1].reforward.value="providerSearch";
			  document.forms[1].action="/ClaimGeneralAction.do";	
			  JS_SecondSubmit=true;	   
			  document.forms[1].submit();
		  }
	}
	   function selectClinician(){
		   if(!JS_SecondSubmit){
					  document.forms[1].mode.value="doGeneral";
					  document.forms[1].reforward.value="clinicianSearch";
					  document.forms[1].action="/ClaimGeneralAction.do";	
					  JS_SecondSubmit=true;	   
//					  element.innerHTML	=	"<b>"+"Please Wait...!!"+"</b>";
					   document.forms[1].submit();	
		   }
		}
		function setValidateIconTitle(){
			document.getElementById('memberIdResult2').innerHTML='';
			document.getElementById('memberSeqID').value='';
			document.getElementById('patientName').value='';
			document.getElementById('memberAge').value='';                                	
			document.getElementById('emirateId').value='';
			document.getElementById('payerId').value='';
			document.getElementById('insSeqId').value='';
			document.getElementById('payerName').value='';
			document.getElementById('policySeqId').value='';
			document.getElementById('patientGender').value='';
			document.getElementById('policyNumber').value='';
			document.getElementById('corporateName').value='';
			document.getElementById('policyStartDate').value='';
		  	document.getElementById('policyEndDate').value='';
		  	document.getElementById('nationality').value='';
		  	document.getElementById('sumInsured').value='';
		  	document.getElementById('availableSumInsured').value='';
		  	
           var preauthMode=document.getElementById("preauthMode").value;
           if(preauthMode==="DHP"){
        	   document.getElementById("validateIcon").title="Validate Member Id";
               }else{
            	   document.getElementById("validateIcon").title="Validate OTP";
                   }
			}
		
function clearAuthorization()
{
	if(!document.forms[1].authNbr.value=='')
	{
		document.forms[1].mode.value="doClearAuthorization";
		document.forms[1].action="/PreAuthGeneralAction.do";
		document.forms[1].submit();
	}//end of if(!document.forms[1].authNbr.value=='')
}//end of clearAuthorization()


function onPrevHospitalization()
{
	if(document.forms[1].authNbr.value=='')
	{
		if(!JS_SecondSubmit){
		document.forms[1].mode.value="doPrevHospitalization";
		document.forms[1].focusID.value="PrevHospId";
		document.forms[1].action="/PreAuthGeneralAction.do";
		 JS_SecondSubmit=true;
		document.forms[1].submit();
		}
	}//end of if(document.forms[1].authNbr.value=='')
}//end of onPrevHospitalization()

function selectEnrollmentID()
{
	 if(trim(document.forms[1].elements['claimantDetailsVO.enrollmentID'].value).length>0)
	 {
		var message=confirm('You are fetching new data. Do you want to continue?');
		if(message)
		{
			document.forms[1].mode.value="doSelectEnrollmentID";
			document.forms[1].child.value="EnrollmentList";
		 	document.forms[1].action="/PreAuthGeneralAction.do";
		 	document.forms[1].submit();
		}//end of if(message)
	 }else
	 {
	 	 document.forms[1].mode.value="doSelectEnrollmentID";
	 	 document.forms[1].child.value="EnrollmentList";
		 document.forms[1].action="/PreAuthGeneralAction.do";
		 document.forms[1].submit();
	 }//end of else
}//end of selectEnrollmentID()

function clearEnrollmentID()
{
	 document.forms[1].mode.value="doClearEnrollmentID";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of clearEnrollmentID()

function selectCorporate()
{
	 document.forms[1].mode.value="doSelectCorporate";
	 document.forms[1].child.value="GroupList";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of selectCorporate()

function selectPolicy()
{
	 if(trim(document.forms[1].elements['claimantDetailsVO.policyNbr'].value).length>0)
	 {
		var message=confirm('You are fetching new data. Do you want to continue?');
		if(message)
		{
			if(!JS_SecondSubmit){
			document.forms[1].mode.value="doSelectPolicy";
			document.forms[1].child.value="PolicyList";
			document.forms[1].action="/PreAuthGeneralAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
			}//End JS
		}//end of if(message)
	 }else
	 {
		 if(!JS_SecondSubmit){
	 	 document.forms[1].mode.value="doSelectPolicy";
	 	 document.forms[1].child.value="PolicyList";
	 	 document.forms[1].action="/PreAuthGeneralAction.do";
	 	JS_SecondSubmit=true;
	 	 document.forms[1].submit();
		 }//End JS
	 }//end of else
}//end of selectPolicy()

function clearPolicy()
{
	 document.forms[1].mode.value="doClearInsurance";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of clearPolicy()

function selectInsurance()
{
	 document.forms[1].mode.value="doSelectInsurance";
	 document.forms[1].child.value="Insurance Company";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of selectInsurance()

function selectHospital()
{
	//OPD_4_hosptial
	if(document.forms[0].leftlink.value=="Claims")
	 {
	if( document.forms[1].elements['claimDetailVO.paymentType'].value =='HSL')
	{
		
		document.forms[1].paymentto.value =document.forms[1].elements['claimDetailVO.paymentType'].value;
	}
	else
	{
		
		document.forms[1].paymentto.value =" ";
	}
	//OPD_4_hosptial
	 }
	if(!JS_SecondSubmit){
	 document.forms[1].mode.value="doSelectHospital";
	 document.forms[1].child.value="HospitalList";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 JS_SecondSubmit=true;
	 document.forms[1].submit();
	}
}//end of selectHospital()

function onClearHospital()
{
	document.forms[1].mode.value="doClearHospital";
    document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
}//end of onClearHospital()

function selectAuthorization()
{
	 document.forms[1].mode.value="doSelectAuthorization";
	 document.forms[1].child.value="SelectAuthorization";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of selectHospital()

function onEnhancementAmount()
{
	 document.forms[1].mode.value="doEnhancementAmount";
	 document.forms[1].child.value="SumInsuredList";
	 document.forms[1].action="/PreAuthGeneralAction.do";
	 document.forms[1].submit();
}//end of onEnhancementAmount()

function showhideadditionalinfo(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	var dagreeObj = document.getElementById("additionalinfo");
	dagreeObj.style.display="none";
	if(selVal == 'MAN')
	{
		dagreeObj.style.display="";
	}//end of if(selVal == 'MAN')
}//end of showhideadditionalinfo(selObj)

function onUserSubmit(){	
	
	 trimForm(document.forms[1]);	
	 if(!JS_SecondSubmit)
     {
		 dateValidation();
		 
		 if(document.forms[1].benefitType.value=="DNTL")
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
		 
		/* if(document.forms[1].modeOfClaim.value=="E_LM" || document.forms[1].modeOfClaim.value=="PCLM" || document.forms[1].modeOfClaim.value=="MCLM" || document.forms[1].modeOfClaim.value=="PLCL" || document.forms[1].modeOfClaim.value=="INTL"){
			 if(document.forms[1].benefitType.value=="OMTI" || document.forms[1].benefitType.value=="IMTI" ){
				
				 if(document.getElementById("natureOfConception").value ==''){
					 alert("Nature of Conception is Required ")
					 return false;
				 }
				 
				 if(document.getElementById("latMenstraulaPeriod").value ==''){
					 alert("Date Of LMP is Required ")
					 return false;
				 }
			 }
			 
		 }else if(document.forms[1].modeOfClaim.value=="PBCL") {
			 if(document.forms[1].benefitType.value=="IMTI"){
				 if(document.getElementById("natureOfConception").value ==''){
					 alert("Nature of Conception is Required ")
					 return false;
				 }
				 
				 if(document.getElementById("latMenstraulaPeriod").value ==''){
					 alert("Date Of LMP is Required ")
					 return false;
				 } 
			 }
		 }*/
		 
		 if(document.forms[1].modeOfClaim.value=="E_LM" || document.forms[1].modeOfClaim.value=="PCLM" || document.forms[1].modeOfClaim.value=="MCLM" || document.forms[1].modeOfClaim.value=="PLCL" || document.forms[1].modeOfClaim.value=="INTL"){
			 if(document.forms[1].benefitType.value=="DNTL"){
				 if(document.getElementById("treatmentTypeID").value==''){
				 alert("Select Treatment Type is Required ");
				 return false; 
				 }
			 }
		 }
		 
		 if(document.getElementById("memberSpecificRemarksid").value !=""){

			 var message=confirm(document.getElementById("memberSpecificRemarksid").value);
			 if(message){

				 document.forms[1].validateIcdCodeYN.value="N";
				   document.forms[1].mode.value="doSave";
				   document.forms[1].action="/UpdateClaimGeneral.do";
				   JS_SecondSubmit=true;
				   document.forms[1].submit();
				   return;
			 }else{
				 return;
			 }
		}
	   document.forms[1].validateIcdCodeYN.value="N";
	   document.forms[1].mode.value="doSave";
	   document.forms[1].action="/UpdateClaimGeneral.do";
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
	
}//end of onUserSubmit()

function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	{
		document.forms[1].mode.value="doReset";
		document.forms[1].action="/PreAuthGeneralAction.do";
		document.forms[1].submit();
	}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	else
	{
		document.forms[1].reset();
		//showhideCorporateDet(document.forms[1].elements['claimantDetailsVO.policyTypeID']);
	}//end of else
}//end of onReset()

function showhideCorporateDet(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	
	if(document.forms[1].flowType.value=="PRE")
	{
		var addInfo = document.getElementById("additionalinfo");
		var empNoLabel =document.getElementById("empNoLabel");
		var empNoField =document.getElementById("empNoField");
		var empName =document.getElementById("empName");
		var schemeName = document.getElementById("schemeName");
		
		if(selVal == 'COR')
		{
		   document.getElementById("corporate").style.display="";
		   //document.forms[1].elements['claimantDetailsVO.policyHolderName'].value="";
		   //document.forms[1].elements['claimantDetailsVO.policyHolderName'].disabled = true;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = true;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge textBoxDisabled";
		   if(document.forms[1].preAuthTypeID.value=="MAN")
		   {

			   addInfo.style.display="";
			   empNoLabel.style.display="";
			   empNoField.style.display="";
		       empName.style.display="";
		      //document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = false;
		   }//end of if(document.forms[1].preAuthTypeID.value=="MAN")
		    //document.forms[1].elements['additionalDetailVO.insScheme'].value="";
		   // document.forms[1].elements['additionalDetailVO.certificateNo'].value="";
		    schemeName.style.display="none";
		}//end of if(selVal == 'COR')
		
		else if(selVal == 'NCR'&& document.forms[1].preAuthTypeID.value=="MAN")
		{
			document.getElementById("corporate").style.display="";
			addInfo.style.display="";
			schemeName.style.display="";
			//document.forms[1].elements['additionalDetailVO.employeeName'].value="";
		    //document.forms[1].elements['additionalDetailVO.joiningDate'].value="";
		   	//document.forms[1].elements['additionalDetailVO.employeeNbr'].value="";
			empNoLabel.style.display="none";
			empNoField.style.display="none";
		    empName.style.display="none";
		}//end of
		
		else
		{
		   document.getElementById("corporate").style.display="none";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly =false;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge";
		   //document.forms[1].elements['additionalDetailVO.insScheme'].value="";
		   //document.forms[1].elements['additionalDetailVO.certificateNo'].value="";
		   addInfo.style.display="none";
		   empNoLabel.style.display="none";
		   empNoField.style.display="none";
		   empName.style.display="none";
		   schemeName.style.display="none";
		}//end of else
	}//end of if(document.forms[1].flowType.value=="PRE")
	else if(document.forms[1].flowType.value=="CLM")
	{
		if(selVal == 'COR')
		{
			document.getElementById("corporate").style.display="";
			//document.forms[1].elements['claimantDetailsVO.policyHolderName'].value="";
	   	    document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = true;
	   	    document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge textBoxDisabled";
		}else
		{
		   //document.getElementById("corporate").style.display="none";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly =false;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge";
		}//end of else if(document.forms[1].flowType.value=="CLM")
	}//end of else
	
	document.forms[1].mode.value="doChangePolicyType";
	document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
}//end of showhideCorporateDet(selObj)

//on Click of review button
function onReview()
{
    //trimForm(document.forms[1]);
    //if(!TrackChanges()) return false;
    if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Review");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	if(!JS_SecondSubmit)
     {
		document.forms[1].mode.value="doReviewInfo";
		document.forms[1].action="/UpdatePreAuthGeneral.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)

}//end of onReview()

function onRevert()
{
	if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Promote");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
    var message;
	if(document.forms[0].leftlink.value=='Pre-Authorization')
	{
		message=confirm('Cashless will be moved to the next level and if you do not have privileges, you may not see this Cashless.');
	}//end of if(document.forms[0].leftlink.value=='Pre-Authorization')
	else
	{
		message=confirm('Claim will be moved to the next level and if you do not have privileges, you may not see this Claim.');
	}//end of else if(document.forms[0].leftlink.value=='Pre-Authorization')
	if(message)
	{
		if(!JS_SecondSubmit)
        {
			document.forms[1].mode.value="doRevert";
			document.forms[1].action="/UpdatePreAuthGeneral.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}
	}//end of if(!JS_SecondSubmit)

}//end of onRevert()

//on Click of promote button
function onPromote()
{
    //trimForm(document.forms[1]);
    //if(!TrackChanges()) return false;
    if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Promote");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	var message;
	if(document.forms[0].leftlink.value=='Pre-Authorization')
	{
		message=confirm('Cashless will be moved to the next level and if you do not have privileges, you may not see this Cashless.');
	}//end of if(document.forms[0].leftlink.value=='Pre-Authorization')
	else
	{
		message=confirm('Claim will be moved to the next level and if you do not have privileges, you may not see this Claim.');
	}//end of else if(document.forms[0].leftlink.value=='Pre-Authorization')
	if(message)
	{
		if(!JS_SecondSubmit)
        {
			document.forms[1].mode.value="doReviewInfo";
			document.forms[1].action="/UpdatePreAuthGeneral.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
	}//end of if(message)
}//end of onPromote()

function onDiscrepancySubmit()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDiscrepancies";
	document.forms[1].child.value="Discrepancy";
	document.forms[1].action="/PreAuthGeneralAction.do";
	document.forms[1].submit();
}//end of function onDiscrepancySubmit()

function getPreauthReferenceNo(refNo)
{
	document.forms[1].DMSRefID.value=refNo;
}//end of getPreauthReferenceNo(refNo)

function showhideClaimSubType(selObj)
{

	var selVal = selObj.options[selObj.selectedIndex].value;
	var dagreeObj = document.getElementById("hospitalinfo");
	// Changes as per KOC1216C Change Request
	
	
	if(selVal != 'CSD')
	{
		document.getElementById('labelchange').innerHTML="Admission Date/Time:<span class=\"mandatorySymbol\">*</span>";
		document.getElementById('labelchange1').innerHTML="Discharge Date/Time:<span class=\"mandatorySymbol\">*</span>";
	    document.getElementById("domicilaryinfo").style.display="none";
	    document.getElementById("domicilaryinfocheckBox").style.display="none";
	}
	else{
		document.getElementById('labelchange').innerHTML="Treatment Commencement Date/Time:<span class=\"mandatorySymbol\">*</span>";
		document.getElementById('labelchange1').innerHTML="Treatment Completion Date/Time:<span class=\"mandatorySymbol\">*</span>";
	    document.getElementById("domicilaryinfo").style.display="";
	    document.getElementById("domicilaryinfocheckBox").style.display="";
	}
 // Changes as per KOC1216C Change Request
    if(selVal == 'OPD')
    {
          document.forms[1].elements['prevHospClaimSeqID'].disabled = true;
          document.getElementById("paymentto").style.display="";
          
    }
    else
    {
          document.forms[1].elements['prevHospClaimSeqID'].disabled = false;
          document.getElementById("paymentto").style.display="none";
    }
    if(selVal == 'CTL')
    {
		document.forms[1].elements['claimRequestAmount'].value=1;
	}
   // Changes as per KOC1216C Change Request
	if(selVal == 'CSD')
	{
	   document.getElementById("hospitalinfo").style.display="none";
	}//end of if(selVal == 'CSD')
	else
	{
	 	document.getElementById("hospitalinfo").style.display="";
	}//end of else if(selVal == 'CSD')
	//added for KOC-1273
	if((selVal == 'HCU')||(selVal == 'CTL'))
	{
	   document.getElementById("hospitalizationdetail").style.display="none";
	}//end of if(selVal == 'CSD')
	//ended
	else
	{
	 	document.forms[1].elements['prevHospClaimSeqID'].value = "";
	 	document.forms[1].elements['clmAdmissionDate'].value = "";
	 	document.forms[1].elements['clmAdmissionTime'].value = "";
	 	document.forms[1].elements['dischargeDate'].value = "";
	 	document.forms[1].elements['dischargeTime'].value = "";

	 	document.getElementById("hospitalizationdetail").style.display="";
	}//end of else if(selVal == 'CSD')

}//end of showhideClaimSubType(selObj)

function onOverride()
{
	if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Overriding");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doOverride";
	document.forms[1].action="/OverridePreAuthGeneral.do";
    JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onOverride()

function reassignEnrID()
{
	if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Re-associating");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doReassociateEnrollID";
	document.forms[1].action="/PreAuthGeneralAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of reassignEnrID()

function onDocumentLoad()
{
		selObj = document.forms[1].elements['claimantDetailsVO.policyTypeID'];
		selVal =selObj.options[selObj.selectedIndex].value;
		var addInfo = document.getElementById("additionalinfo");
		var empNoLabel =document.getElementById("empNoLabel");
		var empNoField =document.getElementById("empNoField");
		var empName =document.getElementById("empName");
		var schemeName = document.getElementById("schemeName");
		
		if(selVal == 'COR')
		{
		   document.getElementById("corporate").style.display="";
	   	   document.forms[1].elements['claimantDetailsVO.policyHolderName'].value="";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly = true;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge textBoxDisabled";
		   if(document.forms[1].preAuthTypeID.value=="MAN")
		   {
			   addInfo.style.display="";
			   empNoLabel.style.display="";
			   empNoField.style.display="";
		   	   empName.style.display="";
		   }//end of if(document.forms[1].preAuthTypeID.value=="MAN")
		   schemeName.style.display="none";
		}//end of if(selVal == 'COR')
		else if(selVal == 'NCR'&& document.forms[1].preAuthTypeID.value=="MAN")
		{
			document.getElementById("corporate").style.display="";
			addInfo.style.display="";
			schemeName.style.display="";
			empNoLabel.style.display="none";
			empNoField.style.display="none";
		  	empName.style.display="none";
		}//end of
		else
		{
		   document.getElementById("corporate").style.display="none";
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].readOnly =false;
		   document.forms[1].elements['claimantDetailsVO.policyHolderName'].className = "textBox textBoxLarge";
		   addInfo.style.display="none";
		   schemeName.style.display="none";
		   empNoLabel.style.display="none";
		   empNoField.style.display="none";
		   empName.style.display="none";
		}//end of else
}//end of onDocumentLoad()
//KOC 1285 Change Request  
function onDoctorCertificate(){
	var doctorCertificateYN=document.forms[1].elements['claimDetailVO.doctorCertificateYN'];
	 if(doctorCertificateYN.checked!=true)	 { 
		 document.forms[1].doctorCertificateYN.value="N";
	     }
	 else{
		 document.forms[1].doctorCertificateYN.value="Y";
		 }
		 
}
function onUnfreeze(){
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDefault";
	//document.forms[1].child.value="Discrepancy";
	document.forms[1].action="/FileUploadUnfreeze.do";
	document.forms[1].submit();
}
//KOC FOR Grievance
/*function call()
{
    popup = window.open('http://www.google.co.in');         
    setInterval(function() {wait();},4000);
} */  
/*function selectageID()
{
    setInterval(function() {call();},5000);
}*/
/*function wait()
{
    popup.close();
}*/
function selectageID()
{

	var gender=document.forms[1].elements['claimantDetailsVO.genderTypeID'].value;
	var age=document.forms[1].elements['claimantDetailsVO.age'].value;
	
	
	if(((gender == "MAL") && (age >= 60)) || ((gender == "FEM") && (age >= 60)))
	{
		alert("Senior Citizen – Prioritize");
	}
	
	
}//end of selectEnrollmentID()
function getMemberDetails(){
	 if(!confirm("You Want To Validate Member ID?"))return;
	 
   document.forms[1].mode.value="getMemberDetails";
   document.forms[1].action = "/ClaimGeneralAction.do";    
   document.forms[1].submit();
}//end of getMemberDetails()
function onInsOverrideConf(){
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doDefault";
	//document.forms[1].child.value="Discrepancy";
	document.forms[1].action="/FileInsOverrideConf.do";
	document.forms[1].submit();
}
function selectDiagnosisCode(){
	if(!JS_SecondSubmit){
	  document.forms[1].mode.value="doGeneral";
	   document.forms[1].reforward.value="diagnosisSearch";
	   document.forms[1].action="/ClaimGeneralAction.do?focusObj=icdCode";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
	}
}

function editDiagnosisDetails(rownum){
	if(!JS_SecondSubmit)
	 {	
		document.forms[1].rownum.value=rownum;
	   document.forms[1].mode.value="editDiagnosisDetails";
	   document.forms[1].action="/ClaimGeneralAction.do";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
	 }		
	}
function deleteDiagnosisDetails(rownum){
 if(confirm("Are You Sure You Want To Delete Diagnosis Details!")){
	if(!JS_SecondSubmit){	
		document.forms[1].rownum.value=rownum;
	   document.forms[1].mode.value="deleteDiagnosisDetails";
	   document.forms[1].child.value="DeleteDiagnosisDetails";
	   document.forms[1].action="/ClaimGeneralAction.do?focusObj=ailmentDescription";	
	   JS_SecondSubmit=true;	   
	   document.forms[1].submit();
	 }	
	 }	
	}	
function onRejectClaim(claimSeqID,denialcode,medicalOpinionRemarks,overrideRemarks,finalRemarks,internalRemarks) {
	window.opener.document.getElementById("mode").value="rejectClaim";
	window.opener.document.forms[1].child.value="FinalSavePermission";
	window.opener.document.forms[1].action="/ClaimGeneralAction.do?claimSeqID="+claimSeqID+"&denialcode="+denialcode+"&medicalOpinionRemarks="+medicalOpinionRemarks+"&overrideRemarks="+overrideRemarks+"&checkFlag=YES"+"&finalRemarks="+finalRemarks+'&internalRemarks='+internalRemarks;
	//JS_SecondSubmit=true;
	window.opener.document.forms[1].submit();
}

	
function saveAndCompleteClaim(){
	var v = document.getElementById("vipmemberuseraccesspermissionflagid").value;
	var userSeqID = document.getElementById("userSeqID").value;
	//var exceptionFalg = document.getElementById("exceptionFalg").value;
	var exceptionFalg =   document.forms[1].exceptionFalg.value;
	//var assigntouserseqid = document.getElementById("assigntouserseqid").value;
	//var vipYorN = document.getElementById("vipYorN").value;
if((document.getElementById("vipmemberuseraccesspermissionflagid").value !="Y" && document.getElementById("vipmemberuseraccesspermissionflagid").value != null) && (document.forms[1].claimStatus.value=="REJ") && (document.getElementById("userSeqID").value == document.getElementById("assigntouserseqid").value) && (document.getElementById("vipYorN").value == "Yes")){
		
		if(!confirm("VIP Member Claim can be rejected only through\n Authorized user.  Please assign Claim to authorized user.")) return ;
		document.forms[1].mode.value="doAssign";
	  	document.forms[1].child.value="Assign";
	  	document.forms[1].action="/AssignToAction.do";
	  	document.forms[1].submit();
	  	
		
  }else if((document.getElementById("vipmemberuseraccesspermissionflagid").value =="Y" && document.getElementById("vipmemberuseraccesspermissionflagid").value != null) && (document.forms[1].claimStatus.value=="REJ") && (document.getElementById("userSeqID").value == document.getElementById("assigntouserseqid").value) && (document.getElementById("vipYorN").value == "Yes")){
        
	  if(!confirm("VIP Member Claim Rejection. \nPlease confirm before rejecting the request."))return ;

		if(document.forms[1].approvedAmount.value)
			{
			Alert.render('<table><tr><td align=center>Approved Amount is not equal to Zero. </td></tr><tr><td align=center>Claim Cannot be Rejected .</td></tr><tr><td align=center>However To Proceed With Entire Claim Rejection.<br/>Please &nbsp;<a  onclick=Alert.ok()></u><b><u>Click here</b></a></td><tr>');
		}
		else
		{
		 if(!JS_SecondSubmit){
			 var isVisible=false;
			 if(document.getElementById('recheckedButtonId')){
				 isVisible=true;
			 }
			 if(isVisible==true){
				 var recheckedButtonIdVal=document.getElementById('recheckedButtonId').checked;
				 if(recheckedButtonIdVal==true){
					 var recheckRemarks=document.forms[1].recheckRemarks.value;
					 if(recheckRemarks.length!=0){
						 if(recheckRemarks.length<10){
							 alert("\'Recheck done Remarks\' Should Not Less Than 10 Characters");
							 return;
						 }else if(recheckRemarks.length>250){
							 alert("\'Recheck done Remarks\' Should Not More Than 250 Characters");
							 return;
						 }else{
							 	document.forms[1].mode.value="saveAndCompleteClaim";
							 	document.forms[1].child.value="FinalSavePermission";
								document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
								JS_SecondSubmit=true;
								document.forms[1].submit(); 
						 }
					 }else{
						 alert('Please Provide \'Recheck done Remarks!\'');
					 }
					 	
				 }else{
					 alert('Please select \'concurrent claim audit status!\'');
				 }
			 }else{
				 document.forms[1].mode.value="saveAndCompleteClaim";
				 document.forms[1].child.value="FinalSavePermission";
					document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
					JS_SecondSubmit=true;
					document.forms[1].submit();
			 }
		  //     if(!confirm("Do you want complete the VIP claim?"))return ;
				
	    }
	
	}
		
  }else if(document.forms[1].claimStatus.value=="REJ"){
		if(document.forms[1].approvedAmount.value)
			{
			Alert.render('<table><tr><td align=center>Approved Amount is not equal to Zero. </td></tr><tr><td align=center>Claim Cannot be Rejected .</td></tr><tr><td align=center>However To Proceed With Entire Claim Rejection.<br/>Please &nbsp;<a  onclick=Alert.ok()></u><b><u>Click here</b></a></td><tr>');
		}
		else
		{
		 if(!JS_SecondSubmit){
			 var isVisible=false;
			 if(document.getElementById('recheckedButtonId')){
				 isVisible=true;
			 }
			 if(isVisible==true){
				 var recheckedButtonIdVal=document.getElementById('recheckedButtonId').checked;
				 if(recheckedButtonIdVal==true){
					 var recheckRemarks=document.forms[1].recheckRemarks.value;
					 if(recheckRemarks.length!=0){
						 if(recheckRemarks.length<10){
							 alert("\'Recheck done Remarks\' Should Not Less Than 10 Characters");
							 return;
						 }else if(recheckRemarks.length>250){
							 alert("\'Recheck done Remarks\' Should Not More Than 250 Characters");
							 return;
						 }
						 else{
							 	document.forms[1].mode.value="saveAndCompleteClaim";
							 	document.forms[1].child.value="FinalSavePermission";
								document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
								JS_SecondSubmit=true;
								document.forms[1].submit(); 
						 }
					 }else{
						 alert('Please Provide \'Recheck done Remarks!\'');
					 }
					 	
				 }else{
					 alert('Please select \'concurrent claim audit status!\'');
				 }
			 }else{
				 
				 if(exceptionFalg=="YES"){
						var message=confirm('Do you want to reject the  exceptional handling claim');
						if(message)
						{
							document.forms[1].mode.value="saveAndCompleteClaim";
							 document.forms[1].child.value="FinalSavePermission";
								document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
								JS_SecondSubmit=true;
								document.forms[1].submit();	  
						}
					else{
					return false;
					}	
					}
					else{
						 if(!confirm("Do you want complete the claim?"))return ;
						 document.forms[1].mode.value="saveAndCompleteClaim";
						 document.forms[1].child.value="FinalSavePermission";
							document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
							JS_SecondSubmit=true;
							document.forms[1].submit();
							
					} 
				
			 }
	    }
	
		}
		}

  else if(document.forms[1].claimStatus.value=="APR"){
	  var bankDeatailsFlag=document.forms[1].bankDeatailsFlag.value;
	  if(bankDeatailsFlag=="N"){
		  var msg = confirm("Member account details not available/incorrect please confirm if the claim can be moved to the next level");
			if(msg)
			{
				 if(!confirm("Do you want complete the claim?"))return ;
				 document.forms[1].mode.value="saveAndCompleteClaim";
				 document.forms[1].child.value="FinalSavePermission";
					document.forms[1].action="/ClaimGeneralAction.do";
					JS_SecondSubmit=true;
					document.forms[1].submit();
			  
		  }//end of if(msg)
	  }
	  else{

		  if(exceptionFalg=="YES"){
				var message=confirm('Do you want to approve the  exceptional handling claim');
				if(message)
				{
					document.forms[1].mode.value="saveAndCompleteClaim";
					 document.forms[1].child.value="FinalSavePermission";
						document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
						JS_SecondSubmit=true;
						document.forms[1].submit();	  
				}
			else{
			return false;
			}	
			}
			else{
				 if(!confirm("Do you want complete the claim?"))return ;
				 document.forms[1].mode.value="saveAndCompleteClaim";
				 document.forms[1].child.value="FinalSavePermission";
					document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
					JS_SecondSubmit=true;
					document.forms[1].submit();
					
			} 
	  }
	  
  }
	else
		{
	 if(!JS_SecondSubmit){
		 var isVisible=false;
		 if(document.getElementById('recheckedButtonId')){
			 isVisible=true;
		 }
		 if(isVisible==true){
			 var recheckedButtonIdVal=document.getElementById('recheckedButtonId').checked;
			 if(recheckedButtonIdVal==true){
				 var recheckRemarks=document.forms[1].recheckRemarks.value;
				 if(recheckRemarks.length!=0){
					 if(recheckRemarks.length<10){
						 alert("\'Recheck done Remarks\' Should Not Less Than 10 Characters");
						 return;
					 }else if(recheckRemarks.length>250){
						 alert("\'Recheck done Remarks\' Should Not More Than 250 Characters");
						 return;
					 }else{
						 	document.forms[1].mode.value="saveAndCompleteClaim";
						 	document.forms[1].child.value="FinalSavePermission";
							document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";
							JS_SecondSubmit=true;
							document.forms[1].submit(); 
					 }
				 }else{
					 alert('Please Provide \'Recheck done Remarks!\'');
				 }
				 	
			 }else{
				 alert('Please select \'concurrent claim audit status!\'');
			 }
		 }else{
			 if(!confirm("Do you want complete the claim?"))return ;
			 document.forms[1].mode.value="saveAndCompleteClaim";
			 document.forms[1].child.value="FinalSavePermission";
				document.forms[1].action="/ClaimGeneralAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
		 }	
	    }
		}
} // end of saveAndCompleteClaim()


function CustomAlert(){
    this.render = function(dialog){
        var winW = window.innerWidth;
        var winH = window.innerHeight;
        var dialogoverlay = document.getElementById('dialogoverlay');
        var dialogbox = document.getElementById('dialogbox');
        dialogoverlay.style.display = "block";
        //dialogoverlay.style.height = winH+"px";
        //dialogbox.style.left = (winW/2) - (550 * .5)+"px";
       // dialogbox.style.top = "100px";
        dialogbox.style.display = "block";
      /*  document.getElementById('dialogboxhead').innerHTML = "Acknowledge This Message";*/
        document.getElementById('dialogboxbody').innerHTML =dialog;
        
       document.getElementById('dialogboxfoot').innerHTML = '<button onclick="Alert.cancel()">Cancel</button>'; 
    };
	this.ok = function(){
		document.getElementById('dialogbox').style.display = "none";
		document.getElementById('dialogoverlay').style.display = "none";
		 popupWindow=window.open("/ClaimDiagnosisDetails.do?claimSeqID="+document.forms[1].claimSeqID.value+"&medicalOpinionRemarks="+document.forms[1].medicalOpinionRemarks.value+"&overrideRemarks="+document.forms[1].overrideRemarks.value+"&finalRemarks="+document.forms[1].finalRemarks.value+"&internalRemarks="+document.forms[1].internalRemarks.value,"CLAIM","toolbar=no,scrollbars=yes,status=no,menubar=0");
		  document.onmousedown=focusPopup; 
		  document.onkeyup=focusPopup; 
		  document.onmousemove=focusPopup;
	};
	this.cancel = function(){
		
		document.getElementById('dialogbox').style.display = "none";
		document.getElementById('dialogoverlay').style.display = "none";
	};
}
var Alert = new CustomAlert();

function addClaimShortFalls(){
	if(!JS_SecondSubmit){
    document.forms[1].mode.value="doGeneral";
    document.forms[1].reforward.value="claimshortfalls";		        	       
    document.forms[1].action = "/ClaimGeneralAction.do";  
	 JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of addClaimShortFalls()

function doViewShortFalls(claimSeqId1,shortFallSeqId1){
	if(!JS_SecondSubmit){
	document.forms[1].reforward.value="viewShortfalls";	
    document.forms[1].mode.value="doGeneral";
    document.forms[1].shortFallSeqId.value=shortFallSeqId1;
    document.forms[1].claimSeqID.value=claimSeqId1;
    document.forms[1].action = "/ClaimGeneralAction.do";   
	JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of doViewShortFall()


function deleteShortfallsDetails(claimSeqId1,shortFallSeqId1){
	   if(confirm("Are You Sure You Want To Delete Shortfalls Details!")){						   
		if(!JS_SecondSubmit){								
		   document.forms[1].claimSeqID.value=claimSeqId1;
		   document.forms[1].shortFallSeqId.value=shortFallSeqId1;
		   document.forms[1].mode.value="deleteShortfallsDetails";
		   document.forms[1].action="/ClaimGeneralAction.do?focusObj=finalRemarks";	
		   JS_SecondSubmit=true;	   
		   document.forms[1].submit();						   
		     }		
	      }
		}

function selectAuthorizationdetails(){
	 var memberSeqID=document.forms[1].memberSeqID.value;
	 if(memberSeqID==null||memberSeqID===""||memberSeqID.length<1){
		 alert("Validate Member Id");return ;
	 }
	 if(!JS_SecondSubmit){
    document.forms[1].mode.value="doGeneral";
    document.forms[1].reforward.value="selectAuthorizationdetails";		        	       
    document.forms[1].action = "/ClaimGeneralAction.do";    
	 JS_SecondSubmit=true;
    document.forms[1].submit();
	 }
}//end of selectAuthorizationdetails()

function deleteAuthorizationdetails(){
	if(!JS_SecondSubmit){   
		  document.forms[1].mode.value="deleteAuthorizationdetails";	
		  document.forms[1].action="/ClaimGeneralAction.do";	
		  JS_SecondSubmit=true;
		  document.forms[1].submit();
	}
}//end of deleteAuthorizationdetails()

function overrideActivityDetails(activityDtlSeqId1){	
	var complYN=document.forms[1].claimCompleteStatus.value;
	var revertFalg = document.forms[1].revertFlag.value;	
	if(revertFalg === 'N'){
		alert("Please Calculate Before Override");
		return false;	
	}
	
	if(complYN==="Y"){
		alert("Completed  Claim Can Not Modify");
		return;
	}
	if(!JS_SecondSubmit){ 
		 if(!confirm("You Want Override This Activity Code Details?")) return;
		    document.forms[1].reforward.value="overrideActivity";	
			document.forms[1].activityDtlSeqId.value=activityDtlSeqId1;
			document.forms[1].override.value="Y";
			var rsData=false;
			var restMsg="";
			//document.forms[1].child.value="Override";
			$(document).ready(function () {
				$.ajax({
		            url :"/asynchronAction.do?mode=checkAccessForOverride&activityDtlSeqId="+activityDtlSeqId1,
		            dataType:"text",
					type:"POST",
					async:false,
				    context:document.body,
		            success : function(responseData) {
		            	var respData=[];
		            	respData=responseData.split("@");
		            	rsData=respData[0];
		            	restMsg=respData[1];
		              }//success data
				 });//$.ajax(	
				});
			
			if(rsData=='false'){
				confirm("User Override Option is restricted for this denial code");
			}
			else{
				document.forms[1].mode.value="doGeneral";
				document.forms[1].action="/ClaimGeneralAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}
		}
	}
function overridClaimDetails(){	
	
	
	
	if(!confirm("You Want Override Claim Details?")) return;	
	else{/*
		var overrRemarks=prompt("Enter Override Remarks","");
		 overrRemarks =jQuery.trim(overrRemarks);
		if(overrRemarks==null||overrRemarks==="" || overrRemarks.length ==0){
	
		 alert("Override Remarks Are Required");
		 return;
	 }else if(overrRemarks.length<20){
		 alert("Override Remarks Should Not Less Than 20 Characters");
		 return;
	 }
    document.forms[1].overrideRemarks.value=overrRemarks;
	if(!JS_SecondSubmit){   
	document.forms[1].mode.value="overridClaimDetails";
	document.forms[1].action="/ClaimGeneralAction.do";
	 JS_SecondSubmit=true;
	document.forms[1].submit();
	}*/
		  popupWindow=window.open("/ClaimGeneralOverrideAction.do?mode=doOverrideClaim","Claim","toolbar=no,scrollbars=yes,status=no,menubar=0,width=470,height=270");
		  document.onmousedown=focusPopup; 
		  document.onkeyup=focusPopup; 
		  document.onmousemove=focusPopup;
	}
}

//end date validation
function endDateValidation()
{
	var startDate =document.getElementById("admissionDate").value;    	
    var endDate=document.getElementById("dischargeDate").value;				
    
    if( !((document.getElementById("admissionDate").value)==="") && !((document.getElementById("dischargeDate").value)===""))
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
       	 	alert("End Date should be greater than or equal to Start Date");
       		document.getElementById("dischargeDate").value="";
       		return ;
       	 }
   	} 
}	

function clearField()
{
	var crate = document.forms[1].conversionRate.value;
	if(crate == "")
	{
		document.forms[1].convertedAmount.value="";
		
	}else if(crate == "0")
		{
		alert("Conversion Rate should be greater than Zero");
		document.forms[1].conversionRate.value = "";
		document.forms[1].convertedAmount.value="";
		document.forms[1].focusID.value ="conversionRate";
		return false;
		}
}

function QARValidation() 
{
if(document.getElementById("requestedAmountcurrencyType").value=="QAR")
	{
	document.getElementById("conversionRate").value="";
	document.getElementById("convertedAmount").value=document.getElementById("requestedAmount").value;
	document.getElementById("conversionRate").className="textBox textBoxSmall textBoxDisabled";
	document.getElementById("conversionRate").readOnly="true";
	}
else
	{
	document.getElementById("conversionRate").className="textBox textBoxSmall";
	document.getElementById("conversionRate").readOnly="";
	}

}

function enableConversionRate() 
{
	
var converionRateYN=document.getElementById("converionRateYN").checked;
if(converionRateYN) document.getElementById("conversionRatediv").style.display="";
else document.getElementById("conversionRatediv").style.display="none";
}

function clearConversionRate() {
	document.getElementById("conversionRate").value="";
	document.getElementById("convertedAmount").value="";
}





function changeTreatmentType(obj){	
	if(!JS_SecondSubmit){
	 document.forms[1].mode.value="doChangeTreatmentType";
 	 document.forms[1].action="/ClaimGeneralAction.do";
	 JS_SecondSubmit=true;
 	 document.forms[1].submit();
	}
}
function changeTreatment(obj){
	  if(obj==='1' || obj==='3')
		  document.getElementById("orthoDiv").style.display="inline";
	  else
		  document.getElementById("orthoDiv").style.display="none";
}

function checkDentoCombo(obj,type){
	  var overjet		=	document.getElementById("overJet").value;
	  var reverseJet	=	document.getElementById("reverseJet").value;
	  
	  if(overjet!='' && reverseJet!=''){
		  alert(type+" cannot be combined");
		  obj.value='';
		  return false;
	  }
	  if(reverseJet!='' && (document.getElementById("overBiteD").checked || document.getElementById("overBiteC").checked
			  	|| document.getElementById("overBiteI").checked))
	  {
		  alert(type+" cannot be combined");
		  if(obj.type=='radio')
			  obj.checked	=	false;
		  else
			  obj.value='';
		  return false;
	  }
	  if((document.getElementById("overBiteD").checked || document.getElementById("overBiteC").checked
			  	|| document.getElementById("overBiteI").checked) &&
	  	(document.getElementById("openbiteAntrio").value!='' || document.getElementById("openbitePosterior").value!=''
			  	|| document.getElementById("openbiteLateral").value!='')  	)
	  {
		  alert(type+" cannot be combined");
		  if(obj.type=='radio')
			  obj.checked	=	false;
		  else
			  obj.value='';
		  return false;
	  }
}
function checkToothNo(obj,type){
	  
	  //Hypodentia with all check
	  var  hypodontiaAllteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+","
									+document.getElementById("hypodontiaQuand2Teeth").value+","
									+document.getElementById("hypodontiaQuand3Teeth").value+","
									+document.getElementById("hypodontiaQuand4Teeth").value;

	  var impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  var impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  var submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
	  var ectopicTeethNo		 = document.getElementById("ectopicTeethNo").value;

	  
if(hypodontiaAllteeth!="" || hypodontiaAllteeth!=null){
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  impededTeethEruptionNo	=	hypodontiaAllteeth.search(impededTeethEruptionNo);
		  if(impededTeethEruptionNo!='-1')
		  {
			  alert(type+" should not be same tooth number 1");
			  obj.value='';
			  return false;
		  }
	  }
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  impededTeethNo			=	hypodontiaAllteeth.search(impededTeethNo);
		  if(impededTeethNo!='-1')
		  {
			  alert(type+" should not be same tooth number 2");
			  obj.value='';
			  return false;
		  }
	  }
	  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
		  submergerdTeethNo			=	hypodontiaAllteeth.search(submergerdTeethNo);
		  if(submergerdTeethNo!='-1')
		  {
			  alert(type+" should not be same tooth number 3");
			  obj.value='';
			  return false;
		  }
	  }
	  
	}

}

function checkSubmergerd(obj,type){
		// submerged with Impacted check
	  if((submergerdTeethNo!=null || submergerdTeethNo!='') && (impededTeethNo!=null || impededTeethNo!='')){
		  alert(submergerdTeethNo);
		  alert(impededTeethNo);
		  if(submergerdTeethNo===impededTeethNo)
		  {
			  alert(type+" should not be same tooth number4");
			  obj.value='';
			  return false;
		  }
	  }
		// submerged with Impeded check
	  if((submergerdTeethNo!=null || submergerdTeethNo!='') && (impededTeethEruptionNo!=null || impededTeethEruptionNo!='')){
		  if(submergerdTeethNo===impededTeethEruptionNo)
		  {
			  alert(type+" should not be same tooth number5");
			  obj.value='';
			  return false;
		  }
	  }
		// submerged with Ectopic check
	  if((submergerdTeethNo!=null || submergerdTeethNo!='') && (ectopicTeethNo!=null || ectopicTeethNo!='')){
		  if(submergerdTeethNo===ectopicTeethNo)
		  {
			  alert(type+" should not be same tooth number6");
			  obj.value='';
			  return false;
		  }
	  }
}



function checkImpededTooth(obj,type){
	  
	  //Hypodentia with all check
	  var  hypodontiaAllteeth	=	document.getElementById("hypodontiaQuand1Teeth").value+","
									+document.getElementById("hypodontiaQuand2Teeth").value+","
									+document.getElementById("hypodontiaQuand3Teeth").value+","
									+document.getElementById("hypodontiaQuand4Teeth").value;

	  var impededTeethEruptionNo = document.getElementById("impededTeethEruptionNo").value;
	  var impededTeethNo		 = document.getElementById("impededTeethNo").value;
	  var submergerdTeethNo		 = document.getElementById("submergerdTeethNo").value;
	  var ectopicTeethNo		 = document.getElementById("ectopicTeethNo").value;

if("Impeded"==type){
	 
	  
	  if((impededTeethNo!=null || impededTeethNo!='') && (impededTeethEruptionNo!=null || impededTeethEruptionNo!='')){
		  if(impededTeethNo==impededTeethEruptionNo)
		  {
			  alert(type+" should not be same tooth number 1");
			  obj.value='';
			  return false;
		  }
	  }
	  
	  if(impededTeethEruptionNo!='' && impededTeethEruptionNo!=null){
		  impededTeethEruptionNo	=	hypodontiaAllteeth.search(impededTeethEruptionNo);
		  if(impededTeethEruptionNo!='-1')
		  {
			  alert(type+" should not be same tooth number 2");
			  obj.value='';
			  return false;
		  }
	  }
	  if((submergerdTeethNo!=null || submergerdTeethNo!='') && (impededTeethEruptionNo!=null || impededTeethEruptionNo!='')){
		  if(submergerdTeethNo==impededTeethEruptionNo)
		  {
			  alert(type+" should not be same tooth number 3");
			  obj.value='';
			  return false;
		  }
	  }
	 
	 
	  if((ectopicTeethNo!=null || ectopicTeethNo!='') && (impededTeethEruptionNo!=null || impededTeethEruptionNo!='')){
		  if(ectopicTeethNo==impededTeethEruptionNo)
		  {
			  alert(type+" should not be same tooth number 4");
			  obj.value='';
			  return false;
		  }
	  }
	  }//Impeded

if("Impacted"==type){
	  alert("impededTeethNo::"+impededTeethNo);
	  alert("impededTeethEruptionNo::"+impededTeethEruptionNo);
	  alert("submergerdTeethNo::"+submergerdTeethNo);
	  alert("ectopicTeethNo::"+ectopicTeethNo);
	  
	  if((impededTeethNo!=null || impededTeethNo!='') && (impededTeethEruptionNo!=null || impededTeethEruptionNo!='')){
		  if(impededTeethNo==impededTeethEruptionNo)
		  {
			  alert(type+" should not be same tooth number 5");
			  obj.value='';
			  return false;
		  }
	  }
	  
	  if(impededTeethNo!='' && impededTeethNo!=null){
		  impededTeethNo	=	hypodontiaAllteeth.search(impededTeethNo);
		  if(impededTeethNo!='-1')
		  {
			  alert(type+" should not be same tooth number 6");
			  obj.value='';
			  return false;
		  }
	  }
	  if((submergerdTeethNo!=null || submergerdTeethNo!='') && (impededTeethNo!=null || impededTeethNo!='')){
		  if(submergerdTeethNo==impededTeethNo)
		  {
			  alert(type+" should not be same tooth number 7");
			  obj.value='';
			  return false;
		  }
	  }
	 
	 
	  if((ectopicTeethNo!=null || ectopicTeethNo!='') && (impededTeethNo!=null || impededTeethNo!='')){
		  if(ectopicTeethNo==impededTeethNo)
		  {
			  alert(type+" should not be same tooth number 8");
			  obj.value='';
			  return false;
		  }
	  }
	  }//Impacted
if("Submerged"==type){
	 
	  
	  if((submergerdTeethNo!=null || submergerdTeethNo!='') && (impededTeethEruptionNo!=null || impededTeethEruptionNo!='')){
		  if(submergerdTeethNo==impededTeethEruptionNo)
		  {
			  alert(type+" should not be same tooth number 9");
			  obj.value='';
			  return false;
		  }
	  }
	  
	  if(submergerdTeethNo!='' && submergerdTeethNo!=null){
		  submergerdTeethNo	=	hypodontiaAllteeth.search(submergerdTeethNo);
		  if(submergerdTeethNo!='-1')
		  {
			  alert(type+" should not be same tooth number 10");
			  obj.value='';
			  return false;
		  }
	  }
	  if((submergerdTeethNo!=null || submergerdTeethNo!='') && (impededTeethNo!=null || impededTeethNo!='')){
		  if(submergerdTeethNo==impededTeethNo)
		  {
			  alert(type+" should not be same tooth number 11");
			  obj.value='';
			  return false;
		  }
	  }
	 
	 
	  if((ectopicTeethNo!=null || ectopicTeethNo!='') && (submergerdTeethNo!=null || submergerdTeethNo!='')){
		  if(ectopicTeethNo==submergerdTeethNo)
		  {
			  alert(type+" should not be same tooth number 12");
			  obj.value='';
			  return false;
		  }
	  }
	  }//Submerged
if("Ectopic"==type){
	 
	  if((ectopicTeethNo!=null || ectopicTeethNo!='') && (impededTeethEruptionNo!=null || impededTeethEruptionNo!='')){
		  if(ectopicTeethNo==impededTeethEruptionNo)
		  {
			  alert(type+" should not be same tooth number 9");
			  obj.value='';
			  return false;
		  }
	  }
	  
	  if(ectopicTeethNo!='' && ectopicTeethNo!=null){
		  ectopicTeethNo	=	hypodontiaAllteeth.search(ectopicTeethNo);
		  if(ectopicTeethNo!='-1')
		  {
			  alert(type+" should not be same tooth number 10");
			  obj.value='';
			  return false;
		  }
	  }
	  if((ectopicTeethNo!=null || ectopicTeethNo!='') && (impededTeethNo!=null || impededTeethNo!='')){
		  if(ectopicTeethNo==impededTeethNo)
		  {
			  alert(type+" should not be same tooth number 11");
			  obj.value='';
			  return false;
		  }
	  }
	 
	 
	  if((ectopicTeethNo!=null || ectopicTeethNo!='') && (submergerdTeethNo!=null || submergerdTeethNo!='')){
		  if(ectopicTeethNo==submergerdTeethNo)
		  {
			  alert(type+" should not be same tooth number 12");
			  obj.value='';
			  return false;
		  }
	  }
	  }//Ectopic
}

function onPolicyTob()
{
	var prodPolicySeqId=document.getElementById("prodPolicySeqId").value;

	   //	alert("hello::::"+claimSeqID);
	   	var openPage = "/ViewTOBClaimGeneralAction.do?mode=doViewPolicyTOB&policySeqId="+prodPolicySeqId;	 
	   	var w = screen.availWidth - 10;
	  	var h = screen.availHeight - 49;
	  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	  	window.open(openPage,'',features);
}

function fillFraudDeatils(){
	
	var clmSeqId = document.forms[1].clmSeqId.value;	
//	var completedYN = document.forms[1].completedYN.value;
	var activelink= document.forms[1].leftlink.value;
	if(activelink=="CounterFraudDept"){
		document.forms[1].mode.value="viewCFDClaimAsSuspect"; 
		document.forms[1].action="/CounterFraudSearchAction.do?claimSeqID="+clmSeqId;
	}else{
	document.forms[1].mode.value="markClaimAsSuspect";
	document.forms[1].action="/ClaimInternalRemarksAction.do?claimSeqID="+clmSeqId;
	}
	
	/*var openPage = "/ttk/claims/internalRemark.jsp?clmSeqId="+clmSeqId+"&completedYN="+completedYN;
	var features = "scrollbars=0,status=0,toolbar=0,top=100,left=200,resizable=0,menubar=0,width=800,height=420";*/
	document.forms[1].submit();
  //  window.open(openPage,'',features);
	
}

function showFraudDeatils(){
	
	/*var clmSeqId = document.forms[1].clmSeqId.value;
	var completedYN = document.forms[1].completedYN.value;
	var openPage = "/ClaimGeneralAction.do?mode=viewFraudData&clmSeqId="+clmSeqId+"&completedYN="+completedYN;
	var features = "scrollbars=0,status=0,toolbar=0,top=250,left=250,resizable=0,menubar=0,width=800,height=420";
    window.open(openPage,'',features);*/
	
	var clmSeqId = document.forms[1].clmSeqId.value;	
//	var completedYN = document.forms[1].completedYN.value;
	var activelink= document.forms[1].leftlink.value;
	if(activelink=="CounterFraudDept"){
		document.forms[1].mode.value="viewCFDPreauthAndClaimAsSuspect"; 
		document.forms[1].action="/CounterFraudSearchAction.do?claimSeqID="+clmSeqId;
	}else{
	document.forms[1].mode.value="dogetFraud";
	document.forms[1].action="/ClaimInternalRemarksAction.do?claimSeqID="+clmSeqId;
	}
	
	/*var openPage = "/ttk/claims/internalRemark.jsp?clmSeqId="+clmSeqId+"&completedYN="+completedYN;
	var features = "scrollbars=0,status=0,toolbar=0,top=100,left=200,resizable=0,menubar=0,width=800,height=420";*/
	document.forms[1].submit();
	
}

function viewClaimDetails(claimSeqID) {
	window.opener.document.getElementById("mode").value="doView";
	window.opener.document.forms[1].action="/ClaimGeneralAction.do?claimSeqID="+claimSeqID;
	window.opener.document.forms[1].submit();
	
}

function displayBenefitsDetails(){
	if(!JS_SecondSubmit){
	   document.forms[1].mode.value="viewBenefitDetails";
	   document.forms[1].action="/ClaimGeneralAction.do";	
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}
}



function onUserIcon()
{
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doAssign";
	document.forms[1].child.value="Assign";
	document.forms[1].action="/AssignToAction.do";
	 JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onUserIcon()

function viewClaimDetail(claimSeqID) {
	if(!JS_SecondSubmit){
	document.forms[1].mode.value="doView";
	document.forms[1].action="/ClaimGeneralAction.do?claimSeqID="+claimSeqID;
	 JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

// added by govind
function qtyAndDaysAlertClm(){
		var qtyAndDaysAlert =  document.forms[1].qtyAndDaysAlert.value;
		if(qtyAndDaysAlert != ""){
			
			alert(qtyAndDaysAlert);
			
		
		}
	}

function onClaimFroudHistory(){
	if(!JS_SecondSubmit){
	   document.forms[1].mode.value="viewClaimFraudHistroy";
	   document.forms[1].action="/ClaimGeneralAction.do";	
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}
}

function onPreauthFroudHistory(){
	if(!JS_SecondSubmit){
	   document.forms[1].mode.value="viewPreauthFraudHistroy";
	   document.forms[1].action="/ClaimGeneralAction.do";	
	   JS_SecondSubmit=true;
	   document.forms[1].submit();
	}
}

function dateValidation()
{	
	
	var claimReceiveDate =document.getElementById("receiveDate").value;    	
	var admStartDate=document.getElementById("admissionDate").value;
	var dischargeDate=document.getElementById("dischargeDate").value;
	
	if( !((document.getElementById("receiveDate").value)==="") && !((document.getElementById("admissionDate").value)===""))
	{	
		var clReceiveDate = claimReceiveDate.split("/");
		var clmReceiveDate=clReceiveDate[1]+"/"+clReceiveDate[0]+"/"+clReceiveDate[2];
		clmReceiveDate=new Date(clmReceiveDate);
		
		var admStartDate =admStartDate.split("/");
		var admStDate=admStartDate[1]+"/"+admStartDate[0]+"/"+admStartDate[2];
		admStDate=new Date(admStDate);
		
		var clmReceiveDt = new Date(clmReceiveDate);
		var admStDa =  new Date(admStDate);
		
		if(admStDa > clmReceiveDt)
		 {
			alert("Start Date should be less than or equal to Claim Received Date.");
			document.getElementById("admissionDate").value="";
			return false;
		 }
	}
	
	 if( !((document.getElementById("receiveDate").value)==="") && !((document.getElementById("dischargeDate").value)===""))
		{
			var clReceiveDate = claimReceiveDate.split("/");
			var clmReceiveDate=clReceiveDate[1]+"/"+clReceiveDate[0]+"/"+clReceiveDate[2];
			clmReceiveDate=new Date(clmReceiveDate);
			
			var dischargeDt =dischargeDate.split("/");
			var dischargeDate=dischargeDt[1]+"/"+dischargeDt[0]+"/"+dischargeDt[2];
			dischargeDate=new Date(dischargeDate);
			
			var clmReceiveDt = new Date(clmReceiveDate);
			var disdt =  new Date(dischargeDate);
			
			if(disdt > clmReceiveDt)
			 {
				alert("End Date should be less than or equal to Claim Received Date.");
				document.getElementById("dischargeDate").value="";
				return false;
			 }
		}
}



var  popupWindow=null;
function onSelectPreAuthNo(tp,lt){
	
		  
	var preAuthSeqId =	document.getElementById("authNum").value;
		
	if(preAuthSeqId==""||preAuthSeqId==null){
		    alert("Please select Authorization No.");
			return false;
	}
	
		  var openPage = "/PreAuthHistoryAction.do?mode=doSelectPreAuthNo";
		  var features = "scrollbars=1,status=1,toolbar=0,top=0,left="+lt+",resizable=1,menubar=0,width=1000,height=1000";
		  window.open(openPage,'',features);
		  /* popupWindow=  window.open(openPage,'',features);
		   popupWindow.focus(); 
			  document.onmousedown=focusPopup; 
			  document.onkeyup=focusPopup; 
			  document.onmousemove=focusPopup;*/
		 
	
}

function exceptionalHandling(){

	var claimStatus=document.getElementById("claimStatus").value;
	var message=confirm('Would you want to move this claim status to exceptional handling stage');
	if(message)
	{
		   document.forms[1].mode.value="getExceptionalClaimFlag";
		   document.forms[1].action = "/ClaimGeneralAction.do";    
		   document.forms[1].submit();	
		   JS_SecondSubmit=true;	  
	}
else{
return false;
} 
}
	


function onExceptionalHandling(){

	var referExceptionFalg=document.getElementById("referExceptionFalg").value;
//	var flag=document.forms[1].referExceptionFalg.value
	if(referExceptionFalg=="YES"){
		var message=confirm('Would you want to do the  exceptional handling for this claim');
		if(message)
		{
			   document.forms[1].mode.value="doExceptionalHandling";
			   document.forms[1].action = "/ClaimGeneralAction.do";    
			   document.forms[1].submit();	
			   JS_SecondSubmit=true;	  
		}
	else{
	return false;
	}	
	}
	else{
	alert("Please do the 'Refer Exceptional Handling' before doing Exceptional Handling");
	return false;
	
	} 
}

function onViewMdf(){

	var	memberSeqID	=	document.getElementById('memberSeqID').value;
	if(memberSeqID==''){
		alert("Please select a Member");
		document.getElementById('memberId').focus();
		return false;
	}else{
	var openPage = "/AddMdfMemberAction.do?mode=doViewMemberMdfFile&patOrClmFlag=YES&memberSeqID="+memberSeqID;
	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   	window.open(openPage,'',features);
	}
}