package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;

public class ProviderListTable extends Table{

	
	/**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
	
	@Override
	public void setTableProperties() {

		setRowCount(50);
        setCurrentPage(1);
        setPageLinkCount(10);

        //Setting properties for Diagnosis
        Column colDiagnosis = new Column("Provider License Id");
        colDiagnosis.setMethodName("getProviderId");
        colDiagnosis.setColumnWidth("15%");
        colDiagnosis.setIsLink(true);
        colDiagnosis.setDBColumnName("HOSP_LICENC_NUMB");
        addColumn(colDiagnosis);

        //Setting properties for DiagnosisType
        Column colDiagnosisType = new Column("Provider Name");
        colDiagnosisType.setMethodName("getProviderName");
        colDiagnosisType.setColumnWidth("15%");
        colDiagnosisType.setDBColumnName("HOSP_NAME");
        addColumn(colDiagnosisType);
        
      //Setting properties for Empanelment No
        Column colEmpanelNo = new Column("Empanelment No");
        colEmpanelNo.setMethodName("getEmpanelmentNo");
        colEmpanelNo.setColumnWidth("15%");
        colEmpanelNo.setDBColumnName("EMPANEL_NUMBER");
        addColumn(colEmpanelNo);
	}

}
