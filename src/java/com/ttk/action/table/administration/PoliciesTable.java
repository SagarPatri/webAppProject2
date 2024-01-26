/**
 * @ (#) HospitalSearchAction.java Sep 20, 2005
 * Project       : TTK HealthCare Services
 * File          : PoliciesTable.java
 * Author        : Pradeep R
 * Company       : Span Systems Corporation
 * Date Created  : Nov 16, 2005
 *
 * @author       : Pradeep R
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */
package com.ttk.action.table.administration;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * This class provides the information of sbb_vendor table
 */
public class PoliciesTable extends Table {
	/**
	 * This creates the columnproperties objects for each and 
	 * every column and adds the column object to the table
	 */
	
		public void setTableProperties()
		{
			setRowCount(10);
			setCurrentPage(1);
			setPageLinkCount(10);
			
			//Setting properties for Policy No
			Column colPolicyNo = new Column("Policy No.");
			colPolicyNo.setMethodName("getPolicyNbr");
			colPolicyNo.setColumnWidth("13%");
			colPolicyNo.setIsLink(true);
			colPolicyNo.setLinkTitle("Edit Policy No");
			colPolicyNo.setIsHeaderLink(true);
			colPolicyNo.setHeaderLinkTitle("Sort by Policy No.");
			colPolicyNo.setDBColumnName("POLICY_NUMBER");
			addColumn(colPolicyNo);
			
			//Setting properties for Insurance Company
			Column colInsuranceCompany = new Column("Insurance Company");
			colInsuranceCompany.setMethodName("getCompanyName");
			colInsuranceCompany.setColumnWidth("15%");
			colInsuranceCompany.setIsHeaderLink(true);
			colInsuranceCompany.setHeaderLinkTitle("Sort by Insurance Company");
			colInsuranceCompany.setDBColumnName("INS_COMP_NAME");
			addColumn(colInsuranceCompany);
			
			//Setting properties for Enrollment Type
			Column colEnrollmentType = new Column("Enrollment Type");
			colEnrollmentType.setMethodName("getEnrollmentType");
			colEnrollmentType.setColumnWidth("15%");
			colEnrollmentType.setIsHeaderLink(true);
			colEnrollmentType.setHeaderLinkTitle("Sort by Enrollment Type");
			colEnrollmentType.setDBColumnName("ENROL_DESCRIPTION");
			addColumn(colEnrollmentType);
			
			
			//Setting properties for Group Id
			Column colGroupId = new Column("Group-Sub Group Id");
			colGroupId.setMethodName("getGroupID");
			colGroupId.setColumnWidth("13%");
			colGroupId.setIsHeaderLink(true);
			colGroupId.setHeaderLinkTitle("Sort by Group Id");
			colGroupId.setDBColumnName("GROUP_ID");
			addColumn(colGroupId);
			
			//Setting properties for Group Name
			Column colGroupName = new Column("Group Name");
			colGroupName.setMethodName("getGroupName");
			colGroupName.setColumnWidth(" 17%");
			colGroupName.setIsHeaderLink(true);
			colGroupName.setHeaderLinkTitle("Sort by Group Name");
			colGroupName.setDBColumnName("GROUP_NAME");
			addColumn(colGroupName);
			
//			Setting properties for Status
			Column colStatus = new Column("Status");
			colStatus.setMethodName("getStatus");
			colStatus.setColumnWidth("10%");
			colStatus.setIsHeaderLink(true);
			colStatus.setHeaderLinkTitle("Sort by Status");
			colStatus.setDBColumnName("DESCRIPTION");
			addColumn(colStatus);
			
//			Setting properties for Status
			Column colTTKBranch = new Column("Al Koot Branch");
			colTTKBranch.setMethodName("getOfficeName");
			colTTKBranch.setColumnWidth("18%");
			colTTKBranch.setIsHeaderLink(true);
			colTTKBranch.setHeaderLinkTitle("Sort by TTKBranch");
			colTTKBranch.setDBColumnName("OFFICE_NAME");
			addColumn(colTTKBranch);
			
			 //Setting properties for image  
	        Column colImage2 = new Column("");
	        colImage2.setIsImage(true);
	        colImage2.setIsImageLink(true);
	        colImage2.setImageName("getSynchronizeImageName");
	        colImage2.setImageTitle("getSynchronizeImageTitle");
	        addColumn(colImage2);
	        
			//Setting properties for check box
			Column colSelect = new Column("Select");
			colSelect.setComponentType("checkbox");
			colSelect.setComponentName("chkopt");		
			addColumn(colSelect); 				
		}//end of public void setTableProperties()
	}//end of class PoliciesTable
