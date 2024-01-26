/**
 * @ (#) ActivityCodeTable.java June 18, 2015
 * Project      : Project-X
 * File         : ActivityCodeTable.java
 * Author       : Nagababu K
 * Company      : Vidal Health TPA Pvt. Ltd.
 * Date Created : June 18, 2015
 *
 * @author       :  Nagababu K
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;


public class ActivityCodeListTable extends Table
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Code
        Column colActivityCode=new Column("Activity Code");
        colActivityCode.setMethodName("getActivityCode");
        colActivityCode.setColumnWidth("10%");
        colActivityCode.setIsLink(true);
        colActivityCode.setIsHeaderLink(true);      
        colActivityCode.setHeaderLinkTitle("Sort by:Activity Code");
        colActivityCode.setDBColumnName("ACTIVITY_CODE");
        addColumn(colActivityCode);
        
        Column colInternalCode=new Column("Internal Code");
        colInternalCode.setMethodName("getInternalCode");
        colInternalCode.setColumnWidth("10%");
        colInternalCode.setDBColumnName("INTERNAL_CODE");
        addColumn(colInternalCode);
        
        Column colDescription=new Column("Description");
        colDescription.setMethodName("getActivityCodeDesc");
        colDescription.setColumnWidth("70%");
        colDescription.setDBColumnName("ACTIVITY_DESCRIPTION");
        addColumn(colDescription);
        
        
        Column colNetworkType=new Column("Network Type");
        colNetworkType.setMethodName("getNetworkType");
        colNetworkType.setColumnWidth("10%");
        colNetworkType.setDBColumnName("Network_Type");
        addColumn(colNetworkType);
        
        
        
        
/*
        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);*/
    }// end of public void setTableProperties()

}// end of ActivityCodeTable class
