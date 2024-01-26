<%@page import="java.util.ArrayList"%>
<%

%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/empanelment/searchPharmacyTariff.js"></script>
<SCRIPT LANGUAGE="JavaScript">
bAction = false;
var TC_Disabled = true;
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	var JS_SecondSubmit=false;
</SCRIPT>
<%
pageContext.setAttribute("alPayerCodeSearch", Cache.getCacheObject("insuranceCompany"));
pageContext.setAttribute("alProviderCodeSearch", Cache.getCacheObject("providerCodeSearch"));
pageContext.setAttribute("alNetworkTypeSearch", Cache.getCacheObject("primaryNetwork"));
pageContext.setAttribute("alCorpCodeSearch", Cache.getCacheObject("corpCodeSearch"));
pageContext.setAttribute("alServiceNamePC", Cache.getCacheObject("serviceNamePC"));
%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/TarrifSearchAction.do" method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td>Pharmacy/Consumables Tariff Search</td>
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
	<table>
	<tr>
		<td align="left" nowrap>Switch To:&nbsp;&nbsp;
			<html:select property="switchTo" styleClass="selectBox selectBoxLarge" onchange="javascript:onSwitch()">
			    <html:option value="PAC">PHARMACY/CONSUMABLE TARIFF</html:option>
				<html:option value="ACT">ACTIVITY TARIFF</html:option>
			</html:select>
		</td>
	</tr>
	</table>
	<!-- Search box -->
	<table class="searchContainer" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <%-- <td align="left" nowrap>Type of Tariff: <span class="mandatorySymbol">*</span><br>
			<html:select property="tariffTypeSearch" styleClass="selectBox selectBoxMedium" onchange="onChangeTariffType(this)">
				<html:option value="HOSP">Provider</html:option>
			</html:select>
		</td> --%>
        <td align="left" nowrap><br> 
			<html:select property="payerCodeSearch" styleClass="selectBox selectBoxLargest">
		        	<html:optionsCollection name="alPayerCodeSearch" label="cacheDesc" value="cacheId" />
			</html:select>
		</td>
		<td align="left" nowrap>Provider:<br>
			<html:select property="providerCodeSearch" styleClass="selectBox selectBoxLarge" disabled="true" readonly="true">
				<html:optionsCollection name="alProviderCodeSearch" label="cacheDesc" value="cacheId"/>
			</html:select>
		</td>
		<td align="left" nowrap>Network Type:<br>
			<html:select property="networkTypeSearch" styleClass="selectBox selectBoxMedium">
					<html:option value="">--Select From List--</html:option>
		        	<html:optionsCollection name="alNetworkTypeSearch" label="cacheDesc" value="cacheId" />
			</html:select>
		</td>
		 <td align="left" nowrap>End Date:<br>
	<!-- <td width="30%" class="formLabel"> frmSearchTariffItem -->
<%-- <html:text property="enDdate" styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].enDdate',document.forms[1].enDdate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a> --%>
<html:text property="enddate" styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].enddate',document.forms[1].enddate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
	
	</td> 
		

		</tr>
		
		<tr>
		<%-- <td align="left" nowrap>Corporate:<br>
			<html:select property="corpCodeSearch" styleClass="selectBox selectBoxMedium">
					<html:option value="">--Select From List--</html:option>
		        	<html:optionsCollection name="alCorpCodeSearch" label="cacheDesc" value="cacheId" />
			</html:select>
		</td> --%>
		<td align="left" nowrap>Price Ref No:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Activity Code:<br>
			<html:text property="priceRefNo" styleClass="textBox textBoxMedium"/>
			&nbsp;&nbsp;
			<html:text property="activityCode" styleClass="textBox textBoxSmall"/>
		</td>
		<td align="left" nowrap>Service Type :<br>
			<html:select property="serviceName" styleClass="selectBox selectBoxMedium">
					<html:option value="">--All--</html:option>
		        	<html:optionsCollection name="alServiceNamePC" label="cacheDesc" value="cacheId" />
			</html:select>
		</td>
		<td align="left" nowrap>Internal Code :<br>
			<html:text property="internalCode" styleClass="textBox textBoxSmall"/>
		</td>
      <td valign="bottom" nowrap>
	        	<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
        </td>
      </tr>
      </table>
	<!-- Search box ends here -->
    
			<div>
		 <p>
	<!--     <button type="button" name="Button" accesskey="u" 
	class="buttons" onMouseout="this.className='buttons'" 
	onMouseover="this.className='buttons buttonsHover'" 
	onClick="javascript:onUploadButton()"><u>U</u>pload Tariff</button>
		  <a href="#" onclick="javascript:onUploadLink()">Click here to <u>U</u>pload Tariff</a>
		  -->
	<%if(("Empanelled".equals(request.getSession().getAttribute("empStatus")))||("In-Progress".equals(request.getSession().getAttribute("empStatus")))){%>
		  <button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUploadLink()" style="padding-left:0px;"><u>U</u>pload Tariff</button>
	<%}else{%>
		  <button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" disabled="disabled" onMouseover="this.className='buttons buttonsHover'" style="padding-left:0px;"><u>U</u>pload Tariff</button>
	<%}%>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onErrorlog()"><u>E</u>rror Log Tariff</button>
 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<!-- adding bulk modify -->
		<button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBulkModify()" style="padding-left:0px;">Bulk <u>A</u>ctivity code Edit/Modify</button>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<button type="button" name="Button" accesskey="e" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onModifyTariff()" style="padding-left:0px;"><u>E</u>dit/Modify Tariff Items</button> 
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <button type="button" name="Button" accesskey="m" class="buttons" 
	onMouseout="this.className='buttons'" 
	onMouseover="this.className='buttons buttonsHover'" 
	onClick="javascript:onModifyServiceType()" 
	style="padding-left:0px;">Service Type Edit/<u>M</u>odify</button>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <button type="button" name="Button" accesskey="D" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:doDownLoadFiles()" style="padding-left:0px;"><u>D</u>ownload Tariff Details</button>

		 </p>
		 </div>

    
    
<!--     <div>
    <p> 
	<button type="button" name="Button" accesskey="u" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onUploadButton()"><u>U</u>pload Tariff</button>
	<a href="#" onclick="javascript:onUploadLink()">Click here to <u>U</u>pload Tariff</a>		
    </p>
    </div> -->
    
    <!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableData" className="gridWithCheckBox zeroMargin"/>
	<!-- E N D : Grid -->
    
	<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
		 <%-- <tr>
		    <td width="27%"></td>
		    <td width="73%" align="right">
		    <%
			   if(TTKCommon.isDataFound(request,"tableData"))
			   {
		    %>
			    <button type="button" name="Button" accesskey="c" class="buttonsCopyWB" onMouseout="this.className='buttonsCopyWB'" onMouseover="this.className='buttonsCopyWB buttonsCopyWBHover'" onClick="javascript:copyToWebBoard();"><u>C</u>opy to Web Board</button>&nbsp;
			<%
				}
			%>
	        </td>
	  	</tr> --%>
	  		 <ttk:PageLinks name="tableData"/>

	</table>
	
	<!-- <div>
	<br>
		<a href="#" onclick="javascript:onModifyTariff()">Click here to Edit/Modify</a>		
		&nbsp;&nbsp;&nbsp;&nbsp;
    </div> -->
	
    <!-- E N D : SELCTIONS -->
	<!-- E N D : Buttons -->
	<!-- E N D : Form Fields -->
   </div>
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<input type="hidden" name="child" value="">
	<html:hidden property="providerCodeSearch"/>
 </html:form>