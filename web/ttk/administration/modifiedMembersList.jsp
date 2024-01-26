
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.TTKCommon" %>
<script language="javascript" src="/ttk/scripts/utils.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/administration/modifiedMembersList.js"></script>

<!-- S T A R T : Content/Form Area -->
<html:form action="/ModifiedMembersAction" >
<!-- S T A R T : Page Title -->
  <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td>List of Modified Members </td>
      <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
    </tr>
  </table>
<!-- E N D : Page Title -->

<div class="contentArea" id="contentArea"><br>
<html:errors/>


<!-- S T A R T : Grid -->
  <ttk:HtmlGrid name="tableData"/>
<!-- E N D : Grid -->

 <!-- S T A R T : Buttons -->
<table  align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
  <ttk:PageLinks name="tableData"/>
</table>
</div>
  <input type="hidden" name="child" value="">
  <INPUT TYPE="hidden" NAME="rownum" VALUE="">
  <INPUT TYPE="hidden" NAME="mode" VALUE="">
  <INPUT TYPE="hidden" NAME="sortId" VALUE="">
  <INPUT TYPE="hidden" NAME="pageId" VALUE="">
</html:form>
<!-- E N D : Buttons -->



