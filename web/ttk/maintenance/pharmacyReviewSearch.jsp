<%
/** @ (#) pharmacyMasterSearch.jsp.jsp 30th Oct 2017
 * Project     	 : TTK Healthcare Services
 * File        	 : pharmacyMasterSearch.jsp.jsp
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
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>

<script language="javascript" src="/ttk/scripts/maintenance/pharmacyReviewSearch.js"></script>
<!-- S T A R T : Content/Form Area -->
<html:form action="/MaintainPharmacyReviewSearchAction.do" >
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
  	<tr>
    	<td>Review Pharmacy Master </td>
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
	<legend>Review Pharmacy  Master</legend>
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
		<tr class="searchContainerWithTab">
		    <td nowrap>QDC Code:<br>
		    	<html:text property="sDdcCode" name="frmPharmacyMaintanceSearch"  styleClass="textBox textBoxLarge" maxlength="60"/>
		    </td>
		     <td nowrap>Short Description.:<br>
            	<html:text property="sShortDesc" name="frmPharmacyMaintanceSearch"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
        	 <td nowrap>Drug Full Description:<br>
               	<html:text property="sFullDesc" name="frmPharmacyMaintanceSearch"  styleClass="textBox textBoxLarge" maxlength="60"/>
            </td>
             <td nowrap>Reviewed :<br>
               	<html:select property="sReviewed" styleClass="selectBox selectBoxSmall">
          			<html:option value="N">No</html:option>
          			<html:option value="Y">Yes</html:option>
				</html:select>
            </td>
        	</tr>
        	<tr>
		    <td nowrap> Gender:<br>
				<html:select property="sGender" styleClass="selectBox selectBoxSmall">
          			<html:option value="">Select from list</html:option>
            		<html:option value="MALE">Male</html:option>
          			<html:option value="FEMALE">Female</html:option>
          			<html:option value="BOTH">BOTH</html:option>
				</html:select>		    </td>
		    <td nowrap>Review Yes/No:<br>
				<html:select property ="sQatarExcYN" styleClass="selectBox selectBoxMedium">
          			<html:option value="">Select from list</html:option>
          			<html:option value="Y">Yes</html:option>
          			<html:option value="N">No</html:option>
   				</html:select>		    </td>
        	<td nowrap>Status:<br>
	            <html:select property ="sStatus" styleClass="selectBox selectBoxMedium">
          			<html:option value="">Select from list</html:option>
          			<html:option value="Active">Active</html:option>
          			<html:option value="Inactive">Inactive</html:option>
   				</html:select>
          	</td> 
    	</tr>
    	<tr>
    	<td align="right" nowrap colspan="3">
	        	<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
        	</td>
		</tr>
		</table></fieldset>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     	<tr>
     		<td width="27%"></td>
     		<td width="73%" align="right">
     		<%
            	if(TTKCommon.isAuthorized(request,"Add"))
		    	{
	    	%>
	            <button type="button" name="Button2" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="addPharmacy()"><u>A</u>dd</button>&nbsp;
	            
	            <%
	        	}
	    	%>
     		</td>
     	</tr>
     	<ttk:PageLinks name="tableData"/>
      	<tr>
        	<td height="4" colspan="2"></td>
      	</tr>
	</table>
</div>	
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
	<!-- E N D : Content/Form Area -->