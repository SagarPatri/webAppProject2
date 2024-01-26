package com.ttk.action.table.onlineforms;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class EmpDisplayBenefitsTable extends Table{
	public void setTableProperties()
    {
        setRowCount(500);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Member Name
        Column colBENEFITNAME=new Column("Benefit");
        colBENEFITNAME.setMethodName("getBenefitName");
        colBENEFITNAME.setColumnWidth("10%");
        colBENEFITNAME.setImageName("");
        colBENEFITNAME.setImageTitle("");
        colBENEFITNAME.setDBColumnName("CLAUSE_NAME");
        addColumn(colBENEFITNAME);

        //Setting properties for Claim No.
   /*     Column colSUBBENEFITNAME=new Column("Sub Benefit");
        colSUBBENEFITNAME.setMethodName("getSubBenefitNoBr");
        colSUBBENEFITNAME.setColumnWidth("13%");
       // colSUBBENEFITNAME.setIsHeaderLink(true);
       // colSUBBENEFITNAME.setHeaderLinkTitle("Sort by: SUB BENEFIT NAME.");
        colSUBBENEFITNAME.setDBColumnName("COND_NAME");
        addColumn(colSUBBENEFITNAME);*/ 
        

        Column colConditionName=new Column("Sub Benefit");
        colConditionName.setMethodName("getCondition");
        colConditionName.setColumnWidth("18%");
        colConditionName.setDBColumnName("COND_NAME");
        addColumn(colConditionName);

        //Setting properties for  Type of Benefit
        Column colLIMIT=new Column("Configuration");
        colLIMIT.setMethodName("getStrConfigration");
        colLIMIT.setColumnWidth("10%");
        colLIMIT.setDBColumnName("RULE_CONFIG");
        addColumn(colLIMIT);

        //Setting properties for  Parent Claim No.
   /*     Column colCOPAY=new Column("Coverage");
        colCOPAY.setMethodName("getCoverage");
        colCOPAY.setColumnWidth("5%");
        colCOPAY.setDBColumnName("COND_NAME");
        addColumn(colCOPAY);*/

        //Setting properties for  Claim Approved Date
        Column colDEDUCTABLE=new Column("Limit");
        colDEDUCTABLE.setMethodName("getLimit");
        colDEDUCTABLE.setColumnWidth("5%");
        colDEDUCTABLE.setDBColumnName("LIMIT");
        addColumn(colDEDUCTABLE);
        
        
        Column colSESSIONS=new Column("Copay (%)");
        colSESSIONS.setMethodName("getCopay");
        colSESSIONS.setColumnWidth("5%");
        colSESSIONS.setDBColumnName("COPAY_PERC");
        addColumn(colSESSIONS);
        
        Column colMemberWaitingPeriod=new Column("Deductible");
        colMemberWaitingPeriod.setMethodName("getDeductible");
        colMemberWaitingPeriod.setColumnWidth("5%");
        colMemberWaitingPeriod.setDBColumnName("DEDUCTABLE");
        addColumn(colMemberWaitingPeriod);
       
        
        Column colAntenatelScans=new Column("Waiting Period");
        colAntenatelScans.setMethodName("getWaitingPeriod");
        colAntenatelScans.setColumnWidth("6%");
        colAntenatelScans.setDBColumnName("WAITING_PERIOD");
        addColumn(colAntenatelScans);
        
        
        Column colOPVisits=new Column("Sessions Allowed");
        colOPVisits.setMethodName("getSessionAllowed");
        colOPVisits.setColumnWidth("8%");
        colOPVisits.setDBColumnName("SESSIONS_BENIFIT");
        addColumn(colOPVisits);
        
        
        Column colLscsCopay=new Column("Mode Type");
        colLscsCopay.setMethodName("getModeType");
        colLscsCopay.setColumnWidth("10%");
        colLscsCopay.setDBColumnName("MODE_OF_BENIFIT");
        addColumn(colLscsCopay);
        
        
  /*      Column colNormalDeleviryCopay=new Column("Other Remarks");
        colNormalDeleviryCopay.setMethodName("getOtherRemarks");
        colNormalDeleviryCopay.setColumnWidth("5%");
        colNormalDeleviryCopay.setDBColumnName("OTHER_REMARKS");
        addColumn(colNormalDeleviryCopay);*/
        
       
        Column colAmmountUtilized=new Column("Limit Utilized");
        colAmmountUtilized.setMethodName("getLimitUtilised");
        colAmmountUtilized.setColumnWidth("8%");
        colAmmountUtilized.setDBColumnName("UTILIZED_AMNT");
        addColumn(colAmmountUtilized);
        
        Column colBalance=new Column("Limit Available");
        colBalance.setMethodName("getLimitAvailable");
        colBalance.setColumnWidth("8%");
        colBalance.setDBColumnName("BALNC_AMNT");
        addColumn(colBalance);
        
        //Setting properties for check box  
     /*   Column colSelect = new Column("Select");
        colSelect.setComponentType("radio");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);*/
    } //end of setTableProperties()
}