<%@page import="org.apache.struts.action.DynaActionForm"%>
<%@page import="java.util.ArrayList"%>
<%
/**
 * @ (#) historylist.jsp August 13, 2015
 * Project 	     : ProjectX
 * File          : historylist.jsp
 * Author        : Nagababu K
 * Company       : RCS Technologies
 * Date Created  : August 13, 2015
 *
 * @author       :  Nagababu K
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */
%>

<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.security.Cache" %>


<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/preauth/historylist.js"></SCRIPT>
<script>
bAction=false;
var TC_Disabled = true;
</script>

<%
pageContext.setAttribute("benefitTypes",
		Cache.getCacheObject("prebenefitTypes"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/PreAuthHistoryAction.do">
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" title="Error" width="16" height="16" align="absmiddle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
</logic:notEmpty>
	<!-- S T A R T : Search Box -->

	<!-- E N D : Page Title -->
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<!-- S T A R T : Search Box -->
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
		<tr class="searchContainerWithTab">
		    <td nowrap>History Mode:<br>
	            <html:select property="historyMode" styleId="historyMode" styleClass="selectBox selectBoxMedium" name="frmHistoryList" onchange="changeHistoryMode()">
		  	 		<html:option value="PAT">PreApproval</html:option>
		  	 		<html:option value="CLM">Claim</html:option>
            	</html:select>
          	</td>
          	<td>Policy Details:<br>
				<html:select property="policyDet" styleId="policyDet" styleClass="selectBox selectBoxMedium" name="frmHistoryList" onchange="getPolicyNo();changePolicyDetails();">
					<html:option value="">Select from list</html:option>
					<html:optionsCollection  property="policyDetails" label="cacheDesc" value="cacheId"/> 
				</html:select>
			</td>
			<td nowrap>Policy No :<br>
       			<html:text styleClass="textBox textBoxLarge"  property="policyNo" styleId="policyNo" name="frmHistoryList" readonly="true"  />
          	</td>
				<td nowrap>Start Date:<br><html:text  style="height:15px"  property="policyStartDt" name="frmHistoryList" styleClass="textBox textDate" readonly="true"/>
			</td>	
          	<td nowrap>Sort By:<br>
	            <html:select property="sortBy" styleId="sortBy" styleClass="selectBox selectBoxMedium" name="frmHistoryList" onchange="changeHistoryMode()">
	            <logic:equal value="PAT" name="frmHistoryList" property="historyMode">
		  	 		<html:option value="pre_auth_number">PreApproval No.</html:option>
		  	 		<html:option value="auth_number">Authorization No.</html:option>
		  	 		<html:option value="claim_number">Linked To Claim No.</html:option>
		  	 	</logic:equal>
	  	 		<logic:equal value="CLM" name="frmHistoryList" property="historyMode">
		  	 		<html:option value="claim_number">Claim No.</html:option>
		  	 		<html:option value="settlement_number">Settlement No.</html:option>
		  	 		<html:option value="invoice_number">Invoice No.</html:option>
		  	 		<html:option value="pre_auth_number">Pre Approval No. Linked.</html:option>
				</logic:equal>		  	 	
		  	 		
		  	 		<html:option value="benifit_type">Benefit Type</html:option>
		  	 		<html:option value="hosp_name">Provider Name</html:option>
		  	 		<html:option value="admission_date">Admission Date/Time</html:option>
		  	 		<html:option value="pat_status ">Status</html:option>
            	</html:select>
          	</td>
          	<td nowrap>Benefit Type:<br>
				<html:select property="benefitType" styleId="bType" styleClass="selectBox selectBoxMedium" name="frmHistoryList" onchange="changeBenefitTypeMode()">
				    <html:option value="">Select from list</html:option>
					<html:optionsCollection name="benefitTypes" label="cacheDesc" value="cacheId" />
				</html:select>
			</td>
    	</tr>
    	<tr class="searchContainerWithTab">
    		<td></td> <td></td>
    		<td>
    			<logic:equal value="Old Policy" name="frmHistoryList" property="policyStatus">
          			<font color="red"><bean:write name="frmHistoryList" property="policyStatus"/></font>
          		</logic:equal>			
    	      	<logic:notEqual value="Old Policy" name="frmHistoryList" property="policyStatus">
	        	  	<b><bean:write name="frmHistoryList" property="policyStatus"/></b>
        	  	</logic:notEqual>
    		</td>
    		 <td nowrap>End Date:<br><html:text  style="height:15px" property="policyEndDt" name="frmHistoryList" styleClass="textBox textDate" readonly="true"/>
    		 </td>
    	 <td>
    	 		Ascending   <html:radio property="ascOrDesc" value="ASC" styleId="asc" onclick="changeBenefitTypeMode()"/>
            	Descending  <html:radio property="ascOrDesc" value="DESC" styleId="desc" onclick="changeBenefitTypeMode()"/>
    	 </td>
    	</tr>
    	</table>
    	
    <logic:equal value="PAT" name="frmHistoryList" property="historyMode">
	<fieldset>
	<legend>Pre-Approval History List</legend>
	<table align="center" class="formContainer" border="1" cellspacing="0" cellpadding="0"  frame="box" rules="all">
			<tr  class="gridHeader">
			<th align="center">PreApproval No.</th>
			<th align="center">Authorization No.</th>
			<th align="center" style="width:11%">ICD Codes</th>
			<th align="center">Linked To Claim No.</th>
			<th align="center">Benefit Type</th>
			<th align="center" style="width:11%">Provider Name</th>
			<th align="center" style="width:5%">Approved Amount</th>
		    <th align="center">Admission Date/Time</th>
		    <th align="center">Status</th>
		     <th align="center">Balance</th>
			</tr>
	<%! ArrayList<String[]> historylistcode= null;%>	
  <logic:notEmpty name="preauthHistoryList" scope="session">  
  <% 
     historylistcode = (ArrayList<String[]>)session.getAttribute("preauthHistoryList");
    int i=0;
    String[] historylistcodestringarray = new String[16];
  %>
      <c:forEach items="${sessionScope.preauthHistoryList}" var="historyList">                 
             <%
            if(i<historylistcode.size()-1)
             historylistcodestringarray =(String[])historylistcode.get(i);
             else
          	   break;
             i++;
             %>
       <tr>
		<td align="center">
		<a href="#" accesskey="g"  onClick="javascript:doViewHistory('${historyList[0]}');"><c:out value="${historyList[1]}"/></a> 
			<% 
				String EnhancedYN[] = ((String[])(pageContext.findAttribute("historyList")));
				if(EnhancedYN.length>1){
				if("Y".equals(EnhancedYN[15])){%>
				 <img title="Enhanced" src="/ttk/images/EnhancedPreauth.gif" style="size: 10px" width="18%" height="15%"> 
			 <%}} %>
		</td>
		<td align="center"><c:out value="${historyList[2]}"/></td>
		                     <% if(i<historylistcode.size()-1) %>
		<td align="center"><%=historylistcodestringarray[16]%></td>
		<td align="center"><c:out value="${historyList[13]}"/></td>
		<td align="center"><c:out value="${historyList[5]}"/></td>
		<td align="center"><c:out value="${historyList[3]}"/></td>
		<td align="center"><c:out value="${historyList[4]}"/></td>
		<td align="center"><c:out value="${historyList[6]}"/></td>
		<td align="center"><c:out value="${historyList[7]}"/></td>
		<td align="center"><c:out value="${historyList[17]}"/></td>
	  </tr>
	  <tr>
    </c:forEach>
  </logic:notEmpty>	
</table>
<table>
<tr>
<td><b>Total Amount:<bean:write name="frmHistoryList" property="sum"/></b></td>
<td align="right" colspan="475"> 
 <button type="submit" onClick="javascript:generateDownloadPATCLM();"  name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>D</u>ownload to Excel</button>
&nbsp;&nbsp;	
<a href="#" onClick="javascript:generatePreAuthHistoryReport()" style="color:blue; font:bold;">Pre-Auth History Report</a>
</td>
</tr>
</table>
</fieldset>
</logic:equal>

<logic:equal value="CLM" name="frmHistoryList" property="historyMode">

	<fieldset>
	<legend>Claim History List</legend>
	<table align="center" class="formContainer" border="1" cellspacing="0" frame="box" rules="all">
			<tr  class="gridHeader">
			<th align="center" style="width:11%;">Common File No.</th>
			<th align="center">Claim No.</th>
			<th align="center" style="width:10%;">Settlement No.</th>
			<th align="center" style="width:15%;">ICD Codes</th>
			<th align="center">Invoice No.</th>
			<th align="center" style="width:9%;">Pre Approval No. Linked</th>
			<th align="center" style="width:10%;">Benefit Type</th>
			<th align="center" style="width:9%;">Provider Name</th>
			<th align="center">Approved Amount</th>
		    <th align="center" style="width:13%;">Admission Date/Time</th>
		    <th align="center" style="width:8%;">Status</th>
		    <th align="center" style="width:5%;">Payment Status</th>
			</tr>
			
  <logic:notEmpty name="preauthHistoryList" scope="session">  
     <% 
     historylistcode = (ArrayList<String[]>)session.getAttribute("preauthHistoryList");
    int i=0;
    String[] historylistcodestringarray = new String[16];
  %>
      <c:forEach items="${sessionScope.preauthHistoryList}" var="historyList">                 
       <%
 
       if(i<historylistcode.size()-1)
       historylistcodestringarray =(String[])historylistcode.get(i);
       else
    	   break;
       i++;
       %>
       <tr>
       <td align="center"><c:out value="${historyList[9]}"/></td>
		<td align="center">
		<a href="#" accesskey="g"  onClick="javascript:doViewHistory('${historyList[0]}');">
		
		<% 
		String fraudYN[] = ((String[])(pageContext.findAttribute("historyList")));
		if(fraudYN.length>1){
		if("N".equals(fraudYN[11]) && "Y".equals(fraudYN[10])){
			if(fraudYN[12].equals("ALKT")){%>
			<c:out value="${historyList[1]}"/><img title="Alkoot Clearification" src="/ttk/images/suspectedcheckedfraud.gif" width="13" height="13">	
	    <% 	}else if(fraudYN[12].equals("PROV")){%>
	    	<c:out value="${historyList[1]}"/><img title="Provider Clearification" src="/ttk/images/suspectedcheckedfraud.gif" width="13" height="13">
	   <% }else if(fraudYN[12].equals("SUSP")){%>
		   <c:out value="${historyList[1]}"/><img title="Suspected Claim" src="/ttk/images/suspectedcheckedfraud.gif" width="13" height="13">
	 <%  }

		%>	
		
		<%} 
		
		else if("Y".equals(fraudYN[11]) && "Y".equals(fraudYN[10]) ){
			if(fraudYN[12].equals("ALKT")){%>
				<c:out value="${historyList[1]}"/><img title="Alkoot Clearification-Verified" src="/ttk/images/verifiedclaimsearchAndhistoryimg.gif" width="13" height="13">
		<%	}else if(fraudYN[12].equals("PROV")){%>
		<c:out value="${historyList[1]}"/><img title="Provider Clearification-Verified" src="/ttk/images/verifiedclaimsearchAndhistoryimg.gif" width="13" height="13">	
	<%	}else if(fraudYN[12].equals("SUSP")){%>
				<c:out value="${historyList[1]}"/><img title="Suspected Claim-Verified" src="/ttk/images/verifiedclaimsearchAndhistoryimg.gif" width="13" height="13">	
		
	<%}
			
			%>
			
	<% 	}
		
		else{%>
			<c:out value="${historyList[1]}"/>
		<% } }%>
		</a>
		</td>
		<td align="center"><c:out value="${historyList[2]}"/></td>
		                   <%if(i<historylistcode.size()-1)%>
		<td align="center"><%=historylistcodestringarray[16]%></td>
		<c:set var="invNO" value="${historyList[14]}"/>
		<%
		String invNOs	=	(String)pageContext.getAttribute("invNO")==null?"":(String)pageContext.getAttribute("invNO");
		if(invNOs!="" && invNOs!=null){
			invNOs			=	invNOs.replace(",#", ", #");
			invNOs			=	invNOs.replace(",", ", ");
		}
		%>
		<td align="center"><%=invNOs %></td>
		<td align="center"><c:out value="${historyList[13]}"/></td>
		<td align="center"><c:out value="${historyList[5]}"/></td>
		<td align="center"><c:out value="${historyList[3]}"/></td>
		<td align="center"><c:out value="${historyList[4]}"/></td>
		<td align="center"><c:out value="${historyList[6]}"/></td>
		<td align="center"><c:out value="${historyList[7]}"/></td>
		<td align="center"><c:out value="${historyList[8]}"/></td>

	  </tr>
    </c:forEach>
  </logic:notEmpty>	
</table>
<table>
<tr>
<td><b>Total Amount:<bean:write name="frmHistoryList" property="sum"/></b></td>
<td align="right" colspan="475"> 
 <button type="submit" onClick="javascript:generateDownloadPATCLM();"  name="Button1" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" ><u>D</u>ownload to Excel</button>
&nbsp;&nbsp;
<a href="#" onClick="javascript:generateClaimHistoryReport()" style="color:blue; font:bold;">Claim History Report</a>
</td>
</tr>
</table>
</fieldset>
</logic:equal>
	<!-- S T A R T : Buttons and Page Counter -->
	</div>
	<!-- E N D : Buttons and Page Counter -->
	
	<input type="hidden" name="authSeqID"/>
	<input type="hidden" name="mode">
	<input type="hidden" name="reforward"/>
	<html:hidden property="memberSeqID" styleId="memberSeqID"/>
	<html:hidden property="memberId" styleId="memberId"/>
	<html:hidden property="PolicySeqId" styleId="PolicySeqId"/>
	<html:hidden property="policyNumber" styleId="policyNumber"/>
	<html:hidden property="policyNoLabel" styleId="policyNoLabel"/>
</html:form>
<!-- E N D : Content/Form Area -->
