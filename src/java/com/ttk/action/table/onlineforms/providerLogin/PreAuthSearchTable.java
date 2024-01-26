package com.ttk.action.table.onlineforms.providerLogin; 

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PreAuthSearchTable extends Table{

	
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

        
        //Setting properties for PreAuth Number
        Column colPreAuthNo = new Column("Pre-Approval No.");//0
        colPreAuthNo.setMethodName("getPreAuthNo");
        colPreAuthNo.setColumnWidth("10%");
        colPreAuthNo.setImageName("getStrShortfallImageName");
        colPreAuthNo.setImageTitle("getStrShortfallImageTitle");
        colPreAuthNo.setImageName("getPreauthEnhanceImageName");
        colPreAuthNo.setImageTitle("getPreauthEnhanceImageTitle");
        colPreAuthNo.setDBColumnName("PRE_AUTH_NUMBER");
        addColumn(colPreAuthNo);
        
        
        //Setting properties for PreAuth Number
        Column colPreAuthRefNo = new Column("Reference No.");//1
        colPreAuthRefNo.setMethodName("getPreAuthRefNo");
        colPreAuthRefNo.setColumnWidth("10%");
        colPreAuthRefNo.setIsLink(true);
        colPreAuthRefNo.setLinkParamName("ThirdLink");
        colPreAuthRefNo.setDBColumnName("PRE_AUTH_REF_NUMBER");
        addColumn(colPreAuthRefNo);
        
        
      //Setting properties for Claim Number
        Column colClaimNo = new Column("Claim Number");//2
        colClaimNo.setMethodName("getPreAuthNo");
        colClaimNo.setColumnWidth("15%");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimNo);
        
        
        //Setting properties for DATE
        Column colDate = new Column("Submission date & time");//3
        colDate.setMethodName("getReceivedDate");
        colDate.setColumnWidth("15%");
        //colProductName.setIsLink(true);
        colDate.setDBColumnName("DATE");
        addColumn(colDate);
        
        
      //Setting properties for DATE
        Column colPatientName = new Column("Patient Name");//4
        colPatientName.setMethodName("getPatientName");
        colPatientName.setColumnWidth("15%");
        colPatientName.setDBColumnName("PATIENT_NAME");
        addColumn(colPatientName);
        
      //Setting properties for BenefitType
        Column colPatientCardId = new Column("Alkoot ID");//5
        colPatientCardId.setMethodName("getPatientCardId");
        colPatientCardId.setColumnWidth("10%");
        colPatientCardId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colPatientCardId);
        
      //Setting properties for Emirate ID
        Column colEmirateID = new Column("Qatar ID");//6
        colEmirateID.setMethodName("getEmirateID");
        colEmirateID.setColumnWidth("10%");
        colEmirateID.setDBColumnName("Emirate_ID");
        addColumn(colEmirateID);
        
      //Setting properties for BenefitType
        Column colBenefitType = new Column("Benefit Type ");//7
        colBenefitType.setMethodName("getBenefitType");
        colBenefitType.setColumnWidth("10%");
        colBenefitType.setDBColumnName("BENEFIT_TYPE");
        addColumn(colBenefitType);
        
      //Setting properties for tRAETING dOCTOR
        Column colTreatingDoctor = new Column("Treating Doctor");//8
        colTreatingDoctor.setMethodName("getTreatingDoctor");
        colTreatingDoctor.setColumnWidth("10%");
        colTreatingDoctor.setDBColumnName("TREATING_DOCTOR");
        addColumn(colTreatingDoctor);
        
      //Setting properties for Status
        Column colStatus = new Column("Status");//9
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("10%");
        colStatus.setDBColumnName("STATUS");
        colStatus.setImageName("getInProImageName"); 
        colStatus.setImageTitle("getInProImageTitle");
        //colStatus.setIsLink(true);
        addColumn(colStatus);
        
      //Setting properties for Status
        Column colClaimStatus = new Column("Status");//10
        colClaimStatus.setMethodName("getStatus");
        colClaimStatus.setColumnWidth("10%");
        colClaimStatus.setDBColumnName("STATUS");
        addColumn(colClaimStatus);
        
        Column colDecisionDt = new Column("Decision Date & Time.");//11
        colDecisionDt.setMethodName("getDecisionDt");
        colDecisionDt.setColumnWidth("10%");
        colDecisionDt.setDBColumnName("DECISION_DATE");
        addColumn(colDecisionDt);
        
      //Setting properties for ApprovalNumber
        Column colApprovalNumber = new Column("Authorization Number");//12
        colApprovalNumber.setMethodName("getApprovalNo");
        colApprovalNumber.setColumnWidth("15%");
        colApprovalNumber.setDBColumnName("APPROVAL_NUMBER");
        colApprovalNumber.setIsLink(true);
        colApprovalNumber.setLinkParamName("SecondLink");
        addColumn(colApprovalNumber);
        
        Column colClaimApprovalNumber = new Column("Approval Number");//13
        colClaimApprovalNumber.setMethodName("getApprovalNo");
        colClaimApprovalNumber.setColumnWidth("20%");
        colClaimApprovalNumber.setDBColumnName("APPROVAL_NUMBER");
        addColumn(colClaimApprovalNumber);
        
        
      //Setting properties for InvoiceNumber
        Column colInvoiceNumber = new Column("Invoice Number");//14
        colInvoiceNumber.setMethodName("getInvoiceNo");
        colInvoiceNumber.setColumnWidth("20%");
        colInvoiceNumber.setDBColumnName("INVOICE_NUMBER");
        addColumn(colInvoiceNumber);
        
      //Setting properties for BatchNumber
        Column colBatchNumber = new Column("Batch Number");//15
        colBatchNumber.setMethodName("getBatchNo");
        colBatchNumber.setColumnWidth("20%");
        colBatchNumber.setDBColumnName("BATCH_NUMBER");
        addColumn(colBatchNumber);
        
      //Setting properties for ClaimType
        Column colClaimType = new Column("Claim Type");//16
        colClaimType.setMethodName("getClaimType");
        colClaimType.setColumnWidth("20%");
        colClaimType.setDBColumnName("CLAIM_TYPE");
        addColumn(colClaimType);
        
      //Setting properties for Cheque No
        Column colChequeNo = new Column("Cheque No");//16
        colChequeNo.setMethodName("getChequeNo");
        colChequeNo.setColumnWidth("20%");
        colChequeNo.setDBColumnName("CHEQUE_NO");
        addColumn(colChequeNo);
        
        Column colEventReferenceNo = new Column("Event Reference No.");//13
        colEventReferenceNo.setMethodName("getEventReferenceNo");
        colEventReferenceNo.setColumnWidth("20%");
        colEventReferenceNo.setDBColumnName("event_no");
        addColumn(colEventReferenceNo);
        
    //Setting properties for PreAuth Enhancement
        Column colImage = new Column("Enhance");
        colImage.setMethodName("getEnhance");
        colImage.setColumnWidth("5%");
        colImage.setIsImage(true);
        colImage.setIsImageLink(true);
        colImage.setImageName("getEnhanceImageName");
        colImage.setImageTitle("getEnhanceImageTitle");
        colImage.setDBColumnName("ENHANCE_YN");
        addColumn(colImage);
        
      //Setting properties for Uploaded File
        /* Column colUploadedFile = new Column("Cheque No");//14
         colUploadedFile.setMethodName("getUploadedFile");
         colUploadedFile.setColumnWidth("20%"); 
         colUploadedFile.setIsLink(true);
         colUploadedFile.setLinkParamName("FourthLink");
         colUploadedFile.setDBColumnName("FILE_NAME");
         addColumn(colUploadedFile);*/
        
        Column colAppeal = new Column("Appeal");//20
        colAppeal.setIsImage(true);
        colAppeal.setIsImageLink(true);
        colAppeal.setImageName("getAppealImageName");
        colAppeal.setImageTitle("getApealImageTitle");
        addColumn(colAppeal);
	}

}
