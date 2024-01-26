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
    	qtyAndDaysAlert();
    });  	
    </script> 
<!-- added by govind end-->


<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script language="javascript" src="/ttk/scripts/jquery.autocomplete.js"></script>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral.js"></script>
<script type="text/javascript"
	src="/ttk/scripts/preauth/preauthgeneral-async.js"></script>
<script>
	//jQuery(function(){
	//    $("#icdCodes").autocomplete("/asynchronAction.do?mode=getIcdCodes");

	//});  
	//jQuery(function(){
	//$("#diagnosisDescription").autocomplete("/asynchronAction.do?mode=getDiagnosisDesc");

	//}); 
	function resetMemSeqId() {
		if (!(document.forms[1].validateMemId.value === document.forms[1].memberId.value)) {
			document.forms[1].memberSeqID.value = "";
		}
	}
</script>
<script language="javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusObj"))%>";
var eventFire = false;
</script>

<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>	

</head>
<body>

	<%
	String activelink = TTKCommon.getActiveLink(request);
	String activetab = TTKCommon.getActiveTab(request);
	
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	Long userseqid = userSecurityProfile.getUSER_SEQ_ID();
	String vipmemberuseraccesspermissionflag=null;
	if(request.getSession().getAttribute("vipmemberuseraccesspermissionflag")!=null){
    vipmemberuseraccesspermissionflag = (String)request.getSession().getAttribute("vipmemberuseraccesspermissionflag");
	}
	Long assigntouserseqid=null;
    if(request.getSession().getAttribute("assigntouserseqid")!=null){
	 assigntouserseqid = (Long)request.getSession().getAttribute("assigntouserseqid");
	}
	
		boolean viewmode = true;
		boolean bEnabled = false;
		boolean viewmode1 = true;
		String ampm[] = { "AM", "PM" };
		boolean enhancedViewMode = false;
		boolean blnAmmendmentFlow = false;
		if (TTKCommon.isAuthorized(request, "Edit")) {
			viewmode = false;
			viewmode1 = false;
		}//end of if(TTKCommon.isAuthorized(request,"Edit"))
		pageContext.setAttribute("preauthReasonList",
				Cache.getCacheObject("preauthReasonList"));
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
		pageContext.setAttribute("dentalTreatmentTypes", Cache.getCacheObject("dentalTreatmentTypes"));
		//pageContext.setAttribute("commCode", Cache.getCacheObject("commCode"));

		//pageContext.setAttribute("conflict", Cache.getCacheObject("conflict"));
		//pageContext.setAttribute("relationshipCode", Cache.getCacheObject("relationshipCode"));
		//pageContext.setAttribute("receivedFrom", Cache.getCacheObject("receivedFrom"));
		//pageContext.setAttribute("claimSubType", Cache.getCacheObject("claimSubType"));
		
		pageContext.setAttribute("preauthStatusList",
				Cache.getCacheObject("preauthStatusList"));
		pageContext.setAttribute("systemOfMedicines",
				Cache.getCacheObject("systemOfMedicines"));
		pageContext.setAttribute("accidentRelatedCases",
				Cache.getCacheObject("accidentRelatedCases"));
		pageContext.setAttribute("benefitTypes",
				Cache.getCacheObject("prebenefitTypes"));
		//pageContext.setAttribute("stateCodeList",Cache.getCacheObject("stateCode"));
		//pageContext.setAttribute("cityCodeList",Cache.getCacheObject("cityCode"));
		pageContext.setAttribute("countryCodeList",
				Cache.getCacheObject("countryCode"));
		pageContext.setAttribute("natureOfConception",	Cache.getCacheObject("natureOfConception"));
		pageContext.setAttribute("modeofdelivery",	Cache.getCacheObject("modeofdelivery"));

		pageContext.setAttribute("submissionCatagory",
				Cache.getCacheObject("submissionCatagory"));
		
	//	pageContext.setAttribute("processType", Cache.getCacheObject("processType"));
		//OPD_4_hosptial
		//pageContext.setAttribute("paymentType", Cache.getCacheObject("PaymentType"));
		//OPD_4_hosptial
		//added as per kOC 1285 Change Request  
		//pageContext.setAttribute("domicilaryReasonList", Cache.getCacheObject("domHospReason"));//1285 change request
		//added as per kOC 1285 Change Request
		pageContext.setAttribute("viewmode", new Boolean(viewmode));
		//pageContext.setAttribute("partnerName",Cache.getCacheObject("partnerName"));
		
		boolean netWorkStatus = true;
	%>
	<logic:equal value="ENHANCEMENT" property="preAuthNoStatus"
		name="frmPreAuthGeneral">
		<%
			enhancedViewMode = true;
		%>
	</logic:equal>	
	<html:form action="/PreAuthGeneralAction.do">
		<!-- S T A R T : Page Title -->
		<table align="center" class="pageTitle" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td width="40%">General Details - <bean:write
						name="frmPreAuthGeneral" property="caption" /></td>
						<%-- <td width="15%">AssignDate:<bean:write name="frmPreAuthGeneral" property="assignDate"/></td> --%>
					<logic:equal property="autoAssignYn" name="frmPreAuthGeneral" value="Y">
					<td width="17%">Remaining Time:<span  id="demo"></span></td>
					</logic:equal>
					<% if(request.getSession().getAttribute("preauthviewfromdashbord")!= null && request.getSession().getAttribute("preauthviewfromdashbord").equals("Y") ){ %>	
					<td width="10%"><a href="javascript:closePreauth();">BackToDashboard</a></td>
						<%} %>
				<td align="right" class="webBoard">&nbsp; 
				<%-- <logic:notEmpty
						name="frmPreAuthGeneral" property="preAuthSeqID">
						<%@ include file="/ttk/common/toolbar.jsp"%>
					</logic:notEmpty> --%>
					
					<logic:empty property="preauthPartnerRefId" name="frmPreAuthGeneral">
					<%@ include file="/ttk/common/toolbar.jsp"%>
					</logic:empty>
					<logic:notEmpty property="preauthPartnerRefId" name="frmPreAuthGeneral">
					<%@ include file="/ttk/common/toolbar.jsp"%>
					</logic:notEmpty>
				</td>
			</tr>
		</table>


<div id="dialogoverlay"></div>
<div id="dialogbox" style="width:390px;">
  <div>
    <div id="dialogboxhead"></div>
    <div id="dialogboxbody"></div>
    <div id="dialogboxfoot"></div>
  </div>
</div>


		<!-- E N D : Page Title -->

		<div class="contentArea" id="contentArea">
			<html:errors/>
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
			<logic:notEmpty name="frmPreAuthGeneral"
				property="duplicatePreauthAlert">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Warning"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="frmPreAuthGeneral" property="duplicatePreauthAlert" /></td>
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
				<legend>PreApproval Details</legend>
				<table align="center" class="formContainer" border="0"
					cellspacing="0" cellpadding="0">
					<%-- <tr>
				
					
					<td class="formLabel">Process Type:<span class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel"><html:select	property="processType" styleClass="selectBox selectBoxMoreMedium" 	disabled="<%=enhancedViewMode%>">
								<html:select property="">Select from list</html:select>
								<html:optionsCollection name="processType" label="cacheDesc" value="cacheId" />
							</html:select></td>
					</tr> --%>
		<tr>
				<%-- <logic:equal property="preAuthRecvTypeID" name="frmPreAuthGeneral" value="PLPR">
					<td class="formLabel">Event Reference Number:</td>
						<td width="30%" class="textLabel"><html:text
								property="eventReferenceNo" styleId="eventReferenceNo" readonly="true"
								styleClass="textBox textBoxLong textBoxDisabled" /></td>
				</logic:equal>
				<logic:notEqual property="preAuthRecvTypeID" name="frmPreAuthGeneral" value="PLPR">
					<td class="formLabel">Event Reference Number:</td>
						<td width="30%" class="textLabel"><html:text
								property="eventReferenceNo" styleId="eventReferenceNo" 
								styleClass="textBox textBoxLong " /></td>
				</logic:notEqual>	 --%>
				
				<td class="formLabel">Event Reference Number:</td>
						<td width="30%" class="textLabel"><html:text
								property="eventReferenceNo" styleId="eventReferenceNo"
								styleClass="textBox textBoxLong"  disabled="<%=enhancedViewMode%>"/></td>
				
			</tr>
			<tr>
					<td class="formLabel">Submission catagory:<span
							class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel"><html:select
								property="processType" styleId="processType"
							onchange="onchangeProcessType()"
								styleClass="selectBox selectBoxMoreMedium"
								disabled="<%=enhancedViewMode%>">
								<html:option value="">Select from list</html:option>
								<html:optionsCollection name="submissionCatagory"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
						<!-- 	<td></td><td></td> -->
							
						<logic:equal value="DBL" property="processType" name="frmPreAuthGeneral">
						<td class="formLabel" id="partnerSelectLabel" style="">Partner Name:</td>
								<td width="30%" class="textLabel" id="partnerSelectBox" style="">
								<html:select property="partnerName" styleClass="selectBox selectBoxMedium" styleId="partnerName" disabled="<%=enhancedViewMode%>">
								 <logic:notEmpty name="partnersList" scope="session">
									<html:option value="">Select from list</html:option>
									<html:optionsCollection name="partnersList" value="key" label="value" />
								</logic:notEmpty>
								</html:select>
						</td>
						</logic:equal>
						
						<logic:notEqual value="DBL" property="processType" name="frmPreAuthGeneral">
						<td class="formLabel" id="partnerSelectLabel" style="display:none">Partner Name:</td>
								<td width="30%" class="textLabel" id="partnerSelectBox" style="display:none"><html:select property="partnerName" styleClass="selectBox selectBoxMedium" styleId="partnerName" disabled="<%=enhancedViewMode%>">
								 <logic:notEmpty name="partnersList" scope="session">
									<html:option value="">Select from list</html:option>
									<html:optionsCollection name="partnersList" value="key" label="value" />
								</logic:notEmpty>
								</html:select>
						</td>
						</logic:notEqual>
							</tr>
					
					<tr>
						<td width="22%" class="formLabel">Pre-Approval No.:</td>
						<td width="30%" class="textLabelBold"><logic:notEmpty
								name="frmPreAuthGeneral" property="preAuthNo">
								<bean:write name="frmPreAuthGeneral" property="preAuthNo" />[<bean:write
									name="frmPreAuthGeneral" property="preAuthNoStatus" />]
			    </logic:notEmpty></td>
						<td class="formLabel">System Of Medicine:<span
							class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel"><html:select
								property="systemOfMedicine"
								styleClass="selectBox selectBoxMoreMedium"
								disabled="<%=enhancedViewMode%>">
								<html:optionsCollection name="systemOfMedicines"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
					</tr>
					<tr>
						<td width="22%" class="formLabel">PreApproval Request Received Date:<span class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabelBold">
							<table cellspacing="0" cellpadding="0">	
								<logic:empty name="frmPreAuthGeneral" property="preAuthNo">
								<tr>
									<td>
										<html:text name="frmPreAuthGeneral" property="receiveDate" styleClass="textBox textDate" maxlength="10" disabled="false" readonly="false" />
										<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.receiveDate',document.frmPreAuthGeneral.receiveDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>&nbsp;
									</td>
									<td>
										<html:text name="frmPreAuthGeneral" property="receiveTime" styleClass="textBox textTime" maxlength="5" disabled="false" readonly="false" />&nbsp;
									</td>
									<td>
										<html:select name="frmPreAuthGeneral" property="receiveDay" styleClass="selectBox" disabled="false" readonly="false" >
											<html:options name="ampm" labelName="ampm" />
										</html:select>
									</td>
								</tr>
								</logic:empty>
								<logic:notEmpty name="frmPreAuthGeneral" property="preAuthNo">
										<logic:equal name="frmPreAuthGeneral" property="preAuthNoStatus" value="ENHANCEMENT">
											<logic:empty name="frmPreAuthGeneral" property="receiveDate">
												<tr>
													<td>
														<html:text name="frmPreAuthGeneral" property="receiveDate" styleClass="textBox textDate" maxlength="10" disabled="false" readonly="false" />
														<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.receiveDate',document.frmPreAuthGeneral.receiveDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>&nbsp;
													</td>
													<td>
														<html:text name="frmPreAuthGeneral" property="receiveTime" styleClass="textBox textTime" maxlength="5" disabled="false" readonly="false" />&nbsp;
													</td>
													<td>
														<html:select name="frmPreAuthGeneral" property="receiveDay" styleClass="selectBox" disabled="false" readonly="false" >
															<html:options name="ampm" labelName="ampm" />
														</html:select>
													</td>
												</tr>
											</logic:empty>
											<logic:notEmpty name="frmPreAuthGeneral" property="receiveDate">
												<tr>
												<%if(request.getAttribute("oraerrormsg")!=null && request.getAttribute("oraerrormsg").equals("enhancement.date.not.lessthan.original.date")) {%>
												<td>
														<html:text name="frmPreAuthGeneral" property="receiveDate" styleId="receivedDateId" styleClass="textBox textDate" maxlength="10" />
														<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.receiveDate',document.frmPreAuthGeneral.receiveDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>&nbsp;
													</td>
													<td>
														<html:text name="frmPreAuthGeneral" property="receiveTime" styleId="receivedTimeId" styleClass="textBox textTime" maxlength="5"/>&nbsp;
													</td>
													<td>
														<html:select name="frmPreAuthGeneral" property="receiveDay" styleId="receivedDayId" styleClass="selectBox">
															<html:options name="ampm" labelName="ampm" />
														</html:select>
													</td>
												<%}else{ %>
													<td>
														<html:text name="frmPreAuthGeneral" property="receiveDate" styleId="receiveDateId" styleClass="textBox textDate" maxlength="10" disabled="true" readonly="true" />
														<!-- <A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.receiveDate',document.frmPreAuthGeneral.receiveDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>&nbsp; -->
													</td>
													<td>
														<html:text name="frmPreAuthGeneral" property="receiveTime" styleId="receiveTimeId" styleClass="textBox textTime" maxlength="5" disabled="true" readonly="true" />&nbsp;
													</td>
													<td>
														<html:select name="frmPreAuthGeneral" property="receiveDay" styleId="receiveDayId" styleClass="selectBox" disabled="true" readonly="true" >
															<html:options name="ampm" labelName="ampm" />
														</html:select>
													</td>
												<%} %>	
												</tr>
											</logic:notEmpty>
										</logic:equal>
										<logic:notEqual name="frmPreAuthGeneral" property="preAuthNoStatus" value="ENHANCEMENT">
												<tr>
													<td>
														<html:text name="frmPreAuthGeneral" property="receiveDate" styleClass="textBox textDate" maxlength="10" disabled="true" readonly="true" />
														<!-- <A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.receiveDate',document.frmPreAuthGeneral.receiveDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>&nbsp; -->
													</td>
													<td>
														<html:text name="frmPreAuthGeneral" property="receiveTime" styleClass="textBox textTime" maxlength="5" disabled="true" readonly="true" />&nbsp;
													</td>
													<td>
														<html:select name="frmPreAuthGeneral" property="receiveDay" styleClass="selectBox" disabled="true" readonly="true" >
															<html:options name="ampm" labelName="ampm" />
														</html:select>
													</td>
												</tr>	
										</logic:notEqual>
								</logic:notEmpty>			
							</table>
						</td>
						<td class="formLabel">Accident Related Cases:<span
							class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel"><html:select
								property="accidentRelatedCase"
								styleClass="selectBox selectBoxMoreMedium"
								disabled="<%=enhancedViewMode%>">
								<html:optionsCollection name="accidentRelatedCases"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
					</tr>
				
					<logic:equal value="Y" property="appealYN" name="frmPreAuthGeneral">
						<tr>
								<td>First Preapproval Received Date :	</td>
								<td class="textLabel" colspan="2"> <bean:write name="frmPreAuthGeneral" property="appealReceivedDt" />
								</td>
								<td></td>
						</tr>
					</logic:equal>
					<tr>
						<td class="formLabel" width="19%">Mode of PreApproval Request:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel">
						
						<logic:equal value="PLPR" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							Provider Login
						</logic:equal>
						<logic:equal value="PBML" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							PBM Login
						</logic:equal>
						<logic:equal value="PTRP" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							PARTNER LOGIN
						</logic:equal>
					<logic:notEqual value="PBML" property="preAuthRecvTypeID" name="frmPreAuthGeneral">	
						<logic:notEqual value="PLPR" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
						<logic:notEqual value="PTRP" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
						<html:select name="frmPreAuthGeneral"
									property="preAuthRecvTypeID"
									styleClass="selectBox selectBoxMoreMedium" styleId="preauthMode"
									onchange="setValidateIconTitle();"
									disabled="<%=enhancedViewMode%>">
									<html:optionsCollection name="source" label="cacheDesc"
										value="cacheId" />
								</html:select>
								</logic:notEqual>
							</logic:notEqual>	
					</logic:notEqual>
						</td>
						<td class="formLabel">Al Koot ID:<span class="mandatorySymbol">*</span>
						 <td class="textLabel" style="margin-left:10PX;"> 
							<table>
								<tr>
									<td colspan="2"><div id="memberIdResult1">
											<div id="memberIdResult2"></div>
										</div></td>
								</tr>
								<tr>
									<td><html:text property="memberId" styleId="memberId"
											styleClass="textBox textBoxLarge"
											style="width:200px;height:16px;" maxlength="60" readonly="true"
											disabled="<%=enhancedViewMode%>" /></td>
									<td><logic:notEqual value="ENHANCEMENT"
											property="preAuthNoStatus" name="frmPreAuthGeneral">
											<a href="#" accesskey="g"
												onClick="javascript:selectMember(this)" class="search"> <img
												src="/ttk/images/EditIcon.gif" title="Select Member"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
											<a href="#" accesskey="g"
												onClick="javascript:deleteMember()" class="search"> <img
												src="/ttk/images/DeleteIcon.gif" title="Delete Member"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
									</logic:notEqual>
									</td>
								</tr>
								<logic:notEmpty name="frmPreAuthGeneral" property="preAuthNo">
								<%if(!activelink.equals("CounterFraudDept")){ %>
								<tr>
									<td>
										<a href="#" onClick="javascript:displayBenefitsDetails()" style="color:blue; font:bold;">Display Of Benefits</a>
									</td>
								</tr>
								<%}%>
								</logic:notEmpty>
								<tr>
									<td class="textLabelBold textBoxMedium">Pre-Auths Count: <bean:write name="frmPreAuthGeneral" property="preAuthCount" /> </td><br/>
										 <logic:equal value="0" property="cfdPreauthCount" name="frmPreAuthGeneral">
									 		<td class="textLabelBold">(CFD Preauth Count :<bean:write name="frmPreAuthGeneral" property="cfdPreauthCount" />)</td>
										</logic:equal>
										<logic:notEqual value="0" property="cfdPreauthCount" name="frmPreAuthGeneral"> 
											 <td class="textLabelBold"><a href="#" onClick="javascript:onPreauthFroudHistory()" style="color:red; font:bold;">(CFD Preauth Count :<bean:write name="frmPreAuthGeneral" property="cfdPreauthCount" />)</a></td>
										 </logic:notEqual>
								</tr>
								<tr>
									<td class="textLabelBold textBoxMedium">Claims Count:<bean:write name="frmPreAuthGeneral" property="clmCount" />  </td> <br/>
										 <logic:equal value="0" property="cfdClaimCount" name="frmPreAuthGeneral">
								     		<td class="textLabelBold">(CFD Claim Count :<bean:write name="frmPreAuthGeneral" property="cfdClaimCount" />)</td>
										</logic:equal>
										<logic:notEqual value="0" property="cfdClaimCount" name="frmPreAuthGeneral"> 
								     		<td class="textLabelBold"><a href="#" onClick="javascript:onClaimFroudHistory()" style="color:red; font:bold;">(CFD Claim Count :<bean:write name="frmPreAuthGeneral" property="cfdClaimCount" />)</a></td>
								 	</logic:notEqual> 
								</tr>
								
								<tr>
									<td class="textLabelBold textBoxMedium">Appeal Count:<bean:write name="frmPreAuthGeneral" property="appealCount" />  </td> <br/>
										 <%-- <logic:equal value="0" property="cfdClaimCount" name="frmPreAuthGeneral">
								     		<td class="textLabelBold">(CFD Claim Count :<bean:write name="frmPreAuthGeneral" property="cfdClaimCount" />)</td>
										</logic:equal>
										<logic:notEqual value="0" property="cfdClaimCount" name="frmPreAuthGeneral"> 
								     		<td class="textLabelBold"><a href="#" onClick="javascript:onClaimFroudHistory()" style="color:red; font:bold;">(CFD Claim Count :<bean:write name="frmPreAuthGeneral" property="cfdClaimCount" />)</a></td>
								 	</logic:notEqual> --%> 
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
 <logic:notEmpty name="frmPreAuthGeneral" property="relationShip">
  <bean:define id="relationShipTemp" property="relationFlag" name="frmPreAuthGeneral"/>
<%if("Y".equals(relationShipTemp)){ %>	
  <font color="blue">
 <bean:write name="frmPreAuthGeneral" property="relationShip" />
 </font>
  <%}
else if("N".equals(relationShipTemp)) {%>
  <font color="red">
 <bean:write name="frmPreAuthGeneral" property="relationShip" />
 </font>
   <%}
else {%>
	<bean:write name="frmPreAuthGeneral" property="relationShip" />
 <%}%>	
 
<%--  <logic:equal name="frmPreAuthGeneral" property="relationFlag" value="Y">
 <font color="blue">
 <bean:write name="frmPreAuthGeneral" property="relationShip" />
 </font>
 </logic:equal>
 <logic:notEqual name="frmPreAuthGeneral" property="relationFlag" value="Y">
 <font color="red">
 <bean:write name="frmPreAuthGeneral" property="relationShip" />
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
								styleClass="textBox textBoxMedium textBoxDisabled"
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
						<td class="formLabel">Corporate Name:</td>
						<td width="30%" class="textLabel"><html:text
								property="corporateName" styleId="corporateName"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
					</tr>

					<!-- my code -->

					<tr>
						<td class="formLabel">Product Name.:</td>
						<td width="30%" class="textLabel"><html:text
								property="productName" styleId="productName" readonly="true"
								styleClass="textBox textBoxLong textBoxDisabled" /></td>
						<td class="formLabel">Authority.:</td>
						<td width="30%" class="textLabel"><html:text
								property="payerAuthority" styleId="payerAuthority"      
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>									
					</tr>

					<tr>
						<td class="formLabel">Insurer Id:</td>
						<td width="30%" class="textLabel"><html:text
								property="payerId" styleId="payerId"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /> <html:hidden property="insSeqId"
								styleId="insSeqId" /> <html:hidden property="policySeqId"
								styleId="policySeqId" /></td>
						<td class="formLabel">Insurer Name:</td>
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
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
					<td class="formLabel">Member Inception Date:</td>
							<td width="30%" class="textLabel"><html:text
								property="preMemInceptionDt" styleId="preMemInceptionDt"
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
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
					</tr>
					<tr>
						<td class="formLabel">Nationality:</td>
						<td width="30%" class="textLabel"><html:text
								property="nationality" styleId="nationality"
								styleClass="textBox textBoxMedium textBoxDisabled"
								readonly="true" /></td>
						<td class="formLabel">Eligible Networks:</td>
						<td width="30%" class="textLabel"><html:text
								property="eligibleNetworks"
								styleClass="textBox textBoxLarge textBoxDisabled"
								readonly="true" /></td>
					</tr>
<tr>

<td class="formLabel">VIP:</td>
						<td width="30%" class="textLabel"><html:text
								property="vipYorN" styleId="vipYorN"
								styleClass="textBox textBoxSmall textBoxDisabled"
								readonly="true" /></td>
					
</tr>
					
					
					
				
					
					<tr>
						<td class="formLabel">Priority:</td>
						<td class="textLabel"><html:select property="priorityTypeID"
								styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
								<html:option value="">Select From List</html:option>
								<html:optionsCollection name="preauthPriority" label="cacheDesc"
									value="cacheId" />
							</html:select></td>
						<td class="formLabel" width="19%">Network (Y/N):<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:select
								property="networkProviderType"
								styleClass="selectBox selectBoxMedium"
								onchange="setProviderMode();" disabled="<%=enhancedViewMode%>">
								<html:option value="Y">YES</html:option>
								<html:option value="N">NO</html:option>
							</html:select><logic:equal value="N" property="networkProviderType"
								name="frmPreAuthGeneral">
								<%
									netWorkStatus = false;
								%>
							</logic:equal></td>
					</tr>
					
					<tr>
					<%-- <td class="formLabel">Tpa Reference No:</td>
						<td class="textLabel"><html:text property="tpaReferenceNo"
								styleId="tpaReferenceNo" styleClass="textBox textBoxLarge"
								maxlength="60" readonly="<%=netWorkStatus%>"
								disabled="<%=enhancedViewMode%>" /></td> --%>
								<td></td><td></td>
				
					<td class="formLabel">Requested Amount:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:text property="requestedAmount"
								styleClass="textBox textBoxMedium"
								readonly=""  onkeyup="isNumeric(this); "/>&nbsp; <html:text property="requestedAmountcurrencyType" styleId="requestedAmountcurrencyType"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />&nbsp; <a href="#" onclick="openRadioList('requestedAmountcurrencyType','CURRENCY_GROUP','option');clearConversionRate()">
		          <img src="/ttk/images/search_edit.gif" width="18" height="18" title="Select Currency" border="0" align="bottom" > </a></td>
                   </tr>
					<tr>
					<td></td><td></td>
					<td class="formLabel">Converted Amount:</td>
						<td class="textLabel"> <html:text property="convertedAmount" styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"  /> &nbsp;
						<html:text property="currencyType" styleId="totalAmountCurrency" value="QAR"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" /> <!-- <a href="#" onclick="openRadioList('totalAmountCurrency','CURRENCY_GROUP','option')">
		          <img src="/ttk/images/search_edit.gif" width="18" height="18" alt="Select Currency" border="0" align="bottom" > </a> --></td>
					</tr>
					<tr>
					  <td></td>
						<td></td>
						
						<td class="formLabel" width="19%">Conversion Rate:</td> 
							<logic:notEmpty property="conversionRate" name="frmPreAuthGeneral">
						<logic:equal property="requestedAmountcurrencyType" name="frmPreAuthGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN" checked="checked"   onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:" > <html:text property="conversionRate" styleId="conversionRate" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" onkeyup="isNumber(this,'Conversion Rate');"/></div></td> </tr></table> </td>
						</logic:equal>
						<logic:notEqual property="requestedAmountcurrencyType" name="frmPreAuthGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN" checked="checked" onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:" > <html:text property="conversionRate" styleId="conversionRate"  styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" onkeyup="isNumber(this,'Conversion Rate');"/></div></td> </tr></table> </td>
						</logic:notEqual>
						</logic:notEmpty>
						
						
						<logic:empty property="conversionRate" name="frmPreAuthGeneral">
						<logic:equal property="requestedAmountcurrencyType" name="frmPreAuthGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN"  onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:none;" > <html:text property="conversionRate" styleId="conversionRate" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" onkeyup="isNumber(this,'Conversion Rate');"/></div></td> </tr></table> </td>
						</logic:equal>
						<logic:notEqual property="requestedAmountcurrencyType" name="frmPreAuthGeneral" value="QAR">
						<td class="textLabel"> <table> <tr><td> <input type="checkbox" id="converionRateYN"  onclick="enableConversionRate();"/></td><td><div id="conversionRatediv" style="display:none;" > <html:text property="conversionRate" styleId="conversionRate" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" onkeyup="isNumber(this,'Conversion Rate');"/></div></td> </tr></table> </td>
						</logic:notEqual>
						</logic:empty>
					</tr> 
					
					
					<tr>
						
						<td class="formLabel">Provider License Id:</td>
						<td class="textLabel">
							<table>
								<tr>
								
									<td><html:text property="providerId" styleId="providerId"
											styleClass="textBox textBoxMedium"
											readonly="<%=netWorkStatus%>"
											disabled="<%=enhancedViewMode%>" /> <html:hidden
											property="providerSeqId" styleId="providerSeqId" /></td>
									<td><logic:notEqual value="ENHANCEMENT"
											property="preAuthNoStatus"  name="frmPreAuthGeneral">
											<a href="#" accesskey="g"
												onClick="javascript:selectProvider();clearConversionRate();" id="providerIcon" class="search"> <img
												src="/ttk/images/EditIcon.gif" title="Select Provider"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
										</logic:notEqual></td>
									<td>
										<div id="providerResult1">
											<div id="providerResult2"></div>
										</div>
									</td>
								</tr>
								
								
								
								
							</table>
						</td>
						<td class="formLabel">Provider Name:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:text property="providerName"
								styleId="providerName" styleClass="textBox textBoxLarge"
								maxlength="60" readonly="<%=netWorkStatus%>"
								disabled="<%=enhancedViewMode%>" /></td>
					</tr>
					<tr>
						<td class="formLabel">Clinician Id:</td>
						<td class="textLabel">
							<table>
								<tr>
									<td><html:text property="clinicianId"
											styleId="clinicianId" styleClass="textBox textBoxMedium"/></td>
									<td><a href="#" accesskey="g"
										onClick="javascript:selectClinician()" class="search"> <img
											src="/ttk/images/EditIcon.gif" title="Select Clinician"
											width="16" height="16" border="0" align="absmiddle">&nbsp;
									</a></td>
									<td>
										<div id="clinicianResult1">
											<div id="clinicianResult2"></div>
										</div>
									</td>
								</tr>
							</table>
						<td class="formLabel">Clinician Name:</td>
						<td><html:text property="clinicianName"
								styleClass="textBox textBoxLarge" styleId="clinicianName"/></td>
					</tr>
					<%-- <logic:match value="AL AHLI" property="providerName">  --%>
						<tr>
							<td class="formLabel">Clinician Specility :</td>
							<td><html:text property="clinicianSpeciality" styleClass="textBox textBoxLarge" styleId="clinicianName" readonly="true"/></td>
							<td class="formLabel">Clinician Email ID:</td>
							<td><html:text property="clinicianEmail" styleClass="textBox textBoxLarge" styleId="clinicianEmail"/></td>
							<td class="formLabel">&nbsp;</td>
							<td class="formLabel">&nbsp;</td>
						</tr>
					<%-- </logic:match> --%>
					<tr>
						<td class="formLabel">Benefit Type:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:select property="benefitType"
								styleClass="selectBox selectBoxMedium"
								onchange="setMaternityMode()" disabled="<%=enhancedViewMode%>">
								<html:option value="">Select from list</html:option>
								<html:optionsCollection name="benefitTypes" label="cacheDesc"
									value="cacheId" />
							</html:select></td>	
						<logic:match name="frmPreAuthGeneral" value="MTI" property="benefitType">						
						<td class="formLabel">Maternity Coverage Limit:</td>
							<td><html:select property="matsubbenefit"	styleId="matsubbenefit" styleClass="selectBox selectBoxMedium" >
										<html:option value="N">Maternity Normal Cover</html:option>
										<html:option value="Y">Maternity Complication cover</html:option>
						</html:select></td>
						</logic:match>
							
						<td class="formLabel" width="19%" colspan="2"><logic:equal
								name="frmPreAuthGeneral" value="MTI" property="benefitType">
								<table>
									<tr>
										<td class="formLabel">Gravida: <html:text
												name="frmPreAuthGeneral" property="gravida"
												styleId="gravida" onkeyup="isZero(this);"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												onblur="setGPLA('GR');" />
										</td>
										<td class="formLabel">Para:
											<html:text name="frmPreAuthGeneral" property="para"
												onkeyup="isNumeric(this);" styleId="para"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												onblur="setGPLA('PA');" />
										</td>
										<td class="formLabel">Live:
											<html:text name="frmPreAuthGeneral" property="live"
												onkeyup="isNumeric(this);" styleId="live"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												onblur="setGPLA('LI');" />
										</td>
										<td class="formLabel">Abortion:<html:text
												name="frmPreAuthGeneral" property="abortion"
												onkeyup="isNumeric(this);" styleId="abortion"
												styleClass="textBox textBoxTooTiny" maxlength="2"
												onblur="setGPLA('AB');" />
										</td>
									</tr>
								</table>
							</logic:equal></td> 
							
										
					</tr>
					<logic:match name="frmPreAuthGeneral" value="MTI" property="benefitType">
					<tr>
						<td class="formLabel" width="19%">Nature of Conception:<!-- <span class="mandatorySymbol">*</span></td> -->
						<td class="textLabel">
						  <html:select property="natureOfConception" styleClass="selectBox selectBoxMedium"  disabled="<%=enhancedViewMode%>">
									<html:option value="">Select From List</html:option>
									<html:optionsCollection name="natureOfConception" label="cacheDesc" value="cacheId" />
							</html:select></td>

        			<td class="formLabel">Date Of LMP: <!-- <span class="mandatorySymbol">*</span> --></td>
       				 <td>
       				 <html:text name="frmPreAuthGeneral" property="latMenstraulaPeriod" styleClass="textBox textDate"	maxlength="10"  disabled="<%=enhancedViewMode%>" />
					<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#"	onClick="javascript:show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.latMenstraulaPeriod',document.frmPreAuthGeneral.latMenstraulaPeriod.value,'',event,148,178);return false;"	onMouseOver="window.status='Calendar';return true;" 	onMouseOut="window.status='';return true;"><img	src="/ttk/images/CalendarIcon.gif" title="Calendar"	name="empDate" width="24" height="17" border="0"	align="absmiddle"></a>			
					
					</td>
					</tr>
					<logic:equal name="frmPreAuthGeneral" value="IMTI" property="benefitType">
					<tr>
						<td class="formLabel" width="19%"> Mode of delivery:</td>
						<td class="textLabel">
						  <html:select property="modeofdelivery" styleClass="selectBox selectBoxMedium">
									<html:optionsCollection name="modeofdelivery" label="cacheDesc" value="cacheId" />
							</html:select></td>
						<td></td>
					</tr>
					</logic:equal>
					</logic:match>
				
					<tr>
						<td class="formLabel" width="19%">Encounter Type:</td>
						<td class="textLabel"><html:select property="encounterTypeId"
								styleClass="selectBox selectBoxLarge" onchange="setDateMode();"
								disabled="<%=enhancedViewMode%>">
								<logic:notEmpty name="encounterTypes" scope="session">
									<html:optionsCollection name="encounterTypes" value="key"
										label="value" />
								</logic:notEmpty>
							</html:select></td>
						<td></td>
						<td></td>
					</tr>
					
					
					
					
					
					<tr>
						<td class="formLabel" width="19%">Encounter Start Type:</td>
						<td class="textLabel"><html:select
								property="encounterStartTypeId"
								styleClass="selectBox selectBoxLarge"
								disabled="<%=enhancedViewMode%>">
								<html:optionsCollection name="encounterStartTypes"
									label="cacheDesc" value="cacheId" />
							</html:select></td>
						<td class="formLabel" width="19%">Encounter End Type:<span
							class="mandatorySymbol">*</span></td>
						<td class="textLabel"><html:select
								property="encounterEndTypeId"
								styleClass="selectBox selectBoxLarge">
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
									<td><html:text name="frmPreAuthGeneral"
											property="admissionDate" styleClass="textBox textDate"
											maxlength="10"  onblur="setEndDate();clearConversionRate();"
											disabled="<%=enhancedViewMode%>" />
										<logic:notEqual value="ENHANCEMENT" property="preAuthNoStatus"
											name="frmPreAuthGeneral">
											<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','frmPreAuthGeneral.admissionDate',document.frmPreAuthGeneral.admissionDate.value,'',event,148,178);return false;"
													onkeyup="clearConversionRate();"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar"
												name="empDate" width="24" height="17" border="0"
												align="absmiddle"></a>
										</logic:notEqual>&nbsp;</td>
									<td><html:text name="frmPreAuthGeneral"
											property="admissionTime" styleClass="textBox textTime"
											maxlength="5" onblur="setEndDate();endDateValidation();"
											disabled="<%=enhancedViewMode%>" />&nbsp;</td>
									<td><html:select property="admissionDay"
											name="frmPreAuthGeneral" styleClass="selectBox"
											onchange="setEndDate();" disabled="<%=enhancedViewMode%>">
											<html:options name="ampm" labelName="ampm" />
										</html:select></td>
								</tr>
							</table>
						</td>
						<td class="formLabel">End Date:<span class="mandatorySymbol">*</span></td>
						<td class="textLabel">
							<table cellspacing="0" cellpadding="0">
								<tr>
									<td><html:text name="frmPreAuthGeneral"
											property="dischargeDate" styleClass="textBox textDate"
											maxlength="10"  onblur="endDateValidation();clearConversionRate();"/>
										<logic:match name="viewmode" value="false">
											<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="checkCalender();"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar"
												id="discImg" name="empDate" width="24" height="17"
												border="0" align="absmiddle"></a>
										</logic:match>&nbsp;</td>
									<td><html:text name="frmPreAuthGeneral"
											property="dischargeTime" styleClass="textBox textTime"
											maxlength="5" />&nbsp;</td>
									<td><html:select property="dischargeDay"
											name="frmPreAuthGeneral" styleClass="selectBox">
											<html:options name="ampm" labelName="ampm" />
										</html:select></td>
								</tr>
							</table>
						</td>
					</tr>
					<logic:equal value="N" name="frmPreAuthGeneral"
						property="networkProviderType">
						<tr>
							<td class="formLabel">Country:<span class="mandatorySymbol">*</span></td>
							<td class="textLabel"><html:select
									property="providerCountry" styleId="providerCountry"
									styleClass="selectBox selectBoxMoreMedium"
									onchange="getStates();getcurrencyCode();clearConversionRate();" disabled="<%=enhancedViewMode%>">
									<html:option value="">Select From List</html:option>
									<html:optionsCollection name="countryCodeList"
										label="cacheDesc" value="cacheId" />
								</html:select></td>
							<td class="formLabel">City:</td>
							<td class="textLabel"><html:select
									property="providerEmirate" styleId="providerEmirate"
									styleClass="selectBox selectBoxMoreMedium"
									onchange="getAreas()" disabled="<%=enhancedViewMode%>">
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
									disabled="<%=enhancedViewMode%>">
									<html:option value="">Select From List</html:option>
									<logic:notEmpty name="providerAreas" scope="session">
										<html:optionsCollection name="providerAreas" value="key"
											label="value" />
									</logic:notEmpty>
								</html:select></td>
							<td class="formLabel">Po-Box:</td>
							<td class="textLabel"><html:text property="providerPobox"
									styleClass="textBox textBoxLong" /></td>
						</tr>
						<tr>
							<td class="formLabel">Address:</td>
							<td class="textLabel"><html:text property="providerAddress"
									styleClass="textBox textBoxLong"
									disabled="<%=enhancedViewMode%>" /></td>
							<logic:equal value="PTRP" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							<td class="formLabel">Line Of Treament:</td>						
						<!-- <td class="textLabel"> -->
						<td><html:textarea name="frmPreAuthGeneral"  property="finalRemarks"
						styleClass="textBoxDisabled" readonly="true" cols="38" rows="2" style="height:30px;"/></td>
						</logic:equal>
						<logic:notEqual value="PTRP" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							<td></td>
							<td></td>
						</logic:notEqual>
						</tr>
					</logic:equal>
					<logic:equal value="TEL" name="frmPreAuthGeneral" property="preAuthRecvTypeID">
					
					<tr>
		   <td class="formLabel">Diagnosis:</td>
		   <td class="textLabel" colspan="3">
		   <html:textarea name="frmPreAuthGeneral" indexed="diagnosis" property="diagnosis" styleClass="textBox textBoxMedium textBoxDisabled" cols="60" rows="2" readonly="true"/>
		 </td>
		 <td></td><td></td>
		 </tr>
		  <logic:notEmpty name="frmPreAuthGeneral" property="revisedDiagnosis">
		 <tr>
		   <td class="formLabel">RevisedDiagnosis:</td>
		   <td class="textLabel" colspan="3">
		   <html:textarea name="frmPreAuthGeneral" property="revisedDiagnosis" styleId="revDiagnosis"  cols="60" rows="2" styleClass="textBox textBoxMedium textBoxDisabled" readonly="true" />
		 </td>		 
		 </tr>
		 </logic:notEmpty>
		 <tr>
		   <td class="formLabel">Services:</td>
		   <td class="textLabel" colspan="3">
		   <html:textarea name="frmPreAuthGeneral" indexed="services" property="services" styleClass="textBox textBoxMedium textBoxDisabled"  cols="60" rows="2" readonly="true"/>
		 </td>
		 </tr>
		 <logic:notEmpty name="frmPreAuthGeneral" property="revisedServices">
		 <tr>
		   <td class="formLabel">RevisedServices:</td>
		   <td class="textLabel" colspan="3">
		   <html:textarea name="frmPreAuthGeneral"   property="revisedServices"  styleId="revService" cols="60" rows="2" styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"/>
		 </td>
		 </tr>
		 </logic:notEmpty>
		         <tr>
				    <td class="formLabel">Approved Amount:  </td>
					  <td class="textLabel">
					  	<html:text name="frmPreAuthGeneral" property="oralApprovedAmount" indexed="apprAmount" styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" />
					  </td>	
					  </tr>
					  <logic:notEmpty name="frmPreAuthGeneral" property="oralRevisedApprovedAmount">
				<tr>
				    <td class="formLabel">Revised Approved Amount:  </td>
					  <td class="textLabel">
					  	<html:text name="frmPreAuthGeneral" property="oralRevisedApprovedAmount" onkeyup="numberValidation(this)" styleId="revApprAmount" styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"/>
					  </td>	
					  </tr>	
					</logic:notEmpty>
					</logic:equal>
					
					<tr>
							<td class="formLabel">Assigned To:</td>
							
							<td width="40%" class="textLabelBold" id="assignedToID">
								<bean:write name="frmPreAuthGeneral" property="assignedTo"/>
			                 </td>						
							<td class="formLabel">
							<logic:equal value="DHP" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							DHPO Upload Status:
							</logic:equal>
							</td>
							<td width="40%" class="textLabelBold">
							<logic:equal value="DHP" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							   <logic:equal value="Y" property="dhpoUploadStatus" name="frmPreAuthGeneral">
							   YES
							   </logic:equal>
							    <logic:notEqual value="Y" property="dhpoUploadStatus" name="frmPreAuthGeneral">
							   NO
							   </logic:notEqual>	
							   </logic:equal>							
			                 </td>
						</tr>
					
					
						
				<logic:notEmpty name="frmPreAuthGeneral" property="providerId">
					<tr>
        			<td width="20%" class="formLabel">Provider Specific Remarks:</td>
        			<td colspan="3">
        				<html:textarea property="providerSpecificRemarks" name="frmPreAuthGeneral" styleClass="textBox textAreaLong textBoxDisabled" style="color : #ff0000;font-weight : bold;" readonly="true"/>
        			</td>
        			<td></td>
        			<td></td>
      			</tr>
      			</logic:notEmpty>
      			<tr>
        			<td width="20%" class="formLabel">Member Specific Policy Remarks:</td>
        			<td colspan="3">
        				<html:textarea property="memberSpecificRemarks" name="frmPreAuthGeneral" styleId="memberSpecificRemarksid" styleClass="textBox textAreaLong textBoxDisabled" style="color : #ff0000;font-weight : bold;" readonly="true"/>
        			</td>
        			<td></td>
        			<td></td>
      			</tr>
						<logic:notEmpty  property="preAuthSeqID" name="frmPreAuthGeneral">
<%-- 						<logic:equal value="Y" property="networkProviderType" name="frmPreAuthGeneral"> --%>
						<tr>
							<td class="formLabel"></td>
							
							<td width="40%" class="textLabelBold">
								
			                 </td>						
							<td></td>
							<!-- <td><a href="#" onclick="doViewProviderDocs();">View Uploaded Docs</a></td> -->
						</tr>
<%-- 						</logic:equal> --%>
						</logic:notEmpty>
					<!-- tr>
			       <td class="formLabel">Requested Amount:</td>
			      <td class="textLabel">
			       	<table><tr>
			      <td><html:text property="requestedAmount"  styleClass="textBox textBoxSmall textBoxDisabled" onkeyup="isNumeric(this)" /></td>
			      <td>
			      <logic:empty property="requestedAmountCurrency" name="frmPreAuthGeneral">
			      <html:text property="requestedAmountCurrency" value="QAR" styleId="requestedAmountCurrency"  styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" />
			      </logic:empty>
			       <logic:notEmpty property="requestedAmountCurrency" name="frmPreAuthGeneral">
			       <html:text property="requestedAmountCurrency"  styleId="requestedAmountCurrency"  styleClass="textBox textBoxSmall textBoxDisabled" readonly="true" />
			      </logic:notEmpty>
			      </td>
			      <td><a href="#" onclick="openRadioList('requestedAmountCurrency','CURRENCY_GROUP','option')">
		         <img src="/ttk/images/EditIcon.gif" width="16" height="16" alt="Select Currancy" border="0" ></a></td>
			      </tr></table>
			      </td>
			       <td></td>
			       <td></td>
			  </tr-->
			  
			  <logic:equal name="frmPreAuthGeneral" value="DNTL" property="benefitType">
			  <tr> <td colspan="4"> 
			  <fieldset> 
			  <legend> Dental </legend>
			  <table>
			  <tr> <td class="formLabel">Select Treatment Type :<span class="mandatorySymbol">*</span> </td>
					<td class="textLabel">
						<html:select property="treatmentTypeID" styleClass="selectBox selectBoxMedium" onchange="changeTreatmentType(this)">
							<html:option value="">--Select from List--</html:option>
							<html:optionsCollection name="dentalTreatmentTypes" label="cacheDesc" value="cacheId" />
						</html:select>
					</td> 
			</tr> 
			
			<tr> 
			<td colspan="4"> 
			<%@include file="/ttk/preauth/dentalDetails.jsp" %>
			</td>
			</tr> 
			
			  </table>
			  </fieldset>
			   </td>
			  </tr>
			  
			  </logic:equal>
			    <%if(!activelink.equals("CounterFraudDept")){ %>
					<tr>
						<td align="center" colspan="4">
							<button type="button" name="Button2" accesskey="s"
								class="buttons" onMouseout="this.className='buttons'"
								onMouseover="this.className='buttons buttonsHover'"
								onClick="onUserSubmit();">
								<u>S</u>ave
							</button>&nbsp;
							
							 <!-- button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button-->
						</td>
					</tr>
				<%} %>
				</table>
			</fieldset>
			<logic:notEmpty name="frmPreAuthGeneral" property="preAuthSeqID">
				<fieldset>
					<legend>Diagnosis Details</legend>
					<table align="center" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th>Presenting Complaints:<span class="mandatorySymbol">*</span></th>
							<td><html:textarea name="frmPreAuthGeneral" 
									property="presentingComplaints" cols="35" rows="2" /></td>
									
							<!-- 	//Added For PED Calculation -->		
							<td class="formLabel">&nbsp;&nbsp;&nbsp;&nbsp;Duration of the Ailment:<span class="mandatorySymbol">*</span></td>
			      					<td  width="20%" class="textLabel">		      
			      					<html:text name="frmPreAuthGeneral" property="durAilment" styleClass="textBox textBoxSmall" maxlength="3" onkeyup="isNumeric(this);"/>
				  <html:select
								name="frmPreAuthGeneral" property="durationFlag"
								styleClass="selectBox textBoxSmall">
								<html:option value="DAYS">DAYS</html:option>
								<html:option value="WEEKS">WEEKS</html:option>
								<html:option value="MONTHS">MONTHS</html:option>
								<html:option value="YEARS">YEARS</html:option>
							</html:select>
						</tr>
					</table>
					<br> 
<br>
					


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
												<td><html:text name="frmPreAuthGeneral"
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
										</table> <html:hidden name="frmPreAuthGeneral" property="icdCodeSeqId"
											styleId="icdCodeSeqId" /> <html:hidden
											name="frmPreAuthGeneral" property="diagSeqId"
											styleId="diagSeqId" />
									</td>
									<td align="center"><html:checkbox name="frmPreAuthGeneral"
											property="primaryAilment" styleId="primaryAilment" value="Y" />
									</td>
									<td align="center"><html:textarea rows="1" cols="80"
											name="frmPreAuthGeneral" styleId="diagnosisDescription"
											property="ailmentDescription" /></td>
								</tr>
								<%if(!activelink.equals("CounterFraudDept")){ %>
								<tr>
									<td colspan="3"><logic:empty name="frmPreAuthGeneral"
											property="diagSeqId">
											<button type="button" name="Button3" accesskey="d"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="addDiagnosisDetails()">
												A<u>d</u>d
											</button>&nbsp;
			  </logic:empty> <logic:notEmpty name="frmPreAuthGeneral" property="diagSeqId">
											<button type="button" name="Button3" accesskey="d"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="addDiagnosisDetails()">Edit</button>&nbsp;
			    </logic:notEmpty></td>
								</tr>
								<%}%>
							</table>
							<br>
						</div>
					</div>
					<br>
					<ttk:DiagnosisDetails flow="PAT" />
				</fieldset>

				<fieldset>
					<legend>
					<%if(!activelink.equals("CounterFraudDept")){ %>
						Activity/Service Details <a href="#" accesskey="a"
							onClick="javascript:addActivityDetails()"><img
							src="/ttk/images/AddIcon.gif" title="Add Activity Details"
							width="13" height="13" border="0" align="absmiddle"></a>
					<%}else{ %>
					Activity/Service Details <a href="#" accesskey="a"><img
							src="/ttk/images/AddIcon.gif" title="Add Activity Details"
							width="13" height="13" border="0" align="absmiddle"></a>
					<%} %>
					</legend>
					
					<logic:notEmpty name="frmPreAuthGeneral" property="lmrpAlert">
						<div style="margin-left:10px;margin-right:10px;text-align:justify;color:red"><bean:write  name="frmPreAuthGeneral" property="lmrpAlert" /></div>
					<br>
					</logic:notEmpty>	
					
					<logic:equal name="frmPreAuthGeneral" property="mophDrugYN" value="Y">
						&nbsp;&nbsp;<font color="red"><b>Claimed quantity for the drug is higher than the usual indication. To be reviewed.</b></font>
						<br>
					</logic:equal>
					<ttk:ActivityDetails flow="PAT" />
					<br>
					<div align="center" style="align: center;">
						<div
							style="border: 2px solid #a1a1a1; background: #F8F8F8; border-radius: 25px; width: 90%; height: auto; text-align: center;">
							<br>
							<logic:notEmpty name="frmPreAuthGeneral"
								property="maternityAlert">
								<table align="center" class="errorContainer" border="0"
									cellspacing="0" cellpadding="0">
									<tr>
										<td style="color:#3352FF">&nbsp; <bean:write
												name="frmPreAuthGeneral" property="maternityAlert" /></td>
									</tr>
								</table>
							</logic:notEmpty>
							
							
							<table align="center">
							<tr>
							   
							   <%
						
						 if(request.getSession().getAttribute("preauthVerifiedForSuspected").equals("N") && request.getSession().getAttribute("suspectedYesorNOFlag").equals("N")){ 
						%>
							
						<td align="left"><img title="Internal Remark Status" src="/ttk/images/suspectedimg.png" onclick="fillFraudDeatils()" href="#">
						<!-- <a href="#"  href="#">IMAGE</a> </td> -->
						
					<%   	}else if(request.getSession().getAttribute("preauthVerifiedForSuspected").equals("N") && request.getSession().getAttribute("suspectedYesorNOFlag").equals("Y")){  

					%>
							
						<td align="left"><img title="Internal Remark Status"  id="" src="/ttk/images/suspectedcheckedimg.png" onclick="showFraudDeatils()" href="#">
						<!-- <a href="#"  href="#">IMAGE</a> </td> -->
						
						<%}else{ 
							%>
							
						<td align="left"><img title="Internal Remark Status"  id="" src="/ttk/images/verifiedclaimimg.png" onclick="showFraudDeatils()" href="#">
						
						<% }%>
							
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
									<td align="center">Converted Approved Amount:</td>
									
								</tr>
								<tr>
								<%if(!activelink.equals("CounterFraudDept")){%>	
									<td align="right">
										<button type="button" accesskey="c" class="buttons"
											onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="calculatePreauthAmount()"><b>C</b>alculate</button>
									</td>
									<%}else{ %>
									<td align="right">
										<button type="button" accesskey="c" class="buttons"
											onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"><b>C</b>alculate</button>
									</td>
									<%} %>
									<td align="center"><html:text name="frmPreAuthGeneral"
											property="grossAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmPreAuthGeneral"
											property="discountAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmPreAuthGeneral"
											property="discountGrossAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmPreAuthGeneral"
											property="patShareAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmPreAuthGeneral"
											property="netAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmPreAuthGeneral"
											property="disAllowedAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" /></td>
									<td align="center"><html:text name="frmPreAuthGeneral"
											property="approvedAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" />&nbsp;
									<html:text property="currencyType" styleId="totalAmountCurrency" value="QAR"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />
									
									<td><html:text name="frmPreAuthGeneral"
											property="convertedFinalApprovedAmount" onkeyup="isNumeric(this);"
											styleClass="textBox textBoxSmall textBoxDisabled"
											readonly="true" />&nbsp;<html:text property="requestedAmountcurrencyType" styleId="requestedAmountcurrencyType"  styleClass="textBox textBoxTooTiny textBoxDisabled" readonly="true" />
			      </td>
											
								</tr>
									<tr>
									<td>Status:</td>
									<td colspan="8" align="left">
										<table>
											<tr>
												<td><html:select property="preauthStatus" styleId="preauthStatus" 
														name="frmPreAuthGeneral"
														styleClass="selectBox selectBoxMedium" onchange="javascript:statusChange()">
														<%-- <html:option value="">Select from list</html:option> --%>
														<html:optionsCollection name="preauthStatusList"
															label="cacheDesc" value="cacheId" />
													</html:select>
													
													<logic:equal name="frmPreAuthGeneral" property="inProgressStatus" value="INP-APL">
													&nbsp;&nbsp;&nbsp;&nbsp; Appeal <img title="" src="/ttk/images/InprogressAppeal.gif"  title="Appeal" width="16" height="16">
													</logic:equal>
													<logic:equal name="frmPreAuthGeneral" property="inProgressStatus" value="INP-RES">
													&nbsp;&nbsp;&nbsp;&nbsp; Shortfall Responded <img  src="/ttk/images/AddIcon.gif"  width="16" height="16">
													</logic:equal>
													<logic:equal name="frmPreAuthGeneral" property="inProgressStatus" value="INP-ENH">
													&nbsp;&nbsp;&nbsp;&nbsp; Enhancement <img  src="/ttk/images/InprogressEnhancement.gif"  width="16" height="16">			
													</logic:equal>
												</td>	
										
												
												<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Authorization Number:</td>
												<td class="textLabelBold" style="background:;"><bean:write
														property="authNum" name="frmPreAuthGeneral" /></td>
											</tr>
										
										</table>
									</td>
								</tr>
									<tr>
											<logic:equal name="frmPreAuthGeneral" property="preauthStatus" value="INP">
												<td>Reason:<span  class="mandatorySymbol">*</span></td>
							                                   
												    <td><html:select property="reason"  styleId="reason"
														name="frmPreAuthGeneral"
														styleClass="selectBox selectBoxMedium" onchange="javascript:statusReasonChange();">
														 <html:option value="">Select from list</html:option> 
														<html:optionsCollection name="preauthReasonList"
															label="cacheDesc" value="cacheId" />
													</html:select>
													</td>
													</logic:equal>
													<logic:notEqual name="frmPreAuthGeneral" property="preauthStatus" value="INP">
													<td>Reason:<span  class="mandatorySymbol"></span></td>
							                                   
												    <td><html:select property="reason"  styleId="reason"
														name="frmPreAuthGeneral" disabled="true"
														styleClass="selectBox selectBoxMedium">
														 <html:option value="">Select from list</html:option> 
													 <html:optionsCollection name="preauthReasonList"
															label="cacheDesc" value="cacheId" />
													</html:select>
													</td>
													</logic:notEqual>
													<logic:equal name="frmPreAuthGeneral" property="preauthStatus" value="INP">
													
													<logic:notEmpty name="frmPreAuthGeneral" property="reason">
													
													<td>Time:</td>	
												 <td>
												 <logic:notEqual name="frmPreAuthGeneral" property="reason" value="HLD">
												 <html:select property="reasonTime"  styleId="reasonTime"
														name="frmPreAuthGeneral" 
														styleClass="selectBox selectBoxMedium">
														 <html:option value="">Select from list</html:option> 
													     <html:option value="30">30 min</html:option> 
													     <html:option value="40">40 min</html:option> 
													</html:select>
													</logic:notEqual>
													<logic:equal name="frmPreAuthGeneral" property="reason" value="HLD">
													<html:select property="reasonTime"  styleId="reasonTime"
														name="frmPreAuthGeneral" 
														styleClass="selectBox selectBoxMedium">
														 <html:option value="">Select from list</html:option> 
													     <html:option value="1440">24 hr</html:option> 
													     <html:option value="2880">48 hr</html:option> 
													</html:select>
													</logic:equal>
													</td>
													</logic:notEmpty>
													</logic:equal>
													
													
											</tr>
								<tr>
										<logic:equal value="Y" property="appealYN" name="frmPreAuthGeneral">
											<td class="formLabel">Appeal Comments:</td>
											<td class="textLabel" colspan="7"><html:textarea
													property="appealRemarks" cols="80" rows="2" readonly="true" />
											</td>
											
											<logic:equal name="frmPreAuthGeneral" property="appealDocYN" value="Y">
											<td>&nbsp;&nbsp;&nbsp;&nbsp; <font color=blue><b>Please Refer the Appeal Documents.</b></font></td> 			
											</logic:equal>
										</logic:equal>	
								</tr>
								<tr>
									<td class="formLabel">Internal Remarks:</td>
									<td class="textLabel" colspan="7"><html:textarea
											property="internalRemarks" cols="80" rows="2" /></td>
								</tr>
								<tr>
									<td class="formLabel">Remarks for the Provider:</td>
									<td class="textLabel" colspan="7"><html:textarea
											property="medicalOpinionRemarks" cols="80" rows="2" /></td>
								</tr>
								<tr>											
									<td class="formLabel">Override Main Remarks:</td>
									<td class="textLabel" colspan="7">
										<html:textarea property="overrideMainRemarks" styleId="overrideMainRemarks" cols="80" rows="2" style="background-color: #f1f1f1;"  readonly="true" />
									</td>											
								</tr>
							<logic:notEmpty name="frmPreAuthGeneral" property="overrideSubRemarks">	
								<tr>											
									<td class="formLabel">Override Sub Remarks:</td>
									<td class="textLabel" colspan="7">
										<html:textarea property="overrideSubRemarks" styleId="overrideSubRemarks" cols="80" rows="2" style="background-color: #f1f1f1;"  readonly="true" />
									</td>											
								</tr>
							</logic:notEmpty>	
								<tr>											
									<td class="formLabel">Override Other Remarks:</td>
									<td class="textLabel" colspan="7"><html:textarea property="overrideRemarks" styleId="overrideRemarks" style="background-color: #f1f1f1;"  readonly="true"
											cols="80" rows="3" /></td>											
								</tr>
						
							<%if(!activelink.equals("CounterFraudDept")){%>	
								<tr>
								
									<td colspan="8" align="center">
										
										<button type="button" name="Button1" accesskey="s"
											class="buttons" onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="saveAndCompletePreauth()">
											<u>S</u>ave
										</button>&nbsp;
										<logic:equal value="Y" property="preauthCompleteStatus"
											name="frmPreAuthGeneral">
											<button type="button" name="Button1" accesskey="g"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="generatePreAuthLetter()">
												<u>G</u>enerate Letter
											</button>&nbsp;
     											<button type="button" name="Button1" accesskey="e" class="buttons"
												onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="sendPreAuthLetter()">
												S<u>e</u>nd
											</button>&nbsp;
											
							<%
         						if (TTKCommon.isAuthorized(request, "PreAuthOverride")) {
        					 %>
											<button type="button" name="Button1" accesskey="o"
												class="buttons" onMouseout="this.className='buttons'"
												onMouseover="this.className='buttons buttonsHover'"
												onClick="overridPreAuthDetails()">
												<u>O</u>verride
											</button>&nbsp;
       							<%
       							}
      							%>
										
							<logic:equal value="DHP" property="preAuthRecvTypeID" name="frmPreAuthGeneral">
							 <logic:notEqual value="Y" property="dhpoUploadStatus" name="frmPreAuthGeneral">
							<button type="button" name="Button2"	class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" 	onClick="uploadToDhpo()">
								Upload To DHPO
							</button>&nbsp;
							
							<button type="button" name="Button2"	class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" 	onClick="viewDhpoLogs()">
								View Dhpo Logs
							</button>&nbsp;
							 </logic:notEqual>
							 </logic:equal>
						
                    </logic:equal> 
                    
								</tr>		
													
							<logic:equal name="frmPreAuthGeneral" property="approvalAlertLog" value="Y">
								<tr>
										<td align="center" colspan="8"><font color=red><b>Note : Member start date and end date is same, do you want to proceed.</b></font></td> 			
								</tr>
							</logic:equal>					
								
					<%} %>
								<tr>
									<td colspan="8">&nbsp;</td>
								</tr>
								<%if(!activelink.equals("CounterFraudDept")){%>	
								<tr>
								<td><a href="#" onclick="javascript:onUserIcon()"><img title="Assign" src="/ttk/images/assignbutton.png" border="white"></a>
								</tr>
								<%}%>
								<tr>
								<td> 
									Decision Date & Time :
								</td>
								<td class="textLabel" colspan="2"> 
									<bean:write name="frmPreAuthGeneral" property="decisionDt" />
								</td>
								<td colspan="3">&nbsp;</td>
								<td>
			                    	Processed By : 
			                    </td>
			                    <td class="textLabel" colspan="2" align="left"> <bean:write name="frmPreAuthGeneral" property="processedBy" /> </td>
								</tr>
							</table>
							<br>
						</div>
					</div>
				</fieldset>

				<fieldset>
					<legend>
					<%if(!activelink.equals("CounterFraudDept")){ %>
						Generate ShortFalls <a href="#" accesskey="q"
							onClick="javascript:addPreauthShortFalls()"><img
							src="/ttk/images/AddIcon.gif" title="Raise ShortFalls" width="13"
							height="13" border="0"></a>
					<%}else{ %>
					Generate ShortFalls <a href="#" accesskey="q"><img
							src="/ttk/images/AddIcon.gif" title="Raise ShortFalls" width="13"
							height="13" border="0"></a>
					<%} %>
					</legend>
					<ttk:Shortfalls flow="PAT" />
				</fieldset>
			</logic:notEmpty>
			<% if(request.getSession().getAttribute("preauthviewfromdashbord")!= null && request.getSession().getAttribute("preauthviewfromdashbord").equals("Y") ){ %>	
			<table>
			<tr>
			<td colspan="8" align="center">
										
										<button type="button" name="Button1" accesskey="s"
											class="buttons" onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="closePreauth()" style="margin-left: 570px;">
											<u>B</u>ackToDashbord
										</button>
										
					</td>
			</tr>
			</table>
	<%}%>
		</div>
		<INPUT TYPE="hidden" NAME="mode" VALUE="">
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
		<input type="hidden" name="validateMemId" id="validateMemId"
			value='<bean:write name="frmPreAuthGeneral" property="memberId"/>' />
        <input type="hidden" name="focusObj" value="<%= request.getAttribute("focusObj")%>">
		<html:hidden property="authType" styleId="authType"
			name="frmPreAuthGeneral" value="PAT" />
		<html:hidden property="letterPath" name="frmPreAuthGeneral" />
		<html:hidden property="preauthCompleteStatus" name="frmPreAuthGeneral" />
		<html:hidden property="validateIcdCodeYN" name="frmPreAuthGeneral" />

		<%-- <html:hidden name="frmPreAuthGeneral" property="preAuthNo" /> --%>
		<html:hidden property="preAuthSeqID" styleId="seqId" name="frmPreAuthGeneral" />
		<logic:notEmpty property="preauthPartnerRefId" name="frmPreAuthGeneral">
		<input type="hidden" id="patSeqId" name="patSeqId" value="<%=session.getAttribute("lngPartnerPreAuthSeqId") %>">
		</logic:notEmpty>
		
		<html:hidden property="approvalAlertLog" name="frmPreAuthGeneral" />
		<html:hidden property="parentPreAuthSeqID" name="frmPreAuthGeneral" />
		<html:hidden property="authNum" name="frmPreAuthGeneral" />
		<html:hidden property="enhancedYN" name="frmPreAuthGeneral" />
		<html:hidden property="preAuthNoStatus" name="frmPreAuthGeneral" />
		<html:hidden property="oralORsystemStatus" value="GENERAL"/>
		<html:hidden property="memberSeqID" styleId="memberSeqID" />
		<html:hidden property="partnerPreAuthSeqId" styleId="PreauthRefSeqId"/>
		<html:hidden property="preauthPartnerRefId" styleId="preauthPartnerRefId" />
		<html:hidden property="prodPolicySeqId" styleId="prodPolicySeqId"/>
		<input type="hidden" name="denailcode" id="denailcode">
		<html:hidden property="preAuthRecvTypeID" styleId="preAuthRecvTypeID"/>
		<html:hidden property="relationShip" name="frmPreAuthGeneral" />   
		<html:hidden property="relationFlag" name="frmPreAuthGeneral" /> 
		<html:hidden property="assignDate" styleId="assignDateID" name="frmPreAuthGeneral" /> 
		 <html:hidden property="autoAssignYn" styleId="auto_assign_ynID" name="frmPreAuthGeneral" /> 
		 <input type="hidden" name="focusID" id="focusID" value="">
		<html:hidden property="benefitPopUpAlhalli" name="frmPreAuthGeneral" />
		
		
<html:hidden property="qtyAndDaysAlert" name="frmPreAuthGeneral" />
	  <!--   <input type="hidden" name="qtyAndDaysAlert" value=<bean:write name="frmPreAuthGeneral" property="qtyAndDaysAlert"/>> -->
		<html:hidden property="preAuthNo" name="frmPreAuthGeneral" />
		<input type="hidden" name="userSeqId" id="userSeqID" value="<%= userseqid %>">
		<% if(assigntouserseqid != null) {%>
		<input type="hidden" name="assignUserSeqID" id="assigntouserseqid" value="<%=assigntouserseqid%>"/>
		<%} %>
	    <%if(vipmemberuseraccesspermissionflag != null){ %>
	    <input type="hidden" id="vipmemberuseraccesspermissionflagid" value="<%=(String)request.getSession().getAttribute("vipmemberuseraccesspermissionflag")%>">
	    <%} %>
	    <input type="hidden" name="preauthorclaimswitchType" id="preauthorclaimswitchTypeId" value="<%=request.getSession().getAttribute("preauthorclaimswitchType")%>">

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
<logic:equal value="DNTL" property="benefitType" name="frmPreAuthGeneral">
<script type="text/javascript">
changeTreatment(document.forms[1].treatmentTypeID);
</script>
</logic:equal>
<html:hidden property="mophDrugYN" styleId="mophDrugYN" name="frmPreAuthGeneral" />
<html:hidden property="revertFlag" styleId="revertFlag" name="frmPreAuthGeneral" />
<html:hidden property="discountFlag"  name="frmPreAuthGeneral" />
<html:hidden property="external_pat_yn" name="frmPreAuthGeneral" /> 
	</html:form>
</body>


<script>

var assignDate= document.getElementById("assignDateID").value;  
var countDownDate = new Date(assignDate).getTime();
var assiagnYn = document.getElementById("auto_assign_ynID").value;
  if(assiagnYn == 'Y'){
 var x = setInterval(function() {

  // Get today's date and time
  
  var now = new Date().getTime();
    
  // Find the distance between now and the count down date
  var distance = countDownDate - now;

 /*  alert("distance..."+distance); */
  // Time calculations for days, hours, minutes and seconds
  var days = Math.floor(distance / (1000 * 60 * 60 * 24));
  var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
  var seconds = Math.floor((distance % (1000 * 60)) / 1000);
    
  // Output the result in an element with id="demo"
  document.getElementById("demo").innerHTML = hours + "H "
  + minutes + "M " + seconds + "S ";
   
  if(minutes === 5 && seconds === 0){
	  alert("\"5 Minutes Remaining to Complete the Preauthorization , before Auto Release\"");
	  }
  // If the count down is over, write some text 
  
  if (distance < 0) {
    clearInterval(x);
    document.getElementById("demo").innerHTML = "EXPIRED";
  }
}, 1000);
  } 
</script>