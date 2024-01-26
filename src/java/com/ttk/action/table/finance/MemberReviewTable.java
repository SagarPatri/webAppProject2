package com.ttk.action.table.finance;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class MemberReviewTable extends Table {

	
	public void setTableProperties() {
		
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        
        //Setting properties for Policy No.
        Column colAccountName = new Column("Policy No.");
        colAccountName.setMethodName("getPolicyNumber");
        colAccountName.setColumnWidth("20%");
        //colAccountName.setLinkTitle("Edit Policy No.");
       // colAccountName.setIsLink(true);
        colAccountName.setIsHeaderLink(true);
        colAccountName.setHeaderLinkTitle("Sort by Policy No.");
        colAccountName.setDBColumnName("POLICY_NUMBER");
       // colAccountName.setLinkParamName("SecondLink");
        addColumn(colAccountName);
        
        
        //Setting properties for Policy Start Date
        Column colStartDate = new Column("Policy Start Date");
        colStartDate.setMethodName("getStrStartDate");
        colStartDate.setColumnWidth("10%");
        colStartDate.setIsHeaderLink(true);
        colStartDate.setHeaderLinkTitle("Policy Start Date");
        colStartDate.setDBColumnName("EFFECTIVE_FROM_DATE");
        addColumn(colStartDate);
        
        //Setting properties for Policy End Date
        Column colEndDate = new Column("Policy End Date");
        colEndDate.setMethodName("getStrEndDate");
        colEndDate.setColumnWidth("10%");
        colEndDate.setIsHeaderLink(true);
        colEndDate.setHeaderLinkTitle("Sort by Policy End Date");
        colEndDate.setDBColumnName("EFFECTIVE_TO_DATE");
        addColumn(colEndDate);
        
        
      //Setting properties for Alkoot No.
        Column colDhaId = new Column("Alkoot No.");
        colDhaId.setMethodName("getEnrollNmbr");
        colDhaId.setColumnWidth("10%");
        colDhaId.setLinkTitle("Edit Alkoot No.");
        colDhaId.setIsLink(true);
        colDhaId.setIsHeaderLink(true);
        colDhaId.setHeaderLinkTitle("Sort by Alkoot No.");
        colDhaId.setDBColumnName("TPA_ENROLLMENT_NUMBE");
        addColumn(colDhaId);
        
      //Setting properties for Policy Type
        Column colEmnalDesc = new Column("Policy Type");
        colEmnalDesc.setMethodName("getPolicyType");
        colEmnalDesc.setColumnWidth("10%");
        colEmnalDesc.setIsHeaderLink(true);
        colEmnalDesc.setHeaderLinkTitle("Sort by Policy Type");
        colEmnalDesc.setDBColumnName("ENROL_TYPE_ID");
        addColumn(colEmnalDesc);
     
        //Setting properties for Beneficiary Name
        Column colHospBankACNo = new Column("Beneficiary Name");
        colHospBankACNo.setMethodName("getInsureName");
        colHospBankACNo.setColumnWidth("15%");
        colHospBankACNo.setIsHeaderLink(true);
        colHospBankACNo.setHeaderLinkTitle("Sort by Beneficiary Name");
        colHospBankACNo.setDBColumnName("INSURED_NAME");
        addColumn(colHospBankACNo);
        
      //Setting properties for Account No
        Column colBankName = new Column("Account No");
        colBankName.setMethodName("getBankAccno");
        colBankName.setColumnWidth("10%");
        colBankName.setIsHeaderLink(true);
        colBankName.setHeaderLinkTitle("Sort by Account No");
        colBankName.setDBColumnName("BANK_ACCOUNT_NO");
        addColumn(colBankName);
        
      //Setting properties for IBAN
        Column colIban = new Column("IBAN");
        colIban.setMethodName("getiBanNo");
        colIban.setColumnWidth("10%");
        colIban.setIsHeaderLink(true);
        colIban.setHeaderLinkTitle("Sort by IBAN");
        colIban.setDBColumnName("IBAN_NO");
        addColumn(colIban);
        
      //Setting properties for SWIFT CODE
        Column colSwiftCode = new Column("Swift Code");
        colSwiftCode.setMethodName("getSwiftCode");
        colSwiftCode.setColumnWidth("10%");
        colSwiftCode.setIsHeaderLink(true);
        colSwiftCode.setHeaderLinkTitle("Sort by Swift Code");
        colSwiftCode.setDBColumnName("SWIFT_CODE");
        addColumn(colSwiftCode);
        
        
        //Setting properties for TTK Branch
        Column colTTKBranch = new Column("Al Koot Branch");
        colTTKBranch.setMethodName("getBankBranch");
        colTTKBranch.setColumnWidth("15%");
        colTTKBranch.setIsHeaderLink(true);
        colTTKBranch.setHeaderLinkTitle("Sort by Al Koot Branch");
        colTTKBranch.setDBColumnName("OFFICE_NAME");
        addColumn(colTTKBranch);
    
    }

}
