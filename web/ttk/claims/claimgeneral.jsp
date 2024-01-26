
<%
	/**
	 * @ (#) claimgeneral.jsp July 13, 2015
	 * Project 	     : ProjectX
	 * File          : claimgeneral.jsp
	 * Author        : Nagababu K
	 * Company       : RCS Technologies
	 * Date Created  : July 13, 2015
	 *
	 * @author       :  Nagababu K
	 * Modified by   :  
	 * Modified date :  
	 * Reason        :  
	 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
<%@ page
	import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,com.ttk.dto.usermanagement.UserSecurityProfile"%>
	<!-- added by govind start-->
	<script >
    $(document).ready(function(){
    	qtyAndDaysAlertClm();
    });  	
    </script> 
	<!-- added by govind end-->
<head>
<script language="javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusObj"))%>";
var JS_SecondSubmit=false;
var eventFire = false;
</script>
<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/preauth/history.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/claims/claimgeneral.js"></script>
<script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral-async.js"></script>

</head>
<body>
<% 

UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
Long userseqid = userSecurityProfile.getUSER_SEQ_ID();
String activelink = TTKCommon.getActiveLink(request);
String activetab = TTKCommon.getActiveTab(request);
String vipmemberuseraccesspermissionflag = (String)request.getSession().getAttribute("vipmemberuseraccesspermissionflag");
Long assigntouserseqid = (Long)request.getSession().getAttribute("assigntouserseqid");
%>
	<%
		boolean viewmode = true;
		boolean bEnabled = false;
		boolean viewmode1 = true;
		String ampm[] = {"AM", "PM"};
		boolean submissionMode = false;
		boolean blnAmmendmentFlow = false;
		
		if (TTKCommon.isAuthorized(request, "Edit")) {
			viewmode = false;
			viewmode1 = false;
		}//end of if(TTKCommon.isAuthorized(request,"Edit"))
			//pageContext.setAttribute("preauthType",Cache.getCacheObject("preauthType"));
		pageContext.setAttribute("ampm", ampm);
		//pageContext.setAttribute("hospitalizedFor",Cache.getCacheObject("hospitalizedFor"));
		pageContext.setAttribute("preauthPriority",
				Cache.getCacheObject("preauthPriority"));
		//pageContext.setAttribute("officeInfo", Cache.getCacheObject("officeInfo"));
		pageContext.setAttribute("source", Cache.getCacheObject("source"));
		//pageContext.setAttribute("encounterTypes", Cache.getCacheObject("encounterTypes"));
		//pageContext.setAttribute("OutPatientEncounterTypes", Cache.getCacheObject("OutPatientEncounterTypes"));
		pageContext.setAttribute("encounterStartTypes",
				Cache.getCacheObject("encounterStartTypes"));
		pageContext.setAttribute("encounterEndTypes",
				Cache.getCacheObject("encounterEndTypes"));
		pageContext.setAttribute("dentalTreatmentTypes",
				Cache.getCacheObject("dentalTreatmentTypes"));
		//pageContext.setAttribute("commCode", Cache.getCacheObject("commCode"));
		pageContext.setAttribute("modeOfClaims",
				Cache.getCacheObject("modeOfClaims"));
		//pageContext.setAttribute("conflict", Cache.getCacheObject("conflict"));
		//pageContext.setAttribute("relationshipCode", Cache.getCacheObject("relationshipCode"));
		//pageContext.setAttribute("receivedFrom", Cache.getCacheObject("receivedFrom"));
		//pageContext.setAttribute("claimSubType", Cache.getCacheObject("claimSubType"));
		pageContext.setAttribute("claimStatusList",
				Cache.getCacheObject("claimStatusList"));
		pageContext.setAttribute("systemOfMedicines",
				Cache.getCacheObject("systemOfMedicines"));
		pageContext.setAttribute("accidentRelatedCases",
				Cache.getCacheObject("accidentRelatedCases"));
		pageContext.setAttribute("benefitTypes",
				Cache.getCacheObject("benefitTypes"));
		pageContext.setAttribute("prebenefitTypes",
				Cache.getCacheObject("prebenefitTypes"));

		//pageContext.setAttribute("stateCodeList",Cache.getCacheObject("stateCode"));
		//pageContext.setAttribute("cityCodeList",Cache.getCacheObject("cityCode"));
		pageContext.setAttribute("countryCodeList",
				Cache.getCacheObject("countryCode"));
		pageContext.setAttribute("claimType",
				Cache.getCacheObject("claimType"));
		pageContext.setAttribute("memberClaimFrom",
				Cache.getCacheObject("MemberClaimFromList"));
		pageContext.setAttribute("natureOfConception",
				Cache.getCacheObject("natureOfConception"));
		pageContext.setAttribute("pbmnatureOfConception",
				Cache.getCacheObject("natureOfConception"));
		pageContext.setAttribute("modeofdelivery",
				Cache.getCacheObject("modeofdelivery"));

		pageContext.setAttribute("submissionCatagory",
				Cache.getCacheObject("submissionCatagory"));
		//pageContext.setAttribute("partnerName",Cache.getCacheObject("partnerName"));
		pageContext.setAttribute("paymentTo",Cache.getCacheObject("paymentTo"));
		pageContext.setAttribute("paymentToEmb",Cache.getCacheObject("paymentToEmb"));
		

		//OPD_4_hosptial
		//pageContext.setAttribute("paymentType", Cache.getCacheObject("PaymentType"));
		//OPD_4_hosptial
		//added as per kOC 1285 Change Request  
		//pageContext.setAttribute("domicilaryReasonList", Cache.getCacheObject("domHospReason"));//1285 change request
		//added as per kOC 1285 Change Request

		//pageContext.setAttribute("productName", Cache.getCacheObject("productName"));

		pageContext.setAttribute("viewmode", new Boolean(viewmode));
		boolean netWorkStatus = true;
		boolean memberValid = false;
		boolean auditStatusView=true;
		if("RCR".equals(session.getAttribute("auditStatus"))){
			auditStatusView=false;
		}
		if("CKD".equals(session.getAttribute("auditStatus"))){
			auditStatusView=true;
		}
		
	%>
	<logic:equal value="DTR" property="claimSubmissionType"
		name="frmClaimGeneral">
		<%
			submissionMode = true;
		%>
	</logic:equal>
	<html:form action="/ClaimGeneralAction.do">
		<!-- S T A R T : Page Title -->


		<table align="center" class="pageTitle" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td width="57%">General Details - <bean:write
						name="frmClaimGeneral" property="caption" /></td>
					<!-- 	<td>	  	<a href="#" id="discrepancies" onclick="javascript:onClaimSubmission();"><img src="/ttk/images/ModifiedIcon.gif" alt="Online Claim Submission" width="16" height="16" border="0" align="absmiddle" class="icons"></a>&nbsp;&nbsp;&nbsp;
   									 <img src="/ttk/images/IconSeparator.gif" width="1" height="15" align="absmiddle" class="icons">&nbsp;&nbsp;&nbsp;	  	   
   						 </td> -->
				<td align="right" class="webBoard">&nbsp;<%@ include
						file="/ttk/common/toolbar.jsp"%>
				</td>
			</tr>

		</table>

		<div id="dialogoverlay"></div>
		<div id="dialogbox">
			<div>
				<div id="dialogboxhead"></div>
				<div id="dialogboxbody"></div>
				<div id="dialogboxfoot"></div>
			</div>
		</div>
		<!-- E N D : Page Title -->

		<div class="contentArea" id="contentArea">
			<html:errors />
			<logic:notEmpty name="errorMsg" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/ErrorIcon.gif" title="Error"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="errorMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			<logic:notEmpty name="frmClaimGeneral" property="duplicateClaimAlert">
				<table align="center" class="errorContainer" style="color: yellow;"
					border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmClaimGeneral" property="duplicateClaimAlert" /></td>
					</tr>
				</table>
			</logic:notEmpty>


			<logic:notEmpty name="frmClaimGeneral" property="memCoveredAlert">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Wrning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmClaimGeneral" property="memCoveredAlert" /></td>
					</tr>
				</table>
			</logic:notEmpty>


			<logic:notEmpty name="frmClaimGeneral" property="clinicianWarning">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Wrning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmClaimGeneral" property="clinicianWarning" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			
			<logic:notEmpty name="frmClaimGeneral" property="exceptionalWarningMsg">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" alt="Wrning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmClaimGeneral" property="exceptionalWarningMsg" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			


			<!-- S T A R T : Success Box -->
			<logic:notEmpty name="updated" scope="request">
				<table align="center" class="successContainer" style="display:"
					border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:message
								name="updated" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			<logic:notEmpty name="clinicianStatus" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="clinicianStatus" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>

			<logic:notEmpty name="successMsg" scope="request">
				<table align="center" class="successContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="successMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			<!-- S T A R T : Form Fields -->
			<fieldset>
				<legend>Claim Details</legend>
				<table align="center" class="formContainer" border="0"
					cellspacing="0" cellpadding="0">
						<tr>
					<td class="formLabel">Submission catagory:<span
							class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel"><html:select
								property="processType"
							
								styleClass="selectBox selectBoxMedium"
								disabled="true">
								<html:option value="">Select from list</html:option>
								<html:optionsCollection name="submissionCatagory"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
							<td></td><td></td>
							</tr>
				 <logic:match value="DBL" name="frmClaimGeneral" property="processType">
				 <logic:match value="CNH" property="claimType" name="frmClaimGeneral">
					
					 <tr><td class="formLabel">Payment To :<span class="mandatorySymbol">*</span></td>
					 <td class="textLabel">
							 <html:select
								name="frmClaimGeneral" property="paymentTo" disabled="true" styleId ="paymentToId"
								styleClass="selectBox selectBoxMedium">
								<html:optionsCollection name="paymentTo"
									label="cacheDesc" value="cacheId" />
							</html:select>
					 </td> 
					 <logic:match value="PTN" property="paymentTo" name="frmClaimGeneral">
					
					<td id="partnerIdLabel" class="formLabel">Partner Name:<span class="mandatorySymbol">*</span></td>
					  <td id="partnerId" style="display:inline;" class="textLabel">
					  
					  		<html:select property="partnerName" styleClass="selectBox selectBoxMedium" styleId="partnerName" >
								 <logic:notEmpty name="partnersList" scope="session">
									<html:option value="">Select from list</html:option>
									<html:optionsCollection name="partnersList" value="key" label="value" />
								</logic:notEmpty>
							</html:select>
					  
					 </td>
					 </logic:match>
					 </tr>
				</logic:match>
				</logic:match>
					
					<tr>
						<td width="22%" class="formLabel">Claim No.:</td>
						<td width="30%" class="textLabelBold"><logic:notEmpty
								name="frmClaimGeneral" property="claimNo">
								<bean:write name="frmClaimGeneral" property="claimNo" />
								<%-- [<bean:write name="frmClaimGeneral" property="preAuthNoStatus"/>] --%>
							</logic:notEmpty> <html:hidden name="frmClaimGeneral" property="claimNo" /> <html:hidden
								property="preAuthSeqID" name="frmClaimGeneral" /> 
								<html:hidden property="claimSeqID" styleId="seqId" name="frmClaimGeneral" /> <html:hidden
								property="parentClaimSeqID" name="frmClaimGeneral" /> <html:hidden
								property="authNum" name="frmClaimGeneral" styleId="authNum"/> <html:hidden
								property="enhancedYN" name="frmClaimGeneral" /> <html:hidden
								property="preAuthNoStatus" name="frmClaimGeneral" /></td>

						<!-- This code will never work as we dnt have takaful in our duabi -->
						<logic:equal value="CTM" property="claimType"
							name="frmClaimGeneral">
							<logic:equal name="frmClaimGeneral" value="Y"
								property="takafulYN">
								<td width="22%" class="formlabel">Takaful Claim Ref No.:<span
									class="mandatorySymbol">*</span>
								</td>
								<td><html:text property="takafulClaimRefNo"
										styleClass="textBox textBoxLong" /></td>
							</logic:equal>
						</logic:equal>
						
						<td width="22%" class="formLabel">Common File No.:</td>
						<td width="30%" class="textLabelBold"><bean:write name="frmClaimGeneral" property="commonFileNo"/> </td>
						
					</tr>
					<logic:equal value="DTR" property="claimSubmissionType"
						name="frmClaimGeneral">
						<tr>
							<td width="22%" class="formLabel">Claims Submission Type:</td>
							<td width="30%" class="textLabelBold">RE-SUBMISSION</td>
							<logic:equal value="CTM" property="claimType"
								name="frmClaimGeneral">
								<td class="formLabel" width="19%">Claim From:</td>
								<td class="textLabel"><html:select property="claimFrom"
										styleClass="selectBox selectBoxMedium selectBoxDisabled"
										disabled="true">
										<html:option value="">Select from list</html:option>
										<html:optionsCollection name="memberClaimFrom"
											label="cacheDesc" value="cacheId" />
									</html:select></td>
							</logic:equal>
						</tr>
						
						<tr>
								<td width="22%" class="formLabel">Parent Claim No .:</td>
								<td width="30%" class="textLabelBold"><bean:write	name="frmClaimGeneral" property="parentClaimNo" /></td>
								<td width="22%" class="formLabel">Resubmission Count:</td>
								<td width="30%" class="textLabelBold"><bean:write	name="frmClaimGeneral" property="resubmissionCount" /></td>
						</tr>
						
						<tr>
							<td class="formLabel" colspan="1">Re-Submission Remarks:<span
								class="mandatorySymbol">*</span>
							</td>
							<td class="textLabel" colspan="3"><html:textarea name="frmClaimGeneral" property="remarks" rows="2" cols="80"/></td>
						</tr>
					</logic:equal>
					<logic:equal value="DTC" property="claimSubmissionType"
						name="frmClaimGeneral">
						<tr>
							<td width="22%" class="formLabel">Claims Submission Type:</td>
							<td width="30%" class="textLabelBold">SUBMISSION</td>
							<logic:equal value="CTM" property="claimType"
								name="frmClaimGeneral">
								<td class="formLabel" width="19%">Claim From:</td>
								<td class="textLabel"><html:select property="claimFrom"
										styleClass="selectBox selectBoxMedium" disabled="true">
										<html:option value="">Select from list</html:option>
										<html:optionsCollection name="memberClaimFrom"
											label="cacheDesc" value="cacheId" />
									</html:select></td>
							</logic:equal>
						</tr>
					</logic:equal>

					<tr>
						<td width="22%" class="formLabel">Claim Received Date:</td>
						<td width="30%" class="textLabelBold">
							<table cellspacing="0" cellpadding="0">
								<tr>
									<td><html:text name="frmClaimGeneral"
											property="receiveDate" styleId="receiveDate"
											styleClass="textBox textDate textBoxDisabled" maxlength="10"
											readonly="true" />&nbsp;</td>
									<td><html:text name="frmClaimGeneral"
											property="receiveTime"
											styleClass="textBox textTime textBoxDisabled" maxlength="5"
											readonly="true" />&nbsp;</td>
									<td><html:select name="frmClaimGeneral"
											property="receiveDay" styleClass="selectBox" disabled="true">
											<html:options name="ampm" labelName="ampm" />
										</html:select></td>
									<c:if test="${sessionScope.fastTrackFlag eq 'Y'}">
										<td>&nbsp;&nbsp;
										<td align="left"><img
											title="${ sessionScope.fastTrackMsg}"
											src="/ttk/images/fastTrackImg.gif" width="20" height="20">
										</td>
									</c:if>
								</tr>
							</table>
						</td>
						<td class="formLabel">Claim Type:</td>
						<td width="30%" class="textLabel"><html:select
								property="claimType" styleClass="selectBox selectBoxMedium"
								disabled="true">
								<html:optionsCollection name="claimType" label="cacheDesc"
									value="cacheId" />
							</html:select></td>
					</tr>
		<!-- Addded only for Emabassy -Kishor kumar S H -->
			<logic:equal value="EMBA" property="claimFrom" name="frmClaimGeneral">
					<tr>
						<td colspan="2">&nbsp;</td>
						<td class="formLabel">Payment To: <span class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel">
							<html:select property="paymentToEmb" styleClass="selectBox selectBoxMedium">
							<html:option value="">Select From List</html:option>
							<html:optionsCollection name="paymentToEmb" label="cacheDesc" value="cacheId" />
							</html:select>
						</td>
					</tr>
			</logic:equal>	
			<logic:equal value="LYVR" property="claimFrom" name="frmClaimGeneral">
					<tr>
						<td colspan="2">&nbsp;</td>
						<td class="formLabel">Payment To: <span class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel">
							<html:select property="paymentToLayOver" styleClass="selectBox selectBoxMedium">
							<html:option value="">Select From List</html:option>
							<html:option value="IQI">Individual</html:option>
							<html:option value="IQC">Corporate</html:option>
							</html:select>
						</td>
					</tr>
			</logic:equal>	
			
			
			
			
			
			
			
			
			
					<tr>
						<td width="22%" class="formLabel">Invoice No.:</td>
						<td width="30%" class="textLabel"><html:text
								property="invoiceNo"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
						<td width="22%" class="formLabel">Batch No.:</td>
						<td width="30%" class="textLabelBold"><bean:write
								name="frmClaimGeneral" property="batchNo" /></td>
					</tr>
					<tr>
						<td class="formLabel">System Of Medicine:<span
							class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel"><html:select
								property="systemOfMedicine"
								styleClass="selectBox selectBoxMedium"
								disabled="<%=submissionMode%>">
								<html:optionsCollection name="systemOfMedicines"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
						<td class="formLabel">Accident Related Cases:<span
							class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel"><html:select
								property="accidentRelatedCase"
								styleClass="selectBox selectBoxMedium"
								disabled="<%=submissionMode%>">
								<html:optionsCollection name="accidentRelatedCases"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
					</tr>
					<tr>
						<td class="formLabel">Priority:</td>
						<td class="textLabel"><html:select property="priorityTypeID"
								styleClass="selectBox selectBoxMedium"
								disabled="<%=submissionMode%>">
								<html:option value="">Select From List</html:option>
								<html:optionsCollection name="preauthPriority" label="cacheDesc"
									value="cacheId" />
							</html:select></td>
						<td class="formLabel" width="19%">Network (Y/N):</td>
						<td class="textLabel"><html:select
								property="networkProviderType"
								styleClass="selectBox selectBoxMedium" disabled="true">
								<html:option value="">Select From List</html:option>
								<html:option value="Y">YES</html:option>
								<html:option value="N">NO</html:option>
							</html:select></td>
					</tr>
					<logic:match value="RGL" name="frmClaimGeneral" property="processType">
					<logic:equal value="Y" name="frmClaimGeneral"
						property="networkProviderType">

						<tr>
							<td class="formLabel">Provider Name:<span
								class="mandatorySymbol">*</span></td>
							<td class="textLabel">
								<table>
									<tr>
										<td><bean:define id="providerNameTitle"
												name="frmClaimGeneral" property="providerName"
												type="java.lang.String" /> <html:text
												property="providerName" title="<%=providerNameTitle%>"
												styleId="providerName"
												styleClass="textBox textBoxLarge textBoxDisabled"
												maxlength="60" readonly="true" /></td>
										<td><logic:equal value="CTM" name="frmClaimGeneral"
												property="claimType">
												<a href="#" accesskey="g"
													onClick="javascript:selectProvider()" class="search"> <img
													src="/ttk/images/EditIcon.gif" title="Select Clinician"
													width="16" height="16" border="0" align="absmiddle">&nbsp;
												</a> 
											</logic:equal></td>
									</tr>
								</table>
							</td>
							<td class="formLabel">Provider License Id:</td>
							<td class="textLabel"><html:text property="providerId"
									styleId="providerId"
									styleClass="textBox textBoxLarge textBoxDisabled"
									readonly="true" /> <html:hidden property="providerSeqId"
									styleId="providerSeqId" /> <html:hidden
									property="hiddenHospitalID" /></td>
						</tr>
					</logic:equal>
					<logic:equal value="N" name="frmClaimGeneral"
						property="networkProviderType">
						<tr>
							<td class="formLabel">Provider Name:<span
								class="mandatorySymbol">*</span></td>
							<td class="textLabel"><html:text property="providerName"
									styleId="providerName" styleClass="textBox textBoxLarge"
									maxlength="60" /></td>
							<td class="formLabel"></td>
							<td class="textLabel"></td>
						</tr>
					</logic:equal>
					
					</logic:match>
					
					
				<logic:match value="DBL" name="frmClaimGeneral" property="processType">
					<logic:equal value="Y" name="frmClaimGeneral"
						property="networkProviderType">
						<logic:notMatch value="PTN" property="paymentTo" name="frmClaimGeneral">

						<tr>
							<td class="formLabel">Provider Name:<span
								class="mandatorySymbol">*</span></td>
							<td class="textLabel">
								<table>
									<tr>
										<td><bean:define id="providerNameTitle"
												name="frmClaimGeneral" property="providerName"
												type="java.lang.String" /> <html:text
												property="providerName" title="<%=providerNameTitle%>"
												styleId="providerName"
												styleClass="textBox textBoxLarge textBoxDisabled"
												maxlength="60" readonly="true" /></td>
										<td><logic:equal value="CTM" name="frmClaimGeneral"
												property="claimType">
												<a href="#" accesskey="g"
													onClick="javascript:selectProvider()" class="search"> <img
													src="/ttk/images/EditIcon.gif" title="Select Clinician"
													width="16" height="16" border="0" align="absmiddle">&nbsp;
												</a> 
											</logic:equal></td>
									</tr>
								</table>
							</td>
							<td class="formLabel">Provider Id:</td>
							<td class="textLabel"><html:text property="providerId"
									styleId="providerId"
									styleClass="textBox textBoxLarge textBoxDisabled"
									readonly="true" /> <html:hidden property="providerSeqId"
									styleId="providerSeqId" /> <html:hidden
									property="hiddenHospitalID" /></td>
						</tr>
						</logic:notMatch>
					</logic:equal>
					<logic:equal value="N" name="frmClaimGeneral"
						property="networkProviderType">
						<tr>
							<td class="formLabel">Provider Name:<span
								class="mandatorySymbol">*</span></td>
							<td class="textLabel"><html:text property="providerName"
									styleId="providerName" styleClass="textBox textBoxLarge"
									maxlength="60" /></td>
							<td class="formLabel"></td>
							<td class="textLabel"></td>
						</tr>
					</logic:equal>
					
					
					<logic:equal value="Y" name="frmClaimGeneral"
						property="networkProviderType">
						<logic:match value="PTN" property="paymentTo" name="frmClaimGeneral">
						
						<tr>
							<td class="formLabel">Provider Name:<span
								class="mandatorySymbol">*</span></td>
							<td class="textLabel"><html:text property="providerName"
									styleId="providerName" styleClass="textBox textBoxLarge"
									maxlength="60" /></td>
							<td class="formLabel"></td>
							<td class="textLabel"></td>
						</tr>
						</logic:match>
					</logic:equal>
					
					
					</logic:match>
					
					
					
					<tr>
						<td class="formLabel" width="19%">Mode of Claim:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:select name="frmClaimGeneral"
								property="modeOfClaim" styleClass="selectBox selectBoxMedium"
								styleId="preauthMode" onchange="setValidateIconTitle();"
								disabled="true">
								<html:optionsCollection name="modeOfClaims" label="cacheDesc"
									value="cacheId" />
							</html:select></td>

						<td class="formLabel">Al Koot ID:<span
							class="mandatorySymbol">*</span></td>
						 
						<td class="textLabel" style="margin-left:10PX;">
						<table>
								<tr>
									<td>
						<html:text property="memberId"
								styleId="memberId" styleClass="textBox textBoxLarge"
								style="width:200px;height:16px;" maxlength="60" readonly="true"
								disabled="<%=submissionMode%>" /></td>
							<logic:notEqual value="DTR" property="claimSubmissionType" name="frmClaimGeneral">
								<td><a href="#" accesskey="g"
												onClick="javascript:selectMember()" class="search"> <img
												src="/ttk/images/EditIcon.gif" title="Select Member"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
											<a href="#" accesskey="g"
												onClick="javascript:deleteMember()" class="search"> <img
												src="/ttk/images/DeleteIcon.gif" title="Delete Member"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
								</td>
							</logic:notEqual>		
                            <%if(!activelink.equals("CounterFraudDept")){ %>
							<logic:notEmpty name="frmClaimGeneral" property="claimNo">
								<tr>
									<td>
										<a href="#" onClick="javascript:displayBenefitsDetails()" style="color:blue; font:bold;">Display Of Benefits</a>
									</td>
								</tr>
							</logic:notEmpty>
							<%}%>
					</tr>
					<tr>
						<td class="textLabelBold">Pre-Auths Count:<bean:write name="frmClaimGeneral" property="preAuthCount" /></td>
							 <logic:equal value="0" property="cfdPreauthCount" name="frmClaimGeneral">
									<td class="textLabelBold">CFD Count :<bean:write name="frmClaimGeneral" property="cfdPreauthCount" /> </td>
							</logic:equal>
							<logic:notEqual value="0" property="cfdPreauthCount" name="frmClaimGeneral"> 
									<td class="textLabelBold"><a href="#" onClick="javascript:onPreauthFroudHistory()" style="color:red; font:bold;">CFD Count :<bean:write name="frmClaimGeneral" property="cfdPreauthCount"/></a> </td>
						 </logic:notEqual> 
					</tr>
					<tr>
						<td class="textLabelBold">Claims Count:<bean:write name="frmClaimGeneral" property="clmCount" /></td>
							<logic:equal value="0" property="cfdClaimCount" name="frmClaimGeneral">
						 		<td class="textLabelBold">CFD Count :<bean:write name="frmClaimGeneral" property="cfdClaimCount" /></td>
							</logic:equal>
							<logic:notEqual value="0" property="cfdClaimCount" name="frmClaimGeneral"> 
						 		<td class="textLabelBold"><a href="#" onClick="javascript:onClaimFroudHistory()" style="color:red; font:bold;">CFD Count :<bean:write name="frmClaimGeneral" property="cfdClaimCount" /></a> </td>
							 </logic:notEqual> 
					</tr>
					
							
								<tr>
									<td>
										<a href="#" onClick="javascript:onPolicyTob()" style="color:blue; font:bold;">Policy TOB</a>
									</td>
								</tr>
						</table>
					</td>
				</tr>	
					
					
					
								<tr>	
 <td width="22%" class="formLabel">Relationship:</td>
 <td width="30%" class="textLabelBold">
 <logic:notEmpty name="frmClaimGeneral" property="relationShip">
 <bean:define id="relationShipTemp" property="relationFlag" name="frmClaimGeneral"/>
<%if("Y".equals(relationShipTemp)){ %>	
  <font color="blue">
 <bean:write name="frmClaimGeneral" property="relationShip" />
 </font>
  <%}
else if("N".equals(relationShipTemp)) {%>
  <font color="red">
 <bean:write name="frmClaimGeneral" property="relationShip" />
 </font>
   <%}
else {%>
	<bean:write name="frmClaimGeneral" property="relationShip" />
 <%}%>		
			
 
<%--  <logic:equal name="frmClaimGeneral" property="relationFlag" value="Y">
 <font color="blue">
 <bean:write name="frmClaimGeneral" property="relationShip" />
 </font>
 </logic:equal>
 <logic:notEqual name="frmClaimGeneral" property="relationFlag" value="Y">
  <font color="red">
 <bean:write name="frmClaimGeneral" property="relationShip" />
 </font>
 </logic:notEqual> --%>
</logic:notEmpty> 
</td>
					</tr>		
					
					
					
					
					
					<tr>
						<td class="formLabel">Qatar Id:</td>
						<td width="30%" class="textLabel"><html:text
								property="emirateId" styleId="emirateId" readonly="true"
								styleClass="textBox textBoxLong textBoxDisabled" /></td>
						<td class="formLabel">Patient Name:</td>
						<td width="30%" class="textLabel"><html:text
								property="patientName" styleId="patientName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
					</tr>


					<tr>
						<td class="formLabel">Product Name:</td>
						<td width="30%" class="textLabel"><html:text
								property="productName" styleId="productName" readonly="true"
								styleClass="textBox textBoxLong textBoxDisabled" /></td>
						<td class="formLabel">Authority:</td>
						<td width="30%" class="textLabel"><html:text
								property="payerAuthority" styleId="payerAuthority"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
						<td class="formLabel">Age:</td>
						<td width="30%" class="textLabelBold"><html:text
								property="memberAge" styleId="memberAge"
								styleClass="textBox textBoxSmall textBoxDisabled"
								readonly="true" /></td>
						<td class="formLabel">Gender:</td>
						<td width="30%" class="textLabel"><html:text
								property="patientGender" styleId="patientGender"
								styleClass="textBox textBoxSmall textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
						<td class="formLabel">Policy No.:</td>
						<td width="30%" class="textLabel"><html:text
								property="policyNumber" styleId="policyNumber" readonly="true"
								styleClass="textBox textBoxLong textBoxDisabled" /></td>
						<td class="formLabel"><logic:equal value="COR"
								name="frmClaimGeneral" property="policyType">
			      Corporate Name:
			     </logic:equal></td>
						<td width="30%" class="textLabel"><logic:equal value="COR"
								name="frmClaimGeneral" property="policyType">
								<html:text property="corporateName" styleId="corporateName"
									styleClass="textBox textBoxLarge textBoxDisabled"
									readonly="true" />
							</logic:equal></td>
					</tr>
					<tr>
						<td class="formLabel">insurer Id:</td>
						<td width="30%" class="textLabel"><html:text
								property="payerId" styleId="payerId"
								styleClass="textBox textBoxLong textBoxDisabled" readonly="true" />
							<html:hidden property="insSeqId" styleId="insSeqId" /> <html:hidden
								property="policySeqId" styleId="policySeqId" /></td>
						<td class="formLabel">insurer Name:</td>
						<td width="30%" class="textLabel"><html:text
								property="payerName" styleId="payerName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
						<td class="formLabel">Policy Start Date:</td>
						<td width="30%" class="textLabel"><html:text
								property="policyStartDate" styleId="policyStartDate"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
						<td class="formLabel">Policy End Date:</td>
						<td width="30%" class="textLabel"><html:text
								property="policyEndDate" styleId="policyEndDate"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
					<td class="formLabel">Member Inception Date:</td>
						<td width="30%" class="textLabel"><html:text
								property="clmMemInceptionDate" styleId="clmMemInceptionDate"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
					<td class="formLabel">Member Exit Date:</td>
							<td width="30%" class="textLabel"><html:text
								property="memberExitDate" styleId="memberExitDate"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
						<td class="formLabel">Sum Insured:</td>
						<td width="30%" class="textLabel"><html:text
								property="sumInsured" styleId="sumInsured"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
						<td class="formLabel">Available Sum Insured:</td>
						<td width="30%" class="textLabel"><html:text
								property="availableSumInsured" styleId="availableSumInsured"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
						<td class="formLabel">Nationality:</td>
						<td width="30%" class="textLabel"><html:text
								property="nationality" styleId="nationality"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
						<td class="formLabel">Payment to be made for (Policy Config.):</td>
						<td width="30%" class="textLabel"><html:text property="paymentMadeFor" styleId="paymentMadeFor" styleClass="textBox textBoxMedium textBoxDisabled" readonly="true" />
						</td>		
					</tr>
					
					<tr>
						<td class="formLabel">Eligible Networks:</td>
						<td width="30%" class="textLabel"><html:text
								property="eligibleNetworks"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
						<td class="formLabel">IBAN No.:</td>
						<td class="textLabel"><html:text property="accountNumber"
								styleClass="textBox textBoxLarge textBoxDisabled" maxlength="60"
								readonly="true" /></td>
					</tr>
					
					<tr>
						<td class="formLabel">Bank Name:</td>
						<td width="30%" class="textLabel"> <bean:write name="frmClaimGeneral" property="bankName" /> </td>		
						
					</tr>
					
					<tr>
						<td class="formLabel"></td>
						<td class="formLabel"></td>
						<td class="formLabel">VIP:</td>
						<td width="30%" class="textLabel"><html:text
								indexed="vipYorN" styleId="vipYorN" property="vipYorN"
								styleClass="textBox textBoxSmall textBoxDisabled"
								readonly="true" /></td>
					</tr>
					
					<tr>
						<td class="formLabel">Benefit Type:<span
							class="mandatorySymbol">*</span></td>
							
							
		<logic:equal value="YES" property="exceptionFalg" name="frmClaimGeneral">	
		             <td class="textLabel"><html:select property="benefitType"
								styleId="benefitType" styleClass="selectBox selectBoxMedium"
								onchange="setMaternityMode();">
								<html:option value="">Select from list</html:option>
								<logic:equal value="CTM" name="frmClaimGeneral"
									property="claimType">
									<html:optionsCollection name="benefitTypes" label="cacheDesc"
										value="cacheId" />
								</logic:equal>
								<logic:equal value="CNH" name="frmClaimGeneral"
									property="claimType">
									<html:optionsCollection name="prebenefitTypes"
										label="cacheDesc" value="cacheId" />

								</logic:equal>

							</html:select></td>
							
</logic:equal>		

	<logic:notEqual value="YES" property="exceptionFalg" name="frmClaimGeneral">
								<td class="textLabel"><html:select property="benefitType"
								styleId="benefitType" styleClass="selectBox selectBoxMedium"
								disabled="<%=submissionMode%>" onchange="setMaternityMode();">
								<html:option value="">Select from list</html:option>
								<logic:equal value="CTM" name="frmClaimGeneral"
									property="claimType">
									<html:optionsCollection name="benefitTypes" label="cacheDesc"
										value="cacheId" />
								</logic:equal>
								<logic:equal value="CNH" name="frmClaimGeneral"
									property="claimType">
									<html:optionsCollection name="prebenefitTypes"
										label="cacheDesc" value="cacheId" />

								</logic:equal>

							</html:select></td>
					</logic:notEqual>					
							
						
							
							
					   	<logic:match name="frmClaimGeneral" value="MTI" property="benefitType">
							<td class="formLabel">Maternity Coverage Limit:</td>
							<td><html:select property="matsubbenefit"	styleId="matsubbenefit" styleClass="selectBox selectBoxLarge" >
										<html:option value="N">Maternity Normal Cover</html:option>
										<html:option value="Y">Maternity Complication cover</html:option>
								</html:select></td>
							</logic:match>
							

						 <td class="formLabel" width="19%" colspan="2"><logic:equal
								name="frmClaimGeneral" value="MTI" property="benefitType">
								<table>
									<tr>
										<td class="formLabel">Gravida: <html:text
												name="frmClaimGeneral" property="gravida"
												onkeyup="isNumeric(this);" styleId="gravida"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												disabled="<%=submissionMode%>" />
										</td>
										<td class="formLabel">Para: <html:text
												name="frmClaimGeneral" property="para"
												onkeyup="isNumeric(this);" styleId="para"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												disabled="<%=submissionMode%>" />
										</td>
										<td class="formLabel">Live: <html:text
												name="frmClaimGeneral" property="live"
												onkeyup="isNumeric(this);" styleId="live"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												disabled="<%=submissionMode%>" />
										</td>
										<td class="formLabel">Abortion: <html:text
												name="frmClaimGeneral" property="abortion"
												onkeyup="isNumeric(this);" styleId="abortion"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												disabled="<%=submissionMode%>" />
										</td>
									</tr>
								</table>
							</logic:equal></td> 
					</tr>
					<logic:match value="PBCL" name="frmClaimGeneral" property="modeOfClaim">
					<logic:match name="frmClaimGeneral" value="OMTI" property="benefitType">
					
					<tr>
						<td class="formLabel" width="19%">Nature of Conception:</td>
						<td class="textLabel">
						  <html:select property="natureOfConception" styleId="natureOfConception" styleClass="selectBox selectBoxMedium"  disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<html:optionsCollection name="pbmnatureOfConception" label="cacheDesc" value="cacheId" />
							</html:select></td>
						
					
							<td class="formLabel">Date Of LMP:</td>
							<td><html:text name="frmClaimGeneral"
									property="latMenstraulaPeriod" styleId="latMenstraulaPeriod" styleClass="textBox textDate"
									maxlength="10" disabled="<%=submissionMode%>" /> <A
								NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#"
								onClick="javascript:show_calendar('CalendarObjectPARDate','frmClaimGeneral.pbmnatureOfConception',document.frmClaimGeneral.pbmnatureOfConception.value,'',event,148,178);return false;"
								onMouseOver="window.status='Calendar';return true;"
								onMouseOut="window.status='';return true;"><img
									src="/ttk/images/CalendarIcon.gif" title="Calendar"
									name="empDate" width="24" height="17" border="0"
									align="absmiddle"></a></td>
						</tr>
						
					</logic:match>
					<logic:equal name="frmClaimGeneral" value="IMTI" property="benefitType">
					<tr>
						<td class="formLabel" width="19%">Nature of Conception:<!-- <span class="mandatorySymbol">*</span> --></td>
						<td class="textLabel">
						  <html:select property="natureOfConception" styleId="natureOfConception" styleClass="selectBox selectBoxMedium"  disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<html:optionsCollection name="natureOfConception" label="cacheDesc" value="cacheId" />
							</html:select></td>
						
					
							<td class="formLabel">Date Of LMP: <!-- <span
								class="mandatorySymbol">*</span> --></td>
							<td><html:text name="frmClaimGeneral"
									property="latMenstraulaPeriod" styleId="latMenstraulaPeriod" styleClass="textBox textDate"
									maxlength="10" disabled="<%=submissionMode%>" /> <A
								NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#"
								onClick="javascript:show_calendar('CalendarObjectPARDate','frmClaimGeneral.latMenstraulaPeriod',document.frmClaimGeneral.latMenstraulaPeriod.value,'',event,148,178);return false;"
								onMouseOver="window.status='Calendar';return true;"
								onMouseOut="window.status='';return true;"><img
									src="/ttk/images/CalendarIcon.gif" title="Calendar"
									name="empDate" width="24" height="17" border="0"
									align="absmiddle"></a></td>
						</tr>
					<tr>
						<td class="formLabel" width="19%">Mode of delivery:</td>
						<td class="textLabel">
						  <html:select property="modeofdelivery" styleClass="selectBox selectBoxMedium">
									<html:optionsCollection name="modeofdelivery" label="cacheDesc" value="cacheId" />
							</html:select></td>
						<td></td>
					</tr>
					</logic:equal>
					</logic:match>
					<logic:notMatch value="PBCL" name="frmClaimGeneral" property="modeOfClaim">
					<logic:match name="frmClaimGeneral" value="MTI" property="benefitType">
					
					<tr>
						<td class="formLabel" width="19%">Nature of Conception:<!-- <span class="mandatorySymbol">*</span> --></td>
						<td class="textLabel">
						  <html:select property="natureOfConception" styleId="natureOfConception" styleClass="selectBox selectBoxMedium"  disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<html:optionsCollection name="natureOfConception" label="cacheDesc" value="cacheId" />
							</html:select></td>
						
					
							<td class="formLabel">Date Of LMP: <!-- <span
								class="mandatorySymbol">*</span> --></td>
							<td><html:text name="frmClaimGeneral"
									property="latMenstraulaPeriod" styleId="latMenstraulaPeriod" styleClass="textBox textDate"
									maxlength="10" disabled="<%=submissionMode%>" /> <A
								NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#"
								onClick="javascript:show_calendar('CalendarObjectPARDate','frmClaimGeneral.latMenstraulaPeriod',document.frmClaimGeneral.latMenstraulaPeriod.value,'',event,148,178);return false;"
								onMouseOver="window.status='Calendar';return true;"
								onMouseOut="window.status='';return true;"><img
									src="/ttk/images/CalendarIcon.gif" title="Calendar"
									name="empDate" width="24" height="17" border="0"
									align="absmiddle"></a></td>
						</tr>
					<logic:equal name="frmClaimGeneral" value="IMTI" property="benefitType">
					<tr>
						<td class="formLabel" width="19%">Mode of delivery:</td>
						<td class="textLabel">
						  <html:select property="modeofdelivery" styleClass="selectBox selectBoxMedium">
									<html:optionsCollection name="modeofdelivery" label="cacheDesc" value="cacheId" />
							</html:select></td>
						<td></td>
					</tr>
					</logic:equal>
					</logic:match>
                    </logic:notMatch>
					<tr>
						<td class="formLabel" width="19%">Encounter Type:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:select property="encounterTypeId"
								styleClass="selectBox selectBoxLarge" onchange="setDateMode();"
								disabled="<%=submissionMode%>">
								<logic:notEmpty name="encounterTypes" scope="session">
									<html:optionsCollection name="encounterTypes" value="key"
										label="value" />
								</logic:notEmpty>
							</html:select></td>
							
							<td class="formLabel">Requested Amount:</td>
						<td class="textLabel"><html:text property="requestedAmount"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" />&nbsp; 
								<logic:match property = "processType" name="frmClaimGeneral" value="RGL">
									<html:text property="requestedAmountcurrencyType" styleId="requestedAmountcurrencyType"   styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />&nbsp;<a href="#" onclick="openRadioList('requestedAmountcurrencyType','CURRENCY_GROUP','option');clearConversionRate();">
								</logic:match>
								<logic:notMatch property = "processType" name="frmClaimGeneral" value="RGL">
								<html:text property="requestedAmountcurrencyType" styleId="requestedAmountcurrencyType"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />&nbsp;<a href="#" onclick="openRadioList('requestedAmountcurrencyType','CURRENCY_GROUP','option');clearConversionRate();">
								</logic:notMatch>
		         <logic:notEqual value="DTR" property="claimSubmissionType" name="frmClaimGeneral">
		          	<img src="/ttk/images/search_edit.gif" width="18" height="18" title="Select Currency" border="0" align="bottom" > </a>
		          </logic:notEqual>
		          </td>
		         </tr>
		      <tr>	
				<%-- <logic:equal property="modeOfClaim" name="frmClaimGeneral" value="PLCL">
					<td class="formLabel">Event Reference Number:</td>
						<td width="30%" class="textLabel"><html:text
								property="eventReferenceNo" styleId="eventReferenceNo" readonly="true"
								styleClass="textBox textBoxLong textBoxDisabled" /></td>
				</logic:equal>
				
				<logic:notEqual property="modeOfClaim" name="frmClaimGeneral" value="PLCL">
					<td class="formLabel">Event Reference Number:</td>
						<td width="30%" class="textLabel"><html:text
								property="eventReferenceNo" styleId="eventReferenceNo" 
								styleClass="textBox textBoxLong " /></td>
				</logic:notEqual> --%>
				
				<td class="formLabel">Event Reference Number:</td>
							<td class="textLabel"><html:text property="eventReferenceNo"
									styleClass="textBox textBoxLong"/>
				</td>
							
							<td class="formLabel">Converted Amount:</td>
						<td class="textLabel"> <html:text property="convertedAmount" styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"  />&nbsp;
						<html:text property="currencyType" styleId="totalAmountCurrency" value="QAR"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" /> <!-- <a href="#" onclick="openRadioList('totalAmountCurrency','CURRENCY_GROUP','option')">
		          <img src="/ttk/images/search_edit.gif" width="18" height="18" alt="Select Currency" border="0" align="bottom" > </a> --></td>
					</tr>
					<tr>
					
				 <td></td><td></td>
						<td class="formLabel" width="19%">Conversion Rate:</td> 
						<logic:notEmpty property="conversionRate" name="frmClaimGeneral">
						<logic:equal property="requestedAmountcurrencyType" name="frmClaimGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN" checked="checked"   onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:" > <html:text property="conversionRate" styleId="conversionRate" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" /></div></td> </tr></table> </td>
						</logic:equal>
						<logic:notEqual property="requestedAmountcurrencyType" name="frmClaimGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN" checked="checked" onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:" > <html:text property="conversionRate" styleId="conversionRate" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true"/></div></td> </tr></table> </td>
						</logic:notEqual>
						</logic:notEmpty>
						<logic:empty property="conversionRate" name="frmClaimGeneral">
						<logic:equal property="requestedAmountcurrencyType" name="frmClaimGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN"  onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:none;" > <html:text property="conversionRate" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" /></div></td> </tr></table> </td>
						</logic:equal>
						<logic:notEqual property="requestedAmountcurrencyType" name="frmClaimGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN"  onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:none;" > <html:text property="conversionRate" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" /></div></td> </tr></table> </td>
						</logic:notEqual>
						</logic:empty>
							
					
					</tr>
				

					<logic:equal value="CNH" name="frmClaimGeneral"
						property="claimType">
						<tr>
							<td class="formLabel">Clinician Id:</td>
							<td class="textLabel">
								<table>
									<tr>
										<td><html:text property="clinicianId"
												styleId="clinicianId"
												styleClass="textBox textBoxLarge textBoxDisabled"
												readonly="true" /></td>
									 <logic:notMatch value="PTN" property="paymentTo" name="frmClaimGeneral">
									 <logic:notEqual value="DTR" property="claimSubmissionType" name="frmClaimGeneral">
										<td><a href="#" accesskey="g"
											onClick="javascript:selectClinician()" class="search"> <img
												src="/ttk/images/EditIcon.gif" title="Select Clinician"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
										</a></td>
									</logic:notEqual>
										</logic:notMatch>
									</tr>
								</table>
							<td class="formLabel">Clinician Name:</td>
							<td><html:text property="clinicianName"
									styleClass="textBox textBoxLarge textBoxDisabled"
									styleId="clinicianName" readonly="true" /></td>
						</tr>
						<tr>
								<td class="formLabel">Clinician Specility :</td>
								<td><html:text property="clinicianSpeciality" styleClass="textBox textBoxLarge textBoxDisabled" styleId="clinicianName" readonly="true" /></td>
						</tr>
					</logic:equal>
					<logic:equal value="CTM" name="frmClaimGeneral"
						property="claimType">
						<logic:equal value="Y" name="frmClaimGeneral"
							property="networkProviderType">
							<tr>
								<td class="formLabel">Clinician Id:</td>
								<td class="textLabel">
									<table>
										<tr>
											<td><html:text property="clinicianId"
													styleId="clinicianId"
													styleClass="textBox textBoxLarge textBoxDisabled"
													readonly="true" /></td>
											<td><a href="#" accesskey="g"
												onClick="javascript:selectClinician()" class="search"> <img
													src="/ttk/images/EditIcon.gif" title="Select Clinician"
													width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a></td>
										</tr>
									</table>
								<td class="formLabel">Clinician Name:</td>
								<td><html:text property="clinicianName"
										styleClass="textBox textBoxLarge textBoxDisabled"
										styleId="clinicianName" readonly="true" /></td>
							</tr>
							<tr>
								<td class="formLabel">Clinician Specility :</td>
								<td><html:text property="clinicianSpeciality" styleClass="textBox textBoxLarge textBoxDisabled" styleId="clinicianName" readonly="true" /></td>
							</tr>
						</logic:equal>

						<logic:equal value="N" name="frmClaimGeneral"
							property="networkProviderType">
							<tr>
								<td class="formLabel">Clinician Name:</td>
								<td><html:text property="clinicianName"
										styleClass="textBox textBoxLarge" styleId="clinicianName"
										disabled="<%=submissionMode%>" /></td>
								<td class="formLabel"></td>
								<td class="textLabel"></td>
							</tr>
						</logic:equal>
					</logic:equal>
					<tr>
						<td class="formLabel" width="19%">Encounter Start Type:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:select
								property="encounterStartTypeId"
								styleClass="selectBox selectBoxLarge"
								disabled="<%=submissionMode%>">
								<html:optionsCollection name="encounterStartTypes"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
						<td class="formLabel" width="19%">Encounter End Type:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:select
								property="encounterEndTypeId"
								styleClass="selectBox selectBoxLarge"
								disabled="<%=submissionMode%>">
								<html:optionsCollection name="encounterEndTypes"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
					</tr>
					<tr>
						<td class="formLabel">Start Date:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel">
							<table cellspacing="0" cellpadding="0">
								<tr>
									<td><html:text name="frmClaimGeneral"
											property="admissionDate" styleId="admissionDate" styleClass="textBox textDate"
											maxlength="10" onblur="setEndDate();endDateValidation();clearConversionRate();"
											disabled="<%=submissionMode%>" /> <logic:notEqual
											value="ENHANCEMENT" property="preAuthNoStatus"
											name="frmClaimGeneral">
											<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','frmClaimGeneral.admissionDate',document.frmClaimGeneral.admissionDate.value,'',event,148,178);return false;"
													onkeyup="clearConversionRate();"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar"
												name="empDate" width="24" height="17" border="0"
												align="absmiddle"></a>
										</logic:notEqual>&nbsp;</td>
									<td><html:text name="frmClaimGeneral"
											property="admissionTime" styleClass="textBox textTime"
											maxlength="5" onblur="setEndDate();"
											disabled="<%=submissionMode%>" />&nbsp;</td>
									<td><html:select property="admissionDay"
											name="frmClaimGeneral" styleClass="selectBox"
											onchange="setEndDate();" disabled="<%=submissionMode%>">
											<html:options name="ampm" labelName="ampm" />
										</html:select></td>
								</tr>
							</table>
						</td>
						<td class="formLabel">End Date:<span class="mandatorySymbol">*</span></td>
						<td class="textLabel">
							<table cellspacing="0" cellpadding="0">
								<tr>
									<td><html:text name="frmClaimGeneral" styleId="dischargeDate"
											property="dischargeDate" disabled="<%=submissionMode%>"
											styleClass="textBox textDate"  onblur="endDateValidation();clearConversionRate();"  maxlength="10" /> <logic:match
											name="viewmode" value="false">
											<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="checkCalender();"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar"
												id="discImg" name="empDate" width="24" height="17"
												border="0" align="absmiddle"></a>
										</logic:match>&nbsp;</td>
									<td><html:text name="frmClaimGeneral"
											property="dischargeTime" disabled="<%=submissionMode%>"
											styleClass="textBox textTime" maxlength="5" />&nbsp;</td>
									<td><html:select property="dischargeDay"
											name="frmClaimGeneral" disabled="<%=submissionMode%>"
											styleClass="selectBox">
											<html:options name="ampm" labelName="ampm" />
										</html:select></td>
								</tr>
							</table>
						</td>
					</tr>
					<logic:equal value="CTM" name="frmClaimGeneral"
						property="claimType">

						<tr>
							<td class="formLabel">RI Copar:</td>

							<td class="textLabel">
								<%--  <html:checkbox name="frmClaimGeneral" property="enablericopar" value="Y" /> --%>
								<html:select name="frmClaimGeneral" property="enablericopar"
									styleClass="selectBox selectBoxMedium">
									<%-- 	<html:option value="">Select From List</html:option> --%>
									<html:option value="N">NO</html:option>
									<html:option value="Y">YES</html:option>

								</html:select>

							</td>

							<td class="formLabel">UCR :</td>

							<td class="textLabel">
								<%--  <html:checkbox name="frmClaimGeneral" property="enablericopar" value="Y" /> --%>
								<html:select name="frmClaimGeneral" property="enableucr"
									styleClass="selectBox selectBoxMedium">
									<%-- 		<html:option value="">Select From List</html:option> --%>
									<html:option value="N">NO</html:option>
									<html:option value="Y">YES</html:option>
								</html:select>

							</td>
						</tr>

					</logic:equal>
					<logic:equal value="N" name="frmClaimGeneral"
						property="networkProviderType">
						<tr>
							<td class="formLabel">Country:<span
							class="mandatorySymbol">*</span></td>
							<td class="textLabel"><html:select
									property="providerCountry" name="frmClaimGeneral" styleId="providerCountry"
									styleClass="selectBox selectBoxMoreMedium"
									onchange="getStates();getcurrencyCode();clearConversionRate();" disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<html:optionsCollection name="countryCodeList"
										label="cacheDesc" value="cacheId" />
								</html:select></td>
							<td class="formLabel">City:</td>
							<td class="textLabel"><html:select
									property="providerEmirate" styleId="providerEmirate"
									styleClass="selectBox selectBoxMoreMedium"
									onchange="getAreas()" disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<logic:notEmpty name="providerStates" scope="session">
										<html:optionsCollection name="providerStates" value="key"
											label="value" />
									</logic:notEmpty>
								</html:select></td>
						</tr>
						<tr>
							<td class="formLabel">Area:</td>
							<td class="textLabel"><html:select property="providerArea"
									styleId="providerArea"
									styleClass="selectBox selectBoxMoreMedium"
									disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<logic:notEmpty name="providerAreas" scope="session">
										<html:optionsCollection name="providerAreas" value="key"
											label="value" />
									</logic:notEmpty>
								</html:select></td>
							<td class="formLabel">Po-Box:</td>
							<td class="textLabel"><html:text property="providerPobox"
									styleClass="textBox textBoxLong" disabled="<%=submissionMode%>" />
							</td>
						</tr>
						<tr>
							<td class="formLabel">Address:</td>
							<td class="textLabel"><html:text property="providerAddress"
									styleClass="textBox textBoxLong" disabled="<%=submissionMode%>" />
							</td>
							<td></td>
							<td></td>
						</tr>
					</logic:equal>
					
					
					<logic:notEqual value="N" name="frmClaimGeneral"
						property="networkProviderType">
							 <logic:match value="PTN" property="paymentTo" name="frmClaimGeneral">
						
						<tr>
							<td class="formLabel">Country:<span
							class="mandatorySymbol">*</span></td>
							<td class="textLabel"><html:select
									property="providerCountry" name="frmClaimGeneral" styleId="providerCountry"
									styleClass="selectBox selectBoxMoreMedium"
									onchange="getStates();getcurrencyCode();clearConversionRate();" disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<html:optionsCollection name="countryCodeList"
										label="cacheDesc" value="cacheId" />
								</html:select></td>
							<td class="formLabel">City:</td>
							<td class="textLabel"><html:select
									property="providerEmirate" styleId="providerEmirate"
									styleClass="selectBox selectBoxMoreMedium"
									onchange="getAreas()" disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<logic:notEmpty name="providerStates" scope="session">
										<html:optionsCollection name="providerStates" value="key"
											label="value" />
									</logic:notEmpty>
								</html:select></td>
						</tr>
						<tr>
							<td class="formLabel">Area:</td>
							<td class="textLabel"><html:select property="providerArea"
									styleId="providerArea"
									styleClass="selectBox selectBoxMoreMedium"
									disabled="<%=submissionMode%>">
									<html:option value="">Select From List</html:option>
									<logic:notEmpty name="providerAreas" scope="session">
										<html:optionsCollection name="providerAreas" value="key"
											label="value" />
									</logic:notEmpty>
								</html:select></td>
							<td class="formLabel">Po-Box:</td>
							<td class="textLabel"><html:text property="providerPobox"
									styleClass="textBox textBoxLong" disabled="<%=submissionMode%>" />
							</td>
						</tr>
						<tr>
							<td class="formLabel">Address:</td>
							<td class="textLabel"><html:text property="providerAddress"
									styleClass="textBox textBoxLong" disabled="<%=submissionMode%>" />
							</td>
							<td></td>
							<td></td>
						</tr>
						</logic:match>
					</logic:notEqual>
					
					
					
					<logic:equal value="CNH" name="frmClaimGeneral"
						property="claimType">
						<tr>
							<td class="formLabel">Authorization No.:</td>
							<td class="textLabel">
								<table>
									<tr>
										<td class="textLabelBold">
										<!-- class="search" -->
										
									<%-- 	
										<html:text property="authNum" style="color:blue;" alt="Select Authorization Details"
												styleClass="textBox textBoxLarge textBoxDisabled"
												readonly="true" />
											
											 --%>
											
										<a id="aauthNum" href="#"  onClick="javascript:onSelectPreAuthNo(200,500)"  alt="Select Authorization Details" style="color:blue;">
											<bean:write name="frmClaimGeneral" property="authNum" />
											</a>
										</td>
												
									<logic:notEqual value="DTR" property="claimSubmissionType" name="frmClaimGeneral">
										<td><a href="#" accesskey="g"
											onClick="javascript:selectAuthorizationdetails()"
											class="search"> <img src="/ttk/images/EditIcon.gif"
												title="Select Authorization Details" width="16" height="16"
												border="0" align="absmiddle"></a>
											<a href="#" accesskey="g"
												onClick="javascript:deleteAuthorizationdetails()" class="search"> <img
												src="/ttk/images/DeleteIcon.gif" title="Delete Authorization details"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
												</td>
									</logic:notEqual>
			<logic:equal value="DTR" property="claimSubmissionType" name="frmClaimGeneral">					
			<logic:equal value="YES" property="exceptionFalg" name="frmClaimGeneral">
			<logic:empty property="authNum" name="frmClaimGeneral">
			<td><a href="#" accesskey="g"
											onClick="javascript:selectAuthorizationdetails()"
											class="search"> <img src="/ttk/images/EditIcon.gif"
												alt="Select Authorization Details" width="16" height="16"
												border="0" align="absmiddle"></a>
											<a href="#" accesskey="g"
												onClick="javascript:deleteAuthorizationdetails()" class="search"> <img
												src="/ttk/images/DeleteIcon.gif" title="Delete Authorization details"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
												</td>
			</logic:empty>
			</logic:equal>					
			</logic:equal>						
									
									
									
												
									</tr>
								</table>
							</td>
							
							<td class="formLabel">Pre-Approval Approved Amt :<br> in Incurred Currency </td>
							<td class="textLabel"><html:text property="patIncAmnt" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" />&nbsp;
								<html:text property="patIncCurr" styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />
							</td>
							
						</tr>
						<tr>
							<td class="formLabel">PreApproved Date :</td>
							<td class="textLabel"><html:text property="preAuthReceivedDateAsString" styleClass="textBox textBoxMedium" readonly="true" /></td>
							<td class="formLabel">Pre-Approval Approved Amt :<br> in QAR </td>
							<td class="textLabel"><html:text property="preAuthApprAmt" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" />&nbsp;
												<html:text property="patReqCurr" styleId="patReqCurr"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />
							</td>
						</tr>
					</logic:equal>
					<tr>
						<td class="formLabel">Assigned To:</td>

						<td width="40%" class="textLabelBold" id="assignedToID"><bean:write
								name="frmClaimGeneral"  property="assignedTo"  /></td>
						<td></td>
						<td></td>
					</tr>

					<logic:notEmpty name="frmClaimGeneral" property="authNum">
						<tr>
							<td width="20%" class="formLabel">PreApproved Remarks for the Provider:</td>
							<td colspan="3"><html:textarea property="providerPreAppRemarks" name="frmClaimGeneral" styleClass="textBox textAreaLong textBoxDisabled"
											style="color : #ff0000;font-weight : bold;" readonly="true" />
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td width="20%" class="formLabel">PreApproved Internal Remarks:</td>
							<td colspan="3"><html:textarea property="providerInternalRemarks" name="frmClaimGeneral" styleClass="textBox textAreaLong textBoxDisabled"
											style="color : #ff0000;font-weight : bold;" readonly="true" />
							</td>
							<td></td>
							<td></td>
						</tr>
					</logic:notEmpty>
					
					<logic:equal value="Y" name="frmClaimGeneral"
						property="networkProviderType">
						<logic:notEmpty name="frmClaimGeneral" property="providerId">
							<tr>
								<td width="20%" class="formLabel">Provider Specific
									Remarks:</td>
								<td colspan="3"><html:textarea
										property="providerSpecificRemarks" name="frmClaimGeneral"
										styleClass="textBox textAreaLong textBoxDisabled"
										style="color : #ff0000;font-weight : bold;" readonly="true" /></td>
								<td></td>
								<td></td>
							</tr>
						</logic:notEmpty>
					</logic:equal>

	<tr>
        			<td width="20%" class="formLabel">Member Specific Policy Remarks:</td>
        			<td colspan="3">
        				<html:textarea property="memberSpecificRemarks" name="frmClaimGeneral" styleId="memberSpecificRemarksid" styleClass="textBox textAreaLong textBoxDisabled" style="color : #ff0000;font-weight : bold;" readonly="true"/>
        			</td>
        			<td></td>
        			<td></td>
      			</tr>

                    <logic:match value="PBCL" name="frmClaimGeneral" property="modeOfClaim">
					<logic:equal name="frmClaimGeneral" value="DNTL"
						property="benefitType">
						<tr>
							<td colspan="4">
								<fieldset>
									<legend> Dental </legend>
									<table>
										<tr>
											<td class="formLabel">Select Treatment Type:</td>
											<td class="textLabel"><html:select
													property="treatmentTypeID"
													styleId="treatmentTypeID"
													styleClass="selectBox selectBoxMedium"
													onchange="changeTreatmentType(this)" disabled="<%=submissionMode%>">
													<html:option value="">--Select from List--</html:option>
													<html:optionsCollection name="dentalTreatmentTypes"
														label="cacheDesc" value="cacheId" />
												</html:select></td>
										</tr>

										<tr>
											<td colspan="4"><%@include
													file="/ttk/preauth/dentalDetails.jsp"%>
											</td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>

					</logic:equal>
                    </logic:match>
                    <logic:notMatch value="PBCL" name="frmClaimGeneral" property="modeOfClaim">
					<logic:equal name="frmClaimGeneral" value="DNTL"
						property="benefitType">
						<tr>
							<td colspan="4">
								<fieldset>
									<legend> Dental </legend>
									<table>
										<tr>
											<td class="formLabel">Select Treatment Type: <span class="mandatorySymbol">*</span></td>
											<td class="textLabel"><html:select
													property="treatmentTypeID"
													styleId="treatmentTypeID"
													styleClass="selectBox selectBoxMedium"
													onchange="changeTreatmentType(this)" disabled="<%=submissionMode%>">
													<html:option value="">--Select from List--</html:option>
													<html:optionsCollection name="dentalTreatmentTypes"
														label="cacheDesc" value="cacheId" />
												</html:select></td>
										</tr>

										<tr>
											<td colspan="4"><%@include
													file="/ttk/preauth/dentalDetails.jsp"%>
											</td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>

					</logic:equal>
                    </logic:notMatch>
					<tr>
					<% if(!activelink.equals("CounterFraudDept")) {%>
						<td align="center" colspan="4">
							<button type="button" name="Button2" accesskey="s"
								class="buttons" onMouseout="this.className='buttons'"
								onMouseover="this.className='buttons buttonsHover'"
								onClick="onUserSubmit()">
								<u>S</u>ave
							</button>&nbsp; <!-- button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button-->
						</td>
					<%}%>
					</tr>
				</table>
			</fieldset>
			<logic:notEmpty name="frmClaimGeneral" property="claimNo">
				<fieldset>
					<legend>Diagnosis Details</legend>
					<table align="center" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th>Presenting Complaints:</th>
							<td><html:textarea name="frmClaimGeneral"
									property="presentingComplaints" cols="35" rows="2" /></td>

							<!-- 	//Added For PED Calculation -->
							<td class="formLabel">&nbsp;&nbsp;&nbsp;&nbsp;Duration of
								the Ailment:<span class="mandatorySymbol">*</span>
							</td>
							<td width="20%" class="textLabel"><html:text
									name="frmClaimGeneral" property="durAilment"
									styleClass="textBox textBoxSmall" maxlength="3"
									onkeyup="isNumeric(this);" /> <html:select
									name="frmClaimGeneral" property="durationFlag"
									styleClass="selectBox textBoxSmall">
									<html:option value="DAYS">DAYS</html:option>
									<html:option value="MONTHS">MONTHS</html:option>
									<html:option value="YEARS">YEARS</html:option>
								</html:select>
						</tr>
					</table>
					<br> <br>


					<div align="center" style="align: center;">
						<div
							style="border: 2px solid #a1a1a1; background: #F8F8F8; border-radius: 25px; width: 850px; height: auto; text-align: center;">
							<br>
							<table align="center">
								<tr>
									<td colspan="3"><div id="icdResult1">
											<div id="icdResult2"></div>
										</div></td>
								</tr>
								<tr>
									<td align="center">ICD Code:<span class="mandatorySymbol">*</span></td>
									<td align="center">Principal:<span class="mandatorySymbol">*</span></td>
									<td align="center">Diagnosis Desription:<span
										class="mandatorySymbol">*</span></td>
								</tr>
								<tr>
									<td align="center">
										<table>
											<tr>
												<td><html:text name="frmClaimGeneral"
														property="icdCode" styleId="icdCode"
														styleClass="textBox textBoxSmall" maxlength="20"
														onblur="getIcdCodeDetails();" /></td>
												<td><a href="#" accesskey="g"
													onClick="javascript:selectDiagnosisCode()" class="search">
														<img src="/ttk/images/EditIcon.gif"
														title="Select Diagnosis Code" width="16" height="16"
														border="0" align="absmiddle">&nbsp;
												</a></td>
											</tr>
										</table> <html:hidden name="frmClaimGeneral" property="icdCodeSeqId"
											styleId="icdCodeSeqId" /> <html:hidden
											name="frmClaimGeneral" property="diagSeqId"
											styleId="diagSeqId" />
									</td>
									<td align="center"><html:checkbox name="frmClaimGeneral"
											property="primaryAilment" styleId="primaryAilment" value="Y" />
									</td>
									<td align="center"><html:textarea rows="1" cols="80"
											name="frmClaimGeneral" styleId="diagnosisDescription"
											property="ailmentDescription" /></td>
								</tr>
								<tr>
									<td colspan="3"><logic:empty name="frmClaimGeneral"
											property="diagSeqId">
											<% if(!activelink.equals("CounterFraudDept")) {%>
											<button type="button" name="Button3" accesskey="a"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="addDiagnosisDetails()">
												<u>A</u>dd
											</button>
										<%} %>	
											&nbsp;
			  </logic:empty> <logic:notEmpty name="frmClaimGeneral" property="diagSeqId">
											<button type="button" name="Button3" accesskey="e"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="addDiagnosisDetails()"><u>E</u>dit</button>&nbsp;
			    </logic:notEmpty></td>
								</tr>
							</table>
							<br>
						</div>
					</div>
					<br>
					<ttk:DiagnosisDetails flow="CLM" />
				</fieldset>
				<fieldset>
				<% if(!activelink.equals("CounterFraudDept")) {%>
					<legend>
						Activity Details <a href="#" accesskey="a"
							onClick="javascript:addActivityDetails()"><img
							src="/ttk/images/AddIcon.gif" title="Add Activity Details"
							width="13" height="13" border="0" align="absmiddle"></a>
					</legend>
				<%}else{ %>
				   <legend>
						Activity Details <a href="#" accesskey="a">
						<img
							src="/ttk/images/AddIcon.gif" title="Add Activity Details"
							width="13" height="13" border="0" align="absmiddle"></a>
					</legend>
				<%} %>
				<logic:notEmpty name="frmClaimGeneral" property="lmrpAlert">
						<div style="margin-left:10px;margin-right:10px;text-align:justify;color:red"><bean:write  name="frmClaimGeneral" property="lmrpAlert" /></div>
					<br>
				</logic:notEmpty>
				<logic:equal name="frmClaimGeneral" property="mophDrugYN" value="Y">
					&nbsp;&nbsp;<font color="red"><b>Claimed quantity for the drug is higher than the usual indication. To be reviewed.</b></font>
					<br>
				</logic:equal>	
					<ttk:ActivityDetails flow="CLM" />
					<br>
					<div align="center" style="align: center;">
						<div
							style="border: 2px solid #a1a1a1; background: #F8F8F8; border-radius: 25px; width: 90%; height: auto; text-align: center;">
							<br>
							<logic:notEmpty name="frmClaimGeneral" property="maternityAlert">
								<table align="center" class="errorContainer"
									style="color: yellow;" border="0" cellspacing="0"
									cellpadding="0">
									<tr>
										<td style="color: #3352FF">&nbsp; <bean:write
												name="frmClaimGeneral" property="maternityAlert" /></td>
									</tr>
								</table>
							</logic:notEmpty>

							<table align="center">
							<tr>
						<%
						if(request.getSession().getAttribute("claimVerifiedforClaim").equals("N") && request.getSession().getAttribute("suspectedYesorNOFlag").equals("N")){
						%>
							
						<td align="left"><img title="Internal Remark Status" src="/ttk/images/suspectedimg.png" onclick="fillFraudDeatils()" href="#">
						<!-- <a href="#"  href="#">IMAGE</a> </td> -->
						
					<% 	}else if(request.getSession().getAttribute("claimVerifiedforClaim").equals("N") && request.getSession().getAttribute("suspectedYesorNOFlag").equals("Y")){ 

					%>
							
						<td align="left"><img title="Internal Remark Status"  id="" src="/ttk/images/suspectedcheckedimg.png" onclick="showFraudDeatils()" href="#">
						<!-- <a href="#"  href="#">IMAGE</a> </td> -->
						
						<%}else{
							%>
							
						<td align="left"><img title="Internal Remark Status"  id="" src="/ttk/images/verifiedclaimimg.png" onclick="showFraudDeatils()" href="#">
						
						<%}%>
									<td align="center" colspan="9">Provider Specific Copay 
									<strong>${alProvicerCopay[0] }</strong>%
									and deductible <strong>${alProvicerCopay[1] }</strong> 
									applied <strong>${alProvicerCopay[2] }</strong> (Please refer <strong>Provider Billed Amounts</strong> section in Activity details page.)</td>
							</tr>
							<tr>
									<td align="center" colspan="9">&nbsp;</td>
							</tr>
								<tr>
									<td></td>
									<td align="center">Gross Amount</td>
									<td align="center">Discount</td>
									<td align="center">Discounted Amount</td>
									<td align="center">Patient Share</td>
									<td align="center">Net Amount</td>
									<td align="center">Dis Allowed Amount</td>
									<td align="center">Allowed Amount</td>
								</tr>
								<tr>
								<% if(!activelink.equals("CounterFraudDept")) {%>
									<td align="right">
										<button type="button" accesskey="c" class="buttons"
											onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="calculateClaimAmount()" id="calculate"><u>C</u>alculate</button>
									</td>
									<%}else{ %>
									<td align="right">
										<button type="button" accesskey="c" class="buttons"
											onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"><u>C</u>alculate</button>
									</td>
									<%}%>
									<td align="center"><html:text name="frmClaimGeneral"
											property="grossAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmClaimGeneral"
											property="discountAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmClaimGeneral"
											property="discountGrossAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmClaimGeneral"
											property="patShareAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmClaimGeneral"
											property="netAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmClaimGeneral"
											property="disAllowedAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmClaimGeneral"
											property="approvedAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
								</tr>
								<tr>
									<td>Final Approved Amount:</td>
									<td><html:text name="frmClaimGeneral"
											property="finalApprovedAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /> &nbsp; <html:text property="currencyType" styleId="totalAmountCurrency" value="QAR"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />
											
											<%-- <html:text property="currencyType" styleId="totalAmountCurrency"  styleClass="textBox textBoxSmall" readonly="true" />
											<a href="#" onclick="openRadioList('totalAmountCurrency','CURRENCY_GROUP','option')">
		          <img src="/ttk/images/search_edit.gif" width="18" height="18" alt="Select Currency" border="0" align="bottom" ></a> --%>
			      </td>	
			      <td colspan="3">Converted Approved Amount: &nbsp;
									 &nbsp;<html:text name="frmClaimGeneral"
											property="convertedFinalApprovedAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /> &nbsp; <html:text property="requestedAmountcurrencyType" styleId="requestedAmountcurrencyType"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />
			      </td>
			      <td colspan="3">Payable Amount in USD:
					&nbsp;<html:text name="frmClaimGeneral" property="usdAmount" onkeyup="isNumeric(this);" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" /> 
						&nbsp; <input type="text" value="EX.RT: 3.64" readonly="readonly" class="textBox textBoxSmall textBoxDisabled"/>
			      </td>
			      </tr>
			      <tr>
			      		  <td colspan="1">&nbsp;&nbsp;Payable Amount in EURO:</td>
								<td>&nbsp;<html:text name="frmClaimGeneral" property="euroAmount" onkeyup="isNumeric(this);" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" /> 
			   	  		 </td>
			       		<td colspan="1">&nbsp;&nbsp;&nbsp;&nbsp;Payable Amount in GBP:</td>
								<td>&nbsp;<html:text name="frmClaimGeneral" property="gbpAmount" onkeyup="isNumeric(this);" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" /> 
			      		</td>
			      </tr>
			      <tr><td colspan="8"><hr></td></tr>
									<tr>
									<td class="formLabel" align="left">Final Remarks:</td>
									<td class="textLabel" colspan="5"><html:textarea
											property="finalRemarks"  cols="60" rows="2" styleId="finalRemarks"/></td>
								</tr>
								
								<tr>
									<td class="formLabel" align="left">Internal Remarks:</td>
									<td colspan="5"><html:textarea
											property="internalRemarks" cols="60" rows="2" styleId="internalRemarks"/></td>
								</tr>
								
								<tr>
									<td class="formLabel" align="left">Remarks for the Provider/Member:</td>
									<td colspan="5"><html:textarea
											property="medicalOpinionRemarks" cols="60" rows="2" styleId="medicalOpinionRemarks"/></td>
								</tr>
								
								<tr>
									<td class="formLabel" align="left">Override Main Remarks:</td>
									<td colspan="5"><html:textarea property="overrideMainRemarks" styleId="overrideMainRemarks" readonly="true" style="background-color: #f1f1f1;"
											cols="60" rows="2" /></td>
								</tr>
								<logic:notEmpty name="frmClaimGeneral" property="overrideSubRemarks">
									<tr>
										<td class="formLabel" align="left">Override Sub Remarks:</td>
										<td colspan="5"><html:textarea property="overrideSubRemarks" styleId="overrideSubRemarks" readonly="true" style="background-color: #f1f1f1;"
											cols="60" rows="2" /></td>
									</tr>
								</logic:notEmpty>
								<tr>
									<td class="formLabel" align="left">Override Other Remarks:</td>
									<td colspan="5"><html:textarea property="overrideRemarks" styleId="overrideRemarks" readonly="true" style="background-color: #f1f1f1;"
											cols="60" rows="3" /></td>
								</tr>
								<c:if test="${sessionScope.auditStatus eq 'RCR'}">
								<tr>
									<td class="formLabel" align="left">Concurrent Claim Audit Status:</td>
									<td colspan="5" align="left">	
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="isRechecked" id='recheckedButtonId'>&nbsp;&nbsp; Recheck done
									</td>
								</tr>
								</c:if>
								<c:if test="${sessionScope.auditStatus eq 'CKD' }">
								<tr>
									<td class="formLabel" align="left">Concurrent Claim Audit Status:</td>
									<td colspan="5" align="left">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="isRechecked" id='recheckedButtonId' checked="checked" disabled="disabled">&nbsp;&nbsp; Recheck done
									</td>
								</tr>
								</c:if>
								<c:if test="${sessionScope.auditStatus ne 'NCH'}">
								<tr>
									<td class="formLabel" align="left">Audit Remarks:</td>
									<td colspan="5"><html:textarea property="auditRemarks" cols="60" rows="2" readonly="true" disabled="true"/></td>
								</tr>
								</c:if>
								<c:if test="${sessionScope.auditStatus ne 'CHK'  and sessionScope.auditStatus ne 'NCH'}">
								<tr>
									<td class="formLabel" align="left">Recheck done Remarks:
									<c:if test="${sessionScope.auditStatus eq 'RCR'}">
									<span class="mandatorySymbol">*</span>
									</c:if>
									</td>
									<td colspan="5"><html:textarea
											property="recheckRemarks" cols="60" rows="2" disabled="<%=auditStatusView%>" readonly="<%=auditStatusView%>"/></td>
								</tr>
								</c:if>
								
								<tr><td colspan="8"><hr></td></tr>
								<tr>
								<td align="left">Status:</td>
									<td colspan="3"><html:select property="claimStatus" styleId="claimStatus"
											name="frmClaimGeneral" styleClass="selectBox selectBoxMedium">
											<%-- <html:option value="">Select from list</html:option> --%>
											<html:optionsCollection name="claimStatusList"
												label="cacheDesc" value="cacheId" />
										</html:select>
										
											<logic:equal name="frmClaimGeneral" property="inProgressStatus" value="INP-RES">
													&nbsp;&nbsp;&nbsp;&nbsp; Shortfall Responded <img  src="/ttk/images/AddIcon.gif" title="Enhancement" width="16" height="16">
													</logic:equal>
													<logic:equal name="frmClaimGeneral" property="inProgressStatus" value="INP-ENH">
													&nbsp;&nbsp; Resubmission <img  src="/ttk/images/InprogressAppeal.gif"  width="16" height="16">			
													</logic:equal>
										</td>
										
			<logic:equal value="DTR" property="claimSubmissionType" name="frmClaimGeneral">							
			<logic:notEqual name="frmClaimGeneral" property="referExceptionFalg" value="YES">	
				<td colspan="1">	
			<button type="button" name="Button6" accesskey="e" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="exceptionalHandling()"> <u>R</u>efer Exceptional Handling </button>
			</td>
			</logic:notEqual>						
			</logic:equal>								
							 <%
             if(TTKCommon.isAuthorized(request,"ExceptionalHandlingAccess"))
            {
            %>
            	
	        <td colspan="1">
	        <logic:equal value="DTR" property="claimSubmissionType" name="frmClaimGeneral">	
			<button type="button" name="Button5" accesskey="e" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onExceptionalHandling()"> <u>E</u>xceptional Handling </button>
			</logic:equal>
			</td>
	        <%
	        } 
           %>  			
										
										
										<td colspan="2">&nbsp;&nbsp;Settlement Number:</td>
									<td class="textLabelBold" style="background:;" align="left"><bean:write
										property="claimSettelmentNo" name="frmClaimGeneral" /></td>
								</tr>
								
								<tr>
								<% if(!activelink.equals("CounterFraudDept")) {%>
									<td colspan="8" align="center">
										<button type="button" name="Button1" accesskey="v"
											class="buttons" onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="saveAndCompleteClaim()">
											Sa<u>v</u>e
										</button> &nbsp; <logic:equal value="Y" property="claimCompleteStatus"
											name="frmClaimGeneral">
											<logic:equal value="CNH" name="frmClaimGeneral"
												property="claimType">
												<button type="button" name="Button1" accesskey="g"
													class="buttons" onMouseout="this.className='buttons'"
													onMouseover="this.className='buttons buttonsHover'"
													onClick="generateClaimLetter()">
													<u>G</u>enerate Letter
												</button>&nbsp;
											</logic:equal>
											<logic:equal value="CTM" name="frmClaimGeneral"
												property="claimType">
												<button type="button" name="Button1" accesskey="g"
													class="buttons" onMouseout="this.className='buttons'"
													onMouseover="this.className='buttons buttonsHover'"
													onClick="generateClaimLetter()">
													<u>G</u>enerate Letter
												</button>&nbsp;
											</logic:equal>
											<logic:notEqual value="CNH" name="frmClaimGeneral" 
												property="claimType">
											<button type="button" name="Button1" accesskey="e"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="sendClaimLetter()">
												S<u>e</u>nd
											</button>&nbsp;   
      										</logic:notEqual>		
      	<%			
         	if (TTKCommon.isAuthorized(request, "ClaimOverride")) {
         %>
											<button type="button" name="Button1" accesskey="o"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="overridClaimDetails()">
												<u>O</u>verride
											</button>&nbsp;
       <%
       	}
       %>
										</logic:equal>
									<%} %>
										 
									</td>
									
								</tr>
								<tr>
									<td colspan="8">&nbsp;</td>
								</tr>
								
							<logic:equal name="frmClaimGeneral" property="approvalAlertLog" value="Y">
								<tr>
										<td align="center" colspan="8"><font color=red><b>Note : Member start date and end date is same, do you want to proceed.</b></font></td> 			
								</tr>
							</logic:equal>
		<tr>
		<% if(!activelink.equals("CounterFraudDept")) {%>
		<td><a href="#" onclick="javascript:onUserIcon()"><img title="Assign" src="/ttk/images/assignbutton.png" border="white"></a>
		<%}%>
		</tr>
								
								<tr>
								<td align="right"> 
									Decision Date & Time :
								</td>
								<td class="textLabel"> 
									<bean:write name="frmClaimGeneral" property="decisionDt" />
								</td>
								<td colspan="2">&nbsp;</td>
								<td colspan="1">
			                    	Processed By : 
			                    </td>
			                    <td class="textLabel" colspan="3" align="left"> <bean:write name="frmClaimGeneral" property="processedBy" /> </td>
								</tr>
							</table>
							<br>
						</div>
					</div>
				</fieldset>
				<br>
			 <fieldset>
              <legend>Override Remarks Details</legend>	
				
				<table  border="1" cellspacing="0" cellpadding="0" class="gridWithCheckBox" style="margin-top: 10PX">
				<tr  class="gridHeader">
			<th align="center" style="width:10%;">S.NO</th>
			<th align="center" style="width:10%;">Status</th>
			<th align="center" style="width:10%;">Decision</th>
			<th align="center" style="width:10%;">Decision Time</th>
			<th align="center" style="width:10%;">Decision Date</th>
			<th align="center" style="width:10%;">Req Amt</th>
			<th align="center" style="width:10%;">Net Amt</th>
			<th align="center" style="width:10%;">Alw Amt</th>
			<th align="center" style="width:10%;">Override Main Remarks</th>
			<th align="center" style="width:10%;">Override Other Remarks</th>
			</tr>
			
		<%
java.util.ArrayList<String[]> overRideAlertList=(java.util.ArrayList<String[]>)session.getAttribute("overRideAlertList");
if(overRideAlertList!=null&& overRideAlertList.size() > 0 )
{
for(String[] strDetails:overRideAlertList){
	
%>
	<tr>
	<td align="center">
	 <%-- <a href="#" accesskey="g"  onClick="javascript:edit('<%=strDetails[1] %>');"> --%>
	 <%=strDetails[0] %>
	 <!-- </a> -->
	</td>
	<td align="center">
	<%=strDetails[1] %>
	</td>
	<td align="center">
	<%=strDetails[2] %>
	</td>
	<td align="center">
	<%=strDetails[3] %>
	</td>
	<td align="center">
	<%=strDetails[4] %>
	</td>
	<td align="center">
	<%=strDetails[5] %>
	</td>
	<td align="center">
	<%=strDetails[6] %>
	</td>
	<td align="center">
	<%=strDetails[7] %>
	</td>
	<td align="center">
	<%=strDetails[8] %>
	</td>
	<td align="center">
	<%=strDetails[9] %>
	</td>
	
	</tr>
	<%	
}
}
%>	
				</table>
				</fieldset>
				
				<br>
				<logic:equal value="APR" property="statusCheck" name="frmClaimGeneral">
					<fieldset>
						<legend>
							Claim Payment Details 
							<table align="center" class="formContainer" border="1" cellspacing="0" cellpadding="0"  frame="box" rules="all">
							<tr><td class="gridHeader"> Payee Name </td>
								<td class="gridHeader"> Payment Transfer ref. No.<br>(cheque/NEFT No.)</td>
								<td class="gridHeader"> (Cheque/NEFT Amt.)<br>QAR</td>
								<td class="gridHeader"> Transferred Currency</td>
								<td class="gridHeader"> Incurred Currency</td>
								<td class="gridHeader"> Payment Method</td>
								<td class="gridHeader"> Cheque/NEFT Date</td>
								<td class="gridHeader"> Cheque/NEFT Status</td>
								<!-- <td class="gridHeader"> Consignment No.</td>
								<td class="gridHeader"> Service Provider<br>Name/Helpdesk</td>
								<td class="gridHeader"> Dispatch Date</td> -->
							</tr> 
							<tr>
								<td>${sessionScope.strClaimSettlementDtls[0] }</td>
								<td>${sessionScope.strClaimSettlementDtls[1] }</td>
								<td>${sessionScope.strClaimSettlementDtls[2] }</td>
								<td>${sessionScope.strClaimSettlementDtls[6] }</td>
								<td>${sessionScope.strClaimSettlementDtls[7] }</td>
								<td>${sessionScope.strClaimSettlementDtls[3] }</td>
							 	<td>${sessionScope.strClaimSettlementDtls[4] }</td>
								<td>${sessionScope.strClaimSettlementDtls[5] }</td>
								<%--<td>${strClaimSettlementDtls[8] }</td> --%>
							</tr>
							</table>
						</legend>
					</fieldset>
				</logic:equal>
				
				<br>
				<fieldset>
				<% if(!activelink.equals("CounterFraudDept")) {%>
					<legend>
						Generate ShortFalls <a href="#" accesskey="q"
							onClick="javascript:addClaimShortFalls()"><img
							src="/ttk/images/AddIcon.gif" title="Raise ShortFalls" width="13"
							height="13" border="0"></a>
					</legend>
				<%}else{ %>
				    <legend>
						Generate ShortFalls <a href="#" accesskey="q">
						<img
							src="/ttk/images/AddIcon.gif" title="Raise ShortFalls" width="13"
							height="13" border="0"></a>
					</legend>
				<%} %>
					<ttk:Shortfalls flow="CLM" />
				</fieldset>
				
			</logic:notEmpty>
		</div>
		<INPUT TYPE="hidden" NAME="mode" id="mode" VALUE="">
		<input type="hidden" name="leftlink" value="<%=activelink%>">
		<input type="hidden" name="sublink" value="">
		<input type="hidden" name="tab" value="<%=activetab%>">
		<input type="hidden" name="child" value="">
		<input type="hidden" name="activitySeqId" id="activitySeqId" />
		<input type="hidden" name="activityDtlSeqId" id="activityDtlSeqId" />

		<input type="hidden" name="shortFallSeqId" id="shortFallSeqId" />
		<input type="hidden" name="reforward" id="reforward" />
		<input type="hidden" name="rownum" id="rownum" />
		<input type="hidden" name="override" id="override" value="N" />
		<input type="hidden" name="focusObj"
			value="<%=request.getAttribute("focusObj")%>">
		<input type="hidden" name="clmSeqId" value="<%=request.getSession().getAttribute("clmSeqId")%>">	
		<html:hidden property="letterPath" name="frmClaimGeneral" />
		<html:hidden property="claimCompleteStatus" name="frmClaimGeneral" />
		<html:hidden property="validateIcdCodeYN" name="frmClaimGeneral" />
		<html:hidden property="authType" styleId="authType"
			name="frmClaimGeneral" value="CLM" />
		<html:hidden property="provAuthority" name="frmClaimGeneral" />
		<html:hidden property="claimSettelmentNo" name="frmClaimGeneral" />
		<input type="hidden" name="denailcode" id="denailcode">
		<html:hidden property="benefitTyperef" name="frmClaimGeneral" />
		<html:hidden property="prodPolicySeqId" styleId="prodPolicySeqId"/>
       <html:hidden property="revertFlag" styleId="revertFlag" name="frmClaimGeneral" />
		<script type="text/javascript">
			changeTreatment(document.forms[1].treatmentTypeID.value);
		</script>
		<html:hidden property="memberSeqID" styleId="memberSeqID" />
		<html:hidden property="memberIDValidYN" />
		<html:hidden property="hiddenMemberID" />
		<input type="hidden" name="completedYN" value="<%=request.getSession().getAttribute("completedYN")%>">
		<input type="hidden" name="claimVerifiedforClaim" value="<%=request.getSession().getAttribute("claimVerifiedforClaim")%>">
	    <input type="hidden" name="userSeqId" id="userSeqID" value="<%= userseqid %>">
	    <input type="hidden" name="preauthorclaimswitchType" id="preauthorclaimswitchTypeId" value="<%=request.getSession().getAttribute("preauthorclaimswitchType")%>">
	   <%if(vipmemberuseraccesspermissionflag != null || vipmemberuseraccesspermissionflag != "") {%>
	   	    <input type="hidden" id="vipmemberuseraccesspermissionflagid" value="<%=(String)request.getSession().getAttribute("vipmemberuseraccesspermissionflag")%>">

	   <%} %>
	 
	  <%if(assigntouserseqid != null){ %>
	   	   <input type="hidden" name="assignUserSeqID" id="assigntouserseqid" value="<%=assigntouserseqid%>"/>
	   <%}else{ %>
		<input type="hidden" name="assignUserSeqID" id="assigntouserseqid" value="0"/>
	<%}%>

	   <!--qtyAndDaysAlert added by govind  -->
		<html:hidden property="qtyAndDaysAlert" name="frmClaimGeneral" />
		<html:hidden property="relationShip" name="frmClaimGeneral" />   
		<html:hidden property="relationFlag" name="frmClaimGeneral" /> 
		<html:hidden property="bankDeatailsFlag" name="frmClaimGeneral" />
		<html:hidden property="approvalAlertLog" name="frmClaimGeneral" />
		<html:hidden property="mophDrugYN" styleId="mophDrugYN" name="frmClaimGeneral" />
		<% if(request.getSession().getAttribute("mat_icd_yn") !=null){%>
		<input type="hidden" name="mat_icd_yn" id="mat_icd_yn_id" value="<%=Integer.parseInt(String.valueOf(request.getSession().getAttribute("mat_icd_yn"))) %>"/>
		<%} else{%>
		<input type="hidden" name="mat_icd_yn" id="mat_icd_yn_id" />
		<%} %>
		<% if(request.getSession().getAttribute("mat_icd_yn") !=null){%>
	    <input type="hidden" name="primary" id="primary_id" value="<%=String.valueOf(request.getSession().getAttribute("primary"))%>"/>
	    <%}else{ %>
	    <input type="hidden" name="primary" id="primary_id" />
	    <%} %>
	    
	    <html:hidden property="discountFlag" name="frmClaimGeneral" />
	    <html:hidden property="exceptionFalg" styleId="exceptionFalg" name="frmClaimGeneral" />
	    <html:hidden property="referExceptionFalg" styleId="referExceptionFalg" name="frmClaimGeneral" />
	</html:form>
	
</body>
</html>