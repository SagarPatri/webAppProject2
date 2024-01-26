/**
 * @ (#) HospitalLogTable.java Sep 26, 2005
 * Project      : TTK HealthCare Services
 * File         : HospitalLogTable.java
 * Author       : Arun K N
 * Company      : Span Systems Corporation
 * Date Created : Sep 26, 2005
 *
 * @author       :  Arun K N
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * 
 * 
 */
public class HospitalLogTable extends Table {
    
    /**
     * This creates the column properties objects for each and 
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(1000);
        setCurrentPage(1);
        setPageLinkCount(10);
        
//      Setting properties for Log Date
        Column colLogDate = new Column("Log Date");
        colLogDate.setMethodName("getLogDate");
        //colLogDate.setWidth(200);
        colLogDate.setColumnWidth("20%");
      //  colLogDate.setIsHeaderLink(true);
       // colLogDate.setHeaderLinkTitle("Sort by Log Date");
        colLogDate.setDBColumnName("log_date");
        addColumn(colLogDate);
        
        //Setting properties for Type
        Column colLogType = new Column("Log Type");
        colLogType.setMethodName("getLogTypeDesc");
        colLogType.setColumnWidth("20%");
       // colLogType.setIsHeaderLink(true);
       // colLogType.setHeaderLinkTitle("Sort by Log Type");
        colLogType.setDBColumnName("log_type");
        addColumn(colLogType);
        
        /*Column internalCode = new Column("Log Type");
        colLogType.setMethodName("getInternalCode");
        colLogType.setColumnWidth("20%");
        colLogType.setIsHeaderLink(true);
        colLogType.setHeaderLinkTitle("Sort by Log Type");
        colLogType.setDBColumnName("C.DESCRIPTION");
        addColumn(internalCode);*/
        
//      Setting properties for Description
        Column colLogDescription = new Column("Remarks");
        colLogDescription.setMethodName("getLogDesc");
        //colLogDescription.setWidth(600);
        colLogDescription.setColumnWidth("50%");
       // colLogDescription.setIsHeaderLink(true);
       // colLogDescription.setHeaderLinkTitle("Sort by Remarks");
        colLogDescription.setDBColumnName("remarks");
        addColumn(colLogDescription);
        
//      Setting properties for User
        Column colUserName = new Column("User");
        colUserName.setMethodName("getUserName");
        //colUserName.setWidth(200);
        colUserName.setColumnWidth("10%");
       // colUserName.setIsHeaderLink(true);
      //  colUserName.setHeaderLinkTitle("Sort by User");
        colUserName.setDBColumnName("user_name");
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
