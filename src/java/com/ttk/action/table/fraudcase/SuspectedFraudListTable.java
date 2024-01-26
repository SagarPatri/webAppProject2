package com.ttk.action.table.fraudcase;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class SuspectedFraudListTable extends Table {

	@Override
	public void setTableProperties() {
		

        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);        
      //Setting properties for Claim No.
        Column colPreauthClaimNo=new Column("Pre-Approval/Claim No.");
        colPreauthClaimNo.setMethodName("getClaimOrPreauthNumber");
        colPreauthClaimNo.setImageName("getPreauthEnhanceImageName");
        colPreauthClaimNo.setImageTitle("getPreauthEnhanceImageTitle");
        colPreauthClaimNo.setColumnWidth("14%");
        colPreauthClaimNo.setIsLink(true);
        colPreauthClaimNo.setDBColumnName("pat_clm_no");
        addColumn(colPreauthClaimNo);
        
      //Setting properties for Claim No.
        Column colIntrRemakrks=new Column("Internal Remarks Status");
        colIntrRemakrks.setMethodName("getInternalRemarkStatus");
        colIntrRemakrks.setColumnWidth("14%");
        colIntrRemakrks.setDBColumnName("internal_remaks_status");
        
        addColumn(colIntrRemakrks);

        //Setting properties for Claim No.
        Column colRiskLevel=new Column("Risk Level");
        colRiskLevel.setMethodName("getRiskLevel");
        colRiskLevel.setColumnWidth("10%");
        colRiskLevel.setIsHeaderLink(true);
       
        colRiskLevel.setDBColumnName("risk_level");
        addColumn(colRiskLevel);

        //Setting properties for  Enrollment Id.
        Column colEnrollmentId=new Column("Al Koot ID");
        colEnrollmentId.setMethodName("getEnrollmentId");
        colEnrollmentId.setColumnWidth("8%");
        colEnrollmentId.setDBColumnName("tpa_enrollment_id");
        addColumn(colEnrollmentId);
        
        
        
        //Setting properties for  Member Name.
        Column colClaimantName=new Column("Member Name");
        colClaimantName.setMethodName("getClaimantName");
        colClaimantName.setColumnWidth("10%");
        colClaimantName.setDBColumnName("mem_name");
        addColumn(colClaimantName);
        
     
        //Setting properties for Hospital Name.
        Column colHospitalName=new Column("Provider Name");
        colHospitalName.setMethodName("getProviderName");
        colHospitalName.setColumnWidth("12%");
        colHospitalName.setDBColumnName("hosp_name");
        addColumn(colHospitalName);
       
       

      //Setting properties for Claim Type.
        Column colClaimType=new Column("Claim Type");
        colClaimType.setMethodName("getClaimType");
        colClaimType.setColumnWidth("10%");
        colClaimType.setDBColumnName("claim_type");
        addColumn(colClaimType);
        
        
        //Setting properties for  Adm. Date.
        Column colPolicyType=new Column("Policy Type");
        colPolicyType.setMethodName("getPolicyType");
        colPolicyType.setColumnWidth("10%");
        colPolicyType.setIsHeaderLink(true);
        colPolicyType.setHeaderLinkTitle("Sort by:Rec. Date");
        colPolicyType.setDBColumnName("pol_type");
        addColumn(colPolicyType);


		
      		 //Koc Decoupling
        Column colStatus=new Column("Claim/Preauth Status");
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("10%");
        colStatus.setDBColumnName("pat_clm_status");
        addColumn(colStatus);        

        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        colSelect.setColumnWidth("1%");
        addColumn(colSelect);
        
    
    

	}

}
