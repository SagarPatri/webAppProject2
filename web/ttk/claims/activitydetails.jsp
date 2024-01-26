<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>

<!DOCTYPE html> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Activity Details</title>
  <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
  <script type="text/javascript" src="/ttk/scripts/claims/activitydetails.js"></script>	
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript">
var JS_Focus_Disabled =true;
var JS_SecondSubmit=false;

</script>
<script language="javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusObj"))%>";

</script>
<style>
#divParent{
    position:absolute;
}
#icdDivID{
position: absolute;
}
#icdDescDivID{
position: absolute;
}
#activityCodeDivID{
position: absolute;
}
#internalCodeDivID{
position: absolute;
}
#activityDescDivID{
position: absolute;
}
#drugCodeDivID{
position: absolute;
}
#drugDescDivID{
position: absolute;
}
#clinicianNameDivID{
position: absolute;
}
#clinicianDivID{
position: absolute;
}

.sdMainDiv{
position: absolute;
}
.sdPageLink{
padding: 1px;
border: 1px solid gray;
background-color: #c0e710;
color: gray;
display: inline;
font-size: 11px;
}
.sdCurrentPageLink{
padding: 1px;
border: 1px solid gray;
background-color: #bdb76b;
color: blue;
display: inline;
font-size: 11px;
max-width:5px; 
}
.sdPageBackward{
padding: 1px;
border: 1px solid gray;
background-color: #c0e710;
color: gray;
display: inline;
font-size: 11px;
font-weight: bold;
}
.sdPageBackward:HOVER{
color: blue;
cursor: pointer;
}

.sdPageForward{
padding: 1px 2px 1px 2px;
border: 1px solid gray;
background-color: #c0e710;
color: gray;
display: inline;
font-size: 11px;
font-weight: bold;
}

.sdPageForward:HOVER{
color: blue;
cursor: pointer;
}

.sdPageLink:HOVER {
	color: blue;
	cursor: pointer;
}
.sdPageLinks{
padding: 0;
border: 0;
width: 200px;
}
.sdTextDiv{
padding: 1px;
border: 1px solid gray;
font-size: 9px;
color:black;
font-stretch: 
}
.sdTextDiv:HOVER {
	color: blue;
	font-weight: bold;
	cursor: pointer;
}
.sdmTextDiv{
background-color: rgb(91, 204, 128);
 overflow-x: hidden; 
 overflow-y: scroll;
height: 70px;


}


.sdCloseBtn{
	cursor: pointer;
	padding: 2px;
	color: blue;
	display: inline;
	font-size: 11px;
	text-align: right;
    background-color: #c0e710;
    border: 1px solid gray;
    box-shadow: 3px 3px 2px #888888;
    border-radius: 8px;
}
.sdCloseBtn:HOVER {
	color: red;
}

#activityDescDivID{
position: absolute;
}
</style>
</head>
<body>
<%
pageContext.setAttribute("denialDescriptions", Cache.getCacheObject("denialDescriptions"));
pageContext.setAttribute("modifiers", Cache.getCacheObject("modifiers"));
pageContext.setAttribute("unitTypes", Cache.getCacheObject("unitTypes"));
pageContext.setAttribute("overrideRemarksList",Cache.getCacheObject("overrideRemarksList"));
boolean dateStatus=request.getSession().getAttribute("dateStatus")==null?false:new Boolean(request.getSession().getAttribute("dateStatus").toString());
boolean tariffStatus=false;
boolean providerNetAmtStatus=false;
pageContext.setAttribute("overrideRemarksMain",Cache.getCacheObject("overrideRemarksMain"));
String dflag=(String)request.getSession().getAttribute("dflag");
%>

<logic:equal value="ECL" property="modeOfClaim" name="frmClaimGeneral" scope="session">
<%providerNetAmtStatus=true;%>
</logic:equal>
<logic:equal name="frmActivityDetails" property="tariffYN" value="Y">
	<logic:equal value="N" name="frmActivityDetails" property="alAhliYN">
			<%tariffStatus=true;%>
	</logic:equal>
	<logic:equal value="Y" name="frmActivityDetails" property="alAhliYN">
		<logic:equal value="ACT" name="frmActivityDetails" property="activityType">
			<%tariffStatus=true;%>
		</logic:equal>	
	</logic:equal>
</logic:equal>

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				    <td width="57%">Activity Details-<logic:empty name="frmActivityDetails" property="activityDtlSeqId">New</logic:empty><logic:notEmpty name="frmActivityDetails" property="activityDtlSeqId">Edit</logic:notEmpty>[<bean:write name="frmActivityDetails" property="activityCode"/>]</td>
					<td  align="right" class="webBoard"></td>
			  </tr>
		</table>
<div class="contentArea" id="contentArea">
<html:errors/>
<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
</logic:notEmpty>
<logic:notEmpty name="activityTariffStatus" scope="request">
			<table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/warning.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:write name="activityTariffStatus" scope="request"/>
			  	  </td>
				</tr>
			</table>
		</logic:notEmpty>
<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" alt="Error" width="16" height="16" align="absmiddle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	
	<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
		 	
<html:form action="/SaveActivityDetailsAction.do">
<fieldset>
		<legend>Activity Details</legend>
		<fieldset>
		     <legend>General</legend>
				<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">				
			<tr>
			    <td width="22%" class="formLabel">Claim No.:</td>
			    <td width="30%" class="textLabelBold">
			    <html:text size="30" readonly="true" name="frmActivityDetails" property="claimNo" styleClass="disabledBox"  />
			    <html:hidden property="preAuthSeqID" name="frmActivityDetails" />
			    <html:hidden property="claimSeqID" name="frmActivityDetails" />
			    <html:hidden property="activitySeqId" name="frmActivityDetails" />
			    <html:hidden property="activityDtlSeqId" name="frmActivityDetails" />
			    <input type="hidden" name="providerSeqID" value="<bean:write name="frmClaimGeneral" property="providerSeqId"/>">
			   		  
			    </td>
			    <td width="22%" class="formLabel">
			    <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">
			      Override
			    </logic:equal>
			    </td>
			    <td>
			     <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">
			       <html:checkbox name="frmActivityDetails" property="overrideYN" styleId="overrideYN"  value="Y" />
			    </logic:equal>			   
			    </td>
			    </tr>
			     <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">
			    	<tr>
			    		<td class="formLabel" colspan="1">Override Remarks:<span class="mandatorySymbol">*</span> </td>
					  	<td nowrap>
					  		<%-- <html:textarea name="frmActivityDetails" property="overrideRemarks" rows="2" cols="80" styleId="overrideDesc"  /> --%>
					  		<%-- <html:select property ="overrideRemarks" name="frmActivityDetails" styleClass="selectBox selectBoxListSecondLargest">
           					<html:option value="">Select from list</html:option>
           						<html:options collection="overrideRemarksList" property="cacheId" labelProperty="cacheDesc"/>
   							</html:select> --%>
   							
   							<html:select property ="overrideRemarks" name="frmActivityDetails" styleClass="selectBox selectBoxListSecondLargest" onchange="javascript:displayOverrideRemarksSub();">
           						<html:option value="">Select from list</html:option>
           						<html:options collection="overrideRemarksMain" property="cacheId" labelProperty="cacheDesc"/>
   							</html:select>
   						 </td>	
			    	</tr>
			    	
			    	<tr>
				   		<td class="formLabel" colspan="1">Override Sub Remarks:<span class="mandatorySymbol">*</span> </td>
   						<td nowrap>
   							<html:select property ="overrideSubRemarks" name="frmActivityDetails" styleClass="selectBox selectBoxListSecondLargest">
           						<html:option value="">Select from list</html:option>
           						<html:optionsCollection  property="overrideRemarksSub" label="cacheDesc" value="cacheId" />
   							</html:select>
					   </td>
			
					   <td nowrap>
							  <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">	
								  <button type="button" onclick="addOverrideRemarks();" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'">Add</button>
							 </logic:equal>
					  </td>							 
					</tr>
					 <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">				   	
				   <tr>
				   <td colspan="4">
				   <table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:850px;height:auto;'>
           			 <tr>
           			<!-- <th align='center' class='gridHeader' style="width: 10%;">Override Code</th> -->
           			<th align='center' class='gridHeader'style="width: 85%;">Override Remarks Description</th>
           			<th align='center' class='gridHeader' style="width: 5%;">Delete</th>
           			</tr>
				   <logic:notEmpty name="overRemarksList" scope="session">
				   <logic:iterate id="overrideList" name="overRemarksList" scope="session">
				   <tr>
	                <td align='center' style="width: 10%;border: 1px solid gray;display:none;"><bean:write name="overrideList" property="key"/></td>
	                <td align='left' style="width: 85%;border: 1px solid gray;"><bean:write name="overrideList" property="value"/></td>
	                <td align='center' style="width: 5%;border: 1px solid gray;"><a href='#' onClick="javascript:delOverirdeRemarksDesc('<bean:write name="overrideList" property="key"/>');"><img src='/ttk/images/DeleteIcon.gif' alt='Delete Override Remarks' width='14' height='14' border='0'></a></td>
                  </tr>
				   </logic:iterate>
				   
				   </logic:notEmpty>
				   </table>
				   </td>
				   </tr>
				   </logic:equal>
				   <br>
					<%-- <tr>
					   <td class="formLabel" colspan="1">Other Remarks:<logic:equal name="frmActivityDetails" property="otherRemarksYN" value="Y"><span class="mandatorySymbol">*</span></logic:equal></td>
						  <td class="textLabel" colspan="3">
						  	<html:textarea name="frmActivityDetails" property="otherRemarks" rows="2" cols="80" styleId="otherRemarks" onblur="spaceValidation();" />
						  </td>					 
					</tr> --%>
				</logic:equal>
			<tr>
			   <td class="formLabel" colspan="1">Activity Type: </td>
				  <td class="textLabel" colspan="3">
					<html:select property="activityType" name="frmActivityDetails" styleClass="selectBox selectBoxMedium" onchange="onChangeServiceType();">
						<html:option value="ACT">Activity</html:option> 
						<html:option value="DRG">Drug</html:option>
					</html:select>
				  </td>					 
		  	</tr>			   
			    <tr>
			     <td class="formLabel">Start Date:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel">
					  <table cellspacing="0" cellpadding="0">
			    	    <tr>
			    		<td><html:text name="frmActivityDetails" property="activityStartDate" styleId="activityStartDate" styleClass="textBox textDate" maxlength="10" readonly="<%=dateStatus%>"/><A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmActivityDetails.activityStartDate',document.frmActivityDetails.activityStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a></td>
			    	   </tr>
			        </table>					  
			        </td>
			        <logic:equal value="Y" name="frmActivityDetails" property="networkProviderType">
			         <td class="formLabel">Clinician Id:</td>
			         <td class="textLabel">
			  <table>
			  <tr>
			  <td>
			  <html:text property="clinicianId" styleId="clinicianId" styleClass="textBox textBoxMedium disabledBox" readonly="true"/>	      	
			  </td>
			  <td>
			       <a href="#" accesskey="g"  onClick="javascript:selectClinician()" class="search"> <img src="/ttk/images/EditIcon.gif" alt="Select Clinician" width="16" height="16" border="0" align="absmiddle">&nbsp;</a>
			  </td>
			  </tr>
			  </table>
			  </td>		    
			</logic:equal>	
			 <logic:equal value="N" name="frmActivityDetails" property="networkProviderType">
			   <td class="formLabel">Clinician Name:</td>
			      <td class="textLabel">
			  <html:text property="clinicianName" styleId="clinicianName" styleClass="textBox textBoxMedium disabledBox" readonly="true"/>	      	
			  </td>		    
			 </logic:equal>	  
			  </tr>
				  <tr>				  
					  <td class="formLabel" width="21%">Activity Code:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel" width="21%">
					  <table>
					   <tr>
			           <td>
			           <html:text name="frmActivityDetails" property="activityCode" styleId="activityCode" styleClass="textBox textBoxMedium" onblur="getActivityCodeDetails('ACT');"  /><!--onblur="getActivityCodeDetails();"  -->
			           </td>
			           <td>
			           <logic:equal value="HAAD" property="provAuthority" name="frmClaimGeneral" scope="session">
				           <a href="#" accesskey="g"  onClick="javascript:selectHaadActivityCode()" class="search"> <img src="/ttk/images/EditIcon.gif" alt="Select Haad Activity Code" width="16" height="16" border="0" align="absmiddle">&nbsp;</a>
			           </logic:equal>
	   		           <logic:notEqual value="HAAD" property="provAuthority" name="frmClaimGeneral" scope="session">
				           <a href="#" accesskey="g"  onClick="javascript:selectActivityCode()" class="search"> <img src="/ttk/images/EditIcon.gif" alt="Select Activity Code" width="16" height="16" border="0" align="absmiddle">&nbsp;</a>
			           </logic:notEqual>
			           </td>
			           <td>
			           <div id="activityResult1"><div id="activityResult2"></div></div>
			           </td>
			           </tr>
			         </table>	
					    <html:hidden property="activityCodeSeqId" styleId="activityCodeSeqId"/>
					    <html:hidden property="activityTypeId" styleId="activityTypeId"/>
					  </td>	
					   <td class="formLabel" width="21%">Modifier:</td>
					  <td class="textLabel">
					  <html:select property="modifier" name="frmActivityDetails" styleClass="selectBox selectBoxMedium">
					  <html:option value="">Select from list</html:option>
					   <html:optionsCollection name="modifiers" label="cacheDesc" value="cacheId" />
					  </html:select>
					  </td>				  
				   </tr>
				<logic:equal value="DNTL" property="benefitType" name="frmClaimGeneral"> 
					   <tr>
					   <td class="formLabel" colspan="1">Tooth No. :  </td>
						  <td class="textLabel" colspan="3">
					        <html:text name="frmActivityDetails" property="toothNo" styleId="toothNo" styleClass="textBox textBoxSmall" readonly="true"/>
				       			<logic:equal value="N" property="actCarryFwdYN" name="frmActivityDetails" > 
				           			<a href="#" onClick="openListIntX('toothNo','TOOTHNOS')"><img src="/ttk/images/EditIcon.gif" alt="Select Tooth NO" width="16" height="16" border="0" align="absmiddle"></a>
			           			</logic:equal>
			           			
			           			<logic:empty property="actCarryFwdYN" name="frmActivityDetails"><!-- This logic to check for Resubmission and freeze for carried cases from main claim --- Kishor Kumar S H -->
				           			<a href="#" onClick="openListIntX('toothNo','TOOTHNOS')"><img src="/ttk/images/EditIcon.gif" alt="Select Tooth NO" width="16" height="16" border="0" align="absmiddle"></a>
			           			</logic:empty>
					  </td>					 
					  </tr>
					  
					  <logic:equal value="Yes" name="showPopUp" scope="request">
	   					  <logic:equal value="Y" property="toothNoReqYN" name="frmActivityDetails">
							  <logic:equal property="codeFlag" name="frmActivityDetails" value="remember">
									   <script type="text/javascript">  
									  	 openListIntX('toothNo','TOOTHNOS');
									  </script>	
							   </logic:equal>
							   <logic:equal property="codeFlag" name="frmActivityDetails" value="search">
									   <script type="text/javascript">  
									  	 openListIntX('toothNo','TOOTHNOS');
									  </script>	
							   </logic:equal>
						  </logic:equal>
					  </logic:equal>
				</logic:equal>				    
				<tr>
			  		 <td class="formLabel" colspan="1">Description: </td>
				  <td class="textLabel" colspan="3">
				
		    	  	<html:textarea name="frmActivityDetails" property="activityCodeDesc" rows="2" cols="80" styleId="activityCodeDesc" onkeyup="onActivityDescSearch();" />
				     	<div id="activityDescDivID" style="display:none;width: 340px;"></div>
			   
			
				
				  </td>					 
				  </tr>
				    
				   <bean:define id="claimTypeId" property="claimType" name="frmClaimGeneral"/>
				   <bean:define id="paymentToId" property="paymentTo" name="frmClaimGeneral"/>
				  
				  <%if("CTM".equals(claimTypeId) || "PTN".equals(paymentToId)){ %>	
				  
				  <tr>
					 <td class="formLabel" width="21%">Service Code: </td>
					  <td>
			           <html:text name="frmActivityDetails" property="memServiceCode" styleClass="textBox textBoxMedium" />
			           </td>
			           <td class="formLabel" width="21%">Service Description:<span class="mandatorySymbol">*</span> </td>
					  <td>
			           <html:text name="frmActivityDetails" property="memServiceDesc" styleClass="textBox textBoxMedium" />
			           </td>
					</tr> 
				  
				    <%}%>
				 
				 <%--  <logic:equal value="CTM" name="frmClaimGeneral" property="claimType" scope="session"> 
					<tr>
					 <td class="formLabel" width="21%">Service Code: </td>
					  <td>
			           <html:text name="frmActivityDetails" property="memServiceCode" styleClass="textBox textBoxMedium" />
			           </td>
			           <td class="formLabel" width="21%">Service Description:<span class="mandatorySymbol">*</span> </td>
					  <td>
			           <html:text name="frmActivityDetails" property="memServiceDesc" styleClass="textBox textBoxMedium" />
			           </td>
					</tr>  
					</logic:equal>
					
					<logic:equal value="PTN" name="frmClaimGeneral" property="paymentTo" scope="session"> 
					<tr>
					 <td class="formLabel" width="21%">Service Code: </td>
					  <td>
			           <html:text name="frmActivityDetails" property="memServiceCode" styleClass="textBox textBoxMedium" />
			           </td>
			           <td class="formLabel" width="21%">Service Description:<span class="mandatorySymbol">*</span> </td>
					  <td>
			           <html:text name="frmActivityDetails" property="memServiceDesc" styleClass="textBox textBoxMedium" />
			           </td>
					</tr>  
					</logic:equal> --%>
					
					  <tr>
					  <td class="formLabel" width="21%">Unit Type: </td>
					  <td class="textLabel">
					  
					  <logic:equal value="Y" name="frmActivityDetails" property="networkProviderType">				  
					  <html:select property="unitType" name="frmActivityDetails" styleClass="selectBox selectBoxMedium" styleId="UnitType" onchange="calculateNetAmount(this);">
					  <html:option value="">Select from list</html:option>
					   <html:optionsCollection name="unitTypes" label="cacheDesc" value="cacheId" />
					  </html:select>
					  </logic:equal>
					   
					  <logic:notEqual value="Y" name="frmActivityDetails" property="networkProviderType">				  
					  <html:select property="unitType" name="frmActivityDetails" styleClass="selectBox selectBoxMedium" styleId="UnitType">
					  <html:option value="">Select from list</html:option>
					   <html:optionsCollection name="unitTypes" label="cacheDesc" value="cacheId" />
					  </html:select>
					  </logic:notEqual>
					  </td>
					  <td class="formLabel" width="21%">Duration Of Medication(Days):</td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" maxlength="3" property="medicationDays" onkeyup="isNumeric(this);"  styleClass="textBox textBoxSmall"/>
					  </td>
					  </tr>
					  <%boolean viewmode=false;
					  String classtype="textBox textBoxMedium";
					  %>
					  <logic:equal value="CB" property="benefitTyperef" name="frmClaimGeneral">
					 <logic:equal value="CTM" name="frmClaimGeneral" property="claimType" scope="session">
					  
					  <% viewmode=true;
					     classtype = "textBox textBoxMedium disabledBox";
					  %> 
					</logic:equal>		
					 </logic:equal>
				
			  <tr>
			   <td class="formLabel" colspan="1">Internal Code : </td>
				  	<td class="textLabel">
		          	<html:text name="frmActivityDetails" property="internalCode" styleId="internalCode" styleClass="textBox textBoxMedium" onblur="getActivityCodeDetails('INT');"  /><!--onblur="getActivityCodeDetails();"  -->
			  </td>
			  <td class="formLabel">Internal Description : </td>
				  	<td class="textLabel">
				  	<html:textarea name="frmActivityDetails" property="internalDesc" rows="2" cols="40" styleId="internalDesc"  />
			  </td>			 
			  </tr>
			  <tr>
			   <td class="formLabel" colspan="1">Provider Internal Code : </td>
				  	<td class="textLabel">
		          	<html:text name="frmActivityDetails" property="providerInternalCode" styleId="providerInternalCode" styleClass="textBox textBoxMedium" readonly="true"/>
			  </td>
			  <td class="formLabel">Provider internal Description : </td>
				  	<td class="textLabel">
				  	<html:textarea name="frmActivityDetails" property="providerInternalDescription" rows="2" cols="40" styleId="internalDesc"  readonly="true"/>
			  </td>			 
			  </tr>
			  
					  <tr>
					  <td class="formLabel">Quantity:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="quantity" styleId="quantity" disabled="<%=viewmode%>" styleClass="<%=classtype%>" onkeyup="calculateNetAmount(this);"  />
					  </td>
					  <td class="formLabel">Approved Quantity:</td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="approvedQuantity" styleId="approvedQuantity"  styleClass="textBox textBoxSmall"/>
					  </td>
					  </tr>
					  <tr>
			        <td class="formLabel">Posology: </td>
					  <td class="textLabel">					  
					  <html:select property="posology" name="frmActivityDetails" styleClass="selectBox selectBoxMedium">
					  <html:option value="">Select from list</html:option>
					   <html:option value="1">OD</html:option>
                       <html:option value="2">BD</html:option>c
                       <html:option value="3">TDS</html:option>
                       <html:option value="4">QID</html:option>
					  </html:select>
					  </td>
		 <logic:equal value="CTM" name="frmClaimGeneral" property="claimType" scope="session">
					 
					  <td class="formLabel">Provider Requested Quantity:<!-- <span class="mandatorySymbol">*</span> --></td>
					  <td class="textLabel">
			<% if(dflag=="N")  {%>
					  	<html:text name="frmActivityDetails" property="providerQty" styleId="providerQty"  styleClass="textBox textBoxSmall" onkeyup="validateProviderQty(this);" />
					  <%} %>
					  
					  <% if(dflag=="Y")  {%>
					  	<html:text name="frmActivityDetails" readonly="true" property="providerQty" styleId="providerQty"  styleClass="textBox textBoxSmall disabledBox" />
					  <%} %>
					  </td> 
					  </logic:equal>
							
					 <logic:equal value="CNH" name="frmClaimGeneral" property="claimType" scope="session">

					  <td class="formLabel">Provider Requested Quantity:<!-- <span class="mandatorySymbol">*</span> --></td>
					  <td class="textLabel">
					  	<% if(dflag=="N")  {%>
					  	<html:text  name="frmActivityDetails" property="providerQty" styleId="providerQty"  styleClass="textBox textBoxSmall " onkeyup= "validateProviderQty(this);"/>
					 <%} %>
					  
					  <% if(dflag=="Y")  {%>
					  	<html:text name="frmActivityDetails" readonly="true" property="providerQty" styleId="providerQty"  styleClass="textBox textBoxSmall disabledBox" />
					  <%} %>
					  
					  </td>
					  </logic:equal>
					 					  </tr>
					  <tr>
					   <td class="formLabel">Package ID: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="packageId" styleClass="textBox textBoxMedium"  />
					  </td>	
					  <td class="formLabel">Bundle ID: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="bundleId" styleClass="textBox textBoxMedium"  />
					  </td>
					  </tr>
					  </table>
					  </fieldset>
		  <fieldset>
		   <legend>Tariff</legend>
		  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		        <td class="formLabel" width="21%">Unit Price:<span class="mandatorySymbol">*</span> </td>
		        
		        
					  <td class="textLabel">
					  <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">
			          <logic:empty name="frmActivityDetails" property="unitPrice">
			          <html:text name="frmActivityDetails" property="unitPrice" styleClass="<%=classtype%>" onkeyup="calculateNetAmount(this)" />
			          </logic:empty>
			          <logic:notEmpty name="frmActivityDetails" property="unitPrice">
			          <html:text name="frmActivityDetails" property="unitPrice" disabled="<%=viewmode%>" styleClass="<%=classtype%>"  onkeyup="calculateNetAmount(this)" />
			          </logic:notEmpty>
			          <input  id="requestedAmountcurrencyType"  class="textBox textBoxTooTiny textBoxDisabled" readonly="readonly" value="<bean:write name="frmClaimGeneral" property="requestedAmountcurrencyType"/>">
			          </logic:equal>
					  <logic:notEqual value="Y" name="frmActivityDetails" property="overrideYN">
					  <logic:empty name="frmActivityDetails" property="unitPrice">
			          <html:text name="frmActivityDetails" property="unitPrice"   styleClass="<%=classtype%>" onkeyup="calculateNetAmount(this)" />
			          </logic:empty>
			          <logic:notEmpty name="frmActivityDetails" property="unitPrice">
			          <html:text name="frmActivityDetails" property="unitPrice"   styleClass="<%=classtype%>" disabled="<%=viewmode%>" onkeyup="calculateNetAmount(this)" readonly="<%=tariffStatus%>"/>
			          </logic:notEmpty>
			          <input  id="requested
currencyType"  class="textBox textBoxTooTiny textBoxDisabled" readonly="readonly" value="<bean:write name="frmClaimGeneral" property="requestedAmountcurrencyType"/>">
			          </logic:notEqual>
					  </td>
					  <td class="formLabel" width="21%">Unit Discount:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel">
					   <html:text name="frmActivityDetails" property="discountPerUnit"  styleId="discountPerUnit"  styleClass="textBox textBoxMedium disabledBox" readonly="true"/>
					  		  
					  </td>
		 </tr>
		 
		 	 <tr>
		 <td class="formLabel">Converted Unit Price: </td>
		 <td class="textLabel">
			          <html:text name="frmActivityDetails" property="convertedUnitPrice"   styleClass="textBox textBoxMedium disabledBox" readonly="true" />
			          <input  id="convertedUnitPricecurrencyType"  class="textBox textBoxTooTiny textBoxDisabled" readonly="readonly" value="<bean:write name="frmClaimGeneral" property="currencyType"/>">
			          </td>
			          <td> </td>
			          <td> </td>
			          </tr>
		 
	   </table>	
			</fieldset>
	<fieldset>
		  <legend>Provider Billed Amounts</legend>
		  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		   <logic:notEqual value="Y" name="frmActivityDetails" property="overrideYN">
		       <tr>
		        <td class="formLabel" width="21%">Gross Amount:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="grossAmount"  styleClass="textBox textBoxMedium disabledBox"  onkeyup="calculateNetAmount(this)" readonly="true"/>
					  </td>
					   <td class="formLabel" width="21%">Discount:</td>
					  <td class="textLabel">
					  
					  <html:text name="frmActivityDetails" property="discount" styleId="discount"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)" readonly="<%=tariffStatus%>"/>
					  <html:hidden property="tariffYN" styleId="tariffYN" name="frmActivityDetails"/>
					  </td>
					  </tr>
					  <tr>
					  <td class="formLabel" >Discounted Gross:<span class="mandatorySymbol">*</span> </td>
					   <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="discountedGross" styleClass="textBox textBoxMedium disabledBox"  readonly="true" />
					   </td>
					  <td class="formLabel">Co-Pay: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="copay"  onkeyup="calculateNetAmount(this)" styleClass="textBox textBoxMedium" readonly="true" />
					  </td>
				   </tr>
				    <tr>				  
					  <td class="formLabel">Co-Insurance: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" styleId="coinsurance" property="coinsurance"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)" onblur="checkDot(this);"/> 
					  </td>
					  <td class="formLabel">Deductible: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="deductible"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)" readonly="true"/>
					  </td>
				   </tr>
				   <tr>				  
					  <td class="formLabel">Dis Allowed Amount:  </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="outOfPocket"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)"/>
					  </td>
					  <td class="formLabel">Applied flag (Copay & Ded) : </td>
					  <td class="textLabel">
					  	<bean:write name="frmActivityDetails" property="provCopayFlag"/>
					  </td>
				   </tr>
				   <tr>
				   <td class="formLabel">Net Amount:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="netAmount"  styleClass="textBox textBoxMedium disabledBox"  readonly="true" />
					  </td>
					  <td class="formLabel">Patient Share: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="patientShare"  styleClass="textBox textBoxMedium disabledBox"  readonly="true"/>
					  </td>
				   </tr>
				   <tr>
				    <td class="formLabel">Allowed:  </td>
					  <td>
					  	<html:checkbox name="frmActivityDetails" property="amountAllowed"   value="Y" />
					  </td>
					 <td class="formLabel">Provider Requested Amount:<span class="mandatorySymbol">*</span></td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="providerRequestedAmt"  styleClass="textBox textBoxMedium" onkeyup="isNumeric(this);"  readonly="<%=providerNetAmtStatus%>" maxlength="15"/>
					  </td>
				   </tr>
				    </logic:notEqual>
	   <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">
		       <tr>
		        <td class="formLabel" width="21%">Gross Amount:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="grossAmount"  styleClass="textBox textBoxMedium disabledBox"  onkeyup="calculateNetAmount(this)" readonly="true"/>
					  </td>
					   <td class="formLabel" width="21%">Discount:</td>
					  <td class="textLabel">
					  
					  <html:text name="frmActivityDetails" property="discount" styleId="discount"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)" onblur="checkDot(this);"/>
					  <html:hidden property="tariffYN" styleId="tariffYN" name="frmActivityDetails"/>
					  </td>
					  </tr>
					  <tr>
					  <td class="formLabel">Discounted Gross:<span class="mandatorySymbol">*</span> </td>
					   <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="discountedGross" styleClass="textBox textBoxMedium disabledBox"  readonly="true" onblur="checkDot(this);" />
					   </td>
					  <td class="formLabel">Co-Pay: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="copay"  onkeyup="calculateNetAmount(this)" styleClass="textBox textBoxMedium" readonly="true" />
					  </td>
				   </tr>
				    <tr>				  
					  <td class="formLabel">Co-Insurance: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" styleId="coinsurance" property="coinsurance"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)" onblur="checkDot(this);" /> 
					  </td>
					  <td class="formLabel">Deductible: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="deductible"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)" readonly="true"/>
					  </td>
				   </tr>
				   
				   <tr>				  
					  <td class="formLabel">Dis Allowed Amount: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="outOfPocket"  styleClass="textBox textBoxMedium"  onkeyup="calculateNetAmount(this)" onblur="checkDot(this);"/>
					  </td>
					  <td class="formLabel">Applied flag (Copay & Ded): </td>
					  <td class="textLabel">
					  	<bean:write name="frmActivityDetails" property="provCopayFlag"/>
					  </td>
				   </tr>
				   <tr>
				   <td class="formLabel">Net Amount:<span class="mandatorySymbol">*</span> </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="netAmount"  styleClass="textBox textBoxMedium disabledBox"  readonly="true" />
					  </td>
					  <td class="formLabel">Patient Share: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="patientShare"  styleClass="textBox textBoxMedium disabledBox"  readonly="true"/>
					  </td>
				   </tr>
				   <tr>
				    <td class="formLabel">Allowed:  </td>
					  <td>
					  	<html:checkbox name="frmActivityDetails" property="amountAllowed"   value="Y" />
					  </td>
					 <td class="formLabel">Provider Requested Amount:<span class="mandatorySymbol">*</span></td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="providerRequestedAmt" onkeyup="isNumeric(this);" styleClass="textBox textBoxMedium disabledBox"  readonly="true" />
					  </td>
				   </tr>
				    </logic:equal>
				    
			    <tr>
				    <td class="formLabel">Al Koot Remarks:  </td>
					  <td class="textLabel" colspan="3">
					  	<html:textarea property="alkootRemarks" name="frmActivityDetails"  cols="80" rows="2" readonly="true"/>
					  </td>
			   </tr>
			   <tr>
				    <td class="formLabel">Resubmission  Remarks:  </td>
					  <td class="textLabel" colspan="3">
					  	<html:textarea property="resubRemarks" name="frmActivityDetails"  cols="80" rows="2" readonly="true"/>
					  </td>
			   </tr>
			    </table>
			</fieldset>
		<fieldset>
		  <legend>Rule Amounts</legend>
		  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		      	  <tr>
					  <td class="formLabel" width="21%">Co-Pay: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="rcopay" styleClass="textBox textBoxMedium disabledBox" readonly="true"  />
					  </td>
					   <logic:equal value="CTM" name="frmClaimGeneral" property="claimType" scope="session">
					  <td class="formLabel">Non Network Co-Pay: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="nonNetworkCopay" onkeyup="isNumeric(this);" styleClass="textBox textBoxMedium"/>(%)
					  </td>
					  </logic:equal>
					  
				   </tr>
				    <tr>				  
					  <td class="formLabel">Deductible: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="rdeductible" styleClass="textBox textBoxMedium disabledBox" readonly="true"/> 
					  </td>
					   <td class="formLabel" width="21%">Co-Insurance: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="rcoinsurance" styleClass="textBox textBoxMedium disabledBox" readonly="true"/>
					  </td>
					 
				   </tr>
				   <tr>				  
					  <td class="formLabel">Allowed Amount:  </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="allowedAmount"  styleClass="textBox textBoxMedium disabledBox" readonly="true"  />
					  </td>
					   <td class="formLabel" width="21%">Applied flag (Copay & Ded) : </td>
					  <td class="textLabel">
					  	<bean:write name="frmActivityDetails" property="copayDeductFlag"/>
					  </td>
					 
				   </tr>
				   <tr>
				    <td class="formLabel">Approved Amount:  </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="approvedAmount"  styleClass="textBox textBoxMedium disabledBox" readonly="true" />
					  </td>	
					  
					   
					  <td class="formLabel">Out Of Pocket: </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="routOfPocket" styleClass="textBox textBoxMedium disabledBox"  readonly="true"/>
					  </td>
					  
				   </tr>
				   <tr>
					  <td class="formLabel">Dis Allowed Amount:  </td>
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="rdisAllowedAmount"  styleClass="textBox textBoxMedium disabledBox" readonly="true"/>
					  </td>
					  </tr>
					 <logic:equal value="CTM" name="frmClaimGeneral" property="claimType" scope="session">
					   
 			   <logic:equal value="Y" name="frmClaimGeneral" property="enablericopar" scope="session">
 			 
					   <tr>
					  
				    <td class="formLabel">Ri copar:  </td> 
					  <td class="textLabel">
					  	<html:text name="frmActivityDetails" property="ricopar"  styleClass="textBox textBoxMedium disabledBox" readonly="true" />(%) 
					  </td>
					   </tr>
					   </logic:equal>
		
					   
					    <logic:equal value="Y" name="frmClaimGeneral" property="enableucr" scope="session">
					   
					  <tr>	
				    <td class="formLabel">UCR:  </td> 
					  <td class="textLabel">  
					  <html:text name="frmActivityDetails" property="ucr" onkeyup="isNumeric(this);"  styleClass="textBox textBoxMedium" />
					 
					  	
					
					  </td> 
				   </tr>
				   </logic:equal>
				
				</logic:equal>
				   </table>	
				   
			</fieldset>
			
			<fieldset>
		  <legend>Area of Coverage </legend>
		  <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		      	  <tr>
					  <td class="formLabel" width="20%">Co-Pay: </td>
					  <td class="textLabel" width="20%">
					  	<html:text name="frmActivityDetails" property="areaOfCoverCopay" styleClass="textBox textBoxMedium disabledBox" readonly="true"  />
					  </td>
					   <td class="formLabel" width="20%">Deductible: </td>
					  <td class="textLabel" width="20%">
					  	<html:text name="frmActivityDetails" property="areaOfCoverDeduct" styleClass="textBox textBoxMedium disabledBox" readonly="true"/>
					  </td>
					   <td class="formLabel" width="20%">Applied flag (Copay & Ded): </td>
					  <td class="textLabel" width="20%">
					  	<html:text name="frmActivityDetails" property="areaOfCoverFlag" styleClass="textBox textBoxMedium disabledBox" readonly="true"/> 
					  </td>
				   </tr>
				   
				   <tr>
				    <td class="formLabel">Note :  </td>
					  <td class="textLabel" colspan="3">
					  	System takes the discounted gross amount (Disc Grs) for the activity code from the  uploaded tariff at provider level. <br>
					  	 The co-pay and deductible amount is patient share. The calculation is depends on the MIN/MAX/BOTH flag applied at Policy Rule configuration level for the selected activity. <br>
					  	MIN: System compares and fetches the minimum value of ( co-pay and deductible ) amount. <br>
					  	MAX: System compares and fetches the maximum value of (co-pay and deductible) amount.<br>
					  	BOTH:  System first deducts the deductible amount from discounted gross, calculates the co-pay on remaining amount on discounted gross again adds the deductible amount to calculated net amount.<br>
					  	<br>
					  	<strong>Provider Wise copay applicability logic</strong> <br>
				  	 	<strong>Patient share1</strong> = calculating copay/deductible for specific activity based on the configuration at policy rule level.<br>
				  	 	<strong>(Providerwise copay/deductible)</strong>= The calculation is depends on the MIN/MAX/BOTH flag applied at provider level copayment charges for the selected benefit type.<br>
				  	 	<strong>Formula : - </strong>  (Unit discount gross amount - patient share1)=result -(Providerwise copay/deductible)= patient share2.<br>
					  	<br>
					  	Final patient share = patient share1+patient share2 
					  </td>
			   </tr>
				   </table>	
			</fieldset>
			
			<fieldset>
			<legend>Remarks</legend>  
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">	
		 <tr>			  
		<td class="formLabel">Denial Description: </td>
					  <td class="formLabel">
					  <table>
					  <tr>
					  <td nowrap>
					  <html:select property="denialCode" name="frmActivityDetails" styleClass="selectBox selectBoxListSecondLargest">
					  <html:option value="">Select from list</html:option>
					   <html:optionsCollection name="denialDescriptions" label="cacheDesc" value="cacheId" />
					  </html:select>
					  </td>
					  <td>
					  &nbsp;
					   <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">	
					  <button type="button" onclick="addDenialDesc();" name="Button2" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'">A<u>d</u>d</button>
					  </logic:equal>
					  </td>
					  </tr>
					  </table>
					  </td>	
					  <td>Approve(Y/N):</td>
					  <td class="textLabel">
					  <html:select property="activityStatus" name="frmActivityDetails">
					  <html:option value="Y">YES</html:option>
					  <html:option value="N">NO</html:option>
					  </html:select>
					  </td>				 
				   </tr>
				    <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">				   	
				   <tr>
				   <td colspan="4">
				   <table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:850px;height:auto;'>
           			 <tr>
           			<th align='center' class='gridHeader' style="width: 10%;">Denial Code</th>
           			<th align='center' class='gridHeader'style="width: 85%;">Denial Desription</th>
           			<th align='center' class='gridHeader' style="width: 5%;">Delete</th>
           			</tr>
				   <logic:notEmpty name="activityDenialList" scope="session">
				   <logic:iterate id="DenialList" name="activityDenialList" scope="session">
				   <tr>
	                <td align='center' style="width: 10%;border: 1px solid gray;"><bean:write name="DenialList" property="key"/></td>
	                <td align='left' style="width: 85%;border: 1px solid gray;"><bean:write name="DenialList" property="value"/></td>
	                <td align='center' style="width: 5%;border: 1px solid gray;"><a href='#' onClick="javascript:deleteDenialDesc('<bean:write name="DenialList" property="key"/>');"><img src='/ttk/images/DeleteIcon.gif' alt='Delete Denial Code' width='14' height='14' border='0'></a></td>
                  </tr>
				   </logic:iterate>
				   
				   </logic:notEmpty>
				   </table>
				   </td>
				   </tr>
				   </logic:equal>
		<tr>
		   <td class="formLabel">Remarks:</td>
		   <td class="textLabel">
		   <html:textarea property="activityRemarks" name="frmActivityDetails"  cols="60" rows="2" readonly="true"/>
		 </td>
		 <td></td><td></td>
		 </tr>	
		  
					    <logic:equal value="Y" name="frmClaimGeneral" property="enableucr" scope="session">
					   
		 <tr>
		   <td class="formLabel">Disallowance/Denial Reason:</td>
		   <td class="textLabel">
		   <html:textarea property="denialRemarks" name="frmActivityDetails"  cols="60" rows="2" />
		 </td>
		 <td></td><td></td>
		 </tr>
		 </logic:equal>
		
				   <!-- <tr>
				   <td colspan="4" align="center">
				    <button type="button" name="Button1" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="saveActivityDetails()"><u>S</u>ave</button>&nbsp;
				    <button type="button" name="Button1" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="resetActivityDetails()"><u>R</u>eset</button>&nbsp;
		            <button type="button" onclick="closeActivities();" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>&nbsp;			  
				    </td>
				   </tr> -->
		</table>
		
			</fieldset>
			
			<fieldset>
			<legend>Denial Codes Before Overriding</legend>
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">	
	
				    <logic:equal value="Y" name="frmActivityDetails" property="overrideYN">				   	
				   <tr>
				   <td colspan="4">
				   <table border='0' align='center' cellpadding='0' cellspacing='0' class='gridWithCheckBox'  style='width:850px;height:auto;'>
           			 <tr>
           			<th align='center' class='gridHeader' style="width: 25%;">Denial Code</th>
           			<th align='center' class='gridHeader'style="width: 75%;">Denial Desription</th>
           			<!-- <th align='center' class='gridHeader' style="width: 5%;">Delete</th> -->
           			</tr>
				   <logic:notEmpty name="tpaActivityDenialList" scope="session">
				   <logic:iterate id="DenialList" name="tpaActivityDenialList" scope="session">
				   <tr>
	                <td align='center' style="width: 10%;border: 1px solid gray;"><bean:write name="DenialList" property="key"/></td>
	                <td align='left' style="width: 85%;border: 1px solid gray;"><bean:write name="DenialList" property="value"/></td>
	                <%-- <td align='center' style="width: 5%;border: 1px solid gray;"><a href='#' onClick="javascript:deleteDenialDesc('<bean:write name="DenialList" property="key"/>');"><img src='/ttk/images/DeleteIcon.gif' alt='Delete Denial Code' width='14' height='14' border='0'></a></td> --%>
                  </tr>
				   </logic:iterate>
				   
				   </logic:notEmpty>
				   </table>
				   </td>
				   </tr>
				   </logic:equal>	
		</table>
		
			</fieldset>
			
			<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
			<tr>
				   <td colspan="4" align="center">
				    <button type="button" name="Button1" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="saveActivityDetails()"><u>S</u>ave</button>&nbsp;
				    <button type="button" name="Button1" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="resetActivityDetails()"><u>R</u>eset</button>&nbsp;
		            <button type="button" onclick="closeActivities();" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>&nbsp;			  
				    </td>
				   </tr>
			</table>
				   <input type="hidden" name="mode"/>
				   <INPUT TYPE="hidden" NAME="leftlink">
                   <INPUT TYPE="hidden" NAME="sublink">
                   <INPUT TYPE="hidden" NAME="tab">
                   <INPUT TYPE="hidden" NAME="reforward" value="">		
        </fieldset>	
        <html:hidden property="override" name="frmActivityDetails" styleId="override" />	
         <html:hidden property="authType" name="frmActivityDetails" value="CLM" />
         <html:hidden property="networkProviderType" name="frmActivityDetails"	 />
         <html:hidden property="denialDescription" name="frmActivityDetails"/>	 
         <input type="hidden" name="focusObj" value="<%= request.getAttribute("focusObj")%>">  
         <bean:define id="benefitTyperefID" name="frmClaimGeneral" property="benefitTyperef" type="java.lang.String"/>
         <html:hidden property="benefitType" name="frmActivityDetails" value="<%=benefitTyperefID%>"/>
              <%--    <html:hidden property="benefitType" name="frmActivityDetails"	 /> --%>
           <html:hidden property="flowType" name="frmActivityDetails" value="CLM"/>	 
           <logic:equal value="CTM" name="frmClaimGeneral" property="claimType" scope="session"> 
             <html:hidden property="claimtype" value="CTM" />
            </logic:equal>
           <html:hidden property="copayPerc" name="frmActivityDetails" />
			<html:hidden property="copayDeductFlag" name="frmActivityDetails" />	 
         <input type="hidden" id="conversionRate" value="<bean:write name="frmClaimGeneral" property="conversionRate"/>">
          <logic:equal value="PTN" name="frmClaimGeneral" property="paymentTo" scope="session"> 
             <html:hidden property="paymentTo" value="PTN" />
             <input type="hidden" name="partnerName" id="partnerName" value="NA" /> 
            </logic:equal>
           <html:hidden property="overrideRemarksDesc" name="frmActivityDetails"/>
           <html:hidden property="claimReceiveDate" styleId="claimReceiveDate" name="frmActivityDetails"/>	
           <html:hidden property="noOfUnits" name="frmActivityDetails" />
		   <html:hidden property="granularUnit" name="frmActivityDetails" />	
		   <html:hidden property="overrideSubRemarksDesc" name="frmActivityDetails"/>
		   <html:hidden property="claimtype" name="frmActivityDetails" styleId="claimtype"/>
		   <html:hidden property="discountFlag" name="frmActivityDetails" styleId="discountFlag"/>
	</html:form>		
</div>	 			    
</body>
</html>