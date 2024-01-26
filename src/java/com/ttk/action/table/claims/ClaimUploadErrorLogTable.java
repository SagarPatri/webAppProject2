/**
* @ (#) BenefitListTable.java
* Project       : TTK HealthCare Services
* File          : ClaimsTable.java
* Author        : Balaji C R B
* Company       : Span Infotech India Pvt. Ltd
* Date Created  : July 2,2008

* @author       : Balaji C R B
* Modified by   :
* Modified date :
* Reason :
*/

package com.ttk.action.table.claims;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ClaimUploadErrorLogTable extends Table
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

        //Setting properties for Reference No
        Column colXmlSeqId=new Column("Reference No");
        colXmlSeqId.setMethodName("getReferenceNo");
        colXmlSeqId.setColumnWidth("20%");
        colXmlSeqId.setIsHeaderLink(true);
        colXmlSeqId.setImageName("");
        colXmlSeqId.setImageTitle("");
        colXmlSeqId.setIsLink(true);
        colXmlSeqId.setLinkTitle("View Error Log");
        colXmlSeqId.setHeaderLinkTitle("Sort by: Reference No");
        colXmlSeqId.setDBColumnName("XML_SEQ_ID");
        addColumn(colXmlSeqId);

        //Setting properties for File Name.
        Column colFileName=new Column("File Name");
        colFileName.setMethodName("getFileName");
        colFileName.setColumnWidth("20%");
        colFileName.setIsHeaderLink(true);
        colFileName.setHeaderLinkTitle("Sort by: File Name");
        colFileName.setDBColumnName("FILE_NAME");
        addColumn(colFileName);

        //Setting properties for Added Date
        Column colAddedDate=new Column("Added Date");
        colAddedDate.setMethodName("getStrAddedDate");
        colAddedDate.setColumnWidth("20%");
        colAddedDate.setIsHeaderLink(true);
        colAddedDate.setHeaderLinkTitle("Sort by: Added Date");
        colAddedDate.setDBColumnName("ADDED_DATE");
        addColumn(colAddedDate);

    } //end of setTableProperties()
}// end of BenefitListTable