/**
 * 
 */
function onEmplclaimHistoryClose(){
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineMemberAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

function onClaimHistorySearch(){
	if(!JS_SecondSubmit)
	{
		if(document.forms[1].treatementStartDate.value.length!=0){
 			if(isFutureDate(document.forms[1].treatementStartDate.value)){
 				alert("Claim Submission From Date cannot be future date ");
 				return;
 			}
         }
	 if(document.forms[1].treatementEndDate.value.length!=0){
 			if(isFutureDate(document.forms[1].treatementEndDate.value)){
 				alert("Claim Submission To Date cannot be future date ");
 				return;
 			}
         }
	document.forms[1].mode.value="doClaimHistorySearch";
	document.forms[1].action="/ClaimEmployeeSearchAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

function pageIndex(pagenumber)
{
    document.forms[1].reset();
    document.forms[1].mode.value="doClaimHistorySearch";
    document.forms[1].pageId.value=pagenumber;
    document.forms[1].action = "/PreAuthEmployeeAction.do";
    document.forms[1].submit();
}//end of pageIndex(pagenumber)

function PressForward()
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].mode.value ="doClaimHistoryForward";
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
    document.forms[1].mode.value ="doClaimHistoryBackward";
    document.forms[1].action = "/PreAuthEmployeeAction.do";
    JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of PressBackWard()

function edit(rownum) {
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value = "doViewMemberDetails";
	document.forms[1].rownum.value = rownum;
	document.forms[1].action = "/OnlineHistoryAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}// end of edit(rownum)

function edit2(rownum) {
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value = "doViewShortfallDetails";
	document.forms[1].rownum.value = rownum;
	document.forms[1].action = "/PreAuthEmployeeAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}

function edit3(rownum) {
	var openPage = "/EmployeeHomeAction.do?mode=doViewFile&rownum="+rownum;
  	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
  	window.open(openPage,'',features);
}

function onEmplShortfallClose(){
	document.forms[1].mode.value = "doBackToPreAuth";
	document.forms[1].action = "/PreAuthEmployeeAction.do";
	document.forms[1].submit();	
}
function onShortfallDoc(obj)
{
	var partmeter = "?mode=doShowPreAuthShortfall&fileName="+obj+"";
	var openPage = "/EmployeeHomeAction.do"+partmeter;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	//document.forms[1].mode.value="doShowShortFalls";
	window.open(openPage,'',features);
}//Using For provider Login Short fall Letter


function onBackToShortfall(){
	document.forms[1].mode.value = "doBackToShortfallList";
	document.forms[1].action = "/EmployeeHomeAction.do";
	document.forms[1].submit();	
}

function onSaveShortfall()
{
	if(document.forms[1].file.value==""){
		alert("Please select a file to Upload");
		document.forms[1].file.focus();
		return false;
	}else{
		var strPath = document.forms[1].file.value;
		var strPath01 = document.forms[1].file01.value;
		var strPath02 = document.forms[1].file02.value;
		var strfileext = strPath.substring((strPath.lastIndexOf(".")+1),strPath.length);
		if(strfileext!="pdf"&&strfileext!="png"&&strfileext!="jpeg"&&strfileext!="jpg"){
			alert("Sorry, Invalid file format." );
			document.forms[1].file.value="";
			document.forms[1].file.focus();
			return false;
		}
		if(strPath01!=null&&strPath01!=""){
			var strfileext01 = strPath01.substring((strPath01.lastIndexOf(".")+1),strPath01.length);
			if(strfileext01!="pdf"&&strfileext01!="png"&&strfileext01!="jpeg"&&strfileext01!="jpg"){
				alert("Sorry, Invalid file format." );
				document.forms[1].file01.value="";
				document.forms[1].file01.focus();
				return false;
			}	
		}
		if(strPath02!=null&&strPath02!=""){
			var strfileext02 = strPath02.substring((strPath02.lastIndexOf(".")+1),strPath02.length);
			if(strfileext02!="pdf"&&strfileext02!="png"&&strfileext02!="jpeg"&&strfileext02!="jpg"){
				alert("Sorry, Invalid file format." );
				document.forms[1].file02.value="";
				document.forms[1].file02.focus();
				return false;
			}
		}
		
	}	
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doSaveShortfall";
   	document.forms[1].action="/EmployeeShortFallAction.do";
   	JS_SecondSubmit=true;
   	document.forms[1].submit();
	}
}
function toggle(sortid)
{
	if(!JS_SecondSubmit)
	{
    document.forms[1].reset();
    document.forms[1].sortId.value=sortid;
    document.forms[1].mode.value="doClaimHistorySearch";
	document.forms[1].action="/PreAuthEmployeeAction.do";
	JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of toggle(sortid)

function isFutureDate(argDate){

	var dateArr=argDate.split("/");	
	var givenDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
	var currentDate = new Date();
	if(givenDate>currentDate){
	return true;
	}
	return false;
}