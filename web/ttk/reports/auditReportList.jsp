
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/reports/auditReport.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>


<script>
bAction=false;
var TC_Disabled = true;
var JS_SecondSubmit=false;
</script>

<style>
.buttons {
    margin-left: 20px;
    margin-right: 20px;
}
</style>
<%
pageContext.setAttribute("auditStatusList", Cache.getCacheObject("auditStatusList"));



pageContext.setAttribute("sAmount", Cache.getCacheObject("amount"));
pageContext.setAttribute("sSource", Cache.getCacheObject("source"));
pageContext.setAttribute("sStatus", Cache.getCacheObject("claimStatusList"));
pageContext.setAttribute("sTtkBranch", Cache.getCacheObject("officeInfo"));
pageContext.setAttribute("sPreAuthType", Cache.getCacheObject("preauthType"));
pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
pageContext.setAttribute("ProviderList",Cache.getCacheObject("ProviderList"));
pageContext.setAttribute("insuranceCompany", Cache.getCacheObject("insuranceCompany"));
pageContext.setAttribute("modeOfClaims", Cache.getCacheObject("modeOfClaims"));
pageContext.setAttribute("submissionCatagory", Cache.getCacheObject("submissionCatagory"));
pageContext.setAttribute("sAssignedTo", Cache.getCacheObject("assignedTo"));
pageContext.setAttribute("clmShortFallStatus",Cache.getCacheObject("preShortfallStatus"));
pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
pageContext.setAttribute("benefitTypes",Cache.getCacheObject("benefitTypes"));
pageContext.setAttribute("partnersList",Cache.getCacheObject("partnersList"));

%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/AuditReportAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">Audit Report</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<table align="center" class="tablePad"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="10%" nowrap class="textLabelBold">Switch to:</td>
        <td width="90%">
	        <html:select property="switchType" styleClass="specialDropDown" styleId="switchType" onchange="javascript:onSwitch()">
				<html:option value="CLM">CLAIM</html:option>
				<html:option value="PAT">PREAUTH</html:option>
			</html:select>
		</td>
		</tr>
    </table>
	<FIELDSET class="diagnosis_fieldset_class">
			<LEGEND>Report Parameters</LEGEND>
			<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
	<tr class="searchContainerWithTab">
	<logic:equal property="switchType" name="frmAuditReportList" value="CLM">
	<td nowrap>Audit Status:<br>
		    	<html:select property="auditStatus" name="frmAuditReportList"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Any</html:option>
  				 <html:options collection="auditStatusList"  property="cacheId" labelProperty="cacheDesc"/> 
		    </html:select>
    	 </td>
	</logic:equal>
		
    	 
    	 <td nowrap>Provider Name:<br>
		    <html:select property="sProviderName" name="frmAuditReportList"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Any</html:option>
  				<html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
		    </html:select>
        </td>
        <logic:equal property="switchType" name="frmAuditReportList" value="CLM">
    	 <td nowrap>Batch No.:<br>
            	<html:text property="sBatchNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
   		  </td>
   		  
   		  <td nowrap>Claim No.:<br>
		    	<html:text property="sClaimNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
    	 </td>
    	 
    	 <td nowrap>Settlement No.:<br>
            	<html:text property="sSettlementNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
        	</logic:equal>
        	<logic:equal property="switchType" name="frmAuditReportList" value="PAT">
        	 <td nowrap>Pre-Auth No.:<br>
		    	<html:text property="preauthNumber"  styleClass="textBox textBoxLarge" maxlength="60"/>
    	 </td>
    	 
    	 <td nowrap>Authorization No.:<br>
            	<html:text property="authorizationNo"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
        	</logic:equal>
    	 </tr>
    	 <tr>

			<td nowrap>Member Name:<br>
		    	<html:text property="sClaimantName"  styleClass="textBox textBoxLarge" maxlength="250"/>
		    </td>
		    
		    <td nowrap>Enrollment Id:<br>
		    	<html:text property="sEnrollmentId"  styleClass="textBox textBoxLarge" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement)"/>
		    </td>
		    
		    <td nowrap>Status:<br>
	            <html:select property="sStatus" styleClass="selectBox selectBoxMoreMedium" >
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
          	
          	<td nowrap>Policy No.:<br>
            	<html:text property="sPolicyNumber"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
        	 <%-- <td nowrap>Product Name:<br>
		    	<html:text property="productName"  styleClass="textBox textBoxLarge" maxlength="60" />
		    </td> --%>
		    </tr>
		    
		    
		    <logic:equal property="switchType" name="frmAuditReportList" value="CLM">
		    <tr>
		    <td>Claim received from Date:<br>
	            	<html:text property="claimRecvdFrmDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateStartDate(this,'Claim received from Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].claimRecvdFrmDate',document.forms[1].claimRecvdFrmDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Claim received to Date:<br>
	            	<html:text property="claimRecvdToDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateEndDate(this,frmAuditReportList.claimRecvdFrmDate,'Claim received from Date','Claim received to Date');"  /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].claimRecvdToDate',document.forms[1].claimRecvdToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Claim processed from Date:<br>
	            	<html:text property="claimProcessedFrmDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateStartDate(this,'Claim processed from Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].claimProcessedFrmDate',document.forms[1].claimProcessedFrmDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Claim processed to Date:<br>
	            	<html:text property="claimProcessedToDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateEndDate(this,frmAuditReportList.claimProcessedFrmDate,'Claim processed from Date','Claim processed to Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].claimProcessedToDate',document.forms[1].claimProcessedToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		     <td>Claim Type:<br>
	            <html:select property="sClaimType" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
            	</html:select>
            	</td>
            	</tr><tr>
            <td>Date of Treatment from Date:<br>
	            	<html:text property="claimTreatmentFrmDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateStartDate(this,'Date of Treatment from Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].claimTreatmentFrmDate',document.forms[1].claimTreatmentFrmDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td>
		    <td>Date of Treatment to Date:<br>
	            	<html:text property="claimTreatmentToDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateEndDate(this,frmAuditReportList.claimTreatmentFrmDate,'Date of Treatment from Date','Date of Treatment to Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].claimTreatmentToDate',document.forms[1].claimTreatmentToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td>
            	</tr>
            </logic:equal>
		    <logic:equal property="switchType" name="frmAuditReportList" value="PAT">
		    <tr>
		    <td>Pre-approval received from Date:<br>
	            	<html:text property="preauthRecvdFrmDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateStartDate(this,'Pre-approval received from Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].preauthRecvdFrmDate',document.forms[1].preauthRecvdFrmDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Pre-approval received to Date:<br>
	            	<html:text property="preauthRecvdToDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateEndDate(this,frmAuditReportList.preauthRecvdFrmDate,'Pre-approval received from Date','Pre-approval received to Date');"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].preauthRecvdToDate',document.forms[1].preauthRecvdToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Pre-approval processed from Date:<br>
	            	<html:text property="preauthProcessedFrmDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateStartDate(this,'Pre-approval processed from Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].preauthProcessedFrmDate',document.forms[1].preauthProcessedFrmDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Pre-approval processed to Date:<br>
	            	<html:text property="preauthProcessedToDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateEndDate(this,frmAuditReportList.preauthProcessedFrmDate,'Pre-approval processed from Date','Pre-approval processed to Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].preauthProcessedToDate',document.forms[1].preauthProcessedToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
            </tr><tr>
            <td>Date of Treatment from Date:<br>
	            	<html:text property="preauthTreatmentFrmDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateStartDate(this,'Date of Treatment from Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].preauthTreatmentFrmDate',document.forms[1].preauthTreatmentFrmDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Date of Treatment to Date:<br>
	            	<html:text property="preauthTreatmentToDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateEndDate(this,frmAuditReportList.preauthTreatmentFrmDate,'Date of Treatment from Date','Date of Treatment to Date');"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].preauthTreatmentToDate',document.forms[1].preauthTreatmentToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td>
            	</tr>
            </logic:equal>
       		
	</table>
		</FIELDSET>
	
	<table align="center" class="buttonsContainer" border="0"
			cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" align="center">
					<button type="button" name="Button" accesskey="g" class="buttons"
						onMouseout="this.className='buttons'"
						onMouseover="this.className='buttons buttonsHover'"
						onClick="javascript:onGenerateAuditReport()">
						<u>G</u>enerate Report
					</button>&nbsp;
					
					<button type="button" name="Button" accesskey="c" class="buttons"
						onMouseout="this.className='buttons'"
						onMouseover="this.className='buttons buttonsHover'"
						onClick="javascript:onCloseReportWindow()">
						<u>C</u>lose
					</button>
				</td>
			</tr>
		</table>
		<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td><b>Note:</b></td>
		</tr>
			<tr>
				<td>
				<font color="red"> <b><li></b>Maximum you can download 90 days reports only from either start date or end date.</font></td>
			</tr>
			
			
		</table>
	</div>

		
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
	<!-- E N D : Content/Form Area -->