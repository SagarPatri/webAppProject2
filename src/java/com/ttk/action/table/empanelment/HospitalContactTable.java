/**
 * @ (#) HospitalContactTable.javaSep 24, 2005
 * Project      : TTK HealthCare Services
 * File         : HospitalContactTable.java
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : Sep 24, 2005
 *
 * @author       :  Chandrasekaran J
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;
import com.ttk.action.table.TableData;

/**
 * 
 * this class provides the information of hospital contact table
 * 
 */
public class HospitalContactTable extends Table 
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
        
        
        
        
        
        //Setting properties for name
        Column colName = new Column("Name");
        colName.setMethodName("getContactName");
        colName.setColumnWidth("10%");
        colName.setIsLink(true);
        colName.setImageName("getImageName");
        colName.setImageTitle("getImageTitle");
        colName.setIsHeaderLink(true);
        colName.setHeaderLinkTitle("Sort by Name");
        colName.setDBColumnName("contact_name");
        addColumn(colName);
        
        
        
        
        
      //Setting properties for contact type
        Column colContactType = new Column("Department");
        colContactType.setMethodName("getContactDesc");
        colContactType.setColumnWidth("15%");
        //colContactType.setIsLink(true);
       // colContactType.setIsHeaderLink(true);
       // colContactType.setHeaderLinkTitle("Sort by Contact Type");
        colContactType.setDBColumnName("dept_special");
        addColumn(colContactType);
        

       
        
        Column coluserId = new Column("User ID");
        coluserId.setMethodName("getLicenseNumb");
        coluserId.setColumnWidth("10%");
        //colContactType.setIsLink(true);
        //coluserId.setIsHeaderLink(true);
       // coluserId.setHeaderLinkTitle("Sort by Contact Type");
        coluserId.setDBColumnName("user_id");
        addColumn(coluserId);

        //Setting properties for Designation
        Column colDesignationType = new Column("Designation");
        colDesignationType.setMethodName("getDesignation");
        colDesignationType.setColumnWidth("15%");
        //colDesignationType.setIsLink(true);
       // colDesignationType.setIsHeaderLink(true);
       // colDesignationType.setHeaderLinkTitle("Sort by Designation");
        colDesignationType.setDBColumnName("design_consult");
        addColumn(colDesignationType);
        
        
        //Setting properties for Email
        Column colEmail = new Column("Email");
        colEmail.setMethodName("getEmail");
        colEmail.setColumnWidth("15%");
        //colEmail.setIsLink(true);
       // colEmail.setIsHeaderLink(true);
      //  colEmail.setHeaderLinkTitle("Sort by Email");
        colEmail.setDBColumnName("primary_email_id");
        addColumn(colEmail);
        
        
        
        //Setting properties for Telephone
        Column colTelephone = new Column("Telephone");
        colTelephone.setMethodName("getPhone");
        colTelephone.setColumnWidth("10%");
        //colTelephone.setIsLink(true);
       // colTelephone.setIsHeaderLink(true);
       // colTelephone.setHeaderLinkTitle("Sort by Telephone");
        colTelephone.setDBColumnName("phone");
        addColumn(colTelephone);
        
        
        
        
        Column coluserRole = new Column("User Role");
        coluserRole.setMethodName("getAuthType");
        coluserRole.setColumnWidth("15%");
        //colContactType.setIsLink(true);
       // coluserRole.setIsHeaderLink(true);
        //coluserRole.setHeaderLinkTitle("Sort by Contact Type");
        coluserRole.setDBColumnName("role_name");
        addColumn(coluserRole);
        
        
        
        Column colStatus = new Column("Status");
        colStatus.setMethodName("getActiveYn");
        colStatus.setColumnWidth("10%");
        //colContactType.setIsLink(true);
        //colStatus.setIsHeaderLink(true);
        //colStatus.setHeaderLinkTitle("Sort by Contact Type");
        colStatus.setDBColumnName("active_yn");
        addColumn(colStatus);
        
        
        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        //colSelect.setWidth(10);
        addColumn(colSelect);
        
    }//end of public void setTableProperties()
    
}
