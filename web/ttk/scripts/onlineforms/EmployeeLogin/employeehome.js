/**
 * 
 */
function edit(rownum)
{
	document.forms[1].rownum.value=rownum;
	var relationship='';
	 $.ajax({
 	     url :"/PreAuthReportEmployeeAction.do?mode=dogetRelatioship",
 	     dataType:"text",
 	     data :"rownum="+rownum,
 	     success : function(data) {
 	    	relationship=data;  
 	     }
 	 });
	if(relationship=='Principal'){
		document.forms[1].mode.value="doViewEmpDetails";
	    document.forms[1].action="/AddEnrollmentAction.do";
	}else{		
		document.forms[1].mode.value="doViewPolicy";
		document.forms[1].action="/OnlinePolicyEmplDetailsAction.do";
	}
	
	document.forms[1].submit();
}//end of editRoot(strRootIndex)

function onSearch()
{
	if(!JS_SecondSubmit)
	{
		 if(document.forms[1].treatementStartDate.value.length!=0){
	 			if(isFutureDate(document.forms[1].treatementStartDate.value)){
	 				alert("Treatment Date From cannot be future date ");
	 				return;
	 			}
	         }
		 if(document.forms[1].treatementEndDate.value.length!=0){
	 			if(isFutureDate(document.forms[1].treatementEndDate.value)){
	 				alert("Treatment Date To cannot be future date ");
	 				return;
	 			}
	         }
	document.forms[1].mode.value="doSearch";
	document.forms[1].action="/PreAuthEmployeeSearchAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onSearch()

function goToHome(){
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefault";
	document.forms[1].leftlink.value="Employee Home";
	document.forms[1].sublink.value="Home";
	document.forms[1].tab.value="Home";
	document.forms[1].action="/EmployeeHomeAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

function toggle(sortid)
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
    document.forms[1].sortId.value=sortid;
    document.forms[1].action = "/PreAuthEmployeeAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of toggle(sortid)
function onEmplClose()
{
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineMemberAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}//end of onSearch()

function edit2(rownum){
	if(!JS_SecondSubmit)
	{
    document.forms[1].mode.value="doViewMemberDetails";
    document.forms[1].rownum.value=rownum;
    document.forms[1].action = "/OnlineHistoryAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of edit(rownum)

function goBackToPreAuth(){
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doBackToPreAuth";
	document.forms[1].action="/PreAuthEmployeeAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}
/*function onSubmit(){*/
	/*document.forms[1].mode.value="doSubmitReport";
	document.forms[1].action="/PreAuthReportEmployeeAction.do";
	document.forms[1].submit();
	*/
	$(document).ready(function(){
		$('#btnSubmit').click(function(event) {
			var finalRemarks = document.getElementById("finalRemarksId").value.trim();
			var customerNbr = document.forms[1].customerNbr.value;
			/*var emailID2 =document.forms[1].emailID2.value;*/
			event.preventDefault();
			if(customerNbr.length==0){
				document.getElementById("errorMessage").value="Please update contact information before submitting query";
			}else{
				var editable = document.forms[1].editable.value;
				if(editable=='ContactInfo'){
					document.getElementById("errorMessage").value="Please update contact information before submitting query";
					return false;
				}
				if(finalRemarks.length==0)
					document.getElementById("errorMessage").value="Remarks is Required";
				else if(finalRemarks.length>1000){
					document.getElementById("errorMessage").value="Remarks cannot be more than 1000 characters";
				}else if(finalRemarks.length>=1&&finalRemarks.length<10){
					document.getElementById("errorMessage").value="Remarks cannot be less than 10 characters";
				}else{
					document.getElementById('errorMessage').value='';
					 $.ajax({
				  	     url :"/PreAuthReportEmployeeAction.do?mode=doSubmitReport",
				  	     dataType:"text",
				  	     data :"finalRemarks="+finalRemarks,
						type:"POST",
						async:false,
					    
				  	     success : function(data) {	 
				  	    	document.getElementById('responseMessage').innerHTML=data;
				  	    	document.getElementById('id01').style.display='block';
				  	    	document.getElementById('finalRemarksId').value=''; 
				  	     }
				  	 });
				}
			}
			
		});
//		event.preventDefault();	  	
	 });
/*}*/
	
	
	
	
	
	
	
	
    


function onNodePolicyTob(){
	var prodPolicySeqID=document.forms[1].prodPolicySeqID.value;
	   	var openPage = "/PreAuthEmployeeAction.do?mode=doViewPolicyTOB&prodPolicySeqID="+prodPolicySeqID;	 
	   	var w = screen.availWidth - 10;
	  	var h = screen.availHeight - 49;
	  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	  	window.open(openPage,'',features);
}



function onBenefitUtilization(){
	   document.forms[1].mode.value="viewEmpBenefitDetails";
	   document.forms[1].action="/OnlineMemberDetailsAction.do";	 
	   document.forms[1].submit();
}

function onBenefitUtilizationForMem(){	
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineMemberAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}
function onEcards()
{
   var openPage = "/OnlineMemberAction.do?mode=doECards";
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}//end of function onECards()


function pageIndex(pagenumber)
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value="doSearch";
	document.forms[1].action="/PreAuthEmployeeAction.do";
    document.forms[1].pageId.value=pagenumber;
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of pageIndex(pagenumber)

function PressForward()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value ="doPreAuthForward";
    document.forms[1].action = "/PreAuthEmployeeAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressForward()

function PressBackWard()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value ="doPreAuthBackward";
    document.forms[1].action = "/PreAuthEmployeeAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()
function onEditContactInfo(){
	document.forms[1].mode.value="doEditable";
    document.forms[1].action = "/GetContactEmployeeAction.do?editable=ContactInfo";
    document.forms[1].submit();
}

function onSaveAccountInfo(){
	document.forms[1].mode.value="doSaveAccountInfo";
    document.forms[1].action = "/UpdateContactEmployeeAction.do?editable="+document.forms[1].editable.value;
    document.forms[1].submit();
	
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