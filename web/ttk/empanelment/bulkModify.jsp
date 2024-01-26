<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/empanelment/tariffUpload.js"></script>
<%
pageContext.setAttribute("alPayerCode", Cache.getCacheObject("payerCode"));
pageContext.setAttribute("alProviderCode", Cache.getCacheObject("providerCode"));
pageContext.setAttribute("alNetworkType", Cache.getCacheObject("networkType"));
pageContext.setAttribute("alCorpCode", Cache.getCacheObject("corpCode"));
pageContext.setAttribute("groupProviderList",Cache.getCacheObject("groupProviderList"));
String newFileName	=	(String)request.getAttribute("newFileName")==null?"":(String)request.getAttribute("newFileName");
String switchToVal= (String) session.getAttribute("switchToVal");
%>
<!-- S T A R T : Content/Form Area -->


<html:form action="/TariffUploadsEmpanelmentAction.do" method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td><%=switchToVal%></td>
		    <%-- <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td> --%>
		  </tr>
	</table>
	<!-- E N D : Page Title -->
	<div class="contentArea" id="contentArea">
		
	<html:errors/>
	<logic:notEmpty name="errorMsg" scope="request">
				<table align="center" class="errorContainer" border="0"
					cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error"
							width="16" height="16" align="absmiddle">&nbsp; <bean:write
								name="errorMsg" scope="request" /></td>
					</tr>
				</table>
</logic:notEmpty>
	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:write name="updated" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
	<!-- S T A R T : SELCTIONS -->
	<fieldset><legend><%=switchToVal%></legend>
	<table class="formContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right" nowrap>Type of Tariff <span class="mandatorySymbol">*</span> :</td>
        <td align="left">
			<html:select property="tariffType" name="frmSearchTariffItem" styleClass="selectBox selectBoxMedium" onchange="onChangeTariffType(this)" readonly="true">
				<html:option value="HOSP">Provider</html:option>
			</html:select>
		</td>
		<td> &nbsp;</td>
		</tr>
		<tr>
		<td align="right" nowrap> Provider licence ID :</td>
		 <td align="left">
		 	<html:text property="providerCode" styleId="providerCode" styleClass="textBox textBoxMedium" readonly="true" value="<%= (String)request.getSession().getAttribute("AuthLicenseNo") %>"/>
			<!-- <a href="#" onClick="openListIntX('providerCode','PROVIDERSCODE')" style="display: inline;" id="aproviderCode"><img src="/ttk/images/EditIcon.gif" alt="Select Providers" width="16" height="16" border="0" align="absmiddle"></a> -->
		</td>
		</tr>
		<tr>
        <td align="right" nowrap>Insurer ID <span class="mandatorySymbol">*</span>:</td>
         <td align="left">
		 	<html:text property="payerCode" styleId="payerCode" value="ALK01" styleClass="textBox textBoxMedium" readonly="true"/>
			<a href="#" onClick="openListIntX('payerCode','PAYERSCODE')" style="display: inline;" id="apayerCode"><img src="/ttk/images/EditIcon.gif" title="Select Payers" alt="Select Payers" width="16" height="16" border="0" align="absmiddle"></a>
		</td>
		</tr>
		<tr>
		<td align="right" nowrap>Network Type <span class="mandatorySymbol">*</span>: </td>
		 <td align="left">
		 	<html:text property="networkType" styleId="networkType" styleClass="textBox textBoxMedium" readonly="true" />
			<a href="#" onClick="openListTariffIntX('networkType','NETWORKSGEN','providerCode')" style="display: inline;" id="anetworkType"><img src="/ttk/images/EditIcon.gif" title="Select Networks" alt="Select Networks" width="16" height="16" border="0" align="absmiddle"></a>
		</td>
		</tr>

		<tr>
		<td align="right" >Select Excel File to Upload Tariff:<span class="mandatorySymbol">*</span></td>
		 <td align="left">
			<html:file property="file" styleId="file"/>
		</td>
      </tr>
      <tr>
	      <td colspan="2" align="center"> 
	      	<font color="#04B4AE"> <strong>Please Select only .xls <!-- or .xlsx  -->file to upload.</strong></font>
	      </td>
      </tr>
      </table>
     </fieldset> 
      <br>
      <table class="formContainer" border="0" cellspacing="0" cellpadding="0">
     
      <tr>
       	<td  colspan="2" align="center">
	<button type="button" name="uploadButton" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUploadBulkModifyTariff()"><u>U</u>pload Tariff</button>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<%if(switchToVal!=null&&switchToVal.equals("ACTIVITY TARIFF")){%>
	<button type="button" name="backButton" accesskey="b" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBackTariff()"><u>B</u>ack</button>
	<%}
	else{ %>
	<button type="button" name="backButton" accesskey="b" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBackPharmacyTariff()"><u>B</u>ack</button>
      <%} %> 
       	</td>
       </tr>
    </table>
    
    <div>
    <%if(switchToVal!=null&& switchToVal.equals("PHARMACY/CONSUMABLE TARIFF")) {%>
    <p> Click on <a href="#" onclick="javascript:bulkpharmacymodifyFormat()">Download</a> Activity mapping edit upload format.</p>
    <%}else {%>
    <p> Click on <a href="#" onclick="javascript:bulktariffmodifyFormat()">Download</a> Activity mapping edit upload format.</p>
    <%} %>
<%--     <p> Click on <a href="#" onclick="javascript:showErrorLog(<bean:write name="newFileName" scope="request"/>)">Error File</a> </p>
 --%>
     <p> <a href="/ReportsAction.do?mode=doViewCommonForAll&module=bulkModifyLogs&fileName=<%= newFileName %>">
     <%=newFileName %>
     </a>
     </p>
    </div>
    
    
    
    <!-- E N D : SELCTIONS -->
	<!-- E N D : Buttons -->
	<!-- E N D : Form Fields -->
   </div>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<input type="hidden" name="child" value="">
	<input type="hidden" name="tab" value="">
	<script>
if(document.forms[1].tariffType.value!="")	
	onChangeTariffType(document.forms[1].tariffType);
</script>
 </html:form>