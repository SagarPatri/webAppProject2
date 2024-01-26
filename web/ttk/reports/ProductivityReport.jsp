

<%
/** @ (#) ProductivityReport.jsp March 17, 2020
 * Project     : TTK Healthcare Services
 * File        : ProductivityReport.jsp
 * Author      : Deepthi Meesala
 * Company     : RCS Technologies 
 * Date Created: March 17, 2020
 *
 * @author 		 :  
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/reports/cashlessReportsList.js"></script>

<!-- S T A R T : Content/Form Area -->
<html:form action="/CashlessReportsAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td>Report</td>
	    </tr>
</table>
<!-- E N D : Page Title -->
<html:errors/>
<div class="contentArea" id="contentArea">
<!-- S T A R T : AllReportsList -->
    <fieldset>
    <legend>Productivity Report</legend>
    
    
    <table border="0" align="center" cellpadding="0" cellspacing="0" class="formContainer">
    
    <tr>
						 <td nowrap>Start Date <span class="mandatorySymbol">*</span></td>
						<td>
							<html:text property="startDate" styleId="startDate" styleClass="textBox textDate" maxlength="10" /> 
							<a NAME="CalendarObjectstartDate" ID="CalendarObjectstartDate" HREF="#"	onClick="javascript:show_calendar('CalendarObjectstartDate','forms[1].startDate',document.forms[1].startDate.value,'',event,148,178);return false;"	onMouseOver="window.status='Calendar';return true;"	onMouseOut="window.status='';return true;">
							<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="startDate" width="24" height="17" border="0" align="absmiddle"></a>
						</td>
						<td nowrap>End Date <span class="mandatorySymbol">*</span></td>
						<td>
							<html:text property="endDate" styleId="endDate" styleClass="textBox textDate" maxlength="10" />
							<a NAME="CalendarObjectendDate" ID="CalendarObjectendDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectendDate','forms[1].endDate',document.forms[1].endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
							<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="endDate" width="24" height="17" border="0" align="absmiddle"></a>
						</td>
					</tr>
    </table>
    
    
   <!--  <table class="formContainer" align="left"  border="0" cellspacing="0" cellpadding="0">
    <tr>
    <td class="formLabel">
	<ul class="liBotMargin">

    <li class="liPad"><a href="#" onClick="javascript:onTatReport()">TAT Report</a></li>
    <li class="liPad"><a href="#" onClick="javascript:onProductivityReport()">Productivity Report</a></li>
    <li class="liPad"><a href="#" onClick="javascript:onPreapprovalDetailedReport()">Pre-approval Detailed Report</a></li>
    <li class="liPad"><a href="#" onClick="javascript:onPreapprovalActivityReport()">Pre-approval Activity  Report</a></li>	
    </ul>
    </td>
    </tr>
    </table> -->
    </fieldset>
    
    
    
    
    
    
    <table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">Report Type 
				<select name="reportType" class="selectBox" id="reporttype">
					<!-- <option value="PDF">PDF</option> -->
					<option value="EXL">EXCEL</option>
				</select> &nbsp;
				<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateProductivityReport();"><u>G</u>enerateReport</button>
				&nbsp;
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
			</td>
		</tr>
	</table>
    
    
    
    
    
    
    
    
    
    
<!-- E N D : AllReportsList -->
</div>

    <INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
	
	</html:form>
<!-- E N D : Content/Form Area -->