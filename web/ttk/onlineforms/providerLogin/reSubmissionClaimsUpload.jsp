<%
/** @ (#) reSubmissionClaimsUpload.jsp 
 * Project     : TTK Healthcare Services
 * File        : reSubmissionClaimsUpload.jsp
 * Author      : Deepthi Meesala
 * Company     : RCS Technologies
 * Date Created: 8th April 2021
 *
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
 %>
 <%@ page import="java.util.ArrayList,com.ttk.common.TTKCommon"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>`
<%@ page import="com.ttk.common.security.Cache,org.apache.struts.action.DynaActionForm"%>

<script language="javascript" src="/ttk/scripts/onlineforms/providerLogin/reSubmissionClaimsUpload.js"></script>
<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
 <script >
    $(document).ready(function(){
        startClaimUpload();
        onlineresizeDocument();
    });  	
 </script>
 	<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
	</SCRIPT>
	
<head>
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
	
<html:form action="/OnlineProviderClaimsAction.do" method="post" enctype="multipart/form-data">
<div class="contentArea" id="contentArea">
			<!-- S T A R T : Content/Form Area -->
	<h4 class="sub_heading">Claims Re-Submission</h4>
	
	
	
	<table align="center">
	<tr>
	      <td  align="center"> 
	      	<font color="red"> <strong>All date fields should be in DD/MM/YYYY format only.</strong></font>
	      </td>
      </tr>	 
	</table>
	
	
	
	<br>
	<html:errors/>

		<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="updated" scope="request"/>
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		<logic:notEmpty name="clinicianUploadStatus" scope="request">
             <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><img src="/ttk/images/warning.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
                        <bean:write name="clinicianUploadStatus" scope="request"/>
                    </td>
                  </tr>
             </table>
        </logic:notEmpty>
        
        
        <div align="center">
        <fieldset style="width: 60%"> <legend>Upload Claims</legend>
	    <table align="center"  border="0" cellspacing="0" cellpadding="0" style="width: 70%"> 
	    <tr> 
	    	<td class="formLabel"> Select file to upload </td>
	    	<td >
				: <html:file property="file" styleId="file"/>
			</td>
	    </tr> 
	    
	    <tr><td> &nbsp;</td> </tr>
	    <tr><td> &nbsp;</td> </tr>
	    <tr><td> 
	    <button type="button" name="Button" accesskey="u" class="olbtnLarge" onClick="javascript:onDownloadClmTemplate();">Download Template</button>
	    </td> </tr>
	    </table>
	</fieldset>
	</div><br>
	<div align="center">
	<logic:equal value="Y"  property="sussessYN" name="frmOnlineClaimUpload">
	 
	   <div style="color: black; font-size: 12px;">
			<table border="1" cellpadding="0" cellspacing="0" width="49%">
				<tr bgcolor="#677BA8" style="height: 20px;">
						<th colspan="2" style="color:white;" >Summary Of Your Latest Data Upload</th>
				</tr>
			
			
			<tr style="height: 20px;">
			    	<td class="formLabel" width="39%"  align="left" valign="middle">&nbsp;&nbsp;Batch Reference No : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmOnlineClaimUpload" property="batchRefNo"/></b> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Batch No : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmOnlineClaimUpload" property="batchNo"/></b></td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left"valign="middle">&nbsp;&nbsp;Batch Total Amount : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmOnlineClaimUpload" property="batchTotalAmount"/></b></td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left"valign="middle">&nbsp;&nbsp;Total No. of Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmOnlineClaimUpload" property="totalNoOfRows"/> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Failed Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmOnlineClaimUpload" property="totalNoOfRowsFailed"/> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Success Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmOnlineClaimUpload" property="totalNoOfRowsPassed"/> </td>
			    </tr> <tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Claims Uploaded : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmOnlineClaimUpload" property="totalNoOfClaimsUploaded"/> </td>
			    </tr> <tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Records Uploaded : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmOnlineClaimUpload" property="totalNoOfRecordsUploaded"/> </td>
			    </tr>  
   </table>
	</div>
	<logic:notEqual value="0" property="totalNoOfRowsFailed" name="frmOnlineClaimUpload">
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
    
	 <!-- S T A R T : Buttons -->
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
   	<tr>
        <td width="100%" align="center">
			<button type="button" name="Button" id="Button" accesskey="u" class="olbtnLarge" onClick="javascript:onUpload();"><u>U</u>pload</button>
		 </td>
      	<td width="100%" align="center"> 
   	</tr>
</table>

<br>
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

  <input type="hidden" name="mode" value="">
  <input type="hidden" name="batchNo" value=<bean:write name="frmOnlineClaimUpload" property="batchNo"/>>
  <input type="hidden" name="hiddenBatchRefNo" value=<bean:write name="frmOnlineClaimUpload" property="hiddenBatchRefNo"/>>
   <input type="hidden" name="flagYN" value="RESUB">
  </div>
</html:form>        