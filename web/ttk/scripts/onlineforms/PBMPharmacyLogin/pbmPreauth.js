/** @ (#) addenrollment.js 07th April 2017
 * Project     : Project X
 * File        : pbmPreauth.js
 * Author      : Nagababu K
 * Company     : RCS Technologies
 * Date Created: 07th April 2017
 *
 * @author 		 : Nagababu K
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */

function onCeed(){

		document.forms[1].mode.value="doCeedValidation";
		document.forms[1].action="/PbmPharmacyWebservicesAction.do";
		document.forms[1].submit();
}

function preauthProceed(){
		
	document.forms[1].mode.value ="preauthProceed";
	document.forms[1].action="/ttkAction.do";
	document.forms[1].submit();
}

/*
 * 

 icdCodeDesc
 searchBox
 icdSearchResults
 icdDivID
 
 */
	  
	  function onIcdDescSearch(searchMode) {
		  document.getElementById("icdCode").value="";
		  document.getElementById("icdCodeSeqID").value="";
		  
		  onSearchData(searchMode,'onIcdDescSearch','icdCodeDesc','icdDivID','/PbmAsynchronousSearch.do?mode=doIcdDetailsSearch&searchFieldMode=DESC','onSelectIcdDesc');
	  }
	  
	  function onIcdCodeSearch(searchMode) {
		  document.getElementById("icdCodeDesc").value="";
		  document.getElementById("icdCodeSeqID").value="";
		  onSearchData(searchMode,'onIcdCodeSearch','icdCode','icdCodeDivID','/PbmAsynchronousSearch.do?mode=doIcdDetailsSearch&searchFieldMode=CODE','onSelectIcdCode');
	  }
	  
	
  
  function onSelectIcdDesc(param1){ 	 
	  
	  var path="/PbmAsynchronousSearch.do?mode=getIcdDetails&icdMode=CODE&icdSeqID="+param1;
	 var varIcdCode= getSearchData(path);
	 document.getElementById("icdCodeSeqID").value=param1;
	 document.getElementById("icdCode").value=varIcdCode;
	    		         
	  }
  
  function onSelectIcdCode(param1){   
	  var path="/PbmAsynchronousSearch.do?mode=getIcdDetails&icdMode=DESC&icdSeqID="+param1;
		 var varIcdCodeDec= getSearchData(path);
		 document.getElementById("icdCodeSeqID").value=param1;
		 document.getElementById("icdCodeDesc").value=varIcdCodeDec; 
	    		         
	  }
  function onDrugDescSearch(searchMode) {
	  //document.getElementById("drugCodeSeqID").value="";
	  onSearchData(searchMode,'onDrugDescSearch','drugDesc','drugDivID','/PbmAsynchronousSearch.do?mode=doDrugDetailsSearch','onSelectDrugDesc');
  }
  
  function onSelectDrugDesc(param1,param2,param3,param4) {
	  document.getElementById("drugCodeSeqID").value=param1;
	  document.getElementById("granularUnit").value=param3;
	  document.getElementById("noOfUnits").value=param4;
	
	  var path="/PbmAsynchronousSearch.do?mode=getDrugDetails&searchMode=QAN&drugCodeSeqID="+param1;
	  var varDrugCodeQuantity= getSearchData(path);
	  document.getElementById("drugCodeQuantity").value=varDrugCodeQuantity;
  }
  
  

  function onClinicianSearch(searchMode) {	  
	  //document.getElementById("clinicianSeqID").value="";
	 // document.getElementById("clinicianName").value="";
	  onSearchData(searchMode,'onClinicianSearch','clinicianID','clinicianDivID','/PbmAsynchronousSearch.do?mode=doClinicianSearch&searchFieldMode=ID','onSelectClinicianID');
  }
  function onSelectClinicianID(param1) {
	  
	  var path="/PbmAsynchronousSearch.do?mode=getClinicianDetails&clinicianMode=NAME&clinicianSeqID="+param1;
		 var varClinicianName= getSearchData(path);
		 document.getElementById("clinicianSeqID").value=param1;	 
	    document.getElementById("clinicianName").value=varClinicianName;
	    
  }  
  
  function onClinicianNameSearch(searchMode) {
	  document.getElementById("clinicianSeqID").value="";
	  //document.getElementById("clinicianID").value="";
	  onSearchData(searchMode,'onClinicianNameSearch','clinicianName','clinicianDivName','/PbmAsynchronousSearch.do?mode=doClinicianSearch&searchFieldMode=NAME','onSelectClinicianName');
  }
 function onSelectClinicianName(param1) {	  
	  var path="/PbmAsynchronousSearch.do?mode=getClinicianDetails&clinicianMode=ID&clinicianSeqID="+param1;
		 var varClinicianID= getSearchData(path);
		 document.getElementById("clinicianSeqID").value=param1;	 
	    document.getElementById("clinicianID").value=varClinicianID;
	    
  }
  function onValidateGeneralDetails() {
    /* if(!JS_SecondSubmit){
    	 JS_SecondSubmit=true;*/
    	 
    	 if(document.forms[1].dateOfTreatment.value.length!=0){
 			if(isFutureDate(document.forms[1].dateOfTreatment.value)){
 				alert("Date Of Prescription cannot be future date ");
 				return true;
 			}
 		}
    	 
	  document.forms[1].mode.value="doValidateGeneralDetails";
	  document.forms[1].action="/PbmPharmacyGeneralUpdationAction.do";
	  document.forms[1].submit();
	  
     
  }
  function addDiagnosis() {
	     if(!JS_SecondSubmit){
	    	 JS_SecondSubmit=true;
	    	 document.forms[1].btnMode.value="ICD";
		  document.forms[1].mode.value="addIcdDetails";
		  document.forms[1].action="/PbmPharmacyGeneralUpdationAction.do";
		  document.forms[1].submit();
		  
	     }
	  }


  function addDrug() {
	     if(!JS_SecondSubmit){
	    		var noOfUnits=document.forms[1].noOfUnits.value;
	    		var granularUnit=document.forms[1].granularUnit.value;
	    		var quantity=document.forms[1].quantity.value;
	    		
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
	    									// document.forms[1].quantity.value="";
	    									document.forms[1].quantity.focus();
	    									return false;
	    								}
	    						}
	    					}
	    			
	    					if(granularUnit != "")
	    					{
	    						var unitType=document.forms[1].unitType.value;
	    						if(unitType == "PCKG")
	    						{
	    								res = parseFloat(quantity) * parseFloat(granularUnit);
	    								if(parseFloat(res) > parseFloat(noOfUnits))
	    								{
	    									var msg = confirm("Requested quantity is more than the usual prescribed.\n"+ 
	    													  "Do you want to continue submission" );
	    							     
	    										if(msg != true)
	    										{
	    											// document.forms[1].quantity.value="";
	    											document.forms[1].quantity.focus();
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
	    										// document.forms[1].quantity.value="";
	    										document.forms[1].quantity.focus();
	    										return false;
	    									}
	    								}	
	    							}
	    					}	
	    	}
	    	 
	      JS_SecondSubmit=true;
		  document.forms[1].btnMode.value="DRUG"; 
		  document.forms[1].mode.value="addDrugDetails";
		  document.forms[1].action="/PbmPharmacyGeneralUpdationAction.do";
		  document.forms[1].submit();
	 	  
	     }
	  }
  function onClearScreen() {
	 
	  var msg=confirm('Do you want to clear screen?');
	     if(msg){
	  if(!JS_SecondSubmit){
	    	 JS_SecondSubmit=true;
		  document.forms[1].mode.value="doClear";
		  document.forms[1].action="/PbmPharmacyGeneralUpdationAction.do";
		  document.forms[1].submit();
		  
	     }
	  }
  }
  function onRequstAuthorization() {
	 // confirm('Are you sure?');
	  var msg=confirm("Confirm pre-approval request submission.","Confirm Submission");
	     if(msg){
	  
	     if(!JS_SecondSubmit){
	    	 
	    	 JS_SecondSubmit=true;	    	
	    	 document.getElementById("processBtn").innerHTML ='Processing...';
	    	 document.getElementById("processBtn").disabled = true;
	    	document.forms[1].mode.value="doRequstAuthorization";
		  document.forms[1].action="/PbmPharmacyGeneralUpdationAction.do";
		  document.forms[1].submit();
		  
	     }
	     
	     }
	  }
  function getPreAuthData() {
	     if(!JS_SecondSubmit){
	    	 JS_SecondSubmit=true;
		  document.forms[1].mode.value="getPreAuthData";
		  document.forms[1].action="/PbmPharmacyGeneralAction.do";
		  document.forms[1].submit();
		  
	     }
	  }
  function deleteDiagnosisDetails(argIndexID,argDiagnosis) {
	  var message=confirm('Do you want delete '+argDiagnosis+' diagnosis details?');
	  if(message){
	     if(!JS_SecondSubmit){
	    	 JS_SecondSubmit=true;
		  document.forms[1].mode.value="deleteDiagnosisDetails";
		  document.forms[1].indexID.value=argIndexID;
		  document.forms[1].action="/PbmPharmacyGeneralAction.do";
		  document.forms[1].submit();
	     }
	  }
	     
	  }
  function deleteDrugDetails(argIndexID,argDrug) {
	  var message=confirm('Do you want delete '+argDrug+' drug details?');
	  if(message){
	     if(!JS_SecondSubmit){
	    	 JS_SecondSubmit=true;
		  document.forms[1].mode.value="deleteDrugDetails";
		  document.forms[1].indexID.value=argIndexID;
		  document.forms[1].action="/PbmPharmacyGeneralAction.do";
		  document.forms[1].submit();
		  
	     }
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

function onClose(){
	
	  document.forms[1].mode.value="doPreauthProccess";
	  document.forms[1].action="/PbmPharmacyGeneralAction.do";
	  document.forms[1].submit();
	  
  }
  
  
