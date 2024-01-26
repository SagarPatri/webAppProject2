<%
/** @ (#) detailreportparam.jsp
 * Project     : TTK Healthcare Services
 * File        : detailreportparam.jsp
 * Author      : Kishor kumar S H
 * Company     : RCS Technologies
 * Date Created: August 5,2008
 * @author 		 : Kishor kumar S H
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>

<%
	pageContext.setAttribute("insuranceCompany", Cache.getCacheObject("insuranceCompany"));
	pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));


%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/misreports/detailreportparam.js"></script>

<html:form action="/MISFinanceReportsAction.do">
<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	<tr>
 <%--    	<td><% String temp=(String)request.getParameter("repType");
    	out.print(request.getParameter("repType")); %> </td> --%>
     <td> <%=request.getSession().getAttribute("repType")%> </td>
    	
	</tr>
</table>
<!-- E N D : Page Title -->
<html:errors/>
<!-- Start of form fields -->
	<!-- Start of Parameter grid -->
	<div class="contentArea" id="contentArea">
	<fieldset>
	 <legend>Report Parameters </legend>
	  <table border="0" align="center" cellpadding="0" cellspacing="0" class="searchContainer">
		<logic:notEqual value="exchangeRate" scope="request" name="logicType">
		<tr>
		
		<td nowrap>Insurance Company:<br>
			<html:select property="insuranceCompany" styleId="insuranceCompany" styleClass="selectBox selectBoxMedium">
		  	 	  <html:option value="">Any</html:option>
		          <html:optionsCollection name="insuranceCompany" label="cacheDesc" value="cacheId" />
            </html:select>
	    </td>
	    
	    <td nowrap>Float Account No. :<br>
            <html:text property="sFloatAccNo" styleId="sFloatAccNo" styleClass="textBox textBoxLarge" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
        </td>
        
        <td nowrap>Debit Note No. :<br>
            <html:text property="fDebitNoteNo" styleId="fDebitNoteNo" styleClass="textBox textBoxLarge" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
        </td>
	</tr>
	
	<tr>

	    <td nowrap>Provider Name :<br>
            <html:text property="sProviderName" styleId="sProviderName" styleClass="textBox textBoxLarge" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
        </td>
        <td nowrap>Corporate Name :<br>
            <html:text property="fCorpName" styleId="fCorpName" styleClass="textBox textBoxLarge" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
        </td>
        <logic:equal value="pending" scope="request" name="logicType">
        <td nowrap>Group Id :<br>
            <html:text property="fGroupId" styleId="fGroupId" styleClass="textBox textBoxLarge" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
        </td>
        </logic:equal>
        <logic:equal value="detailed" scope="request" name="logicType">
        <td nowrap>Group Id :<br>
            <html:text property="fGroupId" styleId="fGroupId" styleClass="textBox textBoxLarge" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
        </td>
        </logic:equal>
    </tr>
        
    <tr>
    <logic:notEqual value="pending" scope="request" name="logicType">
        <td nowrap>From Date:<br>
		<html:text property="sChequeFromDate" styleId="sChequeFromDate" styleClass="textBox textDate" maxlength="10" value=""/>
		<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmMISFinanceReports.sChequeFromDate',document.frmMISFinanceReports.sChequeFromDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
			<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
		</a>
     		</td>
	</logic:notEqual>
	
	<logic:equal value="pending" scope="request" name="logicType">
	<td nowrap>As on Date:<br>
		<html:text property="sChequeToDate" styleId="sChequeToDate" styleClass="textBox textDate" maxlength="10" value=""/>
		<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmMISFinanceReports.sChequeToDate',document.frmMISFinanceReports.sChequeToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
			<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
		</a>
     		</td>
	</logic:equal>
	
    <logic:notEqual value="pending" scope="request" name="logicType">
    	<td nowrap>To Date:<br>
		<html:text property="sChequeToDate" styleId="sChequeToDate" styleClass="textBox textDate" maxlength="10" value=""/>
		<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmMISFinanceReports.sChequeToDate',document.frmMISFinanceReports.sChequeToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
			<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
		</a>
     		</td>
	</logic:notEqual>
	</logic:notEqual>
	<logic:equal value="exchangeRate" scope="request" name="logicType">
	<tr>
	<td nowrap>Date:<br>
		<html:text property="sExchangeToDate" styleId="sExchangeToDate" styleClass="textBox textDate" maxlength="10" value=""/>
		<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmMISFinanceReports.sExchangeToDate',document.frmMISFinanceReports.sExchangeToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
			<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
		</a>
     		</td>
     		<td nowrap>Country:<br>
			<html:select property="sCountry" styleId="sCountry" styleClass="selectBox selectBoxMedium">
		  	 	  <html:option value="">Any</html:option>
		          <html:optionsCollection name="countryCode" label="cacheDesc" value="cacheId" />
            </html:select>
        &nbsp;&nbsp;&nbsp;&nbsp;<a href="#" accesskey="s" onClick="javascript:doSearchExchangeRate()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
            
	    </td>
     		</tr>	
	</logic:equal>
	
	</tr>
	</table>
	
	</fieldset>
		<logic:equal value="exchangeRate" scope="request" name="logicType">
<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableData"/>
<!-- E N D : Grid -->
	</logic:equal>
	
	<!-- End of parameter grid -->
	<!-- Start of Report Type - PDF/EXCEL list and generate button -->
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" align="center">
		<logic:equal value="detailed" scope="request" name="logicType">
			<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateDetailReport();"><u>G</u>enerateReport</button>
		</logic:equal>
		<logic:equal value="pending" scope="request" name="logicType">
			<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGeneratePendingReport();"><u>G</u>enerateReport</button>
		</logic:equal>
		<logic:equal value="exchangeRate" scope="request" name="logicType">
			<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateExchangeReport();"><u>G</u>enerateReport</button>
		</logic:equal>
			&nbsp;
			<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
		</td>
	</tr>
		  			<ttk:PageLinks name="tableData"/>
	
    </table>
		</div>
	<!-- End of Report Type - PDF/EXCEL list and generate button -->
<!-- End of form fields -->	
<input type="hidden" name="mode">
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
<%--  	<INPUT TYPE="text" NAME="flag" VALUE="<%=request.getAttribute("logicType") %>">
 --%></html:form>