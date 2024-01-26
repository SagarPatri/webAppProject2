<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<script>
bAction=false;
var TC_Disabled = true;
var JS_SecondSubmit=false;
</script>
<script type="text/javascript">
function changestatus(){
	document.forms[1].mode.value = "doStatusChangeForRemarks";
	document.forms[1].action = "/ClaimInternalRemarksAction.do";
	document.forms[1].submit();
}

function saveAndGetClaimSusect(){
	var clmSeqId = document.getElementById("clmSeqId").value;
	var claimVerifiedforClaimIdValue = document.getElementById("claimVerifiedforClaimId");
	if(claimVerifiedforClaimIdValue != null && claimVerifiedforClaimIdValue.checked == true){
		claimVerifiedforClaimIdValue = "Y";
	}else{
		claimVerifiedforClaimIdValue = "N";	
	}
	document.forms[1].mode.value = "dosaveFraud";
	document.forms[1].action = "/InternalRemarksClaimSaveAction.do?clmSeqId="+clmSeqId+"&claimVerifiedforClaimIdValue="+claimVerifiedforClaimIdValue;
	document.forms[1].submit();
}

function closeIntrClaim(){
	document.forms[1].mode.value = "doCloseInternalRemarks";
	document.forms[1].action = "/ClaimGeneralAction.do";
	document.forms[1].submit();
}
</script>

<%

pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
String claimStatus = (String)request.getSession().getAttribute("claimStatus");
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/ClaimInternalRemarksAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">Internal Remarks Status</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	
	<div class="contentArea" id="contentArea"> 
	<html:errors/>
	<logic:notEmpty name="successMsg" scope="request">
				<table align="center" class="successContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success"
							width="16" height="16" align="absmiddle">&nbsp; 
							<bean:write name="successMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
	<div class="internalRemarksContainer">	
		
        <%   if(request.getSession().getAttribute("SUSPECTFLAG") != null && request.getSession().getAttribute("SUSPECTFLAG").equals("DISABLED")){
        %>
         <div>Internal Remark Status:<span
							class="mandatorySymbol">*</span><br/></div>
	     <div>      <html:select property="internalRemarkStatus" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:changestatus();" disabled="true">
	               <html:option value="">Any</html:option>
	  				<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheId" />
		    	</html:select>		    	       
        	</div>
        <%}else{ %> 	
        <div>Internal Remark Status:<span
							class="mandatorySymbol">*</span><br/></div>
	     <div>      <html:select property="internalRemarkStatus" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:changestatus();">
	               <html:option value="">Any</html:option>
	  				<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheId" />
		    	</html:select>		    	       
        	</div>
        <%} %>
        <%  if(request.getSession().getAttribute("SUSPECTFLAG") != null && request.getSession().getAttribute("SUSPECTFLAG").equals("DISABLED")){ %>
            <div style="margin-left:249px; margin-top:-40px;">Risk Level:<span
							class="mandatorySymbol">*</span><br/></div>
	      <div>       <html:select property="riskLevel" styleClass="selectBox selectBoxMoreMedium" style="margin-left:225px;" disabled="true">
	                <html:option value="">Any</html:option>
	                <html:option value="LR">Low</html:option>
		  	 		<html:option value="IR">Intermediate</html:option>
		  	 		<html:option value="HR">High</html:option>
            	</html:select>
       </div>
        <%}else{ %>
        <%   if(request.getSession().getAttribute("internalRemarkStatus") !=null && request.getSession().getAttribute("internalRemarkStatus").equals("SUSP")){%>
        <div style="margin-left:249px; margin-top:-40px;">Risk Level:<span
							class="mandatorySymbol">*</span><br/></div>
	     <div>       <html:select property="riskLevel" styleClass="selectBox selectBoxMoreMedium" style="margin-left:225px;">
	                <html:option value="">Any</html:option>
	                <html:option value="LR">Low</html:option>
		  	 		<html:option value="IR">Intermediate</html:option>
		  	 		<html:option value="HR">High</html:option>
            	</html:select>
       </div>
       <%}} %>
        
         <div style="margin-left:450px; margin-top:-21px;">Current Internal Remarks Status: <b><bean:write name="frmFraudCase" property="currecntIntrRemarkStatus"/> </b></div>
        <%if(request.getSession().getAttribute("internalRemarkStatus")!=null){
        if(!request.getSession().getAttribute("internalRemarkStatus").equals("SUSP") && !request.getSession().getAttribute("internalRemarkStatus").equals("")) {%> 
        <div style="padding-left:898px; margin-top:-14px;">Verified: <html:checkbox property="suspectVerified" styleId="claimVerifiedforClaimId" value="Y"/></div>
     <%}}%>
     <div style="margin-top:30px;">Remarks.:<br>
            <html:textarea property="remarksforinternalstatus" style="width:510px; height:210px;"/>
        	</div>
    <%if(request.getSession().getAttribute("INVESTTABLEFLAG") != null && request.getSession().getAttribute("INVESTTABLEFLAG").equals("display")){ %>   	
     <div style="margin-left:540px; margin-top:-194px;">Investigation Status :
    <b> <bean:write name="frmFraudCase" property="investigationStatusDesc"/> </b>
     </div>	
     <div style="margin-left:540px; margin-top:9px;">Investigation Outcome Category :
    <b> <bean:write name="frmFraudCase" property="investigationOutcomeCatgDesc"/> </b>
     </div>
     <div style="margin-left:540px; margin-top:9px;">Investigation TAT(In Days) :
    <b> <bean:write name="frmFraudCase" property="investigationTAT"/> </b>
     </div>
     
      
        <%if(claimStatus.equals("In Progress")  || claimStatus.equals("Required Information")) {%>
        <div style="margin-left:541px; margin-top:85px;">
          	<button type="button" name="Button1" accesskey="v"
											class="buttons" onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="javascript:saveAndGetClaimSusect();">
											Sa<u>v</u>e
										</button></div>
					<%} %>
     <%} else{%>
 	   
    <%if(claimStatus.equals("In Progress")  || claimStatus.equals("Required Information")) {%>
    <div style="margin-left:541px; margin-top:-30px;">
        
          	<button type="button" name="Button1" accesskey="v"
											class="buttons" onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="javascript:saveAndGetClaimSusect()">
											Sa<u>v</u>e
										</button></div>
   
   <%}}%>
      </div>
		 <ttk:HtmlGrid name="tableData"/> 
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
      <%-- 	<ttk:PageLinks name="tableData"/> --%>
      <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
      	<ttk:PageLinks name="tableData"/>
	</table>
	<%if(request.getSession().getAttribute("INVESTTABLEFLAG") != null && request.getSession().getAttribute("INVESTTABLEFLAG").equals("display")){ %>
	<div>
   <ul>Investigation Details</ul> </div>
   <div>
 
   <ttk:HtmlGrid name="tableDataForInvesigation"/>
    <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
      	<ttk:PageLinks name="tableDataForInvesigation"/>
	</table>
   <%}%>
	<table align="center" class="buttonsContainerGrid1"  border="0" cellspacing="0" cellpadding="0">
 	 <tr>
	    <td width="100%" nowrap align="right">
	    <button type="button" onclick="javascript:closeIntrClaim();" name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
	    </td>
	    </tr>
	    </table>
    </div>
	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<input type="hidden" name="clmSeqId" id="clmSeqId" value="<%=request.getSession().getAttribute("clmSeqId")%>">
</html:form>
<!-- E N D : Content/Form Area -->