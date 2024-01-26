<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>


	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
	<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
    <script type="text/javascript" src="/ttk/scripts/reports/auditDataUpload.js"></script>
     <script >
    $(document).ready(function(){
    	getClaimUploadCount();
    });  	
    </script>
    
 <html:form action="/ClaimUploadAction.do" enctype="multipart/form-data">
 
 
 <html:errors/>
 <logic:notEmpty name="notifyerror" scope="request">
  <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning" width="16" height="16" align="absmiddle">&nbsp;
         <bean:write name="notifyerror" scope="request"/>
     </td>
   </tr>
  </table>
 </logic:notEmpty>
 	<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty> 	
 
 <div class="contentArea" id="contentArea">
 <fieldset style="margin-left:10px;">
	<legend>Audit Data Upload</legend>
 
 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="right" nowrap>Select Excel File to Upload Claims&nbsp;: <span class="mandatorySymbol">*</span></td>
	<td align="left" nowrap>
	  <html:file property="excelFile" name="frmAuditReportList"></html:file>
	</td>
	</tr> 
 
 <tr>
	      <td colspan="2" align="center"> 
	      	<font color="#04B4AE"> <strong>Please Select .xls file to upload.</strong></font>
	      </td>
      </tr>	 
 
 </table>
 
 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:20px; margin-bottom:40px;">
	<tr>
	<td></td>
	
	<td colspan="2" align="center">
	<button type="button" name="Button" accesskey="u" class="buttons"  style="margin-left: 10PX;"  onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUpLoadFiles();"><u>U</u>pload Claim</button>&nbsp;
	<!-- <button type="button" name="Button" accesskey="b" class="buttons"  style="margin-left: 10PX;"  onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBack();"><u>B</u>ack</button>&nbsp; -->
	</td>
	<td></td>
	<td></td>
	</tr>
	</table>
 
 <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-bottom:40px;">
	<tr>
	 <td class="valueLabel">Click on <a href="#" onclick="javascript:onDownloadFile();">Download</a> for Claim Upload Format</td>
	
	</tr>
	</table>
 
 </fieldset>
 
 </div>
 
    <INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
<html:hidden property="sussessYN" styleId="sussessYN" name="frmAuditReportList"/>
 
 
 </html:form>   
    
    
    

