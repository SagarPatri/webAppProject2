<%
/**
 * @ (#) policydetails.jsp Feb 2, 2006
 * Project      : TTK HealthCare Services
 * File         : policydetails.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : Feb 2, 2006
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.WebBoardHelper"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/enrollment/policydetails.js"></script>
<script type="text/javascript" src="/ttk/scripts/jquery/ttk-jquery.js"></script>
<script type="text/javascript" src="/ttk/scripts/async.js"></script>
<script type="text/javascript">
$(document).ready(function() {
   getBroker();
});
  
function getNetworkType(){
	var productSeqID=document.getElementById("productSeqID").value;
	$(document).ready(function(){
	  	 $.ajax({
	  	     url :"/asynchronAction.do?mode=getProductNetworkType&productSeqID="+productSeqID,
	  	     dataType:"text",
	  	     success : function(data) {	  	     
	  	 var networkType = document.getElementById("networkType");
	  	 while (networkType.firstChild) {
	  		networkType.removeChild(networkType.firstChild);
	  		}	  	
	  		 var div=document.createElement("div");	
	  		 var node = document.createTextNode(data);
	  		 div.appendChild(node);
	  		networkType.appendChild(div);	  		     
	  	     }
	  	 });
	 });	
}

</script>
<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>	


<%
	boolean viewmode=true;
	boolean viewtype=false;
	boolean viewdtl=false;
	
	boolean blnENDViewmode = false;
	boolean blnInsEndorsement=false;
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))
	String strSubLink=TTKCommon.getActiveSubLink(request);
	pageContext.setAttribute("officeinfo",Cache.getCacheObject("officeInfo"));
	pageContext.setAttribute("policyStatus",Cache.getCacheObject("policyStatus"));
	pageContext.setAttribute("clarificationstatus", Cache.getCacheObject("clarificationstatus"));
	pageContext.setAttribute("logicType", Cache.getCacheObject("logicType"));
	pageContext.setAttribute("creditNotePeriod", Cache.getCacheObject("creditNotePeriod"));
	pageContext.setAttribute("viewmode",new Boolean(viewmode));
%>
<logic:match name="frmPolicyList" property="switchType" value="END">
<% blnENDViewmode =true; %>
</logic:match>

<html:form action="/PolicyDetailsAction.do" enctype="multipart/form-data">
<!-- S T A R T : Page Title -->

<logic:empty name="frmPolicyDetails" property="display">

<logic:match name="frmPolicyDetails" property="enrollmentDesc" value="Non-Corporate">
<% viewtype=true; %>
</logic:match>

<logic:match name="frmPolicyDetails" property="memcountYN" value="Y">
<% viewdtl=true; %>
</logic:match>

	<logic:match name="frmPolicyList" property="switchType" value="ENM">
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	</logic:match>
	<logic:match name="frmPolicyList" property="switchType" value="END">
	<table align="center" class="pageTitleHilite" border="0" cellspacing="0" cellpadding="0">
	</logic:match>
	  	<tr>
	    	 <td><bean:write name="frmPolicyDetails" property="caption"/></td>
			 <td align="right" class="webBoard">
			 		<logic:notEmpty name="frmPolicyDetails" property="policySeqID">
			 		<%@ include file="/ttk/common/toolbar.jsp" %>
			 		</logic:notEmpty >
			 </td>
		</tr>
	</table>
	<%--<logic:notMatch name="frmPolicyList" property="switchType" value="END">
		<%// strENDViewmode = false;%>
	</logic:notMatch>--%>
<!-- E N D : Page Title -->

	<!-- Added Rule Engine Enhancement code : 27/02/2008 -->
	<ttk:BusinessErrors name="BUSINESS_ERRORS" scope="request" />
	<!--  End of Addition -->

	<div class="contentArea" id="contentArea">
	<logic:match name="frmPolicyList" property="switchType" value="ENM">
		<table width="98%" align="center"  border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	   		 <td></td>
	   		  <td align="right">
			  	<strong><bean:write property="eventName" name="frmPolicyDetails"/></strong>&nbsp;&nbsp;<logic:match name="viewmode" value="false"><ttk:ReviewInformation/></logic:match>&nbsp;
			 </td>
	    </tr>
		</table>
	</logic:match>
	<logic:notEmpty name="fileError" scope="request" >
	<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="fileError" scope="request"/>
	        </td>
	      </tr>
   	 </table>
</logic:notEmpty>
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
	<fieldset>
    	<legend>Insurance Company</legend>
	    <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td width="21%" class="formLabel">Insurance Name:</td>
	        <td width="33%" class="textLabelBold"><bean:write name="frmPolicyDetails" property="companyName"/></td>
	        <td width="23%" class="formLabel">Insurance ID: </td>
	        <td width="23%" class="textLabelBold">
	        	<bean:write name="frmPolicyDetails" property="officeCode"/>
	        </td>
	      </tr>
	    </table>
	</fieldset>

	<ttk:PolicyInfo/>
	<ttk:InsuranceRelatedInformation/>

	<fieldset>
    <legend>Al Koot Related Information</legend>
    <table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="formLabel">Policy Status: <span class="mandatorySymbol">*</span></td>
        <td class="formLabelBold">


        <logic:match name="frmPolicyList" property="switchType" value="END">
			<html:select property="policyStatusID" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode %>">
				<html:optionsCollection name="policyStatus" label="cacheDesc" value="cacheId" />
			</html:select>
		</logic:match>

        <logic:match name="frmPolicyList" property="switchType" value="ENM">
			<bean:write name="frmPolicyDetails" property="policyStatusDesc"/>
		</logic:match>


        </td>
        <td class="formLabel">Term Status: </td>
        <td class="formLabelBold"><bean:write name="frmPolicyDetails" property="TPAStatusTypeID"/></td>
      </tr>
      <tr>
        <td width="21%" class="formLabel">Policy Received Date: <span class="mandatorySymbol">*</span></td>
        <td width="33%">
        <html:text property="recdDate" styleClass="textBox textDate" maxlength="10" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
       <logic:match name="viewmode" value="false">
		        <a name="CalendarObjectClDate" id="CalendarObjectClDate" href="#" onClick="javascript:show_calendar('CalendarObjectClDate','frmPolicyDetails.recdDate',document.frmPolicyDetails.recdDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" name="incpDate" width="24" height="17" border="0" align="absmiddle"></a>
       </logic:match>
        </td>
        <td class="formLabel" width="23%">Al Koot Branch: <span class="mandatorySymbol">*</span></td>
        <td width="23%">
	      <html:select property="officeSeqID" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode | blnENDViewmode %>">
			<html:option value="">Select from list</html:option>
			<html:optionsCollection name="officeinfo" label="cacheDesc" value="cacheId" />
		  </html:select>
        </td>
      </tr>
      <tr>
        <td class="formLabel">Policy Condoned: </td>
        <td><span class="textLabel">
			<html:checkbox property="condonedYN" value="Y" disabled="<%=viewmode%>"/>
	        </span>
	    </td>
	    <td class="formLabel">&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table>
	</fieldset>


	<fieldset>
    <legend>Sum Insured</legend>
    <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">  
      <tr>
        <td width="21%" nowrap class="formLabel">Total Group Sum Insured : </td>
        <td width="21%" class="textLabelBold">
        	<html:text property="totalSumInsured" styleClass="textBox textBoxStandard" maxlength="17" disabled="<%= viewtype | viewmode | blnENDViewmode %>" readonly="<%= viewmode | blnENDViewmode %>" onkeyup="isNumeric(this)" />
        	<logic:empty property="currencyFormat" name="frmPolicyDetails">
        	<html:text property="currencyFormat"  styleClass="textBox textBoxTiny"  value="QAR"  maxlength="250"   styleId="currencyFormat"  readonly="true"/>
        	</logic:empty>
        	<logic:notEmpty property="currencyFormat" name="frmPolicyDetails">
        	<html:text property="currencyFormat"  styleClass="textBox textBoxTiny"    maxlength="250"   styleId="currencyFormat"  readonly="true"/>
        	</logic:notEmpty>
        	
		  <a href="#" onclick="openRadioList('currencyFormat','CURRENCY_GROUP','option')">
		  <img src="/ttk/images/EditIcon.gif" width="16" height="16" title="Select Currancy" border="0" ></a>
        	<%if((strSubLink.equals("Corporate Policy")) || (strSubLink.equals("Non-Corporate Policy"))) 
        	  {
        	%>
       	    <logic:match name="frmPolicyList" property="switchType" value="END">
       	      <a href="#" onClick="javascript:onSIInfo();"><img src="/ttk/images/RedStar.gif" title="Sum Insured Info" width="16" height="16" border="0" /></a>
       	    </logic:match>
       	    <%
        	  }//end of if((strSubLink.equals("Corporate Policy")) || (strSubLink.equals("Non-Corporate Policy"))
       	    %>
       	</td>    
       	</tr>   	
	<%-- 	<td width="61%" class="textLabelBold"><bean:write name="frmPolicyDetails" property="sumWording"/></td> --%>
     <tr>
	  <td width="21%" nowrap class="formLabel">Annual Aggregate Limit Per Member: <span class="mandatorySymbol">*</span></td>
        <td width="21%" class="textLabelBold">
        	<html:text property="limitPerMember" styleClass="textBox textBoxStandard" maxlength="17" disabled="<%= viewtype | viewmode | blnENDViewmode %>" readonly="<%= viewmode | blnENDViewmode %>" onkeyup="isNumeric(this)" />
        	<logic:empty property="limitCurrencyFormat" name="frmPolicyDetails">
        	<html:text property="limitCurrencyFormat"  styleClass="textBox textBoxTiny"  value="QAR"  maxlength="250"   styleId="limitCurrencyFormat"  readonly="true"/>
        	</logic:empty>
        	<logic:notEmpty property="limitCurrencyFormat" name="frmPolicyDetails">
        	<html:text property="limitCurrencyFormat"  styleClass="textBox textBoxTiny"    maxlength="250"   styleId="limitCurrencyFormat"  readonly="true"/>
        	</logic:notEmpty>
        	
		  <a href="#" onclick="openRadioList('limitCurrencyFormat','CURRENCY_GROUP','option')">
		  <img src="/ttk/images/EditIcon.gif" width="16" height="16" title="Select Currancy" border="0" ></a>
        	<%if((strSubLink.equals("Corporate Policy")) || (strSubLink.equals("Non-Corporate Policy"))) 
        	  {
        	%>
       	    <logic:match name="frmPolicyList" property="switchType" value="END">
       	      <a href="#" onClick="javascript:onSIInfo();"><img src="/ttk/images/RedStar.gif" title="Sum Insured Info" width="16" height="16" border="0" /></a>
       	    </logic:match>
       	    <%
        	  }//end of if((strSubLink.equals("Corporate Policy")) || (strSubLink.equals("Non-Corporate Policy"))
       	    %>
       	</td>     
	
	
	
	
	
	
	
      </tr>
      <tr>
        <td class="formLabel">Net Premium : </td>
        <td class="textLabelBold">
		 	<html:text property="totalPremium" styleClass="textBox textBoxStandard" maxlength="13" disabled="<%= viewtype | viewmode | blnENDViewmode %>" readonly="<%= viewmode | blnENDViewmode %>" onkeyup="isNumeric(this)"/>
		 </td>		
		<td class="textLabelBold">
		    <bean:write name="frmPolicyDetails" property="premiumWording"/>
 	    </td> 	    
      </tr>
      <tr>
      
       <td class="formLabel">Premium Details&nbsp;
		 <a href="#" onclick="selectPremiumDetails()">
		 <img src="/ttk/images/EditIcon.gif" width="16" height="16" title="Select Currancy" border="0" align="middle" >
		 
		 </a>
        </td>
        <td class="formLabel"></td>
      
      
        <td class="formLabel">Maternity Premium : <span class="mandatorySymbol">*</span></td>
        <td class="textLabelBold">
		 	<html:text property="matPremium" styleClass="textBox textBoxStandard" maxlength="13" disabled="<%=viewdtl| viewtype | viewmode | blnENDViewmode %>" readonly="<%= viewdtl| viewmode | blnENDViewmode %>" onkeyup="isNumeric(this)"/>
		 </td>		
 	    
 	   <%--   <td class="formLabel" width="23%">Excluding maternity Premium : <span class="mandatorySymbol">*</span></td>
        <td class="textLabelBold">
		 	<html:text property="exlMatPremium" styleClass="textBox textBoxStandard" maxlength="13" disabled="<%= viewdtl| viewtype | viewmode | blnENDViewmode %>" readonly="<%= viewdtl | viewmode | blnENDViewmode %>" onkeyup="isNumeric(this)"/>
		 </td>		 --%>
		    
      </tr>
      
       <tr><td>&nbsp;</td></tr>
       <tr>
			       <td class="formLabel" width="30%">Refund condition :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmPolicyDetails" property="logicType"  disabled="<%= viewdtl| viewmode | blnENDViewmode %>" readonly="<%= viewdtl | viewmode | blnENDViewmode %>" styleClass="selectBox selectBoxMedium" onchange="onchangeRefundCondition()">
							  <html:option value="">Select from list</html:option>
							<html:optionsCollection name="logicType" value="cacheId" label="cacheDesc"/>
					  </html:select>
			      </td>	
				  <td class="formLabel" width="30%">Administration Charges :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				     	<html:text name="frmPolicyDetails" onkeyup="isNumeric(this);" disabled="<%= viewdtl| viewmode | blnENDViewmode %>" readonly="<%= viewdtl|viewmode | blnENDViewmode %>" property="administrationCharges" onkeyup="isNumeric(this);"  styleClass="textBox textBoxVerySmall" /><b style="color: black;">%</b>
			      </td>	
			    </tr>
			   
			    <tr>
					 <logic:equal  name="frmPolicyDetails" property="logicType" value="SC1">
						<td></td>
						<td>
							<font color=blue><b>a. Claimed amount  > Member Prorata premium then no refund<br>b. Claimed amount < Member Prorata premium then refund = Member Prorata premium - claimed amount</b></font> 
						</td>
					</logic:equal>
					 <logic:equal  name="frmPolicyDetails" property="logicType" value="SC2">
						<td></td>
						<td>
							<font color=blue><b>a. If member is having any claims then no refund<br>b. If member is not having any claims then refund Member prorata premium.</b></font> 
						</td>
					</logic:equal>
					 <logic:equal  name="frmPolicyDetails" property="logicType" value="SC3">
						<td></td>
						<td>
							<font color=blue><b>a. If member is having any claims or no claim refund member prorata premium.</b></font> 
						</td>
					</logic:equal>
					 <logic:equal  name="frmPolicyDetails" property="logicType" value="SC4">
						<td></td>
						<td>
							<font color=blue><b>a. Claimed amount > Member Prorata premium then no refund<br>b. Claimed amount < Member prorata premium then refund member Prorata premium</b></font>  
						</td>
					</logic:equal>
     			</tr>
			    <tr>
			       <td class="formLabel" width="30%">Duration for credit note generation :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				       <html:select name="frmPolicyDetails" property="creditGeneration" disabled="<%= viewdtl| viewmode | blnENDViewmode %>" readonly="<%=viewdtl | viewmode | blnENDViewmode %>" onchange="onchangeCreditNote(this)" styleClass="selectBox selectBoxMedium"  >
							  <html:option value="">Select from list</html:option> 
							<html:optionsCollection name="creditNotePeriod" value="cacheId" label="cacheDesc"/>
						 </html:select>
			      </td>	
				      <logic:match name="frmPolicyDetails" property="creditGeneration" value="OTHD"> 
			       <td class="formLabel" width="30%">Credit Note (Others) :</td>
			      <td class="textLabel">
				   		<html:text name="frmPolicyDetails"  disabled="<%= viewdtl| viewmode | blnENDViewmode %>" readonly="<%=viewdtl| viewmode | blnENDViewmode %>" onkeyup="isNumericOnly(this);" styleId="creditGenerationOth" property="creditGenerationOth" styleClass="textBox textBoxLarge" />
			      </td>
			      </logic:match>
			      	 <logic:notMatch name="frmPolicyDetails" property="creditGeneration" value="OTHD">
			      	      <td class="formLabel" width="30%">Credit Note (Others) :</td>
			      		  <td class="textLabel">
				   		  <html:text name="frmPolicyDetails" readonly="true"  onkeyup="isNumericOnly(this);" styleId="creditGenerationOth" property="creditGenerationOth" styleClass="textBox textBoxLarge textBoxDisabled" />
			      		  </td>
			      	 </logic:notMatch>   
			    </tr>
      <tr>
      <td class="formLabel" width="30%">Maternity Age Band :<span class="mandatorySymbol">*</span></td>
			      <td class="textLabel">
				   Min. <html:text name="frmPolicyDetails" maxlength="3"  onkeyup="isNumeric(this);" property="maternityMinBand" onkeyup="isNumeric(this);" disabled="<%=  viewdtl|viewmode | blnENDViewmode %>" readonly="<%= viewdtl|viewmode | blnENDViewmode %>" styleClass="textBox textBoxPercentageSmallest" />
			     &nbsp;Max. <html:text name="frmPolicyDetails" maxlength="3" onkeyup="isNumeric(this);" property="maternityMaxBand" onkeyup="isNumeric(this);" disabled="<%= viewdtl| viewmode | blnENDViewmode %>" readonly="<%=viewdtl| viewmode | blnENDViewmode %>" styleClass="textBox textBoxPercentageSmallest" />
			     
			      </td>
			    </tr>
			    
		
        
      
      <tr><td>&nbsp;</td></tr>
      
    </table>
	</fieldset>
	<%-- added for koc 1278 PED --%>
	<%-- <fieldset>
    	<legend>Other Insurance Administrator Related Information</legend>
    		<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
    			<tr>
    				 <td width="20%" nowrap class="formLabel">Policy Beneficiary Name: </td>
    				<td width="21%" class="textLabelBold">
    					<html:text size="50" property="otherTPAInsuredname" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);blockEnterkey(event.srcElement);" maxlength="30" disabled="<%=viewmode%>"/>
    				</td> 
    				<td class="formLabel">Policy No.: </td>
    				<td class="formLabel">
    					<html:text property="otherTPAPolicyNr" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);blockEnterkey(event.srcElement);" maxlength="30" disabled="<%=viewmode%>"/>
    				</td>
    				
    			</tr>
    			<tr>
 					<td width="20%" class="formLabel">From Date:</td>
 					<td width="21%" class="textLabelBold">
		      			<html:text size="50" property="TPAStartDate" styleClass="textBox textDate" maxlength="10"/>
		      			<a name="CalendarObjectstartDate" id="CalendarObjectstartDate" href="#" onClick="javascript:show_calendar('CalendarObjectstartDate','frmPolicyDetails.TPAStartDate',document.frmPolicyDetails.TPAStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
		      				<img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="startDate" width="24" height="17" border="0" align="absmiddle" ></a>
		      		</td>		
 					<td class="formLabel" width="23%">To Date:</td>
 					<td class="formLabel" width="23%">
		      			<html:text property="TPAEndDate" styleClass="textBox textDate" maxlength="10"/>
		      			<a name="CalendarObjectendDate" id="CalendarObjectendDate" href="#" onClick="javascript:show_calendar('CalendarObjectendDate','frmPolicyDetails.TPAEndDate',document.frmPolicyDetails.TPAEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;">
		      				<img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="endDate" width="24" height="17" border="0" align="absmiddle" ></a>
		      		</td>
    			</tr>
    			<tr>
 			        <td width="21%" class="formLabel">Portability Approved: </td>
        			<td class="formLabel" width="33%">
        			    <html:select property="otherTPAPortApproved" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode | blnENDViewmode %>">
						<html:option value="">Select from list</html:option>
						<html:option value="Y">Yes</html:option>
						<html:option value="N">No</html:option>
						</html:select>
					</td>
    				<td class="formLabel" width="23%">Delay Condoned: </td>
    				<td class="textLabelBold">
        			    <html:select property="otherTPADelayCondoned" styleClass="selectBox selectBoxMedium" disabled="<%= viewmode | blnENDViewmode %>">
						<html:option value="">Select from list</html:option>
						<html:option value="Y">Yes</html:option>
						<html:option value="N">No</html:option>
						</html:select>
    				</td>
    			</tr>
			 	<tr>
    				<td width="20%" class="formLabel">SI Value: </td>
    				<td class="formLabel">
    					<html:text property="otherTPASIValue" styleClass="textBox textBoxMedium" onkeyup="ConvertToUpperCase(event.srcElement);blockEnterkey(event.srcElement);" maxlength="10" disabled="<%=viewmode%>"/>
    				</td>
			        <td>&nbsp;</td>
			        <td>&nbsp;</td>
     			</tr>
		    </table>
	</fieldset> --%>
	<%-- added for koc 1278 PED --%>

	<ttk:InsuredAddressInformation/>
	<ttk:BankAccountInformation/>
	<ttk:BeneficiaryInformation/>

	<%-- <fieldset>
		<legend>Clarification with Insurance Company</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="21%" class="formLabel">Clarification Status:</td>
		    <td class="formLabel" width="33%">
		     <html:select property="clarificationTypeID" styleClass="selectBox selectBoxMedium" disabled="<%=viewmode%>">
  	 		  <html:option value="">Select from list</html:option>
		      <html:optionsCollection name="clarificationstatus" label="cacheDesc" value="cacheId" />
			</html:select>
		    </td>
		    <td class="formLabel" width="23%">Clarified Date:</td>
		    <td class="formLabel" width="23%">
			    <html:text property="clarifiedDate" styleClass="textBox textDate" maxlength="10" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
		     <logic:match name="viewmode" value="false">
		        <a name="CalendarObjectClDate" id="CalendarObjectClDate" href="#" onClick="javascript:show_calendar('CalendarObjectClDate','frmPolicyDetails.clarifiedDate',document.frmPolicyDetails.clarifiedDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" alt="Calendar" name="incpDate" width="24" height="17" border="0" align="absmiddle"></a>
		    </logic:match>
		    </td>
		  </tr>
		  <tr>
		    <td class="formLabel">Remarks:</td>
		    <td class="formLabel" colspan="3"><html:textarea property="remarks" styleId="remarks" styleClass="textBox textAreaLong" disabled="<%=viewmode%>"/></td>
		  </tr>
		</table>
	</fieldset> --%>
	<!-- Test Nag -->	
	<fieldset>
		<legend>Policy Documents Upload</legend>
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="textLabelBold" colspan="2">
   				<a href="#" onClick="javascript:onUploadDocs()">Document Uploads </a>
   			</td>
	    </tr>
	    </table>
	</fieldset>

	<!-- S T A R T : Buttons -->
		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="100%" align="center">
		      <logic:match name="viewmode" value="false">
		      	<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUserSubmit()"><u>S</u>ave</button>&nbsp;
				<button type="button" name="Button" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onReset()"><u>R</u>eset</button>&nbsp;
		    </logic:match>
		    </td>
		</tr>
		</table>
	<!-- E N D : Buttons -->
</div>
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
<INPUT TYPE="hidden" NAME="condonedYN" VALUE="">
<html:hidden property="policyTypeID"/>
<html:hidden property="DMSRefID"/>
<html:hidden property="compareYN"/>
<html:hidden property="tenure"/>
<html:hidden property="policyStatusID"/>
<INPUT TYPE="hidden" NAME="switType" value='<bean:write name="frmPolicyList" property="switchType"/>'/>
<INPUT TYPE="hidden" NAME="proposalFormYN" VALUE="">
<html:hidden property="policySeqID"/>

<script type="text/javascript">
	getReferenceNo(document.forms[1].DMSRefID.value);
	//callFocusObj();
</script>
</logic:empty>

<logic:notEmpty name="frmPolicyDetails" property="display">
	<html:errors/>

	<script> TC_Disabled = true;</script>
</logic:notEmpty>

<logic:notEmpty name="frmPolicyDetails" property="frmChanged">
	<script> ClientReset=false;TC_PageDataChanged=true;</script>
</logic:notEmpty>
</html:form>