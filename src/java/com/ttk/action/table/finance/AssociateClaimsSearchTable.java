/**
 * @ (#) ClaimsSearchTable.java June 12th, 2006
 * Project      : TTK HealthCare Services
 * File         : AssociateClaimsSearchTable.java
 * Author       : Krupa J
 * Company      : Span Systems Corporation
 * Date Created : June 12th, 2006
 *
 * @author       :  Krupa J
 * Modified by   : Arun K.M
 * Modified date :30th oct 2006
 * Reason        :Added new column
 */
package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;


public class AssociateClaimsSearchTable extends Table
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public void setTableProperties()
    {
        setRowCount(10000);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Claim Settlement No.
        Column colClaimSettNo = new Column("Claim Settlement No.");
        colClaimSettNo.setMethodName("getClaimSettNo");
        colClaimSettNo.setColumnWidth("21%");
        colClaimSettNo.setIsHeaderLink(true);
        colClaimSettNo.setHeaderLinkTitle("Sort by: Claim Settlement No.");
        colClaimSettNo.setLinkTitle("Edit Claim Settlement No");
        colClaimSettNo.setDBColumnName("CLAIM_SETTLEMENT_NO");
        addColumn(colClaimSettNo);

        //Setting properties for Enrollment Id
        Column colEnrollmentId = new Column("Enrollment Id");
        colEnrollmentId.setMethodName("getEnrollID");
        colEnrollmentId.setColumnWidth("14%");
        colEnrollmentId.setIsHeaderLink(true);
        colEnrollmentId.setHeaderLinkTitle("Sort by: Enrollment Id");
        colEnrollmentId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colEnrollmentId);

        //Setting properties for Member Name
        Column colClaimantName = new Column("Member Name");
        colClaimantName.setMethodName("getClaimantName");
        colClaimantName.setColumnWidth("15%");
        colClaimantName.setIsHeaderLink(true);
        colClaimantName.setHeaderLinkTitle("Sort by: Member Name");
        colClaimantName.setDBColumnName("MEM_NAME");
        addColumn(colClaimantName);

        //Setting properties for Claim Type
        Column colClaimType = new Column("Claim Type");
        colClaimType.setMethodName("getClaimTypeDesc");
        colClaimType.setColumnWidth("10%");
        colClaimType.setIsHeaderLink(true);
        colClaimType.setHeaderLinkTitle("Sort by: Claim Type");
        colClaimType.setDBColumnName("CLAIM_TYPE");
        addColumn(colClaimType);

        //Setting properties for Approved Date
        Column colApprovedDate = new Column("Approved Date");
        colApprovedDate.setMethodName("getApprovedDate");
        colApprovedDate.setColumnWidth("13%");
        colApprovedDate.setIsHeaderLink(true);
        colApprovedDate.setHeaderLinkTitle("Sort by: Approved Date");
        colApprovedDate.setDBColumnName("CLAIM_APRV_DATE");
        addColumn(colApprovedDate);

       //Setting properties for Payee
        Column colPayee = new Column("Payee");
        colPayee.setMethodName("getInFavorOf");
        colPayee.setColumnWidth("14%");
        colPayee.setIsHeaderLink(true);
        colPayee.setHeaderLinkTitle("Sort by: Payee");
        colPayee.setDBColumnName("IN_FAVOUR_OF");
        addColumn(colPayee);

        //Setting properties for Claim Amt. (Rs)
        Column colClaimAmt = new Column("Approved Amt. (AED)");
        colClaimAmt.setMethodName("getClaimAmt");
        colClaimAmt.setColumnWidth("13%");
        colClaimAmt.setIsHeaderLink(true);
        colClaimAmt.setHeaderLinkTitle("Sort by: Claim Amt. (AED)");
        colClaimAmt.setDBColumnName("APPROVED_AMOUNT");
        addColumn(colClaimAmt);

        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        //colSelect.setWidth(10);
        addColumn(colSelect);
    }

}
