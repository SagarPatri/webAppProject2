package com.ttk.action.table.fraudcase;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PreauthFraudHistoryTable extends Table {

	@Override
	public void setTableProperties() {
		

		
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);        
      //Setting properties for Claim No.
        Column colClaimPreauth=new Column("Pre-Approval No.");
        colClaimPreauth.setMethodName("getClaimOrPreauthNumber");
        colClaimPreauth.setColumnWidth("15%");
        colClaimPreauth.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimPreauth);
        
        Column colInternalRemarksStatus=new Column("Internal Remarks Status");
        colInternalRemarksStatus.setMethodName("getInternalRemarkStatusDesc");
        colInternalRemarksStatus.setColumnWidth("10%");
        colInternalRemarksStatus.setDBColumnName("STATUS_DESC");
        addColumn(colInternalRemarksStatus);

        //Setting properties for  Member Name.
        Column colRiskLevel=new Column("Risk Level");
        colRiskLevel.setMethodName("getRiskLevelDesc");
        colRiskLevel.setColumnWidth("10%");
        colRiskLevel.setDBColumnName("RISK_LEVEL");
        addColumn(colRiskLevel);  
        
        //Setting properties for  Member Name.
        Column colRemarks=new Column("Remarks");
        colRemarks.setMethodName("getRemarksforinternalstatus");
        colRemarks.setColumnWidth("18%");
        colRemarks.setDBColumnName("CODE_REMARKS");
        addColumn(colRemarks);
        
     
        //Setting properties for Hospital Name.
        Column colPrincipleDiagnosis=new Column("Principle/Secondary Diagnosis");
        colPrincipleDiagnosis.setMethodName("getPreSecdiagnosis");
        colPrincipleDiagnosis.setColumnWidth("10%");
        colPrincipleDiagnosis.setDBColumnName("DIAGNOSYS_CODES");
        addColumn(colPrincipleDiagnosis);
		
      		 //Koc Decoupling
        Column colProviderName=new Column("Provider Name");
        colProviderName.setMethodName("getProviderName");
        colProviderName.setColumnWidth("10%");
        colProviderName.setDBColumnName("PROVIDER_NAME");
        addColumn(colProviderName); 
        
        Column colInvestigationStatus=new Column("Investigation Status");
        colInvestigationStatus.setMethodName("getInvestigationStatusDesc");
        colInvestigationStatus.setColumnWidth("10%");
        colInvestigationStatus.setDBColumnName("INV_STATUS");
        addColumn(colInvestigationStatus);
        
        Column colInvestigationOutcomeCategory=new Column("Investigation Outcome Category");
        colInvestigationOutcomeCategory.setMethodName("getInvestigationOutcomeCatgDesc");
        colInvestigationOutcomeCategory.setColumnWidth("10%");
        colInvestigationOutcomeCategory.setDBColumnName("INV_OUT_CATEGORY");
        addColumn(colInvestigationOutcomeCategory);

        Column colInvestigationRemarks=new Column("Investigation Remarks");
        colInvestigationRemarks.setMethodName("getCfdRemarks");
        colInvestigationRemarks.setColumnWidth("15%");
        colInvestigationRemarks.setDBColumnName("CFD_REMARKS");
        addColumn(colInvestigationRemarks);
        
	}

}
