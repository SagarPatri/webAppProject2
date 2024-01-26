package com.ttk.action.table.reports;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class FinanceActivityLogTable extends Table{
	public void setTableProperties()
    {
        setRowCount(1000);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        Column TermsIndays = new Column("Payment Agreed Terms( in Days)");
        TermsIndays.setMethodName("getPayTermAgrDays");
        TermsIndays.setColumnWidth("30%");
        TermsIndays.setDBColumnName("PAYMENT_DUR_AGR_DAYS");
        addColumn(TermsIndays);
        
        Column SdateTime = new Column("Start Date/Time");
        SdateTime.setMethodName("getsDateTime");
        SdateTime.setColumnWidth("20%");
        SdateTime.setDBColumnName("START_DATE");
        addColumn(SdateTime);
        
        Column EdateTime = new Column("End Date/Time");
        EdateTime.setMethodName("geteDateTime");
        EdateTime.setColumnWidth("20%");
        EdateTime.setDBColumnName("END_DATE");
        addColumn(EdateTime);
        
        Column UpdatedBy = new Column("Updated by");
        UpdatedBy.setMethodName("getStrUpdatedBy");
        UpdatedBy.setColumnWidth("15%");
        UpdatedBy.setDBColumnName("MODIFIED_BY");
        addColumn(UpdatedBy);
        
        Column Status = new Column("Status");
        Status.setMethodName("getStrStatus");
        Status.setColumnWidth("15%");
        Status.setDBColumnName("STATUS");
        addColumn(Status);
    }
}
