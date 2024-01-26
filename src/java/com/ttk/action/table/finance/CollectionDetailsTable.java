package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class CollectionDetailsTable extends Table {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setTableProperties() {

				    setRowCount(10);
			        setCurrentPage(1);
			        setPageLinkCount(10);
			        
			        Column colGroupName=new Column("Invoice No.");
			        colGroupName.setMethodName("getInvoiceNo");
			        colGroupName.setColumnWidth("20%");
			        colGroupName.setIsLink(true);
			        colGroupName.setHeaderLinkTitle("Sort by: Invoice No.");
			        colGroupName.setDBColumnName("INVOICE_NUMBER");
			        addColumn(colGroupName);
			        
			        Column colLineOfBusiness=new Column("Invoice Amount (QAR)");
			        colLineOfBusiness.setMethodName("getInvoiceAmount");
			        colLineOfBusiness.setColumnWidth("10%");
			        colLineOfBusiness.setHeaderLinkTitle("Sort by: Invoice Amount (QAR)");
			        colLineOfBusiness.setDBColumnName("InvoiceAmount");
			        addColumn(colLineOfBusiness);
			        
			        Column colPolicyNo=new Column("Invoice Date");
			        colPolicyNo.setMethodName("getInvoiceDate");
			        colPolicyNo.setColumnWidth("10%");
			        colPolicyNo.setHeaderLinkTitle("Sort by: Invoice Date");
			        colPolicyNo.setDBColumnName("INV_GEN_DATE");
			        addColumn(colPolicyNo);
			        
			        Column colPolicyStartDate=new Column("Due Date");
			        colPolicyStartDate.setMethodName("getDueDate");
			        colPolicyStartDate.setColumnWidth("10%");
			        colPolicyStartDate.setHeaderLinkTitle("Sort by: Due Date");
			        colPolicyStartDate.setDBColumnName("DUE_DATE");
			        addColumn(colPolicyStartDate);
			        
			        Column colPolicyEndDate=new Column("Total Collection (QAR)");
			        colPolicyEndDate.setMethodName("getTotalCollectionsQAR");
			        colPolicyEndDate.setColumnWidth("10%");
			        colPolicyEndDate.setHeaderLinkTitle("Sort by: Total Collection (QAR)");
			        colPolicyEndDate.setDBColumnName("TOT_COLLECTION");
			        addColumn(colPolicyEndDate);
			        
			        Column colTotapPremium=new Column("Total Outstanding (QAR)");
			        colTotapPremium.setMethodName("getTotalOutstandingQAR");
			        colTotapPremium.setColumnWidth("10%");
			        colTotapPremium.setHeaderLinkTitle("Sort by: Total Outstanding (QAR)");
			        colTotapPremium.setDBColumnName("TOT_OUTSTANDING");
			        addColumn(colTotapPremium);
			        
			        Column colTotalCollections=new Column("Members List");
			        colTotalCollections.setMethodName("getMembersList");
			        colTotalCollections.setColumnWidth("10%");
			        colTotalCollections.setIsLink(true);
			        colTotalCollections.setLinkParamName("SecondLink");
			        colTotalCollections.setHeaderLinkTitle("Sort by: Members List");
			        colTotalCollections.setDBColumnName("MembersList");
			        addColumn(colTotalCollections);
			        
			        
			        Column colStatus=new Column("Status");
			        colStatus.setMethodName("getPolicyStatus");
			        colStatus.setColumnWidth("10%");
			        colStatus.setHeaderLinkTitle("Sort by: Status");
			        colStatus.setDBColumnName("Status");
			        addColumn(colStatus);
			        
			        
			        Column colImage3=new Column("Add Collection Entry");
			        colImage3.setMethodName("getAdd");
			        colImage3.setColumnWidth("10%");
			        colImage3.setIsLink(true);
			        colImage3.setLinkParamName("ThirdLink");
			        colImage3.setIsImage(true);
			        colImage3.setIsImageLink(true);
			        colImage3.setImageName("getAddCollectionImage");
			        colImage3.setImageTitle("getAddCollectionImageTitle");
			        colImage3.setVisibility(true);
			        addColumn(colImage3);
			        
			        
			       /* Column colImage4=new Column("Remove Collection Entry");
			        colImage4.setMethodName("getRemove");
			        colImage4.setColumnWidth("7%");
			        colImage4.setIsLink(true);
			        colImage4.setLinkParamName("FourthLink");
			        colImage4.setIsImage(true);
			        colImage4.setIsImageLink(true);
			        colImage4.setImageName("getRemoveCollectionImage");
			        colImage4.setImageTitle("getRemoveCollectionImageTitle");
			        colImage4.setVisibility(true);
			        addColumn(colImage4);
			      */  
			        
			        
}

}
