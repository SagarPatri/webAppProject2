<%
/**
 * @ (#) collectionsPremiumDistribution.jsp March 06 2019
 * Project      : TTK HealthCare Services
 * File         : collectionsPremiumDistribution.jsp
 * Author       : Deepthi Meesala
 * Company      : RCS
 * Date Created : March 06 2019
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
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<script language="JavaScript" SRC="/ttk/scripts/validation.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/finance/collectionsPremiumDistribution.js"></script>

<html:form action="/CollectionsPremiumDistributionAction.do" >

<logic:equal name="reforward" scope="request" value="xls">
	<SCRIPT LANGUAGE="JavaScript">
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 82;
		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open("/CollectionsSearchAction.do?mode=doViewInvoiceFile&displayFile=<bean:write name="fileName"/>&reportType=EXCEL",'PaymentAdvice',features);
	</SCRIPT>
</logic:equal>

<logic:equal name="reforward" scope="request" value="pdf">
<SCRIPT LANGUAGE="JavaScript">
		var w = screen.availWidth - 10;
		var h = screen.availHeight - 82;
		var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
		window.open("/CollectionsSearchAction.do?mode=doViewInvoiceFile&displayFile=<bean:write name="fileName"/>&reportType=PDF",'PaymentAdvice',features);
	</SCRIPT>
</logic:equal>

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="57%">Invoice List</td>
	   <td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
	  </tr>
	</table>
<div class="contentArea" id="contentArea">
<html:errors/>


 <fieldset>
     
        <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
              <tr>
               	<td width="5%" class="textLabelBold">Group Name:</td>              
                <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="groupName" />
					</td>
                
               	<td width="10%" class="textLabelBold">Group ID:</td>
                 <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="groupId" />
					</td>
              </tr>
              <tr>
               <td width="10%" class="textLabelBold">Policy No.:</td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="policyNum" />
					</td>
               <td width="10%" class="textLabelBold">Line of Business:</td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="lineOfBussiness" />
					</td>
              </tr>
                <tr>
               <td width="10%" class="textLabelBold">Policy Start Date:</td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="startDate" />
					</td>
               <td width="10%" class="textLabelBold">Policy End Date::</td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="endDate" />
					</td>
              </tr>
        </table>
 </fieldset>

	<ttk:HtmlGrid name="tableData"/>

	 <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  	
	    <tr>
		    <td width="27%"></td>
		    <td width="73%" align="right">
		  
			   	 <button type="button" name="Button" accesskey="c" class="button"  onClick="javascript:closePremiumDistribution();"><u>C</u>lose</button>&nbsp;

	        </td>
	  	</tr> 
	  			<%--  <ttk:PageLinks name="tableData"/>  --%>
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

<tr>
<td></td>
 <td class="textLabelBold" colspan="2">
	    				<a href="#" onClick="javascript:onDownloadCollection()">Download Collection Report</a>
	    			</td>
</tr>
</table>	

	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
<html:hidden property="switchTo" name="frmCollectionsList"/>
<html:hidden property="policySeqId" name="frmCollectionsList"/>
<html:hidden property="invoiceSeqId" name="frmCollectionsList"/>
<html:hidden property="reforward" name="frmCollectionsList"/>
<html:hidden property="switchTo" name="frmCollectionsList" value ="COR" />

</div>
</html:form>