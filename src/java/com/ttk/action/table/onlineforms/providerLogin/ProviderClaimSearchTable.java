package com.ttk.action.table.onlineforms.providerLogin; 

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ProviderClaimSearchTable extends Table{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {
		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Claim Submitted Date
        Column colDate = new Column("Submission date & time");//1
        colDate.setMethodName("getClaimSubmittedDate");
        colDate.setColumnWidth("10%");
        //colProductName.setIsLink(true);
        colDate.setDBColumnName("CLAIM_SUBMITTED_DATE");
        addColumn(colDate);
        
        //Setting properties for treatment Date
        Column colTreatmentDate = new Column("Treatment Date");//2
        colTreatmentDate.setMethodName("getTreatmentDate");
        colTreatmentDate.setColumnWidth("6%");
        colTreatmentDate.setDBColumnName("TREATMENT_DATE");
        addColumn(colTreatmentDate);
        
        //Setting properties for Batch Number
        Column colBatchNumber = new Column("Batch Number");//3
        colBatchNumber.setMethodName("getBatchNo");
        colBatchNumber.setColumnWidth("10%");
        colBatchNumber.setDBColumnName("BATCH_NUMBER");
        addColumn(colBatchNumber);
        
        
        //Setting properties for Member Id
        Column colMemberId = new Column("Alkoot Id.");//4
        colMemberId.setMethodName("getPatientCardId");
        colMemberId.setColumnWidth("7%");
       // colMemberId.setIsLink(true);
        colMemberId.setDBColumnName("MEMBER_ID");
        addColumn(colMemberId);
        
        
        //Setting properties for Emirate ID
        Column colEmirateID = new Column("Qatar ID");//5
        colEmirateID.setMethodName("getEmirateID");
        colEmirateID.setColumnWidth("7%");
        colEmirateID.setDBColumnName("emirate_id");
        addColumn(colEmirateID);
        
        
        
        
        //Setting properties for Patient Name
        Column colPatientName = new Column("Patient Name");//6      
        colPatientName.setMethodName("getPatientName");
        colPatientName.setColumnWidth("8%");
        colPatientName.setDBColumnName("PATIENT_NAME");
        addColumn(colPatientName);
        
        
      //Setting properties for Claim Number
        Column colClaimNo = new Column("Claim Number");//7
        colClaimNo.setMethodName("getPreAuthNo");
        colClaimNo.setImageName("getStrShortfallImageName");
        colClaimNo.setImageTitle("getStrShortfallImageTitle");
        colClaimNo.setColumnWidth("10%");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimNo);
        
      //Setting properties for Claimed amount
        Column colClaimedAmount = new Column("Claimed amount");//8
        colClaimedAmount.setMethodName("getClaimedAmount");
        colClaimedAmount.setColumnWidth("6%");
        colClaimedAmount.setDBColumnName("CLAIMED_AMOUNT");
        addColumn(colClaimedAmount);
        
        
        //Setting properties for InvoiceNumber
        Column colInvoiceNumber = new Column("Invoice Number");//9
        colInvoiceNumber.setMethodName("getInvoiceNo");
        colInvoiceNumber.setColumnWidth("10%");
        colInvoiceNumber.setDBColumnName("INVOICE_NUMBER");
        addColumn(colInvoiceNumber);
        
        
        //Setting properties for Status
        Column colStatus = new Column("Status");//10
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("6%");
        colStatus.setDBColumnName("STATUS");
        colStatus.setIsLink(true);
        colStatus.setImageName("getInProImageName"); 
        colStatus.setImageTitle("getInProImageTitle");
        addColumn(colStatus);
        
        Column colDecisionDt = new Column("Decision Date & Time.");//11
        colDecisionDt.setMethodName("getDecisionDt");
        colDecisionDt.setColumnWidth("10%");
        colDecisionDt.setDBColumnName("DECISION_DATE");
        addColumn(colDecisionDt);
        
        Column colEventReferenceNo = new Column("Event Reference No.");//12
        colEventReferenceNo.setMethodName("getEventReferenceNo");
        colEventReferenceNo.setColumnWidth("10%");
        colEventReferenceNo.setDBColumnName("event_no");
        addColumn(colEventReferenceNo);
        
	}

}
