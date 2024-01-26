<%
/**
 * @ (#) assignto.jsp 4th May 2006
 * Project      : TTK HealthCare Services
 * File         : assignto.jsp
 * Author       : Krupa J
 * Company      : Span Systems Corporation
 * Date Created : 4th May 2006
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper,com.ttk.dto.preauth.UserAssignVO"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/preauth/assignto.js"></script>
<script LANGUAGE="javascript" src="/ttk/scripts/preauth/preauthgeneral.js"> </script>
<script LANGUAGE="javascript" src="/ttk/scripts/claims/claimgeneral.js"> </script>

<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>
<%
	boolean viewmode=true;
	pageContext.setAttribute("officeInfo",Cache.getCacheObject("officeInfo"));
	
	//pageContext.setAttribute("assignUsers",Cache.getCacheObject("assignUsers"));
	ArrayList alUserList=(ArrayList)request.getSession().getAttribute("alUserList");
	String strLink=TTKCommon.getActiveLink(request);
	String strSubLink=TTKCommon.getActiveSubLink(request);
	String strTab = TTKCommon.getActiveTab(request);
	pageContext.setAttribute("strLink",strLink);
	pageContext.setAttribute("strSubLink",strSubLink);
	pageContext.setAttribute("strTab",strTab);
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	pageContext.setAttribute("viewmode",new Boolean(viewmode));
%>




<script LANGUAGE="javascript">
function onCancel()
{
	//if(document.getElementById("leftlink").value == "Pre-Authorization" && document.getElementById("tab").value == "System Preauth Approval"){
     if(document.forms[1].leftlink.value=="Pre-Authorization" &&document.forms[1].tab.value=="System Preauth Approval"){

    	   var preAuthSeqID = document.forms[1].preAuthSeqID.value;
			//var preAuthSeqID = document.getElementById("preAuthSeqID").value;
		viewPreAuthDetails(preAuthSeqID);
		
	}
	//else if(document.getElementById("leftlink").value == "Claims" && document.getElementById("tab").value == "General"){

		else if(document.forms[1].leftlink.value=="Claims" &&document.forms[1].tab.value=="General"){
		var claimSeqID = document.forms[1].claimSeqID.value;
		//var claimSeqID = document.getElementById("claimSeqID").value;
		viewClaimDetail(claimSeqID);
		
	}
	
	
	else{
	
	
	if(!TrackChanges()) return false;
 	document.forms[1].mode.value="doClose";
    document.forms[1].action="/AssignToAction.do";
	document.forms[1].submit();
	}
}//end of onCancel
</script>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/AssignToAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="<%=TTKCommon.checkNull(PreAuthWebBoardHelper.getShowBandYN(request)).equals("Y") ? "pageTitleHilite" :"pageTitle" %>" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="57%">Assign To</td>
	<td width="43%" align="right" class="webBoard">&nbsp;</td>
    </tr>
</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
	<html:errors/>
	<!-- S T A R T : Success Box -->
	 <logic:notEmpty name="updated" scope="request">
	  <table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td><img src="/ttk/images/SuccessIcon.gif" title="Success" width="16" height="16" align="absmiddle">&nbsp;
	         <bean:message name="updated" scope="request"/>
	     </td>
	   </tr>
	  </table>
	 </logic:notEmpty>
 	<!-- E N D : Success Box -->


    <!-- S T A R T : Form Fields -->
 
 <% 
 if((TTKCommon.isAuthorized(request, "Assign") || TTKCommon.isAuthorized(request, "AssignAll")) && ("Search".equalsIgnoreCase(strTab))){	%> 
 
 <fieldset>
<legend>General</legend>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>

    <logic:match name="strLink"  value="Pre-Authorization">
    	<td valign="top" class="formLabel" id="strLinkIDForPreAuth">Pre-Approval Selected:</td>
    	 <logic:empty name="frmAssign" property="preAuthSeqID">
    		<td valign="top" class="textLabelBold">
    		 <html:text property="multiple" name="frmAssign"  styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" value="Multiple"/>
    	 </td>
 		</logic:empty>
 		 <logic:notEmpty name="frmAssign" property="preAuthSeqID">
    		 <td valign="top" class="textLabelBold"><bean:write name="frmAssign" property="selectedPreAuthNos"/></td>
 		</logic:notEmpty>
	</logic:match>
  <logic:match name="strLink" value="Claims">
  	<td valign="top" class="formLabel">Claim Selected:</td>
  	<logic:empty name="frmAssign" property="claimSeqID">
    		<td valign="top" class="textLabelBold">
    		 <html:text property="multiple" name="frmAssign"  styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" value="Multiple"/>
    	 </td>
 		</logic:empty>
 		 <logic:notEmpty name="frmAssign" property="claimSeqID">
    		 <td valign="top" class="textLabelBold"><bean:write name="frmAssign" property="selectedClaimNos"/></td>
 		</logic:notEmpty>
  </logic:match>
  <logic:match name="strLink" value="Coding">
  	<logic:match name="strSubLink" value="PreAuth">
  		<td valign="top" class="formLabel">Pre-Approval Selected:</td>
  	</logic:match>
  	<logic:match name="strSubLink" value="Claims">
  		<td valign="top" class="formLabel">Claim Selected:</td>
  	</logic:match>	
 </logic:match>

 <%--  <td valign="top" class="textLabelBold"><bean:write name="frmAssign" property="selectedPreAuthNos"/></td> --%>
  </tr>
  <tr>
    <td nowrap>Al Koot Branch:<span class="mandatorySymbol">*</span><br></td>
    <td><html:select property="officeSeqID" styleClass="selectBox selectBoxMedium" onchange="doSelectUsers()" disabled="<%= viewmode %>">
    	<html:option value="">Select from list</html:option>
    	<html:options collection="officeInfo" property="cacheId" labelProperty="cacheDesc"/>
    </html:select>
	</td>
  </tr>
  <tr>
    <td width="18%" class="formLabel">Assigned To:<span class="mandatorySymbol">*</span></td>
    <td><html:select property="doctor" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
    	<!--<html:option value="">Select from list</html:option>-->
       <html:options collection="alUserList" property="cacheId" labelProperty="cacheDesc"/>
    </html:select> </td>
    </tr>
  <tr>
    <td class="formLabel">Remarks:</td>
    <td class="formLabel">
    <html:textarea  property="remarks" styleClass="textBox textAreaLong" disabled="<%= viewmode %>"/></td>
    </tr>
</table>
</fieldset>

<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" align="center">
    <logic:match name="viewmode" value="false">
     <logic:empty name="frmAssign" property="singleormultiple">
  		  <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave</button>&nbsp;
      </logic:empty>
      <logic:notEmpty name="frmAssign" property="singleormultiple">
      		<button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doMultipleSave();"><u>A</u>ssign Multiple</button>&nbsp;
 		</logic:notEmpty>
 	<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>&nbsp;
 	</logic:match>
    <button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCancel();"><u>C</u>lose</button>
  </tr>
</table>
<% }
  %>
    
<% if((TTKCommon.isAuthorized(request, "Assign") || TTKCommon.isAuthorized(request, "AssignAll")) && ("General".equalsIgnoreCase(strTab) || "System Preauth Approval".equalsIgnoreCase(strTab))){ %>

<fieldset>
<legend>General</legend>
<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
     <logic:match name="strLink"  value="Pre-Authorization">
    	<td valign="top" class="formLabel" id="strLinkIDForPreAuth">Pre-Approval Selected:</td>
    	 <logic:empty name="frmAssign" property="preAuthSeqID">
    		<td valign="top" class="textLabelBold">
    		 <html:text property="multiple" name="frmAssign"  styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" value="Multiple"/>
    	 </td>
 		</logic:empty>
 		 <logic:notEmpty name="frmAssign" property="preAuthSeqID">
    		 <td valign="top" class="textLabelBold"><bean:write name="frmAssign" property="selectedPreAuthNos"/></td>
 		</logic:notEmpty>
	</logic:match>
  <logic:match name="strLink" value="Claims">
  	<td valign="top" class="formLabel">Claim Selected:</td>
  	<logic:empty name="frmAssign" property="claimSeqID">
    		<td valign="top" class="textLabelBold">
    		 <html:text property="multiple" name="frmAssign"  styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" value="Multiple"/>
    	 </td>
 		</logic:empty>
 		 <logic:notEmpty name="frmAssign" property="claimSeqID">
    		 <td valign="top" class="textLabelBold"><bean:write name="frmAssign" property="selectedClaimNos"/></td>
 		</logic:notEmpty>
  </logic:match>
  <logic:match name="strLink" value="Coding">
  	<logic:match name="strSubLink" value="PreAuth">
  		<td valign="top" class="formLabel">Pre-Approval Selected:</td>
  	</logic:match>
  	<logic:match name="strSubLink" value="Claims">
  		<td valign="top" class="formLabel">Claim Selected:</td>
  	</logic:match>	
 </logic:match>

 <%--  <td valign="top" class="textLabelBold"><bean:write name="frmAssign" property="selectedPreAuthNos"/></td> --%>
  </tr>
  <tr>
    <td nowrap>Al Koot Branch:<span class="mandatorySymbol">*</span><br></td>
    <td><html:select property="officeSeqID" styleClass="selectBox selectBoxMedium" onchange="doSelectUsers()" disabled="<%= viewmode %>">
    	<html:option value="">Select from list</html:option>
    	<html:options collection="officeInfo" property="cacheId" labelProperty="cacheDesc"/>
    </html:select>
	</td>
  </tr>
  <tr>
    <td width="18%" class="formLabel">Assigned To:<span class="mandatorySymbol">*</span></td>
    <td><html:select property="doctor" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
    	<!--<html:option value="">Select from list</html:option>-->
       <%-- <html:options collection="alUserList" property="cacheId" labelProperty="cacheDesc"/> --%>
       
       <%
      ArrayList<UserAssignVO> userList =(ArrayList<UserAssignVO>) request.getSession().getAttribute("userList");
       if(userList !=null){
    	   
    	   for(UserAssignVO usevo:userList){
    		   
    		   if(usevo.getUserAssignflag().equals("1")){
    			 %>
    			 <html:option style="color:green;" value="<%=usevo.getContactSeqId()%>"><%=usevo.getContactName()%></html:option>
    			 <%   
    		   }else{
    			   %>
    			 <html:option style="color:red;" value="<%=usevo.getContactSeqId()%>"><%=usevo.getContactName()%></html:option>
    			   <%
    		   }
    	   }
       }
       %>
    </html:select> </td>
    </tr>
  <tr>
    <td class="formLabel">Remarks:<span class="mandatorySymbol">*</span></td>
    <td class="formLabel">
 <%--    <html:textarea  property="remarks" styleClass="textBox textAreaLong" disabled="<%= viewmode %>"/> --%>
    <html:select property="remarks" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
    	<html:option value="System Error Unresolved">System Error Unresolved</html:option>
    	<html:option value="Further Medical Clarification">Further Medical Clarification</html:option>
    	<html:option value="Required">Required</html:option>
    	<html:option value="Above Approval Limit">Above Approval Limit</html:option>
    	<html:option value="Case Assigned Post Shift Timings">Case Assigned Post Shift Timings</html:option>  	
    </html:select>
    
    </td>
    </tr>
</table>
</fieldset>

<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" align="center">
    <logic:match name="viewmode" value="false">
     <logic:empty name="frmAssign" property="singleormultiple">
  		  <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave</button>&nbsp;
      </logic:empty>
      <logic:notEmpty name="frmAssign" property="singleormultiple">
      		<button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doMultipleSave();"><u>A</u>ssign Multiple</button>&nbsp;
 		</logic:notEmpty>
 	<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>&nbsp;
 	</logic:match>
    <button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onCancel();"><u>C</u>lose</button>
  </tr>
</table>
<%} %>	

	<!-- E N D : Form Fields -->
    <!-- S T A R T : Buttons -->

	<!-- E N D : Buttons -->
	</div>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<logic:notEmpty name="frmAssign" property="frmChanged">
 		<script> ClientReset=false;TC_PageDataChanged=true;</script>
	</logic:notEmpty>
	<html:hidden property="claimSeqID"/>
	<%-- <% if(request.getSession().getAttribute("clmSeqId") != null) {
	%>
  <html:hidden property="claimSeqID" styleId="claimSeqID" value="<%=TTKCommon.checkNull(Long.parseLong(String.valueOf(request.getSession().getAttribute("clmSeqId")))) %>"/>
	<%}else {%>
	<html:hidden property="claimSeqID"/>
	<%} %> --%>
	
 <%-- <%	if(request.getSession().getAttribute("preAuthSeqID") !=null) { %>
	<html:hidden property="preAuthSeqID" styleId="preAuthSeqID" value="<%= TTKCommon.checkNull(Long.parseLong(String.valueOf(request.getSession().getAttribute("preAuthSeqID"))))%>"/>
 <%}else{%>
 <html:hidden property="preAuthSeqID"/>
	<%} %> --%>
	<html:hidden property="preAuthSeqID"/>
	<html:hidden property="policySeqID"/>
	<html:hidden property="selectedPreAuthNos"/>
	<html:hidden property="assignUserSeqID"/>
	<html:hidden property="selectedPreAuthSeqId"/>
	<html:hidden property="selectedClaimsSeqId"/>
	<html:hidden property="selectedClaimNos"/>
	<html:hidden property="singleormultiple"/>
	<input type="hidden" name="leftlink" value="<%=strLink%>">
		<input type="hidden" name="sublink" value="<%=strSubLink%>">
		<input type="hidden" name="tab" value="<%=strTab%>">
	</html:form>
	<!-- E N D : Content/Form Area -->