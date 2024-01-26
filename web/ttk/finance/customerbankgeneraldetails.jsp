<%
/**
 * @ (#) preauthgeneral.jsp May 10, 2006
 * Project      : TTK HealthCare Services
 * File         : preauthgeneral.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : May 10, 2006
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
<script language="javascript" src="/ttk/scripts/finance/customerbankgeneraldetails.js"></script>
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
            <logic:notEmpty name="frmCustomerBankAcctGeneral" property="policyGroupSeqID">
            <td width="57%">Bank Account Details <bean:write name="frmCustomerBankAcctGeneral" property="caption"/> <bean:write name="frmCustomerBankAcctGeneral" property="enrollNmbr"/></td>
            </logic:notEmpty>
             <logic:notEmpty name="frmCustomerBankAcctGeneral" property="insurenceSeqId">
            <td width="57%">Bank Account Details <bean:write name="frmCustomerBankAcctGeneral" property="caption"/> <bean:write name="frmCustomerBankAcctGeneral" property="policyNumber"/></td>
            </logic:notEmpty>
             <logic:notEmpty name="frmCustomerBankAcctGeneral" property="hospitalSeqId">
            <td width="57%">Bank Account Details <bean:write name="frmCustomerBankAcctGeneral" property="caption"/> <bean:write name="frmCustomerBankAcctGeneral" property="hospitalEmnalNumber"/></td>
            </logic:notEmpty>
           
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
              <logic:empty name="frmCustomerBankAcctGeneral" property="hospitalSeqId">
              <tr>
                <td width="5%" class="formLabel">Policy No.:</td>              
                 <logic:notEmpty name="frmCustomerBankAcctGeneral" property="policySeqID">
                 <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="policyNumber"/></td>
                 </logic:notEmpty>
               <td width="10%" class="formLabel">Enrollment Type:</td>
                <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="policyType"/></td>
                 </tr>
                 <tr>
                <logic:notEmpty name="frmCustomerBankAcctGeneral" property="policyGroupSeqID">
               <td width="10%" class="formLabel">Enrollment Number:</td>
               <td width="10%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="enrollNmbr"/></td>
               </logic:notEmpty>
                </logic:empty>
               
                <td class="formLabel">Alkoot Branch: </td>
                <td>
                    <html:select property="officeSeqID" styleClass="selectBox selectBoxMedium" styleId="switchType" disabled="true">
                    <html:option value="">Select from list</html:option>
                    <html:optionsCollection name="officeinfo" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
               <logic:notEmpty name="frmCustomerBankAcctGeneral" property="hospitalSeqId">
                <tr>
                <td width="21%" class="formLabel">Empanelment No.: </td>
                <td width="33%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="hospitalEmnalNumber"/></td>
                <td width="21%" class="formLabel">Provider Name: </span></td>
                <td width="33%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="hospitalName"/></td>    
              </tr>
              <tr>
               <td width="21%" class="formLabel">Account in name of: </span></td>
                <td width="32%">
               <html:text property="hospitalAccountINameOf" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                <td width="21%" class="formLabel">Empanelment Status Type: </span></td>
               <td width="33%" class="textLabelBold"><bean:write name="frmCustomerBankAcctGeneral" property="empanalDescription"/></td>
                </tr>
                </logic:notEmpty>
                <!-- END EFT -->
               
              </tr>
              <tr>
		<td class="formLabel">Stop Cashless:</td>	 				
	  				<td>	  				
	 					<html:checkbox name="frmCustomerBankAcctGeneral" property="stopPreAuthsYN" value="Y" styleId="stopPreAuthsYN" onclick="showAndHideDatePreauth();"/>	 
	 					<logic:equal value="Y" name="frmCustomerBankAcctGeneral" property="stopPreAuthsYN">
	 				<span id="stopPreauthDateId">
	 				 <html:text
                        property="stopPreauthDate" name="frmCustomerBankAcctGeneral"
                        styleClass="textBox textDate" maxlength="10" styleId="stopPreauthId"/><a
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopPreauthDate',document.forms[1].stopPreauthDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
				</logic:equal> 
	 				 <logic:notEqual value="Y" name="frmCustomerBankAcctGeneral" property="stopPreAuthsYN">
	 				<span id="stopPreauthDateId" style="display: none">
	 				<!-- <br> --> <html:text
                        property="stopPreauthDate" name="frmCustomerBankAcctGeneral"
                        styleClass="textBox textDate" maxlength="10" styleId="stopPreauthId"/><a
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopPreauthDate',document.forms[1].stopPreauthDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
				</logic:notEqual>					
	 				</td>
					
		<td class="formLabel">Stop Claims:</td>	 				
	  				<td>	  				
	 					<html:checkbox name="frmCustomerBankAcctGeneral" property="stopClaimsYN" value="Y" styleId="stopClaimsYN" onclick="showAndHideDateClaims();"/>	 		
	 					<logic:equal value="Y" name="frmCustomerBankAcctGeneral" property="stopClaimsYN">
	 				<span  id="stopClaimsDateId"><html:text
                        property="stopClaimsDate" name="frmCustomerBankAcctGeneral"
                        styleClass="textBox textDate" maxlength="10" styleId="stopclmsid"/><A
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopClaimsDate',document.forms[1].stopClaimsDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
				</logic:equal> 
	 				 <logic:notEqual value="Y" name="frmCustomerBankAcctGeneral" property="stopClaimsYN">
	 				<span id="stopClaimsDateId" style="display: none"><html:text
                        property="stopClaimsDate" name="frmCustomerBankAcctGeneral"
                        styleClass="textBox textDate" maxlength="10" styleId="stopclmsid"/><A
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopClaimsDate',document.forms[1].stopClaimsDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
		</logic:notEqual>			
	 				</td>
	 				
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
                       
            <logic:notEmpty name="frmCustomerBankAcctGeneral" property="policyGroupSeqID">
                 <td width="21%" class="formLabel">Bank Account Holder Name: </td>
                 <td width="32%">
                  <html:text property="insureName" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;"  maxlength="60" readonly="true"/>
                 </td>
          </logic:notEmpty>
                
			<logic:notEmpty name="frmCustomerBankAcctGeneral" property="bankname">
                <td width="21%" class="formLabel">Bank Name:</td>
              <td width="32%">
              <bean:write name="frmCustomerBankAcctGeneral" property="bankname" />
                </td>
            </logic:notEmpty>    
                
                <td width="21%" class="formLabel">Bank Account No.: </td>
              <td width="32%">
              <html:text property="bankAccno" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true" disabled="true"/>
                </td>
         </tr>
                <tr>
                 <td width="21%" class="formLabel">Account Type: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankAccType" styleClass="selectBox selectBoxMedium"  >
                    <%-- <html:option value="">Select from list</html:option> --%>
                    <html:optionsCollection name="accounttype" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
                  <logic:equal name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                <td width="21%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
                <td width="32%">
                <html:text property="bankname" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
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
                 <td width="21%" class="formLabel">Bank Area:</td>
                <td width="32%">
                <html:text property="bankcity" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                </logic:equal>
                 <logic:notEqual name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                   <td width="21%" class="formLabel">Bank City:  <span class="mandatorySymbol">*</span> </td>
                 <td>
                    <html:select property="bankState" styleId="state2" styleClass="selectBox selectBoxMedium" onchange="onChangeState('state2')" >
                     <html:option value="">Select from list</html:option> 
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
                <td width="21%" class="formLabel">Bank Area:  <span class="mandatorySymbol">*</span> </td>
                 <td>
                    <html:select property="bankcity" styleId="state3" styleClass="selectBox selectBoxMedium" onchange="onChangeCity('state3')" >
                      <html:option value="">Select from list</html:option>
                   <html:optionsCollection name="alDistList" label="cacheDesc" value="cacheId" /> 
                   <%--   <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
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
                  <td width="21%" class="formLabel">Bank Branch: <span class="mandatorySymbol" >*</span> </td>
                 <td>
                    <html:select property="bankBranch" styleId="state4" styleClass="selectBox selectBoxMedium"><!--  onchange="onChangeBranch('state4')"  -->
                   <%--  <html:option value="">Select from list</html:option>  --%>
                     <html:optionsCollection name="alBranchList" label="cacheDesc" value="cacheId" />
                     <%--  <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" /> --%>
                    </html:select>
                </td>
                <td width="21%" class="formLabel">Swift Code: <span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="ifsc" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" maxlength="60" readonly="" onkeyup="ConvertToUpperCase(event.srcElement);" onblur="swiftCodeValidation();" />
                 </td>
                 </logic:notEqual>
                </tr>
                 <tr>
                                       
                 <td width="21%" class="formLabel">Bank Code: </td>
                 <td width="32%">
                  <html:text property="neft" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly=""/>
                 </td>
                 <td width="21%" class="formLabel">IBAN Number:<span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="micr" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                 </td>  
                 
                 </tr>
                 <tr>
                 <td width="21%" class="formLabel">Bank Phone No.: </td>
                 <td width="32%">
                  <html:text property="bankPhoneno" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                 </td>
                 <logic:notEmpty name="frmCustomerBankAcctGeneral" property="policyGroupSeqID">
                 <td width="21%" class="formLabel">EMAIL: </td>
                 <td width="32%">
                  <html:text property="emailID" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);"  maxlength="60" readonly="true"/>
                 </td>
                </logic:notEmpty>
                <logic:empty name="frmCustomerBankAcctGeneral" property="hospitalSeqId">
                 <td width="20%" class="formLabel">Address :</td>
                 <td width="33%"><html:text property="address1" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" maxlength="250" value="NA" readonly="true"/></td>
                </logic:empty>
                </tr>
                 
                 <logic:notEmpty name="frmCustomerBankAcctGeneral" property="hospitalSeqId">
                <tr>
         <td width="20%" class="formLabel">Address 1: <span class="mandatorySymbol">*</span></td>
        <td width="33%"><html:text property="address1" styleClass="textBox textBoxMedium" maxlength="250" /></td>
        <td width="17%" class="formLabel">Address 2:</td>
        <td width="30%"><html:text property="address2" styleClass="textBox textBoxMedium" maxlength="250" /></td>
                 </tr>
                 <tr>
               <td class="formLabel">Address 3: </td>
 	    <td><html:text property="address3" styleClass="textBox textBoxMedium" maxlength="250"/></td>
	    <td class="formLabel">Pincode:</td>
        <td><html:text property="pinCode" styleClass="textBox textBoxSmall" maxlength="10"/></td>
       </tr>   
       <tr>
       
       			<td width="21%" class="formLabel">EMAIL:</span></td>
             	<td width="32%">
             	 	<html:text property="emailID" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);"  maxlength="60" readonly="true"/>
             	</td>
        <td></td>
      <td class="formLabel">Issue Cheques To:</td>
           <td>
           <html:select property="issueChqToHosp" styleClass="selectBox selectBoxMedium">
                 <html:options collection="chequeCode" property="cacheId" labelProperty="cacheDesc"/>
           </html:select>
           </td>
        </tr>
     
    <!-- for projectX -->
                <tr>
                <td class="formLabel"> Start Date<span class="mandatorySymbol">*</span>  : </td>
                 <td>
        				<html:text property="startDate" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>" readonly="<%=viewmode%>"/>
        				
        					<a name="CalendarObjectStartDate" id="CalendarObjectStartDate" href="#" onClick="javascript:show_calendar('CalendarObjectStartDate','frmCustomerBankAcctGeneral.startDate',document.frmCustomerBankAcctGeneral.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a>
        			</td>
        			<td class="formLabel"> End Date<!-- <span class="mandatorySymbol">*</span> --> : </td>
                 <td>
        				<html:text property="endDate" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>" readonly="<%=viewmode%>"/>
        				
        					<a name="CalendarObjectEndDate" id="CalendarObjectEndDate" href="#" onClick="javascript:show_calendar('CalendarObjectEndDate','frmCustomerBankAcctGeneral.endDate',document.frmCustomerBankAcctGeneral.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a>
        				
        			</td>
                 </tr>
                 <!-- Ends -->
              </logic:notEmpty>
                 
         <tr>         
             <td class="formLabel">Country:<span class="mandatorySymbol">*</span> </td>
        <td>
         <logic:equal name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
         	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" onchange="onCountryCode(this);">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="OutsideQatarCountryList" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
         </logic:equal>
         <logic:notEqual name="frmCustomerBankAcctGeneral" property="bankAccountQatarYN" value="N">
        	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" onchange="onCountryCode(this);">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
        </logic:notEqual>
        </td>     
          </tr>  
          
          <logic:notEmpty name="frmCustomerBankAcctGeneral" property="hospitalSeqId">  
           <tr>
       			<td width="21%" class="formLabel">Reviewed Yes/No:</td>
             	<td width="32%">
					<html:checkbox property="reviewedYN" styleId="reviewedYN" disabled="true"/><!-- onclick="checkProviderGroup()" -->
             	</td>
		 </tr>
          </logic:notEmpty>
          
               
        <!--end eft -->
           
    </table>
</fieldset>
     <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="100%" align="center">
                <%
          if(viewmode==false)
          {
              %>
               <logic:notEmpty name="frmCustomerBankAcctGeneral" property="policyGroupSeqID">
                <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onUserMemberSubmit()"><u>S</u>ave</button>&nbsp;
                <button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button>&nbsp;
              
              </logic:notEmpty>
              <logic:notEmpty name="frmCustomerBankAcctGeneral" property="insurenceSeqId">
                <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onUserPolicySubmit()"><u>S</u>ave</button>&nbsp;
                <button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button>&nbsp;
              
              </logic:notEmpty>
              <logic:notEmpty name="frmCustomerBankAcctGeneral" property="hospitalSeqId">
                <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onUserHospitalSubmit()"><u>S</u>ave</button>&nbsp;
                <button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button>&nbsp;
                
              </logic:notEmpty>
              <%
          }//end of if(TTKCommon.isAuthorized(request,"Edit"))
        	  String from	=	request.getParameter("from");
          if("from".equals(from))
          {%>
        	  <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseHospReview();"><u>C</u>lose</button>
          <%}else{%>
        <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
        <%} %>
            </td>
        </tr>
    </table>  
<!-- E N D : Form Fields -->

    <!-- S T A R T : Buttons -->
    
<!-- E N D : Buttons -->
</div>
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="focusID" value="">
<input type="hidden" name="child" value="">
<html:hidden property="editmode"/>
<html:hidden property="policySeqID"/>
<html:hidden property="transactionYN"/>
<html:hidden property="reviewYN"/>
<input type="hidden" name="stoppreauthdate" id="stoppreauthdateflagid" value="<%= request.getSession().getAttribute("stoppreauthdate") %>">
<input type="hidden" name="stopclaimdate" id="stopclaimdateflagid" value="<%= request.getSession().getAttribute("stopclaimdate") %>">
<INPUT TYPE="hidden" NAME="tab" VALUE="">
<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
<INPUT TYPE="hidden" NAME="sublink" VALUE="">
<input type="hidden" name="reforward" value="">
<logic:notEmpty name="frmCustomerBankAcctGeneral" property="frmChanged">
	<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>

<script>
	if(document.getElementById("reviewYN").value=="Y")
		document.forms[1].reviewedYN.checked=true;
	else
		document.forms[1].reviewedYN.checked=false;
	</script>
	<html:hidden property="towDigitCountryCode" styleId="towDigitCountryCode"/>
</html:form>