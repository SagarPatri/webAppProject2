<%
/** @ (#) onlineclinicianSearch.jsp June 29,2015
 * Project     : Project-X
 * File        : onlineclinicianSearch.jsp
 * Author      : Nagababu K
 * Company     : Vidal Health TPA Pvt. Ltd., 
 * Date Created: June 29,2015
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
<script type="text/javascript" src="/ttk/scripts/hospital/onlineclinicianSearch.js"></script>

<script>
bAction=false;
var TC_Disabled = true;
</script>
<%

%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/OnlinePreAuthClinicianAction.do">


		<div class="contentArea" id="contentArea">
			<!-- S T A R T : Content/Form Area -->
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td width="57%">List of Doctors</td>
    		<td width="43%" align="right" class="webBoard">&nbsp;</td>
  		</tr>
	</table>
	<!-- E N D : Page Title -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Search Box -->		
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td nowrap>License No. :<br>        
        <html:text property="sClinicianId" styleClass="textBox textBoxMedium"  name="frmOnlinePreAuth" />
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        <td nowrap> Name of Treating Doctors :<br>
          <html:text property="sClinicianName" styleClass="textBox textBoxLarge"  name="frmOnlinePreAuth"/>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          </td>
		<td valign="bottom" nowrap>
			<a href="#" accesskey="s" onClick="javascript:clinicianSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>
        <td width="100%">&nbsp;</td>
      </tr>
     </table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid  name="clinicianListData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	<tr>
        <td>
     	<ttk:PageLinks  name="clinicianListData"/>
     	</td>
     	</tr>
	</table>
	<br>
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
 	 <tr>
	    <td width="27%"> </td>
	    <td width="73%" nowrap align="right">
	    	    <button type="button" onclick="closeClinicians();" name="Button1" accesskey="c" class="olbtnSmall"><u>C</u>lose</button>
	    <%-- <logic:equal value="activityClinicianSearch" name="forwardMode">
	    <button type="button" onclick="closeClinicians();" name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
	    </logic:equal>
	    <logic:notEqual value="activityClinicianSearch" name="forwardMode">
	    <button type="button" onclick="goGeneral();" name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
	    </logic:notEqual> --%>
	    </td>
	    </tr>
	    </table>
	   <br>
	   <div style="color: blue;">
	   <p><strong>Note</strong> : Click on Clinican Id to select the Clinician.
	   </p>
	   </div> 
	   
	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT type="hidden" name="rownum" value="">
	<input type="hidden" name="child" value="">
	<INPUT type="hidden" name="mode" value="">
	<INPUT type="hidden" name="sortId" value="">
	<INPUT type="hidden" name="pageId" value="">
	<INPUT type="hidden" name="tab" value="">
	<INPUT type="hidden" name="reforward" value="">
	
	</div>
</html:form>
<!-- E N D : Content/Form Area -->
