/**
 * @ (#) TariffSearchTable.java Oct 21, 2005
 * Project       : TTK HealthCare Services
 * File          : TariffSearchTable.java
 * Author        : Kishor kumar S H
 * Company       : RCS
 * Date Created  : 9th April 2015
 *
 * @author       : kishor kumar S H
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * This class provides the information of Hospital Tariff table
 */
public class PharmacyTariffSearchTable extends Table // implements TableObjectInterface,Serializable 
{
	/**
	 * This creates the columnproperties objects for each and 
	 * every column and adds the column object to the table
	 */
	
	public void setTableProperties()
	{
		setRowCount(25);
        setCurrentPage(1);
        setPageLinkCount(25);
        //Setting properties for Link Description
		Column price_Ref=new Column("Price Ref Number");
		price_Ref.setMethodName("getPriceRefNo"); 
		price_Ref.setColumnWidth("10%");	
		price_Ref.setHeaderLinkTitle("Sort by: Price Ref Number");
		price_Ref.setDBColumnName("price_ref_number");
		addColumn(price_Ref);		
				
		
        Column colActivityCode=new Column("Activity Code");
        colActivityCode.setMethodName("getActivityCode");
        colActivityCode.setColumnWidth("10%");
        colActivityCode.setIsLink(true);
        colActivityCode.setIsHeaderLink(true);
        colActivityCode.setHeaderLinkTitle("Sort by: Activity Code");
        colActivityCode.setDBColumnName("activity_code");
        addColumn(colActivityCode);
        
        Column colServiceType=new Column("Service Type");
        colServiceType.setMethodName("getServiceName");
        colServiceType.setColumnWidth("10%");	
        colServiceType.setIsHeaderLink(true);
        colServiceType.setHeaderLinkTitle("Sort by: Service Type");
        colServiceType.setDBColumnName("service_type");
        addColumn(colServiceType);
        
        Column colInternalCode=new Column("Internal Code");
        colInternalCode.setMethodName("getInternalCode");
        colInternalCode.setColumnWidth("10%");	
        colInternalCode.setIsHeaderLink(true);
        colInternalCode.setHeaderLinkTitle("Sort by: Internal Code");
        colInternalCode.setDBColumnName("internal_code");
        addColumn(colInternalCode);
        
        Column colActivityDesc=new Column("Activity Desc");
        colActivityDesc.setMethodName("getActivityDesc");
        colActivityDesc.setColumnWidth("10%");	
        colActivityDesc.setIsHeaderLink(true);
        colActivityDesc.setHeaderLinkTitle("Sort by: Activity Desc");
        colActivityDesc.setDBColumnName("activity_desc");
        addColumn(colActivityDesc);
        
        Column colInternalDesc=new Column("Internal Desc");
        colInternalDesc.setMethodName("getInternalCodeDesc");
        colInternalDesc.setColumnWidth("10%");	
        colInternalDesc.setIsHeaderLink(true);
        colInternalDesc.setHeaderLinkTitle("Sort by: Internal Desc");
        colInternalDesc.setDBColumnName("internal_desc");
        addColumn(colInternalDesc);
        
        Column colUnitPrice = new Column("Unit Price");
        colUnitPrice.setMethodName("getUnitPrice");
        colUnitPrice.setColumnWidth("10%");
        colUnitPrice.setIsHeaderLink(true);
        colUnitPrice.setHeaderLinkTitle("Sort by: Unit Price");
        colUnitPrice.setDBColumnName("unit_price");
        addColumn(colUnitPrice);
        
        Column colPkgPrice = new Column("Package Price");
        colPkgPrice.setMethodName("getPkgPrice");
        colPkgPrice.setColumnWidth("10%");
        colPkgPrice.setIsHeaderLink(true);
        colPkgPrice.setHeaderLinkTitle("Sort by: Package Price");
        colPkgPrice.setDBColumnName("pckg_price");
        addColumn(colPkgPrice);
        
        Column colPkgSize = new Column("Package Size");
        colPkgSize.setMethodName("getPkgSize");
        colPkgSize.setColumnWidth("10%");
        colPkgSize.setIsHeaderLink(true);
        colPkgSize.setHeaderLinkTitle("Sort by: Package Size");
        colPkgSize.setDBColumnName("pkgSize");
        addColumn(colPkgSize);
        //()
        Column colDiscAmt = new Column("Discount Amount");
        colDiscAmt.setMethodName("getDiscAmount");
        colDiscAmt.setColumnWidth("10%");
        colDiscAmt.setIsHeaderLink(true);
        colDiscAmt.setHeaderLinkTitle("Sort by: Discount Amount");
        colDiscAmt.setDBColumnName("disc_amt");
        addColumn(colDiscAmt);
        
        Column colDiscPercentage = new Column("Discount Percentage");
        colDiscPercentage.setMethodName("getDiscPercentage");
        colDiscPercentage.setColumnWidth("10%");
        colDiscPercentage.setIsHeaderLink(true);
        colDiscPercentage.setHeaderLinkTitle("Sort by: Discount Percentage");
        colDiscPercentage.setDBColumnName("discount_percentage");
        addColumn(colDiscPercentage);
        
        
        Column EndDate = new Column("End Date");
        EndDate.setMethodName("getStrend_date");
        EndDate.setColumnWidth("10%");
        EndDate.setIsHeaderLink(true);
        EndDate.setHeaderLinkTitle("Sort by: end date");
        EndDate.setDBColumnName("end_date");
        addColumn(EndDate);
        
        Column NetworkType = new Column("Network Type");
        NetworkType.setMethodName("getNetworkTypeDesc");
        NetworkType.setColumnWidth("10%");
        NetworkType.setIsHeaderLink(true);
        NetworkType.setHeaderLinkTitle("Sort by: Network Type");
        NetworkType.setDBColumnName("network_desc");
        addColumn(NetworkType);
             
       // Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);        
	}
}
