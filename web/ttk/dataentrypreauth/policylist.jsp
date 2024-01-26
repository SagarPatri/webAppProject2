<%
/**
 * @ (#) policylist.jsp 6th May 2006
 * Project      : TTK HealthCare Services
 * File         : policylist.jsp
 * Author       : Krupa J
 * Company      : Span Systems Corporation
 * Date Created : 6th May 2006
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/preauth/policylist.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var TC_Disabled = true;
</SCRIPT>
<%
	pageContext.setAttribute("listInsuranceCompany",Cache.getCacheObject("insuranceCompany"));
%>

<html:form action="/PolicyListAction.do" >
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="57%">Select Policy </td>
    <td width="43%" align="right" class="webBoard">&nbsp;</td>
  </tr>
</table>
	<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">
	<html:errors/>
	<!-- S T A R T : Search Box -->
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
      	<td nowrap><br>
           <html:select property="insuranceSeqID" styleClass="selectBox selectBoxMedium" >
				<html:option value="">Any</html:option>
				<html:optionsCollection name="listInsuranceCompany" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>

        <td nowrap>Policy No.:<br>
            <html:text property="policyNo" styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>

        <td nowrap>Corp. Name:<br>
            <html:text property="corporateName" styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
        <td valign="bottom" nowrap width="100%">
	        <a href="#" class="search" accesskey="s" onClick="onSearch()" ><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
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
    	<td>&nbsp;</td>
    	<td width="73%" nowrap align="right">
    		<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onClose()"><u>C</u>lose</button>
    	</td>
  	</tr>
      <ttk:PageLinks name="tableData"/>
    </table>
</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<input type="hidden" name="child" value="PolicyList">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	</html:form>
	<!-- E N D : Content/Form Area -->

