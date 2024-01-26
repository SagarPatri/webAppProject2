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
<script type="text/javascript">
function onChangeCityForPartner(focusid)
{
	    document.forms[1].mode.value="doChangeAreaForPartner";
	    document.forms[1].focusID.value=focusid;
	    document.forms[1].bankcity.value="";
        document.forms[1].action="/PartnerBankAcctGeneralActionTest.do";
    	document.forms[1].submit();
}//end of onChangeState()

function onUserPartnerSubmit()
{
	if(!JS_SecondSubmit)
	{
		// 1:  space validation
			var ibanNum = document.forms[1].micr.value;
			if(ibanNum.indexOf(' ') >= 0){
			alert("Plase remove the space.");
			document.getElementById("micr").focus();
			return false;
			}
			
		// 2:  length validation
   			var bankLocation = document.forms[1].bankAccountQatarYN.value;
    	 	if(bankLocation=="Y"&& ibanNum.length != 29){
   			alert("IBAN Number should be 29 characters."); 
   			document.getElementById("micr").focus();
  		 	return false;
   	 		}
    	 	
    	// 3:  QA validation
    	 	var IbanNoFirstTwodigi 	= ibanNum.charAt(0)+ibanNum.charAt(1);
        	if(bankLocation=="Y" && ibanNum.length == 29 && IbanNoFirstTwodigi != 'QA')
        	{
       			alert("IBAN Number should be Start with characters QA"); 
       			document.getElementById("micr").focus();
       			return false;
       	 	}
		
        // 4:  swift code & IbanNo validation
        if(bankLocation=="Y"){
			var swiftcode 	= document.forms[1].ifsc.value;
	   	 	var IbanNo	= document.getElementById("micr").value;
	   	 	var IbanNo5to8Digit 	= IbanNo.charAt(4)+IbanNo.charAt(5)+IbanNo.charAt(6)+IbanNo.charAt(7);
	   	 	var swiftcode1to4Digit = swiftcode.charAt(0)+swiftcode.charAt(1)+swiftcode.charAt(2)+swiftcode.charAt(3);
	   	 	if(swiftcode != ""  && IbanNo != "") {
	 			if(IbanNo5to8Digit != swiftcode1to4Digit){
	 				alert("The 5th to 8th characters of IBAN Number and 1st to 4th characters of Swift code are not matching! Please enter valid IBAN Number & swift Code.");
	 				return false;
	 			}
	 		} }
		
	   	// 5:  Swift Code & Country Code validation
	   	 	var swiftcode = document.forms[1].ifsc.value;
 			var FifthSixthDigitOfswiftcode = swiftcode.charAt(4)+swiftcode.charAt(5);
 			var towDigitCountryCode = document.forms[1].towDigitCountryCode.value;
 			if(swiftcode != ""  && towDigitCountryCode != ""){
 				if(towDigitCountryCode != FifthSixthDigitOfswiftcode)
 				{
 					alert("Swift Code 5th & 6th letter not matching with Country Code! Please enter valid Swift Code.");
 					document.forms[1].ifsc.focus();
 					return false;
 				}
 			}
 			
 		// 6 : Bank Location & Country validation
 	 		var bankAccountQatarYN 	= document.forms[1].bankAccountQatarYN.value;
 	 		var countryCode			= document.forms[1].countryCode.value;
 	 		if(bankAccountQatarYN == 'Y' && countryCode != "")
 	 		{
 	 				if(countryCode !='134')
 	 					{
 	 						alert("Mismatch between Bank Location & Country! Please select valid Country.");
 	 						return;
 	 					}
 	 		} 
 			
		document.forms[1].tab.value="Partner Bank Details";
		document.forms[1].mode.value="doSavePartner";
		document.forms[1].action="/PartnerBankAcctSaveGeneralActionTest.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}//end of if(!JS_SecondSubmit)
}//end of onSave()trimForm(document.forms[1]);

function onClosePartner(){
	
	if(!TrackChanges()) return false;
   	//onReset();
   	document.forms[1].leftlink.value="Finance";
   	document.forms[1].sublink.value="Cust. Bank Details";
   	document.forms[1].tab.value="Search";
    document.forms[1].mode.value="doClosePartner";
    document.forms[1].action = "/ChangeIfscGeneralActionTest.do";
    document.forms[1].submit();
}

function onResetForPartner()
{

		document.forms[1].mode.value="doResetForPartner";
		document.forms[1].action="/PartnerBankAcctGeneralActionTest.do";
		document.forms[1].submit();
	
		
}	
function onChangeQatarYN()
{
	document.forms[1].reforward.value="PAT";
    document.forms[1].mode.value="doChangeQatarYN";
	document.forms[1].action="/PartnerBankAcctGeneralActionTest.do";
	document.forms[1].submit();
	
}
function swiftCodeValidation()
{
	var ifsc = document.forms[1].ifsc.value;
	if(ifsc.indexOf(' ') >= 0)
	{
		alert("Space not allowed in the Swift Code.");
		document.forms[1].ifsc.value="";
		document.forms[1].ifsc.focus();
		return false;
	}
	
	if(ifsc.length < 6)
	{
		alert("Swift Code should be minimum 6 digits.");
		document.forms[1].ifsc.value="";
		return;
	}
}

 function onCountryCode(Obj)
{
	var countryCode = Obj.value;
	document.forms[1].mode.value="doGetCountryCodeForPartner";
	document.forms[1].action="/PartnerBankAcctGeneralActionTest.do?countryCode="+countryCode;
	document.forms[1].submit();
}
</script>

<script>
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getParameter("focusID"))%>";
</script>
<%
    boolean viewmode=true;
	pageContext.setAttribute("bankState", Cache.getCacheObject("stateCode"));
   	pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
	pageContext.setAttribute("OutsideQatarCountryList", Cache.getCacheObject("OutsideQatarCountryList"));
   	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
%>

<!-- S T A R T : Content/Form Area -->
<html:form action="/PartnerBankAcctGeneralActionTest.do" >


<!-- S T A R T : Page Title -->
    <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="57%">Bank Account Details <bean:write name="frmPartnerBankAcctGeneral" property="caption"/> <bean:write name="frmPartnerBankAcctGeneral" property="partnerEmnalNumber"/></td>
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
             
                
                <logic:notEmpty name="frmPartnerBankAcctGeneral" property="partnerSeqId">
                <tr>
                <td width="21%" class="formLabel">Empanelment No: </td>
                <td width="33%" class="textLabelBold"><bean:write name="frmPartnerBankAcctGeneral" property="partnerEmnalNumber"/></td>
                <td width="21%" class="formLabel">Partner Name: </span></td>
                <td width="33%" class="textLabelBold"><bean:write name="frmPartnerBankAcctGeneral" property="partnerName"/></td>    
              </tr>
              <tr>
               <td width="21%" class="formLabel">Account Name:<span class="mandatorySymbol">*</span></td>
                <td width="32%">
               <html:text property="accountinNameOf" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                <td width="21%" class="formLabel">Empanelment Status: </span></td>
               <td width="33%" class="textLabelBold"><bean:write name="frmPartnerBankAcctGeneral" property="partnerEmpnalDesc"/></td>
                </tr>
                </logic:notEmpty>
                <!-- END EFT -->
               
              </tr>
        </table>
</fieldset>

<fieldset>
    <legend>Bank Details</legend>
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
        
        <logic:notEmpty name="frmPartnerBankAcctGeneral" property="partnerSeqId">
        
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
                <td width="21%" class="formLabel">Bank Name: <span class="mandatorySymbol">*</span></td>
              <td width="32%">
              <html:text property="bankname" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                <td width="21%" class="formLabel">Bank Account No.:</td>
              <td width="32%">
              <html:text property="bankAccno" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" readonly="true"  disabled="true"/>
                </td>
            </tr>
                <tr>
                                           
                 </tr>
                 <tr>
                 
                 <logic:equal name="frmPartnerBankAcctGeneral" property="bankAccountQatarYN" value="N">
                  <td width="21%" class="formLabel">City: <span class="mandatorySymbol">*</span></td>
                <td width="32%">
                <html:text property="bankState" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                 <td width="21%" class="formLabel"> Area: </td>
                <td width="32%">
                <html:text property="bankcity" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                </td>
                 </logic:equal>
 				<logic:notEqual name="frmPartnerBankAcctGeneral" property="bankAccountQatarYN" value="N">  
 			   <td width="21%" class="formLabel">City: <span class="mandatorySymbol">*</span> </td>
                 <td>
                    <html:select property="bankState" styleId="state2" styleClass="selectBox selectBoxMedium" onchange="onChangeCityForPartner('state2')" >
                   <%--  <html:option value="">Select from list</html:option> --%>
                    <html:optionsCollection name="bankState" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
                <td width="21%" class="formLabel"> Area: <!-- <span class="mandatorySymbol">*</span> --></td>
                 <td>
                    <html:select property="bankcity" styleId="state3" styleClass="selectBox selectBoxMedium" >
                    <%-- <html:option value="">Select from list</html:option>  --%>
                    <html:optionsCollection name="alCityList" label="cacheDesc" value="cacheId" />
                    </html:select>
                </td>
 				</logic:notEqual>
                </tr>
                 <tr>
                 
                 <td width="21%" class="formLabel">Branch Name: 
 				  <logic:notEqual name="frmPartnerBankAcctGeneral" property="bankAccountQatarYN" value="N">   <span class="mandatorySymbol">*</span> </logic:notEqual>
                 </td>
                 <td width="32%">
                  <html:text property="branchName" styleClass="textBox textBoxMedium"  maxlength="60" />
                 </td>
                <td width="21%" class="formLabel">Swift Code: <span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="ifsc" styleClass="textBox textBoxMedium"  maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement);" onblur="swiftCodeValidation();"/>
                 </td>
                </tr>
                 <tr>
                 
                 <td width="21%" class="formLabel">IBAN Number:<span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="micr" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60"/>
                 </td>  
                 
                 </tr>
                 
                <tr>
         <td width="20%" class="formLabel">Address 1: <span class="mandatorySymbol">*</span></td>
        <td width="33%"><html:text property="address1" styleClass="textBox textBoxMedium" maxlength="250" /></td>
        <td width="17%" class="formLabel">Address 2:</td>
        <td width="30%"><html:text property="address2" styleClass="textBox textBoxMedium" maxlength="250" /></td>
                 </tr>
                 <tr>
               <td class="formLabel">Address 3: </td>
 	    <td><html:text property="address3" styleClass="textBox textBoxMedium" maxlength="250"/></td>
	    <td class="formLabel">PO Box:</td>
        <td><html:text property="pinCode" styleClass="textBox textBoxSmall" maxlength="10"/></td>
       </tr>   
       <tr>
         <td class="formLabel">Country:<span class="mandatorySymbol">*</span> </td>
        <td>
         <logic:equal name="frmPartnerBankAcctGeneral" property="bankAccountQatarYN" value="N">
         	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" onchange="onCountryCode(this);">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="OutsideQatarCountryList" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
         </logic:equal>
         <logic:notEqual name="frmPartnerBankAcctGeneral" property="bankAccountQatarYN" value="N">
        	<html:select property ="countryCode" styleClass="selectBox selectBoxMedium" onchange="onCountryCode(this);">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc" />
          </html:select>
        </logic:notEqual>
        </td>   
        </tr>
     
    <!-- for projectX -->
                <tr>
                <td class="formLabel"> Start Date<span class="mandatorySymbol">*</span> : </td>
                 <td>
        				<html:text property="startDate" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>" readonly="<%=viewmode%>"/>
        				
        					<a name="CalendarObjectStartDate" id="CalendarObjectStartDate" href="#" onClick="javascript:show_calendar('CalendarObjectStartDate','frmPartnerBankAcctGeneral.startDate',document.frmPartnerBankAcctGeneral.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a>
        				
        			</td>
        			<td class="formLabel"> End Date<!-- <span class="mandatorySymbol">*</span> --> : </td>
                 <td>
        				<html:text property="endDate" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>" readonly="<%=viewmode%>"/>
        				
        					<a name="CalendarObjectEndDate" id="CalendarObjectEndDate" href="#" onClick="javascript:show_calendar('CalendarObjectEndDate','frmPartnerBankAcctGeneral.endDate',document.frmPartnerBankAcctGeneral.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
        						<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
        					</a>
        				
        			</td>
                 </tr>
              </logic:notEmpty>
           
    </table>
</fieldset>
     <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="100%" align="center">
                <%
          if(viewmode==false)
          {
              %>
              <logic:notEmpty name="frmPartnerBankAcctGeneral" property="partnerSeqId">
                <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserPartnerSubmit()"><u>S</u>ave</button>&nbsp;
                <button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onResetForPartner()"><u>R</u>eset</button>&nbsp;
                
              </logic:notEmpty>
              <%
          }//end of if(TTKCommon.isAuthorized(request,"Edit"))
        	  String from	=	request.getParameter("from");
          if("from".equals(from))
          {%>
        <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClosePartner();"><u>C</u>lose</button>
          <%}else{%>
          <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClosePartner();"><u>C</u>lose</button>
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

<INPUT TYPE="hidden" NAME="tab" VALUE="">
<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
<INPUT TYPE="hidden" NAME="sublink" VALUE="">
<input type="hidden" name="reforward" value="">
<html:hidden property="towDigitCountryCode"/>
<logic:notEmpty name="frmPartnerBankAcctGeneral" property="frmChanged">
	<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>

<script>
	if(document.getElementById("reviewYN").value=="Y")
		document.forms[1].reviewedYN.checked=true;
	else
		document.forms[1].reviewedYN.checked=false;
	</script>
</html:form>