package com.ttk.action.table.claims;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ClaimAutoRejectionTable extends Table{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setTableProperties() {

		

        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Reference No
       /* Column colInvoiceNo=new Column("Invoice No.");
        colInvoiceNo.setMethodName("getInvoiceNo");
        colInvoiceNo.setColumnWidth("14%");
        colInvoiceNo.setIsLink(true);
        colInvoiceNo.setIsHeaderLink(true);
        colInvoiceNo.setHeaderLinkTitle("Invoice No.");
        colInvoiceNo.setDBColumnName("XML_SEQ_ID");
        addColumn(colInvoiceNo);*/

        //Setting properties for File Name.
        Column colBatchNo=new Column("Batch No.");
        colBatchNo.setMethodName("getBatchNO");
        colBatchNo.setColumnWidth("14%");
        colBatchNo.setHeaderLinkTitle("Batch No.");
        colBatchNo.setDBColumnName("BATCH_NO");
        addColumn(colBatchNo);

        
        Column colBatchRefNo=new Column("Batch Reference Number");
        colBatchRefNo.setMethodName("getiBatchRefNO");
        colBatchRefNo.setColumnWidth("14%");
        colBatchRefNo.setHeaderLinkTitle("Batch Reference Number");
        colBatchRefNo.setDBColumnName("batch_ref_no");
        addColumn(colBatchRefNo);
        
        
        //Setting properties for Added Date
        Column colClaimNo=new Column("Parent Claim No.");
        colClaimNo.setMethodName("getParentClaimNo");
        colClaimNo.setColumnWidth("13%");
        colClaimNo.setIsLink(true);
        colClaimNo.setIsHeaderLink(true);
        colClaimNo.setHeaderLinkTitle("Parent Claim No.");
        colClaimNo.setDBColumnName("Parent_Claim_No");
        addColumn(colClaimNo);

    
      /*  Column colAlkootId=new Column("Alkoot ID");
        colAlkootId.setMethodName("getAlkootId");
        colAlkootId.setColumnWidth("10%");
        colAlkootId.setHeaderLinkTitle("Alkoot ID");
        colAlkootId.setDBColumnName("BATCH_NO");
        addColumn(colAlkootId);
*/
        //Setting properties for File Name.
        Column colClaimAge=new Column("Claim Age(In days)");
        colClaimAge.setMethodName("getClaimAge");
        colClaimAge.setColumnWidth("10%");
        colClaimAge.setHeaderLinkTitle("Claim Age(In days)");
        colClaimAge.setDBColumnName("claim_age");
        addColumn(colClaimAge);

        //Setting properties for Added Date
        Column colProviderName=new Column("Provider Name");
        colProviderName.setMethodName("getProviderName");
        colProviderName.setColumnWidth("12%");
        colProviderName.setHeaderLinkTitle("Provider Name");
        colProviderName.setDBColumnName("provider_name");
        addColumn(colProviderName);
        
        
      /*  Column colMemberName=new Column("Member Name");
        colMemberName.setMethodName("getMemberName");
        colMemberName.setColumnWidth("11%");
        colMemberName.setHeaderLinkTitle("Member Name");
        colMemberName.setDBColumnName("BATCH_NO");
        addColumn(colMemberName);
        */
        
        
        Column colClaimType=new Column("Claim Type");
        colClaimType.setMethodName("getClaimType");
        colClaimType.setColumnWidth("10%");
        colClaimType.setHeaderLinkTitle("Claim Type");
        colClaimType.setDBColumnName("claim_type");
        addColumn(colClaimType);
        
        Column colRecDate=new Column("Rec. Date");
        colRecDate.setMethodName("getRecDate");
        colRecDate.setColumnWidth("10%");
        colRecDate.setHeaderLinkTitle("Rec. Date");
        colRecDate.setDBColumnName("received_date");
        addColumn(colRecDate);
        
        Column colStatus=new Column("Status");
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("10%");
        colRecDate.setHeaderLinkTitle("Status");
        colStatus.setDBColumnName("status");
        addColumn(colStatus);
		
	}
	
	
	
	
	

}
