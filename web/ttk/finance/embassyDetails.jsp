<%
/**
 * @ (#) embassyDetails.jsp 12th March 2018
 * Project      : TTK HealthCare Services
 * File         : embassyDetails.jsp
 * Author       : Kishor kumar S H
 * Company      : Vidal Health TPA
 * Date Created : 12th March 2018
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList"%>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/finance/embassyDetails.js"></script>
<script>
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getParameter("focusID"))%>";
</script>
<%
    boolean viewmode=true;
	pageContext.setAttribute("destnationbank",Cache.getCacheObject("destnationbank"));
    pageContext.setAttribute("officeinfo",Cache.getCacheObject("officeInfo"));
    pageContext.setAttribute("accounttype",Cache.getCacheObject("accounttype"));
   	pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
   	pageContext.setAttribute("chequeCode",Cache.getCacheObject("chequeCode"));
	pageContext.setAttribute("OutsideQatarCountryList", Cache.getCacheObject("OutsideQatarCountryList"));
   	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
%>

<!-- S T A R T : Content/Form Area -->
<html:form action="/BankAcctGeneralActionTest.do" >


<!-- S T A R T : Page Title -->
    <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="57%">Bank Account Details <bean:write name="frmCustomerBankAcctGeneral" property="caption"/> <bean:write name="frmCustomerBankAcctGeneral" property="embassyID"/></td>
	      	<td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
	   
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
<!-- S T A R T : Form Fields -->
<fieldset>
        <legend>General</legend>
        <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
              <tr>
               	<td width="5%" class="formLabel">Embassy Name:</td>              
                <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="embassyName"/></td>
               	<td width="10%" class="formLabel">Embassy ID:</td>
                <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="embassyID"/></td>
              </tr>
              <tr>
               <td width="10%" class="formLabel">Account in the name of:</td>
               <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="climentName"/></td>
               <td width="10%" class="formLabel">Status type:</td>
               <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="empanelmentStatus"/></td>
              </tr>
        </table>
</fieldset>

<fieldset>
    <legend>Bank Details</legend>
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
    
    
       <tr>
      <td width="21%" class="formLabel">Bank Location: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankAccountQatarYN" styleClass="selectBox selectBoxMedium" onchange="onChangeQatarYN()" >
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>
                </td>
    </tr>
    
    
        <tr>
                       
                 <td width="21%" class="formLabel">Account Type: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankAccType" styleClass="selectBox selectBoxMedium"  >
                   <%--  <html:option value="">Select from list</html:option> --%>
                    <html:optionsCollection name="accounttype" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
                <logic:equal name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                  <td width="21%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
                  <td width="32%">
                <html:text property="bankname" styleId="state1" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                 </logic:equal>
                 <logic:notEqual name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                 <td width="21%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankname" styleId="state1"  styleClass="selectBox selectBoxLarge" onchange="onChangeBank('state1')" >
                    <html:option value="">Select from list</html:option>
                    <html:optionsCollection name="destnationbank" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
                 </logic:notEqual>
        </tr>
        
                 <tr>
                  <logic:equal name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                <td width="21%" class="formLabel">Bank City: <span class="mandatorySymbol">*</span></td>
                <td width="32%">
                <html:text property="bankState" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                 <td width="21%" class="formLabel">Bank Area: </td>
                <td width="32%">
                <html:text property="bankcity" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                </logic:equal>
                 
                  <logic:notEqual name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                   <td width="21%" class="formLabel">Bank City: <span class="mandatorySymbol">*</span> </td>
                 <td>
                    <html:select property="bankState" styleId="state2" styleClass="selectBox selectBoxMedium" onchange="onChangeState('state2')" >
                    <html:option value="">Select from list</html:option>
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
                <td width="21%" class="formLabel">Bank Area: <span class="mandatorySymbol">*</span> </td>
                 <td>
                    <html:select property="bankcity" styleId="state3" styleClass="selectBox selectBoxMedium" onchange="onChangeCity('state3')" >
                     <html:option value="">Select from list</html:option> 
                    <html:optionsCollection name="alDistList" label="cacheDesc" value="cacheId" />
                  <%--  <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
                    </html:select>
                </td>
                </logic:notEqual>
                </tr>
                 
                 <tr>
                  <logic:equal name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                <td width="21%" class="formLabel">Bank Branch:</td>
                <td width="32%">
                <html:text property="bankBranchText" styleId="state4" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                 <td width="21%" class="formLabel">Swift Code: <span class="mandatorySymbol">*</span></td>
                <td width="32%">
                <html:text property="ifsc"  styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" onblur="swiftCodeValidation();"/>
                </td>
                </logic:equal>
                 <logic:notEqual name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                  <td width="21%" class="formLabel">Bank Branch:  <span class="mandatorySymbol" >*</span> </td>
                 <td>
                    <html:select property="bankBranch" styleId="state4" styleClass="selectBox selectBoxMedium"><!--  onchange="onChangeBranch('state4')"  -->
                    <%--   <html:option value="">Select from list</html:option> --%>
                   <html:optionsCollection name="alBranchList" label="cacheDesc" value="cacheId" /> 
                 <%--  <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
                    </html:select>
                </td>
                <td width="21%" class="formLabel">Swift Code: <span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="ifsc" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement);"/>
                 </td>
                 </logic:notEqual>  
                 </tr>
                 <tr>
                                       
                 <td width="21%" class="formLabel">Bank Code: </td>
                 <td width="32%">
                  <html:text property="neft" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                 </td>
                 <td width="21%" class="formLabel">IBAN Number/Account Number:<span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="micr" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                 </td>  
                 
                 </tr>         
                 
                 
                 <tr>
	                 <td width="21%" class="formLabel">Bank Phone No.: </td>
	                 <td width="32%">
	                  <html:text property="bankPhoneno" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
	                 </td>
	                 
	                 
	     <td class="formLabel">Country:<span class="mandatorySymbol">*</span> </td>
        <td>
         <logic:equal name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
         	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" onchange="onCountryCode(this);">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="OutsideQatarCountryList" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
         </logic:equal>
         <logic:notEqual name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N" >
        	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" onchange="onCountryCode(this);">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
        </logic:notEqual>
        </td>   
                </tr>
                               
    </table>
</fieldset>
     <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="100%" align="center">
                <%
          if(viewmode==false)
          {
              %>
                <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onSave()"><u>S</u>ave</button>&nbsp;
                <button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button>&nbsp;
              <%
          }//end of if(TTKCommon.isAuthorized(request,"Edit"))
          %>
        	  <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseEmbassy();"><u>C</u>lose</button>
            </td>
        </tr>
    </table>  
<!-- E N D : Form Fields -->
</div>
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
<html:hidden property="embassySeqID" name="frmCustomerBankAcctGeneral"/>
<html:hidden property="switchType" name="frmCustomerBankAcctGeneral"/>
<INPUT TYPE="hidden" NAME="tab" VALUE="">
<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
<INPUT TYPE="hidden" NAME="sublink" VALUE="">
<input type="hidden" name="focusID" value="">
<input type="hidden" name="reforward" value="">
<html:hidden property="towDigitCountryCode" />

</html:form>