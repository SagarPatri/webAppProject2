package com.ttk.action.table.fraudcase;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class CFDCampaignSearchTable extends Table {

	@Override
	public void setTableProperties() {
		

        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);  
        
     
        Column campaignName=new Column("Campaign Name");
        campaignName.setMethodName("getCampaignName");
        campaignName.setColumnWidth("20%");
        campaignName.setIsLink(true);
        campaignName.setIsHeaderLink(true);
        campaignName.setHeaderLinkTitle("Sort by: Campaign Name");
        campaignName.setDBColumnName("CAMPAIGN_NAME");
        addColumn(campaignName);
        
        Column providerName=new Column("Provider Name");
        providerName.setMethodName("getCfdProviderName");
        providerName.setColumnWidth("25%");
        providerName.setIsHeaderLink(true);
        providerName.setHeaderLinkTitle("Sort by: Provider Name");
        providerName.setDBColumnName("PROVIDER_NAME");
        addColumn(providerName);
        
        Column compaignStartDate=new Column("Campaign Start Date");
        compaignStartDate.setMethodName("getCampaginStartDate");
        compaignStartDate.setColumnWidth("10%");
        compaignStartDate.setIsHeaderLink(true);
        compaignStartDate.setHeaderLinkTitle("Sort by: Campaign Start Date");
        compaignStartDate.setDBColumnName("START_DATE");
        addColumn(compaignStartDate);
        
        Column compaignEndDate=new Column("Campaign End Date");
        compaignEndDate.setMethodName("getCampaginEndDate");
        compaignEndDate.setColumnWidth("10%");
        compaignEndDate.setIsHeaderLink(true);
        compaignEndDate.setHeaderLinkTitle("Sort by: Campaign End Date");
        compaignEndDate.setDBColumnName("END_DATE");
        addColumn(compaignEndDate);
        
        Column amtUtilized=new Column("Amount Utilized for Investigation");
        amtUtilized.setMethodName("getUtilizedAmount");
        amtUtilized.setColumnWidth("15%");
        amtUtilized.setIsHeaderLink(true);
        amtUtilized.setHeaderLinkTitle("Sort by: Amount Utilized for Investigation");
        amtUtilized.setDBColumnName("UTILIGED_AMOUNT");
        addColumn(amtUtilized);
        
        Column amtSaved=new Column("Amount Saved");
        amtSaved.setMethodName("getSavedAmount");
        amtSaved.setColumnWidth("10%");
        amtSaved.setIsHeaderLink(true);
        amtSaved.setHeaderLinkTitle("Sort by: Amount Saved");
        amtSaved.setDBColumnName("SAVED_AMOUNT");
        addColumn(amtSaved);
        
        Column campaignStatus=new Column("Campaign Status");
        campaignStatus.setMethodName("getCampaginStatus");
        campaignStatus.setColumnWidth("10%");
        campaignStatus.setIsHeaderLink(true);
        campaignStatus.setHeaderLinkTitle("Sort by: Campaign Status");
        campaignStatus.setDBColumnName("CAMPAIGN_STATUS");
        addColumn(campaignStatus);
        
       /* Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);*/
	}

}
