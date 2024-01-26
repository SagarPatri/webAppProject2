package com.ttk.action.table.onlineforms.pbmPharmarcy; 

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class clmDrugDetailsTable extends Table{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * This creates the column properties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        
               
        //Setting properties for Invoice No.
        Column colPreAuthId = new Column("Invoice No.");//0
        colPreAuthId.setMethodName("getInvoiceNo");
        colPreAuthId.setColumnWidth("10%");
        colPreAuthId.setDBColumnName("INVOICE_NUMBER");
       
        addColumn(colPreAuthId);
        
        Column colBatchNo = new Column("Batch No.");//1
        colBatchNo.setMethodName("getBatchNo");
        colBatchNo.setColumnWidth("10%");
        colBatchNo.setDBColumnName("batch_number");
       
        addColumn(colBatchNo);
        
      //Setting properties for Member Id
        Column colMemberId = new Column("Al Koot ID");//2
        colMemberId.setMethodName("getEnrolmentID");
        colMemberId.setColumnWidth("8%");
        colMemberId.setDBColumnName("tpa_enrollment_id");
        addColumn(colMemberId);
        
        
      //Setting properties for Patient Name
        Column colPatientName = new Column("Patient Name");//3
        colPatientName.setMethodName("getMemberName");
        colPatientName.setColumnWidth("10%");
        colPatientName.setDBColumnName("MEM_NAME");
        addColumn(colPatientName);
        
        //Setting properties for Claim Number
        Column colClaimNo= new Column("Claim Number");//4
        colClaimNo.setMethodName("getClaimNo");
        colClaimNo.setColumnWidth("5%");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimNo);
        
        
      //Setting properties for Dispensed Date 
        Column dispDate = new Column("Submission Date");//5
        dispDate.setMethodName("getClaimSubmittedDate");
        dispDate.setColumnWidth("5%");
        //colProductName.setIsLink(true);
        dispDate.setDBColumnName("DISPENSED_DATE");
        addColumn(dispDate);
        
        //Setting properties for Event Ref Number
        Column colEventNo = new Column("Event Ref Number");//6
        colEventNo.setMethodName("getEventRefNo");
        colEventNo.setColumnWidth("5%");
        colEventNo.setDBColumnName("EVENT_REFERENCE_NUMBER");
       
        addColumn(colEventNo);
        
        
      //Setting properties for Drug Description
        Column colDrugDesc = new Column("Drug Description");//7
        colDrugDesc.setMethodName("getDrugDesc");
        colDrugDesc.setColumnWidth("10%");
        colDrugDesc.setDBColumnName("DRUG_DESCRIPTION");
        
        addColumn(colDrugDesc);
        
        
     //Setting properties for Qty
        Column colQty= new Column("Qty");//8
        colQty.setMethodName("getQnty");
        colQty.setColumnWidth("3%");
        colQty.setDBColumnName("QUANTITY");
        
        addColumn(colQty);
        
      //Setting properties for Req Amt
        Column colReqAmt= new Column("Req Amt");//9
        colReqAmt.setMethodName("getTotalReqAmt");
        colReqAmt.setColumnWidth("5%");
        colReqAmt.setDBColumnName("REQUESTED_AMOUNT");
        
        addColumn(colReqAmt);
        
      //Setting properties for Agreed Amt
        Column colAgdAt = new Column("Agreed Amt");//10
        colAgdAt.setMethodName("getAgreedAmt");
        colAgdAt.setColumnWidth("5%");
        //colProductName.setIsLink(true);
        colAgdAt.setDBColumnName("AGRD_AMT");
        addColumn(colAgdAt);
      
      //Setting properties for Disc Amt
        Column colDiscAmt = new Column("Disc Amt");//11
        colDiscAmt.setMethodName("getDiscountAmt");
        colDiscAmt.setColumnWidth("5%");
        //colProductName.setIsLink(true);
        colDiscAmt.setDBColumnName("DISCOUNT_AMOUNT");
        addColumn(colDiscAmt);
        
      //Setting properties for patient Share
        Column colptntAmt= new Column("patient Share");//12
        colptntAmt.setMethodName("getPatientShare");
        colptntAmt.setColumnWidth("5%");
        //colProductName.setIsLink(true);
        colptntAmt.setDBColumnName("PATIENT_SHARE_AMOUNT");
        addColumn(colptntAmt);
        
      //Setting properties for DisAlw Amt
        Column colDisAmt = new Column("DisAlw Amt");//13
        colDisAmt.setMethodName("getDisAlwdAmt");
        colDisAmt.setColumnWidth("3%");
        //colProductName.setIsLink(true);
        colDisAmt.setDBColumnName("DISALLOWED_AMOUNT");
        addColumn(colDisAmt);
      
      //Setting properties for Apprvd Amt
        Column colApprvdAmt = new Column("Apprvd Amt");//14
        colApprvdAmt.setMethodName("getTotalApprAmt");
        colApprvdAmt.setColumnWidth("3%");
        //colProductName.setIsLink(true);
        colApprvdAmt.setDBColumnName("APPROVED_AMOUNT");
        addColumn(colApprvdAmt);
        
        
      //Setting properties for Status
        Column colStatus = new Column("Status");//15
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("5%");
        colStatus.setDBColumnName("status");        
        addColumn(colStatus);
        
        //Setting properties for Denial/Disallowance Reason
        Column colDenial = new Column("Denial/Disallowance Reason");//16
        colDenial.setMethodName("getDenialReason");
        colDenial.setColumnWidth("10%");
        colDenial.setDBColumnName("DENIAL_REASON");
       
        addColumn(colDenial);
        
        Column colpaymentStatus = new Column("Payment Status");//17
        colpaymentStatus.setMethodName("getClmPayStatus");
        colpaymentStatus.setColumnWidth("8%");
        colpaymentStatus.setDBColumnName("clm_payment_status");
        addColumn(colpaymentStatus);
       
       //Setting properties for Payment RefNumber
        Column colPayStatus = new Column("Payment RefNumber");//18
        colPayStatus.setMethodName("getPaymentRefNo");
        colPayStatus.setColumnWidth("3%");
        colPayStatus.setDBColumnName("PAYMENT_REFRENCE_NUMBER");        
        addColumn(colPayStatus);
        
      
        
      
        
     
       
      
        
     
        
	}

}
