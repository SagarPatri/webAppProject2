/**
 * @ (#) ProvierWiseCopayTable.java 23rd Apr, 2018
 * Project       : Vidal Health
 * File          : ProvierWiseCopayTable.java
 * Author        : Kishor kumar S H
 * Company       : Vidal Health
 * Date Created  : Dec 05th, 2005
 *
 * @author       : Bhaskar Sandra
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */
package com.ttk.action.table.administration;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * @author sandra_bhaskar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProvierWiseCopayTable extends Table {
	
	private static final long serialVersionUID = 1L;

	/**
	 * This creates the columnproperties objects for each and 
	 * every column and adds the column object to the table
	 */

	public void setTableProperties()
	{
	setRowCount(5);
	setCurrentPage(1);
	setPageLinkCount(5);
	Column colBenefitType =new Column("Benefit Type");           
	colBenefitType.setMethodName("getBenefitType");                            
	colBenefitType.setColumnWidth("10%");                    
	colBenefitType.setIsHeaderLink(true);                    
	colBenefitType.setHeaderLinkTitle("Sort by: Benefit Type");  
	colBenefitType.setDBColumnName("BENEFIT_TYPE");                       
	addColumn(colBenefitType);
	
	Column colServiceType=new Column("Service Type");                    
	colServiceType.setMethodName("getServiceType");
	colServiceType.setColumnWidth("45%");
	colServiceType.setIsHeaderLink(true);
	colServiceType.setHeaderLinkTitle("Sort by: Service Type");
	colServiceType.setDBColumnName("SERVICE_TYPE");
	addColumn(colServiceType);
	
	Column colCopay =new Column("Copay");           
	colCopay.setMethodName("getCopayPerc");                           
	colCopay.setColumnWidth("10%");   
	colCopay.setIsHeaderLink(true);
	colCopay.setHeaderLinkTitle("Sort by: Copay"); 
	colCopay.setDBColumnName("COPAY_PERC");               
	addColumn(colCopay);
	
	Column colDeductibleAmount =new Column("Deductible Amount");           
	colDeductibleAmount.setMethodName("getDeductible");                  
	colDeductibleAmount.setColumnWidth("10%");                    
	colDeductibleAmount.setIsHeaderLink(true);                    
	colDeductibleAmount.setHeaderLinkTitle("Sort by: Deductible Amount");
	colDeductibleAmount.setDBColumnName("DEDUCT_AMOUNT");            
	addColumn(colDeductibleAmount);
	
	
	Column colSuffix =new Column("Suffix");           
	colSuffix.setMethodName("getSuffix");                  
	colSuffix.setColumnWidth("10%");                    
	colSuffix.setIsHeaderLink(true);                    
	colSuffix.setHeaderLinkTitle("Sort by: Suffix");
	colSuffix.setDBColumnName("PROVD_COPAY_DED_FLAG");            
	addColumn(colSuffix);
    
	Column colApplyRule =new Column("Apply Rule Wise...");           
	colApplyRule.setMethodName("getApplyRule");                  
	colApplyRule.setColumnWidth("10%");                    
	colApplyRule.setIsHeaderLink(true);                    
	colApplyRule.setHeaderLinkTitle("Apply Rule Wise Copay and Deductible");
	colApplyRule.setDBColumnName("RULE_EX_YN");            
	addColumn(colApplyRule);
	
	//Setting properties for image Delete
    Column deleteImg = new Column("Delete");
    deleteImg.setIsImage(true);
    deleteImg.setIsImageLink(true);
    deleteImg.setImageName("getDeleteImageName");
    deleteImg.setImageTitle("getDeleteImageTitle");
    deleteImg.setMethodName("getCopaySeqId");
    deleteImg.setColumnWidth("5%");
    addColumn(deleteImg);		
}//end of public void setTableProperties()
}//end of class ProvierWiseCopayTable

