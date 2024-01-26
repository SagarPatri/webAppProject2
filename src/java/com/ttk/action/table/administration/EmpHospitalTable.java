/**
 * @ (#) EmpHospitalTable.java Dec 05th, 2005
 * Project       : TTK HealthCare Services
 * File          : EmpHospitalTable.java
 * Author        : Bhaskar Sandra
 * Company       : Span Systems Corporation
 * Date Created  : Dec 05th, 2005
 *
 * @author       : Bhaskar Sandra
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */
package com.ttk.action.table.administration;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * @author sandra_bhaskar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EmpHospitalTable extends Table {
	
	/**
	 * This creates the columnproperties objects for each and 
	 * every column and adds the column object to the table
	 */

	public void setTableProperties()
	{
	setRowCount(10);
	setCurrentPage(1);
	setPageLinkCount(10);
	Column colHospName =new Column("Provider Name");           
	colHospName.setMethodName("getHospitalName");                            
	colHospName.setColumnWidth("40%");                    
	colHospName.setIsHeaderLink(true);                    
	colHospName.setHeaderLinkTitle("Sort by: Provider Name");  
	colHospName.setDBColumnName("HOSP_NAME");                       
	addColumn(colHospName);
	
	Column colNetworkType=new Column("Network Type");                    
	colNetworkType.setMethodName("getNetworkTypeList");
	colNetworkType.setColumnWidth("15%");
	colNetworkType.setIsHeaderLink(true);
	colNetworkType.setHeaderLinkTitle("Sort by: Network Type");
	colNetworkType.setDBColumnName("PRIMARY_NETWORK");
	addColumn(colNetworkType);
	
	Column colCity=new Column("City");                    
	colCity.setMethodName("getCityDesc");
	colCity.setColumnWidth("15%");
	colCity.setIsHeaderLink(true);
	colCity.setHeaderLinkTitle("Sort by: City");
	colCity.setDBColumnName("TPA_CITY_CODE.CITY_TYPE_ID");
	addColumn(colCity);
	
	Column colEmpNo =new Column("Empanelment No.");           
	colEmpNo.setMethodName("getEmplNumber");                           
	colEmpNo.setColumnWidth("15%");   
	colEmpNo.setIsHeaderLink(true);
	colEmpNo.setHeaderLinkTitle("Sort by: Empanelment No."); 
	colEmpNo.setDBColumnName("EMPANEL_NUMBER");               
	addColumn(colEmpNo);
	
	Column colTtkBranch =new Column("Al Koot Branch");           
	colTtkBranch.setMethodName("getTtkBranch");                  
	colTtkBranch.setColumnWidth("15%");                    
	colTtkBranch.setIsHeaderLink(true);                    
	colTtkBranch.setHeaderLinkTitle("Sort by: Al Koot Branch");
	colTtkBranch.setDBColumnName("OFFICE_NAME");            
	addColumn(colTtkBranch);
	
	 //Setting properties for image Copay
    Column colImage1 = new Column("");
    colImage1.setIsImage(true);
    colImage1.setIsImageLink(true);
    colImage1.setColumnWidth("5%");
    colImage1.setImageName("getCopayImageName");
    colImage1.setImageTitle("getCopayImageTitle");
    colImage1.setVisibility(false);
    addColumn(colImage1);
    
    //Setting properties for check box
	Column colSelect = new Column("Select");
	colSelect.setComponentType("checkbox");
	colSelect.setComponentName("chkopt");		
	addColumn(colSelect); 				
}//end of public void setTableProperties()
}//end of class EmpHospitalTable

