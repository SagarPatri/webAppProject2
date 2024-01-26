package com.ttk.action.table.onlineforms.insuranceLogin;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class EmployeeDataTable extends Table{

	
	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(20);

        //Setting properties for ENROLLMENT ID
        Column alkootId = new Column("Al koot ID");
        alkootId.setMethodName("getEnrollmentID");
        alkootId.setColumnWidth("10%");
        alkootId.setIsLink(true);
        alkootId.setDBColumnName("AL_KOOT_ID");
        addColumn(alkootId);

        //Setting properties for Member Name
        Column memberName = new Column("Member Name");
        memberName.setMethodName("getMemName");
        memberName.setColumnWidth("15%");
        memberName.setDBColumnName("MEMBER_NAME");
        addColumn(memberName);
        
      //Setting properties for Member Name
        Column gender = new Column("Gender");
        gender.setMethodName("getGenderTypeID");
        gender.setColumnWidth("8%");
        gender.setDBColumnName("GENDER");
        addColumn(gender);
        
      //Setting properties for Member Name
        Column dateOfBirth = new Column("Date Of Birth");
        dateOfBirth.setMethodName("getMemDateOfBirth");
        dateOfBirth.setColumnWidth("10%");
        dateOfBirth.setDBColumnName("DATE_OF_BIRTH");
        addColumn(dateOfBirth);
        
        Column maritalStatus = new Column("Marital Status");
        maritalStatus.setMethodName("getEmpStatusTypeID");
        maritalStatus.setColumnWidth("10%");
        maritalStatus.setDBColumnName("MARITAL_STATUS");
        addColumn(maritalStatus);
        
        Column memberEnrollmentDate = new Column("Member Enrollment Date");
        memberEnrollmentDate.setMethodName("getEnrollmentDate");
        memberEnrollmentDate.setColumnWidth("13%");
        memberEnrollmentDate.setDBColumnName("POLICY_START_DATE");
        addColumn(memberEnrollmentDate);
        
        Column memberExitDate = new Column("Member Exit Date");
        memberExitDate.setMethodName("getMemExitDate");
        memberExitDate.setColumnWidth("13%");
        memberExitDate.setDBColumnName("POLICY_END_DATE");
        addColumn(memberExitDate);
        
        Column relationship = new Column("Relationship");
        relationship.setMethodName("getRelationship");
        relationship.setColumnWidth("10%");
        relationship.setDBColumnName("RELSHIP_DESCRIPTION");
        addColumn(relationship);
        
        Column status = new Column("Status");
        status.setMethodName("getEmpStatusDesc");
        status.setColumnWidth("11%");
        status.setDBColumnName("EMPLOYEE_STATUS");
        addColumn(status);
	}
	

}
