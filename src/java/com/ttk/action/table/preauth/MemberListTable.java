package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class MemberListTable extends Table{

	
	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Diagnosis
        Column colMemberId = new Column("Al Koot Id");
        colMemberId.setMethodName("getMemberId");
        colMemberId.setColumnWidth("25%");
        colMemberId.setIsLink(true);
        colMemberId.setDBColumnName("tpa_enrollment_id");
        addColumn(colMemberId);

        //Setting properties for DiagnosisType
        Column colPolicyNo = new Column("Policy No");
        colPolicyNo.setMethodName("getPolicyNumber");
        colPolicyNo.setColumnWidth("25%");
        colPolicyNo.setDBColumnName("policy_number");
        addColumn(colPolicyNo);
		
        Column colPolicySDate = new Column("Policy StartDate");
        colPolicySDate.setMethodName("getPolicyStartDate");
        colPolicySDate.setColumnWidth("25%");
        colPolicySDate.setDBColumnName("start_date");
        addColumn(colPolicySDate);
        
        Column colDiagnosisType = new Column("Policy EndDate");
        colDiagnosisType.setMethodName("getPolicyEndDate");
        colDiagnosisType.setColumnWidth("25%");
        colDiagnosisType.setDBColumnName("end_date");
        addColumn(colDiagnosisType);
	}

}
