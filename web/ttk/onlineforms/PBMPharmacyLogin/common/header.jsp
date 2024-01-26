<!-- S T A R T : Header -->
<%@ page import="com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" style="width:100%; background-color:#FFFFFF;">
  		<tr>
  			<td style="border-bottom:2px solid #000;">&nbsp;&nbsp;&nbsp;&nbsp;<img style="padding-top:2px;" src="/ttk/images/Al_Koot_New_Logo.png" title="Logo" alt="Logo"></td>

			<td align="right" style="border-bottom:2px solid #000;">
						<font size="2px"><strong>	Welcome <%=userSecurityProfile.getUserName() %>,<br /><br /></strong>
							Last Login : <%=userSecurityProfile.getLastLoginDate() %> 
							&nbsp;&nbsp;|&nbsp;&nbsp;
							Current Date : <%=userSecurityProfile.getLoginDate() %> 
							&nbsp;&nbsp;|&nbsp;&nbsp;
							<a href="#" onClick="javascript:inslogout()" class="headerLinks" title="Logout" style="color:#008c8d">Logout </a>
						</font>
			</td>
		</tr>
	</table>

<!-- E N D : Header 106-->	