/**
 * @ (#) FastTrackPaymentTable.java 4th Jan Apr, 2019
 * Project        		: Vidal Health TPA
 * File          			: FastTrackPaymentTable.java
 * Author       		: Vishwabandhu Kumar
 * Company      	: Vidal Health TPA
 * Date Created  : 4th Jan Apr, 2019
 *
 * @author       : 
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */
package com.ttk.action.table.empanelment;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

/**
 * @author sandra_bhaskar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VolumeBasedPaymentTable extends Table {
	
	private static final long serialVersionUID = 1L;

	/**
	 * This creates the columnproperties objects for each and 
	 * every column and adds the column object to the table
	 */

	public void setTableProperties()
	{
	setRowCount(12);
	setCurrentPage(1);
	setPageLinkCount(12);
	Column colDiscountType =new Column("Discount Type");           
	colDiscountType.setMethodName("getvDiscountType");                            
	colDiscountType.setColumnWidth("8%");                    
	/*colDiscountType.setIsHeaderLink(true);                    
	colDiscountType.setHeaderLinkTitle("Sort by: Discount Type");*/
	colDiscountType.setDBColumnName("V_DISC_TYPE");                       
	addColumn(colDiscountType);
	
	Column colAmountStartRange=new Column("Discount Volume Amount Start Range");                    
	colAmountStartRange.setMethodName("getvAmountStartRange");
	colAmountStartRange.setColumnWidth("12%");
	colAmountStartRange.setHeaderLinkTitle("Discount Volume Amount Start Range");
	/*colAmountStartRange.setIsHeaderLink(true);
	colAmountStartRange.setHeaderLinkTitle("Sort by: Amount Start Range");*/
	colAmountStartRange.setDBColumnName("V_DISC_VOL_AMT_ST");
	addColumn(colAmountStartRange);
	
	Column colAmountEndRange =new Column("Discount Volume Amount End Range");           
	colAmountEndRange.setMethodName("getvAmountEndRange");                           
	colAmountEndRange.setColumnWidth("12%");   
	colAmountEndRange.setHeaderLinkTitle("Discount Volume Amount End Range");
	/*colAmountEndRange.setIsHeaderLink(true);
	colAmountEndRange.setHeaderLinkTitle("Sort by: Amount End Range");*/
	colAmountEndRange.setDBColumnName("V_DISC_VOL_AMT_ED");               
	addColumn(colAmountEndRange);
	
	Column colDiscountPerc =new Column("Discount  (in %)");           
	colDiscountPerc.setMethodName("getvDiscountPerc");                  
	colDiscountPerc.setColumnWidth("8%");                    
	/*colDiscountPerc.setIsHeaderLink(true);                    
	colDiscountPerc.setHeaderLinkTitle("Sort by: Discount");*/
	colDiscountPerc.setDBColumnName("V_DISC_PERC");            
	addColumn(colDiscountPerc);
	
	
	/*Column colPeriodStartDate =new Column("Period Start Date ...");           
	colPeriodStartDate.setMethodName("getvPeriodStartDate");                  
	colPeriodStartDate.setColumnWidth("10%");   
	colAmountStartRange.setHeaderLinkTitle("Volume Discount Period Start Date");
	colPeriodStartDate.setIsHeaderLink(true);                    
	colPeriodStartDate.setHeaderLinkTitle("Sort by: Period Start Date");
	colPeriodStartDate.setDBColumnName("V_VDP_START_DATE");            
	addColumn(colPeriodStartDate);*/
    
	Column colStartDate =new Column("Start Date");           
	colStartDate.setMethodName("getvStartDate");                  
	colStartDate.setColumnWidth("8%");                    
	//colStartDate.setIsHeaderLink(true);                    
	colStartDate.setDBColumnName("V_START_DATE");            
	addColumn(colStartDate);
	
	Column colEndDate =new Column("End Date");           
	colEndDate.setMethodName("getvEndDate");                  
	colEndDate.setColumnWidth("8%");                    
	//colEndDate.setIsHeaderLink(true);                    
	colEndDate.setDBColumnName("V_END_DATE");            
	addColumn(colEndDate);

	Column colStatus =new Column("Status");           
	colStatus.setMethodName("getvStatus");                  
	colStatus.setColumnWidth("8%");                    
	colStatus.setIsHeaderLink(false);                    
	colStatus.setIsLink(true);
	colStatus.setDBColumnName("V_STATUS");            
	addColumn(colStatus);
	
/*	//Setting properties for image Delete
    Column deleteImg = new Column("Delete");
    deleteImg.setIsImage(true);
    deleteImg.setIsImageLink(true);
    deleteImg.setImageName("getDeleteImageName");
    deleteImg.setImageTitle("getDeleteImageTitle");
    deleteImg.setMethodName("getCopaySeqId");
    deleteImg.setColumnWidth("5%");
    addColumn(deleteImg);	*/	
}//end of public void setTableProperties()
}//end of class ProvierWiseCopayTable

