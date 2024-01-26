<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<title>AlKoot Administrator</title>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile" errorPage="/ttk/common/error.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<head>
<title>AlKoot Administrator - List of Companies</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" src="/ttk/scripts/utils.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/trackdatachanges.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/common/healthcarelayout.js"></SCRIPT>
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
<link href="/ttk/styles/OnlineDefault.css" media="screen" rel="stylesheet"></link>
<style type="text/css">
#pbmFooter{
       border-top: 1px solid rgb(255, 47, 76);
       border-collapse: collapse;
       width: 100%;
       height: 20px;
       text-align: center;
       color: brown;
       font-size: 15px;
       background-color: rgb(213, 243, 223);
            }
</style>
<SCRIPT LANGUAGE="JavaScript">
var bAction = true;
ClientReset = true;
var JS_SecondSubmit=false;
</SCRIPT>
<script language="JavaScript" type="text/javascript">
function disableCtrlModifer(evt)
{
	var disabled = {a:0, x:0,w:0, n:0, F4:0, j:0};
	var ctrlMod = (window.event)? window.event.ctrlKey : evt.ctrlKey;
	var key = (window.event)? window.event.keyCode : evt.which;
	key = String.fromCharCode(key).toLowerCase();
	return (ctrlMod && (key in disabled))? false : true;
	
}//end of disableCtrlModifer(evt)

//RIGHT CLICK DISABLE cODE
/* document.onmousedown=rightClickDisable;
var status="This Operation Is  Disabled ! ! !";

function rightClickDisable(e)
{
 if(event.button==2)
  {
    alert(status);
    return false;
  }
} */ 

/* var myClock = document.getElementById("clock");
function renderTime () {
    var currentTime = new Date();
    var h = currentTime.getHours();
    var m = currentTime.getMinutes();
    var s = currentTime.getSeconds();

    if (h < 10) {
        h = "0" + h;
    }


    if (m < 10) {
        m = "0" + m;
    }


    if (s < 10) {
        s = "0" + s;
    }

    //myClock.textContent = h + ":" + m + ":" + s;
    myClock.innerText = h + ":" + m + ":" + s;

    setTimeout(renderTime, 1000);
}
renderTime(); */
</script>

</head>
<!-- Changes Added onunload in body for Password Policy CR KOC 1235 -->
<body onunload="javascript:logoutwindow(event);" onLoad="javascript:onlineresizeDocument();TrackFormData('webBoard');handleFocus();window.history.forward(1);" onkeypress="return disableCtrlModifer(event);" onkeydown="return disableCtrlModifer(event);">

<logic:empty name="UserSecurityProfile">
	<script language="javascript">
			openFullScreenMode("/ttk/index.jsp?sessionexpired=true");
	</script>
</logic:empty>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
		<!-- S T A R T : Header Section-->
		<td>
		<tiles:insert attribute="header"/>
	</td>
</tr>
<tr>
	<td>
		<!-- S T A R T : Main Container Table -->
		<table width="100%" id="mainContainerTable" class="mainContainerTable" border="0" cellspacing="0" cellpadding="0">
		  <tr>
<!-- start Modifications as per Multiple Browser  -->
		    <td width="100%"  valign="top">


			<!-- S T A R T : Tab Navigation -->
			<tiles:insert attribute="provLeftLinks"/>
			<!-- S T A R T : Content Section-->
			
<!--  <div class="contentArea" id="contentArea">-->
			<tiles:insert attribute="content"/>
<!--  </div>-->
		    </td>
		  </tr>
		</table>
	</td>
</tr>
<tr>
    <td id="pbmFooter">&COPY;Al Koot&nbsp;2017</td>
</tr>
<%-- <tr>
	<td>
		<!-- S T A R T : Footer Section -->
		<tiles:insert attribute="footer"/>
		<!-- E N D : Main Container Table -->
	</td>
</tr> --%>
</table>
</body>