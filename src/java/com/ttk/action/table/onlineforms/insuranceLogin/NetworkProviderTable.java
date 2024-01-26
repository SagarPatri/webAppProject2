package com.ttk.action.table.onlineforms.insuranceLogin;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class NetworkProviderTable extends Table{

	
	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for ENROLLMENT ID
        Column sNo = new Column("S. No");
        sNo.setMethodName("getSlNo");
        sNo.setColumnWidth("5%");
        sNo.setDBColumnName("AL_KOOT_ID");
        addColumn(sNo);

        //Setting properties for Member Name
        Column providerName = new Column("Provider Name");
        providerName.setMethodName("getHospitalName");
        providerName.setColumnWidth("20%");
        /*providerName.setIsLink(true);*/
//        memberName.setIsHeaderLink(true);
/*        memberName.setLinkParamName("SecondLink");*/
        providerName.setDBColumnName("HOSP_NAME");
        addColumn(providerName);
        
        //Setting properties for Member Name
        Column country = new Column("Country");
        country.setMethodName("getCountry");
        country.setColumnWidth("15%");
//        gender.setIsHeaderLink(true);
        country.setDBColumnName("COUNTRY_NAME");
        addColumn(country);
        
      //Setting properties for Member Name
        Column state = new Column("State");
        state.setMethodName("getStateName");
        state.setColumnWidth("20%");
//        gender.setIsHeaderLink(true);
        state.setDBColumnName("STATE_NAME");
        addColumn(state);
        
      //Setting properties for Member Name
        Column contactNumber = new Column("Contact Number");
        contactNumber.setMethodName("getContactNumber");
        contactNumber.setColumnWidth("15%");
//        contactNumber.setIsHeaderLink(true);
        contactNumber.setDBColumnName("CONTACT_NO");
        addColumn(contactNumber);
        
        Column emailId = new Column("E-mail Id");
        emailId.setMethodName("getEmailId");
        emailId.setColumnWidth("25%");
//        emailId.setIsHeaderLink(true);
        emailId.setDBColumnName("EMAIL_ID");
        addColumn(emailId);
     
	}
	

}
