package com.ttk.action.table.fraudcase;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class InternalRemarksInvestigationTable extends Table {

	@Override
	public void setTableProperties() {
		

		
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);        
      //Setting properties for Claim No.
        Column colInvestigationStatus=new Column("Investigation Status");
        colInvestigationStatus.setMethodName("getInvestigationStatusDesc");
        colInvestigationStatus.setColumnWidth("10%");
        colInvestigationStatus.setDBColumnName("inv_status");
        addColumn(colInvestigationStatus);
        
      
        //Setting properties for Claim No.
        Column colInvestOutcomeCategory=new Column("Investigation Outcome Category");
        colInvestOutcomeCategory.setMethodName("getInvestigationOutcomeCatgDesc");
        colInvestOutcomeCategory.setColumnWidth("15%");
        colInvestOutcomeCategory.setDBColumnName("inv_out_category");
        addColumn(colInvestOutcomeCategory);

        //Setting properties for  Member Name.
        Column colRemarks=new Column("Remarks");
        colRemarks.setMethodName("getCfdRemarks");
        colRemarks.setColumnWidth("15%");
        colRemarks.setDBColumnName("CFD_REMARKS");
        addColumn(colRemarks);  
        
        
        
        //Setting properties for  Member Name.
        Column colAmountUtilization=new Column("Amount Utilized On Investigation");
        colAmountUtilization.setMethodName("getAmountutilizationforinvestigation");
        colAmountUtilization.setColumnWidth("10%");
        colAmountUtilization.setDBColumnName("amt_util_for_inv");
        addColumn(colAmountUtilization);
        
     
        //Setting properties for Hospital Name.
        Column colAmountSave=new Column("Amount Saved");
        colAmountSave.setMethodName("getAmountsave");
        colAmountSave.setColumnWidth("6%");
        colAmountSave.setDBColumnName("amount_saved");
        addColumn(colAmountSave);
		
      		 //Koc Decoupling
        Column colInvAddedDate=new Column("Investigation Added Date");
        colInvAddedDate.setMethodName("getInvestigationAddedDate");
        colInvAddedDate.setColumnWidth("10%");
        colInvAddedDate.setDBColumnName("added_date");
        addColumn(colInvAddedDate); 
        
        Column colStatus=new Column("Investigation Start Date");
        colStatus.setMethodName("getInvestmentStartDate");
        colStatus.setColumnWidth("10%");
        colStatus.setDBColumnName("inv_start_date");
        colStatus.setLinkParamMethodName("INVERSTIGATIONSTARTDATE");
        addColumn(colStatus);

        Column coluser=new Column("User");
        coluser.setMethodName("getUser");
        coluser.setColumnWidth("5%");
        coluser.setDBColumnName("added_by");
        addColumn(coluser);

	}

}
