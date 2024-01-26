function blinkIt() {
	if (!document.all)
		return;
	else {
		for (var i = 0; i < document.all.tags('blink').length; i++) {
			s = document.all.tags('blink')[i];
			s.style.visibility = (s.style.visibility == 'visible') ? 'hidden': 'visible';
		}
	}
}


function V2Login(OpenPage) {
	var selLogin = document.forms[0].loginType.value;
	var subLogin = document.forms[0].subLoginType.value;

	if (selLogin === "HOS") {
		openWebFullScreenMode(OpenPage);
	} else {
		openWebFullScreenMode(OpenPage);
	}
	if (selLogin != "OCO" || selLogin != "EMPL") {
		if (selLogin == "IBMOCO" || selLogin == "IBMOHR") {
			openWebFullScreenModeYOO(OpenPage);
		} else {
			openWebFullScreenMode(OpenPage);
		}
	} else {
		var grpid = document.forms[0].groupId.value;
		if (grpid == "Y0063" || grpid == "Y040" || grpid == "Y0073" || grpid == "I310" || grpid == "W074" || grpid == "C0717") {
			openWebFullScreenModeYOO(OpenPage);
		} else {
			openWebFullScreenMode(OpenPage);
		}
	}
	if (selLogin === "PTR") {
		if (subLogin === "PRH") {
			openWebFullScreenModeWithScrollbar(OpenPage);
		} else {
			openWebFullScreenMode(OpenPage);
		}
	} else {
		openWebFullScreenMode(OpenPage);
	}
}


function onLogin() {
	trimForm(document.forms[0]);
	var loginTypeID = document.forms[0].loginType.value;
	// added as per IBM
	if (loginTypeID == 'IBMOCO') {
		loginTypeID = 'OCO';
	}
	if (loginTypeID == 'IBMOHR') {
		loginTypeID = 'OHR';
	}
	if (loginTypeID == 'OIN' || loginTypeID == 'OCO' || loginTypeID == 'OHR'
			|| loginTypeID == 'OIU' || loginTypeID == 'OCI'
			|| loginTypeID == 'HOS' || loginTypeID == 'OBR'
			|| loginTypeID == 'PTR' || loginTypeID == 'EMPL') {
		document.forms[0].mode.value = "doLogin";
	}// end of if(loginTypeID =='OIN' && loginTypeID =='OCO' && loginTypeID  =='OHR' || loginTypeID =='OIU' || loginTypeID =='OCI')
	else {
		alert("Login Type is Required");
		return false;
	}// end of else
}// end of onLogin()


function onClose() {
	document.forms[0].logmode.value = "Close";
	document.forms[0].action = "/CloseOnlineAccessAction.do";
	document.forms[0].submit();

}// end of onClose()
function onExcel() {
	document.forms[0].mode.value = "getStreamInfo";
	document.forms[0].action = "/CitiDownloadAction.do";
}


function onForgotPassword() {
	var param3 = "";
	var param1 = "";
	var loginType = document.forms[0].loginType.value;
	var param = "";

	if (loginType === 'OIU') {
		if (document.forms[0].insUserId.value === "") {
			alert("Please enter Insurance User ID");
			document.forms[0].insUserId.focus();
			return false;
		}// end of if(document.forms[0].insUserId.value == "")
		param = "&UserId=" + document.forms[0].insUserId.value + "&LoginType=" + loginType;
	} else if (loginType === 'OBR') {
		if (document.forms[0].broUserId.value === "") {
			alert("Please enter Broker User ID");
			document.forms[0].broUserId.focus();
			return false;
		}// end of if(document.forms[0].broUserId.value == "")	
		param = "&UserId=" + document.forms[0].broUserId.value + "&LoginType=" + loginType;
	} else if (loginType === 'HOS') {
		if (document.forms[0].hosEmpaneId.value === "") {
			alert("Please enter Empanelment ID");
			document.forms[0].hosEmpaneId.focus();
			return false;
		}
		if (document.forms[0].hosUserId.value === "") {
			alert("Please enter User ID");
			document.forms[0].hosEmpaneId.focus();
			return false;
		}
		if (document.forms[0].hosUserId.value === "") {
			alert("Please enter User ID");
			document.forms[0].hosUserId.focus();
			return false;
		}// end of if(document.forms[0].broUserId.value == "")	
		param = "&UserId=" + document.forms[0].hosUserId.value + "&LoginType="
						+ loginType + "&empanelID=" + document.forms[0].hosEmpaneId.value;
	}else if (loginType === 'PTR') {
		if (document.forms[0].ptrEmpaneId.value === "") {
			alert("Please enter Partner ID");
			document.forms[0].ptrEmpaneId.focus();
			return false;
		}
		if (document.forms[0].ptrUserId.value === "") {
			alert("Please enter User ID");
			document.forms[0].ptrEmpaneId.focus();
			return false;
		}
		if (document.forms[0].ptrUserId.value === "") {
			alert("Please enter User ID");
			document.forms[0].ptrUserId.focus();
			return false;
		}// end of if(document.forms[0].broUserId.value == "")
		param = "&UserId=" + document.forms[0].ptrUserId.value + "&LoginType="
						+ loginType + "&empanelID=" + document.forms[0].ptrEmpaneId.value;
	}else if (loginType === 'OCO') {
		if (document.forms[0].groupId.value == "") {
			alert("Please enter GroupId");
			document.forms[0].groupId.focus();
			return false;
		}// end of if(document.forms[0].groupId.value == "")
		if (document.forms[0].corpPolicyNo.value == "") {
			alert("Please enter PolicyNo.");
			document.forms[0].corpPolicyNo.focus();
			return false;
		}// end of if(document.forms[0].corpPolicyNo.value == "")
		if (document.forms[0].userId.value == "") {
			alert("Please enter UserID");
			document.forms[0].userId.focus();
			return false;
		}// end of if(document.forms[0].userId.value == "")	
		param = "&UserId=" + document.forms[0].userId.value + "&LoginType="
				+ document.forms[0].loginType.value + "&GroupID="
				+ document.forms[0].groupId.value + "&PolicyNo="
				+ document.forms[0].corpPolicyNo.value;
	}// end of if(loginType=='OCO')
	else if (loginType === 'EMPL') {
		if (document.forms[0].userId.value == "") {
			alert("Please enter UserID");
			document.forms[0].userId.focus();
			return false;
		}// end of if(document.forms[0].userId.value == "")
		param = "&UserId=" + document.forms[0].userId.value + "&LoginType="
				+ document.forms[0].loginType.value;
	} else if (loginType === 'OHR') {
		if (document.forms[0].hrGroupId.value === "") {
			alert("Please enter group ID");
			document.forms[0].hrGroupId.focus();
			return false;
		}
		if (document.forms[0].hrUserId.value === "") {
			alert("Please enter User ID");
			document.forms[0].hrUserId.focus();
			return false;
		}// end of if(document.forms[0].broUserId.value == "")
		param = "&UserId=" + document.forms[0].hrUserId.value + "&LoginType="
				+ document.forms[0].loginType.value + "&GroupID="
				+ document.forms[0].hrGroupId.value;
	}
	openFullScreenMode(
			"/ForgotPasswordAction.do?mode=doDefault" + param + "",
			'_self',
			"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
}// end of onForgotPassword()


function onForgotPassword1() {
	var param3 = "";
	var param1 = "";
	var loginType = document.forms[0].loginType.value;
	if (loginType == 'HOS') {
		if (document.forms[0].hosUserId.value == "") {
			alert("Please enter User Id.");
			document.forms[0].hosUserId.focus();
			return false;
		}// end of if(document.forms[0].corpPolicyNo.value == "")
		param1 = "&GroupID=" + document.forms[0].hosUserId.value;
		param2 = "&PolicyNo=" + document.forms[0].hosUserId.value;
		param3 = "&UserId=" + document.forms[0].hosUserId.value;
		param4 = "&LoginType=" + document.forms[0].loginType.value;

	}// end of if(loginType=='OCO')

	if (loginType == 'PTR') {

		if (document.forms[0].ptrUserId.value == "") {
			alert("Please enter User Id.");
			document.forms[0].ptrUserId.focus();
			return false;
		}// end of if(document.forms[0].corpPolicyNo.value == "")
		param1 = "&GroupID=" + document.forms[0].ptrUserId.value;
		param2 = "&PolicyNo=" + document.forms[0].ptrUserId.value;
		param3 = "&UserId=" + document.forms[0].ptrUserId.value;
		param4 = "&LoginType=" + document.forms[0].loginType.value;
	}// end of if(loginType=='OCO')
	var param = param1 + param2 + param3 + param4;
	openFullScreenMode(
			"/ForgotPasswordAction.do?mode=doDefault" + param + "",
			'_self',
			"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
}// end of onForgotPassword1()

function onChangePassword() {
	var loginType = document.forms[0].loginType.value;
	if (loginType == 'OCO' || loginType == 'IBMOCO') {
		if (document.forms[0].groupId.value == "") {
			alert("Please enter GroupId");
			document.forms[0].groupId.focus();
			return false;
		}// end of if(document.forms[0].groupId.value == "")
		if (document.forms[0].corpPolicyNo.value == "") {
			alert("Please enter PolicyNo.");
			document.forms[0].corpPolicyNo.focus();
			return false;
		}// end of if(document.forms[0].corpPolicyNo.value == "")
		if (document.forms[0].userId.value == "") {
			alert("Please enter UserID");
			document.forms[0].userId.focus();
			return false;
		}// end of if(document.forms[0].userId.value == "")
		var check = document.forms[0].groupId.value;
		var param1 = "&GroupID=" + document.forms[0].groupId.value;
		if (check == "A0912") {
			var param2 = "&PolicyNo=" + document.forms[0].corpPolicyNo1.value;
		} else {
			var param2 = "&PolicyNo=" + document.forms[0].corpPolicyNo.value;
		}
		var param3 = "&UserId=" + document.forms[0].userId.value;
		var param4 = "&LoginType=" + loginType;
		var param = param1 + param2 + param3 + param4;
		if (loginType == "IBMOCO" || check == "C0717") {
			openWebFullScreenModeYOO(
					"/OnlineChangePasswordAction.do?mode=doDefault" + param
							+ "",
					'_self',
					"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		} else {
			openFullScreenMode(
					"/OnlineChangePasswordAction.do?mode=doDefault" + param
							+ "",
					'_self',
					"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		}
	}// end of if(loginType=='OCO' || loginType=='IBMOCO')

	if (loginType == 'EMPL') {

		if (document.forms[0].userId.value == "") {
			alert("Please enter Alkoot Id");
			document.forms[0].userId.focus();
			return false;
		}// end of if(document.forms[0].userId.value == "")
		var param3 = "&UserId=" + document.forms[0].userId.value;
		var param4 = "&LoginType=" + loginType;
		var param = param3 + param4;

		openFullScreenMode(
				"/OnlineChangePasswordAction.do?mode=doDefault" + param + "",
				'_self',
				"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");

	}
	if (loginType == 'OHR' || loginType == 'IBMOHR') {
		// alert("dsdsdsds::"+document.getElementById("test111").value);
		// alert("dsdsdsds::"+document.forms[0].hrGroupId.value);
		var param1 = "&HRGroupID=" + document.forms[0].hrGroupId.value;
		var param2 = "&HRUserId=" + document.forms[0].hrUserId.value;
		var param3 = "&LoginType=" + loginType;
		var param = param1 + param2 + param3;

		if (loginType == "IBMOHR") {
			openWebFullScreenModeYOO(
					"/OnlineChangePasswordAction.do?mode=doDefault" + param
							+ "",
					'_self',
					"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		} else {

			openFullScreenMode(
					"/OnlineChangePasswordAction.do?mode=doDefault" + param
							+ "",
					'_self',
					"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		}
	}// end of if(loginType=='OHR' || loginType=='IBMOHR')
	if (loginType == 'HOS') {
		// var param1="&HosEmpanelId="+document.forms[0].hosEmpaneId.value;
		var param2 = "&HosUserId=" + document.forms[0].hosUserId.value;
		var param3 = "&LoginType=" + loginType;
		// var param = param1+param2+param3;
		var param = param2 + param3;
		openFullScreenMode(
				"/OnlineChangePasswordAction.do?mode=doDefault" + param + "",
				'_self',
				"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");

	}// end of if(loginType=='OHR' || loginType=='IBMOHR')
	if (loginType == 'PTR') {
		var param2 = "&PtrUserId=" + document.forms[0].ptrUserId.value;
		var param3 = "&LoginType=" + loginType;
		var param = param2 + param3;
		openFullScreenMode(
				"/OnlineChangePasswordAction.do?mode=doDefault" + param + "",
				'_self',
				"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
	}// end of if(loginType=='PTR')

	if (loginType == 'OIU') {
		var param1 = "&InsCompCode=" + document.forms[0].insCompCode.value;
		var param2 = "&InsUserId=" + document.forms[0].insUserId.value;
		var param3 = "&LoginType=" + loginType;
		var param = param1 + param2 + param3;
		openFullScreenMode(
				"/OnlineChangePasswordAction.do?mode=doDefault" + param + "",
				'_self',
				"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
	}// end of if(loginType=='OIU')
	// kocb
	if (loginType == 'OBR') {
		var param1 = "&BroUserId=" + document.forms[0].broUserId.value;

		var param3 = "&LoginType=" + loginType;
		var param = param1 + param3;
		openFullScreenMode(
				"/OnlineChangePasswordAction.do?mode=doDefault" + param + "",
				'_self',
				"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
	}// end of if(loginType=='OBR')
	// Added for partner Login

}// end of onChangePassword()

// For Production the below code should be uncommented
function onBlcUser() {
	var loginType = document.forms[0].loginType.value;
	if (document.forms[0].loginType.value == "") {
		alert("Please select Login Type");
		return false;
	}// end of if(document.forms[0].loginType.value=="")
	else {
		if (loginType == "OIN") {
			openFullScreenMode(
					"https://v1ttk.vidalhealthtpa.com/corp_signin.asp?ltype=IndPol",
					'_self',
					"scrollbars=1,status=0,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		}// end of if(loginType=="OIN")
		else if (loginType == "OCO" || loginType == "EMPL") {
			openFullScreenMode(
					"https://v1ttk.vidalhealthtpa.com/corp_signin.asp?ltype=CompLog&lsubtype=EmpLog",
					'_self',
					"scrollbars=1,status=0,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		}// end of else if(loginType=="OCO")
		else if (loginType == "OHR") {
			openFullScreenMode(
					"https://v1ttk.vidalhealthtpa.com/corp_signin.asp?ltype=CompLog",
					'_self',
					"scrollbars=1,status=0,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		}// end of else if(loginType=="OHR")
		else if (loginType == "OIU") {
			openFullScreenMode(
					"https://v1ttk.vidalhealthtpa.com/signin.asp?ltype=InsLog",
					'_self',
					"scrollbars=1,status=0,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
		}// end of else if(loginType=="OIU")
	}// end of else
}// end of onBlcUser()

function onIBMUser() {
	openFullScreenMode(
			"https://weblogin.vidalhealthtpa.com/index.htm",
			'_self',
			"scrollbars=1,status=0,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
}// end of onIBMUser()

function onDisable(val) {
	if (val == 'Y') {
		document.getElementById("enrollmentId").disabled = true;
		document.getElementById("enrollmentId").className = "textBoxWeblogin textBoxMediumWeblogin textBoxWebloginDisabled";
		document.getElementById("indPolicyNo").disabled = false;
		document.getElementById("indPolicyNo").className = "textBoxWeblogin textBoxMediumWeblogin";
		document.getElementById("enrollmentId").value = "";
	}// end of if(val=='Y')
	else {
		document.getElementById("indPolicyNo").disabled = true;
		document.getElementById("indPolicyNo").className = "textBoxWeblogin textBoxMediumWeblogin textBoxWebloginDisabled";
		document.getElementById("enrollmentId").disabled = false;
		document.getElementById("enrollmentId").className = "textBoxWeblogin textBoxMediumWeblogin";
		document.getElementById("indPolicyNo").value = "";
	}// end of else
}// end of onDisable(val)


var submitted = false;
function SubmitTheForm() {
	if (submitted == true) {
		return;
	}
	document.forms[0].mybutton.value = 'Please wait..';
	document.forms[0].mybutton.disabled = true;
	submitted = true;
	document.forms[0].mode.value = "doLogin";
	document.forms[0].submit();
}

