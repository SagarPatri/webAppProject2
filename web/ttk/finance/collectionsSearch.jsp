<%
/**
 * @ (#) collectionsSearch.jsp March 05 2019
 * Project      : TTK HealthCare Services
 * File         : collectionsSearch.jsp
 * Author       : Deepthi Meesala
 * Company      : RCS
 * Date Created : March 05 2019
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<script language="JavaScript" SRC="/ttk/scripts/validation.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/finance/collectionsSearch.js"></script>


<html:form action="/CollectionsSearchAction.do" >

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="57%">List of Policies</td>
	   <td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
	  </tr>
	</table>
	
	
	 <table align="center" class="tablePad"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="10%" nowrap class="textLabelBold">Switch to:</td>
        <td width="90%">
	        <html:select property="switchTo" styleClass="specialDropDown" styleId="switchTo" onchange="javascript:onSwitch();">
				<html:option value="COR">Corporate</html:option>
				<html:option value="IND">Individual</html:option>
			</html:select>
		</td>
		</tr>

    </table>
	
	
<div class="contentArea" id="contentArea">
<html:errors/>

<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
<tr>
 <td nowrap>Group Name:<br>
	     	 	<html:text property="groupName" styleClass="textBox textBoxMedium" maxlength="60"/>
	     	</td>


 <td nowrap>Group ID:<br>
	     	 	<html:text property="groupId" styleClass="textBox textBoxMedium" maxlength="60"/>
	     	</td>
	     	
	     	
	 <td nowrap>Policy No.:<br>
	     	 	<html:text property="policyNum" styleClass="textBox textBoxMedium" maxlength="60"/>
	     	</td>
	     	     	
 	<td nowrap>Policy Status:<br>
	     	 	<%-- <html:text property="policyStatus" styleClass="textBox textBoxMedium" maxlength="60"/> --%>
	     	 	 <html:select property="policyStatus" styleClass="selectBox selectBoxMedium" styleId="policyStatus" >
	     	 	 <html:option value="">Select from list</html:option>
				<html:option value="A">Active</html:option>
				<html:option value="I">Inactive</html:option>
			</html:select>
	     	 	
	     	 	
	     	 	
	     	</td>
	     	
	  <td>
	  &nbsp;&nbsp;<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
	  </td>   	
	     	
</tr>
</table>

	<ttk:HtmlGrid name="tableData"/>

	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  	
	  	 <tr>
	  	 
	  	    <td width="27%"></td>
		    <td width="73%" align="right">
		  
			   	 <button type="button" name="Button" accesskey="c" class="button"  style="color: black;" onClick="javascript:onExport();"><u>E</u>xport</button>&nbsp;
	        </td>
		  
	  	</tr> 
	  			<ttk:PageLinks name="tableData"/>
	</table>
<table  align="right" class="formContainer" border="0" cellspacing="0" cellpadding="0" style="text-align: right;">
<tr>
 <td><strong>Total Premium (QAR):</strong></td>
	     	 	 <td width="10%" class="formLabel">
	     	 	   <html:text property="totalPremiumSum"  styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"/>
	     	 	 </td>
	     	<td></td>
</tr>
<tr>
 <td nowrap><strong>Total Collections (QAR):</strong></td>
	     	 	 <td width="10%" class="formLabel">
	     	 	  <html:text property="totalCollectionsSum"  styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"/>
	     	 	 </td>
	     	
	     	<td></td>
</tr>
<tr>
 <td nowrap><strong>Total Outstanding (QAR):</strong></td>
	     	 	 <td width="10%" class="formLabel">
	        	  <html:text property="totalOutstandingSum"  styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"/>	 
	     	 	 </td>
	     	<td></td>
</tr>

</table>

	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
<html:hidden property="policySeqId" name="frmCollectionsList"/>
<html:hidden property="reforward" />
<html:hidden property="reportType" value ="EXCEL"/>


</div>
</html:form>