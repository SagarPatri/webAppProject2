//java script called by policydetails.jsp in enrollment flow

function onGroupList()
{
	document.forms[1].mode.value="doSelectGroup";
	document.forms[1].child.value="GroupList";
	document.forms[1].action="/PolicyDetailsAction.do";
	document.forms[1].submit();
}// end of onGroupList()

function changeOffice()
{
	document.forms[1].mode.value="doSelectInsuranceCompany";
	document.forms[1].action="/PolicyDetailsAction.do";
	document.forms[1].submit();
}//end of changeOffice()

//on Click of review button
function onReview()
{
    trimForm(document.forms[1]);
    if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Review");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
    if(!JS_SecondSubmit){
		document.forms[1].mode.value="doReview";
		document.forms[1].action="/SavePolicyDetailsAction.do";
		JS_SecondSubmit=true
		document.forms[1].submit();
    }//end of if(!JS_SecondSubmit)
}//end of onReview()
//on Click of promote button

function onPromote()
{
    trimForm(document.forms[1]);
    if(TC_GetChangedElements().length>0)
    {
    	alert("Please save the modified data, before Promote");
    	return false;
    }//end of if(TC_GetChangedElements().length>0)
	var message=confirm('Policy will be moved to the next level and if you do not have privileges, you may not see this policy.');
	if(message)
	{
		if(!JS_SecondSubmit){
			document.forms[1].mode.value="doReview";
			document.forms[1].action="/SavePolicyDetailsAction.do";
			JS_SecondSubmit=true;
			document.forms[1].submit();
		}//end of if(!JS_SecondSubmit)
	}//end of if(message)

}//end of onPromote()

//on Click on save button
function onUserSubmit()
{var b=$('#brokerSeqID').val();
var c=$('#brokerCommission').val();

	trimForm(document.forms[1]);
	//if policy type is corporate check whether members count emplyoees count is numeric value and
	//assign compareYN to Y else to N
	
	var line=document.getElementById("lineOfBussiness").value;
	//console.log("line of b "+line);
var x=	$('#brokerSeqID').val();
	if(line==="IDT"){
		if(b==""){
			alert("Broker name is mandatory");
			return false;
		}
		if(c==""){
			
				alert("Broker commission is mandatory");
				return false;
			}
		
	}
		
	

	if((document.forms[1].policyTypeID.value =="COR"))
	{
		var regexp=/^[0-9]{1,}$/;
		if(regexp.test(document.forms[1].memberCnt.value) && regexp.test(document.forms[1].employeeCnt.value))
			document.forms[1].compareYN.value="Y";
		else
			document.forms[1].compareYN.value="N";
	}
	else
	{
		document.forms[1].compareYN.value="N";
	}
	if(!JS_SecondSubmit){
		document.forms[1].mode.value="doSave";
		document.forms[1].action="/SavePolicyDetailsAction.do";
		document.forms[1].enctype="multipart/form-data";
		JS_SecondSubmit=true;
		document.forms[1].submit();
    }//end of if(!JS_SecondSubmit)
}//end of onUserSubmit()

//on Click on reset button
function onReset()
{
	if(typeof(ClientReset)!= 'undefined' && !ClientReset)
	{
		document.forms[1].mode.value="doReset";
		document.forms[1].action="/PolicyDetailsAction.do";
		document.forms[1].submit();
	}
	else
	{
		document.forms[1].reset();
	}
}//end of onReset()

function getReferenceNo(refNo)
{
	document.forms[1].DMSRefID.value=refNo;
}//end of getRefernceNo(refNo)

// Function to change the date.
function changeDate(){

	if(document.forms[1].policyStatusID.value == "POA")
	{
	    if(document.forms[1].startDate.value != '' &&  isDate(document.forms[1].startDate,"Start Date")== true && document.forms[1].tenure.value != '')
	    {
		var selVal = document.forms[1].tenure.value;
		var FromDate = document.forms[1].startDate.value;
		var dateArray = FromDate.split("/");

		var passedDay = dateArray[0];
		var passedMonth = dateArray[1];
		var passedYear = dateArray[2];

		var  dateStr = passedMonth+"/"+passedDay +"/"+passedYear;
		var myDate=new Date(dateStr);
		myDate.setFullYear(myDate.getFullYear()+eval(selVal));
		myDate.setDate(myDate.getDate()-1);

		var newDt=myDate.getDate();
		var newMon=myDate.getMonth()+1;
		var newYr=myDate.getFullYear();

		if((newDt.toString()).length==1)
			newDt = "0"+newDt;

		if((newMon.toString()).length==1)
			newMon = "0"+newMon;

		document.forms[1].endDate.value=newDt+"/"+newMon+"/"+newYr;
		}// end of  if(document.forms[1].startDate.value != '')
		else
		{
			document.forms[1].startDate.value ='';
		}//end of else
	}
}//End of Function to change the date.

    function getDate(theDate)
    {
    var dateCtr="";
    var thedate="";
    thedate=theDate+"";
    if(thedate.length >= 2)
    {
     dateCtr = theDate ;
    }
    else
    {
    dateCtr = "0"+theDate;
	}
    return dateCtr;
    }
/*  Added by Unni */
	function getActualMonth(theDate)
	{
		var monthCtr="";
	if(eval(theDate.getMonth())==0)
			monthCtr="01";
	else if(eval(theDate.getMonth())==1)
			monthCtr="02";
	else if(eval(theDate.getMonth())==2)
			monthCtr="03";
	else if(eval(theDate.getMonth())==3)
			monthCtr="04";
	else if(eval(theDate.getMonth())==4) //feb
			monthCtr="05";
	else if(eval(theDate.getMonth())==5) //feb
			monthCtr="06";
	else if(eval(theDate.getMonth())==6) //feb
			monthCtr="07";
	else if(eval(theDate.getMonth())==7) //feb
			monthCtr="08";
	else if(eval(theDate.getMonth())==8) //feb
			monthCtr="09";
	else if(eval(theDate.getMonth())==9) //feb
			monthCtr="10";
	else if(eval(theDate.getMonth())==10) //feb
			monthCtr="11";
	else if(eval(theDate.getMonth())==11) //feb
			monthCtr="12";
		return monthCtr;
		}
/* End of Addition */

function onSIInfo()
{
	document.forms[1].mode.value="doShowSIInfo";
	document.forms[1].action="/PolicyDetailsAction.do";
	document.forms[1].submit();
}//end of onSIInfo()

//added by rekha for policy renewal
//Function to change the date.
function changeDate1(){

	if(document.forms[1].policyStatusID.value == "POA")
	{
	    if(document.forms[1].startDate1.value != '' &&  isDate(document.forms[1].startDate1,"Start Date")== true && document.forms[1].tenure.value != '')
	    {
		var selVal = 2;
		var FromDate = document.forms[1].startDate1.value;
		var dateArray = FromDate.split("/");

		var passedDay = dateArray[0];
		var passedMonth = dateArray[1];
		var passedYear = dateArray[2];

		var  dateStr = passedMonth+"/"+passedDay +"/"+passedYear;
		var myDate=new Date(dateStr);
		myDate.setFullYear(myDate.getFullYear()+eval(selVal));
		myDate.setDate(myDate.getDate()-1);

		var newDt=myDate.getDate();
		var newMon=myDate.getMonth()+1;
		var newYr=myDate.getFullYear();

		if((newDt.toString()).length==1)
			newDt = "0"+newDt;

		if((newMon.toString()).length==1)
			newMon = "0"+newMon;

		document.forms[1].endDate1.value=newDt+"/"+newMon+"/"+newYr;
		}// end of  if(document.forms[1].startDate.value != '')
		else
		{
			document.forms[1].startDate1.value ='';
		}//end of else
	}
}//End of Function to change the date.
function onChangeCountry()
{
	document.forms[1].mode.value="doChangeCountry";
	document.forms[1].action="/PolicyDetailsAction.do";
	document.forms[1].submit();
}

function onChangeState()
{
	document.forms[1].mode.value="doChangeState";
	document.forms[1].action="/PolicyDetailsAction.do";
	document.forms[1].submit();
}

function onUploadDocs(){
	//Test Nag
	document.forms[1].mode.value="doPolicyUploads";
	var policy_seq_id	=	document.forms[1].policySeqID.value;
	document.forms[1].action="/UploadPolicyDocs.do?policy_seq_id="+policy_seq_id;
	document.forms[1].submit();
}
function calcPercentage()
{
	var a	=	parseFloat(document.forms[1].reInsShare.value);
	var b	=	parseFloat(document.forms[1].insShare.value);
	if(!isNaN(a) && !isNaN(b))
	{
		if((a+b)!=100 || (a+b)!=100.00)
		{
			alert("Please enter valid percentage/Risk Coverage should not exceed 100%");
			document.forms[1].reInsShare.value	=	"";
			document.forms[1].insShare.value	=	"";
		}
	}
}
function callFocusObj(){
	alert("1");
	//document.forms[1].remarks.scrollIntoView(true);
	document.getElementById("remarks").focus();
	alert("2");

}



function onchangeCreditNote(obj)
{
	 if(obj.value == "OTHD"){
	
		document.getElementById("creditGenerationOth").className="textBox textBoxLarge ";
	 	document.getElementById("creditGenerationOth").readOnly="";
	 	document.getElementById("creditGenerationOth").value = "";
	 }else{
		var creditgenvaluedays = document.forms[1].creditGeneration.value;
	
		document.getElementById("creditGenerationOth").className="textBox textBoxLarge textBoxDisabled";
		document.getElementById("creditGenerationOth").readOnly="true";
		document.getElementById("creditGenerationOth").value = creditgenvaluedays;
	 }
}

function onchangeRefundCondition()
{
	document.forms[1].mode.value="doChangeRefundCondition";
	document.forms[1].action="/PolicyDetailsAction.do";
	document.forms[1].submit();
}




function selectPremiumDetails()
{

document.forms[1].mode.value="selectPremiumDetails";
document.forms[1].action="/PolicyDetailsAction.do";
document.forms[1].submit();

}