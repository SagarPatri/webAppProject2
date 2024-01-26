/**
 * @ (#) InvoicesTable.java 26 Oct 2007
 * Project       : TTK HealthCare Services
 * File          : InvoicesTable.java
 * Author        : Yogesh S.C
 * Company       : Span Systems Corporation
 * Date Created  : 26 Oct 2007
 *
 * @author       : Yogesh S.C
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 *  This class provides the information of Invoice details
 *
 */

public class BordereauxTable extends Table{

	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	public void setTableProperties() {
		
		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
       //Setting properties for Invoice No.
//        Column colInvoiceNo = new Column("Master Seqence Id");
//        colInvoiceNo.setMethodName("getSeqID");
//        colInvoiceNo.setColumnWidth("10%");
//        colInvoiceNo.setIsLink(true);
//        11
//        colInvoiceNo.setHeaderLinkTitle("Sort by: Master Seqence Id");
//        colInvoiceNo.setDBColumnName("REPOT_MASTER_SEQ_ID");
//        addColumn(colInvoiceNo);
        
        Column colGroupName  =new Column("Report Name.");
        colGroupName.setMethodName("getReportName");
        colGroupName.setColumnWidth("20%");
        colGroupName.setIsLink(true);
//        colGroupName.setIsHeaderLink(true);
        colGroupName.setHeaderLinkTitle("Sort by: Group Name");
        colGroupName.setDBColumnName("REPORT_NAME");
        addColumn(colGroupName);
        
        Column colPolicyNo  =new Column("Generated Date");
        colPolicyNo.setMethodName("getReportGeneratedDate");
        colPolicyNo.setColumnWidth("20%");
//        colPolicyNo.setIsHeaderLink(true);
        colPolicyNo.setHeaderLinkTitle("Sort by: Generated Date");
        colPolicyNo.setDBColumnName("REPORT_GEN_DATE");
        addColumn(colPolicyNo);
        
        
	}//end of setTableProperties()

}//end of class InvoicesTable
