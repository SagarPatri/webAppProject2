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

package com.ttk.action.table.preauth;

import com.ttk.action.table.Column;
import com.ttk.action.table.Table;
import com.ttk.common.TTKCommon;
import com.ttk.common.TTKPropertiesReader;


public class PartnerPreAuthTable extends Table
{
    /**
     * This creates the columnproperties objects for each and
     * every column and adds the column object to the table
     */
    public void setTableProperties()
    {
        setRowCount(11);
        setCurrentPage(1);
        setPageLinkCount(10);

      //Setting properties for image Priority
       /* Column colImage4 = new Column("");
        colImage4.setIsImage(true);
        colImage4.setIsHeaderLink(true);
        colImage4.setImageName("getRejImageName");
        colImage4.setImageTitle("getRejImageTitle");
        colImage4.setDBColumnName("REJECT_COMPLETE_YN");
        addColumn(colImage4);*/
        
        //ADD IMAGE THINGS OVER HERE
        //Setting properties for image Priority


        //Setting properties for Cashless No.
        Column colPreAuthNo=new Column("Partner Reference No.");
        colPreAuthNo.setMethodName("getPreauthPartnerRefId");
        colPreAuthNo.setColumnWidth("15%");
        colPreAuthNo.setIsLink(true);
        colPreAuthNo.setIsHeaderLink(true);
        colPreAuthNo.setImageName("getShortfallImageName");
        colPreAuthNo.setImageTitle("getShortfallImageTitle");
		colPreAuthNo.setImageName2("getInvImageName"); 
        colPreAuthNo.setImageTitle2("getInvImageTitle");
        colPreAuthNo.setHeaderLinkTitle("Sort by:Partner Reference No.");
        colPreAuthNo.setDBColumnName(" onl_pre_auth_refno");
        addColumn(colPreAuthNo);
        
        Column colStatus=new Column("Partner Reference Status");
        colStatus.setMethodName("getStatus");
        colStatus.setColumnWidth("15%");
        colStatus.setIsHeaderLink(true);
        colStatus.setIsLink(true);
        colStatus.setIsHeaderLink(true);
        colStatus.setLinkParamName("SecondLink");
       // colStatus.setHeaderLinkTitle("Sort by: Hospital Name");
        colStatus.setDBColumnName("onl_pat_status");
        addColumn(colStatus);

        //Setting properties for Hospital Name.
        Column colHospitalName=new Column("Partner Name");
        colHospitalName.setMethodName("getPartnerName");
        colHospitalName.setColumnWidth("15%");
        colHospitalName.setIsHeaderLink(true);
        colHospitalName.setHeaderLinkTitle("Sort by: Partner Name");
        colHospitalName.setDBColumnName("partner_name");
        addColumn(colHospitalName);
        
        Column preAuthNo=new Column("Pre-Approval No.");
        preAuthNo.setMethodName("getPreAuthNo");
        preAuthNo.setColumnWidth("15%");
        preAuthNo.setIsHeaderLink(true);
        preAuthNo.setHeaderLinkTitle("Sort by: Partner Name");
        preAuthNo.setDBColumnName("partner_name");
        addColumn(preAuthNo);
        
        Column preAuthStatus=new Column("Pre-Approval Status");
        preAuthStatus.setMethodName("getPreauthStatus");
        preAuthStatus.setColumnWidth("15%");
        preAuthStatus.setIsHeaderLink(true);
        preAuthStatus.setHeaderLinkTitle("Sort by: Partner Name");
        preAuthStatus.setDBColumnName("partner_name");
        addColumn(preAuthStatus);
        

        //Setting properties for  Enrollment Id.
        Column colEnrollmentId=new Column("Al Koot ID");
        colEnrollmentId.setMethodName("getEnrollmentID");
        colEnrollmentId.setColumnWidth("10%");
        colEnrollmentId.setIsHeaderLink(true);
        colEnrollmentId.setHeaderLinkTitle("Sort by: Enrollment Id");
        colEnrollmentId.setDBColumnName("TPA_ENROLLMENT_ID");
        addColumn(colEnrollmentId);
        
        
        
        //Setting properties for  Qatar ID.
        Column colQatarId=new Column("Qatar ID");
        colQatarId.setMethodName("getsQatarId");
        colQatarId.setColumnWidth("10%");
        colQatarId.setIsHeaderLink(true);
        colQatarId.setHeaderLinkTitle("Sort by: Qatar ID");
        colQatarId.setDBColumnName("QATAR_ID");
        addColumn(colQatarId);
        
        
        
        
        //Setting properties for  Alternate Id.
       /* Column colAlternateId=new Column("Alternate Id");
        colAlternateId.setMethodName("getStrAlternateID");				// vo get() method name //
        colAlternateId.setColumnWidth("20%");
        colAlternateId.setIsHeaderLink(true);
        colAlternateId.setHeaderLinkTitle("Sort by: Alternate Id");	
        colAlternateId.setDBColumnName("TPA_ALTERNATE_ID");				// database column name	
        addColumn(colAlternateId);*/

        //Setting properties for  Received Date.
        Column colReceivedDate=new Column("Received Date");
        colReceivedDate.setMethodName("getReceivedDateAsString");
        colReceivedDate.setColumnWidth("10%");
        colReceivedDate.setIsHeaderLink(true);
        colReceivedDate.setHeaderLinkTitle("Sort by: Received Date");
        colReceivedDate.setDBColumnName("added_date");
        addColumn(colReceivedDate);
        
      //Setting properties for  Admission Date.
        Column colCllaimAdmDate=new Column("Admission Date");
        colCllaimAdmDate.setMethodName("getClaimAdmissionDate");
        colCllaimAdmDate.setColumnWidth("10%");
        colCllaimAdmDate.setIsHeaderLink(true);
        colCllaimAdmDate.setHeaderLinkTitle("Sort by: Admission Date");
        colCllaimAdmDate.setDBColumnName("admission_date");
        addColumn(colCllaimAdmDate);
        
      //Setting properties for  Discharge Date.
        Column colCllaimDisrgDate=new Column("Discharge Date");
        colCllaimDisrgDate.setMethodName("getClaimDischargeDate");
        colCllaimDisrgDate.setColumnWidth("10%");
        colCllaimDisrgDate.setIsHeaderLink(true);
        colCllaimDisrgDate.setHeaderLinkTitle("Sort by: Discharge Date");
        colCllaimDisrgDate.setDBColumnName("DISCHARGE_DATE");
        addColumn(colCllaimDisrgDate);

        //Setting properties for image Preauth
       /* Column colImage2 = new Column("");
        colImage2.setIsImage(true);
        colImage2.setIsImageLink(true);
        colImage2.setImageName("getPreAuthImageName");
        colImage2.setImageTitle("getPreAuthImageTitle");
		colImage2.setVisibility(false);
        addColumn(colImage2);

        //Setting properties for image Assign
        Column colImage3 = new Column("");
        colImage3.setIsImage(true);
        colImage3.setIsImageLink(true);
        colImage3.setImageName("getAssignImageName");
        colImage3.setImageTitle("getAssignImageTitle");
        colImage3.setVisibility(false);
        addColumn(colImage3);
		
		//Koc 11 koc11
        Column colImage5 = new Column("");
        colImage5.setIsImage(true);
        colImage5.setIsImageLink(true);
        colImage5.setImageName("getInvestigationImageName");
        colImage5.setImageTitle("getInvestigationImageTitle");        
        colImage5.setVisibility(false);
        addColumn(colImage5); */

        //Setting properties for check box
        Column colSelect = new Column("Select");
        colSelect.setComponentType("checkbox");
        colSelect.setComponentName("chkopt");
        addColumn(colSelect);
    }// end of public void setTableProperties()

}// end of PreAuthTable class
