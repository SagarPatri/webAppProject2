
 <%
/** @ (#) onlineaccess.jsp Sep 25th, 2007
 * Project       : Vidal Health TPA Healthcare Services
 * File          : onlineaccess.jsp
 * Author        : Chandrasekaran J
 * Company       : Span Systems Corporation
 * Date Created  : Sep 25th, 2007
 * @author          : Chandrasekaran J
 * Modified by   : Ramakrishna K M
 * Modified date :
 * Reason        : Insurance Login
 */
 %>
  <%@page import="com.ttk.dao.impl.support.SupportDAOImpl,com.ttk.dto.maintenance.GeneralAnnouncementVO"%>
<%@ page import="java.util.*,java.io.OutputStream,java.sql.Blob,javax.servlet.http.*,com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile" %>
 <%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Alkoot Administrator - Online Access</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
<link href="/ttk/styles/LoginPage.css" media="screen" rel="stylesheet"></link>
<link href="/ttk/styles/OnlineDefault.css" media="screen" rel="stylesheet"></link>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/utils.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineaccess.js"></SCRIPT>
</head>
<body onload="setInterval('blinkIt()',500)">
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%
String sideHeading="";
String loginType=TTKCommon.checkNull(request.getParameter("loginType"));
    pageContext.setAttribute("loginTypeList",Cache.getCacheObject("onlineAccess"));
    session.setAttribute("Login_Type",loginType);
    request.setAttribute("loginType",loginType);
    
    if("HOS".equals(loginType))sideHeading="Hospital Login";
    else if("OIU".equals(loginType))sideHeading="Insurance Login";
    else if("OBR".equals(loginType))sideHeading="Broker Login";
    else if("PTR".equals(loginType))sideHeading="Partner Login";

    
java.util.ArrayList al =SupportDAOImpl.getGeneralAnnouncementsDetails();
GeneralAnnouncementVO generalannounceDetails =null;
 %>
 <html:form action="/OnlineAccessAction.do" onsubmit="SubmitTheForm()">
<body class="loginBodyproviderBg" onLoad="document.forms[0].elements['userid'].focus();">
<div class="loginBodyproviderBg">

<logic:notEmpty name="SessionExpired" scope="request">
    <script type="text/javascript">
    var loginType='<bean:write name="loginType" scope="request"/>';
    openFullScreenMode("/ttk/onlineaccess.jsp?sessionexpired=true&loginType="+loginType);
    </script>
    </logic:notEmpty>
    <logic:notEmpty name="LogoutSeccess" scope="request">
    <script type="text/javascript">
    var loginType='<bean:write name="loginType" scope="request"/>';
    openFullScreenMode("/ttk/onlineaccess.jsp?Logout=SUCCESS&loginType="+loginType);
    </script>
    </logic:notEmpty>
    <%
    if(request.getParameter("Logout")!=null)
    {
%>
          <table align="center" class="successContainer"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
                        <bean:message key="logout.success"/>
                    </td>
                 </tr>
            </table>
<%
    }//end of if(request.getParameter("Logout")!=null)
%>
    <html:errors/>

    <%
    if(request.getParameter("sessionexpired")!=null)
    {
%>
      <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
       <tr>
         <td><img src="/ttk/images/ErrorIcon.gif" alt="Success" title="Success" width="16" height="16" align="absmiddle">&nbsp;<bean:message key="error.onlinesession"/>
         </td>
       </tr>
      </table>
<%
    }//end of if(request.getParameter("sessionexpired")!=null)
%>
<!-- S T A R T : Success Box -->

        <logic:notEmpty name="updated" scope="request">    
               <table align="center" class="successContainer"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
                        <bean:write name="updated" scope="request"/>
                    </td>
                 </tr>
            </table>
        </logic:notEmpty>
        
        
         <%-- <table id="brokerSubContainerTable">
                        <tr>
                            <td  id="brokerLeftImg">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td  id="brokerMidBody">
                            <div id="brokerDivMidBody">
         <div id="sideHeading"><%=sideHeading %></div> --%>             
    <!-- E N D : Success Box -->
<!--h4 class="sub_heading"><%=sideHeading %></h4-->
         <br><br><br>
<table width="32%" align="right" valign="middle" border="0" cellspacing="0" cellpadding="0"style="padding-top: 90px;padding-right: 50px;">

<logic:equal name="Login_Type" value="HOS">     
                     <tr>
                		<td class="loginText" width="35%" nowrap>Empanelment Id</td>
                    <!--     <td>:</td> -->
                        <td align="left" width="170">
                            <html:hidden property="hosProvType"  value="IND"/>
                            <html:text property="hosEmpaneId" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" /></td>
                     </tr>
                    <!--  <tr><td colspan="3"> &nbsp;</td>
                     </tr> -->
                     <tr>
                        <td nowrap="nowrap"  class="loginText" width="35%">User Id</td>
                    <!--     <td>:</td> -->
                        <td align="left" width="170"><html:text property="hosUserId" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="20" /></td>
                      </tr>
                     <!-- <tr><td colspan="3"> &nbsp;</td>
                     </tr> -->
                      <tr>
                        <td nowrap="nowrap" class="loginText" width="35%">Password </td>
                    <!--     <td>:</td> -->
                        <td align="left" width="170"><input name="hosPassword" type="password" class="textBoxWeblogin textBoxMediumWeblogin" id="LoginId32" maxlength="20" ></td>
                      </tr>
                      <tr><td colspan="3"> &nbsp;</td>
                     </tr> 
                     
                     
</logic:equal>
<!-- Added for Partner login -->
 <logic:equal name="Login_Type" value="PTR">     
                     <tr>
                		<td class="loginText" width="35%" nowrap>Partner Id</td>
                        <td align="left" width="170"><html:text property="ptrEmpaneId" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" /></td>
                     </tr>
                     <tr>
                        <td nowrap="nowrap"  class="loginText" width="35%">User Id</td>
                		<td align="left" width="170"><html:text property="ptrUserId" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="20" /></td>
                      </tr>
                      <tr>
                        <td nowrap="nowrap" class="loginText" width="35%">Password </td>
                		<td align="left" width="170"><input name="ptrPassword" type="password" class="textBoxWeblogin textBoxMediumWeblogin" id="LoginId32" maxlength="20"  ></td>
                      </tr>
                      <tr><td colspan="3"> &nbsp;</td>
                     </tr> 
</logic:equal> 

<logic:equal name="Login_Type" value="OIU">   
    
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Ins Company Code</td>
                <td>:</td>
                <td align="right" width="170"><html:text property="insCompCode" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="20"/></td>
              </tr>
               <tr>
                <td nowrap="nowrap" class="loginText" width="125">User Id</td>
                <td>:</td>
                <td align="right" width="170"><html:text property="insUserId" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="20"/></td>
              </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Password</td>
                <td>:</td>
                <td align="right" width="170"><input name="insPassword" type="password" class="textBoxWeblogin textBoxMediumWeblogin"  maxlength="20"></td>
              </tr>
    
    </logic:equal>
       
    <logic:equal name="Login_Type" value="OBR">                      
                              
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Broker Code</td>
            <!--     <td>:</td> -->
                <td align="left" width="170"><html:text property="brokerCompCode"  styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="20" /></td>
              </tr>
               <tr>
                <td nowrap="nowrap" class="loginText" width="125">User Id</td>
              <!--   <td>:</td> -->
                <td align="left" width="170"><html:text property="broUserId"   styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="20" /></td>
              </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Password</td>
            <!--     <td>:</td> -->
                <td align="left" width="170"><input name="broPassword" type="password"   class="textBoxWeblogin textBoxMediumWeblogin"   maxlength="20"></td>
              </tr>       
               <tr><td colspan="3"> &nbsp;</td>
                     </tr>        
             
    
    </logic:equal>
    
     <logic:equal name="Login_Type" value="OHR">
             <tr>
                <td class="loginText" width="125" nowrap>Group Id:</td>
                <td align="left" width="170"><html:text property="hrGroupId" styleClass="textBoxWeblogin textBoxMediumWeblogin"  onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/></td>
              </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">User Id:</td>
                <td align="left" width="170"><html:text property="hrUserId" styleClass="textBoxWeblogin textBoxMediumWeblogin\" autocomplete=\"off"  maxlength="20" /></td>
              </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Password: </td>
                <td align="left" width="170"><input type="password"  style="display:none" /><input name="hrPassword" type="password" class="textBoxWeblogin textBoxMediumWeblogin" autocomplete="off" id="LoginId32" maxlength="20"><input type="password"  style="display:none" /></td>
              </tr>
               <tr><td colspan="3"> &nbsp;</td>
                     </tr> 
             <!--  <tr>
                       <td class="loginText">&nbsp;</td>
                   <td align="left">
                   <input type="submit" name="mybutton" value="Login" class="buttons" onmouseout="this.className='buttons'" onmouseover="this.className='buttons buttonsHover'"/>&nbsp;
                   <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="window.close()"><u>C</u>lose</button>
                   </td>
               </tr> -->
    </logic:equal>
    
    
     <logic:equal name="Login_Type" value="OCO">
              <tr>
                <td class="loginText" width="125" nowrap>Group Id:</td>
                <td align="right" width="170"><html:text property="groupId" styleClass="textBoxWeblogin textBoxMediumWeblogin" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/></td>
                <td></td>
             </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Policy No. :</td>
                <td align="right" width="170"><html:text property="corpPolicyNo"  styleClass="textBoxWeblogin textBoxMediumWeblogin" readonly="true" maxlength="60"/></td>
                <td align="center" class="loginText" width="50" nowrap><a href="#" onClick=window.open("/ttk/common/showpolicylist.jsp?GroupID="+document.forms[0].groupId.value+"",'',"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350")><img src="/ttk/images/ViewDetails.gif" title="View Policies" alt="View Policies" width="16" height="16" border="0"></a></td>
                 <!-- KOC1227A -->
                <td><html:hidden property="corpPolicyNo1"/> </td>
              </tr>
              <tr>
                <td nowrap="nowrap"  class="loginText" width="125">User Id:</td>
                <td align="right" width="170"><html:text property="userId" styleClass="textBoxWeblogin textBoxMediumWeblogin\" autocomplete=\"off" maxlength="20" /></td>
                <td></td>
              </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Password: </td>
                <td align="right" width="170"><input type="password" value="XYZ" style="display:none" /><input name="password" type="password" class="textBoxWeblogin textBoxMediumWeblogin" autocomplete="off" id="LoginId32" maxlength="20"><input type="password" value="XYZ" style="display:none" /></td>
                <td></td>
              </tr>
              <tr>
                <td height="8">&nbsp;</td>
                <td >&nbsp;</td>
              </tr>
    </logic:equal>
     <logic:equal name="Login_Type" value="EMPL">
               <tr>
                <td nowrap="nowrap"  class="loginText" width="125">Alkoot Id: </td>
                <td align="right" width="170"><html:text property="userId" styleClass="textBoxWeblogin textBoxMediumWeblogin\" autocomplete=\"off" maxlength="20" /></td>
                <td></td>
              </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">First Password: </td>
                <td align="right" width="170">&nbsp;<input name="password" type="password" class="textBoxWeblogin textBoxMediumWeblogin" autocomplete="off" id="LoginId32" maxlength="20" ></td>
                <td></td>
              </tr>
              <tr>
                <td nowrap="nowrap"  class="loginText" width="125">Second Password: </td>
                <td align="right" width="170">&nbsp;
                <input name="dateOfBirth" type="text" class="textBoxWeblogin textBoxMediumWeblogin" maxlength="10" >
                 <%-- <html:text  property="dateOfBirth" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="10"/> --%>
                 <!-- <a
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[0].dateOfBirth',document.forms[0].dateOfBirth.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;">
                    <img
                        src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a> -->
				</td>
				
              </tr>
              <tr>
                <td height="8">&nbsp;</td>
                <td >&nbsp;</td>
              </tr>
              
              <%-- <tr>
                <td class="loginText" width="125" nowrap>Group Id:</td>
                <td align="right" width="170"><html:text property="groupId" styleClass="textBoxWeblogin textBoxMediumWeblogin" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/></td>
                <td></td>
             </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Policy No. :</td>
                <td align="right" width="170"><html:text property="corpPolicyNo"  styleClass="textBoxWeblogin textBoxMediumWeblogin" readonly="true" maxlength="60"/></td>
                <td align="center" class="loginText" width="50" nowrap><a href="#" onClick=window.open("/ttk/common/showpolicylist.jsp?GroupID="+document.forms[0].groupId.value+"",'',"scrollbars=1,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=0,width=800,height=350")><img src="/ttk/images/ViewDetails.gif" title="View Policies" alt="View Policies" width="16" height="16" border="0"></a></td>
                 <!-- KOC1227A -->
                <td><html:hidden property="corpPolicyNo1"/> </td>
              </tr>
              <tr>
                <td nowrap="nowrap"  class="loginText" width="125">User Id:</td>
                <td align="right" width="170"><html:text property="userId" styleClass="textBoxWeblogin textBoxMediumWeblogin\" autocomplete=\"off" maxlength="20" /></td>
                <td></td>
              </tr>
              <tr>
                <td nowrap="nowrap" class="loginText" width="125">Password: </td>
                <td align="right" width="170"><input type="password" value="XYZ" style="display:none" /><input name="password" type="password" class="textBoxWeblogin textBoxMediumWeblogin" autocomplete="off" id="LoginId32" maxlength="20"><input type="password" value="XYZ" style="display:none" /></td>
                <td></td>
              </tr>
              <tr>
                <td height="8">&nbsp;</td>
                <td >&nbsp;</td>
              </tr> --%>
    </logic:equal>
    <tr>                   
         <td  colspan="2" align="center">                
                <button type="button" name="mybutton" accesskey="l"  class="olbtn" onClick="SubmitTheForm();"><u>L</u>ogin</button>&nbsp;
                <button type="button" name="Button" accesskey="c" class="olbtn" onClick="window.close()"><u>C</u>lose</button>
             </td>
           </tr>
 
  <tr><td colspan="2"> &nbsp;</td>
                     </tr>
        <!--  <tr>
           <td height="50" class="loginText" width="70" colspan="2">&nbsp;</td>
           <td style="padding-top: 0px" align="left" nowrap="nowrap" class="loginText" colspan="2">
            <a href="#"  onClick="javascript:onForgotPassword()" class="events">Forgot Password</a>
            </td>
          </tr>-->
      <tr>
           <td style="padding-top: 0px;padding-left: 0px;" align="center" nowrap="nowrap" class="loginText" colspan="2">
            <a href="#"  onClick="javascript:onForgotPassword()" class="events">Forgot Password</a>
           </td>
          </tr>
       
       </table>
       <%-- <logic:equal name="Login_Type" value="EMPL"> 
 		<table align="right" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0" style="padding-top: 39px;  padding-left: 65%;">
				<tr>
					<td class="formLabel">Note:  </td>
					<td class="textLabel"><label  style="color:red;text-decoration:none;font-size: 12px; text-align: left;" >Please enter your date of birth in DD/MM/YYYY format <br/>as your password.</label>
					</td>
				</tr>
			</table>
         </logic:equal> --%>
         
        
 <table width="100%" align="center" valign="bottom" border="0" cellspacing="0" cellpadding="0" >
             <tr>
                <td align="bottom" valign="bottom" width="30%" height="100%"
                    style="padding-top: 260px;">
                    
                    <marquee direction="left"
                        scrollamount="3" scrolldelay="0" onmouseover="this.stop()"
                        onmouseout="this.start()">
                        <ul class="ancList">
                        <%
                                if (al != null && al.size() > 0 && "HOS".equals(loginType)) {
                                        Iterator iterator = al.iterator();
                                        while (iterator.hasNext()) {
                                            generalannounceDetails = (GeneralAnnouncementVO) iterator.next();
                                        if (generalannounceDetails.getAnnouncementMes() != null
                                                && (generalannounceDetails.getAnnouncementMes()) != "") {
                            %><span
                                class="scroll"><font size="2" color="red"><%=generalannounceDetails.getAnnouncementMes()%></font></span>&nbsp;&nbsp;&nbsp;
                            <%
                                }}}
                            %>
                        </ul>
                    </marquee></td>
            </tr>            
            <tr>
            <td class="" valign="bottom" align="center"style="padding-top: 30px; padding-right: 0px;">
              <p> <font color="wight"></font>  Note: Best viewed on IE9 & Above </p>
         </td>
</tr>         
         </table>
  
    </div>


<input type="hidden" name="mode">
<INPUT TYPE="hidden" NAME="randomNo" VALUE=""><!-- added for koc 1349 -->
<input type="hidden" name="loginType" value="<%=request.getParameter("loginType")%>">
<input type="hidden" name="subLoginType" value="<%=request.getAttribute("subLoginType")%>">
<input type="hidden" name="open" value="<%=TTKCommon.checkNull(request.getParameter("open"))%>">
<logic:notMatch name="frmOnlineAccess" property="firstLoginYN" value="Y">
<logic:notMatch name="frmOnlineAccess" property="pwdExpiryYN" value="Y">
<logic:notEmpty name="OpenPage">
    <script language="javascript">
        V2Login("<bean:write name="OpenPage"/>");
    </script>
    </logic:notEmpty>
</logic:notMatch>
<logic:match name="frmOnlineAccess" property="pwdExpiryYN" value="Y">
    <script language="javascript">
    var res = confirm("<bean:write name="frmOnlineAccess" property="pwdExpiryMsg"/>");
    if(res == true)
    {
        onChangePassword();
    }//end of if(res == true)
    else{
        V2Login("<bean:write name="OpenPage"/>");
        }    
    </script>
</logic:match>
</logic:notMatch>
<logic:match name="frmOnlineAccess" property="firstLoginYN" value="Y">
<logic:notMatch name="frmOnlineAccess" property="pwdExpiryYN" value="Y">
<logic:equal name="frmOnlineAccess" property="loginExpiryYN" value="Y">
<script language="javascript">
//alert("<bean:write name="frmOnlineAccess" property="firstLoginYN"/>");
            var res = confirm("Please change the password for security reasons");
            if(res == true)
            {
         		onChangePassword();
            }//end of if(res == true)
            	
    </script>
</logic:equal>
<logic:notEqual name="frmOnlineAccess" property="loginExpiryYN" value="Y">
<script language="javascript">

            var res = confirm("Your password has expired.Please change your password");
            if(res == true)
            {
                onChangePassword();
            }//end of if(res == true)
    </script>
</logic:notEqual>

</logic:notMatch>
<logic:match name="frmOnlineAccess" property="pwdExpiryYN" value="Y">
<script language="javascript">
var res = confirm("<bean:write name="frmOnlineAccess" property="pwdExpiryMsg"/>");
            if(res == true)
            {                onChangePassword();
            }//end of if(res == true)
            else{    
                // var loginType=document.forms[0].loginType.value;
                 V2Login("<bean:write name="OpenPage"/>");
            }
    </script>
</logic:match>
</logic:match>

</body>
</html:form>

 
