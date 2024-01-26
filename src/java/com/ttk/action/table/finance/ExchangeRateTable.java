/**
 * @ (#) AddressLableTable.java Dec 2nd, 2009
 * Project      : TTK HealthCare Services
 * File         : AddressLableTable.java
 * Author       : Navin Kumar R
 * Company      : Span Systems Corporation
 * Date Created : Dec 2nd, 2006
 *
 * @author       :  
 * Modified by   : 
 * Modified date :
 * Reason        :
 */
package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ExchangeRateTable extends Table
{
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
 /*       R.CURRENCY_CODE AS "Currency Code",

        C.CURRENCY_NAME AS "Currency Name",

        R.UNITS_PER_QAR AS "Units Per QAR",

        R.QAR_PER_UNIT AS "QAR Per Unit",

        R.CONVERSION_DATE AS "Conversion Rate",

        R.UPLOADED_DATE AS "Uploaded Date"*/

        //Setting properties for Claim Settlement No.
        Column colClaimSettNo = new Column("Currency Code");
        colClaimSettNo.setMethodName("getCurrencyCode");
        colClaimSettNo.setColumnWidth("20%");
        colClaimSettNo.setIsHeaderLink(true);
        colClaimSettNo.setHeaderLinkTitle("Sort by: Currency Code");
        colClaimSettNo.setIsLink(true);
        colClaimSettNo.setLinkTitle("Edit Currency Code");
        colClaimSettNo.setDBColumnName("Currency Code");
        addColumn(colClaimSettNo);

        //Setting properties for Enrollment Id
        Column colEnrollmentId = new Column("Currency Name");
        colEnrollmentId.setMethodName("getCurrencyName");
        colEnrollmentId.setColumnWidth("15%");
        colEnrollmentId.setIsHeaderLink(true);
        colEnrollmentId.setHeaderLinkTitle("Sort by: Currency Name");
        colEnrollmentId.setDBColumnName("Currency Name");
        addColumn(colEnrollmentId);

        //Setting properties for Member Name
        Column colClaimantName = new Column("Units per AED");
        colClaimantName.setMethodName("getUnitsperAED");
        colClaimantName.setColumnWidth("15%");
        colClaimantName.setIsHeaderLink(true);
        colClaimantName.setHeaderLinkTitle("Sort by: Units per AED");
        colClaimantName.setDBColumnName("Units Per QAR");
        addColumn(colClaimantName);

        //Setting properties for Claim Type
        Column colClaimType = new Column("AED per Unit");
        colClaimType.setMethodName("getAedperUnit");
        colClaimType.setColumnWidth("10%");
        colClaimType.setIsHeaderLink(true);
        colClaimType.setHeaderLinkTitle("Sort by: AED per Unit");
        colClaimType.setDBColumnName("QAR Per Unit");
        addColumn(colClaimType);

        //Setting properties for Approved Date
        Column colApprovedDate = new Column("Conversion Rate");
        colApprovedDate.setMethodName("getConversionDate");
        colApprovedDate.setColumnWidth("15%");
        colApprovedDate.setIsHeaderLink(true);
        colApprovedDate.setHeaderLinkTitle("Sort by: Conversion date");
        colApprovedDate.setDBColumnName("Conversion Rate");
        addColumn(colApprovedDate);

       //Setting properties for Payee
        Column colPayee = new Column("Uploaded Date");
        colPayee.setMethodName("getUploadedDate");
        colPayee.setColumnWidth("15%");
        colPayee.setIsHeaderLink(true);
        colPayee.setHeaderLinkTitle("Sort by: Uploaded Date");
        colPayee.setDBColumnName("Uploaded Date");
        addColumn(colPayee);

      
    }
}
