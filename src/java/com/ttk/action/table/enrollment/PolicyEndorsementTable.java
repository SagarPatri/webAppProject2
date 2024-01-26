/**
 * @ (#) PolicyEndorsementTable.java Feb 2, 2006
 * Project      : TTK HealthCare Services
 * File         : PolicyEndorsementTable.java
 * Author       : Arun K N
 * Company      : Span Systems Corporation
 * Date Created : Feb 2, 2006
 *
 * @author       :  Arun K N
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.action.table.enrollment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * This class consists of the information requied to prepare grid table
 * for Policy Search screens of Individual Policy,Ind. Policy as Group,
 * Corporate Policy and Non-Corporate Policy of endorsement flow for 
 * Enrollment module.
 * 
 */
public class PolicyEndorsementTable extends Table{
    
    /**
     * This creates the columnproperties objects for each and 
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);
        
        //The order of the column should remain as same as here, if any new colum needs to be added then add at the end
        
        //Setting properties for Endorsement No.
        Column colEndorsementNo=new Column("Endorsement No.");
        colEndorsementNo.setMethodName("getEndorsementNbr");
        colEndorsementNo.setColumnWidth("20%");
        colEndorsementNo.setIsLink(true);
        colEndorsementNo.setIsHeaderLink(true);
        colEndorsementNo.setHeaderLinkTitle("Sort by: Endorsement No.");
        colEndorsementNo.setDBColumnName("ENDORSEMENT_NUMBER");
        addColumn(colEndorsementNo);
        
        //Setting properties for Cust. Endorsement No.
        Column colCustEndorsementNo=new Column("Cust. Endorsement No.");
        colCustEndorsementNo.setMethodName("getCustEndorsementNbr");
        colCustEndorsementNo.setColumnWidth("20%");
        colCustEndorsementNo.setIsHeaderLink(true);
        colCustEndorsementNo.setHeaderLinkTitle("Sort by: Cust. Endorsement No.");
        colCustEndorsementNo.setDBColumnName("CUST_ENDORSEMENT_NUMBER");
        addColumn(colCustEndorsementNo);
        
      /*  
        //Setting properties for Enrollment No.
        Column colQatarId=new Column("Qatar ID");
        colQatarId.setMethodName("getsQatarId");
        colQatarId.setColumnWidth("10%");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);
        
        */
        
        
        
        
        //Setting properties for Policy No 
        Column colPolicyNo=new Column("Policy No.");
        colPolicyNo.setMethodName("getPolicyNbr");
        colPolicyNo.setColumnWidth("10%");
        colPolicyNo.setIsLink(true);
        colPolicyNo.setIsHeaderLink(true);
        colPolicyNo.setLinkParamName("SecondLink");
        colPolicyNo.setHeaderLinkTitle("Sort by: Policy No.");
        colPolicyNo.setDBColumnName("POLICY_NUMBER");
        addColumn(colPolicyNo);
        
        //Setting properties for Group Name
        Column colGroupName=new Column("Group Name");
        colGroupName.setMethodName("getGroupName");
        colGroupName.setColumnWidth("19%");
        colGroupName.setIsHeaderLink(true);
        colGroupName.setHeaderLinkTitle("Sort by: Group Name");
        colGroupName.setDBColumnName("GROUP_NAME");
        addColumn(colGroupName);
        
        //Setting properties for Corp. Name
        Column colCorpName=new Column("Corp. Name");
        colCorpName.setMethodName("getGroupName");
        colCorpName.setColumnWidth("19%");
        colCorpName.setIsHeaderLink(true);
        colCorpName.setHeaderLinkTitle("Sort by: Corp. Name");
        colCorpName.setDBColumnName("GROUP_NAME");
        addColumn(colCorpName);
        
        //Setting properties for Agent Code
        Column colAgentCode=new Column("Agent Code");
        colAgentCode.setMethodName("getAgentCode");
        colAgentCode.setColumnWidth("12%");
        colAgentCode.setIsHeaderLink(true);
        colAgentCode.setHeaderLinkTitle("Sort by: Agent Code");
        colAgentCode.setDBColumnName("POLICY_AGENT_CODE");
        addColumn(colAgentCode);
        
        //Setting properties for Batch No.
        Column colBatchNo=new Column("Batch No.");
        colBatchNo.setMethodName("getBatchNbr");
        colBatchNo.setColumnWidth("13%");
        colBatchNo.setIsLink(true);
        colBatchNo.setLinkParamName("ThirdLink");
        colBatchNo.setIsHeaderLink(true);
        colBatchNo.setHeaderLinkTitle("Sort by: Batch No.");
        colBatchNo.setDBColumnName("BATCH_NUMBER");
        addColumn(colBatchNo);
        
        //Setting properties for Review
        Column colReview=new Column("Rev. Ct.");
        colReview.setMethodName("getReview");
        colReview.setIsHeaderLink(true);
        colReview.setHeaderLinkTitle("Sort by: Review Count");
        colReview.setColumnWidth("7%");
        colReview.setDBColumnName("REVIEW");
        addColumn(colReview);
        
        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);
    }//end of  setTableProperties()
}//end of EndorsementPolicyTable.java
