<html>
<head>
<style type="text/css">:ACTIVE {
	text-indent: 
}
</style>
</head>
<%
/**
 * @ (#) addeditinvoice.jsp October 25th, 2007
 * Project      : TTK HealthCare Services
 * File         : addeditinvoice.jsp
 * Author       : Krupa 
 * Company      : Span Systems Corporation
 * Date Created : October 25th, 2007
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache"%>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/finance/bordereauinvoice.js"></script>
     <script type="text/javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>  

<%
	 pageContext.setAttribute("debitType",Cache.getCacheObject("debitType"));
	 pageContext.setAttribute("debitTypeDraft",Cache.getCacheObject("debitTypeDraft"));
	 pageContext.setAttribute("paymentMode",Cache.getCacheObject("paymentMode"));
	 boolean viewmode=true;
	 if(TTKCommon.isAuthorized(request,"Edit"))
	 {
	       viewmode=false;
	 }//end of if(TTKCommon.isAuthorized(request,"Edit"))
	 pageContext.setAttribute("viewmode",new Boolean(viewmode));
%>
<html:form action="/GenerateBordereauAction.do"> 

	<logic:notEmpty name="fileName" scope="request">
			<SCRIPT LANGUAGE="JavaScript">
		    	
				var w = screen.availWidth - 10;
				var h = screen.availHeight - 82;
				var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
				window.open("/InvoiceGeneralAction.do?mode=doInvoiceXL&displayFile=<bean:write name="fileName"/>&reportType=EXL",'PaymentAdvice',features);
			</SCRIPT>
		</logic:notEmpty>


<logic:notEmpty name="frmBordereauInvoices" property="seqID">
<logic:match value="DFL" name="frmBordereauInvoices"  property="statusTypeID">
	<% viewmode=true; %>
	</logic:match>
</logic:notEmpty>
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0"> 
          <tr> 
            <td width="57%"><bean:write name="frmBordereauInvoices" property="caption"/></td> 
            <td width="43%" align="right" class="webBoard">
            <logic:notEmpty name="frmBordereauInvoices" property="seqID">
<!--     			<a href="#" onclick="javascript:onListInvoices();"><img src="ttk/images/ClauseIcon.gif" alt="List of UnInvoiced Policies" width="16" height="16" border="0" align="absmiddle" ></a>
 -->          	</logic:notEmpty>
            </td> 
          </tr> 
        </table> 
	<!-- E N D : Page Title --> 
        <!-- S T A R T : Form Fields -->
        <div class="contentArea" id="contentArea">
		<html:errors/>
		<!-- S T A R T : Success Box -->
		<logic:notEmpty name="updated" scope="request">
		  	<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
		    	<td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
		    		<bean:message name="updated" scope="request"/>
		    	</td>
		 	</tr>
			</table>
		</logic:notEmpty>
		<!-- E N D : Success Box --> 
		    <table align="center" class="searchContainer " border="0" cellspacing="0" cellpadding="0">
		<tr>
    		<td width="20%" nowrap>Corp. Name: &nbsp; </td>
            <td width="30%" nowrap class="textLabelBold">
              <bean:write  name="frmBordereauInvoices"  property="groupName"/>
              <a href="#" onClick="javascript:onGroupComp();" ><img src="/ttk/images/EditIcon.gif" title="Select Company" alt="Select Company" width="16" height="16" border="0" align="absmiddle"></a>&nbsp;
<!--               <a href="#" onClick="javascript:onClear('Enrollment')"><img src="/ttk/images/DeleteIcon.gif" alt="Clear Company" width="16" height="16" border="0" align="absmiddle"></a>&nbsp;
 -->          	</td>
          	<td width="20%" nowrap >Group Id: &nbsp;</td>
            <td width="30%" nowrap class="textLabelBold">
	        	<bean:write name="frmBordereauInvoices" property="groupID"/>
	            <html:hidden name="frmBordereauInvoices" property="groupRegnSeqID"/>
            </td>
    	</tr>
    	
    	 </table>
		
	<fieldset>
	<legend>Bordereau Invoice Details </legend>
	<table width="75" border="0" align="center" cellpadding="0" cellspacing="0" class="formContainer">
      
      <tr>
        <td class="formLabel">Invoice No.:</td>
        <td colspan="3" class="textLabelBold"><bean:write name="frmBordereauInvoices" property="invoiceNbr"/></span></td>
      </tr>
      
      <tr>
        <td width="20%" class="formLabel">Invoice From Date:<span class="mandatorySymbol">*</span></td>
       <%--  <td width="30%">
        	<html:text name="frmBordereauInvoices" property="fromDate" styleClass="textBox textDate" disabled="true" readonly="true"/> --%>
        	
        <td width="30%">
        <html:text name="frmBordereauInvoices" onchange="javascript:refreshPolicyNo();" property="fromDate" styleClass="textBox textDate" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
          <logic:match name="viewmode" value="false">
          <A NAME="calEndDate" ID="calEndDate" HREF="#" onClick="javascript:refreshPolicyNo();show_calendar('calEndDate','frmBordereauInvoices.fromDate',document.frmBordereauInvoices.fromDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
          </logic:match>
        </td>
          
        <td width="20%">Invoice To Date:<span class="mandatorySymbol">*</span></td>
        <td width="30%">
        <html:text name="frmBordereauInvoices"  onchange="javascript:refreshPolicyNo();" property="toDate" styleClass="textBox textDate" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
          <logic:match name="viewmode" value="false">
          <A NAME="calEndDate" ID="calEndDate" HREF="#" onClick="javascript:refreshPolicyNo();show_calendar('calEndDate','frmBordereauInvoices.toDate',document.frmBordereauInvoices.toDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
          </logic:match>
          </td>
      </tr>
      
     
        <tr>
	        <td width="20%" class="formLabel">Include Previous Policy:</td>
	        <td width="30%">
		        <html:checkbox name="frmBordereauInvoices" onclick="javascript:refreshPolicyNo();" property="includeOldYN" value="Y" disabled="<%= viewmode %>"/>
		        <input type="hidden" name="includeOldYN" value="N">
	        </td>
	       
    		<td width="20%" nowrap>Policy Number:<span class="mandatorySymbol">*</span> </td>
            <td width="30%" nowrap class="textLabelBold">
              <html:text property="policyNbr" styleId="policyno"  name="frmBordereauInvoices" readonly="true"  styleClass="textBox textBoxLarge textBoxDisabled" ></html:text>
              <a href="#" onclick="javascript:onListInvoices();"><img src="ttk/images/ClauseIcon.gif" title="List of UnInvoiced Policies" alt="List of UnInvoiced Policies" width="16" height="16" border="0" align="absmiddle" ></a>
              
         </td>
          	<td width="20%" nowrap > &nbsp;</td>
            <td width="30%" nowrap class="textLabelBold">	
            </td>
    	
    	   <html:hidden name="frmBordereauInvoices" property="policySeqID"/>
	       </tr>
	      
        
        <tr>	
	              
    	<td width="20%" nowrap>Payment Mode: <span class="mandatorySymbol">*</span></td>
        <td width="30%" nowrap class="textLabelBold">
      <logic:match name="frmBordereauInvoices" property="statusTypeID" value="DFL">
	        	<html:select property="paymentMode" styleId="paymentMode" onclick="" onchange="paymentmodechange(this);" styleClass="selectBox selectBoxMedium"  disabled="true">
				<html:option value="">Select from list</html:option>
				<html:options collection="paymentMode" property="cacheId" labelProperty="cacheDesc" />
			</html:select>
			</logic:match>
		<logic:notMatch name="frmBordereauInvoices" property="statusTypeID" value="DFL">
			<html:select property="paymentMode" styleId="paymentMode" onclick="" onchange="paymentmodechange(this);" styleClass="selectBox selectBoxMedium" >
				<html:option value="">Select from list</html:option>
				<html:options collection="paymentMode" property="cacheId" labelProperty="cacheDesc" />
			</html:select>
		</logic:notMatch>
        </td>
       
       
     <%--    <td width="20%" class="formLabel">Status:<span class="mandatorySymbol">*</span></td>
        <td width="30%">
        <logic:notEmpty name="frmBordereauInvoices" property="seqID">
	        <logic:match name="frmBordereauInvoices" property="statusTypeID" value="DFL">
	        	<html:select property="statusTypeID" styleId="Status" styleClass="selectBoxMedium selectBoxDisabled" disabled="true">
					<html:option value="">Select from list</html:option>
					<html:options collection="debitType" property="cacheId" labelProperty="cacheDesc" />
				</html:select>
			</logic:match>
		<logic:notMatch name="frmBordereauInvoices" property="statusTypeID" value="DFL">
			<html:select property="statusTypeID" styleId="Status" styleClass="selectBox selectBoxMedium" >
				<html:option value="">Select from list</html:option>
				<html:options collection="debitType" property="cacheId" labelProperty="cacheDesc" />
			</html:select>
		</logic:notMatch>
		</logic:notEmpty>
		 <logic:empty name="frmBordereauInvoices" property="seqID">
		 	<html:select property="statusTypeID" styleId="Status" styleClass="selectBox selectBoxMedium" >
				<html:option value="">Select from list</html:option>
				<html:options collection="debitTypeDraft" property="cacheId" labelProperty="cacheDesc" />
			</html:select>
		 </logic:empty>
        </td> --%>
        
        
       <%--  <td width="20%" class="formLabel">Status:<span class="mandatorySymbol">*</span></td>
        <td width="30%">
        <html:select property="statusTypeID" styleId="Status" styleClass="selectBoxMedium selectBoxDisabled" >
                <html:option value="">Select from list</html:option>
				<html:options collection="debitType" property="cacheId" labelProperty="cacheDesc" />
				</html:select>
        </td> --%>
        
        </tr>
        
        
         
        
           <tr>
         <td nowrap id="dueDate1" style="display:none " class="formLabel"><span class="mandatoryLabel spanImmediate">Due date :</span><span class="mandatoryLabel spanHalfyear">Half Year Due date 1 :</span><span class="mandatoryLabel spanQuaterly">Qauterly Due date 1 :</span><span class="mandatorySymbol ">*</span><br>
          <html:text name="frmBordereauInvoices" property="dtdueDate1" styleClass="textBox textDate" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
          <A NAME="calDueDate1" ID="calDueDate1" HREF="#" onClick="javascript:show_calendar('calDueDate1','frmBordereauInvoices.dtdueDate1',document.frmBordereauInvoices.dtdueDate1.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
        </td>
        
         <td nowrap id="dueDate2" style="display:none " class="formLabel"><span class="mandatoryLabel spanHalfyear">Half Year Due date 2:</span><span class="mandatoryLabel spanQuaterly">Qauterly Due date 2:</span><span class="mandatorySymbol ">*</span><br>
          <html:text name="frmBordereauInvoices" property="dtdueDate2" styleClass="textBox textDate" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
          <A NAME="calDueDate1" ID="calDueDate2" HREF="#" onClick="javascript:show_calendar('calDueDate2','frmBordereauInvoices.dtdueDate2',document.frmBordereauInvoices.dtdueDate2.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
        </td>
        
         <td nowrap  id="dueDate3"  style="display:none "class="formLabel">Qauterly Due date 3:<span class="mandatorySymbol">*</span><br>
         <html:text name="frmBordereauInvoices" property="dtdueDate3" styleClass="textBox textDate" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
          <A NAME="calDueDate1" ID="calDueDate3" HREF="#" onClick="javascript:show_calendar('calDueDate3','frmBordereauInvoices.dtdueDate3',document.frmBordereauInvoices.dtdueDate3.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
        </td>
        
         <td nowrap id="dueDate4" style="display:none " class="formLabel">Qauterly Due date 4:<span class="mandatorySymbol">*</span><br>
          <html:text name="frmBordereauInvoices" property="dtdueDate4" styleClass="textBox textDate" disabled="<%= viewmode %>" readonly="<%= viewmode %>"/>
          <A NAME="calDueDate1" ID="calDueDate4" HREF="#" onClick="javascript:show_calendar('calDueDate4','frmBordereauInvoices.dtdueDate4',document.frmBordereauInvoices.dtdueDate4.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar"  width="24" height="17" border="0" align="absmiddle"></a>
        </td>
    	  
        </tr>
        
     </table>
	</fieldset>
	<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
    	<td width="100%" align="center">
    		<%
			    if(TTKCommon.isAuthorized(request,"Edit"))
				{
			%>
			<logic:match name="frmBordereauInvoices" property="statusTypeID" value="DFL">
				<logic:empty name="frmBordereauInvoices" property="batchSeqID">
					<logic:notMatch name="frmBordereauInvoices" property="invGenerateFlag" value="Y">
					<button type="button" id="generatebutton" style="display: " name="Button2" accesskey="g" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onGenerateTPARpt('<bean:write name="frmBordereauInvoices" property="fromDate"/>','<bean:write name="frmBordereauInvoices" property="toDate"/>','<bean:write name="frmBordereauInvoices" property="seqID"/>','<bean:write name="frmBordereauInvoices" property="policySeqID"/>')"><u>G</u>enerate Invoice</button>&nbsp;
				</logic:notMatch>
				</logic:empty>
			</logic:match>
			
			<logic:notMatch name="frmBordereauInvoices" property="statusTypeID" value="DFL">
				<logic:notMatch name="frmBordereauInvoices" property="invGenerateFlag" value="Y">
	        	<button type="button"   name="Button2" accesskey="s" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="onSave()"><u>S</u>ave</button>&nbsp;
				</logic:notMatch>
				<button type="button" name="Button2" accesskey="r" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="Reset()"><u>R</u>eset</button>&nbsp;
			</logic:notMatch>
			<%
				}//end of if(TTKCommon.isAuthorized(request,"Edit"))
			%>	
		 	<button type="button" name="Button2" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="Close()"><u>C</u>lose</button>
			</td>
		</tr>
	</table>
	<!-- E N D : Buttons and Page Counter -->
	
	</div>
 	<logic:notEmpty name="frmBordereauInvoices" property="frmChanged">
		<script> ClientReset=false;TC_PageDataChanged=true;</script>
	</logic:notEmpty> 
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE=''>
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	<html:hidden name="frmBordereauInvoices" property="paymentTypeFlag"/>
	<html:hidden name="frmBordereauInvoices" property="statusTypeID"/>
	
	
<script type="text/javascript">
paymentmodechange(document.forms[1].paymentMode);
</script>
	
	</html:form>
	</html>