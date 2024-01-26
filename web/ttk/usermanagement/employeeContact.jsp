<%
/**
 * @ (#) usercontact.jsp 30th April 2019
 * Project      : TTK HealthCare Services
 * File         : employeeContact.jsp
 * Author       : Deepthi Meesala
 * Company      : RCS
 * Date Created : 30th April 2019
 *
 * @author       :
 * Modified by   : 
 * Modified date :
 */
%>

<%@ page import="com.ttk.common.TTKCommon"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/usermanagement/usercontact.js"></script>
<script type="text/javascript">
function SubmitTheFormFingerPrint(j){
	document.getElementById("fingerPrint").value= j;
}
function intilizeApplet(){
	var container = document.getElementById("applet-container");
		container.innerHTML = "<applet code=\"applet.JSGDApplet.class\" id=\"fingerPrintApplet\" width=\"550\" height=\"350\" name=\"JSGDApplet\" MAYSCRIPT archive=\"ttk/Applets.jar\" hspace=\"0\" vspace=\"0\" align=\"middle\">";
		container.innerHTML += "</applet>"; 
 }
</script>
<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>

<head>
	<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<html:form action="/EmployeeContactDeatils.do" >

	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><bean:write name="frmEmployeeContact" property="caption" /></td>
			<td width="43%" align="right" class="webBoard">&nbsp;</td>
  		</tr>
  	</table>
  	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<html:errors/>
	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		  <table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
		   <tr>
		     <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
		         <bean:message name="updated" scope="request"/>
		     </td>
		  </tr>
		 </table>
    </logic:notEmpty>
	<!-- E N D : Success Box -->
	 
	 <fieldset><legend>Personal Information</legend>
	 
	 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	 
	 <tr>
	  <td class="textLabelBold">Name: <span class="mandatorySymbol">*</span></td>
              <td  width="30%" class="textLabel">
              <html:text property="insuredName"  styleClass="textBox textBoxMedLarge" readonly="true" disabled="true"/>
              </td>
	 
	 <td></td>
	  <td></td>
	 </tr>
	 
	 <tr>
	  <td class="textLabelBold">Group Name: </td>
	 <td  width="30%" class="textLabel">
              <html:text property="strGroupName"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
              </td>
              
      <td width="18%" class="textLabelBold">Group Id: </td>
	 <td width="18%" class="textLabelBold">
              <html:text property="strGroupID"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
              </td>         
	 
	 </tr>
	 
	 <tr>
	  <td width="18%" class="textLabelBold">Policy No.: </td>
	 <td width="18%" class="textLabelBold">
              <html:text property="policyNumber"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
              </td>
              
      <td width="18%" class="textLabelBold">Enrollment No.: </td>
	 <td width="18%" class="textLabelBold">
              <html:text property="enrollmentNumber"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
              </td>         
	 
	 </tr>
	 
	  <tr>
	  
	     <td width="18%" class="textLabelBold">Email Id: </td>
	 <td width="18%" class="textLabelBold">
              <html:text property="emailId"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
              </td>         
	  
	 <td width="18%" class="textLabelBold">Lock: </td>
	 
	  <td width="18%" class="textLabelBold">
        
      <%--   <html:checkbox property="accn_locked_YN" styleId="accn_locked_YN" /> --%>
             <logic:equal name="frmEmployeeContact" property="accn_locked_YN" value="Y"> 
               <html:checkbox property="accn_locked_YN" styleId="accn_locked_YN" />
            </logic:equal>
            <logic:notEqual name="frmEmployeeContact" property="accn_locked_YN" value="Y">
             <html:checkbox  property="accn_locked_YN" styleId="accn_locked_YN"  onclick="return false;" disabled="true"  />
            </logic:notEqual> 
              </td>  
	 </tr>
	 
	 </table>
	 </fieldset>
	
	<fieldset><legend>Personal Information</legend>
	 
	 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	 
	  <tr>
	  <td width="10%" class="textLabelBold">User Type: </td>
	  <td width="30%" class="textLabelBold">
						<bean:write	name="frmEmployeeContact" property="userType1" />
					</td>
	 
	 
	 <td width="10%" class="textLabelBold">User Id: </td>
	 <td width="10%" class="textLabelBold">
              <html:text property="userId"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
              </td>
	 
	 </tr>
	 
	 </table>
	 </fieldset>

   	<!-- S T A R T : Buttons -->
   	
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    <%
	     if(TTKCommon.isAuthorized(request,"Edit"))
    		{
	    %>
			    <%
			    	if(TTKCommon.isAuthorized(request,"ResetPassword"))
		    		{
			    %>
				    	<logic:notEmpty name="frmEmployeeContact" property="policyGroupSeqId">
					    	<button type="button" name="Button2" accesskey="p" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onEmployeeResetPassword();">Reset <u>P</u>assword</button>&nbsp;
						</logic:notEmpty >
			    <%
			    	}
			    %>
		    	<button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSaveEmployee();"><u>S</u>ave</button>&nbsp;
		    	<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:ResetEmployee();"><u>R</u>eset</button>&nbsp;
	    <%
			}
		%>
	    	<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:employeeClose();"><u>C</u>lose</button>
	    </td>
	  </tr>
	</table>
	
	<!-- E N D : Buttons -->
	<html:hidden property="mode"/>
 </div>
<%--  <html:hidden property="userAccessVO.fingerPrint" styleId="fingerPrint"/> --%>
 <input type="hidden" name="child" value="ContactDetails">
 <input type="hidden" name="flag" value="">
 <html:hidden property="hddn_accn_locked_YN" name="frmEmployeeContact" />
 <html:hidden property="policyGroupSeqId" name="frmEmployeeContact" />
  <html:hidden property="userType" name="frmEmployeeContact" />
  <html:hidden property="userId" name="frmEmployeeContact" />
  <html:hidden property="userType1" name="frmEmployeeContact" />
 
 <script>

if(document.forms[1].hddn_accn_locked_YN.value=="Y")
    document.forms[1].accn_locked_YN.checked=true;
else
	document.forms[1].accn_locked_YN.checked=false;
    
	</script> 
</html:form>
