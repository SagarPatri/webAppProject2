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
	if(document.getElementById("phoneNbr2").value=='Phone No2')
		document.getElementById("phoneNbr2").value='';
 if(!JS_SecondSubmit)
 {	
	trimForm(document.forms[1]);
	document.forms[1].mode.value="doSave";
	document.forms[1].action="/UpdateInsCompanyAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
 }//end of if(!JS_SecondSubmit)	
}//end of onSave()

function onReset(){
	if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	{
		if(document.forms[1].insuranceSeqID.value=="")
			document.forms[1].mode.value="doAdd";
		else
			document.forms[1].mode.value="doView";
			document.forms[1].action="/AddEditInsCompanyAction.do";
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
		document.forms[1].action="/EmpInsuranceAction.do";
		document.forms[1].submit();
	}//end of if(document.forms[1].officeType.value == "IHO" && document.forms[1].insuranceSeqID.value=="")
	else
	{
		document.forms[1].mode.value="doViewCompanySummary";
		document.forms[1].child.value="";
		document.forms[1].action="/CompanyDetailAction.do";
		document.forms[1].submit();
	}//end of else
}//end of onClose()

function onChangeState(obj){
	if(obj.value!=""){
    	document.forms[1].mode.value="doChangeState";
    	document.forms[1].focusID.value="state";
    	document.forms[1].action="/AddEditInsCompanyAction.do";
    	document.forms[1].submit();
    }
}//end of onChangeState()