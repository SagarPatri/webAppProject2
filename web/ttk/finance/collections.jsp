<%
/**
 * @ (#) collections.jsp March 06 2019
 * Project      : TTK HealthCare Services
 * File         : collections.jsp
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

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList" %>
<%@page import="com.ttk.action.table.TableData"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ page isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.security.Cache" %>
<script language="JavaScript" SRC="/ttk/scripts/validation.js"></script>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/finance/collections.js"></script>


<html:form action="/CollectionsAction.do" method="post" enctype="multipart/form-data">

<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="57%">Collection Add & Collections List</td>
	   <td width="43%" align="right" class="webBoard">&nbsp;<%@ include file="/ttk/common/toolbar.jsp" %></td>
	  </tr>
	</table>
<div class="contentArea" id="contentArea">
<html:errors/>

<logic:notEmpty name="updated" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp; 
						<bean:message name="updated" scope="request" />
					</td>
				</tr>
			</table>
		</logic:notEmpty> 
<logic:notEmpty name="updated1" scope="request">
			<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp; 
						<bean:message name="updated1" scope="request" />
					</td>
				</tr>
			</table>
		</logic:notEmpty> 

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
               <tr><td></td><td></td></tr>
              </table>
             </fieldset>
             <fieldset>
              <table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
              <tr>
               <td width="10%" class="textLabelBold">Invoice No.: </td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="invoiceNo" />
					</td>
              </tr>
              
               <tr>
               <td width="10%" class="textLabelBold">Invoice Date: </td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="dueDate" />
					</td>
              </tr>
              
               <tr>
               <td width="10%" class="textLabelBold">Invoice Due Date: </td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="invoiceDate" />
					</td>
              </tr>
              <tr>
               <td width="10%" class="textLabelBold">Total Invoice Amount (QAR):</td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="invoiceAmount" />
					</td>
              </tr>
               <tr>
               <td width="10%" class="textLabelBold"> Total Collections (QAR):</td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="totalCollectionsQAR" />
					</td>
              </tr>
               <tr>
               <td width="10%" class="textLabelBold">Total Outstanding (QAR):</td>
               <td width="30%" class="textLabel">
						<bean:write	name="frmCollectionsList" property="totalOutstandingQAR" />
					</td>
              </tr>
              
              <tr>
               <td width="10%" class="textLabelBold">Amount Received (QAR):<span class="mandatorySymbol">*</span></td>
              <td width="10%" class="textLabelBold">
              
               <logic:notEqual name="frmCollectionsList" property="reforward" value="SAVEYN">
               <html:text property="amountReceivedQAR"  styleClass="textBox textBoxMedium " maxlength="60" onblur="decimalValidation()" onkeyup="isNumeric(this);caluculateAmountDue(this)"/>
              </logic:notEqual>
              <logic:equal name="frmCollectionsList" property="reforward" value="SAVEYN">
              <html:text property="amountReceivedQAR"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
               </logic:equal>
               </td>
              </tr>
              
               <tr>
               <td width="10%" class="textLabelBold">Received Date: <span class="mandatorySymbol">*</span></td>
              <td width="10%" class="textLabelBold">
              
              <logic:notEqual name="frmCollectionsList" property="reforward" value="SAVEYN">
             <html:text property="receivedDate"  styleClass="textBox textBoxMedium"  />
                 <a name="CalendarObjectClDate" id="CalendarObjectClDate" href="#" onClick="javascript:show_calendar('CalendarObjectClDate','frmCollectionsList.receivedDate',document.frmCollectionsList.receivedDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="incpDate" width="24" height="17" border="0" align="absmiddle"></a>
             </logic:notEqual>
              <logic:equal name="frmCollectionsList" property="reforward" value="SAVEYN">
                <html:text property="receivedDate"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
              </logic:equal>
                </td>
              </tr>
              
               <tr>
               <td width="10%" class="textLabelBold">Transaction Ref: <span class="mandatorySymbol">*</span></td>
              <td width="10%" class="textLabelBold">
              
              <logic:notEqual name="frmCollectionsList" property="reforward" value="SAVEYN">
              <html:text property="transactionRef"  styleClass="textBox textBoxMedium" />
              </logic:notEqual>
              <logic:equal name="frmCollectionsList" property="reforward" value="SAVEYN">
                 <html:text property="transactionRef"  styleClass="textBox textBoxMedium" readonly="true" disabled="true"/>
               </logic:equal>
               </td>
              </tr>
                <tr>
               <td width="10%" class="textLabelBold">Amount Due (QAR):</td>
              <td width="10%" class="textLabelBold">
                <html:text property="amountDueQAR"  styleClass="textBox textBoxMedium textBoxDisabled" readonly="true"/>
               </td>
              </tr>
              
        </table>
</fieldset>
<fieldset><legend>Upload Document</legend>
	<table align="center" class="formContainer" border="0" cellspacing="0" cellpadding="0">
    	<tr>
	    	<td width="18%" class="formLabel">Upload file: </td>
    	  	<td>
				<html:file property="uploadFile" styleId="uploadFile" name="frmCollectionsList"/> &nbsp;&nbsp;
				<%-- <a href="#" onclick="javascript:showBankFile('<bean:write name="frmCollectionsList" property="fileName"/>')"> <bean:write name="frmCollectionsList" property="fileName"/> </a> --%>
			</td>
        </tr>
    </table>
	</fieldset>
	
	<table align="center" class="buttonsContainer" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" align="center">
				<%
				if (TTKCommon.isAuthorized(request, "Edit")) 
				{
				%>
				
			    <logic:notEqual name="frmCollectionsList" property="reforward" value="SAVEYN">
				<button type="button" name="Button" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="saveCollection()"> Sa<u>v</u>e </button>&nbsp;
				</logic:notEqual>
			   
			    <button type="button" onclick="backCollection();" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>B</u>ack</button>
 
				<%
 				}//end of if(TTKCommon.isAuthorized(request,"Edit"))
 				%>
				</td>
			</tr>
		</table>
		
		<fieldset><legend>Collections List</legend>
		
	<table border="1" align="center" cellpadding="0" cellspacing="0" class="formContainer">
	<tr>
<td width='20%' ID="listsubheader" CLASS="gridHeader" align='center'>&nbsp;Collection No.</td>
<td width='12%' ID="listsubheader" CLASS="gridHeader" align='center'>&nbsp;Invoice No.</td>
<td width='12%' ID="listsubheader" CLASS="gridHeader" align='center'>&nbsp;Received Amount(QAR)</td>
<td width='12%' ID="listsubheader" CLASS="gridHeader" align='center'>&nbsp;Received Date</td>
<td width='12%' ID="listsubheader" CLASS="gridHeader" align='center'>&nbsp;Transaction Ref</td>
<td width='25%' ID="listsubheader" CLASS="gridHeader" align='center'>&nbsp;Uploaded File</td>
<td width='7%' ID="listsubheader" CLASS="gridHeader" align='center'>&nbsp;Reverse</td>
</tr>
	
	

<%
ArrayList alData=new ArrayList();
TableData tableData=(TableData)session.getAttribute("tableData");
if(tableData!=null){
	if(tableData.getData()!=null&&tableData.getData().size()>0){
		alData=tableData.getData();
	}
}


for(int i=0;i<alData.size();i++){
	com.ttk.dto.finance.CollectionsVO dlfVO=(com.ttk.dto.finance.CollectionsVO)alData.get(i);
	
if(i%2==0){
%>
<!-- <tr CLASS="gridEvenRow" > -->
<tr CLASS="gridOddRow" >
<%}else{ %>
<tr CLASS="gridOddRow" >
<%} %>

<td   width='20%' align='left'><%=dlfVO.getCollectionNumber() %></td>
<td   width='12%' align='left'><%=dlfVO.getInvoiceNo() %></td>

<%  if("Y".equals(dlfVO.getDeleteYN())) {%>
<td  width='12%' align='left'><font color="red"><%=dlfVO.getAmountReceivedQAR() %></font></td>
<%}
    else {
    %>
    <td   width='12%' align='left'><%=dlfVO.getAmountReceivedQAR() %></td>
    <%} %>

<td   width='12%' align='left'><%=dlfVO.getReceivedDate() %></td>
<td   width='12%' align='left'><%=dlfVO.getTransactionRef()%></td>
<td   width='25%' align='left'><a href="#" title="Download File" onClick="showBankFile('<%=dlfVO.getFileName()%>')"><%=dlfVO.getFileName()%></a></td>
   
<%  if("Y".equals(dlfVO.getDeleteYN())) {%>
<td   width='7%' align='left'><%=dlfVO.getRemove() %>

<img src="/ttk/images/DeleteIcon.gif" title="Reverse Transaction" alt="Reverse Transaction" width="16" height="16" border="0" align="absmiddle"></a>

<%}
    else {
    %>

<td   width='7%' align='left'>  <a href="#" title="Reverse Transaction"  onClick="reverseTransaction(<%=dlfVO.getCollectionsSeqId()%>)"><%=dlfVO.getRemove() %></a>

<a href="#" onClick="reverseTransaction(<%=dlfVO.getCollectionsSeqId()%>)"><img src="/ttk/images/DeleteIcon.gif" title="Reverse Transaction" alt="Reverse Transaction" width="16" height="16" border="0" align="absmiddle"></a>

<%} %>
</td>
</tr>
<%} %>
	
	</table>
		
		</fieldset>

	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
<html:hidden property="reforward" name="frmCollectionsList"/>
<html:hidden property="policySeqId" name="frmCollectionsList"/>
<html:hidden property="invoiceSeqId" name="frmCollectionsList"/>
<html:hidden property="collectionsSeqId" name="frmCollectionsList"/>
<html:hidden property="totalOutstandingQAR" name="frmCollectionsList" />
<html:hidden property="switchTo" name="frmCollectionsList" value ="COR" />
<html:hidden property="deletionRemarks" name="frmCollectionsList"/>
<html:hidden property="hiddenInvoiceNo" name="frmCollectionsList"/>
<html:hidden property="totalOutstandingAmnt" name="frmCollectionsList" />
<html:hidden property="dueDate" name="frmCollectionsList" />

</div>
</html:form>