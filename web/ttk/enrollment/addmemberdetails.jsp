<%
/**
 * @ (#) addmemberdetails.jsp Feb 2nd 2006
 * Project      : TTK HealthCare Services
 * File         : addmemberdetails.jsp
 * Author       : Krishna K H
 * Company      : Span Systems Corporation
 * Date Created : Feb 2nd 2006
 *
 * @author       : Krishna K H
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.WebBoardHelper,com.ttk.common.security.Cache"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,org.apache.struts.action.DynaActionForm" %>


<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/Hcalendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/hijri-date.js"></script>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/enrollment/addmemberdetails.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
<script type="text/javascript" src="/ttk/scripts/async.js"></script>
<script type="text/javascript">
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getParameter("focusID"))%>";
function changeManSym(){
 var j=document.getElementById("nationality").value;
 var passNo = document.getElementById("passNo");
 while (passNo.firstChild) {
	 passNo.removeChild(passNo.firstChild);
	}
 if(j==="55"||j==55){
	 var div=document.createElement("div");	
	 var node = document.createTextNode("");
	 div.appendChild(node);
	 passNo.appendChild(div);		 
 }else{	 
	 var div=document.createElement("div");	
	 var node = document.createTextNode("*");
	 div.appendChild(node);
	 passNo.appendChild(div);	
	 }
	 
	
}
</script>
<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>	

<style>
.mandatorySymbol{color: red;}
.disabledfieldType{
       font-size: 11px;
       color: #A9A9A9;
       border-left: 1px solid black;
       border-right: 1px solid black;
       border-top:1px solid black; 
       border-bottom: 1px solid black;
}
.disabledfieldType2{
       font-size: 11px;
       color:black;
       border-left: 1px solid black;
       border-right: 1px solid black;
       border-top:1px solid black; 
       border-bottom: 1px solid black;
}
</style>
<%
		pageContext.setAttribute("gender", Cache.getCacheObject("gender"));
		pageContext.setAttribute("stateCode", Cache.getCacheObject("stateCode"));
		pageContext.setAttribute("cityCode", Cache.getCacheObject("cityCode"));
		pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
		pageContext.setAttribute("clarificationstatus", Cache.getCacheObject("clarificationstatus"));
		pageContext.setAttribute("relshipGender", Cache.getCacheObject("relshipGender"));
		pageContext.setAttribute("occupation", Cache.getCacheObject("occupation"));
		pageContext.setAttribute("nationalities", Cache.getCacheObject("nationalities"));
		pageContext.setAttribute("maritalStatuses", Cache.getCacheObject("maritalStatuses"));
		DynaActionForm frmAddMember = (DynaActionForm)request.getSession().getAttribute("frmAddMember");
		String policyStartDate = (String)request.getSession().getAttribute("policyStartDate");
		String dateOfJoining = (String)request.getSession().getAttribute("dateOfJoining");
		String dateOfMarriage = (String)request.getSession().getAttribute("dateOfMarriage");
		String enrollmentNo = (String)request.getSession().getAttribute("enrollmentNo");
		boolean viewmode=true;
		if(TTKCommon.isAuthorized(request,"Edit"))
		{
			viewmode=false;
		}//end of if(TTKCommon.isAuthorized(request,"Edit"))
		pageContext.setAttribute("viewmode",new Boolean(viewmode));
%>
<!-- S T A R T : Content/Form Area -->

<html:form action="/AddMemberDetailAction.do" >
	<!-- S T A R T : Page Title -->
	<logic:match name="frmPolicyList" property="switchType" value="ENM">
		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	</logic:match>
	<logic:match name="frmPolicyList" property="switchType" value="END">
		<table align="center" class="pageTitleHilite" border="0" cellspacing="0" cellpadding="0">
	</logic:match>
			<tr>
	    		<td width="50%">Member Details - <bean:write name="frmAddMember" property="caption"/> </td>
	    		<td width="50%" align="right" class="webBoard">&nbsp;<logic:match name="frmAddMember" property="caption" value="Edit"><logic:match name="frmPolicyList" property="switchType" value="END">&nbsp;&nbsp;&nbsp;<img src="/ttk/images/IconSeparator.gif" width="1" height="15" align="absmiddle" class="icons">&nbsp;&nbsp;&nbsp; <a href="#" onClick="javascript:onSuspendedIcon();"><img src="/ttk/images/SuspendedIcon.gif" title="Suspension Details" width="16" height="16" border="0"></a></logic:match></logic:match></td>
	    	</tr>
		</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:message name="updated" scope="request"/>
			  </td>
			</tr>
		</table>
	</logic:notEmpty>
	<!-- E N D : Success Box -->
	<html:errors/>
	<!-- Added Rule Engine Enhancement code : 26/02/2008 -->
	<ttk:BusinessErrors name="BUSINESS_ERRORS" scope="request" />
	<!--  End of Addition -->

	<div class="contentArea" id="contentArea">
    <!-- S T A R T : Form Fields -->
	<fieldset>
<legend>Personal Information</legend>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="formLabel" width="20%">Relationship: <span class="mandatorySymbol">*</span></td>
    <td class="formLabel" width="32%">
	    <html:select property="relationTypeID" styleClass="selectBox selectBoxMedium" onchange="onRelationshipChange();" disabled="<%=viewmode%>" styleId="relationTypeID">
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="frmAddMember" property="alRelationShip"  label="cacheDesc" value="cacheId" />
		</html:select>
    </td>
    <td class="formLabel" width="24%">VIP</td>
    <td class="formLabel" width="24%"><html:select property="vipYN"   styleClass="selectBox selectBoxMedium"  disabled="<%=viewmode%>">
  	 		  <html:option value="N">NO</html:option>
		      <html:option value="Y">YES</html:option>
		</html:select>
		</td>
  </tr>
  <tr>
    <td width="20%" class="formLabel">Beneficiary Name: <span class="mandatorySymbol">*</span></td>
    <td class="formLabel">
	<logic:notEqual name="frmAddMember" property="relationID" value="NSF">
    	<html:text property="name" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);blockEnterkey(event.srcElement);" maxlength="60" disabled="<%=viewmode%>"/>
	</logic:notEqual>
	<logic:equal name="frmAddMember" property="relationID" value="NSF">
    	<html:text property="name" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);blockEnterkey(event.srcElement);" maxlength="60" disabled="true"/>
    	<input type="hidden" name="name" value="<bean:write name="frmAddMember" property="name"/>">
	</logic:equal>
    </td>

<td width="20%" class="formLabel">Beneficiary Second Name:</td>
    <td class="formLabel">
    	<html:text property="secondName" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);blockEnterkey(event.srcElement);" maxlength="60" disabled="true"/>
	</td>
	</tr>
	<tr>
	<td width="20%" class="formLabel">Family Name: </td>
    <td class="formLabel">
    	<html:text property="familyName" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);blockEnterkey(event.srcElement);" maxlength="60" disabled="true"/>
	</td>
    <td class="formLabel">Gender:<span class="mandatorySymbol">*</span> </td>
    <td class="formLabel">

    <logic:equal name="frmAddMember" property="genderYN" value="OTH">
		<html:select property="genderTypeID" styleClass="selectBox" disabled="<%=(viewmode)%>">
			  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
		</html:select>
	</logic:equal>
	<logic:notEqual name="frmAddMember" property="genderYN" value="OTH">
		<html:select property="genderTypeID" styleClass="selectBox selectBoxDisabled" disabled="true">
			  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
		</html:select>
		<input type="hidden" name="genderTypeID" value="<bean:write name="frmAddMember" property="genderYN"/>">
	</logic:notEqual>

  </tr>
  <tr>
  <td class="formLabel">Date of Birth:<span class="mandatorySymbol">*</span></td>
  
 <!--  ibm DOB removed for qatar -->
  <%-- <%
     if(enrollmentNo.contains("I310"))
     {%> --%>
  	   <%-- <td class="formLabel">
      	<html:text property="dateOfBirth" styleClass="textBox textDate" onblur="javascript:onIBMDateofBirth()" maxlength="10" disabled="<%=viewmode%>"/>
      	<logic:match name="viewmode" value="false">
      	<a name="CalendarObjectdobDate" id="CalendarObjectdobDate" href="#" onClick="javascript:show_calendar('CalendarObjectdobDate','frmAddMember.dateOfBirth',document.frmAddMember.dateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="dobDate" width="24" height="17" border="0" align="absmiddle"></a>
      	</logic:match>
      	</td> --%>
  <%-- 	<%}
     else
     {
  	   %> --%>
	    <td class="formLabel">
    	<html:text property="dateOfBirth" styleId="Dob" styleClass="textBox textDate" onfocus="convertToHDate('HDob')" onblur="javascript:convertToHDate('HDob');onDateofBirth();" maxlength="10" disabled="<%=viewmode%>"/>
    	<logic:match name="viewmode" value="false">
    	<a name="CalendarObjectdobDate" id="CalendarObjectdobDate" href="#" onClick="javascript:show_calendar('CalendarObjectdobDate','frmAddMember.dateOfBirth',document.frmAddMember.dateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" name="dobDate" width="24" height="17" border="0" align="absmiddle"></a>
    	</logic:match>
    	<html:text property="hdateOfBirth" styleId="HDob" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>" onfocus="convertToGDate('Dob')" onblur="javascript:convertToGDate('Dob');onDateofBirth();"/>
    	<logic:match name="viewmode" value="false">
    	<a name="HCalendarObjectdobDate" ID="HDob" HREF="#" onClick="javascript:show_Hcalendar('HDob','forms[1].hdateOfBirth',document.forms[1].hdateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Hijri-Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/HCalendarIcon.gif" title="Hijri-Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
    	</logic:match>
    	</td>
   <%--   <%
   %> --%>

    <td class="formLabel">Age (Yrs):</td>
    <td class="formLabel">
    <logic:notEmpty name="frmAddMember" property="dateOfBirth">
	    <html:text styleId="ageid" property="age" styleClass="textBox textBoxTiny" maxlength="3" readonly="true"/>
    </logic:notEmpty>
    <logic:empty name="frmAddMember" property="dateOfBirth">
	    <html:text styleId="ageid" property="age" styleClass="textBox textBoxTiny" maxlength="3" disabled="<%=viewmode%>"/>
    </logic:empty>

	</tr>
  <tr>
    <td class="formLabel">Occupation:</td>
    <td class="formLabel">
        <html:select property="occupationTypeID" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="occupation" label="cacheDesc" value="cacheId" />
		</html:select>
	</td>
    <td class="formLabel">Qatar ID Number:<span class="mandatorySymbol">*</span></td>
    <td class="formLabelBold">
	    <html:text  property="emirateId"  disabled="<%=viewmode%>"  styleClass="textBox textBoxLarge" onmouseover="getEmirateIdDescription();" onkeyup="isNumeric(this)"/>
    </td>
  </tr>
  <tr>
    <td class="formLabel">Nationality:<span class="mandatorySymbol">*</span></td>
    <td class="formLabelBold">
    	 <html:select name="frmAddMember" property="nationality" styleId="nationality" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>" onchange="changeManSym();">
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="nationalities" label="cacheDesc" value="cacheId" /> 
		</html:select>
    </td>
    <td class="formLabel">Marital Status:<span class="mandatorySymbol">*</span></td>
    <td class="formLabelBold">    	
		 <html:select property="maritalStatus" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="maritalStatuses" label="cacheDesc" value="cacheId" /> 
		</html:select>
    </td>
  </tr>
  <tr>    
    <td width="20%" class="formLabel">
    <table>
    <tr>
    <td>Passport Number:</td>
    <td>
    <div id="passNo" style="color: red;"><div><logic:notMatch name="frmAddMember" property="nationality" value="55">*</logic:notMatch></div></div></td>    
     </tr>
     </table>
     </td>
     <td>
	    <html:text  property="passportNumber"  disabled="<%=viewmode%>"  styleClass="textBox textBoxLarge"/>
    </td>
    <%-- <td>UID Number:<span class="mandatorySymbol">*</span></td>
       <td>
       <html:text name="frmAddMember"  property="uIDNumber" styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>"/>
       </td> --%>
  </tr>
  </table>
</fieldset>

<ttk:MemberPolicyInfo/>

    <fieldset>
    <legend>Card Details</legend>
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="20%" class="formLabel">Photo Present: </td>
        <td width="32%" class="formLabel">
	        <html:checkbox property="photoPresentYN" value="Y" disabled="<%=viewmode%>"/>
        </td>
        <td width="24%" class="formLabel">Print Card: </td>
        <td width="24%" class="formLabel">
        	<html:checkbox property="cardPrintYN" value="Y" disabled="true"/>
        </td>
        </tr>
      <tr>
        <td class="formLabel">No. of Cards Printed:</td>
	    <td class="formLabelBold">
	        <bean:write name="frmAddMember" property="cardPrintCnt"/>
	    </td>
        <td class="formLabel">Card Printed Date: </td>
        <td class="formLabelBold">
        	<bean:write name="frmAddMember" property="cardPrintDate"/>
        </td>
        </tr>
      <tr>
        <td class="formLabel">Card Batch No.:</td>
	    <td class="formLabelBold">
	        <bean:write name="frmAddMember" property="cardBatchNbr"/>
	    </td>
        <td class="formLabel">Courier No.: </td>
        <td class="formLabelBold">
        	<bean:write name="frmAddMember" property="courierNbr"/>
        </td>
      </tr>
	  <tr>
        <td class="formLabel">Dispatch Date:</td>
	    <td class="formLabelBold">
	        <bean:write name="frmAddMember" property="docDispatchDate"/>
	    </td>
        <td class="formLabel">Card Type:</td>
        <td class="formLabelBold">
        	<bean:write name="frmAddMember" property="cardDesc"/>
        </td>
      </tr>
    </table>
    </fieldset>
 <logic:notEqual name="frmAddMember" property="disableYN" value="N">
    <fieldset>
	<legend>Address Information</legend>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="20%" class="formLabel">Address 1:</td>
        <td width="32%">
        	<html:text property="memberAddressVO.address1" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%=viewmode%>"/>
        </td>
        <td width="24%" class="formLabel">Address 2:</td>
        <td width="24%">
	        <html:text property="memberAddressVO.address2" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%=viewmode%>"/>
        </td>
      </tr>
      <tr>
        <td class="formLabel">Address 3:</td>
        <td>
	        <html:text property="memberAddressVO.address3" styleClass="textBox textBoxMedium" maxlength="250" disabled="<%=viewmode%>"/>
        </td>
        <td class="formLabel">Country:<span class="mandatorySymbol">*</span></td>
        <td>
        
        	<html:select property="memberAddressVO.countryCode" styleId="cnCode"  styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>" onchange="getStates('state','stateCode')">
			      <html:option value="">Select from list</html:option>
			      <html:optionsCollection name="countryCode" label="cacheDesc" value="cacheId" />
			</html:select>
        </td>
        
        </tr>
      <tr>
	<td class="formLabel">City:</td>
        <td>
       
			<html:select property="memberAddressVO.stateCode" styleId="stateCode"  styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>" onchange="getStates('city','cityCode')">
			      <html:option value="">Select from list</html:option>
			      <logic:notEmpty name="providerStates" scope="session">
			       <html:optionsCollection name="providerStates" label="value" value="key"  />
			      </logic:notEmpty>
			</html:select>		
        </td>
        <td class="formLabel"> PO Box:</td>
        <td>
      		  <html:text property="memberAddressVO.pinCode" styleClass="textBox textBoxMedium" maxlength="10" disabled="<%=viewmode%>"/>
        </td>
      </tr>
      <tr>
      <td class="formLabel">Area:</td>
        <td>        
        <html:select property="memberAddressVO.cityCode" styleId="cityCode" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
	  	 		  <html:option value="">Select from list</html:option>
	  	 		  <logic:notEmpty name="providerAreas" scope="session">
			       <html:optionsCollection name="providerAreas" label="value" value="key"  />
			      </logic:notEmpty>			      
			      </html:select>        
        </td>
        <td></td>
        <td>
        </td>
        </tr>
        <tr>
        <td>Residential Location:</td>
        <td>
	        <html:text property="memberAddressVO.residentialLocation" styleClass="textBox textBoxMedium" maxlength="50" disabled="<%=viewmode%>"/>
        </td>
		<td>Work Location:</td>
		<td>
			<html:text property="memberAddressVO.workLocation" styleClass="textBox textBoxMedium" maxlength="50" disabled="<%=viewmode%>"/>
		 </td>
		 </tr>
        <tr>
        <td>Email Id1:</td>
        <td>
	        <html:text property="memberAddressVO.emailID" styleClass="textBox textBoxMedium" maxlength="50" disabled="<%=viewmode%>"/>
        </td>
		<td>Email Id2:</td>
		<td>
			<html:text property="memberAddressVO.emailID2" styleClass="textBox textBoxMedium" size="500" disabled="<%=viewmode%>"/>
		 </td>
		 </tr>
      
      <tr>
        <td class="formLabel">Office Phone 1:</td>
        <td>
             <logic:empty name="frmAddMember" property="memberAddressVO.off1IsdCode">
           <html:text name="frmAddMember" property="memberAddressVO.off1IsdCode" styleId="isdCode1" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddMember" property="memberAddressVO.off1IsdCode">
             <html:text name="frmAddMember" property="memberAddressVO.off1IsdCode" styleId="isdCode1" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty>
          <logic:empty name="frmAddMember" property="memberAddressVO.off1StdCode">
            <html:text name="frmAddMember" property="memberAddressVO.off1StdCode" styleId="stdCode1" styleClass="disabledfieldType" size="3" maxlength="3" value="STD" onfocus="if (this.value=='STD') this.value = ''" onblur="if (this.value==''){ this.value = 'STD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>			
            </logic:empty>
             <logic:notEmpty name="frmAddMember" property="memberAddressVO.off1StdCode">
            <html:text name="frmAddMember" property="memberAddressVO.off1StdCode" styleId="stdCode1" styleClass="disabledfieldType" size="3" maxlength="3"/>			
            </logic:notEmpty>
            <html:text property="memberAddressVO.phoneNbr1" styleClass="textBox textBoxMedium" maxlength="25" disabled="<%=viewmode%>"/>
        </td>
        <td class="formLabel">Office Phone 2:</td>
        <td>
             <logic:empty name="frmAddMember" property="memberAddressVO.off2IsdCode">
           <html:text name="frmAddMember" property="memberAddressVO.off2IsdCode" styleId="isdCode2" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddMember" property="memberAddressVO.off2IsdCode">
           <html:text name="frmAddMember" property="memberAddressVO.off2IsdCode" styleId="isdCode2" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty>
          <logic:empty name="frmAddMember" property="memberAddressVO.off2StdCode">
            <html:text name="frmAddMember" property="memberAddressVO.off2StdCode" styleId="stdCode2" styleClass="disabledfieldType" size="3" maxlength="3" value="STD" onfocus="if (this.value=='STD') this.value = ''" onblur="if (this.value==''){ this.value = 'STD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>			
            </logic:empty>
             <logic:notEmpty name="frmAddMember" property="memberAddressVO.off2StdCode">
            <html:text name="frmAddMember" property="memberAddressVO.off2StdCode" styleId="stdCode2" styleClass="disabledfieldType" size="3" maxlength="3"/>			
            </logic:notEmpty>
            <html:text property="memberAddressVO.phoneNbr2" styleClass="textBox textBoxMedium" maxlength="25" disabled="<%=viewmode%>"/>
        </td>
      </tr>
      <tr>
        <td class="formLabel">Home Phone:</td>
        <td>
            <logic:empty name="frmAddMember" property="memberAddressVO.homeIsdCode">
           <html:text name="frmAddMember" property="memberAddressVO.homeIsdCode" styleId="isdCode3" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddMember" property="memberAddressVO.homeIsdCode">
           <html:text name="frmAddMember" property="memberAddressVO.homeIsdCode" styleId="isdCode3" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty>
          <logic:empty name="frmAddMember" property="memberAddressVO.homeStdCode">
            <html:text name="frmAddMember" property="memberAddressVO.homeStdCode" styleId="stdCode3" styleClass="disabledfieldType" size="3" maxlength="3" value="STD" onfocus="if (this.value=='STD') this.value = ''" onblur="if (this.value==''){ this.value = 'STD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>			
            </logic:empty>
             <logic:notEmpty name="frmAddMember" property="memberAddressVO.homeStdCode">
            <html:text name="frmAddMember" property="memberAddressVO.homeStdCode" styleId="stdCode3" styleClass="disabledfieldType" size="3" maxlength="3"/>			
            </logic:notEmpty>
          <html:text property="memberAddressVO.homePhoneNbr" styleClass="textBox textBoxMedium" maxlength="15" disabled="<%=viewmode%>"/>
        </td>
        <td class="formLabel">Contact Number:</td>
        <td>
            <%--  <logic:empty name="frmAddMember" property="memberAddressVO.mobileIsdCode">
           <html:text name="frmAddMember" property="memberAddressVO.mobileIsdCode" styleId="isdCode4" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddMember" property="memberAddressVO.mobileIsdCode">
           <html:text name="frmAddMember" property="memberAddressVO.mobileIsdCode" styleId="isdCode4" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty> --%>
          <html:text property="memberAddressVO.mobileNbr" styleClass="textBox textBoxMedium" maxlength="15" disabled="<%=viewmode%>"/>
        </td>
      </tr>
      <tr>
        <td>Fax:</td>
        <td>
			<html:text property="memberAddressVO.faxNbr" styleClass="textBox textBoxMedium" maxlength="15" disabled="<%=viewmode%>"/>
        </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table>
    </fieldset>
</logic:notEqual>
<fieldset>
	<legend>General</legend>
	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
	<%if(request.getSession().getAttribute("CorporatePolicy") !=null && request.getSession().getAttribute("CorporatePolicy").equals("CorporatePolicy")){%>
	  <tr>
	  <td class="formLabel">Stop Cashless:</td>	 				
	  				<td>	  				
	 					<html:checkbox name="frmAddMember" property="stopPreAuthsYN" value="Y" styleId="stopPreAuthsYN" onclick="showAndHideDatePreauth();"/>	 
	 					<logic:equal value="Y" name="frmAddMember" property="stopPreAuthsYN">
	 				<span id="stopPreauthDateId">
	 				 <html:text
                        property="stopPreauthDate" name="frmAddMember"
                        styleClass="textBox textDate" maxlength="10" styleId="stopPreauthId"/><a
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopPreauthDate',document.forms[1].stopPreauthDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
				</logic:equal> 
	 				 <logic:notEqual value="Y" name="frmAddMember" property="stopPreAuthsYN">
	 				<span id="stopPreauthDateId" style="display: none">
	 				<!-- <br> --> <html:text
                        property="stopPreauthDate" name="frmAddMember"
                        styleClass="textBox textDate" maxlength="10" styleId="stopPreauthId"/><a
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopPreauthDate',document.forms[1].stopPreauthDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
				</logic:notEqual>					
	 				</td>
	 				
		<td class="formLabel">Stop Claims:</td>	 				
	  				<td>	  				
	 					<html:checkbox name="frmAddMember" property="stopClaimsYN" value="Y" styleId="stopClaimsYN" onclick="showAndHideDateClaims();"/>	 		
	 					<logic:equal value="Y" name="frmAddMember" property="stopClaimsYN">
	 				<span  id="stopClaimsDateId"><html:text
                        property="stopClaimsDate" name="frmAddMember"
                        styleClass="textBox textDate" maxlength="10" styleId="stopclmsid"/><A
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopClaimsDate',document.forms[1].stopClaimsDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
				</logic:equal> 
	 				 <logic:notEqual value="Y" name="frmAddMember" property="stopClaimsYN">
	 				<span id="stopClaimsDateId" style="display: none"><html:text
                        property="stopClaimsDate" name="frmAddMember"
                        styleClass="textBox textDate" maxlength="10" styleId="stopclmsid"/><A
                    NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#"
                    onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].stopClaimsDate',document.forms[1].stopClaimsDate.value,'',event,148,178);return false;"
                    onMouseOver="window.status='Calendar';return true;"
                    onMouseOut="window.status='';return true;"><img
                        src="/ttk/images/CalendarIcon.gif" title="Calendar" name="mrkDate"
                        width="24" height="17" border="0" align="absmiddle"></a>
				</span>
		</logic:notEqual>			
	 	</td>
	  </tr>
	  <%}else{ %>
  		<tr>
		 	<td width="20%" class="formLabel">Stop Cashless/Claims:</td>
		    <td class="formLabel" width="32%">
			 	<html:checkbox property="stopPatClmYN" value="Y" disabled="<%=viewmode%>"/>
		    </td>
		    <td class="formLabel" width="24%">Received After:</td>
		    <td class="formLabel" width="24%">
		 	   <html:text property="receivedAfter" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>"/>
				<logic:match name="viewmode" value="false">
		 	   		<a name="CalendarObjectReceivedAfter" id="CalendarObjectReceivedAfter" href="#" onClick="javascript:show_calendar('CalendarObjectReceivedAfter','frmAddMember.receivedAfter',document.frmAddMember.receivedAfter.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" name="receivedAfter" width="24" height="17" border="0" align="absmiddle"></a>
		 	   	</logic:match>
		 	</td>
		 	
 		 </tr>
 		 <%}%>
 		 <tr>
		 	<td width="20%" class="formLabel">Invoice Generation:</td>
		    <td class="formLabel" width="32%">
			 	<bean:write name="frmAddMember" property="invoiceYN"/>
		    </td>
		    <td width="20%" class="formLabel">Credit Note Generation:</td>
		    <td class="formLabel" width="32%">
			 	<bean:write name="frmAddMember" property="creditnoteYN"/>
		    </td>
		 </tr>
		 <tr>
		 	<td width="21%" class="formLabel">Birthday Wish Mail Notification:</td>
		    <td class="formLabel">
			 	<html:checkbox property="mailNotificationYN" styleId="mailNotificationYN"/>
			 	<html:hidden property="mailNotificationhiddenYN" styleId="mailNotificationhiddenYN"/>
		    </td>
		    </tr>
	</table>
</fieldset>
	<%-- <fieldset>
<legend>Clarification with Insurance Company</legend>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="20%" class="formLabel">Clarification Status:</td>
    <td class="formLabel" width="32%">
	    <html:select property="clarificationTypeID" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="clarificationstatus" label="cacheDesc" value="cacheId" />
		</html:select>
    </td>
    <td class="formLabel" width="24%">Clarified Date:</td>
    <td class="formLabel" width="24%">
 	   <html:text property="clarifiedDate" styleClass="textBox textDate" maxlength="10" disabled="<%=viewmode%>"/>
		<logic:match name="viewmode" value="false">
 	   		<a name="CalendarObjectClarifiedDate" id="CalendarObjectClarifiedDate" href="#" onClick="javascript:show_calendar('CalendarObjectClarifiedDate','frmAddMember.clarifiedDate',document.frmAddMember.clarifiedDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="clarifiedDate" width="24" height="17" border="0" align="absmiddle"></a>
 	   	</logic:match>
 	   </td>
  </tr>
  <tr>
    <td class="formLabel">Remarks:</td>
    <td class="formLabel" colspan="3">
    	<html:textarea property="remarks" styleClass="textBox textAreaLong" disabled="<%=viewmode%>"/>
    </td>
  </tr>
</table>
	</fieldset>
	 --%>
	<%-- <logic:equal name="capitationFlag"  value="Y" scope="session">
	
	<logic:match name="frmAddMember" property="capitationflag" value="Y"></logic:match>
	
	<fieldset>
<legend>Capitation Policy</legend>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
   <tr>
   		<td class="formLabel">Age Range:<span class="mandatorySymbol">*</span></td>
        <td>
      		  <html:text name="frmAddMember"  property="ageRange" styleClass="textBox textBoxMedium" onkeyup="isNumeric(this);"   disabled="<%=viewmode%>"/>
        </td>
          </tr>  
         
  <tr>
   		<td class="formLabel">Min Age:</td>
        <td>
      		  <html:text name="frmAddMember"  property="minAge" maxlength="3"  styleId="minAge"  styleClass="textBox textBoxMedium" onkeyup="ageCompare();" onblur="isNegative1();" disabled="<%=viewmode%>"/>
        </td>
        <td class="formLabel">Max Age:</td>
        <td>
      		  <html:text name="frmAddMember"  property="maxAge"  maxlength="3"   styleId="maxAge" styleClass="textBox textBoxMedium"  onchange="ageCompare();" onblur="isNegative2();"  disabled="<%=viewmode%>"/>
        </td>
  </tr>
  <tr>
   		<td class="formLabel">Medical Premium :</td>
        <td>
      		  <html:text name="frmAddMember"  property="medicalPremium"  styleId="medicalPremium"  styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>" onkeyup="calcCapNetPremium(this);"/>
        </td>
        <td class="formLabel">Insurer Margin:<span class="mandatorySymbol">*</span></td>
        <td>
      		  <html:text name="frmAddMember"  property="insurerMargin" maxlength="5" styleClass="textBox textBoxMedium" onkeyup="calcCapNetPremium(this);" onblur="numbervalidation('Insurer Margin');" disabled="<%=viewmode%>"/>[%]
      	</td>
 </tr>
  
  <tr>
   		<td class="formLabel">Maternity Premium :</td>
        <td>
      		  <html:text name="frmAddMember"  property="maternityPremium"  styleId="maternityPremium"  styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>" onkeyup="calcCapNetPremium(this);"/>
        </td>
        <td class="formLabel">Broker Margin:<span class="mandatorySymbol">*</span></td>
        <td>
        		 <html:text name="frmAddMember"  property="brokerMargin" maxlength="5" styleClass="textBox textBoxMedium" onkeyup="calcCapNetPremium(this);" onblur="numbervalidation('Broker Margin');" disabled="<%=viewmode%>"/>[%]
        </td>
  </tr>
  
  <tr>
   		<td class="formLabel">Optical Premium :</td>
        <td>
      		  <html:text name="frmAddMember"  property="opticalPremium"   styleId="opticalPremium"   styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>" onkeyup="calcCapNetPremium(this);"/>
        </td>
        <td class="formLabel">TPA Margin:</td>
        <td>
        	<html:text name="frmAddMember"  property="tapMargin"  maxlength="5" styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>" onblur="numbervalidation('TPA Margin');" onkeyup="calcCapNetPremium(this);"/>[%]
        </td>
  </tr>
  
  <tr>
   		<td class="formLabel">Dental Premium :</td>
        <td>
      		  <html:text name="frmAddMember"  property="dentalPremium"  styleId="dentalPremium"   styleClass="textBox textBoxMedium" onblur="numbervalidation('Dental Premium');" disabled="<%=viewmode%>" onkeyup="calcCapNetPremium(this);"/>
        </td>
        <td class="formLabel">ReIns.Brk.Margin:</td>
        <td>
        	<html:text name="frmAddMember"  property="reInsBrkMargin"  maxlength="5" styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>" onblur="numbervalidation('ReIns.Brk.Margin');" onkeyup="calcCapNetPremium(this);"/>[%]
        </td>
  </tr>
  
  <tr>
   		<td class="formLabel">Wellness Premium :</td>
        <td>
      		  <html:text name="frmAddMember"  property="wellnessPremium"   styleId="wellnessPremium"    styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>" onkeyup="calcCapNetPremium(this);"/>
        </td>
        <td class="formLabel">Other Margin:</td>
        <td>
        	<html:text name="frmAddMember"  property="otherMargin" maxlength="5"  styleClass="textBox textBoxMedium" onkeyup="calcCapNetPremium(this);" onblur="numbervalidation('Other Margin');" disabled="<%=viewmode%>"/>[%]
        </td>
  </tr>
  
  <tr>         
   		<td class="formLabel">Gross Premium :<span class="mandatorySymbol">*</span></td> 
        <td>
      		  <html:text name="frmAddMember"  property="grossPremium"   styleId="grossPremium" readonly="true"  style="background-color: #F5F5DC;" styleClass="textBox textBoxMedium"  disabled="<%=viewmode%>" /> 
        </td>
        <td class="formLabel">IP Net Premium :<span class="mandatorySymbol">*</span></td>
        <td>
        	<html:text name="frmAddMember"  property="ipNetPremium" styleId="ipNetPremium"  styleClass="textBox textBoxMedium"  onkeyup="calcCapNetPremium(this);"  disabled="<%=viewmode%>" />
        	<!--  onblur="calcmarginmatchgrosspremium();" -->
        </td>    
  </tr>        
  <tr>
   		<td class="formLabel">Net Premium :<span class="mandatorySymbol">*</span></td> 
   		 <td>
   			<html:text name="frmAddMember"  property="netPremium" styleClass="textBox textBoxMedium"  readonly="true" styleId="netPremium"  disabled="<%=viewmode%>" style="background-color: #F5F5DC;" /> 
         </td>
  </tr>
    
</table>
	</fieldset>
	</logic:equal>
	 --%>
	<!-- E N D : Form Fields -->
    <!-- S T A R T : Buttons -->
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" align="center">
    <%
		if(TTKCommon.isAuthorized(request,"Edit"))
		{
	%>
		<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave()"><u>S</u>ave</button>&nbsp;
		<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset()"><u>R</u>eset</button>&nbsp;
    <%
    	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
     %>
    <%--  <%
		if(TTKCommon.isAuthorized(request,"Delete"))
		{
	%>
		<logic:match name="frmAddMember" property="caption" value="Edit">
			<button type="button" name="Button" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onDelete()"><u>D</u>elete</button>&nbsp;
		</logic:match>
	    <%
    	}//end of if(TTKCommon.isAuthorized(request,"Delete"))
     %> --%>
	     <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCancel();"><u>C</u>lose</button>&nbsp;
    </td>
  </tr>
</table>
	<!-- E N D : Buttons -->
</div>
</div>

<!-- E N D : Buttons -->

	<!-- E N D : Content/Form Area -->
<!-- E N D : Main Container Table -->
 	<html:hidden property="mode" />
 	<input type="hidden" name="child" value="AddEditMember">
    <input type="hidden" name="pageId" value="">
    <INPUT TYPE="hidden" NAME="stopPatClmYN" VALUE="">
    <INPUT TYPE="hidden" NAME="photoPresentYN" VALUE="">
    <INPUT TYPE="hidden" NAME="cardPrintYN" VALUE="">
    <input type="hidden" name="policyDate" value="<%=policyStartDate%>">
	<input type="hidden" name="DOJ" value="<%=dateOfJoining%>">
	<input type="hidden" name="DOM" value="<%=dateOfMarriage%>">
	<html:hidden property="enrollmentNbr"/>
    <html:hidden property="hiddenName"/>
    <html:hidden property="hiddensecondName"/>
    <html:hidden property="hiddenfamilyName"/>
    <html:hidden property="disableYN"/>
    <html:hidden property="relationID"/>
    <html:hidden property="genderYN"/>
    <html:hidden property="effectiveDate"/>
    <html:hidden property="policyEndDate"/>
    <html:hidden property="policyStartDate"/>
    <input type="hidden" name="focusID" value="">
    <logic:equal name="capitationFlag" value="Y" scope="session">
    <html:hidden property="capitationflag"  value="Y"/> 
    </logic:equal>
    <logic:notEqual name="capitationFlag"  value="Y" scope="session">
    <html:hidden property="capitationflag"  value="N"/>
    </logic:notEqual>
</body>
<logic:notEmpty name="frmAddMember" property="frmChanged">
	<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>
<input type="hidden" name="stopclaimsmember" id="stopclaimsmemberid" value="<%= request.getSession().getAttribute("stopclaimsmember") %>">
<input type="hidden" name="stopcashlessmember" id="stopcashlessmemberid" value="<%= request.getSession().getAttribute("stopcashlessmember") %>">
<script>
mailNotification();
</script>
</html:form>