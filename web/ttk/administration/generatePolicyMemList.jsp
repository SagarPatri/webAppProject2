
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<script language="javascript" src="/ttk/scripts/administration/policydetail.js"></script>

<html:form action="/EditPoliciesSearchAction.do">

	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>Member List Report</td>
		</tr>
	</table>
	<div class="contentArea" id="contentArea">

	<!-- Start of Form Fields -->
    <fieldset>
	<legend>Report Parameters </legend>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	     <tr>
        	<td width="100%" align="center">Member Status
				<html:select property="memberStatus" styleId="memberStatus" styleClass="selectBox selectBoxMedium" >
		  	 		<html:option value="">All</html:option>
		  	 		<html:option value="POA">Active</html:option>
		  	 		<html:option value="POC">Cancelled</html:option>
		  	 	</html:select> &nbsp;&nbsp;
				<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGeneratemMemReport();"><u>G</u>enerate XL Report</button>&nbsp;
	           	<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	       	</td>
      </tr>	
	</table>
	</fieldset>
	</div>
	<!-- E N D : Form Fields -->

 <html:hidden property="polSeqID" styleId="polSeqID"/>
<input type="hidden" name="mode" value=""/>	
</html:form>
