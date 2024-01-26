/**
 * @ (#) EnrollmentSearchTable.java May 4th, 2006
 * Project      : TTK HealthCare Services
 * File         : EnrollmentSearchTable.java
 * Author       : Krupa J
 * Company      : Span Systems Corporation
 * Date Created : May 4th
 *
 * @author       :
 * Modified by   :
 * Modified date :
 * Reason        :
 */
package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;


public class EnrollmentSearchTable extends Table
{

    private static final long serialVersionUID = 1L;

    public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Enrollment Id
        Column colEnrollmentId = new Column("Al Koot Id");
        colEnrollmentId.setMethodName("getEnrollmentID");
        colEnrollmentId.setColumnWidth("10%");
        colEnrollmentId.setIsHeaderLink(true);
        colEnrollmentId.setHeaderLinkTitle("Sort by: Al Koot Id");
        colEnrollmentId.setIsLink(true);
        colEnrollmentId.setLinkTitle("Edit Al Koot Id");
        colEnrollmentId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colEnrollmentId);
        
        //Setting properties for  Qatar ID.
        Column colQatarId=new Column("Qatar ID");
        colQatarId.setMethodName("getsQatarId");
        colQatarId.setColumnWidth("10%");
        colQatarId.setIsHeaderLink(true);
        colQatarId.setHeaderLinkTitle("Sort by: Qatar ID");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);
        
        //Setting properties for Member Name
        Column colClaimantName = new Column("Member Name");
        colClaimantName.setMethodName("getName");
        colClaimantName.setColumnWidth("15%");
        colClaimantName.setIsHeaderLink(true);
        colClaimantName.setHeaderLinkTitle("Sort by: Member Name");
        colClaimantName.setDBColumnName("MEM_NAME");
        addColumn(colClaimantName);
        
        //Setting properties for Gender
        Column colGender = new Column("Gender");
        colGender.setMethodName("getGenderDescription");
        colGender.setColumnWidth("7%");
        colGender.setIsHeaderLink(true);
        colGender.setHeaderLinkTitle("Sort by: Gender");
        colGender.setDBColumnName("GENDER");
        addColumn(colGender);

        //Setting properties for Age (Yrs)
        Column colAge = new Column("Age (Yrs)");
        colAge.setMethodName("getAge");
        colAge.setColumnWidth("6%");
        colAge.setIsHeaderLink(true);
        colAge.setHeaderLinkTitle("Sort by: Age (Yrs)");
        colAge.setDBColumnName("MEM_AGE");
        addColumn(colAge);

        //Setting properties for Policy No.
        Column colPolicyNo = new Column("Policy No.");
        colPolicyNo.setMethodName("getPolicyNbr");
        colPolicyNo.setColumnWidth("10%");
        colPolicyNo.setIsHeaderLink(true);
        colPolicyNo.setHeaderLinkTitle("Sort by: Policy No.");
        colPolicyNo.setDBColumnName("POLICY_NUMBER");
        addColumn(colPolicyNo);

        //Setting properties for Policy Start Date
        Column colPolicyStartDate=new Column("Policy Start Date");
        colPolicyStartDate.setMethodName("getListStartDate");
        colPolicyStartDate.setColumnWidth("9%");
        colPolicyStartDate.setIsHeaderLink(true);
        colPolicyStartDate.setHeaderLinkTitle("Sort by: Policy Start Date");
        colPolicyStartDate.setDBColumnName("EFFECTIVE_FROM_DATE");
        addColumn(colPolicyStartDate);
        
        //Setting properties for Policy End Date
        Column colPolicyEndDate=new Column("Policy End Date");
        colPolicyEndDate.setMethodName("getListEndDate");
        colPolicyEndDate.setColumnWidth("9%");
        colPolicyEndDate.setIsHeaderLink(true);
        colPolicyEndDate.setHeaderLinkTitle("Sort by: Policy End Date");
        colPolicyEndDate.setDBColumnName("EFFECTIVE_TO_DATE");
        addColumn(colPolicyEndDate);
        
       /* //Setting properties for Scheme/Certi. No.
        /*Column colSchemeCertNo = new Column("Scheme/Certi. No.");
        colSchemeCertNo.setMethodName("getSchemeCertNbr");
        colSchemeCertNo.setColumnWidth("13%");
        colSchemeCertNo.setIsHeaderLink(true);
        colSchemeCertNo.setHeaderLinkTitle("Sort by: Scheme/Certi. No.");
        colSchemeCertNo.setDBColumnName("SCHEME_OR_CERTFICATE");
        addColumn(colSchemeCertNo);*/

        //Setting properties for Corp. Name
        Column colCorpName = new Column("Corp. Name");
        colCorpName.setMethodName("getGroupName");
        colCorpName.setColumnWidth("10%");
        colCorpName.setIsHeaderLink(true);
        colCorpName.setHeaderLinkTitle("Sort by: Corp. Name");
        colCorpName.setDBColumnName("GROUP_NAME");
        addColumn(colCorpName);
    }// end of public void setTableProperties()
}// end of EnrollmentSearchTable class