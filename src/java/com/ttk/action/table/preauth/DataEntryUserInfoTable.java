package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class DataEntryUserInfoTable extends Table{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

	    setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        Column colUsername = new Column("User Name");
        colUsername.setMethodName("getUserName");
       // colUsername.setIsLink(true);
        //colUsername.setLinkTitle("Edit Group Name");
        //colUsername.setIsHeaderLink(true);
        //colUsername.setHeaderLinkTitle("Sort by: Name");
        colUsername.setDBColumnName("user_name");
        addColumn(colUsername);
        
        /*Column colUserID = new Column("User ID");
        colUserID.setMethodName("getUserID");
        colUserID.setHeaderLinkTitle("Sort by: Description");
        colUserID.setDBColumnName("user_id");
        addColumn(colUserID);*/
        
        Column colUserStatus = new Column("User Status");
        colUserStatus.setMethodName("getUserCurStatus");
        colUserStatus.setHeaderLinkTitle("Sort by: Received Time");
        colUserStatus.setDBColumnName("status");
        addColumn(colUserStatus);
            
        
        Column colAssigned = new Column("Assigned Cases");
        colAssigned.setMethodName("getNoOfAssignedCases");
        colAssigned.setHeaderLinkTitle("Sort by: Priority");
        colAssigned.setDBColumnName("assigned ");
        addColumn(colAssigned);
        
        
        Column colCompleted = new Column("Completed Cases");
        colCompleted.setMethodName("getNoOfCompletedCases");
        colCompleted.setHeaderLinkTitle("Sort by: Source");
        colCompleted.setDBColumnName("COMPLETED");
        addColumn(colCompleted);
        
        Column colLocation = new Column("Location");
        colLocation.setMethodName("getOfficeCode");
        colLocation.setHeaderLinkTitle("Sort by: Location");
        colLocation.setDBColumnName("LOCATION");
        addColumn(colLocation);
		
        
        
	}

}
