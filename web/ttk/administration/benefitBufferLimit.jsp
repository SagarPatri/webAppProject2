<%/**
 * @ (#) benefitBufferLimit.jsp 22nd July 2019
 * Project       : TTK HealthCare Services
 * File          : benefitBufferLimit.jsp
 * Author        : Deepthi Meesala
 * Company       : RCS Technologies
 * Date Created  : 22nd July 2019
 * Modified by   :
 * Modified date :
 * Reason        :
 */
 %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import=" com.ttk.common.TTKCommon" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/administration/benefitBufferLimit.js"></script>

<!-- S T A R T : Content/Form Area -->	
	<html:form action="/BenefitBufferLimit.do"> 	
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td>BenefitType Buffer Limit - <bean:write name="frmBufferLimit" property="caption"/></td>
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
	<!-- S T A R T : Form Fields -->
	
 <fieldset><legend>Eligibility Check</legend>
<%--  <logic:notEmpty name="frmBufferLimit" property="prodPolicyLimit"> --%>
<!--  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">   -->   
<table>
  <tr>
    <!-- <td class="formLabelBold">Benefit Type</td> -->
    <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td><b>Benefit Type</b></td>
    <td><b>Provider & PBM<br>Eligibility Check</b></td>
 	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Partner<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Eligibility Check</b></td>
  </tr>
  
  
  <tr>
  <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
  <td  >Dental</td>
   <td><html:text  property="dental"  styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isNumaricOnly(this)"/>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text  property="partnerDental"  styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isNumaricOnly(this)"/>
   </td>
    </tr>
   
   <tr>
    <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
  <td  >Optical</td>
   <td ><html:text  property="optical" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text  property="partnerOptical" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
    </tr>
   
   <tr>
   <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
  <td  class="formLabel">Outpatient maternity</td>
   <td><html:text  property="opMaternity" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text  property="partnerOpMaternity" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
   </tr>
  
  <tr>
  <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
  <td  class="formLabel">Outpatient</td>
   <td><html:text property="outpatient" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text property="partnerOutpatient" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
    </tr>
   <tr>
   <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
  <td  class="formLabel">Prescription medications &nbsp;</td>
   <td><html:text property="prescription" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text property="partnerPrescription" styleClass="textBox textBoxSmall" maxlength="10"  onkeyup="isNumaricOnly(this)" /></td>
  </tr>
  </table>
	</fieldset>
	
	<!-- E N D : Form Fields -->
	<!-- S T A R T : Buttons -->
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" align="center">
    <%
    	if(TTKCommon.isAuthorized(request,"Edit"))
    	{
    %>
    <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onSave()"><u>S</u>ave</button>&nbsp;
	<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button>&nbsp;
	<%
		}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	%>
	<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	</td>
  </tr>
</table>


<logic:notEmpty name="frmBufferLimit" property="frmChanged">
		<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>

<%-- <logic:notEmpty name="frmBufferLimit" property="prodPolicyLimit">
<script language="javascript">
			onDocumentLoad();
</script>
</logic:notEmpty> --%>

<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
	<!-- E N D : Buttons -->
</div>
</html:form>

