<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%
//TTKCommon.setActiveLinks("Broker","Home","DashBoard",request);
%>
<head>
<style type="text/css">
#divParent{
    position:relative;
}
.dashboardTable th{
padding: 6px;
text-align: left;
font-size: 11px;
}

.dashboardTable td{
padding: 6px;
text-align: left;
font-size: 11px;
}

.addTable th{
padding: 2px;
text-align: left;
font-size: 11px;
}

.addTable td{
padding: 2px;
text-align: left;
font-size: 11px;
}

.searchResults {
	padding: 0px;
	border: 1px solid #84a10b;
	background-color: #c0e710;	
	position: absolute; 
	width: 618px;	
	display: block;
	overflow: scroll;
	height:300px;
	margin-top: -2px;
	
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
/* #sideHeadingMedium{
 border: 0px solid #91C85F;
    padding: 6px 12px 5px 3px; 
    background-color: #91C85F;
    color: white;
    width: 200px;
font-size: 17px;
text-align: center;    
border-top-right-radius:2em;
border-bottom-right-radius:2em;
margin-top: 10px;
margin-left: -3px;
font-family: Verdana, Arial, Helvetica, sans-serif;
}
 */
</style>
<script type="text/javascript" src="/ttk/scripts/validation.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/common/customeautocomplete.js"></script>
<script type="text/javascript" src="/ttk/scripts/onlineforms/PBMPharmacyLogin/pbmPreauth.js"></script>

<script type="text/javascript">

</script>
</head>
<%pageContext.setAttribute("provDocsType", Cache.getCacheObject("provDocsType")); 

boolean aceessCheck = TTKCommon.isAuthorized(request, "RequestAuthorization");

%>
<div onkeydown="clearSearchData(event)" class="contentArea" id="contentArea">
	<logic:notEmpty name="notify" scope="request">
		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" alt="Alert" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="notify" scope="request"/>
	        </td>
	      </tr>
   	 </table>
   	 </logic:notEmpty>
<html:form action="/PbmPharmacyGeneralAction.do"  method="post" enctype="multipart/form-data">
			<logic:notEmpty name="success" scope="request">
				<table align="center" class="successContainer" style="display:"
					border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success"
							width="16" height="16" align="absmiddle">&nbsp; 
							<bean:write	name="success" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>

<div id="sideHeadingMedium" style=" background-color: rgb(67, 128, 74);">Prior Request</div>
<html:errors/>
<fieldset>
<div id="sideHeadingTooSmall">Eligibility</div>
<br>
<table class="dashboardTable" style="border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
    <tr>
  <th>Al Koot ID<span class="mandatorySymbol">*</span></th><th>:</th>
  <td>
  
  <logic:equal name="flag" value="YES"  >
   <html:text name="frmPbmPreauthGeneral" property="enrolmentID" styleClass="textBoxMoreMedium" />
   </logic:equal>
  <logic:notEqual name="flag" value="YES" >
   <html:text name="frmPbmPreauthGeneral" property="enrolmentID" styleClass="textBoxMoreMedium" readonly="true" disabled="true"/>
  </logic:notEqual>
  </td>
  <th>Qatar ID<span class="mandatorySymbol">*</span></th><th>:</th>
  <td>
   <logic:equal name="flag" value="YES"  >
   <html:text name="frmPbmPreauthGeneral" property="qatarID" styleClass="textBoxMoreMedium"  />
   </logic:equal>
  <logic:notEqual name="flag" value="YES" >
  <html:text name="frmPbmPreauthGeneral" property="qatarID" styleClass="textBoxMoreMedium" readonly="true" disabled="true" />
  </logic:notEqual>
  </td>
  <th>
  Date Of Birth</th><th>:</th>
  <td>
  <html:text name="frmPbmPreauthGeneral" property="dobDate" styleClass="textBoxDate DisabledTextBox" readonly="true"/>
  </td>   
 </tr>
 
  <tr>
  <th>Insurance Company ID</th><th>:</th><td><html:text name="frmPbmPreauthGeneral" property="insCompanyID" styleClass="textBoxMoreMedium "/> </td>
  <th>Clinician ID<span class="mandatorySymbol">*</span></th><th>:</th>
  <td>
  
  <html:text name="frmPbmPreauthGeneral" property="clinicianID" styleId="clinicianID" styleClass="searchBoxMoreMedium" onkeyup="onClinicianSearch();"/>
  <div style="position: relative;">
  <div class="sdMainDiv" id="clinicianDivID"  style="display:none;width: 190px;">
  </div>
  </div>
  </td>

<th>Clinician Name<span class="mandatorySymbol">*</span></th><th>:</th>
<td>
<html:text name="frmPbmPreauthGeneral" property="clinicianName" styleId="clinicianName" styleClass="searchBoxMoreMedium" onkeyup="onClinicianNameSearch();"/>
<div style="position: relative;">
<div class="sdMainDiv"  id="clinicianDivName" style="display:none;width: 190px;">
  </div>
  </div>
</td>
</tr>
 <tr>
  <th>Date Of Prescription<span class="mandatorySymbol">*</span>(dd/mm/yyyy)</th>
  <th>:</th>
  <td>
  <html:text name="frmPbmPreauthGeneral" property="dateOfTreatment" maxlength="10"  styleClass="textBoxDate"  readonly="true"/>
  
  <a NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate"
												HREF="#"
												onClick="javascript:show_calendar('CalendarObjectPARDate','frmPbmPreauthGeneral.dateOfTreatment',document.frmPbmPreauthGeneral.dateOfTreatment.value,'',event,148,178);return false;"
												onMouseOver="window.status='Calendar';return true;"
												onMouseOut="window.status='';return true;"><img
												src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"
												name="dateOfTreatment" width="24" height="17" border="0"
												align="absmiddle"></a>
  </td> 
  <th nowrap>Event Reference No.</th><th>:</th>
	  	 <td><html:text property="eventRefNo" name="frmPbmPreauthGeneral" styleClass="textBoxMoreMedium" onkeyup="isNumeric(this); "maxlength="7"/>  
          	</td> 
  <th colspan="2">
  <button type="button" name="mybutton" accesskey="v"  class="olbtn" onClick="onValidateGeneralDetails();"><u>V</u>alidate</button>
  </th>
   <th colspan="2">
   <button type="button" name="mybutton" accesskey="c"  class="olbtn" onClick="onClose();"><u>C</u>lose</button>
  </th>
  
  <td> <bean:write name="frmPbmPreauthGeneral" property="preAuthSeqID"/></td> 
 <!-- <th><input type="text" name="testPreAuthSeqID" onblur="getPreAuthData();" class="textBoxDate"/></th>
   <th><div id="dispTestID"></div></th>
  <td></td>   -->
 </tr> 
</table>

<logic:notEmpty name="frmPbmPreauthGeneral" property="memSeqID">
<div id="sideHeadingTooSmall">Diagnosis</div>
<br>
 <table class="addTable" style="margin-left: 0;border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
    <tr>
  <th>Diagnosis</th><th>:</th>
  <td>  
  <html:text name="frmPbmPreauthGeneral" property="icdCodeDesc"  onkeyup="onIcdDescSearch();" styleId="icdCodeDesc" styleClass="searchBoxTooLarg" /> 
  <div style="position: relative;">
  <div class="sdMainDiv" id="icdDivID"  style="display:none;width: 700px;">
  </div>
  </div>  
  </td>
  <th>ICD Code</th><th>:</th><td>
  <html:text name="frmPbmPreauthGeneral" property="icdCode" styleId="icdCode" onkeyup="onIcdCodeSearch();"/>
  <div style="position: relative;">
  <div class="sdMainDiv" id="icdCodeDivID"  style="display:none;width: 160px;">
  </div>
  </div>
  
  </td>
  <td><button type="button" name="mybutton" accesskey="a"  class="olbtnSmall" onClick="addDiagnosis();"><u>A</u>dd</button></td>
 </tr> 
 </table>
 
 <logic:empty name="frmPbmPreauthGeneral" property="allIcdDetails">
 <br>
 </logic:empty>
 
  <logic:notEmpty name="frmPbmPreauthGeneral" property="allIcdDetails">
 <div style="margin-left: 65px;margin-top: 10px;width: 75%;">
 <table class="listDisplayTable">
 <tr>
  <th style="width: 65%;">Diagnosis Desc</th>
   <th  style="width: 12%;">ICD Code</th>
    <th style="width: 18%;">Diagnosis Type</th>
     <th style="width: 5%;">Delete</th>
 </tr>

 <logic:iterate id="diagnDetails" name="frmPbmPreauthGeneral" property="allIcdDetails" indexId="icdIndexId">
   <tr>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="ailmentDescription"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="icdCode"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="diagnDetails" property="primaryAilment"/></td>
   <td style="background-color: #F0F0F0;">
   <a href='#' onClick="javascript:deleteDiagnosisDetails('<bean:write name="icdIndexId"/>','<bean:write name="diagnDetails" property="icdCode"/>');">
   <img src='/ttk/images/DeleteIcon.gif' title="Delete Diagnosis Details" alt='Delete Diagnosis Details' width='16' height='16' border='0'>
   </a>
   </td>		
   </tr>
 </logic:iterate>

 </table>
 </div>
  </logic:notEmpty>
  
   <logic:notEmpty name="frmPbmPreauthGeneral" property="allIcdDetails">
   
 <div id="sideHeadingTooSmall">Drugs</div>
 <br>
 <table class="addTable" style="margin-left: 0;border: 0px solid gray;padding: 10px 20px 10px 20px;background-color: #F0F0F0;width: 96%;">
    <tr>
  <th>Drug&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th><th>:</th>
  <td>
    <html:text name="frmPbmPreauthGeneral" property="drugDesc" onkeyup="onDrugDescSearch();" styleId="drugDesc" styleClass="searchBoxTooLarg" /> 
	 <div style="position: relative;">
	  <div class="sdMainDiv" id="drugDivID" style="display:none;width: 700px;">
	  </div>
	  </div>
	 
  </td>
  <th>Quantity</th><th>:</th><td>
  <html:text name="frmPbmPreauthGeneral" property="quantity" styleClass="textBoxTooSmall" onkeyup="isNumeric(this);"/>
  </td>
  <th>Units</th><th>:</th><td>
  <html:select name="frmPbmPreauthGeneral" property="unitType">
  <html:option value="PCKG">Package</html:option>
  <%-- <html:option value="">Select</html:option> --%>
  <html:option value="LOSE">Loose</html:option>
  </html:select>  
  </td>
  <th>Days</th><th>:</th><td>
  <html:text name="frmPbmPreauthGeneral" property="days" styleClass="textBoxTooSmall" onkeyup="isNumeric(this);"/>
  </td>
  <td><button type="button" name="mybutton" accesskey="d"  class="olbtnSmall" onClick="addDrug();">A<u>d</u>d</button></td>
 </tr>
 </table>
 
  <logic:notEmpty name="frmPbmPreauthGeneral" property="allDrugDetails">
  
 <div style="margin-left: 35px;margin-top: 10px;width: 92%;">
 <table class="listDisplayTable">
 <tr>
 <th style="width: 1%;">S.NO</th>
  <th style="width: 50%;">Drug Desc</th>
   <th style="width: 10%;">Gross Price</th>
    <th style="width: 8%;">Disc Amt</th>
    <th style="width: 8%;">Net Amt</th>
    <th style="width: 10%;">Qty Claimed</th>
<!--     <th style="width: 8%;">Total Amt</th> -->
     <th style="width: 5%;">Delete</th>
 </tr>

 <logic:iterate id="drugDetails" name="frmPbmPreauthGeneral" property="allDrugDetails" indexId="drugIndexId">
   <tr>
   <td style="background-color: #F0F0F0;" ><%=(drugIndexId+1) %></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="activityCodeDesc"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="grossAmount"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="discount"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="netAmount"/></td>
   <td style="background-color: #F0F0F0;"><bean:write name="drugDetails" property="quantity"/></td>
<%--    <td><bean:write name="drugDetails" property="approvedAmount"/></td>    --%>
   <td style="background-color: #F0F0F0;">
  <a href='#' onClick="javascript:deleteDrugDetails('<bean:write name="drugIndexId"/>','<bean:write name="drugDetails" property="activityCodeDesc"/>');">
  <img src='/ttk/images/DeleteIcon.gif' title='Delete Diagnosis Details' alt='Delete Diagnosis Details'width='16' height='16' border='0'>
   </a>
   </td>		
   </tr>
 </logic:iterate>

 </table>
 </div> 
  </logic:notEmpty> 
  
  </logic:notEmpty>
  
 <br>
 <div>
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allDrugDetails">
 <fieldset>
		<legend>Document Uploads<span class="mandatorySymbol"></span></legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 99%">
			<!-- <tr  style="border:1px solid;"> -->
          		<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description:</td>
          		<td width="18%"    align="left" nowrap>
          			<html:select property ="description" styleClass="selectBox selectBoxMedium">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="provDocsType" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
          	          
            <td align="left">Browse File : </td>
			<td>
				<html:file property="file" styleId="file"/>
			</td>
			<!-- </tr> -->
			  </table>
		</fieldset>	
 </logic:notEmpty>
 </div>
 <br/><br/>
 <div style="text-align: center;">
 
 <button type="button" name="mybutton" accesskey="c"  class="olbtn" onClick="onClearScreen();"><u>C</u>lear Screen</button>&nbsp;
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allIcdDetails">
 <logic:notEmpty name="frmPbmPreauthGeneral" property="allDrugDetails">
 
 <%if(aceessCheck) {%>
  <button type="button" id="processBtn" name="mybutton" accesskey="r"  class="olbtnLarge" onClick="onRequstAuthorization();"><u>R</u>equest Authorization</button>&nbsp;
 <%}else {%>
 
 <%} %>
 
 </logic:notEmpty>
 </logic:notEmpty>
 </div> 
 
</logic:notEmpty>


 </fieldset>
 <br><br><br><br>

<input type="hidden" name="mode" value="">
<input type="hidden" name="tab" value="">
<input type="hidden" name="leftlink" value="">
<input type="hidden" name="sublink" value="">
<input type="hidden" name="linkMode" value="">
<input type="hidden" name="btnMode" value="">
<input type="hidden" name="indexID">

<html:hidden property="clinicianSeqID" styleId="clinicianSeqID" name="frmPbmPreauthGeneral"/>
<html:hidden property="icdCodeSeqID" styleId="icdCodeSeqID" name="frmPbmPreauthGeneral"/>
<html:hidden property="drugCodeSeqID" styleId="drugCodeSeqID"  name="frmPbmPreauthGeneral"/>
<html:hidden property="drugCodeQuantity" styleId="drugCodeQuantity"  name="frmPbmPreauthGeneral"/>
<html:hidden property="memSeqID" styleId="icdCodeSeqID" name="frmPbmPreauthGeneral"/>
<html:hidden property="preAuthSeqID" styleId="preAuthSeqID" name="frmPbmPreauthGeneral"/>
<html:hidden property="preAuthStatus" styleId="preAuthStatus" name="frmPbmPreauthGeneral"/>
<html:hidden name="frmPbmPreauthGeneral" property="memGender" styleId="memGender"/>
<html:hidden property="flag" name="frmPbmPreauthGeneral"/>
<html:hidden property="preAuthNO" name="frmPbmPreauthGeneral"/>
<html:hidden property="noOfUnits" name="frmPbmPreauthGeneral" styleId="noOfUnits"/>
<html:hidden property="granularUnit" name="frmPbmPreauthGeneral" styleId="granularUnit"/>
<INPUT TYPE="hidden" NAME="loginType" VALUE="PBM">
 <script type="text/javascript">  
 document.forms[1].setAttribute( "autocomplete", "off" );      
  </script>	
</html:form>

</div>