package com.ttk.action.table.administration;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ProductRulesTable extends Table{

	public void setTableProperties()
	{
		setRowCount(5000);
		setCurrentPage(1);
		setPageLinkCount(10);

		//Creating From Date object for Revision column
		Column columnFromDate =new Column("From Date ");
		columnFromDate.setMethodName("getStrFromDate");
		columnFromDate.setColumnWidth("50%");
		columnFromDate.setIsLink(true);
		addColumn(columnFromDate);

		// Creating To Date object for Revision column
		Column columnToDate =new Column("To Date ");
		columnToDate.setMethodName("getStrEndDate");
		columnToDate.setColumnWidth("50%");
		addColumn(columnToDate);
		
		
		
		
		//Creating From Date object for Revision column
				Column columnModifiedDate =new Column("Modified Date ");
				columnModifiedDate.setMethodName("getModifiedDate");
				columnModifiedDate.setColumnWidth("50%");
				columnModifiedDate.setIsLink(true);
				addColumn(columnModifiedDate);

				// Creating To Date object for Revision column
				Column columnModifiedBy =new Column("Modified By");
				columnModifiedBy.setMethodName("getModifiedBy");
				columnModifiedBy.setColumnWidth("50%");
				addColumn(columnModifiedBy);
		
		
		
		
	}//end of public void setTableProperties()
}//end of ProductRulesTable.java
