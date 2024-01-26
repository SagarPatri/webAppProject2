//contains the javascript functions of member.jsp  

function onRootDependentsIcon(strRootIndex)
{
	document.forms[1].mode.value="doRenew";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/RenewMemberAction.do";
	document.forms[1].submit();
}//end of onRootDependentsIcon(strRootIndex)
function onECards()
{
   var openPage = "/OnlineMemberAction.do?mode=doECards";
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}//end of function onECards()
function onRootPhotoIcon(strRootIndex)
{
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of onRootPhotoIcon(strRootIndex)
function onRootCardIcon(strRootIndex)
{
	document.forms[1].mode.value="doApproveCard";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of onRootCardIcon(strRootIndex)
function onRootCancelledIcon(strRootIndex)
{
	var msg = confirm("Are you sure you want to cancel the selected record ?\nNo Pre-approval/Claim can be received for this member from the date of exit");
	if(msg)
	{
		var msg = confirm("Cancellation of a Principal member will automatically cancel the dependants also.\nPlease confirm before proceeding further");
		if(msg)
		{
    document.forms[1].mode.value="doEnrollCancel";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/CancelEnrollmentAction.do";
	document.forms[1].submit();
		}
	}
}//end of onRootCancelledIcon(strRootIndex)

function onNodeCancelledIcon(strRootIndex,strNodeIndex)
{
	var msg = confirm("Are you sure you want to cancel the selected record ?\nNo Pre-approval/Claim can be received for this member from the date of exit");
	if(msg)
	{
    document.forms[1].mode.value="doEnrollCancel";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
    document.forms[1].action="/CancelEnrollmentAction.do";
	document.forms[1].submit();
}
}

function onNodeClaimSubmission(strRootIndex,strNodeIndex){
	
	    document.forms[1].mode.value="doClaimSubmission";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
	    document.forms[1].action="/EmpClaimSubmissionAction.do";
		document.forms[1].submit();
	
}




function onRootAddIcon(strRootIndex)
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].action="/OnlineMemberDetailsAction.do";
	document.forms[1].submit();
}//end of onRootAddIcon(strRootIndex)





function onRootDeleteIcon(strRootIndex)
{
	var msg = confirm("Are you sure you want to delete the selected record ?");
	if(msg)
	{
		document.forms[1].mode.value="doDelete";
		document.forms[1].selectedroot.value=strRootIndex;
	    document.forms[1].action="/CorporateMemberAction.do";
		document.forms[1].submit();
	}//end of if(msg)
}//end of onRootDeleteIcon(strRootIndex)

function onNodePolicyIcon(strRootIndex,strNodeIndex)
{
	document.forms[1].mode.value="";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
    document.forms[1].action="/MemberRuleDataAction.do";
	document.forms[1].submit();
}//end of onNodePolicyIcon(strRootIndex,strNodeIndex)

//koc1352
function onNodeBrowseIcon(strRootIndex,strNodeIndex)
{
    document.forms[1].mode.value="doDefault";
   // document.forms[1].child.value="Browse";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
    document.forms[1].action="/FileUpload.do";
	document.forms[1].submit();
}

function onRootViewFiles(strRootIndex,strNodeIndex)
{
    document.forms[1].mode.value="doSearch";
 	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
    document.forms[1].action="/EmployeeUploadFileList.do";
	document.forms[1].submit();
}
//koc1352
function onNodeSummaryIcon(strRootIndex,strNodeIndex)
{
	document.forms[1].mode.value="doSummary";
	//document.forms[0].tab.value="Summary";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
    document.forms[1].action="/OnlineMemberAction.do";
	document.forms[1].submit();
}//end of onNodeCardIcon(strRootIndex,strNodeIndex)
function onNodeEditIcon(strRootIndex,strNodeIndex)
{
	document.forms[1].mode.value="doViewPolicy";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
    document.forms[1].action="/OnlinePolicyDetailsAction.do";
	document.forms[1].submit();
}//end of onNodeEditIcon(strRootIndex,strNodeIndex)




function onRooteCardIcon(strRootIndex,strNodeIndex)
{
	document.forms[1].mode.value="doGenerateEcard";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
	
    //document.forms[1].action="/OnlineMemberAction.do";
	//document.forms[1].submit();
	var param = "?mode=doGenerateEcard";
	
	var openPage = "/OnlineMemberAction.do"+param+"&selectedroot="+document.forms[1].selectedroot.value+"&selectednode="+document.forms[1].selectednode.value;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
}//end of onRooteCardIcon(strRootIndex,strNodeIndex)
function onNodeeCardIcon(strRootIndex,strNodeIndex)
{
	var msg = confirm("Are you sure you want to delete the selected record ?");
	if(msg)
	{
		document.forms[1].mode.value="doDelete";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
	    document.forms[1].action="/CorporateMemberAction.do";
		document.forms[1].submit();
	}//end of if(msg)
}//end of onNodeDeleteIcon(strRootIndex,strNodeIndex)
//function Search
function onSearch()
{
	if(!JS_SecondSubmit)
 	{
		JS_SecondSubmit=true;
		trimForm(document.forms[1]);
		document.forms[1].mode.value="doSearch";
		document.forms[1].selectedroot.value="";
	    document.forms[1].action="/OnlineMemberAction.do";
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onSearch()
function approveCard()
{
	document.forms[1].mode.value="doApproveCard";
	document.forms[1].selectedroot.value="";
    document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of approveCard()
function onAddNewEnrollment()
{
	document.forms[1].mode.value="doAdd";
	document.forms[1].selectedroot.value="";
     document.forms[1].action="/AddEnrollmentAction.do";
	document.forms[1].submit();
}//end of addNewEnrollment()
function editRoot(strRootIndex)
{	
	/*if(document.forms[1].loginType.value==='EMPL'){
		document.forms[1].mode.value="doDefaultPreAuth";
	    document.forms[1].action="/PreAuthEmployeeAction.do";
	}else{
		document.forms[1].mode.value="doViewEmpDetails";
	    document.forms[1].action="/AddEnrollmentAction.do";
	}*/
	if(document.forms[1].loginType.value!=='EMPL'){
		document.forms[1].mode.value="doViewEmpDetails";
	    document.forms[1].action="/AddEnrollmentAction.do";
	    document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].submit();
	}
	
}//end of editRoot(strRootIndex)

function OnShowhideClick(strRootIndex)
{
	document.forms[1].mode.value="doShowHide";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/OnlineMemberAction.do";
	document.forms[1].submit();
}//end of addNewEnrollment()
function onCancel(strRootIndex)
{
	document.forms[1].mode.value="doClose";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of onCancel
//function on click of pageIndex
function pageIndex(strPageIndex)
{
	document.forms[1].reset();
	document.forms[1].mode.value="doSearch";
	document.forms[1].selectedroot.value="";
	document.forms[1].pageId.value=strPageIndex;
    document.forms[1].action="/OnlineMemberAction.do";
	document.forms[1].submit();
}//end of pageIndex()
//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/OnlineMemberAction.do";
    document.forms[1].submit();
}//end of PressBackWard()
//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/OnlineMemberAction.do";
    document.forms[1].submit();
}//end of PressForward()

function editNode(strRootIndex,strNodeIndex)
{
	if(document.forms[0].sublink.value == "Enrollment")
	{
		document.forms[1].mode.value="doViewPolicy";
		document.forms[1].action="/OnlinePolicyDetailsAction.do";
	}
	else 
	{
		if(document.forms[1].loginType.value==='EMPL'){
			if(document.forms[1].activeSubLink.value==='Claim Submission'){
				document.forms[1].mode.value="doClaimSubmission";
			    document.forms[1].action="/EmployeeClaimSubmissionAction.do";
			}else if(document.forms[1].activeSubLink.value==='Pre-Approval'||document.forms[1].activeSubLink.value==='Claims History'){
				document.forms[1].mode.value="doDefaultPreAuth";
			    document.forms[1].action="/PreAuthEmployeeAction.do";
			}else if(document.forms[1].activeSubLink.value==='Home'){
				 document.forms[1].mode.value="viewEmpBenefitDetails";
				 document.forms[1].action="/OnlineMemberDetailsAction.do?benefitType4=OPT";  
			}
			
		}else{
			document.forms[1].mode.value="doViewMemberDetails";
		    document.forms[1].action="/OnlineHistoryAction.do";
		}
	}
	if(!JS_SecondSubmit)
	{
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
	JS_SecondSubmit=true;
    document.forms[1].submit();
	}
}//end of editNode(strRootIndex,strNodeIndex)
function onRootChangePassword(strRootIndex)
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/EmployeeChangePassword.do";
	document.forms[1].submit();
}//end of onRootChangePassword(strRootIndex)
function onRootSendConfirmation(strRootIndex)
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/PreAuthIntimationAction.do";
	document.forms[1].submit();
}//end of onRootDependentsIcon(strRootIndex)
//Added for IBM...KOC-1216
function onRootOPTOUT1(strRootIndex)
{
	document.forms[1].mode.value="doViewEmpDetails";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/AddEnrollmentAction.do";
	document.forms[1].submit();
}//end

function onNodeMedicalCertificateIcon(strRootIndex, strNodeIndex){
	var openPage = "/CorporateMemberAction.do?mode=doPrintCertificate&strRootIndex="+strRootIndex+"&strNodeIndex="+strNodeIndex;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
	}

/*function onRootDeleteIcon(strRootIndex)
{
	alert("Vishwa onRootDeleteIcon 111");
	var msg = confirm("Are you sure you want to cancel the selected record ?\nNo Pre-approval/Claim can be received for this member form the date of exit");
	if(msg)
	{
		document.forms[1].mode.value="doCancel";
		document.forms[1].selectedroot.value=strRootIndex;
	    document.forms[1].action="/AddEnrollmentAction.do";
		document.forms[1].submit();
	}//end of if(msg)
}//end of onRootCancelledIcon(strRootIndex)

function onNodeDeleteIcon(strRootIndex,strNodeIndex)
{
	alert("Vishwa onNodeDeleteIcon  222");
	var msg = confirm("Are you sure you want to cancel the selected record ?\nNo Pre-approval/Claim can be received for this member form the date of exit");
	if(msg)
	{
		document.forms[1].mode.value="doCancel";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
	    document.forms[1].action="/AddEnrollmentAction.do";
		document.forms[1].submit();
	}//end of if(msg)
}//end of onNodeCancelledIcon(strRootIndex,strNodeIndex)
*/
function onNodeEditIcon(strRootIndex)
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].action="/OnlineMemberDetailsAction.do";
	document.forms[1].submit();
}//end of onNodeEditIcon(strRootIndex)

function onEmplClose(){
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineMemberAction.do";
	document.forms[1].submit();
}

function goToHome(){
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/EmployeeHomeAction.do";
	document.forms[1].submit();
}

function onRootFamilyCardReplacement(strRootIndex)
{
	document.forms[1].mode.value="doDefaultCardReplacement";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].action="/OnlineMemberDetailsRepAction.do";
	document.forms[1].submit();
}

function onCloseWindow()
{
	if(!TrackChanges()) return false;
	document.forms[1].child.value="";
	document.forms[1].mode.value="doClose"; 
	document.forms[1].action="/OnlinePolicyDetailsAction.do";		
    document.forms[1].submit();
}

function onReset()
{
	document.forms[1].finalRemarks.value=""; 
	return false;
}


function onSubmitPrincipalRequest(){
	if(!JS_SecondSubmit)
	{
		document.forms[1].mode.value="doSubmitRequest";
		document.forms[1].action="/OnlineMemberSubmitPrincipalReqAction.do?type=PRN";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}
function onSubmitMemberRequest(){
	if(!JS_SecondSubmit)
	{
		document.forms[1].mode.value="doSubmitRequest";
		document.forms[1].action="/OnlineMemberSubmitMemberReqAction.do?type=MEM";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
}
function onNodeCardReplacement(strRootIndex, strNodeIndex) {
	if (!JS_SecondSubmit) {
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
		document.forms[1].mode.value = "doDependentCardReplacement";
		document.forms[1].action = "/OnlineMemberDetailsRepAction.do";
		JS_SecondSubmit = true;
		document.forms[1].submit();
	}
}