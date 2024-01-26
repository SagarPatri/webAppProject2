<%
/**
 * @ (#) collectionsSearch.jsp March 05 2019
 * Project      : TTK HealthCare Services
 * File         : collectionsSearch.jsp
 * Author       : Deepthi Meesala
 * Company      : RCS
 * Date Created : March 05 2019
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<script language="JavaScript" SRC="/ttk/scripts/validation.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/finance/collectionsSearch..js"></script>


<html:form action="/CreditNoteAction.do" >

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="57%">List of Credit Notes</td>
	   <td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
	  </tr>
	</table>
<div class="contentArea" id="contentArea">
<html:errors/>

<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
<tr>
 <td nowrap>Group Name:<br>
	     	 	<html:text property="groupName" styleClass="textBox textBoxMedium" maxlength="60"/>
	     	</td>


 <td nowrap>Group ID:<br>
	     	 	<html:text property="groupId" styleClass="textBox textBoxMedium" maxlength="60"/>
	     	</td>
	     	
	     	
	 <td nowrap>Policy No.:<br>
	     	 	<html:text property="policyNum" styleClass="textBox textBoxMedium" maxlength="60"/>
	     	</td>
	     	
	     	
	     	
	     	     	
 <td nowrap>Policy Status:<br>
	     	 	<html:text property="policyStatus" styleClass="textBox textBoxMedium" maxlength="60"/>
	     	</td>
	     	
	     	
	  <td>
	  &nbsp;&nbsp;<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
	  </td>   	
	     	
</tr>
</table>


	<ttk:HtmlGrid name="tableData"/>


	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		<%--  <tr>
		    <td width="27%"></td>
		    <td width="73%" align="right">
		    <%
			   if(TTKCommon.isDataFound(request,"tableData"))
			   {
		    %>
			   	 <button type="button" name="Button" accesskey="c" class="buttonsCopyWB" onMouseout="this.className='buttonsCopyWB'" onMouseover="this.className='buttonsCopyWB buttonsCopyWBHover'" onClick="javascript:copyToWebBoard();"><u>C</u>opy to Web Board</button>&nbsp;
			<%
				} // end of if(TTKCommon.isDataFound(request,"tableData"))
			%>

	        </td>
	  	</tr> --%>
	  			<ttk:PageLinks name="tableData"/>

	</table>

	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">





</div>
</html:form>