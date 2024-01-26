// Java Script function for paymentdiscountcofg.jsp
function onFastTrackSave()
{	
	if(!JS_SecondSubmit)
	{
		 if(document.forms[1].fToDays.value==""  || document.forms[1].fToDays.value==="0" || document.forms[1].fToDays.value>365){
			alert("Please Provide Fast Track Discount Payment To Day In Number only!!!");
			document.forms[1].fToDays.focus();
			return false;
		}else if(document.forms[1].fDiscountPerc.value=="" || document.forms[1].fDiscountPerc.value==="0"){
			alert("Please Provide Discount (in %) !!!");
			document.forms[1].fDiscountPerc.focus();
			return false;
		}else if(document.forms[1].fStartDate.value==""){
			alert("Please Provide Start Date !!!");
			document.forms[1].fStartDate.focus();
			return false;
		}else if(document.forms[1].fEndDate.value==""){
			alert("Please Provide End Date !!!");
			document.forms[1].fEndDate.focus();
			return false;
		}
			var stParts =document.forms[1].fStartDate.value.split('/');
			var endParts=document.forms[1].fEndDate.value.split('/');
			var startDate = new Date(stParts[2], stParts[1] - 1, stParts[0]);
			var endDate = new Date(endParts[2], endParts[1] - 1, endParts[0]);
		if(startDate>endDate){
			alert("Please Provide End Date Greater Than Start Date!!!");
			document.forms[1].fEndDate.focus();
			return false;
		}else{
			var oneDay = 24*60*60*1000; 
			var diffDays = Math.round(Math.abs((startDate.getTime() - endDate.getTime())/(oneDay)));
			var stYear=startDate.getYear();
			var endYear=endDate.getYear();
			var stLeapYear=stYear % 4 == 0;
			var endLeapYear=endYear % 4 == 0;
			var validateFlag=false;
			if(stLeapYear==true||endLeapYear==true){
				if(diffDays > 365){
					validateFlag=true;
					document.forms[1].fEndDate.value='';
					document.forms[1].fEndDate.focus();
					alert("Allowed duration for Start date and end date upto one year only!!! ");
				}
			}else{
				if(diffDays > 364){
					validateFlag=true;
					document.forms[1].fEndDate.value='';
					document.forms[1].fEndDate.focus();
					alert("Allowed duration for Start date and end date upto one year only!!!");
				}
			}
			if(validateFlag==false){
				document.forms[1].mode.value="doFastTrackSave";
				document.forms[1].action="/FastTrackAction.do";
				JS_SecondSubmit=true;
				document.forms[1].submit();
			}
		
		}
	}
}//end of onSave()

function onVolumeSave()
{	
	if(!JS_SecondSubmit)
	{
		if(document.forms[1].vDiscountType.value==""){
			alert("Please Provide Discount Type");
			document.forms[1].vDiscountType.focus();
			return false;
		}else if(document.forms[1].vAmountStartRange.value==""  || document.forms[1].vAmountStartRange.value==="0"){
			alert("Please Provide Discount Volume Amount start Range");
			document.forms[1].vAmountStartRange.focus();
			return false;
		}else if(document.forms[1].vAmountEndRange.value==""  || document.forms[1].vAmountEndRange.value==="0"){
			alert("Please Provide Discount Volume Amount End Range");
			document.forms[1].vAmountEndRange.focus();
			return false;
		}else if(document.forms[1].vDiscountPerc.value==""  || document.forms[1].vDiscountPerc.value==="0"){
			alert("Please Provide Discount (in %)");
			document.forms[1].vDiscountPerc.focus();
			return false;
		}else if(document.forms[1].vStartDate.value==""){
			alert("Volume Discount  Start Date");
			document.forms[1].vStartDate.focus();
			return false;
		}else if(document.forms[1].vEndDate.value==""){
			alert("Volume Discount  End Date");
			document.forms[1].vEndDate.focus();
			return false;
		}else if(!(document.forms[1].vEndDate.value=="") && (document.forms[1].vEndDate.value < document.forms[1].vStartDate.value)){
			alert("Please Provide End Date Greater Than Start Date");
			document.forms[1].vEndDate.focus();
			return false;
		}else{
		document.forms[1].mode.value="doVolumeSave";
		document.forms[1].action="/FastTrackAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
		}
	}
}//end of onSave()


function onClose()
{
	document.forms[1].mode.value="doProviderDiscountClose";
	document.forms[1].action="/FastTrackAction.do";
	document.forms[1].submit();
}//end of onClose()

function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	 {
	  document.forms[1].mode.value="doConfReset";
	  var hosp_seq_id	=	document.forms[1].hospSeqId.value;
	  document.forms[1].action="/AddHospitalSearchAction.do?hosp_seq_id="+hosp_seq_id;
	  document.forms[1].submit();
	 }//end of if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	 else
	 {
	  document.forms[1].reset();
	 }//end of else   
}//end of onReset()
