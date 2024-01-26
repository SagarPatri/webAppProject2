<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Activity Details</title>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
  <script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
  <link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>  	
<script type="text/javascript">

function editObserDetails(observSeqId1,activityDtlSeqId) {	
	document.forms[0].observSeqId.value = observSeqId1;
	document.forms[0].mode.value = "editObserDetails";
    document.forms[0].action ="/ObservationAction.do";
    document.forms[0].submit();
}

function saveObserDetails() {
    document.forms[0].mode.value = "saveObserDetails";
    document.forms[0].action ="/SaveObservationAction.do";
    document.forms[0].submit();
}

function deleteObservDetails() { 
	var chkopts= document.forms[0].chkopt;
	//var chkopts=document.getElementsByName('chkopt[]');	
	
	var statuss=false;
	if(chkopts.length>0){
	for(var i=0;i<chkopts.length;i++){
        if(chkopts[i].checked){
        	statuss=true;
        	break;
            }
		}
	if(statuss){
    document.forms[0].mode.value = "deleteObservDetails";
    document.forms[0].action ="/ObservationAction.do";
    document.forms[0].submit();
	}else{
     alert("select atleast one");
	}
	}else{

	if(chkopts.checked){        	
    document.forms[0].mode.value = "deleteObservDetails";
    document.forms[0].action ="/ObservationAction.do";
    document.forms[0].submit();
	}else{
     alert("select atleast one");
	}
}
	
}//deleteObservDetails()

function getObservTypeDetails(){	
	document.forms[0].mode.value = "getObservTypeDetails";
    document.forms[0].action ="/ObservationAction.do";
    document.forms[0].submit();
}
</script>
</head>
<body>
<%
pageContext.setAttribute("observationTypes", Cache.getCacheObject("observationTypes"));
//pageContext.setAttribute("observationCodes", Cache.getCacheObject("observationCodes"));
//pageContext.setAttribute("observationValueTypes", Cache.getCacheObject("observationValueTypes"));
%>
<div class="contentArea" id="contentArea">
<br>
<html:errors/>
<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
</logic:notEmpty>
<br><br>
<html:form action="/SaveObservationAction.do">
	<table align="center" class="formContainer" border="1" cellspacing="0" cellpadding="0">
			<tr  class="gridHeader">
			<th align="center">Type</th>
			<th align="center">Code</th>
			<th align="center">Value</th>
			<th align="center">Value Type</th>
		    <th align="center">Remarks</th>
		    <th align="center">
		    <input type="checkbox" name="chkAll" id="chkAll" value="all" onClick="selectAll(this.checked,document.forms[0])"/> 
		    </th>
			</tr>
			
  <logic:notEmpty name="observations" scope="session">  
      <c:forEach items="${sessionScope.observations}" var="observ">                 
       <tr>
		<td align="center">
		<a href="#" accesskey="g"  onClick="javascript:editObserDetails('${observ[0]}','${observ[1]}');"><c:out value="${observ[2]}"/></a> 
		</td>
		<td align="center"> <c:out value="${observ[3]}"/></td>
		<td align="center"><c:out value="${observ[4]}"/></td>
		<td align="center"><c:out value="${observ[5]}"/></td>
		<td align="center"><c:out value="${observ[6]}"/></td>
		<td align="center">
		<input type="checkbox" name="chkopt" id="chkopt"  onClick="toCheckBox(this,this.checked,document.forms[0])" value="${observ[0]}"/>
		</td>
	  </tr>
    </c:forEach>
    <tr>
     <td colspan="6" align="right"><html:button onclick="deleteObservDetails()" property="observDeleteBtn" value=" Delete "/></td>
  </tr>	
  </logic:notEmpty>	
  	   
</table>
<br><br><br><br>
<fieldset>	         
	<legend>Observations</legend>
	<table align="center" class="formContainer" border="1" cellspacing="0" cellpadding="0">
			<tr>
			<th align="center">Type<span class="mandatorySymbol">*</span></th>
			<th align="center">Code<span class="mandatorySymbol">*</span></th>
			<th align="center">Value <span class="mandatorySymbol">*</span></th>
			<th align="center">Value Type <span class="mandatorySymbol">*</span></th>
		    <th align="center">Remarks</th>
			</tr> 
			<tr>
			<td align="center">
			<html:select property="observType" styleId="observType" name="frmObservDetails" onchange="getObservTypeDetails()">
			 <html:option value="">Select from list</html:option>
			 <html:optionsCollection name="observationTypes" label="cacheDesc" value="cacheId" />
			</html:select>
			</td>
			<td align="center">			
			<html:select property="observCode" styleId="observCode" name="frmObservDetails">
			 <html:option value="">Select from list</html:option>
			 <logic:notEmpty name="observCodes" scope="session">
			 <html:optionsCollection name="observCodes" value="key" label="value" />
			 </logic:notEmpty>			 
			</html:select>
			</td>
			<td align="center">
			<html:text property="observValue"  name="frmObservDetails"/>
			</td>
			<td align="center">			
			<html:select property="observValueType" styleId="observValueType"  name="frmObservDetails">
			 <html:option value="">Select from list</html:option>
			 <logic:notEmpty name="observValueTypes" scope="session">
			 <html:optionsCollection name="observValueTypes" value="key" label="value"/>
			 </logic:notEmpty>			 
			</html:select>				
			</td>
			 <td class="textLabel">
				     <html:textarea property="observRemarks" name="frmObservDetails" cols="25" rows="2" />
			      </td>
			</tr>
    <tr align="center">
     <td align="center"  colspan="5">     
     <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="saveObserDetails()"><u>S</u>ave</button>&nbsp;
     <button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:window.self.close();"><u>C</u>lose</button>&nbsp;    
     </td>
   </tr>		   
 </table>
</fieldset>
<html:hidden property="preAuthSeqID" name="frmObservDetails"/>
<html:hidden property="claimSeqID" name="frmObservDetails"/>
<html:hidden property="authType" name="frmObservDetails"/>
<html:hidden property="activityDtlSeqId" name="frmObservDetails"/>
<html:hidden property="observSeqId" name="frmObservDetails"/>				
				   <input type="hidden" name="mode" value="saveObserDetails"/>
				   <INPUT TYPE="hidden" NAME="leftlink" VALUE="">
                   <INPUT TYPE="hidden" NAME="sublink" VALUE="">
                   <INPUT TYPE="hidden" NAME="tab" VALUE="">				   
</html:form>
</div>	 			    
</body>
</html>