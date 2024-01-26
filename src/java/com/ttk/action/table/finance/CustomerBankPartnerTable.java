package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class CustomerBankPartnerTable extends Table{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setTableProperties() {
		
		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        
        Column colPartnerId = new Column("Partner ID");
        colPartnerId.setMethodName("getPartnerEmnalNumber");
        colPartnerId.setColumnWidth("20%");
        colPartnerId.setIsLink(true);
        colPartnerId.setLinkTitle("Edit Partner ID");
        colPartnerId.setIsHeaderLink(true);
        colPartnerId.setHeaderLinkTitle("Sort by Partner ID.");
        colPartnerId.setDBColumnName("empanel_number");
        addColumn(colPartnerId);
       
        
        //Setting properties for Embassy Name
        Column colPartnerName = new Column("Partner Name");
        colPartnerName.setMethodName("getPartnerName");
        colPartnerName.setColumnWidth("20%");
        colPartnerName.setLinkTitle("Edit Partner Name");
        colPartnerName.setIsHeaderLink(true);
        colPartnerName.setHeaderLinkTitle("Sort by Partner Name");
        colPartnerName.setDBColumnName("partner_name");
       // colAccountName.setLinkParamName("SecondLink");
        addColumn(colPartnerName);
        
      //Setting properties for Embassy Account No
        Column colPartnerAccNo = new Column("Partner Account No.");
        colPartnerAccNo.setMethodName("getPartnerAccno");
        colPartnerAccNo.setColumnWidth("15%");
        colPartnerAccNo.setIsHeaderLink(true);
        colPartnerAccNo.setHeaderLinkTitle("Sort by Partner Acc No.");
        colPartnerAccNo.setDBColumnName("account_number");
        addColumn(colPartnerAccNo);
        
        
      //Setting properties for Embassy Account No
        Column colPartnerLicenseId = new Column("Partner License Id.");
        colPartnerLicenseId.setMethodName("getPartnerLicenseId");
        colPartnerLicenseId.setColumnWidth("15%");
        colPartnerLicenseId.setIsHeaderLink(true);
        colPartnerLicenseId.setHeaderLinkTitle("Sort by Partner License Id.");
        colPartnerLicenseId.setDBColumnName("ptnr_licenc_numb");
        addColumn(colPartnerLicenseId);
        
        
      //Setting properties for Embassy Account No
        Column colEmpanelmentStatus = new Column("Empanelment Status.");
        colEmpanelmentStatus.setMethodName("getPartnerEmpnalDesc");
        colEmpanelmentStatus.setColumnWidth("15%");
        colEmpanelmentStatus.setIsHeaderLink(true);
        colEmpanelmentStatus.setHeaderLinkTitle("Sort by Empanelment Status.");
        colEmpanelmentStatus.setDBColumnName("empanel_description");
        addColumn(colEmpanelmentStatus);
		
        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);
	}

}
