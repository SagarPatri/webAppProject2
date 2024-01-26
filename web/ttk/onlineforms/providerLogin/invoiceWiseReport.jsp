<%@page import="java.util.ArrayList"%>
<%
	/**
	 * @ (#) chequeWiseReport.jsp Nov 27 2015 
	 * Project      : TTK HealthCare Services Dubai
	 * File         : chequeWiseReport.jsp
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
	SRC="/ttk/scripts/onlineforms/providerLogin/chequewisereport.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
</head>
<html:form action="/OnlineFinanceAction.do">
	<%
		ArrayList<String> strChequeDetails = (ArrayList<String>) request.getSession().getAttribute("strChequeDetails");
	%>
		<div class="contentArea" id="contentArea">
			<!-- S T A R T : Content/Form Area -->

								<h4 class="sub_heading">Invoice-Wise Report</h4>
								<html:errors />
								<div id="contentOuterSeparator"></div>

									<!-- <table style="border-radius: 10px; width: 75%; height: auto;"
										class="table table-striped" align="center" cellspacing="3"
										cellpadding="3"> -->
										<table style="width: 75%; height: auto;" align="center"
											class="searchContainer" border="0" cellspacing="3" cellpadding="3">
										<tr>

											<td width="25%" nowrap align="left">Invoice Number:<br>
												<html:text property="chequeNumber"
													name="frmOnlineFinanceDashBoard"
													styleClass="textBoxWeblogin textBoxMediumWeblogin"
													styleId="search2" maxlength="60" />
											</td>

											<td width="25%" nowrap colspan="4" align="center">
												<button type="button" name="Button2" accesskey="s"
													class="olbtnSmall" onClick="onSearchInv()"> 
													<u>S</u>earch
												</button>
											</td>

										</tr>
									</table>

									<br>

									<table style="border-radius: 10px; width: 75%; height: auto;"
										class="table table-striped" align="center" cellspacing="3"
										cellpadding="3">

										<tr>
											<td align="left" width="50%">Invoice Number :</td>
											<td align="left" width="50%">${strChequeDetails[0]}</td>
										</tr>
										<tr>
											<td align="left">Invoice Amount :</td>
											<td align="left">${strChequeDetails[1]}</td>
										</tr>
										<tr>
											<td align="left">Invoice Date :</td>
											<td align="left">${strChequeDetails[2]}</td>
										</tr>
										<tr>
											<td align="left">Batch Name :</td>
											<td align="left">${strChequeDetails[3]}</td>
										</tr>
										<tr>
											<td align="left">Batch Number :</td>
											<td align="left">${strChequeDetails[4]}</td>
										</tr>
										<tr>
											<td align="left">Patient Name :</td>
											<td align="left">${strChequeDetails[5]}</td>
										</tr>
										<tr>
											<td align="left">Treatment Date :</td>
											<td align="left">${strChequeDetails[6]}</td>
										</tr>
										<tr>
											<td align="left">Claim Received Date :</td>
											<td align="left">${strChequeDetails[7]}</td>
										</tr>
										<tr>
											<td align="left">Total Amount Claimed:</td>
											<td align="left">${strChequeDetails[8]}</td>
										</tr>
										<tr>
											<td align="left">Discount :</td>
											<td align="left">${strChequeDetails[9]}</td>
										</tr>
										<tr>
											<td align="left">Deductible :</td>
											<td align="left">${strChequeDetails[10]}</td>
										</tr>
										<tr>
											<td align="left">Co-Payment :</td>
											<td align="left">${strChequeDetails[11]}</td>
										</tr>
										<tr>
											<td align="left">Net Claimed Amount :</td>
											<td align="left">${strChequeDetails[12]}</td>
										</tr>
										<tr>
											<td align="left">Total Amount Approved :</td>
											<td align="left">${strChequeDetails[13]}</td>
										</tr>
										<tr>
											<td align="left">Reason for difference in Amount :</td>
											<td align="left">${strChequeDetails[14]}</td>
										</tr>
										<tr>
											<td align="left">Status :</td>
											<td align="left">${strChequeDetails[15]}</td>
										</tr>
										<tr>
											<td align="left">Cheque Number:</td>
											<td align="left">${strChequeDetails[16]}</td>
										</tr>
										<tr>
											<td align="left">Cheque Date :</td>
											<td align="left">${strChequeDetails[17]}</td>
										</tr>
									</table>


									<!-- S T A R T : Buttons -->
									<table align="center" class="buttonsContainer" border="0"
										cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" align="center">
												<button type="button" name="Button" accesskey="d"
													class="olbtnSmall" onClick="javascript:onDownloadInvoice();">
													<u>D</u>ownload
												</button>&nbsp; <!-- 					<button type="button" name="Button2" accesskey="p" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onPrint();"><u>P</u>rint</button>&nbsp; -->
											</td>
										</tr>
									</table>
									<!-- E N D : Buttons -->

								</div>

		<!--E N D : Content/Form Area -->
		<INPUT TYPE="hidden" NAME="mode" VALUE="">
</html:form>

