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
	<th>Activity_Description</th><th>Package_Id</th><th>Bundle_Id</th><th>Gross_Amount</th><th>Discount_Percentage</th><th>From_Date</th><th>Expiry_Date</th></tr>
	<%String[] temp	=	new String[11];
	for(int i=0;i<alClaimTatData.size();i++)	{
		temp	=	(String[])alClaimTatData.get(i);
		%><tr><%
		for(int k=0;k<temp.length;k++)
		{%>
			  <td> <%=temp[k] %> </td>
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