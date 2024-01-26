function onLogin() {
	document.forms[0].mode.value = "doLogin";
}// end of onLogin()


function onChangePassword(strUserID) {
	var param = "&UserId=" + strUserID;
	openFullScreenMode(
			"/ChangepasswordAction.do?mode=doDefault" + param + "", '_self',
			"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350");
}// end of onChangePassword()


var submitted = false;
function SubmitTheForm() {
	var userid = document.forms[0].userid.value;
	var password = document.forms[0].password.value;

	if (userid == null || password == null || userid === "" || password === "" || userid.length < 1 || password.length < 1) {
		alert("User Id Or Password Should Not Left");
		return false;
	}
	if (submitted == true) {
		return;
	}
	document.forms[0].mode.value = "doLogin";
	// document.forms[0].submit();
	document.forms[0].mybutton.value = 'Please wait..';
	document.forms[0].mybutton.disabled = true;
	submitted = true;
}

function onUserLogout() {
	var userID = document.forms[0].userid;
	var pwd = document.forms[0].password;
	if (userID.value == null || userID.value == "") {
		alert("Please Enter User ID");
		userID.focus();
		return;
	}
	if (pwd.value == null || pwd.value == "") {
		alert("Please Enter Password");
		pwd.focus();
		return;
	}
	document.forms[0].mode.value = "doUserLogout";
	document.forms[0].submit();
}//end of onUserLogout()