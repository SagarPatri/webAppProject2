<%
/** @ (#) claimspendingrptparams.jsp July 4, 2006
 * Project     : TTK Healthcare Services
 * File        : claimspendingrptparams.jsp
 * Author      : Balakrishna Erram
 * Company     : Span Systems Corporation
 * Date Created: Feb 2, 2006
 *
 * @author 		 : Balakrishna Erram
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/reports/claimspendingrptparams.js"></script>
<%
	String strSubLink=TTKCommon.getActiveSubLink(request);
	pageContext.setAttribute("strSubLink",strSubLink);
	
	pageContext.setAttribute("claimspending", Cache.getCacheObject("claimspending"));
	pageContext.setAttribute("corporatename", Cache.getCacheObject("corporatename"));
	pageContext.setAttribute("providername", Cache.getCacheObject("providername"));
	pageContext.setAttribute("sStatus", Cache.getCacheObject("preauthStatus"));
	
%>
<html:form action="/ClaimsPendingReportsAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><%=TTKCommon.getActiveSubLink(request)%> Reports - <bean:write name="frmReportList" property="reportName" /></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Form Fields -->
	<fieldset>
		<legend>Report Parameters </legend>
			<table border="0" align="center" cellpadding="0" cellspacing="0" class="formContainer">	
						
				<tr>
						<td nowrap>Start Date  <span class="mandatorySymbol">*</span></td>
							<td>
									<html:text property="startDate"	styleId="startDate" styleClass="textBox textDate" maxlength="10" onblur="dateFormatValidation('startDate');"/> 
									<a NAME="CalendarObjectstartDate" ID="CalendarObjectstartDate" HREF="#"	onClick="javascript:show_calendar('CalendarObjectstartDate','forms[1].startDate',document.forms[1].startDate.value,'',event,148,178);return false;"	onMouseOver="window.status='Calendar';return true;"	onMouseOut="window.status='';return true;">
									<img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="startDate" width="24" height="17" border="0" align="absmiddle"></a>
						</td>
						
					<td nowrap>End Date  </td>
						<td>
							<html:text property="endDate" styleId="endDate" styleClass="textBox textDate" maxlength="10" onblur="dateFormatValidation('endDate');"/>
							<a NAME="CalendarObjectendDate" ID="CalendarObjectendDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectendDate','forms[1].endDate',document.forms[1].endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
							<img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="endDate" width="24" height="17" border="0" align="absmiddle"></a>
						</td>
				</tr>
				<tr>
					<td class="formLabel">Corporate Name </td>
							 <td>
	         					<html:select property="corporatename" styleId="select" styleClass="selectBox selectBoxMedium">
										<html:option value="">Select from list</html:option>	            				
			            				<html:options collection="corporatename" property="cacheId" labelProperty="cacheDesc"/>
	         				   </html:select>
	       				   </td>
	       				   
							<td class="formLabel">Provider Name </td>
							   <td>
	         					<html:select property="providername" styleId="select" styleClass="selectBox selectBoxMedium">
										<html:option value="">Select from list</html:option>	            				
			            				<html:options collection="providername" property="cacheId" labelProperty="cacheDesc"/>
	         				   </html:select>
	       				   </td>
				</tr>	
				<tr>
					    				   
							<td class="formLabel">Pre-approval Status</td>
							   	<td>
	         						<html:select property="preauthStatus" styleId="select" styleClass="selectBox selectBoxMedium">
	         							<html:option value="">All</html:option>
	         							<html:options collection="sStatus" property="cacheId" labelProperty="cacheDesc"/>
	         				  		 </html:select>
	         				  		 
	       				 	  </td>
	       				 	  
	       				 	  <td class="formLabel">Search Based On  <span class="mandatorySymbol">*</span></td>
							   	<td>
	         						<html:select property="preauthSearchBased" styleId="select" styleClass="selectBox selectBoxMedium">
	         							<html:option value="">Select From List</html:option>
	         				  		    <html:option value="ADD">Added Date</html:option>
	         				  		    <html:option value="APR">Approved Date</html:option>
	         				  		    <html:option value="REC">Received Date</html:option>
	         				  		    <html:option value="ADM">Admission Date</html:option>
	         				  		 </html:select>
	         				  		 
	       				 	  </td>
						</tr>
					
					
			</table>
	</fieldset>
	<!-- E N D : Form Fields -->
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">Report Type 
				<select name="reportType" class="selectBox" id="reporttype">
					<option value="EXL">EXCEL</option>
					<!-- <option value="PDF">PDF</option> -->
				</select> &nbsp;
				<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateFinPreAuthReport();"><u>G</u>enerateReport</button>
				&nbsp;
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
			</td>
		</tr>
	</table>
	<!-- End of Reports List -->
	</div>	
	<html:hidden property="reportName"/>
	<input type="hidden" id="reportID" value="<bean:write name='frmReportList' property='reportID' />"/>
	<input type="hidden" name="mode" value="" />
</html:form>
