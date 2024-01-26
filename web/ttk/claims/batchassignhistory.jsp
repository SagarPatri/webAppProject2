<%
/** @ (#) batchlist.jsp 30th Sep 2020
 * Project     : ProjectX
 * File        : batchassignhistory.jsp
 * Author      : Vishwabandhu  Kumar
 * Company     : Vidal Health Care
 * Date Created: 30th Sep 2020
 *
 * @author 		 : Vishwabandhu Kumar
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.security.Cache,com.ttk.common.TTKCommon,java.util.Date,java.text.SimpleDateFormat" %>
<script type="text/javascript" src="/ttk/scripts/validation.js" type="text/javascript"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/claims/batchlist.js"></script>
<SCRIPT type="text/javascript">
bAction=false;
var TC_Disabled = true; //to avoid the alert message on change of form elements
var JS_SecondSubmit=false;

</SCRIPT>
<%
	boolean viewmode=true;
	if((TTKCommon.isAuthorized(request,"Add")) || (TTKCommon.isAuthorized(request,"Delete")))
	{
		viewmode=false;
	}
	pageContext.setAttribute("viewmode",new Boolean(viewmode));
	pageContext.setAttribute("ProviderList",Cache.getCacheObject("ProviderList"));
	pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
	pageContext.setAttribute("modeOfClaims", Cache.getCacheObject("modeOfClaims"));
	pageContext.setAttribute("claimsSubmissionTypes", Cache.getCacheObject("claimsSubmissionTypes"));
	pageContext.setAttribute("submissionCatagory", Cache.getCacheObject("submissionCatagory"));
	pageContext.setAttribute("sBatchAssigned", Cache.getCacheObject("sBatchAssigned"));
	java.util.Date dt = new java.util.Date();
	int iDate=0;
	int iMonth=0;
	int iYear=0;    
	java.text.SimpleDateFormat sdt;
	sdt = new java.text.SimpleDateFormat("dd/MM/yyyy");
	String strDate = sdt.format(dt);
	String str[] = strDate.split("/");
	for(int i=0; i<str.length; i++)
	{
		iDate =Integer.parseInt(str[0]);
		iMonth =Integer.parseInt(str[1]);
		iYear =Integer.parseInt(str[2]);
	}
	dt = new Date(iYear-1900,iMonth-1,iDate-5);	
	String strStartDate = sdt.format(dt);
%>

<!-- E N D : Tab Navigation -->
	<!-- S T A R T : Content/Form Area -->
	<html:form action="/BatchlistAction.do" method="post" >
	<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td width="57%">List of Batch Entries</td>
    		<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
  		</tr>
	</table>
	<!-- E N D : Page Title -->
<html:errors/>
	<!-- S T A R T : Search Box -->
	
	<div class="contentArea" id="contentArea">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
      		<td nowrap>Batch No.:<br>
            	<html:text property="sBatchNumber"  styleClass="textBox textBoxMedLarge" maxlength="60" name="frmClaimBatchHistoryList"/>
        	</td>
       		<td nowrap>Provider Name:<br>
		    	<html:select property="sProviderName" name="frmClaimBatchHistoryList"  styleClass="selectBox selectBoxLarge">
		    	 	<html:option value="">Any</html:option>
  					<html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
		    	</html:select>
        	</td>
       		<td nowrap>Claim Type:<br>
	            <html:select property="sClaimType" styleClass="selectBox selectBoxMedium" name="frmClaimBatchHistoryList">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
          	<td nowrap>Mode Of Claim:<br>
	            <html:select property="sModeOfClaim" styleClass="selectBox selectBoxMedium" name="frmClaimBatchHistoryList">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="modeOfClaims" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
      </tr>
      
      
      <tr>
        	<td nowrap>Assign Claim Status:<br>
	        	<html:select property="sAssignClaimStatus"  styleClass="selectBox selectBoxMedium" name="frmClaimBatchHistoryList">
	        		<html:option value="">Select From List</html:option>
  					<html:option value="ALL">All</html:option>
  					<html:option value="INP">In Progress</html:option>
  					<html:option value="REQ">Required Information</html:option>
  					<html:option value="APR">Approved</html:option>
  					<html:option value="REJ">Rejected</html:option>
  					<html:option value="PCO">Closed</html:option>
	    		</html:select>
			</td>
			
			<td nowrap>Claims Submission Type:<br>
				      <html:select name="frmClaimBatchHistoryList" property="sSubmissionType" styleClass="selectBox selectBoxMedium">
				            <html:option value="">Select From List</html:option>
							<html:optionsCollection name="claimsSubmissionTypes" label="cacheDesc" value="cacheId" />
					  </html:select>
			 </td>
			 
			 <td>Batch Received From:<br>
			  <html:text property="sBatchReceivedFrom" name="frmClaimBatchHistoryList" styleClass="textBox textDate" maxlength="10"/><A NAME="sBatchReceivedFrom" ID="sBatchReceivedFrom" HREF="#" onClick="javascript:show_calendar('sBatchReceivedFrom','forms[1].sBatchReceivedFrom',document.forms[1].sBatchReceivedFrom.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
      		</td>

			<td nowrap>Batch Received To:<br>
      			<html:text property="sBatchReceivedTo" name="frmClaimBatchHistoryList" styleClass="textBox textDate" maxlength="10"/><A NAME="sBatchReceivedTo" ID="sBatchReceivedTo" HREF="#" onClick="javascript:show_calendar('sBatchReceivedTo','forms[1].sBatchReceivedTo',document.forms[1].sBatchReceivedTo.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
      	 		&nbsp;&nbsp;&nbsp; 
      		</td>
      	
      </tr>
      
      
		<tr>
        	<td nowrap>Assigned To:<br>
	        	<html:select property="sAssignedTo"  styleClass="selectBox selectBoxMedium" name="frmClaimBatchHistoryList">
  					 <html:option value="">Select From List</html:option>
					<html:optionsCollection name="sBatchAssigned" label="cacheDesc" value="cacheId" />
	    		</html:select>
			</td>
			<td nowrap>Assigned BY:<br>
				      <html:select name="frmClaimBatchHistoryList" property="sAssignedBY" styleClass="selectBox selectBoxMedium">
				            <html:option value="">Select From List</html:option>
							<html:optionsCollection name="sBatchAssigned" label="cacheDesc" value="cacheId" />
					  </html:select>
			</td>
			<td> Assigned From  Date:<br>
					<html:text property="sAssignedFromDate" name="frmClaimBatchHistoryList" styleClass="textBox textDate" maxlength="10"/><A NAME="sAssignedFromDate" ID="sAssignedFromDate" HREF="#" onClick="javascript:show_calendar('sAssignedFromDate','forms[1].sAssignedFromDate',document.forms[1].sAssignedFromDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a></td>

			<td nowrap>Assigned To  Date:<br>
					<html:text property="sAssignedToDate" name="frmClaimBatchHistoryList" styleClass="textBox textDate" maxlength="10"/><A NAME="sAssignedToDate" ID="sAssignedToDate" HREF="#" onClick="javascript:show_calendar('sAssignedToDate','forms[1].sAssignedToDate',document.forms[1].sAssignedToDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>&nbsp;&nbsp;&nbsp; </td>
      </tr>
      
      
      <tr>
     	 <td>Submission Type:<br>
				      <html:select name="frmClaimBatchHistoryList" property="sProcessType" styleClass="selectBox selectBoxMedium">
				            <html:option value="">Select From List</html:option>
							<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheId" />
					  </html:select>
					  <a href="#" accesskey="s" onClick="javascript:onHistorySearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
					  
			     </td>
      
      </tr>
      
      
      
     </table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
	
	<ttk:HtmlGrid name="tableData" />
	<!-- E N D : Grid -->
<!-- E N D : Grid -->
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td width="100%" align="center">
    			<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBack();"><u>B</u>ack</button>&nbsp;
  		</td>
  	</tr>
</table>
</div>
	<!-- E N D : Buttons and Page Counter -->

	<!-- E N D : Content/Form Area -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
    <INPUT TYPE="hidden" NAME="sublink" VALUE="">
    <INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">

</html:form>
<!-- E N D : Main Container Table -->