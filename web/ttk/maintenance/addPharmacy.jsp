<%
/** @ (#) addPharmacy.jsp.jsp 30th Oct 2017
 * Project     	 : TTK Healthcare Services
 * File        	 : addPharmacy.jsp.jsp
 * Author      	 : Kishor kumar S H
 * Company     	 : Vidal Health TPA
 * Date Created  : 30th Oct 2017
 *
 * @author 		 :
 * Modified by   : 
 * Modified date : 
 * Reason        : New screen is created in Finance Maintenance link and this screen is shifted to new sublink.
 *
 */
 %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<script language="javascript" src="/ttk/scripts/maintenance/addPharmacy.js"></script>
<!-- S T A R T : Content/Form Area -->
<%
pageContext.setAttribute("gender", Cache.getCacheObject("gender"));
%>
<html:form action="/MaintainPharmacyAddAction.do" >
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  	<tr>
    	<td>Add Drug Master</td>
    </tr>	
	</table>
	<!-- E N D : Page Title --> 
	<div class="contentArea" id="contentArea">	
	<html:errors/>
	
	<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			  		<td>
			  			<img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="updated" scope="request"/>
			  		</td>
				</tr>
			</table>
		</logic:notEmpty>
		<!-- E N D : Success Box -->
		
	<!-- S T A R T : Form Fields -->
	<fieldset>
	<legend>Add Drug Master</legend>
	<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
	<tr>
          	<td class="subHeader" align="left">QDC CODE : </td>        
			<td class="textLabelBold" align="left"><bean:write name="frmPharmacyMaintance" property="activityCode"/>  </td>
			<logic:equal value="N" property="isReviewedByVidal" name="frmPharmacyMaintance">
				<logic:equal value="Y" name="phrmRev" scope="request">
					<td> Reviewed </td>
					<td> <html:checkbox property="isReviewedByVidalCheckBox" name="frmPharmacyMaintance"/></td>
				</logic:equal>
			</logic:equal>
			
			<logic:notEqual value="N" property="isReviewedByVidal" name="frmPharmacyMaintance">
				<logic:notEqual value="Y" name="phrmRev" scope="request">
					<td> &nbsp; </td>
					<td> &nbsp; </td>
				</logic:notEqual>
			</logic:notEqual>
			
	</tr>
	<tr>
			<td> &nbsp; </td>
			<td> &nbsp; </td>
	</tr>
      <tr>
          	<td class="formLabel">Trade Name : </td>        
			<td class="formLabel"><html:text property="tradeName" styleClass="textBox textBoxMedium"/>  </td>
			<td class="formLabel">Scientific Name : <span class="mandatorySymbol">*</span> </td>        
			<td class="formLabel"><html:text property="scientificName" styleClass="textBox textBoxMedium"/>  </td>
	</tr><tr>
          	<td class="formLabel">Short Description : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel"><html:text property="shortDesc" styleClass="textBox textBoxMedium"/>  </td>
			<td class="formLabel"> Activity  Description: <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel"><html:text property="activityDesc" styleClass="textBox textBoxMedium"/>  </td>
	</tr><tr>
          	<td class="formLabel">Start Date : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel">
				<html:text property="startDate" styleClass="textBox textBoxMedium"/>
				<a name="CalendarObjectempDate11" id="CalendarObjectempDate11" href="#" onClick="javascript:show_calendar('CalendarObjectempDate11','frmPharmacyMaintance.startDate',document.frmPharmacyMaintance.startDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
			  </td>
			<td class="formLabel">End  Date : </td>        
			<td class="formLabel">
				<html:text property="endDate" styleClass="textBox textBoxMedium"/>  
				<a name="CalendarObjectempDate12" id="CalendarObjectempDate12" href="#" onClick="javascript:show_calendar('CalendarObjectempDate12','frmPharmacyMaintance.endDate',document.frmPharmacyMaintance.endDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
				</td>
	</tr><tr>
          	<td class="formLabel">Added Date : </td>        
			<td class="formLabel">
				<html:text property="addedDate" styleClass="textBox textBoxMedium"/>  
				<a name="CalendarObjectempDate13" id="CalendarObjectempDate13" href="#" onClick="javascript:show_calendar('CalendarObjectempDate13','frmPharmacyMaintance.addedDate',document.frmPharmacyMaintance.addedDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
				<img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="empDate" width="24" height="17" border="0" align="absmiddle">
				</td>
			<td class="formLabel">Scientific Code : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel"><html:text property="scientificCode" styleClass="textBox textBoxMedium"/>  </td>
	</tr><tr>
          	<td class="formLabel">Unit Price : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel"><html:text property="unitPrice" styleClass="textBox textBoxMedium" onkeyup="isNumeric(this)"/>  </td>
			<td class="formLabel">Package Price : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel"><html:text property="packagePrice" styleClass="textBox textBoxMedium" onkeyup="isNumeric(this)"/>  </td>
	</tr><tr>
          	<td class="formLabel">Granular Unit : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel"><html:text property="granularUnit" styleClass="textBox textBoxMedium" onkeyup="isNumeric(this)"/>  </td>
			<td class="formLabel">Gender : <span class="mandatorySymbol">*</span> </td>        
			<td class="formLabel">
				<html:select property="gender" styleClass="selectBox selectBoxSmall">
          			<html:option value="">Select from list</html:option>
            		<html:option value="MALE">Male</html:option>
          			<html:option value="FEMALE">Female</html:option>
          			<html:option value="BOTH">BOTH</html:option>
				</html:select>
	</tr><tr>
          	<td class="formLabel">Exclusion Yes/No: <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel">
				<html:select property ="exclusionYN" styleClass="selectBox selectBoxMedium">
          			<html:option value="">Select from list</html:option>
          			<html:option value="Y">Yes</html:option>
          			<html:option value="N">No</html:option>
   				</html:select>
          				</td>
			<%-- <td class="formLabel">Qatar Exclusion Yes/No : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel"> 
				<html:select property ="qatarExclusionYN" styleClass="selectBox selectBoxMedium">
	          			<html:option value="">Select from list</html:option>
	          			<html:option value="Y">Yes</html:option>
	          			<html:option value="N">No</html:option>
	   				</html:select> 
  				</td> --%>
	</tr><tr>
          	<td class="formLabel">REC Flag : </td>        
			<td class="formLabel"><html:text property="recFlag" styleClass="textBox textBoxMedium"/>  </td>
			<td class="formLabel">Status : <span class="mandatorySymbol">*</span></td>        
			<td class="formLabel">
				<html:select property ="status" styleClass="selectBox selectBoxMedium">
          			<html:option value="">Select from list</html:option>
          			<html:option value="ACTIVE">Active</html:option>
          			<html:option value="INACTIVE">Inactive</html:option>
   				</html:select>
	</tr><tr>
          	<td class="formLabel">Route of Admin : </td>        
			<td class="formLabel"><html:text property="routeOfAdmin" styleClass="textBox textBoxMedium"/>  </td>
			<td class="formLabel">Registered Owner : </td>        
			<td class="formLabel"><html:text property="registeredOwner" styleClass="textBox textBoxMedium"/>  </td>
	</tr>	
	<tr>
          	<td class="formLabel">Ingredinet Strength : </td>        
			<td class="formLabel"><html:text property="ingStrength" styleClass="textBox textBoxMedium"/>  </td>
			<td class="formLabel">Dosage from Package : </td>        
			<td class="formLabel"><html:text property="doasage" styleClass="textBox textBoxMedium"/>  </td>
	</tr>	
	<tr>
          	<td class="formLabel">QDC ID : </td>        
			<td class="formLabel"><html:text property="ddcId" styleClass="textBox textBoxMedium"/>  </td>
			<td class="formLabel">Qatar Code : </td>        
			<td class="formLabel"><html:text property="qatarCode" styleClass="textBox textBoxMedium"/>  </td>
	</tr>	
     </table>
	</fieldset>
	<!-- E N D : Form Fields -->  
	
	<!-- S T A R T : Buttons -->
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  			<tr>
    			<td width="100%" align="center">
    			
    			<logic:notEqual value="Y" name="phrmRev" scope="request">
    			<%
				if(TTKCommon.isAuthorized(request,"Edit"))
				{
				%>
				<logic:equal value="N" property="isReviewedByVidal" name="frmPharmacyMaintance">
		    		<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserSubmit();"><u>S</u>ave  </button>&nbsp;
		    	</logic:equal>	
				<%
	    		}
 				%>
					<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"> <u>C</u>lose</button>
				</logic:notEqual>	
					
					
				<logic:equal value="Y" name="phrmRev" scope="request">
				<%
				if(TTKCommon.isAuthorized(request,"Edit"))
				{
				%>
				<logic:equal value="N" property="isReviewedByVidal" name="frmPharmacyMaintance">
		    		<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReviewSubmit();"><u>S</u>ave</button>&nbsp;
		    	</logic:equal>	
				<%
	    		}
 				%>
					<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCloseReview();"> <u>C</u>lose</button>
				</logic:equal>
				
 				</td>
  			</tr>  		
		</table>
		<!-- E N D : Buttons -->  
	</div>
	 <input type=hidden name="mode"/>
	<input type="hidden" name="phrmRev" value="<%=request.getAttribute("phrmRev") %>"/>
 </html:form>
    
	<!-- E N D : Content/Form Area -->