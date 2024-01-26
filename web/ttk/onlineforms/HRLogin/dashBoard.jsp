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
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList,com.ttk.dto.usermanagement.UserSecurityProfile" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/onlineforms/HRLogin/dashboard.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">


</script>
<style>
.headLabel{
padding: 5px 2px 5px 2px;
text-align: left;
white-space: nowrap;
}
.valueLabel{
padding: 5px 100px 5px 100px;
text-align: center;
white-space: nowrap;
}
.subHeading {
   background-color: #0F75BC;
   border: 0px solid #91C85F;
    padding: 8px 20px 8px 5px; 
    /* background-color: #91C85F; */
    color: white;
    width: 300px;
font-size: 15px;
text-align: center;    
border-top-right-radius:2em;
border-bottom-right-radius:2em;
margin-top: 20px;
margin-left: -3px;
font-family: Verdana, Arial, Helvetica, sans-serif;
   }
   .tableRefresh
   {
   
   border: 1px solid black;
   margin-top: 20PX; 
   height:5PX;
   background-color: #F8F8F8;
   align:center;
   }
   
   .info_text_class{
    color: #0c48a2;
    font-weight: bold;
    font-size: 12px;
}

.main_info_text_class {
    font-size: 14px;
    font-family: Arial, Helvetica, sans-serif;
}

</style>

<%
%>
<html:form action="/OnlineDashBoardAction.do" enctype="multipart/form-data">

<!-- S T A R T : Content/Form Area -->	
	<!-- S T A R T : Page Title -->
<html:errors/>
<logic:notEmpty name="successMsg" scope="request">
				<table align="center" class="successContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="successMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
<!-- S T A R T : Content/Form Area -->

<table cellspacing="0" cellpadding="0" style="margin-left: 10px;margin-top: 10px;">
	
	
	<tr>
	<td valign="top">
	
	<table>
	
	<tr> 
	<td >
	
	<!--<table>
	<tr>
	
	<td><div   class="subHeading">Dashboard Information</div></td>
	 <td><table class="tableRefresh">
	 <tr><td> <a href="#" accesskey="r" onClick="javascript:onDashBoardEnrollmentRefresh()" class="search">
	<img src="/ttk/images/RenewIcon.gif" alt="Search" title="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>R</u>efresh
    </a></td> 
    </tr>
    </table>
    </td>	
	</tr>
	</table>
	 
   
   
	</td>	
	</tr>
	
	<tr>
	<td>
	<table  border="1" cellspacing="0" cellpadding="0" style="margin-top: 15PX">
	                <tr>                
	                    <td  valign="top" class="gridHeader" align="center">POLICY AND ENDORSEMENT DETAILS</td>
	                    <td  valign="top" class="gridHeader" align="center">Count</td>
	               </tr>
	              <tr>
	              
	             <td class="headLabel">Total Active Policy Under Group	</td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalAPG" name="hmEndorsementDetails" scope="session"/></a>	</td>
				  </tr>
				  
				   <tr>
				  <td    class="headLabel">Total Active Member Under Group						
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalAMG" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				    <tr>
				  <td    class="headLabel">Total Claims from Group					
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalCG" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				    <tr>
				  <td    class="headLabel">Total Pre-Approval from Group					
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalPG" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				  <%--  <tr>
				  <td   class="headLabel">Total Endorsement Raised						
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalER" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				    <tr>
				  <td    class="headLabel">Total Endorsement In-Progress						
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalEI" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				    <tr>
				  <td    class="headLabel">Total Endorsement Approved						
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalEA" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				    <tr>
				  <td   class="headLabel">Required More Information(ShortFall)						
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="shortfall" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				    <tr>
				  <td    class="headLabel">Total Endorsement Under Premium Pending						
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalEPP" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>
				    <tr>
				  <td    class="headLabel">Total Endorsement Requests Completed						
	              </td>
	              <td class="valueLabel"><a href="#" onclick="javascript:onEnrollmentReports();"> <bean:write property="totalERC" name="hmEndorsementDetails" scope="session"/></a>	</td>
				   </tr>  --%>
						
	
	</table> -->
	</td>
	
	</tr>
	</table>  <!-- Main Table End -->
	
	
	
	
	</td>
	
	
	<td valign="top">
	
	<table style="margin-top:50PX;">
	
	<tr>
	<td>
	<fieldset style="margin-left:10PX;">
				<legend align="center">SoftCopy Format DownLoad</legend>
	<table style="margin-top:25PX; margin-left: 10PX; margin-bottom: 20PX;">
	<tr><td nowrap style="color: green;">Select Sample File Type:
			<html:select property="fileTypeDownload" styleClass="selectBox selectBoxMedium">
			      <html:option value="">Select From List</html:option>
		          <html:option value="ADD">Addition</html:option>
		          <html:option value="DEL">Deletion</html:option>
		          <html:option value="MOD">Modification</html:option>
		          <html:option value="REN">Renewal</html:option>
            </html:select>
	    </td>
	     </tr>
	 <tr> 
	 <td>   
	<button type="button" name="Button" style="margin-left: 140PX;margin-top:20PX;" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onDownLoadFiles();">DOWNLOAD FILE</button>&nbsp;
	</td>
	
	</tr>
	</table>
	</fieldset>
	
	</td>
	</tr>
	
	<tr>
	<td>
	
	<fieldset style="margin-left:10PX;">
				<legend align="center">SoftCopy Format Upload</legend>
	<table cellspacing="0" cellpadding="0" style="margin-top:25PX; margin-left: 10PX;margin-bottom: 20PX;">
	<tr>   <!-- selectBoxWeblogin selectBoxLargestWeblogin -->
	<td nowrap style="color:green;"> Policy No :  
	<html:select property="listofPolicysequenceId" styleClass="selectBox selectBoxMedium">
	<html:option value="">Select Policy From List</html:option>
	<html:options collection="policyListCollection" property="cacheId" labelProperty="cacheDesc"/>
	</html:select><br/>
	</td>
	</tr>
	<tr> <td> &nbsp; </td></tr>
	<tr>
	<td nowrap style="color: green;">File Type :
			<html:select property="fileTypeUpload" styleClass="selectBox selectBoxMedium">
		           <html:option value="">Select From List</html:option>
		           <html:option value="ADD">Addition</html:option>
		           <html:option value="DEL">Deletion</html:option>
		           <html:option value="MOD">Modification</html:option>
		           <html:option value="REN">Renewal</html:option>
            </html:select>
	    </td>
	    </tr>
	 <tr class="formFields">
	  <td class="formLabel" >Choose The File:<span class="mandatorySymbol">*</span></td>
	  </tr>
	    <tr><td class="formTextLabel" nowrap="nowrap">
	    <html:file property="excelFile"></html:file>
	    <!-- </td>
	  <td>  --> 
	<button type="button" name="Button" accesskey="u" class="buttons"  style="margin-left: 10PX;"  onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUpLoadFiles();">UPLOAD</button>&nbsp;
	</td>
	</tr> 
	<tr>
	      <td colspan="2" align="center"> 
	      	<font color="#04B4AE"> <strong>Please Select only .xls  file to upload.</strong></font>
	      </td>
      </tr>
	</table>
	</fieldset>
	
	
	</td>
	</tr>
	
	</table>
	
   
   
   
    </td>
    
    </tr>
    </table>
    <br/>

<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td><b>Note:</b></td>
		</tr>
			<tr>
				<td>
				<font color="#0C48A2"> <b><li></b>To add or delete several members, download appropriate Excel sheet from SoftCopy Format Download section, fill in all required information, save it locally on your computer and upload it back in SoftCopy Format Upload Section, browse button. Please select appropriate Policy No. and File Type.</font></td>
			</tr>
			
			<tr>
				<td>
				<font color="#0C48A2"> <b><li></b>For singular additions, deletions or modifications, please proceed to Policies tab, select the required policy and New Enrollment button on the bottom right of the screen for New Principle members or the GREEN PLUS button to add or edit dependents.</font></td>
			</tr>
		</table>

    
	
	 
   

<!--E N D : Content/Form Area -->
<!-- E N D : Main Container Table --> 
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<INPUT TYPE="hidden" NAME="leftlink" VALUE="">
<INPUT TYPE="hidden" NAME="sublink" VALUE="">
<INPUT TYPE="hidden" NAME="tab" VALUE="">
<INPUT TYPE="hidden" NAME="seqID" VALUE="">
<INPUT TYPE="hidden" NAME="fileName" VALUE="">
<input type="hidden" name="child" value="">

</html:form> 
