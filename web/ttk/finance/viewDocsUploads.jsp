<%@page import="org.apache.struts.action.DynaActionForm"%>
<%
/**
 * 
 * Project      : 
 * File         :
 * Author       : 
 * Company      : 
 * Date Created : 
 *
 * @author       : 
 * Modified by   : 
 * Modified date : 
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache" %>
<%@ page import=" com.ttk.common.TTKCommon" %>

<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/claims/claimsUploads.js"></script>
<%  
DynaActionForm frmDocsUpload=(DynaActionForm)request.getAttribute("frmDocsUpload");
    boolean viewmode=true;
   
     if(TTKCommon.isAuthorized(request,"Edit"))
    {
        viewmode=false;
    }//end of if(TTKCommon.isAuthorized(request,"Edit"))
    pageContext.setAttribute("viewmode",new Boolean(viewmode));
    String Caption=(String)session.getAttribute("caption");
    

	pageContext.setAttribute("descriptionCode", Cache.getCacheObject("claimsDocUpload"));
%>
<html:form action="/ClaimDocsUploadList.do"  method="post" enctype="multipart/form-data">
	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
			 <tr>
    			<td>Uploads- <bean:write name="frmDocsUpload" property="caption"/></td>   
    		</tr>
	</table>
	<html:errors/>
	<!-- E N D : Page Title --> 
	<div style="width: 99%; float: right;">
	<div class="scrollableGrid" style="height:290px;">
	
	<logic:notEmpty name="notify" scope="request">
		<table align="center" class="errorContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><img src="/ttk/images/ErrorIcon.gif" title="Alert" title="Alert" width="16" height="16" align="absmiddle">&nbsp;
	          <bean:write name="notify" scope="request"/>
	        </td>
	      </tr>
   	 </table>
   	 </logic:notEmpty>
   	 
	<!-- S T A R T : Success Box -->
	<logic:notEmpty name="updated" scope="request">
		<table align="center" class="successContainer" style="display:" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/ttk/images/SuccessIcon.gif" title="Success" width="16" height="16" align="absmiddle">&nbsp;
					<bean:message name="updated" scope="request"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
	<!-- E N D : Success Box -->
	<!-- S T A R T : Form Fields -->
	<br>
	
	<!-- S T A R T : Grid -->
	<ttk:HtmlGrid name="tableDataDocsUpload" className="gridWithCheckBox zeroMargin"/>
	<!-- E N D : Grid -->
	
	
	</div>
	<!-- S T A R T : Buttons -->
	<br>
 <table class="buttonsSavetolistGrid" border="0" cellspacing="0" cellpadding="0">
		<%-- <tr>
			<td width="100%" align="right" nowrap class="formLabel">
			<%
	    		if(TTKCommon.isDataFound(request,"tableDataDocsUpload") ||TTKCommon.isDataFound(request,"tableDataDocsUpload")&& TTKCommon.isAuthorized(request,"Delete"))
	    			
				{
		    %>
					<button type="button" name="Button" accesskey="d" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onDeletes();"><u>D</u>elete</button>
			<%
        		}// end of if(TTKCommon.isDataFound(request,"tableDataLinkDetails") && TTKCommon.isAuthorized(request,"Delete"))
        	%>		
			</td>
		</tr> --%>
				<tr>
          	       	<td  colspan="4" align="right">
					<button type="button" name="Button" accesskey="c" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onClose()"><u>C</u>lose</button>		
          	       	</td>
          	      </tr>
	</table> 
		
<SCRIPT  type="text/javascript">	
	function onClose(){
	if(!TrackChanges()) return false;//end of if(!TrackChanges())
    document.forms[1].mode.value="doUploadClose";
    document.forms[1].action="/viewBankDocs.do";
    document.forms[1].submit();
}//end of onClose()

/* function onDeletes()
{
	if(!mChkboxValidation(document.forms[1]))
    {
		var msg = confirm("Are you sure you want to delete the Information ?");
		if(msg)
		{
		    document.forms[1].mode.value = "doDelete";
		    document.forms[1].action = "/viewBankDocs.do";
		    document.forms[1].submit();
		}//end of if(msg)
    }//end of if(!mChkboxValidation(document.forms[1]))
}//end of onDelete() */

function edit(rownum)
{
	//document.forms[1].fileName.value = strFileName;
	var openPage = "/ReportsAction.do?mode=doViewUploadFiles&module=mou&rownum="+rownum;
   var w = screen.availWidth - 10;
   var h = screen.availHeight - 49;
   var features = "scrollbars=0,status=1,toolbar=0,top=0,left=0,resizable=0,menubar=yes,width="+w+",height="+h;
   window.open(openPage,'',features);
}//end of edit(rownum)
</SCRIPT>
		
		
		
		</div>
		<!-- END : Form Fields -->
	<html:hidden property="mouDocSeqID"/>
	<INPUT TYPE="hidden" NAME="rownum" VALUE="">
	<INPUT TYPE="hidden" NAME="mode" VALUE="">
	<INPUT TYPE="hidden" NAME="sortId" VALUE="">
	<INPUT TYPE="hidden" NAME="pageId" VALUE="">
	<html:hidden property="source_id" styleId="source_id" name="frmDocsUpload" value="CLM" />
		<html:hidden property="authType" styleId="authType"name="frmDocsUpload" value="CLM" />
</html:form>

<!-- E N D : Content/Form Area -->