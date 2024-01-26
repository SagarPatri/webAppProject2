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
 * @author Vishwabandhu Kumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FastTrackPaymentTable extends Table {
	
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
	
	/*Column colFromDay =new Column("Fast Track Discount Payment From Day");           
	colFromDay.setMethodName("getfDays");                            
	colFromDay.setColumnWidth("18%");            
	colFromDay.setHeaderLinkTitle("Fast Track Discount Payment From Day");
	colFromDay.setIsHeaderLink(true);       
	colFromDay.setHeaderLinkTitle("Sort by: From Day");  
	colFromDay.setDBColumnName("F_FST_FROM_DAYS");                       
	addColumn(colFromDay);*/
	
	Column colToDate =new Column("Claim Processed Within");           
	colToDate.setMethodName("getfToDays");                            
	colToDate.setColumnWidth("12%");       
	//colToDate.setHeaderLinkTitle("Fast Track Discount Payment To Day");
	/*colToDate.setIsHeaderLink(true);          
	colToDate.setHeaderLinkTitle("Sort by: To Day");*/
	colToDate.setDBColumnName("F_FST_TO_DAYS");                       
	addColumn(colToDate);
	
	Column colDiscount=new Column("Discount (in %)");                    
	colDiscount.setMethodName("getfDiscountPerc");
	colDiscount.setColumnWidth("8%");
	/*colDiscount.setIsHeaderLink(true);
	colDiscount.setHeaderLinkTitle("Sort by: Discount");*/
	colDiscount.setDBColumnName("F_DISC_PERC");
	addColumn(colDiscount);
	
	Column colStartDate =new Column("Start Date");           
	colStartDate.setMethodName("getfStartDate");                           
	colStartDate.setColumnWidth("10%");   
	/*colStartDate.setIsHeaderLink(true);
	colStartDate.setHeaderLinkTitle("Sort by: Start Date"); */
	colStartDate.setDBColumnName("F_START_DATE");               
	addColumn(colStartDate);
	
	Column colEndDate =new Column("End Date");           
	colEndDate.setMethodName("getfEndDate");                  
	colEndDate.setColumnWidth("10%");                    
	/*colEndDate.setIsHeaderLink(true);                    
	colEndDate.setHeaderLinkTitle("Sort by: End Date");*/
	colEndDate.setDBColumnName("F_END_DATE");            
	addColumn(colEndDate);
	
	Column colAddedBy =new Column("Added By");           
	colAddedBy.setMethodName("getfAddedBy");                  
	colAddedBy.setColumnWidth("10%");                    
	colAddedBy.setDBColumnName("ADDED_BY");            
	addColumn(colAddedBy);
	
	Column colUpdatedDate =new Column("Configured / Updated Date");           
	colUpdatedDate.setMethodName("getfUpdatedDate");                  
	colUpdatedDate.setColumnWidth("15%");
	colUpdatedDate.setDBColumnName("ADDED_DATE");            
	addColumn(colUpdatedDate);
	
	Column colStatus =new Column("Status");           
	colStatus.setMethodName("getfStatus");                  
	colStatus.setColumnWidth("10%");                    
	colStatus.setIsHeaderLink(false);                    
	colStatus.setIsLink(true);
	colStatus.setDBColumnName("F_STATUS");            
	addColumn(colStatus);
    	

	}//end of public void setTableProperties()
}//end of class FastTrackPaymentTable

