<%
/** @ (#) validatePbmEligibility.jsp 
 * Project     : ttkproject
 * File        : validatePbmEligibility.jsp
 * Author      : Deepthi Meesala
 * Company     : RCS Technologies
 * Date Created: 16th July 2019
 *
 *
 */
 %>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7"/>
</head>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import=" com.ttk.common.TTKCommon,org.apache.struts.action.DynaActionForm" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript"  src="/ttk/scripts/onlineforms/PBMPharmacyLogin/pbmCheckEligibitily.js"></script>
<html:form action="/OnlineCashlessHospActionNew.do" >
<%
DynaActionForm frmPbmPreauthGeneral=(DynaActionForm)request.getSession().getAttribute("frmPbmPreauthGeneral");
String blocked	=	(String)request.getAttribute("blocked");
%>
<h4 class="sub_heading" >ELIGIBILITY CHECK</h4>
<%if("true".equals(request.getSession().getAttribute("memberAuthenticated"))){ %>
<div style="text-align:center;"><strong><b>* Member Validated with Finger Print</b></strong></div>
<%} %>

	<!-- S T A R T : Page Title -->
	<html:errors/>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Form Fields -->
		<div class="contentArea" id="contentArea">
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
		
		<%-- <logic:equal name="eligibility" value="NO" > --%>
		<logic:notEqual  name="eligibility" value="YES">
		
		
	    <p> 
		
		 <logic:equal name="eligibility" value="NO" >
		 <h3> <label  style="color: red;"> Patient Eligible for availing <bean:write name="benefitText" scope="session" /> treatment :  
			<strong><bean:write name="frmPbmPreauthGeneral" property="eligibility"/> </strong></label></h3>
			
			 <br>
			 <h3><label  style="color: red;"> VIP: <bean:write name="vip" scope="session" /></label></h3><br>
		
		
		<fieldset>
		<legend style="font-size: 12px;">Reason for Rejection</legend>
		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
		    	<tr>
			        <td width="25%" class="formLabel" ><strong>Reason</strong></td>
			        <td colspan="3" align="left"> <bean:write name="frmPbmPreauthGeneral" property="reasonForRejection"/>
		      	
		      	</tr>
		</table>        
		</fieldset>
<br>

		<fieldset>
		<legend style="font-size: 12px;">Patient Details</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		    	<tr>
			        <td width="15%" class="formLabel"><strong>Patient Name</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberName"/> </td>
		      	</tr>
		      	<tr>
			        <td width="15%" class="formLabel" ><strong>Al Koot ID </strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="enrollId"/> </td>
			         <td width="15%" class="formLabel"><strong>Qatar ID</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="emirateID"/></td>
		        </tr>
		      	<tr>
		      	<td width="15%" class="formLabel"><strong>Age/DOB</strong></td>
		        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="age"/> Years</td>
		        <td width="15%" class="formLabel"><strong>Gender</strong></td>
		        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="gender"/></td>
		      	<%-- <td width="25%" class="formLabel"><strong>Insurance Company Name</strong></td>
		        <td width="25%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="payer"/></td> --%>
		        </tr>
		</table>       
		</fieldset>
		<br>
		<fieldset>
		<legend style="font-size: 12px;"> Policy Information</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		    	<tr>
			        <td width="15%" class="formLabel"><strong>Policy No</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyNo"/> </td>
		      	</tr>
		      	<tr>
			        <td width="15%" class="formLabel" ><strong>Policy Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyStDt"/> </td>
			         <td width="15%" class="formLabel"><strong>Policy End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyEnDt"/></td>
		        </tr>
		        <tr>
			        <td width="15%" class="formLabel" ><strong>Member Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberStartDate"/> </td>
			         <td width="15%" class="formLabel"><strong>Member End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberEndDate"/></td>
		        </tr>
		</table>        
		</fieldset> <br>
			
		<fieldset>
		<legend style="font-size: 12px;"> Renewed Policy Information</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		        <tr>
			        <td width="15%" class="formLabel" ><strong>Renewed Member Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="renewedMemberStartDate"/> </td>
			         <td width="15%" class="formLabel"><strong>Renewed Member End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="renewedMemberEndDate"/></td>
		        </tr>
		</table>        
		</fieldset><br>
		 </logic:equal>
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		<logic:notEqual  name="eligibility" value="NO">
			
			<h4>Patient Eligible for availing <bean:write name="benefitText" scope="session" /> treatment : <br>
			 <label  style="color: red;"><strong><bean:write name="frmPbmPreauthGeneral" property="eligibility"/>  </strong></label>
			</h4>
		<h3><label  style="color: red;"> VIP: <bean:write name="vip" scope="session" /></label></h3><br>
		
		<p> NOTE: Eligibility is subject to availability of Sum Insured at the time of receipt of Claim.</p>
		
		<fieldset>
		<legend style="font-size: 12px;">Table of Benefits</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		      	<tr>
		      		<!-- <td width="15%" class="formLabel" ><strong>Table of Benefits</strong></td> -->
		      		<logic:equal value="OPTS" name="benifitTypeVal" scope="session">
		     <td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Copay </strong></td><td> : ${tobBenefitsForMemElig[0]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible </strong></td><td> :  ${tobBenefitsForMemElig[2]} </td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Pharmaceutical </strong></td><td> :  ${tobBenefitsForMemElig[12]}  </td></tr><tr>
				      				<td width="15%" class="formLabel"><strong>IP/OP Services </strong></td><td> : ${tobBenefitsForMemElig[11]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks</strong> </td><td> : ${tobBenefitsForMemElig[1]} </td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:equal value="IPT" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Copay</strong> </td><td> :  ${tobBenefitsForMemElig[0]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks </strong></td><td> :  ${tobBenefitsForMemElig[1]} </td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>IP/OP Services</strong> </td><td> :  ${tobBenefitsForMemElig[11]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:match value="MTI" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Maternity Copay</strong> </td><td> : ${tobBenefitsForMemElig[6]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible</strong> </td><td> :  ${tobBenefitsForMemElig[2]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks </strong></td><td> :  ${tobBenefitsForMemElig[1]} </td></tr><tr>
				      				<td width="15%" class="formLabel"><strong>Pharmaceutical</strong> </td><td> :  ${tobBenefitsForMemElig[12]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:match>
		      	
		      	<logic:equal value="OPTC" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Optical Copay </strong></td><td> : ${tobBenefitsForMemElig[8]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible</strong> </td><td> :  ${tobBenefitsForMemElig[2]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks</strong> </td><td> :  ${tobBenefitsForMemElig[1]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:equal value="DNTL" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Dental Copay </strong></td><td> :  ${tobBenefitsForMemElig[10]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible</strong> </strong></td><td> :  ${tobBenefitsForMemElig[2]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks </td><td> :  ${tobBenefitsForMemElig[1]}</td></tr><tr>
				      				<td width="15%" class="formLabel"><strong>Pharmaceutical </strong></td><td> :  ${tobBenefitsForMemElig[12]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:equal value="DAYC" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Copay </strong></td><td> :  ${tobBenefitsForMemElig[0]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Remarks</strong> </td><td> :  ${tobBenefitsForMemElig[1]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>IP/OP Services</strong> </td><td> :  ${tobBenefitsForMemElig[11]} </td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	</tr>
		</table>        
		</fieldset>
		<br>
		
		<fieldset>
		<legend style="font-size: 12px;">Patient Details</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		    	<tr>
			        <td width="15%" class="formLabel"><strong>Patient Name</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberName"/> </td>
		      	</tr>
		      	<tr>
			        <td width="15%" class="formLabel" ><strong>Al Koot ID </strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="enrollId"/> </td>
			         <td width="15%" class="formLabel"><strong>Qatar ID</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="emirateID"/></td>
		        </tr>
		      	<tr>
		      	<td width="15%" class="formLabel"><strong>Age/DOB</strong></td>
		        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="age"/> Years</td>
		        <td width="15%" class="formLabel"><strong>Gender</strong></td>
		        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="gender"/></td>
		      	<%-- <td width="25%" class="formLabel"><strong>Insurance Company Name</strong></td>
		        <td width="25%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="payer"/></td> --%>
		        </tr>
		</table>        
		</fieldset> <br>
		
		<fieldset>
		<legend style="font-size: 12px;"> Policy Information</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		    	<tr>
			        <td width="15%" class="formLabel"><strong>Current Policy No.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyNo"/> </td>
			        <td width="15%" class="formLabel"><strong>Initial Policy No.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="initPolicyNo"/> </td>
		      	</tr>
		      	<tr>
			        <td width="15%" class="formLabel" ><strong>Policy Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyStDt"/> </td>
			         <td width="15%" class="formLabel"><strong>Policy End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyEnDt"/></td>
		        </tr>
		        <tr>
			        <td width="15%" class="formLabel" ><strong>Member Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberStartDate"/> </td>
			         <td width="15%" class="formLabel"><strong>Member End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberEndDate"/></td>
		        </tr>
		        <tr> <td width="100%" class="formLabel" colspan="4">&nbsp;</td> </tr>
		        <tr>
			        <td width="100%" class="formLabel" colspan="4" align="left"><strong>NOTE:</strong> Providers are requested to continue with Preapproval submission if the member is eligible, 
			        irrespective of the policy number on member's card. </td>
		        </tr>
		</table>        
		</fieldset>
		
		<br>
		<fieldset>
		<legend style="font-size: 12px;"> Renewed Policy Information</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		        <tr>
			        <td width="15%" class="formLabel" ><strong>Renewed Member Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="renewedMemberStartDate"/> </td>
			         <td width="15%" class="formLabel"><strong>Renewed Member End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="renewedMemberEndDate"/></td>
		        </tr>
		</table>        
		</fieldset>
		
		
		</logic:notEqual>
		</p>
		
		   
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		<%-- </logic:equal> --%>
		</logic:notEqual>
		<logic:equal name="eligibility" value="YES">
		
		<!-- <button type="button" name="Button" accesskey="p" class="boxWithLine">ELIGIBILITY CHECK</button> -->
		<p> 
			<h4>Patient Eligible for availing <bean:write name="benefitText" scope="session" /> treatment :  
			<strong><bean:write name="frmPbmPreauthGeneral" property="eligibility"/> </strong></h4>
		
		<h4><label  style="color: red;"> VIP: <bean:write name="vip" scope="session" /></label></h4>
		
		</p>
		<p> NOTE: Eligibility is subject to availability of Sum Insured at the time of receipt of Claim.</p>
		
		<fieldset>
		<legend style="font-size: 12px;">Table of Benefits</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		      	<tr>
		      		<!-- <td width="15%" class="formLabel" ><strong>Table of Benefits</strong></td> -->
		      		<logic:equal value="OPTS" name="benifitTypeVal" scope="session">
		     <td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Copay </strong></td><td> : ${tobBenefitsForMemElig[0]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible </strong></td><td> :  ${tobBenefitsForMemElig[2]} </td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Pharmaceutical </strong></td><td> :  ${tobBenefitsForMemElig[12]}  </td></tr><tr>
				      				<td width="15%" class="formLabel"><strong>IP/OP Services </strong></td><td> : ${tobBenefitsForMemElig[11]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks</strong> </td><td> : ${tobBenefitsForMemElig[1]} </td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:equal value="IPT" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Copay</strong> </td><td> :  ${tobBenefitsForMemElig[0]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks </strong></td><td> :  ${tobBenefitsForMemElig[1]} </td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>IP/OP Services</strong> </td><td> :  ${tobBenefitsForMemElig[11]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:match value="MTI" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Maternity Copay</strong> </td><td> : ${tobBenefitsForMemElig[6]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible</strong> </td><td> :  ${tobBenefitsForMemElig[2]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks </strong></td><td> :  ${tobBenefitsForMemElig[1]} </td></tr><tr>
				      				<td width="15%" class="formLabel"><strong>Pharmaceutical</strong> </td><td> :  ${tobBenefitsForMemElig[12]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:match>
		      	
		      	<logic:equal value="OPTC" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Optical Copay </strong></td><td> : ${tobBenefitsForMemElig[8]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible</strong> </td><td> :  ${tobBenefitsForMemElig[2]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks</strong> </td><td> :  ${tobBenefitsForMemElig[1]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:equal value="DNTL" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Dental Copay </strong></td><td> :  ${tobBenefitsForMemElig[10]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Deductible</strong> </strong></td><td> :  ${tobBenefitsForMemElig[2]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>Remarks </td><td> :  ${tobBenefitsForMemElig[1]}</td></tr><tr>
				      				<td width="15%" class="formLabel"><strong>Pharmaceutical </strong></td><td> :  ${tobBenefitsForMemElig[12]}</td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	
		      	<logic:equal value="DAYC" name="benifitTypeVal" scope="session">
			      		<td> 
							<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
				      			<tr><td width="15%" class="formLabel"><strong>Copay </strong></td><td> :  ${tobBenefitsForMemElig[0]} </td>
				      			<td width="15%" class="formLabel"><strong>Psychaitric Treatment </strong></td><td> : ${tobBenefitsForMemElig[13]} </td>
				      			</tr><tr>
				      				<td width="15%" class="formLabel"><strong>Remarks</strong> </td><td> :  ${tobBenefitsForMemElig[1]}</td>
				      			</tr>
				      			<tr><td width="15%" class="formLabel"><strong>IP/OP Services</strong> </td><td> :  ${tobBenefitsForMemElig[11]} </td>
				      			</tr>
			      			</table>
			      		</td>
		      	</logic:equal>
		      	</tr>
		</table>        
		</fieldset>
		<br>
		
		<fieldset>
		<legend style="font-size: 12px;">Patient Details</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		    	<tr>
			        <td width="15%" class="formLabel"><strong>Patient Name</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberName"/> </td>
		      	</tr>
		      	<tr>
			        <td width="15%" class="formLabel" ><strong>Al Koot ID </strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="enrollId"/> </td>
			         <td width="15%" class="formLabel"><strong>Qatar ID</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="emirateID"/></td>
		        </tr>
		      	<tr>
		      	<td width="15%" class="formLabel"><strong>Age/DOB</strong></td>
		        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="age"/> Years</td>
		        <td width="15%" class="formLabel"><strong>Gender</strong></td>
		        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="gender"/></td>
		      	<%-- <td width="25%" class="formLabel"><strong>Insurance Company Name</strong></td>
		        <td width="25%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="payer"/></td> --%>
		        </tr>
		</table>        
		</fieldset> <br>
		
		<fieldset>
		<legend style="font-size: 12px;"> Policy Information</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		    	<tr>
			        <td width="15%" class="formLabel"><strong>Current Policy No.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyNo"/> </td>
			        <td width="15%" class="formLabel"><strong>Initial Policy No.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="initPolicyNo"/> </td>
		      	</tr>
		      	<tr>
			        <td width="15%" class="formLabel" ><strong>Policy Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyStDt"/> </td>
			         <td width="15%" class="formLabel"><strong>Policy End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="policyEnDt"/></td>
		        </tr>
		        <tr>
			        <td width="15%" class="formLabel" ><strong>Member Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberStartDate"/> </td>
			         <td width="15%" class="formLabel"><strong>Member End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="memberEndDate"/></td>
		        </tr>
		        <tr> <td width="100%" class="formLabel" colspan="4">&nbsp;</td> </tr>
		        <tr>
			        <td width="100%" class="formLabel" colspan="4" align="left"><strong>NOTE:</strong> Providers are requested to continue with Preapproval submission if the member is eligible, 
			        irrespective of the policy number on member's card. </td>
		        </tr>
		</table>        
		</fieldset> 
		
		<br>
		<fieldset>
		<legend style="font-size: 12px;"> Renewwd Policy Information</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		        <tr>
			        <td width="15%" class="formLabel" ><strong>Renewed Member Start Date.</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="renewedMemberStartDate"/> </td>
			         <td width="15%" class="formLabel"><strong>Renewed Member End Date</strong></td>
			        <td width="30%">:&nbsp;<bean:write name="frmPbmPreauthGeneral" property="renewedMemberEndDate"/></td>
		        </tr>
		</table>        
		</fieldset>
	<%--<span class="fieldGroupTabHeader">  &nbsp;&nbsp;Eligibility : <bean:write name="benefitText" scope="session" /> - <bean:write name="frmPbmPreauthGeneral" property="eligibility"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    </span> <br>
		    <br> --%>
		</logic:equal>
		
		
		
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
		
	    	<tr>
		        <td width="100%" align="center">
		        <logic:equal name="eligibility" value="YES">
					<button type="button" name="Button" accesskey="p" class="olbtn" onClick="javascript:onPrintForms();"><u>P</u>rint Forms</button>&nbsp;&nbsp;&nbsp;
					<button type="button" name="Button" accesskey="r" class="olbtnSmall" onClick="javascript:onProceed();" title="Proceed for Online Preapproval Submission">P<u>r</u>oceed</button>&nbsp;
					<%-- <logic:notEqual value="IPT" name="benifitTypeVal" scope="session">
						<logic:notEqual value="IMTI" name="benifitTypeVal" scope="session">
							<button type="button" name="Button" accesskey="r" class="olbtnSmall" onClick="javascript:onProceed();" title="Proceed for Online Preapproval Submission">P<u>r</u>oceed</button>&nbsp;
						</logic:notEqual>
					</logic:notEqual> --%>
				</logic:equal>	
				
					<button type="button" name="Button" accesskey="c" class="olbtnSmall" onClick="javascript:onClose();"><u>C</u>lose</button>&nbsp;		       
				 </td>
		      	<td width="100%" align="center">
	      	</tr>
	      	<tr> <td width="100%" align="center">&nbsp; </td></tr><tr>
	    	<td width="100%" align="center">&nbsp; </td></tr><tr>
	    	<td width="100%" align="center">&nbsp; </td></tr><tr>
	    	<td width="100%" align="center">&nbsp; </td></tr>
	    </table>
		<!-- New Fields Ends -->
	 <%-- 
		OTP RELATED CODE
		<!-- S T A R T : Buttons -->
	    <table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	    	
	    	<tr>
	    	<logic:notEqual name="OTP_Done" value="Done">	  
		        <td width="100%" align="center">
		        <logic:empty name="blocked">
<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onConfirmOtp();"><u>C</u>onfirm</button>&nbsp;&nbsp;&nbsp;
</logic:empty>
				 <button type="button" name="Button" accesskey="l" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseOTP();">C<u>l</u>ose</button>&nbsp;		       
				 </td>
	      	</logic:notEqual>
	      	
	      	<logic:equal name="OTP_Done" value="Done">
		      	<td width="100%" align="center">
<!--Removing PreAUth button- as discussed with saravanan  for INTX 
 					<button type="button" name="Button" accesskey="p" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onMemberVitals();"><u>P</u>reauth</button>&nbsp;&nbsp;&nbsp; -->					
<%
if(TTKCommon.getActiveSubLink(request).equals("Pre-Authorization")){
%>
 	&nbsp;&nbsp;&nbsp;<button type="button" name="Button" accesskey="l" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseOTP_PreAuth();">C<u>l</u>ose</button>&nbsp;		       
<%}else if(TTKCommon.getActiveSubLink(request).equals("Eligibility Check")){%>
	&nbsp;&nbsp;&nbsp;<button type="button" name="Button" accesskey="l" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseOTP();">C<u>l</u>ose</button>&nbsp;
	<%} %>					
				</td>
			</logic:equal>
	      	</tr>
	    </table>
	
	    
	    
	    <logic:notEmpty name="wrong">
	     <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<font color=blue><b>Note :</b>  If OTP Number is entered wrong for 3 times it will be blocked. For further processing of blocked OTP, click on Regenerate OTP</font> 
			</td>
     		
     	</tr>
		</table>
		</logic:notEmpty> --%>
	    <!-- E N D : Buttons -->
<input type="hidden" name="enrollId" value="<bean:write name="frmPbmPreauthGeneral" property="enrollId"/>">
<input type="hidden" name="benifitTypeVal" value="<bean:write name="benifitTypeVal" scope="session" />">
	<input type="hidden" name="mode" value="">
	<INPUT TYPE="hidden" NAME="leftlink">
	<INPUT TYPE="hidden" NAME="sublink">
	<INPUT TYPE="hidden" NAME="tab">
	<input type="hidden" name="flag" value="NO"/>
	
<input type="hidden" name="eligibilityFlag" value="<bean:write name="frmPbmPreauthGeneral" property="eligibility"/>">
<input type="hidden" name="benefitType" value="<bean:write name="benefitText" scope="session"/>">
<input type="hidden" name="bufferWarningFlag" value="<bean:write name="frmPbmPreauthGeneral" property="bufferWarning"/>">

<script>
   		window.onload = function() 
   		{
   			var eligibilityFlag = document.forms[1].eligibilityFlag.value;
   			var benefitType = document.forms[1].benefitType.value;
   			var bufferFlag = document.forms[1].bufferWarningFlag.value;
        	 if(eligibilityFlag == "YES")
        	 {
	        	if(bufferFlag == "Y")
	         	alert("Please take the prior approval as there is only minimal balance limit available for "+benefitType+".");
        	 }
    	};
</script>
	</div>
</html:form>