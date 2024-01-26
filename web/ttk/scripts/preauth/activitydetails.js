
function getActivityCodeDetails(obj){
	var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	
	var varActivityCode=document.forms[1].activityCode.value;
	var varActivityStartDate=document.forms[1].activityStartDate.value;
	if("ACT"==obj){
		if(varActivityCode==null||varActivityCode===""){
			alert("Enter Activity Code");
			return false;
		}
	}else if("INT"==obj){
		if(document.forms[1].internalCode.value == null||document.forms[1].internalCode.value === ""){
			alert("Enter Internal Code");
			return false;
		}
	}else  if (varActivityStartDate == null || varActivityStartDate === "" || !pattern.test(varActivityStartDate)){
	        alert("Date format should be (dd/mm/yyyy)");
	        return false;
	    }
	
    //if(!confirm("You Want Validate ActivityCode !"))return;
	document.forms[1].mode.value = "selectActivityCode";
    document.forms[1].action ="/PreActivityDetailsAction.do?codeFlag=remember&searchType="+obj;
    document.forms[1].submit();
}//getActivityCodeDetails
function closeActivities() {
	if(!JS_SecondSubmit)
	 {  
		document.forms[1].mode.value = "doGeneral";
		document.forms[1].reforward.value = "close";
		document.forms[1].action ="/ActivityDetailsAction.do?focusObj=medicalOpinionRemarks";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}

function resetActivityDetails() {
	if(!JS_SecondSubmit)
	 { 
		document.forms[1].mode.value = "resetActivityDetails";
		document.forms[1].action ="/ActivityDetailsAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}

function saveActivityDetails() { 
	var noOfUnits=document.forms[1].noOfUnits.value;
	var granularUnit=document.forms[1].granularUnit.value;
	var quantity=document.forms[1].quantity.value;
	
	if(noOfUnits != "" && quantity != "")
	{	
				if(granularUnit == "")
				{
					if(parseFloat(quantity) > parseFloat(noOfUnits))
					{
						var msg = confirm("claimed quantity for drugs is higher than indicated \n"+ 
				           "                            to be reviewed.");
				     
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
								var msg = confirm("claimed quantity for drugs is higher than indicated \n"+ 
						           "                            to be reviewed.");
						     
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
								var msg = confirm("claimed quantity for drugs is higher than indicated \n"+ 
						           "                            to be reviewed.");
					     
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
		
		var selObj=document.forms[1].denialCode;
		if(selObj.value!==null&&selObj.value!==""&&selObj.value.length>1){		
			document.forms[1].denialDescription.value = selObj.options[selObj.selectedIndex].text;
		}
		if(!JS_SecondSubmit)
		 { 
			document.forms[1].mode.value = "saveActivityDetails";
			document.forms[1].action ="/SaveActivityDetailsAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
	 	}//end of if(!JS_SecondSubmit)
}

function calculateNetAmount(upObj){
	
	var re = /^[0-9]*\.*[0-9]*$/;	
	var objValue=upObj.value;
	var unitPrice=document.forms[1].unitPrice.value;
	var activityTypeId=document.forms[1].activityTypeId.value;
	var activityCodeSeqId=document.forms[1].activityCodeSeqId.value;
	var copayPerc	=	document.forms[1].copayPerc.value;
	var copayDeductFlag	=	document.forms[1].copayDeductFlag.value;
	var internalCode = document.forms[1].internalCode.value;
	var discountFlag = document.getElementById("discountFlag").value;
	if(upObj.id==="UnitType"){
		if(activityCodeSeqId.length<1||activityTypeId!=5)return;
		var activityCode=document.forms[1].activityCode.value;
		var activityStartDate=document.forms[1].activityStartDate.value;
		var unitType=document.forms[1].unitType.value;
		var providerSeqID=document.forms[1].providerSeqID.value;
		$(document).ready(function () {
		$.ajax({
            url :"/asynchronAction.do?mode=getUnitTypePrice&activityCode="+activityCode+"&activityStartDate="+activityStartDate+"&unitType="+unitType+"&providerSeqID="+providerSeqID+"&internalCode="+internalCode,
            dataType:"text",
			type:"POST",
			async:false,
		    context:document.body,
            success : function(responseData) {
            	var rsData=responseData;
            	var rsDatas=rsData.split("@");            	
            	document.forms[1].unitPrice.value=parseFloat(rsDatas[0]).toFixed(2);
            	document.forms[1].discountPerUnit.value=parseFloat(rsDatas[1]).toFixed(2);
            	unitPrice=document.forms[1].unitPrice.value;
              }//success data
		 });//$.ajax(	
		});
	}else if(objValue.length>=1){
		if(!re.test(objValue)){
			alert("Please Enter Valid Data");
			upObj.value="";
			upObj.focus();
		}
	}  
	//document.forms[1].approvedAmount.value="";
	//document.forms[1].allowedAmount.value="";
	
	 var copayAmt=document.forms[1].copay.value;
	var coinsuranceAmt=document.forms[1].coinsurance.value;
	var deductibleAmt=document.forms[1].deductible.value;
	var outOfPocketAmt=document.forms[1].outOfPocket.value;
	var quantity=document.forms[1].quantity.value;
	
	document.getElementById("approvedQuantity").value=quantity;
	
	quantity=(quantity==null||quantity===""||!re.test(quantity)||quantity==="1")?1:quantity;
		
	unitPrice=(unitPrice==null||unitPrice===""||!re.test(unitPrice))?0:unitPrice;
	
	var conversionRate  = document.getElementById("conversionRate").value;
	conversionRate=(conversionRate==null||conversionRate===""||!re.test(conversionRate)||conversionRate==="1")?1:conversionRate;
	
	unitPrice = (Math.round(((unitPrice*conversionRate)+ 0.00001)*100)/100).toFixed(2);

	
	document.forms[1].convertedUnitPrice.value=unitPrice;
	
	document.forms[1].grossAmount.value=parseFloat(parseFloat(unitPrice)*parseFloat(quantity)).toFixed(2);
	
	 var varTariffYN=document.forms[1].tariffYN.value;
	 var overrideYN=document.getElementById("overrideYN");
	 
	// if(varTariffYN==="Y"&&overrideYN===null){
	 if(varTariffYN==="Y"){
		 var discountAmt=document.forms[1].discountPerUnit.value;
			discountAmt=(discountAmt==null||discountAmt===""||!re.test(discountAmt))?0:discountAmt;
			document.forms[1].discount.value=parseFloat(parseFloat(discountAmt)*parseFloat(quantity)).toFixed(2);
		 }else{
			 var discountAmt=document.forms[1].discount.value;
				discountAmt=(discountAmt==null||discountAmt===""||!re.test(discountAmt))?0:discountAmt;
				document.forms[1].discount.value=parseFloat(discountAmt).toFixed(2);
			 }	
	
	if(parseFloat(document.forms[1].discount.value)>parseFloat(document.forms[1].grossAmount.value)){		
		alert("Discount Amount Should Not Greater Than Gross Amount");
		document.forms[1].copay.value='0';
		document.forms[1].coinsurance.value='0';
		document.forms[1].deductible.value='0';
		document.forms[1].outOfPocket.value='0';
		document.forms[1].patientShare.value='0';
		document.forms[1].discount.value='0';
		document.forms[1].discountedGross.value='0';
		document.forms[1].netAmount.value='0';
		document.forms[1].grossAmount.value='0';  
		//document.forms[1].allowedAmount.value=''; 
		//document.forms[1].approvedAmount.value='';
		return ;
		}
	if(discountFlag =='PATCLM')
	{	
		document.forms[1].discountedGross.value=parseFloat(parseFloat(document.forms[1].grossAmount.value)-parseFloat(document.forms[1].discount.value)).toFixed(2);
	}
	else
	{
		document.forms[1].discountedGross.value=parseFloat(parseFloat(document.forms[1].grossAmount.value)).toFixed(2);
	}
	copayAmt=(copayAmt==null||copayAmt===""||!re.test(copayAmt))?0:copayAmt;
	coinsuranceAmt=(coinsuranceAmt==null||coinsuranceAmt===""||!re.test(coinsuranceAmt))?0:coinsuranceAmt;
	deductibleAmt=(deductibleAmt==null||deductibleAmt===""||!re.test(deductibleAmt))?0:deductibleAmt;
	outOfPocketAmt=(outOfPocketAmt==null||outOfPocketAmt===""||!re.test(outOfPocketAmt))?0:outOfPocketAmt;
	
	var patientShareAmt=parseFloat(copayAmt)+parseFloat(coinsuranceAmt)+parseFloat(deductibleAmt)/*+parseFloat(outOfPocketAmt)*/;
	var res	=	0;
	var rdeductible	=	document.forms[1].rdeductible.value;
	var rcopay		=	document.forms[1].rcopay.value;
	if(rcopay=='' || rcopay=='NAN')
		rcopay	=	0;
	if(rdeductible=='' || rdeductible=='NAN')
		rdeductible	=	0;
	if(copayDeductFlag=='BOTH')
	{
		res	=	(parseFloat(document.forms[1].discountedGross.value)-parseFloat(rdeductible)*(copayPerc/100))+parseFloat(deductible);
	}
	if(copayDeductFlag=='MIN')
	{
		res	=	Math.min(parseFloat(rcopay),parseFloat(rdeductible));
	}
	if(copayDeductFlag=='MAX')
	{
		res	=	Math.max(parseFloat(rcopay),parseFloat(rdeductible));
	}
	patientShareAmt	=	parseFloat(patientShareAmt)+parseFloat(res);
	var discountedGrossAmt=document.forms[1].discountedGross.value;//=parseFloat(grossAmt)-parseFloat(discountAmt);
	if(parseFloat(patientShareAmt)>parseFloat(discountedGrossAmt)){
		 alert("PatientShare Amount Should Not Greater Than DiscountedGross Amount");
		document.forms[1].copay.value='0';
		document.forms[1].coinsurance.value='0';
		document.forms[1].deductible.value='0';
		document.forms[1].outOfPocket.value='0';
		document.forms[1].patientShare.value='0';
		document.forms[1].discount.value='0';
		document.forms[1].discountedGross.value='0';
		document.forms[1].netAmount.value='0';
		document.forms[1].grossAmount.value='0';  
		//document.forms[1].allowedAmount.value=''; 
		//document.forms[1].approvedAmount.value='';    
		return;
		}else{
			document.forms[1].patientShare.value=parseFloat(patientShareAmt).toFixed(2);
			document.forms[1].netAmount.value=parseFloat(parseFloat(discountedGrossAmt)-parseFloat(patientShareAmt)-parseFloat(outOfPocketAmt)).toFixed(2);
			}		
	
	if((parseFloat(patientShareAmt)+parseFloat(outOfPocketAmt))>parseFloat(discountedGrossAmt)){
		 alert("Dis Allowed Amount Should Not Greater Than DiscountedGross Amount");
		 document.forms[1].outOfPocket.value='0';
		 document.forms[1].netAmount.value='0';
		}
}
function setApprovedQuantity(){	
	document.getElementById("approvedQuantity").value=document.getElementById("quantity").value;
}

function selectClinician(){
				  document.forms[1].mode.value="doGeneral";
				  document.forms[1].reforward.value="activityClinicianSearch";
				  document.forms[1].action="/ActivityDetailsAction.do";	
				   document.forms[1].submit();	
	}
function selectActivityCode() {
    document.forms[1].mode.value = "doGeneral";
    document.forms[1].reforward.value="activitySearchList";
    document.forms[1].action ="/PreActivityDetailsAction.do";
    document.forms[1].submit();
}
function addDenialDesc() {
	var selObj=document.forms[1].denialCode;
	 var denialCode=selObj.value;
	 if(denialCode===null||denialCode===""||denialCode==""||denialCode.length<1){
		 alert("Please Select Denial Description");
		 return;
	 }	
	document.forms[1].denialDescription.value = selObj.options[selObj.selectedIndex].text;
    document.forms[1].mode.value = "addDenialDesc";
    document.forms[1].action ="/ActivityDetailsAction.do";
    document.forms[1].submit();
}
function deleteDenialDesc(keyVal) {
	document.forms[1].denialCode.value = keyVal;
    document.forms[1].mode.value = "deleteDenialDesc";
    document.forms[1].action ="/ActivityDetailsAction.do?denialCode="+keyVal;
    document.forms[1].submit();
}
function onChangeServiceType() {   
	document.forms[1].mode.value = "doChangeServiceType";
    document.forms[1].action ="/ActivityDetailsAction.do";
    document.forms[1].submit();
}

function onServiceCode(obj){
	var selIndex 	= obj.selectedIndex;
	var selText 	= obj.options[parseInt(selIndex)].text;
	document.getElementById("internalCode").value			=	selText.substring(selText.lastIndexOf("-")+2,selText.length);
}

function addOverrideRemarks() {
	var selObj=document.forms[1].overrideRemarks;
	var selObj2=document.forms[1].overrideSubRemarks;
	 var overrideRemarks=selObj.value;
	 var overrideSubRemarks=selObj2.value;
	 if(overrideRemarks===null||overrideRemarks===""||overrideRemarks==""||overrideRemarks.length<1)
	 {
		 alert("Please Select Override Remarks.");
		 document.forms[1].overrideRemarks.focus();
		 return;
	 }
	 if(overrideSubRemarks===null||overrideSubRemarks===""||overrideSubRemarks==""||overrideSubRemarks.length<1)
	 {
		 alert("Please Select Override Sub Remarks.");
		 document.forms[1].overrideSubRemarks.focus();
		 return;
	 }
	 	document.forms[1].overrideSubRemarksDesc.value = selObj2.options[selObj2.selectedIndex].text;
	    document.forms[1].mode.value = "addOverrideRemarks";
	    document.forms[1].action ="/ActivityDetailsAction.do";
	    document.forms[1].submit();
}	 

function delOverirdeRemarksDesc(keyVal) {
//	document.forms[1].overrideRemarks.value = keyVal;
    document.forms[1].mode.value = "deleteOverrideRemarks";
    document.forms[1].action ="/ActivityDetailsAction.do?keyVal="+keyVal;
    document.forms[1].submit();
}

function spaceValidation()
{
	var otherRemarks = document.getElementById("otherRemarks").value;
	 var space = otherRemarks.charCodeAt(0);
     if(space==32)
     {
            alert("Other Remarks should not start with space.");
            document.getElementById("otherRemarks").value="";
            document.getElementById("otherRemarks").focus();
            return;
     } 
}

function displayOverrideRemarksSub()
{
	document.forms[1].mode.value="displayOverrideRemarksSub";
	document.forms[1].action="/ActivityDetailsAction.do";
	document.forms[1].submit();
}

//cr-650 by rahuul kumar
function onActivityDescSearch(searchMode) {
	 
	 var type=document.forms[1].activityType.value;

	 onSearchData(searchMode,'onActivityDescSearch','activityCodeDesc','activityDescDivID','/ProviderAsynchronousSearch.do?mode=doActivitySearchClaims&icdMode=CODE&type='+type,'onSelectActivityCode');

}

var sdPageForwardNum="1";
var sdPageLinkNum="1";
var sdXhttp;
var varMainDivID;
var dataList=[];
function onSearchData(searchMode,searchMethod,fieldID,dispID,urlAction,selectMethod) {
//clear last search data
if(varMainDivID!=null&&varMainDivID!=""){
	  var lastSpcDiv=document.getElementById(varMainDivID);    
	  lastSpcDiv.innerHTML="";
	  lastSpcDiv.style.display="none";
}

var spcDiv;
varMainDivID=dispID;
spcDiv=document.getElementById(dispID);    
spcDiv.innerHTML="";
spcDiv.style.display="none";

if(searchMode==undefined){
searchMode="new";        
	  sdPageLinkNum="1";
	  sdPageForwardNum="1";
	  spcDiv.innerHTML="";
spcDiv.style.display="none";
}else if(searchMode==="new"){
    searchMode="new";        
	  sdPageLinkNum="1";    	 
} else{
    searchMode="old";         
}
  
 var fieldValue= document.forms[1].activityCodeDesc.value;
//   var dateOfTreatment= document.forms[1].dateOfTreatment.value;
	if(fieldValue!==null&&fieldValue!==""){				
	var path;
	path=urlAction+"&paramName="+fieldID+"&sdPageLinkNum="+sdPageLinkNum+"&"+fieldID+"="+fieldValue+"&sdPageForwardNum="+sdPageForwardNum+"&searchMode="+searchMode;
	
var varSearchData=getSearchData(path);


if(varSearchData!==null&&varSearchData!==""&&varSearchData.length>1){

	  spcDiv.style.display="";

	  var arrSearchData,varSplitData,noOfrows=0;
	  
 
  	arrSearchData =varSearchData.split("@");
  	 noOfrows=arrSearchData[0];
  	 varSplitData=arrSearchData[1];
 
	  
	  if(varSplitData.length>0) {

		  
    var arrData=varSplitData.split("|");
    
    if(arrData.length>0){
	      
        var sdmDiv;

      	  sdmDiv = document.createElement('div');
	          sdmDiv.id = "sdmTextDiv";
	          sdmDiv.className = "sdmTextDiv";
	          if(noOfrows<5){
	          sdmDiv.style.height = "100px";
	          }
        
        for(var i=0;i<arrData.length-1;i++){
      	  
        var sdsDiv = document.createElement('div');
        sdsDiv.className = 'sdTextDiv';
        sdsDiv.id='sdsDiv'+i;
		  var idData=arrData[i].split("#");
		
		  //  var idDataToSplit = idData[1].split("--");
		  dataList[i]=idData;
		 sdsDiv.innerHTML = idData[0];
         var funNameWithParametres;
         var cl= "."+sdsDiv.className;
         funNameWithParametres="onSelectData('"+dispID+"','"+fieldID+"','"+selectMethod+"','"+idData[0]+"','"+idData[1]+"');";
               	   // funNameWithParametres="SelectData();";  		
        //added by rahul kumar
         //sdsDiv.id="sde";
         //sdsDiv.addEventListener("click",hello);
     //   sdsDiv.onclick = function() { onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1],i,funNameWithParametres); };
       
         sdsDiv.setAttribute("onclick",funNameWithParametres);      
        //document.getElementById("sde").setAttribute("onclick","hello()");
         sdmDiv.appendChild(sdsDiv);
         
        }
        //for(var i=0;i<arrData.length-1;i++){
              spcDiv.appendChild(sdmDiv);
       /*
          $(document).ready( function() {
		  var idData=arrData[0].split("#");

       	  $("#sdsDiv0").click( function() {
       	    // this will fire when you click view
       	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
       	  });
       });

          $(document).ready( function() {
    		  var idData=arrData[1].split("#");

           	  $("#sdsDiv1").click( function() {
           	    // this will fire when you click view
           	  console.log("hello");
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });
          $(document).ready( function() {
    		  var idData=arrData[2].split("#");

           	  $("#sdsDiv2").click( function() {
           	    // this will fire when you click view
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });
          $(document).ready( function() {
    		  var idData=arrData[3].split("#");

           	  $("#sdsDiv3").click( function() {
           	    // this will fire when you click view
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });

          $(document).ready( function() {
    		  var idData=arrData[4].split("#");

           	  $("#sdsDiv4").click( function() {
           	    // this will fire when you click view
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });

          $(document).ready( function() {
    		  var idData=arrData[5].split("#");

           	  $("#sdsDiv5").click( function() {
           	    // this will fire when you click view
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });


          $(document).ready( function() {
    		  var idData=arrData[6].split("#");

           	  $("#sdsDiv6").click( function() {
           	    // this will fire when you click view
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });


          $(document).ready( function() {
    		  var idData=arrData[7].split("#");

           	  $("#sdsDiv7").click( function() {
           	    // this will fire when you click view
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });


          $(document).ready( function() {
    		  var idData=arrData[8].split("#");

           	  $("#sdsDiv8").click( function() {
           	    // this will fire when you click view
           	onSelectData(dispID,fieldID,selectMethod,idData[0],idData[1]);
           	  });
           });
*/
          
         //calling page link function
        createPageLiks(searchMethod,noOfrows,dispID);
        
      }// if(arrData.length>0){
     }//if(arrSearchData.length>0) {
    else{
  	  document.getElementById(dispID).style.display="none";
      }
     }//if(sData!==null&&sData!==""&&sData.length>1){
   else {
	  document.getElementById(dispID).style.display="none";
    }
}//if(sObj.value!==null&&sObj.value!==""){ 
}//function onInsSearch(sObj) {

function getSearchData(path){
if(sdXhttp==null){
if (window.XMLHttpRequest) {
	    sdXhttp = new XMLHttpRequest();
	    } else {
	    // code for IE6, IE5
	    sdXhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}



sdXhttp.open("POST", path, false);
sdXhttp.send();
var varSearchData=sdXhttp.responseText;
return varSearchData;
}

function createPageLiks(searchMethod,noOfrows,dispID) {

	  var spcDiv;
	  		spcDiv=document.getElementById(dispID);
    
	//creating page links
	             var sdlmDiv,sdlsDiv,forwardDiv,backwardDiv;
	             sdlmDiv = document.createElement('div');
	             sdlmDiv.className = 'sdPageLinks';           
	        	var tempNoOfrows=noOfrows;


	        	if(sdPageForwardNum!=null&&sdPageForwardNum!=""&&parseInt(sdPageForwardNum)>1){
	        		 
	        		  backwardDiv = document.createElement('div');
	        		  backwardDiv.className = 'sdPageBackward';
	        		  backwardDiv.setAttribute("onclick","sdPageBackward('"+searchMethod+"');");
	        		  backwardDiv.innerText="<";
	        		  sdlmDiv.appendChild(backwardDiv);
		        	  }
	        	
	        	var linkLabel;
	        	if(sdPageForwardNum==null||sdPageForwardNum==""||sdPageForwardNum==="1")linkLabel=0;
	        	else linkLabel=(parseInt(sdPageForwardNum)*parseInt(10)-10);
	        	
	        	if(noOfrows>100)noOfrows=noOfrows-1;
	        	
	        	  for(var i=1;0<noOfrows;i++){
	            	  
	        		sdlsDiv = document.createElement('div');
	        		
	        		sdlsDiv.innerText=(parseInt(i)+parseInt(linkLabel));
	        		sdlsDiv.id = 'sdPageLink';
	        		if(sdPageLinkNum==i){
	        			sdlsDiv.className='sdCurrentPageLink';
		        		}else{
	        		sdlsDiv.setAttribute("onclick","sdPageLink('"+i+"','"+searchMethod+"');");
	        		sdlsDiv.className = 'sdPageLink';
		        	}
	        		
	        		sdlmDiv.appendChild(sdlsDiv);
	        		
	        		noOfrows=noOfrows-10;
	        	  }
              if(tempNoOfrows>100){
	        		  
	        		  forwardDiv = document.createElement('div');
	        		  forwardDiv.className = 'sdPageForward';
	        		  forwardDiv.setAttribute("onclick","sdPageForward('"+searchMethod+"');");
	        		  forwardDiv.innerText=">";
	        		  sdlmDiv.appendChild(forwardDiv);
		        	}
	        	  
	        	  var sdCloseDiv = document.createElement('span');
	        	  sdCloseDiv.className = 'sdCloseBtn';
	        	  sdCloseDiv.setAttribute("onclick","sdCloseSearch('"+dispID+"');");
	        	  sdCloseDiv.innerText="Close";
	        	  sdlmDiv.appendChild(sdCloseDiv);
	        	  
	        	  spcDiv.appendChild(sdlmDiv);
	        	  
}
function onSelectData(sdDivID,searchFieldID,funName,param1,param2,param3,param4) {
	
	var idDataToSplit = param2.split("--");
document.getElementById('activityCode').value=idDataToSplit[0];
	  document.getElementById(searchFieldID).value=param1; 
	  
	  document.getElementById("sdmTextDiv").style.display="none";
	  getActivityCodeDetails('ACT');
      
	//  var regex = /^[a-zA-Z\d\(),-\s]+$/;
	 
	 // var test1 = regex.test(param2);

}	 
//$('div.sdTextDiv').click(function(){
  // SelectData();
//});

function sdPageLink(argPageLink,argSearchMethod) {
	  sdPageLinkNum=argPageLink; 	
	  if(argSearchMethod!=null&&argSearchMethod!=""&&argSearchMethod.length>1){
	  window[argSearchMethod]('old');
	  }

}


	 
function sdPageForward(argSearchMethod) {

	     if(sdPageForwardNum==null||sdPageForwardNum==""||sdPageForwardNum=="1")sdPageForwardNum="1";
	  sdPageForwardNum=parseInt(sdPageForwardNum)+parseInt("1");		
	  if(argSearchMethod!=null&&argSearchMethod!=""&&argSearchMethod.length>1){
	  window[argSearchMethod]('new');
	  }

}

function sdPageBackward(argSearchMethod) {

	     if(sdPageForwardNum==null||sdPageForwardNum==""||sdPageForwardNum=="1")sdPageForwardNum="1";
	  sdPageForwardNum=parseInt(sdPageForwardNum)-parseInt("1");		
	  if(argSearchMethod!=null&&argSearchMethod!=""&&argSearchMethod.length>1){
	  window[argSearchMethod]('new');
	  }

}
function sdCloseSearch(sdDivID) {
	  document.getElementById(sdDivID).innerHTML=""; 
	  document.getElementById(sdDivID).style.display="none";	 
}



function clearSearchData(event)
{
	  var char = event.which || event.keyCode;
	  if(char==27){
	  //clear last search data
	  if(varMainDivID!=null&&varMainDivID!=""){
		  var lastSpcDiv=document.getElementById(varMainDivID);    
		  lastSpcDiv.innerHTML="";
		  lastSpcDiv.style.display="none";
	  }
	  }
}
function validateProviderQty(upObj){
	
	var re = /^[0-9]*\.{0,1}[0-9]*$/;	//------/^[0-9]*\.*[0-9]*$/
	var objValue=upObj.value;
	if(!re.test(objValue)){
		alert("Please Enter Valid Data");
		upObj.value="";
		
		upObj.focus();
	}
}
