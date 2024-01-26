<%
/** @ (#) copaymentcharges.jsp
 * Project     : TTK Healthcare Services
 * File        : copaymentcharges.jsp
 * Author      : Balaji C R B
 * Company     : Span Systems Corporation
 * Date Created: 3 Nov 2008
 *
 * @author 			Balaji C R B
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<!--  -->
<link rel="stylesheet" type="text/css" href="css/redmond/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="css/redmond/style.css" />
<link rel="stylesheet" type="text/css" href="css/redmond/jquery-ui.css" />
<script type="text/javascript" src="css/redmond/jquery.js"></script>
<script type="text/javascript" src="css/redmond/jquery-ui.min.js"></script>
<script type="text/javascript" src="css/redmond/jquery.multiselect.js"></script>
<script type="text/javascript">
$(function(){
	$("select")
		.filter(".single")
		.multiselect({
			multiple: false,
			noneSelectedText: 'Please select a radio',
			header: false
		})
		.end()
		.not(".single")
		.multiselect({
			noneSelectedText: 'Please select an Option'
		})});
</script>
<!--  -->

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/administration/copaymentcharges.js"></script>
<%
	pageContext.setAttribute("providerCopayBenefits",Cache.getCacheObject("providerCopayBenefits"));
	pageContext.setAttribute("alServiceName", Cache.getCacheObject("copayserviceName"));
%>
<!-- S T A R T : Content/Form Area -->
	<html:form action="/CopaymentAction.do" method="post">
	<!-- S T A R T : Page Title -->
    <table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
      <tr>
      	<td>Copayment Details -<bean:write name="frmCopayment" property="caption"/></td>
      	<td align="right"></td>
        <td align="right" ></td>
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
 	<!-- E N D : Success Box -->

    <!-- S T A R T : Form Fields -->	
	<fieldset>
 	<legend>Provider Wise Copayment Charges</legend>
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
      <%-- Existing Code
      <tr>
        <td width="21%" nowrap class="formLabel">Copayment per Claim charges equals (Rs.) 
			<html:text property="copayAmt" styleClass="textBox textBoxSmall" maxlength="15"/>
			Fixed Amount OR 
			<html:text property="copayPerc" styleClass="textBox textBoxTiny" maxlength="2"/>&nbsp;% of Approved Amount.		
		</td>   
      </tr> --%>     
      <tr>
        <td class="gridHeader"> Benefit Type <span class="mandatorySymbol">*</span></td>
        <td class="gridHeader"> Service Type </td>
        <td class="gridHeader"> Copay in % </td>
        <td class="gridHeader"> Deductible Amount </td>
        <td class="gridHeader"> Suffix </td>
        <td class="gridHeader" title="Apply Rule Wise Copay and Deductible"> Apply Rule Wise... </td>
      </tr>
      
      <tr>
        <td class="formLabel"> 
        	<html:select property ="providerCopayBenefitsList" multiple="multiple">
           			<%-- <html:option value="">Select from list</html:option> --%>
           			<html:options collection="providerCopayBenefits" property="cacheId" labelProperty="cacheDesc"/>
   			</html:select> 
   		</td>
        <td class="formLabel"> 
        	<html:select property ="serviceTypeList" multiple="true">
           			<html:options collection="alServiceName" property="cacheId" labelProperty="cacheDesc"/>
   			</html:select>  
		</td>
        <td class="formLabel"> 
        	<html:text property="copayPerc" styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isAmountValue(this)"/> 
       	</td>
        <td class="formLabel"> 
        	<html:text property="deductible" styleClass="textBox textBoxSmall" maxlength="10" onkeyup="isAmountValue(this)"/> 
       	</td>
        <td class="formLabel"> 
        	<html:select property ="suffix" styleClass="single">
           			<html:option value="MIN">MIN</html:option>
           			<html:option value="MAX">MAX</html:option>
           			<html:option value="BOTH">BOTH</html:option>
   			</html:select> 
		</td>
        <td class="formLabel"> 
        	<html:select property ="applyRule" styleClass="single">
           			<html:option value="Y">Yes</html:option>
           			<html:option value="N">No</html:option>
   			</html:select>  
		</td>
      </tr>
      
    </table>
	</fieldset>
	<br>
	<fieldset>
 	<legend> Provider Wise Copayment Charges Details </legend>
 	
 	<%-- <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="gridHeader"> Benefit Type </td>
        <td class="gridHeader"> Service Type </td>
        <td class="gridHeader"> Copay in % </td>
        <td class="gridHeader"> Deductible Amount </td>
        <td class="gridHeader"> Suffix </td>
        <td class="gridHeader" title="Apply Rule Wise Copay and Deductible"> Apply Rule Wise... </td>
        <td class="gridHeader"> Delete </td>
	</tr>
	<% int i=0; %>
<logic:notEmpty name="alCopayDetails" scope="session">
	<logic:iterate id="hospitalCopayVO" name="alCopayDetails" scope="session">
		<%
			String strClass=i%2==0 ? "gridOddRow" : "gridEvenRow" ;
			i++;
		%>
		<tr class=<%=strClass%>>
			<td><bean:write name="hospitalCopayVO" property="providerCopayBenefits"/></td>
			<td><bean:write name="hospitalCopayVO" property="serviceType"/></td>
			<td><bean:write name="hospitalCopayVO" property="copayPerc"/>%</td>
			<td><bean:write name="hospitalCopayVO" property="deductible"/></td>
			<td><bean:write name="hospitalCopayVO" property="suffix"/></td>
			<td><bean:write name="hospitalCopayVO" property="applyRule"/></td>
			<td>
				<a href="#" onClick="deleteProviderCopay(<bean:write name="hospitalCopayVO" property="copaySeqId"/>)">
				<img src="/ttk/images/DeleteIcon.gif" alt="Delete Provier Copay" width="16" height="16" border="0">
				</a>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
	</table> --%>        
	
	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="copaytableData" />
	<!-- E N D : Grid -->
 	</fieldset>
	<!-- E N D : Form Fields -->
	
	

    <!-- S T A R T : Buttons -->
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100%" align="center">  	
	    <%
	       if(TTKCommon.isAuthorized(request,"Edit"))
	       {
    	%>    
	    <%-- <logic:empty name="updated" scope="request"> --%>
	       	<button type="button" name="Button"  accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onSave();"><u>S</u>ave</button>&nbsp;
	       	<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset();"><u>R</u>eset</button>&nbsp;
	    <%-- </logic:empty> --%>
	    <%
	    	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
		%>
	       	<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	       	
			<ttk:PageLinks name="copaytableData"/>
	       	
		</td>
	  </tr>
	</table>
	</div>
	<!-- E N D : Buttons -->	
	<html:hidden property="prodPolicySeqID"/>
	<input type="hidden" name="mode">
    <html:hidden property="caption" />
   	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
    
	</html:form>
	<!-- E N D : Content/Form Area -->