<!DOCTYPE HTML>
<%@page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.ttk.dto.usermanagement.UserListVO"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon" %>

<script language="JavaScript" src="/ttk/scripts/validation.js"></script>
<script language="javascript" src="/ttk/scripts/onlineforms/insuranceLogin/corporate.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<head>
<script type="text/javascript">
function onBack()
{
	document.forms[1].mode.value="doDashBoard";
   	document.forms[1].action="/InsuranceDashBoardAction.do";
   	document.forms[1].submit();
}
</script>
</head>

<html:form action="/InsuranceCorporateAction.do" >
<div class="contentArea" id="contentArea">
<h4 class="sub_heading">Log Details</h4>
<html:errors/>


<!-- S T A R T : Grid -->
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	
<%
ArrayList alLogList	=	(ArrayList)request.getAttribute("alLogList")==null?new ArrayList():(ArrayList)request.getAttribute("alLogList");
UserListVO userListVO	=	null;
%>
	
	<fieldset style="width:550px;" align="center"><div id="navigateBar"> Logged Details</div>
	<br>
	<table align="center" class="table table-striped"  border="0" cellspacing="0" cellpadding="0" style="width:500px;height:auto;">
	  <tr>
	  <th style="width: 10%" align="center" class="gridHeader"> Sl No. </th>
	  <th style="width: 20%" align="center" class="gridHeader"> User ID </th> 
	  <th style="width: 40%" align="center" class="gridHeader"> User Name</th>
	  <th style="width: 30%" align="center" class="gridHeader"> Logged On </th>
	  </tr>
	  <%
	  String strTob	=	"";
	  for(int i=0;i<alLogList.size();i++)
	  {
		  userListVO	=	(UserListVO)alLogList.get(i);
		 
      
	  %>
	  <tr class="<%=i%2==0?"gridEvenRow":"gridOddRow" %>"> 
	  		<td align="center"> <%=(i+1) %> </td>
	  		<td align="center">  <%=TTKCommon.getString(userListVO.getUserID()) %> </td>
	  		<td align="center">  <%=TTKCommon.getString(userListVO.getUserName()) %> </td>
	  		<td align="center">  
	  		<%SimpleDateFormat sdFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");%>
	  		<%=sdFormat.format(userListVO.getLoginDate().getTime()) %>
	  		 </td>
	  </tr>
	  
	  <%} %>
   </table>
 </fieldset>

<div>
<br>
<p style="margin-bottom: 0px; margin-left: 16%;">
<%
UserSecurityProfile securityProfile	=	(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
if(!"HOS".equals(securityProfile.getLoginType())) {%>
<img class="hexagon_small" src="/ttk/images/Insurance/small_hexagon_1.png" title="" alt="" align="middle" height="12" width="12"> &nbsp;&nbsp;&nbsp;
 <a href="#" onclick="javascript:onBack()" > <u>B</u>ack </a>
 <%}%>
 
 </p>
</div>

</div>
	<!-- E N D : Buttons and Page Counter -->
	<!-- E N D : Content/Form Area -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
</body>
</html:form>
