
<%
/** @ (#) premiuminfo.jsp Feb 9, 2006
 * Project     : TTK Healthcare Services
 * File        : changepassword.jsp
 * Author      : Chandrasekaran J
 * Company     : Span Systems Corporation
 * Date Created: 23rd Aug 2007
 *
 * @author 		 :  Chandrasekaran J
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ page import=" com.ttk.common.TTKCommon" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
function onChangePassword()
{
    document.forms[1].mode.value="doChangeUsers";
	document.forms[1].action = "/SaveChangePasswordAction.do";
	document.forms[1].submit();
}	//end of onChangePassword()
function onClose()
{
    document.forms[1].mode.value="doClose";
	document.forms[1].action = "/ChangePasswordAction.do";
	document.forms[1].submit();
}//end of onClose()	
</SCRIPT>
<%
	boolean viewmode=true;
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))	
%>
<!-- S T A R T : Content/Form Area -->	
<html:form action="/ChangePasswordAction.do">

<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td>Employee Change Password</td>
			<td width="43%" align="right" class="webBoard">&nbsp;</td>     
		</tr>
	</table>
<!-- E N D : Page Title --> 		

<!-- S T A R T : Form Fields -->
	<div class="contentArea" id="contentArea">
	<html:errors/>		
	<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">	
		   	<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			    	<td><img src="/ttk/images/SuccessIcon.gif" title="Success" width="16" height="16" align="absmiddle">&nbsp;
			    		<bean:message name="updated" scope="request"/>
			    	</td>
			 	</tr>
			</table>
		</logic:notEmpty>
	<!-- E N D : Success Box -->
	<fieldset>
	<legend>Change Password</legend>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="17%" class="formLabel">Name: </td>
        <td width="37%" class="textLabel"><bean:write property="empName" name="frmChangePassword"/></td>
        <td width="17%" class="formLabel">Emp No.:</td>
        <td width="29%" class="textLabel"><bean:write property="empNbr" name="frmChangePassword"/></td>
      </tr>
      <tr>
        <td class="formLabel">Al Koot No.: </td>
        <td class="textLabel"><bean:write property="enrollmentNbr" name="frmChangePassword"/></td>
        <td class="formLabel">Primary Email ID:</td>
        <td class="textLabel"><bean:write property="primaryEmailID" name="frmChangePassword"/></td>
      </tr>
<!-- 	   <tr> -->
<!--         <td class="formLabel">Alternate Email ID:</td> -->
<!--         <td> -->
<%--         	<html:text property="alterEmailID" styleClass="textBox textBoxMedium" maxlength="60" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>        	 --%>
<!--         </td> -->
<!--       </tr> -->
      <tr>
        <td class="formLabel">User ID:</td>
        <td class="textLabel"><bean:write property="userID" name="frmChangePassword"/></td>
        <td class="formLabel">Current Password:</td>
        <td class="textLabel"><bean:write property="currentPwd" name="frmChangePassword"/></td>
      </tr>
    </table>
	</fieldset>
<!-- E N D : Form Fields -->

<!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
    	<tr>
        	<td width="100%" align="center">
       			<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onChangePassword()">Change <u>P</u>assword</button>&nbsp;
	    		<button type="button" name="Button" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose()"><u>C</u>lose</button>&nbsp;
			</td>
		</tr>
	</table>
<!-- E N D : Buttons -->
</div>
<INPUT TYPE="hidden" NAME="mode" VALUE=""/>
<html:hidden property="PolicyGroupSeqID"/>
</html:form>
<!-- E N D : Content/Form Area -->	
