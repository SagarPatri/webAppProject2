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

</script>
</head>
<body>
<logic:empty name="UserSecurityProfile" scope="session">
	<script type="text/javascript">	
	openFullScreenMode("/ttk/onlineaccess.jsp?sessionexpired=true&loginType=HOS");
	</script>
	</logic:empty>


<form action="/PbmPharmacyGeneralAction.do" method="POST">
<logic:notEmpty name="providerRedirectPage" scope="request">
    <script type="text/javascript">  
    openFullScreenMode("/ttk/onlineforms/PBMPharmacyLogin/providerRelogin.jsp");
    </script>
    </logic:notEmpty>
   </form>
   </body>
</html>