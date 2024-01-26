<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<%
//TTKCommon.setActiveLinks("Broker","Home","DashBoard",request);
%>

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
width: 96%;
}
.listDisplayTable th{
text-align: center;
font-size: 11px;
color: white;
background-color: #677BA8; /* #70A2FF; */
padding: 1px;
border-left: 1px solid gray;
}
.listDisplayTable th:FIRST-CHILD{
text-align: center;
font-size: 11px;
font-weight: bold;
color: white;
background-color: #677BA8;/* #70A2FF; */
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
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/onlineforms/PBMPharmacyLogin/pbmClaimSubmission.js"></script>
<script type="text/javascript">

</script>
<%
pageContext.setAttribute("provDocsType", Cache.getCacheObject("provDocsType"));
String claimStatus=(String) session.getAttribute("claimStatus");
String preAuthStatus=(String) session.getAttribute("preAuthStatus"); 
String closeMode = (String)request.getSession().getAttribute("closeMode");
boolean viewmode=false;

if (TTKCommon.isAuthorized(request, "pbmClaimSuccess")) {
	viewmode = true;
}
%>

<div class="contentArea" id="contentArea">

<html:form action="/PbmPharmacyGeneralAction.do" enctype="multipart/form-data" method="POST">
<html:errors/>
<div id="sideHeading" style=" background-color: rgb(67, 128, 74);">Claim Submission Screen</div>



<logic:notEmpty name="successMsg" scope="request">
				<table align="center" class="successContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="successMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
			<logic:notEmpty name="errorMsg" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="errorMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
<%if(request.getSession().getAttribute("preAuthSeqId")!=null){ %>			
<div id="sideHeadingSmall" style="margin-left: 0px;">Pre-Approval Details</div>
<br>

<table class="dashboardTable" id="preAuthDetailId" style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
    <tr>
  <th>Pre-Approval No.</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="preAuthNO"/></td>
  <th>Transaction Date</th><th>:</th><td><bean:write name="frmPbmPreauthGeneral" property="transactionDate"/></td>
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
<%} %>
<%if(claimStatus!=null){ %><br/><br/>
 <div id="sideHeadingSmall" style="margin-left: 0px;">Claim Details</div><br/>
 <table class="dashboardTable" style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
     <tr>
     	<th>Claim Number</th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="claimNumber"/></td>
     	<th>Transaction Date & Time</th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="transactionDt"/></td>
     	<th>Invoice Number</th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="invoiceNumber"/></td>
   	</tr>
     <tr>
     <th>Card Holder Name</th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="memberName"/></td>
     <th>Policy No.</th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="policyNo"/></td>
     <td>
   	</tr>
     <tr>
     <th>Clinician Id </th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="clinicianId"/></td>
     <th>Clinician Name</th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="clinicianName"/></td>
     <th>Decision Date & Time </th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="decisionDt"/></td>
   	</tr>
     <tr>
     <th>Status </th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="clmStatus"/></td>
     <th>Claimed Amount </th><th>:</th><td><bean:write name="frmPbmClaimSuccess" property="claimedAmt"/></td>
       <td></td>
       <td></td>
   	</tr>
   	
</table>
 <%} %>
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
 <!-- <th style="width: 1%;" title="Serial NO">S.NO</th> -->
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
 <!--  <th style="width: 4%;" title="eRX ref">eRX ref</th> -->
  <!-- <th style="width:10%;" title="eRX Instructions">eRX Instructions</th> -->
  <th style="width: 1%;" title="checkAll"><input type="checkbox" name="drugCheckAll" onclick="drucCheckAll(this);"></th>
  
 </tr>
 
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allDrugDetails">
 <logic:iterate id="drugDetails" name="frmPbmPreauthGeneral" property="allDrugDetails">
 <% if("PBMClaim".equals(TTKCommon.getActiveLink(request))){ %>
 <tr>
   <%-- <td><bean:write name="drugDetails" property="serialNo"/></td> --%>
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
   <%-- <td><bean:write name="drugDetails" property="erxRef"/></td> --%>
  <%--  <td><bean:write name="drugDetails" property="erxInstruction"/></td> --%>  
  <td style="background-color: #F0F0F0;"><input type="checkbox" name="drucCheck" value="<bean:write name="drugDetails" property="activityDtlSeqId"/>"></td>
   	
   </tr>
 
 <%}else{ %>
 <logic:equal name="drugDetails" property="activityStatus" value="Approved">
   <tr>
   <%-- <td><bean:write name="drugDetails" property="serialNo"/></td> --%>
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
   <%-- <td><bean:write name="drugDetails" property="erxRef"/></td> --%>
  <%--  <td><bean:write name="drugDetails" property="erxInstruction"/></td> --%>  
  <td style="background-color: #F0F0F0;"><input type="checkbox" name="drucCheck" value="<bean:write name="drugDetails" property="activityDtlSeqId"/>"></td>
   	
   </tr>
   </logic:equal>
   <%} %>
 </logic:iterate>
 
 <tr style="height:30px">
  <!--  <td style="width: 1%;"></td> -->
   <td style="width: 28%;background-color: #F0F0F0;"></td>
   <td style="width:5%;background-color: #F0F0F0;" ></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalReqAmtforApp"/></td>
   <td style="width: 8%;text-align:left;background-color: #F0F0F0;"><b>Total:</b>&nbsp;&nbsp;<bean:write name="frmPbmPreauthGeneral" property="totalApprAmt"/></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 4%;background-color: #F0F0F0;"></td>
   <td style="width: 8%;background-color: #F0F0F0;"></td>
  <td style="width: 18%;background-color: #F0F0F0;"></td>
   <td style="width: 6%;background-color: #F0F0F0;"></td> 
   <!-- <td style="width: 4%;background-color: #F0F0F0;"></td> -->
  <!--  <td style="width:10%;"></td> -->
   <td style="width:1%;background-color: #F0F0F0;"></td>
   </tr>
   </logic:notEmpty>
 </table>

<%if(claimStatus==null){%>
 <table style="margin-top:35PX; margin-bottom:35PX;border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;height:40PX">
 <tr>
 <th align="left">Invoice Number</th><th align="left">:
 <span class="mandatorySymbol">*</span>
 </th>
  <td align="left">
  <html:text name="frmPbmPreauthGeneral" property="invoiceNo" style="border:2PX solid black;" styleClass="textBoxMoreMedium"/>
  </td>
  <td></td>
  <td></td>
   <td></td>
 </tr>
 </table>
 <%} %>

 <div>
 <% if(preAuthStatus!=null&&claimStatus==null){%>
 <fieldset style="margin-top:60PX; margin-left: 10PX; margin-bottom: 80PX;margin-right:10PX">
<legend>Upload</legend>
<!-- <a href="#" onClick="javascript:onCommonUploadDocuments('PBM|CLM');">Upload Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->	
<table style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;height:50PX">
	<tr><td  width="30%">Type:<!-- </td>
		<td> -->	<html:select name="frmPbmPreauthGeneral" property="fileTypeUpload" styleClass="selectBox selectBoxMedium">
		           <html:option value="">--Select from list--</html:option>
                 			<option value="PBMC">Prescription Document </option>
            </html:select>
	    </td>
	 
	  <td class="formLabel"   width="40%">Browse  File:<!-- <span class="mandatorySymbol">*</span> --><!-- </td>
	  <td> --><html:file name="frmPbmPreauthGeneral" property="pdfUploadFile"/> 
	   </td>

    <!--  <td   width="45%" >
	<button type="button" name="Button" accesskey="u" class="buttons"    onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUploadDocs();">UPLOAD</button>&nbsp;
	</td> -->
      </tr>
	</table>
</fieldset>
 <%}%>
</div>
	
	<%
	if(preAuthStatus==null){
	if(claimStatus!=null){
	%>
	<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|CLM');">View Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
 <%}}else if(preAuthStatus!=null){%>
<a href="#" onClick="javascript:onCommonUploadDocuments('PBM|PAT');">View Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%} %>
 <br>
<div align="center">
 
 <table>
 
 <tr>
 <td></td>
 <td></td>
 <td>
 <!-- <button name="mybutton" class="olbtn" accesskey="c" onclick="onBack();" type="button"><u>C</u>lose</button> -->
 <input type="submit" class="olbtn" accesskey="c" onclick="return onBack(this);" value="Close"/> 
 </td>
 <td></td>
 <%
 if(TTKCommon.getActiveLink(request).equals("PBMPL")){
 if(claimStatus==null){
	 if(viewmode==true){%>
  <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onPBMSubmitClaim();" type="button"><u>S</u>ubmit Claim</button>
 </td>
 <%}}} else if(TTKCommon.getActiveLink(request).equals("PBMPreauth")){ 
 
 if(claimStatus==null){
	 if(viewmode==true){%>
  <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onPBMSubmitClaim();" type="button"><u>S</u>ubmit Claim</button>
 </td>
 <%}}}else{  
 if(claimStatus==null){
	 if(viewmode==true){%>
  <td colspan="7"><button name="mybutton" class="olbtnLarge" accesskey="d" onclick="onPBMSubmitClaim();" type="button"><u>S</u>ubmit Claim</button>
 </td>
 
 <%}}} %>
  
  <td></td>
  
 </tr>
 </table>
</div>




<input type="hidden" name="mode" value="">
<%if(preAuthStatus==null&& claimStatus!=null){ %>
<input type="hidden" name="claimSeqId" id="seqId" value="<%= request.getSession().getAttribute("claimSeqId")%>">
<%}else if(preAuthStatus!=null && claimStatus==null){ %>
<input type="hidden" name="preAuthSeqId" id="seqId" value="<%= request.getSession().getAttribute("preAuthSeqId")%>">
<%} %>
<input type="hidden" name="leftlink" value="">
<input type="hidden" name="sublink" value="">
<input type="hidden" name="tab" value="">
<input type="hidden" name="child" value="">
<input type="hidden" name="loginType" value="PBM">
<html:hidden property="preAuthSeqID" styleId="preAuthSeqID" name="frmPbmPreauthGeneral"/>

</html:form>
</div>

<%-- <html:hidden property="submissionFlag" styleId="submissionFlag" name="frmPbmPreauthGeneral"/> --%>


