/**
 * @ (#) PolicyGroupTableFinance.javaFeb 2, 2006
 * Project      : TTK HealthCare Services
 * File         : PolicyGroupTableFinance.java
 * Author       : Kishor Kumar S H
 * Company      : RCS
 * Date Created : July 15 2015
 *
 * @author       :  Kishor Kumar S H
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.enrollment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;


public class memberPolicyEnrollmentTable extends Table
{
    /**
     * This creates the columnproperties objects for each and 
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        //Setting properties for Group Id.
        Column enrollmentNo=new Column("Enrollment No");
        enrollmentNo.setMethodName("getEnrollmentID");
        enrollmentNo.setColumnWidth("20%");
        enrollmentNo.setIsLink(true);
        enrollmentNo.setIsHeaderLink(true);
        enrollmentNo.setHeaderLinkTitle("Sort by: Enrollment.");
        enrollmentNo.setDBColumnName("tpa_enrollment_id");
        addColumn(enrollmentNo);
        
        
      //Setting properties for Group Id.
        Column memberName=new Column("Member Name");
        memberName.setMethodName("getMemName");
        memberName.setColumnWidth("20%");
        memberName.setIsLink(true);
        memberName.setIsHeaderLink(true);
        memberName.setHeaderLinkTitle("Sort by: Member Name.");
        memberName.setDBColumnName("mem_name");
        addColumn(memberName);
      
    }// end of setTableProperties() 
}//end of PolicyGroupTableFinance.java
