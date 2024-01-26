<%@ page language="java" import="java.util.*,javax.naming.*,java.io.* "contentType="application/vnd.ms-excel"%>
<html>
<head>
<title>Claim TAT</title>
</head>
<%
ArrayList alClaimTatData	=	(ArrayList)request.getAttribute("alErrorMsg");
String[] clmData	=	null;
%>
<body>
<%try{
%>
<table border="1">
	<tr bgcolor=""> <th>Activity_Code </th><th> Internal_Code </th><th> Service_Name </th><th> Internal_Description</th> 
	<th>Activity_Description</th><th>Unit_Price</th><th>Package_Price</th><th>Package_Size</th><th>Discount_Percentage</th><th>From_Date</th><th>Expiry_Date</th></tr>
	<%String[] temp	=	new String[12];
	for(int i=0;i<alClaimTatData.size();i++)	{
		temp	=	(String[])alClaimTatData.get(i);
		%><tr><%
		for(int k=0;k<temp.length;k++)
		{%>
			  <%if(!(temp[k] == null||temp[k].trim().equals("")||"null".equals(temp[k]))){%>
			  <td> <%=temp[k] %> </td>
			  <%}else{%>
			  <td><%="-"%></td>
			  <%}%>
		<%}
			%></tr><%
	}
	%>
</table>


<%}catch(Exception ex)
{
	System.out.println("Error in Excel::"+ex);
} %>
</body>
</html>