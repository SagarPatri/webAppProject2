<%@page import="com.ttk.dto.onlineforms.providerLogin.BatchListVO"%>
<%@page import="java.util.ArrayList"%>
<%
	/**
	 * @ (#) batchReconcilInvoiceDetails.jsp.jsp Nov 27 2015 
	 * Project      : TTK HealthCare Services Dubai
	 * File         : batchReconcilInvoiceDetails.jsp.jsp
	 * Author       : Kishor kumar S H
	 * Company      : RCS Technologies
	 * Date Created : Nov 27 2015
	 *
	 * @author       :
	 * Modified by   :
	 * Modified date :
	 * Reason        :
	 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page
	import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.common.security.Cache"%>
<head>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="/ttk/scripts/onlineforms/providerLogin/batchReconcilInvoiceDetails.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
</head>
<html:form action="/OnlineFinanceAction.do">
	<%
		ArrayList<BatchListVO> alBatchInvDtls = (ArrayList<BatchListVO>) request
					.getSession().getAttribute("alBatchInvDtls");
	%>
		<div class="contentArea" id="contentArea">
								<h4 class="sub_heading">Batch Reconciliation</h4>
								<html:errors />
								<div id="contentOuterSeparator"></div>


									<table style="border-radius: 10px; width: 75%; height: auto;"
										class="table table-striped" align="center" cellspacing="3"
										cellpadding="3">

										<tr>
											<td align="left" width="10%"><strong> Received
													Date </strong></td>
											<td align="left" width="20%"><strong>Invoice
													Number </strong></td>
											<td align="left" width="20%"><strong>Batch
													Number </strong></td>
											<td align="left" width="10%"><strong>Treatment
													Date </strong></td>
											<td align="left" width="20%"><strong>Patient
													Name </strong></td>
											<td align="left" width="10%"><strong>Total Net
													Claimed</strong></td>
											<td align="left" width="10%"><strong>Total
													Amount Approved</strong></td>
										</tr>
										
										
										<!-- Iterate Account Details for particular Hospital -->
										<logic:iterate id="batchListVO" name="alBatchInvDtls"
											indexId="i">
											<tr
												class="<%=(i.intValue() % 2) == 0 ? "gridOddRow"
							: "gridEvenRow"%>">
												<td><bean:write name="batchListVO"
														property="receivedDate" /></td>
												<td><bean:write name="batchListVO" property="invNo" /></td>
												<td><bean:write name="batchListVO"
														property="batchNumber" /></td>
												<td><bean:write name="batchListVO"
														property="treatmentDate" /></td>
												<td><bean:write name="batchListVO"
														property="patientName" /></td>
												<td><bean:write name="batchListVO"
														property="ttlNetClmd" /></td>
												<td><bean:write name="batchListVO"
														property="ttlAmtApprd" /></td>
											</tr>
										</logic:iterate>
									</table>


									<!-- S T A R T : Buttons -->
									<table align="center" class="buttonsContainer" border="0"
										cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" align="center">
												<button type="button" name="Button" accesskey="d"
													class="olbtnSmall" onClick="javascript:onDownload();">
													<u>D</u>ownload
												</button>&nbsp;
												<button type="button" name="Button2" accesskey="b"
													class="olbtnSmall" onClick="javascript:onBack();">
													<u>B</u>ack
												</button>&nbsp;
											</td>
										</tr>
									</table>
									<!-- E N D : Buttons -->

								</div>


		<!--E N D : Content/Form Area -->
		<INPUT TYPE="hidden" NAME="mode" VALUE="">
		<INPUT TYPE="hidden" NAME="batchSeqId" VALUE="<%= request.getAttribute("batchSeqId")%>">
		<INPUT TYPE="hidden" NAME="flag" VALUE="<%= request.getAttribute("flag")%>">
		<INPUT TYPE="hidden" NAME="empnlNo" VALUE="<%= request.getAttribute("empnlNo")%>">
</html:form>

