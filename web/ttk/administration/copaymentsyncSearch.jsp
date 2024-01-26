<%
/** @ (#) productsynclist.jsp Aug 3rd, 2007
 * Project     : TTK Healthcare Services
 * File        : productsynclist.jsp
 * Author      : Raghavendra T M
 * Company     : Span Systems Corporation
 * Date Created: Aug 3rd, 2007
 *
 * @author 		 : Raghavendra T M
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/administration/copaymentsyncSearch.js"></script>
<SCRIPT LANGUAGE="JavaScript">
bAction = false; //to avoid change in web board in product list screen
var TC_Disabled = true;
</SCRIPT>
<%
pageContext.setAttribute("listTTKBranch",Cache.getCacheObject("officeInfo"));
%>
<!-- S T A R T : Content/Form Area -->
	<html:form action="/CopaymentAction.do" >
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
      <tr>
         <td>List of Providers - [ <bean:write name="frmCopayment" property="caption"/> ] </td>
      </tr>
    </table>
<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">

<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
		   	<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			    	<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
			    	<bean:message name="updated" scope="request"/>
			    </td>
			 	</tr>
			</table>
		</logic:notEmpty>
	<!-- E N D : Success Box -->
<html:errors/>
<!-- S T A R T : Search Box -->
<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td nowrap>Empanelment No.:<br>
            <html:text property="sEmpanelmentNo" styleClass="textBox textBoxMedium" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
        </td>
		<td nowrap>Provider Name:<br>
			<html:text property="sProviderName" styleClass="textBox textBoxLarge" maxlength="250" onkeypress="javascript:blockEnterkey(event.srcElement);"/>
	    </td>
		<td nowrap>Alkoot Branch:<br>
	       	<html:select property="sAlkootBranch" styleClass="selectBox selectBoxMedium" >
				<html:option value="">Any</html:option>
				<html:optionsCollection name="listTTKBranch" label="cacheDesc" value="cacheId"/>
			</html:select>
	    </td>

        <td width="100%" valign="bottom">
        	<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
        </td>
	</tr>
</table>
<!-- E N D : Search Box -->

<!-- S T A R T : Grid -->
<ttk:HtmlGrid name="tableDataSync" />
<!-- E N D : Grid -->

<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" nowrap>&nbsp;
	    </td>
	    <td align="right" nowrap>
	    	<%
	    		if(TTKCommon.isDataFound(request,"tableDataSync") && TTKCommon.isAuthorized(request,"Add"))
	    		{
	    	%>
		    		<button type="button" name="Button" accesskey="n" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSynchronizeAll();">Synchronize <u>A</u>ll</button>&nbsp;
		    		<!--denial process-->
		    		<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSynchronize();">Sy<u>n</u>chronize</button>&nbsp;
					<!--denial process-->
		    <%
	    		}//end of if(TTKCommon.isDataFound(request,"tableDataSync") && TTKCommon.isAuthorized(request,"Add"))
	     	%>
	    			<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>&nbsp;
	    </td>
	  </tr>
  		<ttk:PageLinks name="tableDataSync"/>
	</table>
	</div>
<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	</html:form>
<!-- E N D : Content/Form Area -->