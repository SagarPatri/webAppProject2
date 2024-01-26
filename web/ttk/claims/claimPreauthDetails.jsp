<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
<%@ page
	import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,com.ttk.dto.usermanagement.UserSecurityProfile"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<head>


<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script language="javascript" src="/ttk/scripts/jquery.autocomplete.js"></script>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral.js"></script>
<script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral-async.js"></script>
	
	
<script>  

var  popupWindow=null;
function onClose() {
	  popupWindow=  window.close();
	
}



</script>	
	
	
</head>
	
	 <div overflow: scroll >
	<body style="text-align: center; font-size: 8px;">
	<html:form action="/PreAuthHistoryAction.do">
	
	
	<ttk:ClaimPreAuthHistoryDetails/>
	
	
	
	
	
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	
	<tr></tr><tr></tr>
	<tr></tr><tr></tr>
 	 
	    </table>
	
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	
	<tr></tr><tr></tr>
 	 <tr>
	    <td width="27%"> </td>
	    <td width="73%" nowrap align="right">
	     <!--  <button type="submit"  name="Button1" accesskey="n" class="buttons" onMouseout="this.className='buttons'" onClick="javascript:generateDownloadHistoryDetailPATCLM();" onMouseover="this.className='buttons buttonsHover'">Dow<u>n</u>load to Excel</button> -->

       &nbsp;&nbsp;
	    <button type="submit"  name="Button2" accesskey="c" class="buttons" onClick="javascript:onClose();" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
	    </td>
	    </tr>
	    </table>
	





</html:form>

</body>
	</div>



		