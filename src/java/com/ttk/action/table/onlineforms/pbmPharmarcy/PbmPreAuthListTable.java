package com.ttk.action.table.onlineforms.pbmPharmarcy; 

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PbmPreAuthListTable extends Table{

	
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
        Column colPreApprovId = new Column("Pre-Approval No.");//0
        colPreApprovId.setMethodName("getPreAuthNO");
        colPreApprovId.setColumnWidth("7%");
        colPreApprovId.setDBColumnName("PRE_AUTH_NUMBER");
        colPreApprovId.setIsLink(true);
        addColumn(colPreApprovId);
               
        //Setting properties for Authorization Id
        Column colPreAuthId = new Column("Authorization No.");//0
        colPreAuthId.setMethodName("getAuthorizationNO");
        colPreAuthId.setColumnWidth("10%");
        colPreAuthId.setDBColumnName("AUTH_NUMBER");
        colPreAuthId.setIsLink(true);
        colPreAuthId.setLinkParamName("SecondLink");
        addColumn(colPreAuthId);
        
        
      //Setting properties for DATE
        Column colDate = new Column("Transaction Date & Time");//1
        colDate.setMethodName("getDateOfTreatment");
        colDate.setColumnWidth("10%");
        //colProductName.setIsLink(true);
        colDate.setDBColumnName("HOSPITALIZATION_DATE");
        addColumn(colDate);
        
        
      //Setting properties for Patient Name
        Column colPatientName = new Column("Patient Name");//2
        colPatientName.setMethodName("getMemberName");
        colPatientName.setColumnWidth("20%");
        colPatientName.setDBColumnName("PATIENT_NAME");
        addColumn(colPatientName);
        
        
        //Setting properties for Member Id
        Column colMemberId = new Column("Al Koot ID");//3
        colMemberId.setMethodName("getEnrolmentID");
        colMemberId.setColumnWidth("10%");
        colMemberId.setDBColumnName("MEM_NAME");
        addColumn(colMemberId);
      //Setting properties for Member Id
        Column colEventId = new Column("Event Ref No.");//3
        colEventId.setMethodName("getEventRefNo");
        colEventId.setColumnWidth("7%");
        colEventId.setDBColumnName("MEM_NAME");
        addColumn(colEventId);
        
       //Setting properties for Qatar Id
        Column colQatarId = new Column("Qatar ID");//4
        colQatarId.setMethodName("getQatarID");
        colQatarId.setColumnWidth("10%");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);
        
      //Setting properties for Clinician Name
        Column colClinicianName = new Column("Clinician Name");//5
        colClinicianName.setMethodName("getClinicianName");
        colClinicianName.setColumnWidth("10%");
        colClinicianName.setDBColumnName("CLINICIAN_NAME");
        addColumn(colClinicianName);
        
      
      //Setting properties for Status
        Column colStatus = new Column("Status");//6
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("5%");
        colStatus.setDBColumnName("STATUS");
        colStatus.setImageName("getInProImageName"); 
        colStatus.setImageTitle("getInProImageTitle");
        addColumn(colStatus);
        
        Column decisionDate = new Column("Decision Date & Time");//6
        decisionDate.setMethodName("getDecisionDt");
        decisionDate.setColumnWidth("5%");
        decisionDate.setDBColumnName("DECISION_DATE");        
        addColumn(decisionDate);
        
        Column colAppeal = new Column("Appeal");
        colAppeal.setIsImage(true);
        colAppeal.setIsImageLink(true);
        colAppeal.setImageName("getAppealImageName");
        colAppeal.setImageTitle("getApealImageTitle");
        addColumn(colAppeal);
       
        
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
