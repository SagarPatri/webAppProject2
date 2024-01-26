/**
* @ (#) InsuranceCompanyTable.java Nov 21, 2005
* Project 		: TTK HealthCare Services
* File 			: InsuranceCompanyTable.java
* Author 		: Pradeep R
* Company 		: Span Systems Corporation
* Date Created 	: Nov 21, 2005
*
* @author 		: Pradeep R
* Modified by 	: 
* Modified date : 
* Reason 		: 
*/

package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

 

public class ReInsuranceTable extends Table {
	/**
	 * This creates the columnproperties objects for each and 
	 * every column and adds the column object to the table
	 */
	public void setTableProperties() {
		setRowCount(11);
        setCurrentPage(1);
        setPageLinkCount(10);
		
		//Setting properties for Company Name 
		Column colCompanyName = new Column("Re Insurer Name");
		colCompanyName.setMethodName("getReinsurerName");
		colCompanyName.setColumnWidth("25%");
		colCompanyName.setIsLink(true);
		colCompanyName.setLinkTitle("Edit Company Name");
		colCompanyName.setIsHeaderLink(true);
		colCompanyName.setHeaderLinkTitle("Sort by Company Name");
		colCompanyName.setDBColumnName("INS_COMP_NAME");
		addColumn(colCompanyName);
		
		//Setting properties for Company Code 
		Column colCompanyCode = new Column("Re-Insurer Id");
		colCompanyCode.setMethodName("getReinsurerId");
		colCompanyCode.setColumnWidth("25%");
		colCompanyCode.setIsHeaderLink(true);
		colCompanyCode.setHeaderLinkTitle("Sort by Insurance Code");
		colCompanyCode.setDBColumnName("INS_COMP_CODE_NUMBER");
		addColumn(colCompanyCode);

		//Setting properties for Insurance Company
		Column colEmpanelmentDate = new Column("Empanelment Date");
		colEmpanelmentDate.setMethodName("getEmpanelDate");
		colEmpanelmentDate.setColumnWidth("25%");
		colEmpanelmentDate.setIsHeaderLink(true);
		colEmpanelmentDate.setHeaderLinkTitle("Sort by: Empanelment Date");
		colEmpanelmentDate.setDBColumnName("EMPANELED_DATE");
		addColumn(colEmpanelmentDate);
		
		//Setting properties for Status
		Column colStatus = new Column("Status");
		colStatus.setMethodName("getActiveYN");
		colStatus.setColumnWidth("25%");
		colStatus.setIsHeaderLink(true);
		colStatus.setHeaderLinkTitle("Sort by Status");
		colStatus.setDBColumnName("ACTIVE_YN");
		addColumn(colStatus);
		
		//Setting properties for check box
		Column colSelect = new Column("Select");
		colSelect.setComponentType("checkbox");
		colSelect.setComponentName("chkopt");		
		addColumn(colSelect); 				
	}//end of public void setTableProperties()
}//end of class InsuranceCompanyTable



