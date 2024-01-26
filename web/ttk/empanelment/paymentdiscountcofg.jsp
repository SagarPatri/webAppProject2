<%
/** @ (#) paymentdiscountcnfg.jsp 3rd Jan, 2019
 * Project    			 : TTK Healthcare Services
 * File       	 			 : paymentdiscountcnfg.jsp
 * Author     			 : Vishwabandhu Kumar
 * Company    		 : Vidal Health TPA
 * Date Created	 : 3rd Jan, 2019
 *
 * @author 		 : 
 * Modified by   :
 * Modified date :
 * Reason        :
 */
 %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<!--  -->
<link rel="stylesheet" type="text/css" href="css/redmond/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="css/redmond/style.css" />
<link rel="stylesheet" type="text/css" href="css/redmond/jquery-ui.css" />
<script type="text/javascript" src="css/redmond/jquery.js"></script>
<script type="text/javascript" src="css/redmond/jquery-ui.min.js"></script>
<script type="text/javascript" src="css/redmond/jquery.multiselect.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/empanelment/paymentdiscountcofg.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
	function edit(rownum){
		document.forms[1].mode.value="getFastTrackDiscount";
		document.forms[1].rownum.value=rownum;
		document.forms[1].action="/FastTrackAction.do";
		JS_SecondSubmit=true;
		document.forms[1].submit();
	}
</SCRIPT>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/FastTrackAction.do" method="post">
	<!-- S T A R T : Page Title -->
    <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
      <tr>
      	<td>Discount Configuration Details :<bean:write name="frmFastTrack" property="caption"/></td>
      	<td align="right"></td>
        <td align="right" ></td>
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
 	<legend><font color="blue">Fast Track  Discount Configuration </font></legend>
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">   
      <tr>
        <!-- <td class="gridHeader" title="Fast Track Discount Payment From Day">Fast Track Discount Payment From Day</td> -->
        <td class="gridHeader" title="Fast Track Discount Payment In Day">Claim Process Within</td>
        <td class="gridHeader" title="Discount (in %)">Discount (in %)</td>
        <td class="gridHeader" title="Start Date">Start Date</td>
        <td class="gridHeader" title="End Date">End Date</td>
        <td class="gridHeader" title="Status">Status</td>
      </tr>
    
      <tr>
        <%-- <td class="formLabel"> 
   			<html:text property="fDays" styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isAmountValue(this)"/>&nbsp;&nbsp;Days
   		</td> --%>
   		<td class="formLabel"> 
   		<logic:empty name="frmFastTrack" property="fToDays">
   		<html:text property="fToDays" styleClass="textBox textBoxSmall" maxlength="3" onkeyup="isAmountValue(this)"/>&nbsp;&nbsp;&nbsp;&nbsp;Days&nbsp;&nbsp;&nbsp;
   		</logic:empty>
   		<logic:notEmpty name="frmFastTrack" property="fToDays">
   			<html:text property="fToDays" name="frmFastTrack" styleClass="textBox textBoxSmall disabledfieldType" maxlength="3" onkeyup="isAmountValue(this)" readonly="true"/>&nbsp;&nbsp;&nbsp;&nbsp;Days&nbsp;&nbsp;&nbsp;
   		</logic:notEmpty>
   		</td>
        <td class="formLabel"> 
        <logic:empty name="frmFastTrack" property="fDiscountPerc">
           			<html:text property="fDiscountPerc" styleClass="textBox textBoxSmall" maxlength="2" onkeyup="isAmountValue(this)"/>&nbsp;&nbsp;&nbsp;&nbsp;%&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</logic:empty>
		<logic:notEmpty name="frmFastTrack" property="fDiscountPerc">
		<html:text property="fDiscountPerc" name="frmFastTrack" styleClass="textBox textBoxSmall disabledfieldType" maxlength="5" onkeyup="isAmountValue(this)" readonly="true"/>&nbsp;&nbsp;&nbsp;&nbsp;%&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</logic:notEmpty>
		</td>
        <td class="formLabel">
        	<logic:empty name="frmFastTrack" property="fStartDate"> 
        	  <html:text property="fStartDate"  styleClass="textBox textDate" maxlength="10"/>&nbsp;&nbsp;&nbsp;&nbsp;
        	  <a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmFastTrack.fStartDate',document.frmFastTrack.fStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
	    				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="fStartDate" width="24" height="17" border="0" align="absmiddle">
	    		</a>
	    	</logic:empty>
	    	<logic:notEmpty name="frmFastTrack" property="fStartDate">
	    	<html:text property="fStartDate"  name="frmFastTrack" styleClass="textBox textDate disabledfieldType" maxlength="10" readonly="true"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    	<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="" onMouseOut="window.status='';return true;">
	    				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="fStartDate" width="24" height="17" border="0" align="absmiddle">
	    		</a>
	    	</logic:notEmpty>
	    		
       	</td>
        <td class="formLabel">
        <logic:empty name="frmFastTrack" property="fEndDate">
        	<html:text property="fEndDate"  styleClass="textBox textDate" maxlength="10"/>&nbsp;&nbsp;&nbsp;&nbsp;
        	<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmFastTrack.fEndDate',document.frmFastTrack.fEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
	    				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="fEndDate" width="24" height="17" border="0" align="absmiddle">
	    		</a>
        	</logic:empty>
        	<logic:notEmpty name="frmFastTrack" property="fEndDate">
        	<html:text property="fEndDate"  name="frmFastTrack" styleClass="textBox textDate disabledfieldType" maxlength="10" readonly="true"/>&nbsp;&nbsp;&nbsp;&nbsp;
        	<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
	    				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="fEndDate" width="24" height="17" border="0" align="absmiddle">
	    		</a>
        	</logic:notEmpty>
        	
       	</td>
          <td class="formLabel"> 
        	<html:select property ="fStatus" styleClass="selectBox selectBoxSmall">
        			<html:option value="ACT">Active </html:option>
           			<html:option value="INT">Inactive </html:option>	
   			</html:select> 
		</td>
		<td class="formLabel"> 
		</td>
		<td class="formLabel"> 
				<button type="button" name="Button"  accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onFastTrackSave();"><u>S</u>ave</button>&nbsp;
      	</td>
      </tr>
      
    </table>
	</fieldset>
	<br>
	<fieldset>
 	<legend><font color="blue">Fast Track Discount Configuration Details</font></legend>	
	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="fastTracktableData" />
	<!-- E N D : Grid -->
 	</fieldset>
	<!-- E N D : Form Fields -->
	
	 <%-- <fieldset>
 	<legend><font color="blue">Volume Based Payment Discount Configuration </font></legend>
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">   
      <tr>
        <td class="gridHeader"  title="Discount Type">Discount Type</td>
         <td class="gridHeader" title="Discount Volume Amount start Range">Discount Volume Amount start Range</td>
         <td class="gridHeader" title="Discount Volume Amount End Range">Discount Volume Amount End Range</td> 
        <td class="gridHeader" title="Discount (in %)">Discount (in %)</td>
        <!--  <td class="gridHeader" title="Volume Discount Period Start Date ">Period Start Date ... </td>  -->
        <td class="gridHeader" title="Start Date">Start Date</td>
        <td class="gridHeader" title="End Date">End Date</td>
        <td class="gridHeader" title="Status">Status</td>
      </tr>
      
      <tr>
        <td class="formLabel"> 		
        	<html:select property ="vDiscountType" styleClass="selectBox selectBoxSmall">
           			<html:option value="ANPD">Annual Net Payable Discount</html:option>
           			<html:option value="MNPD">Monthly Net Payable Discount</html:option>
           			<html:option value="ANPC">Annual Net Claims After Discount</html:option>
   			</html:select> 
		</td>
        <td class="formLabel"> 
           			<html:text property="vAmountStartRange" styleClass="textBox textBoxMedium" maxlength="10" onkeyup="isAmountValue(this)"/> &nbsp;&nbsp;QAR
		</td>
        <td class="formLabel"> 
        	<html:text property="vAmountEndRange" styleClass="textBox textBoxMedium" maxlength="10" onkeyup="isAmountValue(this)"/> &nbsp;&nbsp;QAR
       	</td>
        <td class="formLabel"> 
        	<html:text property="vDiscountPerc" styleClass="textBox textBoxSmall" maxlength="5" onkeyup="isAmountValue(this)"/> 
       	</td>
       	       <td class="formLabel"> 
        			<html:text property="vPeriodStartDate" styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isAmountValue(this)"/>
        			<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmFastTrack.vPeriodStartDate',document.frmFastTrack.vPeriodStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
	    				<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
	    		</a> 
       	</td>
        <td class="formLabel"> 
        	<html:text property="vStartDate" styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isAmountValue(this)"/> 
        	<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmFastTrack.vStartDate',document.frmFastTrack.vStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
	    				<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="vStartDate" width="24" height="17" border="0" align="absmiddle">
	    		</a>
       	</td>
       	   <td class="formLabel"> 
        	<html:text property="vEndDate" styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isAmountValue(this)"/> 
        	<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmFastTrack.vEndDate',document.frmFastTrack.vEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
	    				<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="vEndDate" width="24" height="17" border="0" align="absmiddle">
	    		</a>
       	</td>
        <td class="formLabel"> 
        	<html:select property ="vStatus" styleClass="selectBox selectBoxSmall">
        			<html:option value="ACT">Active </html:option>
           			<html:option value="INT">Inactive </html:option>	
   			</html:select> 
		</td>
		<td class="formLabel"> 
				<button type="button" name="Button"  accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onVolumeSave();"><u>S</u>ave</button>&nbsp;
      	</td>
      </tr>
      
    </table>
	</fieldset>
	<br>
	<fieldset>
 	<legend><font color="blue">Volume Based Payment Discount Configuration Details</font></legend>	
	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="volumeBasedtableData" />
	<!-- E N D : Grid -->
 	</fieldset>  --%>
	<!-- E N D : Form Fields -->

    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">  	
	       	<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
	       	<button type="button" name="Button1" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>&nbsp;        	
		</td>
	  </tr>
	</table>
	</div>
	<!-- E N D : Buttons -->	
	<input type="hidden" name="mode">
    <html:hidden property="caption" />
   	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
    <INPUT TYPE="hidden" NAME="hospSeqId" VALUE="<%=session.getAttribute("hospSeqId")%>">
	</html:form>
	<!-- E N D : Content/Form Area -->