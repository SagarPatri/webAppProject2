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
<script language="javascript" src="/ttk/scripts/finance/chequelist.js"></script>
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
      	<td nowrap>Payment Trans Ref no.:<br>
            <html:text property="sChequeNumber"  styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
        <td nowrap>Float Account No.:<br>
		    <html:text property="sFloatAccountNumber"  styleClass="textBox textBoxMedium" maxlength="60"/>
	    </td>
	    <td nowrap>Status:<br>
	        <html:select property="sStatus"  styleClass="selectBox selectBoxMedium" >
	        	<html:option value="">Any</html:option>
  				<html:options collection="chequeStatus"  property="cacheId" labelProperty="cacheDesc"/>
	    	</html:select>
   	 	</td>
   	 	<td nowrap>Start Date:<br>
      		<html:text property="sStartDate" styleClass="textBox textDate" maxlength="10"/>
            <A NAME="CalendarObjectchqDate11" ID="CalendarObjectchqDate11" HREF="#" onClick="javascript:show_calendar('CalendarObjectchqDate11','forms[1].sStartDate',document.forms[1].sStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="sStartDate" width="24" height="17" border="0" align="absmiddle"></a>&nbsp;
		</td>
		<td nowrap>End Date:<br>
	  		<html:text property="sEndDate" styleClass="textBox textDate" maxlength="10"/>
            <A NAME="CalendarObjectchqDate12" ID="CalendarObjectchqDate12" HREF="#" onClick="javascript:show_calendar('CalendarObjectchqDate12','forms[1].sEndDate',document.forms[1].sEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="sEndDate" width="24" height="17" border="0" align="absmiddle"></a>&nbsp;
	 	 </td>
	  </tr>
      <tr>
		<td nowrap>Claim Settlement No.:<br>
            <html:text property="sClaimSettleNumber"  styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
		<td nowrap>Claim Type:<br>
	        <html:select property="sClaimType"  styleClass="selectBox selectBoxMedium" >
	        <html:option value="">Any</html:option>
  			<html:options collection="claimType"  property="cacheId" labelProperty="cacheDesc"/>
	    	</html:select>
		</td>
		
		
		 <td nowrap>AlKoot Id:<br>
		    <html:text property="sEnrollmentId"  styleClass="textBox textBoxMedium" maxlength="60"/>
	    </td>
		
		
		
		
		<td nowrap><br>
	        <html:select property="sInsuranceCompany"  styleClass="selectBox selectBoxMedium" >
  			<html:options collection="InsCompanyID"  property="cacheId" labelProperty="cacheDesc"/>
	    	</html:select>
		</td>
		<td nowrap>Insurance Code:<br>
            <html:text property="sCompanyCode"  styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
        <!--<td valign="bottom" nowrap><a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>-->
        <td nowrap>&nbsp;</td>
        <td nowrap>&nbsp;</td>
      </tr>
      <tr>
      	<td nowrap>Batch No.:<br>
            <html:text property="sBatchNumber"  styleClass="textBox textBoxMedium" maxlength="60"/>
        </td>
        <td nowrap>Policy No.:<br>
		    <html:text property="sPolicyNumber"  styleClass="textBox textBoxMedium" maxlength="60"/>
	    </td>
	    
	    
	    
      <td nowrap>Qatar ID:<br>
		    	<html:text property="sQatarId"  styleClass="textBox textBoxMedium" maxlength="60" />
		    </td>	
	    
	    
	    
	    <td nowrap>Payment Method:<br>
	        <html:select property="sPaymentMethod"  styleClass="selectBox selectBoxMedium" >
	        	<html:option value="">Any</html:option>
  				<html:options collection="paymentMethod"  property="cacheId" labelProperty="cacheDesc"/>
	    	</html:select>
   	 	</td>
   	 	<td valign="bottom" nowrap><a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a></td>
   	 	<td nowrap>&nbsp;</td>
        <td nowrap>&nbsp;</td>
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
  	    <ttk:PageLinks name="tableData"/>
  	  </tr>
	</table>
</div>




<div align="center">
	<table align="center">
		<tr>
			<td colspan="4">&nbsp;</td>
     		<td colspan="4">&nbsp;</td>
     		<td colspan="4">&nbsp;</td>
 			<td colspan="4">&nbsp;</td>
  				<!-- <logic:equal property="switchType" name="frmClaimDetailedReports" value="QPR"> -->
	  				<td colspan="4"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onExportToExcel();" type="button"><u>E</u>xport to Excel</button> </td>
 				<!-- </logic:equal> -->
	</table>
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