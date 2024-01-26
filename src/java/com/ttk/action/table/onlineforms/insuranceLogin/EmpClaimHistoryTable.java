package com.ttk.action.table.onlineforms.insuranceLogin;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class EmpClaimHistoryTable extends Table{

	
	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(11);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for ENROLLMENT ID
        Column alkootId = new Column("S. No");
        alkootId.setMethodName("getsNo");
        alkootId.setColumnWidth("5%");
        alkootId.setDBColumnName("AL_KOOT_ID");
        addColumn(alkootId);

        //Setting properties for Member Name
        Column memberName = new Column("Claim Number");
        memberName.setMethodName("getClaimNumber");
        memberName.setColumnWidth("10%");
        memberName.setIsLink(true);
        memberName.setIsHeaderLink(true);
/*        memberName.setLinkParamName("SecondLink");*/
        memberName.setDBColumnName("CLAIM_NUMBER");
        addColumn(memberName);
        
      //Setting properties for Member Name
        Column gender = new Column("Alkoot ID");
        gender.setMethodName("getEnrollmentID");
        gender.setColumnWidth("10%");
        gender.setIsHeaderLink(true);
        gender.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(gender);
        
      //Setting properties for Member Name
        Column dateOfBirth = new Column("Member Name");
        dateOfBirth.setMethodName("getMemName");
        dateOfBirth.setColumnWidth("20%");
        dateOfBirth.setIsHeaderLink(true);
        dateOfBirth.setDBColumnName("MEM_NAME");
        addColumn(dateOfBirth);
        
        Column maritalStatus = new Column("Provider Name");
        maritalStatus.setMethodName("getProviderName");
        maritalStatus.setColumnWidth("18%");
        maritalStatus.setIsHeaderLink(true);
        maritalStatus.setDBColumnName("HOSP_NAME");
        addColumn(maritalStatus);
        
        Column claimType = new Column("Claim Type");
        claimType.setMethodName("getClaim_type");
        claimType.setColumnWidth("12%");
        claimType.setIsHeaderLink(true);
        claimType.setDBColumnName("CLM_TYPE");
        addColumn(claimType);
        
        Column treatmentStartDate = new Column("Treatement Date");
        treatmentStartDate.setMethodName("getMemberTreatmentStartDate");
        treatmentStartDate.setColumnWidth("15%");
        treatmentStartDate.setIsHeaderLink(true);
        treatmentStartDate.setDBColumnName("TREATMENT_DATE");
        addColumn(treatmentStartDate);
        
        Column memberEnrollmentDate = new Column("Status");
        memberEnrollmentDate.setMethodName("getStatus");
        memberEnrollmentDate.setColumnWidth("10%");
        memberEnrollmentDate.setIsHeaderLink(true);
        memberEnrollmentDate.setDBColumnName("STATUS");
        addColumn(memberEnrollmentDate);
     
	}
	

}
