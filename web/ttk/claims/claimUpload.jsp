<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ page import="com.ttk.common.security.Cache,com.ttk.common.TTKCommon,java.util.Date,java.text.SimpleDateFormat" %>
	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
	<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
    <script type="text/javascript" src="/ttk/scripts/claims/claimUpload.js"></script>
    <script >
    $(document).ready(function(){
    	startClaimUpload();
    });  	
    </script>
    
	<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
	</SCRIPT>
<head>
<style>
.sdMainDiv{
position: absolute;
}
.searchBoxMoreMedium{
	width: 190px;
	height: 22px;
	background:white url(../images/SearchIcon.gif) no-repeat 3px 2px;
	padding-left:17px;
}

</style>


<script language=javascript>
document.onkeydown = function() 
{
    switch (event.keyCode) 
    {
        case 116 : //F5 button
        	alert("Please Don't Refresh The Page");
            event.returnValue = false;
            event.keyCode = 0;
            return false;
        case 82 : //R button
            if (event.ctrlKey) 
            {
                event.returnValue = false;
                event.keyCode = 0;
                return false;
            }
    }
}
</script>

<script>
    var message = "For This Page Function Disabled";
    function clickIE() {
        if (document.all) {
            alert(message);
            return false;
        }
    }
    function clickNS(e) {
        if (document.layers || (document.getElementById && !document.all)) {
            if (e.which == 2 || e.which == 3) {
                alert(message);
                return false;
            }
        }
    }
    if (document.layers) {
        document.captureEvents(Event.MOUSEDOWN);
        document.onmousedown = clickNS;
    }
    else {
        document.onmouseup = clickNS;
        document.oncontextmenu = clickIE;
    }
    document.oncontextmenu = new Function("return false")
</script>


</head>
<%

boolean submissionMode = false;
String ampm[] = {"AM", "PM"};
pageContext.setAttribute("ampm", ampm);
pageContext.setAttribute("claimsSubmissionTypes", Cache.getCacheObject("claimsSubmissionTypes"));
%>
<html:form action="/ClaimUploadAction.do" enctype="multipart/form-data">
<!-- S T A R T : Page Title -->

		
	<!-- E N D : Page Title -->
		
	
	<html:errors/>
	<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error" width="16" height="16" align="middle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	
	<!-- S T A R T : Success Box -->
			<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty> 	
    <!-- S T A R T : Form Fields -->
	<div class="contentArea" id="contentArea">
	<fieldset style="margin-left:10px;">
	<legend>Claim Upload</legend>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	    <tr>

        	<td align="right" nowrap><font style="color: red;"> <strong>Claims Submission Type&nbsp;:</strong></font></td>
        	<td align="left" nowrap>
       		<html:select name="frmClaimUpload" property="claimsSubmissionTypes" styleClass="selectBox selectBoxMoreMedium">
					<html:option value="">Select From List</html:option>
					<html:optionsCollection name="claimsSubmissionTypes" label="cacheDesc" value="cacheId" />
			</html:select>	
        	</td>
    </tr>
	<tr>
        <td align="right" nowrap>Provider ID &nbsp;: </td>      
        <td align="left" nowrap>
        <html:text property="providerId"
								styleId="providerId" styleClass="textBox textBoxLarge"
							name="frmClaimUpload"	disabled="<%=submissionMode%>"  readonly="true"/>
								
								<a href="#" accesskey="g"
												onClick="javascript:selectProvider()" class="search"> <img
												src="/ttk/images/EditIcon.gif" title="Select Provider" alt="Select Provider"
												width="16" height="16" border="0" align="absmiddle">&nbsp;
											</a>
        </td>
    </tr>
    <tr>
        
        <td align="right" nowrap>Provider Name&nbsp;:     </td>
        <td align="left" nowrap>
       <html:text property="providerName" styleClass="textBox textBoxLarge"  name="frmClaimUpload" readonly="true" />
        </td>
    </tr>
    
    <tr>
        <td align="right" nowrap>Received date&nbsp;: </td>
        <td align="left" nowrap>
        <html:text name="frmClaimUpload" property="receiveDate" styleClass="textBox textDate" maxlength="10"   />   
       <A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#"
		onClick="javascript:show_calendar('CalendarObjectPARDate','frmClaimUpload.receiveDate',document.frmClaimUpload.receiveDate.value,'',event,148,178);return false;"
		onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
		<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
		</a>&nbsp;
		<html:text name="frmClaimUpload" property="receivedTime" styleClass="textBox textTime" maxlength="5"/> &nbsp;
		<html:select property="receiveDay" name="frmClaimUpload" styleClass="selectBox" onchange="setEndDate();">
			<html:options name="ampm" labelName="ampm" />
		</html:select>
												<br> 
        </td>
    </tr>
    
    
   
   <tr>
    <td align="right" nowrap>Select Excel File to Upload Claims&nbsp;: <span class="mandatorySymbol">*</span></td>
	<td align="left" nowrap>
	  <html:file property="excelFile" name="frmClaimUpload"></html:file>
	</td>
	</tr> 
	
	<tr>
	      <td colspan="2" align="center"> 
	      	<font color="#04B4AE"> <strong>Please Select only .xls  file to upload.</strong></font>
	      </td>
      </tr>	    
		    
			    


  </table>
			    
</fieldset>
<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:20px; margin-bottom:40px;">
	<tr>
	<td></td>
	
	<td colspan="2" align="center">
	<button type="button" name="Button" accesskey="u" class="buttons" id="Button" style="margin-left: 10PX;"  onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUpLoadFiles();"><u>U</u>pload Claim</button>&nbsp;
	<button type="button" name="Button" accesskey="b" class="buttons" id="Button" style="margin-left: 10PX;"  onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBack();"><u>B</u>ack</button>&nbsp;
	</td>
	<td></td>
	<td></td>
	</tr>
	</table>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-bottom:40px;">
	<tr>
	 <td class="valueLabel">Click on <a href="#" onclick="javascript:onDownloadFile();">Download</a> for Claim Upload Format</td>
	
	</tr>
		<tr>
	 <td class="valueLabel">Click on <a href="#" onclick="javascript:onDownloadReSubFile();">Download</a> for Claim ReSubmission Upload Format</td>
	
	</tr>
	</table>
	<div align="center">
	<logic:equal value="Y"  property="sussessYN" name="frmClaimUpload">
	 
	   <div style="color: black; font-size: 12px;">
			<table border="1" cellpadding="0" cellspacing="0" width="49%">
				<tr bgcolor="#677BA8" style="height: 20px;">
						<th colspan="2" style="color:white;" >Summary Of Your Latest Data Upload</th>
				</tr>
			
			
			<tr style="height: 20px;">
			    	<td class="formLabel" width="39%"  align="left" valign="middle">&nbsp;&nbsp;Batch Reference No : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmClaimUpload" property="batchRefNo"/></b> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Batch No : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmClaimUpload" property="batchNo"/></b></td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left"valign="middle">&nbsp;&nbsp;Batch Total Amount : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmClaimUpload" property="batchTotalAmount"/></b></td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left"valign="middle">&nbsp;&nbsp;Total No. of Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmClaimUpload" property="totalNoOfRows"/> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Failed Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmClaimUpload" property="totalNoOfRowsFailed"/> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Success Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmClaimUpload" property="totalNoOfRowsPassed"/> </td>
			    </tr> <tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Claims Uploaded : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmClaimUpload" property="totalNoOfClaimsUploaded"/> </td>
			    </tr> <tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Auto Rejected Claims : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmClaimUpload" property="totalNoOfAutoRejectedClaims"/> </td>
			    </tr>  <tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Records Uploaded : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmClaimUpload" property="totalNoOfRecordsUploaded"/> </td>
			    </tr>  
   </table>
	</div>
	<logic:notEqual value="0" property="totalNoOfRowsFailed" name="frmClaimUpload">
    <table align="center"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
    <tr><td>&nbsp;</td></tr>
    <tr>
    <td colspan="2"> 
    <a href="#" onclick="javascript:onDownloadClmLog();">Click here</a> to view error log. 
	</td> 
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
	<td width="100%" colspan="2" >
	(Please correct the records found in error log and re-submit the whole claim excel once again) 
	</td>
	</tr>
	</table>		    
	</logic:notEqual>
	
	
    </logic:equal>
    </div>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td colspan="2" align="left"><b>Note:</b>
	</td>
	</tr>
	<tr>
	<td>
  <ol start="1">
  <li> All fields are mandatory except Pre Approval Number, Date of discharge, Clinician name, Secondary ICD code from 1 to 5, First incident date, First reported date,
   CPT Code, Tooth number, Nature of conception and Date of LMP.</li>
  <li>All date fields should be in DD-MM-YYYY format only.</li>
  <li>Please provide activity wise amount if there are multiple activities.</li>
  <li>Please select the System of medicine, Benefit type & Encounter type from the drop down only.</li>
  <li>Date of discharge is mandatory for inpatient and inpatient maternity benefit type.</li>
  <li>Please mention tooth numbers separated by comma if there are multiple tooth numbers Example:1,17,19,32</li>
  <li>If Activity type is Drug then Internal code and Activity code are not mandatory</li>
  </ol>
  </td>
  </tr>   
	</table>
	</div>	

 <html:hidden property="providerSeqId" styleId="providerSeqId" name="frmClaimUpload"/>
 <input type="hidden" name="batchNo" value=<bean:write name="frmClaimUpload" property="batchNo"/>>
 <input type="hidden" name="hiddenBatchRefNo" value=<bean:write name="frmClaimUpload" property="hiddenBatchRefNo"/>>
 <INPUT TYPE="hidden" NAME="rownum">
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
<INPUT TYPE="hidden" NAME="sublink" VALUE="">
<INPUT TYPE="hidden" NAME="tab" VALUE="">
<input type="hidden" name="child" value="">
</html:form>


