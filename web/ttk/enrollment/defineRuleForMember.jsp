
<%@ page import="com.ttk.common.TTKCommon" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/enrollment/defineRuleForMember.js"></script>

<SCRIPT type="text/javascript">
	var JS_Focus_Disabled =true;
</SCRIPT>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/ViewMemberPolicyAction.do" >
	<!-- S T A R T : Page Title -->
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td >List of all Applicable Clauses:</td>
	  </tr>
	</table>
	<!-- E N D : Page Title -->
	
	<div class="contentAreaHScroll" id="contentArea">
	<html:errors/>
	<!-- S T A R T : Success Box -->
	 <%-- <logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
					  <bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
	</logic:notEmpty> --%>
	<logic:notEmpty name="updated" scope="request">
				<table align="center" class="successContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="updated" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
 	<!-- E N D : Success Box -->

	<!-- S T A R T : Form Fields -->
	<table align="right" border="0" cellspacing="0" cellpadding="3">
	    <tr>
	    
	     <td>
	    <a href="#" onClick="javascript:viewPolicyRuleHistory(this)">Policy Rule History</a>
	    </td>
	    
	    <td>
	    	<img src="/ttk/images/ConfigureIcon.gif" width="16" height="16" /> - Some conditions will be defined at the Family or Member level
	        </td>
	    </tr>
    </table>
	<br/>

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td valign="top" >
			<ttk:ClauseDetails flow="ProdPolicyRule"/>
		</td>
	</tr>
	</table>

    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">
		   
				<button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave</button>&nbsp;
				<button type="button" name="Button2" accesskey="f" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReconfigure();">Recon<u>f</u>igure</button>&nbsp;
				<%-- <logic:match name="ActiveSubLink" value="Products">
					<logic:notEmpty name="frmDefineRules" property="prodPolicyRuleSeqID">
						<logic:empty name="frmDefineRules" property="frmChanged">
							<button type="button" name="Button2" accesskey="i" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReviseRule();">Rev<u>i</u>se</button>&nbsp;
						</logic:empty>
					</logic:notEmpty>
		    	</logic:match> --%>
			
			
		    	<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	    </td>
	  </tr>
	</table>
	<br/><br/>
	<table align="right" border="0" cellspacing="0" cellpadding="3">
	    <tr><td>
	    	<img src="/ttk/images/ConfigureIcon.gif" width="16" height="16" /> - Some conditions will be defined at the Family or Member level
	        </td>
	    </tr>
    </table>
	<br/>

	<!-- E N D : Buttons -->
	</div>
	<!-- E N D : Buttons -->
	<INPUT TYPE="hidden" NAME="mode" VALUE="">

	<html:hidden property="selectedroot"/>
	<html:hidden property="selectednode"/>
	<html:hidden property="memberSeqID"/>
	<html:hidden property="prodPolicyRuleSeqID"/>
	
	<%-- <html:hidden property="seqId"/>
	<logic:match name="ActiveSubLink" value="Products">
		<input type="hidden" name="child" value="Define Rule">
	</logic:match>
	<logic:notMatch name="ActiveSubLink" value="Products">
		<input type="hidden" name="child" value="">
	</logic:notMatch> --%>
	
	<SCRIPT type="text/javascript">
	validateMaternity();
</SCRIPT>
	<SCRIPT type="text/javascript">
		hideGeneralExclusionFields();
	</SCRIPT>
	</html:form>
	<!-- E N D : Content/Form Area -->