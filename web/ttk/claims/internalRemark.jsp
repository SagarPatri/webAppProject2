<%@page import="org.w3c.dom.Document"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<html>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>


<head>
<style>
div {
    
    width: 682px;
    height :225px;
    border: 1px solid black;
    padding: 25px;
    margin: 25px;
}

td.label2 {
    position: absolute;
    right: 61px;
}
td.button1{
    right: 195px;
}
td.button2{
    right: 180px;
}
</style>
<script type="text/javascript" src="/ttk/scripts/claims/claimgeneral.js"></script>
<script type="text/javascript">

function onsaveFraud(){
	
	var claimVerifiedforClaimIdValue = document.getElementById("claimVerifiedforClaimId");
	if(claimVerifiedforClaimIdValue.checked == true){
		claimVerifiedforClaimIdValue = "Y";
	}else{
		claimVerifiedforClaimIdValue = "N";	
	}
	var clmSeqId = document.getElementById("clmSeqId").value;
	
	if("" ==   document.getElementById("internalRemarkId").value){
		
		alert("Please Select Internal Remarks Status")
	}
	document.forms[0].mode.value="dosaveFraud";
	document.forms[0].action ="/SaveFraudAction.do?claimSeqId="+clmSeqId+"&claimVerifiedforClaimIdValue="+claimVerifiedforClaimIdValue;
    document.forms[0].submit();
 //   viewClaimDetails(clmSeqId); 
}

function onCloseFraud(){
	
	
	
	var clmSeqId = document.getElementById("clmSeqId").value;
    window.close();
    viewClaimDetails(clmSeqId);
   /*  window.opener.document.getElementById("clmSeqId").value=clmSeqId; */   
    
	
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Internal Remark Status</title>
</head> 
<body>
<%
pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
pageContext.setAttribute("clmSeqId", request.getParameter("clmSeqId"));
/* pageContext.setAttribute("completedYN", request.getParameter("completedYN"),PageContext.SESSION_SCOPE); */
/* pageContext.setAttribute("completedYN", request.getSession().getAttribute("completedYN")); */
%>

<html:form action="/SaveFraudAction.do">
 <font color="red" size="10"> <html:errors /> </font>
<div>
<table>


<tr>
<td>Internal Remark Status: </td>
<td>
<html:select property="internalRemarkStatus" name="frmClaimGeneral" styleId="internalRemarkId" styleClass="selectBox selectBoxMedium">
	               <html:option value="">Select from the list</html:option>
	  				<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheId" />
	  				<bean:write name="frmClaimGeneral" property="internalRemarkStatus"/>
		    	</html:select>
</td>
</tr>
 &nbsp;
<tr>
<%-- <html:textarea property="Statusremarks" name="frmClaimGeneral"/> --%>
<td valign="middle" align="left">Status Remark:</td>
<td>
 <textarea rows="4" cols="50" name="remarks">
<bean:write name="frmClaimGeneral" property="suspectedRemarks"/>
</textarea></td>
</tr>
<tr>
<td>Verified Status</td>
<td>
<html:checkbox property="claimVerifiedforClaim" name="frmClaimGeneral" styleId="claimVerifiedforClaimId" value="Y"/></td>
<td></td>
<td></td>
</tr>
<tr align="left"><td colspan="2" >Updated By : <bean:write name="frmClaimGeneral" property="userid"/>
</td>
<td align="center" class="label2">
       Updated Date : <bean:write name="frmClaimGeneral" property="suspectedUpdatededDate"/>
</td>
</tr>

</table>
</div>
<table>

<tr>

<td align="center" class="button1">
<%
if("N".equals(request.getSession().getAttribute("completedYN"))){ %>
<button  type="button" name="Button1" style="margin-left: 260px" accesskey="s" class="button1" onClick="onsaveFraud()"><u>S</u>ave
</button>
<%}%>
</td>
									
										
<td>

<td align="center" class="button2">
<button type="button" name="Button1"  accesskey="s"
											class="button2"
											onClick="onCloseFraud()">
											<u>C</u>lose
										</button>
<%-- <html:submit value="Close" onclick="onCloseFraud()"/> --%>
</td></tr>
</table>
<input type="hidden" name="clmSeqId" id="clmSeqId" value="<%=request.getParameter("clmSeqId")%>">
<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<INPUT TYPE="hidden" NAME="sortId" VALUE="">
<INPUT TYPE="hidden" NAME="pageId" VALUE="">
<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
</body>
</html>
