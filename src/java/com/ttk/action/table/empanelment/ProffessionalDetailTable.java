/**
 * @ (#) ProffessionalDetailTable.javaSep 24, 2005
 * Project      : TTK HealthCare Services
 * File         : ProffessionalDetailTable.java
 * Author       : Kishor kumar S H
 * Company      : RCS Technologies
 * Date Created : 06 Jan 2015
 *
 * @author       :  Kishor kumar S H
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * 
 * this class provides the information of hospital contact table
 * 
 */
public class ProffessionalDetailTable extends Table 
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
        
   /*   //Setting properties for contact type
        Column colContactType = new Column("License Number");
        colContactType.setMethodName("getLicenseNumb");
        colContactType.setColumnWidth("20%");
        colContactType.setIsHeaderLink(true);
        colContactType.setHeaderLinkTitle("Sort by License Number");
        colContactType.setDBColumnName("PROFESSIONAL_ID");
        addColumn(colContactType);*/
        

        //Setting properties for name
        Column colName = new Column("Clinician Name");
        colName.setMethodName("getContactName");
        colName.setColumnWidth("20%");
        colName.setIsLink(true);
        colName.setImageName("getImageName");
        colName.setImageTitle("getImageTitle");
        colName.setIsHeaderLink(true);
        colName.setLinkParamName("SecondLink");
        colName.setHeaderLinkTitle("Sort by Name");
        colName.setDBColumnName("contact_name");
        addColumn(colName);
        
        
        
        
        
        //Setting properties for contact type
        Column colClinicianId = new Column("Clinician ID");
        colClinicianId.setMethodName("getLicenseNumb");
        colClinicianId.setColumnWidth("20%");
       // colClinicianId.setIsHeaderLink(true);
       // colClinicianId.setHeaderLinkTitle("Sort by License Number");
        colClinicianId.setDBColumnName("user_id");
        addColumn(colClinicianId);
        
        
        
      //Setting properties for contact type
        Column colSpeciality = new Column("Speciality");
        colSpeciality.setMethodName("getContactDesc");
        colSpeciality.setColumnWidth("20%");
        //colSpeciality.setIsHeaderLink(true);
       // colSpeciality.setHeaderLinkTitle("Sort by License Number");
        colSpeciality.setDBColumnName("dept_special");
        addColumn(colSpeciality);
        
        
        
        
        
        
        //Setting properties for contact type
        Column colConsultationType = new Column("Consultation Type");
        colConsultationType.setMethodName("getDesignation");
        colConsultationType.setColumnWidth("15%");
        //colConsultationType.setIsHeaderLink(true);
       // colConsultationType.setHeaderLinkTitle("Sort by License Number");
        colConsultationType.setDBColumnName("design_consult");
        addColumn(colConsultationType);
        
        
        
        
        //Setting properties for Email
        Column colEmail = new Column("Email");
        colEmail.setMethodName("getEmail");
        colEmail.setColumnWidth("15%");
        //colEmail.setIsLink(true);
        //colEmail.setIsHeaderLink(true);
        //colEmail.setHeaderLinkTitle("Sort by Email");
        colEmail.setDBColumnName("primary_email_id");
        addColumn(colEmail);
        
        
        
        Column colStatus = new Column("Status");
        colStatus.setMethodName("getActiveYn");
        colStatus.setColumnWidth("10%");
        //colContactType.setIsLink(true);
       // colStatus.setIsHeaderLink(true);
       // colStatus.setHeaderLinkTitle("Sort by Contact Type");
        colStatus.setDBColumnName("active_yn");
        addColumn(colStatus);
        
        
      /*  

        //Setting properties for Designation
        Column colDesignationType = new Column("Authority Standard");
        colDesignationType.setMethodName("getAuthType");
        colDesignationType.setColumnWidth("20%");
        //colDesignationType.setIsLink(true);
        colDesignationType.setIsHeaderLink(true);
        colDesignationType.setHeaderLinkTitle("Sort by Authorized");
        colDesignationType.setDBColumnName("PRO_AUTHORITY");
        addColumn(colDesignationType);
        
        //Setting properties for Telephone
        Column colTelephone = new Column("Effective From");
        colTelephone.setMethodName("getEffectiveFrom");
        colTelephone.setColumnWidth("20%");
        //colTelephone.setIsLink(true);
        colTelephone.setIsHeaderLink(true);
        colTelephone.setHeaderLinkTitle("Sort by Effective From");
        colTelephone.setDBColumnName("START_DATE");
        addColumn(colTelephone);
        
        
        //Setting properties for Email
        Column colEmail = new Column("Effective To");
        colEmail.setMethodName("getEffectiveTo");
        colEmail.setColumnWidth("20%");
        //colEmail.setIsLink(true);
        colEmail.setIsHeaderLink(true);
        colEmail.setHeaderLinkTitle("Sort by Effective To");
        colEmail.setDBColumnName("END_DATE");
        addColumn(colEmail);
        */
        
        //Setting properties for check box
        /*Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        //colSelect.setWidth(10);
        addColumn(colSelect);*/
        
    }//end of public void setTableProperties()
    
}
