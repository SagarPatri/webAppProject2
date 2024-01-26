<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper"%>
<SCRIPT LANGUAGE="JavaScript">
var JS_SecondSubmit=false;
</SCRIPT>
	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
    <script type="text/javascript" src="/ttk/scripts/claims/autoRejectionDetails.js"></script>	
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>  
	<script>
	
	function getProviderInvoiceNO(){
		document.forms[2].providerInvoiceNO.value='';
		 var claimSeqID= document.forms[2].previousClaimNO.value;
		 if(claimSeqID===null || claimSeqID==="" || claimSeqID.length<1)return;
     var  path="/asynchronAction.do?mode=getProviderInvoiceNO&claimSeqID="+claimSeqID;		                 

	 $.ajax({
	     url :path,
	     dataType:"text",
	     success : function(data) {
	    	 document.forms[2].providerInvoiceNO.value=data;
	     }
	 });

}//getAreas
   </script>
<style>

</style>
</head>
<body>
<%
boolean viewmode=true;
%>
<%-- <logic:equal  name="editFlagYN" scope="request" value="Y">
<%
 viewmode=false;
%>
</logic:equal> --%>

 
<html:form action="/ClaimAutoRejectionActivityDetails.do" >

<!-- S T A R T : Page Title -->

		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				    <td width="57%">Activity Details</td>
					<td  align="right" class="webBoard">
					<%--  <logic:notEmpty name="frmAutoRejCalimActivityDetails" property="batchSeqID">
						<%@ include file="/ttk/common/toolbar.jsp" %>
					</logic:notEmpty > --%>
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
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
		<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty> 	
		<logic:notEmpty name="flagValidate" scope="request">
				<table align="center" class="errorContainer" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Wrning" alt="Wrning" width="16" height="16" align="absmiddle">
							<bean:message name="flagValidate" scope="request"/>
					</tr>
				</table>
			</logic:notEmpty>
    <!-- S T A R T : Form Fields -->
	
	<fieldset>
			<legend>General Details</legend>
			<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
			
			 <tr>
			  <!--  <td class="formLabel">Alkoot ID:<span class="mandatorySymbol">*</span> -->
			   <td nowrap>Internal Service Code:<span class="mandatorySymbol">*</span>
			   &nbsp;
			   </td>
			   <td class="textLabel">	      
			      <html:text name="frmAutoRejCalimActivityDetails" property="internalServiceCode" styleClass="textBox textBoxMedium" />
			      </td>	
			
			<td nowrap>Service Date:<span class="mandatorySymbol">*</span></td>
			  <td class="textLabel">
              <html:text name="frmAutoRejCalimActivityDetails" property="serviceDate"  styleClass="textBox textDate" maxlength="20" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].serviceDate',document.forms[1].serviceDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
             </td>
			</tr>
			
			 <tr>
			   <td nowrap>Parent Claim Settlement NO.:<span class="mandatorySymbol">*</span></td>
			    <td class="textLabel">		      
			      <html:text name="frmAutoRejCalimActivityDetails" property="parentClaimSetlmntNo" styleClass="textBox textBoxMedium" />
			      </td>	
			
			  <td nowrap>Service Description:<span class="mandatorySymbol">*</span></td>
			  <td class="textLabel">	
			   <html:text name="frmAutoRejCalimActivityDetails" property="serviceDescription" styleClass="textBox textBoxMedium"  />
			      </td>	
			
			</tr>
			
			<tr>
			   <td nowrap>Activity Type:<span class="mandatorySymbol">*</span></td>
			    <td class="textLabel">	      
			      <html:text name="frmAutoRejCalimActivityDetails" property="activityType" styleClass="textBox textBoxMedium" />
			      </td>	
			
			<td nowrap>CPT Code:</td>
			  <td class="textLabel">		      
			      <html:text name="frmAutoRejCalimActivityDetails" property="cptCode" styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
			      </td>	
			</tr> 
			<tr>
			   <td nowrap>Re-submission Req Amt:</td>
			   <td class="textLabel">	      
			      <html:text name="frmAutoRejCalimActivityDetails" property="reSubReqAmnt" styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
			      </td>	
			
			<td nowrap>Quantity:</td>
			   <td class="textLabel">	      
			      <html:text name="frmAutoRejCalimActivityDetails" property="quantity" styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
			      </td>	
			</tr> 
				<tr>
			  <td nowrap>Tooth Number(for dental):</td>
			   <td class="textLabel">	      
			      <html:text name="frmAutoRejCalimActivityDetails" property="toothNo" styleClass="textBox textBoxMedium" onkeyup="isNumeric(this);" readonly="true" disabled="true"/>
			      </td>	
			<td></td>
			<td></td>
			</tr>
			
			<tr>
			 <td nowrap>AlKoot Remarks:<span class="mandatorySymbol">*</span></td>
			   <td class="textLabel" colspan="3"><html:textarea name="frmAutoRejCalimActivityDetails" property="alkootRemarks" rows="2" cols="80" /></td>
			<td></td>
			<td></td>
			</tr>
			
			
			<tr>
			<td nowrap>Re-Submission Justification:</td>
			 <td class="textLabel" colspan="3"><html:textarea name="frmAutoRejCalimActivityDetails" property="reSubJustification" rows="2" cols="80" readonly="true" disabled="true" />
			 </td>
			<td></td>
			<td></td>
			</tr>
			<tr>
			 <td nowrap>Error Log:</td>
			     <td class="textLabel" colspan="3"><html:textarea name="frmAutoRejCalimActivityDetails" property="errorLogs" rows="2" cols="80" readonly="true" disabled="true"/></td>
			<td></td>
			<td></td>
			</tr>
		
			</table>	
			</fieldset>
		
		<br>
			
			
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
			
			  <tr>
			  <td colspan="4" align="center">
			  
			
             <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onActivitySave()"><u>S</u>ave</button>&nbsp;
              <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onActivityClose()"><u>C</u>lose</button>&nbsp;
         
			<%--  <logic:equal name="frmAutoRejCalimActivityDetails" property="completedYN" value="Y">
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
<%--  <html:hidden property="editFlagYN" name="frmAutoRejCalimActivityDetails" /> --%>
<%--  <logic:equal name="frmAutoRejCalimActivityDetails" property="editFlagYN" value="Y">
<%
viewmode = false;
%>
</logic:equal>
 
  --%>
 
</html:form>

</body>
</html>
