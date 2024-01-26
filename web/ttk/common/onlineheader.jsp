<!-- S T A R T : Header -->
<%@ page import="com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
String LoginType=userSecurityProfile.getLoginType();
String GroupID=userSecurityProfile.getGroupID();
String USER_ID=userSecurityProfile.getUSER_ID();
String RandomNo=userSecurityProfile.getRandomNo();
  
request.setAttribute("LoginType", LoginType);

%>
<table width="100%"  border="0" cellspacing="0" class="header" cellpadding="0">
  <tr>
    <td width="36%"><img src="/ttk/images/Al_Koot_New_Logo.png" alt="Al Koot Administrator Services" width="306" height="57"></td>
    <td width="64%"><table width="100%" height="57"  border="0" cellpadding="0" cellspacing="0">
        <tr></tr>
        <tr>
        <td align="right" valign="middle" class="headerInfo">User: <span class="headerInfoValue"><%=userSecurityProfile.getUserName()%></span> &nbsp;&nbsp;
               	|&nbsp;&nbsp;Date: <span class="headerInfoValue"><%=userSecurityProfile.getLoginDate()%></span>
        |&nbsp;&nbsp;
        <logic:equal value="H" name="LoginType" scope="request">
        <a href="#" onClick="javascript:onlineHRLogout()" class="headerLinks" title="Logout" style="font-weight:bold;">Logout</a>
        </logic:equal>
        <logic:equal value="E" name="LoginType" scope="request">
        <a href="#" onClick="javascript:onlineEmpLogout()" class="headerLinks" title="Logout" style="font-weight:bold;">Logout</a>
        </logic:equal>
        <logic:equal value="EMPL" name="LoginType" scope="request">
        <a href="#" onClick="javascript:onlineEmpLogout()" class="headerLinks" title="Logout" style="font-weight:bold;">Logout</a>
        </logic:equal>
        </td>
        </td>
        
        </tr>
        <tr>
          <td align="left"><span style="font-face:tahoma;font-size:16px;font-weight:bold;color:#A83108;"><%=userSecurityProfile.getGroupName()%></span>
          </td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td colspan="2" class="headerSeparator" nowrap></td>
  </tr>
  <tr>
    <td colspan="2" class="headerLowerBand" nowrap></td>
  </tr>
</table>
<!-- E N D : Header -->