package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PreauthCaseInfoTable extends Table{

	@Override
	public void setTableProperties() {
		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        Column colStatus = new Column("Status");        
        colStatus.setMethodName("getStatus");
        colStatus.setHeaderLinkTitle("Sort by: Status");
        colStatus.setDBColumnName("STATUS");
        //colStatus.setColumnWidth("10%");
        addColumn(colStatus);
        
        Column colSource = new Column("Source");
        colSource.setMethodName("getSource");
        colSource.setHeaderLinkTitle("Sort by: Source");
        colSource.setDBColumnName("STATUS");
        //colSource.setColumnWidth("5%");
        addColumn(colSource);
        
        Column colPreaNo = new Column("Preauth No");
        colPreaNo.setMethodName("getPreauthNo");
        colPreaNo.setHeaderLinkTitle("Sort by: Preauth No");
        colPreaNo.setDBColumnName("preauth_number");
        colPreaNo.setIsLink(true);
        colPreaNo.setIsImage(true);
        colPreaNo.setImageName("getvVipImageName");
        colPreaNo.setImageTitle("getvVipImageTitle");
        colPreaNo.setImageName2("getEmergencyImageName"); //KOC11 koc 11
        colPreaNo.setImageTitle2("getEmergencyImageTitle");//KOC11  koc 11	       
        colPreaNo.setImageName3("getCatogoryImageName");
        colPreaNo.setImageTitle3("getCatogoryImageTitle");        
       /* colPreaNo.setImageHeight("20");
        colPreaNo.setImageWidth("20");*/ 
        //colPreaNo.setColumnWidth("15%");
        addColumn(colPreaNo);
        
        Column colCorporate = new Column("Corporate");
        colCorporate.setMethodName("getCorporate");
        colCorporate.setHeaderLinkTitle("Sort by: Status");
        colCorporate.setDBColumnName("STATUS");
        colCorporate.setColumnWidth("15%");
        addColumn(colCorporate);
        
        
        
    /*    Column colIdnf = new Column("IDNF");
        colIdnf.setMethodName("getIdnf");
        colIdnf.setHeaderLinkTitle("Sort by: IDNF");
        colIdnf.setDBColumnName("STATUS");
        addColumn(colIdnf);*/
        
        
        
        Column assigned = new Column("Assigned To");
        assigned.setMethodName("getAssignTo");
        assigned.setHeaderLinkTitle("Sort by: IDNF");
        assigned.setDBColumnName("assign_to");
        //assigned.setColumnWidth("15%");
        addColumn(assigned);
        
        Column recDate = new Column("Received Date");
        recDate.setMethodName("getRecivedDate");
        recDate.setHeaderLinkTitle("Sort by: IDNF");
        recDate.setDBColumnName("received_date");
        //recDate.setColumnWidth("5%");
        addColumn(recDate);
        
        Column rectime = new Column("Updated Date");
        rectime.setMethodName("getRecivedTime");
        rectime.setHeaderLinkTitle("Sort by: IDNF");
        rectime.setDBColumnName("update_rec_date");
       // rectime.setColumnWidth("5%");
        addColumn(rectime);
        
        Column colelapsedTime = new Column("Elapsed Time");
        colelapsedTime.setMethodName("getElapsedTime");
        colelapsedTime.setHeaderLinkTitle("Sort by: Elapsed Time");
        colelapsedTime.setDBColumnName("STATUS");
        //colelapsedTime.setColumnWidth("5%");
        addColumn(colelapsedTime);
        
        Column colTat = new Column("TAT");
        colTat.setMethodName("getTat");
        colTat.setHeaderLinkTitle("Sort by: TAT");
        colTat.setDBColumnName("STATUS");
        //colTat.setColumnWidth("5%");
        addColumn(colTat);
        
        Column colLocation = new Column("Location");
        colLocation.setMethodName("getLocation");
        colLocation.setHeaderLinkTitle("Sort by: Location");
        colLocation.setDBColumnName("STATUS");
        //colLocation.setColumnWidth("10%");
        addColumn(colLocation);
        
        
        
	}
}
