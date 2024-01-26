<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import="java.util.*,com.ttk.common.security.* ,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<%
//TTKCommon.setActiveLinks("Broker","Home","DashBoard",request);
%>
<head>
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
<link href="/ttk/styles/OnlineDefault.css" media="screen" rel="stylesheet"></link>
<style type="text/css">
.dashboardTable th{
padding: 10px;
text-align: left;
font-size: 11px;
}

.dashboardTable td{
padding: 10px;
text-align: left;
font-size: 11px;
}


.listDisplayTable{
border: 1px solid gray;
width: 100%;
}
.listDisplayTable th{
background-color:#677BA8;
 
text-align: center;
font-size: 11px;
color: white;

padding: 1px;
border-left: 1px solid gray;
}
.listDisplayTable th:FIRST-CHILD{
text-align: center;
font-size: 11px;
font-weight: bold;
color: white;
background-color:#677BA8;
padding: 1px;
border-left:none;
}
.listDisplayTable td{
text-align: center;
font-size: 11px;
background-color: #FAFAFF;
padding: 1px;
border-left: 1px solid gray;
border-top: 1px solid gray;
}
.listDisplayTable td:FIRST-CHILD{
text-align: center;
font-size: 11px;
background-color: #FAFAFF;
padding: 1px;
border-left: none;
}
</style>
<script type="text/javascript" src="/ttk/scripts/validation.js"></script>
<!-- <script type="text/javascript" src="/ttk/scripts/utils.js"></script> -->
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/onlineforms/PBMPharmacyLogin/requestauthorization.js"></script>
<script type="text/javascript">

</script>
</head>


<div class="contentArea" id="contentArea">
<div style=" background-color: rgb(67, 128, 74);" id="sideHeading">Pre-Approval OutPut Screen</div>
	
<html:form action="/PbmPharmacyGeneralAction.do" method="post">
<html:errors/>
<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)session.getAttribute("UserSecurityProfile");
	SecurityProfile spx=null;
	if(userSecurityProfile!=null && userSecurityProfile.getSecurityProfile()!=null)
	{
		spx = userSecurityProfile.getSecurityProfile();
	}
	
	boolean isDispensedfromPBMHome = false;
	boolean isDispensedfromPreauth = false;
	if(TTKCommon.getActiveLink(request).equals("PBMPL")){
		 isDispensedfromPBMHome = TTKCommon.isAuthorized(request, "pbmClaimSubmission");
	 
		}else if(TTKCommon.getActiveLink(request).equals("PBMPreauth")){
			 
			isDispensedfromPreauth = TTKCommon.isAuthorized(request, "pbmClaimSubmission");
		}
%>
<div id="sideHeadingSmall" style="margin-left: 0px;">Details</div>
<br>
<%
String isLogSearch=(String) session.getAttribute("isLogSearch");
if(isLogSearch!=null&&isLogSearch.equals("false")){%>
<logic:equal value="APR" property="finalStatus" name="frmPbmPreauthGeneral">

<table class="dashboardTable" style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
    <tr>
  <th>Pre-Approval No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="preAuthNO"/></td>
  <th>Transaction Date & Time</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="transactionDate"/></td>
  <th>Prescription Date</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="dateOfTreatment"/></td>
 </tr>
  <tr>
  <th>Authorization No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="authorizationNO"/></td>
  <th>Clinician ID</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="clinicianID"/></td>
  <th>Al Koot ID</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="enrolmentID"/></td>
 </tr>
  
  <tr>
  <th>Insurer Name</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="insuranceCompanyName"/></td>
  <th>Decision Date & Time</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="decisionDt"/></td>
  <th>Event Reference No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="eventRefNo"/></td>
  <!-- <th>Erx Ref#</th><th>:</th><td></td> -->
  <th></th><th></th><td></td>
 </tr>
 <%--  <tr>
  <th>Comments</th><th>:</th><td colspan="7"><bean:write name="frmPbmPreauthGeneral" property="denialComments"/></td>
 </tr> --%>
</table>


<div id="sideHeadingSmall" style="margin-left: 0px;">Diagnosis</div>
<br>
 <table class="listDisplayTable">
 <tr style="height:20px">
 <th style="width: 10%;">Type</th> 
  <th style="width: 10%;">ICD Code</th> 
  <th style="width: 80%;">Diagnosis Desc</th>
 </tr>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allIcdDetails">
 <logic:iterate id="diagnDetails" name="frmPbmPreauthGeneral" property="allIcdDetails">
   <tr>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="primaryAilment"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="icdCode"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="ailmentDescription"/></td>   	
   </tr>
 </logic:iterate>
 </logic:notEmpty>
 </table>

<div id="sideHeadingSmall" style="margin-left: 0px;">Drugs</div>
<br>

 <table class="listDisplayTable">
 <tr>
 <th style="width: 1%;" title="Serial NO">S.NO</th>
  <th style="width: 28%;" title="Drug Desc">Drug Desc</th>
  <th style="width:5%;" title="Status">Status</th>
  <th style="width: 4%;" title="Quantity">Qty</th>
   <th style="width: 4%;" title="Units">Units</th>
  <th style="width: 4%;" title="Requested Amount">Req.Amt</th>
  <th style="width: 4%;" title="Approved Amount">Appr.Amt</th>
  <th style="width: 4%;" title="Patient Share">Pat Share</th>
  <th style="width: 4%;" title="Duration">Duration</th>
  <th style="width: 8%;" title="Denial">Denial</th>
  <th style="width: 18%;" title="Comments">Comments</th>
  <th style="width: 6%;" title="Date of Approval">Date Of Approval</th>
  <!-- <th style="width: 4%;" title="MI ref">MI ref</th> -->
 <!--  <th style="width: 4%;" title="eRX ref">eRX ref</th>
  <th style="width:10%;" title="eRX Instructions">eRX Instructions</th> -->
  <!-- <th style="width: 1%;" title="checkAll"><input type="checkbox" name="drugCheckAll" onclick="drucCheckAll(this);"></th> -->
 </tr>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allDrugDetails">
 <logic:iterate id="drugDetails" name="frmPbmPreauthGeneral" property="allDrugDetails">
   <tr>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="serialNo"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="activityCodeDesc"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="activityStatus"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="quantityInt"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="unitType"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="providerRequestedAmt"/></td>
    <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="approvedAmount"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="patientShare"/></td> 
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="duration"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="denialDescription"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="denialRemarks"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="dateOfApproval"/></td>
   <%-- <td><bean:write name="drugDetails" property="miRef"/></td> --%>
   <%-- <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="erxRef"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="erxInstruction"/></td>   --%>
    <%-- <td><input type="checkbox" name="drucCheck" value="<bean:write name="drugDetails" property="activityDtlSeqId"/>"></td>  --%>		
   </tr>
 </logic:iterate>
 <tr style="height:30px">
   <td style="width: 1%;background-color: #F0F0F0;"></td>
   <td style="width: 28%; background-color: #F0F0F0;"></td>
   <td style="width:5%; background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalReqAmt"/></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalApprAmt"/></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;background-color: #F0F0F0;"></td>
   <td style="width: 18%;background-color: #F0F0F0;"></td>
   <td style="width: 6%;background-color: #F0F0F0;"></td>
   <!-- <td style="width: 4%;"></td>
   <td style="width:10%;"></td> -->
   <!-- <td style="width:1%;"></td> -->
   </tr>
   </logic:notEmpty>
 </table>
 <br>
<%--  String preAuthStatus=(String) session.getAttribute("preAuthStatus");
	if(preAuthStatus!=null&&(preAuthStatus.equals("Required Information")||preAuthStatus.equals("In-Progress")||preAuthStatus.equals("In Progress"))){%>
	<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|PAT');">Document Uploads </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	< --%>
	<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|PAT');">View Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<%-- < --%>
 
<div align="center">
 
 <table>
 <tr>
 <td></td>
 <td></td>
 <td>
 <button name="mybutton" class="olbtn" accesskey="c" onclick="onBack();" type="button"><u>C</u>lose</button>
 </td>
 <td></td>
 <%--   <% if(isDispensedfromPBMHome){%>
  <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onDispensed();" type="button"><u>D</u>ispensed</button>
  <%}else if(isDispensedfromPreauth) {%>
    <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onDispensed();" type="button"><u>D</u>ispensed</button>
  <%} %>
  </td> --%>
  <td></td>
 
 </tr>
 </table>
</logic:equal>
<%} if(isLogSearch!=null&&isLogSearch.equals("false")){ %>

<logic:equal value="REJ" property="finalStatus" name="frmPbmPreauthGeneral">

<table class="dashboardTable" style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
    <tr>
  <th>Pre-Approval No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="preAuthNO"/></td>
  <th>Transaction Date & Time</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="transactionDate"/></td>
  <th>Prescription Date</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="dateOfTreatment"/></td>
 </tr>
  <tr>
  <th>Authorization No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="authorizationNO"/></td>
  <th>Clinician ID</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="clinicianID"/></td>
  <th>Al Koot ID</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="enrolmentID"/></td>
 </tr>
  
  <tr>
  <th>Insurer Name</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="insuranceCompanyName"/></td>
  <th>Decision Date & Time</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="decisionDt"/></td>
  <th>Event Reference No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="eventRefNo"/></td>
  <!-- <th>Erx Ref#</th><th>:</th><td></td> -->
  <th></th><th></th><td></td>
 </tr>
 <%--  <tr>
  <th>Comments</th><th>:</th><td colspan="7"><bean:write name="frmPbmPreauthGeneral" property="denialComments"/></td>
 </tr> --%>
</table>


<div id="sideHeadingSmall" style="margin-left: 0px;">Diagnosis</div>
<br>
 <table class="listDisplayTable">
 <tr style="height:20px">
 <th style="width: 10%;">Type</th> 
  <th style="width: 10%;">ICD Code</th> 
  <th style="width: 80%;">Diagnosis Desc</th>
 </tr>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allIcdDetails">
 <logic:iterate id="diagnDetails" name="frmPbmPreauthGeneral" property="allIcdDetails">
   <tr>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="primaryAilment"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="icdCode"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="ailmentDescription"/></td>   	
   </tr>
 </logic:iterate>
 </logic:notEmpty>
 </table>

<div id="sideHeadingSmall" style="margin-left: 0px;">Drugs</div>
<br>

 <table class="listDisplayTable">
 <tr>
 <th style="width: 1%;" title="Serial NO">S.NO</th>
  <th style="width: 28%;" title="Drug Desc">Drug Desc</th>
  <th style="width:5%;" title="Status">Status</th>
  <th style="width: 4%;" title="Quantity">Qty</th>
   <th style="width: 4%;" title="Units">Units</th>
  <th style="width: 4%;" title="Requested Amount">Req.Amt</th>
  <th style="width: 4%;" title="Approved Amount">Appr.Amt</th>
  <th style="width: 4%;" title="Patient Share">Pat Share</th>
  <th style="width: 4%;" title="Duration">Duration</th>
  <th style="width: 8%;" title="Denial">Denial</th>
  <th style="width: 18%;" title="Comments">Comments</th>
  <th style="width: 6%;" title="Date of Approval">Date Of Approval</th>
  <!-- <th style="width: 4%;" title="MI ref">MI ref</th> -->
 <!--  <th style="width: 4%;" title="eRX ref">eRX ref</th>
  <th style="width:10%;" title="eRX Instructions">eRX Instructions</th> -->
  <!-- <th style="width: 1%;" title="checkAll"><input type="checkbox" name="drugCheckAll" onclick="drucCheckAll(this);"></th> -->
 </tr>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allDrugDetails">
 <logic:iterate id="drugDetails" name="frmPbmPreauthGeneral" property="allDrugDetails">
   <tr>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="serialNo"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="activityCodeDesc"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="activityStatus"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="quantityInt"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="unitType"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="providerRequestedAmt"/></td>
    <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="approvedAmount"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="patientShare"/></td> 
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="duration"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="denialDescription"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="denialRemarks"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="dateOfApproval"/></td>
   <%-- <td><bean:write name="drugDetails" property="miRef"/></td> --%>
   <%-- <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="erxRef"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="erxInstruction"/></td>   --%>
    <%-- <td><input type="checkbox" name="drucCheck" value="<bean:write name="drugDetails" property="activityDtlSeqId"/>"></td>  --%>		
   </tr>
 </logic:iterate>
 <tr style="height:30px">
   <td style="width: 1%;background-color: #F0F0F0;"></td>
   <td style="width: 28%; background-color: #F0F0F0;"></td>
   <td style="width:5%; background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalReqAmt"/></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalApprAmt"/></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;background-color: #F0F0F0;"></td>
   <td style="width: 18%;background-color: #F0F0F0;"></td>
   <td style="width: 6%;background-color: #F0F0F0;"></td>
   <!-- <td style="width: 4%;"></td>
   <td style="width:10%;"></td> -->
   <!-- <td style="width:1%;"></td> -->
   </tr>
   </logic:notEmpty>
 </table>
 <br>
<%--  <%	String preAuthStatus=(String) session.getAttribute("preAuthStatus");
	if(preAuthStatus!=null&&(preAuthStatus.equals("Required Information")||preAuthStatus.equals("In-Progress")||preAuthStatus.equals("In Progress"))){%>
	<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|PAT');">Document Uploads </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<%}else{%> --%>
	<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|PAT');">View Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%-- 	<%}%> --%>
<div align="center">
 
 <table>
 <tr>
 <td></td>
 <td></td>
 <td>
 <button name="mybutton" class="olbtn" accesskey="c" onclick="onBack();" type="button"><u>C</u>lose</button>
 </td>
 <td></td>
 <%-- <% if(isDispensedfromPBMHome){%>
  <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onDispensed();" type="button"><u>D</u>ispensed</button>
  <%}else if(isDispensedfromPreauth) {%>
     <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onDispensed();" type="button"><u>D</u>ispensed</button>
   <%} %>
   </td> --%>
  <td></td>
 
 </tr>
 </table>
</logic:equal>

<%} %>
<!--   code for inprogress status -->
<%if(isLogSearch!=null&&isLogSearch.equals("false")){ %>
 <logic:equal value="INP" property="finalStatus" name="frmPbmPreauthGeneral" >
 <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
 
			<%-- <p style="font: italic; size: 16">The Prior Authorization No. is  <h4> <font color="blue"> <%=authNO %></font></h4></p> --%>
			
	<tr>
     
		
			 <td width="100%" class="formLabel" align="left" colspan="2">
				<strong> Your PreApproval Request is <font color="blue">  <bean:write name="frmPbmPreauthGeneral" property="preAuthstatusDesc"/></font> </strong>
				<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
				<br>
			 <strong>  "Received the preapproval request, case is forwarded for review". </strong></td>	
		</tr>
		<tr>
		 <td width="50%" class="formLabel"> <strong>  Pre-Approval No. : &nbsp; &nbsp; <bean:write name="frmPbmPreauthGeneral" property="preAuthNO"/>  </strong></td>	
		 <td width="50%"><strong> <%--  <%=authNO %> --%> </strong> </td>
			
   	</tr>
   	
		</table>
		<br>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 70%">
     <tr>
       <td width="15%" class="formLabel">Authorization No. </td>
       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="authorizationNO"/> </td>
       <td width="15%" class="formLabel">Card Holder Name</td>
       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="memberName"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Policy No.</td>
       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="policyNo"/> </td>
       <td width="15%" class="formLabel">Clinician Id </td>
       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="clinicianID"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Clinician Name</td>
       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="clinicianName"/> </td>
       <td width="15%" class="formLabel">Status </td>
       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="preAuthstatusDesc"/> </td>
   	</tr>
     <tr>
       <td width="15%" class="formLabel">Pre-Approval Amount </td>
       <td width="35%">: <bean:write name="frmPbmPreauthGeneral" property="totalGrossAmt"/>  </td>
   	</tr>
   	
</table>      		



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
					<button type="button" name="Button" accesskey="x" class="olbtnSmall" onClick="javascript:onBack();">E<u>x</u>it</button>&nbsp;
				 </td>
		      	<td width="100%" align="center">
	      	</tr>
	    </table>
	    </logic:equal>
	    <%} %>
	    <% if(isLogSearch.equals("true")){ %>
	    	<table class="dashboardTable" style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
    <tr>
  <th>Pre-Approval No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="preAuthNO"/></td>
  <th>Transaction Date & Time</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="transactionDate"/></td>
  <th>Prescription Date</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="dateOfTreatment"/></td>
 </tr>
  <tr>
  <th>Authorization No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="authorizationNO"/></td>
  <th>Clinician ID</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="clinicianID"/></td>
  <th>Al Koot ID</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="enrolmentID"/></td>
 </tr>
  
  <tr>
  <th>Insurer Name</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="insuranceCompanyName"/></td>
  <th>Decision Date & Time</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="decisionDt"/></td>
  <th>Event Reference No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="eventRefNo"/></td>
  <!-- <th>Erx Ref#</th><th>:</th><td></td> -->
  <th></th><th></th><td></td>
 </tr>
 <%--  <tr>
  <th>Comments</th><th>:</th><td colspan="7"><bean:write name="frmPbmPreauthGeneral" property="denialComments"/></td>
 </tr> --%>
</table>


<div id="sideHeadingSmall" style="margin-left: 0px;">Diagnosis</div>
<br>
 <table class="listDisplayTable">
 <tr style="height:20px">
 <th style="width: 10%;">Type</th> 
  <th style="width: 10%;">ICD Code</th> 
  <th style="width: 80%;">Diagnosis Desc</th>
 </tr>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allIcdDetails">
 <logic:iterate id="diagnDetails" name="frmPbmPreauthGeneral" property="allIcdDetails">
   <tr>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="primaryAilment"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="icdCode"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="ailmentDescription"/></td>   	
   </tr>
 </logic:iterate>
 </logic:notEmpty>
 </table>

<div id="sideHeadingSmall" style="margin-left: 0px;">Drugs</div>
<br>

 <table class="listDisplayTable">
 <tr>
 <th style="width: 1%;" title="Serial NO">S.NO</th>
  <th style="width: 28%;" title="Drug Desc">Drug Desc</th>
  <th style="width:5%;" title="Status">Status</th>
  <th style="width: 4%;" title="Quantity">Qty</th>
   <th style="width: 4%;" title="Units">Units</th>
  <th style="width: 4%;" title="Requested Amount">Req.Amt</th>
  <th style="width: 4%;" title="Approved Amount">Appr.Amt</th>
  <th style="width: 4%;" title="Patient Share">Pat Share</th>
  <th style="width: 4%;" title="Duration">Duration</th>
  <th style="width: 8%;" title="Denial">Denial</th>
  <th style="width: 18%;" title="Comments">Comments</th>
  <th style="width: 6%;" title="Date of Approval">Date Of Approval</th>
  <!-- <th style="width: 4%;" title="MI ref">MI ref</th> -->
 <!--  <th style="width: 4%;" title="eRX ref">eRX ref</th>
  <th style="width:10%;" title="eRX Instructions">eRX Instructions</th> -->
  <!-- <th style="width: 1%;" title="checkAll"><input type="checkbox" name="drugCheckAll" onclick="drucCheckAll(this);"></th> -->
 </tr>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allDrugDetails">
 <logic:iterate id="drugDetails" name="frmPbmPreauthGeneral" property="allDrugDetails">
   <tr>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="serialNo"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="activityCodeDesc"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="activityStatus"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="quantityInt"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="unitType"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="providerRequestedAmt"/></td>
    <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="approvedAmount"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="patientShare"/></td> 
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="duration"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="denialDescription"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="denialRemarks"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="dateOfApproval"/></td>
   <%-- <td><bean:write name="drugDetails" property="miRef"/></td> --%>
   <%-- <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="erxRef"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="erxInstruction"/></td>   --%>
    <%-- <td><input type="checkbox" name="drucCheck" value="<bean:write name="drugDetails" property="activityDtlSeqId"/>"></td>  --%>		
   </tr>
 </logic:iterate>
 <tr style="height:30px">
   <td style="width: 1%;background-color: #F0F0F0;"></td>
   <td style="width: 28%; background-color: #F0F0F0;"></td>
   <td style="width:5%; background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalReqAmt"/></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalApprAmt"/></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;background-color: #F0F0F0;"></td>
   <td style="width: 18%;background-color: #F0F0F0;"></td>
   <td style="width: 6%;background-color: #F0F0F0;"></td>
   <!-- <td style="width: 4%;"></td>
   <td style="width:10%;"></td> -->
   <!-- <td style="width:1%;"></td> -->
   </tr>
   </logic:notEmpty>
 </table>
 <br>
<%--  <%String preAuthStatus=(String) session.getAttribute("preAuthStatus");
	if(preAuthStatus!=null&&(preAuthStatus.equals("Required Information")||preAuthStatus.equals("In-Progress")||preAuthStatus.equals("In Progress"))){%>
	<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|PAT');">Document Uploads </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<%}else{%> --%>
	<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|PAT');">View Documents</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%-- 	<%}%> --%>
 <br><br><br><br><br><br>
<logic:equal name="frmPbmPreauthGeneral" property="appealBtn" value="appealBtn">
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
		<tr>
				<td width="100%" align="center">
				 	<b>Appeal comments:<span class="mandatorySymbol">*</span></b> <html:textarea name="frmPbmPreauthGeneral" property="appealComments" styleId="appealComments" cols="45" rows="2" />
				 	&nbsp;&nbsp;&nbsp;&nbsp; 
				 	<a href="#" onClick="javascript:onCommonUploadDocuments2('PBM|PAT','APL');">Appeal Documents</a>
				</td>
 		</tr>
 		
 	</table>
 </logic:equal>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="appealComments">
	 <logic:equal name="frmPbmPreauthGeneral" property="refview" value="refview">
			<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
			<tr>
					<td width="100%" align="center">
					 	<b>Appeal comments:<span class="mandatorySymbol">*</span></b> <html:textarea name="frmPbmPreauthGeneral" property="appealComments" styleId="appealComments" cols="45" rows="2" readonly="true"/>
					 	&nbsp;&nbsp;&nbsp;&nbsp; 
					 	<!-- <a href="#" onClick="javascript:onCommonUploadDocuments2('PBM|PAT','APL');">Appeal Documents</a> -->
					</td>
	 		</tr>
	 		
	 	</table>
	 </logic:equal>
 </logic:notEmpty>
  <br><br><br><br>
 
<div align="center">
 
 <table>
 <tr>
 <td></td>
 <td></td>
 <logic:equal name="frmPbmPreauthGeneral" property="appealBtn" value="appealBtn">	 
	 <td>
	  <button name="mybutton" class="olbtn" accesskey="c" onclick="onSaveAppealComments();" type="button"><u>S</u>ave</button>
	 </td>
 </logic:equal>	 
	 <td>
  <button name="mybutton" class="olbtn" accesskey="c" onclick="onBack();" type="button"><u>C</u>lose</button>
 </td>
 <td></td>
 <%-- <% if(isDispensedfromPBMHome){%>
 <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onDispensed();" type="button"><u>D</u>ispensed</button>
  <%}else if(isDispensedfromPreauth) {%>
  
     <logic:empty name="frmPbmPreauthGeneral" property="appealBtn">
    <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onDispensed();" type="button"><u>D</u>ispensed</button>
   	 </logic:empty>
  <%} %>  </td> --%>
  <td></td>
 
 </tr>
 </table>
	    <%} %>
	 <!--    code for in progress status end --> 
 
</div>
<logic:equal value="INP" property="finalStatus" name="frmPbmPreauthGeneral">
<div align="left">
<table>
<tr>
<th>Note:</th><td>&nbsp;Select <b>Dispensed</b> option for Claim submission</td>
</table>
</div>
</logic:equal>

<logic:equal name="frmPbmPreauthGeneral" property="appealBtn" value="appealBtn">
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	<tr>	
   	 	<td colspan="2"><br><br><br><br><font size="2"><b>Note:</b> Appeal Comments is mandatory & please provider(Upload) Appeal Documents for future reference.</font></td>
   	</tr>
</table> 
</logic:equal>

<input type="hidden" name="mode" value="">

<input type="hidden" name="leftlink" value="<%= spx.getActiveLink() %>">
<input type="hidden" name="sublink" value="<%= spx.getActiveSubLink() %>">
<logic:equal name="closeMode" value="View" scope="session">
<input type="hidden" name="tab" value= " <%=spx.getActiveTab()%>">
</logic:equal>
<logic:notEqual name="closeMode" value="View" scope="session">
<input type="hidden" name="tab" value= " <%=spx.getActiveTab()%>">
</logic:notEqual>
<input type="hidden" name="child" value="">

<input type="hidden" name="loginType" value="PBM">
<input type="hidden" name="preAuthSeqID" id="seqId" value="<%= request.getSession().getAttribute("preAuthSeqID")%>">
<html:hidden property="preAuthStatus" styleId="preAuthStatus" name="frmPbmPreauthGeneral"/>
<html:hidden property="completedYN" styleId="completedYN" name="frmPbmPreauthGeneral"/>
<html:hidden property="preAuthSeqID" styleId="preAuthSeqID" name="frmPbmPreauthGeneral"/>
<html:hidden property="appealDocsYN" styleId="appealDocsYN" name="frmPbmPreauthGeneral"/>

</html:form>
</div>
