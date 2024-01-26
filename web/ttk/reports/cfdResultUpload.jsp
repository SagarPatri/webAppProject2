<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>

<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,java.util.HashMap,java.util.ArrayList"%>
<%@ page import="com.ttk.dto.usermanagement.UserSecurityProfile,com.ttk.dto.administration.WorkflowVO"%>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/reports/cfdReport.js"></script>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>

<style>
.searchContainer {
    background-color: #fff;
    margin-top: 10px;
    padding: 5px;
    width: 80%;
    border: 0px solid #fff;
}
.gridWithPricing {
    width: 80%;
    margin-top: 10px;
    text-align: center;
    border: 0px solid #fff;
    text-align: left;
}

.buttons {
    background-color: #E6E9ED;
    color: #000000;
    height: 19px;
    border: 1px solid #555555;
    font-size: 11px;
    padding-left: 10px;
    padding-right: 10px;
    margin-right: 70px;
}
.diagnosis_fieldset_class{
    width: 75%;
    border: 1px solid #CCCCCC;
    padding-top: 0px;
    padding-bottom: 10px;
}


</style>
<script>
bAction=false;
var TC_Disabled = true;
var JS_SecondSubmit=false;
</script>
<%
//pageContext.setAttribute("auditStatusList", Cache.getCacheObject("auditStatusList"));

%>
<!-- S T A R T : Content/Form Area -->
<html:form action="/CFDReportAction.do" enctype="multipart/form-data" method="post">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="57%">CFD Report</td>
			
		</tr>
	</table>
	<!-- E N D : Page Title -->
	<!-- S T A R T : Search Box -->
	<logic:notEmpty name="notifyerror" scope="request">
  <table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td><img src="/ttk/images/warning.gif" title="Warning" alt="Warning" width="16" height="16" align="absmiddle">&nbsp;
         <bean:write name="notifyerror" scope="request"/>
     </td>
   </tr>
  </table>
 </logic:notEmpty>
 
  	<logic:notEmpty name="successMsg" scope="request">
  <table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td><img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
         <bean:write name="successMsg" scope="request"/>
     </td>
   </tr>
  </table>
 </logic:notEmpty>
	<html:errors/>
	<div class="contentArea" id="contentArea">
	<FIELDSET>
			<LEGEND>CFD Results Upload</LEGEND>
			<table align="center"  class="gridWithPricing" border="0" cellspacing="1" cellpadding="0">
			    <tr>
			    <td width="40%"></td>
          	    <td width="40%" height="28" nowrap class="fieldGroupHead-r">
      		       File Name :  <html:file  property="resultUploadFile" styleId="resultUploadFileId"/>
               </td>
               </tr>
               <tr>
               <td width="40%"></td>
               <td  width="40%" height="28" nowrap class="fieldGroupHeader-r">
               <font color="color:#495879;">Show Template :</font><a href="#" onClick="javascript:showTemplate()" >CFD Investigation  Template</a>
      		   </td>
        	   </tr>
        	   <tr><td></td></tr>
        	   <tr><td><br/></td></tr>
        <tr>
        <td width="40%"></td>
        <td align="center" width="40%">
					<button type="button" name="Button" accesskey="g" class="buttons"
						onMouseout="this.className='buttons'"
						onMouseover="this.className='buttons buttonsHover'"
						onClick="javascript:onUploadCFDInvReport();">
						<u>U</u>pload File
					</button>&nbsp;
					
					<button type="button" name="Button" accesskey="c" class="buttons"
						onMouseout="this.className='buttons'"
						onMouseover="this.className='buttons buttonsHover'"
						onClick="javascript:onCloseReportWindow();">
						<u>C</u>lose
					</button>
				</td>
        </tr>
		 </table>
		</FIELDSET>
	<FIELDSET class="diagnosis_fieldset_class">
			<LEGEND>Log Details</LEGEND>
	<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
	
       		<tr>
       		<td nowrap>Start Date:
	            	<html:text property="logStartDate"  styleClass="textBox textDate" maxlength="10"/><A NAME="logStartDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].logStartDate',document.forms[1].logStartDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		    <td nowrap style="width: 200px;">End Date:
	            	<html:text property="logEndDate"  styleClass="textBox textDate" maxlength="10" /><A NAME="logEndDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].logEndDate',document.forms[1].logEndDate.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
            </td> 
		     
          		 <td>
          		<a href="#" accesskey="s" onClick="javascript:onSearchUploadLog()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
          		</td> 
			</tr>
	</table>
		</FIELDSET>

	</div>
	<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
</html:form>
	<!-- E N D : Content/Form Area -->