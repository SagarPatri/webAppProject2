/**
 * @ (#) CallCenterTable.java July 28, 2006
 * Project      : TTK HealthCare Services
 * File         : CallCenterTable.java
 * Author       : Lancy A
 * Company      : Span Systems Corporation
 * Date Created : July 28, 2006
 *
 * @author       : Lancy A
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.table.customercare;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * this class provides the information of Call Center Table
 */
public class CallCenterTable extends Table
{
    /**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        
      //ADD IMAGE THINGS OVER HERE   0
        //Setting properties for image Priority
        Column colImage = new Column("Call Back");
        colImage.setMethodName("getCallbackYN");
        colImage.setColumnWidth("5%");
        colImage.setIsHeaderLink(true);
        colImage.setImageName("getPriorityImageName");
        colImage.setImageTitle("getPriorityImageTitle");
        colImage.setHeaderLinkTitle("Sort by:Call Back");
        colImage.setDBColumnName("CALL_BCK_YN");
        addColumn(colImage);
        
        
        //Setting properties for Log Number   1
        Column colLogNo = new Column("Log No.");
        colLogNo.setMethodName("getLogNumber");
        colLogNo.setColumnWidth("10%");
        colLogNo.setIsLink(true);
        colLogNo.setIsHeaderLink(true);
        colLogNo.setLinkTitle("View Log History");
        colLogNo.setHeaderLinkTitle("Sort by: Log No.");
        colLogNo.setDBColumnName("CALL_LOG_NUMBER");
        addColumn(colLogNo);

        //Setting properties for Log Type  2
        Column colLogType  = new Column("Log Type");
        colLogType.setMethodName("getLogTypeDesc");
        colLogType.setColumnWidth("10%");
        colLogType.setIsHeaderLink(true);
        colLogType.setHeaderLinkTitle("Sort by: Log Type");
        colLogType.setDBColumnName("LOG_TYPE");
        addColumn(colLogType);

        //Setting properties for Log Dept.  3
        Column colLogDepartment = new Column("Category");
        colLogDepartment.setMethodName("getCategoryDesc");
        colLogDepartment.setColumnWidth("10%");
        colLogDepartment.setIsHeaderLink(true);
        colLogDepartment.setHeaderLinkTitle("Sort by: Log Dept.");
        colLogDepartment.setDBColumnName("CALL_CATEGORY");
        addColumn(colLogDepartment);

        //Setting properties for Attended By   4
        Column colAttendedBy = new Column("Attended By");
        colAttendedBy.setMethodName("getRecordedBy");
        colAttendedBy.setColumnWidth("10%");
        colAttendedBy.setIsHeaderLink(true);
        colAttendedBy.setHeaderLinkTitle("Sort by: Attended By");
        colAttendedBy.setDBColumnName("CALL_LOGGED_BY_USER");
        addColumn(colAttendedBy);

        //Setting properties for Attended Date     5
        Column colAttendedDate = new Column("Attended Date");
        colAttendedDate.setMethodName("getLogDate");
        colAttendedDate.setColumnWidth("5%");
        colAttendedDate.setIsHeaderLink(true);
        colAttendedDate.setHeaderLinkTitle("Sort by: Attended Date");
        colAttendedDate.setDBColumnName("CALL_RECORDED_DATE");
        addColumn(colAttendedDate);
        
        //Setting properties for Status   6
        Column colStatus = new Column("Status");
        colStatus.setMethodName("getStatusDesc");
        colStatus.setColumnWidth("10%");
        colStatus.setIsLink(true);
        colStatus.setIsHeaderLink(true);
        colStatus.setLinkTitle("Edit Status");
        colStatus.setLinkParamName("SecondLink");
        colStatus.setHeaderLinkTitle("Sort by: Status");
        colStatus.setDBColumnName("STATUS");
        addColumn(colStatus);

        //POLICY HOLDER ******************************
        //Setting properties for Enrollment Id    7
        Column colEnrollmentId = new Column("Al Koot ID");
        colEnrollmentId.setMethodName("getEnrollmentID");
        colEnrollmentId.setColumnWidth("10%");
        colEnrollmentId.setIsHeaderLink(true);
        colEnrollmentId.setHeaderLinkTitle("Sort by: Enrollment Id");
        colEnrollmentId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colEnrollmentId);

        
        
       
         //Setting properties for  Qatar ID.    8
        Column colQatarId=new Column("Qatar ID");
        colQatarId.setMethodName("getsQatarId");
        colQatarId.setColumnWidth("10%");
        colQatarId.setIsHeaderLink(true);
        colQatarId.setHeaderLinkTitle("Sort by: Qatar ID");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);
        
        
        
          
        //Setting properties for Policy Number   9
        Column colPolicyNumber = new Column("Policy No.");
        colPolicyNumber.setMethodName("getPolicyNumber");
        colPolicyNumber.setColumnWidth("10%");
        colPolicyNumber.setIsHeaderLink(true);
        colPolicyNumber.setHeaderLinkTitle("Sort by: Policy No.");
        colPolicyNumber.setDBColumnName("POLICY_NUMBER");
        addColumn(colPolicyNumber);
 
        //Setting properties for Member Name     10
        Column colClaimantName = new Column("Name");
        colClaimantName.setMethodName("getClaimantName");
        colClaimantName.setIsHeaderLink(true);
        colClaimantName.setHeaderLinkTitle("Sort by: Name");
        colClaimantName.setDBColumnName("CLAIMANT_NAME");
        addColumn(colClaimantName);
        
        //CORPORATE *******************************
        //Setting properties for Corporate Name     11
        Column colCorporateName = new Column("Corporate Name");
        colCorporateName.setMethodName("getGroupName");
        colCorporateName.setIsHeaderLink(true);
        colCorporateName.setHeaderLinkTitle("Sort by: Corporate Name");
        colCorporateName.setDBColumnName("GROUP_NAME");
        addColumn(colCorporateName);

        //Setting properties for Policy Number    12
        Column colPolicyNo = new Column("Policy No.");
        colPolicyNo.setMethodName("getPolicyNumber");
        colPolicyNo.setIsHeaderLink(true);
        colPolicyNo.setHeaderLinkTitle("Sort by: Policy No.");
        colPolicyNo.setDBColumnName("POLICY_NUMBER");
        addColumn(colPolicyNo);
        
        //NHCP ********************************
        //Setting properties for Empanelment Number    13
        Column colEmpanelmentNumber = new Column("Empanelment No.");
        colEmpanelmentNumber.setMethodName("getEmpanelmentNbr");
        colEmpanelmentNumber.setIsHeaderLink(true);
        colEmpanelmentNumber.setHeaderLinkTitle("Sort by: Empanelment No.");
        colEmpanelmentNumber.setDBColumnName("EMPANEL_NUMBER");
        addColumn(colEmpanelmentNumber);

        //Setting properties for Hospital Name   14
        Column colHospitalName = new Column("Hosp. Name");
        colHospitalName.setMethodName("getHospName");
        colHospitalName.setIsHeaderLink(true);
        colHospitalName.setHeaderLinkTitle("Sort by: Hosp. Name");
        colHospitalName.setDBColumnName("HOSP_NAME");
        addColumn(colHospitalName);
        
        //Healthcare COMPANY ********************************
        //Setting properties for Healthcare Company    15
        Column colInsuranceCompany = new Column("Healthcare Comp.");
        colInsuranceCompany.setMethodName("getInsCompName");
        colInsuranceCompany.setIsHeaderLink(true);
        colInsuranceCompany.setHeaderLinkTitle("Sort by: Healthcare Comp.");
        colInsuranceCompany.setDBColumnName("INS_COMP_NAME");
        addColumn(colInsuranceCompany);

        //Setting properties for Company Code    16
        Column colCompanyCode = new Column("Company Code");
        colCompanyCode.setMethodName("getInsCompCodeNbr");
        colCompanyCode.setIsHeaderLink(true);
        colCompanyCode.setHeaderLinkTitle("Sort by: Company Code");
        colCompanyCode.setDBColumnName("INS_COMP_CODE_NUMBER");
        addColumn(colCompanyCode);
        
        //BUSINESS DEVELOPMENT AND ANONYMOUS ********************************
        //Setting properties for Name     17
        Column colName = new Column("Caller Name");
        colName.setMethodName("getCallerName");
        colName.setColumnWidth("10%");
        colName.setIsHeaderLink(true);
        colName.setHeaderLinkTitle("Sort by: Caller Name");
        colName.setDBColumnName("CALLER_NAME");
        addColumn(colName);

        //Setting properties for image Assign     18
        Column colImage1 = new Column("");
        colImage1.setIsImage(true);
        colImage1.setIsImageLink(true);
        colImage1.setImageName("getAssignImageName");
        colImage1.setImageTitle("getAssignImageTitle");
        addColumn(colImage1);
        
    }// end of public void setTableProperties()
}// end of CallCenterTable Class