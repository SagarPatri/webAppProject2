<%
/** @ (#) totalprovidersdashboard.jsp 08th Jan 2015
 * Project     : TTK Healthcare Services
 * File        : totalprovidersdashboard.jsp
 * Author      : Kishor kumar
 * Company     : RCS Technologies
 * Date Created: 16th FEB 2015
 *
 * @author 		 : Kishor kumar
 * Modified by   :
 * Modified date :
 * Reason        :
 *
 */
%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.ArrayList,com.ttk.common.TTKCommon,com.ttk.common.security.Cache,org.apache.struts.action.DynaActionForm" %>
<SCRIPT LANGUAGE="JavaScript" SRC="/ttk/scripts/validation.js"></SCRIPT>
<script language="javascript" src="/ttk/scripts/calendar/calendar.js"></script>
<script language="javascript" src="/ttk/scripts/empanelment/providerdashboard.js"></script>

<head>
	<link rel="stylesheet" type="text/css" href="ttk/styles/style.css" />
</head>
<%
ArrayList alTotlaProviders=(ArrayList)request.getAttribute("alTotlaProviders");
%>

<!-- S T A R T : Content/Form Area -->
<html:form action="/TotalProviersDashBoardAction.do">

	<!-- S T A R T : Page Title -->
	<table align="center" class="pageTitle" border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td>&nbsp;&nbsp;Provider <bean:write name="frmProviderDashBoardTotalProviders" property="caption"/> 
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
			  		<td>
			  			<img src="/ttk/images/SuccessIcon.gif" title="Success" alt="Success" width="16" height="16" align="absmiddle">&nbsp;
						<bean:message name="updated" scope="request"/>
			  		</td>
				</tr>
			</table>
		</logic:notEmpty>
		<!-- E N D : Success Box -->

<fieldset>
			<legend></legend>
			<table align="center" border="1" cellspacing="0" cellpadding="1">
				<tr>
    				<th width="20%" style="padding: 5px;">EMAPANELMENT EVENTS </th>
    				<th width="20%" style="padding: 5px;">TOTAL PROVIDERS </th>
    				<th width="20%" style="padding: 5px;">PENDING </th>
    				<th width="20%" style="padding: 5px;">COMPLETED</th>
	    		</tr>

		<logic:iterate id="PreRequisiteVO" name="alTotlaProviders" indexId="i">
		 	<tr class="<%=(i.intValue()%2)==0? "gridOddRow":"gridEvenRow"%>">	
				<td><bean:write name="PreRequisiteVO" property="hospName"/></td>	
			</tr>
		</logic:iterate>
	    	</table>
</fieldset>	    					
  		<!-- S T A R T : Buttons -->
  		<table align="center" class="buttonsContainer"  border="0" cellspacing="0" cellpadding="0">
  			<tr>
    			<td width="100%" align="center">
  				<button type="button" name="Button" accesskey="b" class="buttons" onMouseout="this.className='buttons'" onMouseover="this.className='buttons buttonsHover'" onClick="javascript:onBack();"><u>B</u>ack</button-->&nbsp;
 				</td>
  			</tr>  		
		</table>
	</div>
<input type="hidden" name="mode" value="">
	<!-- input type="hidden" name="child" value=""-->
</html:form>
