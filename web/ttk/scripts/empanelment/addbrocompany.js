//java script for the insurance company screen in the empanelment of insurance flow.


//get the states and cities
function getStates(name1,id1){
	
	 var myselect1=document.getElementById("cityCode");
	 while (myselect1.hasChildNodes()) {   
    	    myselect1.removeChild(myselect1.firstChild);
      }
	 myselect1.options.add(new Option("Select from list",""));	 
	   document.getElementById("stdCode1").value="0";
	    document.getElementById("stdCode2").value="0";
	    document.getElementById("stdCode1").readOnly=false;
	    document.getElementById("stdCode2").readOnly=false;
   
    var path;
    if(name1==="state"){
        var countryId= document.getElementById("cnCode").value;
         path="/asynchronAction.do?mode=getStates&sorc=states&countryId="+countryId;
         getIsdOrStd("/asynchronAction.do?mode=getIsdOrStd&iors=ISD&countryId="+countryId,"isd");        
    }
    else{
    	var stateId= document.getElementById("stateCode").value;
         path="/asynchronAction.do?mode=getAreas&sorc=cities&stateId="+stateId;
         getIsdOrStd("/asynchronAction.do?mode=getIsdOrStd&iors=STD&stateId="+stateId,"std");        
    }

    $(document).ready(function(){ 
   	 $.ajax({
   	     url :path,
   	     dataType:"text",
   	     success : function(data) {
   	   	    
   	      var myselect2=document.getElementById(id1);

   	      while (myselect2.hasChildNodes()) {   
   	    	    myselect2.removeChild(myselect2.firstChild);
   	      }
   	   myselect2.options.add(new Option("Select from list",""));             
   	     var res1 = data.split("&");
   	     
   	     for(var i=0;i<res1.length;i++){   	    	    
   	     var res2=res1[i].split("@");
   	        myselect2.options.add(new Option(res2[1],res2[0]));  	                 
   	     }
  	      
   	     }
   	 });

   	});	

}

function getIsdOrStd(path,id1){
	$.ajax({
	     url :path,
	     dataType:"text",
	     success : function(data) {
		     if(!(data==null||data=="")){
		     if(id1==="isd"){
		         document.getElementById("isdCode1").value=data;
		         document.getElementById("isdCode1").readOnly=true;
		         document.getElementById("isdCode2").value=data;
		         document.getElementById("isdCode2").readOnly=true;		         
		         
		     }else{
		    	 document.getElementById("stdCode1").value=data;
		    	 document.getElementById("stdCode1").readOnly=true;
		         document.getElementById("stdCode2").value=data;
		         document.getElementById("stdCode2").readOnly=true;
		       
			     } 
	     }              
	     }
	 });	
}


function onSave()
{	
///console.log("vsl"+x);
var data=document.forms[1].licenseExpDate.value;
var date2=new Date(new Date().setHours(0,0,0,0));
data=data.split("/");
var day=parseInt(data[0],10);
var month=parseInt(data[1],10);
var year=parseInt(data[2],10);
var date1=new Date(year,month-1,day);
var elementcheckedornot=document.getElementById("companyStatus");

if(!elementcheckedornot.checked){
var activeDate=document.forms[1].inactiveDate.value;
var remarks=document.forms[1].remarks.value;

if(activeDate==""){
	alert("Inactive Date is mandatory field");
	return false;}
if(remarks==""){
	alert(" remarks is mandatory field");
	return false;}

else{var remarks=document.forms[1].remarks.value;
remarks=$.trim(remarks);
	remarks=remarks.split(" ");
	
	
	if(remarks.length<15) {
		alert("Please Enter Valid remarks for Broker Inactivity, with a minimum of 15 words");
		return false;
	}
}
}
if(date1<date2) {alert("As per the data License has been expired. Please enter valid expiration date");return false;}


else {	
	if(document.getElementById("phoneNbr1").value=="Phone No1")
		document.getElementById("phoneNbr1").value	=	"";
	if(document.getElementById("phoneNbr2").value=="Phone No2")
		document.getElementById("phoneNbr2").value	=	"";

	
 if(!JS_SecondSubmit)
 {	
	 
	trimForm(document.forms[1]);
	document.forms[1].mode.value="doSave";
	document.forms[1].action="/UpdateBroCompanyAction.do";
	JS_SecondSubmit=true
	document.forms[1].submit();
 
 }
 
 //end of if(!JS_SecondSubmit)	
 

}

}//end of onSave()

function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset) 
	{
		if(document.forms[1].insuranceSeqID.value=="")
			document.forms[1].mode.value="doAdd";
		else
			document.forms[1].mode.value="doView";
			document.forms[1].action="/AddEditBroCompanyAction.do";
			document.forms[1].submit();
	}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	else
	{
		document.forms[1].reset();
	}//end of else
}//end of onReset()

function onClose()
{
	if(!TrackChanges()) return false;

	if(document.forms[1].officeType.value == "IHO" && document.forms[1].insuranceSeqID.value=="")
	{
		document.forms[1].mode.value="doClose";
		document.forms[1].child.value="";
		document.forms[1].tab.value="Search";
		document.forms[1].action="/EmpBrokerAction.do";
		document.forms[1].submit();
	}//end of if(document.forms[1].officeType.value == "IHO" && document.forms[1].insuranceSeqID.value=="")
	else
	{
		document.forms[1].mode.value="doViewCompanySummary";
		document.forms[1].child.value="";
		document.forms[1].action="/BrokerDetailAction.do";
		document.forms[1].submit();
	}//end of else
}//end of onClose()

function onChangeState()
{
    	document.forms[1].mode.value="doChangeState";
    	document.forms[1].focusID.value="state";
    	document.forms[1].action="/AddEditBroCompanyAction.do";
    	document.forms[1].submit();
}//end of onChangeState()

function getMe(obj)
{
	//alert(obj);
	if(obj=='Phone No1')
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
}
/*
function getInactive()
{
$(document).ready(function() {
var x=document.getElementById("companyStatus").value;
document.getElementById("companyStatus").setAttribute("value","N");
console.log("sssssss"+x);
$("#inactive").hide();
	//document.getElementById("inactive").style.visibility="visible";
var y=document.getElementById("inactive");
	
	  });
}
*/