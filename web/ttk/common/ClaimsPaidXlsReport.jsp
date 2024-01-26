<%@ page language="java" import="java.util.*,javax.naming.*,java.io.* "contentType="application/vnd.ms-excel"%>
    <html>
     <head>
      <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
      
     </head>
    <body>
    <% ArrayList<String[]> alClaimPendingData	=	(ArrayList<String[]>)request.getAttribute("listofclaimpendingreport"); 
  double totalamountsum = 0.0;
  if (alClaimPendingData != null) {
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-Disposition",
				"attachment; filename="+ "claimPaidData.xls");
	}
  %>    
     <% String companyName = (String)request.getAttribute("COUNTRYNAME");
     String OfficeName = (String)request.getAttribute("OFFICENAME");
     String compCode = (String)request.getAttribute("COMPCODE");
     String officeCode = (String)request.getAttribute("OFFICECODE");
     String abbriviationCode = (String)request.getAttribute("ABBRIVIATIONCODE");
     String startDate = (String)request.getAttribute("STARTDATE");
     String endDate = (String)request.getAttribute("ENDDATE");
     %>
     <table cellpadding="1"  cellspacing="1" border="1" bordercolor="gray">
     <% if( alClaimPendingData != null&&!alClaimPendingData.isEmpty()){ %>
    	 
     
     <tr>
     <td colspan="12" ><%= companyName %></td> 
     </tr>
     <tr bgcolor="pink">
     <td colspan="12"><font color="white"><b> CLAIMS DETAIL FOR PAYMENT FOR UNIT : <%=officeCode %>  -  <%=abbriviationCode %>  For The Date <%=startDate %> - <%=endDate %></b> </font> </td>
     </tr>
     <tr>
     <td colspan="12"><%=OfficeName%></td>
     </tr>
     <br/>
     <tr>
     <td colspan="12"><%=compCode%></td>
     </tr>
      <tr bgcolor="#9999FF">
       <td><b>Sl.No.</b></td> 
       <td><b>Healthcare Company</b></td>
       <td><b>Alkoot ID</b></td>
       <td><b>Corporate Name</b></td>
       <td><b>Policy No.</b></td>
       <td><b>Agent Code</b></td>
       <td><b>Claim Settlement No</b></td>
       <td><b>Claim Received Date</b></td>
       <td><b>Claim Approved Date</b></td>
       <td><b>Insured Person's Name</b></td>
       <td><b>Claimant Name</b></td>
       <td><b>Age</b></td>
       <td><b>Relation</b></td>
       <td><b>Name of Hospital</b></td>
       <td><b>Ailment</b></td>
       <td><b>Dt. of Admission</b></td>
       <td><b>Dt. Of Discharge</b></td>
       <td><b>Total Claim Amt.</b></td>
       <td><b>Total Claimed Amount Currency</b></td>
       <td><b>Converted Total Claimed Amount</b></td>
       <td><b>Converted Total Amount Currency</b></td>
       <td><b>Converted Total Claimed Amount(QAR)</b></td>
       <td><b>Deductions</b></td>
       <td><b>Reasons</b></td>
       <td><b>Amount Payable</b></td>
       
       <td><b>Converted Amount Payable</b></td>
       <td><b>Converted Amount Currency</b></td>
       <td><b>Cheque Amount</b></td>
       <td><b>Amount Paid to Beneficary</b></td>
       <td><b>Amount Paid to Hospital</b></td>
       <td><b>Balance sum insured</b></td>
       <td><b>Sum Insured</b></td>
       <td><b>Deposit To.</b></td>
       <td><b>Employee No.</b></td>
       <td><b>Invoice No.</b></td>
       <td><b>Poilcy Start Date</b></td>
       <td><b>Policy End Date</b></td>
       <td><b>Batch No.</b></td>
       <td><b>Payment Mode</b></td>
       <td><b>Cheque No.</b></td>
       <td><b>Cheque Status.</b></td>
       <td><b>Cheque date.</b></td>
       <td><b>Payee Name</b></td>
       <td><b>Claim No.</b></td>
       <td><b>Transaction Reference No.</b></td>
       <td><b>Partner Name.</b></td>
      </tr>
  
     

<%try{
 %>
	<%String[] temp	=	new String[alClaimPendingData.size()+1];
	 //  System.out.println(alClaimPendingData.size());
	int slno =1;
	for(int i=0;i<alClaimPendingData.size();i++)	{
		temp	=	(String[])alClaimPendingData.get(i);
		
		%><%
		for(int k=6;k<temp.length;k++){
		%>
		      <tr>
		      <td><%=slno%></td>
		      <% 
		      slno++; %>
			  <td> <%=temp[6] %> </td>
			  <td> <%=temp[7] %> </td>
			  <td> <%=temp[8] %> </td>
			  <td> <%=temp[9] %> </td>
			  <td> <%=temp[10] %> </td>
			  <td> <%=temp[11] %> </td>
			  <td> <%=temp[12] %> </td>
			  <td> <%=temp[13] %> </td>
			  <td> <%=temp[14] %> </td>
			  <td> <%=temp[15] %> </td>
			  <td> <%=temp[16] %> </td>
			  <td> <%=temp[17] %> </td>
			  <td> <%=temp[18] %> </td>
			  <td> <%=temp[19] %> </td>
			  <td> <%=temp[20] %> </td>
			  <td> <%=temp[21] %> </td>
			  <td> <%=temp[22] %> </td>
			  <td> <%=temp[23] %> </td>
			  <td> <%=temp[24] %> </td>
			  <td> <%=temp[25] %> </td>
			  <td> <%=temp[26] %> </td>
			  <td> <%=temp[27] %> </td>
			  <td> <%=temp[28] %> </td>
			  <td> <%=temp[29] %> </td>
			 <%--  <td> <%=temp[30] %> </td> --%>
			  <td> <%=temp[30] %> </td>
			  <td> <%=temp[31] %> </td>
			  <td> <%=temp[44] %> </td>
			  <td> <%=temp[33] %> </td>
			  <td> <%=temp[34] %> </td>
			  <td> <%=temp[35] %> </td>
			  <td> <%=temp[36] %> </td>
			  <td> <%=temp[45] %> </td>
			  <td> <%=temp[37] %> </td>
			  <td> <%=temp[38] %> </td>
			  <td> <%=temp[39] %> </td>
			  <td> <%=temp[40] %> </td>
			  <td> <%=temp[46] %> </td>
			  <td> <%=temp[47] %> </td>
			  <td> <%=temp[48] %> </td>
			  <td> <%=temp[49] %> </td>
			  <td> <%=temp[50] %> </td>
			  <td> <%=temp[51] %> </td>
			  <td> <%=temp[43] %> </td>
			  <td> <%=temp[52] %> </td>
			  <td> <%=temp[53] %> </td>
			  </tr>
			 
			 <%
			 Double d = Double.parseDouble(temp[29]);
			 
			 totalamountsum = totalamountsum+ d ;
			 %>
			 <%
			 if(temp[51] != null || temp[51] != "" || temp[51] == "" || temp[51] == null){
				 break;
			 }
			 %>
		<%}
	  %> <%
   }%>



<%}catch(Exception ex)
{
	ex.printStackTrace();
} %>

    <tr><td colspan="6"><font color="blue">Total Amount For </font><%=companyName %> (QAR.)<br/><%=totalamountsum %> </td>
    
    </tr> 
    <tr><td colspan="6"><font color="blue">Total Amount For </font><%=OfficeName %>(QAR.)<br/><%=totalamountsum %> </td>  
      
    <tr><td colspan="6"><font color="blue">Total Count </font><br/><%=alClaimPendingData.size()%></td>
    
    </tr> 
    
    <tr><td colspan="4"><font color="blue">Prepared By </font><br/> </td>
    <td colspan="4"><font color="blue">Verified By </font><br/> </td>
    </tr> 
    
  <% } else { %>
  
  <tr> <td colspan="3"><font color="blue" size="15">No Record Found</font>
  
  <%} %>
     </table>
    </body>
    </html>