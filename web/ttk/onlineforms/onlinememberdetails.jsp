<%/**
 * @ (#) onlinememberdetails.jsp Jan 14th,2008
 * Project       : TTK HealthCare Services
 * File          : onlinememberdetails.jsp
 * Author        : Krupa J
 * Company       : Span Systems Corporation
 * Date Created  : Jan 14th,2008
 * @author       : Krupa J  
 * Modified by   : Ramakrishna K M
 * Modified date : Apr 05th,2008
 * Reason        : Added Javascript for blocking F5 key
 */
 %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<!-- 	// Change added for KOC1227H  -->
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,org.apache.struts.action.DynaActionForm,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<head>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/async.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/onlinememberdetails.js"></SCRIPT>

<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
 <script type="text/javascript" src="/ttk/scripts/calendar/Hcalendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/hijri-date.js"></script>
<%
//Added as per kOC 1264
    boolean viewmode=true;
boolean viewallowmodify=false;

pageContext.setAttribute("viewmode",new Boolean(viewmode));
UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	String strActiveSubLink=TTKCommon.getActiveSubLink(request);
	//Added as per kOC 1264
		pageContext.setAttribute("nationalities", Cache.getCacheObject("nationalities"));
		pageContext.setAttribute("maritalStatuses", Cache.getCacheObject("maritalStatuses"));
		pageContext.setAttribute("listRelationshipCode",Cache.getCacheObject("hrRelationship"));
%>


<script language="javascript">
	var JS_Focus_ID="<%=TTKCommon.checkNull(request.getParameter("focusID"))%>";
</script>

<script language=javascript>
document.onkeydown = function(){
	if(window.event && window.event.keyCode == 116)
    { // Capture and remap F5
    	window.event.keyCode = 505;
    }//end of if(window.event && window.event.keyCode == 116)
	if(window.event && window.event.keyCode == 505)
    { // New action for F5
    	return false;
        // Must return false or the browser will refresh anyway
    }//end of if(window.event && window.event.keyCode == 505)
}//end of function of keydown
</script>
</head>

<%
  pageContext.setAttribute("floaterNonFloater",Cache.getCacheObject("floaterNonFloater"));
  pageContext.setAttribute("relationshipCode",Cache.getCacheObject("relationshipCode"));
  pageContext.setAttribute("gender",Cache.getCacheObject("gender"));
  boolean viewname=false;
  boolean viewrelation=false;
  boolean viewgender=false;
  boolean viewdob=false;
  boolean viewage=false;
  boolean viewsum=false;
  boolean viewmemtype=false;
  boolean viewfamilysum=false;

  // Change added for KOC1227H
  DynaActionForm frmMemberDetails=(DynaActionForm)request.getSession().getAttribute("frmMemberDetails");
  String noteChange=(String)frmMemberDetails.get("noteChange");
  String EnrollNo = (String)frmMemberDetails.get("enrollmentNbr");
  String policyDate = (String)request.getSession().getAttribute("policyDate");
  String DOJ = (String)request.getSession().getAttribute("empDateOfJoining");
  String DOM = (String)request.getSession().getAttribute("EmpDateOfMarriage");
  String policyStartDate = (String)request.getSession().getAttribute("policyStartDate");
  String policyEndDate = (String)request.getSession().getAttribute("policyEndDate");
  String employeeNbr = (String)request.getSession().getAttribute("employeeNbr");
  String dateOfExit = (String)request.getSession().getAttribute("dateOfExit"); 
  String dateOfInception = (String)request.getSession().getAttribute("dateOfInception");  
  /* String memberTypeID = (String)request.getSession().getAttribute("memberTypeID"); 
   String name = (String)request.getSession().getAttribute("name"); 
   String familyName = (String)request.getSession().getAttribute("familyName"); 
  String relationTypeID = (String)request.getSession().getAttribute("relationTypeID"); 
  String genderTypeID = (String)request.getSession().getAttribute("genderTypeID"); 
  String dateOfBirth = (String)request.getSession().getAttribute("dateOfBirth"); 
  String hdateOfBirth = (String)request.getSession().getAttribute("hdateOfBirth"); 
  String age = (String)request.getSession().getAttribute("age");  
  String nationality = (String)request.getSession().getAttribute("nationality");
  String maritalStatuses = (String)request.getSession().getAttribute("maritalStatuses");  
  String emirateId = (String)request.getSession().getAttribute("emirateId"); 
  String passportNumber = (String)request.getSession().getAttribute("passportNumber");
  String file = (String)request.getSession().getAttribute("file");
  String enrollmentID = (String)request.getSession().getAttribute("enrollmentID");
  String empStatusDesc = (String)request.getSession().getAttribute("empStatusDesc");
  String endorsementNbr = (String)request.getSession().getAttribute("endorsementNbr");
  String dateOfInception = (String)request.getSession().getAttribute("dateOfInception");
  String remarks = (String)request.getSession().getAttribute("remarks"); */
  String photoYN = (String)request.getSession().getAttribute("photoYN"); 
  
%>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/OnlineMemberDetailsAction.do" method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="78%">Member Details - <bean:write name="frmMemberDetails" property="caption"/></td>
	    <td width="22%" align="right" class="webBoard">&nbsp;</td>
	  </tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Form Fields -->
	<div class="contentArea" id="contentArea">
	<logic:notEmpty name="frmMemberDetails" property="loginWindowPeriodAlert">
	<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" alt="Alert" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="frmMemberDetails" property="loginWindowPeriodAlert"/>
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
	
<!-- Changes as per KOC 1159 and 1160 -->
	<logic:notEmpty name="frmMemberDetails" property="combinationMembersLimit">
	<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	          <td><img src="/ttk/images/ErrorIcon.gif" title="Alert" alt="Alert" width="16" height="16" align="absmiddle">&nbsp;
	         <bean:write name="frmMemberDetails" property="combinationMembersLimit"/>
	      </tr>
   	 </table>
	</logic:notEmpty>

	<!-- Changes as per KOC 1159 and 1160 -->
	<html:errors/>
	<!-- Added Rule Engine Enhancement code : 27/02/2008 -->
	<ttk:BusinessErrors name="BUSINESS_ERRORS" scope="request" />
	<!--  End of Addition -->

	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" alt="Success" title="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:message name="updated" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>

	<%-- <logic:notEmpty name="updated" scope="request">
		<%String alert = (String)request.getAttribute("updated");
	 		if(alert.equalsIgnoreCase("message.savedSuccessfully"))
	 		{%>
		 		<script>
		  		window.alert("Dependent Record updated successfully!");
		 		</script>
	 		<%}
	 		else if(alert.equalsIgnoreCase("message.addedSuccessfully"))
	 		{
		 		%>
		 		<script>
		 		window.alert("Dependent Record added successfully!");
		 		</script>
		 	<%
	 		}%>

	</logic:notEmpty> --%>

	<!-- Added for Oracle alert Message on Deletion of Records -->
	
	<logic:notEmpty name="alert" scope="request">
	
		<%String oraclealert = (String)request.getAttribute("alert");
		String rownum = (String)request.getAttribute("rownum");
		
				
		if(oraclealert.equalsIgnoreCase("message.oracle"))
		{
			%>
			<script>
			 var rownum = '<%=rownum%>';
			 var mode = "doconfirmDelete";
			 var msg =  window.confirm("Once you delete the parents, you will not be able to re-enroll until employed with Oracle.");
			 if(msg)
			 {
				 checkDelete(rownum,mode);
			 }
			</script>
			<%
		}
		else if(oraclealert.equalsIgnoreCase("message.others"))
		{
			%>
			<script>
			 var rownum = '<%=rownum%>';
			 var mode = "doconfirmDelete";
			 var msg = window.confirm("Are you sure you want to delete the selected record ?");
			 if(msg)
			 {
				 checkDelete(rownum,mode);
			 }
			</script>
			<%
		}
		
		%>
	</logic:notEmpty>
	




	<!-- E N D : Success Box -->
	<!--Added for IBM....koc-1216-->

		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<logic:notEmpty name="CheckMaleValidation" scope="request">
					<td><img src="/ttk/images/ErrorIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;<b></b><font color="red"><bean:message name="CheckMaleValidation" scope="request"/></font></b></td>
					</logic:notEmpty>
					<logic:notEmpty name="CheckFemaleValidation" scope="request">
					<td><img src="/ttk/images/ErrorIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;<b></b><font color="red"><bean:message name="CheckFemaleValidation" scope="request"/></font></b></td>
				</logic:notEmpty>
			</tr>
			</table>

	<!--Ended---->
	<logic:notEmpty name="configurationinfo" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" alt="Success" title="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:message name="configurationinfo" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>

	<logic:notEmpty name="frmMemberDetails" property="display">
		<div class="scrollableGrid" style="height:290px;">
			<fieldset>
					<legend>Member List</legend>
					<!-- S T A R T : Grid -->
					<ttk:HtmlGrid name="tableData" className="gridWithCheckBox zeroMargin"/>
					<!-- E N D : Grid -->

						<%-- <table width="98%" style="margin-top:5px;border:1px solid #A0A8BB;" border="0" align="center" cellpadding="0" cellspacing="0" >
						    <tr style="height:22px;">
						    <logic:notEmpty name="frmMemberDetails" property="floaterSumStatus">
							    <logic:notMatch name="frmMemberDetails" property="floaterSumStatus" value="HIDE">
			        			<logic:match name="frmMemberDetails" property="floaterSumStatus" value="SHOW"><%viewfamilysum=true;%></logic:match>
				        			<td align="right" nowrap>Floater Sum insured:&nbsp;&nbsp;</td>
				          				<td width="2%"><html:text property="floaterSumInsured"  maxlength="17" styleClass="textBoxWeblogin textBoxSmall" disabled="<%= viewfamilysum %>"/></td>
				          				&nbsp;
				        		</logic:notMatch>
				        	</logic:notEmpty>
				        	<logic:match name="frmMemberDetails" property="familySumIconYN" value="Y">
							    <td align="right">Family Additional Sum Insured:&nbsp;&nbsp;</td>
							    <td><a href="javascript:onRatesIconFamily()"><img src="/ttk/images/RatesIcon.gif" alt="Family Sum Insured" title="Family Sum Insured" width="16" height="16" border="0" align="absmiddle"></a>&nbsp;</td>
						    	</tr>
							</logic:match>
						</table> --%>
			</fieldset>
		
			</div>
	</logic:notEmpty>
  
	<!-- S T A R T : Buttons -->
	<logic:match name="frmMemberDetails" property="policyOPT" value="Y">
	<logic:equal name="frmMemberDetails" property="allowAddYN" value="N">
	</logic:equal>
	<logic:notEqual name="frmMemberDetails" property="allowAddYN" value="N">
		<fieldset>
		<legend>Member Information</legend>
		  	<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
		  	  <tr style="padding-top:10px;">
		  	  		<td align="left" nowrap>Employee No.:<span class="mandatorySymbol">*</span><br>
				          <html:text property="employeeNbr" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="150" readonly="true"  style="background-color: #EEEEEE;" />
					</td>
					 <td align="left" nowrap>Principal Name:<span class="mandatorySymbol">*</span><br>
				          <html:text property="insuredName" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="250" readonly="true" style="background-color: #EEEEEE;"/>
					   </td>
					  <%--  <td align="left" nowrap>VIP:<!-- <span class="mandatorySymbol">*</span> --><br>
								<html:select property="vipYN" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" disabled="true">
  	 		  						<html:option value="N">NO</html:option>
		    		 				 <html:option value="Y">YES</html:option>
								</html:select>
					</td> --%>
		  	  </tr>
		  	  
		      <tr style="padding-top:10px;">
		      <logic:notEmpty name="frmMemberDetails" property="nameStatus">
			      <logic:notMatch name="frmMemberDetails" property="nameStatus" value="HIDE">
			      	<logic:match name="frmMemberDetails" property="nameStatus" value="SHOW"><%viewname=true;%></logic:match>
				      	<td align="left" nowrap>Member Name:<logic:match name="frmMemberDetails" property="nameMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>
				      	<logic:notEqual name="frmMemberDetails" property="relationID" value="NSF">
				          <html:text property="name"  styleId="memberNameId" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="250" disabled="<%= viewname %>" onkeyup="ConvertToUpperCase(event.srcElement);" />
				        </logic:notEqual>
				        <logic:equal name="frmMemberDetails" property="relationID" value="NSF">
						  <html:text property="name" styleId="memberNameId" styleClass="textBoxWeblogin textBoxMediumWeblogin" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="250" disabled="true"/>
			    		  <input type="hidden" name="name" value="<bean:write name="frmMemberDetails" property="name"/>">
					    </logic:equal>
					   </td>
			      </logic:notMatch>
			  </logic:notEmpty>

		   <%--   <logic:notEmpty name="frmMemberDetails" property="memTypeStatus">
		        <logic:notMatch name="frmMemberDetails" property="memTypeStatus" value="HIDE">
			        <td align="left" nowrap>Member Type:<logic:match name="frmMemberDetails" property="memTypeMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match>&nbsp;&nbsp;&nbsp;<br>
					<logic:match name="frmMemberDetails" property="memTypeStatus" value="SHOW"><%viewmemtype=true;%></logic:match>
					<html:select property="memberTypeID" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" disabled="<%= viewmemtype %>">
			           <html:optionsCollection name="floaterNonFloater" label="cacheDesc" value="cacheId" />
			        </html:select>
			        <html:hidden property="memberTypeID"/>
			       	</td>
		       	</logic:notMatch>
		     </logic:notEmpty> --%>
		     
					<td align="left" nowrap>Family Name:<br>
				          <html:text property="familyName" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="250"disabled="true"style="background-color: #EEEEEE;" />
					   </td>
				 <logic:notEqual property="relationID" name="frmMemberDetails" value="NSF">
		     <logic:notEmpty name="frmMemberDetails" property="relationStatus">
		        <logic:notMatch name="frmMemberDetails" property="relationStatus" value="HIDE">
			    <td align="left" nowrap>Relationship:
			    <logic:match name="frmMemberDetails" property="relationMandatoryYN" value="Y">
			    <logic:empty name="memberName" scope="session">
			    <span class="mandatorySymbol">*</span>
			    </logic:empty>
			    </logic:match>&nbsp;&nbsp;&nbsp;<br>
			        <logic:match name="frmMemberDetails" property="relationStatus" value="SHOW"><%viewrelation=true;%></logic:match>
			      <%--   <logic:notEqual property="relationTypeID" name="frmMemberDetails" value="NSF"> --%>	
			       <logic:empty name="memberName" scope="session">
			        	<html:select property="relationTypeID"styleId="relation" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" onchange="onRelationshipChange();" disabled="<%= viewrelation %>">
				        	<html:option value="">Select from list</html:option>
				            <html:optionsCollection name="frmMemberDetails" property="alRelationShip"  label="cacheDesc" value="cacheId" />
			            </html:select>
			            </logic:empty>
			            <logic:notEmpty name="memberName" scope="session">
			            <html:select property="relationTypeID"styleId="relation" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" onchange="onRelationshipChange();" disabled="true">
				        	<html:option value="">Select from list</html:option>
				            <html:optionsCollection name="frmMemberDetails" property="alRelationShip"  label="cacheDesc" value="cacheId" />
			            </html:select>
			            </logic:notEmpty>
			        </td>
		        </logic:notMatch>
		     </logic:notEmpty>
		     </logic:notEqual>
		     <logic:equal property="relationID" name="frmMemberDetails" value="NSF">	
		     <logic:notEmpty name="frmMemberDetails" property="relationStatus">
		        <logic:notMatch name="frmMemberDetails" property="relationStatus" value="HIDE">
			    <td align="left" nowrap>Relationship:<logic:match name="frmMemberDetails" property="relationMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match>&nbsp;&nbsp;&nbsp;<br>
			        <logic:match name="frmMemberDetails" property="relationStatus" value="SHOW"><%viewrelation=true;%></logic:match>
			        	<html:select property="relationTypeID" styleClass="selectBox selectBoxMedium" style="background-color: #EEEEEE;" readonly="true" disabled="false">
         						 <html:option value="NSF#OTH">Principal</html:option>
    					  </html:select>
			        </td>
		        </logic:notMatch>
		     </logic:notEmpty>
		     </logic:equal>

		     <logic:notEmpty name="frmMemberDetails" property="genderStatus">
		         <logic:notMatch name="frmMemberDetails" property="genderStatus" value="HIDE">
		         	<td align="left" nowrap id="type" >Gender:
		         	<logic:match name="frmMemberDetails" property="genderMandatoryYN" value="Y">
		         	 <logic:empty name="memberName" scope="session">
		         	<span class="mandatorySymbol">*</span>
		         	</logic:empty>
		         	</logic:match><br>
				         <logic:match name="frmMemberDetails" property="genderStatus" value="SHOW"><%viewgender=true;%></logic:match>
				         <logic:equal name="frmMemberDetails" property="genderYN" value="OTH">
				         <%-- <logic:notEmpty property="genderTypeID" name="frmMemberDetails"> --%>
				         <logic:notEmpty name="memberName" scope="session">
				         <html:select property="genderTypeID" styleId="genderId" styleClass="selectBoxWeblogin selectBoxWeblogin" disabled="true">
				           		<html:option value="">Select from list</html:option>
				           		<html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
				           </html:select>
				         </logic:notEmpty>
				        <logic:empty name="memberName" scope="session">
				         <html:select property="genderTypeID" styleId="genderId" styleClass="selectBoxWeblogin selectBoxWeblogin" disabled="<%= viewgender %>">
				           		<html:option value="">Select from list</html:option>
				           		<html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
				           </html:select>
				         </logic:empty>
				          <%--  <html:select property="genderTypeID" styleId="genderId" styleClass="selectBoxWeblogin selectBoxWeblogin" disabled="<%= viewgender %>">
				           		<html:option value="">Select from list</html:option>
				           		<html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
				           </html:select> --%>
				            &nbsp;&nbsp;&nbsp;&nbsp;
				       	  </logic:equal>
				       	  <logic:notEqual name="frmMemberDetails" property="genderYN" value="OTH">
					       	  <html:select property="genderTypeID" styleClass="selectBoxWeblogin selectBoxDisabledWeblogin" disabled="true">
								  <html:option value="">Select from list</html:option>
							      <html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
							  </html:select>
							  <input type="hidden" name="genderTypeID" value="<bean:write name="frmMemberDetails" property="genderYN"/>" disabled="false">
				       	  </logic:notEqual>
			       	 </td>
		         </logic:notMatch>
		     </logic:notEmpty>
	      	 <td width="15%" nowrap colspan="2"></td>
		     </tr>

		      <tr>
		      <%-- 
		      <%
			  	if(EnrollNo.contains("I310"))
			  	{
			  		
			  		      		%>
			  		      		<logic:notEmpty name="frmMemberDetails" property="dobStatus">
			  			      	<logic:notMatch name="frmMemberDetails" property="dobStatus" value="HIDE">
			  			      	<logic:match name="frmMemberDetails" property="dobStatus" value="SHOW"><%viewdob=true;%></logic:match>
			  				        <td align="left" nowrap >Date of Birth(DD/MM/YYYY):<logic:match name="frmMemberDetails" property="dobMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>

			  						 <logic:equal name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			  				          <html:text property="dateOfBirth" maxlength="10" styleClass="textBoxWeblogin textDate" onblur="javascript:onIBMDateofBirth()" disabled="<%= viewdob %>" />
			  						 </logic:equal>
			  				         <logic:notEqual name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			  				          <html:text property="dateOfBirth" maxlength="10" styleClass="textBoxWeblogin textDate" onblur="javascript:onIBMDateofBirth()" disabled="<%= viewdob %>" onchange="javascript:onChangeAge()"/>
			  						 </logic:notEqual>
			  					  		&nbsp;<logic:match name="frmMemberDetails" property="dobStatus" value="EDIT"><a name="CalendarObjectBirthDate" id="CalendarObjectBirthDate" href="#" onClick="javascript:show_calendar('CalendarObjectBirthDate','frmMemberDetails.dateOfBirth',document.frmMemberDetails.dateOfBirth.value,'',event,148,178),onChangeAge();return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" title="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a></logic:match>
			  						</td>
			  					</logic:notMatch>
			  				  </logic:notEmpty>

			  				 <logic:notEmpty name="frmMemberDetails" property="ageStatus">
			  			        <logic:notMatch name="frmMemberDetails" property="ageStatus" value="HIDE">
			  					<logic:match name="frmMemberDetails" property="ageStatus" value="SHOW"><%viewage=true;%></logic:match>
			  						<td align="left" nowrap 10px;>Age:<logic:match name="frmMemberDetails" property="ageMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>
			  				           <logic:equal name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			  				            <html:text property="age"  maxlength="3" styleId="ageid" styleClass="textBoxWeblogin textBoxSmall" disabled="<%= viewage %>" />
			  					          &nbsp;
			  				           </logic:equal>
			  				         <logic:notEqual name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			  				        	 <html:text property="age"  maxlength="3" styleId="ageid" styleClass="textBoxWeblogin textBoxSmall" disabled="<%= viewage %>" onchange="javascript:onChangeAge()"/>
			  					          &nbsp;
			  						 </logic:notEqual>
			  				        </td>
			  			        </logic:notMatch>
			  			         <logic:match name="frmMemberDetails" property="ageStatus" value="HIDE">
			  			        	<input type="hidden" name="age" value="">
			  			        </logic:match>
			  			     </logic:notEmpty>
			  				<%
			  		}
			  	 else
			  	 {
			  			
			  		 %>

		      			<logic:notEmpty name="frmMemberDetails" property="dobStatus">
		      			<logic:notMatch name="frmMemberDetails" property="dobStatus" value="HIDE">
		      			<logic:match name="frmMemberDetails" property="dobStatus" value="SHOW"><%viewdob=true;%></logic:match>
			        	<td align="left" nowrap >Date of Birth(DD/MM/YYYY):<logic:match name="frmMemberDetails" property="dobMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>

					 	<logic:equal name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			          	<html:text property="dateOfBirth" maxlength="10" styleClass="textBoxWeblogin textDate" onblur="javascript:onDateofBirth()" disabled="<%= viewdob %>" />
					 	</logic:equal>
			         	<logic:notEqual name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			          	<html:text property="dateOfBirth" maxlength="10" styleClass="textBoxWeblogin textDate" onblur="javascript:onDateofBirth()" disabled="<%= viewdob %>" onchange="javascript:onChangeAge()"/>
					 	</logic:notEqual>
				  		&nbsp;<logic:match name="frmMemberDetails" property="dobStatus" value="EDIT"><a name="CalendarObjectBirthDate" id="CalendarObjectBirthDate" href="#" onClick="javascript:show_calendar('CalendarObjectBirthDate','frmMemberDetails.dateOfBirth',document.frmMemberDetails.dateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" title="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle"></a></logic:match>
					</td>
					</logic:notMatch>
			  		</logic:notEmpty>

			 	<logic:notEmpty name="frmMemberDetails" property="ageStatus">
		        <logic:notMatch name="frmMemberDetails" property="ageStatus" value="HIDE">
				<logic:match name="frmMemberDetails" property="ageStatus" value="SHOW"><%viewage=true;%></logic:match>
					<td align="left" nowrap 10px;>Age:<logic:match name="frmMemberDetails" property="ageMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>
			           <logic:equal name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			            <html:text property="age"  maxlength="3" styleId="ageid" styleClass="textBoxWeblogin textBoxSmall" disabled="<%= viewage %>" />
				          &nbsp;
			           </logic:equal>
			         <logic:notEqual name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			        	 <html:text property="age"  maxlength="3" styleId="ageid" styleClass="textBoxWeblogin textBoxSmall" disabled="<%= viewage %>" onchange="javascript:onChangeAge()"/>
				          &nbsp;
					 </logic:notEqual>
			        </td>
		        </logic:notMatch>
		         <logic:match name="frmMemberDetails" property="ageStatus" value="HIDE">
		        	<input type="hidden" name="age" value="">
		        </logic:match>
		     </logic:notEmpty>
		      <%
			 	}
		      %>
			 <logic:notEmpty name="frmMemberDetails" property="sumInsuredStatus">
				<logic:notMatch name="frmMemberDetails" property="sumInsuredStatus" value="HIDE">
		        <logic:match name="frmMemberDetails" property="sumInsuredStatus" value="SHOW"><%viewsum=true;%></logic:match>

			     <logic:equal name="frmMemberDetails" property="memberTypeID" value="PFL">
					<td align="left" id="totsumins" nowrap 10px;>Family Sum Insured:<logic:match name="frmMemberDetails" property="sumInsuredMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>
                 </logic:equal>
                 <logic:notEqual name="frmMemberDetails" property="memberTypeID" value="PFL">
					<td align="left" id="totsumins" nowrap 10px;>Sum Insured:<logic:match name="frmMemberDetails" property="sumInsuredMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>
                 </logic:notEqual>

		       	 <html:text property="totalSumInsured"  maxlength="17" styleClass="textBoxWeblogin textBoxSmall" disabled="<%= viewsum %>"/>
			     &nbsp;
			     <logic:equal name="frmMemberDetails" property="addSumIconYN" value="Y">
			     <logic:notEqual name="frmMemberDetails" property="sumInsIconEditYN" value="N">
			      <a href="#" onClick="onRatesIconDetails()"><img src="/ttk/images/RatesIcon.gif" alt="Additional SumInsured" title="Additional SumInsured" width="16" height="16" border="0" align="center" disabled="<%= viewsum %>"></a>
			     </logic:notEqual>
			    </logic:equal>
			        </td>
		        </logic:notMatch>
		     </logic:notEmpty> --%>
		   	  <td align="left" nowrap>Date of Birth:
		   	  <logic:empty property="dateOfBirth" name="frmMemberDetails">
		   	   <span class="mandatorySymbol">*</span>
		   	  </logic:empty>
		   	  <br>
		   	  <logic:empty property="dateOfBirth" name="frmMemberDetails">
								<html:text property="dateOfBirth" styleId="Dob" styleClass="textBox textDate" onfocus="convertToHDate('HDob')" onblur="javascript:convertToHDate('HDob');onDateofBirth();" maxlength="10"/>
    							<a name="CalendarObjectdobDate" id="CalendarObjectdobDate" href="#" onClick="javascript:show_calendar('CalendarObjectdobDate','frmMemberDetails.dateOfBirth',document.frmMemberDetails.dateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="dobDate" width="24" height="17" border="0" align="absmiddle"></a>
    							</logic:empty>
    							<logic:notEmpty property="dateOfBirth" name="frmMemberDetails">
    							<html:text property="dateOfBirth" styleId="Dob" styleClass="textBox textDate" onfocus="convertToHDate('HDob')" onblur="javascript:convertToHDate('HDob');onDateofBirth();" maxlength="10" style="background-color: #EEEEEE;" readonly="true"/>
    							<a name="CalendarObjectdobDate" id="CalendarObjectdobDate" href="#"  onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="dobDate" width="24" height="17" border="0" align="absmiddle"></a>
    							</logic:notEmpty>
    							<logic:empty property="hdateOfBirth" name="frmMemberDetails">
    							<html:text property="hdateOfBirth" styleId="HDob" styleClass="textBox textDate" maxlength="10" onfocus="convertToGDate('Dob')" onblur="javascript:convertToGDate('Dob');onDateofBirth();"/>					
  							  	<a name="HCalendarObjectdobDate" ID="HDob" HREF="#" onClick="javascript:show_Hcalendar('HDob','forms[1].hdateOfBirth',document.forms[1].hdateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Hijri-Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/HCalendarIcon.gif" alt="Hijri-Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
  							  	</logic:empty>  
  							  	<logic:notEmpty property="hdateOfBirth" name="frmMemberDetails">
  							  	<html:text property="hdateOfBirth" styleId="HDob" styleClass="textBox textDate" maxlength="10" onfocus="convertToGDate('Dob')" onblur="javascript:convertToGDate('Dob');onDateofBirth();" style="background-color: #EEEEEE;" readonly="true"/>					
  							  	<a name="HCalendarObjectdobDate" ID="HDob" HREF="#" o onMouseOver="window.status='Hijri-Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/HCalendarIcon.gif" alt="Hijri-Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
  							  	</logic:notEmpty> 
    			</td>
    			 <td align="left" nowrap>Age (Yrs):<span class="mandatorySymbol">*</span><br>
								<logic:notEmpty name="frmMemberDetails" property="dateOfBirth">
	 								   <html:text styleId="ageid" property="age" styleClass="textBox textBoxTiny" maxlength="3" readonly="true"/>
   									 </logic:notEmpty>
   									 <logic:empty name="frmMemberDetails" property="dateOfBirth">
									    <html:text styleId="ageid" property="age" styleClass="textBox textBoxTiny" maxlength="3"/>
   									 </logic:empty></td>				   		     
		     
		   		  <td align="left" nowrap id="type" >Nationality:<span class="mandatorySymbol">*</span><br>
				           <html:select property="nationality" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" >
				         	  	<html:option value="">Select from list</html:option>
				           		  <html:optionsCollection name="nationalities" label="cacheDesc" value="cacheId" /> 
				           </html:select>			       			
			       	 </td>
		     
 					<td align="left" nowrap id="type" >Marital Status:<span class="mandatorySymbol">*</span><br>
				           <logic:empty property="maritalStatus" name="frmMemberDetails">
				           <html:select property="maritalStatus" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" disabled="false">
				           		<html:option value="">Select from list</html:option>
		     					 <html:optionsCollection name="maritalStatuses" label="cacheDesc" value="cacheId" /> 
				           </html:select>
				           </logic:empty>			       			
				           <logic:notEmpty property="maritalStatus" name="frmMemberDetails">
				           <html:select property="maritalStatus" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" disabled="true">
				           		<html:option value="">Select from list</html:option>
		     					 <html:optionsCollection name="maritalStatuses" label="cacheDesc" value="cacheId" /> 
				           </html:select>
				           </logic:notEmpty>			       			
			       	 </td>
		       <td colspan="2" align="center" nowrap >&nbsp;</td>
		        </tr>
		        <tr>
		        	<td align="left" nowrap>Qatar ID Number:<span class="mandatorySymbol">*</span><br>
				          <html:text property="emirateId" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="11" disabled="<%= viewname %>"/>
					   </td>
		        <td align="left" nowrap>Passport Number:<span class="mandatorySymbol">*</span><br>
				          <html:text property="passportNumber" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="20" disabled="<%= viewname %>"  />
					   </td>
		        <td align="left">Upload Photo :</td>
							<td>
								<html:file property="file" styleId="file"/>
							</td>
						 		 <td align="right"  valign="bottom">							
   									<logic:equal name="frmMemberDetails" property="photoYN" value="Y">						
   										 <img src="/GetImageAction.do?mode=dohelloImage" alt="Image" width="120" height="120"  style="border:2px solid black;"/> 
						  			 </logic:equal>
						  			 <logic:notEqual name="frmMemberDetails" property="photoYN" value="Y">						
   										 <img src="/ttk/images/UploadPhoto.gif" alt="Image" width="120" height="120"  style="border:2px solid black;"/> 
						 			  </logic:notEqual>
						   		</td>
						   
		        </tr>
		        <tr>
						<td colspan="4">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
							<!-- <font color="#0C48A2"> <b><li></b>Please ensure you have entered all mandatory information before uploading the Photo.</font> -->
						</td>
				</tr>
		      </table>
	    </fieldset>
 		 <fieldset>
			<legend>Member Policy Information</legend>
		 	 	<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
		 	 	<tr>
		  		<td width="20%" class="formLabel">Alkoot ID:</td>
    				<td width="30%" class="textLabelBold">
    					<bean:write name="frmMemberDetails" property="enrollmentID"/>
    				</td>		
    			</tr> 
    			<tr></tr><tr></tr> 	
    			<tr>
    				<td class="formLabel">Status:</td>
			      	    <td colspan="0" class="formLabelBold">
	         			 	<bean:write name="frmMemberDetails" property="empStatusDesc" />
	      			</td>
	      			<td class="formLabel">Endorsement No.:</td>
	      			   <td colspan="0" class="formLabelBold">
	      				<bean:write name="frmMemberDetails" property="endorsementNbr" />
	      			</td>
    			</tr>
    			<tr>
    		 	<logic:notEmpty name="frmMemberDetails" property="memberSeqID">
			        	<td class="formLabelWeblogin">Date of Inception:<span class="mandatorySymbol">*</span></td>
			        	<td>
			        		<html:text property="dateOfInception" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" readonly="true" style="background-color: #EEEEEE;" />			
			        		<logic:empty name="frmMemberDetails" property="memberSeqID">        		
		            					<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmMemberDetails.dateOfInception',document.frmMemberDetails.dateOfInception.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>	            		
			        	</logic:empty>
			        	</td>
			        	</logic:notEmpty>
			        	<logic:empty name="frmMemberDetails" property="memberSeqID">
			        	<td class="formLabelWeblogin">Date of Inception:<span class="mandatorySymbol">*</span></td>
			        	<td>
			        		<html:text property="dateOfInception" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;"/>			        		
		            					<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmMemberDetails.dateOfInception',document.frmMemberDetails.dateOfInception.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>	            		
			        	</td>
			        	</logic:empty>
			        	<td class="formLabelWeblogin">Date of Exit: </td>
			        	<td>
			        		<html:text property="dateOfExit" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" readonly="true" style="background-color: #EEEEEE;"/>	        			
		            				<!-- <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmMemberDetails.dateOfExit',document.frmMemberDetails.dateOfExit.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>		            		 -->
			        	</td>
			      	</tr>
			      	<tr>
			      		<td class="formLabel">Category:</td>
			      	</tr>
			      	<tr>
			      	</tr>
			      	<tr>
			      	<td align="left" nowrap>Remarks:</td>
			      	<td colspan="5"><html:textarea property="remarks" cols="60" rows="2" /></td>
			      	</tr>
    			
	    </table>
	   </fieldset>
	 
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
    </logic:notEqual>
    </logic:match>

    <logic:match name="frmMemberDetails" property="policyOPT" value="Y">
	    <logic:notEqual name="frmMemberDetails" property="allowAddYN" value="N">
	    <%
			if(EnrollNo.contains("I310-01")||(EnrollNo.contains("I310-001")))
			{%>
				<font color="#0C48A2">
		 		<b>Declaration:</b>
		 		</font>
				<table>
				<tr>
				<td><input type=checkbox id="Accept" name="confirm" value="Y" disabled="true">&nbsp;I hereby declare that the details as given for enrollment of my son/daughter are correct and confirm that they are unmarried and financially dependent on me and have not established their own independent household.  If any information provided by me is found to be incorrect or false,I understand that I may be asked to submit supporting documents (i.e., government approved id card, educational details, etc) for validation. If the company determines that any false information has been provided, this will be construed as a BCG Violation and appropriate action will be taken, which may include termination of employment.
				</tr>
				</table>
				<%
			}%>
		</logic:notEqual>
		</logic:match>

	<%
		    if(TTKCommon.isAuthorized(request,"Edit") && noteChange.equals("PNF"))
		    {
		%>
	<table>
    <tr>
	 		<td>
				<font color="#0C48A2">
				<b>Note:</b> Date of Inception cannot be back dated more than 30 days from current date, for More than 30 days back dated Inception please contact Alkoot Team<br>
				</font>
			</td>
		 </tr>
		 <tr>
						<td colspan="4">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before saving.</font>
							<!-- <font color="#0C48A2"> <b><li></b>Please ensure you have entered all mandatory information before uploading the Photo.</font> -->
						</td>
				</tr>
		      </table>
			  <%
			}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	      //Added as per kOC 1264
     if (userSecurityProfile.getGroupID().equalsIgnoreCase("A0994") && (strActiveSubLink.equalsIgnoreCase("Enrollment")))
		{
			%>
			 	<%--Added as per kOC 1264 --%>
			<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
			<tr>
			<td><b>Note:</b></td>
			</tr>
			<ul>
			<tr>
			<td>
			<font color="#0C48A2"> <li>Parents Coverage is restricted to Rs.100000(1 Lakh).
			</font>
			</td>
			</tr>
			</ul>
			 </table>
			<%
		
	   }
    %>
      <%--Added as per kOC 1264 --%>



    <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="4" align="center" nowrap style="padding-top:10px;">&nbsp;&nbsp;&nbsp;
		<%
		    if(TTKCommon.isAuthorized(request,"Edit"))
		    {
		%>
		<logic:notMatch name="frmMemberDetails" property="policyStatusTypeID" value="POC">
		<logic:match name="frmMemberDetails" property="policyOPT" value="Y">
        <logic:match name="frmMemberDetails" property="allowAddYN" value="Y">
			<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave to List</button>&nbsp;
			<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onReset()"><u>R</u>eset</button>&nbsp;
	    </logic:match>
	    </logic:match>
	    </logic:notMatch>
		<%
			}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	    %>
		<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
        </td>
        </tr>
    </table>
 
   
	<!-- E N D : Buttons -->
	</div>

	<html:hidden property="policySeqID"/>
	<html:hidden property="policyGroupSeqID"/>
	<html:hidden property="prodPlanSeqID"/>
	<html:hidden property="loginDate"/>
	<html:hidden property="insuredName"/>
	<html:hidden property="relationID"/>
    <html:hidden property="genderYN"/>
    <html:hidden property="dobStatus"/>
    <html:hidden property="ageStatus"/>
    <html:hidden property="sumInsuredStatus"/>
    <html:hidden property="memberSeqID"/>
    <html:hidden property="planAmt"/>
    <html:hidden property="enrollmentNbr"/>
	<html:hidden property="ageDeclaration"/>
	
	<%--  <html:hidden property="name"/>	
	<html:hidden property="relationTypeID"/>
	<html:hidden property="genderTypeID"/>
	<html:hidden property="dateOfBirth"/>
	<html:hidden property="hdateOfBirth"/>
	<html:hidden property="age"/>
	<html:hidden property="nationality"/>
	<html:hidden property="emirateId"/>
	<html:hidden property="passportNumber"/> --%>


	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<input type="hidden" name="focusID" value="">
	<input type="hidden" name="policyDate" value="<%=policyDate%>">
	<input type="hidden" name="DOJ" value="<%=DOJ%>">
	<input type="hidden" name="DOM" value="<%=DOM%>">
 	<input type="hidden" name="policyEndDate" value="<%=policyEndDate%>">
	<input type="hidden" name="policyStartDate" value="<%=policyStartDate%>">
	<input type="hidden" name="employeeNbr" value="<%=employeeNbr%>">
 	<input type="hidden" name="dateOfExit" value="<%=dateOfExit%>">
 	<input type="hidden" name="actualMemberName" id='actualMemberName' value='${sessionScope.memberName}'>
 	<INPUT TYPE="hidden" NAME="loginType" value="<%=userSecurityProfile.getLoginType()%>">
	<%--<input type="hidden" name="dateOfInception" value="<%=dateOfInception%>">
	
	 <input type="hidden" name="memberTypeID" value="<%=memberTypeID%>">
	 <input type="hidden" name="name" value="<%=name%>">
	 	 <input type="hidden" name="familyName" value="<%=familyName%>">
	<input type="hidden" name="relationTypeID" value="<%=relationTypeID%>">
	<input type="hidden" name="genderTypeID" value="<%=genderTypeID%>">
	<input type="hidden" name="dateOfBirth" value="<%=dateOfBirth%>">
	<input type="hidden" name="hdateOfBirth" value="<%=hdateOfBirth%>">
	<input type="hidden" name="age" value="<%=age%>">
	<input type="hidden" name="nationality" value="<%=nationality%>">
	<input type="hidden" name="maritalStatuses" value="<%=maritalStatuses%>">
	<input type="hidden" name="emirateId" value="<%=emirateId%>">
	<input type="hidden" name="passportNumber" value="<%=passportNumber%>">
 	<input type="hidden" name="file" value="<%=file%>">
	<input type="hidden" name="enrollmentID" value="<%=enrollmentID%>">
	<input type="hidden" name="empStatusDesc" value="<%=empStatusDesc%>">
	<input type="hidden" name="endorsementNbr" value="<%=endorsementNbr%>">
 	<input type="hidden" name="dateOfInception" value="<%=dateOfInception%>">
	<input type="hidden" name="remarks" value="<%=remarks%>"> --%>
				
	<input type="hidden" name="photoYN" value="<%=photoYN%>">
	
	

	<logic:match name="frmMemberDetails" property="policyOPT" value="Y">
	<%
		if(EnrollNo.contains("I310-01")||(EnrollNo.contains("I310-001")))
		{%>

				<script>
				ageDeclaration();
				</script>
		<%}%>
	</logic:match>

	<logic:notEmpty name="frmMemberDetails" property="frmChanged">
		<script> ClientReset=false;TC_PageDataChanged=true;</script>
	</logic:notEmpty>
	</html:form>

	<!-- E N D : Content/Form Area -->