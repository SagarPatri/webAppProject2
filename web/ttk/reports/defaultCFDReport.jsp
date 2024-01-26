<%
/** @ (#) policylist.jsp July 24, 2020
 * Project     			: TTK Healthcare Services
 * File        				: defaultCFDReport.jsp
 * Author      		: Vishwabandhu Kumar
 * Company     	: Vidal Health Services
 * Date Created	: July 24, 2020
 *
 * @author 		 : Vishwabandhu Kunar
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import=" com.ttk.common.TTKCommon" %>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/reports/cfdReport.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<style>
FIELDSET {
	width: 97%;
	border: 1px solid #CCCCCC;
	padding-top: 0px;
	padding-bottom: 10px;
}
.contentArea {
	overflow: scroll;
	overflow-x: hidden;
	width: 99%;
	padding: 0px 0px 0px 7px;
	scrollbar-face-color: #bad2f4;
	scrollbar-shadow-color: #f0f2f8;
	scrollbar-darkshadow-color: #8290a2;
	scrollbar-highlight-color: #f0f2f8;
	scrollbar-3dlight-color: #ced3d8;
	scrollbar-track-color: #f0f2f8;
	scrollbar-arrow-color: #022665;
	padding-top: 5px;
}
FORM {
	margin: 0px;
}
BODY {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	padding: 0px;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	color: #000000;
}
.mainContainerTable {
	border-top: 1px solid #FFFFFF;
}
FIELDSET LEGEND {
	color: #041a68;
	font-weight: bold;
	padding: 5px;
}
.formContainer {
	color: #000000;
	margin-top: 0px;
	padding: 3px;
	width: 98%;
	text-align: left;
}
.liPad {
	padding-bottom: 10px;
}
LI A {
	color: #0c48a2;
	text-decoration: none;
}
A:hover {
    cursor: hand;
    color: #cd0b3d;
    text-decoration: none;
}

</style>

<SCRIPT LANGUAGE="JavaScript">
bAction = false; //to avoid change in web board in product list screen
var TC_Disabled = true;

</SCRIPT>
<html:form action="/CFDReportAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><%=TTKCommon.getActiveSubLink(request)%></td>
  </tr>
</table>
<!-- End of: Page Title -->
<div class="contentArea" id="contentArea">
<html:errors/>
<!-- Start of Reports List -->
		<FIELDSET class="diagnosis_fieldset_class">
			<LEGEND>CFD Report</LEGEND>
			<TABLE class="formContainer" cellSpacing="0" cellPadding="0" align="center" border="0">
				<TBODY>
					<TR>
						<TD>
						<!-- onMouseout="this.className='liPad'"
						onMouseover="this.className='buttonsHover'" -->
							<UL style="MARGIN-BOTTOM: 0px;">
								<LI class="liPad"><A onclick="javascript:onDefaultGenerateReport();">Generate Report</A>
								<LI class="liPad"><A onclick="javascript:onCFDResultUpload();">CFD Investigation Upload</A></LI>
							</UL>
			</TABLE>
		</FIELDSET>
		<!-- End of Reports List -->
</div>

<input type="hidden" name="mode" value=""/>
</html:form>