/**
 * @ (#) InsuranceContactTable.javaNov 19, 2005
 * Project      : TTK HealthCare Services
 * File         : InsuranceContactTable.java
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : Nov 19, 2005
 *
 * @author       :  Chandrasekaran J
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * 
 * this class provides the information of insurance contact table
 * 
 */
public class InsuranceContactTable extends Table
{
    /**
     * This creates the columnproperties objects for each and 
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(20);
        setCurrentPage(1);
        setPageLinkCount(20);
        
        //Setting properties for  contact name
        Column colName = new Column("Name");
        colName.setMethodName("getContactName");
        colName.setColumnWidth("20%");
        colName.setIsLink(true);
        colName.setImageName("getImageName");
        colName.setImageTitle("getImageTitle");
        colName.setIsHeaderLink(true);
        colName.setHeaderLinkTitle("Sort by Name");
        colName.setDBColumnName("CONTACT_NAME");
        addColumn(colName);
        
        //Setting properties for contact designation
        Column colDesignation = new Column("Designation");
        colDesignation.setMethodName("getContactDesc");
        colDesignation.setColumnWidth("80%");
        colDesignation.setIsHeaderLink(true);
        colDesignation.setHeaderLinkTitle("Sort by Designation");
        colDesignation.setDBColumnName("DESIGNATION_DESCRIPTION");
        addColumn(colDesignation);
        
        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);
    }//end of setTableProperties()
}// end  of class InsuranceContactTable()
