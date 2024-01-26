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

package com.ttk.action.table.claims;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ClaimsTable extends Table
{
	
	public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);        
      //Setting properties for Claim No.
        
        
        Column colInvoiceNo=new Column("Invoice No.");
        colInvoiceNo.setMethodName("getInvoiceNo");
        colInvoiceNo.setColumnWidth("1%");
        colInvoiceNo.setIsLink(true);
        colInvoiceNo.setIsHeaderLink(true);
        colInvoiceNo.setImageName2("getInvImageName"); 
        colInvoiceNo.setImageTitle2("getInvImageTitle");
        colInvoiceNo.setImageName("getResubmissionImageName");
        colInvoiceNo.setImageTitle("getResubmissionImageTitle");
        colInvoiceNo.setHeaderLinkTitle("Sort by: Invoice No.");
        colInvoiceNo.setDBColumnName("INVOICE_NUMBER");
        addColumn(colInvoiceNo);
        
        /*Column priorityCorpColumn=new Column("Priority Corporate");
        priorityCorpColumn.setMethodName("getPriorityCorporate");
        priorityCorpColumn.setColumnWidth("8%");
        priorityCorpColumn.setLinkParamMethodName("ClaimPrioryCorp");
        priorityCorpColumn.setIsImage(true);
        priorityCorpColumn.setIsImageLink(false);
        priorityCorpColumn.setImageName("getPriorityImageName");
        priorityCorpColumn.setImageTitle("getPriorityImageTitle");
        priorityCorpColumn.setVisibility(true);
        priorityCorpColumn.isComponent();
        addColumn(priorityCorpColumn);*/
        
      //Setting properties for Claim No.
        Column colBatchNo=new Column("Batch No.");
        colBatchNo.setMethodName("getBatchNo");
        colBatchNo.setColumnWidth("14%");
        colBatchNo.setIsHeaderLink(true);
        colBatchNo.setHeaderLinkTitle("Sort by: Batch No.");
        colBatchNo.setDBColumnName("BATCH_NO");
        
        addColumn(colBatchNo);

        //Setting properties for Claim No.
        Column colClaimNo=new Column("Claim No.");
        colClaimNo.setMethodName("getClaimNo");
        colClaimNo.setColumnWidth("10%");
        //colClaimNo.setIsLink(true);
        colClaimNo.setIsHeaderLink(true);
		colClaimNo.setImageName("getShortfallImageName");
        colClaimNo.setImageTitle("getShortfallImageTitle");
        colClaimNo.setImageName2("getFarudimg");
        colClaimNo.setImageTitle2("getFraudImgTitle");
        colClaimNo.setHeaderLinkTitle("Sort by: Claim No.");
        colClaimNo.setDBColumnName("CLAIM_NUMBER");
        addColumn(colClaimNo);

        //Setting properties for  Enrollment Id.
        Column colEnrollmentId=new Column("Al Koot ID");
        colEnrollmentId.setMethodName("getEnrollmentID");
        colEnrollmentId.setColumnWidth("8%");
        colEnrollmentId.setIsHeaderLink(true);
        colEnrollmentId.setHeaderLinkTitle("Sort by: Enrollment Id");
        colEnrollmentId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colEnrollmentId);
        
        
        
        //Setting properties for  Qatar ID.
        /*Column colQatarId=new Column("Qatar ID");
        colQatarId.setMethodName("getsQatarId");
        colQatarId.setColumnWidth("8%");
        colQatarId.setIsHeaderLink(true);
        colQatarId.setHeaderLinkTitle("Sort by: Qatar ID");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);*/
        
        
        
        
       /* Column colSubmissionType=new Column("Submission Type");
        colSubmissionType.setMethodName("getProcessType");
        colSubmissionType.setColumnWidth("10%");
        colSubmissionType.setIsHeaderLink(true);
        colSubmissionType.setHeaderLinkTitle("Sort by: Submission Type");
        colSubmissionType.setDBColumnName("PROCESS_TYPE");
        addColumn(colSubmissionType);*/
        
        Column colClaimAge = new Column("Claim Age (In Days)");
        colClaimAge.setMethodName("getClaimAge");
        colClaimAge.setColumnWidth("10%");
        colClaimAge.setIsHeaderLink(true);
        //colClaimAge.setHeaderLinkTitle("Sort by: Claim Type");
        colClaimAge.setDBColumnName("CLAIM_AGE");
        addColumn(colClaimAge);
       
      //Setting properties for  Alternate Id.
        /*Column colAlternateId=new Column("Alternate Id");
        colAlternateId.setMethodName("getStrAlternateID");				// vo  get() method name
        colAlternateId.setColumnWidth("14%");
        colAlternateId.setIsHeaderLink(true);
        colAlternateId.setHeaderLinkTitle("Sort by: Alternate Id");
        colAlternateId.setDBColumnName("TPA_ALTERNATE_ID");			// database column name
        addColumn(colAlternateId);
        */
        
        
        
        //Setting properties for  Policy NO.
     /*   Column colPolicyNo=new Column("Policy No");
        colPolicyNo.setMethodName("getPolicyNo");
        colPolicyNo.setColumnWidth("14%");
        colPolicyNo.setIsHeaderLink(true);
        colPolicyNo.setHeaderLinkTitle("Sort by: Policy No");
        colPolicyNo.setDBColumnName("POLICY_NO");
        addColumn(colPolicyNo);
*/
        //Setting properties for Hospital Name.
        Column colHospitalName=new Column("Provider Name");
        colHospitalName.setMethodName("getHospitalName");
        colHospitalName.setColumnWidth("12%");
        colHospitalName.setIsHeaderLink(true);
        colHospitalName.setHeaderLinkTitle("Sort by: Provider Name");
        colHospitalName.setDBColumnName("HOSP_NAME");
        addColumn(colHospitalName);
       
        //Setting properties for  Member Name.
        Column colClaimantName=new Column("Member Name");
        colClaimantName.setMethodName("getClaimantName");
        colClaimantName.setColumnWidth("10%");
        colClaimantName.setIsHeaderLink(true);
        colClaimantName.setHeaderLinkTitle("Sort by: Member Name");
        colClaimantName.setDBColumnName("MEM_NAME");
        addColumn(colClaimantName);

      //Setting properties for Claim Type.
        Column colClaimType=new Column("Claim Type");
        colClaimType.setMethodName("getClaimType");
        colClaimType.setColumnWidth("10%");
        colClaimType.setImageName("getPriorityImageName");
        colClaimType.setImageTitle("getPriorityImageTitle");
        colClaimType.setIsHeaderLink(true);
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
        colRecDate.setIsHeaderLink(true);
        colRecDate.setImageName("getFastTrackFlagImageName");
        colRecDate.setImageTitle("getFastTrackFlagImageTitle");
        colRecDate.setHeaderLinkTitle("Sort by:Rec. Date");
        colRecDate.setDBColumnName("RECEIVED_DATE");
        addColumn(colRecDate);

        /*Column colRequestedAmt = new Column("Requested Amt In QAR");//13
        colRequestedAmt.setMethodName("getConvertedAmount");
        colRequestedAmt.setColumnWidth("10%");
        colRequestedAmt.setIsHeaderLink(true);
        colRequestedAmt.setHeaderLinkTitle("Sort by:Requested Amount");
        colRequestedAmt.setDBColumnName("CONVERTED_AMOUNT");
		addColumn(colRequestedAmt);*/
        
        Column colApprovedAmt = new Column("Approved Amt In QAR");//13
        colApprovedAmt.setMethodName("getApproveAmount");
        colApprovedAmt.setColumnWidth("10%");
        colApprovedAmt.setIsHeaderLink(true);
        colApprovedAmt.setHeaderLinkTitle("Sort by:Approved Amount");
        colApprovedAmt.setDBColumnName("approved_amount");
		addColumn(colApprovedAmt);
		
      		 //Koc Decoupling
        Column colStatus=new Column("Status.");
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("15%");
        colStatus.setIsHeaderLink(true);
        colStatus.setHeaderLinkTitle("Sort by: Status.");
        colStatus.setDBColumnName("DESCRIPTION");
        colStatus.setImageName("getInProImageName"); 
        colStatus.setImageTitle("getInProImageTitle");
        addColumn(colStatus);      
              
        	/*Column colEventReferenceNo = new Column("Event Ref. No.");//13
    		colEventReferenceNo.setMethodName("getEventReferenceNo");
    		colEventReferenceNo.setColumnWidth("10%");
    		colEventReferenceNo.setIsHeaderLink(true);
    		colEventReferenceNo.setHeaderLinkTitle("Sort by:Event Ref. No.");
    		colEventReferenceNo.setDBColumnName("event_no");
    		addColumn(colEventReferenceNo);*/
        
        Column colEventReferenceNo = new Column("Payment Term Agreed (In Days)");//13
		colEventReferenceNo.setMethodName("getProviderAgreedDays");
		colEventReferenceNo.setColumnWidth("10%");
		colEventReferenceNo.setIsHeaderLink(true);
		//colEventReferenceNo.setHeaderLinkTitle("Sort by:Event Ref. No.");
		colEventReferenceNo.setDBColumnName("PAYMENT_DUR_AGR");
		addColumn(colEventReferenceNo);
        
        //Setting properties for image Assighn
        Column colImage3 = new Column("<a href=\"#\" onClick=\"javascript:onAssignUser()\"><img src=\"/ttk/images/UserIcon.gif\" width=\"16\" height=\"16\" alt=\"Multiple Assign\" title=\"Multiple Assign\" border=\"0\"></a>");
        colImage3.setIsImage(true);
        colImage3.setIsImageLink(true);
        colImage3.setImageName("getAssignImageName");
        colImage3.setImageTitle("getAssignImageTitle");
        colImage3.setVisibility(true);
        addColumn(colImage3);

        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        colSelect.setColumnWidth("1%");
        addColumn(colSelect);
        
    
    } //end of setTableProperties()
}// end of ClaimsTable class


