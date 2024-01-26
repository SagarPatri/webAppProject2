
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/reports/cfdReport.js"></script>
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
pageContext.setAttribute("ProviderListSeq",Cache.getCacheObject("ProviderListSeq"));
pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
pageContext.setAttribute("PartnerList",Cache.getCacheObject("PartnerList"));
pageContext.setAttribute("submissionCatagory", Cache.getCacheObject("submissionCatagory"));
pageContext.setAttribute("benefitTypes",Cache.getCacheObject("benefitTypes"));
pageContext.setAttribute("sStatus", Cache.getCacheObject("claimStatusList"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CFDReportAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">CFD Report</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<FIELDSET class="diagnosis_fieldset_class">
	<LEGEND>Report Parameters</LEGEND>
		<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
		<logic:notEqual name="frmCFDReportList" property="switchType" value="CAM">
			<tr class="searchContainerWithTab">
				<td nowrap>Claim/Preauth No.:<br>
		    		<html:text property="sClaimPreauthNO"  styleClass="textBox textBoxLarge" maxlength="20" onkeyup="isNumeric(this)"/>
    	 		</td>
  				<td nowrap>Flag:<br>
					<html:select property="switchType" styleClass="selectBox selectBoxMoreMedium" onchange="onChangeFlag();" styleId="flag">
						<html:option value="">Any</html:option>
						<html:option value="CLM">CLAIM</html:option>
						<html:option value="PAT">PREAUTH</html:option>
						<html:option value="CAM">Campaign</html:option>
					</html:select>    	 		
				</td>  	 		
       	 		<td nowrap>Authorization No:<br>
            		<html:text property="sAuthorizationNo"  styleClass="textBox textBoxLarge" maxlength="20" onkeyup="isNumeric(this)"/>
        		</td> 	 		
    	 		<td nowrap>Batch No.:<br>
            		<html:text property="sBatchNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
   		  		</td>
   		  		<td nowrap>Provider Name:<br>
		    		<html:select property="sProviderName" name="frmCFDReportList"  styleClass="selectBox selectBoxMoreMedium">
		    	 		<html:option value="">Any</html:option>
  						<html:options collection="ProviderListSeq"  property="cacheId" labelProperty="cacheDesc"/>
		    		</html:select>
       		 	</td>
       		 </tr>
       		 
       		 <tr>
       		 	<td nowrap>Claim Type:<br>
	            	<html:select property="sClaimType" styleClass="selectBox selectBoxMoreMedium">
		  	 			<html:option value="">Any</html:option>
		        		<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
            		</html:select>
            	</td>
            	<td nowrap>Internal Remark Status<br>
	            	<html:select property="sInternalRemarkStatus" name="frmCFDReportList" styleClass="selectBox selectBoxMoreMedium">
	               		<html:option value="">Any</html:option>
	               		<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheId" />
	  			   		<%-- <html:option value="SUSP">Suspected Fraud</html:option>
	  				  	<html:option value="SUSP">Alkoot Clarifications</html:option>
	  				  	<html:option value="SUSP">Provider Clarifications</html:option> --%>
		    		</html:select>		    	       
        		</td>	
        	 	<td nowrap>Settlement No.:<br>
            		<html:text property="sSettlementNO"  styleClass="textBox textBoxLarge" maxlength="20" onkeyup="isNumeric(this)"/>
        		</td>    		
          		<td nowrap>Policy No.:<br>
            		<html:text property="sPolicyNumber"  styleClass="textBox textBoxLarge" maxlength="30"/>
        		</td>
        		<td nowrap>Partner Name:<br>
	            	<html:select property="sPartnerName" styleClass="selectBox selectBoxMoreMedium" name="frmCFDReportList">
		  	 			<html:option value="">Any</html:option>
		        		<html:optionsCollection name="PartnerList" label="cacheDesc" value="cacheId" />
            		</html:select>
          		</td>	
	 		</tr>
	 		
    	 	<tr>
    	 		  <td nowrap>Submission Type:<br>
	            	<html:select property="sSubmissionType" styleClass="selectBox selectBoxMoreMedium" >
		  	 			<html:option value="">Any</html:option>
		        		<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheId"/>
            		</html:select>
          		</td>   
		    	<td>Risk Level:<br>
            		<html:select property="sRiskLevel" styleClass="selectBox selectBoxMoreMedium">
		        		<html:option value="">Any</html:option>
		        		<html:option value="HR">High</html:option>
		        		<html:option value="IR">Intermediate</html:option>
		        		<html:option value="LR">Low</html:option>
            		</html:select>
      			</td>  
		    	<td nowrap>Alkoot ID:<br>
		    			<html:text property="sEnrollmentId"  styleClass="textBox textBoxLarge" maxlength="60" onkeyup="isNumeric(this)"/>
		    	</td>      			
				<td nowrap>Member Name:<br>
		    		<html:text property="sMemberName"  styleClass="textBox textBoxLarge" maxlength="250"/>
		    	</td> 
		    	<td nowrap>Benefit Type:<br>
		    		<html:select property="sBenefitType" styleClass="selectBox selectBoxMoreMedium" >
		  	 			<html:option value="">Any</html:option>
		        		<html:optionsCollection name="benefitTypes" label="cacheDesc" value="cacheId" />
            		</html:select>
		    	</td>        			
    	 </tr>

    	 <tr>
				<td nowrap>Invoice No.:<br>
		    		<html:text property="sInvoiceNo"  styleClass="textBox textBoxLarge" maxlength="250"/>
		    	</td>
  		    	<td nowrap>Policy Type:<br>
	            	<html:select property="sPolicyType" styleClass="selectBox selectBoxMoreMedium" >
		  	 			<html:option value="">Any</html:option>
		  	 			<html:option value="IND">Individual</html:option>
		  	 			<html:option value="COR">Corporate</html:option>
            		</html:select>
          		</td>   		    	 
		    	<td nowrap>Claim/Preauth Status:<br>
	            	<html:select property="sClaimPreauthStatus" styleClass="selectBox selectBoxMoreMedium" >
		  	 			<html:option value="">Any</html:option>
		        		<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
            		</html:select>
          		</td>
  		    	<td nowrap>CFD Investigation Status:<br>
	            	<html:select property="sCFDInvestigationStatus" styleClass="selectBox selectBoxMoreMedium" >
		  	 			<html:option value="">Any</html:option>
	                	<html:option value="II">Investigation In-progress </html:option>
	                	<html:option value="CA">Cleared for Approval</html:option>
		  	 			<html:option value="PCA">Partially Cleared For Approval</html:option>
		  	 			<html:option value="FD">Fraud Detected</html:option>
            		</html:select>
          		</td>   	
		    </tr>

		    <tr>
		    <td>CFD Received from Date:<br>
	            	<html:text property="sCFDReceivedFromDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateStartDate(this,'CFD Received from Date');" /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].sCFDReceivedFromDate',document.forms[1].sCFDReceivedFromDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>CFD Received to Date:<br>
	            	<html:text property="sCFDReceivedtoDate"  styleClass="textBox textDate" maxlength="10" onfocus="javascript:validateEndDate(this,frmCFDReportList.sCFDReceivedFromDate,'CFD Received from Date','CFD Received to Date');"  /><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].sCFDReceivedtoDate',document.forms[1].sCFDReceivedtoDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
          </tr> 
       </logic:notEqual> 
       <logic:equal name="frmCFDReportList" property="switchType" value="CAM">
       <tr class="searchContainerWithTab">
				<td nowrap>Campaign Name:<br>
		    		<html:text property="campName" styleId="campName" styleClass="textBox textBoxLarge" maxlength="20" />
    	 		</td>
  				<td nowrap>Flag:<br>
					<html:select property="switchType" styleClass="selectBox selectBoxMoreMedium" onchange="onChangeFlag();" styleId="flag">
						<html:option value="">Any</html:option>
						<html:option value="CLM">CLAIM</html:option>
						<html:option value="PAT">PREAUTH</html:option>
						<html:option value="CAM">Campaign</html:option>
					</html:select>    	 		
				</td>
				<td nowrap>Provider Name:<br>
		    		<html:select property="sProviderName" styleId="sProviderName" name="frmCFDReportList"  styleClass="selectBox selectBoxMoreMedium">
		    	 		<html:option value="">Any</html:option>
  						<html:options collection="ProviderListSeq"  property="cacheId" labelProperty="cacheDesc"/>
		    		</html:select>
       		 	</td>
       		 	<td nowrap>Campaign Status:<br>
		    		<html:select property="campStatus" styleId="campStatus" name="frmCFDReportList"  styleClass="selectBox selectBoxMoreMedium">
		    	 		<html:option value="OPEN">Open</html:option>
  						<html:option value="CLOSED">Closed</html:option>
		    		</html:select>
       		 	</td>
	      </tr>
	      <tr>
		    <td>Campaign Received from Date:<br>
	            	<html:text property="campReceivedFromDate" styleId="campReceivedFromDate" styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].campReceivedFromDate',document.forms[1].campReceivedFromDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Campaign Received to Date:<br>
	            	<html:text property="campReceivedToDate" styleId="campReceivedToDate" styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].campReceivedToDate',document.forms[1].campReceivedToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
            <td>Campaign Start Date:<br>
	            	<html:text property="campStartDate" styleId="campStartDate" styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].campStartDate',document.forms[1].campStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td>Campaign End Date:<br>
	            	<html:text property="campEndDate" styleId="campEndDate" styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].campEndDate',document.forms[1].campEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td>
          </tr> 
       </logic:equal> 		
	</table>
</FIELDSET>

	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" align="center">
				<logic:notEqual name="frmCFDReportList" property="switchType" value="CAM">
					<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"  onClick="javascript:onGenerateCFDReport()"> <u>G</u>enerate Report </button>
				</logic:notEqual>
				<logic:equal name="frmCFDReportList" property="switchType" value="CAM">
				<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"  onClick="javascript:onGenerateCampaginReport()"> <u>G</u>enerate Report </button>
				</logic:equal>	
					&nbsp;
					<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseReportWindow()"> <u>C</u>lose </button>
				</td>
			</tr>
	</table>
	<logic:notEqual name="frmCFDReportList" property="switchType" value="CAM">
		<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><b>Note:</b></td>
			</tr>
			<tr>
				<td><font color="red"> <b><li></b>Maximum you can download 90 days reports only from either start date or end date.</font></td>
			</tr>
		</table>
	</logic:notEqual>	
	</div>

	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
	<!-- E N D : Content/Form Area -->