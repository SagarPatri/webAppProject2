<%
/**
 * @ (#) financeAccountReview.jsp 28th Sep 2005
 * Project      : TTK HealthCare Services
 * File         : financeAccountReview.jsp
 * Author       : Kishor 
 * Company      : Rcs
 * Date Created : 13th May 2015
 *
 * @author       :
 * Modified by   : kishor kumar S H
 * Modified date : 
 * Reason        :
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
	pageContext.setAttribute("stateCode", Cache.getCacheObject("stateCode"));
	pageContext.setAttribute("cityCode", Cache.getCacheObject("cityCode"));
	pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
	pageContext.setAttribute("chequeCode",Cache.getCacheObject("chequeCode"));
	pageContext.setAttribute("accounttype",Cache.getCacheObject("accounttype"));
	
%>


<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>

<!-- <script language="javascript" src="/ttk/scripts/empanelment/accounts.js"></script>
 -->
 
<script language="javascript" src="/ttk/scripts/finance/financeAccountValidate.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript">
</script>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CustomerBankDetailsAccountList.do" >
<!-- S T A R T : Page Title -->
        <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td width="51%">Account Details - <bean:write name="frmCustomerBankAccountList" property="caption"/></td>
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
	
	<fieldset><legend>Provider Bank Account Details</legend>
	<!-- S T A R T : Form Fields -->
	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
	
	
   <tr>
              <td width="21%" class="formLabel">Bank Location: <span class="mandatorySymbol">*</span></td>
                 <td>
          <html:select property="bankAccountQatarYN" styleClass="selectBox selectBoxMedium"  readonly="true"  disabled="true"  onchange="onChangeQatarYN()" >
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>        
            </td>
         </tr>
	
	
	  <tr>
	    <td width="20%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
	    <td width="33%"><html:text property="bankname" styleClass="textBox textBoxLarge" maxlength="250"   readonly="true"  disabled="true"  onkeyup="ConvertToUpperCase(event.srcElement);" style="background-color: #EEEEEE;" />
	    </td>
	    <td width="17%"class="formLabel">&nbsp;</td>
	    <td width="30%">&nbsp;</td>
	  </tr>
	  <tr>
	    <td class="formLabel">Account No.:</td>
	    <td><html:text property="bankAccno" styleClass="textBox textBoxMedium" maxlength="60" style="background-color: #EEEEEE;" readonly="true"  disabled="true"  />
	 	</td>
	    <td class="formLabel">Account Name:</td>
	    <td><html:text property="bankAccName" styleClass="textBox textBoxLarge" maxlength="250" style="background-color: #EEEEEE;" readonly="true"  disabled="true"  />
		</td>
	  </tr>
	  
	  <!-- adding IBAN and SWIFT code for intX -->
	  
	 <tr>
	    <td class="formLabel">Swift Code :<span class="mandatorySymbol">*</span></td>
	    <td><html:text property="iBanNo" styleClass="textBox textBoxMedium" maxlength="60" style="background-color: #EEEEEE;" readonly="true"  disabled="true"  />
	 	</td>
	    <td class="formLabel">IBAN No.:<span class="mandatorySymbol">*</span></td>
	    <td><html:text property="swiftCode" styleClass="textBox textBoxLarge" maxlength="250" style="background-color: #EEEEEE;" readonly="true"  disabled="true"  />
		</td>
	  </tr>
	  <!-- E N D -->
	  
	  <tr>
                <td class="formLabel"> Start Date<span class="mandatorySymbol">*</span> : </td>
                 <td>
        				<html:text property="startDate" styleClass="textBox textDate" maxlength="10" readonly="true"  disabled="true"    />
        				
        					<!-- <a name="CalendarObjectStartDate" id="CalendarObjectStartDate" href="#" onClick="javascript:show_calendar('CalendarObjectStartDate','frmCustomerBankAccountList.startDate',document.frmCustomerBankAccountList.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a> -->
        				
        			</td>
        			<td class="formLabel"> End Date<!-- <span class="mandatorySymbol">*</span> --> : </td>
                 <td>
        				<html:text property="endDate" styleClass="textBox textDate" maxlength="10"   readonly="true"  disabled="true"/>
        				
        					<!-- <a name="CalendarObjectEndDate" id="CalendarObjectEndDate" href="#" onClick="javascript:show_calendar('CalendarObjectEndDate','frmCustomerBankAccountList.endDate',document.frmCustomerBankAccountList.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a> -->
        				
        			</td>
      </tr>
                 
                 
	  <tr>
	      <td colspan="4" height="5"></td>
	      </tr>
	      <tr>
	        <td colspan="4" class="formLabelBold">Bank Branch Details </td>
	      </tr>
		  <tr>
	        <td colspan="4" height="2"></td>
	      </tr>
	  <tr>
	    <td class="formLabel">Branch Name: <span class="mandatorySymbol">*</span></td>
	    <td><html:text property="bankBranch" styleClass="textBox textBoxLarge" onkeypress='ConvertToUpperCase(event.srcElement);' maxlength="250" style="background-color: #EEEEEE;"  readonly="true"  disabled="true" /></td>
	    
	    
	        <td class="formLabel">Account Type: <span class="mandatorySymbol">*</span></td>
        <td>
                    <html:select property="bankAccType" styleClass="selectBox selectBoxMedium" readonly="true" disabled="true" >
                    <html:optionsCollection name="accounttype" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
	    
	  </tr>
	  <tr>
        <td width="20%" class="formLabel">Address 1: </td>
        <td width="33%"><html:text property="address1" styleClass="textBox textBoxMedium" maxlength="250" style="background-color: #EEEEEE;"  readonly="true"  disabled="true" /></td>
        <td width="17%" class="formLabel">Address 2:</td>
        <td width="30%"><html:text property="address2" styleClass="textBox textBoxMedium" maxlength="250"  readonly="true"  disabled="true" /></td>
      </tr>
	  <tr>
	   <td class="formLabel">Address 3: </td>
 	    <td><html:text property="address3" styleClass="textBox textBoxMedium" maxlength="250" readonly="true"  disabled="true" /></td>
	    <td class="formLabel">City: </td>
	    <td>
	     <logic:equal name="frmCustomerBankAccountList" property="bankAccountQatarYN" value="N">
	     <html:text property="bankState" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
	     </logic:equal>
	      <logic:notEqual name="frmCustomerBankAccountList" property="bankAccountQatarYN" value="N">
	      <html:select property ="bankState" styleId="state1"  styleClass="selectBox selectBoxMedium" readonly="true"  disabled="true" onchange="onChangeState('state1')">
                 <html:option value="">Select from list</html:option>
                 <html:options collection="stateCode" property="cacheId" labelProperty="cacheDesc"/>
    	   	</html:select>
	    </logic:notEqual>
	   </td>
	  </tr>
	  <tr>
	   <td class="formLabel">Area: </td>
        <td>
        <logic:notEqual name="frmCustomerBankAccountList" property="bankAccountQatarYN" value="N">
         	 <html:select property ="bankcity" styleClass="selectBox selectBoxMedium" readonly="true"  disabled="true"  >
                 <html:option value="">Select from list</html:option>
                <html:optionsCollection property="alCityList" label="cacheDesc" value="cacheId"/> 
                <%--  <html:options collection="stateCode" property="cacheId" labelProperty="cacheDesc"/> --%>
          	</html:select> 
          	</logic:notEqual>
          	<logic:equal name="frmCustomerBankAccountList" property="bankAccountQatarYN" value="N">
          	 <html:text property="bankcity" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
        </logic:equal>
        </td>
        <td class="formLabel">PO Box:</td>
        <td><html:text property="pincode" styleClass="textBox textBoxSmall" maxlength="10"  readonly="true"  disabled="true"/></td>
      </tr>
      <tr>
        <td class="formLabel">Country: </td>
        <td colspan="3">
       	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" readonly="true"  disabled="true">
        		<html:option value="">--Select from list--</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select> 
        </td>
      </tr>
      <tr>
        <td colspan="4" height="5"></td>
      </tr>
      <tr>
        <td colspan="4" class="formLabelBold">Provider Management Details </td>
        </tr>
	  <tr>
        <td colspan="4" height="2"></td>
      </tr>
	  <tr>
        <td class="formLabel">Management Name:</td>
        <td><html:text property="managementName" styleClass="textBox textBoxLarge" maxlength="250" onkeyup="ConvertToUpperCase(event.srcElement);"  readonly="true"  disabled="true"/></td>
        <td class="formLabel">Issue Cheques To:</td>
           <td>
           <html:select property="issueChqToHosp" styleClass="selectBox selectBoxMedium" readonly="true"  disabled="true">
           		<html:option value="">--Select from list--</html:option>
                 <html:options collection="chequeCode" property="cacheId" labelProperty="cacheDesc"/>
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
	<legend>Empanelment-Finance Documents</legend>
	<ttk:EmpanelmentFinanceDocs/>
	</fieldset>
	
	<!-- E N D : Form Fields -->
	
    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    	<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserSubmit();"><u>S</u>ave</button>&nbsp;
			<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseReview();"><u>C</u>lose</button>
	  </tr>
	</table>

<!-- E N D : Buttons -->
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<html:hidden property="hiddenReview" />
<html:hidden property="switchHospOrPtr"/>

<script>

if(document.forms[1].hiddenReview.value=="Y")
    document.forms[1].reviewedYN.checked=true;
else
	document.forms[1].reviewedYN.checked=false;
    
	</script>
</html:form>
