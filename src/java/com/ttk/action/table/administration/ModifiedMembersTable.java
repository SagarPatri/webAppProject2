/**
* @ (#) BufferListTable.java Jun 19, 2006
* Project 		: TTK HealthCare Services
* File			: BufferListTable.java
* Author 		: Pradeep R
* Company 		: Span Systems Corporation
* Date Created  : Jun 19, 2006
*
* @author 		: Pradeep R
* Modified by 	:
* Modified date :
* Reason :
*/
package com.ttk.action.table.administration;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;


public class ModifiedMembersTable extends Table {


    public void setTableProperties() {
        setRowCount(10);
        setCurrentPage(1);
        setPageLinkCount(10);

        Column alkootId = new Column("Alkoot ID"); // old Member ID
        alkootId.setMethodName("getMemberId");
        alkootId.setColumnWidth("10%");
        alkootId.setIsHeaderLink(true);
        alkootId.setHeaderLinkTitle("Alkoot ID");
        alkootId.setDBColumnName("tpa_enrollment_id");
        addColumn(alkootId);
        
        Column qatarId = new Column("Qatar ID");  //  old Alkoot ID
        qatarId.setMethodName("getAlkootId");
        qatarId.setColumnWidth("10%");
        qatarId.setIsHeaderLink(true);
        qatarId.setHeaderLinkTitle("Qatar ID");
        qatarId.setDBColumnName("emirate_id");
        addColumn(qatarId);
        
        Column relationship = new Column("Relationship");
        relationship.setMethodName("getRelationship");
        relationship.setColumnWidth("10%");
        relationship.setIsHeaderLink(true);
        relationship.setHeaderLinkTitle("Relationship");
        relationship.setDBColumnName("relship_description");
        addColumn(relationship);
        
        Column inceptionDate = new Column("Inception Date");
        inceptionDate.setMethodName("getInceptionDate");
        inceptionDate.setColumnWidth("10%");
        inceptionDate.setIsHeaderLink(true);
        inceptionDate.setHeaderLinkTitle("Inception Date");
        inceptionDate.setDBColumnName("Inception_Date");
        addColumn(inceptionDate);
        
        Column policyNo = new Column("Policy No");
        policyNo.setMethodName("getPolicyNo");
        policyNo.setColumnWidth("10%");
        policyNo.setIsHeaderLink(true);
        policyNo.setHeaderLinkTitle("Policy No");
        policyNo.setDBColumnName("policy_number");
        addColumn(policyNo);
        
        Column policyStartDate = new Column("Policy Start Date");
        policyStartDate.setMethodName("getPolicyStartDate");
        policyStartDate.setColumnWidth("10%");
        policyStartDate.setIsHeaderLink(true);
        policyStartDate.setHeaderLinkTitle("Policy Start Date");
        policyStartDate.setDBColumnName("Policy_Start_Date");
        addColumn(policyStartDate);
        
        Column policyEndDate = new Column("Policy End Date");
        policyEndDate.setMethodName("getPolicyEndDate");
        policyEndDate.setColumnWidth("10%");
        policyEndDate.setIsHeaderLink(true);
        policyEndDate.setHeaderLinkTitle("Policy End Date");
        policyEndDate.setDBColumnName("Policy_end_Date");
        addColumn(policyEndDate);
        
        Column groupId = new Column("Group ID");
        groupId.setMethodName("getGroupId");
        groupId.setColumnWidth("8%");
        groupId.setIsHeaderLink(true);
        groupId.setHeaderLinkTitle("Group ID");
        groupId.setDBColumnName("group_id");
        addColumn(groupId);
        
        Column modifiedDate = new Column("Modified Date");
        modifiedDate.setMethodName("getModifiedDate");
        modifiedDate.setColumnWidth("10%");
        modifiedDate.setIsHeaderLink(true);
        modifiedDate.setHeaderLinkTitle("Modified Date");
        modifiedDate.setDBColumnName("Modified_Date");
        addColumn(modifiedDate);
        
        Column modifiedBy = new Column("Modified By (User ID)");
        modifiedBy.setMethodName("getModifiedBy");
        modifiedBy.setColumnWidth("12%");
        modifiedBy.setIsHeaderLink(true);
        modifiedBy.setHeaderLinkTitle("Modified By");
        modifiedBy.setDBColumnName("Modified_By");
        addColumn(modifiedBy);
    }

}

