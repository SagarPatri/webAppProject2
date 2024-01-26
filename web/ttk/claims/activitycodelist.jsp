<%
/** @ (#) activitycodelist.jsp June 18,2015
 * Project     : Project-X
 * File        : activitycodelist.jsp
 * Author      : Nagababu K
 * Company     : Vidal Health TPA Pvt. Ltd., 
 * Date Created: June 18,2015
 *
 * @author 		 : Nagababu K
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/claims/activitycodelist.js"></script>

<script>
bAction=false;
var TC_Disabled = true;


</script>
<SCRIPT LANGUAGE="JavaScript">
var JS_SecondSubmit=false;
</SCRIPT>
<%

%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/ClaimActivityListAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td width="57%">List of Activity Codes</td>
    		<td width="43%" align="right" class="webBoard">&nbsp;</td>
  		</tr>
	</table>
	<!-- E N D : Page Title -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Search Box -->		
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
      <td nowrap>Search Type:<br>        
        <html:select property="sSearchType" styleClass="selectBox selectBoxMedium"  name="frmActivitiesList" onchange="changeSearchType(this)">
         <html:option value="TAR">Tariff</html:option>
         <html:option value="ACT">Master</html:option>
        </html:select>
        </td> 
        <td nowrap>Activity Code:<br>        
        <html:text property="sActivityCode" styleClass="textBox textBoxMedium"  name="frmActivitiesList" />
        </td>
        <td nowrap>Activity Description:<br>
          <html:text property="sActivityCodeDesc" styleClass="textBox textBoxLarge"  name="frmActivitiesList"/></td>
        <td nowrap id="internalCode" >Internal Code:<br>    
        <html:text property="sInternalCode" styleClass="textBox textBoxMedium"  name="frmActivitiesList" />
        </td>  
		<td valign="bottom" nowrap>
			<a href="#" accesskey="s" onClick="javascript:activityCodeSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>
        <td width="100%">&nbsp;</td>
      </tr>
     </table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid  name="activityCodeListData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	<tr>
        <td>
     	<ttk:PageLinks  name="activityCodeListData"/>
     	</td>
     	</tr>
	</table>
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
 	 <tr>
	    <td width="27%"> </td>
	    <td width="73%" nowrap align="right">
	    <button type="button" onclick="closeActivityCodeList();" name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
	    </td>
	    </tr>
	    </table>
	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT type="hidden" name="rownum" value="">
	<input type="hidden" name="child" value="">
	<INPUT type="hidden" name="mode" value="">
	<INPUT type="hidden" name="sortId" value="">
	<INPUT type="hidden" name="pageId" value="">
	<INPUT type="hidden" name="tab" value="">
	<html:hidden property="sAuthType" name="frmActivitiesList" value="CLM"/>
	<script type="text/javascript">
changeSearchType(document.forms[1].sSearchType);
	</script>
</html:form>
<!-- E N D : Content/Form Area -->
