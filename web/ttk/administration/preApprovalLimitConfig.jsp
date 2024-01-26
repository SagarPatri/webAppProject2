<%
/**
 * Reason        :  
 */
 %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ page import=" com.ttk.common.TTKCommon"%>



<script src="/ttk/scripts/validation.js" type="text/javascript"></script>
<script type="text/javascript"	src="/ttk/scripts/administration/preApprovalLimitConfig.js"></script>
<!-- S T A R T : Content/Form Area -->
<html:form action="/PreApprovalLimitConfiguration.do" method="post">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0"	cellpadding="0">
		<tr>
			<td>Pre-Apprval Limit -<bean:write
				name="frmPreApprovalLimit" property="caption" /></td>
			<td align="right"></td>
			<td align="right"></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	
	
	<div class="contentArea" id="contentArea">
	<html:errors />
	 <!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display: " border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success"
					width="16" height="16" align="absmiddle">&nbsp; 
					<bean:message name="updated" scope="request" /></td>
			</tr>
		</table>
	</logic:notEmpty> <!-- E N D : Success Box --> <!-- S T A R T : Form Fields -->
	
          <fieldset>
                            <legend>Pre-Approval Limit</legend>
                            
		  	                
		  	                <table align="center" class="formContainer" border="0" cellspacing="0"	cellpadding="0">
		  	                <tr>
		  	                
		  	                <td>
		  	                
		  	                <table align="center" class="formContainer" border="0" cellspacing="0"	cellpadding="0">
		  	                <tr>
		  	                <td align="center">VIP Member Limit:</td>
		  	                </tr>
		  	                <tr>
		  	                <td class="formLabel" align="center">Pre-Approval for Claim Above VIP Member Limit: 
	        		         <html:checkbox name="frmPreApprovalLimit" property="VIPpreApprovalLimitYN"  value="Y" styleId="VIPpreApprovalLimitYN" onclick="preApprovalLimitVIP(this,'preAppravalLimtIDVIP');"/>
	        		        </td>
	        		        </tr>
		  	              
		  	                <logic:equal name="frmPreApprovalLimit" property="VIPpreApprovalLimitYN" value="Y" >
	        		         <tr>
	        		         <td id="preAppravalLimtIDVIP" align="center">
	        		         
	        		          Pre-Approval Limit:&nbsp;&nbsp;<html:text property="preApprovalAmountVIP" name="frmPreApprovalLimit" maxlength="7" onkeyup="isNumeric(this);"/>(QAR)
	        		         
	        		          </td>
	        		          </tr>
	        		         </logic:equal>
	        		         <logic:notEqual name="frmPreApprovalLimit" property="VIPpreApprovalLimitYN" value="Y" >
	        		         <tr>
	        		         <td id="preAppravalLimtIDVIP" style="display: none;" align="center">
	        		            Pre-Approval Limit:&nbsp;&nbsp;<html:text property="preApprovalAmountVIP" name="frmPreApprovalLimit" maxlength="7" onkeyup="isNumeric(this);"/>(QAR)
	        		          </td>
	        		          </tr>
	        		          </logic:notEqual>
	        		          <tr>
		  	                <td class="formLabel" align="center">Mandatory Service Activity to be Pre-Approved: 
	        		         <html:checkbox name="frmPreApprovalLimit" property="MandatoryServiceVIP" value="Y" styleId="MandatoryServiceVIP"/>
	        		        </td>
	        		        </tr>
		  	                
		  	                </table>
		  	                </td>
		  	                <td style="border-left:thin groove;">
		  	                
		  	                <table align="center" class="formContainer" border="0" cellspacing="0"	cellpadding="0">
		  	                <tr>
		  	                <td align="center">NON-VIP Member Limit:</td>
		  	                </tr>
		  	                <tr><td class="formLabel" align="center">Pre-Approval for Claim Above NON-VIP Member Limit: 
	        		         <html:checkbox name="frmPreApprovalLimit" property="NONVIPpreApprovalLimitYN"  value="Y" styleId="NONVIPpreApprovalLimitYN" onclick="preApprovalLimitNONVIP(this,'preAppravalLimtIDNONVIP');"/>
	        		        </td></tr>
		  	                <logic:equal name="frmPreApprovalLimit" property="NONVIPpreApprovalLimitYN" value="Y" >
	        		         <tr><td id="preAppravalLimtIDNONVIP" align="center">
	        		         Pre-Approval Limit:&nbsp;&nbsp;<html:text property="preApprovalAmountNONVIP" name="frmPreApprovalLimit" maxlength="7" onkeyup="isNumeric(this);"/>(QAR)
	        		          </td></tr>
	        		         </logic:equal>
	        		         <logic:notEqual name="frmPreApprovalLimit" property="NONVIPpreApprovalLimitYN" value="Y" >
	        		         <tr><td id="preAppravalLimtIDNONVIP" style="display: none;" align="center">
	        		            Pre-Approval Limit:&nbsp;&nbsp;<html:text property="preApprovalAmountNONVIP" name="frmPreApprovalLimit"  maxlength="7" onkeyup="isNumeric(this);"/>(QAR)
	        		          </td></tr>
	        		         </logic:notEqual>
	        		         
	        		         <tr>
		  	                <td class="formLabel" align="center">Mandatory Service Activity to be Pre-Approved: 
	        		         <html:checkbox name="frmPreApprovalLimit" property="MandatoryServiceNONVIP" styleId="MandatoryServiceNONVIP" value="Y"/>
	        		        </td>
	        		        </tr>
	        		        
		  	                
		  	                </table>
		  	                </td>
		  	                
		  	                </tr>
		  	                </table>
		  	                
		  	                </fieldset>
		  	<table align="center">
		  	<tr>
			<td width="100%" align="center">
			<%
	       if(TTKCommon.isAuthorized(request,"Edit"))
	       {
    	%>
			<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'"	onClick="javascript:onSave();"><u>S</u>ave</button>
			&nbsp;
		 <%
	    	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
		%>
			<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
			</td>
			
		</tr>
	</table>
	</div>
	<!-- E N D : Buttons -->
	<input type="hidden" name="mode">
	<input type="hidden" name="child">
	
	
</html:form>
<!-- E N D : Content/Form Area -->