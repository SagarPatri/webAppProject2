<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<title>Al Koot Administrator</title>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile" errorPage="/ttk/common/error.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<head>
<title>Al Koot Administrator - List of Healthcare Companies</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9"/>
<script language="javascript" src="/ttk/scripts/utils.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/trackdatachanges.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/common/healthcarelayout.js"></SCRIPT>
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
<SCRIPT LANGUAGE="JavaScript">
var bAction = true;
ClientReset = true;
var JS_SecondSubmit=false;
</SCRIPT>
</head>

<body onLoad="javascript:resizeDocument();TrackFormData('webBoard');handleFocus();">
<logic:empty name="UserSecurityProfile">
	<script language="javascript">
			openFullScreenMode("/ttk/index.jsp?sessionexpired=true");
	</script>
</logic:empty>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td>
		<!-- S T A R T : Header Section-->
		<tiles:insert attribute="header"/>
	</td>
</tr>
	<td>
		<!-- S T A R T : Main Container Table -->
		<table width="100%" id="mainContainerTable" class="mainContainerTable" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="163" class="leftNavigationBand" nowrap valign="top">
			<!-- S T A R T : Left Navigation Menu -->
	    	<tiles:insert attribute="leftlinks"/>
		    </td>
		    <td width="100%"  valign="top" align="center">
			<!-- S T A R T : Tab Navigation -->
			<tiles:insert attribute="tablinks"/>
			<!-- S T A R T : Content Section-->
			<tiles:insert attribute="content"/>
		    </td>
		  </tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<!-- S T A R T : Footer Section -->
		<tiles:insert attribute="footer"/>
		<!-- E N D : Main Container Table -->
	</td>
<tr>
</table>
</body>