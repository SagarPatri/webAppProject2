
<%
	/**
	 * @ (#) provNotifications.jsp.jsp Nov 19 2015
	 * Project      : TTK HealthCare Services Dubai
	 * File         : provNotifications.jsp.jsp
	 * Author       : Kishor kumar S H
	 * Company      : RCS Technologies
	 * Date Created : Dec 9th 2015
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
function onProviderGuidelines()
{
	var openPage = "/OnlineProvHelpAction.do?mode=doDownloadHelpDocs&type=providerGuidelines";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}
function onHowToUse()
{
	var openPage = "/OnlineProvHelpAction.do?mode=doDownloadHelpDocs&type=howToUseForPL";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}
function onCirculars()
{
	var openPage = "/OnlineProvHelpAction.do?mode=doDownloadHelpDocs&type=circularsForPL";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}
function onClaimForm()
{
	var openPage = "/OnlineProvHelpAction.do?mode=doDownloadHelpDocs&type=claimFormForPL";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
}

function onCommonICDCodes()
{
	var openPage = "/OnlineProvHelpAction.do?mode=doCommonICDCodes";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);

}

</script>
</head>
<html:form action="/OnlineOverDueAction.do">
		<div class="contentArea" id="contentArea">
			<!-- S T A R T : Content/Form Area -->
								<h4 class="sub_heading">Help Documents</h4>
								<html:errors />
								<div id="contentOuterSeparator"></div>
		<table align="center" class="searchContainer" border="0" cellspacing="3" cellpadding="3">
			   <tr>
			<td>
				<ul  style="margin-bottom:0px; ">
			<li class="liPad"><a href="#" onclick="onProviderGuidelines()">Provider GuideLines</a></li>
			<li class="liPad"><a href="#" onclick="onHowToUse()">How to use Provider Login</a></li>
			<li class="liPad"><a href="#" onclick="onCirculars()">Circulars</a></li>
			<li class="liPad"><a href="#" onclick="onCommonICDCodes()">Common Icd Codes</a></li>
			<li class="liPad"><a href="#" onclick="onClaimForm()">Claim Form</a></li>
			
				</ul>
			</td>
			   </tr>
     
    	</table>
								</div>
		<!--E N D : Content/Form Area -->

		<INPUT TYPE="hidden" NAME="mode" VALUE="">
</html:form>

