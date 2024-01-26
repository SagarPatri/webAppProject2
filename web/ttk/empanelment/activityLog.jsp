<%@ page import=" com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script type="text/javascript">

function onGenerateEXLReport()
{
	var reportType=document.getElementById("reportType").value;
	var reportID=document.getElementById("reportID").value;
	var hospSeqId=document.getElementById("hospSeqId").value;

var strParam = "";
strParam = hospSeqId;

var openPage = "/ReportsAction.do?mode=doGenerateActivityLogReport"+"&strParam="+strParam+"&reportType="+reportType+"&reportID="+reportID;
var w = screen.availWidth - 10;
var h = screen.availHeight - 99;
var features = "scrollbars=0,status=1,toolbar=1,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
window.open(openPage,'',features);
}

function onClose()
{
	document.forms[1].mode.value="doCloseActivityLog";
	document.forms[1].action="/HospitalActivityLogAction.do";
	document.forms[1].submit();
}
</script>
<!-- S T A R T : Content/Form Area -->
	<html:form action="/HospitalActivityLogAction.do" method="post">

<div class="contentArea" id="contentArea">

<!-- S T A R T : Search Box -->
<html:errors/>

<!-- S T A R T : Grid -->
<fieldset>
<legend>Activity Log</legend>
<ttk:HtmlGrid name="tableData" />
<!-- E N D : Grid -->
</fieldset>
<!-- S T A R T : Buttons and Page Counter -->
	<table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	   	 	<td align="right" nowrap>
		   		<button type="button" name="Button" accesskey="d" class="buttons" onMouseout="this.className='buttons'"	onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onGenerateEXLReport();"><u>D</u>ownload to Excel</button>
				&nbsp;
				<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose();"><u>C</u>lose</button>
	    	</td>
	  </tr>
	</table>
	</div>
<!-- E N D : Buttons and Page Counter -->
	<INPUT TYPE="hidden" NAME="rownum" VALUE=''>
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<INPUT TYPE="hidden" NAME="tab" VALUE="">
	
	<html:hidden property="hospSeqId" value="<%=(String)request.getSession().getAttribute("hospSeqId")%>"/>
	<html:hidden property="reportType" 	styleId="reportType" 	value="EXL"/>
	<html:hidden property="reportID" 	styleId="reportID" 		value="ActivityLogRpt"/>
	
	<html:hidden property="oldPaymentDuration" value="<%=(String)request.getSession().getAttribute("oldPaymentDuration")%>"/>
	<html:hidden property="oldPaymentTermAgrSDate" value="<%=(String)request.getSession().getAttribute("oldPaymentTermAgrSDate")%>"/>
	<html:hidden property="oldPaymentTermAgrEDate" value="<%=(String)request.getSession().getAttribute("oldPaymentTermAgrEDate")%>"/>
	</html:form>
<!-- E N D : Content/Form Area -->