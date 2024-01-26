
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/fraudcase/campaignlist.js"></SCRIPT> 
 <script>
var JS_SecondSubmit=false;
</script> 
<%
	pageContext.setAttribute("CFDProviderList",Cache.getCacheObject("CFDProviderList"));	
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CFDCampaignSearchAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">CFD Campaign</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
      <td nowrap>Campaign Name:<br>
            <html:text property="campaignName" styleId="campaignName" styleClass="textBox textBoxMedLarge" name="frmFraudCampaign"/>
        </td>
       <td nowrap>Provider Name:<br>
		    <html:select property="cfdProviderSeqId" styleId="cfdProviderSeqId" name="frmFraudCampaign"  styleClass="selectBox selectBoxLarge">
		    	 <html:option value="">Any</html:option>
  				<html:optionsCollection name="CFDProviderList" label="cacheDesc" value="cacheId" />
		    </html:select>
        </td>
      
        <td nowrap>Campaign Received From:<br>
      			<html:text property="campaignReceivedFrom" styleId="campaignReceivedFrom" name="frmFraudCampaign" styleClass="textBox textDate" maxlength="10"/>
      			<A NAME="campaignReceivedFrom" ID="campaignReceivedFrom" HREF="#" onClick="javascript:show_calendar('campaignReceivedFrom','forms[1].campaignReceivedFrom',document.forms[1].campaignReceivedFrom.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
      	</td> 
      	
      	 <td nowrap>Campaign Received To:<br>
      			<html:text property="campaignReceivedTo" styleId="campaignReceivedTo" name="frmFraudCampaign" styleClass="textBox textDate" maxlength="10"/>
      			<A NAME="campaignReceivedTo" ID="campaignReceivedTo" HREF="#" onClick="javascript:show_calendar('campaignReceivedTo','forms[1].campaignReceivedTo',document.forms[1].campaignReceivedTo.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
      	</td> 
      </tr>
      
      <tr>
        <td nowrap>Campaign Status:<br>
	        <html:select property="campaignStatus" styleId="campaignStatus" styleClass="selectBox selectBoxMedium" name="frmFraudCampaign">
  				<html:option value="OPEN">Open</html:option>
				<html:option value="CLOSED">Closed</html:option>
	    	</html:select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    	<a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
		</td>
      </tr>
     </table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
    	<tr>
    		<td width="27%"></td>
        	<td width="73%" align="right">
        		<button type="button" name="Button" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onAdd()"><u>A</u>dd</button>&nbsp;&nbsp;
        		<%-- <%
        		if(TTKCommon.isDataFound(request,"tableData")){
	    		%>
        			<button type="button" name="Button" accesskey="c" class="buttonsCopyWB" onMouseout="this.className='buttonsCopyWB'" onMouseover="this.className='buttonsCopyWB buttonsCopyWBHover'" onClick="javascript:copyToWebBoard()"><u>C</u>opy to Web Board</button>
        		<%
        		 }
        		%> --%>
        	</td>
      	</tr>
      	<ttk:PageLinks name="tableData"/>
	</table>
	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<%-- <html:hidden property="campaignDtlSeqId" name="frmFraudCampaign" /> --%>
	
</html:form>
	<!-- E N D : Content/Form Area -->