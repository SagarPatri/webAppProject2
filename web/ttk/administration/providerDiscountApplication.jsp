<!-- mm aa -->


<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.security.Cache" %>

<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<%@ page import=" com.ttk.common.TTKCommon"%>



<script type="text/javascript"	src="/ttk/scripts/administration/providerDiscountApplication.js"></script>
<!-- S T A R T : Content/Form Area -->
<html:form action="/ProviderDiscountApplication.do" method="post">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0"	cellpadding="0">
		<tr>
			<td>Provider Discount Configuration - <bean:write name="frmProviderDiscApp" property="caption"/></td>
			<td align="right"></td>
			<td align="right"></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<logic:notEmpty name="updated" scope="request">
		   	<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
			    	<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
			    	<bean:message name="updated" scope="request"/>
			    </td>
			 	</tr>
			</table>
		</logic:notEmpty>
	
	<div class="contentArea" id="contentArea">
	<html:errors />
	 <!-- S T A R T : Success Box -->
	
	
   <fieldset>
  			<legend>Application of Provider Discount</legend>
            	<table align="center" class="formContainer" border="0" cellspacing="0"	cellpadding="0">
		  	        <tr>
		  	                <td class="formLabel" align="left" width="20%">Apply discount at pre-auth or claims level :  </td>      
	        		        <td>
	        		        	<html:radio property="applyDiscount" name="frmProviderDiscApp" styleId="PATCLM"  value="PATCLM" onclick="javascript:onChangeProviderDiscount()"/>
	        		        </td>
	        		</tr>
	        		 <tr>
		  	                <td class="formLabel" align="left" width="20%">Apply discount at finance level :  </td>
	        		         <td>
	        		        	<html:radio property="applyDiscount"  styleId="FIN" value="FIN" onclick="javascript:onChangeProviderDiscount()"/>
	        		        </td>
	        		</tr>
	        		               
		  	  	</table>
		  	  	
		  	  	<table align="center" class="formContainer" border="0" cellspacing="0"	cellpadding="0" id="showHideFin" style="display:none">
		  	  		<tr>
		  	                <td class="formLabel" align="left" width="10%">Benefit Type :  </td>
	        		        <td class="formLabel" width="10%">All</td>
	        		        <td>
	        		        	<html:checkbox name="frmProviderDiscApp" property="all" styleId="all"   onclick="javascript:onCheckAll();"/>
	        		        </td>
	        		</tr>
	        		<tr>
		  	                <td>&nbsp;</td>
	        		        <td class="formLabel"  width="10%">Dental</td>
	        		        <td>
	        		        	<html:checkbox name="frmProviderDiscApp" property="dental" styleId="dental" />
	        		        </td>
	        		</tr>
	        			<tr>
		  	                <td>&nbsp;</td>
	        		        <td class="formLabel"  width="10%">Optical</td>
	        		        <td>
	        		        	<html:checkbox name="frmProviderDiscApp" property="optical" styleId="optical" />
	        		        </td>
	        		</tr>
	        		
	        		<tr>
		  	                <td>&nbsp;</td>
	        		        <td class="formLabel" width="10%">OP Maternity</td>
	        		        <td>
	        		        	<html:checkbox name="frmProviderDiscApp" property="opMaternity"  styleId="opMaternity" />
	        		        </td>
	        		</tr>
	        		<tr>
		  	                <td>&nbsp;</td>
	        		        <td class="formLabel" width="10%">IP Maternity</td>
	        		        <td>
	        		        	<html:checkbox name="frmProviderDiscApp" property="ipMaternity" styleId="ipMaternity" />
	        		        </td>
	        		</tr>
	        		<tr>
		  	                <td>&nbsp;</td>
	        		        <td class="formLabel" width="10%">OP Benefit</td>
	        		        <td>
	        		        	<html:checkbox name="frmProviderDiscApp" property="opBenefit" styleId="opBenefit" />
	        		        </td>
	        		</tr>
	        		<tr>
		  	                <td>&nbsp;</td>
	        		        <td class="formLabel" width="10%">IP Benefit</td>
	        		        <td>
	        		        	<html:checkbox name="frmProviderDiscApp" property="ipBenefit" styleId="ipBenefit" />
	        		        </td>
	        		</tr>
		  	  	</table>
	</fieldset>
	<table style="" align="center">	  
		  	<tr>
				<td width="100%" align="center">
				<br><br>
				<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'"	onClick="javascript:onSave();"><u>S</u>ave</button>
				&nbsp;
				<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
			</td>
		</tr>
	</table>
	</div>
	<!-- E N D : Buttons -->
	<input type="hidden" name="mode">
	<script type="text/javascript">
		onChangeProviderDiscount();
	</script>
</html:form>
<!-- E N D : Content/Form Area -->