package com.ttk.action.table.onlineforms.pbmPharmarcy;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class PBMClaimUploadErrorLogTable extends Table {

	@Override
	public void setTableProperties() {
		

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
	}

}
