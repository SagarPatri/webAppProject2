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
    <script type="text/javascript" src="/ttk/scripts/claims/batchgeneral.js"></script>	
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
	boolean bEnabled=false;
	boolean viewmode1=true;
	String strSubmissionType="";
	String ampm[] = {"AM","PM"};
	
	boolean blnAmmendmentFlow=false;
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
		viewmode1=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	pageContext.setAttribute("ampm",ampm);	
	pageContext.setAttribute("ProviderList",Cache.getCacheObject("ProviderList"));
	pageContext.setAttribute("memberClaimFrom",Cache.getCacheObject("MemberClaimFromList"));
	pageContext.setAttribute("claimsSubmissionTypes", Cache.getCacheObject("claimsSubmissionTypes"));
	pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
	pageContext.setAttribute("viewmode",new Boolean(viewmode));	
	pageContext.setAttribute("encounterTypes", Cache.getCacheObject("encounterTypes"));
	pageContext.setAttribute("sTtkBranch", Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("modeOfClaims", Cache.getCacheObject("modeOfClaims"));
	pageContext.setAttribute("benefitTypes", Cache.getCacheObject("benefitTypes"));
	pageContext.setAttribute("submissionCatagory",Cache.getCacheObject("submissionCatagory"));
	//pageContext.setAttribute("partnerName",Cache.getCacheObject("partnerName"));
	pageContext.setAttribute("paymentTo",Cache.getCacheObject("paymentTo"));
	pageContext.setAttribute("descriptionCode", Cache.getCacheObject("claimsDocUpload"));
	boolean network=false;
	boolean batchOverrideYN=false;
%>
<logic:equal name="frmClaimBatchGeneral" property="batchOverrideAllowYN" value="N">
<%
	batchOverrideYN = true;
%>
	</logic:equal>
<html:form action="/SaveClaimBatchGeneralAction.do" >
<!-- S T A R T : Page Title -->

		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				    <td width="57%">General Details - <bean:write name="frmClaimBatchGeneral" property="caption"/></td>
					<td  align="right" class="webBoard">
					 <logic:notEmpty name="frmClaimBatchGeneral" property="batchSeqID">
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
			<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td width="22%" class="formLabel">Batch NO.:</td>
			    <td width="30%" class="textLabelBold">
			    <bean:write name="frmClaimBatchGeneral" property="batchNO"/>
			    <html:hidden name="frmClaimBatchGeneral" property="batchSeqID"  />
			    </td>
			   	<td class="formLabel">Submission catagory:<span
							class="mandatorySymbol">*</span></td>
						<td width="30%" class="textLabel">
							<html:select property="processType" onchange="setNetWorkType()" styleClass="selectBox selectBoxMedium" disabled="<%=batchOverrideYN%>">
								<html:option value="">Select from list</html:option>
								<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheId" />
							</html:select></td>
							<td></td><td></td>
							</tr>
			    
			    
			    <tr>
			    <td class="formLabel" width="19%">Mode Of Claims:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
			      <logic:equal value="PLCL" property="modeOfClaim" name="frmClaimBatchGeneral">
					Provider Login
					</logic:equal>
					 <logic:equal value="PBML" property="modeOfClaim" name="frmClaimBatchGeneral">
					PBM Login
					</logic:equal>
				<logic:notEqual value="PBML" property="modeOfClaim" name="frmClaimBatchGeneral">
				<logic:notEqual value="PLCL" property="modeOfClaim" name="frmClaimBatchGeneral">
				      <html:select name="frmClaimBatchGeneral" property="modeOfClaim" styleClass="selectBox selectBoxMedium" disabled="<%=batchOverrideYN%>">
							<html:option value="">Select From List</html:option>
							<html:optionsCollection name="modeOfClaims" label="cacheDesc" value="cacheId" />
					  </html:select>
				</logic:notEqual>
				</logic:notEqual>
			      </td>
			      <td class="formLabel" width="19%">Al Koot Branch:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				      <html:select name="frmClaimBatchGeneral" property="vidalBranchName" styleClass="selectBox selectBoxLarge" disabled="<%=batchOverrideYN%>">							
							<html:optionsCollection name="sTtkBranch" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
			    </tr>
			    <tr>
			    <td width="22%" class="formLabel">Received Date:<span class="mandatorySymbol">*</span></td>
			    <td width="30%" class="textLabelBold">
			    <table cellspacing="0" cellpadding="0">
			    	<tr>
			    		<td>
			    		<html:text name="frmClaimBatchGeneral" property="batchReceiveDate" styleClass="textBox textDate" maxlength="10" readonly="<%=batchOverrideYN%>"/>
			    		<logic:notEqual name="frmClaimBatchGeneral" property="batchOverrideAllowYN" value="N">
			    			<A NAME="CalendarObjectPARDate"  ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmClaimBatchGeneral.batchReceiveDate',document.frmClaimBatchGeneral.batchReceiveDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" name="empDate" width="24" height="17" border="0" align="middle" ></a>
			    		</logic:notEqual>
			    		</td>
			    		<td><html:text name="frmClaimBatchGeneral" property="receivedTime"  styleClass="textBox textTime" maxlength="5" readonly="<%=batchOverrideYN%>"/>&nbsp;</td>
			    		<td><html:select name="frmClaimBatchGeneral" property="receiveDay" styleClass="selectBox" disabled="<%=batchOverrideYN%>"><html:options name="ampm" labelName="ampm"/></html:select></td>
			    		 <td>
			    			 <logic:match name="frmClaimBatchGeneral" property="fastTrackFlag" value="Y">
									&nbsp;&nbsp;<td align="left"><img title="<bean:write name="frmClaimBatchGeneral" property="fastTrackMsg"/>" alt="<bean:write name="frmClaimBatchGeneral" property="fastTrackMsg"/>" src="/ttk/images/fastTrackImg.gif"  width="20" height="20" >
							 </logic:match>
			    		</td>
			    	</tr>
			    </table>
			    </td>
			    <td class="formLabel" width="19%">Claims Submission Type:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				      <html:select name="frmClaimBatchGeneral" property="submissionType" styleClass="selectBox selectBoxMedium" disabled="<%=batchOverrideYN%>">
				            <html:option value="">Select From List</html:option>
							<html:optionsCollection name="claimsSubmissionTypes" label="cacheDesc" value="cacheId" />
					  </html:select>
			      </td>
			    </tr>
			    
			    <tr>
			     <td class="formLabel" width="19%">Claim Type:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				      <html:select name="frmClaimBatchGeneral" property="claimType" styleClass="selectBox selectBoxMedium" onchange="setNetWorkType()" disabled="<%=batchOverrideYN%>">
							  <html:option value="">Select from list</html:option>
							<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
					  </html:select>
			      </td>
			     <td class="formLabel" width="19%">Network Type:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
			      <logic:equal value="CNH" property="claimType" name="frmClaimBatchGeneral">
<%-- 			      <logic:equal value="RGL" property="processType" name="frmClaimBatchGeneral">
 --%>			      <%network=true; %>
			      </logic:equal>
			      <logic:equal name="frmClaimBatchGeneral" property="batchOverrideAllowYN" value="N">
 			      		<%network=true; %>
			      </logic:equal>
				      <html:select name="frmClaimBatchGeneral" property="netWorkType" 	onchange="changenetwork()" styleClass="selectBox selectBoxMedium" disabled="<%=network%>" ><!-- onchange="setProviderType()" -->
							  <html:option value="">Select from list</html:option>
							  <html:option value="Y">YES</html:option>
							  <html:option value="N">NO</html:option>
					  </html:select>
			      </td>			     
			  </tr>
			  
			  	<logic:match value="DBL" name="frmClaimBatchGeneral" property="processType">
						 <logic:match value="CNH" property="claimType" name="frmClaimBatchGeneral">
					
					 <tr><td class="formLabel">Payment To :<span class="mandatorySymbol">*</span></td>
					 <td class="textLabel">
							 <html:select
								name="frmClaimBatchGeneral" property="paymentTo" styleId ="paymentToId" onchange="changenetwork()"
								styleClass="selectBox selectBoxMedium">
								<html:optionsCollection name="paymentTo"
									label="cacheDesc" value="cacheId" />
							</html:select>
					 </td> 
					<td id="partnerIdLabel" style="display:inline;" class="formLabel">Partner Name:<span class="mandatorySymbol">*</span></td>
					  <td id="partnerId" style="display:inline;" class="textLabel">
							 <html:select name="frmClaimBatchGeneral" property="partnerName" styleClass="selectBox selectBoxMedium">
							 	   <logic:notEmpty name="partnersList" scope="session">
									<html:option value="">Select from list</html:option>
									<html:optionsCollection name="partnersList" value="key" label="value" />
								</logic:notEmpty>
							</html:select>
					 </td>
					 </tr>
				</logic:match>
				</logic:match>
				
			
			   
			     <logic:equal value="CNH" property="claimType" name="frmClaimBatchGeneral">
			     	<logic:equal value="Y" property="netWorkType" name="frmClaimBatchGeneral">
			     	 <logic:equal value="DBL" property="processType" name="frmClaimBatchGeneral">
			      <tr id  ="providerId" style="display:none">
			      <td class="formLabel" width="19%">Provider Name:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				      <html:select name="frmClaimBatchGeneral" property="providerID" styleClass="selectBox selectBoxLarge" onchange="setProviderID()" disabled="<%=batchOverrideYN%>">
							  <html:option value="">Select from list</html:option>
							<html:optionsCollection name="ProviderList" label="cacheDesc" value="cacheId" />
					  </html:select>
			      </td>
			      <td class="formLabel" width="19%">Provider License ID:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				     <html:text name="frmClaimBatchGeneral" property="providerLisenceNO" readonly="true"  styleClass="textBox textBoxMedium textBoxDisabled"/>
			      <html:hidden name="frmClaimBatchGeneral" property="providerName"/>
			      </td>
			       </tr>
			      </logic:equal>
			    </logic:equal>
			    </logic:equal>
			      <logic:equal value="CNH" property="claimType" name="frmClaimBatchGeneral">
			     	<logic:equal value="Y" property="netWorkType" name="frmClaimBatchGeneral">
			     	 <logic:equal value="RGL" property="processType" name="frmClaimBatchGeneral">
			      <tr id  ="providerId" style="display:">
			      <td class="formLabel" width="19%">Provider Name:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				      <html:select name="frmClaimBatchGeneral" property="providerID" styleClass="selectBox selectBoxLarge" onchange="setProviderID()" disabled="<%=batchOverrideYN%>">
							  <html:option value="">Select from list</html:option>
							<html:optionsCollection name="ProviderList" label="cacheDesc" value="cacheId" />
					  </html:select>
			      </td>
			      <td class="formLabel" width="19%">Provider ID:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				     <html:text name="frmClaimBatchGeneral" property="providerLisenceNO" readonly="true"  styleClass="textBox textBoxMedium textBoxDisabled"/>
			      <html:hidden name="frmClaimBatchGeneral" property="providerName"/>
			      </td>
			       </tr>
			      </logic:equal>
			    </logic:equal>
			    </logic:equal>
			    
				  
			    
			    
			    
			   
			    <tr>
			    <logic:equal value="CTM" property="claimType" name="frmClaimBatchGeneral">
			      <td class="formLabel" width="19%">Claim From:<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				      <html:select name="frmClaimBatchGeneral" property="claimFrom" styleClass="selectBox selectBoxLarge" disabled="<%=batchOverrideYN%>">
							  <html:option value="">Select from list</html:option>
							<html:optionsCollection name="memberClaimFrom" label="cacheDesc" value="cacheId" />
					  </html:select>
			      </td>
			      <td class="formLabel" width="19%"></td>
			      <td class="textLabel"></td>
			    </logic:equal>
			    <logic:equal value="CTM" property="claimType" name="frmClaimBatchGeneral">
			      <html:hidden name="frmClaimBatchGeneral" property="providerID"/>
			      <html:hidden name="frmClaimBatchGeneral" property="providerLisenceNO"/>
			      <html:hidden name="frmClaimBatchGeneral" property="providerName"/>
			      	<html:hidden name="frmClaimBatchGeneral" property="paymentTo"/>
			    	<html:hidden name="frmClaimBatchGeneral" property="partnerName"/> 
			    </logic:equal>
			  </tr>
			  <tr>
			   <td class="formLabel">No. Of Claims Received:<span class="mandatorySymbol">*</span></td>
			      <td  width="30%" class="textLabel">		      
			      <html:text name="frmClaimBatchGeneral" property="noOfClaimsReceived" styleClass="textBox textBoxMedium" onkeyup="isNumeric(this);"/>
			      </td>	
			       <td class="formLabel">Batch Total Amount:<span class="mandatorySymbol">*</span></td>
			      <td  width="30%" class="textLabel">			      
			      <html:text property="totalAmount" name="frmClaimBatchGeneral" styleClass="textBox textBoxMedium" onkeyup="isNumeric(this);"/>
			       <!--  <logic:empty property="totalAmountCurrency" name="frmClaimBatchGeneral">
			      <html:text property="totalAmountCurrency" value="QAR" styleId="totalAmountCurrency"  styleClass="textBox textBoxSmall" readonly="true" />
			      </logic:empty>
			       <logic:notEmpty property="totalAmountCurrency" name="frmClaimBatchGeneral">
			       <html:text property="totalAmountCurrency"  styleId="totalAmountCurrency"  styleClass="textBox textBoxSmall" readonly="true" />
			      </logic:notEmpty>
			     <a href="#" onclick="openRadioList('totalAmountCurrency','CURRENCY_GROUP','option')">
		          <img src="/ttk/images/search_edit.gif" width="18" height="18" alt="Select Currency" border="0" align="bottom" ></a> -->
			      </td>		
			  </tr>
			  <tr>
			  <td class="formLabel">Batch Status:</td>
			  <td>
	        <html:select property="batchStatus"  styleClass="selectBox selectBoxMedium" name="frmClaimBatchGeneral">
  				<html:option value="INP">In Progress</html:option>
  				<html:option value="COMP">Complete</html:option>
	    	</html:select>
		   </td>
		   <td></td>
		   <td></td>
			  </tr>
			  <tr>
			  <td colspan="4" align="center">
			  
			 <%
             if(TTKCommon.isAuthorized(request,"Edit")) {
             %>
            <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onSave()"><u>S</u>ave</button>&nbsp;
          <%
            }
          %>
			 <logic:equal name="frmClaimBatchGeneral" property="completedYN" value="Y">
			 <%
             if(TTKCommon.isAuthorized(request,"Override")) {
             %>
            <button type="button" name="Button" accesskey="o" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:override()"><u>O</u>verride</button>&nbsp;
          <%
            }
          %>
			</logic:equal>
			  </td>
			  </tr>			  
			  </table>
		</fieldset>
 <html:hidden  name="frmClaimBatchGeneral"  property="validateYN" styleId="validateYN"/>
 <html:hidden  name="frmClaimBatchGeneral"  property="overrideYN" styleId="overrideYN"/>
 <html:hidden  name="frmClaimBatchGeneral"  property="completedYN"/>
 <INPUT TYPE="hidden" NAME="rownum">
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
</html:form>
<html:form action="/SaveAddClaimDetailsAction.do" method="post" enctype="multipart/form-data">
		<logic:notEmpty name="frmClaimBatchGeneral" property="batchSeqID">
		
		<fieldset>	         
			<legend>Claim Details</legend>
			 <br>
		     <logic:equal value="DTC" property="submissionType" name="frmClaimBatchGeneral">
		     <table  align="center"  border="0" cellspacing="0" cellpadding="0">
			  <%strSubmissionType="DTC"; %>
			  <tr>	  
			   <td class="formLabel">Provider Invoice No.:<span class="mandatorySymbol">*</span></td> 
			   <td class="textLabel">
			   <html:text  name="frmAddClaimDetails"  property="providerInvoiceNO"  styleClass="textBox textBoxLarge" maxlength="300"/>
			   <html:hidden  name="frmAddClaimDetails"  property="previousClaimNO"/>
			   </td>
			   <td class="formLabel">&nbsp;&nbsp; Requested Amount:<span class="mandatorySymbol">*</span></td>	
			   <td class="textLabel">
			   <html:text  name="frmAddClaimDetails"  property="requestedAmount"  styleClass="textBox textBoxSmall" onkeyup="isNumeric(this);" maxlength="10"/>
			  </td>	
			  </tr>  
			  <tr>	  
			   <td class="formLabel">Description:</td> 
			   <td class="textLabel">   			
          				<html:select property ="description" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="descriptionCode" property="cacheId" labelProperty="cacheDesc"/>
          				</html:select>
          			</td> 
          			<td class="formLabel">&nbsp;&nbsp;Browse File :</td>	      
						<td>
							<html:file property="file" styleId="file"/>
						</td> 
          	    </tr>
			  <tr>
			  <td colspan="4" align="center">
			  <logic:empty name="frmAddClaimDetails" property="claimSeqID">
			    <button type="button" name="Button3" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="addClaimDetails()">A<u>d</u>d</button>&nbsp;
			  </logic:empty>
			    <logic:notEmpty name="frmAddClaimDetails" property="claimSeqID">
			    <button type="button" name="Button3" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="addClaimDetails()">Edit</button>&nbsp;
			    </logic:notEmpty>
			    </td>	  
			 </tr>	 
			  </table>  
		</logic:equal>
		 <logic:equal value="DTR" property="submissionType" name="frmClaimBatchGeneral">
		 <table  align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		 <%strSubmissionType="DTR"; %>
			 <tr>
			  <td class="formLabel">
			  Al koot ID:<span class="mandatorySymbol">*</span>
			  &nbsp;&nbsp;<a href="#" onClick="javascript:selectEnrollmentID();"><img src="/ttk/images/EditIcon.gif" title="Select Alkoot ID" alt="Select Alkoot ID" width="16" height="16" border="0"></a>
			  </td>
			  <td class="textLabelBold">
			   <bean:write name="frmAddClaimDetails" property="enrollmentID"/>
			   <html:hidden  name="frmAddClaimDetails"  property="enrollmentID"/>
			   <html:hidden  name="frmAddClaimDetails"  property="enrollmentSeqID"/>
			   </td>
			  <td class="formLabel">Previous Claim No.:<span class="mandatorySymbol">*</span></td>	
			   <td class="textLabel">			  
			    
			     <logic:empty name="previousClaimNums">
			     <html:select property="previousClaimNO" name="frmAddClaimDetails" styleClass="selectBox selectBoxLarge">
			     <html:option value="">Select from list</html:option>
			     </html:select>
			     </logic:empty>
			     <logic:notEmpty name="previousClaimNums">
			     <html:select property="previousClaimNO" name="frmAddClaimDetails" styleClass="selectBox selectBoxLarge" onchange="getProviderInvoiceNO();">
			     <html:option value="">Select from list</html:option>
			     <html:optionsCollection name="previousClaimNums" value="key" label="value"/>
			     </html:select>
			     </logic:notEmpty>			     			   
			    </td>
			    </tr>
			  <tr>
			  <td class="formLabel">Provider Invoice No.:<span class="mandatorySymbol">*</span></td> 
			   <td class="textLabel">
			   <html:text  name="frmAddClaimDetails"  property="providerInvoiceNO"  styleClass="textBoxDisabled textBoxLarge" readonly="true" maxlength="30"/>
			   </td>		 
			   <td class="formLabel">Requested Amount:<span class="mandatorySymbol">*</span></td>
			  <td class="textLabel">
			   <html:text  name="frmAddClaimDetails"  property="requestedAmount"  styleClass="textBox textBoxSmall" onkeyup="isNumeric(this);" maxlength="10"/>
			  </td>
			 </tr>
			 <tr>
			 <td class="formLabel" colspan="1">Re-Submission Remarks:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel" colspan="3">
					  	<html:textarea name="frmAddClaimDetails" property="resubmissionRemarks" rows="2" cols="80" styleId="overrideDesc"  />
					  </td>	
			 </tr>			
			  <tr>			  
			   <td align="center" colspan="4">
			   <%
             if(TTKCommon.isAuthorized(request,"Edit")) {
             %>
          
			  <logic:empty name="frmAddClaimDetails" property="claimSeqID">
			    <button type="button" name="Button3" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="addClaimDetails()">A<u>d</u>d</button>&nbsp;
			  </logic:empty>
			    <logic:notEmpty name="frmAddClaimDetails" property="claimSeqID">
			    <button type="button" name="Button3" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="addClaimDetails()">Edit</button>&nbsp;
			    </logic:notEmpty>
	      <%
            }
          %>
			    </td>
			 </tr>
			  </table>
			 </logic:equal>		
			  <br>
	     <ttk:ClaimBatchDeatils submissionType="<%=strSubmissionType%>"/>
		</fieldset>
				
		<fieldset>	         
			<legend>Batch Assign History </legend>
			<ttk:HtmlGrid name="tableData" />
				<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
					<%-- <ttk:HtmlGrid name="tableData" /> --%>
				<tr>
						<td width="73%" nowrap align="right">
    						<a href="#" onclick="javascript:onAssignUser()"><img title="Assign" alt="Assign" src="/ttk/images/assignbutton.png" border="white"></a>
   			 			</td>
   			 		</tr>
    			</table>
		</fieldset>
		
		
		
		
		
		
		
		
		
		
		</logic:notEmpty>
 <html:hidden  name="frmAddClaimDetails"  property="validateYN" styleId="validateYN"/>
 <html:hidden  name="frmAddClaimDetails"  property="overrideYN" styleId="overrideYN"/>
 <html:hidden  name="frmAddClaimDetails"  property="completedYN"/>
 <html:hidden  name="frmAddClaimDetails"  property="submissionType" value="<%=strSubmissionType%>"/>
 <html:hidden name="frmClaimBatchGeneral" property="batchSeqID"  />
 <html:hidden name="frmClaimBatchGeneral" property="batchReceiveDate"/>
 <html:hidden name="frmClaimBatchGeneral" property="receivedTime"/>
 <html:hidden name="frmClaimBatchGeneral" property="receiveDay"/>
 <html:hidden name="frmClaimBatchGeneral" property="claimType"/> 
 <html:hidden name="frmClaimBatchGeneral" property="providerName"/>
 <html:hidden name="frmClaimBatchGeneral" property="providerID"/>
 <html:hidden name="frmClaimBatchGeneral" property="providerLisenceNO"/>
 <html:hidden  name="frmAddClaimDetails"  property="claimSeqID" styleId="claimSeqID"/>
 <html:hidden name="frmClaimBatchGeneral" property="paymentTo"/>
 <html:hidden name="frmClaimBatchGeneral" property="partnerName"/>
 <html:hidden name="frmClaimBatchGeneral" property="fastTrackFlag"/>
  <html:hidden name="frmClaimBatchGeneral" property="fastTrackMsg"/>
 <INPUT TYPE="hidden" NAME="rownum">
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
<html:hidden name="frmClaimBatchGeneral" property="batchOverrideAllowYN" styleId="batchOverrideAllowYN"/>
</html:form>
</div>

 <script type="text/javascript">
 changenetwork();
	</script> 
</body>
</html>


