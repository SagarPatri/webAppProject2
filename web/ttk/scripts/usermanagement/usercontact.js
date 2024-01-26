//java script for the add contact screen
// Function to reset the password.
function onResetPassword()
{
	if(!JS_SecondSubmit)
    {
		var message=confirm('Do you want to reset the password');
		if(message)
		{
			document.forms[1].mode.value="doResetPassword";
			document.forms[1].action="/UpdateContact.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit) 	
	}//end of if(message)
}//end of onResetPassword()

// Function to show/hide fields specific to certain contact types.
function showhideContactInfo(selObj)
{
	var selVal = selObj.options[selObj.selectedIndex].value;
	document.getElementById("6").style.display="none";
	if(document.getElementById(selVal))
		document.getElementById(selVal).style.display="";
}//end of showhideContactInfo(selObj)

//Function to uncheck the provider user access when
function activeCheck()
{
	if(document.forms[1].userType.value=='HOS'||document.forms[1].userType.value=='INS'||document.forms[1].userType.value=='COR')
	{
		if(!document.getElementById('activeYNFlag').checked)
		{
			document.getElementById('accessYNFlag').checked=false;
			document.getElementById('hidAccessYNFlag').value="";
			document.forms[1].elements['userAccessVO.roleID'].disabled =true;
		    document.forms[1].elements['userAccessVO.roleID'].className = "selectBoxDisabled";
		}//end of if(!document.forms[1].elements['activeYN'].checked)
	}//end of if
}//end of activeCheck()
//Function to uncheck the provider user access when
function softcopyAccess()
{
		if(!document.getElementById('softcopyAccessYNFlag').checked)
		{
			document.getElementById('softcopyAccessYNFlag').checked=false;			
		}//end of if(!document.forms[1].elements['activeYN'].checked)	
}//end of softcopyAccess()

//Function to uncheck the provider user access when
function softcopyBranch()
{
		if(!document.getElementById('softcopyOtBranchFlag').checked)
		{
			document.getElementById('softcopyOtBranchFlag').checked=false;			
		}//end of if(!document.forms[1].elements['activeYN'].checked)	
}//end of softcopyBranch()


function Reset()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doReset";
		document.forms[1].action="/EditUserContact.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of Reset()

function Close()
{
	if(!JS_SecondSubmit)
	 {
		if(!TrackChanges()) return false;
		//document.forms[1].mode.value="doCloseContact";
		document.forms[1].mode.value="doDefault";
		document.forms[1].child.value="";
		//document.forms[1].action="/EditUserContact.do";
		document.forms[1].action="/UserListSearchAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of Close()

function SetState(obj_checkbox)
{
	if(obj_checkbox.checked)
    {
		   document.forms[1].elements['userAccessVO.roleID'].disabled = false;
		   document.forms[1].elements['userAccessVO.roleID'].className = "selectBox";
 		   document.getElementById('activeYNFlag').checked=true;
   		   document.getElementById('lockYNFlag').checked=true;			//Changes Added for Password Policy CR KOC 1235
	 }//end of if(obj_checkbox.checked)
     else
     {
   		   document.forms[1].elements['userAccessVO.roleID'].disabled =true;
		   document.forms[1].elements['userAccessVO.roleID'].className = "selectBoxDisabled";
		   document.getElementById('accessYNFlag').checked=false;
		  // document.getElementById('lockYNFlag').checked=false;			//Changes Added for Password Policy CR KOC 1235
		   document.getElementById('hidAccessYNFlag').value="";
     }//end of else if(obj_checkbox.checked)
}//end of SetState(obj_checkbox)

function onSave()
{
	document.getElementById('activeYNFlag').disabled = false;
	
	trimForm(document.forms[1]);
	if(document.getElementById("phoneNbr1").value=="Phone No1")
		document.getElementById("phoneNbr1").value	=	"";
	
	if(document.getElementById("phoneNbr2").value=="Phone No2")
		document.getElementById("phoneNbr2").value	=	"";
	// this is commented because Doctor is not required as we using separate contacts for Doctors - professions
	/*if(document.forms[1].elements['userType'].value=='HOS')
	{
		if(document.getElementById("6").style.display !="")
		{
		  	document.forms[1].elements['additionalInfoVO.registrationNbr'].value="";
			document.forms[1].elements['additionalInfoVO.residentYN'].value="";
			document.forms[1].elements['additionalInfoVO.qualification'].value="";
			document.forms[1].elements['additionalInfoVO.specTypeID'].value="";
		}//end of if(document.getElementById("6").style.display !="")
	}*///end of if(document.forms[1].elements['userType'].value=='HOS')
	if(document.getElementById('autoAssignYnFlag')!=null){
    if(document.getElementById('autoAssignYnFlag').checked){
		
		var autoassigntem=document.getElementById("autoAssignTemID").value;
		if(autoassigntem === null || autoassigntem ===""){
			alert("User Assign Templet is required.");
			return false;
		}
		
	}
	}
	if(!JS_SecondSubmit)
	{
		document.forms[1].mode.value="doSave";
		document.forms[1].action="/UpdateContact.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onSave()



function onSaveEmployee(){

	document.forms[1].flag.value="S";
	document.forms[1].mode.value="doSaveEmployee";
	document.forms[1].action="/EmployeeContactDeatils.do";
	JS_SecondSubmit=true
	document.forms[1].submit();
	
}

function onEmployeeResetPassword(){

	if(!JS_SecondSubmit)
    {
		var message=confirm('Do you want to reset the password');
		if(message)
		{
			document.forms[1].flag.value="R";
			document.forms[1].mode.value="doResetEmployeePassword";
			document.forms[1].action="/EmployeeContactDeatils.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit) 	
	}//end of if(message)
}


function employeeClose()
{
	document.forms[1].mode.value = "doSearch";
    document.forms[1].action = "/UserListSearchAction.do";
    document.forms[1].submit();
}//end of Close()





function ResetEmployee()
{
	document.forms[1].mode.value = "resetEmployee";
    document.forms[1].action = "/EmployeeContactDeatils.do";
    document.forms[1].submit();
}//end of Close()












function onSaveProfessions()
{
	trimForm(document.forms[1]);
	if(document.getElementById("phoneNbr1").value=="Phone No1")
		document.getElementById("phoneNbr1").value	=	"";
	
	if(document.getElementById("phoneNbr2").value=="Phone No2")
		document.getElementById("phoneNbr2").value	=	"";
	
	if(!JS_SecondSubmit)
	{ if(isValidated())
	    {
		document.forms[1].mode.value="doSaveProfessions";
		document.forms[1].action="/UpdateProfessions.do";
		JS_SecondSubmit=true
		document.forms[1].submit();
	    }
	}//end of if(!JS_SecondSubmit)
}//end of onSaveProfessions()
function isValidated()
{
		//checks if from Date is entered
		if(document.forms[1].startDate.value.length!=0)
		{
			if(isDate(document.forms[1].startDate,"start Date")==false)
				return true;
			//document.forms[1].fromDate.focus();
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)
		if(document.forms[1].startDate.value.length!=0){
			if(isFutureDate(document.forms[1].startDate.value)){
				alert("start date should not be future date ");
				return false;
			}
		}
    //checks if to Date is entered
		if(document.forms[1].endDate.value.length!=0)
		{
			if(isDate(document.forms[1].endDate,"end Date")==false)
				return true;
		}// end of if(document.forms[1].sRecievedDate.value.length!=0)
		if(document.forms[1].startDate.value.length!=0 && document.forms[1].endDate.value.length!=0)
		{
			if(compareDates("startDate","start Date","endDate","end Date","greater than")==false)
				return false;
		}// end of if(document.forms[1].startDate.value.length!=0 && document.forms[1].endDate.value.length!=0)

		return true;
}//end of isValidated()
function isFutureDate(argDate){

	var dateArr=argDate.split("/");	
	var givenDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
	var currentDate = new Date();
	if(givenDate>currentDate){
	return true;
	}
	return false;
}
function onCloseProfessions()
{
	/*if(!TrackChanges()) return false;
	document.forms[1].mode.value="doSwitchTo";
	document.forms[1].action="/HospitalContactAction.do";
	document.forms[1].submit();*/
	
	
	document.forms[1].mode.value="doCloseClinician";
	//document.forms[1].child.value="";
	document.forms[1].action="/HospitalContactAction.do";
	document.forms[1].submit();

}//end of CloseProfessions()

function deptchange()
{
		document.forms[1].mode.value="doDeptChange";
    	document.forms[1].action="/EditUserContact.do";
    	document.forms[1].submit();	
}//end of function finendyear(financialYear)


//Changes Added for Password Policy CR KOC 1235
function SetLOCKState(obj_checkbox)
{
	if(obj_checkbox.checked)
    {		 
 		   document.getElementById('lockYNFlag').checked=true;
	 }//end of if(obj_checkbox.checked)
     else
     {
   		  document.getElementById('lockYNFlag').checked=false;
   		 document.getElementById('lockYNFlag').value="N";
		  
     }//end of else if(obj_checkbox.checked)
}//end of SetLOCKState(obj_checkbox)
//End changes for Password Policy CR KOC 1235

function showProfessionDocs(obj)
{
	
	var openPage = "/ReportsAction.do?mode=doViewProfessionFiles&module=professionDocs&fileName="+obj;
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

function getProfessionalDetails(obj)
{
	var profId	=	obj.value;
	if (window.ActiveXObject){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		if (xmlhttp){
			xmlhttp.open("GET",'/HospitalContactAction.do?mode=getProfessionalDetails&profId=' +profId ,true);
			xmlhttp.onreadystatechange=state_ChangeProvider;
			xmlhttp.send();
		}
	}
	
}

function state_ChangeProvider(){
	var result,result_arr;
	if (xmlhttp.readyState==4){
		//alert(xmlhttp.status);
	if (xmlhttp.status==200){
				result = xmlhttp.responseText;
			result_arr = result.split(","); 
			if(result_arr[1]!=undefined){
			document.getElementById("name").value=result_arr[1];
			document.getElementById("professionalAuthority").value=result_arr[2];
			}else{
				document.getElementById("name").value="";
				document.getElementById("professionalAuthority").value="";
			}
			}
			
		}
	}


function getProfessionalsName(obj)
{
	  $(document).ready(function() {
	 // $("#name").blur(function(){
	    	var ID	=	obj.value;
	    	var val	=	ID.split('[');
	    	
	    	var professionalsId	=	val[1].substring(0,val[1].length-1);
	    	var professionalsName	=	obj.value.split('[');
	    	
	        $.ajax({
	        		url: "/AsynchronousAction.do?mode=getCommonMethod&id="+professionalsName[0]+"&getType=ProfessionalsName&professionalsId="+professionalsId, 
	        		success: function(result){
	      				var res	=	result.split("@");
						document.getElementById("professionalId").value				=	res[0];
						document.getElementById("professionalAuthority").value		=	res[1]; 
	        		}}); 
	   		// });
	  });
	
}
function checkMe(obj)
{
	//alert(obj);
	if(obj=='ISD')
	{
		if(document.getElementById("isdCode").value=="")
			document.getElementById("isdCode").value=obj;
		
		if(document.getElementById("isdCode1").value=="")
			document.getElementById("isdCode1").value=obj;
	}else if(obj=='STD')
	{
		if(document.getElementById("stdCode").value=="")
			document.getElementById("stdCode").value=obj;
		
		if(document.getElementById("stdCode1").value=="")
			document.getElementById("stdCode1").value=obj;
	}else if(obj=='Phone No1')
	{
		if(document.getElementById("phoneNbr1").value=="")
			document.getElementById("phoneNbr1").value=obj;
		
	}else if(obj=='Phone No2')
	{
		if(document.getElementById("phoneNbr2").value=="")
			document.getElementById("phoneNbr2").value=obj;
	}
}

function changeMe(obj)
{
	var val	=	obj.value;
	//alert(val);
	if(val=='Phone No1')
	{
		document.getElementById("phoneNbr1").value="";
	}
	if(val=='Phone No2')
	{
		document.getElementById("phoneNbr2").value="";
	}
	if(val=='ISD')
	{
		if(document.getElementById("isdCode").value=="ISD")
			document.getElementById("isdCode").value="";
		if(document.getElementById("isdCode1").value=="ISD")
			document.getElementById("isdCode1").value="";
	}
	if(val=='STD')
	{
		if(document.getElementById("stdCode").value=="STD")
			document.getElementById("stdCode").value="";
		if(document.getElementById("stdCode1").value=="STD")
			document.getElementById("stdCode1").value="";
	}
	
function intilizeApplet(){
	var container = document.getElementById("applet-container");
		container.innerHTML = "<applet code=\"applet.JSGDApplet.class\" id=\"fingerPrintApplet\" width=\"550\" height=\"350\" name=\"JSGDApplet\" MAYSCRIPT archive=\"ttk/Applets.jar\" hspace=\"0\" vspace=\"0\" align=\"middle\">";
		container.innerHTML += "</applet>"; 
 }
}


function autoAssignYn(){
	
	if(!document.getElementById('autoAssignYnFlag').checked)
	{
		
		document.getElementById('autoAssignYnFlag').checked=false;
		document.getElementById('autoAssignTemID').value='';
		document.getElementById('userassignID').style.display = "none";
		
		
	}//end of if(!document.forms[1].elements['activeYN'].checked)
	else{
	
		document.getElementById('userassignID').style.display = "block";
	}
}

function onChangeCompany(){
	document.forms[1].mode.value="doCompanyChange";
	document.forms[1].action="/EditUserContact.do";
	document.forms[1].submit();	
}
