<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<head>
<style type="text/css">

table#textTable{
margin-left: 20px;
width: 500px;
display: inline;
}
table#textTable,table#textTable tr td{
border: 1px solid white;
background-color: #F0F0F0;
border-collapse: collapse;
}

table#textTable tr td{
text-align:left;
font-size: 18px;
padding: 20px;
}
#hexagonLinks{
display: inline;
float: right;
list-style: none;
}
#onlineTableTD{
text-align: center;
font-size: 14px;
}
#onlineSearchEnterTable{
border: none;
padding: 0;
}
</style>
<script type="text/javascript" src="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/onlinereports/pbm/pbmReporstList.js"></script>
</head>
<%
pageContext.setAttribute("sStatus", Cache.getCacheObject("preauthStatus"));
%>
<html:form action="/PbmReportsAction.do" >
<html:errors/>
<div id="sideHeadingMedium">Summary Reports</div>
<br><br>
<div  style="border: 1px solid gray; border-radius: 5px;padding: 10px 20px 10px 20px;background-color: #F8F8F8;width: 75%;height:200PX;">
<table>
<!-- <tr>
<td>
<a style="color:black;" href="#" onClick="javascript:onSelectLink('clmDispenseId');"><b>Claim wise dispense status report</b> </a>
</td>
<td></td>
<td></td>
<td></td>
</tr>
<tr>
<td colspan="4">&nbsp;</td></tr>
<tr>
<td >
<a style="color:black;" href="#" onClick="javascript:onSelectLink('drugDispenseId');"><b>Drug wise dispense status report</b> </a>
</td>
<td></td>
<td></td>
<td></td>
</tr> -->
<tr>
<td colspan="4">&nbsp;</td></tr>

<tr>
<td>
<!-- <a style="color:black;" href="#" onClick="javascript:onSelectLink('clmSubmissionId');"><b>Claim wise submission report</b> </a> -->
<a style="color:black;" href="#" onClick="javascript:onSelectLink('claimSummary');"><b>Claim Status Summary Report</b> </a>
</td>
<td></td>
<td></td>
<td></td>

</tr>
<tr>
<td colspan="4">&nbsp;</td></tr>
<tr>
<td>
<!-- <a style="color:black;" href="#" onClick="javascript:onSelectLink('drugSubmissionId');"><b>Drug wise claim submission report</b> </a> -->
<a style="color:black;" href="#" onClick="javascript:onSelectLink('claimDetailed');"><b>Claim Status Detailed Report</b> </a>
</td>
<td></td>
<td></td>
<td></td>

</tr>
<tr>
<td colspan="4">&nbsp;</td></tr>
<!-- <tr>
<td>
<a style="color:black;" href="#" onClick="javascript:onSelectLink('clmPaymentId');"><b>Claim wise payment report </b></a>
</td>
<td></td>
<td></td>
<td></td>
</tr>
<tr>
<td colspan="4">&nbsp;</td></tr>
<tr>
<td>
<a style="color:black;" href="#" onClick="javascript:onSelectLink('drugPaymentId');"><b>Drug wise payment report</b> </a>
</td>
<td></td>
<td></td>
<td></td>
</tr> -->
</table>



</div>
<input type="hidden" name="mode" value="">
<input type="hidden" name="rownum" value="">
<input type="hidden" name="sortId" value="">
<input type="hidden" name="pageId" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="backID" value="">
<input type="hidden" name="tab" value="">
<INPUT TYPE="hidden" NAME="loginType" VALUE="PBM">
<INPUT TYPE="hidden" NAME="reportId" VALUE="">

</html:form>
