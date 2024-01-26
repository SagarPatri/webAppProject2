package com.ttk.action.table.ExceptionalReports;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ClaimsDiscountActReportTable extends Table {
	
private static final long serialVersionUID = 1L;

/**
 * This creates the column properties objects for each and
 * every column and adds the column object to the table
 */

	@Override
	public void setTableProperties() {
		// TODO Auto-generated method stub
		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        //Setting properties for event Batch No 1
        Column colBatchNo = new Column("Batch No");//1
        colBatchNo.setMethodName("getBatchNo");
        colBatchNo.setIsHeaderLink(true);
        colBatchNo.setColumnWidth("16%");
        colBatchNo.setHeaderLinkTitle("Sort by:Batch No");
        colBatchNo.setDBColumnName("BATCH_NO");
        addColumn(colBatchNo);
        
      //Setting properties for ClaimNo 2
        Column colClaimNo = new Column("Claim No");//2
        colClaimNo.setMethodName("getClaimNo");
        colClaimNo.setColumnWidth("10%");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        colClaimNo.setIsHeaderLink(true);
        colClaimNo.setHeaderLinkTitle("Sort by:Claim No");
        addColumn(colClaimNo);
        
        
      //Setting properties for Inovice No 3
        Column colInoviceNo = new Column("Invoice No");//3
        colInoviceNo.setMethodName("getInvoiceNo");
        colInoviceNo.setColumnWidth("10%");
        colInoviceNo.setDBColumnName("INVOICE_NUMBER");
        colInoviceNo.setIsHeaderLink(true);
        colInoviceNo.setHeaderLinkTitle("Sort by:Invoice No");
        addColumn(colInoviceNo);
        
        
      //Setting properties for Alkoot Id 4
        Column colAlkootId = new Column("Al Koot ID");//4
        colAlkootId.setMethodName("getAlkootId");
        colAlkootId.setColumnWidth("9%");
        colAlkootId.setDBColumnName("TPA_ENROLLMENT_ID");
        colAlkootId.setIsHeaderLink(true);
        colAlkootId.setHeaderLinkTitle("Sort by:Al Koot ID");
        addColumn(colAlkootId);
        
        
      //Setting properties for Patient Name 5
        Column colPatientName = new Column("Member Name");//5
        colPatientName.setMethodName("getPatientName");
        colPatientName.setColumnWidth("10%");
        colPatientName.setDBColumnName("MEM_NAME");
        colPatientName.setIsHeaderLink(true);
        colPatientName.setHeaderLinkTitle("Sort by:Member Name");
        addColumn(colPatientName);
        
      //Setting properties for Benefit Type 6
        Column colBenefitType = new Column("Benefit Type");//6
        colBenefitType.setMethodName("getBenefitType");
        colBenefitType.setColumnWidth("10%");
        colBenefitType.setDBColumnName("BENIFIT_TYPE");
        colBenefitType.setIsHeaderLink(true);
        colBenefitType.setHeaderLinkTitle("Sort by:Benefit Type");
        addColumn(colBenefitType);
        
        
      //Setting properties for Status 7
        Column colStatus = new Column("Claim Status");//7
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("7%");
        colStatus.setDBColumnName("CLM_STATUS_TYPE_ID");
        colStatus.setIsHeaderLink(true);
        colStatus.setHeaderLinkTitle("Sort by:Claim Status");
        addColumn(colStatus);
        
        //Setting properties for Approved Amount 8
        Column colApprovedAmt= new Column("Approved Amount (QAR)");//8
        colApprovedAmt.setMethodName("getApprovedAmt");
        colApprovedAmt.setColumnWidth("10%");
        colApprovedAmt.setDBColumnName("TOT_APPROVED_AMOUNT");
        addColumn(colApprovedAmt);
        
      //Setting properties for Finance Batch No 9
        Column colFinanceBatchNo= new Column("Finance Batch No");//9
        colFinanceBatchNo.setMethodName("getFinanceBatchNo");
        colFinanceBatchNo.setColumnWidth("9%");
        colFinanceBatchNo.setDBColumnName("finance_batch_no");
        addColumn(colFinanceBatchNo);
        
        //Setting properties for Claim Payment Status 10
        Column colClaimPaymentStatus= new Column("Finance Status");//10
        colClaimPaymentStatus.setMethodName("getClaimPaymentStatus");
        colClaimPaymentStatus.setColumnWidth("13%");
        colClaimPaymentStatus.setDBColumnName("CLAIM_PAYMENT_STATUS");
        colClaimPaymentStatus.setIsHeaderLink(true);
        colClaimPaymentStatus.setHeaderLinkTitle("Sort by:Finance Status");
        addColumn(colClaimPaymentStatus);
	}
}
