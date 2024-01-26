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

public class ClaimBatchAssignHistoryTable extends Table
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5197596089165070753L;

	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Batch No
        Column colBatchNo=new Column("Batch No");
        colBatchNo.setMethodName("getBatchNO");
        colBatchNo.setColumnWidth("20%");
        colBatchNo.setIsHeaderLink(true);
        colBatchNo.setHeaderLinkTitle("Sort by: Batch No");
        colBatchNo.setDBColumnName("BATCH_NO");
        addColumn(colBatchNo);
        
        //Setting properties for Provider Name.
        Column colProviderName=new Column("Provider Name");
        colProviderName.setMethodName("getProviderName");
        colProviderName.setColumnWidth("20%");
        colProviderName.setIsHeaderLink(true);
        colProviderName.setHeaderLinkTitle("Sort by: Provider Name");
        colProviderName.setDBColumnName("HOSP_NAME");
        addColumn(colProviderName);
        
        Column colClaimType=new Column("Claim Type");
        colClaimType.setMethodName("getClaimType");
        colClaimType.setColumnWidth("10%");
        colClaimType.setIsHeaderLink(true);
        colClaimType.setHeaderLinkTitle("Sort by: Claim Type");
        colClaimType.setDBColumnName("CLAIM_TYPE");
        addColumn(colClaimType);

        Column colSubmissionType=new Column("Submission Type");
        colSubmissionType.setMethodName("getSubmissionType");
        colSubmissionType.setColumnWidth("10%");
        colSubmissionType.setIsHeaderLink(true);
        colSubmissionType.setHeaderLinkTitle("Sort by: Submission Type");
        colSubmissionType.setDBColumnName("SUBMISSION_TYPE");
        addColumn(colSubmissionType);
        
        //Setting properties for Batch No
        Column colAssignTo=new Column("Assign To");
        colAssignTo.setMethodName("getAssignTo");
        colAssignTo.setColumnWidth("10%");
        colAssignTo.setHeaderLinkTitle("Sort by: Assign To");
        colAssignTo.setDBColumnName("ASSIGNED_TO");
        addColumn(colAssignTo);
        
        //Setting properties for Provider Name.
        Column colAssignDate=new Column("Assign Date");
        colAssignDate.setMethodName("getAssignDate");
        colAssignDate.setColumnWidth("10%");
        colAssignDate.setIsHeaderLink(true);
        colAssignDate.setHeaderLinkTitle("Sort by: Assign Date");
        colAssignDate.setDBColumnName("ASSIGNED_DATE");
        addColumn(colAssignDate);
        
        Column colAssignBy=new Column("Assign By");
        colAssignBy.setMethodName("getAssignBy");
        colAssignBy.setColumnWidth("10%");
        colAssignBy.setIsHeaderLink(true);
        colAssignBy.setHeaderLinkTitle("Sort by: Assign By");
        colAssignBy.setDBColumnName("ASSINGED_BY");
        addColumn(colAssignBy);
        
    } //end of setTableProperties()
}// end of ClaimBatchListTable