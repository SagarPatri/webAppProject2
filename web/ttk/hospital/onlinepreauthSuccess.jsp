<%
/** @ (#) onlinepreauthSuccess.jsp 25th Feb 2008
 * Project     : INTX Services
 * File        : Preauth Success.jsp
 * Author      : kishor kumar 
 * Company     : RCS Technologies
 * Date Created: 25th Oct 2014
 *
 * @author 		 : kishor kumar
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
 %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon,org.apache.struts.action.DynaActionForm" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<head>
    
<script type="text/javascript">

function onExit()
{
	document.forms[1].tab.value ="Check Eligibility";
	document.forms[1].sublink.value ="Check Eligibility";
	document.forms[1].leftlink.value ="Approval Claims";
	
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/OnlineCashlessHospAction.do";
	document.forms[1].submit();
}
function generateLetter(){
	var statusID=document.forms[1].status.value;
	if(statusID === 'APR')
    { 
	     var parameterValue="|"+document.forms[1].preAuthSeqID.value+"|"+statusID+"|ALK|";
	     var parameter = "";
	     var authno = document.forms[1].authorizationNo.value;
	      
		      	parameter = "?mode=generatePreauthLetter&reportType=PDF&parameter="+parameterValue+"&fileName=generalreports/AuthAprLetter.jrxml&reportID=AuthLetter&authorizationNo="+authno;
	         
	     
	     /* else if(statusID === 'REJ')
	     {
	           parameter = "?mode=generatePreauthLetter&reportType=PDF&parameter="+parameterValue+"&fileName=generalreports/AuthRejLetter.jrxml&reportID=AuthLetter&authorizationNo="+authno;
	     }//end of else	   
	    	  alert("parameter::"+parameter);
	     */
	  var openPage = "/OnlinePreAuthLetterAction.do"+parameter;
	  var w = screen.availWidth - 10;
	  var h = screen.availHeight - 49;
	  var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	  window.open(openPage,'',features);
    }//end of if(statusID == 'APR')
}
</script>
</head>
<html:form action="/OnlineCashlessHospActionNew.do" >

		<div class="contentArea" id="contentArea">
		
	<logic:equal value="Y" property="appealFlag" name="frmOnlinePreAuth">
	<h4 class="sub_heading">Online Pre-Approval-Appeal</h4>
	</logic:equal>
	<logic:notEqual value="Y" property="appealFlag" name="frmOnlinePreAuth">
	<h4 class="sub_heading">Online Pre-Approval</h4>
	</logic:notEqual>
	
<br><br>
	<!-- S T A R T : Page Title -->
	<html:errors/>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Form Fields -->
		<%
		String authNO	=	(String)request.getSession().getAttribute("onlinePreAuthNO");
		%>
<%-- <div align="center">
	<p style="font: italic; size: 12">You have successfully submitted the  Pre Authorization request. </p>
	<p style="font: italic; size: 10">The Prior Authorization No. is  : <h3> <font color="blue"> <%=authNO %></font>.</h3></p>
	<p style="font: italic; size: 14">Kindly note the same for future references.</p>
	<p style="size: 12">Please check the Pre Auth Log / e-mail for the Approval status.</p>
	<p>Thank you</p>
</div> --%>

		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
			<%-- <p style="font: italic; size: 16">The Prior Authorization No. is  <h4> <font color="blue"> <%=authNO %></font></h4></p> --%>
			
	<tr>
       <logic:equal value="Approved" property="status" name="frmOnlinePreAuth">
        <td width="50%" class="formLabel" align="left">
				<strong> Your PreApproval Request has been <font color="blue">  <bean:write name="frmOnlinePreAuth" property="status"/></font> </strong> 
		</td>
		<td width="50%" class="formLabel" align="left">
			<strong>PreApproval No. :<%=authNO %> </strong>
		</td>			 
		</logic:equal>
		
		<logic:notEqual value="Approved" property="status" name="frmOnlinePreAuth">
			 
			 <logic:equal value="Y" property="appealFlag" name="frmOnlinePreAuth">
				 <td width="100%" class="formLabel" align="left" colspan="2">
					<strong> Your PreApproval Request is <font color="blue"> In-Progress </font> </strong>
					<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
					<br>
				 <strong>  "Received the preapproval appeal request, case is forwarded for review". </strong></td>	
			</logic:equal>
			
			<logic:notEqual value="Y" property="appealFlag" name="frmOnlinePreAuth">
				 <td width="100%" class="formLabel" align="left" colspan="2">
					<strong> Your PreApproval Request is <font color="blue">  <bean:write name="frmOnlinePreAuth" property="status"/></font> </strong>
					<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
					<br>
				 <strong>  "Received the preapproval request, case is forwarded for review". </strong></td>	
			</logic:notEqual>
		
		</tr>
		<tr>
		 <td width="50%" class="formLabel"> <strong>  Pre-Approval No. : &nbsp; &nbsp; <%=authNO %>  </strong></td>	
		 <%-- <td width="50%"><strong> :  <%=authNO %> </strong> </td> --%>
		</logic:notEqual>
			
   	</tr>
   	
		</table>
		<br>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
     <tr>
       <td width="15%" class="formLabel">Authorization No. </td>
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="authorizationNo"/> </td>
       <td width="15%" class="formLabel">Card Holder Name</td>
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="memberName"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Policy No.</td>
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="policyNo"/> </td>
       <td width="15%" class="formLabel">Clinician Id </td>
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="clinicianId"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Clinician Name</td>
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="clinicianName"/> </td>
       <td width="15%" class="formLabel">Status </td>
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="status"/>&nbsp;&nbsp;
       	<logic:equal name="frmOnlinePreAuth" property="status" value="In-Progress-Appeal">
       	<img src="/ttk/images/InprogressAppeal.gif" title="In Progress Appeal" alt="In Progress Appeal" width="16" height="16" align="absmiddle">
       	</logic:equal>
       	<logic:equal name="frmOnlinePreAuth" property="status" value="In-Progress-Enhancement">
       	<img src="/ttk/images/InprogressEnhancement.gif" title="In Progress Enhancement" alt="In Progress Enhancement" width="16" height="16" align="absmiddle">
       	</logic:equal>
       	<logic:equal name="frmOnlinePreAuth" property="status" value="In-Progress-ShortFallresponded">
       	<img src="/ttk/images/AddIcon.gif" title="In Progress ShortFallresponded" alt="In Progress ShortFallresponded" width="16" height="16" align="absmiddle">
       	</logic:equal>			
       					
       </td>
   	</tr>
     <tr>
     	
       <td width="15%" class="formLabel">Pre-Approval Amount </td>
       <logic:equal name="frmOnlinePreAuth" property="status" value="In-Progress-Appeal">
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="preApprovalAmt"/> </td>
       </logic:equal>
        <logic:notEqual name="frmOnlinePreAuth" property="status" value="In-Progress-Appeal">
       <td width="35%">: <bean:write name="frmOnlinePreAuth" property="totalAgreedRate"/> </td>
       </logic:notEqual>
   	</tr>
   	
</table>      		


<logic:equal value="Approved" property="status" name="frmOnlinePreAuth">
<ttk:OnlineDiagnosisDetails flow="PAT" flag="PATCreate" />
<br><br>
<ttk:OnlineActivityDetails flow="PAT" flag="PATCreate"/>
<br><br>
<ttk:OnlineDrugDetails flow="PAT" flag="PATCreate"/>
</logic:equal>


<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
	<tr> <td>&nbsp;</td></tr>
     <tr> <td>&nbsp;</td></tr>
     <tr><td> <strong>NOTE:</strong> An Authorization letter will be sent to the registered email id of Provider & Patient within 30 minutes.
     </td></tr></table>
     
<br>
 <!-- S T A R T : Buttons -->
        
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	    	<tr>
		        <td width="100%" align="center">
		       <logic:notEqual value="Approved" property="status" name="frmOnlinePreAuth">
		            <a href="#" onClick="javascript:onCommonUploadDocuments('HOS|PAT');">Document Uploads </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		            
		      </logic:notEqual>
				
					<button type="button" name="Button" accesskey="x" class="olbtnSmall" onClick="javascript:onExit();">E<u>x</u>it</button>&nbsp;
				 </td>
		      	<td width="100%" align="center">
	      	</tr>
	    </table>
	    
		<!-- E N D : Buttons -->
	
	</div>
	<input type="hidden" name="mode" value="">
	<INPUT TYPE="hidden" NAME="leftlink">
	<INPUT TYPE="hidden" NAME="sublink">
	<INPUT TYPE="hidden" NAME="tab">
	<INPUT TYPE="hidden" NAME="preAuthSeqID" id="seqId" value="<bean:write name="frmOnlinePreAuth" property="preAuthSeqID"/>">
	<INPUT TYPE="hidden" NAME="status" value="<bean:write name="frmOnlinePreAuth" property="statusTypeID"/>">
	<INPUT TYPE="hidden" NAME="authorizationNo" value="<%=authNO%>">
	
	<script type="text/javascript">
	generateLetter();
	</script>
</body>
	
	
	
</html:form>