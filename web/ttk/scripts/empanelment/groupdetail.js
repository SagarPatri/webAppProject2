	//java script for the groupdetail.jsp screen in the Group Registration of Empanelment flow.

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


	//function save
	function onSave()
	{
		if(document.forms[1].bankname.value=="" || document.forms[1].bankname.value=="NA")
        {
          alert('Kindly select a Bank !!');
           document.forms[1].bankname.focus();
           return false;
        }
		if(document.forms[1].accountnoIBANno.value=="" || document.forms[1].accountnoIBANno.value=="NA")
        {
          alert('Kindly select Bank Account No/IBAN No !!');
           document.forms[1].accountnoIBANno.focus();
           return false;
        }
			
		
		
		if(document.getElementById("phoneNbr2").value=='Phone No2')
			document.getElementById("phoneNbr2").value='';
	  if(!JS_SecondSubmit)
      {	
		  trimForm(document.forms[1]);
		  var elementData=false;
		  if(document.getElementById("priorityCorporate")){
			  elementData= document.getElementById("priorityCorporate").checked;
			  if(elementData){
					document.getElementById("priorityCorporate").checked=true;
					document.getElementById("priorityCorporate").value='On';
				}else{
					document.getElementById("priorityCorporate").checked=false;
					document.getElementById("priorityCorporate").value='';
				}
			  document.forms[1].action="/GroupRegistrationSaveAction.do?priorityCorp="+document.getElementById("priorityCorporate").value;
		  }else{
			  document.forms[1].action="/GroupRegistrationSaveAction.do";
		  }			  
		document.forms[1].mode.value="doSave";
		JS_SecondSubmit=true;
		document.forms[1].submit();
 	  }//end of if(!JS_SecondSubmit)	
	}//end of onSave()

	//function close
	function onClose()
	{
		if(!TrackChanges()) return false;

	 	document.forms[1].mode.value="doClose";
	 	document.forms[1].child.value="";
	 	document.forms[1].action="/GroupListAction.do";
	 	document.forms[1].submit();
	}//end of function onClose()

	//function onReset
	function onReset()
	{
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{
				document.forms[1].mode.value ="doReset";
				document.forms[1].action = "/GroupRegistrationAction.do";
				document.forms[1].submit();

		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
			onchangeGroup();
		}//end of else
	}//end of onReset()

	//function to delete
	function onDelete()
	{
	 	var msg = confirm("Are you sure you want to delete the record ?");
		if(msg)
		{
			document.forms[1].mode.value="doDelete";
			document.forms[1].action="/GroupListAction.do";
			document.forms[1].submit();
		}//end of if(msg)
	}//end of onDelete()

	function onChangeState(obj)		
	{
		if(obj.value!="")
	    {
    	document.forms[1].mode.value="doChangeState";
    	document.forms[1].focusID.value="state";
    	document.forms[1].action="/GroupRegistrationAction.do";
    	document.forms[1].submit();
	    }
	}//end of onChangeState()
	
	function selectAccntManager()		
	{
    	document.forms[1].mode.value="doSelectManager";
    	document.forms[1].action="/GroupRegistrationAction.do";
    	document.forms[1].submit();
	}//end of selectAccntManager()
	
	function clearAccntManager()
	{
		document.forms[1].mode.value="doClearManager";
    	document.forms[1].action="/GroupRegistrationAction.do";
    	document.forms[1].submit();
	}//end of clearAccntManager()
	function onchangeGroup()
	{
		selObj = document.forms[1].groupGenTypeID;
		if(selObj!=null)
		{
		    var selVal = selObj.options[selObj.selectedIndex].value;
			if(selVal == 'CRP')
			{
				document.getElementById('corporate').style.display = "";
				
			}//end of if(selVal == 'CRP')
			else
			{
				document.forms[1].ccEmailId.value="";
				document.forms[1].notiEmailId.value="";
				document.forms[1].notiTypeId.value="NIG";
				document.getElementById('corporate').style.display = "none";
			}//end of else
		}//end of if(selObj!=null)
		// added for koc 1346
		if(document.forms[1].ppn.value=="Y")
		{
			document.forms[1].ppnYN.checked=true;
		}
		else
		{
			document.forms[1].ppnYN.checked=false;
		}
		//end  added for koc 1346
	}// end of onchangeGroup()
	
	function deleteUploadedFile()
	{
		document.forms[1].mode.value="dodeleteUploadedFile";
    	document.forms[1].action="/GroupRegistrationAction.do";
    	document.forms[1].submit();
	}
	
	function onChangeBank(obj)		
	{
		if(obj.value!="")
	    {
    	document.forms[1].mode.value="doChangeBank";
    	document.forms[1].focusID.value="accountnoIBANno";
    	document.forms[1].action="/GroupRegistrationAction.do";
    	document.forms[1].submit();
	    }
	}//end of onChangeState()