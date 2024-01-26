//saveenrollment.js is called form saveenrollment.jsp in enrollment flow

//function onsave
function onSave()
{
    trimForm(document.forms[1]);
    if(!JS_SecondSubmit)
    {
    	if(document.getElementById("stopPreauthDateId").style.display ==""){
			var stoppreauth = document.getElementById("stopPreauthId").value;
			if(stoppreauth ==""){
				 alert("Stop Cashless Date is Required");
				  return false;
			}
			
			 if(document.getElementById("stopcashlessemployeeid").value != null){
					var stopcashlessemployeeflag =	document.getElementById("stopcashlessemployeeid").value;
					
					var  currentDate =  new Date();
					var curmonth = currentDate.getMonth() + 1;
				    var curday = currentDate.getDate();
				    var curyear = currentDate.getFullYear();
				    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
					  
				      curnewdate = curnewdate.split("/");
				      stoppreauth = stoppreauth.split("/");
				      if(stopcashlessemployeeflag !=""){
				      stopcashlessemployeeflag = stopcashlessemployeeflag.split("-");
				      stopcashlessemployeeflag = new Date(stopcashlessemployeeflag[0], stopcashlessemployeeflag[1] - 1, stopcashlessemployeeflag[2]).getTime();
				      }
					  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
					  stoppreauth = new Date(stoppreauth[2], stoppreauth[1] - 1, stoppreauth[0]).getTime();
					  
					  if(stopcashlessemployeeflag == stoppreauth){
						  
					  }else if(!(stoppreauth > curnewdate)){
						  alert("Stop Cashless Date must be future date");
						  return false;
					  }else if(stopcashlessemployeeflag==" "){
						  alert("Stop Cashless Date must be future date");
						  return false;  
					  }
					  
					  
				}
		}else{
			document.getElementById("stopcashlessemployeeid").value='';
		}
	    	
	    	if(document.getElementById("stopClaimsDateId").style.display ==""){
			var stopclms = document.getElementById("stopclmsid").value;
			if(stopclms==""){
				  alert("Stop Claims Date is Required");
				  return false;
			  }
			
			if(document.getElementById("stopclaimsemployeeid").value != null){
			var stopclaimsemployeeflag =	document.getElementById("stopclaimsemployeeid").value;
			
			var  currentDate =  new Date();
			var curmonth = currentDate.getMonth() + 1;
		    var curday = currentDate.getDate();
		    var curyear = currentDate.getFullYear();
		    var curnewdate = curday + "/" + curmonth + "/" + curyear; 
			  
		      curnewdate = curnewdate.split("/");
		      stopclms = stopclms.split("/");
		      
		      if(stopclaimsemployeeflag !=""){
		      stopclaimsemployeeflag = stopclaimsemployeeflag.split("-");
		      stopclaimsemployeeflag = new Date(stopclaimsemployeeflag[0], stopclaimsemployeeflag[1] - 1, stopclaimsemployeeflag[2]).getTime();
		      }
			  curnewdate = new Date(curnewdate[2], curnewdate[1] - 1, curnewdate[0]).getTime();
			  stopclms = new Date(stopclms[2], stopclms[1] - 1, stopclms[0]).getTime();
			 
			  if(stopclaimsemployeeflag == stopclms){
				  
			  }else if(!(stopclms > curnewdate)){
				  alert("Stop Claims Date must be future date");
				  return false;
			  }else if(stopclaimsemployeeflag == " "){
				  alert("Stop Claims Date must be future date");
				  return false;  
			  }
			  
			}  
			 
		}else{
			document.getElementById("stopclaimsemployeeid").value='';
		}
	    	
	    
	    document.getElementById("memname").value=document.forms[1].name.value;
	    
	    document.forms[1].mode.value="doSave";
	    document.forms[1].action="/EnrollmentSaveAction.do?stopclaimsemployeeid="+document.getElementById("stopclaimsemployeeid").value+"&stopcashlessemployeeid="+document.getElementById("stopcashlessemployeeid").value;
	    JS_SecondSubmit=true;
	    document.forms[1].submit();
	   
    }//end of if(!JS_SecondSubmit)
}//end of onSave()

//function onClose
function onClose()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doClose";
		document.forms[1].child.value="";
		document.forms[1].action="/EnrollmentAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onClose()

//function onClose

function onReset()
{
	if(!JS_SecondSubmit)
	 {
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{
			document.forms[1].mode.value="doReset";
			document.forms[1].action="/EnrollmentAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
		}//end of else
	 }
}//end of onReset()

//Function for Delete
function onDelete()
{
  var msg = confirm("Are you sure you want to delete the Record?");
  if(msg)
  {
    document.forms[1].mode.value="doDelete";
    document.forms[1].child.value="";
    document.forms[1].action="/EnrollmentAction.do";
    document.forms[1].submit();
  }//end of if(msg)
}//end of onDelete

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