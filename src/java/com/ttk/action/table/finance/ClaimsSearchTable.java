/**
 * @ (#) ClaimsSearchTable.java June 12th, 2006
 * Project      : TTK HealthCare Services
 * File         : ClaimsSearchTable.java
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


public class ClaimsSearchTable extends Table
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
        colClaimSettNo.setColumnWidth("10%");
        colClaimSettNo.setIsHeaderLink(true);
        colClaimSettNo.setHeaderLinkTitle("Sort by: Claim Settlement No.");
        colClaimSettNo.setIsLink(true);
        colClaimSettNo.setLinkTitle("Edit Claim Settlement No");
        colClaimSettNo.setDBColumnName("CLAIM_SETTLEMENT_NO");
        addColumn(colClaimSettNo);

        //Setting properties for Enrollment Id
        Column colEnrollmentId = new Column("AlKoot Id");
        colEnrollmentId.setMethodName("getEnrollID");
        colEnrollmentId.setColumnWidth("7%");
        colEnrollmentId.setIsHeaderLink(true);
        colEnrollmentId.setHeaderLinkTitle("Sort by: Enrollment Id");
        colEnrollmentId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colEnrollmentId);

        
        //Setting properties for  Qatar ID.
       /* Column colQatarId=new Column("Qatar ID");
        colQatarId.setMethodName("getsQatarId");
        colQatarId.setColumnWidth("7%");
        colQatarId.setIsHeaderLink(true);
        colQatarId.setHeaderLinkTitle("Sort by: Qatar ID");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);*/
        
        
        
        
        
        
        //Setting properties for Member Name
        Column colClaimantName = new Column("Member Name");
        colClaimantName.setMethodName("getClaimantName");
        colClaimantName.setColumnWidth("8%");
        colClaimantName.setIsHeaderLink(true);
        colClaimantName.setHeaderLinkTitle("Sort by: Member Name");
        colClaimantName.setDBColumnName("MEM_NAME");
        addColumn(colClaimantName);

        //Setting properties for Claim Type
      /*  Column colClaimType = new Column("Claim Type");
        colClaimType.setMethodName("getClaimTypeDesc");
        colClaimType.setColumnWidth("5%");
        colClaimType.setIsHeaderLink(true);
        colClaimType.setHeaderLinkTitle("Sort by: Claim Type");
        colClaimType.setDBColumnName("CLAIM_TYPE");
        addColumn(colClaimType);*/
        
        Column colAgreedDays = new Column("Payment Term Agreed (In Days)");
        colAgreedDays.setMethodName("getProviderAgreedDays");
        colAgreedDays.setColumnWidth("10%");
        colAgreedDays.setIsHeaderLink(true);
        colAgreedDays.setHeaderLinkTitle("Sort by: Payment Term Agreed");
        colAgreedDays.setDBColumnName("PAYMENT_DUR_AGR");
        addColumn(colAgreedDays);
        
        Column colClaimAge = new Column("Claim Age (In Days)");
        colClaimAge.setMethodName("getClaimAge");
        colClaimAge.setColumnWidth("10%");
        colClaimAge.setIsHeaderLink(true);
        colClaimAge.setHeaderLinkTitle("Sort by: Claim Age");
        colClaimAge.setDBColumnName("CLAIM_AGE");
        addColumn(colClaimAge);

        //Setting properties for Corporate Name
        Column colCorpName = new Column("Corporate Name");
        colCorpName.setMethodName("getCorporateName");
        colCorpName.setColumnWidth("8%");
        colCorpName.setIsHeaderLink(true);
        colCorpName.setHeaderLinkTitle("Sort by: Corporate Name");
        colCorpName.setDBColumnName("CORPORATE_NAME");
        addColumn(colCorpName);

        //Setting properties for Claim received Date
        Column colReceivedDate = new Column("Received Date");
        colReceivedDate.setMethodName("getReceivedDate");
        colReceivedDate.setColumnWidth("7%");
        colReceivedDate.setIsHeaderLink(true);
        colReceivedDate.setImageName("getFastTrackFlagImageName");
        colReceivedDate.setImageTitle("getFastTrackFlagImageTitle");
        colReceivedDate.setHeaderLinkTitle("Sort by: Received Date");
        colReceivedDate.setDBColumnName("CLAIM_RECV_DATE");
        addColumn(colReceivedDate);

        //Setting properties for Approved Date
        Column colApprovedDate = new Column("Approved Date");
        colApprovedDate.setMethodName("getApprovedDate");
        colApprovedDate.setColumnWidth("7%");
        colApprovedDate.setIsHeaderLink(true);
        colApprovedDate.setHeaderLinkTitle("Sort by: Approved Date");
        colApprovedDate.setDBColumnName("CLAIM_APRV_DATE");
        addColumn(colApprovedDate);
        
       //Setting properties for Payee
        Column colPayee = new Column("Payee");
        colPayee.setMethodName("getInFavorOf");
        colPayee.setColumnWidth("7%");
        colPayee.setIsHeaderLink(true);
        colPayee.setHeaderLinkTitle("Sort by: Payee");
        colPayee.setDBColumnName("IN_FAVOUR_OF");
        addColumn(colPayee);        
        
        Column curencyFormat = new Column("Incurred Currency");
        curencyFormat.setMethodName("getIncuredCurencyFormat");
        curencyFormat.setColumnWidth("9%");
        curencyFormat.setIsHeaderLink(true);
        curencyFormat.setHeaderLinkTitle("Sort by: Incurred Currency");
        curencyFormat.setDBColumnName("REQ_AMT_CURRENCY_TYPE");
        addColumn(curencyFormat);
        
        Column convertedApprovedAmt = new Column("Approved Amt in Incurred Currency.");
        convertedApprovedAmt.setMethodName("getConvertedApprovedAmount");
        convertedApprovedAmt.setColumnWidth("9%");
        convertedApprovedAmt.setIsHeaderLink(true);
        convertedApprovedAmt.setHeaderLinkTitle("Sort by: Approved Amt in Incurred Currency.");
        convertedApprovedAmt.setDBColumnName("APPROVED_AMOUNT");
        addColumn(convertedApprovedAmt);
        
        
        //Setting properties for Claim Amt. (Rs)
        Column colClaimAmt = new Column("Approved Amt In QAR.");
        colClaimAmt.setMethodName("getClaimAmt");
        colClaimAmt.setColumnWidth("8%");
        colClaimAmt.setIsHeaderLink(true);
        colClaimAmt.setHeaderLinkTitle("Sort by: Approved Amt In QAR.");
        colClaimAmt.setDBColumnName("APPROVED_AMOUNT");
        addColumn(colClaimAmt);
        
        Column colDiscountAmt = new Column("Discount Amt (In QAR)..");
        colDiscountAmt.setMethodName("getDiscountedAmount");
        colDiscountAmt.setColumnWidth("9%");
        colDiscountAmt.setHeaderLinkTitle("Discounted Amount-Fast Track (In QAR)");
        colDiscountAmt.setIsHeaderLink(true);
        colDiscountAmt.setHeaderLinkTitle("Sort by: Discounted Amount");
        colDiscountAmt.setDBColumnName("DISC_AMOUNT");
        addColumn(colDiscountAmt);
        
        Column colPayableAmt = new Column("Payable Amount (In QAR)..");
        colPayableAmt.setMethodName("getPayableAmount");
        colPayableAmt.setColumnWidth("9%");
        colPayableAmt.setHeaderLinkTitle("Payable Amount after Discount (In QAR)");
        colPayableAmt.setIsHeaderLink(true);
        colPayableAmt.setHeaderLinkTitle("Sort by: Payable Amount");
        colPayableAmt.setDBColumnName("AMT_PAID_AF_DISC");
        addColumn(colPayableAmt);
        
        Column payableAmntInUSD = new Column("Payable Amt in USD / GBP / EURO");
        payableAmntInUSD.setMethodName("getUsdAmount");
        payableAmntInUSD.setColumnWidth("8%");
        payableAmntInUSD.setIsHeaderLink(true);
        payableAmntInUSD.setHeaderLinkTitle("Sort by: Payable Amt in USD / GBP / EURO");
        payableAmntInUSD.setDBColumnName("USD_AMOUNT");
        addColumn(payableAmntInUSD);
        

        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        //colSelect.setWidth(10);
        addColumn(colSelect);
    }

}
