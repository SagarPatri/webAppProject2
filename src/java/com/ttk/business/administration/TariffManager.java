/**
 * @ (#)  TariffManager.java Oct 3, 2005
 * Project      : TTKPROJECT
 * File         : TariffManager.java
 * Author       : Suresh.M
 * Company      : Span Systems Corporation 
 * Date Created : Oct 3, 2005
 *
 * @author       :  Suresh.M
 * Modified by   : 
 * Modified date : 
 * Reason        : 
 */

package com.ttk.business.administration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Clob;
import java.util.ArrayList;

import com.ttk.common.TTKCommon;
import com.ttk.common.exception.TTKException;
import com.ttk.dto.administration.RateVO;
import com.ttk.dto.administration.RevisionPlanVO;
import com.ttk.dto.administration.TariffItemVO;
import com.ttk.dto.administration.TariffPlanVO;
import com.ttk.dto.administration.TariffUploadVO;
import com.ttk.dto.preauth.ActivityDetailsVO;

import javax.ejb.*;
@Local

public interface TariffManager {
    
    /**
     * This method returns the ArrayList, which contains the TariffPlanVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of TariffPlanVO object's which contains the tariff plan details
     * @exception throws TTKException
     */
    public ArrayList getTariffPlanList(ArrayList alSearchObjects) throws TTKException;
    
    /**
     * This method adds/updates the details  TariffPlanVO which contains tariff plan details
     * @param tariffPlanVO the details of tariff plan which has to be added or updated 
     * @return long value, Plan Seq Id
     * @exception throws TTKException
     */
    public long addUpdateTariffPlan(TariffPlanVO tariffPlanVO) throws TTKException;
    
    /**
     * This method delete's the tariff plan's records from the database.  
     * @param alTariffPlanList ArrayList object which contains the tariff plans id's to be deleted
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int deleteTariffPlan(ArrayList alTariffPlanList) throws TTKException;
    
    /**
     * This method returns the ArrayList, which contains the TariffItemVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @param strIdentifier String which contains the Identifier for identifying Administration/Pre-Authorization flow
     * @return ArrayList of TariffItemVO's object's which contains the tariff item details
     * @exception throws TTKException
     */
    public ArrayList getTariffItemList(ArrayList alSearchObjects,String strIdentifier) throws TTKException;
    
    /**
     * This method adds/updates the details  TariffItemVO  which contains the details of tariff items
     * @param tariffItemVO TariffItemVO the details of tariff Item details which has to be added or updated 
     * @return long value, Package Sequence Id
     * @exception throws TTKException
     */
    public long addUpdateTariffItem(TariffItemVO tariffItemVO) throws TTKException;
    
    /**
     * This method deletes concerned TariffItem details from the database
     * @param strPkgSeqID which contains the Pkg Seq id's of the TariffItem's
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int deleteTariffItem(String strPkgSeqID) throws TTKException;
    
    /**
     * This method returns the ArrayList, which contains the ProcedureCodeVO which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of ProcedureCodeVO's object's which contains the procedure details
     * @exception throws TTKException
     */	
    public ArrayList getProcedureList(ArrayList alSearchObjects) throws TTKException;
    
    /**
     * This method adds/updates the RateVO which contains room rates details
     * @param rateVO the details which has to be added or updated
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int addUpdateRates(RateVO rateVO) throws TTKException;
    
    /**
     * This method returns the ArrayList, which contains the RevisionPlanVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of RevisionPlanVO object's which contains the tariff revision plan details
     * @exception throws TTKException
     */
    public ArrayList getRevisionPlanList(ArrayList alSearchObjects) throws TTKException;

    /**
     * This method adds the RevisionPlanVO which contains the details of the revision of the plan
     * @param revisionPlanVO the details which has to be added 
     * @return Long value the revised plan sequence id which is added/updated
     * @exception throws TTKException
     */
    public Long addRevisionPlan(RevisionPlanVO revisionPlanVO) throws TTKException;
    
    /**
     * This method returns the ArrayList, which contains the PlanPackageVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of PlanPackageVO object's which contains the tariff package details
     * @exception throws TTKException
     */
    public ArrayList getPackageList(ArrayList alSearchObjects) throws TTKException;
    
    /**
     * This method returns the ArrayList object, which contains all the details about the Room Rates
     * @param lngRevisedPlanSeqId long Revised Plan Seq Id
     * @param lngPkgSeqId long Package Seq Id
     * @param strGeneralTypeId String  General Type Id for Package/Non Package
     * @param strWardTypeId String  Ward Type Id for Ward Charges, for packages ward type id is null
     * @return ArrayList object which contains all the details about the Room Rates
     * @exception throws TTKException 
     */
    public ArrayList getRates(long lngRevisedPlanSeqId,long lngPkgSeqId,String strGeneralTypeId,String strWardTypeId) throws TTKException;
    
    /**
     * This method will sets the package as available or not available in the Package records in the database
     * @param alPackageList ArrayList which contains the details of the packages, which are to be available or not available
     * @param strAvblGnrlTypeId String which contains the available general type id for available or notavailable the package  
     * @return int value, greater than zero indicates sucessfull execution of the task
     * @exception throws TTKException
     */
    public int updateAvailabilityStatus(ArrayList alPackageList,String strAvblGnrlTypeId) throws TTKException;
    
    /** This method returns the TariffItemVO Which contains the details of the tariff item 
     * @param lngPkgSeqId for which the tariff item detail's has to be fetched
     * @return TariffItemVO which contains the details of Tariff Item
     * @exception throws TTKException
     */
    public TariffItemVO getTariffItemDetail(long lngPkgSeqId) throws TTKException;
    
    /**
     * This method returns the ArrayList, which contains the HospitalTariffPlanDetailVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of HospitalTariffPlanDetailVO object's which contains the hospital's tariff plan details
     * @exception throws TTKException
     */
    public ArrayList getHospitalTariffDetailList(ArrayList alSearchObjects) throws TTKException;
    
    /**
     * This method returns the ArrayList, which contains the RevisionPlanVO's which are populated from the database
     * @param alSearchObjects ArrayList object which contains the search criteria
     * @return ArrayList of RevisionPlanVO object's which contains the tariff revision plan details
     * @exception throws TTKException
     */
    public ArrayList getHospitalRevisionPlanList(ArrayList alSearchObjects) throws TTKException;
    
    /**
     * This method returns the ArrayList object, which contains all the details about the Insurance Companies Associated to Hospital
     * @param lngHospSeqId long Package Hospital Seq Id
     * @return ArrayList object which contains Name&ID Details of the Insurance Companies
     * @exception throws TTKException 
     */
    public ArrayList getAssociatedCompanyList(long lngHospSeqId) throws TTKException;
    
    /**
     * This method returns the ArrayList object, which contains all the details about the Insurance Companies Associated to Hospital
     * @param userid 
     * @param fileName 
     * @param lngHospSeqId long Package Hospital Seq Id
     * @return ArrayList object which contains Name&ID Details of the Insurance Companies
     * @exception throws TTKException 
     */
   // public Clob uploadTariffEmpanelment(FileReader file,int length) throws TTKException;
    public String uploadTariffEmpanelment(InputStream inputStream2, String fileName, Long userid,String hospitalseqid,String insuranceCode, String tariffType,String networkCategory, String providerForNetwork, Long hospGroupSeqId) throws TTKException;
    
    
    // public Clob uploadPharmacyTariffEmpanelment(FileReader file,int length) throws TTKException;
    public Clob uploadPharmacyTariffEmpanelment(InputStream inputStream2) throws TTKException;

    /*
     * Search Upladed tariffs in ADministration
     * 
     */
    public ArrayList getTariffSearchList(ArrayList alSearchObjects,String strIdentifier) throws TTKException;
    
    /*
     * Search Upladed pharmacy tariffs in ADministration
     * 
     */
    public ArrayList getPharmacyTariffSearchList(ArrayList alSearchObjects,String strIdentifier) throws TTKException;

    
    /*
     * Get the Tariff Details for Edit
     * 
     */
    public TariffUploadVO getTariffDetail(long TariffSeqId,String tariffType,long addedBy) throws TTKException;
    
    /*
     *  Edit and Update tariff Items
     * 
     */
    public long editTariffItem(TariffUploadVO tariffUploadVO,long addedBy) throws TTKException;
    
    /*
     *  Edit and Update tariff Items
     * 
     */
    public long editBulkTariffItem(String tariffTypeSearch,String selectedIds,float discAmount,long addedBy) throws TTKException;
    
    /*
     * get All Service Types for Edit and Update Service Types
     * 
     */
    public ArrayList<String> getAllServiceNames(String networkType,String providerCode,String payerCode) throws TTKException;
    
    /*
     * get Pharmacy Service Types for Edit and Update Service Types
     * 
     */
    public ArrayList<String> getPharmacyServiceNames(String networkType,String providerCode,String payerCode) throws TTKException;
    
    /*
     *  Update Service Types
     * 
     */

	public int updateServiceTypesByNetworks(String tarifFlag,StringBuffer strService, String networkType,Long userSeqId, String providerCode, String payerCode,String corpCode) throws TTKException;
    
    public Object[] getTariffUploadedData(String hospSeqId) throws TTKException;
    
    public Object[] getPharmacyTariffUploadedData(String hospSeqId) throws TTKException;

	public ArrayList getActivityCodeList(ArrayList<Object> searchData,String swtch) throws TTKException;

	public Clob uploadBulkModifyTariff(FileInputStream inputStream) throws TTKException;

	public String[] uploadingTariff(String batchRefNo, Long userSeqId) throws TTKException;

	 public ArrayList<Object> getTariffUploadErrorLogList(ArrayList<Object> alSearchCriteria) throws TTKException;

    
    
}// end of interface TariffManager.
