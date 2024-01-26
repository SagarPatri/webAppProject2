//contains the javascript functions of addmemberdetails.jsp

//declare the Ids of the form fields seperated by comma which should not be focused when document is loaded
var JS_donotFocusIDs=["relationTypeID"];


function isNumeric(field) {
    var re = /^[0-9][0-9]*$/;
    if (!re.test(field.value)) {
       alert("Qatar ID should be a Numeric value");
       field.value="";
       field.focus();
		
		return false;
    }
    return true;
}

// validation for gross premium
function onSave()     
{    
	trimForm(document.forms[1]);
	if(!JS_SecondSubmit)
	{
		/*if(document.forms[1].hyperTensCoverYN.checked)
	    {
	    	document.forms[1].hyperTensCoverYN.value='Y';
	    }//end of if(document.forms[1].hyperTensCoverYN.checked)
	    else
	    {
	    	document.forms[1].hyperTensCoverYN.value='N';
	    }//end of else
		if(document.forms[1].diabetesCoverYN.checked)
	    {
	    	document.forms[1].diabetesCoverYN.value='Y';
	    }//end of if(document.forms[1].hyperTensCoverYN.checked)
	    else
	    {
	    	document.forms[1].diabetesCoverYN.value='N';
	    }//end of else
		*/	
		
/*if(document.forms[1].capitationflag.value == "Y")
		{
		 var re = /^[0-9]*\.*[0-9]*$/;
		 
		 var grossPremiumAmt=document.forms[1].grossPremium.value;
		 grossPremiumAmt=(grossPremiumAmt==null||grossPremiumAmt===""||!re.test(grossPremiumAmt)||grossPremiumAmt==="0")?0:grossPremiumAmt;
		 
		 var insurerMarginAmt=document.forms[1].insurerMargin.value;
			 insurerMarginAmt=(insurerMarginAmt==null||insurerMarginAmt===""||!re.test(insurerMarginAmt)||insurerMarginAmt==="0")?0:insurerMarginAmt;
			 
			 var brokerMarginAmt=document.forms[1].brokerMargin.value;
			 brokerMarginAmt=(brokerMarginAmt==null||brokerMarginAmt===""||!re.test(brokerMarginAmt)||brokerMarginAmt==="0")?0:brokerMarginAmt;
			 
			 var tapMarginAmt=document.forms[1].tapMargin.value;
			 tapMarginAmt=(tapMarginAmt==null||tapMarginAmt===""||!re.test(tapMarginAmt)||tapMarginAmt==="0")?0:tapMarginAmt;
			 
			 var reInsBrkMarginAmt=document.forms[1].reInsBrkMargin.value;
			 reInsBrkMarginAmt=(reInsBrkMarginAmt==null||reInsBrkMarginAmt===""||!re.test(reInsBrkMarginAmt)||reInsBrkMarginAmt==="0")?0:reInsBrkMarginAmt;
			 
			 var otherMarginAmt=document.forms[1].otherMargin.value;
			 otherMarginAmt=(otherMarginAmt==null||otherMarginAmt===""||!re.test(otherMarginAmt)||otherMarginAmt==="0")?0:otherMarginAmt;
			 
			 // for finding all margins  total
			 var totalMarginAmt = ((parseFloat(grossPremiumAmt)*parseFloat(otherMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(reInsBrkMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(tapMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(brokerMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(insurerMarginAmt))/100); 
			 
			 
			 totalMarginAmt=(totalMarginAmt==null||totalMarginAmt===""||!re.test(totalMarginAmt)||totalMarginAmt==="0")?0:totalMarginAmt;
			 
			 // for finding Net Premium 
			 var ipNetPremiumAmt = document.forms[1].ipNetPremium.value;
			 ipNetPremiumAmt=(ipNetPremiumAmt==null||ipNetPremiumAmt===""||!re.test(ipNetPremiumAmt)||ipNetPremiumAmt==="0")?0:ipNetPremiumAmt;
			 var totalMarginAmt1=Number(totalMarginAmt)+Number(ipNetPremiumAmt);
			 
			 if(!((Number(totalMarginAmt1) < Number(grossPremiumAmt)) || (Number(totalMarginAmt1) === Number(grossPremiumAmt)))){
				 alert("Gross Premium amount should be always greater than the sum of all deductables");
				// document.forms[1].ipNetPremium.focus();
				 //	 document.forms[1].ipNetPremium.value="";
				 return false;      
			 }
			}*/
		
		if(document.getElementById("stopPreauthDateId").style.display ==""){
			var stoppreauth = document.getElementById("stopPreauthId").value;
			if(stoppreauth ==""){
				 alert("Stop Cashless Date is Required");
				  return false;
			}
			
			 if(document.getElementById("stopcashlessmemberid").value != null){
					var stopcashlessmemberidflag =	document.getElementById("stopcashlessmemberid").value;
					
					var  currentDate =  new Date();
					var curmonth = currentDate.getMonth() + 1;
				    var curday = currentDate.getDate();
				    var curyear = currentDate.getFullYear();
				    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
					  
				      curnewdate = curnewdate.split("/");
				      stoppreauth = stoppreauth.split("/");
				      stopcashlessmemberidflag = stopcashlessmemberidflag.split("-");
					  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
					  stoppreauth = new Date(stoppreauth[2], stoppreauth[1] - 1, stoppreauth[0]).getTime();
					  stopcashlessmemberidflag = new Date(stopcashlessmemberidflag[0], stopcashlessmemberidflag[1] - 1, stopcashlessmemberidflag[2]).getTime();
					  if(stopcashlessmemberidflag == stoppreauth){
						  
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
			
			if(document.getElementById("stopclaimsmemberid").value != null){
			var stopclaimsmemberidflag =	document.getElementById("stopclaimsmemberid").value;
			
			var  currentDate =  new Date();
			var curmonth = currentDate.getMonth() + 1;
		    var curday = currentDate.getDate();
		    var curyear = currentDate.getFullYear();
		    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
			  
		      curnewdate = curnewdate.split("/");
		      stopclms = stopclms.split("/");
		      stopclaimsmemberidflag = stopclaimsmemberidflag.split("-");
			  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
			  stopclms = new Date(stopclms[2], stopclms[1] - 1, stopclms[0]).getTime();
			  stopclaimsmemberidflag = new Date(stopclaimsmemberidflag[0], stopclaimsmemberidflag[1] - 1, stopclaimsmemberidflag[2]).getTime();
			  if(stopclaimsmemberidflag == stopclms){
				  
			  }else if(!(stopclms > curnewdate)){
				  alert("Stop Claims Date must be future date");
				  return false;
			  }
			
			}  
			
		}
		
		if(document.getElementById("mailNotificationYN") != null){
	    	if(document.getElementById("mailNotificationYN").checked)
	    		document.getElementById("mailNotificationhiddenYN").value="Y";
	    	else
	    		document.getElementById("mailNotificationhiddenYN").value="N";
	    	}

		document.forms[1].mode.value="doSave";
		document.forms[1].action="/AddMemberDetailAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onSave()

function onCancel()
{
	if(!TrackChanges()) return false;
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doClose";
		document.forms[1].action="/EditMemberDetailAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onCancel

function onReset(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{	
			document.forms[1].mode.value="doReset";
			document.forms[1].action="/EditMemberDetailAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
		}//end of else
	 }
}//end of onReset()

function onSuspendedIcon()
{
	if(!TrackChanges()) return false;

	document.forms[1].mode.value="doSearch";
	document.forms[1].action="/SuspensionAction.do";
	document.forms[1].submit();
}// end of onSuspendedIcon()

function onDelete()
{
	var msg = confirm("Are you sure you want to delete the selected record ?");
	if(msg)
	{
		if(!JS_SecondSubmit)
		 {
			document.forms[1].mode.value="doDelete";
			document.forms[1].action="/EditMemberDetailAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		 }
	}
}// end of onDelete()

function onClarificationTypeIDChange()
{
	document.forms[1].exitDate.value="";
	if(document.forms[1].status.value=='POC')	//if policy status is made active
	{
		document.forms[1].exitDate.value=document.forms[1].effectiveDate.value;
	}
	else	//if policy status Active,with suspension or with extension
	{
		document.forms[1].exitDate.value=document.forms[1].policyEndDate.value;
	}
}//end of onClarificationTypeIDChange()


function onRelationshipChange()
{
	var relationID=document.forms[1].relationTypeID.value;
	var genderID=relationID.substring(relationID.indexOf("#")+1,relationID.length);
	relationID=relationID.substring(0,relationID.indexOf("#"));

	if(relationID=="NSF")
		{
		document.forms[1].name.value=document.forms[1].hiddenName.value;
	document.forms[1].secondName.value=document.forms[1].hiddensecondName.value;
	document.forms[1].familyName.value=document.forms[1].hiddenfamilyName.value;
		}
	else
		{
		document.forms[1].name.value="";
		}
	document.forms[1].relationID.value=genderID;
	document.forms[1].focusID.value="relationTypeID";
	document.forms[1].mode.value="doChangeRelationship";
	document.forms[1].action="/EditMemberDetailAction.do";
	document.forms[1].submit();
}//end of onClarificationTypeIDChange()

function onDateofBirth()
{
	var regexp = /^(\d{2}\/\d{2}\/\d{4})$/;
	document.forms[1].dateOfBirth.value = trim(document.forms[1].dateOfBirth.value);
	var ageObj = document.getElementById("ageid");
	if (regexp.test(document.forms[1].dateOfBirth.value)==true)
	{
		var age = calculatedAge(document.forms[1].dateOfBirth.value,document.forms[1].policyStartDate.value);
		if(!isNaN(age) && age>=0)
			ageObj.value = age;
		else
			ageObj.value = "0";
	}//end of if (regexp.test(document.forms[1].dateOfBirth.value)==true)
	if(document.forms[1].dateOfBirth.value.length>0)
		ageObj.readOnly=true;
	else
		ageObj.readOnly=false;
}//end of onDateofBirth()

//javascript function called to calculate the IBM members age on the date of birth entered
function onIBMDateofBirth()
{
	var policyStartDate = document.forms[1].policyDate.value;
       var dateofJoining = document.forms[1].DOJ.value;
       var dateofMarriage = document.forms[1].DOM.value;
	var dateofBirth     = document.forms[1].dateOfBirth.value;
	var DOBDOM = "";

	if(document.forms[1].relationTypeID.value=="YSP#OTH")//spouse
	{
		DOBDOM=dateofMarriage;
	}
	else if((document.forms[1].relationTypeID.value.match("NC"))||(document.forms[1].relationTypeID.value.match("ND"))||(document.forms[1].relationTypeID.value.match("NS")))
	{
		DOBDOM=dateofBirth;
	}
	var regExp = /(\d{1,2})\/(\d{1,2})\/(\d{2,4})/;
       var maxDate="";
	if(parseInt(policyStartDate.replace(regExp, "$3$2$1")) > parseInt(dateofJoining.replace(regExp, "$3$2$1")))
	{
	   maxDate = policyStartDate;
	}
	else
	{
	  maxDate = dateofJoining;
	}

	if(DOBDOM!="")
	{
		if(parseInt(maxDate.replace(regExp, "$3$2$1")) < parseInt(DOBDOM.replace(regExp, "$3$2$1")))
		{
	   	     maxDate = DOBDOM;
		}

	}
	day1 = maxDate.substring (0, maxDate.indexOf ("/"));
	month1 = maxDate.substring (maxDate.indexOf ("/")+1, maxDate.lastIndexOf ("/"));
	year1 = maxDate.substring (maxDate.lastIndexOf ("/")+1, maxDate.length);

	day2 = dateofBirth.substring (0, dateofBirth.indexOf ("/"));
	month2 = dateofBirth.substring (dateofBirth.indexOf ("/")+1, dateofBirth.lastIndexOf ("/"));
	year2 = dateofBirth.substring (dateofBirth.lastIndexOf ("/")+1, dateofBirth.length);

	date1 = year1+"/"+month1+"/"+day1;
	date2 = year2+"/"+month2+"/"+day2;

	firstDate = Date.parse(date1);
	secondDate= Date.parse(date2);

	msPerDay = 24 * 60 * 60 * 1000;
	dbd = Math.floor((firstDate.valueOf()-secondDate.valueOf())/ msPerDay) + 1;
    var calculatedAge=Math.floor(dbd/365.25);
    if(!isNaN(calculatedAge) && calculatedAge>=0)
	document.forms[1].age.value = calculatedAge;
    else
	document.forms[1].age.value = "0";
}

function onChangeCity()
{
    	document.forms[1].mode.value="ChangeCity";
    	document.forms[1].action="/EditMemberDetailAction.do";
    	document.forms[1].submit();
}//end of onChangeCity()


function calcCapNetPremium(upObj)
{
	
	var re = /^[0-9]*\.*[0-9]*$/;	
	var objValue=upObj.value;	
	
	 if(objValue.length>=1)
	 {
		if(!re.test(objValue)) 
		{
			alert(" Data Should be Numeric ");
			upObj.value="";
			upObj.focus();
		}
	 }
	 // for gross premium
	 var medicalPremiumAmt=document.forms[1].medicalPremium.value;
	 medicalPremiumAmt=(medicalPremiumAmt==null||medicalPremiumAmt===""||!re.test(medicalPremiumAmt)||medicalPremiumAmt==="0")?0:medicalPremiumAmt;
	 
	 var maternityPremiumAmt=document.forms[1].maternityPremium.value;
	 maternityPremiumAmt=(maternityPremiumAmt==null||maternityPremiumAmt===""||!re.test(maternityPremiumAmt)||maternityPremiumAmt==="0")?0:maternityPremiumAmt;
	 
	 
	 var opticalPremiumAmt=document.forms[1].opticalPremium.value;
	 opticalPremiumAmt=(opticalPremiumAmt==null||opticalPremiumAmt===""||!re.test(opticalPremiumAmt)||opticalPremiumAmt==="0")?0:opticalPremiumAmt;
	 
	 var dentalPremiumAmt=document.forms[1].dentalPremium.value;
	 dentalPremiumAmt=(dentalPremiumAmt==null||dentalPremiumAmt===""||!re.test(dentalPremiumAmt)||dentalPremiumAmt==="0")?0:dentalPremiumAmt;	 
	
	 var wellnessPremiumAmt=document.forms[1].wellnessPremium.value;
	 wellnessPremiumAmt=(wellnessPremiumAmt==null||wellnessPremiumAmt===""||!re.test(wellnessPremiumAmt)||wellnessPremiumAmt==="0")?0:wellnessPremiumAmt;
	 
	 
	 document.forms[1].grossPremium.value=parseFloat(medicalPremiumAmt)+parseFloat(maternityPremiumAmt)+parseFloat(opticalPremiumAmt)+parseFloat(dentalPremiumAmt)+parseFloat(wellnessPremiumAmt);
	 
	 var grossPremiumAmt=document.forms[1].grossPremium.value;
	 grossPremiumAmt=(grossPremiumAmt==null||grossPremiumAmt===""||!re.test(grossPremiumAmt)||grossPremiumAmt==="0")?0:grossPremiumAmt;
	 
	 // for all margins
	 var insurerMarginAmt=document.forms[1].insurerMargin.value;
	 insurerMarginAmt=(insurerMarginAmt==null||insurerMarginAmt===""||!re.test(insurerMarginAmt)||insurerMarginAmt==="0")?0:insurerMarginAmt;
	 
	 var brokerMarginAmt=document.forms[1].brokerMargin.value;
	 brokerMarginAmt=(brokerMarginAmt==null||brokerMarginAmt===""||!re.test(brokerMarginAmt)||brokerMarginAmt==="0")?0:brokerMarginAmt;
	 
	 var tapMarginAmt=document.forms[1].tapMargin.value;
	 tapMarginAmt=(tapMarginAmt==null||tapMarginAmt===""||!re.test(tapMarginAmt)||tapMarginAmt==="0")?0:tapMarginAmt;
	 
	 var reInsBrkMarginAmt=document.forms[1].reInsBrkMargin.value;
	 reInsBrkMarginAmt=(reInsBrkMarginAmt==null||reInsBrkMarginAmt===""||!re.test(reInsBrkMarginAmt)||reInsBrkMarginAmt==="0")?0:reInsBrkMarginAmt;
	 
	 var otherMarginAmt=document.forms[1].otherMargin.value;
	 otherMarginAmt=(otherMarginAmt==null||otherMarginAmt===""||!re.test(otherMarginAmt)||otherMarginAmt==="0")?0:otherMarginAmt;
	 
	 // for finding all margins  total
	 var totalMarginAmt = ((parseFloat(grossPremiumAmt)*parseFloat(otherMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(reInsBrkMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(tapMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(brokerMarginAmt))/100)+((parseFloat(grossPremiumAmt)*parseFloat(insurerMarginAmt))/100); 
	 
	 
	 totalMarginAmt=(totalMarginAmt==null||totalMarginAmt===""||!re.test(totalMarginAmt)||totalMarginAmt==="0")?0:totalMarginAmt;
	 
	 // for finding Net Premium 
	 var ipNetPremiumAmt = document.forms[1].ipNetPremium.value;    
	 ipNetPremiumAmt=(ipNetPremiumAmt==null||ipNetPremiumAmt===""||!re.test(ipNetPremiumAmt)||ipNetPremiumAmt==="0")?0:ipNetPremiumAmt;
	 
	 
	  var  totMar_IpNetPre = parseFloat(ipNetPremiumAmt)+parseFloat(totalMarginAmt);
	   document.forms[1].netPremium.value = parseFloat(grossPremiumAmt)-parseFloat(totMar_IpNetPre);
}

 function isNegative1()
 {
	 var re = /^[0-9]*\.*[0-9]*$/;
	 
	 var objValue=document.getElementById("minAge").value;
	
	 
	 if(objValue.length>=1)
	 {
		if(!re.test(objValue)) 
		{
			alert(" Age Should be Numeric ");
			document.getElementById("minAge").focus();
		}
	 }

	 if(objValue<0)
		 {
			if(!re.test(objValue)) 
			{
				  alert('Negative Values Not allowed');
				objValue.value="";
				document.getElementById("minAge").focus();
			}
		 }
}

 
 function isNegative2()
 {
	 var re = /^[0-9]*\.*[0-9]*$/;
	 var objValue=document.getElementById("maxAge").value;
	
	 if(objValue.length>=1)
	 {
		if(!re.test(objValue)) 
		{
			alert(" Age Should be Numeric ");
			document.getElementById("maxAge").focus();
		}
	 }
	 if(objValue<0)
	 {
		if(!re.test(objValue)) 
		{
			  alert('Negative Values Not allowed');
			objValue.value="";
			document.getElementById("maxAge").focus();
		}
	 }
 }
 
 function ageCompare(){
	
	 if( !((document.getElementById("minAge").value)==="") && !((document.getElementById("maxAge").value)===""))
	  {
		  if(Number(document.getElementById("minAge").value) > Number(document.getElementById("maxAge").value))
		  { 
		  alert("Max age always Greater Then Min age");
		  document.getElementById("maxAge").value="";  
		  document.getElementById("maxAge").focus();
		 }
		  }
	} 
 function numbervalidation(txt)
 {
	
	 if(txt==="Insurer Margin" && !(Number(document.forms[1].insurerMargin.value)< 100))
	 {
		 alert(txt +" should be less than 100");
		 document.forms[1].insurerMargin.value="";
		 document.forms[1].insurerMargin.focus();
	 }
	 if(txt==="Broker Margin" && !(Number(document.forms[1].brokerMargin.value)< 100))
	 {
		 alert(txt +" should be less than 100");
		 document.forms[1].brokerMargin.value="";
		 document.forms[1].brokerMargin.focus();
	 }
	 if(txt==="TPA Margin" && !(Number(document.forms[1].tapMargin.value)< 100))
	 {
		 alert(txt +" should be less than 100");
		 document.forms[1].tapMargin.value="";
		 document.forms[1].tapMargin.focus();
	 }
	 if(txt==="ReIns.Brk.Margin" && !(Number(document.forms[1].reInsBrkMargin.value)< 100))
	 {
		 alert(txt +" should be less than 100");
		 document.forms[1].reInsBrkMargin.value.value="";
		 document.forms[1].reInsBrkMargin.focus();  
	 }
	 if(txt==="Other Margin" && !(Number(document.forms[1].otherMargin.value)< 100))
	 {
		 alert(txt +" should be less than 100 ");
		 document.forms[1].otherMargin.value="";
		 document.forms[1].otherMargin.focus();
	 }  
	 
 }
 
 function convertToHDate(hijriId){
		var dd,mm,yyyy;
		var Gdate = toDate(document.getElementById("Dob").value,false);
		if(Gdate){
			var Hdate = new HijriDate();
			Hdate.setTime(Gdate.getTime());
			dd = Hdate.getDate();
			mm = Hdate.getHMonth()+1;
			yyyy = Hdate.getHFullYear();
			if(dd.toString().length == 1)
				dd = "0"+dd;
			if(mm.toString().length == 1)
				mm = "0"+mm;
			var convertedDate = dd+"/"+mm+"/"+yyyy;
			document.getElementById(hijriId).value = convertedDate;
		}
		else
			document.getElementById(hijriId).value="";
	}

	function convertToGDate(GId){
		var dd,mm,yyyy;
		var Hdate = toDate(document.getElementById("HDob").value,true);
		
		if(Hdate){
			var Gdate = new Date();
			Gdate.setTime(Hdate.getTime());
			//Gdate.setDate(Gdate.getDate()+1);
			dd = Gdate.getDate();
			mm = Gdate.getMonth()+1;
			yyyy = Gdate.getFullYear();
			if(dd.toString().length == 1)
				dd = "0"+dd;
			if(mm.toString().length == 1)
				mm = "0"+mm;
			var convertedDate = dd+"/"+mm+"/"+yyyy;
			document.getElementById(GId).value = convertedDate;
		}
		else
			document.getElementById(GId).value="";
	}

	function toDate(dateStr,isHijri) {
	    var sDate  = dateStr.split("/");
	    if(dateStr){
	    	if(isHijri){
	    		var hijriDate = new HijriDate();
	    		hijriDate.setDate(sDate[0]);
	    		hijriDate.setMonth(sDate[1]-1);
	    		hijriDate.setFullYear(sDate[2]);
	    		
	    		if(sDate[0]==="08"||sDate[0]==="09"){
	    			if(sDate[0]==="08")
	    				hijriDate.setDate(hijriDate.getDate() - 21);
	    			else
	    				hijriDate.setDate(hijriDate.getDate() - 20);
	    		}
	    	return hijriDate;
	      }
	    	else
	    	if(!(dateStr === ""))
	    		return new Date(sDate[2] ,sDate[1]-1, sDate[0]);
	    }
	}
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


function mailNotification()
	{
		   if(document.getElementById("mailNotificationhiddenYN")){
			   if(document.getElementById("mailNotificationhiddenYN").value=='Y')
			   document.getElementById("mailNotificationYN").checked=true;
			   else
					document.getElementById("mailNotificationYN").checked=false;
		   }		   
		
	}
