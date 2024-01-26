package com.ttk.action.table.onlineforms.pbmPharmarcy; 

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PbmClaimListTable extends Table{

	
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

        
               
        //Setting properties for Authorization Id
        Column colPreAuthId = new Column("Invoice No.");//1
        colPreAuthId.setMethodName("getInvoiceNo");
        colPreAuthId.setColumnWidth("9%");
        colPreAuthId.setDBColumnName("INVOICE_NUMBER");
        
        addColumn(colPreAuthId);
        
        
        Column colBatchNo = new Column("Batch Number");//2
        colBatchNo.setMethodName("getBatchNo");
        colBatchNo.setColumnWidth("9%");
        colBatchNo.setDBColumnName("batch_number");
        addColumn(colBatchNo);
        
        
        //Setting properties for Qatar ID
        Column colQatarId = new Column("Qatar ID");//3
        colQatarId.setMethodName("getQatarID");
        colQatarId.setColumnWidth("5%");
        colQatarId.setDBColumnName("emirate_id");
        addColumn(colQatarId);
        
        
        //Setting properties for Member Id
        Column colMemberId = new Column("Al Koot ID");//4
        colMemberId.setMethodName("getEnrolmentID");
        colMemberId.setColumnWidth("6%");
        colMemberId.setDBColumnName("tpa_enrollment_id");
        addColumn(colMemberId);
        
        
        //Setting properties for Patient Name
        Column colPatientName = new Column("Patient Name");//5
        colPatientName.setMethodName("getMemberName");
        colPatientName.setColumnWidth("5%");
        colPatientName.setDBColumnName("MEM_NAME");
        addColumn(colPatientName);
        
      //Setting properties for Authorization Id
        Column colPreApprovId = new Column("Pre-Approval No.");//6
        colPreApprovId.setMethodName("getPreAuthNO");
        colPreApprovId.setColumnWidth("8%");
        colPreApprovId.setDBColumnName("PRE_AUTH_NUMBER");
        
        addColumn(colPreApprovId);
        
        
        //Setting properties for Authorization Id
        Column colAuthId = new Column("Authorization No.");//7
        colAuthId.setMethodName("getAuthorizationNO");
        colAuthId.setColumnWidth("7%");
        colAuthId.setDBColumnName("Auth_number");
        addColumn(colAuthId);
        
        
        
      //Setting properties for DATE
        Column colDate = new Column("Prescription Date");//8
        colDate.setMethodName("getDateOfTreatment");
        colDate.setColumnWidth("5%");
        colDate.setDBColumnName("PRESCRIPTION_DATE");
        addColumn(colDate);
        
      //Setting properties for DATE
        Column dispDate = new Column("Submission Date & Time");//9
        dispDate.setMethodName("getClaimSubmittedDate");
        dispDate.setColumnWidth("5%");
        dispDate.setDBColumnName("DISPENSED_DATE");
        addColumn(dispDate);
        
              
        //Setting properties for Qatar Id
        Column colClaimNo= new Column("Claim Number");//10
        colClaimNo.setMethodName("getClaimNo");
        colClaimNo.setColumnWidth("5%");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        colClaimNo.setIsLink(true);
        addColumn(colClaimNo);
        
        //Setting properties for Drug Description
        Column colDrugDesc = new Column("Drug Description");//11
        colDrugDesc.setMethodName("getDrugDesc");
        colDrugDesc.setColumnWidth("9%");
        colDrugDesc.setDBColumnName("DRUG_DESCRIPTION");
        addColumn(colDrugDesc);
        
        
     //Setting properties for Qty
        Column colQty= new Column("Qty");//12
        colQty.setMethodName("getQnty");
        colQty.setColumnWidth("3%");
        colQty.setDBColumnName("QUANTITY");
         addColumn(colQty);
        
        
      //Setting properties for Req Amt
        Column colReqAmt= new Column("Req Amt");//13
        colReqAmt.setMethodName("getTotalReqAmt");
        colReqAmt.setColumnWidth("3%");
        colReqAmt.setDBColumnName("REQUESTED_AMOUNT");
         addColumn(colReqAmt);
        
      //Setting properties for patient Share
        Column colptntAmt= new Column("patient Share");//14
        colptntAmt.setMethodName("getPatientShare");
        colptntAmt.setColumnWidth("3%");
        colptntAmt.setDBColumnName("PATIENT_SHARE_AMOUNT");
        addColumn(colptntAmt);
        
      //Setting properties for Apprvd Amt
        Column colApprvdAmt = new Column("Apprvd Amt");//15
        colApprvdAmt.setMethodName("getTotalApprAmt");
        colApprvdAmt.setColumnWidth("3%");
        colApprvdAmt.setDBColumnName("APPROVED_AMOUNT");
        addColumn(colApprvdAmt);
        
      //Setting properties for Status
        Column colStatus = new Column("Status");//16
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("3%");
        colStatus.setIsLink(true);
        colStatus.setLinkParamName("SecondLink");
        colStatus.setDBColumnName("status");   
        colStatus.setImageName("getInProImageName"); 
        colStatus.setImageTitle("getInProImageTitle");	 
        addColumn(colStatus);
        
      //Setting properties for Decision Date & Time
        Column decisionDt = new Column("Decision Date & Time");//17
        decisionDt.setMethodName("getDecisionDtOfClaim");
        decisionDt.setColumnWidth("3%");
//        decisionDt.setIsLink(true);
//        decisionDt.setLinkParamName("SecondLink");
        decisionDt.setDBColumnName("DECISION_DATE");        
        addColumn(decisionDt);
        
      //Setting properties for Status
        Column colPayStatus = new Column("Claim Payment Status");//18
        colPayStatus.setMethodName("getClmPayStatus");
        colPayStatus.setColumnWidth("5%");
        colPayStatus.setDBColumnName("payment_status");        
        addColumn(colPayStatus);
        
      //Setting properties for Status
        Column colDispStatus = new Column("Dispenesd Status");//19
        colDispStatus.setMethodName("getDispenseStatus");
        colDispStatus.setColumnWidth("5%");
        colDispStatus.setDBColumnName("Dispensed_status");        
        addColumn(colDispStatus);
      
        
      
        
     
       
      
        
      //Setting properties for Uploaded File
       /* Column colUploadedFile = new Column("Cheque No");//14
        colUploadedFile.setMethodName("getUploadedFile");
        colUploadedFile.setColumnWidth("20%"); 
        colUploadedFile.setIsLink(true);
        colUploadedFile.setLinkParamName("FourthLink");
        colUploadedFile.setDBColumnName("FILE_NAME");
        addColumn(colUploadedFile);*/
        
	}

}
