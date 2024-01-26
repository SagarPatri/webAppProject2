<%@page import="com.ttk.dto.administration.ReportVO"%>
<%@ page language="java" import="java.util.*,javax.naming.*,java.io.*"   %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/ttk-tags.tld" prefix="ttk" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<html>
<head>
<title>Insert title here</title>
</head>

<%
	String parameter = request.getParameter("parameter");
	if("ERP".equalsIgnoreCase(parameter) || "PRP".equalsIgnoreCase(parameter) || "CLR".equalsIgnoreCase(parameter)){
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename="+parameter+"-Reports.xls");
	}
%>

<logic:equal name="flag"	scope="session" value="generatereport">
<%
String historyMode= (String)request.getAttribute("historyMode");

if(historyMode.equals("CLM"))
{
response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition", "attachment; filename=Claim History Detail .xls");
}
else if(historyMode.equals("PAT"))
{
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=Pre-Authorization History Detail.xls");
}

%>
</logic:equal>

<logic:equal name="flag"	scope="session" value="generatereporthistorydetails">

<%
String historyMode= (String)request.getAttribute("historyMode");

if(historyMode.equals("CLM"))
{
response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition", "attachment; filename=Claim History Detail.xls");
}
else if(historyMode.equals("PAT"))
{
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=Pre-Authorization History Detail.xls");
}
%>
</logic:equal>

<%
ArrayList alData	=	(ArrayList)request.getAttribute("alData");
%>
<body>
<table border="1">

<%try{
ReportVO reportVO	=	null;
ArrayList alresults	=	null;
int temp	=	0;

for(int i=0;i<alData.size();i++)
{
	reportVO	=	(ReportVO)alData.get(i);
	alresults	=	(ArrayList)reportVO.getAlColumns();

	%> <tr> 
	
	<%for(int j=0;j<alresults.size();j++)
		{
		temp++;
	if(i==0){
		
		%><td style="width:100%;display:block"> <font color="blue"><strong> <%=alresults.get(j) %></strong></font></td>
		<%}else{
			
	if(j==26)  
	{
		 if("CLM".equals(request.getAttribute("historyMode").toString()) || "PAT".equals(request.getAttribute("historyMode").toString()))
		{ 	 %>
		<td style="width:250px;display:block;"> <%=alresults.get(j) %></td>
	   <%}
		 else
		 {  %>
			<td style="width:100%;display:block;"> <%=alresults.get(j) %></td>
		   <%}
		 
	}
		else
		{
		%>
		<td style="width:100%;display:block"> <%=alresults.get(j) %></td>
		<%}%>
			
		<%}
		}	%>	  
		</tr>
<%
if(alData.size()==1)
{%>
	<tr>
	<td colspan="<%= temp%>" align="center">
	<font color="red"> <strong> No Data Found</strong> </font>
	</td>
	</tr>
<%}

	}}catch(Exception ex)
{
	System.out.println("Error in Excel::"+ex);
}
%>

</table>
</body>
</html>