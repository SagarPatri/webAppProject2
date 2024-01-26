<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
    <%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Please Wait Redirecting....</title>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/utils.js"></SCRIPT>
<script type="text/javascript">

function funSetTimeOut(prMode){
	if("HOS"===prMode){
	setTimeout(funExecuteHOS, 1000);
	}
	if("PHR"===prMode){
		setTimeout(funExecutePHR, 1000);
		}
}

function funExecuteHOS(){
	var pageUrl= document.forms[0].pageURL.value;
	openWebFullScreenMode(pageUrl);
}
function funExecutePHR(){
	var pageUrl= document.forms[0].pageURL.value;
	openWebFullScreenModeWithScrollbar(pageUrl);
}
</script>
</head>
<body>
<logic:empty name="UserSecurityProfile" scope="session">
	<script type="text/javascript">	
	openFullScreenMode("/ttk/onlineaccess.jsp?sessionexpired=true&loginType=HOS");
	</script>
	</logic:empty>

<h2>Please Wait Redirecting....</h2>



<form action="/PbmPharmacyGeneralAction.do" method="POST">
<logic:notEmpty name="providerLoginRedirectingFlag" scope="session">
   <logic:equal name="providerLoginRedirectingFlag" value="HOS" scope="session">
   <script type="text/javascript">
   
   funSetTimeOut('HOS');
    
    </script>
   </logic:equal>
   <logic:equal name="providerLoginRedirectingFlag" value="PHR" scope="session">
   <script type="text/javascript">
   funSetTimeOut('PHR');
   
   </script>
   </logic:equal>
   
   <input type="hidden" name="pageURL" value='<bean:write scope="session" name="reOpenPage"/>'>
   </logic:notEmpty>
   </form>
   </body>
</html>