package com.ttk.action.table.onlineforms.insuranceLogin;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class EmpShortfallTable extends Table{

	
	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(11);
        setCurrentPage(1);
        setPageLinkCount(1000);

        //Setting properties for ENROLLMENT ID
        Column dateOfShortfall = new Column("Date Of Shortfall");
        dateOfShortfall.setMethodName("getDateOfShortfall");
        dateOfShortfall.setColumnWidth("20%");
        dateOfShortfall.setDBColumnName("SENT_DATE");
        addColumn(dateOfShortfall);

        //Setting properties for Member Name
        Column shortfallNo = new Column("Shortfall No.");
        shortfallNo.setMethodName("getShortfallNo");
        shortfallNo.setColumnWidth("15%");
        shortfallNo.setIsLink(true);
        shortfallNo.setLinkParamName("SecondLink");
        shortfallNo.setDBColumnName("SHORTFALL_ID");
        addColumn(shortfallNo);
        
      //Setting properties for Member Name
        Column shortfallType = new Column("Shortfall Type");
        shortfallType.setMethodName("getShortfallType");
        shortfallType.setColumnWidth("15%");
        shortfallType.setDBColumnName("V_TYPE");
        addColumn(shortfallType);
        
      //Setting properties for Member Name
        Column status = new Column("Status");
        status.setMethodName("getStatus");
        status.setColumnWidth("10%");
        status.setDBColumnName("STATUS");
        addColumn(status);
        
        Column replyReceived = new Column("Reply Received");
        replyReceived.setMethodName("getReplyReceived");
        replyReceived.setColumnWidth("20%");
        replyReceived.setDBColumnName("SHRT_DATE");
        addColumn(replyReceived);
        
        Column viewFile = new Column("View File");
        viewFile.setMethodName("getViewFile");
        viewFile.setColumnWidth("20%");
        viewFile.setIsLink(true);
        viewFile.setLinkParamName("ThirdLink");
        viewFile.setDBColumnName("FILE_EXIST");
        
        addColumn(viewFile);
     
	}
	

}
