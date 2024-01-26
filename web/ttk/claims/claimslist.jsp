<%
/** @ (#) claimslist.jsp
 * Project     : TTK Healthcare Services
 * File        : claimslist.jsp
 * Author      : Chandrasekaran J
 * Company     : Span Systems Corporation
 * Date Created:
 *
 * @author 		 : Chandrasekaran J
 * Modified by   : Nagababu
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/claims/claimslist.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<script>
bAction=false;
var TC_Disabled = true;
var JS_SecondSubmit=false;
</script>
<%
pageContext.setAttribute("sAmount", Cache.getCacheObject("amount"));
pageContext.setAttribute("sSource", Cache.getCacheObject("source"));
pageContext.setAttribute("sStatus", Cache.getCacheObject("claimStatusList"));
pageContext.setAttribute("sTtkBranch", Cache.getCacheObject("officeInfo"));
pageContext.setAttribute("sPreAuthType", Cache.getCacheObject("preauthType"));
pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
pageContext.setAttribute("ProviderList",Cache.getCacheObject("ProviderList"));
pageContext.setAttribute("insuranceCompany", Cache.getCacheObject("insuranceCompany"));
pageContext.setAttribute("modeOfClaims", Cache.getCacheObject("modeOfClaims"));
pageContext.setAttribute("submissionCatagory", Cache.getCacheObject("submissionCatagory"));
pageContext.setAttribute("sAssignedTo", Cache.getCacheObject("assignedTo"));
pageContext.setAttribute("clmShortFallStatus",Cache.getCacheObject("preShortfallStatus"));
pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
pageContext.setAttribute("benefitTypes",Cache.getCacheObject("benefitTypes"));
pageContext.setAttribute("partnersList",Cache.getCacheObject("partnersList"));
pageContext.setAttribute("priorityClaimsVal",Cache.getCacheObject("priorityClaimsVal"));
pageContext.setAttribute("auditStatusList", Cache.getCacheObject("auditStatusList"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/ClaimsAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">List of Claims</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
	<tr class="searchContainerWithTab">
		<td nowrap>Claim No.:<br>
		    	<html:text property="sClaimNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
    	 </td>
    	 <td nowrap>Batch No.:<br>
            	<html:text property="sBatchNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
   		  </td>
		<td nowrap>Provider Name:<br>
		    <html:select property="sProviderName" name="frmClaimsList"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Any</html:option>
  				<html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
		    </html:select>
        </td>
        <%-- <td nowrap>Payer Name:<br>
		    <html:select property="sPayerName" name="frmClaimsList"  styleClass="selectBox textBoxLarge">
  				<html:options collection="insuranceCompany"  property="cacheId" labelProperty="cacheDesc"/>
		    </html:select>
        </td> --%>
        <td nowrap>Mode Of Claim:<br>
	            <html:select property="sModeOfClaim" styleClass="selectBox selectBoxMoreMedium" name="frmClaimsList">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="modeOfClaims" label="cacheDesc" value="cacheId" />
            	</html:select>
          </td>
          <td nowrap>Internal Remark Status:<br>
	            <html:select property="internalRemarkStatus" name="frmClaimsList" styleClass="selectBox selectBoxMoreMedium">
	               <html:option value="">Any</html:option>
	  				<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheId" />
	  				<html:option value="NON">None</html:option>
		    	</html:select>		    	       
        	</td>	
         
        <%-- <td nowrap>GlobalNet Member ID:<br>
		    	<html:text property="sGlobalNetMemID" name="frmClaimsList"  styleClass="textBox textBoxLarge" maxlength="250"/>
		    </td> --%>	   
      </tr>
        
      <tr class="searchContainerWithTab">
      		<td nowrap>Settlement No.:<br>
            	<html:text property="sSettlementNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
		 	<td nowrap>Policy No.:<br>
            	<html:text property="sPolicyNumber"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
        	<td nowrap>Partner Name:<br>
	            <html:select property="sPartnerName" styleClass="selectBox selectBoxMoreMedium" name="frmClaimsList">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="partnersList" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
     		 <td nowrap>Submission Catagory:<br>
	            <html:select property="sProcessType" styleClass="selectBox selectBoxMoreMedium" >
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheId"/>
            	</html:select>
          	</td>
		    <td nowrap>Assigned To:<br>
            	<html:select property="sAssignedTo" styleClass="selectBox selectBoxMoreMedium">
		        	<html:optionsCollection name="sAssignedTo" label="cacheDesc" value="cacheId" />
            	</html:select>
      		</td>  
	</tr>
	<tr class="searchContainerWithTab">
			<td nowrap>Invoice No.:<br>
		    	<html:text property="sInvoiceNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
			 </td>
			 <td nowrap>Al Koot ID:<br>
		    	<html:text property="sEnrollmentId"  styleClass="textBox textBoxLarge" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement)"/>
		    </td>	 
		    <td nowrap>Member Name:<br>
		    	<html:text property="sClaimantName"  styleClass="textBox textBoxLarge" maxlength="250"/>
		    </td>
		    <td nowrap>Claim Type:<br>
	            <html:select property="sClaimType" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
          	<td nowrap>If Others:<br>
            	<html:text property="sSpecifyName"  styleClass="textBox textBoxLarge" maxlength="60"/>
            </td>
    	</tr>
    	
    	<tr class="searchContainerWithTab">
    		<td nowrap>Event Reference Number:<br>
		    	<html:text property="eventReferenceNo"  styleClass="textBox textBoxLarge" maxlength="60"/> 
		    </td>
		 
        	    <td nowrap>Qatar ID:<br>
		    	<html:text property="sQatarId"  styleClass="textBox textBoxLarge" maxlength="60" />
		    </td>	
		 
		 <td nowrap>Al Koot Branch:<br>
	            <html:select property="sTtkBranch" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sTtkBranch" label="cacheDesc" value="cacheId" />
            	</html:select>
          </td>
    	   <td nowrap>Benefit Type:<br>
	            	<html:select property="sBenefitType" styleId="benefitType" styleClass="selectBox selectBoxMoreMedium" onchange="setMaternityMode();">
						<html:option value="">Select from list</html:option>
						<html:optionsCollection name="benefitTypes"label="cacheDesc" value="cacheId" />				
					</html:select>
          	</td>
          	 <td nowrap>Claim Req. Amount Range (In QAR):<br>
	            <html:select property="sAmountRange" styleClass="selectBox selectBoxSmall" onchange="javascript:onRangeChanged()" disabled="true">
		  	 		<html:option value="">Any</html:option>
		  	 		<html:option value="<="><=</html:option>
		  	 		<html:option value=">=">>=</html:option>
		  	 		<html:option value="=">=</html:option>
		  	 		<html:option value="<"><</html:option>
		  	 		<html:option value=">">></html:option>
            	</html:select>
            	 <logic:equal value="" name="frmClaimsList" property="sAmountRange">
            			<html:text property="sAmountRangeValue" name="frmClaimsList"  styleClass="textBox textBoxSmall" maxlength="10" disabled="true" style="background-color: #EEEEEE;" value=""/>
            	</logic:equal>         	
            	<logic:notEqual value="" name="frmClaimsList" property="sAmountRange">
            			<html:text property="sAmountRangeValue" name="frmClaimsList"  styleClass="textBox textBoxSmall" maxlength="10"/>
            	</logic:notEqual>
          	</td> 
        </tr>	 
		<tr class="searchContainerWithTab">
      			<td nowrap>Linked Pre-Approval No.:<br>
		    			<html:text property="sLinkedPreapprovalNo" name="frmClaimsList"  styleClass="textBox textBoxLarge" maxlength="60" />
		   		 </td>
      		
            	
            	<td nowrap>Common File No.:<br>
		    	<html:text property="sCommonFileNo"  styleClass="textBox textBoxLarge" maxlength="60"/>
		 </td>
            	
         
        		<td nowrap>Received Date:<br>
	            	<html:text property="sRecievedDate"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].sRecievedDate',document.forms[1].sRecievedDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            	</td>  
            <td nowrap> Risk Level:<br> <html:select property="riskLevel" name="frmClaimsList" styleClass="selectBox selectBoxMoreMedium">
					<html:option value="">Any</html:option>
	                <html:option value="LR">Low</html:option>
		  	 		<html:option value="IR">Intermediate</html:option>
		  	 		<html:option value="HR">High</html:option>
					</html:select>
				</td>
	        <td nowrap> CFD Investigation Status:<br> <html:select property="cfdInvestigationStatus" name="frmClaimsList" styleClass="selectBox selectBoxMoreMedium">
				    <html:option value="">Any</html:option>
	                <html:option value="II">Investigation In-progress </html:option>
	                <html:option value="CA">Cleared for Approval</html:option>
		  	 		<html:option value="PCA">Partially Cleared For Approval</html:option>
		  	 		<html:option value="FD">Fraud Detected</html:option>
					</html:select>
				</td>   	
        </tr>  	
        <tr>
        	 <td nowrap>Priority Claims:<br>
	            <html:select property="priorityClaims" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:onPriorityChanged()">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="priorityClaimsVal" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
          	<logic:equal value="PRVA" name="frmClaimsList" property="priorityClaims">
          	<td nowrap>Range : From - To (In Days)<br>
	            	<html:text  property="rangeFrom" styleClass="textBox textBoxSmall"  maxlength="3" onkeyup="isAmountValue(this)"/>&nbsp;&nbsp;&nbsp;
       				 <html:text  property="rangeTo" styleClass="textBox textBoxSmall"  maxlength="3" onkeyup="isAmountValue(this)"/>
          	</td>
          	</logic:equal>
          	
           <td nowrap>Status:<br>
	            <html:select property="sStatus" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:onStatusChanged()">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
         
         <logic:equal value="INP" name="frmClaimsList" property="sStatus">
        			 <td nowrap>In-Progress Status:<br>
         			<html:select property ="inProgressStatus" styleClass="selectBox selectBoxMoreMedium">
	           			<html:option value="">Any</html:option>
	           			<html:option  value="FRH">Fresh</html:option>
						<html:option  value="ENH">Resubmission</html:option>
						<html:option  value="RES">Shortfall Responded</html:option>
       				</html:select>
       	   		</td>	
			</logic:equal>
			<td nowrap>Audit Status:<br>
		    	<html:select property="auditStatus"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Any</html:option>
  				 <html:options collection="auditStatusList"  property="cacheId" labelProperty="cacheDesc"/> 
		    </html:select>
    	 	</td>
         
<%--          <logic:notEqual value="REQ" name="frmClaimsList" property="sStatus">
          	<td>
          		<a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
          	</td>   
          	</logic:notEqual> 	 --%>
         <%--  <logic:equal value="REQ" name="frmClaimsList" property="sStatus">
          	<td nowrap>ShortFall Status:<br>      	
	            <html:select property="clmShortFallStatus" name="frmClaimsList" styleClass="selectBox selectBoxMoreMedium">
	               <html:option value="">Any</html:option>
	  				<html:optionsCollection name="clmShortFallStatus" label="cacheDesc" value="cacheId" />
		    	</html:select>		    	
          	</td> 
         	</logic:equal>   --%>
        
				<td nowrap> Priority Corporate:<br> <html:select property="priorityCorporate" name="frmClaimsList" styleClass="selectBox selectBoxMoreMedium"
						onchange="javascript:onStatusChanged()">
						<html:option value="">Any</html:option>
	           			<html:option  value="OPC">Priority Coporate Claim</html:option>
						<html:option  value="NPC">Non Priority Coporate Claim</html:option>
					</html:select>
				</td>
			<td>
          		<a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
          	</td>   
        </tr>
       		
        
        
        
	</table>
	<!-- E N D : Search Box -->
	<!-- S T A R T : Grid -->
		<ttk:HtmlGrid name="tableData"/>
	<!-- E N D : Grid -->
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td></td>
	<td></td>	
	<td><!--button type="button" name="Button2" accesskey="a" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="addClaim()"><u>A</u>dd</button-->&nbsp;</td>
	</tr>
    	<tr>
    		<td width="27%"></td>
        	<td width="73%" align="right">
        	<%
        	if(TTKCommon.isDataFound(request,"tableData")){
	    	%>
        		<button type="button" name="Button" accesskey="c" class="buttonsCopyWB" onMouseout="this.className='buttonsCopyWB'" onMouseover="this.className='buttonsCopyWB buttonsCopyWBHover'" onClick="javascript:copyToWebBoard()"><u>C</u>opy to Web Board</button>&nbsp;
        	<%
        	 }
        	%>
        	</td>
      	</tr>
      	<ttk:PageLinks name="tableData"/>
	</table>
	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<html:hidden property="AssignAllFlagYN"/>
	<html:hidden property="AssignFlagYN"/>
	<input type="hidden" name="child" value="">
	
</html:form>
	<!-- E N D : Content/Form Area -->