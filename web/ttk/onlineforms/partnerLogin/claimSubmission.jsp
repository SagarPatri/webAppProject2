<!-- mm aa -->
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page import="java.util.ArrayList,com.ttk.common.TTKCommon"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon" %>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,org.apache.struts.action.DynaActionForm"%>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript"  src="/ttk/scripts/partner/partnerClaimSubmission.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
<script type="text/javascript" src="/ttk/scripts/preauth/preauthgeneral-async.js"></script>
<style>
.formContainer123 {
	margin-top: 0px;
	padding: 3px;
	width: 98%;
	text-align: left;
}

.ptnr_note_class {
	border: 1px;
	border-color: black;
	border-style: groove;
	width: 50%;
}
</style>

<%
	pageContext.setAttribute("alCountryCode",
			Cache.getCacheObject("countryCode"));
	pageContext.setAttribute("benifitType",
			Cache.getCacheObject("main4benifitTypes"));
%>

<html:form action="/OnlinePartnerClaimsSubmission.do" method="post" enctype="multipart/form-data">

<div class="contentArea" id="contentArea">
<html:errors/>
	<h1 class="sub_heading">Add Claim Details</h1>
	<br><br>
	<table align="center" class="formContainer123"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="formLabel" style="width:20%"><strong>Al Koot ID: </strong><span class="mandatorySymbol">*</span></td>
			<td>
			          <html:text property="enrollId" styleId="enrollId" name="frmOnlineClaimSubmission" 
			          value="Al Koot Id / Qatar Id" 
			          onblur="if (this.value == '') {this.value = 'Al Koot Id / Qatar Id';}"
					  onfocus="if (this.value == 'Al Koot Id / Qatar Id') {this.value = '';}"
			          onmouseover="if (this.value == 'Al Koot Id / Qatar Id') {this.value = '';}" 
			          onmouseout="if (this.value == '') {this.value = 'Al Koot Id / Qatar Id';}"
			          onkeydown="if (this.value == 'Al Koot Id / Qatar Id') {this.value = '';}"
			          onkeyup="isNumber(this,'Al Koot ID');"
			          styleClass="textBoxWeblogin textBoxMediumWeblogin"/>
			</td> 
			
		</tr>
		
		<tr>
			<td class="formLabel" style="width:20%">Invoice No:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:text name="frmOnlineClaimSubmission" property="invoiceNo" styleId="invoiceNo" styleClass="textBoxWeblogin textBoxMediumWeblogin"/>
			</td> 
		</tr>
		<tr>
			<td class="formLabel" style="width:20%">Date of Treatment:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:text name="frmOnlineClaimSubmission" property="treatmentDate" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="10" readonly="true"/>
       					 <a name="CalendarObjectTreatmentDate" id="CalendarObjectTreatmentDate" href="#" onClick="javascript:show_calendar('CalendarObjectTreatmentDate','frmOnlineClaimSubmission.elements[\'treatmentDate\']',document.frmOnlineClaimSubmission.elements['treatmentDate'].value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
       						<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
       					</a>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
       			<td style="width: 7%">
       			Benefit Type:<span class="mandatorySymbol">*</span>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
       			<td>
       			<html:select property ="benifitTypeID" styleId="benifitTypeID" styleClass="textBoxWeblogin textBoxMediumWeblogin" style="margin-left: 4px;">
                 			<html:option value="">Select from list</html:option>
                 			<html:optionsCollection	name="benifitType" label="cacheDesc" value="cacheId"/>
                 			
			        </html:select>
			</td>
			
			
		</tr>
		<tr>
			<td class="formLabel">Requested Amount:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:text name="frmOnlineClaimSubmission" property="requestedAmt" styleId="requestedAmt" styleClass="textBoxWeblogin textBoxMediumWeblogin" onkeyup="isNumberForRequestAmount(this,'Requested Amount');"/>
			</td>
			
			<td class="formLabel">Currency<span class="mandatorySymbol">*</span></td>
			        
			        <td>
			          <html:text property="currency" styleId="requestedAmountcurrencyType"  value="QAR" styleClass="textBox textBoxMedium textBoxDisabled"  readonly="false" style="margin-left: 4px;"/><a href="#" onclick="openRadioList('requestedAmountcurrencyType','CURRENCY_GROUP','option');clearConversionRate();">
		        				  <img src="/ttk/images/search_edit.gif" width="18" height="18" title="Select Currency" alt="Select Currency" border="0" align="bottom"> </a>
			        </td> 
		</tr>
		<tr>
			<td class="formLabel" style="width:20%">Country:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:select property ="country" styleId="providerCountry" styleClass="textBoxWeblogin textBoxMediumWeblogin" onchange="getcurrencyCode();">
                 			<html:option value="">Select from list</html:option>
                 			<html:optionsCollection	name="alCountryCode" label="cacheDesc" value="cacheId"/>
			        </html:select>
			</td> 
		</tr>
		<tr>
			<td class="formLabel" style="width:20%">Provider Name:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:text name="frmOnlineClaimSubmission" property="providerName" styleId="providerName" styleClass="textBoxWeblogin textBoxMediumWeblogin"/>
			</td> 
		</tr>
		<tr>
			<td class="formLabel" style="width:20%">Presenting Complaints:</td>
			<td>
				<html:textarea name="frmOnlineClaimSubmission" styleId="presentingComplaintsId" property="presentingComplaints" cols="35" rows="2" />
			</td> 
		</tr>
		<tr>
			<td colspan="3"><div id="icdResult1">
			<div id="icdResult2" style="margin-left: 400px;"></div>
			 </div></td>
			</tr>
		<tr>
		<td class="formLabel"> Diagnosis Code / ICD :</td>
		<td><html:text name="frmOnlineClaimSubmission" property="icdCode" styleId="icdCode" styleClass="textBox textBoxMedium" maxlength="20"
														onblur="getIcdCodeDetails();" /></td>
														
		<td><a href="#" accesskey="g" onClick="javascript:selectDiagnosisCode()" class="search">
				<img src="/ttk/images/EditIcon.gif" title="Select Diagnosis Code" alt="Select Diagnosis Code" width="16" height="16"
					 border="0" align="absmiddle" style="margin-left: -142px;">&nbsp; </a></td>
					 
	    <td><html:textarea rows="1" cols="80" name="frmOnlineClaimSubmission" styleId="diagnosisDescription" property="diagnosisDescription" readonly="true"/></td>
		</tr>
		
		<tr>
			    	<td height="20" class="formLabel">Upload Invoice & Supporting Docs :</td>
	    			<td class="textLabelBold" ><html:file property="file1" styleId="file1"/> </td>
	    			<%-- <td colspan="2"><html:link href="javascript:onViewDocument('file1');"><img src="/ttk/images/ModifiedIcon.gif" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
			    	 <bean:write name="frmOnlineClaimSubmission" property="attachmentname1"/></td> --%>
			    </tr>
			    
			    <tr>
			    	<td></td>
	    			<td class="textLabelBold"><html:file property="file2" styleId="file2"/> </td>
	    			<%-- <td colspan="2" ><html:link href="javascript:onViewDocument('file2');"><img src="/ttk/images/ModifiedIcon.gif" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
			    	<bean:write name="frmOnlineClaimSubmission" property="attachmentname2"/></td> --%>
			    </tr>
			    <br/>
			    <tr>
			  		 <td></td>
	    			<td class="textLabelBold"><html:file property="file3" styleId="file3" style="margin-top: 5px;"/> </td>
	    			<%-- <td colspan="2" ><html:link href="javascript:onViewDocument('file3');"><img src="/ttk/images/ModifiedIcon.gif" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
	    			 <bean:write name="frmOnlineClaimSubmission" property="attachmentname3"/></td> --%>
			    </tr>
			    <br/>
			    <tr>
			    	<td></td>
	    			<td class="textLabelBold"><html:file property="file4" styleId="file4" style="margin-top: 5px;"/> </td>
			    	<%-- <td colspan="2" ><html:link href="javascript:onViewDocument('file4');"><img src="/ttk/images/ModifiedIcon.gif" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
			    	
			    	 <bean:write name="frmOnlineClaimSubmission" property="attachmentname4"/></td> --%>
			    	
			    	<td></td>
			    </tr>
			   <br/> 
			    <tr>
			    	<td></td>
	    			<td class="textLabelBold"><html:file property="file5" styleId="file5" style="margin-top: 5px;"/> </td>
	    		     <%-- <td colspan="2" >	<html:link href="javascript:onViewDocument('file5');"><img src="/ttk/images/ModifiedIcon.gif" alt="Pricing Source Documents" width="16" height="16" border="0" align="absmiddle"></html:link>
	    			 <bean:write name="frmOnlineClaimSubmission" property="attachmentname5"/></td> --%>
			    	
			    	<td></td>
			    </tr>
		       <br/>
		
		
		<%-- <tr>
			<td class="formLabel" style="width:20%">benifit Type:<span class="mandatorySymbol">*</span></td>
			<td>
				<html:select property ="benifitTypeID" styleId="benifitTypeID" styleClass="textBoxWeblogin textBoxMediumWeblogin">
                 			<html:option value="">Select from list</html:option>
                 			<html:optionsCollection	name="benifitType" label="cacheDesc" value="cacheId"/>
                 			
			        </html:select>
			</td> 
		</tr> --%>
	</table>
	<div class="ptnr_note_class">
    <p align='left'><u>NOTE:</u></p>
  
  <p>
    1. All the Fields with <font color="red">*</font> are mandatory.
  </p>
  
    <p>2. Although Presenting Complaints/Diagnosis code fields do not have <font color="red">*</font> mark, Please make sure to provide <br>atleast one of these fields,else invoice/claim cannot be accepted.</br>
    </p>
  
   
    <p>3. Please Provide accurate information for faster claim settlement.</p>
  
	</div>
	<table>
	<tr>
	    <td>
			<button type="button" name="Button" accesskey="s" class="olbtnSmall" onClick="onSubmitForm();" style="margin-left: 500px;"><u>S</u>ubmit</button>
 			<button type="button" name="Button" accesskey="b" class="olbtnSmall" onClick="onCloseCLaim();"><u>C</u>lose</button>
 			
		 </td>	
		</tr>
	</table>
	<html:hidden name="frmOnlineClaimSubmission" property="icdCodeSeqId"
											styleId="icdCodeSeqId" /> 
    <html:hidden name="frmOnlineClaimSubmission" property="diagSeqId"
											styleId="diagSeqId" />
											
	<INPUT TYPE="hidden" NAME="leftlink" value="">										
	<INPUT TYPE="hidden" NAME="sublink" value="">
    <INPUT TYPE="hidden" NAME="tab" value="">
    <INPUT TYPE="hidden" NAME="child" value="">										
	<INPUT TYPE="hidden" NAME="mode" id="mode" VALUE=""> 	
	<INPUT TYPE="hidden" NAME="reforward" id="reforward" VALUE="">
	<input type="hidden" name="seqId" id="seqId" value="">	
	<input type="hidden" name="authType" id="authType" value="">								
</div>
</html:form> 