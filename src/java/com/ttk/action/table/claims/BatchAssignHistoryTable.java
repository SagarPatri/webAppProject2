/**
 * @ (#) ClaimBatchListTable.java July 7, 2015
 * Project 	     : ProjectX
 * File          : ClaimBatchListTable.java
 * Author        : Nagababu K
 * Company       : RCS Technologies
 * Date Created  : July 7, 2015
 *
 * @author       :  Nagababu K
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.action.table.claims;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class BatchAssignHistoryTable extends Table
{
	private static final long serialVersionUID = 1L;
	public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Batch No
        Column colAssignTo=new Column("Assign To");
        colAssignTo.setMethodName("getAssignTo");
        colAssignTo.setColumnWidth("25%");
        colAssignTo.setHeaderLinkTitle("Sort by: Assign To");
        colAssignTo.setDBColumnName("ASSIGNED_TO");
        addColumn(colAssignTo);
        //Setting properties for Provider Name.
        Column colAssignDate=new Column("Assign Date");
        colAssignDate.setMethodName("getAssignDate");
        colAssignDate.setColumnWidth("25%");
        colAssignDate.setIsHeaderLink(true);
        colAssignDate.setHeaderLinkTitle("Sort by: Assign Date");
        colAssignDate.setDBColumnName("ASSIGNED_DATE");
        addColumn(colAssignDate);
        
        Column colAssignBy=new Column("Assign By");
        colAssignBy.setMethodName("getAssignBy");
        colAssignBy.setColumnWidth("15%");
        colAssignBy.setIsHeaderLink(true);
        colAssignBy.setHeaderLinkTitle("Sort by: Assign By");
        colAssignBy.setDBColumnName("ASSINGED_BY");
        addColumn(colAssignBy);

        Column colSubmissionType=new Column("Assign Remarks");
        colSubmissionType.setMethodName("getAssignRemarks");
        colSubmissionType.setColumnWidth("15%");
        colSubmissionType.setIsHeaderLink(true);
        colSubmissionType.setHeaderLinkTitle("Sort by: Assign Remarks");
        colSubmissionType.setDBColumnName("ASSIGN_REMARKS");
        addColumn(colSubmissionType);
        
        //Setting properties for  Type of Batch Creation Date
        Column colBatchcreationDate=new Column("Other Remarks");
        colBatchcreationDate.setMethodName("getOtherRemarks");
        colBatchcreationDate.setColumnWidth("15%");
        colBatchcreationDate.setIsHeaderLink(true);
        colBatchcreationDate.setHeaderLinkTitle("Sort by: Other Remarks");
        colBatchcreationDate.setDBColumnName("OTHER_REMARKS");
        addColumn(colBatchcreationDate);
        
    } //end of setTableProperties()
}// end of ClaimBatchListTable