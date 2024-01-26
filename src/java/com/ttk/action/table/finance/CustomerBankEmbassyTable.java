package com.ttk.action.table.finance;


import com.ttk.action.table.Column;
import com.ttk.action.table.Table;
/**
 * This class is added for cr koc 1103
 * added eft
 */
public class CustomerBankEmbassyTable extends Table {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */

        public void setTableProperties()
        {
            setRowCount(10);
            setCurrentPage(1);
            setPageLinkCount(10);
            //Setting properties for Embassy ID
            Column colEmbassyID = new Column("Embassy ID.");
            colEmbassyID.setMethodName("getEmbassyID");
            colEmbassyID.setColumnWidth("20%");
            colEmbassyID.setIsLink(true);
            colEmbassyID.setLinkTitle("Edit Embassy ID");
            colEmbassyID.setIsHeaderLink(true);
            colEmbassyID.setHeaderLinkTitle("Sort by Embassy ID.");
            colEmbassyID.setDBColumnName("EMBASSY_ID");
            addColumn(colEmbassyID);
           
            
            //Setting properties for Embassy Name
            Column colEmbassyName = new Column("Embassy Name");
            colEmbassyName.setMethodName("getEmbassyName");
            colEmbassyName.setColumnWidth("20%");
            colEmbassyName.setLinkTitle("Edit Embassy Name");
            colEmbassyName.setIsHeaderLink(true);
            colEmbassyName.setHeaderLinkTitle("Sort by Embassy Name");
            colEmbassyName.setDBColumnName("EMBASSY_NAME");
           // colAccountName.setLinkParamName("SecondLink");
            addColumn(colEmbassyName);
            
          //Setting properties for Embassy Account No
            Column colEmbassyAccountNo = new Column("Embassy Account No.");
            colEmbassyAccountNo.setMethodName("getEmbassyAccNo");
            colEmbassyAccountNo.setColumnWidth("15%");
            colEmbassyAccountNo.setIsHeaderLink(true);
            colEmbassyAccountNo.setHeaderLinkTitle("Sort by Embassy Acc No.");
            colEmbassyAccountNo.setDBColumnName("EMBASSY_ACC_NO");
            addColumn(colEmbassyAccountNo);
         
            //Setting properties for Embassy Location
            Column colEmbassyLocation = new Column("Embassy Location");
            colEmbassyLocation.setMethodName("getEmbassyLocation");
            colEmbassyLocation.setColumnWidth("15%");
            colEmbassyLocation.setIsHeaderLink(true);
            colEmbassyLocation.setHeaderLinkTitle("Sort by Embassy Location");
            colEmbassyLocation.setDBColumnName("EMBASSY_LOCATION");
            addColumn(colEmbassyLocation);
            
            //Setting properties for TTK Branch
            Column colEmpanelmentStatus = new Column("Empanelment Status");
            colEmpanelmentStatus.setMethodName("getEmpanelmentStatus");
            colEmpanelmentStatus.setColumnWidth("15%");
            colEmpanelmentStatus.setIsHeaderLink(true);
            colEmpanelmentStatus.setHeaderLinkTitle("Sort by Empanal Status");
            colEmpanelmentStatus.setDBColumnName("EMPANEL_DESCRIPTION");
            addColumn(colEmpanelmentStatus);
            
          //Setting properties for check box
            Column colSelect = new Column("Select");
            colSelect.setComponentType("checkbox");
            colSelect.setComponentName("chkopt");
            addColumn(colSelect);
            
        }//end of public void setTableProperties()

	}


