<%
/**
 * @ (#) batchReconciliationSummary.jsp Nov 19 2015 
 * Project      : TTK HealthCare Services Dubai
 * File         : batchReconciliationSummary.jsp
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : Dec 1st 2015
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.common.security.Cache"%>
<head>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/providerLogin/batchReconciliationSummary.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
</head>
<html:form action="/OnlineFinanceAction.do">
		<div class="contentArea" id="contentArea">
			<!-- S T A R T : Content/Form Area -->

								<h4 class="sub_heading">Batch Reconciliation</h4>
								<html:errors />
								<div id="contentOuterSeparator"></div>
							</div>


									<table align="center" class="searchContainer" border="0"
										cellspacing="3" cellpadding="3">
										<tr>


											<td width="25%" nowrap>From date:<br> <html:text
													property="fromDate" name="frmOnlineFinanceDashBoard"
													styleClass="textBox textBoxMedium" maxlength="12" /> <a
												name="CalendarObjectempDate11" id="CalendarObjectempDate11"
												href="#"
												onClick="javascript:show_calendar('CalendarObjectempDate11','frmOnlineFinanceDashBoard.fromDate',document.frmOnlineFinanceDashBoard.fromDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"> <img
													src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
													name="empDate" width="24" height="17" border="0"
													align="absmiddle">
											</a>
											</td>

											<td width="25%" nowrap>To date:<br> <html:text
													property="toDate" name="frmOnlineFinanceDashBoard"
													styleClass="textBox textBoxMedium" maxlength="12" /> <a
												name="CalendarObjectempDate11" id="CalendarObjectempDate11"
												href="#"
												onClick="javascript:show_calendar('CalendarObjectempDate11','frmOnlineFinanceDashBoard.toDate',document.frmOnlineFinanceDashBoard.toDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"> <img
													src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
													name="toDate1" width="24" height="17" border="0"
													align="absmiddle">
											</a>
											</td>

											<td width="25%" nowrap>BatchNumber:<br> <html:text
													property="batchNumber" name="frmOnlineFinanceDashBoard"
													styleClass="textBoxWeblogin textBoxMediumWeblogin"
													styleId="search2" maxlength="60" />
											</td>

											<td width="25%" nowrap colspan="4" align="center"><br>
												<button type="button" name="Button2" accesskey="s"
													class="olbtnSmall" onClick="onSearch()">
													<u>S</u>earch
												</button>
											</td>
										</tr>
									</table>

									<br>
									<div class="span11" align="center">
										<!-- S T A R T : Grid -->
										<ttk:HtmlGrid name="tableData" className="table table-striped" />
										<!-- E N D : Grid -->
										<!-- S T A R T : Buttons and Page Counter -->
										<table align="center" class="buttonsContainerGrid" border="0"
											cellspacing="0" cellpadding="0">
											<ttk:PageLinks name="tableData" />
										</table>
								</div>


		<!--E N D : Content/Form Area -->

		<INPUT TYPE="hidden" NAME="mode" VALUE="">
		<INPUT TYPE="hidden" NAME="rownum" VALUE="">
		<INPUT TYPE="hidden" NAME="pageId" VALUE="">
		<INPUT TYPE="hidden" NAME="sortId" VALUE="">

</html:form>

