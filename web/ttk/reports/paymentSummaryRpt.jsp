<%
/** @ (#) paymentSummaryRpt.jsp May 10th 2019
 * Project     : TTK Healthcare Services
 * File        : paymentSummaryRpt.jsp
 * Author      : Deepthi Meesala
 * Company     :RCS
 * Date Created: May 10th 2019
 *
 * @author 		 : Deepthi Meesala
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
<script language="javascript" src="/ttk/scripts/reports/paymentSummaryRpt.js"></script>
<%
	String strSubLink=TTKCommon.getActiveSubLink(request);
	pageContext.setAttribute("strSubLink",strSubLink);
	pageContext.setAttribute("listInsuranceCompany",Cache.getCacheObject("insuranceCompany"));
	pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
	pageContext.setAttribute("claimspending", Cache.getCacheObject("claimspending"));
	pageContext.setAttribute("corporatename", Cache.getCacheObject("corporatenameSummary"));
	pageContext.setAttribute("providername", Cache.getCacheObject("providername"));
	pageContext.setAttribute("floatAccountlist", Cache.getCacheObject("floatAccountlist"));
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
  <%--   <table border="0" align="center" cellpadding="0" cellspacing="0" class="tablePad">
         <tr>
         <td width="13%" nowrap class="textLabelBold" align="center">Select Type </td>
         <td width="32%" align="center">
         	<html:select property="selectRptType" styleId="select" styleClass="selectBox selectBoxMedium" onchange="javascript:onReportType()" >
            	<html:options collection="claimspending" property="cacheId" labelProperty="cacheDesc"/>
            </html:select>
          </td>
          <td>&nbsp;</td><td>&nbsp;</td>
          <td>&nbsp;</td><td>&nbsp;</td>
         </tr>
    </table> --%>
	<fieldset>
		<legend>Report Parameters </legend>
			<table border="0" align="center" cellpadding="0" cellspacing="0" class="formContainer">
			
			<tr>
					
					<td width="22%" class="formLabel">Float Account No. <span class="mandatorySymbol">*</span></td>
						<td>
								<html:select property="floatAccNo" styleId="select" styleClass="selectBox selectBoxMedium">
										<html:option value="">Select from list</html:option>	            				
			            				<html:options collection="floatAccountlist" property="cacheId" labelProperty="cacheDesc"/>
	         				   	</html:select>
	         			</td>
					
					
					<td class="formLabel">Search Based on  </td>
							   <td>
	         					<html:select property="searchBasedOn" styleId="select" styleClass="selectBox selectBoxMedium" onchange="onChangeSearchBasedOn()">
										<html:option value="any">Any</html:option>
										<html:option value="ADD">Added Date</html:option>
										<html:option value="APR">Approved Date</html:option>
										<html:option value="REC">Received Date</html:option>
										<html:option value="ADM">Admission Date</html:option>
	         				   </html:select>
	       				   </td>
					
				</tr>

				<logic:notEqual name="frmReportList" property="searchBasedOn" value="any">
				<tr>
					<td nowrap> Start Date <span class="mandatorySymbol">*</span> </td>
					<td>
						<html:text property="apprStartDate"	styleClass="textBox textDate" maxlength="10" /> 
						<a NAME="CalendarObjectstartDate" ID="CalendarObjectstartDate" HREF="#"	onClick="javascript:show_calendar('CalendarObjectstartDate','forms[1].apprStartDate',document.forms[1].apprStartDate.value,'',event,148,178);return false;"	onMouseOver="window.status='Calendar';return true;"	onMouseOut="window.status='';return true;">
						<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="startDate" width="24" height="17" border="0" align="absmiddle"></a>
					</td>
					<td nowrap> End Date  </td>
					<td>
						<html:text property="apprEndDate" styleClass="textBox textDate" maxlength="10" />
						<a NAME="CalendarObjectendDate" ID="CalendarObjectendDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectendDate','forms[1].apprEndDate',document.forms[1].apprEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
						<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="endDate" width="24" height="17" border="0" align="absmiddle"></a>
					</td>
				</tr>
				</logic:notEqual>

	<tr>
					<td class="formLabel">Claim Type </td>
							   <td>
	         							<html:select property="claimTypeID" styleId="select" styleClass="selectBox selectBoxMedium" onchange="onChangeClaimType()">
	         								<html:option value="any">Any</html:option>
		        							<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
            							</html:select>
	       				  		 </td>  

<logic:equal name="frmReportList" property="claimTypeID" value="CTM">
			<td class="formLabel">Corporate Name </td>
							 <td>
	         					<html:select property="corporatename" styleId="select" styleClass="selectBox selectBoxMedium">
										<html:option value="">Select from list</html:option>	            				
			            				<html:options collection="corporatename" property="cacheId" labelProperty="cacheDesc"/>
	         				   </html:select>
	       				   </td>

</logic:equal>

</tr>


<logic:equal name="frmReportList" property="claimTypeID" value="CNH">
<tr>
	       				   
							<td class="formLabel">Provider Name </td>
							   <td>
							   
							   <logic:empty name="frmReportList" property="partnerName">
							   <html:select property="providername" styleId="select" styleClass="selectBox selectBoxMedium" onchange="onChangeProviderOrPartner()">
										<html:option value="">Select from list</html:option>	            				
			            				<html:options collection="providername" property="cacheId" labelProperty="cacheDesc"/>
	         				   </html:select>
							   </logic:empty>
	         					<logic:notEmpty name="frmReportList" property="partnerName" >
	         					 <html:select property="providername" styleId="select" styleClass="selectBox selectBoxMedium" onchange="onChangeProviderOrPartner()"  readonly="true" disabled="true"  >
										<html:option value="">Select from list</html:option>	            				
			            				<html:options collection="providername" property="cacheId" labelProperty="cacheDesc"/>
	         				   </html:select>
	         					
	         					</logic:notEmpty>
	       				   </td>
	       				   
	       				   
	       				   
					<td class="formLabel">Partner Name </td>
							 <td>
							 
							 <logic:empty name="frmReportList" property="providername">
							 	<html:select property="partnerName" styleId="select" styleClass="selectBox selectBoxMedium" onchange="onChangeProviderOrPartner()">
										 <logic:notEmpty name="partnersList" scope="session">
									<html:option value="">Select from list</html:option>
									<html:optionsCollection name="partnersList" value="key" label="value" />
								</logic:notEmpty>
	         				   </html:select>
							 
							 </logic:empty>
							 <logic:notEmpty name="frmReportList" property="providername" >
							 <html:select property="partnerName" styleId="select" styleClass="selectBox selectBoxMedium" onchange="onChangeProviderOrPartner()" readonly="true" disabled="true" >
										 <logic:notEmpty name="partnersList" scope="session">
									<html:option value="">Select from list</html:option>
									<html:optionsCollection name="partnersList" value="key" label="value" />
								</logic:notEmpty>
	         				   </html:select>
							 
	         				</logic:notEmpty>
	       				   </td>
	       				   
				</tr>	
				</logic:equal>
				
			</table>
	</fieldset>
	<!-- E N D : Form Fields -->
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">Report Type 
				<select name="reportType" class="selectBox" id="reporttype">
					<!-- <option value="PDF">PDF</option> -->
					<option value="EXL">EXCEL</option>
				</select> &nbsp;
				<button type="button" name="Button" accesskey="g" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateReport();"><u>G</u>enerateReport</button>
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
