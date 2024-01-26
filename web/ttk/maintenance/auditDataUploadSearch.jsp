<!-- mm aa 
always bless. -->

<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<script type="text/javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/jquery-1.4.2.min.js"></script>
<script language="javascript" src="/ttk/scripts/jquery.autocomplete.js"></script>
<script type="text/javascript" SRC="/ttk/scripts/validation.js"></script>
<script type="text/javascript">
	function onSearch()
	{
		// 1:  from date-formate checking
		var sdate = document.forms[1].uploadDateFrom.value;
		if(sdate != null && sdate != ""){
			var pattern = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
			
			if (!pattern.test(sdate)) 
			{
			   alert("Please enter valid From Date.");
			   document.forms[1].uploadDateFrom.value="";
			   document.forms[1].uploadDateFrom.focus();
			   return false;
			}
		}
		// 2:  to date-formate checking
		var edate = document.forms[1].uploadDateTo.value;
		if(edate != null && edate != ""){
			var pattern = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
			if (!pattern.test(edate)) 
			{
			   alert("Please enter valid To Date.");
			   document.forms[1].uploadDateTo.value="";
			   document.forms[1].uploadDateTo.focus();
			   return false;
			}
		}
		
		 document.forms[1].action="/AuditDataUploadSearchAction.do";	
		 document.forms[1].mode.value="doSearch";
         document.forms[1].submit();
	}
	
	function upperCase()
	{
		document.forms[1].corporateName.value=(document.forms[1].corporateName.value).toUpperCase();
	}
	
	function onDelete()
	{
	    if(!mChkboxValidation(document.forms[1]))
	    {
		var msg = confirm("Are you sure you want to delete the selected record(s)?");
			if(msg)
			{
			    document.forms[1].mode.value = "removeUploadList";
			    document.forms[1].action = "/AuditDataUploadSearchAction.do";
			    document.forms[1].submit();
			}//end of if(msg)
	    }//end of if(!mChkboxValidation(document.forms[1]))
	}
	
	function pageIndex(pagenumber)
	{
	    document.forms[1].reset();
	    document.forms[1].mode.value="doSearch";
	    document.forms[1].pageId.value=pagenumber;
	    document.forms[1].action = "/AuditDataUploadSearchAction.do";
	    document.forms[1].submit();
	}//end of pageIndex(pagenumber)

	//function to display previous set of pages
	function PressBackWard()
	{
	    document.forms[1].reset();
	    document.forms[1].mode.value ="doBackward";
	    document.forms[1].action = "/AuditDataUploadSearchAction.do";
	    document.forms[1].submit();
	}//end of PressBackWard()

	//function to display next set of pages
	function PressForward()
	{
	    document.forms[1].reset();
	    document.forms[1].mode.value ="doForward";
	    document.forms[1].action = "/AuditDataUploadSearchAction.do";
	    document.forms[1].submit();
	}//end of PressForward()
	
</script>

<!-- S T A R T : Content/Form Area -->
	<html:form action="/AuditDataUploadSearchAction.do" method="post">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
      <tr>
        	<td>Data Upload Search</td>
      </tr>
    </table>
    <logic:notEmpty name="delete" scope="request">
				<table align="center" class="errorContainer" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="/ttk/images/ErrorIcon.gif" title="Error" alt="Error" width="16" height="16" align="absmiddle">&nbsp; 
						<bean:write name="delete" scope="request" /></td>
					</tr>
				</table>
	</logic:notEmpty>
<!-- E N D : Page Title -->
<div class="contentArea" id="contentArea">

<!-- S T A R T : Search Box -->
<html:errors/>
<table align="center" class="searchContainer" border="0" cellspacing="0" cellpadding="0">
	<tr>
		
        <td nowrap>Policy number :<br>
            <html:text property="policyNumber" styleClass="textBox textBoxLarge" maxlength="250" />
        </td>
        <td nowrap>Corporate Name :<br>
            <html:text property="corporateName" styleClass="textBox textBoxLarge" maxlength="250"  onkeyup="upperCase();"/>
        </td>
        
        <td nowrap>From Date:<br>
	            <html:text property="uploadDateFrom"  styleClass="textBox textDate" maxlength="10"/>
	            <A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].uploadDateFrom',document.forms[1].uploadDateFrom.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
        </td>
        
         <td nowrap>To Date :<br>
	            <html:text property="uploadDateTo"  styleClass="textBox textDate" maxlength="10"/><A NAME="CalendarObjectMarkDate" ID="CalendarObjectMarkDate" HREF="#" onClick="javascript:show_calendar('CalendarObjectMarkDate','forms[1].uploadDateTo',document.forms[1].uploadDateTo.value,'',event,148,178);return false;" onMouseOver="window.status='Calendar';return true;" onMouseOut="window.status='';return true;"><img src="/ttk/images/CalendarIcon.gif" title="Calendar" alt="Calendar" name="mrkDate" width="24" height="17" border="0" align="absmiddle"></a>
        </td>
     
        <td width="100%" valign="bottom">
        	<a href="#" accesskey="s" onClick="javascript:onSearch()"   class="search"><img src="/ttk/images/SearchIcon.gif" title="Search" alt="Search" width="16" height="16" border="0" align="absmiddle">&nbsp;<u>S</u>earch</a>
        </td>
	</tr>
</table>
<!-- E N D : Search Box -->

<!-- S T A R T : Grid -->
<ttk:HtmlGrid name="tableData" />
<!-- E N D : Grid -->

<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="73%" nowrap>&nbsp;
	    </td>
	    <td align="right" nowrap>
	    	
	    	<%
	    		if(TTKCommon.isDataFound(request,"tableData")&& TTKCommon.isAuthorized(request,"delete"))
	    		{
	    	%>
	    	   		<button type="button" name="Button" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onDelete();"><u>D</u>elete</button>&nbsp;
	    	<%
	    		}
	    	%>
	    </td>
	  </tr>
  		<ttk:PageLinks name="tableData"/>
	</table>
	</div>
<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	</html:form>
<!-- E N D : Content/Form Area -->