/**
 * @ (#) ProductPolicySyncTable.java Aug 3rd, 2007
 * Project      : TTK HealthCare Services
 * File         : ProductPolicySyncTable.java
 * Author       : Raghavendra T M
 * Company      : Span Info Tech
 * Date Created : Aug 3rd, 2007
 *
 * @author       : Raghavendra T M
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.administration;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 *  This class provides the information of Product details
 * 
 */
public class AdminPolicySyncTable extends Table{
    
    /**
     * This creates the columnproperties objects for each and 
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        //Setting properties for Policy Number
        Column colPolicyNbr = new Column("Policy Number");
        colPolicyNbr.setMethodName("getPolicyNbr"); 
        colPolicyNbr.setColumnWidth("32%");
        colPolicyNbr.setIsHeaderLink(true);
        colPolicyNbr.setHeaderLinkTitle("Sort by: Policy Number");
        colPolicyNbr.setDBColumnName("policy_number");
        addColumn(colPolicyNbr);
        
       
        
        //Setting properties for Group Name
        Column colGroupName =new Column("TPA Enrollment Id");
        colGroupName.setMethodName("getTpaEnrollmentId");
        colGroupName.setColumnWidth("32%");
        colGroupName.setIsHeaderLink(true);
        colGroupName.setHeaderLinkTitle("Sort by: Tpa Enrollment Id");
        colGroupName.setDBColumnName("tpa_enrollment_id");    
        addColumn(colGroupName);
        
        Column colInsSynchDate =new Column("Member Name");
        colInsSynchDate.setMethodName("getMemberName");
        colInsSynchDate.setColumnWidth("32%");
        colInsSynchDate.setIsHeaderLink(true);
        colInsSynchDate.setHeaderLinkTitle("Sort by: Member Name");
        colInsSynchDate.setDBColumnName("mem_name");    
        addColumn(colInsSynchDate);
       
        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");       
        addColumn(colSelect); 
    }//end of setTableProperties()

}//end of ProductPolicySyncTable
