package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class CollectionsSearchTable extends Table {

	@Override
	public void setTableProperties() {

		 setRowCount(10);
	        setCurrentPage(1);
	        setPageLinkCount(10);
	        
	        Column colGroupName=new Column("Group Name");
	        colGroupName.setMethodName("getGroupName");
	        colGroupName.setColumnWidth("15%");
	        colGroupName.setHeaderLinkTitle("Sort by: Group Name");
	        colGroupName.setDBColumnName("GROUP_NAME");
	        addColumn(colGroupName);
	        
	        Column colLineOfBusiness=new Column("Line of Business");
	        colLineOfBusiness.setMethodName("getLineOfBussiness");
	        colLineOfBusiness.setColumnWidth("10%");
	        colLineOfBusiness.setHeaderLinkTitle("Sort by: Line of Business");
	        colLineOfBusiness.setDBColumnName("LINE_OF_BUSINESS");
	        addColumn(colLineOfBusiness);
	        
	        Column colPolicyNo=new Column("Policy No.");
	        colPolicyNo.setMethodName("getPolicyNum");
	        colPolicyNo.setColumnWidth("11%");
	        colPolicyNo.setIsLink(true);
	        colPolicyNo.setHeaderLinkTitle("Sort by: Policy No.");
	        colPolicyNo.setDBColumnName("POLICY_NUMBER");
	        addColumn(colPolicyNo);
	        
	        Column colPolicyStartDate=new Column("Policy Start Date");
	        colPolicyStartDate.setMethodName("getStartDate");
	        colPolicyStartDate.setColumnWidth("10%");
	        colPolicyStartDate.setHeaderLinkTitle("Sort by: Policy Start Date");
	        colPolicyStartDate.setDBColumnName("EFFECTIVE_FROM_DATE");
	        addColumn(colPolicyStartDate);
	        
	        Column colPolicyEndDate=new Column("Policy End Date");
	        colPolicyEndDate.setMethodName("getEndDate");
	        colPolicyEndDate.setColumnWidth("10%");
	        colPolicyEndDate.setHeaderLinkTitle("Sort by: Policy End Date");
	        colPolicyEndDate.setDBColumnName("EFFECTIVE_TO_DATE");
	        addColumn(colPolicyEndDate);
	        
	        Column colTotapPremium=new Column("Total Premium (QAR)");
	        colTotapPremium.setMethodName("getTotalPremiumQAR");
	        colTotapPremium.setColumnWidth("11%");
	        colTotapPremium.setHeaderLinkTitle("Sort by: Total Premium (QAR)");
	        colTotapPremium.setDBColumnName("TOT_PREMIUM");
	        addColumn(colTotapPremium);
	     
	        
	        Column colTotapInvoicePremium=new Column("Total Invoiced Premium (QAR)");
	        colTotapInvoicePremium.setMethodName("getInvoiceAmount");
	        colTotapInvoicePremium.setColumnWidth("11%");
	        colTotapInvoicePremium.setHeaderLinkTitle("Sort by: Total Invoiced Premium (QAR)");
	        colTotapInvoicePremium.setDBColumnName("TOT_INV_PREMIUM");
	        addColumn(colTotapInvoicePremium);
	        
	        
	        
	        Column colTotalCollections=new Column("Total Collections (QAR)");
	        colTotalCollections.setMethodName("getTotalCollectionsQAR");
	        colTotalCollections.setColumnWidth("11%");
	        colTotalCollections.setHeaderLinkTitle("Sort by: Total Collections (QAR)");
	        colTotalCollections.setDBColumnName("TOT_COLLECTION");
	        addColumn(colTotalCollections);
	        
	        Column colTotalOutstanding=new Column("Total Outstanding (QAR)");
	        colTotalOutstanding.setMethodName("getTotalOutstandingQAR");
	        colTotalOutstanding.setColumnWidth("11%");
	        colTotalOutstanding.setHeaderLinkTitle("Sort by: Total Outstanding (QAR)");
	        colTotalOutstanding.setDBColumnName("TOT_OUTSTANDING");
	        addColumn(colTotalOutstanding);
	        
	        /*Column colSelect = new Column("Select");
			colSelect.setComponentType("checkbox");
			colSelect.setComponentName("chkopt");		
			addColumn(colSelect); 	*/
	        
	        
	        
	        
		
	}

}
