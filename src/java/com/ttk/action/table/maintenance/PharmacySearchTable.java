package com.ttk.action.table.maintenance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;
  
public class PharmacySearchTable extends Table{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        
        //Setting properties for PreAuth Number
        Column colPreAuthNo = new Column("QDC Code.");//0
        colPreAuthNo.setMethodName("getDdcCode");
        colPreAuthNo.setColumnWidth("10%");
        colPreAuthNo.setIsLink(true);
        colPreAuthNo.setIsHeaderLink(true);
        colPreAuthNo.setDBColumnName("DDC_CODE");
        addColumn(colPreAuthNo);
        
        
        //Setting properties for Short Desc
        Column colPreAuthRefNo = new Column("Short Desc");//1
        colPreAuthRefNo.setMethodName("getShortDesc");
        colPreAuthRefNo.setColumnWidth("20%");
        colPreAuthRefNo.setDBColumnName("SHORT_DESC");
        addColumn(colPreAuthRefNo);
        
        
      //Setting properties for Start Date
        Column colClaimNo = new Column("Start Date");//2
        colClaimNo.setMethodName("getStartDate");
        colClaimNo.setColumnWidth("10%");
        colClaimNo.setDBColumnName("START_DATE");
        addColumn(colClaimNo);
        
        
        //Setting properties for End Date
        Column colDate = new Column("End Date");//3
        colDate.setMethodName("getEndDate");
        colDate.setColumnWidth("10%");
        colDate.setDBColumnName("END_DATE");
        addColumn(colDate);
        
        
      //Setting properties for Gender
        Column colPatientName = new Column("Gender");//4
        colPatientName.setMethodName("getGender");
        colPatientName.setColumnWidth("10%");
        colPatientName.setDBColumnName("GENDER");
        addColumn(colPatientName);
        
      //Setting properties for Qatar_Exclusion
        Column colPatientCardId = new Column("Exclusion Y/N");//5
        colPatientCardId.setMethodName("getQatarExcYN");
        colPatientCardId.setColumnWidth("15%");
        colPatientCardId.setDBColumnName("EXCLUTION_YN");
        addColumn(colPatientCardId);
        
      //Setting properties for Status
        Column colEmirateID = new Column("Status");//6
        colEmirateID.setMethodName("getStatus");
        colEmirateID.setColumnWidth("10%");
        colEmirateID.setDBColumnName("EMIRATE_ID");
        addColumn(colEmirateID);
        
        
      
        
	}

}
