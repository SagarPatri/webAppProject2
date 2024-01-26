package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class EmpanelmentFilesTable extends Table{
	
	public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for FileName
        Column colFileName = new Column("File Name");
        colFileName.setMethodName("getStrFileName");
        colFileName.setColumnWidth("22%");
        colFileName.setIsHeaderLink(true);
        colFileName.setHeaderLinkTitle("Sort by: File Name");
        colFileName.setIsLink(true);
        colFileName.setLinkTitle("View File");
        colFileName.setDBColumnName("FILE_NAME");
        addColumn(colFileName);      
            
        Column colFileUploadDate = new Column("Upload Date");
        colFileUploadDate.setMethodName("getHrUploadDate");
        colFileUploadDate.setColumnWidth("10%");
        colFileUploadDate.setIsHeaderLink(true);
        colFileUploadDate.setHeaderLinkTitle("Uploaded Date");
       //colFileUploadDate.setIsLink(true);
        colFileUploadDate.setDBColumnName("UPLOAD_DATE");
        addColumn(colFileUploadDate); 
        
        Column policyNo = new Column("Policy No");
        policyNo.setMethodName("getPolicyNbr");
        policyNo.setColumnWidth("10%");
        policyNo.setIsHeaderLink(true);
        policyNo.setHeaderLinkTitle("Policy No");
        policyNo.setDBColumnName("POLICY_NUMBER");
        addColumn(policyNo);
        
        Column fileUploadedFor = new Column("File Uploaded For");
        fileUploadedFor.setMethodName("getFileUploadedFor");
        fileUploadedFor.setColumnWidth("12%");
        fileUploadedFor.setIsHeaderLink(true);
        fileUploadedFor.setHeaderLinkTitle("File Uploaded For");
        fileUploadedFor.setDBColumnName("FILE_UPLOADED_FOR");
        addColumn(fileUploadedFor);
        
        Column uploadedBy = new Column("Uploaded By");
        uploadedBy.setMethodName("getUploadedBy");
        uploadedBy.setColumnWidth("10%");
        uploadedBy.setIsHeaderLink(true);
        uploadedBy.setHeaderLinkTitle("Uploaded By");
        uploadedBy.setDBColumnName("UPLOADED_BY");
        addColumn(uploadedBy);
        
        Column statusChangedDate = new Column("Status Changed Date");
        statusChangedDate.setMethodName("getStatusChangedDate");
        statusChangedDate.setColumnWidth("14%");
        statusChangedDate.setIsHeaderLink(true);
        statusChangedDate.setHeaderLinkTitle("Status Changed Date");
        statusChangedDate.setDBColumnName("STATUS_CHANGED_DATE");
        addColumn(statusChangedDate);
        
        Column status = new Column("Status");
        status.setMethodName("getStrStatus");
        status.setColumnWidth("10%");
        status.setIsHeaderLink(true);
        status.setHeaderLinkTitle("Status");
        status.setDBColumnName("STATUS");
        addColumn(status);
        
        Column statusChangedBy = new Column("Status Changed By");
        statusChangedBy.setMethodName("getStatusChangedBy");
        statusChangedBy.setColumnWidth("23%");
        statusChangedBy.setIsHeaderLink(true);
        statusChangedBy.setHeaderLinkTitle("Status Changed By");
        statusChangedBy.setDBColumnName("STATUS_CHANGED_BY");
        addColumn(statusChangedBy);
        
     //   Setting properties for check box
      	Column colSelect = new Column("Select");
      	colSelect.setComponentType("checkbox");
     	colSelect.setComponentName("chkopt");		
      	addColumn(colSelect);
            
    }//end of  public void setTableProperties()

}
