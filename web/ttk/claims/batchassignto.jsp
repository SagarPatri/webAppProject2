<%
/**
 * @ (#) batchassignto.jsp 8th Sep 2020
 * Project      : TTK HealthCare Services
 * File        		: batchassignto.jsp
 * Author      : Vishwabandhu Kumar 
 * Company : Vidal HealthCare
 * Date Created : 8th Sep 2020
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList,com.ttk.common.PreAuthWebBoardHelper"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script LANGUAGE="javascript" src="/ttk/scripts/claims/batchlist.js"> </script>
<script LANGUAGE="javascript">

</script>
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
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	pageContext.setAttribute("viewmode",new Boolean(viewmode));
%>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/ClaimBatchAssignAction.do">
	
<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
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
	     <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
	         <bean:message name="updated" scope="request"/>
	     </td>
	   </tr>
	  </table>
	 </logic:notEmpty>
	 	 <logic:notEmpty name="errorMsg" scope="request">
	  <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error" width="16" height="16" align="absmiddle">&nbsp;
	         <bean:message name="errorMsg" scope="request"/>
	     </td>
	   </tr>
	  </table>
	 </logic:notEmpty>
 <!-- E N D : Success Box -->

 <!-- S T A R T : Form Fields -->
 <%-- <% 
 if((TTKCommon.isAuthorized(request, "Assign") || TTKCommon.isAuthorized(request, "AssignAll")) && ("Search".equalsIgnoreCase(strTab))){	%>  --%>
 <fieldset>
	<legend>General</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
  			<tr>
  				<logic:match name="strLink" value="Claims">
  						<td valign="top" class="formLabel">Batch Selected:</td>
  							<%-- <logic:empty name="frmBatchAssign" property="claimSeqID">
    							<td valign="top" class="textLabelBold">
    		 						<html:text property="multiple" name="frmAssign"  styleClass="textBox textBoxMedium" style="background-color: #EEEEEE;" readonly="true" value="Multiple"/>
    	 						</td>
 							</logic:empty> --%>
 		 					<%-- <logic:notEmpty name="frmAssign" property="claimSeqID"> --%>
    		 					<%-- <td valign="top" class="textLabelBold"><bean:write name="frmAssign" property="selectedClaimNos"/></td> --%>
 							<%-- </logic:notEmpty> --%>
  				</logic:match>
  			</tr>
  			<tr>
    			<td nowrap>Al Koot Branch:<span class="mandatorySymbol">*</span><br></td>
    			<td>
    				<html:select property="officeSeqID" styleClass="selectBox selectBoxMedium">
    					<html:option value="">Select from list</html:option>
    					<html:options collection="officeInfo" property="cacheId" labelProperty="cacheDesc"/>
    				</html:select>
				</td>
  			</tr>
  			<tr>
    			<td width="18%" class="formLabel">Assigned To:<span class="mandatorySymbol">*</span></td>
    			<td>
    				<html:select property="doctor" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
    					<html:option value="">Select from list</html:option>
       					<html:options collection="alUserList" property="cacheId" labelProperty="cacheDesc"/>
    				</html:select> 
    			</td>
    		</tr>
    		
  			<tr>
    			<td width="18%" class="formLabel">Assign Claim Status:<span class="mandatorySymbol">*</span></td>
    			<td>
    						<input type="checkbox" id="ALL" name="assignClaimStatus" value=" ">
      						<label for="ALL">ALL</label>
							<input type="checkbox" id="INP" name="assignClaimStatus" value="INP">
      						<label for="INP">In Progress</label>
      						<input type="checkbox" id="REQ" name="assignClaimStatus" value="REQ">
      						<label for="REQ">Required Information</label>
      						<input type="checkbox" id="APR" name="assignClaimStatus" value="APR">
      						<label for="APR">Approved</label>
      						<input type="checkbox" id="REJ" name="assignClaimStatus" value="REJ">
      						<label for="REJ">Rejected</label>
      						<input type="checkbox" id="PCO" name="assignClaimStatus" value="PCO">
      						<label for="PCO">Closed</label>
    			</td>
    		</tr>    		
    	  	<tr>
    			<td nowrap>Assign Remarks:<span class="mandatorySymbol">*</span><br></td>
    			<td>
    				<html:select property="assignRemarks" styleClass="selectBox selectBoxListSecondLargest" >
    					<html:option value="">Select from list</html:option>
    					<html:option value="Time denial confirmation awaited">Time denial confirmation awaited</html:option>
    					<html:option value="CFD issue">CFD issue</html:option>
    					<html:option value="Source file required">Source file required</html:option>
    					<html:option value="Above limit">Above limit</html:option>
    					<html:option value="Pre auth is in Required information status">Pre auth is in Required information status</html:option>
    					<html:option value="Pharma issue claim">Pharma issue claim</html:option>
    					<html:option value="Others">Others</html:option>
    				</html:select>
				</td>
  			</tr>	
    		
  			<tr>
    			<td class="formLabel">Other Remarks:</td>
    			<td class="formLabel">
   					 <html:textarea  property="otherRemarks" styleClass="textBox textAreaLong" disabled="<%= viewmode %>"/>
   				</td>
   			 </tr>
		</table>
</fieldset>
<!-- E N D : Form Fields -->

<!-- S T A R T : Buttons -->
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td width="100%" align="center">
    		<logic:match name="viewmode" value="false">
  		  			<button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBatchAssignSave();"><u>S</u>ave</button>&nbsp;
 			</logic:match>
    					<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBack();"><u>B</u>ack</button>&nbsp;
  		</td>
  	</tr>
</table>
<%-- <% }
  %> --%>
<!-- E N D : Buttons --> 
	</div>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<html:hidden property="policySeqID"/>
	<input type="hidden" name="selectedBatchSeqId" value="<%=request.getSession().getAttribute("selectedBatchSeqId")%>">
	<%-- <html:hidden property="selectedBatchSeqId"/> --%>
	<html:hidden property="assignUserSeqID"/>
	<html:hidden property="selectedBatchNos"/>
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	</html:form>
	<!-- E N D : Content/Form Area -->