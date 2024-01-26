
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import = "java.io.*,java.util.*" %>
<%@ page import = "javax.servlet.*,java.text.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/support/claimsBulkUploadCount.js"></script>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/utils.js"></SCRIPT>
<style>
.t {
  border: 1px solid black;
  border-collapse: collapse;
}
</style>


<html:errors/>
<html:form action="/ClaimsBulkUploadCountAction.do">

<!-- S T A R T : Page Title -->
<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td>Claims BulkUpload Count Dashboard</td>
	</tr>
</table>
<!-- E N D : Page Title -->

<!-- Start of form fields -->

<div class="contentArea" id="contentArea">
	<fieldset>
	 	<legend>Report Parameters </legend>
		  <table border="0" align="center" cellpadding="0" cellspacing="0" class="searchContainer">
			<tr>
				<td width="305px">From Date:<span class="mandatorySymbol">*</span>
					 <html:text property="sStartDate" styleId="sStartDate" styleClass="textBox textDate" onblur="onDateValidation(this,'From Date');"/>
					 <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmBulkUploadCount.sStartDate',document.frmBulkUploadCount.sStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
					 <img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
				</td>
				<td>To Date:
					
					 <html:text property="sEndDate" styleClass="textBox textDate" styleId="sEndDate" onblur="onDateValidation(this,'To Date');"/>
					 <A NAME="CalendarObjectempDate1" ID="CalendarObjectempDate1" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate1','frmBulkUploadCount.sEndDate',document.frmBulkUploadCount.sEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
					 <img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
				
				</td>		
			</tr>
		</table>
	
	</fieldset>
	
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%" align="center">
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSubmitForm();"><u>G</u>enerate</button>
				 &nbsp;
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>
			</td>
		</tr>
    </table>
    <br><br> <br><br>
    <fieldset>
    <legend>Claims BulkUpload Count Dashboard</legend>
    <table width="100%" border="1">
	  <!-- <tr height="18px">
	    <th align="center" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="40%">Main DashBoard Parameters</th>
	    <th align="center" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="20%">Vings</th>
	    <th align="center" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="20%">Provider Login</th>
	    <th align="center" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="20%">PBM Login</th>
	  </tr> -->
	  <tr height="47px">  
    <th rowspan="2" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="25%">Main DashBoard Parameters</th>  
    <th colspan="2" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="25%">Vings</th>  
    <th colspan="2" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="25%">Provider Login</th> 
     <th colspan="2" style="font-weight:bold;background-color:rgb(145, 200, 95);" width="25%">PBM Login</th> 
  </tr>
  
  
  
  <tr height="40px" style="font-weight:bold;background-color:rgb(145, 200, 95);" >  
    <td >Uploaded</td>  
    <td>Available in VINGS</td>  
    <td >Uploaded</td>  
    <td>Available in VINGS</td>
    <td >Uploaded</td>  
    <td>Available in VINGS</td>
  </tr>
  
  
  
  <tr height="40px"> 
   <td>No. of Successful Uploads</td>  
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="vingsBlkExlCnt"/>&nbsp;</td>  
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="vingsBlkPrcCnt"/>&nbsp;</td>   
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="providerBlkExlCnt"/>&nbsp;</td>   
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="providerBlkPrcCnt"/>&nbsp;</td>   
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="pbmBlkExlCnt"/>&nbsp;</td>   
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="pbmBlkPrcCnt"/>&nbsp;</td>   
  </tr>
  
	  
	  
<tr height="40px"> 
   <td >No. of Claims Uploaded</td>  
     <td >&nbsp;<bean:write name="frmBulkUploadCount" property="vingsRcvClmCnt"/>&nbsp;</td>  
     <td >&nbsp;<bean:write name="frmBulkUploadCount" property="vingsPrcClmCnt"/>&nbsp;</td>  
     <td >&nbsp;<bean:write name="frmBulkUploadCount" property="providerRcvClmCnt"/>&nbsp;</td>  
     <td >&nbsp;<bean:write name="frmBulkUploadCount" property="providerPrcClmCnt"/>&nbsp;</td>  
     <td >&nbsp;<bean:write name="frmBulkUploadCount" property="pbmRcvClmCnt"/>&nbsp;</td>  
     <td >&nbsp;<bean:write name="frmBulkUploadCount" property="pbmPrcClmCnt"/>&nbsp;</td>  
    
  </tr>
  
  
  <tr height="40px">  
   <td >No. of Activities Uploaded</td>  
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="vingsRcvActCnt"/>&nbsp;</td>
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="vingsPrcClmCnt"/>&nbsp;</td>
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="providerRcvActCnt"/>&nbsp;</td>
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="providerPrcClmCnt"/>&nbsp;</td>
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="pbmRcvActCnt"/>&nbsp;</td>
    <td >&nbsp;<bean:write name="frmBulkUploadCount" property="pbmPrcClmCnt"/>&nbsp;</td>
  </tr> 
  
  
	  <%--  <tr height="18px">
	    <td>Bulk Excel Upload Count</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="vingsBlkExlCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="providerBlkExlCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="pbmBlkExlCnt"/>&nbsp;</td>
	  </tr> --%>
	  
	   <%--  <tr height="18px">
	    <td>Received Claims Count</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="vingsRcvClmCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="providerRcvClmCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="pbmRcvClmCnt"/>&nbsp;</td>
	  </tr>
	  
	    <tr height="18px">
	    <td>Received Activity Count</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="vingsRcvActCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="providerRcvActCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="pbmRcvActCnt"/>&nbsp;</td>
	  </tr>
	    <tr height="18px">
	    <td>Processed Claims Count</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="vingsPrcClmCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="providerPrcClmCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="pbmPrcClmCnt"/>&nbsp;</td>
	  </tr>
	  
	    <tr height="18px">
	    <td>Pending Claims Till Date</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="vingsPndClmCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="providerPndClmCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="pbmPndClmCnt"/>&nbsp;</td>
	  </tr>
	  
	    <tr height="18px">
	    <td>Pending Activity Count Till Date</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="vingsPndActCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="providerPndActCnt"/>&nbsp;</td>
	    <td align="center">&nbsp;<bean:write name="frmBulkUploadCount" property="pbmPndActCnt"/>&nbsp;</td>
	  </tr> --%>
	 
	</table>
    </fieldset>
    
</div>
		
<input type="hidden" name="mode">
</html:form>