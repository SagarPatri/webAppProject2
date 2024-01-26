<%
/**
 * @ (#) tariffUpload.jsp 1st April 2015
 * Project      : TTK HealthCare Services
 * File         : tariffUpload.jsp
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 1st April 2015
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script> 
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/empanelment/tariffUpload.js"></script>
  <script >
    $(document).ready(function(){
    	startTariffUpload('ACT');
    });  	
    </script>
    <SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>
<%
pageContext.setAttribute("alPayerCode", Cache.getCacheObject("payerCode"));
pageContext.setAttribute("alProviderCode", Cache.getCacheObject("providerCode"));
pageContext.setAttribute("alNetworkType", Cache.getCacheObject("networkType"));
pageContext.setAttribute("alCorpCode", Cache.getCacheObject("corpCode"));
String newFileName	=	(String)request.getAttribute("newFileName")==null?"":(String)request.getAttribute("newFileName");
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/TariffUploadsEmpanelmentAction.do" method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td>Tariff Upload</td>
		    <%-- <td align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td> --%>
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
					<bean:write name="updated" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
	<!-- S T A R T : SELCTIONS -->
	<fieldset><legend>Tariff Upload</legend>
	<table class="formContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right" nowrap>Type of Tariff <span class="mandatorySymbol">*</span> :</td>
        <td align="left">
			<html:select property="tariffType" name="frmTariffUploadItemEmpanelment" styleClass="selectBox selectBoxMedium" onchange="onChangeTariffType(this)" readonly="true">
				<html:option value="HOSP">Provider</html:option>
				<%if(request.getSession().getAttribute("indOrGroup").equals("GRP")){ %>
				<html:option value="GROP">Group</html:option>
				<%} %>
			</html:select>
		</td>
		<td> &nbsp;</td>
		</tr>
		<tr id="group" style="display: none;">
	      	
      		<td align="right" nowrap>Group <span class="mandatorySymbol">*</span>:</td>
       		<td align="left" id="groupProvider">
			<html:text property="providerGroupName" styleId="providerGroupName"  styleClass="textBox textBoxMedium" readonly="true"/>	        		
   			</td>
	    </tr>
	    <tr id="networkcategory" style="display: none;">
		<td align="right" nowrap>Network Category <span class="mandatorySymbol">*</span>: </td>
		 <td align="left">
		 	<html:text property="networkCategory" styleId="networkCategoryId" styleClass="textBox textBoxMedium" />
			<a href="#" onClick="openListTariffIntX('networkCategoryId','GROUPNETWORKCAT','networkCategoryId')" style="display: inline;" id="anetworkType"><img src="/ttk/images/EditIcon.gif" title="Select Networks" alt="Select Networks" width="16" height="16" border="0" align="absmiddle"></a>
		</td>
		</tr>
		<tr id="provider" style="display: none;">
		<td align="right" nowrap>Provider <span class="mandatorySymbol">*</span>: </td>
		 <td align="left">
		 	<html:text property="providerForNetwork" styleId="providerForNetworkId" styleClass="textBox textBoxMedium" readonly="true" />
			<a href="#" onClick="openListIntX('providerForNetworkId','GROUPPROVIDERLIST')" style="display: inline;" id="anetworkType"><img src="/ttk/images/EditIcon.gif" title="Select Networks" alt="Select Networks" width="16" height="16" border="0" align="absmiddle"></a>
		</td>
		</tr>
		<tr id="providerlincenceId" style="display: inline;">
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
		<td align="right" nowrap>Discount at : </td>
		 <td align="left">
		 	<html:select property="discAt" name="frmTariffUploadItemEmpanelment" styleClass="selectBox selectBoxMedium" onchange="onChangeIndOrGrp(this)">
				<html:option value="PRL">--Provider Level--</html:option>
				<html:option value="SRL">--Service level--</html:option>
			</html:select>
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
	<button type="button"  name="uploadButton" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUploadTariff()"><u>U</u>pload Tariff</button>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<button type="button" name="backButton" accesskey="b" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBackTariff()"><u>B</u>ack</button>
       	</td>
       </tr>
    </table>
    
    <div>
    <p> Click on <a href="#" onclick="javascript:tariffUploadFormat()">Download</a> for tariff upload format.</p>
    
<%--     <p> Click on <a href="#" onclick="javascript:showErrorLog(<bean:write name="newFileName" scope="request"/>)">Error File</a> </p>
 --%>
     <p> <a href="/ReportsAction.do?mode=doViewCommonForAll&module=tariffUploadLogs&fileName=<%= newFileName %>">
     <%=newFileName %>
     </a>
     </p>
    </div>
    
    <!-- <div>
	<br>
		<a href="#" onclick="javascript:onModifyTariff()">Click here to Edit/Modify</a>		
		&nbsp;&nbsp;&nbsp;&nbsp;
    </div> -->
    
    
    
    
    <!-- E N D : SELCTIONS -->
	<!-- E N D : Buttons -->
	<!-- E N D : Form Fields -->
   
   
   <div align="center">
	<logic:equal value="Y"  property="sussessYN" name="frmTariffUploadItemEmpanelment">
	 
	   <div style="color: black; font-size: 12px;">
			<table border="1" cellpadding="0" cellspacing="0" width="49%">
				<tr bgcolor="#677BA8" style="height: 20px;">
						<th colspan="2" style="color:white;" >Summary Of Your Latest Data Upload</th>
				</tr>
			<%if("GROP".equals(request.getSession().getAttribute("tariffType"))){ %>
			
			<%	if(Integer.valueOf(String.valueOf(request.getAttribute("totalNoOfRowsFailed")))==0){ %>
					
			<tr style="height: 20px;">
			    	<td class="formLabel" width="39%"  align="left" valign="middle">&nbsp;&nbsp;Batch Reference No : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmTariffUploadItemEmpanelment" property="batchRefNo"/></b> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left"valign="middle">&nbsp;&nbsp;Status : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp; Success </td>
			    </tr>
			
			<%	}else{
			%>
			   <tr style="height: 20px;">
			    	<td class="formLabel" width="39%"  align="left" valign="middle">&nbsp;&nbsp;Batch Reference No : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmTariffUploadItemEmpanelment" property="batchRefNo"/></b> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left"valign="middle">&nbsp;&nbsp;Status : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp; Failed (Check Error Log) </td>
			    </tr>
			<%} %>   
			
			<%}else{ %>
			
			<tr style="height: 20px;">
			    	<td class="formLabel" width="39%"  align="left" valign="middle">&nbsp;&nbsp;Batch Reference No : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<b><bean:write name="frmTariffUploadItemEmpanelment" property="batchRefNo"/></b> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left"valign="middle">&nbsp;&nbsp;Total No. of Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmTariffUploadItemEmpanelment" property="totalNoOfRows"/> </td>
			    </tr><tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Success Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmTariffUploadItemEmpanelment" property="totalNoOfRowsPassed"/> </td>
			    </tr> <tr style="height: 20px;">
			    	<td class="formLabel" width="39%" align="left" valign="middle">&nbsp;&nbsp;Total No. of Failed Records : </td> 
			    	<td width="61%" align="center" valign="middle">&nbsp;&nbsp;<bean:write name="frmTariffUploadItemEmpanelment" property="totalNoOfRowsFailed"/> </td>
			    </tr></tr> 
			    <%} %> 
   </table>
	</div>
	
    </logic:equal>
    </div>
    
 </div>  
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<input type="hidden" name="child" value="">
	<input type="hidden" name="tab" value="">
	 <input type="hidden" name="batchNo" value=<bean:write name="frmTariffUploadItemEmpanelment" property="batchNo"/>>	
	 <input type="hidden" name="hiddenBatchRefNo" value=<bean:write name="frmTariffUploadItemEmpanelment" property="hiddenBatchRefNo"/>>
	 <input type="hidden" name="groupName" id="groupNameId" value="<%=request.getSession().getAttribute("groupName")%>">
	 
	<script>
if(document.forms[1].tariffType.value!="")	
	onChangeTariffType(document.forms[1].tariffType);
</script>
 </html:form>