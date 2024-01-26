package com.ttk.action.table.maintenance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class AuditDataUploadTable extends Table{

	 public void setTableProperties()
	    {
		    setRowCount(10);
	        setCurrentPage(1);
	        setPageLinkCount(10);
	        
	       
	        Column policyNo = new Column("Policy Number");
	        policyNo.setMethodName("getPolicyNumber");
	        policyNo.setColumnWidth("32%");
	        policyNo.setDBColumnName("POLICY_NUMBER");
	        addColumn(policyNo);
	        
	        Column CorporateName = new Column("Corporate Name"); 
	        CorporateName.setMethodName("getCorporateName");
	        CorporateName.setColumnWidth("32%");
	        CorporateName.setDBColumnName("CORPORATE_NAME");
	        addColumn(CorporateName);
	        
		    Column dateTime = new Column("Uploaded Date"); 
		    dateTime.setMethodName("getAddedDt");
		    dateTime.setColumnWidth("32%");
		    dateTime.setDBColumnName("ADDED_DATE");
	        addColumn(dateTime); 
	     
	        Column colSelect = new Column("Select");
	        colSelect.setComponentType("checkbox");
	        colSelect.setComponentName("chkopt");
	        addColumn(colSelect);
	            
	    }//end of public void setTableProperties()

}//end of class 
