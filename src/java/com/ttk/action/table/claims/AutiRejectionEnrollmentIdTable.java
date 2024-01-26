package com.ttk.action.table.claims;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class AutiRejectionEnrollmentIdTable extends Table{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setTableProperties() {

        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

    
        Column colAlkootId=new Column("Alkoot ID");
        colAlkootId.setMethodName("getAlkootId");
        colAlkootId.setColumnWidth("25%");
        colAlkootId.setIsLink(true);
        colAlkootId.setIsHeaderLink(true);
        colAlkootId.setHeaderLinkTitle("Alkoot ID");
        colAlkootId.setDBColumnName("XML_SEQ_ID");
        addColumn(colAlkootId);

        //Setting properties for File Name.
        Column colPolicyNo=new Column("Policy No");
        colPolicyNo.setMethodName("getClaimAge");
        colPolicyNo.setColumnWidth("25%");
        colPolicyNo.setHeaderLinkTitle("Policy No");
        colPolicyNo.setDBColumnName("FILE_NAME");
        addColumn(colPolicyNo);

        //Setting properties for Added Date
        Column colPolicyStartDate=new Column("Policy StartDate");
        colPolicyStartDate.setMethodName("getProviderName");
        colPolicyStartDate.setColumnWidth("25%");
        colPolicyStartDate.setHeaderLinkTitle("Provider Name");
        colPolicyStartDate.setDBColumnName("ADDED_DATE");
        addColumn(colPolicyStartDate);
        
        
        Column colPolicyEndDate=new Column("Policy EndDate");
        colPolicyEndDate.setMethodName("getMemberName");
        colPolicyEndDate.setColumnWidth("25%");
        colPolicyEndDate.setHeaderLinkTitle("Member Name");
        colPolicyEndDate.setDBColumnName("ADDED_DATE");
        addColumn(colPolicyEndDate);
        
        
		
	}
	
	
	
	
	

}
