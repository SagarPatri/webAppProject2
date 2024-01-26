/**
 * @ (#) PolicyEnrollmentTable.java Feb 1, 2006
 * Project      : TTK HealthCare Services
 * File         : PolicyEnrollmentTable
 * Author       : Arun K N
 * Company      : Span Systems Corporation
 * Date Created : Feb 1, 2006
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
 * Corporate Policy and Non-Corporate Policy of enrollment module
 * 
 */
public class PolicyEnrollmentTable extends Table
{
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
        
        //Setting properties for Scheme No 
        Column colPolicyNo=new Column("Policy No.");
        colPolicyNo.setMethodName("getPolicyNbr");
        colPolicyNo.setColumnWidth("20%");
        colPolicyNo.setImageName("getImageName");
        colPolicyNo.setImageTitle("getImageTitle");
        colPolicyNo.setIsLink(true);
        colPolicyNo.setIsHeaderLink(true);
        colPolicyNo.setHeaderLinkTitle("Sort by: Policy No.");
        colPolicyNo.setDBColumnName("POLICY_NUMBER");
        addColumn(colPolicyNo);
        
        //Setting properties for Enrollment No.
        Column colEnrollmentNo=new Column("Al Koot Id");
        colEnrollmentNo.setMethodName("getEnrollmentNbr");
        colEnrollmentNo.setColumnWidth("12%");
        colEnrollmentNo.setIsLink(true);
        colEnrollmentNo.setIsHeaderLink(true);
        colEnrollmentNo.setLinkParamName("SecondLink");
        colEnrollmentNo.setHeaderLinkTitle("Sort by: Enrollment No.");
        colEnrollmentNo.setDBColumnName("TPA_ENROLLMENT_NUMBER");
        addColumn(colEnrollmentNo);
        
        
        
       /* 
        
      //Setting properties for Enrollment No.
        Column colQatarId=new Column("Qatar ID");
        colQatarId.setMethodName("getsQatarId");
        colQatarId.setColumnWidth("12%");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);
        
        */
        
        
        
        
        
        //Setting properties for Group Name
        Column colGroupName=new Column("Group Name");
        colGroupName.setMethodName("getGroupName");
        colGroupName.setColumnWidth("13%");
        colGroupName.setIsHeaderLink(true);
        colGroupName.setHeaderLinkTitle("Sort by: Group Name");
        colGroupName.setDBColumnName("GROUP_NAME");
        addColumn(colGroupName);
        
        //Setting properties for Corp. Name
        Column colCorpName=new Column("Corp. Name");
        colCorpName.setMethodName("getGroupName");
        colCorpName.setColumnWidth("13%");
        colCorpName.setIsHeaderLink(true);
        colCorpName.setHeaderLinkTitle("Sort by: Corp. Name");
        colCorpName.setDBColumnName("GROUP_NAME");
        addColumn(colCorpName);
        
        //Setting properties for Member Name
        Column colMemberName=new Column("Member Name");
        colMemberName.setMethodName("getMemberName");
        colMemberName.setColumnWidth("24%");
        colMemberName.setIsHeaderLink(true);
        colMemberName.setHeaderLinkTitle("Sort by: Member Name");
        colMemberName.setDBColumnName("MEM_NAME");
        addColumn(colMemberName);
        
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
        
    }//end of setTableProperties()
}//end of EnrollmentPolicyTable.java
