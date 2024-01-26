<html>
<body>
<%
/**
 * @ (#) usersupport.jsp
 * Project      : TTK Software Support
 * File         : usersupport.jsp
 * Author       : Vamsi Krishna CH
 * Company      : 
 * Date Created : 
 *
 * @author       : Vamsi Krishna CH
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.business.common.SecurityManagerBean,com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@page import="java.util.ArrayList,com.ttk.dto.insurancepricing.PolicyConfigVO" %>


<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
    <script type="text/javascript" src="/ttk/scripts/insurancepricing/swpolicydesignconfig.js"></script>	
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
<SCRIPT LANGUAGE="JavaScript">
	bAction = false; //to avoid change in web board in product list screen //to clarify
	var TC_Disabled = true;
</SCRIPT>
 <%

	int iRowCount = 0;

%>
<!-- S T A R T : Search Box -->
	 

	<html:form action="/SwUpdateGenerateQuotationAction.do" method="post">
	
	<logic:notEmpty name="fileName" scope="request">
		<script language="JavaScript">
			var w = screen.availWidth - 10;
			var h = screen.availHeight - 82;
			var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
			window.open("/SwFinalOutputAction.do?mode=doPrintpdf&displayFile=<bean:write name="fileName"/>&reportType=PDF",'PrintAcknowledgement',features);
		</script>
	</logic:notEmpty>
	
	
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="65%"></td> 
		  </tr>
	</table>
	<div class="contentArea" id="contentArea">
	<html:errors />
	<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
	<logic:match name="frmSwPolicyConfigQuote" property="Message" value="N">
	
	<br>
	<fieldset><legend>Gross  Premium </legend>
   <table width="100%"><tr><td>
     <table align="left" class="gridWithPricing" border="0" cellspacing="1" cellpadding="0">
  <tr>
		<td width="50%" ID="listsubheader"  colspan="" CLASS="gridHeader">Benefit &nbsp;</td>
		<td width="50%" ID="listsubheader"  colspan="" CLASS="gridHeader">Final gross premium per covered member per policy year &nbsp;</td>
		
  </tr>
  
   <tr class="gridOddRow">
  	 <td class="formLabel" width="50%">Inpatient  </td>
       <td class="formLabel" >
		<%-- <html:text name="frmSwPolicyConfigQuote" property="fininPatientCPM" readonly="true" styleClass="textBox textBoxSmall" /> --%>
		<b><bean:write property="fininPatientCPM" name="frmSwPolicyConfigQuote"/></b>
		</td>
	
  </tr>
   	<tr class="gridEvenRow">
  	 <td class="formLabel" width="50%">Outpatient  </td>
       <td class="formLabel">
<%-- 		<html:text name="frmSwPolicyConfigQuote" property="finoutPatientCPM" readonly="true" styleClass="textBox textBoxSmall" />
 --%>		<b><bean:write property="finoutPatientCPM" name="frmSwPolicyConfigQuote"/></b>
		</td>
	
  </tr>
  
  	<tr class="gridOddRow">
  	 <td class="formLabel" width="50%">Maternity  </td>
       <td class="formLabel">
		<b><bean:write property="finmaternityCPM" name="frmSwPolicyConfigQuote"/></b>
<%-- 		<html:text name="frmSwPolicyConfigQuote" property="finmaternityCPM" readonly="true" styleClass="textBox textBoxSmall" />
 --%>		</td>
	
  </tr>
  
  <tr class="gridEvenRow">
  	 <td class="formLabel" width="50%">Optical  </td>
       <td class="formLabel">
<%-- 		<html:text name="frmSwPolicyConfigQuote" property="finopticalCPM" readonly="true" styleClass="textBox textBoxSmall" />
 --%>		<b><bean:write property="finopticalCPM" name="frmSwPolicyConfigQuote"/></b>
		</td>
	
  </tr>
  
   	<tr class="gridOddRow">
  	 <td class="formLabel" width="50%">Dental </td>
       <td class="formLabel">
<%-- 		<html:text name="frmSwPolicyConfigQuote" property="findentalCPM" readonly="true" styleClass="textBox textBoxSmall" />
 --%>		<b><bean:write property="findentalCPM" name="frmSwPolicyConfigQuote"/></b>
		</td>
	
  </tr>
  </table>
  </td></tr><tr><td>

  <table align="left" class="gridWithPricing" border="0" cellspacing="1" cellpadding="0">
    <tr class="gridEvenRow">
  	 <td ID="listsubheader"  CLASS="gridHeader" width="50%">All excluding maternity  </td>
       <td ID="listsubheader"  CLASS="gridHeader" >
<%-- 		<html:text name="frmSwPolicyConfigQuote" property="finallExlMaternity" readonly="true" styleClass="textBox textBoxSmall" />
 --%>		<bean:write property="finallExlMaternity" name="frmSwPolicyConfigQuote"/>
		</td>
	
  </tr>
  
   	<tr class="gridEvenRow">
  	 <td ID="listsubheader"  CLASS="gridHeader"  width="50%">Maternity  </td>
       <td ID="listsubheader"  CLASS="gridHeader" >
		<bean:write property="finmaternityCPM" name="frmSwPolicyConfigQuote"/>
<%-- 		<html:text name="frmSwPolicyConfigQuote" property="finmaternityCPM" readonly="true" styleClass="textBox textBoxSmall" />
 --%>		</td>
	
  </tr>
    
  </table>
  </td></tr>
  <tr>
  <td>
   <tr><td>
  <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">
			 <button type="button" name="Button" accesskey="b" class="buttonsPricingSmall" onMouseout="this.className='buttonsPricingSmall'" onMouseover="this.className='buttonsPricingSmall buttonsHover'" onClick="javascript:onProceedProposal();"><u>P</u>roceed</button>&nbsp;
			 <button type="button" name="Button" accesskey="b" class="buttonsPricingSmall" onMouseout="this.className='buttonsPricingSmall'" onMouseover="this.className='buttonsPricingSmall buttonsHover'" onClick="javascript:onCloseOutput();"><u>B</u>ack</button>&nbsp;

</td>
		
			
		</tr>
	</table>
  
  
  </td>
  
  </tr>
  
  
  
  
  
  
  </table>
  
  
  
	</fieldset>
	
	
	<fieldset>
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="left">
			&nbsp;<a href="#" id="discrepancies" onclick="javascript:onViewInputSummary();"><i><b>Click to view pricing summary</b></i></a>&nbsp;&nbsp;&nbsp;
			
<!-- 			<button type="button" name="Button" accesskey="a" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'"	onClick="javascript:onViewInputSummary();">View <u>I</u>nput Summary</button>&nbsp;
 -->		</td>
		
			
		</tr>
	</table>
	</fieldset>
	
	</logic:match>
	 </div>
   <input type="hidden" name="mode" value=""/>
   <input type="hidden" name="child" value="">
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<INPUT TYPE="hidden" NAME="groupseqid" VALUE="<bean:write name="frmSwPolicyConfigQuote" property="lngGroupProfileSeqID" />">
	<INPUT TYPE="hidden" NAME="addedBy" VALUE="<%=(TTKCommon.getUserSeqId(request))%>">

   <input type="hidden" name="parameter" value=""/>
   <input type="hidden" name="reportType" value=""/>
   
   </html:form> 
   </body>
   </html>



