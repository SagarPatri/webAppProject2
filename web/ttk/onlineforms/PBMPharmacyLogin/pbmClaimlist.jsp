<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<head>
<style type="text/css">

table#textTable{
margin-left: 20px;
width: 500px;
display: inline;
}
table#textTable,table#textTable tr td{
border: 1px solid white;
background-color: #F0F0F0;
border-collapse: collapse;
}

table#textTable tr td{
text-align:left;
font-size: 18px;
padding: 20px;
}
#hexagonLinks{
display: inline;
float: right;
list-style: none;
}
#onlineTableTD{
text-align: center;
font-size: 14px;
}
#onlineSearchEnterTable{
border: none;
padding: 0;
}
</style>
<script type="text/javascript" src="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/onlineforms/PBMPharmacyLogin/pbmClaimlist.js"></script>
</head>
<%
pageContext.setAttribute("sStatus", Cache.getCacheObject("preauthStatus"));

%>
<div class="contentArea" id="contentArea">
<html:form action="/PbmClaimAction.do" >
<html:errors/>
<div id="sideHeadingMedium">Claim Log Search</div>
<br><br>
<div align="center" style="border: 1px solid gray;border-radius: 20px;padding: 10px 20px 10px 20px;background-color: #F8F8F8;width: 85%;">
<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="1" id="onlineSearchEnterTable">
  <tr style="margin-bottom: 20PX;"><td nowrap>Prescription Date</td>
  <td></td>
  <td nowrap>Claim Submission Date</td>
  <td></td>
  <td></td>
  </tr>
	 <tr style="margin-bottom: 20PX;">
	 <td nowrap>From date:<br>
	
  <html:text name="frmPBMClaimLog" property="trtmtFromDate" maxlength="10" styleClass="textBox textDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].trtmtFromDate',document.forms[1].trtmtFromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="trtmtFromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>To date:<br>
  <html:text name="frmPBMClaimLog" property="trtmtToDate" maxlength="10" styleClass="textBox textDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].trtmtToDate',document.forms[1].trtmtToDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="trtmtToDate" width="24" height="17" border="0"
												align="absmiddle"></a></td>
	 
	 <td nowrap>From date:<br>
	
  <html:text name="frmPBMClaimLog" property="clmFromDate" maxlength="10" styleClass="textBox textDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmFromDate',document.forms[1].clmFromDate.value,'',event,148,178);return false;"
												
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="clmFromDate" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td>
 
  <td nowrap>To date:<br>
  <html:text name="frmPBMClaimLog" property="clmToDate" maxlength="10" styleClass="textBox textDate"/>
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','forms[1].clmToDate',document.forms[1].clmToDate.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="clmToDate" width="24" height="17" border="0"
												align="absmiddle"></a></td>
	   
	 </tr>
	 <tr style="margin-bottom: 20PX;">
	 
	 <td nowrap>Patient name:<br>
	  <html:text property="patientName" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	<td nowrap>Authorization No.:<br>
	  <html:text property="authNo" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  <td nowrap>Invoice No.:<br>
	  <html:text property="invoiceNumber" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  <td nowrap>Al Koot Id:<br>
	  <html:text property="alKootId" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>
	  </td>
	  </tr>
	  <tr style="margin-bottom: 20PX;">
	  <td nowrap>Claim No.<br>
	  	 <html:text property="claimNumber" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>  
	  </td>
	  
	  <td nowrap>Claim Payment Status:<br>
	   <html:select property="clmPayStatus" name="frmPBMClaimLog" styleClass="selectBox selectBoxMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:option value="PAID">PAID</html:option>
		        	<html:option value="PENDING">PENDING</html:option>
                    <html:option value="READY_TO_BANK">READY_TO_BANK</html:option>
                    <html:option value="SENT_TO_BANK">SENT_TO_BANK</html:option>
            	</html:select></td>
       <td nowrap>Status:<br>
		   <html:select property="status" name="frmPBMClaimLog" styleClass="selectBox selectBoxMedium" onchange="javascript:onStatusChanged()">
			  	 		<html:option value="">Any</html:option>
			  	 		<html:option value="INP">In-Progress</html:option>
			        	<html:option value="APR">Approved</html:option>
	                    <html:option value="REJ">Rejected</html:option>
	                     <html:option value="REQ">Required Information</html:option>
	                     
	       </html:select>
       </td>
       
        <logic:equal value="INP" name="frmPBMClaimLog" property="status">
				<td nowrap>In-Progress Status:<br>
         			<html:select property ="inProgressStatus" styleClass="selectBox selectBoxMedium">
	           			<html:option value="">Any</html:option>
	           			<html:option  value="FRH">Fresh</html:option>
						<html:option  value="ENH">Resubmission</html:option>
						<html:option  value="RES">Shortfall Responded</html:option>
       				</html:select>
       	   		</td>	
		</logic:equal>
		
            <td nowrap>Event Reference No.:<br>
	  	 <html:text property="eventRefNo" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>  
	  	 </td>
	  	<tr>
	  	<td nowrap>Batch No.:<br>
	  	 <html:text property="batchNo" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>
	  	  </td>  
       
       
         <td nowrap>Qatar ID:<br>
	  	 <html:text property="qatarId" name="frmPBMClaimLog" styleClass="textBox textBoxMedLarge"/>  
	  </td>
       
       <td>
        <img title="Search" src="/ttk/images/SearchIcon.gif" height="18" width="16">
       <a href="#">
	   <button class="searchSbtn" accesskey="s"  onclick="return onSearch();"><u>S</u>earch</button>	 
	   </a>  
       </td>
       
       
       
       
       	 
	 <!--  <td><img title="Search" src="/ttk/images/SearchIcon.gif" height="18" width="16"></td>
	  <td id="onlineTableTD"><a title="Proceed" id="imgAncSmall" href="#" onclick="javascript:onSearch();"><u>S</u>earch</a></td>
	   -->
	 </tr>	
	</table>	
</div>
<!-- <br>
	<table align="center">
	<tr>
	<td><img title="Back" src="/ttk/images/broker/small_hexagon.png" height="48" width="46"></td>
	<td><a id="imgAncMedium" onclick="onBack('Home');" href="#" title="Back">Back</a></td>
	</tr>
	</table>
<br> -->
<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table  align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	<tr>
     	<td><ttk:PageLinks name="tableData"/></td>
     	</tr>     	
	</table>
<input type="hidden" name="mode" value="">
<input type="hidden" name="rownum" value="">
<input type="hidden" name="sortId" value="">
<input type="hidden" name="pageId" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="backID" value="">
<input type="hidden" name="tab" value="">
<INPUT TYPE="hidden" NAME="loginType" VALUE="PBM">
</html:form>
</div>