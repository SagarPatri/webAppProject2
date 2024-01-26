
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/fraudcase/campaignDetails.js"></SCRIPT> 
 <script>
var JS_SecondSubmit=false;
</script> 
<%
	  pageContext.setAttribute("CFDProviderList",Cache.getCacheObject("CFDProviderList"));
	  pageContext.setAttribute("campaignRemark",Cache.getCacheObject("campaignRemark"));
	  
%>

<!-- S T A R T : Content/Form Area -->
<html:form action="/CFDCampaignDetailAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">Campaign Details</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
</logic:notEmpty>
<%
	boolean editStatus = false;
%>
 <logic:equal name="frmCampaignDetail" property="displayCampStatusFlag" value="Y">
	<% 
		 editStatus=false; 
	%>
 </logic:equal>
 <logic:notEqual  name="frmCampaignDetail" property="displayCampStatusFlag" value="N" >						
 <%
	editStatus = true;
  %>
  </logic:notEqual>	
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
      <td nowrap>Campaign Name:<span class="mandatorySymbol">*</span><br>
            <html:text property="campaignName" styleId="campaignName" styleClass="textBox textBoxMedLarge" name="frmCampaignDetail" disabled="<%=editStatus%>" />
        </td>
       <td nowrap>Provider Name:<span class="mandatorySymbol">*</span><br>
		    <html:select property="cfdProviderName" styleId="cfdProviderName" name="frmCampaignDetail"  styleClass="selectBox selectBoxLarge" disabled="<%=editStatus%>" >
		    	<html:option value="">Any</html:option>
  				<html:optionsCollection name="CFDProviderList" label="cacheDesc" value="cacheId" />
		    </html:select>
        </td>
      
        <td nowrap>Campaign Start Date:<span class="mandatorySymbol">*</span><br>
      			<html:text property="campaginStartDate" styleId="campaginStartDate" name="frmCampaignDetail" styleClass="textBox textDate" maxlength="10" disabled="<%=editStatus%>" />
      		<logic:equal name="frmCampaignDetail" property="displayCampStatusFlag" value="N">	
      			<A NAME="campaginStartDate" ID="campaginStartDate" HREF="#" onClick="javascript:show_calendar('campaginStartDate','forms[1].campaginStartDate',document.forms[1].campaginStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"   width="24" height="17" border="0" align="absmiddle"></a>
      		</logic:equal>
      		<logic:equal name="frmCampaignDetail" property="displayCampStatusFlag" value="Y">	
				<img src="/ttk/images/CalendarIcon.gif"    width="24" height="17" border="0" align="absmiddle">
      		</logic:equal>
      	</td> 
      	<logic:equal value="CLOSED" name="frmCampaignDetail" property="campaginStatus">
      	 <td nowrap>Campaign End Date:<span class="mandatorySymbol">*</span><br>
      			<html:text property="campaginEndDate" styleId="campaginEndDate" name="frmCampaignDetail" styleClass="textBox textDate" maxlength="10"/>
      			<A NAME="campaginEndDate" ID="campaginEndDate" HREF="#" onClick="javascript:show_calendar('campaginEndDate','forms[1].campaginEndDate',document.forms[1].campaginEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
      	</td> 
      	</logic:equal>
      </tr>
      
      <tr>
      <logic:equal value="Y" name="frmCampaignDetail" property="displayCampStatusFlag">
        <td nowrap>Campaign Status:<br>
	        <html:select property="campaginStatus"  styleId="campaginStatus" styleClass="selectBox selectBoxMedium" name="frmCampaignDetail" onchange="onChangeCampaignStatus();">
  				<html:option value="OPEN">Open</html:option>
				<html:option value="CLOSED">Closed</html:option>
	    	</html:select> 
		</td>
	</logic:equal>	
	<logic:equal value="CLOSED" name="frmCampaignDetail" property="campaginStatus">	
		<td nowrap>Amount Utilized For Investigation:<br>
            <html:text property="utilizedAmount"  styleClass="textBox textBoxMedLarge" name="frmCampaignDetail" onkeyup="isNumeric(this)"/>
        </td>
        <td nowrap>Amount Saved<br>
            <html:text property="savedAmount"  styleClass="textBox textBoxMedLarge" name="frmCampaignDetail" onkeyup="isNumeric(this)"/>
        </td>
         <td nowrap>Total no of Cases:<br>
            <html:text property="totCases"  styleClass="textBox textBoxMedLarge" name="frmCampaignDetail" onkeyup="isNumericOnly(this)"/>
        </td>
         <td nowrap>Campaign Remarks:<span class="mandatorySymbol">*</span><br>
	        <html:select property="campaignRemarks" styleId="campaignRemarks" styleClass="selectBox selectBoxMedium" name="frmCampaignDetail">
  				<html:option value="">Select from list</html:option>
  				<html:optionsCollection name="campaignRemark" label="cacheDesc" value="cacheId" />
	    	</html:select> 
		</td>
     </logic:equal>   
      </tr>
    <logic:equal value="CLOSED" name="frmCampaignDetail" property="campaginStatus">	   
        <tr>
        <td nowrap>Other Remarks:<br>
	       	<html:textarea property="otherRemarks" name="frmCampaignDetail" styleClass="textBox textAreaLong" style="width:243px;height:83px;"/>
		</td>
      </tr>
    </logic:equal>  
     </table>
	<!-- E N D : Search Box -->
	
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center"   border="0" cellspacing="0" cellpadding="0">
    	<tr>
    		
        	<td align="center"><br><br>
        		<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave()"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;
        		<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose()"><u>C</u>lose</button>
        	</td>
      	</tr>
      
	</table>
	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<html:hidden name="frmCampaignDetail" property="campaignDtlSeqId" />
	<html:hidden name="frmCampaignDetail" property="displayCampStatusFlag" styleId="displayCampStatusFlag" />
</html:form>
	<!-- E N D : Content/Form Area -->