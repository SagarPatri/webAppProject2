<%
/**
 * @ (#) financeMemberAccountRevisedValidation.jsp 18th Feb 2019
 * Project      : TTK HealthCare Services
 * File         : financeMemberAccountRevisedValidation.jsp
 * Author       : Deepthi 
 * Company      : Rcs
 * Date Created : 18th FEB 2019

 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList" %>
<%
	boolean viewmode=true;
	boolean viewmodePO=true;
	
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))	
	 pageContext.setAttribute("officeinfo",Cache.getCacheObject("officeInfo"));
	 pageContext.setAttribute("accounttype",Cache.getCacheObject("accounttype"));
	 pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
%>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>

<!-- <script language="javascript" src="/ttk/scripts/empanelment/accounts.js"></script>
 -->
 
<script language="javascript" src="/ttk/scripts/finance/financeMemberAccountReview.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript">
</script>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CustomerBankDetailsAccountList.do" >
<!-- S T A R T : Page Title -->
        <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td width="51%">Member Account Details - <bean:write name="frmCustomerBankAccountList" property="caption"/></td>
	        <td align="right" class="webBoard">
				<a href="#" onClick="javascript:onAccountHistory()"><img src="ttk/images/HistoryIcon.gif" title="View History" alt="View History" width="16" height="16" border="0" align="absmiddle" class="icons"></a>&nbsp;&nbsp;<img src="ttk/images/IconSeparator.gif" width="1" height="15" align="absmiddle" class="icons">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %>
		    </td>
	      </tr>
	    </table>

	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<html:errors/>
	<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
	<!-- E N D : Success Box -->
	<!-- projectX Starts-->
	
	<fieldset><legend>Member Bank Account Details</legend>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	 <tr>
                <td width="5%" class="formLabel">Policy No.:</td>              
                 <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAccountList" property="policyNumber"/></td>
	
	           <td width="10%" class="formLabel">Enrollment Type:</td>
                <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAccountList" property="policyType"/></td>
	</tr>
	
	<tr>
	 <td width="10%" class="formLabel">Enrollment Number:</td>
     <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAccountList" property="enrollNmbr"/></td>
	
	
	  <td class="formLabel">Alkoot Branch: </td>
                <td>
                    <html:select property="officeSeqID" styleClass="selectBox selectBoxMedium" styleId="switchType" disabled="true">
                    <html:option value="">Select from list</html:option>
                    <html:optionsCollection name="officeinfo" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
	</tr>
	
	</table>
	</fieldset>
	<fieldset><legend>Bank Details</legend>
	<!-- S T A R T : Form Fields -->
	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
	
	
	<tr>
	  <td width="21%" class="formLabel">Bank Account: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankAccountQatarYN" styleClass="selectBox selectBoxMedium" onchange="onChangeQatarYN()" readonly="true" disabled="true" >
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>
                </td>
	</tr>
	
	<tr>
	 <td width="21%" class="formLabel">Bank Account Holder Name: </td>
                 <td width="32%">
                  <html:text property="insureName" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;"  maxlength="60" readonly="true"/>
                 </td>
	
	
	<td width="21%" class="formLabel">Bank Account No.: <span class="mandatorySymbol">*</span></td>
              <td width="32%">
              <html:text property="bankAccno" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                </td>
	</tr>
	
	
	<tr>
	 <td width="21%" class="formLabel">Account Type: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankAccType" styleClass="selectBox selectBoxMedium" readonly="true" disabled="true" >
                    <html:option value="">Select from list</html:option>
                    <html:optionsCollection name="accounttype" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
                 
                <td width="21%" class="formLabel">Destination Bank: <span class="mandatorySymbol">*</span></td>
                <td width="32%">
                <html:text property="bankname" styleClass="textBox textBoxMedium"  styleId="state1" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                </td>
               
              
	</tr>
	
	
	<tr>
                <td width="21%" class="formLabel">Destination Bank City: <span class="mandatorySymbol">*</span></td>
                <td width="32%">
                <html:text property="bankState" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                </td>
                 <td width="21%" class="formLabel">Destination Bank Area:</td>
                <td width="32%">
                <html:text property="bankcity" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                </td>
	</tr>
	
	<tr>
                <td width="21%" class="formLabel">Destination Bank Branch:</td>
                <td width="32%">
                <html:text property="bankBranchText" styleId="state4" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                </td>
                 <td width="21%" class="formLabel">Swift Code: <span class="mandatorySymbol">*</span></td>
                <td width="32%">
                <html:text property="ifsc"  styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                </td>
	</tr>
	<tr>
                 <td width="21%" class="formLabel">Bank Code: </td>
                 <td width="32%">
                  <html:text property="neft" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                 </td>
                 <td width="21%" class="formLabel">IBAN Number:<span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="micr" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                 </td>  
                 
                 </tr>
                 <tr>
                 <td width="21%" class="formLabel">Bank Phone No.: </td>
                 <td width="32%">
                  <html:text property="bankPhoneno" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                 </td>
                 <logic:notEmpty name="frmCustomerBankAccountList" property="policyGroupSeqID">
                 <td width="21%" class="formLabel">EMAIL: </td>
                 <td width="32%">
                  <html:text property="emailID" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);"  maxlength="60" readonly="true" disabled="true"/>
                 </td>
                </logic:notEmpty>
                </tr> 
                   <tr>
                 <logic:empty name="frmCustomerBankAccountList" property="hospitalSeqId">
                 <td width="20%" class="formLabel">Address :</td>
                 <td width="33%"><html:text property="address1" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" maxlength="250" value="NA" readonly="true" disabled="true"/></td>
                </logic:empty>
                
                 <td class="formLabel">Country: </td>
            <td>
        	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" readonly="true" disabled="true">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
        </td>
            </tr>
                 
     <!-- intX Finance Review-->
     <tr>
        <td colspan="4" class="formLabelBold">Finance Reviewed Details </td>
     </tr>
     <tr>
        <td colspan="4" height="2"></td>
      </tr>
	 
     
     <tr>
		<td width="21%" class="formLabel">Finance Reviewed Yes/No:</span></td>
		 <td width="32%">
		<html:checkbox property="reviewedYN" styleId="reviewYN" /><!-- onclick="checkProviderGroup()" -->
		</td>
	 </tr>
         
</table>
</fieldset>
	
	<fieldset>
	<legend>Member-Finance Documents</legend>
	<ttk:EmpanelmentFinanceDocs/>
	</fieldset>
	
	<!-- E N D : Form Fields -->
	
    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    	<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserSubmit();"><u>S</u>ave</button>&nbsp;
			<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseRevisedReview();"><u>C</u>lose</button>
	  </tr>
	</table>

<!-- E N D : Buttons -->
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<html:hidden property="hiddenReview" />
<html:hidden property="switchHospOrPtr"/>
<html:hidden property="bankSeqId"/>
<html:hidden property="bankSeqId"/>
<html:hidden property="Flag" value="Rnw"/>
<script>

if(document.forms[1].hiddenReview.value=="Y")
    document.forms[1].reviewedYN.checked=true;
else
	document.forms[1].reviewedYN.checked=false;
    
	</script>
</html:form>
