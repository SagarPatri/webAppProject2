package com.ttk.action.table.qatarSupport;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class StatusCorrectionTable extends Table {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setTableProperties() {
		 setRowCount(50);
	        setCurrentPage(1);
	        setPageLinkCount(10);
	        
	        
	        //Setting properties for Claim Settlement No.    
	        Column colClaimSettNo = new Column("Claim Settlement No.");
	        colClaimSettNo.setMethodName("getClaimSettleNumber");
	        colClaimSettNo.setColumnWidth("15%");
	        colClaimSettNo.setDBColumnName("claim_settlement_no");
	        addColumn(colClaimSettNo);

	        //Setting properties for Enrollment Id
	        Column colEnrollmentId = new Column("AlKoot Id");
	        colEnrollmentId.setMethodName("getAlkootId");
	        colEnrollmentId.setColumnWidth("15%");
	        colEnrollmentId.setDBColumnName("tpa_enrollment_id");
	        addColumn(colEnrollmentId);
	        
	        //Setting properties for  Qatar ID.
	        Column colCorporateName=new Column("Corporate Name");
	        colCorporateName.setMethodName("getCorporateName");
	        colCorporateName.setColumnWidth("20%");
	        colCorporateName.setDBColumnName("group_name");
	        addColumn(colCorporateName);
	        
	        
	        //Setting properties for Member Name
	        Column colPayee = new Column("Payee Name");
	        colPayee.setMethodName("getPayeeName");
	        colPayee.setColumnWidth("15%");
	        colPayee.setDBColumnName("payee_name");
	        addColumn(colPayee);

	        //Setting properties for Claim Type
	        Column colApprovedAmnt = new Column("Approved Amt In QAR");
	        colApprovedAmnt.setMethodName("getApprovedAmtInQAR");
	        colApprovedAmnt.setColumnWidth("15%");
	        colApprovedAmnt.setDBColumnName("Approved_Amt_QAR");
	        addColumn(colApprovedAmnt);

	        //Setting properties for Corporate Name
	        Column colIncuresCurrency = new Column("Approved Amt in Incurred Currency");
	        colIncuresCurrency.setMethodName("getApprovedAmtIncuredCurrency");
	        colIncuresCurrency.setColumnWidth("20%");
	        colIncuresCurrency.setDBColumnName("Approved_Amt_Incurred_Currency");
	        addColumn(colIncuresCurrency);
	        
	        
	        //Setting properties for check box
	        Column colSelect = new Column("Select");
	        colSelect.setComponentType("checkbox");
	        colSelect.setComponentName("chkopt");
	        //colSelect.setWidth(5);
	        addColumn(colSelect); 
	        

	}

}
