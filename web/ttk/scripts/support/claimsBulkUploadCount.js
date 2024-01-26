function onDateValidation(element,elementMsg)
{
	var date='';
	if(element){
		date=element.value;
		var validate=isFutureDate(date);
		if(validate)
		{
			alert("\'"+elementMsg+"\'"+ " cannot be future date.");
			element.value='';
			element.focus();
			return false;
		}else
			return true;
	}else{
		return true;
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

function onSubmitForm()
{
	//1:  from date checking null
	var sdate = document.getElementById('sStartDate').value;
	if(sdate == "" || sdate == null)
	{
		alert("Please enter From Date.");
		document.getElementById('sStartDate').focus();
		return false;
	}
	
	//2:  from date-formate checking
	var pattern = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
    if (!pattern.test(sdate)) 
    {
       alert("Please enter valid From Date.");
       document.getElementById('sStartDate').value="";
       document.getElementById('sStartDate').focus();
       return false;
    }
    
  //3:  to date-formate checking
    var edate = document.getElementById('sEndDate').value;
    if(edate.length != 0)
    {
	    var pattern = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
	    if (!pattern.test(edate)) 
	    {
	       alert("Please enter valid to Date.");
	       document.getElementById('sEndDate').value="";
	       document.getElementById('sEndDate').focus();
	       return false;
	    }
    }
	
    //4 :  30 days validaion
    if(sdate.length !=0 && edate.length != 0)
    {
    		var day1 = sdate.substring (0, sdate.indexOf ("/"));
    	    var month1 = sdate.substring (sdate.indexOf ("/")+1, sdate.lastIndexOf ("/"));
    	    var year1 = sdate.substring (sdate.lastIndexOf ("/")+1, sdate.length);

    	    var day2 = edate.substring (0, edate.indexOf ("/"));
    	    var month2 = edate.substring (edate.indexOf ("/")+1, edate.lastIndexOf ("/"));
    	    var year2 = edate.substring (edate.lastIndexOf ("/")+1, edate.length);

    	    var    date1 = year1+"/"+month1+"/"+day1;
    	    var    date2 = year2+"/"+month2+"/"+day2;
    	    firstDate = Date.parse(date1);
    	    secondDate= Date.parse(date2);
    	    msPerDay =  1000 * 3600 * 24;
    	    dbd = Math.ceil((secondDate.valueOf()-firstDate.valueOf())/ msPerDay) + 1;
    	    if(dbd > 30)
    	    {
    	        alert("Days betweeen From Date and End date Should be Less then 30 days.");
    	        document.getElementById('sStartDate').value="";
    	        document.getElementById('sEndDate').value="";
    	        document.getElementById('sStartDate').focus();
    	        return false;
    	    }    
    }
    
    //5 :   compar date validaion
    
    if(sdate.length !=0 && edate.length != 0)
	{
    	var sdate = sdate.split("/");
		var altsdate=sdate[1]+"/"+sdate[0]+"/"+sdate[2];
		altsdate=new Date(altsdate);
		
		var edate =edate.split("/");
		var altedate=edate[1]+"/"+edate[0]+"/"+edate[2];
		altedate=new Date(altedate);
		
		var Startdate = new Date(altsdate);
		var EndDate =  new Date(altedate);
		
		if(EndDate < Startdate)
		 {
			alert("End Date should be greater than or equal to Start Date");
			document.getElementById('sStartDate').value="";
			document.getElementById('sEndDate').value="";
			document.getElementById('sStartDate').focus();
	        return;
		 }
	} 
	document.forms[1].mode.value="doGenerateReport";
	document.forms[1].action.value="/ClaimsBulkUploadCountAction.do";
	document.forms[1].submit();
}

function onReset()
{
	document.forms[1].mode.value="doReset";
	document.forms[1].action.value="/ClaimsBulkUploadCountAction.do";
	document.forms[1].submit();
}