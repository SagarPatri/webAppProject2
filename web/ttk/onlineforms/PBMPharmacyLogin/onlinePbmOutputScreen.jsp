
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon,org.apache.struts.action.DynaActionForm" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<head>
    
    
<script type="text/javascript">
  function onExit()
{
	/* document.forms[1].tab.value ="Check Eligibility";
	document.forms[1].sublink.value ="Check Eligibility";
	document.forms[1].leftlink.value ="Approval Claims"; */
	
	document.forms[1].mode.value="doBack";
	document.forms[1].action="/PbmPharmacyPreauthViewlAction.do";
	document.forms[1].submit();
}  

</script>
</head>
<html:form action="/PbmPharmacyGeneralAction.do" >

		<div class="contentArea" id="contentArea">
	<h4 class="sub_heading">Pre-Approval-Appeal OutPut Screen</h4>
<br><br>
	<!-- S T A R T : Page Title -->
	<html:errors/>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Form Fields -->
	

	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
		<tr>
			<td width="100%" class="formLabel" align="left" colspan="2">
				<strong> Your PreApproval Request is <font color="blue">  <bean:write name="frmPbmPreauthGeneral" property="preAuthstatusDesc"/></font> </strong>
					<br>
				 <strong>  "Received the preapproval appeal request, case is forwarded for review". </strong></td>	
		</tr>
		
		<tr>
			 <td width="50%" class="formLabel"> <strong>  Pre-Approval No. : &nbsp; &nbsp;  <bean:write name="frmPbmPreauthGeneral" property="preAuthNO"/>  </strong></td>	
	   	</tr>
   	
		</table>
		<br>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
		    <tr>
		       <td width="15%" class="formLabel">Authorization No. </td>
		       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="authorizationNO"/> </td>
		       <td width="15%" class="formLabel">Card Holder Name</td>
		       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="memberName"/> </td>
		   	</tr>
		     <tr>
		       <td width="15%" class="formLabel">Policy No.</td>
		       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="policyNo"/> </td>
		       <td width="15%" class="formLabel">Clinician Id </td>
		       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="clinicianID"/> </td>
		   	</tr>
		     <tr>
		       <td width="15%" class="formLabel">Clinician Name</td>
		       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="clinicianName"/> </td>
		       <td width="15%" class="formLabel">Status </td>
		       <td width="35%">: In-Progress-Appeal &nbsp;&nbsp;
		       	<img src="/ttk/images/InprogressAppeal.gif" title="In Progress Appeal" alt="In Progress Appeal" width="16" height="16" align="absmiddle">
		        </td>
		   	</tr>
		     <tr>
		       <td width="15%" class="formLabel">Pre-Approval Amount </td>
		       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="preApprovalAmt"/> </td>
		   	</tr>
   	
</table>      		




<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
	<tr> <td>&nbsp;</td></tr>
     <tr> <td>&nbsp;</td></tr>
     <tr><td> <strong>NOTE:</strong> An Authorization letter will be sent to the registered email id of Provider & Patient within 30 minutes. <br>
     </td></tr>
</table>
     
<br>
 <!-- S T A R T : Buttons -->
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	    	<tr>
		        <td width="100%" align="center">
					<button type="button" name="Button" accesskey="x" class="olbtnSmall" onClick="javascript:onExit();">E<u>x</u>it</button> &nbsp;
				 </td>
		      	<td width="100%" align="center">
	      	</tr>
	    </table>
		<!-- E N D : Buttons -->
	
	</div>
	<input type="hidden" name="mode" value="">
	<INPUT TYPE="hidden" NAME="leftlink">
	<INPUT TYPE="hidden" NAME="sublink">
	<INPUT TYPE="hidden" NAME="tab">
<%-- 	<INPUT TYPE="hidden" NAME="preAuthSeqID" value="<bean:write name="" property="preAuthSeqID"/>">
	<INPUT TYPE="hidden" NAME="status" value="<bean:write name="" property="statusTypeID"/>">
	<INPUT TYPE="hidden" NAME="authorizationNo" value="<%=authNO%>">
	
	<script type="text/javascript">
	generateLetter();
	</script> --%>
</body>
</html:form>
