package com.ttk.action.table.onlineforms.pbmPharmarcy;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PBMClaimsReportTable extends Table {
	
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
          
        //Setting properties for Submitted Date 1
        Column colsubmittedDate = new Column("Submitted Date");//0
        colsubmittedDate.setMethodName("getSubmittedDate");
        colsubmittedDate.setColumnWidth("10%");
        colsubmittedDate.setDBColumnName("CLM_RECEIVED_DATE");
        addColumn(colsubmittedDate);
        
      //Setting properties for Treatment Date 2
        Column colTreatmentDate = new Column("Treatment Date");//1
        colTreatmentDate.setMethodName("getTreatmentDate");
        colTreatmentDate.setColumnWidth("10%");
        colTreatmentDate.setDBColumnName("DATE_OF_HOSPITALIZATION");
        addColumn(colTreatmentDate);
        
        //Setting properties for event Batch No 3
        Column colBatchNo = new Column("Batch No");//2
        colBatchNo.setMethodName("getBatchNo");
        colBatchNo.setColumnWidth("10%");
        colBatchNo.setDBColumnName("BATCH_NO");
        addColumn(colBatchNo);
        
      //Setting properties for Inovice No 4
        Column colInoviceNo = new Column("Invoice No");//3
        colInoviceNo.setMethodName("getInvoiceNo");
        colInoviceNo.setColumnWidth("5%");
        colInoviceNo.setDBColumnName("INVOICE_NUMBER");
        addColumn(colInoviceNo);
        
      //Setting properties for EventRefNo 5
        Column colEventRefNo = new Column("Event Ref No");//4
        colEventRefNo.setMethodName("getEventRefNo");
        colEventRefNo.setColumnWidth("10%");
        colEventRefNo.setDBColumnName("EVENT_NO");
        addColumn(colEventRefNo);
        
        
        //Setting properties for Alkoot Id 6
        Column colAlkootId = new Column("Alkoot ID");//5
        colAlkootId.setMethodName("getAlkootId");
        colAlkootId.setColumnWidth("10%");
        colAlkootId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colAlkootId);
        
        //Setting properties for Qatar Id 7
        Column colQatarId = new Column("Qatar ID");//6
        colQatarId.setMethodName("getQatarId");
        colQatarId.setColumnWidth("10%");
        colQatarId.setDBColumnName("EMIRATE_ID");
        addColumn(colQatarId);
        
      //Setting properties for Patient Name 8
        Column colPatientName = new Column("Patient Name");//7
        colPatientName.setMethodName("getPatientName");
        colPatientName.setColumnWidth("10%");
        colPatientName.setDBColumnName("MEM_NAME");
        addColumn(colPatientName);
        
      //Setting properties for ClaimNo 9
        Column colClaimNo = new Column("Claim NO");//8
        colClaimNo.setMethodName("getClaimNo");
        colClaimNo.setColumnWidth("10%");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimNo);
        
      //Setting properties for Benefit Type 10
        Column colBenefitType = new Column("Benefit Type");//9
        colBenefitType.setMethodName("getBenefitType");
        colBenefitType.setColumnWidth("10%");
        colBenefitType.setDBColumnName("BENIFIT_TYPE");
        addColumn(colBenefitType);
        
      //Setting properties for Status 11
        Column colStatus = new Column("Status");//10
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("10%");
        colStatus.setDBColumnName("CLM_STATUS_TYPE_ID");
        addColumn(colStatus);
        
        //Setting properties for Internal code 12
        Column colInternalCode = new Column("Internal Code");//11
        colInternalCode.setMethodName("getInternalCode");
        colInternalCode.setColumnWidth("10%");
        colInternalCode.setDBColumnName("INTERNAL_CODE");
        addColumn(colInternalCode);
        
      //Setting properties for Internal code 13
        Column colDescription = new Column("Description");//12
        colDescription.setMethodName("getDescription");
        colDescription.setColumnWidth("10%");
        colDescription.setDBColumnName("INTERNAL_DESC");
        addColumn(colDescription);
        
      //Setting properties for Claimed Amt 14
        Column colClaimedAmt = new Column("Claimed Amount");//13
        colClaimedAmt.setMethodName("getClaimedAmt");
        colClaimedAmt.setColumnWidth("10%");
        colClaimedAmt.setDBColumnName("PAT_APPROVED_AMOUNT");
        addColumn(colClaimedAmt);
        
        //Setting properties for Tariff Amount 15
        Column colTotalTariffAmt = new Column("Tariff Amount");//14
        colTotalTariffAmt.setMethodName("getTariffAmt");
        colTotalTariffAmt.setColumnWidth("10%");
        colTotalTariffAmt.setDBColumnName("TOT_GROSS_AMOUNT");
        addColumn(colTotalTariffAmt);
        
        //Setting properties for Discount Amount 16
        Column colDiscountAmt = new Column("Discount Amount");//15
        colDiscountAmt.setMethodName("getDiscountAmt");
        colDiscountAmt.setColumnWidth("10%");
        colDiscountAmt.setDBColumnName("TOT_DISCOUNT_AMOUNT");
        addColumn(colDiscountAmt);
        
        //Setting properties for Pt Share 17
        Column colPtShare= new Column("Pt Share");//16
        colPtShare.setMethodName("getPtShare");
        colPtShare.setColumnWidth("10%");
        colPtShare.setDBColumnName("TOT_PATIENT_SHARE_AMOUNT");
        addColumn(colPtShare);
        
        //Setting properties for Disallowed Amount 18
        Column colDisallowedAmt= new Column("Disallowed Amount");//17
        colDisallowedAmt.setMethodName("getDisallowedAmt");
        colDisallowedAmt.setColumnWidth("10%");
        colDisallowedAmt.setDBColumnName("DISALLOWED_AMOUNT");
        addColumn(colDisallowedAmt);
        
        //Setting properties for Approved Amount 19
        Column colApprovedAmt= new Column("Approved Amount");//18
        colApprovedAmt.setMethodName("getApprovedAmt");
        colApprovedAmt.setColumnWidth("10%");
        colApprovedAmt.setDBColumnName("TOT_APPROVED_AMOUNT");
        addColumn(colApprovedAmt);
        
        //Setting properties for Denial Reason 20
        Column colDenialReason= new Column("Denial Reason");//19
        colDenialReason.setMethodName("getDenialReason");
        colDenialReason.setColumnWidth("10%");
        colDenialReason.setDBColumnName("DENIAL_DESC");
        addColumn(colDenialReason);
        
        //Setting properties for Authorization number 21
        Column colPaymentRefNumber= new Column("Payment Reference No");//20
        colPaymentRefNumber.setMethodName("getPaymentRefNo");
        colPaymentRefNumber.setColumnWidth("10%");
        colPaymentRefNumber.setDBColumnName("PAYMENT_REFRENCE_NUMBER");
        addColumn(colPaymentRefNumber);
        
        //Setting properties for Pay Date 22
        Column colPayDate= new Column("Pay Date");//21
        colPayDate.setMethodName("getPayDate");
        colPayDate.setColumnWidth("10%");
        colPayDate.setDBColumnName("CHECK_DATE");
        addColumn(colPayDate);
        
        //Setting properties for Claim Payment Status 23
        Column colClaimPaymentStatus= new Column("Payment Status");//22
        colClaimPaymentStatus.setMethodName("getClaimPaymentStatus");
        colClaimPaymentStatus.setColumnWidth("10%");
        colClaimPaymentStatus.setDBColumnName("CLAIM_PAYMENT_STATUS");
        addColumn(colClaimPaymentStatus);
	}
}
