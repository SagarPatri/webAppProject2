
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<!-- <script language="javascript" src="/ttk/scripts/administration/policylist.js"></script> -->
<script language="javascript" src="/ttk/scripts/administration/policySyncList.js"></script>
<SCRIPT LANGUAGE="JavaScript">
bAction = false; //to avoid change in web board in product list screen
var TC_Disabled = true;
</SCRIPT>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/policyListSyncAction.do" >
	<!-- S T A R T : Page Title -->
	<%-- <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
      <tr>
         <td>List of Policy - [ <bean:write name="frmProductList" property="companyName"/> ] [ <bean:write name="frmProductList" property="productName"/> ]</td>
      </tr>
    </table> --%>
<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">

<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
		   	<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			    	<td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
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
		<td nowrap>Enrollment Id :<br>
            <html:text property="enrollmentId" styleClass="textBox textBoxMedium" maxlength="250" />
        </td>
    
        <td width="100%" valign="bottom">
        	<a href="#" accesskey="s" onClick="javascript:onSearchPolicySynk();"   class="search"><img src="/ttk/images/SearchIcon.gif" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
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
	    		if(TTKCommon.isDataFound(request,"tableDataSync"))
	    		{
	    	%>
		    		<button type="button" name="Button" accesskey="n" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSynchronize();">Sy<u>n</u>chronize</button>&nbsp;
		    <%
	    		}
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
	
	<html:hidden property="policySeqID" />
	
	</html:form>
<!-- E N D : Content/Form Area -->