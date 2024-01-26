package com.ttk.action.table.processedcliams;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ProcessedClaimsTable extends Table {

	@Override
	public void setTableProperties() {

		    setRowCount(10);
	        setCurrentPage(1);
	        setPageLinkCount(10); 
		
	        Column colInvoiceNo=new Column("Invoice No.");
	        colInvoiceNo.setMethodName("getInvoiceNo");
	        colInvoiceNo.setColumnWidth("15%");
	        colInvoiceNo.setIsLink(true);
	        colInvoiceNo.setIsHeaderLink(true);
	       // colInvoiceNo.setImageName2("getInvImageName"); 
	       // colInvoiceNo.setImageTitle2("getInvImageTitle");
	        colInvoiceNo.setHeaderLinkTitle("Sort by: Invoice No.");
	        colInvoiceNo.setDBColumnName("Invoice_No");
	        addColumn(colInvoiceNo);
	        
	        
	        
	        Column colBatchNo=new Column("Batch No.");
	        colBatchNo.setMethodName("getBatchNo");
	        colBatchNo.setColumnWidth("10%");
	        colBatchNo.setIsHeaderLink(true);
	        colBatchNo.setHeaderLinkTitle("Sort by: Batch No.");
	        colBatchNo.setDBColumnName("Batch_No");
	        addColumn(colBatchNo);
	        
	        
	        Column colClaimNo=new Column("Claim No.");
	        colClaimNo.setMethodName("getClaimNo");
	        colClaimNo.setColumnWidth("10%");
	        //colClaimNo.setIsLink(true);
	        colClaimNo.setIsHeaderLink(true);
			//colClaimNo.setImageName("getShortfallImageName");
	       // colClaimNo.setImageTitle("getShortfallImageTitle");
	        //colClaimNo.setImageName2("getFarudimg");
	       // colClaimNo.setImageTitle2("getFraudImgTitle");
	        colClaimNo.setHeaderLinkTitle("Sort by: Claim No.");
	        colClaimNo.setDBColumnName("Claim_No");
	        addColumn(colClaimNo);
	        
	        
	        
	        Column colEnrollmentId=new Column("Al Koot ID");
	        colEnrollmentId.setMethodName("getEnrollmentID");
	        colEnrollmentId.setColumnWidth("10%");
	        colEnrollmentId.setIsHeaderLink(true);
	        colEnrollmentId.setHeaderLinkTitle("Sort by: Enrollment Id");
	        colEnrollmentId.setDBColumnName("Alkoot_ID");
	        addColumn(colEnrollmentId);
	        
	        
	        Column colHospitalName=new Column("Provider Name");
	        colHospitalName.setMethodName("getHospitalName");
	        colHospitalName.setColumnWidth("10%");
	        colHospitalName.setIsHeaderLink(true);
	        colHospitalName.setHeaderLinkTitle("Sort by: Provider Name");
	        colHospitalName.setDBColumnName("Provider_Name");
	        addColumn(colHospitalName);
	        
	        Column colClaimantName=new Column("Member Name");
	        colClaimantName.setMethodName("getClaimantName");
	        colClaimantName.setColumnWidth("10%");
	        colClaimantName.setIsHeaderLink(true);
	        colClaimantName.setHeaderLinkTitle("Sort by: Member Name");
	        colClaimantName.setDBColumnName("Member_Name");
	        addColumn(colClaimantName);
	        
	        Column colClaimType=new Column("Claim Type");
	        colClaimType.setMethodName("getClaimType");
	        colClaimType.setColumnWidth("7%");
	        colClaimType.setImageName("getPriorityImageName");
	        colClaimType.setImageTitle("getPriorityImageTitle");
	        colClaimType.setIsHeaderLink(true);
	        colClaimType.setHeaderLinkTitle("Sort by: Claim Type");
	        colClaimType.setDBColumnName("Claim_Type");
	        addColumn(colClaimType);
	        
	        Column colAssigned=new Column("Assigned TO");
	        colAssigned.setMethodName("getAssignedTo");
	        colAssigned.setColumnWidth("9%");
	        colAssigned.setIsHeaderLink(true);
	        colAssigned.setHeaderLinkTitle("Sort by: Assigned TO");
	        colAssigned.setDBColumnName("Assigned_to");
	        addColumn(colAssigned);
	        
	        Column colRecDate=new Column("Rec. Date");
	        colRecDate.setMethodName("getReceivedDateAsString");
	        colRecDate.setColumnWidth("7%");
	        colRecDate.setIsHeaderLink(true);
	      //  colRecDate.setImageName("getFastTrackFlagImageName");
	      //  colRecDate.setImageTitle("getFastTrackFlagImageTitle");
	        colRecDate.setHeaderLinkTitle("Sort by:Rec. Date");
	        colRecDate.setDBColumnName("Received_Date");
	        addColumn(colRecDate);
	        
	        
	        Column colApprovedAmt = new Column("Approved Amt In QAR");//13
	        colApprovedAmt.setMethodName("getApproveAmount");
	        colApprovedAmt.setColumnWidth("6%");
	        colApprovedAmt.setIsHeaderLink(true);
	        colApprovedAmt.setHeaderLinkTitle("Sort by:Approved Amount");
	        colApprovedAmt.setDBColumnName("Approved_amount_in_QAR");
			addColumn(colApprovedAmt);
	        
	        
			    Column colStatus=new Column("Status");
		        colStatus.setMethodName("getStatus");
		        colStatus.setColumnWidth("6%");
		        colStatus.setIsHeaderLink(true);
		        colStatus.setHeaderLinkTitle("Sort by: Status.");
		        colStatus.setDBColumnName("Status");
		      //  colStatus.setImageName("getInProImageName"); 
		       // colStatus.setImageTitle("getInProImageTitle");
		        addColumn(colStatus);
	        
		
	}

}
