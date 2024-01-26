package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ActivityLogTable extends Table{
	public void setTableProperties()
    {
        setRowCount(500);
        setCurrentPage(1);
        setPageLinkCount(10);
        
//      Setting properties for Log Date
        Column colLogDate = new Column("Log Date");
        colLogDate.setMethodName("getLogDate");
        //colLogDate.setWidth(200);
        colLogDate.setColumnWidth("20%");
        colLogDate.setIsHeaderLink(true);
        colLogDate.setHeaderLinkTitle("Sort by Log Date");
        colLogDate.setDBColumnName("A.ADDED_DATE");
        addColumn(colLogDate);
        
        //Setting properties for Type
        Column colLogType = new Column("Log Type");
        colLogType.setMethodName("getLogTypeDesc");
        colLogType.setColumnWidth("20%");
        colLogType.setIsHeaderLink(true);
        colLogType.setHeaderLinkTitle("Sort by Log Type");
        colLogType.setDBColumnName("C.DESCRIPTION");
        addColumn(colLogType);
        
        Column internalCode = new Column("Internal Code");
        internalCode.setMethodName("getInternalCode");
        internalCode.setColumnWidth("20%");
        internalCode.setIsHeaderLink(true);
        internalCode.setHeaderLinkTitle("Sort by Internal Code");
        internalCode.setDBColumnName("C.internal_code");
        addColumn(internalCode);
        
//      Setting properties for Description
        Column colLogDescription = new Column("Remarks");
        colLogDescription.setMethodName("getLogDesc");
        //colLogDescription.setWidth(600);
        colLogDescription.setColumnWidth("50%");
        colLogDescription.setIsHeaderLink(true);
        colLogDescription.setHeaderLinkTitle("Sort by Remarks");
        colLogDescription.setDBColumnName("REMARKS");
        addColumn(colLogDescription);
        
//      Setting properties for User
        Column colUserName = new Column("User");
        colUserName.setMethodName("getUserName");
        //colUserName.setWidth(200);
        colUserName.setColumnWidth("10%");
        colUserName.setIsHeaderLink(true);
        colUserName.setHeaderLinkTitle("Sort by User");
        colUserName.setDBColumnName("CONTACT_NAME");
        addColumn(colUserName);
        
//      Setting properties for image 
        Column colImage = new Column("");//provide appropriate name
        colImage.setIsImage(true);//to indicate whether it is a image column or not
        colImage.setIsImageLink(false);//to indicate whether it is a clickable image column or not
        //implementation for set/get methods has to be provided in the appropriate value object
        colImage.setImageName("getImageName");
        colImage.setImageTitle("getImageTitle");
        addColumn(colImage);   
        
    }
}
