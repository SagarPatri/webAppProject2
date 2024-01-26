package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PreauthUserInfoTable extends Table{

	@Override
	public void setTableProperties() {
		
		setRowCount(13);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        Column colUserName = new Column("Dr.Name");
        colUserName.setMethodName("getUserName");
        colUserName.setHeaderLinkTitle("Sort by: User Name");
        colUserName.setDBColumnName("User_Name");
        colUserName.setColumnWidth("15%");
        addColumn(colUserName);
        
        Column colAvgProTIme = new Column("Avg Time");//Avg.Processing Time
        colAvgProTIme.setMethodName("getAvgProcessTime");
        colAvgProTIme.setHeaderLinkTitle("Sort by: Avg.Processing Time");
        colAvgProTIme.setDBColumnName("AVG_Processing_Time");
        colAvgProTIme.setColumnWidth("10%");
        addColumn(colAvgProTIme);
        
        Column colTotalLoggedHrs = new Column("Total Hrs");//Logged Hrs
        colTotalLoggedHrs.setMethodName("getTotalLoggedHour");
        colTotalLoggedHrs.setHeaderLinkTitle("Sort by: Total Logged Hrs");
        colTotalLoggedHrs.setDBColumnName("Total_Logged_Hrs");
        colTotalLoggedHrs.setColumnWidth("10%");
        addColumn(colTotalLoggedHrs);
        
        Column colLocation = new Column("Total");//Total Transaction
        colLocation.setMethodName("getOfficeCode");
/*        colLocation.setImageName("getsStatusImageName");
        colLocation.setImageTitle("getStatusImageTitle");
        colLocation.setImageHeight("20");
        colLocation.setImageWidth("20");*/
        colLocation.setHeaderLinkTitle("Sort by: Location");
        colLocation.setDBColumnName("Location");
        colLocation.setColumnWidth("10%");
        addColumn(colLocation);
        
        Column colTag = new Column("Tag");//Total Transaction
        colTag.setMethodName("getTag");
        colTag.setHeaderLinkTitle("Sort by: Location");
        
        colTag.setColumnWidth("5%");
        addColumn(colTag);
        
        Column colCurrentStatus = new Column("");//User Status
        colCurrentStatus.setMethodName("getUserCurStatus");
        colCurrentStatus.setHeaderLinkTitle("Sort by: Current Status");
        colCurrentStatus.setDBColumnName("STATUS");
        colCurrentStatus.setIsImage(true);
        colCurrentStatus.setImageName("getsStatusImageName");
        colCurrentStatus.setImageTitle("getStatusImageTitle");
        /*colCurrentStatus.setImageHeight("5");
        colCurrentStatus.setImageWidth("5");*/
       /* colCurrentStatus.setDBColumnName("pat_status_general_type_id");*/
        colCurrentStatus.setColumnWidth("3%");
        addColumn(colCurrentStatus);
                                
	}
}
