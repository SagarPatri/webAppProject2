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
	pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
	pageContext.setAttribute("claimspending", Cache.getCacheObject("claimspending"));
	pageContext.setAttribute("corporatename", Cache.getCacheObject("corporatename"));
	pageContext.setAttribute("providername", Cache.getCacheObject("providername"));
	pageContext.setAttribute("floatAccountlist", Cache.getCacheObject("floatAccountlist"));
	pageContext.setAttribute("partnerNameList", Cache.getCacheObject("partnerNameList"));
	pageContext.setAttribute("paymentTermDaysAgrList",Cache.getCacheObject("paymentTermDaysAgrList"));
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
    <table border="0" align="center" cellpadding="0" cellspacing="0" class="tablePad">
         <tr>
         <td width="7%" nowrap class="textLabelBold" align="center">Select Type </td>
         <td width="42%" align="center">
         	<html:select property="selectRptType" styleId="select" styleClass="selectBox selectBoxMedium" onchange="javascript:onReportType()" >
            	<html:options collection="claimspending" property="cacheId" labelProperty="cacheDesc"/>
            </html:select>
          </td>
          <td>&nbsp;</td><td>&nbsp;</td>
          <td>&nbsp;</td><td>&nbsp;</td>
         </tr>
    </table>
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
						<td class="formLabel">Search Based on  <span class="mandatorySymbol">*</span></td>
							   <td>
	         					<html:select property="searchBasedOn" styleId="select" styleClass="selectBox selectBoxMedium">
										<html:option value="">Select from list</html:option>
										<html:option value="ADD">Added Date</html:option>
										<html:option value="APR">Approved Date</html:option>
										<html:option value="REC">Received Date</html:option>
										<html:option value="ADM">Admission Date</html:option>
	         				   </html:select>
	       				   </td>
				</tr>		
				<tr>
						<td nowrap>Start Date  <span class="mandatorySymbol">*</span></td>
							<td>
									<html:text property="startDate"	styleId="startDate" styleClass="textBox textDate" maxlength="10" /> 
									<a NAME="CalendarObjectstartDate" ID="CalendarObjectstartDate" HREF="#"	onClick="javascript:show_calendar('CalendarObjectstartDate','forms[1].startDate',document.forms[1].startDate.value,'',event,148,178);return false;"	onMouseOver="window.status='Calendar';return true;"	onMouseOut="window.status='';return true;">
									<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="startDate" width="24" height="17" border="0" align="absmiddle"></a>
						</td>
						
					<td nowrap>End Date  </td>
						<td>
							<html:text property="endDate" styleId="endDate" styleClass="textBox textDate" maxlength="10" />
							<a NAME="CalendarObjectendDate" ID="CalendarObjectendDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectendDate','forms[1].endDate',document.forms[1].endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
							<img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="endDate" width="24" height="17" border="0" align="absmiddle"></a>
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
	         					<html:select property="providername" styleId="select" styleClass="selectBox selectBoxMedium" onchange="javascript:onChangeProvider();">
										<html:option value="">Select from list</html:option>	            				
			            				<html:options collection="providername" property="cacheId" labelProperty="cacheDesc"/>
	         				   </html:select>
	       				   </td>
				</tr>	
				<tr>
					<td class="formLabel">Claim Type </td>
							   <td>
	         							<html:select property="claimTypeID" styleId="select" styleClass="selectBox selectBoxMedium" onchange="javascript:onChangeClaimType();" >
	         								<html:option value="">Any</html:option>
		        							<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
            							</html:select>
	       				  		 </td>    				   
							<td class="formLabel">Finance Status  <span class="mandatorySymbol">*</span></td>
							   	<td>
	         						<html:select property="financeStatus" styleId="select" styleClass="selectBox selectBoxMedium">
	         							<html:option value="">Select from list</html:option>
	         							<html:option value="ALL">ALL(4 Months)</html:option>
	         							<html:option value="PENDING">Pending</html:option>
	            						<html:option value="READY_TO_BANK">Ready To Bank</html:option>
	            						<html:option value="SENT_TO_BANK">Approved by finance</html:option>
	            						<html:option value="PAID">Paid</html:option>
	         				  		 </html:select>
	       				 	  </td>
						</tr>
					
					<tr>
						<logic:equal value="CNH" name="frmReportList" property="claimTypeID">
							<td class="formLabel">Partner Name </td>
							<td>
		         				<html:select property="partnerName" styleId="select" styleClass="selectBox selectBoxMedium">
		         					<html:option value="">Select from list</html:option>
		         					<html:option value="0">ALL</html:option>
			        				<html:optionsCollection name="partnerNameList" label="cacheDesc" value="cacheId" />
	            				</html:select>
		       				</td>
	       				</logic:equal>	 
	       				<logic:notEqual value="CNH" name="frmReportList" property="claimTypeID">
							<td></td>
							<td></td>
	       				</logic:notEqual>
	       				 	<td class="formLabel">Ageing Band</td>
							<td>
		         				<html:select property="ageBand" styleId="select" styleClass="selectBox selectBoxMedium">
		         					<html:option value="">Select from list</html:option>
		         					<html:option value="ALL">ALL</html:option>
		         					<html:option value="ADAYS">0-15 Days</html:option>
		         					<html:option value="BDAYS">16-30 Days</html:option>
		         					<html:option value="CDAYS">31-45 Days</html:option>
		         					<html:option value="DDAYS">46-60 Days</html:option>
		         					<html:option value="EDAYS">60 Days & above</html:option>
	            				</html:select>
		       				</td>  				   
					</tr>
					<tr>
						<td class="formLabel">Payment Term Agreed(In Days)</td>
						<td>
							<%-- <html:text property="paymentTermAgr" styleId="paymentTermAgr" styleClass="textBox textBoxSmall" readonly="true"/>  --%>
							<html:select property="paymentTermAgr" styleId="paymentTermAgr" styleClass="selectBox selectBoxMedium">
								<html:option value="">Select from List</html:option>
								<html:optionsCollection name="paymentTermDaysAgrList" label="cacheDesc" value="cacheId" />
							</html:select>
							&nbsp;&nbsp;<a href="#" onClick="javascript:onActivityLog()">Activity Log </a>
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
					<option value="PDF">PDF</option>
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
