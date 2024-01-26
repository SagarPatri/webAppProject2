<%
/**
 * @ (#) tariffEdit.jsp 9th April 2015
 * Project      : TTK HealthCare Services
 * File         : tariffEdit.jsp
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 11th April 2015
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/empanelment/tariffEdit.js"></script>
<%
pageContext.setAttribute("alServiceName", Cache.getCacheObject("serviceName"));
pageContext.setAttribute("alServiceNamePC", Cache.getCacheObject("serviceNamePC"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/TarrifSearchAction.do" method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td>Tariff Details&nbsp; [<bean:write name="frmSearchTariffItem" property="activityCode"/>]</td>
		    <%-- <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td> --%>
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
					<bean:write name="updated" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
	
	<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error" width="16" height="16" align="absmiddle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	
	<!-- D A T A FROM VO FOR TARIFF DETAILS -->
	<fieldset>
	<legend>Tariff Details</legend>
	<table class="formContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
			<td width="20%" class="textLabelBold">Internal Code:</td>
			<td width="30%" class="formLabel">	<bean:write name="frmSearchTariffItem" property="strInternalCode"/></td>
			
			<td width="20%" class="textLabelBold">Activity Code<span class="mandatorySymbol">*</span>:</td>
			<td width="30%" class="formLabel">
			 <%-- <bean:write name="frmSearchTariffItem" property="activityCode"/> --%>
			 <html:text name="frmSearchTariffItem" property="activityCode" styleId="activityCode" styleClass="textBox textBoxMedium" onblur="getActivityCodeDetails('ACT');" readonly="true" /><!--onblur="getActivityCodeDetails();"  -->
			<a href="#" accesskey="g"  onClick="javascript:selectActivityCode()" class="search"> <img src="/ttk/images/EditIcon.gif" title="Select Activity Code" alt="Select Activity Code" width="16" height="16" border="0" align="absmiddle">&nbsp;</a>
			 </td>
  				
	</tr>
	<tr>
		<td width="20%" class="textLabelBold">Type of Tariff:</td>
		<td width="30%" class="formLabel">	<bean:write name="frmSearchTariffItem" property="tariffType"/></td>
        
        <td width="20%" class="textLabelBold">Payer:</td>
        <td width="30%" class="formLabel"><bean:write name="frmSearchTariffItem" property="insCompName"/></td>
   </tr>
	<tr>
		<td width="20%" class="textLabelBold">Provider:</td>
        <td width="30%" class="formLabel"><bean:write name="frmSearchTariffItem" property="hospName"/></td>
        
		
        <td width="20%" class="textLabelBold">Discount :</td>
        <td width="30%" class="formLabel">Percentage &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount<br>
		<html:text property="discPercentage" styleClass="textBox textBoxSmallest"  size="5" onkeyup="isNumeric(this)" onblur="calcDiscount(this)"/>%
		&nbsp;&nbsp;&nbsp;&nbsp;
		 <html:text property="discAmount" styleClass="textBox textBoxSmallest"  size="5" onkeyup="isNumeric(this)" onblur="calcDiscountOnAmt(this)"/> 
        </td>
   </tr>
   <tr>
   <td width="20%" class="textLabelBold">Gross Amount:</td>
        <td width="30%" class="formLabel">
       	 <html:text property="grossAmount" styleClass="textBox textBoxSmall" />
        </td>
        
   </tr>
   <tr>
		<td width="20%" class="textLabelBold">Price Ref No:</td>
        <td width="30%" class="textLabelBold"><bean:write name="frmSearchTariffItem" property="priceRefNo"/></td>
        
        <td width="20%" class="textLabelBold">Service Type :</td>
         <td width="30%" class="formLabel">
			<html:select property="serviceSeqId" name="frmSearchTariffItem" styleClass="selectBox selectBoxMedium">
		        	<%if(!"ACTIVITY TARIFF".equals(request.getSession().getAttribute("switchToVal"))){%>
		        	<html:optionsCollection name="alServiceNamePC" label="cacheDesc" value="cacheId" />
		        	<bean:write name="frmSearchTariffItem" property="serviceSeqId"/>
		        	<%}else{%>
		        	<html:optionsCollection name="alServiceName" label="cacheDesc" value="cacheId" />
		        	<bean:write name="frmSearchTariffItem" property="serviceSeqId"/>
		        	<%}%>
		        	
			</html:select>
		</td>
   </tr>
   <tr>
		<td width="20%" class="textLabelBold">Package Id:</td>
        <td width="30%" class="formLabel"><bean:write name="frmSearchTariffItem" property="packageId"/></td>
        <td width="20%" class="textLabelBold">Bundle Id:</td>
        <td width="30%" class="formLabel"><bean:write name="frmSearchTariffItem" property="bundleId"/></td>
   </tr>
   <tr>
   <td width="20%" class="textLabelBold">Start Date:</td>
	<td width="30%" class="formLabel">
		<html:text property="startDate" styleClass="textBox textDate" maxlength="10" disabled="true"/>
			<!-- <a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmSearchTariffItem.startDate',document.frmSearchTariffItem.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
				<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
			</a> -->
	</td>
	<td width="20%" class="textLabelBold">End Date:</td>
	<td width="30%" class="formLabel">
		<html:text property="endDate" styleClass="textBox textDate" maxlength="10"/>
			<a name="CalendarObjectempDate12" id="CalendarObjectempDate12" href="#" onClick="javascript:show_calendar('CalendarObjectempDate12','frmSearchTariffItem.endDate',document.frmSearchTariffItem.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
			</a>
	</td>
   </tr>
   <tr>
		<td width="20%" class="textLabelBold">Corporate:</td>
        <td width="30%" class="formLabel"><bean:write name="frmSearchTariffItem" property="grpName"/></td>
        <td width="20%" class="textLabelBold">Activity Description:</td>
        <td width="30%" class="formLabel"><bean:write name="frmSearchTariffItem" property="activityDesc"/></td>
   </tr>
   <tr>
		<td width="20%" class="textLabelBold"></td>
        <td width="30%" class="formLabel"></td>
        <td width="20%" class="textLabelBold">Internal Description:</td>
        <td width="30%" class="formLabel"><bean:write name="frmSearchTariffItem" property="internalCodeDesc"/></td>
   </tr>
   <tr>
		<td width="20%" class="textLabelBold">Remarks:<span class="mandatorySymbol">*</span>
<%-- 		<%if(isActCodeChanged!=null&&isActCodeChanged.equals("true")){%>
		
		<%} %> --%>
		</td>
        <td width="30%" class="formLabel" colspan="3">
        <html:textarea property="remarks" styleClass="textBox textAreaLong"/>
   </tr>
      </table>
      </fieldset>
    <!-- E N D : SELCTIONS -->
	<!-- E N D : Buttons -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		 <tr>
		    <td align="center" colspan="4">
		    	<button type="button" name="Button" accesskey="b" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBack();"><u>B</u>ack</button>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    <button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSaveTariffItem();"><u>S</u>ave</button>
	        </td>
		</tr>
	</table>
	<!-- E N D : Form Fields -->
   </div>
   	<INPUT TYPE="hidden" NAME="reforward" value="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<INPUT TYPE="hidden" NAME="switchTo" id="switchTo" VALUE="<%=session.getAttribute("switchToVal")%>"> 
 </html:form>