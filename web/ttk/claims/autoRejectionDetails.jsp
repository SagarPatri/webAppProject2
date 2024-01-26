<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page
	import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper"%>
<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit = false;
</SCRIPT>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript"
	src="/ttk/scripts/claims/autoRejectionDetails.js"></script>
<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script>
	function getProviderInvoiceNO() {
		document.forms[2].providerInvoiceNO.value = '';
		var claimSeqID = document.forms[2].previousClaimNO.value;
		if (claimSeqID === null || claimSeqID === "" || claimSeqID.length < 1)
			return;
		var path = "/asynchronAction.do?mode=getProviderInvoiceNO&claimSeqID="
				+ claimSeqID;

		$.ajax({
			url : path,
			dataType : "text",
			success : function(data) {
				document.forms[2].providerInvoiceNO.value = data;
			}
		});

	}//getAreas
</script>
<style>
</style>
</head>
<body>
	<%-- <%
boolean viewmode=true;
%>
<logic:equal  name="editFlagYN" scope="request" value="Y">
<%
 viewmode=false;
%>
</logic:equal>

  --%>
	<html:form action="/ClaimAutoRejectionGeneralAction.do">

		<!-- S T A R T : Page Title -->

		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				    <td width="57%">Re-submission Error Claims</td>
					<td  align="right" class="webBoard">
					 <logic:notEmpty name="frmAutiRejectionDetail" property="batchSeqID">
						<%@ include file="/ttk/common/toolbar.jsp" %>
					</logic:notEmpty >
			  		</td>
			  </tr>
		</table>	
	<!-- E N D : Page Title -->
		
	<div class="contentArea" id="contentArea">
	<html:errors/>
	<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error" width="16" height="16" align="middle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	
			<!-- S T A R T : Success Box -->
			<logic:notEmpty name="updated" scope="request">
				<table align="center" class="successContainer" style="display:"
					border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" alt="Success"
							width="16" height="16" align="middle">&nbsp; <bean:message
								name="updated" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			<logic:notEmpty name="successMsg" scope="request">
				<table align="center" class="successContainer" style="display:"
					border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" alt="Success"
							width="16" height="16" align="middle">&nbsp; <bean:write
								name="successMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			<logic:notEmpty name="flagValidate" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" alt="Wrning"
							width="16" height="16" align="absmiddle"> <bean:message
								name="flagValidate" scope="request" />
					</tr>
				</table>
			</logic:notEmpty>
			<!-- S T A R T : Form Fields -->

			<fieldset>
				<legend>General Details</legend>
				<table align="center" class="formContainer" border="0"
					cellspacing="0" cellpadding="0">

					<tr>
						<!--  <td class="formLabel">Alkoot ID:<span class="mandatorySymbol">*</span> -->
						<td nowrap>Alkoot ID: &nbsp; <!-- <a href="#" onClick="javascript:selectEnrollmentID();"><img src="/ttk/images/EditIcon.gif" alt="Select Alkoot ID" width="16" height="16" border="0"></a> -->
						</td>
						<td class="textLabel"><html:text
								name="frmAutiRejectionDetail" property="alkootId"
								styleClass="textBox textBoxMedium" onkeyup="isNumeric(this);" />
						</td>

						<td nowrap>Parent Claim No.:</td>
						<td class="textLabel"><html:text
								name="frmAutiRejectionDetail" property="previousClaimNo"
								styleClass="textBox textBoxMedium" /></td>
					</tr>

					<tr>
						<td nowrap>Member Name:</td>
						<td class="textLabel"><html:text
								name="frmAutiRejectionDetail" property="memberName"
								styleClass="textBox textBoxMedium" /></td>
						<td nowrap>Re-Submission Received Date:</td>
						<td class="textLabel"><html:text
								name="frmAutiRejectionDetail" property="reSubRecDate"
								styleClass="textBox textDate" maxlength="20" disabled="true"
								readonly="true" /> <!-- <A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].reSubRecDate',document.forms[1].reSubRecDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a> -->
						</td>
					</tr>
					
					<tr>
					<td nowrap>Action: </td>
                	<td class="textLabel">
            	   <html:select  property="claimAction" styleClass="selectBox selectBoxMoreMedium"  onchange="onActionChange()"  styleId="claimAction">
					<html:option value="REAA">Required Action</html:option>
					<html:option value="REJA">Rejected</html:option>
			</html:select>
     		</td>
					
					<logic:equal  name="frmAutiRejectionDetail" property="claimAction" value="REJA">
					<td nowrap>Rejection Reason:<span class="mandatorySymbol">*</span> </td>
					<td class="textLabel" colspan="3"><html:textarea name="frmAutiRejectionDetail" property="rejectionReason" rows="2" cols="80"/></td>
					</logic:equal>
						
					</tr>
					
					
				</table>
			</fieldset>
			<fieldset>
				<legend>Activity Details</legend>
				<table border="1" cellspacing="0" cellpadding="0"
					class="gridWithCheckBox" style="margin-top: 10PX">
					<tr class="gridHeader">
						<th align="center" style="width: 9%;">Internal Service Code</th>
						<th align="center" style="width: 9%;">Parent Claim Settlement
							NO.</th>
						<th align="center" style="width: 6%;">Service Date</th>
						<th align="center" style="width: 6%;">Activity Type</th>
						<th align="center" style="width: 7%;">Service Desc</th>
						<th align="center" style="width: 8%;">CPT Code</th>
						<th align="center" style="width: 7%;">Re-submission Req Amt</th>
						<th align="center" style="width: 5%;">Quantity</th>
						<th align="center" style="width: 6%;">Tooth NO.</th>
						<th align="center" style="width: 9%;">Alkoot Remarks</th>
						<th align="center" style="width: 9%;">Resubmission
							Justification</th>
						<th align="center" style="width: 38%;">Error Log</th>
						<logic:notEqual  name="frmAutiRejectionDetail" property="claimAction" value="REJ">
						<th align="center" style="width: 38%;">Edit</th>
						</logic:notEqual>
					</tr>

					<%
        			
						java.util.ArrayList<String[]> overRideAlertList = (java.util.ArrayList<String[]>) session
									.getAttribute("activityDetails");
							if (overRideAlertList != null && overRideAlertList.size() > 0) {
								for (String[] strDetails : overRideAlertList) {
					%>
					<tr>
						<td align="center"><%=strDetails[0]%></td>
						<td align="center"><%=strDetails[1]%></td>
						<td align="center"><%=strDetails[2]%></td>
						<td align="center"><%=strDetails[3]%></td>
						<td align="center"><%=strDetails[4]%></td>
						<td align="center"><%=strDetails[5]%></td>
						<td align="center"><%=strDetails[6]%></td>
						<td align="center"><%=strDetails[7]%></td>
						<td align="center"><%=strDetails[8]%></td>
						<td align="center"><%=strDetails[9]%></td>
						<td align="center"><%=strDetails[10]%></td>
						<td align="center"><%=strDetails[11]%></td>
						
						
						<logic:notEqual  name="frmAutiRejectionDetail" property="claimAction" value="REJ">
						    <td align="center"><a href="#" accesskey="g"
							onClick="javascript:edit('<%=strDetails[0]%>','<%=strDetails[1]%>','<%=strDetails[2]%>','<%=strDetails[3]%>','<%=strDetails[4]%>','<%=strDetails[5]%>','<%=strDetails[6]%>','<%=strDetails[7]%>','<%=strDetails[8]%>','<%=strDetails[9]%>','<%=strDetails[10]%>','<%=strDetails[11]%>','<%=strDetails[12]%>','<%=strDetails[13]%>','<%=strDetails[14]%>','<%=strDetails[15]%>');">
								<img src='/ttk/images/EditIcon.gif' alt='Edit Activity Details' width='16' height='16' border='0' align='absmiddle'> </a> </td>
						</logic:notEqual>
					</tr>
					<%
						}
							}
					%>
				</table>
			</fieldset>
			<br>


			<table align="center" class="formContainer" border="0"
				cellspacing="0" cellpadding="0">

				<tr>
					<td colspan="4" align="center">

           <logic:notEqual  name="frmAutiRejectionDetail" property="hiddenClaimAction" value="REJA">
						<button type="button" name="Button2" accesskey="s" class="buttons"
							onMouseout="this.className='buttons'"
							onMouseover="this.className='buttons buttonsHover'"
							onClick="onSave()">
							<u>S</u>ave
						</button>&nbsp;
						</logic:notEqual>
						<logic:notEqual  name="frmAutiRejectionDetail" property="claimAction" value="REJA">
						<button type="button" name="Button2" accesskey="s" class="buttons"
							onMouseout="this.className='buttons'"
							onMouseover="this.className='buttons buttonsHover'"
							onClick="onReEvaluate()">
							<u>R</u>e-Evaluate
						</button>&nbsp;
						</logic:notEqual>
						
						<button type="button" name="Button2" accesskey="s" class="buttons"
							onMouseout="this.className='buttons'"
							onMouseover="this.className='buttons buttonsHover'"
							onClick="onGeneralClose()">
							<u>C</u>lose
						</button>&nbsp; <%--  <logic:equal name="frmAutiRejectionDetail" property="completedYN" value="Y">
			 <%
             if(TTKCommon.isAuthorized(request,"Override")) {
             %>
            <button type="button" name="Button" accesskey="o" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:override()"><u>O</u>verride</button>&nbsp;
          <%
            }
          %>
			</logic:equal> --%>
					</td>
				</tr>
			</table>

		</div>
		<INPUT TYPE="hidden" NAME="rownum">
		<INPUT TYPE="hidden" NAME="mode" VALUE="">
		<input type="hidden" name="child" value="">
		<input type="hidden" name="editFlagYN" value="">
		<html:hidden property="xmlSeqId" name="frmAutiRejectionDetail" />
		<html:hidden property="hiddenPreviousClamNo" name="frmAutiRejectionDetail" />
		<%--  <logic:equal name="frmAutiRejectionDetail" property="editFlagYN" value="Y">
<%
viewmode = false;
%>
</logic:equal>
 
  --%>

	</html:form>

</body>
</html>
