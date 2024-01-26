/**
 * @ (#) HospitalSearchAction.java Sep 20, 2005
 * Project       : TTK HealthCare Services
 * File          : HospitalSearchAction.java
 * Author        : Srikanth H M
 * Company       : Span Systems Corporation
 * Date Created  : Sep 20, 2005
 *
 * @author       : Srikanth H M
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * This class provides the information of sbb_vendor table
 */
public class HospitalSearchTable extends Table // implements TableObjectInterface,Serializable
{
	/**
	 * This creates the columnproperties objects for each and 
	 * every column and adds the column object to the table
	 */
	public void setTableProperties()
	{
		setRowCount(20);
		setCurrentPage(1);
		setPageLinkCount(20);
		// Setting properties for hospital name		
		Column colHospitalName = new Column("Provider Name");
		colHospitalName.setMethodName("getHospitalName");
        colHospitalName.setColumnWidth("30%");
		colHospitalName.setIsLink(true);
		colHospitalName.setLinkTitle("Edit Provider");
		colHospitalName.setIsHeaderLink(true);
		colHospitalName.setHeaderLinkTitle("Sort by Provider Name");
		colHospitalName.setDBColumnName("HOSP_NAME");
		addColumn(colHospitalName);
		
		//Setting properties for empanelment number
		Column colEmpanelmentNo = new Column("Empanelment No.");
		colEmpanelmentNo.setMethodName("getEmplNumber");
        colEmpanelmentNo.setColumnWidth("15%");
		colEmpanelmentNo.setIsHeaderLink(true);
		colEmpanelmentNo.setHeaderLinkTitle("Sort by Empanelment No");
		colEmpanelmentNo.setDBColumnName("EMPANEL_NUMBER");
		addColumn(colEmpanelmentNo);
		
		
		//Setting properties for Empanelment Start Date
        Column colStartDate=new Column("Start Date");
        colStartDate.setMethodName("getEmpnlStartDate");
        colStartDate.setColumnWidth("10%");
        colStartDate.setIsHeaderLink(true);
        colStartDate.setHeaderLinkTitle("Sort by: Start Date");
        colStartDate.setDBColumnName("FROM_DATE");
        addColumn(colStartDate);
        
      //Setting properties for Empanelment End Date
        Column colEndDate=new Column("End Date");
        colEndDate.setMethodName("getEmpnlEndDate");
        colEndDate.setColumnWidth("10%");
        colEndDate.setIsHeaderLink(true);
        colEndDate.setHeaderLinkTitle("Sort by: End Date");
        colEndDate.setDBColumnName("TO_DATE");
        addColumn(colEndDate);
        
		//Setting properties for ttk branch
		Column colTtkBranch =new Column("Al Koot Branch");
		colTtkBranch.setMethodName("getTtkBranch");
        colTtkBranch.setColumnWidth("10%");
		colTtkBranch.setIsHeaderLink(true);
		colTtkBranch.setHeaderLinkTitle("Sort by Al Koot Branch");
		colTtkBranch.setDBColumnName("OFFICE_NAME");	//write appropriate db column name for the field
		addColumn(colTtkBranch);
		
		//Setting properties for ttk branch
		Column colEmpanelledBy =new Column("Empanelled By");
		colEmpanelledBy.setMethodName("getEmpanelledBy");
		colEmpanelledBy.setColumnWidth("15%");
		colEmpanelledBy.setIsHeaderLink(true);
		colEmpanelledBy.setHeaderLinkTitle("Empanelled By");
		colEmpanelledBy.setDBColumnName("USER_ID");	//Empanelled By
		addColumn(colEmpanelledBy);
		
		//Setting properties for hospital Status
		Column colStatus=new Column("Status");		
		colStatus.setMethodName("getStatus");		
        colStatus.setColumnWidth("15%");
		colStatus.setIsHeaderLink(true);
		colStatus.setHeaderLinkTitle("Sort by Status");
		colStatus.setDBColumnName("EMPANEL_DESCRIPTION");
		addColumn(colStatus);
	
		//Setting properties for check box
		Column colSelect = new Column("Select");
		colSelect.setComponentType("checkbox");
		colSelect.setComponentName("chkopt");		
		addColumn(colSelect); 				
	}//end of public void setTableProperties()
}//end of class HospitalSearchTable

		
	
		
	
