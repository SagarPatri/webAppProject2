<%@page import="com.ttk.dto.displayOfBenefits.DisplayBenefitsVO"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.ArrayList,org.apache.struts.action.DynaActionForm"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>

<%@ page import="com.ttk.common.TTKCommon,java.util.Date,java.text.SimpleDateFormat" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/displayOfBenefits/displayOfBenefits.js"></script>
<script type="text/javascript" src="/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="/ttk/scripts/utils.js"></script>

<!-- S T A R T : Content/Form Area -->	
	<html:form action="/getPolicyBenefits.do" method="Post">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td>Display of benefits </td>
	    <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
	  </tr>
	</table>
	<%
		pageContext.setAttribute("configurationOptions", Cache.getCacheObject("configurationOptions"));
		pageContext.setAttribute("modeTypeOptions", Cache.getCacheObject("modeTypeOptions"));
 	%>
	<!-- E N D : Page Title --> 	
	<div class="contentArea" id="contentArea" >
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
	<!-- E N D : Success Box -->	
	<!-- S T A R T : Form Fields -->

 <fieldset><legend>Display of benefits</legend>
 
 
 <logic:notEmpty  property="displayBenefitsList" name="frmDisplayBenefits" scope="session"> 
 
 
 <logic:iterate id="DisplayBenefitsVO" name="frmDisplayBenefits" indexId="i" property="displayBenefitsList" >
 
 <table class="formContainer" >     
  <tr>
    <td class="formLabelBold"><bean:write name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitName"%>'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <%-- <html:checkbox property="slctcheckYN" value="Y" styleId="slctcheckYN" onclick="onBasicCheck();"/>
    <input type="hidden" name="slctcheckYN" value="N"> --%></td>
    </tr>
 </table>
    
<logic:notEmpty name="DisplayBenefitsVO"  property="benefitsDetailsList" > 
<div id="BP">

 <fieldset>
      <table class="searchContainer"  border="1" cellspacing="1" cellpadding="1" >
     
      
 	<tr > <td colspan="1"></td> </tr>
 	<thead>
	  <tr style="background-color:#677BA8">
	         <td class="formLabelBold" style="color:#FFFFFF" nowrap>Sub Benefit<br></td>
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Configuration<br></td>
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Coverage<br></td>   	
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Limit<br></td>  	
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Copay<br></td> 
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Deductible<br></td> 
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Waiting period<br></td> 
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Session allowed<br></td> 
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Mode Type<br></td>
		     <td class="formLabelBold" style="color:#FFFFFF" nowrap>Other Remarks<br></td>
		    <!--  <td class="formLabel" nowrap>Maximum Percentage Allowed:<br></td>    -->
	      	</tr>
	   </thead>   	
        
       <%--  <nested:iterate id="BenefitsDetailsVO" name="DisplayBenefitsVOi" indexId="j" property="benefitsDetailsList" > --%>
       <logic:iterate id="BenefitsDetailsVO" name="frmDisplayBenefits" indexId="j" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList"%>' >
       
       <%if(j%2==0){%>
       <tr style="background-color:#F2F4F7">
	   <%}else { %>
	   <tr style="background-color:#FFFFFF">
	   <%}%>      
	         <td class="formLabel" nowrap><bean:write name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].subBenefit"%>' filter="false" />
	         <html:hidden name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].subBenefit"%>'/></td>
		     <td class="formLabel" nowrap><html:hidden name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].subBenefitSeqId"%>' styleClass="textBox textBoxSmall"  />
		        <html:hidden name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].benefitName"%>' />
		        <html:select name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].configration"%>' styleClass="selectConfig selectBox selectBoxSmall" title="Configuration">
				  <html:optionsCollection name="configurationOptions" label="cacheDesc" value="cacheId" />
				</html:select> 
		     </td>
		     <logic:equal name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].configration"%>' value="3">
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].coverage"%>' styleClass="textBox textBoxSmall" disabled="false" title="Coverage"  /></td>   	
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].limit"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="false" title="Limit"/></td>  	
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].copay"%>' styleClass="textBox textBoxSmall" disabled="false" title="Copay"/></td> 
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].deductible"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="false" title="Deductible"  /></td>
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].waitingPeriod"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="false" title="Waiting Period"/></td>
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].sessionAllowed"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="false" title="Session Allowed"/></td>
		     <td class="formLabel" nowrap>
		     <html:select name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].modeType"%>' styleClass="selectBox selectBoxSmall" disabled="false" title="Mode Type"  >
				<html:option value="">Select from list</html:option>
				<html:optionsCollection name="modeTypeOptions" label="cacheDesc" value="cacheId" />
				</html:select> 
				</td>
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].otherRemarks"%>' styleClass="textBox textBoxSmall" disabled="false" title="Other Remarks"/></td>
			</logic:equal>
			 <logic:notEqual name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].configration"%>' value="3">
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].coverage"%>' styleClass="textBox textBoxSmall" disabled="true" title="Coverage" /></td>   	
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].limit"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="true" title="Limit"/></td>  	
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].copay"%>' styleClass="textBox textBoxSmall" disabled="true" title="Copay"/></td> 
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].deductible"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="true" title="Deductible" /></td>
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].waitingPeriod"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="true" title="Waiting Period"/></td>
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].sessionAllowed"%>' onkeyup="isNumeric(this)" styleClass="textBox textBoxSmall" disabled="true" title="Session Allowed"/></td>
		     <td class="formLabel" nowrap>
		     <html:select name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].modeType"%>' styleClass="selectBox selectBoxSmall" disabled="true" title="Mode Type"  >
				<html:option value="">Select from list</html:option>
				<html:optionsCollection name="modeTypeOptions" label="cacheDesc" value="cacheId" />
				</html:select> 
				</td>
		     <td class="formLabel" nowrap><html:text name="frmDisplayBenefits" property='<%="displayBenefitsList["+ i +"].benefitsDetailsList["+j+"].otherRemarks"%>' styleClass="textBox textBoxSmall" disabled="true" title="Other Remarks"/></td>
			</logic:notEqual>
		</tr>
	    </logic:iterate>
     	</logic:notEmpty>
	</table>
	</fieldset>
	</div>
	</logic:iterate>
	
	<table>
	<tr><td class="formLabelBold">Final Remarks:</td></tr>
     <tr><td class="textLabel" colspan="5">
     <html:textarea property="otherRemarks" cols="135" rows="2" /></td>
	</tr>
	</table>
	
	</logic:notEmpty>
 </fieldset>

	<!-- S T A R T : Buttons -->
<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" align="center">
    <%
    	if(TTKCommon.isAuthorized(request,"Edit"))
    	{
    %>
    <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doSave();"><u>S</u>ave</button>&nbsp;
	<%
		}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	%>
	<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	</td>
  </tr>
</table>

<%-- <logic:match name="frmCignaProduct" property="slctcheckYN" value="Y">
<!--  <script language="javascript">
 onOptCheck();
 onEnhancedCheck();
 onBasicCheck();
</script>
 --></logic:match>
 --%><%-- <logic:notEmpty name="frmCignaProduct" property="frmChanged">
		<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>
 --%>
<%-- <logic:notEmpty name="frmCignaProduct" property="prodPolicyLimit">
<script language="javascript">
			onDocumentLoad();
</script>
</logic:notEmpty> --%>

<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
	<!-- E N D : Buttons -->
</div>
</html:form>

