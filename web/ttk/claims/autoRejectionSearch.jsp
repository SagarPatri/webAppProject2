<%
/** @ (#) autoRejectionSearch.jsp
 *
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/claims/autoRejectionSearch.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<script>
bAction=false;
var TC_Disabled = true;
var JS_SecondSubmit=false;

</script>
<%
pageContext.setAttribute("claimsSubmissionTypes", Cache.getCacheObject("claimsSubmissionTypes"));
pageContext.setAttribute("ProviderList", Cache.getCacheObject("providerNames"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/ClaimAutoRejectionAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">Error Log Search
			<a href="#" onClick="onConfiguration()">
					<img src="/ttk/images/EditIcon.gif" title="Configuration List" alt="Configuration List" width="16"	height="16" border="0" align="absmiddle">
				</a>
			</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
	<tr class="searchContainerWithTab">
	
			<td nowrap>Batch Reference Number:<br>
            	<html:text property="iBatchRefNO"  styleClass="textBox textBoxLarge"  maxlength="60" onkeyup="isNumaricOnly(this)"/>
     		</td>
            <td nowrap>Batch No:<br>
            	<html:text property="batchNO"  styleClass="textBox textBoxLarge"  maxlength="60"/>
     		</td>
     		
     		 <td nowrap>Member ID:<br>
            	<html:text property="memberID"  styleClass="textBox textBoxLarge"  maxlength="60" />
     		</td>
     		
     		<td nowrap>Parent Claim:<br>
            	<html:text property="parentClaimNo"  styleClass="textBox textBoxLarge"  maxlength="60"  />
     		</td>
     		
     		
     	    	<td nowrap>Action: <br>
            	<html:select  property="claimAction" styleClass="selectBox selectBoxMoreMedium" styleId="claimAction">
				    <html:option value="">Select from list</html:option> 
					<html:option value="REAA">Required Action</html:option>
					<html:option value="REJA">Rejected</html:option>
					<html:option value="ALL">All</html:option>
			</html:select>
     		</td>
     		
     		 
     		
     		
     		
     </tr>
        <tr> 
     			 <td nowrap>Provider Name:<br>
                <html:select property="providerName" name="frmAutiRejectionLogList" onchange="setProviderID()" styleClass="selectBox selectBoxMoreMedium">
                    <html:option value="">Any</html:option>
                    <html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
              </html:select>
             </td>
            
           <td nowrap>Provider ID:<br>
            	<html:text property="providerId"  styleClass="textBox textBoxLarge"  maxlength="60" readonly="true" disabled="true"/>
     		</td>
		    
		     <td nowrap>From Date:<br>
	            <html:text property="formDate"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].formDate',document.forms[1].formDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
            
            <td nowrap>To Date:<br>
	            <html:text property="toDate"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].toDate',document.forms[1].toDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    
		        <td nowrap>Claim age band: <br>
            	<html:select  property="ClaimAgeBand" styleClass="selectBox selectBoxMoreMedium" styleId="ClaimAgeBand">
					<html:option value="">Select from list</html:option>
					<html:option value="0 to 5 days">0 to 5 days</html:option>
					<html:option value="6 to 14 days">6 to 14 days</html:option>
					<html:option value="15 to 20 days">15 to 20 days</html:option>
					<html:option value="20 days">>20 days</html:option>
			</html:select>
     		</td>
		    
		    
		    	       
        <td nowrap><br>
        <a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>   
        	</td>
        	</tr>  	
	</table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
			<ttk:HtmlGrid name="tableData"/>
			<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
  <!-- 	<tr>
    	<td>&nbsp;</td>
    	<td width="73%" nowrap align="right">
    		<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onClose()"><u>C</u>lose</button>
    	</td>
  	</tr> -->
      <ttk:PageLinks name="tableData"/>
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