function onAddReIns() {
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value ="doAddReInsure";
	document.forms[1].tab.value="General";
	document.forms[1].action ="/AddReInsuranceAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
	
}


//function to sort based on the link selected
	function toggle(sortid)
	{
		document.forms[1].reset();
	    document.forms[1].mode.value="doSearch";
	    document.forms[1].sortId.value=sortid;
	    document.forms[1].action = "/SearchReInsuranceAction.do";
	    document.forms[1].submit();
	}//end of toggle(sortid)

	function edit(rownum)
	{
	    document.forms[1].mode.value="doView";
	    document.forms[1].rownum.value=rownum;
	    document.forms[1].tab.value="General";
	    document.forms[1].action = "/AddReInsuranceAction.do";
	    document.forms[1].submit();
	}//end of edit(rownum)

	//function to display the selected page
	function pageIndex(pagenumber)
	{
		document.forms[1].reset();
	    document.forms[1].mode.value="doSearch";
	    document.forms[1].pageId.value=pagenumber;
	    document.forms[1].action = "/SearchReInsuranceAction.do";
	    document.forms[1].submit();
	}//end of pageIndex(pagenumber)

	//function to display previous set of pages
	function PressBackWard()
	{
		document.forms[1].reset();
		document.forms[1].mode.value ="doBackward";
	    document.forms[1].action = "/SearchReInsuranceAction.do";
	    document.forms[1].submit();
	}//end of PressBackWard()

	//function to display next set of pages
	function PressForward()
	{
		document.forms[1].reset();
		document.forms[1].mode.value ="doForward";
	    document.forms[1].action = "/SearchReInsuranceAction.do";
	    document.forms[1].submit();
	}//end of PressForward()

	function onSearch()
	{
		if(!JS_SecondSubmit)
		 {
			document.forms[1].sEmpanelDate.value=trim(document.forms[1].sEmpanelDate.value);
			document.forms[1].sCompanyName.value=trim(document.forms[1].sCompanyName.value);
			if(isValidated())
			{
				document.forms[1].mode.value = "doSearch";
				document.forms[1].action = "/SearchReInsuranceAction.do";
				JS_SecondSubmit=true
				document.forms[1].submit();
			}//end of if(isValidated())
		 }//end of if(!JS_SecondSubmit)
	}//end of onSearch()

	function isValidated()
	{
		if(trim(document.forms[1].sEmpanelDate.value).length>0)
		{
			if(isDate(document.forms[1].sEmpanelDate,"Empanel. Date")==false)
			{
				document.forms[1].sEmpanelDate.focus();
				return false;
			}//end of if
		}//end of if(trim(document.forms[1].sEmpanelDate.value).length>0)
		return true;
	}//end of isValidated()

	function copyToWebBoard()
	{
	    if(!mChkboxValidation(document.forms[1]))
	    {
		    document.forms[1].mode.value = "doCopyToWebBoard";
	   		document.forms[1].action = "/SearchReInsuranceAction.do";
		    document.forms[1].submit();
	    }//end of if(!mChkboxValidation(document.forms[1]))
	}//end of copyToWebBoard()

	function onDelete()
	{
	    if(!mChkboxValidation(document.forms[1]))
	    {
		var msg = confirm("Are you sure you want to delete the selected record(s)?");
		if(msg)
		{
		    document.forms[1].mode.value = "doDeleteList";
		    document.forms[1].action = "/EmpInsuranceAction.do";
		    document.forms[1].submit();
		}//end of if(msg)
	    }//end of if(!mChkboxValidation(document.forms[1]))
	}//end of onDelete()

	
	
	function onChangeState(obj){
		if(obj.value!=""){
			document.forms[1].tab.value="General";
	    	document.forms[1].mode.value="doChangeState";
	    	document.forms[1].focusID.value="state";
	    	document.forms[1].action="/AddReInsuranceAction.do";
	    	document.forms[1].submit();
	    }
	}
	
	
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
	 if(!JS_SecondSubmit)
	 {	
	   var retentionshareval = document.getElementById("retentionSharePercid").value;
	   var reinsuranceshareval = document.getElementById("reinsuranceSharePercid").value;
	   var spretentionSharePercval = document.getElementById("spretentionSharePercid").value;
	   var spreinsuranceSharePercval = document.getElementById("spreinsuranceSharePercid").value;
	   if(retentionshareval==""){
		   retentionshareval =  parseInt("0");  
	   }else{
	   retentionshareval =  parseInt(retentionshareval);
	   }
	   if(reinsuranceshareval==""){
		   reinsuranceshareval =  parseInt("0");
	   }else{
	   reinsuranceshareval =  parseInt(reinsuranceshareval);
	   }
	   
	   if(spretentionSharePercval==""){
		   spretentionSharePercval =  parseInt("0");  
	   }else{
		   spretentionSharePercval =  parseInt(spretentionSharePercval);
	   }
	   if(spreinsuranceSharePercval==""){
		   spreinsuranceSharePercval =  parseInt("0");
	   }else{
		   spreinsuranceSharePercval =  parseInt(spreinsuranceSharePercval);
	   }
	 var totalvalue =  retentionshareval+reinsuranceshareval;
	 var totalspretenandspreinsurer = spretentionSharePercval+spreinsuranceSharePercval;
	 if(totalvalue != 100){
		 alert("Sum of Qs Retention Share and Qs ReInsurer Share must be equal to 100%");
		 return;
	 }
	 
	 if(totalspretenandspreinsurer != 100){
		 alert("Sum of Sp Retention Share and Sp ReInsurer Share must be equal to 100%");
		 return;
	 }
		trimForm(document.forms[1]);
//		document.forms[1].tab.value="Add";
		document.forms[1].mode.value="doSave";
		document.forms[1].action="/SaveReInsuranceAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)	
	}//end of onSave()

	function onReset(){
		if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		{
				document.forms[1].mode.value="doAdd";
				document.forms[1].action="/AddReInsuranceAction.do";
				document.forms[1].submit();
		}//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
		else
		{
			document.forms[1].reset();
		}//end of else
	}//end of onReset()

	function onClose()
	{
		if(!JS_SecondSubmit)
		 {
			document.forms[1].mode.value="doClose";
			document.forms[1].tab.value="Search";
			document.forms[1].action="/SearchReInsuranceAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		 }
	}
	