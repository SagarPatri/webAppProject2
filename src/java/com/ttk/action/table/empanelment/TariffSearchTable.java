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
public class TariffSearchTable extends Table // implements TableObjectInterface,Serializable 
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
        colServiceType.setColumnWidth("20%");	
        colServiceType.setIsHeaderLink(true);
        colServiceType.setHeaderLinkTitle("Sort by: Service Type");
        colServiceType.setDBColumnName("service_name");
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
        colActivityDesc.setDBColumnName("hosp_act_desc");
        addColumn(colActivityDesc);
        
        Column colGrossAmount = new Column("Gross Amount");
        colGrossAmount.setMethodName("getGrossAmount");
        colGrossAmount.setColumnWidth("10%");
        colGrossAmount.setIsHeaderLink(true);
        colGrossAmount.setHeaderLinkTitle("Sort by: Gross Amount");
        colGrossAmount.setDBColumnName("gross_amount");
        addColumn(colGrossAmount);
        
        Column colDiscAmount = new Column("Discount Amount");
        colDiscAmount.setMethodName("getDiscAmount");
        colDiscAmount.setColumnWidth("10%");
        colDiscAmount.setIsHeaderLink(true);
        colDiscAmount.setHeaderLinkTitle("Sort by: Discount Amount");
        colDiscAmount.setDBColumnName("disc_amount");
        addColumn(colDiscAmount);
        
        Column colDiscPercentage = new Column("Discount Percentage");
        colDiscPercentage.setMethodName("getDiscPercentage");
        colDiscPercentage.setColumnWidth("10%");
        colDiscPercentage.setIsHeaderLink(true);
        colDiscPercentage.setHeaderLinkTitle("Sort by: Discount Percentage");
        colDiscPercentage.setDBColumnName("disc_amount");
        addColumn(colDiscPercentage);
        
        //added by lohith
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
