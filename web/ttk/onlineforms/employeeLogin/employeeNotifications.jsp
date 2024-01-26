
<%
	/**
	 * @ (#) ptnrNotifications.jsp
	 * Project      : TTK HealthCare Services Dubai
	 * File         : ptnrNotifications.jsp
	 * Author       :
	 * Company      : 
	 * Date Created : 
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
<%@ page
	import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.common.security.Cache"%>
<head>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript">
function testM1()
{
	var openPage = "/OnlinePtnrHelpAction.do?mode=doDefaultNotify1";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}
function testM2()
{
	var openPage = "/OnlinePtnrHelpAction.do?mode=doDefaultNotify2";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}
function testM3()
{
	var openPage = "/OnlinePtnrHelpAction.do?mode=doDefaultNotify3";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}

function onEmplClose()
{
	document.forms[1].mode.value="doCloseEmplClaim";
   	document.forms[1].action="/EmployeeClaimSubmissionAction.do";
   	document.forms[1].submit();

}
</script>
<style type="text/css">
.tob_class {
	background-color: rgb(0, 26, 102);
	font-weight: bold;
    width: 87px;
    height: 34px;
    margin-left: auto;
    margin-right: auto;
    margin: auto;
    position: relative;
    left: 50%;
	color: #fff;
}
</style>
</head>
<html:form action="/PartnerOnlineOverDueAction.do">
		<div class="contentArea" id="contentArea">
			<!-- S T A R T : Content/Form Area -->
								
								<html:errors />
								<div id="contentOuterSeparator"></div>
		<table align="center" class="searchContainer" border="0" cellspacing="3" cellpadding="3">
			   <tr>
			<td>
				<ul  style="margin-bottom:0px; ">
			
			<li class="liPad"><a href="#" onclick="testM2()">General Claim Form</a></li>
			<li class="liPad"><a href="#" onclick="testM3()">Dental Claim Form</a></li>
			
				</ul>
			</td>
			   </tr>
     
    	</table>
    	<!-- <input type="button" class="tob_class" value="Close"
			onClick="javascript:onEmplClose()" /> -->
			<div class="query_div_class">
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	     <button type="button" name="Button" accesskey="c" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onEmplClose();"><u>C</u>lose</button>&nbsp;
	    </td>
	  </tr>
	</table>
	</div>
								</div>
		<!--E N D : Content/Form Area -->

		<INPUT TYPE="hidden" NAME="mode" VALUE="">
</html:form>

