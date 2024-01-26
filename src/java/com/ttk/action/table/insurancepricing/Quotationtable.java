/**
 * @ (#) PreAuthTable.javaMay 5, 2006
 * Project      : TTK HealthCare Services
 * File         : PreAuthTable.java
 * Author       : Chandrasekaran J
 * Company      : Span Systems Corporation
 * Date Created : May 5, 2006
 *
 * @author       :  Chandrasekaran J
 * Modified by   :
 * Modified date :
 * Reason        :
 */

package com.ttk.action.table.insurancepricing;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;


public class Quotationtable extends Table
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
   

        //Setting properties for Cashless No.
        Column colQuotationNo=new Column("Quotation No.");
        colQuotationNo.setMethodName("getQuotationNo");
        colQuotationNo.setColumnWidth("25%");
       colQuotationNo.setIsLink(true);
        colQuotationNo.setIsHeaderLink(true);
        colQuotationNo.setHeaderLinkTitle("Sort by:Quotation No.");
        colQuotationNo.setDBColumnName("QUOT_NO");
        addColumn(colQuotationNo);
        
        Column colQuodate=new Column("Quotation Generated date/time");
        colQuodate.setMethodName("getQuoGeneratedDate");
        colQuodate.setColumnWidth("25%");
        colQuodate.setIsHeaderLink(true);
        colQuodate.setDBColumnName("GENERATED_DATE");
        addColumn(colQuodate);


        
    /*    Column colImage1 = new Column("Quoatation File");
        colImage1.setMethodName("getPolicydocYN");
        colImage1.setIsLink(true);
        colImage1.setIsHeaderLink(true);
        colImage1.setImageName("getFiledocimageYN");
        colImage1.setImageTitle("getFiledoctitle");
        colImage1.setHeaderLinkTitle("Sort by:Priority");
        colImage1.setDBColumnName("POL_COPY_DOC");
        colImage1.setColumnWidth("25%");
        addColumn(colImage1);*/
        

        //Setting properties for  Enrollment Id.
        Column colQuoFile=new Column("Final Quotation");
        colQuoFile.setMethodName("getFinalpolicydocYN");
        colQuoFile.setColumnWidth("25%");
        colQuoFile.setIsHeaderLink(true);
       // colQuoFile.setHeaderLinkTitle("Sort by: Enrollment Id");
        colQuoFile.setDBColumnName("FNL_DOC_APRV_YN");
        addColumn(colQuoFile);
        
       

        //Setting properties for check box
       /* Column colSelect = new Column("Select");
        colSelect.setComponentType("radio");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);*/
        

        
    }// end of public void setTableProperties()

}// end of PreAuthTable class
