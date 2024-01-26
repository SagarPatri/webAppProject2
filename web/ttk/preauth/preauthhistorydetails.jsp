<%
/**
 * @ (#) preauthhistorydetail.jsp.jsp May 10, 2006
 * Project      : TTK HealthCare Services
 * File         : preauthgeneral.jsp
 * Author       : Srikanth H M
 * Company      : Span Systems Corporation
 * Date Created : May 10, 2006
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ page import="com.ttk.common.TTKCommon,com.ttk.common.security.Cache,com.ttk.common.PreAuthWebBoardHelper,org.dom4j.Document"%>
<%@page import="org.dom4j.Node,java.util.List,java.util.Iterator" %>
<link href="/ttk/styles/Default.css" media="screen" rel="stylesheet"></link>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<SCRIPT type="text/javascript" SRC="/ttk/scripts/preauth/preauthhistorydetails.js"></SCRIPT>
<script type="text/javascript">
var JS_Focus_Disabled =true;

</script>
<%
String PreAuthHistoryTypeID = (String)request.getAttribute("PreAuthHistoryTypeID");
String strLink=TTKCommon.getActiveLink(request);
pageContext.setAttribute("PreAuthHistoryTypeID",PreAuthHistoryTypeID);
pageContext.setAttribute("strLink",strLink);
%>
<div id="contentArea" class="contentArea">

<ttk:PreAuthHistoryDetails/>
 <%if("Pre-Authorization".equals(TTKCommon.getActiveLink(request))){ %>
 <form action="/PreAuthHistoryAction.do">
  <%}else if("Claims".equals(TTKCommon.getActiveLink(request))){ %>
 <form action="/ClaimHistoryAction.do">
 <%}else if("CounterFraudDept".equals(TTKCommon.getActiveLink(request))){ %>
  <form action="/PreAuthHistoryAction.do">
  <%}%>
 
 <input type="hidden" name="mode"/>
 <input type="hidden" name="activityDtlSeqId"/>
 <INPUT type="hidden" name="reforward">
 <table align="center" class="buttonsContainerGrid"  border="0" cellspacing="0" cellpadding="0">
 	 <tr>
	    <td width="27%"> </td>
	    <td width="73%" nowrap align="right">
	      <button type="submit"  name="Button1" accesskey="n" class="buttons" onMouseout="this.className='buttons'" onClick="javascript:generateDownloadHistoryDetailPATCLM();" onMouseover="this.className='buttons buttonsHover'">Dow<u>n</u>load to Excel</button>

       &nbsp;&nbsp;
	    <button type="submit"  name="Button2" accesskey="c" class="buttons" onClick="javascript:onClose();" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'"><u>C</u>lose</button>
	    </td>
	    </tr>
	    </table>
	    <html:hidden name="frmHistoryList" property="historyMode" styleId="historyMode" />
	     <logic:equal name="frmHistoryList" property="historyMode" value="PAT">
	     <html:hidden name="frmHistoryList" property="preauthseqid" styleId="preauthseqid" />
	     </logic:equal>
	     
	       <logic:equal name="frmHistoryList" property="historyMode" value="CLM">
	      <html:hidden name="frmHistoryList" property="clmseqid" styleId="clmseqid" />
	       </logic:equal>
	        <html:hidden name="frmHistoryList" property="memberSeqID" styleId="memberSeqID" />
 
  <input type="hidden" name="Linkview" id="Linkview" value="<%=TTKCommon.getActiveLink(request)%>">
 </form>

</div>

