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
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/fraudcase/counterfraudlist.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<script>
bAction=false;
var TC_Disabled = true;
var JS_SecondSubmit=false;
</script>
<%
pageContext.setAttribute("ProviderList",Cache.getCacheObject("ProviderList"));
pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
pageContext.setAttribute("partnersList",Cache.getCacheObject("partnersList"));
pageContext.setAttribute("submissionCatagory", Cache.getCacheObject("submissionCatagory"));
pageContext.setAttribute("benefitTypes",Cache.getCacheObject("benefitTypes"));
pageContext.setAttribute("sStatus", Cache.getCacheObject("claimStatusList"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CounterFraudSearchAction.do">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">Suspected Fraud :Internal Remarks Status Details</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
	<tr class="searchContainerWithTab">
		<% if(request.getSession().getAttribute("claimorpreauthswitchtype").equals("CLM")) {%>
		<td nowrap>Claim No.:<br>
		    	<html:text property="claimNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
    	 </td>
    	 <%}%>
    	 <%if(request.getSession().getAttribute("claimorpreauthswitchtype").equals("PAT")) {%>
			<td nowrap>Pre-Approval No.:<br>
		    	<html:text property="preapprovalNo"  styleClass="textBox textBoxLarge" maxlength="60"/>
			 </td>
		<%} %>
		<%if(request.getSession().getAttribute("claimorpreauthswitchtype").equals("CLM")){ %>
    	 <td nowrap>Batch No.:<br>
            	<html:text property="batchNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
   		  </td>
   		  <%} %>
   		  <%if(request.getSession().getAttribute("claimorpreauthswitchtype").equals("PAT")){ %>	
		 <td nowrap>Authorization No.:<br>
		    	<html:text property="authorizationNo"  styleClass="textBox textBoxLarge" maxlength="60"/>
			 </td>
        <%} %>	
		<td nowrap>Provider Name:<br>
		    <html:select property="providerNamesId" name="frmFraudCase"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Any</html:option>
  				<html:options collection="ProviderList"  property="cacheId" labelProperty="cacheDesc"/>
		    </html:select>
        </td>
        <%if(request.getSession().getAttribute("claimorpreauthswitchtype").equals("CLM")){ %>
        <td nowrap>Claim Type:<br>
	            <html:select property="claimType" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="claimType" label="cacheDesc" value="cacheId" />
            	</html:select>
       </td>
      <%} %>
          <td nowrap>Internal Remark Status:<br>
	            <html:select property="internalRemarkStatus" name="frmFraudCase" styleClass="selectBox selectBoxMoreMedium">
	               <html:option value="">Any</html:option>
	  			   <html:option value="SUSP">Suspected Fraud</html:option>
	  				
		    	</html:select>		    	       
        	</td>	
         
        <%-- <td nowrap>GlobalNet Member ID:<br>
		    	<html:text property="sGlobalNetMemID" name="frmClaimsList"  styleClass="textBox textBoxLarge" maxlength="250"/>
		    </td> --%>	   
      </tr>
        
      <tr class="searchContainerWithTab">
      <%if(request.getSession().getAttribute("claimorpreauthswitchtype").equals("CLM")){ %>
      		<td nowrap>Settlement No.:<br>
            	<html:text property="settlementNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
     <%} %>
		 	<td nowrap>Policy No.:<br>
            	<html:text property="policyNumber"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
        	<td nowrap>Partner Name:<br>
	            <html:select property="partnerName" styleClass="selectBox selectBoxMoreMedium" name="frmFraudCase">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="partnersList" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td>
     		 <td nowrap>Submission Type:<br>
	            <html:select property="submissionType" styleClass="selectBox selectBoxMoreMedium" >
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheId"/>
            	</html:select>
          	</td>
		    <td nowrap>Risk Level:<br>
            	<html:select property="riskLevel" styleClass="selectBox selectBoxMoreMedium">
		        	<html:option value="">Any</html:option>
		        	<html:option value="HR">High</html:option>
		        	<html:option value="IR">Intermediate</html:option>
		        	<html:option value="LR">Low</html:option>
            	</html:select>
      		</td>  
	</tr>
	<tr class="searchContainerWithTab">
	    <td nowrap>Switch To Preauth/Claim:<br>
		    	<html:select property="claimorpreauthswitchtype" styleId="benefitType" styleClass="selectBox selectBoxMoreMedium" onchange="changeclaimtopreauth();">
				<html:option value="CLM">Claims</html:option>		
				<html:option value="PAT">Preauth</html:option>					
					</html:select>
			 </td>
			 <td nowrap>Al Koot ID:<br>
		    	<html:text property="enrollmentId"  styleClass="textBox textBoxLarge" maxlength="60" onkeyup="ConvertToUpperCase(event.srcElement)"/>
		    </td>	 
		    <td nowrap>Member Name:<br>
		    	<html:text property="claimantName"  styleClass="textBox textBoxLarge" maxlength="250"/>
		    </td>
		    
          	<td nowrap>Benefit Type:<br>
	            	<html:select property="benefitType" styleId="benefitType" styleClass="selectBox selectBoxMoreMedium" onchange="setMaternityMode();">
						<html:option value="">Select from list</html:option>
						<html:optionsCollection name="benefitTypes"label="cacheDesc" value="cacheId" />				
					</html:select>
          	</td>
    	</tr>
    	
    	<tr class="searchContainerWithTab">
    	 <%if(request.getSession().getAttribute("claimorpreauthswitchtype").equals("CLM")){ %>  	
		 <td nowrap>Invoice No.:<br>
		    	<html:text property="invoiceNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
			 </td>
		 <%}%>
    	 <td nowrap>Policy Type:<br>
	            <html:select property="policyType" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:onStatusChanged()">
		  	 		<html:option value="">Any</html:option>
		  	 		<html:option value="IND">Individual</html:option>
		  	 		<html:option value="COR">Corporate</html:option>
            	</html:select>
          	</td>  
         <td nowrap>Claim/Preauth Status:<br>
	            <html:select property="status" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:onStatusChanged()">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheId" />
            	</html:select>
          	</td> 
          <td nowrap>CFD Investigation Status:<br>
	            <html:select property="cfdInvestigationStatus" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:onStatusChanged()">
		  	 		<html:option value="">Any</html:option>
	                <html:option value="II">Investigation In-progress </html:option>
	                <html:option value="CA">Cleared for Approval</html:option>
		  	 		<html:option value="PCA">Partially Cleared For Approval</html:option>
		  	 		<html:option value="FD">Fraud Detected</html:option>
            	</html:select>
          	</td>  	
        <td>
          		<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
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
</html:form>
	<!-- E N D : Content/Form Area -->