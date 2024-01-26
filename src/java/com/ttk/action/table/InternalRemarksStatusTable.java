package com.ttk.action.table;

import com.ttk.common.TTKCommon;

public class InternalRemarksStatusTable extends Table {

	@Override
	public void setTableProperties() {
		
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);        
      //Setting properties for Claim No.
        Column colInternalRemarkStatus=new Column("Internal Remarks Status");
        colInternalRemarkStatus.setMethodName("getInternalRemarkStatusDesc");
        colInternalRemarkStatus.setColumnWidth("14%");
        colInternalRemarkStatus.setDBColumnName("int_remk_status");
        addColumn(colInternalRemarkStatus);
        
      
        //Setting properties for Claim No.
        Column colRiskLevel=new Column("Risk Level");
        colRiskLevel.setMethodName("getRiskLevelDesc");
        colRiskLevel.setColumnWidth("14%");
        colRiskLevel.setDBColumnName("risk_level");
        addColumn(colRiskLevel);

      //Setting properties for Risk Remarks
        Column riskRemarks=new Column("Risk Remarks");
        riskRemarks.setMethodName("getRiskRemarks");
        riskRemarks.setColumnWidth("14%");
        riskRemarks.setDBColumnName("risk_remarks");
        addColumn(riskRemarks);
        
        //Setting properties for  Member Name.
        Column colRemarks=new Column("Remarks");
        colRemarks.setMethodName("getRemarksforinternalstatus");
        colRemarks.setColumnWidth("10%");
        colRemarks.setDBColumnName("code_remarks");
        addColumn(colRemarks);
        
     
        //Setting properties for Hospital Name.
        Column colDateAndTime=new Column("Date And Time");
        colDateAndTime.setMethodName("getInternalRemarksAddedDate");
        colDateAndTime.setColumnWidth("12%");
        colDateAndTime.setDBColumnName("code_added_date");
        addColumn(colDateAndTime);
       
       

      //Setting properties for Claim Type.
        Column colUser=new Column("User");
        colUser.setMethodName("getUser");
        colUser.setColumnWidth("10%");
        colUser.setDBColumnName("code_added_by");
        addColumn(colUser);
        
        
              

	}

}
