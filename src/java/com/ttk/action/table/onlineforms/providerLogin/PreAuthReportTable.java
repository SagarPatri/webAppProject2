package com.ttk.action.table.onlineforms.providerLogin; 

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PreAuthReportTable extends Table{

	
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
          
        //Setting properties for Pre-Approval Number 
        Column colpreApprovalNo = new Column("Pre-Approval Number");//0
        colpreApprovalNo.setMethodName("getPreAuthNo");
        colpreApprovalNo.setColumnWidth("10%");
        colpreApprovalNo.setDBColumnName("PRE_AUTH_NUMBER");
        addColumn(colpreApprovalNo);
        
      //Setting properties for Reference Number 2
        Column colRefNo = new Column("Reference Number");//1
        colRefNo.setMethodName("getReferenceNo");
        colRefNo.setColumnWidth("10%");
        colRefNo.setDBColumnName("PL_PREAUTH_REFNO");
        addColumn(colRefNo);
        
        //Setting properties for event reference number 3
        Column colEventRefNo = new Column("Event Reference Number");//2
        colEventRefNo.setMethodName("getEventReferenceNo");
        colEventRefNo.setColumnWidth("10%");
        colEventRefNo.setDBColumnName("EVENT_NO");
        addColumn(colEventRefNo);
        
      //Setting properties for Pre-Auth Date 4
        Column colPreAuthDate = new Column("Date");//3
        colPreAuthDate.setMethodName("getPreAuthDate");
        colPreAuthDate.setColumnWidth("5%");
        colPreAuthDate.setDBColumnName("PAT_RECEIVED_DATE");
        addColumn(colPreAuthDate);
        
      //Setting properties for Patient Name 5
        Column colPatientName = new Column("Patient Name");//4
        colPatientName.setMethodName("getPatientName");
        colPatientName.setColumnWidth("10%");
        colPatientName.setDBColumnName("MEM_NAME");
        addColumn(colPatientName);
        
        
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
        
      //Setting properties for Benefit Type 8
        Column colBenefitType = new Column("Benefit Type");//7
        colBenefitType.setMethodName("getBenefitType");
        colBenefitType.setColumnWidth("10%");
        colBenefitType.setDBColumnName("BENIFIT_TYPE");
        addColumn(colBenefitType);
        
      //Setting properties for Status 9
        Column colStatus = new Column("Status");//8
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("10%");
        colStatus.setDBColumnName("PAT_STATUS_TYPE_ID");
        addColumn(colStatus);
        
        //Setting properties for Internal code 10
        Column colInternalCode = new Column("Internal Code");//9
        colInternalCode.setMethodName("getInternalCode");
        colInternalCode.setColumnWidth("10%");
        colInternalCode.setDBColumnName("INTERNAL_CODE");
        addColumn(colInternalCode);
        
      //Setting properties for Internal code 11
        Column colDescription = new Column("Description");//10
        colDescription.setMethodName("getDescription");
        colDescription.setColumnWidth("10%");
        colDescription.setDBColumnName("INTERNAL_DESC");
        addColumn(colDescription);
        
        //Setting properties for Total Tariff Amount 12
        Column colTotalTariffAmt = new Column("Total Tariff Amount");//11
        colTotalTariffAmt.setMethodName("getTotalTariffAmt");
        colTotalTariffAmt.setColumnWidth("10%");
        colTotalTariffAmt.setDBColumnName("TOT_GROSS_AMOUNT");
        addColumn(colTotalTariffAmt);
        
        //Setting properties for Discount Amount 13
        Column colDiscountAmt = new Column("Discount Amount");//12
        colDiscountAmt.setMethodName("getDiscountAmt");
        colDiscountAmt.setColumnWidth("10%");
        colDiscountAmt.setDBColumnName("TOT_DISCOUNT_AMOUNT");
        addColumn(colDiscountAmt);
        
        //Setting properties for Pt Share 14
        Column colPtShare= new Column("Pt Share");//13
        colPtShare.setMethodName("getPtShare");
        colPtShare.setColumnWidth("10%");
        colPtShare.setDBColumnName("TOT_PATIENT_SHARE_AMOUNT");
        addColumn(colPtShare);
        
        //Setting properties for Disallowed Amount 15
        Column colDisallowedAmt= new Column("Disallowed Amount");//14
        colDisallowedAmt.setMethodName("getDisallowedAmt");
        colDisallowedAmt.setColumnWidth("10%");
        colDisallowedAmt.setDBColumnName("DIS_ALLOWED_AMOUNT");
        addColumn(colDisallowedAmt);
        
        //Setting properties for Approved Amount 16
        Column colApprovedAmt= new Column("Approved Amount");//15
        colApprovedAmt.setMethodName("getApprovedAmt");
        colApprovedAmt.setColumnWidth("10%");
        colApprovedAmt.setDBColumnName("TOT_APPROVED_AMOUNT");
        addColumn(colApprovedAmt);
        
        //Setting properties for Denial Reason 17
        Column colDenialReason= new Column("Denial Reason");//16
        colDenialReason.setMethodName("getDenialReason");
        colDenialReason.setColumnWidth("10%");
        colDenialReason.setDBColumnName("denial_reason");
        addColumn(colDenialReason);
        
        //Setting properties for Authorization number 18
        Column colAuthorizationNumber= new Column("Authorization Number");//17
        colAuthorizationNumber.setMethodName("getAuthorizationNumber");
        colAuthorizationNumber.setColumnWidth("10%");
        colAuthorizationNumber.setDBColumnName("AUTH_NUMBER");
        addColumn(colAuthorizationNumber);
        
        //Setting properties for Shortfall details 19
        Column colShortfallDetails= new Column("Shortfall Details");//18
        colShortfallDetails.setMethodName("getShortfallDetails");
        colShortfallDetails.setColumnWidth("10%");
        colShortfallDetails.setDBColumnName("shortfall");
        addColumn(colShortfallDetails);
        
	}

}
