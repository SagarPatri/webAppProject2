<%
/**
 * @ (#) invoicelist.jsp 25 Oct 2007
 * Project      : TTK HealthCare Services
 * File         : invoicelist.jsp
 * Author       : Yogesh S.C
 * Company      : Span Systems Corporation
 * Date Created : 25 Oct 2007
 *
 * @author       :Yogesh S.C
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%
pageContext.setAttribute("debitType",Cache.getCacheObject("debitType"));
pageContext.setAttribute("reinsurerNameList", Cache.getCacheObject("reinsurerNameList"));
%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/finance/bordereaulist.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var TC_Disabled = true;
</SCRIPT>
	<!-- S T A R T : Content/Form Area -->	
	<html:form action="/GenerateBordereauListAction.do" > 
	
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="51%">List of Invoices </td>     
    <td width="49%" align="right" class="webBoard">&nbsp;</td>
  </tr>
</table>
	<!-- E N D : Page Title --> 
	
	<!-- S T A R T : Form Fields -->
	<div class="contentArea" id="contentArea">
	
	<html:errors />
			<logic:notEmpty name="errorMsg" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="errorMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>

			<!-- S T A R T : Success Box -->
			<logic:notEmpty name="updated" scope="request">
				<table align="center" class="successContainer" style="display:"
					border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:message
								name="updated" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			
			
			
	<%-- <table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        
        <td nowrap>Policy No.:<br>
            <html:text property="strPolicyNbr" styleClass="textBox textBoxMedium" maxlength="60"/></td>
        <td nowrap>Group Id:<br>
            <html:text property="groupID" styleClass="textBox textBoxMedium" maxlength="60"/></td>
        <td nowrap>Group Name:<br>
            <html:text property="groupName" styleClass="textBox textBoxMedium" maxlength="60"/></td>
        <td nowrap>Invoice Date From:<br>
      <html:text  property="sFromDate"  styleClass="textBox textDate" />
      <A NAME="calStartDate" ID="calStartDate" HREF="#" onClick="javascript:show_calendar('calStartDate','frmInvoices.sFromDate',document.frmInvoices.sFromDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
      	<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" width="24" height="17" border="0" align="absmiddle"></a>
      	</td>
        <td nowrap>Invoice Date To:<br>
      <html:text property="sToDate" styleClass="textBox textDate" /><A NAME="calEndDate" ID="calEndDate" HREF="#" onClick="javascript:show_calendar('calEndDate','frmInvoices.sToDate',document.frmInvoices.sToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a></td>
		<td nowrap>Status:<br>
            <html:select property="sStatus" styleId="select10" styleClass="selectBox selectBoxMedium">
              <html:option value="">Any</html:option>
              <html:optionsCollection name="debitType" value="cacheId" label="cacheDesc"/>              
              </html:select></td>
        <!-- <td valign="bottom" nowrap width="100%"><a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td> -->
      </tr>
    </table>	 --%>
	<!-- E N D : Form Fields -->
	<table align="center" class="searchContainer" border="0"
            cellspacing="0" cellpadding="0">
            <tr class="searchContainerWithTab">
                   <td class="formLabel" style="width: 100px;" >ReInsurer:</td>	 				
	  				<td>	  				
	 					<html:select property="reinsurerId" styleId="reinsurerId" styleClass="selectBox" style="width:160px;">
							<html:option value="">Select from list</html:option>
							<html:options collection="reinsurerNameList" property="cacheId" labelProperty="cacheDesc" />
						</html:select>		
	 				</td>
	</tr>
	</table>
		<table align="center">
			<tr>
				<td colspan="4">&nbsp;</td>
				<td colspan="4">&nbsp;</td>
				<td colspan="4">&nbsp;</td>

				<td colspan="4">&nbsp;</td>
				<td colspan="4"><button name="mybutton" class="olbtnLarge"
						accesskey="d" onclick="onGetInvoiceList(this);" type="button">
						<u>G</u>enerate Letter
					</button></td>
		</table>
		<div id="bordereauxReport" style="width: 50%;">
		<ttk:HtmlGrid name="BordereauxTableData"/>
    <!-- S T A R T : Buttons -->
    
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
      <tr>                
        <td width="25%">&nbsp;</td>
        <td width="50%" align="right">
         </td>
         <td width="25%">&nbsp;</td>          
      </tr>
      <ttk:PageLinks name="BordereauxTableData"/>
    </table>
    </div>
    <!-- E N D : Buttons -->
<p>&nbsp;</p>
	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="identifier" VALUE="">
	<input type="hidden" name="child" value="">
	</html:form>
	<!-- E N D : Content/Form Area -->
	
	