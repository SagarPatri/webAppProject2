package com.ttk.action.table.onlineforms.partnerLogin; 

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;
  
public class PartnerClaimSearchTable extends Table{

	
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
        Column colDate = new Column("Claim Submitted Date & Time");//1
        colDate.setMethodName("getClaimSubmittedDate");
        colDate.setColumnWidth("10%");
        //colProductName.setIsLink(true);
        colDate.setDBColumnName("CLAIM_SUBMITTED_DATE");
        addColumn(colDate);
        
        //Setting properties for treatment Date
        Column colTreatmentDate = new Column("Treatment Date");//2
        colTreatmentDate.setMethodName("getTreatmentDate");
        colTreatmentDate.setColumnWidth("7%");
        colTreatmentDate.setDBColumnName("TREATMENT_DATE");
        addColumn(colTreatmentDate);
        
        //Setting properties for Batch Number
        Column colBatchNumber = new Column("Batch Number");//3
        colBatchNumber.setMethodName("getBatchNo");
        colBatchNumber.setColumnWidth("10%");
        colBatchNumber.setDBColumnName("BATCH_NUMBER");
        addColumn(colBatchNumber);
        
        
        
        
        //Setting properties for Emirate ID
        Column colEmirateID = new Column("Qatar ID");//4
        colEmirateID.setMethodName("getEmirateID");
        colEmirateID.setColumnWidth("7%");
        colEmirateID.setDBColumnName("emirate_id");
        addColumn(colEmirateID);
        
        
        
        
        
        //Setting properties for Member Id
        Column colMemberId = new Column("Alkoot Id.");//5
        colMemberId.setMethodName("getPatientCardId");
        colMemberId.setColumnWidth("7%");
       // colMemberId.setIsLink(true);
        colMemberId.setDBColumnName("MEMBER_ID");
        addColumn(colMemberId);
        
        
        //Setting properties for Patient Name
        Column colPatientName = new Column("Patient Name");//6
        colPatientName.setMethodName("getPatientName");
        colPatientName.setColumnWidth("8%");
        colPatientName.setDBColumnName("PATIENT_NAME");
        addColumn(colPatientName);
        
        
      //Setting properties for Claim Number
        Column colClaimNo = new Column("Claim Number");//7
        colClaimNo.setMethodName("getPreAuthNo");
        colClaimNo.setColumnWidth("9%");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimNo);
        
      //Setting properties for Claimed amount
        Column colClaimedAmount = new Column("Claimed amount");//8
        colClaimedAmount.setMethodName("getClaimedAmount");
        colClaimedAmount.setColumnWidth("5%");
        colClaimedAmount.setDBColumnName("CLAIMED_AMOUNT");
        addColumn(colClaimedAmount);
        
        
        //Setting properties for InvoiceNumber
        Column colInvoiceNumber = new Column("Invoice Number");// 9
        colInvoiceNumber.setMethodName("getInvoiceNo");
        colInvoiceNumber.setColumnWidth("7%");
        colInvoiceNumber.setDBColumnName("INVOICE_NUMBER");
        addColumn(colInvoiceNumber);
        
        
        //Setting properties for Status
        Column colStatus = new Column("Status");//10
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("7%");
        colStatus.setDBColumnName("STATUS");
        colStatus.setIsLink(true);
        addColumn(colStatus);
             
        Column decisionDate = new Column("Decision Date & Time");//11
        decisionDate.setMethodName("getDecisionDtOfClaim");
        decisionDate.setColumnWidth("10%");
        //colProductName.setIsLink(true);
        decisionDate.setDBColumnName("DECISION_DATE");
        addColumn(decisionDate);
        
     //Setting properties for Status
        Column colProvider = new Column("Provider Name");//12
        colProvider.setMethodName("getProviderName");
        colProvider.setColumnWidth("8%");
        colProvider.setDBColumnName("Provider_Name");
        addColumn(colProvider);
        
      
        //Setting properties for Status
        Column colCountry = new Column("Provider Country");//13
        colCountry.setMethodName("getCountryName");
        colCountry.setColumnWidth("10%");
        colCountry.setDBColumnName("Country");
      //  colStatus.setIsLink(true);
        addColumn(colCountry);
        
	}

}
