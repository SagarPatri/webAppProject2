//java script for the hospital search screen in the empanelment of hospital flow.

//function to get the substatus change
function onStatusChanged()
{
	document.forms[1].mode.value="doStatusChange";
	document.forms[1].action="/HospitalAction.do";
	document.forms[1].submit();
}//end of onStatusChanged()
//function to call edit screen
function edit(rownum)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doView";
    document.forms[1].rownum.value=rownum;
    document.forms[1].tab.value="General";
    document.forms[1].action = "/EditHospitalSearchAction.do";
    document.forms[1].submit();
}//end of edit(rownum)
//function to sort based on the link selected
function toggle(sortid)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/HospitalAction.do";
    document.forms[1].submit();
}//end of toggle(sortid)
//function to display the selected page
function pageIndex(pagenumber)
{
	document.forms[1].reset();
    document.forms[1].mode.value="doProviderSearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/EmployeeProviderHomeAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)
//function to display previous set of pages
function PressBackWard()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doProviderBackward";
    document.forms[1].action = "/EmployeeProviderHomeAction.do";
    document.forms[1].submit();
}//end of PressBackWard()
//function to display next set of pages
function PressForward()
{
	document.forms[1].reset();
    document.forms[1].mode.value ="doProviderForward";
    document.forms[1].action = "/EmployeeProviderHomeAction.do";
    document.forms[1].submit();
}//end of PressForward()
//function to display pages based on search criteria
function onSearch()
{
	if(!JS_SecondSubmit)
	 {
		
		if(document.forms[1].countryCode.value.length==0){
			alert("Please Select The Country");
		}else{
			document.forms[1].mode.value = "doProviderSearch";
		    document.forms[1].action = "/EmployeeProviderHomeAction.do";
		    document.forms[1].submit();
		}
	    
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()
//function to add hospital
function onAddHospital()
{
	document.forms[1].reset();
    document.forms[1].rownum.value = "";
    document.forms[1].mode.value = "doAdd";
    document.forms[1].tab.value="General";
    document.forms[1].action = "/EditHospitalSearchAction.do";
    document.forms[1].submit();
}//end of onAddHospital()
//copy the select hospitl to webboard
function copyToWebBoard()
{
    if(!mChkboxValidation(document.forms[1]))
    {
    	//document.forms[1].reset();
	    document.forms[1].mode.value = "doCopyToWebBoard";
   		document.forms[1].action = "/HospitalAction.do";
	    document.forms[1].submit();
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of copyToWebBoard()

function onQuery(){
	if(!JS_SecondSubmit)
	{
	 	document.forms[1].mode.value = "doEmployeeQuery";
	 	document.forms[1].action = "/EmployeeContactAction.do";
	 	JS_SecondSubmit=true;
	    document.forms[1].submit();
	}
}


function onGetStateList(focusid){
	document.forms[1].mode.value="doGetProviderState";
    document.forms[1].focusID.value="stateCode";
    document.forms[1].stateCode.focus();
    document.forms[1].stateCode.value="";
	document.forms[1].action="/EmployeeProviderHomeAction.do?focusID=stateCode";
	document.forms[1].submit();
}

function onGetCityList(focusid){
	document.forms[1].mode.value="doGetProviderCity";
    document.forms[1].focusID.value="cityCode";
    document.forms[1].cityCode.focus();
    document.forms[1].cityCode.value="";
	document.forms[1].action="/EmployeeProviderHomeAction.do?focusID=cityCode";
	document.forms[1].submit();
}

function onExportToExcel(){
       var partmeter = "?mode=doGenerateReport";
	    var openPage = "/EmployeeProviderHomeAction.do"+partmeter;
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 99;
		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open(openPage,'',features);
}

function onQueryClose(){
	document.forms[1].mode.value = "doDefaultContact";
 	document.forms[1].action = "/EmployeeContactAction.do";
    document.forms[1].submit();
}

function onSubmit(){
	$(document).ready(function(){
		var finalRemarks = document.getElementById("finalRemarksId").value.trim();
		var queryCategory=document.getElementById("queryType").value;
		if(queryCategory.length==0){
			if(finalRemarks.length!=0)
				document.getElementById('errorMessage').value='';
			document.getElementById("networkTypeErrorMessage").value="Please Select a Category for your query";
			
		}	
		if(finalRemarks.length==0){
			document.getElementById("errorMessage").value="Remarks field cannot be blank";
			if(queryCategory.length!=0)
			document.getElementById('networkTypeErrorMessage').value='';
		}	
		else if(finalRemarks.length>1000){
			if(queryCategory.length!=0)
				document.getElementById('networkTypeErrorMessage').value='';
			document.getElementById("errorMessage").value="Remarks cannot be more than 1000 characters";
		}else if(finalRemarks.length>=1&&finalRemarks.length<10){
			if(queryCategory.length!=0)
				document.getElementById('networkTypeErrorMessage').value='';
			document.getElementById("errorMessage").value="Remarks cannot be less than 10 characters";
		}else if(queryCategory.length!=0&&finalRemarks.length>10&&finalRemarks.length<1000){
			document.getElementById('errorMessage').value='';
			 $.ajax({
		  	     url :"/PreAuthReportEmployeeAction.do?mode=doSubmitReport",
		  	     dataType:"text",
		  	     data :"finalRemarks="+finalRemarks+"&queryCategory="+queryCategory,
		  	     success : function(data) {	  
		  	    	document.getElementById('responseMessage').innerHTML=data;
		  	    	document.getElementById('id01').style.display='block';
		  	    	document.getElementById('finalRemarksId').value='';  
		  	    	document.getElementById('networkTypeErrorMessage').value=''; 
		  	     }
		  	 });
		}	  	
	 });
	return false;
}
