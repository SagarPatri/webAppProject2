package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PreAuthManaSearchTable extends Table{
	
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setTableProperties()
	    {
		    setRowCount(10);
	        setCurrentPage(1);
	        setPageLinkCount(10);
	        
	        
	        
	      
	    	Column colPreauth = new Column("Pre Auth No.");
	    	colPreauth.setMethodName("getPreAuthNo");
	    	colPreauth.setIsLink(true);
	    	colPreauth.setLinkTitle("Edit Preauth No");
	    	colPreauth.setIsHeaderLink(false);
	    	colPreauth.setHeaderLinkTitle("Sort by: Pre Auth No.");
	    	colPreauth.setDBColumnName("pre_auth_number");
	        addColumn(colPreauth);
	        
	        
	        Column colStatus1 = new Column("Status");
	        colStatus1.setMethodName("getStatus");
	        colStatus1.setHeaderLinkTitle("Sort by: Status");
	        colStatus1.setDBColumnName("status");
	        addColumn(colStatus1);
	        
	        Column colReceivedtime1 = new Column("Received Time");
	        colReceivedtime1.setMethodName("getReceiveDate");
	        colReceivedtime1.setHeaderLinkTitle("Sort by: Received Time");
	        colReceivedtime1.setDBColumnName("received_time");
	        addColumn(colReceivedtime1);
	        
	/*        Column colReceivedDate1 = new Column("Received Time");
	        colReceivedDate1.setMethodName("getReceivedTime");
	        colReceivedDate1.setHeaderLinkTitle("Sort by: Received Time");
	        colReceivedDate1.setDBColumnName("received_date");
	        addColumn(colReceivedDate1);*/

	        Column colUpdatedtime = new Column("Updated  Time");
	        colUpdatedtime.setMethodName("getUpdateTime");
	        colUpdatedtime.setHeaderLinkTitle("Sort by: Received Time");
	        colUpdatedtime.setDBColumnName("updated_date");
	        addColumn(colUpdatedtime);
	        
	        Column colElapsedtime = new Column("Elapsed  Time");
	        colElapsedtime.setMethodName("getElapsedTime");
	        colElapsedtime.setHeaderLinkTitle("Sort by: Received Time");
	        colElapsedtime.setDBColumnName("elapsed_time");
	        addColumn(colElapsedtime);
	            
	        
	        Column colPriority1 = new Column("TAT");
	        colPriority1.setMethodName("getTat");
	        colPriority1.setHeaderLinkTitle("Sort by: Priority");
	        colPriority1.setDBColumnName("tat");
	        addColumn(colPriority1);
	        
	        
	        Column colCorporateName = new Column("Corporate Name");
	        colCorporateName.setMethodName("getCorporateName");
	        colCorporateName.setHeaderLinkTitle("Sort by: Corporate Name");
	        colCorporateName.setDBColumnName("tat");
	        addColumn(colCorporateName);
	        
	        Column colClaimantName = new Column("Alkoot Id");
	        colClaimantName.setMethodName("getEnrollmentID");
	        colClaimantName.setHeaderLinkTitle("Sort by: Alkoot Id");
	        colClaimantName.setDBColumnName("Alkoot_id");
	        addColumn(colClaimantName);
	        
	        Column colReqAmount = new Column("Req.Amount");
	        colReqAmount.setMethodName("getPatRequestedAmount");
	        colReqAmount.setDBColumnName("PAT_REQUESTED_AMOUNT");
	        addColumn(colReqAmount);
	        
	      
	        
	        Column colLocation1 = new Column("Location");
	        colLocation1.setMethodName("getLocation");
	        colLocation1.setHeaderLinkTitle("Sort by: Location");
	        colLocation1.setDBColumnName("location");
	        addColumn(colLocation1);
	        
	        Column colImage3 = new Column("<a href=\"#\" ><img src=\"/ttk/images/UserIcon.gif\" width=\"16\" height=\"16\" border=\"0\"></a>");
	        colImage3.setIsImage(true);
	        colImage3.setIsImageLink(true);
	        colImage3.setImageName("getAssignImageName");
	        colImage3.setImageTitle("getAssignImageTitle");
	        colImage3.setVisibility(true);
	        addColumn(colImage3);
			

	        //Setting properties for check box
	       /* Column colSelect = new Column("Select");
	        colSelect.setComponentType("checkbox");
	        colSelect.setComponentName("chkopt");
	        addColumn(colSelect);*/
	            
	    }
	
	

}
