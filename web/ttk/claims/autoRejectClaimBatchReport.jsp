<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,com.ttk.common.ClaimsWebBoardHelper"%>
<SCRIPT LANGUAGE="JavaScript">
var JS_SecondSubmit=false;
</SCRIPT>
	<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
    <script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
   <script type="text/javascript" src="/ttk/scripts/claims/ConfigurationList.js"></script>
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>  
	<script>
	
	function getProviderInvoiceNO(){
		document.forms[2].providerInvoiceNO.value='';
		 var claimSeqID= document.forms[2].previousClaimNO.value;
		 if(claimSeqID===null || claimSeqID==="" || claimSeqID.length<1)return;
     var  path="/asynchronAction.do?mode=getProviderInvoiceNO&claimSeqID="+claimSeqID;		                 

	 $.ajax({
	     url :path,
	     dataType:"text",
	     success : function(data) {
	    	 document.forms[2].providerInvoiceNO.value=data;
	     }
	 });

}//getAreas
   </script>
<style>

</style>
</head>
<body>
<%
	boolean viewmode=true;
	boolean bEnabled=false;
	boolean viewmode1=true;
	String strSubmissionType="";
	String ampm[] = {"AM","PM"};
	
	boolean blnAmmendmentFlow=false;
	if(TTKCommon.isAuthorized(request,"Edit"))
	{
		viewmode=false;
		viewmode1=false;
	}//end of if(TTKCommon.isAuthorized(request,"Edit"))

	pageContext.setAttribute("descriptionCode", Cache.getCacheObject("claimsDocUpload"));
	boolean network=false;
	boolean batchOverrideYN=false;
%>

<html:form action="/AutoRejectionClaimConfiguration.do" >
<!-- S T A R T : Page Title -->

		<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				    <td width="57%">Batch Reports</td>
					<td  align="right" class="webBoard">
					<%@ include file="/ttk/common/toolbar.jsp" %>
					<%--  <logic:notEmpty name="frmAutiRejectionDetail" property="batchSeqID">
						<%@ include file="/ttk/common/toolbar.jsp" %>
					</logic:notEmpty > --%>
			  		</td>
			  </tr>
		</table>	
	<!-- E N D : Page Title -->
		
	<div class="contentArea" id="contentArea">
	<html:errors/>
	<logic:notEmpty name="errorMsg" scope="request">
    <table align="center" class="errorContainer"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error" width="16" height="16" align="middle" >&nbsp;
          <bean:write name="errorMsg" scope="request" />
          </td>
      </tr>
    </table>
   </logic:notEmpty>
	
	<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:message name="updated" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty>
		<logic:notEmpty name="successMsg" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="middle">&nbsp;
						<bean:write name="successMsg" scope="request"/>
				  </td>
				</tr>
			</table>
		</logic:notEmpty> 	
		<logic:notEmpty name="flagValidate" scope="request">
				<table align="center" class="errorContainer" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/warning.gif" title="Wrning" alt="Wrning" width="16" height="16" align="absmiddle">
							<bean:message name="flagValidate" scope="request"/>
					</tr>
				</table>
			</logic:notEmpty>
    <!-- S T A R T : Form Fields -->
	
	<fieldset>
			<legend>Reports</legend>
			<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
			
			 
			
			 <tr>
			   <td nowrap>Batch No.:<br>
			   	      
			      <html:text name="frmAutiRejectionLogList" property="batchNO" styleClass="textBox textBoxMedium"/>
			      </td>	
			
			<td nowrap>Batch Reference No.:<br>
			    
			      <html:text name="frmAutiRejectionLogList" property="iBatchRefNO" styleClass="textBox textBoxMedium" />
			      </td>	
			</tr>
			<tr>
			<td></td>
			<td></td>
			</tr>
			<tr>
			<td></td>
			<td></td>
			</tr>
			<tr>
			<td></td>
			<td></td>
			</tr>
			<tr>
			<td></td>
			<td></td>
			</tr>
			<tr>
			<td></td>
			<td></td>
			</tr>
			<tr>
			<td></td>
			<td></td>
			</tr>
					  
			  </table>
		</fieldset>
		
		
		
		<table align="center" class="formContainer"  border="0" cellspacing="0" cellpadding="0">
			
			  <tr>
			  <td colspan="4" align="center">
			  
				
            <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onGenerateXL()"><u>G</u>enerateXL</button>&nbsp;
             <button type="button" name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onClosebatchReport()"><u>C</u>lose</button>&nbsp;
         
			
			  </td>
			  </tr>			  
			  </table>
		
		
 </div>
 <INPUT TYPE="hidden" NAME="rownum">
<INPUT TYPE="hidden" NAME="mode" VALUE="">
<input type="hidden" name="child" value="">
</html:form>

</body>
</html>
