<%
/** @ (#) chequelist.jsp 12th June 2006
 * Project     	: TTK Healthcare Services
 * File        	: chequelist.jsp
 * Author      	: Harsha Vardhan B N
 * Company     	: Span Systems Corporation
 * Date Created	: 12th June 2006
 *
 * @author 		 : Harsha Vardhan B N
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.security.Cache,com.ttk.common.TTKCommon" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/finance/alertchequelist.js"></script>
<SCRIPT LANGUAGE="JavaScript">
var TC_Disabled = true;
</script>
<%
	pageContext.setAttribute("InsCompanyID",Cache.getCacheObject("insuranceCompany"));
	pageContext.setAttribute("claimType",Cache.getCacheObject("claimType"));
	pageContext.setAttribute("chequeStatus",Cache.getCacheObject("chequeStatus"));
	pageContext.setAttribute("paymentMethod", Cache.getCacheObject("paymentMethod"));
%>

<!-- E N D : Tab Navigation -->
	<!-- S T A R T : Content/Form Area -->
	<html:form action="/ChequeSearchAction.do" method="post" >
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  		<tr>
        	<td>List of Cheques</td>
  		</tr>
	</table>
<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
      	<td nowrap>Claim No.:<br>
            <html:text property="claimNO" styleId="sclaimnoid" styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
        
        <td nowrap>Claim Settlement No.:<br>
            <html:text property="sClaimSettleNumber" styleId="sclaimsettlenumber" styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
	    <%if(TTKCommon.isAuthorized(request,"Search")) {%>
	    <td valign="bottom" nowrap><a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>
	  <%}%>
	  </tr>
      
      
    </table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<%-- <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="27%"></td>
  	    <ttk:PageLinks name="tableData"/>
  	  </tr>
	</table> --%>
</div>
	<!-- E N D : Buttons and Page Counter -->
	<!-- E N D : Content/Form Area -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
<!-- E N D : Main Container Table -->