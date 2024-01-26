<%
/** @ (#) processedClaimDetails.jsp Oct 27th 2020
 * Project    	 : TTK Healthcare Services
 * File       	 : processedClaimDetails.jsp
 * Author     	 : Deepthi Meesala
 * Company    	 : RCS
 * Date Created	 : Oct 27th 2020
 
 * Modified by   : 
 * Modified date :
 * Reason        :
 */
 %>
 
 <%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>



<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/preauth/history.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/processedcliams/processedClaimsList.js"></SCRIPT>
<script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral-async.js"></script>



<html:form action="/ProcessClaimGeneralAction.do">

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">General Details</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
<div class="contentArea" id="contentArea">
<html:errors/>

<fieldset>
<legend>Member Details</legend>
<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">

<tr>
						
						<td class="formLabel">Member Name:</td>
						<td width="30%" class="textLabel"><html:text
								property="patientName" styleId="patientName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
								
				     <td class="formLabel">Enrolment ID:</td>
						<td width="30%" class="textLabel"><html:text
								property="enrollmentId" styleId="enrolmentId" readonly="true"
								styleClass="textBox textBoxLarge textBoxDisabled" /></td>				
								
</tr>

<tr>
    <td class="formLabel" width="20%">Relationship:</td>
    <td width="30%" class="textLabel"><html:text
								property="relationTypeID" styleId="relationTypeID"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td class="formLabel" width="20%">Member Inception Date: </td>
 <td width="30%" class="textLabel"><html:text
								property="memberInceptionDate" styleId="memberInceptionDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>

	<tr>  
						
						<td class="formLabel">Qatar ID:</td>
						<td width="30%" class="textLabel"><html:text
								property="qatarId" styleId="qatarId"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
								
  <td class="formLabel" width="20%">Marital Status: </td>
  <td width="30%" class="textLabel"><html:text
								property="maritalStatus" styleId="maritalStatus"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
  </tr>

<tr>

<td class="formLabel" width="20%">Gender: </td>
 <td width="30%" class="textLabel"><html:text
								property="patientGender" styleId="patientGender"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
   
<td class="formLabel">Age:</td>
						<td width="30%" class="textLabel"><html:text
								property="memberAge" styleId="memberAge"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>

<tr>

<td class="formLabel" width="20%">Date of Birth: </td>
<td width="30%" class="textLabel"><html:text
								property="dob" styleId="dob"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td class="formLabel" width="20%">Corporate Name: </td>
<td width="30%" class="textLabel"><html:text
								property="corporateName" styleId="corporateName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>


<tr>
<td class="formLabel">Policy No.:</td>
<td width="30%" class="textLabel"><html:text
								property="policyNo" styleId="policyNo"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
	<td></td><td></td>
			
</tr>

<tr>
<td class="formLabel" width="20%">Policy Start Date: </td>
<td width="30%" class="textLabel"><html:text
								property="policyStartDate" styleId="policyStartDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td class="formLabel" width="20%">Policy End Date: </td>
<td width="30%" class="textLabel"><html:text
								property="policyEndDate" styleId="policyEndDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>


<tr>

<td class="formLabel">Sum Insured:</td>
						<td width="30%" class="textLabel"><html:text
								property="sumInsured" styleId="sumInsured"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
								
								
<td class="formLabel">Utilized Sum Insured:</td>
						<td width="30%" class="textLabel"><html:text
								property="utilizeSuminsured" styleId="utilizeSuminsured"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>


<tr>
<td class="formLabel">Available sum Insured:</td>
						<td width="30%" class="textLabel"><html:text
								property="availableSumInsured" styleId="availableSumInsured"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>


<td></td><td></td>
</tr>
</table>
</fieldset>
<fieldset>
<legend>Claim Details</legend>

<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
<tr>
					<td class="formLabel">Submission catagory:</td>
					<td width="30%" class="textLabel"><html:text
								property="processType" styleId="processType"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td class="formLabel">Pre-Auth Number:</td>
<td width="30%" class="textLabel"><html:text
								property="preauthNo" styleId="preauthNo"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Claim No.:</td>
<td width="30%" class="textLabel"><html:text
								property="claimNo" styleId="claimNo"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td class="formLabel">Claim Type:</td>
<td width="30%" class="textLabel"><html:text
								property="claimType" styleId="claimType"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>

<tr>
<td class="formLabel">Submission Type:</td>
<td width="30%" class="textLabel"><html:text
								property="submissionType" styleId="submissionType"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
								
<td width="22%" class="formLabel">Batch No:</td>
<td width="30%" class="textLabel"><html:text
								property="batchNo" styleId="batchNo"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>

<tr>
<td width="22%" class="formLabel">Submission Type Flag:</td>
<td width="30%" class="textLabel"><html:text
								property="submissionTypeFlag" styleId="submissionTypeFlag"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>


<td width="22%" class="formLabel">Claim Received Date:</td>	
<td width="30%" class="textLabel"><html:text
								property="receiveDate" styleId="receiveDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>							
</tr>

<tr>
<td width="22%" class="formLabel">Claim Source:</td>
<td width="30%" class="textLabel"><html:text
								property="claimSource" styleId="claimSource"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Invoice No:</td>
<td width="30%" class="textLabel"><html:text
								property="invoiceNo" styleId="invoiceNo"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>

<tr>
<td width="22%" class="formLabel">Benefit Type:</td>
<td width="30%" class="textLabel"><html:text
								property="benefitType" styleId="benefitType"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
<td width="22%" class="formLabel">Service Date:</td>	
<td width="30%" class="textLabel"><html:text
								property="serviceDate" styleId="serviceDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>

<tr>
<td width="22%" class="formLabel">Encounter Type:</td>
<td width="30%" class="textLabel"><html:text
								property="encounterTypeId" styleId="encounterTypeId"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Discharge Date:</td>	
<td width="30%" class="textLabel"><html:text
								property="dischargeDate" styleId="dischargeDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>


<tr>
<td width="22%" class="formLabel">Admission Date:</td>	
<td width="30%" class="textLabel"><html:text
								property="strAdmissionDate" styleId="strAdmissionDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
 
 <td width="22%" class="formLabel">Completed Date:</td>	
 <td width="30%" class="textLabel"><html:text
								property="completedDate" styleId="completedDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
</tr>


<tr>
 <td width="22%" class="formLabel">Added date:</td>		
 <td width="30%" class="textLabel"><html:text
								property="strAddedDate" styleId="strAddedDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
 
<td width="22%" class="formLabel">Claim Status:</td>
<td width="30%" class="textLabel"><html:text
								property="claimStatus" styleId="claimStatus"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td> 
 

</tr>

</table>
</fieldset>

<fieldset>
<legend>Provider Details</legend>
<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="22%" class="formLabel">Provider Name:</td>
<td width="30%" class="textLabel"><html:text
								property="providerName" styleId="providerName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Clinician ID:</td>
<td width="30%" class="textLabel"><html:text
								property="clinicianId" styleId="clinicianId"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Provider Internal Code:</td>
<td width="30%" class="textLabel"><html:text
								property="providerInternalCode" styleId="providerInternalCode"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Clinician Name:</td>
<td width="30%" class="textLabel"><html:text
								property="clinicianName" styleId="clinicianName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Provider Internal Description:</td>
<td width="30%" class="textLabel"><html:text
								property="providerInternalDesc" styleId="providerInternalDesc"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Partner Name:</td>
<td width="30%" class="textLabel"><html:text
								property="partnerName" styleId="partnerName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Empanelment ID:</td>
<td width="30%" class="textLabel"><html:text
								property="empanelmentId" styleId="empanelmentId"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td></td><td></td>
</tr>

</table>
</fieldset>

<ttk:ProcessedClaimDiagnosisDetails  />
 
<ttk:ProcessedClaimActivityDetails  /> 



<fieldset>
<legend>Finance Details</legend>

<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="22%" class="formLabel">Settlement Number:</td>
<td width="30%" class="textLabel"><html:text
								property="settlementNO" styleId="settlementNO"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Provider Country:</td>
<td width="30%" class="textLabel"><html:text
								property="providerCountry" styleId="providerCountry"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Payment Status:</td>
<td width="30%" class="textLabel"><html:text
								property="paymentStatus" styleId="paymentStatus"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Requested amount Original:</td>
<td width="30%" class="textLabel"><html:text
								property="reqAmnt" styleId="reqAmnt"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Cheque Number:</td>
<td width="30%" class="textLabel"><html:text
								property="chequeNo" styleId="chequeNo"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Approved amount Original:</td>
<td width="30%" class="textLabel"><html:text
								property="approvedAmnt" styleId="approvedAmnt"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Cheque Date:</td>
<td width="30%" class="textLabel"><html:text
								property="chequeDate" styleId="chequeDate"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Original Currency:</td>
<td width="30%" class="textLabel"><html:text
								property="originalCurrency" styleId="originalCurrency"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Override (Y/N):</td>
<td width="30%" class="textLabel"><html:text
								property="overrideYN" styleId="overrideYN"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Audit Status:</td>
<td width="30%" class="textLabel"><html:text
								property="auditStatus" styleId="auditStatus"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Claim Requested amount QAR:</td>
<td width="30%" class="textLabel"><html:text
								property="clmReqAmntQAR" styleId="clmReqAmntQAR"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td width="22%" class="formLabel">Claim Aproved amount QAR:</td>
<td width="30%" class="textLabel"><html:text
								property="clmApprAmntQAR" styleId="clmApprAmntQAR"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

</tr>
<tr>
<td width="22%" class="formLabel">Total Disallowed amount:</td>
<td width="30%" class="textLabel"><html:text
								property="totalDisAllowedAmnt" styleId="totalDisAllowedAmnt"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>

<td></td><td></td>
</tr>
</table>
</fieldset>
<fieldset>
<legend>Remarks section</legend>
<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
<tr>
 <td width="22%" class="formLabel">Internal Remark Status:</td>
 <td width="30%" class="textLabel"><html:textarea property="internalRemarkStatus" styleId="internalRemarkStatus" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>
	
<td width="22%" class="formLabel">Provider Specific remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="providerSpecificRemarks" styleId="providerSpecificRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>

</tr>
<tr>
 <td width="22%" class="formLabel">Internal Remark Description:</td>
 <td width="30%" class="textLabel"><html:textarea property="internalRemakDesc" styleId="internalRemakDesc" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>
	
<td width="22%" class="formLabel">Member specific Policy Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="memberSpecificPolicyRemarks" styleId="memberSpecificPolicyRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>

</tr>
<tr>
 <td width="22%" class="formLabel">Final Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="finalRemarks" styleId="finalRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>
	
<td width="22%" class="formLabel">Resubmission Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="resubmissionRemarks" styleId="resubmissionRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>

</tr>
<tr>
 <td width="22%" class="formLabel">Internal Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="internalRemarks" styleId="internalRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>
	
<td width="22%" class="formLabel">Audit Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="auditRemarks" styleId="auditRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>

</tr>
<tr>
 <td width="22%" class="formLabel">Remarks for the Provider/Member:</td>
 <td width="30%" class="textLabel"><html:textarea property="remarksProviderMem" styleId="remarksProviderMem" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>
	
<td width="22%" class="formLabel">Recheck Done Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="recheckRemarks" styleId="recheckRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>

</tr>
<tr>
 <td width="22%" class="formLabel">Override Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="overrideRemarks" styleId="overrideRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>
	
<td width="22%" class="formLabel">Alkoot Remarks:</td>
 <td width="30%" class="textLabel"><html:textarea property="alkootRemarks" styleId="alkootRemarks" styleClass="textBox textAreaMedium" readonly="true" disabled="true"/></td>

</tr>

</table>
</fieldset>
<fieldset>
<legend>Processor Details</legend>
<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
<tr>
					<td class="formLabel">Data Entry By:</td>
					<td width="30%" class="textLabel">
					<html:text
								property="dataEntryBy" styleId="dataEntryBy"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" />
					
					<%-- <html:select property="processType" styleClass="selectBox selectBoxMedium" disabled="true">
					<html:option value="">Select from list</html:option>
					<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheId" />
					</html:select> --%>
							
							
							</td>
							
							
			<td class="formLabel">Last Updated By:</td>
					<td width="30%" class="textLabel">
					<html:text
								property="lastUpdatedBy" styleId="lastUpdatedBy"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" />
							</td>							
</tr>

<tr>
<td class="formLabel">Processed By:</td>
					<td width="30%" class="textLabel">
					<html:text
								property="processedBy" styleId="processedBy"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" />
							</td>	

</tr>

</table>
</fieldset>



<!-- 

<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:20px; margin-bottom:40px;">
	<tr>
	<td></td>
	
	<td colspan="2" align="center">
	
	 <button type="button" name="Button" accesskey="b" class="buttons"  style="margin-left: 10PX;"  onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>&nbsp;
	</td>
	<td></td>
	<td></td>
	</tr>
	</table>

 -->


</div>

	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
<html:hidden property="lngClaimSeqID" styleId="seqId" name="frmProcessedClaimGeneral" /> 

</html:form>

 
 