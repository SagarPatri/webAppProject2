/**

* @ (#) ClaimsTable.java Jul 14, 2006
* Project       : TTK HealthCare Services
* File          : ClaimsTable.java
* Author        : Chandrasekaran J
* Company       : Span Systems Corporation
* Date Created  : Jul 14, 2006

* @author       : Chandrasekaran J
* Modified by   :
* Modified date :
* Reason :
*/

package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class AlertClaimsTable extends Table
{
	
	public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);        

        //Setting properties for Claim No.
        Column colClaimNo=new Column("Claim No.");
        colClaimNo.setMethodName("getClaimNo");
        colClaimNo.setColumnWidth("10%");
        colClaimNo.setIsLink(true);
        colClaimNo.setHeaderLinkTitle("Sort by: Claim No.");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimNo);

        //Setting properties for  Enrollment Id.
        Column colEnrollmentId=new Column("Al Koot ID");
        colEnrollmentId.setMethodName("getEnrollmentID");
        colEnrollmentId.setColumnWidth("8%");
        colEnrollmentId.setHeaderLinkTitle("Sort by: Enrollment Id");
        colEnrollmentId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colEnrollmentId);
        

      //Setting properties for Claim Type.
        Column colClaimType=new Column("Claim Type");
        colClaimType.setMethodName("getClaimType");
        colClaimType.setColumnWidth("10%");
        colClaimType.setHeaderLinkTitle("Sort by: Claim Type");
        colClaimType.setDBColumnName("CLAIM_TYPE");
        addColumn(colClaimType);
        
        //Setting properties for Assigned To Type.
        Column colAssigned=new Column("Assigned TO");
        colAssigned.setMethodName("getAssignedTo");
        colAssigned.setColumnWidth("10%");
        colAssigned.setIsHeaderLink(true);
        colAssigned.setHeaderLinkTitle("Sort by: Assigned TO");
        colAssigned.setDBColumnName("CONTACT_NAME");
        addColumn(colAssigned);
        
        //Setting properties for  Adm. Date.
        Column colRecDate=new Column("Rec. Date");
        colRecDate.setMethodName("getReceivedDateAsString");
        colRecDate.setColumnWidth("10%");
        colRecDate.setHeaderLinkTitle("Sort by:Rec. Date");
        colRecDate.setDBColumnName("RECEIVED_DATE");
        addColumn(colRecDate);
        
        Column colApprovedAmt = new Column("Approved Amt In QAR");//13
        colApprovedAmt.setMethodName("getApproveAmount");
        colApprovedAmt.setColumnWidth("10%");
        colApprovedAmt.setHeaderLinkTitle("Sort by:Approved Amount");
        colApprovedAmt.setDBColumnName("approved_amount");
		addColumn(colApprovedAmt);
		
      		 //Koc Decoupling
        Column colStatus=new Column("Status.");
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("15%");
        colStatus.setHeaderLinkTitle("Sort by: Status.");
        colStatus.setDBColumnName("DESCRIPTION");

        addColumn(colStatus);      
              
    
    } //end of setTableProperties()
}// end of ClaimsTable class


