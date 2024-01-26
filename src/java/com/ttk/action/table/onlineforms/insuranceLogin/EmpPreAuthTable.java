package com.ttk.action.table.onlineforms.insuranceLogin;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class EmpPreAuthTable extends Table{

	
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
        Column memberName = new Column("Pre-Approval Number");
        memberName.setMethodName("getPreAuthNumber");
        memberName.setColumnWidth("13%");
        memberName.setIsHeaderLink(true);
        memberName.setIsLink(true);
        memberName.setLinkParamName("SecondLink");
        memberName.setDBColumnName("PRE_AUTH_NUMBER");
        addColumn(memberName);
        
      //Setting properties for Member Name
        Column gender = new Column("Alkoot ID");
        gender.setMethodName("getEnrollmentID");
        gender.setColumnWidth("11%");
        gender.setIsHeaderLink(true);
        gender.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(gender);
        
      //Setting properties for Member Name
        Column dateOfBirth = new Column("Member Name");
        dateOfBirth.setMethodName("getMemName");
        dateOfBirth.setColumnWidth("18%");
        dateOfBirth.setIsHeaderLink(true);
        dateOfBirth.setDBColumnName("MEM_NAME");
        addColumn(dateOfBirth);
        
        Column maritalStatus = new Column("Provider Name");
        maritalStatus.setMethodName("getProviderName");
        maritalStatus.setColumnWidth("21%");
        maritalStatus.setIsHeaderLink(true);
        maritalStatus.setDBColumnName("HOSP_NAME");
        addColumn(maritalStatus);
        
        Column treatementStartDate = new Column("Treatement Date");
        treatementStartDate.setMethodName("getMemberTreatmentStartDate");
        treatementStartDate.setColumnWidth("15%");
        treatementStartDate.setIsHeaderLink(true);
        treatementStartDate.setDBColumnName("HOSPITALIZATION_DATE");
        addColumn(treatementStartDate);
        
        Column memberEnrollmentDate = new Column("Status");
        memberEnrollmentDate.setMethodName("getStatus");
        memberEnrollmentDate.setColumnWidth("15%");
        memberEnrollmentDate.setIsHeaderLink(true);
        memberEnrollmentDate.setDBColumnName("DESCRIPTION");
        addColumn(memberEnrollmentDate);
	}
}
