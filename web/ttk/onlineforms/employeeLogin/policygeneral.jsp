<%
/**
 * @ (#) policygeneral.jsp July 24, 2007
 * Project      : TTK HealthCare Services
 * File         : policygeneral.jsp
 * Author       : Krupa J	
 * Company      : Span Systems Corporation
 * Date Created : July 24, 2007
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.WebBoardHelper,com.ttk.common.TTKCommon,com.ttk.dto.usermanagement.UserSecurityProfile"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,org.apache.struts.action.DynaActionForm,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>

<script>
function Close()
{
	if(!TrackChanges()) return false;
	document.forms[1].mode.value="doClose";
	document.forms[1].action="/OnlinePolicyDetailsAction.do";
	document.forms[1].submit();
}//end of Close()
function goToHome(){
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doDefault";
	document.forms[1].action="/EmployeeHomeAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}
function onSaveDependent(){
	if(!JS_SecondSubmit)
	{
	document.forms[1].mode.value="doSaveDependentInfo";
	document.forms[1].action="/SaveEmplMemberDetailsAction.do";
	JS_SecondSubmit=true;
	document.forms[1].submit();
	}
}
function onIECards()
{

	var openPage = "/OnlineMemberAction.do?mode=doIECards";
	   var w = screen.availWidth - 10;
	   var h = screen.availHeight - 49;
	   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
	   window.open(openPage,'',features);
	//if(!TrackChanges()) return false;
	//document.forms[1].mode.value="doIECards";
	//document.forms[1].target="_blank";
	//document.forms[1].action="/OnlineMemberAction.do";
	//document.forms[1].submit();
}//end of Close()
</script>

<style type="text/css">
.save_class{
    background-color: rgb(0, 26, 102);
    font-weight: bold;
    width: 8%;
    height: 31px;
    color: #fff;
    text-align: center;
    padding: 8px;
    padding-right: 12px;
    padding-left: 0px;
}
/* FIELDSET {
    width: 90%;
    border: 1px solid #CCCCCC;
    padding-top: 0px;
    padding-bottom: 10px;
} */

/* .button_design_class {
    display: block;
    height: 13%;
    background-color: rgb(0, 26, 102);
    padding: 3px;
    text-align: center;
    border-radius: 5px;
    color: white;
    text-decoration: none;
    width: 50%;
    font-size: 14px;
    float: none;
} */

.buttonsContainer {
    background-color: #FFFFFF;
    margin-top: 20px;
    padding: 5px 5px 5px 5px;
    width: 98%;
}
.successContainer, .errorContainer{
margin-left: -38px;
}


.modify_text_class{
color: #a83108;
}
</style>

<%
	UserSecurityProfile userSecurityProfile=(UserSecurityProfile)request.getSession().getAttribute("UserSecurityProfile");
	String PolicyOpted="";
	PolicyOpted = (String)request.getSession().getAttribute("PolicyOpted");
	pageContext.setAttribute("nationalities", Cache.getCacheObject("nationalities"));
	pageContext.setAttribute("maritalStatuses", Cache.getCacheObject("maritalStatuses"));
	pageContext.setAttribute("listRelationshipCode",Cache.getCacheObject("hrRelationship"));
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
	  String photoYN = (String)request.getSession().getAttribute("photoYN"); 
%>

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="57%">Enrollment Details - <bean:write name="frmMemberDetails" property="caption"/></td>
	<td width="43%" align="right" class="webBoard">&nbsp;&nbsp;</td>
  </tr>
</table>
<div class="contentArea" id="contentArea">
<div>
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
<html:form action="/OnlinePolicyDetailsAction.do"  method="post" enctype="multipart/form-data">

	<div class="modify_text_class1">
       
       <font size="2" color="red" ><b>Note:</b> Please note that some of the fields below are non editable. Please contact Al Koot Customer Care if you want to modify the information in non editable fields.</font>
       
       </div>
	<ttk:OnlinePolicyHistory/>
	
	<fieldset>
		<legend>Member Information</legend>
		  	<table align="center" class="formContainerWeblogin" border="0" cellspacing="0" cellpadding="0">
		  	  <tr style="padding-top:10px;">
		  	  		<td align="left" nowrap>Employee No.:<span class="mandatorySymbol">*</span><br>
				          <html:text property="employeeNbr" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="150" readonly="true"  style="background-color: #EEEEEE;" />
					</td>
					 <td align="left" nowrap>Principal Name:<span class="mandatorySymbol">*</span><br>
				          <html:text property="insuredName" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="250" readonly="true" style="background-color: #EEEEEE;"/>
					   </td>
					   <%-- <td align="left" nowrap>VIP:<!-- <span class="mandatorySymbol">*</span> --><br>
								<html:select property="memberTypeID" name="frmMemberDetails" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" disabled="true">
  	 		  						<html:option value="N">NO</html:option>
		    		 				 <html:option value="Y">YES</html:option>
								</html:select>
					</td> --%>
		  	  </tr>
		  	  
		      <tr style="padding-top:10px;">
				      	<td align="left" nowrap>Member Name:<logic:match name="frmMemberDetails" property="nameMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match><br>
				      	<logic:notEqual name="frmMemberDetails" property="relationID" value="NSF">
				          <html:text property="name" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="250" disabled="true" onkeyup="ConvertToUpperCase(event.srcElement);" />
				        </logic:notEqual>
				        <logic:equal name="frmMemberDetails" property="relationID" value="NSF">
						  <html:text property="name" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin" onkeyup="ConvertToUpperCase(event.srcElement);" maxlength="250" disabled="true"/>
			    		  <input type="hidden" name="name" value="<bean:write name="frmMemberDetails" property="name"/>">
					    </logic:equal>
					   </td>


					<td align="left" nowrap>Family Name:<br>
				          <html:text property="familyName" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="250"disabled="true"style="background-color: #EEEEEE;" />
					   </td>

			    <td align="left" nowrap>Relationship:<logic:match name="frmMemberDetails" property="relationMandatoryYN" value="Y"><span class="mandatorySymbol">*</span></logic:match>&nbsp;&nbsp;&nbsp;<br>
			        
			      <%--   <logic:notEqual property="relationTypeID" name="frmMemberDetails" value="NSF"> --%>	
			        	<html:select property="relationTypeID" name="frmMemberDetails" styleId="relation" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" onchange="onRelationshipChange();" disabled="true" style="background-color: rgb(238, 238, 238);">
				        	<html:option value="">Select from list</html:option>
				            <html:optionsCollection name="frmMemberDetails" property="alRelationShip"  label="cacheDesc" value="cacheId" />
			            </html:select>
			        </td>

		     

		         	<td align="left" nowrap id="type" >Gender:<span class="mandatorySymbol">*</span><br>
				         <logic:equal name="frmMemberDetails" property="genderYN" value="OTH">
				           <html:select property="genderTypeID" name="frmMemberDetails" styleId="genderId" styleClass="selectBoxWeblogin selectBoxWeblogin" disabled="true" style="background-color: rgb(238, 238, 238);">
				           		<html:option value="">Select from list</html:option>
				           		<html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
				           </html:select>
				            &nbsp;&nbsp;&nbsp;&nbsp;
				       	  </logic:equal>
				       	  <logic:notEqual name="frmMemberDetails" property="genderYN" value="OTH">
					       	  <html:select property="genderTypeID"  name="frmMemberDetails" styleClass="selectBoxWeblogin selectBoxDisabledWeblogin" disabled="true">
								  <html:option value="">Select from list</html:option>
							      <html:optionsCollection name="gender" label="cacheDesc" value="cacheId" />
							  </html:select>
							  <input type="hidden" name="genderTypeID" value="<bean:write name="frmMemberDetails" property="genderYN"/>">
				       	  </logic:notEqual>
			       	 </td>
	      	 <td width="15%" nowrap colspan="2"></td>
		     </tr>

		      <tr>
		   	  <td align="left" nowrap>Date of Birth:<span class="mandatorySymbol">*</span><br>
								<html:text property="dateOfBirth" name="frmMemberDetails" styleId="Dob" styleClass="textBox textDate" onfocus="convertToHDate('HDob')" onblur="javascript:convertToHDate('HDob');onDateofBirth();" maxlength="10" readonly="true" disabled="true"/>
    							<a name="CalendarObjectdobDate" id="CalendarObjectdobDate" href="#" onClick="javascript:show_calendar('CalendarObjectdobDate','frmMemberDetails.dateOfBirth',document.frmMemberDetails.dateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="dobDate" width="24" height="17" border="0" align="absmiddle"></a>
    							<html:text property="hdateOfBirth" name="frmMemberDetails" styleId="HDob" styleClass="textBox textDate" maxlength="10" onfocus="convertToGDate('Dob')" onblur="javascript:convertToGDate('Dob');onDateofBirth();" readonly="true" disabled="true"/>					
  							  	<a name="HCalendarObjectdobDate" ID="HDob" HREF="#" onClick="javascript:show_Hcalendar('HDob','forms[1].hdateOfBirth',document.forms[1].hdateOfBirth.value,'',event,148,178);return false;" onMouseOver="window.status='Hijri-Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/HCalendarIcon.gif" alt="Hijri-Calendar"  width="24" height="17" border="0" align="absmiddle"></a>   
    			</td>
    			 <td align="left" nowrap>Age (Yrs):<span class="mandatorySymbol">*</span><br>
								<logic:notEmpty name="frmMemberDetails" property="dateOfBirth">
	 								   <html:text styleId="ageid" name="frmMemberDetails" property="age" styleClass="textBox textBoxTiny" maxlength="3" readonly="true" disabled="true"/>
   								</logic:notEmpty>
   								<logic:empty name="frmMemberDetails" property="dateOfBirth">
									    <html:text styleId="ageid" name="frmMemberDetails" property="age" styleClass="textBox textBoxTiny" maxlength="3"/>
   								</logic:empty>
   				</td>				   		     
		     
		   		  <td align="left" nowrap id="type" >Nationality:<span class="mandatorySymbol">*</span><br>
				           <html:select property="nationality" name="frmMemberDetails" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" readonly="true" disabled="true" style="background-color: rgb(238, 238, 238);">
				         	  	<html:option value="">Select from list</html:option>
				           		  <html:optionsCollection name="nationalities" label="cacheDesc" value="cacheId" /> 
				           </html:select>			       			
			       	 </td>
		     
 					<td align="left" nowrap id="type" >Marital Status:<span class="mandatorySymbol">*</span><br>
				           <html:select property="maritalStatus" name="frmMemberDetails" styleClass="selectBoxWeblogin selectBoxMediumWeblogin" readonly="true" disabled="true" style="background-color: rgb(238, 238, 238);">
				           		<html:option value="">Select from list</html:option>
		     					 <html:optionsCollection name="maritalStatuses" label="cacheDesc" value="cacheId" /> 
				           </html:select>			       			
			       	 </td>
		       <td colspan="2" align="center" nowrap >&nbsp;</td>
		        </tr>
		        <tr>
		        	<td align="left" nowrap>Qatar ID Number:<span class="mandatorySymbol">*</span><br>
				          <html:text property="emirateId" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="11"/>
					   </td>
		        <td align="left" nowrap>Passport Number:<span class="mandatorySymbol">*</span><br>
				          <html:text property="passportNumber" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin " maxlength="20" />
					   </td>
		        <td align="left">Upload Photo :</td>
							<td>
								<html:file property="file" name="frmMemberDetails" styleId="file"/>
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
		        <td align="left" nowrap>Email id:<!-- <span class="mandatorySymbol">*</span> --><br>
		        <html:text  property="emailId" name="frmMemberDetails" styleClass="textBoxWeblogin textBoxMediumWeblogin"  maxlength="250" disabled="true" readonly="true" style="background-color: #EEEEEE;" />
				</td>
		        <td align="left" nowrap>Contact Number:<span class="mandatorySymbol">*</span><br>
		        <html:text property="contactNumber" name="frmMemberDetails" styleClass="textBox textBoxMedium" maxlength="25" />
				</td>
				<td align="left" nowrap></td>
				<td colspan="4">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
						</td>
				</tr>
		        <!-- <tr>
						<td colspan="4">
						<font size="2" color="red" ><b>Note: </b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
							<font color="#0C48A2"> <b><li></b>Please ensure you have entered all mandatory information before uploading the Photo.</font>
						</td>
				</tr> -->
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
			        		<html:text property="dateOfInception" name="frmMemberDetails" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" readonly="true" style="background-color: #EEEEEE;" />			
			        		<logic:empty name="frmMemberDetails" property="memberSeqID">        		
		            					<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmMemberDetails.dateOfInception',document.frmMemberDetails.dateOfInception.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>	            		
			        	</logic:empty>
			        	</td>
			     </logic:notEmpty>
			        	<logic:empty name="frmMemberDetails" property="memberSeqID">
			        	<td class="formLabelWeblogin">Date of Inception:<span class="mandatorySymbol">*</span></td>
			        	<td>
			        		<html:text property="dateOfInception" name="frmMemberDetails" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;"/>			        		
		            					<A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmMemberDetails.dateOfInception',document.frmMemberDetails.dateOfInception.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>	            		
			        	</td>
			        	</logic:empty>
			        	<td class="formLabelWeblogin">Date of Exit: </td>
			        	<td>
			        		<html:text property="dateOfExit" name="frmMemberDetails" styleClass="textBoxWeblogin textDate" maxlength="10" style="margin-top:3px;" readonly="true" style="background-color: #EEEEEE;"/>	        			
		            				<!-- <A NAME="CalendarObjectempDate" ID="CalendarObjectempDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectempDate','frmMemberDetails.dateOfExit',document.frmMemberDetails.dateOfExit.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="joinDate" width="24" height="17" border="0" align="absmiddle"></a>		            		 -->
			        	</td>
			      	</tr>
			      	<tr>
			      		<td class="formLabel">Category:</td>
			      	</tr>
			      	<tr>
			      	</tr>
			      	<tr>
			      	<td align="left" nowrap style="display:none;">Remarks:</td>
			      	<td colspan="5" style="display:none;"><html:textarea property="remarks" name="frmMemberDetails" cols="60" rows="2" /></td>
			      	</tr>
    			
	    </table>
	   </fieldset>
	   
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
	
	<!-- S T A R T : Buttons -->
	<div>
	<!-- <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td align="center">
	    	<button type="button" name="Button2" accesskey="c" class="buttons save_class" onClick="onSaveDependent();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;&nbsp;
	    	<button type="button" name="Button2" accesskey="c" class="buttons save_class" onClick="goToHome()"><u>C</u>lose</button>
<button type="button" name="Button2" accesskey="c" class="buttons save_class button_design_class" onClick="onSaveDependent();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;&nbsp;
	    	<button type="button" name="Button2" accesskey="c" class="buttons save_class " onClick="goToHome()"><u>C</u>lose</button> 
	    	<a href="#" onclick="javascript:onSaveDependent();" class="button_design_class"><u>S</u>ave</a>&nbsp;&nbsp;
	    </td>
	     <td align="center">
	    	<button type="button" name="Button2" accesskey="c" class="buttons save_class" onClick="onSaveDependent();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;&nbsp;
	    	<button type="button" name="Button2" accesskey="c" class="buttons save_class" onClick="goToHome()"><u>C</u>lose</button>
<button type="button" name="Button2" accesskey="c" class="buttons save_class button_design_class" onClick="onSaveDependent();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;&nbsp;
	    	<button type="button" name="Button2" accesskey="c" class="buttons save_class " onClick="goToHome()"><u>C</u>lose</button> 
		 <a href="#" onclick="javascript:goToHome();" class="button_design_class"><u>C</u>lose</a>
	    </td>
	  </tr>
	</table> -->
	
	<div class="query_div_class">
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
	    <button type="button" name="Button" accesskey="s" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:onSaveDependent();"><u>S</u>ave</button>&nbsp;&nbsp;&nbsp;&nbsp;
<!-- <a href="#" onclick="javascript:onSaveDependent();" accesskey="s" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'"><u>S</u>ave</a> --> 
	     <button type="button" name="Button" accesskey="c" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'" onClick="javascript:goToHome();"><u>C</u>lose</button>&nbsp;
<!-- <a href="#" onclick="javascript:goToHome();" accesskey="c" class="button_design_class" onMouseout="this.className='button_design_class'" onMouseover="this.className='button_design_class buttonsHover'"><u>C</u>lose</a> -->
	    </td>
	  </tr>
	</table>
	</div>
	</div>
	
	<!-- E N D : Buttons -->

<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
<input type="hidden" name="logintype" value="<%=userSecurityProfile.getLoginType()%>">
</html:form>
</div>
</div>