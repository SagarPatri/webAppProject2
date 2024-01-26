<%@page import="java.util.ArrayList"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@page import="com.ttk.dao.impl.support.SupportDAOImpl,com.ttk.dto.maintenance.GeneralAnnouncementVO"%>
<%@ page import="java.util.*,java.io.OutputStream,java.sql.Blob,javax.servlet.http.*,com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<SCRIPT type="text/javascript" SRC="/ttk/scripts/utils.js"></SCRIPT>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/index.js"></SCRIPT>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Alkoot Administrator - L-O-G-I-N  P-A-G-E</title>
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
<style>
</style>
</head>
<body onLoad="document.forms[0].elements['userid'].focus();">
<div class="indexLoginBodyBg">
<html:errors/>
<logic:notEmpty name="errorMsg" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/ErrorIcon.gif" alt="Error"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="errorMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>

			<!-- S T A R T : Success Box -->
			<logic:notEmpty name="updated" scope="request">
				<table align="center" class="successContainer" style="display:"
					border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" alt="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="updated" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
            
            <%
	if(request.getParameter("sessionexpired")!=null)
	{
%>
	  <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td><img src="/ttk/images/ErrorIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
	         <bean:message key="error.session"/>
	     </td>
	   </tr>
	  </table>
<%
	}//end of if(request.getParameter("sessionexpired")!=null)
%>

<%  java.util.ArrayList al =SupportDAOImpl.getGeneralAnnouncementsDetails();
 GeneralAnnouncementVO generalannounceDetails =null; 
 %>
<html:form action="/LoginAction.do" onsubmit="return SubmitTheForm();">
 
<table width="35%" align="right" valign="middle" border="0" cellspacing="0" cellpadding="0"style="padding-top: 100px;padding-right: 60px;">
  <tr>
                      <td class="loginText"><font color="grey">User ID: </font></td>
                      <td align="left"><input name="userid" type="text" class="textBox textBoxLarge" id="LoginId" style="height:18px;" maxlength="20">
         </td>
                    </tr>
                    <tr>
                      <td class="loginText"><font color="grey">Password:</font></td>
                      <td align="left"><input name="password"  type="password" class="textBox textBoxLarge" id="Password" style="height:18px;" maxlength="20">
          &nbsp;</td>
                    </tr>
                   <tr>
                      <td class="loginText">&nbsp;</td>
						<td align="left" style="padding-left: 122px;" >
                      <logic:notEmpty name="UALO" scope="request">
                       <input type="button" name="mybutton" value="Logout" onclick="onUserLogout();" class="buttons" onmouseout="this.className='buttons'" onmouseover="this.className='buttons buttonsHover'" />
                      </logic:notEmpty>
                      <input type="submit" name="mybutton" value="Login" class="buttons" onmouseout="this.className='buttons'" onmouseover="this.className='buttons buttonsHover'" />
                      &nbsp;
                      </td>
                    </tr>
                    <tr>
                    <td colspan="2">
                    <logic:notEmpty name="UALO" scope="request">
                        <div>
                        <font style="font-size:10px;color: red;">Please LogOut With Your Valid Credentials</font>
                        </div>
                    </logic:notEmpty>
                    </td>
                    </tr>
</table>

			<table width="100%" align="center" valign="bottom" border="0"
				cellspacing="0" cellpadding="0">
			<tr>
					<td align="bottom" valign="bottom" width="30%" height="100%"
						style="padding-top: 365px;"><marquee direction="left"
						scrollamount="3" scrolldelay="0" onmouseover="this.stop()"
						onmouseout="this.start()">
						<ul class="ancList">

							<%
								if (al != null && al.size() > 0) {
										Iterator iterator = al.iterator();
										while (iterator.hasNext()) {
											generalannounceDetails = (GeneralAnnouncementVO) iterator
													.next();
										if (generalannounceDetails.getAnnouncementMes() != null
												&& (generalannounceDetails.getAnnouncementMes()) != "") {
								%><span class="scroll"><font size="2" color="red"><%=generalannounceDetails
									.getAnnouncementMes()%></font></span>&nbsp;&nbsp;&nbsp;
							<%
									}
											}
										}
							%>

						</ul>
					</marquee></td>
			</tr>
		 <tr>
					<td class="" valign="bottom" align="center"
						style="padding-bottom: 30px; padding-right: 0px;">
						<p>
							<font color="black"></font> Note: Best viewed on IE9 & Above
						</p>
 		</td>
				</tr>
		</table>
			
	<input type="hidden" name="mode">
<input type="hidden" name="open" value="<%=TTKCommon.checkNull(request.getParameter("open"))%>">
<logic:notMatch name="frmLogin" property="firstLoginYN" value="Y">
<!-- Changes Added for Password Policy CR KOC 1235 -->
<logic:notMatch name="frmLogin" property="pwdExpiryYN" value="Y">
<!-- End changes for Password Policy CR KOC 1235 -->
<logic:notEmpty name="OpenPage">
	<script type="text/javascript">
			openFullScreenMode("<bean:write name="OpenPage"/>");
	</script>
	</logic:notEmpty>
</logic:notMatch>
<!-- Changes Added for Password Policy CR KOC 1235 -->
<logic:match name="frmLogin" property="pwdExpiryYN" value="Y">
<script type="text/javascript">
			var res = confirm("<bean:write name="frmLogin" property="pwdExpiryMsg"/>");
			if(res == true)
			{
				onChangePassword("<bean:write name="frmLogin" property="userid"/>");
			}//end of if(res == true)
			else//end of else(res == false)
			{
				openFullScreenMode("<bean:write name="OpenPage"/>");
			}
	</script>
</logic:match>
<!-- End changes for Password Policy CR KOC 1235 -->
</logic:notMatch>
<logic:match name="frmLogin" property="firstLoginYN" value="Y">
<!-- Changes Added for Password Policy CR KOC 1235 -->
<logic:notMatch name="frmLogin" property="pwdExpiryYN" value="Y">
<script type="text/javascript">
			var res = confirm("Please Change the Password for Security Reasons.");
			if(res == true)
			{
				onChangePassword("<bean:write name="frmLogin" property="userid"/>");
			}//end of if(res == true)
	</script>
</logic:notMatch>
<logic:match name="frmLogin" property="pwdExpiryYN" value="Y">
<script type="text/javascript">
			var res = confirm("<bean:write name="frmLogin" property="pwdExpiryMsg"/>");
			if(res == true)
			{
				onChangePassword("<bean:write name="frmLogin" property="userid"/>");
			}//end of if(res == true)
			else
			{
				openFullScreenMode("<bean:write name="OpenPage"/>");
			}//end of else(res == false)
	</script>
</logic:match>
<!-- End changes for Password Policy CR KOC 1235 -->
</logic:match>		
		</html:form>                  
    </div>
</body>

