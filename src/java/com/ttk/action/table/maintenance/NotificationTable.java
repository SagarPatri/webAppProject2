package com.ttk.action.table.maintenance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class NotificationTable extends Table
{
	public void setTableProperties()
    {
	    setRowCount(20);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        //Setting properties for Notification ID
        Column colNotifyID = new Column("Notification ID");
        colNotifyID.setMethodName("getMsgName");
        colNotifyID.setColumnWidth("30%");
        colNotifyID.setIsLink(true);
        colNotifyID.setLinkTitle("Edit Notification ID");
        colNotifyID.setIsHeaderLink(true);
        colNotifyID.setHeaderLinkTitle("Sort by: Notification ID");
        colNotifyID.setDBColumnName("MSG_NAME");
        addColumn(colNotifyID);
        
        // Setting properties for Notification Category
        Column colNotifyCategory = new Column("Notification Category");
        colNotifyCategory.setMethodName("getNotifCategory");
        colNotifyCategory.setColumnWidth("30%");
        colNotifyCategory.setIsHeaderLink(true);
        colNotifyCategory.setHeaderLinkTitle("Sort by: Notification Category");
        colNotifyCategory.setDBColumnName("NOTIFICATION_CATEGORY");
        addColumn(colNotifyCategory);
    }//end of setTableProperties()
}//end of NotificationTable class
