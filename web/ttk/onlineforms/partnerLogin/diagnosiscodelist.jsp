<%
/** @ (#) diagnosiscodelist.jsp June 20,2015
 * Project     : Project-X
 * File        : diagnosiscodelist.jsp
 * Author      : Nagababu K
 * Company     : Vidal Health TPA Pvt. Ltd., 
 * Date Created: June 20,2015
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
<%@ page
	import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper"%>

<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/partner/diagnosiscodelist.js"></script>
<script language="javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusObj"))%>";
</script>
<script>
bAction=false;
var TC_Disabled = true;
var JS_SecondSubmit=false;

</script>

<%

%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/PartnerClaimDiagnosisSearchAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td width="57%">List of Diagnosis Codes</td>
    		<td width="43%" align="right" class="webBoard">&nbsp;</td>
  		</tr>
	</table>
	<!-- E N D : Page Title -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Search Box -->		
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td nowrap>Code:<br>   
        <html:text property="icdCode" styleClass="textBox textBoxMedium" maxlength="10" name="frmOnlineClaimSubmission" />
        </td>
        <td nowrap>Description:<br>
          <html:text property="diagnosisDescription" styleClass="textBox textBoxLarge" maxlength="60" name="frmOnlineClaimSubmission"/></td>
		<td valign="bottom" nowrap>
			<a href="#" accesskey="s" onClick="javascript:diagnosisCodeSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>
        <td width="100%">&nbsp;</td>
      </tr>
     </table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid  name="diagnosisCodeListData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	<tr>
        <td>
     	<ttk:PageLinks  name="diagnosisCodeListData"/>
     	</td>
     	</tr>
	</table>
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
 	 <tr>
	    <td width="27%"> </td>
	    <td width="73%" nowrap align="right">
	    <button type="button" onclick="closeDiagnosis();" name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
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
	<INPUT type="hidden" name="reforward" value="">
	<input type="hidden" name="focusObj" value="<%= request.getAttribute("focusObj")%>">
</html:form>
<!-- E N D : Content/Form Area -->
