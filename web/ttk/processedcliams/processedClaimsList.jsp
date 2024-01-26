<%
/** @ (#) processedClaimsList.jsp
 * Project     : TTK Healthcare Services
 * File        : processedClaimsList.jsp
 * Author      : Deepthi Meesala
 * Company     : RCS
 * Date Created: 22-10-2020
 *
 * @author 		 : 
 * Modified by   : 
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
<SCRIPT type="text/javascript"  SRC="/ttk/scripts/processedcliams/processedClaimsList.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>

<%

pageContext.setAttribute("ProviderList",Cache.getCacheObject("ProviderList"));
pageContext.setAttribute("modeOfClaims", Cache.getCacheObject("modeOfClaims"));
pageContext.setAttribute("internalRemarkStatus",Cache.getCacheObject("internalRemarkStatus"));
pageContext.setAttribute("partnersList",Cache.getCacheObject("partnersList"));
pageContext.setAttribute("submissionCatagory", Cache.getCacheObject("submissionCatagory"));
pageContext.setAttribute("claimType", Cache.getCacheObject("claimType"));
pageContext.setAttribute("sStatus", Cache.getCacheObject("claimStatusList"));
pageContext.setAttribute("sTtkBranch", Cache.getCacheObject("officeInfo"));
pageContext.setAttribute("benefitTypes",Cache.getCacheObject("benefitTypes"));
%>



<html:form action="/ProcessedClaimsSearchAction.do">


<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">List of Claims</td>
			<td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
		</tr>
	</table>

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
		    <html:select property="sProviderName" name="frmProcessedClaimsList"  styleClass="selectBox selectBoxMoreMedium">
		    	 <html:option value="">Any</html:option>
  				<html:options collection="ProviderList"  property="cacheDesc" labelProperty="cacheDesc"/>
		    </html:select>
        </td>
       
        <td nowrap>Mode Of Claim:<br>
	            <html:select property="sModeOfClaim" styleClass="selectBox selectBoxMoreMedium" name="frmProcessedClaimsList">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="modeOfClaims" label="cacheDesc" value="cacheDesc" />
            	</html:select>
          </td>
          <td nowrap>Internal Remark Status:<br>
	            <html:select property="internalRemarkStatus" name="frmProcessedClaimsList" styleClass="selectBox selectBoxMoreMedium">
	               <html:option value="">Any</html:option>
	  				<html:optionsCollection name="internalRemarkStatus" label="cacheDesc" value="cacheDesc" />
	  				<html:option value="NON">None</html:option>
		    	</html:select>		    	       
        	</td>	
          
      </tr>
       <tr class="searchContainerWithTab">
      		<td nowrap>Settlement No.:<br>
            	<html:text property="sSettlementNO"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
		 	<td nowrap>Policy No.:<br>
            	<html:text property="sPolicyNumber"  styleClass="textBox textBoxLarge" maxlength="60"/>
        	</td>
        	<td nowrap>Partner Name:<br>
	            <html:select property="sPartnerName" styleClass="selectBox selectBoxMoreMedium" name="frmProcessedClaimsList">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="partnersList" label="cacheDesc" value="cacheDesc" />
            	</html:select>
          	</td>
     		 <td nowrap>Submission Category:<br>
	            <html:select property="sProcessType" styleClass="selectBox selectBoxMoreMedium" >
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="submissionCatagory" label="cacheDesc" value="cacheDesc"/>
            	</html:select>
          	</td>
          	 <td nowrap>Received Date:<br>
	            	<html:text property="sRecievedDate"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].sRecievedDate',document.forms[1].sRecievedDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
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
		        	<html:optionsCollection name="claimType" label="cacheDesc" value="cacheDesc" />
            	</html:select>
          	</td>
          <%-- 	<td nowrap>If Others:<br>
            	<html:text property="sSpecifyName"  styleClass="textBox textBoxLarge" maxlength="60"/>
            </td> --%>
    	</tr>
      
       <tr class="searchContainerWithTab">
       <td nowrap>Status:<br>
	            <html:select property="sStatus" name="frmProcessedClaimsList" styleClass="selectBox selectBoxMoreMedium" onchange="javascript:onStatusChanged()">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sStatus" label="cacheDesc" value="cacheDesc" />
            	</html:select>
          	</td>
          	 <td nowrap>Qatar ID:<br>
		    	<html:text property="sQatarId"  styleClass="textBox textBoxLarge" maxlength="60" />
		    </td>	
		     <td nowrap>Al Koot Branch:<br>
	            <html:select property="sTtkBranch" styleClass="selectBox selectBoxMoreMedium">
		  	 		<html:option value="">Any</html:option>
		        	<html:optionsCollection name="sTtkBranch" label="cacheDesc" value="cacheDesc" />
            	</html:select>
          </td>
            <td nowrap>Benefit Type:<br>
	            	<html:select property="sBenefitType" styleId="benefitType" styleClass="selectBox selectBoxMoreMedium" onchange="setMaternityMode();">
						<html:option value="">Select from list</html:option>
						<html:optionsCollection name="benefitTypes"label="cacheDesc" value="cacheDesc" />				
					</html:select>
          	</td>
      
            <td>
          		<a href="#" accesskey="s" onClick="javascript:onSearch(this)"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
          	</td> 
      
      
		</tr>

</table>


<ttk:HtmlGrid name="tableData"/>
<%-- <ttk:PageLinks name="tableData"/> --%>


<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
     <tr>
    		<td width="27%"></td>
        	<td width="73%" align="right">
        <%-- 
        	<%
        	if(TTKCommon.isDataFound(request,"tableData")){
	    	%>
        		<button type="button" name="Button" accesskey="c" class="buttonsCopyWB" onMouseout="this.className='buttonsCopyWB'" onMouseover="this.className='buttonsCopyWB buttonsCopyWBHover'" onClick="javascript:copyToWebBoard()"><u>C</u>opy to Web Board</button>&nbsp;
        	<%
        	 }
        	%>
        	 --%>
        	</td>
      	</tr>
     
     
     
      <tr>
        <ttk:PageLinks name="tableData"/>
      </tr>
      
      <tr></tr> <tr></tr>
      
    </table>




</div>

	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<html:hidden property="lngClaimSeqID" styleId="seqId" name="frmProcessedClaimsList" /> 

</html:form>



