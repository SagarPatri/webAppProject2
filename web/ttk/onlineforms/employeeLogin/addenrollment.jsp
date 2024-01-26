<%
/** @ (#) addenrollment.jsp 09th Jan 2007
 * Project     : TTK Healthcare Services
 * File        : addenrollment.jsp
 * Author      : Chandrasekaran J
 * Company     : Span Systems Corporation
 * Date Created: 09th Jan 2007
 *
 * @author 		 : Chandrasekaran J
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
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList,com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.common.WebBoardHelper" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,org.apache.struts.action.DynaActionForm" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/EmployeeLogin/addenrollment.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
 <script type="text/javascript" src="/ttk/scripts/calendar/Hcalendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/hijri-date.js"></script>
<script type="text/javascript" src="/ttk/scripts/async.js"></script>
<SCRIPT LANGUAGE="JavaScript">
function CheckIBMValid(cb)
{
	  		if(cb.checked == true){

	  			if(document.forms[1].EmailId2.value == "")
		  		{
				alert('Please Enter Personal EmailID');
				document.forms[1].EmailId2.focus();
				return false;
			   }
		 		if(document.forms[1].MobileNo.value == "")
				{
				alert('Please Enter Mobile No');
				document.forms[1].MobileNo.focus();
				return false;
				}

		  }

  }
  function CheckOPTOUT(opt)
  {
  	 if(document.forms[1].stopOPtInYN.checked)//koc1216 added by Rekha 19.07.2012
  	{
  			var msg = confirm("Are you sure you would like to opt out of the employee, spouse and children(ESC) coverage?Also, please note that additional coverage will not be applicable on selection this option.If you Opt-Out from the Policy,Dependent Records will be Deleted! and Sum Insured will become null,if you wish to cover yourself, spouse and children under the policy, Please uncheck the 'opt out' checkbox, re-enter the details of your dependents in the enrollment section and save the data.");
  			if(!msg)
  			{
  				document.forms[1].stopOPtInYN.checked=false;
  				return false;
  			}
  			if(msg)
  			{
  				document.forms[1].stopOPtInYN.checked=true;
  				document.forms[1].stopOPtInYN.focus();
  				return true;
  			}
  	}

  }


</script>
<style type="text/css">
.modify_text_class{
color: red;
}
.successContainer, .errorContainer{
margin-left: -38px;
}

</style>
<script>
var JS_Focus_ID="<%=TTKCommon.checkNull(request.getAttribute("focusID"))%>";
</script>
<%
    boolean viewmode=true;
    boolean viewallowmodify=false;
    boolean viewAddress=true;

    //Added for IBM...15
	boolean viewchkpreauthclaims=false;//added by Rekha 13.07.2012
	boolean viewoptyn=true;//added by Rekha 13.07.2012
    //Ended
  ArrayList alLocation=(ArrayList)session.getAttribute("alLocation");
  pageContext.setAttribute("viewmode",new Boolean(viewmode));
  pageContext.setAttribute("countryCode", Cache.getCacheObject("countryCode"));
  pageContext.setAttribute("listStateCode",Cache.getCacheObject("stateCode"));  
  pageContext.setAttribute("ActiveSubLink",TTKCommon.getActiveSubLink(request));
  UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
  String groupId=userSecurityProfile.getGroupID();
  pageContext.setAttribute("designations", Cache.getCacheObject("designationTypeID"));
  pageContext.setAttribute("gender", Cache.getCacheObject("gender"));
	pageContext.setAttribute("nationalities", Cache.getCacheObject("nationalities"));
	pageContext.setAttribute("maritalStatuses", Cache.getCacheObject("maritalStatuses"));
	pageContext.setAttribute("destnationbank",Cache.getCacheObject("destnationbank"));
 	//pageContext.setAttribute("alCityList", Cache.getCacheObject("alCityList"));
//	pageContext.setAttribute("alDistList", Cache.getCacheObject("nationalities"));
//	pageContext.setAttribute("alBranchList", Cache.getCacheObject("nationalities")); 
	  pageContext.setAttribute("listRelationshipCode",Cache.getCacheObject("hrRelationship"));
	  pageContext.setAttribute("OutsideQatarCountryList", Cache.getCacheObject("OutsideQatarCountryList"));
	  String photoYN = (String)request.getSession().getAttribute("photoYN"); 
%>
<%
	 if(TTKCommon.isAuthorized(request,"Edit"))
    {
		 
		 if(userSecurityProfile.getLoginType().equals("B"))
		 {
			 viewAddress=true;
		 }
		 else
		 {
    	viewAddress=false;
		 }
%>
	<%
	if(!userSecurityProfile.getLoginType().equals("EMPL")){ %>
		<logic:equal name="frmMember" property="empaddyn" value="Y">
		<%	viewmode=false; %>
	</logic:equal>
	<%}else{
		viewmode=false;
	}
	%>
		
		<logic:equal name="frmAddEnrollment" property="allowModiYN" value="N">
			<% viewallowmodify=true; %>
		</logic:equal>

		<!-- Added for IBM...15.1 -->
		<!-------added by Rekha 13.07.2012-->

				<logic:equal name="frmAddEnrollment" property="windowsOPTYN" value="Y" >
				<%	viewoptyn=false; %>
			   </logic:equal>
			   <logic:equal name="frmAddEnrollment" property="chkpreauthclaims" value="Y" >
				<%	viewchkpreauthclaims=true; %>
			   </logic:equal>


		<!------- end added by Rekha 13.07.2012-->


		<!-- Ended -->
<%
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
%>

<html:form action="/AddEnrollmentAction.do"  method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><bean:write name="frmAddEnrollment" property="caption"/></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- Added Rule Engine Enhancement code : 27/02/2008 -->
	<ttk:BusinessErrors name="BUSINESS_ERRORS" scope="request" />
	<!--  End of Addition -->
	<div class="contentArea" id="contentArea">
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
 		<!-- E N D : Success Box -->
 		<logic:notEmpty name="statusinfo" scope="request">
			 <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				 <tr>
					 <td>
					 	<strong>
					 		<img src="/ttk/images/ErrorIcon.gif" alt="Error" width="16" height="16" align="absmiddle">&nbsp;
					 	</strong>
					 	<bean:message name="statusinfo" scope="request"/>
					 	<ol style="padding:0px;margin-top:3px;margin-bottom:0px;margin-left:25px;">	</ol>
					 </td>
				 </tr>
			 </table>
		 </logic:notEmpty>
		 	<logic:notEmpty name="notify" scope="request">
		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" alt="Alert" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="notify" scope="request"/>
	        </td>
	      </tr>
   	 </table>
   	 </logic:notEmpty>
 		<html:errors/>
		<!-- S T A R T : Form Fields -->
		<logic:equal name="frmAddEnrollment" property="allowCityBankYN" value="Y">
			<fieldset>
	    	<legend>Member Information</legend>
	      	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		        <tr>
		        	<td width="21%" nowrap class="formLabel">Customer No.: </td>
		        	<td width="30%" nowrap>
		        		<html:text property="customerNbr" styleClass="textBox textBoxMedium" maxlength="60" disabled="<%=viewmode%>"/>
		        	</td>
		        	<td width="19%" nowrap class="formLabel">Card Holders Name: <span class="mandatorySymbol">*</span></td>
			    	<td width="30%">
			    		<html:text property="insuredName" styleClass="textBox textBoxMedium" style="width:190px;" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%=viewmode%>"/>
			    		<input type="hidden" name="cardholdername" id="memname" value="">
		        	</td>
		        </tr>
		      	<tr>
		        	<td class="formLabel" nowrap>Certificate No.: <span class="mandatorySymbol">*</span></td>
		        	<td >
		        		<html:text property="certificateNbr" styleClass="textBox textBoxMedium" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%=viewmode%>"/>
		        	</td>
		        	<td class="formLabel" nowrap>Credit Card No.: </td>
		        	<td >
		        		<html:text property="creditCardNbr" styleClass="textBox textBoxMedium" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement);" disabled="<%=viewmode%>"/>
		        	</td>
		        </tr>
		        <tr>
		        	<td  nowrap class="formLabel">Order No.:<span class="mandatorySymbol">*</td>
		        	<td nowrap>
			        	<span class="formLabel">
			        		<html:text property="orderNbr" styleClass="textBox textBoxMedium" maxlength="60" disabled="<%=viewmode%>"/>
			        	</span>
		        	</td>
		        	<td nowrap class="formLabel">Prev. Order No.:</td>
		        	<td nowrap>
		        		<span class="formLabel">
		        			<html:text property="prevOrderNbr" styleClass="textBox textBoxMedium" maxlength="60" disabled="<%=viewmode%>"/>
		        		</span>
		        	</td>
		        </tr>
	       </table>
	       </fieldset>
       </logic:equal>
       <logic:notEqual name="frmAddEnrollment" property="allowCityBankYN" value="Y">
       
       <div class="modify_text_class1">
       
       <font size="2" color="red" ><b>Note:</b> Please note that some of the fields below are non editable. Please contact Al Koot Customer Care if you want to modify the information in non editable fields.</font>
       
       </div>
		   <fieldset>
		   <legend>Employee Information</legend>
				<table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">
			      	<tr>
			        	<td width="21%" nowrap class="formLabelWeblogin">Employee No.: <span class="mandatorySymbol">*</span></td>
			        	<logic:notEmpty name="frmAddEnrollment" property="memberSeqID">
			        	<td width="30%" nowrap>
			        		<html:text property="employeeNbr" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" style="background-color: #EEEEEE;" readonly="true" />
			        	</td>
			        	</logic:notEmpty>
			        	<logic:empty name="frmAddEnrollment" property="memberSeqID">
			        	<td width="30%" nowrap>
			        		<html:text property="employeeNbr" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60"/>
			        	</td>
			        	</logic:empty>
	  		        	<td class="formAddEnrollmment" width="19%">Group No.: </td>
			        	<td width="30%">
			        		<html:text property="groupNumber" styleClass="textBoxWeblogin textBoxLargeWeblogin" maxlength="100" styleId="insuredName"/>
			        	</td>			        	   
	
			  
			      	</tr>
			      		<tr>
			      		<td class="formLabelWeblogin" width="19%">Principal Name: <span class="mandatorySymbol">*</span></td>
			        	<td width="30%">
			        		<html:text property="insuredName" styleClass="textBoxWeblogin textBoxLargeWeblogin" maxlength="100" style="background-color: #EEEEEE;" readonly="true"/>
			        	</td>	
			        	<td width="21%" nowrap class="formLabelWeblogin">Second Name:</td>
			        	<td width="30%" nowrap>
			        		<html:text property="secondName" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" disabled="true" style="background-color: #EEEEEE;"/>
			        	</td>
			        	
			      	</tr>
			      	<tr>
			      	<td class="formLabelWeblogin" width="19%">Family Name:</td>
			        	<td width="30%">
			        		<html:text property="familyName" styleClass="textBoxWeblogin textBoxLargeWeblogin" maxlength="60"  disabled="true" style="background-color: #EEEEEE;"/>
			        	</td>
			      	<td class="formLabelWeblogin" width="19%">Relationship:</td>
			        	<td width="30%">		        	
			        	 <html:select property="relationship" styleClass="selectBox selectBoxMedium" style="background-color: #EEEEEE;" readonly="true" >
         						 <html:optionsCollection name="listRelationshipCode" label="cacheDesc" value="cacheId"/>
    					  </html:select>
			        	
			        	
			        	
			   	</tr>
			      	<tr>
			      	
			        	<td class="formLabelWeblogin">Department:</td>
			        	<td>
			        		<html:text property="department" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" style="background-color: #EEEEEE;" readonly="true"/>
			        	</td>
			        	
			        	
			        	</td>
			      	    <td class="formLabel">Status:</td>
			      	    <td colspan="3" class="formLabelBold">
	         			 	<bean:write name="frmAddEnrollment" property="empStatusDesc" />
	      				</td>
			        				      	</tr>
			      	<tr>
			        	<td class="formLabelWeblogin">Date of Joining:</td>
			        	<td>
			        		<html:text property="startDate" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;background-color:#EEEEEE;" readonly="true"/>
			        		<%-- <%
		          				if(viewmode==false && viewallowmodify==false)
		      					{
		        			%>
		            					<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmAddEnrollment.startDate',document.frmAddEnrollment.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>
		            		<%
								}//end of if(viewmode==false)
							%> --%>
			        	</td>
			        	<td class="formLabelWeblogin"> Designation:</td>
			        	 <td>
     					     <html:select property="designation" styleClass="selectBox selectBoxMedium">
      					     <html:option value="">SELECT FROM LIST</html:option>
           					 <html:optionsCollection name="designations" label="cacheDesc" value="cacheId" />
         					 </html:select>
     				   </td>
			        	
			        	
			      	</tr>
			      	
			      	<tr>
			      	 <logic:notEmpty name="frmAddEnrollment" property="memberSeqID">
			        	<td class="formLabelWeblogin">Date of Inception:<span class="mandatorySymbol">*</span></td>
			        	<td>
			        		<html:text property="dateOfInception" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" readonly="true" style="background-color: #EEEEEE;" />
			        		<%
		          				if(viewmode==false && viewallowmodify==false)
		      					{
		        			%>
		        				<logic:empty name="frmAddEnrollment" property="memberSeqID"> 
		            					<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmAddEnrollment.dateOfInception',document.frmAddEnrollment.dateOfInception.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>
		            		</logic:empty>
		            		<%
								}//end of if(viewmode==false)
							%>
			        	</td>
			        	</logic:notEmpty>
			        	 <logic:empty name="frmAddEnrollment" property="memberSeqID">
			        	<td class="formLabelWeblogin">Date of Inception:<span class="mandatorySymbol">*</span></td>
			        	<td>
			        		<html:text property="dateOfInception" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" />
			        		<%
		          				if(viewmode==false && viewallowmodify==false)
		      					{
		        			%>	        				
		            					<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmAddEnrollment.dateOfInception',document.frmAddEnrollment.dateOfInception.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>
		            		<%
								}//end of if(viewmode==false)
							%>
			        	</td>
			        	</logic:empty> 
			        		        			        	
			        	<td class="formLabelWeblogin">Date of Resignation: </td>
			        	<td>
			        		<html:text property="endDate" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" />
		        			<%
		          				if(viewmode==false && viewallowmodify==false)
		      					{
		        			%>
		            				<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmAddEnrollment.endDate',document.frmAddEnrollment.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>
		            		<%
								}//end of if(viewmode==false)
							%>
			        	</td>
			        				       
			      	</tr>
			      	<tr>
			      	<td class="formLabelWeblogin">Date of Exit: </td>
			        	<td>
			        		<html:text property="dateOfExit" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" readonly="true" style="background-color: #EEEEEE;"/>
			        	</td>
			      	</tr>
			      	<%--  <tr>
			        	<td class="formLabelWeblogin">Location:</td>
			        	<td>
			        		<html:select property="groupRegnSeqID"  styleClass="selectBoxWeblogin selectBoxMediumWeblogin" style="width:220px; margin-top:5px;" disabled="<%=(viewmode || viewallowmodify)%>" >
		            			<html:options collection="alLocation"  property="cacheId" labelProperty="cacheDesc"/>
		    				</html:select>
			        	</td>
			        	
			        	<td>Email Id:</td>
			        	<%
    						if(userSecurityProfile.getLoginType().equals("B"))
    						{
    					%>
		        	 <td>
		        		<html:text property="memberAddressVO.emailID" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="250" disabled="true" />
		        	</td> 
		        	<%
    						}
		        	%>
		        	
			       	</tr>
			     <% if(!(userSecurityProfile.getLoginType().equals("B"))) {%>
			       	<tr>
			       	<td class="formLabelWeblogin">Family No.:</td>
			        	<td>
			        		<html:text property="familyNbr" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="15" disabled="<%=(viewmode || viewallowmodify)%>" />
			        	</td>
			       	</tr>
			       	
			       	
			       <%} %>
			        --%>
		    	</table>
			</fieldset>
			<!-- kocb  -->	       	
		</logic:notEqual>
		
		<%if(!(userSecurityProfile.getLoginType().equals("B"))) { %>
		<fieldset>
		<legend>Personal Information</legend>
		
		
				<table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">
			      	<tr>
			        	<td width="21%" nowrap class="formLabelWeblogin">Beneficiary Name:<span class="mandatorySymbol">*</span></td>
			        	<td width="30%" nowrap>
			  				<%-- <html:text property="beneficiaryName" styleId="beneficiaryName" styleClass="textBox textBoxMedium" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement);"/> --%>
			  				<html:text property="insuredName" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" />
			  			</td>
			  			
			  				<td class="formLabelWeblogin" width="19%">Gender: <span class="mandatorySymbol">*</span></td>
			        	<td width="30%">	        	
          							<html:select property="genderTypeID" styleClass="selectBox selectBoxMedium" disabled="true" readonly="true" style="background-color: rgb(238, 238, 238);">
         						 	 	<html:option value="">SELECT FROM LIST</html:option>
           								 <html:option value="MAL">Male</html:option>
           								 <html:option value="FEM">Female</html:option>
          							</html:select>
    				   		 </td>    			
   							<%--  <logic:equal name="frmAddEnrollment" property="genderYN" value="OTH">
								<html:select property="genderTypeID" styleClass="selectBox" disabled="<%=(viewmode)%>">
								  <html:option value="">Select from list</html:option>
							      <html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
								</html:select>
							</logic:equal>
							<logic:notEqual name="frmAddEnrollment" property="genderYN" value="OTH">
								<html:select property="genderTypeID" styleClass="selectBox selectBoxDisabled" disabled="true">
								  <html:option value="">Select from list</html:option>
								      <html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
								</html:select>
							<input type="hidden" name="genderTypeID" value="<bean:write name="frmAddEnrollment" property="genderYN"/>">
							</logic:notEqual>		 --%>  			
					</tr>
					
					<tr>
 							 <td class="formLabel">Date of Birth:<span class="mandatorySymbol">*</span></td>
							   <td class="formLabel">
    							<html:text property="dateOfBirth" styleId="Dob" styleClass="textBox textDate" onfocus="convertToHDate('HDob')" onblur="javascript:convertToHDate('HDob');onDateofBirth();" maxlength="10" disabled="true" readonly="true"/>
    							 <a name="CalendarObjectdobDate" id="CalendarObjectdobDate" href="#" onClick="" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="dobDate" width="24" height="17" border="0" align="absmiddle"></a>
    							 <!--  <a name="CalendarObjectdobDate" id="CalendarObjectdobDate" href="#" onClick="javascript:show_calendar('CalendarObjectdobDate','frmMemberDetails.dateOfBirth',document.frmMemberDetails.dateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="dobDate" width="24" height="17" border="0" align="absmiddle"></a> -->
    							<html:text property="hdateOfBirth" styleId="HDob" styleClass="textBox textDate" maxlength="10"  onfocus="convertToGDate('Dob')" onblur="javascript:convertToGDate('Dob');onDateofBirth();" disabled="true" readonly="true" />					
  							  	<a name="HCalendarObjectdobDate" ID="HDob" HREF="#" onClick="" onMouseOut="window.status='';return true;" ><img src="/ttk/images/HCalendarIcon.gif" alt="Hijri-Calendar"  width="24" height="17" border="0" align="absmiddle"></a>     
  							  	<!-- <a name="HCalendarObjectdobDate" ID="HDob" HREF="#" onClick="javascript:show_Hcalendar('HDob','forms[1].hdateOfBirth',document.forms[1].hdateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Hijri-Calendar';return true;" onMouseOut="window.status='';return true;" ><img src="/ttk/images/HCalendarIcon.gif" alt="Hijri-Calendar"  width="24" height="17" border="0" align="absmiddle"></a>      -->
    				</td>
							<td class="formLabelWeblogin" width="19%">Age (Yrs):</td>
			        			<td width="30%">
    								<logic:notEmpty name="frmAddEnrollment" property="dateOfBirth">
	 								   <html:text styleId="ageid" property="age" styleClass="textBox textBoxTiny" maxlength="3" readonly="true"/>
   									 </logic:notEmpty>
   									 <logic:empty name="frmAddEnrollment" property="dateOfBirth">
									    <html:text styleId="ageid" property="age" styleClass="textBox textBoxTiny" maxlength="3" disabled="<%=viewmode%>"/>
   									 </logic:empty>
   								</td>
						</tr>
						<tr>
						 	<td width="21%" nowrap class="formLabelWeblogin">Sum Insured: <span class="mandatorySymbol">*</span></td>
			        			<td width="30%" nowrap>
			        			<html:text property="sumInsured" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="25"  readonly="true" style="background-color: #EEEEEE;"/>
			        		</td>
					<%-- 	<logic:empty name="frmAddEnrollment" property="sumInsured">
						<td width="21%" nowrap class="formLabelWeblogin">Sum Insured: <span class="mandatorySymbol">*</span></td>
			        			<td width="30%" nowrap>
			        			<html:text property="sumInsured" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="25"  />
			        		</td>
						</logic:empty> --%>
						
						
							  <td class="formLabel">Qatar ID Number:<span class="mandatorySymbol">*</span></td>
    							<td class="formLabelBold">
	  						 <html:text  property="emirateId"  disabled="<%=viewmode%>"  styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="11" />
    					</td>					
					</tr>
					<tr>
   				 <td class="formLabel">Nationality:<span class="mandatorySymbol">*</span></td>
    			<td class="formLabelBold">
    	 <html:select name="frmAddEnrollment" property="nationality" styleId="nationality" styleClass="selectBox selectBoxMedium" disabled="true" readonly="true" onchange="changeManSym();" style="background-color: rgb(238, 238, 238);">
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="nationalities" label="cacheDesc" value="cacheId" /> 
		</html:select>
    </td>
    <td class="formLabel">Marital Status:<span class="mandatorySymbol">*</span></td>
    <td class="formLabelBold">    	
		 <html:select property="maritalStatus" styleId="maritalStatus" styleClass="selectBox selectBoxMedium" disabled="true" readonly="true"  style="background-color: rgb(238, 238, 238);" >
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="maritalStatuses" label="cacheDesc" value="cacheId" /> 
		</html:select>
    </td>
  </tr>
  			<tr>
  				<td class="formLabelWeblogin" width="19%">Passport Number: <span class="mandatorySymbol">*</span></td>
			        	<td width="30%">
			        		<html:text property="passportNumber" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60"  disabled="<%=(viewmode || viewallowmodify)%>"/>
			        	</td>
   				 <%-- <td class="formLabel" width="19%">VIP</td>
  					  <td class="formLabel" width="24%"><html:select property="vipYN"   styleClass="selectBox selectBoxMedium" onchange="onRelationshipChange();" disabled="<%=viewmode%>">
  	 		  			<html:option value="N">NO</html:option>
		    		  <html:option value="Y">YES</html:option>
					</html:select>
				</td> --%>
   		 </tr>		
		<%-- 	<tr>
			 <td align="left">Upload Photo :</td>
							<td>
								<html:file property="file" styleId="file"/>
							</td>
						   <td align="right"  valign="bottom">		        	    
   								<img src="/GetImageAction.do?mode=dohelloImage"  alt="Image"  width="100" height="100" />
						   </td>
			</tr> --%>							
		   	<%} %>
		</table>
		</fieldset>		
		<%if(!(userSecurityProfile.getLoginType().equals("B"))) { %>
		<fieldset>
		<legend>Address Information</legend>
			<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
		      	<tr>
		        	<td width="21%" class="formLabelWeblogin">Address 1:</td>
		        	<td width="30%">
		        		<html:text property="memberAddressVO.address1" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="250" disabled="<%=viewmode%>" />
		        		
		        	</td>
		        	<td width="19%" class="formLabelWeblogin">Address 2:</td>
		        	<td width="30%">
		        		<html:text property="memberAddressVO.address2" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="250" disabled="<%=viewAddress%>" />
		        	</td>
		      	</tr>
		      	<tr>
		        	<td class="formLabelWeblogin">Address 3:</td>
					<td><html:text property="memberAddressVO.address3" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="250" disabled="<%=viewAddress%>" />
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
					<td class="formLabel">City:<span class="mandatorySymbol">*</span></td>
       				 <td>     
						<html:select property="memberAddressVO.stateCode" styleId="stateCode"  styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>" onchange="getStates('city','cityCode')">
			     		 <html:option value="">Select from list</html:option>
			      			<logic:notEmpty name="providerStates" scope="session">
			     		  <html:optionsCollection name="providerStates" label="value" value="key"  />
			 		     </logic:notEmpty>
					</html:select>		
     			   </td>
		     		 <td class="formLabel">Area:</td>
     				   <td>        
       						 <html:select property="memberAddressVO.cityCode" styleId="cityCode" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
	  	 						  <html:option value="">Select from list</html:option>
	  	 		 					 <logic:notEmpty name="providerAreas" scope="session">
			     					  <html:optionsCollection name="providerAreas" label="value" value="key"  />
			   						   </logic:notEmpty>			      
			     			 </html:select>        
    				    </td>
		        </tr>
		      	<tr>
		        	 <td class="formLabelWeblogin">PO BOX: </td>
		        	<td>
		        		<html:text property="memberAddressVO.pinCode" styleClass="textBoxWeblogin textBoxSmallWeblogin"  maxlength="6" disabled="<%=viewmode%>" />
		        	</td>
		        	<td>Email ID 1:<!-- <span class="mandatorySymbol">*</span> --></td>
		        	<%
    						if(userSecurityProfile.getLoginType().equals("H"))
    						{
    					%>
    					
		        	<td>
		        		<html:text property="memberAddressVO.emailID" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="250" disabled="false" />
		        	</td>
		        	<%
    						}
		        	%>
		        	<%
    						if(userSecurityProfile.getLoginType().equals("E")||userSecurityProfile.getLoginType().equals("EMPL"))
    						{
    					%>
		        	<td>
		        		<html:text property="memberAddressVO.emailID" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="250" disabled="true"  readonly="true" style="background-color: rgb(238, 238, 238);"/>
		        	</td>

		        	<%
    						}
		        	%>
		      	</tr>
		      	<tr>
		      	<td>Email ID 2:</td>
		      		<!-- New Enail Added by Praveen -->
		      		<%
    						if(userSecurityProfile.getLoginType().equals("H"))
    						{
    					%>
		        	<td>
		        		<html:text name = "frmAddEnrollment" property="EmailId2" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="250" disabled="false" />
 		        	</td>
		        	<%
    						}
		        	%>
		        	<%
    						if(userSecurityProfile.getLoginType().equals("E")||userSecurityProfile.getLoginType().equals("EMPL"))
    						{
    					%>
		        	<td>
		        		<html:text name = "frmAddEnrollment" property="EmailId2" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="250"  disabled="true"  readonly="true" style="background-color: rgb(238, 238, 238);"/>
		        	</td>
		        	<%
    						}
		        	%>
		      	</tr>
		      	<tr>
		      
        <td class="formLabel">Office Phone 1:<span class="mandatorySymbol">*</span></td>
        <td>
             <logic:empty name="frmAddEnrollment" property="memberAddressVO.off1IsdCode">
           <html:text name="frmAddEnrollment" property="memberAddressVO.off1IsdCode" styleId="isdCode1" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddEnrollment" property="memberAddressVO.off1IsdCode">
             <html:text name="frmAddEnrollment" property="memberAddressVO.off1IsdCode" styleId="isdCode1" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty>
          <logic:empty name="frmAddEnrollment" property="memberAddressVO.off1StdCode">
            <html:text name="frmAddEnrollment" property="memberAddressVO.off1StdCode" styleId="stdCode1" styleClass="disabledfieldType" size="3" maxlength="3" value="STD" onfocus="if (this.value=='STD') this.value = ''" onblur="if (this.value==''){ this.value = 'STD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>			
            </logic:empty>
             <logic:notEmpty name="frmAddEnrollment" property="memberAddressVO.off1StdCode">
            <html:text name="frmAddEnrollment" property="memberAddressVO.off1StdCode" styleId="stdCode1" styleClass="disabledfieldType" size="3" maxlength="3"/>			
            </logic:notEmpty>
            <html:text property="memberAddressVO.phoneNbr1" styleClass="textBox textBoxMedium" maxlength="25" disabled="false"/>
        </td>
        <td class="formLabel">Office Phone 2:</td>
        <td>
             <logic:empty name="frmAddEnrollment" property="memberAddressVO.off2IsdCode">
           <html:text name="frmAddEnrollment" property="memberAddressVO.off2IsdCode" styleId="isdCode2" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddEnrollment" property="memberAddressVO.off2IsdCode">
           <html:text name="frmAddEnrollment" property="memberAddressVO.off2IsdCode" styleId="isdCode2" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty>
          <logic:empty name="frmAddEnrollment" property="memberAddressVO.off2StdCode">
            <html:text name="frmAddEnrollment" property="memberAddressVO.off2StdCode" styleId="stdCode2" styleClass="disabledfieldType" size="3" maxlength="3" value="STD" onfocus="if (this.value=='STD') this.value = ''" onblur="if (this.value==''){ this.value = 'STD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>			
            </logic:empty>
             <logic:notEmpty name="frmAddEnrollment" property="memberAddressVO.off2StdCode">
            <html:text name="frmAddEnrollment" property="memberAddressVO.off2StdCode" styleId="stdCode2" styleClass="disabledfieldType" size="3" maxlength="3"/>			
            </logic:notEmpty>
            <html:text property="memberAddressVO.phoneNbr2" styleClass="textBox textBoxMedium" maxlength="25" disabled="<%=viewmode%>"/>
        </td>  		      		        
	</tr>
	<tr>
			         <td class="formLabel">Home Phone:</td>
        <td>
            <logic:empty name="frmAddEnrollment" property="memberAddressVO.homeIsdCode">
           <html:text name="frmAddEnrollment" property="memberAddressVO.homeIsdCode" styleId="isdCode3" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddEnrollment" property="memberAddressVO.homeIsdCode">
           <html:text name="frmAddEnrollment" property="memberAddressVO.homeIsdCode" styleId="isdCode3" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty>
          <logic:empty name="frmAddEnrollment" property="memberAddressVO.homeStdCode">
            <html:text name="frmAddEnrollment" property="memberAddressVO.homeStdCode" styleId="stdCode3" styleClass="disabledfieldType" size="3" maxlength="3" value="STD" onfocus="if (this.value=='STD') this.value = ''" onblur="if (this.value==''){ this.value = 'STD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>			
            </logic:empty>
             <logic:notEmpty name="frmAddEnrollment" property="memberAddressVO.homeStdCode">
            <html:text name="frmAddEnrollment" property="memberAddressVO.homeStdCode" styleId="stdCode3" styleClass="disabledfieldType" size="3" maxlength="3"/>			
            </logic:notEmpty>
          <html:text property="memberAddressVO.homePhoneNbr" styleClass="textBox textBoxMedium" maxlength="25" disabled="<%=viewmode%>"/>
        </td>
        <td class="formLabel">Contact Number:</td>
        <td>
             <%-- <logic:empty name="frmAddEnrollment" property="memberAddressVO.mobileIsdCode">
           <html:text name="frmAddEnrollment" property="memberAddressVO.mobileIsdCode" styleId="isdCode4" styleClass="disabledfieldType" size="3" maxlength="3" value="ISD"  onfocus="if (this.value=='ISD') this.value = ''" onblur="if (this.value==''){ this.value = 'ISD';this.className='disabledfieldType';}" onkeypress="this.className='disabledfieldType2'"/>
          </logic:empty>
          <logic:notEmpty name="frmAddEnrollment" property="memberAddressVO.mobileIsdCode">
           <html:text name="frmAddEnrollment" property="memberAddressVO.mobileIsdCode" styleId="isdCode4" styleClass="disabledfieldType" size="3" maxlength="3"/>
          </logic:notEmpty> --%>
          <html:text property="memberAddressVO.mobileNbr" styleClass="textBox textBoxMedium" maxlength="25" disabled="<%=viewmode%>"/>
        </td>		        
		      	</tr>
		      	<tr>
		      	<td class="formLabelWeblogin">Residential Location:</td>
			        <td>
			        	<html:text name ="frmAddEnrollment" property="residentialLocation"  styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="10" style="background-color: #EEEEEE;" readonly="true" />
			        </td>
			        <td class="formLabelWeblogin">Work Location:</td>
			        <td>
			        	<html:text name ="frmAddEnrollment" property="workLocation"  styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="10" style="background-color: #EEEEEE;" readonly="true" />
			        </td>
		      	</tr>
		      	<tr>
			        <td>Fax:</td>
			        <td>
			        	<html:text property="memberAddressVO.faxNbr" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="15" disabled="<%=viewAddress%>" />
			        </td>
			        <td>&nbsp;</td>
			        <td>&nbsp;</td>
		      	</tr>
		      	<%} %>
	    	</table>
	    	<%-- <logic:match name="frmAddEnrollment" property="v_OPT_ALLOWED" value="WSA">
			<fieldset>
				<table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td><font color="red">
			<html:checkbox property="validEmailPhYN" styleId="validEmailPhYN" disabled="<%=(viewoptyn || viewchkpreauthclaims)%>" onchange='CheckIBMValid(this)'/>
			By providing my personal email ID and mobile phone number,
			I understand and agree that IBM India Pvt. Ltd.
			,its medical Healthcare provider (currently Apollo Munich)
			and their third party administrator (currently Vidal Healthcare Services Pvt. Ltd.),
     		and agents of the above, may contact me for the purposes of providing services
     		and assistance to me under IBM's health Healthcare programs, and for various wellness
     		and healthcare related initiatives (including promotional and awareness initiatives)
     		organized at or through IBM. I hereby provide my consent to being so contacted for the
     		above purposes..</font></td>
		</td>

		</tr>
		</table>
		</fieldset>
		</logic:match> --%>
		</fieldset>
		<%if(!(userSecurityProfile.getLoginType().equals("B"))) { %>
		<fieldset>
		<legend>Bank Account Information</legend>
			<%-- <table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">
		      	<tr>
			        <td width="21%" class="formLabelWeblogin" nowrap>Bank Name:</td>
			        <td width="30%" class="formLabelWeblogin" nowrap>
			          <html:text property="bankName" styleClass="textBoxWeblogin textBoxLargeWeblogin" maxlength="250" disabled="<%=viewAddress%>"/>
			        </td>
			        <td width="19%" class="formLabelWeblogin" nowrap>Branch:</td>
			        <td width="30%" class="formLabelWeblogin" nowrap>
			          <html:text property="branch" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="250" disabled="<%=viewAddress%>"/>
			        </td>
		      	</tr>
		      	<tr>
			        <td class="formLabelWeblogin">Bank Account No.:</td>
			        <td>
			          <html:text property="bankAccNbr" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" disabled="<%=viewAddress%>"/>
			        </td>
			        <td class="formLabelWeblogin">MICR Code: </td>
			        <td>
			          <html:text property="MICRCode" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="60" disabled="<%=viewAddress%>"/>
			        </td>
		      	</tr>
		      	<tr>
			        <td class="formLabelWeblogin">Bank Phone:</td>
			        <td>
			          <html:text property="bankPhone" styleClass="textBoxWeblogin textBoxMediumWeblogin" maxlength="25" disabled="<%=viewAddress%>"/>
			        </td>
			        <td class="formLabelWeblogin">&nbsp;</td>
			        <td>&nbsp;</td>
		      	</tr>
	    	</table> --%>       
      <table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">   
         <tr>
              <td width="21%" class="formLabel">Bank Location: <span class="mandatorySymbol">*</span></td>
                 <td>
          <html:select property="bankAccountQatarYN" styleClass="selectBox selectBoxMedium"   onchange="onChangeQatarYN()" >
                    <html:option value="Y">Within Qatar</html:option>
                    <html:option value="N">Outside Qatar</html:option>
                    </html:select>        
            </td>
         </tr>
		 
		 <tr>
  				<td class="formLabelWeblogin" width="19%">Bank Account Holder Name:</td>
			        	<td width="30%">
			        		<html:text property="insuredName" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" />
			        	</td>
   				 <td class="formLabel" width="19%">Bank Name:<span class="mandatorySymbol">*</span> </td>
  					  <td class="formLabel" width="24%">							   
							   <logic:equal name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
  					   <html:text property="bankName" styleId="state1" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
  					  </logic:equal>
  					  <logic:notEqual name="frmAddEnrollment" property="bankAccountQatarYN" value="N">	
                 		   <html:select property="bankName" styleId="state1"  styleClass="selectBox selectBoxMedium" onchange="onChangeBank('state1')" >
                   			 <html:option value="">Select from list</html:option>
                  			  <html:optionsCollection name="destnationbank" label="cacheDesc"  value="cacheId" />
                 			   </html:select>
                 			</logic:notEqual>
              		  </td> 				  
   		 </tr>		
              <tr>
                   <td width="21%" class="formLabel">Bank City: <span class="mandatorySymbol">*</span></td>
                 <td>
					<logic:equal name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
                   <html:text property="bankState" styleId="state2" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                   </logic:equal>
                  <logic:notEqual name="frmAddEnrollment" property="bankAccountQatarYN" value="N">	
                    <html:select property="bankState" styleId="state2" styleClass="selectBox selectBoxMedium" onchange="onChangeState('state2')" >
                    <html:option value="">Select from list</html:option> 
                    <html:optionsCollection name="alCityList"  label="cacheDesc" value="cacheId" />
                    </html:select>
               </logic:notEqual>
                </td>
                
					<logic:equal name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
					<td width="19%" class="formLabel">Bank Area: </td>
                 <td>
                   <html:text property="bankcity" styleId="state3" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                  </td>
                   </logic:equal>
                  <logic:notEqual name="frmAddEnrollment" property="bankAccountQatarYN" value="N">	
                  <td width="19%" class="formLabel">Bank Area:  <span class="mandatorySymbol">*</span> </td>
                 <td>
                    <html:select property="bankcity" styleId="state3" styleClass="selectBox selectBoxMedium" onchange="onChangeCity('state3')" >
                          <html:option value="">Select from list</html:option> 
                  		  <html:optionsCollection name="alDistList" label="cacheDesc" value="cacheId" /> 
                    </html:select>
                     </td>
                    </logic:notEqual>
               
                </tr>
                 <tr>
                  
					 <logic:equal name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
					 <td width="21%" class="formLabel">Bank Branch:</td>
                   <td>
                   <html:text property="bankBranchText" styleId="state4" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="60" />
                    </td>
                   </logic:equal>
                   <logic:notEqual name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
                   <td width="21%" class="formLabel">Bank Branch: <span class="mandatorySymbol">*</span></td>
                 <td>
                    <html:select property="bankBranch" styleId="state4" styleClass="selectBox selectBoxMedium"><!--  onchange="onChangeBranch('state4')"  -->
                    <html:option value="">Select from list</html:option>
                    <html:optionsCollection name="alBranchList" label="cacheDesc" value="cacheId" /> 
                    </html:select>
                      </td>
                    </logic:notEqual>
					
              
                <td width="19%" class="formLabel">Swift Code: <span class="mandatorySymbol">*</span> </td>
                 <td width="32%">
				   <logic:equal name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
                 	<html:text property="ifsc" styleClass="textBox textBoxMedium"  maxlength="60" onkeyup="lowerToUpperCase1(this);"/>
                 </logic:equal>
                 <logic:notEqual name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
                  <html:text property="ifsc" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" maxlength="60" readonly="true" disabled="true"/>
                </logic:notEqual>
                 </td>
                </tr>
                <tr>                                 
                 <td width="21%" class="formLabel">Bank Code: </td>
                 <td width="30%">
                  <html:text property="neft" styleClass="textBox textBoxMedium" maxlength="60"/>
                 </td>
                 <td width="19%" class="formLabel">IBAN Number / Bank Account No.:<span class="mandatorySymbol">*</span></td>
                 <td width="32%">
                  <html:text property="micr" styleClass="textBox textBoxMedium"  maxlength="29" onkeyup="lowerToUpperCase(this);"/>
                 </td>  
                 
                 </tr>
                   <tr>
                 <td width="21%" class="formLabel">Bank Phone No.: </td>
                 <td width="30%">
                  <html:text property="bankPhoneno" styleClass="textBox textBoxMedium" maxlength="25"/>
                 </td>
                 <td width="19%" class="formLabel">EMAIL: </td>
                 <td width="32%">
                 <html:text property="memberAddressVO.emailID" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" />
                 </td>
                </tr>
           
                <tr>
                <logic:empty name="frmAddEnrollment" property="policyGroupSeqID">
                 <td width="20%" class="formLabel">Address :</td>
                 <td width="30%"><html:text property="address1" styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" maxlength="250" value="NA" readonly="true"/></td>
                </logic:empty>
				<td class="formLabel">Country:<span class="mandatorySymbol">*</span> </td>
        <td>
         <logic:equal name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
         	<html:select property ="bankCountry" styleClass="selectBox selectBoxMedium">
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="OutsideQatarCountryList" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
         </logic:equal>
         <logic:notEqual name="frmAddEnrollment" property="bankAccountQatarYN" value="N">
        	<html:select property ="bankCountry" styleClass="selectBox selectBoxMedium" >
        	     <html:option value="">Select From List</html:option>
                 <html:options collection="countryCode" property="cacheId" labelProperty="cacheDesc"/>
          </html:select>
        </logic:notEqual>
        </td>
                </tr>
           
    </table>
    
    	
	    	<table>
				<tr>
					<%if(userSecurityProfile.getGroupID().contains("I310")){
						%>
					  <td class="formLabelWeblogin"><font color="blue"><b>Note:<b></font></td>
					   <tr>
						   <td width="50%">&nbsp;&nbsp;&nbsp;<font color="blue"><b>THE MODE OF PAYMENT IS <u>CHEQUE ISSUANCE</u>.<b></font></td>
					   </tr>
						   <tr>
					   <td width="50%">&nbsp;&nbsp;&nbsp;<font color="blue"><b>Bank details are taken only for information.<b></font></td>
					   </tr>
						<% }%>
				</tr>

				</table>
		</fieldset>
		<%} %>
		<%if(!(userSecurityProfile.getLoginType().equals("B"))) { %>
		<fieldset>
			<legend>Personal Information</legend>
			<logic:match name="frmAddEnrollment" property="MarriageHideYN" value="Y"><!--KOC FOR Grievance-->
			<table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">
				<!-- <tr>
						<td colspan="4">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
							<font color="#0C48A2"> <b><li></b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
						</td>
				</tr> -->
		      	<tr>
			        <td width="21%" class="formLabelWeblogin" nowrap>Date of Marriage:</td>
			        <td width="30%" class="formLabelWeblogin" nowrap>
						<html:text property="dateOfMarriage" styleClass="textBox textBoxSmall textBoxDisabled" maxlength="10" disabled="<%=viewAddress%>" readonly="true" />
				
					</td>
			        <!-- <td width="19%" class="formLabelWeblogin" nowrap>&nbsp;</td>
			        <td width="30%" class="formLabelWeblogin" nowrap>&nbsp;</td> -->
			        		        
			        <td align="left">Upload Photo :</td>
					<td>
							<html:file property="file" styleId="file"/>
					</td>
		      	</tr>
		      	 <tr>	
		      	 	<td></td> <td></td>
						  <td align="right"  valign="bottom">							
   									 <!-- <img src="/GetImageAction.do?mode=dohelloImage" alt="Image" width="120" height="120"  style="border:2px solid black;"/>  -->
   									 <logic:equal name="frmAddEnrollment" property="photoYN" value="Y">						
   										 <img src="/GetImageAction.do?mode=dohelloImage" alt="Image" width="120" height="120"  style="border:2px solid black;"/> 
						  			 </logic:equal>
						  			 <logic:notEqual name="frmAddEnrollment" property="photoYN" value="Y">						
   										 <img src="/ttk/images/UploadPhoto.gif" alt="Image" width="120" height="120"  style="border:2px solid black;"/> 
						 			  </logic:notEqual>
						   		</td>
						 <td colspan="2">
						 <table>
							<tr><td colspan="2">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
						</td>
						</tr></table>
						</td>
				</tr> 
	    	</table>	  
	    	</logic:match>
	    	<logic:notMatch name="frmAddEnrollment" property="MarriageHideYN" value="Y"><!--KOC FOR Grievance-->
			<table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">
			<!-- <tr>
						<td colspan="4">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
							<font color="#0C48A2"> <b><li></b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
						</td>
				</tr> -->
		      	<tr>
			        <td width="21%" class="formLabelWeblogin" nowrap>Date of Marriage:</td>
			        <td width="30%" class="formLabelWeblogin" nowrap>
						<html:text property="dateOfMarriage" styleClass="textBoxWeblogin textDate" maxlength="10" disabled="<%=viewAddress%>" />
	        			<%
	          				if(viewAddress==false)
	      					{
	        			%>
	            				<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmAddEnrollment.dateOfMarriage',document.frmAddEnrollment.dateOfMarriage.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>
	            		<%
							}//end of if(viewAddress==false)
						%>
			        </td>
			        <!-- <td width="19%" class="formLabelWeblogin" nowrap>&nbsp;</td>
			        <td width="30%" class="formLabelWeblogin" nowrap>&nbsp;</td> -->
			         <td align="left">Upload Photo :</td>
					<td>
							<html:file property="file" styleId="file" />
					</td>
		      	</tr>
		      	<tr>	
		      	<td></td> <td></td>
		      		<td align="right"  valign="bottom">							
   						 <!-- <img src="/GetImageAction.do?mode=dohelloImage" alt="Image" width="120" height="120"  style="border:2px solid black;"/>  -->
   							<logic:equal name="frmAddEnrollment" property="photoYN" value="Y">						
   										 <img src="/GetImageAction.do?mode=dohelloImage" alt="Image" width="120" height="120"  style="border:2px solid black;"/> 
						  	 </logic:equal>
						  	 <logic:notEqual name="frmAddEnrollment" property="photoYN" value="Y">						
   										 <img src="/ttk/images/UploadPhoto.gif" alt="Image" width="120" height="120"  style="border:2px solid black;"/> 
						 	 </logic:notEqual>
					</td>
					<td colspan="2">
						 <table style="margin-top: -50px;">
							<tr><td colspan="2">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
						</td>
						</tr></table>
						</td>
				</tr> 
	    	</table>
	    	</logic:notMatch>
	    	<!--KOC FOR Grievance-->
		</fieldset>
		<%} %>
		<logic:match name="frmAddEnrollment" property="v_OPT_ALLOWED" value="WSA">
		  <fieldset>
			 <legend>OPT OUT</legend>
			    <table align="center" class="formContainerWeblogin"  border="0" cellspacing="0" cellpadding="0">
			       <tr>
			<logic:notEmpty name="chkPreAuth" scope="request">
			    <td><b></b><font color="blue"><bean:message name="chkPreAuth" scope="request"/></font></b></td>
			</logic:notEmpty>
			<logic:notEmpty name="chkWindowsPeriod" scope="request">
			    <td><b></b><font color="blue"><bean:message name="chkWindowsPeriod" scope="request"/></font></b></td>
			</logic:notEmpty>
			</tr>
			  </table>
			</fieldset>
		</logic:match>
		<!--Ended- -->
		<!-- E N D : Form Fields -->
		<!-- S T A R T : Buttons -->
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  		<tr>
		    	<td width="100%" align="center">
				<logic:equal name="ActiveSubLink" value="Home">
				<%
		    	if(userSecurityProfile.getLoginType().equals("EMPL")){
		    		 if(TTKCommon.isAuthorized(request,"Edit"))
	    				{%>
							<logic:notEqual name="frmAddEnrollment" property="empStatusTypeID" value="POC">
							    <button type="button" name="Button" accesskey="s" class="buttons button_design_class" onMouseout="this.className='buttons button_design_class'" onMouseover="this.className='buttons button_design_class buttonsHover'" onClick="javascript:onSave()"><u>S</u>ave</button>&nbsp;
								<button type="button" name="Button" accesskey="r" class="buttons button_design_class" onMouseout="this.className='buttons button_design_class'" onMouseover="this.className='buttons button_design_class  buttonsHover'" onClick="javascript:onReset()"><u>R</u>eset</button>&nbsp;
								<!-- <button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseMember()"><u>C</u>lose</button> -->
							</logic:notEqual>
					<%}//end of if
		    	}%>
		    	</logic:equal>
		    	<button type="button" name="Button" accesskey="c" class="buttons button_design_class" onMouseout="this.className='buttons button_design_class'" onMouseover="this.className='buttons button_design_class buttonsHover'" onClick="javascript:onCloseWindow()"><u>C</u>lose</button><!-- close-->
		    	</td>
	    	</tr>
		</table>
	</div>
	<INPUT TYPE="hidden" NAME="mode" value="">
	<INPUT TYPE="hidden" NAME="loginType" value="<%=userSecurityProfile.getLoginType()%>">
  	<input type="hidden" name="child" value="Employee Details">

  	<!--Added for IBM.....15.3--->
	<!-------added by Rekha 13.07.2012-->
<%--added as per KOC 1261 CHANGE REQUESt --%>
        <html:hidden property="groupId" value="<%=groupId%>"/>
		<html:hidden property="OPT"/>
		<html:hidden property="EmailPh"/>
		<html:hidden property="MarriageHideYN"/>
		 <html:hidden property="genderYN"/>
		<html:hidden property="policyEndDate"/>
    	<html:hidden property="policyStartDate"/>
    	<html:hidden property="dateOfExit"/>    	
    	<html:hidden property="sumInsured"/>   	
    	<html:hidden property="empStatusDesc"/>   
    	 <html:hidden property="memberSeqID"/>

   <logic:notEmpty name="frmAddEnrollment" property="frmChanged">
   <input type="hidden" name="focusID" value="">
   <input type="hidden" name="photoYN" value="<%=photoYN%>">
	<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>
    	
		<logic:match name="frmAddEnrollment" property="v_OPT_ALLOWED" value="WSA">
	  	 <script>
		  stopOPTYN();
		 </script>
		 </logic:match>

		 <logic:match name="frmAddEnrollment" property="v_OPT_ALLOWED" value="WSA">
	  	 <script>
		   EmailMobPhVal();
		    </script>
		 </logic:match>
		 


	<!-------added by Rekha 13.07.2012-->
	<!--Ended--->

</html:form>