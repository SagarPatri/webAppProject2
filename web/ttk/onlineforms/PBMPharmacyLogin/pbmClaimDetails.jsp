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
<script type="text/javascript" src="/ttk/scripts/onlineforms/PBMPharmacyLogin/pbmClaimDetails.js"></script>
<head>
    

</head>
<html:form action="/PbmClaimAction.do" >

		<div class="contentArea" id="contentArea">
	<h4 class="sub_heading">Claim Submission Screen</h4>
<br><br>
	<!-- S T A R T : Page Title -->
	<html:errors/>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Form Fields -->
		

		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
			<%-- <p style="font: italic; size: 16">The Prior Authorization No. is  <h4> <font color="blue"> <%=authNO %></font></h4></p> --%>
			
	<tr>
      
		
		<%-- <logic:notEqual value="Approved" property="clmStatus" name="frmPbmClaimSuccess"> --%>
			 <td width="100%" class="formLabel" align="left" colspan="2">
				<strong> Your Claim Request is <font color="blue">  <bean:write name="frmPbmClaimSuccess" property="clmStatus"/></font> </strong>
				<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
				<br>
			 <strong>  "Received the Claim request, case is forwarded for review". </strong></td>	
		</tr>
		<tr>
		 <td width="50%" class="formLabel"> <strong>  Claim Number: &nbsp; &nbsp;  <bean:write name="frmPbmClaimSuccess" property="claimNumber"/>  </strong></td>	
		 <%-- <td width="50%"><strong> :  <%=authNO %> </strong> </td> --%>
		<%-- </logic:notEqual> --%>
			
   	</tr>
   	
		</table>
		<br>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
     <tr>
       <td width="15%" class="formLabel">Invoice Number </td>
       <td width="35%">: <bean:write name="frmPbmClaimSuccess" property="invoiceNumber"/> </td>
       <td width="15%" class="formLabel">Card Holder Name</td>
       <td width="35%">: <bean:write name="frmPbmClaimSuccess" property="memberName"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Policy No.</td>
       <td width="35%">: <bean:write name="frmPbmClaimSuccess" property="policyNo"/> </td>
       <td width="15%" class="formLabel">Clinician Id </td>
       <td width="35%">: <bean:write name="frmPbmClaimSuccess" property="clinicianId"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Clinician Name</td>
       <td width="35%">: <bean:write name="frmPbmClaimSuccess" property="clinicianName"/> </td>
       <td width="15%" class="formLabel">Status </td>
       <td width="35%">: <bean:write name="frmPbmClaimSuccess" property="clmStatus"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Claimed Amount </td>
       <td width="35%">: <bean:write name="frmPbmClaimSuccess" property="claimedAmt"/> </td>
   	</tr>
   	
</table>      		





<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
	<tr> <td>&nbsp;</td></tr>
     <tr> <td>&nbsp;</td></tr>
     <tr><td> <strong>NOTE:</strong> Claim settlement letter will be sent to registered mail id.
     </td></tr></table>
    
<br>
 <!-- S T A R T : Buttons -->
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	    	<tr>
		        <td width="100%" align="center">
					<button type="button" name="Button" accesskey="x" class="olbtnSmall" onClick="javascript:onExit();">E<u>x</u>it</button>&nbsp;
				 </td>
		      	<td width="100%" align="center">
	      	</tr>
	    </table>
		<!-- E N D : Buttons -->
	
	</div>
	
	
	
	<%-- <%
	String preAuthStatus=(String) session.getAttribute("preAuthStatus");
	if(preAuthStatus!=null&&(preAuthStatus.equals("Required Information")||preAuthStatus.equals("In-Progress"))){
	%> --%>
			
 <%-- <%}else{ %>
 <a href="#" onClick="javascript:onCommonUploadDocuments('HOS|CLM');">View Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <% }%> --%>
 <input type="hidden" name="claimSeqId" id="seqId" value="<%= request.getSession().getAttribute("claimSeqId")%>">
<input type="hidden" name="mode" value="">

<input type="hidden" name="leftlink" value="">
<input type="hidden" name="sublink" value="">
<input type="hidden" name="tab" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="loginType" value="PBM">

<html:hidden property="preAuthSeqID" styleId="preAuthSeqID" name="frmPbmClaimSuccess"/>

	
	<script type="text/javascript">
	generateLetter();
	</script>	
</html:form>