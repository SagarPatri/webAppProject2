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
function saveSuspectPreauthOrClaim(){
	var preauthorclaimswitchType = document.getElementById("preauthorclaimswitchTypeId").value;
	var claimOrPreauthSeqId = document.getElementById("seqId").value;
	var investigationstatusId = document.getElementById("investigationstatusId").value;
	var investigationoutcomecategoryId = document.getElementById("investigationoutcomecategoryId").value;
	var claimStatus = document.getElementById("claimStatusId").value;
	//var preAuthStatus = document.getElementById("preAuthStatusId").value;
	var paymentStatus = document.getElementById("paymentStatusId").value;
	if(investigationstatusId == ""){
		alert("Please Select Investigation Status");
		return;
	}
	if(investigationstatusId=="PCA" || investigationstatusId=="FD"){
		if(investigationoutcomecategoryId==""){
			alert("Please Select Investigation Outcome Category");
			return;
		}
	}
	
	if(document.forms[1].dateOfReceivingCompReqInfo.value.length!=0){
			if(isFutureDate(document.forms[1].dateOfReceivingCompReqInfo.value)){
				alert("Date Of Receiving cannot be future date");
				return true;
			}
		}
	if(preauthorclaimswitchType == "CLM" && claimStatus=="Approved" && (paymentStatus =="APPROVED BY FINANCE" || paymentStatus =="PAID" || paymentStatus =="READY_TO_BANK")){	
		var msg = confirm("Claim status is Approved and Payment status is Paid. Do you want to proceed ?");
		if(msg){
			document.forms[1].mode.value = "dosaveSuspectPreauthOrClaim";
			document.forms[1].action = "/CounterFraudSaveAction.do?preauthorclaimswitchType="+preauthorclaimswitchType+"&claimOrPreauthSeqId="+claimOrPreauthSeqId;
			document.forms[1].submit();
		}
	}else if (preauthorclaimswitchType == "CLM" && claimStatus=="Approved" && paymentStatus =="PENDING"){
		var msg1 = confirm("Claim status is Approved and Payment status is Pending. Do you want to proceed ?");
		if(msg1){
			document.forms[1].mode.value = "dosaveSuspectPreauthOrClaim";
			document.forms[1].action = "/CounterFraudSaveAction.do?preauthorclaimswitchType="+preauthorclaimswitchType+"&claimOrPreauthSeqId="+claimOrPreauthSeqId;
			document.forms[1].submit();
		}
	}else if (preauthorclaimswitchType == "CLM" && claimStatus=="In Progress"){		
		var msg1 = confirm("Claim status is In Progress. Do you want to proceed ?");
		if(msg1){
			document.forms[1].mode.value = "dosaveSuspectPreauthOrClaim";
			document.forms[1].action = "/CounterFraudSaveAction.do?preauthorclaimswitchType="+preauthorclaimswitchType+"&claimOrPreauthSeqId="+claimOrPreauthSeqId;
			document.forms[1].submit();
		}
	}else if (preauthorclaimswitchType == "CLM" && claimStatus=="Rejected"){		
		var msg1 = confirm("Claim status is Rejected. Do you want to proceed ?");
		if(msg1){
			document.forms[1].mode.value = "dosaveSuspectPreauthOrClaim";
			document.forms[1].action = "/CounterFraudSaveAction.do?preauthorclaimswitchType="+preauthorclaimswitchType+"&claimOrPreauthSeqId="+claimOrPreauthSeqId;
			document.forms[1].submit();
		}
	}else if (preauthorclaimswitchType == "CLM" && claimStatus=="PCO"){		
		var msg1 = confirm("Claim status is Closed. Do you want to proceed ?");
		if(msg1){
			document.forms[1].mode.value = "dosaveSuspectPreauthOrClaim";
			document.forms[1].action = "/CounterFraudSaveAction.do?preauthorclaimswitchType="+preauthorclaimswitchType+"&claimOrPreauthSeqId="+claimOrPreauthSeqId;
			document.forms[1].submit();
		}
	}else if (preauthorclaimswitchType == "CLM" && claimStatus=="Required Information"){		
		var msg1 = confirm("Claim status is Required Information. Do you want to proceed ?");
		if(msg1){
			document.forms[1].mode.value = "dosaveSuspectPreauthOrClaim";
			document.forms[1].action = "/CounterFraudSaveAction.do?preauthorclaimswitchType="+preauthorclaimswitchType+"&claimOrPreauthSeqId="+claimOrPreauthSeqId;
			document.forms[1].submit();
		}
	}else if (preauthorclaimswitchType == "PAT"){
		document.forms[1].mode.value = "dosaveSuspectPreauthOrClaim";
		document.forms[1].action = "/CounterFraudSaveAction.do?preauthorclaimswitchType="+preauthorclaimswitchType+"&claimOrPreauthSeqId="+claimOrPreauthSeqId;
		document.forms[1].submit();
	}
}

function closeInternalRemarks(){
	var preauthorclaimswitchType = document.getElementById("preauthorclaimswitchTypeId").value;
	document.forms[1].mode.value = "doCloseInternalRemarks";
	document.forms[1].action = "/CounterFraudSearchAction.do?preauthorclaimswitchType="+preauthorclaimswitchType;
	document.forms[1].submit();
}

/* function onChangeInvStatus(){
	if(document.getElementById("investigationstatusId").value=="II" || document.getElementById("investigationstatusId").value=="II"){
		document.getElementById("investigationoutcomecategoryId").value="";
		document.getElementById("investigationoutcomecategoryId").disabled=true;
	}else{
		document.getElementById("investigationoutcomecategoryId").disabled=false;
	}
} */

function onChangeInvStatus(){
	var investigationstatus=document.forms[1].investigationstatus.value;
	 var select = document.getElementById('investigationoutcomecategoryId');
     select.innerHTML = "";
     
	var option = document.createElement("option");
    option.text = "Select From List";
    option.value = "";
    select.add(option);
         if(investigationstatus!='II'){
        	 var option = document.createElement("option");
             option.text = "No Fraud Detected";
             option.value = "NFD";
             select.add(option);
             var option1 = document.createElement("option");
             option1.text = "Misrepresentation of facts";
             option1.value = "MOF";
             select.add(option1);
             var option2 = document.createElement("option");
             option2.text = "Collision Between Member & Provider";
             option2.value = "CBMP";
             select.add(option2);
             var option3 = document.createElement("option");
             option3.text = "Abuse and undocumented services";
             option3.value = "AAUP";
             select.add(option3);				 				 
			  var option4 = document.createElement("option");
             option4.text = "Phantom Billing";
             option4.value = "PB";
             select.add(option4);		 
			  var option5 = document.createElement("option");
             option5.text = "Duplicate billing";
             option5.value = "DB";
             select.add(option5);
			  var option6 = document.createElement("option");
             option6.text = "Unbundling of services";
             option6.value = "UOS";
             select.add(option6);
			  var option7 = document.createElement("option");
             option7.text = "Overutilization";
             option7.value = "OU";
             select.add(option7);						 
         }
        document.forms[1].mode.value = "doChangeInvStatus";
 		document.forms[1].action = "/ChangeInvStatusAction.do";
 		document.forms[1].submit();       
}//end of onPaymentTypeChanged()

function isFutureDate(argDate){

	var dateArr=argDate.split("/");	
	var givenDate = new Date(dateArr[2],dateArr[1]-1,dateArr[0]);
	var currentDate = new Date();
	if(givenDate>currentDate){
	return true;
	}
	return false;
} 

function onChangeRiskLevel()
{
		document.forms[1].mode.value = "doChangeRiskLevel";
		document.forms[1].action = "/ChangeInvStatusAction.do";
		document.forms[1].submit();   	
}
</script>
<%

pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
String claimOrPreauthNumber = (String)request.getSession().getAttribute("claimOrPreauthNumber");
pageContext.setAttribute("riskRemarksList",Cache.getCacheObject("riskRemarksList"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CounterFraudSearchAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">Suspected Fraud: Internal Remarks Status-Investigation Details</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	 <div class="contentArea" id="contentArea">
	 <html:errors/>
	 
<%
	boolean editRiskLevel = false;	
	boolean editRiskRemarks = true;		

	boolean editInvestigation = false;
	boolean editAmtUtilized = false;
	boolean editAmtSaved = false;
	boolean editDate = false;
	boolean editRemarks = false;
	
%>
<logic:equal name="frmFraudCase" property="riskLevelChangeYN" value="Y">
	<% 
		editRiskRemarks = false;
	%>
 	</logic:equal>
 <logic:equal name="frmFraudCase" property="investigationstatus" value="DOC">
	<% 
		 editInvestigation = true;
		 editAmtUtilized = true;
		 editAmtSaved = true;	
		 editDate = true;
		 editRemarks=false;
	%>
</logic:equal>	
	
<logic:equal name="frmFraudCase" property="investigationstatus" value="II">
	<% 
		 editInvestigation = true;
		 editAmtUtilized = true;
		 editAmtSaved = true;	
		 editDate = false;
		 editRemarks=false;
	%>
</logic:equal>

<logic:equal name="frmFraudCase" property="investigationstatus" value="FD">
	<% 
		 editDate = true;
	%>
</logic:equal>	

<logic:equal name="frmFraudCase" property="investigationstatus" value="PCA">
	<% 
		 editDate = true;
	%>
</logic:equal>	
<logic:equal name="frmFraudCase" property="investigationstatus" value="CA">
	<% 
		 editDate = true;
	%>
</logic:equal>
 
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
	<logic:notEmpty name="errorMsg" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="errorMsg" scope="request" /></td>
					</tr>
				</table>
			</logic:notEmpty>
	  
	 <ttk:HtmlGrid name="tableData"/>
	 <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
      	<ttk:PageLinks name="tableData"/>
	</table>
	<!-- <table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0"> -->
	<!-- <tr class="searchContainerWithTab"> -->
	<div class="internalRemarksContainer">	
		
        
         <div>Internal Remark Status:<br/></div>
	     <div>      <html:select name="frmFraudCase" property="internalRemarkStatus" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:changestatusforintrRemarks();" disabled="true">
	               <html:option value="">Any</html:option>
	  				<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheId" />
		    	</html:select>		    	       
        	</div>
    	<div style="margin-left:214px; margin-top:-35px;">Risk Level:<br/></div>
	     <div><html:select name="frmFraudCase" property="riskLevel" styleId="riskLevel" styleClass="selectBox selectBoxMoreMedium" style="margin-left:210px; position:relative;" onchange="onChangeRiskLevel();" disabled="<%=editRiskLevel%>">
	                <html:option value="">Any</html:option>
	                <html:option value="LR">Low</html:option>
		  	 		<html:option value="IR">Intermediate</html:option>
		  	 		<html:option value="HR">High</html:option>
            	</html:select>
       </div>
      
        <div style="margin-left:450px; margin-top:-31px;">Risk Remarks:<span class="mandatorySymbol">*</span><br/></div>
	     <div>      <html:select name="frmFraudCase" property="riskRemarks" styleId="riskRemarks" styleClass="selectBox selectBoxMoreMedium" style="margin-left:450px;" disabled="<%=editRiskRemarks%>" >
	                <html:option value="">Select from List</html:option>
		  	 		<html:optionsCollection name="riskRemarksList" label="cacheDesc" value="cacheId" />
            	</html:select> 
       </div>
       
         <div style="margin-left:700px; margin-top:-16px;">Pre-Approval/Claim No.: <%=claimOrPreauthNumber%> </div>
     
     <div style="margin-top:29px;">Investigation Status:<span
							class="mandatorySymbol">*</span><br/></div>
	     <div><html:select property="investigationstatus" styleId="investigationstatusId" style="position:relative;" styleClass="selectBox selectBoxMoreMedium" onchange="onChangeInvStatus();">
	              <%--  <html:option value="">Select From List </html:option> --%>
	                <html:option value="DOC">Document Required</html:option>
	                <html:option value="II">Investigation In-progress </html:option>
	                <html:option value="CA">Cleared for Approval</html:option>
		  	 		<html:option value="PCA">Partially Cleared For Approval</html:option>
		  	 		<html:option value="FD">Fraud Detected</html:option>
            	</html:select>
       </div>
     <logic:empty name="frmFraudCase" property="investigationstatus">
     <div style="margin-left:225px; margin-top:-39px;">Investigation Outcome Category:<br/></div>
	     <div>      
	     <html:select property="investigationoutcomecategory" styleId="investigationoutcomecategoryID" styleClass="selectBox selectBoxMoreMedium" style="margin-left:225px;" disabled="<%=editInvestigation%>">
	                <html:option value="">Select From List</html:option>
	                </html:select></div>
     </logic:empty>
     <logic:notEmpty name="frmFraudCase" property="investigationstatus">
    <div style="margin-left:225px; margin-top:-39px;">Investigation Outcome Category:<br/></div>
	     <div>      
	     <html:select property="investigationoutcomecategory" styleId="investigationoutcomecategoryId" styleClass="selectBox selectBoxMoreMedium" style="margin-left:225px;" disabled="<%=editInvestigation%>">
	          <logic:equal name="frmFraudCase" property="investigationstatus" value="II">     
	          		<html:option value="">Select From List</html:option>
	          </logic:equal>
	         <logic:notEqual name="frmFraudCase" property="investigationstatus" value="II"> 
	                <html:option value="">Select From List</html:option>
	          <logic:notEqual name="frmFraudCase" property="investigationstatus" value="FD">       
	                <html:option value="NFD">No Fraud Detected</html:option>
	          </logic:notEqual>      
	                
	                <html:option value="MOF">Misrepresentation of facts</html:option>
	                <html:option value="CBMP">Collision Between Member & Provider</html:option>
		  	 		<html:option value="AAUP">Abuse and undocumented services</html:option>
		  	 		<html:option value="PB">Phantom Billing</html:option>
		  	 		<html:option value="DB">Duplicate billing</html:option>
		  	 		<html:option value="UOS">Unbundling of services</html:option>
		  	 		<html:option value="OU">Overutilization</html:option>
		  	 		<html:option value="HSUS">Highly Suspected</html:option>
		  	 		</logic:notEqual>
            </html:select>
       </div>
      </logic:notEmpty>
      <div style="margin-left:450px;margin-top:-39px;">Amount Utilized For Investigation:<br>
            <html:text property="amountutilizationforinvestigation" onkeyup="isNumeric(this)" disabled="<%=editAmtUtilized%>" />
        	</div>
      <div style="margin-left:700px;margin-top:-39px;">Amount Saved:<br>
            <html:text property="amountsave" disabled="<%=editAmtSaved%>" onkeyup="isNumeric(this)" />
        	</div>
        	
      <div style="margin-left:890px;margin-top:-46px;">Date Of Receiving Complete<br> Required Information:<br>
	   			<html:text property="dateOfReceivingCompReqInfo" name="frmFraudCase" styleClass="textBox textDate" maxlength="10" disabled="<%=editDate%>" />         	
	            <logic:equal name="frmFraudCase" property="investigationstatus" value="DOC">
					<img src="/ttk/images/CalendarIcon.gif" name="mrkDate" width="24" height="17" border="0" align="absmiddle">
 				</logic:equal>
 				<logic:equal  name="frmFraudCase" property="investigationstatus" value="II" >						
	            		<A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].dateOfReceivingCompReqInfo',document.forms[1].dateOfReceivingCompReqInfo.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            	</logic:equal>
            	 <logic:equal name="frmFraudCase" property="investigationstatus" value="FD">
					<img src="/ttk/images/CalendarIcon.gif" name="mrkDate" width="24" height="17" border="0" align="absmiddle">
 				</logic:equal>
 				 <logic:equal name="frmFraudCase" property="investigationstatus" value="PCA">
					<img src="/ttk/images/CalendarIcon.gif" name="mrkDate" width="24" height="17" border="0" align="absmiddle">
 				</logic:equal>
 				 <logic:equal name="frmFraudCase" property="investigationstatus" value="CA">
					<img src="/ttk/images/CalendarIcon.gif" name="mrkDate" width="24" height="17" border="0" align="absmiddle">
 				</logic:equal>
            	</div>
      
     <div style="margin-top:30px;">Remarks.:<br>
            <html:textarea property="remarksforinternalstatus" style="width:510px; height:210px;" disabled="<%=editRemarks%>"/>
        	</div>
       
    
    <div style="margin-left:541px; margin-top:-30px;">
        
          	<button type="button" name="Button1" accesskey="v"
											class="buttons" onMouseout="this.className='buttons'"
											onMouseover="this.className='buttons buttonsHover'"
											onClick="saveSuspectPreauthOrClaim()">
											Sa<u>v</u>e
										</button></div>      	
    </div>
        	    
	<!-- </table> -->
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
   <div style="margin-top:20px;">
   Investigation Details </div>
   <div>
 
   <ttk:HtmlGrid name="tableDataForInvesigation"/>
    <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
      	<ttk:PageLinks name="tableDataForInvesigation"/>
	</table>
	
	<table align="center" class="buttonsContainerGrid1"  border="0" cellspacing="0" cellpadding="0">
 	 <tr>
	    <td width="100%" nowrap align="right">
	    <button type="button" onclick="closeInternalRemarks();" name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
	    </td>
	    </tr>
	    </table>
   </div>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
      	
	 
	</div>
	     	
    
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
	<input type="hidden" name="preauthorclaimswitchType" id="preauthorclaimswitchTypeId" value="<%=request.getSession().getAttribute("preauthorclaimswitchType")%>">
	<input type="hidden" name="claimOrPreauthSeqId" id="seqId" value="<%=request.getSession().getAttribute("claimOrPreauthSeqId")%>">
	<input type="hidden" name="preAuthStatus" id="preAuthStatusId" value="<%=request.getSession().getAttribute("preAuthStatus")%>">
	<input type="hidden" name="claimStatus" id="claimStatusId" value="<%=request.getSession().getAttribute("claimStatus")%>">
	<input type="hidden" name="paymentStatus" id="paymentStatusId" value="<%=request.getSession().getAttribute("vishwa")%>">
	<html:hidden name="frmFraudCase" property="riskLevelChangeYN" styleId="riskLevelChangeYN" />
</html:form>
<!-- E N D : Content/Form Area -->