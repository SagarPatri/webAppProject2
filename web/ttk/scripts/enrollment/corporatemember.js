//contains the javascript functions of member.jsp
function onNodeCompareIcon(strRootIndex,strNodeIndex)
{
	document.forms[1].mode.value="Policy";
	document.forms[1].selectedroot.value=strRootIndex;
	document.forms[1].selectednode.value=strNodeIndex;
    document.forms[1].action="/OtherPolicyAction.do";
	document.forms[1].submit();
}

function onRootDependentsIcon(strRootIndex)
{
	document.forms[1].mode.value="doSearch";
	document.forms[1].child.value="Renew Members";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/RenewMemberAction.do";
	document.forms[1].submit();
}//end of onRootDependentsIcon(strRootIndex)

function onRootDomiciliaryIcon(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doViewDomiciliary";
		document.forms[1].child.value="Domiciliary Treatment";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].action="/DomiciliaryAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onRootDependentsIcon(strRootIndex)

/*function onRootPolicyIcon(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doDefault";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].action="/EnrollmentRuleDataAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}*///end of onRootPolicyIcon(strRootIndex)

//FAMILY RULES UPDATED AS ADD MDF REF CR NO. 616
function onRootPolicyIcon(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doAddMDF";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].action="/AddMdfMemberAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}
function onRootPhotoIcon(strRootIndex)
{
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of onRootPhotoIcon(strRootIndex)

function onRootCardIcon(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doApproveCard";
		document.forms[1].selectedroot.value=strRootIndex;
		var policy_status =	 document.getElementById("policystatus").value;
		if(policy_status == "FTS")
			document.forms[1].action="/CorporateMemberAction.do";
		else if(policy_status == "RTS")
			document.forms[1].action="/CorporateRenewMemberAction.do"; 
			JS_SecondSubmit=true;
			document.forms[1].submit();
	 }				
}//end of onRootCardIcon(strRootIndex)

function onRootRatesIcon(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doViewPremiumInfo";
		document.forms[1].child.value="Premium Information";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].action="/PremiumInfoAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onRootRatesIcon(strRootIndex)

function onRootAddIcon(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doAddMember";
		document.forms[1].child.value="AddEditMember";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].action="/CorporateMemberAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onRootAddIcon(strRootIndex)

function onRootDeleteIcon(strRootIndex)
{
	var msg = confirm("Are you sure you want to delete the selected record ?");
	if(msg)
	{
		if(!JS_SecondSubmit)
		 {
			document.forms[1].mode.value="doDelete";
			document.forms[1].selectedroot.value=strRootIndex;
			var policy_status =	 document.getElementById("policystatus").value;
			if(policy_status == "FTS")
				document.forms[1].action="/CorporateMemberAction.do";
			else if(policy_status == "RTS")
				document.forms[1].action="/CorporateRenewMemberAction.do"; 
			JS_SecondSubmit=true;
			document.forms[1].submit();
		 }
	}//end of if(msg)
}//end of onRootDeleteIcon(strRootIndex)

function onRootCancelledIcon(strRootIndex)
{
	var msg = confirm("Are you sure you want to cancel the selected record ?");
	if(msg)
	{
		document.forms[1].mode.value="doCancel";
		document.forms[1].selectedroot.value=strRootIndex;
	    document.forms[1].action="/CorporateMemberAction.do";
		document.forms[1].submit();
	}//end of if(msg)
}//end of onRootCancelledIcon(strRootIndex)

function onNodeCancelledIcon(strRootIndex,strNodeIndex)
{
	var msg = confirm("Are you sure you want to cancel the selected record ?");
	if(msg)
	{
		document.forms[1].mode.value="doCancel";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
	    document.forms[1].action="/CorporateMemberAction.do";
		document.forms[1].submit();
	}//end of if(msg)
}//end of onNodeCancelledIcon(strRootIndex,strNodeIndex)

function onNodePolicyIcon(strRootIndex,strNodeIndex)
{
	/*if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doDefault";
		document.forms[1].child.value="Member Rules";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
		document.forms[1].action="/MemberRuleDataAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }*/
	//alert("page");
	var openPage = "/AddMdfMemberAction.do?mode=doViewMemberMdfFile&selectedroot="+strRootIndex+"&selectednode="+strNodeIndex;
	var w = screen.availWidth - 10;
  	var h = screen.availHeight - 49;
  	var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   	window.open(openPage,'',features);
	
}//end of onNodePolicyIcon(strRootIndex,strNodeIndex)

function onNodePedIcon(strRootIndex,strNodeIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doSearchPED";
		document.forms[1].child.value="Add PED";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
		document.forms[1].action="/AddPEDAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onNodePedIcon(strRootIndex,strNodeIndex)

function onNodeCardIcon(strRootIndex,strNodeIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doApproveCard";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
		var policy_status =	 document.getElementById("policystatus").value;
		if(policy_status == "FTS")
			document.forms[1].action="/CorporateMemberAction.do";
		else if(policy_status == "RTS")
			document.forms[1].action="/CorporateRenewMemberAction.do"; 
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onNodeCardIcon(strRootIndex,strNodeIndex)

function onNodeEditIcon(strRootIndex,strNodeIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doViewMember";
		document.forms[1].child.value="AddEditMember";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
		var policy_status =	 document.getElementById("policystatus").value;
		if(policy_status == "FTS")
			document.forms[1].action="/CorporateMemberAction.do";
		else if(policy_status == "RTS")
			document.forms[1].action="/CorporateRenewMemberAction.do"; 
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onNodeEditIcon(strRootIndex,strNodeIndex)

function onNodeDeleteIcon(strRootIndex,strNodeIndex)
{
	var msg = confirm("Are you sure you want to delete the selected record ?");
	if(msg)
	{
		document.forms[1].mode.value="doDelete";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
		var policy_status =	 document.getElementById("policystatus").value;
		if(policy_status == "FTS")
			document.forms[1].action="/CorporateMemberAction.do";
		else if(policy_status == "RTS")
			document.forms[1].action="/CorporateRenewMemberAction.do";  
		document.forms[1].submit();
	}//end of if(msg)
}//end of onNodeDeleteIcon(strRootIndex,strNodeIndex)
function onWebboardSelect()
{
	document.forms[1].mode.value="webboardselect";
	document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of onWebboardSelect()
//function Search
function onSearch()
{
	if(!JS_SecondSubmit)
	 {
		trimForm(document.forms[1]);
		document.forms[1].mode.value="doSearch";
		document.forms[1].selectedroot.value="";
	    document.forms[1].action="/CorporateMemberAction.do";
		JS_SecondSubmit=true;
	    document.forms[1].submit();
	 }//end of if(!JS_SecondSubmit)
}//end of onSearch()

function approveCard()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doApproveCard";
		document.forms[1].selectedroot.value="";
		var policy_status =	 document.getElementById("policystatus").value;
		if(policy_status == "FTS")
			document.forms[1].action="/CorporateMemberAction.do";
		else if(policy_status == "RTS")
			document.forms[1].action="/CorporateRenewMemberAction.do"; 
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of approveCard()

function addNewEnrollment()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doAdd";
		document.forms[1].child.value="Employee Details";
		document.forms[1].selectedroot.value="";
		document.forms[1].action="/EnrollmentAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of addNewEnrollment()

function editRoot(strRootIndex)
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doViewEnrollment";
		document.forms[1].child.value="Employee Details";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].action="/EnrollmentAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of editRoot(strRootIndex)

function OnShowhideClick(strRootIndex)
{
	document.forms[1].mode.value="doShowHide";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of addNewEnrollment()

function onCancel(strRootIndex)
{
	document.forms[1].mode.value="Cancel";
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
    document.forms[1].action="/CorporateMemberAction.do";
	document.forms[1].submit();
}//end of pageIndex()

//function to display previous set of pages
function PressBackWard()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doBackward";
    document.forms[1].action = "/CorporateMemberAction.do";
    document.forms[1].submit();
}//end of PressBackWard()

//function to display next set of pages
function PressForward()
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doForward";
    document.forms[1].action = "/CorporateMemberAction.do";
    document.forms[1].submit();
}//end of PressForward()

function onRootUncheckedIcon(strRootIndex)
{
    document.forms[1].reset();
    document.forms[1].mode.value ="doValidateRule";
    document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action = "/CorporateMemberAction.do";
    document.forms[1].submit();
}//end of PressForward()

function onRootPassedIcon(strRootIndex)
{
	
}//end of onRootPassedIcon(strRootIndex)


function onRootFailedIcon(strRootIndex)
{
	
	document.forms[1].reset();
    document.forms[1].mode.value ="doValidateRule";
    document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action = "/MemberAction.do";
    document.forms[1].submit();
}//end of onRootFailedIcon(strRootIndex)

function onShowError()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value ="doDefault";
		document.forms[1].action = "/ShowValidationError.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onShowError()

function onClearRules()
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value ="doClearRules";
		document.forms[1].action = "/MemberAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onClearRules()

function onRootChangePassword(strRootIndex)
{
	document.forms[1].mode.value="doDefault";
	document.forms[1].selectedroot.value=strRootIndex;
    document.forms[1].action="/ChangePasswordAction.do";
	document.forms[1].submit();
}//end of onRootHistoryIcon(strRootIndex)

//added for koc 1278 and 1270
function onRootChangePlanIcon(strRootIndex)

{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].mode.value="doViewChangePlan";
		document.forms[1].child.value="Change Plan";
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].action="/ChangePlanAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}//end of onRootDependentsIcon(strRootIndex)
//added for koc 1278 and 1270

function onUploadProfessionals()
{
	var userSeqId	=	document.forms[1].userSeqId.value;
	var EnrollmentUploadURL	=	document.forms[1].EnrollmentUploadURL.value;
	window.open(EnrollmentUploadURL+"?hospSid="+hospSeqId+"&uploadedBy="+userSeqId+"&empanelmentId="+hospEmpnlNo,'',"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
}




function softcopyUpload()
{
	
	var EnrollmentSoftUploadURL	=	document.forms[1].EnrollmentSoftUploadURL.value;
	var userSeqId	=	document.forms[1].userSeqId.value;

	var policy_num = document.forms[1].policy_num.value;
	var productTyp_num = document.forms[1].productTyp_num.value;
	var insComp_num = document.forms[1].insComp_num.value;
	var grpId_num = document.forms[1].grpId_num.value;

 var param = { 'loginType' : 'Enrollment', 'userId': userSeqId ,'password' :'m','policy_num': policy_num,'productTyp_num':productTyp_num,'insComp_num':insComp_num,'grpId_num':grpId_num};
 OpenWindowWithPost(EnrollmentSoftUploadURL, "width=1000, height=600, left=100, top=100, resizable=yes, scrollbars=yes", "NewFile", param);
}
 
 
function OpenWindowWithPost(url, windowoption, name, params)
{
 var form = document.createElement("form");
 form.setAttribute("method", "post");
 form.setAttribute("action", url);
 form.setAttribute("target", name);
 for (var i in params)
 {
   if (params.hasOwnProperty(i))
   {
     var input = document.createElement('input');
     input.type = 'hidden';
     input.name = i;
     input.value = params[i];
     form.appendChild(input);
   }
 }
 document.body.appendChild(form);
// window.open("", name, windowoption);
 form.submit();
 document.body.removeChild(form);
}

function onNodeMedicalCertificateIcon(strRootIndex, strNodeIndex){
	var openPage = "/CorporateMemberAction.do?mode=doPrintCertificate&strRootIndex="+strRootIndex+"&strNodeIndex="+strNodeIndex;
	var w = screen.availWidth - 10;
	var h = screen.availHeight - 99;
	var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	window.open(openPage,'',features);
	}

function onNodeMemberPolicyMapping(strRootIndex,strNodeIndex)					
{
	if(!JS_SecondSubmit)
	 {
		document.forms[1].selectedroot.value=strRootIndex;
		document.forms[1].selectednode.value=strNodeIndex;
		document.forms[1].action="/ViewMemberPolicyAction.do?";
		document.forms[1].mode.value="doViewMemberPolicyDetails";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	 }
}
