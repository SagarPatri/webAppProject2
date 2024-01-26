 <%
/** @ (#) onlinepreauthEnhance.jsp 
 * Project     : TTK Healthcare Services
 * File        : addLaboratories.jsp
 * Author      : kishor kumar 
 * Company     : RCS Technologies
 * Date Created: 13/04/2017
 *
 * @author 		 : kishor kumar
 * Modified by   : Vishwabandhu Kumar
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
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper,org.apache.struts.action.DynaActionForm"%>

<script language="javascript" src="/ttk/scripts/hospital/onlinepreauth.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/common/customeautocomplete.js"></script>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<head>
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	<script>
			var JS_Focus_Disabled =true;
			var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusId"))%>";
	</script>
	
	<style type="text/css">
	#divParent{
    position:relative;
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
	</style>
</head>
<%
	boolean viewmode=false;
	boolean bEnabled=false;

	pageContext.setAttribute("encounterType", Cache.getCacheObject("encounterTypes"));
	pageContext.setAttribute("treatmentType", Cache.getCacheObject("treatmentTypes"));
	pageContext.setAttribute("consultTypeCode", Cache.getCacheObject("consultTypeCode"));
	pageContext.setAttribute("specialityTypeCode", Cache.getCacheObject("specialityType"));
	pageContext.setAttribute("provDocsType", Cache.getCacheObject("provDocsType"));
	pageContext.setAttribute("dentalTreatmentTypes", Cache.getCacheObject("dentalTreatmentTypes"));
	
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	String readonly	=	(String)request.getAttribute("readonly");
	pageContext.setAttribute("natureOfConception",	Cache.getCacheObject("natureOfConception"));
	pageContext.setAttribute("systemOfMedicines",Cache.getCacheObject("systemOfMedicines"));
	pageContext.setAttribute("modeofdelivery",	Cache.getCacheObject("modeofdelivery"));
	pageContext.setAttribute("activityServiceTypes", Cache.getCacheObject("activityServiceTypes"));
	pageContext.setAttribute("activityServiceCodes", Cache.getCacheObject("activityServiceCodes"));

	if("readonly".equals(readonly))
		viewmode	=	true;

	pageContext.setAttribute("descriptionCode", Cache.getCacheObject("provDocsType"));
%>

<html:form action="/OnlinePreAuthAction.do" method="post" enctype="multipart/form-data" styleId="testId">
		<div class="contentArea" id="contentArea">
			<!-- S T A R T : Content/Form Area -->

<h4 class="sub_heading">Online Pre-Auth Enhance</h4>
<br>
	<!-- S T A R T : Page Title -->
	<html:errors/>
	<!-- E N D : Page Title --> 
	<!-- S T A R T : Form Fields -->
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
		<logic:notEmpty name="clinicianStatus" scope="request">
             <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><img src="/ttk/images/warning.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
                             <bean:write name="clinicianStatus" scope="request"/>
                    </td>
                  </tr>
             </table>
        </logic:notEmpty>
		
	    <span class="fieldGroupTabHeader"> &nbsp;&nbsp;Pre-Approval Details&nbsp;&nbsp;&nbsp;</span><br>
	    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0"> <tr> 
	    	<td width="25%" class="formLabel"> Pre-Approval No. </td>
	    	<td width="25%"> : <bean:write name="frmOnlinePreAuth" property="preAuthNo"/> </td>
	    	<td width="25%" class="formLabel"> Submission Date & Time: </td>
	    	<td width="25%"> : <bean:write name="frmOnlinePreAuth" property="submissionDt"/> </td>
	    </tr> <tr>
	    	<td width="25%" class="formLabel"> Reference No. </td>
	    	<td width="25%"> : <bean:write name="frmOnlinePreAuth" property="preAuthRefNo"/> </td>  
	    	<td width="25%" class="formLabel"> Decision Date & Time: </td>
	    	<td width="25%"> : <bean:write name="frmOnlinePreAuth" property="decisionDt"/> </td>
<logic:notEmpty name="frmOnlinePreAuth" property="preAuthNo"> 		<!-- Once PreAuth Submitted show only Form Data -->	    
     <tr>
	   <td width="25%" class="formLabel">Authorization No. </td>
       <td width="25%">: <bean:write name="frmOnlinePreAuth" property="authorizationNo"/> </td>
       <td width="25%" class="formLabel">Pre-Approval Amount </td>
       <td width="25%">: <bean:write name="frmOnlinePreAuth" property="totalAgreedRate"/> </td>
	</tr>
</logic:notEmpty>       
	</table>
	<br>
	
	
	
  		<span class="fieldGroupTabHeader"> &nbsp;&nbsp;Patient Details&nbsp;&nbsp;&nbsp;</span>
		  <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
	    	<tr>
		        <td width="25%" class="formLabel">Al Koot ID </td>
		        <td width="25%">: <bean:write name="frmOnlinePreAuth" property="enrollId"/> </td>
		        <td width="25%" class="formLabel">Card Holder Name</td>
		        <td width="25%">: <bean:write name="frmOnlinePreAuth" property="memberName"/> </td>
	      	</tr>
	      	<tr>
		        <td width="25%" class="formLabel">Date of Birth</td>
		        <td width="25%"> : <bean:write name="frmOnlinePreAuth" property="memDob"/> </td>
		        <td width="25%" class="formLabel">Gender </td>
		        <td width="25%"> : <bean:write name="frmOnlinePreAuth" property="gender"/> </td>
	      	</tr>
	      	<tr>
		        <td width="25%" class="formLabel">Qatar ID</td>
		        <td width="25%"> : <bean:write name="frmOnlinePreAuth" property="emirateID"/> </td>
	      	</tr>
	    </table>
	    <br>
	    <br>
	    
	    
	    
	    <span class="fieldGroupTabHeader"> &nbsp;&nbsp;Hospitalization Details&nbsp;&nbsp;</span>
		  <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 99%">
	    	<tr>
		        <td width="25%" class="formLabel">Event Reference Number </td>
		        <td width="25%">: <html:text property="eventReferenceNo" styleClass="textBox textBoxMedium" maxlength="30" readonly="true"  style="background-color: #EEEEEE;"/> </td>
	      	</tr>   	
	    	<tr>
		        <td width="25%" class="formLabel">Date of Treatment/Admission<span class="mandatorySymbol">*</span> </td>
		        <td width="25%"> 
		       
		        
: <html:text property="treatmentDate" styleClass="textBox textDateNewDesign" maxlength="10" readonly="true"  style="background-color: #EEEEEE;"/>
  <logic:empty  name="frmOnlinePreAuth" property="treatmentDate">
<logic:notEqual value="true" name="readonly">
<A NAME="CalendarObjectPARDate" ID="CalendarObjectPARDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectPARDate','frmOnlinePreAuth.treatmentDate',document.frmOnlinePreAuth.treatmentDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a>
</logic:notEqual>
</logic:empty>
&nbsp;
   				</td>
		        <td width="25%" class="formLabel">Probable Date of Discharge / (For In-Patient) </td>
		        <td width="25%"> 
					
					: <html:text property="dischargeDate" styleClass="textBox textDateNewDesign" maxlength="10" />
					<logic:notEqual value="true" name="readonly">
   					<a name="CalendarObjectDischargeDate" id="CalendarObjectDschargeDate" href="#" onClick="javascript:show_calendar('CalendarObjectDischargeDate','frmOnlinePreAuth.elements[\'dischargeDate\']',document.frmOnlinePreAuth.elements['dischargeDate'].value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
   						<img src="/ttk/images/CalendarIcon.gif" title="Success" alt="Calendar" name="disDate" width="24" height="17" border="0" align="absmiddle">
   					</a>
					</logic:notEqual> 
				</td>
	      	</tr>
	      	
	      	<tr>
		        
		        <td width="25%" class="formLabel">Name of Treating Doctor
		        <logic:notEqual value="Y" name="frmOnlinePreAuth" property="validateDocNameYN">
		        <span class="mandatorySymbol">*</span> 
		        </logic:notEqual>
		        </td>
		        <td width="25%"> 
		        
		         <logic:equal value="true" name="readonly">
		         : <bean:write name="frmOnlinePreAuth" property="clinicianName"/> 
		        </logic:equal>
		        <logic:notEqual value="true" name="readonly">
		        <div id="divParent">
	        		: <html:text property="clinicianName" styleId="clinicianName" styleClass="textBox textBoxMedium" maxlength="250" onkeyup="onClinicianNameSearch();" readonly="true"  style="background-color: #EEEEEE;"/>
	        		<div class="sdMainDiv" id="clinicianNameDivID"  style="display:none;width: 190px;"></div>
	        	</div>
	        	</logic:notEqual> 
		        </td>
		        <td width="25%" class="formLabel">License No. 
		        <logic:notEqual value="Y" name="frmOnlinePreAuth" property="validateDocNameYN">
		        <span class="mandatorySymbol">*</span> 
		        </logic:notEqual>
		        </td>
		        <td width="25%"> 
				<div id="divParent">
	       			: <html:text property="clinicianId" styleId="clinicianId" styleClass="textBox textBoxMedium" onkeyup="onClinicianSearch()" readonly="true"  style="background-color: #EEEEEE;"/>
			     	 <div id="clinicianDivID" style="display:none;">
			     	 </div>
			     </div>
		        </td>
	      	</tr>
	      	
      	<tr>
	        <td width="25%" class="formLabel">Clinician Specialty 
	        <logic:notEqual value="Y" name="frmOnlinePreAuth" property="validateDocNameYN">
	        <span class="mandatorySymbol">*</span>
	        </logic:notEqual>
	         </td>
	        <td width="25%"> 
	        :
       		<%-- <logic:equal value="true" name="readonly">
					  <bean:write name="frmOnlinePreAuth" property="speciality"/> 
			</logic:equal> 
	        <logic:notEqual value="true" name="readonly">--%>
		        <html:select property ="speciality" styleClass="selectBox selectBoxMedium" disabled="true" style="background-color: #EEEEEE;">
          			<html:option value="">Select from list</html:option>
          			<html:options collection="specialityTypeCode" property="cacheId" labelProperty="cacheDesc"/>
   				</html:select>
	        </td>
        	<td width="25%" class="formLabel">Consultation Type </td>
        	<td width="25%"> 
        	:
        	<html:select property ="consultation" styleClass="selectBox selectBoxMedium" disabled="true"  style="background-color: #EEEEEE;">
       			<html:option value="">Select from list</html:option>
       			<html:options collection="consultTypeCode" property="cacheId" labelProperty="cacheDesc"/>
			</html:select>
	    	</td>
	    	</tr>
		    <tr> 
		        <td width="25%" class="formLabel">Presenting Complaints <span class="mandatorySymbol">*</span></td>
		          <td width="25%"> 
		        <logic:equal value="true" name="readonly">
					<html:textarea property="presentingComplaints" styleClass="textBox textAreaMedium" readonly="true"  style="background-color: #EEEEEE;">
		        	: <bean:write name="frmOnlinePreAuth" property="presentingComplaints"/>
		        	</html:textarea>
					</logic:equal>
					
					<logic:notEqual value="true" name="readonly">
						: <html:textarea property="presentingComplaints" styleClass="textBox textAreaMedium" readonly="true"  style="background-color: #EEEEEE;"/>
					</logic:notEqual> 
				</td>
				<td width="25%" class="formLabel">Duration<span class="mandatorySymbol">*</span> </td>
		        <td width="25%">
		        <bean:define id="benefitTemp" name="frmOnlinePreAuth" property="benifitType"/>	
	         <% if(!"IMTI".equals(String.valueOf(benefitTemp)) && !"In-Patient Maternity".equals(String.valueOf(benefitTemp))
	    		  && !"OMTI".equals(String.valueOf(benefitTemp)) && !"Out-Patient Maternity".equals(String.valueOf(benefitTemp))){
	    	   %>
	        	<%} %>
		        	<logic:equal value="true" name="readonly">
		        	: <bean:write name="frmOnlinePreAuth" property="ailmentDurationText"/> 
					 <bean:write name="frmOnlinePreAuth" property="ailmentDuration"/> 
					</logic:equal>
					
					<logic:notEqual value="true" name="readonly">
					: <html:text property="ailmentDurationText" styleClass="textBox textBoxSmallest" maxlength="10" readonly="true"  style="background-color: #EEEEEE;"/>
						&nbsp;<html:select property ="ailmentDuration" styleClass="selectBox selectBoxSmall" disabled="true" style="background-color: #EEEEEE;">
              			<html:option value="DAYS">Days</html:option>
              			<html:option value="WEEKS">Weeks</html:option>
              			<html:option value="MONTHS">Months</html:option>
              			<html:option value="YEARS">Years</html:option>
       				</html:select> 					</logic:notEqual>      				    								 
       		 </td> 
		    </tr>
		    <tr>
		        <td width="25%" class="formLabel">Details of any Past history relevant to the present illness </td>
		       <td width="25%"> 
		       		<logic:equal value="true" name="readonly">
					<html:textarea property="pastHistory" styleClass="textBox textAreaMedium" readonly="true"  style="background-color: #EEEEEE;">
							: <bean:write name="frmOnlinePreAuth" property="pastHistory"/>
						</html:textarea>							
					</logic:equal>
					
					<logic:notEqual value="true" name="readonly">
						: <html:textarea property="pastHistory" styleClass="textBox textAreaMedium" readonly="true"  style="background-color: #EEEEEE;"/> 
					</logic:notEqual> 
					</td>
			       <td width="25%" class="formLabel">Duration </td>
		       		<td width="25%">
			        <logic:equal value="true" name="readonly">
						: <bean:write name="frmOnlinePreAuth" property="illnessDurationText"/> 
					 <bean:write name="frmOnlinePreAuth" property="illnessDuration"/> 
					</logic:equal>
					
					<logic:notEqual value="true" name="readonly">
			       : <html:text property="illnessDurationText" styleClass="textBox textBoxSmallest" maxlength="10" readonly="true"  style="background-color: #EEEEEE;"/>
						&nbsp;<html:select property ="illnessDuration" styleClass="selectBox selectBoxSmall" disabled="true" style="background-color: #EEEEEE;">
              			<html:option value="DAYS">Days</html:option>
              			<html:option value="WEEKS">Weeks</html:option>
              			<html:option value="MONTHS">Months</html:option>
              			<html:option value="YEARS">Years</html:option>
       				</html:select>			
       				</logic:notEqual> 
				</td>	
	      	</tr>	    
		    	<tr>
	      		<td width="25%" class="formLabel">Benefit Type <span class="mandatorySymbol">*</span> </td>
		        <td width="25%">
		        <logic:notEmpty  name="frmOnlinePreAuth" property="preAuthRefNo">
		        	: <bean:write name="frmOnlinePreAuth" property="benifitTypeCode"/>
		        	<input type="hidden" id="benefitypeCheck" value="<%= benefitTemp%>">
		        </logic:notEmpty> 
		        
		        <logic:empty  name="frmOnlinePreAuth" property="preAuthRefNo">
		        	: <bean:write name="frmOnlinePreAuth" property="benifitType"/>
		        	<input type="hidden" id="benefitypeCheck" value="<bean:write name="frmOnlinePreAuth" property="benifitTypeCode"/> ">
		        </logic:empty> 
		        </td>
		       <% if(!"IMTI".equals(String.valueOf(benefitTemp)) && !"In-Patient Maternity".equals(String.valueOf(benefitTemp))
		    		  && !"OMTI".equals(String.valueOf(benefitTemp)) && !"Out-Patient Maternity".equals(String.valueOf(benefitTemp))){
		    	   %>
	      		<td width="25%" class="formLabel">Treatment Type <span class="mandatorySymbol">*</span></td>
		        <td width="25%">
       				<logic:equal value="true" name="readonly">
					: <bean:write name="frmOnlinePreAuth" property="treatmentType"/> 
					</logic:equal>
					
					<logic:notEqual value="true" name="readonly">					
						<%if("DNTL".equals(benefitTemp) || "Dental".equals(benefitTemp)) {%>
							: 	<html:select property ="treatmentType" styleClass="selectBox selectBoxMedium" onchange="changeTreatment(this)" >
									<html:option value="">--Select from List--</html:option>
									<html:optionsCollection name="dentalTreatmentTypes" label="cacheDesc" value="cacheId" />
							</html:select>   
						<%}else{ %>
							: 	<html:select property ="treatmentType" styleClass="selectBox selectBoxMedium"  disabled="true" style="background-color: #EEEEEE;">
              						<html:options collection="treatmentType" property="cacheId" labelProperty="cacheDesc"/>
              					</html:select>   
						<%} %>
       				</logic:notEqual> 
				</td>
		    	  <%
		       }else if("IMTI".equals(String.valueOf(benefitTemp)) || "In-Patient Maternity".equals(String.valueOf(benefitTemp))){%>
		       
		       <td width="25%" class="formLabel">Mode Of Delivery </td>
		        <td width="25%"> 
		        
						: <html:select property="modeofdelivery" styleClass="selectBox selectBoxMedium"  >
									<html:optionsCollection name="modeofdelivery" label="cacheDesc" value="cacheId" />
							</html:select>
       			</td>
		       
       				<%} %>
       				
	      	</tr>
	      	<tr>	        
				<% if("IMTI".equals(String.valueOf(benefitTemp)) || "In-Patient Maternity".equals(String.valueOf(benefitTemp))
		    		  || "OMTI".equals(String.valueOf(benefitTemp)) || "Out-Patient Maternity".equals(String.valueOf(benefitTemp))){
		    	%>
				 <td width="25%" class="formLabel"> Date of LMP<!-- <span class="mandatorySymbol">*</span> --></td>
				  <td width="25%">
				  : <html:text property="dateOfLMP" styleClass="textBox textDateNewDesign" maxlength="10"  readonly="true"  style="background-color: #EEEEEE;"/>
   					<!-- <a name="CalendarObjectDateOfLMP" id="CalendarObjectDateOfLMP" href="#" onClick="javascript:show_calendar('CalendarObjectDateOfLMP','frmOnlinePreAuth.elements[\'dateOfLMP\']',document.frmOnlinePreAuth.elements['dateOfLMP'].value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
   						<img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="disDate" width="24" height="17" border="0" align="absmiddle">
   					</a> -->
		    	   <%} %>		        
		        </td>		         
		        <% if("IMTI".equals(String.valueOf(benefitTemp)) || "In-Patient Maternity".equals(String.valueOf(benefitTemp))
		    		  || "OMTI".equals(String.valueOf(benefitTemp)) || "Out-Patient Maternity".equals(String.valueOf(benefitTemp))){
		    	   %>
				   <td width="25%" class="formLabel">Nature of Conception<!-- <span class="mandatorySymbol">*</span> --></td>
					<td width="25%">
					  : <html:select property="natureOfConception" styleClass="selectBox selectBoxMedium" disabled="true" style="background-color: #EEEEEE;" >
								<html:optionsCollection name="natureOfConception" label="cacheDesc" value="cacheId" />
						</html:select>
		    	   <%} %>
		        </td>
	      	</tr>
	      	<tr>
	      		<td width="25%" class="formLabel">Encounter Type <span class="mandatorySymbol">*</span> </td>
		        <td width="25%"> 
		        
						: <html:select property ="encounterType" styleClass="selectBox selectBoxMedium" disabled="true" style="background-color: #EEEEEE;">
      					<%-- <html:option value="">Select from list</html:option> --%>
              			<html:optionsCollection property="encounterTypes" label="cacheDesc" value="cacheId"/>
       				</html:select>
       			</td>
       			<td width="25%" class="formLabel">System of Medicine <span class="mandatorySymbol">*</span> </td>
		        <td width="25%"> 
		        
						: <html:select property ="systemOfMedicine" styleClass="selectBox selectBoxMedium" disabled="true" style="background-color: #EEEEEE;">
						<html:optionsCollection name="systemOfMedicines" label="cacheDesc" value="cacheId" />    				</html:select>
       			</td>
	      	</tr>
	      	<tr> 
			<td colspan="4"> 
			<%@include file="/ttk/preauth/dentalDetails.jsp" %>
			</td>
			</tr>
	    </table>
		   <br><br><br>	
		       
	    <span class="fieldGroupTabHeader">  &nbsp;&nbsp;<b>Diagnosis Details</b> &nbsp;&nbsp;</span>
	    <logic:notEqual value="true" name="readonly">
		<a href="#" accesskey="a" onClick="javascript:addDiagnosisDetailsEnhance();"><img src="/ttk/images/AddIcon.gif" title="Add Activity Details" ALT="Add Activity Details" width="13" height="13" border="0" align="absmiddle"></a>
	    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 99%">
	    <tr>
	        <td width="25%" class="formLabel"><b>Diagnosis Description</b><span class="mandatorySymbol">*</span> </td>
	        <td width="25%"> 
	        <div id="divParent">
        		: <html:text property="diagnosisDetailsVO.ailmentDescription" styleId="icdDesc" styleClass="textBox textAreaMedium" onkeyup="onDiagDescSearch();"/>
        		<div id="icdDescDivID"  style="display:none;width: 290px;"></div>
        	</div>
	        </td>
	        <td width="25%" class="formLabel"><b>Diagnosis Code.</b> <span class="mandatorySymbol">*</span> </td>
	        <td width="25%"> 
	        <div id="divParent">
				: <html:text property="diagnosisDetailsVO.icdCode" styleClass="textBox textBoxSmall" styleId="icdCode" onkeyup="this.value = this.value.toUpperCase();onDiagCodeSearch()" />
	       		<!-- <a href="#" accesskey="g"  onClick="javascript:selectClinician()" class="search"> <img src="/ttk/images/EditIcon.gif" alt="Select Clinician" width="16" height="16" border="0" align="absmiddle">&nbsp;</a> -->
 					<div id="icdDivID"  style="display:none;width: 190px;"></div>
 			</div>
	        </td>
	        <html:hidden property="diagnosisDetailsVO.diagnosiscount" styleId="diagnosiscountid"/>
      	</tr>
	      	
		    <tr>    
		        <td colspan="4" align="right"> 
		        	<button type="button" name="Button" accesskey="a" class="olbtnSmall" onClick="javascript:onAddDiagsEnhance('icdDesc');"><u>A</u>dd</button>&nbsp;
		        </td>
		    </tr>
		</table>
		</logic:notEqual>	
		 <ttk:OnlineDiagnosisDetails flow="PAT" preAuthNoYN="E"/>
		<br>
		<br>
		<br>
		<br>
		
		
		
		<span class="fieldGroupTabHeader">  &nbsp;&nbsp;<b>Treatment Details</b> &nbsp;&nbsp;
	    </span>
	<logic:notEqual value="true" name="readonly">
	    <a href="#" accesskey="a" onClick="javascript:addActivityDetailsEnhance();"><img src="/ttk/images/AddIcon.gif" title="Add Activity Details" ALT="Add Activity Details" width="13" height="13" border="0" align="absmiddle"></a> 
	
	   <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 99%">
	   <% if("IMTI".equals(String.valueOf(benefitTemp)) || "In-Patient Maternity".equals(String.valueOf(benefitTemp))
			   || "IPT".equals(String.valueOf(benefitTemp)) || "In-Patient".equals(String.valueOf(benefitTemp))){
   	   %>
	   <tr>
			<td width="10%" class="formLabel"><b>Service Type:</b><span class="mandatorySymbol">*</span> </td>
			<td width="25%">
				: <html:select property="activityDetailsVO.activityServiceType" styleId="activityServiceType" styleClass="selectBox selectBoxMedium" onchange="onChangeServiceType(this);">
				<html:option value="">Select from list</html:option>
				<html:optionsCollection name="activityServiceTypes" label="cacheDesc" value="cacheId" />
				</html:select>
			</td>
	   </tr>
	   <%} %>
	    <tr>
	        <td width="25%" class="formLabel"> 
	        			<label id="service" style="display: none;"><b>Service Description </b></label>
						<label id="activity" style="display: inline;"><b>Activity Description </b> </label>
		   </td>
	        <td width="25%">
	        	<label id="activityDescVal" style="display:inline;">
		       		 <div id="divParent">
							: <html:textarea property="activityDetailsVO.activityCodeDesc" styleId="activityCodeDesc" styleClass="textBox textAreaMedium" onkeyup="onActivityDescSearch()" />
			     			<div id="activityDescDivID" style="display:none;"></div>
			   		 </div>
			    </label>
	     		<label id="serviceDescVal" style="display:none;">
							: <html:textarea property="activityDetailsVO.activityServiceCodeDesc" styleId="activityServiceCodeDesc" styleClass="textBox textAreaMedium" />
			 	</label> 
	     	</td>
			     	
			     	
	     	<td width="25%" class="formLabel">
	     		<label id="serviceCodeId" style="display: none;"><b>Service Code </b></label>
	     		<label id="activityCodeId" style="display: inline;"><b>Activity Code</b> </label>
     		</td>
             
            <td width="25%">
	            <label id="activityCodeVal" style="display:inline;">
		            <div id="divParent">
		      			: <html:text property="activityDetailsVO.activityCode" styleId="activityCode" styleClass="textBox textBoxSmall"/><!--onkeyup="onActivityCodeSearch()"-->
			     	 <div id="activityCodeDivID" style="display:none;">
			     	 </div>
			     	 <%if("DNTL".equals(benefitTemp) || "Dental".equals(benefitTemp)) {%>
	           		<b>Tooth No.</b><html:text property="activityDetailsVO.toothNo" styleId="toothNo" styleClass="textBox textBoxTiny" readonly="true"/>
	           		<a href="#" onClick="openListIntX('toothNo','TOOTHNOS')"><img src="/ttk/images/EditIcon.gif" title="Select Tooth NO" alt="Select Tooth NO" width="16" height="16" border="0" align="absmiddle"></a>
	           	<%} %>
				     </div>
			     </label>
	             <label id="serviceCodeVal" style="display:none;">
			      	: <html:select property="activityDetailsVO.activityServiceCode" styleId="activityServiceCode" styleClass="selectBox selectBoxLargestWeblogin" onchange="onServiceCode(this);">
					 	<html:option value="">Select from list</html:option>
				   		<html:optionsCollection name="activityServiceCodes" label="cacheDesc" value="cacheId" />
				  	</html:select>
			  	</label>
			</td>					  
	        <%-- <td width="10%" class="formLabel">Internal Code </td>
	        <td width="20%" >
		        <label id="activityInternalCode" style="display:inline;">
			        <div id="divParent">
		       			: <html:text property="activityDetailsVO.internalCode" styleId="internalCode" styleClass="textBox textBoxSmall" onkeyup="onInternalCodeSearch()"/>
				     	 <div id="internalCodeDivID" style="display:none;">
				     	 </div>
				     </div>
				</label>    
	     		<label id="serviceInternalCode" style="display:none;">
      				: <html:text property="activityDetailsVO.serviceInternalCode" styleId="serviceInternalCodeVal" styleClass="textBox textBoxSmall" onkeyup="onInternalCodeSearch()"/>
      			</label>
	     	</td> --%>
			     	
		    </tr> 
		    <tr>
		        <%-- <td width="10%" class="formLabel">Quantity </td>
		        <td width="30%">
					: <html:text property="activityDetailsVO.quantity" styleId="activityQuantity" styleClass="textBox textBoxMedium" value="1"/>
		        </td> --%>
		    <td width="10%" class="formLabel"><b>Internal Code </b></td>
	        <td width="20%" >
		        <label id="activityInternalCode" style="display:inline;">
			        <div id="divParent">
		       			: <html:text property="activityDetailsVO.internalCode" styleId="internalCode" styleClass="textBox textBoxSmall" onkeyup="onInternalCodeSearch()"/>
				     	 <div id="internalCodeDivID" style="display:none;">
				     	 </div>
				     </div>
				</label>    
	     		<label id="serviceInternalCode" style="display:none;">
      				: <html:text property="activityDetailsVO.serviceInternalCode" styleId="serviceInternalCodeVal" styleClass="textBox textBoxSmall" onkeyup="onInternalCodeSearch()"/>
      			</label>
	     	</td>
		        <td width="10%" class="formLabel"><b>Unit Price</b></td>
		        <td width="30%">
					: <html:text property="activityDetailsVO.providerRequestedAmt" styleId="providerRequestedAmt" styleClass="textBox textBoxSmall" onkeyup="return isNumeric(this);"/>
		        </td>
		        
		   </tr>  
		   <tr>
		   <td width="10%" class="formLabel"><b>Quantity</b> </td>
		        <td width="25%">
					: <html:text property="activityDetailsVO.quantity" styleId="activityQuantity" styleClass="textBox textBoxSmall" value="1"/>
		        </td>
		   </tr>   
	        <tr>    
		        <td colspan="4" align="right"> 
		        	<button type="button" name="Button" accesskey="a" class="olbtnSmall" onClick="javascript:onSaveActivitiesEnhance('activityCodeDesc');"><u>A</u>dd</button>&nbsp;
		        </td>
		    </tr>
		</table>
		</logic:notEqual>
		
		<ttk:OnlineActivityDetails flow="PAT" preAuthNoYN="E"/>
		<br>
		<br>
		
		
		
		
	    <span class="fieldGroupTabHeader">  &nbsp;&nbsp;<b>Drug Details</b> &nbsp;&nbsp;</span>
 <logic:notEqual value="true" name="readonly">
	    <a href="#" accesskey="a" onClick="javascript:addDrugDetailsEnhance();"><img src="/ttk/images/AddIcon.gif" title="Add Activity Details" ALT="Add Activity Details" width="13" height="13" border="0" align="absmiddle"></a>
	    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 99%">
		      <tr>
		        <td width="25%" class="formLabel"><b>QDC Description</b></td>
			        <td width="25%">
			        <div id="divParent">
					: <html:textarea property="drugDetailsVO.drugDesc" styleId="drugDesc" styleClass="textBox textAreaMedium" onkeyup="onDrugDescSearch()"/> 
			      <div id="drugDescDivID" style="display:none;"></div>
			      </div>
      			</td>
      			
      			<td width="25%" class="formLabel"><b>QDC Code</b></td>
                        <td class="textLabel" width="25%" >
                        <div id="divParent">
			        	: <html:text property="drugDetailsVO.drugCode" styleId="drugCode" styleClass="textBox textBoxMedium" onkeyup="onDrugCodeSearch()"/>
					    <div id="drugCodeDivID" style="display:none;"></div>
					</div>
				</td>
						
		    </tr>
		    
		    <tr>
       			<td width="25%" class="formLabel"><b>Days</b> </td>
		        <td width="25%"> 
		        	: <html:text property="drugDetailsVO.days" styleId="drugdays" styleClass="textBox textBoxSmall" onblur="calcQtyOnPosology()"/><!-- caclQty -->
		        </td>
		    	<td width="15%" class="formLabel"><b>Type of Unit</b> </td>
		        <td width="25%"> 
		        	: <html:select property ="drugDetailsVO.drugunit" styleId="drugUnit" styleClass="selectBox selectBoxMedium" onchange="calcQtyOnPosology()"><!-- checkQty(this) -->
      					<html:option value="LOSE">Unit</html:option>
      					<html:option value="PCKG">Package</html:option>
       				</html:select>
       			</td>
		    </tr>
		    <tr>
       			<td width="25%" class="formLabel"><b>Quantity</b> </td>
		        <td width="25%"> 
		        	: <html:text property="drugDetailsVO.drugqty" styleId="drugquantity" styleClass="textBox textBoxSmall" readonly="readonly"/>
		        </td>
		   </tr>
		   <tr>    
		        <td colspan="4" align="right"> 
		        	<button type="button" name="Button" accesskey="a" class="olbtnSmall" onClick="javascript:onSaveDrugsEnhance();"><u>A</u>dd</button>&nbsp;
		        </td>
		        
		    </tr>
		</table>
		</logic:notEqual>
		<ttk:OnlineDrugDetails flow="PAT" preAuthNoYN="E"/>
		<br>
		
		<!-- Shotfall Docs S T A R T S-->
	<logic:notEmpty name="alShortFallList">
	<fieldset>
		<legend>ShortFall details</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 99%">
	<tr>
				<td class="gridHeader" align="left">Date of Shortfall</td>
				<td class="gridHeader" align="left">Shortfall No.</td>
				<td class="gridHeader" align="left">Shortfall Type</td>
				<td class="gridHeader" align="left">Status</td>
				<td class="gridHeader" align="left">Reply Received</td>
				<!-- <td class="gridHeader" align="left">Remarks</td> -->
				<td class="gridHeader" align="left">View File</td>
			</tr>
		<% int i=0; %>
			<c:forEach var="alData1" items="${sessionScope.alShortFallList}">
			<%
					String strClass=i%2==0 ? "gridOddRow" : "gridEvenRow" ;
					i++;
				%>
		      <tr class=<%=strClass%>>
	      <tr>
	         <c:forEach var="strD" items="${pageScope.alData1}" varStatus="theCount">
	         <td height="20px">
		            <c:set var="test" value="${theCount.index}"/>
		            <%if(!pageContext.getAttribute("test").toString().equals("6") && !pageContext.getAttribute("test").toString().equals("5")){%>
			            <%if(pageContext.getAttribute("test").toString().equals("1")){%>
			            <a href="#" onclick="javascript:onViewShortfall('<c:out value="${pageScope.alData1[6]}"/>')"><c:out value="${strD}"/></a>
			            <% }else{%>
			              <c:out value="${strD}"/>
			            <% }%>
		            <% }%>
		             <%if(pageContext.getAttribute("test").toString().equals("5")){%>
		            	<c:set var="fileYN" value="${strD}"/>
		            	<%if(pageContext.getAttribute("fileYN").toString().equals("Y")){%>
			            	<a href="#" onclick="javascript:onViewShortfallDoc('<c:out value="${pageScope.alData1[6]}"/>')"> File  </a>
		            	<% }else{ %>
		            	Provider not responded
		            	<%} %>
		            <%} %>
		         </td>
	         </c:forEach>
	     </tr>
	        </c:forEach>
	  </table>
	</fieldset>	
	</logic:notEmpty>
	<!--  Shprtfall Docs E N D S -->
	<br>
<%-- 		<fieldset>
		<legend><b>Document Uploads</b></legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0" style="width: 99%">
			<tr  style="border:1px solid;">
          		<td width="19%"    align="left" nowrap>&nbsp;&nbsp;<b>Description:</b></td>
          		<td width="18%"    align="left" nowrap>
          			<html:select property ="description" styleClass="selectBox selectBoxMedium">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="provDocsType" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
          	          
            <td align="left"><b>Browse File : </b></td>
			<td>
				<html:file property="file" styleId="file"/>
			</td>
			</tr>
			  </table>
		</fieldset> --%>			
<%-- <c:if test = "${sessionScope.preAuthStatus eq 'In-Progress'}"> --%>
<logic:notEmpty name="frmOnlinePreAuth" property="preAuthNo">  
 <fieldset>
		<legend>Uploading Documents Details</legend>
		<table class="formContainer" border="0" cellspacing="0" cellpadding="0" style="margin-top:5px;">
			
			<tr>
				
          		<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description1:</td>
          		<td width="18%"    align="left" nowrap>
          			
          			<html:select property ="description" styleId="descriptionid" styleClass="selectBox selectBoxMedium">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="descriptionCode" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
          	          
	          <td align="left">Browse File1 :</td>
			<td>
				<html:file property="file1" styleId="fileid1"/>
			</td>
			</tr>
			<tr>
			<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description2:</td>
          		<td width="18%"    align="left" nowrap>
          			
          			<html:select property ="description2" styleId="descriptionid2" styleClass="selectBox selectBoxMedium">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="descriptionCode" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
			<td align="left">Browse File2 :</td>
			<td>
			<html:file property="file2" styleId="fileid2" style="margin-top: 5px;"/></td>
			
			</tr>
			<tr>
			<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description3:</td>
          		<td width="18%"    align="left" nowrap>
          			
          			<html:select property ="description3" styleId="descriptionid3" styleClass="selectBox selectBoxMedium">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="descriptionCode" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
			<td align="left">Browse File3 :</td>
			<td>
			<html:file property="file3" styleId="fileid3"  style="margin-top: 5px;"/></td>
			
			</tr>
			
			<tr>
			<td width="19%"    align="left" nowrap>&nbsp;&nbsp;Description4:</td>
          		<td width="18%"    align="left" nowrap>
          			
          			<html:select property ="description4" styleId="descriptionid4" styleClass="selectBox selectBoxMedium">
          					<html:option value="">--Select from list--</html:option>
                 			<html:options collection="descriptionCode" property="cacheId" labelProperty="cacheDesc"/>
          			</html:select>
          		</td>
			<td align="left">Browse File4 :</td>
			<td>
			<html:file property="file4" styleId="fileid4"  style="margin-top: 5px;"/></td>
			
			</tr>
			  </table>
		</fieldset>	
</logic:notEmpty>
<c:choose>
<%-- <c:when test = "${(not empty sessionScope.isSubmitted) &&  (sessionScope.isSubmitted eq false)}">
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
   	<tr><td>
			<a href="#" onClick="javascript:onCommonUploadDocuments('HOS|PAT');">Document Uploads </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</td>
</tr></table>
</c:when> --%>
<c:when test = "${(not empty sessionScope.isSubmitted) &&  (sessionScope.isSubmitted eq true)}">
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
   	<tr><td>
			<a href="#" onClick="javascript:onCommonUploadDocuments('HOS|PAT');">View Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</td>
</tr></table>
</c:when>
<%-- <c:otherwise>
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
   	<tr><td>
   			<a href="#" onClick="javascript:onUploadDocs('<bean:write name="frmOnlinePreAuth" property="preAuthNo"/>')">View Documents</a>
			<a href="#" onClick="javascript:onCommonUploadDocuments('HOS|PAT');">View Documents </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</td>
</tr></table>
</c:otherwise> --%>
</c:choose>

    
 <!-- S T A R T : Buttons -->
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
   	<tr>
        <td width="100%" align="center">
			<%-- <button type="button" name="Button" accesskey="s" class="olbtnSmall" onClick="javascript:onSavePartialPreAuthEnhance();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;
			<logic:match name="frmOnlinePreAuth" property="submitEN"  value="Y" > --%>
					<button type="button" name="Button" accesskey="s" class="olbtnMedium" onClick="javascript:onSubmitOnlinePreAuthEnhance();"><u>S</u>ave & <u>S</u>ubmit</button>&nbsp;&nbsp;&nbsp;
			<%-- </logic:match> --%>
			<button type="button" name="Button" accesskey="b" class="olbtnSmall" onClick="javascript:onBack();"><u>B</u>ack</button>&nbsp;&nbsp;&nbsp;
		 </td>
      	<td width="100%" align="center">
   	</tr>
</table>
		<!-- E N D : Buttons -->
		<%-- </logic:notEmpty> --%>
	<input type="hidden" name="mode" value="">
	<INPUT TYPE="hidden" NAME="leftlink">
	<INPUT TYPE="hidden" NAME="sublink">
	<INPUT TYPE="hidden" NAME="tab">
	<INPUT TYPE="hidden" NAME="rownum">
	
	<html:hidden property="diagCodeSeqID" styleId="diagCodeSeqID" name="frmOnlinePreAuth"/>
	<INPUT TYPE="hidden" NAME="preAuthNoYN" value="<bean:write name="frmOnlinePreAuth" property="preAuthNo"/> ">
	<input type="hidden" name="gran" id="gran" value="<%= request.getAttribute("gran")%>">
	<input type="hidden" name="focusObj" value="<%= request.getAttribute("focusObj")%>">
	<input type="hidden" name="lPreAuthIntSeqId" id="seqId" value="<%= request.getSession().getAttribute("lPreAuthIntSeqId")%>">
	<input type="hidden" name="fromFlag" value="<%=request.getSession().getAttribute("fromFlag")%>">
	<input type="hidden" name="hospMailId" value="<%=request.getSession().getAttribute("hospMailId")%>">
	<input type="hidden" name="hospSeqID" id="hospSeqID" value="<%=request.getSession().getAttribute("hospSeqID")%>">
	<INPUT TYPE="hidden" NAME="enrollId" value="<bean:write name="frmOnlinePreAuth" property="enrollId"/> ">
	<input type="hidden" name="tariffSeqId" id="tariffSeqId">
	<html:hidden property="benifitTypeCode" name="frmOnlinePreAuth"/>
	<INPUT TYPE="hidden" NAME="benefitTemp" id="benefitTemp" value="<%= benefitTemp%>"><!-- Hidden property to check Benefi type Dental or not in  changeTreatment method-->
	<input type="hidden" name="enhanceYN" value="Y">
	<input type="hidden" name="loginTypeProvider" id="loginTypeProviderid" value="<%=request.getSession().getAttribute("loginTypeProvider")%>">
	
	<script type="text/javascript">
	callFocusObj();
	</script>
	<br><br><br><br>
	</div>
<script type="text/javascript">
changeTreatment(document.forms[1].treatmentType);
</script>	

  <%if("DNTL".equals(benefitTemp) || "Dental".equals(benefitTemp)) {%>
 <logic:equal value="Y" property="activityDetailsVO.toothNoReqYN" name="frmOnlinePreAuth">
	   <script type="text/javascript">  
	  	 openListIntX('toothNo','TOOTHNOS');
	  </script>	
 </logic:equal>
 <%} %>
	</body>
</html:form>